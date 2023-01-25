package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookInfoDisplayRepository extends JpaRepository<BookInfoDisplay, Integer> {
	// カテゴリテーブルからカテゴリ名を取得

	// 全件取得
	@Query(value = "select *from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id", nativeQuery = true)
	List<BookInfoDisplay> findAll();

	// カテゴリ名以外の書籍名、著者名のみであいまい検索
	@Query(value = "select * from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id "
			+ "where bi.book_name like %?1% and bi.book_author like %?2%", nativeQuery = true)
	List<BookInfoDisplay> findByNameLikeAndByAuthorLike(String name, String author);

	// 書籍、著者名、カテゴリ名で検索を行う
	@Query(value = "select * from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id "
			+ "where bi.book_name like %:name% and bi.book_author like %:author% and c.category_name = :category", nativeQuery = true)
	List<BookInfoDisplay> findByNameLikeAndByAuthorLikeAndByCategoryLike(
			@Param("name") String name, 
			@Param("author") String author,
			@Param("category") String category
			);
	
	// IDから書籍情報を取得する
	@Query(value="select * from bookinfo as bi "
			+ "inner join categories as c on bi.category_id = c.category_id "
			+ "where bi.bookinfo_id = :bookinfo_id",nativeQuery = true)
	List<BookInfoDisplay> findById(@Param("bookinfo_id") int bookInfoId);
	

}
