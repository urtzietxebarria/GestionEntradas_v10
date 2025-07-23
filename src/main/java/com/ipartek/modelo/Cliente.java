package com.ipartek.modelo;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="clientes")
public class Cliente {

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
	
	@ManyToMany
	private List<Entrada> entrada_cli;

	public Cliente(int id, String user, String pass, String salt, String pepper, List<Entrada> entrada_cli) {
		super();
		this.id = id;
		this.user = user;
		this.pass = pass;
		this.salt = salt;
		this.pepper = pepper;
		this.entrada_cli = entrada_cli;
	}

	public Cliente() {
		super();
		this.id = 0;
		this.user = "";
		this.pass = "";
		this.salt = "";
		this.pepper = "";
		this.entrada_cli = new ArrayList<>();
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

	public List<Entrada> getEntrada_cli() {
		return entrada_cli;
	}

	public void setEntrada_cli(List<Entrada> entrada_cli) {
		this.entrada_cli = entrada_cli;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", user=" + user + ", pass=" + pass + ", salt=" + salt + ", pepper=" + pepper
				+ ", entrada_cli=" + entrada_cli + "]";
	}
	
	

	
	
}
