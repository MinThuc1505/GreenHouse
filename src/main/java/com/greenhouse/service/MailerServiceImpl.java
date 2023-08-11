package com.greenhouse.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.MailInfo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailerServiceImpl implements MailerService {
	@Autowired
	JavaMailSender sender;
	@Autowired
	private AccountDAO accountDAO;

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
		List<String> emailList = accountDAO.getAllEmails(); // gmail đang hoạt động active = true
		for (String email : emailList) {
			MailInfo mail = new MailInfo(email, "GREENHOUSE TRI ÂN KHÁCH HÀNG",
					"<!DOCTYPE html><html lang=\'en\'><head><meta charset=\'UTF-8\'><meta http-equiv=\'X-UA-Compatible\' content=\'IE=edge\'><meta name=\'viewport\' content=\'width=device-width, initial-scale=1.0\'><title>Email Confirmation</title><style>body {background-color: #f2f2f2;margin: 0;padding: 0;}.container {margin: 0 auto;padding: 20px;background-color: #fff;border-radius: 5px;box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);}p {color: rgb(9, 49, 9);font-family:sans-serif;}</style></head><body><div class=\'container\'><p>G\u1EEDi qu\u00FD kh\u00E1ch h\u00E0ng,</p><p>C\u1EA3m \u01A1n kh\u00E1ch h\u00E0ng \u0111\u00E3 tin t\u01B0\u1EDFng v\u00E0 l\u1EF1a ch\u1ECDn n\u1ED9i th\u1EA5t GreenHouse l\u00E0m n\u01A1i mua s\u1EAFm \u0111\u1EC3 ho\u00E0n thi\u1EC7n cho ng\u00F4i nh\u00E0 c\u1EE7a m\u00ECnh.</p><p>Ch\u00FAng t\u00F4i hy v\u1ECDng b\u1EA1n s\u1EBD h\u00E0i l\u00F2ng khi l\u1EF1a ch\u1ECDn s\u1EA3n ph\u1EA9m c\u1EE7a c\u1EEDa h\u00E0ng.</p><p>Mong r\u1EB1ng sau n\u00E0y v\u1EABn s\u1EBD \u0111\u1ED3ng h\u00E0nh c\u00F9ng b\u1EA1n, ch\u00FAng t\u00F4i xin h\u1EE9a s\u1EBD mang l\u1EA1i nh\u1EEFng s\u1EA3n ph\u1EA9m t\u1ED1t nh\u1EA5t.</p><p>Ch\u00E2n th\u00E0nh c\u1EA3m \u01A1n!</p><p>Tr\u00E2n tr\u1ECDng!</p></div></body></html>\r\n"
							+ //
							"");
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

}