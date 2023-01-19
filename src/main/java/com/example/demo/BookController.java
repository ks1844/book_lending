package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {

	@Autowired
	BookDisplayRepository bookDisplayRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	BookInfoRepository bookInfoRepository;

	@Autowired
	BookRepository bookRepository;

	// 書籍一覧画面を表示
	@RequestMapping(value = "/book")
	public ModelAndView index(
			ModelAndView mv) {

		// 書籍を全件取得
		List<BookDisplay> bookDisply = bookDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("books", bookDisply);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	// 書籍登録画面へ遷移
	@RequestMapping(value = "/book/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {

		// ステータスを全件取得
		List<Status> statuses = statusRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 書籍登録画面へ遷移
		mv.addObject("statuses", statuses);
		mv.addObject("categories", categories);
		mv.addObject("libraries", libraries);
		mv.setViewName("add_book");

		return mv;
	}

	// 書籍を登録
	@RequestMapping(value = "/book/add")
	public ModelAndView add(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam("category_id") int categoryId,
			@RequestParam("library_id") int libraryId,
			@RequestParam("status_id") int statusId,
			ModelAndView mv) {

		// bookinfoのDBに登録
		BookInfo bookInfo = new BookInfo(bookName, bookAuthor, categoryId);
		bookInfoRepository.save(bookInfo);

		// bookinfoのIDを取得
		int bookInfoId = bookInfo.getId();

		// booksのDBに登録
		Book book = new Book(bookInfoId, libraryId, statusId);
		bookRepository.save(book);

		// 書籍一覧を全件取得
		List<BookDisplay> books = bookDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍一覧画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	// 書籍名、著者名、カテゴリから検索
	@RequestMapping(value = "/book/search")
	public ModelAndView search(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam(name = "category_name", defaultValue = "") String categoryName,
			ModelAndView mv) {

		List<BookDisplay> books;

		// 書籍名、著者名をあいまい検索
		if (categoryName.isEmpty()) {
			// カテゴリ名が指定されていないとき、カテゴリ名で検索を行わない
			System.err.println("no category");
			books = bookDisplayRepository.findByNameLikeAndByAuthorLike(bookName, bookAuthor);
		} else {
			books = bookDisplayRepository.findByNameLikeAndByAuthorLikeAndByCategoryLike(bookName,
					bookAuthor, categoryName);
		}

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍一覧画面へ遷移
		mv.addObject("categories", categories);
		mv.addObject("books", books);
		mv.setViewName("search_book");

		return mv;
	}

	// 特定の書籍のみの書籍情報画面へ遷移
	@RequestMapping(value = "/book/show/{book_id}")
	public ModelAndView showCustomer(
			@PathVariable("book_id") int bookId,
			ModelAndView mv) {

		// bookinfoのIDから書籍情報を取得
		BookDisplay bookInfo = bookDisplayRepository.findById(bookId).get(0);
		
		// 同種で複数所蔵している書籍を全件取得
//		BookDisplay books=

		// 特定の書籍のみの書籍情報画面へ遷移
		mv.addObject("bookInfo", bookInfo);
//		mv.addObject("books", books);
		mv.setViewName("show_book");
		return mv;
	}

	// 書籍情報変更画面へ遷移
	@RequestMapping(value = "/book/showUpdate")
	public ModelAndView showUpdate(
			ModelAndView mv) {

		// 書籍を全件取得
		List<BookDisplay> books = bookDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報更新画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("update_book");
		return mv;
	}

	// 書籍情報を変更
	@RequestMapping(value = "/book/update")
	public ModelAndView update(
			ModelAndView mv) {

		// 書籍情報を更新

		// 書籍を全件取得
		List<BookDisplay> books = bookDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍一覧画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	@RequestMapping(value = "/book/showDelete")
	public ModelAndView showDelete(
			ModelAndView mv) {

		// 書籍削除画面へ遷移
		mv.setViewName("delete_book");
		return mv;
	}

	// 書籍を削除
	@RequestMapping(value = "/book/delete")
	public ModelAndView delete(
			ModelAndView mv) {

		// 書籍を削除

		// 書籍を全件取得
		List<BookDisplay> books = bookDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

}
