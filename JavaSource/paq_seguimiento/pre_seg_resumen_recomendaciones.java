/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.math.BigDecimal;
import java.math.RoundingMode;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */

public class pre_seg_resumen_recomendaciones extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();
    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    private String mes="",anio="";

    public pre_seg_resumen_recomendaciones() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
	    mes=utilitario.getVariable("p_seguimiento_mes");
		anio=utilitario.getVariable("p_seguimiento_anio");
		bar_botones.agregarComponente(new Etiqueta("Escoja una opcion: "));
		Boton bot_limpiar = new Boton();
		bot_limpiar.setValue("Generar Reporte");
		bot_limpiar.setIcon("ui-icon-document");
		bot_limpiar.setMetodo("abrirMes");
		bar_botones.agregarBoton(bot_limpiar);
		
		
		Boton bot_historicos = new Boton();
		bot_historicos.setValue("Ver Reporte Historicos");
		bot_historicos.setIcon("ui-icon-document");
		bot_historicos.setMetodo("generarReporteHistoricos");
		bar_botones.agregarBoton(bot_historicos);
		
		
		Boton bot_consultar = new Boton();
		bot_consultar.setValue("Consultar Estadisticos");
		bot_consultar.setIcon("ui-icon-document");
		bot_consultar.setMetodo("consultarReporte");
		//bar_botones.agregarBoton(bot_consultar);
    	
		
		
    	sel_mes.setId("sel_mes");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+mes+") ","IDE_GEMES");
    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("50");
		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.getBot_aceptar().setMetodo("obtenerMes");
		agregarComponente(sel_mes);
		
		
	    	sel_anio.setId("sel_anio");
			sel_anio.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+") ","IDE_GEANI");
			sel_anio.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
			sel_anio.setRadio();
	    	sel_anio.setTitle("Seleccione Anio Horario");
			gru_pantalla.getChildren().add(sel_anio);
			sel_anio.setWidth("20");
			sel_anio.setHeight("20");
			sel_anio.getBot_aceptar().setMetodo("obtenerAnio");
			agregarComponente(sel_anio);
	    	
		
    	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	
    	tab_permisos_reporte.setSql("SELECT rrec.nro_informes_serer, sepla.descripcion_seesp, rrec.nro_recomendacion_serer,  "
    			+ "rrec.porcentaje_serer, mes.detalle_gemes, anio.detalle_geani  "
    			+ "FROM seg_resumen_recomendaciones  rrec "
    			+ "left join seg_estado_plan_accion sepla  on rrec.ide_seesp=sepla.ide_seesp "
    			+ "left join gen_anio anio  on rrec.ide_geani=anio.ide_geani "
    			+ "left join gen_mes mes  on rrec.ide_gemes=mes.ide_gemes "
    			+ "group by rrec.ide_serer, rrec.nro_informes_serer, sepla.descripcion_seesp, rrec.nro_recomendacion_serer,  "
    			+ "rrec.porcentaje_serer, mes.detalle_gemes, anio.detalle_geani  "
    			+ "order by rrec.ide_serer ");

    	   	
    	tab_permisos_reporte.setCampoPrimaria("nro_informes_serer");
    	tab_permisos_reporte.getColumna("nro_informes_serer").setLongitud(120);
    	tab_permisos_reporte.getColumna("nro_informes_serer").alinearCentro();
    	tab_permisos_reporte.getColumna("nro_informes_serer").setNombreVisual("TOTAL INFORMES DE AI Y CGE");

    	
    	tab_permisos_reporte.getColumna("descripcion_seesp").setFiltroContenido();
    	tab_permisos_reporte.getColumna("descripcion_seesp").setLongitud(40);
    	tab_permisos_reporte.getColumna("descripcion_seesp").alinearCentro();
    	tab_permisos_reporte.getColumna("descripcion_seesp").setNombreVisual("ESTADO");
    	//tab_permisos_reporte.getColumna("descripcion_seesp").setSuma(true);

    	
    	tab_permisos_reporte.getColumna("nro_recomendacion_serer").setFiltroContenido();
    	tab_permisos_reporte.getColumna("nro_recomendacion_serer").setLongitud(10);
    	tab_permisos_reporte.getColumna("nro_recomendacion_serer").alinearCentro();
    	tab_permisos_reporte.getColumna("nro_recomendacion_serer").setNombreVisual("NRO.REC");
    	//tab_permisos_reporte.getColumna("nro_recomendacion_serer").setSuma(true);

    	
    	tab_permisos_reporte.getColumna("porcentaje_serer").setFiltroContenido();
    	tab_permisos_reporte.getColumna("porcentaje_serer").setLongitud(10);
    	tab_permisos_reporte.getColumna("porcentaje_serer").alinearCentro();
    	tab_permisos_reporte.getColumna("porcentaje_serer").setNombreVisual("PORCENTAJE");
    	//tab_permisos_reporte.setColumnaSuma("porcentaje_serer");

    	
    	tab_permisos_reporte.getColumna("detalle_gemes").setFiltroContenido();
    	tab_permisos_reporte.getColumna("detalle_gemes").setLongitud(10);
    	tab_permisos_reporte.getColumna("detalle_gemes").alinearCentro();
    	tab_permisos_reporte.getColumna("detalle_gemes").setNombreVisual("MES");
    	
    	
    	tab_permisos_reporte.getColumna("detalle_geani").setFiltroContenido();
    	tab_permisos_reporte.getColumna("detalle_geani").setLongitud(10);
    	tab_permisos_reporte.getColumna("detalle_geani").alinearCentro();
    	tab_permisos_reporte.getColumna("detalle_geani").setNombreVisual("ANIO");
    	
    	
    	
    	tab_permisos_reporte.setLectura(true);
    	tab_permisos_reporte.setNumeroTabla(1);				
    	tab_permisos_reporte.setRows(40);

    	
   		tab_permisos_reporte.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RESUMEN ESTADO DE RECOMENDACIONES");
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


	public void limpiar() {
		//tab_permisos_reporte.limpiar();	
		//utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
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
	
	
	
	public void abrirMes(){
		sel_mes.dibujar();
	}
	
	
	public void generarReporte(String mes_seguimiento,String anio_seguimiento){

		 String fecha_actual="";
		fecha_actual=utilitario.getFechaActual();
		TablaGenerica tab_anio=null;
		TablaGenerica tab_mes=null;

		
		tab_anio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_actual)+"%'");
		
		tab_mes=utilitario.consultar("select * from gen_mes where ide_gemes ="+(utilitario.getMes(fecha_actual))+"");		

		int anio=0,mes=0;
		int contadorInforme=0,contadorRecomendacionesAplicadas=0,contadorRecomendacionesEjecucion=0,contadorReprogramacion=0,contadorRecomendacionesIncumplida=0,total=0;
		double porcentajeAplicada=0,porcentajeEjecucion=0,porcentajeReprogramacion=0,porcentajeIncumplida=0;
		int ide_seespAplicada=0,ide_seespEjecucion=0,ide_seespReprogramacion=0,ide_seespIncumplida=0;
		
		
		//anio=Integer.parseInt(tab_anio.getValor("ide_geani"));
		//mes=Integer.parseInt(tab_mes.getValor("ide_gemes"));
		  utilitario.getConexion().ejecutarSql("delete from  seg_resumen_recomendaciones where ide_gemes=" + mes_seguimiento +"and ide_geani="+anio_seguimiento);
		
		  
		  TablaGenerica tab_recomendaciones_eva = utilitario.consultar("SELECT ide_serec,ide_seeva "
	    			+ "FROM seg_detalle_evaluacion  "
	    			+ "where ide_seeva in(4) and ide_seesr not in(2)");
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
			
			
	    	TablaGenerica tab_recomendaciones_fal =utilitario.consultar("select ide_serec,ide_seinf from seg_recomendacion where ide_seinf in("+utilitario.getVariable("p_informe_seguimiento_recomendacion")+")");
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
	    	
		  
		  
		  
		  
		  
		  
		TablaGenerica tabObtenerReporte=null;
	/*	tabObtenerReporte=utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad  "
				+ "from seg_plan_accion plan  "
				+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
				+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
				+ "where plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN(SELECT ide_serec   "
				+ "FROM seg_detalle_evaluacion   "
				+ "where ide_seeva in(4) and ide_seesr not in(2))  "
				+ "group by inf.ide_seinf,inf.numero_seinf  "
				+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");*/
		
		tabObtenerReporte=utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad  "
				+ "from seg_plan_accion plan  "
				+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
				+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
				+ "where plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+")  "
				+ "group by inf.ide_seinf,inf.numero_seinf  "
				+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc");
		
		
		contadorInforme=tabObtenerReporte.getTotalFilas();
		
		
			//APLICADAS   
		/*TablaGenerica tabObtenerReporteAplicadas= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
					+ "from seg_plan_accion plan  "
					+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
					+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
					+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
				+ "where  plan.ide_seesp=1 and plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN(SELECT ide_serec  "
				+ "FROM seg_detalle_evaluacion  "
				+ "where ide_seeva in(4) and ide_seesr not in(2))	"
					+ "group by inf.ide_seinf,inf.numero_seinf   "
				+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc"); 
	*/
		TablaGenerica tabObtenerReporteAplicadas= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
					+ "from seg_plan_accion plan  "
					+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
					+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
					+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
				+ "where  plan.ide_seesp=1 and plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+")	"
					+ "group by inf.ide_seinf,inf.numero_seinf   "
					+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");
		
		
		if (tabObtenerReporteAplicadas.getTotalFilas()>0) {
			for (int i = 0; i < tabObtenerReporteAplicadas.getTotalFilas(); i++) {
				contadorRecomendacionesAplicadas=contadorRecomendacionesAplicadas+Integer.parseInt(tabObtenerReporteAplicadas.getValor(i,"cantidad"));
			
			}
		}else {
			contadorRecomendacionesAplicadas=0;
		}
		
		ide_seespAplicada=1;
		
		
			
			//EN EJECUCION  
		
		/*TablaGenerica tabObtenerReporteEjecucion= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
					+ "from seg_plan_accion plan  "
					+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
					+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
					+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
		+ "where  plan.ide_seesp=2 and plan.activo_sepla=true "
		+ "and plan.IDE_SEREC IN(SELECT ide_serec  "
		+ "FROM seg_detalle_evaluacion  "
		+ "where ide_seeva in(4) and ide_seesr not in(2))	"
					+ "group by inf.ide_seinf,inf.numero_seinf   "
					+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");*/
			
		
		TablaGenerica tabObtenerReporteEjecucion= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
					+ "from seg_plan_accion plan  "
					+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
					+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
					+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
		+ "where  plan.ide_seesp=2 and plan.activo_sepla=true "
		+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+")	"
					+ "group by inf.ide_seinf,inf.numero_seinf   "
					+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");
			
		
		if (tabObtenerReporteEjecucion.getTotalFilas()>0) {

		for (int i = 0; i < tabObtenerReporteEjecucion.getTotalFilas(); i++) {
			contadorRecomendacionesEjecucion=contadorRecomendacionesEjecucion+Integer.parseInt(tabObtenerReporteEjecucion.getValor(i,"cantidad"));
		
		}
		}else {
			contadorRecomendacionesEjecucion=0;
		}
		ide_seespEjecucion=2;

		
		// 4 EN REPROGRAMACION
	
	/*TablaGenerica tabObtenerReporteReprogramacion= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
				+ "from seg_plan_accion plan  "
				+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
				+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
				+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
				+ "where  plan.ide_seesp=4 and plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN(SELECT ide_serec  "
				+ "FROM seg_detalle_evaluacion  "
				+ "where ide_seeva in(4) and ide_seesr not in(2))	"
				+ "group by inf.ide_seinf,inf.numero_seinf   "
				+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");*/
		
	TablaGenerica tabObtenerReporteReprogramacion= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
				+ "from seg_plan_accion plan  "
				+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
				+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
				+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
				+ "where  plan.ide_seesp=4 and plan.activo_sepla=true "
				+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+")	"
				+ "group by inf.ide_seinf,inf.numero_seinf   "
				+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");
	
		
	if (tabObtenerReporteReprogramacion.getTotalFilas()>0) {

	for (int i = 0; i < tabObtenerReporteReprogramacion.getTotalFilas(); i++) {
		contadorReprogramacion=contadorReprogramacion+Integer.parseInt(tabObtenerReporteReprogramacion.getValor(i,"cantidad"));
		}
	}else {
		contadorReprogramacion=0;
	}
	ide_seespReprogramacion=4;
	
	// 5 INCUMPLIDA

/*TablaGenerica tabObtenerReporteIncumplida= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
			+ "from seg_plan_accion plan  "
			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
			+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
		+ "where  plan.ide_seesp=5 and plan.activo_sepla=true "
		+ "and plan.IDE_SEREC IN(SELECT ide_serec  "
		+ "FROM seg_detalle_evaluacion  "
		+ "where ide_seeva in(4) and ide_seesr not in(2) )	"
			+ "group by inf.ide_seinf,inf.numero_seinf   "
			+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");*/

TablaGenerica tabObtenerReporteIncumplida= utilitario.consultar("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad   "
			+ "from seg_plan_accion plan  "
			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf  "
			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec  "
			+ "left join seg_estado_plan_accion esp on esp.ide_seesp=plan.ide_seesp  "
		+ "where  plan.ide_seesp=5 and plan.activo_sepla=true "
			+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+")	"
			+ "group by inf.ide_seinf,inf.numero_seinf   "
			+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc ");
	
	
	
if (tabObtenerReporteIncumplida.getTotalFilas()>0) {

for (int i = 0; i < tabObtenerReporteIncumplida.getTotalFilas(); i++) {
	contadorRecomendacionesIncumplida=contadorRecomendacionesIncumplida+Integer.parseInt(tabObtenerReporteIncumplida.getValor(i,"cantidad"));

}
}else {
	contadorRecomendacionesIncumplida=0;
}

ide_seespIncumplida=5;
	
	
	total=contadorRecomendacionesAplicadas+contadorRecomendacionesEjecucion+contadorRecomendacionesIncumplida+contadorReprogramacion;
	porcentajeAplicada=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(""+contadorRecomendacionesAplicadas)/total)*100),2));
	//BigDecimal big_cuota=new BigDecimal(porcentajeAplicada);
	//big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);
	porcentajeEjecucion=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(""+contadorRecomendacionesEjecucion)*100)/total),2));
	//porcentajeReprogramacion=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(""+contadorReprogramacion)*100)/total),2));
	porcentajeIncumplida=Double.parseDouble(utilitario.getFormatoNumero(((Double.parseDouble(""+contadorRecomendacionesIncumplida)*100)/total),2));

	
	insertarTablaResumenRecomendaciones(contadorInforme, ide_seespAplicada, contadorRecomendacionesAplicadas, porcentajeAplicada, Integer.parseInt(mes_seguimiento), Integer.parseInt(anio_seguimiento), utilitario.getFechaActual(), true);
	insertarTablaResumenRecomendaciones(contadorInforme, ide_seespEjecucion, contadorRecomendacionesEjecucion, porcentajeEjecucion, Integer.parseInt(mes_seguimiento), Integer.parseInt(anio_seguimiento), utilitario.getFechaActual(), true);
	//insertarTablaResumenRecomendaciones(contadorInforme, ide_seespReprogramacion, contadorReprogramacion, porcentajeReprogramacion, mes, anio, utilitario.getFechaActual(), true);
	insertarTablaResumenRecomendaciones(contadorInforme, ide_seespIncumplida, contadorRecomendacionesIncumplida, porcentajeIncumplida, Integer.parseInt(mes_seguimiento), Integer.parseInt(anio_seguimiento), utilitario.getFechaActual(), true);

	tab_permisos_reporte.actualizar();
	utilitario.agregarMensaje("Se ha generado el reporte Mensual del mes de "+tab_mes.getValor("detalle_gemes")+" anio: "+tab_anio.getValor("detalle_geani"), "");
	utilitario.addUpdate("sel_mes,sel_anio");
	
	}
	
	

public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}

	
	

public void insertarTablaResumenRecomendaciones(
		 int nro_informes_serer,
		 int ide_seesp,
		 int nro_recomendacion_serer,
		 double porcentaje_serer,
		 int ide_gemes,
		 int ide_geani,
		 String fecha_seres,
		 boolean activo_seres


		 
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_resumen_recomendaciones", "ide_serer"));
		String codigo=tab_codigo.getValor("codigo");
		

		utilitario.getConexion().ejecutarSql("INSERT INTO seg_resumen_recomendaciones(" 
					+ "ide_serer, "
					+ "nro_informes_serer, "
					+ "ide_seesp , "
			  		+ "nro_recomendacion_serer, "
			  		+ "porcentaje_serer, "
			  		+ "ide_gemes, "
			  		+ "ide_geani, "
			  		+ "fecha_seres, "
			  		+ "activo_seres ) "  
			  		+" values( " +codigo + ", "
			  		+ ""+ nro_informes_serer+", "
			  		+ ""+ide_seesp+", "
			  		+ ""+nro_recomendacion_serer+", "
			  		+ ""+porcentaje_serer+", "
			  		+ ""+ide_gemes+", "
			  		+ ""+ide_geani+", "
			  		+ "'"+fecha_seres+"', "
	    	  		+ ""+activo_seres+")"); 
	 
		

}
	

public void generarReporteHistoricos(){
	 
	tab_permisos_reporte.setCondicion("ide_gemes in (select ide_gemes from gen_mes) ");
	tab_permisos_reporte.ejecutarSql();
	tab_permisos_reporte.actualizar();
}



public SeleccionTabla getSel_mes() {
	return sel_mes;
}



public void setSel_mes(SeleccionTabla sel_mes) {
	this.sel_mes = sel_mes;
}



public SeleccionTabla getSel_anio() {
	return sel_anio;
}



public void setSel_anio(SeleccionTabla sel_anio) {
	this.sel_anio = sel_anio;
}



public void obtenerMes(){
				  mes=sel_mes.getValorSeleccionado();
		  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
	 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
	 			return;
	 		}else {
	 			
				  mes=sel_mes.getValorSeleccionado();
System.out.println("obtenerMes: "+mes);
	 			
		  sel_mes.cerrar();	
	      sel_anio.dibujar();
	 		}
	
}


public void obtenerAnio(){

     anio=sel_anio.getValorSeleccionado();
     if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
 			return;
 		}else {
	   	sel_anio.cerrar();
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
		String  anioEmpleado=tab_anio.getValor("ide_geani");    			
generarReporte(mes, anio);
		
}
     
}

}
