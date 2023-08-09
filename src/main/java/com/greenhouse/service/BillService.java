package com.greenhouse.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.BillDetailDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.DTO.OrderDTO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Bill;
import com.greenhouse.model.Bill_Detail;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Discount;

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
            Bill_Detail billDetail = createBillDetailFromCart(cart, bill);
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
        String bankCode = orderDTO.getBankCode();
        double discountPercent = isAppliedDiscount ? orderDTO.getDiscountPercent() : 0;
        long newAmount = (long) (amount * (1 - discountPercent / 100));
        String paymentMethod = orderDTO.getPaymentMethod();
        String receiverAddress = orderDTO.getReceiverAddress();

        Bill bill = new Bill();
        bill.setAccount(acc);
        bill.setCreateDate(createDate);
        bill.setAmount(amount);
        bill.setDiscountCode(discountCode);
        bill.setBankCode(bankCode);
        bill.setDiscountPercent(discountPercent);
        bill.setNewAmount(newAmount);
        bill.setPaymentMethod(paymentMethod);
        bill.setReceiverAddress(receiverAddress);
        bill.setStatus(0);
        billDAO.save(bill);

        return bill;
    }

    private Bill_Detail createBillDetailFromCart(Cart cart, Bill bill) {
        Bill_Detail billDetail = new Bill_Detail();
        billDetail.setBill(bill);
        billDetail.setProduct(cart.getProduct());
        billDetail.setQuantity(cart.getQuantity());
        billDetail.setPrice(cart.getPrice());
        billDetail.setAmount(cart.getAmount());

        return billDetail;
    }
}
