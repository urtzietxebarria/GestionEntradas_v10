package com.ipartek.servicios;

import java.util.List;

import com.ipartek.modelo.Entrada;

public interface EntradaServicio {

	
	Entrada insertarEntrada(Entrada ent);
	void borrarEntrada(Integer id);
	void modificarEntrada(Entrada ent);
	List<Entrada> obtenerEntradasPorClienteId(int clienteId);
	Entrada obtenerEntradaPorId(Integer id);
	List<Entrada> obtenerTodasEntradas();
}
