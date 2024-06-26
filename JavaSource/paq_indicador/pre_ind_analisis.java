/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_analisis extends Pantalla {

    private Tabla tab_analisis = new Tabla();

    public pre_ind_analisis() {
    	tab_analisis.setId("tab_analisis");
    	tab_analisis.setNumeroTabla(1);
    	
    	tab_analisis.setTabla("ind_analisis", "ind_inana", 1); 	
    	tab_analisis.getColumna("ind_inana").setNombreVisual("CODIGO");
    	tab_analisis.getColumna("detalle_inana").setNombreVisual("DETALLE");
    	tab_analisis.getColumna("ACTIVO_inana").setNombreVisual("ACTIVO");
    	tab_analisis.getColumna("ACTIVO_inana").setCheck();
    	tab_analisis.getColumna("ACTIVO_inana").setValorDefecto("TRUE");
    	
    	
    	tab_analisis.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_analisis);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_analisis.insertar();
    }

    @Override
    public void guardar() {
        if (tab_analisis.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_analisis.eliminar();
    }

	public Tabla getTab_analisis() {
		return tab_analisis;
	}

	public void setTab_analisis(Tabla tab_analisis) {
		this.tab_analisis = tab_analisis;
	}



}
