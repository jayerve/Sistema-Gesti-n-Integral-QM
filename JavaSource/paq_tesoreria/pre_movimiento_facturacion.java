package paq_tesoreria;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

public class pre_movimiento_facturacion extends Pantalla{
	
	private Tabla tab_movimiento=new Tabla();
	private Tabla tab_detalle_movimiento=new Tabla();
	private Tabla tab_pre_mensual=new Tabla();   
	private Combo com_anio=new Combo();
	private Combo com_anio2=new Combo();
	private Combo com_anio_debito=new Combo();
	private Combo com_anio_credito=new Combo();

	//private Combo com_estado_factura=new Combo();
	private SeleccionTabla sel_factura_contabilidad= new SeleccionTabla();
	private SeleccionTabla sel_anticipos_contabilidad= new SeleccionTabla();
	private SeleccionTabla sel_factura_consulta = new SeleccionTabla();
	private SeleccionTabla sel_nombre_asiento_tipo = new SeleccionTabla();
	private SeleccionTabla sel_nombre_asiento = new SeleccionTabla();
	private SeleccionTabla sel_nota_debito = new SeleccionTabla();
	private SeleccionTabla sel_nota_credito = new SeleccionTabla();
	private SeleccionTabla sel_nombre_asiento_debito = new SeleccionTabla();
	private SeleccionTabla sel_nombre_asiento_credito = new SeleccionTabla();

	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_inicial_debito = new Calendario();
	private Calendario cal_fecha_inicial_credito = new Calendario();

	//private Calendario cal_fecha_final = new Calendario();
	private Dialogo dia_cabecera_asiento= new Dialogo();
	private Dialogo dia_cabecera_asiento_debito= new Dialogo();
	private Dialogo dia_cabecera_asiento_credito= new Dialogo();

	private SeleccionCalendario sec_importar=new SeleccionCalendario();
	private SeleccionCalendario sel_cal_debito=new SeleccionCalendario();
	private SeleccionCalendario sel_cal_credito=new SeleccionCalendario();
	private SeleccionCalendario sec_cal_rep=new SeleccionCalendario();
	
	private Combo com_tipo_movimiento = new Combo();
	private Combo com_mes = new Combo();
	private Texto txt_comprobante = new Texto();
	private AreaTexto txt_area_detalle_asiento = new AreaTexto();
	private Combo com_tipo_movimiento_debito = new Combo();
	private Combo com_mes_debito = new Combo();
	private Texto txt_comprobante_debito = new Texto();
	private AreaTexto txt_area_detalle_asiento_debito = new AreaTexto();
	private Combo com_tipo_movimiento_credito = new Combo();
	private Combo com_mes_credito = new Combo();
	private Texto txt_comprobante_credito = new Texto();
	private AreaTexto txt_area_detalle_asiento_credito = new AreaTexto();
	private String str_tipo_asiento="0";
	private String str_asiento_seleccionado="";
	private String str_individual="";
	private String str_grupo_material="";
	private String str_anio_anterior="";
	private String str_tipo_emision=utilitario.getVariable("p_factura_emitido");
	private String str_tipo_pagado=utilitario.getVariable("p_factura_pagado");
	private String str_tipo_abonado="30";
	//private String str_parametro_tipo_asiento="";
	private String str_seleccionados_contabilidad="";
	private String str_seleccionados_nota_debito="";
	private String str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov) ";
	private String str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)";
	private String str_lugar_aplica_debe="p_gen_lugar_aplica_debe";
	private String str_lugar_aplica_haber="p_gen_lugar_aplica_haber";
	private String str_ide_conac="";
	private String str_ide_bogrm="";
	
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioFacturacion ser_Facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
        private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
        private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
        private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	private String p_modulo_anticipos="";
	private String p_modulo_factruracion="";
	private String p_modulo_nota_debito="";
	private String p_modulo_nota_credito="";
	private String p_sec_ingresos="";
	private String empleado;
	private static String empleado_responsable;
	
	public pre_movimiento_facturacion ()
	{
		TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from now())");		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}

		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
			utilitario.agregarMensajeInfo("Permisos", "Contacte con el adminsitrador... o use el servidor de financiero...");
			return;
		}
		
		p_modulo_anticipos=utilitario.getVariable("p_modulo_anticipos");
		p_modulo_factruracion=utilitario.getVariable("p_modulo_facturacion");
		p_modulo_nota_debito=utilitario.getVariable("p_modulo_nota_debito");
		p_modulo_nota_credito=utilitario.getVariable("p_modulo_nota_credito");		
		p_sec_ingresos=utilitario.getVariable("p_modulo_ingresos");	
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		empleado_responsable=ser_contabilidad.empleadoResponsable(p_sec_ingresos,empleado);
		System.out.println("empleado_responsable"+empleado_responsable);
		if(empleado_responsable==null ||empleado_responsable.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario responsable para el registro de ingresos... ACCION NO AUTORIZADA");
			//return;
		}
		
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro de asientos de ingreso");
			return;
		}
		
		bar_botones.agregarReporte();
 		rep_reporte.setId("rep_reporte");
 		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
 		agregarComponente(rep_reporte);

 		sef_reporte.setId("sef_reporte");
 		agregarComponente(sef_reporte);
		
		com_anio2.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio2.setMetodo("seleccionaElAnio");
		com_anio2.setStyle("width: 100px; margin: 0 0 -8px 0;");
		//com_anio2.setStyle("position:absolute");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio2);

		// agrego boton contabilizar
		Boton bot_contabilizar = new Boton();
		bot_contabilizar.setValue("Contabilizar Facturas");
		bot_contabilizar.setMetodo("contabilizar");
		bar_botones.agregarBoton(bot_contabilizar);

		// agrego boton contabilizar notas de debito
		Boton bot_contabilizar_debito = new Boton();
		bot_contabilizar_debito.setValue("Contabilizar Notas Debito");
		bot_contabilizar_debito.setMetodo("contabilizarNotaDebito");
		bar_botones.agregarBoton(bot_contabilizar_debito);
		
		// agrego boton contabilizar notas de debito
		Boton bot_contabilizar_credito = new Boton();
		bot_contabilizar_credito.setValue("Contabilizar Notas Credito");
		bot_contabilizar_credito.setMetodo("contabilizarNotaCredito");
		bar_botones.agregarBoton(bot_contabilizar_credito);	
		
		// agrego boton contabilizar notas de debito
		Boton bot_contabilizar_anticipos = new Boton();
		bot_contabilizar_anticipos.setValue("Contabilizar Anticipos");
		bot_contabilizar_anticipos.setMetodo("contabilizarAnticipos");
		bar_botones.agregarBoton(bot_contabilizar_anticipos);	
		
		// agrego boton cponsultar facturas contabilizadas
		Boton bot_consulta_factura = new Boton();
		bot_consulta_factura.setValue("Ver Comprobantes");
		bot_consulta_factura.setMetodo("consultaFacturas");
		bar_botones.agregarBoton(bot_consulta_factura);
				
		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar Anticipos EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);
				
		tab_movimiento.setId("tab_movimiento");
		tab_movimiento.setHeader("MOVIMIENTOS");
		tab_movimiento.setTabla("cont_movimiento", "ide_comov", 1);
		tab_movimiento.setCampoOrden("ide_comov desc");
		tab_movimiento.getColumna("ide_cotim").setCombo("cont_tipo_movimiento", "ide_cotim", "detalle_cotim", "");
		tab_movimiento.getColumna("ide_cotia").setCombo("cont_tipo_asiento", "ide_cotia", "detalle_cotia", "");
		tab_movimiento.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_movimiento.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_movimiento.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		//tab_movimiento.getColumna("ide_gtemp").setLectura(true);
		tab_movimiento.getColumna("ide_gtemp").setAutoCompletar();
		tab_movimiento.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_movimiento.getColumna("ide_geedp").setAutoCompletar();
		tab_movimiento.getColumna("ide_geedp").setLectura(true);
		
		tab_movimiento.getColumna("ide_geani").setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		//tab_movimiento.setCondicion("ide_geani in (select ide_geani from gen_anio where activo_geani=true and bloqueado_geani=false) and ide_gemod in ("+p_modulo_factruracion+","+p_modulo_nota_credito+","+p_modulo_nota_debito+","+p_sec_ingresos+") ");
		tab_movimiento.setCondicion("ide_geani=-1 "); 
		tab_movimiento.getColumna("ide_tecpo").setLectura(true);
		tab_movimiento.getColumna("activo_comov").setValorDefecto("false");
		tab_movimiento.getColumna("ide_geare").setValorDefecto("11");
		tab_movimiento.getColumna("activo_comov").setLectura(true);
		tab_movimiento.setTipoFormulario(true);
		tab_movimiento.getGrid().setColumns(4);
		tab_movimiento.agregarRelacion(tab_detalle_movimiento);
		tab_movimiento.dibujar();
		PanelTabla pat_movimiento=new PanelTabla();
		pat_movimiento.setPanelTabla(tab_movimiento);
		
		/////detalle movinto
		tab_detalle_movimiento.setId("tab_detalle_movimiento");
		tab_detalle_movimiento.setHeader("DETALLE DE MOVIMIENTO");
		tab_detalle_movimiento.setTabla("cont_detalle_movimiento", "ide_codem", 2);
		//tab_detalle_movimiento.getColumna("ide_prcla").setCombo(ser_Presupuesto.getCatalogoPresupuestario("true,false"));
		//tab_detalle_movimiento.getColumna("ide_prcla").setAutoCompletar();
		tab_detalle_movimiento.getColumna("ide_prcla").setVisible(false);
		//tab_detalle_movimiento.getColumna("ide_prpro").setCombo("pre_programa", "ide_prpro", "cod_programa_prpro", "");
		tab_detalle_movimiento.getColumna("ide_prpro").setVisible(false);
		tab_detalle_movimiento.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_detalle_movimiento.getColumna("ide_cocac").setAutoCompletar();
		tab_detalle_movimiento.getColumna("activo_codem").setLectura(true);
		tab_detalle_movimiento.getColumna("activo_codem").setValorDefecto("true");
		tab_detalle_movimiento.getColumna("haber_codem").setMetodoChange("calcularTotal");			
		tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem");			
		tab_detalle_movimiento.getColumna("debe_codem").setMetodoChange("calcularTotal");			
		tab_detalle_movimiento.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_detalle_movimiento.getColumna("ide_gelua").setLongitud_control(20);

		tab_detalle_movimiento.getColumna("ide_tetic").setVisible(false);
        tab_detalle_movimiento.getColumna("ide_tepro").setVisible(false);
        tab_detalle_movimiento.getColumna("conciliado_codem").setVisible(false);
        tab_detalle_movimiento.getColumna("transferencia_codem").setVisible(false);
        tab_detalle_movimiento.getColumna("fecha_concilia_codem").setVisible(false);
        tab_detalle_movimiento.getColumna("documento_codem").setVisible(false);
        tab_detalle_movimiento.agregarRelacion(tab_pre_mensual);
		tab_detalle_movimiento.getGrid().setColumns(4);
		tab_detalle_movimiento.dibujar();
		PanelTabla pat_detalle_movimiento=new PanelTabla();
		pat_detalle_movimiento.setPanelTabla(tab_detalle_movimiento);
		
		tab_pre_mensual.setId("tab_pre_mensual");
        tab_pre_mensual.setHeader("EJECUCION PRESUPUESTARIA");
        tab_pre_mensual.setTabla("pre_mensual", "ide_prmen", 3);
        tab_pre_mensual.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento", "ide_prfuf","detalle_prfuf","");
        tab_pre_mensual.getColumna("ide_prfuf").setNombreVisual("FUENTE FINANCIAMIENTO");
        tab_pre_mensual.getColumna("ide_prfuf").setLongitud_control(50);
        tab_pre_mensual.getColumna("ide_pranu").setCombo(ser_Tesoreria.getCuentaPresupuestaria());
        tab_pre_mensual.getColumna("ide_pranu").setNombreVisual("CUENTA PRESUPUESTARIA");
        tab_pre_mensual.getColumna("ide_pranu").setLongitud_control(120);
        tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
        tab_pre_mensual.getColumna("ide_comov").setVisible(false);
        tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
        tab_pre_mensual.getColumna("ide_prtra").setVisible(false);
        tab_pre_mensual.getColumna("devengado_prmen").setNombreVisual("DEVENGADO");
        tab_pre_mensual.getColumna("cobrado_prmen").setNombreVisual("COBRADO");
        tab_pre_mensual.getColumna("pagado_prmen").setNombreVisual("PAGADO");
        tab_pre_mensual.getColumna("comprometido_prmen").setNombreVisual("COMPROMETIDO");
        tab_pre_mensual.getColumna("valor_anticipo_prmen").setNombreVisual("VALOR ANTICIPO");
        tab_pre_mensual.getColumna("cobradoc_prmen").setVisible(false);
        tab_pre_mensual.getColumna("pagado_prmen").setVisible(false);
        tab_pre_mensual.getColumna("certificado_prmen").setVisible(false);
        tab_pre_mensual.getColumna("ide_prcer").setVisible(false);
        tab_pre_mensual.getColumna("ide_tecpo").setVisible(false);
        //tab_pre_mensual.setCondicion("ide_codem is not null ");
        tab_pre_mensual.setCondicion("(abs(coalesce(certificado_prmen,0))+abs(coalesce(comprometido_prmen,0)))=0 "); 
        tab_pre_mensual.setColumnaSuma("devengado_prmen,cobrado_prmen,pagado_prmen,comprometido_prmen,valor_anticipo_prmen");
        tab_pre_mensual.dibujar();
        PanelTabla pat_pre_mensual= new PanelTabla();
        pat_pre_mensual.setPanelTabla(tab_pre_mensual);
		
		Division div_division =new Division();
		//div_division.dividir2(pat_movimiento, pat_detalle_movimiento, "50%", "H");
		div_division.dividir3(pat_movimiento, pat_detalle_movimiento, pat_pre_mensual, "50%", "30%", "H");
		agregarComponente(div_division);

		
		//Inicio Dialogo
		dia_cabecera_asiento.setId("dia_cabecera_asiento");
		dia_cabecera_asiento.setTitle("DATOS DEL ASIENTO CONTABLE");
		dia_cabecera_asiento.setHeight("45%");
		dia_cabecera_asiento.setWidth("40%");
		
		//Inicio Dialogo DEBITO
		dia_cabecera_asiento_debito.setId("dia_cabecera_asiento_debito");
		dia_cabecera_asiento_debito.setTitle("DATOS DEL ASIENTO CONTABLE");
		dia_cabecera_asiento_debito.setHeight("45%");
		dia_cabecera_asiento_debito.setWidth("40%");		

		//Inicio Dialogo CREDITO
		dia_cabecera_asiento_credito.setId("dia_cabecera_asiento_credito");
		dia_cabecera_asiento_credito.setTitle("DATOS DEL ASIENTO CONTABLE");
		dia_cabecera_asiento_credito.setHeight("45%");
		dia_cabecera_asiento_credito.setWidth("40%");
		
		//inicio del grid
		Grid gri_datos_asiento = new Grid();
		gri_datos_asiento.setColumns(2);
		gri_datos_asiento.getChildren().add(new Etiqueta("Año Contable: "));
		com_anio.setId("com_anio");
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		gri_datos_asiento.getChildren().add(com_anio);
		gri_datos_asiento.getChildren().add(new Etiqueta("Mes: "));
		com_mes.setId("com_mes");
		com_mes.setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		gri_datos_asiento.getChildren().add(com_mes);
		gri_datos_asiento.getChildren().add(new Etiqueta("Tipo de Movimiento: "));
		com_tipo_movimiento.setId("com_tipo_movimiento");
		com_tipo_movimiento.setCombo("select ide_cotim, detalle_cotim from cont_tipo_movimiento");
		com_tipo_movimiento.setValue(utilitario.getVariable("p_tipo_mov_facturacion"));
		gri_datos_asiento.getChildren().add(com_tipo_movimiento);
		gri_datos_asiento.getChildren().add(new Etiqueta("Fecha Movimiento: "));
		cal_fecha_inicial.setId("cal_fecha_inicial");
		cal_fecha_inicial.setFechaActual();
		gri_datos_asiento.getChildren().add(cal_fecha_inicial);
		gri_datos_asiento.getChildren().add(new Etiqueta("Nro. Comprobante"));
		txt_comprobante.setId("txt_comprobante");
		gri_datos_asiento.getChildren().add(txt_comprobante);
		gri_datos_asiento.getChildren().add(new Etiqueta("Detalle Asiento"));
		txt_area_detalle_asiento.setId("txt_area_detalle_asiento");
		txt_area_detalle_asiento.setRendered(true);
		gri_datos_asiento.getChildren().add(txt_area_detalle_asiento);
		dia_cabecera_asiento.getBot_aceptar().setMetodo("contabilizar");
		dia_cabecera_asiento.setDialogo(gri_datos_asiento);
		agregarComponente(dia_cabecera_asiento);
		
		
		//inicio del grid debito
		Grid gri_datos_asiento_debito = new Grid();
		gri_datos_asiento_debito.setColumns(2);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Año Contable: "));
		com_anio_debito.setId("com_anio_debito");
		com_anio_debito.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		gri_datos_asiento_debito.getChildren().add(com_anio_debito);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Mes: "));
		com_mes_debito.setId("com_mes_debito");
		com_mes_debito.setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		gri_datos_asiento_debito.getChildren().add(com_mes_debito);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Tipo de Movimiento: "));
		com_tipo_movimiento_debito.setId("com_tipo_movimiento_debito");
		com_tipo_movimiento_debito.setCombo("select ide_cotim, detalle_cotim from cont_tipo_movimiento");
		com_tipo_movimiento_debito.setValue(utilitario.getVariable("p_tipo_mov_facturacion"));
		gri_datos_asiento_debito.getChildren().add(com_tipo_movimiento_debito);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Fecha Movimiento: "));
		cal_fecha_inicial_debito.setId("cal_fecha_inicial_debito");
		cal_fecha_inicial_debito.setFechaActual();
		gri_datos_asiento_debito.getChildren().add(cal_fecha_inicial_debito);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Nro. Comprobante"));
		txt_comprobante_debito.setId("txt_comprobante_debito");
		gri_datos_asiento_debito.getChildren().add(txt_comprobante_debito);
		gri_datos_asiento_debito.getChildren().add(new Etiqueta("Detalle Asiento"));
		txt_area_detalle_asiento_debito.setId("txt_area_detalle_asiento_debito");
		txt_area_detalle_asiento_debito.setRendered(true);
		gri_datos_asiento_debito.getChildren().add(txt_area_detalle_asiento_debito);
		dia_cabecera_asiento_debito.getBot_aceptar().setMetodo("contabilizarNotaDebito");
		dia_cabecera_asiento_debito.setDialogo(gri_datos_asiento_debito);
		agregarComponente(dia_cabecera_asiento_debito);
		
		//inicio del grid
		Grid gri_datos_asiento_credito = new Grid();
		gri_datos_asiento_credito.setColumns(2);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Año Contable: "));
		com_anio_credito.setId("com_anio_credito");
		com_anio_credito.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		gri_datos_asiento_credito.getChildren().add(com_anio_credito);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Mes: "));
		com_mes_credito.setId("com_mes_credito");
		com_mes_credito.setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		gri_datos_asiento_credito.getChildren().add(com_mes_credito);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Tipo de Movimiento: "));
		com_tipo_movimiento_credito.setId("com_tipo_movimiento_credito");
		com_tipo_movimiento_credito.setCombo("select ide_cotim, detalle_cotim from cont_tipo_movimiento");
		com_tipo_movimiento_credito.setValue(utilitario.getVariable("p_tipo_mov_facturacion"));
		gri_datos_asiento_credito.getChildren().add(com_tipo_movimiento_credito);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Fecha Movimiento: "));
		cal_fecha_inicial_credito.setId("cal_fecha_inicial_credito");
		cal_fecha_inicial_credito.setFechaActual();
		gri_datos_asiento_credito.getChildren().add(cal_fecha_inicial_credito);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Nro. Comprobante"));
		txt_comprobante_credito.setId("txt_comprobante_credito");
		gri_datos_asiento_credito.getChildren().add(txt_comprobante_credito);
		gri_datos_asiento_credito.getChildren().add(new Etiqueta("Detalle Asiento"));
		txt_area_detalle_asiento_credito.setId("txt_area_detalle_asiento_credito");
		txt_area_detalle_asiento_credito.setRendered(true);
		gri_datos_asiento_credito.getChildren().add(txt_area_detalle_asiento_credito);
		dia_cabecera_asiento_credito.getBot_aceptar().setMetodo("contabilizarNotaCredito");
		dia_cabecera_asiento_credito.setDialogo(gri_datos_asiento_credito);
		agregarComponente(dia_cabecera_asiento_credito);
								
		//Inicializamos los seleccion tabla
		inicializaFacturasContabilidad();
		inicializaNotaDebitoContabilidad();
		inicializaNotaCreditoContabilidad();
		inicializaAnticipos();

		inicializaNombreAsientoTipo();
		inicializaNombreAsiento();
		inicializaNombreAsientoCredito();
		inicializaNombreAsientoDebito();
		inicializaCalendario();
		inicializaCalendarioCredito();
		inicializaCalendarioDebito();
		inicializaCalendarioRep();
		
		inicializaFacturasNotasContabilizadas();
		
	}
	
	public void inicializaFacturasContabilidad(){
		sel_factura_contabilidad.setId("sel_factura_contabilidad");
		sel_factura_contabilidad.setTitle("FACTURAS POR CONTABILIZAR");
		sel_factura_contabilidad.setSeleccionTabla(ser_Facturacion.getDatosFacturaContabilidad("1900-01-01", "1900-01-01","0","<0",str_tipo_asiento), "ide_fafac");
		/*sel_factura_contabilidad.getTab_seleccion().getColumna("ide_comov").setVisible(false);
		sel_factura_contabilidad.getTab_seleccion().getColumna("ide_coest").setVisible(false);
		sel_factura_contabilidad.getTab_seleccion().getColumna("ide_conac").setVisible(false);
		sel_factura_contabilidad.getTab_seleccion().getColumna("detalle_coest").setVisible(false);
		sel_factura_contabilidad.getTab_seleccion().getColumna("detalle_conac").setVisible(false);*/
		sel_factura_contabilidad.getTab_seleccion().getColumna("razon_social_recli").setLongitud_control(45);
		sel_factura_contabilidad.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		sel_factura_contabilidad.getTab_seleccion().getColumna("secuencial_fafac").setLongitud(25);
		sel_factura_contabilidad.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_factura_contabilidad.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(20);
		//sel_factura_contabilidad.getTab_seleccion().getColumna("observacion").setFiltro(true);
		sel_factura_contabilidad.getTab_seleccion().getColumna("observacion").setLongitud(30);
		sel_factura_contabilidad.getTab_seleccion().getColumna("observacion").setFiltroContenido(); 
		sel_factura_contabilidad.getTab_seleccion().getColumna("nro_comp_caja").setFiltro(true);
		sel_factura_contabilidad.getTab_seleccion().getColumna("nro_comp_caja").setLongitud(10);
		sel_factura_contabilidad.getTab_seleccion().getColumna("estado_f").setLongitud(12);
		//sel_factura_contabilidad.getTab_seleccion().setColumnaSuma("base_aprobada_fafac,valor_iva_fafac,total_fafac");
		sel_factura_contabilidad.seleccionarTodas();
		sel_factura_contabilidad.getTab_seleccion().ejecutarSql();
		sel_factura_contabilidad.getBot_aceptar().setMetodo("contabilizar");
		agregarComponente(sel_factura_contabilidad);
	}
	
	public void inicializaFacturasNotasContabilizadas(){
		sel_factura_consulta.setId("sel_factura_consulta");
		sel_factura_consulta.setTitle("FACTURAS/NOTAS DEBITO CONTABILIZADAS/RECAUDADAS");
		sel_factura_consulta.setFooter("Nota.- una vez que de clic en el boton QUITAR, necesitará ajustar el asiento de manera manual los saldos. No se olvide de re-contabilizar los registros quitados.");
		sel_factura_consulta.setSeleccionTabla(ser_Facturacion.getFacturasNotasContabilizadas("-1"), "codigo");
		sel_factura_consulta.getTab_seleccion().getColumna("comprobante").setLongitud(20);
		sel_factura_consulta.getTab_seleccion().getColumna("secuencial").setLongitud(25);
		sel_factura_consulta.getTab_seleccion().getColumna("nro_comprobante_cobro").setLongitud(50);
		sel_factura_consulta.getTab_seleccion().getColumna("secuencial").setFiltro(true);
		sel_factura_consulta.getTab_seleccion().getColumna("comprobante").setFiltro(true);
		sel_factura_consulta.getTab_seleccion().getColumna("observacion").setFiltro(true);
		sel_factura_consulta.getTab_seleccion().getColumna("nro_comprobante_cobro").setFiltro(true);
		sel_factura_consulta.getTab_seleccion().setColumnaSuma("subtotal,iva,valor_contabilizado");
		sel_factura_consulta.seleccionarTodas();
		sel_factura_consulta.getTab_seleccion().ejecutarSql();
		//sel_factura_consulta.getBot_aceptar().setRendered(false);
		sel_factura_consulta.getBot_aceptar().setMetodo("quitar");
		sel_factura_consulta.getBot_aceptar().setValue("Quitar");
		sel_factura_consulta.getBot_aceptar().setIcon("ui-icon-unlocked");
		agregarComponente(sel_factura_consulta);
	}
	
	public void inicializaNotaDebitoContabilidad(){
		sel_nota_debito.setId("sel_nota_debito");
		sel_nota_debito.setTitle("NOTAS DE DEBITO POR CONTABILIZAR");
		sel_nota_debito.setSeleccionTabla(ser_Facturacion.getDatosNotaDebitoContabilidad("1", "1900-01-01", "1900-01-01",str_tipo_asiento), "ide_fanod");
		sel_nota_debito.getTab_seleccion().getColumna("ide_comov").setVisible(false);
		sel_nota_debito.getTab_seleccion().getColumna("ide_coest").setVisible(false);
		sel_nota_debito.getTab_seleccion().getColumna("ide_conac").setVisible(false);
		sel_nota_debito.getTab_seleccion().getColumna("detalle_coest").setVisible(false);
		sel_nota_debito.getTab_seleccion().getColumna("detalle_conac").setVisible(false);
		sel_nota_debito.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_nota_debito.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		sel_nota_debito.getTab_seleccion().getColumna("observacion").setFiltro(true);
		sel_nota_debito.getTab_seleccion().getColumna("nro_comp_caja").setFiltro(true);
		sel_nota_debito.seleccionarTodas();
		sel_nota_debito.getTab_seleccion().ejecutarSql();
		sel_nota_debito.getBot_aceptar().setMetodo("contabilizarNotaDebito");
		agregarComponente(sel_nota_debito);
	}
	
	public void inicializaNotaCreditoContabilidad(){
		sel_nota_credito.setId("sel_nota_credito");
		sel_nota_credito.setTitle("NOTAS DE CREDITO POR CONTABILIZAR");
		sel_nota_credito.setSeleccionTabla(ser_Facturacion.getDatosNotaCreditoContabilidad("1900-01-01", "1900-01-01",str_tipo_asiento,"0"), "ide_fafac");
		sel_nota_credito.getTab_seleccion().getColumna("ide_comov").setVisible(false);
		sel_nota_credito.getTab_seleccion().getColumna("ide_coest").setVisible(false);
		sel_nota_credito.getTab_seleccion().getColumna("ide_conac").setVisible(false);
		sel_nota_credito.getTab_seleccion().getColumna("detalle_coest").setVisible(false);
		sel_nota_credito.getTab_seleccion().getColumna("detalle_conac").setVisible(false);
		sel_nota_credito.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_nota_credito.seleccionarTodas();
		sel_nota_credito.getTab_seleccion().ejecutarSql();
		sel_nota_credito.getBot_aceptar().setMetodo("contabilizarNotaCredito");
		agregarComponente(sel_nota_credito);
	}
	
	public void inicializaAnticipos(){
		sel_anticipos_contabilidad.setId("sel_anticipos_contabilidad");
		sel_anticipos_contabilidad.setTitle("ANTICIPOS POR CONTABILIZAR");
		sel_anticipos_contabilidad.setSeleccionTabla(ser_Tesoreria.getActiciposRec("-1","1900-01-01", "1900-01-01"), "ide_facob");
		sel_anticipos_contabilidad.getTab_seleccion().getColumna("secuencial").setFiltro(true);
		sel_anticipos_contabilidad.getTab_seleccion().getColumna("num_recibo_caja").setFiltro(true);
		sel_anticipos_contabilidad.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_anticipos_contabilidad.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		sel_anticipos_contabilidad.seleccionarTodas();
		sel_anticipos_contabilidad.getTab_seleccion().ejecutarSql();
		sel_anticipos_contabilidad.getBot_aceptar().setMetodo("contabilizarAnticipos");
		agregarComponente(sel_anticipos_contabilidad);
	}
	
	public void inicializaNombreAsientoTipo(){
		sel_nombre_asiento_tipo.setId("sel_nombre_asiento_tipo");
		sel_nombre_asiento_tipo.setTitle("Seleccione el asiento tipo");
		sel_nombre_asiento_tipo.setSeleccionTabla(ser_contabilidad.getListaNombreAsientoTipo(), "ide_conac");
		sel_nombre_asiento_tipo.setRadio();
		sel_nombre_asiento_tipo.getTab_seleccion().ejecutarSql();
		sel_nombre_asiento_tipo.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_nombre_asiento_tipo);
	}
	
	public void inicializaNombreAsiento(){
		sel_nombre_asiento.setId("sel_nombre_asiento");
		sel_nombre_asiento.setTitle("Seleccione el tipo de asiento que desea contabilizar");
		sel_nombre_asiento.setSeleccionTabla(ser_contabilidad.getNombreAsientoContable("-1", "true"), "ide_conac");
		sel_nombre_asiento.getTab_seleccion().getColumna("ide_gemod").setVisible(false);
		sel_nombre_asiento.getTab_seleccion().getColumna("ide_coest").setVisible(false);
		sel_nombre_asiento.getTab_seleccion().getColumna("descripcion_asiento").setFiltroContenido();
		sel_nombre_asiento.setRadio();
		sel_nombre_asiento.getTab_seleccion().ejecutarSql();
		sel_nombre_asiento.getBot_aceptar().setMetodo("contabilizar");
		agregarComponente(sel_nombre_asiento);
	}
	
	public void inicializaNombreAsientoDebito(){
		sel_nombre_asiento_debito.setId("sel_nombre_asiento_debito");
		sel_nombre_asiento_debito.setTitle("Seleccione el tipo de asiento que desea contabilizar");
		sel_nombre_asiento_debito.setSeleccionTabla(ser_contabilidad.getNombreAsientoContable("-1", "true"), "ide_conac");
		sel_nombre_asiento_debito.setRadio();
		sel_nombre_asiento_debito.getTab_seleccion().ejecutarSql();
		sel_nombre_asiento_debito.getBot_aceptar().setMetodo("contabilizarNotaDebito");
		agregarComponente(sel_nombre_asiento_debito);
	}
	public void inicializaNombreAsientoCredito(){
		sel_nombre_asiento_credito.setId("sel_nombre_asiento_credito");
		sel_nombre_asiento_credito.setTitle("Seleccione el tipo de asiento que desea contabilizar");
		sel_nombre_asiento_credito.setSeleccionTabla(ser_contabilidad.getNombreAsientoContable("-1", "true"), "ide_conac");
		sel_nombre_asiento_credito.setRadio();
		sel_nombre_asiento_credito.getTab_seleccion().ejecutarSql();
		sel_nombre_asiento_credito.getBot_aceptar().setMetodo("contabilizarNotaCredito");
		agregarComponente(sel_nombre_asiento_credito);
	}
	
	public void inicializaCalendario(){
		sec_importar.setTitle("SELECCION DE FECHAS");
		sec_importar.setFooter("Seleccione un Rango de fechas para Buscar Facturas y Contabilizar");
		sec_importar.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.getBot_aceptar().setMetodo("contabilizar");
		agregarComponente(sec_importar);
	}
	public void inicializaCalendarioDebito(){
		sel_cal_debito.setTitle("SELECCION DE FECHAS");
		sel_cal_debito.setFooter("Seleccione un Rango de fechas para Buscar Notas de Debito y Contabilizar");
		sel_cal_debito.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal_debito.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal_debito.getBot_aceptar().setMetodo("contabilizarNotaDebito");
		agregarComponente(sel_cal_debito);
	}
	public void inicializaCalendarioCredito(){
		sel_cal_credito.setTitle("SELECCION DE FECHAS");
		sel_cal_credito.setFooter("Seleccione un Rango de fechas para Buscar Notas de Credito y Contabilizar");
		sel_cal_credito.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal_credito.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal_credito.getBot_aceptar().setMetodo("contabilizarNotaCredito");
		agregarComponente(sel_cal_credito);
	}
	public void inicializaCalendarioRep(){
		sec_cal_rep.setTitle("SELECCION DE FECHAS");
		sec_cal_rep.setFooter("Seleccione un Rango de fechas");
		sec_cal_rep.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_cal_rep.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_cal_rep.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sec_cal_rep);
	}
	public void consultaFacturas()
	{
		sel_factura_consulta.getTab_seleccion().setSql(ser_Facturacion.getFacturasNotasContabilizadas(tab_movimiento.getValor("ide_comov") ));
		sel_factura_consulta.getTab_seleccion().ejecutarSql();
		sel_factura_consulta.dibujar();
	}
	
	public void contabilizar(){

		
		if(sel_nombre_asiento.isVisible()){
			if(sel_nombre_asiento.getValorSeleccionado()!=null){
			str_asiento_seleccionado=sel_nombre_asiento.getValorSeleccionado();	
			TablaGenerica tab_asiento_seleccionado= utilitario.consultar("select ide_conac,ide_gemod,detalle_conac,individual_conac,ide_coest,ide_bogrm,anio_anterior_conac from cont_nombre_asiento_contable where ide_conac ="+str_asiento_seleccionado);
			str_tipo_asiento = tab_asiento_seleccionado.getValor("ide_coest");
			str_individual = tab_asiento_seleccionado.getValor("individual_conac");
			str_ide_conac =tab_asiento_seleccionado.getValor("ide_conac");
			str_grupo_material =tab_asiento_seleccionado.getValor("ide_bogrm");
			str_anio_anterior =tab_asiento_seleccionado.getValor("anio_anterior_conac");

			sel_nombre_asiento.cerrar();
			sec_importar.getBot_aceptar().setMetodo("contabilizar");
			sec_importar.dibujar();
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		
		else if (sec_importar.isVisible()){
			
			sec_importar.cerrar();
			// Indico que el asiento es de tipo emision
			/*if(str_tipo_asiento.equals(str_tipo_emision))
			{	
				str_parametro_tipo_asiento="1";
			}*/
			// Indico que el asiento es de tipo recaudacion str_tipo_abonado
			if(str_tipo_asiento.equals(str_tipo_pagado) || str_tipo_asiento.equals(str_tipo_abonado) )
			{	
				
				//obtenemos el anio para indicar si vamos a contabilizar anios vigentes o anios anteriores
				TablaGenerica anio_vigente=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+utilitario.getVariable("p_anio_vigente"));
				if(str_anio_anterior.equals("true")){
				  str_anio_anterior=" < "+anio_vigente.getValor("detalle_geani");	
				}
				else {
					str_anio_anterior=" >= "+anio_vigente.getValor("detalle_geani");		
				}
				
				//str_parametro_tipo_asiento="2";
			}
			
			sel_factura_contabilidad.setTitle("FACTURAS POR CONTABILIZAR DEL "+sec_importar.getFecha1String()+" AL "+sec_importar.getFecha2String());
			sel_factura_contabilidad.setSql(ser_Facturacion.getDatosFacturaContabilidad(//str_parametro_tipo_asiento, 
					sec_importar.getFecha1String(), sec_importar.getFecha2String(),//str_tipo_asiento,
					str_grupo_material,str_anio_anterior,str_tipo_asiento));
			//sel_factura_contabilidad.getTab_seleccion().getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
			sel_factura_contabilidad.getTab_seleccion().ejecutarSql();
			sel_factura_contabilidad.dibujar();
		}
		else if(sel_factura_contabilidad.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_factura_contabilidad.getSeleccionados() !=null){
				str_seleccionados_contabilidad=sel_factura_contabilidad.getSeleccionados();
				sel_factura_contabilidad.cerrar();
				txt_comprobante.setValue(ser_contabilidad.numeroSecuencial(p_sec_ingresos));
				com_anio.setValue(com_anio2.getValue());
				com_mes.setValue(utilitario.getMes(cal_fecha_inicial.getFecha()+""));
				
				if(str_tipo_asiento.equals("2") )
					com_tipo_movimiento.setValue("2");
				else
					com_tipo_movimiento.setValue("1");
				
				dia_cabecera_asiento.getBot_aceptar().setMetodo("contabilizar");
				dia_cabecera_asiento.dibujar();
				
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else if (dia_cabecera_asiento.isVisible()){

			dia_cabecera_asiento.cerrar();
			if(validar_movimiento_bloqueado(cal_fecha_inicial.getFecha())){
				utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
				return;
			}
			// Indicamos si el asiento contable se va a generar indivual o agrupado, por ejemplo: individual=true, grupal=false
			/*if(str_individual.equals("true")){
				System.out.println("entre al true de individual ");
				// Tabla genreica si es asiento contable individual consulto factura por factutra
				TablaGenerica tab_factura= utilitario.consultar("select ide_fafac,ide_fadaf from fac_factura where ide_fafac in ("+str_seleccionados_contabilidad+")");
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// Consulto codigo maximo de la cabecera del asiento contle
					TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
					String maximo_cont_movimiento=tab_maximo.getValor("codigo");
					// Inserto en la cabecera del asiento contable
					//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
					str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov) ";
					str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento.getValue()+","+com_mes.getValue()+","+str_asiento_seleccionado+",5,"+com_anio.getValue()+","+p_modulo_factruracion+",'"+cal_fecha_inicial.getFecha()+"','"+txt_area_detalle_asiento.getValue()+"','"+txt_comprobante.getValue()+"',true)";
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
					System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

					// INSERTO EN LA TABLA DEL ASIENTO CONTABLE DE FACTURACION
					// Consulto codigo maximo de la cabecera del asiento factura
					TablaGenerica tab_maximo_factura =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_factura_asiento", "ide_cofaa"));
					String maximo_cont_factura=tab_maximo_factura.getValor("codigo");

					String str_inserta_asiento_factura="insert into cont_factura_asiento (ide_cofaa,ide_coest,ide_comov,ide_fafac,ide_conac,detalle_cofaa,activo_cofaa)"
							+" values ("+maximo_cont_factura+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fafac")+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial.getFecha()+" "+txt_area_detalle_asiento.getValue()+"',true  )";
					utilitario.getConexion().ejecutarSql(str_inserta_asiento_factura);
					
					// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
					TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_debe,str_ide_conac,"0"));
					for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "valor_asiento")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
					
					// Tabla generica devuelve los asientos para insertar en contabilidad al haber
					TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_haber,str_ide_conac,"0"));
					for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "valor_asiento")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
				}
				
			}
			
			/// contabilizo si es asiento grupal
			else{
				
			*/
				System.out.println("ASIENTO GRUPAL");
				// Consulto codigo maximo de la cabecera del asiento contle
				TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
				String maximo_cont_movimiento=tab_maximo.getValor("codigo");
				// Inserto en la cabecera del asiento contable
				//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
				str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
				str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento.getValue()+","+com_mes.getValue()+","+str_asiento_seleccionado+",5,"+com_anio.getValue()+","+p_modulo_factruracion+",'"+cal_fecha_inicial.getFecha()+"','"
													+txt_area_detalle_asiento.getValue()+"','"+txt_comprobante.getValue()+"',false,now(),now(),"+empleado+")";
				utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
				System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

				TablaGenerica tab_factura= utilitario.consultar("select fac.ide_fafac,fac.ide_fadaf,coalesce(fc.ide_facob,0) as ide_facob from fac_factura fac "
						+ " left join fac_cobro fc on fc.ide_fafac = fac.ide_fafac and fc.ide_facob not in (select ide_facob from cont_factura_asiento where ide_facob>0) and fc.fecha_cobro_facob between '"+sec_importar.getFecha1String()+"' and '"+sec_importar.getFecha2String()+"' "
						+ " where fac.ide_fafac in ("+str_seleccionados_contabilidad+")");
				
				System.out.println("tab_factura.imprimirSql(): "); tab_factura.imprimirSql();
				StringBuilder sel_ide_facob=new StringBuilder();
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// INSERTO EN LA TABLA DEL ASIENTO CONTABLE DE FACTURACION
					// Consulto codigo maximo de la cabecera del asiento factura
					TablaGenerica tab_maximo_factura =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_factura_asiento", "ide_cofaa"));
					String maximo_cont_factura=tab_maximo_factura.getValor("codigo");

					String str_inserta_asiento_factura="insert into cont_factura_asiento (ide_cofaa,ide_coest,ide_comov,ide_fafac,ide_facob,ide_conac,detalle_cofaa,activo_cofaa)"
							+" values ("+maximo_cont_factura+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fafac")+","+(str_tipo_asiento.equals(str_tipo_emision)?"0":tab_factura.getValor(i,"ide_facob"))+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial.getFecha()+" "+txt_area_detalle_asiento.getValue()+"',true  )";
					//System.out.println("insert into cont_factura_asiento "+str_inserta_asiento_factura);
					utilitario.getConexion().ejecutarSql(str_inserta_asiento_factura);
					
					if(sel_ide_facob.toString().isEmpty()==false){
						sel_ide_facob.append(",");
	                }
					sel_ide_facob.append(tab_factura.getValor(i,"ide_facob"));

				}
				System.out.println("sel_ide_facob1: "+sel_ide_facob);
				if(str_tipo_asiento.equals(str_tipo_emision))
					sel_ide_facob.append("0");
				System.out.println("sel_ide_facob2: "+sel_ide_facob);
				// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
				TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_debe,str_ide_conac,"1",str_tipo_asiento,sel_ide_facob.toString()));
				for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){		
					
					
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "valor_asiento")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
					//System.out.println("insert into str_insert_contabilidad_detalle DEBE "+str_insert_contabilidad_detalle);
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
				}
				
				// Tabla generica devuelve los asientos para insertar en contabilidad al haber
				TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_haber,str_ide_conac,"1",str_tipo_asiento,sel_ide_facob.toString()));
				for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "valor_asiento")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
					//System.out.println("insert into str_insert_contabilidad_detalle HABER "+str_insert_contabilidad_detalle);
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
				}

				
			//}

			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado()); 
			utilitario.addUpdate("tab_movimiento");
			///guardo secuencial del comprobante de ingresos
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(p_sec_ingresos), p_sec_ingresos);
			System.out.println("contabilizar ok: ");
		}
		else {
			sel_nombre_asiento.getTab_seleccion().setSql(ser_contabilidad.getNombreAsientoContable(p_modulo_factruracion, "true"));
			sel_nombre_asiento.getTab_seleccion().ejecutarSql();
			sel_nombre_asiento.getBot_aceptar().setMetodo("contabilizar");
			sel_nombre_asiento.dibujar();
		}
		
	}
	
	public void contabilizarNotaDebito (){
		if(sel_nombre_asiento_debito.isVisible()){
			if(sel_nombre_asiento_debito.getValorSeleccionado()!=null){
				str_asiento_seleccionado=sel_nombre_asiento_debito.getValorSeleccionado();	
				TablaGenerica tab_asiento_seleccionado= utilitario.consultar("select ide_conac,ide_gemod,detalle_conac,individual_conac,ide_coest,ide_bogrm from cont_nombre_asiento_contable where ide_conac ="+str_asiento_seleccionado);
				str_tipo_asiento = tab_asiento_seleccionado.getValor("ide_coest");
				str_individual = tab_asiento_seleccionado.getValor("individual_conac");
				str_ide_conac =tab_asiento_seleccionado.getValor("ide_conac");
				str_ide_bogrm =tab_asiento_seleccionado.getValor("ide_bogrm");
				sel_nombre_asiento_debito.cerrar();		
				sel_cal_debito.dibujar();
				}
				else {
					utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
				}
		}
		else if (sel_cal_debito.isVisible()){
			
			sel_cal_debito.cerrar();
			// Indico que el asiento es de tipo emision
			/*if(str_tipo_asiento.equals(str_tipo_emision))
			{	
				str_parametro_tipo_asiento="1";
			}
			// Indico que el asiento es de tipo recaudacion
			if(str_tipo_asiento.equals(str_tipo_pagado))
			{	
				str_parametro_tipo_asiento="2";
			}	*/		
			sel_nota_debito.setTitle("NOTAS DE DEBITO POR CONTABILIZAR DEL "+sel_cal_debito.getFecha1String()+" AL "+sel_cal_debito.getFecha2String());
			sel_nota_debito.setSql(ser_Facturacion.getDatosNotaDebitoContabilidad(str_tipo_asiento, sel_cal_debito.getFecha1String(), sel_cal_debito.getFecha2String(),str_ide_bogrm));
			sel_nota_debito.getTab_seleccion().getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
			sel_nota_debito.getTab_seleccion().ejecutarSql();
			sel_nota_debito.dibujar();
		}
		else if(sel_nota_debito.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_nota_debito.getSeleccionados() !=null){
				str_seleccionados_nota_debito=sel_nota_debito.getSeleccionados();
				sel_nota_debito.cerrar();
				txt_comprobante_debito.setValue(ser_contabilidad.numeroSecuencial(p_sec_ingresos));
				com_anio_debito.setValue(com_anio2.getValue());
				com_mes_debito.setValue(utilitario.getMes(cal_fecha_inicial_debito.getFecha()+""));
				if(str_tipo_asiento.equals("2") )
					com_tipo_movimiento_debito.setValue("2");
				else
					com_tipo_movimiento_debito.setValue("1");
				
				dia_cabecera_asiento_debito.dibujar();

			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else if (dia_cabecera_asiento_debito.isVisible()){

			dia_cabecera_asiento_debito.cerrar();
			if(validar_movimiento_bloqueado(cal_fecha_inicial_debito.getFecha())){
				utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
				return;
			}
				// Indicamos si el asiento contable se va a generar indivual o agrupado, por ejemplo: individual=true, grupal=false
			if(str_individual.equals("true")){
				System.out.println("entre al true de individual ");
				// Tabla genreica si es asiento contable individual consulto factura por factutra
				//TablaGenerica tab_factura= utilitario.consultar("select ide_fafac,ide_fanod from fac_detalle_debito where ide_fafac in ("+str_seleccionados_contabilidad+")");
				TablaGenerica tab_factura= utilitario.consultar("select ide_fafac,ide_fanod from fac_detalle_debito where ide_fanod in ("+str_seleccionados_nota_debito+")");
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// Consulto codigo maximo de la cabecera del asiento contle
					TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
					String maximo_cont_movimiento=tab_maximo.getValor("codigo");
					// Inserto en la cabecera del asiento contable
					//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
					str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
					str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento_debito.getValue()+","+com_mes_debito.getValue()+","+str_asiento_seleccionado+",5,"+com_anio_debito.getValue()+","+p_modulo_nota_debito+",'"
														+cal_fecha_inicial_debito.getFecha()+"','"+txt_area_detalle_asiento_debito.getValue()+"','"+txt_comprobante_debito.getValue()+"',false,now(),now(),"+empleado+")";
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
					System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

					// INSERTO EN LA TABLA DEL ASIENTO CONTABLE DE FACTURACION
					// Consulto codigo maximo de la cabecera del asiento nota debito 
					TablaGenerica tab_maximo_nota_debito =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_nota_debito_asiento", "ide_conda"));
					String maximo_cont_nota_debito=tab_maximo_nota_debito.getValor("codigo");

					String str_inserta_nota_debito="insert into cont_nota_debito_asiento (ide_conda,ide_coest,ide_comov,ide_fanod,ide_conac,detalle_conda,activo_conda)"
							+" values ("+maximo_cont_nota_debito+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fanod")+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante_debito.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial_debito.getFecha()+" "+txt_area_detalle_asiento_debito.getValue()+"',true  )";
					utilitario.getConexion().ejecutarSql(str_inserta_nota_debito);
					
					// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
					TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_debe,str_ide_conac,"0"));
					for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "interes")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
					
					// Tabla generica devuelve los asientos para insertar en contabilidad al haber
					TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_haber,str_ide_conac,"0"));
					for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "interes")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
				}
				
			}
			
			/// contabilizo si es asiento grupal
			else{
				System.out.println("contabiliza notas debito grupal ");
				// Consulto codigo maximo de la cabecera del asiento contle
				TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
				String maximo_cont_movimiento=tab_maximo.getValor("codigo");
				// Inserto en la cabecera del asiento contable
				//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
				str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
				str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento_debito.getValue()+","+com_mes_debito.getValue()+","+str_asiento_seleccionado+",5,"+com_anio_debito.getValue()+","+p_modulo_nota_debito+",'"
													+cal_fecha_inicial_debito.getFecha()+"','"+txt_area_detalle_asiento_debito.getValue()+"','"+txt_comprobante_debito.getValue()+"',false,now(),now(),"+empleado+")";
				utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
				System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

				//String sqlDetDedito="select ide_fafac,ide_fanod from fac_detalle_debito where ide_fafac in ("+str_seleccionados_contabilidad+")"; 
				String sqlDetDedito="select ide_fafac,ide_fanod from fac_detalle_debito where ide_fanod in ("+str_seleccionados_nota_debito+")"; 
				TablaGenerica tab_factura= utilitario.consultar(sqlDetDedito);
				System.out.println("tab_factura sql "+sqlDetDedito);
				System.out.println("tab_factura tab_factura.getTotalFilas() "+tab_factura.getTotalFilas());
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// Consulto codigo maximo de la cabecera del asiento nota debito 
					TablaGenerica tab_maximo_nota_debito =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_nota_debito_asiento", "ide_conda"));
					String maximo_cont_nota_debito=tab_maximo_nota_debito.getValor("codigo");

					String str_inserta_nota_debito="insert into cont_nota_debito_asiento (ide_conda,ide_coest,ide_comov,ide_fanod,ide_conac,detalle_conda,activo_conda)"
							+" values ("+maximo_cont_nota_debito+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fanod")+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante_debito.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial_debito.getFecha()+" "+txt_area_detalle_asiento_debito.getValue()+"',true  )";
					//System.out.println("insert into cont_nota_debito_asiento "+str_inserta_nota_debito);
					utilitario.getConexion().ejecutarSql(str_inserta_nota_debito);
					
				}
				// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe +
				//TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(str_seleccionados_contabilidad,str_lugar_aplica_debe,str_ide_conac,"1"));
				TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(str_seleccionados_nota_debito,str_lugar_aplica_debe,str_ide_conac,"1"));
				for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){		
					
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "interes")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
					//System.out.println("insert into cont_detalle_movimiento DEBE "+str_insert_contabilidad_detalle);
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					
				}
				
				// Tabla generica devuelve los asientos para insertar en contabilidad al haber
				//TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(str_seleccionados_contabilidad,str_lugar_aplica_haber,str_ide_conac,"1"));
				TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidadNotaDebito(str_seleccionados_nota_debito,str_lugar_aplica_haber,str_ide_conac,"1"));
				for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "interes")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					//System.out.println("insert into cont_detalle_movimiento HABER "+str_insert_contabilidad_detalle);
				}

				
			}
			
			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado());
			utilitario.addUpdate("tab_movimiento");
			///guardo secuencial del comprobante de ingresos
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(p_sec_ingresos), p_sec_ingresos);

		}

		else {
			sel_nombre_asiento_debito.getTab_seleccion().setSql(ser_contabilidad.getNombreAsientoContable(p_modulo_nota_debito, "true"));
			sel_nombre_asiento_debito.getTab_seleccion().ejecutarSql();
			sel_nombre_asiento_debito.dibujar();
		}

	}
	public void contabilizarNotaCredito (){
		if(sel_nombre_asiento_credito.isVisible()){
			if(sel_nombre_asiento_credito.getValorSeleccionado()!=null){
				str_asiento_seleccionado=sel_nombre_asiento_credito.getValorSeleccionado();	
				TablaGenerica tab_asiento_seleccionado= utilitario.consultar("select ide_conac,ide_gemod,detalle_conac,individual_conac,ide_coest,ide_bogrm from cont_nombre_asiento_contable where ide_conac ="+str_asiento_seleccionado);
				str_tipo_asiento = tab_asiento_seleccionado.getValor("ide_coest");
				str_individual = tab_asiento_seleccionado.getValor("individual_conac");
				str_ide_conac =tab_asiento_seleccionado.getValor("ide_conac");
				str_grupo_material =tab_asiento_seleccionado.getValor("ide_bogrm");

				sel_nombre_asiento_credito.cerrar();		
				sel_cal_credito.dibujar();
				}
				else {
					utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
				}
			
		}
		else if (sel_cal_credito.isVisible()){
			
			sel_cal_credito.cerrar();
			// Indico que el asiento es de tipo emision
			/*if(str_tipo_asiento.equals(str_tipo_emision))
			{	
				str_parametro_tipo_asiento="1";
			}
			// Indico que el asiento es de tipo recaudacion
			if(str_tipo_asiento.equals(str_tipo_pagado))
			{	
				str_parametro_tipo_asiento="2";
			}	*/		
			sel_nota_credito.setTitle("NOTAS DE CREDITO POR CONTABILIZAR DEL "+sel_cal_credito.getFecha1String()+" AL "+sel_cal_credito.getFecha2String());
			sel_nota_credito.setSql(ser_Facturacion.getDatosNotaCreditoContabilidad(sel_cal_credito.getFecha1String(), sel_cal_credito.getFecha2String(),str_tipo_asiento,str_grupo_material));
			sel_nota_credito.getTab_seleccion().getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
			sel_nota_credito.getTab_seleccion().ejecutarSql();
			sel_nota_credito.dibujar();
		}
		else if(sel_nota_credito.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_nota_credito.getSeleccionados() !=null){
				str_seleccionados_contabilidad=sel_nota_credito.getSeleccionados();
				sel_nota_credito.cerrar();
				txt_comprobante_credito.setValue(ser_contabilidad.numeroSecuencial(p_sec_ingresos));
				dia_cabecera_asiento_credito.dibujar();
				
				
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else if (dia_cabecera_asiento_credito.isVisible()){

			dia_cabecera_asiento_credito.cerrar();
			if(validar_movimiento_bloqueado(cal_fecha_inicial_credito.getFecha())){
				utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
				return;
			}
				// Indicamos si el asiento contable se va a generar indivual o agrupado, por ejemplo: individual=true, grupal=false
			if(str_individual.equals("true")){
				System.out.println("entre al true de individual ");
				// Tabla genreica si es asiento contable individual consulto factura por factutra
				TablaGenerica tab_factura= utilitario.consultar("select ide_fafac,ide_fadaf from fac_factura where ide_fafac in ("+str_seleccionados_contabilidad+")");
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// Consulto codigo maximo de la cabecera del asiento contle
					TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
					String maximo_cont_movimiento=tab_maximo.getValor("codigo");
					// Inserto en la cabecera del asiento contable
					//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
					str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
					str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento_credito.getValue()+","+com_mes_credito.getValue()+","+str_asiento_seleccionado+",5,"+com_anio_credito.getValue()+","+p_modulo_nota_credito+",'"
														+cal_fecha_inicial_credito.getFecha()+"','"+txt_area_detalle_asiento_credito.getValue()+"','"+txt_comprobante_credito.getValue()+"',false,now(),now(),"+empleado+")";
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
					System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);
					
					
					// INSERTO EN LA TABLA DEL ASIENTO CONTABLE DE FACTURACION
					// Consulto codigo maximo de la cabecera del asiento factura
					TablaGenerica tab_maximo_factura =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_factura_asiento", "ide_cofaa"));
					String maximo_cont_factura=tab_maximo_factura.getValor("codigo");

					String str_inserta_asiento_factura="insert into cont_factura_asiento (ide_cofaa,ide_coest,ide_comov,ide_fafac,ide_conac,detalle_cofaa,activo_cofaa)"
							+" values ("+maximo_cont_factura+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fafac")+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante_credito.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial_credito.getFecha()+" "+txt_area_detalle_asiento_credito.getValue()+"',true  )";
					utilitario.getConexion().ejecutarSql(str_inserta_asiento_factura);
					
					//actualizo nota de credito
					String update_nota_credito="update fac_nota_credito set ide_comov="+maximo_cont_movimiento+" where ide_fafac="+tab_factura.getValor(i,"ide_fafac");
					utilitario.getConexion().ejecutarSql(update_nota_credito);
					
					// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
					TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_debe,str_ide_conac,"0",str_tipo_asiento,"0"));
					for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "valor_asiento")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
					
					// Tabla generica devuelve los asientos para insertar en contabilidad al haber
					TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(tab_factura.getValor(i,"ide_fafac"),str_lugar_aplica_haber,str_ide_conac,"0",str_tipo_asiento,"0"));
					for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
						TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
						String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

						// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
						str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
						str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "valor_asiento")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
						utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
					}
				}
				
			}
			
			/// contabilizo si es asiento grupal
			else{
				
				// Consulto codigo maximo de la cabecera del asiento contle
				TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
				String maximo_cont_movimiento=tab_maximo.getValor("codigo");
				// Inserto en la cabecera del asiento contable
				//insert into cont_movimiento (ide_comov,ide_cotim,ide_gemes,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov)
				str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
				str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento_credito.getValue()+","+com_mes_credito.getValue()+","+str_asiento_seleccionado+",5,"+com_anio_credito.getValue()+","+p_modulo_nota_credito+",'"+
													cal_fecha_inicial_credito.getFecha()+"','"+txt_area_detalle_asiento_credito.getValue()+"','"+txt_comprobante_credito.getValue()+"',false,now(),now(),"+empleado+")";
				utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
				System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

				TablaGenerica tab_factura= utilitario.consultar("select ide_fafac,ide_fadaf from fac_factura where ide_fafac in ("+str_seleccionados_contabilidad+")");
				//Recorro todas las facturas que voy a contabilizar
				for (int i=0; i < tab_factura.getTotalFilas();i++){
					
					// INSERTO EN LA TABLA DEL ASIENTO CONTABLE DE FACTURACION
					// Consulto codigo maximo de la cabecera del asiento factura
					TablaGenerica tab_maximo_factura =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_factura_asiento", "ide_cofaa"));
					String maximo_cont_factura=tab_maximo_factura.getValor("codigo");

					String str_inserta_asiento_factura="insert into cont_factura_asiento (ide_cofaa,ide_coest,ide_comov,ide_fafac,ide_conac,detalle_cofaa,activo_cofaa)"
							+" values ("+maximo_cont_factura+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_factura.getValor(i,"ide_fafac")+","+str_ide_conac+",'COMPROBANTE NRO: "+txt_comprobante_credito.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial_credito.getFecha()+" "+txt_area_detalle_asiento_credito.getValue()+"',true  )";
					utilitario.getConexion().ejecutarSql(str_inserta_asiento_factura);

					String update_nota_credito="update fac_nota_credito set ide_comov="+maximo_cont_movimiento+" where ide_fafac="+tab_factura.getValor(i,"ide_fafac");
					utilitario.getConexion().ejecutarSql(update_nota_credito);

				}
				// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
				TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_debe,str_ide_conac,"1",str_tipo_asiento,"0"));
				for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){		
					
					
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "valor_asiento")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
				}
				
				// Tabla generica devuelve los asientos para insertar en contabilidad al haber
				TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Facturacion.getFacturasInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_haber,str_ide_conac,"1",str_tipo_asiento,"0"));
				for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
					TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

					// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
					str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
					str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "valor_asiento")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial_fafac")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
					utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
				}

				
			}
			
			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado());
			utilitario.addUpdate("tab_movimiento");
			///guardo secuencial del comprobante de ingresos
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(p_sec_ingresos), p_sec_ingresos);

		}

		else {
			sel_nombre_asiento_credito.getTab_seleccion().setSql(ser_contabilidad.getNombreAsientoContable(p_modulo_nota_credito, "true"));
			sel_nombre_asiento_credito.getTab_seleccion().ejecutarSql();
			sel_nombre_asiento_credito.dibujar();
		}

	}
	
	public void contabilizarAnticipos()
	{
		if(sel_nombre_asiento.isVisible()){
			if(sel_nombre_asiento.getValorSeleccionado()!=null)
			{
				str_asiento_seleccionado=sel_nombre_asiento.getValorSeleccionado();	
				TablaGenerica tab_asiento_seleccionado= utilitario.consultar("select ide_conac,ide_gemod,detalle_conac,individual_conac,ide_coest,ide_bogrm,anio_anterior_conac from cont_nombre_asiento_contable where ide_conac ="+str_asiento_seleccionado);
				str_tipo_asiento = tab_asiento_seleccionado.getValor("ide_coest");
				str_individual = tab_asiento_seleccionado.getValor("individual_conac");
				str_ide_conac =tab_asiento_seleccionado.getValor("ide_conac");
				str_grupo_material =tab_asiento_seleccionado.getValor("ide_bogrm");
				str_anio_anterior =tab_asiento_seleccionado.getValor("anio_anterior_conac");

				sel_nombre_asiento.cerrar();	
				sec_importar.getBot_aceptar().setMetodo("contabilizarAnticipos");
				sec_importar.dibujar();
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else if (sec_importar.isVisible()){
			
			sec_importar.cerrar();
			// Indico que el asiento es de tipo recaudacion
			sel_anticipos_contabilidad.setTitle("ANTICIPOS POR CONTABILIZAR DEL "+sec_importar.getFecha1String()+" AL "+sec_importar.getFecha2String());
			sel_anticipos_contabilidad.getTab_seleccion().setSql(ser_Tesoreria.getActiciposRec("1",sec_importar.getFecha1String(), sec_importar.getFecha2String()));
			sel_anticipos_contabilidad.getTab_seleccion().ejecutarSql();
			sel_anticipos_contabilidad.dibujar();
		}
		else if(sel_anticipos_contabilidad.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_anticipos_contabilidad.getSeleccionados() !=null){
				str_seleccionados_contabilidad=sel_anticipos_contabilidad.getSeleccionados();
				sel_anticipos_contabilidad.cerrar();
				txt_comprobante.setValue(ser_contabilidad.numeroSecuencial(p_sec_ingresos));
				com_anio.setValue(com_anio2.getValue());
				com_mes.setValue(utilitario.getMes(cal_fecha_inicial.getFecha()+""));
				
				com_tipo_movimiento.setValue("1");
				
				dia_cabecera_asiento.getBot_aceptar().setMetodo("contabilizarAnticipos"); 
				dia_cabecera_asiento.dibujar();
				
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else if (dia_cabecera_asiento.isVisible()){

			dia_cabecera_asiento.cerrar();
			if(validar_movimiento_bloqueado(cal_fecha_inicial.getFecha())){
				utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
				return;
			}
			// Indicamos si el asiento contable se va a generar indivual o agrupado, por ejemplo: individual=true, grupal=false
			System.out.println("ASIENTO DE ANTICIPOS");
			// Consulto codigo maximo de la cabecera del asiento contle
			TablaGenerica tab_maximo = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_movimiento", "ide_comov"));
			String maximo_cont_movimiento=tab_maximo.getValor("codigo");
			// Inserto en la cabecera del asiento contable
			str_insert_contabilidad="insert into cont_movimiento (ide_comov,ide_geare,ide_cotim,ide_gemes,ide_conac,ide_cotia,ide_geani,ide_gemod,mov_fecha_comov,detalle_comov,nro_comprobante_comov,activo_comov,fecha_ingre,hora_ingre,ide_gtemp) ";
			str_insert_contabilidad +=" values ("+maximo_cont_movimiento+",11,"+com_tipo_movimiento.getValue()+","+com_mes.getValue()+","+str_asiento_seleccionado+",5,"+com_anio.getValue()+","+p_modulo_factruracion+",'"+cal_fecha_inicial.getFecha()+"','"
												+txt_area_detalle_asiento.getValue()+"','"+txt_comprobante.getValue()+"',false,now(),now(),"+empleado+")";
			utilitario.getConexion().ejecutarSql(str_insert_contabilidad);
			System.out.println("inserto en contabilidad cabecera "+str_insert_contabilidad);

			TablaGenerica tab_fac_cobro= utilitario.consultar("select ide_facob, ide_prcon from fac_cobro where ide_facob in ("+str_seleccionados_contabilidad+")");
			
			//Recorro todos los cobros de anticipos con contratos que voy a contabilizar
			for (int i=0; i < tab_fac_cobro.getTotalFilas();i++){
				
				// Consulto codigo maximo de la cabecera del asiento factura
				TablaGenerica tab_maximo_anticipo_asiendo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_anticipo_asiento", "ide_coana"));
				String maximo_cont_anticipo_asiendo=tab_maximo_anticipo_asiendo.getValor("codigo");

				String str_inserta_asiento_factura="insert into cont_anticipo_asiento (ide_coana,ide_coest,ide_comov,ide_facob,ide_prcon,ide_conac,detalle_coana,activo_coana)"
						+" values ("+maximo_cont_anticipo_asiendo+","+str_tipo_asiento+","+maximo_cont_movimiento+","+tab_fac_cobro.getValor(i,"ide_facob")+","+tab_fac_cobro.getValor(i,"ide_prcon")+","
						+str_ide_conac+",'CONTRATOS NRO: "+txt_comprobante.getValue()+" FECHA MOVIMIENTO: "+cal_fecha_inicial.getFecha()+" "+txt_area_detalle_asiento.getValue()+"',true  )";
				//System.out.println("insert into cont_factura_asiento "+str_inserta_asiento_factura);
				utilitario.getConexion().ejecutarSql(str_inserta_asiento_factura);
				
			}
			
			// Tabla generica devuelve los asientos para insertar en contabilidad.m primewro al debe
			TablaGenerica tab_inserta_detalle = utilitario.consultar(ser_Tesoreria.getAnticiposInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_debe,str_ide_conac,"1",str_tipo_asiento));
			
			for(int j=0;j<tab_inserta_detalle.getTotalFilas();j++){		
				TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
				String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");

				// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
				str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
				str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+","+tab_inserta_detalle.getValor(j, "valor_asiento")+",0,'"+tab_inserta_detalle.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle.getValor(j, "secuencial")+"',true,"+tab_inserta_detalle.getValor(j, "ide_cocac")+","+tab_inserta_detalle.getValor(j, "ide_gelua")+")"; 
				//System.out.println("insert into cont_factura_asiento DEBE "+str_insert_contabilidad_detalle);
				utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
			}
			
			// Tabla generica devuelve los asientos para insertar en contabilidad al haber
			TablaGenerica tab_inserta_detalle_haber = utilitario.consultar(ser_Tesoreria.getAnticiposInsertaContabilidad(str_seleccionados_contabilidad,str_lugar_aplica_haber,str_ide_conac,"1",str_tipo_asiento));
			
			for(int j=0;j<tab_inserta_detalle_haber.getTotalFilas();j++){						
				TablaGenerica tab_codigo_detalle = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
				String maximo_detalle_movimiento=tab_codigo_detalle.getValor("codigo");
				// inserto el detalle de los asientos insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac)
				str_insert_contabilidad_detalle="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,detalle_codem,activo_codem,ide_cocac,ide_gelua)";						
				str_insert_contabilidad_detalle +=" values ("+maximo_detalle_movimiento+","+maximo_cont_movimiento+",0,"+tab_inserta_detalle_haber.getValor(j, "valor_asiento")+",'"+tab_inserta_detalle_haber.getValor(j, "detalle_bogrm")+" "+tab_inserta_detalle_haber.getValor(j, "secuencial")+"',true,"+tab_inserta_detalle_haber.getValor(j, "ide_cocac")+","+tab_inserta_detalle_haber.getValor(j, "ide_gelua")+")"; 
				//System.out.println("insert into cont_factura_asiento HABER "+str_insert_contabilidad_detalle);
				utilitario.getConexion().ejecutarSql(str_insert_contabilidad_detalle);
			}

			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado()); 
			utilitario.addUpdate("tab_movimiento");
			///guardo secuencial del comprobante de ingresos
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(p_sec_ingresos), p_sec_ingresos);
			
		}
		else {
			sel_nombre_asiento.getTab_seleccion().setSql(ser_contabilidad.getNombreAsientoContable(p_modulo_anticipos, "true"));
			sel_nombre_asiento.getTab_seleccion().ejecutarSql();
			sel_nombre_asiento.getBot_aceptar().setMetodo("contabilizarAnticipos");
			sel_nombre_asiento.dibujar();
		}
	}
	
	////metodo año
	public void seleccionaElAnio (){
		if(com_anio2.getValue()!=null){
			//tab_movimiento.setCondicion("ide_geani="+com_anio2.getValue());
			tab_movimiento.setCondicion("ide_geani="+com_anio2.getValue()+" and ide_gemod in ("+p_modulo_factruracion+","+p_modulo_nota_credito+","+p_modulo_nota_debito+","+p_sec_ingresos+")"); 
			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado());
		}
		else{
			tab_movimiento.setCondicion("ide_geani=-1");
			tab_movimiento.ejecutarSql();
		}
	}
	
	public void exportarExcel()
	{
	      if(com_anio2.getValue()==null){
	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
	        return;
	      }
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql(ser_Tesoreria.getRptAnticiposContabilizados(com_anio2.getValue().toString()));
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }
	
	///sacar valores
	public void calcularTotal(AjaxBehaviorEvent evt){
		tab_detalle_movimiento.modificar(evt);
		tab_detalle_movimiento.sumarColumnas();
		utilitario.addUpdate("tab_detalle_movimiento");
	}

	public void quitar()
	{
		if(sel_factura_consulta.getListaSeleccionados().size()>0){
			sel_factura_consulta.cerrar();
			Fila fila=sel_factura_consulta.getListaSeleccionados().get(0);
			boolean nc=false; 
			String sql="";
			StringBuilder sel_ide = new StringBuilder();
			StringBuilder sel_ide_nd = new StringBuilder();
			//StringBuilder sel_ide_nc = new StringBuilder();s
			StringBuilder sel_ide_antp = new StringBuilder();
			
			for (int i = 0; i < sel_factura_consulta.getListaSeleccionados().size(); i++) {
				fila=sel_factura_consulta.getListaSeleccionados().get(i);
				
				String str_comprobante=pckUtilidades.CConversion.CStr(fila.getCampos()[1]);
				String str_id=pckUtilidades.CConversion.CStr(fila.getCampos()[2]);
				//System.out.println("str_comprobante: "+str_comprobante);
				
				if(str_comprobante.contains("NOTA CREDITO"))
				{
					nc=true;
				}
				
				if(str_comprobante.contains("NOTA DEBITO"))
				{
					if(sel_ide_nd.toString().isEmpty()==false){
						sel_ide_nd.append(",");
	                }
					sel_ide_nd.append(str_id);
				}
				else if (str_comprobante.contains("ANTICIPO CONTRATO")){
					if(sel_ide_antp.toString().isEmpty()==false){
						sel_ide_antp.append(",");
	                }
					sel_ide_antp.append(str_id);
				}
				else
				{
					if(sel_ide.toString().isEmpty()==false){
						sel_ide.append(",");
	                }
					sel_ide.append(str_id);
				}

			}
			
			if(pckUtilidades.CConversion.CStr(sel_ide_antp).length()>0)
			{
				sql="delete from cont_anticipo_asiento where ide_coana in ("+sel_ide_antp.toString()+")";
				System.out.println("quitar asientos Anticipos_sql: "+sql);
				
				utilitario.getConexion().ejecutarSql(sql);
			}
			
			if(pckUtilidades.CConversion.CStr(sel_ide_nd).length()>0)
			{
				sql="delete from cont_nota_debito_asiento where ide_conda in ("+sel_ide_nd.toString()+")";
				System.out.println("quitar asientos ND_sql: "+sql);
				
				utilitario.getConexion().ejecutarSql(sql);
			}
			
			if(pckUtilidades.CConversion.CStr(sel_ide).length()>0)
			{
				sql="delete from cont_factura_asiento where ide_cofaa in ("+sel_ide.toString()+")";
				System.out.println("quitar asientos sql: "+sql);
				
				utilitario.getConexion().ejecutarSql(sql);
			}
			
			if(nc)
			{
				sql="update fac_nota_credito set ide_comov=null where ide_comov="+tab_movimiento.getValor("ide_comov");
				System.out.println("quitar asientos notas de credito sql: "+sql);
				utilitario.getConexion().ejecutarSql(sql);
			}
			
			utilitario.agregarMensaje("Descontabilizar", "Se descontabilizaron los registros seleccionados...");

		}
		else{
			utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
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

		if (rep_reporte.getReporteSelecionado().equals("Comprobante Ingreso Rango")){ 
			if (rep_reporte.isVisible()){				
				rep_reporte.cerrar();	
				sel_nombre_asiento_tipo.dibujar();				
			}
			else{
				//utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
				if (sel_nombre_asiento_tipo.isVisible()){
					if(sel_nombre_asiento_tipo.getValorSeleccionado()!=null){
						str_asiento_seleccionado=sel_nombre_asiento_tipo.getValorSeleccionado();	
						sel_nombre_asiento_tipo.cerrar();
						sec_cal_rep.dibujar();
					}
					else {
						utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
					}					
				}	
				else
				{
					if (sec_cal_rep.isVisible()){
						sec_cal_rep.cerrar();
						
						TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+utilitario.getVariable("ide_usua"));
						p_parametros=new HashMap();	
						p_parametros.put("titulo", "EMGIRS - EP");
						p_parametros.put("p_contador_general", tab_usuario.getValor("nom_usua").toUpperCase());
						p_parametros.put("p_ide_conac",pckUtilidades.CConversion.CInt(str_asiento_seleccionado));
						
						p_parametros.put("p_fecha_i",sec_cal_rep.getFecha1String());
						p_parametros.put("p_fecha_f",sec_cal_rep.getFecha2String());
						
						//p_parametros.put("p_fecha_i",utilitario.DeStringADate(sec_cal_rep.getFecha1String()));
						//p_parametros.put("p_fecha_f",utilitario.DeStringADate(sec_cal_rep.getFecha2String()));
						
						p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_usua")));
						p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_empr")));
						p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_sucu")));
						p_parametros.put("REPORT_LOCALE", locale);
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						sef_reporte.dibujar();
					}
				}
			}
		}
		
		if(rep_reporte.getReporteSelecionado().equals("Comprobante Ingreso")){ 
			if (rep_reporte.isVisible()){				
				rep_reporte.cerrar();	
				TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+utilitario.getVariable("ide_usua"));
				p_parametros=new HashMap();		

				p_parametros.put("titulo", "EMGIRS - EP");
				//p_parametros.put("p_contador_general", utilitario.getVariable("p_nombre_contador"));
				p_parametros.put("p_contador_general", tab_usuario.getValor("nom_usua").toUpperCase());
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	
				p_parametros.put("p_ide_comov",pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_comov")));
				p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_usua")));
				p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_empr")));
				p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_sucu")));
				p_parametros.put("REPORT_LOCALE", locale);
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		}
	}
	
	public boolean validar_movimiento_bloqueado(String fecha)
	{
		String id_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");	
		if(!empleado.equals(id_empleado)){
			System.out.println("actualizando empleado comprobante pago ide_gtemp");
			empleado=id_empleado;
		}
		
		TablaGenerica tab_mov = utilitario.consultar("SELECT ide_cocim, detalle_cocim, activo_cocim FROM cont_cierre_movimiento " +
								" where bloqueado_cocim=true and ide_gemes=extract(month from cast('"+fecha+"' as date)) " +
								" and ide_geani in (SELECT ide_geani FROM gen_anio where cast(detalle_geani as integer) = extract(year from cast('"+fecha+"' as date)))");
		tab_mov.imprimirSql();
		if(tab_mov.getTotalFilas()>0)
			return true;
		
		return false;
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_pre_mensual.isFocus()){
        	tab_pre_mensual.insertar();
        	tab_pre_mensual.setValor("ide_codem", tab_detalle_movimiento.getValor("ide_codem"));
        	tab_pre_mensual.setValor("comprobante_prmen", tab_movimiento.getValor("NRO_COMPROBANTE_COMOV"));
        	tab_pre_mensual.setValor("fecha_ejecucion_prmen", tab_movimiento.getValor("mov_fecha_comov"));
        	tab_pre_mensual.setValor("devengado_prmen", "0.00");
        	tab_pre_mensual.setValor("cobrado_prmen", "0.00");
        	tab_pre_mensual.setValor("cobradoc_prmen", "0.00");
        	tab_pre_mensual.setValor("pagado_prmen", "0.00");
        	tab_pre_mensual.setValor("comprometido_prmen", "0.00");
        	tab_pre_mensual.setValor("valor_anticipo_prmen", "0.00");
        	tab_pre_mensual.setValor("certificado_prmen", "0.00");
        	System.out.println("tab_pre_mensual.insertar() "+tab_movimiento.getValor("NRO_COMPROBANTE_COMOV"));
        }
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar Movimientos Contables", "Debe Seleccionar un Año.");
			return;
		}
		else if (tab_movimiento.isFocus()) {
			tab_movimiento.insertar();
			tab_movimiento.setValor("ide_geani", com_anio.getValue()+"");
			tab_movimiento.setValor("ide_gtemp", empleado );
			//tab_movimiento.setValor("ide_geedp", empleado_responsable );
			
            utilitario.addUpdateTabla(tab_movimiento, "ide_geani", "");
		}
		else if (tab_detalle_movimiento.isFocus()) {
			tab_detalle_movimiento.insertar();
			
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		ser_contabilidad.limpiarAcceso("pre_mensual");
		
		TablaGenerica tab_anio = utilitario.consultar("select ide_geani,activo_geani,bloqueado_geani from gen_anio where ide_geani ="+com_anio2.getValue());
		TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from cast('"+tab_movimiento.getValor("mov_fecha_comov")+"' as date))");		
		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}
		
		if(!tab_anio.getValor("activo_geani").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Inactivo");
			return;
		}
		
		if(tab_anio.getValor("bloqueado_geani").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Bloqueado");
			return;
		}
		
		if(tab_movimiento.getValor("activo_comov").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra mayorizado no se puede modificar");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_geedp"))>0){
			utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra aprobado.");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_gtemp"))!=pckUtilidades.CConversion.CInt(empleado)){
			utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra generado por otro usuario.");
			//return;
		}
		
		if (tab_movimiento.guardar()){
			if (tab_detalle_movimiento.guardar())
				if( tab_pre_mensual.guardar())
					guardarPantalla();
		}
		//tab_detalle_movimiento.sumarColumnas();
		//utilitario.addUpdate("tab_detalle_movimiento");
		

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		sel_factura_consulta.getTab_seleccion().setSql(ser_Facturacion.getFacturasNotasContabilizadas(tab_movimiento.getValor("ide_comov") ));
		sel_factura_consulta.getTab_seleccion().ejecutarSql();
		
		if(sel_factura_consulta.getTab_seleccion().getTotalFilas()>0)
		{
			utilitario.agregarMensajeError("No se puede eliminar", "El asiento tiene comprobantes asociados.");
			return;
		}
		
		utilitario.getTablaisFocus().eliminar();
		
	}

	public Tabla getTab_movimiento() {
		return tab_movimiento;
	}

	public void setTab_movimiento(Tabla tab_movimiento) {
		this.tab_movimiento = tab_movimiento;
	}

	public Tabla getTab_detalle_movimiento() {
		return tab_detalle_movimiento;
	}

	public void setTab_detalle_movimiento(Tabla tab_detalle_movimiento) {
		this.tab_detalle_movimiento = tab_detalle_movimiento;
	}

	public Tabla getTab_pre_mensual() {
		return tab_pre_mensual;
	}
	public void setTab_pre_mensual(Tabla tab_pre_mensual) {
		this.tab_pre_mensual = tab_pre_mensual;
	}

	public SeleccionTabla getSel_factura_contabilidad() {
		return sel_factura_contabilidad;
	}


	public void setSel_factura_contabilidad(SeleccionTabla sel_factura_contabilidad) {
		this.sel_factura_contabilidad = sel_factura_contabilidad;
	}


	public SeleccionTabla getSel_factura_consulta() {
		return sel_factura_consulta;
	}


	public void setSel_factura_consulta(SeleccionTabla sel_factura_consulta) {
		this.sel_factura_consulta = sel_factura_consulta;
	}

	public SeleccionTabla getSel_nombre_asiento() {
		return sel_nombre_asiento;
	}

	public void setSel_nombre_asiento(SeleccionTabla sel_nombre_asiento) {
		this.sel_nombre_asiento = sel_nombre_asiento;
	}

	public SeleccionCalendario getSec_importar() {
		return sec_importar;
	}

	public void setSec_importar(SeleccionCalendario sec_importar) {
		this.sec_importar = sec_importar;
	}

	public Dialogo getDia_cabecera_asiento() {
		return dia_cabecera_asiento;
	}

	public void setDia_cabecera_asiento(Dialogo dia_cabecera_asiento) {
		this.dia_cabecera_asiento = dia_cabecera_asiento;
	}

	public SeleccionTabla getSel_nota_debito() {
		return sel_nota_debito;
	}

	public void setSel_nota_debito(SeleccionTabla sel_nota_debito) {
		this.sel_nota_debito = sel_nota_debito;
	}

	public SeleccionTabla getSel_nota_credito() {
		return sel_nota_credito;
	}

	public void setSel_nota_credito(SeleccionTabla sel_nota_credito) {
		this.sel_nota_credito = sel_nota_credito;
	}

	public SeleccionTabla getSel_nombre_asiento_debito() {
		return sel_nombre_asiento_debito;
	}

	public void setSel_nombre_asiento_debito(
			SeleccionTabla sel_nombre_asiento_debito) {
		this.sel_nombre_asiento_debito = sel_nombre_asiento_debito;
	}

	public SeleccionTabla getSel_nombre_asiento_credito() {
		return sel_nombre_asiento_credito;
	}

	public void setSel_nombre_asiento_credito(
			SeleccionTabla sel_nombre_asiento_credito) {
		this.sel_nombre_asiento_credito = sel_nombre_asiento_credito;
	}

	public SeleccionCalendario getSel_cal_debito() {
		return sel_cal_debito;
	}

	public void setSel_cal_debito(SeleccionCalendario sel_cal_debito) {
		this.sel_cal_debito = sel_cal_debito;
	}

	public SeleccionCalendario getSel_cal_credito() {
		return sel_cal_credito;
	}

	public void setSel_cal_credito(SeleccionCalendario sel_cal_credito) {
		this.sel_cal_credito = sel_cal_credito;
	}

	public Dialogo getDia_cabecera_asiento_credito() {
		return dia_cabecera_asiento_credito;
	}

	public void setDia_cabecera_asiento_credito(Dialogo dia_cabecera_asiento_credito) {
		this.dia_cabecera_asiento_credito = dia_cabecera_asiento_credito;
	}

	public Dialogo getDia_cabecera_asiento_debito() {
		return dia_cabecera_asiento_debito;
	}

	public void setDia_cabecera_asiento_debito(Dialogo dia_cabecera_asiento_debito) {
		this.dia_cabecera_asiento_debito = dia_cabecera_asiento_debito;
	}

	public SeleccionTabla getSel_anticipos_contabilidad() {
		return sel_anticipos_contabilidad;
	}

	public void setSel_anticipos_contabilidad(SeleccionTabla sel_anticipos_contabilidad) {
		this.sel_anticipos_contabilidad = sel_anticipos_contabilidad;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public SeleccionCalendario getSec_cal_rep() {
		return sec_cal_rep;
	}

	public void setSec_cal_rep(SeleccionCalendario sec_cal_rep) {
		this.sec_cal_rep = sec_cal_rep;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionTabla getSel_nombre_asiento_tipo() {
		return sel_nombre_asiento_tipo;
	}

	public void setSel_nombre_asiento_tipo(SeleccionTabla sel_nombre_asiento_tipo) {
		this.sel_nombre_asiento_tipo = sel_nombre_asiento_tipo;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	

}
