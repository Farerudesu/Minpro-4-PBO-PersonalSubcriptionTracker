package com.pbo.fareru.minpro.pbo.dao;

import com.pbo.fareru.minpro.pbo.model.Langganan;
import java.sql.SQLException;
import java.util.List;

public interface LanggananDAO {
    List<Langganan> findAll() throws SQLException;
    Langganan findById(String id) throws SQLException;
    void save(Langganan langganan) throws SQLException;
    void update(Langganan langganan) throws SQLException;
    void delete(String id) throws SQLException;
}
