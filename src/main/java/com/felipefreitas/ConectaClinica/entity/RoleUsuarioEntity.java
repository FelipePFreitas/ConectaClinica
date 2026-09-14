package com.felipefreitas.ConectaClinica.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "roles_usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;
}
