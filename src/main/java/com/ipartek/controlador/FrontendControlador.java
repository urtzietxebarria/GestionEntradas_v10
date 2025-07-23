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

@Controller
public class FrontendControlador {

	@Value("${ruta.imagenes}")
	String rutaFotos;

	@Autowired
	private EntradaServicio entradaServ;

	@Autowired
	private ConciertoServicio conciertoServ;

	@PostMapping("/ComprarEntrada")
	public String compraEntrada(Model model, 
			@RequestParam Integer id, 
			@RequestParam Integer cantidad,
			RedirectAttributes redirectAttributes, 
			HttpSession session) {
		
		Cliente cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);

		Concierto conci = conciertoServ.obtenerConciertoPorId(id);

		int disponibles = conci.getAforo() - conci.getEntradas_vendidas();

		if (disponibles > cantidad) {

			List<Entrada> listaEntradas = new ArrayList<>();
			
			Cliente cliLogueado = new Cliente();
			if (session.getAttribute("s_cliente_login") != null) {
				cliLogueado = (Cliente) session.getAttribute("s_cliente_login");
			}

			for (int i = 0; i < cantidad; i++) {
				Entrada entr = new Entrada();
				entr.setId(0);
				entr.setCodigo("DNI-" + UUID.randomUUID().toString());
				entr.setConcierto_id(conci);
				entr.setCliente(cliLogueado);

				if (!cliLogueado.getUser().equals("")) {

					Entrada entradaPreparada = entradaServ.insertarEntrada(entr);

					listaEntradas.add(entradaPreparada);

					conci.setEntradas_vendidas(conci.getEntradas_vendidas() + 1);
					conciertoServ.modificarConcierto(conci);

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
						e.printStackTrace();
					}

				}

			}

			StringBuilder ids = new StringBuilder();
			for (Entrada e : listaEntradas) {
				ids.append(e.getId()).append(",");
			}
			
			if (ids.length() > 0) {
				ids.deleteCharAt(ids.length() - 1); //Eliminar la última coma
			}

			//Ir a otra pagina y mostrar las entradas al usuario
			redirectAttributes.addFlashAttribute("entradasImprimir", listaEntradas);
			return "redirect:/ImprimirEntradas?ids=" + ids.toString();

		} else {
			//Ir otra vea a la pagina de la entrada, para comprar otra cantidad
			model.addAttribute("concierto", conci);
			model.addAttribute("obj_cliente", new Cliente()); //alain
			return "frmCompraEntradas";

		}
		
	}

	@GetMapping("/FrmComprarEntrada")
	public String frmCompraEntrada(Model model, 
			@RequestParam Integer id,
			HttpSession session) {
		
		model.addAttribute("concierto", conciertoServ.obtenerConciertoPorId(id));
		model.addAttribute("obj_cliente", new Cliente()); //alain
		
		Cliente cliente_sesion=new Cliente();
		if (session.getAttribute("s_cliente_login")!=null) {
			cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
		}
		
		model.addAttribute("cliente_login", cliente_sesion);
		
		if (cliente_sesion.getId()>0) {
			return "frmCompraEntradas";
		} else {
			cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
			
			model.addAttribute("cliente_login", cliente_sesion);
			model.addAttribute("mensaje_error", "Debe registrarse para poder comprar entradas.");
			model.addAttribute("obj_cliente", new Cliente());//Es temporal, se va a quitar
			model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());//Es temporal, se va a quitar
			
			return "entradas";
		}
		
	}

	@GetMapping("/ImprimirEntradas")
	public String imprimirEntradas(@RequestParam String ids, 
			Model model,
			HttpSession session) {
		String[] idsArray = ids.split(",");
		List<Entrada> listaEntradas = new ArrayList<>();

		for (String elem : idsArray) {
			try {
				int id = Integer.parseInt(elem);
				Entrada entrada = entradaServ.obtenerEntradaPorId(id);
				listaEntradas.add(entrada);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}

		model.addAttribute("entradasImprimir", listaEntradas);

		model.addAttribute("obj_cliente", new Cliente()); // alain
		
		Cliente cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
		model.addAttribute("cliente_login", cliente_sesion);
		return "imprimir";

	}
	
	
	@GetMapping("/MenuProximosConciertosCli")
	public String menuProxConciertos(Model model,
			HttpSession session) {
		
		Cliente cliente_sesion=new Cliente();
		if (session.getAttribute("s_cliente_login")!=null) {
			cliente_sesion= (Cliente) session.getAttribute("s_cliente_login");
		}
		
		model.addAttribute("cliente_login", cliente_sesion);
		
		model.addAttribute("obj_cliente", new Cliente());
		model.addAttribute("listaConciertos", conciertoServ.obtenerProximosConciertos());
		
		return "entradas";
		
	}
	
	@GetMapping("/MenuAreaPersonalCli")
	public String menuAreaPersonalCli(Model model,
			HttpSession session) {
		
		Cliente clienteSesion = (Cliente) session.getAttribute("s_cliente_login");

	    if (clienteSesion == null || clienteSesion.getId() <= 0) {
	        return "redirect:/"; 
	    }

	    List<Entrada> entradasCliente = entradaServ.obtenerEntradasPorClienteId(clienteSesion.getId());

	    model.addAttribute("cliente_login", clienteSesion);
	    model.addAttribute("obj_cliente", new Cliente());

	    model.addAttribute("entradasCliente", entradasCliente);

	    return "conciertos_confirmados";
			
	}
	
}
