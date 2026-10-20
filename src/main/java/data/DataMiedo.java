package data;

import entities.Miedo;
import java.sql.*;
import java.util.LinkedList;

public class DataMiedo {

    public void Add(Miedo m) {
        PreparedStatement stmt = null;
        ResultSet keyResultSet = null;
        try {
            String query = "INSERT INTO miedo (nombre) VALUES (?)";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, m.getNombre());
            stmt.executeUpdate();
            
            keyResultSet = stmt.getGeneratedKeys();
            if (keyResultSet != null && keyResultSet.next()) {
                m.setId(keyResultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(keyResultSet!=null) keyResultSet.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public LinkedList<Miedo> GetAll() {
        LinkedList<Miedo> miedos = new LinkedList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id, nombre FROM miedo ORDER BY nombre ASC";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Miedo m = new Miedo();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                miedos.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return miedos;
    }

    public Miedo GetOne(int id) {
        Miedo m = new Miedo();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id, nombre FROM miedo WHERE id = ?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return m;
    }

    public void Update(Miedo m) {
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE miedo SET nombre=? WHERE id=?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            
            stmt.setString(1, m.getNombre());
            stmt.setInt(2, m.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void Delete(int id) {
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM miedo WHERE id=?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}