package com.example.watersystem.service;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.Payment;
import com.example.watersystem.repository.AdminUserRepository;
import com.example.watersystem.repository.InvoiceRepository;
import com.example.watersystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final AdminUserRepository adminUserRepository;

    /**
     * Lấy tất cả giao dịch thanh toán
     * @return Danh sách tất cả các giao dịch thanh toán
     */
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Lấy giao dịch thanh toán theo ID
     * @param id ID của giao dịch thanh toán
     * @return Optional chứa giao dịch thanh toán nếu tìm thấy
     */
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    /**
     * Lấy tất cả giao dịch thanh toán theo ID của hóa đơn
     * @param invoiceId ID của hóa đơn
     * @return Danh sách các giao dịch thanh toán của hóa đơn
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByInvoiceId(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .map(paymentRepository::findByInvoice)
                .orElse(List.of());
    }

    /**
     * Lấy tất cả giao dịch thanh toán theo ID của người quản trị
     * @param adminUserId ID của người quản trị
     * @return Danh sách các giao dịch thanh toán được thực hiện bởi người quản trị
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByAdminUserId(Long adminUserId) {
        return adminUserRepository.findById(adminUserId)
                .map(paymentRepository::findByAdminUser)
                .orElse(List.of());
    }

    /**
     * Ghi nhận một giao dịch thanh toán mới cho hóa đơn
     * @param invoiceId ID của hóa đơn
     * @param adminUserId ID của người quản trị thực hiện thanh toán
     * @param amount Số tiền thanh toán
     * @return Optional chứa giao dịch thanh toán mới nếu thành công
     */
    @Transactional
    public Optional<Payment> recordPayment(Long invoiceId, Long adminUserId, BigDecimal amount) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        Optional<AdminUser> adminUserOpt = adminUserRepository.findById(adminUserId);

        if (invoiceOpt.isPresent() && adminUserOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            AdminUser adminUser = adminUserOpt.get();

            // Tạo giao dịch thanh toán mới
            Payment payment = Payment.builder()
                    .invoice(invoice)
                    .adminUser(adminUser)
                    .amount(amount)
                    .paidDate(LocalDate.now())
                    .build();

            // Lưu giao dịch thanh toán
            Payment savedPayment = paymentRepository.save(payment);

            // Cập nhật trạng thái hóa đơn nếu đã thanh toán đủ
            updateInvoiceStatusIfFullyPaid(invoice);

            return Optional.of(savedPayment);
        }

        return Optional.empty();
    }

    /**
     * Cập nhật thông tin của giao dịch thanh toán
     * @param id ID của giao dịch thanh toán
     * @param paymentDetails Thông tin cập nhật
     * @return Optional chứa giao dịch thanh toán đã cập nhật nếu thành công
     */
    @Transactional
    public Optional<Payment> updatePayment(Long id, Payment paymentDetails) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    // Lưu hóa đơn hiện tại để kiểm tra sau khi cập nhật
                    Invoice currentInvoice = payment.getInvoice();

                    // Cập nhật thông tin
                    payment.setAmount(paymentDetails.getAmount());
                    payment.setPaidDate(paymentDetails.getPaidDate());

                    if (paymentDetails.getAdminUser() != null && paymentDetails.getAdminUser().getId() != null) {
                        adminUserRepository.findById(paymentDetails.getAdminUser().getId())
                                .ifPresent(payment::setAdminUser);
                    }

                    if (paymentDetails.getInvoice() != null && paymentDetails.getInvoice().getId() != null &&
                            !paymentDetails.getInvoice().getId().equals(currentInvoice.getId())) {
                        // Nếu hóa đơn thay đổi, cập nhật trạng thái của cả hóa đơn cũ và mới
                        invoiceRepository.findById(paymentDetails.getInvoice().getId())
                                .ifPresent(newInvoice -> {
                                    payment.setInvoice(newInvoice);
                                    paymentRepository.save(payment);

                                    // Cập nhật trạng thái của cả hóa đơn cũ và mới
                                    updateInvoiceStatusIfFullyPaid(currentInvoice);
                                    updateInvoiceStatusIfFullyPaid(newInvoice);
                                });

                        return payment;
                    } else {
                        // Nếu hóa đơn không thay đổi, chỉ cần lưu payment và cập nhật trạng thái
                        Payment savedPayment = paymentRepository.save(payment);
                        updateInvoiceStatusIfFullyPaid(currentInvoice);
                        return savedPayment;
                    }
                });
    }

    /**
     * Xóa giao dịch thanh toán
     * @param id ID của giao dịch thanh toán
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    @Transactional
    public boolean deletePayment(Long id) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    // Lưu hóa đơn trước khi xóa để cập nhật trạng thái sau
                    Invoice invoice = payment.getInvoice();

                    // Xóa giao dịch
                    paymentRepository.delete(payment);

                    // Cập nhật trạng thái hóa đơn
                    updateInvoiceStatusIfFullyPaid(invoice);

                    return true;
                }).orElse(false);
    }

    /**
     * Lấy các giao dịch thanh toán trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Danh sách các giao dịch thanh toán trong khoảng thời gian
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaidDateBetween(startDate, endDate);
    }

    /**
     * Lấy thống kê thanh toán theo ngày
     * @return Danh sách các cặp [ngày, tổng số tiền]
     */
    @Transactional(readOnly = true)
    public List<Object[]> getDailyPayments() {
        return paymentRepository.findDailyPayments();
    }

    /**
     * Lấy thống kê thanh toán theo tháng
     * @return Danh sách các cặp [tháng, tổng số tiền]
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyPayments() {
        return paymentRepository.findMonthlyPayments();
    }

    /**
     * Tính tổng số tiền đã thanh toán cho một hóa đơn
     * @param invoiceId ID của hóa đơn
     * @return Tổng số tiền đã thanh toán
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentsByInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .map(invoice -> {
                    BigDecimal total = BigDecimal.ZERO;
                    for (Payment payment : paymentRepository.findByInvoice(invoice)) {
                        total = total.add(payment.getAmount());
                    }
                    return total;
                })
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Kiểm tra xem một hóa đơn đã được thanh toán đủ chưa và cập nhật trạng thái
     * @param invoice Hóa đơn cần kiểm tra
     */
    private void updateInvoiceStatusIfFullyPaid(Invoice invoice) {
        BigDecimal totalPaid = BigDecimal.ZERO;
        for (Payment payment : paymentRepository.findByInvoice(invoice)) {
            totalPaid = totalPaid.add(payment.getAmount());
        }

        if (totalPaid.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus("DA_THANH_TOAN");
        } else {
            invoice.setStatus("CHUA_THANH_TOAN");
        }

        invoiceRepository.save(invoice);
    }

    /**
     * Lấy các giao dịch thanh toán có số tiền lớn hơn một giá trị
     * @param amount Giá trị so sánh
     * @return Danh sách các giao dịch thanh toán thỏa mãn
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsWithAmountGreaterThan(BigDecimal amount) {
        return paymentRepository.findByAmountGreaterThan(amount);
    }

    /**
     * Lấy các giao dịch thanh toán có số tiền lớn hơn trung bình
     * @return Danh sách các giao dịch thanh toán thỏa mãn
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsAboveAverage() {
        return paymentRepository.findPaymentsAboveAverage();
    }
}