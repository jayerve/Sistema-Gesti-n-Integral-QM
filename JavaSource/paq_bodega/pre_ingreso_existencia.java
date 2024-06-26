package paq_bodega;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_ingreso_existencia extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();
	private SeleccionTabla set_factura = new SeleccionTabla();
	private SeleccionTabla set_factura_cambio = new SeleccionTabla();
	private SeleccionTabla set_factura_detalle = new SeleccionTabla();
	private SeleccionTabla set_certificacion = new SeleccionTabla();
	private SeleccionTabla set_item_catalogo = new SeleccionTabla();

	private Grid gri_partidas = new Grid();

	private Dialogo dia_registrar_catalogo = new Dialogo();
	private Tabla tab_registrar_catalogo = new Tabla();

	private Boolean permitirGuardar = false;

	public static String par_sec_ingreso;

	// Si el valor de la existencia debe ser tomado del costo o del costo_inc_iva
	public Boolean p_valor_existencia_inc_iva;

	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario
			.instanciarEJB(ServicioPresupuesto.class);

	public pre_ingreso_existencia() {
		System.out.println("pre_ingreso_existencia");

		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		par_sec_ingreso = utilitario.getVariable("p_modulo_sec_bodega_ingreso_existencia");
		System.out.println("Secuencial: " + ser_contabilidad.numeroSecuencial(par_sec_ingreso));

		// Si el parametro es 0, false
		p_valor_existencia_inc_iva = utilitario.getVariable("p_modulo_bodega_valor_existencias_inc_iva")
				.equalsIgnoreCase("0") ? false : true;
		System.out.println("p_modulo_bodega_valor_existencias_inc_iva: "
				+ utilitario.getVariable("p_modulo_bodega_valor_existencias_inc_iva"));
		System.out.println("p_valor_existencia_inc_iva: " + p_valor_existencia_inc_iva);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setCondicion("ide_ingeg=-1");
		tab_tabla.setCampoOrden("ide_ingeg desc");
		tab_tabla.setTabla("bodt_ingreso_egreso", "ide_ingeg", 1);
		tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setLectura(true);

		tab_tabla.getColumna("ide_inttr").setCombo("bodt_inventario_tipo_transaccion", "ide_inttr", "detalle_inttr",
				"");

		tab_tabla.getColumna("ide_inttr").setLectura(true);

		// Factura
		tab_tabla.getColumna("ide_adfac").setCombo("adq_factura", "ide_adfac", "num_factura_adfac", "");
		tab_tabla.getColumna("ide_adfac").setAutoCompletar();
		tab_tabla.getColumna("ide_adfac").setLectura(true);

		tab_tabla.getColumna("ide_adfac").setRequerida(true);

		tab_tabla.getColumna("ide_adfac_antigua").setAutoCompletar();
		tab_tabla.getColumna("ide_adfac_antigua").setLectura(true);

		tab_tabla.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp").setLectura(true);

		tab_tabla.getColumna("ide_prcer").setCombo(ser_presupuesto.getCertificacion("true"));
		tab_tabla.getColumna("ide_prcer").setAutoCompletar();
		tab_tabla.getColumna("ide_prcer").setLectura(true);
		tab_tabla.getColumna("ide_prcer").setRequerida(true);

		tab_tabla.getColumna("numero_documento_ingeg").setLectura(true);
		tab_tabla.getColumna("fecha_ingeg").setLectura(true);

		tab_tabla.getColumna("subtotal_ingeg").setLectura(true);
		tab_tabla.getColumna("subtotal_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("subtotal_ingeg").setEtiqueta();

		tab_tabla.getColumna("valor_iva_ingeg").setLectura(true);
		tab_tabla.getColumna("valor_iva_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("valor_iva_ingeg").setEtiqueta();

		tab_tabla.getColumna("total_ingeg").setLectura(true);
		tab_tabla.getColumna("total_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("total_ingeg").setEtiqueta();
		tab_tabla.getSumaColumna("subtotal_ingeg");

		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("DETALLE DEL INGRESO");
		tab_tabla_detalle.setTabla("bodt_ingreso_egreso_det", "ide_inegd", 2);

		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setMetodoChange("cargarCatalogo");
		tab_tabla_detalle.getColumna("ide_bocam").setLectura(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		tab_tabla_detalle.getColumna("ide_inttr").setLectura(true);

		tab_tabla_detalle.getColumna("ide_addef").setLectura(true);

		tab_tabla_detalle.getColumna("cantidad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("subtotal_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("valor_existencia_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("total_inegd").setLectura(true);

		tab_tabla.getColumna("ide_inttr").setNombreVisual("TRANSACCIÓN");
		tab_tabla.getColumna("ide_adfac").setNombreVisual("FACTURA");
		tab_tabla.getColumna("ide_adfac_antigua").setNombreVisual("FACTURA ANTIGUA");
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");
		tab_tabla.getColumna("ide_gtemp").setNombreVisual("EMPLEADO RESPONSABLE");
		tab_tabla.getColumna("ide_prcer").setNombreVisual("NRO CERTIFICACION");
		tab_tabla.getColumna("fecha_ingeg").setNombreVisual("FECHA");
		tab_tabla.getColumna("subtotal_ingeg").setNombreVisual("SUBTOTAL");
		tab_tabla.getColumna("valor_iva_ingeg").setNombreVisual("VALOR IVA");
		tab_tabla.getColumna("total_ingeg").setNombreVisual("TOTAL");
		tab_tabla.getColumna("activo_ingeg").setNombreVisual("ACTIVO");
		tab_tabla.getColumna("numero_documento_ingeg").setNombreVisual("NÚMERO DOCUMENTO");
		tab_tabla.getColumna("observacion_ingeg").setNombreVisual("OBSERVACIÓN");
		tab_tabla.getColumna("numero_proceso_ingeg").setNombreVisual("NÚMERO DE PROCESO");

		tab_tabla_detalle.getColumna("ide_bocam").setNombreVisual("NOMBRE CATALOGO");
		tab_tabla_detalle.getColumna("cantidad_inegd").setNombreVisual("CANTIDAD");
		tab_tabla_detalle.getColumna("subtotal_inegd").setNombreVisual("SUBTOTAL");
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setNombreVisual("COSTO UNITARIO");
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setNombreVisual("COSTO UNITARIO INC IVA");
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setNombreVisual("APLICA IVA");
		tab_tabla_detalle.getColumna("valor_iva_inegd").setNombreVisual("VALOR IVA");
		tab_tabla_detalle.getColumna("total_inegd").setNombreVisual("TOTAL");
		tab_tabla_detalle.getColumna("marca_inegd").setNombreVisual("MARCA");
		tab_tabla_detalle.getColumna("modelo_inegd").setNombreVisual("MODELO");
		tab_tabla_detalle.getColumna("color_inegd").setNombreVisual("COLOR");
		tab_tabla_detalle.getColumna("peligro_salud_inegd").setNombreVisual("PELIGRO SALUD");
		tab_tabla_detalle.getColumna("peligro_inflamabilidad_inegd").setNombreVisual("PELIGRO INFLAMABILIDAD");
		tab_tabla_detalle.getColumna("peligro_reactividad_inegd").setNombreVisual("PELIGRO REACTIVIDAD");
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setNombreVisual("MANEJO ESPECIAL");
		tab_tabla_detalle.getColumna("ide_bounm").setNombreVisual("UNIDAD MEDIDA");
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setNombreVisual("UNIDAD PRESENTACION");
		tab_tabla_detalle.getColumna("ide_afest").setNombreVisual("ESTADO");

		tab_tabla_detalle.getColumna("ide_inegd").setOrden(1);
		tab_tabla_detalle.getColumna("ide_bocam").setOrden(2);
		tab_tabla_detalle.getColumna("cantidad_inegd").setOrden(3);
		tab_tabla_detalle.getColumna("valor_existencia_inegd").setOrden(4);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setOrden(5);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setOrden(6);

		// Ocultar columnas
		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_solicitante").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setVisible(false);
		tab_tabla.getColumna("valor_iva_ingeg").setVisible(false);
		tab_tabla.getColumna("ide_boubi_transferencia").setVisible(false);
		tab_tabla.getColumna("ide_ingeg_ref").setVisible(false);

		tab_tabla_detalle.getColumna("ide_addef").setVisible(true);
		tab_tabla_detalle.getColumna("saldo_disponible_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("ide_inttr").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setVisible(false);

		tab_tabla_detalle.getColumna("marca_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("modelo_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("color_inegd").setLongitud(20);

		tab_tabla_detalle.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_tabla_detalle.getColumna("ide_bounm").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm").setLongitud(20);
		tab_tabla_detalle.getColumna("ide_bounm").setLectura(true);

		tab_tabla_detalle.getColumna("peligro_salud_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_inflamabilidad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_reactividad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLectura(true);

		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLongitud(20);

		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setCombo(ser_bodega.getMedidas());
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLongitud(20);
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLectura(true);

		tab_tabla_detalle.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest",
				"activo_afest=true");
		tab_tabla_detalle.getColumna("ide_afest").setLongitud(15);

		tab_tabla.agregarRelacion(tab_tabla_detalle);

		tab_tabla.setTipoFormulario(true);
		tab_tabla.dibujar();
		tab_tabla_detalle.dibujar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		com_bodega.setCombo(ser_bodega.getBodegas());
		com_bodega.setMetodo("seleccionaParametros");
		com_bodega.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarComponente(new Etiqueta("BODEGA:"));
		bar_botones.agregarComponente(com_bodega);

		ItemMenu editar_item = new ItemMenu();
		editar_item.setValue("Editar Item");
		editar_item.setMetodo("editarItem");
		editar_item.setIcon("ui-icon-mail-closed");

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_tabla_detalle);
		pat_panel_detalle.getMenuTabla().getChildren().add(editar_item);
		pat_panel_detalle.getMenuTabla().getItem_guardar().setRendered(false);
		//pat_panel_detalle.getMenuTabla().getItem_excel().setRendered(false);
		pat_panel_detalle.getMenuTabla().getItem_buscar().setRendered(false);
		pat_panel_detalle.getMenuTabla().getItem_formato().setRendered(true);
		// pat_panel_detalle.getMenuTabla().getItem_eliminar().setRendered(false);
		pat_panel_detalle.getMenuTabla().getItem_importar().setRendered(false);
		pat_panel_detalle.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel_detalle.getMenuTabla().getItem_actualizar().setRendered(false);

		gri_partidas.setId("gri_partidas");
		gri_partidas.setColumns(3);

		Division div_division_partidas = new Division();
		div_division_partidas.setId("div_division_partidas");
		div_division_partidas.dividir2(pat_panel, gri_partidas, "70%", "V");

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(div_division_partidas, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_certificacion = new Boton();
		bot_certificacion.setValue("Certificacion");
		bot_certificacion.setTitle("CERTIFICACION");
		bot_certificacion.setIcon("ui-icon-note");
		bot_certificacion.setMetodo("importarCertificacion");

		Boton bot_factura = new Boton();
		bot_factura.setValue("Factura");
		bot_factura.setTitle("FACTURA");
		bot_factura.setIcon("ui-icon-note");
		bot_factura.setMetodo("importarFactura");

		/*
		 * Boton bot_factura_cambio = new Boton();
		 * bot_factura_cambio.setValue("Cambiar Factura");
		 * bot_factura_cambio.setTitle("CAMBIAR FACTURA");
		 * bot_factura_cambio.setIcon("ui-icon-note");
		 * bot_factura_cambio.setMetodo("importarFacturaCambio");
		 */

		Boton bot_anular = new Boton();
		bot_anular.setValue("Anular");
		bot_anular.setTitle("Anular");
		bot_anular.setIcon("ui-icon-note");
		bot_anular.setMetodo("anularIngreso");

		Boton bot_registrar_catalogo = new Boton();
		bot_registrar_catalogo.setValue("Registrar ítems");
		bot_registrar_catalogo.setTitle("Registrar ítems");
		bot_registrar_catalogo.setIcon("ui-icon-note");
		bot_registrar_catalogo.setMetodo("showRegistrarCatalogo");

		Boton bot_buscar_catalogo = new Boton();
		bot_buscar_catalogo.setValue("Buscar catalogo");
		bot_buscar_catalogo.setTitle("Buscar catalogo");
		bot_buscar_catalogo.setIcon("ui-icon-note");
		bot_buscar_catalogo.setMetodo("buscarItemCatalogo");

		Boton bot_actualizar_partidas = new Boton();
		bot_actualizar_partidas.setValue("Actualizar Resumen Partidas");
		bot_actualizar_partidas.setTitle("Actualizar Resumen Partidas");
		bot_actualizar_partidas.setIcon("ui-icon-note");
		bot_actualizar_partidas.setMetodo("actualizarResumenPartidas");

		tab_registrar_catalogo.setId("tab_registrar_catalogo");
		// tab_registrar_catalogo.setIdCompleto("dia_registrar_catalogo:tab_registrar_catalogo");
		tab_registrar_catalogo.setTabla("bodt_catalogo_material", "ide_bocam", 3);
		tab_registrar_catalogo.setCondicion("ide_bocam=-1");

		tab_registrar_catalogo.setRows(10);

		tab_registrar_catalogo.getColumna("cat_codigo_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("nivel_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("grupo_nivel_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("activo_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("aplica_aprobacion").setVisible(false);
		tab_registrar_catalogo.getColumna("aplica_al_gasto").setVisible(false);
		tab_registrar_catalogo.getColumna("vida_util_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("peligro_salud_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("peligro_inflamabilidad_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("peligro_reactividad_bocam").setVisible(false);
		tab_registrar_catalogo.getColumna("manejo_especial_bocam").setVisible(false);

		tab_registrar_catalogo.getColumna("con_ide_bocam").setNombreVisual("FAMILIA");
		tab_registrar_catalogo.getColumna("descripcion_bocam").setNombreVisual("DESCRIPCIÓN");
		tab_registrar_catalogo.getColumna("ide_prcla").setNombreVisual("CLASIFICADOR PRESUPUESTARIO");
		tab_registrar_catalogo.getColumna("ide_bounm").setNombreVisual("UNIDAD DE MEDIDA");
		tab_registrar_catalogo.getColumna("ide_bounm_presentacion").setNombreVisual("UNIDAD DE MEDIDA PRESENTACIÓN");

		tab_registrar_catalogo.getColumna("con_ide_bocam").setRequerida(true);
		tab_registrar_catalogo.getColumna("descripcion_bocam").setRequerida(true);
		tab_registrar_catalogo.getColumna("ide_prcla").setRequerida(true);
		tab_registrar_catalogo.getColumna("ide_bounm").setRequerida(true);
		tab_registrar_catalogo.getColumna("ide_bounm_presentacion").setRequerida(true);

		tab_registrar_catalogo.getColumna("ide_prcla")
				.setCombo(ser_bodega.getSqlClasificadorPresupuestarioDeCertificacion("-1"));
		tab_registrar_catalogo.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_registrar_catalogo.getColumna("ide_bounm_presentacion").setCombo("bodt_unidad_medida", "ide_bounm",
				"detalle_bounm", "");
		tab_registrar_catalogo.getColumna("con_ide_bocam").setCombo("bodt_catalogo_material", "ide_bocam",
				"descripcion_bocam", "con_ide_bocam = 2");

		tab_registrar_catalogo.getColumna("con_ide_bocam").setMetodoChange("familiaDeCatalogoSeleccionada");

		// tab_registrar_catalogo.setCampoForanea("ide_bocam");

		tab_registrar_catalogo.setTipoFormulario(false);
		tab_registrar_catalogo.setRecuperarLectura(true);
		// tab_registrar_catalogo.setMostrarNumeroRegistros(false);
		// tab_registrar_catalogo.getGrid().setColumns(6);

		// tab_registrar_catalogo.setStyle("width:" +
		// (dia_registrar_catalogo.getAnchoPanel() - 10) + "px; height:" +
		// dia_registrar_catalogo.getAltoPanel() + "px;overflow:auto;display:block;");
		tab_registrar_catalogo.dibujar();

		// tab_registrar_catalogo.setTableStyle("width:" +
		// (dia_registrar_catalogo.getAnchoPanel() - 10) + "px; height:" +
		// dia_registrar_catalogo.getAltoPanel() + "px;overflow:auto;display:block;");
		// PanelTabla pat_panel_catalogo = new PanelTabla();
		// pat_panel_catalogo.setPanelTabla(tab_registrar_catalogo);
		agregarComponente(tab_registrar_catalogo);

		// tab_registrar_catalogo.ejecutarSql();

		// tab_tabla_detalle.agregarRelacion(tab_registrar_catalogo);
		// tab_registrar_catalogo.agregarRelacion(tab_tabla_detalle);

		/*
		 * PanelTabla pnl_registrar_catalogo = new PanelTabla();
		 * pnl_registrar_catalogo.setPanelTabla(tab_registrar_catalogo);
		 */

		// pnl_registrar_catalogo.setStyle("width:" +
		// (dia_registrar_catalogo.getAnchoPanel() - 5) + "px; height:" +
		// dia_registrar_catalogo.getAltoPanel() + "px;overflow:auto;display:block;");

		dia_registrar_catalogo.setId("dia_registrar_catalogo");
		dia_registrar_catalogo.setTitle("Registrar ítems de catálogo faltantes");
		dia_registrar_catalogo.getBot_aceptar().setMetodo("registrarCatalogo");
		dia_registrar_catalogo.setDialogo(tab_registrar_catalogo);
		// dia_registrar_catalogo.getChildren().add(tab_registrar_catalogo);
		dia_registrar_catalogo.setHeight("80%");
		dia_registrar_catalogo.setWidth("96%");
		dia_registrar_catalogo.setDynamic(false); // Si no esta en false no se muestra
		agregarComponente(dia_registrar_catalogo);

		bar_botones.agregarBoton(bot_certificacion);
		bar_botones.agregarBoton(bot_factura);
		// bar_botones.agregarBoton(bot_factura_cambio);
		bar_botones.agregarBoton(bot_registrar_catalogo);
		bar_botones.agregarBoton(bot_actualizar_partidas);
		// bar_botones.agregarBoton(bot_buscar_catalogo);

		System.out.println(utilitario.getVariable("ide_usua"));
		if (utilitario.getVariable("ide_usua").equals("395")|| utilitario.getVariable("ide_usua").equals("10") || utilitario.getVariable("ide_usua").equals("7")) { // dluje, julio mendoza
			bar_botones.agregarBoton(bot_anular);
			bar_botones.agregarBoton(bot_actualizar_partidas);
		}

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_bodega.getFacturas(), "ide_adfac");

		set_factura.setTitle("Seleccione Factura");
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.setRadio();
		agregarComponente(set_factura);

		set_factura_cambio.setId("set_factura_cambio");
		set_factura_cambio.setSeleccionTabla(ser_bodega.getFacturas(), "ide_adfac");

		set_factura_cambio.setTitle("Seleccione Nueva Factura");
		set_factura_cambio.getBot_aceptar().setMetodo("aceptarFacturaCambio");
		set_factura_cambio.setRadio();
		agregarComponente(set_factura_cambio);

		set_item_catalogo.setId("set_item_catalogo");
		set_item_catalogo.setSeleccionTabla(ser_bodega.getCatalogoExistenciasItemsParaBusqueda(), "ide_bocam");
		set_item_catalogo.setTitle("Seleccione el ítem del catálogo");
		set_item_catalogo.getBot_aceptar().setMetodo("aceptarItemCatalogo");
		set_item_catalogo.setRadio();
		set_item_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();
		set_item_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setNombreVisual("Catálogo");
		agregarComponente(set_item_catalogo);

		set_certificacion.setId("set_certificacion");
		set_certificacion.setSeleccionTabla(ser_presupuesto.getCertificacion("0", "true"), "ide_prcer");
		set_certificacion.getTab_seleccion().getColumna("nro_certificacion_prcer").setFiltro(true);
		set_certificacion.getTab_seleccion().getColumna("detalle_prcer").setFiltro(true);

		set_certificacion.setTitle("Seleccione la Certificación Presupuestaria");
		set_certificacion.getBot_aceptar().setMetodo("aceptarCertificacion");
		set_certificacion.setRadio();
		agregarComponente(set_certificacion);

		set_factura_detalle.setId("set_factura_detalle");
		set_factura_detalle.setSeleccionTabla(ser_bodega.getFacturaDetalle("-1"), "ide_addef");
		set_factura.getTab_seleccion().getColumna("NUM_FACTURA_ADFAC").setFiltroContenido();
		set_factura.getTab_seleccion().getColumna("FECHA_FACTURA_ADFAC").setFiltroContenido();
		set_factura.getTab_seleccion().getColumna("DETALLE_ADFAC").setFiltroContenido();
		set_factura.getTab_seleccion().getColumna("NOMBRE_TEPRO").setFiltroContenido();

		set_factura_detalle.setTitle("Seleccione los Items de la Factura");
		set_factura_detalle.getBot_aceptar().setMetodo("aceptarFacturaDetalle");

		agregarComponente(set_factura_detalle);
		tab_tabla.onSelect("actualizarResumenPartidas");
		actualizarResumenPartidas();
	}

	public int getFilaIndexFromSelectedCombo(UIComponent component) {
		try {
			// Intentar obtener el indice desde el id
			String[] array = component.getClientId().split(":");
			String index = array[array.length - 1 - 1];
			int respuesta = Integer.parseInt(index);
			return respuesta;
		} catch (Exception e) {
			if (component.getParent() instanceof Tabla) {
				Tabla tabla = (Tabla) component.getParent();
				return tabla.getFilaActual();
			}
		}
		return -1;
	}

	public void familiaDeCatalogoSeleccionada(AjaxBehaviorEvent event) {

		int index = getFilaIndexFromSelectedCombo((UIComponent) event.getSource());
		if (index < 0) {
			index = tab_registrar_catalogo.getFilaActual();
		}
		String ide_bocam = tab_registrar_catalogo.getValor(index, "con_ide_bocam");
		System.out.println("ide_bocam: " + ide_bocam);
		System.out.println("index: " + index);
		tab_registrar_catalogo.setFilaActual(index);
		TablaGenerica tg_catalogo_familia = ser_bodega.getCatalogoPorId(ide_bocam);

		if (tg_catalogo_familia.getTotalFilas() > 0) {
			String ide_prcla = tg_catalogo_familia.getValor("ide_prcla");
			String ide_bounm = tg_catalogo_familia.getValor("ide_bounm");
			String ide_bounm_presentacion = tg_catalogo_familia.getValor("ide_bounm_presentacion");
			String cat_codigo_bocam = tg_catalogo_familia.getValor("cat_codigo_bocam");
			tab_registrar_catalogo.setValor(index, "ide_prcla", ide_prcla);
			tab_registrar_catalogo.setValor(index, "ide_bounm", ide_bounm);
			tab_registrar_catalogo.setValor(index, "ide_bounm_presentacion", ide_bounm_presentacion);
			tab_registrar_catalogo.setValor(index, "cat_codigo_bocam", cat_codigo_bocam);
			System.out.println("Asignado ide_prcla: " + ide_prcla);
		}
		utilitario.addUpdate("tab_registrar_catalogo");
	}

	public void precargarItemsParaCrearCatalogo() {
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
			if (ide_bocam == null || ide_bocam == "") {
				System.out.println("Intentando precagar: " + i);
				// Si no se ha seleccionado un ítem
				tab_registrar_catalogo.insertar();
				String ide_addef = tab_tabla_detalle.getValor(i, "ide_addef");
				TablaGenerica tg_facdet = ser_bodega.getTablaGenericaFacturaDetalle(ide_addef);

				tab_registrar_catalogo.setValor("descripcion_bocam", tg_facdet.getValor("descripcion_addef"));
				tab_registrar_catalogo.setValor("activo_bocam", Boolean.TRUE.toString());

			}
		}
		utilitario.addUpdate("tab_registrar_catalogo");
		actualizarResumenPartidas();
	}

	public void actualizarResumenPartidas() {
		System.out.println("actualizarResumenPartidas");
		String style_bold = "font-weight: bold;";
		gri_partidas.getChildren().clear();
		Etiqueta lbl_partida = new Etiqueta("Partida");
		lbl_partida.setStyle(style_bold);
		gri_partidas.getChildren().add(lbl_partida);

		Etiqueta lbl_valor_ingreso = new Etiqueta("Valor Ingreso");
		lbl_valor_ingreso.setStyle(style_bold);
		gri_partidas.getChildren().add(lbl_valor_ingreso);

		Etiqueta lbl_valor_certificado = new Etiqueta("Valor Certificado");
		lbl_valor_certificado.setStyle(style_bold);
		gri_partidas.getChildren().add(lbl_valor_certificado);

		Map<String, Double> detalleCertificacion = detalleCertificacion();
		Map<String, Double> sumatoriaPartidas = sumatoriaPartidas();

		LinkedHashSet<String> partidas = new LinkedHashSet<String>();
		for (Map.Entry<String, Double> item : detalleCertificacion.entrySet()) {
			partidas.add(item.getKey());
		}
		for (Map.Entry<String, Double> item : sumatoriaPartidas.entrySet()) {
			partidas.add(item.getKey());
		}
		// Formar el grid
		System.out.println("partidas: " + partidas.toString());
		BigDecimal totalActual = BigDecimal.ZERO;
		BigDecimal totalCertificado = BigDecimal.ZERO;

		for (String ide_prcla : partidas) {
			TablaGenerica tg_clasificador = ser_bodega.getTablaGenericaPreClasificacionPorId(ide_prcla);
			String partida = tg_clasificador.getValor("codigo_clasificador_prcla");

			Etiqueta lbl_partida_i = new Etiqueta(partida != null ? partida : "");
			lbl_partida_i.setStyle(style_bold);
			gri_partidas.getChildren().add(lbl_partida_i);

			Etiqueta lbl_valor_actual_i = new Etiqueta();
			lbl_valor_actual_i.setStyle("text-align: center;");

			BigDecimal valorActual = BigDecimal.ZERO;
			BigDecimal valorCertificado = BigDecimal.ZERO;

			if (sumatoriaPartidas.containsKey(ide_prcla)) {
				valorActual = new BigDecimal(sumatoriaPartidas.get(ide_prcla)).setScale(2, RoundingMode.HALF_UP);
			}
			lbl_valor_actual_i.setValue(valorActual.toString());
			gri_partidas.getChildren().add(lbl_valor_actual_i);

			if (detalleCertificacion.containsKey(ide_prcla)) {
				valorCertificado = new BigDecimal(detalleCertificacion.get(ide_prcla)).setScale(2,
						RoundingMode.HALF_UP);
			}

			if (valorActual.compareTo(valorCertificado) > 0) {
				lbl_valor_actual_i.setStyle(lbl_valor_actual_i.getStyle() + "color: red;");
			} else if (valorActual.compareTo(valorCertificado) == 0) {
				lbl_valor_actual_i.setStyle(lbl_valor_actual_i.getStyle() + "color: blue;");
			}
			gri_partidas.getChildren().add(new Etiqueta(valorCertificado.toString()));

			totalActual = totalActual.add(valorActual);
			totalCertificado = totalCertificado.add(valorCertificado);

		}
		Etiqueta lbl_total = new Etiqueta("TOTAL");
		lbl_total.setStyle(style_bold);
		gri_partidas.getChildren().add(lbl_total);

		Etiqueta lbl_total_actual = new Etiqueta(totalActual.toString());
		lbl_total_actual.setStyle(style_bold + "text-decoration: underline;");
		gri_partidas.getChildren().add(lbl_total_actual);

		Etiqueta lbl_total_certificado = new Etiqueta(totalCertificado.toString());
		lbl_total_certificado.setStyle(style_bold + "text-decoration: underline;");
		gri_partidas.getChildren().add(lbl_total_certificado);

		utilitario.addUpdate("gri_partidas");

	}

	public void showRegistrarCatalogo() {
		tab_registrar_catalogo.limpiar();
		dia_registrar_catalogo.dibujar();
		precargarItemsParaCrearCatalogo();

	}

	public void registrarItemCatalogo(String con_ide_bocam, String cat_codigo_bocam, String descripcion_bocam,
			String ide_prcla, String ide_bounm, String ide_bounm_presentacion) {

		String activo_bocam = "true";
		String usuario_ingre = utilitario.getVariable("ide_usua");
		String fecha_ingre = utilitario.getFechaActual();
		String hora_ingre = utilitario.getHoraActual();

		String sql = String.format(
				"INSERT INTO bodt_catalogo_material( " + "            ide_bocam, "
						+ "            con_ide_bocam, cat_codigo_bocam, descripcion_bocam, "
						+ "            activo_bocam, " + "            usuario_ingre, fecha_ingre, hora_ingre, "
						+ "            ide_prcla, ide_bounm, ide_bounm_presentacion) "
						+ "    VALUES ((SELECT MAX(ide_bocam) + 1 FROM bodt_catalogo_material ), "
						+ "            %s, '%s', '%s', " + "            %s, " + "            '%s', '%s', '%s', "
						+ "            %s, %s, %s);",
				con_ide_bocam, cat_codigo_bocam, descripcion_bocam, activo_bocam, usuario_ingre, fecha_ingre,
				hora_ingre, ide_prcla, ide_bounm, ide_bounm_presentacion);
		System.out.println(sql);
		utilitario.getConexion().ejecutarSql(sql);
	}

	public void registrarCatalogo() {
		for (int i = 0; i < tab_registrar_catalogo.getTotalFilas(); i++) {
			String con_ide_bocam = tab_registrar_catalogo.getValor(i, "con_ide_bocam");
			String cat_codigo_bocam = tab_registrar_catalogo.getValor(i, "cat_codigo_bocam");
			String descripcion_bocam = tab_registrar_catalogo.getValor(i, "descripcion_bocam");
			String ide_prcla = tab_registrar_catalogo.getValor(i, "ide_prcla");
			String ide_bounm = tab_registrar_catalogo.getValor(i, "ide_bounm");
			String ide_bounm_presentacion = tab_registrar_catalogo.getValor(i, "ide_bounm_presentacion");
			registrarItemCatalogo(con_ide_bocam, cat_codigo_bocam, descripcion_bocam, ide_prcla, ide_bounm,
					ide_bounm_presentacion);
		}
		buscarItemCatalogo();
		dia_registrar_catalogo.cerrar();

	}

	public void editarItem() {
		if (tab_tabla_detalle.getFilaSeleccionada() != null) {
			set_item_catalogo.getTab_seleccion().setSql(ser_bodega.getCatalogoExistenciasItemsParaBusqueda());
			set_item_catalogo.getTab_seleccion().ejecutarSql();
			set_item_catalogo.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un item", "");
		}
	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {

		if (rep_reporte.getReporteSelecionado().equals("Comprobante ingreso")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo",
						"INGRESO DE EXISTENCIAS No. " + tab_tabla.getValor("numero_documento_ingeg"));
				p_parametros.put("ide_ingeg", pckUtilidades.CConversion.CInt(tab_tabla.getValor("ide_ingeg")));
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);

				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		}
	}

	public void aceptarItemCatalogo() {
		// Cuando se de clic en Aceptar en el dialogo
		String str_seleccionado = set_item_catalogo.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla_detalle.setValor("ide_bocam", str_seleccionado);
			String ide_bocam = str_seleccionado;

			TablaGenerica tg = ser_bodega.getCatalogoPorId(ide_bocam);

			tab_tabla_detalle.setValor("ide_bounm", tg.getValor("ide_bounm"));

			tab_tabla_detalle.setValor("ide_bounm_presentacion", tg.getValor("ide_bounm_presentacion"));
			tab_tabla_detalle.setValor("peligro_salud_inegd", tg.getValor("peligro_salud_bocam"));
			tab_tabla_detalle.setValor("peligro_inflamabilidad_inegd", tg.getValor("peligro_inflamabilidad_bocam"));
			tab_tabla_detalle.setValor("peligro_reactividad_inegd", tg.getValor("peligro_reactividad_bocam"));
			tab_tabla_detalle.setValor("manejo_especial_inegd", tg.getValor("manejo_especial_bocam"));

			// utilitario.addUpdate("tab_tabla_detalle");

			tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_item_catalogo.cerrar();
		utilitario.addUpdate("tab_tabla_detalle");
	}

	public void aceptarFactura() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_factura.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_adfac", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_factura.cerrar();
		utilitario.addUpdate("tab_tabla");

		importarFacturaDetalle();
	}

	public void aceptarFacturaCambio() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_factura_cambio.getValorSeleccionado();
		if (str_seleccionado != null) {
			String ide_adfac = tab_tabla.getValor("ide_adfac");
			tab_tabla.setValor("ide_adfac_antigua", ide_adfac);
			tab_tabla.setValor("ide_adfac", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_factura_cambio.cerrar();
		utilitario.addUpdate("tab_tabla");
		permitirGuardar = true;

	}

	public void aceptarCertificacion() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_certificacion.getValorSeleccionado();
		if (str_seleccionado != null) {
			String ide_prcer = str_seleccionado;
			tab_tabla.setValor("ide_prcer", ide_prcer);
			tab_tabla.modificar(tab_tabla.getFilaActual());

			tab_registrar_catalogo.getColumna("con_ide_bocam")
					.setCombo(ser_bodega.getFamiliasDeCatalogoPorCertificacion(ide_prcer).getSql());
			tab_registrar_catalogo.getColumna("ide_prcla")
					.setCombo(ser_bodega.getSqlClasificadorPresupuestarioDeCertificacion(ide_prcer));

		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_certificacion.cerrar();
		utilitario.addUpdate("tab_tabla");
		actualizarResumenPartidas();

	}

	public void cargarCatalogo(AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);

		String ide_bocam = tab_tabla_detalle.getValor("ide_bocam");
		System.out.println("selected ide_bocam:" + ide_bocam);
		TablaGenerica tg = ser_bodega.getCatalogoPorId(tab_tabla_detalle.getValor("ide_bocam"));

		String vidaUtil = tg.getValor("vida_util_bocam");
		String fechaVencimiento = tab_tabla_detalle.getValor("fecha_vencimiento_inegd");

		if (vidaUtil != null) {
			if (!vidaUtil.isEmpty() && !vidaUtil.equals("null") && !vidaUtil.equals("0.0000")) {
				if (fechaVencimiento == null || fechaVencimiento == "null" || fechaVencimiento.isEmpty()) {
					Date fechaActual = utilitario.getFecha(utilitario.getFechaActual());
					Calendar cal = Calendar.getInstance();
					cal.setTime(fechaActual);
					Double vida = Double.parseDouble(vidaUtil);
					Integer vidaInt = vida.intValue();
					if (vidaInt > 0) {
						cal.add(Calendar.DAY_OF_MONTH, vidaInt);
						SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
						String nuevaFecha = formatoFecha.format(cal.getTime());
						tab_tabla_detalle.setValor("fecha_vencimiento_inegd", nuevaFecha);
					}
				}
			}
		}
		tab_tabla_detalle.setValor("ide_bounm", tg.getValor("ide_bounm"));

		tab_tabla_detalle.setValor("ide_bounm_presentacion", tg.getValor("ide_bounm_presentacion"));
		tab_tabla_detalle.setValor("peligro_salud_inegd", tg.getValor("peligro_salud_bocam"));
		tab_tabla_detalle.setValor("peligro_inflamabilidad_inegd", tg.getValor("peligro_inflamabilidad_bocam"));
		tab_tabla_detalle.setValor("peligro_reactividad_inegd", tg.getValor("peligro_reactividad_bocam"));
		tab_tabla_detalle.setValor("manejo_especial_inegd", tg.getValor("manejo_especial_bocam"));

		utilitario.addUpdate("tab_tabla_detalle");
	}

	public void aceptarFacturaDetalle() {

		String str_seleccionados = set_factura_detalle.getTab_seleccion().getFilasSeleccionadas();

		if (str_seleccionados != null) {
			TablaGenerica tab_facdet = ser_bodega.getTablaGenericaFacturaDetalle(str_seleccionados);
			Double subtotal = (double) 0;
			Double valor_iva = (double) 0;
			Double total = (double) 0;

			for (int i = 0; i < tab_facdet.getTotalFilas(); i++) {
				tab_tabla_detalle.insertar();
				tab_tabla_detalle.setValor("ide_ingeg", tab_tabla.getValor("ide_ingeg"));
				tab_tabla_detalle.setValor("ide_inttr", tab_tabla.getValor("ide_inttr"));
				tab_tabla_detalle.setValor("ide_addef", tab_facdet.getValor(i, "ide_addef"));
				tab_tabla_detalle.setValor("cantidad_inegd", tab_facdet.getValor(i, "cantidad_addef"));
				tab_tabla_detalle.setValor("costo_unitario_inegd", tab_facdet.getValor(i, "valor_unitario_addef"));

				tab_tabla_detalle.setValor("aplica_iva_inegd", tab_facdet.getValor(i, "aplica_iva_adfac"));
				tab_tabla_detalle.setValor("ide_afest", ser_bodega.ESTADO_PREDETERMINADO.toString());

				try {
					double cantidad = Double.parseDouble(tab_facdet.getValor(i, "cantidad_addef").toString());
					double valor_unitario = Double
							.parseDouble(tab_facdet.getValor(i, "valor_unitario_addef").toString());
					double valor_unitario_inc_iva = valor_unitario;
					Boolean aplica_iva = Boolean.valueOf(tab_facdet.getValor(i, "aplica_iva_adfac"));
					tab_tabla_detalle.setValor("subtotal_inegd",
							utilitario.getFormatoNumero(cantidad * valor_unitario, 2));
					subtotal = subtotal + pckUtilidades.CConversion.CDbl_2(cantidad * valor_unitario);

					double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));
					if (aplica_iva) {
						valor_iva = valor_iva + (cantidad * valor_unitario * porcenjate_iva);
						valor_unitario_inc_iva = valor_unitario * (1 + porcenjate_iva);
					}
					tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd", String.valueOf(valor_unitario_inc_iva));
					tab_tabla_detalle.setValor("valor_iva_inegd",
							utilitario.getFormatoNumero(cantidad * valor_unitario * porcenjate_iva, 2));

					if (p_valor_existencia_inc_iva) {

						tab_tabla_detalle.setValor("valor_existencia_inegd", String.valueOf(valor_unitario_inc_iva));
						tab_tabla_detalle.setValor("total_inegd", utilitario.getFormatoNumero(
								pckUtilidades.CConversion.CDbl_2(cantidad * valor_unitario_inc_iva), 2));
					} else {

						tab_tabla_detalle.setValor("valor_existencia_inegd",
								tab_facdet.getValor(i, "valor_unitario_addef"));
						tab_tabla_detalle.setValor("total_inegd", utilitario
								.getFormatoNumero(pckUtilidades.CConversion.CDbl_2(cantidad * valor_unitario), 2));
					}

					String ide_prcer = tab_tabla.getValor("ide_prcer");
					TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogoDeFacturaDetalleYCertificacion(
							tab_facdet.getValor(i, "ide_addef"), ide_prcer);
					if (tg_catalogo.getTotalFilas() > 0) {
						String ide_bocam = tg_catalogo.getValor("ide_bocam");
						tab_tabla_detalle.setValor("ide_bocam", ide_bocam);
						tab_tabla_detalle.setValor("ide_bounm", tg_catalogo.getValor("ide_bounm"));
						tab_tabla_detalle.setValor("ide_bounm_presentacion",
								tg_catalogo.getValor("ide_bounm_presentacion"));
						tab_tabla_detalle.setValor("peligro_salud_inegd", tg_catalogo.getValor("peligro_salud_bocam"));
						tab_tabla_detalle.setValor("peligro_inflamabilidad_inegd",
								tg_catalogo.getValor("peligro_inflamabilidad_bocam"));
						tab_tabla_detalle.setValor("peligro_reactividad_inegd",
								tg_catalogo.getValor("peligro_reactividad_bocam"));
						tab_tabla_detalle.setValor("manejo_especial_inegd",
								tg_catalogo.getValor("manejo_especial_bocam"));
					}
				} catch (NumberFormatException e) {
					utilitario.agregarMensajeError("Error", e.getMessage());
				}
			}

			if (p_valor_existencia_inc_iva) {
				System.out.println("valor existencia inc iva");
				total = subtotal + valor_iva;
			} else {
				System.out.println("valor existencia sin iva");
				total = subtotal;
			}

			tab_tabla.setValor("subtotal_ingeg", utilitario.getFormatoNumero(subtotal, 2));
			tab_tabla.modificar(tab_tabla.getFilaActual());
			tab_tabla.setValor("valor_iva_ingeg", utilitario.getFormatoNumero(valor_iva, 2));
			tab_tabla.modificar(tab_tabla.getFilaActual());
			tab_tabla.setValor("total_ingeg", utilitario.getFormatoNumero(total, 2));

			tab_tabla.modificar(tab_tabla.getFilaActual());

			utilitario.addUpdate("tab_tabla_detalle");
			utilitario.addUpdate("tab_tabla");

			set_factura_detalle.cerrar();
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}

	}

	public void buscarItemCatalogo() {
		String ide_prcer = tab_tabla.getValor("ide_prcer");
		tab_tabla_detalle.getColumna("ide_bocam").actualizarCombo();
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			if (tab_tabla_detalle.getValor(i, "ide_bocam") == null
					|| tab_tabla_detalle.getValor(i, "ide_bocam") == "") {
				// Cargar solo de los que no tienen asignado un ide_bocam
				System.out.println("Buscando de ide_addef: " + tab_tabla_detalle.getValor(i, "ide_addef"));

				TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogoDeFacturaDetalleYCertificacion(
						tab_tabla_detalle.getValor(i, "ide_addef"), ide_prcer);
				if (tg_catalogo.getTotalFilas() > 0) {
					tab_tabla_detalle.setFilaActual(i);

					// tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
					String ide_bocam = tg_catalogo.getValor("ide_bocam");
					System.out.println("Asignando a " + i + " ide_bocam: " + ide_bocam);
					tab_tabla_detalle.setValor(i, "ide_bocam", ide_bocam);
					tab_tabla_detalle.setValor(i, "ide_bounm", tg_catalogo.getValor("ide_bounm"));
					tab_tabla_detalle.setValor(i, "ide_bounm_presentacion",
							tg_catalogo.getValor("ide_bounm_presentacion"));
					tab_tabla_detalle.setValor(i, "peligro_salud_inegd", tg_catalogo.getValor("peligro_salud_bocam"));
					tab_tabla_detalle.setValor(i, "peligro_inflamabilidad_inegd",
							tg_catalogo.getValor("peligro_inflamabilidad_bocam"));
					tab_tabla_detalle.setValor(i, "peligro_reactividad_inegd",
							tg_catalogo.getValor("peligro_reactividad_bocam"));
					tab_tabla_detalle.setValor(i, "manejo_especial_inegd",
							tg_catalogo.getValor("manejo_especial_bocam"));
					tab_tabla_detalle.modificar(i);
					utilitario.addUpdate("tab_tabla_detalle");
					// tab_tabla_detalle.guardar();
					// utilitario.addUpdate("tab_tabla");
				} else {
					System.out.println("No se encontro ide_addef:" + tab_tabla_detalle.getValor(i, "ide_addef")
							+ " ide_prcer: " + ide_prcer);
				}
			} else {
				System.out.println("Ya tenia un valor previo: " + i);
			}

		}
		utilitario.addUpdate("tab_tabla_detalle");
	}

	public Map<String, Double> detalleCertificacion() {
		HashMap<String, Double> detalleCertificacion = new HashMap<String, Double>();
		TablaGenerica tg_certificacion = ser_bodega
				.getDetalleCertificacionPresupuestaria(tab_tabla.getValor("ide_prcer"));
		for (int i = 0; i < tg_certificacion.getTotalFilas(); i++) {
			try {
				double valorCertificado = Double.parseDouble(tg_certificacion.getValor(i, "valor_certificado"));
				detalleCertificacion.put(tg_certificacion.getValor(i, "ide_prcla"), valorCertificado);
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		}
		return detalleCertificacion;
	}

	public Map<String, Double> sumatoriaPartidas() {
		HashMap<String, Double> sumatoriaPartidas = new HashMap<String, Double>();
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			TablaGenerica tg_catalogo = ser_bodega.getCatalogoPorId(tab_tabla_detalle.getValor(i, "ide_bocam"));
			String ide_prcla = tg_catalogo.getValor("ide_prcla");

			// Sumar el subtotal por partidas
			try {
				double valor = Double.parseDouble(tab_tabla_detalle.getValor(i, "total_inegd"));
				if (sumatoriaPartidas.containsKey(ide_prcla)) {
					sumatoriaPartidas.put(ide_prcla, sumatoriaPartidas.get(ide_prcla) + valor);
				} else {
					sumatoriaPartidas.put(ide_prcla, valor);
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}

		}
		return sumatoriaPartidas;
	}

	public Boolean validarPartidas() {
		if (tab_tabla.getValor("ide_prcer").isEmpty()) {
			return false;
		}

		TablaGenerica tg_certificacion = ser_bodega
				.getDetalleCertificacionPresupuestaria(tab_tabla.getValor("ide_prcer"));

		HashMap<String, Double> detalleCertificacion = new HashMap<String, Double>();

		for (int i = 0; i < tg_certificacion.getTotalFilas(); i++) {
			try {
				double valorCertificado = Double.parseDouble(tg_certificacion.getValor(i, "valor_certificado"));
				detalleCertificacion.put(tg_certificacion.getValor(i, "ide_prcla"), valorCertificado);
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		}

		HashMap<String, Double> sumatoriaPartidas = new HashMap<String, Double>();

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			TablaGenerica tg_catalogo = ser_bodega.getCatalogoPorId(tab_tabla_detalle.getValor(i, "ide_bocam"));
			String ide_prcla = tg_catalogo.getValor("ide_prcla");
			if (ide_prcla == null || ide_prcla.isEmpty()) {
				System.out.println("Error en ide_bocam: " + tab_tabla_detalle.getValor(i, "ide_bocam"));
				String descripcion = tg_catalogo.getValor("descripcion_bocam");
				utilitario.agregarMensajeError("Error", "El ítem del catálogo " + tg_catalogo.getValor("ide_bocam")
						+ " : " + descripcion + " no tiene asociado un clasificador presupuestario");
				return false;
			}
			TablaGenerica tg_clasificador = ser_bodega.getTablaGenericaPreClasificacionPorId(ide_prcla);
			String codigo_clasificador_prcla = tg_clasificador.getValor("codigo_clasificador_prcla");
			
			// Ticket #145103 
			// Verificar si las partidas corresponden a 53.08.*, 73.08.*
			if (!(codigo_clasificador_prcla.startsWith("53.08.") || codigo_clasificador_prcla.startsWith("73.08."))) {
				utilitario.agregarMensajeError("", "La partida " + codigo_clasificador_prcla + " no corresponde a una partida de existencias.");
				return false;
			}
			
			// Verificar si la partida del item del catalogo esta en la certificación
			if (!detalleCertificacion.containsKey(ide_prcla)) {
				utilitario.agregarMensajeError("Error", "La partida " + codigo_clasificador_prcla
						+ " no es parte de la certificación presupuestaria. ");
				return false;
			}
			// Sumar el subtotal por partidas
			try {
				double valor = Double.parseDouble(tab_tabla_detalle.getValor(i, "subtotal_inegd"));
				if (sumatoriaPartidas.containsKey(ide_prcla)) {
					sumatoriaPartidas.put(ide_prcla, sumatoriaPartidas.get(ide_prcla) + valor);
				} else {
					sumatoriaPartidas.put(ide_prcla, valor);
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}

		}

		// Validar que el total por partida no sea mayor a lo certificado
		for (Entry<String, Double> item : sumatoriaPartidas.entrySet()) {
			String ide_prcla = item.getKey();
			if (!detalleCertificacion.containsKey(ide_prcla)) {
				return false;
			}
			BigDecimal valorItem = new BigDecimal(item.getValue()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal valorCertificado = new BigDecimal(detalleCertificacion.get(ide_prcla)).setScale(2,
					RoundingMode.HALF_UP);
			if (valorItem.compareTo(valorCertificado) > 0) {
				TablaGenerica tg_clasificador = ser_bodega.getTablaGenericaPreClasificacionPorId(ide_prcla);
				String codigo_clasificador_prcla = tg_clasificador.getValor("codigo_clasificador_prcla");

				utilitario.agregarMensajeError("Error",
						"El total de la partida " + codigo_clasificador_prcla + " supera al valor certificado.");
				return false;
			}
		}

		return true;
	}

	public void importarFactura() {
		String ide_prcer = tab_tabla.getValor("ide_prcer");
		if (ide_prcer == null || ide_prcer == "") {
			utilitario.agregarMensaje("Debe seleccionar primero la certificación presupuestaria", "");
			return;
		}

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_factura.getTab_seleccion().setSql(ser_bodega.getFacturas());
			set_factura.getTab_seleccion().ejecutarSql();
			set_factura.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un ingreso existente o crear un nuevo ingreso", "");
		}
	}

	public void importarFacturaCambio() {
		if (tab_tabla.getFilaSeleccionada() != null) {
			set_factura_cambio.getTab_seleccion().setSql(ser_bodega.getFacturas());
			set_factura_cambio.getTab_seleccion().ejecutarSql();
			set_factura_cambio.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un ingreso existente o crear un nuevo ingreso", "");
		}
	}

	public void importarCertificacion() {
		if (tab_tabla.getFilaSeleccionada() != null) {
			set_certificacion.getTab_seleccion()
					.setSql(ser_presupuesto.getCertificacion(com_anio.getValue().toString(), "true"));
			System.out.println(ser_presupuesto.getCertificacion(com_anio.getValue().toString(), "true"));
			set_certificacion.getTab_seleccion().ejecutarSql();
			set_certificacion.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un ingreso existente o crear un nuevo ingreso", "");
		}
	}

	public void importarFacturaDetalle() {
		set_factura_detalle.getTab_seleccion().setSql(ser_bodega.getFacturaDetalle(tab_tabla.getValor("ide_adfac")));
		set_factura_detalle.getTab_seleccion().ejecutarSql();
		set_factura_detalle.dibujar();

	}

	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue() + " AND ide_boubi=" + com_bodega.getValue()
					+ " AND ide_inttr=" + ser_bodega.TRANSACCION_INGRESO.toString() + " AND ide_adfac IS NOT NULL");
			tab_tabla.ejecutarSql();
			tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
			actualizarResumenPartidas();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y bodega", "");
		}
	}

	@Override
	public void insertar() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}

		if (tab_tabla.isFocus()) {
			tab_tabla.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");

			tab_tabla.setValor("numero_documento_ingeg", ser_contabilidad.numeroSecuencial(par_sec_ingreso));
			tab_tabla.setValor("ide_gtemp", ide_gtempxx);
			tab_tabla.setValor("ide_geani", com_anio.getValue() + "");
			tab_tabla.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla.setValor("ide_inttr", ser_bodega.TRANSACCION_INGRESO.toString());
			tab_tabla.setValor("activo_ingeg", Boolean.TRUE.toString());
			tab_tabla.setValor("fecha_ingeg", utilitario.getFechaActual());
			tab_tabla.setValor("subtotal_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("valor_iva_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("total_ingeg", Double.valueOf(0).toString());

		}

		if (tab_registrar_catalogo.isFocus()) {
			tab_registrar_catalogo.insertar();
		}
		actualizarResumenPartidas();
	}

	public boolean verificarSiExisteEnInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getTotalFilas() > 0;
	}

	public String obtenerItemInventarioPK(String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getValor("ide_boinv");
	}

	public void anularIngreso() {
		Date fecha_inicial = utilitario.getFecha(tab_tabla.getValor("fecha_ingeg"));
		Date fecha_actual = new Date();
		int diferencia = utilitario.getDiferenciasDeFechas(fecha_inicial, fecha_actual);
		System.out.println("Diferencia de dias: " + diferencia);
		if (diferencia <= 1 || utilitario.getVariable("ide_usua").equals("395")|| utilitario.getVariable("ide_usua").equals("10") || utilitario.getVariable("ide_usua").equals("7")) {
			String usuario = utilitario.getVariable("ide_usua");
			tab_tabla.setValor("activo_ingeg", Boolean.FALSE.toString());
			tab_tabla.setValor("observacion_ingeg", tab_tabla.getValor("observacion_ingeg") + " "
					+ utilitario.getFechaHoraActual() + ": El usuario " + usuario + " anuló la transaccion");
			tab_tabla.modificar(tab_tabla.getFilaActual());

			if (tab_tabla.guardar()) {
				if (tab_tabla_detalle.guardar()) {
					guardarPantalla();
					guardarInventario();
					System.out.println("Transaccion anulada ");
					utilitario.agregarMensaje("Transacción anulada", "");
				}
			}

			utilitario.addUpdate("tab_tabla_detalle");
			utilitario.addUpdate("tab_tabla");
		} else {
			utilitario.agregarNotificacionInfo("Sobrepasó la fecha límite para anular una transacción", "");
		}
	}

	public void guardarInventario() {
		String ide_geani = tab_tabla.getValor("ide_geani");
		String ide_boubi = tab_tabla.getValor("ide_boubi");

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, true);
		}

	}

	@Override
	public void guardar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		if (pckUtilidades.CConversion.CInt(com_anio.getValue().toString()) < 16) {
			utilitario.agregarMensajeInfo("Debe seleccionar el año 2023", "");
			return;
		}

		if (!validarPartidas()) {
			return;
		}

		if (tab_tabla.isFilaInsertada()) {
			tab_tabla.setValor("numero_documento_ingeg", ser_contabilidad.numeroSecuencial(par_sec_ingreso));
			if (tab_tabla.guardar()) {
				if (tab_tabla_detalle.guardar()) {
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_ingreso),
							par_sec_ingreso);
					guardarPantalla();
					guardarInventario();
				}
			}
		} else {
			utilitario.agregarMensajeInfo("No puede modificar un ingreso existente", "");
			if (tab_tabla.guardar()) {
				guardarPantalla();
			}
		}

		if (permitirGuardar && !tab_tabla.isFilaInsertada()) {
			if (tab_tabla.guardar()) {
				guardarPantalla();
			}
		}

	}

	@Override
	public void eliminar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		if (tab_tabla.isFilaInsertada() && tab_tabla_detalle.isFocus()) {
			tab_tabla_detalle.eliminar();
			utilitario.addUpdate("tab_tabla_detalle");
		}

	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_tabla_detalle() {
		return tab_tabla_detalle;
	}

	public void setTab_tabla_detalle(Tabla tab_tabla_detalle) {
		this.tab_tabla_detalle = tab_tabla_detalle;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Combo getCom_bodega() {
		return com_bodega;
	}

	public Dialogo getDia_registrar_catalogo() {
		return dia_registrar_catalogo;
	}

	public void setDia_registrar_catalogo(Dialogo dia_registrar_catalogo) {
		this.dia_registrar_catalogo = dia_registrar_catalogo;
	}

	public Tabla getTab_registrar_catalogo() {
		return tab_registrar_catalogo;
	}

	public void setTab_registrar_catalogo(Tabla tab_registrar_catalogo) {
		this.tab_registrar_catalogo = tab_registrar_catalogo;
	}

	public static String getPar_sec_ingreso() {
		return par_sec_ingreso;
	}

	public static void setPar_sec_ingreso(String par_sec_ingreso) {
		pre_ingreso_existencia.par_sec_ingreso = par_sec_ingreso;
	}

	public void setCom_bodega(Combo com_bodega) {
		this.com_bodega = com_bodega;
	}

	public SeleccionTabla getSet_factura() {
		return set_factura;
	}

	public void setSet_factura(SeleccionTabla set_factura) {
		this.set_factura = set_factura;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public SeleccionTabla getSet_factura_detalle() {
		return set_factura_detalle;
	}

	public void setSet_factura_detalle(SeleccionTabla set_factura_detalle) {
		this.set_factura_detalle = set_factura_detalle;
	}

	public SeleccionTabla getSet_certificacion() {
		return set_certificacion;
	}

	public void setSet_certificacion(SeleccionTabla set_certificacion) {
		this.set_certificacion = set_certificacion;
	}

	public ServicioPresupuesto getSer_presupuesto() {
		return ser_presupuesto;
	}

	public void setSer_presupuesto(ServicioPresupuesto ser_presupuesto) {
		this.ser_presupuesto = ser_presupuesto;
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

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public SeleccionTabla getSet_factura_cambio() {
		return set_factura_cambio;
	}

	public void setSet_factura_cambio(SeleccionTabla set_factura_cambio) {
		this.set_factura_cambio = set_factura_cambio;
	}

	public SeleccionTabla getSet_item_catalogo() {
		return set_item_catalogo;
	}

	public void setSet_item_catalogo(SeleccionTabla set_item_catalogo) {
		this.set_item_catalogo = set_item_catalogo;
	}

	public Boolean getP_valor_existencia_inc_iva() {
		return p_valor_existencia_inc_iva;
	}

	public void setP_valor_existencia_inc_iva(Boolean p_valor_existencia_inc_iva) {
		this.p_valor_existencia_inc_iva = p_valor_existencia_inc_iva;
	}

	public Grid getGri_partidas() {
		return gri_partidas;
	}

	public void setGri_partidas(Grid gri_partidas) {
		this.gri_partidas = gri_partidas;
	}

	@Override
	public void inicio() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.inicio();
		actualizarResumenPartidas();
	}

	@Override
	public void fin() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.fin();
		actualizarResumenPartidas();
	}

	@Override
	public void siguiente() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.siguiente();
		actualizarResumenPartidas();
	}

	@Override
	public void atras() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.atras();
		actualizarResumenPartidas();
	}

}
