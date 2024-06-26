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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.validation.constraints.Size;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
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
public class pre_biometrico_marcaciones extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_resumen_marcaciones = new Tabla(); 
    
    
    private Tabla tab_novedad = new Tabla();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private boolean banderaFeriados=false,sinIngreso=false;
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

    TablaGenerica tabDiasFeriados;
    
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	String horaAnterior="",horaAnteriorAlm="",horaAnteriorAlm1="";
	int ide_gtgre=0,turno=0;
	
	private boolean estado_empleado_nocturno=false;
	private boolean banderaCambioHorario,banderaCambioHorarioPermisoOnline;
    private Integer ide_geedp;
	private String  pos_marcacionEntrada="",pos_marcacionTarde="",pos_marcacionNocturno="",horaActualizarPegado="";
    private Integer estadoMarcacionBiometrico=0,numMarcacionesBiometrico=0;
    String empleadoSeleccionado="";
    public pre_biometrico_marcaciones() {
    	
    	
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	    	
    	//Boton Reporte de Empleado
    	Boton bot_rep_biometrico= new Boton();
    	bot_rep_biometrico.setIcon("ui-icon-calculator");
    	bot_rep_biometrico.setMetodo("importarMarcaciones");
    	bot_rep_biometrico.setValue("Importar Marcaciones");
    	bot_rep_biometrico.setTitle("Importar Marcaciones");
    	bar_botones.agregarBoton(bot_rep_biometrico);
    	

    	//Boton Reporte de Empleado
    	Boton bot_rep_biometrico_eliminar= new Boton();
    	bot_rep_biometrico_eliminar.setIcon("ui-icon-calculator");
    	bot_rep_biometrico_eliminar.setMetodo("eliminarMarcaciones");
    	bot_rep_biometrico_eliminar.setValue("Eliminar Marcaciones");
    	bot_rep_biometrico_eliminar.setTitle("Eliminar Marcaciones");
    	bar_botones.agregarBoton(bot_rep_biometrico_eliminar);
    	
    	
    	
    	Boton bot_departamento_biometrico= new Boton();
    	bot_departamento_biometrico.setIcon("ui-icon-calculator");
    	bot_departamento_biometrico.setMetodo("getImportacionesPorNovedad");
    	bot_departamento_biometrico.setValue("Ver Importación");
    	bot_departamento_biometrico.setTitle("Ver Reporte de Imòrtaciones");
    	bar_botones.agregarBoton(bot_departamento_biometrico);
    	
    	
    	
    	
    	Boton bot_empleado= new Boton();
    	bot_empleado.setIcon("ui-icon-calculator");
    	bot_empleado.setMetodo("seleccionarEmpleado");
    	bot_empleado.setValue("Importar X Empleado");
    	bot_empleado.setTitle("Importar X Empleado");
    	bar_botones.agregarBoton(bot_empleado);
    	
    	
    	
    	Boton bot_dias_libres_sin_timbre= new Boton();
    	bot_dias_libres_sin_timbre.setIcon("ui-icon-calculator");
    	bot_dias_libres_sin_timbre.setMetodo("marcacionesLibreSinTimbre");
    	bot_dias_libres_sin_timbre.setValue("Importar Marcaciones Libre /Sin Timbre");
    	bot_dias_libres_sin_timbre.setTitle("Importar Marcaciones Libre /Sin Timbre");
    	//bar_botones.agregarBoton(bot_dias_libres_sin_timbre);
    	
    	
    	
    	Boton bot_empleados_sin_marcacion= new Boton();
    	bot_empleados_sin_marcacion.setIcon("ui-icon-calculator");
    	bot_empleados_sin_marcacion.setMetodo("empleadoSinMarcacion");
    	bot_empleados_sin_marcacion.setValue("Importar Empleados Sin Marcaciones");
    	bot_empleados_sin_marcacion.setTitle("Importar Empleados Sin Marcaciones");
    	bar_botones.agregarBoton(bot_empleados_sin_marcacion);
    	
    	
    	
    	
    	
       	//Boton Reporte de Empleado
    	Boton bot_eliminar_sin_timbre= new Boton();
    	bot_eliminar_sin_timbre.setIcon("ui-icon-calculator");
    	bot_eliminar_sin_timbre.setMetodo("eliminarMarcaciones");
    	bot_eliminar_sin_timbre.setValue("Eliminar Marcaciones");
    	bot_eliminar_sin_timbre.setTitle("Eliminar Marcaciones");
    	bar_botones.agregarBoton(bot_eliminar_sin_timbre);
    	
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);    	
    	
    	
    	
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

    	
    	
    	set_departamento.setId("set_departamento");

    	String sql = 
    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
    	"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
    	"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
    	"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
    	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD'), "
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
		+ "from gth_empleado emp  " 
		+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP " 
		+ "where emp.ide_gtemp=-1 "  
		+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') "
		+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC ";
    	
    	
    	tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(sql);
        
      
        tab_tabla.getColumna("IDE_GTEMP").setLongitud(5);
        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
        tab_tabla.getColumna("IDE_GTEMP").setNombreVisual("COD");
        tab_tabla.getColumna("NOMBRES_APELLIDOS").setLongitud(40);
        tab_tabla.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
		tab_tabla.getColumna("IDE_GTEMP").setFiltro(true);

        tab_tabla.getColumna("TO_CHAR").setLongitud(20);
        tab_tabla.getColumna("TO_CHAR").setNombreVisual("FECHA");
		tab_tabla.getColumna("TO_CHAR").setFiltro(true);

        
        tab_tabla.getColumna("HORAINICIOHORARIO").setLongitud(25);
        tab_tabla.getColumna("HORAINICIOHORARIO").alinearCentro();
        tab_tabla.getColumna("HORAINICIOHORARIO").setNombreVisual("ENTRADA");

        tab_tabla.getColumna("HORAINICIOBIOMETRICO").setLongitud(25);
        tab_tabla.getColumna("HORAINICIOBIOMETRICO").alinearCentro();
        tab_tabla.getColumna("HORAINICIOBIOMETRICO").setNombreVisual("TIMBRE");
        
        
        tab_tabla.getColumna("HORAINICIOBAND").setLongitud(25);
        tab_tabla.getColumna("HORAINICIOBAND").alinearCentro();
        tab_tabla.getColumna("HORAINICIOBAND").setNombreVisual("ESTADO ENTRADA");
        
        tab_tabla.getColumna("HORAINICIOALM").setLongitud(25);
        tab_tabla.getColumna("HORAINICIOALM").alinearCentro();
        tab_tabla.getColumna("HORAINICIOALM").setNombreVisual("H.INI.ALM");
 

        tab_tabla.getColumna("HORAFINALM").setLongitud(25);
        tab_tabla.getColumna("HORAFINALM").alinearCentro();
        tab_tabla.getColumna("HORAFINALM").setNombreVisual("H.FIN.ALM");
        
        tab_tabla.getColumna("HORAINICIOALMBIO").setLongitud(25);
        tab_tabla.getColumna("HORAINICIOALMBIO").alinearCentro();
        tab_tabla.getColumna("HORAINICIOALMBIO").setNombreVisual("TIM.INI.ALM");  

        tab_tabla.getColumna("HORAFINALMBIO").setLongitud(25);
        tab_tabla.getColumna("HORAFINALMBIO").alinearCentro();
        tab_tabla.getColumna("HORAFINALMBIO").setNombreVisual("TIM.FIN.ALM");  
        
        tab_tabla.getColumna("TIEMPOALM").setLongitud(25);
        tab_tabla.getColumna("TIEMPOALM").alinearCentro();
        tab_tabla.getColumna("TIEMPOALM").setNombreVisual("TIM.TOMA.ALM"); 
        
        
        tab_tabla.getColumna("TIEMPOHORALM").setLongitud(25);
        tab_tabla.getColumna("TIEMPOHORALM").alinearCentro();
        tab_tabla.getColumna("TIEMPOHORALM").setNombreVisual("TIEMPOHORALM"); 
        
                
        tab_tabla.getColumna("HORAFINHORARIO").setLongitud(25);
        tab_tabla.getColumna("HORAFINHORARIO").alinearCentro();
        tab_tabla.getColumna("HORAFINHORARIO").setNombreVisual("HOR.SAL");  
        
        
        tab_tabla.getColumna("HORAFINBIOMETRICO").setLongitud(25);
        tab_tabla.getColumna("HORAFINBIOMETRICO").alinearCentro();
        tab_tabla.getColumna("HORAFINBIOMETRICO").setNombreVisual("TIM.HOR.SAL");  

        tab_tabla.getColumna("HORAFINBAND").setLongitud(25);
        tab_tabla.getColumna("HORAFINBAND").alinearCentro();
        tab_tabla.getColumna("HORAFINBAND").setNombreVisual("ESTADO SALIDA");  
        
		tab_tabla.setHeader("REPORTE RESUMEN MARCACIONES EN BIOMÉTRICO POR EMPLEADO");
	//	tab_tabla.setCampoPrimaria("CODIGO");
	//	tab_tabla.setNumeroTabla(1);
        tab_tabla.setRows(20);
        
        
        tab_tabla.ejecutarSql();
        tab_tabla.setLectura(true);
        
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        
        

        tab_resumen_marcaciones.setId("tab_resumen_marcaciones");
        tab_resumen_marcaciones.setTabla("con_biometrico_marcaciones_resumen", "IDE_COBMR", 2);
        tab_resumen_marcaciones.setNumeroTabla(2);

		tab_resumen_marcaciones.getColumna("IDE_COBMR").setLongitud(5);
		tab_resumen_marcaciones.getColumna("IDE_COBMR").setOrden(1);
        tab_resumen_marcaciones.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GTH_EMPLEADO EMP");
        tab_resumen_marcaciones.getColumna("ide_gtemp").setAutoCompletar();
        tab_resumen_marcaciones.setCampoPrimaria("ide_cobmr");
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setLongitud(27);
        tab_resumen_marcaciones.getColumna("IDE_GTEMP").alinearCentro();
        tab_resumen_marcaciones.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO");
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setOrden(2);
		tab_resumen_marcaciones.getColumna("IDE_GTEMP").setFiltro(true);


        tab_resumen_marcaciones.getColumna("DIA_COBMR").setLongitud(15);
        tab_resumen_marcaciones.getColumna("DIA_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("DIA_COBMR").setOrden(3);
		
		
		tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setLongitud(20);
		tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setOrden(4);
        tab_resumen_marcaciones.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
        
        
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setOrden(5);
        tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setNombreVisual("ENTRADA");

        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOHORARIO_COBMR").setOrden(6);
        tab_resumen_marcaciones.getColumna("HORAINICIOBIOMETRICO_COBMR").setNombreVisual("TIMBRE");
        
        
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setOrden(7);
        tab_resumen_marcaciones.getColumna("HORAINICIOBAND_COBMR").setNombreVisual("ESTADO ENTRADA");
        
        tab_resumen_marcaciones.getColumna("biometrico_entrada_cobmr").setLongitud(20);
        tab_resumen_marcaciones.getColumna("biometrico_entrada_cobmr").alinearCentro();
    	tab_resumen_marcaciones.getColumna("biometrico_entrada_cobmr").setOrden(8);
        tab_resumen_marcaciones.getColumna("biometrico_entrada_cobmr").setNombreVisual("BIOMETRICO ENTRADA"); 
      	        
        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setOrden(9);
        tab_resumen_marcaciones.getColumna("HORAINICIOALM_COBMR").setNombreVisual("H.INI.ALM");
 
             

        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setOrden(10);
        tab_resumen_marcaciones.getColumna("HORAFINALM_COBMR").setNombreVisual("H.FIN.ALM");
        
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setOrden(11);
        tab_resumen_marcaciones.getColumna("HORAINICIOALMBIO_COBMR").setNombreVisual("TIM.INI.ALM");  

        
        
        
        
        
        
        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setOrden(12);
        tab_resumen_marcaciones.getColumna("HORAFINALMBIO_COBMR").setNombreVisual("TIM.FIN.ALM");  
        
        tab_resumen_marcaciones.getColumna("biometrico_alm_salida").setLongitud(20);
        tab_resumen_marcaciones.getColumna("biometrico_alm_salida").alinearCentro();
    	tab_resumen_marcaciones.getColumna("biometrico_alm_salida").setOrden(13);
        tab_resumen_marcaciones.getColumna("biometrico_alm_salida").setNombreVisual("BIOMETRICO SAL.ALM"); 


        tab_resumen_marcaciones.getColumna("biometrico_alm_entrada").setLongitud(20);
        tab_resumen_marcaciones.getColumna("biometrico_alm_entrada").alinearCentro();
    	tab_resumen_marcaciones.getColumna("biometrico_alm_entrada").setOrden(14);
        tab_resumen_marcaciones.getColumna("biometrico_alm_entrada").setNombreVisual("BIOMETRICO ENT.ALM"); 
        
        
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setLongitud(10);
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").alinearCentro();
		tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setOrden(15);
        tab_resumen_marcaciones.getColumna("TIEMPOALM_COBMR").setNombreVisual("TIM.TOMA.ALM"); 
        
        
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setLongitud(10);
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setOrden(16);
        tab_resumen_marcaciones.getColumna("TIEMPOHORALM_COBMR").setNombreVisual("TIEMPOHORALM"); 

        
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setOrden(17);
        tab_resumen_marcaciones.getColumna("HORAFINHORARIO_COBMR").setNombreVisual("HOR.SAL");  
        
        
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setOrden(18);
        tab_resumen_marcaciones.getColumna("HORAFINBIOMETRICO_COBMR").setNombreVisual("TIM.HOR.SAL");  

        
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setLongitud(20);
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").alinearCentro();
    	tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setOrden(19);
        tab_resumen_marcaciones.getColumna("HORAFINBAND_COBMR").setNombreVisual("ESTADO SALIDA");  
    
                
        tab_resumen_marcaciones.getColumna("biometrico_salida_cobmr").setLongitud(20);
        tab_resumen_marcaciones.getColumna("biometrico_salida_cobmr").alinearCentro();
    	tab_resumen_marcaciones.getColumna("biometrico_salida_cobmr").setOrden(20);
        tab_resumen_marcaciones.getColumna("biometrico_salida_cobmr").setNombreVisual("BIOMETRICO SALIDA"); 

        
        tab_resumen_marcaciones.getColumna("horafinextra_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("recargonocturno25_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("recargonocturno100_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("novedad_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("aprueba_hora_extra_cobmr").setVisible(false);  

        tab_resumen_marcaciones.setCondicion("ide_cobmr=-1");  
		tab_resumen_marcaciones.setCampoOrden("IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");

        tab_resumen_marcaciones.setHeader("RESUMEN DE MARCACIONES EN BIOMÉTRICO POR EMPLEADO");
        tab_resumen_marcaciones.setLectura(true);
        tab_resumen_marcaciones.setRows(15);
        tab_resumen_marcaciones.dibujar();
    	PanelTabla pat_panel2 = new PanelTabla();
    	pat_panel2.setPanelTabla(tab_resumen_marcaciones);
        //TABLA DONDE SE GUARDA EL RESUMEN  DE LOS DATOS       


	tab_novedad.setId("tab_novedad");
	tab_novedad.setTabla("ASI_NOVEDAD", "IDE_ASNOV", 1);
	tab_novedad.getColumna("IDE_ASNOV").setNombreVisual("CÓDIGO");
	tab_novedad.getColumna("IDE_ASNOV").setOrden(1);
	tab_novedad.getColumna("IDE_ASNOV").alinearCentro();
	tab_novedad.getColumna("IDE_ASNOV").setLectura(false);
	tab_novedad.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
	tab_novedad.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
	tab_novedad.getColumna("IDE_USUA").setAutoCompletar();
	tab_novedad.getColumna("IDE_USUA").setLectura(true);
	tab_novedad.getColumna("IDE_USUA").setOrden(2);
	tab_novedad.getColumna("IDE_USUA").alinearCentro();
	tab_novedad.getColumna("IDE_USUA").setNombreVisual("USUARIO");


	tab_novedad.getColumna("FECHA_ASNOV").setValorDefecto(utilitario.getFechaActual());
	tab_novedad.getColumna("FECHA_ASNOV").setNombreVisual("FECHA_NOVEDAD");
	tab_novedad.getColumna("FECHA_ASNOV").setLectura(true);
	tab_novedad.getColumna("FECHA_ASNOV").setOrden(3);
	tab_novedad.getColumna("FECHA_ASNOV").alinearCentro();

	tab_novedad.getColumna("FECHA_INICIO_ASNOV").setLectura(true);
	tab_novedad.getColumna("FECHA_INICIO_ASNOV").setOrden(4);
	tab_novedad.getColumna("FECHA_INICIO_ASNOV").setNombreVisual("FECHA INICIO IMPORTA");

	tab_novedad.getColumna("FECHA_FIN_ASNOV").setLectura(true);
	tab_novedad.getColumna("FECHA_FIN_ASNOV").setOrden(5);
	tab_novedad.getColumna("FECHA_FIN_ASNOV").setNombreVisual("FECHA FIN IMPORTA");
	
	tab_novedad.getColumna("OBSERVACION_ASNOV").setOrden(6);
	tab_novedad.getColumna("OBSERVACION_ASNOV").setNombreVisual("OBSERVACIÓN");
	tab_novedad.getColumna("OBSERVACION_ASNOV").setLectura(true);

	tab_novedad.getColumna("ACTIVO_ASNOV").setCheck();
	tab_novedad.getColumna("ACTIVO_ASNOV").setNombreVisual("ACTIVO");
	tab_novedad.getColumna("ACTIVO_ASNOV").setOrden(7);
	tab_novedad.getColumna("ACTIVO_ASNOV").setLectura(true);
	

	tab_novedad.getColumna("IDE_ASTUR").setVisible(false);
	
	
	
	

	tab_novedad.setHeader("REGISTRO DE NOVEDADES MARCACIONES BIOMÉTRICO");
	tab_novedad.getColumna("dia_feriado_asnov").setVisible(false);

	tab_novedad.setCondicion("importacion_asnov=true");
	tab_novedad.setRows(5);
	tab_novedad.dibujar();
	PanelTabla pat_panel1 = new PanelTabla();
	pat_panel1.setPanelTabla(tab_novedad);
   
   

   Division div_division=new Division();
   div_division.setId("div_division");
   div_division.dividir2(pat_panel1, pat_panel2, "35%", "H");
   
   agregarComponente(div_division);

        
    }

   
   public void getImportacionesPorNovedad (){
	   tab_novedad.getValorSeleccionado();
	   String fechaIni=tab_novedad.getValor("FECHA_INICIO_ASNOV");
	   String fechaFin=tab_novedad.getValor("FECHA_FIN_ASNOV");
    	
		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ");
		tab_resumen_marcaciones.setCampoOrden("IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
		tab_resumen_marcaciones.ejecutarSql();
		utilitario.addUpdate("tab_novedad,tab_resumen_marcaciones");
	   
    }
   
    
    
    
    public void importarMarcaciones(){
    	
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

		}
    	//si no existen
    	else {
			
    	//inserto	
    		int ide_usua=Integer.parseInt(utilitario.getVariable("IDE_USUA").toString());
    		String fecha_inicio_asnov= fechaIni;
    		String fecha_fin_asnov= fechaFin;
        	String 	observacion_asnov="Marcaciones importadas desde "+fecha_inicio_asnov+" hasta "+fecha_fin_asnov+"";
        	String fecha_asnov=utilitario.getFechaActual();
        	int mes=utilitario.getMes(fecha_inicio_asnov);
        	int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
        	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
		    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
		    
   			getMarcacionesEmpleado(fechaIni,fechaFin,mes,anio,"",true);
   			getMarcacionesAlmuerzo(fechaIni,fechaFin);
   			
   			
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

				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
						+ "horainicioband_cobmr ='SIN TIMBRE'  where (horainicioband_cobmr ='' OR horainicioband_cobmr is null)  and horainiciohorario_cobmr is not null  "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' ");

				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "horafinband_cobmr ='SIN TIMBRE'  where (horafinband_cobmr ='' OR horafinband_cobmr is null)  and horafinhorario_cobmr is not null  "
						+ " and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' ");


				
				getSinMarcacionesEmpleado(fechaIni, fechaFin, 9, 11, empleadoSeleccionado, true);
				getDiasLibresEmpleado(fechaIni, fechaFin, 9, 11);
			    	marcacionesEmpleado(fechaIni, fechaFin, 9, 11);
				
						
				
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
   		//String fechaIni=(cal_fecha_inicial.getFecha());	
		//String fechaFin=(cal_fecha_final.getFecha());	

    	 //getMarcacionesAlmuerzo(fechaIni,fechaFin);
			
        	
       }
    
	public void getEmpleado(){
		
	  	
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
				+ "where ide_gtemp in ("+set_empleado.getSeleccionados()+") "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");


        getMarcacionesEmpleado(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),0,0,set_empleado.getSeleccionados(),false);

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
         
          
        //  getMarcacionesEmpleado(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),0,0,"");
          set_departamento.cerrar();		
 			
	}
    
    
    
    
    @SuppressWarnings("unused")
	public void getMarcacionesEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani,String  empleadoSeleccionado,boolean actualizarEmpleado){
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
    	
    /**
     * Metodo de asignacion de dias	
     */
    	
    	//Asigno el tamaño de la  tabla en la cual voy ir guardando los datos de timbre de cada empleado
    	
//		Tabla que contiene las fechas de timbre del empleado	
    	TablaGenerica tab_reporte=getTablaReporte(empleadoSeleccionado,strFechaIniReporte, strFechaFinReporte);

    	if (tab_reporte.isEmpty()) {
			utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
			return;
		}
    	
    	
    	///rESULTADO CORRECTO
    /*	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
			System.out.println("lista Resultado"+listaDiaSeleccionado.get(i).toString()+"  mes: "+listaMesSeleccionado.get(i).toString());
			
		}*/
    	
    /////////////////////////////////////////////////////////////////////////////////////////////
    	String horaInicioEmpl ="";
			String horaFinEmpl = "";
			String horaIniAlmEmpl ="";
			String horaFinAlmEmpl = "";
	    	String biometrico_entrada_cobmr= "";
	    	String biometrico_alm_entrada="";
	    	String biometrico_alm_salida= "";
	    	String biometrico_salida_cobmr ="";	
		
    	
    	for (int itemReporte=0; itemReporte< tab_reporte.getTotalFilas(); itemReporte++) {
       		Integer Entrada=0;
       		Integer Salida=0;
       		Integer AlmuerzoEntrada=0;
       		Integer AlmuerzoSalida=0;
       	 //Obtengo el ide del empleado con el cual obtendrenmos los horarios
               boolean banderaSinAccion=false;
               Integer extraMatriz=0;
          		Integer empleado = Integer.parseInt(tab_reporte.getValor(itemReporte, "IDE_GTEMP"));
       	//	System.out.println("Empleado"+empleado);
       		//Integer empleado = 46;
       		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(itemReporte,"TO_CHAR");
       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
   			//Tabla guarda los busca y devuelve el turno de tipo matriz si se le ha asignado 
       		TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR=1");
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
       		 //variable devuelve 0: si tiene turno y diferente de 0 si contine turno de la fecha de biometrico 
       		 contieneDiaTurno=getHorarioXDia(empleado,dia,mes,anio);
       		if (contieneDiaTurno==19) {
				contieneDiaTurno=0;
			}
       		//Variable que guarda 0 si no hubo aignacion de turno el dia anterior y diferente de cero los que si tiene turno de tipo nocturno el  dia anterior 
                int contieneTunnoNocturnoAnterior=0;
                //obtengo la un dia anterior a la fecha del biometrico
                int dia_anterior=utilitario.getDia(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado), -1)));
                //devuelve el turno asignado ese dia 
                contieneTunnoNocturnoAnterior=getHorarioXDia(empleado,dia_anterior,mes,anio);
                if (contieneTunnoNocturnoAnterior==19) {
                	contieneTunnoNocturnoAnterior=0;
    			}
   		     //////////////////////////////////////FESTIVOS Y DESIGNACION////////////////////////////////////////////////////////

                //Verifico para ese dia si tiene asignada el dia festivo 
       		     banderaFeriados=getFeriado(fechaBiometricoAgrupadaXEmpleado);
   			     
       		     
       		     
       		  //Si tiene matriz
    				if (tabEmpleadoMatrizMensual.getTotalFilas()!=0   && tabEmpleado.getTotalFilas()==0 ) {
  					TablaGenerica tabTipoTurno=null;			
  					//Si el empleado contiene ese dia turno nocturno de la fecha biometrico
if (contieneDiaTurno==0) {
	ide_gtgre=0;
}else {
  						//Obtengo el Tabla con el turno asignado 
      tabTipoTurno =utilitario.consultar("select ide_astur,ide_gtgre from asi_turnos where ide_astur="+contieneDiaTurno);
  						//tipo de turno = ide_gtgre
  		ide_gtgre=Integer.parseInt(tabTipoTurno.getValor("IDE_GTGRE").toString());
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
    	        				List<Fila> biometricoMarcacionesUnDia;

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
  	   	     	        	
  	   	     	        	
  	   	     	        	

    	 	      	          boolean esTipoNocturno=false,esTipoDiurno=false;
    	 	      		      if (contieneTunnoNocturnoAnterior!=0 && banderaFeriados==false) {
    	 	      	          biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
        	 	      	        String horaBaseNocturno="19:00:00";
        	 	      	        String horaBase="00:00:00";
        	 	      	        String horaMedia="12:00:00";
        	 	      	        String horaFinal="24:00:00";		
        	 	      	        String timbreSalidaDiaAnterior="";
        	 	      	     //Obtengo el dia anterior para comparar con la fecha actual	
		   	     				String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(dateFechaInicioReporteAgrupadaXEmpleado, -1));
				            
		   	     				
		   	     					//obtener el dia anterior  entonces consulto de la tabala resumen la salida del empleado y si es igual no le ingreso 
		   	     					TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
		   	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' "
		   	     									//+ " novedad_cobmr=true "
		   	     									+ "order by ide_cobmr desc");
		   	     				
		   	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
	

		   	     					if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") ||  tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty())
		   	     					{
		   	     					timbreSalidaDiaAnterior="00:00:00";
		   	     					}else {
		   	     					if (estado_empleado_nocturno ==false) {
				   	     				
		   	     						//if (estado_empleado_nocturno != 3) {
				   	     					timbreSalidaDiaAnterior="00:00:00";
											
										}else {
		   	     					timbreSalidaDiaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");			
									}
		   	    					}
		   	     					}else {
			   	     					timbreSalidaDiaAnterior="00:00:00";

		   	     					
									}
		   	     					//debo cambiar
		   	     					for (Fila bioTempMarcacionesUnDia : biometrico) {
    								//hora de la tabla biometrico
    	  	         				String fechaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[1];
    	  	         				String horaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[2];
    	 			   	        	String ubicacion_biometrico = (String)bioTempMarcacionesUnDia.getCampos()[4];
    	  	         				
    	  	         			//busca si contiene marcacion en la noche
    	  	         				//Date nuevoDia=null;
    	  	         				//String nuevoDiaTemp=fechaBiometricoTemp+" "+horaBiometricoTemp;
    	  	         				//nuevoDia=sumarRestarMinutosFecha(getFechaAsyyyyMMdd(nuevoDiaTemp), 5);
    	  	         			    // utilitario.getHora(getFechaAsyyyyMMddHHmmss(nuevoDia));
    	  	         			     //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	  	         			     //String fechaComoCadena = sdf.format(nuevoDia);
    	  	         			     //timbreSalidaDiaAnterior=fechaComoCadena;
    	  	         				
    	  	         				if (esTipoDiurno==false) {
    	  	         				if (horaBiometricoTemp.compareTo(timbreSalidaDiaAnterior)>=0 && horaBiometricoTemp.compareTo(horaBaseNocturno)<=0) {
   	  	         					esTipoDiurno=true;
    	  	         				}else {
									esTipoDiurno=false;
    	  	         					}
    	  	         				}
    	  	         				if (esTipoDiurno==true) {
   	  	         					if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0 && horaBiometricoTemp.compareTo(horaFinal)<=0) {
	    	  	         				esTipoNocturno=true;
        	  	         				}else {
    									esTipoNocturno=false;
        	  	         					}
										
									}
    	  	         				
		   	     					}
		   	     					
		   	     					
		   	     				for (Fila bioTempMarcacionesUnDia : biometrico) {
    								//hora de la tabla biometrico
    	  	         				String horaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[2];
    	  	         			//busca si contiene marcacion en la noche
   	  	         				if (esTipoDiurno==false && estadoNocturno==false){
   	  	         				if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0) {
   		  	         				esTipoNocturno=true;
   								}else {
   									esTipoNocturno=false;
   								}
   	          	 	    						
    									}else {
											
										}
        	  	         				
		   	     					}//For si entro a validacion de horario diurno pero no nocturno
    
    	  	         				
		   	     					//}//If si hay marcaciones el dia anterior caso contrario 
		   	     					//else {
										
								//	}
    	  	         				
    	  	         				
    	  	         				/*
    	  	         				if (horaBiometricoTemp.compareTo(timbreSalidaDiaAnteriro)>=0 && horaBiometricoTemp.compareTo(horaBaseNocturno)<=0) {
    	    	  	         					

  	 	      	          
  	 	      	          
    	  	         					esTipoNocturno=true;
    	  	         				}else {
									esTipoNocturno=false;
    	  	         					}
              	 	      	   	
              	 	      	   	}//for biometrico
		   	     					
		   	     					
		   	     					
		   	     					}
        	 	      	        
              	 	      	   	for (Fila bioTempMarcacionesUnDia : biometrico) {
    								//hora de la tabla biometrico
    	  	         				String horaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[2];
    	  	         			//busca si contiene marcacion en la noche
    	  	         				if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0) {
    	  	         				esTipoNocturno=true;
								}else {
									esTipoNocturno=false;
								}
              	 	      	   	
              	 	      	   	}   */   
        	 	      	        //si contiene
              	 	      	if (esTipoDiurno==false && esTipoNocturno==true) {
              	 	   
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
   			    			String horaFechaFinConsulta= diaInicio+" "+"22:00:00";    			
   			    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-4));
   			    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
   			    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
   			    			//

   			    			//suma una hora a la fecha final 
   			    			String horaFechaInicioConsulta= diaSumaInicio+" "+"06:00:00";    			
 			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
   			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
   			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));	
              	 	      	biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			
							           	 	      	   	
              	 	      	   	}else {
              	 	      	   		biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
              	 	      	   		   }
              	 	      	   	
   	 						}// Si no contiene turno el dia anterior 
    	 	      		      else {
   	 					  /*  String horaBaseNocturno="19:00:00";  
  	 	      	            biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
          	 	      	   	for (Fila bioTemp : biometrico) {
								//hora de la tabla biometrico
	  	         				String horaBiometricoTemp = (String)bioTemp.getCampos()[2];
	  	         			    //busca si contiene marcacion en la noche
	  	         				if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0) {
	  	         				esTipoNocturno=true;
							}else {
								esTipoNocturno=false;
							}
          	 	      	   	
          	 	      	   	} */
    	 	      		    	  
    	 	      		    	  
    	 	      		   	biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
    	 	      	        String horaBaseNocturno="19:00:00";
    	 	      	        String horaBase="00:00:00";
    	 	      	        String horaMedia="12:00:00";
    	 	      	        String horaFinal="24:00:00";		
    	 	      	        String timbreSalidaDiaAnterior="";
    	 	      	     //Obtengo el dia anterior para comparar con la fecha actual	
	   	     				String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(dateFechaInicioReporteAgrupadaXEmpleado, -1));
			            
	   	 				TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
	     							+ "where ide_gtemp="+empleado+" and fecha_evento_cobmr = '"+diaAnterior+"' "
	     									//+ " novedad_cobmr=true "
	     									+ "order by ide_cobmr desc");
	     				
	     					if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
	     						

	     					if(tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") ||  tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty())
	     					{
	     					timbreSalidaDiaAnterior="00:00:00";
	     					}else {
	     					if (estado_empleado_nocturno ==false) {
	   	     				
	     						//if (estado_empleado_nocturno != 3) {
	   	     					timbreSalidaDiaAnterior="00:00:00";
								
							}else {
			 					timbreSalidaDiaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");										
							}
	    					}
	     					}else {
   	     					timbreSalidaDiaAnterior="00:00:00";

	     					
						}
	     					
	   	     				
	   	     				
   	     					//debo cambiar
	   	     					for (Fila bioTempMarcacionesUnDia : biometrico) {
								//hora de la tabla biometrico
  	         				
   	     					String fechaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[1];
	  	         				String horaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[2];
	  	         				biometrico_entrada_cobmr = (String)bioTempMarcacionesUnDia.getCampos()[3];
 			   	        	String ubicacion_biometrico = (String)bioTempMarcacionesUnDia.getCampos()[4];
	     					//Date nuevoDia=null;
  	         			//	String nuevoDiaTemp=fechaBiometricoTemp+" "+horaBiometricoTemp;
  	         			//	nuevoDia=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(nuevoDiaTemp), 5);
  	         			 //    utilitario.getHora(getFechaAsyyyyMMddHHmmss(nuevoDia));
  	         			 //    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
  	         			  //   String fechaComoCadena = sdf.format(nuevoDia);
  	         			  //   timbreSalidaDiaAnterior= fechaComoCadena;
  	         					
   	     						
	  	         			//busca si contiene marcacion en la noche
	  	         				
	  	         				if (esTipoDiurno==false) {
  	         				if (horaBiometricoTemp.compareTo(timbreSalidaDiaAnterior)>=0 && horaBiometricoTemp.compareTo(horaBaseNocturno)<=0) {
	  	         					esTipoDiurno=true;
	  	         				}else {
								esTipoDiurno=false;
	  	         					}
	  	         				}
	  	         				if (esTipoDiurno==true) {
	  	         					if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0 && horaBiometricoTemp.compareTo(horaFinal)<=0) {
    	  	         				esTipoNocturno=true;
    	  	         				}else {
									esTipoNocturno=false;
    	  	         					}
									
								}
	  	         				
	   	     					}
	   	     					
	   	     					
	   	     					
	   	     				for (Fila bioTempMarcacionesUnDia : biometrico) {
								//hora de la tabla biometrico
	  	         				String horaBiometricoTemp = (String)bioTempMarcacionesUnDia.getCampos()[2];
	  	         			//busca si contiene marcacion en la noche
	  	         				if (esTipoDiurno==false && estadoNocturno==false){
	  	         				if (horaBiometricoTemp.compareTo(horaBaseNocturno)>=0) {
		  	         				esTipoNocturno=true;
								}else {
									esTipoNocturno=false;
								}
	          	 	    						
									}else {
										
									}
    	  	         				
	   	     					}//For si entro a validacion de horario diurno pero no nocturno

    	 	      		    	  
    	 	      	        //si contiene
          	 	      	   	if (esTipoDiurno == false && esTipoNocturno==true) {
          	 	      	   		
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
			    			String horaFechaFinConsulta= diaInicio+" "+"22:00:00";    			
			    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-4));
			    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
			    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
			    			//

			    			//suma una hora a la fecha final 
			    			String horaFechaInicioConsulta= diaSumaInicio+" "+"06:00:00";    			
			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));	
          	 	      	    biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  		
		  	 	      	   	}else {
            	 	      	   		biometrico = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
            	 	      	   		   }
    	 	      		      
    	 	      		      }
  	 	      	          
  	 	      	          
  	 	      	          
  	 	      	          
    						}else {
  	        			//ELSE SI NO CONTIENE TURNO NOCTURNO
    														
    														
    														
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
    				    			String horaFechaFinConsulta= diaInicio+" "+"22:00:00";    			
    				    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-4));
    			    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
    			    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
    			    			//

    			    			//suma una hora a la fecha final 
    				    			String horaFechaInicioConsulta= diaSumaInicio+" "+"06:00:00";    			
  			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
    			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
    			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
    			    			biometrico = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			
  						
  			    			
  						
  						
  						
  						}
  	   	     	        
  	   	     	        
  	   	     	        
  	   	     	        
  	   	     	        
  	   	     	        
  	   	     	        
    	        

    	      	         //Si tiene timbradas en el biometrico 
    	      	           if (biometrico.size()!=0) {
    			
    	      	           
    	      	     	boolean bandEntradaSinAlmuerzo=false;
             			
    	         			int banderaAlmuerzo=0;
    	         			int banderaAlmuerzoEntrada=0;
    	         			int banderaEntrada=0;
    	         			String ubicacion_biometrico;
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
  			   	     		   ubicacion_biometrico = (String)bio.getCampos()[4];

    	         				
    	         				
    	         				
    	         				Calendar calFechaHoraBiometrico = Calendar.getInstance();
    	         			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
    	         				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
    	         				
    	         			
    	         			
    	         				
    	     			
    	         				boolean t=false;
    	         				//si contiene un turno asignado a ese dia  asignado en la matriz
    	         				if (contieneDiaTurno!=0 ) {
    			        				// TURNO SI NO ES DE TIPO NOCTURNO
    	         					if ((ide_gtgre!=3 && banderaFeriados==false)) {
    	         					
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
    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
    	                     			
    	                 	                 			    
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
    	                      			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

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
    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);

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
    	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);

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
  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

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
  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);

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
  	  		       			    			biometrico_entrada_cobmr= ubicacion_biometrico;
  	  		       			    			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
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
  		       			    				biometrico_entrada_cobmr= ubicacion_biometrico;
	  		       			    			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
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
 	   		       			 
 	   	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_alm_salida", ubicacion_biometrico);

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
 	   		           			       	tab_reporte.setValor(itemReporte, "biometrico_alm_salida", ubicacion_biometrico);
 	   	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_alm_entrada", ubicacion_biometrico);

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
 	 	   		           			       	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
 	   	                			    	
 	   	                			    	

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
	 	   		           			       	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
	
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

  	  	            			  		
/*  	  	            			  	 	tab_reporte.setValor(itemReporte, "HORAINICIOHORARIO", horaInicioEmpl);
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
  										
  	  	            			  		
  */	  	            			  		
  	  	            			  		
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
	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
    	
    	                    			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
        	              			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN TIMBRE");
    	                    			    	tipo1=1;
    	                    			    	tipoEntrada1=1;
    	                    			    	tipoEntrada2=0;
    	                    			    	tipoNocturno=1;
  	                  			    	banderaHoraExtra1001=1;
    	                     
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
                    			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", "");
	              			    		tab_reporte.setValor(itemReporte, "HORAFINBAND", "SIN TIMBRE");
	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
  	  	            			  		
                    			    	tipo1=1;
                        			    tipoEntrada1=1;
                    			    	tipoEntrada2=0;
                    			    	tipoNocturno=1;
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
      	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

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
	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

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
      	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
 	   	   	            				    
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
  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);

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
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

  									    		banderaHoraExtra1001=1;
  									   	    }else {
  											        			    
  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
  						 			    	
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
    						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
      	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

    									    		banderaHoraExtra1001=1;
    									   	    }else {
    											        			    
    									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
    						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
    						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
    						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
      	    	                     			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
    						 			    	
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
                   			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

					    		banderaHoraExtra1001=1;
					   	    }else {
					        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
                   			tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);

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
		  						 			    if (horaBiometrico.equals(horaSalidaAnterior)) {
		  						 			    	banderaSinAccion=true;

											}else {
		  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		  						                	 if (banderaHoraExtra1001==0) {
		  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
		  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
		  	    	                     			tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);

		  									    		banderaHoraExtra1001=1;
		  									   	    }else {
		  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
		  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
		  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
		  						 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
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
						int registroExtra=0;
						
						//Sumo un dia  a la fecha de entrada							
						Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
						sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
						sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
									    			
						
						String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
						String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
									    			
						//suma una hora a la fecha inicial  22:00:00
		    			String horaFechaFinConsulta= diaInicio+" 19:00:00";    			
		    			String fechaInicioReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaFinConsulta),-3));
		    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
		    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
		    			//

		    			//suma una hora a la fecha final 
		    			String horaFechaInicioConsulta= diaSumaInicio+" 08:00:00";    			
		    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),6));
		    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
		    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaInicioReporte));
						
						for (Fila bioTemp : biometricoTemporalExtra) {
						//hora de la tabla biometrico
	         				horaBiometricoTempExtra = (String)bioTemp.getCampos()[2];
	         				if (horaBiometricoTempExtra.equals(horaSalidaAnterior)) {
   	     			   		pos_Extra=(int)bioTemp.getIndice()+1;
   	     			   		estadoEntradaNocturno=true;
	         				}
	         				else {
 	         				if (horaBiometricoTempExtra.compareTo("19:00:00")>0 && horaBiometricoTempExtra.compareTo("24:00:00")<=0) {
 	         					biometricoTemporalExtra = obtenerTimbreBiometricoNocturno(empleado, fechaInicioReporte, "" ,fechaFinReporte,"");  			

								}else {
				      				biometricoTemporalExtra = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

								}
	         				}
	         				
						}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  	         			    
int valor=0;
						if (estadoEntradaNocturno==true) {
						valor=biometrico.size()-pos_Extra;
							}
						
						if (valor>=1 && estadoEntradaNocturno==true) {
							
							
							
							pos_Extra=1;
							for (Fila bioTemp : biometricoTemporalExtra) {
		         				horaBiometricoTempExtra = (String)bioTemp.getCampos()[2];

						
 	         			   if (!horaBiometricoTempExtra.equals(horaSalidaAnterior)){
 	   	         			   if (registroExtra==0) {

 		         			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
 		         			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
 		         			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometricoTempExtra);
 		 	         			    tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA"); 
 		 	         			    tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
 		 	         			 registroExtra=1;
 		     			   	    }else {
 									        			    
 		        			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
 		         			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
 		         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometricoTempExtra);
 		         			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
 		         			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
 		         			    	registroExtra=2;
 			     				
 		     				
 								        				
 		         				}

 	         			    
 	         				}
 	         			    
	  	         			    }
							
							//Registro de horas extra
							
							if (registroExtra==1) {
insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
"", "","", "",0 ,"0","","", "",false,diaSemana(fechaBiometricoAgrupadaXEmpleado),tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr"),tab_reporte.getValor(itemReporte, "biometrico_alm_entrada"),tab_reporte.getValor(itemReporte, "biometrico_alm_salida"),tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
																		
							}else  if (registroExtra>1){
insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
"", "","", "",0 ,"0","",tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado),tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr"),tab_reporte.getValor(itemReporte, "biometrico_alm_entrada"),tab_reporte.getValor(itemReporte, "biometrico_alm_salida"),tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
										
								
							}else {
								
							}
		
							banderaHoraExtra1001=3;							
		 			    	biometrico.removeAll(biometrico);
		 			    	//banderaSinAccion=true;

						
						}
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
						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
						 			 
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
						 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
						 			    	
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
										 			    	
										 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
										 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
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
										 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
									 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
									 			    
										 			    	
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
										 			    	
										 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
									 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
									 			    
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
										 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
									 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
									 			    
										 			    	
										 			    	banderaHoraExtra1001=2;
										 			    	banderaSinAccion=false;		
										 			    	biometrico.removeAll(biometrico);
										 			    	
														}
															
			  	  										}
			  										else if (contadorDiurnoExtra>0) {
														
			  											if (contadorNocturno>0) {
			  												// horaActualizarPegadoExtraEntrada=
			  												tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
										 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
										 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", pos_marcacionNocturnoExtra);
										 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
													        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
										 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
										 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", pos_marcacionEntradaExtra1);
										 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
										 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
									 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
										 			    	
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
										 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
									 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
										 			    	
										 			    	banderaHoraExtra1001=2;
										 			    	banderaSinAccion=false;	
										 			    	bio.setIndice(biometrico.size());

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
							 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
						 			    	
										    		banderaHoraExtra1001=1;
										    		banderaSinAccion=false;
										   	    }else {
												        			    
										        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
							 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
							 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
							 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
							 			   	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
							 			    	
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
						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
						 			   	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
						 			    
									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
						 			   	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
						 			    	
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
	 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
	 			    
				    		banderaHoraExtra1001=1;
				   	    }else {
				        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	 			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
	 			    
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
	  						 			    if (horaBiometrico.equals(horaSalidaAnterior)) {
	  						 			    	banderaSinAccion=true;
										}else {
	  						 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	  						 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
	  						                	 if (banderaHoraExtra1001==0) {
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0); 
	  						 			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			        tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
	  						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
							 			    
	  									    		banderaHoraExtra1001=1;
	  									   	    }else {
	  									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
	  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	  						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
							 			    
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
  						 			    	tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "EXTRA");
  						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr",ubicacion_biometrico);
						 			    
    	    	     			    		banderaHoraExtra1001=1;
    	    	     			   	    }else {
    	    								        			    
    	    	        			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
    	    	         			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
    	    	         			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
  						 			 	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr",ubicacion_biometrico);
  						 			    	
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
                 			    tab_reporte.setValor(itemReporte, "HORAFINBAND", "");

 
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
  							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
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
    					
  						  						
  					String banderaInicio="",banderaFin="",bandEntrada="",bandSalida="";
  					banderaInicio=tab_reporte.getValor(itemReporte, "HORAINICIOBAND");
  					banderaFin=tab_reporte.getValor(itemReporte, "HORAFINBAND");
                    boolean bandIni=false,bandFin=false;
  					if (banderaInicio.equals("") || banderaInicio.isEmpty() || banderaInicio==null ) {
						bandIni=false;
					}else {
						bandIni=true;

					}
  	    			
  					if (banderaFin.equals("") || banderaFin.isEmpty() || banderaFin==null) {
						bandFin=false;
					}else {
						bandFin=true;

					}
  	    			
  					if (bandIni==true && bandFin==true) {
						bandEntrada=tab_reporte.getValor(itemReporte, "HORAINICIOBAND");
						bandSalida=tab_reporte.getValor(itemReporte, "HORAFINBAND");
					}else if (bandIni==true && bandFin==false) {
						bandEntrada=tab_reporte.getValor(itemReporte, "HORAINICIOBAND");
						bandSalida="SIN TIMBRE";
					}else if (bandIni==false && bandFin==true) {
						bandEntrada="SIN TIMBRE";
						bandSalida=tab_reporte.getValor(itemReporte, "HORAFINBAND");
					}else {
						bandEntrada="SIN TIMBRE";
						bandSalida="SIN TIMBRE";
					}
  					
  					//***************************************************************************************************************************//
  					
  					if (bandIni==false || bandFin==false) {
						//Si no tiene el timbre de entrada o el de salida

  					List<Fila> biometricoTemporalExtra;
  					biometricoTemporalExtra = obtenerTimbreBiometrico(empleado, dateFechaInicioReporteAgrupadaXEmpleado ,dateFechaInicioReporteAgrupadaXEmpleado);  			
  					boolean band=false;
  					int marcaciones=biometricoTemporalExtra.size();
  					for (Fila bioTemp : biometricoTemporalExtra) {
  					if (marcaciones==1) {
  		 				    String fechaBiometricoTemp = (String)bioTemp.getCampos()[1];
	         				String horaBiometrico = (String)bioTemp.getCampos()[2];
			   	        	String ubicacion_biometrico = (String)bioTemp.getCampos()[4];
			   	        	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  				    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "MARCACION SIN/HORARIO");
  					      	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
  						
					}else if(marcaciones>1){
		 				String fechaBiometricoTemp = (String)bioTemp.getCampos()[1];
         				String horaBiometrico = (String)bioTemp.getCampos()[2];
		   	        	String ubicacion_biometrico = (String)bioTemp.getCampos()[4];
						
  					if (bandIni==false && band==false) {
   					    	tab_reporte.setValor(itemReporte, "HORAINICIOBIOMETRICO", horaBiometrico);
  				    		tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "OTRO HORARIO");
  					      	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
  		  					bandEntrada="OTRO HORARIO";
  			     	    	band=true;
  					    	}
  					   if(bandFin==false && band==true){
  					    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
  					    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "MARCACION SIN/HORARIO");
  					    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
  					    	bandSalida="OTRO HORARIO";
  					    	}
  				
  			}
  			}
  					
					}else {
						//Si tiene el timbre de entrada o el de salida
					}
  					
 					
  					//***************************************************************************************************************************//
  					
    	    	   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	bandEntrada, 
    	 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
    	 		   tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
    	 		   tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
    	 		   bandSalida,bandera,diaSemana(fechaBiometricoAgrupadaXEmpleado),tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr")
    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_entrada")
    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_salida")
    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
    					} 
    					
  					else if (contieneDiaTurno==0 && banderaSinAccion==false &&  sinIngreso==false && banderaHoraExtra1001==0) {
    		
    					   insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
    		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
    		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
    		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
    		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado),
    		 		   tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr")
    		 		  ,tab_reporte.getValor(itemReporte, "biometrico_alm_entrada")
       	 		   	  ,tab_reporte.getValor(itemReporte, "biometrico_alm_salida")
       	 		   	  ,tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
       				
    		 		   
    					}else if (banderaSinAccion==true) {
    						
  					}
  					else if (banderaHoraExtra1001==2 || banderaHoraExtra1001==1) {
						
					if (banderaHoraExtra1001==1) {
					
						if (contieneDiaTurno!=0) {
							insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado,tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
									"", "","", "",0 ,"0",tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),false,diaSemana(fechaBiometricoAgrupadaXEmpleado),
									tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr")
					    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_entrada")
					    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_salida")
					    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
					    				
						}else {
						insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
								"", "","", "",0 ,"0","","", "",false,diaSemana(fechaBiometricoAgrupadaXEmpleado),
								tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr")
				    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_entrada")
				    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_alm_salida")
				    	 		   ,tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
						}
					
					
								    
					}
					if (banderaHoraExtra1001==2) {
						
					
	insertarTablaResumen(empleado, fechaBiometricoAgrupadaXEmpleado, "",tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
	"", "","", "",0 ,"0","",tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometricoAgrupadaXEmpleado),
	tab_reporte.getValor(itemReporte, "biometrico_entrada_cobmr")
	   ,tab_reporte.getValor(itemReporte, "biometrico_alm_entrada")
	   ,tab_reporte.getValor(itemReporte, "biometrico_alm_salida")
	   ,tab_reporte.getValor(itemReporte, "biometrico_salida_cobmr"));
		
	    
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
   		   			   	        			 String ubicacion_biometrico= "";
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
   		   			   	     				ubicacion_biometrico = (String)bio.getCampos()[4];
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
		       			    	biometrico_entrada_cobmr= ubicacion_biometrico;
		       			      	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
   		       			    
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
		       			    	biometrico_entrada_cobmr= ubicacion_biometrico;
   		       			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);

	       			
   		       			    	//tipo2=1;
   		       			    	tipo1=2;
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
   	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_alm_salida", ubicacion_biometrico);
   	   		       			    	biometrico_alm_salida= ubicacion_biometrico;
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
   		       			    	}else{

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
  	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_alm_entrada", ubicacion_biometrico);
   	   		       			    	biometrico_alm_entrada= ubicacion_biometrico;
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
   		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
  		       		
  		       			   
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
	   	       			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
	   	       			    	biometrico_salida_cobmr= ubicacion_biometrico;
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
	   	       			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
	   	      			    	biometrico_salida_cobmr= ubicacion_biometrico;
   		       			    	//tipo6=1;
   		       			    	tipo5=2;
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
   		   			   	     						mensaje="EXTRA";
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
   		   						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
   		   						 			    	biometrico_entrada_cobmr= ubicacion_biometrico;

   		   									    		banderaHoraExtra1001=1;
   		   									   	    }else {
   		   											        			    
   		   									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
   		   						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
   		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
   		   						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", mensaje);
   		   	  	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
   		   	  	   		       		biometrico_salida_cobmr= ubicacion_biometrico;
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
   	  	   		       			    	String ubicacion_biometrico = (String)bio.getCampos()[4];

						 			    
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
						 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
						 			    	biometrico_entrada_cobmr= ubicacion_biometrico;

									    		banderaHoraExtra1001=1;
									   	    }else {
											        			    
									        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
						 			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
	   	  	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
	   	  	   		       			    	biometrico_salida_cobmr= ubicacion_biometrico;
						 			    	
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
  		    							if (tab_reporte.getValor(itemReporte, "HORAINICIOBAND").equals("EXTRA")) {
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
   		   			        	
   		   								
   		   								if (tipo1==0 && tipo5==1) {
   		   								tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
   		   									bandera=false;
										}
   		   								
   		   								
   		   							if (tipo1==0 && tipo5==2) {
		   									tab_reporte.setValor(itemReporte, "HORAINICIOBAND", "");
		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
		   									bandera=false;
									}

   		   								if(tipo1==1 && tipo5==1 ){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","OK");
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
   		   								bandera=false;
										}
   		   								
   		   								if(tipo1==1 && tipo5==2){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","OK");
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
   	   		   								bandera=true;
  		   																	}

   		   							if(tipo1==1 && tipo5==0){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","OK");
   		   							tab_reporte.setValor(itemReporte, "HORAFINBAND","");
   		   							bandera=false;
  		   																	}
   		   								
   		   								
   		   								if(tipo1==2 && tipo5==0){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","ATRASADO");
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "");
   	   		   								bandera=false;
   		   							}
   		   							
   		   								
   		   								
   		   								
   		   								if(tipo1==2 && tipo5==1){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","ATRASADO");
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND", "ANTICIPADO");
   	   		   								bandera=false;
  		   																	}	
   		   								
   		   								
   		   								
   		   								if(tipo1==2 && tipo5==2){
   		   									tab_reporte.setValor(itemReporte, "HORAFINBAND","ATRASADO");
   		   							tab_reporte.setValor(itemReporte, "HORAFINBAND", "OK");
   	   		   								bandera=false;
   		   																	}	
   		   								
   		   								
   		   							
   		   			    	        insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
   		   			 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
   		   			 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
   		   			 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
   		   			 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico),biometrico_entrada_cobmr,biometrico_alm_salida,biometrico_alm_entrada, biometrico_salida_cobmr);
   		   			    	  biometrico_entrada_cobmr="";
   		   			    	  biometrico_alm_salida="";
   		   			    	  biometrico_alm_entrada="";
   		   			    	  biometrico_salida_cobmr="";
   		   			    	        
   		   							}else if (banderaHoraExtra1001!=0 && banderaSinAccion==false) {
   		   								
   		   							
   		   								   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
   		   					 		        		tab_reporte.getValor(itemReporte, "HORAINICIOALM"), tab_reporte.getValor(itemReporte, "HORAFINALM"), 
   		   					 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),almuerzo1,
   		   					 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
   		   					 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico),biometrico_entrada_cobmr,biometrico_alm_salida,biometrico_alm_entrada, biometrico_salida_cobmr);
   		   								
   		   							   //tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico),biometrico_entrada_cobmr,biometrico_alm_salida,biometrico_alm_entrada, biometrico_salida_cobmr)
   		   							  biometrico_entrada_cobmr="";
   		   		   			    	  biometrico_alm_salida="";
   		   		   			    	  biometrico_alm_entrada="";
   		   		   			    	  biometrico_salida_cobmr="";
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
 	   		       			    String ubicacion_biometrico ="";

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
   	 	   		       			    ubicacion_biometrico = (String)bio.getCampos()[4];
   		            				
   		            			
   		            			
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
					 			    	tab_reporte.setValor(itemReporte, "biometrico_entrada_cobmr", ubicacion_biometrico);
					 			    	biometrico_entrada_cobmr= ubicacion_biometrico;
   		        			    		banderaHoraExtra1001=1;
   		        			   	    }else {
   		   							        			    
   		           			        tab_reporte.setValor(itemReporte, "TIEMPOHORALM", ""+0);
   		            			    	tab_reporte.setValor(itemReporte, "TIEMPOALM", ""+0);
   		            			    	tab_reporte.setValor(itemReporte, "HORAFINBIOMETRICO", horaBiometrico);
   		            			    	tab_reporte.setValor(itemReporte, "HORAFINBAND", "EXTRA");
   	  	   		       			    	tab_reporte.setValor(itemReporte, "biometrico_salida_cobmr", ubicacion_biometrico);
   	  	   		       			    	biometrico_salida_cobmr= ubicacion_biometrico;
   		            			    	banderaHoraExtra1001=2;
   		   	     				
   		        				
   		   						        				
   		            				}
   		            			   
   		            			    	 
   		            			    	 
   		            			    }
   		            			    
   		            			}//Fin de timbradas en biometrico 
   		            			
   		            			
   		            			
   		            			if (biometrico.size()!=0 ){
   		    					   insertarTablaResumen(empleado, fechaBiometrico, tab_reporte.getValor(itemReporte, "HORAINICIOHORARIO"),tab_reporte.getValor(itemReporte, "HORAINICIOBIOMETRICO"), 	tab_reporte.getValor(itemReporte, "HORAINICIOBAND"), 
   		    		 		        		"", tab_reporte.getValor(itemReporte, "HORAFINALM"), 
   		    		 		    tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"), tab_reporte.getValor(itemReporte, "HORAFINALMBIO"),0,
   		    		 		    tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), 
   		    		 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico),biometrico_entrada_cobmr,"","",biometrico_salida_cobmr);         			
   		    					biometrico_salida_cobmr="";
   		    					biometrico_entrada_cobmr="";
   		             			}
   		            			}else {
   		   					//Si no tiene marcaciones 	
   		            			   insertarTablaResumen(empleado, fechaBiometrico, "", "","SIN HORARIO", "", "",
   		   		 		   "", "",0,"",
   		   		 		    "","","SIN HORARIO",false,diaSemana(fechaBiometrico),"","","","");         			
   		            			
   		            			
   		            			}
   		     	    			//si tiene marcaciones
   		     	    	
   		     	    			
   		            			
   		           	// sin tiene marcacion en el biometrico inserta sino no hace nada
   		            	
   		            			
   		   					
   		   				}
   		              		 
   				
   				
   				
   				
   				
   				
   				
   				       }

    	
    	
    	//Validacion de horas extra por empleado
    	
    	if (!empleadoSeleccionado.equals("") || !empleadoSeleccionado.isEmpty() || empleadoSeleccionado!=null && actualizarEmpleado==false) {
    	
    	
    	
    	
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

						
				
				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " novedad_cobmr =true  where horainicioband_cobmr like '%FERIADO%' and horafinband_cobmr like '%FERIADO%' "
						+ " and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' and novedad_cobmr =false and ide_gtemp in("+empleadoSeleccionado+") ");*/
   			/**
   			 * VALIDACION ALMUERZO
   			 */
	    		tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"'");
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

    private Date deStringADate(String fecha) throws ParseException{
    	Date date=null;
    	try {
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			date = simpleDateFormat.parse(fecha);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date;
    }
    
    
    private List<Fila> obtenerTimbreBiometrico(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
    	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
    	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
    	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
    				+ "EMP.IDE_GTEMP,"
        			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
        			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM,RELOJ.DETALLE_COREL  "
        			+ "from gth_empleado emp  "
        			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
        			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
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
    			+ ",BIO.EVENTO_RELOJ_COBIM "
    			+ ",RELOJ.DETALLE_COREL  "
    			+ "from "
    			+ "con_biometrico_marcaciones as bio  "
    			+ "left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp "
    			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
    			+ "where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"'  "
    			+ "and emp.ide_gtemp="+IDE_GTEM 
    			//+ " order by  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc ");
    			+ "  order by  TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') asc ");
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
    
    
	public void seleccionarJustificar() {
		
		tab_tabla.getFilaSeleccionada();
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
 	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    

 	public void limpiar(){
 		tab_tabla.limpiar();
 		utilitario.addUpdate("tab_tabla,tab_novedad,cal_fecha_final,cal_fecha_inicial,set_departamento,set_empleado");
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
			 String dia_cobmr,
		  	String biometrico_entrada_cobmr,
		  	String biometrico_alm_entrada,
		  	String biometrico_alm_salida, 
		  	String biometrico_salida_cobmr 
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
				  		+ "dia_cobmr," 
				  		+ "biometrico_entrada_cobmr," 
				  		+ "biometrico_alm_entrada," 
				  		+ "biometrico_alm_salida," 
				  		+ "biometrico_salida_cobmr)" + 

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
				  		+ "'"+dia_cobmr+"', "
				  		+ "'"+biometrico_entrada_cobmr+"', "
				  		+ "'"+biometrico_alm_entrada+"', "
				  		+ "'"+biometrico_alm_salida+"', "
				  		+ "'"+biometrico_salida_cobmr+"')"); 
		 
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
		    	+ "'' AS HORAFINBAND, "
		    	+ "'' AS biometrico_entrada_cobmr, "
		    	+ "'' AS biometrico_alm_entrada,  "
		    	+ "'' AS biometrico_alm_salida, "
		    	+ "'' AS biometrico_salida_cobmr "	
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
		    	+ "'' AS HORAFINBAND, "
		    	+ "'' AS biometrico_entrada_cobmr, "
		    	+ "'' AS biometrico_alm_entrada,  "
		    	+ "'' AS biometrico_alm_salida, "
		    	+ "'' AS biometrico_salida_cobmr "	
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



	public Tabla getTab_resumen_marcaciones() {
		return tab_resumen_marcaciones;
	}



	public void setTab_resumen_marcaciones(Tabla tab_resumen_marcaciones) {
		this.tab_resumen_marcaciones = tab_resumen_marcaciones;
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
	SINDATO, SINDATO,SINDATO, SINDATO,Double.parseDouble(tab_reporte.getValor(itemReporte, "TIEMPOALM")) , tab_reporte.getValor(itemReporte, "TIEMPOHORALM"), tab_reporte.getValor(itemReporte, "HORAFINHORARIO"),tab_reporte.getValor(itemReporte, "HORAFINBIOMETRICO"), tab_reporte.getValor(itemReporte, "HORAFINBAND"),true,diaSemana(fechaBiometrico)
	,"","","","");
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
	 		   tab_reporte.getValor(itemReporte, "HORAFINBAND"),bandera,diaSemana(fechaBiometrico),"","","","");
	 		  //       }    


	}
	    	    }else {//else si no encuentra horario
					
				}

	
				}

	    	
	    	//Asigno los datos extraidos a la tabla para reporte
	    	tab_tabla.setFilas((LinkedList<Fila>)tab_reporte.getFilas());
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
			//   + "and EMP.ide_gtemp IN(SELECT IDE_GTEMP GTH_EMPLEADO WHERE IDE_ASTUR=1) "
			  // + "and EMP.ide_gtemp IN(select ide_gtemp from asi_horario_mes_empleado where ide_gtemp  in( "
			  // + "select ide_gtemp from gen_empleados_departamento_par where ide_gttem=1 and activo_geedp=true "
			  // + "order by ide_gtemp) "
			  // + "and ide_gemes=9 and ide_geani=11 order by ide_gtemp asc) "
			+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
			+ "ORDER BY EMP.IDE_GTEMP");		
	 }else {
		 tab_reporte= utilitario.consultar(
			    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
					+ "from gth_empleado emp  "
					+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
					+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				   + "and EMP.ide_gtemp IN("+IDE_GTEMP+") "
				   //+ "and EMP.ide_gtemp IN(101,103,104,107,108) "
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


public String getHoraActualizarPegado() {
	return horaActualizarPegado;
}


public void setHoraActualizarPegado(String horaActualizarPegado) {
	this.horaActualizarPegado = horaActualizarPegado;
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

public String retornaIdeAnio(String anio){
	String anioActual="";
	TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%' ");
	return anioActual=tabAnio.getValor("ide_geani");
	
}


public  String  retornaMes(int mes){
	String mes_cobmr1="";
	if (mes<10) {
			mes_cobmr1= "0"+mes;
	}else {
			mes_cobmr1=""+mes;

	}
return mes_cobmr1;
	
}


public  String  retornaDia(int dia){
	String dia_cobmr1="";
	if (dia<10) {
		dia_cobmr1= "0"+dia;
}else {
		dia_cobmr1= ""+dia;

}
	return dia_cobmr1;
}



public int fechaFinSemana(Date fch) {
	
	Calendar fechaInicial = Calendar.getInstance();
	fechaInicial.setTime(fch);
	int contador  = 0;
		if (fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ||  fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			contador= fechaInicial.get(Calendar.DATE);
		}
		return contador;
}




/**
 * Metodo devuelve los dias que no han sido marcados por el empleado
 * @param fechaInicial
 * @param fechaFinal
 * @param ide_gemes
 * @param ide_geani
 * @param empleadoSeleccionado
 * @param actualizarEmpleado
 */
public void getSinMarcacionesEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani,String  empleadoSeleccionado,boolean actualizarEmpleado){
	

//Variables de inicio
	int contador = 0;
	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;
	 String fechaBiometrico="";
	 //Estados de marcaciones
		boolean estadoEntrada=false,estadoTarde=false,estadoNocturno=false;
	//
    StringBuilder str_ide_horarios=new StringBuilder(); 
    boolean banderaMatriz= false,banderaExtra=false;
	String strFechaIniReporte =fechaInicial;
 	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
	String strFechaFinReporte = fechaFinal;
	Date dateFechaInicioReporte = getFechaAsyyyyMMdd(fechaInicial);
	Date dateFechaFinReporte = getFechaAsyyyyMMdd(fechaFinal);
	//Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
	//Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
	//Retorno el numero de dias de diferencia entre un rango de fechas
	int diferencia_dias=utilitario.getDiferenciasDeFechas(dateFechaInicioReporte,dateFechaFinReporte);
	Date DiaSuma=null;
	//System.out.println("Diferencia dias"+diferencia_dias);
	
	//Validacion obtiene el mes y anio para el reporte
	if (utilitario.getMes(fechaInicial)== utilitario.getMes(fechaInicial)) {
	if (utilitario.getAnio(fechaInicial)== utilitario.getAnio(fechaInicial)) {
		ide_gemes=utilitario.getMes(fechaInicial);
		int anio=utilitario.getAnio(fechaInicial);
	 	TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%' ");
	 	ide_geani=Integer.parseInt(tabAnioReporte.getValor("ide_geani"));
	
		}
		}
	
	
	
	//Lista con el dia,mes y anio seleccionado
	ArrayList<Integer> listaDiaSeleccionado = new ArrayList<Integer>();
	ArrayList<Integer> listaMesSeleccionado = new ArrayList<Integer>();
	ArrayList<Integer> listaAnioSeleccionado = new ArrayList<Integer>();

	//recorro el numero de dias de diferencia
	for (int i = 0; i < diferencia_dias+1; i++) {
			//Sumo un dia a la vez
		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
			//Obtengo el dia
			utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
			//Obtengo el mes
    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
    		//Agrego a la lista el dia
    		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
    		//Agrego a la lista el mes
    		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
    		//Agrego a la lista el anio
    		listaAnioSeleccionado.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
		}
	
	//lista sin fin de semana
	ArrayList<Integer> listaDiaSeleccionadoSinFinSemana = new ArrayList<Integer>();
	ArrayList<Integer> listaMesSeleccionadoSinFinSemana = new ArrayList<Integer>();
	ArrayList<Integer> listaAnioSeleccionadoSinFinSemana = new ArrayList<Integer>();
	
	int finSemana=0;
	for (int i = 0; i < diferencia_dias+1; i++) {
		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
		finSemana=fechaFinSemana(DiaSuma);
		if (finSemana==0) {
			//Si ni es fin de semana añado elemento
			utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
    		listaDiaSeleccionadoSinFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
    		listaMesSeleccionadoSinFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
    		listaAnioSeleccionadoSinFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
			finSemana=0;
		}  		
		
	}     	
	
	
	
	
	//Obtengo los ides de los empleados que han timbrado 
  	TablaGenerica tab_reporteEmpleado=getTablaResumenMarcacionesEmpleado(fechaInicial, fechaFinal);
  	
	ArrayList<Integer> listaDiasEmpleado = new ArrayList<Integer>();
	ArrayList<Integer> listaMesEmpleado = new ArrayList<Integer>();
	ArrayList<Integer> listaAnioEmpleado = new ArrayList<Integer>();
	
	//Recorro la tabla con los empleados 
	for (int itemReporteEmpleado=0; itemReporteEmpleado< tab_reporteEmpleado.getTotalFilas(); itemReporteEmpleado++) { 		
   		//ide de los empleados
		int contDiasEmp=0;
		Integer empleado = Integer.parseInt(tab_reporteEmpleado.getValor(itemReporteEmpleado, "IDE_GTEMP"));
		//Consulto las marcaciones realizadas por empleado en un rango de fechas
		TablaGenerica tab_reporteTimbradasXDia=getTablaMarcacionesEmpleado(strFechaIniReporte, strFechaFinReporte,""+empleado);
    	 //recorro las timbradas 
    	for (int itemReporteTimbradasXDia=0; itemReporteTimbradasXDia< tab_reporteTimbradasXDia.getTotalFilas(); itemReporteTimbradasXDia++) {
    		//Obtengo el dia Timbrado
    		listaDiasEmpleado.add(utilitario.getDia(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"fecha_evento_cobmr")));
    		//Obtengo el mes 
    		listaMesEmpleado.add(utilitario.getMes(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"fecha_evento_cobmr")));    		
    		listaAnioEmpleado.add(utilitario.getAnio(tab_reporteTimbradasXDia.getValor(itemReporteTimbradasXDia,"fecha_evento_cobmr")));    		
  		
		}
    	
    	//TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+listaAnioEmpleado.get(0).toString()+"%' ");
		//	String anioActualReporte=tabAnioReporte.getValor("ide_geani");

    	
    	
    	/////////////////////////////////////////////////AQUI DEBO VALIDAR CUANDO OTRO MES
    	//Consulto si el empleado contiene un horario anual
    	TablaGenerica tabMatrix=utilitario.consultar("select IDE_GTEMP,IDE_ASTUR from gth_empleado where ide_gtemp ="+empleado+" and ide_astur=1");
    	//Consulto si el empleado contiene un turno mensual
    	TablaGenerica tabEmpleadoMatrizMensual1 = utilitario.consultar("select IDE_GTEMP,IDE_ASHME from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
				+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" ");
    	
    	if (tabMatrix.getTotalFilas()==0 && tabEmpleadoMatrizMensual1.getTotalFilas()>0) {
    	//Si contiene matriz anual
		
    	//Elimino los dias q si fueron timbrados por la persona 
    	for (int i = 0; i < listaDiasEmpleado.size(); i++) {
    		//for recorre la lista de timbradas por empleado
			for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
				//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
				if ( (listaDiasEmpleado.get(i).toString().equals(listaDiaSeleccionado.get(j).toString())) && 
					 (listaMesEmpleado.get(i).toString().equals(listaMesSeleccionado.get(j).toString())) && 
					(listaAnioEmpleado.get(i).toString().equals(listaAnioSeleccionado.get(j).toString()) ) ) {
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
    	
    	
    	//recorro la nueva lista generada  	 
    	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
       	TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+listaAnioSeleccionado.get(i).toString()+"%' ");
   			String anioActual=tabAnio.getValor("ide_geani");
   		//Consulto los turnos asignados a los dias timbrados
    		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
    				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+anioActual);
    	
    		
    		//Obtengo la matriz de la persona
  if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
	String diaMatrizMensual="";
	//////FERIADO
	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
    			//si no existe asignacion de horario o si es libre
	if (diaMatrizMensual==null || diaMatrizMensual.isEmpty() || diaMatrizMensual.equals("") || diaMatrizMensual.equals("19")) {
	diaMatrizMensual="";	
    				//remuevo esos dias del calendario
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
    	
    	
    	
    	
    	//recorro la nueva lista sin los dias que no contienen asignacion de horario
    	for (int i = 0; i < listaDiaSeleccionado.size(); i++) {
    		TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+listaAnioSeleccionado.get(i).toString()+"%' ");
   			String anioActual=tabAnio.getValor("ide_geani");
    		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
    				+ "and ide_gemes="+listaMesSeleccionado.get(i).toString()+" and ide_geani="+anioActual);
	String diaMatrizMensual="";
	if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
	diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionado.get(i).toString());
    			//obtengo el turno asignado a ese dia
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
   			}else{
   				tiempoAlmEmpl=""+(Integer.parseInt(horariosEmpleadoMes.getValor( "MIN_ALMUERZO_ASHOR"))/60);
   			}
   			if (tiempoAlmEmpl.equals("0")) {
   				horaFinAlmEmpl="S/A";
   				horaIniAlmEmpl="S/A";
   				hora1="S/A";
   				hora2="S/A";
			}

   			//Validacion del dia que contenga 
   			
   			
   			TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionado.get(i).toString()+"%' ");
   			String anioActual1=tabAnio1.getValor("detalle_geani");
   			String dia_cobmr1="";
   			String mes_cobmr1="";
   			String fecha_evento_cobmr="";
   			//Voy ceando las fechas para el registro de datos en la tabla
   			if (listaDiaSeleccionado.get(i).intValue()<10) {
   				dia_cobmr1= "0"+listaDiaSeleccionado.get(i).toString();
			}else {
   				dia_cobmr1= listaDiaSeleccionado.get(i).toString();

			}
   			
   			if (listaMesSeleccionado.get(i).intValue()<10) {
   				mes_cobmr1= "0"+listaMesSeleccionado.get(i).toString();
			}else {
   				mes_cobmr1= listaMesSeleccionado.get(i).toString();

			}
   					
   				fecha_evento_cobmr= listaAnioSeleccionado.get(i).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

			
   			
   			
   			
  			
  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
  			//Validacion fecha feriado
  			 if (banderaFeriados==true) {
  				 insertarTablaResumen(empleado, fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
  		  	  	
				//Si se tiene feriado ese dia no se ingrese
			}else if(getEmpleadoActivo(empleado)==false){
				
			}
  			 
  			 
  			 else {
				 //Si no es un dia fericado que se ingrese como no timbrada
				 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr
						 ,"","","","");  		
		         }
  			
    	}else {
    		i=listaDiaSeleccionado.size();	
    	     listaDiasEmpleado = new ArrayList<Integer>();
        	 listaMesEmpleado = new ArrayList<Integer>();
        	 listaAnioEmpleado = new ArrayList<Integer>();
		}
    	
	
    	}
  	     listaDiasEmpleado = new ArrayList<Integer>();
    	 listaMesEmpleado = new ArrayList<Integer>();
    	 listaAnioEmpleado = new ArrayList<Integer>();
    	//Lista con el dia,mes y anio seleccionado
    	listaDiaSeleccionado = new ArrayList<Integer>();
    	listaMesSeleccionado = new ArrayList<Integer>();
    	listaAnioSeleccionado = new ArrayList<Integer>();
    	
    	//recorro el numero de dias de diferencia
    	for (int i = 0; i < diferencia_dias+1; i++) {
    			//Sumo un dia a la vez
    			DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
    			//Obtengo el dia
    			utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
    			//Obtengo el mes
        		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
        		//Agrego a la lista el dia
        		listaDiaSeleccionado.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
        		//Agrego a la lista el mes
        		listaMesSeleccionado.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
        		//Agrego a la lista el anio
        		listaAnioSeleccionado.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
        		//System.out.println("Dias"+listaDiaSeleccionado.get(i).toString());
    		    //System.out.println("Mes"+listaMesSeleccionado.get(i).toString());
    		    //System.out.println("Anio"+listaAnioSeleccionado.get(i).toString());
    		}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	}else if (tabMatrix.getTotalFilas()>0 && tabEmpleadoMatrizMensual1.getTotalFilas()==0) {			
    		//Elimino los dias q si fueron timbrados por la persona 
        	for (int i = 0; i < listaDiasEmpleado.size(); i++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionadoSinFinSemana.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
					
					//System.out.println("empleado"+ empleado +"      sadid: "+listaDiaSeleccionadoSinFinSemana.get(j).toString());
					if ( (listaDiasEmpleado.get(i).toString().equals(listaDiaSeleccionadoSinFinSemana.get(j).toString())) && 
						 (listaMesEmpleado.get(i).toString().equals(listaMesSeleccionadoSinFinSemana.get(j).toString())) && 
						(listaAnioEmpleado.get(i).toString().equals(listaAnioSeleccionadoSinFinSemana.get(j).toString()) ) ) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						listaDiaSeleccionadoSinFinSemana.remove(j);
						//elimino de la lista el dia que si tenga timbrada y asignado
						listaMesSeleccionadoSinFinSemana.remove(j);
						//elimino el mes que corresponde a esa fecha con su timbrada}
						listaAnioSeleccionadoSinFinSemana.remove(j);

						j=listaDiaSeleccionadoSinFinSemana.size();
						//variable para pasar a una nueva timbrada si esta cumple con comparacion 
					}
				}
			}
        	
        	//Recorro la lista con el numero de dias sin timbrar
        	for (int i = 0; i < listaDiaSeleccionadoSinFinSemana.size(); i++) {
    		TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionadoSinFinSemana.get(i).toString()+"%' ");
   			String anioActual1=tabAnio1.getValor("detalle_geani");
   			String dia_cobmr1="";
   			String mes_cobmr1="";
   			String fecha_evento_cobmr="";
   			//Voy ceando las fechas para el registro de datos en la tabla
   			if (listaDiaSeleccionadoSinFinSemana.get(i).intValue()<10) {
   				dia_cobmr1= "0"+listaDiaSeleccionadoSinFinSemana.get(i).toString();
			}else {
   				dia_cobmr1= listaDiaSeleccionadoSinFinSemana.get(i).toString();

			}
   			
   			if (listaMesSeleccionadoSinFinSemana.get(i).intValue()<10) {
   				mes_cobmr1= "0"+listaMesSeleccionadoSinFinSemana.get(i).toString();
			}else {
   				mes_cobmr1= listaMesSeleccionadoSinFinSemana.get(i).toString();

			}
   			//Creo la fecha sin marcacion
   				fecha_evento_cobmr= listaAnioSeleccionadoSinFinSemana.get(i).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;
        	
        
			//	 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		


  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
  			
  			//Validacion fecha feriado
  			 if (banderaFeriados==true) {
  				 insertarTablaResumen(empleado, fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
				//Si se tiene feriado ese dia no se ingrese
			}//else if(getEmpleadoActivo(empleado)==false){
				
			//}
  			 
  			 
  			 else {
  				 
  				Calendar calFechaDia = Calendar.getInstance();
   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
   	     				calFechaDia.setTime(getFechaAsyyyyMMdd(fecha_evento_cobmr)); 
   	     				

  				 //Obtengo el dia de la semana a que corresponde esa fecha
  				 
   	     		int diaSemana = calFechaDia.get(Calendar.DAY_OF_WEEK);
  				 
  		    	//Metodo devuelve el horario asignado a cada empleado
   			       	TablaGenerica diasXHorario=utilitario.consultar("select EMP.IDE_GTEMP,  "
   			       			+ "horario.ide_ashor, "
   			       			+ "DIA.IDE_GEDIA,DIA.DETALLE_GEDIA, "
   			       			+ "HORARIO.HORA_INICIAL_ASHOR, "
   			       			+ "HORARIO.HORA_FINAL_ASHOR, "
   			       			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.NOM_ASHOR "
   			       			+ "from gth_empleado emp "
   			       			+ "left join asi_turnos_horario turnos on turnos.ide_astur=emp.ide_astur "
   			       			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor  "
   			       			+ "left join asi_dia_horario diahor on diahor.ide_ashor=horario.ide_ashor "
   			       			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=DIAHOR.IDE_GEDIA "
   			       			+ "where emp.ide_astur is not null  AND EMP.IDE_GTEMP="+empleado+" and "
   			       			+ "dia.ide_gedia="+(diaSemana-1)+" "
   			       			+ "ORDER BY HORARIO.IDE_ASHOR ASC");
   			    	
  				 
  				 
				 //Si no es un dia fericado que se ingrese como no timbrada
				 //insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		
		         
			 insertarTablaResumen(empleado, fecha_evento_cobmr, diasXHorario.getValor("HORA_INICIAL_ASHOR"), "", "SIN TIMBRE", diasXHorario.getValor("HORA_INICIO_ALMUERZO_ASHOR"),
			 diasXHorario.getValor("HORA_FIN_ALMUERZO_ASHOR"), "","", 0.0,diasXHorario.getValor("MIN_ALMUERZO_ASHOR") ,diasXHorario.getValor("HORA_FINAL_ASHOR") , "", "SIN TIMBRE", false, dia_cobmr,
			 "","","","");  		
  			 	}
        	}
        	
        	
    		
		}else {
			//No reaaliza nada
    		//Elimino los dias q si fueron timbrados por la persona 
        	for (int i = 0; i < listaDiasEmpleado.size(); i++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionadoSinFinSemana.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
					
					//System.out.println("empleado"+ empleado +"      sadid: "+listaDiaSeleccionadoSinFinSemana.get(j).toString());
					if ( (listaDiasEmpleado.get(i).toString().equals(listaDiaSeleccionadoSinFinSemana.get(j).toString())) && 
						 (listaMesEmpleado.get(i).toString().equals(listaMesSeleccionadoSinFinSemana.get(j).toString())) && 
						(listaAnioEmpleado.get(i).toString().equals(listaAnioSeleccionadoSinFinSemana.get(j).toString()) ) ) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						listaDiaSeleccionadoSinFinSemana.remove(j);
						//elimino de la lista el dia que si tenga timbrada y asignado
						listaMesSeleccionadoSinFinSemana.remove(j);
						//elimino el mes que corresponde a esa fecha con su timbrada}
						listaAnioSeleccionadoSinFinSemana.remove(j);

						j=listaDiaSeleccionadoSinFinSemana.size();
						//variable para pasar a una nueva timbrada si esta cumple con comparacion 
					}
				}
			}
        	
        	//Recorro la lista con el numero de dias sin timbrar
        	for (int i = 0; i < listaDiaSeleccionadoSinFinSemana.size(); i++) {
    		TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionadoSinFinSemana.get(i).toString()+"%' ");
   			String anioActual1=tabAnio1.getValor("detalle_geani");
   			String dia_cobmr1="";
   			String mes_cobmr1="";
   			String fecha_evento_cobmr="";
   			//Voy ceando las fechas para el registro de datos en la tabla
   			if (listaDiaSeleccionadoSinFinSemana.get(i).intValue()<10) {
   				dia_cobmr1= "0"+listaDiaSeleccionadoSinFinSemana.get(i).toString();
			}else {
   				dia_cobmr1= listaDiaSeleccionadoSinFinSemana.get(i).toString();

			}
   			
   			if (listaMesSeleccionadoSinFinSemana.get(i).intValue()<10) {
   				mes_cobmr1= "0"+listaMesSeleccionadoSinFinSemana.get(i).toString();
			}else {
   				mes_cobmr1= listaMesSeleccionadoSinFinSemana.get(i).toString();

			}
   			//Creo la fecha sin marcacion
   				fecha_evento_cobmr= listaAnioSeleccionadoSinFinSemana.get(i).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;
        	
        
			//	 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		


  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
  			 if (banderaFeriados==true) {
				//Si se tiene feriado ese dia no se ingrese
  				 insertarTablaResumen(empleado, fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
  			  	
  			 }//else if(getEmpleadoActivo(empleado)==false){
				
			//}
  			 
  			 
  			 else {
  				 
  				Calendar calFechaDia = Calendar.getInstance();
   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
   	     				calFechaDia.setTime(getFechaAsyyyyMMdd(fecha_evento_cobmr)); 
   	     				

  				 //Obtengo el dia de la semana a que corresponde esa fecha
  				 
   	     		int diaSemana = calFechaDia.get(Calendar.DAY_OF_WEEK);
  				 
  		    	//Metodo devuelve el horario asignado a cada empleado
   			       	TablaGenerica diasXHorario=utilitario.consultar("select EMP.IDE_GTEMP,  "
   			       			+ "horario.ide_ashor, "
   			       			+ "DIA.IDE_GEDIA,DIA.DETALLE_GEDIA, "
   			       			+ "HORARIO.HORA_INICIAL_ASHOR, "
   			       			+ "HORARIO.HORA_FINAL_ASHOR, "
   			       			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
   			       			+ "HORARIO.NOM_ASHOR "
   			       			+ "from gth_empleado emp "
   			       			+ "left join asi_turnos_horario turnos on turnos.ide_astur=emp.ide_astur "
   			       			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor  "
   			       			+ "left join asi_dia_horario diahor on diahor.ide_ashor=horario.ide_ashor "
   			       			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=DIAHOR.IDE_GEDIA "
   			       			+ "where emp.ide_astur is not null  AND EMP.IDE_GTEMP="+empleado+" and "
   			       			+ "dia.ide_gedia="+(diaSemana-1)+" "
   			       			+ "ORDER BY HORARIO.IDE_ASHOR ASC");
   			    	
  				 
  				 
				 //Si no es un dia fericado que se ingrese como no timbrada
				 //insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		
		         
			 insertarTablaResumen(empleado, fecha_evento_cobmr, diasXHorario.getValor("HORA_INICIAL_ASHOR"), "", "SIN TIMBRE", diasXHorario.getValor("HORA_INICIO_ALMUERZO_ASHOR"),
			 diasXHorario.getValor("HORA_FIN_ALMUERZO_ASHOR"), "","", 0.0,diasXHorario.getValor("MIN_ALMUERZO_ASHOR") ,diasXHorario.getValor("HORA_FINAL_ASHOR") , "", "SIN TIMBRE", false, dia_cobmr,"","","","");  		
  			 	}
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
    	
    	
    	
    	
    	
    	
        listaDiaSeleccionadoSinFinSemana = new ArrayList<Integer>();
    	listaMesSeleccionadoSinFinSemana = new ArrayList<Integer>();
    	listaAnioSeleccionadoSinFinSemana = new ArrayList<Integer>();
    	
    	finSemana=0;
    	for (int i = 0; i < diferencia_dias+1; i++) {
    		DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
    		finSemana=fechaFinSemana(DiaSuma);
    		
    		if (finSemana==0) {
    			utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
	    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
	    		listaDiaSeleccionadoSinFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
	    		listaMesSeleccionadoSinFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
	    		listaAnioSeleccionadoSinFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
				finSemana=0;
			}    		
		}     	
    	      	     	
	}


}




public void getDiasLibresEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani){

	//Variables de inicio
			int contador = 0;
			int entrada=0,almuerzo=0,salida=0;
		    int entradaNocturno=0,salidaNocturno=0;
			String fechaBiometrico="";
			//Estados de marcaciones
			boolean estadoEntrada=false,estadoTarde=false,estadoNocturno=false;
			//
			Date DiaSuma=null;

		    StringBuilder str_ide_horarios=new StringBuilder(); 
		    boolean banderaMatriz= false,banderaExtra=false;
		 	String strFechaIniReporte =fechaInicial;
		 	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
			String strFechaFinReporte = fechaFinal;
			//Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
			//Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
			Date dateFechaInicioReporte = getFechaAsyyyyMMdd(fechaInicial);
			Date dateFechaFinReporte = getFechaAsyyyyMMdd(fechaFinal);
			int diferencia_dias=utilitario.getDiferenciasDeFechas(dateFechaInicioReporte,dateFechaFinReporte);

			int diaInicio=utilitario.getDia(fechaInicial);
			int diaFin=utilitario.getDia(fechaFinal);
			
			//Validacion obtiene el mes y anio para el reporte
			if (utilitario.getMes(fechaInicial)== utilitario.getMes(fechaInicial)) {
			if (utilitario.getAnio(fechaInicial)== utilitario.getAnio(fechaInicial)) {
				ide_gemes=utilitario.getMes(fechaInicial);
				int anio=utilitario.getAnio(fechaInicial);
			 	TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%' ");
			 	ide_geani=Integer.parseInt(tabAnioReporte.getValor("ide_geani"));
				
				}
				}
			
			 TablaGenerica tabObternerEmpleados=this.getTablaResumenMarcacionesEmpleado(fechaInicial,fechaFinal);
			
			 
			 
			for (int i = 0; i < tabObternerEmpleados.getTotalFilas(); i++) {
			TablaGenerica tabMarcacionesEmpleados=this.getTablaMarcacionesEmpleado(fechaInicial,fechaFinal,tabObternerEmpleados.getValor(i, "IDE_GTEMP"));
				
			

			//Retorno el numero de dias de diferencia entre un rango de fechas
				//Lista con el dia,mes y anio seleccionado
			ArrayList<Integer> listaDiaSeleccionado = new ArrayList<Integer>();
			ArrayList<Integer> listaMesSeleccionado = new ArrayList<Integer>();
			ArrayList<Integer> listaAnioSeleccionado = new ArrayList<Integer>();

			//recorro el numero de dias de diferencia
			for (int j = 0; j < tabMarcacionesEmpleados.getTotalFilas(); j++) {
		    		//Agrego a la lista el dia
		    		listaDiaSeleccionado.add(utilitario.getDia(tabMarcacionesEmpleados.getValor(j, "fecha_evento_cobmr")));
		    		//Agrego a la lista el mes
		    		listaMesSeleccionado.add(utilitario.getMes(tabMarcacionesEmpleados.getValor(j, "fecha_evento_cobmr")));
		    		//Agrego a la lista el anio
		    		listaAnioSeleccionado.add(utilitario.getAnio(tabMarcacionesEmpleados.getValor(j, "fecha_evento_cobmr")));
				}
			
			
			
	    	TablaGenerica tabMatrix=utilitario.consultar("select IDE_GTEMP,IDE_ASTUR from gth_empleado where ide_gtemp ="+tabObternerEmpleados.getValor(i, "IDE_GTEMP")+" and ide_astur=1 ");
	    			//+ "and activo_gtemp=true");
	    	TablaGenerica tabEmpleadoMatrizMensual1 = utilitario.consultar("select IDE_GTEMP,IDE_ASHME from asi_horario_mes_empleado where ide_gtemp="+tabObternerEmpleados.getValor(i, "IDE_GTEMP")+"  "
					+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" ");
			
			// Para personal con asignacion de horario
	    	
	    	if (tabMatrix.getTotalFilas()==0 && tabEmpleadoMatrizMensual1.getTotalFilas()>0) {
	    	    
				
			//lista sin fin de semana
			ArrayList<Integer> listaDia = new ArrayList<Integer>();
			ArrayList<Integer> listaMes = new ArrayList<Integer>();
			ArrayList<Integer> listaAnio = new ArrayList<Integer>();
			
			for (int x=diaInicio; x <= diaFin; x++) {
				TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+tabObternerEmpleados.getValor(i, "IDE_GTEMP")+"  "
	    				+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani);
	    	
	    		
	    		//Obtengo la matriz de la persona
	  if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
		  //Tiene Matriz
//		utilitario.agregarMensaje("sI CONTIENE MATRIZ", "");
		//String diaMatrizMensual="";

		//////FERIADO
		String diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+x);
		String mesMatrizMensual=tabEmpleadoMatrizMensual.getValor("IDE_GEMES");
		
		TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+tabEmpleadoMatrizMensual.getValor("IDE_GEANI")+" ");
		String anioActual1=tabAnio1.getValor("detalle_geani");

		//pulo por dias la matriz
		if (diaMatrizMensual==null || diaMatrizMensual.isEmpty() || diaMatrizMensual.equals("") || diaMatrizMensual.equals("19")) {
		//Para que no me ingrese varaias veces el dia que no se tiene asignado
		//	System.out.println("SIN ASIGNACION");
			listaDia.add(x);
			listaMes.add(Integer.parseInt(mesMatrizMensual));
			listaAnio.add(Integer.parseInt(anioActual1));		
			}else{
				
			}
	  		}
			}	
			
			//Retorno el numero de dias de diferencia entre un rango de fechas
			//Lista con el dia,mes y anio seleccionado
		ArrayList<Integer> listaDiasLibres= new ArrayList<Integer>();
		ArrayList<Integer> listaMesLibres = new ArrayList<Integer>();
		ArrayList<Integer> listaAnioLibres = new ArrayList<Integer>();

	    	
			
			boolean bandInsertListaLire=false;
			int insertar=listaDiaSeleccionado.size()-1;
	    	for (int x = 0; x < listaDia.size(); x++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
				
					if ( (listaDia.get(x).toString().equals(listaDiaSeleccionado.get(j).toString())) && 
						 (listaMes.get(x).toString().equals(listaMesSeleccionado.get(j).toString())) && 
						(listaAnio.get(x).toString().equals(listaAnioSeleccionado.get(j).toString()) ) ) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						j=listaDiaSeleccionado.size();
						
					}else{	
					 if (j==insertar) {
						 listaDiasLibres.add(listaDia.get(x));
							//elimino de la lista el dia que si tenga timbrada y asignado
							listaMesLibres.add(listaMes.get(x));
							//elimino el mes que corresponde a esa fecha con su timbrada}
							listaAnioLibres.add(listaAnio.get(x));
							//j=listaDiaSeleccionado.size();
					}
						
					}
				}
			}
			
			
	    	for (int j = 0; j < listaDiasLibres.size(); j++) {
	    	
			TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioLibres.get(j).toString()+"%' ");
   			String anioActual1=tabAnio1.getValor("detalle_geani");
   			String dia_cobmr1="";
   			String mes_cobmr1="";
   			String fecha_evento_cobmr="";
   			//Voy ceando las fechas para el registro de datos en la tabla
   			if (listaDiasLibres.get(j).intValue()<10) {
   				dia_cobmr1= "0"+listaDiasLibres.get(j).toString();
			}else {
   				dia_cobmr1= listaDiasLibres.get(j).toString();

			}
   			
   			if (listaMesLibres.get(j).intValue()<10) {
   				mes_cobmr1= "0"+listaMesLibres.get(j).toString();
			}else {
   				mes_cobmr1= listaMesLibres.get(j).toString();

			}
   					
   				fecha_evento_cobmr= listaAnioLibres.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;
  	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
	    	insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
			}
	    	
	    	
			
	    	}else if (tabMatrix.getTotalFilas()>0 && tabEmpleadoMatrizMensual1.getTotalFilas()==0) {
				
	    		//lista sin fin de semana
				ArrayList<Integer> listaDiaFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaMesFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaAnioFinSemana = new ArrayList<Integer>();
				
			
		int finSemana=0;
			for (int z = 0; z < diferencia_dias+1; z++) {
				DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, z);
				finSemana=fechaFinSemana(DiaSuma);
				//System.out.println("Fin Semana: "+finSemana);
				if (finSemana!=0) {
					utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
		    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
		    		listaDiaFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaMesFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaAnioFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
					finSemana=0;

				}  		
			}
			     	
					
			
			//Retorno el numero de dias de diferencia entre un rango de fechas
			//Lista con el dia,mes y anio seleccionado
		ArrayList<Integer> listaDiasLibresOperativo= new ArrayList<Integer>();
		ArrayList<Integer> listaMesLibresOperativo = new ArrayList<Integer>();
		ArrayList<Integer> listaAnioLibresOperativo = new ArrayList<Integer>();

	    	
			
			boolean bandInsertListaLire=false;
			int insertar=listaDiaSeleccionado.size()-1;
	    	for (int x = 0; x < listaDiaFinSemana.size(); x++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
				
					if ( (listaDiaFinSemana.get(x).toString().equals(listaDiaSeleccionado.get(j).toString())) && 
						 (listaMesFinSemana.get(x).toString().equals(listaMesSeleccionado.get(j).toString())) && 
						(listaAnioFinSemana.get(x).toString().equals(listaAnioSeleccionado.get(j).toString()) ) ) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						j=listaDiaSeleccionado.size();
						
					}else{	
					 if (j==insertar) {
						 listaDiasLibresOperativo.add(listaDiaFinSemana.get(x));
							//elimino de la lista el dia que si tenga timbrada y asignado
						 listaMesLibresOperativo.add(listaMesFinSemana.get(x));
							//elimino el mes que corresponde a esa fecha con su timbrada}
						 listaAnioLibresOperativo.add(listaAnioFinSemana.get(x));
							//j=listaDiaSeleccionado.size();
					}
						
					}
				}
			}
			

				
				
				
			   	for (int j = 0; j < listaDiasLibresOperativo.size(); j++) {
			    	
					TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioLibresOperativo.get(j).toString()+"%' ");
		   			String anioActual1=tabAnio1.getValor("detalle_geani");
		   			String dia_cobmr1="";
		   			String mes_cobmr1="";
		   			String fecha_evento_cobmr="";
		   			//Voy ceando las fechas para el registro de datos en la tabla
		   			if (listaDiasLibresOperativo.get(j).intValue()<10) {
		   				dia_cobmr1= "0"+listaDiasLibresOperativo.get(j).toString();
					}else {
		   				dia_cobmr1= listaDiasLibresOperativo.get(j).toString();

					}
		   			
		   			if (listaMesLibresOperativo.get(j).intValue()<10) {
		   				mes_cobmr1= "0"+listaMesLibresOperativo.get(j).toString();
					}else {
		   				mes_cobmr1= listaMesLibresOperativo.get(j).toString();

					}
		   					
		   				fecha_evento_cobmr= listaAnioLibresOperativo.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);

	    	
	    	
	    	
		    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
			}
	    	
	    	
			
				
	    	}//else tiene horario tipo matriz
	    	else {
	    		//lista sin fin de semana
				ArrayList<Integer> listaDiaFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaMesFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaAnioFinSemana = new ArrayList<Integer>();
				
			
		int finSemana=0;
			for (int z = 0; z < diferencia_dias+1; z++) {
				DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, z);
				finSemana=fechaFinSemana(DiaSuma);
				//System.out.println("Fin Semana: "+finSemana);
				if (finSemana!=0) {
					utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
		    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
		    		listaDiaFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaMesFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaAnioFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
					finSemana=0;

				}  		
			}
			     	
					
			
			//Retorno el numero de dias de diferencia entre un rango de fechas
			//Lista con el dia,mes y anio seleccionado
		ArrayList<Integer> listaDiasLibresOperativo= new ArrayList<Integer>();
		ArrayList<Integer> listaMesLibresOperativo = new ArrayList<Integer>();
		ArrayList<Integer> listaAnioLibresOperativo = new ArrayList<Integer>();

	    	
			
			boolean bandInsertListaLire=false;
			int insertar=listaDiaSeleccionado.size()-1;
	    	for (int x = 0; x < listaDiaFinSemana.size(); x++) {
        		//for recorre la lista de timbradas por empleado
				for (int j = 0; j < listaDiaSeleccionado.size(); j++) {
					//for recorre los dias seleccionados desde la fecha inicio hasta la fecha fin
				
					if ( (listaDiaFinSemana.get(x).toString().equals(listaDiaSeleccionado.get(j).toString())) && 
						 (listaMesFinSemana.get(x).toString().equals(listaMesSeleccionado.get(j).toString())) && 
						(listaAnioFinSemana.get(x).toString().equals(listaAnioSeleccionado.get(j).toString()) ) ) {
						//Comparacion de lista de timbradas por empleado  con los dias obtenidos desde una fech inicio hasta una fecha fin
						j=listaDiaSeleccionado.size();
						
					}else{	
					 if (j==insertar) {
						 listaDiasLibresOperativo.add(listaDiaFinSemana.get(x));
							//elimino de la lista el dia que si tenga timbrada y asignado
						 listaMesLibresOperativo.add(listaMesFinSemana.get(x));
							//elimino el mes que corresponde a esa fecha con su timbrada}
						 listaAnioLibresOperativo.add(listaAnioFinSemana.get(x));
							//j=listaDiaSeleccionado.size();
					}
    		
		}     	
				}
			}
    	
    	
    	
    	
    	
			   	for (int j = 0; j < listaDiasLibresOperativo.size(); j++) {
    	
					TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioLibresOperativo.get(j).toString()+"%' ");
		   			String anioActual1=tabAnio1.getValor("detalle_geani");
		   			String dia_cobmr1="";
		   			String mes_cobmr1="";
		   			String fecha_evento_cobmr="";
		   			//Voy ceando las fechas para el registro de datos en la tabla
		   			if (listaDiasLibresOperativo.get(j).intValue()<10) {
		   				dia_cobmr1= "0"+listaDiasLibresOperativo.get(j).toString();
					}else {
		   				dia_cobmr1= listaDiasLibresOperativo.get(j).toString();
    	
					}
    	
		   			if (listaMesLibresOperativo.get(j).intValue()<10) {
		   				mes_cobmr1= "0"+listaMesLibresOperativo.get(j).toString();
					}else {
		   				mes_cobmr1= listaMesLibresOperativo.get(j).toString();
    	
					}
		   					
		   				fecha_evento_cobmr= listaAnioLibresOperativo.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

		   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);

			    	
    	
    	
				    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
					}
				
    	
    	      	     	

	    		
	    		
			}
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
			}//For inicial
	}



//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado
public TablaGenerica getTablaResumenMarcacionesEmpleado(String strFechaIniReporte,String strFechaFinReporte){

	 TablaGenerica tab_reporte;

	   tab_reporte= utilitario.consultar("SELECT COUNT(ide_gtemp), ide_gtemp  "
	   		+ "FROM con_biometrico_marcaciones_resumen "
	   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
	   		//+ "and ide_gtemp in (147) "
	   		//+ "and ide_gtemp in (select ide_gtemp from gth_empleado emp where ide_astur=1) "
	   		// + "and ide_gtemp in (SELECT IDE_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
	   		 //+ "SELECT  ide_gtemp  "
	   		 //+ "FROM con_biometrico_marcaciones_resumen  "
	   		 //+ "where  fecha_evento_cobmr between '2018-09-01' and '2018-09-30'  "
	   		// + "group by ide_gtemp  "
	   		// + "order by ide_gtemp asc)) "
	   		+ "group by ide_gtemp  "
	   		+ "order by ide_gtemp asc");
		 return tab_reporte;	   
			}



//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado
public TablaGenerica getTablaMarcacionesEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){
	
	 TablaGenerica tab_reporte;
	 
	   tab_reporte= utilitario.consultar("SELECT ide_cobmr, fecha_evento_cobmr "
	   		+ "FROM con_biometrico_marcaciones_resumen  "
	   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   "
	   		+ "and ide_gtemp in("+IDE_GTEMP+") "
	   		//+ "and ide_gtemp in(SELECT ide_gtemp "
	   		//+ "FROM asi_horario_mes_empleado WHERE IDE_GEMES=9 AND IDE_GEANI=11 order by ide_gtemp asc) " 
  			+ "ORDER by ide_gtemp asc,fecha_evento_cobmr asc");

		 return tab_reporte;	   
}



public void marcacionesLibreSinTimbre(){


	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
    		String fechaIni=(cal_fecha_inicial.getFecha());	
    		String fechaFin=(cal_fecha_final.getFecha());	
        	//Ver si tabla ya contiene las importaciones para ese rango de fecha
        	TablaGenerica tab_novedadImportarMarcacionBruto = utilitario.consultar("select * from con_biometrico_marcaciones where fecha_evento_cobim between  "
        			+ "'"+fechaIni+"' and '"+fechaFin+"'");
    	   	if (tab_novedadImportarMarcacionBruto.getTotalFilas()<=0) {
    			utilitario.agregarMensajeInfo("No existen registro de marcaciones ","Datos no encontrados desde "+fechaIni +" hasta "+fechaFin);
    			return;
    	
    		}
    	System.out.println("INICIO PROCESO MARCACIONES");
		utilitario.getConexion().ejecutarSql("DELETE FROM  con_biometrico_marcaciones_resumen BIO  "
			+ "where fecha_evento_cobmr BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'    "
	    	+ "and horainicioband_cobmr like '%SIN TIMBRE%' and horafinband_cobmr like '%SIN TIMBRE%'");

		utilitario.getConexion().ejecutarSql("DELETE FROM  con_biometrico_marcaciones_resumen BIO  "
	 	 + "where fecha_evento_cobmr BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'    "
	     + "and horainicioband_cobmr like '%LIBRE%' and horafinband_cobmr like '%LIBRE%'");

	getSinMarcacionesEmpleado(fechaIni, fechaFin, 9, 11, empleadoSeleccionado, true);



 	
	getDiasLibresEmpleado(fechaIni, fechaFin, 9, 11);
    	marcacionesEmpleado(fechaIni, fechaFin, 9, 11);
	System.out.println("FIN PROCESO MARCACIONES");

	
		}else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		}
		
	}
	else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	}		


}




public void marcacionesEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani){

		//Variables de inicio
				int contador = 0;
				int entrada=0,almuerzo=0,salida=0;
			    int entradaNocturno=0,salidaNocturno=0;
				String fechaBiometrico="";
				//Estados de marcaciones
				boolean estadoEntrada=false,estadoTarde=false,estadoNocturno=false;
				//
				Date DiaSuma=null;

			    StringBuilder str_ide_horarios=new StringBuilder(); 
			    boolean banderaMatriz= false,banderaExtra=false;
			 	String strFechaIniReporte =fechaInicial;
			 	//metodo que devuelve una variable de tipo date en el siguiente formato yyyy-MM-dd
				String strFechaFinReporte = fechaFinal;
				//Date dateFechaInicioReporte = getFechaAsyyyyMMdd(strFechaIniReporte);
				//Date dateFechaFinReporte = getFechaAsyyyyMMdd(strFechaFinReporte);
				Date dateFechaInicioReporte = getFechaAsyyyyMMdd(fechaInicial);
				Date dateFechaFinReporte = getFechaAsyyyyMMdd(fechaFinal);
				int diferencia_dias=utilitario.getDiferenciasDeFechas(dateFechaInicioReporte,dateFechaFinReporte);

				int diaInicio=utilitario.getDia(fechaInicial);
				int diaFin=utilitario.getDia(fechaFinal);
				
				
				
				
				ArrayList<Integer> listaDiaFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaMesFinSemana = new ArrayList<Integer>();
				ArrayList<Integer> listaAnioFinSemana = new ArrayList<Integer>();
				
			
		int finSemana=0;
			for (int z = 0; z < diferencia_dias+1; z++) {
				DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, z);
				finSemana=fechaFinSemana(DiaSuma);
				//System.out.println("Fin Semana: "+finSemana);
				if (finSemana!=0) {
					utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
		    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
		    		listaDiaFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaMesFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaAnioFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
					finSemana=0;
				}
				
			}
			
			
			//lista sin fin de semana
			ArrayList<Integer> listaDiaSeleccionadoSinFinSemana = new ArrayList<Integer>();
			ArrayList<Integer> listaMesSeleccionadoSinFinSemana = new ArrayList<Integer>();
			ArrayList<Integer> listaAnioSeleccionadoSinFinSemana = new ArrayList<Integer>();
			
			for (int i = 0; i < diferencia_dias+1; i++) {
				DiaSuma=utilitario.sumarDiasFecha(dateFechaInicioReporte, i);
				finSemana=fechaFinSemana(DiaSuma);
				//System.out.println("Fin Semana: "+finSemana);
				if (finSemana==0) {
					utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma));
		    		utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma));
		    		listaDiaSeleccionadoSinFinSemana.add(utilitario.getDia(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaMesSeleccionadoSinFinSemana.add(utilitario.getMes(getFechaAsyyyyMMdd(DiaSuma)));
		    		listaAnioSeleccionadoSinFinSemana.add(utilitario.getAnio(getFechaAsyyyyMMdd(DiaSuma)));
					finSemana=0;

		    		//System.out.println("Dias Sin Fin de Semana"+listaDiaSeleccionadoSinFinSemana.get(i).toString());
				    //System.out.println("Mes Sin Fin de Semana"+listaMesSeleccionadoSinFinSemana.get(i).toString());
				   // finSemana=0;
				}  		
				
			}
			
			
			
				
				
				//Validacion obtiene el mes y anio para el reporte
				if (utilitario.getMes(fechaInicial)== utilitario.getMes(fechaInicial)) {
				if (utilitario.getAnio(fechaInicial)== utilitario.getAnio(fechaInicial)) {
					ide_gemes=utilitario.getMes(fechaInicial);
					int anio=utilitario.getAnio(fechaInicial);
				 	TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%' ");
				 	ide_geani=Integer.parseInt(tabAnioReporte.getValor("ide_geani"));
					
					}
					}
				
				 TablaGenerica tabObternerEmpleados=this.getTablaResumenSinMarcacionesEmpleado(fechaInicial,fechaFinal);
				
				 for (int i = 0; i < tabObternerEmpleados.getTotalFilas(); i++) {
					//    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr);  		
				
				    	/////////////////////////////////////////////////AQUI DEBO VALIDAR CUANDO OTRO MES
				    	//Debo realizar
				    	TablaGenerica tabMatrix=utilitario.consultar("select IDE_GTEMP,IDE_ASTUR from gth_empleado where ide_gtemp ="+tabObternerEmpleados.getValor(i,"IDE_GTEMP")+" and ide_astur=1");
				    			//+ "and activo_gtemp=true");
				    	TablaGenerica tabEmpleadoMatrizMensual1 = utilitario.consultar("select IDE_GTEMP,IDE_ASHME from asi_horario_mes_empleado where ide_gtemp="+tabObternerEmpleados.getValor(i,"IDE_GTEMP")+"  "
								+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" ");
				    	System.out.println("empleado"+tabObternerEmpleados.getValor(i,"IDE_GTEMP"));
				    	
				   if (tabMatrix.getTotalFilas()==0 && tabEmpleadoMatrizMensual1.getTotalFilas()>0) {
				 
						   for (int j = 0; j < listaDiaSeleccionadoSinFinSemana.size(); j++) {
				    		TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+listaAnioSeleccionadoSinFinSemana.get(j).toString()+"%' ");
				   			String anioActual=tabAnio.getValor("ide_geani");
				    		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+tabObternerEmpleados.getValor(i, "IDE_GTEMP")+"  "
				    				+ "and ide_gemes="+listaMesSeleccionadoSinFinSemana.get(j).toString()+" and ide_geani="+anioActual);

					String diaMatrizMensual="";
					


					diaMatrizMensual=tabEmpleadoMatrizMensual.getValor("DIA"+listaDiaSeleccionadoSinFinSemana.get(j).toString());
					
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
				   			if (tiempoAlmEmpl.equals("0") || tiempoAlmEmpl.equals("0.0")) {
				   				horaFinAlmEmpl="S/A";
				   				horaIniAlmEmpl="S/A";
				   				hora1="S/A";
				   				hora2="S/A";
							}

				   			//Validacion del dia que contenga 
				   			
				   			
				   			TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionadoSinFinSemana.get(j).toString()+"%' ");
				   			String anioActual1=tabAnio1.getValor("detalle_geani");
				   			String dia_cobmr1="";
				   			String mes_cobmr1="";
				   			String fecha_evento_cobmr="";
				   			//Voy ceando las fechas para el registro de datos en la tabla
				   			if (listaDiaSeleccionadoSinFinSemana.get(j).intValue()<10) {
				   				dia_cobmr1= "0"+listaDiaSeleccionadoSinFinSemana.get(j).toString();
							}else {
				   				dia_cobmr1= listaDiaSeleccionadoSinFinSemana.get(j).toString();

							}
				   
				   			if (listaMesSeleccionadoSinFinSemana.get(j).intValue()<10) {
				   				mes_cobmr1= "0"+listaMesSeleccionadoSinFinSemana.get(j).toString();
							}else {
				   				mes_cobmr1= listaMesSeleccionadoSinFinSemana.get(j).toString();

							}
				   					
				   				fecha_evento_cobmr= listaAnioSeleccionadoSinFinSemana.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

							
				   			
				   			
				   			
				  			
				  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
				  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
				  			//Validacion fecha feriado
				  			 if (banderaFeriados==true) {
								//Si se tiene feriado ese dia no se ingrese
							}else if(getEmpleadoActivo(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")))==false){
								
							}
				  			 
				  			 
				  			 else {
								 //Si no es un dia fericado que se ingrese como no timbrada
								 insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr,"","","","");  		
						         }
				  			
				    	}
				    	
					
				    	
					   
					   
	   	
				   	
				   	
					 
				   	for (int j = 0; j < listaDiaFinSemana.size(); j++) {
				    	
						TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioFinSemana.get(j).toString()+"%' ");
			   			String anioActual1=tabAnio1.getValor("detalle_geani");
			   			String dia_cobmr1="";
			   			String mes_cobmr1="";
			   			String fecha_evento_cobmr="";
			   			//Voy ceando las fechas para el registro de datos en la tabla
			   			if (listaDiaFinSemana.get(j).intValue()<10) {
			   				dia_cobmr1= "0"+listaDiaFinSemana.get(j).toString();
						}else {
			   				dia_cobmr1= listaDiaFinSemana.get(j).toString();

						}
			   			
			   			if (listaMesFinSemana.get(j).intValue()<10) {
			   				mes_cobmr1= "0"+listaMesFinSemana.get(j).toString();
						}else {
			   				mes_cobmr1= listaMesFinSemana.get(j).toString();

						}
			   					
			   				fecha_evento_cobmr= listaAnioFinSemana.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

			   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
					    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
						}
				   	
				   }else if (tabMatrix.getTotalFilas()>0 && tabEmpleadoMatrizMensual1.getTotalFilas()==0){
					
					   
					   
					   
				       	//Recorro la lista con el numero de dias sin timbrar
			        	for (int x = 0; x < listaDiaSeleccionadoSinFinSemana.size(); x++) {
			    		TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionadoSinFinSemana.get(x).toString()+"%' ");
			   			String anioActual1=tabAnio1.getValor("detalle_geani");
			   			String dia_cobmr1="";
			   			String mes_cobmr1="";
			   			String fecha_evento_cobmr="";
			   			//Voy ceando las fechas para el registro de datos en la tabla
			   			if (listaDiaSeleccionadoSinFinSemana.get(x).intValue()<10) {
			   				dia_cobmr1= "0"+listaDiaSeleccionadoSinFinSemana.get(x).toString();
						}else {
			   				dia_cobmr1= listaDiaSeleccionadoSinFinSemana.get(x).toString();

						}
			   			
			   			if (listaMesSeleccionadoSinFinSemana.get(x).intValue()<10) {
			   				mes_cobmr1= "0"+listaMesSeleccionadoSinFinSemana.get(x).toString();
						}else {
			   				mes_cobmr1= listaMesSeleccionadoSinFinSemana.get(x).toString();

						}
			   			//Creo la fecha sin marcacion
			   				fecha_evento_cobmr= listaAnioSeleccionadoSinFinSemana.get(x).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;
			        	
			        
						//	 insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		

			  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
			  			 banderaFeriados=getFeriado(fecha_evento_cobmr);
			  			//Validacion fecha feriado
			  			 if (banderaFeriados==true) {
							//Si se tiene feriado ese dia no se ingrese
						}//else if(getEmpleadoActivo(empleado)==false){
							
						//}
			  			 
			  			 
			  			 else {
			  				 
			  				Calendar calFechaDia = Calendar.getInstance();
			   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
			   	     				calFechaDia.setTime(getFechaAsyyyyMMdd(fecha_evento_cobmr)); 
			   	     				

			  				 //Obtengo el dia de la semana a que corresponde esa fecha
			  				 
			   	     		int diaSemana = calFechaDia.get(Calendar.DAY_OF_WEEK);
			  				 
			  		    	//Metodo devuelve el horario asignado a cada empleado
			   			       	TablaGenerica diasXHorario=utilitario.consultar("select EMP.IDE_GTEMP,  "
			   			       			+ "horario.ide_ashor, "
			   			       			+ "DIA.IDE_GEDIA,DIA.DETALLE_GEDIA, "
			   			       			+ "HORARIO.HORA_INICIAL_ASHOR, "
			   			       			+ "HORARIO.HORA_FINAL_ASHOR, "
			   			       			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
			   			       			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
			   			       			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
			   			       			+ "HORARIO.NOM_ASHOR "
			   			       			+ "from gth_empleado emp "
			   			       			+ "left join asi_turnos_horario turnos on turnos.ide_astur=emp.ide_astur "
			   			       			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor  "
			   			       			+ "left join asi_dia_horario diahor on diahor.ide_ashor=horario.ide_ashor "
			   			       			+ "LEFT JOIN GEN_DIAS DIA ON DIA.IDE_GEDIA=DIAHOR.IDE_GEDIA "
			   			       			+ "where emp.ide_astur is not null  AND EMP.IDE_GTEMP="+tabObternerEmpleados.getValor(i, "IDE_GTEMP")+" and "
			   			       			+ "dia.ide_gedia="+(diaSemana-1)+" "
			   			       			+ "ORDER BY HORARIO.IDE_ASHOR ASC");
			   			    	
			  				 
			  				 
							 //Si no es un dia fericado que se ingrese como no timbrada
							 //insertarTablaResumen(empleado, fecha_evento_cobmr, horaInicioEmpl, "", "SIN TIMBRE", horaIniAlmEmpl, horaFinAlmEmpl, hora1,hora2, 0, tiempoAlmEmpl, horaFinEmpl, "", "SIN TIMBRE", false, dia_cobmr);  		
					         
						 insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, diasXHorario.getValor("HORA_INICIAL_ASHOR"), "", "SIN TIMBRE", diasXHorario.getValor("HORA_INICIO_ALMUERZO_ASHOR"),
						 diasXHorario.getValor("HORA_FIN_ALMUERZO_ASHOR"), "","", 0.0,diasXHorario.getValor("MIN_ALMUERZO_ASHOR") ,diasXHorario.getValor("HORA_FINAL_ASHOR") , "", "SIN TIMBRE", false, dia_cobmr,"","","","");  		
			  			 	}
			        	}
			        	
			        	
			        	
					   	for (int j = 0; j < listaDiaFinSemana.size(); j++) {
					    	
							TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioFinSemana.get(j).toString()+"%' ");
				   			String anioActual1=tabAnio1.getValor("detalle_geani");
				   			String dia_cobmr1="";
				   			String mes_cobmr1="";
				   			String fecha_evento_cobmr="";
				   			//Voy ceando las fechas para el registro de datos en la tabla
				   			if (listaDiaFinSemana.get(j).intValue()<10) {
				   				dia_cobmr1= "0"+listaDiaFinSemana.get(j).toString();
							}else {
				   				dia_cobmr1= listaDiaFinSemana.get(j).toString();

							}
				   			
				   			if (listaMesFinSemana.get(j).intValue()<10) {
				   				mes_cobmr1= "0"+listaMesFinSemana.get(j).toString();
							}else {
				   				mes_cobmr1= listaMesFinSemana.get(j).toString();

							}
				   					
				   				fecha_evento_cobmr= listaAnioFinSemana.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

				   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
						    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
							}
					   
				}
				   else {
					
					   
					   
					   	for (int j = 0; j < listaDiaSeleccionadoSinFinSemana.size(); j++) {
					    	
							TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioSeleccionadoSinFinSemana.get(j).toString()+"%' ");
				   			String anioActual1=tabAnio1.getValor("detalle_geani");
				   			String dia_cobmr1="";
				   			String mes_cobmr1="";
				   			String fecha_evento_cobmr="";
				   			//Voy ceando las fechas para el registro de datos en la tabla
				   			if (listaDiaSeleccionadoSinFinSemana.get(j).intValue()<10) {
				   				dia_cobmr1= "0"+listaDiaSeleccionadoSinFinSemana.get(j).toString();
							}else {
				   				dia_cobmr1= listaDiaSeleccionadoSinFinSemana.get(j).toString();

							}
				   			
				   			if (listaMesSeleccionadoSinFinSemana.get(j).intValue()<10) {
				   				mes_cobmr1= "0"+listaMesSeleccionadoSinFinSemana.get(j).toString();
							}else {
				   				mes_cobmr1= listaMesSeleccionadoSinFinSemana.get(j).toString();

							}
				   					
				   				fecha_evento_cobmr= listaAnioSeleccionadoSinFinSemana.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

				   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);

					    	
					    	
					    	
						    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");  		
							}
					   	
					   	
					   	
					   	
					   	
					   	
					   	
					   	
						 
					   	for (int j = 0; j < listaDiaFinSemana.size(); j++) {
					    	
							TablaGenerica  tabAnio1=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='%"+listaAnioFinSemana.get(j).toString()+"%' ");
				   			String anioActual1=tabAnio1.getValor("detalle_geani");
				   			String dia_cobmr1="";
				   			String mes_cobmr1="";
				   			String fecha_evento_cobmr="";
				   			//Voy ceando las fechas para el registro de datos en la tabla
				   			if (listaDiaFinSemana.get(j).intValue()<10) {
				   				dia_cobmr1= "0"+listaDiaFinSemana.get(j).toString();
							}else {
				   				dia_cobmr1= listaDiaFinSemana.get(j).toString();

							}
				   			
				   			if (listaMesFinSemana.get(j).intValue()<10) {
				   				mes_cobmr1= "0"+listaMesFinSemana.get(j).toString();
							}else {
				   				mes_cobmr1= listaMesFinSemana.get(j).toString();

							}
				   					
				   				fecha_evento_cobmr= listaAnioFinSemana.get(j).toString()+"-"+mes_cobmr1+"-"+dia_cobmr1;

				   	  			String dia_cobmr=diaSemana(fecha_evento_cobmr);
						    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr,"","","","");  		
							}
					   
					   
					   
					   
}







				   	
		}  		
				
				
				
		     				
				 
	
	
	
}




public  TablaGenerica getTablaResumenSinMarcacionesEmpleado(String fechaInicial, String fechaFinal){
	
	 TablaGenerica tab_reporte;
	 
	   tab_reporte= utilitario.consultar("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
	   		 + "SELECT  ide_gtemp  "
	   		 + "FROM con_biometrico_marcaciones_resumen  "
	   		 + "where  fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"'  "
	   		 + "group by ide_gtemp  "
	   		 + "order by ide_gtemp asc)");

		 return tab_reporte;
	
	
	}


public void getMarcacionesAlmuerzo(String fechaIni,String fechaFin){
//Variables
	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='', horafinalmbio_cobmr='' "
			 + "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and (horainicioalmbio_cobmr like '%SIN TIMBRE%' or horafinalmbio_cobmr='%SIN TIMBRE%') ");
			   	
	String horainicioalm_cobmr="",horainicioalmbio_cobmr="",horafinalmbio_cobmr="";
	 
	TablaGenerica tab_almuerzo=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
		+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
		+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
		+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
		+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
		+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
		+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
		+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr "
		+ "FROM con_biometrico_marcaciones_resumen "
		+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
				//+ "and ide_gtemp=13 "
		+ "order by ide_gtemp asc ,fecha_evento_cobmr  asc");

for (int i = 0; i < tab_almuerzo.getTotalFilas(); i++) {
	horainicioalm_cobmr=tab_almuerzo.getValor(i,"horainicioalm_cobmr");
	horainicioalmbio_cobmr=tab_almuerzo.getValor(i,"horainicioalmbio_cobmr");
	horafinalmbio_cobmr=tab_almuerzo.getValor(i,"horafinalmbio_cobmr");
	
	if (horainicioalm_cobmr.equals("S/A") || horainicioalm_cobmr.equals("") || horainicioalm_cobmr.isEmpty() || horainicioalm_cobmr==null) {
		//Si no existe marcacion en salida al almuerzo
	}else {
		if (horainicioalmbio_cobmr==null || horainicioalmbio_cobmr.equals("") || horainicioalmbio_cobmr.isEmpty() ) {
			if (horafinalmbio_cobmr==null || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.isEmpty() ) {
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='SIN TIMBRE', horafinalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));

		}else {
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));
			
		}
	}else {
		if (horafinalmbio_cobmr==null || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.isEmpty() ) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  horafinalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));
	
	}
	}

	}
	

}}


public void getMarcacionesFeriado(String fecInicia, String fecFinal){
	TablaGenerica tabDiasFeriados=utilitario.consultar("SELECT ide_asnov, ide_usua, fecha_inicio_asnov, fecha_fin_asnov, observacion_asnov,  "
			+ "activo_asnov, fecha_asnov, importacion_asnov, dia_feriado_asnov, "
			+ "cambio_asnov, ide_astur "
			+ "FROM asi_novedad where dia_feriado_asnov=true "
			+ "order by ide_asnov asc");

for (int i = 0; i < tabDiasFeriados.getTotalFilas(); i++) {
	
}


}


public String formatearHora(String hora){
	String horaNueva="",horaTemp="",minutoTemp="",segundosTemp="";
	String[] parts = hora.split(":");
	String part1 = parts[0]; // 123
	String part2 = parts[1]; // -654321
	String part3 = parts[2]; // -654321
	
	if (Integer.parseInt(part1)<10) {
			horaTemp="0"+part1;
    		}else {
			horaTemp=""+part1;
    		}
		
	if (Integer.parseInt(part2)<10) {
		minutoTemp="0"+part2;
		}else {
		minutoTemp=""+part1;
		}

	
	if (Integer.parseInt(part3)<10) {
		segundosTemp="0"+part3;
		}else {
		segundosTemp=""+part3;
		}

			horaNueva=horaTemp+":"+minutoTemp+":"+segundosTemp;
	
	return horaNueva;
}





	public void empleadoSinMarcacion(){
		
		try {
			TablaGenerica tabActivoSinMarcacion=utilitario.consultar("select ide_gtemp,activo_gtemp,ide_astur from gth_empleado where activo_gtemp = true  "
					+ "and ide_gtemp not in (SELECT ide_gtemp  "
					+ "biometrico_salida_cobmr   "
					+ "FROM con_biometrico_marcaciones_resumen  "
					+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"')");
			

			TablaGenerica tablaMarcaciones=utilitario.consultar("SELECT ide_gtemp,ide_gtemp as uno  "
					+ "FROM con_biometrico_marcaciones_resumen  "
					+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
			
			if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
				if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
				}else {
					//Si las fechas o son validas
				utilitario.agregarMensajeInfo("Fechas inválidas ","No se puede procesar transacción" );
			   return;
				}	
			
		}else {
			//Si las fechas de entrada y salida aun no han sido seleciionadp
			utilitario.agregarMensajeInfo("Revisar Fecha Inicio y Fin ","Debe seleccionar una fecha válida" );
			return;
		}
			
			
			if ((tablaMarcaciones.getTotalFilas()==0) || tablaMarcaciones.isEmpty()) {
				utilitario.agregarMensajeInfo("	No existe importación de marcaciones ","Debe importar marcaciones" );	
				return;
			}
			
			
			
			if (tabActivoSinMarcacion.getTotalFilas()>0) {
			//Si existen empleados sin marcaciones 	
		
				
				TablaGenerica tablaMarcacionesTope=utilitario.consultar("SELECT ide_gtemp,"
						+ "fecha_evento_cobmr  "
						+ "FROM con_biometrico_marcaciones_resumen  "
						+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
						+ "order by fecha_evento_cobmr desc "
						+ "limit 1");
			
				
				
				
			    		String fechaIni=(cal_fecha_inicial.getFecha());	
			    		String fechaFin=(tablaMarcacionesTope.getValor("fecha_evento_cobmr"));
						int diferencia_dias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fechaIni),utilitario.DeStringADate(fechaFin));
				
						Date DiaSuma=null;
						int  ide_gemes=0,anio=0;				
					    String  fecha_evento_cobmr="";
			  			String dia_cobmr="";
			  			int ide_geani=0;
			  			String ide_astur="";
			  			String empleado="";
					 
				    	
				    	  			
			
			  	for (int i = 0; i < tabActivoSinMarcacion.getTotalFilas(); i++) {
			  		ide_astur=tabActivoSinMarcacion.getValor(i,"IDE_ASTUR");
			  		empleado=tabActivoSinMarcacion.getValor(i,"IDE_GTEMP");		  		
					for (int j = 0; j < (diferencia_dias+1); j++) {
						
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  	  				
						DiaSuma=utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaIni), j);
						fecha_evento_cobmr= utilitario.DeDateAString(DiaSuma);
						dia_cobmr=diaSemana(fecha_evento_cobmr);
						ide_gemes=utilitario.getMes(fecha_evento_cobmr) ;
						
						
						
						
						if (ide_astur==null ||  ide_astur.equals("") || ide_astur.isEmpty()) {
						//Si contiene turno diario	
			  			TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_evento_cobmr)+"%' ");
					 	ide_geani=Integer.parseInt(tabAnioReporte.getValor("ide_geani"));		
						TablaGenerica tabEmpleadoMatrizMensual1 = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
								+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" and dia"+(i+1)+" is not null ");

						
						if (tabEmpleadoMatrizMensual1.getTotalFilas()>0) {
							//Si contiene asignado un horario mensual 
				    		TablaGenerica  tabAnioReporteMensual=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
									+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
									+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
									+ "FROM asi_turnos_horario astuh  "
									+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
									+ "where astuh.ide_astur="+tabEmpleadoMatrizMensual1.getValor("dia"+(i+1))+" "
									+ "order by ide_astuh asc");	
				    		
				    		
				    	if (tabAnioReporteMensual.getTotalFilas()>0) {
				    		//Contiene turno para el dia 
				    		insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporteMensual.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporteMensual.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporteMensual.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporteMensual.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","","");
							
						}else {
							//No contiene turno asignado en ese dia
							insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");	
							}	
						}else{
						//Si no contiene turno asignado a ese dia 
							insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");	
						}
						
						
						}else {
						//Si contiene turno anual
							
						    //Hora desde Inicio de Entrada de Horario
	             			     Calendar calHoraFechaBase= Calendar.getInstance();
	           	    		     calHoraFechaBase.setTime(DiaSuma);
	           	    		     
	           	    		     
					//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");    
					//		Calendar cini = Calendar.getInstance();
					//               cini.setTime(formatter.parse(utilitario.DeDateAString(DiaSuma)));
					      if (calHoraFechaBase.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
					    		TablaGenerica  tabAnioReporte=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
										+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
										+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
										+ "FROM asi_turnos_horario astuh  "
										+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
										+ "where astuh.ide_astur=1 and horario.ide_ashor=2 "
										+ "order by ide_astuh asc");
								
					    	  insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporte.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporte.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporte.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporte.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","","");  										
						
					      }else if(calHoraFechaBase.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
								insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");				    	  
					      }else			      
					      {
					    		TablaGenerica  tabAnioReporte=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
										+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
										+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
										+ "FROM asi_turnos_horario astuh  "
										+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
										+ "where astuh.ide_astur=1 and horario.ide_ashor=3 "
										+ "order by ide_astuh asc");
							
							insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporte.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporte.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporte.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporte.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","",""); 	
						}				      
					}//fin if si contiene turno anual
					
				}//Primer for
					
			  	}//Segundo for	
				
			
			
			
			}else {
			//Si no  empleados sin marcaciones 	
				utilitario.agregarMensajeInfo("Ya han sido procesadas todas las marcaciones","No existen empleado(s) para importar en las fechas seleccionadas");
				return;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR NUMERO");		
		} }		
		



}
