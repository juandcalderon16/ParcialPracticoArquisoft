package com.example.ParcialPracticoArquisoft.controller;

import com.example.ParcialPracticoArquisoft.model.HC;
import com.example.ParcialPracticoArquisoft.model.Medico;
import com.example.ParcialPracticoArquisoft.model.Paciente;
import com.example.ParcialPracticoArquisoft.repository.HCRepository;
import com.example.ParcialPracticoArquisoft.repository.MedicoRepository;
import com.example.ParcialPracticoArquisoft.repository.PacienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/historias")
@Tag(name = "Historias Clinicas", description = "Gestión de historias clínicas")
@CrossOrigin("*")
public class HCController {

    @Autowired
    HCRepository hcRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Operation(summary = "Registrar una historia clínica",
            description = "Crea una nueva historia clínica para un paciente. Si el médico no existe, se crea uno nuevo.")
    @ApiResponse(responseCode = "201", description = "Historia clínica creada exitosamente")
    @PostMapping("/{cedula}/registrar")
    public ResponseEntity<HC> registrarHistoriaClinica(
            @PathVariable @Parameter(description = "Cédula del paciente") Long cedula,
            @RequestBody @Parameter(description = "Datos de la historia clínica") HC nuevaHC,
            @RequestParam @Parameter(description = "Nombre del médico") String nombreMedico,
            @RequestParam @Parameter(description = "Cédula del médico") Long cedulaMedico) {

        // Verificar si el paciente existe
        Paciente paciente = pacienteRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con cédula: " + cedula));

        // Verificar si el médico ya existe
        Medico medico = medicoRepository.findById(cedulaMedico)
                .orElseGet(() -> {
                    // Crear un nuevo médico si no existe
                    Medico nuevoMedico = new Medico();
                    nuevoMedico.setCedula(cedulaMedico);
                    nuevoMedico.setNombre(nombreMedico);
                    return medicoRepository.save(nuevoMedico); // Guardar en la base de datos
                });

        // Configurar los datos de la historia clínica
        nuevaHC.setFechaHC(new Date()); // Registrar la fecha actual
        nuevaHC.setDescripcion(nuevaHC.getDescripcion()); // Descripción de la H.C.
        paciente.setHc(nuevaHC);
        nuevaHC.setNombreMedico(medico.getNombre());// Asociar al paciente

        // Guardar la nueva historia clínica
        HC historiaGuardada = hcRepository.save(nuevaHC);

        // Agregar enlaces HATEOAS a la respuesta
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HCController.class)
                .obtenerHistoriasClinicas(cedula)).withSelfRel();  // Enlace a la historia clínica
        historiaGuardada.add(selfLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(historiaGuardada);
    }

    @Operation(summary = "Obtener historias clínicas de un paciente",
            description = "Devuelve todas las historias clínicas asociadas a un paciente por su cédula.")
    @ApiResponse(responseCode = "200", description = "Historias clínicas obtenidas correctamente")
    @GetMapping("/{cedula}/historias-clinicas")
    public ResponseEntity<List<HC>> obtenerHistoriasClinicas(@PathVariable Long cedula) {
        Paciente paciente = pacienteRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con cédula: " + cedula));

        // Suponiendo que un Paciente tiene una lista de historias clínicas
        List<HC> historiasClinicas = Collections.singletonList(paciente.getHc());

        // Agregar enlaces HATEOAS a cada historia clínica
        for (HC historia : historiasClinicas) {
            // Enlace para ver la historia clínica en detalle
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HCController.class)
                    .obtenerHistoriasClinicas(cedula)).withSelfRel();  // Enlace a este recurso
            historia.add(selfLink);  // Agregar el enlace a la historia clínica
        }

        return ResponseEntity.ok(historiasClinicas);
    }

}