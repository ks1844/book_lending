package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDisplayRepository extends JpaRepository<HistoryDisplay, Integer> {

	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findAllByOrderByHistoryIdDesc();

	// 貸出中の貸出履歴のみ取得
	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "where s.status_name = :status_name "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByStatusAndByOrderByHistoryIdDesc(@Param("status_name") String statusName);

	// 貸出中の貸出履歴のみ取得
	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "where cu.customer_id = :customer_id "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByCustomerIdAndOrderByHistoryIdDesc(@Param("customer_id") int customerId);

	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "where history_date <= :history_date and s.status_name = :status_name and cu.customer_id = :customer_id "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByCustomerIdAndStatusNameAndHistoryDate(
			@Param("customer_id") int customerId,
			@Param("history_date") String historyDate,
			@Param("status_name") String statusName);

	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "where history_date <= :history_date and s.status_name = :status_name "
			+ "order by h.history_id desc ", nativeQuery = true)
	List<HistoryDisplay> findByStatusNameAndHistoryDate(
			@Param("status_name") String statusName,
			@Param("history_date") String historyDate
			);

	@Query(value = "select * from histories as h "
			+ "inner join books as b on h.book_id = b.book_id "
			+ "inner join bookinfo as bi on b.bookinfo_id = bi.bookinfo_id "
			+ "inner join libraries as l on h.library_id = l.library_id "
			+ "inner join statuses as s on h.status_id = s.status_id "
			+ "inner join customers as cu on h.customer_id = cu.customer_id "
			+ "inner join categories as ca on bi.category_id = ca.category_id "
			+ "where h.history_id = :history_id", nativeQuery = true)
	List<HistoryDisplay> findById(@Param("history_id") int history_id);

}
