package paq_sumillas.ejb;


import javax.ejb.EJB;
import javax.ejb.Stateless;
//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;


/**
 * @author 
 *
 */
@Stateless
public class ServicioGerenciaCoordinacion {

	@EJB
	private ServicioGerenciaCoordinacion ser_gercoo;

	public String getVacio(){
		String sql_empleadoContrato="SELECT IDE_GEARE, DETALLE_GEARE " +
				"FROM GEN_AREA GA " +
				"WHERE  GA.IDE_PADRE_GEARE = 0 ";
		return sql_empleadoContrato;
	}
	
	public String getGerencia(){
		String sql_empleadoContrato="SELECT IDE_GEARE, DETALLE_GEARE " +
				"FROM GEN_AREA GA " +
				"WHERE GA.IDE_PADRE_GEARE IS NULL and activo_geare=true";
		return sql_empleadoContrato;
	}
	
	public String getCoordinacionesTotas(){
		String sql_empleadoContrato="SELECT IDE_GEARE, DETALLE_GEARE " +
				"FROM GEN_AREA GA " +
				"WHERE  GA.IDE_PADRE_GEARE IS NOT NULL and activo_geare=true";
		return sql_empleadoContrato;
	}
	
	public String getCoordinacion(Integer gerencia){
		String sql_empleadoContrato="SELECT IDE_GEARE, DETALLE_GEARE " +
				"FROM GEN_AREA GA " +
				"WHERE  GA.IDE_PADRE_GEARE IS NOT NULL and activo_geare=true AND " +
				"GA.IDE_PADRE_GEARE = " + gerencia;
		return sql_empleadoContrato;
	}
	
	public String getAreaUsuarioConectado(String idUsuarioConectado){
		String sql_empleadoContrato="SELECT IDE_GEARE FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR "+
				"WHERE GEN_EMPLEADOS_DEPARTAMENTO_PAR.IDE_GEDED ="+
				"(SELECT a.IDE_GEDED FROM GEN_DETALLE_EMPLEADO_DEPARTAME a LEFT JOIN ( SELECT IDE_GEINS,DETALLE_GEINS FROM GEN_INSTITUCION)b ON a.IDE_GEINS=b.IDE_GEINS LEFT JOIN ("+ 
					"SELECT a.IDE_GEAME AS IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a LEFT JOIN(SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA )b "+ 
						"ON b.IDE_GEAED=a.IDE_GEAED LEFT JOIN ( SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA )c ON c.IDE_GEMED=a.IDE_GEMED ORDER BY  b.DETALLE_GEAED,"+
							"c.DETALLE_GEMED )c ON c.IDE_GEAME=a.IDE_GEAME WHERE a.IDE_GTEMP=(SELECT IDE_GTEMP FROM SIS_USUARIO WHERE IDE_USUA="+idUsuarioConectado+") ORDER BY IDE_GEDED DESC LIMIT 1) AND IDE_GTEMP=(SELECT IDE_GTEMP FROM SIS_USUARIO WHERE IDE_USUA="+idUsuarioConectado+")";
		return sql_empleadoContrato;
	}
	
	public String getPerfilConectado(String idUsuarioConectado){
		String sql_perfilUsuario="SELECT NOM_PERF FROM SIS_USUARIO, SIS_PERFIL WHERE IDE_USUA="+idUsuarioConectado+ " AND SIS_USUARIO.IDE_PERF=SIS_PERFIL.IDE_PERF";
		return sql_perfilUsuario;
	}
	
	
	
}
