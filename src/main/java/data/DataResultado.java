package data;

import java.util.LinkedList;

import entities.Monster;
import entities.Puesto;
import entities.Puerta;
import entities.Resultado;

import java.sql.*;

public class DataResultado {

	private static final String SELECT_BASE =
		"select r.id, r.fecha, r.energia_generada, r.nombre_nino, r.id_monster, r.id_puerta, "
		+ "m.nombre as monster_nombre, "
		+ "p.numero as puerta_numero, p.id_puesto, "
		+ "pu.nombre as puesto_nombre, pu.descripcion as puesto_descripcion "
		+ "from resultado r "
		+ "inner join monster m on m.id = r.id_monster "
		+ "inner join puerta p on p.id = r.id_puerta "
		+ "inner join puesto pu on pu.id = p.id_puesto ";

	private Resultado Map(ResultSet rs) throws SQLException {
		Resultado r = new Resultado();
		r.setId(rs.getInt("id"));
		r.setFecha(rs.getTimestamp("fecha"));
		r.setEnergiaGenerada(rs.getInt("energia_generada"));
		r.setNombreNino(rs.getString("nombre_nino"));

		Monster monster = new Monster();
		monster.setId(rs.getInt("id_monster"));
		monster.setNombre(rs.getString("monster_nombre"));
		r.setMonster(monster);

		Puesto puesto = new Puesto();
		puesto.setId(rs.getInt("id_puesto"));
		puesto.setNombre(rs.getString("puesto_nombre"));
		puesto.setDescripcion(rs.getString("puesto_descripcion"));

		Puerta puerta = new Puerta();
		puerta.setId(rs.getInt("id_puerta"));
		puerta.setNumero(rs.getString("puerta_numero"));
		puerta.setPuesto(puesto);
		r.setPuerta(puerta);

		return r;
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
	
	public void Add(Resultado r) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
				"insert into resultado (fecha, energia_generada, nombre_nino, id_monster, id_puerta) values (?, ?, ?, ?, ?)");
			pstmt.setTimestamp(1, new Timestamp(r.getFecha().getTime()));
			pstmt.setInt(2, r.getEnergiaGenerada());
			pstmt.setString(3, r.getNombreNino());
			pstmt.setInt(4, r.getMonster().getId());
			pstmt.setInt(5, r.getPuerta().getId());
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
				"update resultado set fecha = ?, energia_generada = ?, nombre_nino = ?, id_monster = ?, id_puerta = ? where id = ?");
			pstmt.setTimestamp(1, new Timestamp(r.getFecha().getTime()));
			pstmt.setInt(2, r.getEnergiaGenerada());
			pstmt.setString(3, r.getNombreNino());
			pstmt.setInt(4, r.getMonster().getId());
			pstmt.setInt(5, r.getPuerta().getId());
			pstmt.setInt(6, r.getId());
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
