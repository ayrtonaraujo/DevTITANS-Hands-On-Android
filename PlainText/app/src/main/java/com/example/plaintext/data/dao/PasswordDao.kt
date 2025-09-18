package com.example.plaintext.data.dao

import androidx.room.Dao
import com.example.plaintext.data.model.Password

@Dao
abstract class PasswordDao : BaseDao<Password> {
}