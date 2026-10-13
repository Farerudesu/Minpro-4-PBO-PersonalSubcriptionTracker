package com.pbo.fareru.minpro.pbo.model;

/**
@author Muhammad Fahriel (2509116050)
 */
public class LanggananStreaming extends Langganan implements DapatDidiskon {
    @Override
    public double hitungDiskon(int bulan) {
    if (bulan >= BATAS_BULAN_DISKON) {
        return getHargaBulanan() * bulan * 0.05; 
    }
    return 0;
}
    private String kualitasResolusi;
    private int batasLayar;

    public LanggananStreaming(String idSubscription, Layanan layanan, double hargaBulanan,
                              MetodePembayaran metode, int tanggalTagihan,
                              String kualitasResolusi, int batasLayar) {
        super(idSubscription, layanan, hargaBulanan, metode, tanggalTagihan);
        setKualitasResolusi(kualitasResolusi);
        setBatasLayar(batasLayar);
    }

    public String getKualitasResolusi() {
        return kualitasResolusi;
    }

    public void setKualitasResolusi(String kualitasResolusi) {
        if (kualitasResolusi != null && !kualitasResolusi.trim().isEmpty()) {
            this.kualitasResolusi = kualitasResolusi;
        }
    }

    public int getBatasLayar() {
        return batasLayar;
    }

    public void setBatasLayar(int batasLayar) {
        if (batasLayar >= 1) {
            this.batasLayar = batasLayar;
        } else {
            this.batasLayar = 1;
        }
    }

    @Override
    public String getTipeLangganan() {
        return "Streaming (Video/Audio)";
    }

    @Override
    public void tampilkanDetail() {
        super.tampilkanDetail();
        boolean isAudio = getLayanan() != null && getLayanan().getKategori() != null
                && (getLayanan().getKategori().toLowerCase().contains("musik")
                    || getLayanan().getKategori().toLowerCase().contains("audio"));
        if (isAudio) {
            System.out.println("Kualitas Audio: " + kualitasResolusi);
            System.out.println("Batas Device  : " + batasLayar + " Perangkat");
        } else {
            System.out.println("Resolusi Video: " + kualitasResolusi);
            System.out.println("Batas Layar   : " + batasLayar + " Perangkat / Layar");
        }
    }

    @Override
    public double hitungEstimasiBiaya(int bulan) {
        if (bulan <= 0) {
            return 0;
        }
        double total = getHargaBulanan() * bulan;
        if (bulan >= 12) {
            return total * 0.95;
        }
        return total;
    }
    @Override
    public double hitungBiayaTahunan(){
        return getHargaBulanan() * BATAS_BULAN_DISKON * 0.95;
    }
}
