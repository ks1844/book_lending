package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="libraries")
public class Library {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "library_id")
	private int id;

	@Column(name = "library_name")
	private String name;

	@Column(name = "library_tel")
	private String tel;

	@Column(name = "library_mail")
	private String mail;

	@Column(name = "library_password")
	private String password;

	public Library() {

	}
	
	public Library(String name, String tel, String mail, String password) {
		this.name = name;
		this.tel = tel;
		this.mail = mail;
		this.password = password;
	}

	public Library(String name, String tel, String mail, String password, boolean isDeleted) {
		this.name = name;
		this.tel = tel;
		this.mail = mail;
		this.password = password;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
