package com.example.sos_mulher.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sos_mulher.models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    // 🔥 LOGIN (usar = ao invés de LIKE)
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    // 🔥 BUSCA PRINCIPAL (essa que você usa no ChangePassword)
    @Query("SELECT * FROM user WHERE uid = :id LIMIT 1")
    User getUserById(int id);

    // ❌ REMOVE ESSA (inútil e confusa)
    // @Query("SELECT * FROM user WHERE uid LIKE :id")
    // User findById(int id);

    @Insert
    void insert(User user);

    // 🔥 UPDATE CORRETO
    @Query("UPDATE user SET password = :novaSenha WHERE uid = :userId")
    void updateSenhaById(int userId, String novaSenha);

    @Delete
    void delete(User user);
}