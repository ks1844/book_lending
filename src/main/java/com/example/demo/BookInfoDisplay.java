package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class BookInfoDisplay {
	@Id
	@Column(name = "bookinfo_id")
	private int id;

	@Column(name = "book_name")
	private String name;

	@Column(name = "book_author")
	private String author;

	@Column(name = "category_name")
	private String category;

	public BookInfoDisplay(String name, String author, String category) {
		this.name = name;
		this.author = author;
		this.category = category;
	}

	public BookInfoDisplay(int id, String name, String author, String category) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
