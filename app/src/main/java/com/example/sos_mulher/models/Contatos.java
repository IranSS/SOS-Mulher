package com.example.sos_mulher.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contatos {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "parentesco")
    private String grau_parentesco;
    @ColumnInfo(name = "sendMsg")
    private Boolean sendMsg;

    public int getUid() {
        return uid;
    }

    public void setUid(int id) {
        this.uid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrau_parentesco() {
        return grau_parentesco;
    }

    public void setGrau_parentesco(String grau_parentesco) {
        this.grau_parentesco = grau_parentesco;
    }

    public Boolean getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(Boolean sendMsg) {
        this.sendMsg = sendMsg;
    }
    public String returnAllInfos() {
        return "Contatos{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", grau_parentesco='" + grau_parentesco + '\'' +
                ", sendMsg=" + sendMsg +
                '}';
    }
}
