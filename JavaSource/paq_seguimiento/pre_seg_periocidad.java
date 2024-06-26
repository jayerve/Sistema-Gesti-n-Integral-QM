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
public class pre_seg_periocidad extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_seg_periocidad() {
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_periocidad", "ide_seper", 1);
        
     
        
        tab_tabla.getColumna("ide_seper").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_seper").setNombreVisual("PERIOCIDAD");
        tab_tabla.getColumna("descripcion_seper").setAncho(80);
        tab_tabla.getColumna("descripcion_seper").setLongitud(150);
  
        tab_tabla.getColumna("activo_seper").setCheck();
        tab_tabla.getColumna("activo_seper").setValorDefecto("true");
        tab_tabla.getColumna("activo_seper"
        		+ ""
        		+ "").setNombreVisual("ACTIVO");

        tab_tabla.getGrid().setColumns(4);
        //tab_tabla.setTipoFormulario(true);
       // tab_tabla.setLectura(true);
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("AREA_RESPONSBLE_DE_LA_RECOMENDACIÓN");

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
