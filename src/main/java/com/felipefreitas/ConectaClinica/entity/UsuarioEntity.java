package com.felipefreitas.ConectaClinica.entity;

import com.felipefreitas.ConectaClinica.enums.RoleUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @OneToOne
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private FuncionarioEntity funcionario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleUsuario role;

}
