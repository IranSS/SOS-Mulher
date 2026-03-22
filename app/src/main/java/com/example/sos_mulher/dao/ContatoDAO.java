package com.example.sos_mulher.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sos_mulher.models.Contatos;

import java.util.List;

@Dao
public interface ContatoDAO {
    @Query("SELECT * FROM contatos")
    List<Contatos> getAll();
    @Query("SELECT * FROM contatos WHERE sendMsg = 1")
    List<Contatos> getContatosParaEnviar();
    @Query("SELECT * FROM user WHERE uid IN (:uid)")
    List<Contatos> loadAllByIds(int[] uid);
    @Query("SELECT * FROM user WHERE email LIKE :email")
    Contatos findByEmail(String email);
    @Query("SELECT * FROM user WHERE uid LIKE :id")
    Contatos findById(int id);
    @Insert
    void insert(Contatos contatos);
    @Delete
    void delete(Contatos contatos);
    @Update
    void update(Contatos contato);
}
