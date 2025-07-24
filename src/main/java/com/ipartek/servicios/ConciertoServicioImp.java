package com.ipartek.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Concierto;
import com.ipartek.repositorio.ConciertoRepositorio;

@Service
public class ConciertoServicioImp implements ConciertoServicio {

	
	@Autowired
	private ConciertoRepositorio conciertoRepo;
	
	
	@Override
	public void insertarConcierto(Concierto conci) {
		
		if (conci.getId()==0) {
			
			conciertoRepo.save(conci);
			
		}	
		
	}

	@Override
	public void borrarConcierto(Integer id) {
		
		conciertoRepo.deleteById(id);	
		
	}

	@Override
	public void modificarConcierto(Concierto conci) {
		
		if(conci.getId()>0) {
			
			conciertoRepo.save(conci);
			
		}
		
	}

	@Override
	public Concierto obtenerConciertoPorId(Integer id) {
		
		int idTemp=0;
		
		if (id!=null) {
			
			idTemp= id;
			
		}
		
		Optional<Concierto> conci = conciertoRepo.findById(idTemp);
		
		if (conci.isPresent()) {
			
			return conci.orElse(new Concierto());
			
		}else {
			
			return new Concierto();
			
		}		
	}

	@Override
	public List<Concierto> obtenerTodosConciertos() {
		
		return conciertoRepo.findAll();
	}

	@Override
	public List<Concierto> obtenerProximosConciertos() {
		
		return conciertoRepo.obtenerProximosConciertosRepo();
		
	}

	@Override
	public List<Concierto> obtenerTodosConciertosAsistenciaPorUsuario(Integer id) {
		
		int idTemp=0;
		if (id != null) {
			idTemp = id;
		}
		
		List<Integer> listaIdConciertos = conciertoRepo.obtenerConciertosUsuarioRepo(idTemp);
		List<Concierto> listaConciertos = new ArrayList<>();
		
		for(Integer elem : listaIdConciertos) {
			
			listaConciertos.add(conciertoRepo.findById(elem).orElse(new Concierto()));
			
		}
		
		return listaConciertos;
	}

}
