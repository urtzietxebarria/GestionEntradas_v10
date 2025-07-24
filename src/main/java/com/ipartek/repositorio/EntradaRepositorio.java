package com.ipartek.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Entrada;

@Repository
public interface EntradaRepositorio extends JpaRepository<Entrada, Integer> {

	List<Entrada> findByClienteId(int clienteId);
	
	String SQL = "SELECT * FROM entradas WHERE cliente_id = :cli AND concierto_id_id = :conci;";
	@Query(value = SQL, nativeQuery = true)
	List<Entrada> obtenerEntradasPorConciertoYClienteRepo(@Param("cli") int cli, @Param("conci") int conci);

}
