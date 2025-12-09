package com.dam2.projecte.projecte_dam2.model;

import java.time.LocalDateTime;

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

        public RegistroSesion(Usuario usuario, String ipAddress) {
            this.usuario = usuario;
            this.ipAddress = ipAddress;
            this.fechaHora = LocalDateTime.now();
        }

        

        public Long getId() {
            return id;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public LocalDateTime getFechaHora() {
            return fechaHora;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public void setFechaHora(LocalDateTime fechaHora) {
            this.fechaHora = fechaHora;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        
}
