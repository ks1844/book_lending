package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	// すべての会員の名前、電話番号、メールであいまい検索
	List<Customer> findByNameLikeAndTelLikeAndMailLike(String customerName, String customerTel, String customerMail);
	
	// 退会していない会員の名前、電話番号、メールであいまい検索
	List<Customer> findByNameLikeAndTelLikeAndMailLikeAndIsDeletedLike(String customerName, String customerTel, String customerMail,boolean isDeleted);
	
	// 会員IDから会員情報を取得する
	List<Customer> findByIdLike(int customerId);
	
	// 会員IDから会員情報を取得する
	List<Customer> findById(int customerId);
	
	
	
}
