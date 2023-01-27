package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class HistoryDisplay {
	@Id
	@Column(name = "history_id")
	private int id;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "customer_id")
	private String customerId;

	@Column(name = "book_name")
	private String bookName;

	@Column(name = "book_author")
	private String bookAuthor;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "status_name")
	private String statusName;

	@Column(name = "history_date")
	private String historyDate;

	@Column(name = "library_name")
	private String libraryName;

}
