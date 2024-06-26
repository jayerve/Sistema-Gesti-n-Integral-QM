package paq_asistencia_permisos;
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

import org.apache.commons.logging.impl.AvalonLogger;
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
import pckEntidades.EnvioMail;
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


public class pre_permisos_dias extends Pantalla {

	private Tabla tab_permisos=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	private Confirmar con_guardar_vacacion=new Confirmar();

	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_filtro_activo = new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_asis=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private Tabla tab_permiso_justificacion=new Tabla();
	private Integer ide_gttem;

	private Dialogo dia_anulado=new Dialogo();
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();
	private int numeroSumarFinesSemana =0;
	//private double diasTotales =0;
	private int numeroFinesSemana = 0;
	private int numeroFinesSemanaTotal =0;
	private int descuentoFinesSemana =0;
	private Check  chk_aplica_vacacion_sabado = new Check();
	private Check chk_aplica_vacacion_domingo = new Check();
	private Check chk_aplica_vacacion_sabado_domingo = new Check();

	private Dialogo dia_aplica_vacion_fin_semana = new Dialogo();
	private Boolean sabado;
	private Boolean domingo;
	private Etiqueta eti_aplica_vacacion_domingo= new Etiqueta();
	private Etiqueta eti_aplica_vacacion_sabado= new Etiqueta();
	private Etiqueta eti_aplica_vacacion_sabado_domingo= new Etiqueta();
    private	PanelGrid panGri = new PanelGrid();
	
	
    
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	private Date parse;

	
	public pre_permisos_dias() {
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("aprobarSolicitud");

		Boton bot_anulado=new Boton();
		bot_anulado.setValue("ANULAR SOLICITUD");
		bot_anulado.setMetodo("anularSolicitud");

		Boton bot_aprobacion_talento_humano=new Boton();
		bot_aprobacion_talento_humano.setValue("APROBACION TALENTO HUMANO");
		bot_aprobacion_talento_humano.setMetodo("aprobacionTalentoHumano");
		
		Boton bot_aplica_vacacion=new Boton();
		bot_aplica_vacacion.setValue("APLICA VACACION");
		bot_aplica_vacacion.setMetodo("getDiaFinSemanaDescontar");

		Boton bot_deshacer_aplica_vacacion=new Boton();
		bot_deshacer_aplica_vacacion.setValue("DESHACER APLICA VACACION");
		bot_deshacer_aplica_vacacion.setMetodo("deshacerAplicaVacacion");
				

		Boton bot_calculo_dias_vacaciones=new Boton();
		bot_calculo_dias_vacaciones.setValue("CALCULAR DIAS VACACION");
		bot_calculo_dias_vacaciones.setMetodo("calcularDiasVacacion");
				

		
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");


		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();




		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);		
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");



		//  PERMISOS (division 1)

		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.agregarRelacion(tab_permiso_justificacion);
		
		tab_permisos.getColumna("TIPO_ASPVH").setValorDefecto("4");// 1 permisos 
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
		tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setVisible(false);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);		
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);	
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setVisible(false);		
		
		tab_permisos.getColumna("IDE_ASPVH").setNombreVisual("CODIGO");
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setNombreVisual("FECHA SOLICITUD");
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		tab_permisos.getColumna("ACTIVO_ASPVH").setNombreVisual("ACTIVO");
		tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO where ver_motivo_asmot=true order by DETALLE_ASMOT");		
		tab_permisos.getColumna("IDE_ASMOT").setNombreVisual("MOTIVO AUSENCIA");
		tab_permisos.getColumna("IDE_ASMOT").setRequerida(true);		
		
		tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP,"
				+ "CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
			//	+ "WHERE EPAR.ACTIVO_GEEDP=TRUE ");
		tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP").setNombreVisual("GERENTE DE AREA");
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setNombreVisual("JEFE INMEDIATO");
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setNombreVisual("RESPONSABLE TH");
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setNombreVisual("APLICA VACACION");
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("calcularDiasPermisos");
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setNombreVisual("FECHA DESDE");
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setMetodoChange("calcularDiasPermisos");
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setNombreVisual("FECHA HASTA");
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setNombreVisual("NRO HORAS");
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
        tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setNombreVisual("NRO DIAS");
		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
    	tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("ANULADO_ASPVH").setNombreVisual("ANULADO");
    	tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_ASPVH").setNombreVisual("APROBADO");
    	tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setNombreVisual("APROBADO TTHH");
		tab_permisos.getColumna("DETALLE_ASPVH").setNombreVisual("DETALLE");
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setNombreVisual("FEC. ANULA");
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setNombreVisual("RAZÓN ANULA");

    	tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);
		tab_permisos.setCondicion("TIPO_ASPVH=1 AND IDE_GEEDP=-1");
		tab_permisos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("SOLICITUD DE PERMISOS POR DIAS");


		//Permiso de Justificacion 

		tab_permiso_justificacion.setId("tab_permiso_justificacion");
		tab_permiso_justificacion.setTabla("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ", 2);
		tab_permiso_justificacion.getColumna("DETALLE_ASPEJ").setNombreVisual("DETALLE");
		tab_permiso_justificacion.getColumna("FECHA_ASPEJ").setNombreVisual("FECHA");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setCheck();
		tab_permiso_justificacion.getColumna(" ACTIVO_ASPEJ").setValorDefecto("true");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setNombreVisual("ACTIVO");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setUpload("permisos");
		//tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setImagen("128", "128");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setNombreVisual("DETALLE JUSTIFICACION");
		tab_permiso_justificacion.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_permiso_justificacion);
		pat_panel2.setMensajeWarn("JUSTIFICACION DE PERMISOS");

		
		
		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		
		

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
	
		
		//bar_botones.agregarBoton(bot_aprobar_solicitud);
		bar_botones.agregarBoton(bot_anulado);
		bar_botones.agregarBoton(bot_aprobacion_talento_humano); 
		bar_botones.agregarBoton(bot_aplica_vacacion); 
		bar_botones.agregarBoton(bot_deshacer_aplica_vacacion); 
		//bar_botones.agregarBoton(bot_calculo_dias_vacaciones);
		
		/// dialogo de anula

		cal_fecha_anula.setId("cal_fecha_anula");

		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:300px;");

		Grid gri_anular_horas_extra=new Grid();
		gri_anular_horas_extra.setColumns(2);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
		dia_anulado.setId("dia_anulado");
		dia_anulado.setDialogo(gri_anular_horas_extra);
		dia_anulado.setWidth("50%");
		dia_anulado.setHeight("30%");
		dia_anulado.setTitle("ANULACION DE SOLICITUD DE PERMISOS");
		dia_anulado.getBot_aceptar().setMetodo("aceptarAnulacionHorasPermisos");				
		dia_anulado.setDynamic(false);
		gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anulado);
		
		
		
		Etiqueta eti_aplica_vacacion_sabado=new Etiqueta("Aplicar cargo a vacaciones Sabado");
		chk_aplica_vacacion_sabado.setId("chk_aplica_vacacion_sabado");
		
		Etiqueta eti_aplica_vacacion_domingo=new Etiqueta("Aplicar cargo a vacaciones Domingo");
		chk_aplica_vacacion_domingo.setId("chk_aplica_vacacion_domingo");
		Etiqueta eti_aplica_vacacion_sabado_domingo=new Etiqueta("Aplicar cargo a vacaciones Sabado y Domingo");
		chk_aplica_vacacion_sabado_domingo.setId("chk_aplica_vacacion_sabado_domingo");
		panGri.setColumns(2);
	//Dialogo confirmacion aplica o no aplica a vacacion
		dia_aplica_vacion_fin_semana.setId("dia_aplica_vacion_fin_semana");
		dia_aplica_vacion_fin_semana.setTitle("CONFIRMACION APLICACIÓN A VACACIÓN");
		dia_aplica_vacion_fin_semana.getBot_aceptar().setMetodo("aceptarAplicaVacacionFinesSemana");
		dia_aplica_vacion_fin_semana.getGri_cuerpo().getChildren().add(panGri);
		dia_aplica_vacion_fin_semana.setWidth("20%");
		dia_aplica_vacion_fin_semana.setHeight("15%");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_aplica_vacion_fin_semana);
		
		
		
	
	}
	

	public void calcularDiasVacacion(){
		
		if (aut_empleado.getValor()!=null){
	   //Me retorna el ide_geedp de la persona que esta realizando el permiso	
        ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		System.out.println("empleado : "+ide_geedp_activo);

	//obtengo la fecha de ingreso del empleado
		TablaGenerica  obtener_fecha_ingreso=utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac from asi_vacacion where ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=434)");
		System.out.println("fecha ingreso : "+obtener_fecha_ingreso.getValor("fecha_ingreso_asvac"));

	//obtengo el calculo de dias de los empleados atados a la losep	
		String fecha_ingreso_asvac=obtener_fecha_ingreso.getValor("fecha_ingreso_asvac");
		
		TablaGenerica calculo_dias_fecha_ingreso =utilitario.consultar("SELECT ide_gtemp, (round(FLOOR(((extract(year from now() )-extract(year from cast('"+fecha_ingreso_asvac+"' as Date)))* 372 + (extract(month from now()) - "+ 
				" extract(month from cast('"+fecha_ingreso_asvac+"' as Date)))*31 + (extract (day from now())-extract(day from cast('"+fecha_ingreso_asvac+"' as Date))))/372)))+1 as anio FROM public.gth_empleado where ide_gtemp "+
				" in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
		
		System.out.println("calculo dias fecha ingreso : "+calculo_dias_fecha_ingreso.getValor("anio"));
	
		
		System.out.println("fecha en date : "+utilitario.DeStringADate(obtener_fecha_ingreso.getValor("fecha_ingreso_asvac")));
		System.out.println("fecha en date1 : "+utilitario.DeStringADate(utilitario.getFechaActual()));


	    

       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
//       String dateInString = obtener_fecha_ingreso.getValor("fecha_ingreso_asvac");
       try {
       
          Date date = formatter.parse(fecha_ingreso_asvac);
          Date date1 = formatter.parse(utilitario.getFechaActual());

           //System.out.println(date);
           System.out.println(formatter.format(date));
           System.out.println(formatter.format(date1));

           
           long fechainicialms = date.getTime();
           long fechafinalms = date1.getTime();
           long diferencia = fechafinalms - fechainicialms;
           double dias = Math.floor(diferencia / 86400000L);// 3600*24*1000
           
   		int valor_diferencia_fechas = utilitario.getDiferenciasDeFechas(date,date1);
        System.out.println("valor diferencia fechas: "+valor_diferencia_fechas);
        
   		int valor_diferencia_fechas1 = utilitario.getDiferenciasDeFechas(date1,date);
        System.out.println("valor diferencia fechas: "+valor_diferencia_fechas1);
        
        int resultado =restarFechas(date,date1);
        System.out.println("resultado: "+resultado);
        
    
       } catch (ParseException e) {
           e.printStackTrace();
       }

		
		
		}else {
			utilitario.agregarMensaje("Debe seleccionar un empleado", "");
		}
		
		//sssss
		
		/*
		TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
		utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,numero_fines_semana_asdev)"
				+"values ( "+tab_codigo.getValor("codigo")+","+tab_permisos.getValor("ide_aspvh")+","+tab_consulta_vacacion.getValor("ide_asvac")+",'"+tab_permisos.getValor("fecha_solicitud_aspvh")+"',"+tab_permisos.getValor("nro_dias_aspvh")+",'"+tab_permisos.getValor("detalle_aspvh")+"',true,"+tab_permisos.getValor("NRO_FINES_SEMANA_ASPVH")+" )");

	*/
	
	
	}
	
	
	
	public static int restarFechas(Date fechaIn, Date fechaFinal ){
		GregorianCalendar fechaInicio= new GregorianCalendar();
		fechaInicio.setTime(fechaIn);
		GregorianCalendar fechaFin= new GregorianCalendar();
		fechaFin.setTime(fechaFinal);
		int dias = 0;
		if(fechaFin.get(Calendar.YEAR)==fechaInicio.get(Calendar.YEAR)){

		dias =(fechaFin.get(Calendar.DAY_OF_YEAR)- fechaInicio.get(Calendar.DAY_OF_YEAR))+1;
		}else{
		int rangoAnyos = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);

		for(int i=0;i<=rangoAnyos;i++){
		int diasAnio = fechaInicio.isLeapYear(fechaInicio.get(Calendar.YEAR)) ? 366 : 365;
		if(i==0){
		dias=1+dias +(diasAnio- fechaInicio.get(Calendar.DAY_OF_YEAR));
		}else if(i==rangoAnyos){
		dias=dias +fechaFin.get(Calendar.DAY_OF_YEAR);
		}else{
		dias=dias+diasAnio;
		}
		}
		}

		return dias;

		}
	
	public void aplicaVacacion(){
		

		System.out.println("Ingresa aqui");
				
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		
		if (tab_permisos.getValor("APROBADO_ASPVH")==null || tab_permisos.getValor("APROBADO_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe Aprobar la solicitud");
			return ;
		}
		if (tab_permisos.getValor("gen_ide_geedp3")==null || tab_permisos.getValor("gen_ide_geedp3").isEmpty() || tab_permisos.getValor("gen_ide_geedp3").equals("")){
			utilitario.agregarMensajeInfo("No se puede aplicar a vacación", "Debe ingresar el campo del responsable de Talento Humano");
			return ;
		}
	
		
		if(tab_permisos.getValor("registro_novedad_aspvh").equals("true")){
			utilitario.agregarMensajeError("Vacación Aplicada", "Ya se encuentra aplicada la vacación");
			return;
		}
	
		if(tab_permisos.getValor("anulado_aspvh").equals("true")){
			utilitario.agregarMensajeError("Vacación Anulada", "La solicitud se encuentra anulada");
			return;
		}
		
		
		if(tab_permisos.getValor("anulado_aspvh").equals("true")){
			utilitario.agregarMensajeError("Vacación Anulada", "La solicitud se encuentra anulada");
			return;
		}
	
		
		String ide_asvac=getPeriodoVacacion(tab_permisos.getValor("IDE_GTEMP"));
		TablaGenerica tab_consulta_vacacion=utilitario.consultar(ser_asistencia.retornaPeriodoVacacion(ide_asvac));
	
		
		System.out.println(tab_consulta_vacacion.getValor("ide_asvac"));
		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
		if(tab_consulta_vacacion.getTotalFilas()>0){
		String detalle_aspvh=tab_permisos.getValor("detalle_aspvh");
	    int nro_dias_aspvh=(int)valor;
		System.out.println("Dias"+nro_dias_aspvh);
				
		TablaGenerica tab_codigo_vacacion=utilitario.consultar(ser_asistencia.retornaPeriodoVacacion(ide_asvac));
		double dias_pendientes=0.0;
		int fines_semana=0;
		double dias_tomados_asvac=0.0;
        double calculoNuevoFinesdeSemana=0.0;

        tab_permisos.setValor("registro_novedad_aspvh", "true");
 			tab_permisos.setValor("aprobado_tthh_aspvh", "true");
 		    tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
 			tab_permisos.guardar();		
			
			
			
///////////////////////////////////////////////////////////////////DIAS SOLICITADOS//////////////////////////////////////////////////////////////	
			TablaGenerica tab_partida = getUltimaPartida(tab_permisos.getValor("IDE_GTEMP"));
			ide_gttem = Integer.parseInt(tab_partida.getValor("IDE_GTTEM"));
			
			int dias_sabados=0;
			dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			
			int dias_domingos=0;
			dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
			
			double numeroDiasRealesSinSabadoDomingo;
			numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
			int nro_dias_fines_semana=0;
			nro_dias_fines_semana=dias_sabados+dias_domingos;
			

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
				 System.out.println("Ingreso 1");
				 
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
			}
				 

			
////////////////////////////////////////////////////////Validacion de dias domingos////////////////////////////////////////////////////////////////////////////		
					
			
			
				else if(((Boolean)chk_aplica_vacacion_domingo.getValue())){
										
				System.out.println("Ingreso 2");
				
				if(numeroDiasRealesSinSabadoDomingo>0){ 
				TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
				 utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
				 +"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+numeroDiasRealesSinSabadoDomingo+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN' ,true,true)");


					//Guardar datos de fines de semana
					int codigo =Integer.parseInt(tab_codigo.getValor("codigo"))+1;
					utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
									+"values ( "+codigo+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+(nro_dias_fines_semana)+",'APLICACIÓN DE PERMISOS DÍA DOMINGO CON CARGO A VACACIÓN',true,true,true)");

				}else if (numeroDiasRealesSinSabadoDomingo==0) {
				
					TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
					 utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
					 +"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS DÍA SÁBADO CON CARGO A VACACIÓN' ,true,true)");


				}
					 guardarPantalla();

				
				
				}
			
////////////////////////////////////////////////////////Validacion de dias sabados y domingos////////////////////////////////////////////////////////////////////////////		
			
			
			else if((Boolean)chk_aplica_vacacion_sabado_domingo.getValue()){
				System.out.println("Ingreso 3");

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
			    
			    guardarPantalla();
			}		

			else{
		// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
		//int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
		//int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
				
		//Solo dias laborables excluye sabados y domingos para hacer el calculo
		//double
			
				
								
	     numeroDiasRealesSinSabadoDomingo=nro_dias_aspvh-(dias_sabados+dias_domingos);
		//int nro_dias_fines_semana=dias_sabados+dias_domingos;	
// v    alido si le hago cargo a vacacion de acuerdo al dia si es un fin de semana
		if (nro_dias_fines_semana==0) {
		
		TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
		utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
		+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN',true,false)");
		
		guardarPantalla();
			
         }else{
		
	
		//valido si el numero de dias es igual a 0
		if (numeroDiasRealesSinSabadoDomingo==0) {
					   		
			TablaGenerica tab_codigo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
			utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_aspvh,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,fin_semana_asdev)"
					+"values ( "+tab_codigo.getValor("codigo")+", "+tab_permisos.getValor("ide_aspvh")+","+tab_codigo_vacacion.getValor("IDE_ASVAC")+",'"+utilitario.getFechaActual()+"',"+nro_dias_aspvh+",'APLICACIÓN DE PERMISOS CON CARGO A VACACIÓN FIN SEMANA' ,true,true,true)");
			guardarPantalla();
		}
		
		else {
			
		    
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
}
		EnviarCorreo(Integer.parseInt(tab_permisos.getValorSeleccionado()));

	  	con_guardar.cerrar();
	  	dia_aplica_vacion_fin_semana.cerrar();
	    utilitario.addUpdate("tab_permisos,con_guardar,dia_aplica_vacion_fin_semana");
        utilitario.agregarMensaje("Aplicado Vacación", "Se aplico descuento a vacaciones exitosamente");

		}else{
			utilitario.agregarMensajeInfo("No existe vacación", "No empleado seleccionado no posee un periodo de vacaciones activo");
		}		
		
		
		
			}

		
	
	public void deshacerAplicaVacacion(){
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		
		if(tab_permisos.getValor("registro_novedad_aspvh").equals("false")){
			utilitario.agregarMensajeError("No se puede Desaplicar", "Solicitud sin aplicación a vacación");
			return;
		}
		utilitario.getConexion().ejecutarSql("delete from asi_detalle_vacacion where ide_aspvh="+tab_permisos.getValor("ide_aspvh"));
		tab_permisos.setValor("registro_novedad_aspvh", "false");
		tab_permisos.modificar(tab_permisos.getFilaActual());//para que haga el update
		tab_permisos.guardar();
		guardarPantalla();
		utilitario.addUpdate("tab_permisos");
		utilitario.agregarMensaje("Aplicado Deshacer Vacación", "Se descontó a vacaciones exitosamente");
		
	}
	
	public void aprobarSolicitud(){	
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
				con_guardar.setTitle("CONFIRMACION APROBACION DE SOLICITUD DE PERMISO");
				con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}		
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		}
	}

	public void aceptarAprobarSolicitud(){		
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where TIPO_ASPVH=4 and ide_aspvh="+tab_permisos.getValorSeleccionado());
		guardarPantalla();
		con_guardar.cerrar();		
		String ide_anterior=tab_permisos.getValorSeleccionado();		
		tab_permisos.ejecutarSql();
		tab_permisos.setFilaActual(ide_anterior);
	}

	public void anularSolicitud(){		
		if(aut_empleado.getValue()!=null && !aut_empleado.getValue().toString().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				cal_fecha_anula.setDisabled(true);
				dia_anulado.dibujar();
				
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar los Permisos ", "El Empleado no Tiene Solicitudes");
			}			
		}else{
			utilitario.agregarMensajeInfo("No se puede Anular los permisos ", "Debe seleccionar un Empleado");
		}
	}

	public void aceptarAnulacionHorasPermisos(){
	
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
					tab_permisos.setValor("razon_anula_aspvh",are_tex_razon_anula.getValue().toString());
					tab_permisos.setValor("fecha_anula_aspvh",""+ utilitario.getFechaActual());
					tab_permisos.modificar(tab_permisos.getFilaActual());
					tab_permisos.guardar();
					utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set anulado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());					
					guardarPantalla();
					dia_anulado.cerrar();
					String ide_anterior=tab_permisos.getValorSeleccionado();		
					tab_permisos.ejecutarSql();
					tab_permisos.setFilaActual(ide_anterior);
			}else{
				utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe Ingresar El Motivo de la Anulación");
			}					
	}



	public void aprobacionTalentoHumano(){
	if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede continuar", "Ya se encuentra aprobado por Talento Humano");
					return;
				}
				
				if (tab_permisos.getValor("gen_ide_geedp3")==null || tab_permisos.getValor("gen_ide_geedp3").isEmpty() || tab_permisos.getValor("gen_ide_geedp3").equals("")){
					utilitario.agregarMensajeInfo("No se puede aplicar a vacación", "Debe ingresar el campo del responsable de Talento Humano");
					return ;
				}
			
				
				con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Talento Humano");
				con_guardar.setTitle("CONFIRMACION APROBACION DE TALENTO HUMANO");
				con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitudTalento");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
		
				
		
			
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}	
		

		
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		}		
		
		
	}

	public void aceptarAprobarSolicitudTalento(){
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true, aprobado_tthh_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
		guardarPantalla();
		EnviarCorreo(Integer.parseInt(tab_permisos.getValorSeleccionado()));

		con_guardar.cerrar();	

		String ide_anterior=tab_permisos.getValorSeleccionado();		
		tab_permisos.ejecutarSql();
		tab_permisos.setFilaActual(ide_anterior);

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
		super.inicio();
		actualizarTabla2();
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
		super.siguiente();
		actualizarTabla2();
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

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
	  tab_permisos.setCondicion("TIPO_ASPVH=4 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_permisos.ejecutarSql();

		actualizarTabla2();
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_permisos.limpiar();
		tab_permiso_justificacion.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
		sel_cal.Limpiar();
		chk_aplica_vacacion_domingo.resetValue();
		chk_aplica_vacacion_sabado.resetValue();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_permisos.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){					
					tab_permisos.insertar();
					tab_permisos.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_permisos.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "El contrato del empleado no esta activo");
				}					
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Permiso");
			}
		}else if(tab_permiso_justificacion.isFocus()){
			tab_permiso_justificacion.insertar();
		}	
	}


	public boolean validarSolicitudPermiso(){
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
			utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return false;
		}
		if (tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha desde ");
			return false;
		}

		if (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha hasta");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP")==null || tab_permisos.getValor("GEN_IDE_GEEDP").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar Jefe Inmediato");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP2")==null || tab_permisos.getValor("GEN_IDE_GEEDP2").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Jefe de Talento Humano");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP3")==null || tab_permisos.getValor("GEN_IDE_GEEDP3").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Gerente de Area");
			return false;
		}	
		if (tab_permisos.getValor("IDE_ASMOT")==null || tab_permisos.getValor("IDE_ASMOT").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Motivo");
			return false;
		}
		if (tab_permisos.getValor("FECHA_SOLICITUD_ASPVH")==null || tab_permisos.getValor("FECHA_SOLICITUD_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar La fecha de Solicitud");
			return false;
		}
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
			utilitario.agregarMensajeError("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return false;
		}

		return true;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		ser_contabilidad.limpiarAcceso("ASI_PERMISOS_VACACION_HEXT");
		ser_contabilidad.limpiarAcceso("ASI_PERMISO_JUSTIFICACION");
		
		if (aut_empleado.getValor()!=null){		
			if (validarSolicitudPermiso()){				
				if (tab_permisos.guardar()){
					if (tab_permiso_justificacion.guardar()) {						
						guardarPantalla();

					}					
					
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Permiso", "Debe seleccionar un Empleado");
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

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
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
		
		
		if(aut_empleado.getValor()== null){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar un empleado para continuar");
			return;
		}
		
		
		//if (tab_permisos.getValor("anulado_aspvh").equals("true")) {
	//	}else{
		
		if (tab_permisos.getValor("aprobado_aspvh")==null || tab_permisos.getValor("aprobado_aspvh").equals("false") || tab_permisos.getValor("aprobado_aspvh").isEmpty()) {
			utilitario.agregarMensajeInfo("Recuerde : ", "El permiso no se encuentra aprobado por el Jefe Inmediato");
			return;
		}
		
		if (tab_permisos.getValor("aprobado_tthh_aspvh")==null || tab_permisos.getValor("aprobado_tthh_aspvh").equals("false") || tab_permisos.getValor("aprobado_tthh_aspvh").isEmpty()) {
			utilitario.agregarMensajeInfo("Recuerde : ", "El permiso no se encuentra aprobado por el Talento Humano");
			return;
		}
		//}
		
		
		
		if (rep_reporte.getReporteSelecionado().equals("Solicitud de Permisos / Vacaciones")){
			if (tab_permisos.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();		
					p_parametros.put("IDE_GTEMP", Long.parseLong(aut_empleado.getValor()));
					p_parametros.put("ide_aspvh", Integer.parseInt(tab_permisos.getValor("IDE_ASPVH")));
					p_parametros.put("titulo", " SOLICITUD DE PERMISOS / VACACION");
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
		
		if(aut_empleado.getValor()!=null){
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}
		
	        if (utilitario.isFechasValidas(utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")))) {
	            tab_permisos.setValor("NRO_DIAS_ASPVH", (new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)) + ""));
				double nro_horas=(new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)))*8;
				tab_permisos.setValor("NRO_HORAS_ASPVH", +nro_horas+"");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
	       		
	       
	        } else {
	        	tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
	        }
	    }

	
	}

	public void calcularDiasPermisos(DateSelectEvent evt){
		tab_permisos.modificar(evt);
		if(aut_empleado.getValor()!=null){
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}

	        if (utilitario.isFechasValidas(utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")))) {
	            tab_permisos.setValor("NRO_DIAS_ASPVH", (new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)) + ""));
				double nro_horas=(new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)))*8;
				tab_permisos.setValor("NRO_HORAS_ASPVH", +nro_horas+"");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
	       		
	       
	        } else {
	        	tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
	        }
	    }

			
			
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
//			    System.out.println(fin_semana);
			    if (fin_semana.equalsIgnoreCase("domingo")) {
					calculo_domingos++;
				}
			}		    
			    
				System.out.println("calculodomingos"+calculo_domingos);
			    return calculo_domingos;
	}
	
    /**
     * Metodo para obtener al grupo que pertenece el empleado 1: Codigo de trabajo, 2: Losep, 3:Pasante
     * @param IDE_GEEDP
     * @return
     */

   public Integer getGrupoTipoEmpleado(String IDE_GEEDP){
	   Integer ide_gttem=0;
       TablaGenerica tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GEEDP=" + IDE_GEEDP);       
        ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      
       return ide_gttem;
   }
    

   public TablaGenerica getPartida(String IDE_GTEMP){
       return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GTEMP="+IDE_GTEMP+" ORDER BY IDE_GEEDP DESC LIMIT 1");
   }


public Dialogo getDia_aplica_vacion_fin_semana() {
	return dia_aplica_vacion_fin_semana;
}


public void setDia_aplica_vacion_fin_semana(Dialogo dia_aplica_vacion_fin_semana) {
	this.dia_aplica_vacion_fin_semana = dia_aplica_vacion_fin_semana;
}
   
/*
 * Metodo aplica cargo a vacacion los fines de semana 
 */
 
  public void aceptarAplicaVacacionFinesSemana(){

	  
	  try {
		  con_guardar.setMessage("Esta Seguro de Aplicar La Solicitud a Vacación");
		  con_guardar.setTitle("CONFIRMACION ALICACIÓN A VACACIÓN");
		  con_guardar.getBot_aceptar().setMetodo("aplicaVacacion");
		  con_guardar.getBot_cancelar().setMetodo("setteraVacacion");
		  con_guardar.dibujar();
		  utilitario.addUpdate("con_guardar");	
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
   
	} 
    
  
  public void setteraVacacion(){
	  dia_aplica_vacion_fin_semana.cerrar();
	  con_guardar.cerrar();
utilitario.addUpdate("dia_aplica_vacion_fin_semana,con_guardar");
	  
  }
  
  
  
  public void getDiaFinSemanaDescontar(){
	  
	  
		TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,aplica_vacaciones_asmot,es_vacacion_asmot  from asi_motivo where ide_asmot="+tab_permisos.getValor("IDE_ASMOT"));
		
        String aplica_vacaciones_asmot=tab_motivo.getValor("aplica_vacaciones_asmot");
        String esvacacion=tab_motivo.getValor("es_vacacion_asmot");
	  
        
                
        
	  
		if(tab_permisos.getValor("ide_aspvh")==null){
			utilitario.agregarMensajeInfo("No existe registro", "No se puede aplicar vacación no existe un registro.");
			return;
		}
		
		
		
		if (tab_permisos.getValor("APROBADO_ASPVH")==null || tab_permisos.getValor("APROBADO_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe Aprobar la solicitud");
			return ;
		}
		if (tab_permisos.getValor("APROBADO_tthh_ASPVH")==null || tab_permisos.getValor("APROBADO_tthh_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe Aprobar la solicitud");
			return ;
		}
	
		
		if(tab_permisos.getValor("registro_novedad_aspvh").equals("true")){
			utilitario.agregarMensajeError("Vacación Aplicada", "Ya se encuentra aplicada la vacación");
			return;
		}


		if(tab_permisos.getValor("anulado_aspvh").equals("true")){
			utilitario.agregarMensajeError("Vacación Anulada", "La solicitud se encuentra anulada");
			return;
		}
	  
		

        if ((aplica_vacaciones_asmot.equals("true")) && (esvacacion.equals("false"))){
        	
		panGri.getChildren().clear();
		utilitario.addUpdate("panGri");
				
		
		TablaGenerica tab_consulta_vacacion = utilitario.consultar(ser_asistencia.getSqlConsultaVacacion(tab_permisos.getValor("ide_aspvh")));
		double valor = (Double.parseDouble( tab_permisos.getValor("nro_dias_aspvh")));
		if(tab_consulta_vacacion.getTotalFilas()>0){
		String detalle_aspvh=tab_permisos.getValor("detalle_aspvh");
	    int nro_dias_aspvh=(int)valor;
				
	// numeros de dias sabados y domingos tomados en el periodo de dias solicitados
	int dias_sabados=getDiasSabado(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);
	int dias_domingos=getDiasDomingo(tab_permisos.getValor("FECHA_DESDE_ASPVH"),nro_dias_aspvh);

	System.out.println("dias_sabados : "+dias_sabados);
	System.out.println("dias_domingo : "+dias_domingos);
	

	if((dias_sabados==1)   || (dias_domingos==1)){
			
		if ((dias_sabados==1) && (dias_domingos==0)) {
			panGri.getChildren().add(new Etiqueta("Aplica dia Sabado"));
			panGri.getChildren().add(chk_aplica_vacacion_sabado);
			dia_aplica_vacion_fin_semana.dibujar();
			utilitario.addUpdate("dia_aplica_vacion_fin_semana");
				
													 }
			
		else if ((dias_sabados==0) && (dias_domingos==1)) {
			panGri.getChildren().add(new Etiqueta("Aplica dia Domingo"));
			panGri.getChildren().add(chk_aplica_vacacion_domingo);
			dia_aplica_vacion_fin_semana.dibujar();
			utilitario.addUpdate("dia_aplica_vacion_fin_semana");
														  }
	
		else if ((dias_sabados==1) && (dias_domingos==1)) {
			panGri.getChildren().add(new Etiqueta("Aplica dia Sabado y Domingo"));
			panGri.getChildren().add(chk_aplica_vacacion_sabado_domingo);
			dia_aplica_vacion_fin_semana.dibujar();
			utilitario.addUpdate("dia_aplica_vacion_fin_semana");
															}
		
		}else {

			con_guardar.setMessage("Esta Seguro de Aplicar La Solicitud a Vacación");
			con_guardar.setTitle("CONFIRMACION APLICACIÓN A VACACIÓN");
			con_guardar.getBot_aceptar().setMetodo("aplicaVacacion");
			con_guardar.getBot_cancelar().setMetodo("setteraVacacion");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}
		
	    }
		
		
        }else{
		
		con_guardar.setMessage("Esta Seguro de Aplicar La Solicitud a Vacación");
		con_guardar.setTitle("CONFIRMACION APLICACIÓN A VACACIÓN");
		con_guardar.getBot_aceptar().setMetodo("aplicaVacacion");
		con_guardar.getBot_cancelar().setMetodo("setteraVacacion");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");
		}
		
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
							null);
			*/
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

  

public String getPeriodoVacacion(String IDE_GTEMP){
	
	String ide_asvac="";
	TablaGenerica tab_vacacion =utilitario.consultar("SELECT * FROM ASI_VACACION WHERE IDE_GTEMP IN("+IDE_GTEMP+") and activo_asvac=true");
	TablaGenerica tab_vacacion_finalizada =utilitario.consultar("SELECT * FROM ASI_VACACION WHERE IDE_GTEMP IN("+IDE_GTEMP+") and activo_asvac=false");
	
	int sinFechaIngreso=0, sinFechaSalida=0;
	 if(tab_vacacion.getTotalFilas()>0){
			String fecha_ingreso_vacacion="";
			
			//for (int i = 0; i < tab_vacacion.getTotalFilas(); i++) {
				//fecha_ingreso_vacacion=tab_vacacion.getValor(i,"FECHA_INICIO_ASVAC");
				System.out.println("COMPARACION ACTIVO MAYOR A 1 "+fecha_ingreso_vacacion+"   de "+tab_permisos.getValor("FECHA_DESDE_ASPVH"));
			//	if (fecha_ingreso_vacacion.compareTo(tab_permisos.getValor("FECHA_SOLICITUD_ASPVH"))<0) {
					ide_asvac=tab_vacacion.getValor(tab_vacacion.getTotalFilas()-1,"IDE_ASVAC");
					//i=tab_vacacion.getTotalFilas();
					sinFechaIngreso=1;
			//	}
				//}
			System.out.println("INGRESO ACTIVO MAYOR A 1");
			System.out.println("IDE_ASVAC= "+ide_asvac);
		
	}
	
	
	if (tab_vacacion_finalizada.getTotalFilas()>0) {

		
		String fecha_salida_vacacion="";
		
		for (int i = 0; i < tab_vacacion_finalizada.getTotalFilas(); i++) {
			fecha_salida_vacacion=tab_vacacion_finalizada.getValor(i,"FECHA_FINIQUITO_ASVAC");
			System.out.println("COMPARACION ACTIVO MAYOR A 1 "+tab_vacacion_finalizada.getValor(i,"FECHA_FINIQUITO_ASVAC"));
			System.out.println("COMPARACION ACTIVO MAYOR A 2 "+tab_permisos.getValor("FECHA_DESDE_ASPVH")+"   "+(fecha_salida_vacacion.compareTo(tab_permisos.getValor("FECHA_DESDE_ASPVH"))>0));
			if (fecha_salida_vacacion.compareTo(tab_permisos.getValor("FECHA_DESDE_ASPVH"))>0) {
				ide_asvac=tab_vacacion_finalizada.getValor(i,"IDE_ASVAC");
				//i=tab_vacacion_finalizada.getTotalFilas();
				sinFechaSalida=1;
			}
		}
		System.out.println("INGRESO INACTIVO");
		System.out.println("IDE_ASVAC= "+ide_asvac);
	
	}
		
		
		if (sinFechaIngreso==0 && sinFechaSalida==0) {
			return "";
		}
	return ide_asvac;

}

public TablaGenerica getUltimaPartida(String IDE_GTEMP){
    return utilitario.consultar("select ide_geedp,ide_gtemp,ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR where "
    		+ "IDE_GTEMP="+IDE_GTEMP+" "
    		+ " LIMIT 1");
}
  


}
