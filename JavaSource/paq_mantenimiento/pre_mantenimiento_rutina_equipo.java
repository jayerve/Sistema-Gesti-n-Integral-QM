/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_mantenimiento;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_mantenimiento_rutina_equipo extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_mantenimiento_rutina_equipo() {
    	
    	tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("mto_rutina_mantenimiento_equipo", "ide_mtrum", 1);
		
		tab_tabla.getColumna("equipo_id").setCombo("SELECT ide_mtequ, est.nombre_mtest, equip.ide_afact, equip.marca_mtequ, equip.memoria_mtequ, "
				+ "equip.modelo_mtequ, equip.nombre_mtequ, equip.observacion_mtequ, equip.procesador_mtequ, "
				+ "equip.serie_mtequ, tesq.nombre_mtteq,tesq.descripcion_mtteq "
				+ "FROM mto_equipo  equip "
				+ "left join mto_estado est  on est.ide_mtest=equip.ide_estado "
				+ "left join mto_tipo_equipo tesq  on tesq.ide_mtteq=equip.ide_tipequ");
		tab_tabla.getColumna("equipo_id").setAutoCompletar();
    
		
		
		tab_tabla.getColumna("realizado_mtrum").setCheck();
		tab_tabla.getColumna("realizado_mtrum").setValorDefecto("false");
		
		tab_tabla.getColumna("ide_mtrut").setCombo("SELECT ide_mtrut,nombre_mtrut, orden_mtrut "
				+ "FROM mto_rutina_mantenimiento");
		tab_tabla.getColumna("ide_mtrut").setAutoCompletar();
	


	
		
		tab_tabla.getColumna("ACTIVO_mtrum").setCheck();
		tab_tabla.getColumna("ACTIVO_mtrum").setValorDefecto("true");
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);
		
		
		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	
       /* tab_tabla.setId("tab_tabla");
        tab_tabla.setNumeroTabla(1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);*/
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
