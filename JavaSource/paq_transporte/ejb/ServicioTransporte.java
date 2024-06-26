package paq_transporte.ejb;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;


/**
 * Session Bean implementation class ServicioTransporte
 */
@Stateless
public class ServicioTransporte {
	private Utilitario utilitario = new Utilitario();

	public ServicioTransporte() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Para buscar vehiculos en los activos fijos
	 * 
	 * @return
	 */
	public String getActivoFijoVehiculo() {
		String str_sql = "select ide_afact, placa_afact, marca_afact, modelo_afact, chasis_afact from afi_activo where ide_afnoa in (108,109); ";
		return str_sql;
	}

	/**
	 * retorna el empleado
	 * 
	 * @return
	 */
	public String getSqlEmpleadosAutocompletar() {
		String str_sql = "SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
				+ "DEPA.DETALLE_GEDEP "
				+ "FROM GTH_EMPLEADO EMP "
				+ "LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP and epar.activo_geedp= true "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.APELLIDO_PATERNO_GTEMP , "
				+ "EMP.APELLIDO_MATERNO_GTEMP , "
				+ "EMP.PRIMER_NOMBRE_GTEMP , " + "EMP.SEGUNDO_NOMBRE_GTEMP , "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " + "DEPA.DETALLE_GEDEP "
				+ "order by APELLIDO_PATERNO_GTEMP";
		return str_sql;
	}

	/**
	 * retorna el conductor
	 * 
	 * @return
	 */
	public String getSqlConductor(String estado) {
		String str_sql = "SELECT COND.IDE_VECON,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
				+ "FROM  VEH_CONDUCTOR COND  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=COND.IDE_GTEMP AND COND.ACTIVO_VECON in ("+estado+" ) ";
		return str_sql;
	}

	public String getSqlEmpleado() {
		String str_sql = "SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
				+ "FROM  GTH_EMPLEADO EMP   " + "WHERE EMP.ACTIVO_GTEMP in (true,false)";
		return str_sql;
	}

	public Date calculaFecha(Date fecha, int valor) {

		Date fechaVigencia = new Date();
		fechaVigencia = utilitario.getDate();

		fechaVigencia = utilitario.sumarDiasFecha(fecha, valor);
		System.out.println("la nueva fecha es" + fechaVigencia + "");

		return fechaVigencia;
	}

	public String getVehiculosMantenimiento() {

		String vehiculos = " select veh.placa_veveh, tip.detalle_vetip, veh.kilometraje_veveh,"
				+ "veh.lectura_aceite_veveh,veh.lectura_abc_veveh, veh.lectura_llantas_veveh,"
				+ "par.detalle_vepav, par.valor_frecuencia_vepav, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE ACEITE' THEN (veh.lectura_aceite_veveh+  par.valor_frecuencia_vepav) END AS valor_aceite, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE LLANTAS' THEN (veh.lectura_llantas_veveh+  par.valor_frecuencia_vepav) END AS valor_llantas, "
				+ "case par.detalle_vepav WHEN 'MANTENIMIENTO ABC' THEN (veh.lectura_abc_veveh+  par.valor_frecuencia_vepav) END AS valor_abc "
				+ " from veh_tipo_vehiculo tip "
				+ " left  join veh_vehiculo veh on veh.ide_vetip=tip.ide_vetip "
				+ " left join veh_parametro_vehiculo par on par.ide_vetip=tip.ide_vetip ";

		return vehiculos;
	}

	public String getVehiculosMantenimiento(String grupos, String placa,
			String fecha_inicial, String fecha_final) {

		String tab_vehiculos = " select veh.placa_veveh, par.detalle_vepav,  veh.kilometraje_veveh,"
				+ "veh.lectura_aceite_veveh,veh.lectura_abc_veveh, veh.lectura_llantas_veveh,"
				+ " tip.detalle_vetip, par.valor_frecuencia_vepav, veh.fecha_ultimo_pago_veveh, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE ACEITE' THEN (veh.lectura_aceite_veveh+  par.valor_frecuencia_vepav) END AS valor_aceite, "
				+ "case par.detalle_vepav WHEN 'MANTENIMIENTO ABC' THEN (veh.lectura_abc_veveh+  par.valor_frecuencia_vepav) END AS valor_abc, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE LLANTAS' THEN (veh.lectura_llantas_veveh+  par.valor_frecuencia_vepav) END AS valor_llantas, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE ACEITE' THEN round((((veh.lectura_aceite_veveh+ par.valor_frecuencia_vepav)-veh.kilometraje_veveh)*100/(par.valor_alerta_vepav)),2) END AS alerta_aceite, "
				+ "case par.detalle_vepav WHEN 'MANTENIMIENTO ABC' THEN round((((veh.lectura_abc_veveh+ par.valor_frecuencia_vepav)-veh.kilometraje_veveh)*100/par.valor_alerta_vepav),2) END AS alerta_abc, "
				+ "case par.detalle_vepav WHEN 'CAMBIO DE LLANTAS' THEN round((((veh.lectura_llantas_veveh+ par.valor_frecuencia_vepav)-veh.kilometraje_veveh)*100/par.valor_alerta_vepav),2) END AS alerta_llantas "
				+ " from veh_tipo_vehiculo tip "
				+ " left  join veh_vehiculo veh on veh.ide_vetip=tip.ide_vetip "
				+ " left join veh_parametro_vehiculo par on par.ide_vetip=tip.ide_vetip "
				+ "where veh.activo_veveh='true' ";
		if (grupos.equals("1")) {
			tab_vehiculos += " and veh.ide_veveh ='" + placa + "'";
		}
		if (grupos.equals("2")) {
			tab_vehiculos += " and veh.fecha_ultimo_pago_veveh between '"
					+ fecha_inicial + "' and '" + fecha_final + "' ";
		}
		return tab_vehiculos;
	}

	
	public String getSolicitudesVehiculo(String grupos, String ide_veveh,
			String fecha_inicial, String fecha_final) {

		String tab_solicitud = 
				
		"select soli.ide_vesol as num_solicitud, lugar_agendamiento, soli.fecha_solicitud_vesol as fecha_solicitud,fecha_salida, trim(case  when to_char(fecha_salida,'d') = '1' then 'DOMINGO' "
		+"    when to_char(fecha_salida,'d') = '2' then 'LUNES' "
		+"    when to_char(fecha_salida,'d') = '3' then 'MARTES' "
		+"    when to_char(fecha_salida,'d') = '4' then 'MIERCOLES' "
		+"    when to_char(fecha_salida,'d') = '5' then 'JUEVES' "
		+"    when to_char(fecha_salida,'d') = '6' then 'VIERNES' "
		+"    when to_char(fecha_salida,'d') = '7' then 'SABADO' "
		+"    ELSE 'LIBRE' "
		+"   end) as dia, hora_salida, hora_retorno, soli.motivo_vesol as motivo,"
		+"  soli.detalle_vetes as estado_solicitud, coalesce(soli.aprobado_vesol,false) as aprobado_vesol,jefe_inmediato,soli.nombres_apellidos as empleado_solicitante, soli.funcionario_pasajero as empleado_pasajero,"
		+"  soli.detalle_verut as ruta, v.placa_veveh as placa_vehículo, (condu.nombre || ' ' ||  condu.apellido) as conductor, t.detalle_vetip as tipo_vehículo, ocup_solicitud"
		+"  from (select s.ide_vesol,ss.nom_sucu as lugar_agendamiento,aprobado_vesol, fecha_solicitud_vesol, motivo_vesol, detalle_vetes, s.ocupantes_vesol as ocup_solicitud,"
		+"  	EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
		+"  	EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
		+" 	,coalesce(EMP2.PRIMER_NOMBRE_GTEMP,EMP2.SEGUNDO_NOMBRE_GTEMP) || ' ' || coalesce(EMP2.APELLIDO_PATERNO_GTEMP,EMP2.APELLIDO_MATERNO_GTEMP) AS FUNCIONARIO_PASAJERO"
		
		+"  	,sr.detalle_verut, s.ide_veveh, s.fecha_salida_vesol as fecha_salida, s.hora_salida_vesol as hora_salida, s.hora_retorno_vesol as hora_retorno, ji.PRIMER_NOMBRE_GTEMP ||' '|| ji.APELLIDO_PATERNO_GTEMP as jefe_inmediato "
		+"  	from veh_solicitud s "
		+"  	left join sis_sucursal ss ON ss.ide_sucu=s.ide_sucu  "
		+"  	left join veh_solicitud_estado e ON e.ide_vetes=s.ide_vetes  "
		+"  	left join gth_empleado emp ON emp.ide_gtemp=s.ide_gtemp "
		+"  	left join gth_empleado emp2 ON emp2.ide_gtemp=s.gen_ide_gtemp "
		+"  	left join ( select vru.ide_vesol, detalle_verut from veh_solicitud_ruta vru "
		+"  	left join veh_ruta rut ON rut.ide_verut=vru.ide_verut)sr ON sr.ide_vesol = s.ide_vesol  "
		+ "     left join (SELECT EPAR.IDE_GEEDP, EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
		+ "	               EMP.APELLIDO_PATERNO_GTEMP, EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, SUCU.NOM_SUCU, AREA.IDE_GEARE,AREA.DETALLE_GEARE, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
		+ "	               LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
		+ "	               LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE) ji on ji.IDE_GEEDP=s.IDE_GEEDP "
		+"  	order by s.ide_vesol ) soli"
		
		+"  left join veh_vehiculo v ON v.ide_veveh=soli.ide_veveh "
		+"  left join veh_tipo_vehiculo t ON t.ide_vetip=v.ide_vetip  "
		
		+"  left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
		+"  where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=v.ide_vecon ";		
		
		if (grupos.equals("1")) {
			tab_solicitud += " where v.ide_veveh ='" + ide_veveh + "' and soli.fecha_solicitud_vesol between '" + fecha_inicial + "' and '" + fecha_final + "' ";
		}
		if (grupos.equals("2")) {
			tab_solicitud += " where soli.fecha_solicitud_vesol between '" + fecha_inicial + "' and '" + fecha_final + "' ";
		}
		
		tab_solicitud += " order by fecha_salida,hora_salida ";
		
		System.out.println("getSolicitudesVehiculo: " +tab_solicitud);
		
		return tab_solicitud;
	}

	
	public String getSolicitudesVehiculoDias(String grupos, String ide_veveh,
			String fecha_inicial, String fecha_final) {

		String tab_solicitud="";
		
		/*if(fecha_inicial==null && fecha_inicial.isEmpty()){
			Date d1 = new Date();
			d1.getDate();
			fecha_inicial=utilitario.DeDateAString(d1);
		}*/
		
		if(fecha_inicial.length()<5)
		{
			fecha_inicial = utilitario.getFechaActual();
		}
		
		if(fecha_final.length()<5)
		{
			fecha_final = utilitario.getFechaActual();
		}
		
		try {
			tab_solicitud ="select v.ide_veveh as Num, v.placa_veveh, (condu.nombre || ' ' ||  condu.apellido) as conductor, s.fecha_salida, " 
						+"trim(case  when to_char(fecha_salida,'d') = '1' then 'DOMINGO' "
						 +" when to_char(fecha_salida,'d') = '2' then 'LUNES' "
						 +" when to_char(fecha_salida,'d') = '3' then 'MARTES' "
						 +" when to_char(fecha_salida,'d') = '4' then 'MIERCOLES' "
						 +" when to_char(fecha_salida,'d') = '5' then 'JUEVES' "
						 +" when to_char(fecha_salida,'d') = '6' then 'VIERNES' "
						 +" when to_char(fecha_salida,'d') = '7' then 'SABADO' "
						 +" ELSE 'LIBRE' "
						 +"end) as dia, s.hora_salida, s.hora_retorno, ruta, s.num_solicitud, s.ocup_solicitud, v.capacidad_pasajeros_veveh as cap_pasajeros,s.cap_restante "
						+"from veh_vehiculo v "
						+"left join "
						+"(select sol.ide_veveh, veh.placa_veveh, count(*) as soli_generadas, sum (sol.ocupantes_vesol) as ocup_solicitud "
						+",capacidad_pasajeros_veveh,(-sum(sol.ocupantes_vesol)+ capacidad_pasajeros_veveh) as cap_restante, ruta.ruta, "
						+"sol.fecha_salida_vesol as fecha_salida, sol.hora_salida_vesol as hora_salida, sol.hora_retorno_vesol as hora_retorno, sol.ide_vesol as num_solicitud "
						+"from veh_solicitud sol "
						+"left join veh_vehiculo veh ON veh.ide_veveh = sol.ide_veveh "
						+"left join (select  sr.ide_vesol, detalle_verut as ruta from veh_solicitud_ruta sr, veh_ruta vr where " 
						+"vr.ide_verut=sr.ide_verut and vr.activo_verut=true ) ruta ON ruta.ide_vesol=sol.ide_vesol " 
						+"where activo_vesol= 'true' and sol.ide_veveh is not null and ide_vetes in (1,2,3,4) "
						+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh, ruta.ruta, " 
						+"sol.fecha_salida_vesol, sol.hora_salida_vesol, sol.hora_retorno_vesol, sol.ide_vesol "
						+") s on s.ide_veveh= v.ide_veveh "
						+"left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
						+"where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=v.ide_vecon " 
						+"where v.activo_veveh = 'true' " ;
						if (grupos.equals("1")) {
							tab_solicitud += " and v.ide_veveh ='" + ide_veveh + "'";
						}
						if (grupos.equals("2")) {
							tab_solicitud += " and s.fecha_salida between '"
									+ fecha_inicial + "' and '" + fecha_final + "' ";
						}
//						"order by s.fecha_salida,s.hora_salida ;" ;
					
					
//					
//					+"right join "
//					+"(select s.ide_vesol, fecha_solicitud_vesol, motivo_vesol, detalle_vetes, " 
//					+"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " 
//					+"EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
//					+",sr.detalle_verut, s.ide_veveh "
//					+"from veh_solicitud s "
//					+"left join veh_solicitud_estado e ON e.ide_vetes=s.ide_vetes " 
//					+"left join gth_empleado emp ON emp.ide_gtemp=s.ide_gtemp "
//					+"left join ( select vru.ide_vesol, detalle_verut from veh_solicitud_ruta vru "
//					+"left join veh_ruta rut ON rut.ide_verut=vru.ide_verut)sr ON sr.ide_vesol = s.ide_vesol " 
//					+"where s.ide_vetes in (1,2,3,4)  order by s.ide_vesol) soli ON soli.ide_veveh=v.ide_veveh "
//					+ " left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
//					+" where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=v.ide_vecon "
//					+"where v.activo_veveh = 'true' ";
//			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tab_solicitud;
	}

	
//	public String getConsultaAsistencia(String fecha_inicial, String fecha_final) {
//		String tab_asi_vacacion = "select a.ide_aspvh,a.ide_asmot,a.ide_gtemp,a.ide_geedp,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,"
//				+ " titulo_cargo_gepgc,nom_sucu,detalle_geare,detalle_gttem,fecha_solicitud_aspvh,(case when aprobado_tthh_aspvh is true then 'Aprobado' else 'No Aprobado' end) as aprobado_talento_humano,"
//				+ " fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh,nro_dias_aspvh,tipo_aspvh,(case when tipo_aspvh=1 then 'Permisos Horas' when tipo_aspvh = 2 then 'Vacaciones' when tipo_aspvh =3 then 'Horas Extras' when tipo_aspvh =4 then 'Permisos por Dias'  end) as tipo_solicitud,"
//				+ " nro_documento_aspvh,nro_horas_aspvh,hora_desde_aspvh,hora_hasta_aspvh,aprobado_tthh_aspvh,aprobado_aspvh,"
//				+ " razon_anula_aspvh,documento_anula_aspvh,fecha_anula_aspvh "
//
//				+ ", (select COALESCE(SUM(DIA_ACUMULADO_ASDEV),0) +  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0) from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as NRO_TOTALES_VACACIONES, "
//				+ "(select ( COALESCE(SUM(DIA_ACUMULADO_ASDEV),0)+  COALESCE(SUM(DIA_ADICIONAL_ASDEV),0))-( COALESCE(SUM(DIA_DESCONTADO_ASDEV),0)+ COALESCE(SUM(DIA_SOLICITADO_ASDEV),0))   from asi_detalle_vacacion where fecha_novedad_asdev <= fecha_solicitud_aspvh  and ide_asvac in (select ide_asvac from asi_vacacion where ide_gtemp = c.ide_gtemp)) as DIAS_PENDIENTES "
//
//				+ " from asi_permisos_vacacion_hext a"
//				+ " left join asi_motivo b on a.ide_asmot = b.ide_asmot"
//				+ " left join gth_empleado c on a.ide_gtemp = c.ide_gtemp"
//				+ " left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp"
//				+ " left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc"
//				+ " left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf"
//				+ " left join gen_area g on d.ide_geare = g.ide_geare"
//				+ " left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu"
//				+ " where fecha_solicitud_aspvh between '"
//				+ fecha_inicial
//				+ "' and '"
//				+ fecha_final
//				+ "' "
//				+ " order by detalle_gttem,apellido_paterno_gtemp,fecha_solicitud_aspvh desc";
//		return tab_asi_vacacion;
//
//	}

	public String getDatosVehiculos() {
		String tab_datos = "select ide_veveh,placa_veveh "
				+ "from veh_vehiculo where activo_veveh = true "
				+ "order by placa_veveh";

		return tab_datos;
	}

	public String getAlertaMantenimiento(String valor) {
		String color = "";
		// String valor = alerta.getValorDefecto().toString();
		try {

			if (Integer.parseInt(valor) > 150) {
				color = "font-size:13px;font-weight: bold;text-decoration: underline;color:black";
			}
			if (Integer.parseInt(valor) < 50) {
				color = "font-size:13px;font-weight: bold;text-decoration: underline;color:red";
			} else {
				color = "font-size:13px;font-weight: bold;text-decoration: underline;color:green";
			}
			return color;
		} catch (Exception e) {
			System.out.println("es un error de " + e);
		}
		return color;

	}

	public double getHoras(String horaInicial, String horaFinal) {

		double horasComision = 0;
		double minutos=0;

		horasComision = utilitario.getDiferenciaHoras(
				utilitario.DeStringAHora(horaInicial),
				utilitario.DeStringAHora(horaFinal));
		minutos=utilitario.getDiferenciaMinutos(utilitario.DeStringAHora(horaInicial),
				utilitario.DeStringAHora(horaFinal));
		minutos = minutos/60;
		
		System.out.println("horas " +horasComision );
		System.out.println("minutos " +minutos);
//		horasComision=horasComision+minutos;
		System.out.println("las nueva horas " +horasComision );
		
		return horasComision;
	}

	public int getCalcularDias(String diaInicial, String diaFinal) {

		int diasComision = 0;

		diasComision = utilitario.getDiferenciasDeFechas(
				utilitario.DeStringADate(diaInicial),
				utilitario.DeStringADate(diaFinal)) + 1;
		return diasComision;
	}

	public double getCalcularHoras(String str_hora_inicial,
			String str_hora_final) {

		Date hora_inicial = utilitario.getHora(utilitario
				.getFormatoHora(str_hora_inicial));
		Date hora_final = utilitario.getHora(utilitario
				.getFormatoHora(str_hora_final));

		hora_inicial.getHours();
		Integer totalSegundosHoraEntrada = 0;
		Integer totalSegundosHoraSalida = 612000;

		int total_segundos_hora_inicial = (hora_inicial.getHours() * 3600)
				+ (hora_inicial.getMinutes() * 60) + hora_inicial.getSeconds();
		int total_segundos_hora_final = (hora_final.getHours() * 3600)
				+ (hora_final.getMinutes() * 60) + hora_final.getSeconds();

		if (total_segundos_hora_inicial < totalSegundosHoraEntrada) {
			total_segundos_hora_inicial = totalSegundosHoraEntrada;
		}
		if (total_segundos_hora_final > totalSegundosHoraSalida) {
			total_segundos_hora_final = totalSegundosHoraSalida;
		}

//		System.out.println("hora inicial " + total_segundos_hora_inicial);
//		System.out.println("hora final " + total_segundos_hora_final);
		int total_diferencia_segundo = total_segundos_hora_final
				- total_segundos_hora_inicial;

		int total_horas = total_diferencia_segundo / 3600;
		int nuevo_valor = total_diferencia_segundo - (total_horas * 3600);
		int total_minutos = nuevo_valor / 60;
		int total_segundos = nuevo_valor - (total_minutos * 60);

		double total_diferencia_segundos = ((total_horas * 3600)
				+ (total_minutos * 60) + total_segundos);
		double total_diferencia_horas = total_diferencia_segundos / 3600;
//		if (total_diferencia_horas > 8)
//			total_diferencia_horas = 8;
//		System.out.println("Horas: " + total_diferencia_horas);
		
		return total_diferencia_horas;
		
//		solicitudVacaciones.setNroHorasAspvh(new BigDecimal(
//				total_diferencia_horas));
//
//		System.out.println("solicitud de vacaciones "
//				+ solicitudVacaciones.getNroHorasAspvh());
//
//		nuemro_horas_reporte = total_diferencia_horas + "";
//		hora_inicial_reporte = str_hora_inicial;
//		hora_final_reporte = str_hora_final;
//
	}
	
	public static int numeroDiasEntreDosFechas(Date fecha1, Date fecha2){
		
	     long startTime = fecha1.getTime();
	     long endTime = fecha2.getTime();
	     long diffTime = endTime - startTime;
//	    System.out.println("la hora" + diffTime);
	     return (int)TimeUnit.HOURS.convert(diffTime, TimeUnit.HOURS);
	     
	}

	public String getVehiculoDisponible(String fechaInicial, String fechaFinal ) {
		String str_sql="";
		
		if(fechaInicial.length()<5)
		{
			fechaInicial = utilitario.getFechaActual();
		}
		
		if(fechaFinal.length()<5)
		{
			fechaFinal = utilitario.getFechaActual();
		}
		
		//if(fechaInicial!=null && !fechaInicial.isEmpty() && fechaFinal!=null && !fechaFinal.isEmpty()){
		
			str_sql= " select veh.ide_veveh, veh.placa_veveh as placa,(condu.nombre || ' ' ||  condu.apellido) as conductor, '','',veh.capacidad_pasajeros_veveh as cap_pasajeros, '','','','','' "
					+ "from veh_vehiculo veh "
					+ "left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
					+" where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=veh.ide_vecon "
					+" where veh.ide_veveh not in ( select sol.ide_veveh "
					+" from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
					+" and (sol.fecha_salida_vesol between '"+fechaInicial+"' and '"+fechaFinal+"'" 
			        +" or sol.fecha_regreso_vesol  between '"+fechaInicial+"' and '"+fechaFinal+"') "
			        +" and sol.ide_veveh is not null and veh.activo_veveh = true ) and veh.activo_veveh = true; ";
			
			System.out.println("getVehiculoDisponible "+str_sql);
			return str_sql;
			
		/*}else {
			
			Date d1 = new Date();
			Date d2 = new Date();
			
			d1.getDate();
			d2.getDate();
			
//			System.out.println("d1" +d1+"  d2" +d2);
			fechaInicial=utilitario.DeDateAString(d1);
			fechaInicial=utilitario.DeDateAString(d2);

			str_sql= " select veh.ide_veveh, veh.placa_veveh as placa, '','',veh.capacidad_pasajeros_veveh as cap_pasajeros, '','','','','', (condu.nombre || ' ' ||  condu.apellido) as conductor "
					+ "from veh_vehiculo veh "
					+ "left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
					+" where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=veh.ide_vecon "
					+ " where veh.ide_veveh not in ( select sol.ide_veveh "
					+" from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
					+" and sol.fecha_salida_vesol between '"+d1+"' and '"+d2+"'" 
			        +" or sol.fecha_regreso_vesol  between '"+d1+"' and '"+d2+"' "
			        + "  and sol.ide_veveh is not null and veh.activo_veveh = true ) and veh.activo_veveh = true; ";

			
//			str_sql= " select veh.ide_veveh, veh.placa_veveh from veh_vehiculo veh where veh.ide_veveh not in ( select sol.ide_veveh "
//					+" from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
//					+" and sol.fecha_salida_vesol between '"+d1+"' and '"+d2+"'" 
//			        +" or sol.fecha_regreso_vesol  between '"+d1+"' and '"+d2+"' "
//			        + " and sol.ide_veveh is not null and veh.activo_veveh = true ); ";
//			System.out.println("el segundo sql"+str_sql);
			return str_sql;

		}
		
		
//				
//		try {
//			String str_sql= " select veh.ide_veveh, veh.placa_veveh from veh_vehiculo veh where veh.ide_veveh not in ( select sol.ide_veveh "
//							+" from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
//							+" and sol.fecha_salida_vesol between '"+utilitario.DeStringADate(fechaInicial)+"' and '"+utilitario.DeStringADate(fechaFinal)+"'" 
//					        +" or sol.fecha_regreso_vesol  between '"+utilitario.DeStringADate(fechaInicial)+"' and '"+utilitario.DeStringADate(fechaFinal)+"' "
//					        + " and veh.activo_veveh = true ); ";
//			return str_sql;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;*/
			
	}

	public String getVehiculoCapacidad(String fechaInicial ) {
		String str_sql="";
		
		if(fechaInicial.length()<5)
		{
			fechaInicial = utilitario.getFechaActual();
		}
		
		//if(fechaInicial!=null && !fechaInicial.isEmpty() ){
			
			str_sql=""
					+ "select v.ide_veveh, v.placa_veveh, (condu.nombre || ' ' ||  condu.apellido) as conductor,s.soli_generadas, s.ocup_solicitud, v.capacidad_pasajeros_veveh as cap_pasajeros,s.cap_restante, ruta, "
					+"s.fecha_salida, s.hora_salida, s.hora_retorno "
					+"from veh_vehiculo v "
					+"left join "
					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as soli_generadas, sum (sol.ocupantes_vesol) as ocup_solicitud "
					+",capacidad_pasajeros_veveh,(capacidad_pasajeros_veveh - sum(sol.ocupantes_vesol)) as cap_restante, ruta.ruta,  "
					+"sol.fecha_salida_vesol as fecha_salida, sol.hora_salida_vesol as hora_salida, sol.hora_retorno_vesol as hora_retorno "
					+"from veh_solicitud sol "
					+"left join veh_vehiculo veh ON veh.ide_veveh = sol.ide_veveh "
					+"left join (select  sr.ide_vesol, detalle_verut as ruta from veh_solicitud_ruta sr, veh_ruta vr where " 
					+"vr.ide_verut=sr.ide_verut and vr.activo_verut=true ) ruta ON ruta.ide_vesol=sol.ide_vesol " 
					+"where activo_vesol= 'true' and sol.ide_veveh is not null and ide_vetes in (1,2) "
					+"and fecha_salida_vesol >= '"+fechaInicial+"' "
					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh, ruta.ruta, " 
					+"sol.fecha_salida_vesol, sol.hora_salida_vesol, sol.hora_retorno_vesol "
					+") s on s.ide_veveh= v.ide_veveh "
					+"left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
					+"where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=v.ide_vecon " 
					+"--where v.ide_veveh=3 "
					+"order by ide_veveh, s.hora_salida";
			
//			str_sql="select v.ide_veveh, v.placa_veveh, s.solicitudes_generadas, s.ocupantes_solicitud, v.capacidad_pasajeros_veveh,s.capacidad_restante "
//					+"from veh_vehiculo v "
//					+"left join "
//					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as solicitudes_generadas, sum (sol.ocupantes_vesol) as ocupantes_solicitud "
//					+",capacidad_pasajeros_veveh,(sum(sol.ocupantes_vesol)- capacidad_pasajeros_veveh) as capacidad_restante "
//					+"from veh_solicitud sol, veh_vehiculo veh "
//					+"where veh.ide_veveh = sol.ide_veveh and "
//					+"activo_vesol= 'true' and sol.ide_veveh is not null "
//					+"and fecha_salida_vesol >= '"+utilitario.DeStringADate(fechaInicial)+"' " 
//					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh) s on s.ide_veveh= v.ide_veveh "
//					+"order by ide_veveh ; " ;

			System.out.println("fechaInicial "+fechaInicial);
			//return str_sql;
			
		/*}else {
			
			Date d1 = new Date();
			d1.getDate();
			fechaInicial=utilitario.DeDateAString(d1);

			str_sql=""
					+ "select v.ide_veveh, v.placa_veveh, s.soli_generadas, s.ocup_solicitud, v.capacidad_pasajeros_veveh as cap_pasajeros,s.cap_restante, ruta, "
					+"s.fecha_salida, s.hora_salida, s.hora_retorno, (condu.nombre || ' ' ||  condu.apellido) as conductor "
					+"from veh_vehiculo v "
					+"left join "
					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as soli_generadas, sum (sol.ocupantes_vesol) as ocup_solicitud "
					+",capacidad_pasajeros_veveh,(sum(sol.ocupantes_vesol)- capacidad_pasajeros_veveh) as cap_restante, ruta.ruta,  "
					+"sol.fecha_salida_vesol as fecha_salida, sol.hora_salida_vesol as hora_salida, sol.hora_retorno_vesol as hora_retorno "
					+"from veh_solicitud sol "
					+"left join veh_vehiculo veh ON veh.ide_veveh = sol.ide_veveh "
					+"left join (select  sr.ide_vesol, detalle_verut as ruta from veh_solicitud_ruta sr, veh_ruta vr where " 
					+"vr.ide_verut=sr.ide_verut and vr.activo_verut=true ) ruta ON ruta.ide_vesol=sol.ide_vesol " 
					+"where activo_vesol= 'true' and sol.ide_veveh is not null and ide_vetes in (1,2) "
					+"and fecha_salida_vesol >= '"+d1+"' "
					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh, ruta.ruta, " 
					+"sol.fecha_salida_vesol, sol.hora_salida_vesol, sol.hora_retorno_vesol "
					+") s on s.ide_veveh= v.ide_veveh "
					+"left join (select c.ide_vecon, e.primer_nombre_gtemp as nombre, e.apellido_paterno_gtemp as apellido  from veh_conductor c, gth_empleado e "
					+"where c.ide_gtemp=e.ide_gtemp) condu ON condu.ide_vecon=v.ide_vecon " 
					+"--where v.ide_veveh=3 "
					+"order by ide_veveh, s.hora_salida";
			
			
//			str_sql="select v.ide_veveh, v.placa_veveh, s.solicitudes_generadas, s.ocupantes_solicitud, v.capacidad_pasajeros_veveh,s.capacidad_restante "
//					+"from veh_vehiculo v "
//					+"left join "
//					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as solicitudes_generadas, sum (sol.ocupantes_vesol) as ocupantes_solicitud "
//					+",capacidad_pasajeros_veveh,(sum(sol.ocupantes_vesol)- capacidad_pasajeros_veveh) as capacidad_restante "
//					+"from veh_solicitud sol, veh_vehiculo veh "
//					+"where veh.ide_veveh = sol.ide_veveh and "
//					+"activo_vesol= 'true' and sol.ide_veveh is not null "
//					+"and fecha_salida_vesol >= '"+d1+"' " 
//					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh) s on s.ide_veveh= v.ide_veveh "
//					+"order by ide_veveh ; " ;
			
			System.out.println("el2 sql "+str_sql);
		}*/
		
		System.out.println("getVehiculoCapacidad "+str_sql);
		return str_sql;
	
	}	

	public String getVehiculoDisponibilidad(String fechaInicial, String ide ) {
		String str_sql="";
		
		if(fechaInicial.length()<5)
		{
			fechaInicial = utilitario.getFechaActual();
		}
		
		//if(fechaInicial!=null && !fechaInicial.isEmpty() ){
			
			str_sql="select v.ide_veveh, v.placa_veveh, s.solicitudes_generadas, s.ocupantes_solicitud, v.capacidad_pasajeros_veveh,s.capacidad_restante "
					+"from veh_vehiculo v "
					+"left join "
					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as solicitudes_generadas, sum (sol.ocupantes_vesol) as ocupantes_solicitud "
					+",capacidad_pasajeros_veveh,(sum(sol.ocupantes_vesol)- capacidad_pasajeros_veveh) as capacidad_restante "
					+"from veh_solicitud sol, veh_vehiculo veh "
					+"where veh.ide_veveh = sol.ide_veveh and "
					+"activo_vesol= 'true' and sol.ide_veveh is not null "
					+"and fecha_salida_vesol >= '"+fechaInicial+"' " 
					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh) s on s.ide_veveh= v.ide_veveh "
					+" where v.ide_veveh= "+ide+" order by ide_veveh ; " ;
			return str_sql;
		/*}else {
			
			Date d1 = new Date();
			d1.getDate();
			fechaInicial=utilitario.DeDateAString(d1);

			str_sql="select v.ide_veveh, v.placa_veveh, s.solicitudes_generadas, s.ocupantes_solicitud, v.capacidad_pasajeros_veveh,s.capacidad_restante "
					+"from veh_vehiculo v "
					+"left join "
					+"(select sol.ide_veveh, veh.placa_veveh, count(*) as solicitudes_generadas, sum (sol.ocupantes_vesol) as ocupantes_solicitud "
					+",capacidad_pasajeros_veveh,(sum(sol.ocupantes_vesol)- capacidad_pasajeros_veveh) as capacidad_restante "
					+"from veh_solicitud sol, veh_vehiculo veh "
					+"where veh.ide_veveh = sol.ide_veveh and "
					+"activo_vesol= 'true' and sol.ide_veveh is not null "
					+"and fecha_salida_vesol >= '"+d1+"' " 
					+"group by sol.ide_veveh, veh.placa_veveh, capacidad_pasajeros_veveh) s on s.ide_veveh= v.ide_veveh "
					+" where v.ide_veveh= "+ide+" order by ide_veveh ; " ;
			return str_sql;
		}*/
	}	

	public String getVehiculos()
	{
		String sql="SELECT ide_veveh,placa_veveh, cast(coalesce(marca_afact,'') || ' ' || "
				+" coalesce(modelo_afact,'') || ' ' || "
				+" coalesce(color_afact,'') as character varying(50)) as Vehiculo,cast(NOMBRES_APELLIDOS as character varying(50)) as conductor"
				+" FROM public.veh_vehiculo vh"
				+" join afi_activo afi on afi.ide_afact=vh.ide_afact"
				+" join (SELECT COND.IDE_VECON,EMP.DOCUMENTO_IDENTIDAD_GTEMP,coalesce(EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP) || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP) AS NOMBRES_APELLIDOS" 
				+" FROM  VEH_CONDUCTOR COND  "
				+" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=COND.IDE_GTEMP AND COND.ACTIVO_VECON = true) cond on cond.ide_vecon=vh.ide_vecon"
				+" where vh.activo_veveh=true"
				+" order by 1";
		System.out.println("getVehiculos :" +sql);
		return sql;
	}
	
	public String getAgendaVehiculo(String ide_veveh, String fecha_inicial, String fecha_final)
	{
		String sql="select s.ide_vesol, s.ide_veveh , fecha_salida_vesol, s.hora_salida_vesol, fecha_regreso_vesol, s.hora_retorno_vesol,motivo_vesol, detalle_vetes,  "
				+" coalesce(EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP) || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP) AS FUNCIONARIO "
				+" ,sr.detalle_verut"
				+" from veh_solicitud s "
				+" left join veh_solicitud_estado e ON e.ide_vetes=s.ide_vetes  "
				+" left join gth_empleado emp ON emp.ide_gtemp=s.gen_ide_gtemp "
				+" left join ( select vru.ide_vesol, detalle_verut from veh_solicitud_ruta vru "
				+" 	left join veh_ruta rut ON rut.ide_verut=vru.ide_verut)sr ON sr.ide_vesol = s.ide_vesol  "	
				+" where s.ide_vetes in (2) and s.fecha_salida_vesol between '" + fecha_inicial + "' and '" + fecha_final + "' and s.ide_veveh="+ide_veveh;
		
		sql+=" order by s.fecha_salida_vesol,s.hora_salida_vesol ";
		//System.out.println("getAgendaVehiculo: "+sql);
		return sql;
	}
	
	public String getAgendaVehiculoRangos(String ide_veveh, String fecha_inicial, String fecha_final)
	{
		String sql="select ide_vesol, ide_veveh, fecha_salida_vesol, hora_salida_vesol, fecha_regreso_vesol, hora_retorno_vesol, motivo_vesol, detalle_vetes, FUNCIONARIO, detalle_verut"
				+" FROM ("
				+" select s.ide_vesol, s.ide_veveh , fecha_salida_vesol, s.hora_salida_vesol, fecha_regreso_vesol, s.hora_retorno_vesol,motivo_vesol, detalle_vetes,   "
				+" coalesce(EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP) || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP) AS FUNCIONARIO  "
				+" ,sr.detalle_verut "
				+" from veh_solicitud s  "
				+" left join veh_solicitud_estado e ON e.ide_vetes=s.ide_vetes   "
				+" left join gth_empleado emp ON emp.ide_gtemp=s.gen_ide_gtemp  "
				+" left join ( select vru.ide_vesol, detalle_verut from veh_solicitud_ruta vru  	"
				+" 	left join veh_ruta rut ON rut.ide_verut=vru.ide_verut) sr ON sr.ide_vesol = s.ide_vesol   "
				+" where s.ide_vetes in (2) "
				+" and s.ide_veveh="+ide_veveh
				+" and s.fecha_salida_vesol between '" + fecha_inicial + "' and '" + fecha_final + "' and s.fecha_salida_vesol = s.fecha_regreso_vesol"
				
				+" union all "
				
				+" select s.ide_vesol, s.ide_veveh , '" + fecha_inicial + "', s.hora_salida_vesol, '" + fecha_inicial + "', s.hora_retorno_vesol,motivo_vesol, detalle_vetes,   "
				+" coalesce(EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP) || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP) AS FUNCIONARIO  "
				+" ,sr.detalle_verut "
				+" from veh_solicitud s  "
				+" left join veh_solicitud_estado e ON e.ide_vetes=s.ide_vetes   "
				+" left join gth_empleado emp ON emp.ide_gtemp=s.gen_ide_gtemp  "
				+" left join ( select vru.ide_vesol, detalle_verut from veh_solicitud_ruta vru  	"
				+" 	left join veh_ruta rut ON rut.ide_verut=vru.ide_verut) sr ON sr.ide_vesol = s.ide_vesol   "
				+" where s.ide_vetes in (2) "
				+" and s.ide_veveh="+ide_veveh
				+" and '" + fecha_inicial + "' between s.fecha_salida_vesol and s.fecha_regreso_vesol and s.fecha_salida_vesol <> s.fecha_regreso_vesol "
				+" ) a ";
		
		sql+=" order by fecha_salida_vesol,hora_salida_vesol  ";
		//System.out.println("getAgendaVehiculoRangos: "+sql);
		return sql;
	}
	
}
