package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.DetailBillDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.config.VNPayConfig;
import com.greenhouse.model.Account;
import com.greenhouse.model.Bill;
import com.greenhouse.model.DetailBill;
import com.greenhouse.model.Product;
import com.greenhouse.service.SessionService;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Controller
@RequestMapping("/client/payment")
public class PaymentController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private BillDAO billDAO;
    @Autowired
    private DetailBillDAO detailBillDAO;
    @Autowired
    private ProductDAO productDAO;

    @PostMapping("/create-payment")
    public ResponseEntity<Map<String, Object>> payment(@RequestParam("orderType") String orderType,
            @RequestParam("totalAmount") Long amount,
            @RequestParam("checkedIDs") String checkedIDs, @RequestParam("codeBank") String codeBank) throws Exception {
        String returnUrl = "http://localhost:8081/client/payment/done-pay?cartIDs=" + checkedIDs + "&amount="
                + amount;
        if (orderType.equalsIgnoreCase("VNPay")) {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_ReturnUrl = returnUrl;
            long vnp_Amount = (long) (amount * 100);
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_CurrCode = "VND";
            String vnp_OrderInfo = "Thanh toan don hang:" + vnp_TxnRef;
            String bankCode = codeBank;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
            vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            // vnp_Params.put("vnp_OrderType", vnp_OrderType);
            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            vnp_Params.put("vnp_Locale", "vn");
            // String locate = language != null && !language.isEmpty() ? language : "vn";
            // vnp_Params.put("vnp_Locale", locate);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            // vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder queryUrl = new StringBuilder();
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
                    queryUrl.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    queryUrl.append('=');
                    queryUrl.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        queryUrl.append('&');
                        hashData.append('&');
                    }
                }
            }
            String query = queryUrl.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            query += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query;

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("code", "00");
            responseData.put("message", "success");
            responseData.put("data", paymentUrl);

            return ResponseEntity.ok(responseData);
        } else {
            String paymentUrl = returnUrl;
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("code", "00");
            responseData.put("message", "success");
            responseData.put("data", paymentUrl);
            return ResponseEntity.ok(responseData);
        }
    }

    @GetMapping("/done-pay")
    public String getDonPayment(Model model, @RequestParam("cartIDs") String cartIDs,
            @RequestParam("amount") long amount) {
        // set up value for insert
        BigDecimal totalBill = BigDecimal.valueOf(amount);
        Account account = sessionService.get("account");
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        // Insert value to bill
        Bill bill = new Bill();
        bill.setAccountId(account);
        bill.setBillDate(currentTimestamp);
        bill.setTotal(totalBill);
        billDAO.save(bill);
        // Foreach checkedIDs ( cart id )
        String[] checked = cartIDs.split(",");
        for (String string : checked) {
            var temp = cartDAO.findById(Integer.parseInt(string));
            temp.get().setStatus(true);
            cartDAO.save(temp.orElse(null));
            // Insert value to detail-bill
            DetailBill detailBill = new DetailBill();
            detailBill.setBill(bill);
            detailBill.setProductId(temp.get().getProductId());
            detailBill.setPrice(temp.get().getPrice());
            detailBill.setQuantity(temp.get().getQuantity());
            detailBillDAO.save(detailBill);
            // Set Instock Product
            Product product = productDAO.findById(temp.get().getProductId()).orElse(null);
            int newInStock = product.getInStock() - temp.get().getQuantity();
            product.setInStock(newInStock);
            productDAO.save(product);
        }

        // User Session - start
        Account acc = sessionService.get("account");
        if (acc != null) {
            try {
                int qty = accountDAO.findQuanityCartById(acc.getId());
                model.addAttribute("qtyCart", qty);
            } catch (Exception e) {
                model.addAttribute("qtyCart", 0);
            }
            model.addAttribute("sessionUsername", acc.getUsername());
        }
        // User Session - end
        return "client/donePay";
    }
}
