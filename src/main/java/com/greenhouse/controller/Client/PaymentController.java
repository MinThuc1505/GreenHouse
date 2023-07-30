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
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.config.VNPayConfig;
import com.greenhouse.model.Account;
import com.greenhouse.model.Bill;
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

}
