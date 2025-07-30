package com.ipartek.controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ipartek.modelo.Cliente;
import com.ipartek.modelo.Concierto;
import com.ipartek.modelo.Entrada;
import com.ipartek.servicios.ConciertoServicio;
import com.ipartek.servicios.EntradaServicio;
import jakarta.servlet.http.HttpSession;

@Controller // Indica que esta clase es un controlador Spring MVC
public class FrontendControlador {

	@Value("${ruta.imagenes}") // Inyecta el valor de la propiedad 'ruta.imagenes' desde application.properties
	String rutaFotos;

	@Autowired // Inyecta automáticamente el servicio de gestión de entradas
	private EntradaServicio entradaServ;

	@Autowired // Inyecta automáticamente el servicio de gestión de conciertos
	private ConciertoServicio conciertoServ;


	/**
	 * Método POST para procesar la compra de entradas
	 */
	@PostMapping("/ComprarEntrada")
	public String compraEntrada(Model model, 
			@RequestParam Integer id, // ID del concierto
			@RequestParam Integer cantidad, // Cantidad de entradas a comprar
			RedirectAttributes redirectAttributes, // Para pasar datos al redireccionar
			HttpSession session) { // Para acceder a los datos de sesión del usuario
		
		// Obtener cliente logueado desde la sesión
		Cliente cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);

		// Obtener información del concierto
		Concierto conci = conciertoServ.obtenerConciertoPorId(id);

		// Calcular cuántas entradas quedan disponibles
		int disponibles = conci.getAforo() - conci.getEntradas_vendidas();
		
		//int cunatasPuedeComprar = 6 - contar cuantas entradas tiene de ese concierto
		
		//en este if añadir && cuantasPuedeComprar <  cantidad
		// Verifica si hay suficientes entradas disponibles
		if (disponibles > cantidad) {
			List<Entrada> listaEntradas = new ArrayList<>();

			// Revalidar cliente en sesión
			Cliente cliLogueado = new Cliente();
			if (session.getAttribute("s_cliente_login") != null) {
				cliLogueado = (Cliente) session.getAttribute("s_cliente_login");
			}

			// Crear y guardar la cantidad de entradas pedida
			for (int i = 0; i < cantidad; i++) {
				Entrada entr = new Entrada();
				entr.setId(0); // ID se genera en base de datos
				entr.setCodigo("DNI-" + UUID.randomUUID().toString()); // Código único
				entr.setConcierto_id(conci); // Asocia el concierto
				entr.setCliente(cliLogueado); // Asocia el cliente

				// Solo si el usuario tiene un nombre de usuario válido
				if (!cliLogueado.getUser().equals("")) {
					// Guarda la entrada en la base de datos
					Entrada entradaPreparada = entradaServ.insertarEntrada(entr);
					listaEntradas.add(entradaPreparada);

					// Actualiza la cantidad de entradas vendidas del concierto
					conci.setEntradas_vendidas(conci.getEntradas_vendidas() + 1);
					conciertoServ.modificarConcierto(conci);

					// Generar código QR
					String contenido = entr.getCodigo();
					String nombreArchivo = "qr_" + entradaPreparada.getId() + ".png";
					int ancho = 300;
					int alto = 300;

					try {
						Map<EncodeHintType, Object> hints = new HashMap<>();
						hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

						QRCodeWriter writer = new QRCodeWriter();
						BitMatrix bitMatrix = writer.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto, hints);

						Path path = new File(rutaFotos + "qr/" + nombreArchivo).toPath();
						MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
					} catch (WriterException | IOException e) {
						e.printStackTrace(); // En caso de error al generar QR
					}
				}
			}

			// Concatenar IDs de las entradas para pasarlas a la vista de impresión
			StringBuilder ids = new StringBuilder();
			for (Entrada e : listaEntradas) {
				ids.append(e.getId()).append(",");
			}
			if (ids.length() > 0) {
				ids.deleteCharAt(ids.length() - 1); // Elimina la última coma
			}

			// Redirecciona a la vista de impresión pasando las entradas creadas
			redirectAttributes.addFlashAttribute("entradasImprimir", listaEntradas);
			return "redirect:/ImprimirEntradas?ids=" + ids.toString();

		} else {
			// Si no hay entradas suficientes, vuelve a la página del concierto
			model.addAttribute("concierto", conci);
			model.addAttribute("obj_cliente", new Cliente()); // objeto temporal
			return "frmCompraEntradas";
		}
	}

	/**
	 * Método GET para mostrar el formulario de compra de entradas
	 */
	@GetMapping("/FrmComprarEntrada")
	public String frmCompraEntrada(Model model, 
			@RequestParam Integer id, // ID del concierto
			HttpSession session) {

		// Cargar información del concierto
		model.addAttribute("concierto", conciertoServ.obtenerConciertoPorId(id));
		model.addAttribute("obj_cliente", new Cliente()); // objeto temporal

		// Recuperar cliente desde sesión
		Cliente cliente_sesion = new Cliente();
		if (session.getAttribute("s_cliente_login") != null) {
			cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
		}
		model.addAttribute("cliente_login", cliente_sesion);

		// Si el cliente está logueado, muestra el formulario
		if (cliente_sesion.getId() > 0) {
			return "frmCompraEntradas";
		} else {
			// Si no está logueado, muestra mensaje de error y retorna al listado de conciertos
			model.addAttribute("mensaje_error", "Debe registrarse para poder comprar entradas.");
			model.addAttribute("obj_cliente", new Cliente()); // temporal
			model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos()); // temporal
			return "entradas";
		}
	}

	/**
	 * Muestra la página con las entradas recién compradas para imprimir
	 */
	@GetMapping("/ImprimirEntradas")
	public String imprimirEntradas(@RequestParam String ids, // IDs separados por coma
			Model model,
			HttpSession session) {

		String[] idsArray = ids.split(",");
		List<Entrada> listaEntradas = new ArrayList<>();

		// Recuperar las entradas según los IDs pasados por parámetro
		for (String elem : idsArray) {
			try {
				int id = Integer.parseInt(elem);
				Entrada entrada = entradaServ.obtenerEntradaPorId(id);
				listaEntradas.add(entrada);
			} catch (NumberFormatException e) {
				e.printStackTrace(); // Manejo de error si algún ID no es válido
			}
		}

		// Pasar entradas a la vista
		model.addAttribute("entradasImprimir", listaEntradas);
		model.addAttribute("obj_cliente", new Cliente()); // temporal

		// Agregar cliente desde sesión
		Cliente cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);

		return "imprimir"; // Nombre de la vista que muestra los QR
	}

	/**
	 * Muestra el menú de próximos conciertos para clientes
	 */
	@GetMapping("/MenuProximosConciertosCli")
	public String menuProxConciertos(Model model,
			HttpSession session) {

		Cliente cliente_sesion = new Cliente();
		if (session.getAttribute("s_cliente_login") != null) {
			cliente_sesion = (Cliente) session.getAttribute("s_cliente_login");
		}

		model.addAttribute("cliente_login", cliente_sesion);
		model.addAttribute("obj_cliente", new Cliente());
		model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());

		return "entradas"; // Muestra lista de conciertos para el cliente
	}

	/**
	 * Muestra el área personal del cliente, con los conciertos para los que tiene entradas
	 */
	@GetMapping("/MenuAreaPersonalCli")
	public String menuAreaPersonalCli(Model model,
			HttpSession session) {

		Cliente clienteSesion = new Cliente();
		if (session.getAttribute("s_cliente_login") != null) {
			clienteSesion = (Cliente) session.getAttribute("s_cliente_login");
		}

		// Obtener todas las entradas del cliente
		List<Entrada> entradasCliente = entradaServ.obtenerEntradasPorClienteId(clienteSesion.getId());
		List<Concierto> conciertosCliente = conciertoServ.obtenerTodosConciertosAsistenciaPorUsuario(clienteSesion.getId());

		model.addAttribute("cliente_login", clienteSesion);
		model.addAttribute("obj_cliente", new Cliente());
		model.addAttribute("entradasCliente", entradasCliente);
		model.addAttribute("conciertosCliente", conciertosCliente);

		return "conciertos_confirmados"; // Vista del historial del cliente
		
	}
	
	@GetMapping("/MenuEntradasUsuConci")
	public String mostrarEntradasUsuarioConcierto(Model model, 
			HttpSession session,
			@RequestParam Integer id) {

		Cliente clienteSesion = new Cliente();
		if (session.getAttribute("s_cliente_login") != null) {
			clienteSesion = (Cliente) session.getAttribute("s_cliente_login");
		}
		
		int id_cli = clienteSesion.getId();
		int id_conci = id;
		
		List<Entrada> entradasCliente = entradaServ.obtenerEntradasClienteConcierto(id_cli, id_conci);
		
		model.addAttribute("cliente_login", clienteSesion);
		model.addAttribute("obj_cliente", new Cliente());
		model.addAttribute("entradasCliente", entradasCliente);
		
		return "entradas_compradas";
		
	}
}

