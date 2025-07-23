package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.auxiliar.Auxiliar;
import com.ipartek.modelo.Cliente;
import com.ipartek.repositorio.ClienteRepositorio;

@Service
public class ClienteServicioImp implements ClienteServicio {

	@Autowired
	private ClienteRepositorio clienteRepo;
	
	@Override
	public void insertarCliente(Cliente cli) {
		if (cli.getId()==0) {
			clienteRepo.save(cli);
		}	
	}

	@Override
	public void borrarCliente(Integer id) {
		clienteRepo.deleteById(id);	
		
	}

	@Override
	public void modificarCliente(Cliente cli) {
		if(cli.getId()>0) {
			clienteRepo.save(cli);
		}
	}

	@Override
	public Cliente obtenerClientePorId(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp= id;
		}
		
		Optional<Cliente> cli= clienteRepo.findById(idTemp);
		
		if (cli.isPresent()) {
			return cli.orElse(new Cliente());
		}else {
			return new Cliente();
		}	
	}

	@Override
	public List<Cliente> obtenerTodosClientes() {
		return clienteRepo.findAll();
	}

	@Override
	public boolean validarCliente(Cliente cli) {
		//en la BD el has almacenado es  15d1c499b2fd112bb93
		String Cliente= "";
		String passTemp="";
		if(cli!=null) {
			Cliente= cli.getUser();		//Cliente
			passTemp= cli.getPass();	//  el hash de 1234 + galea.  = abd56565738cbad	
		}
		
		//1 comprobar si existe el Cliente con ese nombre. 
		//obtener todos sus datos y meterlos en un objeto. si no existe, el Cliente es vacio
		
		Cliente ClienteTemp= new Cliente();
		if (ClienteTemp!=null) {
			ClienteTemp= clienteRepo.findByUser(Cliente);
		}
		 
		
		//2 si el id de Cliente es > 0 o es null, no lo se , habra que comprobar la contraseña
		if (ClienteTemp!=null && ClienteTemp.getId()!=0) {
			//passtemp = passtemp le sumamos la sal y calculamos su SHA256.
			passTemp = Auxiliar.obtenerSHA256(passTemp+ClienteTemp.getSalt());//sha256(abd56565738cbad+Himalaya)
			System.out.println("lo qe verifico = "+passTemp);
			//si passtemp coincide com el pass del objeto del Cliente que tenemos del paso 1
				//return true
			//si no coinciden, return false
			if (passTemp.equals(ClienteTemp.getPass())) {
				return true;
			}else {
				return false;
			}
		}else {
			//4 si el id de Cliente es 0 o null, retornamos false automaticamente
			return false;
		}
	}

	@Override
	public Cliente obtenerClientePorNombre(String nombre) {
		
		Cliente cli =clienteRepo.findByUser(nombre);
		cli.setPass("");
		cli.setPepper("");
		cli.setSalt("");

		return cli;
	}

}
