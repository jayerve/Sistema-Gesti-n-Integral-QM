/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_objetivo extends Pantalla {

    private Tabla tab_objetivo = new Tabla();

    public pre_ind_objetivo() {
    	tab_objetivo.setId("tab_objetivo");
    	tab_objetivo.setNumeroTabla(1);
    	
    	tab_objetivo.setTabla("ind_objetivo", "ide_inobj", 1);


    	tab_objetivo.getColumna("ide_inobj").setNombreVisual("CODIGO");
    	tab_objetivo.getColumna("detalle_inobj").setNombreVisual("DETALLE");
    	tab_objetivo.getColumna("ACTIVO_inobj").setNombreVisual("ACTIVO");
    	tab_objetivo.getColumna("ACTIVO_inobj").setCheck();
    	tab_objetivo.getColumna("ACTIVO_inobj").setValorDefecto("TRUE");
    	
    	
    	
    	tab_objetivo.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_objetivo);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_objetivo.insertar();
    }

    @Override
    public void guardar() {
        if (tab_objetivo.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_objetivo.eliminar();
    }

	public Tabla getTab_objetivo() {
		return tab_objetivo;
	}

	public void setTab_objetivo(Tabla tab_objetivo) {
		this.tab_objetivo = tab_objetivo;
	}








}
