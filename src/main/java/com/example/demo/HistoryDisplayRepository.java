package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDisplayRepository extends JpaRepository<HistoryDisplay, Integer> {

	String sqlSelect = "select h.history_id,cu.customer_name,bi.book_name,bi.book_author,ca.category_name,s.status_name,h.history_date,l.library_name,cu.customer_id "
			+ "from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id ";

	// 全件取得
	@Query(value = sqlSelect + "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findAllByOrderByHistoryIdDesc();

	// 指定した書籍ステータスの貸出履歴のみ取得
	@Query(value = sqlSelect
			+ "where s.status_name = :status_name "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByStatusAndByOrderByHistoryIdDesc(@Param("status_name") String statusName);

	// 指定した会員の貸出履歴のみ取得
	@Query(value = sqlSelect
			+ "where cu.customer_id = :customer_id "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByCustomerIdAndOrderByHistoryIdDesc(@Param("customer_id") int customerId);

	// 指定した日付よりも前で、指定した会員IDと書籍ステータス名に一致したものを取得
	@Query(value = sqlSelect
			+ "where history_date <= :history_date and s.status_name = :status_name and cu.customer_id = :customer_id "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByCustomerIdAndStatusNameAndHistoryDate(
			@Param("customer_id") int customerId,
			@Param("history_date") String historyDate,
			@Param("status_name") String statusName);

	// 指定した日付よりも前で書籍ステータス名が一致したものを取得
	@Query(value = sqlSelect
			+ "where history_date <= :history_date and s.status_name = :status_name "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByStatusNameAndHistoryDate(
			@Param("status_name") String statusName,
			@Param("history_date") String historyDate);
	
	// 指定した貸出履歴IDの貸出履歴を取得
	@Query(value = sqlSelect
			+ "where h.history_id = :history_id", nativeQuery = true)
	List<HistoryDisplay> findById(@Param("history_id") int history_id);

}
