package paq_general.ejb;

import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_general.ejb.ServicioGeneral;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioGeneral  {
	
	private Utilitario utilitario= new Utilitario();
	@EJB
	private ServicioPresupuesto serv_presupuesto;
	/**
	 * Metodo que devuelve los Tipo Persona por Modulo.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return string SQL Tipo Persona
	 */
	public String getTipoPersona(String estado,String  modulo){
		
		String tab_tipo_persona= "select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ( "+modulo+") and activo_getpm in ("+estado+") order by detalle_getip";
		
		 return tab_tipo_persona;
		
	}
	/**
	 * Metodo que devuelve los Tipo Persona por Modulo.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return Tabla Tipo Persona.
	 */
	public TablaGenerica getTablaTipoPersona(String estado,String modulo){
		
		TablaGenerica tab_tipo_persona=utilitario.consultar("select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ("+modulo+") and activo_getpm in ("+estado+") order by detalle_getip");
		 return tab_tipo_persona;
		 
	}

	/**
	 * Metodo que devuelve el distributivo de colaboradores.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return string SQL Tipo Persona
	 */
	public String getDistributivoColaboradores(String fecha){
		
/*		String tab_distributivo= "select row_number() over(order by proceso,rmu_geedp) as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap,"
			+" fecha_ingreso_gtemp,'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,"
			+" lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,"
			+" discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 1 as orden"
			+" from ("
			+" select a.ide_gtemp,documento_identidad_gtemp,"
			+" apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp"
			+" ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,"
			+" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
			+" detalle_gegro as grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,"
			+" (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp"
			+" from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,"
			+" gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k"
			+" where a.ide_gtemp = b.ide_gtemp"
			+" and activo_geedp = true"
			+" and b.ide_gepgc = d.ide_gepgc"
			+" and c.ide_gepap = d.ide_gepap"
			+" and d.ide_geare = e.ide_geare"
			+" and d.ide_gedep = f.ide_gedep"
			+" and d.ide_gegro = g.ide_gegro"
			+" and d.ide_gttem =h.ide_gttem"
			+" and b.ide_gttco = i.ide_gttco"
			+" and a.ide_gtgen = k.ide_gtgen"
			+" and d.ide_sucu = j.ide_sucu"
			+" and fecha_ingreso_gtemp < '"+fecha+"'"
			+" ) a"
			+" left join ("
			+" select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1"
			+" ) b on a.ide_gtemp = b.ide_gtemp"
			+" left join ("
			+" select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes"
			+" from gth_educacion_empleado a"
			+" left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted"
			+" left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp"
			+" left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes"
			+" ) c on a.ide_gtemp = c.ide_gtemp"
			+" left join ("
			+" select ide_gtemp,detalle_gttds"
			+" from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds"
			+" ) d on a.ide_gtemp = d.ide_gtemp"
			+" union "
			+" select null as num_registro,null as ide_gtemp,null as documento_identidad, 'VACANTE' as empleado,codigo_partida_gepap,"
			+" null as fecha_ingreso,null as fecha_salida,titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
			+" detalle_gegro as grupo_ocupacional,rmu_gegro,detalle_gttem,null as detalle_gttco,nom_sucu as lugar_trabajo,null as genero,"
			+" null as formacion, null as titulo,null as area_conocimiento,null as discapacitado,null as detalle_gttds, null as fecha_nacimiento,"
			+" null as observacion,null as fecha_entrega,null as fecha_proxima_entrega,2 as orden"
			+" from gen_partida_grupo_cargo a,gen_partida_presupuestaria b,gen_area c,"
			+" gen_departamento d,gen_grupo_ocupacional e,gth_tipo_empleado f,sis_sucursal h"
			+" where a.ide_gepap = b.ide_gepap and a.ide_geare = c.ide_geare"
			+" and a.ide_gedep = d.ide_gedep and a.ide_gegro = e.ide_gegro"
			+" and a.ide_gttem = f.ide_gttem and a.ide_sucu = h.ide_sucu "
			+" and activo_gepgc = true and vacante_gepgc = true"
			+" union"
			+" select null as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap,"
			+" fecha_ingreso_gtemp,fecha_finctr_geedp||'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,"
			+" lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,"
			+" discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 3 as orden"
			+" from ("
			+" select a.ide_gtemp,documento_identidad_gtemp,"
			+" apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp"
			+" ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,"
			+" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
			+" detalle_gegro as grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,"
			+" (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp,fecha_finctr_geedp"
			+" from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,"
			+" gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k"
			+" where a.ide_gtemp = b.ide_gtemp"
			+" and activo_geedp = true"
			+" and b.ide_gepgc = d.ide_gepgc"
			+" and c.ide_gepap = d.ide_gepap"
			+" and d.ide_geare = e.ide_geare"
			+" and d.ide_gedep = f.ide_gedep"
			+" and d.ide_gegro = g.ide_gegro"
			+" and d.ide_gttem =h.ide_gttem"
			+" and b.ide_gttco = i.ide_gttco"
			+" and a.ide_gtgen = k.ide_gtgen"
			+" and d.ide_sucu = j.ide_sucu"
			+" and ide_geded in ("
			+" select ide_geded from gen_detalle_empleado_departame where ide_geame in ("
			+" select ide_geame from gen_accion_motivo_empleado where ide_geaed in ( select ide_geaed from gen_accion_empleado_depa where finiquito_contrato_geaed = true)"
			+" )"
			+" )"
			+" and fecha_finctr_geedp between cast(extract(year from cast('"+fecha+"' as date))||'-'||extract(month from cast('"+fecha+"' as date))||'-01' as date) and '"+fecha+"'"
			+" ) a"
			+" left join ("
			+" select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1"
			+" ) b on a.ide_gtemp = b.ide_gtemp"
			+" left join ("
			+" select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes"
			+" from gth_educacion_empleado a"
			+" left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted"
			+" left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp"
			+" left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes"
			+" ) c on a.ide_gtemp = c.ide_gtemp"
			+" left join ("
			+" select ide_gtemp,detalle_gttds"
			+" from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds"
			+" ) d on a.ide_gtemp = d.ide_gtemp"
			+" order by  proceso,rmu_geedp,num_registro";
					
		 return tab_distributivo;*/
		
		 
		 
		 
		 /*String tab_distributivo= "select row_number() over(order by proceso,rmu_geedp) as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap, "
		 		+ "fecha_ingreso_gtemp,'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,denominacion,rmu_geedp,detalle_gttem,detalle_gttco,  "
		 		+ "lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,  "
		 		+ "discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 1 as orden "
		 		+ "from ( "
		 		+ "select a.ide_gtemp,documento_identidad_gtemp, "
		 		+ "apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp  "
		 		+ "||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,  "
		 		+ "fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,  "
		 		+ "detalle_gegro as grupo_ocupacional,detalle_gecaf as denominacion,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,  "
		 		+ "(case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp  "
		 		+ "from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,  "
		 		+ "gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k, gen_cargo_funcional l "
		 		+ "where a.ide_gtemp = b.ide_gtemp "
		 		+ "and activo_geedp = true "
		 		+ "and b.ide_gepgc = d.ide_gepgc  "
		 		+ "and c.ide_gepap = d.ide_gepap  "
		 		+ "and d.ide_geare = e.ide_geare  "
		 		+ "and d.ide_gedep = f.ide_gedep "
		 		+ "and d.ide_gegro = g.ide_gegro  "
		 		+ "and d.ide_gttem =h.ide_gttem  "
		 		+ "and b.ide_gttco = i.ide_gttco  "
		 		+ "and a.ide_gtgen = k.ide_gtgen  "
		 		+ "and d.ide_sucu = j.ide_sucu  "
		 		+ "and d.ide_gecaf= l.ide_gecaf "
		 		+ "and fecha_ingreso_gtemp < '"+fecha+"'" 
				+ " ) a "
				+ "left join ( "
				+ "select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1  "
				+ ") b on a.ide_gtemp = b.ide_gtemp  "
				+ "left join ( "
				+ "select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes  "
				+ "from gth_educacion_empleado a  "
				+ "left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted  "
				+ "left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp  "
				+ "left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes  "
				+ ") c on a.ide_gtemp = c.ide_gtemp  "
				+ "left join ( "
				+ "select ide_gtemp,detalle_gttds "
				+ "from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds "
				+ ") d on a.ide_gtemp = d.ide_gtemp  "
				+ "union  "
				+ "select null as num_registro,null as ide_gtemp,null as documento_identidad, 'VACANTE' as empleado,codigo_partida_gepap, "
				+ "null as fecha_ingreso,null as fecha_salida,titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso, "
				+ "detalle_gegro as grupo_ocupacional,detalle_gecaf as denominacion,rmu_gegro,detalle_gttem,null as detalle_gttco,nom_sucu as lugar_trabajo,null as genero, "
				+ "null as formacion, null as titulo,null as area_conocimiento,null as discapacitado,null as detalle_gttds, null as fecha_nacimiento,  "
				+ "null as observacion,null as fecha_entrega,null as fecha_proxima_entrega,2 as orden  "
				+ "from gen_partida_grupo_cargo a,gen_partida_presupuestaria b,gen_area c, "
				+ "gen_departamento d,gen_grupo_ocupacional e,gth_tipo_empleado f,sis_sucursal h,gen_cargo_funcional l  "
				+ "where a.ide_gepap = b.ide_gepap and a.ide_geare = c.ide_geare  "
				+ "and a.ide_gedep = d.ide_gedep and a.ide_gegro = e.ide_gegro  "
				+ "and a.ide_gttem = f.ide_gttem and a.ide_sucu = h.ide_sucu  "
				+ "and a.ide_gecaf = l.ide_gecaf  "
				+ "and activo_gepgc = true and vacante_gepgc = true "
				+ "union "
				+ "select null as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap,  "
				+ "fecha_ingreso_gtemp,fecha_finctr_geedp||'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,denominacion,rmu_geedp,detalle_gttem,detalle_gttco, "
				+ "lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento, "
				+ "discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 3 as orden "
				+ "from ( "
				+ "select a.ide_gtemp,documento_identidad_gtemp,  "
				+ "apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp  "
				+ "||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,  "
				+ "fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,  "
				+ "detalle_gegro as grupo_ocupacional,detalle_gecaf as denominacion,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,  "
				+ "(case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp,fecha_finctr_geedp  "
				+ "from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,  "
				+ "gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k, gen_cargo_funcional l  "
				+ "where a.ide_gtemp = b.ide_gtemp "
				+ "and activo_geedp = true "
				+ "and b.ide_gepgc = d.ide_gepgc "
				+ "and c.ide_gepap = d.ide_gepap "
				+ "and d.ide_geare = e.ide_geare "
				+ "and d.ide_gedep = f.ide_gedep "
				+ "and d.ide_gegro = g.ide_gegro "
				+ "and d.ide_gttem =h.ide_gttem "
				+ "and b.ide_gttco = i.ide_gttco "
				+ "and a.ide_gtgen = k.ide_gtgen "
				+ "and d.ide_sucu = j.ide_sucu "
				+ "and d.ide_gecaf= l.ide_gecaf "
				+ "and ide_geded in ( "
				+ "select ide_geded from gen_detalle_empleado_departame where ide_geame in ( "
				+ "select ide_geame from gen_accion_motivo_empleado where ide_geaed in ( select ide_geaed from gen_accion_empleado_depa where finiquito_contrato_geaed = true) "
				+ ") "
				+ ") "
				+ "and fecha_finctr_geedp between cast(extract(year from cast('"+fecha+"' as date))||'-'||extract(month from cast('"+fecha+"' as date))||'-01' as date) and '"+fecha+"' "
				+ ") a  "
				+ "left join ( "
				+ "select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1 "
				+ ") b on a.ide_gtemp = b.ide_gtemp "
				+ "left join ( "
				+ "select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes "
				+ "from gth_educacion_empleado a "
				+ "left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted "
				+ "left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp "
				+ "left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes "
				+ ") c on a.ide_gtemp = c.ide_gtemp  "
				+ "left join ( "
				+ "select ide_gtemp,detalle_gttds "
				+ "from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds "
				+ ") d on a.ide_gtemp = d.ide_gtemp "
				+ "order by  proceso,rmu_geedp,num_registro";*/
		 
		String tab_distributivo= " select row_number() over(order by proceso,rmu_geedp) as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap, coalesce(fecha_geedp,fecha_ingreso_gtemp) as fecha_ingreso, fecha_finctr_geedp||'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,denominacion,rmu_geedp,detalle_gttem,detalle_gttco,  lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,  discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 1 as orden  " +
				" from (  " +
				" " +
				" select a.ide_gtemp,coalesce(fecha_liquidacion_geedp,fecha_finctr_geedp) as fecha_finctr_geedp, fecha_geedp,documento_identidad_gtemp, apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp  ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,   " +
				" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,  detalle_gegro as grupo_ocupacional,detalle_gecaf as denominacion,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,  (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp   " +
				" " +
				" from gth_empleado a " +
				" " +
				" left join (select max(ide_geded) as ide_geded,ide_gtemp from gen_detalle_empleado_departame  " +
				"                                      where fecha_ingreso_geded <= '"+fecha+"'  " +
				"                                      group by ide_gtemp  ) b1 on a.ide_gtemp = b1.ide_gtemp " +
				" " +
				"left join gen_empleados_departamento_par b on a.ide_gtemp = b.ide_gtemp and b1.ide_geded=b.ide_geded " +
				" " +
				"left join gen_partida_grupo_cargo d on b.ide_gepgc = d.ide_gepgc   " +
				"left join gen_partida_presupuestaria c on c.ide_gepap = d.ide_gepap " +
				" " +
				"left join gen_area e on d.ide_geare = e.ide_geare " +
				"left join gen_departamento f on d.ide_gedep = f.ide_gedep " +
				"left join gen_grupo_ocupacional g on d.ide_gegro = g.ide_gegro " +
				"left join gth_tipo_empleado h on d.ide_gttem =h.ide_gttem " +
				"left join gth_tipo_contrato i on b.ide_gttco = i.ide_gttco " +
				"left join sis_sucursal j on d.ide_sucu = j.ide_sucu " +
				"left join gth_genero k on a.ide_gtgen = k.ide_gtgen  " +
				"left join gen_cargo_funcional l on d.ide_gecaf= l.ide_gecaf " +
				"where " +
				//" (case when coalesce(fecha_liquidacion_geedp,fecha_finctr_geedp) is null  then (case when fecha_geedp <= '"+fecha+"' and activo_gtemp=true then 1 else 0 end) else (case when coalesce(fecha_liquidacion_geedp,fecha_finctr_geedp) >= '"+fecha+"' then 1 else 0 end) end )=1   " +
				" (case when coalesce(fecha_liquidacion_geedp,fecha_finctr_geedp) is null " +
				" then (case when fecha_geedp <= '"+fecha+"' then 1 else 0 end) " +
				" else (case when coalesce(fecha_liquidacion_geedp,fecha_finctr_geedp) >= cast(extract(year from cast('"+fecha+"' as date))||'-'||extract(month from cast('"+fecha+"' as date))||'-01' as date) " + 
				" then 1 else 0 end) end )=1" +
				
				") a  " +
				"left join (  " +
				"select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1   " +
				") b on a.ide_gtemp = b.ide_gtemp   " +
				"left join (  " +
				"select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes  from gth_educacion_empleado a  left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted  left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp  left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes   " +
				") c on a.ide_gtemp = c.ide_gtemp   " +
				"left join (  " +
				"select ide_gtemp,detalle_gttds from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds  " +
				") d on a.ide_gtemp = d.ide_gtemp   " +
				"union   " +
				"select null as num_registro,null as ide_gtemp,null as documento_identidad, 'VACANTE' as empleado,codigo_partida_gepap, null as fecha_ingreso,null as fecha_salida,titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso, detalle_gegro as grupo_ocupacional,detalle_gecaf as denominacion,rmu_gegro,detalle_gttem,null as detalle_gttco,nom_sucu as lugar_trabajo,null as genero, null as formacion, null as titulo,null as area_conocimiento,null as discapacitado,null as detalle_gttds, null as fecha_nacimiento,  null as observacion,null as fecha_entrega,null as fecha_proxima_entrega,2 as orden  from gen_partida_grupo_cargo a,gen_partida_presupuestaria b,gen_area c, gen_departamento d,gen_grupo_ocupacional e,gth_tipo_empleado f,sis_sucursal h,gen_cargo_funcional l  where a.ide_gepap = b.ide_gepap and a.ide_geare = c.ide_geare  and a.ide_gedep = d.ide_gedep and a.ide_gegro = e.ide_gegro  and a.ide_gttem = f.ide_gttem and a.ide_sucu = h.ide_sucu  and a.ide_gecaf = l.ide_gecaf  and activo_gepgc = true and vacante_gepgc = true  " +
				" " +
				"order by  proceso,rmu_geedp,num_registro ";
		
		 System.out.println("tab_distributivo "+tab_distributivo);
		 
		 return tab_distributivo;
		
		 
	}
	
	
	public String getEjecucionActividadesPOA(String fecha_inicial,String fecha_final){
        String sql="select a.ide_prpoa,a.ide_prfup,b.detalle_geani,detalle_programa,cod_pry,detalle_proyecto,detalle_producto_mc,detalle_actividad_mc,cod_prod,detalle_producto,  " +
		" detalle_actividad, codigo_subactividad,detalle_subactividad,codigo_clasificador_prcla,descripcion_clasificador_prcla,presupuesto_inicial_prpoa,reforma_prpoa," +  
		" presupuesto_codificado_prpoa,valor_certificado_prpoa as certificado,valor_compromiso_prpoa as comprometido, valor_devengado_prpoa as devengado   " +
		//" presupuesto_codificado_prpoa,valor_certificado_prpoa as certificado,valor_compromiso_prpoa as comprometido, dv.devengado as devengado   " +
		"from pre_poa a " +
		"left join  gen_anio b on a.ide_geani= b.ide_geani " +
		"left join pre_clasificador c on a.ide_prcla = c.ide_prcla " +
		"left join ( " +serv_presupuesto.getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup" +

		" left join gen_area g on a.ide_geare=g.ide_geare " +
		//" left join (select sum(valor_certificado_prpoc) as certificado,ide_prpoa from pre_poa_certificacion group by ide_prpoa) h on a.ide_prpoa = h.ide_prpoa  " +
		//" left join (select sum(comprometido_prpot) as comprometido,ide_prpoa from pre_poa_tramite group by ide_prpoa ) i on a.ide_prpoa = i.ide_prpoa "+
		//" left join (select sum(devengado_prmen) as devengado, ide_prpoa from pre_mensual pm join pre_anual pa on pa.ide_pranu=pm.ide_pranu group by ide_prpoa ) dv on a.ide_prpoa = dv.ide_prpoa "+
		//" left join (select sum(comprometido_prpot) as comprometido,ide_prpoa from pre_poa_tramite group by ide_prpoa ) i on a.ide_prpoa = i.ide_prpoa "+
		" where fecha_inicio_prpoa>='"+fecha_inicial+"' and fecha_fin_prpoa<='"+fecha_final+"' ";
			
		System.out.println("consulta ejecucion subactividades: "+ sql);
		return sql;
	}
	
	public String getRptMovimientoPresupuesto(String fecha_inicial,String fecha_final){
		
		String sql="select *,(debe_213-pagado) as diferencia_pagado,(debe_113_81-haber_213_81) as diferencia_iva from (";

		  sql+="select mov.ide_comov as movimiento, mov.mov_fecha_comov, tipo.detalle_cotim as tipo_movimiento,nro_comprobante_comov as nro_comprobante, mov.ide_tecpo as cod_comp_pago"
				+ " ,case when coalesce(ide_conac,0)>0 then true else false end as aplica_asientoTipo, substring(detalle_comov from 0 for 200) as detalle"
				+ " , coalesce(pgc.debe_codem,0) as debe_213, coalesce(pgc1.haber_codem,0) as haber_111, coalesce(pgc2.devengado,0) as devengado, coalesce(pgc2.pagado,0) as pagado"
				+ " , coalesce(iva.debe_codem,0) as debe_113_81, coalesce(iva1.haber_codem,0) as haber_213_81"
				+ " from cont_movimiento mov"
				+ " left join cont_tipo_movimiento tipo on tipo.ide_cotim=mov.ide_cotim"
				
				+ " left join (select mov.ide_comov, sum(dmov.debe_codem) as debe_codem, sum(dmov.haber_codem) as haber_codem "
				+ "         from cont_movimiento mov"
				+ " 	left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov "
				+ "   	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				+ "   	where (cat.cue_codigo_cocac like '213.53%' or cat.cue_codigo_cocac like '213.56%'"
				+ "   	or cat.cue_codigo_cocac like '213.51%' or cat.cue_codigo_cocac like '213.57%'"
				+ "   	or cat.cue_codigo_cocac like '213.58%' or cat.cue_codigo_cocac like '213.78%'"
				+ "   	or cat.cue_codigo_cocac like '213.71%' or cat.cue_codigo_cocac like '213.73%'"
				+ "   	or cat.cue_codigo_cocac like '213.75%' or cat.cue_codigo_cocac like '213.96%'"
				+ "   	or cat.cue_codigo_cocac like '213.77%' or cat.cue_codigo_cocac like '213.84%')"
				+ "   	group by mov.ide_comov) pgc on pgc.ide_comov=mov.ide_comov"
				
				+ " left join (select mov.ide_comov, sum(dmov.debe_codem) as debe_codem, sum(dmov.haber_codem) as haber_codem "
				+ "         from cont_movimiento mov"
				+ " 	left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov "
				+ "   	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				+ "   	where cat.cue_codigo_cocac like '111.03.01%'"
				+ "   	group by mov.ide_comov) pgc1 on pgc1.ide_comov=mov.ide_comov"
				
				+ " left join (select ide_comov, sum(devengado_prmen) as devengado, sum(pagado_prmen) as pagado"
				+ "          from pre_mensual "
				+ " 	 group by ide_comov ) pgc2 on pgc2.ide_comov=mov.ide_comov"
				
				+ " left join (select mov.ide_comov, sum(dmov.debe_codem) as debe_codem, sum(dmov.haber_codem) as haber_codem "
				+ "        from cont_movimiento mov"
				+ " 	left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov "
				+ "  	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				+ "  	where cat.cue_codigo_cocac like '113.81%'"
				+ "  	group by mov.ide_comov) iva on iva.ide_comov=mov.ide_comov"
				
				+ " left join (select mov.ide_comov, sum(dmov.debe_codem) as debe_codem, sum(dmov.haber_codem) as haber_codem "
				+ "        from cont_movimiento mov"
				+ " 	left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov "
				+ "  	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				+ "   	where cat.cue_codigo_cocac like '213.81%'"
				+ "  	group by mov.ide_comov) iva1 on iva1.ide_comov=mov.ide_comov"
				
				+ " where (abs(coalesce(pgc.debe_codem,0))+abs(coalesce(pgc1.haber_codem,0))+abs(coalesce(pgc2.devengado,0))+abs(coalesce(pgc2.pagado,0))+abs(coalesce(iva.debe_codem,0))+abs(coalesce(iva1.haber_codem,0)))>0"
				+ "  and mov.mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"' " 
				+ "  order by mov.mov_fecha_comov ";
		  sql+=")a order by diferencia_pagado, diferencia_iva ";	
		System.out.println("consulta getRptMovimientoPresupuesto: "+ sql);
		return sql;
	}
	
	public String getEjecucionPresupuestaria(String fecha_inicial,String fecha_final){
		// Fecha final para sacar los datos del periodo
		
		String sql="select ide_prpoa, tipo_cedula, periodo, detalle_geani, tipo_gestion, cod_prog, detalle_programa, cod_pry, detalle_proyecto, cod_prod_mc, detalle_producto_mc, " + 
				" codigo_obra, obra, cod_act, detalle_actividad_mc, cod_prod, detalle_producto, detalle_actividad, codigo_subactividad, detalle_subactividad, area, codigo_fuente_prfuf, " +
				" fuente, tipo_gasto, grupo_gasto, codigo_clasificador_prcla, descripcion_clasificador_prcla, presupuesto_inicial_prpoa, reforma_prpoa, presupuesto_codificado_prpoa,  " +
				" certificado, liquidado, (presupuesto_codificado_prpoa - certificado) as saldo_certificar, " +
				" comprometido, liberado, (certificado - comprometido) as saldo_comprometido, " +
				" devengado, (comprometido - devengado) as saldo_por_devengar, pagado, (devengado - pagado) as saldo_por_pagar, " +
				" credito_iva, activo " +
				" from( ";
		
		    sql+="select  a.ide_prpoa, 1 as tipo_cedula, cast(extract(month from DATE '"+fecha_final+"') as int) as periodo, cast(extract(year from DATE  '"+fecha_final+"') as int) as detalle_geani,  " +
				"  detalle_prtge as tipo_gestion, cod_prog, detalle_programa,cod_pry,detalle_proyecto, cod_prod_mc, detalle_producto_mc,codigo_obra,obra, cod_act,detalle_actividad_mc,"+
				" cod_prod,detalle_producto,detalle_actividad,codigo_subactividad,detalle_subactividad,detalle_geare as area,codigo_fuente_prfuf, detalle_prfuf as fuente , " +
				"  tipo_gasto,grupo_gasto,codigo_clasificador_prcla, descripcion_clasificador_prcla,    " +
				"  coalesce(codificado,0.00) as presupuesto_inicial_prpoa,  " +
				"  coalesce(reforma, 0.00) as reforma_prpoa,  " +
				"  (coalesce(codificado,0.00) + coalesce(reforma,0.00)) as presupuesto_codificado_prpoa, " +
				"  coalesce(certificado,0.00) as certificado, "+				
				"  coalesce(liquidado,0.00) as liquidado, " +
				"  (coalesce(codificado,0.00) + coalesce(reforma,0.00))-coalesce(certificado,0.00) as saldo_certificar, " +
				"  coalesce(comprometido,0.00) as comprometido, "+
				"  coalesce(liberado,0.00) as liberado, " +
				"  (coalesce(certificado,0.00)-coalesce(comprometido,0.00)) as saldo_comprometido,  " +
				"  coalesce(devengado,0.00) as devengado,  " +
				"  (coalesce(comprometido,0.00)-coalesce(devengado,0.00)) as saldo_por_devengar, " +
				"  coalesce(pagado,0.00) as pagado, " +
				"  (coalesce(devengado,0.00)-coalesce(pagado,0.00)) as saldo_por_pagar, " +
				"  cast(coalesce(ivc.valor_iva,0.00) as numeric(8,2)) as credito_iva, a.activo_prpoa as activo " +
				"  from pre_poa a   " +
				//"  left join pre_clasificador c on a.ide_prcla = c.ide_prcla   " +
				"  left join pre_tipo_gestion tg on tg.ide_prtge = a.ide_prtge " +
				"  left join (SELECT pc.ide_prcla, pc.codigo_clasificador_prcla, pc.descripcion_clasificador_prcla, " +
				"  			pc2.codigo_clasificador_prcla || ' ' || pc2.descripcion_clasificador_prcla as grupo_gasto, " +
				"  			pc3.codigo_clasificador_prcla || ' ' || pc3.descripcion_clasificador_prcla as tipo_gasto " +
				"  			FROM public.pre_clasificador pc " +
				"  			left join pre_clasificador pc1 on pc1.ide_prcla=pc.pre_ide_prcla " +
				"  			left join pre_clasificador pc2 on pc2.ide_prcla=pc1.pre_ide_prcla " +
				"  			left join pre_clasificador pc3 on pc3.ide_prcla=pc2.pre_ide_prcla) pc on pc.ide_prcla = a.ide_prcla   " +
				
				"  left join gen_area area on a.ide_geare=area.ide_geare   " + 
				"  left join ( select a.ide_prfup,codigo_subactividad,detalle_subactividad,codigo_actividad || ' '|| detalle_actividad as detalle_actividad, codigo_producto || ' '||detalle_producto as detalle_producto, cod_prod, producto,  " +
				"  cod_act,codigo_actividad_mc || ' '||detalle_actividad_mc as detalle_actividad_mc, actividad_mc , cod_prod_mc, codigo_producto_mc || ' '||detalle_producto_mc as detalle_producto_mc, producto_mc,codigo_obra,obra,"+
				" codigo_proyecto || ' '|| detalle_proyecto as detalle_proyecto, cod_pry, proyecto, codigo_programa || ' '||detalle_programa as detalle_programa, programa, cod_prog " +
				
				"  from (" +serv_presupuesto.getUbicacionPOA()+") a ) f  on a.ide_prfup = f.ide_prfup  " +
				
				"  left join (select sum(valor_financiamiento_prpof) as codificado, ide_prpoa, pf.ide_prfuf,codigo_fuente_prfuf,detalle_prfuf from pre_poa_financiamiento  pf     " +
				"  join pre_fuente_financiamiento ff on ff.ide_prfuf=pf.ide_prfuf    " +
				"  group by ide_prpoa, pf.ide_prfuf,codigo_fuente_prfuf,detalle_prfuf) pf on a.ide_prpoa = pf.ide_prpoa    " +
				"  left join (select sum(valor_reformado_prprf) as reforma, ide_prpoa, ide_prfuf from pre_poa_reforma_fuente  rf   " +
				"  where fecha_prprf between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,ide_prfuf) rf on a.ide_prpoa = rf.ide_prpoa and pf.ide_prfuf=rf.ide_prfuf    " +
				//"  left join (select sum(dc.valor_certificado_prpoc) as certificado,dc.ide_prpoa,dc.ide_prfuf from pre_poa_certificacion dc,pre_certificacion c where c.ide_prcer=dc.ide_prcer and fecha_prcer between '"+fecha_inicial+"' and  '"+fecha_final+"' group by dc.ide_prpoa,dc.ide_prfuf) h on a.ide_prpoa = h.ide_prpoa and pf.ide_prfuf=h.ide_prfuf   " +
				
				" left join ( select sum(coalesce(dlc.valor_certificado_prdcl,dc.valor_certificado_prpoc)) as certificado,dc.ide_prpoa,dc.ide_prfuf from pre_certificacion c  " +
				" 	join pre_poa_certificacion dc on dc.ide_prcer=c.ide_prcer " +
				" 	left join (select lc.ide_prcer,lc.fecha_prlce,dlc.ide_prpoa,dlc.ide_prfuf,valor_certificado_prdcl,valor_liquidado_prdcl  " +
				" 			from pre_liquida_certificacion lc " +
				" 			join pre_detalle_liquida_certif dlc on dlc.ide_prlce=lc.ide_prlce " +
				" 			where fecha_prlce > '"+fecha_final+"') dlc on dlc.ide_prcer=c.ide_prcer and dlc.ide_prpoa=dc.ide_prpoa and dlc.ide_prfuf=dc.ide_prfuf	 " +
				" 	where fecha_prcer between '"+fecha_inicial+"' and '"+fecha_final+"' " +
				" 	group by dc.ide_prpoa,dc.ide_prfuf ) h on a.ide_prpoa = h.ide_prpoa and pf.ide_prfuf=h.ide_prfuf " +
				
				"  left join (select sum(dc.valor_liquidado_prdcl) as liquidado, dc.ide_prpoa, dc.ide_prfuf from pre_detalle_liquida_certif dc, pre_liquida_certificacion c where c.ide_prlce=dc.ide_prlce and fecha_prlce between '"+fecha_inicial+"' and  '"+fecha_final+"' group by dc.ide_prpoa,dc.ide_prfuf) lq on a.ide_prpoa = lq.ide_prpoa and pf.ide_prfuf=lq.ide_prfuf  " +
				//"  left join (select sum(dc.comprometido_prpot) as comprometido,dc.ide_prpoa,dc.ide_prfuf from pre_poa_tramite dc,pre_tramite c where c.ide_prtra=dc.ide_prtra and fecha_tramite_prtra between '"+fecha_inicial+"' and  '"+fecha_final+"' group by dc.ide_prpoa,dc.ide_prfuf ) i on a.ide_prpoa = i.ide_prpoa and pf.ide_prfuf=i.ide_prfuf " +
				" left join ( select sum(coalesce(dlc.valor_comprometido_prdlc,dc.comprometido_prpot)) as comprometido,dc.ide_prpoa,dc.ide_prfuf " +
				" 	from pre_tramite c  " +
				" 	left join pre_poa_tramite dc on dc.ide_prtra=c.ide_prtra " +
				" 	left join (select lc.ide_prtra,lc.fecha_prlic,dlc.ide_prpoa,dlc.ide_prfuf,valor_comprometido_prdlc,valor_liberado_prdlc   " +
				" 			from pre_libera_compromiso lc  " +
				" 			join pre_detalle_libera_compro dlc on dlc.ide_prlic=lc.ide_prlic  " +
				" 			where fecha_prlic > '"+fecha_final+"') dlc on dlc.ide_prtra=c.ide_prtra and dlc.ide_prpoa=dc.ide_prpoa and dlc.ide_prfuf=dc.ide_prfuf	 " +	
				" 	where fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"' " +
				" 	group by dc.ide_prpoa,dc.ide_prfuf ) i on a.ide_prpoa = i.ide_prpoa and pf.ide_prfuf=i.ide_prfuf " +
				
				
				"  left join (select sum(dc.valor_liberado_prdlc) as liberado,dc.ide_prpoa,dc.ide_prfuf from pre_detalle_libera_compro dc, pre_libera_compromiso c where c.ide_prlic=dc.ide_prlic and fecha_prlic between '"+fecha_inicial+"' and  '"+fecha_final+"' group by dc.ide_prpoa,dc.ide_prfuf ) lb on a.ide_prpoa = lb.ide_prpoa and pf.ide_prfuf=lb.ide_prfuf  " +
				"  left join (select distinct sum(devengado_prmen) as devengado, ide_prpoa, pm.ide_prfuf from pre_mensual pm join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"  where ide_prpoa>0 and abs(devengado_prmen)>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,pm.ide_prfuf) dv on a.ide_prpoa = dv.ide_prpoa and pf.ide_prfuf=dv.ide_prfuf  " ;
				
			//VALIDANDO EL VALOR DEL PAGADO EN VES DEL DEVENGADO desde el anio 2023 Ticket #826400	    
		    //Volver a como antes Ticket #871602
		    //if(fecha_inicial.length()>0)
		    //{
		    //	String[] extraAnio=fecha_inicial.split("-");
		    //	if(pckUtilidades.CConversion.CInt(extraAnio[0])>2022)
		    //	{
		    //		sql+="  left join (select distinct sum(pagado_prmen) as pagado, ide_prpoa, pm.ide_prfuf from pre_mensual pm join pre_anual pa on pa.ide_pranu=pm.ide_pranu  join spi_transferencias_detalle spi on spi.ide_tecpo=pm.ide_tecpo and coalesce(spi.notificado_sptrd,false)=true  " +
			//			 "  where ide_prpoa>0 and abs(pagado_prmen)>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,pm.ide_prfuf) pg on a.ide_prpoa = pg.ide_prpoa and pf.ide_prfuf=pg.ide_prfuf  " ;
						/////////////
		    //	}
		    //	else
		    //	{
			    	sql+="  left join (select distinct sum(pagado_prmen) as pagado, ide_prpoa, pm.ide_prfuf from pre_mensual pm join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
						 "  where ide_prpoa>0 and abs(pagado_prmen)>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,pm.ide_prfuf) pg on a.ide_prpoa = pg.ide_prpoa and pf.ide_prfuf=pg.ide_prfuf  " ;
				/////////////
		    //	}
		    //}

				//VALIDANDO EL VALOR DEL IVA POR CREDITO TRIBUTARIO 
			sql+=" left join (select distinct cast(sum(valor_iva) as numeric(8,2)) as valor_iva, ide_prpoa,pm.ide_prfuf   " +
				" from pre_mensual pm   " +
				" join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				" join (select ide_tecpo,sum(coalesce(valor_iva_adfac,0)) as valor_iva from adq_factura afac where afac.ide_srsuc=1 group by ide_tecpo) afac on afac.ide_tecpo=pm.ide_tecpo " +
				" where ide_prpoa>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,pm.ide_prfuf) ivc on a.ide_prpoa = ivc.ide_prpoa and pf.ide_prfuf=ivc.ide_prfuf  " +

				"  where a.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_final+"') as varchar))" ;
		    
		    sql+=" ) ejec ";
		    sql+="  order by ide_prpoa,periodo, detalle_geani,detalle_programa,cod_pry,detalle_proyecto,cod_prod,detalle_producto,codigo_subactividad,detalle_subactividad,codigo_fuente_prfuf, fuente, " +
				"  codigo_clasificador_prcla, descripcion_clasificador_prcla ";
		
		    
		System.out.println("consulta ejecucion presupuestaria: "+ sql);
		return sql;
	}
	
	public String getEjecucionPresupuestaria_detallado(String fecha_inicial,String fecha_final,boolean grupos_fuentes, boolean grupos_compromisos, boolean grupo){
		// Fecha final para sacar los datos del periodo
		
		String sql="select ide_prpoa , fecha_inicial, fecha_corte,ide_prcla,programa,proyecto,producto,codigo_subactividad, subactividad,codigo_clasificador_prcla, descripcion_clasificador_prcla,fuente,area " +
				",inicial,reforma,codificado, nro_certificacion_prcer,certificado,saldo_certificado,nro_compromiso, compromiso, saldo_compromiso ,nro_comprobante, devengado, saldo_devengado,valor_iva from ( ";
		
		if(!grupos_fuentes)
		{
		   sql+="select poa.ide_prpoa ,poa.ide_geani,'"+fecha_inicial+"' as fecha_inicial, '"+fecha_final+"' as fecha_corte, pc.ide_prcla,programa,proyecto,producto,codigo_subactividad, subactividad, pc.codigo_clasificador_prcla, pc.descripcion_clasificador_prcla,pf.detalle_prfuf as fuente " +
				",coalesce(inicial,0) as inicial,coalesce(reforma,0) as reforma,(coalesce(inicial,0)+coalesce(reforma,0)) as codificado " +
				",nro_certificacion_prcer,coalesce(certificado,0) as certificado,(coalesce(inicial,0)+coalesce(reforma,0))-coalesce(certificado,0) saldo_certificado " +
				",nro_compromiso, coalesce(compromiso,0) as compromiso,coalesce(certificado,0)-coalesce(compromiso,0) as saldo_compromiso " +
				",nro_comprobante, coalesce(devengado,0) as devengado,coalesce(compromiso,0)-coalesce(devengado,0) as saldo_devengado,valor_iva,detalle_geare as area " +
				
				"from pre_clasificador pc  " +
				"join pre_poa poa on poa.ide_prcla = pc.ide_prcla  " +
				"left join gen_area area on area.ide_geare=poa.ide_geare   " +
				"join pre_poa_financiamiento ppf on ppf.ide_prpoa=poa.ide_prpoa  " +
				"join pre_fuente_financiamiento pf on pf.ide_prfuf=ppf.ide_prfuf " +
				"left join (" +serv_presupuesto.getUbicacionPOA()+") desPoa on desPoa.ide_prfup=poa.ide_prfup " +
				
				"left join (select ide_prcla,ide_prfuf,ide_prfup,sum(valor_financiamiento_prpof) as inicial from ( " +
				"select a.ide_prpoa,ide_prfuf,valor_financiamiento_prpof,ide_prcla,ide_prfup from  pre_poa_financiamiento a, pre_poa b  " +
				"where a.ide_prpoa = b.ide_prpoa " +
				") a group by ide_prcla,ide_prfuf,ide_prfup) ini on ini.ide_prcla=pc.ide_prcla and ini.ide_prfuf = ppf.ide_prfuf and ini.ide_prfup=poa.ide_prfup " +
			
				"left join (select ide_prcla,ide_prfuf,ide_prfup,sum(valor_reformado_prprf) as reforma from ( " +
				"select a.ide_prpoa,ide_prfuf,valor_reformado_prprf,ide_prcla,ide_prfup from  pre_poa_reforma_fuente a, pre_poa b  " +
				"where a.ide_prpoa = b.ide_prpoa and fecha_prprf between '"+fecha_inicial+"' and '"+fecha_final+"' " +
				") a group by ide_prcla,ide_prfuf,ide_prfup) reff on reff.ide_prcla=pc.ide_prcla and reff.ide_prfuf = ppf.ide_prfuf and reff.ide_prfup=poa.ide_prfup " +
			
				"left join (select ide_prcla,ide_prfuf,ide_prfup,nro_certificacion_prcer,ide_prpoc,sum(valor_certificado_prpoc) as certificado from (   " +
				"select a.ide_prpoa,ide_prfuf,ide_prfup,valor_certificado_prpoc,ide_prcla, nro_certificacion_prcer,a.ide_prpoc from  pre_poa_certificacion a, pre_poa b, pre_certificacion c   " +
				"where a.ide_prpoa = b.ide_prpoa and a.ide_prcer=c.ide_prcer and fecha_prcer between '"+fecha_inicial+"' and '"+fecha_final+"' " +
				") a group by ide_prcla,ide_prfuf,ide_prfup,nro_certificacion_prcer,ide_prpoc) cert on cert.ide_prcla=pc.ide_prcla and cert.ide_prfuf = ppf.ide_prfuf and cert.ide_prfup=poa.ide_prfup ";
		   
		   //VALIDANDO EL VALOR DEL IVA POR CREDITO TRIBUTARIO 
		   sql += " left join (select distinct cast(sum(valor_iva) as numeric(8,2)) as valor_iva, ide_prpoa,pm.ide_prfuf   " +
			" from pre_mensual pm   " +
			" join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
			" join (select ide_tecpo,sum(coalesce(valor_iva_adfac,0)) as valor_iva from adq_factura afac where afac.ide_srsuc=1 group by ide_tecpo) afac on afac.ide_tecpo=pm.ide_tecpo " +
			" where ide_prpoa>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa,pm.ide_prfuf) ivc on ivc.ide_prpoa = poa.ide_prpoa and ivc.ide_prfuf=ppf.ide_prfuf  " ;
			
			if(grupos_compromisos) 
				sql+=" left join (select ide_prcla,ide_prfuf,ide_prfup,0 as nro_compromiso,ide_prpoc,sum(comprometido_prpot) as compromiso from ( " +
						"select a.ide_prpoa,ide_prfuf,ide_prfup,comprometido_prpot,ide_prcla,ide_prpoc from  pre_poa_tramite a, pre_poa b, pre_tramite c " +
						"where a.ide_prpoa = b.ide_prpoa and a.ide_prtra=c.ide_prtra and fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"' " +
						") a group by ide_prcla,ide_prfuf,ide_prfup,ide_prpoc) comp on comp.ide_prcla=pc.ide_prcla and comp.ide_prfuf = ppf.ide_prfuf and comp.ide_prfup=poa.ide_prfup and comp.ide_prpoc=cert.ide_prpoc " ;
			else
				sql+=" left join (select ide_prcla,ide_prfuf,ide_prfup,secuencial_prtra as nro_compromiso,ide_prpoc,sum(comprometido_prpot) as compromiso,ide_prtra from ( " +
					"select a.ide_prpoa,ide_prfuf,ide_prfup,comprometido_prpot,ide_prcla,c.ide_prtra,c.secuencial_prtra,ide_prpoc from  pre_poa_tramite a, pre_poa b, pre_tramite c " +
					"where a.ide_prpoa = b.ide_prpoa and a.ide_prtra=c.ide_prtra and fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"' " +
					") a group by ide_prcla,ide_prfuf,ide_prfup,ide_prtra,secuencial_prtra,ide_prpoc) comp on comp.ide_prcla=pc.ide_prcla and comp.ide_prfuf = ppf.ide_prfuf and comp.ide_prfup=poa.ide_prfup and comp.ide_prpoc=cert.ide_prpoc " ;
					
			if(grupo)
			{
			  if(!grupos_compromisos) 
				  sql+=" left join (select ide_prcla,ide_prfuf,ide_prfup,0 as nro_comprobante,ide_prtra, sum(devengado_prmen) as devengado from (  " +
					"select a.ide_prpoa,b.ide_pranu,a.ide_prcla,c.ide_prfuf,ide_prfup,devengado_prmen, c.ide_prtra   " +
					"from pre_poa a, pre_anual b, pre_mensual c  " +
					"where a.ide_prpoa = b.ide_prpoa and b.ide_pranu = c.ide_pranu and devengado_prmen != 0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' " +
					" ) a group by ide_prcla,ide_prfuf,ide_prfup,ide_prtra) dev on dev.ide_prcla=pc.ide_prcla and dev.ide_prfuf = ppf.ide_prfuf and dev.ide_prfup=poa.ide_prfup and dev.ide_prtra=comp.ide_prtra ";
			  else
				  sql+=" left join (select ide_prcla,ide_prfuf,ide_prfup,0 as nro_comprobante, sum(devengado_prmen) as devengado from (  " +
						"select a.ide_prpoa,b.ide_pranu,a.ide_prcla,c.ide_prfuf,ide_prfup,devengado_prmen " +
						"from pre_poa a, pre_anual b, pre_mensual c  " +
						"where a.ide_prpoa = b.ide_prpoa and b.ide_pranu = c.ide_pranu and devengado_prmen != 0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' " +
						") a group by ide_prcla,ide_prfuf,ide_prfup) dev on dev.ide_prcla=pc.ide_prcla and dev.ide_prfuf = ppf.ide_prfuf and dev.ide_prfup=poa.ide_prfup " ;
	
			}
			else
			   sql+=" left join (select ide_prcla,ide_prfuf,ide_prfup,ide_tecpo as nro_comprobante,ide_prtra, sum(devengado_prmen) as devengado from (  " +
					"select a.ide_prpoa,b.ide_pranu,a.ide_prcla,c.ide_prfuf,ide_prfup,devengado_prmen, ide_tecpo,c.ide_prtra   " +
					"from pre_poa a, pre_anual b, pre_mensual c  " +
					"where a.ide_prpoa = b.ide_prpoa and b.ide_pranu = c.ide_pranu and devengado_prmen != 0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' " +
					") a group by ide_prcla,ide_prfuf,ide_prfup,ide_tecpo,ide_prtra) dev on dev.ide_prcla=pc.ide_prcla and dev.ide_prfuf = ppf.ide_prfuf and dev.ide_prfup=poa.ide_prfup and dev.ide_prtra=comp.ide_prtra "
					+ "order by poa.ide_prfup,pc.ide_prcla,ppf.ide_prfuf,nro_certificacion_prcer,nro_compromiso,nro_comprobante " ;

		}
		else
		{ //consolidado por numero de certificacion
			sql+="select poa.ide_prpoa,poa.ide_geani, pc.ide_prcla,poa.ide_prfup, '"+fecha_inicial+"' as fecha_inicial, '"+fecha_final+"' as fecha_corte, programa,proyecto,producto,codigo_subactividad, subactividad, pc.codigo_clasificador_prcla, pc.descripcion_clasificador_prcla  "+
	                 " ,'N/A' as fuente, coalesce(inicial,0) as inicial,coalesce(reforma,0) as reforma,(coalesce(inicial,0)+coalesce(reforma,0)) as codificado   " +
	                 " ,nro_certificacion_prcer,coalesce(certificado,0) as certificado,(coalesce(inicial,0)+coalesce(reforma,0))-coalesce(certificado,0) saldo_certificado " +
	                 " ,0 as nro_compromiso,coalesce(compromiso,0) as compromiso,coalesce(certificado,0)-coalesce(compromiso,0) as saldo_compromiso   " +
	                 " ,0 as nro_comprobante,coalesce(devengado,0) as devengado,coalesce(compromiso,0)-coalesce(devengado,0) as saldo_devengado, valor_iva, detalle_geare as area  " +
	               
	                 " from pre_clasificador pc  " +
	                 " join pre_poa poa on poa.ide_prcla = pc.ide_prcla " +
	                 " left join gen_area area on area.ide_geare=poa.ide_geare   " +
	                 " left join (" +serv_presupuesto.getUbicacionPOA()+") desPoa on desPoa.ide_prfup=poa.ide_prfup  " +
	           
	                 " left join (select ide_prcla,ide_prfup,sum(valor_financiamiento_prpof) as inicial from (  " +
	                 " select a.ide_prpoa,valor_financiamiento_prpof,ide_prcla,ide_prfup from  pre_poa_financiamiento a, pre_poa b  " +
	                 " where a.ide_prpoa = b.ide_prpoa " +
	                 " ) a group by ide_prcla,ide_prfup) ini on ini.ide_prcla=pc.ide_prcla and ini.ide_prfup=poa.ide_prfup " +
	               
	                 " left join (select ide_prcla,ide_prfup,sum(valor_reformado_prprf) as reforma from ( " +
	                 " select a.ide_prpoa,valor_reformado_prprf,ide_prcla,ide_prfup from  pre_poa_reforma_fuente a, pre_poa b  " +
	                 " where a.ide_prpoa = b.ide_prpoa and fecha_prprf between  '"+fecha_inicial+"' and '"+fecha_final+"' " +
	                 " ) a group by ide_prcla,ide_prfup) reff on reff.ide_prcla=pc.ide_prcla and reff.ide_prfup=poa.ide_prfup " +
	              
	                 " left join (select ide_prcla,ide_prfup,nro_certificacion_prcer,ide_prcer,sum(valor_certificado_prpoc) as certificado from (     " +
	                 " select a.ide_prpoa,ide_prfup,valor_certificado_prpoc,ide_prcla,nro_certificacion_prcer,c.ide_prcer from  pre_poa_certificacion a, pre_poa b, pre_certificacion c " +
	                 " where a.ide_prpoa = b.ide_prpoa and a.ide_prcer=c.ide_prcer and fecha_prcer between  '"+fecha_inicial+"' and '"+fecha_final+"' " +
	                 " ) a group by ide_prcla,ide_prfup,nro_certificacion_prcer,ide_prcer) cert on cert.ide_prcla=pc.ide_prcla and cert.ide_prfup=poa.ide_prfup  " +
	             
	                 "left join (select ide_prcla,ide_prfup,ide_prcer,sum(comprometido_prpot) as compromiso from (  " +
	                 "select b.ide_prcla,b.ide_prfup,a.ide_prpoc,a.comprometido_prpot,d.ide_prcer from pre_poa_tramite a, pre_poa b, pre_tramite c, pre_poa_certificacion d  " +
	                 "where a.ide_prpoa = b.ide_prpoa and a.ide_prtra=c.ide_prtra and a.ide_prpoc=d.ide_prpoc and c.fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"' " +
	                 ") a group by ide_prcla,ide_prfup,ide_prcer ) comp on comp.ide_prcla=cert.ide_prcla and comp.ide_prfup=cert.ide_prfup and comp.ide_prcer=cert.ide_prcer    " ;
			
			//VALIDANDO EL VALOR DEL IVA POR CREDITO TRIBUTARIO 
			   sql += " left join (select distinct cast(sum(valor_iva) as numeric(8,2)) as valor_iva, ide_prpoa " +
				" from pre_mensual pm   " +
				" join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				" join (select ide_tecpo,sum(coalesce(valor_iva_adfac,0)) as valor_iva from adq_factura afac where afac.ide_srsuc=1 group by ide_tecpo) afac on afac.ide_tecpo=pm.ide_tecpo " +
				" where ide_prpoa>0 and fecha_ejecucion_prmen between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa) ivc on ivc.ide_prpoa = poa.ide_prpoa " ;
	            
			   sql += " left join (select ide_prcla,ide_prfup,ide_prcer, sum(devengado_prmen) as devengado from ( " +
	                 "select distinct a.ide_prpoa, b.ide_pranu, a.ide_prcla, c.ide_prfuf, a.ide_prfup, c.devengado_prmen, e.ide_prcer, d.ide_prtra, c.ide_tecpo " +
	                 "from pre_poa a, pre_anual b, pre_mensual c, pre_poa_tramite d, pre_poa_certificacion e  " +
	                 "where a.ide_prpoa = b.ide_prpoa and b.ide_pranu = c.ide_pranu and devengado_prmen != 0  " +
	                 "  and d.ide_prtra=c.ide_prtra and d.ide_prpoc=e.ide_prpoc and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' " +
	                 ") a group by ide_prcla,ide_prfup,ide_prcer) dev on dev.ide_prcla=cert.ide_prcla and dev.ide_prfup=cert.ide_prfup and dev.ide_prcer=cert.ide_prcer "+
	                 "order by pc.ide_prcla,poa.ide_prfup,nro_certificacion_prcer ";
			
				
	                    
		}
		sql+="  ) b where b.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_final+"') as varchar));";

		System.out.println("consulta ejecucion presupuestaria: "+ sql);
		return sql;
	}
	
	public String getEjecucionPresupuestariaIngreso(String fecha_inicial,String fecha_final){
		String sql="select pc.ide_prcla, extract(month from DATE '"+fecha_final+"') as periodo, extract(year from DATE '"+fecha_final+"') as detalle_geani, pc.codigo_clasificador_prcla,pc.descripcion_clasificador_prcla, coalesce(inicial,0.00) as inicial"
			+" , coalesce(reforma,0.00) as reforma, coalesce(inicial,0.00)+coalesce(reforma,0.00) as codificado, coalesce(devengado,0.00) as devengado, coalesce(cobrado,0.00) as cobrado, ff.detalle_prfuf "
			+" from pre_clasificador pc"
			+" left join pre_anual panu on panu.ide_prcla=pc.ide_prcla"
			+" left join pre_fuente_financiamiento ff on ff.ide_prfuf=panu.ide_prfuf"  
			
			+" left join (select ide_prcla,ide_prfuf,ide_geani,sum(valor_inicial_pranu) as inicial from ("
			+" 	    select a.ide_prcla,a.ide_prfuf,valor_inicial_pranu,ide_geani from pre_anual a where not a.ide_prcla is null"
			+" 	   ) a group by ide_prcla,ide_prfuf,ide_geani) ini on ini.ide_prcla=pc.ide_prcla and ini.ide_prfuf = ff.ide_prfuf and ini.ide_geani=panu.ide_geani"
			
			+" left join ( select ide_prcla,ide_prfuf,ide_geani,sum(reforma) as reforma from  ("
			+" 		select a.ide_prcla,a.ide_prfuf,ide_geani,(val_reforma_d_prrem - val_reforma_h_prrem) as reforma from pre_anual a, pre_reforma_mes b"
			+" 		where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and fecha_reforma_prrem between '"+fecha_inicial+"' and '"+fecha_final+"'"
			+" 	) a group by ide_prcla,ide_prfuf,ide_geani ) ref on ref.ide_prcla=pc.ide_prcla and ref.ide_prfuf = ff.ide_prfuf and ref.ide_geani=panu.ide_geani"
			
			+" left join ( select ide_prcla,ide_prfuf,ide_geani,sum(devengado) as devengado from  ("
			+" 		select a.ide_prcla,a.ide_prfuf,ide_geani,devengado_prmen as devengado from pre_anual a, pre_mensual b"
			+" 		where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"'"
			+" 	) a group by ide_prcla,ide_prfuf,ide_geani ) dev on dev.ide_prcla=pc.ide_prcla and dev.ide_prfuf = ff.ide_prfuf and dev.ide_geani=panu.ide_geani"
			
			+" left join ( select ide_prcla,ide_prfuf,ide_geani,sum(cobrado) as cobrado from  ("
			+" 	select a.ide_prcla,a.ide_prfuf,ide_geani,cobrado_prmen as cobrado from pre_anual a, pre_mensual b"
			+" 	where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"'"
			+" 	) a group by ide_prcla,ide_prfuf,ide_geani ) cob on cob.ide_prcla=pc.ide_prcla and cob.ide_prfuf = ff.ide_prfuf and cob.ide_geani=panu.ide_geani"
			
			+" where panu.ide_prcla is not null and pc.tipo_prcla = 1 and panu.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_final+"') as varchar)) ";
		return sql;
	}
	
	public String getEjecucionPresupuestariaPlanificadaMensual(String fecha_inicial,String fecha_final){
		// Fecha final para sacar los datos del periodo
		
		String sql="select poa.ide_prpoa,poa.detalle_geani,poa.tipo_gestion,poa.detalle_programa,poa.detalle_proyecto,poa.detalle_producto_mc,poa.detalle_actividad_mc,poa.detalle_actividad,poa.detalle_subactividad,poa.area,poa.fuente,poa.tipo_gasto,poa.codigo_clasificador_prcla,poa.descripcion_clasificador_prcla,poa.presupuesto_codificado_prpoa,"
		+" codificado_enero,devengado_enero,"
		+" codificado_febrero,devengado_febrero,"
		+" codificado_marzo,devengado_marzo,"
		+" codificado_abril,devengado_abril,"
		+" codificado_mayo,devengado_mayo,"
		+" codificado_junio,devengado_junio,"
		+" codificado_julio,devengado_julio,"
		+" codificado_agosto,devengado_agosto,"
		+" codificado_septiembre,devengado_septiembre,"
		+" codificado_octubre,devengado_octubre,"
		+" codificado_noviembre,devengado_noviembre,"
		+" codificado_diciembre,devengado_diciembre ";
		
		sql+=" from ( ";		
		sql+=getEjecucionPresupuestaria(fecha_inicial,fecha_final);		
		sql+=") poa ";
		
		sql+=" left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_enero FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=1) enero on enero.ide_prpoa=poa.ide_prpoa  " +
				"left join (select distinct sum(devengado_prmen) as devengado_enero, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=1  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) denero on denero.ide_prpoa = poa.ide_prpoa and denero.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_febrero FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=2) febrero on febrero.ide_prpoa=poa.ide_prpoa  " +
				"left join (select distinct sum(devengado_prmen) as devengado_febrero, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=2  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dfebrero on dfebrero.ide_prpoa = poa.ide_prpoa and dfebrero.fuente=poa.fuente " +
				"         " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_marzo FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=3) marzo on marzo.ide_prpoa=poa.ide_prpoa  " +
				"left join (select distinct sum(devengado_prmen) as devengado_marzo, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=3  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dmarzo on dmarzo.ide_prpoa = poa.ide_prpoa and dmarzo.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_abril FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=4) abril on abril.ide_prpoa=poa.ide_prpoa  " +
				"left join (select distinct sum(devengado_prmen) as devengado_abril, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=4  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dabril on dabril.ide_prpoa = poa.ide_prpoa and dabril.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_mayo FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=5) mayo on mayo.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_mayo, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=5  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dmayo on dmayo.ide_prpoa = poa.ide_prpoa and dmayo.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_junio FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=6) junio on junio.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_junio, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=6  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) djunio on djunio.ide_prpoa = poa.ide_prpoa and djunio.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_julio FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=7) julio on julio.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_julio, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=7  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) djulio on djulio.ide_prpoa = poa.ide_prpoa and djulio.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_agosto FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=8) agosto on agosto.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_agosto, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=8  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dagosto on dagosto.ide_prpoa = poa.ide_prpoa and dagosto.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_septiembre FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=9) septiembre on septiembre.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_septiembre, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=9  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dseptiembre on dseptiembre.ide_prpoa = poa.ide_prpoa and dseptiembre.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_octubre FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=10) octubre on octubre.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_octubre, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=10  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) doctubre on doctubre.ide_prpoa = poa.ide_prpoa and doctubre.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_noviembre FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=11) noviembre on noviembre.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_noviembre, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=11  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) dnoviembre on dnoviembre.ide_prpoa = poa.ide_prpoa and dnoviembre.fuente=poa.fuente " +
				" " +
				"left join (SELECT ide_prpoa,ide_gemes, valor_presupuesto_prpmc as codificado_diciembre FROM pre_poa_mes_codificado  " +
				"	where activo_prpmc=true and ide_gemes=12) diciembre on diciembre.ide_prpoa=poa.ide_prpoa " +
				"left join (select distinct sum(devengado_prmen) as devengado_diciembre, ide_prpoa, detalle_prfuf as fuente from pre_mensual pm  " +
				"	join pre_anual pa on pa.ide_pranu=pm.ide_pranu   " +
				"	join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf " +
				"	where ide_prpoa>0 and abs(devengado_prmen)>0 and extract(month from fecha_ejecucion_prmen)=12  " +
				"        group by ide_prpoa,detalle_prfuf,extract(month from fecha_ejecucion_prmen)) ddiciembre on ddiciembre.ide_prpoa = poa.ide_prpoa and ddiciembre.fuente=poa.fuente";
		
		System.out.println("getEjecucionPresupuestariaPlanificadaMensual: "+ sql);
		return sql;
	}
	
	public String getCodigo_partidas(){
		String sql="select distinct gpp.ide_gepap, codigo_partida_gepap, detalle_gepap, titulo_cargo_gepgc as titulo_cargo,DETALLE_GEGRO as grupo_ocupacional,DETALLE_GECAF as cargo_funcional from gen_partida_presupuestaria gpp " +
		" join gen_partida_grupo_cargo ggc on ggc.ide_gepap=gpp.ide_gepap " +
		" join GEN_GRUPO_OCUPACIONAL ggo on ggo.IDE_GEGRO=ggc.IDE_GEGRO " +
		" join (select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a, GEN_GRUPO_CARGO b where a.IDE_GECAF = b.IDE_GECAF) gcf on gcf.IDE_GECAF=ggc.IDE_GECAF " +
		" where activo_gepap=true " +
		" order by 1";
	
		return sql;
	}
	
	public TablaGenerica getTablaGenericaCodigo_partidas(String ide_gepap) {
		TablaGenerica tab_poa=utilitario.consultar("select ide_gepap as codigo, * from gen_partida_presupuestaria where ide_gepap in ("+ide_gepap+")");
		return tab_poa;
		
	}
}
