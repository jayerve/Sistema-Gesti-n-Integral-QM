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
 *jkhjb
 * @author DELL-USER
 */  
public class pre_posiciograma extends Pantalla {

    
    private Tabla tab_posiciograma = new Tabla();

    public pre_posiciograma() {        
        tab_posiciograma.setId("tab_posiciograma");
        tab_posiciograma.setTabla("SAO_POSICIOGRAMA", "IDE_SAPOS", 1);
    	tab_posiciograma.getColumna("ACTIVO_SAPOS").setCheck();
    	tab_posiciograma.getColumna("ACTIVO_SAPOS").setValorDefecto("true");
        tab_posiciograma.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_posiciograma);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_posiciograma.insertar();
    }

    @Override
    public void guardar() {
        tab_posiciograma.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_posiciograma.eliminar();
    }

	public Tabla getTab_posiciograma() {
		return tab_posiciograma;
	}

	public void setTab_posiciograma(Tabla tab_posiciograma) {
		this.tab_posiciograma = tab_posiciograma;
	}



}
