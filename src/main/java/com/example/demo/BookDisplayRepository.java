package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookDisplayRepository extends JpaRepository<BookDisplay, Integer> {
	// カテゴリテーブルからカテゴリ名を取得

	// 全件取得
	@Query(value = "select bi.bookinfo_id,bi.book_name,bi.book_author,c.category_name "
			+ "from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id", nativeQuery = true)
	List<BookDisplay> findAll();
	
	@Query(value = "select bi.bookinfo_id,bi.book_name,bi.book_author,c.category_name "
			+ "from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id "
			+ "where bi.book_name like ?1 and bi.book_author like ?2 and c.category_name = ?3"
			,nativeQuery = true)
	List<BookDisplay> findByNameLikeAndByAuthorLikeAndByCategoryLike(String name, String author, String category);
	
	// カテゴリ名以外の書籍名、著者名のみであいまい検索
	// 以下のSQL文で動作することは確認済み
//		@Query(value = "select bi.bookinfo_id,bi.book_name,bi.book_author,c.category_name "
//				+ "from bookinfo as bi "
//				+ "inner join categories as c on bi.category_id = c.category_id "
//				+ "where bi.book_name like '%アプリ%'",nativeQuery = true)
//		List<BookDisplay> findByNameLike(String name);
	
	// カテゴリ名以外の書籍名、著者名のみであいまい検索
	@Query(value = "select bi.bookinfo_id,bi.book_name,bi.book_author,c.category_name "
			+ "from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id "
			+ "where bi.book_name like ?1",nativeQuery = true)
	List<BookDisplay> findByNameLike(String name);
	

}
