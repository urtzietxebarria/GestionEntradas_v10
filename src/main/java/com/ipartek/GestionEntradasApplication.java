package com.ipartek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.servlet.http.HttpServlet;

@SpringBootApplication
public class GestionEntradasApplication extends HttpServlet{

	public static void main(String[] args) {
		SpringApplication.run(GestionEntradasApplication.class, args);
	}

}
