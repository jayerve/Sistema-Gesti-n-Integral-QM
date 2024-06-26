package paq_general;

import javax.ejb.EJB;

import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_ejecucion_presupuestaria_mensual extends Pantalla{
	
	private Tabla tab_ejecucionPmensual = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	
	public pre_ejecucion_presupuestaria_mensual(){

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
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
    			
		tab_ejecucionPmensual.setId("tab_ejecucionPmensual");
		tab_ejecucionPmensual.setSql(ser_general.getEjecucionPresupuestariaPlanificadaMensual("1900-01-01","1900-01-01"));

		tab_ejecucionPmensual.getColumna("detalle_geani").setNombreVisual("AÑO");
		tab_ejecucionPmensual.getColumna("detalle_geani").setLongitud(15);
		tab_ejecucionPmensual.getColumna("detalle_programa").setNombreVisual("PROGRAMA");
		tab_ejecucionPmensual.getColumna("detalle_proyecto").setNombreVisual("PROYECTO");
		tab_ejecucionPmensual.getColumna("detalle_programa").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("detalle_proyecto").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("detalle_subactividad").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("ide_prpoa").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("fuente").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("codigo_clasificador_prcla").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("area").setFiltroContenido();
		tab_ejecucionPmensual.getColumna("codigo_clasificador_prcla").setNombreVisual("PARTIDA");
		tab_ejecucionPmensual.getColumna("descripcion_clasificador_prcla").setNombreVisual("DESCRIPCION PARTIDA");
		tab_ejecucionPmensual.getColumna("presupuesto_codificado_prpoa").setNombreVisual("CODIFICADO");
		tab_ejecucionPmensual.getColumna("tipo_gestion").setFiltroContenido();
		
		tab_ejecucionPmensual.setColumnaSuma("presupuesto_codificado_prpoa,codificado_enero,devengado_enero,codificado_febrero,devengado_febrero,codificado_marzo,devengado_marzo,codificado_abril,devengado_abril,codificado_mayo,devengado_mayo");

		tab_ejecucionPmensual.setRows(30);
		tab_ejecucionPmensual.setLectura(true);
		tab_ejecucionPmensual.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_ejecucionPmensual);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		
	}
	
	public void cargaEjecucion(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_ejecucionPmensual.setSql(ser_general.getEjecucionPresupuestariaPlanificadaMensual(fecha_inicial,fecha_final));
		tab_ejecucionPmensual.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionPmensual");
	}
	
	public void limpiar(){
		
		tab_ejecucionPmensual.limpiar();
		
		tab_ejecucionPmensual.setSql(ser_general.getEjecucionPresupuestariaPlanificadaMensual("1900-01-01","1900-01-01"));
		tab_ejecucionPmensual.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionPmensual");
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

	public Tabla getTab_ejecucionPmensual() {
		return tab_ejecucionPmensual;
	}

	public void setTab_ejecucionPmensual(Tabla tab_ejecucionPmensual) {
		this.tab_ejecucionPmensual = tab_ejecucionPmensual;
	}

	

	

}
