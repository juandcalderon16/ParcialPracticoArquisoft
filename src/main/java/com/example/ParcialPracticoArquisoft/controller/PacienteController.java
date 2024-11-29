package com.example.ParcialPracticoArquisoft.controller;

import com.example.ParcialPracticoArquisoft.model.HC;
import com.example.ParcialPracticoArquisoft.model.Paciente;
import com.example.ParcialPracticoArquisoft.repository.HCRepository;
import com.example.ParcialPracticoArquisoft.repository.PacienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Gestión de pacientes y sus historias clínicas")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    HCRepository hcRepository;

    @Operation(summary = "Obtener historias clinicas de un paciente", description ="Devuelve todas las historias clinicas asociadas a un paciente por su cedula")
    @GetMapping("/{cedula}/historias-clinicas")
    public ResponseEntity<List<HC>> obtenerHistoriasClinicas(@PathVariable Long cedula) {
        Paciente paciente = pacienteRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con cédula: " + cedula));

        // Suponiendo que un Paciente tiene una lista de historias clínicas
        List<HC> historiasClinicas = Collections.singletonList(paciente.getHc());


        for (HC historia : historiasClinicas) {
            // Enlace para ver la historia clínica en detalle
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HCController.class)
                    .obtenerHistoriasClinicas(cedula)).withSelfRel();  // Enlace a este recurso
            historia.add(selfLink);  // Agregar el enlace a la historia clínica
        }

        return ResponseEntity.ok(historiasClinicas);
    }
}
