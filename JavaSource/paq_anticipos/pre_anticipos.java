package paq_anticipos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.text.TabableView;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
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
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.Upload;
import java.util.Locale;

public class pre_anticipos extends Pantalla {

	private Tabla tab_anticipo=new Tabla();
	private Tabla tab_capacidad=new Tabla();
	private Tabla tab_amortizacion=new Tabla();
	private Tabla tab_garante=new Tabla();	
	private Tabla tab_anticipo_interes=new Tabla();
	private Tabla tab_anticipo_abonos=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Dialogo dia_roles_empleado=new Dialogo();
	private Tabla tab_ingresos_egresos_emp=new Tabla();
	private Tabla tab_rubro_detalle_pago=new Tabla();
	private Etiqueta eti_tot_rubros_pago=new Etiqueta();
	private Dialogo dia_filtro_activo_anticipo = new Dialogo();
	private	ListaSeleccion lis_activo_anticipo=new ListaSeleccion();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);

	@EJB
	private ServicioEmpleado serv_empleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	
	private Tabulador tab_tabulador=new Tabulador();
	private Etiqueta eti_prom_ing=new Etiqueta();
	private Etiqueta eti_prom_egr=new Etiqueta();
	private Etiqueta eti_porcentaje=new Etiqueta();
	private Etiqueta eti_aprobado=new Etiqueta();

	private Boton bot_calificar_anticipo=new Boton();
	private Boton bot_aprobar_anticipo=new Boton();
	private Boton bot_pre_cancelacion=new Boton();
	private Boton bot_abonar=new Boton();
	private Boton bot_agregar_garante=new Boton();

	private Confirmar con_datos_memo=new Confirmar();
	private Confirmar con_guardar=new Confirmar();

	private Dialogo dia_ingreso_memo=new Dialogo();
	private AutoCompletar aut_empleado_autoriza_memo=new AutoCompletar();
	private Texto tex_num_memo=new Texto();
	private Calendario cal_fecha_autorizacion_memo=new Calendario();
	private AreaTexto art_razon_aut_memo=new AreaTexto();
	private Upload upl_autoriza_memo=new Upload();

	private Etiqueta eti_empleado=new Etiqueta();
	private Etiqueta eti_num_memo=new Etiqueta();
	private Etiqueta eti_fecha_aut=new Etiqueta();
	private Etiqueta eti_raz_aut=new Etiqueta();
	private Etiqueta eti_archivo_adjunto=new Etiqueta();

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	private Dialogo dia_precancelacion=new Dialogo();
	private Tabla tab_pagos_anticipos=new Tabla();
	private Tabla tab_precancelacion=new Tabla();
	private Etiqueta eti_tot_precancelar=new Etiqueta();
	private Etiqueta eti_tot_adeuda=new Etiqueta();

	private Dialogo dia_datos_precancelacion=new Dialogo();
	private Tabla tab_datos_precancelacion=new Tabla();

	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Dialogo dia_filtro_activo = new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_anticipo=new SeleccionTabla();

	private Dialogo dia_abonos = new Dialogo();
	private Consulta con_abonos_anticipo=new Consulta();
	private Tabla tab_telefono_garante=new Tabla();
	private Texto tex_total_ingresos = new Texto();
	private Texto tex_total_egresos = new Texto();
	private Texto tex_valor_a_recibir = new Texto();
	public pre_anticipos() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");

		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");


		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);

		bot_calificar_anticipo.setValue("Calificar");
		bot_calificar_anticipo.setMetodo("calificarAnticipo");
		bar_botones.agregarBoton(bot_calificar_anticipo);

		bot_aprobar_anticipo.setValue("Aprobar");
		bot_aprobar_anticipo.setMetodo("aprobarAnticipo");
		bar_botones.agregarBoton(bot_aprobar_anticipo);

		bot_pre_cancelacion.setValue("Pre-Cancelar");
		bot_pre_cancelacion.setMetodo("preCancelarAnticipo");
		bar_botones.agregarBoton(bot_pre_cancelacion);

		bot_abonar.setValue("Abonar");
		bot_abonar.setMetodo("abonarAnticipo");
		bar_botones.agregarBoton(bot_abonar);

		bot_agregar_garante.setValue("Agregar Garante Funcionario");
		bot_agregar_garante.setMetodo("agregarGarante");
		bar_botones.agregarBoton(bot_agregar_garante);


		Boton bot_anular=new Boton();
		bot_anular.setValue("Anular");
		bot_anular.setMetodo("anularAnticipo");
		bar_botones.agregarBoton(bot_anular);


		bar_botones.agregarReporte();

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE", "IDE_GTEMP");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado);


		set_empleado_anticipo.setId("set_empleado_anticipo");
		set_empleado_anticipo.setSeleccionTabla("select EMP.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES,EMP.DOCUMENTO_IDENTIDAD_GTEMP " +
				"from NRH_ANTICIPO  ant " +
				"left join GTH_EMPLEADO emp on EMP.IDE_GTEMP=ANT.IDE_GTEMP " +
				"where ant.activo_nrant=TRUE  " +
				"GROUP BY EMP.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP , " +
				"EMP.APELLIDO_MATERNO_GTEMP , " +
				"EMP.PRIMER_NOMBRE_GTEMP , " +
				"EMP.SEGUNDO_NOMBRE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP ", "IDE_GTEMP");
		set_empleado_anticipo.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado_anticipo.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado_anticipo.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado_anticipo.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado_anticipo);


		//  ANTICIPOS (division 1)

		tab_anticipo.setId("tab_anticipo");
		tab_anticipo.setTabla("NRH_ANTICIPO", "IDE_NRANT", 1);
		tab_anticipo.setTipoFormulario(true);
		tab_anticipo.getColumna("ACTIVO_NRANT").setCheck();
		tab_anticipo.getColumna("ACTIVO_NRANT").setValorDefecto("true");
		tab_anticipo.getColumna("ACTIVO_NRANT").setLectura(true);
		tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setValorDefecto(utilitario.getFechaActual());
		tab_anticipo.getColumna("IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setLectura(true);
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setCombo(tab_anticipo.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setLectura(true);
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setMetodoChange("cambiaMontoSolicitado");
		tab_anticipo.getColumna("IDE_NRMOA").setCombo("NRH_MOTIVO_ANTICIPO","IDE_NRMOA","DETALLE_NRMOA","ACTIVO_NRMOA=true");
		tab_anticipo.getColumna("NRO_ANTICIPO_NRANT").setLectura(true);
		tab_anticipo.getColumna("GEN_IDE_GEEDP3").setCombo(tab_anticipo.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_anticipo.getColumna("GEN_IDE_GEEDP3").setLectura(true);

		tab_anticipo.getColumna("CALIFICADO_NRANT").setCheck();
		tab_anticipo.getColumna("CALIFICADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("CALIFICADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("ANULADO_NRANT").setCheck();
		tab_anticipo.getColumna("ANULADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("ANULADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("APROBADO_NRANT").setCheck();
		tab_anticipo.getColumna("APROBADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("APROBADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("IDE_GTEMP").setVisible(false);

		tab_anticipo.getColumna("NRO_MEMO_NRANT").setLectura(true);
		tab_anticipo.getColumna("FECHA_AUTORIZA_NRANT").setLectura(true);
		tab_anticipo.getColumna("RAZON_AUTORIZA_NRANT").setLectura(true);

		tab_anticipo.getColumna("ANTICIPO_NRANT").setCheck();
		tab_anticipo.getColumna("ANTICIPO_NRANT").setVisible(false);
		tab_anticipo.getColumna("ANTICIPO_NRANT").setValorDefecto("true");

		tab_anticipo.getColumna("ABONO_NRANT").setCheck();
		tab_anticipo.getColumna("ABONO_NRANT").setLectura(true);
		tab_anticipo.getColumna("ABONO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("REAPROBADO_NRANT").setCheck();
		tab_anticipo.getColumna("REAPROBADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("REAPROBADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("NRO_MES_NRANT").setRequerida(false);
		tab_anticipo.getColumna("MONTO_APROBADO_NRANT").setLectura(true);

		tab_anticipo.getColumna("ARCHIVO_MEMO_NRANT").setLectura(true);
		tab_anticipo.getColumna("ARCHIVO_MEMO_NRANT").setUpload("archivos");
		tab_anticipo.getGrid().setColumns(4);
		tab_anticipo.agregarRelacion(tab_capacidad);
		tab_anticipo.agregarRelacion(tab_anticipo_interes);
		tab_anticipo.agregarRelacion(tab_garante);


		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP=-1");
		tab_anticipo.setCampoOrden("IDE_NRANT DESC");
		tab_anticipo.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anticipo);

		// DIVISION 2  (TABULADOR - (CAPACIDAD DE PAGO/AMORTIZACION/GARANTE))

		tab_tabulador.setId("tab_tabulador");

		// CAPACIDAD DE PAGO (TABULADOR OPCION 1)		

		tab_capacidad.setId("tab_capacidad");
		tab_capacidad.setIdCompleto("tab_tabulador:tab_capacidad");
		tab_capacidad.setTabla("NRH_CAPACIDAD_PAGO", "IDE_NRCAP", 2);
		tab_capacidad.agregarRelacion(tab_rubro_detalle_pago);
		tab_capacidad.setTipoFormulario(true);
		tab_capacidad.getColumna("TOTAL_INGRESO_NRCAP").setLectura(true);
		tab_capacidad.getColumna("TOTAL_EGRESO_NRCAP").setLectura(true);
		tab_capacidad.getColumna("PORCENTAJE_ENDEUDA_NRCAP").setLectura(true);
		tab_capacidad.getColumna("CUOTA_LIMITE_NRCAP").setLectura(true);
		tab_capacidad.getColumna("CUOTA_MENSUAL_NRCAP").setEtiqueta();
		tab_capacidad.getColumna("CUOTA_MENSUAL_NRCAP").setEstilo("font-size: 14px;color: red;font-weight: bold");
		tab_capacidad.getColumna("NRO_MES_NRCAP").setMetodoChange("calcularValorCuota");
		tab_capacidad.getColumna("ACTIVO_NRCAP").setCheck();
		tab_capacidad.getColumna("ACTIVO_NRCAP").setValorDefecto("true");
		tab_capacidad.getColumna("FECHA_CALCULO_NRCAP").setRequerida(true);
		tab_capacidad.getColumna("NRO_MES_NRCAP").setRequerida(true);
		tab_capacidad.getColumna("TOTAL_INGRESO_NRCAP").setRequerida(true);
		tab_capacidad.getColumna("TOTAL_EGRESO_NRCAP").setRequerida(true);
		tab_capacidad.getColumna("MONTO_RECIBIR_NRCAP").setRequerida(true);
		tab_capacidad.getColumna("MONTO_RECIBIR_NRCAP").setMetodoChange("cambiaMontoRecibir");
		tab_capacidad.setMostrarNumeroRegistros(false);
		tab_capacidad.getGrid().setColumns(4);

		tab_capacidad.getColumna("FECHA_CALCULO_NRCAP").setValorDefecto(utilitario.getFechaActual());
		tab_capacidad.getColumna("FECHA_CALCULO_NRCAP").setMetodoChange("cambiaFechaCalculo");
		tab_capacidad.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_capacidad);

		eti_aprobado.setId("eti_aprobado");
		eti_aprobado.setValue("");
		eti_aprobado.setStyle("font-size: 13px;color: red;font-weight: bold");
		eti_porcentaje.setId("eti_porcentaje");
		eti_porcentaje.setStyle("font-size: 13px;color: red;font-weight: bold");
		eti_porcentaje.setValue("CAPACIDAD DE PAGO");


		Grid gri_resul_capacidad=new Grid();
		gri_resul_capacidad.setWidth("100%");
		gri_resul_capacidad.getChildren().add(eti_porcentaje);
		gri_resul_capacidad.getChildren().add(eti_aprobado);

		pat_panel2.setHeader(gri_resul_capacidad);

		ItemMenu itm_ver_ing_egre=new ItemMenu();
		itm_ver_ing_egre.setValue("Ver Ingresos/Egresos");
		itm_ver_ing_egre.setIcon("ui-icon-contact");
		itm_ver_ing_egre.setMetodo("verIngresosEgresosEmpleado");

		pat_panel2.getMenuTabla().getChildren().add(itm_ver_ing_egre);

		// RUBRO DETALLE PAGO
		tab_rubro_detalle_pago.setId("tab_rubro_detalle_pago");
		tab_rubro_detalle_pago.setIdCompleto("tab_tabulador:tab_rubro_detalle_pago");
		tab_rubro_detalle_pago.setTabla("NRH_RUBRO_DETALLE_PAGO", "IDE_NRRDP",6);

		tab_rubro_detalle_pago.getColumna("ACTIVO_NRRDP").setCheck();
		tab_rubro_detalle_pago.getColumna("ACTIVO_NRRDP").setValorDefecto("true");
		tab_rubro_detalle_pago.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB", "ANTICIPO_NRRUB=true");
		tab_rubro_detalle_pago.getColumna("IDE_NRRUB").setMetodoChange("seleccionaRubroDetallePago");
		tab_rubro_detalle_pago.getColumna("IDE_NRRUB").setUnico(true);
		tab_rubro_detalle_pago.getColumna("IDE_NRCAP").setUnico(true);
		tab_rubro_detalle_pago.getColumna("PORCENTAJE_NRRDP").setMetodoChange("calcularValorDescuento");
		tab_rubro_detalle_pago.getColumna("VALOR_DESCUENTO_NRRDP").setMetodoChange("calcularValorDescuento2");
		tab_rubro_detalle_pago.getColumna("VALOR_RUBRO_NRRDP").setLectura(false);
		tab_rubro_detalle_pago.getColumna("VALOR_RUBRO_NRRDP").setMetodoChange("calcularValorDescuento2");
		tab_rubro_detalle_pago.getColumna("FECHA_PAGO_RUBRO_NRRDP").setLectura(false);
		tab_rubro_detalle_pago.getColumna("FECHA_PAGO_RUBRO_NRRDP").setRequerida(true);
		tab_rubro_detalle_pago.setMostrarNumeroRegistros(true);
		tab_rubro_detalle_pago.setTipoFormulario(true);
		tab_rubro_detalle_pago.getGrid().setColumns(4);
		tab_rubro_detalle_pago.dibujar();

		PanelTabla pat_panel6=new PanelTabla();
		pat_panel6.setPanelTabla(tab_rubro_detalle_pago);

		Etiqueta eti_tit_rubro_det_pago=new Etiqueta(); 
		eti_tit_rubro_det_pago.setValue("RUBRO DETALLE PAGO ");
		eti_tit_rubro_det_pago.setStyle("font-size: 13px;color: black;font-weight: bold");

		pat_panel6.setHeader(eti_tit_rubro_det_pago);

		eti_tot_rubros_pago.setId("eti_tot_rubros_pago");
		eti_tot_rubros_pago.setValue("TOTAL DETALLE RUBROS PAGO: ");
		eti_tot_rubros_pago.setStyle("font-size: 14px;color: red;font-weight: bold");

		Grid gri_tot_rub_det_pag=new Grid();
		gri_tot_rub_det_pag.setWidth("100%");
		gri_tot_rub_det_pag.getChildren().add(eti_tot_rubros_pago);

		pat_panel6.setFooter(gri_tot_rub_det_pag);

		Division div_cap_pago=new Division();
		div_cap_pago.dividir2(pat_panel2, pat_panel6, "50%", "V");

		// AMORTIZACION (TABULADOR OPCION 2)

		tab_anticipo_interes.setId("tab_anticipo_interes");
		tab_anticipo_interes.setIdCompleto("tab_tabulador:tab_anticipo_interes");
		tab_anticipo_interes.setTabla("NRH_ANTICIPO_INTERES", "IDE_NRANI", 7);
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setCheck();
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setValorDefecto("true");
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setNombreVisual("PLAZO (Meses)");
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setValorDefecto(utilitario.getVariable("p_amortizacion_cada"));
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setNombreVisual("AMORTIZACION CADA (Dias)");
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes"));
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setVisible(false);
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes_efectiva"));
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setVisible(false);
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setValorDefecto("0");
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setVisible(false);
		tab_anticipo_interes.agregarRelacion(tab_amortizacion);
		tab_anticipo_interes.setTipoFormulario(true);
		tab_anticipo_interes.getGrid().setColumns(8);
		tab_anticipo_interes.setMostrarNumeroRegistros(false);
		tab_anticipo_interes.dibujar();

		PanelTabla pat_panel7=new PanelTabla();
		pat_panel7.setPanelTabla(tab_anticipo_interes);

		ItemMenu itm_tab_amort=new ItemMenu();
		itm_tab_amort.setIcon("ui-contact");
		itm_tab_amort.setMetodo("calcularTablaAmortizacion");
		itm_tab_amort.setValue("Generar Tabla");

		pat_panel7.getMenuTabla().getChildren().add(itm_tab_amort);


		tab_amortizacion.setId("tab_amortizacion");
		tab_amortizacion.setIdCompleto("tab_tabulador:tab_amortizacion");
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", 3);
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setCheck();
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setCheck();
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("IDE_NRRUB").setLectura(true);
		tab_amortizacion.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","");
		tab_amortizacion.getColumna("CAPITAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("INTERES_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("NRO_CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("FECHA_CANCELADO_NRAMO").setLectura(true);
		tab_amortizacion.setRecuperarLectura(true);
		tab_amortizacion.setColumnaSuma("CUOTA_NRAMO,PRINCIPAL_NRAMO");
		tab_amortizacion.setCampoOrden("FECHA_VENCIMIENTO_NRAMO ASC");
		tab_amortizacion.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_amortizacion);

		ItemMenu itm_ver_cancelacion=new ItemMenu();
		itm_ver_cancelacion.setIcon("ui-contact");
		itm_ver_cancelacion.setMetodo("verDatosPreCancelacion");
		itm_ver_cancelacion.setValue("Ver Datos Pre-Can");
		pat_panel3.getMenuTabla().getChildren().add(itm_ver_cancelacion);

		ItemMenu itm_ver_abonos=new ItemMenu();
		itm_ver_abonos.setIcon("ui-contact");
		itm_ver_abonos.setMetodo("verDatosAbonos");
		itm_ver_abonos.setValue("Ver Abonos");
		pat_panel3.getMenuTabla().getChildren().add(itm_ver_abonos);




		tex_valor_a_recibir.setId("tex_valor_a_recibir");
		tex_valor_a_recibir.setDisabled(true);
		tex_valor_a_recibir.setStyle("font-size: 14px;color: black;font-weight: bold");
		tex_total_ingresos.setId("tex_total_ingresos");
		tex_total_ingresos.setDisabled(true);
		tex_total_ingresos.setStyle("font-size: 14px;color: black;font-weight: bold");
		tex_total_egresos.setId("tex_total_egresos");
		tex_total_egresos.setDisabled(true);
		tex_total_egresos.setStyle("font-size: 14px;color: black;font-weight: bold");

		Grid gri_valores = new Grid();
		gri_valores.setColumns(4);
		gri_valores.getChildren().add(new Etiqueta("Total Valor Solicitado"));
		gri_valores.getChildren().add(tex_total_ingresos);
		gri_valores.getChildren().add(new Etiqueta("Total Valor Pagado"));
		gri_valores.getChildren().add(tex_total_egresos);
		gri_valores.getChildren().add(new Etiqueta("Total Valor Adeuda"));
		gri_valores.getChildren().add(tex_valor_a_recibir);
		

		

		//		Division div_amor=new Division();
		//		div_amor.dividir2(pat_panel7, pat_panel3, "12%", "H");

		Grid gri_amor=new Grid();
		gri_amor.setColumns(1);
		gri_amor.setWidth("100%");
		gri_amor.getChildren().add(pat_panel7);
		gri_amor.getChildren().add(pat_panel3);
		gri_amor.getChildren().add(gri_valores);

		// GARANTE (TABULADOR OPCION 3)		
		tab_garante.setId("tab_garante");
		tab_garante.setIdCompleto("tab_tabulador:tab_garante");
		tab_garante.setTabla("NRH_GARANTE", "IDE_NRGAR",4 );
		tab_garante.getColumna("ACTIVO_NRGAR").setCheck();
		tab_garante.getColumna("ACTIVO_NRGAR").setValorDefecto("true");
		tab_garante.getColumna("IDE_GEEDP").setCombo(tab_anticipo.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_garante.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_garante.getColumna("IDE_GEEDP").setLectura(true);
		tab_garante.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD","IDE_GTTDI","DETALLE_GTTDI","ACTIVO_GTTDI=true");		
		tab_garante.getColumna("GTH_IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD","IDE_GTTDI","DETALLE_GTTDI","ACTIVO_GTTDI=true");
		tab_garante.getColumna("IDE_NRTIG").setCombo("NRH_TIPO_GARANTE","IDE_NRTIG","DETALLE_NRTIG","");
		tab_garante.getColumna("IDE_NRTIG").setMetodoChange("cambiaTipoGarante");
		tab_garante.getColumna("VIVIENDA_NRGAR").setCheck();
		tab_garante.getColumna("VEHICULO_NRGAR").setCheck();
		tab_garante.setTipoFormulario(true);
		tab_garante.getGrid().setColumns(4);

		tab_garante.dibujar();

		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_garante);
		pat_panel4.setMensajeWarn("GARANTE");

		tab_telefono_garante.setId("tab_telefono_garante");
		tab_telefono_garante.setIdCompleto("tab_tabulador:tab_telefono_garante");
		tab_telefono_garante.setTabla("gth_telefono", "ide_gttel", 15);
		tab_telefono_garante.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
		tab_telefono_garante.getColumna("ACTIVO_GTTEL").setCheck();
		tab_telefono_garante.getColumna("ACTIVO_GTTEL").setValorDefecto("true");
		tab_telefono_garante.getColumna("IDE_GTEMP").setVisible(false);
		tab_telefono_garante.getColumna("IDE_GTCON").setVisible(false);
		tab_telefono_garante.getColumna("IDE_GTCON").setValorDefecto(null);
		tab_telefono_garante.getColumna("IDE_GTPEE").setVisible(false);
		tab_telefono_garante.getColumna("GTH_IDE_GTEMP").setVisible(false);
		tab_telefono_garante.getColumna("NOTIFICACION_GTTEL").setCheck();
		tab_telefono_garante.getColumna("NOTIFICACION_GTTEL").setVisible(false);
		tab_telefono_garante.getColumna("IDE_GTELE").setVisible(false);
		tab_telefono_garante.getColumna("IDE_NRGAR").setVisible(false);
		tab_telefono_garante.getColumna("IDE_GEBEN").setVisible(false);
		tab_telefono_garante.getColumna("IDE_GTNEE").setVisible(false);
		tab_telefono_garante.setCondicion("ide_nrgar=-1");
		tab_telefono_garante.dibujar();

		PanelTabla pat_panel15=new PanelTabla();
		pat_panel15.setPanelTabla(tab_telefono_garante);
		pat_panel15.setMensajeWarn("TELEFONO GARANTE");

		Grid gri_garante=new Grid();
		gri_garante.setWidth("100%");
		gri_garante.getChildren().add(pat_panel4);
		gri_garante.getChildren().add(pat_panel15);





		tab_tabulador.agregarTab("CAPACIDAD DE PAGO", div_cap_pago);
		tab_tabulador.agregarTab("TABLA DE AMORTIZACIÓN", gri_amor);
		tab_tabulador.agregarTab("GARANTE", gri_garante);


		//  para el visualizador de ingresos y egresos del empleado

		dia_roles_empleado.setId("dia_roles_empleado");
		dia_roles_empleado.setTitle("INGRESOS Y EGRESOS DEL EMPLEADO");
		dia_roles_empleado.setWidth("80%");
		dia_roles_empleado.getBot_aceptar().setMetodo("aceptarIngresosEgresosEmpleado");
		dia_roles_empleado.setDynamic(false);

		tab_ingresos_egresos_emp.setId("tab_ingresos_egresos_emp");
		tab_ingresos_egresos_emp.setSql(ser_nomina.getSqlIngresosEgresosEmpleado("-1"));
		tab_ingresos_egresos_emp.setCampoPrimaria("IDE_NRROL");
		tab_ingresos_egresos_emp.getColumna("DETALLE_NRTIT").setNombreVisual("TIPO ROL");
		tab_ingresos_egresos_emp.getColumna("DETALLE_GEANI").setNombreVisual("ANIO");
		tab_ingresos_egresos_emp.getColumna("DETALLE_GEMES").setNombreVisual("MES");
		tab_ingresos_egresos_emp.onSelectCheck("seleccionaRolEmpleado");
		tab_ingresos_egresos_emp.onUnselectCheck("quitaSeleccionRolEmpleado");
		tab_ingresos_egresos_emp.setNumeroTabla(5);
		tab_ingresos_egresos_emp.setTipoSeleccion(true);
		tab_ingresos_egresos_emp.dibujar();

		eti_prom_ing.setId("eti_prom_ing");
		eti_prom_ing.setValue("PROMEDIO INGRESOS:");
		eti_prom_ing.setStyle("font-size: 14px;color: red;font-weight: bold");
		eti_prom_egr.setId("eti_prom_egr");
		eti_prom_egr.setValue("PROMEDIO EGRESOS:");
		eti_prom_egr.setStyle("font-size: 14px;color: red;font-weight: bold");
		Grid gri_promedio_ing_egr=new Grid();
		gri_promedio_ing_egr.setColumns(1);
		gri_promedio_ing_egr.getChildren().add(eti_prom_ing);
		gri_promedio_ing_egr.getChildren().add(eti_prom_egr);

		Grid gri_ingresos_egresos_emp=new Grid();
		gri_ingresos_egresos_emp.getChildren().add(tab_ingresos_egresos_emp);
		gri_ingresos_egresos_emp.getChildren().add(gri_promedio_ing_egr);
		gri_ingresos_egresos_emp.setStyle("width:" + (dia_roles_empleado.getAnchoPanel() - 5) + "px; height:" + dia_roles_empleado.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_roles_empleado.setDialogo(gri_ingresos_egresos_emp);


		agregarComponente(dia_roles_empleado);

		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
		agregarComponente(div_division);

		// confirmacion para ingreso de anticipo por medio de memo
		con_datos_memo.setId("con_datos_memo");
		con_datos_memo.setTitle("CONFIRMACION DE ANTICIPOS ESPECIALES");
		con_datos_memo.setMessage("EL EMPLEADO NO CUMPLE CON LAS CONDICIONES DE ANTICIPOS<br></br>PERO DE TODAS MANERAS DESEA INSERTAR EL ANTICIPO ");
		agregarComponente(con_datos_memo);

		// dialogo ingreso de memo para ingreso de anticipos especiales

		aut_empleado_autoriza_memo.setId("aut_empleado_autoriza_memo");
		aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");


		eti_empleado.setValue("Empleado Autoriza: ");
		eti_num_memo.setValue("Numero Memo: ");
		eti_fecha_aut.setValue("Fecha Autorizacion: ");
		eti_raz_aut.setValue("Observacion: ");

		upl_autoriza_memo.setId("upl_autoriza_memo");
		upl_autoriza_memo.setUpload("archivos");
		agregarComponente(upl_autoriza_memo);



		Grid gri_memos=new Grid();
		gri_memos.setWidth("100%");
		gri_memos.setColumns(2);
		gri_memos.getChildren().add(eti_empleado);
		gri_memos.getChildren().add(aut_empleado_autoriza_memo);
		gri_memos.getChildren().add(eti_num_memo);
		gri_memos.getChildren().add(tex_num_memo);
		gri_memos.getChildren().add(eti_fecha_aut);
		gri_memos.getChildren().add(cal_fecha_autorizacion_memo);
		gri_memos.getChildren().add(eti_raz_aut);
		gri_memos.getChildren().add(art_razon_aut_memo);
		gri_memos.getChildren().add(eti_archivo_adjunto);
		gri_memos.getChildren().add(upl_autoriza_memo);

		dia_ingreso_memo.setId("dia_ingreso_memo");
		dia_ingreso_memo.setWidth("50%");
		dia_ingreso_memo.setHeight("40%");
		dia_ingreso_memo.setDialogo(gri_memos);
		dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarDatosMemo");
		dia_ingreso_memo.setDynamic(false);
		agregarComponente(dia_ingreso_memo);


		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		// PARA PRE CANCELACION DE ANTICPOS

		tab_precancelacion.setId("tab_precancelacion");
		tab_precancelacion.setTabla("NRH_PRECANCELACION", "IDE_NRPRE", 10);
		tab_precancelacion.getColumna("activo_nrpre").setCheck();
		tab_precancelacion.getColumna("activo_nrpre").setValorDefecto("true");
		tab_precancelacion.getColumna("activo_nrpre").setVisible(false);
		tab_precancelacion.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		
		tab_precancelacion.getColumna("FECHA_PRECANCELADO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		tab_precancelacion.getColumna("FECHA_DEPOSITO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		tab_precancelacion.getColumna("PATH_FOTO_NRPRE").setUpload("nueva_carpeta");		
		tab_precancelacion.getColumna("IDE_NRAMO").setVisible(false);
		tab_precancelacion.setCondicion("IDE_NRPRE=-1");
		tab_precancelacion.setTipoFormulario(false);
		tab_precancelacion.setRecuperarLectura(true);
		tab_precancelacion.setMostrarNumeroRegistros(false);
		tab_precancelacion.getGrid().setColumns(6);
		tab_precancelacion.dibujar();

		PanelTabla pat_precanc=new PanelTabla();
		pat_precanc.setPanelTabla(tab_precancelacion);

		eti_tot_precancelar.setId("eti_tot_precancelar");
		eti_tot_precancelar.setValue("Total Precancelar: 0.0");
		eti_tot_precancelar.setStyle("font-size:18px;color: black;font-weight: bold");

		eti_tot_adeuda.setId("eti_tot_adeuda");
		eti_tot_adeuda.setValue("Total Adeuda: 0.0");
		eti_tot_adeuda.setStyle("font-size:18px;color: black;font-weight: bold");


		tab_pagos_anticipos.setId("tab_pagos_anticipos");
		tab_pagos_anticipos.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT=-1) " +
				"AND IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_descuento_nomina")+") " +
				"and ACTIVO_NRAMO=FALSE " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
		tab_pagos_anticipos.setCampoPrimaria("IDE_NRAMO");
		tab_pagos_anticipos.setNumeroTabla(12);
		tab_pagos_anticipos.setTipoSeleccion(true);
		tab_pagos_anticipos.onSelectCheck("seleccionaCuotaPrecancelar");
		tab_pagos_anticipos.onUnselectCheck("deSeleccionaCuotaPrecancelar");
		tab_pagos_anticipos.getColumna("CUOTA_NRAMO").setEstilo("font-size: 14px;color: black;font-weight: bold");
		tab_pagos_anticipos.setColumnaSuma("CUOTA_NRAMO");
		tab_pagos_anticipos.dibujar();

		PanelTabla pat_pagos_ant=new PanelTabla();
		pat_pagos_ant.setPanelTabla(tab_pagos_anticipos);

		dia_precancelacion.setId("dia_precancelacion");
		dia_precancelacion.setWidth("70%");
		dia_precancelacion.setHeight("75%");

		Grid gri_pre_cancelar=new Grid();
		gri_pre_cancelar.getChildren().add(pat_precanc);
		gri_pre_cancelar.getChildren().add(eti_tot_adeuda);
		gri_pre_cancelar.getChildren().add(pat_pagos_ant);
		gri_pre_cancelar.getChildren().add(eti_tot_precancelar);
		gri_pre_cancelar.setStyle("width:" + (dia_precancelacion.getAnchoPanel() - 5) + "px;height:" + dia_precancelacion.getAltoPanel() + "px;overflow: auto;display: block;");

		dia_precancelacion.setDialogo(gri_pre_cancelar);
		dia_precancelacion.getBot_aceptar().setMetodo("aceptarPrecancelacion");
		dia_precancelacion.setDynamic(false);
		agregarComponente(dia_precancelacion);

		//   PARA VER DATOS PRE CANCELACION

		dia_datos_precancelacion.setId("dia_datos_precancelacion");
		dia_datos_precancelacion.setWidth("60%");
		dia_datos_precancelacion.setHeight("60%");


		tab_datos_precancelacion.setId("tab_datos_precancelacion");
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=TRUE " +
				"and amo.ide_nrani=-1 ");
		tab_datos_precancelacion.setCampoPrimaria("IDE_NRPRE");
		tab_datos_precancelacion.setNumeroTabla(13);
		tab_datos_precancelacion.getColumna("PATH_FOTO_NRPRE").setVisible(true);
		tab_datos_precancelacion.getColumna("PATH_FOTO_NRPRE").setUpload("nueva_carpeta");
		tab_datos_precancelacion.getColumna("IDE_NRPRE").setVisible(false);
		tab_datos_precancelacion.setColumnaSuma("CUOTA_NRAMO");
		tab_datos_precancelacion.setTipoFormulario(false);
		tab_datos_precancelacion.setLectura(true);
		tab_datos_precancelacion.setMostrarNumeroRegistros(false);
		tab_datos_precancelacion.dibujar();

		Grid gri_datos_pre=new Grid();
		gri_datos_pre.getChildren().add(tab_datos_precancelacion);
		gri_datos_pre.setStyle("width:" + (dia_datos_precancelacion.getAnchoPanel() - 5) + "px;height:" + dia_datos_precancelacion.getAltoPanel() + "px;overflow: auto;display: block;");

		dia_datos_precancelacion.setDialogo(gri_datos_pre);
		dia_datos_precancelacion.getBot_aceptar().setRendered(false);
		agregarComponente(dia_datos_precancelacion);


		// PARA VER DATOS DE ABONOS DE ANTICIPOS

		con_abonos_anticipo.setId("con_abonos_anticipo");
		con_abonos_anticipo.setConsulta("select IDE_NRANA, " +
				"SALDO_ANTERIOR_NRANA,VALOR_ABONO_NRANA,MONTO_PENDIENTE_NRANA,PLAZO_NRANA,FECHA_PAGO_NRANA,DETALLE_GEINS,DOC_DEPOSITO_NRANA,FECHA_DEPOSITO_NRANA "+
				"from NRH_ANTICIPO_ABONO ana " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=ana.IDE_GEINS " +
				"where IDE_NRANT=-1", "IDE_NRANA");
		con_abonos_anticipo.getTab_consulta_dialogo().setColumnaSuma("VALOR_ABONO_NRANA");
		con_abonos_anticipo.getBot_aceptar().setMetodo("aceptarVerAbonos"); 
		agregarComponente(con_abonos_anticipo);

		///////////////////////////////////////////////////////////////


		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
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
		dia_filtro_activo.setTitle("SELECCIONE EMPLEADO ACTIVO / INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		agregarComponente(dia_filtro_activo);

		// PARA ABONOS DE ANTICIPOS


		tab_anticipo_abonos.setId("tab_anticipo_abonos");
		tab_anticipo_abonos.setTabla("NRH_ANTICIPO_ABONO", "IDE_NRANA", 11);
		tab_anticipo_abonos.getColumna("ACTIVO_NRANA").setValorDefecto("true");
		tab_anticipo_abonos.getColumna("ACTIVO_NRANA").setVisible(false);
		tab_anticipo_abonos.getColumna("IDE_NRANT").setVisible(false);
		tab_anticipo_abonos.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		

		tab_anticipo_abonos.getColumna("SALDO_ANTERIOR_NRANA").setLectura(true);
		tab_anticipo_abonos.getColumna("SALDO_ANTERIOR_NRANA").setEtiqueta();
		tab_anticipo_abonos.getColumna("VALOR_ABONO_NRANA").setMetodoChange("cambiaValorAbono");
		tab_anticipo_abonos.getColumna("MONTO_PENDIENTE_NRANA").setLectura(true);
		tab_anticipo_abonos.getColumna("FECHA_ABONO_NRANA").setValorDefecto(utilitario.getFechaActual());
		tab_anticipo_abonos.getColumna("PATH_NRANA").setUpload("nueva_carpeta");
		tab_anticipo_abonos.getColumna("PATH_NRANA").setImagen("", "");

		tab_anticipo_abonos.getGrid().setColumns(2);
		tab_anticipo_abonos.setTipoFormulario(true);
		tab_anticipo_abonos.setMostrarNumeroRegistros(false);
		tab_anticipo_abonos.dibujar();

		PanelTabla pat_abonos=new PanelTabla();
		pat_abonos.setPanelTabla(tab_anticipo_abonos);

		dia_abonos.setId("dia_abonos");
		dia_abonos.setTitle("SELECCIONE EMPLEADO ACTIVO / INACTIVO");

		Grid gri_abonos=new Grid();
		gri_abonos.setWidth("100%");
		gri_abonos.getChildren().add(pat_abonos);
		gri_abonos.setStyle("width:" + (dia_abonos.getAnchoPanel() - 5) + "px;height:" + dia_abonos.getAltoPanel() + "px;overflow: auto;display: block;");
		dia_abonos.getBot_aceptar().setMetodo("aceptarAbonos");
		dia_abonos.setDialogo(gri_abonos);
		dia_abonos.setDynamic(false);

		agregarComponente(dia_abonos);

		List lista1 = new ArrayList();
		Object fila11[] = {
				"0", "CREDITOS CANCELADOS"
		};
		Object fila22[] = {
				"1", "CREDITOS NO CANCELADOS"
		};
		lista1.add(fila11);
		lista1.add(fila22);

		lis_activo_anticipo.setListaSeleccion(lista1);
		lis_activo_anticipo.setVertical();
		dia_filtro_activo_anticipo.setId("dia_filtro_activo_anticipo");
		dia_filtro_activo_anticipo.setTitle("ESCOJA ANTICIPO CANCELADO/NO CANCELADO");
		dia_filtro_activo_anticipo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo_anticipo.setDialogo(lis_activo_anticipo);
		dia_filtro_activo_anticipo.setDynamic(false);
		agregarComponente(dia_filtro_activo_anticipo);

		sel_cal.setId("sel_cal");
		sel_cal.setMultiple(true);
		sel_cal.setTitle("ESCOJA RANGO DE FECHA PARA ANTICIPOS");
		agregarComponente(sel_cal);



		//UIViewRoot newView = FacesContext.getCurrentInstance().getApplication().getViewHandler().createView(FacesContext.getCurrentInstance(), "/index.jsf"); FacesContext.getCurrentInstance().setViewRoot(newView);
		//utilitario.addUpdate("@all");
	}


	public void abrirDialogoDatosGarante(){
//System.out.println("variable ide_geedp_activo.... "+ide_geedp_activo);
		aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"INNER JOIN GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EPAR.IDE_GTTCO AND TCO.GARANTE_GTTCO=true "+
				"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
		String sql_aut_empleado_autoriza_memo="";
		sql_aut_empleado_autoriza_memo=aut_empleado_autoriza_memo.getAutocomplete();
		System.out.println("sql_aut_empleado_autoriza_memo  ... "+ide_geedp_activo);
		
		
		System.out.println("aut_empleado_autoriza_memo  ... "+aut_empleado_autoriza_memo);
		eti_empleado.setValue("Empleado Garante: ");
		aut_empleado_autoriza_memo.setValue(null);

		eti_fecha_aut.setRendered(false);
		cal_fecha_autorizacion_memo.setRendered(false);
		eti_num_memo.setRendered(false);
		eti_raz_aut.setRendered(false);
		tex_num_memo.setRendered(false);
		art_razon_aut_memo.setRendered(false);
		eti_archivo_adjunto.setRendered(false);
		upl_autoriza_memo.setRendered(false);

		dia_ingreso_memo.setTitle("EMPLEADO GARANTE ANTICIPO");
		dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarGarante");
		dia_ingreso_memo.dibujar();
	}

	public void inicializarColumnasTablaGarante(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){

			tab_garante.getColumna("IDE_GEEDP").setLectura(true);
			tab_garante.getColumna("IDE_NRTIG").setLectura(false);
			tab_garante.getColumna("APELLIDO_PATERNOG_NRGAR").setLectura(false);
			tab_garante.getColumna("APELLIDO_MATERNOG_NRGAR").setLectura(false);
			tab_garante.getColumna("PRIMER_NOMBREG_NRGAR").setLectura(false);
			tab_garante.getColumna("SEGUNDO_NOMBREG_NRGAR").setLectura(false);
			tab_garante.getColumna("IDE_GTTDI").setLectura(false);
			tab_garante.getColumna("DOCUMENTO_IDENTIDADG_NRGAR").setLectura(false);
			tab_garante.getColumna("CARGO_NRGAR").setLectura(false);
			tab_garante.getColumna("RMU_NRGAR").setLectura(false);
			tab_garante.getColumna("DEPARTAMENTO_NRGAR").setLectura(false);
			tab_garante.getColumna("GTH_IDE_GTTDI").setLectura(false);
			tab_garante.getColumna("DOCUMENTO_IDENTIDADC_NRGAR").setLectura(false);
			tab_garante.getColumna("PRIMER_NOMBREC_NRGAR").setLectura(false);
			tab_garante.getColumna("SEGUNDO_NOMBREC_NRGAR").setLectura(false);
			tab_garante.getColumna("APELLIDO_PATERNOC_NRGAR").setLectura(false);
			tab_garante.getColumna("APELLIDO_MATERNOC_NRGAR").setLectura(false);
			tab_garante.getColumna("VIVIENDA_NRGAR").setLectura(false);
			tab_garante.getColumna("VEHICULO_NRGAR").setLectura(false);
			tab_garante.getColumna("MONTO_VEHICULO_NRGAR").setLectura(false);
			tab_garante.getColumna("MONTO_VIVIENDA_NRGAR").setLectura(false);
			tab_garante.getColumna("ACTIVO_NRGAR").setLectura(false);

		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){

			tab_garante.getColumna("IDE_GEEDP").setLectura(true);
			tab_garante.getColumna("IDE_NRTIG").setLectura(true);
			tab_garante.getColumna("APELLIDO_PATERNOG_NRGAR").setLectura(true);
			tab_garante.getColumna("APELLIDO_MATERNOG_NRGAR").setLectura(true);
			tab_garante.getColumna("PRIMER_NOMBREG_NRGAR").setLectura(true);
			tab_garante.getColumna("SEGUNDO_NOMBREG_NRGAR").setLectura(true);
			tab_garante.getColumna("IDE_GTTDI").setLectura(true);
			tab_garante.getColumna("DOCUMENTO_IDENTIDADG_NRGAR").setLectura(true);
			tab_garante.getColumna("CARGO_NRGAR").setLectura(true);
			tab_garante.getColumna("RMU_NRGAR").setLectura(true);
			tab_garante.getColumna("DEPARTAMENTO_NRGAR").setLectura(true);
			tab_garante.getColumna("GTH_IDE_GTTDI").setLectura(true);
			tab_garante.getColumna("DOCUMENTO_IDENTIDADC_NRGAR").setLectura(true);
			tab_garante.getColumna("PRIMER_NOMBREC_NRGAR").setLectura(true);
			tab_garante.getColumna("SEGUNDO_NOMBREC_NRGAR").setLectura(true);
			tab_garante.getColumna("APELLIDO_PATERNOC_NRGAR").setLectura(true);
			tab_garante.getColumna("APELLIDO_MATERNOC_NRGAR").setLectura(true);
			tab_garante.getColumna("VIVIENDA_NRGAR").setLectura(true);
			tab_garante.getColumna("VEHICULO_NRGAR").setLectura(true);
			tab_garante.getColumna("MONTO_VEHICULO_NRGAR").setLectura(true);
			tab_garante.getColumna("MONTO_VIVIENDA_NRGAR").setLectura(true);
			tab_garante.getColumna("ACTIVO_NRGAR").setLectura(true);
		}
	}


	public void aceptarGarante(){
		if (aut_empleado_autoriza_memo.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar el empleado garante del anticipo");
			return;
		}

		if (ser_anticipo.getNumVecesGaranteAnticipo(aut_empleado_autoriza_memo.getValor())>1){
			utilitario.agregarMensajeInfo("No se puede continuar","El garante seleccionado ya no puede ser garante de otro anticipo");
			return;
		}

		TablaGenerica tab_datos=utilitario.consultar("select " +
				"APELLIDO_PATERNO_GTEMP, " +
				"APELLIDO_MATERNO_GTEMP, " +
				"PRIMER_NOMBRE_GTEMP, " +
				"SEGUNDO_NOMBRE_GTEMP, " +
				"EMP.IDE_GTTDI, " +
				"DOCUMENTO_IDENTIDAD_GTEMP, " +
				"detalle_gecaf, " +
				"RMU_GEEDP, " +
				"dep.detalle_gedep, " +
				"cny.ide_gttdi as ide_gttdi2, " +
				"DOCUMENTO_IDENTIDAD_GTCON, " +
				"PRIMER_NOMBRE_GTCON, " +
				"SEGUNDO_NOMBRE_GTCON, " +
				"APELLIDO_PATERNO_GTCON, " +
				"APELLIDO_MATERNO_GTCON " +
				"from GTH_EMPLEADO emp " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=emp.ide_gtemp " +
				"left join GEN_CARGO_FUNCIONAL caf on caf.ide_gecaf=edp.ide_gecaf " +
				"left join GEN_DEPARTAMENTO dep on dep.ide_gedep=edp.ide_gedep " +
				"left join GTH_CONYUGE cny on cny.ide_gtemp=emp.ide_gtemp " +
				
				"where edp.IDE_GEEDP="+aut_empleado_autoriza_memo.getValor());
		
		double sueldoSolicitanteAnticipo=0.0,sueldoGaranteAnticipo=0.0;
		int diasTrabajados=0;
		//sueldoGaranteAnticipo=ser_nomina.getSueldoUltimoRol(aut_empleado_autoriza_memo.getValor());
		//sueldoSolicitanteAnticipo=ser_nomina.getSueldoUltimoRol(ide_geedp_activo);
		
		diasTrabajados=ser_nomina.diasTrabajadosEmpleado(""+aut_empleado_autoriza_memo.getValor());	
		System.out.println("dias trabajados"+diasTrabajados);
		/*if (sueldoGaranteAnticipo==0.00) {
			utilitario.agregarMensajeInfo("Garante no cumple con las condiciones", "Seleccione un nuevo garante");
			return;
		}
		
		if (sueldoSolicitanteAnticipo==0.00) {
			utilitario.agregarMensajeInfo("Solicitante no contiene cumple las condiciones", "Seleccione un nuevo garante");
			return;
		}
		
		
		if (sueldoGaranteAnticipo>=sueldoSolicitanteAnticipo) {
			
			if (diasTrabajados>=Integer.parseInt(utilitario.getVariable("p_modulo_anticipos_dias_trabajados"))) {
				
			}else {
				System.out.println("sin diasTrabajados>90");
			}
			
		}else {
			utilitario.agregarMensajeInfo("Sueldo de garante es menor al del solicitante", "Seleccione un nuevo garante");
			return;
		}
		*/
		
		TablaGenerica tab_tel=utilitario.consultar("SELECT * FROM gth_telefono WHERE activo_gttel=true and ide_gtemp in(select ide_gtemp from gen_empleados_departamento_par " +
				"where ide_geedp="+aut_empleado_autoriza_memo.getValor()+") " +
				"order by ide_gttel asc");
		if (tab_garante.isFilaInsertada()){	
			tab_telefono_garante.limpiar();
			tab_garante.limpiar();
			
		}else{
			
			utilitario.getConexion().ejecutarSql("delete from gth_telefono where ide_nrgar="+tab_garante.getValorSeleccionado());
			
			tab_garante.eliminar();
		}
		tab_garante.insertar();
		tab_garante.setValor("IDE_GEEDP", aut_empleado_autoriza_memo.getValor());
		tab_garante.setValor("APELLIDO_PATERNOG_NRGAR", tab_datos.getValor("APELLIDO_PATERNO_GTEMP"));
		tab_garante.setValor("APELLIDO_MATERNOG_NRGAR", tab_datos.getValor("APELLIDO_MATERNO_GTEMP"));
		tab_garante.setValor("PRIMER_NOMBREG_NRGAR", tab_datos.getValor("PRIMER_NOMBRE_GTEMP"));
		tab_garante.setValor("SEGUNDO_NOMBREG_NRGAR", tab_datos.getValor("SEGUNDO_NOMBRE_GTEMP"));
		tab_garante.setValor("IDE_GTTDI", tab_datos.getValor("IDE_GTTDI"));
		tab_garante.setValor("DOCUMENTO_IDENTIDADG_NRGAR", tab_datos.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));
		tab_garante.setValor("CARGO_NRGAR", tab_datos.getValor("detalle_gecaf"));
		tab_garante.setValor("RMU_NRGAR", tab_datos.getValor("RMU_GEEDP"));
		tab_garante.setValor("DEPARTAMENTO_NRGAR", tab_datos.getValor("detalle_gedep"));
		tab_garante.setValor("GTH_IDE_GTTDI", tab_datos.getValor("ide_gttdi2"));
		tab_garante.setValor("DOCUMENTO_IDENTIDADC_NRGAR", tab_datos.getValor("DOCUMENTO_IDENTIDAD_GTCON"));
		tab_garante.setValor("PRIMER_NOMBREC_NRGAR", tab_datos.getValor("PRIMER_NOMBRE_GTCON"));
		tab_garante.setValor("SEGUNDO_NOMBREC_NRGAR",tab_datos.getValor("SEGUNDO_NOMBRE_GTCON"));
		tab_garante.setValor("APELLIDO_PATERNOC_NRGAR", tab_datos.getValor("APELLIDO_PATERNO_GTCON"));
		tab_garante.setValor("APELLIDO_MATERNOC_NRGAR", tab_datos.getValor("APELLIDO_MATERNO_GTCON"));
		tab_garante.setValor("IDE_NRTIG", utilitario.getVariable("p_nrh_tipo_garante_empl_biess"));


		tab_telefono_garante.setCondicion("ide_nrgar="+tab_garante.getValorSeleccionado());
		tab_telefono_garante.ejecutarSql();

		for (int i = 0; i < tab_tel.getTotalFilas(); i++) {
			tab_telefono_garante.insertar();
			tab_telefono_garante.setValor("ide_gttit", tab_tel.getValor(i, "ide_gttit"));
			tab_telefono_garante.setValor("numero_telefono_gttel", tab_tel.getValor(i, "numero_telefono_gttel"));
		}
		dia_ingreso_memo.cerrar();

	}
	public void agregarGarante(){


		if (tab_anticipo.getValor("APROBADO_NRANT") !=null 
				&& !tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
				&& (tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("true")
						|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("true"))){
			utilitario.agregarMensajeInfo("No se puede agregar un garante", "El anticipo ya se encuentra aprobado");
			return;
		}

		abrirDialogoDatosGarante();	

	}


	public void cambiaTipoGarante(AjaxBehaviorEvent evt){
		tab_garante.modificar(evt);

		if (!tab_garante.getValor("IDE_NRTIG").equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_garante_empl_biess"))){
			tab_garante.setValor("IDE_GEEDP", null);
			utilitario.addUpdateTabla(tab_garante, "IDE_GEEDP", "tab_tabulador:tab_garante");
		}

	}

	public void aceptarVerAbonos(){
		con_abonos_anticipo.cerrar();
	}

	public void verDatosAbonos(){
		con_abonos_anticipo.getTab_consulta_dialogo().setSql("select IDE_NRANA, " +
				"SALDO_ANTERIOR_NRANA,VALOR_ABONO_NRANA,MONTO_PENDIENTE_NRANA,PLAZO_NRANA,FECHA_PAGO_NRANA,DETALLE_GEINS,DOC_DEPOSITO_NRANA,FECHA_DEPOSITO_NRANA " +
				"from NRH_ANTICIPO_ABONO ana " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=ana.IDE_GEINS " +
				"where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
		con_abonos_anticipo.getTab_consulta_dialogo().ejecutarSql();
		if (con_abonos_anticipo.getTab_consulta_dialogo().getTotalFilas()>0){
			con_abonos_anticipo.dibujar();
		}else{
			utilitario.agregarMensajeInfo("El anticipo seleccionado", "no tiene abonos realizados");
		}
	}
	public void verDatosPreCancelacion(){
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=true " +
				"and amo.ide_nrani="+tab_amortizacion.getValor("IDE_NRANI"));
		tab_datos_precancelacion.ejecutarSql();
		if (tab_datos_precancelacion.getTotalFilas()>0){
			dia_datos_precancelacion.setTitle("DATOS PRECANCELACION ANTICIPO");
			dia_datos_precancelacion.dibujar();
		}else{
			utilitario.agregarMensajeInfo("El anticipo seleccionado", "no tiene cuota pre canceladas");
		}
	}


	public void calcularTotalPrecancelar(){
		double dou_tot_precancelar=0;
		try {

			for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) {
				Object fila[]=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i).getCampos();
				dou_tot_precancelar=Double.parseDouble(fila[5]+"")+dou_tot_precancelar; 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("tot precancelar "+dou_tot_precancelar);
		eti_tot_precancelar.setValue("Total Precancelar: "+utilitario.getFormatoNumero(dou_tot_precancelar));
		utilitario.addUpdate("eti_tot_precancelar");
	}
	public void seleccionaCuotaPrecancelar(SelectEvent evt){
		tab_pagos_anticipos.seleccionarFila(evt);
		calcularTotalPrecancelar();
	}
	public void deSeleccionaCuotaPrecancelar(UnselectEvent evt){
		calcularTotalPrecancelar();
	}


	public void cambiaValorAbono(AjaxBehaviorEvent evt){
		tab_anticipo_abonos.modificar(evt);
		try {

			double dou_tot_rub_det_pag=ser_anticipo.getRubrosDetallePago(tab_capacidad.getValorSeleccionado()).getSumaColumna("VALOR_DESCUENTO_NRRDP");

			if (dou_tot_rub_det_pag>0){
				if ((Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA")) - dou_tot_rub_det_pag)
						>= Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA"))){
					double monto_pendiente=Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA"))-Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA"));
					tab_anticipo_abonos.setValor("MONTO_PENDIENTE_NRANA", monto_pendiente+"");
					utilitario.addUpdateTabla(tab_anticipo_abonos, "MONTO_PENDIENTE_NRANA","");
				}else{
					utilitario.agregarMensajeInfo("Valor de abono erroneo","Existe valor rubro detalle pago "+utilitario.getFormatoNumero(dou_tot_rub_det_pag)+" por lo tanto el valor de abono no puede ser mayor que el saldo por pagar "+utilitario.getFormatoNumero((Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA")) - dou_tot_rub_det_pag)));
					tab_anticipo_abonos.setValor("VALOR_ABONO_NRANA", utilitario.getFormatoNumero((Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA")) - dou_tot_rub_det_pag)));
					utilitario.addUpdateTabla(tab_anticipo_abonos, "VALOR_ABONO_NRANA","");
				}
			}else{
				if (Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA"))>=Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA"))){
					double monto_pendiente=Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA"))-Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA"));
					tab_anticipo_abonos.setValor("MONTO_PENDIENTE_NRANA", monto_pendiente+"");
					utilitario.addUpdateTabla(tab_anticipo_abonos, "MONTO_PENDIENTE_NRANA","");
				}else{
					utilitario.agregarMensajeInfo("Valor de abono erroneo","el valor de abono no puede ser mayor que el saldo por pagar");
					tab_anticipo_abonos.setValor("VALOR_ABONO_NRANA", "0");
					utilitario.addUpdateTabla(tab_anticipo_abonos, "VALOR_ABONO_NRANA","");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean validarDatosAbonoAnticipo(){
		if (tab_anticipo_abonos.getValor("IDE_GEINS")==null || tab_anticipo_abonos.getValor("IDE_GEINS").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No ha seleccionado una Entidad Financiera");
			return false;
		}

		if (tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA")==null || tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No existe saldo por pagar");
			return false;
		}
		if (tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA")==null || tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No existe valor de abono");
			return false;
		}		
		if (Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA"))==0){
			utilitario.agregarMensajeInfo("No se puede continuar","El valor de abono no puede ser cero");
			return false;
		}

		if (Double.parseDouble(tab_anticipo_abonos.getValor("VALOR_ABONO_NRANA")) > 
		Double.parseDouble(tab_anticipo_abonos.getValor("SALDO_ANTERIOR_NRANA"))){
			utilitario.agregarMensajeInfo("No se puede continuar","El valor de abono no puede ser mayor que el saldo pendiente");
			return false;
		}

		if (tab_anticipo_abonos.getValor("MONTO_PENDIENTE_NRANA")==null || tab_anticipo_abonos.getValor("MONTO_PENDIENTE_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No existe valor de abono");
			return false;
		}		
		if (tab_anticipo_abonos.getValor("PLAZO_NRANA")==null || tab_anticipo_abonos.getValor("PLAZO_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No existe plazo");
			return false;
		}
		if (pckUtilidades.CConversion.CInt(tab_anticipo_abonos.getValor("PLAZO_NRANA"))==0){
			utilitario.agregarMensajeInfo("Plazo erroneo","El plazo no puede ser cero ");
			return false;
		}		
		if (pckUtilidades.CConversion.CInt(tab_anticipo_abonos.getValor("PLAZO_NRANA"))>int_num_max_cuotas_abonos){
			utilitario.agregarMensajeInfo("Plazo erroneo","El plazo maximo es "+int_num_max_cuotas_abonos);
			return false;
		}		
		if (tab_anticipo_abonos.getValor("FECHA_PAGO_NRANA")==null || tab_anticipo_abonos.getValor("FECHA_PAGO_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No existe fecha de pago");
			return false;
		}


		if (str_fecha_ultimo_pago!=null && !str_fecha_ultimo_pago.isEmpty()){
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_anticipo_abonos.getValor("FECHA_PAGO_NRANA")), utilitario.getFecha(str_fecha_ultimo_pago))){
				utilitario.agregarMensajeInfo("Fecha de calculo erroneo","La fecha de calculo no puede ser menor que la ultima fecha de pago ( "+str_fecha_ultimo_pago+" )");
				return false;
			}
		}


		if (tab_anticipo_abonos.getValor("FECHA_DEPOSITO_NRANA")==null || tab_anticipo_abonos.getValor("FECHA_DEPOSITO_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No ha seleccionado la fecha de deposito");
			return false;
		}
		if (tab_anticipo_abonos.getValor("DOC_DEPOSITO_NRANA")==null || tab_anticipo_abonos.getValor("DOC_DEPOSITO_NRANA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar","No ha ingresado el numero de documento de deposito");
			return false;
		}


		//		if (utilitario.isFechaMenor(utilitario.getFecha(tab_anticipo_abonos.getValor("FECHA_PAGO_NRANA")),utilitario.getFecha(utilitario.getFechaActual()))){
		//			utilitario.agregarMensajeInfo("No se puede continuar","La fecha de pago no puede ser menor que ");
		//			return false;
		//		}		
		return true;
	}
	public void aceptarAbonos(){
		if (validarDatosAbonoAnticipo()){
			tab_anticipo_abonos.guardar();
			TablaGenerica tab_rubros_detalle_pago=ser_anticipo.getRubrosDetallePago(tab_capacidad.getValorSeleccionado());
			double dou_tot_rubros_pagos=0;

			if (tab_rubros_detalle_pago.getTotalFilas()>0){
				dou_tot_rubros_pagos=ser_anticipo.getMontoTotalRubrosPago(tab_anticipo.getValorSeleccionado());
			}
			String ide_nrcap="";
			if (tab_capacidad.getTotalFilas()>0){
				ide_nrcap=tab_capacidad.getValorSeleccionado();
			}
			if (ser_anticipo.generarAbonoAnticipo(
					tab_anticipo.getValorSeleccionado(),
					Double.parseDouble(tab_anticipo_abonos.getValor("MONTO_PENDIENTE_NRANA")), 

					//					(Double.parseDouble(tab_anticipo_abonos.getValor("MONTO_PENDIENTE_NRANA"))-dou_tot_rubros_pagos), 
					0, 
					pckUtilidades.CConversion.CInt(tab_anticipo_abonos.getValor("PLAZO_NRANA")), 
					30, 
					0, 
					tab_anticipo_abonos.getValor("FECHA_PAGO_NRANA"),
					ide_nrcap)){
				tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());

				utilitario.addUpdate("tab_tabulador:tab_amortizacion");
				dia_abonos.cerrar();
			}
		}
	}

	int int_num_max_cuotas_abonos=0;
	String str_fecha_ultimo_pago="";
	public void abonarAnticipo(){

		if (tab_anticipo.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede realizar el abono","No existe solicitud de anticipo");
			return;
		}
		if (tab_anticipo.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede realizar el abono", "Primero debe guardar el registro que esta trabajando");
			return;
		}
		if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
				&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede realizar el abono", "El anticipo se encuentra anulado ");
			return;
		}
		if (tab_anticipo.getValor("APROBADO_NRANT") ==null 
				|| tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede realizar el abono", "El anticipo no se encuentra aprobado");
			return;
		}

		if (tab_amortizacion.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede abonar", "No existe pagos por cancelar");
			return;
		}

		double tot_pagar=0;
		String total_pagar=ser_anticipo.getTotalPagarAnticipo(tab_anticipo.getValorSeleccionado());
		try {
			tot_pagar=Double.parseDouble(total_pagar);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (total_pagar==null || total_pagar.isEmpty() || Double.parseDouble(total_pagar)==0){
			utilitario.agregarMensajeInfo("No se puede abonar", "No existe pagos por cancelar");
			return;
		}

		//		try {
		//			tot_pagar=Double.parseDouble(total_pagar)-ser_anticipo.getRubrosDetallePago(tab_capacidad.getValorSeleccionado()).getSumaColumna("VALOR_DESCUENTO_NRRDP");
		//		} catch (Exception e) {
		//			// TODO: handle exception
		//		}



		str_fecha_ultimo_pago="";
		TablaGenerica tab_cuotas_pagadas=ser_anticipo.getCuotasPagadas(tab_anticipo.getValorSeleccionado());
		if (tab_cuotas_pagadas.getTotalFilas()>0){
			try {
				str_fecha_ultimo_pago=tab_cuotas_pagadas.getValor(tab_cuotas_pagadas.getTotalFilas()-1,"FECHA_VENCIMIENTO_NRAMO");	
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			try {
				str_fecha_ultimo_pago=ser_anticipo.getCuotasPorPagar(tab_anticipo.getValorSeleccionado()).getValor(0,"FECHA_VENCIMIENTO_NRAMO");	
			} catch (Exception e) {
				// TODO: handle exception
			}

		}


		dia_abonos.setTitle("DATOS PARA ABONOS DE ANTICIPOS");
		tab_anticipo_abonos.insertar();
		tab_anticipo_abonos.setValor("IDE_NRANT",tab_anticipo.getValorSeleccionado());
		tab_anticipo_abonos.setValor("SALDO_ANTERIOR_NRANA",utilitario.getFormatoNumero(tot_pagar));
		int int_num_cuotas_pagar=ser_anticipo.getAmortizacion(tab_anticipo.getValorSeleccionado(), false).getTotalFilas();
		int int_num_det_rub_pagos=ser_anticipo.getDetalleRubrosPago(tab_anticipo.getValorSeleccionado()).getTotalFilas();
		int_num_max_cuotas_abonos=int_num_cuotas_pagar-int_num_det_rub_pagos;
		tab_anticipo_abonos.setValor("PLAZO_NRANA",int_num_max_cuotas_abonos+"");
		tab_anticipo_abonos.setValor("FECHA_PAGO_NRANA",str_fecha_ultimo_pago+"");
		dia_abonos.dibujar();

	}

	public boolean validarPagosSeleccionadosPrecancelacion(){
		Fila fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(0);
		String str_fecha_venc_sel_ini=utilitario.getFormatoFecha(utilitario.getFecha(fila.getCampos()[2]+""));
		TablaGenerica tab_aux=utilitario.consultar("SELECT * FROM NRH_AMORTIZACION " +
				"where IDE_NRANI="+tab_anticipo_interes.getValorSeleccionado()+" " +
				"and ACTIVO_NRAMO = false  " +
				"order by FECHA_VENCIMIENTO_NRAMO ASC");
		int int_indice_tab_aux=0;
		for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) {
			fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i);
			String str_fecha_ven_seleccionada=utilitario.getFormatoFecha(utilitario.getFecha(fila.getCampos()[2]+""));
			String str_fecha_ven_tab_aux=tab_aux.getValor(int_indice_tab_aux, "FECHA_VENCIMIENTO_NRAMO");
			str_fecha_ven_tab_aux=utilitario.getFormatoFecha(utilitario.getFecha(str_fecha_ven_tab_aux));
			int_indice_tab_aux=int_indice_tab_aux+1;
			if (!str_fecha_ven_tab_aux.equalsIgnoreCase(str_fecha_ven_seleccionada)){
				utilitario.agregarMensajeError("No se puede precancelar", "El orden seleccionado es incorrecto no se puede saltar un pago");
				return false;
			}
		}
		if (tab_precancelacion.getValor("IDE_GEINS")==null || tab_precancelacion.getValor("IDE_GEINS").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar una Entidad Bancaria");
			return false;
		}

		if (tab_precancelacion.getValor("FECHA_PRECANCELADO_NRPRE")==null || tab_precancelacion.getValor("FECHA_PRECANCELADO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar la fecha de pre cancelacion");
			return false;
		}

		if (tab_precancelacion.getValor("DOC_DEPOSITO_NRPRE")==null || tab_precancelacion.getValor("DOC_DEPOSITO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe ingresar el Numero de Deposito");
			return false;
		}

		if (tab_precancelacion.getValor("FECHA_DEPOSITO_NRPRE")==null || tab_precancelacion.getValor("FECHA_DEPOSITO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar la fecha de deposito");
			return false;
		}

		return true;
	}
	public void aceptarPrecancelacion(){
		if (tab_pagos_anticipos.getListaFilasSeleccionadas().size()>0){
			if (validarPagosSeleccionadosPrecancelacion()){
				Fila fila;

				String str_ide_geins=tab_precancelacion.getValor("IDE_GEINS");
				String str_fecha_prec=tab_precancelacion.getValor("FECHA_PRECANCELADO_NRPRE");
				String str_doc_deposito=tab_precancelacion.getValor("DOC_DEPOSITO_NRPRE");
				String str_fecha_deposito=tab_precancelacion.getValor("FECHA_DEPOSITO_NRPRE");
				String str_path_foto=tab_precancelacion.getValor("PATH_FOTO_NRPRE");
				String str_observaciones=tab_precancelacion.getValor("OBSERVACIONES_NRPRE");

				for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) {
					fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i);
					utilitario.getConexion().agregarSql("update NRH_AMORTIZACION set ACTIVO_NRAMO=TRUE,PRE_CANCELADO_NRAMO=true ,FECHA_CANCELADO_NRAMO=TO_DATE('"+utilitario.getFechaActual()+"', 'yy-mm-dd') " +
							"where IDE_NRAMO ="+fila.getRowKey());
					if (i>0){
						tab_precancelacion.insertar();
						tab_precancelacion.setValor("IDE_NRAMO", fila.getRowKey());
						tab_precancelacion.setValor("IDE_GEINS", str_ide_geins);
						tab_precancelacion.setValor("FECHA_PRECANCELADO_NRPRE", str_fecha_prec);
						tab_precancelacion.setValor("DOC_DEPOSITO_NRPRE", str_doc_deposito);
						tab_precancelacion.setValor("FECHA_DEPOSITO_NRPRE", str_fecha_deposito);
						tab_precancelacion.setValor("PATH_FOTO_NRPRE", str_path_foto);
						tab_precancelacion.setValor("OBSERVACIONES_NRPRE", str_observaciones);
					}else{
						tab_precancelacion.setValor("IDE_NRAMO", fila.getRowKey());					
					}
				}
				tab_precancelacion.guardar();
				guardarPantalla();
				dia_precancelacion.cerrar();
				utilitario.addUpdate("tab_tabulador:tab_amortizacion");
				tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());

				actualizarEstadoActivoAnticipo();

			}
		}else{
			utilitario.agregarMensajeInfo("No ha seleccionado ningun anticipo para precancelar", "");
		}
	}




	public void anularAnticipo(){
		if (con_guardar.isVisible()){
			con_guardar.cerrar();
			utilitario.getConexion().agregarSql("update NRH_ANTICIPO set ACTIVO_NRANT=false,ANULADO_NRANT=true where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
			utilitario.getConexion().guardarPantalla();

			String ide_nrant_anterior=tab_anticipo.getValorSeleccionado();
			tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
			tab_anticipo.ejecutarSql();
			tab_anticipo.setFilaActual(ide_nrant_anterior);

			cargarTablasPantalla(); 
			cargarTotales();
		}else{
			if (aut_empleado.getValor()==null){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo", "No existen anticipos por anular en pantalla");
				return;
			}
			if (tab_anticipo.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo", "Debe seleccionar un empleado en el autocompletar");
				return;
			}
			if (tab_anticipo.isFilaInsertada()){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo", "Primero debe guardar el registro que esta trabajando");
				return;
			}
			if (tab_anticipo.getValor("ANULADO_NRANT") != null 
					&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty() 
					&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo","El anticipo ya se encuentra anulado ");
				return;
			}

			if (tab_anticipo.getValor("ACTIVO_NRANT") == null 
					|| tab_anticipo.getValor("ACTIVO_NRANT").isEmpty() 
					|| tab_anticipo.getValor("ACTIVO_NRANT").equalsIgnoreCase("false")){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo","El anticipo ya se encuentra cancelado");
				return;
			}

			if (utilitario.consultar("select * from NRH_AMORTIZACION where IDE_NRANI in ( " +
					"select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
					"and ACTIVO_NRAMO=TRUE").getTotalFilas()>0){
				utilitario.agregarMensajeInfo("No se puede anular el anticipo", "El anticipo seleccionado ya tiene cuotas canceladas");
				return;
			}


			con_guardar.setTitle("CONFIRMACION ANULACION DE ANTICIPOS");
			con_guardar.setMessage("ESTA SEGURO DE ANULAR EL ANTICIPO");
			con_guardar.getBot_aceptar().setMetodo("anularAnticipo");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}
	}

	public void actualizarEstadoActivoAnticipo(){

		if (tab_anticipo.getValor("ANULADO_NRANT")==null || tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("false")
				|| tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("0")){

			TablaGenerica tab_cuotas=utilitario.consultar("select * from NRH_AMORTIZACION where IDE_NRANI in ( " +
					"select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+")");
			if (tab_cuotas.getTotalFilas()>0){
				TablaGenerica tab_cuotas_pagadas=utilitario.consultar("select * from NRH_AMORTIZACION where IDE_NRANI in ( " +
						"select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
						"and ACTIVO_NRAMO=TRUE");
				if (tab_cuotas.getTotalFilas()==tab_cuotas_pagadas.getTotalFilas()){
					if (tab_anticipo.getValor("ACTIVO_NRANT")!=null
							&& !tab_anticipo.getValor("ACTIVO_NRANT").isEmpty()
							&& (tab_anticipo.getValor("ACTIVO_NRANT").equalsIgnoreCase("true")
									|| tab_anticipo.getValor("ACTIVO_NRANT").equalsIgnoreCase("true"))){
						utilitario.getConexion().ejecutarSql("update NRH_ANTICIPO set ACTIVO_NRANT=FALSE where IDE_NRANT="+tab_anticipo.getValorSeleccionado());

						String ide_nrant_anterior=tab_anticipo.getValorSeleccionado(); 
						tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
						tab_anticipo.ejecutarSql();
						tab_anticipo.setFilaActual(ide_nrant_anterior);
					}
				}else{
					String ide_nrant_anterior=tab_anticipo.getValorSeleccionado(); 

					utilitario.getConexion().ejecutarSql("update NRH_ANTICIPO set ACTIVO_NRANT=TRUE where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
					tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
					tab_anticipo.ejecutarSql();
					tab_anticipo.setFilaActual(ide_nrant_anterior);

				}
			}
		}
	}

	public void preCancelarAnticipo(){

		if (tab_anticipo.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede pre-cancelar","No existe solicitud de anticipo");
			return;
		}
		if (tab_anticipo.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede realizar la pre cancelacion", "Primero debe guardar el registro que esta trabajando");
			return;
		}
		if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
				&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede realizar la precancelacion", "El anticipo se encuentra anulado ");
			return;
		}
		if (tab_anticipo.getValor("APROBADO_NRANT") ==null 
				|| tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede realizar la precancelacion", "El anticipo aun no se encuentra Aprobado");
			return;
		}

		if (tab_amortizacion.getTotalFilas()==0 ){
			utilitario.agregarMensajeInfo("No se puede precancelar", "No existe tabla de amortizacion");
			return;
		}
		tab_pagos_anticipos.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
				"and ACTIVO_NRAMO=FALSE " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
		tab_pagos_anticipos.ejecutarSql();
		if (tab_pagos_anticipos.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede precancelar", "No existe pagos por precancelar");
			return;
		}
		eti_tot_adeuda.setValue("TOTAL ADEUDA: "+utilitario.getFormatoNumero(tab_pagos_anticipos.getSumaColumna("CUOTA_NRAMO")));
		eti_tot_precancelar.setValue("TOTAL PRECANCELAR: 0.0");
		tab_precancelacion.limpiar();
		tab_precancelacion.insertar();
		dia_precancelacion.setTitle("PRECANCELACION DE ANTICIPOS");					
		dia_precancelacion.dibujar();

	}

	public void abrirDialogoDatosCalificacion(){

		aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
		eti_empleado.setValue("Empleado Califica: ");
		eti_fecha_aut.setValue("Fecha Califica: ");
		aut_empleado_autoriza_memo.setValue(null);
		cal_fecha_autorizacion_memo.setFechaActual();
		eti_fecha_aut.setRendered(true);
		cal_fecha_autorizacion_memo.setRendered(true);

		eti_num_memo.setRendered(false);
		eti_raz_aut.setRendered(false);
		tex_num_memo.setRendered(false);
		art_razon_aut_memo.setRendered(false);
		eti_archivo_adjunto.setRendered(false);
		upl_autoriza_memo.setRendered(false);

		dia_ingreso_memo.setTitle("CALIFICACION DE ANTICIPO");
		dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarCalificacion");
		dia_ingreso_memo.dibujar();
	}

	public void calificarAnticipo(){
		if (aut_empleado.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","No ha seleccionado un empleado");
			return;
		}
		if (tab_anticipo.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","No se registran solicitudes de anticipos");
			return;
		}
		if (tab_anticipo.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo", "Primero debe guardar el registro que esta trabajando");
			return;
		}
		if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
				&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo", "El anticipo se encuentra anulado ");
			return;
		}

		if (tab_anticipo.getValor("CALIFICADO_NRANT") != null 
				&& !tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				&& tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","El anticipo ya se encuentra calificado");
			return;
		}
		if (tab_capacidad.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","El anticipo no tiene capacidad de pago");
			return;
		}
		if (tab_amortizacion.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","El anticipo no tiene tabla de amortizacion");
			return;
		}

		if (tab_capacidad.isFilaInsertada()
				|| tab_rubro_detalle_pago.isFilaInsertada()
				|| tab_amortizacion.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede calificar el anticipo","Debe guardar las transaccciones en curso");
			return;
		}

		if (tab_anticipo.getValor("REAPROBADO_NRANT")!=null 
				&& !tab_anticipo.getValor("REAPROBADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("REAPROBADO_NRANT").equalsIgnoreCase("true")){
			// salta las validaciones y abre dialogo de datos de calificacion
			abrirDialogoDatosCalificacion();
			return;
		}


		double dou_monto_solicitado=0;
		try {
			dou_monto_solicitado=Double.parseDouble(tab_anticipo.getValor("MONTO_APROBADO_NRANT"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (!validarMontoMaximoAnticipo(dou_monto_solicitado)){
			boo_insertar=false;
			con_datos_memo.getBot_aceptar().setMetodo("llenarDatosMemo");
			con_datos_memo.dibujar();
			utilitario.addUpdate("con_datos_memo");
			return;
		}

		if (!validarPlazoMaximoPagoCondicionesAnticipo()){
			boo_insertar=false;
			con_datos_memo.getBot_aceptar().setMetodo("llenarDatosMemo");
			con_datos_memo.dibujar();
			utilitario.addUpdate("con_datos_memo");
			return;
		}

		if (!calcularLimiteCuotaMensual()){
			boo_insertar=false;
			con_datos_memo.setMessage("EL EMPLEADO NO CUMPLE CON LAS CONDICIONES DE CAPACIDAD DE PAGO <br>" +
					"</br>LIMITE CUOTA MENSUAL SUPERA EL 40 % DEL LIQUIDO A RECIBIR <br>  " +
					"</br>PERO DE TODAS MANERAS DESEA CALIFICAR EL ANTICIPO ");
			con_datos_memo.getBot_aceptar().setMetodo("llenarDatosMemo");
			con_datos_memo.dibujar();
			utilitario.addUpdate("con_datos_memo");
			return;
		}
		if (!calcularPorcentajeEndeudamiento()){
			boo_insertar=false;
			con_datos_memo.setMessage("EL EMPLEADO NO CUMPLE CON LAS CONDICIONES DE CAPACIDAD DE PAGO<br>" +
					"</br>PORCENTAJE ENDEUDAMIENTO ES MAYOR QUE EL 70 POR CIENTO <br>  " +
					"</br>PERO DE TODAS MANERAS DESEA CALIFICAR EL ANTICIPO ");
			con_datos_memo.getBot_aceptar().setMetodo("llenarDatosMemo");
			con_datos_memo.dibujar();
			utilitario.addUpdate("con_datos_memo");
			return;
		}

		// si pasa las validaciones abre el dialogo de datos de calificacion
		abrirDialogoDatosCalificacion();
	}
	public boolean validarDatosMemo(){
		if (aut_empleado_autoriza_memo.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar el empleado que autoriza el anticipo");
			return false;
		}
		if (tex_num_memo.getValue()==null || tex_num_memo.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe ingresar el numero de memo ");
			return false;
		}
		if (cal_fecha_autorizacion_memo.getFecha()==null || cal_fecha_autorizacion_memo.getFecha().toString().isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar la fecha de autorizacion ");
			return false;
		}
		if (art_razon_aut_memo.getValue()==null || art_razon_aut_memo.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe llenar la razon de la autorizacion ");
			return false;
		}

		return true;
	}

	public void aceptarDatosMemo(){

		if (validarDatosMemo()){

			System.out.println("archivo "+upl_autoriza_memo.getArchivo());

			String archivo=upl_autoriza_memo.getArchivo()+"";
			String carpeta=upl_autoriza_memo.getCarpeta()+"";
			String ruta="";
			if (archivo!=null && !archivo.isEmpty()
					&& carpeta!=null && !carpeta.isEmpty()){
				if (archivo.indexOf(carpeta)!=-1){
					archivo=archivo.substring(archivo.indexOf(carpeta)+carpeta.length()+1,archivo.length());
				}
				System.out.println("nombre archivo "+archivo);	
				ruta="/upload/"+upl_autoriza_memo.getCarpeta()+"/"+archivo;
			}

			if (boo_insertar){
				dia_ingreso_memo.cerrar();
				tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
				tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
				tab_anticipo.getColumna("IDE_NRMOA").setLectura(false);
				tab_anticipo.insertar();
				tab_anticipo.setValor("GEN_IDE_GEEDP3", aut_empleado_autoriza_memo.getValor());
				tab_anticipo.setValor("NRO_MEMO_NRANT", tex_num_memo.getValue()+"");
				tab_anticipo.setValor("FECHA_AUTORIZA_NRANT", cal_fecha_autorizacion_memo.getFecha());
				tab_anticipo.setValor("RAZON_AUTORIZA_NRANT", art_razon_aut_memo.getValue()+"");
				tab_anticipo.setValor("REAPROBADO_NRANT","true");
				tab_anticipo.setValor("ARCHIVO_MEMO_NRANT",ruta);
				tab_anticipo.setValor("IDE_GEEDP",ide_geedp_activo);
				tab_anticipo.setValor("IDE_GTEMP", aut_empleado.getValor());
				tab_anticipo.setValor("NRO_ANTICIPO_NRANT", ser_anticipo.getNumeroAnticipo(aut_empleado.getValor()));
			}else{
				dia_ingreso_memo.cerrar();
				tab_anticipo.setValor("GEN_IDE_GEEDP3", aut_empleado_autoriza_memo.getValor());
				tab_anticipo.setValor("NRO_MEMO_NRANT", tex_num_memo.getValue()+"");
				tab_anticipo.setValor("FECHA_AUTORIZA_NRANT", cal_fecha_autorizacion_memo.getFecha());
				tab_anticipo.setValor("RAZON_AUTORIZA_NRANT", art_razon_aut_memo.getValue()+"");
				tab_anticipo.setValor("REAPROBADO_NRANT","true");
				tab_anticipo.setValor("ARCHIVO_MEMO_NRANT",ruta);
				utilitario.addUpdateTabla(tab_anticipo,"GEN_IDE_GEEDP3,NRO_MEMO_NRANT,FECHA_AUTORIZA_NRANT,RAZON_AUTORIZA_NRANT,REAPROBADO_NRANT", "");

			}

			//			aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
			//					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
			//					" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
			//					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
			//					"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
			//					"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
			//					"DEPA.DETALLE_GEDEP " +
			//					"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
			//					"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
			//					"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
			//					"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
			//					"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
			//					"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
			//			aut_empleado_autoriza_memo.setValue(null);
			//			cal_fecha_autorizacion_memo.setFechaActual();
			//			eti_empleado.setValue("Empleado Califica: ");
			//			eti_fecha_aut.setValue("Fecha Califica: ");
			//
			//			tex_num_memo.setRendered(false);
			//			art_razon_aut_memo.setRendered(false);
			//			eti_num_memo.setRendered(false);
			//			eti_raz_aut.setRendered(false);
			//
			//			dia_ingreso_memo.setTitle("CALIFICACION DE ANTICIPO");
			//			dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarCalificacion");
			//			dia_ingreso_memo.dibujar();
		}
	}

	public void guardarDatosCalificacion(){

		tab_anticipo.modificar(tab_anticipo.getFilaActual());
		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"FECHA_RRHH_NRANT", cal_fecha_autorizacion_memo.getFecha());
		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"CALIFICADO_NRANT", "true");
		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"GEN_IDE_GEEDP2", aut_empleado_autoriza_memo.getValor());
		utilitario.addUpdateTabla(tab_anticipo, "CALIFICADO_NRANT,GEN_IDE_GEEDP2", "");

		tab_anticipo.guardar();
		guardarPantalla();
		con_guardar.cerrar();
		String ide_nrant_anterior=tab_anticipo.getValorSeleccionado();
		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();
		tab_anticipo.setFilaActual(ide_nrant_anterior);

		cargarTablasPantalla();
		cargarTotales();
	}
	public void aceptarCalificacion(){

		if (utilitario.isFechaMenor(utilitario.getFecha(cal_fecha_autorizacion_memo.getFecha()), utilitario.getFecha(tab_anticipo.getValor("FECHA_SOLICITUD_NRANT")))){
			utilitario.agregarMensajeInfo("No se puede continuar", "La fecha seleccionada no puede ser menor que la fecha de solicitud "+tab_anticipo.getValor("FECHA_SOLICITUD_NRANT"));
			return;
		}

		dia_ingreso_memo.cerrar();
		con_guardar.setTitle("CONFIRMACION CALIFICACION DE ANTICIPOS");
		con_guardar.setMessage("ESTA SEGURO DE GUARDAR LOS DATOS");
		con_guardar.getBot_aceptar().setMetodo("guardarDatosCalificacion");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");

	}

	boolean boo_insertar=false;
	public void insertarDatosMemo(){
		boo_insertar=true;
		llenarDatosMemo();
	}

	public void llenarDatosMemo(){

		con_datos_memo.cerrar();
		aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
		aut_empleado_autoriza_memo.setValue(null);
		cal_fecha_autorizacion_memo.setFechaActual();

		eti_empleado.setValue("Empleado Autoriza: ");
		eti_num_memo.setValue("Numero Memo: ");
		eti_fecha_aut.setValue("Fecha Autorizacion: ");
		eti_raz_aut.setValue("Motivo Autoriza: ");
		eti_archivo_adjunto.setValue("Adjuntar Archivo: ");

		tex_num_memo.setValue("");
		art_razon_aut_memo.setValue("");
		tex_num_memo.setRendered(true);
		art_razon_aut_memo.setRendered(true);
		eti_num_memo.setRendered(true);
		eti_raz_aut.setRendered(true);
		eti_archivo_adjunto.setRendered(true);
		upl_autoriza_memo.setRendered(true);
		dia_ingreso_memo.setTitle("AUTORIZACION DE ANTICIPO");
		dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarDatosMemo");
		dia_ingreso_memo.dibujar();
		utilitario.addUpdate("dia_ingreso_memo");
	}

	public void guardarDatosAprobacion(){

		tab_anticipo.modificar(tab_anticipo.getFilaActual());

		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"FECHA_VENCIMIENTO_NRANT", tab_amortizacion.getValor(tab_amortizacion.getTotalFilas()-1, "FECHA_VENCIMIENTO_NRAMO"));

		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"FECHA_APROBACION_NRANT", cal_fecha_autorizacion_memo.getFecha());
		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"APROBADO_NRANT", "true");
		tab_anticipo.setValor(tab_anticipo.getFilaActual(),"GEN_IDE_GEEDP", aut_empleado_autoriza_memo.getValor());
		utilitario.addUpdateTabla(tab_anticipo, "APROBADO_NRANT,GEN_IDE_GEEDP,FECHA_APROBACION_NRANT,FECHA_VENCIMIENTO_NRANT", "");

		tab_anticipo.guardar();
		guardarPantalla();
		con_guardar.cerrar();
		String ide_nrant_anterior=tab_anticipo.getValorSeleccionado();
		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();
		tab_anticipo.setFilaActual(ide_nrant_anterior);

		cargarTablasPantalla();
		cargarTotales();
	}
	public void aceptarAprobacion(){
		if (utilitario.isFechaMenor(utilitario.getFecha(cal_fecha_autorizacion_memo.getFecha()), utilitario.getFecha(tab_anticipo.getValor("FECHA_SOLICITUD_NRANT")))){
			utilitario.agregarMensajeInfo("No se puede continuar", "La fecha seleccionada no puede ser menor que la fecha de solicitud "+tab_anticipo.getValor("FECHA_SOLICITUD_NRANT"));
			return;
		}

		
		dia_ingreso_memo.cerrar();

		con_guardar.setTitle("CONFIRMACION APROBACION DE ANTICIPOS");
		con_guardar.setMessage("ESTA SEGURO DE GUARDAR LOS DATOS");
		con_guardar.getBot_aceptar().setMetodo("guardarDatosAprobacion");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");

	}

	public void abrirDialogoDatosAprobacion(){
		aut_empleado_autoriza_memo.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
		eti_empleado.setValue("Empleado Aprueba: ");
		eti_fecha_aut.setValue("Fecha Aprueba: ");
		aut_empleado_autoriza_memo.setValue(null);
		cal_fecha_autorizacion_memo.setFechaActual();
		eti_fecha_aut.setRendered(true);
		cal_fecha_autorizacion_memo.setRendered(true);
		eti_num_memo.setRendered(false);
		eti_raz_aut.setRendered(false);
		tex_num_memo.setRendered(false);
		art_razon_aut_memo.setRendered(false);
		eti_archivo_adjunto.setRendered(false);
		upl_autoriza_memo.setRendered(false);

		dia_ingreso_memo.setTitle("APROBACION DE ANTICIPO");
		dia_ingreso_memo.getBot_aceptar().setMetodo("aceptarAprobacion");
		dia_ingreso_memo.dibujar();
	}
	public void aprobarAnticipo(){

		if (aut_empleado.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","No ha seleccionado un empleado");
			return;
		}
		if (tab_anticipo.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","No se registran solicitudes de anticipos");
			return;
		}
		if (tab_anticipo.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo", "Primero debe guardar el registro que esta trabajando");
			return;
		}
		if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
				&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo", "El anticipo se encuentra anulado ");
			return;
		}
		if (tab_anticipo.getValor("APROBADO_NRANT") != null 
				&& !tab_anticipo.getValor("APROBADO_NRANT").isEmpty() 
				&& tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","El anticipo ya se encuentra aprobado");
			return;
		}
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","Primero debe calificar el anticipo ");
			return;
		}


		if (tab_garante.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","No existe un garante ");
			return;
		}

		if (tab_garante.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede aprobar el anticipo","Debe guardar el garante ");
			return;
		}

		abrirDialogoDatosAprobacion();
	}
	public void cambiaFechaCalculo(AjaxBehaviorEvent evt){
		tab_capacidad.modificar(evt);
		//		calcularRubrosDetallePagos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}

	public void cambiaFechaCalculo(DateSelectEvent evt){
		tab_capacidad.modificar(evt);
		//		calcularRubrosDetallePagos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}

	public void aplicaDescuento(Tabla tab_rubro_det_pago,String fecha_vencimiento){
		for (int i = 0; i < tab_rubro_det_pago.getTotalFilas(); i++) {

		}
	}
	public String getFechaPagoRubro(String IDE_NRDER){
		return null;
	}


	private boolean validarDatosAmortizacion(){
		if (tab_anticipo_interes.getValor("TASA_INTERES_NRANI")==null || tab_anticipo_interes.getValor("TASA_INTERES_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo tasa de interes es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("TASA_EFECTIVA_NRANI")==null || tab_anticipo_interes.getValor("TASA_EFECTIVA_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo tasa de Efectiva es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("PLAZO_NRANI")==null || tab_anticipo_interes.getValor("PLAZO_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo Plazo es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("AMORTIZACION_NRANI")==null || tab_anticipo_interes.getValor("AMORTIZACION_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo Amortizacion cada es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("MES_GRACIA_NRANI")==null || tab_anticipo_interes.getValor("MES_GRACIA_NRANI").isEmpty()){
			tab_anticipo_interes.setValor("MES_GRACIA_NRANI","0");
		}

		if (tab_capacidad.getValor("FECHA_CALCULO_NRCAP")==null || tab_capacidad.getValor("FECHA_CALCULO_NRCAP").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la tabla de amortizacion", "No existe fecha de calculo");
			return false;
		}
		return true;
	}

	public void calcularTasaEfectiva(AjaxBehaviorEvent evt){
		tab_anticipo_interes.modificar(evt);
		double tasa_interes=0;
		double amortizacion_cada=0;
		double tasa_efectiva=0;
		try {
			tasa_interes=Double.parseDouble(tab_anticipo_interes.getValor("TASA_INTERES_NRANI"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			amortizacion_cada=Double.parseDouble(tab_anticipo_interes.getValor("AMORTIZACION_NRANI"));		
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (amortizacion_cada!=0){
			tasa_efectiva=(Math.pow((1+((tasa_interes/100)/(360/amortizacion_cada))), (360/amortizacion_cada))-1)*100;
		}
		tab_anticipo_interes.setValor("TASA_EFECTIVA_NRANI",utilitario.getFormatoNumero(tasa_efectiva, 4));

		utilitario.addUpdateTabla(tab_anticipo_interes,"TASA_EFECTIVA_NRANI","tab_tabulador:tex_tasa_efectiva,tab_tabulador:tex_num_periodos");

		if (aut_empleado.getValor()!=null){
			calcularTablaAmortizacion();
		}

	}



	public void calcularTablaAmortizacion(){


		if (tab_anticipo.getValor("CALIFICADO_NRANT") ==null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			if (validarDatosAmortizacion()){
				TablaGenerica tab_amort=ser_anticipo.getTablaAmortizacion(
						//ser_nomina.getIdeDetalleTipoNomina(ide_geedp_activo),
						Double.parseDouble(tab_capacidad.getValor("MONTO_RECIBIR_NRCAP")), 
						Double.parseDouble(tab_anticipo_interes.getValor("TASA_INTERES_NRANI")), 
						pckUtilidades.CConversion.CInt(tab_anticipo_interes.getValor("PLAZO_NRANI")), 
						pckUtilidades.CConversion.CInt(tab_anticipo_interes.getValor("AMORTIZACION_NRANI")), 
						pckUtilidades.CConversion.CInt(tab_anticipo_interes.getValor("MES_GRACIA_NRANI")), 
						tab_capacidad.getValor("FECHA_CALCULO_NRCAP"),
						tab_capacidad.getValorSeleccionado());
				int band=0;
				if (!tab_amortizacion.isFilaInsertada()){
					band=1;
					utilitario.getConexion().ejecutarSql("delete from nrh_amortizacion where ide_nrani="+tab_anticipo_interes.getValorSeleccionado());
				}else{
					band=1;
				}
				tab_amortizacion.limpiar();
				System.out.println("tab amor "+tab_amort.getTotalFilas());
				for (int i = 0; i < tab_amort.getTotalFilas(); i++) {
					// inserto las filas de la amortizacion
					tab_amortizacion.insertar();
					tab_amortizacion.setValor("CAPITAL_NRAMO", tab_amort.getValor(i, "CAPITAL_NRAMO"));
					tab_amortizacion.setValor("INTERES_NRAMO", tab_amort.getValor(i, "INTERES_NRAMO"));
					tab_amortizacion.setValor("CUOTA_NRAMO", tab_amort.getValor(i, "CUOTA_NRAMO"));
					tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", tab_amort.getValor(i, "FECHA_VENCIMIENTO_NRAMO"));
					tab_amortizacion.setValor("PRINCIPAL_NRAMO", tab_amort.getValor(i, "PRINCIPAL_NRAMO"));
					tab_amortizacion.setValor("NRO_CUOTA_NRAMO", tab_amort.getValor(i, "NRO_CUOTA_NRAMO"));
					tab_amortizacion.setValor("IDE_NRRUB", tab_amort.getValor(i, "IDE_NRRUB"));
				}


				//					tab_anticipo.guardar();
				//					tab_capacidad.guardar();
				//					tab_anticipo_interes.guardar();
				//					tab_amortizacion.guardar();
				//
				//					System.out.println("pantalla "+utilitario.getConexion().getSqlPantalla());
				//					guardarPantalla();

				tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(tab_amortizacion.getSumaColumna("CUOTA_NRAMO"));
				tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(tab_amortizacion.getSumaColumna("PRINCIPAL_NRAMO"));


				utilitario.addUpdate("tab_tabulador:tab_amortizacion");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede generar la tabla de amortizacion", "El anticipo ya se enuentra calificado");
		}
	}

	public boolean calcularLimiteCuotaMensual(){
		double tot_ingresos=0;
		double tot_egresos=0;
		double tot_liquido=0;
		double tot_cuota_mensual=0;
		if (tab_capacidad.getTotalFilas()>0){
			try {
				tot_ingresos=Double.parseDouble(tab_capacidad.getValor("TOTAL_INGRESO_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				tot_egresos=Double.parseDouble(tab_capacidad.getValor("TOTAL_EGRESO_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				tot_cuota_mensual=Double.parseDouble(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}

			tot_liquido=tot_ingresos-tot_egresos;
			tot_liquido=(tot_liquido*40)/100;

			if (tab_capacidad.getTotalFilas()>0){
				tab_capacidad.setValor("CUOTA_LIMITE_NRCAP", utilitario.getFormatoNumero(tot_liquido,4));
			}
			if (tot_cuota_mensual<=tot_liquido && tot_liquido>0){
				eti_aprobado.setStyle("font-size: 13px;color: black;font-weight: bold");
				eti_aprobado.setValue("CUOTA ASIGNADA, LA CUOTA MENSUAL NO SUPERA EL 40% ");
				utilitario.agregarMensaje("CUOTA ASIGNADA","LA CUOTA MENSUAL NO SUPERA EL 40% ");
				utilitario.addUpdateTabla(tab_capacidad, "CUOTA_LIMITE_NRCAP","tab_tabulador:eti_aprobado");
				return true;

			}else{
				eti_aprobado.setStyle("font-size: 13px;color: red;font-weight: bold");
				eti_aprobado.setValue("CUOTA NO ASIGNADA, LA CUOTA MENSUAL SUPERA EL 40% ");
				utilitario.agregarMensajeError("CUOTA NO ASIGNADA","LA CUOTA MENSUAL SUPERA EL 40%");
				utilitario.addUpdateTabla(tab_capacidad, "CUOTA_LIMITE_NRCAP","tab_tabulador:eti_aprobado");
				return false;
			}

		}
		return false;
	}
	public void aceptarIngresosEgresosEmpleado(){
		calcularPromedioIngresosEgresos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();

		dia_roles_empleado.cerrar();
	}


	public void verIngresosEgresosEmpleado(){

		if (tab_capacidad.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede ver los ingresos/egresos del empleado", "No existe capacidad de pago");
			return;
		}

		//tab_ingresos_egresos_emp.setSql(ser_nomina.getSqlIngresosEgresosEmpleado(ide_geedp_activo));
		tab_ingresos_egresos_emp.setSql(ser_nomina.getSqlIngresosEgresosEmpleado(aut_empleado.getValor())); //ABECERRA
		tab_ingresos_egresos_emp.ejecutarSql();
		eti_prom_ing.setValue("PROMEDIO INGRESOS:");
		eti_prom_egr.setValue("PROMEDIO EGRESOS:");
		dia_roles_empleado.dibujar();
	}

	private void calcularPromedioIngresosEgresos(){
		double total_ingresos = 0;
		double total_egresos = 0;
		double prom_ing=0;
		double prom_egr=0;
		double count=0;
		for (Fila actual : tab_ingresos_egresos_emp.getSeleccionados()) {
			try {
				total_ingresos = Double.parseDouble(actual.getCampos()[4] + "") + total_ingresos;
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				total_egresos = Double.parseDouble(actual.getCampos()[5] + "") + total_egresos;
			} catch (Exception e) {
				// TODO: handle exception
			}
			count=count+1;
		}
		if (count!=0){
			prom_ing=total_ingresos/count;
			prom_egr=total_egresos/count;
		}

		eti_prom_ing.setValue("PROMEDIO INGRESOS: "+utilitario.getFormatoNumero(prom_ing, 2));
		eti_prom_egr.setValue("PROMEDIO EGRESOS: "+utilitario.getFormatoNumero(prom_egr, 2));

		if (tab_capacidad.getTotalFilas()>0){
			//			if (!tab_capacidad.isFilaInsertada()){
			tab_capacidad.modificar(tab_capacidad.getFilaActual());
			//}
			tab_capacidad.setValor("TOTAL_INGRESO_NRCAP", utilitario.getFormatoNumero(prom_ing, 2));
			tab_capacidad.setValor("TOTAL_EGRESO_NRCAP", utilitario.getFormatoNumero(prom_egr, 2));
			utilitario.addUpdateTabla(tab_capacidad, "TOTAL_INGRESO_NRCAP,TOTAL_EGRESO_NRCAP", "");
		}
		utilitario.addUpdate("eti_prom_ing,eti_prom_egr");

	}
	public void quitaSeleccionRolEmpleado(UnselectEvent evt){
		calcularPromedioIngresosEgresos();
	}
	public void seleccionaRolEmpleado(SelectEvent evt){
		tab_ingresos_egresos_emp.seleccionarFila(evt);
		calcularPromedioIngresosEgresos();
	}

	public boolean validarPlazoMaximoPagoCondicionesAnticipo(){
		String maximo_dias_pago=ser_anticipo.getPlazoMaximoPagoAnticipos(ide_geedp_activo);

		if (maximo_dias_pago==null){
			utilitario.agregarMensajeInfo("Numero de meses invalido", "El empleado no tiene Plazo maximo de pago en Condiciones de anticipos");
			return false;
		}

		try {
			if (pckUtilidades.CConversion.CInt(maximo_dias_pago)<(pckUtilidades.CConversion.CInt(tab_capacidad.getValor("NRO_MES_NRCAP"))*30)){
				utilitario.agregarMensajeInfo("Numero de meses invalido", "El numero de meses ingresado sobrepasa el plazo maximo de pago de las condiciones de anticipos, Plazo maximo de pago: "+maximo_dias_pago+" dias");
				con_datos_memo.setMessage("EL EMPLEADO NO CUMPLE CON LAS CONDICIONES DE ANTICIPOS<br>" +
						"</br>EL PLAZO SOBREPASA LO PERMITIDO, PLAZO MAXIMO DE PAGO:"+maximo_dias_pago +" dias <br>"+
						"</br>PERO DE TODAS MANERAS DESEA CALIFICAR EL ANTICIPO ");
				//tab_capacidad.setValor("NRO_MES_NRCAP", (pckUtilidades.CConversion.CInt(maximo_dias_pago)/30)+"");
				//utilitario.addUpdateTabla(tab_capacidad, "NRO_MES_NRCAP", "");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	public void cambiaMontoSolicitado(AjaxBehaviorEvent evt){
		tab_anticipo.modificar(evt);
		if (tab_anticipo.getValor("REAPROBADO_NRANT") == null
				|| tab_anticipo.getValor("REAPROBADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("REAPROBADO_NRANT").equalsIgnoreCase("false")){
			if (validarPlazoMaximoPagoCondicionesAnticipo()){

				double dou_monto_solicitado=0;
				try {
					dou_monto_solicitado=Double.parseDouble(tab_anticipo.getValor("MONTO_SOLICITADO_NRANT"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (validarMontoMaximoAnticipo(dou_monto_solicitado)){
					calcularCuota();
					calcularPorcentajeEndeudamiento();
					calcularLimiteCuotaMensual();
				}
			}
		}else{
			if (validarPlazoMaximoPagoCondicionesAnticipo()){
				double dou_monto_solicitado=0;
				try {
					dou_monto_solicitado=Double.parseDouble(tab_anticipo.getValor("MONTO_SOLICITADO_NRANT"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (validarMontoMaximoAnticipo(dou_monto_solicitado)){
					tab_anticipo.setValor("GEN_IDE_GEEDP3", "");
					tab_anticipo.setValor("NRO_MEMO_NRANT", "");
					tab_anticipo.setValor("FECHA_AUTORIZA_NRANT", "");
					tab_anticipo.setValor("RAZON_AUTORIZA_NRANT", "");
					tab_anticipo.setValor("REAPROBADO_NRANT","false");
					utilitario.addUpdateTabla(tab_anticipo,"GEN_IDE_GEEDP3,NRO_MEMO_NRANT,FECHA_AUTORIZA_NRANT,RAZON_AUTORIZA_NRANT,REAPROBADO_NRANT", "");
				}
			}
			calcularCuota();
			calcularPorcentajeEndeudamiento();
			calcularLimiteCuotaMensual();
		}
	}

	public void cambiaMontoRecibir(AjaxBehaviorEvent evt){
		tab_capacidad.modificar(evt);

		double dou_monto_solicitado=0;
		try {
			dou_monto_solicitado=Double.parseDouble(tab_anticipo.getValor("MONTO_SOLICITADO_NRANT"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		double dou_monto_recibir=0;
		try {
			dou_monto_recibir=Double.parseDouble(tab_capacidad.getValor("MONTO_RECIBIR_NRCAP"));
		} catch (Exception e) {
			// TODO: handle exception
		}


		if (dou_monto_recibir<=dou_monto_solicitado){
			tab_anticipo.setValor("MONTO_APROBADO_NRANT",tab_capacidad.getValor("MONTO_RECIBIR_NRCAP"));
			utilitario.getConexion().agregarSqlPantalla("update NRH_ANTICIPO set MONTO_APROBADO_NRANT="+tab_capacidad.getValor("MONTO_RECIBIR_NRCAP")+" where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
			utilitario.addUpdate("tab_anticipo");
		}else{
			tab_anticipo.setValor("MONTO_APROBADO_NRANT",utilitario.getFormatoNumero(dou_monto_solicitado));
			utilitario.getConexion().agregarSqlPantalla("update NRH_ANTICIPO set MONTO_APROBADO_NRANT="+dou_monto_solicitado+" where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
			utilitario.addUpdate("tab_anticipo");
			utilitario.agregarMensajeInfo("Monto erroneo","El monto ingresado sobrepasa el monto solicitado de anticipo ");
			tab_capacidad.setValor("MONTO_RECIBIR_NRCAP",utilitario.getFormatoNumero(dou_monto_solicitado));
			utilitario.addUpdateTabla(tab_capacidad, "MONTO_RECIBIR_NRCAP", "tab_tabulador:tab_capacidad");
		}

		validarPlazoMaximoPagoCondicionesAnticipo();

		validarMontoMaximoAnticipo(dou_monto_recibir);
		calcularRubrosDetallePagos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}

	public void calcularValorCuota(AjaxBehaviorEvent evt){
		tab_capacidad.modificar(evt);

		int nro_mes_sol=0;
		try {
			nro_mes_sol=pckUtilidades.CConversion.CInt(tab_anticipo.getValor("NRO_MES_NRANT"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		int nro_mes_cpa=0;
		try {
			nro_mes_cpa=pckUtilidades.CConversion.CInt(tab_capacidad.getValor("NRO_MES_NRCAP"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (nro_mes_cpa>nro_mes_sol){
			utilitario.agregarMensajeInfo("numero de meses ingresado incorrecto", "el numero de meses de la capacidad de pago no puede ser mayor que el numero de meses del anticipo solicitado");
			return;
		}

		if (tab_anticipo_interes.getTotalFilas()>0){
			tab_anticipo_interes.setValor("PLAZO_NRANI",tab_capacidad.getValor("NRO_MES_NRCAP"));
			utilitario.getConexion().agregarSqlPantalla("update NRH_ANTICIPO_INTERES set PLAZO_NRANI="+tab_capacidad.getValor("NRO_MES_NRCAP")+" where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
			utilitario.addUpdate("tab_tabulador:tab_anticipo_interes");
		}


		//		if (tab_anticipo.getValor("REAPROBADO_NRANT") == null
		//				|| tab_anticipo.getValor("REAPROBADO_NRANT").isEmpty()
		//				|| tab_anticipo.getValor("REAPROBADO_NRANT").equalsIgnoreCase("false")){
		validarPlazoMaximoPagoCondicionesAnticipo();

		double dou_monto_solicitado=0;
		try {
			dou_monto_solicitado=Double.parseDouble(tab_anticipo.getValor("MONTO_APROBADO_NRANT"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		validarMontoMaximoAnticipo(dou_monto_solicitado);
		calcularRubrosDetallePagos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
		//		}else{
		//			if (validarPlazoMaximoPagoCondicionesAnticipo()){
		//				if (validarMontoMaximoAnticipo()){
		//					tab_anticipo.setValor("GEN_IDE_GEEDP3", "");
		//					tab_anticipo.setValor("NRO_MEMO_NRANT", "");
		//					tab_anticipo.setValor("FECHA_AUTORIZA_NRANT", "");
		//					tab_anticipo.setValor("RAZON_AUTORIZA_NRANT", "");
		//					tab_anticipo.setValor("REAPROBADO_NRANT","false");
		//					utilitario.addUpdateTabla(tab_anticipo,"GEN_IDE_GEEDP3,NRO_MEMO_NRANT,FECHA_AUTORIZA_NRANT,RAZON_AUTORIZA_NRANT,REAPROBADO_NRANT", "");
		//				}
		//			}
		//			calcularRubrosDetallePagos();
		//			calcularCuota();
		//			calcularPorcentajeEndeudamiento();
		//			calcularLimiteCuotaMensual();
		//		}
		//
	}

	public void calcularValorDescuento1(){
		double porcentaje=0;
		double valor_rubro=0;
		double valor_descuento=0;
		try {
			porcentaje=Double.parseDouble(tab_rubro_detalle_pago.getValor("PORCENTAJE_NRRDP"));
			if (Double.parseDouble(tab_rubro_detalle_pago.getValor("PORCENTAJE_NRRDP"))>100){
				porcentaje=0;
				utilitario.agregarMensajeInfo("Porcentaje invalido", "El porcentaje no puede ser mayor a 100");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			valor_rubro=Double.parseDouble(tab_rubro_detalle_pago.getValor("VALOR_RUBRO_NRRDP"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			valor_descuento=(valor_rubro*porcentaje)/100;
		} catch (Exception e) {
			// TODO: handle exception
		}
		tab_rubro_detalle_pago.setValor("PORCENTAJE_NRRDP",utilitario.getFormatoNumero(porcentaje,2));
		tab_rubro_detalle_pago.setValor("VALOR_DESCUENTO_NRRDP",utilitario.getFormatoNumero(valor_descuento,2));
		utilitario.addUpdateTabla(tab_rubro_detalle_pago, "PORCENTAJE_NRRDP,VALOR_DESCUENTO_NRRDP", "");
	}
	public void calcularValorDescuentoPor(){
		double porcentaje=0;
		double valor_rubro=0;
		double valor_descuento=0;
		try {
			valor_descuento=Double.parseDouble(tab_rubro_detalle_pago.getValor("VALOR_DESCUENTO_NRRDP"));
		} catch (Exception e) {
			// TODO: handle exception0000
		}

		try {
			valor_rubro=Double.parseDouble(tab_rubro_detalle_pago.getValor("VALOR_RUBRO_NRRDP"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (valor_descuento>valor_rubro){
			
			utilitario.agregarMensajeInfo("Atencion", "el valor de descuento no puede ser mayor que el valor del rubro");
			porcentaje=0;
			valor_descuento=0;
		}
		
		if(valor_rubro!=0){
			try {
				porcentaje=(valor_descuento*100)/valor_rubro;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		//		if (valor_descuento>valor_rubro){
		//			valor_descuento=0;
		//			porcentaje=0;
		//			utilitario.agregarMensajeInfo("Valor de descuento invalido", "El valor de descuento no puede ser mayor que el valor del rubro");
		//		}

		tab_rubro_detalle_pago.setValor("PORCENTAJE_NRRDP",utilitario.getFormatoNumero(porcentaje,2));
		tab_rubro_detalle_pago.setValor("VALOR_DESCUENTO_NRRDP",utilitario.getFormatoNumero(valor_descuento,2));
		utilitario.addUpdateTabla(tab_rubro_detalle_pago, "VALOR_DESCUENTO_NRRDP,PORCENTAJE_NRRDP", "");
	}
	public void calcularValorDescuento(AjaxBehaviorEvent evt){
		tab_rubro_detalle_pago.modificar(evt);
		calcularValorDescuento1();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}

	public void calcularValorDescuento2(AjaxBehaviorEvent evt){
		tab_rubro_detalle_pago.modificar(evt);
		calcularValorDescuentoPor();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}
	public void calcularRubrosDetallePagos(){
		System.out.println("tab_rubro_detalle "+tab_rubro_detalle_pago.getSql());
		//for (int i = 0; i < tab_rubro_detalle_pago.getTotalFilas(); i++) {

		if (tab_rubro_detalle_pago.getTotalFilas()==0){
			return;
		}

		if (tab_rubro_detalle_pago.getValor("IDE_NRRUB")!=null && !tab_rubro_detalle_pago.getValor("IDE_NRRUB").isEmpty()){

			String str_ide_nrdtn=ser_nomina.getIdeDetalleTipoNomina(ide_geedp_activo);

			String ide_gereg="";
			try {
				ide_gereg=serv_empleado.getRegionEmpleado(aut_empleado.getValor()).getValor("IDE_GEREG");
			} catch (Exception e) {
				// TODO: handle exception
			}

			System.out.println("IDE_GEREG "+ide_gereg);
			if (tab_rubro_detalle_pago.getValor("IDE_NRRUB").equals(utilitario.getVariable("p_nrh_rubro_descuento_decimo_cuarto"))){


				String fecha_pago_dec=ser_nomina.getFechaPagoDecimoCuartoSueldo(str_ide_nrdtn,ide_gereg);
				String fecha_fin_dec=ser_nomina.getFechaFinalDecimoCuartoSueldo(str_ide_nrdtn,ide_gereg);

				String fecha_calculo=tab_capacidad.getValor("FECHA_CALCULO_NRCAP");
				String num_cuotas=tab_capacidad.getValor("NRO_MES_NRCAP");
				String amortizacion_cada=utilitario.getVariable("p_amortizacion_cada");
				int num_dias=0;
				try {
					num_dias=pckUtilidades.CConversion.CInt(num_cuotas)*pckUtilidades.CConversion.CInt(amortizacion_cada);
				} catch (Exception e) {
					// TODO: handle exception
				}
				String fecha_fin_pagos=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_calculo), num_dias));
				System.out.println("fecha calculo "+fecha_calculo);
				System.out.println("num dias "+num_dias);
				System.out.println("fecha_pago dec "+fecha_pago_dec);
				System.out.println("fecha_fin_pagos "+fecha_fin_pagos);
				if (fecha_pago_dec!=null && !fecha_pago_dec.isEmpty()
						//&& !utilitario.isFechaMenor(utilitario.getFecha(fecha_pago_dec), utilitario.getFecha(fecha_calculo))
						&& !utilitario.isFechaMayor(utilitario.getFecha(fecha_pago_dec), utilitario.getFecha(fecha_fin_pagos))){
					tab_rubro_detalle_pago.setValor("VALOR_RUBRO_NRRDP",(ser_nomina.getDecimoCuartoSueldo(ide_geedp_activo,fecha_calculo)));
					tab_rubro_detalle_pago.setValor("FECHA_PAGO_RUBRO_NRRDP",fecha_pago_dec);
					calcularValorDescuento1();
				}else{
					tab_rubro_detalle_pago.setValor("VALOR_RUBRO_NRRDP","0");
					tab_rubro_detalle_pago.setValor("PORCENTAJE_NRRDP","");
					tab_rubro_detalle_pago.setValor("VALOR_DESCUENTO_NRRDP","");
					tab_rubro_detalle_pago.setValor("FECHA_PAGO_RUBRO_NRRDP","");
					utilitario.agregarMensajeInfo("Atencion", "La fecha de pago del rubro seleccionado no coincide con ninguna fecha de pago de anticipo");
					return;
				}
			}else if (tab_rubro_detalle_pago.getValor("IDE_NRRUB").equals(utilitario.getVariable("p_nrh_rubro_descuento_decimo_tercer"))){

				String fecha_pago_dec=ser_nomina.getFechaPagoDecimoTercerSueldo(str_ide_nrdtn,ide_gereg);
				String fecha_fin_dec=ser_nomina.getFechaFinalDecimoTercerSueldo(str_ide_nrdtn,ide_gereg);

				String fecha_calculo=tab_capacidad.getValor("FECHA_CALCULO_NRCAP");
				String num_cuotas=tab_capacidad.getValor("NRO_MES_NRCAP");
				String amortizacion_cada=utilitario.getVariable("p_amortizacion_cada");
				int num_dias=0;
				try {
					num_dias=pckUtilidades.CConversion.CInt(num_cuotas)*pckUtilidades.CConversion.CInt(amortizacion_cada);
				} catch (Exception e) {
					// TODO: handle exception
				}
				String fecha_fin_pagos=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_calculo), num_dias));
				System.out.println("fecha_pago dec "+fecha_pago_dec);

				if (fecha_pago_dec!=null && !fecha_pago_dec.isEmpty()
						//						&& !utilitario.isFechaMenor(utilitario.getFecha(fecha_pago_dec), utilitario.getFecha(fecha_calculo))
						&& !utilitario.isFechaMayor(utilitario.getFecha(fecha_pago_dec), utilitario.getFecha(fecha_fin_pagos))){
					tab_rubro_detalle_pago.setValor("VALOR_RUBRO_NRRDP",(ser_nomina.getDecimoTercerSueldo(ide_geedp_activo, fecha_calculo)));
					tab_rubro_detalle_pago.setValor("FECHA_PAGO_RUBRO_NRRDP",fecha_pago_dec);
					utilitario.addUpdateTabla(tab_rubro_detalle_pago, "VALOR_RUBRO_NRRDP,FECHA_PAGO_RUBRO_NRRDP,PORCENTAJE_NRRDP,VALOR_DESCUENTO_NRRDP", "");
					calcularValorDescuento1();
				}else{
					utilitario.agregarMensajeInfo("Atencion", "La fecha de pago del rubro seleccionado no coincide con ninguna fecha de pago de anticipo");
					tab_rubro_detalle_pago.setValor("VALOR_RUBRO_NRRDP","0");
					tab_rubro_detalle_pago.setValor("PORCENTAJE_NRRDP","");
					tab_rubro_detalle_pago.setValor("VALOR_DESCUENTO_NRRDP","");
					tab_rubro_detalle_pago.setValor("FECHA_PAGO_RUBRO_NRRDP","");
				}
			}else{
				tab_rubro_detalle_pago.setValor("VALOR_RUBRO_NRRDP","");
				tab_rubro_detalle_pago.setValor("PORCENTAJE_NRRDP","");
				tab_rubro_detalle_pago.setValor("VALOR_DESCUENTO_NRRDP","");
				tab_rubro_detalle_pago.setValor("FECHA_PAGO_RUBRO_NRRDP","");
			}
			utilitario.addUpdateTabla(tab_rubro_detalle_pago, "VALOR_RUBRO_NRRDP,PORCENTAJE_NRRDP,VALOR_DESCUENTO_NRRDP,FECHA_PAGO_RUBRO_NRRDP", "");

		} 
		//}
	}

	public void seleccionaRubroDetallePago(AjaxBehaviorEvent evt){
		tab_rubro_detalle_pago.modificar(evt);
		calcularRubrosDetallePagos();
		calcularCuota();
		calcularPorcentajeEndeudamiento();
		calcularLimiteCuotaMensual();
	}


	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();

		cargarTablasPantalla();
		cargarTotales();
	}

	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		cargarTablasPantalla();
		cargarTotales();
	}

	public void inicializarColumnasTablaAnticipo(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
			tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setLectura(false);
			tab_anticipo.getColumna("IDE_NRMOA").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(true);
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(true);
			tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setLectura(true);
			tab_anticipo.getColumna("IDE_NRMOA").setLectura(true);
		}
	}


	public void inicializarColumnasTablaCapacidad(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_capacidad.getColumna("FECHA_CALCULO_NRCAP").setLectura(false);
			tab_capacidad.getColumna("NRO_MES_NRCAP").setLectura(false);
			tab_capacidad.getColumna("MONTO_RECIBIR_NRCAP").setLectura(false);
			tab_capacidad.getColumna("ACTIVO_NRCAP").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_capacidad.getColumna("FECHA_CALCULO_NRCAP").setLectura(true);
			tab_capacidad.getColumna("NRO_MES_NRCAP").setLectura(true);
			tab_capacidad.getColumna("MONTO_RECIBIR_NRCAP").setLectura(true);
			tab_capacidad.getColumna("ACTIVO_NRCAP").setLectura(true);
		}
	}

	public void inicializarColumnasTablaRubroDetallePago(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_rubro_detalle_pago.getColumna("IDE_NRRUB").setLectura(false);
			tab_rubro_detalle_pago.getColumna("PORCENTAJE_NRRDP").setLectura(false);
			tab_rubro_detalle_pago.getColumna("ACTIVO_NRRDP").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_rubro_detalle_pago.getColumna("IDE_NRRUB").setLectura(true);
			tab_rubro_detalle_pago.getColumna("PORCENTAJE_NRRDP").setLectura(true);
			tab_rubro_detalle_pago.getColumna("ACTIVO_NRRDP").setLectura(true);
		}
	}

	public void inicializarColumnasTablaAnticipoInteres(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(true);
		}
	}

	public void cargarTablasPantalla(){


		tab_anticipo.setDibujo(false);
		actualizarEstadoActivoAnticipo(); //awbecerra 
		inicializarColumnasTablaAnticipo();
		tab_anticipo.setDibujo(true);

		inicializarColumnasTablaCapacidad();
		tab_capacidad.ejecutarValorForanea(tab_anticipo.getValor("IDE_NRANT"));

		String ide_nrrdp_ant=tab_rubro_detalle_pago.getValorSeleccionado();
		System.out.println("rubro detalle pago "+ide_nrrdp_ant);
		inicializarColumnasTablaRubroDetallePago();
		tab_rubro_detalle_pago.ejecutarValorForanea(tab_capacidad.getValor("IDE_NRCAP"));
		if (ide_nrrdp_ant!=null && !ide_nrrdp_ant.isEmpty()){
			tab_rubro_detalle_pago.setFilaActual(ide_nrrdp_ant);
		}


		inicializarColumnasTablaAnticipoInteres();
		tab_anticipo_interes.ejecutarValorForanea(tab_anticipo.getValor("IDE_NRANT"));
		tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValor("IDE_NRANI"));
		tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(tab_amortizacion.getSumaColumna("CUOTA_NRAMO"));
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(tab_amortizacion.getSumaColumna("PRINCIPAL_NRAMO"));


		inicializarColumnasTablaGarante();

		tab_garante.ejecutarValorForanea(tab_anticipo.getValorSeleccionado());

		tab_telefono_garante.setCondicion("ide_nrgar="+tab_garante.getValorSeleccionado());
		tab_telefono_garante.ejecutarSql();
		if (tab_capacidad.getTotalFilas()>0){
			calcularCuota();
			calcularPorcentajeEndeudamiento();
			calcularLimiteCuotaMensual();
		}



	}

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());

		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();

		cargarTablasPantalla();
		cargarTotales();

	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_anticipo.limpiar();
		tab_capacidad.limpiar();
		tab_anticipo_interes.limpiar();
		tab_amortizacion.limpiar();
		tab_precancelacion.limpiar();
		tab_garante.limpiar();
		aut_empleado.limpiar();
		eti_porcentaje.setValue("CAPACIDAD DE PAGO");
		eti_aprobado.setValue("");
		ide_geedp_activo="";
		eti_tot_rubros_pago.setValue("TOTAL DETALLE RUBROS PAGO: 0");
		tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(0);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(0);
		tex_total_ingresos.setValue("0");
		tex_total_egresos.setValue("0");
		tex_valor_a_recibir.setValue("0");
		utilitario.addUpdate("tab_tabulador:tex_valor_a_recibir");
		utilitario.addUpdate("tab_tabulador:tex_total_ingresos");
		utilitario.addUpdate("tab_tabulador:tex_total_egresos");
		utilitario.addUpdate("aut_empleado,tab_tabulador:eti_porcentaje,tab_tabulador:eti_aprobado,tab_tabulador:eti_tot_rubros_pago");// limpia y refresca el autocompletar
	}


	private boolean validarCondicionesAnticipo(){

		String num_dias_trabajados=ser_nomina.getTotalDiasAntiguedadEmp(ide_geedp_activo, utilitario.getFechaActual());
		String minimo_dias=ser_anticipo.getMinimoDiasLaboradosParaAnticipos(ide_geedp_activo);

		if (minimo_dias!=null){
			try {
				if (pckUtilidades.CConversion.CInt(num_dias_trabajados)<pckUtilidades.CConversion.CInt(minimo_dias)){
					utilitario.agregarMensajeInfo("No se puede realizar el Anticipo", "El empleado seleccionado no cumple con las condiciones de anticipos");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			String detalle_gttco=ser_gestion.getTipoContrato(ser_nomina.getEmpleadoDepartamento(ide_geedp_activo).getValor("IDE_GTTCO")).getValor("DETALLE_GTTCO");
			utilitario.agregarMensajeInfo("No se puede realizar el Anticipo, El empleado seleccionado no tiene condiciones de anticipos", "Configurar condiciones de anticipo para tipo de contrato: "+detalle_gttco);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_anticipo.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){
					for (int i = 0; i < tab_anticipo.getTotalFilas(); i++) {
						if (tab_anticipo.isFilaInsertada(i)){
							utilitario.agregarMensajeInfo("No se puede insertar", "Debe guardar el registro que esta trabajando");
							return;
						}
					}
					if (!ser_anticipo.isAnticiposActivos(ide_geedp_activo)){
						if (validarCondicionesAnticipo()){
							tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
							tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
							tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setLectura(false);
							tab_anticipo.getColumna("IDE_NRMOA").setLectura(false);
							tab_anticipo.insertar();
							tab_anticipo.setValor("IDE_GEEDP",ide_geedp_activo);
							tab_anticipo.setValor("IDE_GTEMP", aut_empleado.getValor());
							tab_anticipo.setValor("NRO_ANTICIPO_NRANT", ser_anticipo.getNumeroAnticipo(aut_empleado.getValor()));
							tab_anticipo_interes.limpiar();
							tab_amortizacion.limpiar();
							tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(tab_amortizacion.getSumaColumna("CUOTA_NRAMO"));
							tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(tab_amortizacion.getSumaColumna("PRINCIPAL_NRAMO"));

							calcularCuota();
						}else{
							con_datos_memo.getBot_aceptar().setMetodo("insertarDatosMemo");
							con_datos_memo.dibujar();
							utilitario.addUpdate("con_datos_memo");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede realizar otro anticipo", "Tiene un anticipo activo");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede solicitar un anticipo", "El empleado seleccionado no tiene un contrato activo");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if (tab_capacidad.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_anticipo.getTotalFilas()>0){
					if (tab_anticipo.getValor("MONTO_SOLICITADO_NRANT")!=null && !tab_anticipo.getValor("MONTO_SOLICITADO_NRANT").isEmpty()){
						if (tab_capacidad.getTotalFilas()==0){
							inicializarColumnasTablaCapacidad();
							tab_capacidad.insertar();
							tab_capacidad.setValor("MONTO_RECIBIR_NRCAP", tab_anticipo.getValor("MONTO_SOLICITADO_NRANT"));
							tab_capacidad.setValor("NRO_MES_NRCAP", tab_anticipo.getValor("NRO_MES_NRANT"));

							tab_anticipo.setValor("MONTO_APROBADO_NRANT",tab_anticipo.getValor("MONTO_SOLICITADO_NRANT"));
							utilitario.getConexion().agregarSqlPantalla("update NRH_ANTICIPO set MONTO_APROBADO_NRANT="+tab_anticipo.getValor("MONTO_SOLICITADO_NRANT")+" where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
							utilitario.addUpdate("tab_anticipo");

							inicializarColumnasTablaAnticipoInteres();

							inicializarColumnasTablaGarante();
							tab_anticipo_interes.limpiar();
							tab_amortizacion.limpiar();
							tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(tab_amortizacion.getSumaColumna("CUOTA_NRAMO"));
							tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(tab_amortizacion.getSumaColumna("PRINCIPAL_NRAMO"));
							calcularCuota();

						}else{
							utilitario.agregarMensajeInfo("No se puede insertar", "Ya tiene una capacidad de pago para el anticipo");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No tiene un monto solicitado para el anticipo");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un anticipo");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if (tab_rubro_detalle_pago.isFocus()){
			if (tab_anticipo.getValor("CALIFICADO_NRANT")==null
					|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty()
					|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
				if (tab_capacidad.getTotalFilas()>0){
					if (tab_capacidad.getValor("FECHA_CALCULO_NRCAP")!=null && !tab_capacidad.getValor("FECHA_CALCULO_NRCAP").isEmpty()){
						if (tab_capacidad.getValor("NRO_MES_NRCAP")!=null && !tab_capacidad.getValor("NRO_MES_NRCAP").isEmpty()){
							tab_rubro_detalle_pago.insertar();
						}else{
							utilitario.agregarMensajeInfo("No se puede insertar", "No tiene numero de meses en la capacidad de pago");				
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No tiene fecha de calculo en la capacidad de pago");				
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No tiene Capacidad de pago");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "El anticipo ya se encuentra calificado");
			}
		}
		else if (tab_garante.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_garante.getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede insertar otro garante", "Ya tiene un garante");
					return;
				}
				tab_garante.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if (tab_anticipo_interes.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_capacidad.getTotalFilas()>0){
					if (tab_anticipo_interes.getTotalFilas()==0){
						inicializarColumnasTablaAnticipoInteres();
						tab_anticipo_interes.insertar();
						tab_anticipo_interes.setValor("PLAZO_NRANI",tab_capacidad.getValor("NRO_MES_NRCAP"));
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "Ya se encuentra insertada una fila");
					}

				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No tiene capacidad de pago");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if (tab_amortizacion.isFocus()){
			if (aut_empleado.getValor()!=null){
				//tab_amortizacion.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if(tab_telefono_garante.isFocus()){
			if(tab_garante.getTotalFilas()>0){
				tab_telefono_garante.insertar();
				tab_telefono_garante.setValor("IDE_NRGAR", tab_garante.getValor("IDE_NRGAR"));
			}
		}
	}

	public boolean calcularPorcentajeEndeudamiento(){
		double tot_ingresos=0;
		double tot_egresos=0;
		double val_cuota=0;
		double porcnetaje_endeu=0;

		if (tab_capacidad.getTotalFilas()>0){

			try {
				tot_ingresos=Double.parseDouble(tab_capacidad.getValor("TOTAL_INGRESO_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				tot_egresos=Double.parseDouble(tab_capacidad.getValor("TOTAL_EGRESO_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				val_cuota=Double.parseDouble(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (tot_ingresos>0){
				porcnetaje_endeu=((tot_egresos+val_cuota)/tot_ingresos)*100;
			}
			if (tab_capacidad.getTotalFilas()>0){
				tab_capacidad.setValor("PORCENTAJE_ENDEUDA_NRCAP",utilitario.getFormatoNumero(porcnetaje_endeu,2));
			}

			if (porcnetaje_endeu<70 && porcnetaje_endeu>0){
				eti_porcentaje.setStyle("font-size: 13px;color: black;font-weight: bold");
				eti_porcentaje.setValue("DIN: NO SUPERA EL 70% DE ENDEUDAMIENTO");
				utilitario.agregarMensaje("DIN: NO SUPERA EL 70% DE ENDEUDAMIENTO", "");
				utilitario.addUpdateTabla(tab_capacidad, "PORCENTAJE_ENDEUDA_NRCAP", "tab_tabulador:eti_porcentaje");
				return true;
			}else{
				eti_porcentaje.setStyle("font-size: 13px;color: red;font-weight: bold");
				eti_porcentaje.setValue("DIN: SUPERA EL 70% DE ENDEUDAMIENTO");
				utilitario.agregarMensajeError("DIN: SUPERA EL 70% DE ENDEUDAMIENTO", "");
				utilitario.addUpdateTabla(tab_capacidad, "PORCENTAJE_ENDEUDA_NRCAP", "tab_tabulador:eti_porcentaje");
				return false;
			}
		}
		return false;
	}
	public void calcularCuota(){
		double val_anticipo=0;
		double tot_rubro_pago_anticipo=0;
		double num_meses=0;
		double val_cuota=0;
		try {
			val_anticipo=Double.parseDouble(tab_capacidad.getValor("MONTO_RECIBIR_NRCAP"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < tab_rubro_detalle_pago.getTotalFilas(); i++) {
			try {
				tot_rubro_pago_anticipo=tot_rubro_pago_anticipo+Double.parseDouble(tab_rubro_detalle_pago.getValor(i, "VALOR_DESCUENTO_NRRDP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			num_meses=Double.parseDouble(tab_capacidad.getValor("NRO_MES_NRCAP"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (num_meses!=0){
			try {
				val_cuota=(val_anticipo-tot_rubro_pago_anticipo)/num_meses;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		System.out.println("valor cuota "+val_cuota);

		BigDecimal big_cuota=new BigDecimal(val_cuota);
		big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);

		eti_tot_rubros_pago.setValue("TOTAL DETALLE RUBROS PAGO: "+utilitario.getFormatoNumero(tot_rubro_pago_anticipo,4));
		if (tab_capacidad.getTotalFilas()>0){
			tab_capacidad.setValor("CUOTA_MENSUAL_NRCAP",big_cuota+"");
			utilitario.addUpdateTabla(tab_capacidad, "CUOTA_MENSUAL_NRCAP", "tab_tabulador:eti_tot_rubros_pago,tab_tabulador:tex_plazo");
		}

	}

	public boolean validarMontoMaximoAnticipo(double monto){

		String monto_max_anticipo=ser_anticipo.getMontoMaximoPermitidoAnticipo(ide_geedp_activo);
		//String monto_solicitado=tab_anticipo.getValor("MONTO_SOLICITADO_NRANT");
		//		if (monto_solicitado==null || monto_solicitado.isEmpty()){
		//			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No ha ingresado el monto a solicitar");
		//			return false;
		//		}
		if (monto_max_anticipo==null || monto_max_anticipo.isEmpty()){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No existe un monto maximo de anticipo establecido para el empleado");
			return false;
		}
		if (Double.parseDouble(monto_max_anticipo)==0){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No existe un monto maximo de anticipo establecido para el empleado");			
			return false;
		}
		if (monto > Double.parseDouble(monto_max_anticipo)){
			utilitario.agregarMensajeError("Monto solicitado invalido", "El Monto solicitado supera al monto maximo de anticipos para el empleado, MONTO MAXIMO PERMITIDO "+monto_max_anticipo);
			//tab_anticipo.setValor("MONTO_SOLICITADO_NRANT", monto_max_anticipo+"");
			//utilitario.addUpdateTabla(tab_anticipo, "MONTO_SOLICITADO_NRANT", "");
			con_datos_memo.setMessage("EL EMPLEADO NO CUMPLE CON LAS CONDICIONES DE ANTICIPOS<br>" +
					"</br>EL MONTO SOLICITADO SOBREPASA LO PERMITIDO, MONTO MAXIMO:"+monto_max_anticipo+" <br> " +
					"</br>PERO DE TODAS MANERAS DESEA CALIFICAR EL ANTICIPO ");
			return false;
		}

		return true;
	}

	public boolean validarRubrosDetallePago(){
		for (int i = 0; i < tab_rubro_detalle_pago.getTotalFilas(); i++) {
			if (tab_rubro_detalle_pago.getValor(i, "IDE_NRRUB")==null || tab_rubro_detalle_pago.getValor(i, "IDE_NRRUB").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "La tabla rubro detalle pago tiene un rubro en blanco, La tabla no se guardara");
				return false;
			}
			if (tab_rubro_detalle_pago.getValor(i, "VALOR_DESCUENTO_NRRDP")==null || tab_rubro_detalle_pago.getValor(i, "VALOR_DESCUENTO_NRRDP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "La tabla rubro detalle pago tiene el valor a descontar blanco o nullo, La tabla no se guardara");
				return false;
			}

			if (tab_rubro_detalle_pago.getTotalFilas()>0){
				String str_fecha_pago_rubro=tab_rubro_detalle_pago.getValor("FECHA_PAGO_RUBRO_NRRDP");
				String str_fecha_calculo=tab_capacidad.getValor("FECHA_CALCULO_NRCAP");
				if (!utilitario.isFechaMayor(utilitario.getFecha(str_fecha_pago_rubro), utilitario.getFecha(str_fecha_calculo))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de pago de rubro no coincide con ninguna fecha de vencimiento");
					return false;
				}
			}

			if (Double.parseDouble(tab_rubro_detalle_pago.getValor(i, "VALOR_DESCUENTO_NRRDP"))==0){
				utilitario.agregarMensajeInfo("No se puede guardar", "La tabla rubro detalle pago tiene el valor a descontar igual a cero, La tabla no se guardara");
				return false;
			}

			if (tab_rubro_detalle_pago.getValor(i, "PORCENTAJE_NRRDP")==null || tab_rubro_detalle_pago.getValor(i, "PORCENTAJE_NRRDP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "La tabla rubro detalle pago, no tiene porcentaje");
				return false;
			}

			if (Double.parseDouble(tab_rubro_detalle_pago.getValor(i, "PORCENTAJE_NRRDP"))>100){
				utilitario.agregarMensajeInfo("No se puede guardar", "La tabla rubro detalle pago, el porcentaje no puede mayor a 100");
				return false;
			}

		}
		return true;
	}

	public boolean validarCapacidadPago(){

		if (tab_capacidad.getTotalFilas()>0){
			if (tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP")==null || tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "La cuota mensual es erronea");
				return false;
			}else if (Double.parseDouble(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"))<=0 ){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "La cuota mensual es erronea");
				return false;
			}

			if (tab_capacidad.getValor("PORCENTAJE_ENDEUDA_NRCAP")==null || tab_capacidad.getValor("PORCENTAJE_ENDEUDA_NRCAP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "El porcentaje de Endeudamiento es erroneo");
				return false;
			}else if (Double.parseDouble(tab_capacidad.getValor("PORCENTAJE_ENDEUDA_NRCAP"))<=0 ){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "El porcentaje de Endeudamiento es erroneo");
				return false;
			}

			if (tab_capacidad.getValor("CUOTA_LIMITE_NRCAP")==null || tab_capacidad.getValor("CUOTA_LIMITE_NRCAP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "La Cuota Limite es erronea");
				return false;
			}else if (Double.parseDouble(tab_capacidad.getValor("CUOTA_LIMITE_NRCAP"))<=0 ){
				utilitario.agregarMensajeInfo("No se puede guardar la capacidad de pago", "La Cuota Limite es erronea");
				return false;
			}
		}
		return true;
	}

	public boolean validarAnticipo(){
		
		if(utilitario.isFechaMayor(utilitario.getFecha(tab_anticipo.getValor("FECHA_SOLICITUD_NRANT")),utilitario.getFecha( utilitario.getFechaActual()))){
			utilitario.agregarMensajeInfo("Fecha solicitud Invalido", "La fecha de solicitud no puede ser superior a la fecha actual");
			return false;
		}
		
		if (tab_anticipo.getValor("MONTO_SOLICITADO_NRANT") == null || tab_anticipo.getValor("MONTO_SOLICITADO_NRANT").isEmpty()){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No ha ingresado el monto a solicitar");
			return false;
		}

		if (tab_anticipo.getValor("NRO_MES_NRANT") == null || tab_anticipo.getValor("NRO_MES_NRANT").isEmpty()){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No ha ingresado el numero de meses");
			return false;
		}

		return true;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if (aut_empleado.getValor()!=null){
			if (validarAnticipo()){
				if (tab_anticipo.guardar()){
					
					if (validarCapacidadPago()){
						if(tab_capacidad.guardar()){
							if (validarRubrosDetallePago()){

								tab_rubro_detalle_pago.guardar();

								if (tab_anticipo_interes.guardar()){
									if(tab_amortizacion.guardar()){

										if (tab_garante.getTotalFilas()>0){
											if (tab_garante.getValor("IDE_NRTIG").equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_garante_empl_biess"))){
												if (tab_garante.getValor("IDE_GEEDP") == null || tab_garante.getValor("IDE_GEEDP").isEmpty()){
													utilitario.agregarMensajeInfo("No se puede guardar","El tipo de garante seleccionado obliga a seleccionar un empleado del Biess como garante");
													return;
												}
											}
										}



										if (tab_garante.guardar()) {
										
											if(tab_telefono_garante.getTotalFilas()>0){
												for (int i = 0; i < tab_telefono_garante.getTotalFilas(); i++) {
													tab_telefono_garante.setValor(i,"IDE_NRGAR", tab_garante.getValor("IDE_NRGAR"));	
												}
													
											}
											
											if (tab_telefono_garante.guardar()) {
												guardarPantalla();

												String ide_nrant_anterior=tab_anticipo.getValorSeleccionado();
												tab_anticipo.setCondicion("ANTICIPO_NRANT=true AND IDE_GTEMP="+aut_empleado.getValor());
												tab_anticipo.ejecutarSql();
												tab_anticipo.setFilaActual(ide_nrant_anterior);

												cargarTablasPantalla();
												cargarTotales();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}else{
			if (tab_garante.isFocus()){

				if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
						&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
						&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede eliminar el garante", "El anticipo se encuentra anulado ");
					return;
				}
				if (tab_anticipo.getValor("APROBADO_NRANT") !=null 
						&& !tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
						&& (tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("true")
								|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("1"))){
					utilitario.agregarMensajeInfo("No se puede eliminar el garante", "El anticipo ya se encuentra aprobado");
					return;
				}

				tab_garante.eliminar();

			}
			if(tab_telefono_garante.isFocus()){
				if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
						&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
						&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede eliminar el telefono del garante", "El anticipo se encuentra anulado ");
					return;
				}
				if (tab_anticipo.getValor("APROBADO_NRANT") !=null 
						&& !tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
						&& (tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("true")
								|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("1"))){
					utilitario.agregarMensajeInfo("No se puede eliminar el telefono del garante", "El anticipo ya se encuentra aprobado");
					return;
				}

				tab_telefono_garante.eliminar();
			}
		}
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}


	@Override
	public void aceptarReporte() {
		Locale locale=new Locale("es","ES");
		if (rep_reporte.getReporteSelecionado().equals("Tabla de Amortización")){
			if (tab_anticipo.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();				
					p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
					p_parametros.put("IDE_NRANT",Long.parseLong(tab_anticipo.getValor("IDE_NRANT")));
					p_parametros.put("titulo", " TABLA DE AMORTIZACIÓN");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Solicitud Anticipo")){
			if (tab_anticipo.getTotalFilas()>0) {
				
				if(tab_anticipo.getValor("APROBADO_NRANT").equals("true")){
				
					if(tab_anticipo_interes.getTotalFilas()==0){
						utilitario.agregarMensajeInfo("No se puede continuar", "El empleado no tiene generada la Tabla de Amortización");
						return;
					}
					if(tab_amortizacion.getTotalFilas()==0){
						utilitario.agregarMensajeInfo("No se puede continuar", "El empleado no posee Tabla de Amortización");
						return;
					}
					
					if (rep_reporte.isVisible()){
						p_parametros=new HashMap();				
						rep_reporte.cerrar();				
						//		p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
						p_parametros.put("IDE_NRANT",pckUtilidades.CConversion.CInt(tab_anticipo.getValorSeleccionado()));
						p_parametros.put("ACTIVO_NRANT","1");						
						p_parametros.put("titulo", " SOLICITUD DE ANTICIPOS");
						p_parametros.put("p_firma_resp_solicitud_debito", utilitario.getVariable("p_firma_resp_solicitud_debito_sd"));
						p_parametros.put("p_coordinador_tthh",utilitario.getVariable("p_gth_coordinador_tthh"));
						p_parametros.put("p_analista_tthh",utilitario.getVariable("p_gth_analista_tthh"));
						
						// paramtros para autorizacion debito
						p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
						p_parametros.put("IDE_NRANI",Long.parseLong(tab_anticipo_interes.getValor("IDE_NRANI")));
						p_parametros.put("p_cuota_mensual",utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"),2)));
						String valores=getDebitoAnticipo(tab_anticipo.getValor("IDE_NRANT"));				
						p_parametros.put("p_valores",valores);
						
						
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						sef_reporte.dibujar();
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Usted no se encuetra aprobado");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			}
		}else if (rep_reporte.getReporteSelecionado().equals("Listado Anticipo Empleado")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo_anticipo.dibujar();
			}else if (dia_filtro_activo_anticipo.isVisible()) {
				if(lis_activo_anticipo.getSeleccionados()!=null && !lis_activo_anticipo.getSeleccionados().isEmpty()){
					set_empleado_anticipo.getTab_seleccion().setSql("select EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
							"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
							"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES ," + 
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"from NRH_ANTICIPO  ant " +
							"left join GTH_EMPLEADO emp on EMP.IDE_GTEMP=ANT.IDE_GTEMP " +
							"where ant.activo_nrant in ("+lis_activo_anticipo.getSeleccionados()+") " +
							"GROUP BY EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP , " +
							"EMP.APELLIDO_MATERNO_GTEMP , " +
							"EMP.PRIMER_NOMBRE_GTEMP , " +
							"EMP.SEGUNDO_NOMBRE_GTEMP, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"ORDER BY NOMBRES ASC, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP ASC");
					set_empleado_anticipo.getTab_seleccion().ejecutarSql();
					set_empleado_anticipo.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIV_ANTI",lis_activo_anticipo.getSeleccionados());
					dia_filtro_activo_anticipo.cerrar();
					set_empleado_anticipo.dibujar();	
				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una opción");
				}			
			}else if (set_empleado_anticipo.isVisible()) {
				if (set_empleado_anticipo.getSeleccionados()!=null && !set_empleado_anticipo.getSeleccionados().isEmpty()) {
					System.out.println("EMPLEADOS :"+set_empleado_anticipo.getSeleccionados());
					p_parametros.put("IDE_GTEMP", set_empleado_anticipo.getSeleccionados());
					p_parametros.put("titulo","LISTADO DE SOLICITUDES EMPLEADO");
					
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado_anticipo.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}

			}

		}		else if (rep_reporte.getReporteSelecionado().equals("Detalle Cuotas Anticipo Empleado")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo_anticipo.dibujar();
			}else if (dia_filtro_activo_anticipo.isVisible()) {
				if(lis_activo_anticipo.getSeleccionados()!=null && !lis_activo_anticipo.getSeleccionados().isEmpty()){
					set_empleado_anticipo.getTab_seleccion().setSql("select EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
							"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
							"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES ," + 
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"from NRH_ANTICIPO  ant " +
							"left join GTH_EMPLEADO emp on EMP.IDE_GTEMP=ANT.IDE_GTEMP " +
							"where ant.activo_nrant in ("+lis_activo_anticipo.getSeleccionados()+") " +
							"GROUP BY EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP , " +
							"EMP.APELLIDO_MATERNO_GTEMP , " +
							"EMP.PRIMER_NOMBRE_GTEMP , " +
							"EMP.SEGUNDO_NOMBRE_GTEMP, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"ORDER BY NOMBRES ASC, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP ASC");
					set_empleado_anticipo.getTab_seleccion().ejecutarSql();
					set_empleado_anticipo.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIVO_NRANT", lis_activo_anticipo.getSeleccionados());
					dia_filtro_activo_anticipo.cerrar();
					set_empleado_anticipo.dibujar();

				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una opción");
				}

			}else if (set_empleado_anticipo.isVisible()) {
				if (set_empleado_anticipo.getSeleccionados()!=null && !set_empleado_anticipo.getSeleccionados().isEmpty()) {
					System.out.println("EMPLEADOS :"+set_empleado_anticipo.getSeleccionados());
					p_parametros.put("IDE_GTEMP", set_empleado_anticipo.getSeleccionados());
					p_parametros.put("titulo","DETALLE CUOTAS ANTICIPO EMPLEADO");
					System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado_anticipo.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}
				//
			}


		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Cuotas Anticipo Empleado por Fecha")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo_anticipo.dibujar();
			}else if (dia_filtro_activo_anticipo.isVisible()) {
				if(lis_activo_anticipo.getSeleccionados()!=null && !lis_activo_anticipo.getSeleccionados().isEmpty()){
					//				
					sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIVO_NRANT", lis_activo_anticipo.getSeleccionados());
					dia_filtro_activo_anticipo.cerrar();
					sel_cal.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una opción");
				}			
			}else if (sel_cal.isVisible()) {
				if(sel_cal.isFechasValidas()){
					//					if (sel_cal.getCal_fecha1()!=null && !sel_cal.getFecha2String().isEmpty()) {
					System.out.println("EMPLEADOS :"+sel_cal.getFecha1String());
					p_parametros.put("APROBACION", sel_cal.getFecha1String());
					p_parametros.put("VENCIMIENTO", sel_cal.getFecha2String());
					p_parametros.put("titulo","DETALLE CUOTAS ANTICIPO EMPLEADO POR FECHAS");
					System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sel_cal.cerrar();
					sef_reporte.dibujar();
					//					}else {
					//						utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
					//					}
					//	
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
				}
			}			
		}

		else if (rep_reporte.getReporteSelecionado().equals("Total Anticipos Otorgados")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo_anticipo.dibujar();
			}else if (dia_filtro_activo_anticipo.isVisible()) {
				if(lis_activo_anticipo.getSeleccionados()!=null && !lis_activo_anticipo.getSeleccionados().isEmpty()){					
					p_parametros.put("ACTIVO_NRANT",lis_activo_anticipo.getSeleccionados());
					System.out.println("ACTIVOS_NRANT :"+lis_activo_anticipo.getSeleccionados());
					set_empleado_anticipo.getTab_seleccion().setSql("select EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
							"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
							"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES ," + 
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"from NRH_ANTICIPO  ant " +
							"left join GTH_EMPLEADO emp on EMP.IDE_GTEMP=ANT.IDE_GTEMP " +
							"where ant.activo_nrant in ("+lis_activo_anticipo.getSeleccionados()+") " +
							"GROUP BY EMP.IDE_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP , " +
							"EMP.APELLIDO_MATERNO_GTEMP , " +
							"EMP.PRIMER_NOMBRE_GTEMP , " +
							"EMP.SEGUNDO_NOMBRE_GTEMP, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP "+
							"ORDER BY NOMBRES ASC, " +
							"EMP.DOCUMENTO_IDENTIDAD_GTEMP ASC");
					set_empleado_anticipo.getTab_seleccion().ejecutarSql();
					set_empleado_anticipo.getBot_aceptar().setMetodo("aceptarReporte");
					dia_filtro_activo_anticipo.cerrar();				
					set_empleado_anticipo.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte","Seleccione por lo menos una opción");
				}
			}else if (set_empleado_anticipo.isVisible()) {
				if (set_empleado_anticipo.getSeleccionados()!=null && !set_empleado_anticipo.getSeleccionados().isEmpty()) {
					System.out.println("EMPLEADOS :"+set_empleado_anticipo.getSeleccionados());
					p_parametros.put("ide_gtemp", set_empleado_anticipo.getSeleccionados());
					p_parametros.put("titulo","TOTAL ANTICIPOS OTORGADOS"); 
					System.out.println("path "+rep_reporte.getPath());
					set_empleado_anticipo.cerrar();
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}
				//
			}
			//else if (rep_reporte.getReporteSelecionado().equals("Listado se Pruebas")){
			////			if (tab_anticipo.getTotalFilas()>0) {
			//				if (rep_reporte.isVisible()){
			//					p_parametros=new HashMap();				
			//					rep_reporte.cerrar();				
			////					p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
			////					p_parametros.put("IDE_NRANT",Long.parseLong(tab_anticipo.getValor("IDE_NRANT")));
			////					p_parametros.put("titulo", " TABLA DE AMORTIZACIÓN");
			//					sef_reporte.setSeleccionFormatoReporte(null, rep_reporte.getPath());						
			//					sef_reporte.dibujar();
			//				}
			//			}else{
			//				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			//			}


		}else if (rep_reporte.getReporteSelecionado().equals("Pagare a la Orden con Vencimientos Sucesivos")){
			if (tab_anticipo.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					if(tab_anticipo.getValor("APROBADO_NRANT")==null || 
							tab_anticipo.getValor("APROBADO_NRANT").isEmpty() 
							|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("false")
							|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("0")){
						utilitario.agregarMensajeInfo("No se puede Continuar","No tiene Aprobado el Anticipo");
						return;
					}
					p_parametros=new HashMap();				
					rep_reporte.cerrar();				
					p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
					p_parametros.put("IDE_NRANT",Long.parseLong(tab_anticipo.getValor("IDE_NRANT")));
					p_parametros.put("p_monto_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_anticipo.getValor("MONTO_APROBADO_NRANT"),2)));				
					p_parametros.put("titulo", "PAGARE A LA ORDEN CON VENCIMIENTOS SUCESIVOS");
					p_parametros.put("REPORT_LOCALE", locale);
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			}
		}else if (rep_reporte.getReporteSelecionado().equals("Autorizacion Debito Anticipo")){
			if (tab_anticipo.getTotalFilas()>0) {
				if(tab_anticipo_interes.getTotalFilas()==0){
					utilitario.agregarMensajeInfo("No se puede continuar", "El empleado no tiene generada la Tabla de Amortización");
					return;
				}
				if(tab_amortizacion.getTotalFilas()==0){
					utilitario.agregarMensajeInfo("No se puede continuar", "El empleado no posee Tabla de Amortización");
					return;
				}

				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();				
					p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
					p_parametros.put("IDE_NRANT",Long.parseLong(tab_anticipo.getValor("IDE_NRANT")));
					p_parametros.put("IDE_NRANI",Long.parseLong(tab_anticipo_interes.getValor("IDE_NRANI")));
					p_parametros.put("p_cuota_mensual",utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"),2)));
					String valores=getDebitoAnticipo(tab_anticipo.getValor("IDE_NRANT"));				
					p_parametros.put("p_valores",valores);
					System.out.println("Valor del IDE_GTEMP: "+Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
					System.out.println("Valor del IDE_NRANT: "+Long.parseLong(tab_anticipo.getValor("IDE_NRANT")));
					System.out.println("Valor del IDE_NRANI: "+Long.parseLong(tab_anticipo_interes.getValor("IDE_NRANI")));
					System.out.println("Valor del p_cuota_mensual: "+utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_capacidad.getValor("CUOTA_MENSUAL_NRCAP"),2))); 
					//System.out.println("Valor del p_valores: "+str_total_valores);
					//p_parametros.put("p_monto_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_anticipo.getValor("MONTO_APROBADO_NRANT"),2)));			
					p_parametros.put("REPORT_LOCALE", locale);
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			}
		}
	}

	public String getDebitoAnticipo(String ide_nrant){
		TablaGenerica tab_rubro_pagos_anticipos=new TablaGenerica();
		tab_rubro_pagos_anticipos=utilitario.consultar("SELECT * FROM NRH_RUBRO_DETALLE_PAGO a " +
				"INNER JOIN( " +
				"SELECT IDE_NRRUB AS IDE_NRRUB2,DETALLE_NRRUB FROM NRH_RUBRO " +
				")b ON b.IDE_NRRUB2=a.IDE_NRRUB " +
				"LEFT JOIN ( " +
				"SELECT IDE_NRCAP AS IDE_NRCAP2,IDE_NRANT FROM NRH_CAPACIDAD_PAGO " +
				")c ON c.IDE_NRCAP2=a.IDE_NRCAP " +
				"WHERE c.IDE_NRANT="+ide_nrant+"");
		String cad1=",y el valor de USD ";
		String cad2=" Dólares de los Estados Unidos de America en el ";
		String cadena="";

		for(int i=0;i<tab_rubro_pagos_anticipos.getTotalFilas();i++){
			cad1=",y el valor de USD ";
			cad2=" Dólares de los Estados Unidos de America en el ";
			cad1+=tab_rubro_pagos_anticipos.getValor(i, "VALOR_RUBRO_NRRDP");
			String valorLetras=utilitario.getLetrasDolarNumero(tab_rubro_pagos_anticipos.getValor(i, "VALOR_RUBRO_NRRDP"));			
			cad1+="( "+valorLetras+" )" ;
			cad2+=tab_rubro_pagos_anticipos.getValor(i, "DETALLE_NRRUB");
			cad1+=cad2;	
			cadena+=cad1;			
		}
		System.out.println(cadena);		
		return cadena; 
	}

	public Tabla getTab_anticipo() {
		return tab_anticipo;
	}

	public void setTab_anticipo(Tabla tab_anticipo) {
		this.tab_anticipo = tab_anticipo;
	}
	public Tabla getTab_capacidad() {
		return tab_capacidad;
	}

	public void setTab_capacidad(Tabla tab_capacidad) {
		this.tab_capacidad = tab_capacidad;
	}

	public Tabla getTab_amortizacion() {
		return tab_amortizacion;
	}

	public void setTab_amortizacion(Tabla tab_amortizacion) {
		this.tab_amortizacion = tab_amortizacion;
	}

	public Tabla getTab_garante() {
		return tab_garante;
	}

	public void setTab_garante(Tabla tab_garante) {
		this.tab_garante = tab_garante;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Dialogo getDia_roles_empleado() {
		return dia_roles_empleado;
	}

	public void setDia_roles_empleado(Dialogo dia_roles_empleado) {
		this.dia_roles_empleado = dia_roles_empleado;
	}

	public Tabla getTab_ingresos_egresos_emp() {
		return tab_ingresos_egresos_emp;
	}

	public void setTab_ingresos_egresos_emp(Tabla tab_ingresos_egresos_emp) {
		this.tab_ingresos_egresos_emp = tab_ingresos_egresos_emp;
	}

	public Tabla getTab_rubro_detalle_pago() {
		return tab_rubro_detalle_pago;
	}

	public void setTab_rubro_detalle_pago(Tabla tab_rubro_detalle_pago) {
		this.tab_rubro_detalle_pago = tab_rubro_detalle_pago;
	}

	public Tabla getTab_anticipo_interes() {
		return tab_anticipo_interes;
	}

	public void setTab_anticipo_interes(Tabla tab_anticipo_interes) {
		this.tab_anticipo_interes = tab_anticipo_interes;
	}

	public Confirmar getCon_datos_memo() {
		return con_datos_memo;
	}

	public void setCon_datos_memo(Confirmar con_datos_memo) {
		this.con_datos_memo = con_datos_memo;
	}

	public Dialogo getDia_ingreso_memo() {
		return dia_ingreso_memo;
	}

	public void setDia_ingreso_memo(Dialogo dia_ingreso_memo) {
		this.dia_ingreso_memo = dia_ingreso_memo;
	}

	public AutoCompletar getAut_empleado_autoriza_memo() {
		return aut_empleado_autoriza_memo;
	}

	public void setAut_empleado_autoriza_memo(
			AutoCompletar aut_empleado_autoriza_memo) {
		this.aut_empleado_autoriza_memo = aut_empleado_autoriza_memo;
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


	public Confirmar getCon_guardar() {
		return con_guardar;
	}


	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}


	public Dialogo getDia_precancelacion() {
		return dia_precancelacion;
	}


	public void setDia_precancelacion(Dialogo dia_precancelacion) {
		this.dia_precancelacion = dia_precancelacion;
	}


	public Tabla getTab_pagos_anticipos() {
		return tab_pagos_anticipos;
	}


	public void setTab_pagos_anticipos(Tabla tab_pagos_anticipos) {
		this.tab_pagos_anticipos = tab_pagos_anticipos;
	}


	public Tabla getTab_precancelacion() {
		return tab_precancelacion;
	}

	public void setTab_precancelacion(Tabla tab_precancelacion) {
		this.tab_precancelacion = tab_precancelacion;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public SeleccionTabla getSet_empleado_anticipo() {
		return set_empleado_anticipo;
	}

	public void setSet_empleado_anticipo(SeleccionTabla set_empleado_anticipo) {
		this.set_empleado_anticipo = set_empleado_anticipo;
	}

	public Tabla getTab_anticipo_abonos() {
		return tab_anticipo_abonos;
	}

	public void setTab_anticipo_abonos(Tabla tab_anticipo_abonos) {
		this.tab_anticipo_abonos = tab_anticipo_abonos;
	}

	public Dialogo getDia_abonos() {
		return dia_abonos;
	}

	public void setDia_abonos(Dialogo dia_abonos) {
		this.dia_abonos = dia_abonos;
	}


	public Dialogo getDia_filtro_activo_anticipo() {
		return dia_filtro_activo_anticipo;
	}


	public void setDia_filtro_activo_anticipo(Dialogo dia_filtro_activo_anticipo) {
		this.dia_filtro_activo_anticipo = dia_filtro_activo_anticipo;
	}


	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}


	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}


	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}


	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
	}


	public Dialogo getDia_datos_precancelacion() {
		return dia_datos_precancelacion;
	}


	public void setDia_datos_precancelacion(Dialogo dia_datos_precancelacion) {
		this.dia_datos_precancelacion = dia_datos_precancelacion;
	}

	public Tabla getTab_datos_precancelacion() {
		return tab_datos_precancelacion;
	}

	public void setTab_datos_precancelacion(Tabla tab_datos_precancelacion) {
		this.tab_datos_precancelacion = tab_datos_precancelacion;
	}

	public Consulta getCon_abonos_anticipo() {
		return con_abonos_anticipo;
	}

	public void setCon_abonos_anticipo(Consulta con_abonos_anticipo) {
		this.con_abonos_anticipo = con_abonos_anticipo;
	}

	public Upload getUpl_autoriza_memo() {
		return upl_autoriza_memo;
	}

	public void setUpl_autoriza_memo(Upload upl_autoriza_memo) {
		this.upl_autoriza_memo = upl_autoriza_memo;
	}


	public Tabla getTab_telefono_garante() {
		return tab_telefono_garante;
	}


	public void setTab_telefono_garante(Tabla tab_telefono_garante) {
		this.tab_telefono_garante = tab_telefono_garante;
	}
	public void cargarTotales() {
		// USAR PARAMETROS

		tex_total_ingresos.setValue("0");
		tex_total_egresos.setValue("0");
		tex_valor_a_recibir.setValue("0");

		try {
			
			
			TablaGenerica tab_prestamo=utilitario.consultar("SELECT  ant.ide_nrant,sum(amor.cuota_nramo) as total_pres  "
					+ "FROM nrh_anticipo ant  "
					+ "left join nrh_anticipo_interes anti on anti.ide_nrant=ant.ide_nrant  "
					+ "left join nrh_amortizacion amor on amor.ide_nrani=anti.ide_nrani  "
					+ "where ant.ide_nrant="+tab_anticipo.getValorSeleccionado()+" and ant.aprobado_nrant=true and activo_nrant=true "
					+ "group by ant.ide_nrant");
			
			
			TablaGenerica tab_ing_egr_ant=utilitario.consultar("SELECT  ant.ide_nrant,sum(amor.cuota_nramo) as total_pres  "
					+ "FROM nrh_anticipo ant "
					+ "left join nrh_anticipo_interes anti on anti.ide_nrant=ant.ide_nrant "
					+ "left join nrh_amortizacion amor on amor.ide_nrani=anti.ide_nrani "
					+ "where ant.ide_nrant="+tab_anticipo.getValorSeleccionado()+" and ant.aprobado_nrant=true and activo_nrant=true and amor.fecha_cancelado_nramo is not null and amor.ide_nrrol is not null "
					+ "group by ant.ide_nrant");
			
			
			if (tab_prestamo.getTotalFilas()> 0) {
				double dou_tot_ingresos = Double.parseDouble(tab_prestamo.getValor("total_pres"));
				tex_total_ingresos.setValue(utilitario.getFormatoNumero(dou_tot_ingresos));
				double dou_val_recibir=0;
				if (tab_ing_egr_ant.getTotalFilas()> 0) {
					dou_val_recibir = Double.parseDouble(tab_ing_egr_ant.getValor("total_pres"));
					tex_total_egresos.setValue(utilitario.getFormatoNumero(dou_val_recibir));
					
				}else {
					dou_val_recibir=0;
					tex_total_egresos.setValue(dou_val_recibir);

}
			
			
				
				
				double dou_tot_egresos = dou_tot_ingresos-dou_val_recibir;
				tex_valor_a_recibir.setValue(utilitario.getFormatoNumero(dou_tot_egresos));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		utilitario.addUpdate("tab_tabulador:tex_valor_a_recibir");
		utilitario.addUpdate("tab_tabulador:tex_total_ingresos");
		utilitario.addUpdate("tab_tabulador:tex_total_egresos");

	}
}
