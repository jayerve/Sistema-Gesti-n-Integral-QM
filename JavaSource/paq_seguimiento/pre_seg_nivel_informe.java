/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_seg_nivel_informe extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_seg_nivel_informe() {
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_nivel_informe", "ide_senii", 1);
        
        tab_tabla.getColumna("ide_senii").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_senii").setNombreVisual("NIVEL");
        tab_tabla.getColumna("descripcion_senii").setAncho(80);
        tab_tabla.getColumna("descripcion_senii").setLongitud(150);
  
        tab_tabla.getColumna("activo_senii").setCheck();
        tab_tabla.getColumna("activo_senii").setValorDefecto("true");
        tab_tabla.getColumna("activo_senii").setNombreVisual("ACTIVO");

        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("NIVEL INFORME");

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

  
}
