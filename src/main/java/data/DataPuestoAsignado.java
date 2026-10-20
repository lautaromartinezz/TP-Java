package data;

import java.util.LinkedList;

import entities.Monster;
import entities.Puesto;
import entities.PuestoAsignado;

import java.sql.*;

public class DataPuestoAsignado {

	private static final String SELECT_BASE =
		"select pa.id, pa.fecha, pa.id_monster, pa.id_puesto, "
		+ "m.nombre as monster_nombre, "
		+ "p.nombre as puesto_nombre, p.descripcion as puesto_descripcion "
		+ "from puesto_asignado pa "
		+ "inner join monster m on m.id = pa.id_monster "
		+ "inner join puesto p on p.id = pa.id_puesto ";

	private PuestoAsignado Map(ResultSet rs) throws SQLException {
		PuestoAsignado pa = new PuestoAsignado();
		pa.setId(rs.getInt("id"));
		pa.setFecha(rs.getDate("fecha"));

		Monster monster = new Monster();
		monster.setId(rs.getInt("id_monster"));
		monster.setNombre(rs.getString("monster_nombre"));
		pa.setMonster(monster);

		Puesto puesto = new Puesto();
		puesto.setId(rs.getInt("id_puesto"));
		puesto.setNombre(rs.getString("puesto_nombre"));
		puesto.setDescripcion(rs.getString("puesto_descripcion"));
		pa.setPuesto(puesto);

		return pa;
	}

	public LinkedList<PuestoAsignado> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<PuestoAsignado> asignaciones = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery(SELECT_BASE + "order by pa.fecha desc, m.nombre asc");

			if (rs!=null) {
				while(rs.next()) {
					asignaciones.add(Map(rs));
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
		return asignaciones;
	}
	
	public PuestoAsignado GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		PuestoAsignado asignacion = new PuestoAsignado();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(SELECT_BASE + "where pa.id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					asignacion = Map(rs);
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
		return asignacion;
	}
	
	public boolean ExistsMonsterOnDate(int idMonster, java.util.Date fecha, int excludeId) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		boolean exists = false;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"select count(*) as total from puesto_asignado where id_monster = ? and fecha = ? and id <> ?");
			pstmt.setInt(1, idMonster);
			pstmt.setDate(2, new Date(fecha.getTime()));
			pstmt.setInt(3, excludeId);
			rs = pstmt.executeQuery();

			if (rs!=null && rs.next()) {
				exists = rs.getInt("total") > 0;
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
		return exists;
	}
	
	public void Add(PuestoAsignado pa) {
		PreparedStatement pstmt=null;
		ResultSet keyResultSet = null;
		try {
			String query = "INSERT INTO puesto_asignado (id_puesto, id_monster, fecha) VALUES (?, ?, ?)";
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, pa.getPuesto().getId());
			pstmt.setInt(2, pa.getMonster().getId());
			pstmt.setDate(3, new Date(pa.getFecha().getTime()));
			pstmt.executeUpdate();
			keyResultSet = pstmt.getGeneratedKeys();
			if (keyResultSet != null && keyResultSet.next()) {
				pa.setId(keyResultSet.getInt(1));
			}
			
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
	public java.util.HashMap<Integer, Integer> getMonstruosDelDia(java.sql.Date fecha) {
		java.util.HashMap<Integer, Integer> mapa = new java.util.HashMap<>();
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			String query = "SELECT id_puesto, id_monster FROM puesto_asignado WHERE fecha = ?";
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query);
			pstmt.setDate(1, fecha);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mapa.put(rs.getInt("id_puesto"), rs.getInt("id_monster"));
			}
		} catch (java.sql.SQLException e) { e.printStackTrace(); } 
		finally { try { if(rs!=null) rs.close(); if(pstmt!=null) pstmt.close(); DbConnector.getInstancia().releaseConn(); } catch(Exception e) {} }
		return mapa;
	}

	public java.util.HashMap<Integer, java.util.LinkedList<entities.Puerta>> getPuertasDelDia(java.sql.Date fecha) {
		java.util.HashMap<Integer, java.util.LinkedList<entities.Puerta>> mapa = new java.util.HashMap<>();
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			String query = "SELECT pa.id_puesto, p.id as id_puerta, p.numero as numero_puerta " +
						   "FROM puerta_asignada pua " +
						   "INNER JOIN puesto_asignado pa ON pua.id_puesto_asignado = pa.id " +
						   "INNER JOIN puerta p ON pua.id_puerta = p.id " +
						   "WHERE pa.fecha = ?";
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(query);
			pstmt.setDate(1, fecha);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int idPuesto = rs.getInt("id_puesto");
				entities.Puerta puerta = new entities.Puerta();
				puerta.setId(rs.getInt("id_puerta"));
				puerta.setNumero(rs.getString("numero_puerta"));
				
				if(!mapa.containsKey(idPuesto)) {
					mapa.put(idPuesto, new java.util.LinkedList<>());
				}
				mapa.get(idPuesto).add(puerta);
			}
		} catch (java.sql.SQLException e) { e.printStackTrace(); } 
		finally { try { if(rs!=null) rs.close(); if(pstmt!=null) pstmt.close(); DbConnector.getInstancia().releaseConn(); } catch(Exception e) {} }
		return mapa;
	}

	public int UpdateMonstruo(PuestoAsignado pa) {
		java.sql.Connection conn = null;
		java.sql.PreparedStatement pstmt = null;
		try {
			conn = DbConnector.getInstancia().getConn();
			java.sql.Date fechaSql = new java.sql.Date(pa.getFecha().getTime());

			String sql0 = "DELETE FROM resultado WHERE id_puerta_asignada IN (SELECT id FROM puerta_asignada WHERE id_puesto_asignado IN (SELECT id FROM puesto_asignado WHERE id_puesto = ? AND fecha = ?))";
			pstmt = conn.prepareStatement(sql0);
			pstmt.setInt(1, pa.getPuesto().getId());
			pstmt.setDate(2, fechaSql);
			pstmt.executeUpdate();
			pstmt.close();


			String sql1 = "DELETE FROM puerta_asignada WHERE id_puesto_asignado IN (SELECT id FROM puesto_asignado WHERE id_puesto = ? AND fecha = ?)";
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, pa.getPuesto().getId());
			pstmt.setDate(2, fechaSql);
			int puertasBorradas = pstmt.executeUpdate();
			pstmt.close();
			System.out.println("-> [BULLDOZER] Puertas viejas aniquiladas: " + puertasBorradas);

	
			String sql2 = "DELETE FROM puesto_asignado WHERE id_puesto = ? AND fecha = ?";
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, pa.getPuesto().getId());
			pstmt.setDate(2, fechaSql);
			int turnosBorrados = pstmt.executeUpdate();
			pstmt.close();
			System.out.println("-> [BULLDOZER] Turnos viejos aniquilados: " + turnosBorrados);

		} catch (java.sql.SQLException e) {
			System.out.println("-> [ERROR SQL]: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try { if(pstmt!=null) pstmt.close(); DbConnector.getInstancia().releaseConn(); } catch(Exception e) {}
		}
		

		return 0; 
	}
	
	public void Update(PuestoAsignado pa) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"update puesto_asignado set id_monster = ?, id_puesto = ?, fecha = ? where id = ?");
			pstmt.setInt(1, pa.getMonster().getId());
			pstmt.setInt(2, pa.getPuesto().getId());
			pstmt.setDate(3, new Date(pa.getFecha().getTime()));
			pstmt.setInt(4, pa.getId());
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
	
	public void Delete(int id) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from puesto_asignado where id = ?");
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
