package com.greenhouse.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.BillDetailDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.DTO.OrderDTO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Bill;
import com.greenhouse.model.BillDetail;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Discount;
import com.greenhouse.model.Product;

@Service
public class BillService {
    @Autowired
    private BillDAO billDAO;
    @Autowired
    private DiscountDAO discountDAO;
    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private BillDetailDAO billDetailDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private ProductDAO productDAO;

    public void saveBillToDatabase(Bill bill, OrderDTO orderDTO, String orderIds) {
        // Tạo và lưu Bill
        billDAO.save(bill);

        // Xử lí discount khi apply discount success và thanh toán thành công
        if (orderDTO.getDiscountCode() != null) {
            Discount discount = discountDAO.findById(orderDTO.getDiscountCode()).orElse(null);
            discount.setUsedQuantity(discount.getUsedQuantity() + 1);
            discountDAO.save(discount);
        }

        // Xử lí bill detail khi create bill
        String[] checked = orderIds.split(";");
        for (String string : checked) {
            Cart cart = cartDAO.findById(Integer.parseInt(string)).orElse(null);
            BillDetail billDetail = createBillDetailFromCart(cart, bill);
            billDetailDAO.save(billDetail);
            // Xử lí giỏ hàng sau khi thêm vào hóa đơn chi tiết (detail bill)
            cart.setStatus(false);
            cartDAO.save(cart);
        }
    }

    public Bill createBillFromOrderDTO(OrderDTO orderDTO) {
        Boolean isAppliedDiscount = orderDTO.getDiscountCode() != null;
        Account acc = accountDAO.findById(orderDTO.getUsername()).orElse(null);
        Date createDate = orderDTO.getCreateDate();
        long amount = orderDTO.getAmount();
        String discountCode = orderDTO.getDiscountCode();
        double discountPercent = isAppliedDiscount ? orderDTO.getDiscountPercent() : 0;
        long newAmount = (long) (amount * (1 - discountPercent / 100));
        String paymentMethod = orderDTO.getPaymentMethod();
        String receiverAddress = orderDTO.getReceiverAddress();
        String receiverFullname = orderDTO.getReceiverFullname();
        String receiverPhone = orderDTO.getReceiverPhone();

        Bill bill = new Bill();
        bill.setAccount(acc);
        bill.setCreateDate(createDate);
        bill.setAmount(amount);
        bill.setDiscountCode(discountCode);
        bill.setDiscountPercent(discountPercent);
        bill.setNewAmount(newAmount);
        bill.setPaymentMethod(paymentMethod);
        bill.setReceiverFullname(receiverFullname);
        bill.setReceiverPhone(receiverPhone);
        bill.setReceiverAddress(receiverAddress);
        bill.setStatus(0);
        billDAO.save(bill);

        return bill;
    }

    private BillDetail createBillDetailFromCart(Cart cart, Bill bill) {
        BillDetail billDetail = new BillDetail();
        billDetail.setBill(bill);
        billDetail.setProduct(cart.getProduct());
        billDetail.setQuantity(cart.getQuantity());
        billDetail.setPrice(cart.getPrice());
        billDetail.setAmount(cart.getAmount());

        return billDetail;
    }

    public void subtractQuantity(String orderIds) {
        String[] checked = orderIds.split(";");
        for (String string : checked) {
            Cart cart = cartDAO.findById(Integer.parseInt(string)).orElse(null);
            if (cart != null) {
                System.out.println("======== cart : "+cart.getId());
                Product product = productDAO.findById(cart.getProduct().getId()).orElse(null);
                if (product != null) {
                    System.out.println("======== product : "+product.getId());
                    int currentQuantity = product.getQuantity();
                    int quantity = cart.getQuantity();
                    if (currentQuantity >= quantity) {
                        product.setQuantity(currentQuantity - quantity);
                        productDAO.save(product);
                    }
                    System.out.println(product.getQuantity());
                }
            }
        }
    }
}
