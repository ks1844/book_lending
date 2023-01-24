package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
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
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	HistoryDisplayRepository historyDisplayRepository;

	@Autowired
	MessageRepository messageRepository;

	@RequestMapping(value = "/customer")
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
	@RequestMapping(value = "/customer/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {
		// 新規登録画面へ遷移
		mv.setViewName("add_customer");
		return mv;
	}

	// 会員情報を表示
	@RequestMapping(value = "/customer/show/{customer_id}")
	public ModelAndView show(
			@PathVariable("customer_id") int customerId,
			ModelAndView mv) {

		// 今日の日付を取得
		LocalDate today = LocalDate.now();

		// 貸出期限の2週間前の日付を取得
		LocalDate checkoutDate = today.plusDays(14);
		//		String checkoutDateString = checkoutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// 会員IDから会員情報を取得
		Customer customer = customerRepository.findById(customerId).get(0);

		// 会員IDからその会員の貸出履歴を取得
		List<HistoryDisplay> histories = historyDisplayRepository.findByCustomerIdAndOrderByHistoryIdDesc(customerId);

		// 貸出履歴から履歴IDを取り出す
		List<Message> messageList;
		List<String> messages = new ArrayList<>();
		int historyId;
		for (HistoryDisplay history : histories) {
			historyId = history.getId();
			messageList = messageRepository.findByHistoryId(historyId);
			if (messageList.size() > 0) {
				messages.add(messageList.get(0).getMessageText());
			}
		}

		//		// 貸出日が2週間以上前の貸出履歴をカウントする
		//		List<HistoryDisplay> delayedHistories = historyDisplayRepository
		//				.findByCustomerIdAndStatusNameAndHistoryDate(customerId, checkoutDateString, "貸出中");
		//		int delayedHistoryCount = delayedHistories.size();

		//		// 貸出期限が過ぎているものがあれば、メッセージを渡す
		//		if (delayedHistoryCount > 0) {
		//			mv.addObject("message", String.format("延滞している書籍が%d冊あります。<br>なるべく早く返却してください。", delayedHistoryCount));
		//		}

		// 会員情報と貸出履歴を渡す
		mv.addObject("customer", customer);
		mv.addObject("histories", histories);
		mv.addObject("messages", messages);

		// 会員情報画面を表示
		mv.setViewName("show_customer");
		return mv;
	}

	// 会員を登録
	@RequestMapping(value = "/customer/add", method = RequestMethod.POST)
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
	@RequestMapping(value = "/customer/search")
	public ModelAndView searchCustomer(
			@RequestParam("name") String customerName,
			@RequestParam("tel") String customerTel,
			@RequestParam("mail") String customerMail,
			@RequestParam(name = "is_deleted", defaultValue = "0") String isDeletedString,
			ModelAndView mv) {

		// あいまい検索のため、それぞれの文字列の前後に%を追加
		customerName = "%" + customerName + "%";
		customerTel = "%" + customerTel + "%";
		customerMail = "%" + customerMail + "%";

		boolean isDeleted = isDeletedString.equals("1");

		// 検索結果を取得
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
			@RequestParam(name = "is_deleted", defaultValue = "false") boolean isDeleted,
			ModelAndView mv) {

		// 会員情報を変更
		Customer customer = customerRepository.findByIdLike(customerId).get(0);
		customer.setName(customerName);
		customer.setTel(customerTel);
		customer.setMail(customerMail);
		customer.setPassword(customerPassword);
		customer.setDeleted(isDeleted);

		customerRepository.save(customer);

		// 全件取得
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
			@RequestParam(name = "is_deleted", defaultValue = "false") boolean isDeleted,
			ModelAndView mv) {

		// 退会処理を行う
		Customer customer = customerRepository.findByIdLike(customerId).get(0);
		customer.setDeleted(true);
		customerRepository.save(customer);

		// 全件取得
		List<Customer> customers = customerRepository.findAll();

		// 会員検索画面へ遷移
		mv.addObject("customers", customers);
		mv.setViewName("search_customer");
		return mv;
	}

}
