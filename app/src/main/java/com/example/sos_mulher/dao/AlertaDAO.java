package com.example.sos_mulher.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sos_mulher.models.Alerta;

import java.util.List;

@Dao
public interface AlertaDAO {

    @Insert
    void insert(Alerta alerta);

    @Query("SELECT * FROM alertas ORDER BY id DESC LIMIT 10")
    List<Alerta> getUltimosAlertas();
}
