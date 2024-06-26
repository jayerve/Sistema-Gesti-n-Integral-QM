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
public class pre_seg_usuario extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_seg_usuario() {
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_usuario", "ide_seusu", 1);
        
        tab_tabla.getColumna("ide_seusu").setNombreVisual("CODIGO");   
        tab_tabla.getColumna("ide_seemp").setNombreVisual("AREA");
        tab_tabla.getColumna("ide_seemp").setCombo("SELECT ide_seemp, descripcion_seemp "
        	+ "FROM seg_empresa where  activo_seemp=true");
        tab_tabla.getColumna("ide_seemp").setRequerida(true);
        
        tab_tabla.getColumna("ide_secar").setNombreVisual("CARGO");
        tab_tabla.getColumna("ide_secar").setCombo("SELECT ide_secar, descripcion_secar "
        	+ "FROM seg_cargo");
        tab_tabla.getColumna("ide_secar").setRequerida(true);		
        tab_tabla.getColumna("nombre_seusu").setNombreVisual("NOMBRE");
        tab_tabla.getColumna("login_seusu").setNombreVisual("LOGIN");
        tab_tabla.getColumna("password_seusu").setNombreVisual("PASSWORD");
        tab_tabla.getColumna("usu_email").setNombreVisual("PASSWORD");

  
         tab_tabla.getColumna("ide_usua").setNombreVisual("USUARIO");
         tab_tabla.getColumna("ide_usua").setCombo("SELECT ide_usua,nick_usua "
		+ "FROM sis_usuario  "
		+ "order by ide_usua");
		
		
        
        tab_tabla.getColumna("activo_seusu").setCheck();
        tab_tabla.getColumna("activo_seusu").setValorDefecto("true");
        tab_tabla.getColumna("activo_seusu").setNombreVisual("ACTIVO");

        tab_tabla.getGrid().setColumns(4);
        //tab_tabla.setTipoFormulario(true);
        //tab_tabla.setLectura(true);
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("USUARIO");

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
