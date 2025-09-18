package com.example.plaintext.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty

// Password deve ser uma entidade do Room
@Entity(
    tableName = "passwords"
)
@Immutable
data class Password(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "notes") val notes: String
)

// PasswordInfo deve ser uma classe de dados serializável
@Serializable
@Parcelize
data class PasswordInfo(
    val id: Int,
    val name: String,
    val login: String,
    val password: String,
    val notes: String,
) : Parcelable {
    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Password =
        Password(
            id = id,
            name = name,
            login = login,
            password = password,
            notes = notes
        )
}