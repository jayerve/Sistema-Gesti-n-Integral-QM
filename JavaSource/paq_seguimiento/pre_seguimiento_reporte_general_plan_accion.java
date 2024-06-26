/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;

import org.primefaces.event.FileUploadEvent;

import paq_general.ejb.ServicioGeneral;
import paq_seguimiento.ejb.ServicioSeguimientoPlan;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Framework;
import framework.aplicacion.TablaGenerica;
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

public class pre_seguimiento_reporte_general_plan_accion extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Framework framework = new Framework();
    private Tabla tab_seguimiento_reporte_general = new Tabla();
    
	@EJB
	private ServicioSeguimientoPlan ser_general = (ServicioSeguimientoPlan) utilitario.instanciarEJB(ServicioSeguimientoPlan.class);

    public pre_seguimiento_reporte_general_plan_accion() {
    	
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaEjecucion");
		bar_botones.agregarBoton(bot_actualiza);
    	
		
    	actualiza_datos_reporte();
    	
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
		 //usu.nombre_seusu 
    	tab_seguimiento_reporte_general.setId("tab_seguimiento_reporte_general");
    	/*tab_seguimiento_reporte_general.setSql("select  inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres ,resp.medio_verificacion_seres,resp.mecanismo_reporte_seres,resp.fecha_desde_seres,resp.fecha_hasta_seres, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,emp.descripcion_seemp "
    			//+ "cast (extract( YEAR from resp.fecha_seres)as INTEGER) as anio,cast (extract( MONTH from resp.fecha_seres) as INTEGER) as mes, "
    			//+ "cast (extract( DAY from resp.fecha_seres) as INTEGER) as dia "
    			+ "from seg_plan_accion plan  "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_respuesta resp  on resp.ide_serec=rec.ide_serec and  resp.ide_seinf=rec.ide_seinf "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=rec.ide_serec  "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp  "
    			+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp  "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui   "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr   "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
    			+ "where resp.ide_sepla is not null "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf,  "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp,tiprec.descripcion_setir,estrec.descripcion_seesr,resp.medio_verificacion_seres,"
    			+ "resp.mecanismo_reporte_seres,resp.fecha_desde_seres,resp.fecha_hasta_seres "
    			+ "order by inf.numero_seinf asc,rec.numero_serec asc");*/
    	
    	
    	
      /*	tab_seguimiento_reporte_general.setSql("select  inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres ,resp.medio_verificacion_seres,resp.mecanismo_reporte_seres,resp.fecha_desde_seres,resp.fecha_hasta_seres, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,est.descripcion_seesp,emp.descripcion_seemp "
    			//+ "cast (extract( YEAR from resp.fecha_seres)as INTEGER) as anio,cast (extract( MONTH from resp.fecha_seres) as INTEGER) as mes, "
    			//+ "cast (extract( DAY from resp.fecha_seres) as INTEGER) as dia "
    			+ "from seg_plan_accion plan  "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_respuesta resp  on resp.ide_serec=rec.ide_serec and  resp.ide_seinf=rec.ide_seinf "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=rec.ide_serec  "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp  "
    			+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp  "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui   "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr   "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
    			+ "where resp.ide_sepla is not null and plan.activo_sepla=true and "
    			+ "resp.activo_seres=true "
    			+ "AND plan.IDE_SEREC IN(SELECT ide_serec "
    			+ "FROM seg_detalle_evaluacion  "
    			+ "where ide_seeva in(4) and ide_seesr not in(2) ) "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf,  "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp,tiprec.descripcion_setir,estrec.descripcion_seesr,resp.medio_verificacion_seres,"
    			+ "resp.mecanismo_reporte_seres,resp.fecha_desde_seres,resp.fecha_hasta_seres,est.descripcion_seesp "
    			+ "order by inf.numero_seinf asc,rec.numero_serec asc,resp.fecha_desde_seres asc,resp.fecha_hasta_seres asc");*/
    	
    	
    	 TablaGenerica tab_recomendaciones_eva = utilitario.consultar("SELECT ide_serec,ide_seeva "
	    			+ "FROM seg_detalle_evaluacion  "
	    			+ "where " //ide_seeva in(4) and "
	    			+ " ide_seesr not in(2)"
	    			);
	    	String ide_serec="",ide_serec_fal="";
			if (tab_recomendaciones_eva.getTotalFilas()>0) {
				for (int i = 0; i < tab_recomendaciones_eva.getTotalFilas(); i++) {
					if (tab_recomendaciones_eva.getTotalFilas()==1) {
						ide_serec=tab_recomendaciones_eva.getValor(i,"ide_serec");
					}else if((tab_recomendaciones_eva.getTotalFilas()-1)==i){
						ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec");
					}else{
					ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec")+",";
					}
				}
			}
			
			
	    	TablaGenerica tab_recomendaciones_fal =utilitario.consultar("select ide_serec,ide_seinf from seg_recomendacion where ide_seinf in(select ide_seinf from seg_informe where ide_seinf>=49)");
	    	if (tab_recomendaciones_fal.getTotalFilas()>0) {
				for (int i = 0; i < tab_recomendaciones_fal.getTotalFilas(); i++) {
					if (tab_recomendaciones_fal.getTotalFilas()==1) {
						ide_serec_fal=tab_recomendaciones_fal.getValor(i,"ide_serec");
					}else if((tab_recomendaciones_fal.getTotalFilas()-1)==i){
						ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec");
					}else{
						ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec")+",";
					}
				}
			}
	    	//	    		"         ("+ide_serec+","+ide_serec_fal+") " +
	  /*  String sql = "select DISTINCT inf.ide_seinf, "
	    		+ "       susinf.descripcion_sesui, "
	    		+ "       inf.numero_seinf, "
	    		+ "       inf.fecha_aprobacion_seinf, "
	    		+ "       inf.fecha_inicio_seinf, "
	    		+ "       rec.numero_serec, "
	    		+ "       rec.asunto_serec, "
	    		+ "       upper(resp.descripcion_seres) as descripcion_seres, "
	    		+ "       upper(resp.medio_verificacion_seres) as medio_verificacion_seres, "
	    		+ "       plan.ide_sepla, "
	    		+ "       deva.ide_seeva, "
	    		+ "       resp.ide_seres, "
	    		+ "       resp.mecanismo_reporte_seres, "
	    		+ "       mecanismo_reporte_seres_enlace, "
	    		+ "       resp.fecha_desde_seres, "
	    		+ "       resp.fecha_hasta_seres, "
	    		+ "       tiprec.descripcion_setir, "
	    		+ "       tresp.descripcion_setre, "
	    		+ "       CASE WHEN resp.fecha_seres <='2022-05-01' THEN emp.descripcion_seemp ELSE emp_plan.descripcion_seemp END as area, "
	    		+ "       CASE WHEN resp.fecha_seres <='2022-05-01' THEN usuario_respuesta.nombre_seusu ELSE usuario_plan.nombre_seusu END as nombre_usuario, "
	    		+ "       per.descripcion_seper, "
	    		+ "       plan.fecha_registro_sepla, "
	    		+ "       EXTRACT (YEAR FROM plan.fecha_registro_sepla)::integer as year_seres, "
	    		+ "       plan.reprogramacion_total_sepla "
	    		+ "from seg_plan_accion plan "
	    		+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf "
	    		+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec "
	    		+ "LEFT JOIN seg_respuesta resp on resp.ide_serec=rec.ide_serec "
	    		+ "    and resp.ide_seinf=rec.ide_seinf AND plan.ide_sepla = resp.ide_sepla "
	    		+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf "
	    		+ "and asi.ide_serec=rec.ide_serec "
	    		+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp "
	    		+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp "
	    		+ "left join seg_empresa emp_plan on emp_plan.ide_seemp=plan.ide_seemp "
	    		+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui "
	    		+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr "
	    		+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir "
	    		+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre "
	    		+ "LEFT JOIN seg_periocidad per on per.ide_seper=plan.ide_seper "
	    		+ "LEFT JOIN seg_detalle_evaluacion deva on deva.ide_seinf=plan.ide_seinf "
	    		+ "    and deva.ide_serec=plan.ide_serec AND deva.ide_seeva IN (3,4) "
	    		+ "LEFT JOIN seg_estado_recomendacion estrec1 ON estrec1.ide_seesr =deva.ide_seesr "
	    		+ "LEFT JOIN seg_usuario usuario_respuesta ON usuario_respuesta.ide_seusu = resp.ide_seusu "
	    		+ "LEFT JOIN seg_usuario usuario_plan ON usuario_plan.ide_seusu = plan.ide_seusu "
	    		+ "where resp.ide_sepla is not null "
	    		+ "    and resp.activo_seres=true "
	    		+ "    AND plan.IDE_SEREC IN "
	    		+ "         ("+ide_serec+","+ide_serec_fal+") "

	    		+ "order by inf.numero_seinf asc, "
	    		+ "         plan.ide_sepla ASC, "
	    		+ "         rec.numero_serec asc, "
	    		+ "         resp.fecha_desde_seres asc, "
	    		+ "         resp.fecha_hasta_seres asc";
    	*/
	    	
	    	
	    	
	    	/*String sql = "select DISTINCT inf.ide_seinf, "
	    			+ "susinf.descripcion_sesui, "
	    			+ "inf.numero_seinf, "
	    			+ "inf.fecha_aprobacion_seinf, "
	    			+ "inf.fecha_inicio_seinf, "
	    			+ "rec.numero_serec, "
	    			+ "rec.asunto_serec, "
	    			+ "upper(resp.descripcion_seres) as descripcion_seres, "
	    			+ "upper(resp.medio_verificacion_seres) as medio_verificacion_seres, "
	    			+ "plan.ide_sepla, "
	    			+ "resp.ide_seres, "
	    			+ "resp.mecanismo_reporte_seres, "
	    			+ "mecanismo_reporte_seres_enlace, "
	    			+ "resp.fecha_desde_seres, "
	    			+ "resp.fecha_hasta_seres, "
	    			+ "tiprec.descripcion_setir, "
	    			+ "tresp.descripcion_setre, "
	    			+ "sehmc.fecha_ingre AS fecha_ingre, "
	    			+ "plan.fecha_registro_sepla, "
	    			+ "CASE WHEN resp.fecha_seres <='2022-05-01' THEN emp.descripcion_seemp  "
	    			+ "WHEN  tresp.ide_setre=3 THEN 'RESPONSABLE DE SEGUIMIENTO' ELSE emp_plan.descripcion_seemp END as area, "
	    			+ "CASE WHEN resp.fecha_seres <='2022-05-01' THEN usuario_respuesta.nombre_seusu  "
	    			+ "WHEN tresp.ide_setre=3 THEN 'ARGOTI VASQUEZ DAVID SEBASTIAN' ELSE  usuario_plan.nombre_seusu END as nombre_usuario,     "
	    			+ "per.descripcion_seper, "
	    			+ "EXTRACT (YEAR FROM plan.fecha_registro_sepla)::integer as year_seres, "
	    			+ " plan.reprogramacion_total_sepla, "
	    			+ "CASE WHEN  tresp.ide_setre=3  THEN resp.fecha_seres else plan.fecha_registro_sepla end as fecha_registro, "
	    			+ "CASE WHEN  sehmc.fecha_ingre IS NULL THEN resp.fecha_ingre else sehmc.fecha_ingre end as fecha_ingre_control "
	    			+ "from seg_plan_accion plan  "
	    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
	    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec AND inf.ide_seinf=plan.ide_seinf  "
	    			+ "LEFT JOIN seg_respuesta resp on resp.ide_serec=rec.ide_serec  "
	    			+ "and resp.ide_seinf=rec.ide_seinf AND plan.ide_sepla = resp.ide_sepla  "
	    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf  "
	    			+ "and asi.ide_serec=rec.ide_serec   "
	    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp  "
	    			+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp  "
	    			+ "left join seg_empresa emp_plan on emp_plan.ide_seemp=plan.ide_seemp  "
	    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui  "
	    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr  "
	    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
	    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
	    			+ "LEFT JOIN seg_periocidad per on per.ide_seper=plan.ide_seper  "
	    			+ "LEFT JOIN seg_detalle_evaluacion deva on deva.ide_seinf=plan.ide_seinf   "
	    			+ "and deva.ide_serec=plan.ide_serec AND deva.ide_seeva IN (3,4,5) "
	    			+ "LEFT JOIN seg_estado_recomendacion estrec1 ON estrec1.ide_seesr =deva.ide_seesr  "
	    			+ "LEFT JOIN seg_usuario usuario_respuesta ON usuario_respuesta.ide_seusu = resp.ide_seusu  "
	    			+ "LEFT JOIN seg_usuario usuario_plan ON usuario_plan.ide_seemp = emp_plan.ide_seemp and usuario_plan.activo_seusu=true  "
	    			+ "LEFT JOIN seg_historial_mecanismo_control sehmc ON sehmc.ide_seres = resp.ide_seres  "
	    			+ "where resp.ide_sepla is not null  "
	    			+ "AND plan.IDE_SEREC IN  ("+ide_serec+","+ide_serec_fal+")   "
	    			+ "order by inf.numero_seinf asc, "
	    		+ "         plan.ide_sepla ASC, "
	    			+ "rec.numero_serec asc,  "
	    		+ "         resp.fecha_desde_seres asc, "
	    			+ "resp.fecha_hasta_seres asc";
	    	
	    	
	    
	    tab_seguimiento_reporte_general.setSql(sql);*/
    	
    	/*tab_seguimiento_reporte_general.setSql("select  inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, upper(resp.descripcion_seres) as descripcion_seres ,upper(resp.medio_verificacion_seres) as medio_verificacion_seres, "
    			+ "plan.ide_sepla, "
    			+ "resp.mecanismo_reporte_seres,mecanismo_reporte_seres_enlace,resp.fecha_desde_seres,resp.fecha_hasta_seres,  "
    			+ "tiprec.descripcion_setir,tresp.descripcion_setre,estrec1.descripcion_seesr,emp.descripcion_seemp, "
    			+ "usuario.nombre_seusu, per.descripcion_seper, "
    			+ "resp.fecha_seres, EXTRACT (YEAR FROM resp.fecha_seres)::INTEGER as year_seres, "
    			+ "plan.reprogramacion_total_sepla "
    			+ "from seg_plan_accion plan   "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_respuesta resp  on resp.ide_serec=rec.ide_serec and  resp.ide_seinf=rec.ide_seinf  "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=rec.ide_serec   "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp   "
    			+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp   "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui    "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr   "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
    			+ "LEFT JOIN seg_periocidad per on per.ide_seper=plan.ide_seper  "
    			+ "LEFT JOIN seg_detalle_evaluacion deva on deva.ide_seinf=plan.ide_seinf and deva.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_estado_recomendacion estrec1 ON estrec1.ide_seesr =deva.ide_seesr  "
    			+ "LEFT JOIN seg_usuario usuario ON usuario.ide_seusu = resp.ide_seusu "
    			+ "where resp.ide_sepla is not null and  "
    			+ "resp.activo_seres=true  "
    			+ "AND plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+") and resp.ide_sepla is not null "//and deva.ide_seeva=4 and deva.ide_seesr not in(2)  "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,  "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres ,resp.medio_verificacion_seres, plan.ide_sepla,"
    			+ "resp.mecanismo_reporte_seres,mecanismo_reporte_seres_enlace,resp.fecha_desde_seres,resp.fecha_hasta_seres,  "
    			+ "tiprec.descripcion_setir,tresp.descripcion_setre,estrec1.descripcion_seesr,emp.descripcion_seemp , usuario.nombre_seusu, per.descripcion_seper, resp.fecha_seres  "
    			+ "order by inf.numero_seinf asc,rec.numero_serec asc, plan.ide_sepla ASC, resp.fecha_seres ASC, resp.fecha_desde_seres asc,resp.fecha_hasta_seres asc");
    	*/
      	
      	
   
      	
    	tab_seguimiento_reporte_general.imprimirSql();
    	
    	//tab_seguimiento_reporte_general.setId("tab_seguimiento_reporte_general");
    	tab_seguimiento_reporte_general.setSql(ser_general.getSeguimientoPlan("1900-01-01","1900-01-01"));
    	
    	tab_seguimiento_reporte_general.setCampoPrimaria("ide_seinf");
    	tab_seguimiento_reporte_general.getColumna("ide_seinf").setLongitud(20);
    	tab_seguimiento_reporte_general.getColumna("ide_seinf").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("ide_seinf").setOrden(1);

    	
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setNombreVisual("SUSCRIBE");
    	tab_seguimiento_reporte_general.getColumna("descripcion_sesui").setOrden(2);

    	
      	tab_seguimiento_reporte_general.getColumna("numero_seinf").setFiltroContenido();
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").setLongitud(50);
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").setNombreVisual("NUM.INF");
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("numero_seinf").setOrden(3);

    	
      	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setNombreVisual("FEC. APROB");
    	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_aprobacion_seinf").alinearCentro();
    	
    	
    	
      	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").setNombreVisual("FEC. INI");
    	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_inicio_seinf").alinearCentro();
    	
    	
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
    	tab_seguimiento_reporte_general.getColumna("descripcion_seres").setNombreVisual("ACCION");

    	
     	tab_seguimiento_reporte_general.getColumna("medio_verificacion_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("medio_verificacion_seres").setLongitud(55);
    	tab_seguimiento_reporte_general.getColumna("medio_verificacion_seres").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("medio_verificacion_seres").setNombreVisual("MEDIO VERIFICACION");
    	
    	
     	tab_seguimiento_reporte_general.getColumna("ide_sepla").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("ide_sepla").setLongitud(10);
    	tab_seguimiento_reporte_general.getColumna("ide_sepla").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("ide_sepla").setNombreVisual("PLAN DE ACCION");
    	
    	
    	tab_seguimiento_reporte_general.getColumna("ide_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("ide_seres").setLongitud(10);
    	tab_seguimiento_reporte_general.getColumna("ide_seres").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("ide_seres").setNombreVisual("COD RESP");
    	tab_seguimiento_reporte_general.getColumna("ide_seres").setVisible(false);

    	
    	
     	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres").setLongitud(55);
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres").setUpload("respuesta");
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres").setNombreVisual("DOC ADJ");
    	
     	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres_enlace").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres_enlace").setLongitud(55);
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres_enlace").alinearCentro();
    	tab_seguimiento_reporte_general.getColumna("mecanismo_reporte_seres_enlace").setNombreVisual("LINK DE DESCARGA");

    	
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_desde_seres").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_desde_seres").setNombreVisual("FEC_DESDE_RESP");
    	tab_seguimiento_reporte_general.getColumna("fecha_desde_seres").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_desde_seres").alinearCentro();
    	
    	
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_hasta_seres").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_hasta_seres").setNombreVisual("FEC_HASTA_RESP");
    	tab_seguimiento_reporte_general.getColumna("fecha_hasta_seres").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_hasta_seres").alinearCentro();
    	
    	    	
    	
      	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setNombreVisual("TIPO.RECO");
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").setLongitud(50);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setir").alinearCentro();
    	
    	
    	
      	tab_seguimiento_reporte_general.getColumna("descripcion_setre").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setre").setNombreVisual("EST_RECOMENDACION_EMGIRS");
    	tab_seguimiento_reporte_general.getColumna("descripcion_setre").setLongitud(50);
    	tab_seguimiento_reporte_general.getColumna("descripcion_setre").alinearCentro();

    	
    	
      	/*tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setNombreVisual("EST_RECOMENDACION_CGE");
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").setLongitud(70);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seesr").alinearCentro();*/
    	
    	
    	tab_seguimiento_reporte_general.getColumna("area").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("area").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("area").setNombreVisual("AREA RESPONSABLE");
    	tab_seguimiento_reporte_general.getColumna("area").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("nombre_usuario").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("nombre_usuario").setLongitud(50);
    	tab_seguimiento_reporte_general.getColumna("nombre_usuario").setNombreVisual("USUARIO");
    	tab_seguimiento_reporte_general.getColumna("nombre_usuario").alinearCentro();
    	
    	
    	tab_seguimiento_reporte_general.getColumna("descripcion_seper").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seper").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("descripcion_seper").setNombreVisual("PERIOCIDAD");
    	tab_seguimiento_reporte_general.getColumna("descripcion_seper").alinearCentro();
    	
    	/*tab_seguimiento_reporte_general.getColumna("fecha_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").setNombreVisual("FECHA DEL REGISTRO");
    	tab_seguimiento_reporte_general.getColumna("fecha_seres").alinearCentro();*/
    	
    	tab_seguimiento_reporte_general.getColumna("year_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("year_seres").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("year_seres").setNombreVisual("AÑO DEL REGISTRO");
    	tab_seguimiento_reporte_general.getColumna("year_seres").alinearCentro();
    	
       	tab_seguimiento_reporte_general.getColumna("reprogramacion_total_sepla").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("reprogramacion_total_sepla").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("reprogramacion_total_sepla").setNombreVisual("REPROGRAMACION");
    	tab_seguimiento_reporte_general.getColumna("reprogramacion_total_sepla").setVisible(false);
    	
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").setNombreVisual("FECHA REGISTRO");
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_medio_control").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("fecha_medio_control").setNombreVisual("FECHA INGRE MEDIOS CONTROL");
    	tab_seguimiento_reporte_general.getColumna("fecha_medio_control").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("fecha_medio_control").alinearCentro();
    	
       	
    	tab_seguimiento_reporte_general.getColumna("mes_mecanismo_control").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("mes_mecanismo_control").setNombreVisual("MES CONTROL");
    	tab_seguimiento_reporte_general.getColumna("mes_mecanismo_control").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("mes_mecanismo_control").alinearCentro();
    	
    	
    	
    	tab_seguimiento_reporte_general.getColumna("anio_mecanismo_control").setFiltro(true);
      	tab_seguimiento_reporte_general.getColumna("anio_mecanismo_control").setNombreVisual("ANIO CONTROL");
    	tab_seguimiento_reporte_general.getColumna("anio_mecanismo_control").setLongitud(25);
    	tab_seguimiento_reporte_general.getColumna("anio_mecanismo_control").alinearCentro();
    	/*tab_seguimiento_reporte_general.getColumna("fecha_registro").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").setNombreVisual("FECHA REGISTRO");
    	tab_seguimiento_reporte_general.getColumna("fecha_registro").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("fecha_ingreso").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("fecha_ingreso").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("fecha_ingreso").setNombreVisual("fecha ingreso");
    	tab_seguimiento_reporte_general.getColumna("fecha_ingreso").alinearCentro();
    	
    	tab_seguimiento_reporte_general.getColumna("mes_registro_seres").setFiltro(true);
    	tab_seguimiento_reporte_general.getColumna("mes_registro_seres").setLongitud(40);
    	tab_seguimiento_reporte_general.getColumna("mes_registro_seres").setNombreVisual("MES REGISTRO");
    	tab_seguimiento_reporte_general.getColumna("mes_registro_seres").alinearCentro();*/
    	
    	
    	tab_seguimiento_reporte_general.setLectura(true);
    	tab_seguimiento_reporte_general.setNumeroTabla(1);				
    	tab_seguimiento_reporte_general.setRows(20);
    	
   		tab_seguimiento_reporte_general.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("REPORTE GENERAL SEGUIMIENTO PLAN DE ACCION ");
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
		return;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void eliminar() {
		return;
		// TODO Auto-generated method stub
		
	}

	public void cambiarFecha(){
		
    	/*tab_seguimiento_reporte_general.setSql("select  "
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
    			+ "where resp.ide_sepla is not null and plan.activo_sepla=true "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
    			+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu "
    			+ "ORDER BY  inf.ide_seinf");/*/
    	
    	
    	tab_seguimiento_reporte_general.setSql("select  inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf, "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres ,resp.medio_verificacion_seres,resp.mecanismo_reporte_seres,resp.mecanismo_reporte_seres_enlace,resp.fecha_desde_seres,resp.fecha_hasta_seres,  "
    			+ "tiprec.descripcion_setir,tresp.descripcion_setre,estrec1.descripcion_seesr,emp.descripcion_seemp ,per.descripcion_seper "
    			+ "from seg_plan_accion plan   "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_respuesta resp  on resp.ide_serec=rec.ide_serec and  resp.ide_seinf=rec.ide_seinf  "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=rec.ide_serec   "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp   "
    			+ "left join seg_empresa emp on emp.ide_seemp=plan.ide_seemp   "
    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui    "
    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr   "
    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
    			+ "LEFT JOIN seg_periocidad per on per.ide_seper=plan.ide_seper  "
    			+ "LEFT JOIN seg_detalle_evaluacion deva on deva.ide_seinf=plan.ide_seinf and deva.ide_serec=plan.ide_serec  "
    			+ "LEFT JOIN seg_estado_recomendacion estrec1 ON estrec1.ide_seesr =deva.ide_seesr  "
    			+ "where resp.ide_sepla is not null and plan.activo_sepla=true and  "
    			+ "resp.activo_seres=true  "
    			+ "AND plan.IDE_SEREC IN(SELECT ide_serec  "
    			+ "FROM seg_detalle_evaluacion   "
    			+ "where ide_seeva in(4) and ide_seesr not in(2) ) and deva.ide_seeva=4 and deva.ide_seesr not in(2)  "
    			+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,  "
    			+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres ,resp.medio_verificacion_seres,resp.mecanismo_reporte_seres,resp.fecha_desde_seres,resp.fecha_hasta_seres,  "
    			+ "tiprec.descripcion_setir,tresp.descripcion_setre,estrec1.descripcion_seesr,emp.descripcion_seemp ,per.descripcion_seper  "
    			+ "order by inf.numero_seinf asc,rec.numero_serec asc,resp.fecha_desde_seres asc,resp.fecha_hasta_seres asc");

    	
    	
    	
    	
    	
    	
    	
 	tab_seguimiento_reporte_general.ejecutarSql();
   	
		
		
	}
	public void limpiar() {
		tab_seguimiento_reporte_general.limpiar();	
		utilitario.addUpdate("tab_seguimiento_reporte_general");// limpia y refresca el autocompletar
	}

	
public void actualiza_datos_reporte(){
	  utilitario.getConexion().ejecutarSql(" UPDATE   seg_respuesta "
	  		+ "SET mecanismo_reporte_seres_enlace = null");
	
	  utilitario.getConexion().ejecutarSql("UPDATE   seg_respuesta "
	  		+ "SET mecanismo_reporte_seres_enlace =  'http://erp.emgirs.gob.ec:8080/files_erp/' || mecanismo_reporte_seres");
	
}


public void cargaEjecucion(){
	
	String fecha_inicial=cal_fecha_inicial.getFecha();
	String fecha_final=cal_fecha_final.getFecha();
	
	cambiarEstado();
	
	tab_seguimiento_reporte_general.setSql(ser_general.getSeguimientoPlan(fecha_inicial,fecha_final));
	tab_seguimiento_reporte_general.ejecutarSql();
	utilitario.addUpdate("tab_seguimiento_reporte_general");
	
	for (int i=0;i<tab_seguimiento_reporte_general.getTotalFilas();i++)
	{	
		/*if(pckUtilidades.CConversion.CDbl_2(tab_seguimiento_reporte_general.getValor(i,"saldo_por_devengar")) <0)
		{
			utilitario.agregarNotificacionInfo("MENSAJE - IMPORTANTE","LA EJECUCIÓN PRESUPUESTARIA ESTA DESCUADRADA, REVISAR DEVENGADOS");
			break;
		}*/	
	}

}
	
	
public void cambiarEstado(){
	TablaGenerica tab_ = utilitario.consultar("SELECT "
			+ "ide_seres,	COUNT (ide_seres) "
			+ "FROM 	seg_historial_mecanismo_control "
			+ "GROUP BY 	ide_seres HAVING COUNT (ide_seres) > 1");
	
	for (int i = 0; i < tab_.getTotalFilas(); i++) {
		TablaGenerica tab = utilitario.consultar("select ide_sehmc,ide_seres,fecha_sehmc,hora_sehmc from  "
				+ "seg_historial_mecanismo_control "
				+ "where ide_seres="+tab_.getValor(i,"ide_seres")
				+ " order by ide_seres asc,fecha_sehmc asc,hora_sehmc asc");
		
		for (int j = 0; j < tab.getTotalFilas(); j++) {
			
			if (tab.getTotalFilas()-1==j) {
				
			}else {
				utilitario.getConexion().ejecutarSql("update seg_historial_mecanismo_control set activo_sehmc=false where ide_sehmc=" + tab.getValor(j,"ide_sehmc"));
			}

		}
	}
}
	
	
}
