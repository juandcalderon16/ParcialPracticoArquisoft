package com.example.ParcialPracticoArquisoft.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Paciente {

    @Id
    private Long cedula;

    @ManyToOne
    private HC hc;

    private String nombre;

}
