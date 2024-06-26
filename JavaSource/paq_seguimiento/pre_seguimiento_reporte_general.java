/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.primefaces.event.FileUploadEvent;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Framework;
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

public class pre_seguimiento_reporte_general extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Framework framework = new Framework();
    private Tabla tab_seguimiento_reporte_general = new Tabla();

    public pre_seguimiento_reporte_general() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		

		cal_fecha_inicial.setFechaActual();
		//bar_botones.agregarComponente(cal_fecha_inicial);

		cal_fecha_final.setFechaActual();
		//bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("CONSULTAR");
		bot_aprobar_solicitud.setMetodo("cambiarFecha");
		//bar_botones.agregarBoton(bot_aprobar_solicitud);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		//bar_botones.agregarBoton(bot_limpiar);
    	
    	tab_seguimiento_reporte_general.setId("tab_seguimiento_reporte_general");
    	tab_seguimiento_reporte_general.setSql("select  "
    			+ "inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,"
    			+ "cast (extract( YEAR from resp.fecha_seres)as INTEGER) as anio,cast (extract( MONTH from resp.fecha_seres) as INTEGER) as mes,"
    			+ "cast (extract( DAY from resp.fecha_seres) as INTEGER) as dia,"
    			+ "usu.nombre_seusu "
    			+ "FROM  "
    			+ "seg_respuesta resp "
    			+ "LEFT JOIN seg_usuario usu on usu.ide_seusu= resp.ide_seusu "
    			+ "LEFT JOIN seg_empresa emp ON  emp.ide_seemp = resp.ide_seemp "
    			+ "LEFT JOIN seg_recomendacion rec ON resp.ide_serec = rec.ide_serec  "
    			+ "LEFT JOIN seg_informe inf ON rec.ide_seinf = inf.ide_seinf  "
    			+ "LEFT JOIN   seg_asignacion asi  ON  resp.ide_serec = rec.ide_serec and  emp.ide_seemp=asi.ide_seemp and  rec.ide_seinf=asi.ide_seinf  "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr  "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui  "
    			+ "LEFT JOIN seg_estado_informe estinf ON inf.ide_seesi = estinf.ide_seesi  "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre "
    			//+ "a.anulado_aspvh in(false) and a.ide_gtemp=-1 "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu "
    			+ "ORDER BY  inf.ide_seinf");
    	tab_seguimiento_reporte_general.setCampoPrimaria("ide_seinf");
    	tab_seguimiento_reporte_general.getColumna("ide_seinf").setLongitud(20);
    	tab_seguimiento_reporte_general.getColumna("ide_seinf").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setNombreVisual("SUSCRIBE");
    	
      	tab_seguimiento_reporte_general.getColumna("numero_seinf").setFiltroContenido();
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").setLongitud(50);
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").setNombreVisual("NUM.INF");
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").alinearCentro();
    	
      	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setNombreVisual("FEC. APROB");
    	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").alinearCentro();
    	
    	
      	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").setNombreVisual("FEC. INI");
    	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").alinearCentro();
    	
      	tab_seguimiento_reporte_general.getColumna("fecha_fin_seinf").setNombreVisual("FEC. TENTATIVA");
    	tab_seguimiento_reporte_general.getColumna("fecha_fin_seinf").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_fin_seinf").alinearCentro();
    	
      	tab_seguimiento_reporte_general.getColumna("numero_serec").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("numero_serec").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("numero_serec").setNombreVisual("NUM.REC");
    	tab_seguimiento_reporte_general.getColumna("numero_serec").alinearCentro();
    	
    	
    	
    	tab_seguimiento_reporte_general.getColumna("asunto_serec").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("asunto_serec").setLongitud(55);
    	tab_seguimiento_reporte_general.getColumna("asunto_serec").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("asunto_serec").setNombreVisual("RECOMENDACION");
    	
     	tab_seguimiento_reporte_general.getColumna("descripcion_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seres").setLongitud(55);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seres").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("descripcion_seres").setNombreVisual("RESPUESTA");

    	
    	tab_seguimiento_reporte_general.getColumna("descripcion_seemp").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seemp").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seemp").setNombreVisual("AREA RESPONSABLE");
    	tab_seguimiento_reporte_general.getColumna("descripcion_seemp").alinearCentro();
    	
      	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setNombreVisual("T.RECO");
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").alinearCentro();

      	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setNombreVisual("T.RESP");
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_seres").setNombreVisual("FEC. RESP");
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("anio").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("anio").setNombreVisual("ANIO");
    	tab_seguimiento_reporte_general.getColumna("anio").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("anio").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("mes").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("mes").setNombreVisual("MES");
    	tab_seguimiento_reporte_general.getColumna("mes").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("mes").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("dia").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("dia").setNombreVisual("DIA");
    	tab_seguimiento_reporte_general.getColumna("dia").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("dia").alinearCentro();
    	
    	
    	
    	
      	tab_seguimiento_reporte_general.getColumna("nombre_seusu").setFiltroContenido();
    	tab_seguimiento_reporte_general.getColumna("nombre_seusu").setNombreVisual("USUARIO");
    	tab_seguimiento_reporte_general.getColumna("nombre_seusu").setLongitud(35);
    	tab_seguimiento_reporte_general.getColumna("nombre_seusu").alinearCentro();
    	

    	
    	tab_seguimiento_reporte_general.setLectura(true);
    	tab_seguimiento_reporte_general.setNumeroTabla(1);				
    	tab_seguimiento_reporte_general.setRows(20);
   		tab_seguimiento_reporte_general.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("REPORTE GENERAL SEGUIMIENTO");
		pat_panel.setPanelTabla(tab_seguimiento_reporte_general);
		
	
		Division div_division = new Division();		
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	

    }



	public Tabla getTab_seguimiento_reporte_general() {
		return tab_seguimiento_reporte_general;
	}



	public void setTab_seguimiento_reporte_general(
			Tabla tab_seguimiento_reporte_general) {
		this.tab_seguimiento_reporte_general = tab_seguimiento_reporte_general;
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
		
    	tab_seguimiento_reporte_general.setSql("select  "
    			+ "inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu "
    			+ "FROM  "
    			+ "seg_respuesta resp "
    			+ "LEFT JOIN seg_usuario usu on usu.ide_seusu= resp.ide_seusu "
    			+ "LEFT JOIN seg_empresa emp ON  emp.ide_seemp = resp.ide_seemp "
    			+ "LEFT JOIN seg_recomendacion rec ON resp.ide_serec = rec.ide_serec  "
    			+ "LEFT JOIN seg_informe inf ON rec.ide_seinf = inf.ide_seinf  "
    			+ "LEFT JOIN   seg_asignacion asi  ON  resp.ide_serec = rec.ide_serec and  emp.ide_seemp=asi.ide_seemp and  rec.ide_seinf=asi.ide_seinf  "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr  "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui  "
    			+ "LEFT JOIN seg_estado_informe estinf ON inf.ide_seesi = estinf.ide_seesi  "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre "
    			//+ "a.anulado_aspvh in(false) and a.ide_gtemp=-1 "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu "
    			+ "ORDER BY  inf.ide_seinf");
 	tab_seguimiento_reporte_general.ejecutarSql();
   	
		
		
	}
	public void limpiar() {
		tab_seguimiento_reporte_general.limpiar();	
		utilitario.addUpdate("tab_seguimiento_reporte_general");// limpia y refresca el autocompletar
	}

	
	
	
	
}