package com.ipartek.servicios;

import java.util.List;

import com.ipartek.modelo.Concierto;



public interface ConciertoServicio {
	
	void insertarConcierto(Concierto conci);
	void borrarConcierto(Integer id);
	void modificarConcierto(Concierto conci);
	
	Concierto obtenerConciertoPorId(Integer id);
	List<Concierto> obtenerTodosConciertos();
	List<Concierto> obtenerProximosConciertos();

}
