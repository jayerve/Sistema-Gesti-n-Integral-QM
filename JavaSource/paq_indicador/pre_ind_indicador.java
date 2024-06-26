/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_indicador extends Pantalla {

    private Tabla tab_indicador = new Tabla();

    public pre_ind_indicador() {
    	tab_indicador.setId("tab_indicador");
    	tab_indicador.setNumeroTabla(1);
    	
    	tab_indicador.setTabla("ind_indicador", "ide_inind", 1);
    	
    	tab_indicador.getColumna("ide_inind").setNombreVisual("CODIGO");
    	tab_indicador.getColumna("detalle_inind").setNombreVisual("DETALLE");
    	tab_indicador.getColumna("ACTIVO_inind").setNombreVisual("ACTIVO");
    	tab_indicador.getColumna("ACTIVO_inind").setCheck();
    	tab_indicador.getColumna("ACTIVO_inind").setValorDefecto("TRUE");
    	
    	
    	tab_indicador.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_indicador);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_indicador.insertar();
    }

    @Override
    public void guardar() {
        if (tab_indicador.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_indicador.eliminar();
    }

	public Tabla getTab_indicador() {
		return tab_indicador;
	}

	public void setTab_indicador(Tabla tab_indicador) {
		this.tab_indicador = tab_indicador;
	}




}
