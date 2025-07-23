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

@Controller // Marca esta clase como un controlador de Spring MVC
public class LoginControlador {

	@Autowired // Inyecta el servicio para acceder a los conciertos
	private ConciertoServicio conciertoServ;

	@Autowired // Inyecta el servicio para validar usuarios (administradores)
	private UsuarioServicio usuarioServ;

	@Autowired // Inyecta el servicio para validar clientes
	private ClienteServicio clienteServ;

	/**
	 * Valida el acceso de un administrador (Usuario)
	 * Redirige al menú de gestión si el usuario es válido, o de nuevo al login (/Admin)
	 */
	@GetMapping("/ValidarUsuario")
	public String validarUsuario(Model model, 
			@ModelAttribute Usuario obj_usuario) {
		// Verifica si las credenciales del usuario (admin) son válidas
		if (usuarioServ.validarUsuario(obj_usuario)) {
			return "redirect:/MenuGestionConciertos"; // Acceso permitido
		} else {
			// Si falla, se mantiene el usuario introducido para volver a intentarlo
			model.addAttribute("obj_usuario", obj_usuario);
			return "redirect:/Admin"; // Redirige al login de administrador
		}
	}

	/**
	 * Valida el acceso de un cliente (Cliente)
	 * Si es válido, lo guarda en sesión y muestra la vista de entradas
	 */
	@GetMapping("/ValidarCliente")
	public String validarCliente(Model model, 
			@ModelAttribute Cliente obj_cliente,
			HttpSession session) {

		// Verifica si las credenciales del cliente son válidas
		if (clienteServ.validarCliente(obj_cliente)) {

			// Si es válido, obtener el objeto completo del cliente desde su nombre de usuario
			obj_cliente = clienteServ.obtenerClientePorNombre(obj_cliente.getUser());

			// Guardar el cliente logueado en la sesión
			session.setAttribute("s_cliente_login", obj_cliente);

			// Agregar datos temporales para la vista
			model.addAttribute("obj_cliente", obj_cliente); // temporal
			model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos()); // temporal

			// Agregar cliente a la vista para mostrar su info
			Cliente cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
			model.addAttribute("cliente_login", cliente_sesion);

			return "entradas"; // Vista que se muestra tras login exitoso
		} else {
			// Si las credenciales no son válidas, se vuelve a la página de inicio
			model.addAttribute("obj_cliente", obj_cliente);
			return "redirect:/";
		}
	}

	/**
	 * Cierra la sesión del cliente
	 * Elimina al cliente logueado y vuelve a la vista de entradas
	 */
	@GetMapping("/CerrarSesion")
	public String cerrarSession(Model model, 
			HttpSession session) {

		// Elimina el atributo de sesión que indica que el cliente estaba logueado
		session.removeAttribute("s_cliente_login");

		// Carga temporalmente el listado de conciertos y un cliente vacío
		model.addAttribute("obj_cliente", new Cliente()); // temporal
		model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos()); // temporal

		// Retorna a la vista de entradas (modo público, sin cliente logueado)
		return "entradas";
	}
}
