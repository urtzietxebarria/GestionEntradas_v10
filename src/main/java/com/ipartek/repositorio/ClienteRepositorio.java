package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

	Cliente findByUser(String nombre);

	
	

	
}
