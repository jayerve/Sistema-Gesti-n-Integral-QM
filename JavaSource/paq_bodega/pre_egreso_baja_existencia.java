package paq_bodega;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class pre_egreso_baja_existencia extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();

	private SeleccionTabla set_catalogo = new SeleccionTabla();
	private SeleccionTabla set_ingresos = new SeleccionTabla();
	private SeleccionTabla set_solicitante = new SeleccionTabla();
	private SeleccionTabla set_jefe_solicitante = new SeleccionTabla();

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

	public pre_egreso_baja_existencia() {
		System.out.println("pre_egreso_baja_existencia");
		bar_botones.agregarReporte();

		par_sec_egreso = utilitario.getVariable("p_modulo_sec_bodega_egreso_existencia_por_baja");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setCondicion("ide_ingeg=-1");
		tab_tabla.setCampoOrden("ide_ingeg desc");
		tab_tabla.setTabla("bodt_ingreso_egreso", "ide_ingeg", 1);
		tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setLectura(true);
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla.getColumna("ide_inttr").setCombo("bodt_inventario_tipo_transaccion", "ide_inttr", "detalle_inttr",
				"");

		tab_tabla.getColumna("ide_inttr").setLectura(true);
		tab_tabla.getColumna("fecha_ingeg").setLectura(true);

		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_boubi_transferencia").setVisible(false);
		// Factura
		tab_tabla.getColumna("ide_adfac").setCombo("adq_factura", "ide_adfac", "num_factura_adfac", "");
		tab_tabla.getColumna("ide_adfac").setAutoCompletar();
		tab_tabla.getColumna("ide_adfac").setLectura(true);
		tab_tabla.getColumna("ide_adfac").setNombreVisual("FACTURA");
		// tab_tabla.getColumna("ide_adfac").setRequerida(true);
		// tab_tabla.getColumna("ide_adfac").setVisible(false);
		// Empleado responsable del ingreso
		// tab_tabla.getColumna("ide_gtemp").setCombo("adq_factura",
		// "ide_adfac","num_factura_adfac", "");
		tab_tabla.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp").setLectura(true);
		tab_tabla.getColumna("ide_gtemp").setNombreVisual("EMPLEADO RESPONSABLE");

		tab_tabla.getColumna("ide_gtemp_solicitante").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_solicitante").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_solicitante").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_solicitante").setRequerida(false);
		tab_tabla.getColumna("ide_gtemp_solicitante").setNombreVisual("EMPLEADO SOLICITANTE");

		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setLectura(true);
		// tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setRequerida(true);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setNombreVisual("JEFE SOLICITANTE");

		tab_tabla.getColumna("ide_prcer").setVisible(false);

		tab_tabla.getColumna("numero_documento_ingeg").setLectura(true);

		tab_tabla.getColumna("subtotal_ingeg").setLectura(true);
		tab_tabla.getColumna("subtotal_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("subtotal_ingeg").setEtiqueta();
		tab_tabla.getColumna("subtotal_ingeg").setVisible(false);

		tab_tabla.getColumna("valor_iva_ingeg").setLectura(true);
		tab_tabla.getColumna("valor_iva_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("valor_iva_ingeg").setEtiqueta();

		tab_tabla.getColumna("total_ingeg").setLectura(true);
		tab_tabla.getColumna("total_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("total_ingeg").setEtiqueta();

		tab_tabla.getColumna("ide_adfac_antigua").setVisible(false);
		tab_tabla.getColumna("valor_iva_ingeg").setVisible(false);
		tab_tabla.getColumna("ide_ingeg_ref").setVisible(false);
		tab_tabla.getColumna("soporte_descripcion").setVisible(false);

		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("DETALLE DEL EGRESO");
		tab_tabla_detalle.setTabla("bodt_ingreso_egreso_det", "ide_inegd", 2);

		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		// tab_tabla_detalle.getColumna("ide_bocam").setMetodoChange("cargarCatalogo");
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
		tab_tabla.getColumna("numero_proceso_ingeg").setLectura(true);

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

		tab_tabla_detalle.getColumna("valor_existencia_inegd").setLectura(true);

		// Ocultar columnas
		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_solicitante").setVisible(true);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setVisible(true);
		tab_tabla.getColumna("valor_iva_ingeg").setVisible(false);
		tab_tabla.getColumna("ide_boubi_transferencia").setVisible(false);
		tab_tabla.getColumna("ide_ingeg_ref").setVisible(false);

		tab_tabla_detalle.getColumna("ide_addef").setVisible(false);
		// tab_tabla_detalle.getColumna("saldo_disponible_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("ide_inttr").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setVisible(false);
		tab_tabla_detalle.getColumna("subtotal_inegd").setVisible(false);

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

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_tabla_detalle);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_factura = new Boton();
		bot_factura.setValue("Agregar Item");
		bot_factura.setTitle("AGREGAR ITEM");
		bot_factura.setIcon("ui-icon-note");
		bot_factura.setMetodo("importarItem");

		Boton bot_certificacion = new Boton();
		bot_certificacion.setValue("Solicitante");
		bot_certificacion.setTitle("SOLICITANTE");
		bot_certificacion.setIcon("ui-icon-person");
		bot_certificacion.setMetodo("importarSolicitante");

		Boton bot_jefe_solicitante = new Boton();
		bot_jefe_solicitante.setValue("Jefe Solicitante");
		bot_jefe_solicitante.setTitle("JEFE SOLICITANTE");
		bot_jefe_solicitante.setIcon("ui-icon-person");
		bot_jefe_solicitante.setMetodo("importarJefeSolicitante");

		Boton bot_importar_ingreso = new Boton();
		bot_importar_ingreso.setValue("Importar de ingreso");
		bot_importar_ingreso.setTitle("IMPORTAR INGRESO");
		bot_importar_ingreso.setIcon("ui-icon-arrowstop-1-s");
		bot_importar_ingreso.setMetodo("importarIngreso");

		Boton bot_anular = new Boton();
		bot_anular.setValue("Anular");
		bot_anular.setTitle("Anular");
		bot_anular.setIcon("ui-icon-note");
		bot_anular.setMetodo("anularIngreso");

		if (utilitario.getVariable("ide_usua").equals("395") || utilitario.getVariable("ide_usua").equals("796")|| utilitario.getVariable("ide_usua").equals("10") || utilitario.getVariable("ide_usua").equals("7")) { // dluje,
																													// julio
																													// mendoza
			bar_botones.agregarBoton(bot_anular);
		}

		bar_botones.agregarBoton(bot_factura);
		//bar_botones.agregarBoton(bot_certificacion);
		//bar_botones.agregarBoton(bot_jefe_solicitante);
		//bar_botones.agregarBoton(bot_importar_ingreso);

		set_catalogo.setId("set_catalogo");
		set_catalogo.setSeleccionTabla(ser_bodega.getSqlInventarioActualPorBodegaConSaldo("-1", "-1"), "ide_bocam");

		set_catalogo.setTitle("Seleccione un item");
		set_catalogo.getBot_aceptar().setMetodo("aceptarItem");
		set_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();
//		set_catalogo.setRadio();
		set_catalogo.setCheck();
		agregarComponente(set_catalogo);

		set_ingresos.setId("set_ingresos");
		set_ingresos.setTitle("Seleccione un item");
		set_ingresos.setSeleccionTabla(ser_bodega.getSqlIngresos("-1", "-1"), "ide_ingeg");
		set_ingresos.getBot_aceptar().setMetodo("aceptarItemIngreso");
		// set_ingresos.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();
		set_ingresos.setRadio();
		agregarComponente(set_ingresos);

		set_solicitante.setId("set_solicitante");
		set_solicitante.setSeleccionTabla(ser_bodega.getEmpleadosActivos(), "ide_gtemp");
		set_solicitante.getTab_seleccion().getColumna("nombre").setFiltroContenido();

		set_solicitante.setTitle("Seleccione el usuario solicitante");
		set_solicitante.getBot_aceptar().setMetodo("aceptarSolicitante");
		set_solicitante.setRadio();
		agregarComponente(set_solicitante);

		set_jefe_solicitante.setId("set_jefe_solicitante");
		set_jefe_solicitante.setSeleccionTabla(ser_bodega.getEmpleadosActivos(), "ide_gtemp");
		set_jefe_solicitante.getTab_seleccion().getColumna("nombre").setFiltro(true);

		set_jefe_solicitante.setTitle("Seleccione el jefe solicitante");
		set_jefe_solicitante.getBot_aceptar().setMetodo("aceptarJefeSolicitante");
		set_jefe_solicitante.setRadio();
		agregarComponente(set_jefe_solicitante);

		// Si el parametro es 0, false
		p_valor_existencia_inc_iva = utilitario.getVariable("p_modulo_bodega_valor_existencias_inc_iva")
				.equalsIgnoreCase("0") ? false : true;

	}
	

	public void importarIngreso() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			String ide_geani = com_anio.getValue().toString();
			String ide_boubi = com_bodega.getValue().toString();
			System.out.println(
					"pre_egreso_baja_existencia: importarIngreso ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi);
			if (ide_geani != null && ide_boubi != null) {
				System.out.println(ser_bodega.getSqlIngresos(ide_geani, ide_boubi));
				set_ingresos.getTab_seleccion().setSql(ser_bodega.getSqlIngresos(ide_geani, ide_boubi));
				set_ingresos.getTab_seleccion().ejecutarSql();
				set_ingresos.dibujar();
			}
		} else {
			utilitario.agregarMensaje("Debe seleccionar un egreso existente o crear un nuevo egreso", "");
		}
	}

	public void anularIngreso() {
		Date fecha_inicial = utilitario.getFecha(tab_tabla.getValor("fecha_ingeg"));
		Date fecha_actual = new Date();
		int diferencia = utilitario.getDiferenciasDeFechas(fecha_inicial, fecha_actual);
		System.out.println("pre_egreso_baja_existencia: Diferencia de dias: " + diferencia);
		if (diferencia <= 1 || utilitario.getVariable("ide_usua").equals("395")|| utilitario.getVariable("ide_usua").equals("10") || utilitario.getVariable("ide_usua").equals("7")) {
			String usuario = utilitario.getVariable("ide_usua");
			tab_tabla.setValor("activo_ingeg", Boolean.FALSE.toString());
			tab_tabla.setValor("observacion_ingeg", tab_tabla.getValor("observacion_ingeg") + " "
					+ utilitario.getFechaHoraActual() + ": El usuario " + usuario + " anuló la transaccion");
			tab_tabla.modificar(tab_tabla.getFilaActual());

			guardarInventario();

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

	public void importarItem() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			String ide_geani = com_anio.getValue().toString();
			String ide_boubi = com_bodega.getValue().toString();
			System.out
					.println("pre_egreso_baja_existencia:importarItem ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi);
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
		System.out.println("pre_egreso_baja_existencia: abrirListaReportes");
		if (tab_tabla.getValor("ide_ingeg") == null) {
			utilitario.agregarMensajeError("Debe guardar el egreso antes de imprimir.", "");
			return;
		}

		System.out.println("rep_reporte.dibujar()");
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		// if(!tab_tabla.isFilaInsertada())
		// return;
		System.out.println("Reporte seleccionado: " + rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Comprobante egreso por baja")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "EGRESO DE EXISTENCIAS POR BAJA No. " + tab_tabla.getValor("numero_documento_ingeg"));
				p_parametros.put("ide_ingeg", pckUtilidades.CConversion.CInt(tab_tabla.getValor("ide_ingeg")));
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

				System.out.println(p_parametros);
				System.out.println("pre_egreso_baja_existencia:aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		}
	}

	public void calcularTodo() {
		System.out.println("pre_egreso_baja_existencia:calcularTodo");

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			tab_tabla_detalle.modificar(i);
			try {
				double cantidad = Double.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_inegd").toString());
				double valor_unitario = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "costo_unitario_inegd").toString());
				double valor_unitario_inc_iva = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "costo_unitario_inc_iva_inegd").toString());
				double valor_existencia = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "valor_existencia_inegd").toString());

				double saldo_disponible = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "saldo_disponible_inegd").toString());

				tab_tabla_detalle.setValor(i, "subtotal_inegd",
						utilitario.getFormatoNumero(cantidad * valor_unitario, 2));

				double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));

				tab_tabla_detalle.setValor(i, "total_inegd",
						utilitario.getFormatoNumero(cantidad * valor_existencia, 2));
				Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor(i, "aplica_iva_inegd"));
				if (aplica_iva) {
					tab_tabla_detalle.setValor(i, "valor_iva_inegd",
							utilitario.getFormatoNumero(cantidad * valor_unitario * porcenjate_iva, 2));
				} else {
					tab_tabla_detalle.setValor(i, "valor_iva_inegd", utilitario.getFormatoNumero(0, 2));
				}

				if (cantidad > saldo_disponible) {
					utilitario.agregarMensajeError("No tiene suficiente stock.", "");
				}

				if (cantidad <= 0) {
					utilitario.agregarMensajeError("Cantidad debe ser mayor a 0.", "");
				}

			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		utilitario.addUpdate("tab_tabla_detalle");

		calcularCabecera();

	}

	public void calcularCabecera() {
		BigDecimal valor_iva = BigDecimal.ZERO;
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {

			try {
				BigDecimal cantidad = new BigDecimal(tab_tabla_detalle.getValor(i, "cantidad_inegd"));
				BigDecimal valor_unitario = new BigDecimal(tab_tabla_detalle.getValor(i, "costo_unitario_inegd"));
				// BigDecimal valor_unitario_inc_iva = new
				// BigDecimal(tab_tabla_detalle.getValor(i, "costo_unitario_inc_iva_inegd"));
				BigDecimal valor_existencia = new BigDecimal(tab_tabla_detalle.getValor(i, "valor_existencia_inegd"));

				Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor(i, "aplica_iva_inegd"));
				BigDecimal porcenjate_iva = new BigDecimal(utilitario.getVariable("p_valor_iva"));

				// subtotal += cantidad * valor_unitario
				subtotal = subtotal.add(cantidad.multiply(valor_unitario));
				// total += cantidad * valor_existencia
				total = total.add(cantidad.multiply(valor_existencia));

				if (aplica_iva) {
					// valor_iva = cantidad * valor_unitario * porcenjate_iva;
					valor_iva = valor_iva.add(cantidad.multiply(valor_unitario).multiply(porcenjate_iva));
					// tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd",
					// String.valueOf(valor_unitario * (porcenjate_iva + 1)));
				} /*
					 * else { //tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd",
					 * String.valueOf(valor_unitario)); }
					 */

			} catch (NumberFormatException e) {
				System.out.println("Error:" + e.getMessage());
				e.printStackTrace();
			}
		}

		tab_tabla.setValor("subtotal_ingeg", subtotal.setScale(2, RoundingMode.HALF_UP).toString());
		tab_tabla.setValor("valor_iva_ingeg", valor_iva.setScale(2, RoundingMode.HALF_UP).toString());
		tab_tabla.setValor("total_ingeg", total.setScale(2, RoundingMode.HALF_UP).toString());

		utilitario.addUpdate("tab_tabla");
	}

	public void calcular(AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);
		System.out.println("pre_egreso_baja_existencia");
		try {
			double cantidad = Double.parseDouble(tab_tabla_detalle.getValor("cantidad_inegd").toString());
			double valor_unitario = Double.parseDouble(tab_tabla_detalle.getValor("costo_unitario_inegd").toString());
			double valor_existencia = Double
					.parseDouble(tab_tabla_detalle.getValor("valor_existencia_inegd").toString());

			double saldo_disponible = Double
					.parseDouble(tab_tabla_detalle.getValor("saldo_disponible_inegd").toString());

			tab_tabla_detalle.setValor("subtotal_inegd", utilitario.getFormatoNumero(cantidad * valor_unitario, 2));

			double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));

			tab_tabla_detalle.setValor("total_inegd", utilitario.getFormatoNumero(cantidad * valor_existencia, 2));
			Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor("aplica_iva_inegd"));
			if (aplica_iva) {
				tab_tabla_detalle.setValor("valor_iva_inegd",
						utilitario.getFormatoNumero(cantidad * valor_unitario * porcenjate_iva, 2));
			} else {
				tab_tabla_detalle.setValor("valor_iva_inegd", utilitario.getFormatoNumero(0, 2));
			}

			if (cantidad > saldo_disponible) {
				utilitario.agregarMensajeError("No tiene suficiente stock.", "");
			}

			if (cantidad <= 0) {
				utilitario.agregarMensajeError("Cantidad debe ser mayor a 0.", "");
			}

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		utilitario.addUpdate("tab_tabla_detalle");

		calcularCabecera();
	}

	public void aceptarItemIngreso() {
		// Cuando se de clic en Aceptar en el dialogo de selección de item de inventario
		String str_seleccionados = set_ingresos.getTab_seleccion().getFilasSeleccionadas();

		String ide_bocam = str_seleccionados; // set_catalogo.getValorSeleccionado();
		String ide_boubi = com_bodega.getValue().toString();
		String ide_geani = com_anio.getValue().toString();

		if (str_seleccionados != null) {
			String ide_ingeg = set_ingresos.getValorSeleccionado();
			if (!ide_ingeg.isEmpty()) {

				TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaImportarIngreso(ide_ingeg, ide_boubi, ide_geani);
				Double subtotal = (double) 0;
				Double valor_iva = (double) 0;
				Double total = (double) 0;

				String ide_adfac = null;
				String ide_ingeg_ref = ide_ingeg;

				for (int i = 0; i < tg_catalogo.getTotalFilas(); i++) {
					tab_tabla_detalle.insertar();

					ide_adfac = tg_catalogo.getValor(i, "ide_adfac");

					tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
					tab_tabla_detalle.setValor("ide_ingeg", tab_tabla.getValor("ide_ingeg"));

					tab_tabla_detalle.setValor("cantidad_inegd", tg_catalogo.getValor(i, "cantidad_inegd"));
					tab_tabla_detalle.setValor("ide_inttr", ser_bodega.TRANSACCION_EGRESO_BAJA.toString());
					tab_tabla_detalle.setValor("ide_bounm", tg_catalogo.getValor(i, "ide_bounm"));
					tab_tabla_detalle.setValor("ide_bounm_presentacion",
							tg_catalogo.getValor(i, "ide_bounm_presentacion"));
					tab_tabla_detalle.setValor("costo_unitario_inegd",
							tg_catalogo.getValor(i, "costo_medio_unidad_inres"));
					tab_tabla_detalle.setValor("ide_addef", tg_catalogo.getValor(i, "ide_addef"));
					tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd",
							tg_catalogo.getValor(i, "costo_medio_unidad_inc_iva_inres"));
					tab_tabla_detalle.setValor("valor_existencia_inegd",
							tg_catalogo.getValor(i, "pmp_existencia_inres"));
					tab_tabla_detalle.setValor("saldo_disponible_inegd",
							tg_catalogo.getValor(i, "cantidad_saldo_boinv"));
					tab_tabla_detalle.setValor("ide_bocam", tg_catalogo.getValor(i, "ide_bocam"));
					if (tg_catalogo.getValor(i, "costo_medio_unidad_inres") != tg_catalogo.getValor(i,
							"costo_medio_unidad_inc_iva_inres")) {
						// System.out.println("aplica iva");
						tab_tabla_detalle.setValor("aplica_iva_inegd", "true");
					} else {
						// System.out.println("no aplica iva");
						tab_tabla_detalle.setValor("aplica_iva_inegd", "false");
					}

					tab_tabla_detalle.setValor("ide_afest", ser_bodega.ESTADO_PREDETERMINADO.toString());
				}

				tab_tabla.setValor("ide_adfac", ide_adfac);
				tab_tabla.setValor("ide_ingeg_ref", ide_ingeg_ref);

				TablaGenerica tg_cabecera_ingreso = ser_bodega.getTablaGenericaCabeceraIngreso(ide_ingeg_ref);
				String numero_proceso_ingeg = tg_cabecera_ingreso.getValor("numero_proceso_ingeg");

				tab_tabla.setValor("numero_proceso_ingeg", numero_proceso_ingeg);

				utilitario.addUpdate("tab_tabla_detalle");
				utilitario.addUpdate("tab_tabla");

				set_ingresos.cerrar();
			}
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
		calcularTodo();

	}

	public void aceptarItem() {
		System.out.println("pre_egreso_baja_existencia:aceptarItem");
		// Cuando se de clic en Aceptar en el dialogo de selección de item de inventario
		String str_seleccionados = set_catalogo.getTab_seleccion().getFilasSeleccionadas();

		String ide_bocam = str_seleccionados; // set_catalogo.getValorSeleccionado();
		String ide_boubi = com_bodega.getValue().toString();
		String ide_geani = com_anio.getValue().toString();
		System.out.println("pre_egreso_baja_existencia:seleccionados:" + set_catalogo.getSeleccionados());

		System.out.println("pre_egreso_baja_existencia:valorSeleccionado:" + set_catalogo.getValorSeleccionado()
				+ " -str_seleccionados: " + str_seleccionados);
		if (str_seleccionados != null) {
			TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogo(ide_bocam, ide_boubi, ide_geani);
			Double subtotal = (double) 0;
			Double valor_iva = (double) 0;
			Double total = (double) 0;

			System.out.println("pre_egreso_baja_existencia:totalFilas:" + String.valueOf(tg_catalogo.getTotalFilas()));

			for (int i = 0; i < tg_catalogo.getTotalFilas(); i++) {
				tab_tabla_detalle.insertar();
				tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
				tab_tabla_detalle.setValor("ide_ingeg", tab_tabla.getValor("ide_ingeg"));
				tab_tabla_detalle.setValor("ide_inttr", ser_bodega.TRANSACCION_EGRESO_BAJA.toString());
				tab_tabla_detalle.setValor("ide_bounm", tg_catalogo.getValor(i, "ide_bounm"));
				tab_tabla_detalle.setValor("ide_bounm_presentacion", tg_catalogo.getValor(i, "ide_bounm_presentacion"));
				tab_tabla_detalle.setValor("costo_unitario_inegd", tg_catalogo.getValor(i, "costo_medio_unidad_inres"));
				tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd",
						tg_catalogo.getValor(i, "costo_medio_unidad_inc_iva_inres"));
				tab_tabla_detalle.setValor("saldo_disponible_inegd", tg_catalogo.getValor(i, "cantidad_saldo_boinv"));
				tab_tabla_detalle.setValor("ide_bocam", tg_catalogo.getValor(i, "ide_bocam"));
				tab_tabla_detalle.setValor("valor_existencia_inegd", tg_catalogo.getValor(i, "pmp_existencia_inres"));
				if (tg_catalogo.getValor(i, "costo_medio_unidad_inres") != tg_catalogo.getValor(i,
						"costo_medio_unidad_inc_iva_inres")) {
					System.out.println("aplica iva");
					tab_tabla_detalle.setValor("aplica_iva_inegd", "true");
				} else {
					System.out.println("no aplica iva");
					tab_tabla_detalle.setValor("aplica_iva_inegd", "false");
				}
				// tab_tabla_detalle.setValor("aplica_iva_inegd", );
				System.out.println("ide_bocam: " + ide_bocam);
				System.out.println("ide_bounm_presentacion: " + tg_catalogo.getValor(i, "ide_bounm_presentacion"));
				System.out.println("cantidad_saldo_boinv: " + tg_catalogo.getValor(i, "cantidad_saldo_boinv"));
				System.out.println("costo_medio_unidad_inres: " + tg_catalogo.getValor(i, "costo_medio_unidad_inres"));
				tab_tabla_detalle.setValor("ide_afest", ser_bodega.ESTADO_PREDETERMINADO.toString());
			}

			utilitario.addUpdate("tab_tabla_detalle");

			set_catalogo.cerrar();
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}

	}

	/**
	 * 
	 * @return
	 */
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

	public void aceptarSolicitante() {
		System.out.println("pre_egreso_baja_existencia:aceptarSolicitante");
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_solicitante.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_gtemp_solicitante", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());

			System.out.println("str_seleccionado: " + str_seleccionado);
			System.out.println("tab_tabla.getFilaActual(): " + tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_solicitante.cerrar();
		utilitario.addUpdate("tab_tabla");

	}

	public void aceptarJefeSolicitante() {
		// Cuando se de clic en Aceptar en el dialogo de factura
		String str_seleccionado = set_jefe_solicitante.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_gtemp_jefe_solicitante", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());

			System.out.println("str_seleccionado: " + str_seleccionado);
			System.out.println("tab_tabla.getFilaActual(): " + tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_jefe_solicitante.cerrar();
		utilitario.addUpdate("tab_tabla");

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

	public void importarSolicitante() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_solicitante.getTab_seleccion().setSql(ser_bodega.getEmpleadosActivos());
			set_solicitante.getTab_seleccion().ejecutarSql();
			set_solicitante.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un egreso existente o crear un nuevo egreso", "");
		}
	}

	public void importarJefeSolicitante() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_jefe_solicitante.getTab_seleccion().setSql(ser_bodega.getEmpleadosActivos());
			set_jefe_solicitante.getTab_seleccion().ejecutarSql();
			set_jefe_solicitante.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un egreso existente o crear un nuevo egreso", "");
		}
	}

	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue() + " AND ide_boubi=" + com_bodega.getValue()
					+ " AND ide_inttr=" + ser_bodega.TRANSACCION_EGRESO_BAJA.toString());
			tab_tabla.ejecutarSql();
			tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y bodega", "");
		}
	}

	@Override
	public void insertar() {
		System.out.println("pre_egreso_baja_existencia:insertar");
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

			tab_tabla.setValor("numero_documento_ingeg", ser_contabilidad.numeroSecuencial(par_sec_egreso));

			tab_tabla.setValor("ide_gtemp", ide_gtempxx);
			tab_tabla.setValor("ide_geani", com_anio.getValue() + "");
			tab_tabla.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla.setValor("ide_inttr", ser_bodega.TRANSACCION_EGRESO_BAJA.toString());
			tab_tabla.setValor("activo_ingeg", Boolean.TRUE.toString());
			tab_tabla.setValor("fecha_ingeg", utilitario.getFechaActual());
			tab_tabla.setValor("subtotal_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("valor_iva_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("total_ingeg", Double.valueOf(0).toString());

		}
	}

	@Override
	public void inicio() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.inicio();
	}

	@Override
	public void fin() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.fin();
	}

	@Override
	public void siguiente() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.siguiente();
	}

	@Override
	public void atras() {
		if (tab_tabla.isFilaInsertada()) {
			utilitario.agregarMensaje("", "Debe guardar el registro actual antes de mover.");
			return;
		}
		super.atras();
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

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");

			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, false);
		}

	}

	@Override
	public void guardar() {
		System.out.println("pre_egreso_baja_existencia:guardar ide_geani = " + com_anio.getValue().toString());
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (pckUtilidades.CConversion.CInt(com_anio.getValue().toString()) < 16) {
			utilitario.agregarMensajeInfo("Debe seleccionar el año 2023", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}

		BigDecimal total_ingeg = new BigDecimal(tab_tabla.getValor("total_ingeg"));

		if (total_ingeg.compareTo(BigDecimal.ZERO) <= 0) {
			calcularCabecera();
		}

		if (validarSaldo()) {
			System.out.println("validarSaldo true");
			if (tab_tabla.isFilaInsertada()) {

				// tab_tabla_detalle.getModificadas().get(index)
				System.out.println(tab_tabla_detalle.getFilas());
				tab_tabla.setValor("numero_documento_ingeg", ser_contabilidad.numeroSecuencial(par_sec_egreso));
				if (tab_tabla.guardar()) {
					// tab_tabla_detalle.modi
					// tab_tabla.setValor("numero_documento_ingeg",
					// ser_contabilidad.numeroSecuencial(par_sec_egreso));

					Integer fila = tab_tabla_detalle.getFilaActual();
					String key = tab_tabla_detalle.getFilaSeleccionada().getRowKey();
					// System.out.println(fila.toString() + " - " + key);
					/*
					 * for (int i = 0; i < tab_tabla_detalle.getModificadas().size(); i++) { boolean
					 * encontrada = false; for (int j = 0; j < tab_tabla_detalle.getFilas().size();
					 * j++) { if (tab_tabla_detalle.getFila(j).getRowKey()
					 * .equals(tab_tabla_detalle.getModificadas().get(i))) { encontrada = true; } }
					 * if (!encontrada) { tab_tabla_detalle.getModificadas().remove(i); }
					 * 
					 * }
					 */

					if (tab_tabla_detalle.guardar()) {

						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_egreso),
								par_sec_egreso);
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
		System.out.println("pre_egreso_baja_existencia:eliminar");
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		if (!tab_tabla.isFilaInsertada()) {
			System.out.println("No puede eliminar un egreso existente");
			utilitario.agregarMensajeError("No puede eliminar un egreso existente", "");
			return;
		}

		// tab_tabla_detalle.eliminar();
		utilitario.addUpdate("tab_tabla_detalle");
		utilitario.getTablaisFocus().eliminar();
		utilitario.addUpdate("tab_tabla_detalle");
		// utilitario.addUpdate("tab_tabla_detalle");

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

	public void setCom_bodega(Combo com_bodega) {
		this.com_bodega = com_bodega;
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

	public SeleccionTabla getSet_ingresos() {
		return set_ingresos;
	}

	public void setSet_ingresos(SeleccionTabla set_ingresos) {
		this.set_ingresos = set_ingresos;
	}

	public SeleccionTabla getSet_solicitante() {
		return set_solicitante;
	}

	public void setSet_solicitante(SeleccionTabla set_solicitante) {
		this.set_solicitante = set_solicitante;
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

	public SeleccionTabla getSet_jefe_solicitante() {
		return set_jefe_solicitante;
	}

	public void setSet_jefe_solicitante(SeleccionTabla set_jefe_solicitante) {
		this.set_jefe_solicitante = set_jefe_solicitante;
	}

}
