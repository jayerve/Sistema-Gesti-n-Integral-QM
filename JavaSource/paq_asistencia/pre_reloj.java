/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
/**
 *
 * @author DELL-USER
 */
public class pre_reloj extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    
    private Combo com_sucursales=new Combo();

    public pre_reloj() {

    	com_sucursales.setCombo("SELECT IDE_SUCU,NOM_SUCU FROM SIS_SUCURSAL order by NOM_SUCU");
    	com_sucursales.setMetodo("cambioSucursal");    	
    	bar_botones.agregarComponente(new Etiqueta("Sucursales :"));
    	bar_botones.agregarComponente(com_sucursales);
    	
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CON_RELOJ", "IDE_COREL", 1);
        tab_tabla1.getColumna("ACTIVO_COREL").setCheck();
        tab_tabla1.getColumna("ACTIVO_COREL").setValorDefecto("true");
        tab_tabla1.getColumna("IDE_SUCU").setVisible(false);
        tab_tabla1.setCondicion("IDE_SUCU=-1");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("CON_RELOJ_EVENTO", "IDE_COREE", 2);
        tab_tabla2.getColumna("ACTIVO_COREE").setCheck();
        tab_tabla2.getColumna("ALMUERZO_COREE").setCheck();
        tab_tabla2.getColumna("ENTRADA_COREE").setCheck();
        tab_tabla2.getColumna("ACTIVO_COREE").setValorDefecto("true");
        tab_tabla2.getColumna("IDE_SUCU").setVisible(false);
        tab_tabla2.setCondicion("IDE_SUCU=-1");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        tab_tabla1.setHeader("DISPOSITIVOS DE CONTROL");
        pat_panel2.setPanelTabla(tab_tabla2);
        tab_tabla2.setHeader("TIPO DE EVENTOS DE LOS DISPOSITOS");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "V");
        agregarComponente(div_division);
    }

    public void cambioSucursal(){
    	if(com_sucursales.getValue()!=null){
    		tab_tabla1.setCondicion("IDE_SUCU="+com_sucursales.getValue().toString());
        	tab_tabla1.ejecutarSql();
        	tab_tabla2.setCondicion("IDE_SUCU="+com_sucursales.getValue().toString());
        	tab_tabla2.ejecutarSql();	
    	}
    	else{
    		tab_tabla1.limpiar();
    		tab_tabla2.limpiar();
    	}    	
    }
    
    @Override
    public void insertar() {
    	if(com_sucursales.getValue()!=null){
    		tab_tabla1.getColumna("IDE_SUCU").setValorDefecto(com_sucursales.getValue().toString());
    		tab_tabla2.getColumna("IDE_SUCU").setValorDefecto(com_sucursales.getValue().toString());
    		utilitario.getTablaisFocus().insertar();	
    	}
    	else{
    		utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar una sucursal");
    	}
        
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        guardarPantalla();
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
