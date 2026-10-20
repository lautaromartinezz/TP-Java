package data;
import dto.AsignacionMonstruoDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import entities.Monster;
import entities.Puesto;
import entities.PuestoAsignado;
import entities.Puerta;
import entities.PuertaAsignada;
import entities.Resultado;

import java.sql.*;

public class DataResultado {

	private static final String SELECT_BASE =
		"select r.id, r.fecha, r.energia_generada, r.nombre_nino, r.id_puerta_asignada, "
		+ "pa.fecha as pa_fecha, pa.id_monster, pa.id_puesto, da.id_puesto_asignado, "
		+ "m.nombre as monster_nombre, "
		+ "p.nombre as puesto_nombre, p.descripcion as puesto_descripcion, "
		+ "d.id as id_puerta, d.numero as puerta_numero "
		+ "from resultado r "
		+ "inner join puerta_asignada da on da.id = r.id_puerta_asignada "
		+ "inner join puesto_asignado pa on pa.id = da.id_puesto_asignado "
		+ "inner join monster m on m.id = pa.id_monster "
		+ "inner join puesto p on p.id = pa.id_puesto "
		+ "inner join puerta d on d.id = da.id_puerta ";

	private Resultado Map(ResultSet rs) throws SQLException {
		Resultado r = new Resultado();
		r.setId(rs.getInt("id"));
		r.setFecha(rs.getTimestamp("fecha"));
		r.setEnergiaGenerada(rs.getInt("energia_generada"));
		r.setNombreNino(rs.getString("nombre_nino"));

		Monster monster = new Monster();
		monster.setId(rs.getInt("id_monster"));
		monster.setNombre(rs.getString("monster_nombre"));

		Puesto puesto = new Puesto();
		puesto.setId(rs.getInt("id_puesto"));
		puesto.setNombre(rs.getString("puesto_nombre"));
		puesto.setDescripcion(rs.getString("puesto_descripcion"));

		PuestoAsignado pa = new PuestoAsignado();
		pa.setId(rs.getInt("id_puesto_asignado"));
		pa.setFecha(rs.getDate("pa_fecha"));
		pa.setMonster(monster);
		pa.setPuesto(puesto);

		Puerta puerta = new Puerta();
		puerta.setId(rs.getInt("id_puerta"));
		puerta.setNumero(rs.getString("puerta_numero"));

		PuertaAsignada da = new PuertaAsignada();
		da.setId(rs.getInt("id_puerta_asignada"));
		da.setPuestoAsignado(pa);
		da.setPuerta(puerta);
		r.setPuertaAsignada(da);

		return r;
	}
	public List<AsignacionMonstruoDTO> getTop3Historial(int idMonster) {
	    List<AsignacionMonstruoDTO> lista = new ArrayList<>();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        String query = "SELECT r.id AS idResultado, p.numero AS numeroPuerta, " +
	                       "pu.nombre AS nombrePuesto, r.energia_generada, r.fecha " +
	                       "FROM resultado r " +
	                       "INNER JOIN puerta_asignada pa ON r.id_puerta_asignada = pa.id " +
	                       "INNER JOIN puerta p ON pa.id_puerta = p.id " +
	                       "INNER JOIN puesto_asignado pas ON pa.id_puesto_asignado = pas.id " +
	                       "INNER JOIN puesto pu ON pas.id_puesto = pu.id " +
	                       "WHERE pas.id_monster = ? " +
	                       "ORDER BY r.fecha DESC LIMIT 3";
	        stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
	        stmt.setInt(1, idMonster);
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            AsignacionMonstruoDTO dto = new AsignacionMonstruoDTO();
	            dto.setIdAsignacion(rs.getInt("idResultado"));
	            dto.setNumeroPuerta(rs.getString("numeroPuerta"));
	            dto.setNombrePuesto(rs.getString("nombrePuesto"));
	            dto.setEnergiaGenerada(rs.getInt("energia_generada"));
	            dto.setFecha(rs.getTimestamp("fecha")); 
	            dto.setEstado("Completado");
	            lista.add(dto);
	        }
	    } catch (Exception e) { e.printStackTrace(); } 
	    finally {
	        try { if(rs!=null) rs.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (Exception e) {}
	    }
	    return lista;
	}
	public List<AsignacionMonstruoDTO> getHistorialSustos(int idMonster) {
	    List<AsignacionMonstruoDTO> lista = new ArrayList<>();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        String query = "SELECT r.id AS idResultado, p.numero AS numeroPuerta, " +
	                       "pu.nombre AS nombrePuesto, r.energia_generada, r.fecha " +
	                       "FROM resultado r " +
	                       "INNER JOIN puerta_asignada pa ON r.id_puerta_asignada = pa.id " +
	                       "INNER JOIN puerta p ON pa.id_puerta = p.id " +
	                       "INNER JOIN puesto_asignado pas ON pa.id_puesto_asignado = pas.id " +
	                       "INNER JOIN puesto pu ON pas.id_puesto = pu.id " +
	                       "WHERE pas.id_monster = ? " +
	                       "ORDER BY r.fecha DESC";
	        stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
	        stmt.setInt(1, idMonster);
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            AsignacionMonstruoDTO dto = new AsignacionMonstruoDTO();
	            dto.setIdAsignacion(rs.getInt("idResultado"));
	            dto.setNumeroPuerta(rs.getString("numeroPuerta"));
	            dto.setNombrePuesto(rs.getString("nombrePuesto"));
	            dto.setEnergiaGenerada(rs.getInt("energia_generada"));
	            dto.setFecha(rs.getTimestamp("fecha")); 
	            dto.setEstado("Completado");
	            lista.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if(rs!=null) rs.close(); if(stmt!=null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (Exception e) {}
	    }
	    return lista;
	}

	public LinkedList<Resultado> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Resultado> resultados = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery(SELECT_BASE + "order by r.fecha desc");

			if (rs!=null) {
				while(rs.next()) {
					resultados.add(Map(rs));
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
		return resultados;
	}
	
	public Resultado GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Resultado resultado = new Resultado();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(SELECT_BASE + "where r.id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					resultado = Map(rs);
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
		return resultado;
	}
	public int sumarEnergiaTotal(int idMonster) {
	    int total = 0;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        String query = "SELECT SUM(r.energia_generada) AS total FROM resultado r " +
	                       "INNER JOIN puerta_asignada pa ON r.id_puerta_asignada = pa.id " +
	                       "INNER JOIN puesto_asignado pas ON pa.id_puesto_asignado = pas.id " +
	                       "WHERE pas.id_monster = ?";
	        stmt = DbConnector.getInstancia().getConn().prepareStatement(query);
	        stmt.setInt(1, idMonster);
	        rs = stmt.executeQuery();
	        if (rs.next()) { total = rs.getInt("total"); }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); if (stmt != null) stmt.close(); DbConnector.getInstancia().releaseConn(); } catch (Exception e) { e.printStackTrace(); }
	    }
	    return total;
	}
	public void Add(Resultado r) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"insert into resultado (id_puerta_asignada, fecha, energia_generada, nombre_nino) values (?, ?, ?, ?)");
			pstmt.setInt(1, r.getPuertaAsignada().getId());
			pstmt.setTimestamp(2, new Timestamp(r.getFecha().getTime()));
			pstmt.setInt(3, r.getEnergiaGenerada());
			pstmt.setString(4, r.getNombreNino());
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
	
	public void Update(Resultado r) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"update resultado set id_puerta_asignada = ?, fecha = ?, energia_generada = ?, nombre_nino = ? where id = ?");
			pstmt.setInt(1, r.getPuertaAsignada().getId());
			pstmt.setTimestamp(2, new Timestamp(r.getFecha().getTime()));
			pstmt.setInt(3, r.getEnergiaGenerada());
			pstmt.setString(4, r.getNombreNino());
			pstmt.setInt(5, r.getId());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from resultado where id = ?");
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
