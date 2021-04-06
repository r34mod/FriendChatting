package com.example.friendchatting.Peds;

public class Solicitud {
    String estado;
    String idChat;

    public Solicitud() {
    }

    public Solicitud(String estado, String idChat) {
        this.estado = estado;
        this.idChat = idChat;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }
}
