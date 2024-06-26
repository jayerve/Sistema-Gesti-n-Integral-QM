package paq_nomina.conexion;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ConexionOracle {	
	private Connection con;
	private List<String> sqlPantalla = new ArrayList<String>();

	public Connection getConexion()
	{
		return this.con;
	}

	public ConexionOracle(String servidor,String puerto,String sid,String usuario,String clave) {
		String driver ="oracle.jdbc.driver.OracleDriver";		
		String cadena="jdbc:oracle:thin:@"+servidor+":"+puerto+":"+sid;
		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");			
		}
		try
		{
			Class.forName(driver).newInstance();
			con=DriverManager.getConnection(cadena,usuario,clave);

		}
		catch(Exception ee)
		{
			System.out.println("Error: " + ee.getMessage());
		}
	}


	public String ejecutarSql(String sql) {
		String mensaje = "";	        
		Statement sta_sentencia = null;
		try {
			con.setAutoCommit(false);
			sta_sentencia = con.createStatement();
			sta_sentencia.executeUpdate(sql);	           
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (Exception ex) {
			}
			System.out.println("ERROR ejecutar() : ".concat(sql).concat(" : ").concat(e.getMessage()));	           
			mensaje = e.getMessage();
		} finally {
			if (con != null) {
				try {
					sta_sentencia.close();
				} catch (Exception e) {
				}
			}
		}
		return mensaje;
	}



	public void agregarSql(String sql) {
		if (!sqlPantalla.contains(sql)) {
			sqlPantalla.add(sql);
		}
	}

	public String ejecutarListaSql() {
		String mensaje = "";
		if (!sqlPantalla.isEmpty()) {
			String sql = "";
			Statement sta_sentencia = null;
			try {
				con.setAutoCommit(false);
				sta_sentencia = con.createStatement();
				for (int i = 0; i < sqlPantalla.size(); i++) {
					sql = sqlPantalla.get(i);					
					sta_sentencia.executeUpdate(sql);
				}
				con.commit();
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (Exception ex) {
				}
				System.out.println("FALLO: ".concat(e.getMessage()));
				mensaje = e.getMessage();

			} finally {
				if (con != null) {
					try {
						if (sta_sentencia != null) {
							sta_sentencia.close();
						}
					} catch (Exception e) {
					}
				}
			}
			sqlPantalla.clear();
		}
		return mensaje;
	}


	public void desconectar(){
		try{
			con.close();
		}catch(Exception ex){

		}
	}

	public ResultSet consultar(String sql)
	{
	
		ResultSet reg=null;
		Statement sta_sentencia = null;
		try
		{
			sta_sentencia=getConexion().createStatement();
			reg=sta_sentencia.executeQuery(sql);					
		}
		catch(Exception ee)
		{
			System.out.println("FALLO: ".concat(ee.getMessage()));
		}
		return(reg);
	}


	public List<String> getListaSql(){
		return  sqlPantalla;
	}
	
}
