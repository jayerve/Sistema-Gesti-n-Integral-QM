/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */

public class pre_permisos_reporte_enfermedad extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();

    public pre_permisos_reporte_enfermedad() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		

		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("CONSULTAR");
		bot_aprobar_solicitud.setMetodo("cambiarFecha");
		bar_botones.agregarBoton(bot_aprobar_solicitud);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
    	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	tab_permisos_reporte.setSql("select a.ide_aspvh,b.detalle_asmot,documento_identidad_gtemp,  "
    			+ "APELLIDO_PATERNO_GTEMP || ' ' ||   "
    			+ "(case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
    			+ "fecha_solicitud_aspvh, "
    			+ "fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh, "
    			+ "(SELECT EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
    			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
    			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
    			+ "where epar.ide_geedp=a.gen_ide_geedp2) as jefe_inmediato, "
    			+ "hora_desde_aspvh,hora_hasta_aspvh,nro_horas_aspvh,nro_dias_aspvh, "
    			+ "case when tipo_aspvh=1 then 'HORAS' ELSE 'DIAS' END as tipo_permiso, "
    			+ "case when aprobado_aspvh=true then 'APROBADO' ELSE 'SIN APROBAR' END as aprobacion_jefe_inmediato, "
    			+ "case when aprobado_tthh_aspvh=true then 'APROBADO' ELSE 'SIN APROBAR' END as aprobacion_tthh,  "
    			+ "case when anulado_aspvh=true then 'ANULADO' ELSE 'SIN ANUALAR' END as permiso_anulado "
    			+ "	 from asi_permisos_vacacion_hext a "
    			+ "left join asi_motivo b on a.ide_asmot = b.ide_asmot  "
    			+ "left join gth_empleado c on a.ide_gtemp = c.ide_gtemp  "
    			+ "left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp  "
    			+ "left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc   "
    			+ "left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf  "
    			+ "left join gen_area g on d.ide_geare = g.ide_geare "
    			+ "left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu  "
    			+ "where a.fecha_desde_aspvh between '2018-09-01' and '2018-09-30' AND "
    			+ "a.anulado_aspvh in(false) and a.ide_gtemp=-1 and b.ide_asmot=6 "
    			+ "order by apellido_paterno_gtemp asc,APELLIDO_MATERNO_GTEMP asc,PRIMER_NOMBRE_GTEMP asc,SEGUNDO_NOMBRE_GTEMP asc,fecha_solicitud_aspvh asc");
    	tab_permisos_reporte.setCampoPrimaria("ide_aspvh");
    	tab_permisos_reporte.getColumna("ide_aspvh").setLongitud(20);
    	tab_permisos_reporte.getColumna("ide_aspvh").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("detalle_asmot").setFiltro(true);
    	tab_permisos_reporte.getColumna("detalle_asmot").setLongitud(35);
    	tab_permisos_reporte.getColumna("detalle_asmot").alinearCentro();
    	tab_permisos_reporte.getColumna("detalle_asmot").setNombreVisual("MOTIVO");
    	
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(30);
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("DOCUMENTO IDENTIDAD");

    	
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setLongitud(70);
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").alinearCentro();
    	
    	
    	tab_permisos_reporte.getColumna("fecha_solicitud_aspvh").setNombreVisual("FECHA SOLICITUD");
    	tab_permisos_reporte.getColumna("fecha_solicitud_aspvh").setLongitud(35);
    	tab_permisos_reporte.getColumna("fecha_solicitud_aspvh").alinearCentro();
    	
       	
    	tab_permisos_reporte.getColumna("fecha_desde_aspvh").setNombreVisual("FECHA DESDE");
    	tab_permisos_reporte.getColumna("fecha_desde_aspvh").setLongitud(35);
    	tab_permisos_reporte.getColumna("fecha_desde_aspvh").alinearCentro();
    	

    	tab_permisos_reporte.getColumna("fecha_hasta_aspvh").setNombreVisual("FECHA HASTA");
    	tab_permisos_reporte.getColumna("fecha_hasta_aspvh").setLongitud(35);
    	tab_permisos_reporte.getColumna("fecha_hasta_aspvh").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("detalle_aspvh").setNombreVisual("DETALLE");
    	tab_permisos_reporte.getColumna("detalle_aspvh").setLongitud(80);
    	tab_permisos_reporte.getColumna("detalle_aspvh").alinearCentro();
    	
    	
    	tab_permisos_reporte.getColumna("jefe_inmediato").setNombreVisual("JEFE INMEDIATO");
    	tab_permisos_reporte.getColumna("jefe_inmediato").setLongitud(70);
    	tab_permisos_reporte.getColumna("jefe_inmediato").alinearCentro();
    	
    	   	
    	tab_permisos_reporte.getColumna("hora_desde_aspvh").setNombreVisual("HORA DESDE");
    	tab_permisos_reporte.getColumna("hora_desde_aspvh").setLongitud(25);
    	tab_permisos_reporte.getColumna("hora_desde_aspvh").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("hora_hasta_aspvh").setNombreVisual("HORA HASTA");
    	tab_permisos_reporte.getColumna("hora_hasta_aspvh").setLongitud(25);
    	tab_permisos_reporte.getColumna("hora_hasta_aspvh").alinearCentro();
    	
    	
    	tab_permisos_reporte.getColumna("nro_horas_aspvh").setNombreVisual("NRO.HORAS");
    	tab_permisos_reporte.getColumna("nro_horas_aspvh").setLongitud(15);
    	tab_permisos_reporte.getColumna("nro_horas_aspvh").alinearCentro();
    	
    	
     	tab_permisos_reporte.getColumna("nro_dias_aspvh").setNombreVisual("NRO.DIAS");
    	tab_permisos_reporte.getColumna("nro_dias_aspvh").setLongitud(10);
    	tab_permisos_reporte.getColumna("nro_dias_aspvh").alinearCentro();
    	 	
    	
    	tab_permisos_reporte.getColumna("tipo_permiso").setFiltro(true);
        tab_permisos_reporte.getColumna("tipo_permiso").setNombreVisual("TIPO PERMISO");
    	tab_permisos_reporte.getColumna("tipo_permiso").setLongitud(25);
    	tab_permisos_reporte.getColumna("tipo_permiso").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("aprobacion_jefe_inmediato").setFiltro(true);
    	tab_permisos_reporte.getColumna("aprobacion_jefe_inmediato").setNombreVisual("APROBACI�N JEFE INMEDIATO");
    	tab_permisos_reporte.getColumna("aprobacion_jefe_inmediato").setLongitud(45);
    	tab_permisos_reporte.getColumna("aprobacion_jefe_inmediato").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("aprobacion_tthh").setFiltro(true);
       	tab_permisos_reporte.getColumna("aprobacion_tthh").setNombreVisual("APROBACI�N TTHH");
    	tab_permisos_reporte.getColumna("aprobacion_tthh").setLongitud(35);
    	tab_permisos_reporte.getColumna("aprobacion_tthh").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("permiso_anulado").setFiltro(true);
     	tab_permisos_reporte.getColumna("permiso_anulado").setNombreVisual("ANULADO");
    	tab_permisos_reporte.getColumna("permiso_anulado").setLongitud(35);
    	tab_permisos_reporte.getColumna("permiso_anulado").alinearCentro();
    	tab_permisos_reporte.setLectura(true);
    	tab_permisos_reporte.setNumeroTabla(1);				
    	tab_permisos_reporte.setRows(20);
   		tab_permisos_reporte.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RESUMEN DE VACACI�N POR EMPLEADO");
		pat_panel.setPanelTabla(tab_permisos_reporte);
		
	
		Division div_division = new Division();		
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	

    }

	public Tabla getTab_permisos_reporte() {
		return tab_permisos_reporte;
	}

	public void setTab_permisos_reporte(Tabla tab_permisos_reporte) {
		this.tab_permisos_reporte = tab_permisos_reporte;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public void cambiarFecha(){
		
    	tab_permisos_reporte.setSql("select a.ide_aspvh,b.detalle_asmot,documento_identidad_gtemp,  "
    			+ "APELLIDO_PATERNO_GTEMP || ' ' ||   "
    			+ "(case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
    			+ "fecha_solicitud_aspvh, "
    			+ "fecha_desde_aspvh,fecha_hasta_aspvh,detalle_aspvh, "
    			+ "(SELECT EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
    			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
    			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
    			+ "where epar.ide_geedp=a.gen_ide_geedp2) as jefe_inmediato, "
    			+ "hora_desde_aspvh,hora_hasta_aspvh,nro_horas_aspvh,nro_dias_aspvh, "
    			+ "case when tipo_aspvh=1 then 'HORAS' ELSE 'DIAS' END as tipo_permiso, "
    			+ "case when aprobado_aspvh=true then 'APROBADO' ELSE 'SIN APROBAR' END as aprobacion_jefe_inmediato, "
    			+ "case when aprobado_tthh_aspvh=true then 'APROBADO' ELSE 'SIN APROBAR' END as aprobacion_tthh,  "
    			+ "case when anulado_aspvh=true then 'ANULADO' ELSE 'SIN ANUALAR' END as permiso_anulado "
    			+ "	 from asi_permisos_vacacion_hext a "
    			+ "left join asi_motivo b on a.ide_asmot = b.ide_asmot  "
    			+ "left join gth_empleado c on a.ide_gtemp = c.ide_gtemp  "
    			+ "left join gen_empleados_departamento_par d on a.ide_geedp = d.ide_geedp  "
    			+ "left join gen_partida_grupo_cargo e on d.ide_gepgc = e.ide_gepgc   "
    			+ "left join gen_cargo_funcional f on d.ide_gecaf = f.ide_gecaf  "
    			+ "left join gen_area g on d.ide_geare = g.ide_geare "
    			+ "left join gth_tipo_empleado h on d.ide_gttem = h.ide_gttem left join sis_sucursal i on e.ide_sucu = i.ide_sucu  "
    			+ "where a.fecha_desde_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' AND "
    			+ "(a.anulado_aspvh in(false) or a.anulado_aspvh is null) and b.ide_asmot=6 "
    			+ "order by apellido_paterno_gtemp asc,APELLIDO_MATERNO_GTEMP asc,PRIMER_NOMBRE_GTEMP asc,SEGUNDO_NOMBRE_GTEMP asc,fecha_solicitud_aspvh asc");
 	tab_permisos_reporte.ejecutarSql();
   	
		
		
	}
	public void limpiar() {
		tab_permisos_reporte.limpiar();	
		utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
	}

}
