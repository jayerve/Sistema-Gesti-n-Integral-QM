/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_recursos extends Pantalla {

    private Tabla tab_recursos = new Tabla();

    public pre_ind_recursos() {
    	tab_recursos.setId("tab_recursos");
    	tab_recursos.setNumeroTabla(1);
    	
    	tab_recursos.setTabla("ind_recursos", "ind_inrec", 1); 	
    	tab_recursos.getColumna("ind_inrec").setNombreVisual("CODIGO");
    	tab_recursos.getColumna("detalle_inrec").setNombreVisual("DETALLE");
    	tab_recursos.getColumna("ACTIVO_inrec").setNombreVisual("ACTIVO");
    	tab_recursos.getColumna("ACTIVO_inrec").setCheck();
    	tab_recursos.getColumna("ACTIVO_inrec").setValorDefecto("TRUE");
    	
    	
    	tab_recursos.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_recursos);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_recursos.insertar();
    }

    @Override
    public void guardar() {
        if (tab_recursos.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_recursos.eliminar();
    }

	public Tabla getTab_recursos() {
		return tab_recursos;
	}

	public void setTab_recursos(Tabla tab_recursos) {
		this.tab_recursos = tab_recursos;
	}




}
