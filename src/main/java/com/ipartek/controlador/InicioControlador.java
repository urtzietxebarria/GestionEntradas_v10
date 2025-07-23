package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ipartek.modelo.Cliente;
import com.ipartek.modelo.Usuario;
import com.ipartek.servicios.ConciertoServicio;


@Controller
public class InicioControlador {

	@Autowired
	private ConciertoServicio conciertoServ;
	
	
	@GetMapping("/")
	public String cargarInicio(Model model) {	
		model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());
		model.addAttribute("obj_cliente", new Cliente());  //alain
		return "entradas";
	}
	
	
	@GetMapping("/Admin")
	public String cargarInicioAdmin(Model model) {
		model.addAttribute("obj_usuario", new Usuario());
		return "home";
	}
	

	
}
