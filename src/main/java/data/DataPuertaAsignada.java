package data;

import java.util.LinkedList;

import entities.Monster;
import entities.Puesto;
import entities.PuestoAsignado;
import entities.Puerta;
import entities.PuertaAsignada;

import java.sql.*;

public class DataPuertaAsignada {

	private static final String SELECT_BASE =
		"select da.id, da.id_puesto_asignado, da.id_puerta, "
		+ "pa.fecha, pa.id_monster, pa.id_puesto, "
		+ "m.nombre as monster_nombre, "
		+ "p.nombre as puesto_nombre, p.descripcion as puesto_descripcion, "
		+ "d.numero as puerta_numero "
		+ "from puerta_asignada da "
		+ "inner join puesto_asignado pa on pa.id = da.id_puesto_asignado "
		+ "inner join monster m on m.id = pa.id_monster "
		+ "inner join puesto p on p.id = pa.id_puesto "
		+ "inner join puerta d on d.id = da.id_puerta ";

	private PuertaAsignada Map(ResultSet rs) throws SQLException {
		PuertaAsignada da = new PuertaAsignada();
		da.setId(rs.getInt("id"));

		Monster monster = new Monster();
		monster.setId(rs.getInt("id_monster"));
		monster.setNombre(rs.getString("monster_nombre"));

		Puesto puesto = new Puesto();
		puesto.setId(rs.getInt("id_puesto"));
		puesto.setNombre(rs.getString("puesto_nombre"));
		puesto.setDescripcion(rs.getString("puesto_descripcion"));

		PuestoAsignado pa = new PuestoAsignado();
		pa.setId(rs.getInt("id_puesto_asignado"));
		pa.setFecha(rs.getDate("fecha"));
		pa.setMonster(monster);
		pa.setPuesto(puesto);
		da.setPuestoAsignado(pa);

		Puerta puerta = new Puerta();
		puerta.setId(rs.getInt("id_puerta"));
		puerta.setNumero(rs.getString("puerta_numero"));
		da.setPuerta(puerta);

		return da;
	}

	public LinkedList<PuertaAsignada> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<PuertaAsignada> puertas = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery(SELECT_BASE + "order by pa.fecha desc, m.nombre asc, d.numero asc");

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
	
	public LinkedList<PuertaAsignada> GetByPuestoAsignado(int idPuestoAsignado) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		LinkedList<PuertaAsignada> puertas = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				SELECT_BASE + "where da.id_puesto_asignado = ? order by d.numero asc");
			pstmt.setInt(1, idPuestoAsignado);
			rs = pstmt.executeQuery();

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
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return puertas;
	}
	
	public PuertaAsignada GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		PuertaAsignada puerta = new PuertaAsignada();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(SELECT_BASE + "where da.id = ?");
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
	
	public boolean Exists(int idPuestoAsignado, int idPuerta, int excludeId) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		boolean exists = false;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"select count(*) as total from puerta_asignada where id_puesto_asignado = ? and id_puerta = ? and id <> ?");
			pstmt.setInt(1, idPuestoAsignado);
			pstmt.setInt(2, idPuerta);
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
	
	public void Add(PuertaAsignada da) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"insert into puerta_asignada (id_puesto_asignado, id_puerta) values (?, ?)");
			pstmt.setInt(1, da.getPuestoAsignado().getId());
			pstmt.setInt(2, da.getPuerta().getId());
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
	
	public void Update(PuertaAsignada da) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"update puerta_asignada set id_puesto_asignado = ?, id_puerta = ? where id = ?");
			pstmt.setInt(1, da.getPuestoAsignado().getId());
			pstmt.setInt(2, da.getPuerta().getId());
			pstmt.setInt(3, da.getId());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from puerta_asignada where id = ?");
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
