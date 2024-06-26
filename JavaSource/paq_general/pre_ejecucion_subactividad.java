package paq_general;

import javax.ejb.EJB;

import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_ejecucion_subactividad extends Pantalla{
	
	private Tabla tab_ejecucion = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);

	public pre_ejecucion_subactividad(){

		bar_botones.limpiar();
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setValue(utilitario.obtenerFechaFinAnio());
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaEjecucion");
		bar_botones.agregarBoton(bot_actualiza);

    			
		tab_ejecucion.setId("tab_ejecucion");
		tab_ejecucion.setSql(ser_general.getEjecucionActividadesPOA("1900-01-01","1900-01-01"));
		//tab_ejecucion.getColumna("ide_prpoa").setVisible(false);
		tab_ejecucion.getColumna("ide_prpoa").setFiltro(true);
		tab_ejecucion.getColumna("ide_prfup").setVisible(false);
		tab_ejecucion.getColumna("detalle_geani").setNombreVisual("AÑO");
		tab_ejecucion.getColumna("detalle_geani").setLectura(true);
		tab_ejecucion.getColumna("detalle_programa").setNombreVisual("DETALLE PROGRAMA");
		tab_ejecucion.getColumna("detalle_programa").setLectura(true);
		tab_ejecucion.getColumna("detalle_proyecto").setNombreVisual("DETALLE PROYECTO");
		tab_ejecucion.getColumna("detalle_proyecto").setLectura(true);
		tab_ejecucion.getColumna("detalle_producto").setNombreVisual("DETALLE PRODUCTO");
		tab_ejecucion.getColumna("detalle_producto").setLectura(true);
		tab_ejecucion.getColumna("detalle_subactividad").setNombreVisual("DETALLE SUBACTIVIDAD");
		tab_ejecucion.getColumna("detalle_subactividad").setLectura(true);
		tab_ejecucion.getColumna("codigo_clasificador_prcla").setLectura(true);
		tab_ejecucion.getColumna("codigo_clasificador_prcla").setNombreVisual("CODIGO CLASIFICADOR");
		tab_ejecucion.getColumna("codigo_clasificador_prcla").setFiltro(true);

		tab_ejecucion.getColumna("descripcion_clasificador_prcla").setLectura(true);
		tab_ejecucion.getColumna("codigo_subactividad").setLectura(true);
		tab_ejecucion.getColumna("codigo_subactividad").setFiltro(true);
		tab_ejecucion.getColumna("presupuesto_inicial_prpoa").setLectura(true);
		tab_ejecucion.getColumna("reforma_prpoa").setLectura(true);
		tab_ejecucion.getColumna("presupuesto_codificado_prpoa").setLectura(true);
		tab_ejecucion.getColumna("certificado").setLectura(true);		
		tab_ejecucion.getColumna("comprometido").setLectura(true);	
		tab_ejecucion.getColumna("devengado").setLectura(true);	
		tab_ejecucion.setColumnaSuma("presupuesto_inicial_prpoa,reforma_prpoa,presupuesto_codificado_prpoa,certificado,comprometido,devengado");
		tab_ejecucion.setRows(30);
		tab_ejecucion.setLectura(true);
		tab_ejecucion.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_ejecucion);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
	}
	
	public void cargaEjecucion(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();

		tab_ejecucion.setSql(ser_general.getEjecucionActividadesPOA(fecha_inicial,fecha_final));
		tab_ejecucion.ejecutarSql();
		utilitario.addUpdate("tab_ejecucion");
		
	}
	
	public void limpiar(){
		
		tab_ejecucion.limpiar();
		
		tab_ejecucion.setSql(ser_general.getEjecucionActividadesPOA("1900-01-01","1900-01-01"));
		tab_ejecucion.ejecutarSql();
		utilitario.addUpdate("tab_ejecucion");
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

	public Tabla getTab_ejecucion() {
		return tab_ejecucion;
	}

	public void setTab_ejecucion(Tabla tab_ejecucion) {
		this.tab_ejecucion = tab_ejecucion;
	}


}
