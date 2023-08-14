package com.greenhouse.restController.Client;

import java.io.ObjectInputFilter.Config;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.DTO.OrderDTO;
import com.greenhouse.config.VNPayConfig;
import com.greenhouse.model.Account;
import com.greenhouse.model.Bill;
import com.greenhouse.model.Discount;
import com.greenhouse.service.BillService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/client/rest/checkout")
public class restCheckout {

    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private DiscountDAO discountDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private BillService billService;
    @Autowired
    private BillDAO billDAO;
    @Autowired
    private HttpServletRequest request;

    private String orderIds;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getCart(@RequestParam String username) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";
        if (username != null) {
            Account acc = new Account();
            acc = accountDAO.findById(username).get();
            if (acc != null) {
                responseData.put("account", acc);
            } else {
                responseData.put("account", "");
            }
            if (orderIds != null) {
                String[] checked = orderIds.split(";");
                var carts = new ArrayList<>();
                Long totalAmount = (long) 0;
                for (String string : checked) {
                    var cart = cartDAO.getProductInCartByCartId(string);
                    carts.add(cart);
                    if (cartDAO.getAmountFromCart(string) != null) {
                        Long amount = Long.parseLong(cartDAO.getAmountFromCart(string).toString());
                        totalAmount += amount;
                    }
                }
                if (carts.size() > 0) {
                    status = "success";
                    message = "Lấy dữ liệu lên trang thanh toán thành công";
                    responseData.put("carts", carts);
                    responseData.put("totalAmount", totalAmount);
                }
            } else {
                status = "error";
                message = "Chưa chọn sản phẩm mà vào đây à, đấm phát giờ!!!";
            }
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("apply-discount")
    public ResponseEntity<Map<String, Object>> applyDiscount(@RequestParam String discountCode) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        Discount discount = new Discount();
        discount = discountDAO.findById(discountCode).orElse(null);
        if (discount != null && discount.getStatus() == true) {
            Date currentDate = new Date();
            if (currentDate.after(discount.getStartDate()) && currentDate.before(discount.getEndDate())) {
                if (discount.getQuantity() - discount.getUsedQuantity() > 0) {
                    status = "success";
                    message = "Mã giảm giá của bạn là: " + discount.getDiscountCode();
                    responseData.put("discount", discount);
                } else {
                    status = "error";
                    message = "Mã giảm giá [" + discount.getDiscountCode() + "] đã hết!!!";
                }
            } else {
                status = "error";
                message = "Mã giảm giá [" + discount.getDiscountCode() + "] không có hiệu lực";
            }

        } else {
            status = "error";
            message = "Mã giảm giá không tồn tại";
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("create-payment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody OrderDTO orderDTO) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";
        String paymentUrl = "";

        // Create bill
        if (orderDTO.getPaymentMethod().equals("COD")) {
            status = "COD";
            message = "Đơn hàng của bạn sẽ được xử lý, cảm ơn bạn đã mua hàng!";
            Bill bill = billService.createBillFromOrderDTO(orderDTO);
            bill.setStatus(1);
            billService.saveBillToDatabase(bill, orderDTO, orderIds);
            billService.subtractQuantity(orderIds);
            paymentUrl = "http://localhost:8081/client/checkout/donePay";
        } else {
            try {
                Bill bill = billService.createBillFromOrderDTO(orderDTO);
                billService.saveBillToDatabase(bill, orderDTO, orderIds);
                paymentUrl = createVnPayPaymentUrl(bill);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            status = "VNPAY";
            message = "Set value for url vnpay";
        }
        responseData.put("data", paymentUrl);
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    private String createVnPayPaymentUrl(Bill bill)
            throws UnsupportedEncodingException {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        long vnp_Amount = 100 * Optional.ofNullable(bill.getNewAmount()).orElse(bill.getAmount());
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_CurrCode = "VND";
        String vnp_IpAddr = getCustomerIP();
        String vnp_Local = "vn";
        String vnp_ReturnUrl = VNPayConfig.vnp_ReturnUrl;
        String vnp_TxnRef = String.format("%08d", bill.getId());
        String vnp_OrderInfo = "Thanh toan don hang:" + vnp_TxnRef;
        String vnp_OrderType = VNPayConfig.vnp_OrderType;
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", vnp_Local);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @PostMapping("/vnpay-ipn")
    public ResponseEntity<Map<String, String>> processVnpayIpn(@RequestBody Map<String, String> vnpayData) {
        Map<String, String> responseData = new HashMap<>();
        try {

            // Check if the vnp_TxnRef exists in your database (checkOrderId)
            int billId = Integer.parseInt(vnpayData.get("vnp_TxnRef"));
            Bill bill = billDAO.getBillById(billId);

            boolean checkOrderId = true;
            if (bill == null) {
                checkOrderId = false;
            }

            // Check if the vnp_Amount is valid (Check vnp_Amount returned by VNPAY compared
            // to the amount in your database for the code vnp_TxnRef)
            Long amountBill = bill.getNewAmount();
            boolean checkAmount = true;
            if (amountBill != Long.parseLong(vnpayData.get("vnp_Amount")) / 100) {
                checkAmount = false;
            }

            // Check if the payment status is pending (PaymnentStatus = 0)
            boolean checkOrderStatus = true;
            if (bill.getStatus() != 0) {
                checkOrderStatus = false;
            }
            if (checkOrderId) {
                if (checkAmount) {
                    if (checkOrderStatus) {
                        if ("00".equals(vnpayData.get("vnp_ResponseCode"))) {
                            bill.setStatus(1);
                            billDAO.save(bill);
                            billService.subtractQuantity(orderIds);
                            responseData.put("RspCode", "00");
                            responseData.put("Message", "Xác nhận thành công");
                            orderIds = null;
                        } else {
                            bill.setStatus(2);
                            billDAO.save(bill);
                            String errorMessage = "";
                            switch (vnpayData.get("vnp_ResponseCode")) {
                                case "07":
                                    errorMessage = "Giao dịch bị nghi ngờ, giao dịch bất thường.";
                                    break;
                                case "09":
                                    errorMessage = "Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking.";
                                    break;
                                case "10":
                                    errorMessage = "Khách hàng xác thực thông tin tài khoản không đúng quá 3 lần.";
                                    break;
                                case "11":
                                    errorMessage = "Đã hết hạn chờ thanh toán. Vui lòng thực hiện lại giao dịch.";
                                    break;
                                case "12":
                                    errorMessage = "Tài khoản của khách hàng bị khóa.";
                                    break;
                                case "13":
                                    errorMessage = "Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Vui lòng thực hiện lại giao dịch.";
                                    break;
                                case "24":
                                    errorMessage = "Khách hàng hủy giao dịch.";
                                    break;
                                case "51":
                                    errorMessage = "Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
                                    break;
                                case "65":
                                    errorMessage = "Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
                                    break;
                                case "75":
                                    errorMessage = "Ngân hàng thanh toán đang bảo trì.";
                                    break;
                                case "79":
                                    errorMessage = "KH nhập sai mật khẩu thanh toán quá số lần quy định. Vui lòng thực hiện lại giao dịch.";
                                    break;
                                default:
                                    errorMessage = "Có lỗi xảy ra trong quá trình xử lý giao dịch.";
                            }
                            responseData.put("RspCode", vnpayData.get("vnp_ResponseCode"));
                            responseData.put("Message", errorMessage);
                        }
                    } else {
                        responseData.put("RspCode", "02");
                        responseData.put("Message", "Đơn hàng đã được xác nhận trước đó");
                    }
                } else {
                    responseData.put("RspCode", "04");
                    responseData.put("Message", "Số tiền không hợp lệ");
                }
            } else {
                responseData.put("RspCode", "01");
                responseData.put("Message", "Không tìm thấy đơn hàng");
            }
            responseData.put("invoiceId", String.format("%08d", bill.getId()));
        } catch (Exception e) {
            responseData.put("RspCode", "99");
            responseData.put("Message", "Lỗi không xác định");
        }
        return ResponseEntity.ok(responseData);
    }

    private String getCustomerIP() {
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
    }

    @GetMapping("/temp")
    private ResponseEntity<Map<String, Object>> setCartsId(@RequestParam String checkedIDsStr) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        orderIds = checkedIDsStr;

        status = "success";
        message = "Lưu tạm carts id thành công";

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

}
