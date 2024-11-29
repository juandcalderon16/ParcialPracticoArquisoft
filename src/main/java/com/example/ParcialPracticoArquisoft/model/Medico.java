package com.example.ParcialPracticoArquisoft.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Medico {

    @Id
    private Long cedula;

    private String nombre;
}
