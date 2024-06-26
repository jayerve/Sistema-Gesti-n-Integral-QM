/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_desarrolla extends Pantalla {

    private Tabla tab_desarrolla = new Tabla();

    public pre_ind_desarrolla() {
    	tab_desarrolla.setId("tab_desarrolla");
    	tab_desarrolla.setNumeroTabla(1);
    	
    	tab_desarrolla.setTabla("ind_desarrolla", "ind_indes", 1); 	
    	tab_desarrolla.getColumna("ind_indes").setNombreVisual("CODIGO");
    	tab_desarrolla.getColumna("detalle_indes").setNombreVisual("DETALLE");
    	tab_desarrolla.getColumna("ACTIVO_indes").setNombreVisual("ACTIVO");
    	tab_desarrolla.getColumna("ACTIVO_indes").setCheck();
    	tab_desarrolla.getColumna("ACTIVO_indes").setValorDefecto("TRUE");
    	
    	
    	tab_desarrolla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_desarrolla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_desarrolla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_desarrolla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_desarrolla.eliminar();
    }

	public Tabla getTab_desarrolla() {
		return tab_desarrolla;
	}

	public void setTab_desarrolla(Tabla tab_desarrolla) {
		this.tab_desarrolla = tab_desarrolla;
	}




}
