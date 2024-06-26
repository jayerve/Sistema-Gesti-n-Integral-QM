/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_control_interno;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_detalle_actividad extends Pantalla {

    private Tabla tab_detalle_actividad = new Tabla();

    public pre_detalle_actividad() {
        tab_detalle_actividad.setId("tab_detalle_actividad");
        tab_detalle_actividad.setNumeroTabla(1);
        tab_detalle_actividad.dibujar();
        PanelTabla pat_detalle_actividad = new PanelTabla();
        pat_detalle_actividad.setPanelTabla(tab_detalle_actividad);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_detalle_actividad);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_detalle_actividad.insertar();
    }

    @Override
    public void guardar() {
        if (tab_detalle_actividad.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_detalle_actividad.eliminar();
    }

    public Tabla gettab_detalle_actividad() {
        return tab_detalle_actividad;
    }

    public void settab_detalle_actividad(Tabla tab_detalle_actividad) {
        this.tab_detalle_actividad = tab_detalle_actividad;
    }
}
