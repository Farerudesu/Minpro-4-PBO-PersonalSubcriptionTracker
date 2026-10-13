# Minpro-4-PBO-PersonalSubscriptionTracker

```
+------------------------------------------+
| Nama : Muhammad Fahriel                  |
| NIM  : 2509116050                        |
| Tema : Personal Subscription Tracker     |
+------------------------------------------+
```

Aplikasi berbasis terminal (CLI) menggunakan bahasa Java untuk mencatat, mengelola, dan memantau pengeluaran biaya langganan digital pribadi. **Mini Project 4** melanjutkan pengembangan dari Mini Project 3 dengan menghubungkan program ke **Database Relasional MySQL** menggunakan **Prepared Statement JDBC** dan arsitektur **Data Access Object (DAO)**, mengisi **Dummy Data Awal langsung dari file `.sql`**, serta mengimplementasikan **Framework ORM (Hibernate 6 & Jakarta Persistence)** sebagai nilai tambah utama dengan tetap mempertahankan prinsip **Encapsulation**, **Inheritance**, **Polymorphism**, **Abstraction**, **Interface**, dan arsitektur **MVC**.

---

## Daftar Isi

1. [Deskripsi Singkat Program](#1-deskripsi-singkat-program)
2. [Penjelasan Struktur Package](#2-penjelasan-struktur-package)
3. [Penjelasan Alur Program](#3-penjelasan-alur-program)
4. [Penjelasan Penerapan Encapsulation dan Inheritance](#4-penjelasan-penerapan-encapsulation-dan-inheritance)
   - [A. Encapsulation & Access Modifier](#a-encapsulation--access-modifier)
   - [B. Inheritance (Pewarisan)](#b-inheritance-pewarisan)
5. [Penjelasan Penerapan Polymorphism dan Abstraction](#5-penjelasan-penerapan-polymorphism-dan-abstraction)
   - [A. Polymorphism (Overriding & Overloading)](#a-polymorphism-overriding--overloading)
   - [B. Abstraction (Abstract Class & Abstract Method)](#b-abstraction-abstract-class--abstract-method)
   - [C. Keyword `final`](#c-keyword-final)
6. [Penjelasan Penerapan JDBC](#6-penjelasan-penerapan-jdbc)
   - [A. Koneksi Database Relasional](#a-koneksi-database-relasional)
   - [B. Prepared Statement & Keamanan SQL Injection](#b-prepared-statement--keamanan-sql-injection)
   - [C. Arsitektur Data Access Object (DAO)](#c-arsitektur-data-access-object-dao)
   - [D. Dummy Data Awal via Database (.sql)](#d-dummy-data-awal-via-database-sql)
7. [Penjelasan Letak Penerapan Nilai Tambah](#7-penjelasan-letak-penerapan-nilai-tambah)
   - [A. Framework ORM (Hibernate 6 & Jakarta Persistence)](#a-framework-orm-hibernate-6--jakarta-persistence---nilai-tambah-utama)
   - [B. Interface `DapatDidiskon`](#b-interface-dapatdidiskon--modeldapatdidiskonjava)
   - [C. Auto-ID Cerdas Multi-Tabel](#c-auto-id-cerdas-multi-tabel--controllerlangganancontrollerjava)

---

## 1. Deskripsi Singkat Program

**Personal Subscription Tracker** adalah aplikasi yang membantu pengguna mencatat dan mengawasi pengeluaran langganan digital berbayar (seperti Netflix, Spotify, Disney+, Claude Code, GitHub Copilot, Amazon AWS, VPS Hosting, Docker, dll).

Fitur utama aplikasi:

- **Kategorisasi Langganan Dinamis**: Membedakan langganan hiburan (`Streaming`) dan langganan produktivitas/developer/cloud (`Produktivitas & Cloud SaaS`).
- **Persistensi Database Penuh (CRUD Interaktif)**:
  - **Create**: Menambah langganan baru secara terpandu (`[Langkah 1/5]` hingga `[Langkah 5/5]`) dengan ID otomatis sistem, pemilihan layanan dan metode pembayaran, serta kartu pratinjau (*review*) sebelum data disimpan ke MySQL.
  - **Read**: Menampilkan seluruh data langsung dari database dalam bentuk tabel rapi berbingkai (*border box*) atau kartu detail. Dilengkapi fitur **Pencarian (Search)** berdasarkan kata kunci serta **Filter Data** berdasarkan kategori atau status.
  - **Update**: Mengedit data langganan dengan memilih langsung dari daftar nomor urut atau ID, dilengkapi editor *multi-field* interaktif sehingga pengguna dapat mengubah beberapa atribut sekaligus dalam satu sesi.
  - **Delete**: Menghapus langganan dari sistem dan MySQL dengan konfirmasi aman dan kartu detail peringatan.
- **Data Awal dari Database (.sql)**: Sesuai ketentuan, data awal tidak dibuat lewat kode Java melainkan disediakan langsung melalui skrip database `database/personal_subcrition_tracker.sql` (8 data dummy awal) sehingga saat fitur Read dijalankan pertama kali, data langsung tampil dari database.
- **Ringkasan Finansial**: Menghitung total pengeluaran per bulan dengan rincian proporsional kategori streaming vs produktif.
- **Simulasi & Proyeksi Tahunan**: Menghitung proyeksi pengeluaran akumulatif dan tahunan menggunakan perhitungan polimorfik subclass serta interface diskon.

---

## 2. Penjelasan Struktur Package

Proyek menerapkan arsitektur modular yang rapi dengan memisahkan antarmuka, logika bisnis, akses database, konfigurasi ORM, dan model entitas:

```
com.pbo.fareru.minpro.pbo
├── config/                         <- Konfigurasi koneksi & ORM
│   ├── Koneksi.java                <- Utility koneksi JDBC ke MySQL
│   ├── TesKoneksi.java             <- Verifikasi koneksi JDBC
│   ├── HibernateUtil.java          <- SessionFactory Manager (Nilai Tambah ORM)
│   └── TesORM.java                 <- Pengujian query entitas via Hibernate ORM
├── dao/                            <- Data Access Object (DAO Layer)
│   ├── LanggananDAO.java           <- Interface kontrak operasi CRUD
│   ├── LanggananDAOImpl.java       <- Implementasi DAO dengan Prepared Statement JDBC
│   └── LanggananMapper.java        <- Mapper ResultSet JDBC ke objek Model
├── model/                          <- Model data, entitas JPA, & logika bisnis
│   ├── Langganan.java              <- Abstract class (Superclass, Entity JPA)
│   ├── LanggananStreaming.java     <- Subclass Streaming (implements DapatDidiskon)
│   ├── LanggananProduktivitas.java <- Subclass Produktivitas (implements DapatDidiskon)
│   ├── DapatDidiskon.java          <- Interface kontrak diskon
│   ├── Layanan.java                <- Entitas Layanan (Entity JPA)
│   └── MetodePembayaran.java       <- Entitas Metode Pembayaran (Entity JPA)
├── view/                           <- View: tampilan CLI antarmuka terminal
│   └── LanggananView.java          <- Menu, tabel border box, kartu, format Rupiah
├── controller/                     <- Controller & validasi input
│   ├── LanggananController.java    <- Pengendali alur program, CRUD, & logika bisnis
│   └── InputValidator.java         <- Validasi input anti-crash & nilai default
└── personalsubscriptiontracker/
    └── Main.java                   <- Entry point utama program
```

File konfigurasi & database eksternal:
- `database/personal_subcrition_tracker.sql`: Skrip DDL tabel MySQL (`layanan`, `metode_pembayaran`, `langganan`) beserta 8 data dummy awal.
- `src/main/resources/hibernate.cfg.xml`: Konfigurasi Hibernate ORM dan pemetaan entitas JPA.

Pembagian tanggung jawab layer:
- **Model**: Menyimpan atribut data, entitas relasional, enkapsulasi setter/getter, dan aturan perhitungan bisnis.
- **View**: Menangani representasi visual terminal (tabel ASCII, kartu detail, garis pembatas, format mata uang Rupiah, dan notifikasi status).
- **Controller**: Mengatur alur program, menerima input melalui `InputValidator`, menginstansiasi objek model, dan memanggil DAO.
- **DAO Layer**: Memisahkan logika manipulasi basis data dari logika bisnis aplikasi menggunakan SQL Prepared Statement murni.
- **Config & ORM**: Mengelola koneksi database JDBC dan konfigurasi session Hibernate ORM.

---

## 3. Penjelasan Alur Program

```
[Start Program] -> [Koneksi ke MySQL] -> [Muat Data Layanan, Metode, & Langganan dari DB]
       |
       v
+---------------------------------------+
|           TAMPIL MENU UTAMA           | <-------------+
+---------------------------------------+               |
| 1. Tampilkan & Cari Langganan (Read)  |               |
| 2. Tambah Langganan Baru      (Create)|               |
| 3. Edit Data Langganan        (Update)|               |
| 4. Batalkan / Hapus Langganan (Delete)|               |
| 5. Ringkasan Pengeluaran              |               |
| 6. Simulasi Estimasi Biaya            |               |
| 7. Keluar                             |               |
+---------------------------------------+               |
       |                                                |
       +---> Pilih 1-6 -> Eksekusi DAO / Bisnis --------+
       |
       +---> Pilih 7   -> Tutup Resource & Thread -----> [Program Berhenti]
```

1. **Inisialisasi & Pemuatan Data (`Main.java`)**:
   - `LanggananController` diinstansiasi.
   - Method `muatDataDatabase()` langsung membaca tabel `layanan`, `metode_pembayaran`, dan memanggil `langgananDAO.findAll()` melalui JDBC.
   - Seluruh data dummy awal dari file `.sql` langsung terisi ke dalam memori aplikasi tanpa perlu input awal dari kode Java.
2. **Menampilkan Menu Utama**:
   Program menyajikan antarmuka interaktif 7 menu utama.
3. **Eksekusi Fitur CRUD & Analisis**:
   - **Menu 1 (Read - Tampilkan, Cari, & Filter)**:
     Menyediakan sub-menu interaktif:
     1. Tampilkan seluruh data dalam format tabel ringkas rapi (*border box*) dengan penomoran urut.
     2. Tampilkan seluruh data dalam format kartu detail lengkap.
     3. **Cari Langganan**: Mencari data berdasarkan nama layanan, ID, kategori, atau metode pembayaran.
     4. **Filter Langganan**: Menyaring data berdasarkan kategori (*Streaming* vs *Produktivitas*) atau status (*Aktif* vs *Nonaktif*).
   - **Menu 2 (Create - Tambah Langganan Terpandu)**:
     - ID langganan baru dibuat otomatis oleh sistem (`SUB09`, `SUB10`, dst).
     - Pengguna dipandu melalui 5 langkah berurutan: memilih tipe, memilih layanan (bisa memilih layanan yang ada atau mendaftarkan layanan baru dengan ID otomatis `LYNxx`), memilih metode pembayaran (bisa memilih metode yang ada atau mendaftarkan metode baru dengan ID otomatis `PAYxx`), mengisi nominal tarif & tanggal tagihan, serta mengisi atribut spesifik kategori.
     - Menampilkan **Kartu Pratinjau (*Review*)** data sebelum disimpan ke database, lalu mengeksekusi `langgananDAO.save(...)`.
   - **Menu 3 (Update - Edit Data Interaktif)**:
     - Program menampilkan tabel seluruh langganan terlebih dahulu sehingga pengguna tidak perlu menghafal ID.
     - Pengguna cukup memilih nomor urut (`1..N`) atau mengetik ID (`SUBxx`), atau mengetik `0` untuk batal.
     - Menyediakan editor *multi-field* interaktif: pengguna dapat mengubah harga, tanggal tagihan, status aktif/nonaktif, metode pembayaran, hingga atribut spesifik kategori dalam satu sesi pengeditan tanpa keluar ke menu utama. Setiap pembaruan langsung disimpan ke database via `langgananDAO.update(...)`.
   - **Menu 4 (Delete - Hapus / Batalkan Langganan)**:
     - Menampilkan tabel seluruh langganan dengan nomor urut.
     - Pengguna memilih data yang ingin dihapus via nomor urut atau ID.
     - Menampilkan kartu detail data yang akan dihapus beserta peringatan konfirmasi `(y/n)`. Jika disetujui, program menjalankan `langgananDAO.delete(...)` dan menghapus data dari MySQL secara permanen.
   - **Menu 5 (Ringkasan Pengeluaran)**:
     Menghitung akumulasi biaya bulanan seluruh langganan aktif dan memisahkan total pengeluaran kategori hiburan streaming vs utilitas produktif, disertai evaluasi batas wajar pengeluaran.
   - **Menu 6 (Simulasi Biaya & Polimorfisme)**:
     Menghitung proyeksi biaya $N$ bulan (dengan opsi diskon voucher tambahan via *method overloading*) dan dilanjutkan **Proyeksi Tahunan** yang memanggil *abstract method* `hitungBiayaTahunan()` serta *interface* `hitungDiskon()` untuk masing-masing langganan aktif.
   - **Menu 7 (Keluar)**:
     Menghentikan loop menu utama, membersihkan thread koneksi database MySQL via `AbandonedConnectionCleanupThread.checkedShutdown()`, dan menutup aplikasi secara rapi.

---

## 4. Penjelasan Penerapan Encapsulation dan Inheritance

### A. Encapsulation & Access Modifier

Enkapsulasi menyatukan data dengan method yang mengelolanya serta menyembunyikan detail representasi internal dari akses langsung di luar kelas:

- **Access Modifier `private`**:
  Semua atribut pada kelas model (`idSubscription`, `layanan`, `hargaBulanan`, `metode`, `tanggalTagihan`, `status`, `kualitasResolusi`, `batasLayar`, `kapasitasStorage`, `lisensiUser`) berstatus `private`. Atribut tidak dapat diakses atau diubah secara langsung dari luar kelas.
- **Access Modifier `public`**:
  Getter, setter, konstruktor, dan method operasional berstatus `public` sebagai pintu akses terkontrol.
- **Access Modifier `protected`**:
  Konstanta `BATAS_BULAN_DISKON` berstatus `protected` di kelas induk agar dapat diakses langsung oleh subclass tanpa perantara getter.
- **Validasi Nilai pada Setter**:
  Setter menyaring data agar objek selalu berada dalam kondisi valid:
  - `setHargaBulanan(double harga)`: Memastikan harga tidak bernilai negatif; jika negatif maka otomatis diubah menjadi `0`.
  - `setTanggalTagihan(int tgl)`: Memastikan tanggal berada pada rentang valid `1` sampai `31`.
  - `setBatasLayar(int batas)` dan `setLisensiUser(int lisensi)`: Memastikan kuota minimal bernilai `1`.
  - `setIdSubscription(String id)` dan `setStatus(String status)`: Mencegah nilai `null` dan string kosong.
- **Keyword `final`**:
  Konstanta `protected static final int BATAS_BULAN_DISKON = 12` bernilai tetap dan tidak dapat dimodifikasi setelah diinisialisasi.

```java
// Penerapan Enkapsulasi di model/Langganan.java
public abstract class Langganan {
    protected static final int BATAS_BULAN_DISKON = 12;

    private String idSubscription;
    private Layanan layanan;
    private double hargaBulanan;
    private MetodePembayaran metode;
    private int tanggalTagihan;
    private String status;

    public void setHargaBulanan(double hargaBulanan) {
        if (hargaBulanan >= 0) {
            this.hargaBulanan = hargaBulanan;
        } else {
            this.hargaBulanan = 0;
        }
    }
}
```

### B. Inheritance (Pewarisan)

Mewujudkan hierarki hubungan *is-a* antara kelas induk dan kelas anak:

```
                      +-----------------------+
                      |       Langganan       |  <- Superclass (abstract)
                      +-----------------------+
                                  ▲
                                  │ extends
                  ┌───────────────┴───────────────┐
                  │                               │
     +-------------------------+     +--------------------------+
     |   LanggananStreaming    |     |  LanggananProduktivitas  | <- Subclass
     +-------------------------+     +--------------------------+
     | - kualitasResolusi      |     | - kapasitasStorage       |
     | - batasLayar            |     | - lisensiUser            |
     +-------------------------+     +--------------------------+
```

- **Superclass**: Kelas `Langganan` berfungsi sebagai *superclass* yang mewariskan atribut umum (`idSubscription`, `layanan`, `hargaBulanan`, `metode`, `tanggalTagihan`, `status`) serta method operasional (`hitungEstimasiBiaya`, `tampilkanDetail`).
- **Subclass**:
  - `LanggananStreaming`: Mewarisi `Langganan` dengan keyword `extends` dan menambahkan atribut spesifik streaming: `kualitasResolusi` (resolusi video/audio) dan `batasLayar` (kuota simultan perangkat).
  - `LanggananProduktivitas`: Mewarisi `Langganan` dan menambahkan atribut spesifik SaaS: `kapasitasStorage` (kuota penyimpanan/tier spek) dan `lisensiUser` (jumlah akun pengguna berlisensi).
- **Konstruktor `super(...)`**:
  Kedua subclass memanggil konstruktor superclass menggunakan `super(idSubscription, layanan, hargaBulanan, metode, tanggalTagihan)` untuk menginisialisasi atribut turunan secara benar.

---

## 5. Penjelasan Penerapan Polymorphism dan Abstraction

### A. Polymorphism (Overriding & Overloading)

Polimorfisme memungkinkan satu antarmuka atau method memiliki banyak wujud implementasi:

#### 1. Method Overriding (Dynamic / Runtime Polymorphism)
Method pada superclass didefinisikan ulang oleh subclass untuk menghasilkan perilaku yang spesifik terhadap tipe objek saat runtime:

- **`hitungBiayaTahunan()`**:
  - Pada `LanggananStreaming`: Memberikan diskon tahunan sebesar **5%**:
    $$\text{Biaya} = \text{Harga Bulanan} \times 12 \times 0.95$$
  - Pada `LanggananProduktivitas`: Memberikan diskon tahunan sebesar **10%**:
    $$\text{Biaya} = \text{Harga Bulanan} \times 12 \times 0.90$$
- **`getTipeLangganan()`**:
  - Mengembalikan `"Streaming (Video/Audio)"` pada `LanggananStreaming`.
  - Mengembalikan `"Produktivitas & Cloud SaaS"` pada `LanggananProduktivitas`.
- **`tampilkanDetail()`**:
  - `LanggananStreaming` memanggil `super.tampilkanDetail()` lalu menambahkan output kualitas resolusi dan batas layar.
  - `LanggananProduktivitas` memanggil `super.tampilkanDetail()` lalu menambahkan output kapasitas storage dan lisensi user.
- **`hitungEstimasiBiaya(int bulan)`**:
  Menghitung akumulasi biaya bulanan dengan otomatis menerapkan diskon khusus jika durasi mencapai $\ge 12$ bulan sesuai karakteristik masing-masing subclass.

Pemanggilan polimorfik dinamis pada Controller:
```java
for (Langganan sub : listLangganan) {
    // Memanggil method yang sama, JVM mengeksekusi implementasi subclass yang sesuai
    double tahunan = sub.hitungBiayaTahunan();
}
```

#### 2. Method Overloading (Static / Compile-time Polymorphism)
Method yang memiliki nama sama di dalam satu kelas tetapi dibedakan oleh parameter (*signature*):

- **Overloading `tampilkanDetail()`**:
  - `tampilkanDetail()`: Menampilkan kartu atribut lengkap multi-baris.
  - `tampilkanDetail(boolean ringkas)`: Menampilkan data dalam format ringkas satu baris saat parameter bernilai `true`.
- **Overloading `hitungEstimasiBiaya()`**:
  - `hitungEstimasiBiaya(int bulan)`: Menghitung biaya standar berdasarkan durasi bulan.
  - `hitungEstimasiBiaya(int bulan, double diskonPersen)`: Menghitung biaya dengan menerapkan potongan persentase voucher diskon tambahan.

### B. Abstraction (Abstract Class & Abstract Method)

Abstraksi memisahkan rancangan konsep umum (*what to do*) dari implementasi detail konkret (*how to do*):

1. **Abstract Class (`Langganan`)**:
   Dideklarasikan dengan `public abstract class Langganan`. Kelas ini merupakan cetak biru konseptual langganan dan **tidak dapat diinstansiasi secara langsung** (`new Langganan(...)` dicegah oleh compiler). Hanya kelas turunan konkret (`LanggananStreaming` dan `LanggananProduktivitas`) yang dapat diinstansiasi.
2. **Abstract Method**:
   Method tanpa body yang mewajibkan seluruh subclass untuk mengimplementasikan logikanya masing-masing:
   - `public abstract String getTipeLangganan();`
   - `public abstract double hitungBiayaTahunan();`

### C. Keyword `final`

- `protected static final int BATAS_BULAN_DISKON = 12`:
  - `static`: Menjadi variabel milik kelas (berbagi satu alokasi memori).
  - `final`: Nilai konstanta bersifat absolut dan tidak dapat diubah setelah dideklarasikan.
  - `protected`: Dapat diakses langsung oleh `LanggananStreaming` dan `LanggananProduktivitas` tanpa perlu *getter*.

---

## 6. Penjelasan Penerapan JDBC

Persistensi data pada Mini Project 4 diimplementasikan menggunakan koneksi database relasional MySQL, pola **Prepared Statement**, dan arsitektur **Data Access Object (DAO)**:

### A. Koneksi Database Relasional
Pengelolaan koneksi tersentralisasi pada kelas utilitas [`Koneksi.java`](src/main/java/com/pbo/fareru/minpro/pbo/config/Koneksi.java) menggunakan driver JDBC MySQL (`com.mysql.cj.jdbc.Driver`):

```java
public final class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/personal_subcrition_tracker?serverTimezone=Asia/Makassar";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

### B. Prepared Statement & Keamanan SQL Injection
Seluruh operasi manipulasi dan pembacaan database wajib menggunakan `PreparedStatement` dengan parameterisasi query (`?`). Hal ini mencegah kerentanan keamanan SQL Injection serta meningkatkan efisiensi kompilasi query pada mesin MySQL:

- **Read All (`SELECT`)**:
  ```sql
  SELECT l.*, ly.nama_layanan, ly.kategori, m.nama_metode, m.jenis 
  FROM langganan l 
  JOIN layanan ly ON ly.id_layanan=l.id_layanan 
  JOIN metode_pembayaran m ON m.id_metode=l.id_metode
  ```
- **Read by ID (`SELECT`)**:
  `... WHERE l.id_subscription = ?`
- **Create (`INSERT`)**:
  ```sql
  INSERT INTO langganan 
  (id_subscription, id_layanan, harga_bulanan, id_metode, tanggal_tagihan, status, tipe, detail, jumlah) 
  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
  ```
- **Update (`UPDATE`)**:
  ```sql
  UPDATE langganan 
  SET id_metode=?, harga_bulanan=?, tanggal_tagihan=?, status=?, detail=?, jumlah=? 
  WHERE id_subscription=?
  ```
- **Delete (`DELETE`)**:
  ```sql
  DELETE FROM langganan WHERE id_subscription = ?
  ```

Seluruh statement dieksekusi di dalam blok **`try-with-resources`** untuk menjamin `Connection`, `PreparedStatement`, dan `ResultSet` ditutup secara otomatis setelah pemrosesan selesai, sehingga mencegah terjadinya *resource leak*.

### C. Arsitektur Data Access Object (DAO)
Pemisahan logika akses data ke layer DAO:

1. **DAO Interface ([`LanggananDAO.java`](src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAO.java))**:
   Mendefinisikan kontrak operasi CRUD murni:
   ```java
   public interface LanggananDAO {
       List<Langganan> findAll() throws SQLException;
       Langganan findById(String id) throws SQLException;
       void save(Langganan langganan) throws SQLException;
       void update(Langganan langganan) throws SQLException;
       void delete(String id) throws SQLException;
   }
   ```
2. **DAO Implementation ([`LanggananDAOImpl.java`](src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAOImpl.java))**:
   Mengimplementasikan interface DAO secara konkret menggunakan koneksi JDBC dan mengeksekusi *PreparedStatement* ke MySQL.
3. **Data Mapper ([`LanggananMapper.java`](src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananMapper.java))**:
   Memetakan baris data pada `ResultSet` relasional ke dalam objek model OOP yang sesuai (`LanggananStreaming` atau `LanggananProduktivitas`) berdasarkan kolom diskriminator `tipe`.

### D. Dummy Data Awal via Database (.sql)
Sesuai instruksi tugas, data awal **tidak dibuat melalui kode Java**, melainkan diisi langsung melalui file skrip SQL eksternal:
- **Lokasi file**: [`database/personal_subcrition_tracker.sql`](database/personal_subcrition_tracker.sql)
- **Isi file**:
  - DDL pembentukan database `personal_subcrition_tracker`.
  - Tabel `layanan`, `metode_pembayaran`, dan `langganan` beserta relasi *Foreign Key*.
  - **8 data dummy langganan awal** (Netflix, Spotify, Disney+, Claude Code, GitHub Copilot, Amazon AWS, VPS Hosting, Docker).
  - Saat aplikasi dijalankan pertama kali, data langsung terbaca via `langgananDAO.findAll()` dan tampil saat menu Read dibuka tanpa perlu input manual.

---

## 7. Penjelasan Letak Penerapan Nilai Tambah

### A. Framework ORM (Hibernate 6 & Jakarta Persistence) - *Nilai Tambah Utama*

Proyek ini mengintegrasikan **Framework ORM (Object-Relational Mapping)** menggunakan **Hibernate ORM 6.6.1.Final** bersama standar **Jakarta Persistence API (JPA) 3.2.0**:

1. **Konfigurasi XML ([`src/main/resources/hibernate.cfg.xml`](src/main/resources/hibernate.cfg.xml))**:
   Menghubungkan aplikasi ke MySQL melalui dialek `org.hibernate.dialect.MySQLDialect`, mengatur driver JDBC, pooling koneksi, dan mendaftarkan kelas entitas terpetakan.
2. **Pemetaan Anotasi Entitas JPA pada Model**:
   Kelas model dipetakan ke tabel relasional menggunakan anotasi resmi JPA:
   - [`Layanan.java`](src/main/java/com/pbo/fareru/minpro/pbo/model/Layanan.java):
     `@Entity`, `@Table(name = "layanan")`, `@Id`, `@Column(name = "id_layanan")`, `@Column(name = "nama_layanan")`, `@Column(name = "kategori")`.
   - [`MetodePembayaran.java`](src/main/java/com/pbo/fareru/minpro/pbo/model/MetodePembayaran.java):
     `@Entity`, `@Table(name = "metode_pembayaran")`, `@Id`, `@Column(name = "id_metode")`, `@Column(name = "nama_metode")`, `@Column(name = "jenis")`.
   - [`Langganan.java`](src/main/java/com/pbo/fareru/minpro/pbo/model/Langganan.java):
     `@Entity`, `@Id` pada atribut `idSubscription`.
3. **Session Management ([`HibernateUtil.java`](src/main/java/com/pbo/fareru/minpro/pbo/config/HibernateUtil.java))**:
   Mengelola siklus hidup `SessionFactory` menggunakan `StandardServiceRegistry` dan konfigurasi otomatis.
4. **Program Pengujian ORM Terverifikasi ([`TesORM.java`](src/main/java/com/pbo/fareru/minpro/pbo/config/TesORM.java))**:
   Menyediakan file executable mandiri yang membuktikan query entitas murni via **HQL (Hibernate Query Language)**:
   ```java
   try (Session session = sf.openSession()) {
       List<Layanan> list = session.createQuery("from Layanan", Layanan.class).list();
       for (Layanan l : list) {
           System.out.println("- [" + l.getIdLayanan() + "] " + l.getNamaLayanan());
       }
   }
   ```
   Pengujian ini berhasil mengeksekusi pembacaan entitas objek dari database MySQL secara murni menggunakan framework Hibernate ORM.

### B. Interface `DapatDidiskon` (`model/DapatDidiskon.java`)

Interface mendefinisikan kontrak kemampuan `hitungDiskon(int bulan)` yang dapat diterapkan pada kelas apa pun tanpa terikat langsung pada hierarki warisan `Langganan`:

```java
public interface DapatDidiskon {
    double hitungDiskon(int bulan);
}
```

- Diimplementasikan oleh `LanggananStreaming` (potongan 5% jika $\ge 12$ bulan).
- Diimplementasikan oleh `LanggananProduktivitas` (potongan 10% jika $\ge 12$ bulan).
- Dipanggil secara polimorfik pada Menu 6 menggunakan pemeriksaan tipe `sub instanceof DapatDidiskon`.

### C. Auto-ID Cerdas Multi-Tabel (`controller/LanggananController.java`)

Untuk meningkatkan interaktivitas dan kenyamanan pengguna:
- **`generateIdOtomatis()`**: Mengkalkulasikan ID langganan baru (`SUB09`, `SUB10`, dst) dari nomor urut terbesar yang tersimpan di sistem.
- **`generateIdLayananOtomatis()`**: Mengkalkulasikan ID layanan baru (`LYN09`, `LYN10`, dst) saat mendaftarkan layanan baru.
- **`generateIdMetodeOtomatis()`**: Mengkalkulasikan ID metode baru (`PAY04`, `PAY05`, dst) saat mendaftarkan metode baru.

Pengguna tidak perlu lagi mengingat atau mengetik ID primary key secara manual, sehingga mencegah duplikasi data maupun kesalahan penulisan ID.
