package com.ipartek.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Entrada;

@Repository
public interface EntradaRepositorio extends JpaRepository<Entrada, Integer> {

	List<Entrada> findByClienteId(int clienteId);

}
