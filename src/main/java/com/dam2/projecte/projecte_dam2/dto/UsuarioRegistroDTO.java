package com.dam2.projecte.projecte_dam2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRegistroDTO {

    private long id;

    
    private String nombre;

    
    private String apellidos;

    
    @Email(message = "Introduce un correo electrónico válido")
    private String email;

    
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$;._\\-\\*]).{8,}$", message = "La contraseña debe incluir al menos una mayúscula, una minúscula, un número y un símbolo ($;._-*)")
    private String password;

    public UsuarioRegistroDTO() {
        super();
    }

    public UsuarioRegistroDTO(String nombre, String apellidos, String email, String nombreUsuario, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }

    public UsuarioRegistroDTO(String email) {
        this.email = email;
    }
}
