CREATE DATABASE IF NOT EXISTS personal_subcrition_tracker;
USE personal_subcrition_tracker;

CREATE TABLE IF NOT EXISTS layanan (
    id_layanan VARCHAR(20) PRIMARY KEY,
    nama_layanan VARCHAR(100) NOT NULL,
    kategori VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS metode_pembayaran (
    id_metode VARCHAR(20) PRIMARY KEY,
    nama_metode VARCHAR(100) NOT NULL,
    jenis VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS langganan (
    id_subscription VARCHAR(20) PRIMARY KEY,
    id_layanan VARCHAR(20) NOT NULL,
    harga_bulanan DECIMAL(12,2) NOT NULL,
    id_metode VARCHAR(20) NOT NULL,
    tanggal_tagihan TINYINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    tipe VARCHAR(30) NOT NULL,
    detail VARCHAR(150) NOT NULL,
    jumlah INT NOT NULL,
    FOREIGN KEY (id_layanan) REFERENCES layanan(id_layanan),
    FOREIGN KEY (id_metode) REFERENCES metode_pembayaran(id_metode)
);

INSERT INTO layanan (id_layanan, nama_layanan, kategori) VALUES
    ('LYN01', 'Netflix', 'Hiburan Film & Serial'),
    ('LYN02', 'Spotify', 'Streaming Musik & Audio'),
    ('LYN03', 'Disney+', 'Hiburan Film & Serial'),
    ('LYN04', 'Claude Code', 'AI & Coding'),
    ('LYN05', 'GitHub Copilot', 'AI & Coding'),
    ('LYN06', 'Amazon AWS', 'Cloud Computing'),
    ('LYN07', 'VPS Hosting', 'Server & Hosting'),
    ('LYN08', 'Docker', 'Containerization')
ON DUPLICATE KEY UPDATE nama_layanan = VALUES(nama_layanan), kategori = VALUES(kategori);

INSERT INTO metode_pembayaran (id_metode, nama_metode, jenis) VALUES
    ('PAY01', 'GoPay', 'E-Wallet'),
    ('PAY02', 'Kartu Kredit Mandiri', 'Credit Card'),
    ('PAY03', 'OVO', 'E-Wallet')
ON DUPLICATE KEY UPDATE nama_metode = VALUES(nama_metode), jenis = VALUES(jenis);

INSERT INTO langganan (id_subscription, id_layanan, harga_bulanan, id_metode, tanggal_tagihan, status, tipe, detail, jumlah) VALUES
    ('SUB01', 'LYN01', 186000.00, 'PAY02', 15, 'Aktif', 'STREAMING', '4K UHD + HDR', 4),
    ('SUB02', 'LYN02', 54990.00, 'PAY01', 20, 'Aktif', 'STREAMING', 'Very High Quality', 1),
    ('SUB03', 'LYN03', 79000.00, 'PAY03', 25, 'Aktif', 'STREAMING', 'Full HD', 1),
    ('SUB04', 'LYN04', 350000.00, 'PAY01', 5, 'Aktif', 'PRODUCTIVITY', 'AI coding assistant untuk development', 1),
    ('SUB05', 'LYN05', 150000.00, 'PAY02', 10, 'Aktif', 'PRODUCTIVITY', 'Code completion dan AI pair programming', 1),
    ('SUB06', 'LYN06', 250000.00, 'PAY01', 12, 'Aktif', 'PRODUCTIVITY', 'Cloud infrastructure dan deployment aplikasi', 1),
    ('SUB07', 'LYN07', 150000.00, 'PAY02', 15, 'Aktif', 'PRODUCTIVITY', 'VPS untuk backend, API, dan database', 1),
    ('SUB08', 'LYN08', 100000.00, 'PAY01', 20, 'Aktif', 'PRODUCTIVITY', 'Containerization dan deployment menggunakan Docker', 1)
ON DUPLICATE KEY UPDATE harga_bulanan = VALUES(harga_bulanan), status = VALUES(status), detail = VALUES(detail), jumlah = VALUES(jumlah);
