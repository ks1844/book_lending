package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DelayCotroller {

	@Autowired
	HistoryDisplayRepository historyDisplayRepository;

	@Autowired
	MessageRepository messageRepository;

	// 延滞者管理ホームに遷移
	@RequestMapping(value = "/delay")
	public ModelAndView index(
			ModelAndView mv) {

		// 今日の日付を取得
		LocalDate today = LocalDate.now();

		// 貸出期限の2週間前の日付を取得 
		LocalDate checkoutDate = today.plusDays(14);
		String checkoutDateString = checkoutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// 貸出中かつ貸出期限が過ぎている貸出履歴を取得する。
		List<HistoryDisplay> histories = historyDisplayRepository
				.findByStatusNameAndHistoryDate("貸出中", checkoutDateString);

		// 削除されていない延滞者メッセージを取得
		List<Message> messages = messageRepository.findByIsDeleted(false);
		List<HistoryDisplay> sentHistories = new ArrayList<>();
		List<HistoryDisplay> unsentHistories = new ArrayList<>();
		List<Integer> sentHistoryIds = new ArrayList<>();

		for (Message message : messages) {
			if (!message.isDeleted()) {
				sentHistoryIds.add(message.getHistoryId());
			}
		}

		for (HistoryDisplay history : histories) {
			if(sentHistoryIds.contains(history.getId())){
				sentHistories.add(history);
			}else {
				unsentHistories.add(history);
			}
		}

		mv.addObject("unsent_histories", unsentHistories);
		mv.addObject("sent_histories", sentHistories);
		mv.setViewName("search_delay");
		return mv;
	}

	// 延滞者に催促のメッセージを作成し、確認画面へ遷移する
	@RequestMapping(value = "/delay/showMessage")
	public ModelAndView showMessage(
			@RequestParam("history_id") int historyId,
			ModelAndView mv) {

		HistoryDisplay history = historyDisplayRepository.findById(historyId).get(0);
		String bookName = history.getBookName();
		String bookAuthor = history.getBookAuthor();
		String customerName = history.getCustomerName();
		String libraryName = history.getLibraryName();

		// 貸出期限を求める
		String date = LocalDate.parse(history.getHistoryDate()).plusDays(14)
				.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));

		// メッセージを作成
		String messageTextFormat = "%s 様<br>"
				+ "こんにちは、%sです。<br>"
				+ "以下の図書の貸出期限が過ぎてきます。<br>"
				+ "<br>"
				+ "書籍名: %s<br>"
				+ "著者: %s<br>"
				+ "<br>"
				+ "貸出期限: %s<br>"
				+ "<br>"
				+ "早めの返却をお願いいたします。";

		String messageText = String.format(messageTextFormat, customerName, libraryName, bookName, bookAuthor, date);

		// 貸出履歴IDを渡す
		mv.addObject("history", history);

		// メッセージ内容を渡す
		mv.addObject("message", messageText);

		// 貸出期限を渡す
		mv.addObject("date", date);

		// メッセージ内容確認画面へ遷移
		mv.setViewName("confirm_delay_message");
		return mv;
	}

	// 延滞者への催促メッセージを保存する
	@RequestMapping(value = "/delay/sendMessage")
	public ModelAndView sendMessage(
			@RequestParam("history_id") int historyId,
			@RequestParam("message") String messageText,
			ModelAndView mv) {

		// メッセージを保存
		Message message = new Message(messageText, historyId, false);
		messageRepository.save(message);

		// 今日の日付を取得
		LocalDate today = LocalDate.now();

		// 貸出期限の2週間前の日付を取得
		LocalDate checkoutDate = today.plusDays(14);
		String checkoutDateString = checkoutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// 貸出中かつ貸出期限が過ぎている貸出履歴を取得する。
		List<HistoryDisplay> histories = historyDisplayRepository
				.findByStatusNameAndHistoryDate("貸出中", checkoutDateString);

		// 削除されていない延滞者メッセージを取得
		List<Message> messages = messageRepository.findByIsDeleted(false);
		List<HistoryDisplay> sentHistories = new ArrayList<>();
		List<HistoryDisplay> unsentHistories = new ArrayList<>();
		List<Integer> sentHistoryIds = new ArrayList<>();

		for (Message msg : messages) {
			if (!msg.isDeleted()) {
				sentHistoryIds.add(msg.getHistoryId());
			}
		}

		for (HistoryDisplay history : histories) {
			if(sentHistoryIds.contains(history.getId())){
				sentHistories.add(history);
			}else {
				unsentHistories.add(history);
			}
		}

		mv.addObject("unsent_histories", unsentHistories);
		mv.addObject("sent_histories", sentHistories);
		mv.setViewName("search_delay");

		return mv;
	}

}
