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

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("UPDATE users SET phone = :phone WHERE username = :username")
    void updatePhone(String username, String phone);

    @Query("UPDATE users SET email = :email WHERE username = :username")
    void updateEmail(String username, String email);


}