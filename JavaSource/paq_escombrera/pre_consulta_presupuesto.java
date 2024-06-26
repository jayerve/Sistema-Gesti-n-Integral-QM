
package paq_escombrera;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_escombrera.ejb.ServicioEscombrera;
import paq_sistema.aplicacion.Pantalla;

public class pre_consulta_presupuesto extends Pantalla{
	
	
	private Tabla tab_presupuesto_consulta = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

	@EJB
	private ServicioEscombrera ser_escombrera = (ServicioEscombrera) utilitario.instanciarEJB(ServicioEscombrera.class);
	
	public pre_consulta_presupuesto(){
		
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
		
		tab_presupuesto_consulta.setId("tab_presupuesto_consulta");
		tab_presupuesto_consulta.setSql(ser_escombrera.consultaPresupuesto("1900-01-01","1900-01-01"));
		tab_presupuesto_consulta.getColumna("descripcion_appry").setFiltroContenido();
		tab_presupuesto_consulta.getColumna("capitulo").setFiltroContenido();
		tab_presupuesto_consulta.getColumna("sub_capitulo").setFiltroContenido();
		tab_presupuesto_consulta.setLectura(true);
		tab_presupuesto_consulta.setRows(20);
		tab_presupuesto_consulta.dibujar();
		

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_presupuesto_consulta);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}
	
	public void cargaReporte(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_presupuesto_consulta.setSql(ser_escombrera.consultaPresupuesto(fecha_inicial,fecha_final));
		tab_presupuesto_consulta.ejecutarSql();
		utilitario.addUpdate("tab_presupuesto_consulta");
	}
	
	public void limpiar(){
		
		tab_presupuesto_consulta.limpiar();
		
		tab_presupuesto_consulta.setSql(ser_escombrera.consultaPresupuesto("1900-01-01","1900-01-01"));
		tab_presupuesto_consulta.ejecutarSql();
		utilitario.addUpdate("tab_presupuesto_consulta");
	}
	

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_presupuesto_consulta() {
		return tab_presupuesto_consulta;
	}

	public void setTab_presupuesto_consulta(Tabla tab_presupuesto_consulta) {
		this.tab_presupuesto_consulta = tab_presupuesto_consulta;
	}

	

	
}