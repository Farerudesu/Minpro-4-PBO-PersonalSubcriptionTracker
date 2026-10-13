package com.pbo.fareru.minpro.pbo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.pbo.fareru.minpro.pbo.config.Koneksi;
import com.pbo.fareru.minpro.pbo.dao.LanggananDAO;
import com.pbo.fareru.minpro.pbo.dao.LanggananDAOImpl;
import com.pbo.fareru.minpro.pbo.model.DapatDidiskon;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;
import com.pbo.fareru.minpro.pbo.model.Layanan;
import com.pbo.fareru.minpro.pbo.model.MetodePembayaran;
import com.pbo.fareru.minpro.pbo.view.LanggananView;

public class LanggananController {
    private ArrayList<Langganan> listLangganan;
    private ArrayList<Layanan> listLayanan;
    private ArrayList<MetodePembayaran> listMetode;
    private LanggananView view;
    private Scanner scanner;
    private final LanggananDAO langgananDAO = new LanggananDAOImpl();

    public LanggananController(LanggananView view) {
        this.view = view;
        this.scanner = new Scanner(System.in);
        this.listLangganan = new ArrayList<>();
        this.listLayanan = new ArrayList<>();
        this.listMetode = new ArrayList<>();
        muatDataDatabase();
    }

    private void muatDataDatabase() {
        try (Connection c = Koneksi.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT id_layanan, nama_layanan, kategori FROM layanan ORDER BY id_layanan");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listLayanan.add(new Layanan(rs.getString("id_layanan"), rs.getString("nama_layanan"), rs.getString("kategori")));
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT id_metode, nama_metode, jenis FROM metode_pembayaran ORDER BY id_metode");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listMetode.add(new MetodePembayaran(rs.getString("id_metode"), rs.getString("nama_metode"), rs.getString("jenis")));
                }
            }
            listLangganan.addAll(langgananDAO.findAll());
        } catch (SQLException e) {
            view.tampilkanPesanError("Database gagal dibaca: " + e.getMessage());
            view.tampilkanPesanInfo("Memuat data dummy awal lokal...");
            inisialisasiDummyData();
        }

        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Database belum memiliki data langganan. Anda dapat mengimpor database/personal_subcrition_tracker.sql atau menambah data via Menu 2.");
        }
    }

    private void inisialisasiDummyData() {
        if (!listLayanan.isEmpty() && !listLangganan.isEmpty()) return;

        Layanan netflix = new Layanan("LYN01", "Netflix", "Hiburan Film & Serial");
        Layanan spotify = new Layanan("LYN02", "Spotify", "Streaming Musik & Audio");
        Layanan googleOne = new Layanan("LYN03", "Google One", "Cloud Storage & Backup");
        Layanan ms365 = new Layanan("LYN04", "Microsoft 365", "Office & Produktivitas");

        listLayanan.add(netflix);
        listLayanan.add(spotify);
        listLayanan.add(googleOne);
        listLayanan.add(ms365);

        MetodePembayaran gopay = new MetodePembayaran("PAY01", "GoPay", "E-Wallet");
        MetodePembayaran mandiriCC = new MetodePembayaran("PAY02", "Kartu Kredit Mandiri", "Credit Card");
        MetodePembayaran bcaVA = new MetodePembayaran("PAY03", "BCA Virtual Account", "Bank Transfer");

        listMetode.add(gopay);
        listMetode.add(mandiriCC);
        listMetode.add(bcaVA);

        listLangganan.add(new LanggananStreaming(
                "SUB01", netflix, 186000.0, mandiriCC, 15, "4K UHD + HDR", 4));
        listLangganan.add(new LanggananStreaming(
                "SUB02", spotify, 54990.0, gopay, 20, "Very High Quality (320 kbps)", 1));
        listLangganan.add(new LanggananProduktivitas(
                "SUB03", googleOne, 43000.0, gopay, 5, "2 TB Google Drive & Photos", 5));
        listLangganan.add(new LanggananProduktivitas(
                "SUB04", ms365, 95990.0, bcaVA, 28, "1 TB OneDrive + Office Apps", 1));
    }

    public void jalankanAplikasi() {
        boolean berjalan = true;
        while (berjalan) {
            view.tampilkanMenuUtama();
            int pilihan = InputValidator.bacaInt(scanner, "Pilih Menu (1-7): ", 1, 7);

            switch (pilihan) {
                case 1:
                    menuTampilkanLangganan();
                    break;
                case 2:
                    menuTambahLangganan();
                    break;
                case 3:
                    menuEditLangganan();
                    break;
                case 4:
                    menuHapusLangganan();
                    break;
                case 5:
                    menuRingkasanPengeluaran();
                    break;
                case 6:
                    menuSimulasiEstimasi();
                    break;
                case 7:
                    berjalan = false;
                    view.tampilkanPesan("\nTerima kasih telah menggunakan Personal Subscription Tracker. Sampai jumpa!");
                    break;
                default:
                    view.tampilkanPesanError("Pilihan tidak valid!");
            }
        }
        scanner.close();
    }

    private void menuTampilkanLangganan() {
        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        boolean diSubMenu = true;
        while (diSubMenu) {
            view.tampilkanHeader("SUB-MENU TAMPILKAN LANGGANAN (READ)");
            System.out.println("1. Tampilkan Semua Langganan (Format Tabel Ringkas Rapi)");
            System.out.println("2. Tampilkan Semua Langganan (Format Kartu Detail Lengkap)");
            System.out.println("3. Cari Langganan (Pencarian Nama / ID / Kategori)");
            System.out.println("4. Filter Langganan (Berdasarkan Kategori / Status)");
            System.out.println("0. Kembali ke Menu Utama");
            view.tampilkanGaris();

            int pilihan = InputValidator.bacaInt(scanner, "Pilih Aksi Tampilan (0-4): ", 0, 4);
            switch (pilihan) {
                case 0:
                    diSubMenu = false;
                    break;
                case 1:
                    view.tampilkanHeader("DAFTAR SELURUH LANGGANAN (FORMAT TABEL)");
                    view.tampilkanTabelLangganan(listLangganan, true);
                    tanyaLihatDetail();
                    break;
                case 2:
                    view.tampilkanDaftarLangganan(listLangganan);
                    InputValidator.tekanEnter(scanner);
                    break;
                case 3:
                    menuCariLangganan();
                    break;
                case 4:
                    menuFilterLangganan();
                    break;
            }
        }
    }

    private void tanyaLihatDetail() {
        boolean inginDetail = InputValidator.bacaKonfirmasi(scanner, "Ingin melihat detail kartu lengkap dari salah satu langganan?");
        if (!inginDetail) return;

        Langganan sub = pilihLangganan("PILIH LANGGANAN UNTUK MELIHAT DETAIL KARTU");
        if (sub != null) {
            view.tampilkanKartuLangganan(sub, 0);
            InputValidator.tekanEnter(scanner);
        }
    }

    private void menuCariLangganan() {
        view.tampilkanHeader("PENCARIAN DATA LANGGANAN");
        String keyword = InputValidator.bacaString(scanner, "Masukkan kata kunci pencarian (Nama / ID / Kategori / Metode): ").toLowerCase();

        ArrayList<Langganan> hasil = new ArrayList<>();
        for (Langganan sub : listLangganan) {
            boolean matchId = sub.getIdSubscription() != null && sub.getIdSubscription().toLowerCase().contains(keyword);
            boolean matchNama = sub.getLayanan() != null && sub.getLayanan().getNamaLayanan().toLowerCase().contains(keyword);
            boolean matchKat = sub.getLayanan() != null && sub.getLayanan().getKategori().toLowerCase().contains(keyword);
            boolean matchTipe = sub.getTipeLangganan() != null && sub.getTipeLangganan().toLowerCase().contains(keyword);
            boolean matchStatus = sub.getStatus() != null && sub.getStatus().toLowerCase().contains(keyword);
            boolean matchMetode = sub.getMetode() != null && sub.getMetode().getNamaMetode().toLowerCase().contains(keyword);

            if (matchId || matchNama || matchKat || matchTipe || matchStatus || matchMetode) {
                hasil.add(sub);
            }
        }

        if (hasil.isEmpty()) {
            view.tampilkanPesanInfo("Tidak ditemukan langganan dengan kata kunci '" + keyword + "'.");
        } else {
            view.tampilkanHeader("HASIL PENCARIAN (" + hasil.size() + " DITEMUKAN)");
            view.tampilkanTabelLangganan(hasil, true);

            boolean lihatKartu = InputValidator.bacaKonfirmasi(scanner, "Ingin melihat detail kartu lengkap dari hasil pencarian?");
            if (lihatKartu) {
                Langganan sub = pilihDariDaftarKustom(hasil, "PILIH LANGGANAN UNTUK DETAIL");
                if (sub != null) {
                    view.tampilkanKartuLangganan(sub, 0);
                }
            }
        }
        InputValidator.tekanEnter(scanner);
    }

    private void menuFilterLangganan() {
        view.tampilkanHeader("FILTER DATA LANGGANAN");
        System.out.println("1. Hanya Kategori Streaming (Film / Musik)");
        System.out.println("2. Hanya Kategori Produktivitas & Cloud SaaS");
        System.out.println("3. Hanya Status Aktif");
        System.out.println("4. Hanya Status Nonaktif");
        System.out.println("0. Batal");
        view.tampilkanGaris();

        int pil = InputValidator.bacaInt(scanner, "Pilih Kriteria Filter (0-4): ", 0, 4);
        if (pil == 0) return;

        ArrayList<Langganan> hasil = new ArrayList<>();
        String labelFilter = "";
        switch (pil) {
            case 1:
                labelFilter = "KATEGORI: STREAMING";
                for (Langganan s : listLangganan) {
                    if (s instanceof LanggananStreaming) hasil.add(s);
                }
                break;
            case 2:
                labelFilter = "KATEGORI: PRODUKTIVITAS & CLOUD SAAS";
                for (Langganan s : listLangganan) {
                    if (s instanceof LanggananProduktivitas) hasil.add(s);
                }
                break;
            case 3:
                labelFilter = "STATUS: AKTIF";
                for (Langganan s : listLangganan) {
                    if (s.getStatus().equalsIgnoreCase("Aktif")) hasil.add(s);
                }
                break;
            case 4:
                labelFilter = "STATUS: NONAKTIF";
                for (Langganan s : listLangganan) {
                    if (s.getStatus().equalsIgnoreCase("Nonaktif")) hasil.add(s);
                }
                break;
        }

        if (hasil.isEmpty()) {
            view.tampilkanPesanInfo("Tidak ada data langganan yang cocok dengan kriteria filter.");
        } else {
            view.tampilkanHeader("HASIL " + labelFilter + " (" + hasil.size() + " DATA)");
            view.tampilkanTabelLangganan(hasil, true);
        }
        InputValidator.tekanEnter(scanner);
    }

    private void menuTambahLangganan() {
        view.tampilkanHeader("TAMBAH LANGGANAN BARU (CREATE)");
        String idBaru = generateIdOtomatis();
        System.out.println("[INFO] ID Otomatis Sistem: " + idBaru);

        view.tampilkanHeader("[Langkah 1/5] PILIH TIPE LANGGANAN");
        System.out.println("1. Langganan Streaming (Film / Musik / Hiburan)");
        System.out.println("2. Langganan Produktivitas & Cloud SaaS");
        System.out.println("0. Batal & Kembali ke Menu Utama");
        int tipe = InputValidator.bacaInt(scanner, "Pilih Tipe (0-2): ", 0, 2);
        if (tipe == 0) {
            view.tampilkanPesanInfo("Penambahan langganan dibatalkan.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("[Langkah 2/5] PILIH LAYANAN");
        Layanan layananTerpilih = tentukanLayanan();
        if (layananTerpilih == null) {
            view.tampilkanPesanInfo("Penambahan langganan dibatalkan.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("[Langkah 3/5] PILIH METODE PEMBAYARAN");
        MetodePembayaran metodeTerpilih = tentukanMetodePembayaran();
        if (metodeTerpilih == null) {
            view.tampilkanPesanInfo("Penambahan langganan dibatalkan.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("[Langkah 4/5] HARGA BULANAN & TANGGAL TAGIHAN");
        double harga = InputValidator.bacaDouble(scanner, "Masukkan Harga Bulanan (Rp): ", 0.0);
        int tglTagihan = InputValidator.bacaInt(scanner, "Masukkan Tanggal Tagihan Bulanan (1-31): ", 1, 31);

        view.tampilkanHeader("[Langkah 5/5] ATRIBUT SPESIFIK KATEGORI");
        Langganan langgananBaru;
        if (tipe == 1) {
            System.out.println("Kategori: Streaming Video / Audio");
            String resolusi = InputValidator.bacaString(scanner, "Masukkan Kualitas Streaming/Audio (contoh: 4K UHD / Hi-Fi / Full HD): ");
            int batasLayar = InputValidator.bacaInt(scanner, "Masukkan Batas Layar/Perangkat Simultan: ", 1, 100);
            langgananBaru = new LanggananStreaming(idBaru, layananTerpilih, harga, metodeTerpilih, tglTagihan, resolusi, batasLayar);
        } else {
            System.out.println("Kategori: Produktivitas & Cloud SaaS");
            String storage = InputValidator.bacaString(scanner, "Masukkan Kapasitas Storage / Fitur SaaS (contoh: 1 TB / Pro Tier / VPS): ");
            int lisensi = InputValidator.bacaInt(scanner, "Masukkan Jumlah Lisensi / Akses User: ", 1, 1000);
            langgananBaru = new LanggananProduktivitas(idBaru, layananTerpilih, harga, metodeTerpilih, tglTagihan, storage, lisensi);
        }

        view.tampilkanHeader("PRATINJAU DATA SEBELUM DISIMPAN");
        view.tampilkanKartuLangganan(langgananBaru, 0);

        boolean simpan = InputValidator.bacaKonfirmasi(scanner, "Apakah data di atas sudah benar dan ingin disimpan ke database?");
        if (!simpan) {
            view.tampilkanPesanInfo("Data tidak disimpan. Penambahan langganan dibatalkan.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        try {
            langgananDAO.save(langgananBaru);
            listLangganan.add(langgananBaru);
            view.tampilkanPesanSukses("Data langganan baru [" + idBaru + "] '" + layananTerpilih.getNamaLayanan() + "' berhasil disimpan ke database!");
        } catch (SQLException e) {
            view.tampilkanPesanError("Data gagal disimpan ke database: " + e.getMessage());
        }

        InputValidator.tekanEnter(scanner);
    }

    private Layanan tentukanLayanan() {
        System.out.println("Daftar Layanan yang Tersedia:");
        for (int i = 0; i < listLayanan.size(); i++) {
            Layanan l = listLayanan.get(i);
            System.out.printf(" [%2d] %-20s (%s)%n", (i + 1), l.getNamaLayanan(), l.getKategori());
        }
        System.out.printf(" [%2d] + Daftarkan Layanan Baru%n", (listLayanan.size() + 1));
        System.out.println(" [ 0] Batal");
        view.tampilkanGaris();

        int pilihan = InputValidator.bacaInt(scanner, "Pilih Layanan (0-" + (listLayanan.size() + 1) + "): ", 0, listLayanan.size() + 1);
        if (pilihan == 0) {
            return null;
        }

        if (pilihan <= listLayanan.size()) {
            return listLayanan.get(pilihan - 1);
        }

        String idLayananOtomatis = generateIdLayananOtomatis();
        view.tampilkanHeader("DAFTARKAN LAYANAN BARU");
        System.out.println("[INFO] ID Layanan Baru (Otomatis): " + idLayananOtomatis);

        String namaLayanan = InputValidator.bacaString(scanner, "Masukkan Nama Layanan Baru: ");
        String kategori = InputValidator.bacaString(scanner, "Masukkan Kategori Layanan (contoh: Hiburan Film / AI & Coding / dsb): ");

        Layanan layananBaru = new Layanan(idLayananOtomatis, namaLayanan, kategori);
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO layanan (id_layanan, nama_layanan, kategori) VALUES (?, ?, ?)")) {
            ps.setString(1, idLayananOtomatis);
            ps.setString(2, namaLayanan);
            ps.setString(3, kategori);
            ps.executeUpdate();
            view.tampilkanPesanSukses("Layanan baru '" + namaLayanan + "' (" + idLayananOtomatis + ") berhasil disimpan ke database!");
        } catch (SQLException e) {
            view.tampilkanPesanError("Gagal menyimpan layanan ke database: " + e.getMessage());
        }

        listLayanan.add(layananBaru);
        return layananBaru;
    }

    private MetodePembayaran tentukanMetodePembayaran() {
        System.out.println("Daftar Metode Pembayaran yang Tersedia:");
        for (int i = 0; i < listMetode.size(); i++) {
            MetodePembayaran m = listMetode.get(i);
            System.out.printf(" [%2d] %-22s [%s]%n", (i + 1), m.getNamaMetode(), m.getJenis());
        }
        System.out.printf(" [%2d] + Daftarkan Metode Pembayaran Baru%n", (listMetode.size() + 1));
        System.out.println(" [ 0] Batal");
        view.tampilkanGaris();

        int pilihan = InputValidator.bacaInt(scanner, "Pilih Metode (0-" + (listMetode.size() + 1) + "): ", 0, listMetode.size() + 1);
        if (pilihan == 0) {
            return null;
        }

        if (pilihan <= listMetode.size()) {
            return listMetode.get(pilihan - 1);
        }

        String idMetodeOtomatis = generateIdMetodeOtomatis();
        view.tampilkanHeader("DAFTARKAN METODE PEMBAYARAN BARU");
        System.out.println("[INFO] ID Metode Baru (Otomatis): " + idMetodeOtomatis);

        String namaMetode = InputValidator.bacaString(scanner, "Masukkan Nama Metode Pembayaran: ");
        String jenis = InputValidator.bacaString(scanner, "Masukkan Jenis Metode (contoh: E-Wallet / Credit Card / Bank Transfer): ");

        MetodePembayaran metodeBaru = new MetodePembayaran(idMetodeOtomatis, namaMetode, jenis);
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO metode_pembayaran (id_metode, nama_metode, jenis) VALUES (?, ?, ?)")) {
            ps.setString(1, idMetodeOtomatis);
            ps.setString(2, namaMetode);
            ps.setString(3, jenis);
            ps.executeUpdate();
            view.tampilkanPesanSukses("Metode pembayaran '" + namaMetode + "' (" + idMetodeOtomatis + ") berhasil disimpan ke database!");
        } catch (SQLException e) {
            view.tampilkanPesanError("Gagal menyimpan metode pembayaran ke database: " + e.getMessage());
        }

        listMetode.add(metodeBaru);
        return metodeBaru;
    }

    private void menuEditLangganan() {
        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        Langganan langganan = pilihLangganan("PILIH LANGGANAN YANG INGIN DIEDIT (UPDATE)");
        if (langganan == null) {
            InputValidator.tekanEnter(scanner);
            return;
        }

        boolean tetapEdit = true;
        while (tetapEdit) {
            view.tampilkanHeader("EDITOR LANGGANAN: [" + langganan.getIdSubscription() + "] " + langganan.getLayanan().getNamaLayanan());
            view.tampilkanKartuLangganan(langganan, 0);

            System.out.println("Pilihan Bagian yang Ingin Diedit:");
            System.out.printf(" [1] Ubah Harga Bulanan            (Saat ini: %s)%n", view.formatRupiah(langganan.getHargaBulanan()));
            System.out.printf(" [2] Ubah Tanggal Tagihan Bulanan  (Saat ini: Tanggal %d)%n", langganan.getTanggalTagihan());
            System.out.printf(" [3] Ubah Status Langganan         (Saat ini: %s)%n", langganan.getStatus());
            System.out.printf(" [4] Ubah Metode Pembayaran        (Saat ini: %s)%n", langganan.getMetode().getNamaMetode());
            if (langganan instanceof LanggananStreaming) {
                LanggananStreaming s = (LanggananStreaming) langganan;
                System.out.printf(" [5] Ubah Kualitas Resolusi/Audio  (Saat ini: %s)%n", s.getKualitasResolusi());
                System.out.printf(" [6] Ubah Batas Layar/Perangkat    (Saat ini: %d Perangkat)%n", s.getBatasLayar());
            } else if (langganan instanceof LanggananProduktivitas) {
                LanggananProduktivitas p = (LanggananProduktivitas) langganan;
                System.out.printf(" [5] Ubah Kapasitas Storage/Fitur  (Saat ini: %s)%n", p.getKapasitasStorage());
                System.out.printf(" [6] Ubah Lisensi User/Akses       (Saat ini: %d Akun)%n", p.getLisensiUser());
            }
            System.out.println(" [0] Selesai & Kembali ke Menu Utama");
            view.tampilkanGaris();

            int pilihan = InputValidator.bacaInt(scanner, "Pilih Aksi Edit (0-6): ", 0, 6);
            if (pilihan == 0) {
                tetapEdit = false;
                view.tampilkanPesanSukses("Selesai mengedit langganan [" + langganan.getIdSubscription() + "].");
                break;
            }

            boolean berhasilUbah = false;
            switch (pilihan) {
                case 1:
                    double hargaLama = langganan.getHargaBulanan();
                    System.out.println("Harga saat ini: " + view.formatRupiah(hargaLama));
                    double hargaBaru = InputValidator.bacaDouble(scanner, "Masukkan Harga Bulanan Baru (Rp): ", 0.0);
                    if (hargaBaru != hargaLama) {
                        langganan.setHargaBulanan(hargaBaru);
                        berhasilUbah = true;
                    } else {
                        view.tampilkanPesanInfo("Harga tidak berubah.");
                    }
                    break;

                case 2:
                    int tglLama = langganan.getTanggalTagihan();
                    System.out.println("Tanggal tagihan saat ini: Tanggal " + tglLama);
                    int tglBaru = InputValidator.bacaInt(scanner, "Masukkan Tanggal Tagihan Baru (1-31): ", 1, 31);
                    if (tglBaru != tglLama) {
                        langganan.setTanggalTagihan(tglBaru);
                        berhasilUbah = true;
                    } else {
                        view.tampilkanPesanInfo("Tanggal tagihan tidak berubah.");
                    }
                    break;

                case 3:
                    System.out.println("Status saat ini: " + langganan.getStatus());
                    System.out.println("1. Aktif");
                    System.out.println("2. Nonaktif");
                    int st = InputValidator.bacaInt(scanner, "Pilih Status Baru (1-2): ", 1, 2);
                    String statusBaru = (st == 1) ? "Aktif" : "Nonaktif";
                    if (!statusBaru.equalsIgnoreCase(langganan.getStatus())) {
                        langganan.setStatus(statusBaru);
                        berhasilUbah = true;
                    } else {
                        view.tampilkanPesanInfo("Status tidak berubah.");
                    }
                    break;

                case 4:
                    view.tampilkanHeader("PILIH METODE PEMBAYARAN BARU");
                    MetodePembayaran metodeBaru = tentukanMetodePembayaran();
                    if (metodeBaru != null) {
                        langganan.setMetode(metodeBaru);
                        berhasilUbah = true;
                    }
                    break;

                case 5:
                    if (langganan instanceof LanggananStreaming) {
                        LanggananStreaming s = (LanggananStreaming) langganan;
                        System.out.println("Kualitas saat ini: " + s.getKualitasResolusi());
                        String res = InputValidator.bacaString(scanner, "Masukkan Kualitas Streaming/Audio Baru: ");
                        s.setKualitasResolusi(res);
                        berhasilUbah = true;
                    } else if (langganan instanceof LanggananProduktivitas) {
                        LanggananProduktivitas p = (LanggananProduktivitas) langganan;
                        System.out.println("Fitur/Storage saat ini: " + p.getKapasitasStorage());
                        String storage = InputValidator.bacaString(scanner, "Masukkan Kapasitas Storage / Fitur SaaS Baru: ");
                        p.setKapasitasStorage(storage);
                        berhasilUbah = true;
                    }
                    break;

                case 6:
                    if (langganan instanceof LanggananStreaming) {
                        LanggananStreaming s = (LanggananStreaming) langganan;
                        System.out.println("Batas layar saat ini: " + s.getBatasLayar() + " Perangkat");
                        int layar = InputValidator.bacaInt(scanner, "Masukkan Batas Layar/Perangkat Baru: ", 1, 100);
                        s.setBatasLayar(layar);
                        berhasilUbah = true;
                    } else if (langganan instanceof LanggananProduktivitas) {
                        LanggananProduktivitas p = (LanggananProduktivitas) langganan;
                        System.out.println("Lisensi saat ini: " + p.getLisensiUser() + " Akun");
                        int lisensi = InputValidator.bacaInt(scanner, "Masukkan Jumlah Lisensi User Baru: ", 1, 1000);
                        p.setLisensiUser(lisensi);
                        berhasilUbah = true;
                    }
                    break;
            }

            if (berhasilUbah) {
                try {
                    langgananDAO.update(langganan);
                    view.tampilkanPesanSukses("Perubahan berhasil diperbarui di database!");
                } catch (SQLException e) {
                    view.tampilkanPesanError("Gagal memperbarui ke database: " + e.getMessage());
                }

                boolean lanjut = InputValidator.bacaKonfirmasi(scanner, "Ingin mengedit bagian lain dari langganan ini?");
                if (!lanjut) {
                    tetapEdit = false;
                }
            }
        }

        InputValidator.tekanEnter(scanner);
    }

    private void menuHapusLangganan() {
        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        Langganan langganan = pilihLangganan("PILIH LANGGANAN YANG INGIN DIHAPUS / DIBATALKAN (DELETE)");
        if (langganan == null) {
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("KONFIRMASI PENGHAPUSAN LANGGANAN");
        view.tampilkanKartuLangganan(langganan, 0);

        view.tampilkanPesanPeringatan("Data langganan ini akan dihapus secara PERMANEN dari database!");
        boolean konfirmasi = InputValidator.bacaKonfirmasi(scanner, "Apakah Anda yakin ingin menghapus langganan ini?");
        if (konfirmasi) {
            try {
                langgananDAO.delete(langganan.getIdSubscription());
                listLangganan.remove(langganan);
                view.tampilkanPesanSukses("Langganan '" + langganan.getLayanan().getNamaLayanan() + "' [" + langganan.getIdSubscription() + "] berhasil dihapus dari sistem!");
                System.out.println("Total langganan tersisa: " + listLangganan.size() + " layanan.");
            } catch (SQLException e) {
                view.tampilkanPesanError("Data gagal dihapus dari database: " + e.getMessage());
            }
        } else {
            view.tampilkanPesanInfo("Penghapusan langganan dibatalkan. Data tetap aman.");
        }

        InputValidator.tekanEnter(scanner);
    }

    private void menuRingkasanPengeluaran() {
        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("RINGKASAN & TOTAL PENGELUARAN BULANAN");

        double totalSemua = 0;
        double totalStreaming = 0;
        double totalProduktivitas = 0;
        int jumlahAktif = 0;
        int jumlahStreaming = 0;
        int jumlahProduktivitas = 0;

        for (Langganan sub : listLangganan) {
            if (sub.getStatus().equalsIgnoreCase("Aktif")) {
                totalSemua += sub.getHargaBulanan();
                jumlahAktif++;

                if (sub instanceof LanggananStreaming) {
                    totalStreaming += sub.getHargaBulanan();
                    jumlahStreaming++;
                } else if (sub instanceof LanggananProduktivitas) {
                    totalProduktivitas += sub.getHargaBulanan();
                    jumlahProduktivitas++;
                }
            }
        }

        System.out.println("Jumlah Langganan Aktif : " + jumlahAktif + " layanan");
        System.out.println("- Kategori Streaming   : " + jumlahStreaming + " layanan (" + view.formatRupiah(totalStreaming) + ")");
        System.out.println("- Kategori Produktif   : " + jumlahProduktivitas + " layanan (" + view.formatRupiah(totalProduktivitas) + ")");
        view.tampilkanGaris();
        System.out.println("TOTAL PENGELUARAN/BULAN: " + view.formatRupiah(totalSemua));
        view.tampilkanGaris();

        if (totalSemua > 500000) {
            view.tampilkanPesanPeringatan("Total pengeluaran langganan Anda melebihi Rp 500.000! Segera Evaluasi layanan yang jarang dipakai.");
        } else {
            view.tampilkanPesanInfo("STATUS AMAN: Pengeluaran langganan Anda masih dalam batas wajar/hemat.");
        }

        InputValidator.tekanEnter(scanner);
    }

    private void menuSimulasiEstimasi() {
        if (listLangganan.isEmpty()) {
            view.tampilkanPesanInfo("Belum ada data langganan yang tercatat.");
            InputValidator.tekanEnter(scanner);
            return;
        }

        view.tampilkanHeader("SIMULASI ESTIMASI BIAYA (POLYMORPHISM)");
        int durasiBulan = InputValidator.bacaInt(scanner, "Masukkan proyeksi durasi langganan (jumlah bulan): ", 1, 60);

        boolean pakaiDiskon = InputValidator.bacaKonfirmasi(scanner, "Apakah ingin menerapkan voucher diskon tambahan (Overloading)?");
        double diskon = 0.0;
        if (pakaiDiskon) {
            diskon = InputValidator.bacaDouble(scanner, "Masukkan persentase diskon tambahan (0-100%): ", 0.0);
            if (diskon > 100.0) {
                diskon = 100.0;
            }
        }

        view.tampilkanHeader("HASIL PROYEKSI BIAYA UNTUK " + durasiBulan + " BULAN");
        System.out.printf("%-8s | %-16s | %-26s | %-18s | %s%n",
                "ID", "Layanan", "Tipe Langganan", "Tarif/Bulan", "Estimasi Total");
        view.tampilkanGaris();

        double grandTotal = 0;
        for (Langganan sub : listLangganan) {
            if (!sub.getStatus().equalsIgnoreCase("Aktif")) {
                continue;
            }
            double estimasi;
            if (pakaiDiskon) {
                estimasi = sub.hitungEstimasiBiaya(durasiBulan, diskon);
            } else {
                estimasi = sub.hitungEstimasiBiaya(durasiBulan);
            }
            grandTotal += estimasi;

            System.out.printf("%-8s | %-16s | %-26s | %-18s | %s%n",
                    sub.getIdSubscription(),
                    sub.getLayanan().getNamaLayanan(),
                    sub.getTipeLangganan(),
                    view.formatRupiah(sub.getHargaBulanan()),
                    view.formatRupiah(estimasi));
        }

        view.tampilkanGaris();
        System.out.println("TOTAL ESTIMASI BIAYA AKUMULATIF: " + view.formatRupiah(grandTotal));

        view.tampilkanHeader("PROYEKSI TAHUNAN (ABSTRACT METHOD + INTERFACE)");
        double totalTahunan = 0, totalDiskonTahunan = 0;
        for (Langganan sub : listLangganan) {
            if (!sub.getStatus().equalsIgnoreCase("Aktif")) continue;
            double tahunan = sub.hitungBiayaTahunan();
            double diskonTahunan = 0;
            if (sub instanceof DapatDidiskon) {
                diskonTahunan = ((DapatDidiskon) sub).hitungDiskon(12);
            }
            totalTahunan += tahunan;
            totalDiskonTahunan += diskonTahunan;
            System.out.printf("%-8s | %-16s | Tahunan: %-18s | Potongan Diskon: %s%n",
                    sub.getIdSubscription(), sub.getLayanan().getNamaLayanan(),
                    view.formatRupiah(tahunan), view.formatRupiah(diskonTahunan));
        }
        view.tampilkanGaris();
        System.out.println("TOTAL TAHUNAN SETELAH DISKON : " + view.formatRupiah(totalTahunan));
        System.out.println("TOTAL HEMAT POTONGAN DISKON  : " + view.formatRupiah(totalDiskonTahunan));
        InputValidator.tekanEnter(scanner);
    }

    private Langganan pilihLangganan(String judulAksi) {
        return pilihDariDaftarKustom(listLangganan, judulAksi);
    }

    private Langganan pilihDariDaftarKustom(ArrayList<Langganan> daftar, String judulAksi) {
        if (daftar.isEmpty()) {
            view.tampilkanPesanInfo("Tidak ada data langganan.");
            return null;
        }

        view.tampilkanHeader(judulAksi);
        view.tampilkanTabelLangganan(daftar, true);
        System.out.println("Petunjuk: Masukkan NOMOR URUT (1-" + daftar.size() + ") atau ID LANGGANAN (contoh: SUB01).");
        System.out.println("          Ketik '0' atau 'batal' untuk membatalkan.");
        view.tampilkanGaris();

        while (true) {
            String input = InputValidator.bacaString(scanner, "Pilih Langganan [Nomor / ID / 0]: ").trim();
            if (input.equals("0") || input.equalsIgnoreCase("batal") || input.equalsIgnoreCase("kembali")) {
                view.tampilkanPesanInfo("Aksi dibatalkan.");
                return null;
            }

            try {
                int no = Integer.parseInt(input);
                if (no >= 1 && no <= daftar.size()) {
                    return daftar.get(no - 1);
                }
                view.tampilkanPesanError("Nomor urut tidak valid! Masukkan angka antara 1 sampai " + daftar.size() + " atau 0 untuk batal.");
                continue;
            } catch (NumberFormatException ignored) {
            }

            for (Langganan sub : daftar) {
                if (sub.getIdSubscription().equalsIgnoreCase(input)) {
                    return sub;
                }
            }

            view.tampilkanPesanError("Langganan dengan nomor atau ID '" + input + "' tidak ditemukan! Coba lagi atau ketik '0' untuk batal.");
        }
    }

    private String generateIdOtomatis() {
        int max = 0;
        for (Langganan sub : listLangganan) {
            String id = sub.getIdSubscription();
            if (id != null && id.toUpperCase().startsWith("SUB")) {
                try {
                    int num = Integer.parseInt(id.substring(3).trim());
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("SUB%02d", max + 1);
    }

    private String generateIdLayananOtomatis() {
        int max = 0;
        for (Layanan l : listLayanan) {
            String id = l.getIdLayanan();
            if (id != null && id.toUpperCase().startsWith("LYN")) {
                try {
                    int num = Integer.parseInt(id.substring(3).trim());
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("LYN%02d", max + 1);
    }

    private String generateIdMetodeOtomatis() {
        int max = 0;
        for (MetodePembayaran m : listMetode) {
            String id = m.getIdMetode();
            if (id != null && id.toUpperCase().startsWith("PAY")) {
                try {
                    int num = Integer.parseInt(id.substring(3).trim());
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("PAY%02d", max + 1);
    }

    private Langganan cariLanggananById(String id) {
        if (id == null) {
            return null;
        }
        for (Langganan sub : listLangganan) {
            if (sub.getIdSubscription().equalsIgnoreCase(id.trim())) {
                return sub;
            }
        }
        return null;
    }
}
