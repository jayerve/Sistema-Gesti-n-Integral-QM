package paq_asistencia;

import java.sql.PreparedStatement;
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
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_vacaciones extends Pantalla {

	private Tabla tab_permisos=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_vacaciones = new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private	ListaSeleccion lis_vacaciones=new ListaSeleccion();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private Dialogo dia_filtro_activo = new Dialogo();
	private SeleccionTabla sel_tab_fecha_periodos=new SeleccionTabla();
	private Consulta con_ver_vacaciones = new Consulta();
	private Tabla tab_tabla3 = new Tabla();	
	private Dialogo dia_vacacion_escojer = new Dialogo();
	private Texto tex_nvacacion=new Texto();
	private Radio rad_conceder_vacacion = new Radio();
	private Calendario cal_fecha_desde=new Calendario();
	private Calendario cal_fecha_hasta=new Calendario();
	private Etiqueta eti_vacacion_disponible=new Etiqueta();
	private SeleccionTabla sel_tab_area=new SeleccionTabla();
	private SeleccionTabla sel_tab_departamento =new SeleccionTabla();
	private SeleccionTabla sel_tab_sucursal=new SeleccionTabla();
	private SeleccionTabla sel_tab_empleado=new SeleccionTabla();
	private Dialogo dia_anulacion_vacacion=new Dialogo();	
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();


	public pre_vacaciones() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		Boton bot_ver_vacaciones=new Boton();
		bot_ver_vacaciones.setValue("SOLICITAR VACACIONES");
		bot_ver_vacaciones.setMetodo("verVacaciones");
		bar_botones.getBot_insertar().setRendered(false);

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
		bar_botones.agregarBoton(bot_ver_vacaciones);


		//consultar vacaciones

		con_ver_vacaciones.setId("con_ver_vacaciones");
		con_ver_vacaciones.setTitle("VACACIONES DEL EMPLEADO");
		con_ver_vacaciones.setWidth("60%");
		con_ver_vacaciones.setHeight("50%");
		con_ver_vacaciones.setConsulta("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
				"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC " +
				") a where IDE_ASVAC=-1","IDE_ASVAC");
		con_ver_vacaciones.getTab_consulta_dialogo().setTipoFormulario(true);
		con_ver_vacaciones.getTab_consulta_dialogo().getGrid().setColumns(4);
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("IDE_ASVAC").setNombreVisual("CODIGO");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setNombreVisual("DIAS ACUMULADOS");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setNombreVisual("NRO DIAS ADICIONAL");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setNombreVisual("DIAS DESCONTADOS");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setNombreVisual("DIAS SOLICITADOS");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setNombreVisual("NRO TOTAL VACACIONES");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setNombreVisual("DIAS PENDIENTES");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("IDE_ASVAC").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setEtiqueta();
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setEtiqueta();      
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setEstilo("font-size: 17px;font-weight: bold");
		con_ver_vacaciones.getTab_consulta_dialogo().setMostrarNumeroRegistros(false);        
		con_ver_vacaciones.getBot_aceptar().setMetodo("aceptarConsultarVacaciones");   
		agregarComponente(con_ver_vacaciones);


		//  PERMISOS (division 1)

		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.getColumna("TIPO_ASPVH").setValorDefecto("2");// 1 vacaciones 
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
		tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setVisible(true);
		tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setMetodoChange("cambioFecha");		
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
		tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
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
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP2").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH ").setVisible(false);
		//GERENTE DE AREA GEN_IDE_GEEDP3
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);		
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);	
		tab_permisos.getColumna("detalle_aspvh").setRequerida(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("calcularDiasVacacion");
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setMetodoChange("calcularDiasVacacion");		
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);

		tab_permisos.getColumna("HORA_DESDE_ASPVH").setVisible(false);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setVisible(false);				
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setVisible(false);

		tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();

		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
		tab_permisos.getColumna("IDE_GEEST").setLectura(true);
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setLectura(true);
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setLectura(true);
		tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);
		tab_permisos.setCondicion("TIPO_ASPVH=2 AND IDE_GTEMP=-1");

		tab_permisos.dibujar();


		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("SOLICITUD DE VACACIONES");


		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir1(pat_panel1);
		agregarComponente(div_division);



		sel_tab_fecha_periodos.setId("sel_tab_fecha_periodos");
		sel_tab_fecha_periodos.setSeleccionTabla("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP " +
				"in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=-1 ) " +
				" AND ACTIVO_ASVAC=true","IDE_ASVAC");
		sel_tab_fecha_periodos.getBot_aceptar().setMetodo("aceptarSelTablaPeriodos");
		sel_tab_fecha_periodos.setTitle("SELECCIONE LA FECHA");
		sel_tab_fecha_periodos.setRadio();
		agregarComponente(sel_tab_fecha_periodos);

		//tabla3 ver vacaciones detallada

		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setSql("SELECT " +
				"(cast (PERIODO as integer) -1) ||' - '|| periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
				"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
				" FROM ( " +
				"SELECT IDE_ASVAC,cast(PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
				"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
				"FROM ( " +
				"SELECT IDE_ASVAC, " +
				"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
				"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
				"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
				"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
				"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true AND IDE_ASVAC=-1 " +
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

		//Dialogo de seleccionar vacaciones

		tex_nvacacion.setId("tex_nvacacion");
		tex_nvacacion.setSoloNumeros();

		tex_nvacacion.setMetodoChange("cambiaNumDiasVacacion");

		List lista11 = new ArrayList();
		Object fila111[] = {
				"true", "SI"
		};
		Object fila222[] = {
				"false", "NO"
		};
		lista11.add(fila111);
		lista11.add(fila222);
		rad_conceder_vacacion.setRadio(lista11);
		rad_conceder_vacacion.setMetodoChange("calcularPedidoVacaciones");

		cal_fecha_desde.setId("cal_fecha_desde");
		cal_fecha_hasta.setId("cal_fecha_hasta");

		Ajax fechaDesde_aja=new Ajax();
		fechaDesde_aja.setMetodo("calcularDiasFechaHasta");


		cal_fecha_desde.addClientBehavior("dateSelect", fechaDesde_aja);
		cal_fecha_desde.addClientBehavior("change", fechaDesde_aja);


		eti_vacacion_disponible.setId("eti_vacacion_disponible");

		Grid gri_cab_dia_vacacion=new Grid();
		gri_cab_dia_vacacion.setColumns(1);
		gri_cab_dia_vacacion.getChildren().add(eti_vacacion_disponible);

		Grid gri_escojer=new Grid();
		gri_escojer.setColumns(2);		
		gri_escojer.getChildren().add(new Etiqueta("Conceder Todo: "));
		gri_escojer.getChildren().add(rad_conceder_vacacion);
		gri_escojer.getChildren().add(new Etiqueta("Valor: "));
		gri_escojer.getChildren().add(tex_nvacacion);
		gri_escojer.getChildren().add(new Etiqueta("Fecha Inicio Vacación: "));
		gri_escojer.getChildren().add(cal_fecha_desde);
		gri_escojer.getChildren().add(new Etiqueta("Fecha Hasta Vacación: "));
		gri_escojer.getChildren().add(cal_fecha_hasta);

		Grid gri_total=new Grid();
		gri_total.setColumns(1);
		gri_total.getChildren().add(gri_cab_dia_vacacion);
		gri_total.getChildren().add(gri_escojer);

		gri_escojer.setStyle("width:" + (dia_vacacion_escojer.getAnchoPanel() - 5) + "px;height:" + dia_vacacion_escojer.getAltoPanel() + "px;overflow: auto;display: block;");

		dia_vacacion_escojer.setId("dia_vacacion_escojer");
		dia_vacacion_escojer.setWidth("40%");
		dia_vacacion_escojer.setHeight("40%");
		dia_vacacion_escojer.setTitle("ESCOGER VACACIONES");		
		dia_vacacion_escojer.setDialogo(gri_total);
		dia_vacacion_escojer.getBot_aceptar().setMetodo("aceptarEscojerVacaciones");		
		agregarComponente(dia_vacacion_escojer);



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
		dia_filtro_activo.setId("dia_activo");
		dia_filtro_activo.setTitle("ESCOGA EMPLEADO ACTIVO/INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		dia_filtro_activo.setDynamic(false);
		agregarComponente(dia_filtro_activo);

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,  " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' ||  " +
				"PRIMER_NOMBRE_GTEMP || ' ' ||  " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES  " +
				"from GTH_EMPLEADO " +
				"WHERE ACTIVO_GTEMP IN(false,true) " +
				"ORDER BY IDE_GTEMP ASC, " +
				"NOMBRES ASC ","IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado);

		sel_cal.setId("sel_cal");
		sel_cal.setMultiple(true);
		sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal);

		//boton anular
		Boton bot_anular_vacaion=new Boton();
		bot_anular_vacaion.setValue("ANULAR VACACION");
		bot_anular_vacaion.setMetodo("anularVacacion");
		bar_botones.agregarBoton(bot_anular_vacaion);
		
		cal_fecha_anula.setId("cal_fecha_anula");
		
		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:300px;");
		
		Grid gri_anular_vacacion=new Grid();
		gri_anular_vacacion.setColumns(2);
		gri_anular_vacacion.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_vacacion.getChildren().add(are_tex_razon_anula);
		gri_anular_vacacion.getChildren().add(new Etiqueta("DOCUMENTO DE ANULACIÓN"));
		gri_anular_vacacion.getChildren().add(tex_documento_anula);
		gri_anular_vacacion.getChildren().add(new Etiqueta("FECHA DE ANULACIÓN"));
		gri_anular_vacacion.getChildren().add(cal_fecha_anula);
		
		dia_anulacion_vacacion.setId("dia_anulacion_vacacion");
		dia_anulacion_vacacion.setWidth("50%");
		dia_anulacion_vacacion.setHeight("50%");
		dia_anulacion_vacacion.setTitle("ANULAR VACACIÓN");
		dia_anulacion_vacacion.setDynamic(false);
		dia_anulacion_vacacion.setDialogo(gri_anular_vacacion);
		gri_anular_vacacion.setStyle("width:" + (dia_anulacion_vacacion.getAnchoPanel() - 5) + "px;overflow:auto;");
		//are_tex_razon_anula.setStyle("width:" + (dia_anulacion_vacacion.getAnchoPanel() - 15) + "px;overflow:auto; height:"+ (dia_anulacion_vacacion.getAltoPanel() - 5)+"px");
		dia_anulacion_vacacion.getBot_aceptar().setMetodo("aceptarAnularVacacion");
		agregarComponente(dia_anulacion_vacacion);
		
		
		sel_tab_sucursal.setId("sel_tab_sucursal");
		sel_tab_sucursal.setTitle("SELECCION DE SUCURSAL");
		sel_tab_sucursal.setSeleccionTabla("SELECT ide_sucu,nom_sucu  FROM sis_sucursal where ide_sucu=-1 order by  nom_sucu", "ide_sucu");
		sel_tab_sucursal.getTab_seleccion().getColumna("nom_sucu").setFiltro(true);
		sel_tab_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_sucursal);
		
		sel_tab_area.setId("sel_tab_area");
		sel_tab_area.setTitle("SELECCION DE AREAS");
		sel_tab_area.setSeleccionTabla("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare=-1", "ide_geare");
		sel_tab_area.getTab_seleccion().getColumna("detalle_geare").setFiltro(true);
		sel_tab_area.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_area);
		
		sel_tab_departamento.setId("sel_tab_departamento");
		sel_tab_departamento.setTitle("SELECCION DE DEPARTAMENTOS");
		sel_tab_departamento.setSeleccionTabla("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in(-1) order by detalle_gedep", "ide_gedep");
		sel_tab_departamento.getTab_seleccion().getColumna("detalle_gedep").setFiltro(true);
		sel_tab_departamento.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_departamento);
		
		sel_tab_empleado.setId("sel_tab_empleado");
		sel_tab_empleado.setSeleccionTabla("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO EMP " +
				"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
				"WHERE ACTIVO_GTEMP = null", "IDE_GTEMP");
		sel_tab_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		sel_tab_empleado.setTitle("SELECCIONE EMPLEADO");
		agregarComponente(sel_tab_empleado);


	}	
	
	public void aceptarAnularVacacion(){
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
				if (cal_fecha_anula.getValue()!=null && !cal_fecha_anula.getValue().toString().isEmpty()) {
					tab_permisos.setValor("razon_anula_aspvh",are_tex_razon_anula.getValue().toString());
					tab_permisos.setValor("documento_anula_aspvh",tex_documento_anula.getValue().toString());
					tab_permisos.setValor("fecha_anula_aspvh", cal_fecha_anula.getFecha());
					tab_permisos.modificar(tab_permisos.getFilaActual());
					tab_permisos.guardar();
					utilitario.getConexion().agregarSqlPantalla("update asi_detalle_vacacion set activo_asdev=false, anulado_asdev=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
					utilitario.getConexion().agregarSqlPantalla("update asi_permisos_vacacion_hext set activo_aspvh=false,anulado_aspvh=true, ide_geest="+utilitario.getVariable("p_gen_estado_inactivo")+" where ide_aspvh="+tab_permisos.getValorSeleccionado());
					tab_permisos.imprimirSql();
					guardarPantalla();
					dia_anulacion_vacacion.cerrar();
					tab_permisos.ejecutarSql();
				} else {
					utilitario.agregarMensajeInfo("No se puede anular la vacación", "Debe seleccionar una fecha para para anular la vacación");
				}			
		} else {
			utilitario.agregarMensajeInfo("No se puede anular la vacación", "Debe ingresar una razon para anular la vacación");
		}
	}

	public void cambiaNumDiasVacacion(AjaxBehaviorEvent evt){
		if(tex_nvacacion.getValue()!=null && !tex_nvacacion.getValue().toString().isEmpty()){
			tex_nvacacion.setValue(tex_nvacacion.getValue());
			utilitario.addUpdate("tex_nvacacion");
		}else{
			cal_fecha_desde.setValue(null);
			cal_fecha_hasta.setValue(null);
			utilitario.addUpdate("cal_fecha_desde,cal_fecha_hasta");
		}
	}

	public void calcularDiasVacacion(DateSelectEvent evt){
		tab_permisos.modificar(evt);
		if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
				|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
			return;
		}
		if(aut_empleado.getValor()!=null){
			try {
				int nro_dias=0;
				if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
					utilitario.agregarMensajeError("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
				}else{						
					nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
					tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",nro_dias+"");
					utilitario.addUpdateTabla(tab_permisos, "NRO_DIAS_ASPVH", "");
				}	
			} catch (Exception e) {
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
			}					
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}	
	}

	public void calcularDiasVacacion(SelectEvent evt){
		tab_permisos.modificar(evt);
		if(aut_empleado.getValor()!=null){				
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{				
				int nro_dias=0;		
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
				tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",nro_dias+"");
				utilitario.addUpdateTabla(tab_permisos, "NRO_DIAS_ASPVH", "");
				System.out.println("numero de dias: "+nro_dias);
			}					
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}	
	}

	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_permisos.setCondicion("TIPO_ASPVH=2 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_permisos.ejecutarSql();
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_permisos.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado,sel_cal,dia_filtro_activo");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
	}

	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new SimpleDateFormat ("hh:mm:ss");
			String hora1 = hora_ini;
			String hora2 = hora_fin;
			Date date1, date2, dateNueva;
			date1 = dateFormat.parse(hora1);
			date2 = dateFormat.parse(hora2);
			if (date1.compareTo(date2) > 0){ 
				return true;
			}

		} catch (ParseException parseException){
			parseException.printStackTrace();
		}
		return false;
	}


	public boolean validarSolicitudVacaciones(){
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
		if (tab_permisos.getValor("FECHA_SOLICITUD_ASPVH")==null || tab_permisos.getValor("FECHA_SOLICITUD_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Fecha de Solicitud");
			return false;
		}
		if (tab_permisos.getValor("NRO_DIAS_ASPVH")==null || tab_permisos.getValor("NRO_DIAS_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Numero de Dias");
			return false;
		}
		if (tab_permisos.getValor("detalle_aspvh")==null || tab_permisos.getValor("detalle_aspvh").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Detalle de la Vacacion");
			return false;
		}
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
			utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return false;
		}		
		return true;
	}
	//abre el dialogo para visualizar las vacaciones por medio del periodo
	public void verVacaciones(){
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){
				sel_tab_fecha_periodos.getTab_seleccion().setSql("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP " +
						"in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP= "+ide_geedp_activo+") " +
						" AND ACTIVO_ASVAC=true");
				sel_tab_fecha_periodos.getTab_seleccion().ejecutarSql();				
				sel_tab_fecha_periodos.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No puede solicitar vacaciones", "No existe un contrato activo del empleado");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar los registros", "Debe seleccionar un Empleado");
		}		
	}
	//muestra un detalle de los dias que tiene de vacacion el empleado
	public void aceptarSelTablaPeriodos(){
		if(sel_tab_fecha_periodos.getValorSeleccionado()!=null && !sel_tab_fecha_periodos.getValorSeleccionado().isEmpty()){		
			sel_tab_fecha_periodos.cerrar();
			con_ver_vacaciones.dibujar();
			con_ver_vacaciones.getTab_consulta_dialogo().setSql("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
					"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
					"FROM ( " +
					"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
					"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
					"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
					"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
					"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC " +
					") a where IDE_ASVAC="+Long.parseLong(sel_tab_fecha_periodos.getValorSeleccionado()));
			con_ver_vacaciones.getTab_consulta_dialogo().ejecutarSql();
			tab_tabla3.setSql("SELECT " +
					"(cast (PERIODO as integer)-1) ||' - '|| periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
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
					"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true AND IDE_ASVAC="+Long.parseLong(sel_tab_fecha_periodos.getValorSeleccionado())+
					")a GROUP BY a.periodo,a.IDE_ASVAC " +
					")b ORDER BY PERIODO DESC");													
			tab_tabla3.ejecutarSql();
			con_ver_vacaciones.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede consultar las vacaciones", "Debe seleccionar solo un Periodo");
		}
	}
	//consulta las vacaciones pendientes del empleado
	public void aceptarConsultarVacaciones(){
		if(con_ver_vacaciones.getTab_consulta_dialogo().getValor("DIAS_PENDIENTES")!="0"){
			con_ver_vacaciones.cerrar();
			eti_vacacion_disponible.setValue("Total de Vaciones Disponibles: "+con_ver_vacaciones.getTab_consulta_dialogo().getValor("DIAS_PENDIENTES"));
			tex_nvacacion.setValue(null);
			cal_fecha_desde.setValue(null);
			cal_fecha_hasta.setValue(null);
			dia_vacacion_escojer.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede solicitar vacaciones", "Usted No tiene dias pendientes");
		}
	}
	// calcula los dias de vacacion solicitados e inserta en un nuevo formulario la solicitud
	public void aceptarEscojerVacaciones(){
		int dias_solicitados=0;
		double dias_pend_original=0;
		double dias_pend_redondeados=0;
		int dias_pendientes=0;
		if(rad_conceder_vacacion.getValue()!=null && !rad_conceder_vacacion.getValue().toString().isEmpty()){
			if(tex_nvacacion.getValue()!=null && !tex_nvacacion.getValue().toString().isEmpty()){
				if(cal_fecha_desde.getValue()!=null && !cal_fecha_desde.getValue().toString().isEmpty()){
					if(cal_fecha_hasta.getValue()!=null && !cal_fecha_hasta.getValue().toString().isEmpty()){			
						dias_solicitados=pckUtilidades.CConversion.CInt(tex_nvacacion.getValue().toString());						
						dias_pend_original=Double.parseDouble(con_ver_vacaciones.getTab_consulta_dialogo().getValor("DIAS_PENDIENTES"));
						dias_pend_redondeados=Math.round(dias_pend_original);
						dias_pendientes=(int)dias_pend_redondeados;						
						if(dias_solicitados<=dias_pendientes){							
							//TablaGenerica tab_emp=new TablaGenerica();
							tab_permisos.insertar();
							tab_permisos.setValor("IDE_GEEDP",ide_geedp_activo);
							tab_permisos.setValor("ide_gtemp", aut_empleado.getValor());
							tab_permisos.setValor("fecha_desde_aspvh",cal_fecha_desde.getFecha());
							tab_permisos.setValor("fecha_hasta_aspvh",cal_fecha_hasta.getFecha());
							tab_permisos.setValor("nro_dias_aspvh",tex_nvacacion.getValue().toString());
							cargarValoresFechaCombo();
							dia_vacacion_escojer.cerrar();
						}else{
							utilitario.agregarMensajeInfo("No se puede Generar la Solicitud", "Los dias solicitados son mayor a a los dias pendientes ingrese otro día");
						}						
					}else{
						utilitario.agregarMensajeInfo("No se puede Generar la Solicitud", "Debe ingresar la Fecha Hasta");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede Generar la Solicitud", "Debe ingresar la Fecha Desde");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede Generar la Solicitud", "Debe ingresar el número de días");
			}	
		}else{
			utilitario.agregarMensajeInfo("No se puede Generar la Solicitud", "Debe escoger una opción");
		}
	}
	//calcula el pedido de las vacaciones de los dias pendientes cuando selecciona en el radio si - no
	public void calcularPedidoVacaciones(){		
		double diasOriginal=0;
		double diasOrfin=0;		
		int diasv=0;		
		if(rad_conceder_vacacion.getValue().equals("true")){				
			diasOriginal=Double.parseDouble(con_ver_vacaciones.getTab_consulta_dialogo().getValor("DIAS_PENDIENTES"));
			diasOrfin=Math.round(diasOriginal);
			diasv=(int)diasOrfin;					
			tex_nvacacion.setValue(diasv);
			tex_nvacacion.setDisabled(true);
			cal_fecha_desde.setValue(null);
			cal_fecha_hasta.setValue(null);
			cal_fecha_hasta.setDisabled(true);
			utilitario.addUpdate("tex_nvacacion,cal_fecha_desde,cal_fecha_hasta");			
		}else{				
			tex_nvacacion.setValue(null);
			tex_nvacacion.setDisabled(false);
			cal_fecha_desde.setValue(null);
			cal_fecha_hasta.setValue(null);
			cal_fecha_hasta.setDisabled(true);
			utilitario.addUpdate("tex_nvacacion,cal_fecha_desde,cal_fecha_hasta");
		}		
	}

	public void calcularDiasFechaHasta(DateSelectEvent evt){
		if(tex_nvacacion.getValue()!=null && !tex_nvacacion.toString().isEmpty()){
			cal_fecha_hasta.setValue(ingresaDiasVacacion());
			utilitario.addUpdate("cal_fecha_hasta");	
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular las vacaciones", "Debe ingresar un día");
		}	
	}

	public void calcularDiasFechaHasta(AjaxBehaviorEvent evt){
		if(tex_nvacacion.getValue()!=null && !tex_nvacacion.toString().isEmpty()){
			cal_fecha_hasta.setValue(ingresaDiasVacacion());
			utilitario.addUpdate("cal_fecha_hasta");
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular las vacaciones", "Debe ingresar un día");
		}
	}
	//suma los dias solicitados a la fecha se la solicitud 
	private Date ingresaDiasVacacion(){
		double diasOriginal=0;
		double diasOrfin=0;
		if (tex_nvacacion.getValue()!=null && !tex_nvacacion.toString().isEmpty()){
			if(cal_fecha_desde.getValue()!=null && !cal_fecha_desde.getValue().toString().isEmpty()){
				Date fechaDesdeV=new Date();
				Date fechaHasta=new Date();
				fechaDesdeV=(Date)cal_fecha_desde.getValue();			
				int dias=pckUtilidades.CConversion.CInt(tex_nvacacion.getValue().toString());
				if(dias>0){
					fechaHasta=utilitario.sumarDiasFecha(fechaDesdeV, dias-1);					
					return fechaHasta;	
				}else{
					utilitario.agregarMensajeInfo("No se puede solicitar la vacación", "Debe ingresar día mayor a cero");
				}
			}
		}
		return null;
	}
	//inserta en la tabla de asi_vacacion cuando guarda la solicitud e la vacacion
	public void insertarDatosDetalleVacacion() {  
					
		TablaGenerica tab_codigo_vacacion=utilitario.consultar("select * from asi_vacacion where activo_asvac=true and ide_gtemp in(select ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp_activo+")");
		

		TablaGenerica tab_detalle_vacacion = new  TablaGenerica();
		tab_detalle_vacacion.setTabla("asi_detalle_vacacion", "ide_asdev", -1);
		tab_detalle_vacacion.setCondicion("ide_asdev=-1");
		tab_detalle_vacacion.ejecutarSql();
		tab_detalle_vacacion.insertar();
		tab_detalle_vacacion.setValor("ide_aspvh", tab_permisos.getValor("ide_aspvh"));
		tab_detalle_vacacion.setValor("ide_asvac", tab_codigo_vacacion.getValor("ide_asvac"));
		tab_detalle_vacacion.setValor("fecha_novedad_asdev",tab_permisos.getValor("fecha_solicitud_aspvh"));
		tab_detalle_vacacion.setValor("dia_descontado_asdev", tab_permisos.getValor("nro_dias_aspvh"));
		tab_detalle_vacacion.setValor("observacion_asdev", tab_permisos.getValor("detalle_aspvh"));
		tab_detalle_vacacion.setValor("activo_asdev", "true");		
		tab_detalle_vacacion.guardar();         
	}
	//carga los meses y años dependiendo de la fecha de la solicitud
	private void cargarValoresFechaCombo(){				
		if(tab_permisos.isEmpty()==false){
			String meses="";
			meses=tab_permisos.getValor("FECHA_SOLICITUD_ASPVH");
//			tab_permisos.setValor("IDE_GEMES", String.valueOf(utilitario.getMes(meses)));			
			TablaGenerica tab_anios=utilitario.consultar("select * from gen_anio where detalle_geani='"+utilitario.getAnio(meses)+"'");
			if(tab_anios.getTotalFilas()>0){				
//				tab_permisos.setValor("IDE_GEANI", tab_anios.getValor("IDE_GEANI"));				
			}else{
//				tab_permisos.setValor("IDE_GEANI", null);
			}
		}		
	}
	//cambios de fecha en el dialogo de solicitar la vacacion
	public void cambioFecha(DateSelectEvent evt){
		tab_permisos.modificar(evt);
		cargarValoresFechaCombo();
		utilitario.addUpdateTabla(tab_permisos, "IDE_GEMES,IDE_GEANI", "");
	}

	public void cambioFecha(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);
		cargarValoresFechaCombo();
		utilitario.addUpdateTabla(tab_permisos, "IDE_GEMES,IDE_GEANI", "");
	}

	public void anularVacacion(){
		if(aut_empleado.getValue()!=null && !aut_empleado.getValor().toString().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("ACTIVO_ASPVH").equalsIgnoreCase("false") || tab_permisos.getValor("IDE_GEEST").equalsIgnoreCase(utilitario.getVariable("p_gen_estado_inactivo"))){
					utilitario.agregarMensajeInfo("No se puede anular", "El registro se encuentra Inactivo o Anulado");

				}else{
					TablaGenerica tab_anula_vacacion=utilitario.consultar("SELECT * FROM (" +
							"SELECT * FROM asi_permisos_vacacion_hext where activo_aspvh=true and ide_geest=1 order by ide_aspvh desc ) a " +
							"limit 1");
					if (tab_anula_vacacion.getTotalFilas()==1) {					
						dia_anulacion_vacacion.dibujar();
					}else{						
					}
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede anular la vacación", "El Empleado no tiene registros en la cabecera");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede anular la vacación", "Debe ingresar el nombre de un empleado");
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (aut_empleado.getValor()!=null){
			if (validarSolicitudVacaciones()){	
				
				if (tab_permisos.guardar()){
					
					insertarDatosDetalleVacacion();
					System.out.println("guardar pantalla "+utilitario.getConexion().getSqlPantalla());
					guardarPantalla();					
				}
				
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar la vacacion", "Debe seleccionar un Empleado");
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		System.out.print("p_parametros...  "+p_parametros);
		if (rep_reporte.getReporteSelecionado().equals("Detalle Vacaciones")){
			if (tab_permisos.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();		
					p_parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(ide_geedp_activo));
					p_parametros.put("titulo", " EMGIRS GERENCIA ADMINISTRATIVA - FINANCIERA DEPARTAMENTO DE TALENTO HUMANO VACACIONES");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No contiene registro de permisos");
			}	
		}

		else if (rep_reporte.getReporteSelecionado().equals("Detalle Vacaciones Fecha")){

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
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,  " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' ||  " +
							"PRIMER_NOMBRE_GTEMP || ' ' ||  " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES  " +
							"from GTH_EMPLEADO " +
							"WHERE ACTIVO_GTEMP IN("+lis_activo.getSeleccionados()+") " +
							"ORDER BY IDE_GTEMP ASC, " +
							"NOMBRES ASC ");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					sel_cal.cerrar();
					set_empleado.dibujar();
				}else  {
					utilitario.agregarMensajeInfo("ERROR AL GENERAR REPORTE","Las fechas no son validas");
				}
			}
			else if(set_empleado.isVisible()){				
				if(set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()){				

					System.out.println(""+set_empleado.getSeleccionados());				
					p_parametros.put("IDE_GTEMP",set_empleado.getSeleccionados());
					p_parametros.put("titulo", " EMGIRS GERENCIA ADMINISTRATIVA - FINANCIERA DEPARTAMENTO DE TALENTO HUMANO VACACIONES POR FECHAS");
					System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		} else	if (rep_reporte.getReporteSelecionado().equals("Detalle")){
			//	if (tab_permisos.getTotalFilas()>0) {
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();		

				p_parametros.put("IDE_GTEMP",aut_empleado.getValor());
				p_parametros.put("ACTIVO_ASPVH","false,true");
				p_parametros.put("titulo", " GERENCIA ADMINISTRATIVA DEPARTAMENTO DE TALENTO HUMANO PERMISOS CONCEDIDAS Y NO CONCEDIDAS");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						

				sef_reporte.dibujar();
			}
			//	}else{
			//	utilitario.agregarMensajeInfo("No se puede continuar", "No contiene registro de permisos");
			//}
		}
		else if(rep_reporte.getReporteSelecionado().equals("Periodos de Vacaciones Pendientes")){
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					p_parametros.put("ide_sucu_t", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					p_parametros.put("ide_geare", sel_tab_area.getSeleccionados());
					sel_tab_departamento.getTab_seleccion().setSql("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in("+sel_tab_area.getSeleccionados()+") order by detalle_gedep");
					sel_tab_departamento.getTab_seleccion().ejecutarSql();
					sel_tab_area.cerrar();
					sel_tab_departamento.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una Área");
				}
			}else if(sel_tab_departamento.isVisible()){
				if(sel_tab_departamento.getSeleccionados()!=null && ! sel_tab_departamento.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gedep", sel_tab_departamento.getSeleccionados());					
					sel_tab_departamento.cerrar();			
					sel_tab_empleado.getTab_seleccion().setSql("SELECT emp.ide_gtemp,emp.documento_identidad_gtemp, " +
							"emp.apellido_paterno_gtemp ||' '|| emp.apellido_materno_gtemp ||' '|| emp.primer_nombre_gtemp ||' '|| emp.segundo_nombre_gtemp as nombres FROM gth_empleado emp " +
							"inner join gen_empleados_departamento_par edp on emp.ide_gtemp=edp.ide_gtemp " +
							"where edp.ide_gedep in ("+sel_tab_departamento.getSeleccionados()+") and edp.activo_geedp=true and emp.activo_gtemp=true");
							set_empleado.getTab_seleccion().ejecutarSql();							
							sel_tab_empleado.dibujar();															
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
				}
			}else if(sel_tab_empleado.isVisible()){
				if(sel_tab_empleado.getSeleccionados()!=null && !sel_tab_empleado.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gtemp", sel_tab_empleado.getSeleccionados());
					p_parametros.put("titulo","PERIODOS DE VACACIONES PENDIENTES");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());					
					sel_tab_empleado.cerrar();
					sef_reporte.dibujar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un empleado");
				}
			}
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

	public Dialogo getDia_vacaciones() {
		return dia_vacaciones;
	}

	public void setDia_vacaciones(Dialogo dia_vacaciones) {
		this.dia_vacaciones = dia_vacaciones;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}

	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}

	public SeleccionTabla getSel_tab_fecha_periodos() {
		return sel_tab_fecha_periodos;
	}

	public void setSel_tab_fecha_periodos(SeleccionTabla sel_tab_fecha_periodos) {
		this.sel_tab_fecha_periodos = sel_tab_fecha_periodos;
	}

	public Consulta getCon_ver_vacaciones() {
		return con_ver_vacaciones;
	}

	public void setCon_ver_vacaciones(Consulta con_ver_vacaciones) {
		this.con_ver_vacaciones = con_ver_vacaciones;
	}

	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}

	public Dialogo getDia_vacacion_escojer() {
		return dia_vacacion_escojer;
	}

	public void setDia_vacacion_escojer(Dialogo dia_vacacion_escojer) {
		this.dia_vacacion_escojer = dia_vacacion_escojer;
	}

	public SeleccionTabla getSel_tab_area() {
		return sel_tab_area;
	}

	public void setSel_tab_area(SeleccionTabla sel_tab_area) {
		this.sel_tab_area = sel_tab_area;
	}

	public SeleccionTabla getSel_tab_departamento() {
		return sel_tab_departamento;
	}

	public void setSel_tab_departamento(SeleccionTabla sel_tab_departamento) {
		this.sel_tab_departamento = sel_tab_departamento;
	}

	public SeleccionTabla getSel_tab_sucursal() {
		return sel_tab_sucursal;
	}

	public void setSel_tab_sucursal(SeleccionTabla sel_tab_sucursal) {
		this.sel_tab_sucursal = sel_tab_sucursal;
	}

	public SeleccionTabla getSel_tab_empleado() {
		return sel_tab_empleado;
	}

	public void setSel_tab_empleado(SeleccionTabla sel_tab_empleado) {
		this.sel_tab_empleado = sel_tab_empleado;
	}

	public Dialogo getDia_anulacion_vacacion() {
		return dia_anulacion_vacacion;
	}

	public void setDia_anulacion_vacacion(Dialogo dia_anulacion_vacacion) {
		this.dia_anulacion_vacacion = dia_anulacion_vacacion;
	}	
}
