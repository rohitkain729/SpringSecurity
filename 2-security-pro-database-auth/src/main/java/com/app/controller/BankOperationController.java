package com.app.controller;

import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bank")
public class BankOperationController {

	@GetMapping("/")
	public String showHomePage() {
		return "welcome";
	}

	@GetMapping("/balance")
	public String showBalance(Map<String, Object> map) {
		double amount = new Random().nextInt(10000);

		map.put("amount", amount);
		// return lvn
		return "show_balance";
	}

	@GetMapping("/offers")
	public String showOffers() {

		return "offers";
	}

	@GetMapping("/loanApprove")
	public String approveLaon(Map<String, Object> map) {
		int amount = new Random().nextInt(10000);
		map.put("amount", amount);
		return "loan";

	}

	@GetMapping("/denied")
	public String showAccessDeinedPage() {
		return "authorization_failure";
	}

}
