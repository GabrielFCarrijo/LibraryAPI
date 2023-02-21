package com.cursogabriel.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Scheduled(cron = "0 31 18 1/1 * ?")
	public void testeAgendamentoTarefas() {
		System.out.printf("Agendamento funcionando com sucesso");
	}
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
