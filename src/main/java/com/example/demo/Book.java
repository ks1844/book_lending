package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private int bookId;

	@Column(name = "bookinfo_id")
	private int bookInfoId;

	@Column(name = "library_id")
	private int libraryId;

	@Column(name = "status_id")
	private int statusId;

	@Column(name = "is_deletede")
	private boolean isDeleted;

	public Book() {

	}

	public Book(int bookInfoId, int libraryId, int statusId) {
		this.bookInfoId = bookInfoId;
		this.libraryId = libraryId;
		this.statusId = statusId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getBookInfoId() {
		return bookInfoId;
	}

	public void setBookInfoId(int bookInfoId) {
		this.bookInfoId = bookInfoId;
	}

	public int getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
