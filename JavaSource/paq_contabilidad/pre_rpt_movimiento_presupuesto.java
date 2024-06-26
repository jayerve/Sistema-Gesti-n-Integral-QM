package paq_contabilidad;

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

public class pre_rpt_movimiento_presupuesto extends Pantalla{
	
	private Tabla tab_movVSpresupuesto = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);

	public pre_rpt_movimiento_presupuesto(){

		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargarDatos");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
    			
		tab_movVSpresupuesto.setId("tab_movVSpresupuesto");
		tab_movVSpresupuesto.setSql(ser_general.getRptMovimientoPresupuesto("1900-01-01","1900-01-01"));
		tab_movVSpresupuesto.setColumnaSuma("debe_213,haber_111,devengado,pagado,debe_113_81,haber_213_81,diferencia_pagado,diferencia_iva");

		tab_movVSpresupuesto.setRows(30);
		tab_movVSpresupuesto.setLectura(true);
		tab_movVSpresupuesto.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_movVSpresupuesto);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);

	}
	
	public void cargarDatos(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_movVSpresupuesto.setSql(ser_general.getRptMovimientoPresupuesto(fecha_inicial,fecha_final));
		tab_movVSpresupuesto.ejecutarSql();
		utilitario.addUpdate("tab_movVSpresupuesto");
	}
	
	public void limpiar(){
		
		tab_movVSpresupuesto.limpiar();
		
		tab_movVSpresupuesto.setSql(ser_general.getRptMovimientoPresupuesto("1900-01-01","1900-01-01"));
		tab_movVSpresupuesto.ejecutarSql();
		utilitario.addUpdate("tab_movVSpresupuesto");
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

	public Tabla getTab_movVSpresupuesto() {
		return tab_movVSpresupuesto;
	}

	public void setTab_movVSpresupuesto(Tabla tab_movVSpresupuesto) {
		this.tab_movVSpresupuesto = tab_movVSpresupuesto;
	}

	

}
