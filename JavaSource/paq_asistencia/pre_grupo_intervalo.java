/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_grupo_intervalo extends Pantalla {


	private Tabla tab_tabla = new Tabla();

	public pre_grupo_intervalo() {        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("ASI_GRUPO_INTERVALO", "IDE_ASGRI", 1);
		tab_tabla.getColumna("ACTIVO_ASGRI").setCheck();
		tab_tabla.getColumna("ACTIVO_ASGRI").setValorDefecto("true");
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
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