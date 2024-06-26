/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_plazo_facturas extends Pantalla {

	private Tabla tab_tabla = new Tabla();

	public pre_plazo_facturas() {        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("gen_plazo_facturas", "ide_geplf", 1);
		tab_tabla.getColumna("activo_geplf").setCheck();
		tab_tabla.getColumna("activo_geplf").setValorDefecto("true");
		tab_tabla.dibujar();

		PanelTabla pat_plazo_fac=new PanelTabla();
		pat_plazo_fac.setPanelTabla(tab_tabla);
		agregarComponente(pat_plazo_fac);
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		
		if (tab_tabla.guardar()){
			guardarPantalla();
		}
		
	}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}
}
