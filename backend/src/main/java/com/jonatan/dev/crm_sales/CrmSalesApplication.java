package com.jonatan.dev.crm_sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CrmSalesApplication {

	public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		System.setProperty("MONGODB_DB", dotenv.get("MONGODB_DB"));
		SpringApplication.run(CrmSalesApplication.class, args);
	}

}
