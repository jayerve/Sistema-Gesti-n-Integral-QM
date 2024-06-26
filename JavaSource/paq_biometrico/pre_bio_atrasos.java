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
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.tabview.Tab;
import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
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
import groovy.util.IFileNameFinder;

/**
 *
 * @author DELL-USER
 */
public class pre_bio_atrasos extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_Coordinadores = new Tabla();
    private Tabla tab_calendario = new Tabla();
    private Tabla tab_sql = new Tabla();
    private Tabla tab_tabla_sumatoria = new Tabla();


    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private SeleccionTabla set_area = new SeleccionTabla();
    private SeleccionTabla set_areaDepartamento = new SeleccionTabla();
    private String area;
 
    
    
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	private boolean novedad=false,sinNovedad=false;
	
	private boolean matriz=false,operativo=false;
    
    public pre_bio_atrasos() {
    	
    	
    	  
	bar_botones.agregarComponente(new Etiqueta("BÚSQUEDA POR SUCURSAL"));
	bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
	cal_fecha_inicial.setFechaActual();
	bar_botones.agregarComponente(cal_fecha_inicial);

	bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
	cal_fecha_final.setFechaActual();
	bar_botones.agregarComponente(cal_fecha_final);

	
	
	
	Boton bot_copiar= new Boton();
	bot_copiar.setIcon("ui-icon-document");
	bot_copiar.setMetodo("justificar");
//	bot_copiar.setMetodo("seleccionArea");
	bot_copiar.setValue("REPORTE ATRASOS");
	bot_copiar.setTitle("REPORTE ATRASOS");
	bar_botones.agregarBoton(bot_copiar);	
    	
    	
    	
	Boton bot_opertivo= new Boton();
	bot_opertivo.setIcon("ui-icon-document");
	bot_opertivo.setMetodo("buscarPersonalMensualOperativo");
	bot_opertivo.setValue("OPERATIVO");
	bot_opertivo.setTitle("OPERATIVO");
//	bar_botones.agregarBoton(bot_opertivo);	
    	

	
	Boton bot_AreaDepartamentoEmpleado= new Boton();
	bot_AreaDepartamentoEmpleado.setIcon("ui-icon-document");
	bot_AreaDepartamentoEmpleado.setMetodo("seleccionArea");
	bot_AreaDepartamentoEmpleado.setValue("BÚSQUEDA");
	bot_AreaDepartamentoEmpleado.setTitle("BÚSQUEDA");
	//bar_botones.agregarBoton(bot_AreaDepartamentoEmpleado);	
    	

	Boton bot_entrada= new Boton();
	bot_entrada.setIcon("ui-icon-document");
	bot_entrada.setMetodo("buscarSinEntrada");
	bot_entrada.setValue("SIN ENTRADA");
	bot_entrada.setTitle("SIN ENTRADA");
	//bar_botones.agregarBoton(bot_entrada);	
    
	
	Boton bot_salida= new Boton();
	bot_salida.setIcon("ui-icon-document");
	bot_salida.setMetodo("buscarSinSalida");
	bot_salida.setValue("SIN SALIDA");
	bot_salida.setTitle("SIN SALIDA");
	//bar_botones.agregarBoton(bot_salida);	
    
	Boton bot_cambioHorario= new Boton();
	bot_cambioHorario.setIcon("ui-icon-document");
	bot_cambioHorario.setMetodo("buscarCambioHorario");
	bot_cambioHorario.setValue("CAMBIO TURNO");
	bot_cambioHorario.setTitle("CAMBIO TURNO");
	//bar_botones.agregarBoton(bot_cambioHorario);	
    
	
	
	
    		// boton limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar);     	
       
    	
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla(ser_empleado.servicioEmpleadosActivos("true"), "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
    	set_empleado.setTitle("Seleccione un Empleado");
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

    	
    	
   	
    	
    	
   	
    	
		set_area.setId("set_area");
		set_area.setSeleccionTabla("SELECT AREA.IDE_GEARE,  "
				+ "AREA.DETALLE_GEARE "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "WHERE AREA.IDE_GEARE NOT IN (16) "
				+ "GROUP BY AREA.IDE_GEARE,  "
				+ "AREA.DETALLE_GEARE "
				+ "order by detalle_geare asc", "IDE_GEARE");
		set_area.setRadio();
		set_area.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		set_area.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
		set_area.setTitle("Seleccione AREA");
		gru_pantalla.getChildren().add(set_area);
		set_area.getBot_aceptar().setMetodo("getArea");
		agregarComponente(set_area);

    	
    	
		
		set_areaDepartamento.setId("set_areaDepartamento");
		
		agregarComponente(set_areaDepartamento);

	
		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(getDiferenciaEntradaSalida("",""));
		tab_tabla.setCampoPrimaria("ide_gtemp");
		tab_tabla.setNumeroTabla(1);
		tab_tabla.setLectura(true);

		
		tab_tabla.getColumna("IDE_GTEMP").setLongitud(5);
        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
        tab_tabla.getColumna("IDE_GTEMP").setNombreVisual("COD");


		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
        tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
        tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CÉDULA");
		

		tab_tabla.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
        tab_tabla.getColumna("NOMBRES_APELLIDOS").alinearCentro();
        tab_tabla.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
		tab_tabla.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

		
		tab_tabla.getColumna("NOM_SUCU").setLongitud(15);
        tab_tabla.getColumna("NOM_SUCU").alinearCentro();
        tab_tabla.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
		tab_tabla.getColumna("NOM_SUCU").setFiltro(true);

        
    	
		tab_tabla.getColumna("DETALLE_GEARE").setLongitud(15);
        tab_tabla.getColumna("DETALLE_GEARE").alinearCentro();
        tab_tabla.getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		tab_tabla.getColumna("DETALLE_GEARE").setFiltro(true);

        
        tab_tabla.getColumna("DETALLE_GEDEP").setLongitud(15);
        tab_tabla.getColumna("DETALLE_GEDEP").alinearCentro();
        tab_tabla.getColumna("DETALLE_GEDEP").setNombreVisual("DEPT");
		tab_tabla.getColumna("DETALLE_GEDEP").setFiltro(true);

		tab_tabla.getColumna("IDE_COBMR").setLongitud(5);
        tab_tabla.getColumna("IDE_COBMR").alinearCentro();

        tab_tabla.getColumna("DIA_COBMR").setLongitud(15);
        tab_tabla.getColumna("DIA_COBMR").alinearCentro();
        tab_tabla.getColumna("DIA_COBMR").setNombreVisual("DIA");
        
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
        
        
        tab_tabla.getColumna("horainiciohorario_cobmr").setLongitud(15);
        tab_tabla.getColumna("horainiciohorario_cobmr").setNombreVisual("ENTRADA");
        tab_tabla.getColumna("horainiciohorario_cobmr").alinearCentro();

        
        
        tab_tabla.getColumna("horainiciobiometrico_cobmr").setLongitud(15);
        tab_tabla.getColumna("horainiciobiometrico_cobmr").setNombreVisual("TIMBRE");
        tab_tabla.getColumna("horainiciobiometrico_cobmr").alinearCentro();

        
        tab_tabla.getColumna("horainicioband_cobmr").setLongitud(15);
        tab_tabla.getColumna("horainicioband_cobmr").setNombreVisual("ESTADO ENTRADA");
        tab_tabla.getColumna("horainicioband_cobmr").alinearCentro();

        tab_tabla.getColumna("biometrico_entrada_cobmr").setLongitud(20);
        tab_tabla.getColumna("biometrico_entrada_cobmr").alinearCentro();
        tab_tabla.getColumna("biometrico_entrada_cobmr").setNombreVisual("UBICACIÓN"); 
      	
        
        tab_tabla.getColumna("diferenciaEntradaTimbrado").setLongitud(15);
        tab_tabla.getColumna("diferenciaEntradaTimbrado").setNombreVisual("MIN ATRASO ENTRADA");
        tab_tabla.getColumna("diferenciaEntradaTimbrado").alinearCentro();
        tab_tabla.getColumna("diferenciaEntradaTimbrado").setDecimales(2);
        
        
        tab_tabla.getColumna("horainicioalm_cobmr").setLongitud(15);
        tab_tabla.getColumna("horainicioalm_cobmr").setNombreVisual("H.INI.ALM");
        tab_tabla.getColumna("horainicioalm_cobmr").alinearCentro();

        
        tab_tabla.getColumna("horafinalm_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinalm_cobmr").setNombreVisual("H.FIN.ALM");
        tab_tabla.getColumna("horafinalm_cobmr").alinearCentro();

        
        
               
        tab_tabla.getColumna("horainicioalmbio_cobmr").setLongitud(15);
        tab_tabla.getColumna("horainicioalmbio_cobmr").setNombreVisual("TM.INI.ALM");
        tab_tabla.getColumna("horainicioalmbio_cobmr").alinearCentro();

        
        
        
        tab_tabla.getColumna("horafinalmbio_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinalmbio_cobmr").setNombreVisual("TM.FIN.ALM");
        tab_tabla.getColumna("horafinalmbio_cobmr").alinearCentro();

        

        tab_tabla.getColumna("tiempoalm_cobmr").setLongitud(15);
        tab_tabla.getColumna("tiempoalm_cobmr").setNombreVisual("TIM.TOMA.ALM");
        tab_tabla.getColumna("tiempoalm_cobmr").alinearCentro();

               
        tab_tabla.getColumna("biometrico_alm_salida").setLongitud(20);
        tab_tabla.getColumna("biometrico_alm_salida").alinearCentro();
        tab_tabla.getColumna("biometrico_alm_salida").setNombreVisual("UBICACÓN SAL.ALM"); 


        tab_tabla.getColumna("biometrico_alm_entrada").setLongitud(20);
        tab_tabla.getColumna("biometrico_alm_entrada").alinearCentro();
        tab_tabla.getColumna("biometrico_alm_entrada").setNombreVisual("UBICACIÓN ENT.ALM"); 
 
        
        
               
        tab_tabla.getColumna("diferenciaAlm").setLongitud(15);
        tab_tabla.getColumna("diferenciaAlm").setNombreVisual("MINUTOS DE ATRASO ALMUERZO");
        tab_tabla.getColumna("diferenciaAlm").alinearCentro();

        
        tab_tabla.getColumna("horafinalmbio_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinalmbio_cobmr").setNombreVisual("TM.FIN.ALM");
        tab_tabla.getColumna("horafinalmbio_cobmr").alinearCentro();



        
        tab_tabla.getColumna("horafinhorario_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinhorario_cobmr").setNombreVisual("SALIDA");
        tab_tabla.getColumna("horafinhorario_cobmr").alinearCentro();

        
        
        tab_tabla.getColumna("horafinbiometrico_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinbiometrico_cobmr").setNombreVisual("TIMBRE SALIDA");
        tab_tabla.getColumna("horafinbiometrico_cobmr").alinearCentro();

        
        tab_tabla.getColumna("horafinband_cobmr").setLongitud(15);
        tab_tabla.getColumna("horafinband_cobmr").setNombreVisual("ESTADO SALIDA");
        tab_tabla.getColumna("horafinband_cobmr").alinearCentro();

        
        tab_tabla.getColumna("biometrico_salida_cobmr").setLongitud(20);
        tab_tabla.getColumna("biometrico_salida_cobmr").alinearCentro();
        tab_tabla.getColumna("biometrico_salida_cobmr").setNombreVisual("UBICACIÓN SALIDA"); 

     
        
        
        tab_tabla.getColumna("diferenciaSalidaTimbrado").setLongitud(15);
        tab_tabla.getColumna("diferenciaSalidaTimbrado").setNombreVisual("MIN ATRASO SALIDA");
        tab_tabla.getColumna("diferenciaSalidaTimbrado").alinearCentro();
        tab_tabla.getColumna("diferenciaSalidaTimbrado").setDecimales(2);
        
        tab_tabla.getColumna("tiempohoralm_cobmr").setLongitud(15);
        tab_tabla.getColumna("tiempohoralm_cobmr").setNombreVisual("MIN.ALM");
        tab_tabla.getColumna("tiempohoralm_cobmr").alinearCentro();

	
		//+ "res., "
	  
        

    
        tab_tabla.getColumna("E_FECHA_SOLICITUD").setLongitud(15);
        tab_tabla.getColumna("E_FECHA_SOLICITUD").alinearCentro();
        tab_tabla.getColumna("E_FECHA_SOLICITUD").setNombreVisual("FECHA SOLICITUD");
        tab_tabla.getColumna("E_FECHA_SOLICITUD").setVisible(false);

        
        
        
        tab_tabla.getColumna("E_TTHH").setLongitud(15);
        tab_tabla.getColumna("E_TTHH").alinearCentro();
        tab_tabla.getColumna("E_TTHH").setNombreVisual("TTHH");
        tab_tabla.getColumna("E_TTHH").setVisible(false);

        
        tab_tabla.getColumna("E_JEFE_INMEDIATO").setLongitud(15);
        tab_tabla.getColumna("E_JEFE_INMEDIATO").alinearCentro();
        tab_tabla.getColumna("E_JEFE_INMEDIATO").setNombreVisual("JEFE INMEDIATO");
        tab_tabla.getColumna("E_JEFE_INMEDIATO").setVisible(false);
    
      	        
        tab_tabla.getColumna("E_FECHA_DESDE").setLongitud(15);
        tab_tabla.getColumna("E_FECHA_DESDE").alinearCentro();
        tab_tabla.getColumna("E_FECHA_DESDE").setNombreVisual("F.DESDE");
        tab_tabla.getColumna("E_FECHA_DESDE").setVisible(false);

        
        
        
        
         tab_tabla.getColumna("E_FECHA_HASTA").setLongitud(16);
        tab_tabla.getColumna("E_FECHA_HASTA").alinearCentro();
        tab_tabla.getColumna("E_FECHA_HASTA").setNombreVisual("F.HASTA");
        tab_tabla.getColumna("E_FECHA_HASTA").setVisible(false);

        
        
        tab_tabla.getColumna("E_DETALLE").setLongitud(20);
        tab_tabla.getColumna("E_DETALLE").alinearCentro();
        tab_tabla.getColumna("E_DETALLE").setNombreVisual("DETALLE");  
        tab_tabla.getColumna("E_DETALLE").setVisible(false);

        
        
        tab_tabla.getColumna("E_TIPO_PERMISO").setLongitud(20);
        tab_tabla.getColumna("E_TIPO_PERMISO").alinearCentro();
        tab_tabla.getColumna("E_TIPO_PERMISO").setNombreVisual("TIP.PERMISO");  
        tab_tabla.getColumna("E_TIPO_PERMISO").setVisible(false);

        
        tab_tabla.getColumna("E_NUM_HORAS").setLongitud(10);
        tab_tabla.getColumna("E_NUM_HORAS").alinearCentro();
        tab_tabla.getColumna("E_NUM_HORAS").setNombreVisual("NUM.HORAS");
        tab_tabla.getColumna("E_NUM_HORAS").setVisible(false);

        
        
        tab_tabla.getColumna("E_HORA_DESDE").setLongitud(10);
        tab_tabla.getColumna("E_HORA_DESDE").alinearCentro();
        tab_tabla.getColumna("E_HORA_DESDE").setNombreVisual("H.DESDE"); 
        tab_tabla.getColumna("E_HORA_DESDE").setVisible(false);

        
        
        tab_tabla.getColumna("E_HORA_HASTA").setLongitud(25);
        tab_tabla.getColumna("E_HORA_HASTA").alinearCentro();
        tab_tabla.getColumna("E_HORA_HASTA").setNombreVisual("H.HASTA");  
        tab_tabla.getColumna("E_HORA_HASTA").setVisible(false);

                

        
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").setLongitud(15);
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").setNombreVisual("FECHA SOL.ALM");
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").setVisible(false);


        tab_tabla.getColumna("TTHH_ALM").setLongitud(15);
        tab_tabla.getColumna("TTHH_ALM").alinearCentro();
        tab_tabla.getColumna("TTHH_ALM").setNombreVisual("TTHH.ALM");
        tab_tabla.getColumna("TTHH_ALM").setVisible(false);
        
        
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").setLongitud(15);
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").alinearCentro();
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").setNombreVisual("JEFE INM.ALM");
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").setVisible(false);

      	        
        tab_tabla.getColumna("FECHA_DESDE_ALM").setLongitud(15);
        tab_tabla.getColumna("FECHA_DESDE_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_DESDE_ALM").setNombreVisual("F.DESDE.ALM");
        tab_tabla.getColumna("FECHA_DESDE_ALM").setVisible(false);

        
        
        
         tab_tabla.getColumna("FECHA_HASTA_ALM").setLongitud(16);
        tab_tabla.getColumna("FECHA_HASTA_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_HASTA_ALM").setNombreVisual("F.HASTA.ALM");
        tab_tabla.getColumna("FECHA_HASTA_ALM").setVisible(false);

        
        
        
        tab_tabla.getColumna("DETALLE_ALM").setLongitud(20);
        tab_tabla.getColumna("DETALLE_ALM").alinearCentro();
        tab_tabla.getColumna("DETALLE_ALM").setNombreVisual("DETALLE_ALM");  
        tab_tabla.getColumna("DETALLE_ALM").setVisible(false);


        tab_tabla.getColumna("TIPO_PERMISO_ALM").setLongitud(20);
        tab_tabla.getColumna("TIPO_PERMISO_ALM").alinearCentro();
        tab_tabla.getColumna("TIPO_PERMISO_ALM").setNombreVisual("TIP.PERMISO.ALM");  
        tab_tabla.getColumna("TIPO_PERMISO_ALM").setVisible(false);

        
        
        tab_tabla.getColumna("NUM_HORAS_ALM").setLongitud(10);
        tab_tabla.getColumna("NUM_HORAS_ALM").alinearCentro();
        tab_tabla.getColumna("NUM_HORAS_ALM").setNombreVisual("NUM.HORAS_ALM"); 
        tab_tabla.getColumna("NUM_HORAS_ALM").setVisible(false);

        
        tab_tabla.getColumna("HORA_DESDE_ALM").setLongitud(10);
        tab_tabla.getColumna("HORA_DESDE_ALM").alinearCentro();
        tab_tabla.getColumna("HORA_DESDE_ALM").setNombreVisual("H.DESDE_ALM"); 
        tab_tabla.getColumna("HORA_DESDE_ALM").setVisible(false);

        
        tab_tabla.getColumna("HORA_HASTA_ALM").setLongitud(25);
        tab_tabla.getColumna("HORA_HASTA_ALM").alinearCentro();
        tab_tabla.getColumna("HORA_HASTA_ALM").setNombreVisual("H.HASTA_ALM");  
        tab_tabla.getColumna("HORA_HASTA_ALM").setVisible(false);

                
                
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").setLongitud(15);
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").setNombreVisual("FECHA SOL.SAL");
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").setVisible(false);

        
        

        tab_tabla.getColumna("TTHH_SAL").setLongitud(15);
        tab_tabla.getColumna("TTHH_SAL").alinearCentro();
        tab_tabla.getColumna("TTHH_SAL").setNombreVisual("TTHH_SAL");     
        tab_tabla.getColumna("TTHH_SAL").setVisible(false);

        
        
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").setLongitud(15);
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").alinearCentro();
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").setNombreVisual("JEFE INM.SAL");
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").setVisible(false);

        
        
        
      	        
        tab_tabla.getColumna("FECHA_DESDE_SAL").setLongitud(15);
        tab_tabla.getColumna("FECHA_DESDE_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_DESDE_SAL").setNombreVisual("F.DESDE.SAL");
        tab_tabla.getColumna("FECHA_DESDE_SAL").setVisible(false);

               
        tab_tabla.getColumna("FECHA_HASTA_SAL").setLongitud(16);
        tab_tabla.getColumna("FECHA_HASTA_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_HASTA_SAL").setNombreVisual("F.HASTA.SAL");
        tab_tabla.getColumna("FECHA_HASTA_SAL").setVisible(false);

        
        
        tab_tabla.getColumna("DETALLE_SAL").setLongitud(20);
        tab_tabla.getColumna("DETALLE_SAL").alinearCentro();
        tab_tabla.getColumna("DETALLE_SAL").setNombreVisual("DETALLE.SAL");  
        tab_tabla.getColumna("DETALLE_SAL").setVisible(false);


        tab_tabla.getColumna("TIPO_PERMISO_SAL").setLongitud(20);
        tab_tabla.getColumna("TIPO_PERMISO_SAL").alinearCentro();
        tab_tabla.getColumna("TIPO_PERMISO_SAL").setNombreVisual("TIP.PERMISO.SAL");  
        tab_tabla.getColumna("TIPO_PERMISO_SAL").setVisible(false);

        
        tab_tabla.getColumna("NUM_HORAS_SAL").setLongitud(10);
        tab_tabla.getColumna("NUM_HORAS_SAL").alinearCentro();
        tab_tabla.getColumna("NUM_HORAS_SAL").setNombreVisual("NUM.HORAS.SAL"); 
        tab_tabla.getColumna("NUM_HORAS_SAL").setVisible(false);

        
        tab_tabla.getColumna("HORA_DESDE_SAL").setLongitud(10);
        tab_tabla.getColumna("HORA_DESDE_SAL").alinearCentro();
        tab_tabla.getColumna("HORA_DESDE_SAL").setNombreVisual("H.DESDE.SAL"); 
        tab_tabla.getColumna("HORA_DESDE_SAL").setVisible(false);

        
        tab_tabla.getColumna("HORA_HASTA_SAL").setLongitud(25);
        tab_tabla.getColumna("HORA_HASTA_SAL").alinearCentro();
        tab_tabla.getColumna("HORA_HASTA_SAL").setNombreVisual("H.HASTA_SAL");  
        tab_tabla.getColumna("HORA_HASTA_SAL").setVisible(false);

        tab_tabla.getColumna("DIFERENCIAENTRADA").setLongitud(25);
        tab_tabla.getColumna("DIFERENCIAENTRADA").setVisible(false);
        
        tab_tabla.getColumna("diferenciaSalidaAlm").setLongitud(25);
        tab_tabla.getColumna("diferenciaSalidaAlm").setVisible(false);
         
        tab_tabla.getColumna("diferenciaEntradaAlm").setLongitud(25);
        tab_tabla.getColumna("diferenciaEntradaAlm").setVisible(false);
        
        
        tab_tabla.getColumna("p_entrada_cobmr").setLongitud(25);
        tab_tabla.getColumna("p_entrada_cobmr").setVisible(false);
        
        tab_tabla.getColumna("p_salida_cobmr").setLongitud(25);
        tab_tabla.getColumna("p_salida_cobmr").setVisible(false);
        
        tab_tabla.getColumna("p_alm_cobmr").setLongitud(25);
        tab_tabla.getColumna("p_alm_cobmr").setVisible(false);

                
        tab_tabla.getColumna("DETALLE_GTTEM").setLongitud(25);
        tab_tabla.getColumna("DETALLE_GTTEM").alinearCentro();
        tab_tabla.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMPLEADO");  
        
        
		tab_tabla.setRows(15);
		tab_tabla.dibujar();
		tab_tabla.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
    	
        
        

		tab_tabla_sumatoria.setId("tab_tabla_sumatoria");
		tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(0,"",""));
		tab_tabla_sumatoria.setCampoPrimaria("ide_gtemp");
		tab_tabla_sumatoria.setNumeroTabla(5);
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
		
		
		tab_tabla_sumatoria.getColumna("ATRASO_ENTRADA").setLongitud(35);
		tab_tabla_sumatoria.getColumna("ATRASO_ENTRADA").alinearCentro();
		tab_tabla_sumatoria.getColumna("ATRASO_ENTRADA").setNombreVisual("TOTAL ATRASO ENTRADA"); 
		
		
		tab_tabla_sumatoria.getColumna("ATRASO_ALMUERZO").setLongitud(35);
		tab_tabla_sumatoria.getColumna("ATRASO_ALMUERZO").alinearCentro();
		tab_tabla_sumatoria.getColumna("ATRASO_ALMUERZO").setNombreVisual("TOTAL ATRASO ALMUERZO"); 
		
		
		tab_tabla_sumatoria.getColumna("ATRASO_SALIDA").setLongitud(35);
		tab_tabla_sumatoria.getColumna("ATRASO_SALIDA").alinearCentro();
		tab_tabla_sumatoria.getColumna("ATRASO_SALIDA").setNombreVisual("TOTAL ATRASO SALIDA"); 
		

		tab_tabla_sumatoria.setRows(5);
		tab_tabla_sumatoria.dibujar();
		tab_tabla_sumatoria.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");


        
        
        
		   PanelTabla pat_panel11 = new PanelTabla();
	        pat_panel11.setPanelTabla(tab_tabla_sumatoria);
        
        
        
        
        
        
        
        
        
        
        
		tab_Coordinadores.setId("tab_Coordinadores");
		tab_Coordinadores.setSql(getCoordinadores());
		tab_Coordinadores.setCampoPrimaria("ide_geedp");
		tab_Coordinadores.setNumeroTabla(2);
		tab_Coordinadores.setLectura(true);
		
	      
		tab_Coordinadores.getColumna("IDE_GEEDP").setLongitud(10);
		tab_Coordinadores.getColumna("IDE_GEEDP").alinearCentro();
		tab_Coordinadores.getColumna("IDE_GEEDP").setNombreVisual("COD"); 
        
                
		tab_Coordinadores.getColumna("DETALLE_GECAF").setLongitud(20);
		tab_Coordinadores.getColumna("DETALLE_GECAF").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GECAF").setNombreVisual("PUESTO");  
        
        
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").setLongitud(100);
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").alinearCentro();
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");  

		tab_Coordinadores.getColumna("NOM_SUCU").setLongitud(50);
		tab_Coordinadores.getColumna("NOM_SUCU").alinearCentro();
		tab_Coordinadores.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");  
		
			
		tab_Coordinadores.getColumna("DETALLE_GEARE").setLongitud(120);
		tab_Coordinadores.getColumna("DETALLE_GEARE").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GEARE").setNombreVisual("AREA");  
		
		tab_Coordinadores.getColumna("DETALLE_GEDEP").setLongitud(140);
		tab_Coordinadores.getColumna("DETALLE_GEDEP").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");  
		tab_Coordinadores.onSelect("actualizarJefe");
		
		tab_Coordinadores.setRows(5);
		tab_Coordinadores.dibujar();
		
		tab_Coordinadores.setHeader("APROBADORES POR SUCURSAL");

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_Coordinadores);
    	
           
        
        tab_calendario.setId("tab_calendario");
        tab_calendario.setSql(getAsignacionEmpleado());
        tab_calendario.setCampoPrimaria("ide_astur");
        tab_calendario.getColumna("IDE_ASTUR").setLongitud(10);
        tab_calendario.getColumna("IDE_ASTUR").alinearCentro();
        tab_calendario.getColumna("IDE_ASTUR").setNombreVisual("COD");  
		
        tab_calendario.getColumna("DESCRIPCION_ASTUR").setLongitud(120);
        tab_calendario.getColumna("DESCRIPCION_ASTUR").alinearCentro();
        tab_calendario.getColumna("DESCRIPCION_ASTUR").setNombreVisual("DESCRIPCIÓN");  
	    
        
        tab_calendario.setNumeroTabla(3);
        tab_calendario.setLectura(true);
        tab_calendario.dibujar();
        tab_calendario.setHeader("HORARIO POR DÍA");

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_calendario);
    	

        /*
        
        String sql="select ide_gtemp, "
        		+ "'' as horas "
        		+ "from con_biometrico_marcaciones_resumen	 "
        		+ "order by ide_gtemp asc, fecha_evento_cobmr asc";
        
        
        
        
        
        
   		tab_sql.setId("tab_sql");
   		tab_sql.setSql(sql);
   		tab_sql.setCampoPrimaria("cod");
   		tab_sql.setLectura(true);
   		tab_sql.dibujar();
        PanelTabla pat_panelSQL = new PanelTabla();
        pat_panelSQL.setPanelTabla(tab_sql);
        */
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel,pat_panel11,"70%","H");

      /*  Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir1(pat_panel);
        */
        agregarComponente(div_division);
    }

    
    
        
    
    
    public void seleccionarReporteTotal(){
    	novedad=false;
    	sinNovedad=false;
        
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
                tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' ");
                tab_tabla.ejecutarSql();
                utilitario.addUpdate("tab_tabla");
        		//getMarcacionesEmpleado("",fechaIni,fechaFin);
				
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

			}
    		
		}
		else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
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
        int int_num_col_idegtemp=set_empleado.getTab_seleccion().getNumeroColumna("IDE_GTEMP");
        for (Fila filaActual:set_empleado.getTab_seleccion().getSeleccionados()) {
                if(str_ide.toString().isEmpty()==false){
                        str_ide.append(",");
                }
                str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
        }

       getMarcacionesEmpleado(str_ide.toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
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

		 //tab_tabla.setLectura(false);
 		//System.out.println(tab_tabla.getValor("IDE_COBMR"));
 		//System.out.println(tab_tabla.getValorSeleccionado());
		 
 		
 		
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
    

    
public TablaGenerica  obtenerPermisoXEmpleado(String FechaInicio, String FechaFin,String horaInicio, String horaFin,  Integer IDE_GTEMP){
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh,fecha_desde_aspvh,fecha_solicitud_aspvh,fecha_hasta_aspvh "
        + "from asi_permisos_vacacion_hext where "
  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaFin+"' and ide_gtemp="+IDE_GTEMP+" "
  	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"'");
  
  obtenerPermisos.imprimirSql();
  return obtenerPermisos;
 	
}
 	
 	
 	public void limpiar(){
 		tab_tabla.limpiar();
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

   	 
    	//Para resta de Horas

   	/*private String obtenerDiferenciaMinutos(Date fechaInicial, Date fechaFinal){
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
		//Diferencia en horas 
		double total_diferencia_minutos = total_diferencia_horas * 60;

		
	
		
		
		
		/*
		
		
		
		String retornaValor="";
		if (total_diferencia_minutos>=60) {
			int part_ent=(int)total_diferencia_horas;
			double resultado=total_diferencia_horas-part_ent;
			double resultadoHorasMinutos=resultado*60;
		//	String min="0."+(int)resultadoHorasMinutos;
		//	double minTotal=pckUtilidades.CConversion.CDbl(min);
		//	total_diferencia_minutos=part_ent+minTotal;
		//	retornaValor=""+total_diferencia_minutos+" hor";
		
		
		}else {
			int part_ent=(int)total_diferencia_minutos;
			double resultado=total_diferencia_minutos-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" min";
			
		}
		

		
   	
		*/
		
		
		
		//retorno la diferencia en horas
		//return total_diferencia_horas;
	//	return ""+total_diferencia_minutos;
    //}
   	
   	
   	
   	//Para resta de Minutos
   	
   	private String obtenerDiferenciaMinutos1(Date fechaInicial, Date fechaFinal){
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
		//Diferencia en horas 
		double total_diferencia_minutos = total_diferencia_horas * 60;
		String retornaValor="";
		/*if (total_diferencia_minutos>=60) {
			int part_ent=(int)total_diferencia_horas;
			double resultado=total_diferencia_horas-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" hor";
		
		
		}else {
			int part_ent=(int)total_diferencia_minutos;
			double resultado=total_diferencia_minutos-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" min";
			
		}*/
		
		
		//retorno la diferencia en horas
		//return total_diferencia_minutos;
		return retornaValor+total_diferencia_minutos;
    }
   	
   	
   	

  //Sumar horas en fecha
  	 public Date sumarRestarHorasFecha(Date fecha, int horas){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(fecha); // Configuramos la fecha que se recibe
       calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
       return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

  	 }



	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}



	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
  	 
  	 

	public void  seleccionarNovedades(){
       	String sql="SELECT res.ide_cobmr, "
    				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
    				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
    				+ "DEPA.DETALLE_GEDEP, "
    				+ "res.dia_cobmr, "
    				+ "res.fecha_evento_cobmr, "
    				+ "res.horainiciohorario_cobmr, "
    				+ "res.horainiciobiometrico_cobmr, "
    				+ "res.horainicioband_cobmr, "
    				+ "res.horainicioalm_cobmr, "
    				+ "res.horafinalm_cobmr, "
    				+ "res.horainicioalmbio_cobmr, "
    				+ "res.horafinalmbio_cobmr, "
    				+ "res.tiempoalm_cobmr, "
    				+ "res.tiempohoralm_cobmr, "
    				+ "res.horafinhorario_cobmr, "
    				+ "res.horafinbiometrico_cobmr, "
    				+ "res.horafinband_cobmr "
    				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
    				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
    				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
    				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
    				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
    				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
    				+ "where SUCU.IDE_SUCU=-1 and epar.activo_geedp=true "
    				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
    				+ "AND horainicioband_cobmr!='OK' AND horafinband_cobmr!='OK'  and tiempoalm_cobmr is null "
    				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc";
    	tab_tabla.setSql(sql);	
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    		
	}

	
	public void seleccionarSinNovedades(){
		sinNovedad=true;
		novedad=false;

        tab_tabla.setCondicion("novedad_cobmr=true and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and  '"+cal_fecha_final.getFecha()+"'");
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");		
	}
	
	
	
	public void seleccionoEmpleado(SelectEvent evt){
	
		aut_empleados.onSelect(evt);
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());
		actualizarMarcaciones(fechaIni,fechaFin,novedad,sinNovedad);	
	}


	public void actualizarMarcaciones(String fechaIni,String fechaFin,boolean novedad,boolean sinNovedad){
		
		
		
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null && aut_empleados.getValor()!=null) {
			if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
				String fechaInicioNovedad=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaIni),-1));
				String fechaFinNovedad =getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaFin),1));

				if (novedad==true) {
			 		tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' "
			 				+ "and novedad_cobmr=false ");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");
						
				}
				if (sinNovedad==true) {
			 		tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' "
			 				+ "and novedad_cobmr=true");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");
				}
				
				if (sinNovedad==false && novedad==false){
					tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' ");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");	
				}
	    	 	
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
				
			}
		}
		
				

	}



	public boolean isNovedad() {
		return novedad;
	}



	public void setNovedad(boolean novedad) {
		this.novedad = novedad;
	}



	public boolean isSinNovedad() {
		return sinNovedad;
	}



	public void setSinNovedad(boolean sinNovedad) {
		this.sinNovedad = sinNovedad;
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
				+ "res.horainicioband_cobmr, "
				+ "res.biometrico_entrada_cobmr, "			       
				+ "'' as diferenciaEntradaTimbrado, "
				+ "res.horainicioalm_cobmr,  "
				+ "res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  "
				+ "res.horafinalmbio_cobmr,  "
				+ "res.tiempoalm_cobmr, "
				+ "res.biometrico_alm_salida, "
				+ "res.biometrico_alm_entrada, "
				+ "res.tiempohoralm_cobmr, "
				+ "'' as diferenciaAlm, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr, "
				+ "res.biometrico_salida_cobmr, "
				+ "'' as diferenciaSalidaTimbrado, "
				+ "GTT.DETALLE_GTTEM, "
				+ "'' as E_FECHA_SOLICITUD, "
				+ "'' as E_TTHH, "
				+ "'' as E_JEFE_INMEDIATO, "
				+ "'' as E_FECHA_DESDE, "
				+ "'' as E_FECHA_HASTA, "
				+ "'' as E_DETALLE, "
				+ "'' as E_TIPO_PERMISO, "
				+ "'' as E_NUM_HORAS,  "
				+ "'' as E_HORA_DESDE, "
				+ "'' as E_HORA_HASTA,  "
				+ "'' as FECHA_SOLICITUD_ALM, "
				+ "'' as TTHH_ALM, "
				+ "'' as JEFE_INMEDIATO_ALM, "
				+ "'' as FECHA_DESDE_ALM, "
				+ "'' as FECHA_HASTA_ALM, "
				+ "'' as DETALLE_ALM, "
				+ "'' as TIPO_PERMISO_ALM, "
				+ "'' as NUM_HORAS_ALM,  "
				+ "'' as HORA_DESDE_ALM, "
				+ "'' as HORA_HASTA_ALM,  "
				+ "'' as FECHA_SOLICITUD_SAL, "
				+ "'' as TTHH_SAL, "
				+ "'' as JEFE_INMEDIATO_SAL, "
				+ "'' as FECHA_DESDE_SAL, "
				+ "'' as FECHA_HASTA_SAL, "
				+ "'' as DETALLE_SAL, "
				+ "'' as TIPO_PERMISO_SAL, "
				+ "'' as NUM_HORAS_SAL,  "
				+ "'' as HORA_DESDE_SAL, "
				+ "'' as HORA_HASTA_SAL,  "
				+ "'' as DIFERENCIAENTRADA, "
				+ "'' as diferenciaSalidaAlm, "
				+ "'' as diferenciaEntradaAlm, " 			
				+ "p_entrada_cobmr, " 
				+ "p_salida_cobmr, " 		
				+ "p_alm_cobmr " 

		
		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
		+ "where epar.activo_geedp=true AND emp.ide_gtemp=-1 " 
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
					+ "'' as FECHA_SOLICITUD, "
					+ "'' as TTHH, "
					+ "'' as JEFE_INMEDIATO, "
					+ "'' as FECHA_DESDE, "
					+ "'' as FECHA_HASTA, "
					+ "'' as DETALLE, "
					+ "'' as TIPO_PERMISO, "
					+ "'' as NUM_HORAS,  "
					+ "'' as HORA_DESDE, "
					+ "'' as HORA_HASTA  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
					+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '2017-12-01' and '2018-01-31' "
					+ " and inconsistencia_biometrico_cobmr is null " 
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
		}
		return sql;
			
	}
	

	
	
	
	

	public TablaGenerica getTablaReporte(String strFechaIniReporte, String strFechaFinReporte){
				
		
		TablaGenerica tabEmpleadosTimbradas=utilitario.consultar("SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
		+ "res.horainicioband_cobmr, "
		+ "'' as diferenciaEntradaTimbrado, "
		+ "res.horainicioalm_cobmr,  "
		+ "res.horainicioalmbio_cobmr,  "
		+ "res.horafinalm_cobmr,  "
		+ "res.horafinalmbio_cobmr,  "
		+ "res.tiempoalm_cobmr, "
		+ "res.tiempohoralm_cobmr, "
		+ "'' as diferenciaAlm, "
		+ "res.horafinhorario_cobmr, "
		+ "res.horafinbiometrico_cobmr, "
		+ "res.horafinband_cobmr, "
		+ "'' as diferenciaSalidaTimbrado, "
		+ "GTT.DETALLE_GTTEM, "
		+ "'' as E_FECHA_SOLICITUD, "
		+ "'' as E_TTHH, "
		+ "'' as E_JEFE_INMEDIATO, "
		+ "'' as E_FECHA_DESDE, "
		+ "'' as E_FECHA_HASTA, "
		+ "'' as E_DETALLE, "
		+ "'' as E_TIPO_PERMISO, "
		+ "'' as E_NUM_HORAS,  "
		+ "'' as E_HORA_DESDE, "
		+ "'' as E_HORA_HASTA,  "
		+ "'' as FECHA_SOLICITUD_ALM, "
		+ "'' as TTHH_ALM, "
		+ "'' as JEFE_INMEDIATO_ALM, "
		+ "'' as FECHA_DESDE_ALM, "
		+ "'' as FECHA_HASTA_ALM, "
		+ "'' as DETALLE_ALM, "
		+ "'' as TIPO_PERMISO_ALM, "
		+ "'' as NUM_HORAS_ALM,  "
		+ "'' as HORA_DESDE_ALM, "
		+ "'' as HORA_HASTA_ALM,  "
		+ "'' as FECHA_SOLICITUD_SAL, "
		+ "'' as TTHH_SAL, "
		+ "'' as JEFE_INMEDIATO_SAL, "
		+ "'' as FECHA_DESDE_SAL, "
		+ "'' as FECHA_HASTA_SAL, "
		+ "'' as DETALLE_SAL, "
		+ "'' as TIPO_PERMISO_SAL, "
		+ "'' as NUM_HORAS_SAL,  "
		+ "'' as HORA_DESDE_SAL, "
		+ "'' as HORA_HASTA_SAL,  "
		+ "'' as DIFERENCIAENTRADA, "
		+ "'' as diferenciaSalidaAlm, "
		+ "'' as diferenciaEntradaAlm, " 			
		+ "p_entrada_cobmr, " 
		+ "p_salida_cobmr, " 		
		+ "p_alm_cobmr " 

		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
		+ "where epar.activo_geedp=true and res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
		+ " and inconsistencia_biometrico_cobmr is null " 

	//	+ "AND NOVEDAD_cobmr=false "
	//	+ "and epar.ide_gtemp in(516) "
		//	+ "25,22,21,17,16,13,12,8,212,211,205,200,199,196,195,189,188,187,185,184,177,173,172,169,168,167,145,144,143,137,134,131,110,104,83,82,81,79,73,62,57,49,48,40,30,27,301,264,206 "
	//		+ "519,492,27,485,377,623,549,562,328,) "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
	tabEmpleadosTimbradas.imprimirSql();
		return tabEmpleadosTimbradas;
		
		
		//Una vez que traigo los datos calculo las horas trabajadas
		


		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
//		dos();
		
		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
//		tres();
		
		
//cuatro();
		
//	quinto();
		
	}


	
	public void justificar(){
   
		String fechaIni="";
		String fechaFin="";
	   	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		 fechaIni=(cal_fecha_inicial.getFecha());	
        		 fechaFin=(cal_fecha_final.getFecha());	
    		 
/*        	tab_tabla.setSql("SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
        			+ "res.horainicioband_cobmr, "
        			+ "'' as diferenciaEntradaTimbrado, "
        			+ "res.horainicioalm_cobmr,  "
        			+ "res.horainicioalmbio_cobmr,  "
        			+ "res.horafinalm_cobmr,  "
        			+ "res.horafinalmbio_cobmr,  "
        			+ "res.tiempoalm_cobmr, "
        			+ "res.tiempohoralm_cobmr, "
        			+ "'' as diferenciaAlm, "
        			+ "res.horafinhorario_cobmr, "
        			+ "res.horafinbiometrico_cobmr, "
        			+ "res.horafinband_cobmr, "
        			+ "'' as diferenciaSalidaTimbrado, "
        			+ "GTT.DETALLE_GTTEM, "
        			+ "'' as E_FECHA_SOLICITUD, "
        			+ "'' as E_TTHH, "
        			+ "'' as E_JEFE_INMEDIATO, "
        			+ "'' as E_FECHA_DESDE, "
        			+ "'' as E_FECHA_HASTA, "
        			+ "'' as E_DETALLE, "
        			+ "'' as E_TIPO_PERMISO, "
        			+ "'' as E_NUM_HORAS,  "
        			+ "'' as E_HORA_DESDE, "
        			+ "'' as E_HORA_HASTA,  "
        			+ "'' as FECHA_SOLICITUD_ALM, "
        			+ "'' as TTHH_ALM, "
        			+ "'' as JEFE_INMEDIATO_ALM, "
        			+ "'' as FECHA_DESDE_ALM, "
+ "'' as FECHA_HASTA_ALM, "
        			+ "'' as DETALLE_ALM, "
        			+ "'' as TIPO_PERMISO_ALM, "
        			+ "'' as NUM_HORAS_ALM,  "
        			+ "'' as HORA_DESDE_ALM, "
        			+ "'' as HORA_HASTA_ALM,  "
        			+ "'' as FECHA_SOLICITUD_SAL, "
        			+ "'' as TTHH_SAL, "
        			+ "'' as JEFE_INMEDIATO_SAL, "
        			+ "'' as FECHA_DESDE_SAL, "
        			+ "'' as FECHA_HASTA_SAL, "
        			+ "'' as DETALLE_SAL, "
        			+ "'' as TIPO_PERMISO_SAL, "
        			+ "'' as NUM_HORAS_SAL,  "
        			+ "'' as HORA_DESDE_SAL, "
        			+ "'' as HORA_HASTA_SAL,  "
        			+ "'' as DIFERENCIAENTRADA, "
        			+ "'' as diferenciaSalidaAlm, "
        			+ "'' as diferenciaEntradaAlm, " 			
        			+ "p_entrada_cobmr, " 
        			+ "p_salida_cobmr, " 		
        			+ "p_alm_cobmr " 
	*/
        	tab_tabla.setSql("SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
        			+ "res.horainicioband_cobmr, "
        					+ "res.biometrico_entrada_cobmr, "			       
        			+ "'' as diferenciaEntradaTimbrado, "
        			+ "res.horainicioalm_cobmr,  "
        			+ "res.horainicioalmbio_cobmr,  "
        			+ "res.horafinalm_cobmr,  "
        			+ "res.horafinalmbio_cobmr,  "
        			+ "res.tiempoalm_cobmr, "
        					+ "res.biometrico_alm_salida, "
        					+ "res.biometrico_alm_entrada, "
        			+ "res.tiempohoralm_cobmr, "
        			+ "'' as diferenciaAlm, "
        			+ "res.horafinhorario_cobmr, "
        			+ "res.horafinbiometrico_cobmr, "
        			+ "res.horafinband_cobmr, "
        					+ "res.biometrico_salida_cobmr, "
        			+ "'' as diferenciaSalidaTimbrado, "
        			+ "GTT.DETALLE_GTTEM, "
        			+ "'' as E_FECHA_SOLICITUD, "
        			+ "'' as E_TTHH, "
        			+ "'' as E_JEFE_INMEDIATO, "
        			+ "'' as E_FECHA_DESDE, "
        			+ "'' as E_FECHA_HASTA, "
        			+ "'' as E_DETALLE, "
        			+ "'' as E_TIPO_PERMISO, "
        			+ "'' as E_NUM_HORAS,  "
        			+ "'' as E_HORA_DESDE, "
        			+ "'' as E_HORA_HASTA,  "
        			+ "'' as FECHA_SOLICITUD_ALM, "
        			+ "'' as TTHH_ALM, "
        			+ "'' as JEFE_INMEDIATO_ALM, "
        			+ "'' as FECHA_DESDE_ALM, "
+ "'' as FECHA_HASTA_ALM, "
        			+ "'' as DETALLE_ALM, "
        			+ "'' as TIPO_PERMISO_ALM, "
        			+ "'' as NUM_HORAS_ALM,  "
        			+ "'' as HORA_DESDE_ALM, "
        			+ "'' as HORA_HASTA_ALM,  "
        			+ "'' as FECHA_SOLICITUD_SAL, "
        			+ "'' as TTHH_SAL, "
        			+ "'' as JEFE_INMEDIATO_SAL, "
        			+ "'' as FECHA_DESDE_SAL, "
        			+ "'' as FECHA_HASTA_SAL, "
        			+ "'' as DETALLE_SAL, "
        			+ "'' as TIPO_PERMISO_SAL, "
        			+ "'' as NUM_HORAS_SAL,  "
        			+ "'' as HORA_DESDE_SAL, "
        			+ "'' as HORA_HASTA_SAL,  "
        			+ "'' as DIFERENCIAENTRADA, "
        			+ "'' as diferenciaSalidaAlm, "
        			+ "'' as diferenciaEntradaAlm, " 			
        			+ "p_entrada_cobmr, " 
        			+ "p_salida_cobmr, " 		
        			+ "p_alm_cobmr " 
				
        				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
        				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
        				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
        				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
        				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
        				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
        				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
        				+ "where epar.activo_geedp=true AND res.fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'  "
						+ " and inconsistencia_biometrico_cobmr is null " 

        				//		+ "and res.horainicioband_cobmr like '%ATRASADO%' "
        				//+ "	AND NOVEDAD_cobmr=false "
        				//+ "and epar.ide_gtemp in(552) "
        				//+ "25,22,21,17,16,13,12,8,212,211,205,200,199,196,195,189,188,187,185,184,177,173,172,169,168,167,145,144,143,137,134,131,110,104,83,82,81,79,73,62,57,49,48,40,30,27,301,264,206) " 
        				+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
        				tab_tabla.ejecutarSql();
        				utilitario.addUpdate("tab_tabla");
        				 
   
    	
    	 
    	
	    int banderaJustificaEntrada=0;
	    String justificacionEntrada="";
	    String justificacionAlmuerzoSalida="";
	    String justificacionAlmuerzoEntrada="";
	    String justificacionSalida="";
		 	
		tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(1,fechaIni,fechaFin));		
		tab_tabla_sumatoria.ejecutarSql();		
		utilitario.addUpdate("tab_tabla_sumatoria");
				
	//	utilitario.addUpdate("tab_tabla_sumatoria");

			//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada entrada
	   		dos();
			
			//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
			//
			tres();
			cuatro();
			cuatroUno();
      
    		} 
    		
    		   	}
	   	
	   	 
	 
	
      
	
	   // seis();
			

	//tab_tabla_sumatoria.getTotalFilas();
	
	//tab_tabla.getTotalFilas();
	
	
	
	
	
	
	
	
	
	
	
   	}
	
	
	
	
	

	
	//DIFERENCIA ENTRE ENTRADA Y SALIDA
	
		
	//DIFERENCIA ENTRE ENTRADA Y SALIDA
	
	public void uno(){
		boolean entrada=false,salida=false;

		// ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		//double tiempoTrabajoXDia=0.0;
		double tiempoTrabajoXDia=0.0;

		
		String entradaBandera="",salidaBandera="";
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			String fecha= tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");

			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
			String banderaSalida=tab_tabla.getValor(i, "horafinband_cobmr");

			
			entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
			salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");
			
			
			
		//	entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
		//	salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");

		//System.out.println("empleado "+empleado+"  salidaBandera"+salidaBandera+" FECHA:"+fecha);
		//System.out.println("empleado "+empleado+"  entradaBandera"+entradaBandera+" FECHA:"+fecha);

			if ((banderaEntrada==null) || (banderaEntrada.isEmpty()) || (banderaEntrada.equals("")) || (banderaEntrada.equals("SIN TIMBRE")) ) {
				
				 if(entradaBandera==null || entradaBandera.equals("") || entradaBandera.isEmpty() ){
						entrada=true;
				 }else {
					 	entrada=false;
					 	entradaTimbre=entradaHorario;
				}
				//si entrada vacia o sin datos
			}else if (banderaEntrada.equals("OK")) {
				entrada=false;
			 
			}
	
			if ((banderaSalida==null) || (banderaSalida.isEmpty()) || (banderaSalida.equals("")) || (banderaSalida.equals("SIN TIMBRE")) ) {
				//Si la salida no se encuentra marcada 
				 if(salidaBandera==null || salidaBandera.equals("") || salidaBandera.isEmpty()){
					 salida=true;
				 }else {
					 salida=false;
					 salidaTimbre=salidaHorario;
				}
			}else if (banderaSalida.equals("OK")) {
				salida=false;
			}
			
		//	else {
		//		salida=false;
		//	}
			
			Date fechaIniAlmBio = null;
	    	Date fechaFinAlmBio = null;
			//Si hay salida y entrada 
			if (entrada==false && salida==false) {
				
				
				//Validamos si el timbre de la entrada es es mayor o menro q el horario planificado
				if (entradaTimbre.compareTo(entradaHorario)<=0) {
				// Si es menor le pongo la misma hora que el horario
					fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
					}else {
				//	Si es mayor escogo la hora de timbrado	
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);

					}	
				
				
				String horaTipoTurno="20:00:00";
				boolean tipoTurno=false;
				if (entradaTimbre.compareTo(horaTipoTurno)>=0) {
					tipoTurno=true;
				}else {
					tipoTurno=false;
				}
				
				if (tipoTurno==false) {
					
				
		    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
		    	 
			    	tiempoTrabajoXDia = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);

		    	//tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
		    	//(Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    //	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}else {

					
					
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaBiometrico),1))+" "+salidaTimbre);
			    //	 tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
				    	tiempoTrabajoXDia = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));

			    	 //listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
			    //	listaEmpleado.add(empleado);
					tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}
		    				}
			else {
				
			}
		}
	}
	 
	
	//DIFERENCIA ENTRE ENTRADA HORARIO Y ENTRADA TIMBRE

	public void dos(){
		boolean entrada=false,bandEntradaHorario=false;
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		double tiempoTrabajoXDia=0.0,tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
		String entradaBandera="",salidaBandera="";
		int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
 
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			//System.out.println("i: "+i+"   :"+tab_tabla.getValor(i, "ide_gtemp"));
			horaExtra=0.0;
			acumuladoVacaciones=0.0;
			
			
			empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));
			empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			
			//System.out.println("empleado"+empleado);
			//System.out.println("FECHA"+tab_tabla.getValor(i, "FECHA_EVENTO_COBMR"));
			
			   
			
			if (empleado==empleadoTemporal) {
			anteriorEmpleado=1;

			}else {
			anteriorEmpleado=0;
			y++;
			}
			
			
			
		
			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
			String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
		

			Date fechaIniAlmBio=null;
			Date fechaFinAlmBio=null;
			//Si la bandera se encuentra atrasada
			if (banderaEntrada.equals("ATRASADO") || banderaEntrada.equals("OTRO HORARIO")) {
				// Si tiene justificativo 
				 if(entradaBandera==null || entradaBandera.equals("") || entradaBandera.isEmpty() ){
						//tab_tabla.setValor(i,"diferenciaEntradaTimbrado","0.0");
						if (entradaTimbre==null || entradaTimbre.equals("") || entradaTimbre.isEmpty() ) {
						entrada=true;
				 }else {
						entrada=false;
						String[] parts = entradaTimbre.split(":");
						String part1 = parts[0]; // 123
						String part2 = parts[1]; // -654321
						entradaTimbre=part1+":"+part2+":00";
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);
				 }
											 
				 }else {
						tab_tabla.setValor(i,"diferenciaEntradaTimbrado","0.0");
						entrada=true;

					
				 }
			}
			//Si la bandera se encuentra atrasada
			if (banderaEntrada.equals("SIN TIMBRE") || banderaEntrada==null || banderaEntrada.equals("")) {
				// Si tiene justificativo 
				 if(entradaBandera==null || entradaBandera.equals("") || entradaBandera.isEmpty() ){
					if (entradaTimbre==null || entradaTimbre.equals("") || entradaTimbre.isEmpty() ) {

					 entrada=true;

				 }else {
						entrada=false;
						String[] parts = entradaBandera.split(":");
						String part1 = parts[0]; // 123
						String part2 = parts[1]; // -654321
						entradaTimbre=part1+":"+part2+":00";
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);

				 }
			}else {
				tab_tabla.setValor(i,"diferenciaEntradaTimbrado","0.0");
				entrada=true;
			}
			
			}

			if (banderaEntrada.equals("LIBRE")) {
				entrada=true;
			}
			
			/*if (banderaEntrada.equals("") || banderaEntrada==null || banderaEntrada.equals("")) {
				// Si tiene justificativo 
				 if(entradaBandera==null || entradaBandera.equals("") || entradaBandera.isEmpty() ){
					 entrada=true;	
				 }else {
					 
					entrada=true;
					tab_tabla.setValor(i,"diferenciaEntradaTimbrado","0.0");

				 }
			}*/
			
			
			
			if (banderaEntrada.equals("OK")) {
				entrada=true;
			}
			
			if (banderaEntrada.equals("EXTRA") || banderaEntrada.equals("FERIADO") || banderaEntrada.equals("LIBRE") || banderaEntrada.equals("JUSTIFICADO")) {
				entrada=true;
			}	
			
			//Si hay salida y entrada 
			if (entrada==false) {
				tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));
				 /*String p="";
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
					horaExtra=pt_entera+p1;*/
				
				
				tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));
				 /*String p="";
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
					horaExtra=pt_entera+p1;*/
			
				tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));
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
			
				//tab_tabla.setValor(i,"diferenciaEntradaTimbrado",""+horaExtra);
			
				
				Double a= acumuladoVacacionesTemp;
				double horas_temp=0.00,minutos_temp=0.00,minutos_;
				horas_temp=Double.parseDouble(utilitario.getFormatoNumero((tiempoHorasExtra100Auxiliar/60),2));
				int horas =0,minutos=0;
				horas=(int)horas_temp;
				minutos_temp=horas_temp-horas;
				minutos_=Math.round(Double.parseDouble(utilitario.getFormatoNumero((minutos_temp*60),2)));
				minutos=(int)minutos_;
				String horasMinSeg="";
			if (minutos<10) {
				horasMinSeg=horas+":0"+minutos;
			}else{
				horasMinSeg=horas+":"+minutos;}
				//tab_tabla_sumatoria.setValor(x,"ATRASO_ENTRADA",""+horasMinSeg);
				
				//tab_tabla.setValor(i,"diferenciaEntradaTimbrado",""+horaExtra);
				tab_tabla.setValor(i,"diferenciaEntradaTimbrado",""+horasMinSeg);

		    				}
			
			if (anteriorEmpleado==1) {
				acumuladoVacaciones=horaExtra;
				acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				
			
			}else {
		//		System.out.println("ACUMULADO"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);
				//CALCULO DE MINUTOS A HORAS
				Double a= acumuladoVacacionesTemp;
				double horas_temp=0.00,minutos_temp=0.00,minutos_;
				horas_temp=Double.parseDouble(utilitario.getFormatoNumero((acumuladoVacacionesTemp/60),2));
				int horas =0,minutos=0;
				horas=(int)horas_temp;
				minutos_temp=horas_temp-horas;
				minutos_=Math.round(Double.parseDouble(utilitario.getFormatoNumero((minutos_temp*60),2)));
				minutos=(int)minutos_;
				String horasMinSeg="";//horas+":"+minutos;
				if (minutos<10) {
					horasMinSeg=horas+":0"+minutos;
				}else{
					horasMinSeg=horas+":"+minutos;}

				tab_tabla_sumatoria.setValor(x,"ATRASO_ENTRADA",""+horasMinSeg);
				//System.out.println("x"+x);
				x++;
				acumuladoVacaciones=horaExtra;
				acumuladoVacacionesTemp=0;
				acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				empleadoTemporal=Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));

			//System.out.println("ACUMULADO"+empleado+" --->  "+acumuladoVacacionesTemp+"  ----->  "+tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));

		    				}
			
			
			
			
		}
		
	}
	

	 
	//DIFERENCIA ENTRE SALIDA HORARIO Y SALIDA TIMBRE

	public void tres(){
		boolean salida=false,bandSalidaHorario=false;
		double tiempoTrabajoXDia=0.0,tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;

		int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;

		/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
		*/
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();
		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		String entradaBandera="",salidaBandera="";
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
		
			horaExtra=0.0;
			acumuladoVacaciones=0.0;
			
			
			empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));
			empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			
			
			
			if (empleado==empleadoTemporal) {
			anteriorEmpleado=1;

			}else {
			anteriorEmpleado=0;
			y++;
			}
			

			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
			String banderaSalida=tab_tabla.getValor(i, "horafinband_cobmr");

			salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");
			
			
		

			
			
			
			
			Date fechaIniAlmBio=null;
			Date fechaFinAlmBio=null;
			//Si la bandera se encuentra atrasada
			if (banderaSalida.equals("ANTICIPADO") || banderaSalida.equals("OTRO HORARIO")) {
				// Si tiene justificativo 
				 if(salidaBandera==null || salidaBandera.equals("") || salidaBandera.isEmpty() ){
					if (salidaTimbre==null || salidaTimbre.equals("") || salidaTimbre.isEmpty() ) {
						salida=true;
	        	 }else {
						salida=false;
						String[] parts = salidaTimbre.split(":");
						String part1 = parts[0]; // 123
						String part2 = parts[1]; // -654321
						salidaTimbre=part1+":"+part2+":00";
				
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);

				 }
			}else {
				salida=true;
				tab_tabla.setValor(i,"diferenciaSalidaTimbrado","0.0");
			}
			
			}
			

			

			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			
			if (banderaSalida.equals("") || banderaSalida==null || banderaSalida.equals("") || banderaSalida.equals("SIN TIMBRE") ) {
				// Si tiene justificativo 
				 if(salidaBandera==null || salidaBandera.equals("") || salidaBandera.isEmpty() ){
						if (salidaTimbre==null || salidaTimbre.equals("") || salidaTimbre.isEmpty() ) {
					 salida=true;	
				 }else {
					 
					salida=false;
					String[] parts = salidaHorario.split(":");
					String part1 = parts[0]; // 123
					String part2 = parts[1]; // -654321
					salidaTimbre=part1+":"+part2+":00";

					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);  
				 }
			}else {
				salida=true;
				tab_tabla.setValor(i,"diferenciaSalidaTimbrado","0.0");
			}
			}
			
			
			
			
			
			
			if (banderaSalida.equals("OK")) {
				salida=true;
			}
			
			if (banderaSalida.equals("EXTRA") || banderaSalida.equals("FERIADO") || banderaSalida.equals("LIBRE") || banderaSalida.equals("JUSTIFICADO") ) {
				salida=true;
			}	
			
			//Si hay salida y entrada 
			if (salida==false) {
				tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));
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
			
				//tab_tabla.setValor(i,"diferenciaSalidaTimbrado",""+horaExtra)
				
	

				Double a= acumuladoVacacionesTemp;
				double horas_temp=0.00,minutos_temp=0.00,minutos_;
				horas_temp=Double.parseDouble(utilitario.getFormatoNumero((tiempoHorasExtra100Auxiliar/60),2));
				int horas =0,minutos=0;
				horas=(int)horas_temp;
				minutos_temp=horas_temp-horas;
				minutos_=Math.round(Double.parseDouble(utilitario.getFormatoNumero((minutos_temp*60),2)));
				minutos=(int)minutos_;
				String horasMinSeg="";//horas+":"+minutos;
				//tab_tabla_sumatoria.setValor(x,"ATRASO_ENTRADA",""+horasMinSeg);
				if (minutos<10) {
					horasMinSeg=horas+":0"+minutos;
				}else{
					horasMinSeg=horas+":"+minutos;}
				//tab_tabla.setValor(i,"diferenciaSalidaTimbrado",""+horaExtra);
				tab_tabla.setValor(i,"diferenciaSalidaTimbrado",""+horasMinSeg);
				
				
			}
				
				
				if (anteriorEmpleado==1) {
					acumuladoVacaciones=horaExtra;
					acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
					
				
				}else {
					//System.out.println("ACUMULADO SALIDA"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);

					Double a= acumuladoVacacionesTemp;
					double horas_temp=0.00,minutos_temp=0.00,minutos_;
					horas_temp=Double.parseDouble(utilitario.getFormatoNumero((acumuladoVacacionesTemp/60),2));
					int horas =0,minutos=0;
					horas=(int)horas_temp;
					minutos_temp=horas_temp-horas;
					minutos_=Math.round(Double.parseDouble(utilitario.getFormatoNumero((minutos_temp*60),2)));
					minutos=(int)minutos_;
					String horasMinSeg="";//horas+":"+minutos;
					if (minutos<10) {
						horasMinSeg=horas+":0"+minutos;
					}else{
						horasMinSeg=horas+":"+minutos;}				
					tab_tabla_sumatoria.setValor(x,"ATRASO_SALIDA",""+horasMinSeg);
					//System.out.println("x"+x);
					x++;
					acumuladoVacaciones=horaExtra;
					acumuladoVacacionesTemp=0;
					acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
					empleadoTemporal=Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));

				//System.out.println("ACUMULADO"+empleado+" --->  "+acumuladoVacacionesTemp+"  ----->  "+tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));

		    				}
			
							
		    
			
							
			}
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
//			double tiempoTrabajoXDia=0;
			double tiempoTrabajoXDia=0.0;
			int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
			double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
			double sumaTotal=0.0;

			
			for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
				

		
				
				
				salidaHorarioAlm=tab_tabla.getValor(i, "HORAINICIOALM_COBMR");
				salidaTimbreAlm=tab_tabla.getValor(i, "HORAINICIOALMBIO_COBMR");
				fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
					String almBandera=tab_tabla.getValor(i, "p_alm_cobmr");
				   String ide_cobmr=tab_tabla.getValor(i,"IDE_COBMR");
				    String fecha=tab_tabla.getValor(i,"FECHA_EVENTO_COBMR");
				    String horaEntrada=tab_tabla.getValor(i,"HORAINICIOBIOMETRICO_COBMR");
				    String banderaEntrada=tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");
				    String almuerzoSalidaTimbre =tab_tabla.getValor(i,"HORAINICIOALMBIO_COBMR");
				    String almuerzoEntradaTimbre=tab_tabla.getValor(i,"HORAFINALMBIO_COBMR");
				    String almuerzoSalida=tab_tabla.getValor(i,"horainicioalm_cobmr");
				    String almuerzoEntrada=tab_tabla.getValor(i,"horafinalm_cobmr");
				    String tiempoAlmuerzotomado =tab_tabla.getValor(i,"TIEMPOALM_COBMR");
				    //tiempo de almuerzo
				    String tiempohoralm=tab_tabla.getValor(i,"TIEMPOHORALM_COBMR");
				    
				    String banderaSalida=tab_tabla.getValor(i,"HORAFINBAND_COBMR");
				    String horaFin=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

				 		    
				    String horaSalida=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

				    
				    String horarioSalida=tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
			        String horarioEntrada=tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
			        double tiempoAlm=0.0;
			    	boolean banderaAlmuerzo=false;  
			    	int tiempoJustificadoAlm=0;

					empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));
					empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
					

					if (empleado==empleadoTemporal) {
					anteriorEmpleado=1;

					}else {
					anteriorEmpleado=0;
					y++;
					sumaTotal=0.0;
					}	
					
					
					if (tab_tabla.getValor(i,"TIEMPOHORALM_COBMR")==null || tab_tabla.getValor(i,"TIEMPOHORALM_COBMR").equals("") || tab_tabla.getValor(i,"TIEMPOHORALM_COBMR").isEmpty()) {
						horaExtra=0.0;
					}else {
						
						if (Double.parseDouble(tiempoAlmuerzotomado)==0.00) {
						horaExtra=0.0;
						}else if(Double.parseDouble(tiempoAlmuerzotomado)>1.00 && !almuerzoSalidaTimbre.equals("JUSTIFICADO")){
					
						
										
						String[] parts = tab_tabla.getValor(i,"TIEMPOALM_COBMR").split("\\.");
						String part1 = parts[0]; // 123
						String part2 = parts[1]; // -654321
						int pt_entera=Integer.parseInt(part1);
						int valorPart1=0;
						double valorPart2=0.0;
						double valorPart3=Double.parseDouble(tiempoAlmuerzotomado)-pt_entera;
						if (valorPart3<1.00) {
							valorPart1=0;
							valorPart2=valorPart1+(valorPart3*60);
						}else if(valorPart3>1.00) {
							int nuevaEntero=pt_entera-1;
							valorPart1=(nuevaEntero*60);
							valorPart2=valorPart1+(Double.parseDouble("0."+part2)*60);
						}
						horaExtra=valorPart2;						
					}else {
							horaExtra=0.0;
						}

					}
					
					if (anteriorEmpleado==1) {
						acumuladoVacaciones=horaExtra;
						acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
						
					
					}else {
						String horasMinSeg="";
						Double a= acumuladoVacacionesTemp;
						double horas_temp=0.00,minutos_temp=0.00,minutos_;
						horas_temp=Double.parseDouble(utilitario.getFormatoNumero((acumuladoVacacionesTemp/60),2));
						int horas =0,minutos=0;
						horas=(int)horas_temp;
						minutos_temp=horas_temp-horas;
						minutos_=Math.round(Double.parseDouble(utilitario.getFormatoNumero((minutos_temp*60),2)));
						minutos=(int)minutos_;
						if (minutos<10) {
							horasMinSeg=horas+":0"+minutos;
						}else{
							horasMinSeg=horas+":"+minutos;}
						
						
															
						
				//		System.out.println("ACUMULADO"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);
						tab_tabla_sumatoria.setValor(x,"ATRASO_ALMUERZO",""+horasMinSeg);
						//System.out.println("x"+x);
						x++;
						acumuladoVacaciones=horaExtra;
						acumuladoVacacionesTemp=0;
						acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
						empleadoTemporal=Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));

		
						
					//System.out.println("ACUMULADO"+empleado+" --->  "+acumuladoVacacionesTemp+"  ----->  "+tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));

					}
					
			
					
				}
			    	
			    	
		
		
		}
		

		
		
		
	//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
	public void cuatro(){
		
		
		
		boolean salidaAlm=false,bandSalidaHorarioAlm=false;

		/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
		*/
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//		double tiempoTrabajoXDia=0;
		double tiempoTrabajoXDia=0.0;
        

		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			
			
			
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			
			
			
			//entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			//entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			
			
			
			
			salidaHorarioAlm=tab_tabla.getValor(i, "HORAINICIOALM_COBMR");
			salidaTimbreAlm=tab_tabla.getValor(i, "HORAINICIOALMBIO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
		//	System.out.println(fechaBiometrico);
				String almBandera=tab_tabla.getValor(i, "p_alm_cobmr");
			   String ide_cobmr=tab_tabla.getValor(i,"IDE_COBMR");
			    String fecha=tab_tabla.getValor(i,"FECHA_EVENTO_COBMR");
			    String horaEntrada=tab_tabla.getValor(i,"HORAINICIOBIOMETRICO_COBMR");
			    String banderaEntrada=tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");
			    String almuerzoSalidaTimbre =tab_tabla.getValor(i,"HORAINICIOALMBIO_COBMR");
			    String almuerzoEntradaTimbre=tab_tabla.getValor(i,"HORAFINALMBIO_COBMR");
			    String almuerzoSalida=tab_tabla.getValor(i,"horainicioalm_cobmr");
			    String almuerzoEntrada=tab_tabla.getValor(i,"horafinalm_cobmr");
			    String tiempoAlmuerzo =tab_tabla.getValor(i,"TIEMPOALM_COBMR");
			    //tiempo de almuerzo
			    String tiempohoralm=tab_tabla.getValor(i,"TIEMPOHORALM_COBMR");
			    
			    String banderaSalida=tab_tabla.getValor(i,"HORAFINBAND_COBMR");
			    String horaFin=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

				    String justificativoAlm=tab_tabla.getValor(i, "p_alm_cobmr");
			
			
			 		    
			    String horaSalida=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

			    
			    String horarioSalida=tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
		        String horarioEntrada=tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
		        double tiempoAlm=0.0;
		    	boolean banderaAlmuerzo=false;  
		    	int tiempoJustificadoAlm=0;
			
			
			//Reviso si tengo almuerzo para lo cual tengo tres casos 30,60,0
				if (tiempohoralm.equals("0.5") || tiempohoralm.equals("1.00")) {
				//Si tengo almuerzo
				
					if (tiempoAlmuerzo=="" || tiempoAlmuerzo.isEmpty() || tiempoAlmuerzo==null ) {
					
					if (almBandera==null || almBandera.equals("") || almBandera.isEmpty()) {
						tiempoAlm=0;	
					}else {
							String[] parts = tiempoAlmuerzo.split(":");
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // -654321
							tiempoAlmuerzo=part1+":"+part2+":00";
							
							
							tiempoAlm=(Double.parseDouble(tiempoAlmuerzo))*60;
					}
						
						
				}else {
					//tiempoAlm=(Double.parseDouble(tiempoAlmuerzo))*60;		
					tiempoAlm=(Double.parseDouble(tiempoAlmuerzo));		

					
				}
				
				
				banderaAlmuerzo=true;    	
				if (tiempohoralm.equals("0.5") && tiempoAlm>0) {
					tiempoJustificadoAlm=30;
						getPermisoTiempoAlm(fecha, empleado,tiempoAlm,tiempoJustificadoAlm,i,30,justificativoAlm);
		
				    
				    
				    
				    }
					if (tiempohoralm.equals("1.00") && tiempoAlm>0) {
						getPermisoTiempoAlm(fecha, empleado,tiempoAlm,tiempoJustificadoAlm,i,60,justificativoAlm);
				}
				
			}else {
				//No tengo almuerzo
					getPermisoTiempoAlm(fecha, empleado,tiempoAlm,tiempoJustificadoAlm,i,60,justificativoAlm);
			}

		
	}
	
	}
	





				
		
			
				

				

				
				

				    	 


					
	
	
	
	public void buscarPersonalMensualOperativo(){
		matriz=false;
		operativo=true;
		tab_tabla.setSql("SELECT res.ide_cobmr,documento_identidad_gtemp, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horainicioband_cobmr, "
				+ "res.horainicioalm_cobmr, "
				+ "res.horafinalm_cobmr, "
				+ "res.horainicioalmbio_cobmr, "
				+ "res.horafinalmbio_cobmr, "
				+ "res.tiempoalm_cobmr, "
				+ "res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
			tab_tabla.ejecutarSql();
			
			
			tab_Coordinadores.setSql("SELECT epar.ide_geedp,CAF.DETALLE_GECAF, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN gen_cargo_funcional CAF ON  CAF.IDE_GECAF =EPAR.IDE_GECAF "
					+ "where epar.activo_geedp=true and emp.activo_gtemp=true "
					+ "and EPAR.IDE_GEEDP IN(487,444,618,667,823,838,746,1024,581) "
					//+ "AND CAF.IDE_GECAF IN (11,2,18,13) AND EPAR.IDE_GEEDP IN(487,444,618,667,823,838,746,1101,1024,581) "
					//+ "and sucu.ide_sucu !=1 "
					+ "ORDER BY AREA.DETALLE_GEARE asc");
			
			
			
					tab_Coordinadores.ejecutarSql();

			
			utilitario.addUpdate("tab_tabla,tab_Coordinadores");
	}

	public String  getConsultaSucursal(){
	String sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
			+ "res.horainicioband_cobmr, "
			+ "res.horainicioalm_cobmr,  "
			+ "res.horafinalm_cobmr,  "
			+ "res.horainicioalmbio_cobmr,  "
			+ "res.horafinalmbio_cobmr,  "
			+ "res.tiempoalm_cobmr, "
			+ "res.tiempohoralm_cobmr, "
			+ "res.horafinhorario_cobmr, "
			+ "res.horafinbiometrico_cobmr, "
			+ "res.horafinband_cobmr, "
			+ "GTT.DETALLE_GTTEM, "
			+ "'' as FECHA_SOLICITUD, "
			+ "'' as TTHH, "
			+ "'' as JEFE_INMEDIATO, "
			+ "'' as FECHA_DESDE, "
			+ "'' as FECHA_HASTA, "
			+ "'' as DETALLE, "
			+ "'' as TIPO_PERMISO, "
			+ "'' as NUM_HORAS,  "
			+ "'' as HORA_DESDE, "
			+ "'' as HORA_HASTA  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
			+ "where SUCU.IDE_SUCU=-1 and epar.activo_geedp=true "
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
			/*+ "SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP, "
			+ "res.dia_cobmr, "
			+ "res.fecha_evento_cobmr, "
			+ "res.horainiciohorario_cobmr, "
			+ "res.horainiciobiometrico_cobmr, "
			+ "res.horainicioband_cobmr, "
			+ "res.horainicioalm_cobmr, "
			+ "res.horafinalm_cobmr, "
			+ "res.horainicioalmbio_cobmr, "
			+ "res.horafinalmbio_cobmr, "
			+ "res.tiempoalm_cobmr, "
			+ "res.tiempohoralm_cobmr, "
			+ "res.horafinhorario_cobmr, "
			+ "res.horafinbiometrico_cobmr, "
			+ "res.horafinband_cobmr, "
			+ "GTT.DETALLE_GTTEM, "
			+ "'' as diferenciaEntrada, "
			+ "'' as diferenciaEntradaTimbrado, "

			+ "'' as diferenciaSalidaTimbrado, "
			+ "'' as diferenciaSalidaAlm, "
			+ "'' as diferenciaEntradaAlm "

			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM "

			+ "where SUCU.IDE_SUCU=-1 and epar.activo_geedp=true "
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
	*/
	
	return sql;
	}


	public String  getCoordinadores(){
		
		String sql="SELECT epar.ide_geedp,CAF.DETALLE_GECAF, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "LEFT JOIN gen_cargo_funcional CAF ON  CAF.IDE_GECAF =EPAR.IDE_GECAF "
				+ "where epar.activo_geedp=true and emp.activo_gtemp=true "
				+ "AND CAF.IDE_GECAF IN (11,2,18) "
				+ "and sucu.ide_sucu =-1 "
				+ "ORDER BY AREA.DETALLE_GEARE asc";
		
		
		return sql;
	}
	
	
	
	public void  retornaAsignacionEmpleado(){
		
		TablaGenerica  tabAsignacion=utilitario.consultar("select ide_cobmr,ide_gtemp,fecha_evento_cobmr from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
		
		if (tabAsignacion.getTotalFilas()>0) {
			
			String sql="SELECT *  FROM asi_horario_mes_empleado WHERE ide_gtemp="+tabAsignacion.getValor("IDE_GTEMP")+" AND IDE_GEMES=9 AND IDE_GEANI=10";
			tab_calendario.ejecutarSql();
			utilitario.addUpdate("tab_calendarios");
		}
		
		
	}
	
	
	
	public String   getAsignacionEmpleado(){
		String sql="SELECT ide_astur,descripcion_astur FROM asi_turnos WHERE ide_astur=-1";
		
		
		return sql;
	}
	
	public Tabla getTab_Coordinadores() {
		return tab_Coordinadores;
	}



	public void setTab_Coordinadores(Tabla tab_Coordinadores) {
		this.tab_Coordinadores = tab_Coordinadores;
	}



	public Tabla getTab_calendario() {
		return tab_calendario;
	}



	public void setTab_calendario(Tabla tab_calendario) {
		this.tab_calendario = tab_calendario;
	}

	
	public void   getHorarioEmpleado(SelectEvent evt){
		tab_tabla.seleccionarFila(evt);
		
		TablaGenerica tabEmpleado =utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());

		TablaGenerica tabDia=utilitario.consultar("SELECT ide_ashme, ide_gtemp,dia"+utilitario.getDia(tabEmpleado.getValor("fecha_evento_cobmr"))+" FROM asi_horario_mes_empleado WHERE ide_gtemp="+tabEmpleado.getValor("IDE_GTEMP"));
		String ide_astur=tabDia.getValor("DIA"+utilitario.getDia(tabEmpleado.getValor("fecha_evento_cobmr")));
		
		String sql="SELECT ide_astur, descripcion_astur FROM asi_turnos WHERE ide_astur="+ide_astur;
		tab_calendario.setSql(sql);
		tab_calendario.ejecutarSql();
		utilitario.addUpdate("tab_calendario");
	}
	

	public void buscarSinEntrada(){
		
		if (matriz) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true  "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horainicioband_cobmr='' and  res.horafinband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823)  "
					+ "ORDER BY DEPA.DETALLE_GEDEP asc");
				tab_tabla.ejecutarSql();
		}else {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horainicioband_cobmr='' and  res.horafinband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823) "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
					tab_tabla.ejecutarSql();
				
		}
		
	}
	
	
	public void buscarSinSalida(){
		
		if (matriz) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true  "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr='' and  res.horainicioband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823,310,311,101) "
					+ "ORDER BY DEPA.DETALLE_GEDEP asc");
				tab_tabla.ejecutarSql();
		}else {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horafinband_cobmr ='' and  res.horainicioband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823,310,311,101) "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
					tab_tabla.ejecutarSql();
				
		}
		
	}
	

	
	public void buscarCambioHorario(){
		
		if (matriz==true) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true   "
					+ "AND RES.FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr like'%SIN%' AND res.horainicioband_cobmr like'%SIN%'  and epar.ide_geedp not in(487,823,310,311,101) "
					+ "GROUP BY  "
					+ "res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "EMP.APELLIDO_PATERNO_GTEMP, "
					+ "EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr,  "
					+ "res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr,  "
					+ "res.horainicioband_cobmr,  "
					+ "res.horainicioalm_cobmr,  "
					+ "res.horafinalm_cobmr,  "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr,  "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
				tab_tabla.ejecutarSql();
		}
		
		
		if (operativo==true) {
		/*	tab_tabla.setSql("SELECT res.ide_cobmr, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU not in(1) and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");*/
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true   "
					+ "AND RES.FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' and epar.ide_geedp not in(487,823,310,311,101)  "
					+ "GROUP BY  "
					+ "res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP, "
					+ "EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr,  "
					+ "res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr,  "
					+ "res.horainicioband_cobmr,  "
					+ "res.horainicioalm_cobmr,  "
					+ "res.horafinalm_cobmr,  "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr,  "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
		
					tab_tabla.ejecutarSql();
				
		}		
	
	}
	
	
	
	public void actualizarJefe(SelectEvent evt){
		tab_Coordinadores.seleccionarFila(evt);

		tab_Coordinadores.getValorSeleccionado();
		
		tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horainicioband_cobmr, "
				+ "res.horainicioalm_cobmr, "
				+ "res.horafinalm_cobmr, "
				+ "res.horainicioalmbio_cobmr, "
				+ "res.horafinalmbio_cobmr, "
				+ "res.tiempoalm_cobmr, "
				+ "res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr "

				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "where epar.activo_geedp=true "
				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
				+ "and res.ide_geedp="+tab_Coordinadores.getValorSeleccionado()+ "  " 
				//+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' "
				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
				tab_tabla.ejecutarSql();
			
		
		
		
	}
	
	
	
	public void seleccionArea(){
	set_area.dibujar();


	//utilitario.addUpdate("set_area");
		
	}
	
	

	
	public void getArea(){
		  area=set_area.getValorSeleccionado();
		 set_area.cerrar();
	
		
		  set_areaDepartamento.setSeleccionTabla("SELECT EPAR.IDE_GEEDP,   "
		  		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    "
		  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
		  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		  		+ "DEPA.DETALLE_GEDEP,gecaf.detalle_gecaf "
		  		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
		  		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		  		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		  		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		  		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
		  		+ "left join gen_cargo_funcional gecaf on gecaf.ide_gecaf=epar.ide_gecaf  "
		  		+ "where epar.ide_geare="+area+" and epar.activo_geedp=true "
		  		+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ","IDE_GEEDP");
		
		
		
		set_areaDepartamento.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("NOMBRES");
		set_areaDepartamento.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		
		set_areaDepartamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_areaDepartamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		
		set_areaDepartamento.getTab_seleccion().getColumna("detalle_gecaf").setNombreVisual("CARGO");
		set_areaDepartamento.getTab_seleccion().getColumna("detalle_gecaf").setFiltro(true);
		
		
		//set_area.setRadio();
		set_areaDepartamento.setTitle("Seleccione Empleados");
		gru_pantalla.getChildren().add(set_areaDepartamento);
		set_areaDepartamento.getBot_aceptar().setMetodo("getEmpleadoDepartamento");
    	set_areaDepartamento.redibujar();
	   	utilitario.addUpdate("set_areaDepartamento,set_area");

    	

	}

	
	public void  getEmpleadoDepartamento(){
    	set_areaDepartamento.cerrar();

		
		  String empleados=set_areaDepartamento.getSeleccionados();
		  
		  tab_tabla.setSql("SELECT res.ide_cobmr, documento_identidad_gtemp,"
				  	+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where epar.activo_geedp=true and "
					+ "epar.ide_geedp in("+set_areaDepartamento.getSeleccionados()+") "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
 
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
		  tab_tabla.ejecutarSql();
			set_areaDepartamento.cerrar();
			utilitario.addUpdate("tab_tabla,set_areaDepartamento");
			
			if (tab_tabla.getTotalFilas()<=0) {
				utilitario.agregarMensaje("No existen datos para la fecha selecciona", "Fecha inicio: "+cal_fecha_inicial.getFecha()+" y Fecha Fin: "+cal_fecha_final.getFecha());
				return;
			}


	}


	public SeleccionTabla getSet_area() {
		return set_area;
	}



	public void setSet_area(SeleccionTabla set_area) {
		this.set_area = set_area;
	}



	public SeleccionTabla getSet_areaDepartamento() {
		return set_areaDepartamento;
	}



	public void setSet_areaDepartamento(SeleccionTabla set_areaDepartamento) {
		this.set_areaDepartamento = set_areaDepartamento;
	}



	public void setArea(String area) {
		this.area = area;
	}



	public Tabla getTab_sql() {
		return tab_sql;
	}



	public void setTab_sql(Tabla tab_sql) {
		this.tab_sql = tab_sql;
	}
	
	
    public void justificarTimbradas(){
    	
		
    	try {
    		//System.out.println("seleccionado : "+tab_tabla.getValorSeleccionado());
    		
    		
    		tab_tabla.setLectura(false);

		    int banderaJustificaEntrada=0;
		    String justificacionEntrada="";
		    String justificacionAlmuerzoSalida="";
		    String justificacionAlmuerzoEntrada="";
		    String justificacionSalida="";
		   // int banderaEntrada=0;
		    //int banderaSalida=0;

/*		    if (aut_empleados.getValor()==null) {
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

	*/	    
		    
    		//Si hay datos en la pantalla las recorro uno a uno por empleado
		    
		      
		    for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
		    	
		   // TablaGenerica  tabMarcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
		    
		    //Obtengo los datos de cada fila que son entrada ,salida, salida almuerzo, entrada salida para validar
		    	//Variables de inicio
		    String ide_cobmr=tab_tabla.getValor(i,"IDE_COBMR");
		    int empleado= Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP"));
	        String fecha=tab_tabla.getValor(i,"FECHA_EVENTO_COBMR");
		    String horaEntrada=tab_tabla.getValor(i,"HORAINICIOBIOMETRICO_COBMR");
		    String banderaEntrada=tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");
		    String almuerzoSalidaTimbre =tab_tabla.getValor(i,"HORAINICIOALMBIO_COBMR");
		    String almuerzoEntradaTimbre=tab_tabla.getValor(i,"HORAFINALMBIO_COBMR");
		    String almuerzoSalida=tab_tabla.getValor(i,"horainicioalm_cobmr");
		    String almuerzoEntrada=tab_tabla.getValor(i,"horafinalm_cobmr");
		    String tiempoAlmuerzo =tab_tabla.getValor(i,"TIEMPOALM_COBMR");
		    //tiempo de almuerzo
		    String tiempohoralm=tab_tabla.getValor(i,"TIEMPOHORALM_COBMR");
		    
		    String banderaSalida=tab_tabla.getValor(i,"HORAFINBAND_COBMR");
		    String horaFin=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

		 		    
		    String horaSalida=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");
	
		    
		    String horarioSalida=tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
	        String horarioEntrada=tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
	        
	        

		    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
		    String  permiso;
		    String  nro_horas;
		    boolean banderaEntradaIngreso=false,banderaSalidaIngreso=false,banderaAlmuerzo=false;
		    int valorTipo=0;
    		
    		//Bandera de Entrada
		    if (banderaEntrada=="" || banderaEntrada.isEmpty()) {
    			//Si la bandera se encuentra vacia
		    	if (horarioEntrada.equals("") || horarioEntrada.equals("")) {
    				//Busco si contiene horario asignado
		    		banderaEntradaIngreso=false;
				}else {
					//Si tiene horario
					banderaEntradaIngreso=true;
					horaEntrada=horarioEntrada;
				}
    		}
    			

		    
		    if (banderaSalida.equals("") || banderaSalida.isEmpty()) {
        			if (horarioSalida.equals("") || horarioSalida.isEmpty()) {
            				banderaSalidaIngreso=false;
        				}else {
        					banderaSalidaIngreso=true;
        					horaSalida=horarioSalida;
        				}
				
    			}
		    
		    
		    double tiempoJustificadoAlm=0.0;
		    
    		//Reviso si tengo almuerzo para lo cual tengo tres casos 30,60,0
    		if (tiempohoralm.equals("30") || tiempohoralm.equals("60")) {
    			//Si tengo almuerzo
    			banderaAlmuerzo=true;    	
    			if (tiempohoralm.equals("30")) {
    				tiempoJustificadoAlm=30;
				}
    			if (tiempohoralm.equals("60")) {
    				tiempoJustificadoAlm=60;
				}
    			
    		}else {
    			//No tengo almuerzo
    			banderaAlmuerzo=false;    		    		
			}


    	
    		
    		
    	if (banderaAlmuerzo==true) {
    		//Si tengo almuerzo
    		if (!almuerzoSalidaTimbre.equals("")) {
    			
    			getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalidaTimbre, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);
			}else {
				getPermisoHorarioAlmuerzo(fecha, empleado, almuerzoSalida, horarioEntrada,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);

			}
		}
    		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   		
    		
    		
    	//Si tiene horario y hora de entrada	
    			if (banderaEntradaIngreso==true) {
    			getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,i,0,ide_cobmr);
    		}
    		
    			if (banderaEntrada.equals("SIN TIMBRE")) {
	    			getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,i,1,ide_cobmr);		
        		}
    		
  
    			if (banderaEntrada.equals("ATRASADO")) {
    				getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,i,2,ide_cobmr);		
    				}
	
	
	
	
	
	/*
			if (banderaSalidaIngreso==true) {
   			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,i);
			}
			
		
    		if (banderaSalida.equals("ANTICIPADO")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		    			
			}
    		
    		
    		if (banderaSalida.equals("SIN TIMBRE")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
    		}
    		
    		/*    		if (banderaSalida.equals("EXTRA")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
    		}
    		
    		if (banderaSalida.equals("FERIADO")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
    		}
    		
    		*/    		


  
		}

    		
		      tab_tabla.setCondicion("fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and  '"+cal_fecha_final.getFecha()+"'");
		      	//	tab_tabla.imprimirSql();
		      		tab_tabla.ejecutarSql();
		      		utilitario.addUpdate("tab_tabla");

		    
		    for (int j = 0; j < tab_tabla.getTotalFilas(); j++) {
				
	
		    
    		
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("JUSTIFICADO") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
	}

//	if (tab_tabla.getValor(i, "HORAINICIOBAND_COBMR").contains("OK") && tab_tabla.getValor(i, "HORAINICIOALMBIO_COBMR").contains("S/A") && tab_tabla.getValor(i, "HORAFINBAND_COBMR").contains("OK")) {
//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+ide_cobmr);

//	}

if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("OK") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}


if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("ATRASADO") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}

if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("EXTRA") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("EXTRA")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}

//nOVEDAD ES FALSA
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("EXTRA") && (tab_tabla.getValor(j, "HORAFINBAND_COBMR")==null || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("") || tab_tabla.getValor(j, "HORAFINBAND_COBMR").isEmpty())) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=false where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("SIN TIMBRE") && (tab_tabla.getValor(j, "HORAFINBAND_COBMR")==null || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("") || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("SIN TIMBRE"))) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=false where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}








			}
	    guardarPantalla();
		    
		    
		    
		  //  }

		    
    	} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}		
			
			
    }
    
    
    
    
    
    
    
    
    
    private Boolean getPermisoHorario(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,int j,int estadoEntrada,String ide_cobmr){
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene los permisos para justificar
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh,aprobado_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
        		+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 20))+"' and anulado_aspvh =false ");
    	
    	
    	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
    			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
    	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
    	
    	if (tab_novedad.getTotalFilas()<0) {
    		//si no tiene permisos ingresados en ese rango de fechas
    		valorRetorno=false;
    		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

    	}else {
  	
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
    	
    		//Obtengo los dias de los permisos consultados hasta tres dias antes
    		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
    		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

    	
    			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
    				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
    				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
    				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
    				//cALFECHAiNICIO ES MENOR QUE  FECHA 
    				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
    			//Encuentro si existe permiso para esa fecha
    			
    			//Obtengo el tipo de permiso si es por dias o por horas
    			String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
    			String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
    			String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
    			String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
    			String horaDesdeAspvh="";
    			String horaHastaAspvh="";
    			String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
    			
    	
    			
    			String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
    			String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
    			
    		

    			
    			//tipo dias justifica todo
    			if (tipoPermiso.equals("4")) {
    			
    			String mensaje="";
    			int banderaAprobacion=0;
    			String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");

    			mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
    					+ "  "+detalleAspvh;

    			
				
    
    			
    			
    			if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
    			{
    				banderaAprobacion=1;
    		
    			/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
    			+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
    				*/
    				
    				tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
        			tab_tabla.setValor(j, "E_TTHH","APROBADO");
        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
        			tab_tabla.setValor(j, "E_TIPO_PERMISO","DIAS");
        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
        			tab_tabla.setValor(j, "E_HORA_DESDE","");
        			tab_tabla.setValor(j, "E_HORA_HASTA","");
        			
        			
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
        	    			
        			
        			
        	
        		
    			
    			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
    				banderaAprobacion=2;
    				
    	/*			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
    				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
*/
    				
    				tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
        			tab_tabla.setValor(j, "E_TIPO_PERMISO","DIAS");
        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
        			tab_tabla.setValor(j, "E_HORA_DESDE","");
        			tab_tabla.setValor(j, "E_HORA_HASTA","");
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
        	    	
        		
    	
    				
    			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
    				banderaAprobacion=3;
    				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
    				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);
    				}*/
    				
    				
    				tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","PENDIENTE");
        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
        			tab_tabla.setValor(j, "E_TIPO_PERMISO","DIAS");
        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
        			tab_tabla.setValor(j, "E_HORA_DESDE","");
        			tab_tabla.setValor(j, "E_HORA_HASTA","");
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
        	    	
        		
    	
    			}else {
    				
    			}
    			
    			//tab_tabla.ejecutarSql();
    			//utilitario.addUpdate("tab_tabla");
    			
    				
    				//saco si es de tipo de permiso si es por dias 4  o horas 1
    				valorRetorno= true;
    				//System.out.println("permiso ingresado por dias"+valorRetorno);
    		
    			}else {
    				//permiso ingresadio por dias
    			
    				//Obtengo los valores 
    				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
    				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
    				String mensaje="";
    				int estadoPermiso=0;
    				int contieneDiaTurno=0,ide_gtgre=0;
    				
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;
    				
    				
    				
    				
    				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
    				{estadoPermiso=1;
    					

    				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
     
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=2;

    				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=3;
    	
    				}else {
    					
    				}
    				
    				
    				
    				
    				try {
						String myDateString =""; //La hora con forma de String
						
						if (horaTimbre.length()<6) {
							myDateString = horaTimbre+":00";
						}else{
							myDateString = horaTimbre;
						}

						//Creamos la hora con formato del api Java
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						Date date = sdf.parse(myDateString);

						//Podemos asignar la hora a una fecha
						Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
						calendar.setTime(date);   // assigns calendar to given date
						//Podemos recuperar la hora, minuto, etc. de la fecha 
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute=calendar.get(Calendar.MINUTE);
						
						
					
						
						
						String min="",hora="";
						if (minute<10) {
						   min="0"+minute+":00";

						}else {
							 min=""+minute+":00";

						}
						
						
						
						if (hour<10) {
							  hora="0"+hour;

						}else {
							  hora=""+hour;

						}
						
					
						
						
						
						//Obtengo si tiene matriz
	    	       		int mes=0;
	    	       		mes=utilitario.getMes(fecha);
	    	       		int anioTemp=0;
	    	       		anioTemp=utilitario.getAnio(fecha);
	    	       		TablaGenerica tab_Anio=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+anioTemp+"%'");
	    	       		int anio=Integer.parseInt(tab_Anio.getValor("IDE_GEANI"));
	    	       		int dia=0;
	    	       		dia=utilitario.getDia(fecha);
	    	       		
	    	       		//Variables para asignacion Entrada,Salida,Almuerzo Entrada, Almuerzo Salida
	    	       		
	    	       		
	    	       		 contieneDiaTurno=getHorarioXDia(empleado,dia,mes,anio);
	    	       		if (contieneDiaTurno==0) {
	    	       			ide_gtgre=0;
	    	       		}else {
	    	       				//Si tiene asignado matriz para ese dia
	    	       		      TablaGenerica tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
	    	       		  		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
	    	       		}
	    	       		  	    	        
	    	       	
	    	       		

	    				if (ide_gtgre!=0) {
							
	    					
				   String fechaTimbreNocturno="",fechaHoraHasta="",fechaHoraDesde="",fechaHastaSuma="",horaResultado="";
				   fechaTimbreNocturno=fecha+" "+horaTimbre;
	    					

			
				
				   
		           	Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturno),-15);
		           	String minTolerancia="",horaTolerancia="";
		        	
		           	
		           
		           	
		           	
		           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(finEntrada);   // assigns calendar to given date
					calendar.getTime();
					//Podemos recuperar la hora, minuto, etc. de la fecha 
					//int minutes=calendarEntrada.get(Calendar.MINUTE);
					//int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes=finEntrada.getMinutes();
		            	
					int hours = finEntrada.getHours();
					if (minutes<10) {
						   minTolerancia="0"+minutes+":00";

						}else {
							 minTolerancia=""+minutes+":00";
						}
										
						
						if (hours<10) {
							horaTolerancia="0"+hours;

						}else {
							horaTolerancia=""+hours;

						}
						
						horaResultado=horaTolerancia+":"+minTolerancia+"";
						fechaTimbreNocturno=fecha+" "+horaResultado;
	    				
//Aqui le vamos a sumar un dia	    								
							Calendar calFechaHoraBiometrico = Calendar.getInstance();
							calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturno));
							
							
							Calendar calHoraFechaFinConsulta = Calendar.getInstance();

							String fechaTimbreNocturnoHorarioEntrada="",fechaTimbreNocturnoHorarioSalida="",fechaBaseHorarioNocturno="",fechaTerminHorarioNocturno;
							fechaTimbreNocturnoHorarioEntrada=fecha+" "+horaHorarioEntrada;
							Calendar calFechaHoraHorarioBiometricoEntrada = Calendar.getInstance();
							calFechaHoraHorarioBiometricoEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturnoHorarioEntrada));
						
							fechaTimbreNocturnoHorarioSalida=fecha+" "+horaHorarioSalida;
							Calendar calFechaHoraHorarioBiometricoSalida = Calendar.getInstance();
							calFechaHoraHorarioBiometricoSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturnoHorarioSalida));
						
							fechaBaseHorarioNocturno=fecha+" "+"24:00:00";
							Calendar calFechaHoraBase = Calendar.getInstance();
							calFechaHoraBase.setTime(getFechaAsyyyyMMddHHmmss(fechaBaseHorarioNocturno));
						
							fechaHoraDesde=fecha+" "+hora_desde_aspvh;
							Calendar calHoraFechaDesdeTimbreNocturno = Calendar.getInstance();
							calHoraFechaDesdeTimbreNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraDesde));
						
							fechaTerminHorarioNocturno=fecha+" "+horaHorarioSalida;
							Calendar calFechaHoraTermina = Calendar.getInstance();
							calFechaHoraHorarioBiometricoSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaTerminHorarioNocturno));
						
							horaTimbre=""+hora+":"+min+"";
							
							if (estadoEntrada==1 || estadoEntrada==0) {
								
								fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
								fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),1));
					    		calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));


							}else {
								if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioBiometricoEntrada)<=0 && calFechaHoraBiometrico.compareTo(calFechaHoraBase)<=0) {
									fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
									fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),0));
									calHoraFechaFinConsulta = Calendar.getInstance();
									calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));
			
								}else if (calFechaHoraBiometrico.compareTo(calFechaHoraBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioBiometricoSalida)<=0){
									
									fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
									fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),1));
						    		calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));

								} 								
							}

							
							//Comparando las horas del permiso
							   if (calFechaHoraBiometrico.compareTo(calHoraFechaDesdeTimbreNocturno)<=0 && calFechaHoraBiometrico.compareTo(calHoraFechaFinConsulta) <= 0){
	
							
							//Si es aprobado po thh
							if (estadoPermiso==1) {
	 
	    						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "E_TTHH","APROBADO");
	    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
	    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
	    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
	    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
	    	        	    	
	    	        		
	    	    	
	    					
	    					
	    					}//Si es aprobado por jefe inmediato
	    					if (estadoPermiso==2) {
	      						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
	    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
	    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
	    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
	    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
	    	        	    	
	    					
	    					}
	    					//Si esta ingresado
	    					if (estadoPermiso==3) {
	    						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
	    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","PENDIENTE");
	    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
	    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
	    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
	    	        	    	
	    	        		
	    					}
								    		

							   }
							
							
//////////////////////////////////////////////////////////////////////AQUI CUANDO NO ES NOCTURNO/////////////////////////////////////////////////////////////
	    				}else {
							
						
	    	       		
	    	       		
							
							
							
	    	       			
												
						
						if (hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
							horaTimbre=""+hora+":"+min+"";
							
						}else {
							 horaTimbre=""+hora+":"+min+"";
							//Sumar horas en fecha
							 
		         			    String fechaHoraHorarioFinEntrada = fecha+" "+horaTimbre;

		           	Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada),-15);
		           	String minTolerancia="",horaTolerancia="";
		        	
		           	
		           
		           	
		           	
		           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(finEntrada);   // assigns calendar to given date
					calendar.getTime();
					//Podemos recuperar la hora, minuto, etc. de la fecha 
					//int minutes=calendarEntrada.get(Calendar.MINUTE);
					//int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes=finEntrada.getMinutes();
		           	
					int hours = finEntrada.getHours();
					if (minutes<10) {
						   minTolerancia="0"+minutes+":00";

						}else {
							 minTolerancia=""+minutes+":00";
						}
										
						
						if (hours<10) {
							horaTolerancia="0"+hours;

						}else {
							horaTolerancia=""+hours;

						}
						
						
						 horaTimbre=""+horaTolerancia+":"+minTolerancia+"";
						
						}

						}//Else si no tiene turno nocturno
						
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					//System.out.println("Error al formatear hora ");
					}
    				
    				

    	       	
    	       		
    	       		
    	       		
    				
    				
    				
    			
    				
    				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
    					//justifico;horaHorarioSalidahoraHorarioSalida
    					valorRetorno= true;
    					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
    					
    					if (estadoPermiso==1) {
    						/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
*/
    				
    					
    						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "E_TTHH","APROBADO");
    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
    	        	    	
    	    	
    					
    					
    					}
    					if (estadoPermiso==2) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
*/
    						
    						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","APROBADO");
    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
    	        	    	
    				
    					
    					
    					}
    					if (estadoPermiso==3) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);}*/
    						tab_tabla.setValor(j, "E_FECHA_SOLICITUD",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "E_TTHH","PENDIENTE");
    	        			tab_tabla.setValor(j, "E_JEFE_INMEDIATO","PENDIENTE");
    	        			tab_tabla.setValor(j, "E_FECHA_DESDE",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "E_FECHA_HASTA",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "E_DETALLE",detalleAspvh);
    	        			tab_tabla.setValor(j, "E_TIPO_PERMISO","HORAS");
    	        			tab_tabla.setValor(j, "E_NUM_HORAS",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "E_HORA_DESDE",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "E_HORA_HASTA",hora_hasta_aspvh);
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
    	        	    	

    					}
    					
    				//	tab_tabla.ejecutarSql();
    				//	utilitario.addUpdate("tab_tabla");

    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					break;
    				}
    				
    				}//Fin de tipo horas
    			
    			
    			
    			
    			}else {
    				//Si no se encuentra en esa fecha no hace nada
    				valorRetorno=false;
    				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
    			} 
    				
    			

    	}//Termina For
    		
    		
    		
    	}// Termina else si tiene permiso en el rango de fechas ingresado
    	return valorRetorno;

    	
    	
    }//Fin de if si cumple


    
    
    
    
    

    
    
    private Boolean getPermisoHorarioSalida(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr,int j,int estadoEntrada){

    	
    	
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene los permisos para justificar
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
    					
    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 20))+"' and anulado_aspvh =false");
    	
    	
    	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
    			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
    	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
    	
    	
    	
    	if (tab_novedad.getTotalFilas()<=0) {
    		//si no tiene permisos ingresados en ese rango de fechas
    		valorRetorno=false;
    		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

    	}else {
    		
    	
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
    	
    		
    		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
    		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

    
    			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
    				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
    				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
    				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
    				//cALFECHAiNICIO ES MENOR QUE  FECHA 
    				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
    			//Encuentro si existe permiso para esa fecha
    					//Obtengo el tipo de permiso si es por dias o por horas
    					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
    					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
    					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
    					String detalleAspvh="";
    							
    					if (tab_novedad.getValor(i,"detalle_aspvh")==null) {
    						detalleAspvh="";
    					}else {
    						detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
    					}
    					
    					String horaDesdeAspvh="";
    					String horaHastaAspvh="";
    					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
    					
    			
    					
    					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
    					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
    					
    				

    			//Obtengo el tipo de permiso si es por dias o por horas

    			//tipo dias justifica todo
    			if (tipoPermiso.equals("4")) {
    				
    				
    				String mensaje="";
    				int banderaAprobacion=0;
    				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
    				
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
    						+ "  "+detalleAspvh;
    						

        			
        			if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
        			{
        				banderaAprobacion=1;
        		
        			/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
        			+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
        				*/
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_SAL","APROBADO");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_SAL","");
            			tab_tabla.setValor(j, "HORA_HASTA_SAL","");
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

        			
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
        				banderaAprobacion=2;
        				
        	/*			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
    */
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_SAL","");
            			tab_tabla.setValor(j, "HORA_HASTA_SAL","");
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

        	
        				
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
        				banderaAprobacion=3;
        				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);
        				}*/
        				
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","PENDIENTE");
            			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_SAL","");
            			tab_tabla.setValor(j, "HORA_HASTA_SAL","");
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

        	
        			}else {
        				
        			}
          				
    				//tab_tabla.ejecutarSql();
    				//utilitario.addUpdate("tab_tabla");
    				
    				
    			}else {
    				//permiso ingresadio por dias
    				
    				//Obtengo los valores 
    				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
    				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
    				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
    				int contieneDiaTurno=0,ide_gtgre=0;
		
    	
    				String mensaje="";
    				int estadoPermiso=0;
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;
    				if(aprobadoTthhAspvh.equals("true")&& aprobadoJefeInmediato.equals("true"))
    				{
    				/*	
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;*/
    						estadoPermiso=1;

    				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=2;

    				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=3;
    				}else {
    					
    				}
    				
    				
    				
    				
    				
    				try {
						String myDateString = ""; //La hora con forma de String
						if (horaTimbre.length()<6) {
							myDateString = horaTimbre+":00";
						}else{
							myDateString = horaTimbre;
						}

						//Creamos la hora con formato del api Java
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						Date date = sdf.parse(myDateString);

						//Podemos asignar la hora a una fecha
						Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
						calendar.setTime(date);   // assigns calendar to given date
						//Podemos recuperar la hora, minuto, etc. de la fecha 
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute=calendar.get(Calendar.MINUTE);
						
						
					
						
						
						String min="",hora="";
						if (minute<10) {
						   min="0"+minute+":00";

						}else {
							 min=""+minute+":00";

						}
						
						
						
						if (hour<10) {
							  hora="0"+hour;

						}else {
							  hora=""+hour;

						}
						
					
						
						
						
						//Obtengo si tiene matriz
	    	       		int mes=0;
	    	       		mes=utilitario.getMes(fecha);
	    	       		int anioTemp=0;
	    	       		anioTemp=utilitario.getAnio(fecha);
	    	       		TablaGenerica tab_Anio=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+anioTemp+"%'");
	    	       		int anio=Integer.parseInt(tab_Anio.getValor("IDE_GEANI"));
	    	       		int dia=0;
	    	       		dia=utilitario.getDia(fecha);
	    	       		
	    	       		//Variables para asignacion Entrada,Salida,Almuerzo Entrada, Almuerzo Salida
	    	       		
	    	       		
	    	       		 contieneDiaTurno=getHorarioXDia(empleado,dia,mes,anio);
	    	       		if (contieneDiaTurno==0) {
	    	       			ide_gtgre=0;
	    	       		}else {
	    	       				//Si tiene asignado matriz para ese dia
	    	       		      TablaGenerica tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
	    	       		  		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
	    	       		}
	    	       		  	    	        
	    	       	
	    	       		

	    				if (ide_gtgre!=0) {
							
	    					
				   String fechaTimbreNocturno="",fechaHoraHasta="",fechaHoraDesde="",fechaHastaSuma="",horaResultado="";
				   fechaTimbreNocturno=fecha+" "+horaTimbre;
	    					

			
				
				   
		           	Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturno),-15);
		           	String minTolerancia="",horaTolerancia="";
		        	
		           	
		           
		           	
		           	
		           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(finEntrada);   // assigns calendar to given date
					calendar.getTime();
					//Podemos recuperar la hora, minuto, etc. de la fecha 
					//int minutes=calendarEntrada.get(Calendar.MINUTE);
					//int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes=finEntrada.getMinutes();
		            	
					int hours = finEntrada.getHours();
					if (minutes<10) {
						   minTolerancia="0"+minutes+":00";

						}else {
							 minTolerancia=""+minutes+":00";
						}
										
						
						if (hours<10) {
							horaTolerancia="0"+hours;

						}else {
							horaTolerancia=""+hours;

						}
						
						horaResultado=horaTolerancia+":"+minTolerancia+"";
						fechaTimbreNocturno=fecha+" "+horaResultado;
	    				
//Aqui le vamos a sumar un dia	    								
							Calendar calFechaHoraBiometrico = Calendar.getInstance();
							calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturno));
							
							
							Calendar calHoraFechaFinConsulta = Calendar.getInstance();

							String fechaTimbreNocturnoHorarioEntrada="",fechaTimbreNocturnoHorarioSalida="",fechaBaseHorarioNocturno="",fechaTerminHorarioNocturno;
							fechaTimbreNocturnoHorarioEntrada=fecha+" "+horaHorarioEntrada;
							Calendar calFechaHoraHorarioBiometricoEntrada = Calendar.getInstance();
							calFechaHoraHorarioBiometricoEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturnoHorarioEntrada));
						
							fechaTimbreNocturnoHorarioSalida=fecha+" "+horaHorarioSalida;
							Calendar calFechaHoraHorarioBiometricoSalida = Calendar.getInstance();
							calFechaHoraHorarioBiometricoSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturnoHorarioSalida));
						
							fechaBaseHorarioNocturno=fecha+" "+"24:00:00";
							Calendar calFechaHoraBase = Calendar.getInstance();
							calFechaHoraBase.setTime(getFechaAsyyyyMMddHHmmss(fechaBaseHorarioNocturno));
						
							fechaHoraDesde=fecha+" "+hora_desde_aspvh;
							Calendar calHoraFechaDesdeTimbreNocturno = Calendar.getInstance();
							calHoraFechaDesdeTimbreNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraDesde));
						
							fechaTerminHorarioNocturno=fecha+" "+horaHorarioSalida;
							Calendar calFechaHoraTermina = Calendar.getInstance();
							calFechaHoraHorarioBiometricoSalida.setTime(getFechaAsyyyyMMddHHmmss(fechaTerminHorarioNocturno));
						
							horaTimbre=""+hora+":"+min+"";
							
							if (estadoEntrada==1 || estadoEntrada==0) {
								
								fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
								fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),1));
					    		calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));


							}else {
								if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioBiometricoEntrada)<=0 && calFechaHoraBiometrico.compareTo(calFechaHoraBase)<=0) {
									fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
									fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),0));
									calHoraFechaFinConsulta = Calendar.getInstance();
									calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));
			
								}else if (calFechaHoraBiometrico.compareTo(calFechaHoraBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioBiometricoSalida)<=0){
									
									fechaHoraHasta=fecha+" "+hora_hasta_aspvh;
									fechaHastaSuma=getFechaAsyyyyMMddHHmmss(utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHasta),1));
						    		calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaHastaSuma));

								} 								
							}

							
							//Comparando las horas del permiso
							   if (calFechaHoraBiometrico.compareTo(calHoraFechaDesdeTimbreNocturno)<=0 && calFechaHoraBiometrico.compareTo(calHoraFechaFinConsulta) <= 0){
	
							
							//Si es aprobado po thh
							if (estadoPermiso==1) {
	 
	    						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "TTHH_SAL","APROBADO");
	    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
	    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
	    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
	    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

	    	        		
	    	    	
	    					
	    					
	    					}//Si es aprobado por jefe inmediato
	    					if (estadoPermiso==2) {
	      						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
	    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
	    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
	    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
	    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

	    					
	    					}
	    					//Si esta ingresado
	    					if (estadoPermiso==3) {
	    						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
	    	        			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
	    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","PENDIENTE");
	    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
	    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
	    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
	    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
	    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
	    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
	    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

	    	        		
	    					}
								    		

							   }
							
							
//////////////////////////////////////////////////////////////////////AQUI CUANDO NO ES NOCTURNO/////////////////////////////////////////////////////////////
	    				}else {
							
						
	    	       		
	    	       		
							
							
							
	    	       			
												
						
						if (hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
							horaTimbre=""+hora+":"+min+"";
							
						}else {
							 horaTimbre=""+hora+":"+min+"";
							//Sumar horas en fecha
							 
		         			    String fechaHoraHorarioFinEntrada = fecha+" "+horaTimbre;

		           	Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada),-15);
		           	String minTolerancia="",horaTolerancia="";
		        	
		           	
		           
		           	
		           	
		           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(finEntrada);   // assigns calendar to given date
					calendar.getTime();
					//Podemos recuperar la hora, minuto, etc. de la fecha 
					//int minutes=calendarEntrada.get(Calendar.MINUTE);
					//int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes=finEntrada.getMinutes();
		           	
					int hours = finEntrada.getHours();
					if (minutes<10) {
						   minTolerancia="0"+minutes+":00";

						}else {
							 minTolerancia=""+minutes+":00";
						}
										
						
						if (hours<10) {
							horaTolerancia="0"+hours;

						}else {
							horaTolerancia=""+hours;

						}
						
						
						 horaTimbre=""+horaTolerancia+":"+minTolerancia+"";
						
						}

						}//Else si no tiene turno nocturno
						
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					//System.out.println("Error al formatear hora ");
					}

    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				
    				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaHorarioSalida)<=0) {
    					//justifico;horaHorarioSalidahoraHorarioSalida
    					if (estadoPermiso==1) {
    						/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
*/
    				
    					
    						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "TTHH_SAL","APROBADO");
    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

    	    	
    					
    					
    					}
    					if (estadoPermiso==2) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
*/
    						
    						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","APROBADO");
    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
    	        	
    	        		
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

    					
    					
    					}
    					if (estadoPermiso==3) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);}*/
    						tab_tabla.setValor(j, "FECHA_SOLICITUD_SAL",fechaSolicitudAspvh);
    	        			tab_tabla.setValor(j, "TTHH_SAL","PENDIENTE");
    	        			tab_tabla.setValor(j, "JEFE_INMEDIATO_SAL","PENDIENTE");
    	        			tab_tabla.setValor(j, "FECHA_DESDE_SAL",fechaInicioAsnov);
    	        			tab_tabla.setValor(j, "FECHA_HASTA_SAL",fechaFinAsnov);
    	        			tab_tabla.setValor(j, "DETALLE_SAL",detalleAspvh);
    	        			tab_tabla.setValor(j, "TIPO_PERMISO_SAL","HORAS");
    	        			tab_tabla.setValor(j, "NUM_HORAS_SAL",nroHorasAspvh);
    	        			tab_tabla.setValor(j, "HORA_DESDE_SAL",hora_desde_aspvh);
    	        			tab_tabla.setValor(j, "HORA_HASTA_SAL",hora_hasta_aspvh);
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   where ide_cobmr="+ide_cobmr);

    	        		
    					}
    										
    					valorRetorno= true;
    					
    					
    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					break;
    				}
    				
    				}//Fin de tipo horas
    			
    			
    			
    			
    			}else {
    				//Si no se encuentra en esa fecha no hace nada
    				valorRetorno=false;
    				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
    			} 
    				
    			

    	}//Termina For
    		
    		
    		
    	}// Termina else si tiene permiso en el rango de fechas ingresado
    	return valorRetorno;

    	
    	
    }//Fin de if si cumple

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private Boolean getPermisoHorarioAlmuerzoEntrada(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr, double tiempoJustificado,int j){
  
    	//System.out.println("Ingresa");
    	
    	
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene los permisos para justificar
    	
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh,aprobado_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_desde_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
        		+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 20))+"' and anulado_aspvh =false ");
    	
    	/*
    	
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
    					
    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 4))+"' and anulado_aspvh =false");
    	*/

    	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
    			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
    	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
    	
    	if (tab_novedad.getTotalFilas()<0) {
    		//si no tiene permisos ingresados en ese rango de fechas
    		valorRetorno=false;
    		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

    	}else {
    		
    	
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
    	
    	
    		
    	    /*+ "'' as FECHA_SOLICITUD, "
			+ "'' as TTHH, "
			+ "'' as JEFE_INMEDIATO, "
			+ "'' as FECHA_DESDE, "
			+ "'' as FECHA_HASTA, "
			+ "'' as DETALLE, "
			+ "'' as TIPO_PERMISO, "
			+ "'' as NUM_HORAS,  "
			+ "'' as HORA_DESDE, "
			+ "'' as HORA_HASTA  "
*/
    		//Obtengo los dias de los permisos consultados hasta tres dias antes
    		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
    		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");
    			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
    				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
    				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
    				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
    				//cALFECHAiNICIO ES MENOR QUE  FECHA 
    				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
    			//Encuentro si existe permiso para esa fecha
    					//Encuentro si existe permiso para esa fecha
    					
    					//Obtengo el tipo de permiso si es por dias o por horas
    					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
    					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
    					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
    					String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
    					String horaDesdeAspvh="";
    					String horaHastaAspvh="";
    					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
    					
    			
    					
    					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
    					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
    					
    			//Obtengo el tipo de permiso si es por dias o por horas

    			//tipo dias justifica todo
    			if (tipoPermiso.equals("4")) {
    				//saco si es de tipo de permiso si es por dias 4  o horas 1
    				String mensaje="";
    				int banderaAprobacion=0;
    				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
    						+ "  "+detalleAspvh;
    			
    		
    	   			
        			if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
        			{
        				banderaAprobacion=1;
        		
        			/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
        			+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
        				*/
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","APROBADO");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
            			
            			
            	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
	    		
        			
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
        				banderaAprobacion=2;
        				
        	/*			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
    */
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
            								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
            	    		
                    			
            		
        	
        				
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
        				banderaAprobacion=3;
        				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);
        				}*/
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
            								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
            	    		
                    			
            	
            		
        	
        			}else {
        				
        			}
        				
    				
    				
    				valorRetorno= true;
    				//System.out.println("permiso ingresado por dias"+valorRetorno);
    				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
    					tab_tabla.ejecutarSql();
    					utilitario.addUpdate("tab_tabla");
    				*/
    			}else {
    				//permiso ingresadio por dias
    				
    				//Obtengo los valores 
    				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
    				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
    				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
    				
    	
    				hora_desde_aspvh =horaHorarioEntrada;
    				hora_hasta_aspvh =horaHorarioSalida;
    				String mensaje="";
    				int estadoPermiso=0;
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;
    				
    				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
    				{
    					
    				/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;*/
    						estadoPermiso=1;

    				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true"))
    					{
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=2;

    				}else 	if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
    				/*	mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=3;
    				}else {
    					
    				}

    				
    				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
    					//justifico;horaHorarioSalidahoraHorarioSalida
    					valorRetorno= true;
    					
    					
    					if (estadoPermiso==1) {
    						/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
*/
    				
    					
    	      				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","APROBADO");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                        	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
    	    		
            			
    	    	
    					
    					
    					}
    					if (estadoPermiso==2) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
*/
    						
    		  				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                	
                        	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
    	    		
            			
    	        		
    				
    					
    					
    					}
    					if (estadoPermiso==3) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);}*/
    						tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                	
                        	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
    	    		
            			
    					}
      					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
    					/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
    					tab_tabla.ejecutarSql();
    					utilitario.addUpdate("tab_tabla");*/
      					valorRetorno= true;
      				  
    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					break;
    				}
    				
    				}//Fin de tipo horas
    			
    			
    			
    			
    			}else {
    				//Si no se encuentra en esa fecha no hace nada
    				valorRetorno=false;
    				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
    			} 
    				
    			

    	}//Termina For
    		
    		
    		
    	}// Termina else si tiene permiso en el rango de fechas ingresado
    	return valorRetorno;

    	
    	
    }//Fin de if si cumple


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private Boolean getPermisoHorarioAlmuerzo(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr,double tiempoJustificado,int j){

    	
    	
    	
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	boolean valorRetorno=false;
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene los permisos para justificar
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_desde_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
    					
    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 20))+"' and anulado_aspvh =false");

    	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
    			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
    	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
    	
    	
    	if (tab_novedad.getTotalFilas()<0) {
    		//si no tiene permisos ingresados en ese rango de fechas
    		valorRetorno=false;
    		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

    	}else {
    		
    	
    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
    	
    		//Obtengo los dias de los permisos consultados hasta tres dias antes
    		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
    		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

    	
    			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
    				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
    				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
    				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
    				//cALFECHAiNICIO ES MENOR QUE  FECHA 
    				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
    			//Encuentro si existe permiso para esa fecha
    					//Obtengo el tipo de permiso si es por dias o por horas
    					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
    					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
    					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
    					String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
    					String horaDesdeAspvh="";
    					String horaHastaAspvh="";
    					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
    					
    			
    					
    					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
    					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"fecha_solicitud_aspvh");
    				
    	
    			//tipo dias justifica todo
    			if (tipoPermiso.equals("4")) {
    				//saco si es de tipo de permiso si es por dias 4  o horas 1
    			
    				String mensaje="";
    				int banderaAprobacion=0;
    				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
    				
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
    						+ "  "+detalleAspvh;
    			
    	   			
    				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
        			{
        				banderaAprobacion=1;
        		
        			/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
        			+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
        				*/
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","APROBADO");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
            	
    				/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='JUSTIFICADO JF.INM',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
	    		
        			*/
            			
                    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
            								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
            	    		
                    			
            			
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
        				banderaAprobacion=2;
        				
        	/*			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
    */
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
            	
            			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
        	
        				
        			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
        				banderaAprobacion=3;
        				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
        				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);
        				}*/
        				
        				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
            			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","PENDIENTE");
            			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
            			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
            			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
            			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","DIAS");
            			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
            			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
            			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
            	
            			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
            		
        	
        			}else {
        				
        			}
        				
    	    					

    				
    				
    				valorRetorno= true;
    				//System.out.println("permiso ingresado por dias"+valorRetorno);
    				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"',horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
    					tab_tabla.ejecutarSql();
    					utilitario.addUpdate("tab_tabla");*/
    				
    			}else {
    				//permiso ingresadio por dias
    				
    				//Obtengo los valores 
    				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
    				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
    				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
    				
    	
    				
    				hora_desde_aspvh =horaHorarioEntrada;
    				hora_hasta_aspvh =horaHorarioSalida;
    				String mensaje="";
    				int estadoPermiso=0;
    		
    				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    						+ "  "+detalleAspvh;	
    				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
    				{
    			
    						estadoPermiso=1;

    				}
    				
    				else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")) {
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=2;

    				}
    				
    				
    				
    				
    				else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")) {
    					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
    							+ "  "+detalleAspvh;*/
    					estadoPermiso=3;

    				}else {
    					
    				}

    		
    				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
    				
    										
    					if (estadoPermiso==1) {
    						/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
*/
    				
    					
    	      				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","APROBADO");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
        	        		
                			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
    					
    					
    					}
    					if (estadoPermiso==2) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);
*/
    						
    		  				tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","APROBADO");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
                		        	
    	        		
    				
    					
    					
    					}
    					if (estadoPermiso==3) {
    					/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
    						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);}*/
    						tab_tabla.setValor(j, "FECHA_SOLICITUD_ALM",fechaSolicitudAspvh);
                			tab_tabla.setValor(j, "TTHH_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "JEFE_INMEDIATO_ALM","PENDIENTE");
                			tab_tabla.setValor(j, "FECHA_DESDE_ALM",fechaInicioAsnov);
                			tab_tabla.setValor(j, "FECHA_HASTA_ALM",fechaFinAsnov);
                			tab_tabla.setValor(j, "DETALLE_ALM",detalleAspvh);
                			tab_tabla.setValor(j, "TIPO_PERMISO_ALM","HORAS");
                			tab_tabla.setValor(j, "NUM_HORAS_ALM",nroHorasAspvh);
                			tab_tabla.setValor(j, "HORA_DESDE_ALM","");
                			tab_tabla.setValor(j, "HORA_HASTA_ALM","");
                	
                			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    								+ "p_alm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
    					}
    					//justifico;horaHorarioSalidahoraHorarioSalida
    				
    					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
    					//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"',horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
    					valorRetorno= true;
    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					break;
    				}
    				
    				}//Fin de tipo horas
    			
    			
    			
    			
    			}else {
    				//Si no se encuentra en esa fecha no hace nada
    				valorRetorno=false;
    				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
    			} 
    				
    			

    	}//Termina For
    		
    		
    		
    	}// Termina else si tiene permiso en el rango de fechas ingresado
    	return valorRetorno;

    	
    	
    }//Fin de if si cumple

    
  //Sumar horas en fecha
	 public Date sumarRestarMinutosFecha(Date fecha, int horas){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fecha); // Configuramos la fecha que se recibe
    calendar.add(Calendar.MINUTE, horas);  // numero de horas a añadir, o restar en caso de horas<0
    return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
		  }
 
    
	 
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
		
		
		
		
		
//ALMUERZO		
	
		    private void getPermisoTiempoAlm1(String fecha,int empleado,double tiempoAlm,double tiempoJustificado,int j,double tiempo){
		boolean entrada=false,salida=false;
		int empleadoTemporal=0,anteriorEmpleado=0,x=0,y=0;
		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		double tiempoTrabajoXDia=0.0,tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();
			empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));

			
			
			
			if (empleado==empleadoTemporal) {
				anteriorEmpleado=1;

				}else {
				anteriorEmpleado=0;
				y++;
				} 
			
			
	    	double uno=tiempo;
	    	double dos=tiempoAlm;
	    	double tres=0.0;
	    	String p="";
			if (tiempo>tiempoAlm) {
				//tres =uno-dos;
			}else {
				tres =dos-uno;
			
				int pt_entera=(int)tres;
				double pt_decimal=(tres-pt_entera)*60;
				
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) {
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				
				double p1=Double.parseDouble(p);
				tres=pt_entera+p1;
				
				
				
				
				
				
				
				tab_tabla.setValor(j, "diferenciaAlm",""+tres);
				
			
				
				if (anteriorEmpleado==1) {
					acumuladoVacaciones=tres;
					acumuladoVacacionesTemp=acumuladoVacacionesTemp+tres;
					
				
				}else {
			//		System.out.println("ACUMULADO"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);

					tab_tabla_sumatoria.setValor(x,"ATRASO_ALMUERZO",""+acumuladoVacacionesTemp);
					//System.out.println("x"+x);
					x++;
					acumuladoVacaciones=horaExtra;
					acumuladoVacacionesTemp=0;
					acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
					empleadoTemporal=empleado;

				//System.out.println("ACUMULADO"+empleado+" --->  "+acumuladoVacacionesTemp+"  ----->  "+tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));

				}
				
	   	    	
			}		
	    	
	    	
					
			
	    }//Fin de if si cumple

		
		    
		    
		  //ALMUERZO		
			
		    private void getPermisoTiempoAlm(String fecha,int empleado,double tiempoAlm,double tiempoJustificado,int j,double tiempo,String justificativo){
	/*	boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

	    	double uno=tiempo;
	    	double dos=tiempoAlm;
	    	double tres=0.0;
	    	String p="";
			if (tiempo>tiempoAlm) {
				//tres =uno-dos;
			}else {
				tres =dos-uno;
			
				int pt_entera=(int)tres;
				double pt_decimal=(tres-pt_entera)*60;
				
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) {
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				
				double p1=Double.parseDouble(p);
				tres=pt_entera+p1;
				
				double valor=0.0;
				if (tab_tabla.getValor(j, "p_alm_cobmr")==null || tab_tabla.getValor(j, "p_alm_cobmr").equals("") || tab_tabla.getValor(j, "p_alm_cobmr").isEmpty()) {
					valor=tres;
				}else {
					valor=0.0;
				}
				
				
				
				if
				
				tab_tabla.setValor(j, "diferenciaAlm",""+valor);*/
		    	
		    	
		    	if (tiempoAlm>3){
		    		tiempoAlm=(tiempoAlm/60);
		    	}
				
				
		    	
		    	tiempo=(tiempo/60);
		boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

	    	double uno=tiempo;
	    	double dos=tiempoAlm;
	    	double tres=0.0;
	    	String p="";
			if (tiempo>tiempoAlm) {
				tres =0.0;
			}else {
				tres =dos-uno;
			
				int pt_entera=(int)tres;
				double temp_=(tres-pt_entera)*60;
				double pt_decimal=Math.round(temp_);
				
				//p_parametros.put("total",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));
			//p_parametros.put("diasdescontados",""+(Math.rint(numeroDiasTomados * 100) / 100));
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) {
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				
				double p1=Double.parseDouble(p);
				
				
				
				
						if(justificativo==null || justificativo.equals("") || justificativo.isEmpty()){
							tres=p1;
							String tres_temp=""+p1;
							String[] parts = tres_temp.split("\\.");
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // -654321
							//entradaTimbre=part1+":"+part2+":00";
							if (pt_entera>0) {
								tab_tabla.setValor(j, "diferenciaAlm",pt_entera+":"+part2);
							}else if(pt_entera==0){
								tab_tabla.setValor(j, "diferenciaAlm","0:"+part2);	
							}
				
							
						//	tres=pt_entera+p1;
						}else if (justificativo.equals("0") ||  justificativo.equals("0.0") || justificativo.equals("0.00")) {
							tres=0;
							tab_tabla.setValor(j, "diferenciaAlm","0:0");	

						} else if (justificativo!=null) {
							tres=0;
							tab_tabla.setValor(j, "diferenciaAlm","0:0");	

						}
				
				
				
				
	   	    	
					
	   	    	
			}		
	    	
	    	
					
			
	    }//Fin de if si cumple

		
		    
		    
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
		    
		 	
		 	
//////////SUMATORIA
		 	
		 	
		 	
		 	
		 	
		 	public String getSumatoriaAtrasos(int parametro,String fechaIni,String fechaFin){
		 		String consulta="";
		 		
		 		

		 		if (parametro==0) {	
consulta="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||    "
		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
		+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,  "
		+ "'' as ATRASO_ENTRADA,  "
		+ "'' as ATRASO_ALMUERZO,  "
		+ "'' as ATRASO_SALIDA " 
		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
		+ "where epar.activo_geedp=true  "
		+ " and emp.ide_gtemp=-1   "
		+ " and inconsistencia_biometrico_cobmr is null " 

		+ "GROUP BY EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP ,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, "
		+ "SUCU.NOM_SUCU,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,ATRASO_ENTRADA, ATRASO_ALMUERZO,ATRASO_SALIDA "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";

}else {
	consulta="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||    "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,  "
			+ "'' as ATRASO_ENTRADA,  "
				+ "'' as ATRASO_ALMUERZO,  "
				+ "'' as ATRASO_SALIDA "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'  "
			+ " and inconsistencia_biometrico_cobmr is null  " 
			+ "GROUP BY EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP ,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,ATRASO_ENTRADA, ATRASO_ALMUERZO,ATRASO_SALIDA "
			+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";
}
		
		

return consulta;
	}

		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		/*
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		
		 		if (parametro==0) {
					
			
		  consulta="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,   "
		 				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		 				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
		 				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		 				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		 				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,   "
		 				+ "DEPA.DETALLE_GEDEP, "
		 				+ "GTT.DETALLE_GTTEM,  "
		 				//+ "'' as ENTRADA_FECHA_SOLICITUD,  "
		 			//	+ "'' as SALIDA_FECHA_SOLICITUD, "
		 				+ "'' as ATRASO_ENTRADA,  "
		 				+ "'' as ATRASO_ALMUERZO,  "
		 				+ "'' as ATRASO_SALIDA "
		 				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
		 				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		 				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		 				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		 				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
		 				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
		 				+ "where epar.activo_geedp=true and EMP.IDE_GTEMP=-1 "
		 				+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP ,  "
		 				+ "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
		 				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,   "
		 				+ "DEPA.DETALLE_GEDEP,  "
		 				+ "GTT.DETALLE_GTTEM,ATRASO_ENTRADA,ATRASO_ALMUERZO,ATRASO_SALIDA "
		 				+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";
		 	
		  
		  }else {
			  consulta="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,   "
		 				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		 				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
		 				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		 				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		 				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,   "
		 				+ "DEPA.DETALLE_GEDEP, "
		 				+ "GTT.DETALLE_GTTEM,  "
		 			//	+ "'' as ENTRADA_FECHA_SOLICITUD,  "
		 			//	+ "'' as SALIDA_FECHA_SOLICITUD, "
		 				+ "'' as ATRASO_ENTRADA,  "
		 				+ "'' as ATRASO_ALMUERZO,  "
		 				+ "'' as ATRASO_SALIDA "
		 				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
		 				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		 				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		 				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		 				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
		 				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
		 				+ "where epar.activo_geedp=true  "
		 				+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP ,  "
		 				+ "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
		 				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,   "
		 				+ "DEPA.DETALLE_GEDEP,  "
		 				+ "GTT.DETALLE_GTTEM,ATRASO_ENTRADA,ATRASO_ALMUERZO,ATRASO_SALIDA "
		 				+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc";
		 	
		}*/
		  






			public Tabla getTab_tabla_sumatoria() {
				return tab_tabla_sumatoria;
			}






			public void setTab_tabla_sumatoria(Tabla tab_tabla_sumatoria) {
				this.tab_tabla_sumatoria = tab_tabla_sumatoria;
			}
		 	
		 	
		 	
		 	
			public void seis(){
				boolean entrada=false,salida=false;

				// ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
				 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


				String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
				//double tiempoTrabajoXDia=0;
				//double tiempoTrabajoXDia=0.0;
				double tiempoTrabajoXDia=0.0;

				
				String entradaBandera="",salidaBandera="";
				
				for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
					int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
					String fecha= tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");

					entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
					entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
					salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
					salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
					fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
					String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
					String banderaSalida=tab_tabla.getValor(i, "horafinband_cobmr");

					
					entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
					salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");
					
					
					
				//	entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
				//	salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");

				//System.out.println("empleado "+empleado+"  salidaBandera"+salidaBandera+" FECHA:"+fecha);
				//System.out.println("empleado "+empleado+"  entradaBandera"+entradaBandera+" FECHA:"+fecha);

					if ((banderaEntrada==null) || (banderaEntrada.isEmpty()) || (banderaEntrada.equals("")) || (banderaEntrada.equals("SIN TIMBRE")) ) {
						
						 if(entradaBandera==null || entradaBandera.equals("") || entradaBandera.isEmpty() ){
								entrada=true;
						 }else {
							 	entrada=false;
							 	entradaTimbre=entradaHorario;
						}
						//si entrada vacia o sin datos
					}else if (banderaEntrada.equals("OK")) {
						entrada=false;
					 
					}
			
					if ((banderaSalida==null) || (banderaSalida.isEmpty()) || (banderaSalida.equals("")) || (banderaSalida.equals("SIN TIMBRE")) ) {
						//Si la salida no se encuentra marcada 
						 if(salidaBandera==null || salidaBandera.equals("") || salidaBandera.isEmpty()){
							 salida=true;
						 }else {
							 salida=false;
							 salidaTimbre=salidaHorario;
						}
					}else if (banderaSalida.equals("OK")) {
						salida=false;
					}
					
				//	else {
				//		salida=false;
				//	}
					
					Date fechaIniAlmBio = null;
			    	Date fechaFinAlmBio = null;
					//Si hay salida y entrada 
					if (entrada==false && salida==false) {
						
						
						//Validamos si el timbre de la entrada es es mayor o menro q el horario planificado
						if (entradaTimbre.compareTo(entradaHorario)<=0) {
						// Si es menor le pongo la misma hora que el horario
							fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
							}else {
						//	Si es mayor escogo la hora de timbrado	
							 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);

							}	
						
						
						String horaTipoTurno="20:00:00";
						boolean tipoTurno=false;
						if (entradaTimbre.compareTo(horaTipoTurno)>=0) {
							tipoTurno=true;
						}else {
							tipoTurno=false;
						}
						
						if (tipoTurno==false) {
							
						
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
				    	 
					    	tiempoTrabajoXDia = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);

				    	//tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
				    	//(Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
				    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
				    //	listaEmpleado.add(empleado);
						tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
						}else {

							
							
					    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaBiometrico),1))+" "+salidaTimbre);
					    //	 tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
						    	tiempoTrabajoXDia = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio));

					    	 //listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
					    //	listaEmpleado.add(empleado);
							tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
						}
				    				}
					else {
						
					}
				}
			}
		 	
		 	
		 	
		 	
		 	
}

