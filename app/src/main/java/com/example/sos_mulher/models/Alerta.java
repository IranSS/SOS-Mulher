package com.example.sos_mulher.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alertas")
public class Alerta {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mensagem;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
