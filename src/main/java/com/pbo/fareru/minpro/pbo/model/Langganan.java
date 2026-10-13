package com.pbo.fareru.minpro.pbo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

/**

 * @author Muhammad Fahriel (2509116050)
 */
public abstract class Langganan {
    protected static final int BATAS_BULAN_DISKON = 12;
    @Id
    private String idSubscription;
    private Layanan layanan;
    private double hargaBulanan;
    private MetodePembayaran metode;
    private int tanggalTagihan;
    private String status;

    public Langganan(String idSubscription, Layanan layanan, double hargaBulanan, MetodePembayaran metode, int tanggalTagihan) {
        setIdSubscription(idSubscription);
        setLayanan(layanan);
        setHargaBulanan(hargaBulanan);
        setMetode(metode);
        setTanggalTagihan(tanggalTagihan);
        setStatus("Aktif");
    }

    public String getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(String idSubscription) {
        if (idSubscription != null && !idSubscription.trim().isEmpty()) {
            this.idSubscription = idSubscription;
        }
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public void setLayanan(Layanan layanan) {
        if (layanan != null) {
            this.layanan = layanan;
        }
    }

    public double getHargaBulanan() {
        return hargaBulanan;
    }

    public void setHargaBulanan(double hargaBulanan) {
        if (hargaBulanan >= 0) {
            this.hargaBulanan = hargaBulanan;
        } else {
            this.hargaBulanan = 0;
        }
    }

    public MetodePembayaran getMetode() {
        return metode;
    }

    public void setMetode(MetodePembayaran metode) {
        if (metode != null) {
            this.metode = metode;
        }
    }

    public int getTanggalTagihan() {
        return tanggalTagihan;
    }

    public void setTanggalTagihan(int tanggalTagihan) {
        if (tanggalTagihan >= 1 && tanggalTagihan <= 31) {
            this.tanggalTagihan = tanggalTagihan;
        } else {
            this.tanggalTagihan = 1;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && !status.trim().isEmpty()) {
            this.status = status;
        }
    }

    public abstract String getTipeLangganan(); 
    public abstract double hitungBiayaTahunan();
    
    public void tampilkanDetail() {
        System.out.println("ID Langganan : " + idSubscription);
        System.out.println("Tipe         : " + getTipeLangganan());
        System.out.println("Layanan      : " + (layanan != null ? layanan.getNamaLayanan() + " (" + layanan.getKategori() + ")" : "-"));
        System.out.println("Harga        : Rp " + String.format("%,.2f", hargaBulanan));
        System.out.println("Bayar Lewat  : " + (metode != null ? metode.getNamaMetode() + " [" + metode.getJenis() + "]" : "-"));
        System.out.println("Tgl Tagihan  : Tanggal " + tanggalTagihan);
        System.out.println("Status       : " + status);
    }

    public void tampilkanDetail(boolean ringkas) {
        if (ringkas) {
            String nama = layanan != null ? layanan.getNamaLayanan() : "-";
            String tipe = getTipeLangganan();
            System.out.printf("[%s] %-15s | %-20s | Rp %,10.2f | Tgl %02d | %s%n",
                    idSubscription, nama, tipe, hargaBulanan, tanggalTagihan, status);
        } else {
            tampilkanDetail();
        }
    }

    public double hitungEstimasiBiaya(int bulan) {
        if (bulan <= 0) {
            return 0;
        }
        return hargaBulanan * bulan;
    }

    public double hitungEstimasiBiaya(int bulan, double diskonPersen) {
        if (bulan <= 0) {
            return 0;
        }
        double total = hitungEstimasiBiaya(bulan);
        if (diskonPersen > 0 && diskonPersen <= 100) {
            total = total * (1.0 - (diskonPersen / 100.0));
        }
        return total;
    }
}
