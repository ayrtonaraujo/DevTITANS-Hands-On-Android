package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.model.toPassword
import com.example.plaintext.data.model.toPasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PasswordDBStore {
    fun getList(): Flow<List<PasswordInfo>>
    suspend fun add(password: PasswordInfo): Long
    suspend fun update(password: PasswordInfo)
    fun get(id: Int): Flow<PasswordInfo>
    suspend fun save(passwordInfo: PasswordInfo)
    suspend fun isEmpty(): Flow<Boolean>
}

class LocalPasswordDBStore @Inject constructor(
    private val passwordDao : PasswordDao
): PasswordDBStore {
    override fun getList(): Flow<List<PasswordInfo>> {
        return passwordDao.getAll().map { passwords ->
            passwords.map { it.toPasswordInfo() }
        }
    }

    override suspend fun add(password: PasswordInfo): Long {
        return passwordDao.insert(password.toPassword())
    }

    override suspend fun update(password: PasswordInfo) {
        passwordDao.update(password.toPassword())
    }

    override fun get(id: Int): Flow<PasswordInfo> {
        return passwordDao.get(id).map { it.toPasswordInfo() }
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        if (passwordInfo.id > 0) {
            update(passwordInfo)
        } else {
            add(passwordInfo)
        }
    }

    override suspend fun isEmpty(): Flow<Boolean> {
        return passwordDao.getRowCount().map { it == 0 }
    }
}