/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_metodo_calculo extends Pantalla {

    private Tabla tab_metodo_calculo = new Tabla();

    public pre_ind_metodo_calculo() {
    	tab_metodo_calculo.setId("tab_metodo_calculo");
    	tab_metodo_calculo.setTabla("ind_metodo_calculo", "ide_inmec", 1);

    	tab_metodo_calculo.setNumeroTabla(1);  
    	tab_metodo_calculo.getColumna("ide_inmec").setNombreVisual("CODIGO");
    	tab_metodo_calculo.getColumna("detalle_inmec").setNombreVisual("DETALLE");
    	tab_metodo_calculo.getColumna("ACTIVO_inmec").setNombreVisual("ACTIVO");
    	tab_metodo_calculo.getColumna("ACTIVO_inmec").setCheck();
    	tab_metodo_calculo.getColumna("ACTIVO_inmec").setValorDefecto("TRUE");
    	
    	tab_metodo_calculo.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_metodo_calculo);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_metodo_calculo.insertar();
    }

    @Override
    public void guardar() {
        if (tab_metodo_calculo.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_metodo_calculo.eliminar();
    }

	public Tabla getTab_metodo_calculo() {
		return tab_metodo_calculo;
	}

	public void setTab_metodo_calculo(Tabla tab_metodo_calculo) {
		this.tab_metodo_calculo = tab_metodo_calculo;
	}






}
