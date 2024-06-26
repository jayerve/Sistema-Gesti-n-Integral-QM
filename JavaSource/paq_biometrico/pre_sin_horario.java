/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.record.formula.Ptg;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_sin_horario extends Pantalla {


    
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	TablaGenerica  obtenerTurnoEmpleado;
    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_empleado= new SeleccionTabla();
    
    private Tabla tab_tabla_sumatoria =new Tabla();
    private Tabla tab_tabla_hora_extra =new Tabla();


    
    
    String mes="";
  	String fechaIni="",fechaFin="";
  	String empleado="";
  	String fechaInicial="";
	String fechaFinal="";
    public pre_sin_horario() {
    	
    	
    	bar_botones.agregarComponente(new Etiqueta("Reportes de Biometrico"));
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	    	
    	
    	
    	  	//Boton Reporte de Empleado
    	Boton bot_rep_biometrico= new Boton();
    	bot_rep_biometrico.setIcon("ui-icon-calculator");
    	bot_rep_biometrico.setMetodo("seleccionarReporteTotal");
    	bot_rep_biometrico.setValue("CONSULTA HORAS EXTRA");
    	bot_rep_biometrico.setTitle("CONSULTA HORAS EXTRA");
    	bar_botones.agregarBoton(bot_rep_biometrico);
    	

    	
	bar_botones.agregarSeparador();
		
		aut_empleados.setId("aut_empleados");
		aut_empleados.setAutoCompletar("SELECT emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM  GTH_EMPLEADO EMP " +
				"WHERE EMP.ACTIVO_GTEMP=true");
		aut_empleados.setMetodoChange("seleccionoEmpleado");
		
	//bar_botones.agregarComponente(aut_empleados);
	
    
    	
    		// boton limpiar
    	
     	
    	Boton bot_departamento_biometrico= new Boton();
    	bot_departamento_biometrico.setIcon("ui-icon-calculator");
    	bot_departamento_biometrico.setMetodo("asignarMatriz");
    	bot_departamento_biometrico.setValue("ASIGNAR EMPLEADOS MATRIZ");
    	bot_departamento_biometrico.setTitle("ASIGNAR EMPLEADOS MATRIZ");
    	bar_botones.agregarBoton(bot_departamento_biometrico);
    	
    	Boton bot_pegar_horario= new Boton();
    	bot_pegar_horario.setIcon("ui-icon-calculator");
    	bot_pegar_horario.setMetodo("pegarHorario");
    	bot_pegar_horario.setValue("PEGAR HORARIO");
    	bot_pegar_horario.setTitle("PEGAR HORARIO");
    	bar_botones.agregarBoton(bot_pegar_horario);
    	
   
    	
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla(ser_empleado.servicioEmpleadosActivos("true"), "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
    	set_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("getEmpleado");
		agregarComponente(set_empleado);
		
	 	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar);   
    	
    	
    	
    	//Boton Justificar
    	Boton bot_justificar= new Boton();
    	bot_justificar.setIcon("ui-icon-calculator");
    	bot_justificar.setMetodo("getMarcacionesEmpleadoHorasExtra");
    	bot_justificar.setValue("CALCULAR HORAS EXTRA");
    	bot_justificar.setTitle("CALCULAR HORAS EXTRA");
    	bar_botones.agregarBoton(bot_justificar);
       
		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

    
    	
		sel_mes.setId("sel_mes");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in(12,1) ","IDE_GEMES");

    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("20");


		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.getBot_aceptar().setMetodo("abrirEmpleado");
		agregarComponente(sel_mes);
    	
    	
    	
	  	
			sel_empleado.setId("sel_empleado");
	    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
	    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
	    
			agregarComponente(sel_empleado);
    	
    	
    	
    	
    	
    	
    	
    	
    	
			
			tab_tabla_hora_extra.setId("tab_tabla_hora_extra");
			tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.horafinextra_cobmr,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true and res.ide_cobmr=-1   "
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
			tab_tabla_hora_extra.setCampoPrimaria("ide_cobmr");
			tab_tabla_hora_extra.setLectura(true);

			
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").setLongitud(5);
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").alinearCentro();
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").setNombreVisual("COD");


			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CÉDULA");
			

			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").alinearCentro();
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

			
			tab_tabla_hora_extra.getColumna("NOM_SUCU").setLongitud(15);
			tab_tabla_hora_extra.getColumna("NOM_SUCU").alinearCentro();
			tab_tabla_hora_extra.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
			tab_tabla_hora_extra.getColumna("NOM_SUCU").setFiltro(true);

	        
	    	
			tab_tabla_hora_extra.getColumna("DETALLE_GEARE").setLongitud(15);
			tab_tabla_hora_extra.getColumna("DETALLE_GEARE").alinearCentro();
			tab_tabla_hora_extra.getColumna("DETALLE_GEARE").setNombreVisual("AREA");
			tab_tabla_hora_extra.getColumna("DETALLE_GEARE").setFiltro(true);

	        
			tab_tabla_hora_extra.getColumna("DETALLE_GEDEP").setLongitud(15);
			tab_tabla_hora_extra.getColumna("DETALLE_GEDEP").alinearCentro();
			tab_tabla_hora_extra.getColumna("DETALLE_GEDEP").setNombreVisual("DEPT");
			tab_tabla_hora_extra.getColumna("DETALLE_GEDEP").setFiltro(true);

			tab_tabla_hora_extra.getColumna("IDE_COBMR").setLongitud(5);
			tab_tabla_hora_extra.getColumna("IDE_COBMR").alinearCentro();
			tab_tabla_hora_extra.getColumna("IDE_COBMR").setVisible(false);


			tab_tabla_hora_extra.getColumna("DIA_COBMR").setLongitud(15);
			tab_tabla_hora_extra.getColumna("DIA_COBMR").alinearCentro();
			tab_tabla_hora_extra.getColumna("DIA_COBMR").setNombreVisual("DIA");
	        
			tab_tabla_hora_extra.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
			tab_tabla_hora_extra.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
	        
	        
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").setNombreVisual("ENTRADA");
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").alinearCentro();

	        
	        
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").setNombreVisual("TIMBRE");
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").alinearCentro();

	        
        
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").setNombreVisual("SALIDA");
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").alinearCentro();

	       
	        
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").setNombreVisual("TIMBRE SALIDA");
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").alinearCentro();

			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setLongitud(45);
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setNombreVisual("HORAS NOCTURNAS");
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setDecimales(2);
	        
			tab_tabla_hora_extra.getColumna("horafinextra_cobmr").setLongitud(55);
			tab_tabla_hora_extra.getColumna("horafinextra_cobmr").setNombreVisual("HORAS SUPLEMENTARIAS");
			tab_tabla_hora_extra.getColumna("horafinextra_cobmr").alinearCentro();

		
		
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").setLongitud(55);
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").setNombreVisual("HORAS EXTRAORDINARIAS"); 

	                        
			tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").setLongitud(25);
			tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").alinearCentro();
			tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMPLEADO");  
	        

			tab_tabla_hora_extra.setRows(15);
			tab_tabla_hora_extra.dibujar();
			tab_tabla_hora_extra.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");

	        PanelTabla pat_panel = new PanelTabla();
	        pat_panel.setPanelTabla(tab_tabla_hora_extra);
    	
    	
   		
			
			tab_tabla_sumatoria.setId("tab_tabla_sumatoria");
			tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(0,"",""));
			tab_tabla_sumatoria.setCampoPrimaria("ide_gtemp");
			tab_tabla_sumatoria.setLectura(true);
	        
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setLongitud(5);
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").alinearCentro();
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setNombreVisual("COD");


			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CÉDULA");
			

			tab_tabla_sumatoria.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
			tab_tabla_sumatoria.getColumna("NOMBRES_APELLIDOS").alinearCentro();
			tab_tabla_sumatoria.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
			tab_tabla_sumatoria.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

			
			tab_tabla_sumatoria.getColumna("NOM_SUCU").setLongitud(15);
			tab_tabla_sumatoria.getColumna("NOM_SUCU").alinearCentro();
			tab_tabla_sumatoria.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
			tab_tabla_sumatoria.getColumna("NOM_SUCU").setFiltro(true);

	        
	    	
			tab_tabla_sumatoria.getColumna("DETALLE_GEARE").setLongitud(15);
			tab_tabla_sumatoria.getColumna("DETALLE_GEARE").alinearCentro();
			tab_tabla_sumatoria.getColumna("DETALLE_GEARE").setNombreVisual("AREA");
			tab_tabla_sumatoria.getColumna("DETALLE_GEARE").setFiltro(true);

	        
			tab_tabla_sumatoria.getColumna("DETALLE_GEDEP").setLongitud(15);
			tab_tabla_sumatoria.getColumna("DETALLE_GEDEP").alinearCentro();
			tab_tabla_sumatoria.getColumna("DETALLE_GEDEP").setNombreVisual("DEPT");
			tab_tabla_sumatoria.getColumna("DETALLE_GEDEP").setFiltro(true);
			
			
			tab_tabla_sumatoria.getColumna("DETALLE_GTTEM").setLongitud(25);
			tab_tabla_sumatoria.getColumna("DETALLE_GTTEM").alinearCentro();
			tab_tabla_sumatoria.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMPLEADO");  
			
			
			tab_tabla_sumatoria.getColumna("HORAS_NOCTURNAS").setLongitud(35);
			tab_tabla_sumatoria.getColumna("HORAS_NOCTURNAS").alinearCentro();
			tab_tabla_sumatoria.getColumna("HORAS_NOCTURNAS").setNombreVisual("TOTAL HORAS NOCTURNAS"); 
			
			
			tab_tabla_sumatoria.getColumna("HORAS_SUPLEMENTARIAS").setLongitud(35);
			tab_tabla_sumatoria.getColumna("HORAS_SUPLEMENTARIAS").alinearCentro();
			tab_tabla_sumatoria.getColumna("HORAS_SUPLEMENTARIAS").setNombreVisual("TOTAL HORAS SUPLEMENTARIAS"); 
			
			
			tab_tabla_sumatoria.getColumna("HORAS_EXTRA").setLongitud(35);
			tab_tabla_sumatoria.getColumna("HORAS_EXTRA").alinearCentro();
			tab_tabla_sumatoria.getColumna("HORAS_EXTRA").setNombreVisual("TOTAL HORAS EXTRAORDINARIAS"); 

			tab_tabla_sumatoria.setRows(5);
			tab_tabla_sumatoria.dibujar();
			tab_tabla_sumatoria.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");


	        
	        
	        
			   PanelTabla pat_panel11 = new PanelTabla();
		        pat_panel11.setPanelTabla(tab_tabla_sumatoria);
	        
		        Division div_division = new Division();
		        div_division.setId("div_division");
		        div_division.dividir2(pat_panel,pat_panel11,"75%","H");

		
		        
		        agregarComponente(div_division);


        
        
    }

    
  
    
    
    public void seleccionarReporteTotal(){
    	
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
 		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
 			fechaIni=(cal_fecha_inicial.getFecha());	
     		fechaFin=(cal_fecha_final.getFecha());
 		
     		tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "					
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.horafinextra_cobmr,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
			
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla_hora_extra");
			
			tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(1,fechaIni,fechaFin));
			tab_tabla_sumatoria.ejecutarSql();
			utilitario.addUpdate("tab_tabla_sumatoria");
			//Calculo de Horas Extra
			cuatroUno();
		
 		
 		
 		
 		}	 else {
			utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Fecha inicial debe ser menor a fecha final");
			return;
		
	} 	
	 	
  		}else {
				utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
		}
    
    
    }
    
    
    
    
     public void seleccionarEmpleado(){
    
    		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    			
        		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
            		set_empleado.dibujar();
        		}else {
    				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

    			}
        		
    		}
    		else {
    			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
    		}		
        	
       }
    

	
	
    public void seleccionarDepartamento(){
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
			
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
    
    	
    	set_departamento.dibujar();

		}else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		}
		
	}
	else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	}		
    
 }
    

	
	
    @SuppressWarnings("unused")
	

  
  private TablaGenerica obtenerHorariosTurnoEmpleado(Integer ide_ashor){
    	
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select hor.ide_astur,turno.ide_gtgre from asi_turnos_horario hor "
    			+ "left join asi_turnos turno on turno.ide_astur=hor.ide_astur "
    			+ " where hor.ide_ashor="+ide_ashor);
    	return horarioXEmpleado;
    	
    }
    
   private TablaGenerica obtenerTurnos(){
    	
    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario");
    	return obtenerHorarios;
    	
    }
    
    

 	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    

    
public TablaGenerica  obtenerPermisoXEmpleadoEntrada(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
        + "from asi_permisos_vacacion_hext where "
  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
  	    + "and hora_desde_aspvh >= '"+horaInicio+"' ");
  
  obtenerPermisos.imprimirSql();
  return obtenerPermisos;
 	
}
 	



public TablaGenerica  obtenerPermisoXEmpleadoSalida(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
    TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
    + "from asi_permisos_vacacion_hext where "
		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
	    + "and hora_hasta_aspvh <= '"+horaInicio+"' ");

obtenerPermisos.imprimirSql();
return obtenerPermisos;
	
}
	


 	public void limpiar(){
 		//tab_tabla.limpiar();
 	aut_empleados.limpiar();
 		utilitario.addUpdate("aut_empleados");
 	}
    
 	private Date getFechaAsyyyyMMddHHmmss(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    
    private String getFechaAsyyyyMMddHHmmss(Date fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return df.format(fecha);
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

    
    
    @Override
    public void insertar() {
		
	  
    }

    @Override
    public void guardar() {
  
    	
   	for (int i = 0; i < tab_tabla_hora_extra.getTotalFilas(); i++) {
   
    		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr="+tab_tabla_hora_extra.getValor(i,"aprueba_hora_extra_cobmr")+" where ide_cobmr="+tab_tabla_hora_extra.getValor(i,"IDE_COBMR"));
            guardarPantalla();
		}
    	utilitario.agregarMensaje("Se han realizado cambios con Exito", "Registros de Horas Extra Modificado");

    	
    }

    @Override
    public void eliminar() {
        //tab_tabla.eliminar();
    }





	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}



	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}



	public SeleccionTabla getSet_departamento() {
		return set_departamento;
	}



	public void setSet_departamento(SeleccionTabla set_departamento) {
		this.set_departamento = set_departamento;
	}

	
    private TablaGenerica obtenerHorariosEmpleado(Integer ide_getemp){
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select EMP.IDE_GTEMP, "
    			+ "horario.ide_ashor, "
    			+ "HORARIO.HORA_INICIAL_ASHOR, "
    			+ "HORARIO.HORA_FINAL_ASHOR, "
    			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
    			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
    			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
    			+ "HORARIO.NOM_ASHOR "
    			+ "from gth_empleado emp "
    			+ "left join asi_turnos_horario turnos on turnos.ide_astur=emp.ide_astur "
    			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor "
    			+ "where emp.ide_astur is not null  AND EMP.IDE_GTEMP="+ide_getemp
    			+ " ORDER BY HORARIO.IDE_ASHOR ASC");
    	return horarioXEmpleado;
    }
    	
    	
   	 public void updateTablaResumen(
   			 int ide_cobmr,
   			 String horainiciobiometrico_cobmr,
			 String horainicioband_cobmr,
			 String horainicioalmbio_cobmr,
			 String horafinalmbio_cobmr,
			 String tiempoalm_cobmr,
			 String horafinbiometrico_cobmr,
			 String horafinband_cobmr){
	
			utilitario.getConexion().ejecutarSql(""
					+ "UPDATE con_biometrico_marcaciones_resumen set "
					+ "horainiciobiometrico_cobmr='"+horainiciobiometrico_cobmr+"', " 
					+ "horainicioband_cobmr='"+horainicioband_cobmr+"', "
					+ "horainicioalmbio_cobmr='"+horainicioalmbio_cobmr+"',"
					+ "horafinalmbio_cobmr='"+horafinalmbio_cobmr+"',"
					+ "tiempoalm_cobmr='"+tiempoalm_cobmr+"',"
					+ "horafinbiometrico_cobmr='"+horafinbiometrico_cobmr+"',"
					+ "horafinband_cobmr='"+horafinband_cobmr+"' "
					+ "where ide_cobmr="+ide_cobmr+""); 

    }

   	private double obtenerDiferenciaMinutos(Date fechaInicial, Date fechaFinal){
        double diferencia=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/1000);
 
        int dias=0;
        double horas=0;
        double minutos=0;
        if(diferencia>86400) {
            dias=(int)Math.floor(diferencia/86400);
            diferencia=diferencia-(dias*86400);
        }
        if(diferencia>3600) {
            horas=(int)Math.floor(diferencia/3600);
            diferencia=diferencia-(horas*3600);
        }
        if(diferencia>60) {
            minutos=(int)Math.floor(diferencia/60);
            diferencia=diferencia-(minutos*60);
        }
    	
    	double total_diferencia_segundos = ((horas * 3600) + (minutos * 60) + diferencia);
		double total_diferencia_horas = total_diferencia_segundos / 3600;
		
		double total_diferencia_minutos = total_diferencia_horas * 60;
		
		
		return total_diferencia_minutos;
    }

  //Sumar horas en fecha
  	 public Date sumarRestarHorasFecha(Date fecha, int horas){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(fecha); // Configuramos la fecha que se recibe
       calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
       return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

  	 }











	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new  SimpleDateFormat ("hh:mm:ss");

			String hora1 = utilitario.getFormatoHora(hora_ini);
			String hora2 = utilitario.getFormatoHora(hora_fin);

			int int_hora1=pckUtilidades.CConversion.CInt(hora1.replaceAll(":", ""));
			int int_hora2=pckUtilidades.CConversion.CInt(hora2.replaceAll(":", ""));


			if(int_hora1>int_hora2){
				return true;
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	



	public void actualizarMarcaciones(){

	
		
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null && aut_empleados.getValor()!=null) {
			if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
	    		String fechaIni=(cal_fecha_inicial.getFecha());	
	    		String fechaFin=(cal_fecha_final.getFecha());	
	    		tab_tabla_hora_extra.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
	       				+ "and ide_gtemp='"+aut_empleados.getValor()+"' and aprueba_hora_extra_cobmr=true ");
	    		tab_tabla_hora_extra.ejecutarSql();
    		utilitario.addUpdate("tab_tabla");
    	 	cuatroUno();
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
				
			}
		}
		

	}
	
	
	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
		actualizarMarcaciones();	
	}



	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}



	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
	
	
	




public TablaGenerica  obtenerPermisoXEmpleado(String FechaInicio, String FechaFin,String horaInicio, String horaFin,  Integer IDE_GTEMP){
    TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
    + "from asi_permisos_vacacion_hext where "
		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaFin+"' and ide_gtemp="+IDE_GTEMP+" "
	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"'");

obtenerPermisos.imprimirSql();
return obtenerPermisos;
}


private TablaGenerica obtenerMarcacionesEmpleado(Integer ide_gtemp){
	TablaGenerica marcacionesXEmpleado=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr");
	return marcacionesXEmpleado;
	
	
}



public String getConsultaHorario(String ide_astur){
	String tab_asi_vacacion="select  "
			+ "turhor.ide_astuh, "
			+ "hor.ide_ashor, "
			+ "hor.nom_ashor, "
			+ "hor.hora_inicial_ashor, "
			+ "hor.hora_final_ashor, "
			+ "hor.hora_inicio_almuerzo_ashor, "
			+ "hor.hora_fin_almuerzo_ashor,  "
			+ "hor.min_almuerzo_ashor "
			+ "from asi_turnos_horario turhor "
			+ "left join asi_horario hor on hor.ide_ashor=turhor.ide_ashor "
			+ "where turhor.ide_astur="+ide_astur;
			 return tab_asi_vacacion;		
}

  


public void getMarcacionesEmpleadoHorasExtra(){ 
	tab_tabla_hora_extra.setLectura(false);
	int contador = 0;

	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;

	 String fechaBiometrico="";
	
//	Tabla que contiene las fechas de timbre del empleado	

	 
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
  		fechaIni=(cal_fecha_inicial.getFecha());	
  		fechaFin=(cal_fecha_final.getFecha());
	
		TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr,  "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
				+ "horafinband_cobmr, horafinextra_cobmr, recargonocturno25_cobmr, "
				+ "recargonocturno100_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr, "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,   "
				+ "ide_aspvh "
				+ "FROM con_biometrico_marcaciones_resumen  where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =true");

		
		if (tabConBiometricoMarcacionesResumen.getTotalFilas()<=0) {
			utilitario.agregarMensajeInfo("Rango de Fechas  Inválidas",	"No existen datos para el rango seleccionado");
			return;
		}else{
		

		 
			for (int itemReporte=0; itemReporte< tabConBiometricoMarcacionesResumen.getTotalFilas(); itemReporte++) {
				//Obtengo el ide del empleado con el cual obtendrenmos los horarios
				Integer ide_gtemp = Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(itemReporte, "IDE_GTEMP"));
				//fecha de registro en la tabla resumen
				String fechaBiometricoAgrupadaXEmpleado = tabConBiometricoMarcacionesResumen.getValor(itemReporte,"fecha_evento_cobmr");
				Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
				//Obtengo los horarios de cada empleado de acuerdo a su ide_gtemp
				//Unimos la fecha con la hora
				String fechaHoraBiometrico = fechaBiometricoAgrupadaXEmpleado+" 01:00:00";
			    //Calendario 
				Calendar calFechaHoraBiometrico = Calendar.getInstance();
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
				
				
				//Obtengo datos de marcaciones de Entrada y Salida por empleado

			    String fecha=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"FECHA_EVENTO_COBMR");
			    String horaEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOBIOMETRICO_COBMR");
			    String horaSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINBIOMETRICO_COBMR");
			    String empleado=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"ide_gtemp");

			    String horarioSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINHORARIO_COBMR");
		        String horarioEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOHORARIO_COBMR");
		        String horaDescanso=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"tiempohoralm_cobmr");
		        String horaSinAlmuerzo=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioalm_cobmr");
		        String estadoEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioband_cobmr");

		        boolean banderaAtraso=false;
		         
		      //  System.out.println("EMPLEADO : "+empleado+" fecha : "+fecha);
				 double tiempoHorasExtra25=0.0;
		        
		        
		        
		        boolean sinAlmHorario=false;
		        if (horaDescanso == null || horaDescanso.isEmpty()) {
		        	horaDescanso="0";
				}else {
					horaDescanso=horaDescanso;
				}
		    
		        
		 // Se valida si se tiene hora de almuerzo       
		        
		       if (horaSinAlmuerzo.equals("S/A") || horaSinAlmuerzo.equals("") || horaSinAlmuerzo.isEmpty() || horaSinAlmuerzo==null) {
				sinAlmHorario=true;
			} else {
				sinAlmHorario=false;
			}
		       
		       
		       
		       
		       
		       
		        
				boolean horaExtraEmpleado=false,horarioNocturno=false;
				//Sin no tiene hora de entrada horario entonces  hora al 100%
				if (horarioEntrada==null ||horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					//Si tiene EXTRA O FERIADO
				
					if (estadoEntrada.equals("EXTRA") || estadoEntrada.equals("FERIADO") ) {
						horaExtraEmpleado=true;

					}
					
				}else {
					if (estadoEntrada.equals("ATRASADO")) {
						horaExtraEmpleado=false;
						banderaAtraso=true;
					}
					else  if (estadoEntrada.equals("OK")) {
						horaExtraEmpleado=false;
						banderaAtraso=false;
					}
					
				}
				
				
			
				
				
				
				//Sin tiene hora de entrada horario entonces  hora al 100%
				
				 
				String fechaEntradaTopeNocturno = fecha+" 20:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeNocturno));
			    
			    
			    Calendar calendario = Calendar.getInstance();
			    int hora =calendario.get(Calendar.HOUR_OF_DAY);
			    int minutos = calendario.get(Calendar.MINUTE);
			    int segundos = calendario.get(Calendar.SECOND);
			    
			    
			    if (horarioNocturno) {
					
				}
			    
				String fechaHoraHorarioInicioEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
		//		System.out.println("DDDDDDDD"+fechaHoraHorarioInicioEntrada);
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
				    

			    
			    if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
			    horarioNocturno=true;
				}
				
			    Date fechaFinExtra=null,fechaIniExtra=null;
			     
			   
			    
			    
				 //Date fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
				 //Date fechaFinExtra=null;
				//tiempo descanso en horas
			          	    
				    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
				    String  permiso;
				    double tiempoHorasExtra=0.0;
				    double tiempoHorasExtra100=0.0;
				    double tiempoHorasCalculoExtra=0.0;
				   //calculo de hora extra
				    double horaExtra=0.0;
				     
				 
		//Calculo de horas Extra al 100%		
							if (horarioNocturno==true && horaExtraEmpleado==false) {
							    if (banderaAtraso==true) {
							    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							    		String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);			     
								}else {
									 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
									 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);

								}	
								
						
							double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				 	        double valorHoraDescanso=Double.parseDouble(horaDescanso); 
				 	        String p="";
							int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							
							
							
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
							//if (horaExtra>=8.75) {
						   	//horaExtra=horaExtra-8;		
							//}
							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	

							///////////////////////////////////////////////////////////////////////////////////
							
							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							double p3=Double.parseDouble(p2);
						
							if (banderaAtraso==true) {
								horaExtra=(int)horaExtra+p3-8;
								
							}else {
								horaExtra=(int)horaExtra+p3;
								
							}
											
												
							
							
							
							if (horaExtra>=1) {

								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"' ");
							
							}/*else {

								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"' "
										+" where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
			
							}*/
																	
							//	tab_tabla.ejecutarSql();
						//utilitario.addUpdate("tab_tabla");	
							
							String horaNocInicio25="19:00:00";
							String horaNocFin25="06:00:00";
							String Hora="";
							
						   //Validacion hora Entrada horas al 25%
							//if (horaEntrada.compareTo(horaNocInicio25)>= 0)  
						    	//	{
						    	//horaEntrada=horarioEntrada;
						    	//	}else {
								 //   	horaEntrada=horaNocInicio25;

							//		}	
						    
						   
							//Validacion hora Salida en horas al 25%
							if((horaSalida.compareTo(horaNocFin25)>=0) ){
									    	horaSalida=horaNocFin25;
									    		}else {
											    	horaSalida=horaSalida;
												}	
							
							 Date fechaIniNoc25=null;
							if (banderaAtraso==true) {
								  fechaIniNoc25 = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
										
							}else {
								fechaIniNoc25 = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
							}
							
							
							 Date fechaFinNoc25=null;
							//tiempo descanso en horas
							 tiempoHorasExtra25=0.0;
							 String fechaFinExtra25="";
							    
					
							    
							
					//Calculo de horas Extra al 100%		
									    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaSalida);
									    tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniNoc25, fechaFinNoc25));				 	   
								        p="";
										pt_entera=(int)tiempoHorasExtra100Auxiliar;
										pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
										
										
										
										if ((int)pt_decimal<10 && pt_decimal>-1) {
											p="0.0"+(int)pt_decimal;
										}else if ((int)pt_decimal<=-1) {
											p="0.0";
										}
										else {
											p="0."+(int)pt_decimal;
										}
										
										p1=Double.parseDouble(p);
										horaExtra=pt_entera+p1;
										//if (horaExtra>=8.75) {
									   	//horaExtra=horaExtra-8;		
										//}
										
										if (horaExtra>=0) {
									   	horaExtra=horaExtra/60;		
										}
										else {
											horaExtra=0.0;
										}	

										///////////////////////////////////////////////////////////////////////////////////
										
										 pt_decimal1=(horaExtra-((int)horaExtra))*60;
										 p2="";
										if ((int)pt_decimal1<10 && pt_decimal1>-1) {
											p2="0.0"+(int)pt_decimal1;
										}else if ((int)pt_decimal1<=-1) {
											p2="0.0";
										}
										else {
											p2="0."+(int)pt_decimal1;
										}
										
										p3=Double.parseDouble(p2);
									
											horaExtra=(int)horaExtra+p3;
											
										
		  
									    
									    
									    
									    
									    
		if (horaExtra>=1) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+horaExtra+"'"
					+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

		}/*else {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
					+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
			
		}*/
								
					}
					else if(horaExtraEmpleado==true) {
					
					   //  System.out.println(""+empleado+fecha+fechaIniExtra);
						
						if (horarioNocturno==false) {
							
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
						    //Para horario tipo nocturno
						  //   System.out.println("HORAS EXTRA");
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				
						
						}else {
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
						    //Para horario tipo nocturno
						  //   System.out.println("HORAS EXTRA");
						     Date fechNocturno=utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada), 1);
							
						     String fec=getFechaAsyyyyMMdd(fechNocturno);
						     String fec1=fec+" "+horaSalida;
							 fechaFinExtra= getFechaAsyyyyMMddHHmmss(fec1);
						}
						

						double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				 	   
				        String p="";
						int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						double p1=0.0;
						
						
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						 double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						 String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
					
							horaExtra=(int)horaExtra+p3;
							
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						/*
						
						
						int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						String p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						double p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						*/
						if (horaExtra>=1) {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+horaExtra+"'"
									+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

						}/*else {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr=0.0"
									+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
							
						}*/
																
						
					//	tab_tabla.ejecutarSql();
					//	utilitario.addUpdate("tab_tabla");
							}//ELSE NO ES 100% hora extra
						
					
					else if(horarioNocturno==false){
						
						 	
					     if (horarioSalida==null || horarioSalida.equals("") || horarioSalida.isEmpty()) {
								//System.out.println("empleado y hora para arreglar: "+empleado+" "+fecha+" "+horarioSalida);
							}
					     
					     int tiempoAlm=0;
					      if (banderaAtraso==true) {
					    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
					    		if (horaDescanso.equals("0.5")) {
					    			tiempoAlm=30;
								}else if (horaDescanso.equals("1.0")) {
									tiempoAlm=60;
								}
								 
							     
						}else {
							 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
						     	tiempoAlm=0;
						}
					     
					   fechaFinExtra=null;
					     
						 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
						//double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra)/60);
			 	        //double valorHoraDescanso=Double.parseDouble(horaDescanso)/60; 
						
						 
						 
						 
						// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
						 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
							tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

						 //int a=(int)tiempoHorasExtra100Auxiliar/60;
						 
						//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
						//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						String p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						double p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
						
						if (banderaAtraso==true) {
							horaExtra=(int)horaExtra+p3-8;
							
						}else {
							horaExtra=(int)horaExtra+p3;
							
						}
						
						
						
						

						if (horaExtra>=1) {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"'"
									+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");


						}/*else {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"',"
									+ "aprueba_hora_extra_cobmr=false where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

							
						}*/

						
						
					//		tab_tabla.ejecutarSql();
					//	utilitario.addUpdate("tab_tabla");
				
						
						
						
						
						
						
						
						String horaNocInicio25="19:00:00";
						String horaNocFin25="06:00:00";
						String Hora="";
						

						
						
			    			//suma una hora a la fecha final 
			    			String horaFechaInicioConsulta= fecha+" "+horaNocFin25;    			
			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporte));
								
			    			String fechaHoraHorarioInicio = fecha+" "+horaNocInicio25; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
		 			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
							
						
		 			    
		 				String horaSalidaValidacion25 = fecha+" "+horaSalida; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calhoraSalidaValidacion25 = Calendar.getInstance();//
		 			   calhoraSalidaValidacion25.setTime(getFechaAsyyyyMMddHHmmss(horaSalidaValidacion25));//
							
						
		 		
		 			   
		 			   
		 			    
		 			   if(calhoraSalidaValidacion25.compareTo(calFechaHoraHorarioInicio) >0 ){
		 				   
		 				   
		 				   
		 				Date fechaIniNoc25 = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
						 Date fechaFinNoc25=null;
						//tiempo descanso en horas
						 String fechaFinExtra25="",horaBase="24:00:00";
						    boolean horaSobrepasa=false;
				
						if (horaSalida.compareTo(horaNocInicio25)>0  && horaSalida.compareTo(horaBase)<=0) {
							 
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),0));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
						    tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaFinNoc25,fechaIniNoc25 ));
					        p="";
													pt_entera=(int)tiempoHorasExtra100Auxiliar;
													pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
													
													
													
													if ((int)pt_decimal<10 && pt_decimal>-1) {
														p="0.0"+(int)pt_decimal;
													}else if ((int)pt_decimal<=-1) {
														p="0.0";
													}
													else {
														p="0."+(int)pt_decimal;
													}
													
													p1=Double.parseDouble(p);
													horaExtra=pt_entera+p1;
													//if (horaExtra>=8.75) {
												   	//horaExtra=horaExtra-8;		
													//}
													
													if (horaExtra>=0) {
												   	horaExtra=horaExtra/60;		
													}
													else {
														horaExtra=0.0;
													}	

													///////////////////////////////////////////////////////////////////////////////////
													
													 pt_decimal1=(horaExtra-((int)horaExtra))*60;
													 p2="";
													if ((int)pt_decimal1<10 && pt_decimal1>-1) {
														p2="0.0"+(int)pt_decimal1;
													}else if ((int)pt_decimal1<=-1) {
														p2="0.0";
													}
													else { 
														p2="0."+(int)pt_decimal1;
													}
													
													p3=Double.parseDouble(p2);
												
														horaExtra=(int)horaExtra+p3;
														
													
					  
							
							if (horaExtra>=1) {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+horaExtra+"'"
										+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR") +" and fecha_evento_cobmr='"+fecha+"'");
									
							}/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
								
							}*/
						
					
						
						}else {
							 
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
							tiempoHorasExtra25 = (obtenerDiferenciaMinutos(fechaIniNoc25, fechaFinNoc25)/60);
							if (tiempoHorasExtra25>=1) {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+tiempoHorasExtra25+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");					
							}
							
							/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));					
								
							}*/
			
		}    
						 
						 
						
				//Calculo de horas Extra al 100%		
						 
						 
						
						
						
		 			   }	//else {
					//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+0+"'  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

					//}	
						
						
					}
			   
				
					
					
				/*	if (horaExtra>=1 || tiempoHorasExtra25>=1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
						//tab_tabla.ejecutarSql();

					}else {
						
					}
					
					*/
					
					
					
					
					}
			
			
			
			
			
			
			
			int mesRestar = utilitario.getMes(utilitario.getFechaActual());
			mesRestar=mesRestar+1;
			TablaGenerica tabMes=utilitario.consultar("select ide_gemes,ide_geani from asi_horario_mes_empleado where ide_gemes not in ("+(mesRestar)+") group by ide_gemes,ide_geani order by ide_gemes,ide_geani desc");
			
			for (int i = 0; i < tabMes.getTotalFilas(); i++) {
				TablaGenerica tabAprobadoHorasExtra=utilitario.consultar("select ide_gtemp,ide_gemes from asi_horario_mes_empleado where ide_gemes="+Integer.parseInt(tabMes.getValor(i,"ide_gemes"))+" and "
						+ " ide_geani="+tabMes.getValor(i,"ide_geani")+" and aplica_hora_extra=true group by ide_gtemp,ide_gemes order by ide_gtemp asc");
		 String fechaInicio="";
		 String fechaFinal="";
		 int anioActualizar=0,mesActualizarEmpleado=0,mes=0;
		 String mesActualizar="";
		 anioActualizar=Integer.parseInt(tabMes.getValor(i,"ide_geani"));
		 mesActualizarEmpleado=Integer.parseInt(tabMes.getValor(i,"ide_gemes"));
		if (mesActualizarEmpleado<10) {
			mesActualizar="0"+mesActualizarEmpleado;
		}else {
			mesActualizar=""+mesActualizarEmpleado;
		}
		 
		TablaGenerica tabAnio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anioActualizar);

		 fechaInicio=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-01";
		 fechaFinal=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-31";

				
				for (int j = 0; j < tabAprobadoHorasExtra.getTotalFilas(); j++) {
		//			System.out.println("EMPLEADO INSERTADO"+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP"));
					
			/*		System.out.println("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" ");
*/
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" " );					
				}
			}


			
		//	getDiferenciaEntradaSalida(fechaIni,fechaFin);
			
			tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "					
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.horafinextra_cobmr,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
					//and epar.ide_gtemp in(149,508)
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
			
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla_hora_extra");
			
			tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(1,fechaIni,fechaFin));
			tab_tabla_sumatoria.ejecutarSql();
			utilitario.addUpdate("tab_tabla_sumatoria");
			System.out.println("Procesando Horas Extra");	
			cuatroUno();
			System.out.println("Fin Procesamiento de Horas Extra");	

		
		}
		
		}	 else {
			utilitario.agregarMensajeInfo("Rango de Fechas Inválidas",	"Fecha Inicial debe ser menor a Fecha Final");
			return;
		
	}
	 	
		}else {
				utilitario.agregarMensajeInfo("Rango de Fechas Inválidas",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
 } 




	//cuatroUno();
/*	boolean extra=false,extra25=false,extra100=false;
	
	for (int i = 0; i <tab_tabla.getTotalFilas();i++) {
		
		
		if(tab_tabla.getValor(i,"horafinextra_cobmr")==null || tab_tabla.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla.getValor(i,"horafinextra_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"horafinextra_cobmr").equals("0") )
		{
			extra=true;
		}else {
			extra=false;

		}
			 
		if(tab_tabla.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno25_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("0"))
		{
			extra25=true;
			}else {
				extra25=false;

			}
			
		
		if(tab_tabla.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno100_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("0"))
		{
			extra100=true;
		}else {
			extra100=false;

		}
		
		
		if (extra==true && extra25==true && extra100==true) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));

		}
	       

	}
	
	tab_tabla.setCondicion("aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
			//and ide_gtemp in(447,583) ");
	tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
	*/
	
}	
		


private Map<Integer, Boolean> obtenerDiasByHorario(TablaGenerica diasXHorario, Integer ide_horario){

	Map<Integer, Boolean> mapDias = new HashMap<Integer, Boolean>();
	for (int i=0 ; i<diasXHorario.getTotalFilas();i++) {
		if (ide_horario.equals(Integer.parseInt(diasXHorario.getValor(i, "IDE_ASHOR")))){
			mapDias.put(Integer.parseInt(diasXHorario.getValor(i, "IDE_GEDIA")), Boolean.TRUE);
		}
	}
	return mapDias;
}

private List<Fila> obtenerTimbreBiometricoNocturno(Integer IDE_GTEM,String FECHA_INICIAL,String FECHA_INICIAL1, String FECHA_FINAL, String FECHA_FINAL1){
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
			+ "IDE_COBIM,"
			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA,"
			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') as HORA "
			+ "from  con_biometrico_marcaciones  "
			+ "where FECHA_EVENTO_COBIM between '"+FECHA_INICIAL+"' AND  '"+FECHA_INICIAL1+"'  "
			+ "or  FECHA_EVENTO_COBIM between '"+FECHA_FINAL+"' AND  '"+FECHA_FINAL1+"'  " 
			+ "ORDER BY FECHA ASC");    	
	
	return tabObteberTimbreXEmpleado.getFilas();
	
}

	
private List<Fila> obtenerTimbreBiometrico(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
				+ "EMP.IDE_GTEMP,"
    			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
    			+ "from gth_empleado emp  "
    			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
    			+ "where emp.ide_astur is not null  and TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
    			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
    			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");    	
	return tabObteberTimbreXEmpleado.getFilas();
}



public void seleccionarHorasExtra(){
if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null ) {
	if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
		
		if (aut_empleados.getValor()!=null) {
			
		
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());	
		//.setCondicion("aprueba_hora_extra_cobmr=true");
		//tab_tabla.ejecutarSql();
	
		tab_tabla_hora_extra.setCondicion("ide_gtemp="+aut_empleados.getValor()+" and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
		tab_tabla_hora_extra.ejecutarSql();
		utilitario.addUpdate("tab_tabla");
		}else {
			tab_tabla_hora_extra.setCondicion("fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
			tab_tabla_hora_extra.imprimirSql();          		
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla");
		}
	
	}else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
		
	}
		
	
}
}



public String getFechaIni() {
	return fechaIni;
}



public void setFechaIni(String fechaIni) {
	this.fechaIni = fechaIni;
}



public String getFechaFin() {
	return fechaFin;
}



public void setFechaFin(String fechaFin) {
	this.fechaFin = fechaFin;
}



public SeleccionTabla getSel_mes() {
	return sel_mes;
}



public void setSel_mes(SeleccionTabla sel_mes) {
	this.sel_mes = sel_mes;
}



public void asignarMatriz(){
	
	sel_mes.dibujar();
}



public SeleccionTabla getSel_empleado() {
	return sel_empleado;
}



public void setSel_empleado(SeleccionTabla sel_empleado) {
	this.sel_empleado = sel_empleado;
}



public void abrirEmpleado(){
	int mesActual=0;
	  mes=sel_mes.getValorSeleccionado();
String anio="";
	  if (mes.equals("") ||  mes==null || mes.isEmpty()) {
		return;
	}else {
	mesActual=Integer.parseInt(mes);
	}
	  
	  
	  if (mesActual<10) {
		mes="0"+mesActual;
	}
	   
	  
	  if (mesActual>10) {
		anio="2017";
	}else {
		anio="2018";
	}
		sel_mes.cerrar();

	  fechaInicial=anio+"-"+mes+"-01";
	  fechaFinal=anio+"-"+mes+"-31";

	  
		sel_empleado.setSeleccionTabla("select cor.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
    			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
    			+ "from con_biometrico_marcaciones_resumen cor "
    			+ "left join gth_empleado emp on emp.ide_gtemp =cor.ide_gtemp "
    			+ "where cor.fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' "
    			+ "and cor.ide_gtemp in(select ide_gtemp from gth_empleado where ide_astur=1 "
    			+ "and (aprueba_hora_extra_cobmr = FALSE OR aprueba_hora_extra_cobmr IS NULL)  "
    			+ "order by ide_gtemp asc) "
    			+ "group by cor.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP,emp.apellido_paterno_gtemp,emp.apellido_materno_gtemp,primer_nombre_gtemp,emp.segundo_nombre_gtemp "
    			+ "order by emp.apellido_paterno_gtemp,emp.apellido_materno_gtemp,primer_nombre_gtemp,emp.segundo_nombre_gtemp  ","IDE_GTEMP");

    	sel_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
    	sel_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);

		//sel_empleado.setRadio();
    	sel_empleado.setTitle("Seleccione Empleado");
    	sel_empleado.setWidth("50");
    	sel_empleado.setHeight("47");


		gru_pantalla.getChildren().add(sel_empleado);
		sel_empleado.getBot_aceptar().setMetodo("actualizarEmpleadoHorasExtra");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
		utilitario.addUpdate("sel_empleado");
	  
	  
	  
	  
	  

	
}
















//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
public void cuatroUno(){
	

	boolean salidaAlm=false,bandSalidaHorarioAlm=false;

	/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
			+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
	*/
	 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

 
	String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//	double tiempoTrabajoXDia=0;
	double tiempoTrabajoXDia=0.0;
	int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
	double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,horaExtra25=0.0,horaExtra100=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	double acumuladoVacaciones25=0.0,acumuladoVacacionesTemp25=0.0,acumuladoVacaciones100=0.0,acumuladoVacacionesTemp100=0.0;
	double minutos=0.0,minutos25=0.0,minutos100=0.0;
	int horas=0,horas25=0,horas100=0;
	int p_t_enteraTemp=0,p_t_enteraTemp25=0,p_t_enteraTemp100=0;	
	double p_t_decimalTemp=0,p_t_decimalTemp25=0,p_t_decimalTemp100=0;	

	
	for (int i = 0; i < tab_tabla_hora_extra.getTotalFilas(); i++) {
			    
		    String horaFinExtra=tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr");
		    String horaRecargo25Entrada=tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr");
		    String  horaRecargo100Entrada=tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr");
					    
			int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
			double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	



			empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));
	//		System.out.println("empleadoTemporal   "+empleadoTemporal);
			empleado= Integer.parseInt(tab_tabla_hora_extra.getValor(i, "ide_gtemp"));
	//		System.out.println("empleado   "+empleado);
 

			if (empleado==empleadoTemporal) {
			anteriorEmpleado=1;
			}else {
			anteriorEmpleado=0;
			y++;
			}	
			
			if (tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr")==null || tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr").isEmpty()) {
				horaExtra=0.0;
			}else {
				horaExtra=Double.parseDouble(tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr"));

			}
			
			
			if (tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr").isEmpty()) {
				horaExtra25=0.0;
			}else {
				horaExtra25=Double.parseDouble(tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr"));

			}
			  
			 
			
			if (tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr").isEmpty()) {
				horaExtra100=0.0;
			}else {
				horaExtra100=Double.parseDouble(tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr"));
			//	System.out.println("100  recargo "+tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr"));

			}
			
			

			if (anteriorEmpleado==1) {
			//	System.out.println("fecha"+tab_tabla_hora_extra.getValor(i,"fecha_evento_cobmr"));

				acumuladoVacaciones=horaExtra;
				p_t_entera=(int)horaExtra+p_t_entera;
                p_t_enteraTemp=p_t_enteraTemp+p_t_entera;
			//	System.out.println("parte entera Acumulado: "+p_t_enteraTemp);

				p_t_decimal=(horaExtra-p_t_entera)+p_t_decimal;
				p_t_decimalTemp=p_t_decimalTemp+p_t_decimal;
			//	System.out.println("parte entera Decimal: "+p_t_decimalTemp);

			//	System.out.println("p_t_decimalTemp"+p_t_decimalTemp);

				//acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				 
				
				acumuladoVacaciones25=horaExtra25;
				p_t_entera25=(int)horaExtra25+p_t_entera25;
                p_t_enteraTemp25=p_t_enteraTemp25+p_t_entera25;
		//		System.out.println("parte entera Acumulado25: "+p_t_enteraTemp25);

				
				
				p_t_decimal25=(horaExtra25-p_t_entera25)+p_t_decimal25;
				p_t_decimalTemp25=p_t_decimalTemp25+p_t_decimal25;
			//	System.out.println("parte entera Decimal Acumulado25:  "+p_t_decimalTemp25);
			//	System.out.println("p_t_entera25"+p_t_entera25);
//				System.out.println("horaExtra100"+horaExtra100);

				
				acumuladoVacaciones100=horaExtra100;
				p_t_entera100=(int)horaExtra100+p_t_entera100;
                p_t_enteraTemp100=p_t_enteraTemp100+p_t_entera100;
			//	System.out.println("parte entera  Acumulado100:  "+p_t_enteraTemp100);


							
				p_t_decimal100=(horaExtra100-p_t_entera100)+p_t_decimal100;
				p_t_decimalTemp100=p_t_decimalTemp100+p_t_decimal100;
			//	System.out.println("parte entera  Decimal Acumulado100:  "+p_t_decimalTemp100);


				//	System.out.println("p_t_entera100"+p_t_entera100);
			//	System.out.println("p_t_decimal100"+p_t_decimal100);

				//acumuladoVacacionesTemp100=acumuladoVacacionesTemp100+horaExtra;
		
			}else {
				String valor="";
		//		System.out.println("ACUMULADO"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);
				
				if (  (((p_t_decimalTemp)/0.60) >= 1.0) || (((p_t_decimalTemp)/0.60) >= 1.0)  ) {
					horas=(int)((p_t_decimalTemp)/0.60);
					minutos=(((p_t_decimalTemp)/0.60)-horas)*0.60;

				}else {
					horas=0;
					minutos=((p_t_decimalTemp));

				}
		
				
				acumuladoVacacionesTemp=(p_t_enteraTemp)+(horas)+(minutos);
				//System.out.println("acumuladoVacacionesTemp"+acumuladoVacacionesTemp);

				
				horas25=(int)((p_t_decimalTemp25)/0.60);
				//System.out.println("horas25"+horas25);

				minutos25=((p_t_decimalTemp25)/0.60)-horas25;
				
			
				if (  (((p_t_decimalTemp25)/0.60) >= 1.0) || (((p_t_decimalTemp25)/0.60) >= 1.0)  ) {
					horas25=(int)((p_t_decimalTemp25)/0.60);
					minutos25=(((p_t_decimalTemp25)/0.60)-horas25)*0.60;

				}else {
					horas25=0;
					minutos25=((p_t_decimalTemp25));

				}
				
				
				
				
				acumuladoVacacionesTemp25=(p_t_enteraTemp25)+(horas25)+(minutos25);


				horas100=(int)((p_t_decimalTemp100)/0.60);
				minutos100=((p_t_decimalTemp100)/0.60)-horas100;
				
				
				if (  (((p_t_decimalTemp100)/0.60) >= 1.0) || (((p_t_decimalTemp100)/0.60) >= 1.0)  ) {
					horas100=(int)((p_t_decimalTemp100)/0.60);
					minutos100=(((p_t_decimalTemp100)/0.60)-horas100)*0.60;

				}else {
					horas100=0;
					minutos100=((p_t_decimalTemp100));

				}
				
	
				
				
				/*
				double minResultadoCalculo100=minutos100*60;
				int decNumberInt100=0;
				String str100="";
				if (minResultadoCalculo100>=1) {
					decNumberInt100=(int)minResultadoCalculo100;
					str100="0."+decNumberInt100;

				}else {
					str100=""+minResultadoCalculo100;
				}
				*/
			
				
				acumuladoVacacionesTemp100=(p_t_enteraTemp100)+(horas100)+(minutos100);

				
				tab_tabla_sumatoria.setValor(x,"HORAS_NOCTURNAS",""+acumuladoVacacionesTemp25);
				tab_tabla_sumatoria.setValor(x,"HORAS_SUPLEMENTARIAS",""+acumuladoVacacionesTemp);
				tab_tabla_sumatoria.setValor(x,"HORAS_EXTRA",""+acumuladoVacacionesTemp100);


				x++;
				acumuladoVacaciones=horaExtra;
				acumuladoVacacionesTemp=0;
				acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				//System.out.println("nuevoAcumulado"+acumuladoVacacionesTemp25);

				
				acumuladoVacaciones25=horaExtra25;
				acumuladoVacacionesTemp25=0;
				acumuladoVacacionesTemp25=acumuladoVacacionesTemp25+horaExtra25;
				//System.out.println("nuevoAcumulado25"+acumuladoVacacionesTemp25);
				
				acumuladoVacaciones100=horaExtra100;
				acumuladoVacacionesTemp100=0;
				acumuladoVacacionesTemp100=acumuladoVacacionesTemp100+horaExtra100;		
				//System.out.println("nuevoAcumulado100"+acumuladoVacacionesTemp25);

				empleadoTemporal=Integer.parseInt(tab_tabla_hora_extra.getValor(i, "ide_gtemp"));
				
				/* p_t_entera=0;p_t_entera25=0;p_t_entera100=0;	
				 p_t_enteraTemp=0;p_t_enteraTemp25=0;p_t_enteraTemp100=0;
				 */
				minutos=0.0;minutos25=0.0;minutos100=0.0;
				 horas=0;horas25=0;horas100=0;
				 p_t_decimalTemp=0.0;
				 p_t_decimalTemp25=0.0;
				 p_t_decimalTemp100=0.0;
			     p_t_enteraTemp=0;
			     p_t_enteraTemp25=0;
			     p_t_enteraTemp100=0;	

			    //Acumulo el valor de la hora extra para el nuevo empleado 
			    p_t_entera=(int)horaExtra+p_t_entera;
                p_t_enteraTemp=p_t_enteraTemp+p_t_entera;
                //System.out.println("parte entera Acumulado: "+p_t_enteraTemp);

				p_t_decimal=(horaExtra-p_t_entera)+p_t_decimal;
				p_t_decimalTemp=p_t_decimalTemp+p_t_decimal;
				//System.out.println("parte entera Decimal: "+p_t_decimalTemp);

				p_t_entera25=(int)horaExtra25+p_t_entera25;
                p_t_enteraTemp25=p_t_enteraTemp25+p_t_entera25;
                //System.out.println("parte entera Acumulado25: "+p_t_enteraTemp25);

			
				p_t_decimal25=(horaExtra25-p_t_entera25)+p_t_decimal25;
				p_t_decimalTemp25=p_t_decimalTemp25+p_t_decimal25;
				//System.out.println("parte entera Decimal Acumulado25:  "+p_t_decimalTemp25);

				
				p_t_entera100=(int)horaExtra100+p_t_entera100;
                p_t_enteraTemp100=p_t_enteraTemp100+p_t_entera100;
				//System.out.println("parte entera  Acumulado100:  "+p_t_enteraTemp100);
		
				p_t_decimal100=(horaExtra100-p_t_entera100)+p_t_decimal100;
				p_t_decimalTemp100=p_t_decimalTemp100+p_t_decimal100;
				//System.out.println("parte entera  Decimal Acumulado100:  "+p_t_decimalTemp100);

		
			
			}
			
	
	
		}
	    	
	    	


}


 


	
	public String getSumatoriaAtrasos(int parametro,String strFechaIniReporte, String strFechaFinReporte){
		String consulta="";
		
		if (parametro==0) {
		

			
consulta="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||    "
		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
		+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,  "
		+ "'' as HORAS_NOCTURNAS,  "
		+ "'' as HORAS_SUPLEMENTARIAS,   "
		+ "'' as HORAS_EXTRA  "
		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
		+ "where epar.activo_geedp=true  "
		+ "and res.aprueba_hora_extra_cobmr=true and emp.ide_gtemp=-1   "
		+ "GROUP BY EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP ,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, "
		+ "SUCU.NOM_SUCU,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,HORAS_NOCTURNAS,HORAS_SUPLEMENTARIAS,HORAS_EXTRA "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";

}else {
	consulta="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||    "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,  "
			+ "'' as HORAS_NOCTURNAS,  "
			+ "'' as HORAS_SUPLEMENTARIAS,   "
			+ "'' as HORAS_EXTRA  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' "
			+ "and res.aprueba_hora_extra_cobmr=true    "
			+ "GROUP BY EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP ,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,HORAS_NOCTURNAS, HORAS_SUPLEMENTARIAS,HORAS_EXTRA "
			+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";
}
		
		
		
		

return consulta;
	}

 

	public Tabla getTab_tabla_sumatoria() {
		return tab_tabla_sumatoria;
	}



	public void setTab_tabla_sumatoria(Tabla tab_tabla_sumatoria) {
		this.tab_tabla_sumatoria = tab_tabla_sumatoria;
	}



	
	
	
	
	public String getDiferenciaEntradaSalida(String strFechaIniReporte, String strFechaFinReporte){
		
		String sql="";
		if (strFechaFinReporte==null || strFechaFinReporte.equals("") || strFechaFinReporte.isEmpty() || strFechaIniReporte==null || strFechaIniReporte.equals("")
				|| strFechaIniReporte.isEmpty()) {
			
		
		
		sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.ide_cobmr, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horafinhorario_cobmr,  "
				+ "res.horafinbiometrico_cobmr,  "
				+ "res.horafinextra_cobmr,  "
				+ "res.recargonocturno25_cobmr,  "
				+ "res.recargonocturno100_cobmr, "
				+ "GTT.DETALLE_GTTEM  "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
				+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true   "
				+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
		}else {
			sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.horafinextra_cobmr,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' "
					+ " and res.aprueba_hora_extra_cobmr=true  " 
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
}
		return sql;
			
	}





	public Tabla getTab_tabla_hora_extra() {
		return tab_tabla_hora_extra;
	}





	public void setTab_tabla_hora_extra(Tabla tab_tabla_hora_extra) {
		this.tab_tabla_hora_extra = tab_tabla_hora_extra;
	}
	

	public void actualizarEmpleadoHorasExtra(){
		
		empleado=sel_empleado.getSeleccionados();
		sel_empleado.cerrar();
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true where ide_gtemp in("+empleado+") and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' ");

		utilitario.agregarMensajeInfo("Empleado(s) Asignado(s) Correctamente", "Hora(s) Extra(s) Generadas ");
		guardarPantalla();
		//tab_tabla.setCondicion("ide_gtemp in ("+empleado+") and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' ");
		//tab_tabla.ejecutarSql();
		//utilitario.addUpdate("tab_tabla");
		

		
		
		
	}

public void	pegarHorario(){

	
	//Validacion Entrada
	String horarioDiurnoEntradaInicio="00:00:00",horarioDiurnoEntradaFin="12:00:00";
	String horarioTardeEntradaInicio="12:00:00",horarioTardeEntradaFin="18:00:00";
	String horarioNocturnoEntradaInicio="20:00:00",horarioNocturnoEntradaFin="24:00:00";

	//Validacion Salida
	
	String horarioDiurnoEntradaInicioSalida="20:00:00";
	
	boolean diurno=false,tarde=false,nocturno=false;
	String empleado="";
	
	String horaEntradaPlanificada="",horaSalidaPlanificada="";
	
	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
 		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
 			fechaIni=(cal_fecha_inicial.getFecha());	
     		fechaFin=(cal_fecha_final.getFecha());
 		
     		TablaGenerica tab_tabla_pruebas= utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
     				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr,  "
     				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
     				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
     				+ "horafinband_cobmr, horafinextra_cobmr, recargonocturno25_cobmr,  "
     				+ "recargonocturno100_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
     				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
     				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
     				+ "ide_aspvh "
     				+ "FROM con_biometrico_marcaciones_resumen where horainiciobiometrico_cobmr='' and horafinbiometrico_cobmr='' "
     				+ "and  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' order by ide_gtemp asc, fecha_evento_cobmr asc");
			     			
 		TablaGenerica tabEmpleado=null,tabConBiometrico=null,tabConBiometricoNocturno=null;
 		String tarjeta_marcacion_gtemp="",fecha_cobim="";
 		for (int i = 0; i < tab_tabla_pruebas.getTotalFilas(); i++) {
 			
 			
     		horaEntradaPlanificada=tab_tabla_pruebas.getValor(i,"horainiciohorario_cobmr");
     		horaSalidaPlanificada=tab_tabla_pruebas.getValor(i,"horafinhorario_cobmr");
     		String fecha_evento_cobmr=tab_tabla_pruebas.getValor(i,"fecha_evento_cobmr");

     		empleado=tab_tabla_pruebas.getValor(i,"IDE_GTEMP");
 			int contadorMarcaciones=0;
			//Consulto la tarjeta de marcacion del  empleado 
 			//tabEmpleado=utilitario.consultar("select ide_gtemp,tarjeta_marcacion_gtemp from gth_empleado emp where ide_gtemp="+tab_tabla_pruebas.getValor(i,"IDE_GTEMP"));
 			//tarjeta_marcacion_gtemp=tabEmpleado.getValor("tarjeta_marcacion_gtemp");
 			
			//Consulto  empleado 
 			tabConBiometrico=utilitario.consultar("select "
    				+ "EMP.IDE_GTEMP,"
        			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
        			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
        			+ "from gth_empleado emp  "
        			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
        			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr")+"' AND '"+tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr")+"' "
        			+ "AND EMP.IDE_GTEMP="+empleado+"  "
        			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");
 			
 			if (tabConBiometrico.getTotalFilas()==0) {
				System.out.println("SIN MARCACIONES");
			}else {
				contadorMarcaciones=tabConBiometrico.getTotalFilas();
	 			String horaBiometricoPegarInicio="",horaBiometricoPegarFin="";

				if (contadorMarcaciones>1) {
	 			horaBiometricoPegarInicio=tabConBiometrico.getValor(0,"HORAM");
	 			horaBiometricoPegarFin=tabConBiometrico.getValor(contadorMarcaciones-1,"HORAM");

				}

				
				if(  (horaBiometricoPegarInicio.compareTo(horarioDiurnoEntradaInicio)>=0)  &&  (horaBiometricoPegarInicio.compareTo(horarioDiurnoEntradaFin)<=0)){
					System.out.println("INGRESO DIURNO : "+empleado+"   "+fecha_evento_cobmr+" "+horaBiometricoPegarInicio+"    "+horaBiometricoPegarFin);
					
					if((horaBiometricoPegarFin.compareTo(horarioTardeEntradaInicio)>=0) && (horaBiometricoPegarFin.compareTo(horarioNocturnoEntradaInicio)<0)){
						//System.out.println("INGRESO DIURNO : "+tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr"));
						
	 					//System.out.println("UPDATE EMPLEADO : "+empleado+" ENTRADA: "+horaBiometricoPegarInicio+" SALIDA: "+horaBiometricoPegarFin);
	 					
	 					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarInicio+"',horafinbiometrico_cobmr='"+horaBiometricoPegarFin+"'"
	 							+ "where ide_gtemp in ("+empleado+") "
	 							+ " and fecha_evento_cobmr between '"+fecha_evento_cobmr+"' and '"+fecha_evento_cobmr+"'");

	 				//	break;
					}else {
	 				//	System.out.println("INGRESO DIURNO NOCTURNO");

							}
					
					
					
					
					if((horaBiometricoPegarFin.compareTo(horarioNocturnoEntradaInicio)>=0) && (horaBiometricoPegarFin.compareTo(horarioNocturnoEntradaFin)<=0)){
	 					//System.out.println("DIURNO NOCTURNO");
	 					String fechaNocturno="",horaFinNocturno;
					fechaNocturno=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr")), 1));
	 			
	 					
					tabConBiometricoNocturno=utilitario.consultar("select "
	 		    				+ "EMP.IDE_GTEMP,"
	 		        			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
	 		        			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
	 		        			+ "from gth_empleado emp  "
	 		        			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
	 		        			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaNocturno+"' AND '"+fechaNocturno+"' "
	 		        			+ "AND EMP.IDE_GTEMP="+empleado+"  "
	 		        			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");
					
					horaFinNocturno=tabConBiometricoNocturno.getValor(0,"HORAM");
				//	System.out.println("fecha inicio: "+tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr")+"    fecha fin "+fechaNocturno);

					//System.out.println("UPDATE EMPLEADO : "+empleado+" ENTRADA: "+horaBiometricoPegarFin+" SALIDA: "+horaFinNocturno);
					
 					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarFin+"',horafinbiometrico_cobmr='"+horaFinNocturno+"'"
 							+ "where ide_gtemp in ("+empleado+") "
 							+ " and fecha_evento_cobmr between '"+fecha_evento_cobmr+"' and '"+fecha_evento_cobmr+"'");

					}
					
					
					
					
					
					
							
				}
				
			/*	String horarioDiurnoEntradaInicio="00:00:00",horarioDiurnoEntradaFin="12:00:00";
				String horarioTardeEntradaInicio="12:00:00",horarioTardeEntradaFin="18:00:00";
				String horarioNocturnoEntradaInicio="20:00:00",horarioNocturnoEntradaFin="24:00:00";
				*/
				
				
	 			if((horaBiometricoPegarInicio.compareTo(horarioTardeEntradaInicio)>=0) && (horaBiometricoPegarInicio.compareTo(horarioTardeEntradaFin)<=0)){
	 				//System.out.println("INGRESO TARDE: "+tab_tabla_pruebas.getValor(i, "fecha_evento_cobmr"));
	 				//String horaEntradaTarde="";
	 				//horaEntradaTarde=horaBiometricoPegarInicio;
		 			if((horaBiometricoPegarFin.compareTo(horaBiometricoPegarInicio)>0) && (horaBiometricoPegarFin.compareTo(horarioNocturnoEntradaFin)<=0)){
						System.out.println("UPDATE EMPLEADO TARDE: "+empleado+" ENTRADA: "+horaBiometricoPegarInicio+" SALIDA: "+horaBiometricoPegarFin);
	 					//contadorMarcaciones
	 					
	 					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarInicio+"',horafinbiometrico_cobmr='"+horaBiometricoPegarFin+"'"
	 							+ "where ide_gtemp in ("+empleado+") "
	 							+ " and fecha_evento_cobmr between '"+fecha_evento_cobmr+"' and '"+fecha_evento_cobmr+"'");
	
		 			}

 	

				}
				
 				if((horaBiometricoPegarInicio.compareTo(horarioNocturnoEntradaInicio)>=0) && (horaBiometricoPegarInicio.compareTo(horarioNocturnoEntradaFin)<=0)){
 					System.out.println("NOCTURNO ENTRADA");
 					System.out.println("UPDATE EMPLEADO : "+empleado+" ENTRADA: "+horaBiometricoPegarInicio+" SALIDA: "+horaBiometricoPegarFin);

				}

				

			
			
			} 
			
 		
 			

 		
 			
 			
 			
		}
 		
 		
 		//if de si la fecha es null
 		}
//		if si las fechas ingresadas son invalidas
	}


}
















}
