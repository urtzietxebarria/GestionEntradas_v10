package com.ipartek.servicios;

import java.util.List;

import com.ipartek.modelo.Ubicacion;

public interface UbicacionServicio {

	void insertarUbicacion(Ubicacion ubi);
	void borrarUbicacion(Integer id);
	void modificarUbicacion(Ubicacion ubi);
	void insertarUbicacionBackup(Ubicacion ubi);
	
	Ubicacion obtenerUbicacionPorId(Integer id);
	List<Ubicacion> obtenerTodasUbicaciones();
	
	
}
