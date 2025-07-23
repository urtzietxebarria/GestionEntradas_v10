package com.ipartek.auxiliar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

public class Auxiliar {

	
	public static String obtenerSHA256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

            // Convertir los bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular SHA-256", e);
        }
    }
	
	public static String guardarImagen(MultipartFile archivo, String rutaFotos) {
		LocalDateTime fecha= LocalDateTime.now(); 
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
		String semilla = fecha.format(formato);
		
		//2 guardar el archivo en la carpeta
		String nombreArchivo = semilla + archivo.getOriginalFilename();
		
		Path ruta = Paths.get(rutaFotos + nombreArchivo);
		try {
			Files.write(ruta, archivo.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nombreArchivo;
	}
	
	public static void borrarFoto(String rutaFotos, String nombreFoto) {
		Path ruta = Paths.get(rutaFotos + nombreFoto);
		
		try {
			if (!nombreFoto.equals("default.jpg")) {
				Files.deleteIfExists(ruta);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
}
