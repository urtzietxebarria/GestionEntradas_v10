package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

	String SQL= "SELECT * FROM usuarios where user = :usu;" ;
	@Query(value= SQL, nativeQuery = true)
	Usuario buscarUsuarioPorNombre(@Param("usu") String usuario);
	
}
