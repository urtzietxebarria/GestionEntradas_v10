package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.auxiliar.Auxiliar;
import com.ipartek.modelo.Cliente;
import com.ipartek.modelo.Concierto;
import com.ipartek.servicios.ConciertoServicio;
import com.ipartek.servicios.UbicacionServicio;
import jakarta.servlet.http.HttpSession;

@Controller // Controlador para manejar la parte administrativa (backend)
public class BackendControlador {

	@Autowired // Inyecta el servicio de gestión de conciertos
	private ConciertoServicio conciertoServ;

	@Autowired // Inyecta el servicio de gestión de ubicaciones
	private UbicacionServicio ubicacionServ;

	@Value("${ruta.imagenes}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;

	// ==========================================
	// MENÚ DE GESTIÓN (BACKEND)
	// ==========================================

	/**
	 * Muestra el menú principal de gestión de conciertos
	 */
	@GetMapping("/MenuGestionConciertos")
	public String menuGestionConciertos(Model model) {
		
		model.addAttribute("obj_concierto", new Concierto()); // Objeto vacío para el formulario
		model.addAttribute("listaUbicaciones", ubicacionServ.obtenerTodasUbicaciones()); // Lista de salas
		model.addAttribute("listaConciertos", conciertoServ.obtenerTodosConciertos()); // Lista de conciertos existentes
		
		return "conciertos"; // Vista de administración de conciertos
	}

	/**
	 * Muestra el menú de entradas (aún no implementado completamente)
	 */
	@GetMapping("/MenuGestionEntradas")
	public String menuGestionEntradas(Model model, 
			HttpSession session) {
		
		// Cargar cliente de sesión (aunque este menú es más para admins)
		Cliente cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);
		
		return "entradas"; // Vista compartida o temporal para entradas
		
	}

	// ==========================================
	// GESTIÓN DE CONCIERTOS (CRUD)
	// ==========================================

	/**
	 * Inserta un nuevo concierto
	 */
	@PostMapping("/AgregarConcierto")
	public String agregarConcierto(Model model,
			@ModelAttribute Concierto obj_concierto, // Objeto recibido del formulario
			@RequestParam("foto2") MultipartFile archivo) { // Imagen enviada

		// Guardar imagen si se ha subido, sino usar una por defecto
		String nombreReal = "default.png";
		if (!archivo.getOriginalFilename().equals("")) {
			nombreReal = Auxiliar.guardarImagen(archivo, rutaFotos); // Guarda la imagen en disco
		}

		// Asigna el nombre de la imagen y guarda el concierto
		obj_concierto.setFoto(nombreReal);
		conciertoServ.insertarConcierto(obj_concierto);

		return "redirect:/MenuGestionConciertos"; // Redirige al menú de gestión
	}

	/**
	 * Elimina un concierto existente (y su foto)
	 */
	@GetMapping("/ConciertoBorrar")
	public String conciertoBorrar(@RequestParam Integer id) {
		
		// Recuperar el concierto para obtener el nombre de la imagen
		Concierto conci = conciertoServ.obtenerConciertoPorId(id);

		// Borrar imagen asociada
		Auxiliar.borrarFoto(rutaFotos, conci.getFoto());

		// Borrar concierto de la base de datos
		conciertoServ.borrarConcierto(id);

		return "redirect:/MenuGestionConciertos"; // Redirige al menú
	}

	/**
	 * Carga el formulario para modificar un concierto existente
	 */
	@GetMapping("/FrmConciertoModificar")
	public String conciertoModificar(Model model, 
			@RequestParam Integer id) {
		
		model.addAttribute("obj_concierto", conciertoServ.obtenerConciertoPorId(id)); // Concierto a modificar
		model.addAttribute("listaUbicaciones", ubicacionServ.obtenerTodasUbicaciones()); // Lista de salas
		
		return "frmModificarConcierto"; // Muestra formulario de modificación
		
	}

	/**
	 * Guarda los cambios de un concierto modificado
	 */
	@PostMapping("/ConciertoModificar")
	public String conciertoModificarReal(Model model,
			@ModelAttribute Concierto obj_concierto,
			@RequestParam("foto2") MultipartFile archivo) {

		// Si no se subió nueva foto, solo se actualiza el resto
		if (archivo.getOriginalFilename().equals("")) {
			conciertoServ.modificarConcierto(obj_concierto);
		} else {
			// Guardar nueva imagen en disco
			String nuevoNombre = Auxiliar.guardarImagen(archivo, rutaFotos);

			// Borrar imagen anterior del concierto
			Auxiliar.borrarFoto(rutaFotos, obj_concierto.getFoto());

			// Actualizar el objeto con el nuevo nombre de la imagen
			obj_concierto.setFoto(nuevoNombre);

			// Guardar cambios en la base de datos
			conciertoServ.modificarConcierto(obj_concierto);
		}

		return "redirect:/MenuGestionConciertos"; // Vuelve al menú
		
	}
	
}