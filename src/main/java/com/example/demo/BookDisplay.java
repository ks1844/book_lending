package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class BookDisplay {
	@Id
	@Column(name = "book_id")
	private int id;
	
	@Column(name="bookinfo_id")
	private int bookinfo_id;

	@Column(name = "library_name")
	private String library;

	@Column(name = "status_name")
	private String status;

	@Column(name = "is_deleted")
	private String isDeleted;
	
	
	
	
	
	
}
