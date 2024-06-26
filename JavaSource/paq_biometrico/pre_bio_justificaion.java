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
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_bio_justificaion extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_Coordinadores = new Tabla();
    private Tabla tab_calendario = new Tabla();
    private Tabla tab_sql = new Tabla();



    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private Calendario cal_fecha_inicial_marcaciones = new Calendario();
	private Calendario cal_fecha_final_marcaciones = new Calendario();
	
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private SeleccionTabla set_area = new SeleccionTabla();
    private SeleccionTabla set_areaDepartamento = new SeleccionTabla();
    private String area;
    private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros = new HashMap();
	private Dialogo dia_fecha_marcaciones = new Dialogo();
	private String fechaReporteIni,fechaReporteFin;
 private Tabla tab_tabla_sumatoria =new Tabla();
    
    
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	private boolean novedad=false,sinNovedad=false;
	
	private boolean matriz=false,operativo=false;
    
    public pre_bio_justificaion() {
    	
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
    	
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		bar_botones.agregarReporte();
    	  
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
	bot_copiar.setValue("REPORTE BIOMÉTRICO");
	bot_copiar.setTitle("REPORTE BIOMÉTRICO");
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
	bar_botones.agregarBoton(bot_AreaDepartamentoEmpleado);	
    	

	Boton bot_entrada= new Boton();
	bot_entrada.setIcon("ui-icon-document");
	bot_entrada.setMetodo("buscarSinEntrada");
	bot_entrada.setValue("SIN ENTRADA");
	bot_entrada.setTitle("SIN ENTRADA");
	bar_botones.agregarBoton(bot_entrada);	
    
	
	Boton bot_salida= new Boton();
	bot_salida.setIcon("ui-icon-document");
	bot_salida.setMetodo("buscarSinSalida");
	bot_salida.setValue("SIN SALIDA");
	bot_salida.setTitle("SIN SALIDA");
	bar_botones.agregarBoton(bot_salida);	
    
	Boton bot_cambioHorario= new Boton();
	bot_cambioHorario.setIcon("ui-icon-document");
	bot_cambioHorario.setMetodo("buscarCambioHorario");
	bot_cambioHorario.setValue("CAMBIO TURNO");
	bot_cambioHorario.setTitle("CAMBIO TURNO");
	bar_botones.agregarBoton(bot_cambioHorario);	
    
    		// boton limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar);     	
       
    	
dia_fecha_marcaciones.setId("dia_fecha_marcaciones");
dia_fecha_marcaciones.setTitle("SELECCIONE FECHA");
dia_fecha_marcaciones.setWidth("20%");
dia_fecha_marcaciones.setHeight("15%");
dia_fecha_marcaciones.getBot_aceptar().setMetodo("aceptarReporte");
Grid gri_grid = new Grid();
gri_grid.getChildren().add(new Etiqueta("Fecha Inicio"));
gri_grid.getChildren().add(cal_fecha_inicial_marcaciones);
gri_grid.getChildren().add(new Etiqueta("Fecha Fin "));
gri_grid.getChildren().add(cal_fecha_final_marcaciones);
gri_grid.setStyle("height:" + (dia_fecha_marcaciones.getAltoPanel() - 10) + "px;overflow: auto;display: block;");
gri_grid.setColumns(2);
gri_grid.setWidth("98%");
dia_fecha_marcaciones.setDialogo(gri_grid);
agregarComponente(dia_fecha_marcaciones);


	
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla(empleadosConsulta(),"IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setNombreVisual("DOC. IDENTIFICACIÓN");
		set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("NOMBRES Y APELLIDOS");
		set_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
		set_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
	//	set_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		set_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
	//	set_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_empleado.getTab_seleccion().getColumna("DETALLE_GTTEM").setFiltro(true);
	//	set_empleado.getTab_seleccion().getColumna("DETALLE_GTTEM").setNombreVisual("TIPO EMPLEADO");

		
    	set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
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
		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(5);

		

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

        tab_tabla.getColumna("DIA_COBMR").setLongitud(15);
        tab_tabla.getColumna("DIA_COBMR").alinearCentro();
        tab_tabla.getColumna("DIA_COBMR").setNombreVisual("DIA");
        
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");

    
        tab_tabla.getColumna("E_FECHA_SOLICITUD").setLongitud(15);
        tab_tabla.getColumna("E_FECHA_SOLICITUD").alinearCentro();
        tab_tabla.getColumna("E_FECHA_SOLICITUD").setNombreVisual("FECHA SOLICITUD");

        tab_tabla.getColumna("E_TTHH").setLongitud(15);
        tab_tabla.getColumna("E_TTHH").alinearCentro();
        tab_tabla.getColumna("E_TTHH").setNombreVisual("TTHH");
        
        
        tab_tabla.getColumna("E_JEFE_INMEDIATO").setLongitud(15);
        tab_tabla.getColumna("E_JEFE_INMEDIATO").alinearCentro();
        tab_tabla.getColumna("E_JEFE_INMEDIATO").setNombreVisual("JEFE INMEDIATO");
      	        
        tab_tabla.getColumna("E_FECHA_DESDE").setLongitud(15);
        tab_tabla.getColumna("E_FECHA_DESDE").alinearCentro();
        tab_tabla.getColumna("E_FECHA_DESDE").setNombreVisual("F.DESDE");
        
         tab_tabla.getColumna("E_FECHA_HASTA").setLongitud(16);
        tab_tabla.getColumna("E_FECHA_HASTA").alinearCentro();
        tab_tabla.getColumna("E_FECHA_HASTA").setNombreVisual("F.HASTA");
        
        tab_tabla.getColumna("E_DETALLE").setLongitud(20);
        tab_tabla.getColumna("E_DETALLE").alinearCentro();
        tab_tabla.getColumna("E_DETALLE").setNombreVisual("DETALLE");  

        tab_tabla.getColumna("E_TIPO_PERMISO").setLongitud(20);
        tab_tabla.getColumna("E_TIPO_PERMISO").alinearCentro();
        tab_tabla.getColumna("E_TIPO_PERMISO").setNombreVisual("TIP.PERMISO");  
        
        tab_tabla.getColumna("E_NUM_HORAS").setLongitud(10);
        tab_tabla.getColumna("E_NUM_HORAS").alinearCentro();
        tab_tabla.getColumna("E_NUM_HORAS").setNombreVisual("NUM.HORAS"); 
        
        
        tab_tabla.getColumna("E_HORA_DESDE").setLongitud(10);
        tab_tabla.getColumna("E_HORA_DESDE").alinearCentro();
        tab_tabla.getColumna("E_HORA_DESDE").setNombreVisual("H.DESDE"); 
        
        
        tab_tabla.getColumna("E_HORA_HASTA").setLongitud(25);
        tab_tabla.getColumna("E_HORA_HASTA").alinearCentro();
        tab_tabla.getColumna("E_HORA_HASTA").setNombreVisual("H.HASTA");  
        
                

        
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").setLongitud(15);
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_SOLICITUD_ALM").setNombreVisual("FECHA SOL.ALM");

        tab_tabla.getColumna("TTHH_ALM").setLongitud(15);
        tab_tabla.getColumna("TTHH_ALM").alinearCentro();
        tab_tabla.getColumna("TTHH_ALM").setNombreVisual("TTHH.ALM");
        
        
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").setLongitud(15);
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").alinearCentro();
        tab_tabla.getColumna("JEFE_INMEDIATO_ALM").setNombreVisual("JEFE INM.ALM");
      	        
        tab_tabla.getColumna("FECHA_DESDE_ALM").setLongitud(15);
        tab_tabla.getColumna("FECHA_DESDE_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_DESDE_ALM").setNombreVisual("F.DESDE.ALM");
        
         tab_tabla.getColumna("FECHA_HASTA_ALM").setLongitud(16);
        tab_tabla.getColumna("FECHA_HASTA_ALM").alinearCentro();
        tab_tabla.getColumna("FECHA_HASTA_ALM").setNombreVisual("F.HASTA.ALM");
        
        tab_tabla.getColumna("DETALLE_ALM").setLongitud(20);
        tab_tabla.getColumna("DETALLE_ALM").alinearCentro();
        tab_tabla.getColumna("DETALLE_ALM").setNombreVisual("DETALLE_ALM");  

        tab_tabla.getColumna("TIPO_PERMISO_ALM").setLongitud(20);
        tab_tabla.getColumna("TIPO_PERMISO_ALM").alinearCentro();
        tab_tabla.getColumna("TIPO_PERMISO_ALM").setNombreVisual("TIP.PERMISO.ALM");  
        
        tab_tabla.getColumna("NUM_HORAS_ALM").setLongitud(10);
        tab_tabla.getColumna("NUM_HORAS_ALM").alinearCentro();
        tab_tabla.getColumna("NUM_HORAS_ALM").setNombreVisual("NUM.HORAS_ALM"); 
        
        
        tab_tabla.getColumna("HORA_DESDE_ALM").setLongitud(10);
        tab_tabla.getColumna("HORA_DESDE_ALM").alinearCentro();
        tab_tabla.getColumna("HORA_DESDE_ALM").setNombreVisual("H.DESDE_ALM"); 
        
        
        tab_tabla.getColumna("HORA_HASTA_ALM").setLongitud(25);
        tab_tabla.getColumna("HORA_HASTA_ALM").alinearCentro();
        tab_tabla.getColumna("HORA_HASTA_ALM").setNombreVisual("H.HASTA_ALM");  
        
                
        
        
        
        
        
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").setLongitud(15);
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_SOLICITUD_SAL").setNombreVisual("FECHA SOL.SAL");

        tab_tabla.getColumna("TTHH_SAL").setLongitud(15);
        tab_tabla.getColumna("TTHH_SAL").alinearCentro();
        tab_tabla.getColumna("TTHH_SAL").setNombreVisual("TTHH_SAL");
        
        
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").setLongitud(15);
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").alinearCentro();
        tab_tabla.getColumna("JEFE_INMEDIATO_SAL").setNombreVisual("JEFE INM.SAL");
      	        
        tab_tabla.getColumna("FECHA_DESDE_SAL").setLongitud(15);
        tab_tabla.getColumna("FECHA_DESDE_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_DESDE_SAL").setNombreVisual("F.DESDE.SAL");
        
         tab_tabla.getColumna("FECHA_HASTA_SAL").setLongitud(16);
        tab_tabla.getColumna("FECHA_HASTA_SAL").alinearCentro();
        tab_tabla.getColumna("FECHA_HASTA_SAL").setNombreVisual("F.HASTA.SAL");
        
        tab_tabla.getColumna("DETALLE_SAL").setLongitud(20);
        tab_tabla.getColumna("DETALLE_SAL").alinearCentro();
        tab_tabla.getColumna("DETALLE_SAL").setNombreVisual("DETALLE.SAL");  

        tab_tabla.getColumna("TIPO_PERMISO_SAL").setLongitud(20);
        tab_tabla.getColumna("TIPO_PERMISO_SAL").alinearCentro();
        tab_tabla.getColumna("TIPO_PERMISO_SAL").setNombreVisual("TIP.PERMISO.SAL");  
        
        tab_tabla.getColumna("NUM_HORAS_SAL").setLongitud(10);
        tab_tabla.getColumna("NUM_HORAS_SAL").alinearCentro();
        tab_tabla.getColumna("NUM_HORAS_SAL").setNombreVisual("NUM.HORAS.SAL"); 
        
        
        tab_tabla.getColumna("HORA_DESDE_SAL").setLongitud(10);
        tab_tabla.getColumna("HORA_DESDE_SAL").alinearCentro();
        tab_tabla.getColumna("HORA_DESDE_SAL").setNombreVisual("H.DESDE.SAL"); 
        
        
        tab_tabla.getColumna("HORA_HASTA_SAL").setLongitud(25);
        tab_tabla.getColumna("HORA_HASTA_SAL").alinearCentro();
        tab_tabla.getColumna("HORA_HASTA_SAL").setNombreVisual("H.HASTA_SAL");  
        
                
        
        
        
        
        
        
        
        
        
        
      
	
		
		tab_tabla.setRows(20);
		tab_tabla.dibujar();
		tab_tabla.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
    	
        
        
		
	      
        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir1(pat_panel);
        
        agregarComponente(div_division1);
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
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
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

   	private String obtenerDiferenciaMinutos(Date fechaInicial, Date fechaFinal){
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
		return ""+total_diferencia_minutos;
    }
   	
   	
   	
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
			
		
		
		/*sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
		+ "res.horainicioalm_cobmr, "
				+ "res.horainicioalmbio_cobmr,  "
		+ "res.horafinalm_cobmr, "
		+ "res.horafinalmbio_cobmr, "
		+ "res.tiempoalm_cobmr, "
		+ "res.tiempohoralm_cobmr, "
				+ "'' as diferenciaAlm, "
		+ "res.horafinhorario_cobmr, "
		+ "res.horafinbiometrico_cobmr, "
		+ "res.horafinband_cobmr,  "
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
		+ "'' as HORA_HASTA_SAL, "
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
		+ "where "
		//+ "epar.activo_geedp=true AND "
		+ "emp.ide_gtemp=-1 " 
		+ "and res.ide_aspvh IS NULL "
		+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
		+ "EMP.APELLIDO_PATERNO_GTEMP,"
		+ "EMP.APELLIDO_MATERNO_GTEMP,"
		+ "EMP.PRIMER_NOMBRE_GTEMP, "
		+ "EMP.SEGUNDO_NOMBRE_GTEMP,"
		+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
		+ "DEPA.DETALLE_GEDEP, "
		+ "res.ide_cobmr, "
		+ "res.dia_cobmr, "
		+ "res.fecha_evento_cobmr, "
		+ "res.horainiciohorario_cobmr, "
		+ "res.horainiciobiometrico_cobmr, "
		+ "res.horainicioband_cobmr, "
		+ "diferenciaEntradaTimbrado, "
		+ "res.horainicioalm_cobmr, "
		+ "res.horainicioalmbio_cobmr,  "
		+ "res.horafinalm_cobmr,  "
		+ "res.horafinalmbio_cobmr,  "
		+ "res.tiempoalm_cobmr,  "
		+ "res.tiempohoralm_cobmr,  "
		+ "diferenciaAlm,  "
		+ "res.horafinhorario_cobmr, "
		+ "res.horafinbiometrico_cobmr, "
		+ "res.horafinband_cobmr,  "
		+ "diferenciaSalidaTimbrado, "
		+ "GTT.DETALLE_GTTEM, "
		+ "E_FECHA_SOLICITUD, "
		+ "E_TTHH, "
		+ "E_JEFE_INMEDIATO, "
		+ "E_FECHA_DESDE, "
		+ "E_FECHA_HASTA, "
		+ "E_DETALLE, "
		+ "E_TIPO_PERMISO, "
		+ "E_NUM_HORAS, "
		+ "E_HORA_DESDE, "
		+ "E_HORA_HASTA, "
		+ "FECHA_SOLICITUD_ALM, "
		+ "TTHH_ALM,  "
		+ "JEFE_INMEDIATO_ALM, "
		+ "FECHA_DESDE_ALM, "
		+ "FECHA_HASTA_ALM, "
		+ "DETALLE_ALM, "
		+ "TIPO_PERMISO_ALM, "
		+ "NUM_HORAS_ALM, "
		+ "HORA_DESDE_ALM, "
		+ "HORA_HASTA_ALM, "
		+ "FECHA_SOLICITUD_SAL, "
		+ "TTHH_SAL, "
		+ "JEFE_INMEDIATO_SAL, "
		+ "FECHA_DESDE_SAL,  "
		+ "FECHA_HASTA_SAL, "
		+ "DETALLE_SAL, "
		+ "TIPO_PERMISO_SAL, "
		+ "NUM_HORAS_SAL, "
		+ "HORA_DESDE_SAL, "
		+ "HORA_HASTA_SAL,"
		+ "DIFERENCIAENTRADA, "
		+ "diferenciaSalidaAlm, "
		+ "diferenciaEntradaAlm,"
		+ "p_entrada_cobmr,  "
		+ "p_salida_cobmr, "
		+ "p_alm_cobmr " 
		+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";*/
			
			sql="SELECT  "
					+ "cbmr.ide_gtemp, "
					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr,'' as diferenciaEntradaTimbrado, "
					+ "cbmr.horainicioalm_cobmr,cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr,'' as diferenciaAlm, "
					+ "cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr,cbmr.horafinband_cobmr,'' as diferenciaSalidaTimbrado, "
					+ "cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "GTT.DETALLE_GTTEM, "
					+ "p_entrada_cobmr, "
					+ "p_salida_cobmr, "
					+ "p_alm_cobmr, "
					+ "'' as diferenciaEntradaTimbrado, "
					+ "'' as diferenciaAlm, "
					+ "'' as diferenciaSalidaTimbrado, "
					+ "'' as E_FECHA_SOLICITUD,  "
					+ "'' as E_TTHH, "
					+ "'' as E_JEFE_INMEDIATO, "
					+ "'' as E_FECHA_DESDE, "
					+ "'' as E_FECHA_HASTA,  "
					+ "'' as E_DETALLE, "
					+ "'' as E_TIPO_PERMISO, "
					+ "'' as E_NUM_HORAS, "
					+ "'' as E_HORA_DESDE, "
					+ "'' as E_HORA_HASTA, "
					+ "'' as FECHA_SOLICITUD_ALM, "
					+ "'' as TTHH_ALM,  "
					+ "'' as JEFE_INMEDIATO_ALM, "
					+ "'' as FECHA_DESDE_ALM, "
					+ "'' as FECHA_HASTA_ALM, "
					+ "'' as DETALLE_ALM,  "
					+ "'' as TIPO_PERMISO_ALM, "
					+ "'' as NUM_HORAS_ALM, "
					+ "'' as HORA_DESDE_ALM, "
					+ "'' as HORA_HASTA_ALM, "
					+ "'' as FECHA_SOLICITUD_SAL, "
					+ "'' as TTHH_SAL, "
					+ "'' as JEFE_INMEDIATO_SAL, "
					+ "'' as FECHA_DESDE_SAL, "
					+ "'' as FECHA_HASTA_SAL, "
					+ "'' as DETALLE_SAL, "
					+ "'' as TIPO_PERMISO_SAL, "
					+ "'' as NUM_HORAS_SAL, "
					+ "'' as HORA_DESDE_SAL, "
					+ "'' as HORA_HASTA_SAL, "
					+ "'' as DIFERENCIAENTRADA, "
					+ "'' as diferenciaSalidaAlm, "
					+ "'' as diferenciaEntradaAlm		"
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM    "
					+ "where cbmr.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
					+ "and epar.ide_gtemp=-1 "
					+ "and cbmr.ide_aspvh IS NULL  "
					+ "and  EPAR.ACTIVO_GEEDP=TRUE "
					+ "group by "
					+ "cbmr.ide_gtemp, "
					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr, cbmr.horainicioalm_cobmr, "
					+ "cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr, cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr, "
					+ "cbmr.horafinband_cobmr, cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM, "
					+ "p_entrada_cobmr, "
					+ "p_salida_cobmr, "
					+ "p_alm_cobmr "
					+ "ORDER BY cbmr.ide_gtemp ASC,cbmr.fecha_evento_cobmr ASC";
		

		}else {
			
		sql="SELECT "
					+ "cbmr.ide_gtemp, "
					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr,'' as diferenciaEntradaTimbrado, "
					+ "cbmr.horainicioalm_cobmr,cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr,'' as diferenciaAlm, "
					+ "cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr,cbmr.horafinband_cobmr,'' as diferenciaSalidaTimbrado, "
					+ "cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "GTT.DETALLE_GTTEM, "
					+ "p_entrada_cobmr, "
					+ "p_salida_cobmr, "
					+ "p_alm_cobmr, "
					+ "'' as diferenciaEntradaTimbrado, "
					+ "'' as diferenciaAlm, "
					+ "'' as diferenciaSalidaTimbrado, "
					+ "'' as E_FECHA_SOLICITUD,  "
					+ "'' as E_TTHH, "
					+ "'' as E_JEFE_INMEDIATO, "
					+ "'' as E_FECHA_DESDE, "
					+ "'' as E_FECHA_HASTA,  "
					+ "'' as E_DETALLE, "
					+ "'' as E_TIPO_PERMISO, "
					+ "'' as E_NUM_HORAS, "
					+ "'' as E_HORA_DESDE, "
					+ "'' as E_HORA_HASTA, "
					+ "'' as FECHA_SOLICITUD_ALM, "
					+ "'' as TTHH_ALM,  "
					+ "'' as JEFE_INMEDIATO_ALM, "
					+ "'' as FECHA_DESDE_ALM, "
					+ "'' as FECHA_HASTA_ALM, "
					+ "'' as DETALLE_ALM,  "
					+ "'' as TIPO_PERMISO_ALM, "
					+ "'' as NUM_HORAS_ALM, "
					+ "'' as HORA_DESDE_ALM, "
					+ "'' as HORA_HASTA_ALM, "
					+ "'' as FECHA_SOLICITUD_SAL, "
					+ "'' as TTHH_SAL, "
					+ "'' as JEFE_INMEDIATO_SAL, "
					+ "'' as FECHA_DESDE_SAL, "
					+ "'' as FECHA_HASTA_SAL, "
					+ "'' as DETALLE_SAL, "
					+ "'' as TIPO_PERMISO_SAL, "
					+ "'' as NUM_HORAS_SAL, "
					+ "'' as HORA_DESDE_SAL, "
					+ "'' as HORA_HASTA_SAL, "
					+ "'' as DIFERENCIAENTRADA, "
					+ "'' as diferenciaSalidaAlm, "
					+ "'' as diferenciaEntradaAlm		"
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM    "
					+ "where cbmr.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
					//+ "and epar.ide_gtemp in(555) "
					+ "and cbmr.ide_aspvh IS NULL  "
					+ "and  EPAR.ACTIVO_GEEDP=TRUE "
					+ "group by  "
					+ "cbmr.ide_gtemp, "
					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr, cbmr.horainicioalm_cobmr, "
					+ "cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr, cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr, "
					+ "cbmr.horafinband_cobmr, cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM, "
					+ "p_entrada_cobmr, "
					+ "p_salida_cobmr, "
					+ "p_alm_cobmr "
					+ "ORDER BY cbmr.ide_gtemp ASC,cbmr.fecha_evento_cobmr ASC";
			
	
	
			
			/*
				
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
					+ "where "
					//+ "epar.activo_geedp=true  AND "
					+ "res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' "
					+ "and res.ide_aspvh IS NULL "
					+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP,"
					+ "EMP.APELLIDO_MATERNO_GTEMP,"
					+ "EMP.PRIMER_NOMBRE_GTEMP, "
					+ "EMP.SEGUNDO_NOMBRE_GTEMP,"
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "diferenciaEntradaTimbrado, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  "
					+ "res.horafinalmbio_cobmr,  "
					+ "res.tiempoalm_cobmr,  "
					+ "res.tiempohoralm_cobmr,  "
					+ "diferenciaAlm,  "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr,  "
					+ "diferenciaSalidaTimbrado, "
					+ "GTT.DETALLE_GTTEM, "
					+ "E_FECHA_SOLICITUD, "
					+ "E_TTHH, "
					+ "E_JEFE_INMEDIATO, "
					+ "E_FECHA_DESDE, "
					+ "E_FECHA_HASTA, "
					+ "E_DETALLE, "
					+ "E_TIPO_PERMISO, "
					+ "E_NUM_HORAS, "
					+ "E_HORA_DESDE, "
					+ "E_HORA_HASTA, "
					+ "FECHA_SOLICITUD_ALM, "
					+ "TTHH_ALM,  "
					+ "JEFE_INMEDIATO_ALM, "
					+ "FECHA_DESDE_ALM, "
					+ "FECHA_HASTA_ALM, "
					+ "DETALLE_ALM, "
					+ "TIPO_PERMISO_ALM, "
					+ "NUM_HORAS_ALM, "
					+ "HORA_DESDE_ALM, "
					+ "HORA_HASTA_ALM, "
					+ "FECHA_SOLICITUD_SAL, "
					+ "TTHH_SAL, "
					+ "JEFE_INMEDIATO_SAL, "
					+ "FECHA_DESDE_SAL,  "
					+ "FECHA_HASTA_SAL, "
					+ "DETALLE_SAL, "
					+ "TIPO_PERMISO_SAL, "
					+ "NUM_HORAS_SAL, "
					+ "HORA_DESDE_SAL, "
					+ "HORA_HASTA_SAL,"
					+ "DIFERENCIAENTRADA, "
					+ "diferenciaSalidaAlm, "
					+ "diferenciaEntradaAlm,"
					+ "p_entrada_cobmr,  "
					+ "p_salida_cobmr, "
					+ "p_alm_cobmr " 
					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
		*/}
		return sql;
			
	}
	

	
	
	
	

	public TablaGenerica getTablaReporte(String strFechaIniReporte, String strFechaFinReporte){
		
		
		TablaGenerica tabEmpleadosTimbradas=utilitario.consultar(
				
				"SELECT  "
    					+ "cbmr.ide_gtemp, "
    					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
    					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
    					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
    					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
    					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
    					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
    					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr,'' as diferenciaEntradaTimbrado, "
    					+ "cbmr.horainicioalm_cobmr,cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
    					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr,'' as diferenciaAlm, "
    					+ "cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr,cbmr.horafinband_cobmr,'' as diferenciaSalidaTimbrado, "
    					+ "cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
    					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
    					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
    					+ "DEPA.DETALLE_GEDEP, "
    					+ "GTT.DETALLE_GTTEM, "
    					+ "p_entrada_cobmr, "
    					+ "p_salida_cobmr, "
    					+ "p_alm_cobmr, "
    					+ "'' as diferenciaEntradaTimbrado, "
    					+ "'' as diferenciaAlm, "
    					+ "'' as diferenciaSalidaTimbrado, "
    					+ "'' as E_FECHA_SOLICITUD,  "
    					+ "'' as E_TTHH, "
    					+ "'' as E_JEFE_INMEDIATO, "
    					+ "'' as E_FECHA_DESDE, "
    					+ "'' as E_FECHA_HASTA,  "
    					+ "'' as E_DETALLE, "
    					+ "'' as E_TIPO_PERMISO, "
    					+ "'' as E_NUM_HORAS, "
    					+ "'' as E_HORA_DESDE, "
    					+ "'' as E_HORA_HASTA, "
    					+ "'' as FECHA_SOLICITUD_ALM, "
    					+ "'' as TTHH_ALM,  "
    					+ "'' as JEFE_INMEDIATO_ALM, "
    					+ "'' as FECHA_DESDE_ALM, "
    					+ "'' as FECHA_HASTA_ALM, "
    					+ "'' as DETALLE_ALM,  "
    					+ "'' as TIPO_PERMISO_ALM, "
    					+ "'' as NUM_HORAS_ALM, "
    					+ "'' as HORA_DESDE_ALM, "
    					+ "'' as HORA_HASTA_ALM, "
    					+ "'' as FECHA_SOLICITUD_SAL, "
    					+ "'' as TTHH_SAL, "
    					+ "'' as JEFE_INMEDIATO_SAL, "
    					+ "'' as FECHA_DESDE_SAL, "
    					+ "'' as FECHA_HASTA_SAL, "
    					+ "'' as DETALLE_SAL, "
    					+ "'' as TIPO_PERMISO_SAL, "
    					+ "'' as NUM_HORAS_SAL, "
    					+ "'' as HORA_DESDE_SAL, "
    					+ "'' as HORA_HASTA_SAL, "
    					+ "'' as DIFERENCIAENTRADA, "
    					+ "'' as diferenciaSalidaAlm, "
    					+ "'' as diferenciaEntradaAlm		"
    					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
    					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
    					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
    					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
    					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
    					+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp "
    					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM    "
        				+ "where cbmr.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
    					+ "and cbmr.ide_aspvh IS NULL  "
    					+ "and  EPAR.ACTIVO_GEEDP=TRUE "
    					//+ "and epar.ide_gtemp=269 "
    					+ "group by  "
    					+ "cbmr.ide_gtemp, "
    					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
    					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
    					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
    					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr, cbmr.horainicioalm_cobmr, "
    					+ "cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
    					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr, cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr, "
    					+ "cbmr.horafinband_cobmr, cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
    					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
    					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
    					+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM, "
    					+ "p_entrada_cobmr, "
    					+ "p_salida_cobmr, "
    					+ "p_alm_cobmr "
    					+ "ORDER BY cbmr.ide_gtemp ASC,cbmr.fecha_evento_cobmr ASC");		
				
		/*"SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
		+ "where "
		//+ "epar.activo_geedp=true and  "
		+ "res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
				+ "and res.ide_aspvh is null  "
    	//+ "and epar.ide_gtemp in(555) "
		
+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
+ "EMP.APELLIDO_PATERNO_GTEMP,"
+ "EMP.APELLIDO_MATERNO_GTEMP,"
+ "EMP.PRIMER_NOMBRE_GTEMP, "
+ "EMP.SEGUNDO_NOMBRE_GTEMP,"
+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
+ "DEPA.DETALLE_GEDEP, "
+ "res.ide_cobmr, "
+ "res.dia_cobmr, "
+ "res.fecha_evento_cobmr, "
+ "res.horainiciohorario_cobmr, "
+ "res.horainiciobiometrico_cobmr, "
+ "res.horainicioband_cobmr, "
+ "diferenciaEntradaTimbrado, "
+ "res.horainicioalm_cobmr, "
+ "res.horainicioalmbio_cobmr,  "
+ "res.horafinalm_cobmr,  "
+ "res.horafinalmbio_cobmr,  "
+ "res.tiempoalm_cobmr,  "
+ "res.tiempohoralm_cobmr,  "
+ "diferenciaAlm,  "
+ "res.horafinhorario_cobmr, "
+ "res.horafinbiometrico_cobmr, "
+ "res.horafinband_cobmr,  "
+ "diferenciaSalidaTimbrado, "
+ "GTT.DETALLE_GTTEM, "
+ "E_FECHA_SOLICITUD, "
+ "E_TTHH, "
+ "E_JEFE_INMEDIATO, "
+ "E_FECHA_DESDE, "
+ "E_FECHA_HASTA, "
+ "E_DETALLE, "
+ "E_TIPO_PERMISO, "
+ "E_NUM_HORAS, "
+ "E_HORA_DESDE, "
+ "E_HORA_HASTA, "
+ "FECHA_SOLICITUD_ALM, "
+ "TTHH_ALM,  "
+ "JEFE_INMEDIATO_ALM, "
+ "FECHA_DESDE_ALM, "
+ "FECHA_HASTA_ALM, "
+ "DETALLE_ALM, "
+ "TIPO_PERMISO_ALM, "
+ "NUM_HORAS_ALM, "
+ "HORA_DESDE_ALM, "
+ "HORA_HASTA_ALM, "
+ "FECHA_SOLICITUD_SAL, "
+ "TTHH_SAL, "
+ "JEFE_INMEDIATO_SAL, "
+ "FECHA_DESDE_SAL,  "
+ "FECHA_HASTA_SAL, "
+ "DETALLE_SAL, "
+ "TIPO_PERMISO_SAL, "
+ "NUM_HORAS_SAL, "
+ "HORA_DESDE_SAL, "
+ "HORA_HASTA_SAL,"
+ "DIFERENCIAENTRADA, "
+ "diferenciaSalidaAlm, "
+ "diferenciaEntradaAlm,"
+ "p_entrada_cobmr,  "
+ "p_salida_cobmr, "
+ "p_alm_cobmr  " 
		+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");*/
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

	
	
	public void justificarOLD(){
   
		
	 
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
    		 
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
        				+ "res.horainicioalm_cobmr, "
        				+ "res.horafinalm_cobmr, "
        				+ "res.horainicioalmbio_cobmr, "
        				+ "res.horafinalmbio_cobmr, "
        				+ "res.tiempoalm_cobmr, "
        				+ "res.tiempohoralm_cobmr, "
        				+ "res.horafinhorario_cobmr, "
        				+ "res.horafinbiometrico_cobmr, "
        				+ "res.horafinband_cobmr,  "
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
        				+ "'' as HORA_HASTA,  "
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
        				
        				/////////////////////////////////////////////////////////
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
        				+ "'' as diferenciaEntradaTimbrado, "
        				+ "'' as diferenciaSalidaTimbrado, "
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
        				+ "where "
        				//+ "epar.activo_geedp=true AND  "
        				+ "res.fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'  "
        				//+ "	AND NOVEDAD_cobmr=false "
           				//+ "and epar.ide_gtemp in(516) "
        				//+ "25,22,21,17,16,13,12,8,212,211,205,200,199,196,195,189,188,187,185,184,177,173,172,169,168,167,145,144,143,137,134,131,110,104,83,82,81,79,73,62,57,49,48,40,30,27,301,264,206) " 
        				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
        				tab_tabla.ejecutarSql();
        				utilitario.addUpdate("tab_tabla");
    		}
	 	}
	   	
	   	uno();
		


		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
	dos();
		
		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		tres();
		cuatro();
		
	quinto();
	
	
	}
	
	
	
	
	
	
	
	
	
	
	public void justificar(){
   
		
	   	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
    		 
        	tab_tabla.setSql("SELECT  "
        					+ "cbmr.ide_gtemp, "
        					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
        					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
        					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
        					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
        					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
        					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
        					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr,'' as diferenciaEntradaTimbrado, "
        					+ "cbmr.horainicioalm_cobmr,cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
        					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr,'' as diferenciaAlm, "
        					+ "cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr,cbmr.horafinband_cobmr,'' as diferenciaSalidaTimbrado, "
        					+ "cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
        					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
        					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
        					+ "DEPA.DETALLE_GEDEP, "
        					+ "GTT.DETALLE_GTTEM, "
        					+ "p_entrada_cobmr, "
        					+ "p_salida_cobmr, "
        					+ "p_alm_cobmr, "
        					+ "'' as diferenciaEntradaTimbrado, "
        					+ "'' as diferenciaAlm, "
        					+ "'' as diferenciaSalidaTimbrado, "
        					+ "'' as E_FECHA_SOLICITUD,  "
        					+ "'' as E_TTHH, "
        					+ "'' as E_JEFE_INMEDIATO, "
        					+ "'' as E_FECHA_DESDE, "
        					+ "'' as E_FECHA_HASTA,  "
        					+ "'' as E_DETALLE, "
        					+ "'' as E_TIPO_PERMISO, "
        					+ "'' as E_NUM_HORAS, "
        					+ "'' as E_HORA_DESDE, "
        					+ "'' as E_HORA_HASTA, "
        					+ "'' as FECHA_SOLICITUD_ALM, "
        					+ "'' as TTHH_ALM,  "
        					+ "'' as JEFE_INMEDIATO_ALM, "
        					+ "'' as FECHA_DESDE_ALM, "
        					+ "'' as FECHA_HASTA_ALM, "
        					+ "'' as DETALLE_ALM,  "
        					+ "'' as TIPO_PERMISO_ALM, "
        					+ "'' as NUM_HORAS_ALM, "
        					+ "'' as HORA_DESDE_ALM, "
        					+ "'' as HORA_HASTA_ALM, "
        					+ "'' as FECHA_SOLICITUD_SAL, "
        					+ "'' as TTHH_SAL, "
        					+ "'' as JEFE_INMEDIATO_SAL, "
        					+ "'' as FECHA_DESDE_SAL, "
        					+ "'' as FECHA_HASTA_SAL, "
        					+ "'' as DETALLE_SAL, "
        					+ "'' as TIPO_PERMISO_SAL, "
        					+ "'' as NUM_HORAS_SAL, "
        					+ "'' as HORA_DESDE_SAL, "
        					+ "'' as HORA_HASTA_SAL, "
        					+ "'' as DIFERENCIAENTRADA, "
        					+ "'' as diferenciaSalidaAlm, "
        					+ "'' as diferenciaEntradaAlm		"
        					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
        					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
        					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
        					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
        					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
        					+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp "
        					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM    "
            				+ "where cbmr.fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'  "
        					+ "and cbmr.ide_aspvh IS NULL  "
        					+ "and  EPAR.ACTIVO_GEEDP=TRUE "
        					+ "group by  "
        					+ "cbmr.ide_gtemp, "
        					+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
        					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
        					+ "cbmr.IDE_COBMR,cbmr.fecha_evento_cobmr, cbmr.horainiciohorario_cobmr, "
        					+ "cbmr.horainiciobiometrico_cobmr, cbmr.horainicioband_cobmr, cbmr.horainicioalm_cobmr, "
        					+ "cbmr.horafinalm_cobmr, cbmr.horainicioalmbio_cobmr, cbmr.horafinalmbio_cobmr, "
        					+ "cbmr.tiempoalm_cobmr, cbmr.tiempohoralm_cobmr, cbmr.horafinhorario_cobmr, cbmr.horafinbiometrico_cobmr, "
        					+ "cbmr.horafinband_cobmr, cbmr.novedad_cobmr,cbmr.aprueba_hora_extra_cobmr, "
        					+ "cbmr.dia_cobmr, cbmr.horafinextra_cobmr, cbmr.recargonocturno25_cobmr, cbmr.recargonocturno100_cobmr, "
        					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
        					+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM, "
        					+ "p_entrada_cobmr, "
        					+ "p_salida_cobmr, "
        					+ "p_alm_cobmr "
        					+ "ORDER BY cbmr.ide_gtemp ASC,cbmr.fecha_evento_cobmr ASC");        	
        			
        			
        			
        	/*		"SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
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
        				+ "res.horainicioalm_cobmr, "
        			+ "res.horainicioalmbio_cobmr,  "
        				+ "res.horafinalm_cobmr, "
        				+ "res.horafinalmbio_cobmr, "
        				+ "res.tiempoalm_cobmr, "
        				+ "res.tiempohoralm_cobmr, "
        			+ "'' as diferenciaAlm, "
        				+ "res.horafinhorario_cobmr, "
        				+ "res.horafinbiometrico_cobmr, "
        				+ "res.horafinband_cobmr,  "
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
        				+ "where  "
        				//+ "epar.activo_geedp=true AND "
        				+ "res.fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'  "
        						+ "and res.ide_aspvh IS NULL "
        				+ "GROUP BY EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
    					+ "EMP.APELLIDO_PATERNO_GTEMP,"
    					+ "EMP.APELLIDO_MATERNO_GTEMP,"
    					+ "EMP.PRIMER_NOMBRE_GTEMP, "
    					+ "EMP.SEGUNDO_NOMBRE_GTEMP,"
    					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
    					+ "DEPA.DETALLE_GEDEP, "
    					+ "res.ide_cobmr, "
    					+ "res.dia_cobmr, "
    					+ "res.fecha_evento_cobmr, "
    					+ "res.horainiciohorario_cobmr, "
    					+ "res.horainiciobiometrico_cobmr, "
    					+ "res.horainicioband_cobmr, "
    					+ "diferenciaEntradaTimbrado, "
    					+ "res.horainicioalm_cobmr, "
    					+ "res.horainicioalmbio_cobmr,  "
    					+ "res.horafinalm_cobmr,  "
    					+ "res.horafinalmbio_cobmr,  "
    					+ "res.tiempoalm_cobmr,  "
    					+ "res.tiempohoralm_cobmr,  "
    					+ "diferenciaAlm,  "
    					+ "res.horafinhorario_cobmr, "
    					+ "res.horafinbiometrico_cobmr, "
    					+ "res.horafinband_cobmr,  "
    					+ "diferenciaSalidaTimbrado, "
    					+ "GTT.DETALLE_GTTEM, "
    					+ "E_FECHA_SOLICITUD, "
    					+ "E_TTHH, "
    					+ "E_JEFE_INMEDIATO, "
    					+ "E_FECHA_DESDE, "
    					+ "E_FECHA_HASTA, "
    					+ "E_DETALLE, "
    					+ "E_TIPO_PERMISO, "
    					+ "E_NUM_HORAS, "
    					+ "E_HORA_DESDE, "
    					+ "E_HORA_HASTA, "
    					+ "FECHA_SOLICITUD_ALM, "
    					+ "TTHH_ALM,  "
    					+ "JEFE_INMEDIATO_ALM, "
    					+ "FECHA_DESDE_ALM, "
    					+ "FECHA_HASTA_ALM, "
    					+ "DETALLE_ALM, "
    					+ "TIPO_PERMISO_ALM, "
    					+ "NUM_HORAS_ALM, "
    					+ "HORA_DESDE_ALM, "
    					+ "HORA_HASTA_ALM, "
    					+ "FECHA_SOLICITUD_SAL, "
    					+ "TTHH_SAL, "
    					+ "JEFE_INMEDIATO_SAL, "
    					+ "FECHA_DESDE_SAL,  "
    					+ "FECHA_HASTA_SAL, "
    					+ "DETALLE_SAL, "
    					+ "TIPO_PERMISO_SAL, "
    					+ "NUM_HORAS_SAL, "
    					+ "HORA_DESDE_SAL, "
    					+ "HORA_HASTA_SAL,"
    					+ "DIFERENCIAENTRADA, "
    					+ "diferenciaSalidaAlm, "
    					+ "diferenciaEntradaAlm,"
    					+ "p_entrada_cobmr,  "
    					+ "p_salida_cobmr, "
    					+ "p_alm_cobmr  " 
        				   				
        				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");*/
        				tab_tabla.ejecutarSql();
        				utilitario.addUpdate("tab_tabla");
        				 
        TablaGenerica tab_reporte=getTablaReporte(fechaIni, fechaFin);
    	 
    	if (tab_reporte.isEmpty()) {
			utilitario.agregarMensajeInfo("Rango de Fechas no válidos", "No existen datos para el rango seleccionado");
		}
    	
    	
    	
	
    	
    	
    	
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
	    
	      
	    for (int i=0; i< tab_reporte.getTotalFilas(); i++) {   	
	   // TablaGenerica  tabMarcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
	    
	    //Obtengo los datos de cada fila que son entrada ,salida, salida almuerzo, entrada salida para validar
	    	//Variables de inicio
	    String ide_cobmr=tab_reporte.getValor(i,"IDE_COBMR");
	    int empleado= Integer.parseInt(tab_reporte.getValor(i,"IDE_GTEMP"));
        String fecha=tab_reporte.getValor(i,"FECHA_EVENTO_COBMR");
	    String horaEntrada=tab_reporte.getValor(i,"HORAINICIOBIOMETRICO_COBMR");
	    String banderaEntrada=tab_reporte.getValor(i,"HORAINICIOBAND_COBMR");
	    String almuerzoSalidaTimbre =tab_reporte.getValor(i,"HORAINICIOALMBIO_COBMR");
	    String almuerzoEntradaTimbre=tab_reporte.getValor(i,"HORAFINALMBIO_COBMR");
	    String almuerzoSalida=tab_reporte.getValor(i,"horainicioalm_cobmr");
	    String almuerzoEntrada=tab_reporte.getValor(i,"horafinalm_cobmr");
	    String tiempoAlmuerzo =tab_reporte.getValor(i,"TIEMPOALM_COBMR");
	    //tiempo de almuerzo
	    String tiempohoralm=tab_reporte.getValor(i,"TIEMPOHORALM_COBMR");
	    String banderaSalida=tab_reporte.getValor(i,"HORAFINBAND_COBMR");
	    String horaFin=tab_reporte.getValor(i,"HORAFINBIOMETRICO_COBMR");
	    String horaSalida=tab_reporte.getValor(i,"HORAFINBIOMETRICO_COBMR");
	    String horarioSalida=tab_reporte.getValor(i,"HORAFINHORARIO_COBMR");
        String horarioEntrada=tab_reporte.getValor(i,"HORAINICIOHORARIO_COBMR");
        double tiempoAlm=0.0;
        System.out.println(fecha+"   "+empleado);
	    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
	    String  permiso;
	    String  nro_horas;
	    boolean banderaEntradaIngreso=false,banderaSalidaIngreso=false,banderaAlmuerzo=false;
	    int valorTipo=0;
		
		//Bandera de Entrada
	    if (banderaEntrada=="" || banderaEntrada.isEmpty() || banderaEntrada.equals("SIN TIMBRE") || banderaEntrada.equals("ATRASADO")) {
			//Si la bandera se encuentra vacia
	    	if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty() || horarioEntrada.equals("null")) {
				//Busco si contiene horario asignado
	    		banderaEntradaIngreso=false;
			}else {
				
		    	if (horaEntrada==null || horaEntrada.equals("") || horaEntrada.isEmpty() || horaEntrada.equals("null")) {
		    		banderaEntradaIngreso=true;
					horaEntrada=horarioEntrada;
		    	}else {

					//Si tiene horario
					banderaEntradaIngreso=true;
					horaEntrada=horaEntrada;
				}
			}
		}
	    
	    
		//Bandera de Entrada
	    if (banderaEntrada=="" || banderaEntrada.isEmpty() || banderaEntrada.equals("SIN TIMBRE")) {
			//Si la bandera se encuentra vacia
	    	if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty() || horarioEntrada.equals("null")) {
				//Busco si contiene horario asignado
	    		banderaEntradaIngreso=false;
			}else {
				//Si tiene horario
				banderaEntradaIngreso=true;
				horaEntrada=horarioEntrada;
			}
		}
			 

	    
	    
	    
	    
	    if (banderaSalida.equals("") || banderaSalida.isEmpty() || banderaSalida.equals("SIN TIMBRE")) {
    			if (horarioSalida==null || horarioSalida.equals("") || horarioSalida.isEmpty() || horarioSalida.equals("null")) {
        				banderaSalidaIngreso=false;
    				}else {
    					banderaSalidaIngreso=true;
    					horaSalida=horarioSalida;
    				}
			
			}
	    
	    
	    double tiempoJustificadoAlm=0.0;
	    
		//Reviso si tengo almuerzo para lo cual tengo tres casos 30,60,0
		if (tiempohoralm.equals("0.5") || tiempohoralm.equals("1.0")) {
			//Si tengo almuerzo
			if (almuerzoSalidaTimbre!=null || !almuerzoSalidaTimbre.equals("") || !almuerzoSalidaTimbre.isEmpty() || !almuerzoSalidaTimbre.equals("SIN TIMBRE")) {
			if (almuerzoEntradaTimbre!=null || !almuerzoEntradaTimbre.equals("") || !almuerzoEntradaTimbre.isEmpty() || !almuerzoEntradaTimbre.equals("SIN TIMBRE") || !almuerzoEntradaTimbre.equals("JUSTIFICADO")  ) {
			
			if (tiempoAlmuerzo=="" || tiempoAlmuerzo.isEmpty() || tiempoAlmuerzo==null || tiempoAlmuerzo.equals("0.0")) {
				tiempoAlm=0;
			}else {
				tiempoAlm=Double.parseDouble(tiempoAlmuerzo);		
			}
			
			if (tiempohoralm.equals("0.5") && tiempoAlm>0.0) {
				tiempoJustificadoAlm=30;
				getPermisoTiempoAlm(fecha, empleado,tiempoAlm,tiempoJustificadoAlm,i,0.5);
	
				/*if (tiempoAlm>0.50) {
					getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalidaTimbre, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);

				}*/

			}
			    
			if (tiempohoralm.equals("1.0") && tiempoAlm>0.0) {
				getPermisoTiempoAlm(fecha, empleado,tiempoAlm,tiempoJustificadoAlm,i,1);
			    
				/*If (tiempoAlm>1.0) {
					getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalidaTimbre, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);
			}*/
			}
			
			}
			}
			
			
		}else {
			//No tengo almuerzo
			banderaAlmuerzo=false;    		    		
		}


	
		
	if (banderaAlmuerzo==true) {
		//Si tengo almuerzo
		if (!almuerzoSalidaTimbre.equals("") || almuerzoSalidaTimbre!=null || !almuerzoSalidaTimbre.isEmpty()) {

			
			if (almuerzoSalidaTimbre.equals("SIN TIMBRE")) {
				getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalida, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);
			
			}else {
			getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalidaTimbre, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);
			}
		}else {
			getPermisoHorarioAlmuerzo(fecha, empleado, almuerzoSalida, horarioEntrada,horarioSalida,ide_cobmr,tiempoJustificadoAlm,i);
		}
	}
	
	//Si tiene horario y hora de entrada	

		
		if (banderaEntradaIngreso==true) {
			if (banderaEntrada.equals("SIN TIMBRE")) {
			getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,i,0,ide_cobmr);
		}
		 
			//if (banderaEntrada.equals("SIN TIMBRE")) {
    	//		getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,i,1,ide_cobmr);		
    	//	}
		
		
			if (banderaEntrada.equals("ATRASADO")) {
				getPermisoHorario(fecha, empleado, horarioEntrada, horarioEntrada,horarioSalida,i,2,ide_cobmr);		
				}


		}



		if (banderaSalidaIngreso==true) {
			if(banderaSalida.equals("SIN TIMBRE")){
			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr,i,2);				
			}else {
			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr,i,0);
		}
		
		
		
		}
		if (banderaSalida.equals("ANTICIPADO")) {
			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr,i,1);		    			
		}
		
		
		//if (banderaSalida.equals("SIN TIMBRE") || banderaSalida.equals("")) {
		//	getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr,i,2);		
		//}
		
		/*    		if (banderaSalida.equals("EXTRA")) {
			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
		}
		
		if (banderaSalida.equals("FERIADO")) {
			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
		}
		
		*/    		


	}
    	
	
	
	
	
	
	
	
	   	}else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		}
		
	}
	else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	}		
	
	
	
	
	
	
	   //	uno();  	
	   	
	   	
	   	
	   	
	   	
//uno();
		


		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		//dos();
		
		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		//tres();
		
		
//cuatro();
		
//uno();
		


		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
	//	dos();
		
		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		//	tres();
		//	cuatro();
		
	//quinto();
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//DIFERENCIA ENTRE ENTRADA Y SALIDA
	
	public void unoOLD(){
		boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		String tiempoTrabajoXDia="";
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
		
			if ((entradaTimbre==null) || (entradaTimbre.isEmpty()) || (entradaTimbre.equals(""))  || (entradaTimbre.equals("JUSTIFICADO"))
					
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				entrada=true;
			}else {
				entrada=false;

			}
	
			if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) || (salidaTimbre.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}
			
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
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}else {

					
					
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaBiometrico),1))+" "+salidaTimbre);
			    	tiempoTrabajoXDia = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
			    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
			    	listaEmpleado.add(empleado);
					tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}
		    				}
			else {
				
			}
		}
	}
	
	//DIFERENCIA ENTRE ENTRADA Y SALIDA
	
	public void uno(){
		boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
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
			System.out.println("Error:"+fechaBiometrico+"  empleado: "+empleado);
			
			
			
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
		    	 
			    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

		    	//tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);
		    	//(Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    //	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}else {

					System.out.println(empleado+" dsdds"+fecha);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaBiometrico),1))+" "+salidaTimbre);
			    //	 tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio))/60);
				    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

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
		double tiempoTrabajoXDia=0.0;
		String entradaBandera="",salidaBandera="";
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			entradaBandera=tab_tabla.getValor(i, "P_ENTRADA_COBMR");
			String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
			String banderaSalida=tab_tabla.getValor(i, "horafinband_cobmr");
			
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
		
	/*		if ((entradaTimbre==null) || (entradaTimbre.isEmpty()) || (entradaTimbre.equals("")) || (entradaTimbre.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))		
					) {
				//si entrada vacia o sin datos
				entrada=true;
			}else {
				entrada=false;

			}
		*/	
			
// Si el horario no se ha pegado en las columnas
			if ((entradaHorario==null) || (entradaHorario.isEmpty()) || (entradaHorario.equals("")) || (entradaHorario.equals("JUSTIFICADO"))
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandEntradaHorario=true;
			}else {
				bandEntradaHorario=false;

			}
			
			
	
			
			//Si hay salida y entrada 
			if (entrada==false && bandEntradaHorario==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
				
				//Si hora de timbre es menor a la hora de entrada
				//if (entradaTimbre.compareTo(entradaHorario)<=0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
					//}	
				/*if (entradaTimbre.compareTo(entradaHorario)>0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);
        		}*/
        		
        		//Fin de tipo horas
	          // System.out.println("impresion de empleado :"+empleado+" horaEntrada  :"+fechaIniAlmBio+"    HoraTimbre: "+fechaFinAlmBio);
		    	//
				    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

			    //	 tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio))/60);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	//listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaEntradaTimbrado",""+tiempoTrabajoXDia);

		    				}
			else {
				
			}
		}
		
	}
	
	
	
	//DIFERENCIA ENTRE SALIDA HORARIO Y SALIDA TIMBRE

	public void tres(){
		boolean salida=false,bandSalidaHorario=false;

		/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
		*/
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();
		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		double tiempoTrabajoXDia=0.0;
		String entradaBandera="",salidaBandera="";
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));

			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			String banderaEntrada=tab_tabla.getValor(i, "horainicioband_cobmr");
			String banderaSalida=tab_tabla.getValor(i, "horafinband_cobmr");

			salidaBandera=tab_tabla.getValor(i, "P_SALIDA_COBMR");
			
			
		
		/*	if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) || (salidaTimbre.equals("JUSTIFICADO"))
					|| (entradaTimbre.equals("INGRESADO")) ) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}*/
			
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
			
// Si el horario no se ha pegado en las columnas
			if ((salidaHorario==null) || (salidaHorario.isEmpty()) || (salidaHorario.equals("")) || (salidaHorario.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandSalidaHorario=true;
			}else {
				bandSalidaHorario=false;

			}
			
			
			//Si hay salida y entrada 
			if (salida==false && bandSalidaHorario==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
				
			    	 
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
        		//Fin de tipo horas
	          // System.out.println("impresion de empleado :"+empleado+" horaSalida  :"+fechaIniAlmBio+"    HoraTimbre: "+fechaFinAlmBio);
		    	//tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio))/60);
				    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    //	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaSalidaTimbrado",""+tiempoTrabajoXDia);

		    				}
			else {
				
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
			
	
		
			if ((salidaTimbreAlm==null) || (salidaTimbreAlm.isEmpty()) || (salidaTimbreAlm.equals("")) || (salidaTimbreAlm.equals("S/A")) || (salidaTimbreAlm.equals("JUSTIFICADO"))
					|| (salidaTimbreAlm.equals("INGRESADO"))	) {
				//Si la salida no se encuentra marcada 
				salidaAlm=true;
			}else {
				salidaAlm=false;
			}
			
// Si el horario no se ha pegado en las columnas
			if ((salidaHorarioAlm==null) || (salidaHorarioAlm.isEmpty()) || (salidaHorarioAlm.equals("")) || (salidaHorarioAlm.equals("S/A")) || (salidaHorarioAlm.equals("JUSTIFICADO")) 
					|| (salidaTimbreAlm.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandSalidaHorarioAlm=true;
			}else {
				bandSalidaHorarioAlm=false;

			}
			
	
			
			
			/*if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) ) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}*/
		
			String[] parts = salidaTimbreAlm.split(" ");
			String part1 = parts[0]; // 123
			
			if (part1.equals("JUSTIFICADO")) {
			}else if (salidaTimbreAlm.equals("INGRESADO")) {
			///	System.out.println();	
				}
				
			else{

			//Si hay salida y entrada 
			if (salidaAlm==false && bandSalidaHorarioAlm==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
		
				//Si hora de timbre es menor a la hora de entrada
				//if (salidaTimbreAlm.compareTo(salidaHorarioAlm)<=0) {
					// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
			    	// fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
			    	 
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorarioAlm);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbreAlm);
					//}	
				/*if (salidaTimbreAlm.compareTo(salidaHorarioAlm)>0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorarioAlm);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbreAlm);
        		}*/
        		//Fin de tipo horas
	        //   System.out.println("impresion de empleado :"+empleado+" horaSalidaAlm  :"+fechaIniAlmBio+"    HoraTimbreAlm: "+fechaFinAlmBio);
		    	//tiempoTrabajoXDia = Double.parseDouble(obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio))/60;
				    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	//listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaSalidaAlm",""+tiempoTrabajoXDia);

		    				}
			else {
				
			}
		}
			
		}
		
	}
	
	
	
	//DIFERENCIA ENTRE ENTRADA DEL ALMUERZO Y ENTRADA ALMUERZO TIMBRE
		public void quinto(){
			boolean entradaAlm=false,bandEntradaHorarioAlm=false;

			/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
					+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
					+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
			*/
			 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
			 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


			String entradaHorarioAlm="",entradaTimbreAlm="",fechaBiometrico="";
			//double tiempoTrabajoXDia=0;
			double tiempoTrabajoXDia=0.0;

			/*for (int i = 0; i < tab_marcaciones.getTotalFilas(); i++) {
				int empleado= Integer.parseInt(tab_marcaciones.getValor(i, "IDE_GTEMP"));
				entradaHorario=tab_marcaciones.getValor(i, "HORAINICIOHORARIO_COBMR");
				entradaTimbre=tab_marcaciones.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
				salidaHorario=tab_marcaciones.getValor(i, "HORAFINHORARIO_COBMR");
				salidaTimbre=tab_marcaciones.getValor(i, "HORAFINBIOMETRICO_COBMR");
				fechaBiometrico=tab_marcaciones.getValor(i, "FECHA_EVENTO_COBMR");*/

			for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
				int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
				//entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
				//entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
				entradaHorarioAlm=tab_tabla.getValor(i, "HORAFINALM_COBMR");
				entradaTimbreAlm=tab_tabla.getValor(i, "HORAFINALMBIO_COBMR");
				fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
				
		
			
				if ((entradaTimbreAlm==null) || (entradaTimbreAlm.isEmpty()) || (entradaTimbreAlm.equals("")) || (entradaTimbreAlm.equals("S/A")) 
						|| (entradaTimbreAlm.equals("INGRESADO"))	)  {
					//Si la salida no se encuentra marcada 
					entradaAlm=true;
				}else {
					entradaAlm=false;
				}
				
	// Si el horario no se ha pegado en las columnas
				if ((entradaHorarioAlm==null) || (entradaHorarioAlm.isEmpty()) || (entradaHorarioAlm.equals("")) || (entradaHorarioAlm.equals("S/A")) 
						|| (entradaTimbreAlm.equals("INGRESADO"))) {
					//si entrada vacia o sin datos
					bandEntradaHorarioAlm=true;
				}else {
					bandEntradaHorarioAlm=false;

				}
				

				
				String[] parts = entradaTimbreAlm.split(" ");
				String part1 = parts[0]; // 123
				
				if (part1.equals("JUSTIFICADO")) {
				}else if (entradaHorarioAlm.equals("INGRESADO")) {
				//	System.out.println();	
					
				}else{
				//Si hay salida y entrada 
				if (entradaAlm==false && bandEntradaHorarioAlm==false) {
					Date fechaIniAlmBio=null;
					Date fechaFinAlmBio=null;
					

					
					
					//Si hora de timbre es menor a la hora de entrada
					//if (entradaTimbreAlm.compareTo(entradaHorarioAlm)<=0) {
						// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
				    	// fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
				    	 
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
						//}	
					/*if (entradaTimbreAlm.compareTo(entradaHorarioAlm)>0) {
						// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
				    	 //fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
					
					}*/
					
					
					//Fin de tipo horas
		       //    System.out.println("impresion de empleado :"+empleado+" horaEntradaAlm  :"+fechaIniAlmBio+"    HoraTimbreEntradaAlm: "+fechaFinAlmBio);
			    //	tiempoTrabajoXDia = (Double.parseDouble(obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio))/60);
					    	tiempoTrabajoXDia = (utilitario.getDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio)/60);

				    	 
			    //	listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
			    	//listaEmpleado.add(empleado);
					tab_tabla.setValor(i,"diferenciaEntradaAlm",""+tiempoTrabajoXDia);


			    				}
				else {
					
				}
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
    			+ "where ide_gtemp="+empleado+"  and fecha_desde_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -15))+"' and "
        		+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 15))+"' and anulado_aspvh =false ");
    	
    	
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
    			
    			//tipo permiso por dias 
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
        	
        		//Actualizo si tiene justificada la marcacion de entrada
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',p_alm_cobmr=0.0,"
        					+ "horainicioband_cobmr='JUSTIFICADO' "
        					+ "where ide_cobmr="+ide_cobmr);
        	
        		
    			
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
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',p_alm_cobmr=0.0,   "
        					+ "horainicioband_cobmr='JUSTIFICADO' "
        					+ "where ide_cobmr="+ide_cobmr);
        	
        		
    	
    				
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
        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',p_alm_cobmr=0.0, "
        					+ "horainicioband_cobmr='JUSTIFICADO' "
        					+ "where ide_cobmr="+ide_cobmr);
        	
        		
    	
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
						String myDateString = horaTimbre; //La hora con forma de String

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
	    	       		  	    	        
	    	       	
	    	       		

	    				if (ide_gtgre==3) {
							
	    					
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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
	    	        					+ "horainicioband_cobmr='JUSTIFICADO'"
	    	        					+ "where ide_cobmr="+ide_cobmr);
	    	        	 
	    	        		
	    	    	
	    					
	    					
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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
	    	        			+ "where ide_cobmr="+ide_cobmr);
	    	        	    	
	    					
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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
	    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
	    	        					+ "where ide_cobmr="+ide_cobmr);
	    	        	
	    	        		
	    					}
								    		

							   }
							
							
//////////////////////////////////////////////////////////////////////AQUI CUANDO NO ES NOCTURNO/////////////////////////////////////////////////////////////
	    				}else {
							
						
	    	       		
					/*	if ((hora_desde_aspvh.compareTo(horaTimbre)<=0) && (hora_hasta_aspvh.compareTo(horaTimbre)>=0)) {
							horaTimbre=""+hora+":"+min+"";
							
						}else {
							estadoEntrada=5;
							 //horaTimbre=""+hora+":"+min+"";
							//Sumar horas en fecha
						}*/	 
		         	
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

						//}//Else si no tiene turno nocturno
						
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					System.out.println("Error al formatear hora ");
					}
    				
    				

    				if (estadoEntrada==1 || estadoEntrada==0) {
    			
    				if (hora_desde_aspvh.compareTo(horaTimbre)>=0 && hora_hasta_aspvh.compareTo(horaHorarioSalida)<=0) {
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    	    	
    					
    					
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    				
    					
    					
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
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        	
    	        		
    					}
    					
    				//	tab_tabla.ejecutarSql();
    				//	utilitario.addUpdate("tab_tabla");

    				}else if(hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)<0){
        				double numerohoras=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_hasta_aspvh), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_desde_aspvh));
        				System.out.println("numero horas"+numerohoras);
        				double numerohorasPasadas=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaTimbre), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaHorarioEntrada));
        				System.out.println("numero horas"+numerohorasPasadas);
    					double resultado = numerohorasPasadas-numerohoras;
        				System.out.println("resultado "+resultado);
        				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
        						+ "where ide_cobmr="+ide_cobmr);
        	        	

        				
        				
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    	    	
    					
    					
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    				
    					
    					
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
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        	
    	        		
    					}

        				
        				
        				
    					
    				}			
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					//break;
    				}
    				
    				}else if(estadoEntrada==2){
    				
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    	    	
    					
    					
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',"
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "   where ide_cobmr="+ide_cobmr);
    	        		
    				
    					
    					
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
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',"
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "   where ide_cobmr="+ide_cobmr);
    	        	
    	        		
    					}
    					
    				//	tab_tabla.ejecutarSql();
    				//	utilitario.addUpdate("tab_tabla");

    				}	
    				
    				else 
    					if(hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)<0){
        				double numerohoras=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_hasta_aspvh), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_desde_aspvh));
        				//System.out.println("numero horas"+numerohoras);
        				double numerohorasPasadas=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaTimbre), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaHorarioEntrada));
        				//System.out.println("numero horas"+numerohorasPasadas);
    					double resultado = numerohorasPasadas-numerohoras;
        				//System.out.println("resultado "+resultado);
        				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
        	        	
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    	    	
    					
    					
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    				
    					
    					
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
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',   "
    	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        	
    	        		
    					}
    					
    					
    				}	 
    					if(hora_desde_aspvh.compareTo(horaTimbre)>=0 && hora_hasta_aspvh.compareTo(horaTimbre)>0){
            				double numerohoras=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_hasta_aspvh), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+hora_desde_aspvh));
            				System.out.println("numero horas"+numerohoras);
            				double numerohorasPasadas=utilitario.getDiferenciaHoras(getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaTimbre), getFechaAsyyyyMMdd(utilitario.getFechaActual()+" "+horaHorarioEntrada));
            				System.out.println("numero horas"+numerohorasPasadas);
        					double resultado = numerohorasPasadas-numerohoras;
            				System.out.println("resultado "+resultado);
            				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"'   where ide_cobmr="+ide_cobmr);
            	        	
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
        	        	
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',"
        	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
        	        					+ "where ide_cobmr="+ide_cobmr);
        	        		
        	    	
        					
        					
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
        	        	
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',"
        	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
        	        					+ "   where ide_cobmr="+ide_cobmr);
        	        		
        				
        					
        					
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
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_entrada_cobmr='"+horaHorarioEntrada+"',"
        	    	        			+ "horainicioband_cobmr='JUSTIFICADO' "
        	        					+ "   where ide_cobmr="+ide_cobmr);
        	        	
        	        		
        					}
    				
    				
    			
    					}else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					//break;
    				}
    				
    					
    					
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
    	String horaTimbreSalida="";
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	//tabla que contiene los permisos para justificar
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_desde_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -30))+"' and "
    					
    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 30))+"' and anulado_aspvh =false");
    	
    	
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
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"',   "
	        					+ "HORAFINBAND_COBMR='JUSTIFICADO' "
	        					+ "where ide_cobmr="+ide_cobmr);
            		
        			
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
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'  "
	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
	        					+ " where ide_cobmr="+ide_cobmr);
            		
        	
        				
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
            	
	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'"
	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
	        					+ "   where ide_cobmr="+ide_cobmr);
            		
        	
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
						String myDateString = horaTimbre; //La hora con forma de String

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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"' "
	    	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
	    	        					+ "  where ide_cobmr="+ide_cobmr);
	    	        	 
	    	        		
	    	    	
	    					
	    					
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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   "
	    	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
	    	        					+ "where ide_cobmr="+ide_cobmr);

	    					
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
	    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   "
	    	        			+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
	    	        			+ "where ide_cobmr="+ide_cobmr);
	    	        	
	    	        		
	    					}
								    		

							   }
							
							
//////////////////////////////////////////////////////////////////////AQUI CUANDO NO ES NOCTURNO/////////////////////////////////////////////////////////////
	    				}else {
							
						
	    	       		
	    	       		
							
							
							
	    	       			
												
					//	if (hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
					//		horaTimbre=""+hora+":"+min+"";
						
						//}else {
						//	 horaTimbre=""+hora+":"+min+"";
							//Sumar horas en fecha
							 
		         			    String fechaHoraHorarioFinEntrada = fecha+" "+hora_desde_aspvh;

		           	Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada),0);
		           	String minTolerancia="",horaTolerancia="";
		        	
		           	
				    String fechaHoraHorarioFinSalida = fecha+" "+hora_hasta_aspvh;

		           	Date finSalida=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinSalida),-15);
		           	String minToleranciaSalida="",horaToleranciaSalida="";
		           
		           	
		           	
		           	/*Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(finSalida);   // assigns calendar to given date
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
						
						
						 String horaTimbreEntrada=""+horaTolerancia+":"+minTolerancia+"";
						
						
						 
						 
						 
						 
						 
						 
				           	Calendar calendarSalida = GregorianCalendar.getInstance(); // creates a new calendar instance
							calendar.setTime(finSalida);   // assigns calendar to given date
							calendar.getTime();
							//Podemos recuperar la hora, minuto, etc. de la fecha 
							//int minutes=calendarEntrada.get(Calendar.MINUTE);
							//int hours = calendar.get(Calendar.HOUR_OF_DAY);
							int minutesSalida=finSalida.getMinutes();
				           	
							int hoursSalida = finSalida.getHours();
							if (minutesSalida<10) {
								   minToleranciaSalida="0"+minutesSalida+":00";

								}else {
									 minToleranciaSalida=""+minutesSalida+":00";
								}
												
								
								if (hoursSalida<10) {
									horaToleranciaSalida="0"+hoursSalida;

								}else {
									horaToleranciaSalida=""+hoursSalida;
						
						}

								
								 horaTimbreSalida=""+horaToleranciaSalida+":"+minToleranciaSalida+"";
														 
						 
						*/ 
						 
						 
						 
						 

						}//Else si no tiene turno nocturno
						
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					System.out.println("Error al formatear hora ");
					}

    				
    				
    				
    				
    				
    				
    				   //String fechaTimbreNocturno=fecha+" "+hora_desde_aspvh;
    				
    				
    				
    				
    				
   		           	//Date finEntrada=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaTimbreNocturno),-15);
   		           	String minTolerancia="",horaTolerancia="";
    				
    				//utilitario.getHora(getFechaAsyyyyMMddHHmmss(finEntrada));
    	
    				
    				if (estadoEntrada==0) {
    					if (hora_desde_aspvh.compareTo(horaHorarioEntrada)>=0 && hora_hasta_aspvh.compareTo(horaHorarioSalida)<=0) {
    						//if (hora_desde_aspvh.compareTo(horaHorarioEntrada)>=0 && hora_hasta_aspvh.compareTo(horaTimbreSalida)<=0) {
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
        	        	
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   "
        	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
        	        					+ "where ide_cobmr="+ide_cobmr);
        	        		
        	    	
        					
        					
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
        	        	
    				
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'"
        	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
        	        					+ "   where ide_cobmr="+ide_cobmr);
        				
        					
    				
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
        	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'"
        	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
        	        					+ "   where ide_cobmr="+ide_cobmr);
    				
    				
        					}
      			
    					}
    	    					
					}
						
    					if (estadoEntrada==1) {

    					    String fechaHoraHorarioFinEntrada = fecha+" "+horaTimbre;

    			           	Date finEntrada1=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioFinEntrada),-30);
    			           	String minTolerancia1="",horaTolerancia1="";
    			        	
    			           	
    					     			           	
    			           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
    			           	calendarEntrada.setTime(finEntrada1);   // assigns calendar to given date
    			           	calendarEntrada.getTime();
    						//Podemos recuperar la hora, minuto, etc. de la fecha 
    						//int minutes=calendarEntrada.get(Calendar.MINUTE);
    						//int hours = calendar.get(Calendar.HOUR_OF_DAY);
    						int minutes=finEntrada1.getMinutes();
    			           	
    						int hours = finEntrada1.getHours();
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
    							
    							
    							 String horaTimbreEntrada1=""+horaTolerancia+":"+minTolerancia+"";

    						
    						
    						
    						
    						
    						
    						
    						
    						
    						
    						
    						
    						if (horaTimbreEntrada1.compareTo(hora_desde_aspvh)<=0 && hora_hasta_aspvh.compareTo(horaTimbreSalida)>=0) {
    						//if (hora_desde_aspvh.compareTo(horaHorarioEntrada)>=0 && hora_hasta_aspvh.compareTo(horaTimbreSalida)<=0) {
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
    	        	
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   "
    	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        		
    	    	
    					
    					
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
    	        	
    	        		
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'"
    	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
    	        					+ "   where ide_cobmr="+ide_cobmr);
    				
    					
    					
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
    	        			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set p_salida_cobmr='"+horaHorarioSalida+"'   "
    	        					+ ",HORAFINBAND_COBMR='JUSTIFICADO' "
    	        					+ "where ide_cobmr="+ide_cobmr);
    	        	
    	        		
    					}
    										
					}
    			  										
    					valorRetorno= true;
    					
    					
    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    					//break;
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
    	
    	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_solicitud_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,fecha_desde_aspvh,nro_dias_aspvh,aprobado_aspvh  "
    			+ "from asi_permisos_vacacion_hext "
    			+ "where ide_gtemp="+empleado+"  and fecha_desde_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
        		+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 30))+"' and anulado_aspvh =false ");
    	
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
								+ "p_alm_cobmr="+tiempoJustificado+" "
		    					+ ",horafinalmbio_cobmr='JUSTIFICADO' "
								+ "where ide_cobmr="+ide_cobmr);
	    		
        			
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
            								+ "p_alm_cobmr="+tiempoJustificado+"   "
            								+ ",horafinalmbio_cobmr='JUSTIFICADO' "
            								+ "where ide_cobmr="+ide_cobmr);
            	    		
            	
            		
        	
        				
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
            								+ "p_alm_cobmr="+tiempoJustificado+" "
            								+ ",horafinalmbio_cobmr='JUSTIFICADO' "
            								+ "where ide_cobmr="+ide_cobmr);
            	    		
            	
            	
            		
        	
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

    				
					
    				String fechaMarcacion=fecha+" "+horaTimbre;
    				
        			Date finEntradaAlmuerzo=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(fechaMarcacion),-20);
		           	String minTolerancia="",horaTolerancia="";
		           	String horaResultado="",horaMarcacionSalidaAlm="";
		           	
		           	Calendar calendarEntrada = GregorianCalendar.getInstance(); // creates a new calendar instance
		           	calendarEntrada.setTime(finEntradaAlmuerzo);   // assigns calendar to given date
		           	calendarEntrada.getTime();
					//Podemos recuperar la hora, minuto, etc. de la fecha 
					//int minutes=calendarEntrada.get(Calendar.MINUTE);
					//int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes=finEntradaAlmuerzo.getMinutes();
		            	
					int hours = finEntradaAlmuerzo.getHours();
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
						
						horaMarcacionSalidaAlm=horaTolerancia+":"+minTolerancia+"";

						horaTimbre=horaMarcacionSalidaAlm;



    				
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
    								+ "p_alm_cobmr="+tiempoJustificado+"  "
			    					+ ",horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
    	    		
        	        		
    	    	
    					
    					
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
    								+ "p_alm_cobmr="+tiempoJustificado+" "
    								+ ",horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
    	    		
                		        	
    	        		
    				
    					
    					
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
    								+ "p_alm_cobmr="+tiempoJustificado+" "
			    					+ ",horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
    	    		
    	        		
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
    					//break;
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
    					
    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 30))+"' and anulado_aspvh =false");

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
            								+ "p_alm_cobmr="+tiempoJustificado+" "
		    		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
            								+ "where ide_cobmr="+ide_cobmr);
            	    		
                    			
            			
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
								+ "p_alm_cobmr="+tiempoJustificado+"   "
   		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
								+ "where ide_cobmr="+ide_cobmr);
        	
        				
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
								+ "p_alm_cobmr="+tiempoJustificado+"   "
   		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
								+ "where ide_cobmr="+ide_cobmr);
            		
        	
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

    		
    				if (hora_desde_aspvh.compareTo(horaHorarioEntrada)>=0 &&  hora_hasta_aspvh.compareTo(horaTimbre)>=0 &&   hora_hasta_aspvh.compareTo(horaHorarioSalida)<=0) {
    				
    										
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
    								+ "p_alm_cobmr=0.0 "
       		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
    					
    					
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
    								+ "p_alm_cobmr=0.0   "
       		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
                		        	
    	        		
    				
    					
    					
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
    								+ "p_alm_cobmr=0.0   "
       		    					+ ",horainicioalmbio_cobmr='JUSTIFICADO',horafinalmbio_cobmr='JUSTIFICADO' "
    								+ "where ide_cobmr="+ide_cobmr);
    					}
    					//justifico;horaHorarioSalidahoraHorarioSalida
    				
    					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
    					//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"',horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
    					valorRetorno= true;
    				}	
    				else {
    					valorRetorno=false;
    					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
    				
    					
    					
    					
    					//break;
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
	
		    private void getPermisoTiempoAlm(String fecha,int empleado,double tiempoAlm,double tiempoJustificado,int j,double tiempo){
		boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

	    	double uno=tiempo;
	    	double dos=tiempoAlm;

					
					double tres =uno-dos;
					tab_tabla.setValor(j, "diferenciaAlm",""+tres);
   	    	
	    }//Fin de if si cump






			@Override
			public void abrirListaReportes() {
	
				rep_reporte.dibujar();
			}






			@Override
			public void aceptarReporte() {
				String ide_gtemp="";
				if (rep_reporte.getReporteSelecionado().equals("Reporte Marcaciones Por Empleado")){
					if (rep_reporte.isVisible()){
						p_parametros=new HashMap();
						rep_reporte.cerrar();
						dia_fecha_marcaciones.dibujar();
				
					
					}else if (dia_fecha_marcaciones.isVisible()) {
						fechaReporteIni=cal_fecha_inicial_marcaciones.getFecha();
						fechaReporteFin=cal_fecha_final_marcaciones.getFecha();
						set_empleado.setSeleccionTabla(empleadosConsulta(),"IDE_GTEMP");
						set_empleado.getTab_seleccion().ejecutarSql();
						set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setNombreVisual("DOC. IDENTIFICACIÓN");
						set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("NOMBRES Y APELLIDOS");
						set_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
						set_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
						set_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
						set_empleado.getTab_seleccion().getColumna("DETALLE_GTTEM").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("DETALLE_GTTEM").setNombreVisual("TIPO EMPLEADO");

						set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
						//fechaReporteIni=cal_fecha_inicial_marcaciones.getFecha();
						//fechaReporteFin=cal_fecha_final_marcaciones.getFecha();
						p_parametros.put("fechaIni",fechaReporteIni);
						p_parametros.put("fechaFin",fechaReporteFin);
					dia_fecha_marcaciones.cerrar();
					set_empleado.dibujar();
					}else if (set_empleado.isVisible()) {
						ide_gtemp=set_empleado.getSeleccionados();
						p_parametros.put("ide_gtemp",ide_gtemp);
						p_parametros.put("titulo","DETALLE MARCACIONES POR EMPLEADO");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						set_empleado.cerrar();
						sef_reporte.dibujar();
						
					}
				}
			}

		    
		    
		    
		    
public void aceptarFechas(){
	dia_fecha_marcaciones.cerrar();
	utilitario.addUpdate("dia_fecha_marcaciones");
	set_empleado.dibujar();
}
		    
		    
public String empleadosConsulta(){

	
	
	String sql_empleadosActivos="SELECT  "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP,  "
			+ "GTT.DETALLE_GTTEM "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM     "
			+ "where cbmr.fecha_evento_cobmr between '"+getFechaReporteIni()+"' AND '"+getFechaReporteFin()+"'   "
			+ "and  EPAR.ACTIVO_GEEDP=TRUE  "
			+ "group by "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
			+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM "
			+ "ORDER BY cbmr.ide_gtemp ASC";
	
	return sql_empleadosActivos;
	
}






public Reporte getRep_reporte() {
	return rep_reporte;
}






public void setRep_reporte(Reporte rep_reporte) {
	this.rep_reporte = rep_reporte;
}






public SeleccionFormatoReporte getSef_reporte() {
	return sef_reporte;
}






public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
	this.sef_reporte = sef_reporte;
}






public String getFechaReporteIni() {
	return fechaReporteIni;
}






public void setFechaReporteIni(String fechaReporteIni) {
	this.fechaReporteIni = fechaReporteIni;
}






public String getFechaReporteFin() {
	return fechaReporteFin;
}


public void setFechaReporteFin(String fechaReporteFin) {
	this.fechaReporteFin = fechaReporteFin;
}

		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
}






















