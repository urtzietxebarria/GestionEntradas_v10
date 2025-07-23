package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ipartek.modelo.Cliente;
import com.ipartek.modelo.Usuario;
import com.ipartek.servicios.ClienteServicio;
import com.ipartek.servicios.ConciertoServicio;
import com.ipartek.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginControlador {
	@Autowired
	private ConciertoServicio conciertoServ;
	
	@Autowired
	private UsuarioServicio usuarioServ;
	
	@Autowired
	private ClienteServicio clienteServ;
	
	
	@GetMapping("/ValidarUsuario")
	public String validarUsuario(Model model, @ModelAttribute Usuario obj_usuario) {	
		if (usuarioServ.validarUsuario(obj_usuario)) {
			return "redirect:/MenuGestionConciertos";
		} else {
			model.addAttribute("obj_usuario", obj_usuario);
			return "redirect:/Admin";
		}
	}
	
	@GetMapping("/ValidarCliente")
	public String validarCliente(Model model, @ModelAttribute Cliente obj_cliente, 
			HttpSession session) {	
		if (clienteServ.validarCliente(obj_cliente)) {

			//metere en la sesion algo para saber que tengo un cliente
			//que se ha logueado
			obj_cliente=clienteServ.obtenerClientePorNombre(obj_cliente.getUser());
			
			session.setAttribute("s_cliente_login", obj_cliente);
			model.addAttribute("obj_cliente", obj_cliente);//es temporal, se va a quitar
			model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());//es temporal, se va a quitar
			
			Cliente cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
			model.addAttribute("cliente_login", cliente_sesion);
			
			return "entradas"; //en el futuro se va a cambiar
		} else {
			model.addAttribute("obj_cliente", obj_cliente);
			return "redirect:/";
		}
	}
	
	
	@GetMapping("/CerrarSesion")
	public String cerrarSession(Model model, HttpSession session) {

		session.removeAttribute("s_cliente_login");
		
		model.addAttribute("obj_cliente", new Cliente());//es temporal, se va a quitar
		model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());//es temporal, se va a quitar
		
		return "entradas";
	}


}
