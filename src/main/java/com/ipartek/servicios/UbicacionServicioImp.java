package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.modelo.Ubicacion;
import com.ipartek.repositorio.UbicacionRepositorio;

@Service
public class UbicacionServicioImp implements UbicacionServicio {

	@Autowired
	private UbicacionRepositorio ubicacionRepo;
	
	
	@Override
	public void insertarUbicacion(Ubicacion ubi) {
		if (ubi.getId()==0) {
			ubicacionRepo.save(ubi);
		}	
		
	}
	
	@Override
	public void insertarUbicacionBackup(Ubicacion ubi) {
			ubicacionRepo.save(ubi);
	}

	@Override
	public void borrarUbicacion(Integer id) {
		ubicacionRepo.deleteById(id);	
		
	}

	@Override
	public void modificarUbicacion(Ubicacion ubi) {
		if(ubi.getId()>0) {
			ubicacionRepo.save(ubi);
		}
		
	}

	@Override
	public Ubicacion obtenerUbicacionPorId(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp= id;
		}
		
		Optional<Ubicacion> ubi= ubicacionRepo.findById(idTemp);
		
		if (ubi.isPresent()) {
			return ubi.orElse(new Ubicacion());
		}else {
			return new Ubicacion();
		}	
	}

	@Override
	public List<Ubicacion> obtenerTodasUbicaciones() {
		return ubicacionRepo.findAll();
	}

}
