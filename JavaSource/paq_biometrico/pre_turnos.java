/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_turnos extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_turnos() {
        tab_tabla.setId("tab_tabla");
       	tab_tabla.setTabla("ASI_TURNOS", "ide_astur", 1);
        tab_tabla.getColumna("IDE_GTGRE").setCombo("SELECT IDE_GTGRE,DETALLE_GTGRE FROM gth_grupo_empleado ORDER BY DETALLE_GTGRE ASC ");
        tab_tabla.getColumna("IDE_GTGRE").setNombreVisual("TIPO DE TURNO");
        tab_tabla.getColumna("IDE_GTGRE").setOrden(2);
        tab_tabla.getColumna("NOM_ASTUR").setNombreVisual("NOMBRE");
        tab_tabla.getColumna("NOM_ASTUR").setOrden(1);
        tab_tabla.getColumna("MINUTO_TOLERANCIA_ASTUR").setNombreVisual("TOLERANCIA_MIN");
        tab_tabla.getColumna("MINUTO_TOLERANCIA_ASTUR").setOrden(3);
        tab_tabla.getColumna("ACTIVO_ASTUR").setCheck();
        tab_tabla.getColumna("ACTIVO_ASTUR").setValorDefecto("true");
        tab_tabla.getColumna("ACTIVO_ASTUR").setNombreVisual("ACTIVO");
        tab_tabla.getColumna("ACTIVO_ASTUR").setOrden(4);
        tab_tabla.getColumna("IDE_ASHOR").setVisible(false);
        tab_tabla.getColumna("IDE_ASHOR").setOrden(5);
        tab_tabla.getColumna("DESCRIPCION_ASTUR").setVisible(false);
        tab_tabla.getColumna("turno_matriz_astur").setValorDefecto("false");
        tab_tabla.getColumna("minuto_tolerancia_astur").setVisible(false);
 
        
        tab_tabla.setHeader("CREACIÓN DE TURNOS");
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
