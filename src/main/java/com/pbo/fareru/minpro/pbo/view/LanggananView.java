package com.pbo.fareru.minpro.pbo.view;

import com.pbo.fareru.minpro.pbo.model.Langganan;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanggananView {

    public void tampilkanMenuUtama() {
        System.out.println("\n========================================================");
        System.out.println("             PERSONAL SUBSCRIPTION TRACKER              ");
        System.out.println("========================================================");
        System.out.println(" [1] Tampilkan & Cari Langganan (Read)");
        System.out.println(" [2] Tambah Langganan Baru      (Create)");
        System.out.println(" [3] Edit Data Langganan        (Update)");
        System.out.println(" [4] Batalkan / Hapus Langganan (Delete)");
        System.out.println(" [5] Ringkasan & Total Pengeluaran Bulanan");
        System.out.println(" [6] Simulasi Estimasi Biaya Langganan");
        System.out.println(" [7] Keluar");
        System.out.println("--------------------------------------------------------");
    }

    public void tampilkanHeader(String judul) {
        System.out.println("\n=== " + judul + " ===");
    }

    public void tampilkanGaris() {
        System.out.println("--------------------------------------------------------");
    }

    public void tampilkanPesan(String pesan) {
        System.out.println(pesan);
    }

    public void tampilkanPesanInfo(String pesan) {
        System.out.println("[INFO] " + pesan);
    }

    public void tampilkanPesanSukses(String pesan) {
        System.out.println("[SUKSES] " + pesan);
    }

    public void tampilkanPesanError(String pesan) {
        System.out.println("[ERROR] " + pesan);
    }

    public void tampilkanPesanPeringatan(String pesan) {
        System.out.println("[PERINGATAN] " + pesan);
    }

    public void tampilkanDaftarLangganan(ArrayList<Langganan> list) {
        if (list == null || list.isEmpty()) {
            tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            return;
        }

        tampilkanHeader("DAFTAR SELURUH LANGGANAN (DETAIL KARTU)");
        for (int i = 0; i < list.size(); i++) {
            tampilkanKartuLangganan(list.get(i), i + 1);
        }
    }

    public void tampilkanDaftarRingkas(ArrayList<Langganan> list) {
        if (list == null || list.isEmpty()) {
            tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            return;
        }

        tampilkanHeader("RINGKASAN CEPAT LANGGANAN");
        tampilkanTabelLangganan(list, true);
    }

    public void tampilkanKartuLangganan(Langganan sub, int nomor) {
        if (sub == null) return;
        System.out.println("+--------------------------------------------------------------------------+");
        if (nomor > 0) {
            String label = "[" + sub.getIdSubscription() + "] " + (sub.getLayanan() != null ? sub.getLayanan().getNamaLayanan() : "-");
            System.out.printf("| DATA #%-2d | %-60s |%n", nomor, potongTeks(label, 60));
        } else {
            System.out.printf("| KARTU INFORMASI LANGGANAN [%-46s] |%n", potongTeks(sub.getIdSubscription(), 46));
        }
        System.out.println("+--------------------------------------------------------------------------+");
        sub.tampilkanDetail();
        System.out.println("+--------------------------------------------------------------------------+");
    }

    public void tampilkanTabelLangganan(List<Langganan> list, boolean denganNomor) {
        if (list == null || list.isEmpty()) {
            tampilkanPesanInfo("Tidak ada data untuk ditampilkan.");
            return;
        }

        String border = "+-----+--------+--------------------+----------------------------+-----------------+------------+-----------+----------------------+";
        System.out.println(border);
        System.out.printf("| %-3s | %-6s | %-18s | %-26s | %-15s | %-10s | %-9s | %-20s |%n",
                "No", "ID", "Layanan", "Kategori / Tipe", "Harga/Bulan", "Tagihan", "Status", "Metode Pembayaran");
        System.out.println(border);

        for (int i = 0; i < list.size(); i++) {
            Langganan sub = list.get(i);
            String noStr = String.valueOf(i + 1);
            String idStr = sub.getIdSubscription() != null ? sub.getIdSubscription() : "-";
            String namaStr = potongTeks(sub.getLayanan() != null ? sub.getLayanan().getNamaLayanan() : "-", 18);
            String tipeStr = potongTeks(sub.getTipeLangganan() != null ? sub.getTipeLangganan() : "-", 26);
            String hargaStr = formatRupiah(sub.getHargaBulanan());
            String tglStr = "Tgl " + String.format("%02d", sub.getTanggalTagihan());
            String statusStr = potongTeks(sub.getStatus() != null ? sub.getStatus() : "-", 9);
            String metodeStr = potongTeks(sub.getMetode() != null ? sub.getMetode().getNamaMetode() : "-", 20);

            System.out.printf("| %-3s | %-6s | %-18s | %-26s | %15s | %-10s | %-9s | %-20s |%n",
                    noStr, idStr, namaStr, tipeStr, hargaStr, tglStr, statusStr, metodeStr);
        }
        System.out.println(border);
        System.out.printf("Total data: %d langganan terdaftar.%n", list.size());
    }

    private String potongTeks(String teks, int batas) {
        if (teks == null) return "-";
        if (teks.length() > batas) {
            return teks.substring(0, batas - 2) + "..";
        }
        return teks;
    }

    public String formatRupiah(double nominal) {
        return "Rp " + String.format(Locale.GERMANY, "%,.2f", nominal);
    }
}
