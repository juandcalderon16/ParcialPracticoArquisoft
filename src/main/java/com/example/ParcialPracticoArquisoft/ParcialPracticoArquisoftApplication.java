package com.example.ParcialPracticoArquisoft;

import com.example.ParcialPracticoArquisoft.model.HC;
import com.example.ParcialPracticoArquisoft.model.Medico;
import com.example.ParcialPracticoArquisoft.model.Paciente;
import com.example.ParcialPracticoArquisoft.repository.HCRepository;
import com.example.ParcialPracticoArquisoft.repository.MedicoRepository;
import com.example.ParcialPracticoArquisoft.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class ParcialPracticoArquisoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParcialPracticoArquisoftApplication.class, args);
	}

	@Autowired
	HCRepository hcRepository;

	@Autowired
	MedicoRepository medicoRepository;

	@Autowired
	PacienteRepository pacienteRepository;


	@Bean
	CommandLineRunner initData(PacienteRepository pacienteRepository,
							   MedicoRepository medicoRepository,
							   HCRepository hcRepository) {
		return args -> {

			// Crear médicos
			Medico medico1 = new Medico();
			medico1.setCedula(987654L);
			medico1.setNombre("Dr. García");
			medicoRepository.save(medico1);

			Medico medico2 = new Medico();
			medico2.setCedula(456789L);
			medico2.setNombre("Dra. López");
			medicoRepository.save(medico2);

			// Crear historias clínicas
			HC historia1 = new HC();
			historia1.setDescripcion("Consulta por fiebre.");
			historia1.setFechaHC(new Date());
			historia1.setNombreMedico(medico1.getNombre());
			hcRepository.save(historia1);


			HC historia2 = new HC();
			historia2.setDescripcion("Dolor abdominal.");
			historia2.setFechaHC(new Date());
			historia2.setNombreMedico(medico2.getNombre());
			hcRepository.save(historia2);


			// Crear pacientes
			Paciente paciente1 = new Paciente();
			paciente1.setCedula(123456L);
			paciente1.setNombre("Juan Diego");
			paciente1.setHc(historia1);
			pacienteRepository.save(paciente1);

			Paciente paciente2 = new Paciente();
			paciente2.setCedula(654321L);
			paciente2.setNombre("María Pérez");
			paciente2.setHc(historia2);
			pacienteRepository.save(paciente2);

			System.out.println("Datos de prueba creados en la base de datos H2.");
		};
	}
}
