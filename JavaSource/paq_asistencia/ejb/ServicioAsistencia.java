package paq_asistencia.ejb;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.component.tabview.Tab;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Etiqueta;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Utilitario;



import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.component.tabview.Tab;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;


import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioAsistencia {
	private Utilitario utilitario=new Utilitario();

	
	public TablaGenerica getNovedadJustificacion(String columna_busqueda,String ide_a_buscar){
		TablaGenerica tab_nove_jus=new TablaGenerica();
		try {
			tab_nove_jus=utilitario.consultar("select * from ASI_NOVEDAD_JUSTIFICACION where "+columna_busqueda+"="+ide_a_buscar);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return tab_nove_jus;
	}
	
	public boolean desactivarPeriodoVacacion(String ide_asvac,String fecha_finiquito){
		if (utilitario.getConexion().ejecutarSql("update ASI_VACACION set ACTIVO_ASVAC=false, " +
				"fecha_finiquito_asvac=to_date('"+fecha_finiquito+"','yyyy-mm-dd') where IDE_ASVAC="+ide_asvac).isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	public String getSqlActivarPeriodoVacacion(String ide_asvac){
		String str="update ASI_VACACION set ACTIVO_ASVAC=true where IDE_ASVAC="+ide_asvac;
		return str;
	}

	public String getSqlConsultaVacacion(String ide_aspvh){
		String str="select ide_asvac,ide_gtemp,fecha_ingreso_asvac,activo_asvac from  asi_vacacion where ide_gtemp in (select ide_gtemp from asi_permisos_vacacion_hext where ide_aspvh="+ide_aspvh+") and activo_asvac =true";
		return str;
	}
	public boolean activarPeriodoVacacion(String ide_asvac){
		if (utilitario.getConexion().ejecutarSql("update ASI_VACACION set ACTIVO_ASVAC=true where IDE_ASVAC="+ide_asvac).isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public 	String retornaPeriodosVacacionXEmpleado(String ide_gtemp){
		String str="select ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac  "
												+ "FROM ASI_VACACION WHERE IDE_GTEMP="+ide_gtemp;
												return str;
			}
	
	
	public 	String retornaPeriodoVacacion(String ide_asvac){
		String str="select ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac  "
												+ "FROM ASI_VACACION WHERE IDE_ASVAC="+ide_asvac;
												return str;
			}
	
	
	
	public void crearPeriodoVacacion(String ide_gtemp,String fecha_ingreso){
		Tabla tab_asi_vac=new Tabla();
		tab_asi_vac.setTabla("ASI_VACACION", "IDE_ASVAC", -1);
		tab_asi_vac.setCondicion("IDE_ASVAC=-1");
		tab_asi_vac.ejecutarSql();
		
		tab_asi_vac.insertar();
		tab_asi_vac.setValor("IDE_GTEMP", ide_gtemp);
		tab_asi_vac.setValor("FECHA_INGRESO_ASVAC", fecha_ingreso);
		tab_asi_vac.setValor("ACTIVO_ASVAC", "true");
		tab_asi_vac.guardar();
		
//		if (utilitario.getConexion().guardarPantalla().isEmpty()){
//			return true;
//		}else{
//			return false;
//		}
		
	}
	
	public TablaGenerica getAsiVacacionActiva(String ide_gtemp){
		TablaGenerica tab_asi_vacacion=utilitario.consultar("select * from ASI_VACACION " +
				"where activo_asvac=true and IDE_GTEMP="+ide_gtemp);
		return tab_asi_vacacion;
	}
	public TablaGenerica getAsiVacacionMaximoPeriodo(String ide_gtemp){
		TablaGenerica tab_asi_vacacion=utilitario.consultar("select * from ( " +
				"select * from ASI_VACACION where IDE_GTEMP="+ide_gtemp+" " +
				"order by FECHA_INGRESO_ASVAC DESC " +
				")a " +
				" limit 1"); 
		return tab_asi_vacacion;
	}
				
								

	
	public String getConsultaAsistencia(String fecha_inicial,String fecha_final){
		String tab_asi_vacacion="select a.ide_aspvh,a.ide_asmot,a.ide_gtemp,a.ide_geedp,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,"
				+" titulo_cargo_gepgc,nom_sucu,detalle_geare,detalle_gttem,fecha_solicitud_aspvh,(case when aprobado_tthh_aspvh is true then 'Aprobado' else 'No Aprobado' end) as aprobado_talento_humano,"
				+" fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,tipo_aspvh,(case when tipo_aspvh=1 then 'Permisos Horas' when tipo_aspvh = 2 then 'Vacaciones' when tipo_aspvh =3 then 'Horas Extras' when tipo_aspvh =4 then 'Permisos por Dias'  end) as tipo_solicitud,"
				+" nro_documento_aspvh,nro_horas_aspvh,hora_desde_aspvh,hora_hasta_aspvh,aprobado_tthh_aspvh,aprobado_aspvh,"
				+" razon_anula_aspvh,documento_anula_aspvh,fecha_anula_aspvh "
				
				+", (select COALESCE(SUM(DIA_ACUMULADO_ASDEV),0) +  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as NRO_TOTALES_VACACIONES, "
				+"(select ( COALESCE(SUM(DIA_ACUMULADO_ASDEV),0)+  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0))-( COALESCE(SUM(DIA_DESCONTADO_ASDEV),0)+ COALESCE(SUM(DIA_SOLICITADO_ASDEV),0))   from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as DIAS_PENDIENTES "		
	
				+" from asi_permisos_vacacion_hext a"
				+" left join asi_motivo b on a.ide_asmot = b.ide_asmot"
				+" left join gth_empleado c on a.ide_gtemp = c.ide_gtemp"
				+" left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp"
				+" left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc"
				+" left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf"
				+" left join gen_area g on d.ide_geare = g.ide_geare"
				+" left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu"
				+" where fecha_solicitud_aspvh between '"+fecha_inicial+"' and '"+fecha_final+"' "
				+" order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc"; 
		return tab_asi_vacacion;
		
	}
	
	public String getConsultaAsistenciaJefeInmediato(String fecha_inicial,String fecha_final,Integer ide_geedp){
		String tab_asi_vacacion="select a.ide_aspvh,a.ide_asmot,a.ide_gtemp,a.ide_geedp,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,"
				+" titulo_cargo_gepgc,nom_sucu,detalle_geare,detalle_gttem,fecha_solicitud_aspvh,(case when aprobado_tthh_aspvh is true then 'Aprobado' else 'No Aprobado' end) as aprobado_talento_humano,"
				+" fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,tipo_aspvh,(case when tipo_aspvh=1 then 'Permisos Horas' when tipo_aspvh = 2 then 'Vacaciones' when tipo_aspvh =3 then 'Horas Extras' when tipo_aspvh =4 then 'Permisos por Dias'  end) as tipo_solicitud,"
				+" nro_documento_aspvh,nro_horas_aspvh,hora_desde_aspvh,hora_hasta_aspvh,aprobado_tthh_aspvh,aprobado_aspvh,"
				+" razon_anula_aspvh,documento_anula_aspvh,fecha_anula_aspvh, archivo_adjunto_aspvh"
				
				+", (select COALESCE(SUM(DIA_ACUMULADO_ASDEV),0) +  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as NRO_TOTALES_VACACIONES, "
				+"(select ( COALESCE(SUM(DIA_ACUMULADO_ASDEV),0)+  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0))-( COALESCE(SUM(DIA_DESCONTADO_ASDEV),0)+ COALESCE(SUM(DIA_SOLICITADO_ASDEV),0))   from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as DIAS_PENDIENTES, "		
				+"(select  COALESCE(SUM(numero_fines_semana_asdev),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as SABADOS_DOMINGOS"
								
				+" from asi_permisos_vacacion_hext a"
				+" left join asi_motivo b on a.ide_asmot = b.ide_asmot"
				+" left join gth_empleado c on a.ide_gtemp = c.ide_gtemp"
				+" left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp"
				+" left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc"
				+" left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf"
				+" left join gen_area g on d.ide_geare = g.ide_geare"
				+" left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu"
				+" where a.aprobado_aspvh=false and "
				+" fecha_solicitud_aspvh between '"+fecha_inicial+"' and '"+fecha_final+"' "
				+" and gen_ide_geedp2 = "+ide_geedp+"  order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc"; 
		return tab_asi_vacacion;
	}
	
	
	public String getConsultaAsistenciaTalentoHumano(String fecha_inicial,String fecha_final){
		String tab_asi_vacacion="select a.ide_aspvh,a.ide_asmot,a.ide_gtemp,a.ide_geedp,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,"
				+" titulo_cargo_gepgc,nom_sucu,detalle_geare,detalle_gttem,fecha_solicitud_aspvh,(case when aprobado_tthh_aspvh is true then 'Aprobado' else 'No Aprobado' end) as aprobado_talento_humano,"
				+" fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,tipo_aspvh,(case when tipo_aspvh=1 then 'Permisos Horas' when tipo_aspvh = 2 then 'Vacaciones' when tipo_aspvh =3 then 'Horas Extras' when tipo_aspvh =4 then 'Permisos por Dias'  end) as tipo_solicitud,"
				+" nro_documento_aspvh,nro_horas_aspvh,hora_desde_aspvh,hora_hasta_aspvh,aprobado_tthh_aspvh,aprobado_aspvh,"
				+" razon_anula_aspvh,documento_anula_aspvh,fecha_anula_aspvh, archivo_adjunto_aspvh"
				
				+", (select COALESCE(SUM(DIA_ACUMULADO_ASDEV),0) +  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as NRO_TOTALES_VACACIONES, "
				+"(select ( COALESCE(SUM(DIA_ACUMULADO_ASDEV),0)+  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0))-( COALESCE(SUM(DIA_DESCONTADO_ASDEV),0)+ COALESCE(SUM(DIA_SOLICITADO_ASDEV),0))   from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as DIAS_PENDIENTES, "		
				+"(select  COALESCE(SUM(numero_fines_semana_asdev),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as SABADOS_DOMINGOS"
								
				+" from asi_permisos_vacacion_hext a"
				+" left join asi_motivo b on a.ide_asmot = b.ide_asmot"
				+" left join gth_empleado c on a.ide_gtemp = c.ide_gtemp"
				+" left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp"
				+" left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc"
				+" left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf"
				+" left join gen_area g on d.ide_geare = g.ide_geare"
				+" left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu"
				+" where a.aprobado_aspvh=true and"
				+" fecha_solicitud_aspvh between '"+fecha_inicial+"' and '"+fecha_final+"' "
				+" order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc"; 
		return tab_asi_vacacion;
	}
	
	
	
	
	
	
	
	
	public String getConsultaAsistenciaLote(String fecha_inicial,String fecha_final){
		String tab_asi_vacacion="select a.ide_aspvh,a.ide_aspvh as Código,b.detalle_asmot,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp, "
				+ "fecha_solicitud_aspvh,fecha_desde_aspvh,fecha_hasta_aspvh,hora_desde_aspvh,hora_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,nro_horas_aspvh "
				+ "from asi_permisos_vacacion_hext a "
				+ "left join asi_motivo b on a.ide_asmot = b.ide_asmot "
				+ "left join gth_empleado c on a.ide_gtemp = c.ide_gtemp "
				+ "left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp "
				+ "left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc "
				+ "left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf "
				+ "left join gen_area g on d.ide_geare = g.ide_geare "
				+ "left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu "
				+ "where a.aprobado_aspvh=false and (aprobado_tthh_aspvh=false or aprobado_tthh_aspvh is null) and (anulado_aspvh=false or anulado_aspvh is null) and "
				+" fecha_solicitud_aspvh between '"+fecha_inicial+"' and '"+fecha_final+"' "
				+" order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc";
    			 return tab_asi_vacacion;		
	}
	
	
	public String getConsultaAsistenciaJefeInmediatoLote(String fecha_inicial,String fecha_final,Integer ide_geedp){
		
		
		String tab_asi_vacacion="select a.ide_aspvh,a.ide_aspvh as Código,b.detalle_asmot,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp, "
				+ "fecha_solicitud_aspvh,fecha_desde_aspvh,fecha_hasta_aspvh,hora_desde_aspvh,hora_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,nro_horas_aspvh "
						+ "from asi_permisos_vacacion_hext a "
				+ "left join asi_motivo b on a.ide_asmot = b.ide_asmot "
				+ "left join gth_empleado c on a.ide_gtemp = c.ide_gtemp "
				+ "left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp "
				+ "left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc "
				+ "left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf "
				+ "left join gen_area g on d.ide_geare = g.ide_geare "
				+ "left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu "
				+ "where a.aprobado_aspvh=false and  (aprobado_tthh_aspvh=false or aprobado_tthh_aspvh is null) and (anulado_aspvh=false or anulado_aspvh is null) and "
				+" fecha_solicitud_aspvh between '"+fecha_inicial+"' and '"+fecha_final+"' "
				+" and gen_ide_geedp2 = "+ide_geedp+"  order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc";
    			 return tab_asi_vacacion;
	}
	
	
	
	
	
	
	//Devuelve el el periodo d vacaciones final cuando no existen datos
	
	
/////////METODO PROBAR
	
public String getDiasLaboradosUltimoPeriodoVacacionCT(String IDE_GTEMP,int ide_gttem,String ide_asvac,String fecIniVacacion,String fecFinVacacion,
												int bandEjecutoRol,int diasExesoLiquidacion){
	
int bandEntrada=0;
int bandSalida=0;
double valorTotal=0;
double dias_pendientes=0.0;
int nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
TablaGenerica tabla_vacacion=utilitario.consultar(retornaPeriodoVacacion(ide_asvac));
boolean estado_periodo=false;
//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
int nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac);
//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
int ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

// Recalculo los valores para q no se pasen de 360 dias al año
//nde = getDiasEmpleadoXAnio(nde, nda);
//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

// Obtener los periodos de cada empleado 1,2,3 etc
List<Integer> peridos = getPeriodos(nda, nde);


// Obtengo los periodos para el ajuste de dias pendientes del
// empleado hasta el 30 de abril del 2017
List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);






//System.out.println("Tipo empleado: "+ide_gttem);
double numeroDiasVacacionXAnio=0.0;
int p_asi_dias_max_vacaciones_codigo_trabajo=0,p_asi_dias_max_vacaciones_losep=0;
double division=0.0;
if (ide_gttem == 1) {
	// Asigno el numero de dias max de vacaciones al año 15 dias
	numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
	// Los dias maximo q puede acumular 45 dias por tres periodos
	p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
	
//Aumentamos dia por trabajar mas de 5 anios
	if(peridos.size()>5){
		double anio_periodo=peridos.size();
		 division=anio_periodo/5;
		nde=nde+(int)division;
	}
	

}

if (ide_gttem == 2) {
	// Asigno el numero de dias max de vacaciones al año 15 dias
	numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
	// Los dias maximo q puede acumular 45 dias por tres periodos
	p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));

}







// Obtengo el total de mis dias pendientes al restar los dias
// acumulados-dias tomados
double sumatotalDiasGenerados = 0.0;
// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
// que le corresponde y los dias generados hasta el presente
List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
// que le corresponde y los dias generados hasta el 30 de abril de

List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
 double sumatotal_vacaciones = 0.0;
// Asigno la sumatoria de total de mis dias acumulados a vacacion
// desde la fecha de ingreso hasta la fecha de hoy
 
 BigDecimal num1 = new BigDecimal(0);

for (int i = 0; i < vacacionesPeriodo.size(); i++) {
	sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
         	
}

//System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
 double sumatotal_vacacionesCalculoInicial = 0.0;

// Asigno la sumatoria de total de mis dias acumulados a vacacion
// desde la fecha de ingreso hasta el 30 de Abril de 2017
for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
	sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
}
//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

// Devuelve el numero de dias pendientes obtenidos de excel hasta el
// 30 de abril

 
           double numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac);
			double totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac);
			double nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac);
			double diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac);
			
			
			
			
			  if(numeroDiasTomadosInicial!=0)
			  {
				
				if (numeroDiasTomadosInicial < 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
		            //valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
					utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
        totalNumeroDiasAjuste=valor;
						

				}
				if (numeroDiasTomadosInicial > 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
		            //valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
												utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
	                totalNumeroDiasAjuste=valor;

					}

			  
			  }
				


			double totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac);
				double numeroDiasTomados=totalNumeroDiasAjuste;
			double numeroDiasTomadosTemporal = numeroDiasTomados;

			// suma el valor de los dias descontados de la tabla
			// asi_detalle_vacacion
			// Aqui se encuentra el cuadre
			double dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
			int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
			double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);

			
		
			
		   double numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
			
			
			double resultado_descuento = descuento + numeroDiasTomados;
	

			if (ide_gttem == 2) {

				double nro_dias_ajuste_periodo_asvac = 0;
						TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
								+ ide_asvac+" and activo_asvac=true");

						if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 60) {
							//System.out.println("Ingreso a descuento mayor a 60");
							
							BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
							BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
							BigDecimal descuento1= new BigDecimal(descuento);
							BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
							BigDecimal  dias= new BigDecimal(60);
							BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
							BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

							if (calculoPasaDiasPendientes.doubleValue()>=0.01) {
						
							
							TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
			          utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
			+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
						
							}
			          nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;

			          
						}
					}

	if (ide_gttem == 1) {

						double nro_dias_ajuste_periodo_asvac = 0;
						TablaGenerica tab_codigo_vacacion = utilitario
								.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
										+ ide_asvac+" and activo_asvac=true");
					

						if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 45) {
							
						//	System.out.println("Ingreso a descuento mayor a 45");

							
							BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
							BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
							BigDecimal descuento1= new BigDecimal(descuento);
							BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
							BigDecimal  dias= new BigDecimal(45);
							BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
							BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

							if (calculoPasaDiasPendientes.doubleValue()>=0.01) {

							TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
							utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
							+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
							}
							nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;
															
						}
					}
	
	       numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
		   numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

			/*
			 * Tomo los parametros creados para realizar los calculos de los
			 * dias pendientes calculo de vacaciones dependiendo si el empleado
			 * pertenece a la losep o al codigo de trabajo
			 */


			List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);
			//for (int i = 0; i < 1; i++) {
				//System.out.println("vacacionesTomadas: " + vacacionesTomadas);
		//	}

			String periodoNuevo="";
			String periodoNuevoEntrada="",periodoNuevoSalida="";
			int valorDiasXAnio=0;
			List<Double[]> matriz = new ArrayList<Double[]>();
				
				Integer dimension = vacacionesPeriodo.size();
				for (Integer i = 0; i < dimension; i++) {
					Double[] obj = new Double[5];
					obj[0] = i.doubleValue() + 1; // periodo
					obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
				//	obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
					obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
				 //   obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
					obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
				//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
					obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
				  //  obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;

				
				
				}
			
			/**
			 * Obtencion de dias pendientes del empleado
			 */
			// System.out.println("numero dias pendientes vacacion: "+
			// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
			
				
				
				
				boolean bandAjusteBaseImponible=false;
				String baseImponibleVacacion="";
				double baseImponibleVacacionValor=0.0,valorVacacion=0.0;
				int anioInicioVacacion=0,anioFinVacacion=0;
				List<Integer> peridosTemp = getPeriodos(nda, nde);
				List<Double> vacacionesPeriodoTemp = getVacacionesXPeriodo(nda, nde, peridosTemp, numeroDiasVacacionXAnio);
				List<Double> vacacionesTomadasTemp = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridosTemp, numeroDiasVacacionXAnio);
				String base_imponible="";
				int cont=0;
				Integer dimensionTemp = vacacionesPeriodoTemp.size();
				Date nuevaFecha = new Date();
				Date nuevaFechaInicio = new Date();
				Date nuevaFechaFin = new Date();

				Calendar cal = Calendar.getInstance(); 
                 cal.setTime(utilitario.getFecha(fecIniVacacion)); 
             	
                 Calendar calFechaFin = Calendar.getInstance(); 
                 calFechaFin.setTime(utilitario.getFecha(fecFinVacacion)); 
                 
                 int mes=0,anio=0;
			StringBuilder str_ide_mes = new StringBuilder();
			StringBuilder str_ide_anio = new StringBuilder();
			StringBuilder	str_ide_mes_cambio = new StringBuilder();
			StringBuilder	str_ide_anio_cambio = new StringBuilder();
			double total_dias=0;
				for (int i = 0; i < utilitario.getDiferenciaMeses(utilitario.getFecha(fecIniVacacion), utilitario.getFecha(fecFinVacacion)); i++) {
					    cont++; 
						cal.add(Calendar.MONTH, i);
		                 nuevaFecha = cal.getTime();
		        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));

		        	     if (cont>=i) {
							
						}else {
							 str_ide_mes.append(",");
						}
				}
				
				
				 double calculoBaseNueva=0.0;
			double baseImponibleVacacionValorTemp1=0.0;
				double baseImponibleVacacionValorTemp2=0.0;
				valorVacacion=0.0;
					for (Integer i = 0; i < dimensionTemp; i++) {
						cont++;
						Double[] obj = new Double[5];
						obj[0] = i.doubleValue() + 1; // periodo
						obj[1] = vacacionesPeriodoTemp.get(i); // dias vacacion x periodo
						//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
						obj[2] = vacacionesTomadasTemp.get(i); // dias vacacion tomadas x periodo
					//	obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
						obj[3] = vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i); // dias vacacion penientes x periodo
					//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
						obj[4] = vacacionesPeriodoTemp.get(i)+ vacacionesPeriodoTemp.get(i);
					//	obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;


						
						int anioInicio=0;
						
						if ((vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i))!=0.0 || (vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i))!=0) {
							String fechaIniTemp="",fechaFinTemp="";
							cal.add(Calendar.YEAR, i);
							nuevaFechaInicio=cal.getTime();
							anioInicio=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaInicio));
							calFechaFin.add(Calendar.YEAR, i);
							nuevaFechaFin=calFechaFin.getTime();
							int anioFin=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaFin));
							baseImponibleVacacionValorTemp1=0.0;
							baseImponibleVacacionValorTemp2=0.0;

							/*if (anioInicio==anioFin) {
								TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
								str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
							}else {
								TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
								TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFin+"%' ");
								str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
								str_ide_anio.append(",");
								str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
							}*/
							if (i==0){
								double valorDiasPendientes=vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i);
								if ((i+1)==dimension) {
									//activo es igual a false
											periodoNuevoEntrada=fecIniVacacion;	
											 periodoNuevoSalida=fecFinVacacion;

											 int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
											 int mesVacacion = utilitario.getMes(fecIniVacacion);
											 int diaVacacion = utilitario.getDia(fecIniVacacion);
										String nuevaFechaEntrada="";
										String mesTempEntrada="",diaTempEntrada="";
										 if (mesVacacion<10) {
											 mesTempEntrada="0"+mesVacacion;
										}else {
											mesTempEntrada=""+mesVacacion;
										}
										
										 
										 if (diaVacacion<10) {
											 diaTempEntrada="0"+diaVacacion;
										}else {
												diaTempEntrada=""+diaVacacion;
											}

										periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;

											
										    int anioVacacionSalida=utilitario.getAnio(fecIniVacacion)+(i+1);
											String mesTemp="",diaTemp="";
											 if (mesVacacion<10) {
												mesTemp="0"+mesVacacion;
											}else{
												mesTemp=""+mesVacacion;
											}
											
											
											 if (diaVacacion<10) {
													diaTemp="0"+diaVacacion;
												}else {
													diaTemp=""+diaVacacion;
											}
											 String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
											 periodoNuevoSalida=fecFinVacacion;	
											
											 
											 
											 
												anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
												anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
												if (anioInicioVacacion==anioFinVacacion) {
													TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
													str_ide_anio= new StringBuilder();
													str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
												}else {
													TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
													TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
													str_ide_anio= new StringBuilder();
													str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
													str_ide_anio.append(",");
													str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
												}
													cont=0;
													cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
													str_ide_mes = new StringBuilder();
													str_ide_mes_cambio = new StringBuilder();
													str_ide_anio_cambio = new StringBuilder();
													str_ide_anio = new StringBuilder();


														for (int x = 0; x < ((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))); x++) {
															    cont++; 
															    boolean bandera1=false,bandera2=false;
																cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
																cal.add(Calendar.MONTH, x);
												                 nuevaFecha = cal.getTime();
												                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
													        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
																		
																		
																		

																}else {
																	str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
																	//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

																}

												        	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
												        	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
																	cal.add(Calendar.MONTH, x+1);
													                 nuevaFecha = cal.getTime();
													                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
													                	if (cont>=(utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))) {
																			 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));

																		}else {
														                	 str_ide_mes.append(",");

																		}
													                	 
													                	// str_ide_anio.append(",");
													                 }else {
																		 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
																	}
																	 
																}else {
																	if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
																		str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
																	}else {
																		str_ide_mes_cambio.append(",");
																		//str_ide_anio_cambio.append(",");
																	}
																	
																	
																	mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
																	anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
																}
												 		}
												
														
												
												
												
												periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
												if (bandEjecutoRol==1) {
													if (diasExesoLiquidacion>=0) {
														//total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)));
														total_dias=getDias360(periodoNuevoEntrada, periodoNuevoSalida);
													}}else {


														int a,b;
														Calendar calTemp = Calendar.getInstance(); 

														calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
														calTemp.add(Calendar.MONTH, 0);
														Date nuevaFechaTemp = new Date();
														nuevaFechaTemp = calTemp.getTime();
														String fechaNuevaTemp="";
														int ultimoDia= utilitario.getDia(utilitario.getUltimoDiaMesFecha(utilitario.DeDateAString(nuevaFechaTemp)));
													
														fechaNuevaTemp=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaTemp))+"-"+utilitario.getMes(utilitario.DeDateAString(nuevaFechaTemp))+"-"+ultimoDia;
													  int totalMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
														//total_dias=totalMeses*30;
														//total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(fechaNuevaTemp)));
														total_dias=getDias360(periodoNuevoEntrada, fechaNuevaTemp);
													}
														
												 if (total_dias>360) {
													 total_dias=360;
												}
								
												 
												 
												 
												 
												 calculoBaseNueva=0.0;
												
													if (str_ide_mes.length()!=0 || str_ide_mes_cambio.length()!=0) {
														baseImponibleVacacionValor=0.0;
														baseImponibleVacacionValorTemp1=0.0;
														baseImponibleVacacionValorTemp2=0.0;
														
														if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
															TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
															String anioParte1=tabAnio1.getValor("IDE_GEANI");
														/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
																	+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																					+ "a.ide_nrdtn in(2,4)");*/
															
															TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
																	+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
																	+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
																	+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
																	+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
																if(valor_base_mes_anterior1.getTotalFilas()>0){
																	for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
																		if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																			baseImponibleVacacionValorTemp1+=0;
																		}else {
																		baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
																	}
																		//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
																	}
																}
																
															
														}else {

														
														
														TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
														String anioParte1=tabAnio1.getValor("IDE_GEANI");
														/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
																+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																		+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																				+ "a.ide_nrdtn in(2,4)");*/
														
														TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
																+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
																+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
																+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
																+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
																+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
															if(valor_base_mes_anterior1.getTotalFilas()>0){
																for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
																	if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																		baseImponibleVacacionValorTemp1+=0;
																	}else {
																	baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
																}
																	//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
																}
															}
														
															TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
															String anioParte2=tabAnio2.getValor("IDE_GEANI");
															
															/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
																	+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
																			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																					+ "a.ide_nrdtn in(2,4)");*/
															

															TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
																	+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
																	+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
																	+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
																	+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
														
															if(valor_base_mes_anterior2.getTotalFilas()>0){
																for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
																	if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																		baseImponibleVacacionValorTemp2+=0;
																	}else {
																	baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
																}
																	//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
																}
															}
													}										
													}
														 
												 
												 
													
												
													
													
													
													
													
													if (bandEjecutoRol==1) {
														if (diasExesoLiquidacion>0) {
	///////////////////////////////		para ejecutar ajuste					
															
															int ajuste=0;
															if (utilitario.getMes(fecFinVacacion)!=0) {
															int mesFinAjuste=utilitario.getMes(fecFinVacacion);
															int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

															TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
															int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
															

															TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
																	+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																					+ "a.ide_nrdtn in(2,4)");
															if(valor_base_mes_liquidacion.getTotalFilas()>0){
																int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
																if (ultimoDia>=31) {
																	ultimoDia=30;
																}
																String nuevaFec=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-01";
																periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
																//double diferenciaDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(fecFinVacacion),utilitario.DeStringADate(nuevaFec));
																double diferenciaDias=getDias360(nuevaFec, periodoNuevoSalida)+1;
																calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

															}else {
																
																
																calculoBaseNueva=0;
															}
															
														}			
															
													
														}//Final de ajusteel
						
														else{
															
															int ajuste=0;
															if (utilitario.getMes(fecFinVacacion)!=0) {
															int mesFinAjuste=utilitario.getMes(fecFinVacacion);
															int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

															TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
															int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
															

															TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
																	+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																					+ "a.ide_nrdtn in(2,4)");
															if(valor_base_mes_liquidacion.getTotalFilas()>0){
																calculoBaseNueva=Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"));
															}
															
															}
														}
														
														
														
													}//Si ejecuto rol
													
													
													else {
														
														//Si no ha ejecutado rol entonces deja en cero 
														calculoBaseNueva=0.0;
													}
																				 
										}///if == dimension+1
								else{
									//Cuando tiene mas de una iteraciones
//***************************************************************************************************************************************										
									
									
									//activo es igual a false
									periodoNuevoEntrada=fecIniVacacion;	
									 periodoNuevoSalida=fecFinVacacion;

									 int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
									 int mesVacacion = utilitario.getMes(fecIniVacacion);
									 int diaVacacion = utilitario.getDia(fecIniVacacion);
								String nuevaFechaEntrada="";
								String mesTempEntrada="",diaTempEntrada="";
								 if (mesVacacion<10) {
									 mesTempEntrada="0"+mesVacacion;
								}else {
									mesTempEntrada=""+mesVacacion;
								}
								
								 
								 if (diaVacacion<10) {
									 diaTempEntrada="0"+diaVacacion;
								}else {
										diaTempEntrada=""+diaVacacion;
									}

								periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;

									
								    int anioVacacionSalida=utilitario.getAnio(fecIniVacacion)+(i+1);
									String mesTemp="",diaTemp="";
									 if (mesVacacion<10) {
										mesTemp="0"+mesVacacion;
									}else{
										mesTemp=""+mesVacacion;
									}
									
									
									 if (diaVacacion<10) {
											diaTemp="0"+diaVacacion;
										}else {
											diaTemp=""+diaVacacion;
									}
									 String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
									 periodoNuevoSalida=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(nuevaFechaIngreso), -1));	
									
									 
									 
									 
										anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
										anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
										if (anioInicioVacacion==anioFinVacacion) {
											TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
											str_ide_anio= new StringBuilder();
											str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
										}else {
											TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
											TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
											str_ide_anio= new StringBuilder();
											str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
											str_ide_anio.append(",");
											str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
											
											cont=0;
											cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
											str_ide_mes = new StringBuilder();
											str_ide_mes_cambio = new StringBuilder();
											str_ide_anio_cambio = new StringBuilder();
											str_ide_anio = new StringBuilder();


												for (int x = 0; x < ((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))); x++) {
													    cont++; 
													    boolean bandera1=false,bandera2=false;
														cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
														cal.add(Calendar.MONTH, x);
										                 nuevaFecha = cal.getTime();
										                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
											        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
																
																
																

														}else {
															str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
															//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

														}

										        	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
										        	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
															cal.add(Calendar.MONTH, x+1);
											                 nuevaFecha = cal.getTime();
											                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
											                	 str_ide_mes.append(",");
											                	// str_ide_anio.append(",");
											                 }else {
																 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
															}
															 
														}else {
															if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
																str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
															}else {
																str_ide_mes_cambio.append(",");
																//str_ide_anio_cambio.append(",");
															}
															
															
															mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
															anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
														}
										 		}
										
												
												
										}
										
										periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
										 total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
										 
										 if (total_dias>360) {
											 total_dias=360;
										}
						
										 
										 
										 
										 
										 calculoBaseNueva=0.0;
											if ((i+1)==dimension){}else{
											if ((str_ide_mes.toString()!=null || !str_ide_mes.toString().equals("") || !str_ide_mes.toString().isEmpty()) && (str_ide_mes_cambio.toString()!=null || !str_ide_mes_cambio.toString().equals("") || !str_ide_mes_cambio.toString().isEmpty()) ) {
												baseImponibleVacacionValor=0.0;
												baseImponibleVacacionValorTemp1=0.0;
												baseImponibleVacacionValorTemp2=0.0;
												
												if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
													TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
													String anioParte1=tabAnio1.getValor("IDE_GEANI");
												/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");*/
													
													TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
															+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
															+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
															+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
															+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
														if(valor_base_mes_anterior1.getTotalFilas()>0){
															for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
																if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																	baseImponibleVacacionValorTemp1+=0;
																}else {
																baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
															}
																//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
															}
														}
														
														baseImponibleVacacionValorTemp2=0.0;
														
												}else {

												
												
												TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
												String anioParte1=tabAnio1.getValor("IDE_GEANI");
												/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
														+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																		+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																		+ "a.ide_nrdtn in(2,4)");*/
												
												TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
														+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
														+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
														+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
														+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
														+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
													if(valor_base_mes_anterior1.getTotalFilas()>0){
														for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
															if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																baseImponibleVacacionValorTemp1+=0;
															}else {
															baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
														}
															//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
															
														}
													}
//Sigue ahi												
													TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
													String anioParte2=tabAnio2.getValor("IDE_GEANI");
													
													/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");*/
													

													TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
															+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
															+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
															+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
															+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
												
													if(valor_base_mes_anterior2.getTotalFilas()>0){
														for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
															if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																baseImponibleVacacionValorTemp2+=0;
															}else {
															baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
														}
															//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
														}
													}
											}										
											}
											}		 
										 
										 
											//calculoBaseNueva=0.0;

											
											
											
											
										/*	if (bandEjecutoRol==1) {
												if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste												
													int ajuste=0;
													if (utilitario.getMes(fechaFinTemp)!=0) {
													int mesFinAjuste=utilitario.getMes(fechaFinTemp);
													int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

													TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
													int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
													

													TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");
													if(valor_base_mes_liquidacion.getTotalFilas()>0){
														String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
														double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
													calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

													}else {
													}
													
												}			
													
													
													
													
													
												}//Final de ajusteel
												else {
													int ajuste=0;
													if (utilitario.getMes(fechaFinTemp)!=0) {
													int mesFinAjuste=utilitario.getMes(fechaFinTemp);
													int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

													TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
													int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
													

													TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");
													if(valor_base_mes_liquidacion.getTotalFilas()>0){
													calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro")));

													}else {
													}
													
												}	
												}
												
												
												
												
												
											}else {
												calculoBaseNueva=0.0;
											}
											
											*/
											
											
											
											
											
											
											
											
										
									/*		
										 
										int ajuste=0;
												if (utilitario.getMes(fechaFinTemp)!=0) {
												int mesFinAjuste=utilitario.getMes(fechaFinTemp);
												int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

												TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
												int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
												

												TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
														+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																		+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																		+ "a.ide_nrdtn in(2,4)");
												if(valor_base_mes_liquidacion.getTotalFilas()>0){
													String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
													double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
												calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

												}else {
												}
												
											}										
									
									
									*/
									
									
									
									
									
									
									
									
									
									
									
									
									
									
									
									//Este es para cuando contiene mas de dos periodos 
//***9*9*9*********9*****************************************************************************************************************************									
									
								}
								
								
								
							}else {

								if ((i+1)==dimension) {
									//ITERACIONES DESDE LA 1 EN ADELANTE	
									anioInicio=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaInicio));

									 int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
									 int mesVacacion = utilitario.getMes(fecIniVacacion);
									 int diaVacacion = utilitario.getDia(fecIniVacacion);
								String nuevaFechaEntrada="";
								String mesTempEntrada="",diaTempEntrada="";
								 if (mesVacacion<10) {
									 mesTempEntrada="0"+mesVacacion;
								}else {
									mesTempEntrada=""+mesVacacion;
								}
								
								 
								 if (diaVacacion<10) {
									 diaTempEntrada="0"+diaVacacion;
								}else {
										diaTempEntrada=""+diaVacacion;
									}

								periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;

									
								    int anioVacacionSalida=utilitario.getAnio(fecFinVacacion);
									String mesTemp="",diaTemp="";
									 if (mesVacacion<10) {
										mesTemp="0"+mesVacacion;
									}else{
										mesTemp=""+mesVacacion;
									}
									
									
									 if (diaVacacion<10) {
											diaTemp="0"+diaVacacion;
										}else {
											diaTemp=""+diaVacacion;
									}
									 String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
									 
									 if (utilitario.getDia(fecFinVacacion)>=31) {
										 periodoNuevoSalida=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-30";	

									}else {
										 periodoNuevoSalida=fecFinVacacion;	

									}
									
									
									

									
									anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
									anioFinVacacion=utilitario.getAnio(fecFinVacacion);
									//periodoNuevoSalida=fecFinVacacion;
									if (anioInicioVacacion==anioFinVacacion) {
										TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
										str_ide_anio= new StringBuilder();
										str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
									}else {
										TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
										TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
										str_ide_anio= new StringBuilder();
										str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
										str_ide_anio.append(",");
										str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
									}

									cont=0;
									cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
									str_ide_mes = new StringBuilder();
									str_ide_mes_cambio = new StringBuilder();
									str_ide_anio_cambio = new StringBuilder();
									str_ide_anio = new StringBuilder();
									//valor elimina dias
									int valorDiferenciaMes=0;
									if (utilitario.getMes(periodoNuevoEntrada)==utilitario.getMes(fecFinVacacion)) {
										valorDiferenciaMes=0;
									}else {
										valorDiferenciaMes=((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))));
									}
									
													
										for (int x = 0; x < valorDiferenciaMes; x++) {
										    cont++; 
										    boolean bandera1=false,bandera2=false;
											cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
											cal.add(Calendar.MONTH, x);
							                 nuevaFecha = cal.getTime();
							                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
								        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
													
													
													

											}else {
												str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
												//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

											}

							        	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
							        	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
												cal.add(Calendar.MONTH, x+1);
								                 nuevaFecha = cal.getTime();
								                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
								                		if (cont>=(utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))) {
															 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));

														}else {
										                	 str_ide_mes.append(",");

														}
								                	// str_ide_anio.append(",");
								                 }else {
													 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
												}
												 
											}else {
												if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
													str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
												}else {
													str_ide_mes_cambio.append(",");
													//str_ide_anio_cambio.append(",");
												}
												
												
												mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
												anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
											}
							 		}
									
				
									
									
										/*for (int x = 0; x < valorDiferenciaMes; x++) {
											    cont++; 
												cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
												cal.add(Calendar.MONTH, x);
								                 nuevaFecha = cal.getTime();
								        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));

								        	     if (cont<utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))) {
													 str_ide_mes.append(",");
												}else {
													mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));

												}
										}*/
										total_dias=0;
										int valorMeses=0;
										double valorMesesTemp=0.0;
										total_dias=0;
										//total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
										//total_dias=utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));


										
										if (bandEjecutoRol==1) {
											if (diasExesoLiquidacion>=0) {
												total_dias=0;
												valorMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
												valorMesesTemp=30*valorMeses;
												total_dias=valorMesesTemp+utilitario.getDia(periodoNuevoSalida);
												total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)));
												Calendar calTemp = Calendar.getInstance(); 
												calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
												Date nuevaFechaTemp = new Date();
												nuevaFechaTemp = calTemp.getTime();
												total_dias=getDias360(periodoNuevoEntrada,periodoNuevoSalida);
												
											
												
											}
										}else {
											total_dias=0;
											valorMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
											valorMesesTemp=30*valorMeses;
											total_dias=valorMesesTemp;
											Calendar calTemp = Calendar.getInstance(); 
											calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
											calTemp.add(Calendar.MONTH, -1);
											Date nuevaFechaTemp = new Date();
											nuevaFechaTemp = calTemp.getTime();
										
											int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(utilitario.DeDateAString(nuevaFechaTemp)));
											if (ultimoDia>30) {
												ultimoDia=30;
											}
											
											String fechaNuevaTemp=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaTemp))+"-"+utilitario.getMes(utilitario.DeDateAString(nuevaFechaTemp))+"-"+ultimoDia;

											total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(fechaNuevaTemp)));
											total_dias=getDias360(periodoNuevoEntrada,fechaNuevaTemp);

											//total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), 
										}
										
										
										
//***********************************************************************************************************************************************								
										
										
										
										
										
										 if (total_dias>360) {
											 total_dias=360;
										}
						
										 
										 
										 
										 
										 calculoBaseNueva=0.0;
										//	if ((i+1)==dimension){}else{
											if (str_ide_mes.length()!=0 && str_ide_mes_cambio.length()!=0) {
												baseImponibleVacacionValor=0.0;
												baseImponibleVacacionValorTemp1=0.0;
												baseImponibleVacacionValorTemp2=0.0;
												
												if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
													TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
													String anioParte1=tabAnio1.getValor("IDE_GEANI");
												/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");*/
													
													TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
															+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
															+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
															+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
															+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
													
													
														if(valor_base_mes_anterior1.getTotalFilas()>0){
															for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
																if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																	baseImponibleVacacionValorTemp1+=0;
																}else {
																baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
															}
																//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
															}
														}
														
														baseImponibleVacacionValorTemp2=0.0;
														
												}else {

												
												
												TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
												String anioParte1=tabAnio1.getValor("IDE_GEANI");
												/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
														+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
																+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																		+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																		+ "a.ide_nrdtn in(2,4)");*/
												
												TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
														+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
														+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
														+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
														+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
														+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
													if(valor_base_mes_anterior1.getTotalFilas()>0){
														for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
															if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																baseImponibleVacacionValorTemp1+=0;
															}else {
															baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
														}
															//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
														}
													}
												
													TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
													String anioParte2=tabAnio2.getValor("IDE_GEANI");
													
													/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");*/
													

													TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
															+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
															+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
															+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
															+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
												
													if(valor_base_mes_anterior2.getTotalFilas()>0){
														for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
															if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
																baseImponibleVacacionValorTemp2+=0;
															}else {
															baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
														}
															//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
														}
													}
											}										
											}
											//}		 
										 
										 
											
											
											
											
											
											if (bandEjecutoRol==1) {
												if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste					
													
													int ajuste=0;
													if (utilitario.getMes(fecFinVacacion)!=0) {
													int mesFinAjuste=utilitario.getMes(fecFinVacacion);
													int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

													TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
													int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
													

													TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");
													if(valor_base_mes_liquidacion.getTotalFilas()>0){
														int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
														if (ultimoDia>=31) {
															ultimoDia=30;
														}
														String nuevaFec=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-01";
														periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
														double diferenciaDias=getDias360(nuevaFec, fecFinVacacion);
														calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

													}else {
														
														
														calculoBaseNueva=0;
													}
													
												}			
													
											
												}//Final de ajusteel
				
												else{
													
													int ajuste=0;
													if (utilitario.getMes(fecFinVacacion)!=0) {
													int mesFinAjuste=utilitario.getMes(fecFinVacacion);
													int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

													TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
													int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
													

													TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");
													valor_base_mes_liquidacion.imprimirSql();
													if(valor_base_mes_liquidacion.getTotalFilas()>0){
														calculoBaseNueva=Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"));
													}
													
													}
												}
												
												
												
											}//Si ejecuto rol
											
											
											
											
											//fvgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg
											
											
											
											
									/*		if (bandEjecutoRol==1) {
												if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste												
													
													int ajuste=0;
													if (utilitario.getMes(fechaFinTemp)!=0) {
													int mesFinAjuste=utilitario.getMes(fechaFinTemp);
													int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

													TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
													int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
													

													TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
															+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																			+ "a.ide_nrdtn in(2,4)");
													if(valor_base_mes_liquidacion.getTotalFilas()>0){
														int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
														if (ultimoDia>=31) {
															ultimoDia=30;
														}
														String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-"+ultimoDia;
														if (nuevaFec.compareTo(periodoNuevoSalida)<=0) {
															periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
														}
														double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+1;
														calculoBaseNueva=-(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

													}else {
													}
													
												}			
													
													
													
													
													
												}//Final de ajusteel
				
												
												
											}else {
												calculoBaseNueva=0.0;
											}							
											
											
											*/
											
											
											
											
											
											
											
											
										 
									/*	int ajuste=0;
												if (utilitario.getMes(fechaFinTemp)!=0) {
												int mesFinAjuste=utilitario.getMes(fechaFinTemp);
												int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

												TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
												int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
												

												TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
														+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
																+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
																		+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
																		+ "a.ide_nrdtn in(2,4)");
												if(valor_base_mes_liquidacion.getTotalFilas()>0){
													String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
													double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
												calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

												}else {
												}
												
											}	*/									
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
								
//****************************************************************************************************************************************************								
								
								
								
								}else {
												
									//activo es igual a false
									periodoNuevoEntrada=fecIniVacacion;	
									 periodoNuevoSalida=fecFinVacacion;

									 int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
									 int mesVacacion = utilitario.getMes(fecIniVacacion);
									 int diaVacacion = utilitario.getDia(fecIniVacacion);
								String nuevaFechaEntrada="";
								String mesTempEntrada="",diaTempEntrada="";
								 if (mesVacacion<10) {
									 mesTempEntrada="0"+mesVacacion;
								}else {
									mesTempEntrada=""+mesVacacion;
								}
								
								 
								 if (diaVacacion<10) {
									 diaTempEntrada="0"+diaVacacion;
								}else {
										diaTempEntrada=""+diaVacacion;
									}

								periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;

									
								    int anioVacacionSalida=utilitario.getAnio(fecIniVacacion)+(i+1);
									String mesTemp="",diaTemp="";
									 if (mesVacacion<10) {
										mesTemp="0"+mesVacacion;
									}else{
										mesTemp=""+mesVacacion;
									}
									
									
									 if (diaVacacion<10) {
											diaTemp="0"+diaVacacion;
										}else {
											diaTemp=""+diaVacacion;
									}
									 String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
									 periodoNuevoSalida=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(nuevaFechaIngreso), -1));	
									
									 
						
								
								
							
							
						
						
									anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
									anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
									if (anioInicioVacacion==anioFinVacacion) {
										TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
										str_ide_anio= new StringBuilder();
										str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
									}else {
										TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
										TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
										str_ide_anio= new StringBuilder();
										str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
										str_ide_anio.append(",");
										str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
										
										cont=0;
										cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
										str_ide_mes = new StringBuilder();
										str_ide_mes_cambio = new StringBuilder();
										str_ide_anio_cambio = new StringBuilder();
										str_ide_anio = new StringBuilder();
										int ajusteBaseImponibleUltimoDiaMes=0;
										
										if (utilitario.getDia(periodoNuevoSalida)>=30) {
											ajusteBaseImponibleUltimoDiaMes=1;
											}else {
												ajusteBaseImponibleUltimoDiaMes=0;
											}
                                        int ajuste;
										int diferenciaFechas=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
										if (utilitario.getDia(periodoNuevoSalida)<30) {
											ajuste=0;
										}else {
											ajuste=1;
										}
										int valorDierenciaFechas=diferenciaFechas+ajuste;
										if (bandAjusteBaseImponible) {
											
										}
										
											for (int x = 0; x < valorDierenciaFechas; x++) {
												    cont++; 
												    boolean bandera1=false,bandera2=false;
													cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
													cal.add(Calendar.MONTH, x);
									                 nuevaFecha = cal.getTime();
									                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
										        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
															
															
															

													}else {
														str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
														//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

													}

									        	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
									        	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
														cal.add(Calendar.MONTH, x+1);
										                 nuevaFecha = cal.getTime();
										                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
										                	 str_ide_mes.append(",");
										                	// str_ide_anio.append(",");
										                 }else {
															 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
														}
														 
													}else {
														if(cont>=valorDierenciaFechas){
															str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
														}else {
															str_ide_mes_cambio.append(",");
															//str_ide_anio_cambio.append(",");
														}
														
														
														mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
														anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
													}
									 		}
									
											
											
									}
									
									periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
									 total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
									 total_dias=getDias360(periodoNuevoEntrada,periodoNuevoSalida);
									 if (total_dias>360) {
										 total_dias=360;
									}

						
								
							
								
							calculoBaseNueva=0.0;
						//	if ((i+1)==dimension){}else{
							if ((str_ide_mes.toString()!=null || !str_ide_mes.toString().equals("") || !str_ide_mes.toString().isEmpty()) && (str_ide_mes_cambio.toString()!=null || !str_ide_mes_cambio.toString().equals("") || !str_ide_mes_cambio.toString().isEmpty()) ) {
								baseImponibleVacacionValor=0.0;
								baseImponibleVacacionValorTemp1=0.0;
								baseImponibleVacacionValorTemp2=0.0;
								
								if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
									TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
									String anioParte1=tabAnio1.getValor("IDE_GEANI");
								/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
											+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
													+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
															+ "a.ide_nrdtn in(2,4)");*/
								
									TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
											+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
											+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
											+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
											+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
											+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
										if(valor_base_mes_anterior1.getTotalFilas()>0){
											for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
												//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
												if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
													baseImponibleVacacionValorTemp1+=0;
												}else {
												baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
											}
										}
										}
										
										baseImponibleVacacionValorTemp2=0.0;
										
								}else {

								
								
								TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
								String anioParte1=tabAnio1.getValor("IDE_GEANI");
								/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
										+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
												+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
														+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
														+ "a.ide_nrdtn in(2,4)");*/
								
								TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
										+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
										+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
										+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
										+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
									if(valor_base_mes_anterior1.getTotalFilas()>0){
										for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
										//	baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
											if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
												baseImponibleVacacionValorTemp1+=0;
											}else {
											baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
										}
									}
									}
								
									TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
									String anioParte2=tabAnio2.getValor("IDE_GEANI");
									
									/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
											+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
													+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
															+ "a.ide_nrdtn in(2,4)");*/
									

									TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
											+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
											+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
											+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
											+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
											+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
								
									if(valor_base_mes_anterior2.getTotalFilas()>0){
										for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
											if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
												baseImponibleVacacionValorTemp2+=0;
											}else {
											baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
										}
											//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
										}
									}
							}										
							}
							}
							
							//calculoBaseNueva=0.0;								
	//////////////7////////////////////////////AJUSTE DE BASE IMPONIBLE	
							
							
			
									/*int ajuste=0;
									if (utilitario.getMes(fechaFinTemp)!=0) {
									int mesFinAjuste=utilitario.getMes(fechaFinTemp);
									int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

									TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
									int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
									

									TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
											+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
													+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
															+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
															+ "a.ide_nrdtn in(2,4)");
									if(valor_base_mes_liquidacion.getTotalFilas()>0){
									calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro")));

									}else {
									}
									
								}	*/
								
								
		
							
							
							
							
							
							
							
							
							
							
							
							
							// si tiene un ultimo periodo
										/*if (((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))))==12) {
	int ajuste=0;
		if (mes!=0) {
		int mesTemp=mes+1;
		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anio+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesTemp+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
			String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
			double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
		calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

		}else {
		}
		
	}										
}*/

							}
							
							if (bandEjecutoRol==1) {
								if (diasExesoLiquidacion>=0) {

						double valor1=0.0,valor2=0.0,valor3=0.0,valor4=0.0;
						//	if ((i+1)==dimension){
								 valor1=0.0;
								 valor2=0.0;valor3=0.0;
										valor1=(baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2)+(calculoBaseNueva);
										valor2=valor1/total_dias;
										valor4=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
										valor3=(Math.rint(valor4 * 100) / 100);
										System.out.println("valor1"+valor1);
										System.out.println("valor2"+valor2);
										System.out.println("valor3"+valor3);
		valorVacacion+=valor2*valor3;
		System.out.println("valor vbase "+valorVacacion);			
								}}else {
									double valor1=0.0,valor2=0.0,valor3=0.0,valor4=0.0;
									//	if ((i+1)==dimension){
											 valor1=0.0;
											 valor2=0.0;valor3=0.0;
													valor1=(baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2)+(calculoBaseNueva);
													valor2=valor1/total_dias;
													valor4=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
													valor3=(Math.rint(valor4 * 100) / 100);
													System.out.println("valor1"+valor1);
													System.out.println("valor2"+valor2);
													System.out.println("valor3"+valor3);
					valorVacacion+=valor1;
					System.out.println("valor vbase "+valorVacacion);			
								}
								
								
								
						/*	}else{
							 valor1=0.0;
					 valor2=0.0;valor3=0.0;
							valor1=baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2;
							valor2=valor1/total_dias;
							valor3=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
							System.out.println("valor1"+valor1);
							System.out.println("valor2"+valor2);
							System.out.println("valor3"+valor3);
valorVacacion+=valor2*valor3;
System.out.println("valor vbase "+valorVacacion);		*/}
									}
						
					return ""+valorVacacion;
								
							}		

				//return ""+utilitario.getFormatoNumero(valorVacacion,2);







public int getDias360(String periodoNuevoEntrada, String periodoNuevoSalida){

int mesIniLaborados,mesFinLaborados,diaInicioLaborados,diaFinLaborados,anioInicioLaborados,anioFinLaborados;
diaFinLaborados=utilitario.getDia(periodoNuevoSalida);
diaInicioLaborados=utilitario.getDia(periodoNuevoEntrada);
mesFinLaborados=utilitario.getMes(periodoNuevoSalida);
mesIniLaborados=utilitario.getMes(periodoNuevoEntrada);
anioFinLaborados=utilitario.getAnio(periodoNuevoSalida);
anioInicioLaborados=utilitario.getAnio(periodoNuevoEntrada);

int valorAnio,valorMes,valorDia;
valorAnio=anioFinLaborados-anioInicioLaborados;
valorMes=mesFinLaborados-mesIniLaborados;
valorDia=diaFinLaborados-diaInicioLaborados;
int formula=((valorAnio*360)+((valorMes)*30)+(valorDia))+1;
return formula;
}


//********************************************************************************************************************************************************************************



public String getDiasLaboradosPendientesCTVacaciones(String IDE_GTEMP,int ide_gttem,String ide_asvac,String fecIniVacacion,String fecFinVacacion,
	int bandEjecutoRol,int diasExesoLiquidacion,int diasPendientesLaborados,String RMU,int dias_exceso_liquidacion,boolean ejecuta_rmu){

	if (fecFinVacacion==null || fecFinVacacion.equals("") || fecFinVacacion.isEmpty()){
		fecFinVacacion=utilitario.getFechaActual();
	}
	
int bandEntrada=0;
int bandSalida=0;

double valorTotal=0.0,valorTotal1=0.0,valorRetorno=0.0,valorRetornoVacaciones=0.0;
boolean estado_periodo=false;
double dias_pendientes=0.0;
int nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
TablaGenerica tabla_vacacion=utilitario.consultar(retornaPeriodoVacacion(ide_asvac));
//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
int nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac);
//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
int ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

//Recalculo los valores para q no se pasen de 360 dias al año
//nde = getDiasEmpleadoXAnio(nde, nda);
//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

//Obtener los periodos de cada empleado 1,2,3 etc
List<Integer> peridos = getPeriodos(nda, nde);


//Obtengo los periodos para el ajuste de dias pendientes del
//empleado hasta el 30 de abril del 2017
List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);






//System.out.println("Tipo empleado: "+ide_gttem);
double numeroDiasVacacionXAnio=0.0;
int p_asi_dias_max_vacaciones_codigo_trabajo=0,p_asi_dias_max_vacaciones_losep=0;
double division=0.0;
if (ide_gttem == 1) {
//Asigno el numero de dias max de vacaciones al año 15 dias
numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
//Los dias maximo q puede acumular 45 dias por tres periodos
p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));

//Aumentamos dia por trabajar mas de 5 anios
if(peridos.size()>5){
double anio_periodo=peridos.size();
division=anio_periodo/5;
nde=nde+(int)division;
}


}

if (ide_gttem == 2) {
//Asigno el numero de dias max de vacaciones al año 15 dias
numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
//Los dias maximo q puede acumular 45 dias por tres periodos
p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));

}







//Obtengo el total de mis dias pendientes al restar los dias
//acumulados-dias tomados
double sumatotalDiasGenerados = 0.0;
//Obtengo el numero de periodos y le asigno a cada uno los 30 dias
//que le corresponde y los dias generados hasta el presente
List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
//Obtengo el numero de periodos y le asigno a cada uno los 30 dias
//que le corresponde y los dias generados hasta el 30 de abril de

List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
double sumatotal_vacaciones = 0.0;
//Asigno la sumatoria de total de mis dias acumulados a vacacion
//desde la fecha de ingreso hasta la fecha de hoy

BigDecimal num1 = new BigDecimal(0);

for (int i = 0; i < vacacionesPeriodo.size(); i++) {
sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));

}

//System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
double sumatotal_vacacionesCalculoInicial = 0.0;

//Asigno la sumatoria de total de mis dias acumulados a vacacion
//desde la fecha de ingreso hasta el 30 de Abril de 2017
for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
}
//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

//Devuelve el numero de dias pendientes obtenidos de excel hasta el
//30 de abril


double numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac);
double totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac);
double nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac);
double diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac);




if(numeroDiasTomadosInicial!=0)
{

if (numeroDiasTomadosInicial < 0) {
double valor = 0.0;
double valor1 = 0.0;
valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
//valor=valor1-sumatotal_vacaciones;
utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
totalNumeroDiasAjuste=valor;


}
if (numeroDiasTomadosInicial > 0) {
double valor = 0.0;
double valor1 = 0.0;
valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
//valor=valor1-sumatotal_vacaciones;
utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
	utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
totalNumeroDiasAjuste=valor;

}


}



double totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac);
double numeroDiasTomados=totalNumeroDiasAjuste;
double numeroDiasTomadosTemporal = numeroDiasTomados;

//suma el valor de los dias descontados de la tabla
//asi_detalle_vacacion
//Aqui se encuentra el cuadre
double dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);




double numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);


double resultado_descuento = descuento + numeroDiasTomados;


if (ide_gttem == 2) {

double nro_dias_ajuste_periodo_asvac = 0;
TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
+ ide_asvac+" and activo_asvac=true");

if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 60) {
//System.out.println("Ingreso a descuento mayor a 60");

BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
BigDecimal descuento1= new BigDecimal(descuento);
BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
BigDecimal  dias= new BigDecimal(60);
BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

if (calculoPasaDiasPendientes.doubleValue()>=0.01) {


TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");

}
nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;


}
}

if (ide_gttem == 1) {

double nro_dias_ajuste_periodo_asvac = 0;
TablaGenerica tab_codigo_vacacion = utilitario
.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
+ ide_asvac+" and activo_asvac=true");


if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 45) {

//System.out.println("Ingreso a descuento mayor a 45");


BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
BigDecimal descuento1= new BigDecimal(descuento);
BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
BigDecimal  dias= new BigDecimal(45);
BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

if (calculoPasaDiasPendientes.doubleValue()>=0.01) {

TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
}
nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;
				
}
}

numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

/*
* Tomo los parametros creados para realizar los calculos de los
* dias pendientes calculo de vacaciones dependiendo si el empleado
* pertenece a la losep o al codigo de trabajo
*/


List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);
//for (int i = 0; i < 1; i++) {
//System.out.println("vacacionesTomadas: " + vacacionesTomadas);
//}

String periodoNuevo="";
String periodoNuevoEntrada="",periodoNuevoSalida="";
int valorDiasXAnio=0,dimension_for=0;
List<Double[]> matriz = new ArrayList<Double[]>();
String valorBaseImponiblePeriodo="";
Integer dimension = vacacionesPeriodo.size();
dimension_for=dimension-1;
for (Integer i = 0; i < dimension; i++) {
Double[] obj = new Double[5];
obj[0] = i.doubleValue() + 1; // periodo
obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
//obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
//obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
//obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;

valorRetorno=0.0;


//if (dimension>1) {
	double valorDiasPendientes=0.0;
	int valorDia=0;
	double tempValorVacaciones=0;
	valorDiasPendientes=Double.parseDouble(utilitario.getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)),2));
	if (valorDiasPendientes!=0.0) {
		if (i==0) {
			//CUANDO ES LA PRIMERA ITERACION
			if (calcular360Dias(utilitario.DeStringADate(fecIniVacacion),utilitario.DeStringADate(fecFinVacacion))<=360) {
				valorRetorno=getValorVacacionesPagarCT(fecIniVacacion,fecFinVacacion,IDE_GTEMP,RMU,dias_exceso_liquidacion);
				valorDia=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fecIniVacacion),utilitario.DeStringADate(fecFinVacacion))+1;
				tempValorVacaciones=Double.parseDouble(utilitario.getFormatoNumero((valorRetorno/valorDia),2));
			}else {
				//AQUI DEBO DE SUMARLE UN AÑO A LA FECHA
				int mesInicio=0,anioInicio=0,diaInicio=0;
				String nuevaFecha="",fechaNuevoPeriodo="";
				nuevaFecha=(utilitario.getAnio(fecIniVacacion)+1)+"-"+utilitario.getMes(fecIniVacacion)+"-"+utilitario.getDia(fecIniVacacion);
				fechaNuevoPeriodo=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(nuevaFecha), -1));
				valorRetorno=getValorVacacionesPagarCT(fecIniVacacion,fechaNuevoPeriodo,IDE_GTEMP,RMU,dias_exceso_liquidacion);
				valorDia=calcular360Dias(utilitario.DeStringADate(fecIniVacacion),utilitario.DeStringADate(fechaNuevoPeriodo))+1;
				tempValorVacaciones=Double.parseDouble(utilitario.getFormatoNumero((valorRetorno/valorDia),2));
			}
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			
			System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
			if (valorDiasPendientes<0) {
				utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
						+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+valorDia+",activo_liquidacion=true,"
						+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
						+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
				
			}else {
				utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
						+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+valorDia+",activo_liquidacion=true,"
						+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones
						+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
		
			}

		
		}else if(i==dimension_for){
			//CUANDO TERMINA DE RECORRER EL BUCLE FOR
			valorRetorno=getValorVacacionesPagarCT((utilitario.getAnio(fecIniVacacion)+(i))+"-"+utilitario.getMes(fecIniVacacion)+"-"+(utilitario.getDia(fecIniVacacion)),fecFinVacacion,IDE_GTEMP,RMU,dias_exceso_liquidacion);
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			double valorDias=0.0;
			
			int dia=0,mes=0,anioFin=0;
			String diaFin="",mesFin="";
			dia=utilitario.getDia(fecIniVacacion);
			if (dia<10) {
				diaFin="0"+dia;
			}else {
				diaFin=""+dia;
			}
			
			mes=utilitario.getMes(fecIniVacacion);
			if (mes<10) {
				mesFin="0"+mes;
			}else {
				mesFin=""+mes;
			}
			mes=utilitario.getMes(fecIniVacacion);
			anioFin=utilitario.getAnio(fecIniVacacion)+dimension_for;
			
			String fechaTemp=anioFin+"-"+mesFin+"-"+diaFin;
	 
			//valorDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(fechaTemp), utilitario.DeStringADate(fecFinVacacion));
			valorDias=calcular360Dias(utilitario.DeStringADate((utilitario.getAnio(fecIniVacacion)+(i))+"-"+utilitario.getMes(fecIniVacacion)+"-"+(utilitario.getDia(fecIniVacacion))), utilitario.DeStringADate(fecFinVacacion))+1;
			tempValorVacaciones=0.0;
			tempValorVacaciones=valorRetorno/valorDias;

			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
			
			if (valorDiasPendientes>0.0) {

			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+valorDias+",activo_liquidacion=true,"
					+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
					+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
			}else {

				utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
						+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+valorDias+",activo_liquidacion=true,"
						+ "valor_pagar_asvre=0.0,valor_dia_asvre="+tempValorVacaciones 
						+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
			}
			
			
		}else {
			
			String fec_fin="",fec_fin_temp="";
			
			int mesTemp=0,mes=0;
		
			int mesInicio=0,anioInicio=0,diaInicio=0;
			String nuevaFecha="",fechaNuevoPeriodo="";
			periodoNuevoEntrada=(utilitario.getAnio(fecIniVacacion)+i)+"-"+utilitario.getMes(fecIniVacacion)+"-"+utilitario.getDia(fecIniVacacion);
	
			periodoNuevoEntrada=(utilitario.getAnio(fecIniVacacion)+i)+"-"+utilitario.getMes(fecIniVacacion)+"-"+utilitario.getDia(fecIniVacacion);
			//fechaNuevoPeriodo=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(nuevaFecha), -1));			
			fec_fin_temp=((utilitario.getAnio(fecIniVacacion)+i)+1)+"-"+utilitario.getMes(fecIniVacacion)+"-"+utilitario.getDia(fecIniVacacion);
			periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_fin_temp), -1));
			double valorDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(periodoNuevoEntrada),utilitario.DeStringADate(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida)));
				//fec_fin_temp=((utilitario.getAnio(fecIniVacacion)+i)+1)+"-"+utilitario.getMes(fecIniVacacion)+"-"+(utilitario.getDia(fecIniVacacion));
				//fec_fin=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_fin_temp), -1));		
				valorDias=360;
	    		valorRetorno=getValorVacacionesPagarCT(periodoNuevoEntrada,periodoNuevoSalida,IDE_GTEMP,RMU,dias_exceso_liquidacion);
			
					
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			tempValorVacaciones=0.0;
			tempValorVacaciones=valorRetorno/valorDias;
			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			
				System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
				
				if (valorDiasPendientes>0.0) {

			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+360+",activo_liquidacion=true,"
					+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
					 + " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
		}else {
			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET activo_asvre=false,base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+360+",activo_liquidacion=true,"
					+ "valor_pagar_asvre=0.0,valor_dia_asvre="+tempValorVacaciones 
					 + " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
		}
			
      }
/*}else if (dimension==1){
	double valorDiasPendientes=0;
	valorDiasPendientes=Double.parseDouble(utilitario.getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)),2));
	valorRetorno+=Double.parseDouble(utilitario.getFormatoNumero(getValorVacacionesPagarCT(fecIniVacacion,fecFinVacacion,IDE_GTEMP,RMU,dias_exceso_liquidacion),2));
	valorRetornoVacaciones+=valorRetorno*valorDiasPendientes;

}*/




	}else {
		//Si no contiene periodos de vacaciones
	}


}
return utilitario.getFormatoNumero(valorRetornoVacaciones,2);
/**
* Obtencion de dias pendientes del empleado
*/
//System.out.println("numero dias pendientes vacacion: "+
//servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
}



//*********************************************************************************************************************************************************************************



public String getDiasLaboradosPendientesCT(String IDE_GTEMP,int ide_gttem,String ide_asvac,String fecIniVacacion,String fecFinVacacion,
	int bandEjecutoRol,int diasExesoLiquidacion,int diasPendientesLaborados){

int bandEntrada=0;
int bandSalida=0;

double valorTotal=0.0,valorTotal1=0.0;
double dias_pendientes=0.0;
int nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
TablaGenerica tabla_vacacion=utilitario.consultar(retornaPeriodoVacacion(ide_asvac));
boolean estado_periodo=false;
//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
int nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac);
//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
int ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

//Recalculo los valores para q no se pasen de 360 dias al año
//nde = getDiasEmpleadoXAnio(nde, nda);
//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

//Obtener los periodos de cada empleado 1,2,3 etc
List<Integer> peridos = getPeriodos(nda, nde);


//Obtengo los periodos para el ajuste de dias pendientes del
//empleado hasta el 30 de abril del 2017
List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);






//System.out.println("Tipo empleado: "+ide_gttem);
double numeroDiasVacacionXAnio=0.0;
int p_asi_dias_max_vacaciones_codigo_trabajo=0,p_asi_dias_max_vacaciones_losep=0;
double division=0.0;
if (ide_gttem == 1) {
//Asigno el numero de dias max de vacaciones al año 15 dias
numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
//Los dias maximo q puede acumular 45 dias por tres periodos
p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));

//Aumentamos dia por trabajar mas de 5 anios
if(peridos.size()>5){
double anio_periodo=peridos.size();
division=anio_periodo/5;
nde=nde+(int)division;
}


}

if (ide_gttem == 2) {
//Asigno el numero de dias max de vacaciones al año 15 dias
numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
//Los dias maximo q puede acumular 45 dias por tres periodos
p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));

}







//Obtengo el total de mis dias pendientes al restar los dias
//acumulados-dias tomados
double sumatotalDiasGenerados = 0.0;
//Obtengo el numero de periodos y le asigno a cada uno los 30 dias
//que le corresponde y los dias generados hasta el presente
List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
//Obtengo el numero de periodos y le asigno a cada uno los 30 dias
//que le corresponde y los dias generados hasta el 30 de abril de

List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
double sumatotal_vacaciones = 0.0;
//Asigno la sumatoria de total de mis dias acumulados a vacacion
//desde la fecha de ingreso hasta la fecha de hoy

BigDecimal num1 = new BigDecimal(0);

for (int i = 0; i < vacacionesPeriodo.size(); i++) {
sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));

}

//System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
double sumatotal_vacacionesCalculoInicial = 0.0;

//Asigno la sumatoria de total de mis dias acumulados a vacacion
//desde la fecha de ingreso hasta el 30 de Abril de 2017
for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
}
//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

//Devuelve el numero de dias pendientes obtenidos de excel hasta el
//30 de abril


double numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac);
double totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac);
double nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac);
double diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac);




if(numeroDiasTomadosInicial!=0)
{

if (numeroDiasTomadosInicial < 0) {
double valor = 0.0;
double valor1 = 0.0;
valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
//valor=valor1-sumatotal_vacaciones;
utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
totalNumeroDiasAjuste=valor;


}
if (numeroDiasTomadosInicial > 0) {
double valor = 0.0;
double valor1 = 0.0;
valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
//valor=valor1-sumatotal_vacaciones;
utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
	utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
totalNumeroDiasAjuste=valor;

}


}



double totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac);
double numeroDiasTomados=totalNumeroDiasAjuste;
double numeroDiasTomadosTemporal = numeroDiasTomados;

//suma el valor de los dias descontados de la tabla
//asi_detalle_vacacion
//Aqui se encuentra el cuadre
double dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);




double numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);


double resultado_descuento = descuento + numeroDiasTomados;


if (ide_gttem == 2) {

double nro_dias_ajuste_periodo_asvac = 0;
TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
+ ide_asvac+" and activo_asvac=true");

if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 60) {
//System.out.println("Ingreso a descuento mayor a 60");

BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
BigDecimal descuento1= new BigDecimal(descuento);
BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
BigDecimal  dias= new BigDecimal(60);
BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

if (calculoPasaDiasPendientes.doubleValue()>=0.01) {


TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");

}
nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;


}
}

if (ide_gttem == 1) {

double nro_dias_ajuste_periodo_asvac = 0;
TablaGenerica tab_codigo_vacacion = utilitario
.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
+ ide_asvac+" and activo_asvac=true");


if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 45) {

//System.out.println("Ingreso a descuento mayor a 45");


BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
BigDecimal descuento1= new BigDecimal(descuento);
BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
BigDecimal  dias= new BigDecimal(45);
BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

if (calculoPasaDiasPendientes.doubleValue()>=0.01) {

TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
}
nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;
				
}
}

numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

/*
* Tomo los parametros creados para realizar los calculos de los
* dias pendientes calculo de vacaciones dependiendo si el empleado
* pertenece a la losep o al codigo de trabajo
*/


List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);
//for (int i = 0; i < 1; i++) {
//System.out.println("vacacionesTomadas: " + vacacionesTomadas);
//}

String periodoNuevo="";
String periodoNuevoEntrada="",periodoNuevoSalida="";
int valorDiasXAnio=0;
List<Double[]> matriz = new ArrayList<Double[]>();

Integer dimension = vacacionesPeriodo.size();
for (Integer i = 0; i < dimension; i++) {
Double[] obj = new Double[5];
obj[0] = i.doubleValue() + 1; // periodo
obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
//obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
//obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
//obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;



}

/**
* Obtencion de dias pendientes del empleado
*/
//System.out.println("numero dias pendientes vacacion: "+
//servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));




boolean bandAjusteBaseImponible=false;
String baseImponibleVacacion="";
double baseImponibleVacacionValor=0.0,valorVacacion=0.0;
int anioInicioVacacion=0,anioFinVacacion=0;
List<Integer> peridosTemp = getPeriodos(nda, nde);
List<Double> vacacionesPeriodoTemp = getVacacionesXPeriodo(nda, nde, peridosTemp, numeroDiasVacacionXAnio);
List<Double> vacacionesTomadasTemp = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridosTemp, numeroDiasVacacionXAnio);
String base_imponible="";
int cont=0;
Integer dimensionTemp = vacacionesPeriodoTemp.size();
Date nuevaFecha = new Date();
Date nuevaFechaInicio = new Date();
Date nuevaFechaFin = new Date();

Calendar cal = Calendar.getInstance(); 
cal.setTime(utilitario.getFecha(fecIniVacacion)); 

Calendar calFechaFin = Calendar.getInstance(); 
calFechaFin.setTime(utilitario.getFecha(fecFinVacacion)); 

int mes=0,anio=0;
StringBuilder str_ide_mes = new StringBuilder();
StringBuilder str_ide_anio = new StringBuilder();
StringBuilder	str_ide_mes_cambio = new StringBuilder();
StringBuilder	str_ide_anio_cambio = new StringBuilder();
double total_dias=0;
for (int i = 0; i < utilitario.getDiferenciaMeses(utilitario.getFecha(fecIniVacacion), utilitario.getFecha(fecFinVacacion)); i++) {
cont++; 
cal.add(Calendar.MONTH, i);
nuevaFecha = cal.getTime();
str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));

if (cont>=i) {

}else {
str_ide_mes.append(",");
}
}


double calculoBaseNueva=0.0;
double baseImponibleVacacionValorTemp1=0.0;
double baseImponibleVacacionValorTemp2=0.0;
for (Integer i = 0; i < dimensionTemp; i++) {
cont++;
Double[] obj = new Double[5];
obj[0] = i.doubleValue() + 1; // periodo
obj[1] = vacacionesPeriodoTemp.get(i); // dias vacacion x periodo
//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
obj[2] = vacacionesTomadasTemp.get(i); // dias vacacion tomadas x periodo
//obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
obj[3] = vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i); // dias vacacion penientes x periodo
//obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
obj[4] = vacacionesPeriodoTemp.get(i)+ vacacionesPeriodoTemp.get(i);
//obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;



int anioInicio=0;

if ((vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i))!=0.0 || (vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i))!=0) {
String fechaIniTemp="",fechaFinTemp="";
cal.add(Calendar.YEAR, i);
nuevaFechaInicio=cal.getTime();
anioInicio=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaInicio));
calFechaFin.add(Calendar.YEAR, i);
nuevaFechaFin=calFechaFin.getTime();
int anioFin=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaFin));
baseImponibleVacacionValorTemp1=0.0;
baseImponibleVacacionValorTemp2=0.0;

/*if (anioInicio==anioFin) {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
}else {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFin+"%' ");
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
str_ide_anio.append(",");
str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
}*/
if (i==0){
double valorDiasPendientes=vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i);
if ((i+1)==dimension) {
//activo es igual a false
periodoNuevoEntrada=fecIniVacacion;	
 periodoNuevoSalida=fecFinVacacion;

 int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
 int mesVacacion = utilitario.getMes(fecIniVacacion);
 int diaVacacion = utilitario.getDia(fecIniVacacion);
String nuevaFechaEntrada="";
String mesTempEntrada="",diaTempEntrada="";
if (mesVacacion<10) {
 mesTempEntrada="0"+mesVacacion;
}else {
mesTempEntrada=""+mesVacacion;
}


if (diaVacacion<10) {
 diaTempEntrada="0"+diaVacacion;
}else {
	diaTempEntrada=""+diaVacacion;
}

periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;


int anioVacacionSalida=utilitario.getAnio(fecFinVacacion);
String mesTemp="",diaTemp="";
 if (mesVacacion<10) {
	mesTemp="0"+mesVacacion;
}else{
	mesTemp=""+mesVacacion;
}


 if (diaVacacion<10) {
		diaTemp="0"+diaVacacion;
	}else {
		diaTemp=""+diaVacacion;
}
 String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
 periodoNuevoSalida=fecFinVacacion;	

 
 
 
	anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
	anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
	if (anioInicioVacacion==anioFinVacacion) {
		TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
		str_ide_anio= new StringBuilder();
		str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
	}else {
		TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
		TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
		str_ide_anio= new StringBuilder();
		str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
		str_ide_anio.append(",");
		str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
	}
		cont=0;
		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
		str_ide_mes = new StringBuilder();
		str_ide_mes_cambio = new StringBuilder();
		str_ide_anio_cambio = new StringBuilder();
		str_ide_anio = new StringBuilder();


			for (int x = 0; x < ((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))); x++) {
				    cont++; 
				    boolean bandera1=false,bandera2=false;
					cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
					cal.add(Calendar.MONTH, x);
	                 nuevaFecha = cal.getTime();
	                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
		        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
							
							
							

					}else {
						str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
						//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

					}

	        	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
	        	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
						cal.add(Calendar.MONTH, x+1);
		                 nuevaFecha = cal.getTime();
		                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
		                	if (cont>=(utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))) {
								 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));

							}else {
			                	 str_ide_mes.append(",");

							}
		                	 
		                	// str_ide_anio.append(",");
		                 }else {
							 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
						}
						 
					}else {
						if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
							str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
						}else {
							str_ide_mes_cambio.append(",");
							//str_ide_anio_cambio.append(",");
						}
						
						
						mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
						anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
					}
	 		}
	
			
	
	
	
	periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
	if (bandEjecutoRol==1) {
		if (diasExesoLiquidacion>=0) {
			//total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)));
			total_dias=getDias360(periodoNuevoEntrada, periodoNuevoSalida);
		}}else {


			int a,b;
			Calendar calTemp = Calendar.getInstance(); 

			calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
			calTemp.add(Calendar.MONTH, 0);
			Date nuevaFechaTemp = new Date();
			nuevaFechaTemp = calTemp.getTime();
			String fechaNuevaTemp="";
			int ultimoDia= utilitario.getDia(utilitario.getUltimoDiaMesFecha(utilitario.DeDateAString(nuevaFechaTemp)));
		
			fechaNuevaTemp=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaTemp))+"-"+utilitario.getMes(utilitario.DeDateAString(nuevaFechaTemp))+"-"+ultimoDia;
		  int totalMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
			//total_dias=totalMeses*30;
			//total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(fechaNuevaTemp)));
			total_dias=getDias360(periodoNuevoEntrada, fechaNuevaTemp);
		}
			
	 if (total_dias>360) {
		 total_dias=360;
	}

	 
	 
	 
	 
	 calculoBaseNueva=0.0;
	
		if (str_ide_mes.length()!=0 || str_ide_mes_cambio.length()!=0) {
			baseImponibleVacacionValor=0.0;
			baseImponibleVacacionValorTemp1=0.0;
			baseImponibleVacacionValorTemp2=0.0;
			
			if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
				TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
				String anioParte1=tabAnio1.getValor("IDE_GEANI");
			/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
										+ "a.ide_nrdtn in(2,4)");*/
				
				TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
						+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
						+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
						+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
						+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
					if(valor_base_mes_anterior1.getTotalFilas()>0){
						for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
							if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
								baseImponibleVacacionValorTemp1+=0;
							}else {
							baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
						}
							//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
						}
					}
					
				
			}else {

			
			
			TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
			String anioParte1=tabAnio1.getValor("IDE_GEANI");
			/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
					+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
							+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
									+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
									+ "a.ide_nrdtn in(2,4)");*/
			
			TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
					+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
					+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
					+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
				if(valor_base_mes_anterior1.getTotalFilas()>0){
					for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
						if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
							baseImponibleVacacionValorTemp1+=0;
						}else {
						baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
					}
						//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
					}
				}
			
				TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
				String anioParte2=tabAnio2.getValor("IDE_GEANI");
				
				/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
										+ "a.ide_nrdtn in(2,4)");*/
				

				TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
						+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
						+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
						+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
						+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
			
				if(valor_base_mes_anterior2.getTotalFilas()>0){
					for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
						if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
							baseImponibleVacacionValorTemp2+=0;
						}else {
						baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
					}
						//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
					}
				}
		}										
		}
			 
	 
	 
		
	
		
		
		
		
		
		if (bandEjecutoRol==1) {
			if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste					
				
				int ajuste=0;
				if (utilitario.getMes(fecFinVacacion)!=0) {
				int mesFinAjuste=utilitario.getMes(fecFinVacacion);
				int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
				int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
				

				TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
										+ "a.ide_nrdtn in(2,4)");
				if(valor_base_mes_liquidacion.getTotalFilas()>0){
					int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
					if (ultimoDia>=31) {
						ultimoDia=30;
					}
					String nuevaFec=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-01";
					periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
					//double diferenciaDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(fecFinVacacion),utilitario.DeStringADate(nuevaFec));
					double diferenciaDias=getDias360(nuevaFec, periodoNuevoSalida)+1;
					calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

				}else {
					
					
					calculoBaseNueva=0;
				}
				
			}			
				
		
			}//Final de ajusteel

			else{
				
				int ajuste=0;
				if (utilitario.getMes(fecFinVacacion)!=0) {
				int mesFinAjuste=utilitario.getMes(fecFinVacacion);
				int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
				int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
				

				TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
										+ "a.ide_nrdtn in(2,4)");
				if(valor_base_mes_liquidacion.getTotalFilas()>0){
					calculoBaseNueva=Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"));
				}
				
				}
			}
			
			
			
		}//Si ejecuto rol
		
		
		else {
			
			//Si no ha ejecutado rol entonces deja en cero 
			calculoBaseNueva=0.0;
		}
									 
}///if == dimension+1
else{
//Cuando tiene mas de una iteraciones
//***************************************************************************************************************************************										


//activo es igual a false
periodoNuevoEntrada=fecIniVacacion;	
periodoNuevoSalida=fecFinVacacion;

int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
int mesVacacion = utilitario.getMes(fecIniVacacion);
int diaVacacion = utilitario.getDia(fecIniVacacion);
String nuevaFechaEntrada="";
String mesTempEntrada="",diaTempEntrada="";
if (mesVacacion<10) {
mesTempEntrada="0"+mesVacacion;
}else {
mesTempEntrada=""+mesVacacion;
}


if (diaVacacion<10) {
diaTempEntrada="0"+diaVacacion;
}else {
diaTempEntrada=""+diaVacacion;
}

periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;


int anioVacacionSalida=utilitario.getAnio(fecIniVacacion)+(i+1);
String mesTemp="",diaTemp="";
if (mesVacacion<10) {
mesTemp="0"+mesVacacion;
}else{
mesTemp=""+mesVacacion;
}


if (diaVacacion<10) {
diaTemp="0"+diaVacacion;
}else {
diaTemp=""+diaVacacion;
}
String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
periodoNuevoSalida=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(nuevaFechaIngreso), -1));	




anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
if (anioInicioVacacion==anioFinVacacion) {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
}else {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
str_ide_anio.append(",");
str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));

cont=0;
cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
str_ide_mes = new StringBuilder();
str_ide_mes_cambio = new StringBuilder();
str_ide_anio_cambio = new StringBuilder();
str_ide_anio = new StringBuilder();


	for (int x = 0; x < ((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))); x++) {
		    cont++; 
		    boolean bandera1=false,bandera2=false;
			cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
			cal.add(Calendar.MONTH, x);
             nuevaFecha = cal.getTime();
             if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
        	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
					
					
					

			}else {
				str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
				//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

			}

    	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
    	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
				cal.add(Calendar.MONTH, x+1);
                 nuevaFecha = cal.getTime();
                 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
                	 str_ide_mes.append(",");
                	// str_ide_anio.append(",");
                 }else {
					 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
				}
				 
			}else {
				if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
					str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
				}else {
					str_ide_mes_cambio.append(",");
					//str_ide_anio_cambio.append(",");
				}
				
				
				mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
				anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
			}
		}

	
	
}

periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));

if (total_dias>360) {
 total_dias=360;
}





calculoBaseNueva=0.0;
if ((i+1)==dimension){}else{
if ((str_ide_mes.toString()!=null || !str_ide_mes.toString().equals("") || !str_ide_mes.toString().isEmpty()) && (str_ide_mes_cambio.toString()!=null || !str_ide_mes_cambio.toString().equals("") || !str_ide_mes_cambio.toString().isEmpty()) ) {
	baseImponibleVacacionValor=0.0;
	baseImponibleVacacionValorTemp1=0.0;
	baseImponibleVacacionValorTemp2=0.0;
	
	if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
		TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
		String anioParte1=tabAnio1.getValor("IDE_GEANI");
	/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");*/
		
		TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
			if(valor_base_mes_anterior1.getTotalFilas()>0){
				for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
					if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
						baseImponibleVacacionValorTemp1+=0;
					}else {
					baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
				}
					//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
				}
			}
			
			baseImponibleVacacionValorTemp2=0.0;
			
	}else {

	
	
	TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
	String anioParte1=tabAnio1.getValor("IDE_GEANI");
	/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
			+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
							+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
							+ "a.ide_nrdtn in(2,4)");*/
	
	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
			+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
			+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
			+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
		if(valor_base_mes_anterior1.getTotalFilas()>0){
			for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
				if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
					baseImponibleVacacionValorTemp1+=0;
				}else {
				baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
			}
				//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
			}
		}
	
		TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
		String anioParte2=tabAnio2.getValor("IDE_GEANI");
		
		/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");*/
		

		TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
	
		if(valor_base_mes_anterior2.getTotalFilas()>0){
			for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
				if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
					baseImponibleVacacionValorTemp2+=0;
				}else {
				baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
			}
			//	baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
			}
		}
}										
}
}		 


//calculoBaseNueva=0.0;





/*	if (bandEjecutoRol==1) {
	if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste												
		int ajuste=0;
		if (utilitario.getMes(fechaFinTemp)!=0) {
		int mesFinAjuste=utilitario.getMes(fechaFinTemp);
		int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
			String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
			double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
		calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

		}else {
		}
		
	}			
		
		
		
		
		
	}//Final de ajusteel
	else {
		int ajuste=0;
		if (utilitario.getMes(fechaFinTemp)!=0) {
		int mesFinAjuste=utilitario.getMes(fechaFinTemp);
		int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
		calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro")));

		}else {
		}
		
	}	
	}
	
	
	
	
	
}else {
	calculoBaseNueva=0.0;
}

*/









/*		

int ajuste=0;
	if (utilitario.getMes(fechaFinTemp)!=0) {
	int mesFinAjuste=utilitario.getMes(fechaFinTemp);
	int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

	TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
	int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
	

	TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
			+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
							+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
							+ "a.ide_nrdtn in(2,4)");
	if(valor_base_mes_liquidacion.getTotalFilas()>0){
		String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
		double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
	calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

	}else {
	}
	
}										


*/















//Este es para cuando contiene mas de dos periodos 
//***9*9*9*********9*****************************************************************************************************************************									

}



}else {

if ((i+1)==dimension) {
//ITERACIONES DESDE LA 1 EN ADELANTE	
anioInicio=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaInicio));

int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
int mesVacacion = utilitario.getMes(fecIniVacacion);
int diaVacacion = utilitario.getDia(fecIniVacacion);
String nuevaFechaEntrada="";
String mesTempEntrada="",diaTempEntrada="";
if (mesVacacion<10) {
mesTempEntrada="0"+mesVacacion;
}else {
mesTempEntrada=""+mesVacacion;
}


if (diaVacacion<10) {
diaTempEntrada="0"+diaVacacion;
}else {
diaTempEntrada=""+diaVacacion;
}

periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;


int anioVacacionSalida=utilitario.getAnio(fecFinVacacion);
String mesTemp="",diaTemp="";
if (mesVacacion<10) {
mesTemp="0"+mesVacacion;
}else{
mesTemp=""+mesVacacion;
}


if (diaVacacion<10) {
diaTemp="0"+diaVacacion;
}else {
diaTemp=""+diaVacacion;
}
String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;

if (utilitario.getDia(fecFinVacacion)>=31) {
periodoNuevoSalida=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-30";	

}else {
periodoNuevoSalida=fecFinVacacion;	

}





anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
anioFinVacacion=utilitario.getAnio(fecFinVacacion);
//periodoNuevoSalida=fecFinVacacion;
if (anioInicioVacacion==anioFinVacacion) {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
}else {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
str_ide_anio.append(",");
str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));
}

cont=0;
cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
str_ide_mes = new StringBuilder();
str_ide_mes_cambio = new StringBuilder();
str_ide_anio_cambio = new StringBuilder();
str_ide_anio = new StringBuilder();
//valor elimina dias
int valorDiferenciaMes=0;
if (utilitario.getMes(periodoNuevoEntrada)==utilitario.getMes(fecFinVacacion)) {
valorDiferenciaMes=0;
}else {
valorDiferenciaMes=((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))));
}

		
for (int x = 0; x < valorDiferenciaMes; x++) {
cont++; 
boolean bandera1=false,bandera2=false;
cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
cal.add(Calendar.MONTH, x);
 nuevaFecha = cal.getTime();
 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
		
		
		

}else {
	str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
	//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

}

 if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
	cal.add(Calendar.MONTH, x+1);
     nuevaFecha = cal.getTime();
     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
    		if (cont>=(utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)))) {
				 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));

			}else {
            	 str_ide_mes.append(",");

			}
    	// str_ide_anio.append(",");
     }else {
		 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
	}
	 
}else {
	if(cont>=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))){
		str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
	}else {
		str_ide_mes_cambio.append(",");
		//str_ide_anio_cambio.append(",");
	}
	
	
	mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
	anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
}
}




/*for (int x = 0; x < valorDiferenciaMes; x++) {
    cont++; 
	cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
	cal.add(Calendar.MONTH, x);
     nuevaFecha = cal.getTime();
     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));

     if (cont<utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))) {
		 str_ide_mes.append(",");
	}else {
		mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));

	}
}*/
total_dias=0;
int valorMeses=0;
double valorMesesTemp=0.0;
total_dias=0;
//total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
//total_dias=utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));



if (bandEjecutoRol==1) {
if (diasExesoLiquidacion>=0) {
	total_dias=0;
	valorMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
	valorMesesTemp=30*valorMeses;
	total_dias=valorMesesTemp+utilitario.getDia(periodoNuevoSalida);
	total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida)));
	Calendar calTemp = Calendar.getInstance(); 
	calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
	Date nuevaFechaTemp = new Date();
	nuevaFechaTemp = calTemp.getTime();
	total_dias=getDias360(periodoNuevoEntrada,periodoNuevoSalida);
	

	
}
}else {
total_dias=0;
valorMeses=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
valorMesesTemp=30*valorMeses;
total_dias=valorMesesTemp;
Calendar calTemp = Calendar.getInstance(); 
calTemp.setTime(utilitario.getFecha(periodoNuevoSalida));
calTemp.add(Calendar.MONTH, -1);
Date nuevaFechaTemp = new Date();
nuevaFechaTemp = calTemp.getTime();

int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(utilitario.DeDateAString(nuevaFechaTemp)));
if (ultimoDia>30) {
	ultimoDia=30;
}

String fechaNuevaTemp=utilitario.getAnio(utilitario.DeDateAString(nuevaFechaTemp))+"-"+utilitario.getMes(utilitario.DeDateAString(nuevaFechaTemp))+"-"+ultimoDia;

total_dias=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(fechaNuevaTemp)));
total_dias=getDias360(periodoNuevoEntrada,fechaNuevaTemp);

//total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), 
}



//***********************************************************************************************************************************************								





if (total_dias>360) {
 total_dias=360;
}





calculoBaseNueva=0.0;
//if ((i+1)==dimension){}else{
if (str_ide_mes.length()!=0 && str_ide_mes_cambio.length()!=0) {
	baseImponibleVacacionValor=0.0;
	baseImponibleVacacionValorTemp1=0.0;
	baseImponibleVacacionValorTemp2=0.0;
	
	if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
		TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
		String anioParte1=tabAnio1.getValor("IDE_GEANI");
	/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");*/
		
		TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
		
		
			if(valor_base_mes_anterior1.getTotalFilas()>0){
				for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
					if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
						baseImponibleVacacionValorTemp1+=0;
					}else {
					baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
				}
					//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
				}
			}
			
			baseImponibleVacacionValorTemp2=0.0;
			
	}else {

	
	
	TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
	String anioParte1=tabAnio1.getValor("IDE_GEANI");
	/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
			+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
							+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
							+ "a.ide_nrdtn in(2,4)");*/
	
	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
			+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
			+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
			+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
			+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
		if(valor_base_mes_anterior1.getTotalFilas()>0){
			for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
				if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
					baseImponibleVacacionValorTemp1+=0;
				}else {
				baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
			}
				//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
			}
		}
	
		TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
		String anioParte2=tabAnio2.getValor("IDE_GEANI");
		
		/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");*/
		

		TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
	
		if(valor_base_mes_anterior2.getTotalFilas()>0){
			for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
				if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
					baseImponibleVacacionValorTemp2+=0;
				}else {
					baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
				}
				baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
			}
		}
}										
}
//}		 







if (bandEjecutoRol==1) {
	if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste					
		
		int ajuste=0;
		if (utilitario.getMes(fecFinVacacion)!=0) {
		int mesFinAjuste=utilitario.getMes(fecFinVacacion);
		int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
			int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
			if (ultimoDia>=31) {
				ultimoDia=30;
			}
			String nuevaFec=utilitario.getAnio(fecFinVacacion)+"-"+utilitario.getMes(fecFinVacacion)+"-01";
			periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
			double diferenciaDias=getDias360(nuevaFec, fecFinVacacion);
			calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

		}else {
			
			
			calculoBaseNueva=0;
		}
		
	}			
		

	}//Final de ajusteel

	else{
		
		int ajuste=0;
		if (utilitario.getMes(fecFinVacacion)!=0) {
		int mesFinAjuste=utilitario.getMes(fecFinVacacion);
		int anioFinAjuste=utilitario.getAnio(fecFinVacacion);

		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		valor_base_mes_liquidacion.imprimirSql();
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
			calculoBaseNueva=Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"));
		}
		
		}
	}
	
	
	
}//Si ejecuto rol




//fvgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg




/*		if (bandEjecutoRol==1) {
	if (diasExesoLiquidacion>0) {
///////////////////////////////		para ejecutar ajuste												
		
		int ajuste=0;
		if (utilitario.getMes(fechaFinTemp)!=0) {
		int mesFinAjuste=utilitario.getMes(fechaFinTemp);
		int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
		int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
		

		TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
								+ "a.ide_nrdtn in(2,4)");
		if(valor_base_mes_liquidacion.getTotalFilas()>0){
			int ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida));
			if (ultimoDia>=31) {
				ultimoDia=30;
			}
			String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-"+ultimoDia;
			if (nuevaFec.compareTo(periodoNuevoSalida)<=0) {
				periodoNuevoSalida=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(periodoNuevoSalida), -1));
			}
			double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+1;
			calculoBaseNueva=-(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

		}else {
		}
		
	}			
		
		
		
		
		
	}//Final de ajusteel

	
	
}else {
	calculoBaseNueva=0.0;
}							


*/









/*	int ajuste=0;
	if (utilitario.getMes(fechaFinTemp)!=0) {
	int mesFinAjuste=utilitario.getMes(fechaFinTemp);
	int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

	TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
	int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
	

	TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
			+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
							+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
							+ "a.ide_nrdtn in(2,4)");
	if(valor_base_mes_liquidacion.getTotalFilas()>0){
		String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
		double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
	calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

	}else {
	}
	
}	*/									






























//****************************************************************************************************************************************************								



}else {
	
//activo es igual a false
periodoNuevoEntrada=fecIniVacacion;	
periodoNuevoSalida=fecFinVacacion;

int anioVacacion=utilitario.getAnio(fecIniVacacion)+(i);
int mesVacacion = utilitario.getMes(fecIniVacacion);
int diaVacacion = utilitario.getDia(fecIniVacacion);
String nuevaFechaEntrada="";
String mesTempEntrada="",diaTempEntrada="";
if (mesVacacion<10) {
mesTempEntrada="0"+mesVacacion;
}else {
mesTempEntrada=""+mesVacacion;
}


if (diaVacacion<10) {
diaTempEntrada="0"+diaVacacion;
}else {
diaTempEntrada=""+diaVacacion;
}

periodoNuevoEntrada=anioVacacion+"-"+mesTempEntrada+"-"+diaTempEntrada;


int anioVacacionSalida=utilitario.getAnio(fecIniVacacion)+(i+1);
String mesTemp="",diaTemp="";
if (mesVacacion<10) {
mesTemp="0"+mesVacacion;
}else{
mesTemp=""+mesVacacion;
}


if (diaVacacion<10) {
diaTemp="0"+diaVacacion;
}else {
diaTemp=""+diaVacacion;
}
String nuevaFechaIngreso=anioVacacionSalida+"-"+mesTemp+"-"+diaTemp;
periodoNuevoSalida=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(nuevaFechaIngreso), -1));	









anioInicioVacacion=utilitario.getAnio(periodoNuevoEntrada);
anioFinVacacion=utilitario.getAnio(periodoNuevoSalida);
if (anioInicioVacacion==anioFinVacacion) {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicio+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
}else {
TablaGenerica tabAnio=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioInicioVacacion+"%' ");
TablaGenerica tabAnioTemp=utilitario.consultar("SELECT IDE_GEANI, DETALLE_GEANI FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+anioFinVacacion+"%' ");
str_ide_anio= new StringBuilder();
str_ide_anio.append(tabAnio.getValor("IDE_GEANI"));
str_ide_anio.append(",");
str_ide_anio.append(tabAnioTemp.getValor("IDE_GEANI"));

cont=0;
cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
str_ide_mes = new StringBuilder();
str_ide_mes_cambio = new StringBuilder();
str_ide_anio_cambio = new StringBuilder();
str_ide_anio = new StringBuilder();
int ajusteBaseImponibleUltimoDiaMes=0;

if (utilitario.getDia(periodoNuevoSalida)>=30) {
ajusteBaseImponibleUltimoDiaMes=1;
}else {
	ajusteBaseImponibleUltimoDiaMes=0;
}
int ajuste;
int diferenciaFechas=utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
if (utilitario.getDia(periodoNuevoSalida)<30) {
ajuste=0;
}else {
ajuste=1;
}
int valorDierenciaFechas=diferenciaFechas+ajuste;
if (bandAjusteBaseImponible) {

}

for (int x = 0; x < valorDierenciaFechas; x++) {
	    cont++; 
	    boolean bandera1=false,bandera2=false;
		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
		cal.add(Calendar.MONTH, x);
         nuevaFecha = cal.getTime();
         if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
    	     str_ide_mes.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
				
				
				

		}else {
			str_ide_mes_cambio.append(utilitario.getMes(utilitario.DeDateAString(nuevaFecha)));
			//str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));

		}

	     if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
	 		cal.setTime(utilitario.getFecha(periodoNuevoEntrada));
			cal.add(Calendar.MONTH, x+1);
             nuevaFecha = cal.getTime();
             if (utilitario.getAnio(utilitario.DeDateAString(nuevaFecha))== utilitario.getAnio(periodoNuevoEntrada)) {
            	 str_ide_mes.append(",");
            	// str_ide_anio.append(",");
             }else {
				 str_ide_anio.append(utilitario.getAnio(periodoNuevoEntrada));
			}
			 
		}else {
			if(cont>=valorDierenciaFechas){
				str_ide_anio_cambio.append(utilitario.getAnio(utilitario.DeDateAString(nuevaFecha)));
			}else {
				str_ide_mes_cambio.append(",");
				//str_ide_anio_cambio.append(",");
			}
			
			
			mes=utilitario.getMes(utilitario.DeDateAString(nuevaFecha));
			anio=utilitario.getAnio(utilitario.DeDateAString(nuevaFecha));
		}
	}



}

periodoNuevo=""+periodoNuevoEntrada+" - "+periodoNuevoSalida;	
total_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida));
total_dias=getDias360(periodoNuevoEntrada,periodoNuevoSalida);
if (total_dias>360) {
total_dias=360;
}





calculoBaseNueva=0.0;
//if ((i+1)==dimension){}else{
if ((str_ide_mes.toString()!=null || !str_ide_mes.toString().equals("") || !str_ide_mes.toString().isEmpty()) && (str_ide_mes_cambio.toString()!=null || !str_ide_mes_cambio.toString().equals("") || !str_ide_mes_cambio.toString().isEmpty()) ) {
baseImponibleVacacionValor=0.0;
baseImponibleVacacionValorTemp1=0.0;
baseImponibleVacacionValorTemp2=0.0;

if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
String anioParte1=tabAnio1.getValor("IDE_GEANI");
/*	TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
		+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
				+ "a.ide_nrdtn in(2,4)");*/

TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
if(valor_base_mes_anterior1.getTotalFilas()>0){
for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
	if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
		baseImponibleVacacionValorTemp1+=0;
	}else {
	baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
}
	//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
}
}

baseImponibleVacacionValorTemp2=0.0;

}else {



TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio.toString()+"%'");
String anioParte1=tabAnio1.getValor("IDE_GEANI");
/*TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+")) "
	+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
			+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
			+ "a.ide_nrdtn in(2,4)");*/

TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
if(valor_base_mes_anterior1.getTotalFilas()>0){
for (int j = 0; j <valor_base_mes_anterior1.getTotalFilas(); j++) {
	if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
		baseImponibleVacacionValorTemp1+=0;
	}else {
baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
}
//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
}
}

TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+str_ide_anio_cambio.toString()+"%'");
String anioParte2=tabAnio2.getValor("IDE_GEANI");

/*TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+")) "
		+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+" ))  "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
				+ "a.ide_nrdtn in(2,4)");*/


TablaGenerica valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes_cambio.toString()+") and ide_geani in("+anioParte2+"))  "
+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");

if(valor_base_mes_anterior2.getTotalFilas()>0){
for (int j = 0; j <valor_base_mes_anterior2.getTotalFilas(); j++) {
	if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
		baseImponibleVacacionValorTemp2+=0;
	}else {
baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
}
//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
}
}
}										
}
}

//calculoBaseNueva=0.0;								
//////////////7////////////////////////////AJUSTE DE BASE IMPONIBLE	



/*int ajuste=0;
if (utilitario.getMes(fechaFinTemp)!=0) {
int mesFinAjuste=utilitario.getMes(fechaFinTemp);
int anioFinAjuste=utilitario.getAnio(fechaFinTemp);

TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioFinAjuste+"%'");
int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));


TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinAjuste+") and ide_geani in("+anioTemp+")) "
		+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
				+ "a.ide_nrdtn in(2,4)");
if(valor_base_mes_liquidacion.getTotalFilas()>0){
calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro")));

}else {
}

}	*/















//si tiene un ultimo periodo
/*if (((utilitario.getDiferenciaMeses(utilitario.getFecha(periodoNuevoEntrada), utilitario.getFecha(periodoNuevoSalida))))==12) {
int ajuste=0;
if (mes!=0) {
int mesTemp=mes+1;
TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anio+"%'");
int anioTemp=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));


TablaGenerica valor_base_mes_liquidacion=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
+" where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesTemp+") and ide_geani in("+anioTemp+")) "
+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))  "
+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and "
+ "a.ide_nrdtn in(2,4)");
if(valor_base_mes_liquidacion.getTotalFilas()>0){
String nuevaFec=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
double diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(nuevaFec),utilitario.DeStringADate(periodoNuevoSalida))+2;
calculoBaseNueva=(Double.parseDouble(valor_base_mes_liquidacion.getValor("valor_nrdro"))/30)*diferenciaDias;

}else {
}

}										
}*/

}
double valor1=0.0,valor2=0.0,valor3=0.0,valor4=0.0;
valorTotal=0.0;
//if ((i+1)==dimension){
valor1=0.0;
valor2=0.0;valor3=0.0;
valor1=(baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2)+(calculoBaseNueva);
valor2=valor1/total_dias;
valor4=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
valor3=(Math.rint(valor4 * 100) / 100);
valorTotal=total_dias;
valorTotal1=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
System.out.println("valor1"+valor1);
System.out.println("valor2"+valor2);
System.out.println("valor3"+valor3);
valorVacacion+=valor2*valor3;
System.out.println("valor vbase "+valorVacacion);			



/*	}else{
valor1=0.0;
valor2=0.0;valor3=0.0;
valor1=baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2;
valor2=valor1/total_dias;
valor3=(vacacionesPeriodoTemp.get(i) - vacacionesTomadasTemp.get(i));
System.out.println("valor1"+valor1);
System.out.println("valor2"+valor2);
System.out.println("valor3"+valor3);
valorVacacion+=valor2*valor3;
System.out.println("valor vbase "+valorVacacion);		*/}
}


if (diasPendientesLaborados==1) {
return ""+valorTotal;
}else if(diasPendientesLaborados==2){
return ""+valorTotal1;
}else {
return ""+0.0;
}

}		

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//CALCULO DE NUMERO DE DIAS 

	/**
	 * calcula el numero de dias calendario que el empleado lleva en la empresa desde la fecha ingreso hasta el 30 de abril del 2017
	 * @param fechaIngresoEmpleado
	 * @return
	 */
  public Integer getNumeroDiasEmpleadoCalculoInicial(String IDE_GEEDP,String IDE_ASVAC){
     //TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GEEDP=" + IDE_GEEDP + ")");        
  	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac "
  			+ "from asi_vacacion where  IDE_ASVAC="+IDE_ASVAC);        

     
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     String dateInString = tab_periodo.getValor("fecha_ingreso_asvac");
		String dateInStringFechaCalculoInicial="2017-05-30";

		Date date = null;
  	//Fecha calculo de vacaciones hasta el 30 de abril
 		Date dateFechaCalculoInicial=null;	
  		/**
  		 * Le asigno a una variable de tipo date la fecha de ingreso
  		 */
  			
  		try {
  			date = sdf.parse(dateInString);
  			dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
  		} catch (ParseException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}

  		
  		Calendar calendarInicio = Calendar.getInstance();
  		calendarInicio.setTime(date);
  		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
  		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
  		int anioInicio = calendarInicio.get(Calendar.YEAR);
  		
  		
  		// Fecha fin
  		Calendar calendarFin = Calendar.getInstance();
  		calendarFin.setTime(dateFechaCalculoInicial);
  		int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
  		int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
  		int anioFin = calendarFin.get(Calendar.YEAR);
  		
  		
		    
  		int anios = 0;
  		int mesesPorAnio = 0;
  		int diasPorMes = 0;
  		int diasTipoMes = 0;
  		diasTipoMes=30;
  		// Calculo de días del mes

if (mesInicio <= mesFin) {
  				anios = anioFin - anioInicio;
  				if (diaInicio <= diaFin) {
  					mesesPorAnio = mesFin - mesInicio;
  					diasPorMes = diaFin - diaInicio;
		    	}else{
  					if (mesFin == mesInicio) {
  						anios = anios - 1;
  					}
  					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
  					diasPorMes = diasTipoMes - (diaInicio - diaFin);
  				}
  			} else {
  				anios = anioFin - anioInicio - 1;
  				if (diaInicio > diaFin) {
  					mesesPorAnio = mesFin - mesInicio - 1 + 12;
  					diasPorMes = diasTipoMes - (diaInicio - diaFin);
  				} else {
  					mesesPorAnio = mesFin - mesInicio + 12;
  					diasPorMes = diaFin - diaInicio;
		    	}
			}
  //		System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");		
 // 		System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
  	 
  		int returnValue = -1;
  				returnValue = anios * 12 + mesesPorAnio;
 				//System.out.println("Total meses anteriores: " + returnValue + " Meses.");
  				returnValue =(((returnValue*30)+diasPorMes)+1);
  				 //System.out.println("Total dias anteriore: " + returnValue);

  				 return returnValue;
  		
  }
  
  
  
	   //PERIODOS DE EMPLEADO DE ACUERDO A LA FECHA DE INGRESO 
		    
			public static List<Integer> getPeriodos(Integer nda, Integer nde) {
				List<Integer> peridos = new ArrayList<Integer>();
				Integer indicePeriodo = 1;
				Integer ndeAux = nde;
				do {
					ndeAux = ndeAux - nda;
					peridos.add(indicePeriodo);
					indicePeriodo++;
				}while(ndeAux > 0);
				return peridos;
			}
			
			
			 //VACACIONES POR PERIODOS
	 		public static List<Double> getVacacionesXPeriodo(Integer nda, Integer nde, List<Integer> peridos, double numeroDiasVacacionXAnio) {
	 			List<Double> vaxacionXPeriodo = new ArrayList<Double>();
	 			Integer ndeAux = nde;
	 			for (Integer periodo : peridos) {
	 				if (ndeAux >= nda){
	 					ndeAux = nde - (periodo * nda);
	 					vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
	 				}else{
	 				//	System.out.println("calculo final: "+ndeAux);
	 					double numdiasUltimoperiodo = (ndeAux * numeroDiasVacacionXAnio) / nda;
	 					vaxacionXPeriodo.add(numdiasUltimoperiodo);
	 				}
	 			}
	 			return vaxacionXPeriodo;
	 		}
	 		
	 		
	 		
	 		 /**
		 	    * Metodo extrae el numero de dias pendientes para el calculo de dias tomados hasta el 30 de abril obtenidos del excel
		 	    * @param IDE_GEEDP
		 	    * @return
		 	    */
		 	   
		 	            double valorIncialDiasTomados;
		 		  public double getNumeroDiasPendientesInicial(String IDE_GEEDP,String ide_asvac) {
		 			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
		 		        if (!tab_periodo.isEmpty()) {
		 	         	                             TablaGenerica tab_valor_InicialDiasTomados=utilitario.consultar("select ide_gtemp,dias_tomados_asvac "
		 	         	                             		+ "from asi_vacacion where IDE_GTEMP="+IDE_GEEDP+" and IDE_ASVAC="+ide_asvac);
		 		       	if (tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac")==null || tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac").isEmpty())
		 		       	{
		 		       	valorIncialDiasTomados=0.0;
		 	    		}else {
		 	       		valorIncialDiasTomados=Double.parseDouble(tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac"));
		 	    		}
		 		       //	System.out.println(valorIncialDiasTomados);
		 		        return valorIncialDiasTomados;	
		 		        }

		 		        return 0;
		 	    		
		 		  }
		 		  
		 		  
		 		   /*
		 		    * Metodo devuelve los dias pendientes calculados a partir del excel de cada empleado
		 		    */
		 		   
		 		   public double getNumeroDiasPendientesInicialAjuste(String IDE_GEEDP,String ide_asvac) {     
		 			   double numeroDiasTomadoss=0.0;
		 			   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
		 		        if (!tab_periodo.isEmpty()) {
		 			    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_SOLICITADO "
		 		        		+ "FROM ( "
		 		        		+ "SELECT IDE_ASVAC,(case when sum(DIA_SOLICITADO_ASDEV) is null then 0 else sum(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO "
		 		        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC "
		 		        	    + " ) a where IDE_ASVAC="+ ide_asvac);

		 			    	   if (tab_numero_tomados.getValor("DIA_SOLICITADO")==null || tab_numero_tomados.getValor("DIA_SOLICITADO").isEmpty())
		 			    	   {
		 			    		   numeroDiasTomadoss=0.0;
		 	      		       }else {
		 	      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_SOLICITADO"));	
		 	      		       
		 	      		       }
		 	         		   return  numeroDiasTomadoss;

		 		               }
		 		      return  0;
		 		        
		 		    }
		 		    

		 		  /*
		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
		 		   */
		 		  	   public double getNumeroDiasTomados(String IDE_GEEDP, String ide_asvac) {     
		 		  		   double numeroDiasTomadoss=0.0;
		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
		 		  	        if (!tab_periodo.isEmpty()) {
		 		  		    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
		 		  	        		+ "FROM ( "
		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=false GROUP BY IDE_ASVAC "
		 		  	        	    + " ) a where IDE_ASVAC="+ ide_asvac);

		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
		 		  		    	   {
		 		  		    		   numeroDiasTomadoss=0.0;
		 		        		       }else {
		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
		 		        		       
		 		        		       }
		 		           		   return  numeroDiasTomadoss;

		 		  	               }
		 		  	      return  0;
		 		  	        
		 		  	    }
		 		  	          


		 		  	   
		 		  /*
		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
		 		   */
		 		  	   public double getNumeroDiasTomadosFinSemana(String IDE_GEEDP,String ide_asvac) {     
		 		  		   double numeroDiasTomadoss=0.0;
		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_ASVAC =" + ide_asvac + " ");        
		 		  	        if (!tab_periodo.isEmpty()) {
		 		  		    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
		 		  	        		+ "FROM ( "
		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=true GROUP BY IDE_ASVAC "
		 		  	        	    + " ) a where IDE_ASVAC="+ ide_asvac);

		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
		 		  		    	   {
		 		  		    		   numeroDiasTomadoss=0.0;
		 		        		       }else {
		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
		 		        		       
		 		        		       }
		 		           		   return  numeroDiasTomadoss;

		 		  	               }
		 		  	      return  0;
		 		  	        
		 		  	    }
		 		  	   

//Metodo Descuento Fines de Semana
		 		  	   
	    		 		  /*
	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	    		 		   */
	    		 		  	   public double getNumeroFinSemanaTomado(String IDE_GEEDP) {     
	    		 		  		   double numeroDiasTomadoss=0.0;
	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 		  	        if (!tab_periodo.isEmpty()) {
	    		 		  		    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
	    		 		  	        		+ "FROM ( "
	    		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
	    		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
	    		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=true and es_fin_semana_asdev=true GROUP BY IDE_ASVAC "
		 		  	        	    + " ) a where IDE_ASVAC="+ tab_periodo.getValor("IDE_ASVAC"));

		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
		 		  		    	   {
		 		  		    		   numeroDiasTomadoss=0.0;
		 		        		       }else {
		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
		 		        		       
		 		        		       }
		 		           		   return  numeroDiasTomadoss;

		 		  	               }
		 		  	      return  0;
		 		  	        
		 		  	    }
		 		  	          

		 		  	  /**
		 		  	   * Metodo devuelve el numero de dias para el nuevo calculo de dias tomados a vacacion 
		 		  	   * @param IDE_GEEDP
		 		  	   * @return
		 		  	   */
		 		  		  public double getNumeroDiasAjusteEmpleado(String IDE_GEEDP,String ide_asvac) {
		 		  	          double valorNumeroDiasAjusteEmpleado;
		 		  			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
		 		  		        if (!tab_periodo.isEmpty()) {
		 		  	       	                             TablaGenerica tabvalorNumeroDiasAjusteEmpleado=utilitario.consultar("select ide_gtemp,nro_dias_ajuste_asvac "
		 		  	       	                             		+ "from asi_vacacion where IDE_ASVAC=" +ide_asvac);
		 		  		       	if (tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac")==null || tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac").isEmpty())
		 		  		       	{
		 		  		       		valorNumeroDiasAjusteEmpleado=0.0;
		 		  	  		}else {
		 		  	  			valorNumeroDiasAjusteEmpleado=Double.parseDouble(tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac"));
		 		  	  		}
		 		  		        return valorNumeroDiasAjusteEmpleado;	
		 		  		        }

		 		  		        return 0;
		 		  	  		
		 		  		  }
		 		  		  
		 		  		  
		 		  		  
		 		  		  /**
		 		  		   * Metodo devuelve el numero de dias de ajuste para cuando se pasa de 60
		 		  		   * @param IDE_GEEDP
		 		  		   * @return
		 		  		   */
		 		  			  public double nroDiasAjustePeriodo(String IDE_GEEDP, String ide_asvac) {
		 		  		       
		 		  			   double numeroDiasTomadoss=0.0;
		 					   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
		 				        if (!tab_periodo.isEmpty()) {
		 					    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_ACUMULADO "
		 				        		+ "FROM ( "
		 				        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
		 				        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
		 				        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC "
		 				        	    + " ) a where IDE_ASVAC="+ ide_asvac);

		 					    	   if (tab_numero_tomados.getValor("DIA_ACUMULADO")==null || tab_numero_tomados.getValor("DIA_ACUMULADO").isEmpty())
		 					    	   {
		 					    		   numeroDiasTomadoss=0.0;
		 			      		       }else {
		 			      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_ACUMULADO"));	
		 						  //     System.out.println("cambio de valores"+numeroDiasTomadoss);
		 			      		       
		 			      		       }
		 			         		   return  numeroDiasTomadoss;

		 				               }
		 				      return  0;
		 		  				  
		 		  			  }
		 		  		   
		 		  	   
		 		  	   

		 		  			
		 		  			//DIAS TOMADOS POR VACACION
		 		  					
		 		  					public static List<Double> getVacacionesTomadasXPeriodo(Double numeroDiasTomados, List<Integer> peridos, Double numeroDiasVacacionXAnio) {
		 		  						List<Double> vaxacionXPeriodo = new ArrayList<Double>();
		 		  						double ndeAux = numeroDiasTomados;
		 		  						int valorVacacionesPeriodo=0;

		 		  						for (Integer periodo : peridos) {
		 		  							valorVacacionesPeriodo++;
		 		  							if (valorVacacionesPeriodo==peridos.size()) {
		 		  								//vaxacionXPeriodo.add(ndeAux);
		 		  								if (ndeAux >= 0){
	    		 		  							vaxacionXPeriodo.add(ndeAux);
			 		  								}
			 		  								else{
			 		  									vaxacionXPeriodo.add(0.0);
			 		  								}
		 		  								
											}else {
		 		  							if (ndeAux >= numeroDiasVacacionXAnio){
		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
		 		  								vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
		 		  							}else{
		 		  								if (ndeAux >= 0){
		 		  								vaxacionXPeriodo.add(ndeAux);
		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
		 		  								}else{
		 		  									vaxacionXPeriodo.add(0.0);
		 		  								}
		 		  							}
		 		  						}
		 		  						}
		 		  						return vaxacionXPeriodo;
		 		  					}
		 		  					
		 		  					
		 		  					
		 		  					
		 		  					
		 		  					
		 		  					/**
		 		  					 * Metodo que crea la 		
		 		  					 * @param vacacionesPeriodo
		 		  					 * @param vacacionesTomadas
		 		  					 * @return
		 		  					 */
		 		  							
		 		  							public static List<Double[]> getVacacionesMatriz(List<Double> vacacionesPeriodo, List<Double> vacacionesTomadas) {
		 		  								List<Double[]> matriz = new ArrayList<Double[]>();
		 		  								
		 		  								Integer dimension = vacacionesPeriodo.size();
		 		  								for (Integer i = 0; i < dimension; i++) {
		 		  									Double[] obj = new Double[5];
		 		  									obj[0] = i.doubleValue() + 1; // periodo
		 		  									obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
		 		  									//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
		 		  									obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
		 		  								//	obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
		 		  									obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
		 		  								//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
		 		  									obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
		 		  								//	obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
		 		  									
		 		  									
		 		  									if ((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i))>0.0 || (vacacionesPeriodo.get(i) - vacacionesTomadas.get(i))>0) {
														
													}
		 		  									
		 		  									matriz.add(obj);
		 		  								}
		 		  								return matriz;
		 		  							}
		 		  							
		 		  							/**
		 		  							 * 		
		 		  							 * @param fechaIngresoEmpleado
		 		  							 * @return
		 		  							 */

		 		  									public  Double getNumeroDiasPendientes(int nde, int nda, double numeroDiasTomados,double numeroDiasVacacionXAnio){

		 		  										List<Integer> peridos = getPeriodos(nda, nde);
		 		  										List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
		 		  										List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos, numeroDiasVacacionXAnio);

		 		  										List<Double[]> matriz = getVacacionesMatriz(vacacionesPeriodo, vacacionesTomadas);
		 		  										Double diasPendientes = 0.0;
		 		  										for (Double[] vacaciones : matriz) {
		 		  											diasPendientes = diasPendientes + vacaciones[3];	
		 		  										
		 		  										}
		 		  										return diasPendientes;
}
		 		  									
		 		  									
		 		  									/**
		 		  								    * Metodo que devuelve el ide maximo de una tabla
		 		  									* @return String SQL Codigo maximo de los ide primarios de de las tablas
		 		  									*/

		 		  									public String servicioCodigoMaximo(String tabla,String ide_primario){
		 		  									String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		 		  									return maximo;
		 		  									}
		 		  								     		  									



		 		  								//METODOS DE PERIODO VACACIONES

		 		  									//CALCULO DE NUMERO DE DIAS 

		 		  									/**
		 		  									 * calcular numero de dias calendario que el empleado lleva en la empresa
		 		  									 * @param fechaIngresoEmpleado
		 		  									 * @return
		 		  									 */
		 		  									public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC){

		 		  										TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
		 		  												+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
		 		  									   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		 		  									   String dateInString = "";
		 		  									   if ((tab_periodo.getValor("fecha_ingreso_asvac")==null) || (tab_periodo.getValor("fecha_ingreso_asvac").equals("")) || (tab_periodo.getValor("fecha_ingreso_asvac").isEmpty()) ) {
		 		  										   return 0;
		 		  									   }else {
		 		  										   dateInString=tab_periodo.getValor("fecha_ingreso_asvac");
		 		  									   }
		 		  									   
		 		  									   boolean activo_asvac=false;
		 		  									//   if ((tab_periodo.getValor("fecha_ingreso_asvac")==null) || (tab_periodo.getValor("fecha_ingreso_asvac").equals("")) || (tab_periodo.getValor("fecha_ingreso_asvac").isEmpty()) ) {
//		 		  									 	   return 0;
		 		  									//   }else {
//		 		  									 	   dateInString=tab_periodo.getValor("fecha_ingreso_asvac");
		 		  									//   }



		 		  									   Boolean.parseBoolean(tab_periodo.getValor("ACTIVO_ASVAC"));
		 		  									   String fecha_finiquito=null;
		 		  									   
		 		  									   	 int diaFin=0;
		 		  											 int mesFin=0; // 0 Enero, 11 Diciembre
		 		  											 int anioFin=0;
		 		  									        

		 		  									    
		 		  											Date date = null;
		 		  											Date dateFechaCalculoFiniquito = null;
		 		  											/**
		 		  											 * Le asigno a una variable de tipo date la fecha de ingreso
		 		  											 */
		 		  												
		 		  									/*		try {
		 		  												date = sdf.parse(dateInString);
		 		  											} catch (ParseException e) {
		 		  												// TODO Auto-generated catch block
		 		  												e.printStackTrace();
		 		  											}
		 		  											
		 		  										*/	
		 		  											
		 		  												
		 		  									   	 try {
		 		  											if (tab_periodo.getValor("fecha_finiquito_asvac")==null || tab_periodo.getValor("fecha_finiquito_asvac").isEmpty()){
		 		  											   		try {
		 		  											    			date = sdf.parse(dateInString);
		 		  											   		       } catch (ParseException e) {
		 		  											    			// TODO Auto-generated catch block
		 		  											    			e.printStackTrace();
		 		  											    		}
		 		  									        		
		 		  									        		Calendar calendarFin = Calendar.getInstance();
		 		  									        		calendarFin.setTime(utilitario.getDate());
		 		  									        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		 		  									        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		 		  									        		 anioFin = calendarFin.get(Calendar.YEAR);
		 		  											
		 		  											
		 		  											}else{
		 		  											if (activo_asvac==false){
		 		  												   fecha_finiquito = tab_periodo.getValor("fecha_finiquito_asvac");
		 		  												   
		 		  												   if (utilitario.getDia(fecha_finiquito)==31) {
		 		  													   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), -1));
		 		  																		
		 		  												   }
		 		  												   
		 		  												   if (utilitario.getMes(fecha_finiquito)==2 || utilitario.getMes(fecha_finiquito)==02) {
		 		  													
		 		  											
		 		  												   if (utilitario.getDia(fecha_finiquito)==29) {
		 		  													   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
		 		  																		
		 		  												   }
		 		  												   
		 		  												   
		 		  												   if (utilitario.getDia(fecha_finiquito)==28) {
		 		  													   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 2));
		 		  																		
		 		  												   }
		 		  												   
		 		  													}
		 		  											try {
		 		  												date = sdf.parse(dateInString);
		 		  														dateFechaCalculoFiniquito = sdf.parse(fecha_finiquito);
		 		  												   	
		 		  											} catch (ParseException e) {
		 		  												// TODO Auto-generated catch block
		 		  												//e.printStackTrace();
		 		  												System.out.println();
		 		  											}
		 		  											

		 		  													Calendar calendarFin = Calendar.getInstance();
		 		  									    			calendarFin.setTime(dateFechaCalculoFiniquito);
		 		  									    			 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		 		  									        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		 		  									        		 anioFin = calendarFin.get(Calendar.YEAR);
		 		  												
		 		  												
		 		  											}else if (activo_asvac==true){
		 		  												try {
		 		  									    			date = sdf.parse(dateInString);
		 		  									   		//		dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
		 		  									    	   	
		 		  									   		       } catch (ParseException e) {
		 		  									    			// TODO Auto-generated catch block
		 		  									    			e.printStackTrace();
		 		  									    		}
		 		  									       		
		 		  									   		
		 		  											
		 		  											
		 		  											
		 		  											Calendar calendarFin = Calendar.getInstance();
		 		  											calendarFin.setTime(utilitario.getDate());
		 		  											 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		 		  											 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		 		  											 anioFin = calendarFin.get(Calendar.YEAR);
		 		  												
		 		  												
		 		  											}else {
		 		  												utilitario.agregarMensajeError("No contiene Vacaciones", "No se pudo encontrar un registro para el empleado");
		 		  											}
		 		  											
		 		  												
		 		  											}	
		 		  												
		 		  												
		 		  										} catch (Exception e) {
		 		  											// TODO Auto-generated catch block
		 		  											e.printStackTrace();
		 		  										}
		 		  									   		

		 		  										
		 		  											

		 		  											Calendar calendarInicio = Calendar.getInstance();
		 		  											calendarInicio.setTime(date);
		 		  											int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
		 		  											int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		 		  											int anioInicio = calendarInicio.get(Calendar.YEAR);
		 		  											
		 		  											int anios = 0;
		 		  											int mesesPorAnio = 0;
		 		  											int diasPorMes = 0;
		 		  											int diasTipoMes = 30;

		 		  												if (mesInicio <= mesFin) {
		 		  													anios = anioFin - anioInicio;
		 		  													if (diaInicio <= diaFin) {
		 		  														mesesPorAnio = mesFin - mesInicio;
		 		  														diasPorMes = diaFin - diaInicio;
		 		  													} else {
		 		  														if (mesFin == mesInicio) {
		 		  															anios = anios - 1;
		 		  														}
		 		  														mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
		 		  														diasPorMes = diasTipoMes - (diaInicio - diaFin);
		 		  													}
		 		  										    	}else{
		 		  													anios = anioFin - anioInicio - 1;
		 		  													//System.out.println(anios);
		 		  													if (diaInicio > diaFin) {
		 		  														mesesPorAnio = mesFin - mesInicio - 1 + 12;
		 		  														diasPorMes = diasTipoMes - (diaInicio - diaFin);
		 		  													} else {
		 		  														mesesPorAnio = mesFin - mesInicio + 12;
		 		  														diasPorMes = diaFin - diaInicio;
		 		  										    	}
		 		  											}
		 		  											
		 		  									       			
		 		  									       		//System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");

		 		  									       		//System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
		 		  									       	 

		 		  										 
		 		  											int returnValue = -1;
		 		  													returnValue = anios * 12 + mesesPorAnio;
		 		  									       				//System.out.println("Total meses: " + returnValue + " Meses.");

		 		  									       				returnValue =(((returnValue*30)+diasPorMes))+1;
		 		  									       				//System.out.println("Total dias: " + returnValue);
		 		  													 return returnValue;
		 		  											
		 		  											


		 		  									}



		 		  									private Date getFechaAsyyyyMMddHHmmss(String fecha){
		 		  									    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 		  									    Date fechaDate = new Date();
		 		  									    try {
		 		  									    	fechaDate = df.parse(fecha);
		 		  									    	return fechaDate;
		 		  										} catch (ParseException e) {
		 		  											// TODO Auto-generated catch block
		 		  											e.printStackTrace();
		 		  										}
		 		  									    return null;

		 		  								    }

		 		  								    
		 		  								    private String getFechaAsyyyyMMddHHmmss(Date fecha){
		 		  									    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 		  									    return df.format(fecha);
		 		  									    }
		 		  								    
		 		  								    
		 		  								    
		 		  								    private Date getFechaAsyyyyMMdd(String fecha){
		 		  									    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 		  									    Date fechaDate = new Date();
		 		  									    try {	
		 		  									    	fechaDate = df.parse(fecha);
		 		  									    	return fechaDate;
		 		  										} catch (ParseException e) {
		 		  											// TODO Auto-generated catch block
		 		  											e.printStackTrace();
		 		  										}
		 		  									    return null;

		 		  								    }
		 		  								    private String getFechaAsyyyyMMdd(Date fecha){
		 		  									    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 		  									    return df.format(fecha);
		 		  								    }

	

		 		  									
		 		  									public String getDatosEmpleado(String IDE_GTEMP,int ide_gttem,String ide_asvac,String RMU,String fecIniVacacion,String fecFinVacacion,int dias_exceso_liquidacion,boolean band_liquidar_marcaciones,boolean ejecuta_rmu){
		 		  										
		 		  									    int bandEntrada=0;
		 		  									    int bandSalida=0;
		 		  										double dias_pendientes=0.0;
		 		  										int nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		 		  										TablaGenerica tabla_vacacion=utilitario.consultar(retornaPeriodoVacacion(ide_asvac));
		 		  										boolean estado_periodo=false;
		 		  										//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
		 		  										int nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac);
		 		  										//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
		 		  										int ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

		 		  										// Recalculo los valores para q no se pasen de 360 dias al año
		 		  										//nde = getDiasEmpleadoXAnio(nde, nda);
		 		  										//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

		 		  										// Obtener los periodos de cada empleado 1,2,3 etc
		 		  										List<Integer> peridos = getPeriodos(nda, nde);
		 		  										
		 		  										
		 		  										// Obtengo los periodos para el ajuste de dias pendientes del
		 		  										// empleado hasta el 30 de abril del 2017
		 		  										List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);

		 		  									
		 		  									
		 		  										
		 		  										
		 		  										
		 		  										//System.out.println("Tipo empleado: "+ide_gttem);
		 		  										double numeroDiasVacacionXAnio=0.0;
		 		  										int p_asi_dias_max_vacaciones_codigo_trabajo=0,p_asi_dias_max_vacaciones_losep=0;
		 		  										double division=0.0;
		 		  										if (ide_gttem == 1) {
		 		  											// Asigno el numero de dias max de vacaciones al año 15 dias
		 		  											numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
		 		  											// Los dias maximo q puede acumular 45 dias por tres periodos
		 		  											p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
		 		  											
		 		  										//Aumentamos dia por trabajar mas de 5 anios
		 		  											if(peridos.size()>5){
		 		  												double anio_periodo=peridos.size();
		 		  												 division=anio_periodo/5;
		 		  												nde=nde+(int)division;
		 		  											}
		 		  											
		 		  										
		 		  										}

		 		  										if (ide_gttem == 2) {
		 		  											// Asigno el numero de dias max de vacaciones al año 15 dias
		 		  											numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
		 		  											// Los dias maximo q puede acumular 45 dias por tres periodos
		 		  											p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));

		 		  										}
		 		  										
		 		  										
		 		  										
		 		  										
		 		  										
		 		  										
		 		  										
		 		  										// Obtengo el total de mis dias pendientes al restar los dias
		 		  										// acumulados-dias tomados
		 		  										double sumatotalDiasGenerados = 0.0;
		 		  										// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		 		  										// que le corresponde y los dias generados hasta el presente
		 		  										List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
		 		  										// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		 		  										// que le corresponde y los dias generados hasta el 30 de abril de
		 		  										
		 		  										List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
		 		  										 double sumatotal_vacaciones = 0.0;
		 		  										// Asigno la sumatoria de total de mis dias acumulados a vacacion
		 		  										// desde la fecha de ingreso hasta la fecha de hoy
		 		  										 
		 		  										 BigDecimal num1 = new BigDecimal(0);

		 		  										for (int i = 0; i < vacacionesPeriodo.size(); i++) {
		 		  											sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
		 		  									             	
		 		  										}

		 		  									//	System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
		 		  										 double sumatotal_vacacionesCalculoInicial = 0.0;

		 		  										// Asigno la sumatoria de total de mis dias acumulados a vacacion
		 		  										// desde la fecha de ingreso hasta el 30 de Abril de 2017
		 		  										for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
		 		  											sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
		 		  										}
		 		  										//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

		 		  										// Devuelve el numero de dias pendientes obtenidos de excel hasta el
		 		  										// 30 de abril
		 		  									
		 		  										 
		 		  										           double numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac);
		 		  													double totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac);
		 		  													double nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac);
		 		  													double diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac);
		 		  													
		 		  													
		 		  													
		 		  													
		 		  													  if(numeroDiasTomadosInicial!=0)
		 		  													  {
		 		  														
		 		  														if (numeroDiasTomadosInicial < 0) {
		 		  															double valor = 0.0;
		 		  															double valor1 = 0.0;
		 		  															valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
		 		  												            //valor=valor1-sumatotal_vacaciones;
		 		  															utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
		 		  															utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
		 		  								                totalNumeroDiasAjuste=valor;
		 		  																

		 		  														}
		 		  														if (numeroDiasTomadosInicial > 0) {
		 		  															double valor = 0.0;
		 		  															double valor1 = 0.0;
		 		  															valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
		 		  												            //valor=valor1-sumatotal_vacaciones;
		 		  															utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
		 		  																						utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
		 		  											                totalNumeroDiasAjuste=valor;

		 		  															}

		 		  													  
		 		  													  }
		 		  														


		 		  													double totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac);
		 		  								     				double numeroDiasTomados=totalNumeroDiasAjuste;
		 		  													double numeroDiasTomadosTemporal = numeroDiasTomados;

		 		  													// suma el valor de los dias descontados de la tabla
		 		  													// asi_detalle_vacacion
		 		  													// Aqui se encuentra el cuadre
		 		  													double dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
		 		  													int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
		 		  													double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);

		 		  													
		 		  												
		 		  													
		 		  												   double numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
		 		  													
		 		  													
		 		  													double resultado_descuento = descuento + numeroDiasTomados;
		 		  											
		 		  										
		 		  													if (ide_gttem == 2) {

		 		  														double nro_dias_ajuste_periodo_asvac = 0;
		 		  																TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
		 		  																		+ ide_asvac+" and activo_asvac=true");

		 		  																if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 60) {
		 		  																	//System.out.println("Ingreso a descuento mayor a 60");
		 		  																	
		 		  																	BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
		 		  																	BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
		 		  																	BigDecimal descuento1= new BigDecimal(descuento);
		 		  																	BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
		 		  																	BigDecimal  dias= new BigDecimal(60);
		 		  																	BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
		 		  																	BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

		 		  																	if (calculoPasaDiasPendientes.doubleValue()>=0.01) {
		 		  																
		 		  																	
		 		  																	TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
		 		  													          utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
		 		  													+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
		 		  																
		 		  																	}
		 		  													          nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;

		 		  													          
		 		  																}
		 		  															}

		 		  											if (ide_gttem == 1) {

		 		  																double nro_dias_ajuste_periodo_asvac = 0;
		 		  																TablaGenerica tab_codigo_vacacion = utilitario
		 		  																		.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
		 		  																				+ ide_asvac+" and activo_asvac=true");
		 		  															

		 		  																if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 45) {
		 		  																	
		 		  																//	System.out.println("Ingreso a descuento mayor a 45");

		 		  																	
		 		  																	BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
		 		  																	BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
		 		  																	BigDecimal descuento1= new BigDecimal(descuento);
		 		  																	BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
		 		  																	BigDecimal  dias= new BigDecimal(45);
		 		  																	BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
		 		  																	BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

		 		  																	if (calculoPasaDiasPendientes.doubleValue()>=0.01) {

		 		  																	TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
		 		  																	utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
		 		  																	+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
		 		  																	}
		 		  																	nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;
		 		  																									
		 		  																}
		 		  															}
		 		  											
		 		  											       numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac);
		 		  												   numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

		 		  													/*
		 		  													 * Tomo los parametros creados para realizar los calculos de los
		 		  													 * dias pendientes calculo de vacaciones dependiendo si el empleado
		 		  													 * pertenece a la losep o al codigo de trabajo
		 		  													 */


		 		  													List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);
		 		  													//for (int i = 0; i < 1; i++) {
		 		  														//System.out.println("vacacionesTomadas: " + vacacionesTomadas);
		 		  												//	}

		 		  													String periodoNuevo="";
		 		  													String periodoNuevoEntrada="",periodoNuevoSalida="";
		 		  													int valorDiasXAnio=0;
		 		  													double valorRetornoVacaciones=0.0;
		 		  													List<Double[]> matriz = new ArrayList<Double[]>();
		 		  													double valorRetorno=0.0;	  														
		 		  														Integer dimension = vacacionesPeriodo.size();
		 		  														for (Integer i = 0; i < dimension; i++) {
		 		  															Double[] obj = new Double[5];
		 		  															obj[0] = i.doubleValue() + 1; // periodo
		 		  															obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
		 		  														//	obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
		 		  															obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
		 		  														 //   obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
		 		  															obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
		 		  														//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
		 		  															obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
		 		  														  //  obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;

		 	 							
		 		  												/*			if (band_liquidar_marcaciones==true){
		 		  														
		 		 
		 		  					
valorRetorno=0.0;

//if (dimension>1) {
	double valorDiasPendientes=0;
	valorDiasPendientes=Double.parseDouble(utilitario.getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)),2));
	if (valorDiasPendientes!=0) {
		if (i==0) {
			//CUANDO ES LA PRIMERA ITERACION
			
			double valorDias=0.0;
			
			int dia=0,mes=0,anioFin=0;
			String diaFin="",mesFin="";
			dia=utilitario.getDia(fecIniVacacion);
			if (dia<10) {
				diaFin="0"+dia;
			}else {
				diaFin=""+dia;
			}
			
			mes=utilitario.getMes(fecIniVacacion);
			if (mes<10) {
				mesFin="0"+mes;
			}else {
				mesFin=""+mes;
		 		  															}
			mes=utilitario.getMes(fecIniVacacion);
			anioFin=utilitario.getAnio(fecIniVacacion)+1;
			
			String fechaTemp=anioFin+"-"+mesFin+"-"+diaFin;
			
			//utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaTemp), -1));
			
			valorRetorno=getValorVacacionesPagarCT(fecIniVacacion,utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaTemp), -1)),IDE_GTEMP,RMU,dias_exceso_liquidacion);
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			double tempValorVacaciones=valorRetorno/360;
			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			
			System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre=360,activo_liquidacion=true,"
					+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
					+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));

		
		}else if(i==(dimension-1)){
			//CUANDO TERMINA DE RECORRER EL BUCLE FOR
			
			int diaTemp=0,mesTemp=0;
			String diaTempTexto="",mesTempTexto="";
			
			if (utilitario.getDia(fecIniVacacion)<10) {
				diaTempTexto="0"+utilitario.getDia(fecIniVacacion);
			}else {
				diaTempTexto=""+utilitario.getDia(fecIniVacacion);
			}
			
			
			if (utilitario.getMes(fecIniVacacion)<10) {
				mesTempTexto="0"+utilitario.getMes(fecIniVacacion);
			}else {
				mesTempTexto=""+utilitario.getMes(fecIniVacacion);
			}
			
			
			valorRetorno=getValorVacacionesPagarCT((utilitario.getAnio(fecIniVacacion)+(i))+"-"+mesTempTexto+"-"+diaTempTexto,fecFinVacacion,IDE_GTEMP,RMU,dias_exceso_liquidacion);
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			double valorDias=0.0;
			
			int dia=0,mes=0,anioFin=0;
			String diaFin="",mesFin="";
			dia=utilitario.getDia(fecIniVacacion);
			if (dia<10) {
				diaFin="0"+dia;
			}else {
				diaFin=""+dia;
			}
			
			mes=utilitario.getMes(fecIniVacacion);
			if (mes<10) {
				mesFin="0"+mes;
			}else {
				mesFin=""+mes;
			}
			mes=utilitario.getMes(fecIniVacacion);
			anioFin=utilitario.getAnio(fecIniVacacion)+i;
			
			String fechaTemp=anioFin+"-"+mesFin+"-"+diaFin;
	 
			//valorDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(fechaTemp), utilitario.DeStringADate(fecFinVacacion));
			valorDias=calcular360Dias(utilitario.DeStringADate(fechaTemp), utilitario.DeStringADate(fecFinVacacion))+1;
			double tempValorVacaciones=valorRetorno/valorDias;

			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
			
			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+valorDias+",activo_liquidacion=true,"
					+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
					+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));

			
			
		}else {
			
			valorRetorno=getValorVacacionesPagarCT((utilitario.getAnio(fecIniVacacion)+(i))+"-"+utilitario.getMes(fecIniVacacion)+"-"+(utilitario.getDia(fecIniVacacion)),((utilitario.getAnio(fecIniVacacion)+i)+1)+"-"+utilitario.getMes(fecIniVacacion)+"-"+(utilitario.getDia(fecIniVacacion)-1),IDE_GTEMP,RMU,dias_exceso_liquidacion);
			System.out.println("VALOR RETORNO: "+i+"   TOTAL:  "+valorRetorno);
			double tempValorVacaciones=valorRetorno/360;
			valorRetornoVacaciones+=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			double valorRetornoVacacionesTemp=0.00;
			valorRetornoVacacionesTemp=Double.parseDouble(utilitario.getFormatoNumero(tempValorVacaciones,2))*valorDiasPendientes;
			
			System.out.println("VALOR RETORNO VACACIONES: "+i+"   TOTAL:  "+valorRetornoVacaciones);
			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
					+ "SET base_imponible_asvre="+valorRetorno+",dias_trabajados_liquidacion_asvre="+360+",activo_liquidacion=true,"
					+ "valor_pagar_asvre="+utilitario.getFormatoNumero(valorRetornoVacacionesTemp,2)+",valor_dia_asvre="+tempValorVacaciones 
					 + " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(i+1));
		}
			
      }

	}//si se requiere ingresar los valores de liquidacion										
		 		  															else{}	 	*/	  													}//for
		 		  													
		 		  													/**
		 		  													 * Obtencion de dias pendientes del empleado
		 		  													 */
		 		  													// System.out.println("numero dias pendientes vacacion: "+
		 		  													// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
		 		  													
		 		  													dias_pendientes = getNumeroDiasPendientes(nde,nda, numeroDiasTomados, numeroDiasVacacionXAnio);
		 		  													// redondeo el valor
		 		  													//dias_pendientes = Math.rint(dias_pendientes * 100) / 100;


		 		  													// //////////////////////////////////////////////////7vsvnkkskvdvskvnvdsknsd/////////////////////////////////////////////////7
		 		  													dias_pendientes = (sumatotal_vacaciones - numeroDiasTomados);
		 		  													//dias_pendientes = Math.rint(dias_pendientes * 100) / 100;


		 		  													double numeroInicioFinesSemana = (int) ((sumatotal_vacaciones*4) / 30);
		 		  													double numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);
		 		  													double numeroInicioFinesSemanaPendientes = numeroInicioFinesSemana
		 		  															- numeroInicioFinesSemanaSolicitados;
		 		  													sumatotal_vacaciones = Math.rint(sumatotal_vacaciones * 100) / 100;

		 		  										
		 		  													/*etiDiasAcumulados.setValue(sumatotal_vacaciones);
		 		  												    etiNroFinesSemana.setValue(numeroInicioFinesSemana);
		 		  													etiDiasDescontados.setValue(numeroDiasTomados);
		 		  													etiDescuentoFinesSemana.setValue(numeroInicioFinesSemanaSolicitados);
		 		  													etiDiasPendientes.setValue(dias_pendientes);
		 		  													etiNroFinesSemanaPendientes.setValue(numeroInicioFinesSemanaPendientes);
		 		  												*/
		 		  													
		 		  													if (ejecuta_rmu==true) {
																	double temp=0.0;
		 		  													temp=(Double.parseDouble(RMU)/30) * Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes,2)); 
		 		  													System.out.println(dias_pendientes);
		 		  													int dias_trabajados=0;
		 		  													
		 		  													dias_trabajados=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fecIniVacacion), utilitario.DeStringADate(fecFinVacacion));
		 		  													if (dias_trabajados>=360) {
		 		  														dias_trabajados=getDias360(fecIniVacacion, fecFinVacacion);
																	}else {
																		dias_trabajados=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fecIniVacacion),utilitario.DeStringADate(fecFinVacacion));
																	}
		 		  													
		 		  												
		 		  													
		 		  													if (dias_pendientes>=0) {
																		
																	
		 		  													utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
		 		  						 	 								+ "SET base_imponible_asvre="+RMU+",dias_trabajados_liquidacion_asvre="+dias_trabajados+",activo_liquidacion=true,"
		 		  						 	 								+ "valor_pagar_asvre="+utilitario.getFormatoNumero(temp)+",valor_dia_asvre="+utilitario.getFormatoNumero((Double.parseDouble(RMU)/30),2) 
		 		  						 	 								+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(dimension));
		 		  													}else {
		 		  														utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
			 		  						 	 								+ "SET base_imponible_asvre="+RMU+",dias_trabajados_liquidacion_asvre="+dias_trabajados+",activo_liquidacion=true,"
			 		  						 	 								+ "valor_pagar_asvre=0.0,valor_dia_asvre="+utilitario.getFormatoNumero((Double.parseDouble(RMU)/30),2) 
			 		  						 	 								+ " WHERE ide_asvac="+ide_asvac+"  and ide_periodo_asvre="+(dimension));
			 		  									
																	}
		 		  													utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado "
		 		  						 	 								+ "SET activo_asvre=false "
		 		  						 	 								+ "WHERE ide_asvac="+ide_asvac);
																	}
		 		  													return ""+Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes,2));
		 		  															 		  														}
	
		 		  									
		 		  									
public double getValorVacacionesPagarCT(String periodoNuevoEntrada, String periodoNuevoSalida,String IDE_GTEMP,String RMU,int dias_exceso){

    double valorRetorno=0.0;
		if (utilitario.getAnio(periodoNuevoEntrada)==utilitario.getAnio(periodoNuevoSalida)) {
			//Obtengo el año
			TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(periodoNuevoSalida)+"%'");
			String anioParte1=tabAnio1.getValor("IDE_GEANI");
			
			StringBuilder str_ide_mes= new StringBuilder();
			
			//variables que registran el mes de inicio y el mes de fin
			int mesIniValidacion=utilitario.getMes(periodoNuevoEntrada);
			int mesFinValidacion=utilitario.getMes(periodoNuevoSalida);
			//variables donde se guardaran los valores generados por rubro base imponible
			double  baseImponibleVacacionValorTemp1=0.0,baseImponibleVacacionValorTemp2=0.0,baseImponibleVacacionValorTemp3=0.0,baseImponibleVacacionValorTemp4=0.0;
			double valorDias=0,valoDiasInicio=0,valoDiasFin=0;
			double valorDiasFinal=0;
			boolean bandTemp=false;
			String fechaIniBaseImponinble="",fechaFinBaseImponinble="";
			//variables de dias trabajados en la fecha de entrada del empleado y la fecha de salida
			valorDias=calcular360Dias(utilitario.DeStringADate(periodoNuevoEntrada),utilitario.DeStringADate(periodoNuevoSalida));
			valoDiasFin=calcular360Dias(utilitario.DeStringADate(periodoNuevoSalida), utilitario.DeStringADate(periodoNuevoSalida))+1;
			
			
			fechaIniBaseImponinble=utilitario.getAnio(periodoNuevoEntrada)+"-"+utilitario.getMes(periodoNuevoEntrada)+"-30";
			fechaFinBaseImponinble=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
			
			if (utilitario.getDia(periodoNuevoSalida)>30) {
				valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaFinBaseImponinble), utilitario.DeStringADate(utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-30"))+1;
			}else {
				valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaFinBaseImponinble), utilitario.DeStringADate(periodoNuevoSalida))+1;
			}
			
			
			
			valoDiasInicio=calcular360Dias(utilitario.DeStringADate(periodoNuevoEntrada),utilitario.DeStringADate(fechaIniBaseImponinble))+1;
			
			
			
			
			
			
			if (mesIniValidacion==mesFinValidacion){
				valorDias=0;
				
				
				//Genero el ultimo mes en el cual ajuste los dias de acuerdo a la base imponible
				TablaGenerica valor_base_meses_final=null;
				
				valor_base_meses_final=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinValidacion+") and ide_geani in("+anioParte1+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
					if (valor_base_meses_final.getValor("valor_nrdro")==null || valor_base_meses_final.getValor("valor_nrdro").equals("") || valor_base_meses_final.getValor("valor_nrdro").isEmpty()) {
						//si no tengo en ese mes consulto un mes anteiror
					
						int sumaMes=0,sumaAnio=0;
						
						valor_base_meses_final=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
								+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
								+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+(mesFinValidacion-1)+") and ide_geani in("+anioParte1+"))  "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_rmu")+") and  "
								+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
							if (valor_base_meses_final.getValor("valor_nrdro")==null || valor_base_meses_final.getValor("valor_nrdro").equals("") || valor_base_meses_final.getValor("valor_nrdro").isEmpty()) {
									TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
											+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
											+ "from gen_empleados_departamento_par epar "
											+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
											+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
											+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
											+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
											+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
											+ "order by ide_geedp desc");
									
									valor_base_meses_final=utilitario.consultar("select ide_geedp,rmu_geedp "
											+ "from gen_empleados_departamento_par where ide_geedp in ("+tab_empleado_departamento_par.getValor("IDE_GEEDP")+")");
									baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("rmu_geedp"))/30)*valorDiasFinal),2));
								}else {
									baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
							}
				
						
					}else {
						baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
					}
				
				
			}else {
				
				 int periodo=0;
		    //guardo en todos los meses que requiero consultar en la variable str_ide_mes
				if (dias_exceso==0) {
					if (utilitario.getMes(periodoNuevoSalida)==1) {
						periodo=12;
					}else {
						periodo=utilitario.getMes(periodoNuevoSalida)-1;
					}					
				}else {
					periodo=utilitario.getMes(periodoNuevoSalida);
				}
		
			for (int i = (mesIniValidacion)+1; i < periodo; i++) {
				str_ide_mes.append(""+i);
				if (i==(periodo-1)) {
					
				}else {
					str_ide_mes.append(",");
				}
			}
		
			TablaGenerica valor_base_inicio=null;
					
				//consulto el primer la base imponible del primer mes
				valor_base_inicio=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
						+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
						+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesIniValidacion+") and ide_geani in("+anioParte1+"))  "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
						+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
						+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
				
						//if base_inicio_contiene valores
							if (valor_base_inicio.getValor("valor_nrdro")==null || valor_base_inicio.getValor("valor_nrdro").equals("") || valor_base_inicio.getValor("valor_nrdro").isEmpty()) {
								//Consulto el siguiente mes
											
								//consulto el valor de remuneracion si existe en el mes anterior
								valor_base_inicio=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
										+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
										+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesIniValidacion+") and ide_geani in("+anioParte1+"))  "
										+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
										+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_sri_301_sueldos_y_salarios")+") and  "
										+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
								
											if (valor_base_inicio.getValor("valor_nrdro")==null || valor_base_inicio.getValor("valor_nrdro").equals("") || valor_base_inicio.getValor("valor_nrdro").isEmpty()) {
												//SI NO EXISTE VALOR CONSULTO EL SUELDO en la accion de personal
												TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
														+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
														+ "from gen_empleados_departamento_par epar "
														+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
														+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
														+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
														+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
														+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
														+ "order by ide_geedp desc");
												
												valor_base_inicio=utilitario.consultar("select ide_geedp,rmu_geedp "
														+ "from gen_empleados_departamento_par where ide_geedp in ("+tab_empleado_departamento_par.getValor("IDE_GEEDP")+")");
												baseImponibleVacacionValorTemp1=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio.getValor("rmu_geedp"))/30)*valoDiasInicio),2));
											}else {
												baseImponibleVacacionValorTemp1=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio.getValor("valor_nrdro"))/30)*valoDiasInicio),2));
											}
																			
											
							//If si contiene base imponible en el mes de ingreso
							}else {
								//cierre if base_inicio_contiene valores
								baseImponibleVacacionValorTemp1=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(valor_base_inicio.getValor("valor_nrdro")),2));
								//baseImponibleVacacionValorTemp2=0.0;
								//baseImponibleVacacionValorTemp3=0.0;
							}
				
				
							
							
							
							boolean bandAjusteBaseImponible=false;
							if (str_ide_mes.length()>0) {
								bandAjusteBaseImponible=false;
							}else {
								bandAjusteBaseImponible=true;
							}
							
							
							
							TablaGenerica valor_base_meses=null;
							
						
						if (bandAjusteBaseImponible==false) {
							
						
							//Busco en meses que faltan antes de empezar el siguiente 
							valor_base_meses=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
									+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
									+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
									+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
									+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
									+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
								if(valor_base_meses.getTotalFilas()>0){
										if (valor_base_meses.getValor("valor_nrdro")==null || valor_base_meses.getValor("valor_nrdro").equals("") || valor_base_meses.getValor("valor_nrdro").isEmpty()) {
											baseImponibleVacacionValorTemp2=0;
										}else {
											baseImponibleVacacionValorTemp2=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(valor_base_meses.getValor("valor_nrdro")),2));
										}
										
								}
								
						}else {
							baseImponibleVacacionValorTemp2=0.0;
						}				
								
								//Genero el ultimo mes en el cual ajuste los dias de acuerdo a la base imponible
								TablaGenerica valor_base_meses_final=null;
								
								valor_base_meses_final=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
								+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
								+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinValidacion+") and ide_geani in("+anioParte1+"))  "
								+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
								+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
								+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
									if (valor_base_meses_final.getValor("valor_nrdro")==null || valor_base_meses_final.getValor("valor_nrdro").equals("") || valor_base_meses_final.getValor("valor_nrdro").isEmpty()) {
										//si no tengo en ese mes consulto un mes anteiror
									
										int sumaMes=0,sumaAnio=0;
										
										valor_base_meses_final=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
												+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
												+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+(mesFinValidacion-1)+") and ide_geani in("+anioParte1+"))  "
												+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
												+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_rmu")+") and  "
												+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
											if (valor_base_meses_final.getValor("valor_nrdro")==null || valor_base_meses_final.getValor("valor_nrdro").equals("") || valor_base_meses_final.getValor("valor_nrdro").isEmpty()) {
													TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
															+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
															+ "from gen_empleados_departamento_par epar "
															+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
															+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
															+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
															+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
															+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
															+ "order by ide_geedp desc");
													
													valor_base_meses_final=utilitario.consultar("select ide_geedp,rmu_geedp "
															+ "from gen_empleados_departamento_par where ide_geedp in ("+tab_empleado_departamento_par.getValor("IDE_GEEDP")+")");
													baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("rmu_geedp"))/30)*valorDiasFinal),2));
												}else {
													baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
											}
								
										
									}else {
										baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_meses_final.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
									}
								
			}//else
									valorRetorno=Double.parseDouble(utilitario.getFormatoNumero((baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2+baseImponibleVacacionValorTemp3),2));
			
		}else {

			TablaGenerica tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(periodoNuevoEntrada)+"%'");
			String anioParte1=tabAnio1.getValor("IDE_GEANI");
			int mesIniValidacion=utilitario.getMes(periodoNuevoEntrada);
			TablaGenerica tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(periodoNuevoSalida)+"%'");
			String anioParte2=tabAnio2.getValor("IDE_GEANI");
			int mesFinValidacion=utilitario.getMes(periodoNuevoSalida);
			StringBuilder str_ide_mes= new StringBuilder();
			StringBuilder str_ide_mes1= new StringBuilder();
			double  baseImponibleVacacionValorTemp=0.0,baseImponibleVacacionValorTemp1=0.0,baseImponibleVacacionValorTemp2=0.0,baseImponibleVacacionValorTemp3=0.0;
			TablaGenerica valor_base_inicio_primer_mes=null;
			TablaGenerica valor_base_mes_anterior1=null;
			TablaGenerica valor_base_mes_anterior2=null;
			TablaGenerica valor_base_inicio_ultimo_mes=null;
			double valorDias=0;
			double valorDiasFinal=0;
			
			
			//variables de dias trabajados en la fecha de entrada del empleado y la fecha de salida
			valorDias=utilitario.getDiferenciaFechas360(utilitario.DeStringADate(periodoNuevoEntrada),utilitario.DeStringADate(utilitario.getUltimoDiaMesFecha(periodoNuevoSalida)));
			if (valorDias>370) {
				valorDias=360;
			}
			String fechaInicial="",fechaFinal="";
			fechaInicial=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-"+utilitario.getDia(periodoNuevoEntrada);
			fechaFinal=utilitario.getUltimoDiaMesFecha(periodoNuevoSalida);
			valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaInicial), utilitario.DeStringADate(fechaFinal))+1;

			
			//if (mesIniValidacion==11) {
				for (int i = (mesIniValidacion)+1; i <= 12; i++) {
					str_ide_mes.append(""+i);
					if (i==12) {
						
					}else {
						str_ide_mes.append(",");
					}
				}
	

			if (mesFinValidacion==1) {
			//Si el mes de Fin periodo es enero
				str_ide_mes1.append("-1");
			}else {
			for (int z = 1; z <= (mesFinValidacion-1); z++) {
				str_ide_mes1.append(""+z);
				if (z==(mesFinValidacion-1)) {
					
				}else {
					str_ide_mes1.append(",");
				}
			}
	
			}  
			
			//consulto el primer la base imponible del primer mes
			valor_base_inicio_primer_mes=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
					+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
					+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesIniValidacion+") and ide_geani in("+anioParte1+"))  "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
					//p_nrh_rubro_remuneracion_unificada
					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
					+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
			
					//if base_inicio_contiene valores
						if (valor_base_inicio_primer_mes.getValor("valor_nrdro")==null || valor_base_inicio_primer_mes.getValor("valor_nrdro").equals("") || valor_base_inicio_primer_mes.getValor("valor_nrdro").isEmpty()) {
							//Consulto el siguiente mes
							int sumaMes=0;
							//Metodo para busqueda de remuneracion en rol de siguiente mes
							if (mesIniValidacion==12) {
								sumaMes=-11;
								tabAnio1=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(periodoNuevoEntrada)+1)+"%'");
								anioParte1=tabAnio1.getValor("IDE_GEANI");
								}else{
								sumaMes=1;
								}
													
							//consulto el valor de remuneracion si existe en el mes anterior
							valor_base_inicio_primer_mes=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
									+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
									+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+(mesIniValidacion+(sumaMes))+") and ide_geani in("+anioParte1+"))  "
									+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
									+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_sri_301_sueldos_y_salarios")+") and  "
									+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
							
										if (valor_base_inicio_primer_mes.getValor("valor_nrdro")==null || valor_base_inicio_primer_mes.getValor("valor_nrdro").equals("") || valor_base_inicio_primer_mes.getValor("valor_nrdro").isEmpty()) {
											//SI NO EXISTE VALOR CONSULTO EL SUELDO en la accion de personal
											TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
													+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
													+ "from gen_empleados_departamento_par epar "
													+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
													+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
													+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
													+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
													+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
													+ "order by ide_geedp desc");
											
											valor_base_inicio_primer_mes=utilitario.consultar("select ide_geedp,rmu_geedp "
													+ "from gen_empleados_departamento_par where ide_geedp in ("+tab_empleado_departamento_par.getValor("IDE_GEEDP")+")");
											baseImponibleVacacionValorTemp=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_primer_mes.getValor("rmu_geedp"))/30)*valorDiasFinal),2)); 
										}else {
										    baseImponibleVacacionValorTemp=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_primer_mes.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
										}
																		
										
						//If si contiene base imponible en el mes de ingreso
						}else {
							//cierre if base_inicio_contiene valores
							baseImponibleVacacionValorTemp=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_primer_mes.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
						}
				
				
			if (mesIniValidacion==12) {
				baseImponibleVacacionValorTemp1=0;
			}else{
			//Busco en meses que faltan antes de empezar el siguiente 
		valor_base_mes_anterior1=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
				+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
				+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes.toString()+") and ide_geani in("+anioParte1+"))  "
				+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
				+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
				+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
			if(valor_base_mes_anterior1.getTotalFilas()>0){
					if (valor_base_mes_anterior1.getValor("valor_nrdro")==null || valor_base_mes_anterior1.getValor("valor_nrdro").equals("") || valor_base_mes_anterior1.getValor("valor_nrdro").isEmpty()) {
						baseImponibleVacacionValorTemp1=0;
					}else {
						baseImponibleVacacionValorTemp1=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro")),2));
					}
					//baseImponibleVacacionValorTemp1+=Double.parseDouble(valor_base_mes_anterior1.getValor("valor_nrdro"));
			}
			
			
			}
			//si contengo que el mes =1 no recorre ya que este seria el ultimo generado en nomina
			if (mesFinValidacion>1) {
				//busco las bases imponibles desde el mes 1 al mes fin del siguiente año
			valor_base_mes_anterior2=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
					+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
					+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+str_ide_mes1.toString()+") and ide_geani in("+anioParte2+"))  "
					+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
					+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
					+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
		
			if(valor_base_mes_anterior2.getTotalFilas()>0){
					if (valor_base_mes_anterior2.getValor("valor_nrdro")==null || valor_base_mes_anterior2.getValor("valor_nrdro").equals("") || valor_base_mes_anterior2.getValor("valor_nrdro").isEmpty()) {
						baseImponibleVacacionValorTemp2=0;
					}else {
						baseImponibleVacacionValorTemp2=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro")),2));
					}
					//baseImponibleVacacionValorTemp2+=Double.parseDouble(valor_base_mes_anterior2.getValor("valor_nrdro"));
			}else {
				baseImponibleVacacionValorTemp2=0;	
			}
			
			}//si el mesFinValidacion =1
			else {
				baseImponibleVacacionValorTemp2=0;
			}
			
			
			
		//Genero el ultimo mes en el cual ajuste los dias de acuerdo a la base imponible
			
		valor_base_inicio_ultimo_mes=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
		+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
		+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+mesFinValidacion+") and ide_geani in("+anioParte2+"))  "
		+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
		+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_base_imponible")+") and  "
		+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
			if (valor_base_inicio_ultimo_mes.getValor("valor_nrdro")==null || valor_base_inicio_ultimo_mes.getValor("valor_nrdro").equals("") || valor_base_inicio_ultimo_mes.getValor("valor_nrdro").isEmpty()) {
				//si no tengo en ese mes consulto un mes anteiror
			
				int sumaMes=0,sumaAnio=0;
				//Metodo para busqueda de remuneracion en rol de siguiente mes
				if (mesFinValidacion==1) {
					sumaMes=11;
					tabAnio2=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(periodoNuevoSalida)-1)+"%'");
					anioParte2=tabAnio2.getValor("IDE_GEANI");
					
				}else{
					sumaMes=-1;
					sumaAnio=0;
											
				}
				valor_base_inicio_ultimo_mes=utilitario.consultar("select sum(valor_total) as valor_nrdro, sum(valor_total) as total1 from ( "
						+ "select sum(valor_nrdro) as valor_total from nrh_rol a, nrh_detalle_rol b  "
						+ "where a.ide_nrrol=b.ide_nrrol and ide_gepro in (select ide_gepro from gen_perido_rol where ide_gemes in("+(mesFinValidacion+sumaMes)+") and ide_geani in("+anioParte2+"))  "
						+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in ("+IDE_GTEMP+"))   "
						+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_valor_rmu")+") and  "
						+ "a.ide_nrdtn in(2,4) group by valor_nrdro) a");
					if (valor_base_inicio_ultimo_mes.getValor("valor_nrdro")==null || valor_base_inicio_ultimo_mes.getValor("valor_nrdro").equals("") || valor_base_inicio_ultimo_mes.getValor("valor_nrdro").isEmpty()) {
							TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
									+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
									+ "from gen_empleados_departamento_par epar "
									+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
									+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
									+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
									+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
									+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
									+ "order by ide_geedp desc");
							
							valor_base_inicio_ultimo_mes=utilitario.consultar("select ide_geedp,rmu_geedp "
									+ "from gen_empleados_departamento_par where ide_geedp in ("+tab_empleado_departamento_par.getValor("IDE_GEEDP")+")");

							fechaInicial="";
							fechaFinal="";
							fechaInicial=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
							fechaFinal=periodoNuevoSalida;
							valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaInicial), utilitario.DeStringADate(fechaFinal))+1;

							baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_ultimo_mes.getValor("rmu_geedp"))/30)*valorDiasFinal),2));
						}else {
							fechaInicial="";
							fechaFinal="";
							fechaInicial=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
							fechaFinal=periodoNuevoSalida;
							valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaInicial), utilitario.DeStringADate(fechaFinal))+1;
							baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_primer_mes.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
					}
		
				
			}else {
				fechaInicial="";
				fechaFinal="";
				fechaInicial=utilitario.getAnio(periodoNuevoSalida)+"-"+utilitario.getMes(periodoNuevoSalida)+"-01";
				fechaFinal=periodoNuevoSalida;
				valorDiasFinal=calcular360Dias(utilitario.DeStringADate(fechaInicial), utilitario.DeStringADate(fechaFinal))+1;
				baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(valor_base_inicio_ultimo_mes.getValor("valor_nrdro"))/30)*valorDiasFinal),2));
				//baseImponibleVacacionValorTemp3=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(valor_base_inicio_ultimo_mes.getValor("valor_nrdro"))/valorDiasFinal)));
			}
			valorRetorno=Double.parseDouble(utilitario.getFormatoNumero((baseImponibleVacacionValorTemp+baseImponibleVacacionValorTemp1+baseImponibleVacacionValorTemp2+baseImponibleVacacionValorTemp3),2));

		}

		 		  											
	 	return valorRetorno;	  									



}

public static int calcular360Dias(Date ini, Date fin)
{ 
Calendar cini = Calendar.getInstance(); 
cini.setTime(ini); 
Calendar cfin = Calendar.getInstance(); 
cfin.setTime(fin); 
int anios = cfin.get(Calendar.YEAR) - cini.get(Calendar.YEAR); 
int meses = cfin.get(Calendar.MONTH) - cini.get(Calendar.MONTH); 
int dia1=0, dia2=0, antiguedad=0;

if(cini.get(Calendar.DATE)==31)
{ 
dia1=30; 
} 
else if(cini.get(Calendar.MONTH)==Calendar.FEBRUARY && cini.get(Calendar.DATE)>=28)
{ 
dia1=30; 
		 		  														}
else
{ 
dia1=cini.get(Calendar.DATE); 
} 

if(cfin.get(Calendar.DATE)==31)
{ 
dia2=30; 
} 
else if(cfin.get(Calendar.MONTH)==Calendar.FEBRUARY && cfin.get(Calendar.DATE)>=28)
{ 
dia2=30; 
} 
else
{ 
dia2=cfin.get(Calendar.DATE); 
} 

int dias = dia2-dia1; 

if(meses < 0)
{
meses +=12;
anios --;
}

if(dias < 0)
{
dias += 30;
meses --;
}
antiguedad=anios*360+meses*30+dias;

return antiguedad; 
}		 		  									
		 		  									
	
}
