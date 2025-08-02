package com.ejemplo.SCruzProgramacionNCapasMaven.ML;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int idUsuario;
//    @NotBlank(message = "Ingresa tu nombre")
//    @Size(min = 3, max = 15, message = "El Nombre debe tener entre 3 y 15 caracteres")
//    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo debe contener letras y espacios")
    private String Nombre;

//    @NotBlank(message = "Ingresa tu apellido paterno")
//    @Size(min = 3, max = 15, message = "El Apellido Paterno debe tener entre 3 y 15 caracteres")
//    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido Paterno solo debe contener letras y espacios")
    private String ApellidoPaterno;

//    @NotBlank(message = "Ingresa tu apellido paterno")
//    @Size(min = 3, max = 15, message = "El Apellido Paterno debe tener entre 3 y 15 caracteres")
//    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido Materno solo debe contener letras y espacios")
    private String ApellidoMaterno;

//    @NotEmpty(message = "Ingresa la fecha de nacimiento")
//    @Past(message = "La Fecha de Nacimiento debe ser anterior a la fecha actual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;

//    @NotBlank(message = "Ingresa tu Telefono")
//    @Pattern(regexp = "^\\d{10}$", message = "El Teléfono debe tener exactamente 10 dígitos")
    private String Telefono;

//    @NotBlank(message = "Ingresa tu Email")
//    @Pattern(regexp = "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}", message = "correo invalido regex")
    private String Email;

    private String Username;

    private String Password;

    private char Sexo;

//    @NotBlank(message = "Ingresa tu Celular")
//    @Pattern(regexp = "^\\d{10}$", message = "El Celular debe tener exactamente 10 dígitos")
    private String Celular;

    private String CURP;

    public Roll Roll;
    
    private String Imagen;
    
    private int Estatus;
    
    public UsuarioDireccion UsuarioDireccion;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public Roll getRoll() {
        return Roll;
    }

    public void setRoll(Roll Roll) {
        this.Roll = Roll;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public char getSexo() {
        return Sexo;
    }

    public void setSexo(char Sexo) {
        this.Sexo = Sexo;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public UsuarioDireccion getUsuarioDireccion() {
        return UsuarioDireccion;
    }

    public void setUsuarioDireccion(UsuarioDireccion UsuarioDireccion) {
        this.UsuarioDireccion = UsuarioDireccion;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    public int getEstatus() {
        return Estatus;
    }

    public void setEstatus(int Estatus) {
        this.Estatus = Estatus;
    }

    
    
    

}
