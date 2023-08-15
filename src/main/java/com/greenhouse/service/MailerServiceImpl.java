package com.greenhouse.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.model.Discount;
import com.greenhouse.model.MailInfo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailerServiceImpl implements MailerService {
	@Autowired
	JavaMailSender sender;
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	private DiscountDAO discountDAO;

	@Override
	public void send(MailInfo mail) throws MessagingException {
		// Tạo message
		MimeMessage message = sender.createMimeMessage();
		// Sử dụng Helper để thiết lập các thông tin cần thiết cho message
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());
		String[] cc = mail.getCc();
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		String[] bcc = mail.getBcc();
		if (bcc != null && bcc.length > 0) {
			helper.setBcc(bcc);
		}
		String[] attachments = mail.getAttachments();
		if (attachments != null && attachments.length > 0) {
			for (String path : attachments) {
				File file = new File(path);
				helper.addAttachment(file.getName(), file);
			}
		}
		sender.send(message);
	}

	@Override
	public void send(String to, String subject, String body) throws MessagingException {
		this.send(new MailInfo(to, subject, body));
	}

	List<MailInfo> list = new ArrayList<>();

	@Override
	public void queue(MailInfo mail) {
		list.add(mail); 
	}

	@Override
	public void queue(String to, String subject, String body) {
		queue(new MailInfo(to, subject, body));
	}

	// @Scheduled(cron = "0 0 9 * * *")
	@Scheduled(fixedRate = 10000) // Chạy mỗi 10 giây
	public void startSending() {
		Discount discount = getRandomDiscountCode();
		String discountCode = discount.getDiscountCode();
		String discountPercent = discount.getDiscountPercent().toString();
		Date startDate = discount.getStartDate();
		Date endDate = discount.getEndDate();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		String formattedStartDate = dateFormat.format(startDate);
		String formattedEndDate = dateFormat.format(endDate);
		List<String> emailList = accountDAO.getAllEmails();
		for (String email : emailList) {
			MailInfo mail = new MailInfo(email, "GREENHOUSE GỬI TẶNG VOUCHER",
					"<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Document</title><style>.card{width:400px;height:300px;background-color:#f7f7f7;border-radius:10px;box-shadow:0px 0px 10px rgba(0,0,0,0.1);position:relative}.card-front{width:100%;height:100%;padding:20px;border-radius:10px;background-color:#0c6e62;color:#fff;border:2px solid #f0c30f;box-shadow:0px 0px 10px rgba(0,0,0,0.2);text-align:center;text-transform:uppercase;font-weight:bold;letter-spacing:3px}</style></head><body><div class='card'><div class='card-front'><p>Green House tặng voucher</p><p><strong>Voucher: "
							+ discountCode + "</strong></p><p><strong>Giảm:</strong>" + discountPercent
							+ "% tổng hóa đơn</p><p><strong>Voucher có hiệu lực từ ngày</strong></p><p><strong>"
							+ formattedStartDate + "</strong> đến ngày <strong>" + formattedEndDate
							+ "</strong></p></div></div></body></html>");
			this.queue(mail);
		}
		int success = 0, error = 0;
		while (!list.isEmpty()) {
			MailInfo mail = list.remove(0);
			try {
				this.send(mail);
				success++;
			} catch (Exception e) {
				error++;
				System.out.println("Không tìm thấy Email");
			}
		}
		System.out.printf(">>Sent: %d, Eror: %d \r\n", success, error);
	}

	public Discount getRandomDiscountCode() {
		// Truy vấn tất cả các mã giảm giá từ cơ sở dữ liệu
		List<Discount> allDiscountCodes = discountDAO.findAll();

		// Lọc ra những mã còn hạn sử dụng và số lượng còn
		List<Discount> validDiscountCodes = allDiscountCodes.stream()
				.filter(discount -> discount.getStartDate().before(new Date())
						&& discount.getEndDate().after(new Date())
						&& discount.getQuantity() - discount.getUsedQuantity() > 0)
				.collect(Collectors.toList());

		// Kiểm tra xem có mã giảm giá còn hạn và số lượng còn hay không
		if (validDiscountCodes.isEmpty()) {
			return null;
		}

		// Chọn ngẫu nhiên một mã giảm giá từ danh sách các mã còn hạn và số lượng còn
		Random random = new Random();
		int randomIndex = random.nextInt(validDiscountCodes.size());
		Discount randomDiscountCode = validDiscountCodes.get(randomIndex);

		return randomDiscountCode;
	}
}