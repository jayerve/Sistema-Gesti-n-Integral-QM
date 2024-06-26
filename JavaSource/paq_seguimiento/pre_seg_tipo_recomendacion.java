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
public class pre_seg_tipo_recomendacion extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_seg_tipo_recomendacion() {
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_tipo_recomendacion", "ide_setir", 1);
        
       
        tab_tabla.getColumna("ide_setir").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_setir").setNombreVisual("TIPO");
        tab_tabla.getColumna("descripcion_setir").setAncho(80);
        tab_tabla.getColumna("descripcion_setir").setLongitud(150);
  
        tab_tabla.getColumna("activo_setir").setCheck();
        tab_tabla.getColumna("activo_setir").setValorDefecto("true");
        tab_tabla.getColumna("activo_setir").setNombreVisual("ACTIVO");
        
        
        tab_tabla.getGrid().setColumns(4);
       // tab_tabla.setTipoFormulario(true);
       // tab_tabla.setLectura(true);
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("TIPO RECOMENDACIÓN");

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
