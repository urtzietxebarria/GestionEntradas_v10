package com.ipartek.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.modelo.Ubicacion;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class InicializarDatos {

	@Autowired
	private UbicacionServicio ubicacionServ;

	@PostConstruct
	@Transactional
	public void inicializarDatos() {
		ubicacionServ.insertarUbicacionBackup(new Ubicacion(1, "Sala RockStar", "Gran Vía de Don Diego López de Haro 89, Bilbao"));
		ubicacionServ.insertarUbicacionBackup(new Ubicacion(2, "Sala Azkena", "Ibáñez de Bilbao Kalea 26, Bilbao"));
		ubicacionServ.insertarUbicacionBackup(new Ubicacion(3, "Kafe Antzokia", "Done Bikendi Kalea 2, Bilbao"));
		ubicacionServ.insertarUbicacionBackup(new Ubicacion(4, "Santana 27", "Tellería Kalea 27, Bilbao"));
	}

}
