package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.db.MySqlConnector;


public class MateriaSolicitud implements Serializable {
	
	/** Clave de la solicitud **/
	private int cveSolicitud;
	/** Clave de la materia **/
	private String cveMat;
	/** Fecha de la solicitud **/
	private Date fechaSolicitud;
	/** Usuario de la solicitud **/
	private String usuarioSolicitud;
	/** Nota **/
	private String nota;
	/** Resultado **/
	private int resultado = -1;
	/** Resultado **/
	private String nomResultado;
	/** Estado de la solicitud **/
	private int estadoSolicitud = -1;
	/** Nombre del estado de la solicitud **/
	private String nomEstadoSolicitud;
	/** Usuario de solicitar **/
	private Usuario usuSolcitud;
	
	public int getCveSolicitud(){
		return cveSolicitud;
	}
	public void setCveSolicitud(int cveSolicitud){
		this.cveSolicitud = cveSolicitud;
	}
	public String getCveMat(){
		return cveMat;
	}
	public void setCveMat(String cveMat){
		this.cveMat = cveMat;
	}

	
	public Date getFechaSolicitud(){
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud){
		this.fechaSolicitud = fechaSolicitud;
	}
	public String getUsuarioSolicitud(){
		return usuarioSolicitud;
	}
	public void setUsuarioSolicitud(String usuarioSolicitud){
		this.usuarioSolicitud = usuarioSolicitud;
	}
	public String getNota(){
		return nota;
	}
	public void setNota(String nota){
		this.nota = nota;
	}
	public int getResultado(){
		return resultado;
	}
	public void setResultado(int resultado){
		this.resultado = resultado;
	}
	public int getEstadoSolicitud(){
		return estadoSolicitud;
	}
	public void setEstadoSolicitud(int estadoSolicitud){
		this.estadoSolicitud = estadoSolicitud;
	}	
	public String getNomResultado(){
		return nomResultado;
	}
	public void setNomResultado(String nomResultado){
		this.nomResultado = nomResultado;
	}	
	public String getNomEstadoSolicitud(){
		return nomEstadoSolicitud;
	}
	public void setNomEstadoSolicitud(String nomEstadoSolicitud){
		this.nomEstadoSolicitud = nomEstadoSolicitud;
	}	
	public Usuario getUsuSolcitud(){
		return usuSolcitud;
	}
	public void setUsuSolcitud(String usuarioSolicitud){
		try {
			this.usuSolcitud = Usuario.Buscar(usuarioSolicitud); 
		} catch (Exception e) {
			
		}
	}	
	

	/**
	 * Buscar las materias
	 * @return
	 * @throws Exception
	 */
	public List<MateriaSolicitud> Buscar() throws Exception{
		List<MateriaSolicitud> list = new ArrayList<MateriaSolicitud>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT cveSolicitud, cveMat, fechaSolicitud, usuarioSolicitud, "
					+ " nota, resultado, estadoSolicitud, "
					+ " nomEstado.nomCode AS nomEstado, nomResultado.nomCode AS nomResultado"
					+ "	FROM tblmateriaSolicitud "
					+ " LEFT OUTER JOIN mstcode nomEstado ON nomEstado.cveCode ='" + MstCode.CVE_CODE_SOLICITUD + "' AND nomEstado.Code =  estadoSolicitud"
					+ " LEFT OUTER JOIN mstcode nomResultado ON nomResultado.cveCode ='" + MstCode.CVE_CODE_SOLICITUD + "' AND nomResultado.Code =  resultado"
					+ " WHERE cveMat = ? AND bansolicitud = 1 ";
			
			if(this.estadoSolicitud >= 0){
				query += " AND estadoSolicitud = ?";
			}

			query += " ORDER BY fechaSolicitud DESC";

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, this.cveMat);

			int count = 1;
			if(this.estadoSolicitud >= 0){
				count++;
				preparedStatement.setInt(count, this.estadoSolicitud);
			}

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				MateriaSolicitud bean = new MateriaSolicitud();
				bean.cveSolicitud = rs.getInt("cveSolicitud");
				bean.cveMat = rs.getString("cveMat");
				bean.usuarioSolicitud = rs.getString("usuarioSolicitud");
				bean.fechaSolicitud = rs.getTimestamp("fechaSolicitud");
				bean.nota = rs.getString("nota");
				bean.resultado = rs.getInt("resultado");
				bean.estadoSolicitud = rs.getInt("estadoSolicitud");
				bean.setUsuSolcitud(bean.usuarioSolicitud);
				bean.nomEstadoSolicitud = rs.getString("nomEstado");
				bean.nomResultado = rs.getString("nomResultado");
				
				list.add(bean);
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
		
		return list;
	}
	

	
	
	public int Insertar(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "INSERT INTO tblmateriasolicitud "
					+ "(CveMat, FechaSolicitud, UsuarioSolicitud,"
					+ " Nota, EstadoSolicitud, BanSolicitud, Resultado) "
					+ " VALUES (?,now(),?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveMat);
			preparedStatement.setString(2, this.usuarioSolicitud);
			preparedStatement.setString(3, this.nota);
			preparedStatement.setInt(4, this.estadoSolicitud);
			preparedStatement.setInt(5, 1);
			preparedStatement.setInt(6, this.resultado);
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
	 * Dar de baja
	 * @param con
	 * @param cvemat
	 * @return
	 * @throws SQLException
	 */
	public static int DarBajaLogical(Connection con, String cvemat) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblmateriasolicitud SET BanSolicitud =0 WHERE CveMat =?";
			
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
	


}
