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
public class pre_calendario_horario_periodo extends Pantalla {

    private Tabla tab_periodo = new Tabla();

    public pre_calendario_horario_periodo() {
    	
    	
    	tab_periodo.setId("tab_periodo");
		tab_periodo.setTabla("asi_horario_periodo", "ide_ashop", 1);
		
		tab_periodo.getColumna("ide_ashop").setNombreVisual("CODIGO");
		
		
		tab_periodo.getColumna("ide_gemes").setCombo("select ide_gemes,  "
           		+ "detalle_gemes "
           		+ "from gen_mes  order by ide_gemes asc ");
		
		tab_periodo.getColumna("ide_geani").setCombo("select ide_geani,  "
	           		+ "detalle_geani "
	           		+ "from gen_anio  order by ide_geani asc ");
	
		tab_periodo.getColumna("activo_ashop").setCheck();
		tab_periodo.getColumna("activo_ashop").setValorDefecto("true");
		tab_periodo.getColumna("activo_ashop").setNombreVisual("ACTIVO");;
		
		
        tab_periodo.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_periodo);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_periodo.insertar();
    }

    @Override
    public void guardar() {
        if (tab_periodo.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_periodo.eliminar();
    }

	public Tabla getTab_periodo() {
		return tab_periodo;
	}

	public void setTab_periodo(Tabla tab_periodo) {
		this.tab_periodo = tab_periodo;
	}


}
