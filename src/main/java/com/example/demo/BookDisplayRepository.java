package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDisplayRepository extends JpaRepository<BookDisplay, Integer>{
	
	// bookInfo_idから書籍一覧を取得
	@Query(value="select b.book_id,b.bookinfo_id,l.library_name,s.status_name,b.is_deleted "
			+ "from books as b "
			+ "inner join libraries as l on b.library_id = l.library_id "
			+ "inner join statuses as s on b.status_id = s.status_id "
			+ "where b.bookinfo_id = :bookinfo_id",nativeQuery=true)
	List<BookDisplay> findByBookInfoId(@Param("bookinfo_id") int bookInfoId);
	
	// book_idから書籍情報を取得
	@Query(value="select b.book_id,b.bookinfo_id,l.library_name,s.status_name,b.is_deleted "
			+ "from books as b "
			+ "inner join libraries as l on b.library_id = l.library_id "
			+ "inner join statuses as s on b.status_id = s.status_id "
			+ "where b.book_id = :book_id",nativeQuery = true)
	List<BookDisplay> findById(@Param("book_id") int bookId);

}
