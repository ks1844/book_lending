package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class LibraryController {

	@Autowired
	HttpSession session;

	@Autowired
	LibraryRepository libraryRepository;

	@RequestMapping(value = "/library")
	public String index() {

		// セッションを削除
		session.invalidate();
		return "login_library";
	}

	// ログイン
	@RequestMapping(value = "/library/login", method = RequestMethod.POST)
	public ModelAndView login(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			ModelAndView mv) {

		// 名前が未入力のとき、メッセージを送る
		if (name == null || name.length() == 0) {
			mv.addObject("message", "名前を入力してください");
			mv.setViewName("login_library");
			return mv;
		}

		session.setAttribute("name", name);

		mv.setViewName("/book");
		return mv;
	}

	// ログアウト
	@RequestMapping(value = "/logout")
	public String logout() {
		return index();
	}

	// 管理者登録画面へ遷移
	@RequestMapping(value = "/library/showAdd")
	public ModelAndView showAdd(
			ModelAndView mv) {
		// 新規登録画面へ遷移
		mv.setViewName("add_library");
		return mv;
	}

	// 管理者を登録
	@RequestMapping(value = "/library/add", method = RequestMethod.POST)
	public ModelAndView addLibrary(
			@RequestParam("name") String name,
			@RequestParam("tel") String tel,
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			ModelAndView mv) {

		// DBに管理者を登録
		Library library = new Library(name, tel, mail, password);
		libraryRepository.save(library);

		// 全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 管理者検索画面へ遷移
		mv.addObject("customers", libraries);
		mv.setViewName("search_library");
		return mv;
	}

	// 名前、電話番号、メールからあいまい検索
	@RequestMapping(value = "/library/search")
	public ModelAndView searchLibrary(
			@RequestParam("name") String libraryName,
			@RequestParam("tel") String libraryTel,
			@RequestParam("mail") String libraryMail,
			@RequestParam(name = "is_deleted", defaultValue = "0") String isDeletedString,
			ModelAndView mv) {

		// あいまい検索のため、それぞれの文字列の前後に%を追加
		libraryName = "%" + libraryName + "%";
		libraryTel = "%" + libraryTel + "%";
		libraryMail = "%" + libraryMail + "%";

		// 検索結果を取得
		List<Library> libraries = libraryRepository.findByNameLikeAndTelLikeAndMailLike(libraryName, libraryTel,
				libraryMail);

		// 管理者検索画面へ遷移
		mv.addObject("customers", libraries);
		mv.setViewName("search_library");

		return mv;
	}

	// 管理者情報の変更画面へ遷移
	@RequestMapping(value = "/library/showUpdate")
	public ModelAndView showUpdate(
			@RequestParam("library_id") int libraryId,
			ModelAndView mv) {

		// 管理者IDから管理者情報を取得
		Library library = libraryRepository.findByIdLike(libraryId).get(0);

		// 管理者情報更新画面へ遷移
		mv.addObject("library", library);
		mv.setViewName("update_library");
		return mv;
	}

	// 管理者情報の変更
	@RequestMapping(value = "/library/update", method = RequestMethod.POST)
	public ModelAndView updateLibrary(
			@RequestParam("library_id") int libraryId,
			@RequestParam("library_name") String libraryName,
			@RequestParam("library_tel") String libraryTel,
			@RequestParam("library_mail") String libraryMail,
			@RequestParam("library_password") String libraruPassword,
			ModelAndView mv) {

		// 管理者情報を変更
		Library library = libraryRepository.findByIdLike(libraryId).get(0);
		library.setName(libraryName);
		library.setTel(libraryTel);
		library.setMail(libraryMail);
		library.setPassword(libraruPassword);

		libraryRepository.save(library);

		// 全件取得
		List<Library> libraries = libraryRepository.findAll();

		// 管理者検索画面へ遷移
		mv.addObject("customers", libraries);
		mv.setViewName("search_library");
		return mv;

	}

}
