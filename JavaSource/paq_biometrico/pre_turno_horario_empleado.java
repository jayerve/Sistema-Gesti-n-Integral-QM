/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.text.TabableView;

import org.primefaces.event.SelectEvent;

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
public class pre_turno_horario_empleado extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();    
	private Tabla tab_consulta_turno_empleado=new Tabla();
	private Tabla tab_operativo=new Tabla();
	private Tabla tab_consulta_empleado=new Tabla();
	private boolean bandActualizoOperativo=false;
	

    private Combo com_tipo_hora= new Combo();
    private SeleccionTabla sel_departamento_empleado = new SeleccionTabla();
    private SeleccionTabla set_empleado = new SeleccionTabla();
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private boolean bandEliminarDepartamento=false;
	private boolean bandEliminarEmpleado=false;
	private boolean bandOperativo=false;
	private boolean bandMatriz=false;

	private boolean banderaTipoTurno=false;
	
	private boolean bandAsignar=false,bandEliminar=false;
	int ide_mes=0,ide_anio=0;
    public pre_turno_horario_empleado() {
    	
    	
    	bar_botones.agregarComponente(new Etiqueta("Escoga Tipo Turno :"));

    	
     	com_tipo_hora.setCombo(" SELECT turno_matriz_astur, "
     			+ "(case when turno_matriz_astur is false then 'OPERATIVO' else 'MATRIZ' end)  "
     			+ "as TIPO_TURNO from  ASI_TURNOS  "
     			+ " where turno_matriz_astur is not null "
     			+ "group by turno_matriz_astur");
    	com_tipo_hora.setMetodo("cambioTipoHora");
    	
    	bar_botones.agregarComponente(com_tipo_hora);    
    	
    	
    	
		Boton bot_limpiar= new Boton();
		bot_limpiar.setIcon("ui-icon-calculator");
		bot_limpiar.setMetodo("limpiar");
		bot_limpiar.setValue("LIMPIAR");
		bot_limpiar.setTitle("LIMPIAR");
		bar_botones.agregarBoton(bot_limpiar);
		
    	// boton Asignar Turno Empleado
		Boton bot_asignar = new Boton();
		bot_asignar.setIcon("ui-icon-calculator");
		bot_asignar.setMetodo("consultarDepartamento");
		bot_asignar.setValue("Asignar Dapartamento");
		bot_asignar.setTitle("Asignar Departamento");
		bar_botones.agregarBoton(bot_asignar);
		
    	// boton Asignar Turno Empleado
		Boton bot_asignar_empleado = new Boton();
		bot_asignar_empleado.setIcon("ui-icon-calculator");
		bot_asignar_empleado.setMetodo("consultarEmpleado");
		bot_asignar_empleado.setValue("Asignar Empleado");
		bot_asignar_empleado.setTitle("Asignar Empleado");
		bar_botones.agregarBoton(bot_asignar_empleado);
    	

		
    	// boton Asignar Turno Empleado
		Boton bot_eliminar_empleado = new Boton();
		bot_eliminar_empleado.setIcon("ui-icon-calculator");
		bot_eliminar_empleado.setMetodo("eliminarEmpleado");
		bot_eliminar_empleado.setValue("Eliminar Empleado");
		bot_eliminar_empleado.setTitle("Eliminar Empleado");
		bar_botones.agregarBoton(bot_eliminar_empleado);
    	
		
		
		
		Boton bot_eliminar_empleado_departamento = new Boton();
		bot_eliminar_empleado_departamento.setIcon("ui-icon-calculator");
		bot_eliminar_empleado_departamento.setMetodo("eliminarDepartamento");
		bot_eliminar_empleado_departamento.setValue("Eliminar Dapartamento");
		bot_eliminar_empleado_departamento.setTitle("Eliminar Departamento");
		bar_botones.agregarBoton(bot_eliminar_empleado_departamento);
		
		

    	
		
		sel_departamento_empleado.setId("sel_departamento_empleado");
		//sel_departamento_empleado.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO", "IDE_GEDEP");
		
		sel_departamento_empleado.setSeleccionTabla("SELECT DEPA.IDE_GEDEP,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,SUCU.NOM_SUCU FROM gen_departamento_sucursal DESUC "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=DESUC.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=DESUC.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DESUC.IDE_GEARE  " 
				+ "where sucu.ide_sucu=1  and  depa.IDE_GEDEP !=35 " 
				+ "	ORDER BY DETALLE_GEARE ASC", "IDE_GEDEP");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
		sel_departamento_empleado.getTab_seleccion().getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
		sel_departamento_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);

		sel_departamento_empleado.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(sel_departamento_empleado);
		sel_departamento_empleado.getBot_aceptar().setMetodo("aceptarAsignarEmpleadoDepartamento");
		sel_departamento_empleado.setWidth("60");
		sel_departamento_empleado.setHeight("60");
		agregarComponente(sel_departamento_empleado);
    	

		set_empleado.setId("set_empleado");

		agregarComponente(set_empleado);

		
		
		  tab_tabla1.setId("tab_tabla1");
    	  tab_tabla1.setTabla("ASI_TURNOS", "IDE_ASTUR", 1);
          tab_tabla1.getColumna("IDE_GTGRE").setCombo("SELECT IDE_GTGRE,DETALLE_GTGRE FROM gth_grupo_empleado ORDER BY DETALLE_GTGRE ASC ");
          tab_tabla1.getColumna("IDE_GTGRE").setNombreVisual("TIPO DE TURNO");
          tab_tabla1.getColumna("IDE_GTGRE").setOrden(2);

          tab_tabla1.getColumna("NOM_ASTUR").setNombreVisual("NOMBRE");
          tab_tabla1.getColumna("NOM_ASTUR").setOrden(1);
          tab_tabla1.getColumna("NOM_ASTUR").setLongitud(15);

          
          tab_tabla1.getColumna("MINUTO_TOLERANCIA_ASTUR").setNombreVisual("TOLERANCIA_MIN");
          tab_tabla1.getColumna("MINUTO_TOLERANCIA_ASTUR").setOrden(3);
          tab_tabla1.getColumna("MINUTO_TOLERANCIA_ASTUR").setVisible(false);

          tab_tabla1.getColumna("ACTIVO_ASTUR").setCheck();
          tab_tabla1.getColumna("ACTIVO_ASTUR").setValorDefecto("true");
          tab_tabla1.getColumna("ACTIVO_ASTUR").setNombreVisual("ACTIVO");
          tab_tabla1.getColumna("ACTIVO_ASTUR").setOrden(4);
          tab_tabla1.getColumna("ACTIVO_ASTUR").setVisible(false);
          tab_tabla1.getColumna("IDE_ASHOR").setVisible(false);
          tab_tabla1.getColumna("IDE_ASHOR").setOrden(5);

          tab_tabla1.getColumna("turno_matriz_astur").setVisible(false);
  		  tab_tabla1.setStyle("width:100%;");
  		  tab_tabla1.setCondicion("ide_astur=-1");
          tab_tabla1.setHeader("ASIGNACIÓN TURNOS A EMPLEADOS");
          tab_tabla1.onSelect("getConsultaHorarioMesEvento");

          tab_tabla1.agregarRelacion(tab_tabla3);
          //tab_tabla1.agregarRelacion(tab_consulta);
          //tab_tabla1.agregarRelacion(tab_tabla3);

          tab_tabla1.dibujar();          
          PanelTabla pat_panel1 = new PanelTabla();
          pat_panel1.setPanelTabla(tab_tabla1);
      
      
        tab_tabla3.setId("tab_tabla3");
       tab_tabla3.setTabla("ASI_TURNOS_HORARIO", "IDE_ASTUH", 2);
       tab_tabla3.getColumna("IDE_ASTUH").setVisible(false);
       
        tab_tabla3.getColumna("IDE_ASHOR").setCombo("SELECT hor.ide_ashor,hor.nom_ashor, "
        		+ "hor.hora_inicial_ashor, "
        		+ "hor.hora_final_ashor, "
        		+ "hor.hora_inicio_almuerzo_ashor, "
        		+ "hor.hora_fin_almuerzo_ashor, "
        		+ "hor.min_almuerzo_ashor "
        		+ "FROM ASI_HORARIO hor " );

        tab_tabla3.getColumna("IDE_ASHOR").setAutoCompletar();
        tab_tabla3.getColumna("IDE_ASHOR").setLongitud(350);

        tab_tabla3.getColumna("IDE_ASHOR").setNombreVisual("HORARIO");
        tab_tabla3.getColumna("IDE_ASTUR").setVisible(false);
        tab_tabla3.setHeader("ASIGNACIÓN DE HORARIOS");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        
        
        
        
        int anio= utilitario.getAnio(utilitario.getFechaActual());
        int mes =utilitario.getAnio(utilitario.getFechaActual());
		int dia=utilitario.getDia(utilitario.getFechaActual());
		
		
		

        
        tab_consulta_empleado.setId("tab_consulta_empleado");
		tab_consulta_empleado.setSql(getConsultaHorarioMesEmpleado());
    	tab_consulta_empleado.setCampoPrimaria("ide_gtemp");

		tab_consulta_empleado.setLectura(true);
    	tab_consulta_empleado.setCondicion("ide_gtemp=-1");

        tab_consulta_empleado.setNumeroTabla(4);
        TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(utilitario.getFechaActual()));
		String mesEscogido=tabMes.getValor("detalle_gemes");
		

        tab_consulta_empleado.setHeader("PERSONAL OPERATIVO MATRIZ "+mesEscogido+" DEL "+utilitario.getAnio(utilitario.getFechaActual()));
        tab_consulta_empleado.setRows(20);

        tab_consulta_empleado.dibujar();
        PanelTabla pat_tab=new PanelTabla();
		pat_tab.setPanelTabla(tab_consulta_empleado);
	    
	
		/*
		tab_consulta_turno_empleado.setId("tab_consulta_turno_empleado");
		   	tab_consulta_turno_empleado.setSql("select  "
				+ "'' as evento, "
				+ "'' as dia,  "
				+ "'' as turno  "
				+ "from asi_horario_mes_empleado");
    	
    	tab_consulta_turno_empleado.getColumna("evento").setLongitud(10);
    	tab_consulta_turno_empleado.getColumna("evento").setNombreVisual("FECHA");

    	tab_consulta_turno_empleado.getColumna("dia").setLongitud(10);
    	tab_consulta_turno_empleado.getColumna("dia").setNombreVisual("DÍA");

    	tab_consulta_turno_empleado.getColumna("turno").setLongitud(85);
    	tab_consulta_turno_empleado.getColumna("turno").setNombreVisual("TURNO");

    	tab_consulta_turno_empleado.setLectura(true);
    	tab_consulta_turno_empleado.setNumeroTabla(5);
    	tab_consulta_turno_empleado.dibujar();
    	
    	
    	tab_consulta_turno_empleado.setHeader("HORARIO POR EMPLEADO "+mesEscogido+" AÑO "+utilitario.getAnio(utilitario.getFechaActual()));

		PanelTabla pat_tab1=new PanelTabla();
		pat_tab1.setPanelTabla(tab_consulta_turno_empleado);
	    */
        Division div_div1 = new Division();
        div_div1.dividir2(pat_panel1, pat_panel3, "70%", "V");     
        Division div_div2 = new Division();
        div_div2.dividir1(pat_tab);   
        Division div_division = new Division();        
        div_division.dividir2(div_div1,div_div2,"50%","H");
        agregarComponente(div_division);

    
    }
    
    
    
  
    
    public void eliminarEmpleado(){
    	//Si quiere eliminar empleado bandera se pone en true
       	bandAsignar=false;
    	bandEliminar=true;
    	bandEliminarEmpleado=true;
   	 	bandEliminarDepartamento=false;
	   tab_tabla1.setLectura(false);
	   if (tab_tabla1.getTotalFilas()<=0) {
		utilitario.agregarMensajeInfo("Debe seleccionar un empleado","");
			   return;
		}
   	 TablaGenerica getHorarioEmpleado=utilitario.consultar("select ide_astuh,ide_astur from asi_turnos_horario where ide_astuh="+tab_tabla3.getValorSeleccionado());
   	utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null  where ide_GTEMP in(" +tab_consulta_empleado.getValorSeleccionado()+")");
		guardarPantalla();
		tab_consulta_empleado.setSql("select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE EMP.IDE_ASTUR=1 and EPAR.ACTIVO_GEEDP=TRUE  "
				+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
		tab_consulta_empleado.ejecutarSql();
		utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado");  			
	

    }
    
 public void eliminarDepartamento(){
 	//Si quiere eliminar empleado por departamento bandera se pone en true

	 bandEliminarDepartamento=true;
 	bandEliminarEmpleado=false;
	sel_departamento_empleado.dibujar();

 	

		
 }
    
    public void consultarEmpleado(){
    	bandAsignar=true;
    	bandEliminar=false;
    	   tab_tabla1.setLectura(false);
    	   
    	      
    			TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    	    			+ "'%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");
    			int anioEmpleado=Integer.parseInt(tab_anio.getValor("ide_geani"));    			
    			
    			
    			TablaGenerica tabEmpleado=utilitario.consultar("select emp.ide_gtemp, emp.apellido_paterno_gtemp  "
    			+ "from gth_empleado emp   "
    			+ " where emp.activo_gtemp in (true) and ide_astur=1 "
    			+ "order by apellido_paterno_gtemp");


    			
    			
    			
    	    	StringBuilder str_ide_empleado=new StringBuilder();
    	     String int_num_col_idegetemp;
    	     for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) {
    				int_num_col_idegetemp=tabEmpleado.getValor(i, "IDE_GTEMP");
    	     	  	if(str_ide_empleado.toString().isEmpty()==false){
    	                     str_ide_empleado.append(",");
    	             }
    	             str_ide_empleado.append(int_num_col_idegetemp);
    	     }
    	     

 			
 			TablaGenerica tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
 					+ "from asi_horario_mes_empleado  "
 					+ " where ide_geani="+anioEmpleado);
    			         
    	 		StringBuilder str_ide_empleado_mensual=new StringBuilder();
    	 		String int_num_col_idegetempmensual;
    	 		for (int i = 0; i < tabEmpleadoMensual.getTotalFilas(); i++) {
    	 			int_num_col_idegetempmensual=tabEmpleadoMensual.getValor(i, "IDE_GTEMP");
    	  	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
    	  	  	str_ide_empleado_mensual.append(",");
    	          }
    	  	  str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
    	  }

    	 		
    	 		
    

    	 		String sql="";
    	 		String empleadosMatrizYMensual="";

    	 		
    	 		boolean datos_empleado=false,datos_empleado_matriz=false;
    	 		 empleadosMatrizYMensual="";

    	 		if (!str_ide_empleado.toString().equals("")) {
    				datos_empleado=true;
    			}
    	 		if (!str_ide_empleado_mensual.toString().equals("")) {
    	 			datos_empleado_matriz=true;
    			}
    	 		
    	 		if (datos_empleado==true) {
    	 	 		empleadosMatrizYMensual=str_ide_empleado.toString();
    	 	 		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
        					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
        					+ "emp.documento_identidad_gtemp "
        					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
        					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
        					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
        					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
        					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
        					+ "WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in("+empleadosMatrizYMensual+") and sucu.ide_sucu=1 "
        					+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
        					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
        					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
    			}
    	 
    	 		if (datos_empleado_matriz==true) {
    	 	 		empleadosMatrizYMensual=str_ide_empleado_mensual.toString();
    		 		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
        					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
        					+ "emp.documento_identidad_gtemp "
        					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
        					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
        					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
        					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
        					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
        					+ "WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in("+empleadosMatrizYMensual+") and sucu.ide_sucu=1 "
        					+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
        					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
        					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
    			}
    	 
    	 		
    	 		if (datos_empleado==true  &&  datos_empleado_matriz==true) {
    	 	 		empleadosMatrizYMensual=str_ide_empleado.toString()+","+str_ide_empleado_mensual.toString();
    		 		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
        					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
        					+ "emp.documento_identidad_gtemp "
        					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
        					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
        					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
        					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
        					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
        					+ "WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in("+empleadosMatrizYMensual+") and sucu.ide_sucu=1 "
        					+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
        					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
        					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
    			}
    	 
    	
    	 		
    	 		
    	 		if (datos_empleado==false  &&  datos_empleado_matriz==false) {
    		 		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
        					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
        					+ "emp.documento_identidad_gtemp "
        					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
        					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
        					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
        					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
        					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
        					+ "WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and sucu.ide_sucu=1 "
        					+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
        					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
        					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
    	 		}
    	 		
    	 		set_empleado.setSeleccionTabla(sql,"IDE_GTEMP");
    	 		
    	 		    
    	 		set_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
    	 		set_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
    	 		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
    			
    			//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
    			set_empleado.setHeight("50");
    			set_empleado.setWidth("70");

    			set_empleado.setTitle("Seleccione un Empleado");
    			gru_pantalla.getChildren().add(set_empleado);
    			set_empleado.getBot_aceptar().setMetodo("aceptarEliminarTurnoEmpleado");
    		   set_empleado.dibujar();
    		   set_empleado.redibujar();
    		   utilitario.addUpdate("set_empleado");
    	   
		
		}
    	   
    
    
    public void aceptarEliminarTurnoEmpleado(){
    
    	
    	if (bandAsignar==true) {
        	aceptarAsignarEmpleado();
		}else if (bandEliminar==true) {
	    	aceptarEliminarEmpleado();
		}
    }
    
    
    public void aceptarAsignarEmpleado(){
    	//Asignar horario a empleado
       
    	
    	
    	
    	StringBuilder str_ide=new StringBuilder();
           int int_num_col_idegtemp=set_empleado.getTab_seleccion().getNumeroColumna("IDE_GTEMP");

           for (Fila filaActual:set_empleado.getTab_seleccion().getSeleccionados()) {
                   if(str_ide.toString().isEmpty()==false){
                           str_ide.append(",");
                   }
                   str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
           }

    
           
           
           
           
      		   if (str_ide.toString()==null || str_ide.toString().equals("") || str_ide.toString().isEmpty() ) {
       			utilitario.agregarMensajeInfo("No se ha seleccionado empleado", "");
       	 	   return;
			}else {
				
			
       	utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=1 where ide_GTEMP in(" +str_ide+")");
   		set_empleado.cerrar();		

		tab_consulta_empleado.actualizar();
			
			
			}
   			guardarPantalla();
   			utilitario.agregarMensajeInfo("Se han Actualizado correctamente los empleados seleccionados", "");
   		
   			
    }
    
    
    public void aceptarEliminarEmpleado(){
    	//Asignar horario a empleado
       
    	
    	
    	
    	StringBuilder str_ide=new StringBuilder();
           int int_num_col_idegtemp=set_empleado.getTab_seleccion().getNumeroColumna("IDE_GTEMP");

           for (Fila filaActual:set_empleado.getTab_seleccion().getSeleccionados()) {
                   if(str_ide.toString().isEmpty()==false){
                           str_ide.append(",");
                   }
                   str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
         
           }

    	   if (str_ide.toString()==null || str_ide.toString().equals("") || !str_ide.toString().isEmpty() ) {
      			utilitario.agregarMensajeInfo("No se ha seleccionado empleado", "");
      	 	   return;
			}else {
				
			   TablaGenerica getHorarioEmpleado=utilitario.consultar("select ide_astuh,ide_astur from asi_turnos_horario where ide_astuh="+tab_tabla3.getValorSeleccionado());
              	utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur='' where ide_GTEMP in(" +str_ide+")");
          			set_empleado.cerrar();		
		}
          
        	guardarPantalla();
   			utilitario.agregarMensajeInfo("Se han Actualizado correctamente los empleados seleccionados", "");
   		
   			
    }
    
    
    
    public void consultarDepartamento(){
   tab_tabla1.setLectura(false);
   
	   tab_tabla1.setLectura(false);
	     	sel_departamento_empleado.dibujar();


         
    	
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
           
            if (bandEliminarDepartamento) {
            	TablaGenerica getHorarioEmpleado=utilitario.consultar("select ide_astuh,ide_astur from asi_turnos_horario where ide_astuh="+tab_tabla3.getValorSeleccionado());
               	utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null  where ide_GTEMP in(" +str_ide_empleado+")");
       			sel_departamento_empleado.cerrar();		
       			guardarPantalla();
       			
       			tab_consulta_empleado.setSql("select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
       					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
       					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
       					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
       					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
       					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
       					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
       					+ "WHERE EMP.IDE_ASTUR=1 and EPAR.ACTIVO_GEEDP=TRUE  "
       					+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
       					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
       			tab_consulta_empleado.ejecutarSql();
       		
       			
       			utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado");  			
            }else {
				
			
            
            //TablaGenerica getHorarioEmpleado=utilitario.consultar("select ide_astuh,ide_astur from asi_turnos_horario where ide_astuh="+tab_tabla3.getValorSeleccionado());
           	utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=1 where ide_GTEMP in(" +str_ide_empleado+")");
   			sel_departamento_empleado.cerrar();		
   			guardarPantalla(); 
   			tab_consulta_empleado.actualizar();
   			//tab_consulta_turno_empleado.setCondicion("ide_astur="+tab_tabla1.getValorSeleccionado());
   			//tab_consulta_turno_empleado.ejecutarSql();
   			utilitario.addUpdate("tab_consulta_empleado,tab_consulta_turno_empleado,sel_departamento_empleado");
			}
   			utilitario.agregarMensajeInfo("Se han Actualizado correctamente los empleados seleccionados", "");
    	
    	
    }
    
    
    
    
    
    
  	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    
    
    
    
    public void cambioTipoHora(){
    	if(com_tipo_hora.getValue()!=null){
    		tab_tabla1.setCondicion("turno_matriz_astur="+com_tipo_hora.getValue());
    		boolean bandraOperativo=false,bandraMatriz=false;
    		
    		
    		
    		tab_tabla1.ejecutarSql();   
    		 tab_tabla1.actualizar();
    		if (com_tipo_hora.getValue().toString().equals("true")){
    			
    	    tab_tabla3.setCondicion("IDE_ASTUR=1");
    	    bandraMatriz=true;
    		}else {
    			   tab_tabla3.setCondicion("IDE_ASTUR!=1");
			bandraOperativo=true;
    		}
    		tab_tabla3.ejecutarSql();
    	    tab_tabla3.actualizar();
    	    
    	    String tab_horario="";
    	    
    	    if (com_tipo_hora.getValue().toString().equals("false")) {
    	    tab_horario="select ASEMP.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
        				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
        				+ "from asi_horario_mes_empleado  asemp  "
        				+ "LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON ASEMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
        				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
        				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
        				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
        				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
        				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND ASEMP.IDE_GEMES="+(utilitario.getMes(utilitario.getFechaActual()))+" AND IDE_GEANI=10 "
        				+ "GROUP BY ASEMP.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
        				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 

			}else {
				tab_horario="select EPAR.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
						+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
						+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR    "
						+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
						+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
						+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
						+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
						+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND EMP.IDE_ASTUR=1 AND ACTIVO_GTEMP=TRUE "
						+ "GROUP BY EPAR.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
						+ "ORDER BY SUCU.NOM_SUCU ASC,EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

			}
    	    
    		
    		tab_consulta_empleado.setSql(tab_horario);
    		tab_consulta_empleado.ejecutarSql();
    		tab_consulta_empleado.actualizar();
  	      utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado");

    		
    		
    	    
    		//tab_consulta_empleado.    		
    		   	}
    	else{
    		tab_tabla1.limpiar();    		
    	}
    }
    
    
    @Override
    public void insertar() {
    	
    	
    		try {
    		if(tab_tabla1.isFocus()){
    		tab_tabla1.insertar();
    		//tab_tabla1.setValor("IDE_ASTUR", tab_tabla1.getValorSeleccionado());x
    		}
    		else if(tab_tabla3.isFocus()){
    			tab_tabla3.insertar();
    		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error al insertar");			
				}
    	  
    }

    @Override
    public void guardar() {
        if(tab_tabla1.guardar()){
        	tab_tabla3.guardar();
        		guardarPantalla();        	       
        } 
    }

    @Override
    public void eliminar() {
        if(tab_tabla1.isFocus()){
        	tab_tabla1.eliminar();
        }

        else if(tab_tabla3.isFocus()){
        	tab_tabla3.eliminar();
        }
    }

	



	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
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



	
	public String getConsultaAsignacionEmpleado(String ide_astur){
		String tab_asignacio_empleado="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "WHERE IDE_ASTUR="+ide_astur;
    			 return tab_asignacio_empleado;		
	}



 
	public Tabla getTab_consulta_turno_empleado() {
		return tab_consulta_turno_empleado;
	}

	public void setTab_consulta_turno_empleado(Tabla tab_consulta_turno_empleado) {
		this.tab_consulta_turno_empleado = tab_consulta_turno_empleado;
	}

	public void verAsignacionEmpleado(AjaxBehaviorEvent evt){
		  tab_tabla1.modificar(evt);
		  tab_consulta_turno_empleado.setSql(getConsultaAsignacionEmpleado(tab_tabla1.getValorSeleccionado()));
		  tab_consulta_turno_empleado.ejecutarSql();
		  tab_consulta_turno_empleado.actualizar();
	    }

	public boolean isBandEliminarDepartamento() {
		return bandEliminarDepartamento;
	}

	public void setBandEliminarDepartamento(boolean bandEliminarDepartamento) {
		this.bandEliminarDepartamento = bandEliminarDepartamento;
	}

	public boolean isBandEliminarEmpleado() {
		return bandEliminarEmpleado;
	}

	public void setBandEliminarEmpleado(boolean bandEliminarEmpleado) {
		this.bandEliminarEmpleado = bandEliminarEmpleado;
	}
	
	public void verOperarativo(){
		bandMatriz=false;
		bandOperativo=true;
		tab_tabla1.setCondicion("turno_matriz_astur is null");
		tab_tabla1.ejecutarSql();

		tab_tabla3.setCondicion("IDE_ASTUR="+tab_tabla1.getValorSeleccionado());
		tab_tabla3.ejecutarSql();
		/*
		 * 
		 */
		String tab_horario="select ASEMP.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from asi_horario_mes_empleado  asemp  "
				+ "LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON ASEMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE ASEMP.ide_ashme=-1 and EPAR.ACTIVO_GEEDP=TRUE AND ASEMP.IDE_GEMES=11  "
				+ "GROUP BY ASEMP.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 

		tab_consulta_empleado.setSql(tab_horario);
		tab_consulta_empleado.ejecutarSql();
	
		
		
		tab_consulta_turno_empleado.setSql("select  "
				+ "'' as evento, "
				+ "'' as dia,  "
				+ "'' as turno  "
				+ "from asi_horario_mes_empleado");
		tab_consulta_turno_empleado.ejecutarSql();
		utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado,tab_consulta_turno_empleado");

		
				
	}
	
	
	public void verMatriz(){
		bandMatriz=true;
		bandOperativo=false;
		bandActualizoOperativo=false;	
		tab_tabla1.setCondicion("turno_matriz_astur=true");
		tab_tabla1.ejecutarSql();
		tab_tabla3.setCondicion("IDE_ASTur=1");
		tab_tabla3.ejecutarSql();

		/*
		 * 
		 */
		String tab_horario="select ASEMP.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from asi_horario_mes_empleado  asemp  "
				+ "LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON ASEMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE ASEMP.ide_ashme=-1 and EPAR.ACTIVO_GEEDP=TRUE AND ASEMP.IDE_GEMES=11  "
				+ "GROUP BY ASEMP.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 

		tab_consulta_empleado.setSql(tab_horario);
		tab_consulta_empleado.ejecutarSql();
	
		
		
		tab_consulta_turno_empleado.setSql("select  "
				+ "'' as evento, "
				+ "'' as dia,  "
				+ "'' as turno  "
				+ "from asi_horario_mes_empleado");
		tab_consulta_turno_empleado.ejecutarSql();
		utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado,tab_consulta_turno_empleado");
	}

	public boolean isBandOperativo() {
		return bandOperativo;
	}

	public void setBandOperativo(boolean bandOperativo) {
		this.bandOperativo = bandOperativo;
	}

	public boolean isBandMatriz() {
		return bandMatriz;
	}

	public void setBandMatriz(boolean bandMatriz) {
		this.bandMatriz = bandMatriz;
	}
	
	public void limpiar(){
		tab_tabla1.setCondicion("ide_astur=1");
		tab_tabla1.ejecutarSql();
		tab_tabla3.setCondicion("ide_astur=1");
		tab_tabla3.ejecutarSql();
		tab_consulta_turno_empleado.actualizar();
		tab_consulta_turno_empleado.setCondicion("ide_gtemp=-1");
		tab_consulta_turno_empleado.ejecutarSql();

		utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado,tab_consulta_turno_empleado");
		
	}
	
	
	
	
	/**
	 * Metodo cambia cuando el tipo de horario cambia de matriz a operativo y viceversa
	 * @param banderaTipoTurno
	 * @param ide_mes
	 * @param ide_anio
	 * @return
	 */
	
public String getConsultaHorarioMesEmpleado(){
	String tab_horario="";
	TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%2017%'");
	int ide_geani= Integer.parseInt(tabAnio.getValor("ide_geani"));
	

		tab_horario="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE EMP.IDE_ASTUR=-1 and EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "
				+ "GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 
		
	
			 return tab_horario;		

	
}
	


public void getConsultaHorarioMesEvento(AjaxBehaviorEvent evt) {
	tab_tabla1.seleccionarFila(evt);
	seleccionarTabla1();
}
public void getConsultaHorarioMesEvento(SelectEvent evt){
	tab_tabla1.seleccionarFila(evt);
	seleccionarTabla1();	
}
	
public void seleccionarTabla1(){
	
	try {
	
	TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(utilitario.getFechaActual()));
	String mesEscogido=tabMes.getValor("detalle_gemes");
	
	String tab_horario="";

	if (com_tipo_hora.getValue().toString().equals("false")) {
	    tab_horario="select ASEMP.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
    				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
    				+ "from asi_horario_mes_empleado  asemp  "
    				+ "LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON ASEMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
    				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
    				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
    				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
    				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
    				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND ASEMP.IDE_GEMES="+(utilitario.getMes(utilitario.getFechaActual()))+" AND ASEMP.IDE_GEANI=10 AND  "
    				+ "ASEMP.DIA"+utilitario.getDia(utilitario.getFechaActual())+"="+tab_tabla1.getValorSeleccionado()+" "
    				+ "GROUP BY ASEMP.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
    				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 

		
		
		tab_consulta_empleado.setSql(tab_horario);
		tab_consulta_empleado.ejecutarSql();
		tab_consulta_empleado.actualizar();
	      utilitario.addUpdate("tab_consulta_empleado");
}else
{
	
}
	} catch (Exception e) {
		// TODO Auto-generated catch block
System.out.println("Error en método getConsultaHorarioMesEvento");	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	/*

	select 	'' as dia,
		'' as fecha
		from asi_horario_mes_empleado*/
}
	
		/**
		 * Metodo cambia cuando el tipo de horario cambia de matriz a operativo y viceversa
		 * @param banderaTipoTurno
		 * @param ide_meside_gtemp
		 * @param ide_anio
		 * @return
		 */

public String getConsultaHorarioMes(int ide_gtemp){
		String tab_asi_vacacion="";
		TablaGenerica tabEmpleadoMatriz= utilitario.consultar("select * from asi_horario_mes_empleado where "
				+ "ide_gemes="+utilitario.getMes(utilitario.getFechaActual())+" and"
				+ " ide_gtemp="+ide_gtemp);

		
//Veo los dias de cada mes 		
		
		if (tabEmpleadoMatriz.getTotalFilas()<=0) {
			
		}else {
			
	
		
		int dias=diasDelMes(utilitario.getMes(utilitario.getFechaActual()),
		utilitario.getAnio(utilitario.getFechaActual()));
		 
		int anio=utilitario.getAnio(utilitario.getFechaActual());
		int mes=utilitario.getMes(utilitario.getFechaActual());
			int diaInicio=1;
			int diaFin=0;
			String fecha="";
			//String dia_final=utilitario.getUltimoDiaMesFecha(fecha);
	       // diaFin=utilitario.getDia(dia_final);
			
			

		 
		String diaSemanaPalabra="";

			for (int i = 1; i < dias; i++) {
				if (i<10) {
					fecha=anio+"-"+mes+"-0"+i;	
				}else {
					fecha=anio+"-"+mes+"-"+i;	

				}
				//Obtener el el turno
				String ide_turno=tabEmpleadoMatriz.getValor("DIA"+i);
				if (ide_turno==null) {
					ide_turno="0";
				}
				
				//Obtener el nombre del turno
				String getnombreTurnotabEmpleadoMatriz="";
				getnombreTurnotabEmpleadoMatriz=getTurnoMatriz(Integer.parseInt(ide_turno));
			
			diaSemanaPalabra=diaSemana(i, mes, anio);
			tab_consulta_turno_empleado.setValor(i,"EVENTO", fecha);
			tab_consulta_turno_empleado.setValor(i,"DIA",diaSemanaPalabra);
			tab_consulta_turno_empleado.setValor(i,"TURNO", getnombreTurnotabEmpleadoMatriz);
		}
			
			TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(utilitario.getFechaActual()));
			String mesEscogido=tabMes.getValor("detalle_gemes");
			

	    	tab_consulta_turno_empleado.setHeader("HORARIO MENSUAL "+mesEscogido+" AÑO "+utilitario.getAnio(utilitario.getFechaActual()));
	    	
		utilitario.addUpdate("tab_consulta_turno_empleado");

		
		}
			 return tab_asi_vacacion;		

	
	
}
	
	public void obtenerProgramcionMensual(){
		
	}

	public boolean isBanderaTipoTurno() {
		return banderaTipoTurno;
	}

	public void setBanderaTipoTurno(boolean banderaTipoTurno) {
		this.banderaTipoTurno = banderaTipoTurno;
	}

	public int getIde_mes() {
		return ide_mes;
	}

	public void setIde_mes(int ide_mes) {
		this.ide_mes = ide_mes;
	}

	public int getIde_anio() {
		return ide_anio;
	}

	public void setIde_anio(int ide_anio) {
		this.ide_anio = ide_anio;
	}




	public Tabla getTab_consulta_empleado() {
		return tab_consulta_empleado;
	}

	public void setTab_consulta_empleado(Tabla tab_consulta_empleado) {
		this.tab_consulta_empleado = tab_consulta_empleado;
	}

	public void actualizarHorario(SelectEvent evt){
   
		tab_tabla1.seleccionarFila(evt);
		
		if (com_tipo_hora.getValue().toString().equals("true")){
	    
			tab_tabla3.setCondicion("ide_astur=1");
			}else {
				   tab_tabla3.setCondicion("ide_astur!=1");
			}
		

		//TablaGenerica tabTurnoHorario=utilitario.consultar("select * from asi_turnos_horario where ide_astur="+tab_tabla1.getValorSeleccionado());
		//tab_tabla3.setCondicion("IDE_ASTUH="+tabTurnoHorario.getValor("IDE_ASTUH"));
		//tab_tabla3.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
		tab_tabla3.ejecutarSql();
		tab_tabla3.actualizar();
		
    String tab_horario="";
    
    if (com_tipo_hora.getValue().toString().equals("false")) {
    tab_horario="select ASEMP.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from asi_horario_mes_empleado  asemp  "
				+ "LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON ASEMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND ASEMP.IDE_GEMES=9 AND IDE_GEANI=10 "
				+ "GROUP BY ASEMP.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC"; 

	}else {
		tab_horario="select EPAR.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR    "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND EMP.IDE_ASTUR=1 AND ACTIVO_GTEMP=TRUE "
				+ "GROUP BY EPAR.ide_gtemp, SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

	}
    
	
	tab_consulta_empleado.setSql(tab_horario);
	tab_consulta_empleado.ejecutarSql();
	tab_consulta_empleado.actualizar();
    utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_consulta_empleado");

	
	}
	
	public void actualizarHorario2(){
		
		
		
	}
	
	
	/**
	 * Retorna el numero de dias de un mes
	 * @param mes
	 * @param anio
	 * @return
	 */
	public static int diasDelMes(int mes, int anio){
        switch(mes){
            case 1:  // Enero
            case 3:  // Marzo
            case 5:  // Mayo
            case 7:  // Julio
            case 8:  // Agosto
            case 10:  // Octubre
            case 12: // Diciembre
                return 31;
            case 4:  // Abril
            case 6:  // Junio
            case 9:  // Septiembre
            case 11: // Noviembre
                return 30;
            case 2:  // Febrero
                if ( ((anio%100 == 0) && (anio%400 == 0)) ||
                        ((anio%100 != 0) && (anio%  4 == 0))   )
                    return 29;  // Año Bisiesto
                else
                    return 28;
            default:
                throw new java.lang.IllegalArgumentException(
                "El mes debe estar entre 0 y 11");
        }
}
	
	
	  
    /**
     * Sacar la fecha en palabras
     * @param dia
     * @param mes
     * @param anioEscogido
     * @return
     */
    public String diaSemana (int dia, int mes, int anioEscogido)
    {
    	String mesMenor="";
        String Valor_dia="";
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
        return Valor_dia;
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
    
  public String getTurnoMatriz(int ide_astur){
	  String turno="";
	  if (ide_astur==0) {
		turno="DÍA LIBRE";
	}else {
		  TablaGenerica consulta=utilitario.consultar("select ide_astur,nom_astur from asi_turnos where ide_astur="+ide_astur);
		   turno=consulta.getValor("NOM_ASTUR");
	}

	  
	  return turno;
  }

public boolean isBandAsignar() {
	return bandAsignar;
}

public void setBandAsignar(boolean bandAsignar) {
	this.bandAsignar = bandAsignar;
}

public boolean isBandEliminar() {
	return bandEliminar;
}

public void setBandEliminar(boolean bandEliminar) {
	this.bandEliminar = bandEliminar;
}
  
  
 
  
}
