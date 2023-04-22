package com.example.mc_assignment_6232;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<user_data> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    user_data getUserById(int id);


    @Insert
    void insertUser(user_data user);

    @Update
    void updateUser(user_data user);

    @Delete
    void deleteUser(user_data user);

    @Query("DELETE FROM users")
    void deleteAll();
}
