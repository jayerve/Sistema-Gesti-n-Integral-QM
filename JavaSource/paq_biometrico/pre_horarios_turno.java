/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import javax.ejb.EJB;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
/**
 *
 * @author DELL-USER
 */
public class pre_horarios_turno extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();    
    private Combo com_tipo_hora= new Combo();
    private SeleccionTabla sel_departamento_empleado = new SeleccionTabla();
    private SeleccionTabla set_empleado = new SeleccionTabla();
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

    
    public pre_horarios_turno() {
    	

		sel_departamento_empleado.setId("sel_departamento_empleado");
		sel_departamento_empleado.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO", "IDE_GEDEP");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		sel_departamento_empleado.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(sel_departamento_empleado);
		sel_departamento_empleado.getBot_aceptar().setMetodo("aceptarAsignarEmpleadoDepartamento");
		agregarComponente(sel_departamento_empleado);
    	

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_empleado.servicioEmpleadosActivos("true"),"ide_gtemp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
		set_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("aceptarAsignarEmpleado");
		agregarComponente(set_empleado);

		
		
		  tab_tabla1.setId("tab_tabla1");
    	  tab_tabla1.setTabla("ASI_HORARIO", "IDE_ASHOR", 1);
          tab_tabla1.getColumna("IDE_ASHOR").setNombreVisual("CÓDIGO");
          tab_tabla1.getColumna("IDE_ASHOR").setOrden(1);
    	  tab_tabla1.getColumna("HORA_INICIAL_ASHOR").setMascara("99:99");   
          tab_tabla1.getColumna("HORA_INICIAL_ASHOR").setNombreVisual("HORA_ENTRADA");
          tab_tabla1.getColumna("HORA_INICIAL_ASHOR").setOrden(3);
          tab_tabla1.getColumna("HORA_FINAL_ASHOR").setMascara("99:99");
          tab_tabla1.getColumna("HORA_FINAL_ASHOR").setNombreVisual("HORA_SALIDA"); 
          tab_tabla1.getColumna("HORA_FINAL_ASHOR").setOrden(4);
          tab_tabla1.getColumna("IDE_ASGRI").setVisible(false);
          tab_tabla1.getColumna("HORA_INICIO_ALMUERZO_ASHOR").setMascara("99:99");     
          tab_tabla1.getColumna("HORA_INICIO_ALMUERZO_ASHOR").setNombreVisual("INICIO_ALMUERZO");
          tab_tabla1.getColumna("HORA_INICIO_ALMUERZO_ASHOR").setOrden(5);   

          tab_tabla1.getColumna("HORA_FIN_ALMUERZO_ASHOR").setMascara("99:99");
          tab_tabla1.getColumna("HORA_FIN_ALMUERZO_ASHOR").setNombreVisual("FIN_ALMUERZO");
          tab_tabla1.getColumna("HORA_FIN_ALMUERZO_ASHOR").setOrden(6);   

          tab_tabla1.getColumna("MIN_ALMUERZO_ASHOR").setNombreVisual("MIN_ALMUERZO");
          tab_tabla1.getColumna("MIN_ALMUERZO_ASHOR").setOrden(7);

          tab_tabla1.getColumna("NOM_ASHOR").setNombreVisual("NOMBRE_HORARIO");
          tab_tabla1.getColumna("NOM_ASHOR").setOrden(2);
          tab_tabla1.getColumna("ACTIVO_ASHOR").setCheck();
          tab_tabla1.getColumna("ACTIVO_ASHOR").setValorDefecto("true");
          tab_tabla1.getColumna("ACTIVO_ASHOR").setNombreVisual("ACTIVO");
          tab_tabla1.getColumna("ACTIVO_ASHOR").setOrden(8);
          tab_tabla1.agregarRelacion(tab_tabla2);
          tab_tabla1.dibujar();          
          tab_tabla1.setHeader("CREACIÓN DE HORARIO");
          tab_tabla1.setStyle("width:100%;");
          PanelTabla pat_panel1 = new PanelTabla();
          pat_panel1.setPanelTabla(tab_tabla1);
  		  
      
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("ASI_DIA_HORARIO", "IDE_ASDIH", 2);
        tab_tabla2.getColumna("IDE_ASDIH").setNombreVisual("CÓDIGO");
        tab_tabla2.getColumna("ACTIVO_ASDIH").setCheck();
        tab_tabla2.getColumna("ACTIVO_ASDIH").setValorDefecto("true");
        tab_tabla2.getColumna("ACTIVO_ASDIH").setNombreVisual("ACTIVO");
        tab_tabla2.getColumna("IDE_GEDIA").setCombo("SELECT IDE_GEDIA,DETALLE_GEDIA FROM GEN_DIAS ORDER BY IDE_GEDIA");
        tab_tabla2.getColumna("IDE_GEDIA").setNombreVisual("DÍA");
        tab_tabla2.setHeader("ASIGNACIÓN DIAS HORARIO");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ASI_TURNOS_HORARIO", "IDE_ASTUH", 3);
        tab_tabla3.getColumna("IDE_ASTUH").setVisible(false);
        tab_tabla3.getColumna("IDE_ASHOR").setVisible(true);
        tab_tabla3.getColumna("IDE_ASHOR").setLectura(true);
        tab_tabla3.getColumna("IDE_ASTUR").setCombo("select turn.ide_astur,turn.nom_astur,'  | '||ggt.detalle_gtgre from asi_turnos turn "
        		+ "LEFT JOIN gth_grupo_empleado ggt on ggt.ide_gtgre=turn.ide_gtgre ");
        tab_tabla3.getColumna("IDE_ASTUR").setNombreVisual("TURNO");
        tab_tabla3.setHeader("ASIGNACIÓN DE TURNOS");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        
        Division div_div1 = new Division();
        div_div1.dividir2(pat_panel1, pat_panel2, "50%", "H");        
        //Division div_division = new Division();        
        //div_division.dividir2(pat_panel1,div_div1,"50%","H");
        agregarComponente(div_div1);
        
        
        

    
    }
    
    public void consultarEmpleado(){
    	if (tab_tabla1.getValorSeleccionado().isEmpty() || tab_tabla1.getValorSeleccionado()==null) {
			utilitario.agregarMensajeInfo("No se ha seleccionado Registro", "Debe escoger un turno y horario para asignar");
		}
    	
    	set_empleado.dibujar();
        }
    
   
        
    @Override
    public void insertar() {
    		if(tab_tabla1.isFocus()){
    		tab_tabla1.insertar();
    		}
    		else if(tab_tabla2.isFocus()){
    			tab_tabla2.insertar();
    		}
    }

    @Override
    public void guardar() {
        if(tab_tabla1.guardar()){
        tab_tabla2.guardar();
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




	public SeleccionTabla getSel_departamento_empleado() {
		return sel_departamento_empleado;
	}




	public void setSel_departamento_empleado(
			SeleccionTabla sel_departamento_empleado) {
		this.sel_departamento_empleado = sel_departamento_empleado;
	}






	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}






	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}





 
}
