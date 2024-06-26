/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_relevancia extends Pantalla {

    private Tabla tab_relevnancia = new Tabla();

    public pre_ind_relevancia() {
    	tab_relevnancia.setId("tab_relevnancia");
    	tab_relevnancia.setNumeroTabla(1);
    	
    	tab_relevnancia.setTabla("ind_relevancia", "ind_inrel", 1); 	
    	tab_relevnancia.getColumna("ind_inrel").setNombreVisual("CODIGO");
    	tab_relevnancia.getColumna("detalle_inrel").setNombreVisual("DETALLE RELEVANCIA");
    	tab_relevnancia.getColumna("ACTIVO_inrel").setNombreVisual("ACTIVO");
    	tab_relevnancia.getColumna("ACTIVO_inrel").setCheck();
    	tab_relevnancia.getColumna("ACTIVO_inrel").setValorDefecto("TRUE");
    	
    	
    	tab_relevnancia.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_relevnancia);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_relevnancia.insertar();
    }

    @Override
    public void guardar() {
        if (tab_relevnancia.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_relevnancia.eliminar();
    }

	public Tabla getTab_relevnancia() {
		return tab_relevnancia;
	}

	public void setTab_relevnancia(Tabla tab_relevnancia) {
		this.tab_relevnancia = tab_relevnancia;
	}





}
