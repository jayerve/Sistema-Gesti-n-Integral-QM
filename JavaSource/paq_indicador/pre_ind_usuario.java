/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import java.util.ArrayList;
import java.util.List;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_usuario extends Pantalla {

    private Tabla tab_usuario = new Tabla();

    public pre_ind_usuario() {
    	tab_usuario.setId("tab_usuario");
    	tab_usuario.setNumeroTabla(1);
    	
    	tab_usuario.setTabla("ind_usuario", "ide_indus", 1);
    	
    	tab_usuario.getColumna("ide_indus").setNombreVisual("CODIGO");

    	
    	tab_usuario.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare  "
    			+ "FROM gen_area  "
    			+ "WHERE activo_geare=true ");
    	tab_usuario.getColumna("ide_geare").setAutoCompletar(); 	
    	tab_usuario.getColumna("ide_geare").setRequerida(true);
    	tab_usuario.getColumna("ide_geare").setNombreVisual("AREA");

    	
    	

    	tab_usuario.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
    			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
    			+ "FROM GTH_EMPLEADO EMP  "
    			+ "WHERE ACTIVO_GTEMP=true  "
    			+ "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP");
    	
    	 	
    	
    	tab_usuario.getColumna("ide_gtemp").setAutoCompletar(); 	
    	tab_usuario.getColumna("ide_gtemp").setRequerida(true);
    	tab_usuario.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");

    	List listax = new ArrayList();
		Object fila1[] = { "1", "ADMIN" };
		Object fila2[] = { "2", "ASIGNADO" };
		// Object fila8[] = {"2", "ACTIVOS FIJOS"};
		listax.add(fila1);
		listax.add(fila2);
		// listax.add(fila8);
		tab_usuario.getColumna("tipo_indus").setCombo(listax);
    	tab_usuario.getColumna("tipo_indus").setNombreVisual("PERFIL");
    	
    	tab_usuario.getColumna("activo_indus").setNombreVisual("ESTADO");

    	tab_usuario.getColumna("activo_indus").setCheck();
    	tab_usuario.getColumna("activo_indus").setValorDefecto("TRUE");
    	tab_usuario.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_usuario);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	tab_usuario.insertar();
    }

    @Override
    public void guardar() {
        if (tab_usuario.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_usuario.eliminar();
    }

	public Tabla getTab_usuario() {
		return tab_usuario;
	}

	public void setTab_usuario(Tabla tab_usuario) {
		this.tab_usuario = tab_usuario;
	}

}
