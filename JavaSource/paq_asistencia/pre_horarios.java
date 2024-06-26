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
public class pre_horarios extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();    
    private Combo com_tipo_hora= new Combo();

    public pre_horarios() {
    	
    	com_tipo_hora.setCombo("select IDE_ASGRI,detalle_ASGRI from ASI_GRUPO_INTERVALO");
    	com_tipo_hora.setMetodo("cambioTipoHora");
    	
    	bar_botones.agregarComponente(new Etiqueta("Tipo de Hora :"));
    	bar_botones.agregarComponente(com_tipo_hora);    
    	
    	  tab_tabla1.setId("tab_tabla1");
         tab_tabla1.setTabla("ASI_HORARIO", "IDE_ASHOR", 1);
          tab_tabla1.getColumna("ACTIVO_ASHOR").setCheck();
          tab_tabla1.getColumna("ACTIVO_ASHOR").setValorDefecto("true");
          tab_tabla1.setMostrarcampoSucursal(true);   
          tab_tabla1.getColumna("HORA_INICIAL_ASHOR").setMascara("99:99");     
         tab_tabla1.getColumna("HORA_FINAL_ASHOR").setMascara("99:99");
          tab_tabla1.getColumna("IDE_ASGRI").setVisible(false);
          tab_tabla1.setHeader("HORARIO DE TIPO DE HORA");
          tab_tabla1.setCondicion("IDE_ASGRI=-1");          
          tab_tabla1.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
         tab_tabla1.agregarRelacion(tab_tabla2);
         tab_tabla1.agregarRelacion(tab_tabla3);
          tab_tabla1.dibujar();          
          PanelTabla pat_panel1 = new PanelTabla();
          pat_panel1.setPanelTabla(tab_tabla1);
                
          tab_tabla2.setId("tab_tabla2");
          tab_tabla2.setTabla("ASI_TURNOS","IDE_ASTUR",2);
          tab_tabla2.getColumna("ACTIVO_ASTUR").setCheck();
          tab_tabla2.getColumna("IDE_GTGRE").setCombo("GTH_GRUPO_EMPLEADO","IDE_GTGRE","detalle_GTGRE","ACTIVO_GTGRE=true");
          tab_tabla2.getColumna("ACTIVO_ASTUR").setValorDefecto("true");
          tab_tabla2.setHeader("GRUPO DE EMPLEADO POR HORARIO");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ASI_DIA_HORARIO", "IDE_ASDIH", 3);
        tab_tabla3.getColumna("ACTIVO_ASDIH").setCheck();
        tab_tabla3.getColumna("ACTIVO_ASDIH").setValorDefecto("true");
        tab_tabla3.getColumna("IDE_GEDIA").setCombo("SELECT IDE_GEDIA,DETALLE_GEDIA FROM GEN_DIAS ORDER BY IDE_GEDIA");
        tab_tabla3.setHeader("DIAS HORARIO");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        
        Division div_div1 = new Division();
        div_div1.dividir2(pat_panel3, pat_panel2, "50%", "H");        
        Division div_division = new Division();        
        div_division.dividir2(pat_panel1,div_div1,"50%","v");
        agregarComponente(div_division);
    }

    public void cambioTipoHora(){
    	if(com_tipo_hora.getValue()!=null){
    		tab_tabla1.setCondicion("IDE_ASGRI="+com_tipo_hora.getValue());
    		tab_tabla1.ejecutarSql();    		
    		tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());    		
    		tab_tabla3.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());    		
    		   	}
    	else{
    		tab_tabla1.limpiar();    		
    	}
    }
    
    
    @Override
    public void insertar() {
    	if(com_tipo_hora.getValue()!=null){
    		if(tab_tabla1.isFocus()){
    		tab_tabla1.insertar();
    		tab_tabla1.setValor("IDE_ASGRI", com_tipo_hora.getValue().toString());
    		}
    		else if(tab_tabla2.isFocus()){
    			tab_tabla2.insertar();
    		}
    		else if(tab_tabla3.isFocus()){
    			tab_tabla3.insertar();
    		}
    	}
    	else{
    		utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar un Tipo de Hora");
    	}        
    }

    @Override
    public void guardar() {
        if(tab_tabla1.guardar()){
        tab_tabla2.guardar();
        	tab_tabla3.guardar();
        		guardarPantalla();        	       
        } 
    }

    @Override
    public void eliminar() {
        if(tab_tabla1.isFocus()){
        	tab_tabla1.eliminar();
        }
        else if(tab_tabla2.isFocus()){
        	tab_tabla2.eliminar();
        }
        else if(tab_tabla3.isFocus()){
        	tab_tabla2.eliminar();
        }
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

	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
}
