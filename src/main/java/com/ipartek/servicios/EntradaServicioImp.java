package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.modelo.Entrada;
import com.ipartek.repositorio.EntradaRepositorio;

@Service
public class EntradaServicioImp implements EntradaServicio {
	
	@Autowired
	private EntradaRepositorio entradaRepo;
	
	@Override
	public Entrada insertarEntrada(Entrada ent) {
		if (ent.getId()==0) {
			return entradaRepo.save(ent);
		}
		return new Entrada();
		
	}

	@Override
	public void borrarEntrada(Integer id) {
		entradaRepo.deleteById(id);
		
	}

	@Override
	public void modificarEntrada(Entrada ent) {
		if (ent.getId()>0) {
			entradaRepo.save(ent);
		}	
	}

	@Override
	public Entrada obtenerEntradaPorId(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp= id;
		}
		
		Optional<Entrada> ent= entradaRepo.findById(idTemp);
		
		if (ent.isPresent()) {
			return ent.orElse(new Entrada());
		}else {
			return new Entrada();
		}	
	}

	@Override
	public List<Entrada> obtenerTodasEntradas() {
		return entradaRepo.findAll();
	}

	@Override
	public List<Entrada> obtenerEntradasPorClienteId(int clienteId) {
		
		return entradaRepo.findByClienteId(clienteId);

	}

	@Override
	public List<Entrada> obtenerEntradasClienteConcierto(Integer idCliente, Integer idConcierto) {
		
		int idClienteTemp=0;
		if (idCliente!=null) {
			idClienteTemp= idCliente;
		}
		
		int idConciertoTemp=0;
		if (idConcierto!=null) {
			idConciertoTemp= idConcierto;
		}
		
		return entradaRepo.obtenerEntradasPorConciertoYClienteRepo(idClienteTemp, idConciertoTemp);
		
	}

	@Override
	public List<Entrada> obtenerTodasEntradasPorIdConcierto(Integer idConci) {
		
		int idConciTemp=0;
		if (idConci!=null) {
			idConciTemp= idConci;
		}
		
		return entradaRepo.obtenerEntradasPorConciertoId(idConciTemp);
		
	}

}
