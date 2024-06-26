/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import javax.ejb.EJB;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import paq_sri.ejb.ServicioSRI;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author Alex
 */
public class pre_gastos_personales extends Pantalla {

	private Tabla tab_tabla = new Tabla();
	private Combo com_anio=new Combo();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSRI ser_sri = (ServicioSRI) utilitario.instanciarEJB(ServicioSRI.class);

	public pre_gastos_personales() {   
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(ser_sri.getGastosPersonales("-1"));

		tab_tabla.getColumna("mes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_tabla.setColumnaSuma("totalActivos,totalInacActivos");
		tab_tabla.setLectura(true);
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);	
	}

	public void seleccionaElAnio()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		tab_tabla.setSql(ser_sri.getGastosPersonales(com_anio.getValue()+""));
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_tabla");
		
	}

	@Override
	public void insertar() {

	}

	@Override
	public void guardar() {
		
	}

	@Override
	public void eliminar() {
	
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}



}
