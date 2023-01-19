package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LendingController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BookInfoRepository bookInfoRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	@RequestMapping(value="/lending")
	public ModelAndView index(
			ModelAndView mv) {
		
		// 貸出一覧へ遷移
		mv.setViewName("search_lending");
		return mv;
	}
	
	// 貸出登録画面へ遷移
	@RequestMapping(value="/lending/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {
		
		// 貸出登録画面へ遷移
		mv.setViewName("add_lending");
		return mv;
	}
	
	// 貸出登録
	@RequestMapping(value="/lending/add")
	public ModelAndView add(
			ModelAndView mv) {
		
		// 貸出登録
		
		
		// 貸出一覧へ遷移
		mv.setViewName("search_lending");
		return mv;
	}
	
	// 貸出返却画面へ遷移
	@RequestMapping(value="/lending/showReturn")
	public ModelAndView showReturn(
			ModelAndView mv) {
		
		// 貸出返却画面へ遷移
		mv.setViewName("delete_lending");
		return mv;	
	}
	
	// 貸出返却
	@RequestMapping(value = "/lending/return")
	public ModelAndView returnLending(
			ModelAndView mv) {
		
		// 貸出返却
		
		// 貸出一覧へ遷移
		mv.setViewName("search_lending");
		return mv;
	}
	
	
}
