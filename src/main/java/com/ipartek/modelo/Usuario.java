package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="user")
	private String user;
	
	@Column(name="pass")
	private String pass;
	
	@Column(name="salt")
	private String salt;
	
	@Column(name="pepper")
	private String pepper;

	public Usuario(int id, String user, String pass, String salt, String pepper) {
		super();
		this.id = id;
		this.user = user;
		this.pass = pass;
		this.salt = salt;
		this.pepper = pepper;
	}
	
	public Usuario() {
		super();
		this.id = 0;
		this.user = "";
		this.pass = "";
		this.salt = "";
		this.pepper = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPepper() {
		return pepper;
	}

	public void setPepper(String pepper) {
		this.pepper = pepper;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", user=" + user + ", pass=" + pass + ", salt=" + salt + ", pepper=" + pepper
				+ "]";
	}
	
	
	
}
