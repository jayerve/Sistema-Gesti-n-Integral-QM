package paq_juridico;

import javax.ejb.EJB;

import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_rpt_titulos_credito extends Pantalla{
	
	private Tabla tab_titulosRpt = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	
	public pre_rpt_titulos_credito(){

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
		bot_actualiza.setMetodo("cargaReporte");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		
		tab_titulosRpt.setId("tab_titulosRpt");
		tab_titulosRpt.setSql(ser_tesoreria.getSqlTitulosCredito("1900-01-01","1900-01-01"));
		tab_titulosRpt.getColumna("nro_titulo_facon").setFiltroContenido();
		tab_titulosRpt.getColumna("razon_social_recli").setFiltroContenido();
		tab_titulosRpt.getColumna("ruc_comercial_recli").setFiltroContenido();
		tab_titulosRpt.getColumna("estado").setLongitud(10);
		tab_titulosRpt.setColumnaSuma("valor_facturas,valor_interes,valor_titulo");
		tab_titulosRpt.setRows(30);
		tab_titulosRpt.setLectura(true);
		tab_titulosRpt.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_titulosRpt);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
			
	}
	
	public void cargaReporte(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_titulosRpt.setSql(ser_tesoreria.getSqlTitulosCredito(fecha_inicial,fecha_final));
		tab_titulosRpt.ejecutarSql();
		utilitario.addUpdate("tab_titulosRpt");
	}
	
	public void limpiar(){
		
		tab_titulosRpt.limpiar();
		
		tab_titulosRpt.setSql(ser_tesoreria.getSqlTitulosCredito("1900-01-01","1900-01-01"));
		tab_titulosRpt.ejecutarSql();
		utilitario.addUpdate("tab_titulosRpt");
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

	public Tabla getTab_titulosRpt() {
		return tab_titulosRpt;
	}

	public void setTab_titulosRpt(Tabla tab_titulosRpt) {
		this.tab_titulosRpt = tab_titulosRpt;
	}


}
