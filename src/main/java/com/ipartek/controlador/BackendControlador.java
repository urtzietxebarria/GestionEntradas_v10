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

@Controller
public class BackendControlador {
	
@Autowired
private ConciertoServicio conciertoServ;

@Autowired
private UbicacionServicio ubicacionServ;

@Value("${ruta.imagenes}")
String rutaFotos; 
	
//controladores del menu del backend
	@GetMapping("/MenuGestionConciertos")
	public String menuGestionConciertos(Model model) {
		model.addAttribute("obj_concierto",new Concierto());
		model.addAttribute("listaUbicaciones",ubicacionServ.obtenerTodasUbicaciones());
		model.addAttribute("listaConciertos",conciertoServ.obtenerTodosConciertos());
		return "conciertos" ;
	}
	
	@GetMapping("/MenuGestionEntradas")
	public String menuGestionEntradas(Model model, HttpSession session) {
		
		Cliente cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);
		return "entradas" ;
	}
	
//controladores de la gestion de conciertos:
	@PostMapping("/AgregarConcierto")
	public String agregarConcierto(Model model, 
			@ModelAttribute Concierto obj_concierto, 
			@RequestParam("foto2") MultipartFile archivo) {

		//guardar la imagen en la carpeta
		String nombreReal="default.png";
		if (!archivo.getOriginalFilename().equals("")) {
			nombreReal=Auxiliar.guardarImagen(archivo, rutaFotos);
		}			
		
		//guardar en la BD
		obj_concierto.setFoto(nombreReal);
		conciertoServ.insertarConcierto(obj_concierto);
		
		return "redirect:/MenuGestionConciertos" ;
	}
	
	@GetMapping("/ConciertoBorrar")
	public String conciertoBorrar(@RequestParam Integer id) {
		Concierto conci= conciertoServ.obtenerConciertoPorId(id);
	
		Auxiliar.borrarFoto(rutaFotos,conci.getFoto());
	
		conciertoServ.borrarConcierto(id);
	
		return "redirect:/MenuGestionConciertos";
	}

	@GetMapping("/FrmConciertoModificar")
	public String conciertoModificar(Model model, @RequestParam Integer id) {
		model.addAttribute("obj_concierto", conciertoServ.obtenerConciertoPorId(id));
		model.addAttribute("listaUbicaciones",ubicacionServ.obtenerTodasUbicaciones());
		
		return "frmModificarConcierto";
	}

	@PostMapping("/ConciertoModificar")
	public String conciertoModificarReal(Model model, 
			@ModelAttribute Concierto obj_concierto, 
			@RequestParam("foto2") MultipartFile archivo) {

			if (archivo.getOriginalFilename().equals("")) {
				conciertoServ.modificarConcierto(obj_concierto);
			}else {
				
				// guardar la foto nueva
				String nuevoNombre = Auxiliar.guardarImagen(archivo, rutaFotos);
				
				// borrar la foto vieja
				Auxiliar.borrarFoto(rutaFotos, obj_concierto.getFoto());
				
				// modificar el registro con el nombre de la foto nueva
				obj_concierto.setFoto(nuevoNombre);
				conciertoServ.modificarConcierto(obj_concierto);
			}
	
		return "redirect:/MenuGestionConciertos";
	}
	
}
