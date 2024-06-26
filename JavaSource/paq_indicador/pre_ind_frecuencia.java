/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_frecuencia extends Pantalla {

    private Tabla tab_frecuencia = new Tabla();

    public pre_ind_frecuencia() {
    	tab_frecuencia.setId("tab_frecuencia");
    	tab_frecuencia.setNumeroTabla(1);
    	
    	tab_frecuencia.setTabla("ind_frecuencia", "ide_infre", 1);

    	tab_frecuencia.getColumna("ide_infre").setNombreVisual("CODIGO");
    	tab_frecuencia.getColumna("detalle_infre").setNombreVisual("DETALLE");
    	tab_frecuencia.getColumna("ACTIVO_infre").setNombreVisual("ACTIVO");
  	
    	tab_frecuencia.getColumna("ACTIVO_infre").setCheck();
    	tab_frecuencia.getColumna("ACTIVO_infre").setValorDefecto("TRUE");
    	
    	tab_frecuencia.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_frecuencia);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_frecuencia.insertar();
    }

    @Override
    public void guardar() {
        if (tab_frecuencia.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_frecuencia.eliminar();
    }

	public Tabla getTab_frecuencia() {
		return tab_frecuencia;
	}

	public void setTab_frecuencia(Tabla tab_frecuencia) {
		this.tab_frecuencia = tab_frecuencia;
	}










}
