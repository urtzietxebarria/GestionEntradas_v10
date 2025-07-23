package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.auxiliar.Auxiliar;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

	@Autowired
	private UsuarioRepositorio usuarioRepo;
	
	@Override
	public void insertarUsuario(Usuario usu) {
		if (usu.getId()==0) {
			usuarioRepo.save(usu);
		}	
	}

	@Override
	public void borrarUsuario(Integer id) {
		usuarioRepo.deleteById(id);	
		
	}

	@Override
	public void modificarUsuario(Usuario usu) {
		if(usu.getId()>0) {
			usuarioRepo.save(usu);
		}
	}

	@Override
	public Usuario obtenerUsuarioPorId(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp= id;
		}
		
		Optional<Usuario> usu= usuarioRepo.findById(idTemp);
		
		if (usu.isPresent()) {
			return usu.orElse(new Usuario());
		}else {
			return new Usuario();
		}	
	}

	@Override
	public List<Usuario> obtenerTodosUsuarios() {
		return usuarioRepo.findAll();
	}

	@Override
	public boolean validarUsuario(Usuario usu) {
		//en la BD el has almacenado es  15d1c499b2fd112bb93
		String usuario= "";
		String passTemp="";
		if(usu!=null) {
			usuario= usu.getUser();		//usuario
			passTemp= usu.getPass();	//  el hash de 1234 + galea.  = abd56565738cbad	
		}
		
		//1 comprobar si existe el usuario con ese nombre. 
		//obtener todos sus datos y meterlos en un objeto. si no existe, el usuario es vacio
		
		Usuario usuarioTemp= new Usuario();
		if (usuarioTemp!=null) {
			usuarioTemp= usuarioRepo.buscarUsuarioPorNombre(usuario);
		}
		 
		
		//2 si el id de usuario es > 0 o es null, no lo se , habra que comprobar la contraseña
		if (usuarioTemp!=null && usuarioTemp.getId()!=0) {
			//passtemp = passtemp le sumamos la sal y calculamos su SHA256.
			passTemp = Auxiliar.obtenerSHA256(passTemp+usuarioTemp.getSalt());//sha256(abd56565738cbad+Himalaya)
			System.out.println("lo qe verifico = "+passTemp);
			//si passtemp coincide com el pass del objeto del usuario que tenemos del paso 1
				//return true
			//si no coinciden, return false
			if (passTemp.equals(usuarioTemp.getPass())) {
				return true;
			}else {
				return false;
			}
		}else {
			//4 si el id de usuario es 0 o null, retornamos false automaticamente
			return false;
		}
	}

}
