package com.example.ParcialPracticoArquisoft.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HC extends RepresentationModel<HC> {

    @Id @GeneratedValue
    private Long idHC;

    private String Descripcion;

    private Date fechaHC;

    private String nombreMedico;
}
