package com.ipartek.servicios;

import java.util.List;

import com.ipartek.modelo.Usuario;

public interface UsuarioServicio {
	
	void insertarUsuario(Usuario usu);
	void borrarUsuario(Integer id);
	void modificarUsuario(Usuario usu);
	
	Usuario obtenerUsuarioPorId(Integer id);
	List<Usuario> obtenerTodosUsuarios();
	
	boolean validarUsuario(Usuario usu);

}
