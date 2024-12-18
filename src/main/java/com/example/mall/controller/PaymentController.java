package com.example.mall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mall.service.CartService;
import com.example.mall.service.PaymentService;
import com.example.mall.util.TeamColor;
import com.example.mall.vo.Page;
import com.example.mall.vo.Payment;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

//Author : 김문정, 김은서
@Controller
@Slf4j
public class PaymentController {
	@Autowired PaymentService paymentService;
	@Autowired CartService cartService;
	// Author : 김은서
	// 고객용 : /customer/addPayment 에서 결제 완료시 출력되는 화면
	@GetMapping("/customer/payResult")
	public String getPaymentResult(HttpSession session) {
		Integer countCart = cartService.getCountCartByCustomerEmail((String) session.getAttribute("loginCustomer"));
		log.debug(TeamColor.KES + "회원 장바구니 = " + countCart  + TeamColor.RESET);
		session.setAttribute("countCart", countCart);
		return "customer/payResult";
	}
	
	// Author : 김은서
	// 고객용 : /customer/addPayment에서 '결제하기' 클릭 시 사용
	@PostMapping("/customer/addPayment") 
	public String addPayment(Payment payment, Integer[] cartNo) {
		paymentService.addPayment(payment, cartNo);
		return "redirect:/customer/payResult";
	}
	
	// Author : 김은서
	// 고객용 : /customer/getPaymentList에서 '결제이력' 리스팅 출력
	@GetMapping("/customer/getPaymentList")
	public String getPaymentList(Model model, HttpSession session, Page page) {
		page.setRowPerPage(3);
		// 1) 로그인한 고객의 paymentList 출력
		List<Map<String,Object>> paymentList = paymentService.getPaymentList(page ,(String) session.getAttribute("loginCustomer"));

		// 2) 1)의 라스트 페이지 출력 및 Page 객체에 sets
		page.setLastPage(paymentService.getlastPageOnPaymentList(page, (String) session.getAttribute("loginCustomer")));
		log.debug(TeamColor.KES + "페이먼트 라스트 페이지 : " +page.getLastPage() + TeamColor.RESET);
		
		// 3) 1)에서 출력된 paymentList 인덱스 별 상세정보 출력 (Payment 별 orders + goods + category 정보) 
		List<Map<String,Object>> payInfoList = new ArrayList<>();
		
		for(Map<String, Object> p : paymentList) {
			String[] ordersNoArr = paymentService.getOrdersNoByPaymentNo(Integer.parseInt(String.valueOf(p.get("paymentNo"))));
			
			StringBuilder debuglog = new StringBuilder(); // 디버그용 
			for(String o : ordersNoArr) {
				debuglog.append(o);
				payInfoList.add(paymentService.getPayInfoListByPaymentNo(Integer.parseInt(o)));
			}
			log.debug(TeamColor.KES + p.get("paymentNo") + "번에 매칭되는 OrdersNo 리스트 = " + debuglog + TeamColor.RESET);
			log.debug(TeamColor.KES + p.get("paymentNo") + "번에 매칭되는 상세내역 = " + payInfoList + TeamColor.RESET);
		}
		
		model.addAttribute("page", page);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("PayInfoListByPaymentNo", payInfoList);
		
		return "customer/getPaymentList";
	}
	
	// Author : 김은서
	// 직원용 : /customer/modifyPaymentStatus 에서 특정 paymentNo에 따라 paymentStatus 변경
	@GetMapping("/customer/modifyPaymentStatus")
	public String modifyPaymentStatus(Payment payment, HttpSession session) {
		//g.debug(TeamColor.KES + "받아온 payment 값" + payment.toString() + TeamColor.RESET);
		
		// 고객이 '결제완료' -> '배송중' 바꾸지 못하도록 막기
		if(payment.getPaymentStatus().equals("배송중")) {
			return "redirect:/customer/getPaymentList";
		}
		// paymentStatus 변경
		int checkSuccess = paymentService.modifyPaymentStatus(payment, (String) session.getAttribute("loginCustomer"));
		log.debug(TeamColor.KES + "modify PayStatus 결과" + checkSuccess + TeamColor.RESET);	// 1일시 정상 실행
		
		return "redirect:/customer/getPaymentList";
	}
	
	// Author : 김문정
		// getPaymentList
		@GetMapping("/staff/getPaymentList")
		public String getPaymentList(Model model, Page page) {
			log.debug(TeamColor.KMJ + "[PaymentController]");
			log.debug(TeamColor.KMJ + "[GET - getPaymentList]");
			
			page.setRowPerPage(3);
			
			// 직원용 paymentList 가져오기
			List<Map<String,Object>> paymentList = paymentService.getPaymentList(page);
			log.debug(TeamColor.KMJ + "paymentList : "+ paymentList.toString() + TeamColor.RESET);
			log.debug(TeamColor.KMJ + "paymentListsize : "+ paymentList.size() + TeamColor.RESET);

			// (2) 1)의 lastpage 값 출력 및 Page 객체에 담기
			Integer lastPage = paymentService.getlastPageOnPaymentList(page);
			page.setLastPage(lastPage);
			
			// 3) 1)에서 출력된 paymentList 인덱스 별 상세정보 출력 (Payment 별 orders + goods + category 정보) 
			List<Map<String,Object>> payInfoList = new ArrayList<>();
			
			for(Map<String, Object> p : paymentList) {
				String[] ordersNoArr = paymentService.getOrdersNoByPaymentNo(Integer.parseInt(String.valueOf(p.get("paymentNo"))));
				
				StringBuilder debuglog = new StringBuilder(); // 디버그용 
				for(String o : ordersNoArr) {
					debuglog.append(o);
					payInfoList.add(paymentService.getPayInfoListByPaymentNo(Integer.parseInt(o)));
				}
				log.debug(TeamColor.KES + p.get("paymentNo") + "번에 매칭되는 OrdersNo 리스트 = " + debuglog + TeamColor.RESET);
				log.debug(TeamColor.KES + p.get("paymentNo") + "번에 매칭되는 상세내역 = " + payInfoList + TeamColor.RESET);
			}
			
			log.debug(TeamColor.KMJ + "PayInfoListByPaymentNo : "+ payInfoList + TeamColor.RESET);	
			log.debug(TeamColor.KMJ + "PayInfoListByPaymentNosize : "+ payInfoList.size() + TeamColor.RESET);	
			
			model.addAttribute("PayInfoListByPaymentNo", payInfoList);
			model.addAttribute("paymentList", paymentList);
			model.addAttribute("page", page);
			
			
			return "/staff/getPaymentList";
		}
	
	// Author : 김문정
	// modifyPaymentStatus : getPaymentList - '결제완료' => '배송중' or '결제취소'로 변경
	@GetMapping("/staff/modifyPaymentStatus")
	public String modifyPaymentStatus(Payment payment) {
		log.debug(TeamColor.KMJ + "[PaymentController]");
		log.debug(TeamColor.KMJ + "[GET - modifyPaymentStatus]");
		
		log.debug(TeamColor.KMJ + "payment : "+ payment.toString() + TeamColor.RESET);	

		
		Integer result = paymentService.modifyPaymentStatus(payment);
		
		log.debug(TeamColor.KMJ + "result : "+ result + TeamColor.RESET);	
		
		if(result == 0) {
			
			return "redirect:/staff/getPaymentList";
		}
	
		return "redirect:/staff/getPaymentList";
	}
	
	
	
	
}
