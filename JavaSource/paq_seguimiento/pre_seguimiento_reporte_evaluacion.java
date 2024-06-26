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

public class pre_seguimiento_reporte_evaluacion extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Framework framework = new Framework();
    private Tabla tab_seguimiento_reporte_general = new Tabla();

    public pre_seguimiento_reporte_evaluacion() {
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
    			+ "rec.ide_serec,rec.numero_serec,rec.asunto_serec,  "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,  "
    			+ "(select numero_seeva from seg_evaluacion where  ide_seeva=3) as numero_evaluacion,  " 
    			+ " (select asunto_seeva from seg_evaluacion where  ide_seeva=3) as asunto_evaluacion, "
    			+ "(select nombre_archivo_seeva from seg_evaluacion where  ide_seeva=3) as nombre_archivo_evaluacion,"
    			+ "(select archivo_adjunto_seeva from seg_evaluacion where  ide_seeva=3) as archivo_adjunto_evaluacion, "
    			+ "(select estrec.descripcion_seesr from seg_detalle_evaluacion sde1 "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = sde1.ide_seesr  "
    			+ "where ide_seinf=inf.ide_seinf and ide_serec=rec.ide_serec and ide_seeva=3) as estado_actual_rec_evaluacion,"
    			+ "(select  "
    			+"APELLIDO_PATERNO_GTEMP || ' ' || " 
    			+"APELLIDO_MATERNO_GTEMP || ' ' ||  " 
    			+"PRIMER_NOMBRE_GTEMP || ' ' ||  " 
    			+"SEGUNDO_NOMBRE_GTEMP AS NOMBRES  " 
    			+ "from seg_detalle_evaluacion sde2  "
    			+ "left join GTH_EMPLEADO emp on emp.ide_gtemp=sde2.ide_gtemp where ide_seeva=3 and ide_seinf=inf.ide_seinf and ide_serec=rec.ide_serec) as nombres " 
    			+ "FROM   "
    			+ "seg_informe inf  "
    			+ "LEFT JOIN seg_recomendacion rec ON inf.ide_seinf = rec.ide_seinf  "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr   "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui   "
    			+ "LEFT JOIN seg_estado_informe estinf ON inf.ide_seesi = estinf.ide_seesi   "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
    			+ "left join seg_detalle_evaluacion  sde on sde.ide_seinf=rec.ide_seinf and sde.ide_serec=rec.ide_serec  "
    			+ "left join seg_evaluacion eva on eva.ide_seeva=sde.ide_seeva  "
    			+ "where eva.ide_seeva IN(3) "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf,  "
    			+ "rec.ide_serec,rec.numero_serec,rec.asunto_serec,   "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr ORDER BY  inf.ide_seinf,rec.ide_serec");
    	
    	
    	
  
    	
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

      	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setNombreVisual("T.RECO");
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").alinearCentro();

      	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setNombreVisual("T.RESP");
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").alinearCentro();
    	
    	
    	
    	
    	tab_seguimiento_reporte_general.getColumna("numero_evaluacion").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("numero_evaluacion").setNombreVisual("NUM. EVALUACION");
    	tab_seguimiento_reporte_general.getColumna("numero_evaluacion").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("numero_evaluacion").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("asunto_evaluacion").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("asunto_evaluacion").setNombreVisual("ASUNTO_EVALUACION");
    	tab_seguimiento_reporte_general.getColumna("asunto_evaluacion").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("asunto_evaluacion").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("nombre_archivo_evaluacion").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("nombre_archivo_evaluacion").setNombreVisual("NOM.ARCHIVO");
    	tab_seguimiento_reporte_general.getColumna("nombre_archivo_evaluacion").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("nombre_archivo_evaluacion").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("archivo_adjunto_evaluacion").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("archivo_adjunto_evaluacion").setNombreVisual("ARCHIVO_ADJUNTO");
    	tab_seguimiento_reporte_general.getColumna("archivo_adjunto_evaluacion").setLongitud(15);
    	tab_seguimiento_reporte_general.getColumna("archivo_adjunto_evaluacion").alinearCentro();
    	    	
      	tab_seguimiento_reporte_general.getColumna("estado_actual_rec_evaluacion").setFiltroContenido();
    	tab_seguimiento_reporte_general.getColumna("estado_actual_rec_evaluacion").setNombreVisual("ESTADO_RECOMENDACION_EVALUACION");
    	tab_seguimiento_reporte_general.getColumna("estado_actual_rec_evaluacion").setLongitud(35);
    	tab_seguimiento_reporte_general.getColumna("estado_actual_rec_evaluacion").alinearCentro();


      	tab_seguimiento_reporte_general.getColumna("nombres").setFiltroContenido();
    	tab_seguimiento_reporte_general.getColumna("nombres").setNombreVisual("USUARIO RESPONSABLE");
    	tab_seguimiento_reporte_general.getColumna("nombres").setLongitud(35);
    	tab_seguimiento_reporte_general.getColumna("nombres").alinearCentro();

    	
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
