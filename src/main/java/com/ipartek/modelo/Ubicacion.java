package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ubicaciones")
public class Ubicacion {
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="nombre_sala", length = 45)
	private String nombre_sala;
	
	@Column(name="direccion", length = 90)
	private String direccion;

	public Ubicacion(int id, String nombre_sala, String direccion) {
		super();
		this.id = id;
		this.nombre_sala = nombre_sala;
		this.direccion = direccion;
	}
	
	public Ubicacion() {
		super();
		this.id = 0;
		this.nombre_sala = "";
		this.direccion = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre_sala() {
		return nombre_sala;
	}

	public void setNombre_sala(String nombre_sala) {
		this.nombre_sala = nombre_sala;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Ubicacion [id=" + id + ", nombre_sala=" + nombre_sala + ", direccion=" + direccion + "]";
	}
	
	
	

}
