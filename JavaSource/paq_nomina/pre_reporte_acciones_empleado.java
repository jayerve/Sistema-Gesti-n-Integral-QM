/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

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

public class pre_reporte_acciones_empleado extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();

    public pre_reporte_acciones_empleado() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		

		//cal_fecha_inicial.setFechaActual();
		//bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("REPORTE ACCIONES EMPLEADO"));
		//cal_fecha_final.setFechaActual();
		//bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("cambiarFecha");
		//bar_botones.agregarBoton(bot_aprobar_solicitud);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		//bar_botones.agregarBoton(bot_limpiar);
    	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	tab_permisos_reporte.setSql(" SELECT DISTINCT EPAR.IDE_GEEDP, "
    			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
    			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP,    "
    			+ "GECAR.TITULO_CARGO_GEPGC,TEMP.DETALLE_GTTEM,GEAED.DETALLE_GEAED,GEMED.DETALLE_GEMED,GEGRO.DETALLE_GEGRO,GECAF.DETALLE_GECAF, "
    			+ "EPAR.FECHA_GEEDP,CASE WHEN EPAR.FECHA_FINCTR_GEEDP IS NULL THEN EPAR.FECHA_LIQUIDACION_GEEDP ELSE EPAR.FECHA_FINCTR_GEEDP  END AS "
    			+ "FECHA_FIN, "
    			//SUCU.NOM_SUCU, 
    			+ "AREA.DETALLE_GEARE, "
    			//DEPA.DETALLE_GEDEP,
    			+ "DIR.DETALLE_GTDIR || ' ' || DIR.REFERENCIA_GTDIR AS DIREECION, "
    			+ "TTELF.DETALLE_GTTIT,TELF.NUMERO_TELEFONO_GTTEL,COR.DETALLE_GTCOR, "
    			+ "CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END  "
    			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
    			+ "LEFT JOIN GEN_DETALLE_EMPLEADO_DEPARTAME GEDED ON GEDED.IDE_GEDED=EPAR.IDE_GEDED  "
    			+ "LEFT JOIN GEN_ACCION_MOTIVO_EMPLEADO GEAME ON GEAME.IDE_GEAME=GEDED.IDE_GEAME  "
    			+ "LEFT JOIN GEN_ACCION_EMPLEADO_DEPA  GEAED ON GEAED.IDE_GEAED=GEAME.IDE_GEAED  "
    			+ "LEFT JOIN GEN_MOTIVO_EMPLEADO_DEPA  GEMED ON GEMED.IDE_GEMED=GEAME.IDE_GEMED  "
    			+ "left join GEN_PARTIDA_grupo_cargo GECAR ON GECAR.IDE_GEPGC=EPAR.IDE_GEPGC  "
    			+ "left join GEN_GRUPO_OCUPACIONAL GEGRO ON GEGRO.IDE_GEGRO=EPAR.IDE_GEGRO  "
    			+ "left join GEN_CARGO_FUNCIONAL GECAF ON GECAF.IDE_GECAF=EPAR.IDE_GECAF  "
    			+ "LEFT JOIN GTH_TIPO_EMPLEADO TEMP ON TEMP.IDE_GTTEM=EPAR.IDE_GTTEM   "
    			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
    			+ "LEFT JOIN GTH_CORREO COR ON COR.IDE_GTEMP=EMP.IDE_GTEMP  "
    			+ "LEFT JOIN GTH_DIRECCION DIR ON DIR.IDE_GTEMP=EMP.IDE_GTEMP  "
    			+ "LEFT JOIN GTH_TELEFONO TELF ON TELF.IDE_GTEMP=EMP.IDE_GTEMP  "
    			+ "LEFT JOIN GTH_TIPO_TELEFONO  TTELF ON TTELF.IDE_GTTIT=TELF.IDE_GTTIT  "
    			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
    			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
    			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				//WHERE EMP.IDE_GTEMP=22 AND COR.DETALLE_GTCOR  NOT LIKE '%emgirs%'
    			+ "GROUP BY  "
    			+ "EPAR.IDE_GEEDP, "
    			+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,   "
    			+ "GECAR.TITULO_CARGO_GEPGC,TEMP.DETALLE_GTTEM,GEAED.DETALLE_GEAED,GEMED.DETALLE_GEMED,GEGRO.DETALLE_GEGRO,GECAF.DETALLE_GECAF,TEMP.DETALLE_GTTEM, "
    			+ "EPAR.FECHA_GEEDP,  "
    			+ "AREA.DETALLE_GEARE,  "
    			//DEPA.DETALLE_GEDEP,
    			+ "DIR.DETALLE_GTDIR ,DIR.REFERENCIA_GTDIR, "
    			+ "TTELF.DETALLE_GTTIT,TELF.NUMERO_TELEFONO_GTTEL,COR.DETALLE_GTCOR, "
    			+ "EPAR.ACTIVO_GEEDP "
    			+ "ORDER BY EPAR.IDE_GEEDP ASC,EMP.DOCUMENTO_IDENTIDAD_GTEMP,NOMBRES_APELLIDOS ASC");
    	
    	tab_permisos_reporte.setCampoPrimaria("IDE_GEEDP");
    	tab_permisos_reporte.getColumna("IDE_GEEDP").setLongitud(20);
    	tab_permisos_reporte.getColumna("IDE_GEEDP").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setLongitud(70);
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").alinearCentro();
    	tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");
    	
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(15);
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
    	tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("DOCUMENTO IDENTIDAD");

    	
    	tab_permisos_reporte.getColumna("TITULO_CARGO_GEPGC").setFiltro(true);
    	tab_permisos_reporte.getColumna("TITULO_CARGO_GEPGC").setLongitud(70);
    	tab_permisos_reporte.getColumna("TITULO_CARGO_GEPGC").setNombreVisual("TÍTULO CARGO");
    	tab_permisos_reporte.getColumna("TITULO_CARGO_GEPGC").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("DETALLE_GEGRO").setFiltro(true);
    	tab_permisos_reporte.getColumna("DETALLE_GEGRO").setNombreVisual("CARGO");
    	tab_permisos_reporte.getColumna("DETALLE_GEGRO").setLongitud(55);
    	tab_permisos_reporte.getColumna("DETALLE_GEGRO").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("DETALLE_GEARE").setFiltro(true);
    	tab_permisos_reporte.getColumna("DETALLE_GEARE").setNombreVisual("AREA");
    	tab_permisos_reporte.getColumna("DETALLE_GEARE").setLongitud(35);
    	tab_permisos_reporte.getColumna("DETALLE_GEARE").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("DETALLE_GEAED").setFiltro(true);
    	tab_permisos_reporte.getColumna("DETALLE_GEAED").setNombreVisual("ACCIÓN");
    	tab_permisos_reporte.getColumna("DETALLE_GEAED").setLongitud(70);
    	tab_permisos_reporte.getColumna("DETALLE_GEAED").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("DETALLE_GEMED").setFiltro(true);
    	tab_permisos_reporte.getColumna("DETALLE_GEMED").setNombreVisual("MOTIVO - ACCIÓN");
    	tab_permisos_reporte.getColumna("DETALLE_GEMED").setLongitud(70);
    	tab_permisos_reporte.getColumna("DETALLE_GEMED").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("FECHA_GEEDP").setFiltro(true);
    	tab_permisos_reporte.getColumna("FECHA_GEEDP").setNombreVisual("FECHA INCIOI");
    	tab_permisos_reporte.getColumna("FECHA_GEEDP").setLongitud(25);
    	tab_permisos_reporte.getColumna("FECHA_GEEDP").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("FECHA_FIN").setFiltro(true);
    	tab_permisos_reporte.getColumna("FECHA_FIN").setNombreVisual("FECHA_FIN");
    	tab_permisos_reporte.getColumna("FECHA_FIN").setLongitud(25);
    	tab_permisos_reporte.getColumna("FECHA_FIN").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("DETALLE_GTTEM").setNombreVisual("TIPO EMPLEADO");
    	tab_permisos_reporte.getColumna("DETALLE_GTTEM").setLongitud(25);
    	tab_permisos_reporte.getColumna("DETALLE_GTTEM").alinearCentro();
    	
    	
    	tab_permisos_reporte.getColumna("DETALLE_GECAF").setNombreVisual("CARGO FUNCIONAL");
    	tab_permisos_reporte.getColumna("DETALLE_GECAF").setLongitud(60);
    	tab_permisos_reporte.getColumna("DETALLE_GECAF").alinearCentro();
    	
    	
     	tab_permisos_reporte.getColumna("DIREECION").setNombreVisual("DIRECCIÓN");
    	tab_permisos_reporte.getColumna("DIREECION").setLongitud(100);
    	tab_permisos_reporte.getColumna("DIREECION").alinearCentro();
    	 	
    	
    	tab_permisos_reporte.getColumna("DETALLE_GTCOR").setFiltro(true);
        tab_permisos_reporte.getColumna("DETALLE_GTCOR").setNombreVisual("EMAIL");
    	tab_permisos_reporte.getColumna("DETALLE_GTCOR").setLongitud(25);
    	tab_permisos_reporte.getColumna("DETALLE_GTCOR").alinearCentro();
    	
;
    	tab_permisos_reporte.setLectura(true);
    	tab_permisos_reporte.setNumeroTabla(1);				
    	tab_permisos_reporte.setRows(20);
   		tab_permisos_reporte.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RESUMEN ACCIONES EMPLEADO");
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
    			+ "a.anulado_aspvh in(false) "
    			+ "order by apellido_paterno_gtemp asc,APELLIDO_MATERNO_GTEMP asc,PRIMER_NOMBRE_GTEMP asc,SEGUNDO_NOMBRE_GTEMP asc,fecha_solicitud_aspvh asc");
 	tab_permisos_reporte.ejecutarSql();
   	
		
		
	}
	public void limpiar() {
		tab_permisos_reporte.limpiar();	
		utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
	}

}
