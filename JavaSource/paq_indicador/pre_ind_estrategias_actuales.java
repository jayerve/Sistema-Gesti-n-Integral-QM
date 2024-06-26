/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_estrategias_actuales extends Pantalla {

    private Tabla tab_estrategias_actuales = new Tabla();

    public pre_ind_estrategias_actuales() {
    	tab_estrategias_actuales.setId("tab_estrategias_actuales");
    	tab_estrategias_actuales.setNumeroTabla(1);
    	tab_estrategias_actuales.setTabla("ind_estrategias_actuales", "ide_inesa", 1); 
    	
    	tab_estrategias_actuales.getColumna("ide_inesa").setNombreVisual("CODIGO");
    	tab_estrategias_actuales.getColumna("detalle_inesa").setNombreVisual("DETALLE");
    	tab_estrategias_actuales.getColumna("ACTIVO_inesa").setNombreVisual("ACTIVO");
    	tab_estrategias_actuales.getColumna("ACTIVO_inesa").setCheck();
    	tab_estrategias_actuales.getColumna("ACTIVO_inesa").setValorDefecto("TRUE");
    	
    	
    	
    	tab_estrategias_actuales.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_estrategias_actuales);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_estrategias_actuales.insertar();
    }

    @Override
    public void guardar() {
        if (tab_estrategias_actuales.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_estrategias_actuales.eliminar();
    }

	public Tabla getTab_estrategias_actuales() {
		return tab_estrategias_actuales;
	}

	public void setTab_estrategias_actuales(Tabla tab_estrategias_actuales) {
		this.tab_estrategias_actuales = tab_estrategias_actuales;
	}






}
