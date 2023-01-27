package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	// リポジトリの宣言
	// 書籍情報の表示用
	@Autowired
	BookInfoDisplayRepository bookInfoDisplayRepository;
	
	// カテゴリ
	@Autowired
	CategoryRepository categoryRepository;
	
	// ホーム画面を書籍情報の検索画面
	@RequestMapping(value = "/")
	public ModelAndView indexHome(
			ModelAndView mv) {

		// 書籍情報を全件取得
		List<BookInfoDisplay> bookDisply = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("books", bookDisply);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}


}
