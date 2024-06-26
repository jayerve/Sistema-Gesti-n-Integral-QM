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
public class pre_biometrico_marcaciones_new extends Pantalla {

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
    String empleadoSeleccionado="",listEmpleadoHorarioAnual="",listEmpleadoHorarioMensual="";

    public pre_biometrico_marcaciones_new() {
    	
    	
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
    //	Boton bot_eliminar_sin_timbre= new Boton();
    //	bot_eliminar_sin_timbre.setIcon("ui-icon-calculator");
    //	bot_eliminar_sin_timbre.setMetodo("eliminarMarcaciones");
    //	bot_eliminar_sin_timbre.setValue("Eliminar Marcaciones");
    //	bot_eliminar_sin_timbre.setTitle("Eliminar Marcaciones");
    //	bar_botones.agregarBoton(bot_eliminar_sin_timbre);
    	
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);    	
    	
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());
		String fecha_inicio_asnov= fechaIni;
		String fecha_fin_asnov= fechaFin;
		
		
		int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
    	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
	    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
    	
String sql1="select ide_gtemp,ide_astur  "
				+ "from gth_empleado "
				+ "where ide_gtemp in(select par.ide_gtemp "
				+ "from gen_empleados_departamento_par par "
				+ "where par.activo_geedp=true "
				+ "group by par.ide_gtemp "
				+ "UNION all  "
				+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2 "
				+ "where par2.fecha_finctr_geedp between '"+fechaIni+"' and '"+fechaFin+"' "
				+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true "
				+ "group by ide_gtemp ) "
				+ "group by par2.ide_gtemp "
				+ "order by ide_gtemp ) "
				+ "and ide_astur is not null "
				+ "order by ide_astur asc,ide_gtemp asc";
		
		TablaGenerica tab_empleados_horario_matriz=utilitario.consultar(sql1);
		StringBuilder str_ide_empleado = new StringBuilder();

		if (tab_empleados_horario_matriz.getTotalFilas()>0) {
	String int_num_col_idegetemp="";
			 for (int i = 0; i < tab_empleados_horario_matriz.getTotalFilas(); i++) {
					int_num_col_idegetemp=tab_empleados_horario_matriz.getValor(i, "IDE_GTEMP");
	          	  	if(str_ide_empleado.toString().isEmpty()==false){
	                          str_ide_empleado.append(",");
	                  }
	                  str_ide_empleado.append(int_num_col_idegetemp);
	          }
				
		}else {
			utilitario.agregarMensaje("No se puede realizar esta accion", "Fechas invalidas");
			return;
		}
		
		
		int estado=0;
		
		String sql_turnoMensual="";
			

		sql_turnoMensual="SELECT ide_gtemp, ide_gemes "
		+ "FROM asi_horario_mes_empleado "
		+ "where ide_geani="+anio+" and ide_gemes="+utilitario.getMes(fechaIni)+" and ide_gtemp in( "
		+ "select ide_gtemp   "
		+ "from gth_empleado  "
		+ "where ide_gtemp in(select par.ide_gtemp  "
		+ "from gen_empleados_departamento_par par  "
		+ "where par.activo_geedp=true  "
		+ "group by par.ide_gtemp  "
		+ "UNION all  "
		+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2  "
		+ "where par2.fecha_finctr_geedp between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'  "
		+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true  "
		+ "group by ide_gtemp )  "
		+ "group by par2.ide_gtemp  "
		+ "order by ide_gtemp )  "
		+ "and ide_astur is null  "
		+ "order by ide_astur asc,ide_gtemp asc) "
		+ "group by  ide_gtemp,ide_gemes "
		+ "order by ide_gtemp asc";
		
		String empleados="";
		TablaGenerica tab_empleados_horario_mensual=utilitario.consultar(sql_turnoMensual);
		StringBuilder str_ide_empleado_mensual = new StringBuilder();

		if (tab_empleados_horario_mensual.getTotalFilas()>0) {
	String int_num_col_idegetemp="";
			 for (int i = 0; i < tab_empleados_horario_mensual.getTotalFilas(); i++) {
					int_num_col_idegetemp=tab_empleados_horario_mensual.getValor(i, "IDE_GTEMP");
	          	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
	          	  	str_ide_empleado_mensual.append(",");
	                  }
	          	  str_ide_empleado_mensual.append(int_num_col_idegetemp);
	          }
			estado=1;	
		}else {
			estado=0;
	    	set_empleado.setId("set_empleado");
	    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
					" AS NOMBRES_APELLIDOS "
					+ "FROM GTH_EMPLEADO EMP "
					+ "where ide_gtemp in("+str_ide_empleado+") " +
					" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
			set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
			set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
	    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
			gru_pantalla.getChildren().add(set_empleado);
			set_empleado.getBot_aceptar().setMetodo("getEmpleado");
			agregarComponente(set_empleado);			
			
			
		}
		System.out.println(str_ide_empleado+"     "+str_ide_empleado_mensual);
		

		if (estado==1) {
			
		
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS "
				+ "FROM GTH_EMPLEADO EMP "
				+ "where ide_gtemp in("+str_ide_empleado+","+str_ide_empleado_mensual+") " +
				" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("getEmpleado");
		agregarComponente(set_empleado);			
		
		}
	
	

		
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
        
        tab_resumen_marcaciones.getColumna("horas25_loep_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("horas60_loep_cobmr").setVisible(false);  
        tab_resumen_marcaciones.getColumna("horas50_ct_cobmr").setVisible(false);  

        

        tab_resumen_marcaciones.setCondicion("ide_cobmr=-1");  
		tab_resumen_marcaciones.setCampoOrden("tipo_horario_cobmr ASC,IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");

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
		tab_resumen_marcaciones.setCampoOrden("tipo_horario_cobmr ASC,IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
		tab_resumen_marcaciones.ejecutarSql();
		utilitario.addUpdate("tab_novedad,tab_resumen_marcaciones");
	   
    }
   
    
    
    
    public void importarMarcaciones(){
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());	

    	/*if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
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
    	banderaFin=false;
    	banderaInicio=false;
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
    /*	if (tab_novedadImportarMarcacionExistentes.getTotalFilas()>0) {
			utilitario.agregarMensajeInfo("Marcaciones importadas para este rango de fechas","Datos importados Desde "+fechaIni +" hasta "+fechaFin);
			return;

		}
    	//si no existen
    	else {*/
			
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
		     
		    //Crear Plantilla
		    //
		    crearPlantillaHorario(fecha_inicio_asnov,fecha_fin_asnov,"");
		    
//		    getMarcacionesEmpleado(fechaIni,fechaFin,mes,anio,"",true);
   			//getMarcacionesAlmuerzo(fechaIni,fechaFin,"");
   			
   			
			//tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ");
			//tab_resumen_marcaciones.ejecutarSql();

        	//insertarTablaNovedad(ide_usua, fecha_inicio_asnov, fecha_fin_asnov, observacion_asnov, true, fecha_asnov,true,false);
			//tab_novedad.ejecutarSql();

    			
			/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
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


				
				getSinMarcacionesEmpleado(fechaIni, fechaFin, 9, 11, "", true);
				getDiasLibresEmpleado(fechaIni, fechaFin, 9, 11,"");
			  	marcacionesEmpleado(fechaIni, fechaFin, 9, 11,"");
				
				**/		
				
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
		
    	//   }

    		//}else {
			//utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		//}
		
//	}
	//else {
	//	utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	//}		

    		
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
         
        			
        			String fechaIni=(cal_fecha_inicial.getFecha());	
        			String fechaFin=(cal_fecha_final.getFecha());
        			String fecha_inicio_asnov= fechaIni;
        			String fecha_fin_asnov= fechaFin;
        			
        			
        			int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
        	    	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
        		    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
        	    	
        	String sql1="select ide_gtemp,ide_astur  "
        					+ "from gth_empleado "
        					+ "where ide_gtemp in(select par.ide_gtemp "
        					+ "from gen_empleados_departamento_par par "
        					+ "where par.activo_geedp=true "
        					+ "group by par.ide_gtemp "
        					+ "UNION all  "
        					+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2 "
        					+ "where par2.fecha_finctr_geedp between '"+fechaIni+"' and '"+fechaFin+"' "
        					+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true "
        					+ "group by ide_gtemp ) "
        					+ "group by par2.ide_gtemp "
        					+ "order by ide_gtemp ) "
        					+ "and ide_astur is not null "
        					+ "order by ide_astur asc,ide_gtemp asc";
        			
        			TablaGenerica tab_empleados_horario_matriz=utilitario.consultar(sql1);
        			StringBuilder str_ide_empleado = new StringBuilder();

        			if (tab_empleados_horario_matriz.getTotalFilas()>0) {
        		String int_num_col_idegetemp="";
        				 for (int i = 0; i < tab_empleados_horario_matriz.getTotalFilas(); i++) {
        						int_num_col_idegetemp=tab_empleados_horario_matriz.getValor(i, "IDE_GTEMP");
        		          	  	if(str_ide_empleado.toString().isEmpty()==false){
        		                          str_ide_empleado.append(",");
        		                  }
        		                  str_ide_empleado.append(int_num_col_idegetemp);
        		          }
        					
        			}else {
        				utilitario.agregarMensaje("No se puede realizar esta accion", "Fechas invalidas");
        				return;
        			}
        			
        			
        			int estado=0;
        			
        			String sql_turnoMensual="";
        				

        			sql_turnoMensual="SELECT ide_gtemp, ide_gemes "
        			+ "FROM asi_horario_mes_empleado "
        			+ "where ide_geani="+anio+" and ide_gemes="+utilitario.getMes(fechaIni)+" and ide_gtemp in( "
        			+ "select ide_gtemp   "
        			+ "from gth_empleado  "
        			+ "where ide_gtemp in(select par.ide_gtemp  "
        			+ "from gen_empleados_departamento_par par  "
        			+ "where par.activo_geedp=true  "
        			+ "group by par.ide_gtemp  "
        			+ "UNION all  "
        			+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2  "
        			+ "where par2.fecha_finctr_geedp between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'  "
        			+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true  "
        			+ "group by ide_gtemp )  "
        			+ "group by par2.ide_gtemp  "
        			+ "order by ide_gtemp )  "
        			+ "and ide_astur is null  "
        			+ "order by ide_astur asc,ide_gtemp asc) "
        			+ "group by  ide_gtemp,ide_gemes "
        			+ "order by ide_gtemp asc";
        			
        			String empleados="";
        			TablaGenerica tab_empleados_horario_mensual=utilitario.consultar(sql_turnoMensual);
        			StringBuilder str_ide_empleado_mensual = new StringBuilder();

        			if (tab_empleados_horario_mensual.getTotalFilas()>0) {
        		String int_num_col_idegetemp="";
        				 for (int i = 0; i < tab_empleados_horario_mensual.getTotalFilas(); i++) {
        						int_num_col_idegetemp=tab_empleados_horario_mensual.getValor(i, "IDE_GTEMP");
        		          	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
        		          	  	str_ide_empleado_mensual.append(",");
        		                  }
        		          	  str_ide_empleado_mensual.append(int_num_col_idegetemp);
        		          }
        				estado=1;	
        			}else {
        				estado=0;
        		    	set_empleado.setId("set_empleado");
        		    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
        						"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
        						" AS NOMBRES_APELLIDOS "
        						+ "FROM GTH_EMPLEADO EMP "
        						+ "where ide_gtemp in("+str_ide_empleado+") " +
        						" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
        				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
        				set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
        		    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
        				gru_pantalla.getChildren().add(set_empleado);
        				set_empleado.getBot_aceptar().setMetodo("getEmpleado");
        				set_empleado.redibujar();		
        				
        				
        			}
        			System.out.println(str_ide_empleado+"     "+str_ide_empleado_mensual);
        			

        			if (estado==1) {
        				
        			
        	    	set_empleado.setId("set_empleado");
        	    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
        					"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
        					" AS NOMBRES_APELLIDOS "
        					+ "FROM GTH_EMPLEADO EMP "
        					+ "where ide_gtemp in("+str_ide_empleado+","+str_ide_empleado_mensual+") " +
        					" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
        			set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
        			set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
        	    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
        			gru_pantalla.getChildren().add(set_empleado);
        			set_empleado.getBot_aceptar().setMetodo("getEmpleado");
        			set_empleado.redibujar();			
        			
        			}
        			
        			
        			
        			
        			//set_empleado.dibujar();
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
	 
		utilitario.getConexion().ejecutarSql("delete from con_biometrico_marcaciones_resumen "
				+ "where ide_gtemp in ("+set_empleado.getSeleccionados()+") "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");

crearPlantillaHorario(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha(),set_empleado.getSeleccionados());
 //       getMarcacionesEmpleado(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),0,0,set_empleado.getSeleccionados(),false);
//	    getMarcacionesAlmuerzo(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),set_empleado.getSeleccionados());
	    
	/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ "recargonocturno25_cobmr=null, recargonocturno100_cobmr=null, horafinextra_cobmr=null, novedad_cobmr=false "
				+ "where fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
		
		
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%OK%' "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+") and novedad_cobmr =false");

				
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ " novedad_cobmr =true  where horainicioband_cobmr like '%ATRASADO%' and horafinband_cobmr like '%OK%' "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+") and novedad_cobmr =false");


		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%ANTICIPADO%' "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+") and novedad_cobmr =false");


		///EXTRA Y FERIADO
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ " novedad_cobmr =true  where horainicioband_cobmr like '%EXTRA%' and horafinband_cobmr like '%EXTRA%' "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+") and novedad_cobmr =false");

		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
				+ "horainicioband_cobmr ='SIN TIMBRE'  where (horainicioband_cobmr ='' OR horainicioband_cobmr is null)  and horainiciohorario_cobmr is not null  "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+") ") ;

		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ "horafinband_cobmr ='SIN TIMBRE'  where (horafinband_cobmr ='' OR horafinband_cobmr is null)  and horafinhorario_cobmr is not null  "
				+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+set_empleado.getSeleccionados()+")");

	    
		getSinMarcacionesEmpleado(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha(), 9, 11, set_empleado.getSeleccionados(), true);
		getDiasLibresEmpleado(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha(), 9, 11,set_empleado.getSeleccionados());
	 // 	marcacionesEmpleado(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha(), 9, 11,set_empleado.getSeleccionados());
*/
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
    

    private Date getFechaAsyyyyMMddHHmm(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

    
    private String getFechaAsyyyyMMddHHmm(Date fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    	/*TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
    				+ "EMP.IDE_GTEMP,"
        			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
        			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM,RELOJ.DETALLE_COREL  "
        			+ "from gth_empleado emp  "
        			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
        			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
        			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
        			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
        			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");*/    	
    	//tabObteberTimbreXEmpleado.imprimirSql();
    	
    	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select a.IDE_GTEMP, a.FECHAM, a.HORAM,a.EVENTO_RELOJ_COBIM,a.DETALLE_COREL  "
    			+ "from (  "
    			+ "select  "
    			+ "  EMP.IDE_GTEMP, "
    			+ "  TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM, "
    			+ "	 TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') AS HORAM,BIO.EVENTO_RELOJ_COBIM,RELOJ.DETALLE_COREL  "
    			+ "  from gth_empleado emp  "
    			+ "  LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
    			+ "	 INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  "
    			+ "	 where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
    			+ "	 AND EMP.IDE_GTEMP="+IDE_GTEM+" AND RELOJ.IDE_COREL NOT IN(11,12,14,16,13,15)  "
    			+ "	 group by EMP.IDE_GTEMP,FECHAM,HORAM,BIO.EVENTO_RELOJ_COBIM,RELOJ.DETALLE_COREL,FECHA_EVENTO_COBIM  "
    			+ "	 ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc) a  "
    			+ "	 group by  a.IDE_GTEMP, a.FECHAM, a.HORAM,a.EVENTO_RELOJ_COBIM,a.DETALLE_COREL  "
    			+ "  ORDER BY a.FECHAM ASC,a.HORAM asc");   
    	return tabObteberTimbreXEmpleado.getFilas();	
    }
    //Nocturno

    private List<Fila> obtenerTimbreBiometricoNocturno(Integer IDE_GTEM,String FECHA_INICIAL,String FECHA_INICIAL1, String FECHA_FINAL, String FECHA_FINAL1){
   	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select IDE_COBIM, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA, "
    			//+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') as HORA  "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') as HORA  "
    			+ ",BIO.EVENTO_RELOJ_COBIM "
    			+ ",RELOJ.DETALLE_COREL  "
    			+ "from "
    			+ "con_biometrico_marcaciones as bio  "
    			+ "left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp "
    			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
    			+ "where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"'  "
    			+ "and emp.ide_gtemp="+IDE_GTEM+" AND RELOJ.IDE_COREL NOT IN(11,12,14,16,13,15) " 
    			//+ " order by  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc ");
    			+ "  order by  TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') asc "
    			+ ",TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') asc");
    //	tabObteberTimbreXEmpleado.imprimirSql();
    	return tabObteberTimbreXEmpleado.getFilas();


    	/*utilitario.consultar("select a.IDE_GTEMP, a.FECHA, a.HORA,a.EVENTO_RELOJ_COBIM,a.DETALLE_COREL "
    			+ " from (  "
    			+ "		  select "
    			+ "  	  EMP.IDE_GTEMP, "
    			+ "		  TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA, "
    			+ "		  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') as HORA,  "
    			+ "		  BIO.EVENTO_RELOJ_COBIM, "
    			+ "		  RELOJ.DETALLE_COREL  "
    			+ " 	  from  "
    			+ "		  con_biometrico_marcaciones as bio  "
    			+ "       left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp  "
    			+ "		  INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL    "
    			+ "		  where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "		  and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"' "
    			+ "		  and emp.ide_gtemp="+IDE_GTEM
    			+ " 	  ) a  "
    			+ "		 group by  a.FECHA,a.IDE_GTEMP, a.HORA,a.EVENTO_RELOJ_COBIM,a.DETALLE_COREL "
    			+ "		 order by  FECHA  asc, HORA  desc");*/

    	
    	
    	
    	
    	
    	
    	
    }
    
    
    private List<Fila> obtenerTimbreBiometricoNocturno2(Integer IDE_GTEM,String FECHA_INICIAL,String FECHA_INICIAL1, String FECHA_FINAL, String FECHA_FINAL1){
   	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select IDE_COBIM, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') as HORA  "
    			+ ",BIO.EVENTO_RELOJ_COBIM "
    			+ ",RELOJ.DETALLE_COREL  "
    			+ "from "
    			+ "con_biometrico_marcaciones as bio  "
    			+ "left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp "
    			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
    			+ "where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"'  "
    			+ "and emp.ide_gtemp="+IDE_GTEM+" AND RELOJ.IDE_COREL NOT IN(11,12,14,16,13,15) " 
    			//+ " order by  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc ");
    			+ "  order by  TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') asc ");
    //	tabObteberTimbreXEmpleado.imprimirSql();
    	return tabObteberTimbreXEmpleado.getFilas();

    }
    
    
    private List<Fila> obtenerTimbreBiometricoHoraExtra(Integer IDE_GTEM,String FECHA_INICIAL, String FECHA_FINAL){
   	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select IDE_COBIM, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA, "
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') as HORA  "
    			+ ",BIO.EVENTO_RELOJ_COBIM "
    			+ ",RELOJ.DETALLE_COREL  "
    			+ "from "
    			+ "con_biometrico_marcaciones as bio  "
    			+ "left join gth_empleado as emp  on TRIM(bio.ide_persona_cobim) = emp.tarjeta_marcacion_gtemp "
    			+ "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL "
    			+ "where bio.fecha_evento_cobim >= timestamp '"+FECHA_INICIAL+"'  "
    			+ "and  bio.fecha_evento_cobim <= timestamp '"+FECHA_FINAL+"'  "
    			+ "and emp.ide_gtemp="+IDE_GTEM+" AND RELOJ.IDE_COREL NOT IN(11,12,14,16,13,15) " 
    			//+ " order by  TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc ");
    			+ "  order by  TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') asc ");
    //	tabObteberTimbreXEmpleado.imprimirSql();
    	return tabObteberTimbreXEmpleado.getFilas();}
    
    
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
    private void getFeriadoXFecha(String fechaInicio, String fechaFin, int x){
    	String fechaInicioAsnov="";
    	String fechaFinAsnov="";
    	   	
  		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaFin);

  		int diferenciaDias=0; 
  		diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fechaInicio),utilitario.DeStringADate(fechaFin))+1;

    	for (int i = 0; i < diferenciaDias; i++) {
			if (utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaInicio), i).compareTo(dateFechaInicioReporteAgrupadaXEmpleado)>0) {
				return;
			}else{
			//utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaInicio), i));	
			System.out.println("Actualizar feriado:  "+utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaInicio), i)));
			System.out.println("update con_biometrico_marcaciones_resumen set horainicioband_cobmr='FERIADO',horafinband_cobmr='FERIADO' "
					 + "where  fecha_evento_cobmr='"+utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaInicio), i))+"'");
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioband_cobmr='FERIADO',horafinband_cobmr='FERIADO' "
			+ "where  fecha_evento_cobmr='"+utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaInicio), i))+"'");
			}
			
    	   	}
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
	
	 
	 
	 

	 public void insertarTablaPlantilla(
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
			 //boolean novedad_cobmr,3
			 String dia_cobmr,
		  	 int tipo_horario_cobmr,
		  	//String biometrico_alm_entrada,3
		  	//String biometrico_alm_salida, 3
		  	//String biometrico_salida_cobmr 3
		  	 String horainiciohorariohxe_cobmr, 
		  	 String horainiciobiometricohxe_cobmr, 
		  	 String horainiciobandhxe_cobmr, 
		  	 String horafinhorariohxe_cobmr, 
		  	 String horafinbiometricohxe_cobmr, 
		  	 String horafinbandhxe_cobmr

		  	  
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
				  		//+ "novedad_cobmr," 
				  		+ "dia_cobmr,"
				  		+ "tipo_horario_cobmr, "  
				  		+ "horainiciohorariohxe_cobmr," 
				  		+ "horainiciobiometricohxe_cobmr," 
				  		+ "horainiciobandhxe_cobmr," 
				  		+ "horafinhorariohxe_cobmr," 
				  		+ "horafinbiometricohxe_cobmr," 
				  		+ "horafinbandhxe_cobmr)" + 

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
				  		+ "'"+dia_cobmr+"', "
				  		+ ""+tipo_horario_cobmr+", "
				  		+ "'"+horainiciohorariohxe_cobmr+"', "
				  		+ "'"+horainiciobiometricohxe_cobmr+"', "
				  		+ "'"+horainiciobandhxe_cobmr+"', "
				  		+ "'"+horafinhorariohxe_cobmr+"', "
				  		+ "'"+horafinbiometricohxe_cobmr+"', "
	  					+ "'"+horafinbandhxe_cobmr+"') ");

			
			
		 
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
				+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'   "
				+ "AND IDE_COREL NOT IN(11,12,14,16,13,15)  "
			//	+ "and emp.ide_gtemp IN(204) "
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
				//+ "and emp.ide_gtemp IN(1093) "
				+ "and emp.ide_gtemp IN("+IDE_GTEMP+") "
				+ "AND IDE_COREL NOT IN(11,12,14,16,13,15)  "
				+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD')  "
				+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC");			
				
	}
			//	tab_reporte.imprimirSql();
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
//  				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico)); 
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
//      			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
//  					
//  					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
//      			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
//      			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));
//
//      			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
//      			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
//      			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
//  					
//      			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
//      			   Date finAlmuerzo=null;
//      			    
//      			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
//      			  if ((int)tiempoAlmuerzo==1) {
//        			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
//					}else {
//        			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
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
//          			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
//          			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
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
//      			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));
//
//      			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
//      			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
//      			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
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
	        TablaGenerica tabObternerTurnos=this.obtenerTurnosMatriz(1);

	        
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
	     				horaBiometrico = "";//(String)bio.getCampos()[2]+":00";
	     				
	     				if (bio.getCampos()[2].toString().length()==8) {
		   	     					horaBiometrico =(String)bio.getCampos()[2];
		   	     				}else {
		   	     					horaBiometrico =(String)bio.getCampos()[2]+":00";
		   	     				}
	     				//Evento de acuerdo a si es entrada o almuerzo
	     			//	String evento = (String)bio.getCampos()[3];
	     				//Unimos la fecha con la hora
	     				fechaHoraBiometrico = fechaBiometrico+" "+horaBiometrico;
	     			    //Calendario 
	     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico)); 
	     				
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
	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
	         			   //Suma de minuto para el ingreso
	             			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
	     					
	     					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

	         			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
	         			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
	         			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
	     					
	         			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
	         			   Date finAlmuerzo=null;
	         			    
	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
	         			  if ((int)tiempoAlmuerzo==1) {
	           			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
						}else {
	           			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR")));
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
	             			    	
	             			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
	             			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));

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
	         			  //  calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
	     					
	     					String fechaHoraHorarioFin = fechaBiometrico+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

	         			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
	         			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	         			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
	     					
	         			    
	         			   
							
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
	    
	 
	    
	    
	    private TablaGenerica obtenerTurnosMatriz(int ide_astur){
	    	
	    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario where ide_astur in("+ide_astur+")");
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
			String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-1));
			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
				
			//

			//suma una hora a la fecha final 
			String horaFechaInicioConsulta= diaSumaInicio+" "+horaSalida;    			
			String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),1));
			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
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
				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico)); 
				
			
			
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
 			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
 			   //Suma de minuto para el ingreso
    			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
 			    
					
					String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

 			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico+" "+horaIniAlmEmpl;
 			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
 			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
					
 			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico+" "+horaFinAlmEmpl;
 			   Date finAlmuerzo=null;
 			    
 			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);
 			  if ((int)tiempoAlmuerzo==1) {
   			    finAlmuerzo=sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),(int)tiempoAlmuerzoHorario);
 			  							  }else{
   			    finAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo),pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR")));
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
        			    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAINICIOALMBIO"));
        			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico+" "+tab_reporte.getValor(itemReporte, "HORAFINALMBIO"));
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
	    			String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-1));
	    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
	    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
						
	    			//

	    			//suma una hora a la fecha final 
	    			String horaFechaInicioConsulta= diaSumaInicio+" "+horaFinEmpl;    			
	    			String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),1));
	    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
	    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
						
	    			
	    			
	    			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7  	    			
	    			
	    			
	    			
	 			//Horario de acuerdo a los turnos ingrresados por talento humano
				String horaInicioEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_INICIAL_ASHOR");//cogo la hora del horario del erp
				String horaFinEmpl1 = horariosEmpleadoMes.getValor(i, "HORA_FINAL_ASHOR");// la hora final del horario erp
				horaIniAlmEmpl = horariosEmpleadoMes .getValor(i, "HORA_INICIO_ALMUERZO_ASHOR");//hora inicio erp
				horaFinAlmEmpl = horariosEmpleadoMes.getValor(i, "HORA_FIN_ALMUERZO_ASHOR");//Hora fin erp
				
				
				
				String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl1; //cogo la hora y le concateno con la fecha del horario
    			//String fechaHoraHorarioInicio= getFechaAsyyyyMMddHHmm(sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio1),Integer.parseInt(minuto_tolerancia_astur)));

				
				
				Calendar calFechaHoraHorarioInicio1 = Calendar.getInstance();//
			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
			    //Suma de minuto para el ingreso
    			 calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
				
				 String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioFin1 = Calendar.getInstance();
			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

			    
			  //Desde hasta puede hora de salida para el horario nocturno
			   String fechaHoraHorarioFinSalida = fechaBiometrico+" 08:00:00";
			    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
			    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));
				
			    String fechaHoraHorarioInicioFinSalida = fechaBiometrico+" 04:00:00";
			    Calendar calFechaHoraHorarioInicioFinSalida = Calendar.getInstance();
			    calFechaHoraHorarioInicioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));
				       			    
				//Desde hasta puede hora de ingreso para el horario nocturno
			    String fechaHoraHorarioFinEntrada = fechaBiometrico+" 24:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinEntrada));
			    
				String fechaHoraHorarioInicioEntrada = fechaBiometrico+" 00:00:00";
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicioEntrada));
				    
			    
			    Calendar calSumaDia = Calendar.getInstance();
			    calSumaDia.setTime(getFechaAsyyyyMMddHHmm(horaFechaSumaDia1));
				    
			    
			   //Prueba horario nocturno
			    //variable que guarda la hora desde 00:00: del dia siguiente
			    String fechaHoraCeroHoras = fechaBiometrico+" 00:00:00";
			    Calendar fecHoraInicio = Calendar.getInstance();
			    fecHoraInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicioEntrada));
				    
			    
			    
				//Siguiente
    			String fecha= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado);
    			String fechayhora=fecha+" 08:00:00";
    		    Calendar calFin = Calendar.getInstance();//
			    calFin.setTime(getFechaAsyyyyMMddHHmm(fechayhora));//
    			
			    
			  //Siguiente
    			String fecha1= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
    			String fechayhora12=fecha1+" 22:00:00";
    		    Calendar calEntrada = Calendar.getInstance();//
			    calEntrada.setTime(getFechaAsyyyyMMddHHmm(fechayhora12));//
			    
			    

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
		 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

		 			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
		 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
							
						
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
			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
			   
			
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
     			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

     			    String fechaHoraHorarioHasta = fechaBiometrico+" 24:00:00";
     			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
     			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
     			   
					
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
			+ "ORDER BY EMP.IDE_astur,EMP.IDE_GTEMP");		
	 }else {
		 tab_reporte= utilitario.consultar(
			    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
					+ "from gth_empleado emp  "
					+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
					+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				   + "and EMP.ide_gtemp IN("+IDE_GTEMP+") "
				   //+ "and EMP.ide_gtemp IN(101,103,104,107,108) "
					+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
					+ "ORDER BY EMP.IDE_astur,EMP.IDE_GTEMP");		
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
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
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
  	TablaGenerica tab_reporteEmpleado=getTablaResumenMarcacionesEmpleado(fechaInicial, fechaFinal,empleadoSeleccionado);
  	
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




public void getDiasLibresEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani, String IDE_GTEMP){

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
			
			 TablaGenerica tabObternerEmpleados=this.getTablaResumenMarcacionesEmpleado(fechaInicial,fechaFinal,IDE_GTEMP);
			
			 
			 
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
public TablaGenerica getTablaResumenMarcacionesEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){

	 TablaGenerica tab_reporte=null;
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
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
	 }else {
		   tab_reporte= utilitario.consultar("SELECT COUNT(ide_gtemp), ide_gtemp  "
			   		+ "FROM con_biometrico_marcaciones_resumen "
			   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' and ide_gtemp in("+IDE_GTEMP+") "
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
	 }
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



 	
	getDiasLibresEmpleado(fechaIni, fechaFin, 9, 11,"");
    	marcacionesEmpleado(fechaIni, fechaFin, 9, 11,"");
	System.out.println("FIN PROCESO MARCACIONES");

	
		}else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		}
		
	}
	else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	}		


}




public void marcacionesEmpleado(String fechaInicial, String fechaFinal,int ide_gemes, int ide_geani,String IDE_GTEMP){

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
				
				 TablaGenerica tabObternerEmpleados=this.getTablaResumenSinMarcacionesEmpleado(fechaInicial,fechaFinal,IDE_GTEMP);
				
				 for (int i = 0; i < tabObternerEmpleados.getTotalFilas(); i++) {
					//    insertarTablaResumen(Integer.parseInt(tabObternerEmpleados.getValor(i, "IDE_GTEMP")), fecha_evento_cobmr, "", "", "LIBRE", "", "", "","", 0, "", "", "", "LIBRE", false, dia_cobmr);  		
				
				    	/////////////////////////////////////////////////AQUI DEBO VALIDAR CUANDO OTRO MES
				    	//Debo realizar
				    	TablaGenerica tabMatrix=utilitario.consultar("select IDE_GTEMP,IDE_ASTUR from gth_empleado where ide_gtemp ="+tabObternerEmpleados.getValor(i,"IDE_GTEMP")+" and ide_astur IN(1,26)");
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




public  TablaGenerica getTablaResumenSinMarcacionesEmpleado(String fechaInicial, String fechaFinal,String IDE_GTEMP){
	
	 TablaGenerica tab_reporte;
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
	   tab_reporte= utilitario.consultar("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
	   		 + "SELECT  ide_gtemp  "
	   		 + "FROM con_biometrico_marcaciones_resumen  "
	   		 + "where  fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"'  "
	   		 + "group by ide_gtemp  "
	   		 + "order by ide_gtemp asc)");
	 }
	 else{
		   tab_reporte= utilitario.consultar("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
			   		 + "SELECT  ide_gtemp  "
			   		 + "FROM con_biometrico_marcaciones_resumen  "
			   		 + "where  fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' AND IDE_GTEMP IN("+IDE_GTEMP+") "
			   		 + "group by ide_gtemp  "
			   		 + "order by ide_gtemp asc)");
	 }
		 return tab_reporte;
	
	
	}


public void getMarcacionesAlmuerzo(String fechaIni,String fechaFin, String IDE_GTEMP){
//Variablesf
	String horainicioalm_cobmr="",horainicioalmbio_cobmr="",horafinalmbio_cobmr="";

	TablaGenerica tab_almuerzo=null;
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='', horafinalmbio_cobmr='' "
					 + "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and (horainicioalmbio_cobmr like '%SIN TIMBRE%' or horafinalmbio_cobmr='%SIN TIMBRE%') ");
					   	
			 
			tab_almuerzo=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
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
	 }else {
		 
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='', horafinalmbio_cobmr='' "
					 + "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and (horainicioalmbio_cobmr like '%SIN TIMBRE%' or horafinalmbio_cobmr='%SIN TIMBRE%')  "
					 		+ "AND IDE_GTEMP IN("+IDE_GTEMP+") ");
					   	
			 
		tab_almuerzo=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
				+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
				+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr "
				+ "FROM con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
				+ "and ide_gtemp in("+IDE_GTEMP+") "
				+ "order by ide_gtemp asc ,fecha_evento_cobmr  asc");
	 }

	
if (tab_almuerzo.getTotalFilas()==0) {
	
}else{

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
}

}



}


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
		

public void crearPlantillaHorario(String fecha_ini,String fecha_fin,String empleadoSeleccionado){

	int inicio=0,fin=0;
	inicio=utilitario.getDia(fecha_ini);
	fin=utilitario.getDia(fecha_fin);
	String sql="",sql_turnoMensual="",nuevaFechaInicio="",nuevaFechaFin="";
	nuevaFechaInicio=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-01";
	nuevaFechaFin=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_ini));
	int anioTemp=utilitario.getAnio(fecha_ini);
	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
    
    if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
	
    	sql="select ide_gtemp,ide_astur  "
    			+ "from gth_empleado "
    			+ "where ide_gtemp in(select par.ide_gtemp "
    			+ "from gen_empleados_departamento_par par "
    			+ "where par.activo_geedp=true "
    			+ "group by par.ide_gtemp "
    			+ "UNION all  "
    			+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2 "
    			+ "where par2.fecha_finctr_geedp between '"+nuevaFechaInicio+"' and '"+nuevaFechaFin+"' "
    			+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true "
    			+ "group by ide_gtemp ) "
    			+ "group by par2.ide_gtemp "
    			+ "order by ide_gtemp ) "
    			+ "and ide_astur is not null "
    			+ "order by ide_astur asc,ide_gtemp asc";

    	sql_turnoMensual="SELECT ide_gtemp, ide_gemes "
    			+ "FROM asi_horario_mes_empleado "
    			+ "where ide_geani="+anio+" and ide_gemes="+utilitario.getMes(fecha_ini)+" and ide_gtemp in( "
    			+ "select ide_gtemp   "
    			+ "from gth_empleado  "
    			+ "where ide_gtemp in(select par.ide_gtemp  "
    			+ "from gen_empleados_departamento_par par  "
    			+ "where par.activo_geedp=true  "
    			+ "group by par.ide_gtemp  "
    			+ "UNION all  "
    			+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2  "
    			+ "where par2.fecha_finctr_geedp between '"+nuevaFechaInicio+"' and '"+nuevaFechaFin+"'  "
    			+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true  "
    			+ "group by ide_gtemp )  "
    			+ "group by par2.ide_gtemp  "
    			+ "order by ide_gtemp )  "
    			+ "and ide_astur is null  "
    			+ "order by ide_astur asc,ide_gtemp asc) "
    			+ "group by  ide_gtemp,ide_gemes "
    			+ "order by ide_gtemp asc";
    			
    			
    	
    	
	}else {
		sql=" select ide_gtemp,ide_astur  "
				+ "from gth_empleado "
				+ "where ide_gtemp in(select par.ide_gtemp "
				+ "from gen_empleados_departamento_par par  "
				+ "where par.activo_geedp=true and ide_gtemp="+empleadoSeleccionado+" "
				+ "group by par.ide_gtemp "
				+ "UNION all  "
				+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2 "
				+ "where par2.fecha_finctr_geedp between '"+nuevaFechaInicio+"' and '"+nuevaFechaFin+"'  "
				+ "and par2.ide_gtemp  in("+empleadoSeleccionado+")  "
				+ "group by par2.ide_gtemp  "
				+ "order by ide_gtemp )  "
				+ "and ide_astur is not null  "
				+ "order by ide_astur asc,ide_gtemp asc";
		
		sql_turnoMensual="SELECT ide_gtemp, ide_gemes "
				+ "FROM asi_horario_mes_empleado "
				+ "where ide_geani="+anio+" and ide_gemes="+utilitario.getMes(fecha_ini)+" and ide_gtemp in( "
				+ "select ide_gtemp   "
				+ "from gth_empleado  "
				+ "where ide_gtemp in(select par.ide_gtemp  "
				+ "from gen_empleados_departamento_par par  "
				+ "where par.activo_geedp=true and par.ide_gtemp="+empleadoSeleccionado
				+ " group by par.ide_gtemp  "
				+ " UNION all  "
				+ " select par2.ide_gtemp from gen_empleados_departamento_par  par2  "
				+ " where par2.fecha_finctr_geedp between '"+nuevaFechaInicio+"' and '"+nuevaFechaFin+"'  "
				+ " and par2.ide_gtemp in("+empleadoSeleccionado+")  "
				+ " group by par2.ide_gtemp  "
				+ " order by ide_gtemp )  "
				+ " and ide_astur is null  "
				+ " order by ide_astur asc,ide_gtemp asc) "
				+ " group by  ide_gtemp,ide_gemes "
				+ " order by ide_gtemp asc";
		
		
		utilitario.getConexion().ejecutarSql("delete from con_biometrico_marcaciones_resumen  "
				+ " where ide_gtemp ="+empleadoSeleccionado+" "
				+ " and  fecha_evento_cobmr between  '"+fecha_ini+"' and '"+fecha_fin+"'  "
						+ "");
			}
	
	TablaGenerica tab_empleados_horario_matriz=utilitario.consultar(sql);
	
	String sql_turnoMatriz="";
	TablaGenerica tab_empleados_horario_mensual=utilitario.consultar(sql_turnoMensual);

	
	
	String hora_inicial_ashor="",hora_inicio_almuerzo_ashor="",hora_fin_almuerzo_ashor="",min_almuerzo_ashor="",hora_final_ashor="";
	String hora_inicial_ashor_relleno="",hora_inicio_almuerzo_ashor_relleno="",hora_fin_almuerzo_ashor_relleno="",min_almuerzo_ashor_relleno="",hora_final_ashor_relleno="";
	String hora_inicial_ashor_horario="",hora_inicio_almuerzo_ashor_horario="",hora_fin_almuerzo_ashor_horario="",min_almuerzo_ashor_horario="",hora_final_ashor_horario="";

	
	sql_turnoMatriz="SELECT turnos.ide_astur,horario.hora_inicial_ashor,horario.hora_inicio_almuerzo_ashor,horario.hora_fin_almuerzo_ashor,horario.min_almuerzo_ashor,horario.hora_final_ashor  "
			+ "FROM asi_turnos_horario turnos  "
			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor  "
			+ "WHERE turnos.IDE_ASTUR IN(1,26)  and horario.activo_ashor=true  "
			+ "group by turnos.ide_astur,horario.hora_inicial_ashor,horario.hora_inicio_almuerzo_ashor,"
			+ "horario.hora_fin_almuerzo_ashor,horario.min_almuerzo_ashor,horario.hora_final_ashor "
			+ "order by turnos.ide_astur asc";
		
	
	
	TablaGenerica turnosMariz=utilitario.consultar(sql_turnoMatriz);
	if (turnosMariz.getTotalFilas()>0) {
		hora_inicial_ashor=turnosMariz.getValor(0, "hora_inicial_ashor");
		hora_inicio_almuerzo_ashor=turnosMariz.getValor(0, "hora_inicio_almuerzo_ashor");
		hora_fin_almuerzo_ashor=turnosMariz.getValor(0, "hora_fin_almuerzo_ashor");
		min_almuerzo_ashor=turnosMariz.getValor(0, "min_almuerzo_ashor");
		hora_final_ashor=turnosMariz.getValor(0, "hora_final_ashor");
		
		hora_inicial_ashor_relleno=turnosMariz.getValor(1, "hora_inicial_ashor");
		hora_inicio_almuerzo_ashor_relleno=turnosMariz.getValor(1, "hora_inicio_almuerzo_ashor");
		hora_fin_almuerzo_ashor_relleno=turnosMariz.getValor(1, "hora_fin_almuerzo_ashor");
		min_almuerzo_ashor_relleno=turnosMariz.getValor(1, "min_almuerzo_ashor");
		hora_final_ashor_relleno=turnosMariz.getValor(1, "hora_final_ashor");
	}
		
	int diaSemana =0;

	

	System.out.println("Total Empleados: "+tab_empleados_horario_matriz.getTotalFilas());
	int contador=0;
	for (int i = 0; i < tab_empleados_horario_matriz.getTotalFilas(); i++) {
		contador=0;
	//	System.out.println("Empleado: "+tab_empleados_horario_matriz.getValor(i,"ide_gtemp"));
		if (i==(tab_empleados_horario_matriz.getTotalFilas()-1)) {
			listEmpleadoHorarioAnual=tab_empleados_horario_matriz.getValor(i,"ide_gtemp");
		}else {
			listEmpleadoHorarioAnual=tab_empleados_horario_matriz.getValor(i,"ide_gtemp")+",";
		}


	for (int x = inicio; x<= fin; x++) {
		Calendar calFechaDia = Calendar.getInstance()																																																			;
		calFechaDia.setTime(getFechaAsyyyyMMdd(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)))); 
		diaSemana = calFechaDia.get(Calendar.DAY_OF_WEEK);
		//diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x)));
		if (tab_empleados_horario_matriz.getValor(i,"ide_astur").equals("1")) {
			if (diaSemana==7 || diaSemana==1) {
				insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_matriz.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
						"","","LIBRE", "", "","","", 0.00, "0.00","","","LIBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), (x-1)))),1,"","","","","","");
						
			
			}else{
			insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_matriz.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
				hora_inicial_ashor,"","SIN TIMBRE", hora_inicio_almuerzo_ashor, hora_fin_almuerzo_ashor,"","",0.00, "1.00", hora_final_ashor,"","SIN TIMBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), (x-1)))),1,"","","","","","");
			}
			contador++;
		}else {
			if (diaSemana==7 || diaSemana==1) {
				insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_matriz.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
						"","","LIBRE", "", "","","", 0.00,"0.00", "","","LIBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), (x-1)))),1,"","","","","","");
			
			}else{
			
			insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_matriz.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
					hora_inicial_ashor_relleno,"","SIN TIMBRE", hora_inicio_almuerzo_ashor_relleno, hora_fin_almuerzo_ashor_relleno,"","", 0.00,"1.00", hora_final_ashor_relleno,"","SIN TIMBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), (x-1)))),1,"","","","","","");
		}
			contador++;
		}
	}
	
	
	}
	
	
	
	
	

	 contador=0;
	String hora_inicial_ashor_mensual="",hora_inicio_almuerzo_ashor_mensual="",hora_fin_almuerzo_ashor_mensual="",min_almuerzo_ashor_mensual="",hora_final_ashor_mensual="";
	 contador=0;

    String emp="";
	int contadorHorarioMensual=0;
	for (int i = 0; i < tab_empleados_horario_mensual.getTotalFilas(); i++) {
		contador=0;
		//System.out.println("Empleado: "+tab_empleados_horario_mensual.getValor(i,"ide_gtemp"));
		//TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+tab_empleados_horario_mensual.getValor(i, "ide_gtemp")+"  "
   		//		+ "and ide_gemes="+utilitario.getMes(fecha_ini)+" and ide_geani="+anio);
		
		
		//if (i==(tab_empleados_horario_matriz.getTotalFilas()-1)) {
			//listEmpleadoHorarioMensual=tab_empleados_horario_matriz.getValor(i,"ide_gtemp");
		//}else {
		//	listEmpleadoHorarioMensual=tab_empleados_horario_matriz.getValor(i,"ide_gtemp")+",";
		//}

		//
		TablaGenerica tabEmpleadoMatrizMensualHorario;
		//if (tabEmpleadoMatrizMensual.getTotalFilas()>0) {
			for (int x = inicio; x<= fin; x++) {
				String sql_turno="select  horario.ide_ashor,  "
						+ "HORARIO.HORA_INICIAL_ASHOR,  "
						+ "HORARIO.HORA_FINAL_ASHOR,  "
						+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
						+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR,  "
						+ "HORARIO.MIN_ALMUERZO_ASHOR, "
						+ "HORARIO.NOM_ASHOR "
						+ "from  "
						+ "asi_turnos_horario turno  "
						+ "left join asi_horario HORARIO  on horario.ide_ashor=turno.ide_ashor  "
						+ "where  turno.IDE_ASTUR in(select dia"+x+" from asi_horario_mes_empleado where ide_gemes="+utilitario.getMes(fecha_ini)+" and ide_gtemp="+tab_empleados_horario_mensual.getValor(i,"ide_gtemp")+" and ide_geani="+anio+")"
						+ "and horario.activo_ashor=true  "
						+ "ORDER BY HORARIO.IDE_ASHOR ASC";
				tabEmpleadoMatrizMensualHorario=utilitario.consultar(sql_turno);
				if (tabEmpleadoMatrizMensualHorario.getTotalFilas()>0) {	
					if (tabEmpleadoMatrizMensualHorario.getValor("MIN_ALMUERZO_ASHOR").equals("0") || Integer.parseInt(tabEmpleadoMatrizMensualHorario.getValor("MIN_ALMUERZO_ASHOR"))==0) {
						hora_inicio_almuerzo_ashor_horario="";
						hora_fin_almuerzo_ashor_horario="";
						min_almuerzo_ashor_horario="0.00";
						hora_inicial_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_inicial_ashor");
						hora_final_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_final_ashor");
						insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_mensual.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
								hora_inicial_ashor_horario,"","SIN TIMBRE", "S/A", "S/A","S/A","S/A", 0.00,"0.00", hora_final_ashor_horario,"","SIN TIMBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x-1))),2,"","","","","","");
						contador++;
						
						
					}else {
						if (tabEmpleadoMatrizMensualHorario.getValor("IDE_ASHOR").equals("19")) {
							insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_mensual.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
								"","","LIBRE", "", "","","", 0.00,"0.00","","","LIBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x-1))),2,"","","","","","");			
						
						}else {
							
						
						
						hora_inicio_almuerzo_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_inicio_almuerzo_ashor");
						hora_fin_almuerzo_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_fin_almuerzo_ashor");
						min_almuerzo_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("min_almuerzo_ashor");
						hora_inicial_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_inicial_ashor");
						hora_final_ashor_horario=tabEmpleadoMatrizMensualHorario.getValor("hora_final_ashor");
						insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_mensual.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
							hora_inicial_ashor_horario,"","SIN TIMBRE", hora_inicio_almuerzo_ashor_horario, hora_fin_almuerzo_ashor_horario,"","", 0.00,"1.00", hora_final_ashor_horario,"","SIN TIMBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x-1))),2,"","","","","","");
						}
						contador++;
					}
				}else {
insertarTablaPlantilla(Integer.parseInt(tab_empleados_horario_mensual.getValor(i,"ide_gtemp")), utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), contador)), 
"","","LIBRE", "", "","","", 0.00,"0.00","","","LIBRE", diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x-1))),2,"","","","","","");			
				
					
					
			
					contador++;
				}			
			}
	}
		//}else {
		//	emp+=tab_empleados_horario_mensual.getValor(i,"ide_gtemp");

		//}
		
			
	
	

	//	tab_resumen_marcaciones.setCondicion("fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=1");
				//+ "and ide_gtemp in(103,104,107,108,109,110,112,113,114,116,118,120,124,125,128,129,149)");
	TablaGenerica tab_plantilla_matriz=null;
	if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
	
	tab_plantilla_matriz=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
				+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
				+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
				+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
				+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,horafinhorariohxe_cobmr,ide_gtgre_extra "
				+ "FROM con_biometrico_marcaciones_resumen  "
				+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=1 "
				+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
	}else{
		
		tab_plantilla_matriz=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
				+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
				+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
				+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
				+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,horafinhorariohxe_cobmr,ide_gtgre_extra "
				+ "FROM con_biometrico_marcaciones_resumen  "
				+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=1 and ide_gtemp in("+empleadoSeleccionado+") "
				+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
		
	}
	
	
	
		if (tab_plantilla_matriz.getTotalFilas()==0) {
			System.out.println("No existen datos para procesar en loep");
			
		}
		
		//	tab_resumen_marcaciones.setCondicion("fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=1");

		//and ide_gtemp in(623,1211) and ide_gtemp=103
		//tab_resumen_marcaciones.ejecutarSql();
		//utilitario.addUpdate("tab_resumen_marcaciones");
System.out.println("Inicio: "+utilitario.getHoraActual());




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////









List<Fila> biometrico_matriz=null;
int banderaAlmuerzo_matriz=0;
int banderaAlmuerzoEntrada_matriz=0;
int banderaEntrada_matriz=0,banderaHoraExtra1001=0;
Integer tipo1 = 0;
Integer tipo2 = 0;
	Integer tipo3 = 0;
	Integer tipo4= 0;
	Integer tipo5 = 0;
	Integer tipo6 = 0;
	Integer tipo7 = 0;
Integer tipo1SinAlmuerzo_matriz= 0;
	Integer tipo2SinAlmuerzo_matriz = 0;
	Integer tipo3SinAlmuerzo_matriz = 0;
	Integer tipo4SinAlmuerzo_matriz= 0;
	double tiempoAlmuerzo_matriz=0.00;
int fin_matriz=utilitario.getDia(fecha_fin);
boolean bandRegistroUnico_matriz=false;
int indice_matriz=0,empleadoActual_matriz=0;
String horaInicioEmpl_matriz="",horaIniAlmEmpl_matriz="",horaFinAlmEmpl_matriz="",min_almuerzo_matriz="",horaFinEmpl_matriz="",fechaBiometrico_matriz="",horaBiometrico_matriz="",ubicacion_biometrico_matriz="",fechaHoraBiometrico_matriz="",biometrico_entrada_cobmr_matriz="",horainicioalmbio_cobmr_matriz="";
String empleadoActual_matriz_="",tipo_horario_matriz="",biometrico_alm_salida_matriz="",biometrico_alm_entrada_matriz="",biometrico_salida_cobmr_matriz="",horainiciohorariohxe_cobmr="",horafinhorariohxe_cobmr="";

TablaGenerica tab_empleados_matriz=null;
if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
 tab_empleados_matriz=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=1  "
		+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
		+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
}else{
	 tab_empleados_matriz=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
				+ "FROM con_biometrico_marcaciones_resumen  "
				+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=1 and ide_gtemp in("+empleadoSeleccionado+") "
				+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
				+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
	
}
// and ide_gtemp in(1211,1212)
 int valor_matriz=0;
  empleadoActual_matriz=0;//tab_empleados_matriz.getTotalFilas()
for (int x = 0; x < tab_empleados_matriz.getTotalFilas(); x++) {
	indice_matriz=0;
TablaGenerica tab_reporte=getTablaReporte(tab_empleados_matriz.getValor(x, "ide_gtemp"),fecha_ini, fecha_fin);//getTablaReporte(tab_empleados.getValor(j, "ide_gtemp"),fecha_ini, fecha_fin);
//System.out.println("Empleado: "+tab_empleados_matriz.getValor(x, "ide_gtemp")+"   Empleado Marcacion: "+tab_plantilla_matriz.getValor(valor_matriz,"ide_gtemp")+"  valor matriz: "+valor_matriz);
empleadoActual_matriz_=tab_empleados_matriz.getValor(x, "ide_gtemp");
empleadoActual_matriz=Integer.parseInt(empleadoActual_matriz_);
	if (tab_reporte.getTotalFilas()==0) {
		valor_matriz=valor_matriz+fin_matriz;
	}else {
	
	}
	
	
	
   
	for (int y = 0; y < tab_reporte.getTotalFilas(); y++) {
		for (int z = valor_matriz; z < tab_plantilla_matriz.getTotalFilas(); z++) {
		//for (int z = tab_plantilla_matriz.getTotalFilas(); z < tab_plantilla_matriz.getTotalFilas(); z++) {

		
		
			//valor_relleno++;
		//System.out.println("z: "+z);
		if (tab_plantilla_matriz.getValor(z, "fecha_evento_cobmr").equals(tab_reporte.getValor(y, "TO_CHAR"))) {

			if((tab_plantilla_matriz.getValor(z, "horainiciohorario_cobmr")==null || tab_plantilla_matriz.getValor(z, "horainiciohorario_cobmr").equals("") || tab_plantilla_matriz.getValor(z, "horainiciohorario_cobmr").isEmpty())){
				
	       		String fechaBiometricoAgrupadaXEmpleado ="";

				
	       		
	       		
	   	    	//Calendar calFechaDia = Calendar.getInstance();
				//	calFechaDia.setTime(getFechaAsyyyyMMdd(tab_plantilla_matriz.getValor(x, "fecha_evento_cobmr"))); 
				//	 diaSemana = calFechaDia.get(Calendar.DAY_OF_WEEK);
					//diaSemana(utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_ini), x)));
					
					 
			 		 fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y,"TO_CHAR");
			       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
			       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
			   	    	biometrico_matriz = obtenerTimbreBiometrico(Integer.parseInt(empleadoActual_matriz_), dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);


			   	 	for (Fila bio : biometrico_matriz) {
		     				//fecha de la tabla biometrico
		     				fechaBiometrico_matriz = (String)bio.getCampos()[1];
		     				//Hora de la tabla biometrico
		     				horaBiometrico_matriz = "";//(String)bio.getCampos()[2]+":00";
		     				if (bio.getCampos()[2].toString().length()==8) {
		     					horaBiometrico_matriz =(String)bio.getCampos()[2];
		     				}else {
		     					horaBiometrico_matriz =(String)bio.getCampos()[2]+":00";
		     				}
					
					
		     				fechaHoraBiometrico_matriz = fechaBiometrico_matriz+" "+horaBiometrico_matriz;
		     			    //Calendario 
		     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
		     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_matriz));
					
							 banderaHoraExtra1001=0;
							String fechaHoraHorarioFin = fechaBiometrico_matriz+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario
			 			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
			 			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

			 			    String fechaHoraHorarioHasta = fechaBiometrico_matriz+" 24:00:00";
			 			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
			 			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
								    		
				    		
				    		//tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
				    		//tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "ANTICIPADO");
			 			    
			 			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
			 			         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
			                	 if (banderaHoraExtra1001==0) {
			                		// tab_resumen_marcaciones.setValor(x, "horainiciobiometrico_cobmr", horaBiometrico);
			                		// tab_resumen_marcaciones.setValor(x, "horainicioband_cobmr", "EXTRA");
			                		 //tab_resumen_marcaciones.setValor(x, "biometrico_entrada_cobmr", ubicacion_biometrico);
			 			    	biometrico_entrada_cobmr_matriz= ubicacion_biometrico_matriz;

						    		banderaHoraExtra1001=1;
						    		
	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	+ " horainiciobiometrico_cobmr ='"+horaBiometrico_matriz+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));
						    					  
						    		
						   	    }else {
						   	    	//tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
						   	    	//tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "EXTRA");
						   	    	//tab_resumen_marcaciones.setValor(x, "biometrico_salida_cobmr", ubicacion_biometrico);
			       		biometrico_salida_cobmr_matriz= ubicacion_biometrico_matriz;
			 			    	banderaHoraExtra1001=2;

	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	+ " horafinbiometrico_cobmr ='"+horaBiometrico_matriz+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));
			 			    						
			 			    	
			 			    	
			 				}
						
						}	
				
			   	 	}
				
				
			}else {
			
				
				horaInicioEmpl_matriz=tab_plantilla_matriz.getValor(z, "horainiciohorario_cobmr");
				horaIniAlmEmpl_matriz=tab_plantilla_matriz.getValor(z, "horainicioalm_cobmr");
				horaFinAlmEmpl_matriz=tab_plantilla_matriz.getValor(z, "horafinalm_cobmr");
				horaFinEmpl_matriz=tab_plantilla_matriz.getValor(z, "horafinhorario_cobmr");						
	       		String fechaBiometricoAgrupadaXEmpleado ="";
	       		//if (bandRegistroUnico_matriz) {
				//	k=k-1;
				//}		
	       		 fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y,"TO_CHAR");
	       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
	       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
	       		
	       		 banderaAlmuerzo_matriz=0;
	    		 banderaAlmuerzoEntrada_matriz=0;
	    		 banderaEntrada_matriz=0;
	    		 tipo1 = 0;
	    		 tipo2 = 0;
	    	   	 tipo3 = 0;
	    	   	 tipo4= 0;
	    	   	 tipo5 = 0;
	    	   	 tipo6 = 0;
	    	   	 tipo7 = 0;
	    	     tipo1SinAlmuerzo_matriz= 0;
	    	   	 tipo2SinAlmuerzo_matriz = 0;
	    	   	 tipo3SinAlmuerzo_matriz = 0;
	    	   	 tipo4SinAlmuerzo_matriz= 0;
	    	   	 tiempoAlmuerzo_matriz=0.00;
	       		
	       		
	       		//System.out.println("SALE ERROR:  " +empleadoActual+" : "+fechaBiometricoAgrupadaXEmpleado );
	   	    	biometrico_matriz = obtenerTimbreBiometrico(Integer.parseInt(empleadoActual_matriz_), dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

	   	    	boolean band=false;
	   	 	for (Fila bio : biometrico_matriz) {
   	     				//fecha de la tabla biometrico
   	     				fechaBiometrico_matriz = (String)bio.getCampos()[1];
   	     				//Hora de la tabla biometrico
   	     				horaBiometrico_matriz = "";//(String)bio.getCampos()[2]+":00";
   	     				if (bio.getCampos()[2].toString().length()==8) {
   	     					horaBiometrico_matriz =(String)bio.getCampos()[2];
   	     				}else {
   	     					horaBiometrico_matriz =(String)bio.getCampos()[2]+":00";
   	     				}
   	     				//Evento de acuerdo a si es entrada o almuerzo
   	     				ubicacion_biometrico_matriz = (String)bio.getCampos()[4];
   	     				//Unimos la fecha con la hora
   	     				fechaHoraBiometrico_matriz = fechaBiometrico_matriz+" "+horaBiometrico_matriz;
   	     			    //Calendario 
   	     				Calendar calFechaHoraBiometrico = Calendar.getInstance();
   	     			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
   	     				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_matriz)); 
   	     				
	   	     				String fechaHoraHorarioInicio = fechaBiometrico_matriz+" "+horaInicioEmpl_matriz; //cogo la hora y le concateno con la fecha del horario
   	         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
   	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
   	         			    //Suma de minuto para el ingreso
   	         			   // calFechaHoraHorarioInicio.add(Calendar.MINUTE, 0);
   	     					
   	     					String fechaHoraHorarioFin = fechaBiometrico_matriz+" "+horaFinEmpl_matriz;//cogo la hora y le concateno con la fecha del horario
   	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
   	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

   	         			    
   	         			    String fechaHoraHorarioInicial = fechaBiometrico_matriz+" "+horaFinAlmEmpl_matriz;//cogo la hora y le concateno con la fecha del horario
   	         			    Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
   	         			    calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicial));
   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    
   	         			    

   	         			    String fechaHoraHorarioIniAlmuerzo = fechaBiometrico_matriz+" "+horaIniAlmEmpl_matriz;
   	         			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
   	         			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
   	         			    calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, -15);	    
   	     					
   	         			    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico_matriz+" "+horaFinAlmEmpl_matriz;
   	         			   Date finAlmuerzo=null;
   	         			    
   	         			   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(tab_plantilla_matriz.getValor(z, "tiempohoralm_cobmr")));

   	         			Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
	         			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo)); 
	         			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
   						

 			    //Hora desde Inicio de Entrada de Horario
 			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio),-3));
	    		     Calendar calHoraFechaBase= Calendar.getInstance();
	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteBase));
		
	
		
	   	 if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
			    	if (banderaEntrada_matriz==0) {

		    		//tab_resumen_marcaciones.setValor(x, "horainiciobiometrico_cobmr", horaBiometrico);
	    		//tab_resumen_marcaciones.setValor(x, "horainicioband_cobmr", "OK");
	    		//tab_resumen_marcaciones.setValor(x, "horainicioalmbio_cobmr", "SIN TIMBRE");
	    		//tab_resumen_marcaciones.setValor(x, "horafinalmbio_cobmr", "SIN TIMBRE");
	    		biometrico_entrada_cobmr_matriz= ubicacion_biometrico_matriz;
	    		//tab_resumen_marcaciones.setValor(x, "biometrico_entrada_cobmr", ubicacion_biometrico);
	    	   	tipo1=1;
   			    	tipo2=0;
   			    	banderaEntrada_matriz++;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_matriz+"',horainicioband_cobmr='OK',biometrico_entrada_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));

	    }
	   	 } else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) >= 0
				&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
					   		 
					if (banderaEntrada_matriz==0) {
					//tab_resumen_marcaciones.setValor(x, "horainiciobiometrico_cobmr", horaBiometrico);
				//tab_resumen_marcaciones.setValor(x, "horainicioband_cobmr", "ATRASADO");
	    		//tab_resumen_marcaciones.setValor(x, "horainicioalmbio_cobmr", "SIN TIMBRE");
	    		//tab_resumen_marcaciones.setValor(x, "horafinalmbio_cobmr", "SIN TIMBRE");
				biometrico_entrada_cobmr_matriz= ubicacion_biometrico_matriz;
    			//tab_resumen_marcaciones.setValor(x, "biometrico_entrada_cobmr", ubicacion_biometrico);
    		 	tipo1=2;
			    	banderaEntrada_matriz++;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_matriz+"',horainicioband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));

			    	
	   	 }
		}

			    	
	    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
	    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo_matriz==0){
			    	if (banderaAlmuerzo_matriz == 0){
 			    	//tab_resumen_marcaciones.setValor(x, "horainicioalmbio_cobmr", horaBiometrico);
			    	biometrico_alm_salida_matriz= ubicacion_biometrico_matriz;
			    	//tab_resumen_marcaciones.setValor(x, "biometrico_alm_salida", ubicacion_biometrico);
	    		//tab_resumen_marcaciones.setValor(x, "horafinalmbio_cobmr", "SIN TIMBRE");

				horaAnterior=horaBiometrico_matriz;			    
				banderaAlmuerzo_matriz++;
  			    	tipo3=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainicioalmbio_cobmr ='"+horaBiometrico_matriz+"',biometrico_alm_salida='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));
  			    	
horainicioalmbio_cobmr_matriz=horaBiometrico_matriz;	    	
			    	}
			    	}else if (banderaAlmuerzo_matriz==1 && horaBiometrico_matriz.compareTo(horaAnterior)>0  && horaBiometrico_matriz.compareTo(horaFinEmpl_matriz)<0 && banderaAlmuerzoEntrada_matriz==0){
    			String uno= fechaBiometrico_matriz+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
        		String dos = fechaBiometrico_matriz+" "+horaFinAlmEmpl_matriz;
  			    	
    				Calendar caluno = Calendar.getInstance();//
        			Calendar caldos = Calendar.getInstance();//
        			caluno.setTime(getFechaAsyyyyMMddHHmm(dos));//
        			caldos.setTime(getFechaAsyyyyMMddHHmm(uno));//
        			//Suma de minuto para el ingreso  			
        		
        			caluno.add(Calendar.MINUTE, 60);				
	    	
	    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
		    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0 && banderaAlmuerzo_matriz==1){
	    			//tab_resumen_marcaciones.setValor(x, "horafinalmbio_cobmr", horaBiometrico);
		    	Date fechaIniAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico_matriz+" "+horainicioalmbio_cobmr_matriz);
		    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico_matriz+" "+horaBiometrico_matriz);
		    	tiempoAlmuerzo_matriz = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
		    	//tab_resumen_marcaciones.setValor(x, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
				horaAnteriorAlm=horaBiometrico_matriz;
				//tab_resumen_marcaciones.setValor(x, "biometrico_alm_entrada", ubicacion_biometrico);
			    biometrico_alm_entrada_matriz= ubicacion_biometrico_matriz;
			    tipo4=1;
		    	banderaAlmuerzoEntrada_matriz++;
		    	horaAnterior="";
		    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinalmbio_cobmr ='"+horaBiometrico_matriz+"',tiempoalm_cobmr="+tiempoAlmuerzo_matriz+",biometrico_alm_entrada='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));

		    	
	    		}
	    		
	    		
	    	if (calFechaHoraBiometrico.compareTo(caldos) > 0
		    		&& calFechaHoraBiometrico.compareTo(caluno) >= 0 && banderaAlmuerzo_matriz==1 &&  calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){						
		    	//	tab_resumen_marcaciones.setValor(x, "horafinalmbio_cobmr", "SIN TIMBRE");
	    		//tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
	    		//tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "ANTICIPADO");
				horaAnteriorAlm=horaBiometrico_matriz;
				//tab_resumen_marcaciones.setValor(x, "biometrico_salida_cobmr", ubicacion_biometrico);
		    	biometrico_salida_cobmr_matriz= ubicacion_biometrico_matriz; 		
		    	tipo5=1;
			   	tipo6=0;
		    	banderaAlmuerzoEntrada_matriz++;
		    	horaAnterior="";
		    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_matriz+"',horafinband_cobmr='ANTICIPADO',biometrico_salida_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));

	    	}
	 
		if (calFechaHoraBiometrico.compareTo(caldos) > 0
		    		&& calFechaHoraBiometrico.compareTo(caluno) >= 0 && banderaAlmuerzo_matriz==1 &&  calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
		//	tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
		//	tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "OK");
			horaAnteriorAlm=horaBiometrico_matriz;
		//	tab_resumen_marcaciones.setValor(x, "biometrico_alm_entrada", ubicacion_biometrico);
	  biometrico_salida_cobmr_matriz= ubicacion_biometrico_matriz;
	    	tipo5=2;;
    	banderaAlmuerzoEntrada_matriz++;
    	horaAnterior="";
    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_matriz+"',horafinband_cobmr='OK',biometrico_alm_entrada='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));

    	
    	}
	    		
	    		

	    	}	  else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
   			    	//Salida
	    	//	tab_resumen_marcaciones.setValor(x, "horainiciohorario_cobmr", horaInicioEmpl);
	    	//	tab_resumen_marcaciones.setValor(x, "horainicioalm_cobmr", horaIniAlmEmpl);
	    	//	tab_resumen_marcaciones.setValor(x, "horafinalm_cobmr", horaFinAlmEmpl);
	    	//	tab_resumen_marcaciones.setValor(x, "horafinhorario_cobmr", horaFinEmpl);
	    	//	tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
	    	//	tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "ANTICIPADO");
	    	//	tab_plantilla_matriz.setValor(, "TIEMPOHORALM",""+(pckUtilidades.CConversion.CDbl(horariosEmpleado.getValor(i, "MIN_ALMUERZO_ASHOR"))/60));
			   // 	tab_resumen_marcaciones.setValor(x, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
 			//    	tab_resumen_marcaciones.setValor(x, "biometrico_salida_cobmr", ubicacion_biometrico);
   			    	biometrico_salida_cobmr_matriz= ubicacion_biometrico_matriz;
       			   	tipo5=1;
       			   	tipo6=0;
   			    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_matriz+"',horafinband_cobmr='ANTICIPADO',tiempoalm_cobmr="+utilitario.getFormatoNumero(tiempoAlmuerzo_matriz,2)+",biometrico_salida_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));
       			   	
       			   	
   			    }  	
				    
				    
				
   			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
   			    	//tab_resumen_marcaciones.setValor(x, "horainiciohorario_cobmr", horaInicioEmpl);
   			    	//tab_resumen_marcaciones.setValor(x, "horainicioalm_cobmr", horaIniAlmEmpl);
   			    	//tab_resumen_marcaciones.setValor(x, "horafinalm_cobmr", horaFinAlmEmpl);
   			    	//tab_resumen_marcaciones.setValor(x, "horafinhorario_cobmr", horaFinEmpl);
   			    	//tab_resumen_marcaciones.setValor(x, "horafinbiometrico_cobmr", horaBiometrico);
   			    	//tab_resumen_marcaciones.setValor(x, "horafinband_cobmr", "OK");
 			    	//tab_resumen_marcaciones.setValor(x, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo,2)+"");
 			    	//tab_resumen_marcaciones.setValor(x, "biometrico_salida_cobmr", ubicacion_biometrico);
  			    	biometrico_salida_cobmr_matriz= ubicacion_biometrico_matriz;
   			    	//tipo6=1;
   			    	tipo5=2;
   			    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_matriz+"',horafinband_cobmr='OK',tiempoalm_cobmr="+utilitario.getFormatoNumero(tiempoAlmuerzo_matriz,2)+",biometrico_salida_cobmr='"+ubicacion_biometrico_matriz+"'  where ide_cobmr ="+tab_plantilla_matriz.getValor(z, "ide_cobmr"));
   			    				 
   			    	
   			    }    
	   	 
	   	 
	   	 
	   	 
   	    //	band=true;
   	    	//valor_matriz=x;		
	   	 	}   	//for biometricos
	   	    	
		
	
	   	    	
	   	    	
	   	    	
	   	    	
				
				
				
				
				
			}





			y++;
			
			if (y==(tab_reporte.getTotalFilas())) {
				indice_matriz=fin_matriz-indice_matriz;
				valor_matriz=valor_matriz+indice_matriz;
				break;
			}
			}else{
	
			}
			valor_matriz++;
			//System.out.println("valorRelleno :"+valor_relleno);
			indice_matriz++;
			if(indice_matriz==31){
				break;
			}
		}
		
}
									
}






System.out.println("inicio ct: "+utilitario.getHoraActual());


TablaGenerica tab_plantilla_mensual=null;
if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
 tab_plantilla_mensual=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
		+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
		+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
		+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
		+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
		+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
		+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
		+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
		+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
		+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,horainiciobiometricohxe_cobmr, "
		+ "horainiciobandhxe_cobmr, horafinhorariohxe_cobmr,horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=2 "
		+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
}else{
	
	tab_plantilla_mensual=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
			+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
			+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
			+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
			+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
			+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
			+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
			+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
			+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
			+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,  "
			+ "horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
			+ "horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
			+ "FROM con_biometrico_marcaciones_resumen  "
			+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr=2  and ide_gtemp="+empleadoSeleccionado
			+ " order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
}
		
if (tab_plantilla_mensual.getTotalFilas()==0) {
	System.out.println("No existen datos para procesar en operativo");
	
}





/*
TablaGenerica tab_empleados_relleno_extra=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=2 "
		+ "and ide_gtemp in("+empleadoSeleccionado+") "
		+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
		+ "order by tipo_horario_cobmr asc, ide_gtemp asc");

*/





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////EXTRA/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/*

List<Fila> biometrico_relleno;
int banderaAlmuerzo_relleno=0;
int banderaAlmuerzoEntrada_relleno=0;
int banderaEntrada_relleno=0;
	Integer EntradaRelleno=0;
	Integer SalidaRelleno=0;
	Integer AlmuerzoEntradaRelleno=0;
	Integer AlmuerzoSalidaRelleno=0;
Integer tipo1_relleno = 0;
Integer tipo2_relleno = 0;
	Integer tipo3_relleno = 0;
	Integer tipo4_relleno= 0;
	Integer tipo5_relleno = 0;
	Integer tipo6_relleno = 0;
	Integer tipo7_relleno = 0;
Integer tipo1SinAlmuerzo_relleno= 0;
	Integer tipo2SinAlmuerzo_relleno = 0;
	Integer tipo3SinAlmuerzo_relleno = 0;
	Integer tipo4SinAlmuerzo_relleno= 0;
	double tiempoAlmuerzo_relleno=0.00;
int fin_relleno=utilitario.getDia(fecha_fin);
boolean bandRegistroUnico_relleno=false,diaAnteriorTurnoNocturno=false;
int indice=0,empleado_actual_relleno=0,	 banderaAlmuerzo=0,banderaAlmuerzoEntrada=0,banderaEntrada=0;//22,13,48,53,54
String fechaHorario="",ubicacion_biometrico="",horainicioalmbio_cobmr="";
String horaInicioEmpl_relleno="",horaIniAlmEmpl_relleno="",horaFinAlmEmpl_relleno="",min_almuerzo_relleno="",horaFinEmpl_relleno="",fechaBiometrico_relleno="",horaBiometrico_relleno="",ubicacion_biometrico_relleno="",fechaHoraBiometrico_relleno="",biometrico_entrada_cobmr_relleno="";
String empleadoActual_relleno="",tipo_horario_relleno="",biometrico_alm_salida_relleno="",biometrico_alm_entrada_relleno="",biometrico_salida_cobmr_relleno="",ide_gtgre_extra_relleno="";
TablaGenerica  tab_empleados_relleno=null;
if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){

 tab_empleados_relleno=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=2 and ide_gtgre_extra in(1,3) "
			//	+ "and ide_gtemp in(103,104,107,108,109,110,112,113,114,116,118,120,124,125,128,129,149) "
		+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
		+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
// and ide_gtemp in(1211,1212) and ide_gtemp=103 
}else{
	tab_empleados_relleno=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
			+ "FROM con_biometrico_marcaciones_resumen  "
			+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=2 "
			+ "and ide_gtemp in("+empleadoSeleccionado+") and ide_gtgre_extra in(1,3) "
			+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
			+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
}





int valor_relleno=0;
  empleadoActual_relleno="";
for (int x = 0; x < tab_empleados_relleno.getTotalFilas(); x++) {
	indice=0;
TablaGenerica tab_reporte=getTablaReporte(tab_empleados_relleno.getValor(x, "ide_gtemp"),fecha_ini, fecha_fin);//getTablaReporte(tab_empleados.getValor(j, "ide_gtemp"),fecha_ini, fecha_fin);
//System.out.println("Empleado: "+tab_empleados_relleno.getValor(x, "ide_gtemp")+"   Empleado Marcacion: "+tab_resumen_marcaciones.getValor(valor_relleno,"ide_gtemp")+"  valor relleno: "+valor_relleno);
empleadoActual_relleno=tab_empleados_relleno.getValor(x, "ide_gtemp");
empleado_actual_relleno=Integer.parseInt(empleadoActual_relleno);
	if (tab_reporte.getTotalFilas()==0) {
		valor_relleno=valor_relleno+fin_relleno;
	}else {
	
	}
	


	for (int y = 0; y < tab_reporte.getTotalFilas(); y++) {
		for (int z = valor_relleno; z < tab_plantilla_mensual.getTotalFilas(); z++) {
			//valor_relleno++;
		//System.out.println("z: "+z);
if (tab_plantilla_mensual.getValor(z, "fecha_evento_cobmr").equals(tab_reporte.getValor(y, "TO_CHAR"))) {

			//y++;
			
if((tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr")==null || tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").equals("") 
|| tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").isEmpty()) && 
(!tab_plantilla_mensual.getValor(z, "horainicioband_cobmr").isEmpty()  &&  !tab_plantilla_mensual.getValor(z, "horainicioband_cobmr").equals("LIBRE"))){
		//&&  !tab_plantilla_mensual.getValor(z, "horafinband_cobmr").equals("LIBRE"))){
	
				
	/////////////////////////////////////////////////////////////////////////////////EXTRA////////////////////////////////////////////////////////////////////////////////////////////////////			
				
		 		String fechaHoraHorarioFin ="";
		 		int banderaHoraExtra=0;
		 		
		 		//tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr"),tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
System.out.println("Error: "+tab_plantilla_mensual.getValor(z, "fecha_evento_cobmr"));

if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")==null || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()) {
	
}else 
if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()){
	ide_gtgre_extra_relleno=tab_plantilla_mensual.getValor(z, "ide_gtgre_extra");
	
	
	if (ide_gtgre_extra_relleno.equals("3")) {
		
		
		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
   		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
		
		String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
		String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR

		
	  	Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
		sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
		sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
		
		String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 

		String horaFechaInicioConsulta= diaSumaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
		String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));

		
			
 		fechaHoraHorarioFin ="";
   		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
		String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));
   		Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	     calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));
	     
		
	    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
		String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),1));

	    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));
	    
	    
	    
		   String fechaHoraHorarioEntradaSalida = fechaBiometricoAgrupadaXEmpleado+" 23:59:59";
		    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
		    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioEntradaSalida));
			
		    String fechaHoraHorarioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 00:00:00";
		    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
		    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));
			       			    
		    
		
	    biometrico_relleno = obtenerTimbreBiometricoNocturno(empleado_actual_relleno, fechaInicioReporte, "" ,fechaFinReporte,"");  		


	      	for (Fila bio1 : biometrico_relleno) {
	 		 ubicacion_biometrico="";
				fechaBiometrico_relleno = (String)bio1.getCampos()[1];
				//Hora de la tabla biometrico
				horaBiometrico_relleno ="";
				if (bio1.getCampos()[2].toString().length()==8) {
					horaBiometrico_relleno =(String)bio1.getCampos()[2];
				}else {
					horaBiometrico_relleno =(String)bio1.getCampos()[2]+":00";
				}
				fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
				//Calendario
				ubicacion_biometrico = (String)bio1.getCampos()[4];

	
	
	
	Calendar calFechaHoraBiometrico = Calendar.getInstance();
    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 

	 if (banderaHoraExtra==0) {        		
	    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada) < 0){
    			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    					+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
    		banderaHoraExtra=1;
	    }
   	    }else {
		      			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

	    	banderaHoraExtra=2;
		        				
		}

	    
	}

	
		

    	 
	    }if (ide_gtgre_extra_relleno.equals("1")) {

   		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
   		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
		
		String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
		String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
		String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
		String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));

			
 		fechaHoraHorarioFin ="";
   		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
		String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

   		Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	     calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));
		
	    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
		String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

	    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));
	    
		
		
	    biometrico_relleno = obtenerTimbreBiometricoNocturno(empleado_actual_relleno, fechaInicioReporte, "" ,fechaFinReporte,"");  		


	      	for (Fila bio1 : biometrico_relleno) {
	 		 ubicacion_biometrico="";
				fechaBiometrico_relleno = (String)bio1.getCampos()[1];
				//Hora de la tabla biometrico
				horaBiometrico_relleno ="";
				if (bio1.getCampos()[2].toString().length()==8) {
					horaBiometrico_relleno =(String)bio1.getCampos()[2];
				}else {
					horaBiometrico_relleno =(String)bio1.getCampos()[2]+":00";
				}
				fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
				//Calendario
				ubicacion_biometrico = (String)bio1.getCampos()[4];

	
	
	
	Calendar calFechaHoraBiometrico = Calendar.getInstance();
    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 

	 if (banderaHoraExtra==0) {
	    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
    			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    					+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
    		banderaHoraExtra=1;
	    }  }else {
		      			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

	    	banderaHoraExtra=2;
		        				
		

	    }
	}

	
	
	
		}else {
	
		}

				
}	else {
	
	
}	
			}
		
		
		
		
		
y++;


if (y==(tab_reporte.getTotalFilas())) {
indice=fin-indice;
valor_relleno=valor_relleno+indice;

break;
}
}else{

}
valor_relleno++;
//System.out.println("valorRelleno :"+valor_relleno);
indice++;
if(indice==31){

break;
}
	}
				

}

}


*/



/////////////////////////////////////////////////////////////////////////////////////////EXTRA   EXTRA///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



























List<Fila> biometrico_relleno;
int banderaAlmuerzo_relleno=0;
int banderaAlmuerzoEntrada_relleno=0;
int banderaEntrada_relleno=0;
	Integer EntradaRelleno=0;
	Integer SalidaRelleno=0;
	Integer AlmuerzoEntradaRelleno=0;
	Integer AlmuerzoSalidaRelleno=0;
Integer tipo1_relleno = 0;
Integer tipo2_relleno = 0;
	Integer tipo3_relleno = 0;
	Integer tipo4_relleno= 0;
	Integer tipo5_relleno = 0;
	Integer tipo6_relleno = 0;
	Integer tipo7_relleno = 0;
Integer tipo1SinAlmuerzo_relleno= 0;
	Integer tipo2SinAlmuerzo_relleno = 0;
	Integer tipo3SinAlmuerzo_relleno = 0;
	Integer tipo4SinAlmuerzo_relleno= 0;
	double tiempoAlmuerzo_relleno=0.00;
int fin_relleno=utilitario.getDia(fecha_fin);
boolean bandRegistroUnico_relleno=false,diaAnteriorTurnoNocturno=false;
int indice=0,empleado_actual_relleno=0,	 banderaAlmuerzo=0,banderaAlmuerzoEntrada=0,banderaEntrada=0;//22,13,48,53,54
String fechaHorario="",ubicacion_biometrico="",horainicioalmbio_cobmr="";
String horaInicioEmpl_relleno="",horaIniAlmEmpl_relleno="",horaFinAlmEmpl_relleno="",min_almuerzo_relleno="",horaFinEmpl_relleno="",fechaBiometrico_relleno="",horaBiometrico_relleno="",ubicacion_biometrico_relleno="",fechaHoraBiometrico_relleno="",biometrico_entrada_cobmr_relleno="";
String empleadoActual_relleno="",tipo_horario_relleno="",biometrico_alm_salida_relleno="",biometrico_alm_entrada_relleno="",biometrico_salida_cobmr_relleno="",ide_gtgre_extra_relleno="";
TablaGenerica  tab_empleados_relleno=null;
if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){

 tab_empleados_relleno=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=2 "
			//	+ "and ide_gtemp in(103,104,107,108,109,110,112,113,114,116,118,120,124,125,128,129,149) "
		+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
		+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
// and ide_gtemp in(1211,1212) and ide_gtemp=103 
}else{
	tab_empleados_relleno=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
			+ "FROM con_biometrico_marcaciones_resumen  "
			+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr=2 "
			+ "and ide_gtemp in("+empleadoSeleccionado+") "
			+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
			+ "order by tipo_horario_cobmr asc, ide_gtemp asc");
}





int valor_relleno=0;
  empleadoActual_relleno="";
for (int x = 0; x < tab_empleados_relleno.getTotalFilas(); x++) {
	indice=0;
TablaGenerica tab_reporte=getTablaReporte(tab_empleados_relleno.getValor(x, "ide_gtemp"),fecha_ini, fecha_fin);//getTablaReporte(tab_empleados.getValor(j, "ide_gtemp"),fecha_ini, fecha_fin);
//System.out.println("Empleado: "+tab_empleados_relleno.getValor(x, "ide_gtemp")+"   Empleado Marcacion: "+tab_resumen_marcaciones.getValor(valor_relleno,"ide_gtemp")+"  valor relleno: "+valor_relleno);
empleadoActual_relleno=tab_empleados_relleno.getValor(x, "ide_gtemp");
empleado_actual_relleno=Integer.parseInt(empleadoActual_relleno);
	if (tab_reporte.getTotalFilas()==0) {
		valor_relleno=valor_relleno+fin_relleno;
	}else {
	
	}
	


	for (int y = 0; y < tab_reporte.getTotalFilas(); y++) {
		for (int z = valor_relleno; z < tab_plantilla_mensual.getTotalFilas(); z++) {
			//valor_relleno++;
		//System.out.println("z: "+z);
		if (tab_plantilla_mensual.getValor(z, "fecha_evento_cobmr").equals(tab_reporte.getValor(y, "TO_CHAR"))) {

			//y++;
			
			if(tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr")==null || tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").equals("") || tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").isEmpty()){
	
				
	/////////////////////////////////////////////////////////////////////////////////EXTRA////////////////////////////////////////////////////////////////////////////////////////////////////			
				
	/*	 		String fechaHoraHorarioFin ="";
		 		int banderaHoraExtra=0;
		 		
		 		//tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr"),tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");

if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()){
	ide_gtgre_extra_relleno=tab_plantilla_mensual.getValor(z, "ide_gtgre_extra");
	
	
	if (ide_gtgre_extra_relleno.equals("3")) {
		
		
		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
   		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
		
		String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
		String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR

		
	  	Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
		sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
		sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
		
		String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 

		String horaFechaInicioConsulta= diaSumaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
		String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));

		
			
 		fechaHoraHorarioFin ="";
   		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
		String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));
   		Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	     calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));
	     
		
	    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
		String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),1));

	    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));
	    
	    
	    
		   String fechaHoraHorarioEntradaSalida = fechaBiometricoAgrupadaXEmpleado+" 23:59:59";
		    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
		    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioEntradaSalida));
			
		    String fechaHoraHorarioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 00:00:00";
		    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
		    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));
			       			    
		    
		
	    biometrico_relleno = obtenerTimbreBiometricoNocturno(empleado_actual_relleno, fechaInicioReporte, "" ,fechaFinReporte,"");  		


	      	for (Fila bio1 : biometrico_relleno) {
	 		 ubicacion_biometrico="";
				fechaBiometrico_relleno = (String)bio1.getCampos()[1];
				//Hora de la tabla biometrico
				horaBiometrico_relleno ="";
				if (bio1.getCampos()[2].toString().length()==8) {
					horaBiometrico_relleno =(String)bio1.getCampos()[2];
				}else {
					horaBiometrico_relleno =(String)bio1.getCampos()[2]+":00";
				}
				fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
				//Calendario
				ubicacion_biometrico = (String)bio1.getCampos()[4];

	
	
	
	Calendar calFechaHoraBiometrico = Calendar.getInstance();
    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 

	 if (banderaHoraExtra==0) {        		
	    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada) < 0){
    			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    					+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
    		banderaHoraExtra=1;
	    }
   	    }else {
		      			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

	    	banderaHoraExtra=2;
		        				
		}

	    
	}

	
		

    	 
	    }if (ide_gtgre_extra_relleno.equals("1")) {

   		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
   		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
		
		String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
		String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
		String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
		String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));

			
 		fechaHoraHorarioFin ="";
   		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
		String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

   		Calendar calFechaHoraHorarioFin = Calendar.getInstance();
	     calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));
		
	    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
		String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

	    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
	    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));
	    
		
		
	    biometrico_relleno = obtenerTimbreBiometricoNocturno(empleado_actual_relleno, fechaInicioReporte, "" ,fechaFinReporte,"");  		


	      	for (Fila bio1 : biometrico_relleno) {
	 		 ubicacion_biometrico="";
				fechaBiometrico_relleno = (String)bio1.getCampos()[1];
				//Hora de la tabla biometrico
				horaBiometrico_relleno ="";
				if (bio1.getCampos()[2].toString().length()==8) {
					horaBiometrico_relleno =(String)bio1.getCampos()[2];
				}else {
					horaBiometrico_relleno =(String)bio1.getCampos()[2]+":00";
				}
				fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
				//Calendario
				ubicacion_biometrico = (String)bio1.getCampos()[4];

	
	
	
	Calendar calFechaHoraBiometrico = Calendar.getInstance();
    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 

	      		
	    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
	         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
    	 if (banderaHoraExtra==0) {
    			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
    					+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
    		banderaHoraExtra=1;
   	    }else {
		      			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

	    	banderaHoraExtra=2;
		        				
		}

	    }
	}

	
	
	
		}else {
	
		}

				
}		*/
				
				
				//EXTRA
				 //		String fechaBiometrico = tab_reporte.getValor(y, "TO_CHAR");
				 		int banderaHoraExtra=0;

				 		String fechaHoraHorarioFin ="";
			       		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario

				 		
				 	//	if (diaAnteriorTurnoNocturno==true) {
				 			String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(tab_reporte.getValor(y, "TO_CHAR")), -1));

				    		TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
		    							+ "where ide_gtemp="+empleadoActual_relleno+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
		     			    
		    				String horaSalidaAnterior="";	
		    				if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
		    				if(tab_consultarSalidaDiaAnterior.getValor("horainiciohorario_cobmr").equals("22:00:00") || tab_consultarSalidaDiaAnterior.getValor("horainiciohorario_cobmr").equals("23:00:00")){
							 if (tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty()) {
						       		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario

							}else {
							 	horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
			   					//Hora Inicial tomada del horario
			     					String fechaHoraHorarioInicio = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr"); //cogo la hora y le concateno con la fecha del horario
			         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
			         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
			         			    
			         			    //Suma de minuto para el ingreso
			         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
			         			   getFechaAsyyyyMMddHHmmss(calFechaHoraHorarioInicio.getTime()) ;
					       		//fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");//cogo la hora y le concateno con la fecha del horario
					       		fechaHoraHorarioFin = getFechaAsyyyyMMddHHmmss(calFechaHoraHorarioInicio.getTime());//cogo la hora y le concateno con la fecha del horario

							}
							 
					
		    				}else {
								
							}
				    			
						}
				 		
				 		
				 		
				 		
				  		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
			       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
			       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
					    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
					    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

					    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" 24:00:00";
					    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
					    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
					    
				 		biometrico_relleno = obtenerTimbreBiometricoHoraExtra(empleado_actual_relleno, fechaHoraHorarioFin, fechaHoraHorarioHasta);
				 		//biometrico_relleno = obtenerTimbreBiometrico(empleado_actual_relleno, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);

		  	 	      	for (Fila bio : biometrico_relleno) {
		  	 	 		 ubicacion_biometrico="";
								fechaBiometrico_relleno = (String)bio.getCampos()[1];
								//Hora de la tabla biometrico
								horaBiometrico_relleno ="";
								if (bio.getCampos()[2].toString().length()==8) {
									horaBiometrico_relleno =(String)bio.getCampos()[2];
								}else {
									horaBiometrico_relleno =(String)bio.getCampos()[2]+":00";
								}
								fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
								//Calendario
								ubicacion_biometrico = (String)bio.getCampos()[4];

					
					
					
					Calendar calFechaHoraBiometrico = Calendar.getInstance();
				    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
					calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 

		  	 	      		
					    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
					         calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		            	 if (banderaHoraExtra==0) {
		            		 //tab_plantilla_mensual.setValor(z, "tiempoalm_cobmr", ""+0); 
		            		 //tab_plantilla_mensual.setValor(z, "tiempohoralm_cobmr", ""+0);
		            		 //tab_plantilla_mensual.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
		            		 //tab_plantilla_mensual.setValor(z, "horainicioband_cobmr", "EXTRA");
		            		 //tab_plantilla_mensual.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);	
		            			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
		            					+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
				    		banderaHoraExtra=1;
				   	    }else {
						        			    
				   	    	//tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", ""+0);
				   	    	//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
				   	    	//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
				   	    	//tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "EXTRA");
				   	    	//tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);
				   			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
		        					+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

					    	banderaHoraExtra=2;
						
					
					        				
						}

					    }
		  	 	      	}
					
		  	 	     diaAnteriorTurnoNocturno=false;
					
							
				
				
				
				
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
				
			}else {
				if(tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").equals("22:00:00") || tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").equals("23:00:00")){
					diaAnteriorTurnoNocturno=true;
		       		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
		       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
		       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		       		
				   	Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
						sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
						sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);
									    			
						String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 
						String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
					
			    			String horaFechaFinConsulta= diaInicio+" "+"22:00:00";    			

					String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-10));//TERMINATOR
		    			Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
		    			calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
		    			//

		    			//suma una hora a la fecha final 
		    			String horaFechaInicioConsulta= diaSumaInicio+" "+"06:00:00";    			
		    			String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),6));
		    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
		    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));	
		    			
		    			///Consulto Marcacion de dia anterior
		    			
		    
		    			
      	 	      	biometrico_relleno = obtenerTimbreBiometricoNocturno(empleado_actual_relleno, fechaInicioReporte, "" ,fechaFinReporte,"");  		

      	 	      	
      	 	      	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	 	      	
						int banderaIngreso=0;
   	    			    int banderaSalida=0;
   	    				  banderaAlmuerzo=0;
   	    				   banderaEntrada=0;
   	    				  int cont=0;
   	    				 int tipoEntrada3=0;
   	    				 int tipoEntrada4=0;
   						  banderaAlmuerzoEntrada=0;
   				    	int tipoEntrada1=1;
    			    	int tipoEntrada2=0;
    			    	int tipoNocturno=1;
   	    				boolean bandEntradaSinAlmuerzo=false;
    			    	 banderaHoraExtra1001=1;
    			    	tipo1=0;

      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	for (Fila bio : biometrico_relleno) {
						//Si es de tipo nocturno	
						//System.out.println("TIPO NOCTURNO");
   						//Ingreso a metodo
 						cont++;
   	    


   	    				
   						 ubicacion_biometrico="";
   						//recorro todos las timbradas
   						//for (Fila bio : biometrico_relleno) {
   							//fecha de la tabla biometrico
   							fechaBiometrico_relleno = (String)bio.getCampos()[1];
   							//Hora de la tabla biometrico
   							horaBiometrico_relleno ="";
   							if (bio.getCampos()[2].toString().length()==8) {
   								horaBiometrico_relleno =(String)bio.getCampos()[2];
   							}else {
   								horaBiometrico_relleno =(String)bio.getCampos()[2]+":00";
   							}
   							fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
   							//Calendario
   							ubicacion_biometrico = (String)bio.getCampos()[4];

   				
   				
   				
   				Calendar calFechaHoraBiometrico = Calendar.getInstance();
   			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
   				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 
   			   	   		
   			   	   		boolean t=false; 					      	         		
   	    					horaInicioEmpl_relleno=tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr");
   							horaIniAlmEmpl_relleno=tab_plantilla_mensual.getValor(z, "horainicioalm_cobmr");
   							horaFinAlmEmpl_relleno=tab_plantilla_mensual.getValor(z, "horafinalm_cobmr");
   							horaFinEmpl_relleno=tab_plantilla_mensual.getValor(z, "horafinhorario_cobmr");	
   	     					
   	     					
   	     					//Hora Inicial tomada del horario
   	     					String fechaHoraHorarioInicio = fechaBiometricoAgrupadaXEmpleado+" "+horaInicioEmpl_relleno; //cogo la hora y le concateno con la fecha del horario
   	         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
   	         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
   	         			    
   	         			    //Suma de minuto para el ingreso
   	         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
   	         			    
   	         			    
   	         			    
   	         			    //Hora desde Inicio de Entrada de Horario
   	       			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio),-8));
   	      	    		     Calendar calHoraFechaBase= Calendar.getInstance();
   	      	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteBase));
   	  			
   	         			    
   	     					
   	     					//Hora Final tomada del horario
   	     					String fechaHoraHorarioFin = fechaBiometricoAgrupadaXEmpleado+" "+horaFinEmpl_relleno;//cogo la hora y le concateno con la fecha del horario
   	         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
   	         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

   	         			    //String fechaHoraHorarioIniAlmuerzo = fechaBiometricoAgrupadaXEmpleado+" "+horaIniAlmEmpl_relleno;
   	         			    //Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
   	         			    //calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
   	       			        //calFechaHoraHorarioIniAlmuerzo.add(Calendar.MINUTE, -15);	    
   	       			    
   	       			    
   	   	         		//String fechaHoraHorarioInicial = fechaBiometricoAgrupadaXEmpleado+" "+horaFinAlmEmpl_relleno;//cogo la hora y le concateno con la fecha del horario
   	   	         		//Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
   	   	         		//calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicial));
   	   	         		//calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    
   					    //String fechaHoraHorarioFinAlmuerzo = fechaBiometrico_relleno+" "+horaFinAlmEmpl_relleno;
   	         			//Date finAlmuerzo=null;
   	    			
   	    		
   	    			
   	    			
   	    			
   	    			
   	    			//Parametro hasta para el sql de la consulta cuando es por turno nocturno
   	    			String horaFechaDiaInicio=diaInicio+" 20:00:00";
   	    			String horaFechaDiaInicioConsulta=diaInicio+" 24:00:00";
   	    		
   	    			//Parametro antes para el sql de la consulta cuando es por turno nocturno
   	    			String horaFechaSumaDia=diaSumaInicio+" 04:00:00";
   	    			String horaFechaSumaDia1=diaSumaInicio+" 08:00:00";
   	    			
   	    			
   	    			//suma una hora a la fecha inicial  22:00:00
   	    		//	String horaFechaFinConsulta= diaInicio+" "+horaInicioEmpl;    			
   	    		//	String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-1));
   	    		//	Calendar calHoraFechaInicioConsulta = Calendar.getInstance();
   	    		//	calHoraFechaInicioConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
   						
   	    			//

   	    			//suma una hora a la fecha final 
   	    	   //    String horaFechaInicioConsulta= diaSumaInicio+" "+horaFinEmpl;    			
   	    	  //	 String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),1));
   	    	 //		Calendar calHoraFechaFinConsulta = Calendar.getInstance();
   	    	//		calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmm(fechaInicioReporte));
   						
   	    			
   	    			
   	    			
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7  	    			
   	    			
   	    			
   	    			
   	 		
					
					
			//		String fechaHoraHorarioInicio = fechaBiometrico+" "+horaInicioEmpl1; //cogo la hora y le concateno con la fecha del horario
	    			//String fechaHoraHorarioInicio= getFechaAsyyyyMMddHHmm(sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio1),Integer.parseInt(minuto_tolerancia_astur)));

					
					
					Calendar calFechaHoraHorarioInicio1 = Calendar.getInstance();//
    			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));
    			    
    			    //Suma de minuto para el ingreso
     			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
					
					// String fechaHoraHorarioFin = fechaBiometrico+" "+horaFinEmpl;//cogo la hora y le concateno con la fecha del horario
    			    //Calendar calFechaHoraHorarioFin1 = Calendar.getInstance();
    			    //calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

    			    
    			  //Desde hasta puede hora de salida para el horario nocturno
  			   String fechaHoraHorarioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 10:00:00";
    			    Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
    			    calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));
					
    			    String fechaHoraHorarioInicioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 04:00:00";
    			    Calendar calFechaHoraHorarioInicioFinSalida = Calendar.getInstance();
    			    calFechaHoraHorarioInicioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));
					       			    
					//Desde hasta puede hora de ingreso para el horario nocturno
    			    String fechaHoraHorarioFinEntrada = fechaBiometrico_relleno+" 24:00:00";
    			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
    			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinEntrada));
   			    
    				String fechaHoraHorarioInicioEntrada = fechaBiometrico_relleno+" 00:00:00";
    			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
    			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicioEntrada));
   				    
    			    
    			    Calendar calSumaDia = Calendar.getInstance();
    			    calSumaDia.setTime(getFechaAsyyyyMMddHHmm(horaFechaSumaDia1));
   				    
    			    
    			   //Prueba horario nocturno
    			    //variable que guarda la hora desde 00:00: del dia siguiente
    			    String fechaHoraCeroHoras = fechaBiometrico_relleno+" 00:00:00";
    			    Calendar fecHoraInicio = Calendar.getInstance();
    			    fecHoraInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicioEntrada));
   				    
    			    
    			    
    				//Siguiente
        			String fecha= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado);
        			String fechayhora=fecha+" 08:00:00";
        		    Calendar calFin = Calendar.getInstance();//
    			    calFin.setTime(getFechaAsyyyyMMddHHmm(fechayhora));//
        			
    			    
    			  //Siguiente
        			String fecha1= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
        			String fechayhora12=fecha1+" 22:00:00";
        		    Calendar calEntrada = Calendar.getInstance();//
    			    calEntrada.setTime(getFechaAsyyyyMMddHHmm(fechayhora12));//
    			    
    			    
    			    
    			    

    			  	if (cont==1) {

  			  		if (biometrico_relleno.size()==1) { 		
  			  		calFechaHoraBiometrico.getTime();calFechaHoraHorarioInicioFinSalida.getTime();calFechaHoraHorarioInicio.getTime();
        			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

        			    ///	tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
        			   // 	tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "OK");
         			   // 	tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
        			   // 	tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
        			   // 	tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);
        			   // 	tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "SIN TIMBRE");
            			    	tipo1=1;
            			    	tipoEntrada1=1;
            			    	tipoEntrada2=0;
            			    	tipoNocturno=1;
        			    	banderaHoraExtra1001=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='OK',biometrico_entrada_cobmr='"+ubicacion_biometrico+"',horafinband_cobmr='SIN TIMBRE'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

							}
        			  		
    				else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0 &&
    			    		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada)<=0){
                           
    					//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
    					//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ATRASADO");
    					//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
    					//tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
    					//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);
        			  		
    			    	tipo1=1;
        			    tipoEntrada1=1;
    			    	tipoEntrada2=0;
    			    	tipoNocturno=1;
    			    	banderaHoraExtra1001=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico+"',horafinband_cobmr='SIN TIMBRE'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

    				
    				
    				}
        			  		
        			  		
        			  		
  			  		}else{
  			  			
  			  		calFechaHoraBiometrico.getTime();calFechaHoraHorarioInicioFinSalida.getTime();calFechaHoraHorarioInicio.getTime();
    			    Integer tipoEvento = 0;
    			    if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){

    			    	//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
    			    	//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "OK");
    			    	//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
  	            		//tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
  	            		//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);

            			    	tipo1=1;
            			    	tipoEntrada1=1;
            			    	tipoEntrada2=0;
            			    	tipoNocturno=1;
 utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
 + " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='OK',biometrico_entrada_cobmr='"+ubicacion_biometrico+"',horafinband_cobmr='SIN TIMBRE'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
            			    	
             
            			    }
    			    
    				else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0 &&
    			    		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada)<=0){
                           
    					//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
    					//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ATRASADO");
    					//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
    					//tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
    					//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);

    			    	tipo1=1;
        			    tipoEntrada1=1;
    			    	tipoEntrada2=0;
    			    	tipoNocturno=1;
    			    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico+"',horafinband_cobmr='SIN TIMBRE'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

    			   }
            			    
            			      
    			 	
  			  	}	//contador de marcaciones mayor a uno
  			  		
    			  	}	
    			 	
    			 	if (cont>1) {
            				
             			    //mayor a hora: 0:00:00
            			    //menor a las 6 de la manaña	
            			     if(calFechaHoraBiometrico.compareTo(fecHoraInicio) >0 &&
            			    	     calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)<0){
            			    	 //tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
            			    	 //tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ANTICIPADO");
            			    	 //tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
            			    	 //tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
            			    	 //tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);

utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='ANTICIPADO',biometrico_salida_cobmr='"+ubicacion_biometrico+"',horafinband_cobmr='SIN TIMBRE'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
			    
          			    		if (tipoNocturno==0) {
          			    		  tipo1=3;
								}else if(tipoNocturno==1){
	            				    tipo1=2;}
            			    //mayor que las 06:00:00
            			    //	y menor que 08:00:00
            			    }else if(calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinSalida)>=0 
            			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicioFinSalida)<=0){
            			    	//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", ""+0);
            			    	//tab_resumen_marcaciones.setValor(z, "tiempohoralm_cobmr", "0");
            			    	//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
            			    	//tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "OK");
            			    	//tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);
      			
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='OK',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
            			    	
            			    	
            			    	
            			  if (tipoNocturno==0) {
			    		  tipo1=3;
						}else if(tipoNocturno==1){
        				    tipo1=2;}
            			    }   										
            				}					
				}	//for
 						
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
      	 	      	
				}else {
					
					
					
	       		    String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y,"TO_CHAR");
				       		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
				       		
				       		 String fechaHoraHorarioFin ="";
				       		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario


				       		
				       		String diaAnterior=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(tab_reporte.getValor(y, "TO_CHAR")), -1));

				    		TablaGenerica tab_consultarSalidaDiaAnterior = utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
		    							+ "where ide_gtemp="+empleadoActual_relleno+" and fecha_evento_cobmr = '"+diaAnterior+"' order by ide_cobmr desc");
		     			    
		    				String horaSalidaAnterior="";	
		    				if (tab_consultarSalidaDiaAnterior.getTotalFilas()>0) {
		    				if(tab_consultarSalidaDiaAnterior.getValor("horainiciohorario_cobmr").equals("22:00:00") || tab_consultarSalidaDiaAnterior.getValor("horainiciohorario_cobmr").equals("23:00:00")){
							 if (tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr")==null || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").equals("") || tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr").isEmpty()) {
						       		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+"0:00:00";//cogo la hora y le concateno con la fecha del horario

							}else {
							 	horaSalidaAnterior=tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");
			   					//Hora Inicial tomada del horario
			     					String fechaHoraHorarioInicio = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr"); //cogo la hora y le concateno con la fecha del horario
			         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
			         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
			         			    
			         			    //Suma de minuto para el ingreso
			         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
			         			   getFechaAsyyyyMMddHHmmss(calFechaHoraHorarioInicio.getTime()) ;
					       		//fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_consultarSalidaDiaAnterior.getValor("horafinbiometrico_cobmr");//cogo la hora y le concateno con la fecha del horario
					       		fechaHoraHorarioFin = getFechaAsyyyyMMddHHmmss(calFechaHoraHorarioInicio.getTime());//cogo la hora y le concateno con la fecha del horario

							}
							 
					
		    				}else {
								
							}
				    			
						}   		
				       		
		    		 		
		    		  	//	String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
		    	       		//System.out.println("Fecha"+fechaBiometricoAgrupadaXEmpleado);
		    	       //		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		    			//    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		    			   // calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));

		    			    String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" 24:00:00";
		    			    Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		    			    calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta));
		    			    
		    		 		biometrico_relleno = obtenerTimbreBiometricoHoraExtra(empleado_actual_relleno, fechaHoraHorarioFin, fechaHoraHorarioHasta);
				       		
				       		
				       		
				       		
				       		
		   	   		//biometrico_relleno = obtenerTimbreBiometrico(empleado_actual_relleno, dateFechaInicioReporteAgrupadaXEmpleado, dateFechaInicioReporteAgrupadaXEmpleado);
		   	   		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		   	   				   	   		
      				


					boolean bandEntradaSinAlmuerzo=false;
					tipo1_relleno=0;
					 banderaAlmuerzo=0;
					 banderaAlmuerzoEntrada=0;
					 banderaEntrada=0;
					 ubicacion_biometrico="";
					//recorro todos las timbradas
					for (Fila bio : biometrico_relleno) {
						//fecha de la tabla biometrico
						fechaBiometrico_relleno = (String)bio.getCampos()[1];
						//Hora de la tabla biometrico
						horaBiometrico_relleno ="";
						if (bio.getCampos()[2].toString().length()==8) {
							horaBiometrico_relleno =(String)bio.getCampos()[2];
						}else {
							horaBiometrico_relleno =(String)bio.getCampos()[2]+":00";
						}
						fechaHoraBiometrico_relleno = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno;
						//Calendario
						ubicacion_biometrico = (String)bio.getCampos()[4];

			
			
			
			Calendar calFechaHoraBiometrico = Calendar.getInstance();
		    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
			calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno)); 
		   	   		
		   	   		boolean t=false; 					      	         		
    					horaInicioEmpl_relleno=tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr");
						horaIniAlmEmpl_relleno=tab_plantilla_mensual.getValor(z, "horainicioalm_cobmr");
						horaFinAlmEmpl_relleno=tab_plantilla_mensual.getValor(z, "horafinalm_cobmr");
						horaFinEmpl_relleno=tab_plantilla_mensual.getValor(z, "horafinhorario_cobmr");	
     					
     					
     					//Hora Inicial tomada del horario
     					String fechaHoraHorarioInicio = fechaBiometricoAgrupadaXEmpleado+" "+horaInicioEmpl_relleno; //cogo la hora y le concateno con la fecha del horario
         			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
         			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio));//
         			    
         			    //Suma de minuto para el ingreso
         			    calFechaHoraHorarioInicio.add(Calendar.MINUTE, 1);
         			    
         			    
         			    
         			    //Hora desde Inicio de Entrada de Horario
       			     String fechaFinReporteBase= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicio),-8));
      	    		     Calendar calHoraFechaBase= Calendar.getInstance();
      	    		     calHoraFechaBase.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteBase));
  			
         			    
     					
     					//Hora Final tomada del horario se elimino el String
      	    		   fechaHoraHorarioFin="";
     					fechaHoraHorarioFin = fechaBiometricoAgrupadaXEmpleado+" "+horaFinEmpl_relleno;//cogo la hora y le concateno con la fecha del horario
         			    Calendar calFechaHoraHorarioFin = Calendar.getInstance();
         			    calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin));


      			   
	   	         //   double tiempoAlmuerzoHorario= (pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60);

      			   //Calendar calFechaHoraHorarioFinAlmuerzo=Calendar.getInstance();		   
	   	         //			if (tiempoAlmuerzoHorario==0.5) {
	   	         	
        		//	    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo)); 
        		//	    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 30);
				//		}else if (tiempoAlmuerzoHorario==1) {
						
   	        // 			    calFechaHoraHorarioFinAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinAlmuerzo)); 
   	        // 			    calFechaHoraHorarioFinAlmuerzo.add(Calendar.MINUTE, 60);
			///		}
	   	         		
      			   	
         			    //Condicion si no existe hora de almuerzo
         			   if ((horaIniAlmEmpl_relleno.equals("00:00:00") || horaIniAlmEmpl_relleno.equals("S/A")) || (horaFinAlmEmpl_relleno.equals("00:00:00") || horaFinAlmEmpl_relleno.equals("S/A"))) {
         				   
         			       
         			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
         			    	if (tipo1_relleno==0) {
         			    	//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
         			    	//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "OK");
     			    		//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);          	                 			    
         			    	tipo1_relleno=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='OK',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
         			    			            			             			    	
         			    	
         			    	}
         			    }
      
        				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {

        					if (tipo1_relleno==0) {
        					//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
					    	//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ATRASADO");
     			    		//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);      			    
             			    tipo1_relleno=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
             					           			    			            			             			    	
             					          
             			    
             			    
        					}else {
        						
        						
        					if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {
        					//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
             			    //tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "OK");
             			    //tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);
             			    tipo1_relleno=2;
 utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
 + " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='OK',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

        					
        					
        					}  
        					else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0) {
        				//	tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
                         //   tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "ANTICIPADO");
                     	 //   tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);
	                     	tipo1_relleno=2;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
 + " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='ANTICIPADO',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

        					
        					
        					}
							}
        					
        					//else de caso
         			                 			    
         				}else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0) {
         					if (tipo1_relleno==0) {
         				//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
       			    	//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ATRASADO");
   			    		//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr",ubicacion_biometrico);
                     	tipo1_relleno=4;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
                     	

         					}else {
         				//		tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
         			//			tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "OK");
         			//			tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr",ubicacion_biometrico);
                 			    tipo1_relleno=2;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='OK',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

                 			   
                 			    
         				}

         				}
      			   }//fin de condicion  si no tiene ALMUERZO
		
         			   else {
         				  //Caso contrario si tiene almuerzo
            			    
            			    String fechaHoraHorarioIniAlmuerzo = fechaBiometricoAgrupadaXEmpleado+" "+horaIniAlmEmpl_relleno;
             			    Calendar calFechaHoraHorarioIniAlmuerzo = Calendar.getInstance();
             			    calFechaHoraHorarioIniAlmuerzo.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioIniAlmuerzo));
           			    calFechaHoraHorarioIniAlmuerzo.add(Calendar.MINUTE, -15);	    
           			    
           			    
       	         		String fechaHoraHorarioInicial = fechaBiometricoAgrupadaXEmpleado+" "+horaFinAlmEmpl_relleno;//cogo la hora y le concateno con la fecha del horario
       	         		Calendar calFechaHoraHorarioFinInicial = Calendar.getInstance();
       	         		calFechaHoraHorarioFinInicial.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioInicial));
       	         		calFechaHoraHorarioFinInicial.add(Calendar.MINUTE, 15);	    
    				    String fechaHoraHorarioFinAlmuerzo = fechaBiometrico_relleno+" "+horaFinAlmEmpl_relleno;
             			Date finAlmuerzo=null;
             			    
         				   
         				   
            			    EntradaRelleno=6;
	             			    if (calFechaHoraBiometrico.compareTo(calHoraFechaBase)>=0 && calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
            			    
            			  //  if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) <= 0){
            			    	if (banderaEntrada==0) {
                               //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
            			    	//tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
            			    	//tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "OK");
					    		//tab_resumen_marcaciones.setValor(z, "horainicioalmbio_cobmr", "SIN TIMBRE");
					    		//tab_resumen_marcaciones.setValor(z, "horafinalmbio_cobmr", "SIN TIMBRE");
            			       	biometrico_entrada_cobmr_relleno= ubicacion_biometrico;
            			       	//tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr", ubicacion_biometrico);
            			    	tipo1_relleno=2;
            			    	EntradaRelleno=1;
            			    	banderaEntrada=1;
            			    	
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='OK',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

            			    	
            			    	}
            			    }
         
         				else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioInicio) > 0
   								&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) < 0) {
           					if (banderaEntrada==0) {
           					//	tab_resumen_marcaciones.setValor(z, "horainiciobiometrico_cobmr", horaBiometrico_relleno);
           					//	tab_resumen_marcaciones.setValor(z, "horainicioband_cobmr", "ATRASADO");
           					//	tab_resumen_marcaciones.setValor(z, "horainicioalmbio_cobmr", "SIN TIMBRE");
           					//	tab_resumen_marcaciones.setValor(z, "horafinalmbio_cobmr", "SIN TIMBRE");
     			    			biometrico_entrada_cobmr_relleno= ubicacion_biometrico;
     			    	   //	tab_resumen_marcaciones.setValor(z, "biometrico_entrada_cobmr", ubicacion_biometrico);
            			    	//tipo1=1;
            			    	//tipo2=1;
            			    	tipo1_relleno=2;
            			    	banderaEntrada++;
            			    	EntradaRelleno=1;

utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno+"',horainicioband_cobmr='ATRASADO',biometrico_entrada_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

           					
           					}
            				}

             			    
             			else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioIniAlmuerzo) >= 0
		       			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) <= 0 && banderaAlmuerzo==0){
	       			    	//Almuerzo
	       			    	
	       			    	if (banderaAlmuerzo == 0){
	                              //tab_reporte.setValor(itemReporte,"CODIGO",""+(contador));
	       			    		//tab_resumen_marcaciones.setValor(z, "horainicioalmbio_cobmr", horaBiometrico_relleno);
	       			    		//tab_resumen_marcaciones.setValor(z, "horafinalmbio_cobmr", "SIN TIMBRE");
	       			    		//tab_resumen_marcaciones.setValor(z, "biometrico_alm_salida", ubicacion_biometrico);
   		      					horaAnterior=horaBiometrico_relleno;
	           			    	banderaAlmuerzo++;
	           			    	tipo3_relleno=1;
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	           			    			+ " horainicioalmbio_cobmr ='"+horaBiometrico_relleno+"',horafinalmbio_cobmr='SIN TIMBRE',biometrico_alm_salida='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

horainicioalmbio_cobmr=horaBiometrico_relleno;
	       			    	}
	       			    	
	       			    	
	       			    	
	       			    	
	       			    }else if (banderaAlmuerzo==1 && horaBiometrico_relleno.compareTo(horaAnterior)>0  && horaBiometrico_relleno.compareTo(horaFinEmpl_relleno)<0 && banderaAlmuerzoEntrada==0){
	
		   	     			String uno= fechaBiometrico_relleno+" "+horaAnterior; //cogo la hora y le concateno con la fecha del horario
		   	         		String dos = fechaBiometrico_relleno+" "+horaFinAlmEmpl_relleno;
		   	     			
		   	     				Calendar caluno = Calendar.getInstance();//
		   	         			Calendar caldos = Calendar.getInstance();//
		   	         			caluno.setTime(getFechaAsyyyyMMddHHmm(dos));//
		   	         			caldos.setTime(getFechaAsyyyyMMddHHmm(uno));//
		   	         			//Suma de minuto para el ingreso
		   	         			
		   	         		//	if((pckUtilidades.CConversion.CDbl(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))/60)==1){
		   	         			caluno.add(Calendar.MINUTE, 60);	
		   	         	//		}else if (pckUtilidades.CConversion.CInt(horariosEmpleadoMes.getValor(i, "MIN_ALMUERZO_ASHOR"))==0.5) {
		   	         		//	caluno.add(Calendar.MINUTE, 30);	
							//	}else {
									
								//}
		   	         			
		   	         		
	       			    	
	       			    		if (calFechaHoraBiometrico.compareTo(caldos) > 0
   		       			    		&& calFechaHoraBiometrico.compareTo(caluno) <= 0 && banderaAlmuerzo==1){
	       			    			//tab_resumen_marcaciones.setValor(z, "horafinalmbio_cobmr", horaBiometrico_relleno  );
	       			    			Date fechaIniAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico_relleno+" "+horainicioalmbio_cobmr);
		           			    	Date fechaFinAlmBio = getFechaAsyyyyMMddHHmm(fechaBiometrico_relleno+" "+horaBiometrico_relleno);
			           			    tiempoAlmuerzo_relleno = (obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
				 			    	//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+"");
				   					//tab_resumen_marcaciones.setValor(z, "biometrico_alm_entrada", ubicacion_biometrico);
	       			
	           			    	//Validacion si se pasa el almuerzo
	           			    	tipo4_relleno=1;
	           			    	banderaAlmuerzoEntrada++;
	           			    	horaAnterior="";
	           			    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	           			    			+ " horafinalmbio_cobmr ='"+horaBiometrico_relleno+"',tiempoalm_cobmr="+utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+",biometrico_alm_entrada='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

	       			    		}
	       			    		
	       			    		
	       				    	if (calFechaHoraBiometrico.compareTo(caldos) > 0
			    			    		&& calFechaHoraBiometrico.compareTo(caluno) >= 0 && banderaAlmuerzo==1 &&  calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){						
							    	//tab_resumen_marcaciones.setValor(z, "horafinalmbio_cobmr", "SIN TIMBRE");
						    		//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
						    		//tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "ANTICIPADO");
			 					horaAnteriorAlm=horaBiometrico_relleno;
			 					//tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr", ubicacion_biometrico);
			  			    	biometrico_salida_cobmr_relleno= ubicacion_biometrico; 		
			  			    	tipo5_relleno=1;
			      			   	tipo6_relleno=0;
			   			    	banderaAlmuerzoEntrada++;
			   			    	horaAnterior="";
			   			    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
	           			    			+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinalmbio_cobmr='SIN TIMBRE',horafinband_cobmr='ANTICIPADO',biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

						    	}
						 
							if (calFechaHoraBiometrico.compareTo(caldos) > 0
			  			    		&& calFechaHoraBiometrico.compareTo(caluno) >= 0 && banderaAlmuerzo==1 &&  calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
								//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
								//tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "OK");
								horaAnteriorAlm=horaBiometrico_relleno;
								//tab_resumen_marcaciones.setValor(z, "biometrico_alm_entrada", ubicacion_biometrico);
						  biometrico_salida_cobmr_relleno= ubicacion_biometrico;
						    	tipo5_relleno=2;
					    	banderaAlmuerzoEntrada++;
					    	horaAnterior="";
					    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
           			    			+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='OK',biometrico_alm_entrada='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

							
							}
	       			    		
	       			    		
	       			    		
	       			    	}
	           			   	
 
          			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinInicial) > 0
            			    		&& calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) < 0){
            			    	//Salida
          		                			
          			    		//tab_resumen_marcaciones.setValor(z, "horainiciohorario_cobmr", horaInicioEmpl_relleno);
					    		//tab_resumen_marcaciones.setValor(z, "horainicioalm_cobmr", horaIniAlmEmpl_relleno);
					    		//tab_resumen_marcaciones.setValor(z, "horafinalm_cobmr", horaFinAlmEmpl_relleno);
					    		//tab_resumen_marcaciones.setValor(z, "horafinhorario_cobmr", horaFinEmpl_relleno);
					    	//	tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
					    	//	tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "ANTICIPADO");
			 			   // 	tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+"");
			     			//    tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr", ubicacion_biometrico);
			       			    biometrico_salida_cobmr_relleno= ubicacion_biometrico;
                 			    tipo1_relleno=2;
                			    SalidaRelleno=1;
                			   	EntradaRelleno=4;
    					    	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
  + " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='ANTICIPADO',tiempoalm_cobmr="+utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+",biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
            			    	
            			    }  	
            			    else if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0){
            			    	//tab_resumen_marcaciones.setValor(z, "horainiciohorario_cobmr", horaInicioEmpl_relleno);
		       			    	//tab_resumen_marcaciones.setValor(z, "horainicioalm_cobmr", horaIniAlmEmpl_relleno);
		       			    	//tab_resumen_marcaciones.setValor(z, "horafinalm_cobmr", horaFinAlmEmpl_relleno);
		       			    	//tab_resumen_marcaciones.setValor(z, "horafinhorario_cobmr", horaFinEmpl_relleno);
		       			    	//tab_resumen_marcaciones.setValor(z, "horafinbiometrico_cobmr", horaBiometrico_relleno);
		       			    	//tab_resumen_marcaciones.setValor(z, "horafinband_cobmr", "OK");
		     			    	//tab_resumen_marcaciones.setValor(z, "tiempoalm_cobmr", utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+"");
		     			    	//tab_resumen_marcaciones.setValor(z, "biometrico_salida_cobmr", ubicacion_biometrico);
		      			    	biometrico_salida_cobmr_relleno= ubicacion_biometrico;
               			    	tipo1_relleno=2;

                			   	SalidaRelleno=1;
                				EntradaRelleno=5;
 	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
  + " horafinbiometrico_cobmr ='"+horaBiometrico_relleno+"',horafinband_cobmr='OK',tiempoalm_cobmr="+utilitario.getFormatoNumero(tiempoAlmuerzo_relleno,2)+",biometrico_salida_cobmr='"+ubicacion_biometrico+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
      					    		  
            			    }    
        				
            			
					}//Fin de ciclo si contiene almuerzo
         			   
         			   
         
         			 
		//Si es igual 
   					}
		   	   		
				}//ELSE 	
		   	   		
		   	   		
			
			
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
		   	   		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	   	   		
		   	   		
				}
			
			
							    				
			
			y++;
			
			
			if (y==(tab_reporte.getTotalFilas())) {
				indice=fin-indice;
				valor_relleno=valor_relleno+indice;
				
				break;
			}
			}else{
	
			}
			valor_relleno++;
			//System.out.println("valorRelleno :"+valor_relleno);
			indice++;
			if(indice==31){
				
				break;
			}
		}
		
}
	//tab_resumen_marcaciones.guardar();
	//guardarPantalla();							
}







//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////EXTRA/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////








//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////// PLANIFICACION HORAS EXTRA /////////////////////////////////////////////////////////////////////////////////////////////////////////
	

TablaGenerica TablaBiometrico = null;



if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){

TablaBiometrico =utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr, "
+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
+ "horafinband_cobmr, novedad_cobmr, aprueba_hora_extra_cobmr, "
+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida, "
+ "biometrico_salida_cobmr, tipo_horario_cobmr, horas25_loep_cobmr, "
+ "horas60_loep_cobmr, horas50_ct_cobmr, horainiciohorariohxe_cobmr, "
+ "horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
+ "horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where  fecha_evento_cobmr between   '"+nuevaFechaInicio+"' and   '"+nuevaFechaFin+"'  "
//+ "tipo_horario_cobmr=1 "
+ "order by ide_gtemp asc,fecha_evento_cobmr asc");

}else {

	TablaBiometrico=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
	+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
	+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr, "
	+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
	+ "horafinband_cobmr, novedad_cobmr, aprueba_hora_extra_cobmr, "
	+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
	+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
	+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida, "
	+ "biometrico_salida_cobmr, tipo_horario_cobmr, horas25_loep_cobmr, "
	+ "horas60_loep_cobmr, horas50_ct_cobmr, horainiciohorariohxe_cobmr, "
	+ "horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
	+ "horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
	+ "FROM con_biometrico_marcaciones_resumen  "
	+ "where  fecha_evento_cobmr between   '"+nuevaFechaInicio+"' and   '"+nuevaFechaFin+"' and "
	//+ "tipo_horario_cobmr=1 "
	+ "ide_gtemp in("+empleadoSeleccionado+") "
	+ "order by ide_gtemp asc,fecha_evento_cobmr asc");
}





TablaGenerica  turnoMatrizExtra=null;
int valorDia=0;
String dia="",ide_gtgre_extra="";
for (int j = 0; j < TablaBiometrico.getTotalFilas(); j++) {	
	turnoMatrizExtra=utilitario.consultar("select  horario.ide_ashor,  "
			+ "HORARIO.HORA_INICIAL_ASHOR, "
			+ "HORARIO.HORA_FINAL_ASHOR,  "
			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR,  "
			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
			+ "HORARIO.NOM_ASHOR,  "
			+ "tur.ide_gtgre   "
			+ "from  "
			+ "asi_turnos_horario turno  "
			+ "left join asi_horario HORARIO  on horario.ide_ashor=turno.ide_ashor  "
			+ "left join asi_turnos tur on tur.ide_astur=turno.ide_astur "
			+ "where  turno.IDE_ASTUR in(select dia"+utilitario.getDia(TablaBiometrico.getValor(j, "fecha_evento_cobmr"))+" from asi_horario_mes_empleado_hxe where ide_gemes="+utilitario.getMes(fecha_ini)+"  "
			+ "and ide_gtemp="+TablaBiometrico.getValor(j,"ide_gtemp")+" and ide_geani="+anio+" ) "
			+ "and horario.activo_ashor=true  "
			+ "ORDER BY HORARIO.IDE_ASHOR ASC");		

	if (turnoMatrizExtra.getTotalFilas()>0) {	
			hora_inicial_ashor_horario=turnoMatrizExtra.getValor("hora_inicial_ashor");
			hora_final_ashor_horario=turnoMatrizExtra.getValor("hora_final_ashor");
			ide_gtgre_extra=turnoMatrizExtra.getValor("ide_gtgre");
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
					+ " horainiciohorariohxe_cobmr ='"+hora_inicial_ashor_horario+"',horainiciobandhxe_cobmr='EXTRA',"
					+ " horafinhorariohxe_cobmr ='"+hora_final_ashor_horario+"',horafinbandhxe_cobmr='EXTRA',ide_gtgre_extra="+ide_gtgre_extra+" "
					+ " where ide_cobmr ="+TablaBiometrico.getValor(j, "ide_cobmr"));
	}			
	}





utilitario.agregarMensaje("Se creo plantilla de Horas Extra", "Se genero correctamente");






////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



















List<Fila> biometrico_relleno_;
int banderaAlmuerzo_relleno_=0;
int banderaAlmuerzoEntrada_relleno_=0;
int banderaEntrada_relleno_=0;
Integer EntradaRelleno_=0;
Integer SalidaRelleno_=0;
Integer AlmuerzoEntradaRelleno_=0;
Integer AlmuerzoSalidaRelleno_=0;
Integer tipo1_relleno_ = 0;
Integer tipo2_relleno_ = 0;
Integer tipo3_relleno_ = 0;
Integer tipo4_relleno_= 0;
Integer tipo5_relleno_ = 0;
Integer tipo6_relleno_ = 0;
Integer tipo7_relleno_ = 0;
Integer tipo1SinAlmuerzo_relleno_= 0;
Integer tipo2SinAlmuerzo_relleno_ = 0;
Integer tipo3SinAlmuerzo_relleno_ = 0;
Integer tipo4SinAlmuerzo_relleno_= 0;
double tiempoAlmuerzo_relleno_=0.00;
int fin_relleno_=utilitario.getDia(fecha_fin);
boolean bandRegistroUnico_relleno_=false,diaAnteriorTurnoNocturno_=false;
int indice_=0,empleado_actual_relleno_=0,banderaAlmuerzo_=0,banderaAlmuerzoEntrada_=0,banderaEntrada_=0;//22,13,48,53,54
String fechaHorario_="",ubicacion_biometrico_="",horainicioalmbio_cobmr_="";
String horaInicioEmpl_relleno_="",horaIniAlmEmpl_relleno_="",horaFinAlmEmpl_relleno_="",min_almuerzo_relleno_="",horaFinEmpl_relleno_="",fechaBiometrico_relleno_="",horaBiometrico_relleno_="",ubicacion_biometrico_relleno_="",fechaHoraBiometrico_relleno_="",biometrico_entrada_cobmr_relleno_="";
String empleadoActual_relleno_="",tipo_horario_relleno_="",biometrico_alm_salida_relleno_="",biometrico_alm_entrada_relleno_="",biometrico_salida_cobmr_relleno_="",ide_gtgre_extra_relleno_="";
String empleado="";



TablaGenerica tab_empleados_relleno2=null;
TablaGenerica tab_plantilla_mensual_hxe=null;
if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
tab_empleados_relleno2=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr in(1,2) and ide_gtgre_extra in(1,3) "
//+ "and ide_gtemp in("+empleadoSeleccionado+") "        //in(103,104,107,108,109,110,112,113,114,116,118,120,124,125,128,129,149) "
+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
+ "order by IDE_GTEMP ASC");
//and ide_gtemp in(1211,1212) and ide_gtemp=103 
}else{
tab_empleados_relleno2=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr in(1,2) and ide_gtgre_extra in(1,3) "
+ "and ide_gtemp in("+empleadoSeleccionado+") and ide_gtgre_extra in(1,3) "
+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
+ "order by IDE_GTEMP ASC");
}

for (int i = 0; i <tab_empleados_relleno2.getTotalFilas(); i++) {
	empleado_actual_relleno_=Integer.parseInt(tab_empleados_relleno2.getValor(i, "ide_gtemp"));
	tab_plantilla_mensual_hxe=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
			+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
			+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
			+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
			+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
			+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
			+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
			+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
			+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
			+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,  "
			+ "horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
			+ "horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
			+ "FROM con_biometrico_marcaciones_resumen  "
			+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr in (1,2)  and ide_gtemp="+empleado_actual_relleno_+" and ide_gtgre_extra in(1,3) "
			+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");


	
			for (int  z= 0; z < tab_plantilla_mensual_hxe.getTotalFilas(); z++) {
	

				
				if((tab_plantilla_mensual_hxe.getValor(z, "horainiciohorario_cobmr")==null || tab_plantilla_mensual_hxe.getValor(z, "horainiciohorario_cobmr").equals("") 
						|| tab_plantilla_mensual_hxe.getValor(z, "horainiciohorario_cobmr").isEmpty()) && 
						(!tab_plantilla_mensual_hxe.getValor(z, "horainicioband_cobmr").isEmpty())){
						//&&  !tab_plantilla_mensual.getValor(z, "horafinband_cobmr").equals("LIBRE"))){


						/////////////////////////////////////////////////////////////////////////////////EXTRA////////////////////////////////////////////////////////////////////////////////////////////////////			

						String fechaHoraHorarioFin ="";
						int banderaHoraExtra=0;

						//tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr"),tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
						System.out.println("Error: "+tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr"));

						if(tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra")==null || tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").equals("") || tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").isEmpty()) {

						}else 
						if(tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").isEmpty()){
						ide_gtgre_extra_relleno_=tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra");


						if (ide_gtgre_extra_relleno_.equals("3")) {


						String fechaBiometricoAgrupadaXEmpleado = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr");
						Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
						String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);

						String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");    			
						String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-5));//TERMINATOR


						Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
						sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
						sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);

						String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 

						String horaFechaInicioConsulta= diaSumaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");    			
						String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));



						fechaHoraHorarioFin ="";
						fechaHoraHorarioFin = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr")+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
						String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-6));
						Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));


						String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");
						String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),1));

						Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



						String fechaHoraHorarioEntradaSalida = fechaBiometricoAgrupadaXEmpleado+" 23:59:59";
						Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
						calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioEntradaSalida));

						String fechaHoraHorarioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 00:00:00";
						Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
						calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));



						biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


						for (Fila bio1 : biometrico_relleno_) {
						ubicacion_biometrico_="";
						fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
						//Hora de la tabla biometrico
						horaBiometrico_relleno_ ="";
						if (bio1.getCampos()[2].toString().length()==8) {
						horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
						}else {
						horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
						}
						fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
						//Calendario
						ubicacion_biometrico_ = (String)bio1.getCampos()[4];




						Calendar calFechaHoraBiometrico = Calendar.getInstance();
						//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
						calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

						if (banderaHoraExtra==0) {        		
						if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada) < 0){
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));
						banderaHoraExtra=1;
						}
						}else {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));

						banderaHoraExtra=2;

						}


						}





						}if (ide_gtgre_extra_relleno_.equals("1")) {

						String fechaBiometricoAgrupadaXEmpleado = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr");
						Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
						String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);

						String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");    			
						String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
						String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");    			
						String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));


						fechaHoraHorarioFin ="";
						fechaHoraHorarioFin = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr")+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
						String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

						Calendar calFechaHoraHorarioFin = Calendar.getInstance();
						calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));

						String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");
						String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

						Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
						calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



						biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


						for (Fila bio1 : biometrico_relleno_) {
						ubicacion_biometrico_="";
						fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
						//Hora de la tabla biometrico
						horaBiometrico_relleno_ ="";
						if (bio1.getCampos()[2].toString().length()==8) {
						horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
						}else {
						horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
						}
						fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
						//Calendario
						ubicacion_biometrico_ = (String)bio1.getCampos()[4];




						Calendar calFechaHoraBiometrico = Calendar.getInstance();
						//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
						calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

						if (banderaHoraExtra==0) {
						if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
						calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));
						banderaHoraExtra=1;
						}  }else {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));

						banderaHoraExtra=2;



						}
						}




						}else {

						}


						}	else {


						}	
						}else {

							if(tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra")==null || tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").equals("") || tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").isEmpty()) {

							}else 
							if(tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra").isEmpty()){
							ide_gtgre_extra_relleno_=tab_plantilla_mensual_hxe.getValor(z, "ide_gtgre_extra");
							
							if (ide_gtgre_extra_relleno_.equals("1")) {
								int banderaHoraExtra=0;
								String fechaBiometricoAgrupadaXEmpleado = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr");
								Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
								String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
						System.out.println(tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr"));
								String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");    			
								String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
								String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");    			
								String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));


								String fechaHoraHorarioFin ="";
								fechaHoraHorarioFin = tab_plantilla_mensual_hxe.getValor(z, "fecha_evento_cobmr")+" "+tab_plantilla_mensual_hxe.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
								String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

								Calendar calFechaHoraHorarioFin = Calendar.getInstance();
								calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));

								String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual_hxe.getValor(z,"horafinhorariohxe_cobmr");
								String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

								Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
								calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



								biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


								for (Fila bio1 : biometrico_relleno_) {
								ubicacion_biometrico_="";
								fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
								//Hora de la tabla biometrico
								horaBiometrico_relleno_ ="";
								if (bio1.getCampos()[2].toString().length()==8) {
								horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
								}else {
								horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
								}
								fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
								//Calendario
								ubicacion_biometrico_ = (String)bio1.getCampos()[4];




								Calendar calFechaHoraBiometrico = Calendar.getInstance();
								//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
								calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

								if (banderaHoraExtra==0) {
								if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
								calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));
								banderaHoraExtra=1;
								}  }else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual_hxe.getValor(z, "ide_cobmr"));

								banderaHoraExtra=2;



								}
								}

							


								}	else {


								}	
							
							}
							
							
							
							
							
						//tuzi	
						}

			
			
			
			
			
			
			
			
			}
	
	
	
	
	
	
	
	
	
	
	
	
	
}



/*
tab_plantilla_mensual=null;
if (!empleadoSeleccionado.equals("") || !empleadoSeleccionado.isEmpty()){
 tab_plantilla_mensual=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
		+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
		+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
		+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
		+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
		+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
		+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
		+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
		+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
		+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,horainiciobiometricohxe_cobmr, "
		+ "horainiciobandhxe_cobmr, horafinhorariohxe_cobmr,horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
		+ "FROM con_biometrico_marcaciones_resumen  "
		+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr in(1,2) and ide_gtgre_extra in(1,3) "
		+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
}else{
	
	tab_plantilla_mensual=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
			+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
			+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
			+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
			+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
			+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
			+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr, "
			+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr, "
			+ "biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
			+ "biometrico_salida_cobmr, tipo_horario_cobmr,horainiciohorariohxe_cobmr,  "
			+ "horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
			+ "horafinbiometricohxe_cobmr, horafinbandhxe_cobmr,ide_gtgre_extra "
			+ "FROM con_biometrico_marcaciones_resumen  "
			+" where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'  and tipo_horario_cobmr in (1,2)  and ide_gtemp="+empleadoSeleccionado+" and ide_gtgre_extra in(1,3) "
			+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
}
		
if (tab_plantilla_mensual.getTotalFilas()==0) {
	System.out.println("No existen datos para procesar en operativo");
	
}


List<Fila> biometrico_relleno_;
int banderaAlmuerzo_relleno_=0;
int banderaAlmuerzoEntrada_relleno_=0;
int banderaEntrada_relleno_=0;
Integer EntradaRelleno_=0;
Integer SalidaRelleno_=0;
Integer AlmuerzoEntradaRelleno_=0;
Integer AlmuerzoSalidaRelleno_=0;
Integer tipo1_relleno_ = 0;
Integer tipo2_relleno_ = 0;
Integer tipo3_relleno_ = 0;
Integer tipo4_relleno_= 0;
Integer tipo5_relleno_ = 0;
Integer tipo6_relleno_ = 0;
Integer tipo7_relleno_ = 0;
Integer tipo1SinAlmuerzo_relleno_= 0;
Integer tipo2SinAlmuerzo_relleno_ = 0;
Integer tipo3SinAlmuerzo_relleno_ = 0;
Integer tipo4SinAlmuerzo_relleno_= 0;
double tiempoAlmuerzo_relleno_=0.00;
int fin_relleno_=utilitario.getDia(fecha_fin);
boolean bandRegistroUnico_relleno_=false,diaAnteriorTurnoNocturno_=false;
int indice_=0,empleado_actual_relleno_=0,banderaAlmuerzo_=0,banderaAlmuerzoEntrada_=0,banderaEntrada_=0;//22,13,48,53,54
String fechaHorario_="",ubicacion_biometrico_="",horainicioalmbio_cobmr_="";
String horaInicioEmpl_relleno_="",horaIniAlmEmpl_relleno_="",horaFinAlmEmpl_relleno_="",min_almuerzo_relleno_="",horaFinEmpl_relleno_="",fechaBiometrico_relleno_="",horaBiometrico_relleno_="",ubicacion_biometrico_relleno_="",fechaHoraBiometrico_relleno_="",biometrico_entrada_cobmr_relleno_="";
String empleadoActual_relleno_="",tipo_horario_relleno_="",biometrico_alm_salida_relleno_="",biometrico_alm_entrada_relleno_="",biometrico_salida_cobmr_relleno_="",ide_gtgre_extra_relleno_="";
TablaGenerica  tab_empleados_relleno_=null;
if (!empleadoSeleccionado.equals("") || !empleadoSeleccionado.isEmpty()){

tab_empleados_relleno_=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr in(1,2) and ide_gtgre_extra in(1,3) "
//	+ "and ide_gtemp in(103,104,107,108,109,110,112,113,114,116,118,120,124,125,128,129,149) "
+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
//and ide_gtemp in(1211,1212) and ide_gtemp=103 
}else{
tab_empleados_relleno_=utilitario.consultar("SELECT   tipo_horario_cobmr,IDE_GTEMP "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and tipo_horario_cobmr in(1,2) and ide_gtgre_extra in(1,3) "
+ "and ide_gtemp in("+empleadoSeleccionado+") and ide_gtgre_extra in(1,3) "
+ "GROUP BY  tipo_horario_cobmr,IDE_GTEMP "
+ "order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");
}





int valor_relleno_=0;
empleadoActual_relleno_="";
for (int x = 0; x < tab_plantilla_mensual.getTotalFilas(); x++) {
indice_=0;
TablaGenerica tab_reporte=getTablaReporte(tab_empleados_relleno_.getValor(x, "ide_gtemp"),fecha_ini, fecha_fin);//getTablaReporte(tab_empleados.getValor(j, "ide_gtemp"),fecha_ini, fecha_fin);
//System.out.println("Empleado: "+tab_empleados_relleno.getValor(x, "ide_gtemp")+"   Empleado Marcacion: "+tab_resumen_marcaciones.getValor(valor_relleno,"ide_gtemp")+"  valor relleno: "+valor_relleno);
empleadoActual_relleno_=tab_empleados_relleno_.getValor(x, "ide_gtemp");
empleado_actual_relleno_=Integer.parseInt(empleadoActual_relleno_);
if (tab_reporte.getTotalFilas()==0) {
valor_relleno_=valor_relleno_+fin_relleno_;
}else {

}



for (int y = 0; y < tab_reporte.getTotalFilas(); y++) {
for (int z = valor_relleno_; z < tab_plantilla_mensual.getTotalFilas(); z++) {
//valor_relleno++;
//System.out.println("z: "+z);
if (tab_plantilla_mensual.getValor(z, "fecha_evento_cobmr").equals(tab_reporte.getValor(y, "TO_CHAR"))) {

//y++;

if((tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr")==null || tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").equals("") 
|| tab_plantilla_mensual.getValor(z, "horainiciohorario_cobmr").isEmpty()) && 
(!tab_plantilla_mensual.getValor(z, "horainicioband_cobmr").isEmpty())){
//&&  !tab_plantilla_mensual.getValor(z, "horafinband_cobmr").equals("LIBRE"))){


/////////////////////////////////////////////////////////////////////////////////EXTRA////////////////////////////////////////////////////////////////////////////////////////////////////			

String fechaHoraHorarioFin ="";
int banderaHoraExtra=0;

//tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr"),tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
System.out.println("Error: "+tab_plantilla_mensual.getValor(z, "fecha_evento_cobmr"));

if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")==null || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()) {

}else 
if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()){
ide_gtgre_extra_relleno_=tab_plantilla_mensual.getValor(z, "ide_gtgre_extra");


if (ide_gtgre_extra_relleno_.equals("3")) {


String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);

String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR


Date sumadateFechaInicioReporteAgrupadaXEmpleado= null;
sumadateFechaInicioReporteAgrupadaXEmpleado=dateFechaInicioReporteAgrupadaXEmpleado;
sumadateFechaInicioReporteAgrupadaXEmpleado=utilitario.sumarDiasFecha(sumadateFechaInicioReporteAgrupadaXEmpleado, 1);

String diaSumaInicio= getFechaAsyyyyMMdd(sumadateFechaInicioReporteAgrupadaXEmpleado); 

String horaFechaInicioConsulta= diaSumaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));



fechaHoraHorarioFin ="";
fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));
Calendar calFechaHoraHorarioFin = Calendar.getInstance();
calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));


String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),1));

Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



String fechaHoraHorarioEntradaSalida = fechaBiometricoAgrupadaXEmpleado+" 23:59:59";
Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioEntradaSalida));

String fechaHoraHorarioFinSalida = fechaBiometricoAgrupadaXEmpleado+" 00:00:00";
Calendar calFechaHoraHorarioFinSalida = Calendar.getInstance();
calFechaHoraHorarioFinSalida.setTime(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFinSalida));



biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


for (Fila bio1 : biometrico_relleno_) {
ubicacion_biometrico_="";
fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
//Hora de la tabla biometrico
horaBiometrico_relleno_ ="";
if (bio1.getCampos()[2].toString().length()==8) {
horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
}else {
horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
}
fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
//Calendario
ubicacion_biometrico_ = (String)bio1.getCampos()[4];




Calendar calFechaHoraBiometrico = Calendar.getInstance();
//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

if (banderaHoraExtra==0) {        		
if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFinEntrada) < 0){
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
banderaHoraExtra=1;
}
}else {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

banderaHoraExtra=2;

}


}





}if (ide_gtgre_extra_relleno_.equals("1")) {

String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);

String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));


fechaHoraHorarioFin ="";
fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

Calendar calFechaHoraHorarioFin = Calendar.getInstance();
calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));

String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


for (Fila bio1 : biometrico_relleno_) {
ubicacion_biometrico_="";
fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
//Hora de la tabla biometrico
horaBiometrico_relleno_ ="";
if (bio1.getCampos()[2].toString().length()==8) {
horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
}else {
horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
}
fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
//Calendario
ubicacion_biometrico_ = (String)bio1.getCampos()[4];




Calendar calFechaHoraBiometrico = Calendar.getInstance();
//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

if (banderaHoraExtra==0) {
if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
banderaHoraExtra=1;
}  }else {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

banderaHoraExtra=2;



}
}




}else {

}


}	else {


}	
}else {

	if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")==null || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()) {

	}else 
	if(tab_plantilla_mensual.getValor(z, "ide_gtgre_extra")!=null || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").equals("") || !tab_plantilla_mensual.getValor(z, "ide_gtgre_extra").isEmpty()){
	ide_gtgre_extra_relleno_=tab_plantilla_mensual.getValor(z, "ide_gtgre_extra");
	
	if (ide_gtgre_extra_relleno_.equals("1")) {
		int banderaHoraExtra=0;
		String fechaBiometricoAgrupadaXEmpleado = tab_reporte.getValor(y, "TO_CHAR");
		Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		String diaInicio= getFechaAsyyyyMMdd(dateFechaInicioReporteAgrupadaXEmpleado);
System.out.println(tab_reporte.getValor(y, "TO_CHAR"));
		String horaFechaFinConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");    			
		String fechaInicioReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaFinConsulta),-2));//TERMINATOR
		String horaFechaInicioConsulta= diaInicio+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");    			
		String fechaFinReporte= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(horaFechaInicioConsulta),2));


		String fechaHoraHorarioFin ="";
		fechaHoraHorarioFin = tab_reporte.getValor(y, "TO_CHAR")+" "+tab_plantilla_mensual.getValor(z,"horainiciohorariohxe_cobmr");//cogo la hora y le concateno con la fecha del horario
		String fechaFinReporteHoraEntrada= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioFin),-1));

		Calendar calFechaHoraHorarioFin = Calendar.getInstance();
		calFechaHoraHorarioFin.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraEntrada));

		String fechaHoraHorarioHasta = fechaBiometricoAgrupadaXEmpleado+" "+tab_plantilla_mensual.getValor(z,"horafinhorariohxe_cobmr");
		String fechaFinReporteHoraSalida= getFechaAsyyyyMMddHHmm(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmm(fechaHoraHorarioHasta),1));

		Calendar calFechaHoraHorarioHasta = Calendar.getInstance();
		calFechaHoraHorarioHasta.setTime(getFechaAsyyyyMMddHHmm(fechaFinReporteHoraSalida));



		biometrico_relleno_ = obtenerTimbreBiometricoNocturno(empleado_actual_relleno_, fechaInicioReporte, "" ,fechaFinReporte,"");  		


		for (Fila bio1 : biometrico_relleno_) {
		ubicacion_biometrico_="";
		fechaBiometrico_relleno_ = (String)bio1.getCampos()[1];
		//Hora de la tabla biometrico
		horaBiometrico_relleno_ ="";
		if (bio1.getCampos()[2].toString().length()==8) {
		horaBiometrico_relleno_ =(String)bio1.getCampos()[2];
		}else {
		horaBiometrico_relleno_ =(String)bio1.getCampos()[2]+":00";
		}
		fechaHoraBiometrico_relleno_ = fechaBiometricoAgrupadaXEmpleado+" "+horaBiometrico_relleno_;
		//Calendario
		ubicacion_biometrico_ = (String)bio1.getCampos()[4];




		Calendar calFechaHoraBiometrico = Calendar.getInstance();
		//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmm(fechaHoraBiometrico_relleno_)); 

		if (banderaHoraExtra==0) {
		if (calFechaHoraBiometrico.compareTo(calFechaHoraHorarioFin) >= 0 &&
		calFechaHoraBiometrico.compareTo(calFechaHoraHorarioHasta) < 0){
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
		+ " horainiciobiometrico_cobmr ='"+horaBiometrico_relleno_+"',horainicioband_cobmr='EXTRA',biometrico_entrada_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));
		banderaHoraExtra=1;
		}  }else {
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
		+ " horafinbiometrico_cobmr ='"+horaBiometrico_relleno_+"',horafinband_cobmr='EXTRA',biometrico_salida_cobmr='"+ubicacion_biometrico_+"'  where ide_cobmr ="+tab_plantilla_mensual.getValor(z, "ide_cobmr"));

		banderaHoraExtra=2;



		}
		}

	


		}	else {


		}	
	
	}
	
	
	
	
	
//tuzi	
}





y++;


if (y==(tab_reporte.getTotalFilas())) {
indice_=fin-indice_;
valor_relleno_=valor_relleno_+indice_;

break;
}
}else{

}
valor_relleno_++;
//System.out.println("valorRelleno :"+valor_relleno);
indice_++;
if(indice_==31){

break;
}
}


}

}//FOR INICIO


*/



/////////////////////////////////////////////////////////////////////////////////////////EXTRA   EXTRA///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////









/////////////////////////////////////////////////////////////////////////////////////////////////Elimina marcaciones/////////////////////////////////////////////////////////////////////////////////////////////
TablaGenerica tab_elimina_marcaciones=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
+ "horainiciobiometrico_cobmr, horainicioband_cobmr,horafinhorario_cobmr, horafinbiometrico_cobmr,  "
+ "horafinband_cobmr  "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'   and tipo_horario_cobmr=2 "
	//	+ "and ide_gtemp="+empleadoSeleccionado+" "
+ "and (horainiciobiometrico_cobmr !='') and horafinbiometrico_cobmr='' "
+"order by IDE_GTEMP ASC,FECHA_EVENTO_COBMR ASC");



TablaGenerica tab_marcacion_dia_anterior=null;
String fecha="",horaSalidaDiaAnterior="",horaEntrada="",bandSalida="";
for (int x = 0; x < tab_elimina_marcaciones.getTotalFilas(); x++) {
fecha=tab_elimina_marcaciones.getValor(x, "fecha_evento_cobmr");
horaEntrada=tab_elimina_marcaciones.getValor(x, "horainiciobiometrico_cobmr");
bandSalida=tab_elimina_marcaciones.getValor(x, "horafinband_cobmr");

Date diaAnterior= null;
diaAnterior=utilitario.DeStringADate(fecha);
diaAnterior=utilitario.sumarDiasFecha(diaAnterior, -1);


tab_marcacion_dia_anterior=utilitario.consultar("SELECT  ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr, "
+ "horainiciobiometrico_cobmr, horainicioband_cobmr,horafinhorario_cobmr, horafinbiometrico_cobmr,  "
+ "horafinband_cobmr  "
+ "FROM con_biometrico_marcaciones_resumen  "
+ "where fecha_evento_cobmr ='"+utilitario.DeDateAString(diaAnterior)+"' and tipo_horario_cobmr=2 "
//+ "and ide_gtemp in("+empleadoSeleccionado+") "
+ "and ide_gtemp in("+tab_elimina_marcaciones.getValor(x, "ide_gtemp")+") "



+ "order by tipo_horario_cobmr asc, ide_gtemp asc");

if (tab_marcacion_dia_anterior.getTotalFilas()>0) {
horaSalidaDiaAnterior=tab_marcacion_dia_anterior.getValor("horafinbiometrico_cobmr");

if (horaSalidaDiaAnterior.compareTo(horaEntrada)==0) {

utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " horainiciobiometrico_cobmr ='',horainicioband_cobmr='"+bandSalida+"'  where ide_cobmr ="+tab_elimina_marcaciones.getValor(x, "ide_cobmr"));

}


}else {

}



}




















TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov "
		+ "from asi_novedad  "
		+ "WHERE FECHA_INICIO_ASNOV >= '"+nuevaFechaInicio+"' AND  FECHA_FIN_ASNOV <='"+nuevaFechaFin+"' "
		+ "and dia_feriado_asnov=true");


String fechaInicioAsnov="",fechaFinAsnov="";

if (tab_novedad.getTotalFilas()>0) {
	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
		fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
		getFeriadoXFecha(fechaInicioAsnov, fechaFinAsnov,i);
	}		
}



System.out.println("fin: "+utilitario.getHoraActual());
//tab_resumen_marcaciones.guardar();
//guardarPantalla();

if (empleadoSeleccionado.equals("") || empleadoSeleccionado.isEmpty()){
	tab_resumen_marcaciones.setCondicion("fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"'");
}else{
	tab_resumen_marcaciones.setCondicion("fecha_evento_cobmr between '"+fecha_ini+"' and '"+fecha_fin+"' and ide_gtemp in ("+empleadoSeleccionado+")");
}
//and ide_gtemp in(623,1211) and ide_gtemp=103
tab_resumen_marcaciones.ejecutarSql();
utilitario.addUpdate("tab_resumen_marcaciones");


//////////////////////////////////////////////////////////////////////////////////////RELLENMO//////////////////////////////////////////////////////////////////////////////////////////////////////////




	
	
	
	
	
	
}




public void procesarMarcaciones(int ide_gtemp,String fecha,String horario){
	
	
	
}



	

}
