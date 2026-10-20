package data;

import java.util.LinkedList;

import entities.Monster;

import java.sql.*;

public class DataMonster {
	
	
	public LinkedList<Monster> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Monster> mons = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from monster");

			if (rs!=null) {
				while(rs.next()) {
					Monster m = new Monster();
					m.setId(rs.getInt("id"));
					m.setNombre(rs.getString("nombre"));
					
					mons.add(m);
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
		return mons;
	}
	
	public LinkedList<Monster> GetRanking() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Monster> mons = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery(
				"select m.id, m.nombre, coalesce(sum(r.energia_generada), 0) as energia_total "
				+ "from monster m "
				+ "left join puesto_asignado pa on pa.id_monster = m.id "
				+ "left join puerta_asignada da on da.id_puesto_asignado = pa.id "
				+ "left join resultado r on r.id_puerta_asignada = da.id "
				+ "group by m.id, m.nombre "
				+ "order by energia_total desc, m.nombre asc");

			if (rs!=null) {
				while(rs.next()) {
					Monster m = new Monster();
					m.setId(rs.getInt("id"));
					m.setNombre(rs.getString("nombre"));
					m.setEnergiaTotal(rs.getInt("energia_total"));
					
					mons.add(m);
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
		return mons;
	}
	
	public Monster GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Monster mon = new Monster();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from monster where id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					Monster m = new Monster();
					m.setId(rs.getInt("id"));
					m.setNombre(rs.getString("nombre"));
					m.setLegajo(rs.getInt("legajo"));
					m.setPassword(rs.getString("password"));
					m.setRol(rs.getString("rol"));
					
					mon = m;
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
		return mon;
	}
	
	public void Add(Monster m) {
		PreparedStatement pstmtInsert = null;
		PreparedStatement pstmtUpdate = null;
		ResultSet keyResultSet = null;
		
		try {
			String insertQuery = "INSERT INTO monster (nombre, legajo, password, rol) VALUES (?, 0, ?, ?)";
			pstmtInsert = DbConnector.getInstancia().getConn().prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmtInsert.setString(1, m.getNombre());
			pstmtInsert.setString(2, m.getPassword());
			pstmtInsert.setString(3, m.getRol());
			
			pstmtInsert.executeUpdate();

			keyResultSet = pstmtInsert.getGeneratedKeys();
			if (keyResultSet != null && keyResultSet.next()) {
				int nuevoId = keyResultSet.getInt(1);
				m.setId(nuevoId);
				int legajoReal = nuevoId * 1000; 
				m.setLegajo(legajoReal);
				
				String updateQuery = "UPDATE monster SET legajo = ? WHERE id = ?";
				pstmtUpdate = DbConnector.getInstancia().getConn().prepareStatement(updateQuery);
				pstmtUpdate.setInt(1, legajoReal);
				pstmtUpdate.setInt(2, nuevoId);
				pstmtUpdate.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(keyResultSet != null) { keyResultSet.close(); }
				if(pstmtInsert != null) { pstmtInsert.close(); }
				if(pstmtUpdate != null) { pstmtUpdate.close(); }
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public void Update(Monster m) {
		PreparedStatement pstmt=null;
		try {

			String query = "UPDATE monster SET nombre = ?, password = ?, rol = ? WHERE id = ?";
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query);
			
			pstmt.setString(1, m.getNombre());
			pstmt.setString(2, m.getPassword());
			pstmt.setString(3, m.getRol());
			pstmt.setInt(4, m.getId());
			
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
	public Monster login(int legajo, String password) {
	    Monster m = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        String query = "SELECT * FROM monster WHERE legajo = ? AND password = ?";
	        stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
	        stmt.setInt(1, legajo);
	        stmt.setString(2, password);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            m = new Monster();
	            m.setId(rs.getInt("id"));
	            m.setLegajo(rs.getInt("legajo"));
	            m.setNombre(rs.getString("nombre"));
	            m.setRol(rs.getString("rol")); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            DbConnector.getInstancia().releaseConn();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return m;
	}
	public void Delete(int id) {
		PreparedStatement pstmtResultados = null;
		PreparedStatement pstmtPuertas = null;
		PreparedStatement pstmtPuestos = null;
		PreparedStatement pstmtMiedos = null;
		PreparedStatement pstmtMonster = null;
		
		try {
			String qResultados = "DELETE FROM resultado WHERE id_puerta_asignada IN (SELECT id FROM puerta_asignada WHERE id_puesto_asignado IN (SELECT id FROM puesto_asignado WHERE id_monster = ?))";
			pstmtResultados = DbConnector.getInstancia().getConn().prepareStatement(qResultados);
			pstmtResultados.setInt(1, id);
			pstmtResultados.executeUpdate();
			String qPuertas = "DELETE FROM puerta_asignada WHERE id_puesto_asignado IN (SELECT id FROM puesto_asignado WHERE id_monster = ?)";
			pstmtPuertas = DbConnector.getInstancia().getConn().prepareStatement(qPuertas);
			pstmtPuertas.setInt(1, id);
			pstmtPuertas.executeUpdate();
			String qPuestos = "DELETE FROM puesto_asignado WHERE id_monster = ?";
			pstmtPuestos = DbConnector.getInstancia().getConn().prepareStatement(qPuestos);
			pstmtPuestos.setInt(1, id);
			pstmtPuestos.executeUpdate();
			String qMiedos = "DELETE FROM monster_miedo WHERE id_monster = ?";
			pstmtMiedos = DbConnector.getInstancia().getConn().prepareStatement(qMiedos);
			pstmtMiedos.setInt(1, id);
			pstmtMiedos.executeUpdate();

			String qMonster = "DELETE FROM monster WHERE id = ?";
			pstmtMonster = DbConnector.getInstancia().getConn().prepareStatement(qMonster);
			pstmtMonster.setInt(1, id);
			pstmtMonster.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmtResultados != null) { pstmtResultados.close(); }
				if(pstmtPuertas != null) { pstmtPuertas.close(); }
				if(pstmtPuestos != null) { pstmtPuestos.close(); }
				if(pstmtMiedos != null) { pstmtMiedos.close(); }
				if(pstmtMonster != null) { pstmtMonster.close(); }
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    public LinkedList<Integer> getMiedosAsignados(int idMonster) {
        LinkedList<Integer> miedos = new LinkedList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id_miedo FROM monster_miedo WHERE id_monster=?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
            stmt.setInt(1, idMonster);
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

  
    public void actualizarMiedos(int idMonster, String[] idMiedos) {
        PreparedStatement stmt = null;
        try {
            String delete = "DELETE FROM monster_miedo WHERE id_monster=?";
            stmt = DbConnector.getInstancia().getConn().prepareStatement(delete);
            stmt.setInt(1, idMonster);
            stmt.executeUpdate();
            stmt.close();

            if (idMiedos != null && idMiedos.length > 0) {
                String insert = "INSERT INTO monster_miedo (id_monster, id_miedo) VALUES (?, ?)";
                stmt = DbConnector.getInstancia().getConn().prepareStatement(insert);
                for (String idMiedo : idMiedos) {
                    stmt.setInt(1, idMonster);
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
}
