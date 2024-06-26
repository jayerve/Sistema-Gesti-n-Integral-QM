/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_salud;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_calificacion extends Pantalla {

    
    private Tabla tab_calificacion = new Tabla();

    public pre_calificacion() {        
        tab_calificacion.setId("tab_calificacion");
        tab_calificacion.setTabla("SAO_CALIFICACION","IDE_SACAL",1);
    	tab_calificacion.getColumna("ACTIVO_SACAL").setCheck();
    	tab_calificacion.getColumna("ACTIVO_SACAL").setValorDefecto("true");
        tab_calificacion.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_calificacion);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_calificacion.insertar();
    }

    @Override
    public void guardar() {
        tab_calificacion.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_calificacion.eliminar();
    }

	public Tabla getTab_calificacion() {
		return tab_calificacion;
	}

	public void setTab_calificacion(Tabla tab_calificacion) {
		this.tab_calificacion = tab_calificacion;
	}


}
