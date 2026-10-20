package data;

import java.util.LinkedList;

import entities.Puerta;

import java.sql.*;

public class DataPuerta {

	private Puerta Map(ResultSet rs) throws SQLException {
		Puerta p = new Puerta();
		p.setId(rs.getInt("id"));
		p.setNumero(rs.getString("numero"));
		return p;
	}

	public LinkedList<Puerta> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Puerta> puertas = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from puerta order by numero");

			if (rs!=null) {
				while(rs.next()) {
					puertas.add(Map(rs));
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return puertas;
	}
	
	public Puerta GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Puerta puerta = new Puerta();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from puerta where id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					puerta = Map(rs);
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return puerta;
	}
	
	public void Add(Puerta p) {
		PreparedStatement pstmt = null;
		ResultSet keyResultSet = null;
		try {
			String query = "INSERT INTO puerta (numero) VALUES (?)";
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, p.getNumero());
			pstmt.executeUpdate();
			keyResultSet = pstmt.getGeneratedKeys();
			if (keyResultSet != null && keyResultSet.next()) {
				p.setId(keyResultSet.getInt(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(keyResultSet != null) { keyResultSet.close(); }
				if(pstmt != null) { pstmt.close(); }
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Update(Puerta p) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update puerta set numero = ? where id = ?");
			pstmt.setString(1, p.getNumero());
			pstmt.setInt(2, p.getId());
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public LinkedList<Integer> getMiedosAsignados(int idPuerta) {
        LinkedList<Integer> miedos = new LinkedList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id_miedo FROM puerta_miedo WHERE id_puerta=?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            stmt.setInt(1, idPuerta);
            rs = stmt.executeQuery();
            while(rs.next()) {
                miedos.add(rs.getInt("id_miedo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { }
        }
        return miedos;
    }

	public void actualizarMiedos(int idPuerta, String[] idMiedos) {
		PreparedStatement stmt = null;
		try {
			String delete = "DELETE FROM puerta_miedo WHERE id_puerta=?";
			stmt = DbConnector.getInstancia().getConn().prepareStatement(delete);
			stmt.setInt(1, idPuerta);
			stmt.executeUpdate();
			stmt.close();
			if (idMiedos != null && idMiedos.length > 0) {
				String insert = "INSERT INTO puerta_miedo (id_puerta, id_miedo) VALUES (?, ?)";
				stmt = DbConnector.getInstancia().getConn().prepareStatement(insert);
				for (String idMiedo : idMiedos) {
					stmt.setInt(1, idPuerta);
					stmt.setInt(2, Integer.parseInt(idMiedo));
					stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch(Exception e) {}
		}
	}
	public java.util.HashMap<Integer, LinkedList<Puerta>> getTodasLasPuertasCompatibles() {
		java.util.HashMap<Integer, LinkedList<Puerta>> mapa = new java.util.HashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			String query = "SELECT DISTINCT mm.id_monster, p.id, p.numero " +
		               "FROM puerta p " +
		               "INNER JOIN puerta_miedo pm ON p.id = pm.id_puerta " +
		               "INNER JOIN monster_miedo mm ON pm.id_miedo = mm.id_miedo";
			
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query);
			rs = pstmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					int idMonster = rs.getInt("id_monster");
					Puerta p = new Puerta();
					p.setId(rs.getInt("id"));
					p.setNumero(rs.getString("numero"));
					mapa.putIfAbsent(idMonster, new LinkedList<>());
					mapa.get(idMonster).add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); if(pstmt!=null) pstmt.close(); DbConnector.getInstancia().releaseConn(); } catch (SQLException e) { }
		}
		return mapa;
	}
	public void Delete(int id) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from puerta where id = ?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
