package com.example.friendchatting.Peds;

public class Users {
    private String id;
    private String nombre;
    private String email;
    private String photo;
    private String estado;
    private String hora;
    private String fecha;
    private int solicitud;
    private int mensaje;
    private String telefono;

    public Users() {

    }

    public Users(String id, String nombre, String email, String photo, String estado, String hora, String fecha, int solicitud, int mensaje, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.photo = photo;
        this.estado = estado;
        this.hora = hora;
        this.fecha = fecha;
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(int solicitud) {
        this.solicitud = solicitud;
    }

    public int getMensaje() {
        return mensaje;
    }

    public void setMensaje(int mensaje) {
        this.mensaje = mensaje;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
