package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	List<Message> findById(int messageId);

	List<Message> findByHistoryId(int historyId);

	List<Message> findByIsDeleted(boolean isDeleted);

	@Query(value = "select m.message_id,m.message_text,h.history_id,m.is_deleted "
			+ "from messages as m "
			+ "inner join histories as h on h.history_id = m.history_id "
			+ "inner join customers as cu on cu.customer_id = h.customer_id "
			+ "where cu.customer_id = :customer_id and m.is_deleted = :is_deleted "
			+ "order by h.history_id desc", nativeQuery = true)
	List<Message> findByCustomerIdAndIsDeletedOrderByHistoryIdDesc(
			@Param("customer_id") int customerid,
			@Param("is_deleted") boolean isDeleted);

}
