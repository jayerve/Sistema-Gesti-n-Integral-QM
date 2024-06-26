/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author Diego
 */
public class pre_tipo_perfil_asistencia extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_tipo_perfil_asistencia() {
        tab_tabla.setId("tab_tabla");
       	tab_tabla.setTabla("asi_tipo_perfil", "ide_astip", 1);
        tab_tabla.getColumna("ide_astip").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_astip").setNombreVisual("DESCRIPCION");
        tab_tabla.getColumna("descripcion_astip").setOrden(1);
        tab_tabla.getColumna("activo_astip").setCheck();
        tab_tabla.getColumna("activo_astip").setValorDefecto("true");
        tab_tabla.getColumna("activo_astip").setNombreVisual("ACTIVO");
 
        
        tab_tabla.setHeader("TIPO PERFIL");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    
    
    
    public Tabla getTab_tabla() {
		return tab_tabla;
	}




	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
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


}
