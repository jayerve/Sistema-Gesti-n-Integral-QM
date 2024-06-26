/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.print.DocFlavor.STRING;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.apache.poi.util.TempFile;
import org.codehaus.groovy.util.ListHashMap;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Pantalla;
import pckUtilidades.CConversion;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.TablaGrid;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import paq_contabilidad.ejb.ServicioContabilidad;



public class pre_registro_vacacion extends Pantalla {

	 
	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	
	private Tabla tab_tabla_vacacion = new Tabla();
	private Tabla tab_tabla_detalle_vacaccion = new Tabla();
	private Tabla tab_tabla3 = new Tabla();	
	private Tabla tab_tabla_resumen_vacacion_empleado = new Tabla();	
	private Tabla 	tab_asi_vacacion_resumen_empleado = new Tabla();	
	private Tabla 	tab_dias_sin_timbre = new Tabla();	

	private Integer anioVacacion=0,mesVacacion=0,diaVacacion=0;
	private Integer anioVacacionSalida=0,mesVacacionSalida=0,diaVacacionSalida=0;
	
	private AutoCompletar aut_empleado=new AutoCompletar();
	private Consulta con_ver_vacaciones = new Consulta();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Consulta con_ver_ficha_vacacion = new Consulta();
	private Dialogo dia_aplica_vacion = new Dialogo();

	double division;
	
	
	// variables para calculo de vacaciones
	private Integer nda;
	private Double numeroDiasVacacionXAnio = 0.0;
	private Integer ide_gttem;
	private Integer nde;
	private Integer ndeInicialFechaCalculoDiasTomados;
	private double numeroDiasTomados;
	private int p_asi_dias_max_vacaciones_losep;
	private int p_asi_dias_max_vacaciones_codigo_trabajo;
	private int p_asi_dias_anio;
	
	private double dias_pendientes;
	// Calculo de de dias tomados por el empleado
	double numeroDiasTomadosInicial;
	double totalNumeroDiasTomadosInicial;
	double diasPendientesInicialAjuste;
	double nroDiasAjustePeriodo;
	double dias_sumados_aplicados_vacacion;
	private double numeroDiasTomadosTemporal;
	double numeroDiasTomadosFinSemana;
	// Ajuste inicial de dias

	double calculaParteDecimal = 0.0;
	int calcula_dias_vacacion = 0;
	int enteroDias = 0;
	double valor = 0.0;
	int calculoDias = enteroDias = 0;
	int enteroSuma = 0;
	// crear en parametros
		double calculo_dias_max_aplica = 0.0;
		double diasGraciaPuedeTomar = 0.0;
		int numeroInicioFinesSemana = 0;
		int numeroInicioFinesSemanaSolicitados = 0;
		int numeroInicioFinesSemanaPendientes = 0;

		private Object objMatriz;
		private List<Double[]> matriz;
		private Double[] obj = new Double[4];
		
		private Etiqueta etiDiasAcumulados= new Etiqueta("");
		private Etiqueta etiDiasDescontados= new Etiqueta("");
		private Etiqueta etiDiasPendientes= new Etiqueta("");
		
		private Etiqueta etiNroFinesSemana=new Etiqueta("");
		private Etiqueta etiDescuentoFinesSemana=new Etiqueta("");
		private Etiqueta etiNroFinesSemanaPendientes=new Etiqueta("");
		private Etiqueta resumenVacaciones1=new Etiqueta();
		private Etiqueta resumenVacaciones2=new Etiqueta();
		private Etiqueta resumenVacaciones3=new Etiqueta();
		private Etiqueta etiDiasAdicionales = new Etiqueta();
		private Etiqueta periodo = new Etiqueta();
		private Etiqueta dias_vacacion = new Etiqueta();
		private Etiqueta dias_vac_tomados = new Etiqueta();
		private Etiqueta dias_vac_pendientes = new Etiqueta();


		private Etiqueta resumenVacaciones=new Etiqueta();
		PanelGrid gri_resumen_vacaciones_periodo =new PanelGrid();
		PanelGrid gri_anular_dias_solicitados = new PanelGrid();
		PanelGrid gri_resumen_vacaciones_cabecera = new PanelGrid();
		Grid gri_resumen_vacaciones_espaciador=new Grid();
		Grid gri_titulo=new Grid();

	    int bandEntrada=0,bandSalida=0;
		private double totalNumeroDiasAjuste;
			
		File result; 
		WritableWorkbook archivo_xls ; 
		WritableSheet hojaPlantilla_xls; 
		WritableSheet hojaAux_xls;
		
		String str_fecha_actual=utilitario.getFechaActual();
		int fila =0, columna=0;
		boolean banderaExcedeDias=false;
		private SeleccionCalendario sec_rango_fechas=new SeleccionCalendario();
		
		private Dialogo seleciona_anio = new Dialogo();
		private Combo com_anio=new Combo();
	
	public pre_registro_vacacion() {        
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		rep_reporte.setId("rep_reporte"); 
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		bar_botones.agregarReporte();

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		Boton bot_ver_vacaciones=new Boton();
		
		bot_ver_vacaciones.setValue("VER VACACIONES");
		bot_ver_vacaciones.setMetodo("verVacaciones");
		
		Boton bot_auxiliar_vacaciones=new Boton();
		
		bot_auxiliar_vacaciones.setValue("AUXILIAR VACACIONES");
		bot_auxiliar_vacaciones.setMetodo("generarReporteXls");
		
		
		
		//PanelGrid panGri = new PanelGrid();
		gri_resumen_vacaciones_periodo.setColumns(1);
		gri_resumen_vacaciones_periodo.getChildren().add(periodo);
		
		//Titulo de Tabla de Vacaciones
		gri_titulo.setColumns(1);
		gri_titulo.getChildren().add(new Etiqueta("TABLA VACACIONES"));
		gri_titulo.setStyle("font-size:12px;font-weight: bold;border:2px;color:red;");

		//Espacios para dar formato a los datos del empleado
		gri_resumen_vacaciones_espaciador.setColumns(4);
		gri_resumen_vacaciones_espaciador.getChildren().add(new Etiqueta("<br></br>"));
		gri_resumen_vacaciones_espaciador.getChildren().add(new Etiqueta("<br></br>"));

	
		//Titulo de Tabla de Vacaciones
		
		PanelGrid panGri = new PanelGrid();
		gri_resumen_vacaciones_cabecera.setColumns(5);
		gri_resumen_vacaciones_cabecera.getChildren().add(new Etiqueta("PERIODO"));
		gri_resumen_vacaciones_cabecera.getChildren().add(new Etiqueta("NRO DIAS VACACION"));
		gri_resumen_vacaciones_cabecera.getChildren().add(new Etiqueta("NRO DIAS TOMADOS"));
		gri_resumen_vacaciones_cabecera.getChildren().add(new Etiqueta("NRO DIAS PENDIENTES"));
		gri_resumen_vacaciones_cabecera.getChildren().add(new Etiqueta("DIA EXTRA"));
		gri_resumen_vacaciones_cabecera.setStyle("font-size:12px;font-weight: bold;border:2px");


		//Resumen de Vacaciones de cada empleado
		gri_anular_dias_solicitados.setColumns(4);
		gri_anular_dias_solicitados.setStyle("font-size:12px;font-weight: bold;border:2px");

		//dialogo de Resumen de Vacaciones 	
		dia_aplica_vacion.setId("dia_aplica_vacion");
		dia_aplica_vacion.setTitle("RESUMEN DE VACACIONES");
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(gri_resumen_vacaciones_espaciador);
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(gri_titulo);
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(gri_resumen_vacaciones_cabecera);
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(gri_resumen_vacaciones_periodo);
	    dia_aplica_vacion.getBot_aceptar().setMetodo("setear");			
		dia_aplica_vacion.getBot_cancelar().setMetodo("setear");
		dia_aplica_vacion.setWidth("60");
		dia_aplica_vacion.setHeight("50");
		agregarComponente(dia_aplica_vacion);
		
		      
		tab_dias_sin_timbre.setId("tab_dias_sin_timbre");
		tab_dias_sin_timbre.setTabla("con_biometrico_marcaciones_resumen", "ide_cobmr",4);
		tab_dias_sin_timbre.getColumna("ide_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("ide_gtemp").setVisible(false);
		tab_dias_sin_timbre.getColumna("novedad_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("aprueba_hora_extra_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("p_entrada_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("p_salida_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("p_alm_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("inconsistencia_biometrico_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("ide_aspvh").setVisible(false);
		tab_dias_sin_timbre.getColumna("horafinextra_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("recargonocturno25_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("recargonocturno100_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("biometrico_entrada_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("biometrico_alm_entrada").setVisible(false);
		tab_dias_sin_timbre.getColumna("biometrico_alm_salida").setVisible(false);
		tab_dias_sin_timbre.getColumna("biometrico_salida_cobmr").setVisible(false);
		tab_dias_sin_timbre.getColumna("tiempohoralm_cobmr").setVisible(false);


		tab_dias_sin_timbre.getColumna("fecha_evento_cobmr").setNombreVisual("FECHA");
		tab_dias_sin_timbre.getColumna("fecha_evento_cobmr").setLongitud(15);
		tab_dias_sin_timbre.getColumna("fecha_evento_cobmr").setOrden(1);
		
		tab_dias_sin_timbre.getColumna("dia_cobmr").setNombreVisual("DIA");
		tab_dias_sin_timbre.getColumna("dia_cobmr").setLongitud(12);
		tab_dias_sin_timbre.getColumna("dia_cobmr").setOrden(2);

		tab_dias_sin_timbre.getColumna("horainiciohorario_cobmr").setNombreVisual("HORA.ENT");
		tab_dias_sin_timbre.getColumna("horainiciohorario_cobmr").setLongitud(8);
		tab_dias_sin_timbre.getColumna("horainiciohorario_cobmr").setOrden(3);

		
		
		tab_dias_sin_timbre.getColumna("horainiciobiometrico_cobmr").setNombreVisual("TIM.ENT");
		tab_dias_sin_timbre.getColumna("horainiciobiometrico_cobmr").setLongitud(12);
		tab_dias_sin_timbre.getColumna("horainiciobiometrico_cobmr").setOrden(4);
		
		
		
		tab_dias_sin_timbre.getColumna("horainicioband_cobmr").setNombreVisual("EST.ENT");
		tab_dias_sin_timbre.getColumna("horainicioband_cobmr").setLongitud(15);
		tab_dias_sin_timbre.getColumna("horainicioband_cobmr").setOrden(5);
		
		
		tab_dias_sin_timbre.getColumna("horainicioalm_cobmr").setNombreVisual("SAL.ALM");
		tab_dias_sin_timbre.getColumna("horainicioalm_cobmr").setOrden(6);
		tab_dias_sin_timbre.getColumna("horainicioalm_cobmr").setLongitud(8);

		
		tab_dias_sin_timbre.getColumna("horafinalm_cobmr").setNombreVisual("ENT.ALM");
		tab_dias_sin_timbre.getColumna("horafinalm_cobmr").setLongitud(8);
		tab_dias_sin_timbre.getColumna("horafinalm_cobmr").setOrden(7);
		
		
		
		tab_dias_sin_timbre.getColumna("horainicioalmbio_cobmr").setNombreVisual("TIM.SAL.ALM");
		tab_dias_sin_timbre.getColumna("horainicioalmbio_cobmr").setLongitud(12);
		tab_dias_sin_timbre.getColumna("horainicioalmbio_cobmr").setOrden(8);
		
		
		tab_dias_sin_timbre.getColumna("horafinalmbio_cobmr").setNombreVisual("TIM.SAL.ALM");
		tab_dias_sin_timbre.getColumna("horafinalmbio_cobmr").setLongitud(12);
		tab_dias_sin_timbre.getColumna("horafinalmbio_cobmr").setOrden(9);
		
		
		
		tab_dias_sin_timbre.getColumna("tiempoalm_cobmr").setNombreVisual("TEMP");
		tab_dias_sin_timbre.getColumna("tiempoalm_cobmr").setLongitud(5);
		tab_dias_sin_timbre.getColumna("tiempoalm_cobmr").setOrden(10);
		
		
		tab_dias_sin_timbre.getColumna("horafinhorario_cobmr").setNombreVisual("HOR.SAL");
		tab_dias_sin_timbre.getColumna("horafinhorario_cobmr").setLongitud(8);
		tab_dias_sin_timbre.getColumna("horafinhorario_cobmr").setOrden(11);
		
		
		tab_dias_sin_timbre.getColumna("horafinbiometrico_cobmr").setNombreVisual("TIM.SAL");
		tab_dias_sin_timbre.getColumna("horafinbiometrico_cobmr").setLongitud(12);
		tab_dias_sin_timbre.getColumna("horafinbiometrico_cobmr").setOrden(12);
				
		
		tab_dias_sin_timbre.getColumna("horafinband_cobmr").setNombreVisual("EST.SAL");
		tab_dias_sin_timbre.getColumna("horafinband_cobmr").setLongitud(15);
		tab_dias_sin_timbre.getColumna("horafinband_cobmr").setOrden(13);
		
		
		
		tab_dias_sin_timbre.setCampoOrden("fecha_evento_cobmr desc");
		tab_dias_sin_timbre.setCondicion("IDE_GTEMP=-1");
		tab_dias_sin_timbre.setLectura(true);
		tab_dias_sin_timbre.dibujar();
		PanelTabla pat_panel5 = new PanelTabla();
		pat_panel5.setMensajeWarn("AUSENTISMO");
		pat_panel5.setPanelTabla(tab_dias_sin_timbre);
		
	
		
		tab_tabla_vacacion.setId("tab_tabla_vacacion");
		tab_tabla_vacacion.setTabla("ASI_VACACION", "IDE_ASVAC", 1);
		tab_tabla_vacacion.getColumna("DIAS_PENDIENTES_ASVAC").setVisible(false);
		tab_tabla_vacacion.getColumna("DIAS_TOMADOS_ASVAC").setVisible(false);
		tab_tabla_vacacion.getColumna("NRO_DIAS_AJUSTE_ASVAC").setVisible(false);
		tab_tabla_vacacion.getColumna("ACTIVO_ASVAC").setCheck();
		tab_tabla_vacacion.getColumna("ACTIVO_ASVAC").setValorDefecto("TRUE");
		tab_tabla_vacacion.getColumna("ACTIVO_ASVAC").setNombreVisual("ACTIVO");
		tab_tabla_vacacion.getColumna("ACTIVO_ASVAC").setLectura(true);
		tab_tabla_vacacion.getColumna("IDE_ASVAC").setNombreVisual("CODIGO");
		tab_tabla_vacacion.getColumna("IDE_ASVAC").setLectura(true);
		tab_tabla_vacacion.getColumna("NRO_DIAS_AJUSTE_PERIODO_ASVAC").setVisible(false);
		//tab_tabla_vacacion.getColumna("NRO_DIAS_AJUSTE_PERIODO_ASVAC").setNombreVisual("DIAS DESCONTADOS POR ACUMULACI�N");
		tab_tabla_vacacion.getColumna("FECHA_INGRESO_ASVAC").setNombreVisual("FECHA INGRESO");
		tab_tabla_vacacion.getColumna("FECHA_INGRESO_ASVAC").setLectura(true);
		tab_tabla_vacacion.getColumna("FECHA_FINIQUITO_ASVAC").setNombreVisual("FECHA FINIQUITO");
		tab_tabla_vacacion.getColumna("FECHA_FINIQUITO_ASVAC").setLectura(true);
		
		tab_tabla_vacacion.getColumna("fecha_liquidacion_asvac").setNombreVisual("FECHA LIQUIDACION");
		tab_tabla_vacacion.getColumna("fecha_liquidacion_asvac").setVisible(false);
		tab_tabla_vacacion.getColumna("fecha_liquidacion_asvac").setLectura(true);
		
		
		tab_tabla_vacacion.getColumna("ide_geedp_liquidacion").setNombreVisual("IDE_GEEDP LIQUIDACION");
		tab_tabla_vacacion.getColumna("ide_geedp_liquidacion").setVisible(false);
		tab_tabla_vacacion.getColumna("ide_geedp_liquidacion").setLectura(true);
		

		tab_tabla_vacacion.getColumna("OBERVACION_ASVAC").setNombreVisual("OBSERVACION");
		tab_tabla_vacacion.getColumna("OBERVACION_ASVAC").setLectura(true);
		
		//tab_tabla_vacacion.getColumna("fecha_liquidacion_asvac").setNombreVisual("FECHA LIQUIDACI�N ");
		/*tab_tabla_vacacion.getColumna("ide_geedp_liquidacion").setCombo("SELECT EPAR.IDE_GEEDP,EPAR.IDE_GEEDP || ' || ' || " +
				"gaed.detalle_geaed || ' ' || gmed.detalle_gemed as ACCION, "
				+"AREA.DETALLE_GEARE, " 
				+"DEPA.DETALLE_GEDEP, " 
				+ "epar.fecha_geedp , " +
				"CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END "+
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "+
				"left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "+
				"left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "+
				"left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed ");
		tab_tabla_vacacion.getColumna("ide_geedp_liquidacion").setNombreVisual("ACCION INGRESO");*/
		tab_tabla_vacacion.getColumna("IDE_GTEMP").setVisible(false);
		tab_tabla_vacacion.agregarRelacion(tab_tabla_detalle_vacaccion);
		tab_tabla_vacacion.setCondicion("IDE_ASVAC=-1");
		tab_tabla_vacacion.setLectura(false);
		tab_tabla_vacacion.onSelect("actualizarMarcacionesSinTimbre");
		tab_tabla_vacacion.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("VACACION");
		pat_panel1.setPanelTabla(tab_tabla_vacacion);

		tab_tabla_detalle_vacaccion.setId("tab_tabla_detalle_vacaccion");
		tab_tabla_detalle_vacaccion.setTabla("ASI_DETALLE_VACACION", "IDE_ASDEV", 2);
		tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setCheck();
		tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setValorDefecto("TRUE");
		tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setNombreVisual("ACTIVO");
		tab_tabla_detalle_vacaccion.getColumna("ANULADO_ASDEV").setCheck();
		tab_tabla_detalle_vacaccion.getColumna("ANULADO_ASDEV").setNombreVisual("ANULADO");
		tab_tabla_detalle_vacaccion.getColumna("IDE_ASDEV").setNombreVisual("COD DETALLE PERMISO");
		tab_tabla_detalle_vacaccion.getColumna("IDE_ASVAC").setNombreVisual("COD VACACI�N");
		tab_tabla_detalle_vacaccion.getColumna("IDE_ASPVH").setNombreVisual("COD PERMISO");
		tab_tabla_detalle_vacaccion.getColumna("FECHA_NOVEDAD_ASDEV").setNombreVisual("FECHA NOVEDAD");
		tab_tabla_detalle_vacaccion.getColumna("DIA_SOLICITADO_ASDEV").setNombreVisual("DIAS TOMADOS HASTA 31 DE MAYO");
		tab_tabla_detalle_vacaccion.getColumna("DIA_ADICIONAL_ASDEV").setNombreVisual("DIAS PENDIENTES HASTA 31 DE MAYO");
		tab_tabla_detalle_vacaccion.getColumna("DIA_DESCONTADO_ASDEV").setNombreVisual("DIAS SOLICITADO");
		tab_tabla_detalle_vacaccion.getColumna("DIA_ACUMULADO_ASDEV").setNombreVisual("DIAS RECALCULADOS");
		tab_tabla_detalle_vacaccion.getColumna("OBSERVACION_ASDEV").setNombreVisual("OBSERVACI�N");
    	tab_tabla_detalle_vacaccion.getColumna("IDE_ASNOD").setVisible(false);
		tab_tabla_detalle_vacaccion.getColumna("IDE_ASESV").setVisible(false);
		tab_tabla_detalle_vacaccion.getColumna("ANULADO_ASDEV").setVisible(false);
		tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setVisible(false);
		tab_tabla_detalle_vacaccion.getColumna("FIN_SEMANA_ASDEV").setVisible(false);

		tab_tabla_detalle_vacaccion.setLectura(true);		
		//tab_tabla_detalle_vacaccion.setCondicion("IDE_ASDEV=-1");

		tab_tabla_detalle_vacaccion.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("DETALLE VACACI�N");
		pat_panel2.setPanelTabla(tab_tabla_detalle_vacaccion);
				
		ItemMenu itm_vacaciones=new ItemMenu();
		itm_vacaciones.setValue("Ver Ficha");	
		itm_vacaciones.setIcon("ui-icon-note");
		itm_vacaciones.setMetodo("verFichaVacacion");
		pat_panel2.getMenuTabla().getChildren().add(itm_vacaciones);
		
		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setSql("SELECT " +
				"(cast(PERIODO as integer) -1) ||' - '|| periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
				"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
				" FROM ( " +
				"SELECT IDE_ASVAC,cast (PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
				"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
				"FROM ( " +
				"SELECT IDE_ASVAC, " +
				"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
				"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
				"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
				"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
				"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE AND IDE_ASVAC=-1 " +
				")a GROUP BY a.periodo,a.IDE_ASVAC " +
				")b ORDER BY PERIODO DESC");
		tab_tabla3.getColumna("DIA_ACUMULADO").setNombreVisual("DIAS ACUMULADOS");
		tab_tabla3.getColumna("NRO_DIAS_ADICIONAL").setNombreVisual("NRO DIAS ADICIONAL");
		tab_tabla3.getColumna("DIA_DESCONTADO").setNombreVisual("DIAS DESCONTADOS");
		tab_tabla3.getColumna("DIA_SOLICITADO").setNombreVisual("DIAS SOLICITADOS");
		tab_tabla3.getColumna("NRO_TOTALES_VACACIONES").setNombreVisual("NRO TOTAL VACACIONES");
		tab_tabla3.getColumna("DIAS_PENDIENTES").setNombreVisual("DIAS PENDIENTES");		
		tab_tabla3.getColumna("DIA_ACUMULADO").alinearDerecha();
		tab_tabla3.getColumna("NRO_DIAS_ADICIONAL").alinearDerecha();
		tab_tabla3.getColumna("DIA_DESCONTADO").alinearDerecha();
		tab_tabla3.getColumna("DIA_SOLICITADO").alinearDerecha();
		tab_tabla3.getColumna("NRO_TOTALES_VACACIONES").alinearDerecha();
		tab_tabla3.getColumna("DIAS_PENDIENTES").alinearDerecha();
		tab_tabla3.setLectura(true);	
		tab_tabla3.dibujar();
		con_ver_vacaciones.getGri_cuerpo().setFooter(tab_tabla3);
		   		
		
		
		
		tab_tabla_resumen_vacacion_empleado.setId("tab_tabla_resumen_vacacion_empleado");
		tab_tabla_resumen_vacacion_empleado.setSql("select IDE_ASVAC,'' as DIASACUMULADOS,'' as NROFINESSEMANA, "
				+ "'' as DIASDESCONTADOS, '' as DESCUENTOFINESDESEMANA, '' as TOTALDIASPENDIENTES,  "
				+ "'' as NROFINESSEMANAPENDIENTES  from asi_vacacion where ide_gtemp=-1");
		tab_tabla_resumen_vacacion_empleado.setCampoPrimaria("IDE_ASVAC");

		tab_tabla_resumen_vacacion_empleado.getColumna("DIASACUMULADOS").setNombreVisual("DIAS ACUMULADOS");
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASDESCONTADOS").setNombreVisual("DIAS DESCONTADOS");
		tab_tabla_resumen_vacacion_empleado.getColumna("TOTALDIASPENDIENTES").setNombreVisual("NRO TOTAL VACACIONES");
		tab_tabla_resumen_vacacion_empleado.getColumna("NROFINESSEMANA").setNombreVisual("NRO ACUMULADOS FINES DE SEMANA");
		tab_tabla_resumen_vacacion_empleado.getColumna("DESCUENTOFINESDESEMANA").setNombreVisual("NRO FINES DE SEMANA DESCONTADOS");
		tab_tabla_resumen_vacacion_empleado.getColumna("NROFINESSEMANAPENDIENTES").setNombreVisual("NRO TOTAL DE FINES DE SEMANA");
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASACUMULADOS").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASDESCONTADOS").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("TOTALDIASPENDIENTES").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("NROFINESSEMANA").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("DESCUENTOFINESDESEMANA").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("NROFINESSEMANAPENDIENTES").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.setLectura(true);	
	//	tab_tabla_resumen_vacacion_empleado.dibujar();
		tab_tabla_resumen_vacacion_empleado.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setMensajeWarn("REPORTE DETALLE X EMPLEADO VACACI�N");
		pat_panel3.setPanelTabla(tab_tabla_resumen_vacacion_empleado);
		
		Division div_division2 = new Division();
	//	div_division1.dividir2(pat_panel2, pat_panel3, "50%", "V");

		
		
		Division div_division = new Division();	

		div_division2.dividir2(pat_panel2, pat_panel5, "50%", "V");
		div_division.dividir2(pat_panel1, div_division2, "35%", "H");

		
		agregarComponente(div_division);
		bar_botones.agregarComponente(new Etiqueta("Empleado"));
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.agregarBoton(bot_ver_vacaciones);
		bar_botones.agregarBoton(bot_auxiliar_vacaciones);
		agregarComponente(con_ver_vacaciones);
				
		
		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");	
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO ", "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado);
		
		con_ver_ficha_vacacion.setId("con_ver_ficha_vacacion");
		con_ver_ficha_vacacion.setTitle("FICHA DE VACACIONES");
		con_ver_ficha_vacacion.setWidth("70%");
		con_ver_ficha_vacacion.setHeight("65%");		
		con_ver_ficha_vacacion.setConsulta("SELECT " +
				"a.ide_aspvh, " +
				"h.jefe_inmediato, " +
				"j.jefe_talento_humano, " +
				"l.gerente_area, " +
				"b.detalle_asmot, " +
				"a.fecha_solicitud_aspvh,a.fecha_desde_aspvh,a.fecha_hasta_aspvh,a.detalle_aspvh,a.nro_dias_aspvh, " +
				"d.detalle_gemes,c.detalle_geani,a.nro_documento_aspvh,a.aprobado_aspvh,a.activo_aspvh " +
				"FROM ASI_PERMISOS_VACACION_HEXT a " +
				"LEFT JOIN( " +
				"SELECT ide_asmot,detalle_asmot FROM asi_motivo " +
				")b ON a.ide_asmot=b.ide_asmot " +
				"LEFT JOIN( " +
				"SELECT ide_geani,detalle_geani FROM gen_anio " +
				")c ON c.ide_geani=a.ide_geani " +
				"LEFT JOIN( " +
				"SELECT ide_gemes,detalle_gemes FROM gen_mes " +
				")d ON d.ide_gemes=a.ide_gemes " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")g ON g.ide_geedp=a.gen_ide_geedp " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_inmediato FROM gth_empleado " +
				")h ON h.ide_gtemp=g.ide_gtemp " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")i ON i.ide_geedp=a.gen_ide_geedp2 " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_talento_humano FROM gth_empleado " +
				")j ON j.ide_gtemp=i.ide_gtemp " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")k ON k.ide_geedp=a.gen_ide_geedp3 " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as gerente_area FROM gth_empleado " +
				")l ON l.ide_gtemp=k.ide_gtemp " +
				"WHERE a.ide_aspvh in(SELECT ide_aspvh FROM asi_detalle_vacacion where ide_asdev=-1)", "ide_aspvh");
		
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("ide_aspvh").setNombreVisual("CODIGO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("jefe_inmediato").setNombreVisual("JEFE INMEDIATO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("jefe_talento_humano").setNombreVisual("JEFE DE TALENTO HUMANO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("gerente_area").setNombreVisual("GERENTE DE AREA");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_asmot").setNombreVisual("MOTIVO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_solicitud_aspvh").setNombreVisual("FECHA SOLICITUD");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_desde_aspvh").setNombreVisual("FECHA DESDE");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_hasta_aspvh").setNombreVisual("FECHA HASTA");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_aspvh").setNombreVisual("DETALLE");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("nro_dias_aspvh").setNombreVisual("NRO DIAS");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_gemes").setNombreVisual("MES");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_geani").setNombreVisual("A�O");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("nro_documento_aspvh").setNombreVisual("NRO DOCUMENTO");		
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("aprobado_aspvh").setNombreVisual("APROBADO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("activo_aspvh").setNombreVisual("ACTIVO");			
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("aprobado_aspvh").setCheck();
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("activo_aspvh").setCheck();
		con_ver_ficha_vacacion.getBot_aceptar().setRendered(false);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().setTipoFormulario(true);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getGrid().setColumns(4);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().setMostrarNumeroRegistros(false);
		agregarComponente(con_ver_ficha_vacacion);
		
		
		tab_asi_vacacion_resumen_empleado.setId("tab_asi_vacacion_resumen_empleado");
		tab_asi_vacacion_resumen_empleado.setTabla("asi_vacacion_resumen_empleado", "IDE_ASVRE", 3);
		tab_asi_vacacion_resumen_empleado.setCondicion("IDE_ASVRE=-1");
		tab_asi_vacacion_resumen_empleado.dibujar();
		
		/*sec_rango_fechas.setId("sec_rango_fechas");
		sec_rango_fechas.setTitle("SELECCION DE RANGO DE FECHAS");
		sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRangoFechas");
		sec_rango_fechas.setDynamic(false);
		agregarComponente(sec_rango_fechas)*/
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		
		//com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 150px; margin: 0 0 -8px 0;");
		
       	Grid gri_anio = new Grid();
       	gri_anio.setColumns(2);
       	gri_anio.getChildren().add(new Etiqueta("Seleccione el A�o: "));
       	gri_anio.getChildren().add(com_anio);
       	
		seleciona_anio.setId("seleciona_anio");
		seleciona_anio.setTitle("Seleccione el a�o para el reporte");
		seleciona_anio.setWidth("20%");
		seleciona_anio.setHeight("20%");
		seleciona_anio.setDialogo(gri_anio);
		seleciona_anio.getBot_aceptar().setMetodo("aceptarReporteAuxVaca");
		agregarComponente(seleciona_anio);
				
		//agregarComponente(com_anio);
		/*bar_botones.agregarComponente(new Etiqueta("A�O:"));
		bar_botones.agregarComponente(com_anio);*/
	}

	
	public void filtrarEmpleado(SelectEvent evt){		
			aut_empleado.onSelect(evt);
		
		
			tab_tabla_vacacion.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
			tab_tabla_vacacion.ejecutarSql();
			tab_tabla_detalle_vacaccion.ejecutarValorForanea(tab_tabla_vacacion.getValorSeleccionado());
			tab_tabla_resumen_vacacion_empleado.setSql("select ide_asvac,'' as diasAcumulados,'' as NroFinesSemana, "
				+ "'' as diasDescontados, '' as DescuentoFinesDeSemana, '' as TotalDiasPendientes,  "
				+ "'' as NroFinesSemanaPendientes   from asi_vacacion where ide_gtemp="+aut_empleado.getValor()+" and activo_asvac=true");
			tab_tabla_resumen_vacacion_empleado.ejecutarSql();
			
			String fecha_inico="",fecha_fin="";
			
			if (tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC")== null || tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC").isEmpty() || tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC").equals("")) {
				fecha_fin=utilitario.getFechaActual();
				fecha_inico=tab_tabla_vacacion.getValor("FECHA_INGRESO_ASVAC");

			}else{
				fecha_fin=tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC");
				fecha_inico=tab_tabla_vacacion.getValor("FECHA_INGRESO_ASVAC");
			}
			tab_dias_sin_timbre.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" AND  "
			+ "fecha_evento_cobmr BETWEEN '"+fecha_inico+"' AND '"+fecha_fin+"' "
			+ "and (horainicioband_cobmr like '%SIN TIMBRE%' or horafinband_cobmr like '%SIN TIMBRE%') ");
			tab_dias_sin_timbre.ejecutarSql();
			utilitario.addUpdate("tab_tabla_resumen_vacacion_empleado,tab_dias_sin_timbre");
		
	}
	
	public void limpiar() {
		tab_tabla_vacacion.limpiar();
		tab_tabla_detalle_vacaccion.limpiar();
		aut_empleado.limpiar();
		tab_dias_sin_timbre.limpiar();
		utilitario.addUpdate("aut_empleado,dia_aplica_vacion");// limpia y refresca el autocompletar
	} 
	
	public void verVacaciones(){
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_tabla_vacacion.getTotalFilas()>0){		
				/*	con_ver_vacaciones.getTab_consulta_dialogo().setSql("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
							"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
							"FROM ( " +
							"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
							"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
							"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
							"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
							"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
							") a where IDE_ASVAC="+tab_tabla_vacacion.getValorSeleccionado());
					tab_tabla3.setSql("SELECT " +
							"periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
							"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
							" FROM ( " +
							"SELECT IDE_ASVAC,cast (PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
							"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
							"FROM ( " +
							"SELECT IDE_ASVAC, " +
							"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
							"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
							"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
							"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
							"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
							"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE AND IDE_ASVAC="+tab_tabla_vacacion.getValorSeleccionado()+
							")a GROUP BY a.periodo,a.IDE_ASVAC " +
							")b ORDER BY PERIODO DESC");

					tab_tabla3.ejecutarSql();		*/
					//Obtengo los parametros de mi reporte
					try {
					getDatosEmpleado(aut_empleado.getValor(),false,tab_tabla_vacacion.getValorSeleccionado());

					} catch (Exception e) {
System.out.println("error verVacaciones()");					}
					//dia_aplica_vacion.Limpiar();
					//utilitario.addUpdate("dia_aplica_vacion");
       		}else{
				utilitario.agregarMensajeInfo("No se puede visualizar los registros", "El Empleado no posee registros ");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar los registros", "Debe seleccionar un Empleado");
		}		
	}
	
	/**
	 * Auxiliar de Vacaciones.
	 */
	
	public void generarReporteXls() {
		String nombre_archivo="auxiliar_de_vacaciones.xls";
		try {
			jxl.write.Label lab2;
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			Workbook archivo_xls2 = Workbook.getWorkbook(new File(utilitario.getPropiedad("rutaUpload")+"\\" + nombre_archivo)); 
			result = new File(extContext.getRealPath("/" + nombre_archivo)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2); 

			int columna=0;
			int anioInicio = 2015;
			int anioFin = utilitario.getAnio(str_fecha_actual);
			int anioSiguiente = anioInicio;
			int incrementoAnio=1;
			int nro_anio_transcurridos = (anioFin - anioInicio)+1;
			String nombreHoja="", periodo_reporte;
			int ide_gtemp=0;
			
			Map<Integer, List<Map<String,String>>> mPeriodosAvre = new LinkedHashMap<Integer, List<Map<String,String>>>();
			Map<String, String> ItemsPeriodosAvre = new ListHashMap<String, String>();
			Map<String, String> AddItemsPeriodosAvre = new LinkedHashMap<String, String>();
			
			TablaGenerica tabEmpleados = ser_empleado.getEmpleadosAnio(CConversion.CStr(anioInicio));
			updateDiasVacacionesEmpleados(tabEmpleados);
			
			do {
				int contador=0;
				columna=0;
				fila =0;
				ide_gtemp=0;
				nombreHoja= CConversion.CStr(anioSiguiente);
				hojaPlantilla_xls = archivo_xls.getSheet(incrementoAnio-1);
				periodo_reporte = (incrementoAnio == nro_anio_transcurridos) ? nombreHoja+"-01-01 hasta "+str_fecha_actual : nombreHoja+"-01-01 hasta "+nombreHoja+"-12-31";
				
				for(int indice=0;indice<tabEmpleados.getTotalFilas();indice++){
					ArrayList<String> listIdeVac = ser_empleado.getGroupIdVacacionEmpleado(tabEmpleados.getValor(indice,"IDE_GTEMP"));
					if (listIdeVac.size() > 0) {
						TablaGenerica tabResumenVacacion = ser_empleado.getResumenVacacionEmpleado(explodeArray(listIdeVac), CConversion.CStr(anioSiguiente));
						
						/*for (int rv = 0; rv < tabResumenVacacion.getTotalFilas(); rv ++) {
							if (mPeriodosAvre.containsKey(CConversion.CInt(tabEmpleados.getValor(indice, "ide_gtemp")))) {
								AddItemsPeriodosAvre.put("anio", CConversion.CStr(anioSiguiente));
								AddItemsPeriodosAvre.put("ide_periodo_asvre", tabResumenVacacion.getValor(rv, "ide_periodo_asvre"));
								AddItemsPeriodosAvre.put("nro_dias_tomados_asvre", tabResumenVacacion.getValor(rv, "nro_dias_tomados_asvre"));
								
								mPeriodosAvre.get(CConversion.CInt(tabEmpleados.getValor(indice, "ide_gtemp"))).add(AddItemsPeriodosAvre);
							}else {
								ItemsPeriodosAvre.put("anio", CConversion.CStr(anioSiguiente));
								ItemsPeriodosAvre.put("ide_periodo_asvre", tabResumenVacacion.getValor(rv, "ide_periodo_asvre"));
								ItemsPeriodosAvre.put("nro_dias_tomados_asvre", tabResumenVacacion.getValor(rv, "nro_dias_tomados_asvre"));
								
								List<Map<String, String>> items = new ArrayList<Map<String,String>>();
								items.add(ItemsPeriodosAvre);
								mPeriodosAvre.put(CConversion.CInt(tabEmpleados.getValor(indice, "ide_gtemp")), items);
							}
						}*/
						
						for (int rvac = 0; rvac < tabResumenVacacion.getTotalFilas(); rvac ++) {
							if(ide_gtemp != CConversion.CInt(tabEmpleados.getValor(indice, "ide_gtemp"))) {
								if(ide_gtemp > 0)fila++;
								
								construyeAuxVacacionesCab(columna,fila, contador,
										tabEmpleados.getValor(indice,"NOMBRES_APELLIDOS"),
										tabEmpleados.getValor(indice,"DOCUMENTO_IDENTIDAD_GTEMP"),
										tabEmpleados.getValor(indice,"FECHA_INGRESO_GRUPO_GTEMP"),
										CConversion.CStr(anioSiguiente));
								
								ide_gtemp = CConversion.CInt(tabEmpleados.getValor(indice, "ide_gtemp"));
							}
							
							//construyeAuxVacaciones(tabResumenVacacion, rvac, anioSiguiente);
							
							detalleAuxVacaciones(tabResumenVacacion, rvac, anioSiguiente, mPeriodosAvre);
							fila ++;
						}
						contruyeExcel(anioSiguiente);
						contador++;
					}
				}
				
				lab2 = new jxl.write.Label(1, 2, ""+nombreHoja, hojaPlantilla_xls.getCell(1, 2).getCellFormat());
				hojaPlantilla_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(1, 3, ""+periodo_reporte, hojaPlantilla_xls.getCell(1, 2).getCellFormat());
				hojaPlantilla_xls.addCell(lab2);
				
				hojaPlantilla_xls.setName(nombreHoja);
				
				//System.out.println("fechaSiguiente :" +fechaSiguiente+ " - str_fecha2: "+str_fecha2);
				
				/*if(fechaSiguiente.equals(str_fecha2))
					break;
				*/
				
				anioSiguiente = anioInicio + incrementoAnio;
				archivo_xls.importSheet("Copia", incrementoAnio, archivo_xls2.getSheet(0));
				incrementoAnio++;
				mPeriodosAvre.clear();
				
			} while (incrementoAnio <= nro_anio_transcurridos);
			
			archivo_xls.write(); 
			archivo_xls.close(); 
			
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nombre_archivo);
						
		} catch (Exception e) {
			System.out.println("Excepcion en funci�n generarReporteXls. ");
			e.printStackTrace();
		}
	}
	
	private String explodeArray(ArrayList<String> array) {
		StringBuilder strList = new StringBuilder();
		String SEPARADOR = ",";
  		for (String a : array) {
			strList.append(a);
			strList.append(SEPARADOR);
		}
  		String b = strList.toString();
  		b = b.substring(0, b.length() - SEPARADOR.length());
  		
  		return b;
	}
	
	public void pedirParametros(){
		com_anio.setValue(null);
		seleciona_anio.dibujar();

	}
	
	String ide_gepro="";
	public void aceptarReporteAuxVaca(){
		if(com_anio.getValue() != null) {
			String anio_fiscal =  com_anio.getValue().toString();
			auxiliarVacaciones(anio_fiscal);
			seleciona_anio.cerrar();
		}else {
			utilitario.agregarMensaje("A�o inv�lido", "Selecione el A�o");
		}
	}
	
	String fecha_ingreso_actual = "", fecha_ingreso_nuevo="", fecha_finiquito_actual = "", fecha_finiquito_nuevo = "";
	Map<String, List<String>> m_vacaciones = new TreeMap<String, List<String>>();
	Map<String , List<String>> mp = new TreeMap<String, List<String>>();
	
	public void auxiliarVacaciones(String anioFiscal) {
		m_vacaciones = new TreeMap<String, List<String>>();
		
		try {
			jxl.write.Label lab2;
			
			TablaGenerica tabAnio = utilitario.consultar("SELECT ide_geani, detalle_geani as anio FROM gen_anio where ide_geani="+anioFiscal+" limit 1;");
			String periodo_anio = tabAnio.getValor("anio");
			
			TablaGenerica tabResumeneVacaciones = utilitario.consultar("WITH resumen_vacaciones AS ("
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, emp.FECHA_INGRESO_GRUPO_GTEMP, "
					+ "VRE.IDE_ASVRE, VRE.IDE_ASVAC, split_part(VRE.PERIODO_ASVRE, ' - ', 1) as fecha_ingreso, "
					+ "split_part(VRE.PERIODO_ASVRE, ' - ', 2) as fecha_finiquito, VRE.NRO_DIAS_VACACION_ASVRE, VRE.NRO_DIAS_TOMADOS_ASVRE, VRE.NRO_DIAS_PENDIENTES_ASVRE, "
					+ "VRE.IDE_PERIODO_ASVRE, VRE.ACTIVO_LIQUIDACION, VRE.VALOR_PAGAR_ASVRE, VRE.VALOR_DIA_ASVRE, VRE.IDE_GEEDP_ASVAC, pgc.titulo_cargo_gepgc, EDP.RMU_GEEDP, "
					+ "tem.ide_gttem, tem.detalle_gttem, cast(extract(year from to_date(split_part(VRE.PERIODO_ASVRE, ' - ', 1), 'YYYY-MM-DD')) as int ) as anio_ingreso, "
					+ "cast(extract(year from to_date(split_part(VRE.PERIODO_ASVRE, ' - ', 1), 'YYYY-MM-DD')) as int ) as anio_finiquito, "
					+ "cast(extract(year from to_date(emp.FECHA_INGRESO_GRUPO_GTEMP::TEXT, 'YYYY-MM-DD')) as int ) as anio_ingreso_gtemp, "
					+ "ded.activo_geded, vac.nro_dias_ajuste_asvac "
					+ "FROM ASI_VACACION AS VAC "
					+ "JOIN GTH_EMPLEADO AS EMP ON EMP.IDE_GTEMP = VAC.IDE_GTEMP "
					+ "JOIN ASI_VACACION_RESUMEN_EMPLEADO AS VRE ON VRE.IDE_ASVAC = VAC.IDE_ASVAC "
					+ "JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR AS EDP ON EDP.IDE_GEEDP = VRE.IDE_GEEDP_ASVAC "
					+ "INNER JOIN gen_partida_grupo_cargo as pgc on pgc.ide_gepgc = edp.ide_gepgc "
					+ "JOIN gth_tipo_empleado as tem on tem.ide_gttem = edp.ide_gttem "
					+ "JOIN GEN_DETALLE_EMPLEADO_DEPARTAME as ded on edp.ide_geded = ded.ide_geded "
					+ "ORDER BY VRE.IDE_ASVRE asc "
					+ ") select * from resumen_vacaciones where  (extract(year from to_date(fecha_ingreso, 'YYYY-MM-DD')) = (SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+anioFiscal+") "
					+ "or extract(year from to_date(fecha_finiquito, 'YYYY-MM-DD')) >= (SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+anioFiscal+")) "
					+ "and extract(year from to_date(anio_ingreso_gtemp::text, 'YYYY-MM-DD')) <= '"+periodo_anio+"'"
					+ "order by NOMBRES_APELLIDOS, fecha_ingreso asc;");
			
			int gtemp = 0;
			fila=0;
			String nombre_archivo="auxiliar_de_vacaciones.xls";
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			Workbook archivo_xls2 = Workbook.getWorkbook(new File(utilitario.getPropiedad("rutaUpload")+"\\" + nombre_archivo)); 
			result = new File(extContext.getRealPath("/" + nombre_archivo)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2);
			hojaPlantilla_xls = archivo_xls.getSheet(0);
			
			if (tabResumeneVacaciones.getTotalFilas() > 0) {
				int contador = 0;
				for (int rv=0; rv<tabResumeneVacaciones.getTotalFilas(); rv++) {
					if (CConversion.CInt(tabResumeneVacaciones.getValor(rv, "anio_ingreso")) <= CConversion.CInt(periodo_anio) /*&& 
							CConversion.CInt(tabResumeneVacaciones.getValor(rv, "anio_finiquito")) == CConversion.CInt(periodo_anio)*/ ) {
						if (gtemp != Integer.parseInt(tabResumeneVacaciones.getValor(rv, "IDE_GTEMP"))) {
							if(gtemp > 0)fila++;
							
							/*construyeAuxVacacionesCab(columna,fila, contador, tabResumeneVacaciones.getValor(rv,"NOMBRES_APELLIDOS"), tabResumeneVacaciones.getValor(rv,"DOCUMENTO_IDENTIDAD_GTEMP"), 
									tabResumeneVacaciones.getValor(rv, "FECHA_INGRESO_GRUPO_GTEMP"));
							*/
							gtemp = Integer.parseInt(tabResumeneVacaciones.getValor(rv, "IDE_GTEMP"));
							contador ++;
							
							
							TablaGenerica tabVacaciones = utilitario.consultar("select * from asi_vacacion where ide_gtemp ="+gtemp+" and activo_asvac=true order by ide_asvac asc");
							for (int v =0; v<tabVacaciones.getTotalFilas(); v++) {
								updateDiasVacacionesEmpleados(tabVacaciones);
								
								
							}
							fecha_ingreso_actual = "";
							fecha_finiquito_actual = "";
						}
						construyeAuxVacaciones(tabResumeneVacaciones, rv, CConversion.CInt(periodo_anio));
					}
				}
				lab2 = new jxl.write.Label(1, 2, periodo_anio, hojaPlantilla_xls.getCell(1, 2).getCellFormat());
				hojaPlantilla_xls.addCell(lab2);
				
				String periodo_reporte = periodo_anio+"-01-01 hasta "+periodo_anio+"-12-31";
				
				lab2 = new jxl.write.Label(1, 3, ""+periodo_reporte, hojaPlantilla_xls.getCell(1, 2).getCellFormat());
				hojaPlantilla_xls.addCell(lab2);
				
				//hojaPlantilla_xls.setName(nombreHoja);
				
				archivo_xls.write(); 
				archivo_xls.close(); 
				
				FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nombre_archivo);
			}else {
				utilitario.agregarMensaje("Aviso", "No existen registros para mostrar");
			}
			
		} catch (Exception e) {
			utilitario.agregarMensajeError("Auxiliar de Vacaciones", e.getMessage());
		}
		
	}
	
	private void construyeAuxVacacionesCab(int columna, int indice, int cont, String empleado, String cedula, String fecha_ingreso, String Anio) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		
		lab2 = new jxl.write.Label(0, (7+indice),(cont+1)+"", hojaPlantilla_xls.getCell(0, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(1, (7+indice),cedula, hojaPlantilla_xls.getCell(1, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(2, (7+indice),empleado, hojaPlantilla_xls.getCell(2, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(3, (7+indice),fecha_ingreso, hojaPlantilla_xls.getCell(3, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(4, (7+indice),Anio, hojaPlantilla_xls.getCell(4, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
	}
	
	Map<Integer, List<Map<String, String>>> m_parametros = new  LinkedHashMap<Integer, List<Map<String,String>>>();
	Map<Integer, List<Map<String, String>>> m_PeriodosAsvre = new  LinkedHashMap<Integer, List<Map<String,String>>>();
		
	private void detalleAuxVacaciones(TablaGenerica tabResumenVacacionEmpleado, int indice, int anio_reporte, Map<Integer, List<Map<String,String>>> periodosAvre)  throws RowsExceededException, WriteException {
		String IDE_GTEMP = tabResumenVacacionEmpleado.getValor(indice, "IDE_GTEMP");
		String IDE_ASVAC = tabResumenVacacionEmpleado.getValor(indice, "IDE_ASVAC");
		String IDE_PERIODO_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "ide_periodo_asvre");
		String IDE_GTTEM = tabResumenVacacionEmpleado.getValor(indice, "ide_gttem");
		String REGIMEN_LABORAL = tabResumenVacacionEmpleado.getValor(indice, "detalle_gttem");
		String RMU = tabResumenVacacionEmpleado.getValor(indice, "rmu_geedp");
		String CARGO = tabResumenVacacionEmpleado.getValor(indice, "titulo_cargo_gepgc");
		String FECHA_INGRESO_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "fecha_ingreso_asvre");
		String FECHA_FINIQUITO_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "fecha_finiquito_asvre");
		String ANIO_FINIQUITO_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "anio_finiquito_periodo_asvre");		
		String NRO_DIAS_VACACION_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "nro_dias_vacacion_asvre");
		String NRO_DIAS_TOMADOS_ASVRE = tabResumenVacacionEmpleado.getValor(indice, "nro_dias_tomados_asvre");
		boolean ESTADO_ASVAC = CConversion.CBol(tabResumenVacacionEmpleado.getValor(indice, "activo_asvac"));
		boolean ESTADO_ASVRE = CConversion.CBol(tabResumenVacacionEmpleado.getValor(indice, "activo_asvre"));

		double nro_vacaciones_actual = 0d, nro_dias_tomados_actual = 0, diasTrabajados = 0d, nro_dias_periodo_anterioro =0d, nro_dias_acumulados = 0d, nro_dias_descontados_periodo_anterior = 0d, total_dias_descontados = 0d, 
				total_dias_descomtados_periodo_actual = 0d, vacaciones_por_tomar = 0d;
		int fin_semana = 0, filaRow = 7+fila, filaRowOld=0;
			
		Map<String, String> mNewItems = new LinkedHashMap<String, String>();
		Map<String, String> mAddItems = new LinkedHashMap<String, String>();
			
		Map<String, String> mPeriodos = getPeriodoAnio(FECHA_INGRESO_ASVRE, FECHA_FINIQUITO_ASVRE, anio_reporte);
		String periodo_reporte = mPeriodos.get("fecha_ingreso")+" - "+mPeriodos.get("fecha_finiquito");
		diasTrabajados = getNumeroDiasEmpleado(IDE_GTEMP, IDE_ASVAC, mPeriodos.get("fecha_ingreso"), mPeriodos.get("fecha_finiquito"));
    		 
		if (CConversion.CInt(IDE_GTTEM) == 1) {
			nro_vacaciones_actual = (diasTrabajados*15)/360;
		}else if(CConversion.CInt(IDE_GTTEM) == 2) {
			nro_vacaciones_actual = (diasTrabajados*30)/360;
		}		
     		
     		
		if(m_parametros.containsKey(CConversion.CInt(IDE_GTEMP))){
			for (int iterator = 0; iterator < m_parametros.get(CConversion.CInt(IDE_GTEMP)).size(); iterator ++) {
				if(CConversion.CInt(m_parametros.get(CConversion.CInt(IDE_GTEMP)).get(iterator).get("anio_reporte")) == (anio_reporte-1)) {
					nro_dias_acumulados = CConversion.CDbl(m_parametros.get(CConversion.CInt(IDE_GTEMP)).get(iterator).get("nro_dias_acumulados"));
            		total_dias_descontados = CConversion.CDbl(m_parametros.get(CConversion.CInt(IDE_GTEMP)).get(iterator).get("total_dias_descontados"));				
				}else {
					nro_dias_acumulados = 0d;
            		total_dias_descontados = 0d;
				}
     		}
		}
				
		nro_dias_tomados_actual = CConversion.CDbl(ser_empleado.getTotalDiasVacacionPorFechas(IDE_GTEMP, mPeriodos.get("fecha_ingreso"), mPeriodos.get("fecha_finiquito")));
		if ( FECHA_INGRESO_ASVRE.equals(mPeriodos.get("fecha_ingreso")) && FECHA_FINIQUITO_ASVRE.equals(mPeriodos.get("fecha_finiquito")) ) {
			nro_dias_tomados_actual = CConversion.CDbl(NRO_DIAS_TOMADOS_ASVRE);
		}else {
			if (CConversion.CInt(tabResumenVacacionEmpleado.getTotalFilas()) > 1) {
				nro_dias_tomados_actual = CConversion.CDbl(NRO_DIAS_TOMADOS_ASVRE);
				}
				}
				
				
		nro_dias_periodo_anterioro = nro_dias_acumulados;
		nro_dias_acumulados = nro_dias_periodo_anterioro+nro_vacaciones_actual;
		nro_dias_descontados_periodo_anterior = total_dias_descontados;
		total_dias_descontados = nro_dias_descontados_periodo_anterior + CConversion.CDbl(nro_dias_tomados_actual);
		fin_semana = getNroDiasFinSemana(CConversion.CDbl(total_dias_descontados));
		total_dias_descomtados_periodo_actual = total_dias_descontados + fin_semana;
		vacaciones_por_tomar = nro_dias_acumulados-total_dias_descomtados_periodo_actual;
				
		if (m_parametros.containsKey(CConversion.CInt(IDE_GTEMP))) {
			mAddItems.put("ide_gtemp", IDE_GTEMP);
			mAddItems.put("anio_reporte", CConversion.CStr(anio_reporte));
			mAddItems.put("periodo", periodo_reporte);
			mAddItems.put("cargo", CARGO);
			mAddItems.put("rmu", RMU);
			mAddItems.put("regimen_laboral", REGIMEN_LABORAL);
			mAddItems.put("nro_vacacion_periodo_anterior",  utilitario.getFormatoNumero(nro_dias_periodo_anterioro, 2));
			mAddItems.put("nro_vacacion_periodo_actual",  utilitario.getFormatoNumero(nro_vacaciones_actual, 2));
			mAddItems.put("nro_dias_acumulados",  utilitario.getFormatoNumero(nro_dias_acumulados, 2));
			mAddItems.put("nro_dias_tomados_periodo_anterior", utilitario.getFormatoNumero(nro_dias_descontados_periodo_anterior, 2));
			mAddItems.put("nro_dias_tomados_periodo_actual", utilitario.getFormatoNumero(nro_dias_tomados_actual, 2));
			mAddItems.put("total_dias_descontados", utilitario.getFormatoNumero(total_dias_descontados, 2));
			mAddItems.put("dias_fin_semana", CConversion.CStr(fin_semana));
			mAddItems.put("total_dias_descomtados_periodo_actual", utilitario.getFormatoNumero(total_dias_descomtados_periodo_actual,2));
			mAddItems.put("vacaciones_por_tomar", utilitario.getFormatoNumero(vacaciones_por_tomar,2));
			mAddItems.put("filaRow", filaRowOld > 0 ? CConversion.CStr(filaRowOld):CConversion.CStr(filaRow));
			mAddItems.put("estado_asvac", CConversion.CStr(ESTADO_ASVAC));
			mAddItems.put("ide_periodo_asvre", IDE_PERIODO_ASVRE);
				
			m_parametros.get(CConversion.CInt(IDE_GTEMP)).add(mAddItems);
			}else {
			mNewItems.put("ide_gtemp", IDE_GTEMP);
			mNewItems.put("anio_reporte", CConversion.CStr(anio_reporte));
			mNewItems.put("periodo", periodo_reporte);
			mNewItems.put("cargo", CARGO);
			mNewItems.put("rmu", RMU);
			mNewItems.put("regimen_laboral", REGIMEN_LABORAL);
			mNewItems.put("nro_vacacion_periodo_anterior",  utilitario.getFormatoNumero(nro_dias_periodo_anterioro, 2));
			mNewItems.put("nro_vacacion_periodo_actual",  utilitario.getFormatoNumero(nro_vacaciones_actual, 2));
			mNewItems.put("nro_dias_acumulados",  utilitario.getFormatoNumero(nro_dias_acumulados, 2));
			mNewItems.put("nro_dias_tomados_periodo_anterior", utilitario.getFormatoNumero(nro_dias_descontados_periodo_anterior, 2));
			mNewItems.put("nro_dias_tomados_periodo_actual", utilitario.getFormatoNumero(nro_dias_tomados_actual, 2));
			mNewItems.put("total_dias_descontados", utilitario.getFormatoNumero(total_dias_descontados, 2));
			mNewItems.put("dias_fin_semana", CConversion.CStr(fin_semana));
			mNewItems.put("total_dias_descomtados_periodo_actual", utilitario.getFormatoNumero(total_dias_descomtados_periodo_actual,2));
			mNewItems.put("vacaciones_por_tomar", utilitario.getFormatoNumero(vacaciones_por_tomar, 2));
			mNewItems.put("filaRow", CConversion.CStr(filaRow));
			mNewItems.put("estado_asvac", CConversion.CStr(ESTADO_ASVAC));
			mNewItems.put("ide_periodo_asvre", IDE_PERIODO_ASVRE);
					
			List<Map<String, String>> items = new ArrayList<Map<String,String>>();
			items.add(mNewItems);
			m_parametros.put(CConversion.CInt(IDE_GTEMP), items);
			}
		}
		
	private void writeLogFile(String data) {
		BufferedWriter bw = null;
	    FileWriter fw = null;
		
	    try {
	        File file = new File("C:\\borrar.txt");
	        // Si el archivo no existe, se crea!
	        if (!file.exists()) {
	            file.createNewFile();
		}
	        // flag true, indica adjuntar informaci�n al archivo.
	        fw = new FileWriter(file.getAbsoluteFile(), true);
	        bw = new BufferedWriter(fw);
	        bw.write(data);
	        //System.out.println("informaci�n agregada!");
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	                        //Cierra instancias de FileWriter y BufferedWriter
	            if (bw != null)
	                bw.close();
	            if (fw != null)
	                fw.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	private void contruyeExcel(int anio_reporte)  throws RowsExceededException, WriteException {
		jxl.write.Label lab2;
		Map<Integer, Map<String, String>> m_ajuste = new LinkedHashMap<Integer, Map<String, String>>();
		Map<String, String> items_ajuste = new ListHashMap<String, String>();
		
		for (Map.Entry<Integer, List<Map<String,String>>> entry : m_parametros.entrySet()) {
            Integer key = entry.getKey();
        	List<Map<String,String>> values = entry.getValue();
		
            for(Map<String, String> listItem : values){
            	if (anio_reporte == CConversion.CInt(listItem.get("anio_reporte")) && key == CConversion.CInt(listItem.get("ide_gtemp"))) {
            		int filaRow = CConversion.CInt(listItem.get("filaRow"));
            		String nro_dias_tomados_periodo_actual = listItem.get("nro_dias_tomados_periodo_actual");
            		String total_dias_descontados = listItem.get("total_dias_descontados");
            		String vacaciones_por_tomar = listItem.get("vacaciones_por_tomar");
            		
                	lab2 = new jxl.write.Label(5, filaRow, ""+listItem.get("periodo"), hojaPlantilla_xls.getCell(5, 7).getCellFormat());
            		hojaPlantilla_xls.addCell(lab2);
		
            		lab2 = new jxl.write.Label(6, filaRow, listItem.get("cargo"), hojaPlantilla_xls.getCell(6, 7).getCellFormat());
            		hojaPlantilla_xls.addCell(lab2);
		
            		lab2 = new jxl.write.Label(7, filaRow, utilitario.getFormatoNumeroMoney(listItem.get("rmu")), hojaPlantilla_xls.getCell(7, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(8, filaRow, listItem.get("regimen_laboral"), hojaPlantilla_xls.getCell(8, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(9, filaRow, listItem.get("nro_vacacion_periodo_anterior"), hojaPlantilla_xls.getCell(9, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(10, filaRow, listItem.get("nro_vacacion_periodo_actual"), hojaPlantilla_xls.getCell(10, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(11, filaRow, listItem.get("nro_dias_acumulados"), hojaPlantilla_xls.getCell(11, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(12, filaRow, listItem.get("nro_dias_tomados_periodo_anterior"), hojaPlantilla_xls.getCell(12, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(13, filaRow, nro_dias_tomados_periodo_actual, hojaPlantilla_xls.getCell(13, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(14, filaRow, total_dias_descontados, hojaPlantilla_xls.getCell(14, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(15, filaRow, listItem.get("dias_fin_semana"), hojaPlantilla_xls.getCell(15, 7).getCellFormat());
            		hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(16, filaRow, listItem.get("total_dias_descomtados_periodo_actual"), hojaPlantilla_xls.getCell(16, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		lab2 = new jxl.write.Label(17, filaRow, vacaciones_por_tomar, hojaPlantilla_xls.getCell(17, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
            		//fila++;
            		
            		items_ajuste.put("filaOldRow", CConversion.CStr(filaRow));
            		items_ajuste.put("dias_descontados_actual",utilitario.getFormatoNumero(nro_dias_tomados_periodo_actual, 2));
            		items_ajuste.put("vacaciones_pendientes",utilitario.getFormatoNumero(vacaciones_por_tomar, 2));
            		items_ajuste.put("estado_asvac", listItem.get("estado_asvac"));

            		m_ajuste.put(CConversion.CInt(listItem.get("ide_periodo_asvac")), items_ajuste);
				}
            }
            
            //fila++;
       }
	}
	String ide_gtemp_old = "";
	Map<Integer, List<Map<String, String>>> mapPeriodos = new  LinkedHashMap<Integer, List<Map<String,String>>>();
	private void construyeAuxVacaciones(TablaGenerica infoVacacionEmpleado, int indice, int anio_reporte)  throws RowsExceededException, WriteException {
		jxl.write.Label lab2;
		int filaRow =  7+fila, filaRowOld =0;
		double dias_vacaciones=0.0, dias_ajuste = 0.00;
		String ide_gtemp = infoVacacionEmpleado.getValor(indice, "IDE_GTEMP");
		String ide_asvac = infoVacacionEmpleado.getValor(indice, "ide_asvac");
		String ide_rgml = infoVacacionEmpleado.getValor(indice, "ide_gttem");
		String rgml = infoVacacionEmpleado.getValor(indice, "detalle_gttem");
		String rmu = infoVacacionEmpleado.getValor(indice, "rmu_geedp");
		String cargo = infoVacacionEmpleado.getValor(indice, "titulo_cargo_gepgc");
		String fecha_ingreso_asvre = infoVacacionEmpleado.getValor(indice, "fecha_ingreso_asvre");
		String fecha_finiquito_asvre = infoVacacionEmpleado.getValor(indice, "fecha_finiquito_asvre");
		String nro_dias_tomados_asvre = infoVacacionEmpleado.getValor(indice, "nro_dias_tomados_asvre");
		String ide_periodo_asvre = infoVacacionEmpleado.getValor(indice, "ide_periodo_asvre");
		double nro_dias_tomados = 0d, nro_dias_vacaciones =0d, nro_dias_periodo_anterioro =0d, nro_dias_acumulados = 0d, nro_dias_descontados_periodo_anterior = 0d, total_dias_descontados = 0d, 
				total_dias_descomtados_periodo_actual = 0d, vacaciones_por_tomar = 0d; 
		int fin_semana = 0;
		int anio_anterior=0;
		
		boolean estado_asvac = CConversion.CBol(infoVacacionEmpleado.getValor(indice, "activo_asvac"));
		boolean estado_asvre = CConversion.CBol(infoVacacionEmpleado.getValor(indice, "activo_asvre"));
		
		Map<String, String> m_periodo = getPeriodoAnio(fecha_ingreso_asvre, fecha_finiquito_asvre, anio_reporte);
		Map<String, String> m_items = new TreeMap<String, String>();
		Map<String, String> mapItems = new LinkedHashMap<String, String>();
		
		String periodo = m_periodo.get("fecha_ingreso")+" - "+m_periodo.get("fecha_finiquito");
		double diasTrabajados = getNumeroDiasEmpleado(ide_gtemp, ide_asvac, m_periodo.get("fecha_ingreso"), m_periodo.get("fecha_finiquito"));
		if (CConversion.CInt(ide_rgml) == 1) {
			dias_vacaciones = (diasTrabajados*15)/360;
		}else if(CConversion.CInt(ide_rgml) == 2) {
			dias_vacaciones = (diasTrabajados*30)/360;
		}
		
		nro_dias_vacaciones = dias_vacaciones;
		nro_dias_tomados = CConversion.CDbl(ser_empleado.getTotalDiasVacacionPorFechas(ide_gtemp, m_periodo.get("fecha_ingreso"), m_periodo.get("fecha_finiquito")));
		
		/*if (fecha_ingreso_asvre.equals(m_periodo.get("fecha_ingreso")) && fecha_finiquito_asvre.equals(m_periodo.get("fecha_finiquito"))) {
			nro_dias_tomados = CConversion.CDbl(nro_dias_tomados_asvre);
		}else {
			nro_dias_tomados = CConversion.CDbl(ser_empleado.getTotalDiasVacacionPorFechas(ide_gtemp, m_periodo.get("fecha_ingreso"), m_periodo.get("fecha_finiquito")));
		}*/
		
		if(m_parametros.containsKey(CConversion.CInt(ide_gtemp))){
			for (Map.Entry<Integer, List<Map<String,String>>> entry : m_parametros.entrySet()) {
	            Integer key = entry.getKey();
	            if (key == CConversion.CInt(ide_gtemp)) {
	            	List<Map<String,String>> values = entry.getValue();
		            
	            	for(int index = 0 ; index < values.size() ; index++){
	            	    Map<String, String> listItem = values.get(index);
	            	    if (CConversion.CInt(listItem.get("anio_anterior")) == (anio_reporte-1)) {
							nro_dias_acumulados = CConversion.CDbl(listItem.get("dias_acumulados"));
							nro_dias_descontados_periodo_anterior = CConversion.CDbl(listItem.get("dias_descontados"));
							
							mapItems.put("anio", listItem.get("anio_anterior"));
							mapItems.put("fila", listItem.get("fila"));
							mapItems.put("estado", listItem.get("estado_asvac"));
							mapItems.put("total_dias_descontados", listItem.get("dias_descontados"));
							mapItems.put("total_dias_vacacion", listItem.get("vacaciones_por_tomar"));
							List<Map<String, String>> items1 = new ArrayList<Map<String,String>>();
							items1.add(mapItems);
							mapPeriodos.put(CConversion.CInt(ide_gtemp_old), items1);
			
			
		}else {
							nro_dias_acumulados = 0d;
							nro_dias_descontados_periodo_anterior = 0;
						}
	            	}
				}
	       }
		}else {
			nro_dias_acumulados = 0d;
			nro_dias_descontados_periodo_anterior = 0;
		}
		
		if (!ide_gtemp.equals(ide_gtemp_old)) {
			for (Map.Entry<Integer, List<Map<String,String>>> entry : mapPeriodos.entrySet()) {
	            Integer key = entry.getKey();
	            if (key == CConversion.CInt(ide_gtemp_old)) {
		            List<Map<String,String>> values = entry.getValue();
		            
	            	for(int index = 0 ; index < values.size() ; index++){
	            	    Map<String, String> listItem = values.get(index);
	            	    if (CConversion.CBol(listItem.get("estado")) && CConversion.CDbl(listItem.get("total_dias_vacacion")) >= CConversion.CDbl(listItem.get("total_dias_descontados"))) {
	            	    	
						}
	            	}
	            }
	       }
			ide_gtemp_old = ide_gtemp;
		}
		nro_dias_periodo_anterioro = nro_dias_acumulados;
		nro_dias_acumulados = nro_dias_periodo_anterioro+CConversion.CDbl(nro_dias_vacaciones);
		nro_dias_descontados_periodo_anterior = total_dias_descontados;
		total_dias_descontados = nro_dias_descontados_periodo_anterior + CConversion.CDbl(nro_dias_tomados);
		fin_semana = getNroDiasFinSemana(CConversion.CDbl(total_dias_descontados));
		total_dias_descomtados_periodo_actual = total_dias_descontados + fin_semana;
		vacaciones_por_tomar = nro_dias_acumulados-total_dias_descomtados_periodo_actual;
		
		
		lab2 = new jxl.write.Label(5, filaRow, ""+periodo, hojaPlantilla_xls.getCell(5, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(6, filaRow, cargo, hojaPlantilla_xls.getCell(6, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(7, filaRow, utilitario.getFormatoNumeroMoney(rmu), hojaPlantilla_xls.getCell(7, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(8, filaRow, rgml, hojaPlantilla_xls.getCell(8, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(10, filaRow, utilitario.getFormatoNumero(nro_dias_vacaciones,2), hojaPlantilla_xls.getCell(10, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(9, filaRow, utilitario.getFormatoNumero(nro_dias_periodo_anterioro,2), hojaPlantilla_xls.getCell(9, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(11, filaRow, utilitario.getFormatoNumero(nro_dias_acumulados,2), hojaPlantilla_xls.getCell(11, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(12, filaRow, utilitario.getFormatoNumero(nro_dias_descontados_periodo_anterior,2), hojaPlantilla_xls.getCell(12, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(13, filaRow, utilitario.getFormatoNumero(nro_dias_tomados,2), hojaPlantilla_xls.getCell(13, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(14, filaRow, utilitario.getFormatoNumero(total_dias_descontados,2), hojaPlantilla_xls.getCell(14, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(15, filaRow, ""+fin_semana, hojaPlantilla_xls.getCell(15, 7).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(16, filaRow, utilitario.getFormatoNumero(total_dias_descomtados_periodo_actual,2), hojaPlantilla_xls.getCell(16, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
		lab2 = new jxl.write.Label(17, filaRow, utilitario.getFormatoNumero(vacaciones_por_tomar,2), hojaPlantilla_xls.getCell(17, 7).getCellFormat());
			hojaPlantilla_xls.addCell(lab2);
			
			
		if (m_parametros.containsKey(CConversion.CInt(infoVacacionEmpleado.getValor(indice, "IDE_GTEMP")))) {			
			Map<String, String> m_items2 = new TreeMap<String, String>();
			m_items2.put("dias_acumulados", utilitario.getFormatoNumero(nro_dias_acumulados,2));
			m_items2.put("dias_descontados", utilitario.getFormatoNumero(total_dias_descontados,2));
			m_items2.put("anio_anterior", CConversion.CStr(anio_reporte));
			m_items2.put("fila", CConversion.CStr(filaRow));
			m_items2.put("nro_dias_tomados_asvre", nro_dias_tomados_asvre);
			m_items2.put("estado_asvac", CConversion.CStr(estado_asvac));
			m_items2.put("ide_periodo_asvre", ide_periodo_asvre);
			m_items2.put("fila", CConversion.CStr(fila));
			m_items2.put("vacaciones_por_tomar", utilitario.getFormatoNumero(vacaciones_por_tomar,2));
			m_items2.put("ide_asvac", ide_asvac);
			
			m_parametros.get(CConversion.CInt(infoVacacionEmpleado.getValor(indice, "IDE_GTEMP"))).add(m_items2);
		}else {
			m_items.put("dias_acumulados", utilitario.getFormatoNumero(nro_dias_acumulados,2));
			m_items.put("dias_descontados", utilitario.getFormatoNumero(total_dias_descontados,2));
			m_items.put("anio_anterior", CConversion.CStr(anio_reporte));
			m_items.put("fila", CConversion.CStr(filaRow));
			m_items.put("nro_dias_tomados_asvre", nro_dias_tomados_asvre);
			m_items.put("estado_asvac", CConversion.CStr(estado_asvac));
			m_items.put("ide_periodo_asvre", ide_periodo_asvre);
			m_items.put("fila", CConversion.CStr(fila));
			m_items.put("vacaciones_por_tomar", utilitario.getFormatoNumero(vacaciones_por_tomar,2));
			m_items.put("ide_asvac", ide_asvac);
			
			List<Map<String, String>> items = new ArrayList<Map<String,String>>();
			items.add(m_items);
			m_parametros.put(CConversion.CInt(infoVacacionEmpleado.getValor(indice, "IDE_GTEMP")), items);
		}
	}
			
	private Map<String, String> getPeriodoAnio(String FECHA_INGRESO, String FECHA_FINIQUITO, int ANIO_PERIODO) {
		Map<String, String> mp = new TreeMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = FECHA_INGRESO;
		String dateOutString = FECHA_FINIQUITO;
		
		try {
			
			Date dateIn = sdf.parse(dateInString);
			Date dateOut = sdf.parse(dateOutString);
			
			Calendar calendarIn = Calendar.getInstance();
    		calendarIn.setTime(dateIn);
    		int diasIn = calendarIn.get(Calendar.DAY_OF_MONTH);
    		int mesIn = calendarIn.get(Calendar.MONTH);
    		int anioIn = calendarIn.get(Calendar.YEAR);
    		 
    		Calendar calendarOut = Calendar.getInstance();
     		calendarOut.setTime(dateOut);
     		int diasOut = calendarOut.get(Calendar.DAY_OF_MONTH);
     		int mesOut = calendarOut.get(Calendar.MONTH);
     		int anioOut = calendarOut.get(Calendar.YEAR);
     		
     		if(anioIn != ANIO_PERIODO) {
     			mp.put("fecha_ingreso", ANIO_PERIODO+"-01-01");
     			mp.put("cambioFecha", "true");
     		}else {
     			mp.put("fecha_ingreso", FECHA_INGRESO);
     			mp.put("cambioFecha", "false");
     		}
     		
     		if(anioOut != ANIO_PERIODO) {
     			mp.put("fecha_finiquito", ANIO_PERIODO+"-12-31");
     			mp.put("cambioFecha", "true");
     		}else {
     			mp.put("fecha_finiquito", FECHA_FINIQUITO);
     			mp.put("cambioFecha", "false");
     		}
		} catch (Exception e) {
			System.out.println("EXECPCION getPeriodoAnio: "+e.getMessage());
		}
		
		return mp;
		}
	
	private Integer getNroDiasFinSemana(Double nro_dias_tomados) {
		double dias_vacacion = CConversion.CDbl(nro_dias_tomados);
		int p_etn1 = ((int) dias_vacacion / 5);
		int valor = (p_etn1 * 2);
		return valor;
	}
	
	private Double cuadreDiasTomadosVacacion(Double nro_dias_tomados) {
		double dias_vacacion = CConversion.CDbl(nro_dias_tomados);
		int p_etn1 = ((int) dias_vacacion / 5);
		double descuento = dias_vacacion + (p_etn1 * 2);
		
		return descuento;
	}
	
	private String getPeriodoLiquidado(String fecha_inicio, String fecha_fin) {
		int difDias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_inicio), utilitario.getFecha(fecha_fin));
		String valor_retorno = "";
		if(difDias > 3) {
			valor_retorno = fecha_inicio+" / "+fecha_fin;
		}
		
		return valor_retorno;
	}
	
	private void updateDiasVacacionesEmpleados(TablaGenerica EMPLEADOS) {
		for (int emp =0; emp<EMPLEADOS.getTotalFilas(); emp++) {
			String IDE_GTEMP = EMPLEADOS.getValor(emp, "IDE_GTEMP");
			TablaGenerica tabAsiVacaciones = utilitario.consultar("select * from asi_vacacion where ide_gtemp ="+IDE_GTEMP+" and activo_asvac=true order by ide_asvac asc");
			for (int v =0; v<tabAsiVacaciones.getTotalFilas(); v++) {
		bandEntrada = 0;
		bandSalida = 0;
		boolean reporte = false;
				
				String ide_asvac = tabAsiVacaciones.getValor(v, "IDE_ASVAC");
				
		try {
			nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
			TablaGenerica tab_partida = getPartida(IDE_GTEMP);

			String fecha_ingreso_continuidad = "", fecha_fin_continuidad = "", ide_asvac_continuidad = "",
					fecha_ingreso_continuidad_temp = "";
			ide_asvac_continuidad = ide_asvac;
			boolean bandFecha = false;
			TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,"
					+ "fecha_finiquito_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,activo_asvac "
					+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP + " " + "AND IDE_ASVAC=" + ide_asvac);


			TablaGenerica tab_vacacion_continuidad_temp = utilitario.consultar(
					"select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
							+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP
							+ " and activo_asvac=false and ide_asvac not in (" + tab_codigo_vacacion.getValor("ide_asvac")
							+ ") " + " and fecha_ingreso_asvac < '" + tab_codigo_vacacion.getValor("fecha_ingreso_asvac")
							+ "' and es_continuidad=false " + "order by ide_asvac desc limit 1");

			TablaGenerica tab_vacacion_continuidad = null;
			
			if (tab_vacacion_continuidad_temp.getTotalFilas() > 0) {
				tab_vacacion_continuidad = utilitario.consultar(
						"select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
								+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP
								+ " and activo_asvac=false and ide_asvac not in ("
								+ tab_codigo_vacacion.getValor("ide_asvac") + ") " + " and fecha_ingreso_asvac  between '"
								+ utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario
										.DeStringADate(tab_vacacion_continuidad_temp.getValor("fecha_ingreso_asvac")), 1))
								+ "'  and '" + tab_codigo_vacacion.getValor("fecha_ingreso_asvac") + "'  "
								+ "order by ide_asvac desc");

				
			} else {
				tab_vacacion_continuidad = utilitario.consultar(
						"select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
								+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP
								+ " and activo_asvac=false and ide_asvac not in ("
								+ tab_codigo_vacacion.getValor("ide_asvac") + ") " + " and fecha_ingreso_asvac < '"
								+ tab_codigo_vacacion.getValor("fecha_ingreso_asvac") + "' " + "order by ide_asvac desc");

			}

			if (tab_vacacion_continuidad.getTotalFilas() > 0) {
				for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
					if (tab_vacacion_continuidad.getValor(i, "es_continuidad") == null
							|| tab_vacacion_continuidad.getValor(i, "es_continuidad").equals("false")) {
						// SI TIENE CONTINUIDAD
						if (tab_vacacion_continuidad.getTotalFilas() == 1) {
							// ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac");
							fecha_ingreso_continuidad = tab_codigo_vacacion.getValor("fecha_ingreso_asvac");

						} else {
							// ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac")+",";
							fecha_ingreso_continuidad = tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
						}

						break;
					} else {
						// SI ES LIQUIDACION
						fecha_ingreso_continuidad_temp = tab_vacacion_continuidad.getValor(i, "fecha_ingreso_asvac");
						fecha_ingreso_continuidad = tab_vacacion_continuidad.getValor(i, "fecha_ingreso_asvac");

						bandFecha = true;
						if ((bandFecha == true && i > 0) && (fecha_ingreso_continuidad_temp
								.compareTo(tab_vacacion_continuidad.getValor((i - 1), "fecha_ingreso_asvac")) > 0)) {
							// if(bandFecha== true &&
							// (fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==0 ||
							// fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==-1)){
							fecha_ingreso_continuidad = tab_vacacion_continuidad.getValor((i - 1), "fecha_ingreso_asvac");
						} else {
							fecha_ingreso_continuidad = tab_vacacion_continuidad.getValor(i, "fecha_ingreso_asvac");
						}

						if ((tab_vacacion_continuidad.getTotalFilas() - 1) == i) {
							ide_asvac_continuidad += "," + tab_vacacion_continuidad.getValor(i, "ide_asvac");
						} else {
							ide_asvac_continuidad += "," + tab_vacacion_continuidad.getValor(i, "ide_asvac");
						}

					}
				}
			} else {
				fecha_ingreso_continuidad = tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
			}

			if (tab_codigo_vacacion.getValor("activo_asvac") == null || tab_codigo_vacacion.getValor("activo_asvac").equals("false")) {
				fecha_fin_continuidad = tab_codigo_vacacion.getValor("fecha_finiquito_asvac");
			} else {
				// fecha_fin_continuidad=null;
				fecha_fin_continuidad = utilitario.getFechaActual();
			}

			// nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
			nde = getNumeroDiasEmpleado(IDE_GTEMP, ide_asvac_continuidad, fecha_ingreso_continuidad, fecha_fin_continuidad);

			// ndeInicialFechaCalculoDiasTomados =
			// getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
			ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP, ide_asvac);

			// Recalculo los valores para q no se pasen de 360 dias al a�o
			// nde = getDiasEmpleadoXAnio(nde, nda);
			// ndeInicialFechaCalculoDiasTomados =
			// getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);
			// Obtener los periodos de cada empleado 1,2,3 etc
			List<Integer> peridos = getPeriodos(nda, nde);
			// Obtengo los periodos para el ajuste de dias pendientes del
			// empleado hasta el 30 de abril del 2017
			List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);
			
			ide_gttem = getGrupoTipoEmpleado(IDE_GTEMP, ide_asvac);
			
			if (ide_gttem == 0) {
				System.out.println("El empleado seleccionado no contiene acci�n de personal, Debe realizar la acci�n correspondiente para el c�lculo de vacaciones del empleado: "+IDE_GTEMP);
				return;
			}
			if (ide_gttem == 10) {
				System.out.println("Periodo de vacaciones inactivo, Debe activar el periodo de vacaciones del empleado seleccionado: "+IDE_GTEMP+" ide asvac: "+ide_asvac);
				return;
			}
			
			String banderaCodigo = "";

			if (ide_gttem == 1) {
				// Asigno el numero de dias max de vacaciones al a�o 15 dias
				numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
				// Los dias maximo q puede acumular 45 dias por tres periodos
				p_asi_dias_max_vacaciones_codigo_trabajo = Integer
						.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
				banderaCodigo = "true";
				// Aumentamos dia por trabajar mas de 5 anios
				if (peridos.size() > 5) {
					double anio_periodo = peridos.size();
					division = anio_periodo / 5;
					nde = nde + (int) division;
				}

			}

			if (ide_gttem == 2) {
				// Asigno el numero de dias max de vacaciones al a�o 15 dias
				numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
				// Los dias maximo q puede acumular 45 dias por tres periodos
				p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));
				banderaCodigo = null;

			}

			// Obtengo el total de mis dias pendientes al restar los dias
			// acumulados-dias tomados
			double sumatotalDiasGenerados = 0.0;
			// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
			// que le corresponde y los dias generados hasta el presente
			List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
			// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
			// que le corresponde y los dias generados hasta el 30 de abril de

			List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda, ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
			double sumatotal_vacaciones = 0.0;
			// Asigno la sumatoria de total de mis dias acumulados a vacacion
			// desde la fecha de ingreso hasta la fecha de hoy

			BigDecimal num1 = new BigDecimal(0);

			for (int i = 0; i < vacacionesPeriodo.size(); i++) {
				sumatotal_vacaciones = (sumatotal_vacaciones + vacacionesPeriodo.get(i));

			}
			// System.out.println("Suma vacaciones hasta Fecha Actual: "+
			// sumatotal_vacaciones);

			double sumatotal_vacacionesCalculoInicial = 0.0;

			// Asigno la sumatoria de total de mis dias acumulados a vacacion
			// desde la fecha de ingreso hasta el 30 de Abril de 2017
			for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
				sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial
						+ vacacionesPeriodoCalculoInicial.get(i);
			}
			// System.out.println("Suma vacaciones hasta 31 de Mayo: "+
			// sumatotal_vacacionesCalculoInicial);

			// Devuelve el numero de dias pendientes obtenidos de excel hasta el
			// 30 de abril

			numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP, ide_asvac_continuidad);
			totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP, ide_asvac_continuidad);
			nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP, ide_asvac_continuidad);
			diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP, ide_asvac_continuidad);
			
			if (numeroDiasTomadosInicial != 0) {

				if (numeroDiasTomadosInicial < 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
					// valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion()
							.ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac=" + valor
									+ " where ide_GTEMP=" + IDE_GTEMP);
					utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
					totalNumeroDiasAjuste = valor;

				}
				if (numeroDiasTomadosInicial > 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
					// valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion()
							.ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac=" + valor
									+ " where ide_GTEMP=" + IDE_GTEMP);
					utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
					totalNumeroDiasAjuste = valor;

				}

			}

			totalNumeroDiasTomadosInicial = getNumeroDiasTomados(IDE_GTEMP, ide_asvac_continuidad);
			numeroDiasTomados = totalNumeroDiasAjuste;
			numeroDiasTomadosTemporal = numeroDiasTomados;

			// suma el valor de los dias descontados de la tabla
			// asi_detalle_vacacion
			// Aqui se encuentra el cuadre
			dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
			int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
			double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);

			numeroDiasTomadosFinSemana = getNumeroDiasTomadosFinSemana(IDE_GTEMP, ide_asvac_continuidad);
			
			double resultado_descuento = descuento + numeroDiasTomados;
			if (ide_gttem == 2) {

				double nro_dias_ajuste_periodo_asvac = 0;

				if ((sumatotal_vacaciones - (numeroDiasTomados + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana)) > 60) {
					// System.out.println("Ingreso a descuento mayor a 60");

					BigDecimal sumatotal_vacaciones1 = new BigDecimal(sumatotal_vacaciones);
					BigDecimal numeroDiasTomados1 = new BigDecimal(numeroDiasTomados);
					BigDecimal descuento1 = new BigDecimal(descuento);
					BigDecimal nroDiasAjustePeriodo1 = new BigDecimal(nroDiasAjustePeriodo);
					BigDecimal dias = new BigDecimal(60);
					BigDecimal numeroDiasTomadosFinSemana1 = new BigDecimal(numeroDiasTomadosFinSemana);
					BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(
							numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1)))
							.subtract(dias));

					if (calculoPasaDiasPendientes.doubleValue() >= 0.01) {

						TablaGenerica tab_codigo = utilitario
								.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
						utilitario.getConexion().ejecutarSql(
								"insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
										+ "values ( " + tab_codigo.getValor("codigo") + "," + ide_asvac + ",'"
										+ utilitario.getFechaActual() + "'," + (calculoPasaDiasPendientes)
										+ ",'APLICACI�N DESCUENTO POR EXCESO DE DIAS' ,true, false)");

					}
					nroDiasAjustePeriodo = calculoPasaDiasPendientes.doubleValue() + nroDiasAjustePeriodo;
				}
			}

			if (ide_gttem == 1) {

				double nro_dias_ajuste_periodo_asvac = 0;
				if ((sumatotal_vacaciones - (numeroDiasTomados + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana)) > 45) {

					// System.out.println("Ingreso a descuento mayor a 45");

					BigDecimal sumatotal_vacaciones1 = new BigDecimal(sumatotal_vacaciones);
					BigDecimal numeroDiasTomados1 = new BigDecimal(numeroDiasTomados);
					BigDecimal descuento1 = new BigDecimal(descuento);
					BigDecimal nroDiasAjustePeriodo1 = new BigDecimal(nroDiasAjustePeriodo);
					BigDecimal dias = new BigDecimal(45);
					BigDecimal numeroDiasTomadosFinSemana1 = new BigDecimal(numeroDiasTomadosFinSemana);
					BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(
							numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1)))
							.subtract(dias));

					if (calculoPasaDiasPendientes.doubleValue() >= 0.01) {

						TablaGenerica tab_codigo = utilitario
								.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
						utilitario.getConexion().ejecutarSql(
								"insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
										+ "values ( " + tab_codigo.getValor("codigo") + "," + ide_asvac + ",'"
										+ utilitario.getFechaActual() + "'," + (calculoPasaDiasPendientes)
										+ ",'APLICACI�N DESCUENTO POR EXCESO DE DIAS' ,true, false)");
					}
					nroDiasAjustePeriodo = calculoPasaDiasPendientes.doubleValue() + nroDiasAjustePeriodo;

				}
			}
			

			numeroDiasTomadosFinSemana = getNumeroDiasTomadosFinSemana(IDE_GTEMP, ide_asvac_continuidad);
			numeroDiasTomados = numeroDiasTomados + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana;
			
			/*
			 * Tomo los parametros creados para realizar los calculos de los dias pendientes
			 * calculo de vacaciones dependiendo si el empleado pertenece a la losep o al
			 * codigo de trabajo
			 */

			List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);

			String periodoNuevo = "";
			String periodoNuevoEntrada = "", periodoNuevoSalida = "";
			int valorDiasXAnio = 0;
			List<Double[]> matriz = new ArrayList<Double[]>();

			TablaGenerica tabVacacionInicial = utilitario.consultar("select ide_asvac,ide_gtemp,fecha_ingreso_asvac,activo_asvac,fecha_finiquito_asvac  from asi_vacacion where ide_asvac="
							+ ide_asvac);

			// if (tabVacacionInicial.getValor("activo_asvac")!=null ||
			// tabVacacionInicial.getValor("activo_asvac").equals("true")) {
			utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac=" + ide_asvac);
			// }
			TablaGenerica tab_asi_vacacion_resumen = null;
			boolean banderaSalidaEmpleado = false;
			
			String parametro_dia_extra = utilitario.getVariable("p_asi_empleados_codigo_trabajo_dia_extra");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado = parametro_dia_extra.split(",");
			boolean empleado_dia_extra_vacacion = false;
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				if (tabVacacionInicial.getValor("IDE_GTEMP").equals(listaAccionesEmpleado[i].toString())) {
					empleado_dia_extra_vacacion = true;
					i = listaAccionesEmpleado.length;
				} else {
					empleado_dia_extra_vacacion = false;
				}
			}

			utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac=" + ide_asvac);
			boolean fin_accion = false, fin_accion_continua = false;
			TablaGenerica acciones = retornaTablaAccionResumenVacacion(fecha_ingreso_continuidad, fecha_fin_continuidad,IDE_GTEMP);
			double dias_ = 0.0;
			dias_ = numeroDiasTomados;
			double valor_retorno = 0.0;
			int contador = 0;
			for (int i = 0; i < acciones.getTotalFilas(); i++) {
				String fec_geedp = "", fec_fin_geedp = "";
				dias_ = dias_ - valor_retorno;
				if ((acciones.getTotalFilas() - 1) == i) {
					if (acciones.getValor(i, "fecha_liquidacion_geedp") == null
							|| acciones.getValor(i, "fecha_liquidacion_geedp").isEmpty()) {
						contador++;
						fec_geedp = acciones.getValor(i, "fecha_geedp");
						fec_fin_geedp = utilitario.getFechaActual();
						valor_retorno = getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp,
								acciones.getValor(i, "ide_geedp"), dias_, contador, ide_asvac);
					} else {
						contador++;
						fec_geedp = acciones.getValor(i, "fecha_geedp");
						fec_fin_geedp = acciones.getValor(i, "fecha_liquidacion_geedp");
						valor_retorno = getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp,
								acciones.getValor(i, "ide_geedp"), dias_, contador, ide_asvac);

					}
				} else {

					fec_geedp = acciones.getValor(i, "fecha_geedp");
					// fec_fin_geedp=acciones.getValor((i+1),"fecha_geedp");

					if (acciones.getValor((i + 1), "fecha_liquidacion_geedp") == null
							|| acciones.getValor((i + 1), "fecha_liquidacion_geedp").isEmpty()) {
						fec_geedp = acciones.getValor(i, "fecha_geedp");

						while (fec_geedp.equals(acciones.getValor((i + 1), "fecha_geedp"))) {
							i++;
							if (((i + 1) == acciones.getTotalFilas())) {
								if (acciones.getValor((i), "fecha_liquidacion_geedp") == null
										|| acciones.getValor((i), "fecha_liquidacion_geedp").isEmpty()) {
									// if(fec_geedp.equals(acciones.getValor((i),"fecha_geedp"))){
									fin_accion = true;
									fin_accion_continua = true;
								} else {
									fin_accion = false;
									fin_accion_continua = true;

								}

								break;
							}
						}
						contador++;
						if (fin_accion) {
							fec_fin_geedp = utilitario.getFechaActual();
						} else if (fin_accion == false && fin_accion_continua == true) {

							fec_fin_geedp = acciones.getValor((i), "fecha_liquidacion_geedp");
						} else {
							fec_fin_geedp = utilitario.DeDateAString(utilitario.sumarDiasFecha(
									utilitario.DeStringADate(acciones.getValor((i + 1), "fecha_geedp")), -1));
						}
						valor_retorno = getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp,
								acciones.getValor(i, "ide_geedp"), dias_, contador, ide_asvac);
						// i=i+1;
					} else {
						contador++;
						fec_geedp = acciones.getValor(i, "fecha_geedp");
						fec_fin_geedp = acciones.getValor((i + 1), "fecha_liquidacion_geedp");
						valor_retorno = getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp,
								acciones.getValor(i, "ide_geedp"), dias_, contador, ide_asvac);
						i = i + 1;
					}

				}
			}
			Integer dimension = vacacionesPeriodo.size();

			try {
				/*tab_asi_vacacion_resumen = utilitario.consultar("select ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
								+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre, "
								+ "ide_periodo_asvre, usuario_ingre, fecha_ingre, usuario_actua, "
								+ "fecha_actua, hora_ingre, hora_actua, base_imponible_asvre, dias_trabajados_liquidacion_asvre, "
								+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac, "
								+ "ide_geedp_asvac " + "from asi_vacacion_resumen_empleado " + "where activo_asvre=true"
								+ " and ide_asvac=" + ide_asvac + " order by ide_asvre asc");*/
				tab_asi_vacacion_resumen = utilitario.consultar("SELECT EMP.IDE_GTEMP, VRE.IDE_ASVRE, VRE.IDE_ASVAC, VRE.PERIODO_ASVRE, VRE.NRO_DIAS_VACACION_ASVRE, "
						+ "VRE.NRO_DIAS_TOMADOS_ASVRE, VRE.NRO_DIAS_PENDIENTES_ASVRE, VRE.IDE_PERIODO_ASVRE, VRE.ACTIVO_LIQUIDACION, VRE.VALOR_PAGAR_ASVRE, VRE.VALOR_DIA_ASVRE, VRE.IDE_GEEDP_ASVAC "
						+ "FROM ASI_VACACION AS VAC "
						+ "JOIN GTH_EMPLEADO AS EMP ON EMP.IDE_GTEMP = VAC.IDE_GTEMP "
						+ "JOIN ASI_VACACION_RESUMEN_EMPLEADO AS VRE ON VRE.IDE_ASVAC = VAC.IDE_ASVAC "
						+ "WHERE VAC.IDE_GTEMP = "+IDE_GTEMP+" AND VRE.ACTIVO_ASVRE = TRUE AND VAC.FECHA_INGRESO_ASVAC BETWEEN '2021-10-04' AND '2021-11-30'"
						+ "ORDER BY VRE.IDE_ASVRE asc");
				if (tabVacacionInicial.getValor("ACTIVO_ASVAC").equals("true")) {
					banderaSalidaEmpleado = false;
				} else {
					banderaSalidaEmpleado = true;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (reporte == false) {
				
				/*int ide_periodo_asvre_actual = 0;			
				for (int j = 0; j < tab_asi_vacacion_resumen.getTotalFilas(); j++) {
					m_vacaciones.put(""+j, new ArrayList<String>(Arrays.asList(IDE_GTEMP,
							tab_asi_vacacion_resumen.getValor(j, "periodo_asvre"),
							tab_asi_vacacion_resumen.getValor(j, "nro_dias_vacacion_asvre"),
							tab_asi_vacacion_resumen.getValor(j, "nro_dias_tomados_asvre"), 
							tab_asi_vacacion_resumen.getValor(j, "nro_dias_pendientes_asvre"))
						)
					);
				}*/
				
				/*for (Map.Entry<String, List<String>> entry : m_vacaciones.entrySet()) {
		            String key = entry.getKey();
		            List<String> values = entry.getValue();
		            
		            //System.out.println("key: "+key+" values: "+values);
		       }*/
		

			}

			tab_asi_vacacion_resumen = utilitario.consultar("select IDE_ASVRE,IDE_ASVAC from asi_vacacion_resumen_empleado where activo_asvre is null and ide_asvac=" + ide_asvac);
			if (tab_asi_vacacion_resumen.getTotalFilas() > 0) {

				utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado SET "
						+ "activo_asvre=true where " + "ide_asvac=" + ide_asvac);
			}

			guardarPantalla();
			if (reporte == false) {
				utilitario.addUpdate("dia_aplica_vacion");
			}

			/**
			 * Obtencion de dias pendientes del empleado
			 */
			// System.out.println("numero dias pendientes vacacion: "+
			// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));

			double dias = 0.0, dias_extra = 0.0, vacaciones_extra = 0.0;
			int dias_final_extra = 0;
			if (ide_gttem == 1) {
				double dias_extra_ = (sumatotal_vacaciones / 5);
				dias_extra = dias_extra_;
				int diasE = pckUtilidades.CConversion.CInt(dias_extra);
			}

			dias_pendientes = getNumeroDiasPendientes(nde, nda, numeroDiasTomados, numeroDiasVacacionXAnio);
			// redondeo el valor
			// dias_pendientes = Math.rint(dias_pendientes * 100) / 100;

			// //////////////////////////////////////////////////7vsvnkkskvdvskvnvdsknsd/////////////////////////////////////////////////7
			dias_pendientes = (Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones, 2)) - Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados, 2)));
			// dias_pendientes = Math.rint(dias_pendientes * 100) / 100;
			if (ide_gttem == 1) {
				dias_pendientes = dias_pendientes + dias_final_extra;
				numeroInicioFinesSemana = (int) (((sumatotal_vacaciones * 4) + dias_final_extra) / 30);
			} else {
				numeroInicioFinesSemana = (int) ((sumatotal_vacaciones * 4) / 30);
			}

			numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados * 4) / 30);
			numeroInicioFinesSemanaPendientes = numeroInicioFinesSemana - numeroInicioFinesSemanaSolicitados;

			if (ide_gttem == 1) {

				if (reporte == false) {

					if ((int) dias_final_extra > 0 && empleado_dia_extra_vacacion == true) {
						gri_anular_dias_solicitados.getChildren().add(new Etiqueta("Nro dias Adicionales"));
						gri_anular_dias_solicitados.getChildren().add(new Etiqueta("" + (int) dias_final_extra));
					}

				}
			}

			// Si es para reporte
			if (reporte == true) {

				try {
					p_parametros.put("IDE_GTEMP", Long.parseLong(aut_empleado.getValor()));
					p_parametros.put("titulo", "DETALLE DE VACACIONES POR EMPLEADO");
					p_parametros.put("diasacumulados", "" + utilitario.getFormatoNumero(sumatotal_vacaciones, 2));
					p_parametros.put("nrofinesdesemana", "" + numeroInicioFinesSemana);
					p_parametros.put("diasdescontados", "" + (Math.rint(numeroDiasTomados * 100) / 100));
					p_parametros.put("descuentofinesdesemana", "" + numeroInicioFinesSemanaSolicitados);
					p_parametros.put("totaldiaspendientes", "" + (Math.rint((dias_pendientes) * 100) / 100));
					p_parametros.put("nrofinessemanapendientes", "" + numeroInicioFinesSemanaPendientes);
					p_parametros.put("ide_asvac", Integer.parseInt(ide_asvac));
					p_parametros.put("p_coordinador_tthh", utilitario.getVariable("p_gth_coordinador_tthh"));
					p_parametros.put("p_analista_tthh", utilitario.getVariable("p_gth_analista_tthh_vacaciones"));
					p_parametros.put("p_especialiista_tthh", utilitario.getVariable("p_especialiista_tthh"));
					p_parametros.put("dia_extra", (int) dias_final_extra);
					p_parametros.put("fecha_ingreso_asvac", fecha_ingreso_continuidad);

					p_parametros.put("banderaCodigo", banderaCodigo);

					// System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error en generacion de reporte");
				}

			} else {
				/*System.out.println("Dias Acumulados: "+utilitario.getFormatoNumero(sumatotal_vacaciones, 2));
				System.out.println("Nro Fines de Semana: "+numeroInicioFinesSemana);
				System.out.println("Dias Descontados: "+utilitario.getFormatoNumero(numeroDiasTomados, 2));
				System.out.println("Descuento Fines de Semana: "+numeroInicioFinesSemanaSolicitados);
				System.out.println("Total de Dias Pendientes: "+utilitario.getFormatoNumero(dias_pendientes, 2));
				System.out.println("Nro Fines de Semana Pendientes: "+numeroInicioFinesSemanaPendientes);*/
			}			
		} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		}
	}
	// fin Auxiliar de vacaciones
	public void verFichaVacacion(){		
		if(tab_tabla_detalle_vacaccion.getValor("ide_aspvh")!=null && !tab_tabla_detalle_vacaccion.getValor("ide_aspvh").isEmpty()){
			con_ver_ficha_vacacion.getTab_consulta_dialogo().setSql("SELECT " +
					"a.ide_aspvh, " +
					"h.jefe_inmediato, " +
					"j.jefe_talento_humano, " +
					"l.gerente_area, " +
					"b.detalle_asmot, " +
					"a.fecha_solicitud_aspvh,a.fecha_desde_aspvh,a.fecha_hasta_aspvh,a.detalle_aspvh,a.nro_dias_aspvh, " +
					"d.detalle_gemes,c.detalle_geani,a.nro_documento_aspvh,a.aprobado_aspvh,a.activo_aspvh " +
					"FROM ASI_PERMISOS_VACACION_HEXT a " +
					"LEFT JOIN( " +
					"SELECT ide_asmot,detalle_asmot FROM asi_motivo " +
					")b ON a.ide_asmot=b.ide_asmot " +
					"LEFT JOIN( " +
					"SELECT ide_geani,detalle_geani FROM gen_anio " +
					")c ON c.ide_geani=a.ide_geani " +
					"LEFT JOIN( " +
					"SELECT ide_gemes,detalle_gemes FROM gen_mes " +
					")d ON d.ide_gemes=a.ide_gemes " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")g ON g.ide_geedp=a.gen_ide_geedp " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_inmediato FROM gth_empleado " +
					")h ON h.ide_gtemp=g.ide_gtemp " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")i ON i.ide_geedp=a.gen_ide_geedp2 " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_talento_humano FROM gth_empleado " +
					")j ON j.ide_gtemp=i.ide_gtemp " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")k ON k.ide_geedp=a.gen_ide_geedp3 " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as gerente_area FROM gth_empleado " +
					")l ON l.ide_gtemp=k.ide_gtemp " +
					"WHERE a.ide_aspvh in(SELECT ide_aspvh FROM asi_detalle_vacacion where ide_asdev="+tab_tabla_detalle_vacaccion.getValorSeleccionado()+")"); 
			con_ver_ficha_vacacion.getTab_consulta_dialogo().ejecutarSql();	
			con_ver_ficha_vacacion.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede mostrar informaci�n", "No posee vacaci�n");
		}
		
	}
	

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla_vacacion.guardar();
		tab_tabla_detalle_vacaccion.guardar();
		utilitario.getConexion().guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
 if (rep_reporte.getReporteSelecionado().equals("Detalle Vacaciones Por Empleado")){

			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
								rep_reporte.cerrar();
			try {
				getDatosEmpleado(aut_empleado.getValor(),true,tab_tabla_vacacion.getValorSeleccionado());

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error en generacion de reporte aceptarReporte()");
			}

			}
		}
	}	


	public Tabla gettab_tabla_vacacion() {
		return tab_tabla_vacacion;
	}

	public void settab_tabla_vacacion(Tabla tab_tabla_vacacion) {
		this.tab_tabla_vacacion = tab_tabla_vacacion;
	}

	public Tabla gettab_tabla_detalle_vacaccion() {
		return tab_tabla_detalle_vacaccion;
	}

	public void settab_tabla_detalle_vacaccion(Tabla tab_tabla_detalle_vacaccion) {
		this.tab_tabla_detalle_vacaccion = tab_tabla_detalle_vacaccion;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Consulta getCon_ver_vacaciones() {
		return con_ver_vacaciones;
	}

	public void setCon_ver_vacaciones(Consulta con_ver_vacaciones) {
		this.con_ver_vacaciones = con_ver_vacaciones;
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

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}
	
	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
	
	public Consulta getCon_ver_ficha_vacacion() {
		return con_ver_ficha_vacacion;
	}

	public void setCon_ver_ficha_vacacion(Consulta con_ver_ficha_vacacion) {
		this.con_ver_ficha_vacacion = con_ver_ficha_vacacion;
	}		
	
	
	
	
	public void getDatosEmpleado(String IDE_GTEMP,boolean reporte,String ide_asvac){
		 
	    bandEntrada=0;
	    bandSalida=0;
		
		nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		TablaGenerica tab_partida = getPartida(IDE_GTEMP);
		
		String fecha_ingreso_continuidad="",fecha_fin_continuidad="",ide_asvac_continuidad="",fecha_ingreso_continuidad_temp="";
		ide_asvac_continuidad=ide_asvac;
		boolean bandFecha=false;
		TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,"
				+ "fecha_finiquito_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,activo_asvac "
				+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" "
				+ "AND IDE_ASVAC="+ide_asvac);
		
		
		TablaGenerica tab_vacacion_continuidad_temp = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
				+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
				+ " and fecha_ingreso_asvac < '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"' and es_continuidad=false "
				+ "order by ide_asvac desc limit 1");
		
		
		TablaGenerica tab_vacacion_continuidad=null;
		
		if (tab_vacacion_continuidad_temp.getTotalFilas()>0) {
			tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
					+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
					+ " and fecha_ingreso_asvac  between '"+utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_vacacion_continuidad_temp.getValor("fecha_ingreso_asvac")), 1))+"'  and '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"'  "
					+ "order by ide_asvac desc");
		}else {
			tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
					+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
					+ " and fecha_ingreso_asvac < '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"' "
					+ "order by ide_asvac desc");

		}
		
		fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
		
		if (tab_vacacion_continuidad.getTotalFilas()>0) {
			for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
				if (tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
				//SI TIENE CONTINUIDAD
					if (tab_vacacion_continuidad.getTotalFilas()==1) {
					//	ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac");
						fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");

					}else {
						//ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac")+",";	
						//fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
					}
					 
					break;
				}else{
				//SI ES LIQUIDACION
					fecha_ingreso_continuidad_temp=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
					fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");

					bandFecha=true;
					if((bandFecha== true && i>0) && (fecha_ingreso_continuidad_temp.compareTo(tab_vacacion_continuidad.getValor((i-1),"fecha_ingreso_asvac"))>0)){
						//if(bandFecha== true && (fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==0 || fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==-1)){
						fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor((i-1),"fecha_ingreso_asvac");
					}else {
						fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
					} 
					  
					
					 if ((tab_vacacion_continuidad.getTotalFilas()-1)==i) {
						ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");
					}else {
						ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");	
					}

				}
			}
		}else{
			 fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");				
		}
		
		if (tab_codigo_vacacion.getValor("activo_asvac")==null || tab_codigo_vacacion.getValor("activo_asvac").equals("false")) {
			fecha_fin_continuidad=tab_codigo_vacacion.getValor("fecha_finiquito_asvac");
		}else {
			//fecha_fin_continuidad=null;
			fecha_fin_continuidad=utilitario.getFechaActual();
		}
		
		
 

		
		//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
		nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac_continuidad,fecha_ingreso_continuidad,fecha_fin_continuidad);
		//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
		ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

		// Recalculo los valores para q no se pasen de 360 dias al a�o
		//nde = getDiasEmpleadoXAnio(nde, nda);
		//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

		// Obtener los periodos de cada empleado 1,2,3 etc
		List<Integer> peridos = getPeriodos(nda, nde);
		
		
		// Obtengo los periodos para el ajuste de dias pendientes del
		// empleado hasta el 30 de abril del 2017
		List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);

	
	
		
		ide_gttem = getGrupoTipoEmpleado(IDE_GTEMP,ide_asvac);
		if (ide_gttem==0) {
			utilitario.agregarMensajeInfo("El empleado seleccionado no contiene acci�n de personal", "Debe realizar la acci�n correspondiente para el c�lculo de vacaciones del empleado");
			return;
		}
		if (ide_gttem==10) {
			utilitario.agregarMensajeInfo("Periodo de vacaciones inactivo", "Debe activar el periodo de vacaciones del empleado seleccionado");
			return;
		}

		
		//System.out.println("Tipo empleado: "+ide_gttem);
		String banderaCodigo="";
		
		//Dias adicionelae para Codigo de Trabajo
		
		
		if (ide_gttem == 1) {
			// Asigno el numero de dias max de vacaciones al a�o 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
			banderaCodigo="true";	
			
		//Aumentamos dia por trabajar mas de 5 anios
		/*	if(peridos.size()>5){
				double anio_periodo=peridos.size();
				 division=anio_periodo/5;
				nde=nde+(int)division;
			}*/
			
		
		}

		if (ide_gttem == 2) {
			// Asigno el numero de dias max de vacaciones al a�o 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));
			banderaCodigo=null;	

		}
		
			
		
		
		// Obtengo el total de mis dias pendientes al restar los dias
		// acumulados-dias tomados
		double sumatotalDiasGenerados = 0.0;
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el presente
		List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el 30 de abril de
		 
		List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
		 double sumatotal_vacaciones = 0.0;
		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta la fecha de hoy
		 
	//	 BigDecimal num1 = new BigDecimal(0);

	//	for (int i = 0; i < vacacionesPeriodo.size(); i++) {
	//		sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
	///             	
	//	}

	//	System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
	
		double sumatotal_vacacionesCalculoInicial = 0.0;

		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta el 30 de Abril de 2017
		for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
			sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
		}
		//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

		// Devuelve el numero de dias pendientes obtenidos de excel hasta el
		// 30 de abril
	
					//numeroDiasTomadosInicial  = tabla asi_vacacion (dias_tomados_asvac)
		            numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac_continuidad);
					//totalNumeroDiasAjuste  = tabla asi_vacacion (nro_dias_ajuste_asvac)
					totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac_continuidad);
					//nroDiasAjustePeriodo  = tabla ASI_DETALLE_VACACION (dia_acumulado_asdev)
					nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac_continuidad);
					//diasPendientesInicialAjuste  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
					diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac_continuidad);
					
				    //Numero de dias 
					if(numeroDiasTomadosInicial!=0.00)
					  {
						if (numeroDiasTomadosInicial < 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
							utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
							totalNumeroDiasAjuste=valor;
						}
						if (numeroDiasTomadosInicial > 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
														utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
			                totalNumeroDiasAjuste=valor;
							}
				     }
					//totalNumeroDiasTomadosInicial  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
				   //Dias con SIN de semana 
					totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac_continuidad);
					//nro_dias_ajuste_asvac DE LA TABALA ASI VACACION
     				numeroDiasTomados=totalNumeroDiasAjuste;
					numeroDiasTomadosTemporal = numeroDiasTomados;

					// suma el valor de los dias descontados de la tabla
					// asi_detalle_vacacion
					// Aqui se encuentra el cuadre
					dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
					int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
					//EL NUMERO DE DIAS POR FIN DE SEMANA A DESCONTAR
					double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);
					//totalNumeroDiasTomadosInicial  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
				    numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
				    double resultado_descuento = descuento + numeroDiasTomados;
							 
			        numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
				    numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

					/*
					 * Tomo los parametros creados para realizar los calculos de los
					 * dias pendientes calculo de vacaciones dependiendo si el empleado
					 * pertenece a la losep o al codigo de trabajo
					 */


					//List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);

					String periodoNuevo="";
					String periodoNuevoEntrada="",periodoNuevoSalida="";
					int valorDiasXAnio=0;
					List<Double[]> matriz = new ArrayList<Double[]>();

				
					TablaGenerica tabVacacionInicial=utilitario.consultar("select ide_asvac,ide_gtemp,fecha_ingreso_asvac,activo_asvac,fecha_finiquito_asvac  from asi_vacacion where ide_asvac="+ide_asvac);						
				    utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac="+ide_asvac);
					TablaGenerica tab_asi_vacacion_resumen=null;
					boolean banderaSalidaEmpleado=false;

					String parametro_dia_extra=utilitario.getVariable("p_asi_empleados_codigo_trabajo_dia_extra");
					String[] listaAccionesEmpleado;
					listaAccionesEmpleado=parametro_dia_extra.split(",");
					boolean empleado_dia_extra_vacacion=false;
					for (int i = 0; i < listaAccionesEmpleado.length; i++) {
						if(tabVacacionInicial.getValor("IDE_GTEMP").equals(listaAccionesEmpleado[i].toString())){
						empleado_dia_extra_vacacion=true;
						i=listaAccionesEmpleado.length;
						}else {
							empleado_dia_extra_vacacion=false;
						}
					}

					
					 
					///dias_pendientes = servicioVacaciones.getNumeroDiasPendientes(nde, nda, numeroDiasTomados, numeroDiasVacacionXAnio);
					utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac="+ide_asvac);
				    boolean fin_accion=false,fin_accion_continua=false;
					TablaGenerica acciones=retornaTablaAccionResumenVacacion(fecha_ingreso_continuidad,fecha_fin_continuidad,IDE_GTEMP);
					double dias_=0.0;
					dias_=(Math.rint(numeroDiasTomados * 100) / 100);
					double valor_retorno=0.0;
					int contador=0;
					for (int i = 0; i < acciones.getTotalFilas(); i++) {
						String fec_geedp="",fec_fin_geedp="";
						dias_=dias_-valor_retorno;
						if ((acciones.getTotalFilas()-1)==i) {
						if (acciones.getValor(i,"fecha_liquidacion_geedp")==null || acciones.getValor(i,"fecha_liquidacion_geedp").isEmpty()) {
							contador++;
							fec_geedp=acciones.getValor(i,"fecha_geedp");
							fec_fin_geedp=utilitario.getFechaActual();
							valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
							}else {
								contador++;
								fec_geedp=acciones.getValor(i,"fecha_geedp");
								fec_fin_geedp=acciones.getValor(i,"fecha_liquidacion_geedp");	
								valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
							}
						}else {
						fec_geedp=acciones.getValor(i,"fecha_geedp");
							if (acciones.getValor((i+1),"fecha_liquidacion_geedp")==null || acciones.getValor((i+1),"fecha_liquidacion_geedp").isEmpty()) {
								fec_geedp=acciones.getValor(i,"fecha_geedp");
								while(fec_geedp.equals(acciones.getValor((i+1),"fecha_geedp"))){
									i++;
									if (((i+1)==acciones.getTotalFilas())) {
										if(acciones.getValor((i),"fecha_liquidacion_geedp")==null || acciones.getValor((i),"fecha_liquidacion_geedp").isEmpty()){
											fin_accion=true;
											fin_accion_continua=true;
										}else{
											fin_accion=false;	
											fin_accion_continua=true;
										}
										break;
									}
								}
								contador++;
								if (fin_accion) {
								fec_fin_geedp=utilitario.getFechaActual();
								}else if (fin_accion==false && fin_accion_continua==true){
								fec_fin_geedp=acciones.getValor((i),"fecha_liquidacion_geedp");
								}else{
								fec_fin_geedp=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(acciones.getValor((i+1),"fecha_geedp")),-1));
								}
								valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
								}else {
									contador++;
									fec_geedp=acciones.getValor(i,"fecha_geedp");
									fec_fin_geedp=acciones.getValor((i+1),"fecha_liquidacion_geedp");
									valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
									i=i+1;
								}
						}
					}
//					Integer dimension = vacacionesPeriodo.size();
					//Variables para el calculo de dias extra
					Double total_dias_extra=0.00,dias_extra_temp=0.00;
					int valor_entero_dias_extra=0,valor_total_dias_extra=0;
					Double dias_pendientes_resumen=0.0,dias_acumulados=0.0;
					try {
						tab_asi_vacacion_resumen=utilitario.consultar(//"select IDE_ASVRE,IDE_ASVAC from asi_vacacion_resumen_empleado where activo_asvre=true"
								"select ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
								+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre, "
								+ "ide_periodo_asvre, usuario_ingre, fecha_ingre, usuario_actua, "                                        
								+ "fecha_actua, hora_ingre, hora_actua, base_imponible_asvre, dias_trabajados_liquidacion_asvre, "
								+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac, "
								+ "ide_geedp_asvac,dia_extra_asvre "
								+ "from asi_vacacion_resumen_empleado "
								+ "where activo_asvre=true"
								+ " and ide_asvac="+ide_asvac+" order by ide_asvre asc");
						if (tabVacacionInicial.getValor("ACTIVO_ASVAC").equals("true")) {
                               banderaSalidaEmpleado=false;
						}else{
							banderaSalidaEmpleado=true;
					}
					} catch (Exception e1) {
						System.out.println("ERROR CONSULTA RESUMEN VACACION EMPLEADO");
					}

					tab_asi_vacacion_resumen=utilitario.consultar("select IDE_ASVRE,IDE_ASVAC "
																  + "from asi_vacacion_resumen_empleado "
																  + "where activo_asvre is null"
																  + " and ide_asvac="+ide_asvac);
					
					if (tab_asi_vacacion_resumen.getTotalFilas()>0) {
						
					utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado SET "
														+ "activo_asvre=true where "
														+ "ide_asvac="+ide_asvac);
														}
						guardarPantalla();
						if (reporte==false) {
						 utilitario.addUpdate("dia_aplica_vacion");
						}
					
					/**
					 * Obtencion de dias pendientes del empleado
					 */
					// System.out.println("numero dias pendientes vacacion: "+
					// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
					
						
						double dias=0.0,dias_extra=0.0,vacaciones_extra=0.0;
						int dias_final_extra=0;
					    if (ide_gttem==1) {
					    	double dias_extra_=(sumatotal_vacaciones/5);
							dias_extra=dias_extra_;
							int diasE=pckUtilidades.CConversion.CInt(dias_extra);
						}
						
										
					TablaGenerica tab_resumen=utilitario.consultar("select a.nro_dias_vacacion_asvre,a.nro_dias_pendientes_asvre  "
							+ "from  "
							+ "(select ide_asvac, sum(nro_dias_vacacion_asvre) as nro_dias_vacacion_asvre,sum(nro_dias_pendientes_asvre) as nro_dias_pendientes_asvre  "
							+ "from asi_vacacion_resumen_empleado  "
							+ "where activo_asvre=true  "
							+ "and ide_asvac="+ide_asvac+"   "
							+ "group  by ide_asvac) a");
					
					
					dias_acumulados=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"));
			
					Double valor=0.00;
					
					
					
					if (ide_gttem==1) {
						//VALIDACION DIAS EXTRA CODIGO DE TRABAJO
						dias_extra_temp=(dias_acumulados/15);
						valor_entero_dias_extra=dias_extra_temp.intValue();
						//Integer.parseInt(""+dias_extra_temp)
						if(valor_entero_dias_extra>=5){
							valor_total_dias_extra=(valor_entero_dias_extra-5)+1;
						//}
						if ((int)valor_total_dias_extra>0) {
							TablaGenerica tab_resumen_vacaciones=utilitario.consultar("SELECT ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
									+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre,  "
									+ "ide_periodo_asvre, "
									+ "base_imponible_asvre, dias_trabajados_liquidacion_asvre,  "
									+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac,  "
									+ "ide_geedp_asvac  "
									+ "FROM asi_vacacion_resumen_empleado "
									+ "where activo_asvre=true "  
									+ "and ide_asvac="+ide_asvac+" "
									+ "order by ide_asvre asc");
							double sumatoria=0.00;
							int valor_acumulado=0,valor_actualizar_acumulado=0,valor_obtenido=0,valor_actualizar_acumulado_anterior=0;
					
							 
							for (int i = 0; i < tab_resumen_vacaciones.getTotalFilas(); i++) {
								sumatoria=Double.parseDouble(tab_resumen_vacaciones.getValor(i,"nro_dias_vacacion_asvre"))+sumatoria;
								valor_acumulado=(int)sumatoria/15;
								valor_obtenido=(valor_acumulado-5)+1-valor_obtenido;
								if (valor_obtenido>0) {
									if (i==0) {
										valor_actualizar_acumulado=valor_obtenido;
										utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre="+valor_obtenido+"  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));
									}else{
										valor_actualizar_acumulado=valor_obtenido-valor_actualizar_acumulado;
											//Actualizo valor generado
											//double valor_actualizar=0.00;
											utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre="+valor_obtenido+"  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));
											valor_actualizar_acumulado=valor_obtenido;
									}
								}else {
									valor_actualizar_acumulado=0;
									valor_obtenido=0;
									utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre=0.00  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));

								}
						}
							}
						
						}				
						}
				
							
					
					if (((int)valor_total_dias_extra)>1) {
						banderaCodigo="true";
					}else {
						banderaCodigo="false";
					}
					
					
					
					 dias_acumulados=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"))+(int)valor_total_dias_extra;
					dias_pendientes_resumen=dias_acumulados-numeroDiasTomados;
					
					/*if (dias_pendientes_resumen<0.00 || dias_pendientes_resumen<0) {
						TablaGenerica tab_resumen_vacaciones2=utilitario.consultar("SELECT ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
								+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre,  "
								+ "ide_periodo_asvre, "
								+ "base_imponible_asvre, dias_trabajados_liquidacion_asvre,  "
								+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac,  "
								+ "ide_geedp_asvac  "
								+ "FROM asi_vacacion_resumen_empleado "
								+ "where activo_asvre=true "  
								+ "and ide_asvac="+ide_asvac+" "
								+ "order by ide_asvre asc");
						
						int indice=0;
						indice=tab_resumen_vacaciones2.getTotalFilas()-1;
						utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado SET "
								+ "nro_dias_pendientes_asvre="+dias_pendientes_resumen+"  "
								+ "where "
								+ "ide_asvre="+tab_resumen_vacaciones2.getValor(indice,"ide_asvre"));
					}*/
					
					
					int numeroInicioFinesSemanaResumen = (int) (((dias_acumulados*4)+dias_final_extra) / 30);						
					//numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);
					int numeroInicioFinesSemanaPendientesResumen =0;
					numeroInicioFinesSemanaSolicitados=0;
					numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);

					numeroInicioFinesSemanaPendientesResumen = numeroInicioFinesSemanaResumen - numeroInicioFinesSemanaSolicitados;
					Double calculoPasaDiasPendientes=0.0;
					boolean banderaExcedeDias=false;
					if (ide_gttem == 2) {
						double nro_dias_ajuste_periodo_asvac = 0;						
								if (dias_pendientes_resumen > 60 || dias_pendientes_resumen > 60.00 ) {
								calculoPasaDiasPendientes = Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen,3))-60.00;
									if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0 ) {
										banderaExcedeDias=true;
						TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
					    utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
					    +"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACI�N DESCUENTO POR EXCESO DE DIAS' ,true, false,true)");
							//	utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
								//return;
									}else{
										banderaExcedeDias=false;	
									}
					    		}
							}

			if (ide_gttem == 1) {
								double nro_dias_ajuste_periodo_asvac = 0.0;
								if (dias_pendientes_resumen> 45 || dias_pendientes_resumen > 45.00 ) {
									calculoPasaDiasPendientes = Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen, 3))-45.00;
									if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0) {
										banderaExcedeDias=true;
									TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
									utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
									+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACI�N DESCUENTO POR EXCESO DE DIAS' ,true, false, true)");
									//return;
									}else{
										banderaExcedeDias=false;	
									}
								}
							}


					

					//Si es para reporte
					if (reporte==true) {
						if (banderaExcedeDias==true) {
							utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
							return;
						}
					try {
						
						
//						int numeroInicioFinesSemanaResumen = (int) 	gri_anular_dias_solicitados.getChildren().add(eti_dias_acumulados);
						
						
						p_parametros.put("IDE_GTEMP",Long.parseLong(aut_empleado.getValor()));
						p_parametros.put("titulo", "DETALLE DE VACACIONES POR EMPLEADO");
						//p_parametros.put("diasacumulados",""+utilitario.getFormatoNumero(sumatotal_vacaciones,2));
						p_parametros.put("diasacumulados",""+utilitario.getFormatoNumero(dias_acumulados,2));
						p_parametros.put("total",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));

						p_parametros.put("nrofinesdesemana",""+numeroInicioFinesSemanaResumen);
						p_parametros.put("diasdescontados",""+(Math.rint(numeroDiasTomados * 100) / 100));
						p_parametros.put("descuentofinesdesemana",""+numeroInicioFinesSemanaSolicitados);
						//p_parametros.put("totaldiaspendientes",""+(Math.rint((dias_pendientes) * 100) / 100));
						p_parametros.put("totaldiaspendientes",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));
						p_parametros.put("nrofinessemanapendientes",""+numeroInicioFinesSemanaPendientesResumen);
						p_parametros.put("ide_asvac",Integer.parseInt(ide_asvac));
						p_parametros.put("p_coordinador_tthh",utilitario.getVariable("p_gth_coordinador_tthh"));
						p_parametros.put("p_analista_tthh",utilitario.getVariable("p_gth_analista_tthh_vacaciones"));
						p_parametros.put("p_especialiista_tthh",utilitario.getVariable("p_especialiista_tthh"));
						p_parametros.put("dia_extra",(int)valor_total_dias_extra);
						p_parametros.put("fecha_ingreso_asvac",fecha_ingreso_continuidad);
						p_parametros.put("banderaCodigo",banderaCodigo);
						
						//	System.out.println("path "+rep_reporte.getPath());
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						sef_reporte.dibujar();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error en generacion de reporte");
					}
						
					}else {
						if (banderaExcedeDias==true) {
							utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
							return;
						}
							
						TablaGenerica tab_asi_vacacion_resumen_=utilitario.consultar(//"select IDE_ASVRE,IDE_ASVAC from asi_vacacion_resumen_empleado where activo_asvre=true"
								"select ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
								+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre, "
								+ "ide_periodo_asvre, usuario_ingre, fecha_ingre, usuario_actua, "                                        
								+ "fecha_actua, hora_ingre, hora_actua, base_imponible_asvre, dias_trabajados_liquidacion_asvre, "
								+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac, "
								+ "ide_geedp_asvac,dia_extra_asvre "
								+ "from asi_vacacion_resumen_empleado "
								+ "where activo_asvre=true"
								+ " and ide_asvac="+ide_asvac+" order by ide_asvre asc");
						
						if (ide_gttem==1) {
							for (int j = 0; j < tab_asi_vacacion_resumen_.getTotalFilas(); j++) {
								
						periodo = new Etiqueta("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+(tab_asi_vacacion_resumen_.getValor(j,"ide_periodo_asvre"))+" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | "
						+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+(tab_asi_vacacion_resumen_.getValor(j,"nro_dias_vacacion_asvre"))+ "   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ " |   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   "+(tab_asi_vacacion_resumen_.getValor(j,"nro_dias_tomados_asvre"))+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+tab_asi_vacacion_resumen_.getValor(j,"nro_dias_pendientes_asvre")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+(tab_asi_vacacion_resumen_.getValor(j,"dia_extra_asvre"))+" "
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp ");	
						gri_resumen_vacaciones_periodo.getChildren().add(periodo);
						gri_resumen_vacaciones_periodo.setStyle("font-size:10px;border:2px");}
								
							} else {
								for (int j = 0; j < tab_asi_vacacion_resumen_.getTotalFilas(); j++) {
									
									periodo = new Etiqueta("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+(tab_asi_vacacion_resumen_.getValor(j,"ide_periodo_asvre"))+" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | "
									+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+(tab_asi_vacacion_resumen_.getValor(j,"nro_dias_vacacion_asvre"))+ "   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+ " |   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   "+(tab_asi_vacacion_resumen_.getValor(j,"nro_dias_tomados_asvre"))+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+tab_asi_vacacion_resumen_.getValor(j,"nro_dias_pendientes_asvre")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+0.00+" "
									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp ");	
									gri_resumen_vacaciones_periodo.getChildren().add(periodo);
									gri_resumen_vacaciones_periodo.setStyle("font-size:10px;border:2px");
						
								
								}
								
								}
						
					etiDiasAcumulados.setValue(utilitario.getFormatoNumero(dias_acumulados,2));
					etiNroFinesSemana.setValue(numeroInicioFinesSemanaResumen);
				    etiDiasDescontados.setValue(utilitario.getFormatoNumero(numeroDiasTomados,2));
					etiDescuentoFinesSemana.setValue(numeroInicioFinesSemanaSolicitados);
					//etiDiasPendientes.setValue(utilitario.getFormatoNumero(dias_pendientes_resumen,2));
					etiDiasPendientes.setValue((Math.rint((dias_pendientes_resumen) * 100) / 100));
					etiNroFinesSemanaPendientes.setValue(numeroInicioFinesSemanaPendientesResumen);

					Etiqueta eti_dias_acumulados=new Etiqueta("Dias Acumulados: ");
					Etiqueta eti_nro_fines_semana=new Etiqueta("Nro Fines de Semana: ");
					Etiqueta eti_dias_solicitados=new Etiqueta("Dias Descontados: ");
					Etiqueta eti_descuento_fines_semana=new Etiqueta("Descuento Fines de Semana: ");
					Etiqueta eti_dias_pendientes=new Etiqueta("Total de Dias Pendientes: ");
					Etiqueta eti_nro_fines_semana_pendientes=new Etiqueta("Nro Fines de Semana Pendientes: ");
					gri_anular_dias_solicitados.getChildren().add(eti_dias_acumulados);
					gri_anular_dias_solicitados.getChildren().add(etiDiasAcumulados);
					gri_anular_dias_solicitados.getChildren().add(eti_nro_fines_semana);
					gri_anular_dias_solicitados.getChildren().add(etiNroFinesSemana);
					gri_anular_dias_solicitados.getChildren().add(eti_dias_solicitados);
					gri_anular_dias_solicitados.getChildren().add(etiDiasDescontados);
					gri_anular_dias_solicitados.getChildren().add(eti_descuento_fines_semana);
					gri_anular_dias_solicitados.getChildren().add(etiDescuentoFinesSemana);
					gri_anular_dias_solicitados.getChildren().add(eti_dias_pendientes);
					gri_anular_dias_solicitados.getChildren().add(etiDiasPendientes);
					gri_anular_dias_solicitados.getChildren().add(eti_nro_fines_semana_pendientes);
					gri_anular_dias_solicitados.getChildren().add(etiNroFinesSemanaPendientes);	

					Etiqueta eti_dias_extra=new Etiqueta("Dias Acumulados: ");
					eti_dias_extra.setStyle("font-size:12px;color:red;font-weight: bold;text-align:center;");
					eti_dias_extra.setValue("Nro dias Adicionales");
					gri_anular_dias_solicitados.getChildren().add(eti_dias_extra);
					gri_anular_dias_solicitados.getChildren().add(new Etiqueta(""+(int)valor_total_dias_extra));
					Etiqueta eti_dias_extra_blanco=new Etiqueta(" ");
					Etiqueta eti_dias_extra_blanco1=new Etiqueta(" ");

					Etiqueta eti_dias_extra_total=new Etiqueta("TOTAL");

					gri_anular_dias_solicitados.getChildren().add(eti_dias_extra_blanco);
					gri_anular_dias_solicitados.getChildren().add(eti_dias_extra_blanco1);
					gri_anular_dias_solicitados.getChildren().add(eti_dias_extra_total);
					//gri_anular_dias_solicitados.getChildren().add(new Etiqueta(""+(utilitario.getFormatoNumero((dias_pendientes_resumen),2))));
					gri_anular_dias_solicitados.getChildren().add(new Etiqueta(""+Math.rint((dias_pendientes_resumen) * 100) / 100));
					//etiDiasPendientes.setValue((Math.rint((dias_pendientes_resumen) * 100) / 100));

					
					dia_aplica_vacion.dibujar();		
					}				
					utilitario.addUpdate("dia_aplica_vacion");
					}
				
				
			
	   public Tabla getTab_tabla_resumen_vacacion_empleado() {
		return tab_tabla_resumen_vacacion_empleado;
	}
	
	public void setTab_tabla_resumen_vacacion_empleado(
			Tabla tab_tabla_resumen_vacacion_empleado) {
		this.tab_tabla_resumen_vacacion_empleado = tab_tabla_resumen_vacacion_empleado;
	}
	
	   public TablaGenerica getPartida(String IDE_GTEMP){
	        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GTEMP="+IDE_GTEMP+" ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
	        
	        
///METODO PARA SABER LA ACCION DEL EMPLEADO 	        
	        
	        /*String cambioContratoPorEmpleado="";
			//tabla gen_accion_motivo_empleado  
			TablaGenerica tab_accion_motivo_empleado=null;
			//for (int i = tab_empleado_departamento_par.getTotalFilas()-1; i < tab_empleado_departamento_par.getTotalFilas(); i++) {
				cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor((Integer)(tab_empleado_departamento_par.getTotalFilas()-1),"IDE_GEDED");
				//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
				tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
						+ "FROM gen_detalle_empleado_departame "
						+ "where ide_geded  in("+cambioContratoPorEmpleado+") ");

				//Validacion de que tenga terminacion de contrato
				String motivoContratoPorEmpleado="";
				boolean estadoTerminacionContrato=false;
			//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
					motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
					if (motivoContratoPorEmpleado.equals("19")) {
						estadoTerminacionContrato=true;
					}*/

	        
	    }
	
	 //CALCULO DE NUMERO DE DIAS 
	    
		/**
		 * calcular numero de dias calendario que el empleado lleva en la empresa
		 * @param fechaIngresoEmpleado
		 * @return
		 */
	    public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC,String fecha_ingreso_asvac,String fecha_finiquito_asvac){
		
	    //	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
	    //			+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TablaGenerica tab_partida = getPartida(IDE_GEEDP);

		       
	    	
			
	       String dateInString = "";
	       boolean activo_asvac=false;
		      int sumaValor=0;
	       if (fecha_ingreso_asvac==null || fecha_ingreso_asvac.equals("") || fecha_ingreso_asvac.isEmpty()) {
	    	   return 0;
	       }else {
	    	   dateInString=fecha_ingreso_asvac;
	       }
	       
	      
	       String fecha_finiquito=null;
	       
	       	 int diaFin=0;
	    		 int mesFin=0; // 0 Enero, 11 Diciembre
	    		 int anioFin=0;
		        
	    
	        
	   		Date date = null;
	   		Date dateFechaCalculoFiniquito = null;
	    		/**
	    		 * Le asigno a una variable de tipo date la fecha de ingreso
	    		 */
	    			
	    /*		try {
	    			date = sdf.parse(dateInString);
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		
	    	*/	
	    		
	   			
	       	 try {
				if (fecha_finiquito_asvac==null || fecha_finiquito_asvac.isEmpty()){
				   		try {
				    			date = sdf.parse(dateInString);
				   		       } catch (ParseException e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
		        		
		        		Calendar calendarFin = Calendar.getInstance();
		        		calendarFin.setTime(utilitario.getDate());
		        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		        		 anioFin = calendarFin.get(Calendar.YEAR);
				activo_asvac=true;
				
				}else{
				if (activo_asvac==false){
					   fecha_finiquito = fecha_finiquito_asvac;
					   
					  if (utilitario.getDia(fecha_finiquito)==31) {
						   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), -1));
											
					   }
					   
					   if (utilitario.getMes(fecha_finiquito)==2 || utilitario.getMes(fecha_finiquito)==02) {
						
				
					   if (utilitario.getDia(fecha_finiquito)==29) {
						   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
											sumaValor=1;
					   }
					   
					   
					   if (utilitario.getDia(fecha_finiquito)==28) {
						//fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
											sumaValor=2;
					   }
					   
						}
	    		try {
	    			date = sdf.parse(dateInString);
							dateFechaCalculoFiniquito = sdf.parse(fecha_finiquito);
					   	
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			//e.printStackTrace();
	    			System.out.println();
	    		}
	    		

						Calendar calendarFin = Calendar.getInstance();
	        			calendarFin.setTime(dateFechaCalculoFiniquito);
	        			 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	            		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	            		 anioFin = calendarFin.get(Calendar.YEAR);
					
					
				}else if (activo_asvac==true){
					try {
		    			date = sdf.parse(dateInString);
		   		//		dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
		    	   	
		   		       } catch (ParseException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		       		
		   		
				
				
        		
        		Calendar calendarFin = Calendar.getInstance();
        		calendarFin.setTime(utilitario.getDate());
        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        		 anioFin = calendarFin.get(Calendar.YEAR);
					
					
				}else {
					utilitario.agregarMensajeError("No contiene Vacaciones", "No se pudo encontrar un registro para el empleado");
				}
				
					
				}	
					
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       		
	
			
	    		
	
	    		Calendar calendarInicio = Calendar.getInstance();
        		calendarInicio.setTime(date);
        		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
        		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        		int anioInicio = calendarInicio.get(Calendar.YEAR);
	    		
        		int anios = 0;
        		int mesesPorAnio = 0;
        		int diasPorMes = 0;
        		int diasTipoMes = 30;

        			if (mesInicio <= mesFin) {
        				anios = anioFin - anioInicio;
        				if (diaInicio <= diaFin) {
        					mesesPorAnio = mesFin - mesInicio;
        					diasPorMes = diaFin - diaInicio;
        				} else {
        					if (mesFin == mesInicio) {
        						anios = anios - 1;
        					}
        					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
        				}
			    	}else{
        				anios = anioFin - anioInicio - 1;
        				//System.out.println(anios);
        				if (diaInicio > diaFin) {
        					mesesPorAnio = mesFin - mesInicio - 1 + 12;
        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
        				} else {
        					mesesPorAnio = mesFin - mesInicio + 12;
        					diasPorMes = diaFin - diaInicio;
			    	}
				}
        		
               			
               		//System.out.println("Han transcurrido " + anios + " A�os, " + mesesPorAnio + " Meses y " + diasPorMes + " D�as.");

               		//System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
               	 

        	 
        		int returnValue = -1;
        				returnValue = anios * 12 + mesesPorAnio;
               				//System.out.println("Total meses: " + returnValue + " Meses.");

        				if (activo_asvac==false){
        					returnValue =(((returnValue*30)+diasPorMes))+1+sumaValor;
               				}else{
               					returnValue =(((returnValue*30)+diasPorMes))+1;
               				}
               				//System.out.println("Total dias: " + returnValue);
        				 return returnValue;
	    		 
	    		


	    }
	    
	    //CALCULO DE NUMERO DE DIAS 
	      
	    	/**
	    	 * calcula el numero de dias calendario que el empleado lleva en la empresa desde la fecha ingreso hasta el 30 de abril del 2017
	    	 * @param fechaIngresoEmpleado
	    	 * @return
	    	 */
	        public Integer getNumeroDiasEmpleadoCalculoInicial(String IDE_GEEDP,String IDE_ASVAC){
	           //TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GEEDP=" + IDE_GEEDP + ")");        
		    	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac "
		    			+ "from asi_vacacion where  IDE_ASVAC="+IDE_ASVAC);        

	           
	           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		       String dateInString = tab_periodo.getValor("fecha_ingreso_asvac");
	      		String dateInStringFechaCalculoInicial="2017-05-30";

	      		Date date = null;
	        	//Fecha calculo de vacaciones hasta el 30 de abril
	       		Date dateFechaCalculoInicial=null;	
	        		/**
	        		 * Le asigno a una variable de tipo date la fecha de ingreso
	        		 */
	        			
	        		try {
	        			date = sdf.parse(dateInString);
	        			dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
	        		} catch (ParseException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}

		    		
	        		Calendar calendarInicio = Calendar.getInstance();
	        		calendarInicio.setTime(date);
	        		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
	        		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		int anioInicio = calendarInicio.get(Calendar.YEAR);
	        		
	        		
	        		// Fecha fin
	        		Calendar calendarFin = Calendar.getInstance();
	        		calendarFin.setTime(dateFechaCalculoInicial);
	        		int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	        		int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		int anioFin = calendarFin.get(Calendar.YEAR);
	        		
	        		
				    
	        		int anios = 0;
	        		int mesesPorAnio = 0;
	        		int diasPorMes = 0;
	        		int diasTipoMes = 0;
	        		diasTipoMes=30;
	        		// Calculo de d�as del mes

		if (mesInicio <= mesFin) {
	        				anios = anioFin - anioInicio;
	        				if (diaInicio <= diaFin) {
	        					mesesPorAnio = mesFin - mesInicio;
	        					diasPorMes = diaFin - diaInicio;
				    	}else{
	        					if (mesFin == mesInicio) {
	        						anios = anios - 1;
	        					}
	        					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				}
	        			} else {
	        				anios = anioFin - anioInicio - 1;
	        				if (diaInicio > diaFin) {
	        					mesesPorAnio = mesFin - mesInicio - 1 + 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				} else {
	        					mesesPorAnio = mesFin - mesInicio + 12;
	        					diasPorMes = diaFin - diaInicio;
				    	}
					}
	        //		System.out.println("Han transcurrido " + anios + " A�os, " + mesesPorAnio + " Meses y " + diasPorMes + " D�as.");		
	       // 		System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
	        	 
	        		int returnValue = -1;
	        				returnValue = anios * 12 + mesesPorAnio;
	       				//System.out.println("Total meses anteriores: " + returnValue + " Meses.");
	        				returnValue =(((returnValue*30)+diasPorMes)+1);
	        				 //System.out.println("Total dias anteriore: " + returnValue);

	        				 return returnValue;
		    		
	        }
	        
	        
	        

	    	public int getDiasEmpleadoXAnio(int nde, int nda) {

	    		if (nde < 360) {
	    			return nde;
	    		}
	    		int nuevo_calculo = nde - nda;
	    		if (nuevo_calculo >= 1 && nuevo_calculo <= 5) {
	    			nde = nda;
	    			return nde;
	    		} else {
	    			BigDecimal nuevo_valor_nde = new BigDecimal(nde / nda);
	    			if (nuevo_valor_nde.intValue() >= 1) {
	    				int valor_calcula = nuevo_valor_nde.intValue() * 5;
	    				nde = nde - valor_calcula;
	    				return nde;
	    			}

	    		}
	    		return nde;

	    	}     
	        
	        
	    	   //PERIODOS DE EMPLEADO DE ACUERDO A LA FECHA DE INGRESO 
	    		    
	    			public static List<Integer> getPeriodos(Integer nda, Integer nde) {
	    				List<Integer> peridos = new ArrayList<Integer>();
	    				Integer indicePeriodo = 1;
	    				Integer ndeAux = nde;
	    				do {
	    					ndeAux = ndeAux - nda;
	    					peridos.add(indicePeriodo);
	    					indicePeriodo++;
	    				}while(ndeAux > 0);
	    				return peridos;
	    			}
	    			
	    			
	    		    /**
	    		     * Metodo para obtener al grupo que pertenece el empleado 1: Codigo de trabajo, 2: Losep, 3:Pasante
	    		     * @param IDE_GEEDP
	    		     * @return
	    		     */

	    		   public Integer getGrupoTipoEmpleado(String IDE_GTEMP,String ide_asvac){
	    			   Integer ide_gttem=0;
	    			   TablaGenerica tab_tipo_empleado=null;
	    			   boolean estadoTerminacion=false;
	    			   boolean fechaEntradaEmpleado=false,fechaSalidaEmpleado=false;
		 			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,IDE_GTEMP,FECHA_INGRESO_ASVAC,FECHA_FINIQUITO_ASVAC, "
		 			    		+ "ACTIVO_ASVAC"
		 			    		+ " FROM ASI_VACACION WHERE IDE_ASVAC=" + ide_asvac+ " ");  
		 			    
		 			    if (tab_periodo.getValor("ACTIVO_ASVAC").equals("false")) {
							String fecha_ingreso_asvac="",fecha_finiquito_asvac="";
		 			    	if ((tab_periodo.getValor("FECHA_INGRESO_ASVAC")==null) || (tab_periodo.getValor("FECHA_INGRESO_ASVAC").equals("") || (tab_periodo.getValor("FECHA_INGRESO_ASVAC").isEmpty()))) {
		 			    		fechaEntradaEmpleado=false;
							}else {
								fechaEntradaEmpleado=true;
							}
		 			    	if ((tab_periodo.getValor("FECHA_FINIQUITO_ASVAC")==null) || (tab_periodo.getValor("FECHA_FINIQUITO_ASVAC").equals("") || (tab_periodo.getValor("FECHA_FINIQUITO_ASVAC").isEmpty()))) {
		 			    		fechaSalidaEmpleado=false;
							}else {
								fechaSalidaEmpleado=true;
							}
		 			    	
		 			    	
		 			    	if (fechaEntradaEmpleado==true && fechaSalidaEmpleado==true) {
		 			    		fecha_ingreso_asvac=tab_periodo.getValor("FECHA_INGRESO_ASVAC");
								fecha_finiquito_asvac=tab_periodo.getValor("FECHA_FINIQUITO_ASVAC");

								TablaGenerica tab_empleado_departamento_par=null;
								tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,ide_geded,ide_gttem  "
										+ "from gen_empleados_departamento_par  "
										+ "where ide_gtemp="+IDE_GTEMP+" "
												+ "order by ide_geedp DESC ");
												//+ "limit 1");
								if (tab_empleado_departamento_par.getTotalFilas()==0) {
									return 0;
								}
								String cambioContratoPorEmpleado="";
								//tabla gen_accion_motivo_empleado  
								TablaGenerica tab_accion_motivo_empleado=null;
								//for (int i = tab_empleado_departamento_par.getTotal
								for (int i = 0; i < tab_empleado_departamento_par.getTotalFilas(); i++) {
							
								
								cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(i,"IDE_GEDED");
								//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
								tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
										+ "FROM gen_detalle_empleado_departame "
										+ "where ide_geded  in("+cambioContratoPorEmpleado+") ORDER BY IDE_GEDED DESC ");
								String motivoContratoPorEmpleado="";
								boolean estadoTerminacionContrato=false;
							//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
									motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
									
									
								TablaGenerica tabTerminacionContrato=null;
								 tabTerminacionContrato= utilitario.consultar("SELECT geame.ide_geame, geame.ide_geaed, geame.ide_gemed,  "
										+ "geame.activo_geame,geame.anterior_geame,geaed.detalle_geaed  "
										+ "FROM  gen_accion_motivo_empleado geame "
										+ "left join gen_accion_empleado_depa  geaed on geaed.ide_geaed=geame.ide_geaed  "
										+ "where geaed.finiquito_contrato_geaed=true");
									
								
								for (int j = 0; j < tabTerminacionContrato.getTotalFilas(); j++) {
									
									if (tabTerminacionContrato.getValor(j, "IDE_GEAME").equals(motivoContratoPorEmpleado)) {
										estadoTerminacion=true;
										j=tabTerminacionContrato.getTotalFilas();
									}else {
										estadoTerminacion=false;
									}
								}
								
								
								
							//		if (motivoContratoPorEmpleado.equals("19") || motivoContratoPorEmpleado.equals("6")) {
								if (estadoTerminacion==true) {
										tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +" "
						    			   		+ " and IDE_GEDED ="+tab_empleado_departamento_par.getValor(i,"IDE_GEDED")+" "
						    			   		+ " ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
										i=tab_empleado_departamento_par.getTotalFilas();
					    		        ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      

										}else {
										//tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +" "
						    			//   		+ " and fecha_geedp='"+fecha_ingreso_asvac+"' "
						    			//   		+ " ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
						    		        ide_gttem= Integer.parseInt(tab_empleado_departamento_par.getValor(i,"ide_gttem"));      

										}
								
								
					    
							}
								
								
							}else {
								return 10;
								
								
							}
						}else {
			    			   tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +"  "
			    			   		+ "AND ACTIVO_GEEDP=TRUE "
			    			   		+ "ORDER BY IDE_GEEDP DESC  LIMIT 1 ");   
			    			   //tab_tipo_empleado.imprimirSql();
			    		        ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      


						}
		 			    

	    			   
	    		        //ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      
	    		       return ide_gttem;
	    		   }
	    		    
	    		    
	    		 //VACACIONES POR PERIODOS
	    		 		public static List<Double> getVacacionesXPeriodo(Integer nda, Integer nde, List<Integer> peridos, double numeroDiasVacacionXAnio) {
	    		 			List<Double> vaxacionXPeriodo = new ArrayList<Double>();
	    		 			Integer ndeAux = nde;
	    		 			for (Integer periodo : peridos) {
	    		 				if (ndeAux >= nda){
	    		 					ndeAux = nde - (periodo * nda);
	    		 					vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
	    		 				}else{
	    		 				//	System.out.println("calculo final: "+ndeAux);
	    		 					double numdiasUltimoperiodo = (ndeAux * numeroDiasVacacionXAnio) / nda;
	    		 					vaxacionXPeriodo.add(numdiasUltimoperiodo);
	    		 				}
	    		 			}
	    		 			return vaxacionXPeriodo;
	    		 		}
	    		 		
	    		 		
	    		 		 /**
	    		 	    * Metodo extrae el numero de dias pendientes para el calculo de dias tomados hasta el 30 de abril obtenidos del excel
	    		 	    * @param IDE_GEEDP
	    		 	    * @return
	    		 	    */
	    		 	   
	    		 	            double valorIncialDiasTomados;
	    		 		  public double getNumeroDiasPendientesInicial(String IDE_GEEDP,String ide_asvac) {
	    		 			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
	    		 		        if (!tab_periodo.isEmpty()) {
	    		 	         	                             /*TablaGenerica tab_valor_InicialDiasTomados=utilitario.consultar("select ide_gtemp,dias_tomados_asvac "
	    		 	         	                             		+ "from asi_vacacion where IDE_GTEMP="+IDE_GEEDP+" and IDE_ASVAC="+ide_asvac);*/
	    		 	         	                   		TablaGenerica tab_valor_InicialDiasTomados = utilitario.consultar("select ide_gtemp,sum(dias_tomados_asvac) as dias_tomados_asvac  " + " "
	    		 	         	          					+ "from asi_vacacion  "
	    		 	         	          					+ "where IDE_ASVAC in("+ide_asvac+") and IDE_GTEMP="+IDE_GEEDP+" "
	    		 	         	          					+ "group by ide_gtemp");
	    		 	         	          	
	    		 		       	if (tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac")==null || tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac").isEmpty())
	    		 		       	{
	    		 		       	valorIncialDiasTomados=0.0;
	    		 	    		}else {
	    		 	       		valorIncialDiasTomados=Double.parseDouble(tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac"));
	    		 	    		}
	    		 		       //	System.out.println(valorIncialDiasTomados);
	    		 		        return valorIncialDiasTomados;	
	    		 		        }

	    		 		        return 0;
	    		 	    		
	    		 		  }
	    		 		  
	    		 		  
	    		 		   /*
	    		 		    * Metodo devuelve los dias pendientes calculados a partir del excel de cada empleado
	    		 		    */
	    		 		   
	    		 		   public double getNumeroDiasPendientesInicialAjuste(String IDE_GEEDP,String ide_asvac) {     
	    		 			   double numeroDiasTomadoss=0.0;
	    		 			   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 		        if (!tab_periodo.isEmpty()) {
	    		 			    	   /*TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_SOLICITADO "
	    		 		        		+ "FROM ( "
	    		 		        		+ "SELECT IDE_ASVAC,(case when sum(DIA_SOLICITADO_ASDEV) is null then 0 else sum(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO "
	    		 		        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC "
	    		 		        	    + " ) a where IDE_ASVAC="+ ide_asvac);*/

	    		 			  		TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_SOLICITADO_ASDEV) as DIA_SOLICITADO "
	    		 			  				+ "from asi_vacacion vac "
	    		 			  				+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    		 			  				+ "where vac.ide_asvac in("+ide_asvac+") "
	    		 			  				+ "group by vac.ide_gtemp");
	    		 			    	   if (tab_numero_tomados.getValor("DIA_SOLICITADO")==null || tab_numero_tomados.getValor("DIA_SOLICITADO").isEmpty())
	    		 			    	   {
	    		 			    		   numeroDiasTomadoss=0.0;
	    		 	      		       }else {
	    		 	      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_SOLICITADO"));	
	    		 	      		       
	    		 	      		       }
	    		 	         		   return  numeroDiasTomadoss;

	    		 		               }
	    		 		      return  0;
	    		 		        
	    		 		    }
	    		 		    

	    		 		  /*
	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	    		 		   */
	    		 		  	   public double getNumeroDiasTomados(String IDE_GEEDP, String ide_asvac) {     
	    		 		  		   double numeroDiasTomadoss=0.0;
	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 		  	        if (!tab_periodo.isEmpty()) {
	    		 		  		    	   /*TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
	    		 		  	        		+ "FROM ( "
	    		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
	    		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
	    		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=false GROUP BY IDE_ASVAC "
	    		 		  	        	    + " ) a where IDE_ASVAC="+ ide_asvac);*/
	    		 		  				TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
	    		 		  						+ "from asi_vacacion vac "
	    		 		  						+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    		 		  						+ "where vac.ide_asvac in("+ide_asvac+") and dev.fin_semana_asdev=false "
	    		 		  						+ "group by vac.ide_gtemp");
	    		 		  		    	   
	    		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
	    		 		  		    	   {
	    		 		  		    		   numeroDiasTomadoss=0.0;
	    		 		        		       }else {
	    		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
	    		 		        		       
	    		 		        		       }
	    		 		           		   return  numeroDiasTomadoss;

	    		 		  	               }
	    		 		  	      return  0;
	    		 		  	        
	    		 		  	    }
	    		 		  	          

 
	    		 		  	   
	 	    		 		  /*
	 	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	 	    		 		   */
	 	    		 		  	   public double getNumeroDiasTomadosFinSemana(String IDE_GEEDP,String ide_asvac) {     
	 	    		 		  		   double numeroDiasTomadoss=0.0;
	 	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_ASVAC in(" + ide_asvac + ") ");        
	 	    		 		  	        if (!tab_periodo.isEmpty()) {
	 	    		 		  		    	   /*TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
	 	    		 		  	        		+ "FROM ( "
	 	    		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
	 	    		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
	 	    		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=true GROUP BY IDE_ASVAC "
	 	    		 		  	        	    + " ) a where IDE_ASVAC="+ ide_asvac);*/
	 	    		 		  		   	TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
	 	    		 							+ "from asi_vacacion vac "
	 	    		 							+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	 	    		 							+ "where vac.ide_asvac in("+ide_asvac+") and  dev.fin_semana_asdev=true "
	 	    		 							+ "group by vac.ide_gtemp");
	 	    		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
	 	    		 		  		    	   {
	 	    		 		  		    		   numeroDiasTomadoss=0.0;
	 	    		 		        		       }else {
	 	    		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
	 	    		 		        		       
	 	    		 		        		       }
	 	    		 		           		   return  numeroDiasTomadoss;

	 	    		 		  	               }
	 	    		 		  	      return  0;
	 	    		 		  	        
	 	    		 		  	    }
	    		 		  	   

	 //Metodo Descuento Fines de Semana
	 	    		 		  	   
	 	 	    		 		  /*
	 	 	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	 	 	    		 		   */


	    		 		  	  /**
	    		 		  	   * Metodo devuelve el numero de dias para el nuevo calculo de dias tomados a vacacion 
	    		 		  	   * @param IDE_GEEDP
	    		 		  	   * @return
	    		 		  	   */
	    		 		  		  public double getNumeroDiasAjusteEmpleado(String IDE_GEEDP,String ide_asvac) {
	    		 		  	          double valorNumeroDiasAjusteEmpleado;
	    		 		  			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
	    		 		  		        if (!tab_periodo.isEmpty()) {
	    		 		  		        	
	    		 		  	       	                            /* TablaGenerica tabvalorNumeroDiasAjusteEmpleado=utilitario.consultar("select ide_gtemp,nro_dias_ajuste_asvac "
	    		 		  	       	                             		+ "from asi_vacacion where IDE_ASVAC=" +ide_asvac);*/
	    		 		  	       	                             
	    		 		  	       	                  		TablaGenerica tabvalorNumeroDiasAjusteEmpleado = utilitario.consultar("select ide_gtemp,sum(nro_dias_ajuste_asvac) as nro_dias_ajuste_asvac  " + " "
	    	    		 	         	          					+ "from asi_vacacion  "
	    	    		 	         	          					+ "where IDE_ASVAC in("+ide_asvac+") and IDE_GTEMP="+IDE_GEEDP+" "
	    	    		 	         	          					+ "group by ide_gtemp");
	    	    		 	         	          	     
	    		 		  		       	if (tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac")==null || tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac").isEmpty())
	    		 		  		       	{
	    		 		  		       		valorNumeroDiasAjusteEmpleado=0.0;
	    		 		  	  		}else {
	    		 		  	  			valorNumeroDiasAjusteEmpleado=Double.parseDouble(tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac"));
	    		 		  	  		}
	    		 		  		        return valorNumeroDiasAjusteEmpleado;	
	    		 		  		        }

	    		 		  		        return 0;
	    		 		  	  		
	    		 		  		  }
	    		 		  		  
	    		 		  		  
	    		 		  		  
	    		 		  		  /**
	    		 		  		   * Metodo devuelve el numero de dias de ajuste para cuando se pasa de 60
	    		 		  		   * @param IDE_GEEDP
	    		 		  		   * @return
	    		 		  		   */
	    		 		  			  public double nroDiasAjustePeriodo(String IDE_GEEDP, String ide_asvac) {
	    		 		  		       
	    		 		  			   double numeroDiasTomadoss=0.0;
	    		 					   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 				        if (!tab_periodo.isEmpty()) {
	    		 					       	TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.dia_acumulado_asdev) as DIA_ACUMULADO "
	    		 				   			+ "from asi_vacacion vac "
	    		 				   			+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    		 				   			+ "where vac.ide_asvac in("+ide_asvac+") "
	    		 				   			+ "group by vac.ide_gtemp");

	    		 					    	   if (tab_numero_tomados.getValor("DIA_ACUMULADO")==null || tab_numero_tomados.getValor("DIA_ACUMULADO").isEmpty())
	    		 					    	   {
	    		 					    		   numeroDiasTomadoss=0.0;
	    		 			      		       }else {
	    		 			      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_ACUMULADO"));	
	    		 						  //     System.out.println("cambio de valores"+numeroDiasTomadoss);
	    		 			      		       
	    		 			      		       }
	    		 			         		   return  numeroDiasTomadoss;

	    		 				               }
	    		 				      return  0;
	    		 		  				  
	    		 		  			  }
	    		 		  		   
	    		 		  	   
	    		 		  	   

	    		 		  			
	    		 		  			//DIAS TOMADOS POR VACACION
	    		 		  					
	    		 		  					public static List<Double> getVacacionesTomadasXPeriodo(Double numeroDiasTomados, List<Integer> peridos, Double numeroDiasVacacionXAnio) {
	    		 		  						List<Double> vaxacionXPeriodo = new ArrayList<Double>();
	    		 		  						double ndeAux = numeroDiasTomados;
	    		 		  						int valorVacacionesPeriodo=0;
	    		 		  						for (Integer periodo : peridos) {
	    		 		  							valorVacacionesPeriodo++;
	    		 		  							if (valorVacacionesPeriodo==peridos.size()) {
	    		 		  								if (ndeAux >= 0){
		    		 		  							vaxacionXPeriodo.add(ndeAux);
	    		 		  								}
	    		 		  								else{
	    		 		  									vaxacionXPeriodo.add(0.0);
	    		 		  								}
	    											}else {
	    		 		  							if (ndeAux >= numeroDiasVacacionXAnio){
	    		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
	    		 		  								vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
	    		 		  							}else{
	    		 		  								if (ndeAux >= 0){
	    		 		  								vaxacionXPeriodo.add(ndeAux);
	    		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
	    		 		  								}else{
	    		 		  									vaxacionXPeriodo.add(0.0);
	    		 		  								}
	    		 		  							}
	    		 		  						}
	    		 		  						}
	    		 		  						return vaxacionXPeriodo;
	    		 		  					}
	    		 		  					
	    		 		  					
	    		 		  					
	    		 		  					
	    		 		  					/**
	    		 		  					 * Metodo que crea la 		
	    		 		  					 * @param vacacionesPeriodo
	    		 		  					 * @param vacacionesTomadas
	    		 		  					 * @return
	    		 		  					 */
	    		 		  							
	    		 		  							public static List<Double[]> getVacacionesMatriz(List<Double> vacacionesPeriodo, List<Double> vacacionesTomadas) {
	    		 		  								List<Double[]> matriz = new ArrayList<Double[]>();
	    		 		  								
	    		 		  								Integer dimension = vacacionesPeriodo.size();
	    		 		  								for (Integer i = 0; i < dimension; i++) {
	    		 		  									Double[] obj = new Double[5];
	    		 		  									obj[0] = i.doubleValue() + 1; // periodo
	    		 		  									obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
	    		 		  									//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
	    		 		  									obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
	    		 		  								//	obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
	    		 		  									obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
	    		 		  								//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
	    		 		  									obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
	    		 		  								//	obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
	    		 		  									matriz.add(obj);
	    		 		  								}
	    		 		  								return matriz;
	    		 		  							}
	    		 		  							
	    		 		  							/**
	    		 		  							 * 		
	    		 		  							 * @param fechaIngresoEmpleado
	    		 		  							 * @return
	    		 		  							 */

	    		 		  									public  Double getNumeroDiasPendientes(int nde, int nda, double numeroDiasTomados,double numeroDiasVacacionXAnio){

	    		 		  										List<Integer> peridos = getPeriodos(nda, nde);
	    		 		  										List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
	    		 		  										List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos, numeroDiasVacacionXAnio);

	    		 		  										List<Double[]> matriz = getVacacionesMatriz(vacacionesPeriodo, vacacionesTomadas);
	    		 		  										Double diasPendientes = 0.0;
	    		 		  										for (Double[] vacaciones : matriz) {
	    		 		  											diasPendientes = diasPendientes + vacaciones[3];	
	    		 		  										
	    		 		  										}
	    		 		  										return diasPendientes;
}

													public Dialogo getDia_aplica_vacion() {
														return dia_aplica_vacion;
													}

													public void setDia_aplica_vacion(Dialogo dia_aplica_vacion) {
														this.dia_aplica_vacion = dia_aplica_vacion;
													}

													public List<Double[]> getMatriz() {
														return matriz;
													}

													public void setMatriz(List<Double[]> matriz) {
														this.matriz = matriz;
													}
	    		 		  									
		/**
	    * Metodo que devuelve el ide maximo de una tabla
		* @return String SQL Codigo maximo de los ide primarios de de las tablas
		*/

		public String servicioCodigoMaximo(String tabla,String ide_primario){
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
		}
	    		 		  									
	
	/*
	 * Limpio los coponentes para la tabla de de resumen de vacaciones	
	 */
		public void setear(){
		  gri_resumen_vacaciones_periodo.getChildren().clear();
		  gri_anular_dias_solicitados.getChildren().clear();
		  utilitario.addUpdate("dia_aplica_vacion");
		  utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");

		  dia_aplica_vacion.cerrar();
	  }
	  
	  

	    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
	        double parteEntera, resultado;
	        resultado = valorInicial;
	        parteEntera = Math.floor(resultado);
	        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
	        resultado=Math.round(resultado);
	        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
	        return resultado;
	    }
	    
		public Tabla getTab_tabla_vacacion() {
			return tab_tabla_vacacion;
		}

		public void setTab_tabla_vacacion(Tabla tab_tabla_vacacion) {
			this.tab_tabla_vacacion = tab_tabla_vacacion;
		}

		public Tabla getTab_tabla_detalle_vacaccion() {
			return tab_tabla_detalle_vacaccion;
		}

		public void setTab_tabla_detalle_vacaccion(Tabla tab_tabla_detalle_vacaccion) {
			this.tab_tabla_detalle_vacaccion = tab_tabla_detalle_vacaccion;
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

		public Tabla getTab_asi_vacacion_resumen_empleado() {
			return tab_asi_vacacion_resumen_empleado;
		}

		public void setTab_asi_vacacion_resumen_empleado(
				Tabla tab_asi_vacacion_resumen_empleado) {
			this.tab_asi_vacacion_resumen_empleado = tab_asi_vacacion_resumen_empleado;
		}

		public int getDias360(String periodoNuevoEntrada, String periodoNuevoSalida){

			int mesIniLaborados,mesFinLaborados,diaInicioLaborados,diaFinLaborados,anioInicioLaborados,anioFinLaborados;
			diaFinLaborados=utilitario.getDia(periodoNuevoSalida);
			diaInicioLaborados=utilitario.getDia(periodoNuevoEntrada);
			mesFinLaborados=utilitario.getMes(periodoNuevoSalida);
			mesIniLaborados=utilitario.getMes(periodoNuevoEntrada);
			anioFinLaborados=utilitario.getAnio(periodoNuevoSalida);
			anioInicioLaborados=utilitario.getAnio(periodoNuevoEntrada);

			int valorAnio,valorMes,valorDia;
			valorAnio=anioFinLaborados-anioInicioLaborados;
			valorMes=mesFinLaborados-mesIniLaborados;
			valorDia=diaFinLaborados-diaInicioLaborados;
			int formula=((valorAnio*360)+((valorMes)*30)+(valorDia))+1;
			return formula;
}

	    public String retornaAccionResumenVacacion(String fec_ini,String fec_fin,String ide_gtemp){
	    	TablaGenerica tab_acciones=utilitario.consultar(" select ide_geedp,rmu_geedp FROM gen_empleados_departamento_par where "
	    			+ "fecha_geedp between '"+fec_ini+"' and '"+fec_fin+"' and ide_gtemp="+ide_gtemp+"");
	    	return tab_acciones.getValor("ide_geedp");
	    } 
	    
	    public TablaGenerica retornaTablaAccionResumenVacacion(String fec_ini,String fec_fin,String ide_gtemp){
	    	TablaGenerica tab_acciones=utilitario.consultar(" select ide_geedp,rmu_geedp,fecha_geedp,fecha_liquidacion_geedp,activo_geedp FROM gen_empleados_departamento_par where "
	    			+ "fecha_geedp between '"+fec_ini+"' and '"+fec_fin+"' and ide_gtemp="+ide_gtemp+" order by ide_geedp asc");
	    	return tab_acciones;
	    } 

	    
	    public TablaGenerica retornaTablaAccionResumenVacacionesRepetidas(String fec_ini,String fec_fin,String ide_gtemp){
	    	TablaGenerica tab_acciones=utilitario.consultar(" select ide_geedp,rmu_geedp,fecha_geedp,fecha_liquidacion_geedp,,activo_geedp FROM gen_empleados_departamento_par where "
	    			+ "fecha_geedp between '"+fec_ini+"' and '"+fec_fin+"' and ide_gtemp="+ide_gtemp+" order by ide_geedp asc");
	    	return tab_acciones;
	    } 
	    
		public double getDatosEmpleado1(String IDE_GTEMP,boolean reporte,String ide_asvac,String fecha_ingreso_continuidad,String fecha_fin_continuidad,String IDE_GEEDP,Double numeroDiasTomados, int periodo,String ide_asvac_origen){
			 bandEntrada=0;
			    bandSalida=0;
				nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
				TablaGenerica tab_partida = getPartida(IDE_GTEMP);
				nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac,fecha_ingreso_continuidad,fecha_fin_continuidad);
				// Obtener los periodos de cada empleado 1,2,3 etc
				List<Integer> peridos = getPeriodos(nda, nde);
				// Obtengo los periodos para el ajuste de dias pendientes del
				// empleado hasta el 30 de abril del 2017
				List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);
				// Obtengo el total de mis dias pendientes al restar los dias
				// acumulados-dias tomados
				//double sumatotalDiasGenerados = 0.0;
				// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
				// que le corresponde y los dias generados hasta el presente
				List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
				// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
				// que le corresponde y los dias generados hasta el 30 de abril de
				//List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
				 double sumatotal_vacaciones = 0.0;
				// Asigno la sumatoria de total de mis dias acumulados a vacacion
				// desde la fecha de ingreso hasta la fecha de hoy
				 //BigDecimal num1 = new BigDecimal(0);
				for (int i = 0; i < vacacionesPeriodo.size(); i++) {
					sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
				}
				double total_dias_tomados=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2))-Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
				double total_dias_pendientes=0,total_dias_tomados_=0.0;
                double valor_retorno=0.0;
				if (total_dias_tomados>0.0) {
					total_dias_pendientes=Double.parseDouble(utilitario.getFormatoNumero(total_dias_tomados,2));
					total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
					valor_retorno=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
				}else if(total_dias_tomados==0.0){
					total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
					total_dias_pendientes=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
				}else {
					total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
					total_dias_pendientes=0.0;
					valor_retorno=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
				}

				String periodoNuevo=""+fecha_ingreso_continuidad+" - "+fecha_fin_continuidad;
				TablaGenerica tab_asi_vacacion_resumen_empleado=new TablaGenerica();
				tab_asi_vacacion_resumen_empleado.setTabla("asi_vacacion_resumen_empleado", "IDE_ASVRE", 3);
				tab_asi_vacacion_resumen_empleado.setCondicion("IDE_ASVRE=-1");
				tab_asi_vacacion_resumen_empleado.ejecutarSql();
				tab_asi_vacacion_resumen_empleado.insertar();
				tab_asi_vacacion_resumen_empleado.setValor("IDE_ASVAC",""+Integer.parseInt(ide_asvac_origen));
				tab_asi_vacacion_resumen_empleado.setValor("ide_periodo_asvre",""+(periodo));
				tab_asi_vacacion_resumen_empleado.setValor("PERIODO_ASVRE",""+periodoNuevo);
				tab_asi_vacacion_resumen_empleado.setValor("nro_dias_vacacion_asvre",""+utilitario.getFormatoNumero(sumatotal_vacaciones,2));
				tab_asi_vacacion_resumen_empleado.setValor("nro_dias_tomados_asvre",""+total_dias_tomados_);
				tab_asi_vacacion_resumen_empleado.setValor("nro_dias_pendientes_asvre",""+total_dias_pendientes);
			    tab_asi_vacacion_resumen_empleado.setValor("ide_geedp_asvac",IDE_GEEDP);
			    tab_asi_vacacion_resumen_empleado.setValor("activo_asvre","true");
				tab_asi_vacacion_resumen_empleado.guardar();
				guardarPantalla();
                return valor_retorno;
		}


		public Tabla getTab_dias_sin_timbre() {
			return tab_dias_sin_timbre;
		}


		public void setTab_dias_sin_timbre(Tabla tab_dias_sin_timbre) {
			this.tab_dias_sin_timbre = tab_dias_sin_timbre;
		}

		
	public void	actualizarMarcacionesSinTimbre(SelectEvent evt){
		String fecha_inico="",fecha_fin="";
		tab_tabla_vacacion.seleccionarFila(evt);

		if (tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC")== null || tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC").isEmpty() || tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC").equals("")) {
			fecha_fin=utilitario.getFechaActual();
			fecha_inico=tab_tabla_vacacion.getValor("FECHA_INGRESO_ASVAC");

		}else{
			fecha_fin=tab_tabla_vacacion.getValor("FECHA_FINIQUITO_ASVAC");
			fecha_inico=tab_tabla_vacacion.getValor("FECHA_INGRESO_ASVAC");
		}
		tab_dias_sin_timbre.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" AND  "
				+ "fecha_evento_cobmr BETWEEN '"+fecha_inico+"' AND '"+fecha_fin+"' "
				+ "and (horainicioband_cobmr like '%SIN TIMBRE%' or horafinband_cobmr like '%SIN TIMBRE%') ");
				tab_dias_sin_timbre.ejecutarSql();
				tab_dias_sin_timbre.imprimirSql();
				utilitario.addUpdate("tab_tabla_resumen_vacacion_empleado,tab_dias_sin_timbre");
				
	}
		
	
	
}		
