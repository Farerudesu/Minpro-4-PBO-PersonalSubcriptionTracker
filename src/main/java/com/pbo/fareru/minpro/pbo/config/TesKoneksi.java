package com.pbo.fareru.minpro.pbo.config;

import java.sql.Connection;

public final class TesKoneksi {
    private TesKoneksi() { }
    public static void main(String[] args) {
        try (Connection connection = Koneksi.getConnection()) {
            System.out.println("Koneksi database berhasil: " + connection.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            System.err.println("Koneksi database gagal: " + e.getMessage());
        } finally {
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Exception ignored) { }
        }
    }
}
