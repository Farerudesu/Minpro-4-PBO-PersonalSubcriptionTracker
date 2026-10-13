package com.pbo.fareru.minpro.pbo.dao;

import com.pbo.fareru.minpro.pbo.config.Koneksi;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LanggananDAOImpl implements LanggananDAO {
    private static final String SELECT = "SELECT l.*, ly.nama_layanan, ly.kategori, m.nama_metode, m.jenis FROM langganan l JOIN layanan ly ON ly.id_layanan=l.id_layanan JOIN metode_pembayaran m ON m.id_metode=l.id_metode";
    @Override public List<Langganan> findAll() throws SQLException { List<Langganan> result=new ArrayList<>(); try(Connection c=Koneksi.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT); ResultSet rs=ps.executeQuery()){ while(rs.next()) result.add(LanggananMapper.toModel(rs)); } return result; }
    @Override public Langganan findById(String id) throws SQLException { try(Connection c=Koneksi.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT+" WHERE l.id_subscription=?")){ ps.setString(1,id); try(ResultSet rs=ps.executeQuery()){ return rs.next()?LanggananMapper.toModel(rs):null; } } }
    @Override public void save(Langganan x) throws SQLException {
        String tipe = x instanceof com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas ? "PRODUCTIVITY" : "STREAMING";
        String detail = tipe.equals("PRODUCTIVITY") ? ((com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas) x).getKapasitasStorage() : ((com.pbo.fareru.minpro.pbo.model.LanggananStreaming) x).getKualitasResolusi();
        int jumlah = tipe.equals("PRODUCTIVITY") ? ((com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas) x).getLisensiUser() : ((com.pbo.fareru.minpro.pbo.model.LanggananStreaming) x).getBatasLayar();
        try (Connection c=Koneksi.getConnection(); PreparedStatement ps=c.prepareStatement("INSERT INTO langganan (id_subscription,id_layanan,harga_bulanan,id_metode,tanggal_tagihan,status,tipe,detail,jumlah) VALUES (?,?,?,?,?,?,?,?,?)")) {
            ps.setString(1,x.getIdSubscription()); ps.setString(2,x.getLayanan().getIdLayanan()); ps.setDouble(3,x.getHargaBulanan()); ps.setString(4,x.getMetode().getIdMetode()); ps.setInt(5,x.getTanggalTagihan()); ps.setString(6,x.getStatus()); ps.setString(7,tipe); ps.setString(8,detail); ps.setInt(9,jumlah); ps.executeUpdate();
        }
    }
    @Override public void update(Langganan x) throws SQLException {
        String tipe = x instanceof com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas ? "PRODUCTIVITY" : "STREAMING";
        String detail = tipe.equals("PRODUCTIVITY") ? ((com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas) x).getKapasitasStorage() : ((com.pbo.fareru.minpro.pbo.model.LanggananStreaming) x).getKualitasResolusi();
        int jumlah = tipe.equals("PRODUCTIVITY") ? ((com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas) x).getLisensiUser() : ((com.pbo.fareru.minpro.pbo.model.LanggananStreaming) x).getBatasLayar();
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE langganan SET id_metode=?, harga_bulanan=?, tanggal_tagihan=?, status=?, detail=?, jumlah=? WHERE id_subscription=?")) {
            ps.setString(1, x.getMetode().getIdMetode());
            ps.setDouble(2, x.getHargaBulanan());
            ps.setInt(3, x.getTanggalTagihan());
            ps.setString(4, x.getStatus());
            ps.setString(5, detail);
            ps.setInt(6, jumlah);
            ps.setString(7, x.getIdSubscription());
            ps.executeUpdate();
        }
    }
    @Override public void delete(String id) throws SQLException { try(Connection c=Koneksi.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM langganan WHERE id_subscription=?")){ ps.setString(1,id); ps.executeUpdate(); } }
}
