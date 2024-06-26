package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioGeneralAdmPrecon {
	public ServicioGeneralAdmPrecon(){
		
	}
	
	//servicio empleadoContrato por Area
	
	public String servicioEmpleadoContrato(String estado,Integer ide_area){
		String sql_empleadoContrato="SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP || ' ', " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) || ' ' " +
						" AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
						" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
						" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
						" WHERE "
						//+ "EPAR.IDE_GEARE="+ide_area+" AND "
								+ "EPAR.ACTIVO_GEEDP in ("+estado+")";
		return sql_empleadoContrato;
	}
	//servicio empleadosActivos
	public String servicioEmpleados(String estado,Integer ide_gtemp){
		String sql_empleadosActivos="select ide_gtemp, (apellido_paterno_gtemp|| ' ' || " +
								"(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)|| ' ' ||primer_nombre_gtemp|| ' ' || " +
								"(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end)|| ' ' ||documento_identidad_gtemp) as empleado from gth_empleado where ide_gtemp = "+ide_gtemp+" and activo_gtemp in ("+estado+")  " +
								"order by apellido_paterno_gtemp, apellido_materno_gtemp ";
		return sql_empleadosActivos;
	}
	//servicio departamento por empleado
	public String servicioDepartamento(String estado,String ide_gtemp){
		String sql_empleadoContrato="SELECT EPAR.IDE_GEDEP, DEPA.DETALLE_GEDEP,EPAR.IDE_GEEDP, AREA.IDE_GEARE,area.abreviatura_geare,area.detalle_geare,garea.abreviatura_geare as gabrev"
				+ " FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
						" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
						" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
						+ " LEFT JOIN GEN_AREA GAREA ON GAREA.IDE_GEARE=AREA.IDE_PADRE_GEARE " +
						" WHERE EPAR.IDE_GTEMP="+ide_gtemp+" AND EPAR.ACTIVO_GEEDP in ("+estado+")";
		return sql_empleadoContrato;
	}
	public String servicioDepartamentoYIdEmpleado(String ide_geedp){
		String sql_depar_empleado="SELECT EPAR.IDE_GTEMP, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE WHERE EPAR.IDE_GEEDP="+ide_geedp+" AND EPAR.ACTIVO_GEEDP;";
		return sql_depar_empleado;
		
	}
	//servcio perfilConectado
	public String getPerfilConectado(String IDE_PERF){
		//public String getPerfilConectado(String idUsuarioConectado, String IDE_PERF){
		  //String sql_perfilUsuario="SELECT NOM_PERF FROM SIS_USUARIO, SIS_PERFIL WHERE IDE_USUA="+idUsuarioConectado+ " AND SIS_USUARIO.IDE_PERF=SIS_PERFIL.IDE_PERF";
		String sql_perfilUsuario="SELECT distinct NOM_PERF FROM SIS_PERFIL WHERE IDE_PERF IN ("+IDE_PERF+")";
		return sql_perfilUsuario;
	}
	public String getAreaUsuarioConectado(String idUsuarioConectado){
		  String sql_empleadoContrato="SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR "+
		    "WHERE GEN_EMPLEADOS_DEPARTAMENTO_PAR.IDE_GEDED ="+
		    "(SELECT a.IDE_GEDED FROM GEN_DETALLE_EMPLEADO_DEPARTAME a LEFT JOIN ( SELECT IDE_GEINS,DETALLE_GEINS FROM GEN_INSTITUCION)b ON a.IDE_GEINS=b.IDE_GEINS LEFT JOIN ("+ 
		     "SELECT a.IDE_GEAME AS IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a LEFT JOIN(SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA )b "+ 
		      "ON b.IDE_GEAED=a.IDE_GEAED LEFT JOIN ( SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA )c ON c.IDE_GEMED=a.IDE_GEMED ORDER BY  b.DETALLE_GEAED,"+
		       "c.DETALLE_GEMED )c ON c.IDE_GEAME=a.IDE_GEAME WHERE a.IDE_GTEMP=(SELECT IDE_GTEMP FROM SIS_USUARIO WHERE IDE_USUA="+idUsuarioConectado+") ORDER BY IDE_GEDED DESC LIMIT 1) AND IDE_GTEMP=(SELECT IDE_GTEMP FROM SIS_USUARIO WHERE IDE_USUA="+idUsuarioConectado+")";
	  return sql_empleadoContrato;
	}
	//retorna id usuario (ide_geedp)
	public String getIdUsuario(Integer ide_area,String idUsuarioConectado){
		String ide_geedp="select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ide_geare="+ide_area+" and ide_gtemp=(SELECT ide_gtemp FROM SIS_USUARIO WHERE IDE_USUA="+idUsuarioConectado+");";
		return ide_geedp;
	}
	public String getProveedores(){
	    String tab_proveedor="select ide_tepro, ruc_tepro, nombre_tepro " +
	            "  from tes_proveedor  " +
	            " where activo_tepro = 'true' and nombre_tepro is not null " +
	            " ORDER BY  nombre_tepro";
	    return tab_proveedor;
	}
	public String getInsertContrato(Integer ide_prcon,Integer ide_tipocontrato,String objeto_contrato, Double monto,Integer ide_proveedor){
		String insert_contrato = "insert into pre_contrato(ide_prcon,ide_prtsc,tipo_int_ext_prcon,observacion_prcon,monto_original_prcon,ide_tepro,activo_prcon)"
				+ "values ("+ide_prcon+","+ide_tipocontrato+",1,'"+objeto_contrato+"',"+monto+","+ide_proveedor+",true);";
		return insert_contrato;
	}

}
