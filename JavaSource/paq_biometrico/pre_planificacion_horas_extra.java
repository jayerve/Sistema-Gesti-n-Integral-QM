/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.awt.image.BandedSampleModel;
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

import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Consulta;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imprimir;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_planificacion_horas_extra extends Pantalla {

    private Tabla tab_resumen_marcaciones = new Tabla(); 
    
    
    private Tabla tab_novedad = new Tabla();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private boolean banderaFeriados=false,sinIngreso=false;
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    
    @EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

    private Tabla tab_marcaciones = new Tabla();

    TablaGenerica tabDiasFeriados;
    
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	String horaAnterior="",horaAnteriorAlm="",horaAnteriorAlm1="";
	int ide_gtgre=0,turno=0;
	
	private boolean estado_empleado_nocturno=false;
	private boolean banderaCambioHorario,banderaCambioHorarioPermisoOnline;
    private Integer ide_geedp;
    private SeleccionTabla sel_editar_horario= new SeleccionTabla();
	private AutoCompletar aut_empleados=new AutoCompletar();

      
	private String  pos_marcacionEntrada="",pos_marcacionTarde="",pos_marcacionNocturno="",horaActualizarPegado="";

    
    //Estados Timbre
    
    private Integer estadoMarcacionBiometrico=0,numMarcacionesBiometrico=0;
	StringBuilder str_ide=new StringBuilder();
	private String jefeAsignacionHorario="";
	
     	TablaGenerica tabEmpDep=null;
     	TablaGenerica tabEmpleado=null;
    public pre_planificacion_horas_extra() {

    	//Implementamos metodo 
    	bloquearBotones();
		utilitario.agregarNotificacionInfo("NOTA - ENVIO DE PLANIFICACION DE HORAS EXTRA","Recuerde para la aprobacion de la planificacion de horas  extra, debera ser remitida a TTHH ");

    	
		Boton bot_impor_valores = new Boton();
		bot_impor_valores.setValue("Importar Valores");
		bot_impor_valores.setIcon("ui-icon-note");
		bot_impor_valores.setMetodo("abrirDialogoImportar");
		bar_botones.agregarBoton(bot_impor_valores);
    	
    	   String fecha =utilitario.getFechaActual();
           String fechaActual=getFechaAsyyyyMMdd(sumarRestarMeses(getFechaAsyyyyMMdd(fecha), -1));
           String mesDespliege="";
           int mesTemp=utilitario.getMes(fechaActual);
           String mes="";
           if (mesTemp<10) {
   			mes="0"+mesTemp;
   		}else {
   			mes=""+mesTemp;
   		}
           
          String fechaConsultaInicio="";
          String fechaConsultaFin="";

          int diaFinMes=retornarDiasMes(1,Integer.parseInt(mes),utilitario.getAnio(fechaActual));
          fechaConsultaInicio=utilitario.getAnio(fechaActual)+"-"+mes+"-01";
          fechaConsultaFin=utilitario.getAnio(fechaActual)+"-"+mes+"-"+diaFinMes;
          tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
          boolean bandSinArea=false;
          if (tabEmpDep.getTotalFilas()>0) {
			bandSinArea=true;
		}else {
			bandSinArea=false;
		}
         	jefeAsignacionHorario=tabEmpDep.getValor("ide_geedp");
         	tabEmpleado= utilitario.consultar("SELECT ide_gtemp, "
         			+ "ide_geedp, "
         			+ "ide_geedp_cambio "
         			+ "FROM asi_horario_mes_empleado where ide_geedp="+jefeAsignacionHorario+" and ide_gemes="+mes+"");
         	
         	
       	int valor=0;
   		String horario="";
   		
              
         	   	
         	if (tabEmpleado.getTotalFilas()>0) {
         		for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) 
   			 {
          	  
                str_ide.append(tabEmpleado.getValor(i, "IDE_GTEMP"));
                valor++;
                if (tabEmpleado.getTotalFilas()==1) {
   			}else if (valor<=tabEmpleado.getTotalFilas()) {
                    str_ide.append(",");
                    valor=valor+1;
    			}
    
   			 }
         		
         	}
    	
    		bar_botones.agregarComponente(new Etiqueta("OPCIONES HORAS EXTRA :"));

    	//bar_botones.agregarComponente(new Etiqueta("Reportes de Biometrico"));
		//bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	    	
    

    	//Boton Reporte de Empleado
    	Boton bot_sin_entrada_salida= new Boton();
    	bot_sin_entrada_salida.setIcon("ui-icon-calculator");
    	bot_sin_entrada_salida.setMetodo("getSinEntradaSalida");
    	bot_sin_entrada_salida.setValue("SIN ENT/SALD");
    	bot_sin_entrada_salida.setTitle("SIN ENT/SALD");
    	bar_botones.agregarBoton(bot_sin_entrada_salida);
    	
    	    	
    	Boton bot_sin_entrada= new Boton();
    	bot_sin_entrada.setIcon("ui-icon-calculator");
    	bot_sin_entrada.setMetodo("getSinEntrada");
    	bot_sin_entrada.setValue("SIN ENTRADA");
    	bot_sin_entrada.setTitle("SIN ENTRADA");
    	bar_botones.agregarBoton(bot_sin_entrada);
    	
    	
		Boton bot_sin_salida = new Boton();
		bot_sin_salida.setIcon("ui-icon-calculator");
		bot_sin_salida.setMetodo("getSinSalida");
		bot_sin_salida.setValue("SIN SALIDA");
		bot_sin_salida.setTitle("SIN SALIDA");
		bar_botones.agregarBoton(bot_sin_salida);
    	
    	
    	
    	
    	Boton bot_empleado= new Boton();
    	bot_empleado.setIcon("ui-icon-calculator");
    	bot_empleado.setMetodo("seleccionarEmpleado");
    	bot_empleado.setValue("Importar X Empleado");
    	bot_empleado.setTitle("Importar X Empleado");
//    	bar_botones.agregarBoton(bot_empleado);

    	
    	
    	Boton bot_actualizar_empleado= new Boton();
    	bot_actualizar_empleado.setIcon("ui-icon-calculator");
    	bot_actualizar_empleado.setMetodo("getHorario");
    	bot_actualizar_empleado.setValue("ACTUALIZAR HORARIO");
    	bot_actualizar_empleado.setTitle("ACTUALIZAR HORARIO");
    	bar_botones.agregarBoton(bot_actualizar_empleado);
    	
    	/*Boton bot_pegar_horario= new Boton();
    	bot_pegar_horario.setIcon("ui-icon-calculator");
    	bot_pegar_horario.setMetodo("pegarHorario");
    	bot_pegar_horario.setValue("PEGAR HORARIO");
    	bot_pegar_horario.setTitle("PEGAR HORARIO");
    	bar_botones.agregarBoton(bot_pegar_horario);*/
    	
    	
    	Boton bot_ConsultarEmpleado= new Boton();
    	bot_ConsultarEmpleado.setIcon("ui-icon-calculator");
    	bot_ConsultarEmpleado.setMetodo("consultarEmpleado");
    	bot_ConsultarEmpleado.setValue("CONSULTAR");
    	bot_ConsultarEmpleado.setTitle("CONSULTAR");
    	bar_botones.agregarBoton(bot_ConsultarEmpleado);
    	
    	

    	
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS "
				+ "FROM GTH_EMPLEADO EMP " +
				" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("getEmpleado");
		agregarComponente(set_empleado);

		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

		
		aut_empleados.setId("aut_empleados");
		if (bandSinArea) {
		
		}else {
			
		
		if (tabEmpleado.getTotalFilas()>0){
			
			
				if(!(tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9"))) {      		
			
			aut_empleados.setAutoCompletar("SELECT emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
					"FROM  GTH_EMPLEADO EMP " +
					"WHERE EMP.ACTIVO_GTEMP=true and ide_gtemp in("+str_ide.toString()+") ");
			}else {

	      	if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
				aut_empleados.setAutoCompletar("SELECT emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
						"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
						"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
						"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
						"FROM  GTH_EMPLEADO EMP " +
						"WHERE EMP.ACTIVO_GTEMP=true");			}
			}
		}
		}
		//aut_empleados.setMetodoChange("filtrarTimbradasPorEmpleado");
		//bar_botones.agregarComponente(new Etiqueta("Empleado:"));
		//bar_botones.agregarComponente(aut_empleados);
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
    	
		bar_botones.agregarBoton(bot_limpiar);    	

		//aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

    	
    	set_departamento.setId("set_departamento");


		sel_editar_horario.setId("sel_editar_horario");
		agregarComponente(sel_editar_horario);
		
		
    	
        
        

        tab_resumen_marcaciones.setId("tab_resumen_marcaciones");
        tab_resumen_marcaciones.setTabla("con_biometrico_marcaciones_resumen", "IDE_COBMR", 1);
        tab_resumen_marcaciones.setNumeroTabla(1);
        tab_resumen_marcaciones.setCampoPrimaria("IDE_COBMR");		


		tab_resumen_marcaciones.getColumna("IDE_COBMR").setLongitud(5);
		tab_resumen_marcaciones.getColumna("IDE_COBMR").setOrden(1);
		
        tab_resumen_marcaciones.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GTH_EMPLEADO EMP");
        tab_resumen_marcaciones.getColumna("ide_gtemp").setAutoCompletar();
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setLongitud(27);
        tab_resumen_marcaciones.getColumna("IDE_GTEMP").alinearCentro();
        tab_resumen_marcaciones.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO");
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setOrden(2);
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setFiltro(true);
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setLectura(true);


        tab_resumen_marcaciones.getColumna("DIA_COBMR").setLongitud(15);
        tab_resumen_marcaciones.getColumna("DIA_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("DIA_COBMR").setOrden(4);
		tab_resumen_marcaciones.getColumna("DIA_COBMR").setLectura(true);
		
		
		tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setLongitud(20);
		tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setOrden(3);
        tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
    	tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setLectura(true);
        
        //  tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setMetodoChange("actualizarMarcacionesXFecha");
        
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setOrden(5);
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setNombreVisual("ENTRADA");
     //   tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setMetodoChange("actualizarHorarioProgramadoEntrada");
    	
        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setOrden(6);
        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").setNombreVisual("TIMBRE");
       // tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").setMetodoChange("actualizarHorarioTimbradoEntrada");
        
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setOrden(7);
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setNombreVisual("ESTADO ENTRADA");
     //   tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setMetodoChange("actualizarEstadoEntrada");


        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setOrden(8);
        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setNombreVisual("H.INI.ALM");
		tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setVisible(false);

             

        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setOrden(9);
        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setNombreVisual("H.FIN.ALM");
		tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setVisible(false);

        
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setOrden(10);
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setNombreVisual("TIM.INI.ALM");  
		tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setVisible(false);

        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setOrden(11);
        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setNombreVisual("TIM.FIN.ALM");  
		tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setVisible(false);
        
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setLongitud(10);
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setOrden(12);
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setNombreVisual("TIM.TOMA.ALM"); 
		tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setVisible(false);

        
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setLongitud(10);
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setOrden(13);
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setNombreVisual("TIEMPOHORALM"); 
		tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setVisible(false);

        
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setOrden(14);
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setNombreVisual("HOR.SAL");  
     //   tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setMetodoChange("actualizarHorarioProgramadoSalida");

        
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setOrden(15);
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setNombreVisual("TIM.HOR.SAL");  
       // tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setMetodoChange("actualizarHorarioTimbradoSalida");

        
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setOrden(16);
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setNombreVisual("ESTADO SALIDA");  
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setMetodoChange("actualizarEstadoSalida");

    
        
        
        tab_resumen_marcaciones.getColumna("horafinextra_cobmr").setLongitud(35);
        tab_resumen_marcaciones.getColumna("horafinextra_cobmr").alinearCentro();
    	tab_resumen_marcaciones.getColumna("horafinextra_cobmr").setOrden(17);
        tab_resumen_marcaciones.getColumna("horafinextra_cobmr").setNombreVisual("HORA EXTRA");  

        
        
        
        tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").setLongitud(35);
        tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").alinearCentro();
    	tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").setOrden(18);
        tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").setNombreVisual("HORA RECARGO 25%");  

        
        
        
        
        
        tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").setLongitud(35);
        tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").alinearCentro();
    	tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").setOrden(19);
        tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").setNombreVisual("HORA RECARGO 100%");  

    
       // tab_resumen_marcaciones.getColumna("horafinextra_cobmr").setVisible(false);  
       // tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").setVisible(false);  
       // tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("novedad_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("aprueba_hora_extra_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("p_entrada_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("p_salida_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("p_alm_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("inconsistencia_biometrico_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("ide_aspvh").setVisible(false);  
        //tab_resumen_marcaciones.getColumna("pegado_horario_cobmr").setVisible(false);  

        
        
     
      		
      		
         try {
			if (tabEmpleado.getTotalFilas()>0){
			if(!(tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) ) {      		
      	tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaConsultaInicio+"' AND '"+fechaConsultaFin+"' and ide_gtemp in("+str_ide.toString()+") ");	
		}else {

			if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
      		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaConsultaInicio+"' AND '"+fechaConsultaFin+"' ");	
		}
		}
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
System.out.println("Error en obtener el resumen de vacaciones()");		}
         
   		//tab_resumen_marcaciones.setCondicion("IDE_COBMR=-1");	

         
        tab_resumen_marcaciones.setHeader("RESUMEN DE MARCACIONES EN BIOMÉTRICO POR EMPLEADO");
        tab_resumen_marcaciones.setLectura(true);
        tab_resumen_marcaciones.setRows(20);
        tab_resumen_marcaciones.onSelect("actualizarMarcacionesXFecha");
        tab_resumen_marcaciones.dibujar();
    	PanelTabla pat_panel2 = new PanelTabla();
    	pat_panel2.setPanelTabla(tab_resumen_marcaciones);
        //TABLA DONDE SE GUARDA EL RESUMEN  DE LOS DATOS       


    tab_marcaciones.setId("tab_marcaciones");
    tab_marcaciones.setCampoPrimaria("IDE_COBIM");		
    tab_marcaciones.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM,(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as NOMBRES,DETALLE_COREL,TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
    tab_marcaciones.getColumna("EVENTO_RELOJ_COBIM").setFiltro(true);
    tab_marcaciones.getColumna("IDE_PERSONA_COBIM").setFiltro(true);
    tab_marcaciones.getColumna("NOMBRES").setFiltro(true);
    tab_marcaciones.getColumna("DETALLE_COREL").setFiltro(true);
    tab_marcaciones.setNumeroTabla(2);				
    tab_marcaciones.setLectura(true);
    tab_marcaciones.setRows(10);
    tab_marcaciones.dibujar();		
	PanelTabla pat_panel_marcaciones = new PanelTabla();
	pat_panel_marcaciones.setPanelTabla(tab_marcaciones);


   Division div_division=new Division();
   div_division.setId("div_division");
   div_division.dividir2(pat_panel2, pat_panel_marcaciones, "75%", "H");
  // div_division.dividir1(pat_panel2);
   
   agregarComponente(div_division);


    }

   
   public void getImportacionesPorNovedad (){
	   tab_novedad.getValorSeleccionado();
	   String fechaIni=tab_novedad.getValor("FECHA_INICIO_ASNOV");
	   String fechaFin=tab_novedad.getValor("FECHA_FIN_ASNOV");
    	
		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ");
		tab_resumen_marcaciones.ejecutarSql();
		utilitario.addUpdate("tab_novedad,tab_resumen_marcaciones");
	    
    }
   
  
   

   public void importarMarcaciones(){
       //	System.out.println(tab_novedad.getValorSeleccionado());
       	
       	//tab_novedad.setLectura(false);
       	/**
       	 * Validacion de fecha
       	 */
       	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
       		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
           		String fechaIni=(cal_fecha_inicial.getFecha());	
           		String fechaFin=(cal_fecha_final.getFecha());	
         
      			
       	
       	TablaGenerica tab_novedadImportar = utilitario.consultar("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad where "
       			+ "importacion_asnov=true");
       	
       	String  fechaComparacionInicio="";
       	String  fechaComparacionFin="";
       	boolean banderaInicio=false,banderaFin=false;
       	for (int i = 0; i < tab_novedadImportar.getTotalFilas(); i++) {
       		fechaComparacionInicio=tab_novedadImportar.getValor(i,"FECHA_INICIO_ASNOV");
       		if (fechaComparacionInicio.equals(fechaIni)) {
   				banderaInicio=true;
   			}
       		if (fechaComparacionInicio.equals(fechaFin)) {
   				banderaInicio=true;
   			}
        		fechaComparacionFin=tab_novedadImportar.getValor(i,"FECHA_FIN_ASNOV");
       		if (fechaComparacionFin.equals(fechaFin)) {
   				banderaFin=true;
   			}
       
       		if (fechaComparacionFin.equals(fechaIni)) {
   				banderaFin=true;
   			}
       	}
       	
       	if (banderaInicio==true || banderaFin==true) {
       		utilitario.agregarMensajeInfo("Marcaciones importadas para este rango de fechas","Datos importados Desde "+fechaIni +" hasta "+fechaFin);
   			return;
   		}
       	
       	
       	
       	//Ver si tabla ya contiene las importaciones para ese rango de fecha
       	TablaGenerica tab_novedadImportarMarcacionBruto = utilitario.consultar("select * from con_biometrico_marcaciones where fecha_evento_cobim between  "
       			+ "'"+fechaIni+"' and '"+fechaFin+"'");
       	
       	if (tab_novedadImportarMarcacionBruto.getTotalFilas()<=0) {
   			utilitario.agregarMensajeInfo("No existen registro de marcaciones ","Datos no encontrados desde "+fechaIni +" hasta "+fechaFin);
   			return;
   	
   		}
       	
       	//Ver si tabla ya contiene las importaciones para ese rango de fecha
       	TablaGenerica tab_novedadImportarMarcacionExistentes = utilitario.consultar("select * from con_biometrico_marcaciones_resumen where fecha_evento_cobmr between  "
       			+ "'"+fechaIni+"' and '"+fechaFin+"'");
       	
       	//Si esxisten 
       	if (tab_novedadImportarMarcacionExistentes.getTotalFilas()>0) {
   			utilitario.agregarMensajeInfo("Marcaciones importadas para este rango de fechas","Datos importados Desde "+fechaIni +" hasta "+fechaFin);
   			return;
   			//tab_novedad.ejecutarSql();
   			//utilitario.addUpdate("tab_novedad");

   		}
       	//si no existen
       	else {
   			
       	//inserto	
       		int ide_usua=Integer.parseInt(utilitario.getVariable("IDE_USUA").toString());
       		String fecha_inicio_asnov= fechaIni;
       		String fecha_fin_asnov= fechaFin;
           	String 	observacion_asnov="Marcaciones importadas desde "+fecha_inicio_asnov+" hasta "+fecha_fin_asnov+"";
           	String fecha_asnov=utilitario.getFechaActual();
   //inserto datos
           	/**
           	 * Obtengo el me y anio que se realiza la consulta
           	 */
           	
           	int mes=utilitario.getMes(fecha_inicio_asnov);
           	int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
           	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
   		    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
   		    
   		    
   		    
   		    
       	  //inserto marcaciones para rango de fecha seleccionado
   		    
   			//getMarcacionesEmpleadoMatriz("",fechaIni,fechaFin);
   //System.out.println("fechaIni"+fechaIni);
   //System.out.println("fechaFin"+fechaFin);
   //System.out.println("fechaFin"+mes);
   //System.out.println("fechaFin"+anio);



   //




   			//getMarcacionesEmpleado(fechaIni,fechaFin,mes,anio,"");
   			tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ");
   			tab_resumen_marcaciones.ejecutarSql();
   			
   		   	insertarTablaNovedad(ide_usua, fecha_inicio_asnov, fecha_fin_asnov, observacion_asnov, true, fecha_asnov,true,false);
   			tab_novedad.ejecutarSql();

   			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "recargonocturno25_cobmr=null, recargonocturno100_cobmr=null, horafinextra_cobmr=null, novedad_cobmr=false "
						+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
				
				
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%OK%' "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =false");

						
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%ATRASADO%' and horafinband_cobmr like '%OK%' "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =false");


				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%ANTICIPADO%' "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =false");



				
				
				///EXTRA Y FERIADO
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%EXTRA%' and horafinband_cobmr like '%EXTRA%' "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =false");

						
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%FERIADO%' and horafinband_cobmr like '%FERIADO%' "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =false");
   			/**
   			 * VALIDACION ALMUERZO
   			 */
   			
   		/*	try {
   				double tiempoAlmuerzo=0.0,tiempoHoraAlmuerzo=0.0;
   				for (int i = 0; i < tab_resumen_marcaciones.getTotalFilas(); i++) {
   					
   if (tab_resumen_marcaciones.getValor(i, "tiempoalm_cobmr").isEmpty() || tab_resumen_marcaciones.getValor(i, "tiempoalm_cobmr")==null || tab_resumen_marcaciones.getValor(i, "tiempoalm_cobmr").equals("")) {
   	tiempoAlmuerzo=0.0;
   }else {
   	tiempoAlmuerzo=Double.parseDouble(tab_resumen_marcaciones.getValor(i, "tiempoalm_cobmr"));
   	
   }

   if (tab_resumen_marcaciones.getValor(i, "tiempohoralm_cobmr").isEmpty() || tab_resumen_marcaciones.getValor(i, "tiempohoralm_cobmr")==null || tab_resumen_marcaciones.getValor(i, "tiempohoralm_cobmr").equals("")) {
   	tiempoAlmuerzo=0.0;
   }else {
   	tiempoHoraAlmuerzo=Double.parseDouble(tab_resumen_marcaciones.getValor(i, "tiempohoralm_cobmr"));
   	
   }

   				
   				

   					if (tiempoAlmuerzo>tiempoHoraAlmuerzo) {
   						tab_resumen_marcaciones.getColumna("tiempoalm_cobmr").setEstiloColumna(" background-color: red;");
   					}
   				}
   				
   				utilitario.addUpdate("tab_novedad,tab_resumen_marcaciones");
   			} catch (NumberFormatException e) {
   				// TODO Auto-generated catch block
   	//		System.out.println("error en calculo de almuerzo");
   			}*/
   		
       	   }

       		}else {
   			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

   		}
   		
   	}
   	else {
   		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
   	}		

       		
       }
       
    
    
    public void eliminarMarcaciones(){
    	//System.out.println(tab_novedad.getValorSeleccionado());
    	tab_novedad.setLectura(false);
    	TablaGenerica tab_novedadImportar = utilitario.consultar("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad where "
    			+ "importacion_asnov=true and ide_asnov="+tab_novedad.getValorSeleccionado());
    	
    	String  fechaComparacionInicio="";
    	String  fechaComparacionFin="";
    	
    	boolean banderaInicio=false,banderaFin=false;
        	
if (tab_novedadImportar.getTotalFilas()<=0) {
	utilitario.agregarMensajeInfo("No se ha seleccionado datos",	"Fecha inicial debe ser menor a fecha final");
return;
	
}else {
	String fechaIni="";
	String fechaFin="";

  fechaIni=tab_novedadImportar.getValor("FECHA_INICIO_ASNOV");
  fechaFin=tab_novedadImportar.getValor("FECHA_FIN_ASNOV");
  
  TablaGenerica tab_novedadImportarMarcacionExistentes = utilitario.consultar("select * from con_biometrico_marcaciones_resumen where fecha_evento_cobmr between  "
			+ "'"+fechaIni+"' and '"+fechaFin+"'");
	
	//Si esxisten 
	if (tab_novedadImportarMarcacionExistentes.getTotalFilas()<=0) {
		utilitario.agregarMensajeInfo("No existen marcaciones para este rango de fechas","Datos importados Desde "+fechaIni +" hasta "+fechaFin);
		return;
		//tab_novedad.ejecutarSql();
		//utilitario.addUpdate("tab_novedad");

	}
	//si no existen
	else {
 	utilitario.getConexion().ejecutarSql("DELETE FROM  asi_novedad  WHERE ide_asnov="+tab_novedad.getValorSeleccionado());
	tab_novedad.ejecutarSql();
	tab_novedad.actualizar();

 	utilitario.getConexion().ejecutarSql("DELETE FROM  con_biometrico_marcaciones_resumen  WHERE FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ");
	tab_resumen_marcaciones.ejecutarSql();
	tab_resumen_marcaciones.actualizar();
	
	
	
	/*
	int ide_usua=Integer.parseInt(utilitario.getVariable("IDE_USUA").toString());
	String fecha_inicio_asnov= fechaIni;
	String fecha_fin_asnov= fechaFin;
	String 	observacion_asnov="Marcaciones eliminadas desde "+fecha_inicio_asnov+" hasta "+fecha_fin_asnov+"";
	String fecha_asnov=utilitario.getFechaActual();
//inserto datos
	/**
	 * Obtengo el me y anio que se realiza la consulta
	 */
	/*                                                                                                                                                              
	int mes=utilitario.getMes(fecha_inicio_asnov);
	int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));


	insertarTablaNovedad(ide_usua, fecha_inicio_asnov, fecha_fin_asnov, observacion_asnov, true, fecha_asnov,false,false);
	tab_novedad.ejecutarSql();
*/

}
    	
    		
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
    
	public void getEmpleado(String empleado,String fecha){
		
	  	
		//StringBuilder str_ide=new StringBuilder();
        //int int_num_col_idegtemp=set_empleado.getTab_seleccion().getNumeroColumna("IDE_GTEMP");
        //for (Fila filaActual:set_empleado.getTab_seleccion().getSeleccionados()) {
         //       if(str_ide.toString().isEmpty()==false){
          //              str_ide.append(",");
           //     }
           //     str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
        //}

       // getMarcacionesEmpleado(str_ide.toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
		utilitario.getConexion().ejecutarSql("delete from con_biometrico_marcaciones_resumen "
				+ "where ide_gtemp in ("+empleado+") "
				+ " and fecha_evento_cobmr = '"+fecha+"'");


        getMarcacionesEmpleado(fecha,fecha,0,0,empleado);

        sel_editar_horario.cerrar();		
        utilitario.addUpdate("sel_editar_horario");
    			
    		
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
         
          
         // getMarcacionesEmpleado(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),0,0,"");
          set_departamento.cerrar();		
 			
	}
    
    
    
    
    @SuppressWarnings("unused")
	public void getMarcacionesEmpleadoOLD(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani,String  empleadoSeleccionado){
int contador = 0;

		int entrada=0,almuerzo=0,salida=0;
	    int entradaNocturno=0,salidaNocturno=0;

		 String fechaBiometrico="";

    	
    	//Hacer la consulta para poder retornar los horarios de cada empleado
    	
        StringBuilder str_ide_horarios=new StringBuilder(); 
        //obtengo todos los turnos con sus horarios
       
        //
        boolean banderaMatriz= false,banderaExtra=false;
       	       	
    	String strFechaIniReporte =fechaInicial;
    	String strFechaFinReporte = fechaFinal;
    	
    	
    	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
    	Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
    	Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
    	

    	//Asigno el tamaño de la  tabla en la cual voy ir guardando los datos de timbre de cada empleado
    	
//		Tabla que contiene las fechas de timbre del empleado	
    	TablaGenerica tab_reporte=getTablaReporte(empleadoSeleccionado,strFechaIniReporte, strFechaFinReporte);

    	if (tab_reporte.isEmpty()) {
			utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
			return;
		}
    	
    	
    	
    
    	
    	//Lista con dias y meses seleccionados
    	int diferencia_dias=utilitario.getDiferenciasDeFechas(dateFechaInicioReporte,dateFechaFinReporte);
    	Date DiaSuma=null;
    	//System.out.println("Diferencia dias"+diferencia_dias);
    	ArrayList<Integer> listaDiaSeleccionado = new ArrayList<Integer>();
    	ArrayList<Integer> listaMesSeleccionado = new ArrayList<Integer>();
    	ArrayList<Integer> listaAnioSeleccionado = new ArrayList<Integer>();


    	for (int i = 0; i < diferencia_dias+1; i++) {
    		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
    		utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
    		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
    		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
    		listaAnioSeleccionado.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));

		//System.out.println("Dias"+listaDiaSeleccionado.get(i).toString());
		//System.out.println("Mes"+listaMesSeleccionado.get(i).toString());
		} 
    	
    	
    	
    	//Obtengo los ides de los empleados que han timbrado 
      	TablaGenerica tab_reporteEmpleado=getTablaReporteEmpleado(strFechaIniReporte, strFechaFinReporte,empleadoSeleccionado);
      	
    	ArrayList<Integer> listaDiasEmpleado = new ArrayList<Integer>();
    	ArrayList<Integer> listaMesEmpleado = new ArrayList<Integer>();
    	ArrayList<Integer> listaAnioEmpleado = new ArrayList<Integer>();
    	for (int itemReporteEmpleado=0; itemReporteEmpleado< tab_reporteEmpleado.getTotalFilas(); itemReporteEmpleado++) { 		
       		//ide de los empleados
    		Integer empleado = Integer.parseInt(tab_reporteEmpleado.getValor(itemReporteEmpleado, "IDE_GTEMP"));
    		//Consulto las timbradas realizadas del empleado por fecha
    		TablaGenerica tab_reporteTimbradasXDia=getTablaReporte(""+empleado,strFechaIniReporte, strFechaFinReporte);
        	 //recorro las timbradas 
        	for (int itemReporteTimbradasXDia=0; itemReporteTimbradasXDia< tab_reporteTimbradasXDia.getTotalFilas(); itemReporteTimbradasXDia++) {
        		//Obtengo el dia Timbrado
        		listaDiasEmpleado.add(utilitario.getDia(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));
        		//Obtengo el mes 
        		listaMesEmpleado.add(utilitario.getMes(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));  
        		listaAnioEmpleado.add(utilitario.getAnio(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));    		

			}
        	/////////////////////////////////////////////////AQUI DEBO VALIDAR CUANDO ES FERIADO
        	
        	for (int i = 0; i < listaDiasEmpleado.size(); i++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
					if (listaDiasEmpleado.get(i)==listaDiaSeleccionado.get(j) && listaMesEmpleado.get(i)==listaMesSeleccionado.get(j) && listaAnioEmpleado.get(i)==listaAnioSeleccionado.get(j)) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						listaDiaSeleccionado.remove(j);
						//elimino de la lista el dia que si tenga timbrada y asignado
						listaMesSeleccionado.remove(j);
						//elimino el mes que corresponde a esa fecha con su timbrada}
						listaAnioSeleccionado.remove(j);

						j=listaDiaSeleccionado.size();
						//variable para pasar a una nueva timbrada si esta cumple con comparacion 
					}
				}
			}
        	
        	
       
        	//Lista de dias seleccionados pulida sin marcacion pero asignados
        	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
        		//for que recorre la lista final de dias sin asignar
        		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
        				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+listaAnioSeleccionado.get(i).toString());
        		
        		
        		//Obtengo la matriz de la persona
      if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
    	  //Tiene Matriz
    //	utilitario.agregarMensaje("sI CONTIENE MATRIZ", "");
    	String diaMatrizMensual="";

    	//////FERIADO
    	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
    	//pulo por dias la matriz
    	if (diaMatrizMensual==null || diaMatrizMensual.isEmpty() || diaMatrizMensual.equals("")) {
    	diaMatrizMensual="";	
    	//Para que no me ingrese varaias veces el dia que no se tiene asignado
    	//	System.out.println("SIN ASIGNACION");
    		listaDiaSeleccionado.remove(i);
    		listaMesSeleccionado.remove(i);
    		listaAnioSeleccionado.remove(i);

    		i=-1;
    	}else {
    	    	    
    	    	}
      	
    }else {
    	i=listaDiaSeleccionado.size();
    }
      
      
      
       
    		}
        	
         	
        	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
        		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
        				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+listaMesSeleccionado.get(i).toString());

    	String diaMatrizMensual="";
    	
    	if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
			
		

    	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
    	
    	 	//	System.out.println("ASIGNADO"+diaMatrizMensual);
    		/////Inserto la fila 
    		
    		
    		 TablaGenerica tabObternerTurnos=this.obtenerTurnos(pckUtilidades.CConversion.CInt(diaMatrizMensual));
    		//guardo el ide_del horario
    	     int ide_ashor= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
       		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(ide_ashor);
    	
       			String horaInicioEmpl = horariosEmpleadoMes.getValor( "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
       			String horaFinEmpl = horariosEmpleadoMes.getValor("HORA_FINAL_ASHOR");// la hora final del horario erp
       			String horaIniAlmEmpl = horariosEmpleadoMes.getValor( "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
       			String hora1="",hora2="";
       			if (horaIniAlmEmpl==null || horaIniAlmEmpl.equals("") || horaIniAlmEmpl.isEmpty()) {
       				horaIniAlmEmpl="";
				}
				
				
				
       			String horaFinAlmEmpl = horariosEmpleadoMes.getValor( "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
       			if (horaFinAlmEmpl==null || horaFinAlmEmpl.equals("") || horaFinAlmEmpl.isEmpty()) {
       				horaFinAlmEmpl="";
       			}
    
      			String tiempoAlmEmpl = horariosEmpleadoMes.getValor( "MIN_ALMUERZO_ASHOR");//hora inicio erp
       			if (tiempoAlmEmpl==null || tiempoAlmEmpl.equals("") || tiempoAlmEmpl.isEmpty()) {
       				tiempoAlmEmpl="";
       			}
       			if (tiempoAlmEmpl.equals("0")) {
       				horaFinAlmEmpl="S/A";
       				horaIniAlmEmpl="S/A";
       				hora1="S/A";
       				hora2="S/A";
				}

       			//Validacion del dia que contenga 
       			
       			
       			TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+listaAnioSeleccionado.get(i).toString());
       			String anioActual=tabAnio.getValor("detalle_geani");
       			String fecha_evento_cobmr="";
       			if (listaDiaSeleccionado.get(i).intValue()<10) {
       				fecha_evento_cobmr= anioActual+"-"+listaMesSeleccionado.get(i)+"-0"+listaDiaSeleccionado.get(i).toString();
				}else {
       				fecha_evento_cobmr= anioActual+"-"+listaMesSeleccionado.get(i)+"-"+listaDiaSeleccionado.get(i).toString();

				}
      			
      			String dia_cobmr=diaSemana(fecha_evento_cobmr);
      			 banderaFeriados=getFeriado(fecha_evento_cobmr);
      			 //Validacion fecha feriado
      			 if (banderaFeriados==true) {
					//Si se tiene feriado ese dia no se ingrese
				}else if(getEmpleadoActivo(empleado)==false){
					
				}
      			 
      			 
      			 else {
					 //Si no es un dia fericado que se ingrese como no timbrada
					 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		
			         }
      			
        	}else {
        		i=listaDiaSeleccionado.size();	
			}
        	
    	
        	}
    	
        	listaDiaSeleccionado = new ArrayList<Integer>();
        	listaMesSeleccionado = new ArrayList<Integer>();
        	listaDiasEmpleado = new ArrayList<Integer>();
        	listaMesEmpleado =  new ArrayList<Integer>();
//Reinicio la matriz de dia selecionados
        	for (int i = 0; i < diferencia_dias+1; i++) {
        		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
        		utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
        		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
        		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
        		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
    	//	System.out.println("Dias"+listaDiaSeleccionado.get(i).toString());
    	//	System.out.println("Mes"+listaMesSeleccionado.get(i).toString());
    		} 
        	      	     	
    	}
    	
    /**
     * Metodo de asignacion de dias	
     */
    	
    	
    	
    	///rESULTADO CORRECTO
    /*	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
			System.out.println("lista Resultado"+listaDiaSeleccionado.get(i).toString()+"  mes: "+listaMesSeleccionado.get(i).toString());
			
		}*/
    	
    /////////////////////////////////////////////////////////////////////////////////////////////
    	String horaInicioEmpl ="";
			String horaFinEmpl = "";
			String horaIniAlmEmpl ="";
			String horaFinAlmEmpl = "";
    	
    	for (int itemReporte=0; itemReporte< tab_reporte.getTotalFilas(); itemReporte++) {
       		Integer Entrada=0;
       		Integer Salida=0;
       		Integer AlmuerzoEntrada=0;
       		Integer AlmuerzoSalida=0;
       	 //Obtengo el ide del empleado con el cual obtendrenmos los horarios
               boolean banderaSinAccion=false;
               Integer extraMatriz=0;
          		Integer empleado = Integer.parseInt(tab_reporte.getValor(itemReporte, "IDE_GTEMP"));
       		//Integer empleado = 46;
       		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(itemReporte,"TO_CHAR");
       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
   			//Tabla guarda los busca y devuelve el turno de tipo matriz si se le ha asignado 
       		TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR=1 and ACTIVO_GTEMP=TRUE");
       		//variable donde se guarda el ide del turnomatriz
       		int turnoMatriz=0;
       		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
       			turnoMatriz=0;
   			}else {
   				turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
   			} 
       		
       		
       		//Obtengo si tiene matriz
       		int mes=0;
       		mes=utilitario.getMes(fechaBiometricoAgrupadaXEmpleado);
       		int anioTemp=0;
       		anioTemp=utilitario.getAnio(fechaBiometricoAgrupadaXEmpleado);
       		TablaGenerica tab_Anio=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+anioTemp+"%'");
       		int anio=Integer.parseInt(tab_Anio.getValor("IDE_GEANI"));
       		int dia=0;
       		dia=utilitario.getDia(fechaBiometricoAgrupadaXEmpleado);
       		
       		//Variables para asignacion Entrada,Salida,Almuerzo Entrada, Almuerzo Salida
       		
       		
       			
       		Integer tipo1= 0;
       		Integer tipoNocturno= 0;

       		
       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
       		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
       				+ "and ide_gemes="+mes+" and ide_geani="+anio);
       		

            
                //Obtengo el turno por  dia,mes,anio,empleado 
       		int contieneDiaTurno=0;
       		 contieneDiaTurno=getHorarioXDia(empleado,dia,mes,anio);
       		
       		//Variable que guarda 0 si no hubo aignacion de turno el dia anterior y diferente de cero los que si tiene turno de tipo nocturno el  dia anterior 
                int contieneTunnoNocturnoAnterior=0;
                int dia_anterior=utilitario.getDia(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado), -1)));
                //devuelve el turno asignado ese dia 
                contieneTunnoNocturnoAnterior=getHorarioXDia(empleado,dia_anterior,mes,anio);
       
   		     //////////////////////////////////////FESTIVOS Y DESIGNACION////////////////////////////////////////////////////////

                //Verifico para ese dia si tiene asignada el dia festivo 
       		     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
   			     
       		     //boolean banderaTurnoDesignado= getTurnoDesignadoFeriado(fechaBiometricoAgrupadaXEmpleado);
   			    //Variable devuelve si contiene asignado el dia en la asi_novedad como feriado
   		/*	     if (banderaFeriados) {
   			    	 //Si tengo esa variable le asigno ese horario asigno a ese dia ese horario
   			    	
   			    	 contieneDiaTurno= getTurnoDesignadoFeriado(fechaBiometricoAgrupadaXEmpleado);
   				}
   			    

   			     //////////////////////////////////////FESTIVOS Y DESIGNACION////////////////////////////////////////////////////////
   			     
   			   //Verifico para ese dia si tiene asignada el dia festivo o cambio de turno por designacion
       		     banderaCambioHorario=getCambioHorario(fechaBiometricoAgrupadaXEmpleado);
   			    //Variable devuelve si contiene asignado el dia en la asi_novedad
   			     if (banderaCambioHorario) {
   			    	 //Si tengo esa variable le asigno ese horario
   			    	//obtengo el turno 0 si no tiene y diferente de 0 si contiene asignacion
   				      //turno= getTurnoDesignadoConsulta(fechaBiometricoAgrupadaXEmpleado);
   			    	 contieneDiaTurno= getTurnoDesignadoCambioHorario(fechaBiometricoAgrupadaXEmpleado);
   				}
   			     
   			     
   			     //////////////////////////////////////PERMISO ONLINE/////////////////////////////////////////////////////////////
   			    
   				//Verifico para ese dia si tiene asignada el dia festivo o cambio de turno por designacion
       		     banderaCambioHorarioPermisoOnline=getCambioHorarioPermisoOnline(fechaBiometricoAgrupadaXEmpleado);
   			    //Variable devuelve si contiene asignado el dia en la asi_novedad
   			     if (banderaCambioHorarioPermisoOnline) {
   			    	 //Si tengo esa variable le asigno ese horario
   			    	//obtengo el turno 0 si no tiene y diferente de 0 si contiene asignacion
   				      //turno= getTurnoDesignadoConsulta(fechaBiometricoAgrupadaXEmpleado);
   			    	 contieneDiaTurno= getTurnoDesignadoPermisoOnline(fechaBiometricoAgrupadaXEmpleado);
   				}
   			     
   			     
   			*/     
   			     
                
       		     
       		     
       		 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       		     
       		     
       		     
       		//Si tiene matriz
  				if (tabEmpleadoMatrizMensual.getTotalFilas()!=0   && tabEmpleado.getTotalFilas()==0 ) {
  					
  					
  					//permiso online
  				
  					TablaGenerica tabTipoTurno=null;			
if (contieneDiaTurno==0) {
	ide_gtgre=0;
}else {
		//Si tiene asignado matriz para ese dia
      tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
  		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
  	//Libre para calculo de horas extra
  		if (ide_gtgre==4) {
			contieneDiaTurno=0;
		}
}
  	    	        
  	    	        
  	    	        
  	    	        
  	    	        
  	    	      TablaGenerica tabTipoTurnoNocturno=null;
  	    	    	if (contieneTunnoNocturnoAnterior==0) {
  	    	        	estado_empleado_nocturno=false;

					}else {
			
  					//Si tiene asignado matriz para ese dia
  	    	         tabTipoTurnoNocturno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneTunnoNocturnoAnterior+" and ide_gtgre=3");
					if (tabTipoTurnoNocturno.getTotalFilas()==0) {
	  	    	         estado_empleado_nocturno=false;

					}else {
	  	    	         estado_empleado_nocturno=true;
						
					}	
  	    	         

					}
  	    	        
  	    	        
  	    	        
  	    	   
  	    	        
  	    	       
  		          //Obtengo el horario
  	       		 TablaGenerica tabObternerTurnos=this.obtenerTurnos(contieneDiaTurno);
  //guardo el ide_del horario
  	    	        int ide_ashor= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
  	    	        
  	    	     
  	    	        
  	    	        
  	        		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(ide_ashor);

  	        		//para turno nocturno
  	        	//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
  	        	//	String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
  	        	//	String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
  	        		//TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
  	        	//	String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
  	        		 
  	        		
  	        		
  	        		
  	        		
  	        		int banderaHoraExtra1001=0;
  	    			    banderaFeriados=false;

  	        		 
  	    		    Integer tipo2 = 0;
  	    		    Integer tipo3 = 0;
  	    		    Integer tipo4= 0;
  	    		    Integer tipo5 = 0;
  	    		    Integer tipo6 = 0;
  	    		    Integer tipo7 = 0;
  	    			Integer tipo1SinAlmuerzo= 0;
  	    		    Integer tipo2SinAlmuerzo = 0;
  	    		    Integer tipo3SinAlmuerzo = 0;
  	    		    Integer tipo4SinAlmuerzo= 0;
  	    		    String  SINDATO="";
  	    		    int cont=0;
  	    	    
  	    		    int forHorarios=0;
  	    		    forHorarios = horariosEmpleadoMes.getTotalFilas();
  	    		    if (contieneDiaTurno==0 && forHorarios==0) {
  	    		    	forHorarios=1;
  	    			}
  	        		//Recorro los horarios 
  	        		for (int i=0; i < forHorarios; i++) {
  	        			
  	        			
  	        				List<Fila> biometrico;
  	            			 String horaBiometrico="";
  	            			//variable para calculo de tiempo a justificar
  	            			 String fechaHoraBiometrico;
  	            			double tiempoAlmuerzo=0.0;
  	        		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
  	        		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
  	             			double tiempoDiferenciaEntradaEmpleado=0.0;
  	             			double tiempoDiferenciaSinEntradaEmpleado=0.0;

  	             			
  	         				//Para creacion de inconvenientes
  	         				String entradaSinJustificacion="";
  	             			String almuerzoSinEntradaJustificacion="";
  	             			String salidaSinJustificacion="";

  	         			 	String marcacion1="";
  	         			 	String marcacion="";

  	         			    banderaHoraExtra1001=0;
  	             			         			    
  	         			     
  	         			    
  	         				//Verifico si es dia feriado    
  	   	     			   banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
  	   	     	            
  	   	     	         
  	   	     	        int tipoEntrada1=0,tipoEntrada2=0;
  	   	     	            
  	   	     	            //si es de tipo nocturno
  	   	     	            
  	   	     	        if (ide_gtgre!=3) {
  	   	     			//Obtengo los timbres realizados por un empleado 
  	 	      	          biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

  						}else {
  							
  							List<Fila> biometricoTemporal;

  							TablaGenerica tabTurnosHorario=utilitario.consultar("select * from asi_turnos_horario where ide_astur="+contieneDiaTurno);
  							TablaGenerica tab_horario=utilitario.consultar("select * from asi_horario where ide_ashor="+tabTurnosHorario.getValor("IDE_ASHOR")); 
  							String horaEntrada=tab_horario.getValor("hora_inicial_ashor");
  							String horaSalida=tab_horario.getValor("hora_final_ashor");
  														
  														
  														
  							//Sumo un dia  a la fecha de entrada							
  							Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
  							sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
  							sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
  										    			
  							
  							String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
  							String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
  										    			
  										    						
  														
  														
  							//Parametro hasta para el sql de la consulta cuando es por turno nocturno
  			    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
  			    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
  			    		
  			    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
  			    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
  			    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
  			    			
  			    			
  			    			//suma una hora a la fecha inicial  22:00:00
  			    			String horaFechaFinConsulta= diaInicio+" "+horaEntrada;    			
  			    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-3));
  			    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
  			    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
  			    			// 
  	         				
  	         				//suma una hora a la fecha final 
  			    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaSalida;    			
  			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
  			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
  			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
  			    			biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			
  			    			cont=0;
  			    			biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaInicioReporte), getFechaAsyyyyMMdd(fechaInicioReporte));  			
  			    			numMarcacionesBiometrico=biometricoTemporal.size();
  			    			if (numMarcacionesBiometrico>1) {
  			    				
  			    				//numero de marcaciones
  			    				int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
  			    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
								
  			    				
  			    				String timbreMarcacion="";
								String 	horaBiometricoTemp="";
								for (Fila bioTemp : biometricoTemporal) {
								//hora de la tabla biometrico
	  	         				horaBiometricoTemp = (String)bioTemp.getCampos()[2];
	  	         				
	  	         				if (horaBiometricoTemp.compareTo(horaDiurno)>0 && horaBiometricoTemp.compareTo(horaTarde)<=0) {
	  			    				//estadoEntrada=true;
	  			    				//estadoTarde=false;
	  			    				//estadoNocturno=false;
	  			    				contadorDiurno++;
		  	         				pos_marcacionEntrada=horaBiometricoTemp;
	  			    				
								}else if (horaBiometricoTemp.compareTo(horaTarde)>0 && horaBiometricoTemp.compareTo(horaTardeTope)<=0) {
									//estadoEntrada=false;
	  			    				//estadoTarde=true;
	  			    				//estadoNocturno=false;
	  			    				
	  			    				contadorTarde++;
	  			    				pos_marcacionTarde=horaBiometricoTemp;
								}else if (horaBiometricoTemp.compareTo(horaTardeTope)>0 && horaBiometricoTemp.compareTo(horaNocturno)<=0) {
									//estadoEntrada=false;
	  			    				//estadoTarde=false;
	  			    				//estadoNocturno=true;
	  			    				pos_marcacionNocturno=horaBiometricoTemp;
	  			    				contadorNocturno++;
								}	
	  	         				
	  	         				
	  	         				
								
								}
								

								
								
								
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
								
 			    				 //biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));  			
			    					
  			    			 	
 			    					int mesTempDiaSiguiente=0,diaTempDiaSiguiente=0,anioDiaSiguiente=0,anioTempDiaSiguiente=0;
 			    		       		String diaDiaSiguiente="",mesDiaSiguiente="";
 			    		       		diaTempDiaSiguiente=utilitario.getDia(fechaFinReporte);
 			    		  		
 			    		  		
 			    		       		if (diaTempDiaSiguiente<10) {
 			    		  			diaDiaSiguiente="0"+diaTempDiaSiguiente;
 			    		       		}else {
 			    		  			diaDiaSiguiente=""+diaTempDiaSiguiente;
 			    		       		}
 			    		  		
 			    		  	   		  		
 			    		  		
 			    		       		mesTempDiaSiguiente=utilitario.getMes(fechaFinReporte);
 			    		       		if (mesTempDiaSiguiente<10) {
 			    		  			mesDiaSiguiente="0"+mesTempDiaSiguiente;
 			    		       		}else {
 			    		  			mesDiaSiguiente=""+mesTempDiaSiguiente;
 			    		       		}
 			    					
 			    					
 			    					
 			    		       		//Obtengo si tiene matriz
 			    		       		//int mesDiaSiguiente=0;
 			    		       		//mes=utilitario.getMes(fechaFinReporte);
 			    		       		//int anioTempDiaSiguiente=0;
 			    		       		//anioTempDiaSiguiente=utilitario.getAnio(fechaFinReporte);
 			    		       		TablaGenerica tab_AnioDiaSiguiente=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fechaFinReporte)+"%'");
 			    		       		anioDiaSiguiente=Integer.parseInt(tab_AnioDiaSiguiente.getValor("IDE_GEANI"));
 			    		      	       		
 			    		       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
 			    		       		TablaGenerica tabEmpleadoMatrizMensualDiaSiguiente = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
 			    		       				+ "and ide_gemes="+mesDiaSiguiente+" and ide_geani="+anio);
 			    		       		

 			    		            
 			    		                //Obtengo el turno por  dia,mes,anio,empleado 
 			    		       		int contieneDiaTurnoDiaSiguiente=0;
 			    		       		 contieneDiaTurnoDiaSiguiente=getHorarioXDia(empleado,dia,mes,anio);
 			    		     
 			    		       		 

 			     					TablaGenerica tabTipoTurnoDiaSiguiente=null;			
 			     					int ide_gtgreDiaSiguiente=0;
 			     					if (contieneDiaTurnoDiaSiguiente==0) {
 			     						ide_gtgreDiaSiguiente=0;
 			     					}else {
 			     						//Si tiene asignado matriz para ese dia
 			     						tabTipoTurnoDiaSiguiente =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurnoDiaSiguiente);
 			     						ide_gtgreDiaSiguiente=Integer.parseInt(tabTipoTurnoDiaSiguiente.getValor("IDE_GTGRE").toString());
 			     					      }
 			     	    	        
 			     	    	        
 			     	    	        int turnoDiaSiguiente=0;
 			     	    	    if (ide_gtgreDiaSiguiente==1) {
 			     	    	    	turnoDiaSiguiente=1;
								}else if (ide_gtgreDiaSiguiente==2) {
									turnoDiaSiguiente=2;
								}else if (ide_gtgreDiaSiguiente==3) {
									turnoDiaSiguiente=2;
								}		
								else {
									turnoDiaSiguiente=0;
								}    
 			     	    
								
								
								
								
								
								
								
								
								
								
								
								
								
								
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
								
				
			    		       	if (turnoDiaSiguiente!=0 ) {
  			    				if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno>0) {

  										if (turnoDiaSiguiente==3) {
  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
										
  											pos_marcacionTarde=horaBiometricoTemp;
  											
  										}
  								
  	  										
  										
  									}else if (contadorDiurno>0 && contadorTarde==0  && contadorNocturno>0) {
  										
  										if (turnoDiaSiguiente==3) {
  											//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
  										
  												pos_marcacionEntrada=horaBiometricoTemp;
  											}
  									
  										
  									}else if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno==0) {
  										if (turnoDiaSiguiente==3) {
  	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
  											
  	  											pos_marcacionTarde=horaBiometricoTemp;
  	  											
  	  										}
  										
   		       		
  			    		       		 }		
									else if (contadorDiurno==0 && contadorTarde>0 && contadorNocturno>0) {
										if (turnoDiaSiguiente==3) {
  	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
  											
  	  											pos_marcacionTarde=horaBiometricoTemp;
  	  											
  	  										}
	
										
									}
  									
  									
  									
  									
  									else {
///////////////////////////////////////////////////////////////////////////////////////Si no tiene turno al dia siquiente///////////////////////////////////////////////////////////////////////////////////
  			    		       			 
  			    		       		
  										
  										///////////////////////////ANALIZAR LOS CASOS
  										
  									/*	if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno>0) {
  	  											pos_marcacionNocturno="";
  	  											
  	  									}else if (contadorDiurno>0 && contadorTarde==0  && contadorNocturno>0) {
  	  										
  	  											pos_marcacionNocturno="";
  	  								  	  									
  	  				 				     }else if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno==0) {
  	  	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
  	  											
  	  	  											pos_marcacionTarde="";
  	   		       		
  	  			    		       		 }		
  										else if (contadorDiurno==0 && contadorTarde>0 && contadorNocturno>0) {
  	  	  											pos_marcacionTarde="";
  	  	  											
  	  	  														
  				    				
  			    		       		 }
  			    		       		 
  										
  										
  										else if (contadorDiurno>0 && contadorTarde==0 && contadorNocturno=0) {
	  											pos_marcacionTarde="";
	  											
	  														
			    				
		    		       		 }*/
  			    		       		 
  			    		       		 
			    		       	}
  			    		       		 
			    		    
			    		       	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			    		       	
			    		       	
			    		       	
			    		       	
			    		       	
			    		       	
  			    				
							}else {
								
								
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
								
								
								

								//numero de marcaciones
  			    				int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
  			    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
								
  			    				
  			    				String timbreMarcacion="";
								String 	horaBiometricoTemp="";
								for (Fila bioTemp : biometricoTemporal) {
								//hora de la tabla biometrico
	  	         				horaBiometricoTemp = (String)bioTemp.getCampos()[2];
	  	         				}
	  	         				if (horaBiometricoTemp.compareTo(horaDiurno)>0 && horaBiometricoTemp.compareTo(horaTarde)<=0) {
	  			    				//estadoEntrada=true;
	  			    				//estadoTarde=false;
	  			    				//estadoNocturno=false;
	  			    				contadorDiurno++;
		  	         				pos_marcacionEntrada=horaBiometricoTemp;
	  			    				
								}else if (horaBiometricoTemp.compareTo(horaTarde)>0 && horaBiometricoTemp.compareTo(horaTardeTope)<=0) {
									//estadoEntrada=false;
	  			    				//estadoTarde=true;
	  			    				//estadoNocturno=false;
	  			    				
	  			    				contadorTarde++;
	  			    				pos_marcacionTarde=horaBiometricoTemp;
								}else if (horaBiometricoTemp.compareTo(horaTardeTope)>0 && horaBiometricoTemp.compareTo(horaNocturno)<=0) {
									//estadoEntrada=false;
	  			    				//estadoTarde=false;
	  			    				//estadoNocturno=true;
	  			    				pos_marcacionNocturno=horaBiometricoTemp;
	  			    				contadorNocturno++;
								}	
	  	         				
	  	         				
	  	         				
								
								
								

								
								
								
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
								
 			    				 //biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));  			
			    					
  			    			 	
 			    					int mesTempDiaSiguiente=0,diaTempDiaSiguiente=0,anioDiaSiguiente=0,anioTempDiaSiguiente=0;
 			    		       		String diaDiaSiguiente="",mesDiaSiguiente="";
 			    		       		diaTempDiaSiguiente=utilitario.getDia(fechaFinReporte);
 			    		  		
 			    		  		
 			    		       		if (diaTempDiaSiguiente<10) {
 			    		  			diaDiaSiguiente="0"+diaTempDiaSiguiente;
 			    		       		}else {
 			    		  			diaDiaSiguiente=""+diaTempDiaSiguiente;
 			    		       		}
 			    		  		
 			    		  	   		  		
 			    		  		
 			    		       		mesTempDiaSiguiente=utilitario.getMes(fechaFinReporte);
 			    		       		if (mesTempDiaSiguiente<10) {
 			    		  			mesDiaSiguiente="0"+mesTempDiaSiguiente;
 			    		       		}else {
 			    		  			mesDiaSiguiente=""+mesTempDiaSiguiente;
 			    		       		}
 			    					
 			    					
 			    					
 			    		       		//Obtengo si tiene matriz
 			    		       		//int mesDiaSiguiente=0;
 			    		       		//mes=utilitario.getMes(fechaFinReporte);
 			    		       		//int anioTempDiaSiguiente=0;
 			    		       		//anioTempDiaSiguiente=utilitario.getAnio(fechaFinReporte);
 			    		       		TablaGenerica tab_AnioDiaSiguiente=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fechaFinReporte)+"%'");
 			    		       		anioDiaSiguiente=Integer.parseInt(tab_AnioDiaSiguiente.getValor("IDE_GEANI"));
 			    		      	       		
 			    		       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
 			    		       		TablaGenerica tabEmpleadoMatrizMensualDiaSiguiente = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
 			    		       				+ "and ide_gemes="+mesDiaSiguiente+" and ide_geani="+anio);
 			    		       		

 			    		            
 			    		                //Obtengo el turno por  dia,mes,anio,empleado 
 			    		       		int contieneDiaTurnoDiaSiguiente=0;
 			    		       		 contieneDiaTurnoDiaSiguiente=getHorarioXDia(empleado,dia,mes,anio);
 			    		     
 			    		       		 

 			     					TablaGenerica tabTipoTurnoDiaSiguiente=null;			
 			     					int ide_gtgreDiaSiguiente=0;
 			     					if (contieneDiaTurnoDiaSiguiente==0) {
 			     						ide_gtgreDiaSiguiente=0;
 			     					}else {
 			     						//Si tiene asignado matriz para ese dia
 			     						tabTipoTurnoDiaSiguiente =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurnoDiaSiguiente);
 			     						ide_gtgreDiaSiguiente=Integer.parseInt(tabTipoTurnoDiaSiguiente.getValor("IDE_GTGRE").toString());
 			     					      }
 			     	    	        
 			     	    	        
 			     	    	        int turnoDiaSiguiente=0;
 			     	    	    if (ide_gtgreDiaSiguiente==1) {
 			     	    	    	turnoDiaSiguiente=1;
								}else if (ide_gtgreDiaSiguiente==2) {
									turnoDiaSiguiente=2;
								}else if (ide_gtgreDiaSiguiente==3) {
									turnoDiaSiguiente=2;
								}		
								else {
									turnoDiaSiguiente=0;
								}    		
								
								
								
 			     	    	    
 			     	    	    
								
 			     	       	if (turnoDiaSiguiente!=0 ) {
  			    				if (contadorDiurno==1 && contadorTarde==0 && contadorNocturno==0) {

  										if (turnoDiaSiguiente==3) {
  											pos_marcacionEntrada=horaBiometricoTemp;
  										}
  								
  	  										
  										
  									}else if (contadorDiurno==0 & contadorTarde==1  && contadorNocturno==0) {
  										
  										if (turnoDiaSiguiente==3) {
  											pos_marcacionTarde=horaBiometricoTemp;
  											}
  									
  										
  									}		
			    		       	
  			    			
  									
  									}else {
										
  										
  										
  		  			    				if (contadorDiurno==1 && contadorTarde==0 && contadorNocturno==0) {
  		  			    				if (turnoDiaSiguiente==3) {
  	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
  											pos_marcacionEntrada=horaBiometricoTemp;
  	  										}
  	  								  	  										
  		  			    				}

  		  			    				if (contadorDiurno==0 && contadorTarde==1 && contadorNocturno==0) {
  		  			    				if (turnoDiaSiguiente==3) {
  											pos_marcacionTarde=horaBiometricoTemp;
  	  										}
		  			    				}
  									
  										
									}
								
														
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
															
							}
  			    			
  						}
  	        

  	      	         //Si tiene timbradas en el biometrico 
  	      	           if (biometrico.size()!=0) {
  			
  	      	           
  	      	     	boolean bandEntradaSinAlmuerzo=false;
           			
  	         			int banderaAlmuerzo=0;
  	         			int banderaAlmuerzoEntrada=0;
  	         			int banderaEntrada=0;
  	         			//recorro todos las timbradas
  	         			for (Fila bio : biometrico) {
  	         				//fecha de la tabla biometrico
  	         				fechaBiometrico = (String)bio.getCampos()[1];
  	         				//Hora de la tabla biometrico
  	         				horaBiometrico = (String)bio.getCampos()[2];
  	         				//Evento de acuerdo a si es entrada o almuerzo
  	         			//	String evento = (String)bio.getCampos()[3];
  	         				//Unimos la fecha con la hora
  	         				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
  	         			    //Calendario
  	         				
  	         				
  	         				
  	         				Calendar calFechaHoraBiometrico = Calendar.getInstance();
  	         			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
  	         				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
  	         				
  	         			
  	         			
  	         				
  	     			
  	         				boolean t=false;
  	         				//si contiene un turno asignado a ese dia  asignado en la matriz
  	         				if (contieneDiaTurno!=0 ) {
  			        				// TURNO SI NO ES DE TIPO NOCTURNO
  	         					if (ide_gtgre!=3 && banderaFeriados==false) {
  	         					
  	         				    horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
  	         					horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
  	         					horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
  	         					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
  	         					
  	         					
  	         					
  	         					 
  	         					//Hora Inicial tomada del horario
  	         					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
  	             			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
  	             			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
  	             			    
  	             			    //Suma de minuto para el ingreso
  	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
  	             			    
  	             			    
  	             			    
  	             			    //Hora desde Inicio de Entrada de Horario
  	             			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio),-3));
 	           	    		     Calendar calHoraFechaBase= Calendar.getInstance();
 	           	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporteBase));
 	         			
  	             			    
  	         					
  	         					//Hora Final tomada del horario
  	         					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
  	             			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  	             			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  	             			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
  	             			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
  	             			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
  	             			    calFechaHoraHorarioIniAlmuerzo.add(Calendar.MINUTE, -15);	    
  	             			    
  	             			    
			   	         		String fechaHoraHorarioInicial = fechaBiometrico+" "+horaFinAlmEmpl;//cogo la hora y le concateno con la fecha del horario
			   	         		Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
			   	         		calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicial));
			   	         		calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    

			   	                  		
			   	         		
			   	         		
			   	         		
			   	         		
			   	         		
			   	         			    
  	             			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
  	             			   Date finAlmuerzo=null;
  	             			   
  	             			   
 			   	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);

  	             			   Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
  			   	         			if (tiempoAlmuerzoHorario==0.5) {
  			   	         	
 	   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
 	   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 30);
 								}else if (tiempoAlmuerzoHorario==1) {
 									
 			   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
 			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
 								}
  			   	         		
  	             			   
  	             			   
  	             			   
  	             			  /* 
  	             			    
  	             			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
  	             			  if ((int)tiempoAlmuerzo==1) {
  	               			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
  	             			  							  }else{
  	               			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
  	             			  							  		}
  	         					
  	             			    //Sumamos una hora para el almuerzo
  	             			    fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(finAlmuerzo);
  	             			    Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();
  	             			    calFechaHoraHorarioFinAlmuerzo.setTime(finAlmuerzo); 

  	             			    SSSS
  	             		
  	       						*/
  	       					
  	             			    //Condicion si no existe hora de almuerzo
  	             			   if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")  ) {
  	             				   
  	             			       
  	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
  	                                //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  	             			    	
  	             			    	if (tipo1==0) {
  										
  									
  	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
  	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	                     			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                     			
  	                     			
  	                 	                 			    
  	                     			//Calculo de tiempo de atraso en la entrada del empleado
  	             			    	tipo1=1;
  	             			    	}
  	             			    }
  	          
  	            				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
  	    								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {

  	            					if (tipo1==0) {
  	    								
  	            						
  	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
  	         						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                  			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
                  			    	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	         			      		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	             			       	//tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	         			    		//tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	         			    		//tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	                     			//tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
  	                     			//tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                 			    
  	                 			    tipo1=1;
  	            					}else {
  	            						
  	            						
  	            					if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {
  	            				   	
  	                  			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	               						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                      			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	                 			    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	                 				tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	                  			    
  	                 			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                 			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  	                 			    tipo1=2;
  	            						}  
  	            					else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) <= 0) {
  	                  			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	               						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                      			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	            						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	            						    tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
  	    	                 			    tipo1=2;
  	            					}
  	            					
  	            					
  	    							}
  	            					
  	            					
  	            					
  	            					
  	            					
  	            					
  	            					//else de caso
  	             			                 			    
  	             				}else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) > 0) {
  	              			    
  	             					if (tipo1==0) {
  	    	    						
  	              			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
      	        						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	            							tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	    	        					    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  	    	                     			    tipo1=4;
  	             		
  	             					
  	             					
  	  
  	             					}else {
  	             					tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
 	                     				tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
 	            						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
 	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
 	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
 	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
 	    	                 			    tipo1=2;
  	             				}

  	             				}
  	             			    
  	             			    
  	             			    
  	             			    //Segundo caso Caso
  	             			    
  	             			    
  	            				/*else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin)>= 0) {
  	            					
  	            					if (tipo1==0) {
  	    								
  	            						
  	   	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	   	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	   	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
  	   	         			    		tipo1=1;
  	            					
  	            					}else {
  	   	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
 										
 									}
  	            					
  	            			
  	            			  
  								}*/
  	             			    //SQT
  	             			
  	  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////           		  
  				
  	             			   }//fin de condicion  si no tiene ALMUERZO
  				
  	             			   else {
  	             				  //Caso contrario si tiene almuerzo
  	                			   
  	                			    Entrada=6;
  	   	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
  	                			    
  	                			  //  if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
  	                			    	if (banderaEntrada==0) {
  	                                   //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  	                			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
  	                			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	            			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  	                			    
  	                			    	//Calculo de tiempo de atraso en la entrada del empleado
  	                			    	tipo1=2;
  	                			    	Entrada=1;
  	                			    	banderaEntrada=1;
  	                			    	}
  	                			    }
  	             
  	               				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
  	       								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
  	               					if (banderaEntrada==0) {
  	                			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
  	                			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	            			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  	                			    	//tipo1=1;
  	                			    	//tipo2=1;
  	                			    	tipo1=2;
  	                			    	banderaEntrada++;
  	                			    	Entrada=1;
  	               					}
  	                				}
/*
  	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) > 0
  	                			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) < 0){
  	                			    	//Almuerzo
  	                			    	
  	                			    	if (banderaAlmuerzo == 0){
  	                                       //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  	                    			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	                    			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
  	                			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
  	                    			    	banderaAlmuerzo++;
  	   	                			    	tipo1=2;
  	                    			    	//tipo3=1;
  	   	                			    	Entrada=2;	
  	                    			    	
  	                			    	}else{
  	                                       //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

  	                			    		if (banderaAlmuerzoEntrada==0) {
  	                			    			tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
      	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
      	                    			    	
  	       								tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                    			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
  	                    			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
  	                    			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
  	                    			    	tiempoAlmuerzo = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
  	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
  	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
  	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                    			    	//Validacion si se pasa el almuerzo
  	                    			    	//tipo4=1;
  	   	                			    	
  	                    			    	AlmuerzoEntrada=1;
  	                    			    	Entrada=3;
  	                			    		}
  	                			    	}
  	                    			   	

  	                			    }  
  	                			  */
  	   	             			    

  	   	             			else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
  	   		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo==0){
  	   		       			    	//Almuerzo

  	   		       			    	if (banderaAlmuerzo == 0){
  	   		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  	   		           			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	   		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
  	   		       			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ (pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
  	   	   		      					horaAnterior=horaBiometrico;
  	   		           			    	banderaAlmuerzo++;
  	   		           			    	tipo3=1;
  	   		           			  
  	   		       			    	}
  	   		       			    
  	   		       			    	
  	   		       			    	
  	   		       			    	
  	   		       			    }else if (banderaAlmuerzo==1 && horaBiometrico.compareTo(horaAnterior)>0  && horaBiometrico.compareTo(horaFinEmpl)<0 && banderaAlmuerzoEntrada==0){
  	   		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

  			
  				   	     			String uno= fechaBiometrico+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
  				   	         		String dos = fechaBiometrico+" "+horaFinAlmEmpl;
  				   	     			
  				   	     				Calendar caluno = Calendar.getInstance();//
  				   	         			Calendar caldos = Calendar.getInstance();//
  				   	         			caluno.setTime(getFechaAsyyyyMMddHHmmss(dos));//
  				   	         			caldos.setTime(getFechaAsyyyyMMddHHmmss(uno));//
  				   	         			//Suma de minuto para el ingreso
  				   	         			
  				   	         			if((pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==1){
  				   	         			caluno.add(Calendar.MINUTE, 60);	
  				   	         			}else if (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))==0.5) {
  				   	         			caluno.add(Calendar.MINUTE, 30);	
  										}else {
  											
  										}
  				   	         			
  				   	         				
  	   		       			    	
  	   		       			    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
  	   	   		       			    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0){
  	   	   		       			   	 
  	   		       			    	    tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	   		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	   		           			    	
  	   		   							tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
  	   		           			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
  	   		           			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
  	   		           			    	tiempoAlmuerzo = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
  	   		           			    	
  	   		           			    	
  	   		           			    	
  	   		           			    	
  	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
  	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
  			       			
  	   		           			    	//Validacion si se pasa el almuerzo
  	   		           			    	tipo4=1;
  	   		           			    	banderaAlmuerzoEntrada++;
  	   		           			    	horaAnterior="";
  	   		       			    		}
  	   		       			    	}
  	   		           			   	

  	   		       			      
  	   	             			    
  	   	             			    
  	   	             			    
  	   	             			    
  	                			    
  	                			   
  	                			    
  	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
  	                			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
  	                			    	//Salida
  	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
  	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  	                    			   	//tipo5=1;
  	                    			   	//tipo6=0;
 	   	                			    	tipo1=2;
 	   	                			    	
 	   	                			    	
 	   	                			    	

  	                    			   	Salida=1;
  	                    			   	Entrada=4;
  	                			    	
  	                			    }  	
  	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
  	                                 //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

  	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
 	   	                			    	tipo1=2;
  	                			    	//tipo6=1;
  	                			    	//tipo5=0;
  	                    			   	Salida=1;
  	                    				Entrada=5;

  	                			    }    
  	            				
  	                			
  							}//Fin de ciclo si contiene almuerzo
  	             			   
  	             			   
  	             
  	           
  				//Si es igual 
  	         					}else if(ide_gtgre==3 && banderaFeriados==false && biometrico.size()!=0){ 
  	         						//Si es de tipo nocturno	
  	         						//System.out.println("TIPO NOCTURNO");
  	           						
  	           						
  	         						
  	         						
  	         						
  	         						
  	         						//Ingreso a metodo
  	         						cont++;
  	           						
  	           						int banderaIngreso=0;
  	           	    			    int banderaSalida=0;
  	           	    				    banderaAlmuerzo=0;
  	           	    				    banderaEntrada=0;

  	           	    				int tipoEntrada3=0,tipoEntrada4=0;

  	           	    				//Horario de acuerdo a los turnos ingrresados por talento humano
  	           						horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
  	           						horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
  	           						horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
  	           						horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
  	           						horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp

  	           					
  	           						
  	           						
  	           						String HoraHorarioInicio = horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
  	           	    			   Calendar  calFechaHoraHorarioInicio = Calendar.getInstance();//
  	           	    			    
  	           						String HoraHorarioFin = horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
  	           						Calendar calFechaHoraHorarioFin = Calendar.getInstance();

  	           					
  	           	    			Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
  	           	    			sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
  	           	    			sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
  	           	    			
  	           	    	
  	           	    		
  	           	    			String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
  	           	    			String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
  	           	    			
  	           	    		
  	           	    			
  	           	    			
  	           	    			
  	           	    			
  	           	    			//Parametro hasta para el sql de la consulta cuando es por turno nocturno
  	           	    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
  	           	    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
  	           	    		
  	           	    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
  	           	    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
  	           	    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
  	           	    			
  	           	    			
  	           	    			//suma una hora a la fecha inicial  22:00:00
  	           	    			String horaFechaFinConsulta= diaInicio+" "+horaInicioEmpl;    			
  	           	    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-1));
  	           	    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
  	           	    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
  	           						
  	           	    			//

  	           	    			//suma una hora a la fecha final 
  	           	    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaFinEmpl;    			
  	           	    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
  	           	    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
  	           	    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
  	           						
  	           	    			 
  	           	    			
  	           	    			
  	         //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7  	    			
  	           	    			
  	           	    			
  	           	    			
  	           	 			//Horario de acuerdo a los turnos ingrresados por talento humano
  	        					String horaInicioEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
  	        					String horaFinEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
  	        					horaIniAlmEmpl = horariosEmpleadoMes .getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
  	        					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
  	        					
  	        					
  	        					
  	        					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl1; //cogo la hora y le concateno con la fecha del horario
  	        	    			//String fechaHoraHorarioInicio= getFechaAsyyyyMMddHHmmss(sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio1),Integer.parseInt(minuto_tolerancia_astur)));

  	        					
  	        					
  	        					Calendar calFechaHoraHorarioInicio1 = Calendar.getInstance();//
  	            			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));
  	            			    
  	            			    //Suma de minuto para el ingreso
  	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
  	        					
  	        					 String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
  	            			    Calendar calFechaHoraHorarioFin1 = Calendar.getInstance();
  	            			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  	            			    
  	            			  //Desde hasta puede hora de salida para el horario nocturno
  	            			   String fechaHoraHorarioFinSalida = fechaBiometrico+" 20:00:00";
  	            			    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
  	            			    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
  	        					
  	            			    String fechaHoraHorarioInicioFinSalida = fechaBiometrico+" 04:00:00";
  	            			    Calendar calFechaHoraHorarioInicioFinSalida = Calendar.getInstance();
  	            			    calFechaHoraHorarioInicioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinSalida));
  	        					       			    
  	        					//Desde hasta puede hora de ingreso para el horario nocturno
  	            			    String fechaHoraHorarioFinEntrada = fechaBiometrico+" 24:00:00";
  	            			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
  	            			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada));
  	           			    
  	            				String fechaHoraHorarioInicioEntrada = fechaBiometrico+" 00:00:00";
  	            			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
  	            			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
  	           				    
  	            			    
  	            			    Calendar calSumaDia = Calendar.getInstance();
  	            			    calSumaDia.setTime(getFechaAsyyyyMMddHHmmss(horaFechaSumaDia1));
  	           				    
  	            			    
  	            			   //Prueba horario nocturno
  	            			    //variable que guarda la hora desde 00:00: del dia siguiente
  	            			    String fechaHoraCeroHoras = fechaBiometrico+" 00:00:00";
  	            			    Calendar fecHoraInicio = Calendar.getInstance();
  	            			    fecHoraInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
  	           				    
  	            			    
  	            			    
  	            				//Siguiente
  	                			String fecha= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado);
  	                			String fechayhora=fecha+" 08:00:00";
  	                		    Calendar calFin = Calendar.getInstance();//
  	            			    calFin.setTime(getFechaAsyyyyMMddHHmmss(fechayhora));//
  	                			
  	            			    
  	            			  //Siguiente
  	                			String fecha1= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
  	                			String fechayhora12=fecha1+" 22:00:00";
  	                		    Calendar calEntrada = Calendar.getInstance();//
  	            			    calEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechayhora12));//
  	            			    
  	            			    
  	            			    
  	            			    

  	            			  	if (cont==1) {

  	            			    Integer tipoEvento = 0;
  	            			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

  	            			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	                    			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
  	                				       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  	    	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
      	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                    			    	tipo1=1;
  	                    			    	tipoEntrada1=1;
  	                    			    	tipoEntrada2=0;
  	                    			    	tipoNocturno=1;
  	                    			    	
  	                     
  	                    			    }
  	            			    
  	            				else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0 &&
                  			    		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada)<=0){
  	                                   
  									
  									
                  			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
                  			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
              			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
              				       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
 	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
 	   	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
 	   	            					tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
 	   	            					tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
 	   	            					tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
                  			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
                  			    	tipo1=1;
                      			    tipoEntrada1=1;
                  			    	tipoEntrada2=0;
                  			    	tipoNocturno=1;
                  			   }
  	                    			    
  	                    			    //mayor a la hora de horario incio con la hora biometrico timbrada
  	                			      //menor a la hora 24:00:00
  	            			    
  	            			  /*  if (tipoEntrada1==0) {
  	            			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
              			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "SIN TIMBRE");
                  			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
              			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
                  			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM","0");
                  			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
                  			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN TIMBRE");

 								}*/
  	            			    
  	            			 	
  	            			  	}	
  	            			 	
  	            			 	if (cont>1) {
  	                    				
  	                     			    //mayor a hora: 0:00:00
  	                    			    //menor a las 6 de la manaña	
  	                    			     if(calFechaHoraBiometrico.compareTo(fecHoraInicio) >0 &&
  	                    			    	     calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)<0){
  	                    			    		    	
  	                    			       	//tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
  	                			    		//tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
  	                    			    	 
  	                    			    	 
  	                			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	                  			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	                  			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
  	                  			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	   	   	            					tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
  	   	   	            					tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	   	   	            					tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
 	   	   	            				    
  	                  			    		if (tipoNocturno==0) {
  	                  			    		  tipo1=3;
 											}else if(tipoNocturno==1){
 	   	   	            				    tipo1=2;}
  	                    			    //mayor que las 06:00:00
  	                    			    //	y menor que 08:00:00
  	                    			    }else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)>=0 
  	                    			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)<=0){
  	                    			    	
  	                 			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
 	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
 	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
 	   	   	            				    tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
 	   	   	            				    tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
 	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  	              			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  	              			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  	              			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  	              			   	if (tipoNocturno==0) {
              			    		  tipo1=3;
 									}else if(tipoNocturno==1){
 	   	            				    tipo1=2;}

  	                    			    }
  	        	
  	             					
  	             					
  	                    				}
  	             					
  	             					
  	         						
  	         						
  	         						
  	         						
  	         					}
  	         					//si es de tipo nocturno pero  no es feriado y tiene marcaciones
  	         					//else if(ide_gtgre==3 && banderaFeriados==false && biometrico.size()==0){
  	         						
  	         					//}
  	         					//Fin de ciclo si es tipo ncturno
  	         					//si es feriado 
  	         					else if (banderaFeriados=true && estado_empleado_nocturno==false && biometrico.size()>0) {
  								//si es dia normal y es feriado

  	         						
  	         						
  	         						
  	         						
  										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
  						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
  						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
  						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
  											
  										
  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
  						                	 if (banderaHoraExtra1001==0) {
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
  									    		banderaHoraExtra1001=1;
  									   	    }else {
  											        			    
  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
  						 			    	
  						 			    	banderaHoraExtra1001=2;
  						 				
  										
  										        				
  						 				}
  									
  									}
  						 			    
  						 			    
  						 			    
  						 			    
  						 			    
  			   	     				
  			   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==true && biometrico.size()>0 ) {
  			   	     					
  			   	     					
  			   	     					
  			   	     					//Obtengo el dia anterior para comparar con la fecha actual	
  			   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
						            
  			   	     				
  			   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
  			   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
  			   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
  			   	     					
  			   	     				//Si el dia anterior no era de tipo nocturno
  			   	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()==0) {
									
  										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
  						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
  						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
  						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
  											
  										
  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
  						                	 if (banderaHoraExtra1001==0) {
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
  									    		banderaHoraExtra1001=1;
  									   	    }else {
  											        			    
  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
  						 			    	
  						 			    	banderaHoraExtra1001=2;
  						 				
  										
  										        				
  						 				}
  									
  									}  			   	     					
  			   	     					
  			   	     				
  			   	     				
  			   	     				}
  			   	     				else{
  			   	     						//Si el empleado y la fecha son iguales 
												//Consulto la hora de salida 
				if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty() )
				{
					String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
		 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
		 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
		 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
					    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		                	 if (banderaHoraExtra1001==0) {
		 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
					    		banderaHoraExtra1001=1;
					   	    }else {
					        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
		 			    	banderaHoraExtra1001=2;
		 				}
				
					    			}
					    
					    
					    
					    
				}else {
					String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
	 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
	 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
			
												String horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
		  						 			    if (horaBiometrico==horaSalidaAnterior) {
											}else {
		  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		  						                	 if (banderaHoraExtra1001==0) {
		  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
		  									    		banderaHoraExtra1001=1;
		  									   	    }else {
		  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
		  						 			    	banderaHoraExtra1001=2;
		  						 				}//else de validacion de hora extra
		  						 		}//hora inicio y hora fin
											}//ELSE DE HORA ANTERIOR DE SALIDA	
											}//ELSE DE HORA FIN
									
											
											
											
											
											
											
											}//Else si hay empleado y fecha anteior
  			   	     				
  			   	     				
  			   	     				
  			   	     					
  			   	     					
  			   	 //    				System.out.println("ddddd");	
  			   	     				}
  	         						
  	         						
  	         						
  	         						
  	         						
  	         						
  	         						
  	         						
  	         						
  								
  					
  	         			}// Fin de ciclo si contiene turno 
  	         				
  	         				
  	         				
  	         				else if (contieneDiaTurno==0) {  					
  	         				//si no contiene turno
  	         				
  	         			if(biometrico.size()!=0){
  	         					if (banderaFeriados==false && estado_empleado_nocturno==false ) {
  								
  	         				String fechaHoraHorarioFin = getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico),1))+" 24:00:00";//cogo la hora y le concateno con la fecha del horario
  	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  	         			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
  	         			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
  	         			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
  	         			   
  	         if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) <= 0){
	  	   	         			   if (banderaHoraExtra1001==0) {

	  		         			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	  		         			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  		         			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	  		 	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 
	  		 	         				banderaHoraExtra1001=1;
	  		     			   	    }else {
	  									        			    
	  		        			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  		         			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	  		         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	  		         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	  		         			    	banderaHoraExtra1001=2;
	  			     				
	  		     				
	  								        				
	  		         				}

	  	         			    
	  	         			    
	  	         			    
	  	         			    }								
							
								
							
	   	     				

				 			  	         			   
  	         				}else if (banderaFeriados==false  && estado_empleado_nocturno==true && biometrico.size()>0) { 
  	         						//Cambios
  	         					
  	         					
									String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
											
										
						 				//Obtengo el dia anterior para comparar con la fecha actual	
			   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
					            
			   	     				
			   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
			   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
		  	         			    
			   	     				String horaSalidaAnterior="";	
			   	     				if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
										 horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");

									}
			   	     				
			   	     				if (horaSalidaAnterior==null || horaSalidaAnterior.equals("") || horaSalidaAnterior.isEmpty()) {

						 			    
						 			    
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				
										
										        				
						 				}
									
									}
						 			    
			   	     				}else {
			   	     			    if (horaBiometrico.equals(horaSalidaAnterior)) {
			   	     			    horaBiometrico="";
			   	     			    	banderaSinAccion=true;
			   	     			    	
			   	     			    	
									}else {

									    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
							 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) <= 0){
							                	 if (banderaHoraExtra1001==0) {
							 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
							 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
							 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
							 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
										    		banderaHoraExtra1001=1;
										    		banderaSinAccion=false;
										   	    }else {
												        			    
										        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
							 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
							 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
							 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
							 			    	
							 			    	banderaHoraExtra1001=2;
							 			    	banderaSinAccion=false;
											
											        				
							 				}
										
										}
			  	         			    
			  	         			    
			  	         			    }//fin de si tiene TurnoNocturno el dia anteriror
									}

									
						 			    
						 			    
						 			    
			   	     				
			   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==true && biometrico.size()>0 ) {
			   	     					
			   	     					
			   	     					
			   	     					//Obtengo el dia anterior para comparar con la fecha actual	
			   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
					            
			   	     				
			   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
			   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
			   	     					
			   	     				//Si el dia anterior no era de tipo nocturno
			   	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()==0) {
								
										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
											
										
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				
										
										        				
						 				}
									
									}  			   	     					
			   	     					
			   	     				
			   	     				
			   	     				}
			   	     				else{
			   	     						//Si el empleado y la fecha son iguales 
											//Consulto la hora de salida 
			if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty() )
			{
				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
	 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
	 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
				    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	                	 if (banderaHoraExtra1001==0) {
	 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
				    		banderaHoraExtra1001=1;
				   	    }else {
				        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	 			    	banderaHoraExtra1001=2;
	 				}
			
				    			}
				    
				    
				    
				    
			}else {
				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
		
											String horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
	  						 			    if (horaBiometrico==horaSalidaAnterior) {
										}else {
	  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	  						                	 if (banderaHoraExtra1001==0) {
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
	  									    		banderaHoraExtra1001=1;
	  									   	    }else {
	  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	  						 			    	banderaHoraExtra1001=2;
	  						 				}//else de validacion de hora extra
	  						 		}//hora inicio y hora fin
										}//ELSE DE HORA ANTERIOR DE SALIDA	
										}//ELSE DE HORA FIN
								
										
										
										
										
										
										
										}//Else si hay empleado y fecha anteior
			   	     				
			   	     				
			   	     				
  	         					
  	         					
  	         					
			   	     		       			   
  	         			   
  			   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==false && biometrico.size()>0) {
  								//si es dia normal y es feriado

  	         						
  	         						
  	         						
  	         						
  										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
  						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
  						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
  						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
  											
  										
  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
  						                	 if (banderaHoraExtra1001==0) {
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
  									    		banderaHoraExtra1001=1;
  									   	    }else {
  											        			    
  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
  						 			    	
  						 			    	banderaHoraExtra1001=2;
  						 				
  										
  										        				
  						 				}
  									
  									}
  						 			    
  						 			    
  						 			    
  						 			    
  						 			    
  			   	     				
  			   	     				}/*else if(estado_empleado_nocturno==true || estado_empleado_nocturno==false && biometrico.size()==1){
  	         			    	//si tiene anterior
  	         						//Variable que avisa si se ingresa o no el evento su es de tipo nocturno el dia anterior
  	         						sinIngreso=true;
  								break;
  							}*/
  	         			    	 
  	         			    
  	         			    
  	         				}//Si tiene marcaciones en el biometrico
  	         			else {
 								
 							}
  	         			    
  	         			}// FIN DE CICLO SI NO CONTIENE HORARIO 
  	         					
  	         				
  	         				
  	         		
  	         				
  	        			}//For Biometrico
  	         			
  	         			
  	         	    if (tipo1==0 && Entrada==0 && banderaHoraExtra1001==0) {
           			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
      			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
             		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");  
    			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
                		   	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
 	         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
                			
 						}
           			    
           			  if (tipo1==1 && Entrada==0 && banderaHoraExtra1001==0 )  {
 	             			  
             		    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
             		    		tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");  
                              tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
      			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                    			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                  			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
                  			tab_reporte.setValor(itemReporte, "HORAFINBAND", "");

 							}
           			  
           			  
           			  if (tipo1==4 && Entrada==0 && banderaHoraExtra1001==0) {
           	  			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
            			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
        			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
        			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
          			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                 		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
               		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
        			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                    			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                    			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
               			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");


 							}
             			    
           			  
           			    
           	
           			  if (tipo1==3 && Entrada==0 && banderaHoraExtra1001==0) {
           				  
               		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
               		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
           				 	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
            			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
        			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
        			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
          			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
        			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                    			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                    			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
               			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");

 							}
           			  
           			  
           			  if (Entrada!=0 && banderaHoraExtra1001==0) {
         	  			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
        		    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
         			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
           			  
 						}
           			  
           			  
           			  
           			  
           			  
           			  
           			  

  	      	     	}// if biometrico
  	      	           else {	//Si no se tiene marcaciones hechas en el biometrico 
  	      	     		
  	      	     		if (contieneDiaTurno!=0) {
  	      	     			//Si  contiene horario pero no timbrada
  	      	     		double minAlm=0.0;
  	      	     		horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
  	      	     		horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
  	      	     		horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
  	      	     		horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
  	      	     		minAlm =(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);// horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR");//Hora fin erp

  	      	     		
           					
  	      	     		if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")) {
  	      	     			horaIniAlmEmpl="S/A";
  	      	     			horaFinAlmEmpl="S/A";
               		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
               		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
        			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+minAlm);

 							}
  	      	     		
           					
  	      	     			
  		      	  	    	
          			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
          			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO","" );
      			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
          			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
      			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
          			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ minAlm);
        			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", "0");
          			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
        			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
        			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
  	      	        		
  	      	     		
        			    		
  	      	     		
  	      	     			
  	      	     			
  	      	     			
  					}
  	      	     		else {
  	      	     			banderaSinAccion=true;
  						}
  	      	    
  	      	     		
  	      	     		
  					}
  	         			
  	        		}//For horario
  	          
  	        		
  	        		
  	        		
  	        		double almuerzo1=0.0;
  					String horarioEntrada="";
  					String horarioSalida="";
  					boolean bandera=false,banderaEntrada=false;
  					int estadosAlmuerzo=0;
  					//Si no hay datos le pongo =""
  						if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("")) {
  							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") || tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
  								almuerzo1=0;
  								estadosAlmuerzo=1;
							}else {
								almuerzo1=0;
								estadosAlmuerzo=0;
							}
  							
  							
  						
  					}else if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("S/A")) {
  		  				almuerzo1=0;
  		  			estadosAlmuerzo=1;
  		  				
  		  				
  			
  					}else {
  						if(tab_reporte.getValor(itemReporte, "TIEMPOALM")== null || tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()
  								|| tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("0")){
  							almuerzo=0;
  							estadosAlmuerzo=0;
  						}else {
	    						almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));
	    						estadosAlmuerzo=1;							
						}
			
					}
  					
  						
  						
  						bandera=false;
  					
  					 
  					
  			/*		if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
  					bandera=true;
  				}else {
  					bandera=false;
  				}
  	        		
  					if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("EXTRA") ) {
  	  					bandera=true;
  	  				}else {
  	  					bandera=false;
  	  				}	
  					
  					
  					
  					if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("FERIADO") ) {
  	  					bandera=true;
  	  				}else {
  	  					bandera=false;
  	  				}
  					
  				*/	
  					
  					
  					if (contieneDiaTurno!=0 && banderaSinAccion==false && banderaHoraExtra1001==0) {
  					
  			/*			TablaGenerica tabSucursal = utilitario.consultar(SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  	
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
  				"SUCU.IDE_SUCU, AREA.IDE_GEARE, " +
  				"DEPA.IDE_GEDEP " +
  				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
  				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
  				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
  				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
  				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "+
  				"where epar.ide_gtemp="+empleado+ " "
  				+ "	ORDER BY DETALLE_GEARE ASC");
  						
  				String sucursal=tabSucursal.getValor("IDE_SUCU");		
  				String  area=tabSucursal.getValor("IDE_SUCU");
  				String departamento=tabSucursal.getValor("IDE_SUCU");
  						
  				*/	
   	    	   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
  	 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
  	 		   tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
  	 		   tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
  	 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometricoAgrupadaXEmpleado));
  				
  					
  					
  					} 
  					
  					else if (contieneDiaTurno==0 && banderaSinAccion==false &&  sinIngreso==false && banderaHoraExtra1001==0) {
  					   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
  		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
  		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
  		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
  		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado));         
  					}else if (banderaSinAccion==true) {
  						
  					}
  					else if (banderaHoraExtra1001==2 || banderaHoraExtra1001==1) {
						
					if (banderaHoraExtra1001==1) {
					
						insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
								"", "","", "",0 ,"0","","", "",false,diaSemana(fechaBiometricoAgrupadaXEmpleado));
								    
					}
					if (banderaHoraExtra1001==2) {
						
					
	insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
	"", "","", "",0 ,"0","",tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado));
	    
		}
  					} 					
  					else {
  						
  					}
    
  				}//Si tiene Matriz
  				
  				

  				
  				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
  				

  				
  		   		//AQUI ES CONDICION si tiene turno de matriz
  		       		     
       		     
       		     
    		     
       if (tabEmpleado.getTotalFilas()!=0 && tabEmpleadoMatrizMensual.getTotalFilas()==0  ) {//TIENE MATRIZ
  		   				//Busca si tiene asignado horario anualmente
  		   					
  		   					
  		   					
  		   			/*		if (banderaCambioHorario || banderaCambioHorarioPermisoOnline) {
  		   						
  		   						ddddddddd
  		   						
  		   					}
  		   				*/	
  		   					
  		   					
  		   					
  		   					
  		   				
  		   			    	
  		   			    	//Hacer la consulta para poder retornar los horarios de cada empleado
  		   			    	
  		   			        TablaGenerica tabObternerTurnos=this.obtenerTurnosMatriz();
  		   			        
//  		   					
  		   			        
  		   			        if (tabObternerTurnos.getTotalFilas()==0) {
  		   						utilitario.agregarMensajeError("No existen Horarios", "Asignados a turnos");
  		   					}
  		   			    	
  		   			    	for (int itemHorario=0; itemHorario< tabObternerTurnos.getTotalFilas(); itemHorario++) {
  		   			    		//Obtengo el ide del empleado con el cual obtendrenmos los horarios
  		   			    		Integer ide_horario=0;
  		   			    		ide_horario= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor(itemHorario, "IDE_ASHOR"));
  		   			            if(str_ide_horarios.toString().isEmpty()==false){
  		   			                str_ide_horarios.append(",");
  		   			    			}
  		   			    			str_ide_horarios.append(ide_horario);

  		   			    	}
  		   			    	
  		   			        	
  		   			    	//Metodo devuelve el horario asignado a cada empleado
  		   			       	TablaGenerica diasXHorario=utilitario.consultar("SELECT HORARIO.IDE_ASHOR,DIA.IDE_GEDIA,DIA.DETALLE_GEDIA FROM ASI_DIA_HORARIO HORARIO "
  		   			    			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=HORARIO.IDE_GEDIA "
  		   			    			+ "WHERE HORARIO.IDE_ASHOR IN ("+str_ide_horarios+")");
  		   			    	
  		   			       	       	
  		   			    	
  		   			    	
  		   			    	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
  		   			    	

  		   		    		TablaGenerica horariosEmpleado = this.obtenerHorariosEmpleado(empleado);

  		   			    		//para turno nocturno
  		   			    	//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
  		   			    //		String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
  		   			    //		String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
  		   			    //		TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
  		   			    //		String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
  		   					
  		   					
  		   					
  		   					
  		   					
  		   					
  		   					
  		   					
  		   						//	TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
  		   			   	    //		String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
  		   			   	    		
  		   			   	    		int banderaHoraExtra1001=0;
  		   			   				    banderaFeriados=false;

  		   			   				//tipo1= 0;
  		   			   			    Integer tipo2 = 0;
  		   			   			    Integer tipo3 = 0;
  		   			   			    Integer tipo4= 0;
  		   			   			    Integer tipo5 = 0;
  		   			   			    Integer tipo6 = 0;
  		   			   			    Integer tipo7 = 0;
  		   			   				Integer tipo1SinAlmuerzo= 0;
  		   			   			    Integer tipo2SinAlmuerzo = 0;
  		   			   			    Integer tipo3SinAlmuerzo = 0;
  		   			   			    Integer tipo4SinAlmuerzo= 0;
  		   			   	String  SINDATO="";
  		   			   		    
  		   			     int turno= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
  		   		    		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(turno);
  		   			   	    		//Recorro los horarios 
  		   			   	    		for (int i=0; i < horariosEmpleado.getTotalFilas(); i++) {
  		   			   	    			//Me devuelve el dia si se encuentra dentro de su horario
  		   			   	    			Map<Integer, Boolean> mapDias = obtenerDiasByHorario(diasXHorario, Integer.parseInt(horariosEmpleado.getValor(i, "ide_ashor")));
  		   			   	    			
  		   			   	    			List<Fila> biometrico;
  		   			   	    			//Para marcaciones de tipo nocturno
  		   			   	    			   

  		   			   	        			 String horaBiometrico="";
  		   			   	        			//variable para calculo de tiempo a justificar
  		   			   	        			 String fechaHoraBiometrico;
  		   			   	        			double tiempoAlmuerzo=0.0;
  		   			   	    		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
  		   			   	    		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
  		   			   	         			double tiempoDiferenciaEntradaEmpleado=0.0;
  		   			   	         			double tiempoDiferenciaSinEntradaEmpleado=0.0;

  		   			   	         			 
  		   			   	     				//Para creacion de inconvenientes
  		   			   	     				String entradaSinJustificacion="";
  		   			   	         			String almuerzoSinEntradaJustificacion="";
  		   			   	         			String salidaSinJustificacion="";

  		   			   	     			 	String marcacion1="";
  		   			   	     			 	String marcacion="";

  		   			   	     			    banderaHoraExtra1001=0;
  		   			   	         			         			    
  		   			   	     			//Verifico si es dia feriado    
  		   			   	     			      banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
  		   			   	     	            
  		   			   	     	       
  		   			   	    			//Obtengo los timbres realizados por un empleado 
  		   			   	    			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

  		   			   	     			if (biometrico.size()!=0 ) 
  		   			   	     			//Si es contiene marcaciones en le biomtrico
  		   			   	     			{
  		   									 
  		   								

  		   			   	     			int banderaAlmuerzo=0;
  		   			   	     			int banderaAlmuerzoEntrada=0;
  		   			   	     			int banderaEntrada=0;
  		   			   	     			//recorro todos las timbradas
  		   			   	     			for (Fila bio : biometrico) {
  		   			   	     				//fecha de la tabla biometrico
  		   			   	     				fechaBiometrico = (String)bio.getCampos()[1];
  		   			   	     				//Hora de la tabla biometrico
  		   			   	     				horaBiometrico = (String)bio.getCampos()[2];
  		   			   	     				//Evento de acuerdo a si es entrada o almuerzo
  		   			   	     			//	String evento = (String)bio.getCampos()[3];
  		   			   	     				//Unimos la fecha con la hora
  		   			   	     				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
  		   			   	     			    //Calendario 
  		   			   	     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
  		   			   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
  		   			   	     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
  		   			   	     					
  		   			   	     				//Obtengo el dia de la semana a que corresponde esa fecha
  		   			   	     				int diaSemana = calFechaHoraBiometrico.get(Calendar.DAY_OF_WEEK);
  		   			   	     			    //Validacion para el dia domingo=0
  		   			   	     				if (diaSemana==1) {
  		   			   	 						diaSemana=8;
  		   			   	 					}
  		   			   	     				
  		   			   	     				//verifico si el dia se encuentra en mi mapita de dias y la bandera de si es dia feriado
  		   			   	     				if (mapDias.get(diaSemana-1) != null && banderaFeriados==false){
  		   			   	     					banderaMatriz=true;
  		   			   	     					//Horario de acuerdo a los turnos ingrresados por talento humano
  		   			   	     					 horaInicioEmpl = horariosEmpleado.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
  		   			   	     					horaFinEmpl = horariosEmpleado.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
  		   			   	     					horaIniAlmEmpl = horariosEmpleado.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
  		   			   	     					horaFinAlmEmpl = horariosEmpleado.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
  		   			   	     					
  		   			   	     					
  		   			   	     					
  		   			   	     					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
  		   			   	         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
  		   			   	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
  		   			   	         			    //Suma de minuto para el ingreso
  		   			   	         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
  		   			   	     					
  		   			   	     					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
  		   			   	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  		   			   	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
  		   			   	         			    
  		   			   	         			    
  		   			   	         			    String fechaHoraHorarioInicial = fechaBiometrico+" "+horaFinAlmEmpl;//cogo la hora y le concateno con la fecha del horario
  		   			   	         			    Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
  		   			   	         			    calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicial));
  		   			   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    


  		   			   	         			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
  		   			   	         			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
  		   			   	         			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
  		   			   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, -15);	    

  		   			   	         			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
  		   			   	         			   Date finAlmuerzo=null;
  		   			   	         			    
  		   			   	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
  		   			   	         		
  		   			   	         			   
  		   			   	         			   Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
  		   			   	         			if (tiempoAlmuerzoHorario==0.5) {
  		   			   	         	
  			   	        			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
  			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 30);
											}else if (tiempoAlmuerzoHorario==1) {
												
		   			   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
		   			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
											}
  		   			   	         		
	   			    		
  		   			   	         			   
  		   			   	         			   
  		   			   	         			   
  		   			   	         			   
  		   			   	         			   
  		   			   	         			   
  		   			   	         		//	  if ((int)tiempoAlmuerzo==1) {
  		   			   	           	//		    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
  		   			   				//		}else {
  		   			   	           	//		    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR")));
  		   			   				//		}
  		   			   	     					
  		   			   	         			    //Sumamos una hora para el almuerzo
  		   			   	         			   
  		   			   	        
		
  		   			    		
  		   			    	
  		  	             			    
  		  	             			    
  		  	             			    
  		  	             			    //Hora desde Inicio de Entrada de Horario
  		  	             			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio),-3));
  		 	           	    		     Calendar calHoraFechaBase= Calendar.getInstance();
  		 	           	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporteBase));
  		 	         			
  		   			    		
  		   					
		  	   	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

  		   					
  		   				  //  if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
  		       			    	if (banderaEntrada==0) {
  		   							
  		   						
  		                          //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  		       			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
  		       			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		   			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ (pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
		       			
  		       			    
  		       			    	//Calculo de tiempo de atraso en la entrada del empleado
  		       			    	tipo1=1;
  		       			    	tipo2=0;
  		       			    	banderaEntrada++;
  		       			    	}
  		       			    }
  		    
  		      				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) >= 0
  		   							&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
  		      					if (banderaEntrada==0) {
  		       			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
  		       			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		   			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	       			
  		       			    	tipo2=1;
  		       			    	tipo1=0;
  		       			    	banderaEntrada++;
  		       			    	}
  		       				}

  		       			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
  		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo==0){
  		       			    	//Almuerzo
  		       			    	
  		       			    	if (banderaAlmuerzo == 0){
  		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
  		           			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		           			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
  		       			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
  	   		      					horaAnterior=horaBiometrico;
  		           			    	banderaAlmuerzo++;
  		           			    	tipo3=1;
  		           			  
  		       			    	}
  		       			    	
  		       			    	
  		       			    	
  		       			    	
  		       			    }else if (banderaAlmuerzo==1 && horaBiometrico.compareTo(horaAnterior)>0  && horaBiometrico.compareTo(horaFinEmpl)<0 && banderaAlmuerzoEntrada==0){
  		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

		
			   	     			String uno= fechaBiometrico+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
			   	         		String dos = fechaBiometrico+" "+horaFinAlmEmpl;
			   	     			
			   	     				Calendar caluno = Calendar.getInstance();//
			   	         			Calendar caldos = Calendar.getInstance();//
			   	         			caluno.setTime(getFechaAsyyyyMMddHHmmss(dos));//
			   	         			caldos.setTime(getFechaAsyyyyMMddHHmmss(uno));//
			   	         			//Suma de minuto para el ingreso
			   	         			
			   	         			if((pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==1){
			   	         			caluno.add(Calendar.MINUTE, 60);	
			   	         			}else if ((pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==0.5) {
			   	         			caluno.add(Calendar.MINUTE, 30);	
									}else {
										
									}
			   	         			
			   	         				
  		       			    	
  		       			    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
  	   		       			    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0 && banderaAlmuerzo==1){
  	   		       			   	 
  		       			    	    tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		           			    	
  		   							tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
  		           			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
  		           			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
  		           			    	tiempoAlmuerzo = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
  		           			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
  		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
  	   		      					horaAnteriorAlm=horaBiometrico;

  		           			    	//Validacion si se pasa el almuerzo
  		           			    	tipo4=1;
  		           			    	banderaAlmuerzoEntrada++;
  		           			    	horaAnterior="";
  		       			    		}
  		       			    	}
  		           			   	

  		       			      //JUAN CARLOS AYERBE
  		       			    
  		       			    
  		       			
  		       			    
  		       			 /*   else if (banderaAlmuerzoEntrada==1 && banderaAlmuerzo==1){
  		       			    	//Salida
  		       			    	

  		       				Calendar calAlm = Calendar.getInstance();//
  		       				calAlm.setTime(getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+horaAnteriorAlm));//
  		       			   if (calFechaHoraBiometrico.compareTo(calAlm) > 0
 		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0 ){
  		       		
  		       			   
  		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
  		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
  		       			
  		           			   	tipo5=1;
  		           			   	tipo6=0;
  		       			    	
  		       			    }  	
  		       			    }*/
  		   				    
  		       			  else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
		       			    	//Salida
		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		       			
		           			   	tipo5=1;
		           			   	tipo6=0;
		       			    	
		       			    }  
  		   				    
  		   				    
  		   				
  		       			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
  		                        //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

  		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
  		       			    	
  		       			    	tipo6=1;
  		       			    	tipo5=0;
  		       			    }    
  		   					
  		   				    
  		   			   	     		
  		   			   	     				
  		   			   	     				}//Fin de ciclo si el dia de la semana en el horario no concuerda 
  		   			   	     						   	     				
  		   			   	     				else
  		   			   	     				{//si es dia normal y es feriado

  		   			   	     					
  		   			   	     					
  		   			   	     				if (tipo1!=0 || tipo2!=0 || tipo3!=0  || tipo4!=0 || tipo5!=0  || tipo6!=0 || banderaFeriados==false && mapDias.get(diaSemana-1) == null ) {
  		   			   	     			extraMatriz++;
  		   			   	     					break;	
  		   			   							}else {
  		   			   						banderaMatriz=true;
  		   			   								String mensaje="";
  		   			   	     					
  		   			   	     					if(banderaFeriados==true){
  		   			   	     						mensaje="FERIADO";
  		   			   	     					}else {
  		   			   	     						mensaje="EXTRA";

												}
  		   			   	     					
  		   										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
  		   						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
  		   						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

  		   						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
  		   						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
  		   						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
  		   											
  		   										
  		   						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
  		   						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
  		   						                	 if (banderaHoraExtra1001==0) {
  		   						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  		   						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  		   						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  		   						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", mensaje);
  		   									    		banderaHoraExtra1001=1;
  		   									   	    }else {
  		   											        			    
  		   									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  		   						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", mensaje);
  		   						 			    	
  		   						 			    	banderaHoraExtra1001=2;
  		   						 				
  		   										
  		   										        				
  		   						 				}
  		   									
  		   									}
  		   			   							}//Else si no se ha insertado datos anteriormente
  		   			   	     				
  		   			   	     				}//Fin de else de tipo de permiso
  		   			   	     				
  		   			   	     				
  		   			   	     				
  		   			   	     			}//Biometrico
  		   			   	     			
  		   			   	     			
  		   			   	    		}else 
  		   			   	    		//este si no tiene marcaciones realizadas en el biometrico
  		   			   	    		{
  		   			   	    			
  		   			   	    		
  		   			   	        		if (horariosEmpleado.getTotalFilas()>0) {
  		   			      	     			//Si  contiene horario pero no timbrada
  		   				      	  	    	
  		   		        			  /*  	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", "");
  		   		        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
  		   		    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "SIN MARCACIÓN");
  		   		        			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
  		   		    			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
  		   		        			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
  		   		        			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", "");
  		   		      			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
  		   		      			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN MARCACIÓN");
  		   		      			    	*/	
  		   			      	     			banderaSinAccion=true;

  		   							}
  		   			      	     		else {
  		   			      	     			banderaSinAccion=true;
  		   								}
  		   			      	    
  		   			      	     		
  		   			      	     		
  		   							}
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		}
  		   			   	    	
  		   			   	    			
  		   			   	    	////////////////////////////////////////////////////////////////////
  		   			   	    			
  		   			   	    			
  		   			   	   	if (extraMatriz>1 ) {
			   							
			   								
			   					
			   	     				List<Fila> biometrico;		
			   	       			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

	   			   	     			if (biometrico.size()!=0 ) 
	   			   	     			//Si es contiene marcaciones en le biomtrico
	   			   	     			{
	   									
	   								

	   			   	     			int banderaAlmuerzo=0;
	   			   	     			int banderaAlmuerzoEntrada=0;
	   			   	     			int banderaEntrada=0;
	   			   	     			//recorro todos las timbradas
	   			   	     			for (Fila bio : biometrico) {
			   	     					
										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
						 			    
						 			    fechaBiometrico = (String)bio.getCampos()[1];
						 			    //Hora de la tabla biometrico
						 			    String horaBiometrico = (String)bio.getCampos()[2];
						 			    //Evento de acuerdo a si es entrada o almuerzo
						 			    //	String evento = (String)bio.getCampos()[3];
						 			    //Unimos la fecha con la hora
						 			    String fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
						 			    //Calendario 
						 			    Calendar calFechaHoraBiometrico = Calendar.getInstance();
						 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
						 			    calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
		            				
									
										
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				       				
						 				}
									
									}
 			   	    			
 			   	    			
	   			   	     			}
 			   	    			
	   			   	     			}
 			   	    		
						

  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    			
  		   			   	    	//////////////////////////////////////////////////////////77		
										
									}
  		   			   	    		
  		   			   	    		
  		   			   	    		//Horarios del Empleado
  		   					
  		   					
  		   			   	    		

  		   							
  		   						double almuerzo1=0.0;
  		    					String horarioEntrada="";
  		    					String horarioSalida="";
  		    					boolean bandera=false,banderaEntrada=false;
  		    					int estadosAlmuerzo=0;
  		    	  								
  		    					//Si no hay datos le pongo =""
  		    						if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("")) {
  		    							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") || tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
  		    								almuerzo1=0;
  		    								estadosAlmuerzo=1;
  		  							}else {
  		  								almuerzo1=0;
  		  							estadosAlmuerzo=0;
  		  							}
  		    							
  		    			
  		    					}else {
  		    						if(tab_reporte.getValor(itemReporte, "TIEMPOALM")== null || tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()
  		    								|| tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("0")){
  		    							almuerzo=0;
  		    							estadosAlmuerzo=0;
  		    						}else {
  		    							
  	  		    						almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));
  	  		    					estadosAlmuerzo=1;									
									}


  		  					}
  		    					

  		    					
  		    					 
  		    					
  		    				//	if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
  		    				//	bandera=true;
  		    				//}else {
  		    				//	bandera=false;
  		    				//}
  		    	        		
  		    			//		if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("EXTRA") ) {
  		    	  		//			bandera=true;
  		    	  		//		}else {
  		    	  		//			bandera=false;
  		    	  		//		}	
  		    					
  		    					
  		    					
  		    				//	if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("FERIADO") ) {
  		    	  			//		bandera=true;
  		    	  			//	}else {
  		    	  			//		bandera=false;
  		    	  			//	}
  		    					
  		   							
  		   							
  		   						//	if (bandera==false) {
										
									
  		   						//if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("ATRASADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
  		   					//		bandera=true;
  		   					//	}else {
  		   				//			bandera=false;
  		   				//		}
  		   					//		}
  		   							
  		   							
  		   							
  		   						bandera=false;	
  		   							
  		   							
  		   							
  		   							
  		   							
  		   							
  		   			        		
  		   							if (tipo1!=0 || tipo2!=0 || tipo3!=0 || tipo4!=0 || tipo5!=0 || tipo6!=0 && banderaSinAccion==false) {
  		   			        	
  		   			    	        insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
  		   			 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
  		   			 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
  		   			 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
  		   			 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
  		   							}else if (banderaHoraExtra1001!=0 && banderaSinAccion==false) {
  		   								
  		   							
  		   								   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
  		   					 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
  		   					 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
  		   					 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
  		   					 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
  		   								
  		   							} else if (banderaSinAccion==true) {
  		   									
  		   								
  		   							}
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   			   	    		
  		   					
  		   				//System.out.println("Programacion Anual"+empleado);

  		       	
  		       		} 
       		     
       		     
       		     
       		     
       		     
   //   si no tiene asignado la matriz mensual opertivo  ni  tampoco el turno sucursal matriz 
   		   		
  				
  				
  				
  				
  				
  				
  				
  				
  				
  				
  				
  				else if (tabEmpleadoMatrizMensual.getTotalFilas()==0 && tabEmpleado.getTotalFilas()==0) {
   		   					
   		   		 			//Verifico si es dia feriado    
   		   	     			     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
   		   	     	            //Consulto marcaciones 	
   		      	    			List<Fila> biometrico;
   		     	    			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
   		     	    		   
   		     	    			 
   		     	    			 int 	banderaHoraExtra1001=0;
   		     	    			if (biometrico.size()!=0 ) {
   		     	    				// Si tiene marcaciones 
   		     	    				
   		     	    				
   		   						//si tiene marxcaciones y es feriado
   		     	    			 
   		     	    			boolean bandEntradaSinAlmuerzo=false;
   		            			
   		            			int banderaAlmuerzo=0;
   		            			int banderaAlmuerzoEntrada=0;
   		            			int banderaEntrada=0;
   		            			//recorro todos las timbradas
   		            			for (Fila bio : biometrico) {
   		            				//fecha de la tabla biometrico
   		            				fechaBiometrico = (String)bio.getCampos()[1];
   		            				//Hora de la tabla biometrico
   		            				String horaBiometrico = (String)bio.getCampos()[2];
   		            				//Evento de acuerdo a si es entrada o almuerzo
   		            			//	String evento = (String)bio.getCampos()[3];
   		            				//Unimos la fecha con la hora
   		            				String fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
   		            			    //Calendario 
   		            				Calendar calFechaHoraBiometrico = Calendar.getInstance();
   		            			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
   		            				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
   		            				
   		            			
   		            			
   		            				 horaInicioEmpl ="";
   		        					 horaFinEmpl = "";
   		        					 horaIniAlmEmpl ="";
   		        					 horaFinAlmEmpl = "";
   		        			
   		            				boolean t=false;
   		            				
   		            				//si no contiene turno
   		            				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
   		            			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
   		            			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

   		            			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
   		            			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
   		            			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
   		            			
   		   						
   		            			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
   		            			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
   		                           	 if (banderaHoraExtra1001==0) {
   		            			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
   		            			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
   		            			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
   		    	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 

   		        			    		banderaHoraExtra1001=1;
   		        			   	    }else {
   		   							        			    
   		           			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
   		            			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
   		            			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
   		            			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
   		            			    	
   		            			    	banderaHoraExtra1001=2;
   		   	     				
   		        				
   		   						        				
   		            				}
   		            			   
   		            			    	 
   		            			    	 
   		            			    }
   		            			    
   		            			}//Fin de timbradas en biometrico 
   		            			
   		            			
   		            			
   		            			if (biometrico.size()!=0 ){
   		    					   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
   		    		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
   		    		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
   		    		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
   		    		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico));         			
   		             			
   		             			}
   		            			}else {
   		   					//Si no tiene marcaciones 	
   		            			   insertarTablaResumen(empleado, fechaBiometrico, "", "","SIN HORARIO", "", "",
   		   		 		   "", "",0,"",
   		   		 		    "","","SIN HORARIO",false,diaSemana(fechaBiometrico));         			
   		            			
   		            			
   		            			}
   		     	    			//si tiene marcaciones
   		     	    	
   		     	    			
   		            			
   		           	// sin tiene marcacion en el biometrico inserta sino no hace nada
   		            	
   		            			
   		   					
   		   				}
   		              		 
   				
   				
   				
   				
   				
   				
   				
   				       }

    	
    	
    	
    	
    	
    	
    	
    	
    	//Validacion de horas extra por empleado
    	
    	if (!empleadoSeleccionado.equals("") || !empleadoSeleccionado.isEmpty() || empleadoSeleccionado!=null ) {
			
		
    	
    	
    		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' and ide_gtemp in("+empleadoSeleccionado+") ");
   			tab_resumen_marcaciones.ejecutarSql();
   			
   		      			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "recargonocturno25_cobmr=null, recargonocturno100_cobmr=null, horafinextra_cobmr=null, novedad_cobmr=false "
						+ "where fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and ide_gtemp in("+empleadoSeleccionado+") ");
				
				
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%OK%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");

						
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%ATRASADO%' and horafinband_cobmr like '%OK%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");


				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%ANTICIPADO%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");



				
				
				///EXTRA Y FERIADO
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%EXTRA%' and horafinband_cobmr like '%EXTRA%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");

						
				
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%FERIADO%' and horafinband_cobmr like '%FERIADO%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");
   			/**
   			 * VALIDACION ALMUERZO
   			 */
	    		tab_resumen_marcaciones.setCondicion("IDE_COBMR=-1");
	   			tab_resumen_marcaciones.ejecutarSql();
	   			utilitario.addUpdate("tab_resumen_marcaciones");
	   			utilitario.agregarMensajeInfo("Se han reprocesado las marcaciones",	"Empleado(s) incluido(s) satisfactoriamente");

    	}
    	
    	
    	
    	
    	
    	
    	
    	
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
    
    private Map<Integer, Boolean> obtenerDiasByHorario(TablaGenerica diasXHorario, Integer ide_horario){

    	Map<Integer, Boolean> mapDias = new HashMap<Integer, Boolean>();
    	for (int i=0 ; i<diasXHorario.getTotalFilas();i++) {
			if (ide_horario.equals(Integer.parseInt(diasXHorario.getValor(i, "IDE_ASHOR")))){
				mapDias.put(Integer.parseInt(diasXHorario.getValor(i, "IDE_GEDIA")), Boolean.TRUE);
			}
		}
    	return mapDias;
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

    private List<Fila> obtenerTimbreBiometrico(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
    	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
    	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
    	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
    				+ "EMP.IDE_GTEMP,"
        			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
        			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
        			+ "from gth_empleado emp  "
        			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
        			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
        			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
        			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");    	
    	//tabObteberTimbreXEmpleado.imprimirSql();
    	return tabObteberTimbreXEmpleado.getFilas();
    	
    }
    //Nocturno

    private List<Fila> obtenerTimbreBiometricoNocturno(Integer IDE_GTEM,String FECHA_INICIAL,String FECHA_INICIAL1, String FECHA_FINAL, String FECHA_FINAL1){
   	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select IDE_COBIM, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') as HORA  "
    			+ "from "
    			+ "con_biometrico_marcaciones as bio  "
    			+ "left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp "
    			+ "where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"'  "
    			+ "and emp.ide_gtemp="+IDE_GTEM 
    			//+ " order by  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc ");
    			+ " order by bio.ide_persona_cobim");
    //	tabObteberTimbreXEmpleado.imprimirSql();
    	return tabObteberTimbreXEmpleado.getFilas();

    	
    	
    	
    	
    	
    	
    	
    	
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
    

    
    
    private TablaGenerica obtenerHorariosEmpleadoMes(Integer ide_ashor){
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select  horario.ide_ashor,  "
    			+ "HORARIO.HORA_INICIAL_ASHOR, "
    			+ "HORARIO.HORA_FINAL_ASHOR, "
    			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
    			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
    			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
    			+ "HORARIO.NOM_ASHOR  "
    			+ "from asi_horario HORARIO "
    			+ "where  HORARIO.IDE_ASHOR="+ide_ashor
    			+ " ORDER BY HORARIO.IDE_ASHOR ASC");
    	return horarioXEmpleado;
    }
    
    
    
    
    private TablaGenerica obtenerHorariosTurnoEmpleado(Integer ide_ashor){
    	
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select hor.ide_astur,turno.ide_gtgre from asi_turnos_horario hor "
    			+ "left join asi_turnos turno on turno.ide_astur=hor.ide_astur "
    			+ " where hor.ide_ashor="+ide_ashor);
    	return horarioXEmpleado;
    	
    }
    
    
   private TablaGenerica obtenerHorariosTurnoEmpleadoTolerancia(Integer ide_astur){
    	
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select ide_astur,minuto_tolerancia_astur from asi_turnos "
    			+ " where ide_astur="+ide_astur);
    	return horarioXEmpleado;
    	
    }
    
  
	/**
	 * Obtiene los horarios de cada turno
	 * @return  Tabla Generica
	 */
    private TablaGenerica obtenerHorariosPorMes(int ide_gtemp, int ide_gemes, int ide_geani){
    	
    	TablaGenerica obtenerHorarios = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gemes="+ide_gemes+ " "
    			+ "and ide_geani="+ide_geani+" and ide_gtemp="+ide_gtemp);
    	//obtenerHorarios.imprimirSql();
    	return obtenerHorarios;
    	
    }
    
    
    /**
     * 
     * @return
     */
    private TablaGenerica obtenerTurnos(int ide_astur){
    	
    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario where ide_astur="+ide_astur);
    	return obtenerHorarios;
    	    	
    }
    
    
   // Devuelve true si  tiene asignado el dia festivo
    private Boolean getFeriado(String fecha){
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov from asi_novedad where dia_feriado_asnov=true");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
 				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				valorRetorno= true;	
				i=tab_novedad.getTotalFilas();
			}else {
				valorRetorno= false;
			}
 				
 				
		}
    	
    	return valorRetorno;

    	
    	
    }
    
    
    

    // Devuelve true si  tiene asignado el dia festivo
    private Boolean getEmpleadoActivo(int empleado){
    	boolean valorRetorno=false;
   	TablaGenerica tab_novedad=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where activo_geedp=true "
   			+ "and ide_gtemp="+empleado);
    				
   	if (tab_novedad.getTotalFilas()>0) {
   	 	valorRetorno= true;	
	}else {
	 	valorRetorno= false;
	}
  
		return valorRetorno;

    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //Ver si tiene permiso online para designacion y 
    
    //Obtener el turno de tipo dia fetivo o asignacion y   devuelve true y false
    
    private Boolean getCambioHorario(String fecha){
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,ide_astur from asi_novedad where cambio_asnov=true");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
			if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				valorRetorno= true;
				
			}else {
				valorRetorno= false;
			}
		}
    	
    	return valorRetorno;

    	
    	
    }
    
  //Obtener el turno de tipo dia fetivo o asignacion y   devuelve true y false
    
    private int getTurnoDesignadoFeriado(String fecha){

    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	int ide_astur=0; 
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,ide_astur from asi_novedad where dia_feriado_asnov=true");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
			if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				ide_astur=Integer.parseInt(tab_novedad.getValor(i, "ide_astur"));
			}else {
			ide_astur=0;	
			}
		}
    	
    	return ide_astur;

    	
    	
    }
    
    
  //Obtener el turno de tipo dia fetivo o asignacion y   devuelve true y false
    
    private int getTurnoDesignadoCambioHorario(String fecha){

    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	int ide_astur=0; 
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,ide_astur from asi_novedad where cambio_asnov=true");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
			if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				ide_astur=Integer.parseInt(tab_novedad.getValor(i, "ide_astur"));
			}else {
			ide_astur=0;	
			}
		}
    	
    	return ide_astur;

    	
    	
    }
    
    
    

    
    
    
    //Ver si tiene permiso online para designacion y 
    
    //Obtener el turno de tipo dia fetivo o asignacion y   devuelve true y false
    
    private Boolean getCambioHorarioPermisoOnline(String fecha){
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,fecha_desde_aspvh,fecha_hasta_aspvh,ide_astur  "
    			+ "from asi_permisos_vacacion_hext where order by ide_aspvh desc");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
			if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				valorRetorno= true;
				
			}else {
				valorRetorno= false;
			}
		}
    	
    	return valorRetorno;

    	
    	
    }
    
  //Obtener el turno de tipo dia fetivo o asignacion y   devuelve true y false
    
    private int getTurnoDesignadoPermisoOnline(String fecha){

    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	int ide_astur=0; 
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
		Calendar calFechaInicio = Calendar.getInstance();
		Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene las novedades
	 	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,fecha_desde_aspvh,fecha_hasta_aspvh,ide_astur  "
    			+ "from asi_permisos_vacacion_hext where order by ide_aspvh desc");
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
			fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
			fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");
			
			

 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
 				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
 				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
 				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
			if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
				ide_astur=Integer.parseInt(tab_novedad.getValor(i, "ide_astur"));
			}else {
			ide_astur=0;	
			}
		}
    	
    	return ide_astur;

    	
    	
    }

    
        
    
    //vER METODODO
    private TablaGenerica obtenerCambioHorario(String fecha){
    	
    	TablaGenerica obtenerHorarios =utilitario.consultar ("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad "
    			+ "where dia_feriado_asnov=true AND FECHA_INICIO_ASNOV>='"+fecha+"' AND FECHA_FIN_ASNOV<='"+fecha+"'");
    	return obtenerHorarios;
    	
    }
    
    
    
  public TablaGenerica  obtenerPermisoXEmpleado(String FechaInicio, String FechaFin,String horaInicio, String horaFin,  Integer IDE_GTEMP){
            TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
            + "from asi_permisos_vacacion_hext where "
	  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaFin+"' and ide_gtemp="+IDE_GTEMP+" "
	  	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"'");
	  
	  //obtenerPermisos.imprimirSql();
	  return obtenerPermisos;
  }
  
  
  ///Si tiene permiso online con cambio de horario
    
  
  
    
  
  
  
  
  
  
  
  
  
  
    
    @Override
    public void insertar() {
    //	TablaGenerica tab_novedadInsertar=utilitario.consultar("select * from asi_");
    	
    	
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
        	
                tab_novedad.insertar();
            	tab_novedad.setLectura(false);
            	tab_novedad.setValor("FECHA_INICIO_ASNOV",fechaIni);
            	tab_novedad.setValor("FECHA_FIN_ASNOV",fechaFin);
				
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

			}
    		
		}
		else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
		}		
    	


    	
    }

    @Override
    public void guardar() {
        if (tab_novedad.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_novedad.eliminar();
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
 	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    

 	public void limpiar(){
 		//aut_empleados.limpiar();
	
 		tab_marcaciones.limpiar();
 		tab_resumen_marcaciones.limpiar();
 		utilitario.addUpdate("tab_marcaciones,tab_resumen_marcaciones");
 	}
 	
 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}




 	
 	
public Tabla getTab_novedad() {
		return tab_novedad;
	}



	public void setTab_novedad(Tabla tab_novedad) {
		this.tab_novedad = tab_novedad;
	}



	//Sumar horas en fecha
	 public Date sumarRestarHorasFecha(Date fecha, int horas){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(fecha); // Configuramos la fecha que se recibe
     calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
		  }
	 
	 
	//Sumar horas en fecha
		 public Date sumarRestarMinutosFecha(Date fecha, int horas){
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(fecha); // Configuramos la fecha que se recibe
	     calendar.add(Calendar.MINUTE, horas);  // numero de horas a añadir, o restar en caso de horas<0
	     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
			  }
 	

	 public void insertarTablaResumen(
			 int ide_gtemp ,
			 String fecha_evento_cobmr,
			 String horainiciohorario_cobmr,
			 String horainiciobiometrico_cobmr,
			 String horainicioband_cobmr,
			 String horainicioalm_cobmr,
			 String horafinalm_cobmr,
			 String horainicioalmbio_cobmr,
			 String horafinalmbio_cobmr,
			 double tiempoalm_cobmr,
			 String tiempohoralm_cobmr,
			 String horafinhorario_cobmr,
			 String horafinbiometrico_cobmr,
			 String horafinband_cobmr,
			 boolean novedad_cobmr,
			 String dia_cobmr
			 ){
	
		 
			TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("con_biometrico_marcaciones_resumen", "ide_cobmr"));
			String codigo=tab_codigo.getValor("codigo");
			utilitario.getConexion().ejecutarSql("INSERT INTO con_biometrico_marcaciones_resumen(" 
						+ "ide_cobmr, "
						+ "ide_gtemp, "
				  		+ "fecha_evento_cobmr, "
				  		+ "horainiciohorario_cobmr, "
				  		+ "horainiciobiometrico_cobmr, "
				  		+ "horainicioband_cobmr, "
				  		+ "horainicioalm_cobmr, "
				  		+ "horafinalm_cobmr, "
				  		+ "horainicioalmbio_cobmr, "
				  		+ "horafinalmbio_cobmr, "
				  		+ "tiempoalm_cobmr, "
				  		+ "tiempohoralm_cobmr, "
				  		+ "horafinhorario_cobmr, "
				  		+ "horafinbiometrico_cobmr, "
				  		+ "horafinband_cobmr,"
				  		+ "novedad_cobmr," 
				  		+ "dia_cobmr)" + 

				  		" values( " +codigo + ", "
				  		+ ""+ ide_gtemp+", "
				  		+ "'"+fecha_evento_cobmr+"', "
				  		+ "'"+horainiciohorario_cobmr+"', "
				  		+ "'"+horainiciobiometrico_cobmr+"', "
				  		+ "'"+horainicioband_cobmr+"', "
				  		+ "'"+horainicioalm_cobmr+"', "
				  		+ "'"+horafinalm_cobmr+"', "
				  		+ "'"+horainicioalmbio_cobmr+"', "
				  		+ "'"+horafinalmbio_cobmr+"', "
				  		+ " '"+tiempoalm_cobmr+"', "
				  		+ "'"+tiempohoralm_cobmr+"', "
				  		+ "'"+horafinhorario_cobmr+"', "
				  		+ "'"+horafinbiometrico_cobmr+"', "
				  		+ "'"+horafinband_cobmr+"', "
				  		+ ""+novedad_cobmr+", "
				  		+ "'"+dia_cobmr+"')"); 
		 
	 }
	
	 
	 
	 
	 public TablaGenerica getTablaReporte(String IDE_GTEMP, String strFechaIniReporte, String strFechaFinReporte ){
	
		 TablaGenerica tab_reporte;
		 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
		        tab_reporte= utilitario.consultar(
		    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
		     	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') , "		
		    	+ "'' AS HORAINICIOHORARIO, "
		    	+ "'' AS HORAINICIOBIOMETRICO, "
		    	+ "'' AS HORAINICIOBAND, "		
		    	+ "'' AS HORAINICIOALM, "
		    	+ "'' AS HORAFINALM, "
		    	+ "'' AS HORAINICIOALMBIO, "		
		    	+ "'' AS HORAFINALMBIO, "		
		    	+ "'' AS TIEMPOALM, "
		    	+ "'' AS TIEMPOHORALM, "
		    	+ "'' AS HORAFINHORARIO, "
		    	+ "'' AS HORAFINBIOMETRICO, "
		    	+ "'' AS HORAFINBAND "		
				+ "from gth_empleado emp "
				+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
				+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD')  "
				+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC");			

	}else{
	
		
		
		tab_reporte= utilitario.consultar(
		    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
		     	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') , "		
		    	+ "'' AS HORAINICIOHORARIO, "
		    	+ "'' AS HORAINICIOBIOMETRICO, "
		    	+ "'' AS HORAINICIOBAND, "		
		    	+ "'' AS HORAINICIOALM, "
		    	+ "'' AS HORAFINALM, "
		    	+ "'' AS HORAINICIOALMBIO, "		
		    	+ "'' AS HORAFINALMBIO, "		
		    	+ "'' AS TIEMPOALM, "
		    	+ "'' AS TIEMPOHORALM, "
		    	+ "'' AS HORAFINHORARIO, "
		    	+ "'' AS HORAFINBIOMETRICO, "
		    	+ "'' AS HORAFINBAND "		
				+ "from  gth_empleado emp  "
				+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
				+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				+ "and emp.ide_gtemp IN("+IDE_GTEMP+") "
				+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD')  "
				+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC");			
					
	}
				//tab_reporte.imprimirSql();
		 return tab_reporte;
		 
	 }
	 
	 
	 public void insertarTablaNovedad(
			  int ide_usua, 
			  String fecha_inicio_asnov,
			  String fecha_fin_asnov,
			  String observacion_asnov ,
			  boolean activo_asnov, 
			  String fecha_asnov,
			  boolean importacion_asnov,
			  boolean dia_feriado_asnov){
			 TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_novedad", "ide_asnov"));
			String codigo=tab_codigo.getValor("codigo");
			utilitario.getConexion().ejecutarSql("INSERT INTO asi_novedad(" 
						+ "ide_asnov, "
						+ "ide_usua, "
				  		+ "fecha_inicio_asnov, "
				  		+ "fecha_fin_asnov, "
				  		+ "observacion_asnov, "
				  		+ "activo_asnov, "
				  		+ "fecha_asnov, "
				  		+ "importacion_asnov,"
				  		+ "dia_feriado_asnov)" + 
				  		" values( " +codigo + ", "
				  		+ ""+ ide_usua+", "
				  		+ "'"+fecha_inicio_asnov+"', "
				  		+ "'"+fecha_fin_asnov+"', "
				  		+ "'"+observacion_asnov+"', "
				  		+ "'"+activo_asnov+"', "
				  		+ "'"+fecha_asnov+"', "
				  		+ ""+importacion_asnov+" , "
				  		+ ""+dia_feriado_asnov+" )"); 
 
	 }




	


	public String diaSemana (String fecha)
	    {
		 
		 int dia=utilitario.getDia(fecha);
		 int mes=utilitario.getMes(fecha);
		 int anioEscogido=utilitario.getAnio(fecha);
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
	 
	 
	 
	 
	 
//	 public void procesar(String fechaBiometricoAgrupadaXEmpleado){
//		 
//		 
//		 
//		 
//
// 		String fechaBiometrico="";
// 		
// 		//Integer empleado = Integer.parseInt(tab_reporte.getValor(itemReporte, "IDE_GTEMP"));
// 		Integer empleado = 508;
// 		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
//			//Obtengo los horarios de cada empleado de acuerdo a su ide_gtemp
// 		//TablaGenerica horariosEmpleado = this.obtenerHorariosEmpleado(empleado);
//
// 		
// 		/**
// 		 * 
// 		 */
// 		
// 		//Veo si dias es feriado
// 		
// 		/*
// 		 * Obtengo la fecha
// 		 */
// 		
// 		int diaMes=utilitario.getDia(fechaBiometricoAgrupadaXEmpleado);
// 		
// 		
// 		/*       
// 		 *  Obtengo la planificacion por empleado mensual
// 		 */
// 		TablaGenerica tabHorarioXEmpleadoMes=this.obtenerHorariosPorMes(empleado,ide_gemes,ide_geani);
// 		/*
// 		 * Obtengo el dia con su horario
// 		 */
// 	
// 		int horarioDiaMes=pckUtilidades.CConversion.CInt(tabHorarioXEmpleadoMes.getValor("DIA"+diaMes));
//         String diaLibre="";
// 		if (pckUtilidades.CConversion.CStr(horarioDiaMes).isEmpty() || pckUtilidades.CConversion.CStr(horarioDiaMes)==null ||  pckUtilidades.CConversion.CStr(horarioDiaMes).equals("") ) {
// 			horarioDiaMes=0;
//			}
// 		
//		
// 		/*
// 		 * oBTENGO EL DIA PARA LUEGO BUSCAR EL HORARIO
// 		 */
// 		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(horarioDiaMes);
//
// 		//para turno nocturno
// 	//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
// 	//	String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
// 	//	String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
// 		//TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
// 	//	String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
// 		
// 		int banderaHoraExtra1001=0;
//			    banderaFeriados=false;
//
// 		Integer tipo1= 0;
//		    Integer tipo2 = 0;
//		    Integer tipo3 = 0;
//		    Integer tipo4= 0;
//		    Integer tipo5 = 0;
//		    Integer tipo6 = 0;
//		    Integer tipo7 = 0;
//			Integer tipo1SinAlmuerzo= 0;
//		    Integer tipo2SinAlmuerzo = 0;
//		    Integer tipo3SinAlmuerzo = 0;
//		    Integer tipo4SinAlmuerzo= 0;
//		    String  SINDATO="";
//	    
//		    int forHorarios=0;
//		    forHorarios = horariosEmpleadoMes.getTotalFilas();
//		    
//		    if (horarioDiaMes==0 && forHorarios==0) {
//		    	forHorarios=1;
//			}
// 		//Recorro los horarios 
// 		for (int i=0; i < forHorarios; i++) {
// 				List<Fila> biometrico;
//     			 String horaBiometrico="";
//     			//variable para calculo de tiempo a justificar
//     			 String fechaHoraBiometrico;
//     			double tiempoAlmuerzo=0.0;
// 		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
// 		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
//      			double tiempoDiferenciaEntradaEmpleado=0.0;
//      			double tiempoDiferenciaSinEntradaEmpleado=0.0;
//
//      			
//  				//Para creacion de inconvenientes
//  				String entradaSinJustificacion="";
//      			String almuerzoSinEntradaJustificacion="";
//      			String salidaSinJustificacion="";
//
//  			 	String marcacion1="";
//  			 	String marcacion="";
//
//  			    banderaHoraExtra1001=0;
//      			         			    
//  			    
//  			     TablaGenerica tabDiasFeriados=obtenerDiasFeriados(fechaBiometricoAgrupadaXEmpleado);
//  	            
//  	            if (tabDiasFeriados.getTotalFilas()>0) {
//  					banderaFeriados=true;
//  				}
// 			//Obtengo los timbres realizados por un empleado 
//	           biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
//
//
//  			int banderaAlmuerzo=0;
//  			int banderaAlmuerzoEntrada=0;
//  			int banderaEntrada=0;
//  			//recorro todos las timbradas
//  			for (Fila bio : biometrico) {
//  				//fecha de la tabla biometrico
//  				fechaBiometrico = (String)bio.getCampos()[1];
//  				//Hora de la tabla biometrico
//  				horaBiometrico = (String)bio.getCampos()[2];
//  				//Evento de acuerdo a si es entrada o almuerzo
//  			//	String evento = (String)bio.getCampos()[3];
//  				//Unimos la fecha con la hora
//  				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
//  			    //Calendario 
//  				Calendar calFechaHoraBiometrico = Calendar.getInstance();
//  			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
//  				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
//  				
//  				//Obtengo el dia de la semana a que corresponde esa fecha
//  				//int diaSemana = calFechaHoraBiometrico.get(Calendar.DAY_OF_WEEK);
//  			    //Validacion para el dia domingo=0
//  				//if (diaSemana==1) {
//					//	diaSemana=8;
//					//}
//  			
//  				String horaInicioEmpl ="";
//					String horaFinEmpl = "";
//					String horaIniAlmEmpl ="";
//					String horaFinAlmEmpl = "";
//			
//  				boolean t=false;
//  				if (horarioDiaMes!=0){
//  					//Horario de acuerdo a los turnos ingrresados por talento humano
//  					
//  					
//  				    horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
//  					horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
//  					horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
//  					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
//  					
//  					
//  					
//  					
//  					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
//      			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
//      			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
//  					
//  					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
//      			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
//      			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
//
//      			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
//      			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
//      			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
//  					
//      			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
//      			   Date finAlmuerzo=null;
//      			    
//      			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
//      			  if ((int)tiempoAlmuerzo==1) {
//        			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
//					}else {
//        			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
//					}
//  					
//      			    //Sumamos una hora para el almuerzo
//      			    fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(finAlmuerzo);
//      			    Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();
//      			    calFechaHoraHorarioFinAlmuerzo.setTime(finAlmuerzo); 
//
//      			    
//      			    
//      			    
//      			    
//      			    
//      			
//						
//      			    
//      			    
//      			    
//      			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
//      			    	if (banderaEntrada==0) {
//								
//							
//                         //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
//      			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
//  			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
//      			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
//  			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
//      			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
//      			    
//      			    	//Calculo de tiempo de atraso en la entrada del empleado
//      			    	tipo1=1;
//      			    	tipo2=0;
//      			    	banderaEntrada++;
//      			    	}
//      			    }
//   
//     				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
//								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
//     					if (banderaEntrada==0) {
//      			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
//  			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADA");
//      			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
//  			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
//      			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
//      			    	tipo2=1;
//      			    	tipo1=0;
//      			    	banderaEntrada++;
//     					}
//      				}
//
//      			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) > 0
//      			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) < 0){
//      			    	//Almuerzo
//      			    	
//      			    	if (banderaAlmuerzo == 0){
//                             //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
//          			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
//      			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
//          			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
//      			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//          			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
//          			    	banderaAlmuerzo++;
//          			    	tipo3=1;
//          			    	
//          			    	
//          			    	
//      			    	}else{
//                             //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
//
//      			    		if (banderaAlmuerzoEntrada==0) {
//								tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//          			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
//          			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
//          			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
//          			    	tiempoAlmuerzo = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
//          			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
//          			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
//          			    	//Validacion si se pasa el almuerzo
//          			    	tipo4=1;
//          			    	banderaAlmuerzoEntrada++;
//      			    		}
//      			    	}
//          			   	
//
//      			    }  
//      			    
//      			    
//      			    
//      			    
//      			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) > 0
//      			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
//      			    	//Salida
//      			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
//  			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADA");
//      			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
//          			   	tipo5=1;
//          			   	tipo6=0;
//      			    	
//      			    }  	
//      			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
//                       //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
//
//      			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
//  			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
//      			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
//      			    	
//      			    	tipo6=1;
//      			    	tipo5=0;
//      			    }    
//  				
//      			
//      			    
//    //PARA LAS HORAS EXTRA
//      			    
//      			    
//  				}
//  				
//  				
//  				if (horarioDiaMes==0) {  					
//  					String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
//      			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
//      			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
//
//      			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
//      			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
//      			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
//  					
//      			    
//      			   
//						
//      			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
//      			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
//                     	 if (banderaHoraExtra1001==0) {
//      			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
//      			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
//      			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
//      			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
//  			    		banderaHoraExtra1001=1;
//  			   	    }else {
//							        			    
//      			    if (banderaHoraExtra1001==1) {
//     			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
//      			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
//      			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
//      			    	
//      			    	banderaHoraExtra1001=2;
//	     				}
//  				
//						        				
//      				}
//      			    }         			    
//      			    
//      			    
//  				
//  				}
//			
//  			}
//
//
//  		         			
//			 	//}else de grupo 
//			}//horario
// 		
// 		
// 		
//		//	if (!ideGrupoEmpleado.equals("3")) {
//
//			
//			//sin entrada
//			//sin almuerzo
//			//sin salida
//				if (banderaHoraExtra1001==2 || banderaHoraExtra1001==1) {
//					
//				if (banderaHoraExtra1001==1) {
//					//System.out.println("Ingresa aqui");
//				}
//				if (banderaHoraExtra1001==2) {
//					
//				
//if (tipo1==0 && tipo2==0 && tipo3==0 && tipo4==0 && tipo5==0 && tipo6==0) {
//insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, " HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
//SINDATO, SINDATO,SINDATO, SINDATO,Double.parseDouble(tab_reporte.getValor(itemReporte, "TIEMPOALM")) , tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),
//tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico));
// }
//	}    
//
//				}else {
//					//Variables para validacion de amracaciones
//					double almuerzo1=0.0;
//					String horarioEntrada="";
//					String horarioSalida="";
//					boolean bandera=false,banderaEntrada=false;
//					
//					//Si no hay datos le pongo =""
//					if (tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()) {
//						
//						almuerzo1=0.0;
//						
//					}else {
//						almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));
//
//					}
//					
//					if (tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO").equals("") || tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO").isEmpty()) {
//						
//						horarioEntrada="";
//						tab_reporte.setValor(itemReporte, "HORAINICIOBAND","");
//					}else {
//						horarioEntrada=tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO");
//						
//
//					}
//					if (tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO").equals("") || tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO").isEmpty()) {
//							horarioSalida="";
//
//					}else {
//						horarioSalida=tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO");
//
//					}
//					
//					 
//					
//					if (almuerzo1!=0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
//					bandera=true;
//				}else {
//					bandera=false;
//				}
//				
//				//	System.out.println("cccc");
//			
//			//sin entrada
//			//salida almuerzo
//			//sin salida
//		//	if (tipo1==0 && tipo2==0 && tipo3==1 && tipo4==0 && tipo5==0 && tipo6==0) {
//					
//					//diaSemana(fechaBiometrico);
//					
//		        insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
//		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
//		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
//		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
//		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
//		  //       }    
//
//
////}
//
//}
//		 
//		 
//		 
//		 
//		 
//		 
//		 }
	 
	 
	 
	 
	 
	 

	    @SuppressWarnings("unused")
	public void getMarcacionesEmpleadoMatriz(String IDE_GTEMP,String fechaInicial, String fechaFinal ){
			int contador = 0;

			int entrada=0,almuerzo=0,salida=0;
		    int entradaNocturno=0,salidaNocturno=0;

			 String fechaBiometrico="";

	    	
	    	//Hacer la consulta para poder retornar los horarios de cada empleado
	    	
	        StringBuilder str_ide_horarios=new StringBuilder(); 
	        TablaGenerica tabObternerTurnos=this.obtenerTurnosMatriz();

	        
	        if (tabObternerTurnos.getTotalFilas()==0) {
				utilitario.agregarMensajeError("No existen Horarios", "Asignados a turnos");
			}
	    	
	    	for (int itemHorario=0; itemHorario< tabObternerTurnos.getTotalFilas(); itemHorario++) {
	    		//Obtengo el ide del empleado con el cual obtendrenmos los horarios
	    		Integer ide_horario = pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor(itemHorario, "IDE_ASHOR"));
	            if(str_ide_horarios.toString().isEmpty()==false){
	                str_ide_horarios.append(",");
	    			}
	    			str_ide_horarios.append(ide_horario);

	    	}
	    	
	        	
	    	//Metodo devuelve el horario asignado a cada empleado
	       	TablaGenerica diasXHorario=utilitario.consultar("SELECT HORARIO.IDE_ASHOR,DIA.IDE_GEDIA,DIA.DETALLE_GEDIA FROM ASI_DIA_HORARIO HORARIO "
	    			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=HORARIO.IDE_GEDIA "
	    			+ "WHERE HORARIO.IDE_ASHOR IN ("+str_ide_horarios+")");
	    	
	       	       	
	    	String strFechaIniReporte =fechaInicial;
	    	String strFechaFinReporte = fechaFinal;
	    	
	    	
	    	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
	    	Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
	    	Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
	    	

	    	//Asigno el tamaño de la  tabla en la cual voy ir guardando los datos de timbre de cada empleado
	    	
//			Tabla que contiene las fechas de timbre del empleado	
	    	TablaGenerica tab_reporte=getTablaReporte("",strFechaIniReporte, strFechaFinReporte);
	    	
	    	if (tab_reporte.isEmpty()) {
				utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
			}
	    	
	    	
	    	for (int itemReporte=0; itemReporte< tab_reporte.getTotalFilas(); itemReporte++) {
	    	 //Obtengo el ide del empleado con el cual obtendrenmos los horarios
	    		Integer ide_gtemp = Integer.parseInt(tab_reporte.getValor(itemReporte, "IDE_GTEMP"));
	    		//Integer ide_gtemp = 32;
	    		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(itemReporte,"TO_CHAR");
	    		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
				//Obtengo los horarios de cada empleado de acuerdo a su ide_gtemp
	    		TablaGenerica horariosEmpleado = this.obtenerHorariosEmpleado(ide_gtemp);
	    		
	    		//Veo si dias es feriado
	    		
	    	    if (horariosEmpleado.getTotalFilas()>0) {
	    	

	    		
	    		//para turno nocturno
	    		TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
	    		String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
	    		String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
	    		TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
	    		String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
	    		
	    		int banderaHoraExtra1001=0;
				    banderaFeriados=false;

	    		Integer tipo1= 0;
			    Integer tipo2 = 0;
			    Integer tipo3 = 0;
			    Integer tipo4= 0;
			    Integer tipo5 = 0;
			    Integer tipo6 = 0;
			    Integer tipo7 = 0;
				Integer tipo1SinAlmuerzo= 0;
			    Integer tipo2SinAlmuerzo = 0;
			    Integer tipo3SinAlmuerzo = 0;
			    Integer tipo4SinAlmuerzo= 0;
	String  SINDATO="";
		    

	    		//Recorro los horarios 
	    		for (int i=0; i < horariosEmpleado.getTotalFilas(); i++) {
	    			//Me devuelve el dia si se encuentra dentro de su horario
	    			Map<Integer, Boolean> mapDias = obtenerDiasByHorario(diasXHorario, Integer.parseInt(horariosEmpleado.getValor(i, "ide_ashor")));
	    			
	    			List<Fila> biometrico;
	    			//Para marcaciones de tipo nocturno
	    			   

	        			 String horaBiometrico="";
	        			//variable para calculo de tiempo a justificar
	        			 String fechaHoraBiometrico;
	        			double tiempoAlmuerzo=0.0;
	    		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
	    		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
	         			double tiempoDiferenciaEntradaEmpleado=0.0;
	         			double tiempoDiferenciaSinEntradaEmpleado=0.0;

	         			
	     				//Para creacion de inconvenientes
	     				String entradaSinJustificacion="";
	         			String almuerzoSinEntradaJustificacion="";
	         			String salidaSinJustificacion="";

	     			 	String marcacion1="";
	     			 	String marcacion="";

	     			    banderaHoraExtra1001=0;
	         			         			    
	     			    
	     			     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
	     	            
	    			//Obtengo los timbres realizados por un empleado 
	    			 biometrico = obtenerTimbreBiometrico(ide_gtemp, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

	     			

	     			int banderaAlmuerzo=0;
	     			int banderaAlmuerzoEntrada=0;
	     			int banderaEntrada=0;
	     			//recorro todos las timbradas
	     			for (Fila bio : biometrico) {
	     				//fecha de la tabla biometrico
	     				fechaBiometrico = (String)bio.getCampos()[1];
	     				//Hora de la tabla biometrico
	     				horaBiometrico = (String)bio.getCampos()[2];
	     				//Evento de acuerdo a si es entrada o almuerzo
	     			//	String evento = (String)bio.getCampos()[3];
	     				//Unimos la fecha con la hora
	     				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
	     			    //Calendario 
	     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
	     				
	     				//Obtengo el dia de la semana a que corresponde esa fecha
	     				int diaSemana = calFechaHoraBiometrico.get(Calendar.DAY_OF_WEEK);
	     			    //Validacion para el dia domingo=0
	     				if (diaSemana==1) {
	 						diaSemana=8;
	 					}
	     				
	     				
	     				if (mapDias.get(diaSemana-1) != null && banderaFeriados==false){
	     					//Horario de acuerdo a los turnos ingrresados por talento humano
	     					String horaInicioEmpl = horariosEmpleado.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	     					String horaFinEmpl = horariosEmpleado.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	     					String horaIniAlmEmpl = horariosEmpleado.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
	     					String horaFinAlmEmpl = horariosEmpleado.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
	     					
	     					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
	         			   //Suma de minuto para el ingreso
	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
	     					
	     					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

	         			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
	         			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
	         			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
	     					
	         			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
	         			   Date finAlmuerzo=null;
	         			    
	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
	         			  if ((int)tiempoAlmuerzo==1) {
	           			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
						}else {
	           			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR")));
						}
	     					
	         			    //Sumamos una hora para el almuerzo
	         			    fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(finAlmuerzo);
	         			    Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();
	         			    calFechaHoraHorarioFinAlmuerzo.setTime(finAlmuerzo); 

	         			    
	         			    
	         			    
	         			    
	         			    
	         			    
	         			    if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")  ) {
	         			   	         			       
	             			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
	                                //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	                     			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                 			    
	                     			//Calculo de tiempo de atraso en la entrada del empleado
	             			    	tipo1=1;
	             			    
	             			    }
	          
	            				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
	    								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {

	            					if (tipo1==0) {
										
	            						
	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	                     			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                 			    
	                 			    tipo1=1;
	            					}else {
	            						if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {

	                 			    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                 			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                 			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	            						}  else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {
	            						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
										}else {
										    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
										}               			    		
									}
	             			                 			    
	             				}

							}else {
								
							
	         			    
	         			    
	         			    
	         			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
	         			    	if (banderaEntrada==0) {
									
								
	                            //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	         			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	     			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	         			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	     			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	         			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	         			    
	         			    	//Calculo de tiempo de atraso en la entrada del empleado
	         			    	tipo1=1;
	         			    	tipo2=0;
	         			    	banderaEntrada++;
	         			    	}
	         			    }
	      
	        				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
									&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
	        					if (banderaEntrada==0) {
	         			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	     			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	         			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	     			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	         			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	         			    	tipo2=1;
	         			    	tipo1=0;
	         			    	banderaEntrada++;
	        					}
	         				}

	         			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) > 0
	         			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) < 0){
	         			    	//Almuerzo
	         			    	
	         			    	if (banderaAlmuerzo == 0){
	                                //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	             			    	banderaAlmuerzo++;
	             			    	tipo3=1;
	             			    	
	             			    	
	             			    	
	         			    	}else{
	                                //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	         			    		if (banderaAlmuerzoEntrada==0) {
										
									
	         			    		
	             			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
	             			    	
	             			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
	             			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));

	             			    	tiempoAlmuerzo = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
	             			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
	             			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	             	
	             			    	//Validacion si se pasa el almuerzo
	             			    	tipo4=1;
	             			    	banderaAlmuerzoEntrada++;
	         			    		}
	         			    	}
	             			   	

	         			    }  
	         			    
	         			    
	         			    
	         			    
	         			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) > 0
	         			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
	         			    	//Salida
	                           // tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	         			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	     			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
	         			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	             			   	tipo5=1;
	             			   	tipo6=0;
	         			    	
	         			    }  	
	         			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
	                          //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	         			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	     			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	         			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
	         			    	
	         			    	tipo6=1;
	         			    	tipo5=0;
	         			    }  
							}
	         		
	     				}else{
	     					//break;//si no se encuentra dia
	     				if (tipo1!=0 || tipo2!=0 || tipo3!=0  || tipo4!=0 || tipo5!=0  || tipo6!=0 || banderaFeriados==false && mapDias.get(diaSemana-1) != null ) {
	     					
						break;	
						
						}
	     				
	     				else if(mapDias.get(diaSemana-1)==null) {
							
	    					diaSemana = calFechaHoraBiometrico.get(Calendar.DAY_OF_WEEK);
	         			    //Validacion para el dia domingo=0
	         				if (diaSemana==1) {
	     						diaSemana=8;
	     					}
	         				
	         				String horaInicioEmpl = horariosEmpleado.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	     					String horaFinEmpl = horariosEmpleado.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	     				//	String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
	         			 //  Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
	         			  //  calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
	     					
	     					String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

	         			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	         			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	         			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
	     					
	         			    
	         			   
							
	         			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	         			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	                         
	         			    	 if (banderaHoraExtra1001==0) {
	         			    	
	         			    	//tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	         			     	//tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", "00:00:00");
	         			    
	         			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	         			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	         			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
	     			    		banderaHoraExtra1001=1;
	     			   	    }else {
								        			    
	         			    if (banderaHoraExtra1001==1) {
	        			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	         			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	         			    	
	         			    	banderaHoraExtra1001=2;
		     				}
	     				
							        				
	         				}
	         			    }
	         			    	 

						}		
	         			    	 

	  
	     				
	     				}
	  			
	     			}


	     		         			
				 	
				}//horario
	    		
	    		
	    		
	  
	 			
	 			//sin entrada
	 			//sin almuerzo
	 			//sin salida
					if (banderaHoraExtra1001==2 || banderaHoraExtra1001==1) {
						
					if (banderaHoraExtra1001==1) {
						//System.out.println("Ingresa aqui");
					}
					if (banderaHoraExtra1001==2) {
						
					
	if (tipo1==0 && tipo2==0 && tipo3==0 && tipo4==0 && tipo5==0 && tipo6==0) {
	insertarTablaResumen(ide_gtemp, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, " HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
	SINDATO, SINDATO,SINDATO, SINDATO,Double.parseDouble(tab_reporte.getValor(itemReporte, "TIEMPOALM")) , tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico));
	    }
		}    

					}else {
						//Variables para validacion de amracaciones
						double almuerzo1=0.0;
						String horarioEntrada="";
						String horarioSalida="";
						boolean bandera=false,banderaEntrada=false;
						
						//Si no hay datos le pongo =""
						if (tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()) {
							
							almuerzo1=0.0;
							
						}else {
							almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));

						}
						
						if (tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO").equals("") || tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO").isEmpty()) {
							
							horarioEntrada="";
							tab_reporte.setValor(itemReporte, "HORAINICIOBAND","");
						}else {
							horarioEntrada=tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO");
							

						}
						if (tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO").equals("") || tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO").isEmpty()) {
								horarioSalida="";

						}else {
							horarioSalida=tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO");

						}
						
						 
						
						if (almuerzo1!=0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
						bandera=true;
					}else {
						bandera=false;
					}
					
					//	System.out.println("cccc");
	 			
	 			//sin entrada
	 			//salida almuerzo
	 			//sin salida
	 		//	if (tipo1==0 && tipo2==0 && tipo3==1 && tipo4==0 && tipo5==0 && tipo6==0) {
	 		        insertarTablaResumen(ide_gtemp, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
	 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
	 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
	 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
	 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
	 		  //       }    


	}
	    	    }else {//else si no encuentra horario
					
				}

	
				}

	    	
	    	//Asigno los datos extraidos a la tabla para reporte
	    //	tab_tabla.setFilas((LinkedList<Fila>)tab_reporte.getFilas());
	    	utilitario.addUpdate("tab_tabla,tab_inconsistencias");

	    	
	    }
	    
	 
	    
	    
	    private TablaGenerica obtenerTurnosMatriz(){
	    	
	    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario where ide_astur=1");
	    	return obtenerHorarios;
	    	
	    }


		public boolean isEstado_empleado_nocturno() {
			return estado_empleado_nocturno;
		}


		public void setEstado_empleado_nocturno(boolean estado_empleado_nocturno) {
			this.estado_empleado_nocturno = estado_empleado_nocturno;
		}
	    
	    
	    //
		//retorna si el dia tiene turno 
		public int getHorarioXDia(int ide_empleado,int dia,int mes ,int anio){
	    
		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+ide_empleado+"  "
				+ "and ide_gemes="+mes+" and ide_geani="+anio);
         int contieneDiaTurno=0;
		
        //Si contiene Matriz
         if (tabEmpleadoMatrizMensual.getValor("DIA"+dia)==null || tabEmpleadoMatrizMensual.getValor("DIA"+dia).isEmpty() || tabEmpleadoMatrizMensual.getValor("DIA"+dia).equals("")) {
        	 contieneDiaTurno=0;
        	 }else {
        		 //contiene el ide del turno
       	 contieneDiaTurno=Integer.parseInt(tabEmpleadoMatrizMensual.getValor("DIA"+dia));

		}
         
         return contieneDiaTurno;
	 
		}

		
		/*
		
/////  dsfsfdsdfsfdsdsffdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss
public void procesarMarcaciones(int contieneDiaTurno,int contieneTunnoNocturnoAnterior,String fechaBiometricoAgrupadaXEmpleado,int empleado,Date dateFechaInicioReporteAgrupadaXEmpleado,String fechaBiometrico,
		int itemReporte,String strFechaIniReporte,String strFechaFinReporte ){

            boolean banderaSinAccion=false;

	
	
	
	    	TablaGenerica tab_reporte=getTablaReporte("",strFechaIniReporte, strFechaFinReporte);
	    	
	    	if (tab_reporte.isEmpty()) {
				utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
			}
			
			
	
	
	//permiso online

	
	
	//Si tiene asignado matriz para ese dia
    TablaGenerica tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
    
    
    
    
    
    if (tabTipoTurno.getValor("IDE_GTGRE")==null || tabTipoTurno.getValor("IDE_GTGRE").equals("") ) {
    	ide_gtgre=0;
	}else {
		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
	}
    
    
	
	//Si tiene asignado matriz para ese dia
    TablaGenerica tabTipoTurnoNocturno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneTunnoNocturnoAnterior);
    
    
    
    
    
    if (tabTipoTurnoNocturno.getValor("IDE_GTGRE")==null || tabTipoTurnoNocturno.getValor("IDE_GTGRE").equals("") ) {
    	estado_empleado_nocturno=false;
	}else {
		estado_empleado_nocturno=true;
	}
    
    
   
  //Obtengo el horario
	 TablaGenerica tabObternerTurnos=this.obtenerTurnos(contieneDiaTurno);
//guardo el ide_del horario
    int ide_ashor= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
    
 
    
    
	TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(ide_ashor);

	//para turno nocturno
//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
//	String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
//	String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
	//TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
//	String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
	
	
	
	
	
	int banderaHoraExtra1001=0;
	    banderaFeriados=false;

	Integer tipo1= 0;
    Integer tipo2 = 0;
    Integer tipo3 = 0;
    Integer tipo4= 0;
    Integer tipo5 = 0;
    Integer tipo6 = 0;
    Integer tipo7 = 0;
	Integer tipo1SinAlmuerzo= 0;
    Integer tipo2SinAlmuerzo = 0;
    Integer tipo3SinAlmuerzo = 0;
    Integer tipo4SinAlmuerzo= 0;
    String  SINDATO="";
    int cont=0;

    int forHorarios=0;
    forHorarios = horariosEmpleadoMes.getTotalFilas();
    if (contieneDiaTurno==0 && forHorarios==0) {
    	forHorarios=1;
	}
	
	
	
	
	
	
	
	
	//Recorro los horarios 
	for (int i=0; i < forHorarios; i++) {
		
		
			List<Fila> biometrico;
			 String horaBiometrico="";
			//variable para calculo de tiempo a justificar
			 String fechaHoraBiometrico;
			double tiempoAlmuerzo=0.0;
	    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
	    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
 			double tiempoDiferenciaEntradaEmpleado=0.0;
 			double tiempoDiferenciaSinEntradaEmpleado=0.0;

 			
				//Para creacion de inconvenientes
				String entradaSinJustificacion="";
 			String almuerzoSinEntradaJustificacion="";
 			String salidaSinJustificacion="";

			 	String marcacion1="";
			 	String marcacion="";

			    banderaHoraExtra1001=0;
 			         			    
			    
			    
				//Verifico si es dia feriado    
			   banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
	            
	         
	        int tipoEntrada1=0,tipoEntrada2=0;
	            
	            //si es de tipo nocturno
	            
	        if (ide_gtgre!=3) {
			//Obtengo los timbres realizados por un empleado 
	          biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

		}else {
			
			TablaGenerica tabTurnosHorario=utilitario.consultar("select * from asi_turnos_horario where ide_astur="+contieneDiaTurno);
			TablaGenerica tab_horario=utilitario.consultar("select * from asi_horario where ide_ashor="+tabTurnosHorario.getValor("IDE_ASHOR")); 
			String horaEntrada=tab_horario.getValor("hora_inicial_ashor");
			String horaSalida=tab_horario.getValor("hora_final_ashor");
										
										
										
			//Sumo un dia  a la fecha de entrada							
			Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
			sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
			sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
						    			
			
			String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
			String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
						    			
						    									
										
										
			//Parametro hasta para el sql de la consulta cuando es por turno nocturno
			String horaFechaDiaInicio=diaInicio+" 20:00:00";
			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
		
			//Parametro antes para el sql de la consulta cuando es por turno nocturno
			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
			
			
			//suma una hora a la fecha inicial  22:00:00
			String horaFechaFinConsulta= diaInicio+" "+horaEntrada;    			
			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-1));
			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
				
			//

			//suma una hora a la fecha final 
			String horaFechaInicioConsulta= diaSumaInicio+" "+horaSalida;    			
			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
			biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			
			cont=0;
			
		}	


       //Si tiene timbradas en el biometrico 
         if (biometrico.size()!=0) {

         
   	boolean bandEntradaSinAlmuerzo=false;
		
			int banderaAlmuerzo=0;
			int banderaAlmuerzoEntrada=0;
			int banderaEntrada=0;
			//recorro todos las timbradas
			for (Fila bio : biometrico) {
				//fecha de la tabla biometrico
				fechaBiometrico = (String)bio.getCampos()[1];
				//Hora de la tabla biometrico
				horaBiometrico = (String)bio.getCampos()[2];
				//Evento de acuerdo a si es entrada o almuerzo
			//	String evento = (String)bio.getCampos()[3];
				//Unimos la fecha con la hora
				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
			    //Calendario 
				Calendar calFechaHoraBiometrico = Calendar.getInstance();
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
				
			
			
				String horaInicioEmpl ="";
				String horaFinEmpl = "";
				String horaIniAlmEmpl ="";
				String horaFinAlmEmpl = "";
		
				boolean t=false;
				//si contiene un turno asignado a ese dia  asignado en la matriz
				if (contieneDiaTurno!=0 ) {
    				// TURNO SI NO ES DE TIPO NOCTURNO
					if (ide_gtgre!=3 && banderaFeriados==false) {
					
				    horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
					horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
					horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
					
					
					
					
					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
 			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
 			   //Suma de minuto para el ingreso
    			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
 			    
					
					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

 			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
 			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
 			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
					
 			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
 			   Date finAlmuerzo=null;
 			    
 			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
 			  if ((int)tiempoAlmuerzo==1) {
   			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
 			  							  }else{
   			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
 			  							  		}
					
 			    //Sumamos una hora para el almuerzo
 			    fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(finAlmuerzo);
 			    Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();
 			    calFechaHoraHorarioFinAlmuerzo.setTime(finAlmuerzo); 

 			    
 			
					
				
 			    //Condicion si no existe hora de almuerzo
 			   if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")  ) {
 				   
 			       
 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
                    //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
 			    	
 			    	if (tipo1==0) {
						
					
 			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
 			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
         			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
         			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
     			    
         			//Calculo de tiempo de atraso en la entrada del empleado
 			    	tipo1=1;
 			    	}
 			    }

				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
						&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {

					if (tipo1==0) {
						
						
 			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
 			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
         			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
         			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
     			    
     			    tipo1=1;
					}else {
						if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {

     			    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
     			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
     			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
     			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
						}  else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) <= 0) {
						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
             			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
             			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
             			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
						}else {
						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
             			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
             			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
             			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
						}               			    		
					}//else de caso
 			                 			    
 				}//Segundo caso Caso
 			    
 			    
				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin)>= 0) {
				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
     			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
     			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
     			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
			
				}

 			   }//fin de condicion  si no tiene ALMUERZO

 			   else {
 				  //Caso contrario si tiene almuerzo
    			    
    			    
    			    
    			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
    			    	if (banderaEntrada==0) {
							
						
                       //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
    			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
    			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
    			    
    			    	//Calculo de tiempo de atraso en la entrada del empleado
    			    	tipo1=1;
    			    	tipo2=0;
    			    	banderaEntrada++;
    			    	}
    			    }
 
   				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
							&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
   					if (banderaEntrada==0) {
    			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
    			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
    			    	tipo2=1;
    			    	tipo1=0;
    			    	banderaEntrada++;
   					}
    				}

    			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) > 0
    			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) < 0){
    			    	//Almuerzo
    			    	
    			    	if (banderaAlmuerzo == 0){
                           //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
        			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
    			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
        			    	banderaAlmuerzo++;
        			    	tipo3=1;
        			    	
        			    	
        			    	
    			    	}else{
                           //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

    			    		if (banderaAlmuerzoEntrada==0) {
							tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
        			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
        			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
        			    	tiempoAlmuerzo = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
        			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
        			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
        			    	//Validacion si se pasa el almuerzo
        			    	tipo4=1;
        			    	banderaAlmuerzoEntrada++;
    			    		}
    			    	}
        			   	

    			    }  
    			    
    			    
    			    
    			    
    			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) > 0
    			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
    			    	//Salida
    			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
    			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
        			   	tipo5=1;
        			   	tipo6=0;
    			    	
    			    }  	
    			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
                     //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

    			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
    			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
    			    	
    			    	tipo6=1;
    			    	tipo5=0;
    			    }    
				
    			
			}//Fin de ciclo si contiene almuerzo
 			   
 			   
 
 			   
 			   
 			   
 			   
 			   
 			   

					}else if(ide_gtgre==3 && banderaFeriados==false){ 
						//Si es de tipo nocturno	
						//System.out.println("TIPO NOCTURNO");
						
						//Ingreso a metodo
						cont++;
						
						int banderaIngreso=0;
	    			    int banderaSalida=0;
	    				    banderaAlmuerzo=0;
	    				    banderaEntrada=0;

	    				int tipoEntrada3=0,tipoEntrada4=0;

	    				//Horario de acuerdo a los turnos ingrresados por talento humano
						horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
						horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
						//String horaIniAlmEmpl = horariosEmpleado.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
						//String horaFinAlmEmpl = horariosEmpleado.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
						
						String HoraHorarioInicio = horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
	    			   Calendar  calFechaHoraHorarioInicio = Calendar.getInstance();//
	    			    
						String HoraHorarioFin = horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
						Calendar calFechaHoraHorarioFin = Calendar.getInstance();

					
	    			Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
	    			sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
	    			sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
	    			
	    	
	    		
	    			String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
	    			String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
	    			
	    		
	    			
	    			
	    			
	    			
	    			//Parametro hasta para el sql de la consulta cuando es por turno nocturno
	    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
	    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
	    		
	    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
	    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
	    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
	    			
	    			
	    			//suma una hora a la fecha inicial  22:00:00
	    			String horaFechaFinConsulta= diaInicio+" "+horaInicioEmpl;    			
	    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-1));
	    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
	    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
						
	    			//

	    			//suma una hora a la fecha final 
	    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaFinEmpl;    			
	    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
	    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
	    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
						
	    			
	    			
	    			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7  	    			
	    			
	    			
	    			
	 			//Horario de acuerdo a los turnos ingrresados por talento humano
				String horaInicioEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
				String horaFinEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
				horaIniAlmEmpl = horariosEmpleadoMes .getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
				horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
				
				
				
				String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl1; //cogo la hora y le concateno con la fecha del horario
    			//String fechaHoraHorarioInicio= getFechaAsyyyyMMddHHmmss(sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio1),Integer.parseInt(minuto_tolerancia_astur)));

				
				
				Calendar calFechaHoraHorarioInicio1 = Calendar.getInstance();//
			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
			    //Suma de minuto para el ingreso
    			 calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
				
				 String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioFin1 = Calendar.getInstance();
			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

			    
			  //Desde hasta puede hora de salida para el horario nocturno
			   String fechaHoraHorarioFinSalida = fechaBiometrico+" 08:00:00";
			    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
			    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
				
			    String fechaHoraHorarioInicioFinSalida = fechaBiometrico+" 04:00:00";
			    Calendar calFechaHoraHorarioInicioFinSalida = Calendar.getInstance();
			    calFechaHoraHorarioInicioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinSalida));
				       			    
				//Desde hasta puede hora de ingreso para el horario nocturno
			    String fechaHoraHorarioFinEntrada = fechaBiometrico+" 24:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada));
			    
				String fechaHoraHorarioInicioEntrada = fechaBiometrico+" 00:00:00";
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
				    
			    
			    Calendar calSumaDia = Calendar.getInstance();
			    calSumaDia.setTime(getFechaAsyyyyMMddHHmmss(horaFechaSumaDia1));
				    
			    
			   //Prueba horario nocturno
			    //variable que guarda la hora desde 00:00: del dia siguiente
			    String fechaHoraCeroHoras = fechaBiometrico+" 00:00:00";
			    Calendar fecHoraInicio = Calendar.getInstance();
			    fecHoraInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
				    
			    
			    
				//Siguiente
    			String fecha= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado);
    			String fechayhora=fecha+" 08:00:00";
    		    Calendar calFin = Calendar.getInstance();//
			    calFin.setTime(getFechaAsyyyyMMddHHmmss(fechayhora));//
    			
			    
			  //Siguiente
    			String fecha1= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
    			String fechayhora12=fecha1+" 22:00:00";
    		    Calendar calEntrada = Calendar.getInstance();//
			    calEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechayhora12));//
			    
			    

			  	if (cont==1) {

			    Integer tipoEvento = 0;
			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
        			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
    			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
        			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM","0");
        			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);

        			    	tipoEntrada1=1;
        			    	tipoEntrada2=0;
        			    }
				else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0 &&
			    		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada)<=0){
                       
					
					
			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
		    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
			    
    			    tipoEntrada1=1;
			    	tipoEntrada2=0;

			   }
        			    
        			    //mayor a la hora de horario incio con la hora biometrico timbrada
    			      //menor a la hora 24:00:00
			 	
			  	}	
			 	
			 	if (cont>1) {
        					
         			    //mayor a hora: 0:00:00
        			    //menor a las 6 de la manaña	
        			     if(calFechaHoraBiometrico.compareTo(fecHoraInicio) >0 &&
        			    	     calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)<0){
        			    		    	
        			       	//tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
    			    		//tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
    			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
      			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
      			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
      			    		tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
      			    		
        			    //mayor que las 06:00:00
        			    //	y menor que 08:00:00
        			    }else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)>=0 
        			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)<=0){
        			    	

      			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
  			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
  			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
  			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
  			    		tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
        			    
        			    }

 					
 					
        				}
 					
 					
						
						
						
						
					}//Fin de ciclo si es tipo ncturno
					else if (banderaFeriados=true && ide_gtgre!=3 ) {
				//si es dia normal y es feriado

						String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
		 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

		 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
		 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
							
						
		 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		                	 if (banderaHoraExtra1001==0) {
		 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
					    		banderaHoraExtra1001=1;
					   	    }else {
							        			    
					        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
		 			    	
		 			    	banderaHoraExtra1001=2;
		 				
						
						        				
		 				}
					
					}
	     				
	     				/*}else if ((banderaFeriados=true && ide_gtgre==3)) {
							
							
							
							
						}*/
						
						
						
						
						
						
						
						
						
				
	/*
			}// Fin de ciclo si contiene turno 
				
				
				
				else if (contieneDiaTurno==0) {  					
				//si no contiene turno
				<>
				
			if(biometrico.size()!=0){
					if (estado_empleado_nocturno==false && biometrico.size()!=1) {
					
				
				
				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
			   
			
			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
            	 if (banderaHoraExtra1001==0) {
			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 

		    		banderaHoraExtra1001=1;
		   	    }else {
				        			    
		        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
			    	
			    	banderaHoraExtra1001=2;
				
			
			        				
				}
			   
			    }//fin de si tiene TurnoNocturno el dia anteriror
			    
					}else if (estado_empleado_nocturno==true && biometrico.size()!=1 ) { 
						
     				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
     			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
     			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

     			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
     			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
     			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
     			   
					
     			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
     			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
                    	 if (banderaHoraExtra1001==0) {
     			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
     			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
     			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 

 			    		banderaHoraExtra1001=1;
 			   	    }else {
						        			    
    			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
     			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
     			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
     			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
     			    	
     			    	banderaHoraExtra1001=2;
     				
 				
					        				
     				}
     			   
				}
			    
			    
					}else if(estado_empleado_nocturno==true || estado_empleado_nocturno==false && biometrico.size()==1){
			    	//si tiene anterior
						//Variable que avisa si se ingresa o no el evento su es de tipo nocturno el dia anterior
						sinIngreso=true;
				break;
			}
			    	 
			    
			    
				}
			    
			}// FIN DE CICLO SI NO CONTIENE HORARIO 
					
				
				
		
				
		}//For Biometrico
			
			
			
			
			
   	}// if biometrico
         else {	//Si no se tiene marcaciones hechas en el biometrico 
   		
   		if (contieneDiaTurno!=0) {
   			//Si  contiene horario pero no timbrada
	  	  /*  	
	    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", "");
	    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "SIN MARCACIÓN");
	       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
	    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", "");
	    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN MARCACIÓN");
      		*/
	/*}
   		else {
   			banderaSinAccion=true;
		}
  
   		
   		
	}
			
	}//For horario

	
	
		
	
}


*/







//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado


public TablaGenerica getTablaReporteEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){
	
	 TablaGenerica tab_reporte;
	
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
	   tab_reporte= utilitario.consultar(
	    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
			+ "from gth_empleado emp  "
			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
			+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
			+ "ORDER BY EMP.IDE_GTEMP");		
	 }else {
		 tab_reporte= utilitario.consultar(
			    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
					+ "from gth_empleado emp  "
					+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
					+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				   + "and EMP.ide_gtemp IN("+IDE_GTEMP+") "
					+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
					+ "ORDER BY EMP.IDE_GTEMP");		
	}
	   
		//tab_reporte.imprimirSql();
		 return tab_reporte;	   
			}



public Integer getIde_geedp() {
	return ide_geedp;
}


public void setIde_geedp(Integer ide_geedp) {
	this.ide_geedp = ide_geedp;
}


public String getHoraAnterior() {
	return horaAnterior;
}


public void setHoraAnterior(String horaAnterior) {
	this.horaAnterior = horaAnterior;
}


public String getHoraAnteriorAlm() {
	return horaAnteriorAlm;
}


public void setHoraAnteriorAlm(String horaAnteriorAlm) {
	this.horaAnteriorAlm = horaAnteriorAlm;
}



 public void getSinEntradaSalida(){
	 
		

		
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
       		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
           		String fechaIni=(cal_fecha_inicial.getFecha());	
           		String fechaFin=(cal_fecha_final.getFecha());	
           		
           	   if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
           		tab_resumen_marcaciones.setCondicion("horafinband_cobmr ='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           				+ " (horainicioband_cobmr='')");
       		}else {
       			tab_resumen_marcaciones.setCondicion("horafinband_cobmr ='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           				+ " (horainicioband_cobmr='') and ide_gtemp in("+str_ide.toString()+")");
			}
           		
           		
     
           	
        		tab_resumen_marcaciones.ejecutarSql();
        		utilitario.addUpdate("tab_resumen_marcaciones");
        		if (tab_resumen_marcaciones.getTotalFilas()==0) {
					utilitario.agregarMensajeInfo("No se encontraron registros para las fechas seleccionadas ", "Fecha Inicio:"+fechaIni+" y Fecha Final: "+fechaFin);
				}
        	
       		}else {
       			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

       		}
       		
       	}else {
       		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
       	}		

	 
}



 public void getSinEntrada(){
	
	 
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
       		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
           		String fechaIni=(cal_fecha_inicial.getFecha());	
           		String fechaFin=(cal_fecha_final.getFecha());	

           		//tab_resumen_marcaciones.setCondicion("horafinband_cobmr !='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           	   //		+ " (horainicioband_cobmr='')");
        	  	
            	   if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
                  		tab_resumen_marcaciones.setCondicion("horafinband_cobmr !='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
                   				+ " (horainicioband_cobmr='') ");
              		}else {
              			tab_resumen_marcaciones.setCondicion("horafinband_cobmr !='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
                   				+ " (horainicioband_cobmr='') and ide_gtemp in("+str_ide.toString()+")");
       			}
           		
           		
           		tab_resumen_marcaciones.ejecutarSql();
        		utilitario.addUpdate("tab_resumen_marcaciones");

       		
       		}else {
       			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");


       		}
       		
       	}else {
       		utilitario.agregarMensajeInfo("Debe seleccionar un rango de fechas ",	"Ingrese fecha inicial y fecha final");
       	}		
	 
	 
}
 
 
 

 public void getSinSalida(){
	
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
       		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
           		String fechaIni=(cal_fecha_inicial.getFecha());	
           		String fechaFin=(cal_fecha_final.getFecha());	


           		//tab_resumen_marcaciones.setCondicion("horafinband_cobmr ='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           		//		+ " (horainicioband_cobmr!='')");
           		
            	   if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
                  		tab_resumen_marcaciones.setCondicion("horafinband_cobmr ='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           				+ " (horainicioband_cobmr!='') ");
              		}else {
              			tab_resumen_marcaciones.setCondicion("horafinband_cobmr ='' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' AND "
           				+ " (horainicioband_cobmr!='') and ide_gtemp in("+str_ide.toString()+")");
       			}
           		
           		tab_resumen_marcaciones.ejecutarSql();
        		utilitario.addUpdate("tab_resumen_marcaciones");
      		
       		
       		}else {
       			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

       		}
       		
       	}else {
       		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
       	}		
	 
	 
}

 
 
 

public Tabla getTab_marcaciones() {
	return tab_marcaciones;
}





public void setTab_marcaciones(Tabla tab_marcaciones) {
	this.tab_marcaciones = tab_marcaciones;
}





public void actualizarMarcacionesXFecha(SelectEvent evt){
	tab_resumen_marcaciones.seleccionarFila(evt);
	String ide_gtemp=tab_resumen_marcaciones.getValor("IDE_GTEMP");
//	System.out.println(ide_gtemp+"MARCACIONES");

	TablaGenerica tab_empleado =utilitario.consultar("SELECT ide_gtemp, documento_identidad_gtemp,tarjeta_marcacion_gtemp FROM gth_empleado where ide_gtemp in("+ide_gtemp+")");
	String fechaIni=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(tab_resumen_marcaciones.getValor("FECHA_EVENTO_COBMR")), -1));
	String fechaFin=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(tab_resumen_marcaciones.getValor("FECHA_EVENTO_COBMR")), 1));
	String ide_persona_cobim = tab_empleado.getValor("tarjeta_marcacion_gtemp");
	
		tab_marcaciones.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM,(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as NOMBRES,DETALLE_COREL,TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  "
				              + "WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' and trim(ide_persona_cobim)='"+ide_persona_cobim+"' order by TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') asc");	
		tab_marcaciones.ejecutarSql();
	utilitario.addUpdate("tab_marcaciones");
	
	
}

 
 //Validacion Horario Entrada
public void getHorario()
{
	//System.out.println("sdsmdsnsd");
	
 		TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+tab_resumen_marcaciones.getValor("IDE_GTEMP")+" AND IDE_ASTUR=1 and ACTIVO_GTEMP=TRUE");
		//variable donde se guarda el ide del turnomatriz
		int turnoMatriz=0;
		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
			turnoMatriz=0;
		}else {
			turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
		} 
		
	
	//tab_resumen_marcaciones.seleccionarFila(evt);
	String ide_gtemp="";
	ide_gtemp="323";
//	ide_gtemp=tab_resumen_marcaciones.getValor("IDE_GTEMP");

/*	if (tab_resumen_marcaciones.getTotalFilas()<=0) {
		utilitario.agregarMensajeInfo("No se puede actualizar","No existen registros");
		return;		
	}*/
	
	if (turnoMatriz!=0) {
		utilitario.agregarMensajeInfo("No se puede actualizar","El funcionario continene un horario anual");
		return;
	}
	
	if (tab_resumen_marcaciones.getTotalFilas()<=0) {
		utilitario.agregarMensajeInfo("No se puede actualizar","No se ha seleccionado un registro");
		return;
	}
	
	
	
	sel_editar_horario.setId("sel_editar_horario");
	
	sel_editar_horario.setSeleccionTabla("SELECT ath.ide_astur, ahor.nom_ashor,ahor.hora_inicial_ashor, "
			+ "ahor.hora_final_ashor, ahor.hora_inicio_almuerzo_ashor, "
			+ "ahor.hora_fin_almuerzo_ashor, ahor.min_almuerzo_ashor "
			+ "FROM asi_turnos_horario ath "
			+ "LEFT JOIN asi_turnos atur on atur.ide_astur=ath.ide_astur "
			+ "LEFT JOIN asi_horario ahor on ahor.ide_ashor=ath.ide_ashor "
			+ "where atur.turno_matriz_astur=false  ORDER BY ath.ide_astur asc","ide_astur");
	
	sel_editar_horario.getTab_seleccion().getColumna("nom_ashor").setFiltro(true);
	sel_editar_horario.setTitle("Seleccione Horario ");
	sel_editar_horario.setRadio();

	gru_pantalla.getChildren().add(sel_editar_horario);
	sel_editar_horario.getBot_aceptar().setMetodo("actualizarHorario");
	sel_editar_horario.setTitle("Seleccione Mes Horario");
	sel_editar_horario.setWidth("35");
	sel_editar_horario.setHeight("40");

	sel_editar_horario.redibujar();
   	utilitario.addUpdate("sel_editar_horario");
   	

	
	
	
	
	
/*+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ "select emp.ide_gtemp, emp.apellido_paterno_gtemp,  "
				+ "(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) as apellido_materno_gtemp,primer_nombre_gtemp,   "
				+ "(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as segundo_nombre_gtemp,documento_identidad_gtemp "
				+ "from gth_empleado emp   "
				+ "left join asi_horario_mes_empleado asemp on asemp.ide_gtemp=emp.ide_gtemp  "
				+ " where emp.activo_gtemp in (true) "
				+ " and ide_gemes="+mes+" and ide_geani in("+sel_anio_editar.getValorSeleccionado()+")  "
				+ "order by apellido_paterno_gtemp, apellido_materno_gtemp","IDE_GTEMP");
	
	*/


   	
   	

}


public SeleccionTabla getSel_editar_horario() {
	return sel_editar_horario;
}


public void setSel_editar_horario(SeleccionTabla sel_editar_horario) {
	this.sel_editar_horario = sel_editar_horario;
}



 
//Validacion Horario Entrada Timbrada 
public void actualizarHorario()
{
	
	if (sel_editar_horario.getValorSeleccionado()== null || sel_editar_horario.getValorSeleccionado().equals("")) {
		utilitario.agregarMensajeInfo("No se puede actualizar","Debe seleccionar un registro");
		return;
		
	}else {
		int turno=Integer.parseInt(sel_editar_horario.getValorSeleccionado());
		

		String ide_gtemp=tab_resumen_marcaciones.getValor("IDE_GTEMP");
		String fecha = tab_resumen_marcaciones.getValor("FECHA_EVENTO_COBMR");
		
		int mesTemp=0,diaTemp=0,anio=0,anioTemp=0;
		String dia="",mes="";
		diaTemp=utilitario.getDia(fecha);
		
		
		if (diaTemp<10) {
			dia="0"+diaTemp;
		}else {
			dia=""+diaTemp;
		}
		
		
		
		
		mesTemp=utilitario.getMes(fecha);
		if (mesTemp<10) {
			mes="0"+mesTemp;
		}else {
			mes=""+mesTemp;
		}
		
		
		
		anio=utilitario.getAnio(fecha);
		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio  where detalle_geani like '%"+anio+"%'");
          anioTemp=Integer.parseInt(tabAnio.getValor("ide_geani"));
		ide_gtemp=tab_resumen_marcaciones.getValor("IDE_GTEMP");
//Verificar si existe horario para el empleado se 
		TablaGenerica tabTurnos = utilitario.consultar("SELECT  ide_gtemp, ide_geedp "
				+ "FROM asi_horario_mes_empleado  "
				+ "where ide_gemes="+mes+" and ide_gtemp="+ide_gtemp+" and ide_geani="+anioTemp+"  "
				+ "group by ide_gtemp, ide_geedp "
				+ "order by ide_gtemp");
		
		if (tabTurnos.getTotalFilas()>0) {
			//System.out.println("update");
			utilitario.getConexion().ejecutarSql("update  asi_horario_mes_empleado set dia"+diaTemp+"="+turno+" where ide_gemes="+mes+" and ide_gtemp="+ide_gtemp+" and ide_geani="+anioTemp+" ");
            guardarPantalla();
            //Procesamiento de horas Extra
            getEmpleado(ide_gtemp,fecha);      
            utilitario.agregarMensajeInfo("Horario actualizado correctamente","");
			
		}else {
	        sel_editar_horario.cerrar();		
	        utilitario.addUpdate("sel_editar_horario");
			utilitario.agregarMensajeInfo("No se puede actualizar el horario","Pongase en contacto con el administrador");
			return;
		}
		
		
	}




	consultarEmpleado();


}
//Validacion Estado Entrada
//public void actualizarEstadoEntrada(AjaxBehaviorEvent evt)
//{}
//Validacion Horario Salida
//public void actualizarHorarioProgramadoSalida(AjaxBehaviorEvent evt)
//{}
//Validacion Horario Salida Timbrado
//public void actualizarHorarioTimbradoSalida(AjaxBehaviorEvent evt)
//{}
//Validacion Horario Estado Salida
//public void actualizarEstadoSalida(AjaxBehaviorEvent evt)
//{}


public String getPos_marcacionEntrada() {
	return pos_marcacionEntrada;
}


public void setPos_marcacionEntrada(String pos_marcacionEntrada) {
	this.pos_marcacionEntrada = pos_marcacionEntrada;
}


public String getPos_marcacionTarde() {
	return pos_marcacionTarde;
}


public void setPos_marcacionTarde(String pos_marcacionTarde) {
	this.pos_marcacionTarde = pos_marcacionTarde;
}


public String getPos_marcacionNocturno() {
	return pos_marcacionNocturno;
}


public void setPos_marcacionNocturno(String pos_marcacionNocturno) {
	this.pos_marcacionNocturno = pos_marcacionNocturno;
}


 
public Date sumarRestarMeses(Date fecha , int meses){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(fecha);
	calendar.add(Calendar.MONTH, meses);
	return calendar.getTime();
	
	
}


public int retornarDiasMes(int dia, int mes,int anio){

Calendar cal = new GregorianCalendar(anio, mes-1, 1); 
	// Get the number of days in that month 
	int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
	//System.out.println(days);
	return days;
	}




public void filtrarTimbradasPorEmpleado(SelectEvent evt){
	aut_empleados.onSelect(evt);
	utilitario.addUpdate("aut_empleados,tab_marcaciones,tab_resumen_marcaciones");
//    actualizarTabla(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),aut_empleados.getValor());	
  	tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+aut_empleados.getValor()+") ");	
    tab_resumen_marcaciones.ejecutarSql();		
    utilitario.addUpdate("tab_resumen_marcaciones,tab_marcaciones,aut_empleados");



}


public AutoCompletar getAut_empleados() {
	return aut_empleados;
}


public void setAut_empleados(AutoCompletar aut_empleados) {
	this.aut_empleados = aut_empleados;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



@SuppressWarnings("unused")
public void getMarcacionesEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani,String  empleadoSeleccionado){
int contador = 0;

	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;

	 String fechaBiometrico="";

	 //Estados de marcaciones
		boolean estadoEntrada=false,estadoTarde=false,estadoNocturno=false;

	 
	
	//Hacer la consulta para poder retornar los horarios de cada empleado
	
    StringBuilder str_ide_horarios=new StringBuilder(); 
    //obtengo todos los turnos con sus horarios
   
    //
    boolean banderaMatriz= false,banderaExtra=false;
   	       	
	String strFechaIniReporte =fechaInicial;
	String strFechaFinReporte = fechaFinal;
	
	
	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
	Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
	Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
	

	//Asigno el tamaño de la  tabla en la cual voy ir guardando los datos de timbre de cada empleado
	
//	Tabla que contiene las fechas de timbre del empleado	
	TablaGenerica tab_reporte=getTablaReporte(empleadoSeleccionado,strFechaIniReporte, strFechaFinReporte);

	if (tab_reporte.isEmpty()) {
		utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
		return;
	}
	
	
	

	
	//Lista con dias y meses seleccionados
	int diferencia_dias=utilitario.getDiferenciasDeFechas(dateFechaInicioReporte,dateFechaFinReporte);
	Date DiaSuma=null;
	//System.out.println("Diferencia dias"+diferencia_dias);
	ArrayList<Integer> listaDiaSeleccionado = new ArrayList<Integer>();
	ArrayList<Integer> listaMesSeleccionado = new ArrayList<Integer>();
	ArrayList<Integer> listaAnioSeleccionado = new ArrayList<Integer>();


	for (int i = 0; i < diferencia_dias+1; i++) {
		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
		utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
		listaAnioSeleccionado.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));

	//System.out.println("Dias"+listaDiaSeleccionado.get(i).toString());
	//System.out.println("Mes"+listaMesSeleccionado.get(i).toString());
	} 
	
	
	
	//Obtengo los ides de los empleados que han timbrado 
  	TablaGenerica tab_reporteEmpleado=getTablaReporteEmpleado(strFechaIniReporte, strFechaFinReporte,empleadoSeleccionado);
  	
	ArrayList<Integer> listaDiasEmpleado = new ArrayList<Integer>();
	ArrayList<Integer> listaMesEmpleado = new ArrayList<Integer>();
	ArrayList<Integer> listaAnioEmpleado = new ArrayList<Integer>();
	for (int itemReporteEmpleado=0; itemReporteEmpleado< tab_reporteEmpleado.getTotalFilas(); itemReporteEmpleado++) { 		
   		//ide de los empleados
		Integer empleado = Integer.parseInt(tab_reporteEmpleado.getValor(itemReporteEmpleado, "IDE_GTEMP"));
		//Consulto las timbradas realizadas del empleado por fecha
		TablaGenerica tab_reporteTimbradasXDia=getTablaReporte(""+empleado,strFechaIniReporte, strFechaFinReporte);
    	 //recorro las timbradas 
    	for (int itemReporteTimbradasXDia=0; itemReporteTimbradasXDia< tab_reporteTimbradasXDia.getTotalFilas(); itemReporteTimbradasXDia++) {
    		//Obtengo el dia Timbrado
    		listaDiasEmpleado.add(utilitario.getDia(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));
    		//Obtengo el mes 
    		listaMesEmpleado.add(utilitario.getMes(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));  
    		listaAnioEmpleado.add(utilitario.getAnio(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"TO_CHAR")));    		

		}
    	/////////////////////////////////////////////////AQUI DEBO VALIDAR CUANDO ES FERIADO
    	
    	for (int i = 0; i < listaDiasEmpleado.size(); i++) {
    		//for recorre la lista de timbradas por empleado
			for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
				//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
				if (listaDiasEmpleado.get(i)==listaDiaSeleccionado.get(j) && listaMesEmpleado.get(i)==listaMesSeleccionado.get(j) && listaAnioEmpleado.get(i)==listaAnioSeleccionado.get(j)) {
					//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
					listaDiaSeleccionado.remove(j);
					//elimino de la lista el dia que si tenga timbrada y asignado
					listaMesSeleccionado.remove(j);
					//elimino el mes que corresponde a esa fecha con su timbrada}
					listaAnioSeleccionado.remove(j);

					j=listaDiaSeleccionado.size();
					//variable para pasar a una nueva timbrada si esta cumple con comparacion 
				}
			}
		}
    	
    	
   
    	//Lista de dias seleccionados pulida sin marcacion pero asignados
    	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
    		//for que recorre la lista final de dias sin asignar
    		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
    				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+listaAnioSeleccionado.get(i).toString());
    		
    		
    		//Obtengo la matriz de la persona
  if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
	  //Tiene Matriz
//	utilitario.agregarMensaje("sI CONTIENE MATRIZ", "");
	String diaMatrizMensual="";

	//////FERIADO
	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
	//pulo por dias la matriz
	if (diaMatrizMensual==null || diaMatrizMensual.isEmpty() || diaMatrizMensual.equals("")) {
	diaMatrizMensual="";	
	//Para que no me ingrese varaias veces el dia que no se tiene asignado
	//	System.out.println("SIN ASIGNACION");
		listaDiaSeleccionado.remove(i);
		listaMesSeleccionado.remove(i);
		listaAnioSeleccionado.remove(i);

		i=-1;
	}else {
	    	    
	    	}
  	
}else {
	i=listaDiaSeleccionado.size();
}
  
  
  
   
		}
    	
     	
    	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
    		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
    				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+listaMesSeleccionado.get(i).toString());

	String diaMatrizMensual="";
	
	if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
		
	

	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
	
	 	//	System.out.println("ASIGNADO"+diaMatrizMensual);
		/////Inserto la fila 
		
		
		 TablaGenerica tabObternerTurnos=this.obtenerTurnos(pckUtilidades.CConversion.CInt(diaMatrizMensual));
		//guardo el ide_del horario
	     int ide_ashor= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
   		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(ide_ashor);
	
   			String horaInicioEmpl = horariosEmpleadoMes.getValor( "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
   			String horaFinEmpl = horariosEmpleadoMes.getValor("HORA_FINAL_ASHOR");// la hora final del horario erp
   			String horaIniAlmEmpl = horariosEmpleadoMes.getValor( "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
   			String hora1="",hora2="";
   			if (horaIniAlmEmpl==null || horaIniAlmEmpl.equals("") || horaIniAlmEmpl.isEmpty()) {
   				horaIniAlmEmpl="";
			}
			
			
			
   			String horaFinAlmEmpl = horariosEmpleadoMes.getValor( "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
   			if (horaFinAlmEmpl==null || horaFinAlmEmpl.equals("") || horaFinAlmEmpl.isEmpty()) {
   				horaFinAlmEmpl="";
   			}

  			String tiempoAlmEmpl = horariosEmpleadoMes.getValor( "MIN_ALMUERZO_ASHOR");//hora inicio erp
   			if (tiempoAlmEmpl==null || tiempoAlmEmpl.equals("") || tiempoAlmEmpl.isEmpty()) {
   				tiempoAlmEmpl="";
   			}
   			if (tiempoAlmEmpl.equals("0")) {
   				horaFinAlmEmpl="S/A";
   				horaIniAlmEmpl="S/A";
   				hora1="S/A";
   				hora2="S/A";
			}

   			//Validacion del dia que contenga 
   			
   			
   			TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+listaAnioSeleccionado.get(i).toString());
   			String anioActual=tabAnio.getValor("detalle_geani");
   			String fecha_evento_cobmr="";
   			if (listaDiaSeleccionado.get(i).intValue()<10) {
   				fecha_evento_cobmr= anioActual+"-"+listaMesSeleccionado.get(i)+"-0"+listaDiaSeleccionado.get(i).toString();
			}else {
   				fecha_evento_cobmr= anioActual+"-"+listaMesSeleccionado.get(i)+"-"+listaDiaSeleccionado.get(i).toString();

			}
  			
  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
  			 //Validacion fecha feriado
  			 if (banderaFeriados==true) {
				//Si se tiene feriado ese dia no se ingrese
			}else if(getEmpleadoActivo(empleado)==false){
				
			}
  			 
  			 
  			 else {
				 //Si no es un dia fericado que se ingrese como no timbrada
				 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		
		         }
  			
    	}else {
    		i=listaDiaSeleccionado.size();	
		}
    	
	
    	}
	
    	listaDiaSeleccionado = new ArrayList<Integer>();
    	listaMesSeleccionado = new ArrayList<Integer>();
    	listaDiasEmpleado = new ArrayList<Integer>();
    	listaMesEmpleado =  new ArrayList<Integer>();
//Reinicio la matriz de dia selecionados
    	for (int i = 0; i < diferencia_dias+1; i++) {
    		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
    		utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
    		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
    		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
	//	System.out.println("Dias"+listaDiaSeleccionado.get(i).toString());
	//	System.out.println("Mes"+listaMesSeleccionado.get(i).toString());
		} 
    	      	     	
	}
	
/**
 * Metodo de asignacion de dias	
 */
	
	
	
	///rESULTADO CORRECTO
/*	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
		System.out.println("lista Resultado"+listaDiaSeleccionado.get(i).toString()+"  mes: "+listaMesSeleccionado.get(i).toString());
		
	}*/
	
/////////////////////////////////////////////////////////////////////////////////////////////
	String horaInicioEmpl ="";
		String horaFinEmpl = "";
		String horaIniAlmEmpl ="";
		String horaFinAlmEmpl = "";
	
	for (int itemReporte=0; itemReporte< tab_reporte.getTotalFilas(); itemReporte++) {
   		Integer Entrada=0;
   		Integer Salida=0;
   		Integer AlmuerzoEntrada=0;
   		Integer AlmuerzoSalida=0;
   	 //Obtengo el ide del empleado con el cual obtendrenmos los horarios
           boolean banderaSinAccion=false;
           Integer extraMatriz=0;
      		Integer empleado = Integer.parseInt(tab_reporte.getValor(itemReporte, "IDE_GTEMP"));
   		//Integer empleado = 46;
   		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(itemReporte,"TO_CHAR");
   		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
			//Tabla guarda los busca y devuelve el turno de tipo matriz si se le ha asignado 
   		TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR=1 and ACTIVO_GTEMP=TRUE");
   		//variable donde se guarda el ide del turnomatriz
   		int turnoMatriz=0;
   		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
   			turnoMatriz=0;
			}else {
				turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
			} 
   		
   		
   		//Obtengo si tiene matriz
   		int mes=0;
   		mes=utilitario.getMes(fechaBiometricoAgrupadaXEmpleado);
   		int anioTemp=0;
   		anioTemp=utilitario.getAnio(fechaBiometricoAgrupadaXEmpleado);
   		TablaGenerica tab_Anio=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+anioTemp+"%'");
   		int anio=Integer.parseInt(tab_Anio.getValor("IDE_GEANI"));
   		int dia=0;
   		dia=utilitario.getDia(fechaBiometricoAgrupadaXEmpleado);
   		
   		//Variables para asignacion Entrada,Salida,Almuerzo Entrada, Almuerzo Salida
   		
   		
   			
   		Integer tipo1= 0;
   		Integer tipoNocturno= 0;

   		
   		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
   		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
   				+ "and ide_gemes="+mes+" and ide_geani="+anio);
   		

        
            //Obtengo el turno por  dia,mes,anio,empleado 
   		int contieneDiaTurno=0;
   		 contieneDiaTurno=getHorarioXDia(empleado,dia,mes,anio);
   		
   		//Variable que guarda 0 si no hubo aignacion de turno el dia anterior y diferente de cero los que si tiene turno de tipo nocturno el  dia anterior 
            int contieneTunnoNocturnoAnterior=0;
            int dia_anterior=utilitario.getDia(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado), -1)));
            //devuelve el turno asignado ese dia 
            contieneTunnoNocturnoAnterior=getHorarioXDia(empleado,dia_anterior,mes,anio);
   
		     //////////////////////////////////////FESTIVOS Y DESIGNACION////////////////////////////////////////////////////////

            //Verifico para ese dia si tiene asignada el dia festivo 
   		     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
			     
   		     //boolean banderaTurnoDesignado= getTurnoDesignadoFeriado(fechaBiometricoAgrupadaXEmpleado);
			    //Variable devuelve si contiene asignado el dia en la asi_novedad como feriado
		/*	     if (banderaFeriados) {
			    	 //Si tengo esa variable le asigno ese horario asigno a ese dia ese horario
			    	
			    	 contieneDiaTurno= getTurnoDesignadoFeriado(fechaBiometricoAgrupadaXEmpleado);
				}
			     

			     //////////////////////////////////////FESTIVOS Y DESIGNACION////////////////////////////////////////////////////////
			     
			   //Verifico para ese dia si tiene asignada el dia festivo o cambio de turno por designacion
   		     banderaCambioHorario=getCambioHorario(fechaBiometricoAgrupadaXEmpleado);
			    //Variable devuelve si contiene asignado el dia en la asi_novedad
			     if (banderaCambioHorario) {
			    	 //Si tengo esa variable le asigno ese horario
			    	//obtengo el turno 0 si no tiene y diferente de 0 si contiene asignacion
				      //turno= getTurnoDesignadoConsulta(fechaBiometricoAgrupadaXEmpleado);
			    	 contieneDiaTurno= getTurnoDesignadoCambioHorario(fechaBiometricoAgrupadaXEmpleado);
				}
			     
			     
			     //////////////////////////////////////PERMISO ONLINE/////////////////////////////////////////////////////////////
			    
				//Verifico para ese dia si tiene asignada el dia festivo o cambio de turno por designacion
   		     banderaCambioHorarioPermisoOnline=getCambioHorarioPermisoOnline(fechaBiometricoAgrupadaXEmpleado);
			    //Variable devuelve si contiene asignado el dia en la asi_novedad
			     if (banderaCambioHorarioPermisoOnline) {
			    	 //Si tengo esa variable le asigno ese horario
			    	//obtengo el turno 0 si no tiene y diferente de 0 si contiene asignacion
				      //turno= getTurnoDesignadoConsulta(fechaBiometricoAgrupadaXEmpleado);
			    	 contieneDiaTurno= getTurnoDesignadoPermisoOnline(fechaBiometricoAgrupadaXEmpleado);
				}
			     
			     
			*/     
			     
            
   		     
   		     
   		 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   		     
   		     
   		     
   		//Si tiene matriz
				if (tabEmpleadoMatrizMensual.getTotalFilas()!=0   && tabEmpleado.getTotalFilas()==0 ) {
					
					
					//permiso online
				
					TablaGenerica tabTipoTurno=null;			
if (contieneDiaTurno==0) {
ide_gtgre=0;
}else {
	//Si tiene asignado matriz para ese dia
  tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
		if (ide_gtgre==4) {
			contieneDiaTurno=0;
		}
}
	    	        
	    	        
	    	        
	    	        
	    	        
	    	      TablaGenerica tabTipoTurnoNocturno=null;
	    	    	if (contieneTunnoNocturnoAnterior==0) {
	    	        	estado_empleado_nocturno=false;

				}else {
		
					//Si tiene asignado matriz para ese dia
	    	         tabTipoTurnoNocturno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneTunnoNocturnoAnterior+" and ide_gtgre=3");
				if (tabTipoTurnoNocturno.getTotalFilas()==0) {
  	    	         estado_empleado_nocturno=false;

				}else {
  	    	         estado_empleado_nocturno=true;
					
				}	
	    	         

				}
	    	        
	    	        
	    	        
	    	   
	    	        
	    	       
		          //Obtengo el horario
	       		 TablaGenerica tabObternerTurnos=this.obtenerTurnos(contieneDiaTurno);
//guardo el ide_del horario
	    	        int ide_ashor= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
	    	        
	    	     
	    	        
	    	        
	        		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(ide_ashor);

	        		//para turno nocturno
	        	//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
	        	//	String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
	        	//	String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
	        		//TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
	        	//	String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
	        		 
	        		
	        		
	        		
	        		
	        		int banderaHoraExtra1001=0;
	    			    banderaFeriados=false;

	        		 
	    		    Integer tipo2 = 0;
	    		    Integer tipo3 = 0;
	    		    Integer tipo4= 0;
	    		    Integer tipo5 = 0;
	    		    Integer tipo6 = 0;
	    		    Integer tipo7 = 0;
	    			Integer tipo1SinAlmuerzo= 0;
	    		    Integer tipo2SinAlmuerzo = 0;
	    		    Integer tipo3SinAlmuerzo = 0;
	    		    Integer tipo4SinAlmuerzo= 0;
	    		    String  SINDATO="";
	    		    int cont=0;
	    	    
	    		    int forHorarios=0;
	    		    forHorarios = horariosEmpleadoMes.getTotalFilas();
	    		    if (contieneDiaTurno==0 && forHorarios==0) {
	    		    	forHorarios=1;
	    			}
	        		//Recorro los horarios 
	        		for (int i=0; i < forHorarios; i++) {
	        				List<Fila> biometrico;
	            			 String horaBiometrico="";
	            			//variable para calculo de tiempo a justificar
	            			 String fechaHoraBiometrico;
	            			double tiempoAlmuerzo=0.0;
	        		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
	        		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
	             			double tiempoDiferenciaEntradaEmpleado=0.0;
	             			double tiempoDiferenciaSinEntradaEmpleado=0.0;

	             			
	         				//Para creacion de inconvenientes
	         				String entradaSinJustificacion="";
	             			String almuerzoSinEntradaJustificacion="";
	             			String salidaSinJustificacion="";

	         			 	String marcacion1="";
	         			 	String marcacion="";

	         			    banderaHoraExtra1001=0;
	             			         			    
	         			     
	         			    
	         				//Verifico si es dia feriado    
	   	     			   banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
	   	     	            
	   	     	         
	   	     	        int tipoEntrada1=0,tipoEntrada2=0;
	   	     	            
	   	     	            //si es de tipo nocturno
	   	     	            
	   	     	        if (ide_gtgre!=3) {
	   	     			//Obtengo los timbres realizados por un empleado 
	   	     	        	
	   	     	        	
	   	     	      boolean a=estado_empleado_nocturno;
	   	     	        	
	   	     	        	
	   	     	        	
	 	      	          biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

	 	      	          
	 	      	          
	 	      	          
	 	      	          
	 	      	          
	 	      	          
						}else {
	        				List<Fila> biometricoTemporal;

							TablaGenerica tabTurnosHorario=utilitario.consultar("select * from asi_turnos_horario where ide_astur="+contieneDiaTurno);
							TablaGenerica tab_horario=utilitario.consultar("select * from asi_horario where ide_ashor="+tabTurnosHorario.getValor("IDE_ASHOR")); 
							String horaEntrada=tab_horario.getValor("hora_inicial_ashor");
							String horaSalida=tab_horario.getValor("hora_final_ashor");
														
														
														
							//Sumo un dia  a la fecha de entrada							
							Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
							sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
							sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
										    			
							
							String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
							String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
										    			
										    						
														
														
							//Parametro hasta para el sql de la consulta cuando es por turno nocturno
			    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
			    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
			    		
			    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
			    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
			    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
			    			
			    			
			    			//suma una hora a la fecha inicial  22:00:00
			    			String horaFechaFinConsulta= diaInicio+" "+horaEntrada;    			
			    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-3));
			    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
			    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
			    			// 
	         				
	         				//suma una hora a la fecha final 
			    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaSalida;    			
			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
			    			biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			
			    			cont=0;
			    			
			    			if (contieneDiaTurno==0) {
						
			    			biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));  			
			    			numMarcacionesBiometrico=biometricoTemporal.size();
			    			if (numMarcacionesBiometrico>1) {
			    				
			    				//numero de marcaciones
			    				int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
			    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
							
			    				
			    				String timbreMarcacion="";
							String 	horaBiometricoTemp="";
							for (Fila bioTemp : biometricoTemporal) {
							//hora de la tabla biometrico
  	         				horaBiometricoTemp = (String)bioTemp.getCampos()[2];
  	         				
  	         				if (horaBiometricoTemp.compareTo(horaDiurno)>0 && horaBiometricoTemp.compareTo(horaTarde)<=0) {
  			    				//estadoEntrada=true;
  			    				//estadoTarde=false;
  			    				//estadoNocturno=false;
  			    				contadorDiurno++;
	  	         				pos_marcacionEntrada=horaBiometricoTemp;
  			    				
							}else if (horaBiometricoTemp.compareTo(horaTarde)>0 && horaBiometricoTemp.compareTo(horaTardeTope)<=0) {
								//estadoEntrada=false;
  			    				//estadoTarde=true;
  			    				//estadoNocturno=false;
  			    				
  			    				contadorTarde++;
  			    				pos_marcacionTarde=horaBiometricoTemp;
							}else if (horaBiometricoTemp.compareTo(horaTardeTope)>0 && horaBiometricoTemp.compareTo(horaNocturno)<=0) {
								//estadoEntrada=false;
  			    				//estadoTarde=false;
  			    				//estadoNocturno=true;
  			    				pos_marcacionNocturno=horaBiometricoTemp;
  			    				contadorNocturno++;
							}	
  	         				
  	         				
  	         				
							
							}
							

							
							
							
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////							
							
			    				 //biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));  			
		    					
			    			 	
			    					int mesTempDiaSiguiente=0,diaTempDiaSiguiente=0,anioDiaSiguiente=0,anioTempDiaSiguiente=0;
			    		       		String diaDiaSiguiente="",mesDiaSiguiente="";
			    		       		diaTempDiaSiguiente=utilitario.getDia(fechaFinReporte);
			    		  		
			    		  		
			    		       		if (diaTempDiaSiguiente<10) {
			    		  			diaDiaSiguiente="0"+diaTempDiaSiguiente;
			    		       		}else {
			    		  			diaDiaSiguiente=""+diaTempDiaSiguiente;
			    		       		}
			    		  		
			    		  	   		  		
			    		  		
			    		       		mesTempDiaSiguiente=utilitario.getMes(fechaFinReporte);
			    		       		if (mesTempDiaSiguiente<10) {
			    		  			mesDiaSiguiente="0"+mesTempDiaSiguiente;
			    		       		}else {
			    		  			mesDiaSiguiente=""+mesTempDiaSiguiente;
			    		       		}
			    					
			    					
			    					
			    		       		//Obtengo si tiene matriz
			    		       		//int mesDiaSiguiente=0;
			    		       		//mes=utilitario.getMes(fechaFinReporte);
			    		       		//int anioTempDiaSiguiente=0;
			    		       		//anioTempDiaSiguiente=utilitario.getAnio(fechaFinReporte);
			    		       		TablaGenerica tab_AnioDiaSiguiente=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fechaFinReporte)+"%'");
			    		       		anioDiaSiguiente=Integer.parseInt(tab_AnioDiaSiguiente.getValor("IDE_GEANI"));
			    		      	       		
			    		       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
			    		       		TablaGenerica tabEmpleadoMatrizMensualDiaSiguiente = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
			    		       				+ "and ide_gemes="+mesDiaSiguiente+" and ide_geani="+anio);
			    		       		

			    		            
			    		                //Obtengo el turno por  dia,mes,anio,empleado 
			    		       		int contieneDiaTurnoDiaSiguiente=0;
			    		       		 contieneDiaTurnoDiaSiguiente=getHorarioXDia(empleado,Integer.parseInt(diaDiaSiguiente),Integer.parseInt(mesDiaSiguiente),anioDiaSiguiente);
			    		     
			    		       		 

			     					TablaGenerica tabTipoTurnoDiaSiguiente=null;			
			     					int ide_gtgreDiaSiguiente=0;
			     					if (contieneDiaTurnoDiaSiguiente==0) {
			     						ide_gtgreDiaSiguiente=0;
			     					}else {
			     						//Si tiene asignado matriz para ese dia
			     						tabTipoTurnoDiaSiguiente =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurnoDiaSiguiente);
			     						ide_gtgreDiaSiguiente=Integer.parseInt(tabTipoTurnoDiaSiguiente.getValor("IDE_GTGRE").toString());
			     					      }
			     	    	        
			     	    	        
			     	    	        int turnoDiaSiguiente=0;
			     	    	    if (ide_gtgreDiaSiguiente==1) {
			     	    	    	turnoDiaSiguiente=1;
							}else if (ide_gtgreDiaSiguiente==2) {
								turnoDiaSiguiente=2;
							}else if (ide_gtgreDiaSiguiente==3) {
								turnoDiaSiguiente=3;
							}		
							else {
								turnoDiaSiguiente=0;
							}    
			     	    
							
							
							
							
							
							
							
							
							
							
							
							
							
							
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
							
			
		    		       	if (turnoDiaSiguiente!=0 ) {
			    				if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno>0) {

										if (turnoDiaSiguiente==3) {
										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
									
											pos_marcacionTarde=horaActualizarPegado;
											
										}
								
	  										
										
									}else if (contadorDiurno>0 && contadorTarde==0  && contadorNocturno>0) {
										
										if (turnoDiaSiguiente==3) {
											//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
										
											horaActualizarPegado=pos_marcacionEntrada;
											}
									
										
									}else if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno==0) {
										if (turnoDiaSiguiente==3) {
	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
											
											horaActualizarPegado=pos_marcacionTarde;
	  											
	  										}
										
										
									}else if (contadorDiurno==0 &&  contadorTarde>0 && contadorNocturno>0) {
										if (turnoDiaSiguiente==3) {
	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
											
											horaActualizarPegado=pos_marcacionTarde;
	  											
	  										}
		       		
			    		       		 }		
								else if (contadorDiurno==0 && contadorTarde>0 && contadorNocturno>0) {
									if (turnoDiaSiguiente==3) {
	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
											
										horaActualizarPegado=pos_marcacionTarde;
	  											
	  										}

									
								}
									
									
									
									
									else {
///////////////////////////////////////////////////////////////////////////////////////Si no tiene turno al dia siquiente///////////////////////////////////////////////////////////////////////////////////
			    		       			 
			    		       		
										
										///////////////////////////ANALIZAR LOS CASOS
										
									/*	if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno>0) {
	  											pos_marcacionNocturno="";
	  											
	  									}else if (contadorDiurno>0 && contadorTarde==0  && contadorNocturno>0) {
	  										
	  											pos_marcacionNocturno="";
	  								  	  									
	  				 				     }else if (contadorDiurno>0 &&  contadorTarde>0 && contadorNocturno==0) {
	  	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
	  											
	  	  											pos_marcacionTarde="";
	   		       		
	  			    		       		 }		
										else if (contadorDiurno==0 && contadorTarde>0 && contadorNocturno>0) {
	  	  											pos_marcacionTarde="";
	  	  											
	  	  														
				    				
			    		       		 }
			    		       		 
										
										
										else if (contadorDiurno>0 && contadorTarde==0 && contadorNocturno=0) {
  											pos_marcacionTarde="";
  											
  														
		    				
	    		       		 }*/
			    		       		 
			    		       		 
		    		       	}
			    		       		 
		    		    
		    		       	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		    		       	
		    		       	
		    		       	
		    		       	
		    		       	
		    		      
			    				
						}else if (numMarcacionesBiometrico==1) {
							
							
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							
							
							

							//numero de marcaciones
			    				int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
			    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
							
			    				
			    				String timbreMarcacion="";
							String 	horaBiometricoTemp="";
							for (Fila bioTemp : biometricoTemporal) {
							//hora de la tabla biometrico
  	         				horaBiometricoTemp = (String)bioTemp.getCampos()[2];
  	         				}
  	         				if (horaBiometricoTemp.compareTo(horaDiurno)>0 && horaBiometricoTemp.compareTo(horaTarde)<=0) {
  			    				//estadoEntrada=true;
  			    				//estadoTarde=false;
  			    				//estadoNocturno=false;
  			    				contadorDiurno++;
  			    				horaBiometricoTemp=pos_marcacionEntrada;
  			    				
							}else if (horaBiometricoTemp.compareTo(horaTarde)>0 && horaBiometricoTemp.compareTo(horaTardeTope)<=0) {
								//estadoEntrada=false;
  			    				//estadoTarde=true;
  			    				//estadoNocturno=false;
  			    				
  			    				contadorTarde++;
  			    				horaBiometricoTemp=pos_marcacionTarde;
							}else if (horaBiometricoTemp.compareTo(horaTardeTope)>0 && horaBiometricoTemp.compareTo(horaNocturno)<=0) {
								//estadoEntrada=false;
  			    				//estadoTarde=false;
  			    				//estadoNocturno=true;
								horaBiometricoTemp=pos_marcacionNocturno;
  			    				contadorNocturno++;
							}	
  	         				
  	         				
  	         				
							
							
							

							
							
							
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////							
							
			    				 //biometricoTemporal = obtenerTimbreBiometrico(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));  			
		    					
			    			 	
			    					int mesTempDiaSiguiente=0,diaTempDiaSiguiente=0,anioDiaSiguiente=0,anioTempDiaSiguiente=0;
			    		       		String diaDiaSiguiente="",mesDiaSiguiente="";
			    		       		diaTempDiaSiguiente=utilitario.getDia(fechaFinReporte);
			    		  		
			    		  		
			    		       		if (diaTempDiaSiguiente<10) {
			    		  			diaDiaSiguiente="0"+diaTempDiaSiguiente;
			    		       		}else {
			    		  			diaDiaSiguiente=""+diaTempDiaSiguiente;
			    		       		}
			    		  		
			    		  	   		  		
			    		  		
			    		       		mesTempDiaSiguiente=utilitario.getMes(fechaFinReporte);
			    		       		if (mesTempDiaSiguiente<10) {
			    		  			mesDiaSiguiente="0"+mesTempDiaSiguiente;
			    		       		}else {
			    		  			mesDiaSiguiente=""+mesTempDiaSiguiente;
			    		       		}
			    					
			    					
			    					
			    		       		//Obtengo si tiene matriz
			    		       		//int mesDiaSiguiente=0;
			    		       		//mes=utilitario.getMes(fechaFinReporte);
			    		       		//int anioTempDiaSiguiente=0;
			    		       		//anioTempDiaSiguiente=utilitario.getAnio(fechaFinReporte);
			    		       		TablaGenerica tab_AnioDiaSiguiente=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fechaFinReporte)+"%'");
			    		       		anioDiaSiguiente=Integer.parseInt(tab_AnioDiaSiguiente.getValor("IDE_GEANI"));
			    		      	       		
			    		       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
			    		       		TablaGenerica tabEmpleadoMatrizMensualDiaSiguiente = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
			    		       				+ "and ide_gemes="+mesDiaSiguiente+" and ide_geani="+anio);
			    		       		

			    		            
			    		                //Obtengo el turno por  dia,mes,anio,empleado 
			    		       		int contieneDiaTurnoDiaSiguiente=0;
			    		       		 contieneDiaTurnoDiaSiguiente=getHorarioXDia(empleado,dia,mes,anio);
			    		     
			    		       		 

			     					TablaGenerica tabTipoTurnoDiaSiguiente=null;			
			     					int ide_gtgreDiaSiguiente=0;
			     					if (contieneDiaTurnoDiaSiguiente==0) {
			     						ide_gtgreDiaSiguiente=0;
			     					}else {
			     						//Si tiene asignado matriz para ese dia
			     						tabTipoTurnoDiaSiguiente =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurnoDiaSiguiente);
			     						ide_gtgreDiaSiguiente=Integer.parseInt(tabTipoTurnoDiaSiguiente.getValor("IDE_GTGRE").toString());
			     					      }
			     	    	        
			     	    	        
			     	    	        int turnoDiaSiguiente=0;
			     	    	    if (ide_gtgreDiaSiguiente==1) {
			     	    	    	turnoDiaSiguiente=1;
							}else if (ide_gtgreDiaSiguiente==2) {
								turnoDiaSiguiente=2;
							}else if (ide_gtgreDiaSiguiente==3) {
								turnoDiaSiguiente=2;
							}		
							else {
								turnoDiaSiguiente=0;
							}    		
							
							
							
			     	    	    
			     	    	    
							
			     	       	if (turnoDiaSiguiente!=0 ) {
			    				if (contadorDiurno==1 && contadorTarde==0 && contadorNocturno==0) {

										if (turnoDiaSiguiente==3) {
											horaActualizarPegado=pos_marcacionEntrada;
										}
								
	  										
										
									}else if (contadorDiurno==0 & contadorTarde==1  && contadorNocturno==0) {
										
										if (turnoDiaSiguiente==3) {
											horaActualizarPegado=pos_marcacionTarde;
											}
									
										
									}		
		    		       	
			    			
									
									}else {
									
										
										
		  			    				if (contadorDiurno==1 && contadorTarde==0 && contadorNocturno==0) {
		  			    				if (turnoDiaSiguiente==3) {
	  										//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
		  			    				horaActualizarPegado=pos_marcacionEntrada;
	  										}
	  								  	  										
		  			    				}

		  			    				if (contadorDiurno==0 && contadorTarde==1 && contadorNocturno==0) {
		  			    				if (turnoDiaSiguiente==3) {
		  			    				horaActualizarPegado=pos_marcacionTarde;
	  										}
	  			    				}
									
										
								}
							
													
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
														
						}
			    			

						
			    			
						}//Cierre de else de  horario tipo  nocturno
			    			
			    			

						
						
			    			
						
						
						
						}
	   	     	        
	   	     	        
	   	     	        
	   	     	        
	   	     	        
	   	     	        
	   	     	        
	   	     	        

	      	         //Si tiene timbradas en el biometrico 
	      	           if (biometrico.size()!=0) {
			
	      	           
	      	     	boolean bandEntradaSinAlmuerzo=false;
       			
	         			int banderaAlmuerzo=0;
	         			int banderaAlmuerzoEntrada=0;
	         			int banderaEntrada=0;
	         			//recorro todos las timbradas
	         			for (Fila bio : biometrico) {
	         				//fecha de la tabla biometrico
	         				fechaBiometrico = (String)bio.getCampos()[1];
	         				//Hora de la tabla biometrico
	         				horaBiometrico = (String)bio.getCampos()[2];
	         				//Evento de acuerdo a si es entrada o almuerzo
	         			//	String evento = (String)bio.getCampos()[3];
	         				//Unimos la fecha con la hora
	         				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
	         			    //Calendario
	         				
	         				
	         				
	         				Calendar calFechaHoraBiometrico = Calendar.getInstance();
	         			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	         				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
	         				
	         			
	         			
	         				
	     			
	         				boolean t=false;
	         				//si contiene un turno asignado a ese dia  asignado en la matriz
	         				if (contieneDiaTurno!=0 ) {
			        				// TURNO SI NO ES DE TIPO NOCTURNO
	         					if (ide_gtgre!=3 && banderaFeriados==false) {
	         					
	         				    horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	         					horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	         					horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
	         					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
	         					
	         					
	         				
	         					 
	         					//Hora Inicial tomada del horario
	         					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
	             			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
	             			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
	             			    
	             			    //Suma de minuto para el ingreso
	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
	             			    
	             			     
	             			    
	             			    //Hora desde Inicio de Entrada de Horario
	             			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio),-8));
	           	    		     Calendar calHoraFechaBase= Calendar.getInstance();
	           	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporteBase));
	         			
	             			    
	         					
	         					//Hora Final tomada del horario
	         					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
	             			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	             			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

	             			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
	             			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
	             			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
	             			    calFechaHoraHorarioIniAlmuerzo.add(Calendar.MINUTE, -15);	    
	             			    
	             			    
		   	         		String fechaHoraHorarioInicial = fechaBiometrico+" "+horaFinAlmEmpl;//cogo la hora y le concateno con la fecha del horario
		   	         		Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
		   	         		calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicial));
		   	         		calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    

		   	                  		
		   	         		
		   	         		
		   	         		
		   	         		
		   	         		
		   	         			    
	             			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
	             			   Date finAlmuerzo=null;
	             			   
	             			   
			   	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);

	             			   Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
			   	         			if (tiempoAlmuerzoHorario==0.5) {
			   	         	
	   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
	   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 30);
								}else if (tiempoAlmuerzoHorario==1) {
									
			   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
								}
			   	         		
	             			   
	             			   
	             			   
	             			  /* 
	             			    
	             			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
	             			  if ((int)tiempoAlmuerzo==1) {
	               			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
	             			  							  }else{
	               			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
	             			  							  		}
	         					
	             			    //Sumamos una hora para el almuerzo
	             			    fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(finAlmuerzo);
	             			    Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();
	             			    calFechaHoraHorarioFinAlmuerzo.setTime(finAlmuerzo); 

	             			    SSSS
	             		
	       						*/
	       					
	             			    //Condicion si no existe hora de almuerzo
	             			   if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")  ) {
	             				   
	             			       
	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
	                                //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	             			    	
	             			    	if (tipo1==0) {
										
									
	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	                     			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                     			
	                     			
	                 	                 			    
	                     			//Calculo de tiempo de atraso en la entrada del empleado
	             			    	tipo1=1;
	             			    	}
	             			    }
	          
	            				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
	    								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {

	            					if (tipo1==0) {
	    								
	            						
	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	         						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
              			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
              			    	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	         			      		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                     			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	             			       	//tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	         			    		//tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	         			    		//tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                     			//tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
	                     			//tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                 			    
	                 			    tipo1=1;
	            					}else {
	            						
	            						
	            					if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {
	            				   	
	                  			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	               						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                  			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	                 			    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                 				tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	                  			    
	                 			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                 			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	                 			    tipo1=2;
	            						}  
	            					else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) <= 0) {
	                  			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	               						tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
                  			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	            						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	            						    tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
	    	                 			    tipo1=2;
	            					}
	            					
	            					
	    							}
	            					
	            					
	            					
	            					
	            					
	            					
	            					//else de caso
	             			                 			    
	             				}else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) > 0) {
	              			    
	             					if (tipo1==0) {
	              			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	              			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
  	        						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	            							tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	    	        					    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");

	                         			  //  tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	    	                     			    tipo1=4;
	             		
	             					
	             					
	  
	             					}else {
	             					tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	             			        tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	           						tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	                     				tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	            						    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	    	                 			    tipo1=2;
	             				}

	             				}
	             			    
	             			    
	             			    
	             			    //Segundo caso Caso
	             			    
	             			    
	            				/*else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin)>= 0) {
	            					
	            					if (tipo1==0) {
	    								
	            						
	   	             			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	   	             			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	   	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	   	         			    		tipo1=1;
	            					
	            					}else {
	   	             			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	   	                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
										
									}
	            					
	            			
	            			  
								}*/
	             			    //SQT
	             			
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////           		  
				
	             			   }//fin de condicion  si no tiene ALMUERZO
				
	             			   else {
	             				  //Caso contrario si tiene almuerzo
	                			   
	                			    Entrada=6;
	   	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
	                			    
	                			  //  if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
	                			    	if (banderaEntrada==0) {
	                                   //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	                			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	                			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	            			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	                			    
	                			    	//Calculo de tiempo de atraso en la entrada del empleado
	                			    	tipo1=2;
	                			    	Entrada=1;
	                			    	banderaEntrada=1;
	                			    	}
	                			    }
	             
	               				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
	       								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
	               					if (banderaEntrada==0) {
	                			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
	                			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	            			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	                			    	//tipo1=1;
	                			    	//tipo2=1;
	                			    	tipo1=2;
	                			    	banderaEntrada++;
	                			    	Entrada=1;
	               					}
	                				}
/*
	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) > 0
	                			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinAlmuerzo) < 0){
	                			    	//Almuerzo
	                			    	
	                			    	if (banderaAlmuerzo == 0){
	                                       //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	                    			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	                    			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
	                			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
	                    			    	banderaAlmuerzo++;
	   	                			    	tipo1=2;
	                    			    	//tipo3=1;
	   	                			    	Entrada=2;	
	                    			    	
	                			    	}else{
	                                       //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	                			    		if (banderaAlmuerzoEntrada==0) {
	                			    			tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
  	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  	                    			    	
	       								tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                    			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
	                    			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
	                    			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
	                    			    	tiempoAlmuerzo = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
	                    			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"));
	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                    			    	//Validacion si se pasa el almuerzo
	                    			    	//tipo4=1;
	   	                			    	
	                    			    	AlmuerzoEntrada=1;
	                    			    	Entrada=3;
	                			    		}
	                			    	}
	                    			   	

	                			    }  
	                			  */
	   	             			    

	   	             			else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
	   		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo==0){
	   		       			    	//Almuerzo

	   		       			    	if (banderaAlmuerzo == 0){
	   		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	   		           			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	   		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	   		           			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
	   		       			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ (pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	   	   		      					horaAnterior=horaBiometrico;
	   		           			    	banderaAlmuerzo++;
	   		           			    	tipo3=1;
	   		           			  
	   		       			    	}
	   		       			    
	   		       			    	
	   		       			    	
	   		       			    	
	   		       			    }else if (banderaAlmuerzo==1 && horaBiometrico.compareTo(horaAnterior)>0  && horaBiometrico.compareTo(horaFinEmpl)<0 && banderaAlmuerzoEntrada==0){
	   		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

			
				   	     			String uno= fechaBiometrico+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
				   	         		String dos = fechaBiometrico+" "+horaFinAlmEmpl;
				   	     			
				   	     				Calendar caluno = Calendar.getInstance();//
				   	         			Calendar caldos = Calendar.getInstance();//
				   	         			caluno.setTime(getFechaAsyyyyMMddHHmmss(dos));//
				   	         			caldos.setTime(getFechaAsyyyyMMddHHmmss(uno));//
				   	         			//Suma de minuto para el ingreso
				   	         			
				   	         			if((pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==1){
				   	         			caluno.add(Calendar.MINUTE, 60);	
				   	         			}else if (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))==0.5) {
				   	         			caluno.add(Calendar.MINUTE, 30);	
										}else {
											
										}
				   	         			
				   	         				
	   		       			    	
	   		       			    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
	   	   		       			    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0){
	   	   		       			   	 
	   		       			    	    tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	   		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	   		           			    	
	   		   							tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
	   		           			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
	   		           			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
	   		           			    	tiempoAlmuerzo = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
	   		           			    	
	   		           			    	
	   		           			    	
	   		           			    	
	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
	   		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	   		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
			       			
	   		           			    	//Validacion si se pasa el almuerzo
	   		           			    	tipo4=1;
	   		           			    	banderaAlmuerzoEntrada++;
	   		           			    	horaAnterior="";
	   		       			    		}
	   		       			    	}
	   		           			   	

	   		       			      
	   	             			    
	   	             			    
	   	             			    
	   	             			    
	                			    
	                			   
	                			    
	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
	                			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
	                			    	//Salida
	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	                    			   	//tipo5=1;
	                    			   	//tipo6=0;
	   	                			    	tipo1=2;
	   	                			    	
	   	                			    	
	   	                			    	

	                    			   	Salida=1;
	                    			   	Entrada=4;
	                			    	
	                			    }  	
	                			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
	                                 //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	                			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	            			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	   	                			    	tipo1=2;
	                			    	//tipo6=1;
	                			    	//tipo5=0;
	                    			   	Salida=1;
	                    				Entrada=5;

	                			    }    
	            				
	                			
							}//Fin de ciclo si contiene almuerzo
	             			   
	             			   
	             
	           
				//Si es igual 
	         					}else if(ide_gtgre==3 && banderaFeriados==false && biometrico.size()!=0){ 
	         						//Si es de tipo nocturno	
	         						//System.out.println("TIPO NOCTURNO");
	           						
	           						
	         			
	         						
	         						
	         						
	         						//Ingreso a metodo
	         						cont++;
	           						
	           						int banderaIngreso=0;
	           	    			    int banderaSalida=0;
	           	    				    banderaAlmuerzo=0;
	           	    				    banderaEntrada=0;

	           	    				int tipoEntrada3=0,tipoEntrada4=0;

	           	    				//Horario de acuerdo a los turnos ingrresados por talento humano
	           						horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	           						horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	           						horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
	           						horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp

	           					
	           						
	           						
	           						String HoraHorarioInicio = horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
	           	    			   Calendar  calFechaHoraHorarioInicio = Calendar.getInstance();//
	           	    			    
	           						String HoraHorarioFin = horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
	           						Calendar calFechaHoraHorarioFin = Calendar.getInstance();

	           					
	           	    			Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
	           	    			sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
	           	    			sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
	           	    			
	           	    	
	           	    		
	           	    			String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
	           	    			String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
	           	    			
	           	    		
	           	    			
	           	    			
	           	    			
	           	    			
	           	    			//Parametro hasta para el sql de la consulta cuando es por turno nocturno
	           	    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
	           	    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
	           	    		
	           	    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
	           	    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
	           	    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
	           	    			
	           	    			
	           	    			//suma una hora a la fecha inicial  22:00:00
	           	    			String horaFechaFinConsulta= diaInicio+" "+horaInicioEmpl;    			
	           	    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-1));
	           	    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
	           	    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
	           						
	           	    			//

	           	    			//suma una hora a la fecha final 
	           	    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaFinEmpl;    			
	           	    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
	           	    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
	           	    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
	           						
	           	    			 
	           	    			
	           	    			
	         //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7  	    			
	           	    			
	           	    			
	           	    			
	           	 			//Horario de acuerdo a los turnos ingrresados por talento humano
	        					String horaInicioEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	        					String horaFinEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	        					horaIniAlmEmpl = horariosEmpleadoMes .getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
	        					horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
	        					
	        					
	        					
	        					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl1; //cogo la hora y le concateno con la fecha del horario
	        	    			//String fechaHoraHorarioInicio= getFechaAsyyyyMMddHHmmss(sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio1),Integer.parseInt(minuto_tolerancia_astur)));

	        					
	        					
	        					Calendar calFechaHoraHorarioInicio1 = Calendar.getInstance();//
	            			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));
	            			    
	            			    //Suma de minuto para el ingreso
	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
	        					
	        					 String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
	            			    Calendar calFechaHoraHorarioFin1 = Calendar.getInstance();
	            			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

	            			    
	            			  //Desde hasta puede hora de salida para el horario nocturno
	            			   String fechaHoraHorarioFinSalida = fechaBiometrico+" 19:00:00";
	            			    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
	            			    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
	        					
	            			    String fechaHoraHorarioInicioFinSalida = fechaBiometrico+" 04:00:00";
	            			    Calendar calFechaHoraHorarioInicioFinSalida = Calendar.getInstance();
	            			    calFechaHoraHorarioInicioFinSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinSalida));
	        					       			    
	        					//Desde hasta puede hora de ingreso para el horario nocturno
	            			    String fechaHoraHorarioFinEntrada = fechaBiometrico+" 24:00:00";
	            			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
	            			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada));
	           			    
	            				String fechaHoraHorarioInicioEntrada = fechaBiometrico+" 00:00:00";
	            			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
	            			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
	           				    
	            			    
	            			    Calendar calSumaDia = Calendar.getInstance();
	            			    calSumaDia.setTime(getFechaAsyyyyMMddHHmmss(horaFechaSumaDia1));
	           				    
	            			    
	            			   //Prueba horario nocturno
	            			    //variable que guarda la hora desde 00:00: del dia siguiente
	            			    String fechaHoraCeroHoras = fechaBiometrico+" 00:00:00";
	            			    Calendar fecHoraInicio = Calendar.getInstance();
	            			    fecHoraInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
	           				    
	            			    
	            			    
	            				//Siguiente
	                			String fecha= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado);
	                			String fechayhora=fecha+" 08:00:00";
	                		    Calendar calFin = Calendar.getInstance();//
	            			    calFin.setTime(getFechaAsyyyyMMddHHmmss(fechayhora));//
	                			
	            			    
	            			  //Siguiente
	                			String fecha1= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
	                			String fechayhora12=fecha1+" 22:00:00";
	                		    Calendar calEntrada = Calendar.getInstance();//
	            			    calEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechayhora12));//
	            			    
	            			    
	            			    
	            			    

	            			  	if (cont==1) {

	            			  		if (biometrico.size()==1) {

	  	            			  		if (biometrico.size()==1) {
	  	            			  	 	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	                  			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	              			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	              				       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	 	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	  	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	 	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	 	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	                  			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                  			    	banderaHoraExtra1001=1;
										}
	  	            			  		
	  	            			  		
	  	            			  		
	  	            			  		
	  	            			  		
	  	            			  		
	            			  		}else{
	            			  			
	            			  
	            			    Integer tipoEvento = 0;
	            			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

	            			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	                    			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	                			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
	                				       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	    	         			    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  	                			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	                    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                    			    	tipo1=1;
	                    			    	tipoEntrada1=1;
	                    			    	tipoEntrada2=0;
	                    			    	tipoNocturno=1;
	                    			    	
	                     
	                    			    }
	            			    
	            				else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0 &&
              			    		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada)<=0){
	                                   
									
									
              			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
              			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
          			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
          				       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	   	         			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	   	            					tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	   	            					tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	   	            					tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
              			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
              			    	tipo1=1;
                  			    tipoEntrada1=1;
              			    	tipoEntrada2=0;
              			    	tipoNocturno=1;
              			   }
	                    			    
	                    			    //mayor a la hora de horario incio con la hora biometrico timbrada
	                			      //menor a la hora 24:00:00
	            			    
	            			  /*  if (tipoEntrada1==0) {
	            			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
          			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "SIN TIMBRE");
              			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
          			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
              			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM","0");
              			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
              			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN TIMBRE");

								}*/
	            			    
	            			 	
	            			  	}	//contador de marcaciones mayor a uno
	            			  		
	            			  	}
	            			 	
	            			 	if (cont>1) {
	                    				
	                     			    //mayor a hora: 0:00:00
	                    			    //menor a las 6 de la manaña	
	                    			     if(calFechaHoraBiometrico.compareTo(fecHoraInicio) >0 &&
	                    			    	     calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)<0){
	                    			    		    	
	                    			       	//tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
	                			    		//tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
	                    			    	 
	                    			    	 
	                			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	                  			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	                  			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
	                  			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	   	   	            					tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	   	   	            					tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	   	   	            					tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	   	   	            				    
	                  			    		if (tipoNocturno==0) {
	                  			    		  tipo1=3;
											}else if(tipoNocturno==1){
	   	   	            				    tipo1=2;}
	                    			    //mayor que las 06:00:00
	                    			    //	y menor que 08:00:00
	                    			    }else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)>=0 
	                    			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)<=0){
	                    			    	
	                 			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
	   	   	         			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	   	   	            				    tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
	   	   	            				    tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
	   	   	            				    tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
	              			    		tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	              			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	              			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
	              			   	if (tipoNocturno==0) {
          			    		  tipo1=3;
									}else if(tipoNocturno==1){
	   	            				    tipo1=2;}

	                    			    }
	        	
	             					
	             					
	                    				}
	             					
	             					
	         						
	         						
	         						
	         						
	         					}
	         					//si es de tipo nocturno pero  no es feriado y tiene marcaciones
	         					//else if(ide_gtgre==3 && banderaFeriados==false && biometrico.size()==0){
	         						
	         					//}
	         					//Fin de ciclo si es tipo ncturno
	         					//si es feriado 
	         					else if (banderaFeriados=true && estado_empleado_nocturno==false && biometrico.size()>0) {
								//si es dia normal y es feriado

	         						
	         						
	         						
	         						
										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
											
										
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				
										
										        				
						 				}
									
									}
						 			    
						 			    
						 			    
						 			    
						 			    
			   	     				
			   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==true && biometrico.size()>0 ) {
			   	     					
			   	     					
			   	     					
			   	     					//Obtengo el dia anterior para comparar con la fecha actual	
			   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
					            
			   	     				
			   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
			   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
			   	     					
			   	     				//Si el dia anterior no era de tipo nocturno
			   	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()==0) {
								
										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
											
										
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				
										
										        				
						 				}
									
									}  			   	     					
			   	     					
			   	     				
			   	     				
			   	     				}
			   	     				else{
			   	     						//Si el empleado y la fecha son iguales 
											//Consulto la hora de salida 
			if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty() )
			{
				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
	 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
	 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
				    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	                	 if (banderaHoraExtra1001==0) {
	 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
				    		banderaHoraExtra1001=1;
				   	    }else {
				        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
	 			    	banderaHoraExtra1001=2;
	 				}
			
				    			}
				    
				    
				    
			//si existe timbradas y no hay horario	    
			}else {
				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
		
											String horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
	  						 			    if (horaBiometrico==horaSalidaAnterior) {
										}else {
	  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	  						                	 if (banderaHoraExtra1001==0) {
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
	  									    		banderaHoraExtra1001=1;
	  									   	    }else {
	  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
	  						 			    	banderaHoraExtra1001=2;
	  						 				}//else de validacion de hora extra
	  						 		}//hora inicio y hora fin
										}//ELSE DE HORA ANTERIOR DE SALIDA	
										}//ELSE DE HORA FIN
								
										
										
										
										
										
										
										}//Else si hay empleado y fecha anteior
			   	     				
			   	     				
			   	     				
			   	     					
			   	     					
			   	 //    				System.out.println("ddddd");	
			   	     				}
	         						
	         						
	         						
	         						
	         						
	         						
	         						
	         						
	         						
								
					
	         			}// Fin de ciclo si contiene turno 
	         				
	         				
	         				
	         				else if (contieneDiaTurno==0) {  					
	         				//si no contiene turno
	         				
	         				
	         			if(biometrico.size()!=0){
	         					if (banderaFeriados==false && estado_empleado_nocturno==false ) {
								
	         				String fechaHoraHorarioFin = getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico),1))+" 24:00:00";//cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

	         			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	         			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	         			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
	         			    
	         			//Obtengo el dia anterior para comparar con la fecha actual	
	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
	            
	     				
	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
         			    
	     				String horaSalidaAnterior="";	
	     				if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
						 horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");

					}
	     				
	     			List<Fila> biometricoTemporalExtra;

					
  				biometricoTemporalExtra = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

	      	          
	      	         

  				boolean estadoEntradaNocturno=false;	
	      	          
	      	          
	      	   	int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
	    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
					
	    				
	    				String timbreMarcacion="";
					String 	horaBiometricoTempExtra="";
					int pos_Extra=0;
					
					for (Fila bioTemp : biometricoTemporalExtra) {
					//hora de la tabla biometrico
         				horaBiometricoTempExtra = (String)bioTemp.getCampos()[2];
         				if (horaBiometricoTempExtra.equals(horaSalidaAnterior)) {
	     			   		pos_Extra=(int)bioTemp.getIndice()+1;
	     			   		estadoEntradaNocturno=true;
         				}
         				
					}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  	         			    
int valor=0;
					if (estadoEntradaNocturno==true) {
					valor=biometrico.size()-pos_Extra;
						}
					
					if (valor>=1 && estadoEntradaNocturno==true) {
	 			    	biometrico.removeAll(biometrico);
	 			    	banderaSinAccion=true;}
	 			    	else if (valor==0 && estadoEntradaNocturno==true) {
	 			       	biometrico.removeAll(biometrico);
	 			    	banderaSinAccion=true;
				
					}else{
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
					
	         			    
	         			   
	         if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) <= 0){
  	   	         			   if (banderaHoraExtra1001==0) {

  		         			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  		         			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  		         			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  		 	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 
  		 	         				banderaHoraExtra1001=1;
  		     			   	    }else {
  									        			    
  		        			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  		         			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  		         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  		         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
  		         			    	banderaHoraExtra1001=2;
  			     				
  		     				
  								        				
  		         				}

  	         			    
  	         			    
  	         			    
  	         			    }
					
	         					}
						
							
						
   	     				

			 			  	         			   
	         				}else if (banderaFeriados==false  && estado_empleado_nocturno==true && biometrico.size()>0) { 
	         						//Cambios
	         					
	         					
								String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
					 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
					 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

					 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
					 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
					 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
										
									
					 				//Obtengo el dia anterior para comparar con la fecha actual	
		   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
				            
		   	     				
		   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
		   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
		   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
	  	         			    
		   	     				String horaSalidaAnterior="";	
		   	     				if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
									 horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");

								}
		   	     				
		   	     				if (horaSalidaAnterior==null || horaSalidaAnterior.equals("") || horaSalidaAnterior.isEmpty()) {

					 			    
					 			    
					 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
					 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
					                	 if (banderaHoraExtra1001==0) {
					 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
					 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
								    		banderaHoraExtra1001=1;
								   	    }else {
										        			    
								        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
					 			    	
					 			    	banderaHoraExtra1001=2;
					 				
									        				
					 				}
								
								}
					 			    
		   	     				}else {
		   	     					
		  	        				List<Fila> biometricoTemporalExtra;

		   	     					
		  	        				biometricoTemporalExtra = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

		    	 	      	          
		    	 	      	         

		  	        				boolean estadoEntradaNocturno=false;	
		    	 	      	          
		    	 	      	          
		    	 	      	   	int contadorDiurno=0,contadorTarde=0,contadorNocturno=0;
		  			    				String horaDiurno="00:00:00",horaTarde="12:00:00",horaTardeTope="18:00:00",horaNocturno="24:00:00";
										
		  			    				
		  			    				String timbreMarcacion="";
										String 	horaBiometricoTempExtra="";
										int pos_Extra=0;
										
										for (Fila bioTemp : biometricoTemporalExtra) {
										//hora de la tabla biometrico
			  	         				horaBiometricoTempExtra = (String)bioTemp.getCampos()[2];
			  	         				if (horaBiometricoTempExtra.equals(horaSalidaAnterior)) {
				   	     			   		pos_Extra=(int)bioTemp.getIndice()+1;
				   	     			   	estadoEntradaNocturno=true;
			  	         				}
			  	         				
										}
										
										
									
										
										
										 
									    if (estadoEntradaNocturno==true) {
						   	     			   
						   	     			   horaBiometrico="";
						   	     			    	banderaSinAccion=true;
									 
										
										
			  	        				biometricoTemporalExtra = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
										
										TablaGenerica tabExtra=obtenerTimbreBiometricoExtra(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
										
										 String  pos_marcacionEntradaExtra="",pos_marcacionTardeExtra="",pos_marcacionNocturnoExtra="",horaActualizarPegadoExtraEntrada="";

										 String  pos_marcacionEntradaExtra1="",pos_marcacionTardeExtra1="",pos_marcacionNocturnoExtra1="",horaActualizarPegadoExtraSalida="";

										 
										 
										
										 
										 
										 
										
										
										
										if ((tabExtra.getTotalFilas()-pos_Extra)>0) {
										
										for (int j = pos_Extra; j < tabExtra.getTotalFilas(); j++) {
											
										String hor="";	
											hor=tabExtra.getValor(j,"HORAM");
											if (hor.compareTo(horaDiurno)>0 && hor.compareTo(horaTarde)<=0) {
			  			    				contadorDiurno++;
			  			    				pos_marcacionEntradaExtra=hor;
			  			    				
										}else if (hor.compareTo(horaTarde)>0 && hor.compareTo(horaTardeTope)<=0) {
											//estadoEntrada=false;
			  			    				//estadoTarde=true;
			  			    				//estadoNocturno=false;
			  			    				
			  			    				contadorTarde++;
			  			    				pos_marcacionTardeExtra=hor;
										}else if (hor.compareTo(horaTardeTope)>0 && hor.compareTo(horaNocturno)<=0) {
											//estadoEntrada=false;
			  			    				//estadoTarde=false;
			  			    				//estadoNocturno=true;
											pos_marcacionNocturnoExtra=hor;
			  			    				contadorNocturno++;
										}	
			  	         				
										}
											
											
											String fechaFinReporte="";
											fechaFinReporte=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(dateFechaInicioReporteAgrupadaXEmpleado,1));
											
											
											int mesTempDiaSiguiente=0,diaTempDiaSiguiente=0,anioDiaSiguiente=0,anioTempDiaSiguiente=0;
		 			    		       		String diaDiaSiguiente="",mesDiaSiguiente="";
		 			    		       		diaTempDiaSiguiente=utilitario.getDia(fechaFinReporte);
		 			    		  		
		 			    		  		
		 			    		       		if (diaTempDiaSiguiente<10) {
		 			    		  			diaDiaSiguiente="0"+diaTempDiaSiguiente;
		 			    		       		}else {
		 			    		  			diaDiaSiguiente=""+diaTempDiaSiguiente;
		 			    		       		}
		 			    		  		
		 			    		  	   		  		
		 			    		  		
		 			    		       		mesTempDiaSiguiente=utilitario.getMes(fechaFinReporte);
		 			    		       		if (mesTempDiaSiguiente<10) {
		 			    		  			mesDiaSiguiente="0"+mesTempDiaSiguiente;
		 			    		       		}else {
		 			    		  			mesDiaSiguiente=""+mesTempDiaSiguiente;
		 			    		       		}
		 			    					
		 			    					
		 			    					
		 			    		       		//Obtengo si tiene matriz
		 			    		       		//int mesDiaSiguiente=0;
		 			    		       		//mes=utilitario.getMes(fechaFinReporte);
		 			    		       		//int anioTempDiaSiguiente=0;
		 			    		       		//anioTempDiaSiguiente=utilitario.getAnio(fechaFinReporte);
		 			    		       		TablaGenerica tab_AnioDiaSiguiente=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fechaFinReporte)+"%'");
		 			    		       		anioDiaSiguiente=Integer.parseInt(tab_AnioDiaSiguiente.getValor("IDE_GEANI"));
		 			    		      	       		
		 			    		       		//Aqui guardo la tabla que contiene la matriz mensual del empleado buscado por mes , anio y empleado como sus parametros.
		 			    		       		TablaGenerica tabEmpleadoMatrizMensualDiaSiguiente = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
		 			    		       				+ "and ide_gemes="+mesDiaSiguiente+" and ide_geani="+anio);
		 			    		       		

		 			    		            
		 			    		                //Obtengo el turno por  dia,mes,anio,empleado 
		 			    		       		int contieneDiaTurnoDiaSiguiente=0;
		 			    		       		 contieneDiaTurnoDiaSiguiente=getHorarioXDia(empleado,Integer.parseInt(diaDiaSiguiente),Integer.parseInt(mesDiaSiguiente),anioDiaSiguiente);
		 			    		     
		 			    		       		 

		 			     					TablaGenerica tabTipoTurnoDiaSiguiente=null;			
		 			     					int ide_gtgreDiaSiguiente=0;
		 			     					if (contieneDiaTurnoDiaSiguiente==0) {
		 			     						ide_gtgreDiaSiguiente=0;
		 			     					}else {
		 			     						//Si tiene asignado matriz para ese dia
		 			     						tabTipoTurnoDiaSiguiente =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurnoDiaSiguiente);
		 			     						ide_gtgreDiaSiguiente=Integer.parseInt(tabTipoTurnoDiaSiguiente.getValor("IDE_GTGRE").toString());
		 			     					      }
		 			     	    	        
		 			     	    	        
		 			     	    	        int turnoDiaSiguiente=0;
		 			     	    	    if (ide_gtgreDiaSiguiente==1) {
		 			     	    	    	turnoDiaSiguiente=1;
										}else if (ide_gtgreDiaSiguiente==2) {
											turnoDiaSiguiente=2;
										}else if (ide_gtgreDiaSiguiente==3) {
											turnoDiaSiguiente=3;
										}		
										else {
											turnoDiaSiguiente=0;
										}    
		 			     	    
										
				    	 	      	   	int contadorDiurno1=0,contadorTarde1=0,contadorNocturno1=0;

										
		 			     	    	  //String pos_marcacionEntradaExtra1="",pos_marcacionTardeExtra1="",pos_marcacionNocturnoExtra1="";
										
										
		 			     	    	  int contadorDiurnoExtra=0,contadorTardeExtra=0,contadorNocturnoExtra=0;
										
		 			     	    	  TablaGenerica biometricoTemporalExtraDiaS=obtenerTimbreBiometricoExtra(empleado, getFechaAsyyyyMMdd(fechaFinReporte), getFechaAsyyyyMMdd(fechaFinReporte));

								for (int k = 0; k < biometricoTemporalExtraDiaS.getTotalFilas(); k++) {
							
									String hor1="";	
										hor1=biometricoTemporalExtraDiaS.getValor(i,"HORAM");
										if (hor1.compareTo(horaDiurno)>0 && hor1.compareTo(horaTarde)<=0) {
		  			    				contadorDiurnoExtra++;
			  	         				pos_marcacionEntradaExtra1=hor1;
			  	         				
									}else if (hor1.compareTo(horaTarde)>0 && hor1.compareTo(horaTardeTope)<=0) {
										//estadoEntrada=false;
		  			    				//estadoTarde=true;
		  			    				//estadoNocturno=false;
		  			    				
		  			    				contadorTardeExtra++;
		  			    				pos_marcacionTardeExtra1=hor1;
									}else if (hor1.compareTo(horaTardeTope)>0 && hor1.compareTo(horaNocturno)<=0) {
										//estadoEntrada=false;
		  			    				//estadoTarde=false;
		  			    				//estadoNocturno=true;
		  			    				pos_marcacionNocturnoExtra1=hor1;
		  			    				contadorNocturnoExtra++;
									}	
		  	         				
									}
										
										
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
										
						//String horaActualizarPegadoExtraEntrada="",horaActualizarPegadoExtraSalida="";
					    		       	if (turnoDiaSiguiente!=0 ) {
					    		       		
					    	
											if (contadorNocturno>0) {
													//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
		  											if (contadorDiurnoExtra>0) {
		  												// horaActualizarPegadoExtraEntrada=
		  												tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
									 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", pos_marcacionNocturnoExtra);
									 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
												        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", pos_marcacionEntradaExtra1);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
									 			    	
									 			    	banderaHoraExtra1001=2;
									 			    	banderaSinAccion=false;
									 			    	bio.setIndice(biometrico.size());
		  													
													}
														
		  	  																			
												//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
		  											if (contadorTardeExtra>0) {
		  												
		  												tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
									 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", pos_marcacionNocturnoExtra);
									 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");

												        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", pos_marcacionTardeExtra1);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
									 			    	
									 			    	banderaHoraExtra1001=2;
									 			    	banderaSinAccion=false;	
									 			    	bio.setIndice(biometrico.size());

													}
														
		  	  										}
												
												
												
												
											}
		  									
		  									
		  									
		  									
		  									else {
		///////////////////////////////////////////////////////////////////////////////////////Si no tiene turno al dia siquiente///////////////////////////////////////////////////////////////////////////////////
		  			    		       			 
		  			    		       		
		  										
		  										///////////////////////////ANALIZAR LOS CASOS
		  										
		  										if (contadorNocturno>0) {
													//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
		  											if (contadorDiurnoExtra>0) {
		  												// horaActualizarPegadoExtraEntrada=
		  												tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
									 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", pos_marcacionNocturnoExtra);
									 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
												        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", pos_marcacionEntradaExtra1);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
									 			    	
									 			    	banderaHoraExtra1001=2;
									 			    	banderaSinAccion=false;
									 			    	biometrico.removeAll(biometrico);
									 			    

		  													
													}
														
		  	  																			
												//	pos_marcacionEntrada,pos_marcacionTarde,pos_marcacionNocturno
		  											if (contadorTardeExtra>0) {
		  												
		  												tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
									 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", pos_marcacionNocturnoExtra);
									 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");

												        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", pos_marcacionTardeExtra1);
									 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
									 			    	
									 			    	banderaHoraExtra1001=2;
									 			    	banderaSinAccion=false;		
									 			    	biometrico.removeAll(biometrico);
									 			    	
													}
														
		  	  										}
		  	  									
		  	  									
		  	  							
		  			    		       		 
					    		       	}

											
											
					    		       	}	///si es nocturno
											
											
								
		    	 	      	           					
		   	     			
		   	     			    	
		   	     			    	
								}else {
									
									
									

								    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) <= 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
									    		banderaHoraExtra1001=1;
									    		banderaSinAccion=false;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 			    	banderaSinAccion=false;
										
										        				
						 				}
									
									}
		  	         			    
		  	         			    
		  	         			    }//fin de si tiene TurnoNocturno el dia anteriror
								}

								
					 			    
					 			    
					 			    
		   	     				
		   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==true && biometrico.size()>0 ) {
		   	     					
		   	     					
		   	     					
		   	     					//Obtengo el dia anterior para comparar con la fecha actual	
		   	     					String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometrico), -1));
				            
		   	     				
		   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
		   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
		   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
		   	     					
		   	     				//Si el dia anterior no era de tipo nocturno
		   	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()==0) {
							
									String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
					 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
					 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

					 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
					 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
					 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
										
									
					 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
					 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
					                	 if (banderaHoraExtra1001==0) {
					 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
					 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
								    		banderaHoraExtra1001=1;
								   	    }else {
										        			    
								        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
					 			    	
					 			    	banderaHoraExtra1001=2;
					 				
									
									        				
					 				}
								
								}  			   	     					
		   	     					
		   	     				
		   	     				
		   	     				}
		   	     				else{
		   	     						//Si el empleado y la fecha son iguales 
										//Consulto la hora de salida 
		if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty() )
		{
			String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
                	 if (banderaHoraExtra1001==0) {
 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
			    		banderaHoraExtra1001=1;
			   	    }else {
			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
 			    	banderaHoraExtra1001=2;
 				}
		
			    			}
			    
			    
			    
			    
		}else {
			String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
	
										String horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
  						 			    if (horaBiometrico==horaSalidaAnterior) {
									}else {
  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
  						                	 if (banderaHoraExtra1001==0) {
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
  									    		banderaHoraExtra1001=1;
  									   	    }else {
  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
  						 			    	banderaHoraExtra1001=2;
  						 				}//else de validacion de hora extra
  						 		}//hora inicio y hora fin
									}//ELSE DE HORA ANTERIOR DE SALIDA	
									}//ELSE DE HORA FIN
							
									
									
									
									
									
									
									}//Else si hay empleado y fecha anteior
		   	     				
		   	     				
		   	     				
	         					
	         					
	         					
		   	     		       			   
	         			   
			   	     				}else if (banderaFeriados=true && estado_empleado_nocturno==false && biometrico.size()>0) {
								//si es dia normal y es feriado

	         						
	         						
	         						
	         						
										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
											
										
						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						                	 if (banderaHoraExtra1001==0) {
						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "FERIADO");
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "FERIADO");
						 			    	
						 			    	banderaHoraExtra1001=2;
						 				
										
										        				
						 				}
									
									}
						 			    
						 			    
						 			    
						 			    
						 			    
			   	     				
			   	     				}/*else if(estado_empleado_nocturno==true || estado_empleado_nocturno==false && biometrico.size()==1){
	         			    	//si tiene anterior
	         						//Variable que avisa si se ingresa o no el evento su es de tipo nocturno el dia anterior
	         						sinIngreso=true;
								break;
							}*/
	         			    	 
	         			    
	         			    
	         				}//Si tiene marcaciones en el biometrico
	         			else {
								
							}
	         			    
	         			}// FIN DE CICLO SI NO CONTIENE HORARIO 
	         					
	         				
	         				
	         		
	         				
	        			}//For Biometrico
	         			
	         			
	         	    if (tipo1==0 && Entrada==0 && banderaHoraExtra1001==0) {
       			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
         		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");  
			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
            			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
            			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
            		   	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	         			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
            			
						}
       			    
       			  if (tipo1==1 && Entrada==0 && banderaHoraExtra1001==0 )  {
	             			  
         		    		tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
         		    		tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");  
                          tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
  			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
  			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
              			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
              			tab_reporte.setValor(itemReporte, "HORAFINBAND", "");

							}
       			  
       			  
       			  if (tipo1==4 && Entrada==0 && banderaHoraExtra1001==0) {
       				  
       				
       	  			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"));
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND",tab_reporte.getValor(itemReporte, "HORAINICIOBAND"));
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
             		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
           		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
    			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
           			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");


							}
         			    
       			  
       			    
       	
       			  if (tipo1==3 && Entrada==0 && banderaHoraExtra1001==0) {
       				  
           		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
           		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
       				 	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
    			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", "S/A");
      			    	tab_reporte.setValor(itemReporte, "HORAFINALM", "S/A");
    			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
                			tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
                			tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
           			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");

							}
       			  
       			  
       			  if (Entrada!=0 && banderaHoraExtra1001==0) {
     	  			   	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
    			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    		    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
     			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
       			  
						}
       			  
       			  
       			  
       			  
       			  
       			  
       			  

	      	     	}// if biometrico
	      	           else {	//Si no se tiene marcaciones hechas en el biometrico 
	      	     		
	      	     		if (contieneDiaTurno!=0) {
	      	     			//Si  contiene horario pero no timbrada
	      	     		double minAlm=0.0;
	      	     		horaInicioEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
	      	     		horaFinEmpl = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
	      	     		horaIniAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
	      	     		horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
	      	     		minAlm =(pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);// horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR");//Hora fin erp

	      	     		
       					
	      	     		if (horaIniAlmEmpl.equals("00:00:00") && horaFinAlmEmpl.equals("00:00:00")) {
	      	     			horaIniAlmEmpl="S/A";
	      	     			horaFinAlmEmpl="S/A";
           		    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", "S/A");
           		    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", "S/A");
    			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+minAlm);

							}
	      	     		
       					
	      	     			
		      	  	    	
      			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
      			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO","" );
  			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
      			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
  			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
      			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ minAlm);
    			    		tab_reporte.setValor(itemReporte, "TIEMPOALM", "0");
      			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
    			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
    			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	      	        		
	      	     		
    			    		
	      	     		
	      	     			
	      	     			
	      	     			
					}
	      	     		else {
	      	     			banderaSinAccion=true;
						}
	      	    
	      	     		
	      	     		
					}
	         			
	        		}//For horario
	          
	        		
	        		
	        		
	        		double almuerzo1=0.0;
					String horarioEntrada="";
					String horarioSalida="";
					boolean bandera=false,banderaEntrada=false;
					int estadosAlmuerzo=0;
					//Si no hay datos le pongo =""
						if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("")) {
							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") || tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
								almuerzo1=0;
								estadosAlmuerzo=1;
						}else {
							almuerzo1=0;
							estadosAlmuerzo=0;
						}
							
							
						
					}else if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("S/A")) {
		  				almuerzo1=0;
		  			estadosAlmuerzo=1;
		  				
		  				
			
					}else {
						if(tab_reporte.getValor(itemReporte, "TIEMPOALM")== null || tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()
								|| tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("0")){
							almuerzo=0;
							estadosAlmuerzo=0;
						}else {
    						almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));
    						estadosAlmuerzo=1;							
					}
		
				}
					
						
						
						bandera=false;
					
					 
					
			/*		if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
					bandera=true;
				}else {
					bandera=false;
				}
	        		
					if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("EXTRA") ) {
	  					bandera=true;
	  				}else {
	  					bandera=false;
	  				}	
					
					
					
					if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("FERIADO") ) {
	  					bandera=true;
	  				}else {
	  					bandera=false;
	  				}
					
				*/	
					
					
					if (contieneDiaTurno!=0 && banderaSinAccion==false && banderaHoraExtra1001==0) {
					
			/*			TablaGenerica tabSucursal = utilitario.consultar(SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
	
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.IDE_SUCU, AREA.IDE_GEARE, " +
				"DEPA.IDE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "+
				"where epar.ide_gtemp="+empleado+ " "
				+ "	ORDER BY DETALLE_GEARE ASC");
						
				String sucursal=tabSucursal.getValor("IDE_SUCU");		
				String  area=tabSucursal.getValor("IDE_SUCU");
				String departamento=tabSucursal.getValor("IDE_SUCU");
						
				*/	
	    	   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
	 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
	 		   tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
	 		   tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
	 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometricoAgrupadaXEmpleado));
				
					
					
					} 
					
					else if (contieneDiaTurno==0 && banderaSinAccion==false &&  sinIngreso==false && banderaHoraExtra1001==0) {
					   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado));         
					}else if (banderaSinAccion==true) {
						
					}
					else if (banderaHoraExtra1001==2 || banderaHoraExtra1001==1) {
					
				if (banderaHoraExtra1001==1) {
					
					if (!horaActualizarPegado.equals("") && !horaActualizarPegado.isEmpty()) {
						insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado,tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
								"", "","", "",0 ,"0",tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),horaActualizarPegado, "OK",false,diaSemana(fechaBiometricoAgrupadaXEmpleado));
					}else {
						insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
								"", "","", "",0 ,"0","","", "",false,diaSemana(fechaBiometricoAgrupadaXEmpleado));
					}
				
				
							    
				}
				if (banderaHoraExtra1001==2) {
					
				
insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
"", "","", "",0 ,"0","",tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado));
    
	}
					} 					
					else {
						
					}

				}//Si tiene Matriz
				
				

				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
				

				
		   		//AQUI ES CONDICION si tiene turno de matriz
		       		     
   		     
   		     
		     
   if (tabEmpleado.getTotalFilas()!=0 && tabEmpleadoMatrizMensual.getTotalFilas()==0  ) {//TIENE MATRIZ
		   				//Busca si tiene asignado horario anualmente
		   					
		   					
		   					
		   			/*		if (banderaCambioHorario || banderaCambioHorarioPermisoOnline) {
		   						
		   						ddddddddd
		   						
		   					}
		   				*/	
		   					
		   					
		   					
		   					
		   				
		   			    	
		   			    	//Hacer la consulta para poder retornar los horarios de cada empleado
		   			    	
		   			        TablaGenerica tabObternerTurnos=this.obtenerTurnosMatriz();
		   			        
//		   					
		   			        
		   			        if (tabObternerTurnos.getTotalFilas()==0) {
		   						utilitario.agregarMensajeError("No existen Horarios", "Asignados a turnos");
		   					}
		   			    	
		   			    	for (int itemHorario=0; itemHorario< tabObternerTurnos.getTotalFilas(); itemHorario++) {
		   			    		//Obtengo el ide del empleado con el cual obtendrenmos los horarios
		   			    		Integer ide_horario=0;
		   			    		ide_horario= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor(itemHorario, "IDE_ASHOR"));
		   			            if(str_ide_horarios.toString().isEmpty()==false){
		   			                str_ide_horarios.append(",");
		   			    			}
		   			    			str_ide_horarios.append(ide_horario);

		   			    	}
		   			    	
		   			        	
		   			    	//Metodo devuelve el horario asignado a cada empleado
		   			       	TablaGenerica diasXHorario=utilitario.consultar("SELECT HORARIO.IDE_ASHOR,DIA.IDE_GEDIA,DIA.DETALLE_GEDIA FROM ASI_DIA_HORARIO HORARIO "
		   			    			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=HORARIO.IDE_GEDIA "
		   			    			+ "WHERE HORARIO.IDE_ASHOR IN ("+str_ide_horarios+")");
		   			    	
		   			       	       	
		   			    	
		   			    	
		   			    	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
		   			    	

		   		    		TablaGenerica horariosEmpleado = this.obtenerHorariosEmpleado(empleado);

		   			    		//para turno nocturno
		   			    	//	TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
		   			    //		String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
		   			    //		String ide_astur= obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();	
		   			    //		TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
		   			    //		String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
		   					
		   					
		   					
		   					
		   					
		   					
		   					
		   					
		   						//	TablaGenerica  obtenerMinutoTolerancia=this.obtenerHorariosTurnoEmpleadoTolerancia(Integer.parseInt(ide_astur));
		   			   	    //		String minuto_tolerancia_astur=obtenerMinutoTolerancia.getValor("minuto_tolerancia_astur");
		   			   	    		
		   			   	    		int banderaHoraExtra1001=0;
		   			   				    banderaFeriados=false;

		   			   				//tipo1= 0;
		   			   			    Integer tipo2 = 0;
		   			   			    Integer tipo3 = 0;
		   			   			    Integer tipo4= 0;
		   			   			    Integer tipo5 = 0;
		   			   			    Integer tipo6 = 0;
		   			   			    Integer tipo7 = 0;
		   			   				Integer tipo1SinAlmuerzo= 0;
		   			   			    Integer tipo2SinAlmuerzo = 0;
		   			   			    Integer tipo3SinAlmuerzo = 0;
		   			   			    Integer tipo4SinAlmuerzo= 0;
		   			   	String  SINDATO="";
		   			   		    
		   			     int turno= pckUtilidades.CConversion.CInt(tabObternerTurnos.getValor("ide_ashor"));
		   		    		TablaGenerica horariosEmpleadoMes = this.obtenerHorariosEmpleadoMes(turno);
		   			   	    		//Recorro los horarios 
		   			   	    		for (int i=0; i < horariosEmpleado.getTotalFilas(); i++) {
		   			   	    			//Me devuelve el dia si se encuentra dentro de su horario
		   			   	    			Map<Integer, Boolean> mapDias = obtenerDiasByHorario(diasXHorario, Integer.parseInt(horariosEmpleado.getValor(i, "ide_ashor")));
		   			   	    			
		   			   	    			List<Fila> biometrico;
		   			   	    			//Para marcaciones de tipo nocturno
		   			   	    			   

		   			   	        			 String horaBiometrico="";
		   			   	        			//variable para calculo de tiempo a justificar
		   			   	        			 String fechaHoraBiometrico;
		   			   	        			double tiempoAlmuerzo=0.0;
		   			   	    		    	double tiempoDiferenciaSalidaAnticipadaEmpleado=0.0;
		   			   	    		    	double tiempoDiferenciaSinSalidaEmpleado=0.0;
		   			   	         			double tiempoDiferenciaEntradaEmpleado=0.0;
		   			   	         			double tiempoDiferenciaSinEntradaEmpleado=0.0;

		   			   	         			 
		   			   	     				//Para creacion de inconvenientes
		   			   	     				String entradaSinJustificacion="";
		   			   	         			String almuerzoSinEntradaJustificacion="";
		   			   	         			String salidaSinJustificacion="";

		   			   	     			 	String marcacion1="";
		   			   	     			 	String marcacion="";

		   			   	     			    banderaHoraExtra1001=0;
		   			   	         			         			    
		   			   	     			//Verifico si es dia feriado    
		   			   	     			      banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
		   			   	     	            
		   			   	     	       
		   			   	    			//Obtengo los timbres realizados por un empleado 
		   			   	    			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

		   			   	     			if (biometrico.size()!=0 ) 
		   			   	     			//Si es contiene marcaciones en le biomtrico
		   			   	     			{
		   									 
		   								

		   			   	     			int banderaAlmuerzo=0;
		   			   	     			int banderaAlmuerzoEntrada=0;
		   			   	     			int banderaEntrada=0;
		   			   	     			//recorro todos las timbradas
		   			   	     			for (Fila bio : biometrico) {
		   			   	     				//fecha de la tabla biometrico
		   			   	     				fechaBiometrico = (String)bio.getCampos()[1];
		   			   	     				//Hora de la tabla biometrico
		   			   	     				horaBiometrico = (String)bio.getCampos()[2];
		   			   	     				//Evento de acuerdo a si es entrada o almuerzo
		   			   	     			//	String evento = (String)bio.getCampos()[3];
		   			   	     				//Unimos la fecha con la hora
		   			   	     				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
		   			   	     			    //Calendario 
		   			   	     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
		   			   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		   			   	     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
		   			   	     					
		   			   	     				//Obtengo el dia de la semana a que corresponde esa fecha
		   			   	     				int diaSemana = calFechaHoraBiometrico.get(Calendar.DAY_OF_WEEK);
		   			   	     			    //Validacion para el dia domingo=0
		   			   	     				if (diaSemana==1) {
		   			   	 						diaSemana=8;
		   			   	 					}
		   			   	     				
		   			   	     				//verifico si el dia se encuentra en mi mapita de dias y la bandera de si es dia feriado
		   			   	     				if (mapDias.get(diaSemana-1) != null && banderaFeriados==false){
		   			   	     					banderaMatriz=true;
		   			   	     					//Horario de acuerdo a los turnos ingrresados por talento humano
		   			   	     					 horaInicioEmpl = horariosEmpleado.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
		   			   	     					horaFinEmpl = horariosEmpleado.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
		   			   	     					horaIniAlmEmpl = horariosEmpleado.getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
		   			   	     					horaFinAlmEmpl = horariosEmpleado.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
		   			   	     					
		   			   	     					
		   			   	     					
		   			   	     					String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl; //cogo la hora y le concateno con la fecha del horario
		   			   	         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
		   			   	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
		   			   	         			    //Suma de minuto para el ingreso
		   			   	         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
		   			   	     					
		   			   	     					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
		   			   	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		   			   	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));
		   			   	         			    
		   			   	         			    
		   			   	         			    String fechaHoraHorarioInicial = fechaBiometrico+" "+horaFinAlmEmpl;//cogo la hora y le concateno con la fecha del horario
		   			   	         			    Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
		   			   	         			    calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicial));
		   			   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    


		   			   	         			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
		   			   	         			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
		   			   	         			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioIniAlmuerzo));
		   			   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, -15);	    

		   			   	         			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
		   			   	         			   Date finAlmuerzo=null;
		   			   	         			    
		   			   	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
		   			   	         		
		   			   	         			   
		   			   	         			   Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
		   			   	         			if (tiempoAlmuerzoHorario==0.5) {
		   			   	         	
			   	        			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 30);
		   			   	         			}else if (tiempoAlmuerzoHorario==1) {
											
	   			   	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo)); 
	   			   	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
										}
		   			   	         		
   			    		
		   			   	         			   
		   			   	         			   
		   			   	         			   
		   			   	         			   
		   			   	         			   
		   			   	         			   
		   			   	         		//	  if ((int)tiempoAlmuerzo==1) {
		   			   	           	//		    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
		   			   				//		}else {
		   			   	           	//		    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR")));
		   			   				//		}
		   			   	     					
		   			   	         			    //Sumamos una hora para el almuerzo
		   			   	         			   
		   			   	        
	
		   			    		
		   			    	
		  	             			    
		  	             			    
		  	             			    
		  	             			    //Hora desde Inicio de Entrada de Horario
		  	             			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio),-3));
		 	           	    		     Calendar calHoraFechaBase= Calendar.getInstance();
		 	           	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporteBase));
		 	         			
		   			    		
		   					
	  	   	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

		   					
		   				  //  if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
		       			    	if (banderaEntrada==0) {
		   							
		   						
		                          //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
		       			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OK");
		       			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		   			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+ (pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	       			
		       			    
		       			    	//Calculo de tiempo de atraso en la entrada del empleado
		       			    	tipo1=1;
		       			    	tipo2=0;
		       			    	banderaEntrada++;
		       			    	}
		       			    }
		    
		      				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) >= 0
		   							&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
		      					if (banderaEntrada==0) {
		       			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "ATRASADO");
		       			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		   			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
       			
		       			    	tipo2=1;
		       			    	tipo1=0;
		       			    	banderaEntrada++;
		       			    	}
		       				}

		       			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo==0){
		       			    	//Almuerzo
		       			    	
		       			    	if (banderaAlmuerzo == 0){
		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
		           			     	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		           			    	tab_reporte.setValor(itemReporte, "HORAINICIOALMBIO", horaBiometrico);
		       			    		tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	   		      					horaAnterior=horaBiometrico;
		           			    	banderaAlmuerzo++;
		           			    	tipo3=1;
		           			  
		       			    	}
		       			    	
		       			    	
		       			    	
		       			    	
		       			    }else if (banderaAlmuerzo==1 && horaBiometrico.compareTo(horaAnterior)>0  && horaBiometrico.compareTo(horaFinEmpl)<0 && banderaAlmuerzoEntrada==0){
		                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

	
		   	     			String uno= fechaBiometrico+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
		   	         		String dos = fechaBiometrico+" "+horaFinAlmEmpl;
		   	     			
		   	     				Calendar caluno = Calendar.getInstance();//
		   	         			Calendar caldos = Calendar.getInstance();//
		   	         			caluno.setTime(getFechaAsyyyyMMddHHmmss(dos));//
		   	         			caldos.setTime(getFechaAsyyyyMMddHHmmss(uno));//
		   	         			//Suma de minuto para el ingreso
		   	         			
		   	         			if((pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==1){
		   	         			caluno.add(Calendar.MINUTE, 60);	
		   	         			}else if ((pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==0.5) {
		   	         			caluno.add(Calendar.MINUTE, 30);	
								}else {
									
								}
		   	         			
		   	         				
		       			    	
		       			    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
	   		       			    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0 && banderaAlmuerzo==1){
	   		       			   	 
		       			    	    tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		       			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		           			    	
		   							tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		           			    	tab_reporte.setValor(itemReporte, "HORAFINALMBIO", horaBiometrico);
		           			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
		           			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
		           			    	tiempoAlmuerzo = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
		           			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
		           			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		           			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		           			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
		           			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
	   		      					horaAnteriorAlm=horaBiometrico;

		           			    	//Validacion si se pasa el almuerzo
		           			    	tipo4=1;
		           			    	banderaAlmuerzoEntrada++;
		           			    	horaAnterior="";
		       			    		}
		       			    	}
		           			   	

		       			      //JUAN CARLOS AYERBE
		       			    
		       			    
		       			
		       			    
		       			 /*   else if (banderaAlmuerzoEntrada==1 && banderaAlmuerzo==1){
		       			    	//Salida
		       			    	

		       				Calendar calAlm = Calendar.getInstance();//
		       				calAlm.setTime(getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+horaAnteriorAlm));//
		       			   if (calFechaHoraBiometrico.compareTo(calAlm) > 0
		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0 ){
		       		
		       			   
		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"));
		       			
		           			   	tipo5=1;
		           			   	tipo6=0;
		       			    	
		       			    }  	
		       			    }*/
		   				    
		       			  else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
	       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
	       			    	//Salida
	       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
	   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
	       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
	       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
	       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
	       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
	       			
	           			   	tipo5=1;
	           			   	tipo6=0;
	       			    	
	       			    }  
		   				    
		   				    
		   				
		       			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
		                        //  tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));

		       			    	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
		   			    		tab_reporte.setValor(itemReporte, "HORAINICIOALM", horaIniAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINALM", horaFinAlmEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", horaFinEmpl);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		       			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
		       			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
		       			    	
		       			    	tipo6=1;
		       			    	tipo5=0;
		       			    }    
		   					
		   				    
		   			   	     		
		   			   	     				
		   			   	     				}//Fin de ciclo si el dia de la semana en el horario no concuerda 
		   			   	     						   	     				
		   			   	     				else
		   			   	     				{//si es dia normal y es feriado

		   			   	     					
		   			   	     					
		   			   	     				if (tipo1!=0 || tipo2!=0 || tipo3!=0  || tipo4!=0 || tipo5!=0  || tipo6!=0 || banderaFeriados==false && mapDias.get(diaSemana-1) == null ) {
		   			   	     			extraMatriz++;
		   			   	     					break;	
		   			   							}else {
		   			   						banderaMatriz=true;
		   			   								String mensaje="";
		   			   	     					
		   			   	     					if(banderaFeriados==true){
		   			   	     						mensaje="FERIADO";
		   			   	     					}else {
		   			   	     						mensaje="EXTRA";

											}
		   			   	     					
		   										String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
		   						 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		   						 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

		   						 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
		   						 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		   						 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
		   											
		   										
		   						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		   						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		   						                	 if (banderaHoraExtra1001==0) {
		   						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		   						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		   						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		   						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", mensaje);
		   									    		banderaHoraExtra1001=1;
		   									   	    }else {
		   											        			    
		   									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		   						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", mensaje);
		   						 			    	
		   						 			    	banderaHoraExtra1001=2;
		   						 				
		   										
		   										        				
		   						 				}
		   									
		   									}
		   			   							}//Else si no se ha insertado datos anteriormente
		   			   	     				
		   			   	     				}//Fin de else de tipo de permiso
		   			   	     				
		   			   	     				
		   			   	     				
		   			   	     			}//Biometrico
		   			   	     			
		   			   	     			
		   			   	    		}else 
		   			   	    		//este si no tiene marcaciones realizadas en el biometrico
		   			   	    		{
		   			   	    			
		   			   	    		
		   			   	        		if (horariosEmpleado.getTotalFilas()>0) {
		   			      	     			//Si  contiene horario pero no timbrada
		   				      	  	    	
		   		        			  /*  	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", "");
		   		        			    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", "");
		   		    			    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "SIN MARCACIÓN");
		   		        			       	tab_reporte.setValor(itemReporte, "HORAINICIOALM", "00:00:00");
		   		    			    		tab_reporte.setValor(itemReporte, "HORAFINALM", "00:00:00");
		   		        			    	tab_reporte.setValor(itemReporte, "TIEMPOHORALM", "0");
		   		        			    	tab_reporte.setValor(itemReporte, "HORAFINHORARIO", "");
		   		      			    		tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
		   		      			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN MARCACIÓN");
		   		      			    	*/	
		   			      	     			banderaSinAccion=true;

		   							}
		   			      	     		else {
		   			      	     			banderaSinAccion=true;
		   								}
		   			      	    
		   			      	     		
		   			      	     		
		   							}
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		}
		   			   	    	
		   			   	    			
		   			   	    	////////////////////////////////////////////////////////////////////
		   			   	    			
		   			   	    			
		   			   	   	if (extraMatriz>1 ) {
		   							
		   								
		   					
		   	     				List<Fila> biometrico;		
		   	       			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

   			   	     			if (biometrico.size()!=0 ) 
   			   	     			//Si es contiene marcaciones en le biomtrico
   			   	     			{
   									
   								

   			   	     			int banderaAlmuerzo=0;
   			   	     			int banderaAlmuerzoEntrada=0;
   			   	     			int banderaEntrada=0;
   			   	     			//recorro todos las timbradas
   			   	     			for (Fila bio : biometrico) {
		   	     					
									String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
					 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
					 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

					 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
					 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
					 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
					 			    
					 			    fechaBiometrico = (String)bio.getCampos()[1];
					 			    //Hora de la tabla biometrico
					 			    String horaBiometrico = (String)bio.getCampos()[2];
					 			    //Evento de acuerdo a si es entrada o almuerzo
					 			    //	String evento = (String)bio.getCampos()[3];
					 			    //Unimos la fecha con la hora
					 			    String fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
					 			    //Calendario 
					 			    Calendar calFechaHoraBiometrico = Calendar.getInstance();
					 			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
					 			    calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
	            				
								
									
					 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
					 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
					                	 if (banderaHoraExtra1001==0) {
					 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
					 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
								    		banderaHoraExtra1001=1;
								   	    }else {
										        			    
								        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
					 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
					 			    	
					 			    	banderaHoraExtra1001=2;
					 				       				
					 				}
								
								}
			   	    			
			   	    			
   			   	     			}
			   	    			
   			   	     			}
			   	    		
					

		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    			
		   			   	    	//////////////////////////////////////////////////////////77		
									
								}
		   			   	    		
		   			   	    		
		   			   	    		//Horarios del Empleado
		   					
		   					
		   			   	    		

		   							
		   						double almuerzo1=0.0;
		    					String horarioEntrada="";
		    					String horarioSalida="";
		    					boolean bandera=false,banderaEntrada=false;
		    					int estadosAlmuerzo=0;
		    	  								
		    					//Si no hay datos le pongo =""
		    						if (tab_reporte.getValor(itemReporte, "HORAINICIOALM").equals("")) {
		    							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") || tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
		    								almuerzo1=0;
		    								estadosAlmuerzo=1;
		  							}else {
		  								almuerzo1=0;
		  							estadosAlmuerzo=0;
		  							}
		    							
		    			
		    					}else {
		    						if(tab_reporte.getValor(itemReporte, "TIEMPOALM")== null || tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("") || tab_reporte.getValor(itemReporte, "TIEMPOALM").isEmpty()
		    								|| tab_reporte.getValor(itemReporte, "TIEMPOALM").equals("0")){
		    							almuerzo=0;
		    							estadosAlmuerzo=0;
		    						}else {
		    							
	  		    						almuerzo1=Double.parseDouble( tab_reporte.getValor(itemReporte, "TIEMPOALM"));
	  		    					estadosAlmuerzo=1;									
								}


		  					}
		    					

		    					
		    					 
		    					
		    				//	if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("OK") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
		    				//	bandera=true;
		    				//}else {
		    				//	bandera=false;
		    				//}
		    	        		
		    			//		if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("EXTRA") ) {
		    	  		//			bandera=true;
		    	  		//		}else {
		    	  		//			bandera=false;
		    	  		//		}	
		    					
		    					
		    					
		    				//	if (estadosAlmuerzo>0 && tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("FERIADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("FERIADO") ) {
		    	  			//		bandera=true;
		    	  			//	}else {
		    	  			//		bandera=false;
		    	  			//	}
		    					
		   							
		   							
		   						//	if (bandera==false) {
									
								
		   						//if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("ATRASADO") && tab_reporte.getValor(itemReporte, "HORAFINBAND").equals("OK") ) {
		   					//		bandera=true;
		   					//	}else {
		   				//			bandera=false;
		   				//		}
		   					//		}
		   							
		   							
		   							
		   						bandera=false;	
		   							
		   							
		   							
		   							
		   							
		   							
		   			        		
		   							if (tipo1!=0 || tipo2!=0 || tipo3!=0 || tipo4!=0 || tipo5!=0 || tipo6!=0 && banderaSinAccion==false) {
		   			        	
		   			    	        insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
		   			 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
		   			 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
		   			 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
		   			 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
		   							}else if (banderaHoraExtra1001!=0 && banderaSinAccion==false) {
		   								
		   							
		   								   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
		   					 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
		   					 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
		   					 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
		   					 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico));
		   								
		   							} else if (banderaSinAccion==true) {
		   									
		   								
		   							}
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   			   	    		
		   					
		   				//System.out.println("Programacion Anual"+empleado);

		       	
		       		} 
   		     
   		     
   		     
   		     
   		     
//   si no tiene asignado la matriz mensual opertivo  ni  tampoco el turno sucursal matriz 
		   		
				
				
				
				
				
				
				
				
				
				
				
				else if (tabEmpleadoMatrizMensual.getTotalFilas()==0 && tabEmpleado.getTotalFilas()==0) {
		   					
		   		 			//Verifico si es dia feriado    
		   	     			     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
		   	     	            //Consulto marcaciones 	
		      	    			List<Fila> biometrico;
		     	    			 biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
		     	    		   
		     	    			 
		     	    			 int 	banderaHoraExtra1001=0;
		     	    			if (biometrico.size()!=0 ) {
		     	    				// Si tiene marcaciones 
		     	    				
		     	    				
		   						//si tiene marxcaciones y es feriado
		     	    			 
		     	    			boolean bandEntradaSinAlmuerzo=false;
		            			
		            			int banderaAlmuerzo=0;
		            			int banderaAlmuerzoEntrada=0;
		            			int banderaEntrada=0;
		            			//recorro todos las timbradas
		            			for (Fila bio : biometrico) {
		            				//fecha de la tabla biometrico
		            				fechaBiometrico = (String)bio.getCampos()[1];
		            				//Hora de la tabla biometrico
		            				String horaBiometrico = (String)bio.getCampos()[2];
		            				//Evento de acuerdo a si es entrada o almuerzo
		            			//	String evento = (String)bio.getCampos()[3];
		            				//Unimos la fecha con la hora
		            				String fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
		            			    //Calendario 
		            				Calendar calFechaHoraBiometrico = Calendar.getInstance();
		            			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		            				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
		            				
		            			
		            			
		            				 horaInicioEmpl ="";
		        					 horaFinEmpl = "";
		        					 horaIniAlmEmpl ="";
		        					 horaFinAlmEmpl = "";
		        			
		            				boolean t=false;
		            				
		            				//si no contiene turno
		            				String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
		            			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		            			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFin));

		            			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
		            			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		            			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioHasta));
		            			
		   						
		            			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		            			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		                           	 if (banderaHoraExtra1001==0) {
		            			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		            			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		            			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		    	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 

		        			    		banderaHoraExtra1001=1;
		        			   	    }else {
		   							        			    
		           			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		            			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		            			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		            			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
		            			    	
		            			    	banderaHoraExtra1001=2;
		   	     				
		        				
		   						        				
		            				}
		            			   
		            			    	 
		            			    	 
		            			    }
		            			    
		            			}//Fin de timbradas en biometrico 
		            			
		            			
		            			
		            			if (biometrico.size()!=0 ){
		    					   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
		    		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
		    		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
		    		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
		    		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico));         			
		             			
		             			}
		            			}else {
		   					//Si no tiene marcaciones 	
		            			   insertarTablaResumen(empleado, fechaBiometrico, "", "","SIN HORARIO", "", "",
		   		 		   "", "",0,"",
		   		 		    "","","SIN HORARIO",false,diaSemana(fechaBiometrico));         			
		            			
		            			
		            			}
		     	    			//si tiene marcaciones
		     	    	
		     	    			
		            			
		           	// sin tiene marcacion en el biometrico inserta sino no hace nada
		            	
		            			
		   					
		   				}
		              		 
				
				
				
				
				
				
				
				       }

	
	
	
	
	
	
	
	
	//Validacion de horas extra por empleado
	
	if (!empleadoSeleccionado.equals("") || !empleadoSeleccionado.isEmpty() || empleadoSeleccionado!=null ) {
		
	
	
	
		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' and ide_gtemp in("+empleadoSeleccionado+") ");
			tab_resumen_marcaciones.ejecutarSql();
			
		      			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ "recargonocturno25_cobmr=null, recargonocturno100_cobmr=null, horafinextra_cobmr=null, novedad_cobmr=false "
					+ "where fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and ide_gtemp in("+empleadoSeleccionado+") ");
			
			
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%OK%' "
					+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");

					
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " novedad_cobmr =true  where horainicioband_cobmr like '%ATRASADO%' and horafinband_cobmr like '%OK%' "
					+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");


			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%ANTICIPADO%' "
					+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");



			
			
			///EXTRA Y FERIADO
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " novedad_cobmr =true  where horainicioband_cobmr like '%EXTRA%' and horafinband_cobmr like '%EXTRA%' "
					+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");

					
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " novedad_cobmr =true  where horainicioband_cobmr like '%FERIADO%' and horafinband_cobmr like '%FERIADO%' "
					+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");
			/**
			 * VALIDACION ALMUERZO
			 */
    		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"'");
   			tab_resumen_marcaciones.ejecutarSql();
   			utilitario.addUpdate("tab_resumen_marcaciones");
   			utilitario.agregarMensajeInfo("Se han reprocesado las marcaciones",	"Empleado(s) incluido(s) satisfactoriamente");

	}
	
	
	
	
	
	
	
	
	    }


private TablaGenerica obtenerTimbreBiometricoExtra(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
				+ "EMP.IDE_GTEMP,"
    			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
    			+ "from gth_empleado emp  "
    			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
    			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
    			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
    			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");    	
	//tabObteberTimbreXEmpleado.imprimirSql();
	return tabObteberTimbreXEmpleado;
	
}


public Tabla getTab_resumen_marcaciones() {
	return tab_resumen_marcaciones;
}


public void setTab_resumen_marcaciones(Tabla tab_resumen_marcaciones) {
	this.tab_resumen_marcaciones = tab_resumen_marcaciones;
}



public void bloquearBotones(){
	
	bar_botones.getBot_eliminar().setRendered(false);
	bar_botones.getBot_guardar().setRendered(false);
	bar_botones.getBot_insertar().setRendered(false);
	bar_botones.getBot_inicio().setRendered(false);
	bar_botones.getBot_atras().setRendered(false);
	bar_botones.getBot_siguiente().setRendered(false);
	bar_botones.getBot_fin().setRendered(false);
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
 			String fechaIni=(cal_fecha_inicial.getFecha());	
     		String fechaFin=(cal_fecha_final.getFecha());
 		
     		TablaGenerica tab_tabla_pruebas= utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
     				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr,  "
     				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
     				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
     				+ "horafinband_cobmr, horafinextra_cobmr, recargonocturno25_cobmr,  "
     				+ "recargonocturno100_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
     				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
     				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
     				+ "ide_aspvh "
     				+ "FROM con_biometrico_marcaciones_resumen where horainiciobiometrico_cobmr=''  "
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
	 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarInicio+"',horafinbiometrico_cobmr='"+horaBiometricoPegarFin+"',"
	 									+ "pegado_horario_cobmr=true,novedad_cobmr=true "
	 							+ "where ide_gtemp in ("+empleado+") "
	 							+ " and fecha_evento_cobmr='"+fecha_evento_cobmr+"' ");
	 					
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
 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarFin+"',horafinbiometrico_cobmr='"+horaFinNocturno+"',"
	 					    + "pegado_horario_cobmr=true,novedad_cobmr=true "
 							+ "where ide_gtemp in ("+empleado+") "
 							+ " and fecha_evento_cobmr='"+fecha_evento_cobmr+"' ");

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
	 							+ "horainiciobiometrico_cobmr='"+horaBiometricoPegarInicio+"',horafinbiometrico_cobmr='"+horaBiometricoPegarFin+"',"
	 							+ "pegado_horario_cobmr=true,novedad_cobmr=true "
	 							+ "where ide_gtemp in ("+empleado+") "
	 							+ " and fecha_evento_cobmr = '"+fecha_evento_cobmr+"'");
	
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





public void consultarEmpleado(){

if (tabEmpleado.getTotalFilas()>0 &&  !(tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) ) {      		
tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+str_ide.toString()+") ");	
}else {

if (tabEmpDep.getValor("ide_geedp").equals("1307") || tabEmpDep.getValor("ide_geare").equals("9")) {
tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' ");	
}
}

tab_resumen_marcaciones.ejecutarSql();
utilitario.addUpdate("tab_resumen_marcaciones,tab_marcaciones");

}

}





