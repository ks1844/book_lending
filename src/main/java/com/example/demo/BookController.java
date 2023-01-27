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

	// リポジトリの宣言
	// 書籍情報
	@Autowired
	BookInfoRepository bookInfoRepository;

	// 書籍
	@Autowired
	BookRepository bookRepository;

	// 書籍情報の表示用
	@Autowired
	BookInfoDisplayRepository bookInfoDisplayRepository;

	// 書籍の表示用
	@Autowired
	BookDisplayRepository bookDisplayRepository;

	// カテゴリ
	@Autowired
	CategoryRepository categoryRepository;

	// 書籍のステータス
	@Autowired
	StatusRepository statusRepository;

	// 図書館
	@Autowired
	LibraryRepository libraryRepository;

	// DBでの文字数の上限
	int maxBookNameLength = 50;
	int maxBookAuthorLength = 20;

	// 書籍情報一覧画面を表示
	@RequestMapping(value = "/bookinfo")
	public ModelAndView index(
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

	// 書籍名、著者名、カテゴリから書籍情報を検索
	@RequestMapping(value = "/bookinfo/search")
	public ModelAndView search(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam(name = "category_name", defaultValue = "") String categoryName,
			ModelAndView mv) {

		// 書籍名、著者名をあいまい検索
		List<BookInfoDisplay> books;
		if (categoryName.isEmpty()) {
			// カテゴリ名が指定されていないとき、カテゴリ名で検索を行わない
			books = bookInfoDisplayRepository.findByNameLikeAndByAuthorLike(bookName, bookAuthor);
		} else {
			books = bookInfoDisplayRepository.findByNameLikeAndByAuthorLikeAndByCategory(bookName,
					bookAuthor, categoryName);
		}

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("categories", categories);
		mv.addObject("books", books);
		mv.setViewName("search_book");

		return mv;
	}

	// 一件の書籍情報を表示する画面へ遷移
	@RequestMapping(value = "/book/show/{bookinfo_id}")
	public ModelAndView showCustomer(
			@PathVariable("bookinfo_id") int bookInfoId,
			ModelAndView mv) {

		// 書籍情報IDから書籍情報を取得
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 同じ書籍情報IDで複数ある書籍を全件取得
		List<BookDisplay> books = bookDisplayRepository.findByBookInfoId(bookInfoId);

		// 一件の書籍情報を表示する画面へ遷移
		mv.addObject("bookInfo", bookInfo);
		mv.addObject("books", books);
		mv.setViewName("show_book");
		return mv;
	}

	// 書籍情報の変更画面へ遷移
	@RequestMapping(value = "/bookinfo/showUpdate")
	public ModelAndView showUpdate(
			@RequestParam("bookinfo_id") int bookInfoId,
			ModelAndView mv) {

		// 書籍情報IDから書籍情報を取得
		BookInfoDisplay bookinfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の変更画面へ遷移
		mv.addObject("bookinfo", bookinfo);
		mv.addObject("bookinfo_id", bookInfoId);
		mv.addObject("categories", categories);
		mv.setViewName("update_bookinfo");
		return mv;
	}

	// 書籍情報を変更
	@RequestMapping(value = "/bookinfo/update")
	public ModelAndView update(
			@RequestParam("bookinfo_id") int bookInfoId,
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam("category_id") int categoryId,
			ModelAndView mv) {

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();
		
		// カテゴリIDからカテゴリ名を取得
		String categoryName = categoryRepository.findById(categoryId).get(0).getName();
		
		System.err.println(categoryName);

		// 書籍情報IDから表示用の書籍情報を取得
		BookInfoDisplay bookinfoDisplay = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 入力画面の入力内容を消さないように、書籍情報を変更(保存はまだ行わない)
		bookinfoDisplay.setName(bookName);
		bookinfoDisplay.setAuthor(bookAuthor);
		bookinfoDisplay.setCategory(categoryName);

		// 文字数がDBの上限を越えるとき、変更処理を行わない
		if (bookName.length() > maxBookNameLength) {
			mv.addObject("message", "書籍名の文字数制限の" + maxBookNameLength + "文字を越えています");
			mv.addObject("bookinfo", bookinfoDisplay);
			mv.addObject("categories", categories);
			mv.setViewName("update_bookinfo");
			return mv;
		} else if (bookAuthor.length() > maxBookAuthorLength) {
			mv.addObject("message", "著者名の文字数制限の" + maxBookAuthorLength + "文字を越えています");
			mv.addObject("bookinfo", bookinfoDisplay);
			mv.addObject("categories", categories);
			mv.setViewName("update_bookinfo");
			return mv;
		}

		// 書籍情報IDから書籍情報を取得
		BookInfo bookinfo = bookInfoRepository.findById(bookInfoId).get(0);
		
		// 書籍情報を変更
		bookinfo.setName(bookName);
		bookinfo.setAuthor(bookAuthor);
		bookinfo.setCategoryId(categoryId);
		bookInfoRepository.save(bookinfo);

		// 書籍情報を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	// 書籍削除画面へ遷移
	@RequestMapping(value = "/book/showDelete")
	public ModelAndView showDelete(
			@RequestParam("book_id") int bookId,
			ModelAndView mv) {

		// 書籍IDから書籍を取得
		BookDisplay book = bookDisplayRepository.findById(bookId).get(0);
		int bookInfoId = book.getBookinfo_id();

		// 書籍情報IDから書籍情報を取得
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 書籍削除画面へ遷移
		mv.addObject("book", book);
		mv.addObject("bookInfo", bookInfo);
		mv.setViewName("delete_book");
		return mv;
	}

	// 書籍を削除
	@RequestMapping(value = "/book/delete")
	public ModelAndView delete(
			@RequestParam("book_id") int bookId,
			ModelAndView mv) {

		// 書籍IDから書籍情報を取得
		Book book = bookRepository.findById(bookId).get(0);

		// 書籍を削除
		book.setDeleted(true);
		bookRepository.save(book);

		// 書籍情報を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	// 書籍追加画面へ遷移
	@RequestMapping(value = "/book/showAdd")
	public ModelAndView showAdd(
			@RequestParam("bookinfo_id") int bookInfoId,
			ModelAndView mv) {

		// 書籍IDから書籍情報を取得
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 書籍ステータスを全件取得
		List<Status> statuses = statusRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("bookinfo", bookInfo);
		mv.addObject("libraries", libraries);
		mv.addObject("statuses", statuses);
		mv.setViewName("add_book");
		return mv;
	}

	// 書籍の追加
	@RequestMapping(value = "/book/add")
	public ModelAndView add(
			@RequestParam("bookinfo_id") int bookInfoId,
			@RequestParam("library_id") int libraryId,
			ModelAndView mv) {

		// 書籍ステータスを返却済み(status_id=1)にする
		int statusId = 1;

		// 書籍を登録
		Book book = new Book(bookInfoId, libraryId, statusId);
		bookRepository.save(book);

		// 書籍情報を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;

	}

	// 書籍情報の追加画面に遷移
	@RequestMapping(value = "/bookinfo/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 書籍情報の追加画面へ遷移
		mv.addObject("categories", categories);
		mv.addObject("libraries", libraries);
		mv.setViewName("add_bookinfo");
		return mv;
	}

	// 書籍情報を追加
	@RequestMapping(value = "/bookinfo/add")
	public ModelAndView add(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam("category_id") int categoryId,
			@RequestParam("library_id") int libraryId,
			ModelAndView mv) {

		// 書籍情報を追加
		BookInfo bookInfo = new BookInfo(bookName, bookAuthor, categoryId);
		bookInfoRepository.save(bookInfo);

		// 状態を返却済み(status_id=1)にして、書籍を追加
		int statusId = 1;
		Book book = new Book(bookInfo.getId(), libraryId, statusId);
		bookRepository.save(book);

		// 書籍情報を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報の検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

}
