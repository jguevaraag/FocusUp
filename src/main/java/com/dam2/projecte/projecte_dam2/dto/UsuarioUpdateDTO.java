package com.dam2.projecte.projecte_dam2.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor; // Opcional, dependiendo de si usas este constructor

@Data
@AllArgsConstructor
public class UsuarioUpdateDTO {

    private long id; // Necesario para identificar al usuario a actualizar

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Introduce un correo electrónico válido")
    private String email;

    // Nombre de usuario: No editable, pero necesario para el mapeo
    private String nombreUsuario; 

    // Contraseña: SIN @NotBlank y SIN @Size para permitir que sea opcional/vacía
    private String password; 

    public UsuarioUpdateDTO() {
        super();
    }
    
    // Constructor usado para pre-cargar datos (GET)
    public UsuarioUpdateDTO(String nombre, String apellidos, String email, String nombreUsuario, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password; 
    }
}
