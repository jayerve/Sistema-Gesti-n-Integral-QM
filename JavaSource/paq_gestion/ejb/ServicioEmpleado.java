package paq_gestion.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.print.DocFlavor.STRING;

import framework.aplicacion.TablaGenerica;



import paq_sistema.aplicacion.Utilitario;
import pckUtilidades.CConversion;

@Stateless
public class ServicioEmpleado {

	private Utilitario utilitario=new Utilitario();

	
	/**metodo que retorna una fila de la tabla gen_region a la que pertenece el empleado 
	 * @param ide_gtemp
	 * @return tabla
	 */
	public TablaGenerica getRegionEmpleado(String ide_gtemp){
		if (ide_gtemp!=null && !ide_gtemp.isEmpty()){
		TablaGenerica tab_reg=utilitario.consultar("select * from GEN_REGION where IDE_GEREG in ( " +
				"select IDE_GEREG from GEN_DIVISION_POLITICA where IDE_GEDIP in ( " +
				"select IDE_GEDIP from SIS_SUCURSAL where ide_sucu in ( " +
				"select IDE_SUCU from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP="+ide_gtemp+" and ACTIVO_GEEDP=TRUE)))");
		return tab_reg;
		}
		return null;
		
	}
	
	public boolean isContratoActivo(String IDE_GEEDP){
		TablaGenerica tab_emp_dep_par=utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEEDP="+IDE_GEEDP);
		if (tab_emp_dep_par.getTotalFilas()>0){
			if (tab_emp_dep_par.getValor("ACTIVO_GEEDP")!=null && !tab_emp_dep_par.getValor("ACTIVO_GEEDP").isEmpty()){
				if (tab_emp_dep_par.getValor("ACTIVO_GEEDP").equals("TRUE")){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 * metodo booleano para validar el tipo de documento de identidad cedula y ruc
	 */
	public boolean isDocumentoIdentidadValido(String ide_gttdi,String documento_identidad){
		if (ide_gttdi!=null && !ide_gttdi.isEmpty()){
			if (documento_identidad!=null && !documento_identidad.isEmpty()){
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
					if (!utilitario.validarCedula(documento_identidad)){					
						return false;
					}
				}else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
					if (!utilitario.validarRUC(documento_identidad)){						
						return false;
					}
				}
			}else{				
				return false;
			}
		}else{			
			return false;
		}
		return true;
	}


	/**Busca un empleado por cualquier campo 
	 * @param campo Nombre del campo al que se realizara la busqueda
	 * @param valor Valor de la busqueda
	 * @return
	 */
	public TablaGenerica getEmpleado(String campo,String valor){
		return utilitario.consultar("SELECT * FROM GTH_EMPLEADO WHERE "+campo+"='"+valor+"'");
	}

	/**Busca el correo de un empleado empleado por el campo IDE_GTEMP
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public TablaGenerica getCorreoEmpleado(String IDE_GTEMP){
		return utilitario.consultar("SELECT documento_identidad_gtemp,detalle_gtcor  FROM GTH_EMPLEADO e "
				+ " left join gth_correo c on c.ide_gtemp=e.ide_gtemp WHERE e.IDE_GTEMP="+IDE_GTEMP);		
	}

	/**Busca un empleado por el campo IDE_GEPER
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public TablaGenerica getEmpleado(String IDE_GTEMP){
		return getEmpleado("IDE_GTEMP",IDE_GTEMP);		
	}

	/**Busca un empleado por el campo DOCUMENTO_IDENTIDAD_GTEMP
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public TablaGenerica getEmpleadoDocumento(String DOCUMENTO_IDENTIDAD_GTEMP){
		return getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP",DOCUMENTO_IDENTIDAD_GTEMP);	
	}

	/**Busca si existe un empleado empleado por el campo IDE_GEPER
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public boolean isExisteEmpleado (String IDE_GTEMP){
		return !getEmpleado(IDE_GTEMP).isEmpty();
	}
	/**Busca si existe un empleado empleado por el campo DOCUMENTO_IDENTIDAD_GTEMP
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public boolean isExisteEmpleadoDocumento (String DOCUMENTO_IDENTIDAD_GTEMP){
		return !getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP",DOCUMENTO_IDENTIDAD_GTEMP).isEmpty();
	}

	
	public TablaGenerica getCargasFamiliares(String IDE_GTEMP){
		TablaGenerica tab_cargas_fam=utilitario.consultar("select * from GTH_CARGAS_FAMILIARES where IDE_GTEMP="+IDE_GTEMP);
		return tab_cargas_fam;
	}

	public boolean isExisteHijosEmpelado (String IDE_GTEMP){
		TablaGenerica tab_cargas_fam=getCargasFamiliares(IDE_GTEMP);
		for (int i = 0; i < tab_cargas_fam.getTotalFilas(); i++) {
			if (tab_cargas_fam.getValor(i, "IDE_GTTPR").equals("1")
					|| tab_cargas_fam.getValor(i, "IDE_GTTPR").equals("7")){
				return true;
			}
		}
		return false;
	}
	
	/**Verifica si un empleado es de la tercera edad >=65
	 * @return
	 */
	public boolean isTerceraEdad(String IDE_GTEMP){		
		TablaGenerica tab_empleado=getEmpleado(IDE_GTEMP);
		String str_fecha=tab_empleado.getValor("FECHA_NACIMIENTO_GTEMP");
		
		if(utilitario.getEdad(str_fecha)>=65){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public String getSQLEmpleadosActivos(){
		return "SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE ORDER BY APELLIDO_PATERNO_GTEMP";
	}
	
	public double getPorcentajeDiscapacidad(String IDE_GTEMP){
		TablaGenerica tab_discapa=utilitario.consultar("SELECT * FROM GTH_DISCAPACIDAD_EMPLEADO WHERE IDE_GTEMP="+IDE_GTEMP);
		if(!tab_discapa.isEmpty()){
			if(tab_discapa.getValor("PORCENTAJE_GTDIE")!=null){
				try {
					return Double.parseDouble(tab_discapa.getValor("PORCENTAJE_GTDIE"));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}		
		return 0;
	}

    public TablaGenerica getPartida(String IDE_GTEMP){
        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=true and IDE_GTEMP="+IDE_GTEMP);
    }
    
    public TablaGenerica getPartidaTipoRoles(String IDE_GTEMP){
        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GEEDP="+IDE_GTEMP+" limit 1");
    }
    public TablaGenerica getPartidaRoles(String IDE_GTEMP){
        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP="+IDE_GTEMP+" order by ide_geedp desc limit 1");
    }
    
    public TablaGenerica getPartidaNominaManual(String IDE_GTEMP){
        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where FECHA_LIQUIDACION_GEEDP BETWEEN '2019-08-01' AND '2019-08-31' and IDE_GTEMP="+IDE_GTEMP+" order by ide_geedp desc");
    }
    
    
    public TablaGenerica getPartidaLiquidacion(String IDE_NRROL,String ide_gtemp){

    	return utilitario.consultar("SELECT " +
					"EDP.IDE_GEEDP, " +
					"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
					"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " + //02
					"EDP.IDE_SUCU, "+
					"EDP.IDE_GTEMP, " + //04
					"EDP.IDE_GTTEM, " +
					"(case when EMP.PRIMER_NOMBRE_GTEMP is null then EMP.SEGUNDO_NOMBRE_GTEMP else EMP.PRIMER_NOMBRE_GTEMP end) as nombre, " + //06
					"(case when EMP.APELLIDO_PATERNO_GTEMP is null then EMP.APELLIDO_MATERNO_GTEMP else APELLIDO_PATERNO_GTEMP end) as apellido " + //07
					"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
					"INNER JOIN NRH_DETALLE_ROL DROL ON DROL.IDE_GEEDP=EDP.IDE_GEEDP AND IDE_NRROL in("+IDE_NRROL+") " +
					"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
					"WHERE EDP.IDE_GTEMP="+ide_gtemp+" "+ 
					"GROUP BY EDP.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP , " +
					"EMP.APELLIDO_MATERNO_GTEMP , " +
					"EMP.PRIMER_NOMBRE_GTEMP , " +
					"EMP.SEGUNDO_NOMBRE_GTEMP,EDP.IDE_SUCU,EDP.IDE_GTEMP, " +
					"EDP.IDE_GTTEM " +
					"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC");
	//System.out.println("sql getSqlEmpleadosRol..."+sql);
    }
    
    
    public TablaGenerica getPartidaEmpleado(String IDE_GTEMP){
        return utilitario.consultar("select  "
        		+ "ide_geedp, ide_gtemp, ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, "
        		+ "ide_gedep, ide_geare, ide_gttem, ide_gttco, ide_gttsi, ide_gtgre, "
        		+ "gen_ide_gegro, gen_ide_gecaf, ide_getiv, ide_gecae, ide_geded, "
        		+ "fecha_geedp, fecha_finctr_geedp, rmu_geedp, ajuste_sueldo_geedp, "
        		+ "fecha_encargo_geedp, fecha_ajuste_geedp, fecha_liquidacion_geedp,  "
        		+ "liquidacion_geedp, fecha_encargo_fin_geedp, sueldo_subroga_geedp, "
        		+ "ejecuto_liquidacion_geedp, observacion_geedp, usuario_ingre, "
        		+ "fecha_ingre, usuario_actua, fecha_actua, hora_ingre, hora_actua,  "
        		+ "activo_geedp, linea_supervicion_geedp, acumula_fondos_geedp, "
        		+ "control_asistencia_geedp, encargado_subrogado_geedp, valor_liquidacion_geedp  "
        		+ "from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP="+IDE_GTEMP+" "
        		+ "order by ide_geedp asc");
    }
    
    public TablaGenerica getRolesEmpleado(String IDE_GEEDP) {
        // (NRESR) ESTADO DE ROL ACTIVO=1 ===Poner en parametro
        return utilitario.consultar("select IDE_NRROL,DETALLE_GEANI,DETALLE_GEMES,IDE_NRDTN,ROL.IDE_GEPRO,FECHA_NRROL from NRH_ROL rol "
                + "INNER JOIN GEN_PERIDO_ROL PROL ON ROL.IDE_GEPRO=PROL.IDE_GEPRO "
                + "INNER JOIN GEN_ANIO ANIO ON PROL.IDE_GEANI=ANIO.IDE_GEANI "
                + "INNER JOIN GEN_MES mes on prol.ide_gemes=mes.ide_gemes "
                + "where IDE_NRROL in (select IDE_NRROL  from NRH_DETALLE_ROL where IDE_GEEDP="+IDE_GEEDP+" GROUP BY IDE_NRROL) "
                + "and IDE_NRESR=1"
                + "ORDER BY IDE_GEPRO desc");
    }
    
      public List getRolesEmpleadoLista(String IDE_GEEDP, String IDE_GTEMP) {
        // (NRESR) ESTADO DE ROL ACTIVO=1 ===Poner en parametro
        return utilitario.getConexion().consultar("select IDE_NRROL,DETALLE_GEANI,DETALLE_GEMES,IDE_NRDTN,ROL.IDE_GEPRO,to_CHAR(FECHA_NRROL,'DD-MM-YYYY') from NRH_ROL rol "
                + "INNER JOIN GEN_PERIDO_ROL PROL ON ROL.IDE_GEPRO=PROL.IDE_GEPRO "
                + "INNER JOIN GEN_ANIO ANIO ON PROL.IDE_GEANI=ANIO.IDE_GEANI "
                + "INNER JOIN GEN_MES mes on prol.ide_gemes=mes.ide_gemes "
                + "where IDE_NRROL in (select IDE_NRROL  from NRH_DETALLE_ROL where IDE_GEEDP in("+IDE_GEEDP+") GROUP BY IDE_NRROL) "
                + "and IDE_NRESR=1"
                + "ORDER BY IDE_GEPRO desc, mes.ide_gemes desc");    	  
    }
      
      
      public List getRolesEmpleadoListaControlador(String IDE_GEEDP, String IDE_GTEMP, String str_ide_rol) {
    	  int mes=0,anio=0;
    	  
    	  return utilitario.getConexion().consultar("select rol.IDE_NRROL,anio.DETALLE_GEANI,mes.DETALLE_GEMES || ' ' || ntn.detalle_nrtin,rol.IDE_NRDTN,ROL.IDE_GEPRO,to_CHAR(rol.FECHA_NRROL,'DD-MM-YYYY'), "
      	  		+ "drol.ide_geedp,mes.ide_gemes  "
      	  		+ "from NRH_ROL rol "
      	  		+ "INNER JOIN GEN_PERIDO_ROL PROL ON ROL.IDE_GEPRO=PROL.IDE_GEPRO  "
      	  		+ "INNER JOIN GEN_ANIO ANIO ON PROL.IDE_GEANI=ANIO.IDE_GEANI  "
      	  		+ "INNER JOIN GEN_MES mes on prol.ide_gemes=mes.ide_gemes  "
      	  		+ "inner join nrh_detalle_rol drol on drol.ide_nrrol=rol.ide_nrrol  "
      	  		+ "left join nrh_detalle_tipo_nomina ndtn on ndtn.ide_nrdtn=rol.ide_nrdtn  "
      	  		+ "left join nrh_tipo_nomina ntn on ntn.ide_nrtin=ndtn.ide_nrtin "
      	  		+ "where drol.IDE_GEEDP in("+IDE_GEEDP+")  "
      	  		+ "and prol.tipo_rol=1 "
      	  		+ "and  rol.ide_nrrol in ("+str_ide_rol +") "
      	  		+ "GROUP BY rol.IDE_NRROL,anio.DETALLE_GEANI,mes.DETALLE_GEMES,ntn.detalle_nrtin,rol.IDE_NRDTN,ROL.IDE_GEPRO,rol.FECHA_NRROL,drol.ide_geedp,mes.ide_gemes "
      	  		+ "ORDER BY rol.IDE_GEPRO desc, mes.ide_gemes desc");
        	  
    }
      
  	/**
  	 * Busca los correos de los empleados
  	 * @param IDE_GTEMP
  	 * @return
  	 */
  	public TablaGenerica getCorreoEmpleados(String IDE_GTEMP){
  		return utilitario.consultar("SELECT EMP.IDE_GTEMP,DETALLE_GTCOR FROM GTH_EMPLEADO emp " +
  				"left JOIN GTH_CORREO mail on EMP.IDE_GTEMP = MAIL.IDE_GTEMP and NOTIFICACION_GTCOR=true " +
  				"where EMP.IDE_GTEMP in("+IDE_GTEMP+")");
  	}
  	/**
  	 * Busca los correos de una partida de un empleado
  	 * @param IDE_GEEDP
  	 * @return
  	 */
  	public TablaGenerica getCorreoEmpleadoPepartamentoPartida(String IDE_GEEDP){
  		return utilitario.consultar("select detalle_gtcor,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado, " +
  				"ide_geedp from GTH_CORREO a, GEN_EMPLEADOS_DEPARTAMENTO_PAR b,GTH_EMPLEADO c " +
  				"where a.IDE_GTEMP = c.IDE_GTEMP and b.ide_gtemp = c.IDE_GTEMP " +
  				"and activo_geedp=TRUE and notificacion_gtcor=TRUE and ide_geedp in("+IDE_GEEDP+") ");
  	}
	
  	
  	/**
  	 * Metodo devuelve los empleado activo
  	 *   	 * @param IDE_GTEMP
  	 * @return
  	 */
  	public TablaGenerica getEmpleadoActivo(String IDE_GTEMP){
  		return utilitario.consultar("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES, "+ 
				"JUBILADO_GTEMP, " +
				"JUBILADO_INVALIDEZ_GTEMP " +
				"from GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE and IDE_GTEMP="+IDE_GTEMP);
  	}
  	/**
  	 * Funciones para Auxiliar de Vacaciones
  	 * @return
  	 */
  	public TablaGenerica getEmpleadosAnio(String ANIO) {
  		
  		return utilitario.consultar("SELECT IDE_GTEMP, APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) "
  				+ "|| ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
  				+ "DOCUMENTO_IDENTIDAD_GTEMP, fecha_ingreso_gtemp, FECHA_INGRESO_GRUPO_GTEMP, "
  				+ "cast(extract(year from to_date(FECHA_INGRESO_GRUPO_GTEMP::text, 'YYYY-MM-DD')) as int ) as anio_ingreso "
  				+ "FROM GTH_EMPLEADO "
  				+ "WHERE IDE_GTEMP IN (SELECT IDE_GTEMP FROM ASI_VACACION) and extract(year from to_date(fecha_ingreso_gtemp::text, 'YYYY-MM-DD')) >= '"+ANIO+"' "
  				+ "ORDER BY NOMBRES_APELLIDOS ASC");
  	}
  	
  	public ArrayList<String> getGroupIdVacacionEmpleado(String IDE_GTEMP) {
  		String query = "SELECT asvre.ide_asvac, '' "
  				+ "FROM asi_vacacion_resumen_empleado asvre "
  				+ "left join gen_empleados_departamento_par par on par.ide_geedp=asvre.ide_geedp_asvac "
  				+ "left join gen_partida_grupo_cargo gpgc on gpgc.ide_gepgc=par.ide_gepgc "
  				+ "where par.ide_gtemp ="+IDE_GTEMP+" "
  				+ "group by 1 "
  				+ "order by asvre.ide_asvac";
  		TablaGenerica tabData = utilitario.consultar(query);
  		
  		ArrayList<String> list = new ArrayList<String>();
  		for (int i =0; i< tabData.getTotalFilas(); i++) {
  			list.add(tabData.getValor(i, "ide_asvac"));
		}
  	
  	
  		return list;
  	}
  	
  	public TablaGenerica getResumenVacacionEmpleado(String IDE_ASVAC, String ANIO) {
  		String query = "WITH agrupar_ide_asvac AS("
  				+ "SELECT  periodo_asvre, max(ide_asvre) as id "
  				+ "FROM asi_vacacion_resumen_empleado "
  				+ "where ide_asvac in ("+IDE_ASVAC+") "
  				+ "group by 1"
  				+ "),"
  				+ "resumen_vacaciones AS ("
  				+ "SELECT asvre.ide_asvre, asvre.ide_asvac, par.ide_gtemp, asvre.periodo_asvre, split_part(asvre.periodo_asvre, ' - ', 1) as fecha_ingreso_asvre, "
  				+ "split_part(asvre.periodo_asvre, ' - ', 2) as fecha_finiquito_asvre, asvre.nro_dias_vacacion_asvre, asvre.nro_dias_tomados_asvre, asvre.nro_dias_pendientes_asvre, "
  				+ "asvre.activo_asvre,vac.activo_asvac, asvre.ide_periodo_asvre,asvre.ide_geedp_asvac,par.rmu_geedp,gpgc.titulo_cargo_gepgc, tem.ide_gttem, tem.detalle_gttem, "
  				+ "cast(extract(year from to_date(split_part(asvre.periodo_asvre, ' - ', 1), 'YYYY-MM-DD')) as int ) as anio_ingreso_periodo_asvre, "
  				+ "cast(extract(year from to_date(split_part(asvre.periodo_asvre, ' - ', 2), 'YYYY-MM-DD')) as int ) as anio_finiquito_periodo_asvre "
  				+ "FROM asi_vacacion_resumen_empleado asvre "
  				+ "left join asi_vacacion as vac on vac.ide_asvac = asvre.ide_asvac "
  				+ "left join gen_empleados_departamento_par par on par.ide_geedp=asvre.ide_geedp_asvac "
  				+ "left join gen_partida_grupo_cargo gpgc on gpgc.ide_gepgc=par.ide_gepgc "
  				+ "left join gth_tipo_empleado as tem on tem.ide_gttem = par.ide_gttem "
  				+ "join agrupar_ide_asvac as gidevac on gidevac.id = asvre.ide_asvre "
  				+ "where gpgc.ide_gegro not in(23) "
  				+ "ORDER BY IDE_ASVRE asc "
  				+ ") "
  				+ "SELECT * FROM resumen_vacaciones where extract(year from to_date(fecha_ingreso_asvre, 'YYYY-MM-DD')) <= '"+ANIO+"' "
  				+ "and extract(year from to_date(fecha_finiquito_asvre, 'YYYY-MM-DD')) >= '"+ANIO+"' "
  				+ "order by fecha_ingreso_asvre asc ";
  		
  		return utilitario.consultar(query);
  	}

  	public String getTotalDiasVacacionPorFechas(String IDE_GTEMP, String FECHA_INICIO, String FECHA_FIN){
  		String query = "select sum(dv.dia_descontado_asdev) as total, '' "
  				+ "from asi_permisos_vacacion_hext as pv "
  				+ "inner join asi_detalle_vacacion as dv on dv.ide_aspvh = pv.ide_aspvh "
  				+ "inner join asi_motivo as mt on mt.ide_asmot = pv.ide_asmot "
  				+ "where pv.ide_gtemp = "+IDE_GTEMP+" and dv.fin_semana_asdev = false and pv.fecha_desde_aspvh between '"+FECHA_INICIO+"' and '"+FECHA_FIN+"' "
  				+ "and aprobado_tthh_aspvh = true ";
  		
  		TablaGenerica tabTotalPermisos = utilitario.consultar(query);
  		
  		double dias_tomados = CConversion.CDbl(tabTotalPermisos.getValor("total"));
  		return utilitario.getFormatoNumero(dias_tomados, 2);
  	}
  	
}
