package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.coroutines.flow.Flow

interface PasswordDBStore {
    fun getList(): Flow<List<Password>>
    suspend fun add(password: Password): Long
    suspend fun update(password: Password)
    fun get(id: Int): Password?
    suspend fun save(passwordInfo: PasswordInfo)
    suspend fun isEmpty(): Flow<Boolean>
}

class LocalPasswordDBStore(
    private val passwordDao : PasswordDao
): PasswordDBStore {
    override fun getList(): Flow<List<Password>> {
        TODO("Not yet implemented")
    }

    override suspend fun add(password: Password): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(password: Password) {
        TODO("Not yet implemented")
    }

    override fun get(id: Int): Password? {
        TODO("Not yet implemented")
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun isEmpty(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}