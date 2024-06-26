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
public class pre_certificado_tipo extends Pantalla {

    
    private Tabla tab_certificado = new Tabla();

    public pre_certificado_tipo() {        
        tab_certificado.setId("tab_certificado");
        tab_certificado.setTabla("SAO_CERTIFCADO_TIPO","IDE_SACET",1);
     	tab_certificado.getColumna("ACTIVO_SACET").setCheck();
    	tab_certificado.getColumna("ACTIVO_SACET").setValorDefecto("true");
        tab_certificado.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_certificado);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_certificado.insertar();
    }

    @Override
    public void guardar() {
        tab_certificado.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_certificado.eliminar();
    }

	public Tabla gettab_certificado() {
		return tab_certificado;
	}

	public void settab_certificado(Tabla tab_certificado) {
		this.tab_certificado = tab_certificado;
	}

}
