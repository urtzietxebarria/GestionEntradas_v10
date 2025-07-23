package com.ipartek.modelo;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="conciertos")
public class Concierto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="grupo", length = 45)
	private String grupo;
	
	@Column(name="precio", precision = 6, scale = 2 )
	private BigDecimal precio;
	
	@Column(name="fecha" , length = 10)
	private String fecha;
	
	@Column(name="aforo" , columnDefinition ="int check(aforo>0 and aforo<5000)" )
	private int aforo;
	
	@Column(name ="entradas_vendidas" , columnDefinition = "integer default 0" )
	private int entradas_vendidas ;
	
	@ManyToOne
	private Ubicacion ubicacion_id;
	
	@Column(name="foto" , length = 255)
	private String foto;

	public Concierto(int id, String grupo, BigDecimal precio, String fecha, int aforo, int entradas_vendidas,
			Ubicacion ubicacion_id, String foto) {
		super();
		this.id = id;
		this.grupo = grupo;
		this.precio = precio;
		this.fecha = fecha;
		this.aforo = aforo;
		this.entradas_vendidas = entradas_vendidas;
		this.ubicacion_id = ubicacion_id;
		this.foto = foto;
	}

	public Concierto() {
		super();
		this.id = 0;
		this.grupo = "";
		this.precio = new BigDecimal(0);
		this.fecha = "";
		this.aforo = 0;
		this.entradas_vendidas = 0;
		this.ubicacion_id = new Ubicacion();
		this.foto = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	

	public Ubicacion getUbicacion_id() {
		return ubicacion_id;
	}

	public void setUbicacion_id(Ubicacion ubicacion_id) {
		this.ubicacion_id = ubicacion_id;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public int getEntradas_vendidas() {
		return entradas_vendidas;
	}

	public void setEntradas_vendidas(int entradas_vendidas) {
		this.entradas_vendidas = entradas_vendidas;
	}

	@Override
	public String toString() {
		return "Concierto [id=" + id + ", grupo=" + grupo + ", precio=" + precio + ", fecha=" + fecha + ", aforo="
				+ aforo + ", entradas_vendidas=" + entradas_vendidas + ", ubicacion_id=" + ubicacion_id + ", foto="
				+ foto + "]";
	}
	
	
	

	
	
	
	
	
	
}
