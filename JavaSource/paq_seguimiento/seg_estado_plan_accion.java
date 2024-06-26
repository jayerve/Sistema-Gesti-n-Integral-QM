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
public class seg_estado_plan_accion extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public seg_estado_plan_accion() {
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_estado_plan_accion", "ide_seesp", 1);
        
        tab_tabla.getColumna("ide_seesp").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_seesp").setNombreVisual("ESTADO_PLAN_ACCION");
        tab_tabla.getColumna("descripcion_seesp").setAncho(80);
        tab_tabla.getColumna("descripcion_seesp").setLongitud(150);
  
        tab_tabla.getColumna("activo_seesp").setCheck();
        tab_tabla.getColumna("activo_seesp").setValorDefecto("true");
        tab_tabla.getColumna("activo_seesp").setNombreVisual("ACTIVO");

        tab_tabla.getGrid().setColumns(4);
    
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("ESTADO PLAN ACCION");

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
