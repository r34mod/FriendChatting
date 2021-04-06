package com.example.friendchatting.Peds;

public class Estado {
    String estado;
    String fecha;
    String hora;
    String chatting;

    public Estado() {
    }

    public Estado(String estado, String fecha, String hora, String chatting) {
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.chatting=chatting;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getChatting() {
        return chatting;
    }

    public void setChatting(String chatting) {
        this.chatting = chatting;
    }
}
