package beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import util.common.Common;
import util.conf.Configuracion;
import util.db.DbUtil;
import util.db.MySqlConnector;


public class Materia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** Clave de la materia **/
	private String cveMat ="";
	/** Clave del nivel grado **/
	private int cveNivelGrado = 0;
	/** Nombre de la materia **/
	private String nomMat ="";
	/** Modulo **/
	private int modulo = 0;
	/** Unidad **/
	private int unidad = 0;
	/** Nombre del Nivel **/
	private String nomNivel = "";
	/** Nombre del Nivel **/
	private String nomNivelDir = "";
	/** Nombre del Nivel Grado **/
	private String nomNivelGrado = "";
	/** Nombre del Nivel Grado **/
	private String nomGrado = "";
	/** Nivel **/
	private int nivel = 0;
	/** Lista de la solicitud de la materia **/
	private List<MateriaSolicitud> listMatSol = new ArrayList<MateriaSolicitud>();
	/** Bean de MateriaSolicitud **/
	private MateriaSolicitud matSol;
	/** Estado de la solicitud para buscar materia **/
	private int estadoSolicitud = -1;
	/** permiso **/
	private int permisoContenido = -1;
	/** bandera **/
	private int banMat = -1;
	/** selected **/
	private String selected = "";
	/** fecha de modificar **/
	private Date fechaModf = null;

	
	public String getCveMat(){
		return cveMat;
	}
	public void setCveMat(String cveMat){
		this.cveMat = cveMat;
	}
	public int getNivelGrado(){
		return cveNivelGrado;
	}
	public void setNivelGrado(int nivelGrado){
		this.cveNivelGrado = nivelGrado;
	}
	public String getNomMat(){
		return nomMat;
	}
	public void setNomMat(String nomMat){
		this.nomMat = nomMat;
	}
	public int getModulo(){
		return modulo;
	}
	public void setModulo(int modulo){
		this.modulo = modulo;
	}
	public int getUnidad(){
		return unidad;
	}
	public void setUnidad(int unidad){
		this.unidad = unidad;
	}
	public String getNomNivel(){
		return nomNivel;
	}
	public void setNomNivel(String nomNivel){
		this.nomNivel = nomNivel;
	}
	public String getNomNivelDir(){
		return nomNivelDir;
	}
	public void setNomNivelDir(String nomNivelDir){
		this.nomNivelDir = nomNivelDir;
	}
	public String getNomNivelGrado(){
		return nomNivelGrado;
	}
	public void setNomNivelGrado(String nomNivelGrado){
		this.nomNivelGrado = nomNivelGrado;
	}
	public String getNomGrado(){
		return nomGrado;
	}
	public void setNomGrado(String nomGrado){
		this.nomGrado = nomGrado;
	}
	public int getNivel(){
		return nivel;
	}
	public void setNivel(int nivel){
		this.nivel = nivel;
	}
	
	public List<MateriaSolicitud> getMateriaSol(){
		return listMatSol;
	}
	public void setMateriaSol(List<MateriaSolicitud> s){
		this.listMatSol = s;
	}

	public MateriaSolicitud getMatSol(){
		return matSol;
	}
	public void setMatSol(MateriaSolicitud mt){
		this.matSol = mt;
	}
	public int getEstadoSolicitud(){
		return estadoSolicitud;
	}
	public void setEstadoSolicitud(int estadoSolicitud){
		this.estadoSolicitud = estadoSolicitud;
	}
	public int getPermisoContenido(){
		return permisoContenido;
	}
	public void setPermisoContenido(int permisoContenido){
		this.permisoContenido = permisoContenido;
	}
	public int getBanMat(){
		return banMat;
	}
	public void setBanMat(int banMat){
		this.banMat = banMat;
	}
	public String getSelected(){
		return selected;
	}
	public void setSelected(String selected){
		this.selected = selected;
	}
	public Date getFechaModf(){
		return fechaModf;
	}
	public void setFechaModf(Date fechaModf){
		this.fechaModf = fechaModf;
	}
	
	public int CountMateria(Usuario u) throws Exception{
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "select count(*) cnt "
					+ queryBuscar;

			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				query  += condicion.toString();
			}
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, u.getCveUsu());
			SetPreparedStatement(preparedStatement);
						
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				cnt = rs.getInt("cnt");
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return cnt;
	}
	
	private int SetPreparedStatement(PreparedStatement preparedStatement) throws Exception {
		int count = 1;
		try {

		if(this.cveMat != null && this.cveMat.length() > 0){
			count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveMat) + "%");
		}
		if(this.nomMat != null && this.nomMat.length() > 0){
			count++;
			preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.nomMat) + "%");
		}
		if(this.nivel > 0){
			count++;
			preparedStatement.setInt(count, this.nivel);
		}
		if(this.modulo > 0){
			count++;
			preparedStatement.setInt(count, this.modulo);
		}
		if(this.unidad > 0){
			count++;
			preparedStatement.setInt(count, this.unidad);
		}
		if(this.estadoSolicitud >= 0){
			count++;
			preparedStatement.setInt(count, this.estadoSolicitud);
		}

		} catch (SQLException e) {
			throw e;
		}
		return count;

	}

	private String CreateCondition(Connection con) {
		StringBuilder condicion = new StringBuilder();
		if(this.cveMat != null && this.cveMat.length() > 0){
			condicion.append(" AND m.cveMat LIKE ? ");
		}
		if(this.nomMat != null && this.nomMat.length() > 0){
			condicion.append(" AND m.nomMat LIKE ? ");
		}
		if(this.nivel > 0){
			condicion.append(" AND n.cveNiv = ? ");
		}
		if(this.modulo > 0){
			condicion.append(" AND m.Modulo = ? ");
		}
		if(this.unidad > 0){
			condicion.append(" AND m.Unid = ? ");
		}
		if(this.estadoSolicitud >= 0){
			if(this.estadoSolicitud == 0){
				condicion.append(" AND (s.estadoSolicitud IS NULL OR s.estadoSolicitud = ? ) ");
			}else{
				condicion.append(" AND s.estadoSolicitud = ? ");
			}
			
		}
		return condicion.toString();
	}

	String queryBuscar = "	FROM tblmat m "
	+ " LEFT JOIN (SELECT MAX(fechaSolicitud) fechasoli, CveMat"
	+ "            FROM tblmateriasolicitud"
	+ " 		   WHERE BanSolicitud = 1"
	+ " 		   GROUP BY CveMat) solicitud"
	+ " ON m.cvemat = solicitud.cvemat"
	+ " LEFT JOIN tblmateriasolicitud s"
	+ " ON s.fechasolicitud = solicitud.fechasoli AND s.cveMat = solicitud.cvemat"
	+ " LEFT JOIN mstcode mc"
	+ " ON mc.CveCode = '" + MstCode.CVE_CODE_SOLICITUD + "' AND IFNULL(s.EstadoSolicitud,0) = mc.code"
	+ "  AND mc.BanCode = 1"
	+ " LEFT JOIN tblniv_Grado ng ON ng.cveNiv_Grado = m.CveNiv_Grado AND ng.banGrado = 1"
	+ " INNER JOIN tblniv n ON n.cveNiv = ng.cveNiv AND n.banNiv = 1"
	+ " LEFT JOIN tblpermisocontenido pc ON pc.cveMat = m.cveMat AND pc.cveUsu = ? AND pc.banpermisocontenido = 1"
	+ " WHERE m.banmat IN (0,1) ";

	/**
	 * Buscar las materias
	 * @return
	 * @throws Exception
	 */
	public List<Materia> Buscar(Usuario u, int limitfrom, int numeromostrar) throws Exception{
		List<Materia> listMat = new ArrayList<Materia>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT m.cveMat ,m.nomMat ,m.modulo ,m.unid ,m.banMat ,m.fechaModf ,ng.nomGrado, ng.nomniv_grado "
					+ " ,n.cveNiv, n.nomniv ,n.nomDir ,mc.nomCode ,IFNULL(s.estadoSolicitud,0) AS estadoSolicitud, "
					+ " fechasolicitud, pc.permiso" 
					+ queryBuscar;
			
			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				query += condicion.toString();
			}

			query += " ORDER BY fechasolicitud DESC, ng.cveNiv, m.modulo, m.cveMat ";
			query += " LIMIT ?, ?";

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, u.getCveUsu());
			int cnt = SetPreparedStatement(preparedStatement);

			cnt++;
			preparedStatement.setInt(cnt, limitfrom);
			cnt++;
			preparedStatement.setInt(cnt, numeromostrar);

			
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				Materia m = new Materia();
				m.cveMat = rs.getString("cveMat");
				m.nomMat = rs.getString("nomMat");
				m.modulo = rs.getInt("modulo");
				m.unidad = rs.getInt("unid");
				m.nivel = rs.getInt("cveNiv");
				m.nomNivel = rs.getString("nomNiv");
				m.nomNivelGrado = rs.getString("nomniv_grado");
				m.permisoContenido = rs.getInt("permiso");
				m.banMat = rs.getInt("banMat");
				m.fechaModf = rs.getTimestamp("fechaModf");
								
				MateriaSolicitud ms = new MateriaSolicitud();
				ms.setCveMat(rs.getString("nomCode"));
				m.listMatSol.add(ms);
				
				ms.setNomEstadoSolicitud(rs.getString("nomCode"));
				ms.setEstadoSolicitud(rs.getInt("estadoSolicitud"));
				ms.setFechaSolicitud(rs.getDate("fechasolicitud"));
				m.matSol = ms;
				
				listMat.add(m);
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return listMat;
	}
	
	/**
	 * Buscar las materias
	 * @return
	 * @throws Exception
	 */
	public static Materia Buscar(String cveMat, String cveUsu) throws Exception{
		Materia m = new Materia();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = 
					"SELECT m.cveMat ,m.nomMat ,m.modulo ,m.unid ,m.banMat ,m.CveNiv_Grado"
					+ " ,m.fechaModf ,n.cveNiv ,n.nomDir ,n.nomNiv ,ng.nomGrado, ng.nomNiv_Grado"
					+ " FROM tblmat m"
					+ " LEFT JOIN tblniv_Grado ng ON m.CveNiv_Grado = ng.cveNiv_Grado AND ng.banGrado = 1"
					+ " INNER JOIN tblniv n ON ng.cveNiv = n.cveNiv AND n.banNiv = 1"
					+ " LEFT JOIN tblpermisocontenido pc ON pc.cveMat = m.cveMat AND pc.cveUsu = ? AND pc.banpermisocontenido = 1"
					+ " WHERE m.cveMat = ?";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveUsu);
			preparedStatement.setString(2, cveMat);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				m.cveMat = rs.getString("cveMat");
				m.nomMat = rs.getString("nomMat");
				m.modulo = rs.getInt("modulo");
				m.unidad = rs.getInt("unid");
				m.nivel = rs.getInt("cveNiv");
				m.cveNivelGrado = rs.getInt("cveNiv_Grado");
				m.nomNivel = rs.getString("nomNiv");
				m.nomNivelDir = rs.getString("nomDir");
				m.nomNivelGrado = rs.getString("nomNiv_Grado");
				m.nomGrado = rs.getString("nomGrado");
				m.banMat = rs.getInt("banMat");
				m.fechaModf = rs.getTimestamp("fechaModf");

 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return m;
	}

	public static Materia Buscar(String cveMat) throws Exception{
		Materia m = new Materia();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = 
					"SELECT m.cveMat ,m.nomMat ,m.modulo ,m.unid ,m.banMat ,m.CveNiv_Grado"
					+ " ,m.fechaModf ,n.cveNiv ,n.nomDir ,n.nomNiv ,ng.nomGrado, ng.nomNiv_Grado"
					+ " ,IFNULL(s.estadoSolicitud,0) as estadoSolicitud, mc.nomCode,fechasolicitud"
					+ " FROM tblmat m"
					+ " LEFT JOIN tblniv_Grado ng ON m.CveNiv_Grado = ng.cveNiv_Grado AND ng.banGrado = 1"
					+ " INNER JOIN tblniv n ON ng.cveNiv = n.cveNiv AND n.banNiv = 1"
					+ "	LEFT JOIN (SELECT MAX(fechaSolicitud) fechasoli, CveMat"
					+ "	            FROM tblmateriasolicitud"
					+ "	 		    WHERE BanSolicitud = 1"
					+ "	  		    GROUP BY CveMat) solicitud"
					+ " ON m.cvemat = solicitud.cvemat"
					+ " LEFT JOIN tblmateriasolicitud s"
					+ " ON s.fechasolicitud = solicitud.fechasoli AND s.cveMat = solicitud.cvemat"
					+ "	LEFT JOIN mstcode mc ON mc.CveCode = '" + MstCode.CVE_CODE_SOLICITUD + "' "
					+ "  AND IFNULL(s.EstadoSolicitud,0) = mc.code AND mc.BanCode = 1"					
					+ " WHERE m.cveMat = ?";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveMat);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				m.cveMat = rs.getString("cveMat");
				m.nomMat = rs.getString("nomMat");
				m.modulo = rs.getInt("modulo");
				m.unidad = rs.getInt("unid");
				m.nivel = rs.getInt("cveNiv");
				m.cveNivelGrado = rs.getInt("cveNiv_Grado");
				m.nomNivel = rs.getString("nomNiv");
				m.nomNivelDir = rs.getString("nomDir");
				m.nomNivelGrado = rs.getString("nomNiv_Grado");
				m.nomGrado = rs.getString("nomGrado");
				m.banMat = rs.getInt("banMat");
				m.fechaModf = rs.getTimestamp("fechaModf");
				
				MateriaSolicitud ms = new MateriaSolicitud();
				ms.setCveMat(rs.getString("nomCode"));
				ms.setNomEstadoSolicitud(rs.getString("nomCode"));
				ms.setEstadoSolicitud(rs.getInt("estadoSolicitud"));
				ms.setFechaSolicitud(rs.getDate("fechasolicitud"));				
				m.matSol = ms;


 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return m;
	}

	/**
	 * Buscar las materias en Bachillerato o Licenciatura o etc.
	 * @return
	 * @throws Exception
	 */
	public static boolean ExistsMateria(Connection con, String cveMat) throws Exception{
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT cveMat FROM tblmat WHERE cveMat = ? AND banMat = 1";
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveMat);

			ResultSet rs = preparedStatement.executeQuery();   
			return rs.next();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
	}


	public static int CountMateriaDeExternoBac(Connection con) throws Exception {
		int cnt = 0;
		
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT count(A.CveMat) cnt FROM tblmat A WHERE A.BanMat=1 ";
			
			preparedStatement = con.prepareStatement(query);
						
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				cnt = rs.getInt("cnt");
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return cnt;
	}
	public static int CountMateriaDeExternoLic(Connection con, int nivelGrado) throws Exception {

		int cnt = 0;
		
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT count(A.CveMat) cnt FROM tblmat A, tblniv_grado B WHERE A.CveNiv = B.CveNiv_Grado AND B.CveNiv_Grado = ? AND A.BanMat=1 ";
			
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, nivelGrado);
						
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				cnt = rs.getInt("cnt");
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return cnt;
	}

	
	/**
	 * Buscar en el sistema de Bachillerato
	 * @param con
	 * @param limitfrom
	 * @param numeromostrar
	 * @return
	 * @throws Exception
	 */
	public static List<Materia> BuscarDeExternoBac(Connection con, int limitfrom, int numeromostrar) throws Exception{
		List<Materia> listMat = new ArrayList<Materia>();
		Materia m = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT A.CveMat, A.NomMat FROM tblmat A WHERE A.BanMat=1"
							+ " ORDER BY A.CveMat"
							+ " LIMIT ?, ?";
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, limitfrom);
			preparedStatement.setInt(2, numeromostrar);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				m = new Materia();
				m.cveMat = rs.getString("CveMat");
				m.nomMat = rs.getString("NomMat");
				m.nivel = Common.NIVEL_BACHILLERATO;

				listMat.add(m);
				 
			}
			
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
		
		return listMat;
	}


	/**
	 * Buscar en el sistema de Licenciatura
	 * @param con
	 * @param nivelGrado
	 * @param limitfrom
	 * @param numeromostrar
	 * @return
	 * @throws Exception
	 */
	public static List<Materia> BuscarDeExternoLic(Connection con, int nivelGrado, int limitfrom, int numeromostrar) throws Exception{
		List<Materia> listMat = new ArrayList<Materia>();
		Materia m = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = 
					"SELECT A.CveMat, A.NomMat FROM tblmat A, tblniv_grado B WHERE A.CveNiv = B.CveNiv_Grado and B.CveNiv_Grado = ? AND A.BanMat=1 "
					+ " ORDER BY A.CveMat"
					+ " LIMIT ?, ?";

			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, nivelGrado);
			preparedStatement.setInt(2, limitfrom);
			preparedStatement.setInt(3, numeromostrar);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				m = new Materia();
				m.cveMat = rs.getString("CveMat");
				m.nomMat = rs.getString("NomMat");
				m.nivel = Common.NIVEL_LICENCIATURA;
				m.cveNivelGrado = nivelGrado;

				listMat.add(m);
  			}
			
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
		
		return listMat;
	}

	
	
	/**
	 * Buscar en el sistema de Bachillerato o Licenciatura
	 * @param con
	 * @param cveMat
	 * @return
	 * @throws Exception
	 */
	public static Materia BuscarDeExterno(Connection con, String cveMat) throws Exception{
		Materia m = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = 
					"SELECT m.cveMat ,m.nomMat ,m.sem ,m.unid ,m.banMat, m.cveNiv"
					+ " FROM tblmat m"
					+ " WHERE m.cveMat = ? AND banMat = 1";
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveMat);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				m = new Materia();
				m.cveMat = rs.getString("cveMat");
				m.nomMat = rs.getString("nomMat");
				m.modulo = rs.getInt("sem");
				m.unidad = rs.getInt("unid");
				m.cveNivelGrado = rs.getInt("cveNiv");
//				m.nomGrado = rs.getString("nmggrado");
				m.banMat = rs.getInt("banMat");

 			}
			
			NivelGrado bean = NivelGrado.BuscarConCveNivGrado(m.cveNivelGrado);
			Nivel beanNv= Nivel.Buscar(bean.getCveNivel());
			m.nomNivelDir = beanNv.getNomDir();
			m.nomGrado = bean.getNomGrado();
			
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
		
		return m;
	}


	
	/**
	 * Dar de baja una materia
	 * @param cveMat
	 * @throws SQLException
	 */
	public static void DarBajaLogical(String cveMat) throws SQLException{
		Connection con = MySqlConnector.getConnection();
		con.setAutoCommit(false);
		try{
			DarBajaLogical(con, cveMat, 1);
			con.commit();
		}catch(Exception e){
			if (con != null) {
				con.rollback();
			}			
		}finally{
			if (con != null) {
				con.setAutoCommit(true);
	        }
		}
	}
	
	/**
	 * Dar de baja una materia
	 * @param con
	 * @param cvemat
	 * @return
	 * @throws SQLException
	 */
	public static int DarBajaLogical(Connection con, String cvemat, int bandera) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblmat SET BanMat = ? WHERE CveMat = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, bandera);
			preparedStatement.setString(2, cvemat);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return count;
	}
	
	/**
	 * Dar de baja una materia
	 * @param con
	 * @param cvemat
	 * @return
	 * @throws SQLException
	 */
	public static int Delete(Connection con, String cvemat) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "DELETE FROM tblmat WHERE CveMat = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cvemat);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return count;
	}
	

	public int Modificar(Connection con) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblmat SET ";
			
			String condition= "";
			if(this.nomMat != null && this.nomMat.length() > 0){
				condition += " NomMat = ? ";
			}
			if(this.modulo > 0){
				if(condition.length() > 0){
					condition += ",";
				}
				condition += " Modulo = ? ";
			}
			if(this.unidad > 0){
				if(condition.length() > 0){
					condition += ",";
				}
				condition += " Unid = ? ";
			}
			if(this.cveNivelGrado > 0){
				if(condition.length() > 0){
					condition += ",";
				}
				condition += " CveNiv_Grado = ? ";
			}

			if(condition.length() > 0){
				condition += ",";
			}
			query += condition + " FechaModf = now() WHERE CveMat = ?";
			
			preparedStatement = con.prepareStatement(query);

			int condCount = 0;

			if(this.nomMat != null && this.nomMat.length() > 0){
				condCount++;
				preparedStatement.setString(condCount, this.nomMat);
			}
			if(this.modulo > 0){
				condCount++;
				preparedStatement.setInt(condCount, this.modulo);
			}
			if(this.unidad > 0){
				condCount++;
				preparedStatement.setInt(condCount, this.unidad);
			}
			if(this.cveNivelGrado > 0){
				condCount++;
				preparedStatement.setInt(condCount, this.cveNivelGrado);
			}

			condCount++;
			preparedStatement.setString(condCount, this.cveMat);

			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return count;
	}

	
	public int Insertar(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "INSERT INTO tblmat (CveMat, CveNiv_Grado, NomMat, Modulo, unid, FechaModf, BanMat) "
					+ "VALUES (?,?,?,?,?,now(),?)";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveMat);
			
			preparedStatement.setInt(2, this.cveNivelGrado);
			preparedStatement.setString(3, this.nomMat);
			preparedStatement.setInt(4, this.modulo);
			preparedStatement.setInt(5, this.unidad);
			preparedStatement.setInt(6, 1);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		return count;
	}
	
	public static List<Materia> BuscaModulos() throws Exception{
		List<Materia> listMat = new ArrayList<Materia>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query ="SELECT ng.cveNiv ,m.modulo "
					+ " FROM tblmat m "
					+ " LEFT JOIN tblniv_grado ng ON ng.cveNiv_Grado = m.CveNiv_Grado "
					+ " GROUP BY ng.cveNiv, modulo "
					+ " ORDER BY ng.cveNiv, modulo";

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				Materia bean = new Materia();
				bean.nivel = rs.getInt("cveNiv");
				bean.modulo = rs.getInt("modulo");
				listMat.add(bean);
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return listMat;
	}
	
	public String GetPathMateriaAbsolute(){
		Configuracion config = new Configuracion();
		return GetPathMateria(config.getUserfilesAbsolutePath());

	}
	public String GetPathMateriaRelative(){
		
		Configuracion config = new Configuracion();
		return GetPathMateria(Common.SLASH + config.getUserfilesPath()).
				replaceAll(Matcher.quoteReplacement(File.separator), Common.SLASH);
	}
	
	public String GetPathMateria(String prefix){
		return prefix 	    
				+ File.separator + this.nomNivelDir + File.separator 
	    		+ this.nomGrado + File.separator + this.modulo + File.separator + this.cveMat;

	}
	
}
