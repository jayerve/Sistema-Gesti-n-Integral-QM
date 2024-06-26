/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_objetivo_indicador extends Pantalla {

    private Tabla tab_objetivo_indicador = new Tabla();

    public pre_ind_objetivo_indicador() {
    	tab_objetivo_indicador.setId("tab_objetivo_indicador");
    	tab_objetivo_indicador.setNumeroTabla(1);
    	tab_objetivo_indicador.setTabla("ind_objetivo_indicador", "ide_inobi", 1);
    	tab_objetivo_indicador.getColumna("ide_inobi").setNombreVisual("CODIGO");
    	tab_objetivo_indicador.getColumna("detalle_inobi").setNombreVisual("DETALLE");
    	tab_objetivo_indicador.getColumna("ACTIVO_inobi").setNombreVisual("ACTIVO");    	
    	tab_objetivo_indicador.getColumna("ACTIVO_inobi").setCheck();
    	tab_objetivo_indicador.getColumna("ACTIVO_inobi").setValorDefecto("TRUE");
    	
    	
    	
    	tab_objetivo_indicador.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_objetivo_indicador);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_objetivo_indicador.insertar();
    }

    @Override
    public void guardar() {
        if (tab_objetivo_indicador.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_objetivo_indicador.eliminar();
    }

	public Tabla getTab_objetivo_indicador() {
		return tab_objetivo_indicador;
	}

	public void setTab_objetivo_indicador(Tabla tab_objetivo_indicador) {
		this.tab_objetivo_indicador = tab_objetivo_indicador;
	}





}
