/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_perspectiva extends Pantalla {

    private Tabla tab_perspectiva = new Tabla();

    public pre_ind_perspectiva() {
    	tab_perspectiva.setId("tab_perspectiva");
    	tab_perspectiva.setNumeroTabla(1);
    	
    	tab_perspectiva.setTabla("ind_perspectiva", "ide_inper", 1);
    	tab_perspectiva.getColumna("ide_inper").setNombreVisual("CODIGO");
    	tab_perspectiva.getColumna("detalle_inper").setNombreVisual("DETALLE");
    	tab_perspectiva.getColumna("ACTIVO_inper").setNombreVisual("ACTIVO");
    	tab_perspectiva.getColumna("ACTIVO_inper").setCheck();
    	tab_perspectiva.getColumna("ACTIVO_inper").setValorDefecto("TRUE");
    	
    	
    	tab_perspectiva.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_perspectiva);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_perspectiva.insertar();
    }

    @Override
    public void guardar() {
        if (tab_perspectiva.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_perspectiva.eliminar();
    }

	public Tabla getTab_perspectiva() {
		return tab_perspectiva;
	}

	public void setTab_perspectiva(Tabla tab_perspectiva) {
		this.tab_perspectiva = tab_perspectiva;
	}


}
