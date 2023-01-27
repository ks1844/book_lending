package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomerController {

	// リポジトリの宣言
	// 会員
	@Autowired
	CustomerRepository customerRepository;

	// 貸出履歴の表示用
	@Autowired
	HistoryDisplayRepository historyDisplayRepository;

	// 延滞者メッセージ
	@Autowired
	MessageRepository messageRepository;
	
	// DBでの文字数の上限
	int maxCustomerNameLength = 20;
	int maxCustomerTelLength = 20;
	int maxCustomerMailLength = 50;
	int maxCustomerPasswordLength = 20;

	@RequestMapping(value = "/customer")
	public ModelAndView index(
			ModelAndView mv) {

		// 会員の全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索一覧画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

	// 会員ごとに表示
	@RequestMapping(value = "/customer/show/{customer_id}")
	public ModelAndView show(
			@PathVariable("customer_id") int customerId,
			ModelAndView mv) {

		// 会員IDから会員を取得
		Customer customer = customerRepository.findById(customerId).get(0);

		// 会員IDからその会員の貸出履歴を取得
		List<HistoryDisplay> histories = historyDisplayRepository.findByCustomerIdAndOrderByHistoryIdDesc(customerId);

		// 貸出履歴IDから延滞しているメッセージを取り出す
		List<Message> messages = messageRepository.findByCustomerIdAndIsDeletedOrderByHistoryIdDesc(customerId, false);

		// 会員情報と貸出履歴を渡す
		mv.addObject("customer", customer);
		mv.addObject("histories", histories);
		mv.addObject("messages", messages);

		// 会員情報画面を表示
		mv.setViewName("show_customer");
		return mv;
	}

	// 会員の新規登録画面へ遷移
	@RequestMapping(value = "/customer/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {

		// 会員の新規登録画面へ遷移
		mv.setViewName("add_customer");
		return mv;
	}
	
	// 会員を新規登録
	@RequestMapping(value = "/customer/add", method = RequestMethod.POST)
	public ModelAndView addCustomer(
			@RequestParam("customer_name") String customerName,
			@RequestParam("customer_tel") String customerTel,
			@RequestParam("customer_mail") String customerMail,
			@RequestParam("customer_password") String customerPassword,
			@RequestParam("customer_password_identification") String customerPasswordIdentification,
			ModelAndView mv) {
		
		// 文字数がDBの上限を越えるとき、変更処理を行わない
		if (customerName.length() > maxCustomerNameLength) {
			mv.addObject("message", "会員名の文字数制限の" + maxCustomerNameLength + "文字を越えています");
			mv.addObject("customer_name", customerName);
			mv.addObject("customer_tel", customerTel);
			mv.addObject("customer_mail", customerMail);
			mv.addObject("customer_password", customerPassword);
			mv.setViewName("add_customer");
			return mv;
		} else if (customerTel.length() > maxCustomerTelLength) {
			mv.addObject("message", "電話番号の文字数制限の" + maxCustomerTelLength + "文字を越えています");
			mv.addObject("customer_name", customerName);
			mv.addObject("customer_tel", customerTel);
			mv.addObject("customer_mail", customerMail);
			mv.addObject("customer_password", customerPassword);
			mv.setViewName("add_customer");
			return mv;
		} else if (customerMail.length() > maxCustomerMailLength) {
			mv.addObject("message", "メールアドレスの文字数制限の" + maxCustomerMailLength + "文字を越えています");
			mv.addObject("customer_name", customerName);
			mv.addObject("customer_tel", customerTel);
			mv.addObject("customer_mail", customerMail);
			mv.addObject("customer_password", customerPassword);
			mv.setViewName("add_customer");
			return mv;
		} else if (customerPassword.length() > maxCustomerPasswordLength) {
			mv.addObject("message", "パスワードの文字数制限の" + maxCustomerPasswordLength + "文字を越えています");
			mv.addObject("customer_name", customerName);
			mv.addObject("customer_tel", customerTel);
			mv.addObject("customer_mail", customerMail);
			mv.addObject("customer_password", customerPassword);
			mv.setViewName("add_customer");
			return mv;
		}

		// パスワードが一致しないとき、再入力を促す
		if (!customerPassword.equals(customerPasswordIdentification)) {
			mv.addObject("message", "パスワードが一致しません");
			mv.addObject("customer_name", customerName);
			mv.addObject("customer_tel", customerTel);
			mv.addObject("customer_mail", customerMail);
			mv.addObject("customer_password", customerPassword);
			mv.setViewName("add_customer");
			return mv;
		}

		//会員登録前の会員情報を全件取得
		List<Customer> customersBeforeAdd = customerRepository.findAll();

		// 入力で得たメールアドレスで既に会員登録済みのとき、警告を渡す
		for (Customer customer : customersBeforeAdd) {
			if (customerMail.equals(customer.getMail())) {
				mv.addObject("message", customer.getMail() + "はすでに登録済みです。");
				mv.setViewName("add_customer");
				return mv;
			}
		}

		// DBに会員を登録
		Customer customer = new Customer(customerName, customerTel, customerMail, customerPassword);
		customerRepository.save(customer);

		// 会員情報を全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

	// 名前、電話番号、メールからあいまい検索
	@RequestMapping(value = "/customer/search")
	public ModelAndView searchCustomer(
			@RequestParam("name") String customerName,
			@RequestParam("tel") String customerTel,
			@RequestParam("mail") String customerMail,
			@RequestParam(name = "is_deleted", defaultValue = "false") boolean isDeleted,
			ModelAndView mv) {

		// 検索内容を保存するため渡しておく
		mv.addObject("customer_name", customerName);
		mv.addObject("customer_tel", customerTel);
		mv.addObject("customer_mail", customerMail);
		mv.addObject("is_deleted", isDeleted);

		// あいまい検索のため、それぞれの文字列の前後に%を追加
		customerName = "%" + customerName + "%";
		customerTel = "%" + customerTel + "%";
		customerMail = "%" + customerMail + "%";

		// 会員を検索
		List<Customer> customers;
		if (isDeleted) {
			// 退会された会員も含んで検索
			customers = customerRepository.findByNameLikeAndTelLikeAndMailLike(customerName, customerTel, customerMail);
		} else {
			// 退会していない会員のみで検索
			customers = customerRepository.findByNameLikeAndTelLikeAndMailLikeAndIsDeletedLike(customerName,
					customerTel, customerMail, false);
		}

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");

		return mv;
	}

	// 会員情報の変更画面へ遷移
	@RequestMapping(value = "/customer/showUpdate")
	public ModelAndView showUpdate(
			@RequestParam("customer_id") int customerId,
			ModelAndView mv) {

		// 会員IDから会員情報を取得
		Customer customer = customerRepository.findByIdLike(customerId).get(0);

		// 会員情報更新画面へ遷移
		mv.addObject("customer", customer);
		mv.setViewName("update_customer");
		return mv;
	}

	// 会員情報の変更
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST)
	public ModelAndView updateCustomer(
			@RequestParam("customer_id") int customerId,
			@RequestParam("customer_name") String customerName,
			@RequestParam("customer_tel") String customerTel,
			@RequestParam("customer_mail") String customerMail,
			@RequestParam("customer_password") String customerPassword,
			@RequestParam("customer_password_identification") String customerPasswordIdentification,
			@RequestParam(name = "is_deleted", defaultValue = "false") boolean isDeleted,
			ModelAndView mv) {

		// 会員IDから会員情報を取得
		Customer customer = customerRepository.findByIdLike(customerId).get(0);

		// 入力画面の入力内容を消さないように、会員情報を変更(保存はまだ行わない)
		customer.setName(customerName);
		customer.setTel(customerTel);
		customer.setMail(customerMail);
		customer.setPassword(customerPassword);
		customer.setDeleted(isDeleted);

		// 文字数がDBの上限を越えるとき、変更処理を行わない
		if (customerName.length() > maxCustomerNameLength) {
			mv.addObject("message", "会員名の文字数制限の" + maxCustomerNameLength + "文字を越えています");
			mv.addObject("customer", customer);
			mv.setViewName("update_customer");
			return mv;
		} else if (customerTel.length() > maxCustomerTelLength) {
			mv.addObject("message", "電話番号の文字数制限の" + maxCustomerTelLength + "文字を越えています");
			mv.addObject("customer", customer);
			mv.setViewName("update_customer");
			return mv;
		} else if (customerMail.length() > maxCustomerMailLength) {
			mv.addObject("message", "メールアドレスの文字数制限の" + maxCustomerMailLength + "文字を越えています");
			mv.addObject("customer", customer);
			mv.setViewName("update_customer");
			return mv;
		} else if (customerPassword.length() > maxCustomerPasswordLength) {
			mv.addObject("message", "パスワードの文字数制限の" + maxCustomerPasswordLength + "文字を越えています");
			mv.addObject("customer", customer);
			mv.setViewName("update_customer");
			return mv;
		}

		// パスワードと確認用のパスワードが一致していないとき、再度入力を促す
		if (!customerPassword.equals(customerPasswordIdentification)) {
			mv.addObject("customer", customer);
			mv.addObject("message", "パスワードが一致していません");
			mv.setViewName("update_customer");
			return mv;
		}

		//会員登録前の会員情報を全件取得
		List<Customer> customersBeforeAdd = customerRepository.findAll();

		// 入力で得たメールアドレスで既に会員登録済みのとき、警告を渡す
		for (Customer customerBeforAdd : customersBeforeAdd) {
			if (customerMail.equals(customerBeforAdd.getMail())) {
				if (customerBeforAdd.getId() == customerId) {
					// 登録済みのメールでも、同じ会員のメールアドレスなら更新手続きに戻る
					break;
				} else {
					mv.addObject("message", customerBeforAdd.getMail() + "はすでに登録済みです。");
					mv.addObject("customer", customer);
					mv.setViewName("update_customer");
					return mv;
				}
			}
		}

		// 会員情報の変更を保存
		customerRepository.save(customer);

		// 会員情報を全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;

	}

	// 退会手続き画面へ遷移
	@RequestMapping(value = "/customer/showDelete")
	public ModelAndView showDelete(
			@RequestParam("customer_id") int customerId,
			ModelAndView mv) {

		// 会員IDから会員情報を取得
		Customer customer = customerRepository.findByIdLike(customerId).get(0);

		// 退会手続き画面へ遷移
		mv.addObject("customer", customer);
		mv.setViewName("delete_customer");
		return mv;
	}

	// 退会
	@RequestMapping(value = "/customer/delete")
	public ModelAndView deleteCustomer(
			@RequestParam("customer_id") int customerId,
			@RequestParam("customer_password") String customerPassword,
			@RequestParam(name = "is_deleted", defaultValue = "false") boolean isDeleted,
			ModelAndView mv) {

		// 会員IDから会員を取得
		Customer customer = customerRepository.findByIdLike(customerId).get(0);

		// 入力されたパスワードが異なるとき、再度入力を促す
		if (!customerPassword.equals(customer.getPassword())) {
			mv.addObject("message", "パスワードが間違っています");
			mv.addObject("customer", customer);
			mv.setViewName("delete_customer");
			return mv;
		}

		// 退会処理を行う
		customer.setDeleted(true);
		customerRepository.save(customer);

		// 会員情報を全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

}
