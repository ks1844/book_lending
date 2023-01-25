package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LendingController {

	@Autowired
	BookDisplayRepository bookDisplayRepository;

	@Autowired
	BookInfoDisplayRepository bookInfoDisplayRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	HistoryDisplayRepository historyDisplayRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	MessageRepository messageRepository;


	// 貸出履歴画面へ遷移
	@RequestMapping("/lending")
	public ModelAndView lending(
			ModelAndView mv) {

		// 貸出履歴一覧を全件取得
		List<HistoryDisplay> histories = historyDisplayRepository.findAllByOrderByHistoryIdDesc();

		// 貸出履歴オブジェクトを渡す
		mv.addObject("histories", histories);

		// 貸出履歴画面へ遷移
		mv.setViewName("search_history");
		return mv;
	}

	// 貸出画面へ遷移
	@RequestMapping("/lending/showLend")
	public ModelAndView showLend(
			ModelAndView mv) {

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 図書館オブジェクトを渡す
		mv.addObject("libraries", libraries);

		// 貸出登録画面へ遷移
		mv.setViewName("lend_book");
		return mv;
	}

	// 貸出内容を確認
	@RequestMapping(value = "/lending/confirm")
	public ModelAndView confirm(
			@RequestParam(name = "customer_id", defaultValue = "-1") int customerId,
			@RequestParam(name = "book_id", defaultValue = "-1") int bookId,
			@RequestParam("date") String date,
			ModelAndView mv) {

		// 会員IDから会員を取得し存在する会員か確認する
		List<Customer> customers = customerRepository.findById(customerId);
		if (customers == null) {
			mv.addObject("message", "存在しない会員IDです。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 書籍オブジェクトを取得
		List<Book> books = bookRepository.findById(bookId);
		if (books == null) {
			mv.addObject("message", "存在しない書籍IDです。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 一件分の書籍オブジェクトを作成
		Book book = books.get(0);

		// 書籍の状態が返却済み以外のとき、貸出処理を行わない
		if (book.getStatusId() != 1) {
			mv.addObject("message", "この書籍は貸出できません。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 日付の入力がないとき、貸出処理を行わない
		if (date.length() <= 0) {
			mv.addObject("message", "貸出日時を入力してください。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 会員オブジェクトの作成
		Customer customer = customers.get(0);

		// 書籍情報オブジェクトの作成
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(book.getBookInfoId()).get(0);

		// 会員オブジェクトを渡す
		mv.addObject("customer", customer);

		// 書籍情報を渡す
		mv.addObject("bookinfo", bookInfo);
		
		// 日付を渡す
		mv.addObject("date", date);
		
		// 書籍固有のIDを渡す
		mv.addObject("book_id", bookId);

		mv.setViewName("confirm_lending");
		return mv;
	}

	// 貸出登録
	@RequestMapping(value = "/lending/lend")
	public ModelAndView lend(
			@RequestParam(name = "customer_id", defaultValue = "-1") int customerId,
			@RequestParam(name = "book_id", defaultValue = "-1") int bookId,
			@RequestParam("date") String date,
			ModelAndView mv) {

		// 会員IDから会員を取得し存在する会員か確認する
		List<Customer> customers = customerRepository.findById(customerId);
		if (customers == null) {
			mv.addObject("message", "存在しない会員IDです。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 書籍オブジェクトを取得
		List<Book> books = bookRepository.findById(bookId);
		if (books == null) {
			mv.addObject("message", "存在しない書籍IDです。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 一件分の書籍オブジェクトを作成
		Book book = books.get(0);

		// 書籍の状態が返却済み以外のとき、貸出処理を行わない
		if (book.getStatusId() != 1) {
			mv.addObject("message", "この書籍は貸出できません。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 日付の入力がないとき、貸出処理を行わない
		if (date.length() <= 0) {
			mv.addObject("message", "貸出日時を入力してください。");
			mv.setViewName("lend_book");
			return mv;
		}

		// 書籍オブジェクトから図書館を取得
		int libraryId = book.getLibraryId();

		// 状態を貸出中にする
		book.setStatusId(2);

		// 書籍情報を保存
		bookRepository.save(book);

		// 貸出履歴オブジェクトを作成して更新
		History history = new History(date, bookId, customerId, libraryId, 2);
		historyRepository.save(history);

		// 貸出履歴一覧を全件取得
		List<HistoryDisplay> histories = historyDisplayRepository.findAllByOrderByHistoryIdDesc();

		// 貸出履歴オブジェクトを渡す
		mv.addObject("histories", histories);

		// 貸出履歴画面へ遷移
		mv.setViewName("search_history");
		return mv;
	}

	// 返却画面へ遷移
	@RequestMapping(value = "/lending/showReturn")
	public ModelAndView showReturn(
			ModelAndView mv) {

		// 貸出履歴で貸出中のもののみ全件取得
		List<HistoryDisplay> histories = historyDisplayRepository.findByStatusAndByOrderByHistoryIdDesc("貸出中");

		// 貸出履歴オブジェクトを渡す
		mv.addObject("histories", histories);

		// 返却画面へ遷移
		mv.setViewName("return_book");
		return mv;
	}

	// 返却
	@RequestMapping(value = "/lending/return")
	public ModelAndView returnBook(
			@RequestParam("history_id") int historyId,
			ModelAndView mv) {

		// 貸出中の履歴IDから書籍固有ID、会員ID、図書館IDを取得
		History history = historyRepository.findById(historyId).get(0);

		// 状態が貸出中のもの以外はホームに戻る
		if (history.getStatusId() != 2) {
			mv.setViewName("lend_book");
			return mv;
		}

		// 状態を返却に変更
		history.setStatusId(1);

		// 貸出履歴を更新
		historyRepository.save(history);

		// 履歴オブジェクトから書籍固有IDを取得
		int bookId = history.getBookId();

		// 書籍固有IDから書籍オブジェクトを取得
		Book book = bookRepository.findById(bookId).get(0);

		// 書籍を返却で保存する
		book.setStatusId(1);
		bookRepository.save(book);
		

		// 延滞者メッセージが出ていれば、メッセージを削除する
		List<Message> messages=messageRepository.findByHistoryId(historyId);
		Message message;
		if(messages.size()>0) {
			message=messages.get(0);
			message.setDeleted(true);
			messageRepository.save(message);
		}

		// 貸出履歴一覧を全件取得
		List<HistoryDisplay> histories = historyDisplayRepository.findAllByOrderByHistoryIdDesc();

		// 貸出履歴オブジェクトを渡す
		mv.addObject("histories", histories);

		// 貸出履歴画面へ遷移
		mv.setViewName("search_history");

		return mv;
	}

	// 会員と書籍を検索
	@RequestMapping(value = "/lending/search")
	public ModelAndView search(
			@RequestParam("customer_id") int customerId,
			@RequestParam("book_id") int bookId,
			ModelAndView mv) {

		// 会員IDから会員情報を取得
		Customer customer = customerRepository.findById(customerId).get(0);

		// 書籍固有IDから書籍情報を取得
		BookDisplay book = bookDisplayRepository.findById(bookId).get(0);

		// 書籍IDを取得
		int bookInfoId = book.getBookinfo_id();
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 会員情報を渡す
		mv.addObject("customer", customer);

		// 書籍情報を渡す
		mv.addObject("book", book);
		mv.addObject("bookinfo", bookInfo);

		// 図書館オブジェクトを渡す
		mv.addObject("libraries", libraries);

		// 貸出登録画面へ遷移
		mv.setViewName("lend_book");
		return mv;
	}

}
