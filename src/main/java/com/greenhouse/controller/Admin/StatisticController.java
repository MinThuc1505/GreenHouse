package com.greenhouse.controller.Admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.DetailBillDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Product;
import com.greenhouse.service.SessionService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("admin")
public class StatisticController {
	@Autowired
	DetailBillDAO dao;
	@Autowired
	ProductDAO productDAO;
	@Autowired
	SessionService session;
	@Autowired
	DetailBillDAO detailBillDAO;

	@Autowired
	BillDAO billDAO;

	// REPORT- THÚY NÈ - START
	@GetMapping("/productCategoryStatistics")
	public String productCategoryStatistics(Model model, @RequestParam("p") Optional<Integer> p) {
		Account account = session.get("account");
		model.addAttribute("account", account);
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		Page<Object[]> page = productDAO.calculateCategoryRevenue(pageable);

		model.addAttribute("page", page);
		model.addAttribute("template", "ProductCategoryStatistics.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}

	@RequestMapping("/monthlyStatistic")
	public String revenueStatistic(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model, @RequestParam("p") Optional<Integer> p) {
		Account account = session.get("account");
		model.addAttribute("account", account);
		if (startDate == null && endDate == null) {
			LocalDate defaultStartDate = LocalDate.of(2023, Month.JANUARY, 1);
			LocalDate defaultEndDate = LocalDate.of(2024, Month.JANUARY, 1);
			startDate = Date.from(defaultStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(defaultEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		// Chart
		List<Object[]> data = detailBillDAO.getRevenueByYear();
		model.addAttribute("data", data);

		List<Object[]> reportList = detailBillDAO.getListReport(startDate, endDate);

		model.addAttribute("page", reportList);
		int productCount = productDAO.countProducts(); // Lấy tổng số sản phẩm
		model.addAttribute("productCount", productCount);
		// Lấy Doanh thu
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		double totalRevenue = productDAO.calculateTotalRevenue();
		String formattedTotalRevenue = currencyFormatter.format(totalRevenue);
		model.addAttribute("formattedTotalRevenue", formattedTotalRevenue);

		model.addAttribute("template", "MonthlyStatistic.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}

	@RequestMapping("/reportBill")
	public String reportBill(Model model, @RequestParam("keyword") Optional<String> kw,
			@RequestParam("p") Optional<Integer> p) {
		String kwords = kw.orElse((String) session.get("keyword"));
		session.set("keyword", kwords);
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		Page<Object[]> billPage;
		if (kwords == null) {
			billPage = billDAO.getBillDetails(pageable);
		} else {
			billPage = billDAO.getBillDetailsbyKeyword("%" + kwords + "%", pageable);
		}
		
		model.addAttribute("page", billPage);
		Account account = session.get("account");
		model.addAttribute("account", account);
		model.addAttribute("template", "BillReport.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}

	@GetMapping("/print-bill/{billId}")
	public void printBill(@PathVariable("billId") int billId, HttpServletRequest request,
			HttpServletResponse response) {
		// Tạo đối tượng Document của iText
		Document document = new Document();
		try {
			// Lấy thông tin hóa đơn và danh sách chi tiết hóa đơn
			List<Object[]> billDetails = billDAO.getBillDetailsByBillId(billId);
			List<Object[]> billSummary = billDAO.getBillUsername(billId);

			// Thiết lập response header để xuất file PDF
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=bill.pdf");

			// Tạo PdfWriter để ghi dữ liệu vào file PDF
			PdfWriter.getInstance(document, response.getOutputStream());

			// Mở document
			document.open();

			// Tạo đối tượng Font sử dụng font Arial Unicode MS
			// Tạo đối tượng BaseFont với font Arial
			BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/Calibri.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			Font font = new Font(baseFont);

			// Sử dụng font trong document
			Paragraph paragraph = new Paragraph("Hóa Đơn", font);
			document.add(paragraph);
			// Lấy ngày giờ hiện tại
			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String currentDateTime = dateFormat.format(currentDate);
			DecimalFormat decimalFormat = new DecimalFormat("#,##0");
			// In thông tin hóa đơn
			for (Object[] bill : billSummary) {
				int billID = (int) bill[0];
				String username = (String) bill[1];
				BigDecimal total = (BigDecimal) bill[2];

				Paragraph usernameParagraph = new Paragraph("Người mua: " + username, font);
				Paragraph billIDParagraph = new Paragraph("Bill Id: " + billID, font);
				Paragraph totalParagraph = new Paragraph("Tổng tiền: " + decimalFormat.format(total), font);
				Paragraph dateParagraph = new Paragraph("Ngày in: " + currentDateTime, font);

				document.add(usernameParagraph);
				document.add(billIDParagraph);
				document.add(totalParagraph);
				document.add(dateParagraph);
				document.add(Chunk.NEWLINE); // Thêm khoảng cách dọc sau mỗi đối tượng
			}
			PdfPTable table = new PdfPTable(4); // Tạo bảng với 4 cột

			// Thiết lập tiêu đề cột
			PdfPCell cellProductName = new PdfPCell(new Paragraph("Tên Sản Phẩm", font));
			PdfPCell cellQuantity = new PdfPCell(new Paragraph("Số lượng", font));
			PdfPCell cellPrice = new PdfPCell(new Paragraph("Đơn giá", font));
			PdfPCell cellTotalPrice = new PdfPCell(new Paragraph("Thành tiền", font));

			// Thiết lập các thuộc tính của tiêu đề cột
			// Màu nền của cột
			cellProductName.setBackgroundColor(BaseColor.YELLOW);
			cellQuantity.setBackgroundColor(BaseColor.YELLOW);
			cellPrice.setBackgroundColor(BaseColor.YELLOW);
			cellTotalPrice.setBackgroundColor(BaseColor.YELLOW);

			// Thiết lập căn giữa cho các ô trong tiêu đề cột
			cellProductName.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellQuantity.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTotalPrice.setHorizontalAlignment(Element.ALIGN_CENTER);

			// Thêm các ô vào bảng
			table.addCell(cellProductName);
			table.addCell(cellQuantity);
			table.addCell(cellPrice);
			table.addCell(cellTotalPrice);

			// Thêm dữ liệu từ danh sách chi tiết hóa đơn vào bảng
			for (Object[] billDetail : billDetails) {
				String productName = (String) billDetail[5];
				int quantity = (int) billDetail[2];
				BigDecimal price = (BigDecimal) billDetail[3];
				BigDecimal totalPrice = (BigDecimal) billDetail[4];

				PdfPCell productNameCell = new PdfPCell(new Paragraph(productName, font));
				PdfPCell quantityCell = new PdfPCell(new Paragraph(String.valueOf(quantity), font));
				PdfPCell priceCell = new PdfPCell(new Paragraph(decimalFormat.format(price), font));
				PdfPCell totalPriceCell = new PdfPCell(new Paragraph(decimalFormat.format(totalPrice), font));

				// Thiết lập căn giữa cho các ô trong dữ liệu
				productNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);

				table.addCell(productNameCell);
				table.addCell(quantityCell);
				table.addCell(priceCell);
				table.addCell(totalPriceCell);
			}
			// Thêm bảng vào document
			document.add(table);

			// Đóng document
			document.close();
			System.out.println("Ok DOO");
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

}
