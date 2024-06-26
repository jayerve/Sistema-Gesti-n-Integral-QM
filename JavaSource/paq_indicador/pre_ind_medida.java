/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_medida extends Pantalla {

    private Tabla tab_medida = new Tabla();

    public pre_ind_medida() {
    	tab_medida.setId("tab_medida");
    	tab_medida.setNumeroTabla(1);
    	
    	tab_medida.setTabla("ind_medida", "ide_inmed", 1);
    	tab_medida.getColumna("ide_inmed").setNombreVisual("CODIGO");
    	tab_medida.getColumna("detalle_inmed").setNombreVisual("DETALLE");
    	tab_medida.getColumna("ACTIVO_inmed").setNombreVisual("ACTIVO");
    	tab_medida.getColumna("activo_inmed").setCheck();
    	tab_medida.getColumna("activo_inmed").setValorDefecto("TRUE");
    	
    	tab_medida.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_medida);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_medida.insertar();
    }

    @Override
    public void guardar() {
        if (tab_medida.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_medida.eliminar();
    }

	public Tabla getTab_medida() {
		return tab_medida;
	}

	public void setTab_medida(Tabla tab_medida) {
		this.tab_medida = tab_medida;
	}













}
