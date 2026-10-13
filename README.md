# Minpro-4-PBO-PersonalSubscriptionTracker

```text
+------------------------------------------+
| Nama : Muhammad Fahriel                  |
| NIM  : 2509116050                        |
| Tema : Personal Subscription Tracker     |
+------------------------------------------+
```

Personal Subscription Tracker adalah aplikasi berbasis terminal (CLI) yang dibuat menggunakan bahasa Java untuk mencatat dan mengelola pengeluaran dari berbagai layanan langganan digital.

**Mini Project 4** merupakan pengembangan dari Mini Project 3. Pada versi ini, program sudah terhubung ke database MySQL menggunakan JDBC dan `PreparedStatement`. Program juga menggunakan pola DAO untuk mengatur proses pengelolaan data ke database. Selain itu, terdapat penerapan Hibernate ORM dan Jakarta Persistence sebagai nilai tambah, serta penerapan konsep PBO seperti Encapsulation, Inheritance, Polymorphism, Abstraction, Interface, dan arsitektur MVC.

---

## Daftar Isi

1. [Deskripsi Singkat Program](#1-deskripsi-singkat-program)
2. [Struktur Package](#2-struktur-package)
3. [Alur Program](#3-alur-program)
4. [Penerapan Encapsulation dan Inheritance](#4-penerapan-encapsulation-dan-inheritance)
   - [A. Encapsulation dan Access Modifier](#a-encapsulation-dan-access-modifier)
   - [B. Inheritance (Pewarisan)](#b-inheritance-pewarisan)
5. [Penerapan Polymorphism dan Abstraction](#5-penerapan-polymorphism-dan-abstraction)
   - [A. Polymorphism](#a-polymorphism)
   - [B. Abstraction](#b-abstraction)
   - [C. Keyword `final`](#c-keyword-final)
6. [Penerapan JDBC](#6-penerapan-jdbc)
   - [A. Koneksi ke Database](#a-koneksi-ke-database)
   - [B. PreparedStatement dan SQL Injection](#b-preparedstatement-dan-sql-injection)
   - [C. Data Access Object (DAO)](#c-data-access-object-dao)
   - [D. Data Awal dari File SQL](#d-data-awal-dari-file-sql)
7. [Nilai Tambah Program](#7-nilai-tambah-program)
   - [A. Hibernate ORM dan Jakarta Persistence](#a-hibernate-orm-dan-jakarta-persistence)
   - [B. Interface `DapatDidiskon`](#b-interface-dapatdidiskon)
   - [C. Pembuatan ID Otomatis](#c-pembuatan-id-otomatis)

---

## 1. Deskripsi Singkat Program

**Personal Subscription Tracker** merupakan aplikasi yang digunakan untuk mencatat dan memantau biaya langganan digital, seperti Netflix, Spotify, Disney+, Claude Code, GitHub Copilot, Amazon AWS, VPS Hosting, dan Docker.

Program ini dibuat agar pengguna dapat mengetahui layanan apa saja yang sedang digunakan dan berapa total biaya langganan yang harus dibayar setiap bulan.

Fitur yang tersedia meliputi:

- **Pengelompokan Langganan:** Memisahkan langganan menjadi dua kategori, yaitu Streaming dan Produktivitas & Cloud SaaS.
- **Menampilkan Data Langganan:** Menampilkan seluruh data yang tersimpan di database dalam bentuk tabel atau kartu detail.
- **Mencari dan Memfilter Data:** Mencari langganan berdasarkan nama layanan, ID, kategori, atau metode pembayaran. Pengguna juga bisa memfilter data berdasarkan kategori dan status langganan.
- **Menambahkan Langganan:** Pengguna dapat menambahkan langganan baru melalui beberapa langkah, mulai dari memilih kategori dan layanan hingga mengisi harga serta tanggal tagihan. ID akan dibuat otomatis oleh sistem.
- **Mengedit Langganan:** Pengguna dapat memilih data berdasarkan nomor urut atau ID, kemudian mengubah beberapa informasi sekaligus, seperti harga, tanggal tagihan, status, dan metode pembayaran.
- **Menghapus Langganan:** Data yang dipilih dapat dihapus setelah pengguna memeriksa detailnya dan memberikan konfirmasi.
- **Ringkasan Pengeluaran:** Menghitung total biaya langganan aktif setiap bulan dan membaginya berdasarkan kategori.
- **Simulasi Biaya:** Menghitung perkiraan biaya untuk beberapa bulan atau satu tahun, termasuk potongan harga yang berlaku.
- **Data Awal dari File SQL:** Program sudah memiliki delapan data langganan awal yang dimasukkan langsung melalui file SQL, bukan melalui kode Java.

Seluruh perubahan data langganan dilakukan melalui database MySQL sehingga data tetap tersimpan meskipun program ditutup.

---

## 2. Struktur Package

Struktur program dibagi menjadi beberapa package agar setiap bagian memiliki tugas yang berbeda. Dengan cara ini, kode untuk tampilan, pengolahan data, koneksi database, dan aturan bisnis tidak tercampur dalam satu kelas.

```text
com.pbo.fareru.minpro.pbo
├── config/
│   ├── Koneksi.java
│   ├── TesKoneksi.java
│   ├── HibernateUtil.java
│   └── TesORM.java
│
├── dao/
│   ├── LanggananDAO.java
│   ├── LanggananDAOImpl.java
│   └── LanggananMapper.java
│
├── model/
│   ├── Langganan.java
│   ├── LanggananStreaming.java
│   ├── LanggananProduktivitas.java
│   ├── DapatDidiskon.java
│   ├── Layanan.java
│   └── MetodePembayaran.java
│
├── view/
│   └── LanggananView.java
│
├── controller/
│   ├── LanggananController.java
│   └── InputValidator.java
│
└── personalsubscriptiontracker/
    └── Main.java
```

Berikut fungsi dari masing-masing package:

- **`config`**: Berisi kelas untuk mengatur koneksi JDBC, konfigurasi Hibernate, dan pengujian koneksi maupun ORM.
- **`dao`**: Berisi interface dan kelas yang menangani proses membaca, menambah, mengubah, serta menghapus data dari database.
- **`model`**: Berisi kelas untuk menyimpan data langganan, layanan, metode pembayaran, dan aturan perhitungan biaya.
- **`view`**: Mengatur tampilan program di terminal, seperti menu, tabel, kartu detail, dan informasi biaya.
- **`controller`**: Mengatur jalannya program, memproses pilihan pengguna, memvalidasi input, dan menghubungkan tampilan dengan proses pengolahan data.
- **`personalsubscriptiontracker`**: Berisi kelas `Main.java` yang menjadi titik awal ketika program dijalankan.

Selain package tersebut, terdapat dua file pendukung:

- `database/personal_subcrition_tracker.sql`: Berisi perintah untuk membuat database, tabel, relasi antartabel, dan delapan data langganan awal.
- `src/main/resources/hibernate.cfg.xml`: Berisi pengaturan yang digunakan Hibernate untuk terhubung ke database dan mengenali kelas entitas.

Program ini juga menggunakan pola MVC (Model-View-Controller). Model mengatur data dan perhitungan, View menampilkan informasi kepada pengguna, sedangkan Controller mengatur alur program dan menghubungkan keduanya dengan DAO.

---

## 3. Alur Program

Secara umum, program dimulai dengan menghubungkan aplikasi ke database MySQL. Setelah itu, data layanan, metode pembayaran, dan langganan dibaca dari database. Pengguna kemudian dapat memilih menu yang tersedia sesuai kebutuhan.

```text
[Program Dimulai]
        |
        v
[Koneksi ke Database MySQL]
        |
        v
[Membaca Data dari Database]
        |
        v
+------------------------------+
|          Menu Utama          |
+------------------------------+
| 1. Tampilkan dan Cari Data   |
| 2. Tambah Langganan          |
| 3. Edit Langganan            |
| 4. Hapus Langganan           |
| 5. Ringkasan Pengeluaran     |
| 6. Simulasi Biaya            |
| 7. Keluar                    |
+------------------------------+
        |
        v
[Proses Sesuai Pilihan Pengguna]
        |
        +---- Kembali ke Menu Utama
        |
        +---- Keluar dari Program
```

### 1. Program dimulai

Saat `Main.java` dijalankan, program membuat objek `LanggananController`. Controller kemudian menjalankan method `muatDataDatabase()` untuk membaca data layanan dan metode pembayaran, serta mengambil data langganan melalui `langgananDAO.findAll()`.

Data yang sudah tersimpan di MySQL dapat langsung ditampilkan tanpa harus dimasukkan ulang oleh pengguna.

### 2. Menampilkan menu utama

Program menyediakan tujuh pilihan menu. Pengguna dapat memilih fitur yang ingin digunakan dengan memasukkan nomor menu melalui terminal.

### 3. Menjalankan fitur program

**Menu 1 — Menampilkan dan Mencari Langganan**

Pengguna dapat memilih untuk menampilkan seluruh langganan dalam bentuk tabel atau kartu detail. Selain itu, tersedia fitur pencarian berdasarkan kata kunci dan filter berdasarkan kategori maupun status langganan.

**Menu 2 — Menambahkan Langganan Baru**

Pengguna mengikuti lima langkah pengisian data, mulai dari memilih kategori langganan, memilih atau menambahkan layanan, memilih atau menambahkan metode pembayaran, mengisi harga dan tanggal tagihan, hingga mengisi informasi khusus sesuai kategori.

Sebelum disimpan, program menampilkan ringkasan data yang sudah diisi agar pengguna dapat memeriksanya terlebih dahulu. Jika sudah sesuai, data akan disimpan ke MySQL melalui `langgananDAO.save(...)`.

ID langganan, layanan, dan metode pembayaran dibuat otomatis oleh program.

**Menu 3 — Mengedit Data Langganan**

Program menampilkan daftar langganan terlebih dahulu. Pengguna dapat memilih data berdasarkan nomor urut atau ID, lalu mengubah informasi yang diperlukan dalam satu sesi pengeditan.

Setelah perubahan selesai, data diperbarui di database melalui `langgananDAO.update(...)`.

**Menu 4 — Menghapus Langganan**

Pengguna memilih langganan yang ingin dihapus dari daftar. Program kemudian menampilkan detail data tersebut dan meminta konfirmasi sebelum menghapusnya.

Jika pengguna menyetujui penghapusan, program menjalankan `langgananDAO.delete(...)`. Data yang dihapus tidak lagi tersimpan di database.

**Menu 5 — Ringkasan Pengeluaran**

Program menghitung total biaya bulanan dari seluruh langganan yang berstatus aktif. Hasilnya dibagi menjadi pengeluaran untuk layanan streaming dan layanan produktivitas, sehingga pengguna dapat melihat kategori yang paling banyak menghabiskan biaya.

**Menu 6 — Simulasi Biaya**

Pengguna dapat menghitung perkiraan biaya langganan untuk beberapa bulan, dengan pilihan diskon tambahan. Program juga menghitung perkiraan biaya selama satu tahun menggunakan method yang berbeda sesuai jenis langganan.

**Menu 7 — Keluar**

Program menghentikan menu utama dan membersihkan resource yang berkaitan dengan koneksi database. Untuk koneksi JDBC yang digunakan oleh aplikasi, resource ditutup sesuai kebutuhan. Program kemudian berakhir.

---

## 4. Penerapan Encapsulation dan Inheritance

### A. Encapsulation dan Access Modifier

Encapsulation atau enkapsulasi digunakan untuk melindungi data di dalam kelas agar tidak bisa diubah secara langsung dari luar kelas. Pada program ini, atribut yang terdapat di kelas model menggunakan access modifier `private`, sedangkan akses terhadap data dilakukan melalui getter dan setter.

Contohnya terdapat pada kelas `Langganan.java`, yang memiliki atribut seperti `idSubscription`, `layanan`, `hargaBulanan`, `metode`, `tanggalTagihan`, dan `status`.

Berikut fungsi access modifier yang digunakan:

- **`private`**: Digunakan pada atribut agar data tidak dapat diakses langsung dari luar kelas.
- **`public`**: Digunakan pada method yang perlu dipanggil dari kelas lain, seperti getter, setter, konstruktor, dan method untuk menghitung biaya.
- **`protected`**: Digunakan pada konstanta `BATAS_BULAN_DISKON` agar dapat diakses oleh subclass.
- **`final`**: Membuat nilai konstanta tidak dapat diubah setelah ditetapkan.

Setter juga dapat digunakan untuk memeriksa nilai sebelum disimpan ke atribut. Contohnya, harga langganan tidak boleh negatif dan tanggal tagihan harus berada pada rentang 1–31.

Contoh penerapannya:

```java
// Penerapan Encapsulation pada model/Langganan.java
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

Pada method `setHargaBulanan()`, program memeriksa nilai yang diberikan terlebih dahulu. Jika harga tidak negatif, nilai tersebut disimpan. Jika harga negatif, program menetapkan harga menjadi `0`.

Dengan cara ini, setter tidak hanya digunakan untuk mengubah data, tetapi juga dapat membantu menjaga agar nilai atribut tetap sesuai dengan aturan program.

### B. Inheritance (Pewarisan)

Inheritance atau pewarisan digunakan agar kelas lain dapat menggunakan atribut dan method dari kelas induknya. Pada program ini, kelas `Langganan` menjadi kelas induk yang memiliki informasi umum untuk semua jenis langganan.

Dua kelas yang mewarisi `Langganan` adalah `LanggananStreaming` dan `LanggananProduktivitas`.

```text
                 +-----------------------+
                 |       Langganan       |
                 |    Abstract Class     |
                 +-----------------------+
                            |
                         extends
                            |
              +-------------+-------------+
              |                           |
+---------------------------+ +---------------------------+
|     LanggananStreaming    | |  LanggananProduktivitas   |
+---------------------------+ +---------------------------+
| - kualitasResolusi        | | - kapasitasStorage        |
| - batasLayar              | | - lisensiUser             |
+---------------------------+ +---------------------------+
```

Kelas `Langganan` menyimpan atribut yang digunakan bersama oleh kedua jenis langganan, seperti ID, layanan, harga bulanan, metode pembayaran, tanggal tagihan, dan status.

Sementara itu, masing-masing subclass memiliki atribut tambahan sesuai jenis layanannya:

- **`LanggananStreaming`**: Menyimpan informasi kualitas resolusi dan jumlah layar yang dapat digunakan secara bersamaan.
- **`LanggananProduktivitas`**: Menyimpan informasi kapasitas penyimpanan dan jumlah pengguna yang mendapatkan lisensi.

Kedua subclass menggunakan keyword `extends` untuk mewarisi kelas `Langganan`. Konstruktor subclass juga memanggil `super(...)` untuk menginisialisasi atribut yang berasal dari kelas induk.

Dengan penerapan ini, atribut umum tidak perlu ditulis berulang kali pada setiap subclass, sedangkan informasi khusus tetap dapat dikelola sesuai jenis langganannya.

---

## 5. Penerapan Polymorphism dan Abstraction

### A. Polymorphism

Polymorphism atau polimorfisme memungkinkan method dengan nama yang sama memberikan hasil berbeda sesuai objek atau parameter yang digunakan. Pada program ini, polimorfisme diterapkan melalui method overriding dan overloading.

#### 1. Method Overriding

Overriding dilakukan ketika subclass memberikan implementasi sendiri terhadap method yang berasal dari superclass. Pada program ini, setiap jenis langganan memiliki cara perhitungan biaya tahunan yang berbeda.

Contohnya, `LanggananStreaming` memberikan diskon tahunan sebesar 5%, sedangkan `LanggananProduktivitas` memberikan diskon sebesar 10%.

Perhitungannya adalah sebagai berikut:

- Streaming:

  \[
  \text{Biaya Tahunan}
  = \text{Harga Bulanan} \times 12 \times 0{,}95
  \]

- Produktivitas:

  \[
  \text{Biaya Tahunan}
  = \text{Harga Bulanan} \times 12 \times 0{,}90
  \]

Selain `hitungBiayaTahunan()`, overriding juga digunakan pada method `getTipeLangganan()`, `tampilkanDetail()`, dan `hitungEstimasiBiaya(int bulan)` sesuai implementasi kelas masing-masing.

Contoh pemanggilannya pada Controller:

```java
for (Langganan sub : listLangganan) {
    double tahunan = sub.hitungBiayaTahunan();
}
```

Walaupun variabel `sub` menggunakan tipe `Langganan`, method yang dijalankan akan mengikuti jenis objek sebenarnya. Jika objeknya adalah `LanggananStreaming`, perhitungan mengikuti aturan streaming. Jika objeknya `LanggananProduktivitas`, perhitungan mengikuti aturan produktivitas.

#### 2. Method Overloading

Overloading terjadi ketika satu kelas memiliki beberapa method dengan nama sama, tetapi parameter yang digunakan berbeda.

Pada program ini, contohnya adalah:

- `tampilkanDetail()` digunakan untuk menampilkan informasi langganan secara lengkap.
- `tampilkanDetail(boolean ringkas)` digunakan untuk menampilkan informasi dengan format ringkas sesuai nilai parameter.
- `hitungEstimasiBiaya(int bulan)` digunakan untuk menghitung biaya berdasarkan jumlah bulan.
- `hitungEstimasiBiaya(int bulan, double diskonPersen)` digunakan untuk menghitung biaya dengan tambahan diskon persentase.

Dengan overloading, program dapat menyediakan beberapa cara untuk menjalankan fungsi yang sama tanpa harus menggunakan nama method yang berbeda.

### B. Abstraction

Abstraction atau abstraksi digunakan untuk menentukan bagian umum yang harus dimiliki suatu kelas tanpa harus menuliskan seluruh detail implementasinya di kelas induk.

Pada program ini, abstraksi diterapkan melalui abstract class `Langganan` dan beberapa abstract method di dalamnya.

**Abstract class `Langganan`**

Kelas ini dideklarasikan menggunakan `public abstract class Langganan`. Karena merupakan abstract class, objek `Langganan` tidak dapat dibuat secara langsung menggunakan `new Langganan(...)`.

Sebaliknya, objek dibuat melalui kelas turunannya, yaitu `LanggananStreaming` dan `LanggananProduktivitas`.

**Abstract method**

Kelas `Langganan` mendefinisikan method yang harus diimplementasikan oleh subclass, seperti:

```java
public abstract String getTipeLangganan();

public abstract double hitungBiayaTahunan();
```

Method `getTipeLangganan()` digunakan untuk mendapatkan jenis langganan, sedangkan `hitungBiayaTahunan()` digunakan untuk menghitung biaya selama satu tahun.

Detail implementasinya diserahkan kepada masing-masing subclass karena setiap jenis langganan memiliki aturan perhitungan yang berbeda.

### C. Keyword `final`

Keyword `final` digunakan untuk menetapkan nilai yang tidak dapat diubah setelah ditentukan. Pada kelas `Langganan`, keyword ini digunakan pada konstanta berikut:

```java
protected static final int BATAS_BULAN_DISKON = 12;
```

Masing-masing keyword memiliki fungsi berbeda:

- `protected` memungkinkan kelas turunan mengakses konstanta tersebut.
- `static` membuat konstanta menjadi bagian dari kelas, bukan milik masing-masing objek.
- `final` mencegah nilai konstanta diubah setelah diinisialisasi.

Konstanta ini digunakan untuk menentukan batas durasi langganan ketika aturan diskon mulai berlaku, yaitu 12 bulan.

---

## 6. Penerapan JDBC

JDBC (Java Database Connectivity) digunakan untuk menghubungkan program Java dengan database MySQL. Pada Mini Project 4, JDBC digunakan untuk menjalankan operasi database, mulai dari membaca data hingga menambahkan, mengubah, dan menghapus langganan.

### A. Koneksi ke Database

Pengaturan koneksi database ditempatkan pada kelas `Koneksi.java` agar kelas lain tidak perlu menuliskan konfigurasi koneksi berulang kali.

Contoh kode:

```java
public final class Koneksi {

    private static final String URL =
        "jdbc:mysql://localhost:3306/personal_subcrition_tracker?serverTimezone=Asia/Makassar";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

Pada kode tersebut, `URL` menunjukkan alamat database MySQL yang digunakan. `USER` dan `PASSWORD` berisi informasi akun database, sedangkan `getConnection()` digunakan untuk membuat koneksi.

Dengan memusatkan pengaturan koneksi di satu kelas, perubahan konfigurasi database dapat dilakukan tanpa harus mengubah setiap kelas yang mengakses database.

### B. PreparedStatement dan SQL Injection

Program menggunakan `PreparedStatement` untuk menjalankan query SQL yang membutuhkan parameter dari input pengguna.

Sebagai contoh, ketika program ingin mencari langganan berdasarkan ID, query yang digunakan dapat ditulis seperti berikut:

```sql
SELECT *
FROM langganan
WHERE id_subscription = ?
```

Tanda `?` digunakan sebagai tempat untuk mengisi parameter. Nilainya kemudian diberikan melalui method seperti `setString()`.

Contoh operasi SQL yang digunakan dalam program:

**Menampilkan seluruh data:**

```sql
SELECT l.*, ly.nama_layanan, ly.kategori,
       m.nama_metode, m.jenis
FROM langganan l
JOIN layanan ly ON ly.id_layanan = l.id_layanan
JOIN metode_pembayaran m ON m.id_metode = l.id_metode
```

**Mencari data berdasarkan ID:**

```sql
SELECT *
FROM langganan
WHERE id_subscription = ?
```

**Menambahkan data:**

```sql
INSERT INTO langganan
(id_subscription, id_layanan, harga_bulanan,
 id_metode, tanggal_tagihan, status, tipe, detail, jumlah)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
```

**Mengubah data:**

```sql
UPDATE langganan
SET id_metode = ?, harga_bulanan = ?,
    tanggal_tagihan = ?, status = ?,
    detail = ?, jumlah = ?
WHERE id_subscription = ?
```

**Menghapus data:**

```sql
DELETE FROM langganan
WHERE id_subscription = ?
```

Penggunaan `PreparedStatement` membantu mencegah SQL Injection karena nilai input diperlakukan sebagai parameter, bukan langsung digabungkan menjadi bagian dari perintah SQL.

Selain itu, program menggunakan `try-with-resources` untuk membantu menutup `Connection`, `PreparedStatement`, dan `ResultSet` secara otomatis setelah selesai digunakan.

### C. Data Access Object (DAO)

DAO (Data Access Object) merupakan pola yang digunakan untuk memisahkan proses pengelolaan database dari bagian program lainnya. Dengan pola ini, Controller tidak perlu menuliskan query SQL secara langsung setiap kali ingin mengakses data.

Pada program ini, DAO terdiri dari tiga bagian.

**1. `LanggananDAO.java`**

Interface ini menentukan method yang tersedia untuk mengelola data langganan.

```java
public interface LanggananDAO {

    List<Langganan> findAll() throws SQLException;

    Langganan findById(String id) throws SQLException;

    void save(Langganan langganan) throws SQLException;

    void update(Langganan langganan) throws SQLException;

    void delete(String id) throws SQLException;
}
```

Setiap method memiliki fungsi berbeda: `findAll()` untuk mengambil seluruh data, `findById()` untuk mencari berdasarkan ID, `save()` untuk menambahkan data, `update()` untuk mengubah data, dan `delete()` untuk menghapus data.

**2. `LanggananDAOImpl.java`**

Kelas ini mengimplementasikan method yang sudah ditentukan di interface `LanggananDAO`. Prosesnya menggunakan JDBC dan `PreparedStatement` untuk menjalankan query ke database MySQL.

**3. `LanggananMapper.java`**

Kelas ini digunakan untuk mengubah hasil query berupa `ResultSet` menjadi objek Java. Program menentukan apakah data perlu dibuat menjadi objek `LanggananStreaming` atau `LanggananProduktivitas` berdasarkan nilai kolom `tipe`.

Pemisahan ini membuat setiap kelas memiliki tugas yang lebih jelas. Interface menentukan operasi yang tersedia, implementasi DAO menjalankan query, sedangkan mapper mengubah hasil query menjadi objek model.

### D. Data Awal dari File SQL

Delapan data langganan awal dimasukkan langsung melalui file SQL, sesuai ketentuan mini project. Dengan demikian, program tidak perlu membuat data awal melalui kode Java.

File yang digunakan adalah:

`database/personal_subcrition_tracker.sql`

File tersebut berisi perintah untuk membuat database `personal_subcrition_tracker`, membuat tabel `layanan`, `metode_pembayaran`, dan `langganan`, serta mengatur relasi antartabel menggunakan foreign key.

File SQL tersebut juga berisi delapan data langganan awal, yaitu Netflix, Spotify, Disney+, Claude Code, GitHub Copilot, Amazon AWS, VPS Hosting, dan Docker.

Setelah file SQL dijalankan di MySQL, data dapat dibaca oleh aplikasi melalui method `langgananDAO.findAll()`. Pengguna bisa langsung melihat data tersebut melalui menu tampilan langganan tanpa harus memasukkannya satu per satu.

---

## 7. Nilai Tambah Program

Selain memenuhi kebutuhan utama mini project, program ini juga memiliki beberapa fitur tambahan, yaitu penggunaan Hibernate ORM, interface untuk perhitungan diskon, dan pembuatan ID otomatis untuk beberapa tabel.

### A. Hibernate ORM dan Jakarta Persistence

Hibernate ORM digunakan sebagai framework tambahan untuk menghubungkan objek Java dengan tabel di database. Dengan ORM, data dari tabel dapat direpresentasikan sebagai objek Java sehingga proses membaca dan mengelola data dapat dilakukan menggunakan model entitas.

Pada proyek ini, Hibernate digunakan sebagai nilai tambah dan pengujian ORM. Operasi CRUD utama aplikasi tetap menggunakan JDBC melalui DAO.

Beberapa bagian yang berkaitan dengan Hibernate adalah:

**1. File `hibernate.cfg.xml`**

File ini berisi pengaturan koneksi Hibernate ke MySQL, dialect database, dan daftar kelas entitas yang digunakan.

**2. Anotasi Jakarta Persistence**

Kelas model yang dipetakan sebagai entitas menggunakan anotasi seperti `@Entity`, `@Table`, `@Id`, dan `@Column`.

Contohnya terdapat pada:

- `Layanan.java`, yang dipetakan ke tabel `layanan`.
- `MetodePembayaran.java`, yang dipetakan ke tabel `metode_pembayaran`.
- `Langganan.java`, yang digunakan sebagai kelas entitas langganan.

Anotasi tersebut membantu Hibernate mengenali hubungan antara atribut pada kelas Java dan kolom pada tabel database.

**3. Kelas `HibernateUtil.java`**

Kelas ini mengatur pembuatan dan penggunaan `SessionFactory`, yang digunakan untuk membuka sesi Hibernate ketika aplikasi perlu menjalankan operasi ORM.

**4. Kelas `TesORM.java`**

Kelas ini digunakan untuk menguji apakah Hibernate dapat membaca data dari database melalui query HQL (Hibernate Query Language).

Contoh query:

```java
try (Session session = sf.openSession()) {

    List<Layanan> list = session
        .createQuery("from Layanan", Layanan.class)
        .list();

    for (Layanan l : list) {
        System.out.println(
            "- [" + l.getIdLayanan() + "] " + l.getNamaLayanan()
        );
    }
}
```

Query `from Layanan` mengambil data berdasarkan nama entitas Java, bukan menuliskan query SQL secara langsung terhadap nama tabel. Hibernate kemudian menangani proses pemetaan hasil query menjadi objek `Layanan`.

Pengujian ini digunakan untuk menunjukkan bahwa Hibernate dapat membaca entitas dari database MySQL. Penggunaan Hibernate tersebut menjadi pelengkap bagi implementasi JDBC yang digunakan pada fitur utama aplikasi.

### B. Interface `DapatDidiskon`

Interface `DapatDidiskon` digunakan untuk menentukan method yang harus dimiliki oleh kelas yang mendukung perhitungan diskon.

```java
public interface DapatDidiskon {

    double hitungDiskon(int bulan);
}
```

Interface ini diimplementasikan oleh `LanggananStreaming` dan `LanggananProduktivitas`. Keduanya menggunakan method `hitungDiskon(int bulan)`, tetapi nilai diskonnya berbeda sesuai kategori langganan.

- `LanggananStreaming` memberikan diskon 5% jika durasi mencapai minimal 12 bulan.
- `LanggananProduktivitas` memberikan diskon 10% jika durasi mencapai minimal 12 bulan.

Interface ini juga memungkinkan program memeriksa kemampuan suatu objek menggunakan `instanceof DapatDidiskon` sebelum memanggil method diskon.

Dengan cara ini, aturan diskon dapat digunakan oleh beberapa jenis langganan tanpa harus menjadikan interface tersebut sebagai kelas induk.

### C. Pembuatan ID Otomatis

Program menyediakan fitur pembuatan ID otomatis agar pengguna tidak perlu mengetik primary key secara manual ketika menambahkan data baru.

Terdapat tiga method yang menangani pembuatan ID:

- `generateIdOtomatis()`: Membuat ID langganan, misalnya `SUB09`, `SUB10`, dan seterusnya.
- `generateIdLayananOtomatis()`: Membuat ID layanan, misalnya `LYN09`, `LYN10`, dan seterusnya.
- `generateIdMetodeOtomatis()`: Membuat ID metode pembayaran, misalnya `PAY04`, `PAY05`, dan seterusnya.

Method tersebut menentukan nomor ID berikutnya berdasarkan nomor terbesar yang sudah tersimpan di sistem. Dengan demikian, ID baru dapat dibuat mengikuti pola penamaan yang digunakan program.

Fitur ini memudahkan proses penambahan data dan mengurangi risiko kesalahan ketika pengguna harus memasukkan ID secara manual.
