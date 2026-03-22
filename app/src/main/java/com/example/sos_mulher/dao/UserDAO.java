package com.example.sos_mulher.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sos_mulher.models.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE email LIKE :email")
    User findByEmail(String email);

    @Query("SELECT * FROM user WHERE uid LIKE :id")
    User findById(int id);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
