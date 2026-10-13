package com.pbo.fareru.minpro.pbo.config;

import com.pbo.fareru.minpro.pbo.model.Layanan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public final class TesORM {
    private TesORM() { }

    public static void main(String[] args) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        if (sf != null) {
            try (Session session = sf.openSession()) {
                List<Layanan> list = session.createQuery("from Layanan", Layanan.class).list();
                System.out.println("Berhasil membaca entitas via Hibernate ORM (" + list.size() + " data):");
                for (Layanan l : list) {
                    System.out.println("- [" + l.getIdLayanan() + "] " + l.getNamaLayanan() + " (" + l.getKategori() + ")");
                }
            } catch (Exception e) {
                System.err.println("Query Hibernate: " + e.getMessage());
            } finally {
                HibernateUtil.shutdown();
                try {
                    com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
                } catch (Exception ignored) { }
            }
        }
    }
}
