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
	CategoryRepository categoryRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	BookInfoRepository bookInfoRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	BookInfoDisplayRepository bookInfoDisplayRepository;

	@Autowired
	BookDisplayRepository bookDisplayRepository;

	// 書籍一覧画面を表示
	@RequestMapping(value = "/bookinfo")
	public ModelAndView index(
			ModelAndView mv) {

		// 書籍を全件取得
		List<BookInfoDisplay> bookDisply = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("books", bookDisply);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;
	}

	// 書籍名、著者名、カテゴリから検索
	@RequestMapping(value = "/bookinfo/search")
	public ModelAndView search(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam(name = "category_name", defaultValue = "") String categoryName,
			ModelAndView mv) {

		List<BookInfoDisplay> books;

		// 書籍名、著者名をあいまい検索
		if (categoryName.isEmpty()) {
			// カテゴリ名が指定されていないとき、カテゴリ名で検索を行わない
			System.err.println("no category");
			books = bookInfoDisplayRepository.findByNameLikeAndByAuthorLike(bookName, bookAuthor);
		} else {
			books = bookInfoDisplayRepository.findByNameLikeAndByAuthorLikeAndByCategoryLike(bookName,
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
	@RequestMapping(value = "/book/show/{bookinfo_id}")
	public ModelAndView showCustomer(
			@PathVariable("bookinfo_id") int bookInfoId,
			ModelAndView mv) {

		// bookinfoのIDから書籍情報を取得
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 同種で複数所蔵している書籍を全件取得
		List<BookDisplay> books = bookDisplayRepository.findByBookInfoId(bookInfoId);

		// 特定の書籍のみの書籍情報画面へ遷移
		mv.addObject("bookInfo", bookInfo);
		mv.addObject("books", books);
		mv.setViewName("show_book");
		return mv;
	}

	// 書籍情報変更画面へ遷移
	@RequestMapping(value = "/bookinfo/showUpdate")
	public ModelAndView showUpdate(
			@RequestParam("bookinfo_id") int bookInfoId,
			ModelAndView mv) {

		// bookInfoIdから書籍情報を取得
		BookInfoDisplay book = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍情報更新画面へ遷移
		mv.addObject("books", book);
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

		// bookInfoIdから書籍情報を取得
		BookInfo book = bookInfoRepository.findById(bookInfoId).get(0);

		// 書籍情報を更新	
		book.setName(bookName);
		book.setAuthor(bookAuthor);
		book.setCategoryId(categoryId);
		bookInfoRepository.save(book);

		// 書籍を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍一覧画面へ遷移
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

		// 固有IDから書籍固有の情報を取得
		BookDisplay book = bookDisplayRepository.findById(bookId).get(0);
		int bookInfoId = book.getBookinfo_id();

		// 書籍IDから書籍情報を取得
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

		// 書籍固有のIDから書籍固有の情報を取得
		Book book = bookRepository.findById(bookId).get(0);

		// 書籍を削除
		book.setDeleted(true);
		bookRepository.save(book);

		// 書籍を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
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

		System.out.println(bookInfoId);

		// 書籍IDから書籍情報を取得
		BookInfoDisplay bookInfo = bookInfoDisplayRepository.findById(bookInfoId).get(0);

		// 図書館を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 状態を全件取得
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

		// 状態を返却済み(status_id=1)にする
		int statusId = 1;

		// 書籍を登録
		Book book = new Book(bookInfoId, libraryId, statusId);
		bookRepository.save(book);

		// 書籍を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");
		return mv;

	}

	// 新しい種類の書籍の追加画面に遷移
	@RequestMapping(value = "/bookinfo/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 図書館情報を全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 新しい書籍の登録画面へ遷移
		mv.addObject("categories", categories);
		mv.addObject("libraries", libraries);
		mv.setViewName("add_bookinfo");
		return mv;
	}

	// 新しい種類の書籍を追加
	@RequestMapping(value = "/bookinfo/add")
	public ModelAndView add(
			@RequestParam("book_name") String bookName,
			@RequestParam("book_author") String bookAuthor,
			@RequestParam("category_id") int categoryId,
			@RequestParam("library_id") int libraryId,
			ModelAndView mv) {

		// 新しい種類の書籍のオブジェクトを作成
		BookInfo bookInfo = new BookInfo(bookName, bookAuthor, categoryId);

		// 新しい種類の書籍を作成
		bookInfoRepository.save(bookInfo);
		
		// 状態を返却済み(status_id=1)にする
		int statusId = 1;

		// 書籍固有のオブジェクトを作成
		Book book = new Book(bookInfo.getId(),libraryId,statusId);

		// 書籍を追加
		bookRepository.save(book);
		
		// 書籍を全件取得
		List<BookInfoDisplay> books = bookInfoDisplayRepository.findAll();

		// カテゴリを全件取得
		List<Category> categories = categoryRepository.findAll();

		// 書籍検索画面へ遷移
		mv.addObject("books", books);
		mv.addObject("categories", categories);
		mv.setViewName("search_book");

		return mv;
	}

}
