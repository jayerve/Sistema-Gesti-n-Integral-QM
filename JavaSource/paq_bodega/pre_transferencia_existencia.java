package paq_bodega;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
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

public class pre_transferencia_existencia extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	private Combo com_anio = new Combo();
	// private Combo com_bodega = new Combo();

	private SeleccionTabla set_catalogo = new SeleccionTabla();

	private SeleccionTabla set_bodega_origen = new SeleccionTabla();
	private SeleccionTabla set_bodega_destino = new SeleccionTabla();

	private Map<String, Object> p_parametros = new HashMap<String, Object>();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();

	public static String par_sec_egreso;

	// Si el valor de la existencia debe ser tomado del costo o del costo_inc_iva
	public Boolean p_valor_existencia_inc_iva;

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

	public pre_transferencia_existencia() {
		System.out.println("pre_transferencia_existencia");
		// Si el parametro es 0, false
		p_valor_existencia_inc_iva = utilitario.getVariable("p_modulo_bodega_valor_existencias_inc_iva")
				.equalsIgnoreCase("0") ? false : true;

		bar_botones.agregarReporte();

		par_sec_egreso = utilitario.getVariable("p_modulo_sec_bodega_transferencia_existencia");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setCampoOrden("ide_ingeg desc");
		tab_tabla.setCondicion("ide_ingeg=-1");
		tab_tabla.setTabla("bodt_ingreso_egreso", "ide_ingeg", 1);
		tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setLectura(true);
		tab_tabla.getColumna("ide_boubi").setRequerida(true);
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA ORIGEN");

		tab_tabla.getColumna("ide_boubi_transferencia").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi",
				"");
		tab_tabla.getColumna("ide_boubi_transferencia").setLectura(true);
		tab_tabla.getColumna("ide_boubi_transferencia").setRequerida(true);
		tab_tabla.getColumna("ide_boubi_transferencia").setNombreVisual("BODEGA DESTINO");

		tab_tabla.getColumna("ide_inttr").setCombo("bodt_inventario_tipo_transaccion", "ide_inttr", "detalle_inttr",
				"");

		tab_tabla.getColumna("ide_inttr").setLectura(true);
		tab_tabla.getColumna("fecha_ingeg").setLectura(true);

		tab_tabla.getColumna("ide_geani").setVisible(false);
		// Factura
		tab_tabla.getColumna("ide_adfac").setCombo("adq_factura", "ide_adfac", "num_factura_adfac", "");
		tab_tabla.getColumna("ide_adfac").setAutoCompletar();
		tab_tabla.getColumna("ide_adfac").setLectura(true);
		tab_tabla.getColumna("ide_adfac").setNombreVisual("FACTURA");
		// tab_tabla.getColumna("ide_adfac").setRequerida(true);
		tab_tabla.getColumna("ide_adfac").setVisible(false);
		// Empleado responsable del ingreso
		// tab_tabla.getColumna("ide_gtemp").setCombo("adq_factura",
		// "ide_adfac","num_factura_adfac", "");
		tab_tabla.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp").setLectura(true);
		tab_tabla.getColumna("ide_gtemp").setNombreVisual("EMPLEADO RESPONSABLE");

		tab_tabla.getColumna("ide_gtemp_solicitante").setVisible(true);
		tab_tabla.getColumna("ide_gtemp_solicitante").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_solicitante").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_solicitante").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setVisible(false);

		tab_tabla.getColumna("ide_prcer").setVisible(false);

		tab_tabla.getColumna("numero_documento_ingeg").setLectura(true);
		tab_tabla.getColumna("ide_ingeg_ref").setLectura(true);

		tab_tabla.getColumna("subtotal_ingeg").setLectura(true);
		tab_tabla.getColumna("subtotal_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("subtotal_ingeg").setEtiqueta();

		tab_tabla.getColumna("valor_iva_ingeg").setLectura(true);
		tab_tabla.getColumna("valor_iva_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("valor_iva_ingeg").setEtiqueta();

		tab_tabla.getColumna("total_ingeg").setLectura(true);
		tab_tabla.getColumna("total_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("total_ingeg").setEtiqueta();

		tab_tabla.getColumna("ide_adfac_antigua").setVisible(false);
		tab_tabla.getColumna("valor_iva_ingeg").setVisible(false);

		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("DETALLE DEL EGRESO");
		tab_tabla_detalle.setTabla("bodt_ingreso_egreso_det", "ide_inegd", 2);

		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		tab_tabla_detalle.getColumna("ide_bocam").setLectura(true);
		tab_tabla_detalle.getColumna("ide_inttr").setLectura(true);
		tab_tabla_detalle.getColumna("ide_inttr").setVisible(false);
		tab_tabla_detalle.getColumna("ide_addef").setLectura(true);
		tab_tabla_detalle.getColumna("ide_addef").setVisible(false);
		tab_tabla_detalle.getColumna("cantidad_inegd").setLectura(false);
		tab_tabla_detalle.getColumna("subtotal_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setLectura(false);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("total_inegd").setLectura(true);

		tab_tabla_detalle.getColumna("marca_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("modelo_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("color_inegd").setLongitud(20);

		tab_tabla_detalle.getColumna("cantidad_inegd").setRequerida(true);

		tab_tabla_detalle.getColumna("cantidad_inegd").setMetodoChange("calcular");
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setMetodoChange("calcular");

		tab_tabla_detalle.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_tabla_detalle.getColumna("ide_bounm").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm").setLongitud(20);
		tab_tabla_detalle.getColumna("ide_bounm").setLectura(true);

		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLongitud(20);

		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setCombo(ser_bodega.getMedidas());
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLongitud(20);
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLectura(true);

		tab_tabla_detalle.getColumna("peligro_salud_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_inflamabilidad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_reactividad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLectura(true);

		tab_tabla_detalle.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest",
				"activo_afest=true");
		tab_tabla_detalle.getColumna("ide_afest").setLongitud(15);

		tab_tabla_detalle.getColumna("saldo_disponible_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("saldo_disponible_inegd").setEstilo(ESTILO_ETIQUETA);

		tab_tabla_detalle.getColumna("aplica_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setVisible(false);
		// tab_tabla_detalle.getColumna("valor_iva_inegd").setVisible(false);

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

		tab_tabla_detalle.getColumna("valor_existencia_inegd").setLectura(true);
		
		// Ocultar columnas
		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_solicitante").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setVisible(false);
		tab_tabla.getColumna("valor_iva_ingeg").setVisible(false);
		tab_tabla.getColumna("ide_boubi_transferencia").setVisible(false);
		tab_tabla.getColumna("ide_ingeg_ref").setVisible(false);
		tab_tabla.getColumna("numero_proceso_ingeg").setVisible(false);
		

		tab_tabla_detalle.getColumna("ide_addef").setVisible(false);
		// tab_tabla_detalle.getColumna("saldo_disponible_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("ide_inttr").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("subtotal_inegd").setVisible(false);

		tab_tabla_detalle.getColumna("ide_inegd").setOrden(1);
		tab_tabla_detalle.getColumna("ide_bocam").setOrden(2);
		tab_tabla_detalle.getColumna("cantidad_inegd").setOrden(3);
		tab_tabla_detalle.getColumna("valor_existencia_inegd").setOrden(4);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setOrden(5);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setOrden(6);

		tab_tabla.agregarRelacion(tab_tabla_detalle);

		tab_tabla.setTipoFormulario(true);
		tab_tabla.dibujar();
		tab_tabla_detalle.dibujar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_tabla_detalle);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_item = new Boton();
		bot_item.setValue("Agregar Item");
		bot_item.setTitle("AGREGAR ITEM");
		bot_item.setIcon("ui-icon-note");
		bot_item.setMetodo("importarItem");

		Boton bot_bodega_origen = new Boton();
		bot_bodega_origen.setValue("Bodega Origen");
		bot_bodega_origen.setTitle("BODEGA ORIGEN");
		bot_bodega_origen.setIcon("ui-icon-home");
		bot_bodega_origen.setMetodo("importarBodegaOrigen");

		Boton bot_bodega_destino = new Boton();
		bot_bodega_destino.setValue("Bodega Destino");
		bot_bodega_destino.setTitle("BODEGA DESTINO");
		bot_bodega_destino.setIcon("ui-icon-home");
		bot_bodega_destino.setMetodo("importarBodegaDestino");

		bar_botones.agregarBoton(bot_bodega_origen);
		bar_botones.agregarBoton(bot_bodega_destino);
		bar_botones.agregarBoton(bot_item);

		set_catalogo.setId("set_catalogo");
		set_catalogo.setSeleccionTabla(ser_bodega.getSqlInventarioActualPorBodegaConSaldo("-1", "-1"), "ide_bocam");

		set_catalogo.setTitle("Seleccione un item");
		set_catalogo.getBot_aceptar().setMetodo("aceptarItem");
		set_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();
		set_catalogo.setRadio();
		agregarComponente(set_catalogo);

		set_bodega_origen.setId("set_bodega_origen");
		set_bodega_origen.setSeleccionTabla(ser_bodega.getBodegas(), "ide_boubi");

		set_bodega_origen.setTitle("Seleccione la bodega de origen");
		set_bodega_origen.getBot_aceptar().setMetodo("aceptarBodegaOrigen");
		set_bodega_origen.setRadio();
		agregarComponente(set_bodega_origen);

		set_bodega_destino.setId("set_bodega_destino");
		set_bodega_destino.setSeleccionTabla(ser_bodega.getBodegas(), "ide_boubi");

		set_bodega_destino.setTitle("Seleccione la bodega de destino");
		set_bodega_destino.getBot_aceptar().setMetodo("aceptarBodegaDestino");
		set_bodega_destino.setRadio();
		agregarComponente(set_bodega_destino);
	}

	public void importarItem() {
		if (tab_tabla.getFilaSeleccionada() != null) {
			String ide_geani = com_anio.getValue().toString();
			String ide_boubi = tab_tabla.getValor("ide_boubi");
			System.out.println("ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi);
			set_catalogo.getTab_seleccion()
					.setSql(ser_bodega.getSqlInventarioActualPorBodegaConSaldo(ide_geani, ide_boubi));
			set_catalogo.getTab_seleccion().ejecutarSql();
			set_catalogo.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un egreso existente o crear un nuevo egreso", "");
		}
	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		System.out.println("Reporte seleccionado: " + rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Comprobante transferencia")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				p_parametros.put("titulo",
						"TRANSFERENCIA DE EXISTENCIAS No. " + tab_tabla.getValor("numero_documento_ingeg"));
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

	public void calcular(AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);
		System.out.println("calcular");
		try {
			double cantidad = Double.parseDouble(tab_tabla_detalle.getValor("cantidad_inegd").toString());
			double valor_unitario = Double.parseDouble(tab_tabla_detalle.getValor("costo_unitario_inegd").toString());
			double valor_unitario_inc_iva = Double
					.parseDouble(tab_tabla_detalle.getValor("costo_unitario_inc_iva_inegd").toString());
			double valor_existencia = Double
					.parseDouble(tab_tabla_detalle.getValor("valor_existencia_inegd").toString());

			double saldo_disponible = Double
					.parseDouble(tab_tabla_detalle.getValor("saldo_disponible_inegd").toString());

			tab_tabla_detalle.setValor("subtotal_inegd", Double.valueOf(cantidad * valor_unitario).toString());

			// double porcenjate_iva =
			// Double.valueOf(utilitario.getVariable("p_valor_iva"));

			tab_tabla_detalle.setValor("total_inegd", String.valueOf(cantidad * valor_existencia));

			if (cantidad > saldo_disponible) {
				utilitario.agregarMensajeError("No tiene suficiente stock.", "");
			}

			if (cantidad <= 0) {
				utilitario.agregarMensajeError("Cantidad debe ser mayor a 0.", "");
			}

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}

		utilitario.addUpdate("tab_tabla_detalle");

		Double valor_iva = (double) 0;
		Double subtotal = (double) 0;
		Double total = (double) 0;
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {

			try {
				double cantidad = Double.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_inegd").toString());
				double valor_unitario = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "costo_unitario_inegd").toString());
				double valor_unitario_inc_iva = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "costo_unitario_inc_iva_inegd").toString());
				double valor_existencia = Double
						.parseDouble(tab_tabla_detalle.getValor("valor_existencia_inegd").toString());

				Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor(i, "aplica_iva_inegd"));
				double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));

				subtotal += cantidad * valor_unitario;
				total += cantidad * valor_existencia;

				if (aplica_iva) {
					valor_iva += cantidad * valor_unitario * porcenjate_iva;
				}

			} catch (NumberFormatException e) {
				System.out.println("Error:" + e.getMessage());
			}
		}

		tab_tabla.setValor("subtotal_ingeg", subtotal.toString());
		tab_tabla.setValor("valor_iva_ingeg", valor_iva.toString());
		tab_tabla.setValor("total_ingeg", total.toString());

		utilitario.addUpdate("tab_tabla");
	}

	public void aceptarItem() {
		// Cuando se de clic en Aceptar en el dialogo de selección de item de inventario
		String str_seleccionados = set_catalogo.getTab_seleccion().getFilasSeleccionadas();

		String ide_bocam = set_catalogo.getValorSeleccionado();
		String ide_boubi = tab_tabla.getValor("ide_boubi");
		String ide_geani = com_anio.getValue().toString();

		System.out.println("valorSeleccionado:" + set_catalogo.getValorSeleccionado());
		if (str_seleccionados != null) {
			TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogo(ide_bocam, ide_boubi, ide_geani);
			Double subtotal = (double) 0;
			Double valor_iva = (double) 0;
			Double total = (double) 0;
			tab_tabla_detalle.insertar();
			tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
			tab_tabla_detalle.setValor("ide_ingeg", tab_tabla.getValor("ide_ingeg"));
			tab_tabla_detalle.setValor("ide_inttr", ser_bodega.TRANSACCION_TRANSFERENCIA_EGRESO.toString());
			tab_tabla_detalle.setValor("ide_bounm", tg_catalogo.getValor("ide_bounm"));
			tab_tabla_detalle.setValor("ide_bounm_presentacion", tg_catalogo.getValor("ide_bounm_presentacion"));
			tab_tabla_detalle.setValor("costo_unitario_inegd", tg_catalogo.getValor("costo_medio_unidad_inres"));
			tab_tabla_detalle.setValor("valor_existencia_inegd", tg_catalogo.getValor("pmp_existencia_inres"));
			tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd",
					tg_catalogo.getValor("costo_medio_unidad_inc_iva_inres"));
			tab_tabla_detalle.setValor("saldo_disponible_inegd", tg_catalogo.getValor("cantidad_saldo_boinv"));
			tab_tabla_detalle.setValor("ide_bocam", ide_bocam);
			System.out.println("ide_bocam: " + ide_bocam);
			System.out.println("ide_bounm_presentacion: " + tg_catalogo.getValor("ide_bounm_presentacion"));
			System.out.println("cantidad_saldo_boinv: " + tg_catalogo.getValor("cantidad_saldo_boinv"));
			System.out.println(tg_catalogo.getValor("costo_medio_unidad_inres"));
			tab_tabla_detalle.setValor("ide_afest", ser_bodega.ESTADO_PREDETERMINADO.toString());

			utilitario.addUpdate("tab_tabla_detalle");

			set_catalogo.cerrar();
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}

	}

	public Boolean validarSaldo() {

		if (tab_tabla_detalle.getTotalFilas() <= 0) {
			utilitario.agregarMensajeError("No hay items en el egreso", "");
			return false;
		}
		
		HashMap<String, Double> cantidadesPorEgresar = new HashMap<String, Double>();
		
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			try {
				
				String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
				double saldo_disponible_inged = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "saldo_disponible_inegd").toString());
				double cantidad = Double.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_inegd").toString());
				
				if (cantidadesPorEgresar.containsKey(ide_bocam)) {
					double cantidadTotal = cantidadesPorEgresar.get(ide_bocam) + cantidad;
					if (cantidadTotal > saldo_disponible_inged) {
						return false;
					}
					
					cantidadesPorEgresar.put(ide_bocam, cantidadTotal);
					
				} else {
					cantidadesPorEgresar.put(ide_bocam, cantidad);
				}

				if (cantidad > saldo_disponible_inged) {
					return false;
				}
				if (cantidad <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}

		return true;
	}

	public void cargarCatalogo(AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);

		String ide_bocam = tab_tabla_detalle.getValor("ide_bocam");
		System.out.println("selected ide_bocam:" + ide_bocam);
		TablaGenerica tg = ser_bodega.getCatalogoPorId(tab_tabla_detalle.getValor("ide_bocam"));

		String vidaUtil = tg.getValor("vida_util_bocam");
		String fechaVencimiento = tab_tabla_detalle.getValor("fecha_vencimiento_inegd");

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
		tab_tabla_detalle.setValor("ide_bounm", tg.getValor("ide_bounm"));
		System.out.println("ide_bocam:" + tg.getValor("ide_bounm"));
		tab_tabla_detalle.setValor("ide_bounm_presentacion", tg.getValor("ide_bounm_presentacion"));
		tab_tabla_detalle.setValor("peligro_salud_inegd", tg.getValor("peligro_salud_bocam"));
		tab_tabla_detalle.setValor("peligro_inflamabilidad_inegd", tg.getValor("peligro_inflamabilidad_bocam"));
		tab_tabla_detalle.setValor("peligro_reactividad_inegd", tg.getValor("peligro_reactividad_bocam"));
		tab_tabla_detalle.setValor("manejo_especial_inegd", tg.getValor("manejo_especial_bocam"));
		tab_tabla_detalle.setValor("cantidad_inegd", Integer.toString(0));

		utilitario.addUpdate("tab_tabla_detalle");

	}

	public void importarBodegaOrigen() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_bodega_origen.getTab_seleccion().setSql(ser_bodega.getBodegas());
			set_bodega_origen.getTab_seleccion().ejecutarSql();
			set_bodega_origen.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar una transferencia existente o crear una nueva", "");
		}
	}

	public void aceptarBodegaOrigen() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_bodega_origen.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_boubi", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());
			String ide_boubi = str_seleccionado;
			String ide_geani = com_anio.getValue().toString();
			
			// En caso de que existan ítems, actualizar el saldo disponible para evitar que se transfieran cantidades que no existen
			for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
				String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
				TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogo(ide_bocam, ide_boubi, ide_geani);
				tab_tabla_detalle.setValor(i, "saldo_disponible_inegd", tg_catalogo.getValor("cantidad_saldo_boinv"));
			}
			
			

			System.out.println("str_seleccionado: " + str_seleccionado);
			System.out.println("tab_tabla.getFilaActual(): " + tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_bodega_origen.cerrar();
		utilitario.addUpdate("tab_tabla");

	}

	public void importarBodegaDestino() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_bodega_destino.getTab_seleccion().setSql(ser_bodega.getBodegas());
			set_bodega_destino.getTab_seleccion().ejecutarSql();
			set_bodega_destino.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar una transferencia existente o crear una nueva", "");
		}
	}

	public void aceptarBodegaDestino() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_bodega_destino.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_boubi_transferencia", str_seleccionado);
			TablaGenerica tg_bodega = ser_bodega.getTablaGenericaBodega(str_seleccionado);

			tab_tabla.setValor("ide_gtemp_solicitante", tg_bodega.getValor("ide_gtemp"));
			tab_tabla.modificar(tab_tabla.getFilaActual());

		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_bodega_destino.cerrar();
		utilitario.addUpdate("tab_tabla");

	}

	public void seleccionaParametros() {
		if (com_anio.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue() + " AND ide_inttr="
					+ ser_bodega.TRANSACCION_TRANSFERENCIA_EGRESO.toString());
			tab_tabla.ejecutarSql();
			tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}

	@Override
	public void insertar() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		if (tab_tabla.isFocus()) {
			tab_tabla.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");

			tab_tabla.setValor("numero_documento_ingeg", ser_contabilidad.numeroSecuencial(par_sec_egreso));

			tab_tabla.setValor("ide_gtemp", ide_gtempxx);
			tab_tabla.setValor("ide_geani", com_anio.getValue() + "");
			// tab_tabla.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla.setValor("ide_inttr", ser_bodega.TRANSACCION_TRANSFERENCIA_EGRESO.toString());
			tab_tabla.setValor("activo_ingeg", Boolean.TRUE.toString());
			tab_tabla.setValor("fecha_ingeg", utilitario.getFechaActual());
			tab_tabla.setValor("subtotal_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("valor_iva_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("total_ingeg", Double.valueOf(0).toString());

		}
	}

	public boolean verificarSiExisteEnInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getTotalFilas() > 0;
	}

	public String obtenerItemInventarioPK(String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getValor("ide_boinv");
	}

	public void guardarInventario() {
		String ide_geani = tab_tabla.getValor("ide_geani");

		String ide_boubi = tab_tabla.getValor("ide_boubi");
		String ide_boubi_transferencia = tab_tabla.getValor("ide_boubi_transferencia");
		String ide_ingeg = tab_tabla.getValor("ide_ingeg");

		System.out.println("guardarInventario " + ide_ingeg);

		ser_bodega.trigTransferencia(ide_ingeg, ser_bodega.TRANSACCION_TRANSFERENCIA_EGRESO.toString(),
				ser_bodega.TRANSACCION_TRANSFERENCIA_INGRESO.toString());

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, false);
			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi_transferencia, false);
		}

	}

	@Override
	public void guardar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if (pckUtilidades.CConversion.CInt(com_anio.getValue().toString()) < 16 ) {
			utilitario.agregarMensajeInfo("Debe seleccionar el año 2023", "");
			return;
		}

		if (validarSaldo()) {

			System.out.println("validarSaldo true");
			if (tab_tabla.isFilaInsertada()) {
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_egreso), par_sec_egreso);
				if (tab_tabla.guardar()) {
					if (tab_tabla_detalle.guardar()) {
						guardarPantalla();
						guardarInventario();
					}
				}
			} else {
				utilitario.agregarMensaje("No puede modificar un egreso existente", "");
			}

		} else {
			utilitario.agregarMensaje("No dispone del stock necesario", "");
		}
	}

	@Override
	public void eliminar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		if (tab_tabla.isFilaInsertada()) {
			if (tab_tabla_detalle.isFocus()) {
				tab_tabla_detalle.eliminar();
			}
		} else {
			utilitario.agregarMensajeError("No puede eliminar un egreso existente", "");
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

	public ServicioPresupuesto getSer_presupuesto() {
		return ser_presupuesto;
	}

	public void setSer_presupuesto(ServicioPresupuesto ser_presupuesto) {
		this.ser_presupuesto = ser_presupuesto;
	}

	public Map<String, Object> getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map<String, Object> p_parametros) {
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

	public SeleccionTabla getSet_catalogo() {
		return set_catalogo;
	}

	public void setSet_catalogo(SeleccionTabla set_catalogo) {
		this.set_catalogo = set_catalogo;
	}

	public SeleccionTabla getSet_bodega_origen() {
		return set_bodega_origen;
	}

	public void setSet_bodega_origen(SeleccionTabla set_bodega_origen) {
		this.set_bodega_origen = set_bodega_origen;
	}

	public SeleccionTabla getSet_bodega_destino() {
		return set_bodega_destino;
	}

	public void setSet_bodega_destino(SeleccionTabla set_bodega_destino) {
		this.set_bodega_destino = set_bodega_destino;
	}

}
