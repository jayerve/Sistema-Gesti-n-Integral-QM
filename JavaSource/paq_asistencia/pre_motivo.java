/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_motivo extends Pantalla {


	private Tabla tab_tabla = new Tabla();

	public pre_motivo() {        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("ASI_MOTIVO", "IDE_ASMOT", 1);
 		tab_tabla.getColumna("IDE_ASGRM").setCombo("ASI_GRUPO_MOTIVO", "IDE_ASGRM", "DETALLE_ASGRM", "");

		tab_tabla.getColumna("IDE_GETPR").setCombo("GEN_TIPO_PERIODO", "IDE_GETPR", "DETALLE_GETPR", "");
		tab_tabla.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","");
		tab_tabla.getColumna("APLICA_VACACIONES_ASMOT").setCheck();
		tab_tabla.getColumna("ACTIVO_ASMOT").setCheck();
		tab_tabla.getColumna("ACTIVO_ASMOT").setValorDefecto("true");
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
