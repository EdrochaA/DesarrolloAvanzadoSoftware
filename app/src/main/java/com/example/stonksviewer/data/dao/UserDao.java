package com.example.stonksviewer.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.stonksviewer.model.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUser(String username);
}