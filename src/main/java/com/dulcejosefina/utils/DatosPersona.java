package com.dulcejosefina.utils;

public class DatosPersona {
    
    private long id;
    private String nombre;
    private String apellido;
    private long dni;
    private long cuil;
    private String email;
    private String login;
    private String password;
    private String fechaCarga;
    private char estado;
    private String detalle;
    private String tipoPersona;
    private String genero;
    private String tipoDocumento;
    private int puntosClientePrefencial;
    private char clientePerefencial;
    private DatosTelefono datosTelefono;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public long getCuil() {
        return cuil;
    }

    public void setCuil(long cuil) {
        this.cuil = cuil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public DatosTelefono getDatosTelefono() {
        return datosTelefono;
    }

    public int getPuntosClientePrefencial() {
        return puntosClientePrefencial;
    }

    public void setPuntosClientePrefencial(int puntosClientePrefencial) {
        this.puntosClientePrefencial = puntosClientePrefencial;
    }

    public char getClientePerefencial() {
        return clientePerefencial;
    }

    public void setClientePerefencial(char clientePerefencial) {
        this.clientePerefencial = clientePerefencial;
    }
    
    
    
    
}
