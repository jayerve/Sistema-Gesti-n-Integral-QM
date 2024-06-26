package paq_asistencia;
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
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_aplicar_vacacion=new Confirmar();
	//private Confirmar con_guardar_solicitud=new Confirmar();
	private Confirmar con_aprobar_solicitud=new Confirmar();

	//private Confirmar con_=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_filtro_activo = new Dialogo();
	
	private Dialogo dia_aplica_vacion = new Dialogo();
	
	//private Dialogo dia_aplica_justificacion = new Dialogo();
	//ANULACION
	
	private Dialogo dia_aplica_anulacion_parcial = new Dialogo();
	private Dialogo dia_anular_dias_solicitados =  new Dialogo();
	
	


	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_asis=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private Tabla tab_permiso_justificacion=new Tabla();
	//private Tabla tab_permiso_justificacion_dialogo=new Tabla();
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
	
	//Anulacion
	
	private Texto tex_num_dias_anula=new Texto();
	private AreaTexto are_tex_num_dias_solicitados=new AreaTexto();

	
	//Control de Asistencia
	int nda=0;
	
    int p_asi_dias_max_vacaciones_losep;
    int p_asi_dias_max_vacaciones_codigo_trabajo;
    int p_asi_dias_anio;
	String fecha_ingreso="";
	Integer  nde;


	
	
	
	private String ide_gtemp_sel;
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	private Check chk_aplica_vacacion = new Check();
	private Check chk_aplica_anulacion_parcial = new Check();


	
	public pre_permisos_tthh() {
		try{

	//Inicio los valores de asistencia
			

		Boton bot_anulado=new Boton();
		bot_anulado.setValue("ANULAR SOLICITUD");
		bot_anulado.setMetodo("anularSolicitud");

		Boton bot_aprobacion_talento_humano=new Boton();
		bot_aprobacion_talento_humano.setValue("APROBACION TALENTO HUMANO");
		bot_aprobacion_talento_humano.setMetodo("aprobacionTalentoHumano");
		
		Boton bot_aplica_vacacion=new Boton();
		bot_aplica_vacacion.setValue("APLICA VACACION");
		bot_aplica_vacacion.setMetodo("aplicaVacacion");

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiarPantalla");




		Etiqueta eti_colaborador=new Etiqueta("OPCIONES TALENTO HUMANO:");
		bar_botones.agregarComponente(eti_colaborador);
		//bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");
		bar_botones.agregarBoton(bot_anulado);
		bar_botones.agregarBoton(bot_aprobacion_talento_humano); 

	//  PERMISOS (division 1)

			tab_permisos.setId("tab_permisos");
			tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		    tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
		    tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
			tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
			tab_permisos.getColumna("GEN_IDE_GEEDP").setVisible(false);
			tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
			tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
			tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
			tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
			
			
			tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
			tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
			//		tab_permisos.getColumna("GEN_IDE_GEEDP").setLectura(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
			//		tab_permisos.getColumna("GEN_IDE_GEEDP2").setLectura(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
			//GERENTE DE AREA GEN_IDE_GEEDP3
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);

			tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);
			//tab_permisos.getColumna("IDE_ASMOT").setRequerida(true);		
			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("calcularDiasPermisos");
			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setMetodoChange("calcularDiasPermisos");
			tab_permisos.getColumna("HORA_DESDE_ASPVH").setVisible(true);
			tab_permisos.getColumna("HORA_DESDE_ASPVH").setMetodoChange("calaculahoras");

			tab_permisos.getColumna("HORA_HASTA_ASPVH").setVisible(true);
			tab_permisos.getColumna("HORA_HASTA_ASPVH").setMetodoChange("calaculahoras");

			tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);

			tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
			tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
			tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
			tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
			tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));


			tab_permisos.getColumna("NRO_FINES_SEMANA_ASPVH").setEtiqueta();
			tab_permisos.getColumna("NRO_FINES_SEMANA_ASPVH").alinearCentro();

			//campos solo de lectura
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setValorDefecto("false");
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setLectura(true);
			
			tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
			tab_permisos.getColumna("ANULADO_ASPVH").setValorDefecto("false");
			tab_permisos.getColumna("ANULADO_ASPVH").setLectura(true);
			
		
			tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
			tab_permisos.getColumna("APROBADO_ASPVH").setLectura(true);

			tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
			tab_permisos.getColumna("ACTIVO_ASPVH").setLectura(true);
			
			
			tab_permisos.getColumna("IDE_GEMES").setVisible(false);
			tab_permisos.getColumna("IDE_GEANI").setVisible(false);
			tab_permisos.getColumna("IDE_GEEST").setVisible(false);
			tab_permisos.getGrid().setColumns(4);
			tab_permisos.setTipoFormulario(true);
			tab_permisos.setLectura(true);
			tab_permisos.setCondicion("APROBADO_ASPVH=TRUE");
			tab_permisos.setCampoOrden("IDE_ASPVH desc");
			tab_permisos.agregarRelacion(tab_permiso_justificacion);

			tab_permisos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("SOLICITUD DE PERMISOS ONLINE");


		//Permiso de Justificacion 

		tab_permiso_justificacion.setId("tab_permiso_justificacion");
		tab_permiso_justificacion.setTabla("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ", 2);
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setCheck();
		tab_permiso_justificacion.getColumna(" ACTIVO_ASPEJ").setValorDefecto("true");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setUpload("archivos");
		tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permiso_justificacion.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_permiso_justificacion);
		pat_panel2.setMensajeWarn("JUSTIFICACION DE PERMISOS ONLINE");

		
		
		
		
		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
	//    div_division.dividir1(pat_panel1);
		agregarComponente(div_division);


		// confirmacion para guardar datos
		con_aplicar_vacacion.setId("con_aplicar_vacacion");
		agregarComponente(con_aplicar_vacacion);
	
		Etiqueta eti_aplica_vacacion=new Etiqueta("Aplicar cargo a vacaciones");
		chk_aplica_vacacion.setId("chAplicaVacacion");
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
		con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
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
	
		//Botones de Acciones para TTHH
		//bar_botones.agregarBoton(bot_aprobar_solicitud);

		//bar_botones.agregarBoton(bot_aplica_vacacion); 
		//bar_botones.agregarBoton(bot_deshacer_aplica_vacacion); 
		
	
		/// dialogo de anula

		cal_fecha_anula.setId("cal_fecha_anula");

		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:200px;");

		Grid gri_anular_horas_extra=new Grid();
		gri_anular_horas_extra.setColumns(1);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
		//gri_anular_horas_extra.getChildren().add(new Etiqueta("DOCUMENTO DE ANULACIÓN"));
		//gri_anular_horas_extra.getChildren().add(tex_documento_anula);
		//gri_anular_horas_extra.getChildren().add(new Etiqueta("FECHA DE ANULACIÓN"));
		//gri_anular_horas_extra.getChildren().add(cal_fecha_anula);

		dia_anulado.setId("dia_anulado");
		dia_anulado.setDialogo(gri_anular_horas_extra);
		dia_anulado.setWidth("25%");
		dia_anulado.setHeight("25%");
		dia_anulado.setTitle("ANULACIÓN DE SOLICITUD DE PERMISOS");
		dia_anulado.getBot_aceptar().setMetodo("aceptarAnulacionHorasPermisos");				
		dia_anulado.setDynamic(false);
		gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anulado);
		
		
		//Aplica anulacion parcial
		
		
		Etiqueta eti_aplica_anulacion_parcial=new Etiqueta("ANULACIÓN PARCIAL");
		chk_aplica_anulacion_parcial.setId("chAplicaAnulacionParcial");
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
		gri_anular_dias_solicitados.getChildren().add(eti_num_dias_vacacion);
		gri_anular_dias_solicitados.getChildren().add(new Etiqueta("NRO DE DIAS TOMADOS A RECALUCULAR: "));
		gri_anular_dias_solicitados.getChildren().add(tex_num_dias_anula);
		//dia_anular_dias_solicitados.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		
		

		dia_anular_dias_solicitados.setWidth("30%");
		dia_anular_dias_solicitados.setHeight("20%");
		dia_anular_dias_solicitados.setTitle("INGRESO EL NRO DIAS TOMADOS A RECALCULAR");
		dia_anular_dias_solicitados.getGri_cuerpo().getChildren().add(gri_anular_dias_solicitados);
		dia_anular_dias_solicitados.getBot_aceptar().setMetodo("aceptarDescuentoAnulacionParcial");				
		dia_anular_dias_solicitados.setDynamic(false);
		gri_anular_dias_solicitados.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anular_dias_solicitados);
	

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error en el constructor "+e);
		}
	}
	
	

	
	
	public Check getChk_aplica_vacacion() {
		return chk_aplica_vacacion;
	}

	public void setChk_aplica_vacacion(Check chk_aplica_vacacion) {
		this.chk_aplica_vacacion = chk_aplica_vacacion;
	}

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
		
		if (dias>dias_solicitados_vacaciones) {
 			utilitario.agregarMensajeError("Anulación Parcial de Vacaciones incorrecto", "El número de días debe ser mayor al solicitado ");
		    return;
		}else {
			aplicaAnulacionDias(dias);	
		}
		
		
		
	}
	
	
	
	public void validarAnulacionParcial(){
	
	if((Boolean)chk_aplica_anulacion_parcial.getValue()){
		/*
		//dia_aplica_anulacion_parcial.cerrar();
		//aplicarVacacion();	
		System.out.println("verifica el estado del check"+(Boolean)chk_aplica_anulacion_parcial.getValue());
		String numdias=tab_permisos.getValor("NRO_DIAS_ASPVH").toString();
		int dias=Integer.parseInt(numdias);
		eti_num_dias_vacacion.setValue(dias);
		System.out.println(tex_num_dias_anula.getValue());
		utilitario.addUpdate("eti_num_dias_vacacion");
		//dia_aplica_anulacion_parcial.cerrar();
		dia_anular_dias_solicitados.dibujar();
		utilitario.addUpdate("dia_anular_dias_solicitados");
		*/
		
		dia_aplica_anulacion_parcial.cerrar();
		//aplicarVacacion();	
		System.out.println("verifica el estado del check"+(Boolean)chk_aplica_anulacion_parcial.getValue());
		String numdias=tab_permisos.getValor("NRO_DIAS_ASPVH").toString();
		int dias=Integer.parseInt(numdias);
		eti_num_dias_vacacion.setValue(dias);
		System.out.println(tex_num_dias_anula.getValue());
		//utilitario.addUpdate("eti_num_dias_vacacion");
		
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
			utilitario.addUpdate("tab_permisos");
			utilitario.agregarMensaje("Anulación De Vacación", "Se descontó a vacaciones exitosamente");
            PruebaCorreo();
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
		
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		if(tab_permisos.getValor("registro_novedad_aspvh").equals("true")){
			utilitario.agregarMensajeInfo("Vacación Aplicada", "Ya se encuentra aplicada la vacación");
			return;
		}
		
		TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
		if(tab_consulta_vacacion.getTotalFilas()>0){
		String detalle_aspvh=tab_permisos.getValor("detalle_aspvh");
		String dias=tab_permisos.getValor("nro_dias_aspvh");
        int nro_dias_aspvh=Integer.parseInt(dias);
		double nro_horas_aspvh=Double.parseDouble(tab_permisos.getValor("nro_horas_aspvh"));

		
        System.out.println("Dias"+dias);
		
		
		
		
			TablaGenerica tab_codigo_vacacion=utilitario.consultar("select * from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
		double dias_pendientes=0.0;
		int fines_semana=0;
		double dias_tomados_asvac=0.0;
        double calculoNuevoFinesdeSemana=0.0;
			
            tab_permisos.setValor("registro_novedad_aspvh", "true");
			tab_permisos.setValor("aprobado_tthh_aspvh", "true");
		    tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
			tab_permisos.guardar();
				
			
		    
		    

///////////////////////////////////////////////////////////////////DIAS SOLICITADOS//////////////////////////////////////////////////////////////	
				
			
// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
//Solo dias laborables excluye sabados y domingos para hacer el calculo
double numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
			
// valido si le hago cargo a vacacion de acuerdo al dia si es un fin de semana
if (numeroDiasRealesSinSabadoDomingo==0) {
			guardarPantalla();
			
}else {
			
			TablaGenerica tab_detalle_vacacion_ajuste_fin_semana = new  TablaGenerica();
			tab_detalle_vacacion_ajuste_fin_semana.setTabla("asi_detalle_vacacion", "ide_asdev", -1);
			tab_detalle_vacacion_ajuste_fin_semana.setCondicion("ide_asdev=-1");
			tab_detalle_vacacion_ajuste_fin_semana.ejecutarSql();
			tab_detalle_vacacion_ajuste_fin_semana.insertar();		
			tab_detalle_vacacion_ajuste_fin_semana.setValor("ide_aspvh", tab_permisos.getValor("ide_aspvh"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("ide_asvac", tab_codigo_vacacion.getValor("ide_asvac"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("fecha_novedad_asdev",tab_permisos.getValor("fecha_solicitud_aspvh"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("dia_descontado_asdev", numeroDiasRealesSinSabadoDomingo+"");
			tab_detalle_vacacion_ajuste_fin_semana.setValor("observacion_asdev", "APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN");
			tab_detalle_vacacion_ajuste_fin_semana.setValor("activo_asdev", "true");	
			tab_detalle_vacacion_ajuste_fin_semana.guardar(); 
			guardarPantalla();
}
			
		
			
			//utilitario.addUpdate("tab_permisos");
			
		//}
		//utilitario.getConexion().agregarSqlPantalla("update asi_vacacion set dias_pendientes_asvac="+calculodiaspendientes+", fines_semana_asvac="+resultadofinessemana+" where ide_gtemp="+tab_permisos.getValor("ide_gtemp"));

		utilitario.addUpdate("tab_permisos");
		utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");
		dia_aplica_anulacion_parcial.cerrar();
		dia_anular_dias_solicitados.cerrar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones activo");
		}
		
	}
	
	
	public void aplicaAnulacionDias(int num_dias){

		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}

		if(tab_permisos.getValor("anulado_aspvh").equals("true")){
			utilitario.agregarMensajeInfo("El registro se encuentra anulado", "No se puede aplicar justificación.");
			return;
		}
		
	System.out.println("Ingresas a metodo  AplicaAnulacionDias");	

    //ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));

	//Dias solicitados con cargo a vacacion anterior
     	double diaDescontado=0.0;
		List<BigDecimal> tab_consulta=utilitario.getConexion().consultar("select dia_descontado_asdev from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("IDE_ASPVH"));      
    for(int i=0;i<tab_consulta.size();i++){
    	BigDecimal  resultado= (BigDecimal)(tab_consulta.get(i));
	    	diaDescontado=resultado.doubleValue(); 	
    }

	
	int nuevocalculohoras=0;
	//nuevocalculohoras=num_dias*8;
	//tab_permisos.setValor("registro_novedad_aspvh", "true");
	//tab_permisos.setValor("nro_dias_aspvh", ""+num_dias);
	//tab_permisos.setValor("nro_horas_aspvh",""+nuevocalculohoras);
	//tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
	//tab_permisos.guardar();

	String mensaje;
		mensaje="RECÁLCULO DE DIAS VACACIONES";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
	
	
	if(tab_consulta_vacacion.getTotalFilas()>0){
	
//fecha de recalculo
		Date fecha_nueva=utilitario.getDate();				
		String fechaCadena = sdf.format(fecha_nueva);
		System.err.println("sadsadsaadsds"+fechaCadena);
		utilitario.getConexion().agregarSqlPantalla("update asi_detalle_vacacion set dia_acumulado_asdev="+diaDescontado+",dia_descontado_asdev="+(double)num_dias+",observacion_asdev='"+mensaje+"',fecha_novedad_asdev='"+fechaCadena+"', anulado_asdev=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
	guardarPantalla();
	utilitario.addUpdate("tab_permisos");
		utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");
		}
		
	    else
	    {
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones activo");
		}
		
		dia_anular_dias_solicitados.cerrar();
		PruebaCorreo();
	}

	
	
	
	
	
	public void deshacerAplicaVacaciones(){
		
		
		System.out.println("Ingresa a metodo1111");

		
		
		
	
	}
	public void deshacerAplicaVacacion(){
		System.out.println("Ingresa a metodo");
		

		TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
		tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
		
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_tthh_aspvh=true,gen_ide_geedp3="+empleado_atual.getValor("ide_geedp")+"  where ide_aspvh="+tab_permisos.getValorSeleccionado());
		
		
		//aplicaVacacionHoras();
		
		
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
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Anular la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				
				
				 TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
	             String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
	             String esvacacion=tab_motivo.getValor("es_vacacion_asmot");
				
				
				
				
			if (aplica_vacaciones_asmot.equals("true")) {
				dia_aplica_anulacion_parcial.dibujar();
			}else {
				are_tex_razon_anula.setValue("");
				dia_anulado.dibujar();
				utilitario.addUpdate("dia_anulado");	
			}					
				
				//dia_anulado.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar los Permisos ", "El Empleado no Tiene Solicitudes");
			}			
		//}else{
		//	utilitario.agregarMensajeInfo("No se puede Anular los permisos ", "Debe seleccionar un Empleado");
		//}
	}

	public void aceptarAnulacionHorasPermisos(){
		
		TablaGenerica empleado_atual=ser_empleado.getPartida(tab_permisos.getValor("IDE_GTEMP"));
		tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
					tab_permisos.setValor("razon_anula_aspvh",are_tex_razon_anula.getValue().toString());
					tab_permisos.setValor("fecha_anula_aspvh",utilitario.getFechaActual());
					tab_permisos.modificar(tab_permisos.getFilaActual());
					tab_permisos.guardar();
					
					utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set anulado_aspvh=true,gen_ide_geedp3="+empleado_atual.getValor("ide_geedp")+" where ide_aspvh="+tab_permisos.getValorSeleccionado());	
					guardarPantalla();
					dia_anulado.cerrar();
					aplicaAnulacion();
					String ide_anterior=tab_permisos.getValorSeleccionado();		
					tab_permisos.ejecutarSql();
					tab_permisos.setFilaActual(ide_anterior);
					PruebaCorreo();
						
		} else {
			utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe ingresar una Razon para anular  los Permisos");
		}	
	}


	int estado_num_max_aplica_vacacion;
	public void aprobacionTalentoHumano(){
		//if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
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
		
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				
				
				
				
				
				
				//Empleado Activo
				ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));

				
				//Verifica si se aplica automticamente con cargo a vacaciones
				
             TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot,num_max_aplica_vacacion_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
             String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
             String esvacacion=tab_motivo.getValor("es_vacacion_asmot");


          
             
             
             /**
              * Validacion ingreso de maximo de justificacion para los tipos de permisos
              */
             
             
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateInString = tab_permisos.getValor("FECHA_HASTA_ASPVH");
           		System.out.println(dateInString);
            		Date date = null;
             		
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
                 	//System.out.println(dias);
             		//return (int) dias-5;
             		System.out.println("dias de trabajador" +(int)dias);
             		 
             int   num_max_aplica_vacacion=Integer.parseInt(tab_motivo.getValor("num_max_aplica_vacacion_asmot"));
             System.out.println("num_max_aplica_vacacion+1"+(num_max_aplica_vacacion+1));
             if (num_max_aplica_vacacion+1<=dias) {
            	 estado_num_max_aplica_vacacion=1;
            	 aceptarAprobarSolicitudTalento();
            	 
			}else{
             
             
             
             
             
                 
            if((aplica_vacaciones_asmot.equals("true"))) {
            	
            	 System.out.println("Ingresas Aqui");
            	 aceptarAprobarSolicitudTalento();
            
             }

				
			/*if ((tab_permisos.getValor("IDE_ASMOT").equals("13") ) || (tab_permisos.getValor("IDE_ASMOT").equals("14"))) {
				aceptarAprobarSolicitudTalento();
				
				}*/else {
					dia_aplica_vacion.dibujar();
					utilitario.addUpdate("dia_aplica_vacion");					
				}
				
				
				
				/*con_aplicar_vacacion.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
				con_aplicar_vacacion.setTitle("CONFIRMACION APROBACION DE SOLICITUD DE PERMISO");
				con_aplicar_vacacion.getBot_aceptar().setMetodo("abrirConfirmacion");
				//con_aplicar_vacacion.getBot_cancelar().setMetodo("aceptarAprobarSolicitudTalento");
				con_aplicar_vacacion.dibujar();*/
			}//if de dias max permitidos antes d que se aplique a vacacion
             
             
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}		
		//}else{
		//	utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		//}					
	}

	
	   /**
     * Metodo emailNotificacionAprobado
     */
    
   
	public String emailNotificacionAprobado(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh) {
        String html = "<p>&nbsp;</p>\n"
                + "<p>Estimado, "
                + "</p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>Notificamos mediante la presente que la solicitud de permisos/vacaciones:  "+detallePermiso+".</p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>&nbsp;</p>\n" 
                + "<p>Saludos cordiales,</p>\n"
                + "<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">Ing."+strNombreEmpleado+" </p>\n"
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

    
		
	
	
	

	public void PruebaCorreo(){

	
	//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
	//util.EnviaEmail("juan.ayerve@emgirs.com.ec", "PERMISOS Y VACACIONES",emailNotificacionAprobado(), null);
		String mensaje_anulacion="";
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP"));
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
		
		
	/*	EnvioMail envMail = new EnvioMail(Constantes.servidorSMTP, 
				Constantes.puertoEnvio, 
				Constantes.correo_envio, 
				Constantes.usuario_envio, 
				Constantes.clave_correo);
		*/
		

		
		
	//	EnvioMail envMail = new EnvioMail(("mail.emgirs.gob.ec"), ("587"),("permisos@emgirs.gob.ec"),("pemgirs"),("emgirs2017$"));

		
			try {
				
				String detallePermiso="";
				String nombreTipoSolicitud;
				String nroHoras;
				String motivo;
				String obtieneNroDias;
			
				//Estructura de mensaje
				String strNombreEmpleado="";
				String strNombreJefe="";
				
				/*
				//obtengo el empleado del cual requiero los datos
				TablaGenerica tab_empleado = ser_empleado.getEmpleado(tab_permisos.getValor("IDE_GTEMP"));
				String documento = tab_empleado.getValor("documento_identidad_gtemp");
				String primer_nombre_empleado= tab_empleado.getValor("primer_nombre_gtemp").toString(); 
				String segundo_nombre_empleado= tab_empleado.getValor("segundo_nombre_gtemp").toString();
				String apellido_paterno_empleado= tab_empleado.getValor("apellido_paterno_gtemp").toString(); 
				String apellido_materno_empleado= tab_empleado.getValor("apellido_materno_gtemp").toString();
				*/
				
				strNombreEmpleado = retornaDatosCorreoEmpleado(tab_permisos.getValor("IDE_GTEMP"));
					
				// armar mensaje
				String ide_aspvh_correo =tab_permisos.getValor("ide_aspvh").toString();
				String anulado_aspvh_correo =tab_permisos.getValor("anulado_aspvh").toString();
				String aprobado_tthh_aspvh_correo =tab_permisos.getValor("aprobado_tthh_aspvh").toString();
				String registro_novedad_aspvh =tab_permisos.getValor("registro_novedad_aspvh").toString();
				
				//Para solicitude de horas
				
				String fecha=tab_permisos.getValor("fecha_solicitud_aspvh").toString();
				Date fecha_nueva= utilitario.DeStringADate(fecha);
				Date fecha_solicitud_aspvh1 = fecha_nueva;
				 SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
				 String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
				//String nro_horas_aspvh =tab_permisos.getValor("nro_horas_aspvh").toString();
				
				
				//Para solicitude de dias
				String nro_dias_aspvh =tab_permisos.getValor("nro_dias_aspvh").toString();
				
				

							
				String estado_aprobacion_tthh="";
				
				
				if ((aprobado_tthh_aspvh_correo.equals("true")) && (anulado_aspvh_correo.equals("false"))) {
					estado_aprobacion_tthh="APROBADO";
					
				String  anuladoAsdev="";
				TablaGenerica tab_consulta=utilitario.consultar("select anulado_asdev from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("IDE_ASPVH"));      
				if (tab_consulta.getValor("anulado_asdev").equals("true")) {
					estado_aprobacion_tthh="RECALCULADO Y APROBADO";
				}
				    
				
				}
				if ((aprobado_tthh_aspvh_correo.equals("true")) && (anulado_aspvh_correo.equals("true"))) 
				 {
					estado_aprobacion_tthh="ANULADO";
				    mensaje_anulacion=" Debe Ingresar una nueva solicitud en las proximas 24h de los contrario usted se encuentra con inasistencia";
				}
				 
				if ((aprobado_tthh_aspvh_correo.equals("true")) && (registro_novedad_aspvh.equals("true"))) {
					estado_aprobacion_tthh="APROBADO CON CARGO A VACACIONES";
					
				}				 
					
			TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
				
				if (tab_permisos.getValor("tipo_aspvh").equals("1")) {
					 nombreTipoSolicitud="Permiso Por Horas ";
					 nroHoras= tab_permisos.getValor("nro_horas_aspvh"); 
					 motivo=tab_motivo.getValor("detalle_asmot");
					detallePermiso="  "+motivo+"  Nro:  "+ide_aspvh_correo+"  por un lapso de     "+nroHoras+" horas  se encuentra "+estado_aprobacion_tthh+"  "+fecha_solicitud_aspvh+" ";
				
				}
				if (tab_permisos.getValor("tipo_aspvh").equals("2")) {
					nombreTipoSolicitud="Solicitud A Vacaciones";
					obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");  
					 motivo=tab_motivo.getValor("detalle_asmot");

					detallePermiso="  "+motivo+"  Nro:  "+ide_aspvh_correo+"  por un lapso de     "+obtieneNroDias+" dias se encuentra "+estado_aprobacion_tthh+"  "+fecha_solicitud_aspvh+" ";
					
				}
				if (tab_permisos.getValor("tipo_aspvh").equals("4")) {
					nombreTipoSolicitud="Permiso Por Dias ";
					obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
					 motivo=tab_motivo.getValor("detalle_asmot");

					detallePermiso="  "+motivo+"  Nro:  "+ide_aspvh_correo+"  por un lapso de "+obtieneNroDias+" dias se encuentra "+estado_aprobacion_tthh+"  "+fecha_solicitud_aspvh+" ";

				}
				
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
				// TODO Auto-generated catch block
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
			tab_permisos.setValor("gen_ide_geedp3", empleado_atual.getValor("ide_geedp")); 
			
			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_tthh_aspvh=true,gen_ide_geedp3="+empleado_atual.getValor("ide_geedp")+"  where ide_aspvh="+tab_permisos.getValorSeleccionado());
			guardarPantalla();
			System.out.println("value aplica vacacion "+chk_aplica_vacacion.getValue());
			
		//VALIDACION CHECKBOX
			if((Boolean)chk_aplica_vacacion.getValue()){
				aplicarVacacion();	
			}
			
		//VALIDACION POR TIPO DE MOTIVO	
			/*if ((tab_permisos.getValor("IDE_ASMOT").equals("13") ) || (tab_permisos.getValor("IDE_ASMOT").equals("14"))) {
				aplicarVacacion();	
			
			}		*/	
			
			TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
			
            String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
            String esvacacion=tab_motivo.getValor("es_vacacion_asmot");


            if ((aplica_vacaciones_asmot.equals("true"))){
            	aplicarVacacion();
            }
			
			
			guardarPantalla();
			
			dia_aplica_vacion.cerrar();	
			con_aprobar_solicitud.cerrar();
			
			
			String ide_anterior=tab_permisos.getValorSeleccionado();		
			tab_permisos.ejecutarSql();
			tab_permisos.setFilaActual(ide_anterior);

			tab_permiso_justificacion.limpiar();
			
			utilitario.agregarMensaje("Aprobacion realizada con exito", "");
			
			//comente correo
			PruebaCorreo();
			//enviarCorreo();
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
	
}

public void actualizarTabla3(){
	tab_permisos.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
	tab_permisos.ejecutarSql();		
	utilitario.addUpdate("tab_permisos");
}

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		try {
			
	
			aut_empleado.onSelect(evt);
			ide_gtemp_sel = aut_empleado.getValor();
			ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
//	tab_permisos.setCondicion("TIPO_ASPVH=4 AND IDE_GTEMP="+aut_empleado.getValor());
			//tab_permisos.setCondicion("APROBADO_ASPVH = TRUE AND (APROBADO_TTHH_ASPVH = FALSE OR APROBADO_TTHH_ASPVH IS NULL)"
			//		+ " AND IDE_GTEMP="+aut_empleado.getValor());
					
					tab_permisos.setCondicion("APROBADO_ASPVH = TRUE  AND IDE_GTEMP="+aut_empleado.getValor());
			
			tab_permisos.ejecutarSql();


			actualizarTabla2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error al filtrar "+e);
		}
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiarPantalla() {
		tab_permisos.limpiar();
		tab_permiso_justificacion.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
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
		//if (aut_empleado.getValor()!=null){
			//if (validarSolicitudPermiso()){				
				//if (tab_permisos.guardar()){
					
		if (tab_permisos.getValorSeleccionado()!=null) {

						
					
		if (tab_permiso_justificacion.guardar()) {
							guardarPantalla();
				}
			}
		
		else {
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "No existen datos para guardar");
		}

	
	
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
	}




	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
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
		
		
		//if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
			//	|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
			//return;
		//}

		//if(aut_empleado.getValor()!=null){
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
				tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{
				int nro_dias=0;				
				int nrh_horas=0;
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
				int nrh_horas_semi=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_asi_permiso_dias"));
				
				
				numeroSumarFinesSemana = utilitario.numeroFinSemanaEntreFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));

				TablaGenerica fecha_ingreso =utilitario.consultar("SELECT ide_gtemp, (round(FLOOR(((extract(year from now() )-extract(year from cast('2017-01-01' as Date)))* 372 + (extract(month from now()) - "+ 
						" extract(month from cast('2017-01-01' as Date)))*31 + (extract (day from now())-extract(day from cast('2017-01-01' as Date))))/372)))+1 as anio FROM public.gth_empleado where ide_gtemp "+
						" in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
							
				TablaGenerica acumula_vacacion =utilitario.consultar("select 'd', sum(dia_descontado_asdev) as  DIA_DESCONTADO from asi_detalle_vacacion where ide_aspvh in (SELECT ide_aspvh FROM public.asi_permisos_vacacion_hext where  "+
						" ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")  and not (upper( COALESCE(detalle_aspvh,' ')) like '%VACACION%' or upper( COALESCE(detalle_aspvh,' ')) like  '%AJUSTE DESCUENTO AUTOMÀTICO FINES DE SEMANA%') and anulado_aspvh = false "+
						" and activo_aspvh = true and fecha_solicitud_aspvh > '2017-01-01') ");
				
				
				TablaGenerica fines_acumula_vacacion =utilitario.consultar("select 'd', sum(numero_fines_semana_asdev) as  SABADOS_DOMINGOS from asi_detalle_vacacion where ide_aspvh in (SELECT ide_aspvh FROM public.asi_permisos_vacacion_hext where  "+
						" ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")  and anulado_aspvh = false "+
						" and activo_aspvh = true) ");
				
				
				numeroFinesSemanaTotal  =  ((int) pckUtilidades.CConversion.CDbl(fecha_ingreso.getValor("anio")))*8;
				numeroFinesSemana = pckUtilidades.CConversion.CInt(fines_acumula_vacacion.getValor("SABADOS_DOMINGOS"));
				
				System.out.println("numeroFinesSemanaPedidos:  " +numeroFinesSemana);
				
				
				if(numeroFinesSemana < numeroFinesSemanaTotal)	
				{
					
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_FINES_SEMANA_ASPVH",numeroSumarFinesSemana+"");
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",(nro_dias+1)+"");
					nrh_horas=nrh_horas_semi*(nro_dias+1);
					//descuentoFinesSemana =( ((int)( (pckUtilidades.CConversion.CDbl(acumula_vacacion.getValor("DIA_DESCONTADO")) +nro_dias+1)/5))*2 ) -numeroSumarFinesSemana-  numeroFinesSemana;
				}
				else
				{
					
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_FINES_SEMANA_ASPVH","0");
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",(nro_dias+1-numeroSumarFinesSemana)+"");
					numeroSumarFinesSemana = 0;
					nrh_horas=nrh_horas_semi*(nro_dias+1-numeroSumarFinesSemana);
					//descuentoFinesSemana =( ((int)( (pckUtilidades.CConversion.CDbl(acumula_vacacion.getValor("DIA_DESCONTADO")) +(nro_dias+1-numeroSumarFinesSemana))/5))*2 ) -  numeroFinesSemana;
				}
				
				tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH",nrh_horas+"");	
				
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH,NRO_FINES_SEMANA_ASPVH", "");
			}
		/*}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}*/	
	}

	public void calcularDiasPermisos(DateSelectEvent evt){
		tab_permisos.modificar(evt);
		
		if(aut_empleado.getValor()!=null){				
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
				tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{				
				int nro_dias=0;	
				int nrh_horas=0;
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
				int nrh_horas_semi=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_asi_permiso_dias"));
				
				
				numeroSumarFinesSemana = utilitario.numeroFinSemanaEntreFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));

				TablaGenerica fecha_ingreso =utilitario.consultar("SELECT ide_gtemp, (round(FLOOR(((extract(year from now() )-extract(year from cast('2017-01-01' as Date)))* 372 + (extract(month from now()) - "+ 
						" extract(month from cast('2017-01-01' as Date)))*31 + (extract (day from now())-extract(day from cast('2017-01-01' as Date))))/372)))+1 as anio FROM public.gth_empleado where ide_gtemp "+
						" in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
				
				TablaGenerica acumula_vacacion =utilitario.consultar("select 'd', sum(dia_descontado_asdev) as  DIA_DESCONTADO from asi_detalle_vacacion where ide_aspvh in (SELECT ide_aspvh FROM public.asi_permisos_vacacion_hext where  "+
						" ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")  and not (upper( COALESCE(detalle_aspvh,' ')) like '%VACACION%' or upper( COALESCE(detalle_aspvh,' ')) like  '%AJUSTE DESCUENTO AUTOMÀTICO FINES DE SEMANA%') and anulado_aspvh = false "+
						" and activo_aspvh = true and fecha_solicitud_aspvh > '2017-01-01') ");
				
				TablaGenerica fines_acumula_vacacion =utilitario.consultar("select 'd', sum(numero_fines_semana_asdev) as  SABADOS_DOMINGOS from asi_detalle_vacacion where ide_aspvh in (SELECT ide_aspvh FROM public.asi_permisos_vacacion_hext where  "+
						" ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")  and anulado_aspvh = false "+
						" and activo_aspvh = true) ");
				
				
				numeroFinesSemanaTotal  =  ((int) pckUtilidades.CConversion.CDbl(fecha_ingreso.getValor("anio")))*8;
				numeroFinesSemana = pckUtilidades.CConversion.CInt(fines_acumula_vacacion.getValor("SABADOS_DOMINGOS"));
				
				System.out.println("numeroFinesSemanaAcumulado:  " +numeroFinesSemanaTotal);

				
				System.out.println("numeroFinesSemanaPedidos:  " +numeroFinesSemana);
				
				
				
				
				if(numeroFinesSemana < numeroFinesSemanaTotal)	
				{
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_FINES_SEMANA_ASPVH",numeroSumarFinesSemana+"");
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",(nro_dias+1)+"");
					nrh_horas=nrh_horas_semi*(nro_dias+1);
					descuentoFinesSemana =( ((int)( (pckUtilidades.CConversion.CDbl(acumula_vacacion.getValor("DIA_DESCONTADO")) +nro_dias+1)/5))*2 ) -numeroSumarFinesSemana-  numeroFinesSemana;
				}
				else
				{
					
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_FINES_SEMANA_ASPVH","0");
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",(nro_dias+1-numeroSumarFinesSemana)+"");
					numeroSumarFinesSemana = 0;
					nrh_horas=nrh_horas_semi*(nro_dias+1-numeroSumarFinesSemana);
					descuentoFinesSemana =( ((int)( (pckUtilidades.CConversion.CDbl(acumula_vacacion.getValor("DIA_DESCONTADO")) +(nro_dias+1-numeroSumarFinesSemana))/5))*2 ) -  numeroFinesSemana;
				}
	
				System.out.println("descuentoFinesSemana:  " +descuentoFinesSemana);
				
				tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH",nrh_horas+"");	
				
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH,NRO_FINES_SEMANA_ASPVH", "");
			}					
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}	
	}
	
	
	
	
	public void calculoHoras(String str_hora_inicial , String str_hora_final){
		try {
			System.out.println("hora inicial"+str_hora_inicial);
			System.out.println("hora inicial"+str_hora_final);
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
		
		System.out.println("HORAS");
		
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		if(tab_permisos.getValor("registro_novedad_aspvh").equals("true")){
			utilitario.agregarMensajeInfo("Vacación Aplicada", "Ya se encuentra aplicada la vacación");
			return;
		}
		TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
		if(tab_consulta_vacacion.getTotalFilas()>0){
			
		
		/**
		 * validacion ingreso número de horas< a 8h correspondiente a 1 dia de trabajo	
		 */
			
		double nro_horas_aspvh=Double.parseDouble( tab_permisos.getValor("nro_horas_aspvh"));	
		double valor;
		int nro_dias_aspvh=Integer.parseInt(tab_permisos.getValor("NRO_DIAS_ASPVH"));

		
//pongo 8 horas como maximo a tomar en solicitudes por horas
		
		if (nro_horas_aspvh>=8) {
			valor=1.0;	
		}else {
			valor=(Double.parseDouble( tab_permisos.getValor("nro_horas_aspvh"))/8);
		}
    TablaGenerica tab_codigo_vacacion=utilitario.consultar("select ide_asvac,ide_gtemp,dias_pendientes_asvac from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
	TablaGenerica tab_fines_semana=utilitario.consultar("select dias_pendientes_asvac,fines_semana_asvac,dias_tomados_asvac from asi_vacacion where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP"));
	double dias_pendientes=0.0;
	int fines_semana=0;
	double dias_tomados_asvac=0.0;
	double calculoNuevoFinesdeSemana=0.0;
		
	//int nro_dias_aspvh=Integer.parseInt(tab_permisos.getValor("NRO_DIAS_ASPVH"));
			
			fines_semana=Integer.parseInt(tab_fines_semana.getValor("fines_semana_asvac"));
			dias_pendientes=Double.parseDouble(tab_fines_semana.getValor("dias_pendientes_asvac"));
			System.out.println("dias_pendientes : "+dias_pendientes);
		    
	        tab_permisos.setValor("nro_dias_aspvh",""+1);
	    	tab_permisos.setValor("registro_novedad_aspvh", "true");
			tab_permisos.setValor("aprobado_tthh_aspvh", "true");
			tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
			tab_permisos.guardar();
		    

			// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
			int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			//Solo dias laborables excluye sabados y domingos para hacer el calculo
			double numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
				
			// valido si le hago cargo a vacacion de acuerdo al dia si es un fin de semana
			if (numeroDiasRealesSinSabadoDomingo==0) {
				guardarPantalla();
			
			}else {
			
			TablaGenerica tab_detalle_vacacion_ajuste_fin_semana = new  TablaGenerica();
			tab_detalle_vacacion_ajuste_fin_semana.setTabla("asi_detalle_vacacion", "ide_asdev", -1);
			tab_detalle_vacacion_ajuste_fin_semana.setCondicion("ide_asdev=-1");
			tab_detalle_vacacion_ajuste_fin_semana.ejecutarSql();
			tab_detalle_vacacion_ajuste_fin_semana.insertar();
			tab_detalle_vacacion_ajuste_fin_semana.setValor("ide_aspvh", tab_permisos.getValor("ide_aspvh"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("ide_asvac", tab_codigo_vacacion.getValor("ide_asvac"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("fecha_novedad_asdev",tab_permisos.getValor("fecha_solicitud_aspvh"));
			tab_detalle_vacacion_ajuste_fin_semana.setValor("dia_descontado_asdev", valor+"");
						tab_detalle_vacacion_ajuste_fin_semana.setValor("observacion_asdev", "PERMISO APLICADO A VACACION");
			tab_detalle_vacacion_ajuste_fin_semana.setValor("activo_asdev", "true");	
			tab_detalle_vacacion_ajuste_fin_semana.guardar(); 
						guardarPantalla();
			}
	
//}
//utilitario.getConexion().agregarSqlPantalla("update asi_vacacion set dias_pendientes_asvac="+calculodiaspendientes+", fines_semana_asvac="+resultadofinessemana+" where ide_gtemp="+tab_permisos.getValor("ide_gtemp"));
			utilitario.addUpdate("tab_permisos");
		    utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");
		
		}
		else{
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones activo");
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


	public Check getChk_aplica_anulacion_parcial() {
		return chk_aplica_anulacion_parcial;
	}


	public void setChk_aplica_anulacion_parcial(Check chk_aplica_anulacion_parcial) {
		this.chk_aplica_anulacion_parcial = chk_aplica_anulacion_parcial;
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
		    System.out.println(fin_semana);
		    
		    if (fin_semana.equalsIgnoreCase("sábado")) {
				calculo_sabados++;
			}
		    
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
			System.out.println("calculosabado"+calculo_sabados);
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
//		    System.out.println(fin_semana);
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
			System.out.println("calculodomingos"+calculo_domingos);
		    return calculo_domingos;
}








    
    
    
	
	
}
