package com.pbo.fareru.minpro.pbo.dao;

import com.pbo.fareru.minpro.pbo.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;

final class LanggananMapper {
    private LanggananMapper() { }
    static Langganan toModel(ResultSet rs) throws SQLException {
        Layanan layanan = new Layanan(rs.getString("id_layanan"), rs.getString("nama_layanan"), rs.getString("kategori"));
        MetodePembayaran metode = new MetodePembayaran(rs.getString("id_metode"), rs.getString("nama_metode"), rs.getString("jenis"));
        Langganan item;
        String tipe = rs.getString("tipe");
        if ("PRODUCTIVITY".equalsIgnoreCase(tipe)
                || "AI_TOOLS".equalsIgnoreCase(tipe)
                || "CLOUD".equalsIgnoreCase(tipe)
                || "HOSTING".equalsIgnoreCase(tipe)
                || "DEVELOPMENT".equalsIgnoreCase(tipe)) {
            item = new LanggananProduktivitas(rs.getString("id_subscription"), layanan, rs.getDouble("harga_bulanan"), metode, rs.getInt("tanggal_tagihan"), rs.getString("detail"), rs.getInt("jumlah"));
        } else {
            item = new LanggananStreaming(rs.getString("id_subscription"), layanan, rs.getDouble("harga_bulanan"), metode, rs.getInt("tanggal_tagihan"), rs.getString("detail"), rs.getInt("jumlah"));
        }
        item.setStatus(rs.getString("status"));
        return item;
    }
}
