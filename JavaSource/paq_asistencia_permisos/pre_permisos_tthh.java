package paq_asistencia_permisos;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import pckUtilidades.Constantes;
import pckUtilidades.Utilitario;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Confirmar;
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
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_permisos_tthh extends Pantalla {

	private Tabla tab_permisos=new Tabla();
	private Confirmar con_aplicar_vacacion=new Confirmar();
	private Confirmar con_aprobar_solicitud=new Confirmar();
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	
	//CONDICION 
	
	private String fechaIni="";	
	private String fechaFin="";

	
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_filtro_activo = new Dialogo();
	
	private Dialogo dia_aplica_vacion = new Dialogo();
	private Dialogo dia_aplica_anulacion_parcial = new Dialogo();
	private Dialogo dia_anular_dias_solicitados =  new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_asis=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private Tabla tab_permiso_justificacion=new Tabla();
	private Etiqueta eti_num_dias_vacacion= new Etiqueta();
	private Boolean aplica_vacacion =false;
	private Dialogo dia_anulado=new Dialogo();
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();
	private int numeroSumarFinesSemana =0;
	//private double diasTotales =0;
	private int numeroFinesSemana = 0;
	private int numeroFinesSemanaTotal =0;
	private int descuentoFinesSemana =0;
	private Confirmar con_guardar=new Confirmar();

	//Anulacion
	
	private Texto tex_num_dias_anula=new Texto();
	private AreaTexto are_tex_num_dias_solicitados=new AreaTexto();
	private String ide_gtemp_sel;
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	private Check chk_aplica_vacacion = new Check();
	private Check chk_aplica_anulacion_parcial = new Check();
    private	PanelGrid panGri1 = new PanelGrid();
	private Check  chk_aplica_vacacion_sabado = new Check();
	private Check chk_aplica_vacacion_domingo = new Check();
	private Check chk_aplica_vacacion_sabado_domingo = new Check();

	private Dialogo dia_aplica_vacion_fin_semana = new Dialogo();
	private Boolean sabado;
	private Boolean domingo;
	private Etiqueta eti_aplica_vacacion_domingo= new Etiqueta();
	private Etiqueta eti_aplica_vacacion_sabado= new Etiqueta();
	private Etiqueta eti_aplica_vacacion_sabado_domingo= new Etiqueta();
	private Etiqueta eti_num_dias_solicitud_vacacion= new Etiqueta();

	private Texto tex_num_dias_permitidos=new Texto();
	private Texto tex_num_dias_cargo_vacacion=new Texto();
	private Dialogo dia_motivo_dias_solicitados= new Dialogo();
	
	int motivo_calamidad;
	int motivo_asistencia;
	


	public pre_permisos_tthh() {
		try{

	//Inicio los valores de asistencia
		Boton bot_anulado=new Boton();
		bot_anulado.setValue("ANULAR SOLICITUD");
		bot_anulado.setMetodo("anularSolicitud");

		Boton bot_aprobacion_talento_humano=new Boton();
		bot_aprobacion_talento_humano.setValue("APROBACIÓN TALENTO HUMANO");
		bot_aprobacion_talento_humano.setMetodo("aprobacionTalentoHumano");
		

		Boton bot_aprobados=new Boton();
		bot_aprobados.setValue("PERMISOS APROBADOS");
		bot_aprobados.setMetodo("permisosAprobados");
		
		Boton bot_Sinaprobar=new Boton();
		bot_Sinaprobar.setValue("PERMISOS SIN APROBAR");
		bot_Sinaprobar.setMetodo("permisosSinAprobar");
		
		

		
		
	// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiarPantalla");

		//Etiqueta eti_colaborador=new Etiqueta("OPCIONES TALENTO HUMANO:");

		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	Boton bot_fechas= new Boton();
    	bot_fechas.setIcon("ui-icon-calculator");
    	bot_fechas.setMetodo("seleccionarFechasReporte");
    	bot_fechas.setValue("CONSULTA POR FECHAS");
    	bot_fechas.setTitle("CONSULTA POR FECHAS");
    	bar_botones.agregarBoton(bot_fechas);
    	
		
		
		//bar_botones.agregarComponente(eti_colaborador);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");
		bar_botones.agregarBoton(bot_anulado);
		bar_botones.agregarBoton(bot_aprobacion_talento_humano); 
		bar_botones.agregarBoton(bot_aprobados); 
		bar_botones.agregarBoton(bot_Sinaprobar); 
		bar_botones.agregarBoton(bot_limpiar); 
		
		
		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.agregarRelacion(tab_permiso_justificacion);
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
		//tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);	
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setVisible(false);		

		tab_permisos.getColumna("IDE_ASPVH").setNombreVisual("CODIGO");
    	tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
    	tab_permisos.getColumna("IDE_ASMOT").setNombreVisual("MOTIVO AUSENCIA");
		tab_permisos.getColumna("IDE_ASMOT").setLectura(true);	
		//tab_permisos.getColumna("IDE_ASMOT").setAutoCompletar();

		
		
		
		
		tab_permisos.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_permisos.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_permisos.getColumna("IDE_GEEDP").setLectura(true);
		tab_permisos.getColumna("IDE_GEEDP").setNombreVisual("EMPLEADO");
		tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo(tab_permisos.getColumna("IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP").setLectura(true);	
		tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP").setNombreVisual("GERENTE DE AREA");
		tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setNombreVisual("JEFE INMEDIATO");
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setNombreVisual("RESPONSABLE TH");
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setLectura(true);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setNombreVisual("APLICA VACACION");
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
		//tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("CargarFechaHasta");
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setNombreVisual("FECHA DESDE");
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setNombreVisual("FECHA HASTA");
		//tab_permisos.getColumna("HORA_DESDE_ASPVH").setRequerida(true);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setMetodoChange("calaculahoras");
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setNombreVisual("HORA DESDE");
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setLectura(true);
		//tab_permisos.getColumna("HORA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setMetodoChange("calaculahoras");
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setNombreVisual("HORA HASTA");
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setNombreVisual("NRO HORAS");
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setNombreVisual("NRO DIAS");
		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);
		tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_ASPVH").setLectura(true);
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		tab_permisos.getColumna("ACTIVO_ASPVH").setNombreVisual("ACTIVO");
		tab_permisos.getColumna("ACTIVO_ASPVH").setLectura(true);
		tab_permisos.getColumna("DETALLE_ASPVH").setNombreVisual("DETALLE");
		tab_permisos.getColumna("DETALLE_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setNombreVisual("FEC. ANULA");
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setLectura(true);
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setNombreVisual("RAZÓN ANULA");
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setLectura(true);
		tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("ANULADO_ASPVH").setNombreVisual("ANULADO");
		tab_permisos.getColumna("ANULADO_ASPVH").setLectura(true);

		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setNombreVisual("APROBADO TTHH");
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setLectura(true);;

		tab_permisos.getColumna("APROBADO_ASPVH").setNombreVisual("APROBADO");
		
	
		//tab_permisos.setCondicion("APROBADO_ASPVH=TRUE and APROBADO_TTHH_ASPVH=FALSE AND ANULADO_ASPVH=FALSE and fecha_solicitud_aspvh between '2019-06-01' and '"+utilitario.getFechaActual()+"' ");
		tab_permisos.setCondicion("IDE_ASPVH=-1");
		
		
		tab_permisos.setCampoOrden("IDE_ASPVH DESC");
		tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);

		tab_permisos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("SOLICITUD DE PERMISOS / VACACIONES TALENTO HUMANO");

		
	//Permiso de Justificacion 

		tab_permiso_justificacion.setId("tab_permiso_justificacion");
		tab_permiso_justificacion.setTabla("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ", 2);
		tab_permiso_justificacion.getColumna("DETALLE_ASPEJ").setNombreVisual("DETALLE");
		tab_permiso_justificacion.getColumna("FECHA_ASPEJ").setNombreVisual("FECHA");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setCheck();
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setValorDefecto("true");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setNombreVisual("ACTIVO");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setUpload("permisos");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setImagen("128", "128");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setNombreVisual("DETALLE JUSTIFICACION");
		tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permiso_justificacion.setLectura(true);
		tab_permiso_justificacion.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_permiso_justificacion);
		pat_panel2.setMensajeWarn("JUSTIFICACION DE PERMISOS / VACACIONES");


		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "70%", "H");
		agregarComponente(div_division);

		
		// confirmacion para guardar datos
		con_aplicar_vacacion.setId("con_aplicar_vacacion");
		agregarComponente(con_aplicar_vacacion);
	
		
		Etiqueta eti_aplica_vacacion=new Etiqueta("Aplicar cargo a vacaciones");
		chk_aplica_vacacion.setId("chk_aplica_vacacion");
		PanelGrid panGri = new PanelGrid();
		panGri.setColumns(2);
		panGri.getChildren().add(eti_aplica_vacacion);
		panGri.getChildren().add(chk_aplica_vacacion);
		
		//Dialogo confirmacion aplica o no aplica a vacacion
		
		dia_aplica_vacion.setId("dia_aplica_vacion");
		dia_aplica_vacion.setTitle("CONFIRMACION APLICACION");
		dia_aplica_vacion.getBot_aceptar().setMetodo("aceptarAplicaVacacion");
		dia_aplica_vacion.getGri_cuerpo().getChildren().add(panGri);
		dia_aplica_vacion.setWidth("20");
		dia_aplica_vacion.setHeight("15");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_aplica_vacion);
		
				

		con_aprobar_solicitud.setId("con_aprobar_solicitud");
		con_aprobar_solicitud.setMessage("Esta seguro que desea aprobar la solicitud");
		con_aprobar_solicitud.setTitle("CONFIRMACION APROBACION DE SOLICITUD");
		con_aprobar_solicitud.getBot_aceptar().setMetodo("aceptarAprobarSolicitudTalento");
	//	con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
		agregarComponente(con_aprobar_solicitud);
		
		List lista = new ArrayList();
		Object fila1[] = {
				"0", "INACTIVO"
		};
		Object fila2[] = {
				"1", "ACTIVO"
		};
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setListaSeleccion(lista);
		lis_activo.setVertical();
		dia_filtro_activo.setId("dia_filtro_activo");
		dia_filtro_activo.setTitle("ESCOGA  EMPLEADO ACTIVO/INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		dia_filtro_activo.setDynamic(false);
		agregarComponente(dia_filtro_activo);
		set_empleado_asis.setId("set_empleado_asis");
		set_empleado_asis.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,  " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"(case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' ||  " +
				"PRIMER_NOMBRE_GTEMP || ' ' ||  " +
				"(case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES  " +
				"from GTH_EMPLEADO " +
				"WHERE ACTIVO_GTEMP IN(FALSE,TRUE) " +
				"ORDER BY IDE_GTEMP ASC, " +
				"NOMBRES ASC ", "IDE_GTEMP");
		set_empleado_asis.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado_asis.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado_asis.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado_asis.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado_asis);
		sel_cal.setId("sel_cal");
		sel_cal.setMultiple(true);
		sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal);
	

	
		/// dialogo de anula

		cal_fecha_anula.setId("cal_fecha_anula");

		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:200px;");

		Grid gri_anular_horas_extra=new Grid();
		gri_anular_horas_extra.setColumns(1);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
	
	
		dia_anulado.setId("dia_anulado");
		dia_anulado.setDialogo(gri_anular_horas_extra);
		dia_anulado.setWidth("18%");
		dia_anulado.setHeight("20%");
		dia_anulado.setTitle("ANULACIÓN DE SOLICITUD DE PERMISOS");
		dia_anulado.getBot_aceptar().setMetodo("aceptarAnulacionHorasPermisos");				
		dia_anulado.setDynamic(false);
		gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anulado);
		
		
		//Aplica anulacion parcial
		
		
		Etiqueta eti_aplica_anulacion_parcial=new Etiqueta("ANULACIÓN PARCIAL");
		chk_aplica_anulacion_parcial.setId("chk_aplica_anulacion_parcial");
		PanelGrid panGrianulacion = new PanelGrid();
		panGrianulacion.setColumns(2);
		panGrianulacion.getChildren().add(eti_aplica_anulacion_parcial);
		panGrianulacion.getChildren().add(chk_aplica_anulacion_parcial);
		
		//Dialogo confirmacion aplica o no aplica a vacacion
		
		dia_aplica_anulacion_parcial.setId("dia_aplica_anulacion_parcial");
		dia_aplica_anulacion_parcial.setTitle("CONFIRMACION ANULACIÓN PARCIAL");
		dia_aplica_anulacion_parcial.getBot_aceptar().setMetodo("validarAnulacionParcial");
		dia_aplica_anulacion_parcial.getGri_cuerpo().getChildren().add(panGrianulacion);
		dia_aplica_anulacion_parcial.setWidth("20%");
		dia_aplica_anulacion_parcial.setHeight("15%");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_aplica_anulacion_parcial);
		
		

/// dialogo de anula

		Grid gri_anular_dias_solicitados=new Grid();
		gri_anular_dias_solicitados.setColumns(2);
		gri_anular_dias_solicitados.getChildren().add(new Etiqueta("TOTAL DIAS SOLICITADOS: "));
		gri_anular_dias_solicitados.getChildren().add(eti_num_dias_solicitud_vacacion);
		gri_anular_dias_solicitados.getChildren().add(new Etiqueta("NRO DE DIAS TOMADOS A RECALCULAR: "));
		gri_anular_dias_solicitados.getChildren().add(tex_num_dias_anula);
		//dia_anular_dias_solicitados.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		
		dia_anular_dias_solicitados.setId("dia_anular_dias_solicitados");
		dia_anular_dias_solicitados.setWidth("30%");
		dia_anular_dias_solicitados.setHeight("20%");
		dia_anular_dias_solicitados.setTitle("INGRESE EL NRO DIAS TOMADOS A RECALCULAR");
		dia_anular_dias_solicitados.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		dia_anular_dias_solicitados.getBot_aceptar().setMetodo("aceptarDescuentoAnulacionParcial");				
		dia_anular_dias_solicitados.setDynamic(false);
		gri_anular_dias_solicitados.setStyle("width:" + (dia_anular_dias_solicitados.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anular_dias_solicitados);
	

		
		
// Anulacion Parcial Calamidad y Asistencia Medica		
		
		Grid gri_motivo_dias_solicitados=new Grid();
		gri_motivo_dias_solicitados.setColumns(2);
		gri_motivo_dias_solicitados.getChildren().add(new Etiqueta("TOTAL TIEMPO SOLICITADO: "));
		gri_motivo_dias_solicitados.getChildren().add(eti_num_dias_vacacion);
		gri_motivo_dias_solicitados.getChildren().add(new Etiqueta("TIEMPO PERMITIDO: "));
		gri_motivo_dias_solicitados.getChildren().add(tex_num_dias_permitidos);
		gri_motivo_dias_solicitados.getChildren().add(new Etiqueta("TIEMPO CARGO VACACION: "));
		gri_motivo_dias_solicitados.getChildren().add(tex_num_dias_cargo_vacacion);
		//dia_anular_dias_solicitados.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		dia_motivo_dias_solicitados.setId("dia_motivo_dias_solicitados");
		dia_motivo_dias_solicitados.setWidth("30%");
		dia_motivo_dias_solicitados.setHeight("20%");
		dia_motivo_dias_solicitados.setTitle("REGISTRO DE DIAS CON CARGO A VACACIÓN");
		dia_motivo_dias_solicitados.getGri_cuerpo().getChildren().add(gri_motivo_dias_solicitados);
		dia_motivo_dias_solicitados.getBot_aceptar().setMetodo("aceptarAplicacionVacacionParcial");		
		dia_motivo_dias_solicitados.getBot_cancelar().setMetodo("setteraVacacion");		
	
		dia_motivo_dias_solicitados.setDynamic(false);
		gri_anular_dias_solicitados.setStyle("width:" + (dia_motivo_dias_solicitados.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_motivo_dias_solicitados);
	
		
		
		
		
		
		
		
		

		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		
		 eti_aplica_vacacion_sabado=new Etiqueta("Aplicar cargo a vacaciones Sabado");
		chk_aplica_vacacion_sabado.setId("chk_aplica_vacacion_sabado");
		
		 eti_aplica_vacacion_domingo=new Etiqueta("Aplicar cargo a vacaciones Domingo");
		chk_aplica_vacacion_domingo.setId("chk_aplica_vacacion_domingo");
		 eti_aplica_vacacion_sabado_domingo=new Etiqueta("Aplicar cargo a vacaciones Sabado y Domingo");
		chk_aplica_vacacion_sabado_domingo.setId("chk_aplica_vacacion_sabado_domingo");
		panGri1.setColumns(2);
	//Dialogo confirmacion aplica o no aplica a vacacion
		dia_aplica_vacion_fin_semana.setId("dia_aplica_vacion_fin_semana");
		dia_aplica_vacion_fin_semana.setTitle("CONFIRMACIÓN APLICACIÓN A VACACIÓN");
		dia_aplica_vacion_fin_semana.getBot_aceptar().setMetodo("aplicarVacacion");
		dia_aplica_vacion_fin_semana.getGri_cuerpo().getChildren().add(panGri1);
		dia_aplica_vacion_fin_semana.setWidth("20%");
		dia_aplica_vacion_fin_semana.setHeight("15%");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_aplica_vacion_fin_semana);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error en el constructor "+e);
		}
	}
	
 	

//Metodo aceptar del aplicar o no vacaciones
	public void aceptarAplicaVacacion(){
		
		
	
		con_aprobar_solicitud.dibujar();
		
		
		
		utilitario.addUpdate("con_aprobar_solicitud");
		
		
	}
	
	public void aceptarAnulacionParcial(){
		dia_aplica_anulacion_parcial.dibujar();
		utilitario.addUpdate("con_aprobar_solicitud");
		
		
		
		
	}
	
	
	public void aceptarDescuentoAnulacionParcial(){
		String num_dias=tex_num_dias_anula.getValue().toString();
		int dias= Integer.parseInt(num_dias);
		int dias_solicitados_vacaciones= Integer.parseInt(tab_permisos.getValor("NRO_DIAS_ASPVH").toString());
		
		if ((dias>dias_solicitados_vacaciones)) {
 			utilitario.agregarMensajeError("Anulación Parcial de Vacaciones incorrecto", "El número de días debe ser mayor al solicitado ");
		    return;
		}else {
			aplicaAnulacionDias(dias);	
		}
		
		
		
	}
	
	
	public void aceptarAplicacionVacacionParcial(){
		
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));
		
		TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot,num_max_dias_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
        String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
        String esvacacion=tab_motivo.getValor("es_vacacion_asmot");
          int num_max_dias_asmot=Integer.parseInt(tab_motivo.getValor("num_max_aplica_vacacion_asmot"));
          int num_dias_solicitado=Integer.parseInt(tab_permisos.getValor("nro_dias_aspvh"));
          double num_horas_solicitado=Double.parseDouble(tab_permisos.getValor("nro_horas_aspvh"));
          int tipo_permiso=Integer.parseInt(tab_permisos.getValor("tipo_aspvh"));
          int motivo=Integer.parseInt(tab_permisos.getValor("ide_asmot"));
		
      	String num_dias_permitidos=tex_num_dias_permitidos.getValue().toString();
		double dias_permitidos= Double.parseDouble(num_dias_permitidos);
		String num_dias_cargo=tex_num_dias_cargo_vacacion.getValue().toString();
		double num_dias_cargo_vacacion= Double.parseDouble(num_dias_cargo);
		TablaGenerica tab_codigo_vacacion=utilitario.consultar("select * from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");

	
          if (tipo_permiso==1) {
			
        	  double dias_solicitados_vacaciones= Double.parseDouble(tab_permisos.getValor("NRO_HORAS_ASPVH").toString());
      	  		if ((dias_permitidos+num_dias_cargo_vacacion)>dias_solicitados_vacaciones) {
       			utilitario.agregarMensajeError("Apicación de Vacaciones incorrecto", "El número de horas debe ser igual al solicitado ");
      		    return;
      		}else if ((dias_permitidos+num_dias_cargo_vacacion)<dias_solicitados_vacaciones) {
      			utilitario.agregarMensajeError("Apicación de Vacaciones incorrecto", "El número de horas debe ser igual al solicitado ");
      		    return;
			} else {

		    	ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));
			    TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
			    tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
				if (num_dias_cargo_vacacion>0.0) {

			    tab_permisos.setValor("registro_novedad_aspvh", "true");
				}
				tab_permisos.setValor("aprobado_tthh_aspvh", "true");
			    tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
				tab_permisos.guardar();
				
				if (num_dias_cargo_vacacion>0.0) {
				
				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
				utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
				+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(num_dias_cargo_vacacion/8)+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,false)");
				}
				guardarPantalla();
				dia_motivo_dias_solicitados.cerrar();
				utilitario.addUpdate("dia_motivo_dias_solicitados");
				EnviarCorreoMaxDias((num_dias_cargo_vacacion/8),"Asistencia a Consulta Médica","hora(s)",num_horas_solicitado);
				if (num_dias_cargo_vacacion>0.0) {

				utilitario.agregarMensaje("Aplicado Vacación", "Se aplico "+(num_dias_cargo_vacacion/8) +" hora(s) a vacaciones exitosamente");
				}
      		}
      		
          
          
          }
		
		if (tipo_permiso==4) {
			int dias_solicitados_vacaciones= Integer.parseInt(tab_permisos.getValor("NRO_DIAS_ASPVH").toString());
			double horas_solicitados_vacaciones= Double.parseDouble(tab_permisos.getValor("NRO_HORAS_ASPVH"));
			
			if ((dias_permitidos+num_dias_cargo_vacacion)>(horas_solicitados_vacaciones/8)) {
       			utilitario.agregarMensajeError("Apicación de Vacaciones incorrecto", "El número de dias debe ser menor al solicitado ");
			    return;
			}else if ((dias_permitidos+num_dias_cargo_vacacion)<(horas_solicitados_vacaciones/8)) {
      			utilitario.agregarMensajeError("Apicación de Vacaciones incorrecto", "El número de dias debe ser igual al solicitado ");
      		    return;
      		    }
			else {
		    	ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));
			    TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
			    tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
				if (num_dias_cargo_vacacion>0.0) {
			    tab_permisos.setValor("registro_novedad_aspvh", "true");
				}
				tab_permisos.setValor("aprobado_tthh_aspvh", "true");
			    tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
				tab_permisos.guardar();
				
				if (num_dias_cargo_vacacion>0.0) {
					
				
				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
				utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
				+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+num_dias_cargo_vacacion+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,false)");
				}
				guardarPantalla();
				dia_motivo_dias_solicitados.cerrar();
				utilitario.addUpdate("dia_motivo_dias_solicitados");
				EnviarCorreoMaxDias(num_dias_cargo_vacacion,"Calamidad","dia(s)",(double)dias_solicitados_vacaciones);
				if (num_dias_cargo_vacacion>0.0) {
				utilitario.agregarMensaje("Aplicado Vacación", "Se aplico "+num_dias_cargo_vacacion +" dia(s) a vacaciones exitosamente");
				}else{
				}
			}
		
			
		}
		
		
		
		
		
		
	}	
	
	
	public void validarAnulacionParcial(){
	
	if((Boolean)chk_aplica_anulacion_parcial.getValue()){
		dia_aplica_anulacion_parcial.cerrar();
		String numdias=tab_permisos.getValor("NRO_DIAS_ASPVH").toString();
		int dias=Integer.parseInt(numdias);
		eti_num_dias_solicitud_vacacion.setValue(""+numdias);
		dia_anular_dias_solicitados.dibujar();
		utilitario.addUpdate("dia_anular_dias_solicitados");
		
		
	
	}else {
		dia_aplica_anulacion_parcial.cerrar();
		dia_anulado.dibujar();
		utilitario.addUpdate("dia_anulado");

		
	}
	

}
	
	
	public String obtenerMailEmpleadoSolicitaPermiso(){
		TablaGenerica tabEmp = ser_empleado.getCorreoEmpleado(ide_gtemp_sel);
		
		String correo = String.valueOf(tabEmp.getFilas().get(0).getCampos()[1]);
		return correo;
	}
	
	
	
	//aplica anulacion
	
	public void aplicaAnulacion(){
		
	
		if (tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equals("true")) {
			utilitario.getConexion().ejecutarSql("delete from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("ide_aspvh"));
			tab_permisos.setValor("registro_novedad_aspvh", "false");
			tab_permisos.setValor("anulado_aspvh", "true");
			tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
			tab_permisos.guardar();
			guardarPantalla();
			utilitario.agregarMensaje("Anulación De Vacación", "Se descontó a vacaciones exitosamente");
            EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));
			utilitario.addUpdate("tab_permisos");

			}
			else{
			utilitario.agregarMensaje("Anulación De Vacación", "Realizada exitosamente");
			}
		
		
	}
	
	
		
	
	public void aplicarVacacion(){
		
		if (tab_permisos.getValor("tipo_aspvh").equalsIgnoreCase("4")
				|| tab_permisos.getValor("tipo_aspvh").equalsIgnoreCase("2")){
	 	    aplicaVacacionDias();
		}else{
			aplicaVacacionHoras();
		}
	}
	
	
	public void aplicaVacacionDias(){
		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));

		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

	//	TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
		
		TablaGenerica tab_consulta_vacacion = utilitario.consultar("select * from gth_empleado where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_GTEMP=TRUE");
		boolean banderaPeriodoVacacion=false;
		TablaGenerica tab_codigo_vacacion=null;
		if(tab_consulta_vacacion.getTotalFilas()>0){
	tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=TRUE order by ide_asvac desc limit 1");
	banderaPeriodoVacacion=true;
	}else {
		tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=FALSE order by ide_asvac desc limit 1");
		banderaPeriodoVacacion=true;
	}	
	
		
		if(banderaPeriodoVacacion==true){
				
		String detalle_aspvh=tab_permisos.getValor("detalle_aspvh");
		String dias=tab_permisos.getValor("nro_dias_aspvh");
        int nro_dias_aspvh=Integer.parseInt(dias);
		double nro_horas_aspvh=Double.parseDouble(tab_permisos.getValor("nro_horas_aspvh"));
        
        
		//TablaGenerica tab_codigo_vacacion=utilitario.consultar("select * from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"))+")");
		double dias_pendientes=0.0;
		int fines_semana=0;
		double dias_tomados_asvac=0.0;
        double calculoNuevoFinesdeSemana=0.0;
		TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));

	     utilitario.getConexion().ejecutarSql("update ASI_PERMISOS_VACACION_HEXT set  "
           		+ "aprobado_tthh_aspvh=true,gen_ide_geedp3="+ide_geedp_activo+","
           				+ "registro_novedad_aspvh=true "
           	    + "where ide_aspvh="+tab_permisos.getValorSeleccionado());
					   	
		guardarPantalla();
		//Validación de tipo permiso personal fines de semana para codigo de trabajo	
			int dias_sabados=0;
			dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			
			int dias_domingos=0;
			dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			
			//Solo dias laborables
			double numeroDiasRealesSinSabadoDomingo;
			numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
			//Solo fines de semana
			int nro_dias_fines_semana=0;
			nro_dias_fines_semana=dias_sabados+dias_domingos;
			
			
			// Validacion si se ha escogido un permiso para un fin de semana
			if(((Boolean)chk_aplica_vacacion_sabado.getValue())==null ){
				chk_aplica_vacacion_sabado.setValue(false);
			}
		
			if(((Boolean)chk_aplica_vacacion_domingo.getValue())==null ){
				chk_aplica_vacacion_domingo.setValue(false);
				
				}
	
			if(((Boolean)chk_aplica_vacacion_sabado_domingo.getValue())==null ){
				chk_aplica_vacacion_sabado_domingo.setValue(false);
				}
			
////////////////////////////////////////////////////////Validacion de dias sabado////////////////////////////////////////////////////////////////////////////		
			
			
				if(((Boolean)chk_aplica_vacacion_sabado.getValue())){

if(numeroDiasRealesSinSabadoDomingo>0){ 

TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");


//Guardar datos de fines de semana
int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN',true,true)");

}else if (numeroDiasRealesSinSabadoDomingo==0) {

				 TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN' ,true,true)");


}
				 guardarPantalla();
//EnviarCorreo(null)();
			}
			


////////////////////////////////////////////////////////Validacion de dias domingos////////////////////////////////////////////////////////////////////////////		



				else if(((Boolean)chk_aplica_vacacion_domingo.getValue())){
										

if(numeroDiasRealesSinSabadoDomingo>0){ 
				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");


//Guardar datos de fines de semana
int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS DÍA DOMINGO CON CARGO A VACACIÓN',true,true)");

}else if (numeroDiasRealesSinSabadoDomingo==0) {

TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN' ,true,true)");


}
				guardarPantalla();
//EnviarCorreo(null)();


			}		

////////////////////////////////////////////////////////Validacion de dias sabados y domingos////////////////////////////////////////////////////////////////////////////		


			else if((Boolean)chk_aplica_vacacion_sabado_domingo.getValue()){

if(numeroDiasRealesSinSabadoDomingo>0){ 

		        TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");


//Guardar datos de fines de semana
int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS DÍA SÁBADO Y DOMINGO CON CARGO A VACACIÓN',true,true)");



}else if (numeroDiasRealesSinSabadoDomingo==0) {

TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN' ,true,true)");

}

else if (numeroDiasRealesSinSabadoDomingo<0) {


TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");

			
//Guardar datos de fines de semana
int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS DÍA SÁBADO Y DOMINGO CON CARGO A VACACIÓN',true,true)");
			
}

	guardarPantalla();
//EnviarCorreo(null)();
}		

else {
	
numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
	if (nro_dias_fines_semana==0) {

	TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
	utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
    +"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN',true,false)");
	guardarPantalla();
}else{
		TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");

//Guardar datos de fines de semana
int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN FIN SEMANA',true,true)");
			guardarPantalla();
}

			}
		utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");
	    dia_aplica_anulacion_parcial.cerrar();
		dia_anular_dias_solicitados.cerrar();
		dia_aplica_vacion_fin_semana.cerrar();
		con_aplicar_vacacion.cerrar();
		con_guardar.cerrar();
		con_aprobar_solicitud.cerrar();
		dia_aplica_vacion.cerrar();
		EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));
		tab_permisos.setCondicion("aprobado_tthh_aspvh=false and anulado_aspvh=false and fecha_desde_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "and APROBADO_ASPVH=TRUE and APROBADO_TTHH_ASPVH=FALSE AND ANULADO_ASPVH=FALSE");
		tab_permisos.ejecutarSql();
		//tab_permisos.actualizar();
		tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permiso_justificacion.ejecutarSql();
		//tab_permisos.actualizar();
		utilitario.addUpdate("tab_permisos,dia_aplica_anulacion_parcial,dia_anular_dias_solicitados,dia_aplica_vacion_fin_semana,con_guardar,con_aplicar_vacacion,con_aprobar_solicitud,dia_aplica_vacion");

		}
		else{
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones contactese con el administrador");
		}
		
	}
	
	
	public void aplicaAnulacionDias(int num_dias){
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));

	//Dias solicitados con cargo a vacacion anterior
     	double diaDescontado=0.0;
		List<BigDecimal> tab_consulta=utilitario.getConexion().consultar("select dia_descontado_asdev from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("IDE_ASPVH"));      
	    for(int i=0;i<tab_consulta.size();i++){
	    	BigDecimal  resultado= (BigDecimal)(tab_consulta.get(i));
	    	diaDescontado=resultado.doubleValue(); 	
	    }
	    

	int nuevocalculohoras=0;
	
	String mensaje;
		mensaje="Recálculo de "+tab_permisos.getValor("nro_dias_aspvh")+" por :"+num_dias+" dia(s) tomados con cargo a vacación";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
		TablaGenerica tab_consulta_vacacion = utilitario.consultar("select * from gth_empleado where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_GTEMP=TRUE");
		boolean banderaPeriodoVacacion=false;
		TablaGenerica tab_codigo_vacacion=null;
		if (tab_consulta_vacacion.getTotalFilas()>0) {
	tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=TRUE order by ide_asvac desc limit 1");
	banderaPeriodoVacacion=true;
	}else {
		tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=FALSE order by ide_asvac desc limit 1");
		banderaPeriodoVacacion=true;
	}	
		
		
		if(banderaPeriodoVacacion==true){

//fecha de recalculo
		Date fecha_nueva=utilitario.getDate();				
		String fechaCadena = sdf.format(fecha_nueva);
		utilitario.getConexion().ejecutarSql("delete from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValorSeleccionado());
		
		TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
		//TablaGenerica tab_codigo_vacacion=utilitario.consultar("select * from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+tab_permisos.getValor("IDE_GEEDP")+")");
		int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
		utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
				+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+((double)num_dias)+",'"+mensaje+"',true,false)");
		guardarPantalla();
        utilitario.addUpdate("tab_permisos");
		utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");
		dia_anular_dias_solicitados.cerrar();
		
		tex_num_dias_anula.limpiar();
		eti_num_dias_solicitud_vacacion.setValue("");
		EnviarCorreoRecalculo(num_dias);
	
		utilitario.addUpdate("tab_permisos,dia_aplica_anulacion_parcial,dia_anular_dias_solicitados,con_guardar");
	


		}

	    else
	    {
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones contactese con el administrador");
		}
		
 	    }

	
	
	public void deshacerAplicaVacacion(){
		TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
		tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_tthh_aspvh=true,gen_ide_geedp3="+empleado_atual.getValor("ide_geedp")+"  where ide_aspvh="+tab_permisos.getValorSeleccionado());
		
		
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		
		if(tab_permisos.getValor("anulado_aspvh").equals("true")){
			utilitario.agregarMensajeInfo("El registro se encuentra anulado", "No se puede aplicar justificación.");
			return;
		}
		
		if((tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equals("")) || (tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equals("false"))  ){
			utilitario.agregarMensajeInfo("Registro sin cargo a vacación", "No puede aplicar justificación no tiene cargo a vacación.");
			return;
		}
		
		if (tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equals("true")) {
		
			
		utilitario.getConexion().ejecutarSql("delete from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("ide_aspvh"));
		aplicaVacacionDias();
		
		
		//tab_permisos.setValor("registro_novedad_aspvh", "false");
		tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
		tab_permisos.guardar();
		guardarPantalla();
		utilitario.addUpdate("tab_permisos");
		utilitario.agregarMensaje("Aplicado Deshacer Vacación", "Se descontó a vacaciones exitosamente");
		}
		
		
		
		}
	

	public void aceptarAprobarSolicitud(){		
		
		
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where TIPO_ASPVH=4 and ide_aspvh="+tab_permisos.getValorSeleccionado());
		guardarPantalla();
		con_aplicar_vacacion.cerrar();		
		String ide_anterior=tab_permisos.getValorSeleccionado();		
		tab_permisos.ejecutarSql();
		tab_permisos.setFilaActual(ide_anterior);
	}

	public void anularSolicitud(){		
		//if(aut_empleado.getValue()!=null && !aut_empleado.getValue().toString().isEmpty()){
		
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");


		
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Anular la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				
				
				 TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
	             String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
	             String esvacacion=tab_motivo.getValor("es_vacacion_asmot");

				
	       int aplica_vacacion1=0;
	        if ((tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equals("false"))) {
	        	 aplica_vacacion1=1;
				
	        }else {
	        	 aplica_vacacion1=2;
			}  
				
				
	             
	       if (esvacacion.equals("true") && aplica_vacacion1==2) {
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
				dia_aplica_anulacion_parcial.dibujar();
	            		utilitario.addUpdate("dia_aplica_anulacion_parcial");
				}
				
			}else {
				are_tex_razon_anula.limpiar();
				dia_anulado.dibujar();
				utilitario.addUpdate("dia_anulado");	
			}					
			}else{
				utilitario.agregarMensajeInfo("No se puede Anular", "No se hay solicitudes en curso");
			}			

	}

	public void aceptarAnulacionHorasPermisos(){
		
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
 				utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set  "
 						+ "razon_anula_aspvh='"+are_tex_razon_anula.getValue().toString()+"',"
 								+ "fecha_anula_aspvh='"+utilitario.getFechaActual()+"',"
 						+ "anulado_aspvh=true,gen_ide_geedp3="+ide_geedp_activo+" where ide_aspvh="+tab_permisos.getValorSeleccionado());	
					
					
					if(tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equalsIgnoreCase("true")){
						utilitario.getConexion().ejecutarSql("delete from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValorSeleccionado());
						guardarPantalla();
					}						
					guardarPantalla();
					dia_anulado.cerrar();
					EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));	
					tab_permisos.actualizar();
					utilitario.addUpdate("tab_permisos,dia_anulado");

		} else {
			utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe ingresar una Razon para anular  los Permisos");
		}	
		
	}


	int estado_num_max_aplica_vacacion;
	
	//METODO 1 
	public void aprobacionTalentoHumano(){

			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				
				if(tab_permisos.getValor("REGISTRO_NOVEDAD_ASPVH").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aplicada a Vacacion");
					return;
				}
		
				
				
		//Datos para validación de tipo de permisos
             TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot,num_max_dias_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
             String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
             String esvacacion=tab_motivo.getValor("es_vacacion_asmot");
               int num_max_dias_asmot=Integer.parseInt(tab_motivo.getValor("num_max_aplica_vacacion_asmot"));
                int tipo_permiso=Integer.parseInt(tab_permisos.getValor("tipo_aspvh"));
                int num_dias_solicitado=0;
               //Por dias
               if (tipo_permiso==4) {
            	   num_dias_solicitado=Integer.parseInt(tab_permisos.getValor("nro_dias_aspvh"));
               	}
               double num_horas_solicitado=(Double.parseDouble(tab_permisos.getValor("nro_horas_aspvh"))/8);
               int motivo=Integer.parseInt(tab_permisos.getValor("ide_asmot"));
                String fecha_solicitud=tab_permisos.getValor("FECHA_SOLICITUD_ASPVH");             
                double num_horas_solicitado_asistencia=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tab_permisos.getValor("nro_horas_aspvh")),2));
              
                
             /**
              * Validacion ingreso de maximo de justificacion para los tipos de permisos
              */
             
             
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateInString = tab_permisos.getValor("FECHA_HASTA_ASPVH");
            		Date date = null;
            		Date date_hasta = null;
             		
             		/**
             		 * Le asigno a una variable de tipo date la fecha de ingreso
             		 */
             			
             		try {
             			date = sdf.parse(dateInString);
  
             		} catch (ParseException e) {
             			// TODO Auto-generated catch block
             			e.printStackTrace();
             		}

             		//Creo variable tipo calendario para guardar la fecha de ingreso del empleado
             		Calendar fechaIngresoEmpleado = Calendar.getInstance();
             		fechaIngresoEmpleado.setTime(date);
             		long diferenciaEn_ms = new Date().getTime() - fechaIngresoEmpleado.getTime().getTime();
             		long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
          
             		 
	int diferencia_fechas=	utilitario.getDiferenciasDeFechas(fechaIngresoEmpleado.getTime(),utilitario.getDate());
             		
     //Valida si ingreso en los tres dias posteriores a su ingreo esto cambiar en el erp
	
             	//valido si no es vacacion 
                if((esvacacion.equals("true")) && (aplica_vacaciones_asmot.equals("true"))) {
                	con_aprobar_solicitud.dibujar();
                	utilitario.addUpdate("con_aprobar_solicitud");		
                	
                              
                }else if ((esvacacion.equals("false")) && (aplica_vacaciones_asmot.equals("true"))){
                	con_guardar.setMessage("ESTA SEGURO DE APLICAR A VACACIÓN");
     				con_guardar.setTitle("CONFIRMACION APROBACION DE SOLICITUD DE PERMISOS");
     				con_guardar.getBot_aceptar().setMetodo("getDiaFinSemanaDescontar");
     				con_guardar.getBot_cancelar().setMetodo("setteraVacacion");
     				con_guardar.dibujar(); 
     				utilitario.addUpdate("con_guardar");       	    	
                	
                	
                }//ASISTENCIA A CONSULTA MEDICA
                else if((esvacacion.equals("false")) && (aplica_vacaciones_asmot.equals("false"))){
                
//					Validadcion si no se pasa actualizo                 	
                	if (motivo==4) {
                   	if (num_horas_solicitado_asistencia<=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(utilitario.getVariable("p_horas_limite_asistencia_medica")),2))) {
    					//aceptarAplicaVacacion();
                
                   		dia_aplica_vacion.dibujar();
    					utilitario.addUpdate("dia_aplica_vacion");		
                    
                   	}else 
					{
                   		//Aplico vacacion parcial
                   		//Si es mayor que 
		    			eti_num_dias_vacacion.setValue((num_horas_solicitado*8)+" horas");
		    			tex_num_dias_permitidos.limpiar();
		    			tex_num_dias_cargo_vacacion.limpiar();
						dia_motivo_dias_solicitados.dibujar();
						utilitario.addUpdate("dia_motivo_dias_solicitados");
					}
                	
                	   	
                
                }//CALAMIDAD
                else if(motivo==2){
                	if (num_dias_solicitado<=num_max_dias_asmot) {
    				//	aceptarAplicaVacacion();
                		dia_aplica_vacion.dibujar();
    					utilitario.addUpdate("dia_aplica_vacion");		
                    
                	
                	
                	}else 
					{
		    			eti_num_dias_vacacion.setValue(num_dias_solicitado+" dias");
		    			tex_num_dias_permitidos.limpiar();
		    			tex_num_dias_cargo_vacacion.limpiar();
		    			dia_motivo_dias_solicitados.dibujar();
						utilitario.addUpdate("dia_motivo_dias_solicitados");
	         		}
             }else {
            		dia_aplica_vacion.dibujar();
					utilitario.addUpdate("dia_aplica_vacion");		
                
			}          	
                
             	
                }
                
                }else{
            	      utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
            	      return;
            		}		    	
              }
             	
	
	   /**
     * Metodo emailNotificacionAprobadoO
     */
    
   
	public String emailNotificacionAprobado(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh) {
        String html = "<p>Estimado, "+strNombreEmpleado+"</p>"
                + "<p>&nbsp;</p>\n"
                + "<p>Notificamos mediante la presente que la solicitud de Permisos/Vacaciones:  "+detallePermiso+".</p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>Saludos cordiales,</p>\n"
                + "<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
        return html;
    }

    
		
	
	
	


	public void EnviarCorreo(int ide_permiso){
	
		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor  "
				+ "from gth_correo  "
				+ "where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" "
				+ "and activo_gtcor=true and notificacion_gtcor=true");
		String correo=tab_correo.getValor("detalle_gtcor");
        
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		
			
			
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				String detallePermiso="";
				String nombreTipoSolicitud;
				String nroHoras;
				String motivo;
				String obtieneNroDias;
			
				//Estructura de mensaje
				String strNombreEmpleado="";
				strNombreEmpleado = retornaDatosCorreoEmpleado(tab_permisos.getValor("IDE_GTEMP"));
				
			    // armar mensaje
				
				TablaGenerica tab_permisos1=utilitario.consultar("select * from asi_permisos_vacacion_hext where ide_aspvh="+ide_permiso);

				
				String ide_aspvh_correo =tab_permisos1.getValor("ide_aspvh").toString();
				String anulado_aspvh_correo =tab_permisos1.getValor("anulado_aspvh").toString();
				String aprobado_tthh_aspvh_correo =tab_permisos1.getValor("aprobado_tthh_aspvh").toString();
				String aprobado_aspvh_correo =tab_permisos1.getValor("aprobado_aspvh").toString();
				String registro_novedad_aspvh =tab_permisos1.getValor("registro_novedad_aspvh").toString();
				String detalleAnulacion="";
				//Para solicitude de horas
				
				String fecha=tab_permisos1.getValor("fecha_solicitud_aspvh").toString();
				Date fecha_nueva= utilitario.DeStringADate(fecha);
				Date fecha_solicitud_aspvh1 = fecha_nueva;
				 SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
				 String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
				
					String nro_dias_aspvh="";
				//Para solicitude de dias
				 if (tab_permisos1.getValor("TIPO_ASPVH").equals("4")) {
				nro_dias_aspvh =tab_permisos1.getValor("nro_dias_aspvh").toString();
				
				}else {
					nro_dias_aspvh="1";
				}
				String estado_aprobacion_tthh="";
				
				
				if ((aprobado_tthh_aspvh_correo.equals("true")) && (registro_novedad_aspvh.equals("false"))) {
					estado_aprobacion_tthh="Aprobado";
				}
					
				if ((aprobado_tthh_aspvh_correo.equals("true")) && (registro_novedad_aspvh.equals("true"))) {
					estado_aprobacion_tthh="Aprobado Con Cargo a Vacación";
				}
				    
				
				if ((anulado_aspvh_correo.equals("true"))) {
					detalleAnulacion=tab_permisos1.getValor("razon_anula_aspvh");
					estado_aprobacion_tthh="Anulado debido a "+detalleAnulacion;
				}
				 
					
				
					
			TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
				
				if (tab_permisos1.getValor("tipo_aspvh").equals("1")) {
					 nombreTipoSolicitud="Permiso Por Horas ";
					 nroHoras= tab_permisos1.getValor("nro_horas_aspvh"); 
					 motivo=tab_motivo.getValor("detalle_asmot");
					 detallePermiso="El permiso Nro:  "+ide_aspvh_correo+" "+motivo.toLowerCase()+" ingresado el "+fecha_solicitud_aspvh+". "
					 		+ "Desde: "+tab_permisos1.getValor("fecha_desde_aspvh")+"  y "
	  				 		+ "Hasta: "+tab_permisos1.getValor("fecha_hasta_aspvh")+" por un lapso de "+nroHoras+" hora(s)  "
	  				 		+ " se encuentra "+estado_aprobacion_tthh+".";
				     System.out.println(detallePermiso);
				}
				if (tab_permisos1.getValor("tipo_aspvh").equals("4")) {
					 motivo=tab_motivo.getValor("detalle_asmot");
					 nroHoras= tab_permisos1.getValor("nro_dias_aspvh"); 
					
					detallePermiso="El permiso Nro:  "+ide_aspvh_correo+" "+motivo.toLowerCase()+" ingresado el "+fecha_solicitud_aspvh+" "
						 		+ "Desde: "+tab_permisos1.getValor("fecha_desde_aspvh")+"  "
		  				 		+ "Hasta: "+tab_permisos1.getValor("fecha_hasta_aspvh")+" por un lapso de "+nroHoras+" dia(s)  "
		  				 		+ " se encuentra "+estado_aprobacion_tthh+". ";
 					 System.out.println(detallePermiso);
				}
							

		System.out.println("Enviando Correo.........");
					
		try {
			/*util.EnviaMail(envMail, correo, 
							"GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE",
							emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh), 
							null);*/
				String str_mail=correo;
				envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
				envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh));
				envMail.setPara(str_mail);
				pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
				
				if(obj.getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			} catch (Exception e) {
			System.out.println("Error en el envío de correo"+e.getMessage());
				}

			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}

				}
							
				
	public void EnviarCorreoRecalculo(int dias){

		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor  "
				+ "from gth_correo  "
				+ "where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" "
				+ "and activo_gtcor=true and notificacion_gtcor=true");
		String correo=tab_correo.getValor("detalle_gtcor");
		
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		
			
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				String detallePermiso="";
				String obtieneNroDias;
				// armar mensaje
				//Estructura de mensaje
				String strNombreEmpleado="";
				strNombreEmpleado = retornaDatosCorreoEmpleado(tab_permisos.getValor("IDE_GTEMP"));
				
				
				
				String ide_aspvh_correo =tab_permisos.getValor("ide_aspvh").toString();
				String fecha=tab_permisos.getValor("fecha_solicitud_aspvh").toString();
				Date fecha_nueva= utilitario.DeStringADate(fecha);
				Date fecha_solicitud_aspvh1 = fecha_nueva;
				SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
				String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
				TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
				
				String	 nombreTipoSolicitud="Permiso Por Dias Recálculo de Vacaciones ";
					 obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
	  			     detallePermiso="Solicitud de Vacaciones Nro: "+ide_aspvh_correo+"  por un lapso de "+obtieneNroDias+" dia(s) se ha recálculado a "+dias+" dia(s) con fecha solicitud "+fecha_solicitud_aspvh+" ";
 					 System.out.println(detallePermiso);
			System.out.println("Enviando Correo Anulación Parcial.........");
		
		try {
				/*util.EnviaMail(envMail, correo, 
							"GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE",
							emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh), 
						null);*/
				String str_mail=correo;
				envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
				envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh));
				envMail.setPara(str_mail);
				pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
				
				if(obj.getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			} catch (Exception e) {
			System.out.println("Error en el envío de correo"+e.getMessage());
			}
	
			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}
	
	}
	

	
	
	public void EnviarCorreoMaxDias(double valor,String motivo,String tipo,double valorInicial){

		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor "
				+ "from gth_correo  "
				+ "where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" "
				+ "and activo_gtcor=true and notificacion_gtcor=true");
		String correo=tab_correo.getValor("detalle_gtcor");
			
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
				
						
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				String detallePermiso="";
				String obtieneNroDias;
				// armar mensaje
				
				//Estructura de mensaje
				String strNombreEmpleado="";
				strNombreEmpleado = retornaDatosCorreoEmpleado(tab_permisos.getValor("IDE_GTEMP"));
				
				
				String ide_aspvh_correo =tab_permisos.getValor("ide_aspvh").toString();
				String fecha=tab_permisos.getValor("fecha_solicitud_aspvh").toString();
				Date fecha_nueva= utilitario.DeStringADate(fecha);
				Date fecha_solicitud_aspvh1 = fecha_nueva;
				SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
				String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
				TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
				
				String	 nombreTipoSolicitud="Permiso Por Dias Recálculo de Vacaciones ";
					 obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
	  			     detallePermiso="Solicitud de "+motivo+" nro "+ide_aspvh_correo+"  por un lapso de "+valorInicial+" "+tipo+" ha sobrepasodo el tiempo permitido, se aplicó "+valor+" "+tipo+" con cargo a vacación realizada el "+fecha_solicitud_aspvh+" ";
 					 System.out.println(detallePermiso);
				
				
		System.out.println("Enviando Correo.........");
				
		try {
			/*util.EnviaMail(envMail, correo, 
							"GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE",
							emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh), 
							null);*/
				String str_mail=correo;
				envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
				envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh));
				envMail.setPara(str_mail);
				pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
				
				if(obj.getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			} catch (Exception e) {
			System.out.println("Error en el envío de correo"+e.getMessage());
			}
				
			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}
		
	}
	
	
	
	
	
		
		
	
	
	
	
public String retornaDatosCorreoEmpleado(String IDE_GTEMP){

		
		String detallePermiso="";
		String nombreTipoSolicitud;
		String nroHoras;
		String motivo;
		String obtieneNroDias;
	
		//Estructura de mensaje
		String strNombreEmpleado="";
		
		//obtengo el empleado del cual requiero los datos
		TablaGenerica tab_empleado = ser_empleado.getEmpleado(IDE_GTEMP);
		String documento = tab_empleado.getValor("documento_identidad_gtemp");
		String primer_nombre_empleado= tab_empleado.getValor("primer_nombre_gtemp").toString(); 
		String segundo_nombre_empleado= tab_empleado.getValor("segundo_nombre_gtemp").toString();
		String apellido_paterno_empleado= tab_empleado.getValor("apellido_paterno_gtemp").toString(); 
		String apellido_materno_empleado= tab_empleado.getValor("apellido_materno_gtemp").toString();
		strNombreEmpleado=primer_nombre_empleado+" "+segundo_nombre_empleado+" "+apellido_paterno_empleado+"  "+apellido_materno_empleado;
		return strNombreEmpleado;


	
	}
	



	public void aceptarAprobarSolicitudTalento(){
	
		
		try {
			TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
			  TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot,num_max_dias_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
			  int   num_max_aplica_dias=Integer.parseInt(tab_motivo.getValor("num_max_dias_asmot"));
	         int nro_dias=0;
	         //empleado aprueba por talento humano
	 		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
			
	         if (tab_permisos.getValor("TIPO_ASPVH").equals("4")) {
	        	 nro_dias=Integer.parseInt(tab_permisos.getValor("nro_dias_aspvh"));	
			}else {
				nro_dias=1;
			}
			
	         int motivo=Integer.parseInt(tab_permisos.getValor("ide_asmot"));
			
	        //Permiso solictud de Vacacion y Personal
            String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
            String esvacacion=tab_motivo.getValor("es_vacacion_asmot");
            

		    //Solicitud de Permiso Vacaciones o Personal
            if ((esvacacion.equals("true"))){
            	aplicarVacacion();
            	return;
            }
			//Validacion si el permiso aplica o no a vacacion
             if((Boolean)chk_aplica_vacacion.getValue()){
				aplicarVacacion();
				return;
            }
            
      		
     			if (tab_permisos.getValor("TIPO_ASPVH").equals("4")) {
     			     utilitario.getConexion().ejecutarSql("update ASI_PERMISOS_VACACION_HEXT set  "
                      		+ "aprobado_tthh_aspvh=true,gen_ide_geedp3="+ide_geedp_activo+"   "
                      	    + "where ide_aspvh="+tab_permisos.getValorSeleccionado());
                 
     			}else {
     			     utilitario.getConexion().ejecutarSql("update ASI_PERMISOS_VACACION_HEXT set  "
                      		+ "aprobado_tthh_aspvh=true,gen_ide_geedp3="+ide_geedp_activo+","
                      				+ "nro_dias_aspvh="+1+"  "
                      	    + "where ide_aspvh="+tab_permisos.getValorSeleccionado());
                 
				}
     			guardarPantalla();
			dia_aplica_vacion.cerrar();	
			con_aprobar_solicitud.cerrar();
       			 utilitario.agregarMensaje("Aprobación de Solicitud realizada con exito", "");
			//comente correo
       			 EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));
       			 
     			tab_permisos.setCondicion("aprobado_tthh_aspvh=false and anulado_aspvh=false and fecha_desde_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
    					+ "and APROBADO_ASPVH=TRUE and APROBADO_TTHH_ASPVH=FALSE AND ANULADO_ASPVH=FALSE");
    			tab_permisos.ejecutarSql();
    		//	tab_permisos.actualizar();
    			tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
    			tab_permiso_justificacion.ejecutarSql();
       			 utilitario.addUpdate("tab_permisos,tab_permiso_justificacion");     
		 	     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
		}
		
		
	}

	
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		actualizarTabla2();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		actualizarTabla2();
	}

	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		try {
			super.inicio();
			actualizarTabla2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);
		}
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarTabla2();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 0000000000000000
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		try {
			super.siguiente();
			actualizarTabla2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error en siguiente "+e);
		}
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarTabla2();
	}

public void actualizarTabla2(){
	tab_permiso_justificacion.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
	tab_permiso_justificacion.ejecutarSql();
	//tab_permiso_justificacion.ejecutarValorForanea("IDE_ASPVH="+tab_permisos.getValorSeleccionado());

	
}

public void actualizarTabla3(){
	tab_permisos.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
	tab_permisos.ejecutarSql();		
	utilitario.addUpdate("tab_permisos");
}

	String ide_geedp_activo="";
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiarPantalla() {
		tab_permisos.limpiar();
		tab_permiso_justificacion.limpiar();
		ide_geedp_activo="";
		sel_cal.Limpiar();

		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	
		if (tab_permisos.getValorSeleccionado()!=null) {
	
		
		
		
		
		if(tab_permiso_justificacion.isFocus()){
			tab_permiso_justificacion.insertar();
	}	
		}
		
		else {
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "No existen datos para guardar");
		}
	
	}
	

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
						
			
		
		
		if (tab_permiso_justificacion.guardar()) {
							guardarPantalla();
		}
		
	
	
	
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
	}



	public Tabla getTab_permisos() {
		return tab_permisos;
	}

	public void setTab_permisos(Tabla tab_permisos) {
		this.tab_permisos = tab_permisos;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Detalle Permisos")){
			if (tab_permisos.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();		
					p_parametros.put("IDE_GEEDP",ide_geedp_activo);
					p_parametros.put("titulo", " BIESS GERENCIA ADMINISTRATIVA - FINANCIERA DEPARTAMENTO DE TALENTO HUMANO PERMISOS");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						

					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No contiene registro de permisos");
			}	

		}else if (rep_reporte.getReporteSelecionado().equals("Detalle Permisos Fecha")){

			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();	
				dia_filtro_activo.dibujar();
			}
			else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!=null && ! lis_activo.getSeleccionados().isEmpty()){
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					sel_cal.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}



			} else if (sel_cal.isVisible()) {
				if(sel_cal.isFechasValidas()){

					p_parametros.put("APROBACION",sel_cal.getFecha1String());
					p_parametros.put("VENCIMIENTO",sel_cal.getFecha2String());
					System.out.println("fecha 1:"+sel_cal.getFecha1String());
					System.out.println("fecha 2:"+sel_cal.getFecha2String());
					//				sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
					set_empleado_asis.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,  " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' ||  " +
							"PRIMER_NOMBRE_GTEMP || ' ' ||  " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES  " +
							"from GTH_EMPLEADO " +
							"WHERE ACTIVO_GTEMP IN("+lis_activo.getSeleccionados()+") " +
							"ORDER BY IDE_GTEMP ASC, " +
							"NOMBRES ASC ");
					set_empleado_asis.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado_asis.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado_asis.getTab_seleccion().ejecutarSql();

					set_empleado_asis.getBot_aceptar().setMetodo("aceptarReporte");
					sel_cal.cerrar();
					set_empleado_asis.dibujar();
				}else  {
					utilitario.agregarMensajeInfo("ERROR AL GENERAR REPORTE","Las fechas no son validas");
				}
			}
			else if(set_empleado_asis.isVisible()){
				if(set_empleado_asis.getSeleccionados()!=null && !set_empleado_asis.getSeleccionados().isEmpty()){

					System.out.println(""+set_empleado_asis.getSeleccionados());

					p_parametros.put("IDE_GTEMP",set_empleado_asis.getSeleccionados());
					p_parametros.put("titulo", " BIESS GERENCIA ADMINISTRATIVA - FINANCIERA  DEPARTAMENTO DE TALENTO HUMANO PERMISOS POR FECHA");
					System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					set_empleado_asis.cerrar();
					sef_reporte.dibujar();

				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
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

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}

	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
	}

	public SeleccionTabla getSet_empleado_asis() {
		return set_empleado_asis;
	}

	public void setSet_empleado_asis(SeleccionTabla set_empleado_asis) {
		this.set_empleado_asis = set_empleado_asis;
	}

	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}

	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}
	
	
	public Dialogo getDia_anulado() {
		return dia_anulado;
	}

	public void setDia_anulado(Dialogo dia_anulado) {
		this.dia_anulado = dia_anulado;
	}

	public Tabla getTab_permiso_justificacion() {
		return tab_permiso_justificacion;
	}

	public void setTab_permiso_justificacion(Tabla tab_permiso_justificacion) {
		this.tab_permiso_justificacion = tab_permiso_justificacion;
	}

	public void calcularDiasPermisos(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);
		
		}

	public void calcularDiasPermisos(DateSelectEvent evt){
		tab_permisos.modificar(evt);
		
	
	}
	
	
	
	
	public void calculoHoras(String str_hora_inicial , String str_hora_final){
		try {
			Date hora_inicial= utilitario.getHora(utilitario.getFormatoHora(str_hora_inicial));
			Date hora_final= utilitario.getHora(utilitario.getFormatoHora(str_hora_final));
			int total_segundos_hora_inicial=(hora_inicial.getHours()*3600)+(hora_inicial.getMinutes()*60) + hora_inicial.getSeconds();
			int total_segundos_hora_final=(hora_final.getHours()*3600)+(hora_final.getMinutes()*60)+hora_final.getSeconds();


			int total_diferencia_segundo=total_segundos_hora_final-total_segundos_hora_inicial;


			int total_horas=total_diferencia_segundo/3600;
			int nuevo_valor=total_diferencia_segundo-(total_horas*3600);
			int total_minutos=nuevo_valor/60;
			int total_segundos=nuevo_valor-(total_minutos*60);

			double total_diferencia_segundos=((total_horas*3600)+(total_minutos*60)+total_segundos);
			double total_diferencia_horas=total_diferencia_segundos/3600;

			tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH",total_diferencia_horas+"");
			utilitario.addUpdateTabla(tab_permisos,"NRO_HORAS_ASPVH", total_diferencia_horas+"");
		} catch (Exception e) {
			// TODO: handle exception
			tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH","");
			utilitario.addUpdateTabla(tab_permisos,"NRO_HORAS_ASPVH", "");
		}
	}
	public  void calaculahoras(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);		
		if(tab_permisos.getValor("HORA_DESDE_ASPVH")!=null && !tab_permisos.getValor("HORA_DESDE_ASPVH").isEmpty()
				&& tab_permisos.getValor("HORA_HASTA_ASPVH")!=null && !tab_permisos.getValor("HORA_HASTA_ASPVH").isEmpty()){
			if (!isHoraMayor(tab_permisos.getValor("HORA_DESDE_ASPVH"),tab_permisos.getValor("HORA_HASTA_ASPVH"))) {
						
				calculoHoras(tab_permisos.getValor("HORA_DESDE_ASPVH"), tab_permisos.getValor("HORA_HASTA_ASPVH"));
			}else {
				utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
			}	
		}
	}

	public  void calaculahoras(SelectEvent evt){
		tab_permisos.modificar(evt);
		if (!isHoraMayor(tab_permisos.getValor("HORA_DESDE_ASPVH"),tab_permisos.getValor("HORA_HASTA_ASPVH"))) {
			calculoHoras(tab_permisos.getValor("HORA_DESDE_ASPVH"), tab_permisos.getValor("HORA_HASTA_ASPVH"));
			tab_permisos.setColumnaSuma("NRO_HORAS_ASPVH");
		}
		else {
			utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
		}
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


	public void aplicaVacacionHoras(){
		
		
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
		TablaGenerica tab_consulta_vacacion = utilitario.consultar("select * from gth_empleado where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_GTEMP=TRUE");
		boolean banderaPeriodoVacacion=false;
		TablaGenerica tab_codigo_vacacion=null;
		if (tab_consulta_vacacion.getTotalFilas()>0) {
	tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=TRUE order by ide_asvac desc limit 1");
	banderaPeriodoVacacion=true;
	}else {
		tab_codigo_vacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=FALSE order by ide_asvac desc limit 1");
		banderaPeriodoVacacion=true;
	}	


		if(banderaPeriodoVacacion==true){
			
		double nro_horas_aspvh=Double.parseDouble( tab_permisos.getValor("nro_horas_aspvh"));	
		double valor;
		
		if (nro_horas_aspvh>=8) {
			valor=1.0;	
		}else {
			valor=(Double.parseDouble( tab_permisos.getValor("nro_horas_aspvh"))/8);
		}
		
		
//    TablaGenerica tab_codigo_vacacion=utilitario.consultar("select ide_asvac,ide_gtemp,dias_pendientes_asvac from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"))+")");
	double dias_pendientes=0.0;
	int fines_semana=0;
	double dias_tomados_asvac=0.0;
	double calculoNuevoFinesdeSemana=0.0;
	TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));

	

    utilitario.getConexion().ejecutarSql("update ASI_PERMISOS_VACACION_HEXT set  "
      		+ "aprobado_tthh_aspvh=true,gen_ide_geedp3="+ide_geedp_activo+","
      				+ "nro_dias_aspvh=1,"
      				+ "registro_novedad_aspvh=true "
      	    + "where ide_aspvh="+tab_permisos.getValorSeleccionado());
	guardarPantalla();
			   	

			if(((Boolean)chk_aplica_vacacion_sabado.getValue())==null ){
				chk_aplica_vacacion_sabado.setValue(false);
			}
			if(((Boolean)chk_aplica_vacacion_domingo.getValue())==null ){
				chk_aplica_vacacion_domingo.setValue(false);
				}
			if(((Boolean)chk_aplica_vacacion_sabado_domingo.getValue())==null ){
				chk_aplica_vacacion_sabado_domingo.setValue(false);
				}
			

/////////////////////////////////////////////////////////////////aplica sabado///////////////////////////////////////////////////////
				if(((Boolean)chk_aplica_vacacion_sabado.getValue())){
				 TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+valor+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN' ,true,true)");
				 guardarPantalla();
			}
			
/////////////////////////////////////////////////////////////////aplica domingo///////////////////////////////////////////////////////

				else if(((Boolean)chk_aplica_vacacion_domingo.getValue())){
										
				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+valor+",'APLICACIÓN DE PERMISOS DÍA DOMINGO CON CARGO A VACACIÓN' ,true,true)");
				guardarPantalla();
			}		



			else {
			
			
			// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),1);
int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),1);
			//Solo dias laborables excluye sabados y domingos para hacer el calculo
double numeroDiasRealesSinSabadoDomingo=1-(dias_sabados+dias_domingos);
			
			// valido si le hago cargo a vacacion de acuerdo al dia si es un fin de semana

			if (numeroDiasRealesSinSabadoDomingo==0) {
TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+valor+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");					
				guardarPantalla();
			}else {

				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
						+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+valor+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true, false)");
						guardarPantalla();
			}
	
			}
		    utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones por horas exitosamente");
		    dia_aplica_anulacion_parcial.cerrar();
			dia_anular_dias_solicitados.cerrar();
			dia_aplica_vacion_fin_semana.cerrar();
			con_aplicar_vacacion.cerrar();
			con_guardar.cerrar();
			dia_aplica_vacion.cerrar();
			con_aprobar_solicitud.cerrar();
			EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));
			tab_permisos.setCondicion("aprobado_tthh_aspvh=false and anulado_aspvh=false and fecha_desde_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
					+ "and APROBADO_ASPVH=TRUE and APROBADO_TTHH_ASPVH=FALSE AND ANULADO_ASPVH=FALSE");
			tab_permisos.ejecutarSql();
			//tab_permisos.actualizar();
			tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
			tab_permiso_justificacion.ejecutarSql();
			utilitario.addUpdate("tab_permisos,dia_aplica_anulacion_parcial,dia_anular_dias_solicitados,dia_aplica_vacion_fin_semana,con_guardar,con_aplicar_vacacion,con_aprobar_solicitud,dia_aplica_vacion");
		}
		else{
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones contactese con el administrador");
		}
			}

	
	

	
	
	
	

	public Confirmar getCon_aplicar_vacacion() {
		return con_aplicar_vacacion;
	}

	public void setCon_aplicar_vacacion(Confirmar con_aplicar_vacacion) {
		this.con_aplicar_vacacion = con_aplicar_vacacion;
	}


	public Confirmar getCon_aprobar_solicitud() {
		return con_aprobar_solicitud;
	}

	public void setCon_aprobar_solicitud(Confirmar con_aprobar_solicitud) {
		this.con_aprobar_solicitud = con_aprobar_solicitud;
	}

	public Dialogo getDia_aplica_vacion() {
		return dia_aplica_vacion;
	}

	public void setDia_aplica_vacion(Dialogo dia_aplica_vacion) {
		this.dia_aplica_vacion = dia_aplica_vacion;
	}

	


	public Dialogo getDia_aplica_anulacion_parcial() {
		return dia_aplica_anulacion_parcial;
	}


	public void setDia_aplica_anulacion_parcial(Dialogo dia_aplica_anulacion_parcial) {
		this.dia_aplica_anulacion_parcial = dia_aplica_anulacion_parcial;
	}



	
	
	public Dialogo getDia_anular_dias_solicitados() {
		return dia_anular_dias_solicitados;
	}


	public void setDia_anular_dias_solicitados(Dialogo dia_anular_dias_solicitados) {
		this.dia_anular_dias_solicitados = dia_anular_dias_solicitados;
	}
	
	
	
public int getDiasSabado(String FechaSolicitud,int nro_dias_aspvh){ 	
	String dateInString = FechaSolicitud;
    
    
int dias_asignados=nro_dias_aspvh;		
int calculo_sabados=0;
int calculo_domingos=0; 		  
for (int i = 0; i < dias_asignados; i++) {
	
		   

StringTokenizer st = new StringTokenizer(dateInString, "-");
		    String año = st.nextToken(); 
		    String mes = st.nextToken(); 
		    String dia = st.nextToken();		
	
		    Calendar cal = GregorianCalendar.getInstance(); 
		    cal.set(Integer.parseInt(año), Integer.parseInt(mes) - 1 , Integer.parseInt(dia)+i); 
		    //System.out.println(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES")));
		    
		    String fin_semana=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
		    
		    if (fin_semana.equalsIgnoreCase("sábado")) {
				calculo_sabados++;
			}
		    
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
		    return calculo_sabados;
}




public int getDiasDomingo(String FechaSolicitud,int nro_dias_aspvh){ 	
	String dateInString = FechaSolicitud;
    
    
int dias_asignados=nro_dias_aspvh;		
int calculo_sabados=0;
int calculo_domingos=0; 		  
for (int i = 0; i < dias_asignados; i++) {
	
StringTokenizer st = new StringTokenizer(dateInString, "-");
		    String año = st.nextToken(); 
		    String mes = st.nextToken(); 
		    String dia = st.nextToken();		
	
		    Calendar cal = GregorianCalendar.getInstance(); 
		    cal.set(Integer.parseInt(año), Integer.parseInt(mes) - 1 , Integer.parseInt(dia)+i); 
		    //System.out.println(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES")));
		    
		    String fin_semana=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
		    return calculo_domingos;
}




public Confirmar getCon_guardar() {
	return con_guardar;
}




public void setCon_guardar(Confirmar con_guardar) {
	this.con_guardar = con_guardar;
}




public void setteraVacacion(){
	//  dia_aplica_vacion_fin_semana.cerrar();
	  con_aplicar_vacacion.cerrar();
	  con_guardar.cerrar();
	  dia_motivo_dias_solicitados.cerrar();
	tex_num_dias_permitidos.limpiar();
		tex_num_dias_cargo_vacacion.limpiar();
utilitario.addUpdate("dia_aplica_vacion_fin_semana,con_guardar,con_aplicar_vacacion,dia_motivo_dias_solicitados");
	  
}




public void getDiaFinSemanaDescontar(){
	  
	
	panGri1.getChildren().clear();
	utilitario.addUpdate("panGri1");
	
	
	//TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
	String NroDias=tab_permisos.getValor("nro_dias_aspvh");
	double valor=0.0;
	if (NroDias==null || NroDias.isEmpty() || NroDias.equals("")) {
		double horas=Double.parseDouble( tab_permisos.getValor("nro_horas_aspvh"));
		double division=horas/8;
		int total= (int)division;
		if (total==0) {
			valor=1.0;
			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set nro_dias_aspvh="+valor+"  where ide_aspvh="+tab_permisos.getValorSeleccionado());
			guardarPantalla();
		}
	}else {
		valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));		
	}
	

	
	//if (valor==null) {
//		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
//	}else {
//		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
			
//	}
	
	//if(tab_consulta_vacacion.getTotalFilas()>0){
	String detalle_aspvh=tab_permisos.getValor("detalle_aspvh");
    int nro_dias_aspvh=(int)valor;
			
// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);


	if((dias_sabados==1)   || (dias_domingos==1)){
		
	if ((dias_sabados==1) && (dias_domingos==0)) {
		panGri1.getChildren().add(new Etiqueta("Aplica dia Sabado"));
		panGri1.getChildren().add(chk_aplica_vacacion_sabado);
		dia_aplica_vacion_fin_semana.dibujar();
		utilitario.addUpdate("dia_aplica_vacion_fin_semana");
			
	}
		
	else if ((dias_sabados==0) && (dias_domingos==1)) {
		panGri1.getChildren().add(new Etiqueta("Aplica dia Domingo"));
		panGri1.getChildren().add(chk_aplica_vacacion_domingo);
		dia_aplica_vacion_fin_semana.dibujar();
		utilitario.addUpdate("dia_aplica_vacion_fin_semana");
		}

	else if ((dias_sabados==1) && (dias_domingos==1)) {
		panGri1.getChildren().add(new Etiqueta("Aplica día Sábado y Domingo"));
		panGri1.getChildren().add(chk_aplica_vacacion_sabado_domingo);
		dia_aplica_vacion_fin_semana.dibujar();
		utilitario.addUpdate("dia_aplica_vacion_fin_semana");
		}
	
	}

else{
	
	con_guardar.setMessage("Esta Seguro de Aplicar La Solicitud a Vacación");
	con_guardar.setTitle("CONFIRMACION APLICACIÓN A VACACIÓN");
	con_guardar.getBot_aceptar().setMetodo("aplicarVacacion");
	con_guardar.getBot_cancelar().setMetodo("setteraVacacion");
	con_guardar.dibujar();
	utilitario.addUpdate("con_guardar");
	}
	//}	

}




public Dialogo getDia_aplica_vacion_fin_semana() {
	return dia_aplica_vacion_fin_semana;
}




public void setDia_aplica_vacion_fin_semana(Dialogo dia_aplica_vacion_fin_semana) {
	this.dia_aplica_vacion_fin_semana = dia_aplica_vacion_fin_semana;
}

    
    
	
public Dialogo getDia_motivo_dias_solicitados() {
	return dia_motivo_dias_solicitados;
}




public void setDia_motivo_dias_solicitados(Dialogo dia_motivo_dias_solicitados) {
	this.dia_motivo_dias_solicitados = dia_motivo_dias_solicitados;
}

    
    
	
public int getMotivo_calamidad() {
	return motivo_calamidad;
}




public void setMotivo_calamidad(int motivo_calamidad) {
	this.motivo_calamidad = motivo_calamidad;
}




public int getMotivo_asistencia() {
	return motivo_asistencia;
}




public void setMotivo_asistencia(int motivo_asistencia) {
	this.motivo_asistencia = motivo_asistencia;
}

    
public void permisosAprobados(){
	tab_permisos.setCondicion("aprobado_tthh_aspvh=true and anulado_aspvh=false "
			+ "and APROBADO_ASPVH=TRUE and fecha_solicitud_aspvh between '"+cal_fecha_inicial.getFecha()+"'  "
			+ "and '"+cal_fecha_final.getFecha()+"'");
	tab_permisos.ejecutarSql();
	tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
	tab_permiso_justificacion.ejecutarSql();
	utilitario.addUpdate("tab_permisos,tab_permiso_justificacion");
}

public void permisosSinAprobar(){

	TablaGenerica tab_permisos_x_dia=utilitario.consultar("select ide_aspvh,ide_gtemp from asi_permisos_vacacion_hext where  tipo_aspvh=1 and nro_dias_aspvh is null");
	if(tab_permisos_x_dia.getTotalFilas()>0){
	utilitario.getConexion().ejecutarSql("update asi_permisos_vacacion_hext set nro_dias_aspvh=1 where  tipo_aspvh=1 and nro_dias_aspvh is null");
	//tab_permisos.setCondicion("APROBADO_ASPVH=TRUE and APROBADO_TTHH_ASPVH=FALSE AND ANULADO_ASPVH=FALSE and fecha_solicitud_aspvh >= '2019-06-01'");
	tab_permisos.ejecutarSql();
	//tab_permisos.actualizar();
//	utilitario.addUpdate("tab_permisos");
	}else {
		
	}
	tab_permisos.setCondicion("aprobado_tthh_aspvh=false and anulado_aspvh=false and fecha_solicitud_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
			+ "and APROBADO_ASPVH=TRUE");
	tab_permisos.ejecutarSql();
	tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
	tab_permiso_justificacion.ejecutarSql();
	utilitario.addUpdate("tab_permisos,tab_permiso_justificacion");
}

    
	
public void seleccionarFechasReporte(){
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
			fechaIni=(cal_fecha_inicial.getFecha());	
			fechaFin=(cal_fecha_final.getFecha());
  	
		tab_permisos.setCondicion("fecha_solicitud_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "AND APROBADO_ASPVH=TRUE");
		tab_permisos.ejecutarSql();		
		tab_permiso_justificacion.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
		tab_permiso_justificacion.ejecutarSql();
		
		
		}	 else {
			utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Fecha inicial debe ser menor a fecha final");
			return;
		
	} 	
	 	
		}else {
				utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
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
