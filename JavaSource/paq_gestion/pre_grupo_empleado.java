/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_grupo_empleado extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_grupo_empleado() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GTH_GRUPO_EMPLEADO", "IDE_GTGRE", 1);
        tab_tabla.getColumna("ACTIVO_GTGRE").setCheck();
        tab_tabla.getColumna("ACTIVO_GTGRE").setValorDefecto("TRUE");
        tab_tabla.setCampoOrden("DETALLE_GTGRE");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
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
