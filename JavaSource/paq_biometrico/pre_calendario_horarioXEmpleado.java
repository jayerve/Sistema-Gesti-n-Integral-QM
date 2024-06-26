/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.text.TabExpander;

import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.codehaus.groovy.tools.groovydoc.FileOutputTool;
import org.jfree.util.Log;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;

import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author Juan
 */
public class pre_calendario_horarioXEmpleado extends Pantalla {
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioEmpleado ser_empleado1 = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_calendario= new Tabla(); 
    private Tabla tab_turnos= new Tabla(); 
    private Tabla tab_tabla3= new Tabla(); 
private boolean copirDepartamento,copiar;

private String empleado1;
private String empleadoSucursal;
private String meses="",anios="",mesEditar="",anioEditar="";
private String dia1="",dia2="",dia3="",dia4="",dia5="",dia6="",dia7="",dia8="",dia9="",
dia10="",dia11="",dia12="",dia13="",dia14="",dia15="",dia16="",dia17="",dia18="",dia19="",dia20="",dia21="",
dia22="",dia23="",dia24="",dia25="",dia26="",dia27="",dia28="",dia29="",dia30="",dia31="";


    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_mes_editar= new SeleccionTabla();
    private SeleccionTabla sel_anio_editar= new SeleccionTabla();
    private SeleccionTabla sel_mes_reporte= new SeleccionTabla();
    private SeleccionTabla sel_anio_reporte= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    private SeleccionTabla sel_empleado= new SeleccionTabla();
    private SeleccionTabla sel_empleado_editar= new SeleccionTabla();
    private SeleccionTabla sel_departamento_empleado = new SeleccionTabla();
    private String sucursal="";
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();



    
    private Dialogo  dia_num_copiar = new Dialogo();
    private Boolean num_copias=false,bandAsignar=false;
    private Boolean bandEdit=false;

    private String mes;
    private String anio;
	 private String empleado;
	private  ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	private  ArrayList<Integer> listaAnio = new ArrayList<Integer>();
	private  ArrayList<Integer> listaMes = new ArrayList<Integer>();
	private Calendario cal_fecha_inicial = new Calendario();
	private AutoCompletar aut_empleados=new AutoCompletar();
	private boolean editar=false;
	private Grid gri=new Grid();

private    	String sucu="";
private	String area="",ide_geare="",jefe_padre="",cargo_padre="";
private String depa="",ide_gtempxx="",tipo_perfil="";
String ide_asjei="";     	
private Reporte rep_reporte = new Reporte();
private Map p_parametros = new HashMap();
private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
TablaGenerica tabJefeInmediato=null;
TablaGenerica tabJefeInmediatos=null;

	public pre_calendario_horarioXEmpleado() {

    
		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	
    	
    	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,ide_gtemp_padre_asjei,cargo_padre_asjei  "
    			+ "FROM asi_jefe_inmediato  asjei "
    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
    			+ "where ide_gtemp="+ide_gtempxx);
    	
    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    		utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		return;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
        	area=tabJefeInmediato.getValor("detalle_geare");
        	TablaGenerica tabEmpleadoXJefeInmediato=null;
           	
        	if(tipo_perfil.equals("1")){
        		ide_asjei=""; 
   				TablaGenerica tab_ide_geedp=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare  "
   		    			+ "FROM asi_jefe_inmediato  asjei "
   		    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare ");
				if (tab_ide_geedp.getTotalFilas()>0) {
					for (int j = 0; j < tab_ide_geedp.getTotalFilas(); j++) {
						if (j==(tab_ide_geedp.getTotalFilas()-1)) {
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
						}else{
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
							ide_asjei+=",";

						}
					}
        		
	    	 }
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
                tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
          				+ "FROM asi_empleado_jefe_inmediato ");
          			//	+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
      
                 String int_num_col_idegetempmensual="";
                 for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                		 str_ide_empleado_mensual.append(",");
                	 }
                	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                 }
        		
        		
        		
        	}
        	
        	if(tipo_perfil.equals("2")){
        		ide_asjei=tabJefeInmediato.getValor("ide_asjei"); 
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
        	       tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
             				+ "FROM asi_empleado_jefe_inmediato "
             				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
         
                    String int_num_col_idegetempmensual="";
                    for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                   	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                   	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                   		 str_ide_empleado_mensual.append(",");
                   	 }
                   	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                    }
        		
        		
        		
	    	 }
        	
		
    	}
		
 
    	//bar_botones.agregarComponente(new Etiqueta("Reportes de Biometrico"));
		//bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);
		
		//Consulta empleado loggeado
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

		//Consulta de empleado por sucursal
		
		TablaGenerica tabSucursal=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EPAR.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.IDE_SUCU,SUCU.NOM_SUCU, AREA.IDE_GEARE,AREA.DETALLE_GEARE,  "
				+ "DEPA.IDE_GEDEP,DEPA.DETALLE_GEDEP   "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "where epar.ide_geedp="+ide_geedp_activo
				+ " ORDER BY DETALLE_GEARE ASC");
		area=tabSucursal.getValor("IDE_GEARE");
		sucu=tabSucursal.getValor("IDE_SUCU");
		empleadoSucursal=tabSucursal.getValor("IDE_GTEMP");
		
		
		
		Etiqueta eti_colaborador=new Etiqueta("ESCOGER :  ");
		bar_botones.agregarComponente(eti_colaborador);
		
			
		//bar_botones.agregarComponente(aut_empleados);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(true);
		bar_botones.getBot_guardar().setRendered(true);

 	
    	
    	// boton limpiar
    	Boton bot_mes = new Boton();
    	bot_mes.setIcon("ui-icon-cancel");
    	bot_mes.setValue("MES");
    	bot_mes.setTitle("MES");
    	bot_mes.setMetodo("seleccionarMes");
    	bar_botones.agregarBoton(bot_mes);

    	
    	// boton limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setValue("Limpiar");
    	bot_limpiar.setTitle("Limpiar");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar);
    	
    	
    

		
    	
		String str_mes_asignacion_horario = utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
    	 
    	sel_mes.setId("sel_mes");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in ("+str_mes_asignacion_horario+") ","IDE_GEMES");

    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("20");
		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.getBot_aceptar().setMetodo("obtenerMes");
		agregarComponente(sel_mes);
		
		

		sel_mes_reporte.setId("sel_mes_reporte");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
		sel_mes_reporte.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in ("+str_mes_asignacion_horario+") ","IDE_GEMES");

		sel_mes_reporte.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes_reporte.setRadio();
		sel_mes_reporte.setTitle("Seleccione Mes Horario");
		sel_mes_reporte.setWidth("20");
		sel_mes_reporte.setHeight("20");
		gru_pantalla.getChildren().add(sel_mes_reporte);
		sel_mes_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_mes_reporte);
		
    
		
    	sel_anio_editar.setId("sel_anio_editar");
    	sel_anio_editar.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato")+") ","IDE_GEANI");
    	sel_anio_editar.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	sel_anio_editar.setRadio();
    	sel_anio_editar.setTitle("Seleccione A�o Horario");
		gru_pantalla.getChildren().add(sel_anio_editar);
		sel_anio_editar.setWidth("25");
		sel_anio_editar.setHeight("30");
		sel_anio_editar.getBot_aceptar().setMetodo("obtenerAnioEditar");
		agregarComponente(sel_anio_editar);
    	

    
		sel_anio_reporte.setId("sel_anio_reporte");
		sel_anio_reporte.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato")+") ","IDE_GEANI");
		sel_anio_reporte.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
		sel_anio_reporte.setRadio();
		sel_anio_reporte.setTitle("Seleccione A�o Horario");
		gru_pantalla.getChildren().add(sel_anio_reporte);
		sel_anio_reporte.setWidth("25");
		sel_anio_reporte.setHeight("30");
		sel_anio_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_anio_reporte);
    	
    	
    	

		
		tab_tabla.setId("tab_tabla");
  	    tab_tabla.setTabla("asi_horario_mes_empleado", "ide_ashme", 1);
  	    tab_tabla.getColumna("ide_ashme").setNombreVisual("COD");
  	    tab_tabla.getColumna("ide_ashme").setLongitud(5);
  	    tab_tabla.getColumna("ide_ashme").alinearCentro();

 
  	  tab_tabla.getColumna("IDE_GTEMP").setCombo("SELECT EPAR.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE");
  	  tab_tabla.getColumna("IDE_GTEMP").setLectura(true);

  	  
  	   // tab_tabla.getColumna("IDE_GTEMP").setLectura(true);
        tab_tabla.getColumna("IDE_GTEMP").setLongitud(50);
        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
		tab_tabla.getColumna("IDE_GTEMP").setFiltroContenido();


  	    
       tab_tabla.getColumna("IDE_GEMES").setCombo("select ide_gemes,detalle_gemes from gen_mes ");
       tab_tabla.getColumna("IDE_GEMES").setVisible(false);

       tab_tabla.getColumna("IDE_GEANI").setCombo("select ide_geani,detalle_geani from gen_anio");
       tab_tabla.getColumna("IDE_GEANI").setVisible(false);

       
       

       tab_tabla.getColumna("dia1").setCombo("select ide_astur,  "
       		+ "nom_astur, "
       		+ "descripcion_astur "
       		+ "from asi_turnos ");
       tab_tabla.getColumna("dia1").setAutoCompletar();
       tab_tabla.getColumna("dia1").setLongitud(20);
       tab_tabla.getColumna("dia1").alinearCentro();

      
        tab_tabla.getColumna("dia2").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia2").setAutoCompletar();
        tab_tabla.getColumna("dia2").setLongitud(20);
        tab_tabla.getColumna("dia2").alinearCentro();

        
                   
        tab_tabla.getColumna("dia3").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia3").setAutoCompletar();
        tab_tabla.getColumna("dia3").setLongitud(20);
        tab_tabla.getColumna("dia3").alinearCentro();

        
        
        tab_tabla.getColumna("dia4").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia4").setAutoCompletar();
        tab_tabla.getColumna("dia4").setLongitud(20);
        tab_tabla.getColumna("dia4").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia5").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia5").setAutoCompletar();
        tab_tabla.getColumna("dia5").setLongitud(20);
        tab_tabla.getColumna("dia5").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia6").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia6").setAutoCompletar();
        tab_tabla.getColumna("dia6").setLongitud(20);
        tab_tabla.getColumna("dia6").alinearCentro();
       
        
        
        tab_tabla.getColumna("dia7").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia7").setAutoCompletar();
        tab_tabla.getColumna("dia7").setLongitud(20);
        tab_tabla.getColumna("dia7").alinearCentro();


           
        
        tab_tabla.getColumna("dia8").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia8").setAutoCompletar();
        tab_tabla.getColumna("dia8").setLongitud(20);
        tab_tabla.getColumna("dia8").alinearCentro();

        
        
        

        
        tab_tabla.getColumna("dia9").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia9").setAutoCompletar();
        tab_tabla.getColumna("dia9").setLongitud(20);
        tab_tabla.getColumna("dia9").alinearCentro();

        
        
        
        
        tab_tabla.getColumna("dia10").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia10").setAutoCompletar();
        tab_tabla.getColumna("dia10").setLongitud(20);
        tab_tabla.getColumna("dia10").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia11").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia11").setAutoCompletar();
        tab_tabla.getColumna("dia11").setLongitud(20);
        tab_tabla.getColumna("dia11").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia12").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia12").setAutoCompletar();
        tab_tabla.getColumna("dia12").setLongitud(20);
        tab_tabla.getColumna("dia12").alinearCentro();

        
        
        
        
        
        tab_tabla.getColumna("dia13").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia13").setAutoCompletar();
        tab_tabla.getColumna("dia13").setLongitud(20);
        tab_tabla.getColumna("dia13").alinearCentro();

        
        
        
        
        
        
        tab_tabla.getColumna("dia14").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia14").setAutoCompletar();
        tab_tabla.getColumna("dia14").setLongitud(20);
        tab_tabla.getColumna("dia14").alinearCentro();

        
        
        
        
        tab_tabla.getColumna("dia15").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia15").setAutoCompletar();
        tab_tabla.getColumna("dia15").setLongitud(20);
        tab_tabla.getColumna("dia15").alinearCentro();

        
        
        
        
        

        tab_tabla.getColumna("dia16").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia16").setAutoCompletar();
        tab_tabla.getColumna("dia16").setLongitud(20);
        tab_tabla.getColumna("dia16").alinearCentro();

        
        
        
        
        
        
        tab_tabla.getColumna("dia17").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia17").setAutoCompletar();
        tab_tabla.getColumna("dia17").setLongitud(20);
        tab_tabla.getColumna("dia17").alinearCentro();

        
        
        
        
        
        
        
        tab_tabla.getColumna("dia18").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia18").setAutoCompletar();
        tab_tabla.getColumna("dia18").setLongitud(20);
        tab_tabla.getColumna("dia18").alinearCentro();

        
        
        
        
        
        

        tab_tabla.getColumna("dia19").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia19").setAutoCompletar();
        tab_tabla.getColumna("dia19").setLongitud(20);
        tab_tabla.getColumna("dia19").alinearCentro();

        
        
        
        
        

        tab_tabla.getColumna("dia20").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia20").setAutoCompletar();
        tab_tabla.getColumna("dia20").setLongitud(20);
        tab_tabla.getColumna("dia20").alinearCentro();

        
        
        
        
        tab_tabla.getColumna("dia21").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia21").setAutoCompletar();
        tab_tabla.getColumna("dia21").setLongitud(20);
        tab_tabla.getColumna("dia21").alinearCentro();

        
        

        tab_tabla.getColumna("dia22").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia22").setAutoCompletar();
        tab_tabla.getColumna("dia22").setLongitud(20);
        tab_tabla.getColumna("dia22").alinearCentro();

        
        
        
        
        
        tab_tabla.getColumna("dia23").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia23").setAutoCompletar();
        tab_tabla.getColumna("dia23").setLongitud(20);
        tab_tabla.getColumna("dia23").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia24").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia24").setAutoCompletar();
        tab_tabla.getColumna("dia24").setLongitud(20);
        tab_tabla.getColumna("dia24").alinearCentro();

        
        
        
        
        tab_tabla.getColumna("dia25").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia25").setAutoCompletar();
        tab_tabla.getColumna("dia25").setLongitud(20);
        tab_tabla.getColumna("dia25").alinearCentro();

        
        
        

        tab_tabla.getColumna("dia26").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia26").setAutoCompletar();
        tab_tabla.getColumna("dia26").setLongitud(20);
        tab_tabla.getColumna("dia26").alinearCentro();

        
        tab_tabla.getColumna("dia27").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia27").setAutoCompletar();
        tab_tabla.getColumna("dia27").setLongitud(20);
        tab_tabla.getColumna("dia27").alinearCentro();

        
        
        
        

        tab_tabla.getColumna("dia28").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia28").setAutoCompletar();
        tab_tabla.getColumna("dia28").setLongitud(20);
        tab_tabla.getColumna("dia28").alinearCentro();

        
        
        
        
        
        tab_tabla.getColumna("dia29").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia29").setAutoCompletar();
        tab_tabla.getColumna("dia29").setLongitud(20);
        tab_tabla.getColumna("dia29").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia30").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia30").setAutoCompletar();
        tab_tabla.getColumna("dia30").setLongitud(20);
        tab_tabla.getColumna("dia30").alinearCentro();

        
        
        
        tab_tabla.getColumna("dia31").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_tabla.getColumna("dia31").setAutoCompletar();
        tab_tabla.getColumna("dia31").setLongitud(20);
        tab_tabla.getColumna("dia31").alinearCentro();

        
        
        tab_tabla.getColumna("activo_ashme").setNombreVisual("ACTIVO");
        tab_tabla.getColumna("activo_ashme").setLongitud(5);

        tab_tabla.getColumna("activo_ashme").setCheck();
        tab_tabla.getColumna("activo_ashme").setValorDefecto("true");
        tab_tabla.getColumna("activo_ashme").alinearCentro();
        
        tab_tabla.getColumna("aplica_hora_extra").setNombreVisual("H.EXT");
        tab_tabla.getColumna("aplica_hora_extra").setCheck();
        tab_tabla.getColumna("aplica_hora_extra").alinearCentro();
        
        
        tab_tabla.getColumna("IDE_SUCURSAL").setCombo("select ide_sucu,nom_sucu from sis_sucursal");
        tab_tabla.getColumna("IDE_SUCURSAL").setAutoCompletar();

        tab_tabla.getColumna("IDE_SUCURSAL").setNombreVisual("SUCURSAL");
        tab_tabla.getColumna("IDE_SUCURSAL").alinearCentro();
        tab_tabla.getColumna("IDE_SUCURSAL").setLectura(true);

        
     
        tab_tabla.getColumna("IDE_GEARE").setCombo("select ide_geare,detalle_geare from gen_area");
        tab_tabla.getColumna("IDE_GEARE").setAutoCompletar();
        tab_tabla.getColumna("IDE_GEARE").setNombreVisual("AREA");
        tab_tabla.getColumna("IDE_GEARE").setLectura(true);

        
        
        tab_tabla.getColumna("IDE_GEDEP").setCombo("select ide_gedep,detalle_gedep from gen_departamento");
        tab_tabla.getColumna("IDE_GEDEP").setAutoCompletar();
        tab_tabla.getColumna("IDE_GEDEP").setNombreVisual("DEPARTAMENTO");
        tab_tabla.getColumna("IDE_GEDEP").alinearCentro();
        tab_tabla.getColumna("IDE_GEDEP").setLectura(true);


        
        
        
        tab_tabla.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
  				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
  				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
  				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
  				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
  				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
        tab_tabla.getColumna("IDE_GEEDP").setAutoCompletar();
        tab_tabla.getColumna("IDE_GEEDP").setLongitud(20);
        tab_tabla.getColumna("IDE_GEEDP").setNombreVisual("INGRESO");
        tab_tabla.getColumna("IDE_GEEDP").alinearCentro();

        
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").setCombo("SELECT EPAR.IDE_GEEDP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
  				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
  				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
  				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
  				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
  				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
        
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").setAutoCompletar();
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").setLongitud(20);
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").setNombreVisual("CAMBIO");
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").alinearCentro();


        tab_tabla.setRows(10);
      
    	/*TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    			+ "'%"+pckUtilidades.CConversion.CStr(utilitario.getAnio((utilitario.getFechaActual())))+"%'");
		int anio=Integer.parseInt(tab_anio.getValor("ide_geani"));*/
    //    tab_tabla.setCondicion("ide_geani="+anio+" and ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1));
		//tab_tabla.setCondicion("ide_geani="+anio+" and ide_gemes=11 and ide_sucursal="+sucu);
       tab_tabla.setHeader("ASIGNACI�N TURNOS A EMPLEADOS");
        //tab_tabla.onSelect("getMesEmpleado");
        

        
        tab_tabla.setLectura(true);
        tab_tabla.setCondicion("ide_ashme=-1");

		
		
        tab_tabla.dibujar();          
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);
        
        

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("ASI_TURNOS", "IDE_ASTUR", 2);
        tab_tabla2.getColumna("ide_astur").setLongitud(8);
        tab_tabla2.getColumna("ide_astur").setOrden(1);
        tab_tabla2.getColumna("ide_astur").setNombreVisual("CODIGO");


        tab_tabla2.getColumna("nom_astur").setLongitud(40);
        tab_tabla2.getColumna("nom_astur").setOrden(2);
        tab_tabla2.getColumna("nom_astur").setNombreVisual("NOMBRE");
        tab_tabla2.getColumna("nom_astur").setLectura(true);
        
        tab_tabla2.getColumna("ide_gtgre").setLongitud(8);
        tab_tabla2.getColumna("ide_gtgre").setCombo("select ide_gtgre,detalle_gtgre from gth_grupo_empleado");
        tab_tabla2.getColumna("ide_gtgre").setAutoCompletar();
        tab_tabla2.getColumna("ide_gtgre").setNombreVisual("TIPO");
        tab_tabla2.getColumna("ide_gtgre").setOrden(3);
        tab_tabla2.getColumna("ide_gtgre").setLectura(true);

        tab_tabla2.getColumna("minuto_tolerancia_astur").setLongitud(8);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setOrden(4);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setNombreVisual("TOL.MIN");
        tab_tabla2.getColumna("minuto_tolerancia_astur").setLectura(true);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setVisible(false);

        tab_tabla2.getColumna("descripcion_astur").setVisible(false);

        tab_tabla2.getColumna("activo_astur").setLongitud(8);
        tab_tabla2.getColumna("activo_astur").setOrden(5);
        tab_tabla2.getColumna("activo_astur").setLectura(true);

        
        
        
        
        tab_tabla2.getColumna("ide_ashor").setOrden(6);
        tab_tabla2.getColumna("ide_ashor").setVisible(false);
        tab_tabla2.getColumna("turno_matriz_astur").setVisible(false);
        tab_tabla2.setCondicion("turno_matriz_astur=false");
        tab_tabla2.getColumna("hxe_astur").setVisible(false);

        
        
        tab_tabla2.onSelect("cargarHorario");

        tab_tabla2.setHeader("T U R N O S");
        
        tab_tabla2.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla2);
      
      
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ASI_HORARIO", "IDE_ASHOR", 3);
        tab_tabla3.getColumna("IDE_ASGRI").setVisible(false);
        tab_tabla3.getColumna("IDE_ASGRI").setVisible(false);
        tab_tabla3.setLectura(true);
        tab_tabla3.setCondicion("ide_ashor=-1");
        tab_tabla3.dibujar();
        tab_tabla3.setHeader("H O R A R I O S");
        
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla3);
      
      

      
      
      

//TABLA DIAS DEL MES      
  	String sql ="SELECT "
  			+ "''anio , "
  			+ "''mes , "
  			+ "''dia1 , "
  			+ "''dia2 , "
  			+ "''dia3 , "
  			+ "''dia4 , "
  			+ "''dia5 , "
  			+ "''dia6 , "
  			+ "''dia7 , "
  			+ "''dia8 , "
  			+ "''dia9 , "
  			+ "''dia10 , "
  			+ "''dia11 , "
  			+ "''dia12 , "
  			+ "''dia13 , "
  			+ "''dia14 , "
  			+ "''dia15 , "
  			+ "''dia16 , "
  			+ "''dia17 , "
  			+ "''dia18 , "
  			+ "''dia19 , "
  			+ "''dia20 , "
  			+ "''dia21 , "
  			+ "''dia22 , "
  			+ "''dia23 , "
  			+ "''dia24 , "
  			+ "''dia25 , "
  			+ "''dia26 , "
  			+ "''dia27 , "
  			+ "''dia28 , "
  			+ "''dia29 , "
  			+ "''dia30 , "
  			+ "''dia31 "
  			+ "from gen_anio where ide_geani=10";
  	
    tab_calendario.setId("tab_calendario");
    tab_calendario.setSql(sql);
    
 	tab_calendario.getColumna("anio").setLongitud(15);
 	tab_calendario.getColumna("anio").alinearCentro();
 	tab_calendario.getColumna("anio").setNombreVisual("A�O");

 	tab_calendario.getColumna("mes").setLongitud(50);
 	tab_calendario.getColumna("mes").alinearCentro();
 	tab_calendario.getColumna("mes").setNombreVisual("MES");

 	tab_calendario.setNumeroTabla(4);
    
 	tab_calendario.getColumna("dia1").setLongitud(20);
 	tab_calendario.getColumna("dia1").alinearCentro();
  	tab_calendario.getColumna("dia2").setLongitud(20);
 	tab_calendario.getColumna("dia2").alinearCentro();
  	tab_calendario.getColumna("dia3").setLongitud(20);
 	tab_calendario.getColumna("dia3").alinearCentro();
  	tab_calendario.getColumna("dia4").setLongitud(20);
 	tab_calendario.getColumna("dia4").alinearCentro();

  	tab_calendario.getColumna("dia5").setLongitud(20);
 	tab_calendario.getColumna("dia5").alinearCentro();

  	tab_calendario.getColumna("dia6").setLongitud(20);
 	tab_calendario.getColumna("dia6").alinearCentro();

  	tab_calendario.getColumna("dia7").setLongitud(20);
 	tab_calendario.getColumna("dia7").alinearCentro();

  	tab_calendario.getColumna("dia8").setLongitud(20);
 	tab_calendario.getColumna("dia8").alinearCentro();

  	tab_calendario.getColumna("dia9").setLongitud(20);
 	tab_calendario.getColumna("dia9").alinearCentro();

  	tab_calendario.getColumna("dia10").setLongitud(20);
 	tab_calendario.getColumna("dia10").alinearCentro();

  	tab_calendario.getColumna("dia11").setLongitud(20);
 	tab_calendario.getColumna("dia11").alinearCentro();

  	tab_calendario.getColumna("dia12").setLongitud(20);
 	tab_calendario.getColumna("dia12").alinearCentro();

  	tab_calendario.getColumna("dia13").setLongitud(20);
 	tab_calendario.getColumna("dia13").alinearCentro();

  	tab_calendario.getColumna("dia14").setLongitud(20);
 	tab_calendario.getColumna("dia14").alinearCentro();

  	tab_calendario.getColumna("dia15").setLongitud(20);
 	tab_calendario.getColumna("dia15").alinearCentro();

  	tab_calendario.getColumna("dia16").setLongitud(20);
 	tab_calendario.getColumna("dia16").alinearCentro();

  	tab_calendario.getColumna("dia17").setLongitud(20);
 	tab_calendario.getColumna("dia17").alinearCentro();

  	tab_calendario.getColumna("dia18").setLongitud(20);
 	tab_calendario.getColumna("dia18").alinearCentro();

  	tab_calendario.getColumna("dia19").setLongitud(20);
 	tab_calendario.getColumna("dia19").alinearCentro();

  	tab_calendario.getColumna("dia20").setLongitud(20);
 	tab_calendario.getColumna("dia20").alinearCentro();

  	tab_calendario.getColumna("dia21").setLongitud(20);
 	tab_calendario.getColumna("dia21").alinearCentro();

  	tab_calendario.getColumna("dia22").setLongitud(20);
 	tab_calendario.getColumna("dia22").alinearCentro();

  	tab_calendario.getColumna("dia23").setLongitud(20);
 	tab_calendario.getColumna("dia23").alinearCentro();

  	tab_calendario.getColumna("dia24").setLongitud(20);
 	tab_calendario.getColumna("dia24").alinearCentro();

  	tab_calendario.getColumna("dia25").setLongitud(20);
 	tab_calendario.getColumna("dia25").alinearCentro();

  	tab_calendario.getColumna("dia26").setLongitud(20);
 	tab_calendario.getColumna("dia26").alinearCentro();

  	tab_calendario.getColumna("dia27").setLongitud(20);
 	tab_calendario.getColumna("dia27").alinearCentro();

  	tab_calendario.getColumna("dia28").setLongitud(20);
 	tab_calendario.getColumna("dia28").alinearCentro();

  	tab_calendario.getColumna("dia29").setLongitud(20);
 	tab_calendario.getColumna("dia29").alinearCentro();

  	tab_calendario.getColumna("dia30").setLongitud(20);
 	tab_calendario.getColumna("dia30").alinearCentro();

  	tab_calendario.getColumna("dia31").setLongitud(20);
 	tab_calendario.getColumna("dia31").alinearCentro();
 	tab_calendario.setLectura(true);
 	tab_calendario.setHeader("MES SELECCIONADO");
    tab_calendario.dibujar();
    
    PanelTabla pat_panel6 = new PanelTabla();
    PanelTabla pat_panel7 = new PanelTabla();
    pat_panel7.setPanelTabla(tab_calendario);
    TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");
    String anioInicio=tabAnio.getValor("IDE_GEANI");
//Actualizo mi tabla calendario con el mes actual 
    getCalendario(""+utilitario.getMes(utilitario.getFechaActual()),anioInicio);

    pat_panel6.getChildren().add(pat_panel7);
    pat_panel6.getChildren().add(pat_panel1);
	Division div_division = new Division();
	div_division.dividir1(pat_panel6);

	 Division div_division2 = new Division();
	 div_division2.dividir2(pat_panel3,pat_panel5,"50%","V");
	 Division div_division1 = new Division();
	 div_division1.dividir2(div_division,div_division2,  "60%", "H");     
     agregarComponente(div_division1);
     
    }
    
    
    public void abrirMes(){
	sel_mes.dibujar();
		}
   
    public void obtenerMes(){
		  int anioActual=0;

    mes=sel_mes.getValorSeleccionado();
			  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		 			return;
		 		}else {
		 		
		 			  sel_mes.cerrar();	
				      sel_anio_editar.dibujar();
		 			
		 		}
		 			
		 			
		 			/*	 if (mes.equals("11") || mes.equals("12")) {
							anioActual=10;
						}else {
							anioActual=11;

						}
		 			 
		 			 
		 
		 			 
			  if (empleadoSucursal.equals("612") || empleadoSucursal.equals("508")) {
				  tab_tabla.setCondicion("ide_gemes="+mes);	
			  tab_tabla.ejecutarSql();
			 
				  getCalendario(mes,"2017");
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			  }else {
				  tab_tabla.setCondicion("ide_geedp="+ide_geedp_activo+" and ide_gemes="+mes+" and ide_geani="+anioActual);	
				  tab_tabla.ejecutarSql();
				  getCalendario(mes,"2017");
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			}

		 		}
		
			  sel_mes.cerrar();	*/

    }
    
    
    
    
    
   
    
    //Metodo distingue si es ingreso o edicion
    public void obtenerAnio(){
    	
    	
		  int anioActual=0;

  mes=sel_mes.getValorSeleccionado();
			  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		 			return;
		 		}else {
		 			 
			  if (area.equals("9") || empleadoSucursal.equals("508")) {
				  tab_tabla.setCondicion("ide_gemes="+mes);	
			  tab_tabla.ejecutarSql();
			 
				  getCalendario(mes,anio);
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			  }else {
				  tab_tabla.setCondicion("ide_geedp="+ide_geedp_activo+" and ide_gemes="+mes+" and ide_geani="+anio);	
				  tab_tabla.ejecutarSql();
				  getCalendario(mes,anio);
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			}

		 		}
		
			  sel_mes.cerrar();	

  
    	
    	
    	
    	
    	
 	   utilitario.addUpdate("sel_empleado");

        anio=sel_anio.getValorSeleccionado();
        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
    			return;
    		}else {
	   	sel_anio.cerrar();
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    			+ "'%"+pckUtilidades.CConversion.CStr(utilitario.getAnio((utilitario.getFechaActual())))+"%'");
		int anioEmpleado=Integer.parseInt(tab_anio.getValor("ide_geani"));    			
	 		
 		String sql="";
		
		TablaGenerica tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
				+ "from asi_horario_mes_empleado  "
				+ " where ide_geani="+anioEmpleado+" and ide_gemes="+utilitario.getMes(utilitario.getFechaActual())+" and ide_geedp="+ide_geedp_activo);
		         
 		StringBuilder str_ide_empleado_mensual=new StringBuilder();
 		String int_num_col_idegetempmensual;
 		for (int i = 0; i < tabEmpleadoMensual.getTotalFilas(); i++) {
 			int_num_col_idegetempmensual=tabEmpleadoMensual.getValor(i, "IDE_GTEMP");
  	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
  	  	str_ide_empleado_mensual.append(",");
          }
  	  str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
  }

 		
 		
 		if (str_ide_empleado_mensual==null || str_ide_empleado_mensual.toString().equals("") || str_ide_empleado_mensual.toString().isEmpty()) {
 			utilitario.agregarMensajeInfo("No contiene empleados", "No se puede asignar");
 			return;
 		}else {
 	    	sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
 					+ "emp.documento_identidad_gtemp "
 					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp in("+str_ide_empleado_mensual+")"  
 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

		}
 		
 		
 		if (area.equals("9") || empleadoSucursal.equals("508")) {
 			sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
 					+ "emp.documento_identidad_gtemp "
 					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "  
 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

		}
 		

	
		
		sel_empleado.setSeleccionTabla(sql, "IDE_GTEMP");
 		sel_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
 		sel_empleado.setHeight("60");
 		sel_empleado.setWidth("70");
 		sel_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(sel_empleado);
		sel_empleado.getBot_aceptar().setMetodo("getEmpleado");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
  	    utilitario.addUpdate("sel_empleado");
 		
 		

         
    
    }
    
    }
    
    
    

    	
   
    
	String ide_geedp_activo="";
	

	
	public void getDepartamento(){
		
		copirDepartamento=true;
		empleado="";
    /**
     * Validacion empleado
     */
		
		
		
		
		
		
		StringBuilder str_ide=new StringBuilder();
        int int_num_col_idegedep=sel_departamento_empleado.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
        for (Fila filaActual:sel_departamento_empleado.getTab_seleccion().getSeleccionados()) {
     	   
                if(str_ide.toString().isEmpty()==false){
                        str_ide.append(",");
                }
                str_ide.append(filaActual.getCampos()[int_num_col_idegedep]);
        			}

        	TablaGenerica tabEmpleadosDeparatamento=getEmpleadosDepartamento(str_ide.toString());
        
         
        	
        	StringBuilder str_ide_empleado=new StringBuilder();
         String int_num_col_idegetemp;
         for (int i = 0; i < tabEmpleadosDeparatamento.getTotalFilas(); i++) {
				int_num_col_idegetemp=tabEmpleadosDeparatamento.getValor(i, "IDE_GTEMP");
         	  	if(str_ide_empleado.toString().isEmpty()==false){
                         str_ide_empleado.append(",");
                 }
                 str_ide_empleado.append(int_num_col_idegetemp);
         }
         


 		empleado=str_ide_empleado.toString();

 		empleado1=str_ide_empleado.toString();

		
		
		
		if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			sel_empleado.cerrar();
					      }
		

	
		
		
		empleado=empleado+",";
/**
 * Validacion que limpia la lista para la asignacion de horarios para nuevos empleados
 */
		int contenido=listaEmpleado.size();
/**
 * Inicializo nuevamente la lista
 */
		
			if (contenido>0) {
				listaEmpleado.removeAll(listaEmpleado);
			}


		
		//obtengo el numero de "," dentro de los empleados seleccionados
		char[] arrayChar = empleado.toCharArray();
		int contComas=0;
		String empAux="";
		for(int i=0; i<arrayChar.length; i++){
		if( arrayChar[i] == ','){
				contComas++;	
				listaEmpleado.add(Integer.parseInt((empAux)));
				empAux="";
		}else {
			empAux=empAux+empleado.charAt(i);
			
		}
		
		
		}
		
	
		
		
		//Validacion si se ha seleccionado copiar fila mediante la variable num_copias
		
		   
			
		
		
		TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' ");	
		int anio=Integer.parseInt(tab_anio.getValor("IDE_GEANI"));
		int mesAsignado=utilitario.getMes(utilitario.getFechaActual());
		int anioAsignado=utilitario.getAnio(utilitario.getFechaActual());
		for (int i = 0; i < contComas; i++) {
			listaAnio.add(anioAsignado);
			listaMes.add(mesAsignado);
		}
		
			
		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
		String anioEscogido=tabAnio.getValor("detalle_geani");
		
		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mesAsignado);
		String mesEscogido=tabMes.getValor("detalle_gemes");
		
		getCalendario(""+mesAsignado,""+anio);
		
		
		
		tab_calendario.setLectura(true);
		tab_calendario.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
		//tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
      	sel_departamento_empleado.cerrar();

		utilitario.addUpdate("tab_calendario,sel_departamento_empleado");
	

}

	
	
	/**
	 * 
	 * @param mes caido
	 * @param anio
	 */
	public void  getCalendario(String mes,String anio){
	    TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
	    String anioInicio=tabAnio.getValor("detalle_geani");
		

		
		
		int diaInicio=1;
		int diaFin=0;
		String fecha=anioInicio+"-"+mes+"-01";
		String dia_final=utilitario.getUltimoDiaMesFecha(fecha);
        diaFin=utilitario.getDia(dia_final);
		int ani=Integer.parseInt(anioInicio);
		
		

		
		int diasSinValor=31-diaFin;
		tab_calendario.setLectura(false);
		for (int i = 1; i < diaFin+1; i++) {
			
			
			String diaSemanaPalabra=diaSemana(i, Integer.parseInt(mes), ani,0);
			tab_calendario.setValor("dia"+i, diaSemanaPalabra);
	
		}
		
		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
		String mesEscogido=tabMes.getValor("detalle_gemes");		
		tab_calendario.setValor("anio",anioInicio );
		tab_calendario.setValor("mes",mesEscogido);
		utilitario.addUpdate("tab_calendario");
		
	}
    		
    
	
	
	
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
        	utilitario.addUpdate("tab_tabla,tab_calendario,sel_empleado");
    		
        	guardarPantalla();
      
    		
    		TablaGenerica tab=utilitario.consultar("select ide_ashme,ide_gtemp from asi_horario_mes_empleado");
    		for (int j = 0; j < tab.getTotalFilas(); j++) {
    			utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null where ide_gtemp in("+tab.getValor(j, "IDE_GTEMP")+")");
                
    		}
            if (copirDepartamento) {
    			utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null where ide_gtemp in("+empleado1+")");

    		}
            
            
            
            

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



	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}



	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}



	public SeleccionTabla getSel_mes() {
		return sel_mes;
	}

	public void setSel_mes(SeleccionTabla sel_mes) {
		this.sel_mes = sel_mes;
	}



	public SeleccionTabla getSel_empleado() {
		return sel_empleado;
	}



	public SeleccionTabla getSel_empleado_editar() {
		return sel_empleado_editar;
	}



	public void setSel_empleado_editar(SeleccionTabla sel_empleado_editar) {
		this.sel_empleado_editar = sel_empleado_editar;
	}



	public void setSel_empleado(SeleccionTabla sel_empleado) {
		this.sel_empleado = sel_empleado;
	}





	public String getMes() {
		return mes;
	}



	public void setMes(String mes) {
		this.mes = mes;
	}



	public String getAnio() {
		return anio;
	}



	public void setAnio(String anio) {
		this.anio = anio;
	}



	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}



	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}



	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}
 
	 private Date getFechaAsyyyyMMdd(String fecha){
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Date fechaDate = new Date();
		    try {	
		    	fechaDate = df.parse(fecha);
		    	return fechaDate;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return null;

	    }
	    private String getFechaAsyyyyMMdd(Date fecha){
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    return df.format(fecha);
	    }
	    
	  
	    
	    /**
	     * 
	     * @param dia
	     * @param mes
	     * @param anioEscogido
	     * @return
	     */
	    public String diaSemana (int dia, int mes, int anioEscogido,int tipo_consulta)
	    {
	    	String Valor_dia="";
			try {
				String mesMenor="";
				Valor_dia = "";
				String diaMenor="";

				if (dia<10) {
					diaMenor="0";
				}else {
					diaMenor="";
				}
				
				if (mes<10) {
					mesMenor="0";
				}else {
					mesMenor="";
				}
				

				String fechaHoraBiometrico=anioEscogido+"-"+mesMenor+mes+"-"+diaMenor+dia;
				//Calendario 
				Calendar c = Calendar.getInstance();
				//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				c.setTime(getFechaAsyyyyMMdd(fechaHoraBiometrico)); 
				
				
				int diaSemana = c.get(Calendar.DAY_OF_WEEK);
				
				if (tipo_consulta==0) {
				if (diaSemana == 1) {
				    Valor_dia = "Domingo";
				} else if (diaSemana == 2) {
				    Valor_dia = "Lunes";
				} else if (diaSemana == 3) {
				    Valor_dia = "Martes";
				} else if (diaSemana == 4) {
				    Valor_dia = "Miercoles";
				} else if (diaSemana == 5) {
				    Valor_dia = "Jueves";
				} else if (diaSemana == 6) {
				    Valor_dia = "Viernes";
				} else if (diaSemana == 7) {
				    Valor_dia = "Sabado";
				}
				}else{
					if (diaSemana == 1) {
					    Valor_dia = "D";
					} else if (diaSemana == 2) {
					    Valor_dia = "L";
					} else if (diaSemana == 3) {
					    Valor_dia = "M";
					} else if (diaSemana == 4) {
					    Valor_dia = "Mi";
					} else if (diaSemana == 5) {
					    Valor_dia = "J";
					} else if (diaSemana == 6) {
					    Valor_dia = "V";
					} else if (diaSemana == 7) {
					    Valor_dia = "S";
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error en metodo diaSemana() ");			}
	        return Valor_dia;
	    }



		public Tabla getTab_calendario() {
			return tab_calendario;
		}



		public void setTab_calendario(Tabla tab_calendario) {
			this.tab_calendario = tab_calendario;
		}
	    
		public static String dateMonth(Calendar date){
			 String result="";
			 int month=0;
			 
			 try{
			   month=date.get(Calendar.MONTH);
			 }catch(Exception ex){}
			 switch(month){
			  case 0:
			    {
			      result="Enero";
			      break;
			    }
			  case 1:
			    {
			      result="Febrero";
			      break;
			    }
			  case 2:
			    {
			      result="Marzo";
			      break;
			    }
			  case 3:
			    {
			      result="Abril";
			      break;
			    }
			  case 4:
			    {
			      result="Mayo";
			      break;
			    }
			  case 5:
			    {
			      result="Junio";
			      break;
			    }
			  case 6:
			    {
			      result="Julio";
			      break;
			    }
			  case 7:
			    {
			      result="Agosto";
			      break;
			    }
			  case 8:
			    {
			      result="Septiembre";
			      break;
			    }
			  case 9:
			    {
			      result="Octubre";
			      break;
			    }
			  case 10:
			    {
			      result="Noviembre";
			      break;
			    }
			  case 11:
			    {
			      result="Diciembre";
			      break;
			    }
			  default:
			    {
			      result="Error";
			      break;
			    }
			 }
			 return result;
			}
		
		
		/**
		 * limpia toda la pantalla incluyendo el autocompletar
		 */
		public void limpiar() {
			//Consulta empleado loggeado
			TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			//ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
			tab_tabla.setCondicion("ide_ashme=-1");
			tab_tabla.ejecutarSql();
		   // getCalendario("11",""+utilitario.getAnio(utilitario.getFechaActual()));

			//tab_calendario.setCondicion("ide_geani=10 and ide_sucu=dd");ccccc
			//tab_calendario.ejecutarSql();
			aut_empleados.limpiar();
			utilitario.addUpdate("tab_tabla,tab_calendario,aut_empleados");// limpia y refresca el autocompletar
		}		
	    
		
		
	public void	verFechaMes(AjaxBehaviorEvent evt){
			tab_tabla.modificar(evt);
	
			try {
				String mes=tab_tabla.getValor("IDE_GEMES");
				String anio=tab_tabla.getValor("IDE_GEANI");

				TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
				String anioEscogido=tabAnio.getValor("detalle_geani");
				
				TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
				String mesEscogido=tabMes.getValor("detalle_gemes");
				
				getCalendario(mes,anio);
				tab_calendario.setLectura(true);
			    tab_calendario.setHeader("TABLA FECHAS DEL MES "+mesEscogido+"  DEL "+anioEscogido);
			    tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
				utilitario.addUpdate("tab_calendario,tab_tabla");
			} catch (Exception e) {
				// TODO: handle exception
			}
		

	}

	
public void	editarPorMes(){
	
	/*if (sucu.equals("1") && !empleadoSucursal.equals("513") ) {

		utilitario.agregarMensajeInfo("Empleado no puede realizar esta acci�n", "Por favor contactese con Talento Humano");
		return;
	}*/
bandEdit=true;
	sel_mes_editar.dibujar();
}


	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}



	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}



	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}



	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}



	@Override
	public void buscar() {
		// TODO Auto-generated method stub
		super.buscar();
	}



	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
	}




	public Boolean getEditar() {
		return editar;
	}



	public void setEditar(Boolean editar) {
		this.editar = editar;
	}









	public Dialogo getDia_num_copiar() {
		return dia_num_copiar;
	}



	public void setDia_num_copiar(Dialogo dia_num_copiar) {
		this.dia_num_copiar = dia_num_copiar;
	}
	
	
		
    private TablaGenerica obtenerHorarioMesEmpleado(String empleado,String anio,String mes){
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select ide_ashme,ide_gtemp from asi_horario_mes_empleado "
    			+ "where ide_gemes="+mes+" and ide_geani="+anio+" and ide_gtemp in("+empleado+")");
    	return horarioXEmpleado;
    }



	public SeleccionTabla getSel_mes_editar() {
		return sel_mes_editar;
	}



	public void setSel_mes_editar(SeleccionTabla sel_mes_editar) {
		this.sel_mes_editar = sel_mes_editar;
	}



	public SeleccionTabla getSel_anio_editar() {
		return sel_anio_editar;
	}



	public void setSel_anio_editar(SeleccionTabla sel_anio_editar) {
		this.sel_anio_editar = sel_anio_editar;
	}
	
    
    public void buscarHorario(){
    	
    	String estado=tab_tabla2.getValor("ACTIVO_ASHOR");
    	
    	if (estado.equals("false")) {
			TablaGenerica tabHorario=utilitario.consultar("select emp.ide_gtemp,emp.ide_astur from gth_empleado emp "
					+ "left join asi_turnos_horario tuh on tuh.ide_astur=emp.ide_astur "
					+ "left join asi_horario hor on hor.ide_ashor=tuh.ide_ashor "
					+ "where tuh.ide_ashor="+tab_tabla2.getValorSeleccionado());
		
    	if (tabHorario.getTotalFilas()>0) {
			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=true where ide_ashor="+tab_tabla2.getValorSeleccionado());
			tab_tabla2.actualizar();
			utilitario.agregarMensajeError("No se puede desactivar ", "Hay empleados asociados con este horario");
			return;
		}
    	else {
			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=false where ide_ashor="+tab_tabla2.getValorSeleccionado());
			tab_tabla2.actualizar();
			utilitario.agregarMensajeError("Se ha  desactivado el horario", "Cambio realizado correcto");

		}
    	}else {			
    		
    			TablaGenerica tabHorario=utilitario.consultar("select emp.ide_gtemp,emp.ide_astur from gth_empleado emp "
    					+ "left join asi_turnos_horario tuh on tuh.ide_astur=emp.ide_astur "
    					+ "left join asi_horario hor on hor.ide_ashor=tuh.ide_ashor "
    					+ "where tuh.ide_ashor="+tab_tabla2.getValorSeleccionado());
    		
        	if (tabHorario.getTotalFilas()==0) {
    			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=true where ide_ashor="+tab_tabla2.getValorSeleccionado());
    			tab_tabla2.actualizar();
    			utilitario.agregarMensaje("Se ha activado correctamente ", "Horario sin asignaci�n de horarios");
    			return;
    		}
        
    		
    		
		}
    	
    }
    
    
    private TablaGenerica obtenerNombreEmpleado(String empleado){
    	TablaGenerica nombreEmpleado=utilitario.consultar("SELECT EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
    			+ "FROM  GTH_EMPLEADO EMP  "
    			+ "WHERE EMP.ACTIVO_GTEMP=true and emp.ide_gtemp in("+empleado+")");
    	return nombreEmpleado;
    }



	public Tabla getTab_turnos() {
		return tab_turnos;
	}



	public void setTab_turnos(Tabla tab_turnos) {
		this.tab_turnos = tab_turnos;
	}



	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}



	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
    
	
	public void cargarHorario(SelectEvent evt){
		tab_tabla2.seleccionarFila(evt);
		tab_tabla2.getValorSeleccionado();
		
		if (tab_tabla2.getValorSeleccionado()==null || tab_tabla2.getValorSeleccionado().isEmpty()) {
			utilitario.agregarMensajeInfo("No se ha seleccionado turno", "Debe seleccionar un turno");
			return;
		}
		TablaGenerica tab_horario=utilitario.consultar("select ide_astur,ide_ashor from asi_turnos_horario where ide_astur="+tab_tabla2.getValorSeleccionado());
	
		if (tab_horario.getTotalFilas()<=0 || tab_horario.isEmpty()) {
			utilitario.agregarMensajeInfo("No se han encontrado horarios asignados", "Debe asignar un turno al o los horario(s)");
			return;
		}	
		
		
		int valor=0;
		String horario="";
		
		   StringBuilder str_ide=new StringBuilder();
           for (int i = 0; i < tab_horario.getTotalFilas(); i++) 
			 {
        	  
              str_ide.append(tab_horario.getValor(i, "IDE_ASHOR"));
              valor++;
              if (tab_horario.getTotalFilas()==1) {
			}else if (valor<=tab_horario.getTotalFilas()) {
                  str_ide.append(",");
                  valor=valor+1;
  			}

			 }

           
           

		
		tab_tabla3.setCondicion("ide_ashor in("+str_ide.toString()+")");
		tab_tabla3.ejecutarSql();
		tab_tabla3.actualizar();
	
	}

	
	
    public void consultarDepartamento(){
		
		if (sucu.equals("1")) {
			utilitario.agregarMensajeInfo("Empleado no puede realizar esta acci�n", "Por favor contactese con Talento Humano");
			return;
		}
      	sel_departamento_empleado.dibujar();
      	
      	
}



	public SeleccionTabla getSel_departamento_empleado() {
		return sel_departamento_empleado;
	}



	public void setSel_departamento_empleado(
			SeleccionTabla sel_departamento_empleado) {
		this.sel_departamento_empleado = sel_departamento_empleado;
	}
    
    
    
	 public void aceptarAsignarEmpleadoDepartamento(){
	    	
	    	
  	   StringBuilder str_ide=new StringBuilder();
         int int_num_col_idegedep=sel_departamento_empleado.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
         for (Fila filaActual:sel_departamento_empleado.getTab_seleccion().getSeleccionados()) {
      	   
                 if(str_ide.toString().isEmpty()==false){
                         str_ide.append(",");
                 }
                 str_ide.append(filaActual.getCampos()[int_num_col_idegedep]);
         			}

         	TablaGenerica tabEmpleadosDeparatamento=getEmpleadosDepartamento(str_ide.toString());
         
          
         	
         	StringBuilder str_ide_empleado=new StringBuilder();
          String int_num_col_idegetemp;
          for (int i = 0; i < tabEmpleadosDeparatamento.getTotalFilas(); i++) {
				int_num_col_idegetemp=tabEmpleadosDeparatamento.getValor(i, "IDE_GTEMP");
          	  	if(str_ide_empleado.toString().isEmpty()==false){
                          str_ide_empleado.append(",");
                  }
                  str_ide_empleado.append(int_num_col_idegetemp);
          }
          
          
             
         
         		utilitario.agregarMensajeInfo("Se han Actualizado correctamente los empleados seleccionados", "");
  	
  	
  }

	
	  	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
	  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
	  	}



		public Boolean getBandEdit() {
			return bandEdit;
		}



		public void setBandEdit(Boolean bandEdit) {
			this.bandEdit = bandEdit;
		}



		public String getSucu() {
			return sucu;
		}



		public void setSucu(String sucu) {
			this.sucu = sucu;
		}



		public String getArea() {
			return area;
		}



		public void setArea(String area) {
			this.area = area;
		}



		public String getDepa() {
			return depa;
		}



		public void setDepa(String depa) {
			this.depa = depa;
		}



		public boolean isCopirDepartamento() {
			return copirDepartamento;
		}



		public void setCopirDepartamento(boolean copirDepartamento) {
			this.copirDepartamento = copirDepartamento;
		}



		public String getEmpleado1() {
			return empleado1;
		}



		public void setEmpleado1(String empleado1) {
			this.empleado1 = empleado1;
		}



		public String getSucursal() {
			return sucursal;
		}



		public void setSucursal(String sucursal) {
			this.sucursal = sucursal;
		}



		public AutoCompletar getAut_empleados() {
			return aut_empleados;
		}



		public void setAut_empleados(AutoCompletar aut_empleados) {
			this.aut_empleados = aut_empleados;
		}
	  	
		
		public void cambioHorario(){
			
		}
		
		
		public void seleccionarMes(){
			sel_mes.dibujar();
		}



		@Override
		public void exportarXLS() {
			// TODO Auto-generated method stub
			super.exportarXLS();
		}
		
		
		public void obtenerAnioEditar(){
			  anio=sel_anio_editar.getValorSeleccionado();
			  
			  TablaGenerica tab_jefe_inmediato = utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei "
			  		+ "FROM asi_jefe_inmediato  "
			  		+ " where ide_gtemp in(select ide_gtemp from gen_empleados_departamento_par where ide_geedp ="+ide_geedp_activo+" "
			  		+ "order by ide_geedp desc  limit 1 )");
			  
			  if ( tab_jefe_inmediato.getTotalFilas()<=0) {
				
				  return ;
			}
			  
		        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
		    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
		    			return;
		    		}else {
			   	sel_anio_editar.cerrar();
		    		}

			  if (tab_jefe_inmediato.getValor("tipo_asjei").equals("1")) {
				  tab_tabla.setCondicion("ide_gemes="+mes+" and ide_geani="+anio);	
				  tab_tabla.ejecutarSql();
			 	  getCalendario(mes,anio);
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			  }else {
				  tab_tabla.setCondicion("ide_asjei="+tab_jefe_inmediato.getValor("ide_asjei")+" and ide_gemes="+mes+" and ide_geani="+anio);	
				  tab_tabla.ejecutarSql();
				  getCalendario(mes,anio);
			      utilitario.addUpdate("tab_tabla,sel_mes,tab_calendario");
			}

				}


		@Override
		public void abrirListaReportes() {
			// TODO Auto-generated method stub
			rep_reporte.dibujar();

		}


		@Override
		public void aceptarReporte() {
			if (rep_reporte.getReporteSelecionado().equals("Horario por Empleado")) {
			if (rep_reporte.isVisible()) {
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					sel_mes_reporte.dibujar();
			}else if (sel_mes_reporte.isVisible()){	
				 mes=sel_mes_reporte.getValorSeleccionado();
				  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
			 			return;
			 		}
					sel_mes_reporte.cerrar();
				sel_anio_reporte.dibujar();
			}else if (sel_anio_reporte.isVisible()){
				 anio=sel_anio_reporte.getValorSeleccionado();
			        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
			    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
			    			return;
			    		}else {
				   //	sel_anio_reporte.cerrar();
			    		}
    				p_parametros.put("titulo", "REPORTE EMPLEADOS POR HORARIO");
					p_parametros.put("IDE_GEANI", Integer.parseInt(anio));
					p_parametros.put("IDE_GEMES", Integer.parseInt(mes));
					sel_anio_reporte.cerrar();
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					self_reporte.dibujar();
				}
			}else if(rep_reporte.getReporteSelecionado().equals("Asignacion Mensual de Horarios")){

				if (rep_reporte.isVisible()) {
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					sel_mes_reporte.dibujar();
			}else if (sel_mes_reporte.isVisible()){	
				 mes=sel_mes_reporte.getValorSeleccionado();
				  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
			 			return;
			 		}
					sel_mes_reporte.cerrar();
				sel_anio_reporte.dibujar();
			}else if (sel_anio_reporte.isVisible()){
				 anio=sel_anio_reporte.getValorSeleccionado();
			        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
			    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
			    			return;
			    		}else {
				   //	sel_anio_reporte.cerrar();
			    		}
    			/*	p_parametros.put("titulo", "REPORTE EMPLEADOS POR HORARIO");
					p_parametros.put("IDE_GEANI", Integer.parseInt(anio));
					p_parametros.put("IDE_GEMES", Integer.parseInt(mes));
					sel_anio_reporte.cerrar();
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					self_reporte.dibujar();*/
			        
			        
			        
					
			        try {
						  
						/*if ((mes==null || mes.isEmpty() || mes.equals("") )) {
								utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
								return;
							}
						  
						    if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
								utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
								return;
							}
						  
						p_parametros = new HashMap();
						rep_reporte.cerrar();*/
						TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
						TablaGenerica tabMes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
						TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));

						int anioInicio=Integer.parseInt(tabAnio.getValor("detalle_geani"));
						String fecha=anioInicio+"-"+mes+"-01";
						String fechaAnio="";
						fechaAnio=tabMes.getValor("DETALLE_GEMES").toUpperCase()+" - "+tabAnio.getValor("detalle_geani");
						int diaFin=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
						
						
						String IDE_ASTUR=retornaTurnos(Integer.parseInt(mes),Integer.parseInt(anio),diaFin);

						
						
						for (int i = 1; i < diaFin+1; i++) {
							if (i==1) {
								dia1=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
									p_parametros.put("dia1", dia1);	
									
									System.out.println("dia1"+ dia1);
							}else if (i==2) {
								dia2=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								System.out.println("dia1"+ dia1);

								p_parametros.put("dia2", dia2);	
							}else if (i==3) {
								dia3=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								System.out.println("dia3"+ dia3);

								p_parametros.put("dia3", dia3);	
							}else if (i==4) {
								dia4=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia4", dia4);	
								System.out.println("dia4"+ dia4);

							}else if (i==5) {
								dia5=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia5", dia5);	
								System.out.println("dia5"+ dia5);

							}else if (i==6) {
								dia6=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia6", dia6);	
								System.out.println("dia6"+ dia6);

							}else if (i==7) {
								dia7=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia7", dia7);	
								System.out.println("dia7"+ dia7);

							}else if (i==8) {
								dia8=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia8", dia8);	
								System.out.println("dia8"+ dia8);

							}else if (i==9) {
								dia9=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia9", dia9);	
								System.out.println("dia9"+ dia9);

							}else if (i==10) {
								dia10=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia10", dia10);
								System.out.println("dia10"+ dia10);

							}else if (i==11) {
								dia11=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia11", dia11);
								System.out.println("dia11"+ dia11);

							}else if (i==12) {
								dia12=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia12", dia12);	
								System.out.println("dia12"+ dia12);

							}else if (i==13) {
								dia13=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia13", dia13);	
								System.out.println("dia13"+ dia13);

							}else if (i==14) {
								dia14=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia14", dia14);	
								System.out.println("dia14"+ dia14);

							}else if (i==15) {
								dia15=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia15", dia15);	
								System.out.println("dia15"+ dia15);

							}else if (i==16) {
								dia16=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia16", dia16);	
								System.out.println("dia16"+ dia16);

							}else if (i==17) {
								dia17=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia17", dia17);	
								System.out.println("dia17"+ dia17);

							}else if (i==18) {
								dia18=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia18", dia18);	
								System.out.println("dia18"+ dia18);

							}else if (i==19) {
								dia19=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia19", dia19);	
								System.out.println("dia19"+ dia19);

							}else if (i==20) {
								dia20=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia20", dia20);	
								System.out.println("dia20"+ dia20);

							}else if (i==21) {
								dia21=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia21", dia21);
								System.out.println("dia21"+ dia1);

							}else if (i==22) {
								dia22=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia22", dia22);	
								System.out.println("dia22"+ dia22);

							}else if (i==23) {
								dia23=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia23", dia23);	
								System.out.println("dia23"+ dia23);

							}else if (i==24) {
								dia24=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia24", dia24);
								System.out.println("dia24"+ dia24);

							}else if (i==25) {
								dia25=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia25", dia25);	
								System.out.println("dia25"+ dia25);

							}else if (i==26) {
								dia26=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia26", dia26);	
								System.out.println("dia26"+ dia26);

							}else if (i==27) {
								dia27=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia27", dia27);	
								System.out.println("dia27"+ dia27);

							}else if (i==28) {
								dia28=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia28", dia28);	
								System.out.println("dia28"+ dia28);

							}else if (i==29) {
								dia29=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia29", dia29);
								System.out.println("dia29"+ dia29);

							}else if (i==30) {
								dia30=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia30", dia30);	
								System.out.println("dia30"+ dia30);

							}else if (i==31) {
								dia31=diaSemana(i, Integer.parseInt(mes), anioInicio,1);
								p_parametros.put("dia31", dia31);
								System.out.println("dia31"+ dia31);

							}
						}
						
						if (dia29==null || dia29.equals("")) {
							p_parametros.put("dia29", "S/D");	
							System.out.println("dia29"+ dia29);

						}else if(dia30==null || dia30.equals("")) {
							p_parametros.put("dia30", "S/D");	
							System.out.println("dia30"+ dia30);

						}else if(dia31==null || dia31.equals("")) {
							p_parametros.put("dia31", "S/D");	
							System.out.println("dia31"+ dia31);

						}
						TablaGenerica TabEmpleado=utilitario.consultar("select * from gen_empleados_departamento_par par "
								+ "left join gth_empleado emp on emp.ide_gtemp=par.ide_gtemp "
								+ "left join gen_partida_grupo_cargo gepgc on gepgc.ide_gepgc=par.ide_gepgc "
								+ "where par.ide_geedp="+tabEmpDep.getValor("ide_geedp"));
						
						
						
						String nomEmpleado=retornaNombreEmpleado(Integer.parseInt(TabEmpleado.getValor("IDE_GTEMP")));
						//String nomEmpleadoJefe=retornaNombreEmpleado(Integer.parseInt(getJefesInmediatos(ide_geare,0)));
						String nomEmpleadoJefe=retornaNombreEmpleado(Integer.parseInt(jefe_padre));
						
						p_parametros.put("IDE_GEANI", Integer.parseInt(anio));	
						System.out.println("ANIO"+ anio);

						p_parametros.put("IDE_GEMES", Integer.parseInt(mes));
						System.out.println("MES"+ mes);

					//	System.out.println("juan : : "+tabEmpDep.getValor("ide_geedp"));
						
		
						
						
						
						/*if( tabEmpDep.getValor("ide_geedp").equals(utilitario.getVariable("p_administrador_horarios_empleados"))){
							String ide_geedp="";
							TablaGenerica tab_ide_geedp=utilitario.consultar("SELECT ide_geedp, ide_geedp "
									+ "FROM asi_horario_mes_empleado  "
									+ "where ide_geani in("+anio+") and ide_gemes in("+mes+") "
									+ "group by ide_geedp ");
							if (tab_ide_geedp.getTotalFilas()>0) {
								for (int j = 0; j < tab_ide_geedp.getTotalFilas(); j++) {
									if (j==(tab_ide_geedp.getTotalFilas()-1)) {
										ide_geedp+=tab_ide_geedp.getValor(j,"ide_geedp");
									}else{
										ide_geedp+=tab_ide_geedp.getValor(j,"ide_geedp");
										ide_geedp+=",";

									}
								}
								p_parametros.put("IDE_GEEDP", ide_geedp);

							}else{
								p_parametros.put("IDE_GEEDP", "-1");

							}


						}else{
						  	p_parametros.put("IDE_GEEDP", tabEmpDep.getValor("ide_geedp"));
						}
						System.out.println("IDE_GEEDP "+ tabEmpDep.getValor("ide_geedp"));
*/
					  	p_parametros.put("ide_asjei", ide_asjei);

						
						
						p_parametros.put("mesAnio ",fechaAnio);
						System.out.println("mesAnio "+ fechaAnio);

						p_parametros.put("titulo", "REPORTE ASIGNACION DE HORARIO MENSUAL");
						p_parametros.put("elaborado_por", nomEmpleado);	
						System.out.println("elaborado_por"+ nomEmpleado);

						p_parametros.put("cargo_elaborado", TabEmpleado.getValor("titulo_cargo_gepgc"));	
						System.out.println("cargo_elaborado"+ TabEmpleado.getValor("titulo_cargo_gepgc"));

						p_parametros.put("IDE_ASTUR", IDE_ASTUR);	
						System.out.println("IDE_ASTUR"+ IDE_ASTUR);

						p_parametros.put("aprobado_por", nomEmpleadoJefe);	
						System.out.println("aprobado_por "+ nomEmpleadoJefe);

						p_parametros.put("cargo_aprobado", cargo_padre);
						System.out.println("aprobado_por "+ getJefesInmediatos(TabEmpleado.getValor("IDE_GEARE"),1));

						sel_anio_reporte.cerrar();
System.out.println("PARAMETROS "+p_parametros.toString());
						
						self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						self_reporte.dibujar();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						System.out.println("problemae en repporte de permisos mensual");				
						}
					
					
					
					
					
				}

				
				
				
				
			}
			
		}


		public Reporte getRep_reporte() {
			return rep_reporte;
		}


		public void setRep_reporte(Reporte rep_reporte) {
			this.rep_reporte = rep_reporte;
		}


		public SeleccionFormatoReporte getSelf_reporte() {
			return self_reporte;
		}


		public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
			this.self_reporte = self_reporte;
		}


		public Map getP_parametros() {
			return p_parametros;
		}


		public void setP_parametros(Map p_parametros) {
			this.p_parametros = p_parametros;
		}

		
		    	
	public void obtenerAnioReporte(){
		  anio=sel_anio_reporte.getValorSeleccionado();
	        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
	    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
	    			return;
	    		}else {
		   //	sel_anio_reporte.cerrar();
	    		}
			}
    
    public void obtenerMesReporte(){
		  int anioActual=0;

    mes=sel_mes_reporte.getValorSeleccionado();
			  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		 			return;
		 		}else {
		 		
		 			//  sel_mes_reporte.cerrar();	
				    //  sel_anio_reporte.dibujar();
		 			
		 		}
		 			
		 			
	
    }


	public SeleccionTabla getSel_mes_reporte() {
		return sel_mes_reporte;
	}


	public void setSel_mes_reporte(SeleccionTabla sel_mes_reporte) {
		this.sel_mes_reporte = sel_mes_reporte;
	}


	public SeleccionTabla getSel_anio_reporte() {
		return sel_anio_reporte;
	}


	public void setSel_anio_reporte(SeleccionTabla sel_anio_reporte) {
		this.sel_anio_reporte = sel_anio_reporte;
	}


	public String getDia1() {
		return dia1;
	}


	public void setDia1(String dia1) {
		this.dia1 = dia1;
	}


	public String getDia2() {
		return dia2;
	}


	public void setDia2(String dia2) {
		this.dia2 = dia2;
	}


	public String getDia3() {
		return dia3;
	}


	public void setDia3(String dia3) {
		this.dia3 = dia3;
	}


	public String getDia4() {
		return dia4;
	}


	public void setDia4(String dia4) {
		this.dia4 = dia4;
	}


	public String getDia5() {
		return dia5;
	}


	public void setDia5(String dia5) {
		this.dia5 = dia5;
	}


	public String getDia6() {
		return dia6;
	}


	public void setDia6(String dia6) {
		this.dia6 = dia6;
	}


	public String getDia7() {
		return dia7;
	}


	public void setDia7(String dia7) {
		this.dia7 = dia7;
	}


	public String getDia8() {
		return dia8;
	}


	public void setDia8(String dia8) {
		this.dia8 = dia8;
	}


	public String getDia9() {
		return dia9;
	}


	public void setDia9(String dia9) {
		this.dia9 = dia9;
	}


	public String getDia10() {
		return dia10;
	}


	public void setDia10(String dia10) {
		this.dia10 = dia10;
	}


	public String getDia11() {
		return dia11;
	}


	public void setDia11(String dia11) {
		this.dia11 = dia11;
	}


	public String getDia12() {
		return dia12;
	}


	public void setDia12(String dia12) {
		this.dia12 = dia12;
	}


	public String getDia13() {
		return dia13;
	}


	public void setDia13(String dia13) {
		this.dia13 = dia13;
	}


	public String getDia14() {
		return dia14;
	}


	public void setDia14(String dia14) {
		this.dia14 = dia14;
	}


	public String getDia15() {
		return dia15;
	}


	public void setDia15(String dia15) {
		this.dia15 = dia15;
	}


	public String getDia16() {
		return dia16;
	}


	public void setDia16(String dia16) {
		this.dia16 = dia16;
	}


	public String getDia17() {
		return dia17;
	}


	public void setDia17(String dia17) {
		this.dia17 = dia17;
	}


	public String getDia18() {
		return dia18;
	}


	public void setDia18(String dia18) {
		this.dia18 = dia18;
	}


	public String getDia19() {
		return dia19;
	}


	public void setDia19(String dia19) {
		this.dia19 = dia19;
	}


	public String getDia20() {
		return dia20;
	}


	public void setDia20(String dia20) {
		this.dia20 = dia20;
	}


	public String getDia21() {
		return dia21;
	}


	public void setDia21(String dia21) {
		this.dia21 = dia21;
	}


	public String getDia22() {
		return dia22;
	}


	public void setDia22(String dia22) {
		this.dia22 = dia22;
	}


	public String getDia23() {
		return dia23;
	}


	public void setDia23(String dia23) {
		this.dia23 = dia23;
	}


	public String getDia24() {
		return dia24;
	}


	public void setDia24(String dia24) {
		this.dia24 = dia24;
	}


	public String getDia25() {
		return dia25;
	}


	public void setDia25(String dia25) {
		this.dia25 = dia25;
	}


	public String getDia26() {
		return dia26;
	}


	public void setDia26(String dia26) {
		this.dia26 = dia26;
	}


	public String getDia27() {
		return dia27;
	}


	public void setDia27(String dia27) {
		this.dia27 = dia27;
	}


	public String getDia28() {
		return dia28;
	}


	public void setDia28(String dia28) {
		this.dia28 = dia28;
	}


	public String getDia29() {
		return dia29;
	}


	public void setDia29(String dia29) {
		this.dia29 = dia29;
	}


	public String getDia30() {
		return dia30;
	}


	public void setDia30(String dia30) {
		this.dia30 = dia30;
	}


	public String getDia31() {
		return dia31;
	}


	public void setDia31(String dia31) {
		this.dia31 = dia31;
	}
    
    
	public String retornaNombreEmpleado(int IDE_GTEMP) {
		String detallePermiso = "";
		String nombreTipoSolicitud;
		String nroHoras;
		String motivo;
		String obtieneNroDias;
		// Estructura de mensaje
		String strNombreEmpleado = "";
		// obtengo el empleado del cual requiero los datos
		TablaGenerica tab_empleado = ser_empleado1.getEmpleado(""+IDE_GTEMP);
		String documento = tab_empleado.getValor("documento_identidad_gtemp");
		String primer_nombre_empleado = tab_empleado.getValor("primer_nombre_gtemp").toString();
		String segundo_nombre_empleado = tab_empleado.getValor("segundo_nombre_gtemp").toString();
		String apellido_paterno_empleado = tab_empleado.getValor("apellido_paterno_gtemp").toString();
		String apellido_materno_empleado = tab_empleado.getValor("apellido_materno_gtemp").toString();
		strNombreEmpleado = ucFirst(primer_nombre_empleado) + " " + ucFirst(apellido_paterno_empleado);
		return strNombreEmpleado;

	}
	public static String ucFirst(String str) {
	    if (str.isEmpty()) {
	        return str;            
	    } else {
	        return Character.toUpperCase(str.charAt(0)) + str.substring(1); 
	    }
	}
	
	public String  getJefesInmediatos(String IDE_GEARE, int tipo) {
		/*TablaGenerica tab_Jefe=utilitario.consultar(
				"SELECT EPAR.IDE_GEEDP,   EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,EMP.IDE_GTEMP,gepgc.titulo_cargo_gepgc   "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE    "
				+ "left join gen_partida_grupo_cargo gepgc on gepgc.ide_gepgc=EPAR.ide_gepgc "
				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE   "
				+ "and epar.ide_gegro in(6,4,7,2,8) and epar.activo_geedp=true  "
				+ "and epar.ide_geare="+IDE_GEARE
				+ " group by EPAR.IDE_GEEDP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.IDE_GTEMP,gepgc.titulo_cargo_gepgc "
				+ "order by APELLIDO_PATERNO_GTEMP  ");*/
		
		
		TablaGenerica tab_Jefe=utilitario.consultar(
				" SELECT EPAR.IDE_GEEDP,   EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,EMP.IDE_GTEMP,gepgc.titulo_cargo_gepgc   "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE    "
				+ "left join gen_partida_grupo_cargo gepgc on gepgc.ide_gepgc=EPAR.ide_gepgc  "
				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE   "
				+ " and epar.ide_geare="+IDE_GEARE
				+ " and epar.ide_gegro in(6,4,7,2,8,5)  "
				
				//6,7,4,8,1,19
				+ "group by EPAR.IDE_GEEDP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.IDE_GTEMP,gepgc.titulo_cargo_gepgc "
				+ "order by APELLIDO_PATERNO_GTEMP  ,EPAR.IDE_GEEDP ASC LIMIT 1");

if (tipo==0) {
	return tab_Jefe.getValor("IDE_GTEMP");

}else {
	return tab_Jefe.getValor("titulo_cargo_gepgc");

}
	}
	
	
	 
	public String retornaTurnos(int ide_gemes, int ide_geani, int diaFin ){
		String sql="";
		int dia=1;
		StringBuilder turnos = new StringBuilder();
		TablaGenerica tabTurnos=null;
		for (int i = 1; i <diaFin+1; i++) {
		sql="SELECT case when dia"+i+" is null then 19 else  dia"+i+" end as dia"+i+",case when dia"+i+" is null then 19 else  dia"+i+" end as dia"+i+" "
				+ "FROM asi_horario_mes_empleado  "
				+ "where ide_gemes="+ide_gemes+" and ide_geani ="+ide_geani+"  "
				+ "group by dia"+i+" ";
		tabTurnos=utilitario.consultar(sql);
		for (int x = 0; x <tabTurnos.getTotalFilas(); x++) {
			if (x==(tabTurnos.getTotalFilas()-1)) {
			turnos.append(tabTurnos.getValor(x,"dia"+i));
			}else{
		//  if (dia>1) {
	//		turnos.append(",");
		//	dia++;
		//	}else if(dia==1){
		//		if (i>1) {
		//			turnos.append(",");
					//dia++;
	//			}	
		//		}
			turnos.append(tabTurnos.getValor(x,"dia"+i));
			turnos.append(",");
			} 
		}
		if (i==diaFin) {
			
		}else{
		turnos.append(",");
		}
		if (dia==1) {
			
		}else{
			dia--;
		}
		
		}
		
		return turnos.toString();
	}
	
	
	 
	  
	  
	  
	  
	  
	  
}
