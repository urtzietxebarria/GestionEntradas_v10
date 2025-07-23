package com.ipartek.servicios;

import java.util.List;

import com.ipartek.modelo.Cliente;


public interface ClienteServicio {
	
	void insertarCliente(Cliente cli);
	void borrarCliente(Integer id);
	void modificarCliente(Cliente cli);
	
	Cliente obtenerClientePorId(Integer id);
	List<Cliente> obtenerTodosClientes();
	Cliente obtenerClientePorNombre(String nombre);
	
	boolean validarCliente(Cliente cli);

}
