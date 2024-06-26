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
public class pre_funcionario extends Pantalla {

    private Tabla tab_funcionario = new Tabla();

    public pre_funcionario() {
        tab_funcionario.setId("tab_funcionario");
        tab_funcionario.setNumeroTabla(1);
        tab_funcionario.dibujar();
        PanelTabla pat_funcionario = new PanelTabla();
        pat_funcionario.setPanelTabla(tab_funcionario);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_funcionario);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_funcionario.insertar();
    }

    @Override
    public void guardar() {
        if (tab_funcionario.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_funcionario.eliminar();
    }

    public Tabla gettab_funcionario() {
        return tab_funcionario;
    }

    public void settab_funcionario(Tabla tab_funcionario) {
        this.tab_funcionario = tab_funcionario;
    }
}
