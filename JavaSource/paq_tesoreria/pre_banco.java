/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_tesoreria;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_banco extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    public pre_banco() {
		
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("tes_banco", "ide_teban",1);
        tab_tabla1.getColumna("ACTIVO_teban").setCheck();
        tab_tabla1.getColumna("ACTIVO_teban").setValorDefecto("true"); 
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
    	pat_panel1.setMensajeWarn("BANCO");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("tes_banco_cuenta", "ide_tebac",2);
        tab_tabla2.getColumna("ide_gttcb").setCombo("gth_tipo_cuenta_bancaria", "ide_gttcb", "detalle_gttcb", "");
        tab_tabla2.getColumna("ACTIVO_tebac").setValorDefecto("true"); 
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {

        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
}
