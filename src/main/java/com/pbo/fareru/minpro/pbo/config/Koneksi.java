package com.pbo.fareru.minpro.pbo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/personal_subcrition_tracker?serverTimezone=Asia/Makassar";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Koneksi() { }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
