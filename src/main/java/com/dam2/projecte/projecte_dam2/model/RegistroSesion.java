package com.dam2.projecte.projecte_dam2.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_sesiones")
@Data
@AllArgsConstructor
public class RegistroSesion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @Column(name = "fecha_hora")
        private LocalDateTime fechaHora;

        @Column(name = "ip_address")
        private String ipAddress;

        public RegistroSesion() {
            this.fechaHora = LocalDateTime.now();
        }
}
