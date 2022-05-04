package com.ktrobackend.db

import org.ktorm.database.Database

object DatabaseConnection {
    // Create database

    val database = Database.connect(
        url = "jdbc:mysql://127.0.0.1:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root"
    )
}