package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomerController {
	@Autowired
	CustomerRepository customerRepository;

	@RequestMapping(value = "/")
	public ModelAndView index(
			ModelAndView mv) {

		// 全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索一覧画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

	// 会員登録画面へ遷移
	@RequestMapping(value = "/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {
		// 新規登録画面へ遷移
		mv.setViewName("add_customer");
		return mv;
	}

	// 会員を登録
	@RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
	public ModelAndView addCustomer(
			@RequestParam("name") String name,
			@RequestParam("tel") String tel,
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			ModelAndView mv) {

		// DBに会員を登録
		Customer customer = new Customer(name, tel, mail, password);
		customerRepository.save(customer);

		// 全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

	// 名前、電話番号、メールからあいまい検索
	@RequestMapping(value = "/searchCustomer")
	public ModelAndView searchCustomer(
			@RequestParam("name") String customerName,
			@RequestParam("tel") String customerTel,
			@RequestParam("mail") String customerMail,
			ModelAndView mv) {

		// 検索結果を取得
//		List<Customer> customers = customerRepository.findByCustomerNameLike(customerName);
//
//		// 会員検索画面へ遷移
//		mv.addObject("customers", customers);
		mv.setViewName("search_customer");

		return mv;
	}

}
