package paq_facturacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import paq_bodega.ejb.ServicioBodega;
import paq_comercializacion.ejb.ServicioClientes;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import pck_cliente.servicio;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Etiqueta;

public class pre_factura extends Pantalla {

	private Tabla tab_factura = new Tabla();
	private Tabla tab_detalle_factura = new Tabla();
	private AutoCompletar aut_factura = new AutoCompletar();
	private Dialogo crear_cliente_dialogo=new Dialogo();
	private Dialogo dia_cliente_establecimiento = new Dialogo();
	private Tabla tab_cliente=new Tabla();
	private Tabla tab_direccion= new Tabla();
	private Tabla tab_email= new Tabla();

	private SeleccionTabla set_sucursales = new SeleccionTabla();
	private SeleccionTabla set_factura_inicial = new SeleccionTabla();
	private SeleccionTabla set_pantallacliente = new SeleccionTabla();
	private SeleccionTabla set_pantallaestablecimiento = new SeleccionTabla();
	private SeleccionTabla set_actualizar_cliente = new SeleccionTabla();
	private Confirmar con_guardar_cliente = new Confirmar();
	private Confirmar con_generar_Nfactura = new Confirmar();
	public static String p_modulo_facturacion;

	private Etiqueta eti_total = new Etiqueta();
	public Radio rad_tipo= new Radio();
	private int tipoRecalculo=0;
	private Panel pan_opcion = new Panel();
	private Efecto efecto = new Efecto();

	private double dou_por_iva = 0.12;
	private double dou_base_no_iva = 0;
	private double dou_base_cero = 0;
	private double dou_base_aprobada = 0;
	private double dou_valor_iva = 0;
	private double dou_total = 0;
	private double dou_descuento = 0;
	private double dou_irbpnr = 0;
	private boolean unirAnuladas;

	private String valor;
	private String valorAut_factura;

	// REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	// Inicio Facturacion Electronica - AVACA
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo factura_elec_dialogo = new Dialogo();
	private int ambiente = 2; // 1 Test: 2 Produccion
	private boolean autorizar = true; // true Produccion
	private Check che_iva14 = new Check();
	private String str_ide_factura;
	private Check che_ambiente=new Check();
	private VisualizarPDF vpdf_rideFac = new VisualizarPDF();
	// Fin Facturacion Electronica - AVACA
	private Combo com_anio=new Combo();
	private Combo com_mes=new Combo();
	
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);

	
	public pre_factura() {

		// TODO Auto-generated constructor stub

		dou_por_iva = pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva"));
		ambiente = pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		p_modulo_facturacion = utilitario.getVariable("p_modulo_facturacion");

		str_ide_factura = "0";
		rep_reporte.setId("rep_reporte"); // id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");// ejecuta el  metodo al aceptar  reporte
		agregarComponente(rep_reporte);// agrega el componente a la pantalla
		bar_botones.agregarReporte();// aparece el boton de reportes en la barra  de botones
		self_reporte.setId("self_reporte"); // id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes.setCombo(ser_contabilidad.getMes("true"));	
		com_mes.setMetodo("seleccionaElAnio");
		com_mes.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("MES:"));
		bar_botones.agregarComponente(com_mes);
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_factura.setId("aut_factura");
		aut_factura.setAutoCompletar(ser_facturacion.getDatosFactura("1", "")); // 1  carga  todos,
																				// 0  carga  por  grupos  enviados
		aut_factura.setMetodoChange("seleccionoAutocompletar"); // ejecuta el  metodo  seleccionoAutocompletar

		Etiqueta eti_colaborador = new Etiqueta("PUNTO FACTURACIÓN:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);

		che_iva14.setId("che_iva14");
		che_iva14.setMetodoChange("cambiaIVA");
		//Etiqueta eti_iva14 = new Etiqueta("IVA 14%");
		//bar_botones.agregarComponente(eti_iva14);
		//bar_botones.agregarComponente(che_iva14);

		pan_opcion.setId("pan_opcion");
		pan_opcion.setTransient(true);

		efecto.setType("drop");
		efecto.setSpeed(150);
		efecto.setPropiedad("mode", "'show'");
		efecto.setEvent("load");
		pan_opcion.getChildren().add(efecto);
		
		Grid gri_lateral = new Grid();
		gri_lateral.setWidth("100%");
		
		// BOTON Nueva factura desde anulada
		Boton bot_nuevAnulada = new Boton();
		bot_nuevAnulada.setValue("Facturar Anulada");
		bot_nuevAnulada.setIcon("ui-icon-document");
		bot_nuevAnulada.setMetodo("nuevAnulada");
		
		// BOTON Nueva factura desde anulada
		Boton bot_dividirAnulada = new Boton();
		bot_dividirAnulada.setValue("Facturar Dividir Anulada");
		bot_dividirAnulada.setIcon("ui-icon-document");
		bot_dividirAnulada.setMetodo("divAnulada");
		
		// BOTON AGREGAR CLIENTE
		Boton bot_agregarCliente = new Boton();
		bot_agregarCliente.setValue("Facturar Cliente/Establecimiento");
		bot_agregarCliente.setIcon("ui-icon-person");
		bot_agregarCliente.setMetodo("agregarCliente");
		//bar_botones.agregarBoton(bot_agregarCliente);

		// PANTALLA SELECIONAR CLIENTE
		set_pantallacliente.setId("set_pantallacliente");
		set_pantallacliente.setTitle("SELECCIONE CLIENTES");
		set_pantallacliente.getBot_aceptar().setMetodo("aceptarCliente");
		set_pantallacliente.setSeleccionTabla(ser_facturacion.getClientesActivos("0,1"), "ide_recli");
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantallacliente.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(25);
		set_pantallacliente.getTab_seleccion().getColumna("razon_social_recli").setLongitud(40);
		set_pantallacliente.getTab_seleccion().getColumna("numero_contrato").setLongitud(10);
		set_pantallacliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(20);
		set_pantallacliente.getTab_seleccion().getColumna("telefono_factura_recli").setLongitud(20);
		set_pantallacliente.getTab_seleccion().getColumna("email_recle").setLongitud(20);
		set_pantallacliente.setRadio();
		set_pantallacliente.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantallacliente);
		
		set_pantallaestablecimiento.setId("set_pantallaestablecimiento");
		set_pantallaestablecimiento.setTitle("SELECCIONE UN ESTABLECIMIENTO");
		set_pantallaestablecimiento.getBot_aceptar().setMetodo("aceptarCliente");
		set_pantallaestablecimiento.setSeleccionTabla(ser_facturacion.getEstablecimientosActivos("true,false"), "ide_reest");
		set_pantallaestablecimiento.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		set_pantallaestablecimiento.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(25);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("razon_social_recli").setLongitud(40);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("numero_contrato").setLongitud(10);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(20);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("telefono_factura_recli").setLongitud(20);
		set_pantallaestablecimiento.getTab_seleccion().getColumna("email_recle").setLongitud(20);
		set_pantallaestablecimiento.setRadio();
		set_pantallaestablecimiento.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantallaestablecimiento);
		
		List listax = new ArrayList();
        Object fila1x[] = {"0", "MATRIZ"};
        Object fila2x[] = {"1", "ESTABLECIMIENTOS" };
	       
        listax.add(fila1x);
        listax.add(fila2x);
        rad_tipo.setRadio(listax);
        //rad_tipo.setValue(fila1x);
		dia_cliente_establecimiento.setId("dia_cliente_establecimiento");
		dia_cliente_establecimiento.setTitle("Seleccione el tipo de facturación");
		dia_cliente_establecimiento.setWidth("25%");
		dia_cliente_establecimiento.setHeight("20%");
		dia_cliente_establecimiento.setDialogo(rad_tipo);
		dia_cliente_establecimiento.getBot_aceptar().setMetodo("agregarCliente");
       	
       	agregarComponente(dia_cliente_establecimiento);

		// Inicio Facturacion Electronica - AVACA
		// BOTÓN FACTURA ELECTRÓNICA ---------------------------------
		che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);
 		
		Boton bot_fac_elec = new Boton();
		bot_fac_elec.setIcon("ui-icon-newwin");
		bot_fac_elec.setValue("Factura Electrónica");
		bot_fac_elec.setMetodo("abrirDialogoFacElectronica");
		bar_botones.agregarBoton(bot_fac_elec);

		// DIÁLOGO FACTURA ELECTRÓNICA --------------------------------
		factura_elec_dialogo.setId("factura_elec_dialogo");
		factura_elec_dialogo.setTitle("GENERAR FACTURA ELECTRÓNICA");
		factura_elec_dialogo.setWidth("45%");
		factura_elec_dialogo.setHeight("30%");

		// GRID FACTURA ELECTRÓNICA
		Grid gri_fac_elec = new Grid();
		gri_fac_elec.setColumns(2);
		factura_elec_dialogo.setDialogo(gri_fac_elec);
		factura_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoFacElectronica");
		agregarComponente(factura_elec_dialogo);

		// DIÁLOGO RESPUESTA DEL CORE --------------------------------
		respuesta_core_dialogo.setId("respuesta_core_dialogo");
		respuesta_core_dialogo.setTitle("RESPUESTA DEL SERVIDOR");
		respuesta_core_dialogo.setWidth("45%");
		respuesta_core_dialogo.setHeight("30%");

		// GRID RESPUESTA DEL CORE
		Grid gri_respuesta = new Grid();
		gri_respuesta.setColumns(2);
		respuesta_core_dialogo.setDialogo(gri_respuesta);
		respuesta_core_dialogo.getBot_aceptar().setMetodo("aceptarDialogoRespuestaCore");
		agregarComponente(respuesta_core_dialogo);
		// FIN Facturacion Electronica - AVACA

		// NUEVO PROCESO PARA LA EMISION DE NOTAS DE DEBITO
		set_sucursales.setId("set_sucursales");
		set_sucursales.setSeleccionTabla(ser_facturacion.getDatosFactura("1", ""), "ide_fadaf");
		set_sucursales.setTitle("SELECCIONE UN ESTABLECIMIENTO - GENERAR FACTURA A PARTIR DE UNA ANULADA");
		// set_sucursales.getTab_seleccion().getColumna("secuencial").setFiltro(true);
		set_sucursales.setFooter("Presione Cancelar en caso de generar una nueva factura...");
		set_sucursales.getBot_aceptar().setMetodo("aceptarEstablecimiento");
		set_sucursales.getBot_cancelar().setMetodo("cancelarFactura");
		set_sucursales.setRadio();
		agregarComponente(set_sucursales);

		set_factura_inicial.setId("set_factura_inicial");
		set_factura_inicial.setSeleccionTabla(ser_facturacion.getFacturasAnuladas("-1"), "ide_fafac");
		set_factura_inicial.setTitle("SELECCIONE UNA FACTURA ANULADA");
		set_factura_inicial.getTab_seleccion().getColumna("secuencial").setFiltro(true);
		set_factura_inicial.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura_inicial.getBot_cancelar().setMetodo("cancelarFactura");
		// set_factura_inicial.setRadio();
		// set_factura_inicial.getTab_seleccion().ejecutarSql();
		agregarComponente(set_factura_inicial);

		con_generar_Nfactura.setId("con_generar_Nfactura");
		con_generar_Nfactura.setMessage("Esta Seguro de Generar una Factura a partir de una Anulada");
		con_generar_Nfactura.setTitle("Confirmación de Generación");
		con_generar_Nfactura.getBot_aceptar().setMetodo("generarFactura");
		agregarComponente(con_generar_Nfactura);

		// TABLA CABECERA FACTURA
		tab_factura.setHeader("FACTURACIÓN");
		tab_factura.setId("tab_factura");
		tab_factura.setTabla("fac_factura", "ide_fafac", 1);
		tab_factura.setGenerarPrimaria(false);
		tab_factura.getColumna("ide_fafac").setLectura(true);
		// para q no se dibuje antes q seleccione el autocompletar
		tab_factura.setCondicion("ide_fadaf=-1");
		tab_factura.setTipoFormulario(true);
		tab_factura.getGrid().setColumns(6);
		tab_factura.getColumna("ide_fadaf").setVisible(false);
		// tab_factura.getColumna("ide_comov").setVisible(false);
		tab_factura.getColumna("ide_sucu").setCombo("sis_sucursal", "ide_sucu", "nom_sucu", "");
		// tab_factura.getColumna("ide_tetid").setVisible(false);
		tab_factura.getColumna("ide_tedar").setVisible(false);
		tab_factura.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", p_modulo_facturacion));
		tab_factura.getColumna("ide_geins").setCombo("gen_institucion", "ide_geins", "detalle_geins", "");
		tab_factura.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_factura.getColumna("ide_recli").setAutoCompletar();
		tab_factura.getColumna("ide_recli").setLectura(true);
		tab_factura.getColumna("ide_reest").setCombo(ser_facturacion.getEstablecimientosActivos("true,false"));
		tab_factura.getColumna("ide_reest").setAutoCompletar();
		tab_factura.getColumna("ide_reest").setLectura(true);
		tab_factura.getColumna("ide_falug").setCombo("fac_lugar", "ide_falug", "detalle_lugar_falug", "");
		tab_factura.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		tab_factura.getColumna("ide_retip").setValorDefecto(utilitario.getVariable("p_tipo_cobro_factura"));
		tab_factura.getColumna("ide_retip").setValorDefecto("4");
		tab_factura.getColumna("ide_gtemp").setCombo(ser_empleado.servicioEmpleadosActivos("true,false"));
		// TOTALES DE COLOR ROJO--ESTILO DE COLOR ROJO Y NEGRILLA
		tab_factura.getColumna("base_no_iva_fafac").setEtiqueta();
		tab_factura.getColumna("base_no_iva_fafac").setValorDefecto("0");
		tab_factura.getColumna("base_no_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.setCampoOrden("ide_fafac desc");
		tab_factura.getColumna("base_cero_fafac").setEtiqueta();
		tab_factura.getColumna("base_cero_fafac").setValorDefecto("0");
		tab_factura.getColumna("base_cero_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_factura.getColumna("base_aprobada_fafac").setEtiqueta();
		tab_factura.getColumna("base_aprobada_fafac").setValorDefecto("0");
		tab_factura.getColumna("base_aprobada_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_factura.getColumna("base_aprobada_fafac").setFormatoNumero(2);
		tab_factura.getColumna("valor_iva_fafac").setEtiqueta();
		tab_factura.getColumna("valor_iva_fafac").setValorDefecto("0");
		tab_factura.getColumna("valor_iva_fafac").setFormatoNumero(2);
		tab_factura.getColumna("valor_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo

		tab_factura.getColumna("descuento_fafac").setEtiqueta();
		tab_factura.getColumna("descuento_fafac").setValorDefecto("0");
		tab_factura.getColumna("descuento_fafac").setFormatoNumero(2);
		tab_factura.getColumna("descuento_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo

		tab_factura.getColumna("irbpnr_fafac").setEtiqueta();
		tab_factura.getColumna("irbpnr_fafac").setValorDefecto("0");
		tab_factura.getColumna("irbpnr_fafac").setFormatoNumero(2);
		tab_factura.getColumna("irbpnr_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo

		tab_factura.getColumna("total_fafac").setEtiqueta();
		tab_factura.getColumna("total_fafac").setValorDefecto("0");
		tab_factura.getColumna("total_fafac").setFormatoNumero(2);
		tab_factura.getColumna("total_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_factura.getColumna("secuencial_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.getColumna("secuencial_fafac").setEtiqueta();
		tab_factura.getColumna("fecha_transaccion_fafac").setMetodoChange("fechaVencimiento");
		tab_factura.getColumna("fecha_emision_fafac").setVisible(false);
		tab_factura.getColumna("direccion_fafac").setVisible(false);
		tab_factura.getColumna("responsable_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("codigo_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("activo_fafac").setValorDefecto("true");
		tab_factura.getColumna("activo_fafac").setLectura(true);
		tab_factura.getColumna("conciliado_fafac").setLectura(true);
		tab_factura.getColumna("fecha_transaccion_fafac").setValorDefecto(utilitario.getFechaActual());
		tab_factura.getColumna("base_no_iva_fafac").setVisible(false);
		tab_factura.getColumna("nro_autorizacion_sri_fac").setLongitud(50);
		
		tab_factura.agregarRelacion(tab_detalle_factura);
		tab_factura.dibujar();

		PanelTabla pat_factura = new PanelTabla();
		pat_factura.setPanelTabla(tab_factura);

		// TABLA DETALLE FACTURA
		tab_detalle_factura.setId("tab_detalle_factura");
		tab_detalle_factura.setTabla("fac_detalle_factura", "ide_fadef", 2);
		tab_detalle_factura.setGenerarPrimaria(false);
		tab_detalle_factura.setCampoForanea("ide_fafac");
		tab_detalle_factura.getColumna("cantidad_fadef").setValorDefecto("0");
		tab_detalle_factura.getColumna("activo_fadef").setValorDefecto("true");
		tab_detalle_factura.getColumna("activo_fadef").setLectura(true);
		tab_detalle_factura.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("0", "true,false", "2,9"));
		tab_detalle_factura.getColumna("ide_bomat").setAutoCompletar();
		// definimos el metodo que va a ejecutar cuando el usuario seleccione
		// del Autocompletar
		tab_detalle_factura.getColumna("ide_bomat").setMetodoChange("seleccionoProducto");
		tab_detalle_factura.getColumna("total_fadef").setEtiqueta();
		tab_detalle_factura.getColumna("total_fadef").setEstilo("font-size:13px;font-weight:bold;");
		tab_detalle_factura.getColumna("valor_fadef").setEtiqueta();
		tab_detalle_factura.getColumna("valor_fadef").setFormatoNumero(2);
		tab_detalle_factura.getColumna("valor_fadef").setEstilo("font-size:13px;font-weight:bold;");

		tab_detalle_factura.getColumna("descuento_fadef").setValorDefecto("0");
		tab_detalle_factura.getColumna("irbpnr_fadef").setValorDefecto("0");

		tab_detalle_factura.getColumna("fecha_fadef").setValorDefecto(utilitario.getFechaActual());
		// LLAMAR A ESTE METODO CUANDO EL USUARIO, MODIFIQUE LA CANTIDAD O EL
		// VALOR DESDE LA APLICACION
		tab_detalle_factura.getColumna("cantidad_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("valor_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("descuento_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("irbpnr_fadef").setMetodoChange("calcularDetalle");

		tab_detalle_factura.dibujar();

		PanelTabla pat_detalle_factura = new PanelTabla();
		pat_detalle_factura.setMensajeWarn("DETALLE FACTURACION");
		pat_detalle_factura.setPanelTabla(tab_detalle_factura);

		eti_total.setId("eti_total");
		eti_total.setStyle("font-size:18px;color:red;widht:80%");
		eti_total.setValue("TOTAL : 0.00");
		Division div_aux = new Division();
		div_aux.setFooter(pat_detalle_factura, eti_total, "80%");

		Division div_division2 = new Division();
		div_division2.dividir2(pat_factura, div_aux, "50%", "h");
		

		//BOTON CREAR CLIENTE
		Boton bot_crearCliente=new Boton();
		bot_crearCliente.setValue("Crear Cliente");
		bot_crearCliente.setIcon("ui-icon-person");
		bot_crearCliente.setMetodo("abrirDialogoCliente");
		//bar_botones.agregarBoton(bot_crearCliente);
				
		//PANTALLA CREAR CLIENTE
		crear_cliente_dialogo.setId("crear_cliente_dialogo");
		crear_cliente_dialogo.setTitle("CREAR CLIENTE");
		crear_cliente_dialogo.setWidth("45%");
		crear_cliente_dialogo.setHeight("55%");
		
		Grid gri_cuerpo=new Grid();
		tab_cliente.setId("tab_cliente");
		tab_cliente.setTabla("rec_clientes", "ide_recli",10);
		tab_cliente.setTipoFormulario(true);
		tab_cliente.setCondicion("ide_recli=-1");//para que aparesca vacia
		tab_cliente.getGrid().setColumns(2);
		//oculto todos los campos
		for(int i=0;i<tab_cliente.getTotalColumnas();i++){
			tab_cliente.getColumnas()[i].setVisible(false);
		}
		//Muestro solo los campos necesarios
		tab_cliente.getColumna("razon_social_recli").setVisible(true);	
		tab_cliente.getColumna("razon_social_recli").setNombreVisual("RAZON SOCIAL");
		tab_cliente.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_cliente.getColumna("ide_gttdi").setVisible(true);
		tab_cliente.getColumna("ide_gttdi").setValorDefecto("3");
		tab_cliente.getColumna("ide_gttdi").setNombreVisual("DOCUMENTO IDENTIDAD");
		tab_cliente.getColumna("ruc_comercial_recli").setVisible(true);
		tab_cliente.getColumna("ruc_comercial_recli").setNombreVisual("RUC/CEDULA");
		tab_cliente.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_cliente.getColumna("telefono_factura_recli").setVisible(true);
		tab_cliente.getColumna("telefono_factura_recli").setNombreVisual("TELEFONO");
		tab_cliente.getColumna("factura_datos_recli").setValorDefecto("1");
		tab_cliente.getColumna("aplica_mtarifa_recli").setValorDefecto("false");
		tab_cliente.getColumna("nro_establecimiento_recli").setValorDefecto("1");
		tab_cliente.setMostrarNumeroRegistros(false);//PARA Q NO MUESTRE EL TITULO REGSITRO 1 DE 1
		tab_cliente.getColumna("matriz_sucursal_recli").setValorDefecto("1");
		tab_cliente.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_cliente.getColumna("ide_tetar").setVisible(true);
		tab_cliente.getColumna("ide_tetar").setValorDefecto("1");
		tab_cliente.getColumna("ide_tetar").setNombreVisual("TARIFA");
		tab_cliente.getColumna("ide_geplf").setValorDefecto("1");
		tab_cliente.getColumna("activo_recli").setValorDefecto("true");
		tab_cliente.dibujar();

		gri_cuerpo.getChildren().add(tab_cliente);

		//DIRECCION 
		tab_direccion.setId("tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion", "ide_recld", 11);
		tab_direccion.setMostrarNumeroRegistros(false);//PARA Q NO MUESTRE EL TITULO REGSITRO 1 DE 1
		tab_direccion.setTipoFormulario(true);
		tab_direccion.getGrid().setColumns(2);
		//oculto todos los campos
		for(int i=0;i<tab_direccion.getTotalColumnas();i++){
			tab_direccion.getColumnas()[i].setVisible(false);
		}
		tab_direccion.getColumna("direccion_recld").setNombreVisual("DIRECCION");
		tab_direccion.getColumna("direccion_recld").setVisible(true);
		tab_direccion.getColumna("activo_recld").setValorDefecto("true");
		tab_direccion.setCondicion("ide_recld=-1");
		tab_direccion.dibujar();
		gri_cuerpo.getChildren().add(tab_direccion);

		//EMAIL 
		tab_email.setId("tab_email");
		tab_email.setTabla("rec_cliente_email", "ide_recle", 12);
		tab_email.setMostrarNumeroRegistros(false);//PARA Q NO MUESTRE EL TITULO REGSITRO 1 DE 1
		tab_email.setTipoFormulario(true);
		tab_email.getGrid().setColumns(2);
		//oculto todos los campos
		for(int i=0;i<tab_email.getTotalColumnas();i++){
			tab_email.getColumnas()[i].setVisible(false);
		}
		tab_email.getColumna("email_recle").setNombreVisual("E-MAIL");
		tab_email.getColumna("email_recle").setVisible(true);
		tab_email.getColumna("activo_recle").setValorDefecto("true");
		tab_email.setCondicion("ide_recle=-1");
		tab_email.dibujar();
		gri_cuerpo.getChildren().add(tab_email);

		crear_cliente_dialogo.getBot_aceptar().setMetodo("aceptarDialogoCliente");

		crear_cliente_dialogo.setDialogo(gri_cuerpo);
		agregarComponente(crear_cliente_dialogo);		
		
		// Boton Actualizar Cliente
		Boton bot_actualizar_cliente = new Boton();
		bot_actualizar_cliente.setIcon("ui-icon-person");
		bot_actualizar_cliente.setValue("Actualizar Cliente");
		bot_actualizar_cliente.setMetodo("actualizarCliente");
		//bar_botones.agregarBoton(bot_actualizar_cliente);

		con_guardar_cliente.setId("con_guardar_cliente");
		con_guardar_cliente.setMessage("Esta Seguro de Actualizar el Cliente");
		con_guardar_cliente.setTitle("Confirmación de actualizar");
		con_guardar_cliente.getBot_aceptar().setMetodo("guardarActualizarCliente");
		agregarComponente(con_guardar_cliente);

		set_actualizar_cliente.setId("set_actualizar_cliente");
		set_actualizar_cliente.setTitle("SELECCIONE CLIENTES");
		set_actualizar_cliente.setSeleccionTabla(ser_facturacion.getClientesActivos("0,1"), "ide_recli");
		set_actualizar_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_actualizar_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setNombreVisual("RUC");
		set_actualizar_cliente.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		set_actualizar_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(25);
		set_actualizar_cliente.getTab_seleccion().getColumna("razon_social_recli").setLongitud(40);
		set_actualizar_cliente.getTab_seleccion().getColumna("numero_contrato").setLongitud(10);
		set_actualizar_cliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(20);
		set_actualizar_cliente.getTab_seleccion().getColumna("telefono_factura_recli").setLongitud(20);
		set_actualizar_cliente.getTab_seleccion().getColumna("email_recle").setLongitud(20);
		set_actualizar_cliente.setRadio();
		set_actualizar_cliente.getBot_aceptar().setMetodo("modificarCliente");
		agregarComponente(set_actualizar_cliente);
				
		//REPORTE
		vpdf_rideFac.setId("vpdf_rideFac");
        vpdf_rideFac.setTitle("Factura Electrónica");
        agregarComponente(vpdf_rideFac);
        
        Boton bot_ride = new Boton();
        bot_ride.setIcon("ui-icon-print");
        bot_ride.setValue("Imprimir Factura");
        bot_ride.setMetodo("verRide");
        //bot_ride.setAjax(false);
 		//bar_botones.agregarBoton(bot_ride);
        
        Grid gri_botones = new Grid();
		gri_botones.setWidth("100%");
		gri_botones.setColumns(1);		
		gri_botones.getChildren().add(bot_agregarCliente);
		gri_botones.getChildren().add(bot_nuevAnulada);
		gri_botones.getChildren().add(bot_dividirAnulada);
		gri_botones.getChildren().add(bot_crearCliente);
		gri_botones.getChildren().add(bot_actualizar_cliente);
		gri_botones.getChildren().add(bot_ride);
		
		PanelAcordion pac_acordion=new PanelAcordion();
		pac_acordion.setId("pac_acordion");	
		pac_acordion.setRendered(true);
		pac_acordion.setActiveIndex("0,1");
		pac_acordion.agregarPanel("FACTURAR", gri_botones);
		
		gri_lateral.getChildren().add(pac_acordion);
		
		pan_opcion.getChildren().add(div_division2);
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(gri_lateral, pan_opcion, "15%", "V");
		div_division.getDivision1().setCollapsible(true);
		div_division.getDivision1().setHeader("BOTONERA");
		
		agregarComponente(div_division);
	}
	
	
	// Factura Electrónica -----------------------------------------------------
	public void cambiaAmbiente(){
 		if(che_ambiente.getValue().toString().equalsIgnoreCase("true")){
 			ambiente=1; //test
 		}
 		else{
 			ambiente=2; //produccion
 		}
 		
 		System.out.println("cambia ambiente factura: "+ambiente);
 	}
	
	// Abre el diálogo de confirmación para emitir la facturación electrónica
	public void abrirDialogoFacElectronica() {
		// Código del estado de la factura seleccionada
		int estadoFactura = 0;
		try {
			estadoFactura = Integer.valueOf(tab_factura.getValor("ide_coest"));
		} catch (Exception ex) {
		}
		// Estados de la Factura: 2 - Emitido
		// 24 - Emitido Nota Debito

		// Solo se autorizan las facturas emitidas o emitidas nota débito
		if (estadoFactura == 2 || estadoFactura == 24 || estadoFactura == 16) {
			// Limpiando el grid existente
			factura_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 2 columnas para el grid existente
			factura_elec_dialogo.getGri_cuerpo().setColumns(2);

			// Agregando una etiqueta vacía
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(new Etiqueta(""));

			// Agregando una etiqueta con la información de la confirmación
			Etiqueta preguntaConfirmacion = new Etiqueta("¿Desea autorizar la siguiente factura electrónica en el SRI?");
			preguntaConfirmacion.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

			factura_elec_dialogo.getGri_cuerpo().getChildren().add(preguntaConfirmacion);

			// Etiqueta con Estilos Ambiente
			Etiqueta etiqueta1 = new Etiqueta("Ambiente: ");
			etiqueta1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta1);

			// Valor con Estilos
			Etiqueta valor1;

			if (ambiente == 1)
				valor1 = new Etiqueta("PRUEBAS");
			else
				valor1 = new Etiqueta("PRODUCCION");

			valor1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:green;border-width: 0px");

			// Agregando el valor del campo
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(valor1);

			// Etiqueta con Estilos Secuencial
			Etiqueta etiqueta = new Etiqueta("Secuencial: ");
			etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Valor con Estilos
			Etiqueta valor = new Etiqueta(tab_factura.getValor("secuencial_fafac"));
			valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

			// Agregando el valor del campo
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

			// Agregando la función al botón aceptar
			factura_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoFacElectronica");
		} else {
			// Limpiando el grid existente
			factura_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 1 columna para el grid existente
			factura_elec_dialogo.getGri_cuerpo().setColumns(1);

			// Etiqueta
			Etiqueta etiqueta;

			// Mostrando un mensaje con el estado de la factura
			switch (estadoFactura) {
			case 0:
				etiqueta = new Etiqueta("Seleccione una Factura");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 1:
				etiqueta = new Etiqueta("La factura fue anulada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 16:
				etiqueta = new Etiqueta("La factura fue pagada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			default:
				etiqueta = new Etiqueta();
				break;
			}

			// Etiqueta del título
			Etiqueta titulo = new Etiqueta("");
			titulo.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration:none; color:black; border-width: 0px");

			// Agregando las etiquetas dentro del grid
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(titulo);
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(new Etiqueta(""));
			factura_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Agregando la función al botón aceptar
			factura_elec_dialogo.getBot_aceptar().setMetodo("cerrarDialogoFacElectronica");
		}

		// Dibujando en pantalla el diálogo
		factura_elec_dialogo.dibujar();
	}

	// Cierra la confirmación para emitirla facturación electrónica
	public void cerrarDialogoFacElectronica() {
		// Cerrando el diálogo
		factura_elec_dialogo.cerrar();
	}

	// Abre el diálogo con la respuesta del core de facturación (Factura
	// Electrónica)
	public void aceptarDialogoFacElectronica() {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();

		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			respuestaAutorizacion = servicio.procesarFacturaElectronica(ambiente, tab_factura.getValor("secuencial_fafac"), dou_por_iva + "", autorizar);

			respuestaCabecera = respuestaAutorizacion.get(0);
			
			//System.out.println("respuestaCabecera "+respuestaCabecera);
			if(respuestaCabecera.toUpperCase().contains("AUTORIZADO"))
			{
				boolean existeClave=false;
				String clave_acceso=pckUtilidades.CConversion.CStr(respuestaAutorizacion.get(1)).toUpperCase();
				
				try {
					if(clave_acceso.contains("CLAVE DE ACCESO") || clave_acceso.contains("CLAVE ACCESO"))
					{
						String [] strResp=clave_acceso.split("-");
						String [] strRespCA=pckUtilidades.CConversion.CStr(strResp[clave_acceso.contains("AUTORIZACION")?6:1]).split(":");
						clave_acceso=pckUtilidades.CConversion.CStr(strRespCA[1]);
						existeClave=true;
					}
					
				}
				catch(Exception ex)
				{
					System.out.println("Factura Autorizada clave acceso error: "+ex.getMessage());
				}
				
				System.out.println("Factura Autorizada: "+tab_factura.getValor("secuencial_fafac")+" clave_acceso: "+clave_acceso);
				tab_factura.modificar(tab_factura.getFilaActual());
				tab_factura.setValor("autorizada_sri_fafac","true");
				if(existeClave)
					tab_factura.setValor("nro_autorizacion_sri_fac",clave_acceso);
				tab_factura.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_factura");
			}

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replaceAll("Recepcion: ", "");
		} catch (Exception ex) {
			System.out.println("Error aceptarDialogoFacElectronica: "+ex.getMessage());
		}

		// Limpiando el grid existente
		respuesta_core_dialogo.getGri_cuerpo().getChildren().clear();

		// Configurando 2 columnas para el grid existente
		respuesta_core_dialogo.getGri_cuerpo().setColumns(2);

		// Cabecera de la respuesta del core con Estilos
		Etiqueta cabecera = new Etiqueta(respuestaCabecera);
		cabecera.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:green;border-width: 0px");

		// Agregando la etiqueta
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(cabecera);

		// Mensaje del core con Estilos
		Etiqueta mensaje = new Etiqueta(respuestaMensaje);
		mensaje.setEstiloContenido("font-size:15px; border-width: 0px");

		// Ocultando el botón cancelar
		respuesta_core_dialogo.getBot_cancelar().setStyle("width: 0px;height: 0px");

		// Agregando el mensaje de respuesta del core
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(mensaje);

		// Dibujando el mensaje de respuesta del core
		respuesta_core_dialogo.dibujar();

		// Cerrando el diálogo
		factura_elec_dialogo.cerrar();
	}

	// Respuesta del Core de Facturación Electrónica ---------------------------
	// Cierra la respuesta del core de facturación
	public void aceptarDialogoRespuestaCore() {
		// Cerrando el diálogo
		respuesta_core_dialogo.cerrar();
	}

	// FIN Facturacion Electronica - AVACA
	
	public void verRide() 
	 {
		List<String> respuestaRide = new ArrayList<String>();
		
		String nro_autorizacion= pckUtilidades.CConversion.CStr(tab_factura.getValor("nro_autorizacion_sri_fac"));
		String nombreDoc="Factura_"+pckUtilidades.CConversion.CStr(tab_factura.getValor("secuencial_fafac"));

		try {
			if(nro_autorizacion.length()>5)
			{
				respuestaRide = utilitario.consumoRIDEJson( nro_autorizacion );
	
				if(respuestaRide.get(0).toUpperCase().contains("OK"))
				{
					vpdf_rideFac.setVisualizarPDF(respuestaRide.get(1), nombreDoc);			
					vpdf_rideFac.dibujar();
		            utilitario.addUpdate("vpdf_rideFac");
				}
			}
			else
			{
				utilitario.agregarMensajeInfo("Factura no disponible", "La factura electrónica no cuenta con un numero de autorización...");
			}

			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		

			
	}

	public void fechaVencimiento(AjaxBehaviorEvent evt) {
		tab_factura.modificar(evt);

		/*
		 * int
		 * numero_dias=pckUtilidades.CConversion.CInt(utilitario.getVariable(
		 * "p_dias_calculo_interes_mora_nd"));
		 * 
		 * //consorcio linea uno y pecksambiente
		 * if(pckUtilidades.CConversion.CInt
		 * (tab_factura.getValor("ide_recli"))==18149 ||
		 * pckUtilidades.CConversion
		 * .CInt(tab_factura.getValor("ide_recli"))==16806) { numero_dias=15; }
		 */
		// awbecerra
		int numero_dias = ser_facturacion.obtenerDiasPlazo(tab_factura.getValor("ide_recli"));

		String fecha = tab_factura.getValor("fecha_transaccion_fafac");
		Date fecha_a_sumar = utilitario.DeStringADate(fecha);
		Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);

		String str_fecha = utilitario.DeDateAString(nueva_fecha);
		tab_factura.setValor("fecha_vencimiento_fafac", str_fecha);
		utilitario.addUpdateTabla(tab_factura, "fecha_vencimiento_fafac", "");

	}

	public void fechaVencimientoInserta() {

		/*
		 * int
		 * numero_dias=pckUtilidades.CConversion.CInt(utilitario.getVariable(
		 * "p_dias_calculo_interes_mora_nd")); //consorcio linea uno y
		 * pecksambiente
		 * if(pckUtilidades.CConversion.CInt(tab_factura.getValor("ide_recli"
		 * ))==18149 ||
		 * pckUtilidades.CConversion.CInt(tab_factura.getValor("ide_recli"
		 * ))==16806) { numero_dias=15; }
		 */

		// awbecerra
		int numero_dias = ser_facturacion.obtenerDiasPlazo(tab_factura.getValor("ide_recli"));

		String fecha = tab_factura.getValor("fecha_transaccion_fafac");
		Date fecha_a_sumar = utilitario.DeStringADate(fecha);
		Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
		String str_fecha = utilitario.DeDateAString(nueva_fecha);
		tab_factura.setValor("fecha_vencimiento_fafac", str_fecha);
		utilitario.addUpdate("tab_factura");
	}

	public void abrirDialogoCliente(){

		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		//Hace aparecer el componente
		if(aut_factura.getValor()!=null){
			tab_cliente.limpiar();
			tab_cliente.insertar();
			tab_direccion.limpiar();
			tab_direccion.insertar();
			tab_email.limpiar();
			tab_email.insertar();
			crear_cliente_dialogo.dibujar();
		}
		else{
			utilitario.agregarMensaje("Requiere ingresar una factura para crear el cliente", "");
		}

	}

	public void aceptarDialogoCliente(){
		
		if(!habilitaGuardar())
			return;
		
		if(!validarDocumentoIdentidad(tab_cliente.getValor("ide_gttdi"), tab_cliente.getValor("ruc_comercial_recli"))){
			System.out.println("entre a validar cedula");
			tab_cliente.setValor("ruc_comercial_recli","");
			utilitario.addUpdate("tab_clientes");
			return;
			
		}
		
		if (ser_cliente.validarRuc(tab_cliente.getValor("ide_gttdi"), tab_cliente.getValor("ruc_comercial_recli"))) 
		{
			utilitario.agregarMensajeInfo("No se puede guardar",
			"Existe un Cliente Registrado con el número de:"+tab_cliente.getValor("ruc_comercial_recli"));
			return;
		}

		if(tab_cliente.guardar())
		{ //si guarda el gliente cierra el dialogo
			tab_direccion.setValor("ide_recli",  tab_cliente.getValor("ide_recli"));
			tab_email.setValor("ide_recli",  tab_cliente.getValor("ide_recli"));
			if(tab_direccion.guardar())
			{
				if(tab_email.guardar())
				{
					if(guardarPantalla().isEmpty())
					{
						crear_cliente_dialogo.cerrar();	
						tab_factura.actualizarCombos();//actualiza los combos para que aparezca el nuevo cliente	
						if(tab_factura.isFilaInsertada()==false){
							tab_factura.insertar();	
						}
						//CARGA EL CLIENTE Q SE INSERTO
						tab_factura.setValor("ide_recli",  tab_cliente.getValor("ide_recli"));
						tab_factura.setValor("ide_fadaf",  aut_factura.getValor());
	
					}	
				}
			}

		}

	}
	
	public void nuevAnulada()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}

		if (aut_factura.getValor() != null) {
			unirAnuladas=true;
			set_sucursales.dibujar();
		} else {
			utilitario.agregarMensajeError("Debe seleccionar los datos de Facturación", "");
		}
	}
	
	public void divAnulada()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		
		if (aut_factura.getValor() != null) {
			unirAnuladas=false;
			set_sucursales.dibujar();
		} else {
			utilitario.agregarMensajeError("Debe seleccionar los datos de Facturación", "");
		}
	}

	
	public void agregarCliente() {
		//utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		// Hace aparecer el componente
		if (aut_factura.getValor() != null) {
			
			if(dia_cliente_establecimiento.isVisible()){
				if(rad_tipo.getValue()==null){
					utilitario.agregarMensajeInfo("Selecione un Tipo", "");
					return;			
				}
				tipoRecalculo=pckUtilidades.CConversion.CInt(rad_tipo.getValue());
				dia_cliente_establecimiento.cerrar();
				
				if(tipoRecalculo==0)
				{
					set_pantallacliente.getTab_seleccion().setSql(ser_facturacion.getClientesActivos("0,1"));
					set_pantallacliente.getTab_seleccion().ejecutarSql();
					set_pantallacliente.dibujar();
				}
				else
				{
					set_pantallaestablecimiento.getTab_seleccion().setSql(ser_facturacion.getEstablecimientosActivos("true,false"));
					set_pantallaestablecimiento.getTab_seleccion().ejecutarSql();
					set_pantallaestablecimiento.dibujar();
				}
			}
			else
			{
				rad_tipo.setValue(tipoRecalculo);
				dia_cliente_establecimiento.dibujar();
			}
			
		}
		else
			utilitario.agregarMensaje("Seleccione un PUNTO DE FACTURACION", "");
	}

	public void aceptarCliente() {
		String str_seleccionado = tipoRecalculo==0 ? set_pantallacliente.getValorSeleccionado():set_pantallaestablecimiento.getValorSeleccionado();
		System.out.println("Entrar al aceptar" + str_seleccionado);
		if (str_seleccionado != null) {
			// Inserto los cleintes seleccionados en la tabla
			if (tab_factura.isFilaInsertada() == false) {
				// Controla que si ya esta insertada no vuelva a insertar
				tab_factura.insertar();
				tab_factura.setValor("ide_tetid", "4");
				tab_factura.setValor("ide_retip", "4");
				tab_factura.setValor("ide_coest", "2");
				tab_factura.setValor("ide_falug", "24");
				tab_factura.setValor("ide_fadaf",  aut_factura.getValor());
			}

			if(tipoRecalculo==0)
			{
				tab_factura.setValor("ide_recli", str_seleccionado);
				set_pantallacliente.cerrar();
			}
			else
			{
				
				TablaGenerica tab_ide_recli = utilitario.consultar("select ide_reest,ide_recli from rec_clientes_establecimiento where ide_reest="+str_seleccionado);
				if(tab_ide_recli.getTotalFilas()>0)
					tab_factura.setValor("ide_recli", tab_ide_recli.getValor("ide_recli"));
				else
				{
					utilitario.agregarMensajeInfo("Vuelva a intentarlo...", "");
					return;
				}
				tab_factura.setValor("ide_reest", str_seleccionado);
				set_pantallaestablecimiento.cerrar();
			}
			
			tab_factura.modificar(tab_factura.getFilaActual());// para que haga
																// el update
			fechaVencimientoInserta();
			
			utilitario.addUpdate("tab_factura");
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}

	// ACTUALIZAR CLIENTE
	public void actualizarCliente() {

		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		// Hace aparecer el componente
		if (aut_factura.getValor() != null) {
			set_actualizar_cliente.getTab_seleccion().setSql(ser_facturacion.getClientesActivos("0,1"));
			set_actualizar_cliente.getTab_seleccion().ejecutarSql();
			set_actualizar_cliente.dibujar();
		}

	}

	public void modificarCliente() {
		String str_clienteActualizado = set_actualizar_cliente.getValorSeleccionado();
		if (str_clienteActualizado != null) {
			con_guardar_cliente.dibujar();
			utilitario.addUpdate("con_guardar_cliente");
		}

	}

	public void guardarActualizarCliente() {
		if(!habilitaGuardar())
			return;
		
		String str_clienteActualizado = set_actualizar_cliente.getValorSeleccionado();
		tab_factura.modificar(tab_factura.getFilaActual());
		tab_factura.setValor("ide_recli", str_clienteActualizado);
		fechaVencimientoInserta();
		utilitario.addUpdate("tab_factura");

		tab_factura.guardar();
		con_guardar_cliente.cerrar();
		set_actualizar_cliente.cerrar();
		guardarPantalla();
	}

	public void limpiar() {
		valorAut_factura="";
		aut_factura.limpiar();
		tab_factura.limpiar();
		tab_detalle_factura.limpiar();
		utilitario.addUpdate("aut_factura");
	}

	public void cambiaIVA() {
		if (che_iva14.getValue().toString().equalsIgnoreCase("true")) {
			dou_por_iva = 0.14;
		} else {
			dou_por_iva = pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); // 0.12
																									// de
																									// IVA
		}

		System.out.println("cambiaIVA FAC " + dou_por_iva);
	}

	public void aceptarEstablecimiento() {

		String str_ide_est = set_sucursales.getValorSeleccionado();
		if (str_ide_est != null) {
			/*if(unirAnuladas)
				set_factura_inicial.setCheck();
			else
				set_factura_inicial.setRadio();*/
				
			set_factura_inicial.getTab_seleccion().setSql(ser_facturacion.getFacturasAnuladas(str_ide_est));
			set_factura_inicial.getTab_seleccion().ejecutarSql();
			set_factura_inicial.dibujar();
			set_sucursales.cerrar();
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un establecimiento", "");
		}

	}

	public void aceptarFactura() {
		/*if(unirAnuladas)
			str_ide_factura = set_factura_inicial.getSeleccionados();
		else
			str_ide_factura = set_factura_inicial.getValorSeleccionado();*/
		
		str_ide_factura = set_factura_inicial.getSeleccionados();
		
		if(!unirAnuladas)
			if(str_ide_factura.contains(","))
			{
				utilitario.agregarMensajeError("Error división", "Seleccione solamente una factura...");
				return;		
			}
		
		utilitario.agregarMensaje("Generando factura a partir de una anulada. " + str_ide_factura, "");
		System.out.println("str_ide_factura "+str_ide_factura);
		if (str_ide_factura != null) {
			con_generar_Nfactura.dibujar();
			utilitario.addUpdate("con_generar_Nfactura");
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}

	}

	public void cancelarFactura() {
		if (tab_factura.isFocus()) {
			tab_factura.getColumna("ide_fadaf").setValorDefecto(aut_factura.getValor());
			tab_factura.insertar();
		} else if (tab_detalle_factura.isFocus()) {
			if (tab_detalle_factura.getValorForanea().toString().equals("-1")) {
				utilitario.agregarMensaje("No puede insertar", "Debe guardar registro de la factura");
			} else {
				tab_detalle_factura.insertar();
			}
		}
		fechaVencimientoInserta();
		set_sucursales.cerrar();
		set_factura_inicial.cerrar();
	}

	public void generarFactura() {
		
		System.out.println("limpiarAcceso facturas ide_fadaf: " + tab_factura.getValor("ide_fadaf"));
		ser_contabilidad.limpiarAcceso("fac_factura");
		ser_contabilidad.limpiarAcceso("fac_detalle_factura");

		if(!habilitaGuardar())
			return;

		boolean guardar = ser_facturacion.getDatosFacturaModificar(tab_factura.getValor("ide_fadaf"));

		if (guardar) 
		{
			utilitario.agregarMensaje("generar la factura a partir de una anulada y la fecha de vencimiento debe ser el ultimo dia del mes en curso", "");
			// generar la factura a partir de una anulada y la fecha de vencimiento
			// debe ser el ultimo dia del mes en curso
			
			if(unirAnuladas)
			{
				TablaGenerica tab_facturaG = ser_facturacion.getTablaGenericaFactura(str_ide_factura);
				
				if(tab_facturaG.getTotalFilas()>1)
				{
					utilitario.agregarMensajeError("No se pudo generar la nueva factura","Las facturas seleccionadas no son del mismo cliente.");
					return;
				}
				
				TablaGenerica tab_det_factura = ser_facturacion.getTablaGenericaDetFactura(str_ide_factura);
		
				tab_factura.insertar();
				// tab_factura.setValor("ide_fafac", tab_facturaG.getValor(i,
				// "ide_fafac"));
				tab_factura.setValor("ide_fadaf", aut_factura.getValor());
				tab_factura.setValor("ide_sucu", tab_facturaG.getValor( "ide_sucu"));
				tab_factura.setValor("ide_retip", tab_facturaG.getValor( "ide_retip"));
				tab_factura.setValor("ide_recli", tab_facturaG.getValor( "ide_recli"));
				tab_factura.setValor("ide_tetid", tab_facturaG.getValor( "ide_tetid"));
				tab_factura.setValor("ide_coest", "2");
				tab_factura.setValor("fecha_vencimiento_fafac", utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual()));
				tab_factura.setValor("ide_falug", "24"); //matriz
				tab_factura.setValor("num_comprobante_fafac", "0");
				tab_factura.setValor("factura_fisica_fafac", "0");
				tab_factura.setValor("autorizada_sri_fafac", "false");
				tab_factura.setValor("conciliado_fafac", "false");
				tab_factura.setValor("activo_fafac", "true");
				
				tab_factura.setValor("observacion_fafac", pckUtilidades.CConversion.CStr(tab_facturaG.getValor( "observacion_fafac")) + " - Factura consecutiva");
				tab_factura.setValor("base_aprobada_fafac", tab_facturaG.getValor( "base_aprobada_fafac"));
				tab_factura.setValor("valor_iva_fafac", tab_facturaG.getValor( "valor_iva_fafac"));
				tab_factura.setValor("total_fafac", tab_facturaG.getValor( "total_fafac"));
				tab_factura.setValor("descuento_fafac", tab_facturaG.getValor( "descuento_fafac"));
				tab_factura.setValor("irbpnr_fafac", tab_facturaG.getValor( "irbpnr_fafac"));
				
				tab_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
				
				for (int i = 0; i < tab_det_factura.getTotalFilas(); i++) {
					tab_detalle_factura.insertar();
					// tab_detalle_factura.setValor("ide_fadef",
					// tab_det_factura.getValor(i, "ide_fadef"));
					// tab_detalle_factura.setValor("ide_fafac",
					// tab_det_factura.getValor(i, "ide_fafac"));
					//tab_detalle_factura.setValor("ide_fafac", null);
					tab_detalle_factura.setValor("ide_bomat", tab_det_factura.getValor(i, "ide_bomat"));
					tab_detalle_factura.setValor("cantidad_fadef", tab_det_factura.getValor(i, "cantidad_fadef"));
					tab_detalle_factura.setValor("valor_fadef", tab_det_factura.getValor(i, "valor_fadef"));
					tab_detalle_factura.setValor("total_fadef", tab_det_factura.getValor(i, "total_fadef"));
					tab_detalle_factura.setValor("observacion_fadef", tab_det_factura.getValor(i, "observacion_fadef"));
					// tab_detalle_factura.setValor("numero_visita_fadef",
					// str_ide_factura);
					tab_detalle_factura.setValor("numero_visita_fadef", tab_det_factura.getValor(i, "secuencial_fafac"));
					tab_detalle_factura.setValor("activo_fadef", "true");
					tab_detalle_factura.setValor("descuento_fadef", tab_det_factura.getValor(i, "descuento_fadef"));
					tab_detalle_factura.setValor("irbpnr_fadef", tab_det_factura.getValor(i, "irbpnr_fadef"));
				}
				
				//calcular(true);
				//tab_factura.guardar();
				tab_detalle_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
			}
			else
			{
				TablaGenerica tab_facturaG = ser_facturacion.getTablaGenericaFactura(str_ide_factura);			
				TablaGenerica tab_det_factura = ser_facturacion.getTablaGenericaDetFactura(str_ide_factura);
					
				tab_factura.insertar();
				tab_factura.setValor("ide_fadaf", aut_factura.getValor());
				tab_factura.setValor("ide_sucu", tab_facturaG.getValor("ide_sucu"));
				tab_factura.setValor("ide_retip", tab_facturaG.getValor("ide_retip"));
				tab_factura.setValor("ide_recli", tab_facturaG.getValor("ide_recli"));
				tab_factura.setValor("ide_tetid", tab_facturaG.getValor("ide_tetid"));
				tab_factura.setValor("ide_coest", "2");
				tab_factura.setValor("fecha_vencimiento_fafac", utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual()));
				tab_factura.setValor("ide_falug", "24"); //matriz
				tab_factura.setValor("num_comprobante_fafac", "0");
				tab_factura.setValor("factura_fisica_fafac", "0");
				tab_factura.setValor("autorizada_sri_fafac", "false");
				tab_factura.setValor("conciliado_fafac", "false");
				tab_factura.setValor("activo_fafac", "true");			
				tab_factura.setValor("observacion_fafac", pckUtilidades.CConversion.CStr(tab_facturaG.getValor("observacion_fafac")) + " - Factura consecutiva");
				
				tab_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
				
				for (int i = 0; i < tab_det_factura.getTotalFilas(); i++) {
					tab_detalle_factura.insertar();
					tab_detalle_factura.setValor("ide_bomat", tab_det_factura.getValor(i, "ide_bomat"));
					tab_detalle_factura.setValor("cantidad_fadef", pckUtilidades.CConversion.CDbl_2(tab_det_factura.getValor(i, "cantidad_fadef"))/2+"");
					tab_detalle_factura.setValor("valor_fadef", tab_det_factura.getValor(i, "valor_fadef"));
					tab_detalle_factura.setValor("total_fadef", "0");
					tab_detalle_factura.setValor("observacion_fadef", tab_det_factura.getValor(i, "observacion_fadef"));
					tab_detalle_factura.setValor("numero_visita_fadef", tab_det_factura.getValor(i, "secuencial_fafac"));
					tab_detalle_factura.setValor("activo_fadef", "true");
					//tab_detalle_factura.setValor("descuento_fadef", pckUtilidades.CConversion.CDbl_2(tab_det_factura.getValor(i, "descuento_fadef"))/2+"");
					//tab_detalle_factura.setValor("irbpnr_fadef", pckUtilidades.CConversion.CDbl_2(tab_det_factura.getValor(i, "irbpnr_fadef"))/2+"");
					calcular(false);
				}
				
				tab_detalle_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
				calcular(true);			
				tab_factura.guardar();
				guardarPantalla();
				super.actualizar();
				
				System.out.println("Guardando la primera Factura");
				
				tab_factura.insertar();
				tab_factura.setValor("ide_fadaf", aut_factura.getValor());
				tab_factura.setValor("ide_sucu", tab_facturaG.getValor("ide_sucu"));
				tab_factura.setValor("ide_retip", tab_facturaG.getValor("ide_retip"));
				tab_factura.setValor("ide_recli", tab_facturaG.getValor("ide_recli"));
				tab_factura.setValor("ide_tetid", tab_facturaG.getValor("ide_tetid"));
				tab_factura.setValor("ide_coest", "2");
				tab_factura.setValor("fecha_vencimiento_fafac", utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual()));
				tab_factura.setValor("ide_falug", "24"); //matriz
				tab_factura.setValor("num_comprobante_fafac", "0");
				tab_factura.setValor("factura_fisica_fafac", "0");
				tab_factura.setValor("autorizada_sri_fafac", "false");
				tab_factura.setValor("conciliado_fafac", "false");
				tab_factura.setValor("activo_fafac", "true");			
				tab_factura.setValor("observacion_fafac", pckUtilidades.CConversion.CStr(tab_facturaG.getValor("observacion_fafac")) + " - Factura consecutiva");
				
				tab_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
							
				for (int i = 0; i < tab_det_factura.getTotalFilas(); i++) {
					tab_detalle_factura.insertar();
					tab_detalle_factura.setValor("ide_bomat", tab_det_factura.getValor(i, "ide_bomat"));
					tab_detalle_factura.setValor("cantidad_fadef", pckUtilidades.CConversion.CDbl_2(tab_det_factura.getValor(i, "cantidad_fadef"))/2+"");
					tab_detalle_factura.setValor("valor_fadef", tab_det_factura.getValor(i, "valor_fadef"));
					tab_detalle_factura.setValor("total_fadef", "0");
					tab_detalle_factura.setValor("observacion_fadef", tab_det_factura.getValor(i, "observacion_fadef"));
					tab_detalle_factura.setValor("numero_visita_fadef", tab_det_factura.getValor(i, "secuencial_fafac"));
					tab_detalle_factura.setValor("activo_fadef", "true");
					calcular(false);
				}
				
				tab_detalle_factura.guardar();
				guardarPantalla();
				tab_factura.setFocus();
				super.actualizar();
				calcular(true);			
				tab_factura.guardar();
				guardarPantalla();
				super.actualizar();
				
				System.out.println("Guardando la segunda Factura");
				
				utilitario.agregarMensaje("Facturación", "Favor autorizar las facturas de forma manual...");
				
			}
			
			con_generar_Nfactura.cerrar();
			set_factura_inicial.cerrar();
		} else
			utilitario.agregarMensajeInfo("No se pudo guardar", "NO ESTA AUTORIZADO(A). PUNTO ELECTRONICO BLOQUEADO.");

	}

	// METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt) {
		// Cuando selecciona una opcion del autocompletar siempre debe hacerse
		// el onSelect(evt)
		
		aut_factura.onSelect(evt);
		valorAut_factura=aut_factura.getValor();
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		
		tab_factura.setCondicion(" ide_fadaf=" + valorAut_factura +" and extract(year from fecha_transaccion_fafac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and extract(month from fecha_transaccion_fafac)="+com_mes.getValue());
		tab_factura.ejecutarSql();
		tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		
		
	}
	
	public void seleccionaElAnio ()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		
		if( pckUtilidades.CConversion.CInt(valorAut_factura)>0){
			tab_factura.setCondicion(" ide_fadaf=" + valorAut_factura +" and extract(year from fecha_transaccion_fafac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and extract(month from fecha_transaccion_fafac)="+com_mes.getValue());
			tab_factura.ejecutarSql();
			tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un punto de facturación...", "");
		}
	}

	// metodo tieneIvaProducto
	private boolean tieneIvaProducto(String ide_bodtmat) {
		// Declaramos un String con la consulta que vamos a ejecutar
		String str_sql = "Select * from bodt_material where ide_bomat=" + ide_bodtmat;
		// Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta = utilitario.consultar(str_sql);

		// Preguntamos si la tabla no esta vacia es decir que si retorno un
		// resultado la consulta
		if (tab_consulta.isEmpty() == false) {
			// Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_bomat = tab_consulta.getValor("aplica_valor_bomat");
			// Preguntamos si el valor de la variable es true
			if (str_aplica_valor_bomat != null && str_aplica_valor_bomat.equalsIgnoreCase("true")) {
				return true; // Si carga iva
			}
		}
		// System.out.println(tab_consulta.getValor("aplica_valor_bomat"));
		return false; // retorna false
	}

	// metodo tieneIvaProducto
	private boolean AplicaIVA(String ide_bodtmat) {
		// Declaramos un String con la consulta que vamos a ejecutar
		String str_sql = "Select * from bodt_material where ide_bomat=" + ide_bodtmat;
		// Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta = utilitario.consultar(str_sql);

		// Preguntamos si la tabla no esta vacia es decir que si retorno un
		// resultado la consulta
		if (tab_consulta.isEmpty() == false) {
			// Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_bomat = tab_consulta.getValor("iva_bomat");
			// Preguntamos si el valor de la variable es true
			//System.out.println(str_aplica_valor_bomat);
			if (str_aplica_valor_bomat != null && str_aplica_valor_bomat.equals("1")) {
				return true; // Si carga iva
			}
		}
		// System.out.println(tab_consulta.getValor("aplica_valor_bomat"));
		return false; // retorna false
	}

	// Metodo metodo cuando se seleccione algun producto del autocompletar
	public void seleccionoProducto(SelectEvent evt) {
		tab_detalle_factura.modificar(evt); // simepre que se ejecuta un
											// metodoChange
		// Consultamos si el producto seleccionado carga iva
		// boolean
		// boo_iva=tieneIvaProducto(tab_detalle_factura.getValor("ide_bomat"));
		String str_seleccionado = tab_detalle_factura.getValor("ide_bomat");
		TablaGenerica validarTarifaUnica = utilitario.consultar("select ide_recli, aplica_mtarifa_recli from rec_clientes where aplica_mtarifa_recli=false and ide_recli=" + tab_factura.getValor("ide_recli"));
		TablaGenerica retornarValorUnico = utilitario.consultar(" select a.ide_recli, a.aplica_mtarifa_recli, valor_temat from rec_clientes a, tes_material_tarifa c  where  a.ide_tetar= c.ide_tetar  and a.ide_recli=" + tab_factura.getValor("ide_recli")
				+ " and ide_bomat=" + str_seleccionado);
		TablaGenerica retornaValorMultiple = utilitario.consultar("select ide_teclt,a.ide_recli,ide_bomat,valor_temat from tes_cliente_tarifa a, rec_clientes b,tes_material_tarifa c"
				+ " where a.ide_recli = b.ide_recli and a.ide_temat = c.ide_temat and a.ide_recli =" + tab_factura.getValor("ide_recli") + " and ide_bomat=" + str_seleccionado);

		if (validarTarifaUnica.isEmpty()) {
			valor = retornaValorMultiple.getValor("valor_temat");
			System.out.println("Multiple " + valor);

		} else {
			valor = retornarValorUnico.getValor("valor_temat");
			System.out.println("Valor Unico " + valor);
		}

		if (valor != null) {
			tab_detalle_factura.setValor("valor_fadef", valor);

			utilitario.addUpdateTabla(tab_detalle_factura, "valor_fadef", "");

		} else {
			// Mensaje
			utilitario.agregarMensajeInfo("No existen tarifas para el cliente y el articulo seleccionado", "");
		}

		calcular(true);

	}

	// total_fadef
	public void calcular(boolean recalcular) {
		// Variables para almacenar y calcular el total del detalle
		double dou_cantidad_fadef = 0;
		double dou_valor_fadef = 0;
		double dou_total_fadef = 0;
		double dou_descuento_fadef = 0;
		double dou_irbpnr_fadef = 0;

		try {
			// Obtenemos el valor de la cantidad
			dou_cantidad_fadef = pckUtilidades.CConversion.CDbl_n(tab_detalle_factura.getValor("cantidad_fadef"),3);
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_valor_fadef = pckUtilidades.CConversion.CDbl_2(tab_detalle_factura.getValor("valor_fadef"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_descuento_fadef = pckUtilidades.CConversion.CDbl_2(tab_detalle_factura.getValor("descuento_fadef"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_irbpnr_fadef = pckUtilidades.CConversion.CDbl_2(tab_detalle_factura.getValor("irbpnr_fadef"));
		} catch (Exception e) {
		}

		// Calculamos el total
		dou_total_fadef = pckUtilidades.CConversion.CDbl_2((dou_cantidad_fadef * dou_valor_fadef) - dou_descuento_fadef);
		
		// Asignamos el total a la tabla detalle, con 2 decimales
		tab_detalle_factura.setValor("total_fadef", utilitario.getFormatoNumero(dou_total_fadef, 2));// cambiar  a  2  produccion
		//tab_detalle_factura.modificar(tab_detalle_factura.getFilaActual());
		// Actualizamos el campo de la tabla AJAX
		if(recalcular)
		{
			utilitario.addUpdateTabla(tab_detalle_factura, "total_fadef", "tab_factura");		
			calcularFactura();
		}
	}

	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle_factura.modificar(evt); // Siempre es la primera linea
		calcular(true);
	}

	// Vamos a calcular los totales tanto de iva como de valores de toda la
	// factura

	private void calcularFactura() {
		// Enceramos las variables
		dou_base_no_iva = 0;
		dou_base_cero = 0;
		dou_base_aprobada = 0;
		dou_valor_iva = 0;
		dou_total = 0;
		dou_descuento = 0;
		dou_irbpnr = 0;

		// Reccoremos todos los detalles de la factura
		for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
			// Obtenemos si el producto actual tiene iva
			boolean boo_iva = tieneIvaProducto(tab_detalle_factura.getValor(i, "ide_bomat"));
			boolean boo_aplica_iva = AplicaIVA(tab_detalle_factura.getValor(i, "ide_bomat"));

			if (boo_iva) {
				// CARGA IVA
				try {
					// Acumulamos la base aprobada
					dou_base_aprobada += pckUtilidades.CConversion.CDbl_n(tab_detalle_factura.getValor(i, "total_fadef"),2);

					if (boo_aplica_iva) {
						// Acumulamos el valor del iva de cada detalle
						dou_valor_iva += pckUtilidades.CConversion.CDbl_n(pckUtilidades.CConversion.CDbl_n(tab_detalle_factura.getValor(i, "total_fadef"),2) * dou_por_iva,2);
					}

				} catch (Exception e) {
				}
			} else {
				// NO CARGA IVA
				try {
					// Acumulamos la base no iva
					dou_base_no_iva += pckUtilidades.CConversion.CDbl_n(tab_detalle_factura.getValor(i, "total_fadef"),2);
					// Acumulamos la base cero
					dou_base_cero += pckUtilidades.CConversion.CDbl_n(tab_detalle_factura.getValor(i, "total_fadef"),2);
				} catch (Exception e) {
				}
			}
			dou_irbpnr += pckUtilidades.CConversion.CDbl_2(tab_detalle_factura.getValor(i, "irbpnr_fadef"));
			dou_descuento += pckUtilidades.CConversion.CDbl_2(tab_detalle_factura.getValor(i, "descuento_fadef"));
		}

		// dou_total=Double.parseDouble(utilitario.getFormatoNumero(dou_valor_iva,2))+Double.parseDouble(utilitario.getFormatoNumero(dou_base_aprobada,2))+dou_base_no_iva;
		dou_total = pckUtilidades.CConversion.CDbl_n(dou_valor_iva,2) + pckUtilidades.CConversion.CDbl_n(dou_base_aprobada,2) + dou_base_no_iva + dou_irbpnr;
		tab_factura.setValor("base_no_iva_fafac", utilitario.getFormatoNumero(dou_base_no_iva, 2));
		tab_factura.setValor("base_cero_fafac", utilitario.getFormatoNumero(dou_base_cero, 2));
		tab_factura.setValor("base_aprobada_fafac", utilitario.getFormatoNumero((dou_base_aprobada), 2));
		tab_factura.setValor("valor_iva_fafac", utilitario.getFormatoNumero(dou_valor_iva, 2));
		tab_factura.setValor("total_fafac", utilitario.getFormatoNumero(dou_total, 2));
		tab_factura.setValor("descuento_fafac", utilitario.getFormatoNumero(dou_descuento, 2));
		tab_factura.setValor("irbpnr_fafac", utilitario.getFormatoNumero(dou_irbpnr, 2));
		tab_factura.modificar(tab_factura.getFilaActual());// para que haga el
															// update
		eti_total.setValue("TOTAL : " + utilitario.getFormatoNumero(dou_total, 2));
		utilitario.addUpdate("eti_total");
	}

	public void validaDocumento(AjaxBehaviorEvent evt){
		tab_cliente.modificar(evt);
		if(!validarDocumentoIdentidad(tab_cliente.getValor("ide_gttdi"), tab_cliente.getValor("ruc_comercial_recli"))){
			System.out.println("entre a validar cedula");
			tab_cliente.setValor("ruc_comercial_recli","");
			utilitario.addUpdate("tab_clientes");
		}			
	}
	
	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 *         metodo booleano para validar el tipo de documento de identidad
	 *         cedula y ruc
	 */
	public boolean validarDocumentoIdentidad(String ide_gttdi, String documento_identidad) {
		if (ide_gttdi != null && !ide_gttdi.isEmpty()) {
			if (documento_identidad != null && !documento_identidad.isEmpty()) {
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))) {
					if (!utilitario.validarCedula(documento_identidad)) {
						utilitario.agregarMensajeInfo("Atencion", "El numero de cedula ingresado no es valido");
						return false;
					}
				} else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))) {
					if (!utilitario.validarRUC(documento_identidad)) {
						utilitario.agregarMensajeInfo("Atencion", "El numero de RUC ingresado no es valido");
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean habilitaGuardar()
	{
		TablaGenerica tab_anio =utilitario.consultar("select ide_geani,activo_geani,bloqueado_geani from gen_anio where ide_geani ="+com_anio.getValue());
		
		if(tab_anio.getValor("activo_geani").equals("false")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Inactivo");
			return false;
		}
		
		if(tab_anio.getValor("bloqueado_geani").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Bloqueado");
			return false;
		}
		return true;
	}
	
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		
		if (aut_factura.getValor() != null) {
			//utilitario.getTablaisFocus().insertar();
			if(tab_factura.getTotalFilas()>0)
			{
				if (pckUtilidades.CConversion.CBol(tab_factura.getValor("autorizada_sri_fafac"))) {
					utilitario.agregarMensajeError("FACTURA AUTORIZADA.","");
					return;
				}
				tab_detalle_factura.insertar();
			}
			else
				utilitario.agregarMensajeError("Debe seleccionar una factura", "");
		} else {
			utilitario.agregarMensajeError("Debe seleccionar los datos de Facturación", "");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		System.out.println("limpiarAcceso facturas ide_fadaf: " + tab_factura.getValor("ide_fadaf"));
		ser_contabilidad.limpiarAcceso("fac_factura");
		ser_contabilidad.limpiarAcceso("fac_detalle_factura");

		if(!habilitaGuardar())
			return;

		boolean guardar = ser_facturacion.getDatosFacturaModificar(tab_factura.getValor("ide_fadaf"));

		if (guardar) {
			if (!pckUtilidades.CConversion.CBol(tab_factura.getValor("autorizada_sri_fafac"))) {
				if (tab_factura.guardar()) {
	
					if (tab_detalle_factura.guardar()) {
						guardarPantalla();
						tab_factura.ejecutarSql();
	
						if (str_ide_factura != null) {
							System.out.println("str_ide_factura: " + str_ide_factura);
							System.out.println("tab_factura-ide_fafac: " + tab_factura.getValor("ide_fafac"));
							System.out.println("tab_factura-factura_fisica_fafac: " + tab_factura.getValor("factura_fisica_fafac"));
	
							utilitario.getConexion().ejecutarSql("update fac_detalle_factura set ide_fafac=" + tab_factura.getValor("ide_fafac") + " where ide_fafac is null and numero_visita_fadef like '" + tab_factura.getValor("factura_fisica_fafac") + "'");
	
						}
						str_ide_factura = null;
						tab_detalle_factura.ejecutarSql();
					}
				}
			} else
				utilitario.agregarMensajeInfo("No se pudo guardar", "FACTURA AUTORIZADA.");
		} else
			utilitario.agregarMensajeInfo("No se pudo guardar", "NO ESTA AUTORIZADO(A). PUNTO ELECTRONICO BLOQUEADO.");

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_factura.isFocus()) {
			utilitario.agregarMensajeInfo("No puede Eliminar una factura.", "");
		}
		
		if (pckUtilidades.CConversion.CBol(tab_factura.getValor("autorizada_sri_fafac"))) {
			utilitario.agregarMensajeError("FACTURA AUTORIZADA.","");
			return;
		}

		utilitario.getTablaisFocus().eliminar();
		if (tab_detalle_factura.isFocus()) {
			calcularFactura();// calcula los totales
			utilitario.addUpdate("tab_factura"); // actualiza la tabla
			if (tab_factura.isFilaModificada()) {
				// Para que haga el update
				tab_factura.modificar(tab_factura.getFilaActual());
			}
		}

	}

	// REPORTE
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Factura"))
			;
		{
			if (tab_factura.getTotalFilas() > 0) {
				if (rep_reporte.isVisible()) {
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					p_parametros.put("pide_fafac", pckUtilidades.CConversion.CInt(tab_factura.getValor("ide_fafac")));
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					self_reporte.dibujar();
				}
			} else {
				utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una Factura");

			}
		}
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		calcularFactura();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		calcularFactura();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		calcularFactura();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		calcularFactura();
	}

	public Tabla gettab_factura() {
		return tab_factura;
	}

	public void settab_factura(Tabla tab_factura) {
		this.tab_factura = tab_factura;
	}

	public Tabla gettab_detalle_factura() {
		return tab_detalle_factura;
	}

	public void settab_detalle_factura(Tabla tab_detalle_factura) {
		this.tab_detalle_factura = tab_detalle_factura;
	}

	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	public SeleccionTabla getSet_pantallacliente() {
		return set_pantallacliente;
	}

	public void setSet_pantallacliente(SeleccionTabla set_pantallacliente) {
		this.set_pantallacliente = set_pantallacliente;
	}

	public SeleccionTabla getSet_factura_inicial() {
		return set_factura_inicial;
	}

	public void setSet_factura_inicial(SeleccionTabla set_factura_inicial) {
		this.set_factura_inicial = set_factura_inicial;
	}

	public SeleccionTabla getSet_sucursales() {
		return set_sucursales;
	}

	public void setSet_sucursales(SeleccionTabla set_sucursales) {
		this.set_sucursales = set_sucursales;
	}

	public SeleccionTabla getSet_actualizar_cliente() {
		return set_actualizar_cliente;
	}

	public void setSet_actualizar_cliente(SeleccionTabla set_actualizar_cliente) {
		this.set_actualizar_cliente = set_actualizar_cliente;
	}

	public Confirmar getCon_guardar_cliente() {
		return con_guardar_cliente;
	}

	public void setCon_guardar_cliente(Confirmar con_guardar_cliente) {
		this.con_guardar_cliente = con_guardar_cliente;
	}
	public Dialogo getCrear_cliente_dialogo() {
		return crear_cliente_dialogo;
	}


	public void setCrear_cliente_dialogo(Dialogo crear_cliente_dialogo) {
		this.crear_cliente_dialogo = crear_cliente_dialogo;
	}
	public Tabla getTab_cliente() {
		return tab_cliente;
	}
	public void setTab_cliente(Tabla tab_cliente) {
		this.tab_cliente = tab_cliente;
	}
	public Tabla getTab_direccion() {
		return tab_direccion;
	}
	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}
	
	
	public Tabla getTab_email() {
		return tab_email;
	}


	public void setTab_email(Tabla tab_email) {
		this.tab_email = tab_email;
	}


	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}


	public Dialogo getDia_cliente_establecimiento() {
		return dia_cliente_establecimiento;
	}


	public void setDia_cliente_establecimiento(Dialogo dia_cliente_establecimiento) {
		this.dia_cliente_establecimiento = dia_cliente_establecimiento;
	}


	public SeleccionTabla getSet_pantallaestablecimiento() {
		return set_pantallaestablecimiento;
	}


	public void setSet_pantallaestablecimiento(SeleccionTabla set_pantallaestablecimiento) {
		this.set_pantallaestablecimiento = set_pantallaestablecimiento;
	}


	public Panel getPan_opcion() {
		return pan_opcion;
	}


	public void setPan_opcion(Panel pan_opcion) {
		this.pan_opcion = pan_opcion;
	}
	
	

}
