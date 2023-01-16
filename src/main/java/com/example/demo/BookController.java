package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {
	
	@Autowired
	BookRepository bookRepository;
	
 	@RequestMapping(value="/")
	public ModelAndView index(
			ModelAndView mv) {
 		
 		// 書籍検索画面へ遷移
 		mv.setViewName("search_book");
 		return mv;
 	}
	
	
}
