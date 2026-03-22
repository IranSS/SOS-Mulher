package com.example.sos_mulher.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sos_mulher.dao.AlertaDAO;
import com.example.sos_mulher.dao.ContatoDAO;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.models.Alerta;
import com.example.sos_mulher.models.Contatos;
import com.example.sos_mulher.models.User;

@Database(entities = {User.class, Contatos.class, Alerta.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDAO userDao();
    public abstract ContatoDAO contatoDAO();
    public abstract AlertaDAO alertaDAO();

    // Singleton
    private static AppDataBase instance;

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "contatos"
                    )
                    .fallbackToDestructiveMigration() // evita crash ao mudar versão
                    .build();
        }
        return instance;
    }
}