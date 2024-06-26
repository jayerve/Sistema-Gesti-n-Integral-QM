/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_metrica extends Pantalla {

    private Tabla tab_metrica = new Tabla();

    public pre_ind_metrica() {
    	tab_metrica.setId("tab_metrica");
    	tab_metrica.setNumeroTabla(1);
    	
    	tab_metrica.setTabla("ind_metrica", "ide_inmet", 1);
  	
    	tab_metrica.getColumna("ide_inmet").setNombreVisual("CODIGO");
    	tab_metrica.getColumna("detalle_inmet").setNombreVisual("DETALLE");
    	tab_metrica.getColumna("ACTIVO_inmet").setNombreVisual("ACTIVO");
    	tab_metrica.getColumna("activo_inmet").setCheck();
    	tab_metrica.getColumna("activo_inmet").setValorDefecto("TRUE");
    	
    	tab_metrica.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_metrica);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_metrica.insertar();
    }

    @Override
    public void guardar() {
        if (tab_metrica.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_metrica.eliminar();
    }

	public Tabla getTab_metrica() {
		return tab_metrica;
	}

	public void setTab_metrica(Tabla tab_metrica) {
		this.tab_metrica = tab_metrica;
	}












}
