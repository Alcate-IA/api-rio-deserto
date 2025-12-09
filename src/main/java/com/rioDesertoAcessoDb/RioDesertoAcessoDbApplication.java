package com.rioDesertoAcessoDb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // ← ADICIONE ESTA LINHA
public class RioDesertoAcessoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(RioDesertoAcessoDbApplication.class, args);
		System.out.println("------>Operação Rio Deserto-Alcateia iniciada!");
	}

}
