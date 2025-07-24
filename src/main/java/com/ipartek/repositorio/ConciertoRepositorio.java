package com.ipartek.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Concierto;

@Repository
public interface ConciertoRepositorio extends JpaRepository<Concierto, Integer> {

	String SQL="SELECT * FROM conciertos WHERE fecha > now();";
	@Query(value = SQL, nativeQuery = true)
	List<Concierto> obtenerProximosConciertosRepo();
	
	String SQL2="SELECT DISTINCT concierto_id_id FROM entradas WHERE cliente_id = :cliente_id;";
	@Query(value = SQL2, nativeQuery = true)
	List<Integer> obtenerConciertosUsuarioRepo(@Param("cliente_id") int cli);
	
}
