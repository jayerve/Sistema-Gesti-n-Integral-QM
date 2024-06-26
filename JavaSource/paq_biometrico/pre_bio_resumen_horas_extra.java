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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

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
public class pre_bio_resumen_horas_extra extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	TablaGenerica  obtenerTurnoEmpleado;

  	 String fechaIni="",fechaFin="";
    
    public pre_bio_resumen_horas_extra() {
    	
    	
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
    	bot_rep_biometrico.setValue("Horas Extra");
    	bot_rep_biometrico.setTitle("Horas Extra");
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
		
		bar_botones.agregarComponente(aut_empleados);
	
    	
    	
    		// boton limpiar
    	
     	
    	Boton bot_departamento_biometrico= new Boton();
    	bot_departamento_biometrico.setIcon("ui-icon-calculator");
    	bot_departamento_biometrico.setMetodo("seleccionarHorasExtra");
    	bot_departamento_biometrico.setValue("VER HORAS EXTRA");
    	bot_departamento_biometrico.setTitle("VER HORAS EXTRA");
    //	bar_botones.agregarBoton(bot_departamento_biometrico);
    	
    	
    	
    
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
    	bot_justificar.setValue("Calcular Horas Extra");
    	bot_justificar.setTitle("Justificación Masiva");
    	bar_botones.agregarBoton(bot_justificar);
       
		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

    	
    	
    	set_departamento.setId("set_departamento");
    	
    	
    	
    	
    	
    	
			tab_tabla.setId("tab_tabla");
			tab_tabla.setTabla("con_biometrico_marcaciones_resumen", "ide_cobmr", 1);
			
			tab_tabla.getColumna("IDE_COBMR").setLongitud(30);
	        tab_tabla.getColumna("IDE_COBMR").setNombreVisual("CÓDIGO");
	        tab_tabla.getColumna("IDE_COBMR").setOrden(1);


			tab_tabla.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
					"FROM GTH_EMPLEADO EMP");
			tab_tabla.getColumna("ide_gtemp").setAutoCompletar();

			tab_tabla.getColumna("IDE_GTEMP").setLongitud(30);
	        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
	        tab_tabla.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO");
	        tab_tabla.getColumna("IDE_GTEMP").setLectura(true);
	        tab_tabla.getColumna("IDE_GTEMP").setOrden(2);

	        
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLectura(true);
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setOrden(3);

	        tab_tabla.getColumna("DIA_COBMR").setNombreVisual("DÍA");
	        tab_tabla.getColumna("DIA_COBMR").setLectura(true);  
	        tab_tabla.getColumna("DIA_COBMR").setOrden(4);
	         
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setLongitud(5);
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setNombreVisual("ENTRADA");
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setOrden(5);


	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setNombreVisual("HOR.SAL");  
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setOrden(6);



	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setNombreVisual("TIMBRE ENTRADA");
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setOrden(7);

	        
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setNombreVisual("TIM.HOR.SAL");  
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setOrden(8);

	        
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setNombreVisual("ESTADO ENTRADA");
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setOrden(9);
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setVisible(false);

	        
	        
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setNombreVisual("H.INI.ALM");
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setVisible(false);
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setOrden(10);

	             

	        tab_tabla.getColumna("HORAFINALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAFINALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINALM_COBMR").setNombreVisual("H.FIN.ALM");
	        tab_tabla.getColumna("HORAFINALM_COBMR").setVisible(false);
	        tab_tabla.getColumna("HORAFINALM_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAFINALM_COBMR").setOrden(11);

	        
	        
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setNombreVisual("TIM.INI.ALM");  
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setVisible(false);
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setOrden(12);


	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setNombreVisual("TIM.FIN.ALM");  
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setVisible(false);
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setOrden(13);

	        
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("TIEMPOALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setNombreVisual("TIM.TOMA.ALM"); 
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setVisible(false);
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setLectura(true);
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setOrden(14);

	          
	        
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setNombreVisual("TIEMPOHORALM"); 
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setVisible(false);
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setLectura(true);
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setOrden(15);

	                
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAFINBAND_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setNombreVisual("ESTADO SALIDA");  
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setLectura(true);
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setOrden(16);
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setVisible(false);



	        
	        tab_tabla.getColumna("recargonocturno25_cobmr").setLongitud(15);
	        tab_tabla.getColumna("recargonocturno25_cobmr").alinearCentro();
	        tab_tabla.getColumna("recargonocturno25_cobmr").setNombreVisual("RECARGO NOCTURNO 25%");  
	        tab_tabla.getColumna("recargonocturno25_cobmr").setLectura(true);
	        tab_tabla.getColumna("recargonocturno25_cobmr").setOrden(17);


	        
	        tab_tabla.getColumna("horafinextra_cobmr").setLongitud(15);
	        tab_tabla.getColumna("horafinextra_cobmr").alinearCentro();
	        tab_tabla.getColumna("horafinextra_cobmr").setNombreVisual("HORA SUPLEMENTARIA"); 
	        tab_tabla.getColumna("horafinextra_cobmr").alinearCentro();
	        tab_tabla.getColumna("horafinextra_cobmr").setFormatoNumero(2);
	        tab_tabla.getColumna("horafinextra_cobmr").setLectura(true);
	        tab_tabla.getColumna("horafinextra_cobmr").setOrden(18);
	        
	        
	        tab_tabla.getColumna("recargonocturno100_cobmr").setLongitud(15);
	        tab_tabla.getColumna("recargonocturno100_cobmr").alinearCentro();
	        tab_tabla.getColumna("recargonocturno100_cobmr").setNombreVisual("RECARGO 100%");  
	        tab_tabla.getColumna("recargonocturno100_cobmr").setFormatoNumero(2);
	        tab_tabla.getColumna("recargonocturno100_cobmr").setLectura(true);
	        tab_tabla.getColumna("recargonocturno100_cobmr").setOrden(19);

	        tab_tabla.getColumna("aprueba_hora_extra_cobmr").setLongitud(15);
	        tab_tabla.getColumna("aprueba_hora_extra_cobmr").setNombreVisual("APROBAR H.EXTRA");  
	        tab_tabla.getColumna("aprueba_hora_extra_cobmr").setCheck();  
	        tab_tabla.getColumna("aprueba_hora_extra_cobmr").setOrden(20);

	        
	        tab_tabla.getColumna("novedad_cobmr").setLongitud(15);
	        tab_tabla.getColumna("novedad_cobmr").alinearCentro();
	        tab_tabla.getColumna("novedad_cobmr").setNombreVisual("NOVEDAD");  
	        tab_tabla.getColumna("novedad_cobmr").setLectura(true);
	        tab_tabla.getColumna("novedad_cobmr").setVisible(false);
	        tab_tabla.getColumna("novedad_cobmr").setOrden(21);


	        tab_tabla.getColumna("inconsistencia_biometrico_cobmr").setVisible(false);
	        tab_tabla.getColumna("recargonocturno25_cobmr").setVisible(false);

	        

	        tab_tabla.getColumna("p_entrada_cobmr").setVisible(false);
	        tab_tabla.getColumna("p_salida_cobmr").setVisible(false);
	        tab_tabla.getColumna("p_alm_cobmr").setVisible(false);

	        tab_tabla.setCondicion("ide_COBMR=-1");
	       //tab_tabla.setCondicion("ide_gtemp in(205)");   
	        //tab_tabla.setCondicion("fecha_evento_cobmr between '2017-12-01' and '2017-12-31' and ide_gtemp=274");
	        	//	+ "and ide_gtemp in(447,583)");
	        
	        //tab_tabla.setCampoOrden("IDE_GTEMP");
	        tab_tabla.setHeader("REPORTE RESUMEN HORAS EXTRA BIOMÉTRICO");
	        tab_tabla.setRows(25);
			tab_tabla.dibujar();
			PanelTabla pat_panel = new PanelTabla();
			pat_panel.setPanelTabla(tab_tabla);
        
         Division div_div1 = new Division();
        div_div1.dividir1(pat_panel);        
        agregarComponente(div_div1);

        
        
    }

    
    
    public void calcularHorasExtra(){
    	
		
    	try {
    		
    		
    		tab_tabla.setLectura(false);
		    int banderaJustificaEntrada=0;
		    String justificacionEntrada="";
		    String justificacionAlmuerzoSalida="";
		    String justificacionAlmuerzoEntrada="";
		    String justificacionSalida="";
		   // int banderaEntrada=0;
		    //int banderaSalida=0;

    		
		    
		    TablaGenerica  tabMarcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
		   int empleado= Integer.parseInt(tabMarcaciones.getValor("IDE_GTEMP"));
	        
		    String fecha=tabMarcaciones.getValor("FECHA_EVENTO_COBMR");
		    String horaEntrada=tabMarcaciones.getValor("HORAINICIOBIOMETRICO_COBMR");
		    String horaSalida=tabMarcaciones.getValor("HORAFINBIOMETRICO_COBMR");
		 
		    String horarioSalida=tab_tabla.getValor("HORAFINHORARIO_COBMR");
	        String horarioEntrada=tab_tabla.getValor("HORAINICIOHORARIO_COBMR");
	        
	        
		
		    	    
		    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
		    String  permiso;


		    double tiempoHorasExtra=0.0;
		    double tiempoHorasCalculoExtra=0.0;

		    Date fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
		    Date fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
			tiempoHorasExtra = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra)/60);
			if (tiempoHorasExtra>=8) {
		    
		    Date fechaIniCalculoExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
		    Date fechaFinCalculoExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
			 tiempoHorasCalculoExtra = (obtenerDiferenciaMinutos(fechaIniCalculoExtra, fechaFinCalculoExtra)/60);
		
				String horaExtra="";
						
			if (tiempoHorasCalculoExtra>=0.75) {
			if (tiempoHorasCalculoExtra>=0.75 && tiempoHorasCalculoExtra<=0.99) {
			horaExtra="1.0";
			}else if (tiempoHorasCalculoExtra<0.75) {
			horaExtra="0.0";
			}
			else {
				int p_ent=0;
				double horasnuevas=0.0;
				p_ent=(int)tiempoHorasCalculoExtra;
				
				
				horaExtra=utilitario.getFormatoNumero((tiempoHorasCalculoExtra),2);
			}	
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+Double.parseDouble(horaExtra)+"'  where ide_cobmr="+tab_tabla.getValorSeleccionado());
			tab_tabla.ejecutarSql();
			utilitario.addUpdate("tab_tabla");
			}
			}
			
			else{
			utilitario.agregarMensajeInfo("Usted no ha cumplido con las 8 horas de trabajo", "Contiene novedades");	
			}
			
		
    		
  	} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}		
			
			
    }
    
    
    
    public void seleccionarReporteTotal(){
        
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null ) {
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
     		fechaIni=(cal_fecha_inicial.getFecha());	
     		fechaFin=(cal_fecha_final.getFecha());
 		}	 else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
			return;
        		
	}
	 	tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
	 	//tab_tabla.setCampoOrden("IDE_GTEMP");
       		tab_tabla.ejecutarSql();
        		utilitario.addUpdate("tab_tabla");
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe seleccionar Fecha Inici y Fecha Fin");
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
    
	public void getEmpleado(){
		
	  	
		StringBuilder str_ide=new StringBuilder();
       getMarcacionesEmpleado(aut_empleados.getValor().toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
        set_empleado.cerrar();		
	
    			
    		
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
    
	
	public void getDepartamento(){

			
		
		StringBuilder str_ide=new StringBuilder();
         int int_num_col_idegedep=set_departamento.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
         for (Fila filaActual:set_departamento.getTab_seleccion().getSeleccionados()) {
      	   
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
         
          
          getMarcacionesEmpleado(str_ide_empleado.toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
          set_departamento.cerrar();		
 			
	}

	
	
    @SuppressWarnings("unused")
	public void getMarcacionesEmpleado(String IDE_GTEMP,String fechaInicial, String fechaFinal ){
		int contador = 0;

		int entrada=0,almuerzo=0,salida=0;
	    int entradaNocturno=0,salidaNocturno=0;

		 String fechaBiometrico="";

 		tab_tabla.setLectura(false);

 		
    	TablaGenerica tab_reporte;
    	if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null) {
    		 tab_tabla.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' ");
    		 tab_tabla.ejecutarSql();
    		 if (tab_tabla.getTotalFilas()<0) {
				utilitario.agregarMensajeInfo("No existen Registro(s)", "La fechas seleccionadas no contienen datos ");
			}
    		 
    		 
		}else{
			 tab_tabla.setCondicion("IDE_GTEMP='"+IDE_GTEMP+"' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' ");
    		 tab_tabla.ejecutarSql();
    		 if (tab_tabla.getTotalFilas()<0) {
 				utilitario.agregarMensajeInfo("No existen Registro(s)", "El empleado no contienen datos ");
 			}
  		}

 
    }	

  
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
  
    	
    	for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
    		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr="+tab_tabla.getValor(i,"aprueba_hora_extra_cobmr")+" where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
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



	public Tabla getTab_tabla() {
		return tab_tabla;
	}



	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
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
		     	tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
	       				+ "and ide_gtemp='"+aut_empleados.getValor()+"' ");
	    		tab_tabla.ejecutarSql();
    		utilitario.addUpdate("tab_tabla");

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

	
	public void actualizarHorario(AjaxBehaviorEvent evt){
		tab_tabla.modificar(evt);
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
	tab_tabla.setLectura(false);
	int contador = 0;

	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;

	 String fechaBiometrico="";
	
//	Tabla que contiene las fechas de timbre del empleado	
	
	 
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
  		fechaIni=(cal_fecha_inicial.getFecha());	
  		fechaFin=(cal_fecha_final.getFecha());
		}	 else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
			return;
		
	}
	 	tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =true");
	 	tab_tabla.ejecutarSql();
	 	utilitario.addUpdate("tab_tabla");
		}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe seleccionar Fecha Inici y Fecha Fin");
				return;
			
 }


	if (tab_tabla.isEmpty()) {
		utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
	}
	
	
	for (int itemReporte=0; itemReporte< tab_tabla.getTotalFilas(); itemReporte++) {
		//Obtengo el ide del empleado con el cual obtendrenmos los horarios
		Integer ide_gtemp = Integer.parseInt(tab_tabla.getValor(itemReporte, "IDE_GTEMP"));
		//fecha de registro en la tabla resumen
		String fechaBiometricoAgrupadaXEmpleado = tab_tabla.getValor(itemReporte,"fecha_evento_cobmr");
		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		//Obtengo los horarios de cada empleado de acuerdo a su ide_gtemp
		//Unimos la fecha con la hora
		String fechaHoraBiometrico = fechaBiometricoAgrupadaXEmpleado+" 01:00:00";
	    //Calendario 
		Calendar calFechaHoraBiometrico = Calendar.getInstance();
	    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
		
		
		//Obtengo datos de marcaciones de Entrada y Salida por empleado

	    String fecha=tab_tabla.getValor(itemReporte,"FECHA_EVENTO_COBMR");
	    String horaEntrada=tab_tabla.getValor(itemReporte,"HORAINICIOBIOMETRICO_COBMR");
	    String horaSalida=tab_tabla.getValor(itemReporte,"HORAFINBIOMETRICO_COBMR");
	    String empleado=tab_tabla.getValor(itemReporte,"ide_gtemp");
	 
	    String horarioSalida=tab_tabla.getValor(itemReporte,"HORAFINHORARIO_COBMR");
        String horarioEntrada=tab_tabla.getValor(itemReporte,"HORAINICIOHORARIO_COBMR");
        String horaDescanso=tab_tabla.getValor(itemReporte,"tiempohoralm_cobmr");
        String horaSinAlmuerzo=tab_tabla.getValor(itemReporte,"horainicioalm_cobmr");
        String estadoEntrada=tab_tabla.getValor(itemReporte,"horainicioband_cobmr");
    	
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
	    
		String fechaHoraHorarioInicioEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
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
								+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
					
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
			+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

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
							+ "  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

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
			    		if (horaDescanso.equals("30")) {
			    			tiempoAlm=30;
						}else if (horaDescanso.equals("60")) {
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
							+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));


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
								+ "  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
							
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
								+ " where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));					
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
				+ " ide_geani="+tabMes.getValor(i,"ide_geani")+" group by ide_gtemp,ide_gemes order by ide_gtemp asc");
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
			System.out.println("EMPLEADO INSERTADO"+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP"));
			
			System.out.println("update con_biometrico_marcaciones_resumen set  "
			+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
					+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" ");

	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
			+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
					+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" " );					
		}
	}


	tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
	
		

	 
	tab_tabla.setCondicion("aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
			//ide_gtemp in(447,583)");
				tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
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
	
		tab_tabla.setCondicion("ide_gtemp="+aut_empleados.getValor()+" and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_tabla");
		}else {
			tab_tabla.setCondicion("fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
			tab_tabla.imprimirSql();          		
	   		tab_tabla.ejecutarSql();
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






}
