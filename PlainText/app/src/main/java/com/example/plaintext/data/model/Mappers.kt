package com.example.plaintext.data.model

fun PasswordInfo.toPassword(): Password {
    return Password(
        id = this.id,
        name = this.name,
        login = this.login,
        password = this.password,
        notes = this.notes
    )
}

fun Password.toPasswordInfo(): PasswordInfo {
    return PasswordInfo(
        id = this.id,
        name = this.name,
        login = this.login,
        password = this.password,
        notes = this.notes
    )
}