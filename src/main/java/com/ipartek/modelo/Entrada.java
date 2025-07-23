package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="entradas")
public class Entrada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="codigo" , length = 255)
	private String codigo;
	
	@ManyToOne
	private Concierto concierto_id;
	
	@ManyToOne
	private Cliente cliente;

	public Entrada(int id, String codigo, Concierto concierto_id, Cliente cliente) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.concierto_id = concierto_id;
		this.cliente = cliente;
	}

	public Entrada() {
		super();
		this.id = 0;
		this.codigo = "";
		this.concierto_id = new Concierto();
		this.cliente = new Cliente();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Concierto getConcierto_id() {
		return concierto_id;
	}

	public void setConcierto_id(Concierto concierto_id) {
		this.concierto_id = concierto_id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Entrada [id=" + id + ", codigo=" + codigo + ", concierto_id=" + concierto_id + ", cliente="
				+ cliente + "]";
	}
	
	

}
