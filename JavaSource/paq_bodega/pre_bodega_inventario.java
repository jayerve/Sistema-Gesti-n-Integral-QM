package paq_bodega;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_bodega_inventario extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color: blue";
	private final String ESTILO_ETIQUETA_ROJA = "font-size:15px;font-weight: bold;text-decoration: underline;color: red";

	private Tabla tab_tabla_inventario = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();

	private Map<String, Object> p_parametros = new HashMap<String, Object>();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();

	private SeleccionTabla set_item_catalogo = new SeleccionTabla();
	private SeleccionTabla set_partida_presupuestaria = new SeleccionTabla();
	private SeleccionTabla set_partida_presupuestaria_reporte_fechas = new SeleccionTabla();
	private SeleccionTabla set_tipo_transaccion = new SeleccionTabla();

	private Dialogo dialogo_estimacion = new Dialogo();

	private Grid grid_dialogo = new Grid();
	private Texto txt_meses_estimacion = new Texto();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario
			.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_bodega_inventario() {
		System.out.println("pre_bodega_inventario");
		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		tab_tabla_inventario.setId("tab_tabla_inventario");
		tab_tabla_inventario.setSql(ser_bodega.getSqlInventarioActual("-1", "-1"));

		tab_tabla_inventario.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla_inventario.getColumna("ide_boubi").setVisible(false);
		
		tab_tabla_inventario.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla_inventario.getColumna("ide_bocam").setVisible(true);
		tab_tabla_inventario.getColumna("ide_bocam").setFiltroContenido();

		tab_tabla_inventario.getColumna("cat_codigo_bocam").setFiltroContenido();
		tab_tabla_inventario.getColumna("cat_codigo_bocam").setNombreVisual("COD. CATALOGO");

		tab_tabla_inventario.getColumna("descripcion_bocam").setFiltroContenido();
		tab_tabla_inventario.getColumna("descripcion_bocam").setNombreVisual("CATALOGO");

		tab_tabla_inventario.getColumna("cantidad_saldo_boinv").setEstilo(ESTILO_ETIQUETA);

		tab_tabla_inventario.getColumna("pmp_existencia_inres").setEstilo(ESTILO_ETIQUETA_ROJA);
		tab_tabla_inventario.getColumna("pmp_existencia_inres").setFormatoNumero(4);

		tab_tabla_inventario.setRows(30);
		tab_tabla_inventario.setLectura(true);
		tab_tabla_inventario.dibujar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		com_bodega.setCombo(ser_bodega.getBodegas());
		com_bodega.setMetodo("seleccionaParametros");
		com_bodega.setStyle("width: 100px; margin: 0 0 -8px 0;");

		Boton bot_recalcular = new Boton();
		bot_recalcular.setValue("Recalcular");
		bot_recalcular.setTitle("Recalcular");
		bot_recalcular.setIcon("ui-icon-note");
		bot_recalcular.setMetodo("recalcularInventario");

		Boton bot_recalcular_pmp = new Boton();
		bot_recalcular_pmp.setValue("Recalcular PMP");
		bot_recalcular_pmp.setTitle("Recalcular PMP");
		bot_recalcular_pmp.setIcon("ui-icon-note");
		bot_recalcular_pmp.setMetodo("recalcularInventarioYPMP");

		if (utilitario.getVariable("ide_usua").equals("395") || utilitario.getVariable("ide_usua").equals("10") || utilitario.getVariable("ide_usua").equals("7")) { // dluje abecerra
			bar_botones.agregarBoton(bot_recalcular);
			bar_botones.agregarBoton(bot_recalcular_pmp);
		}

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarComponente(new Etiqueta("BODEGA:"));
		bar_botones.agregarComponente(com_bodega);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla_inventario);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);

		dialogo_estimacion.setId("dialogo_estimacion");
		dialogo_estimacion.setTitle("SELECCION CODIGO DE BARRAS LOTE");
		dialogo_estimacion.setHeight("20%");
		dialogo_estimacion.setWidth("30%");
		grid_dialogo.setColumns(2);
		txt_meses_estimacion.setMaxlength(4);

		txt_meses_estimacion.setSoloEnteros();

		grid_dialogo.getChildren().add(new Etiqueta("Meses para estimación:"));
		grid_dialogo.getChildren().add(txt_meses_estimacion);

		dialogo_estimacion.getBot_aceptar().setMetodo("aceptarDialogo");
		dialogo_estimacion.setDialogo(grid_dialogo);
		agregarComponente(dialogo_estimacion);

		set_item_catalogo.setId("set_item_catalogo");
		set_item_catalogo.setSeleccionTabla(ser_bodega.getCatalogoExistenciasItemsParaBusqueda(), "ide_bocam");
		set_item_catalogo.setTitle("Seleccione el ítem del catálogo");
		set_item_catalogo.getBot_aceptar().setMetodo("aceptarItemCatalogo");
		// set_item_catalogo.setRadio();
		set_item_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();
		set_item_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setNombreVisual("Catálogo");
		agregarComponente(set_item_catalogo);

		set_partida_presupuestaria.setId("set_partida_presupuestaria");
		set_partida_presupuestaria.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"),
				"ide_prcla");
		set_partida_presupuestaria.setTitle("Seleccione la partida presupuestaria");
		set_partida_presupuestaria.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido();
		set_partida_presupuestaria.getTab_seleccion().getColumna("codigo_clasificador_prcla").setNombreVisual("Código");
		set_partida_presupuestaria.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();
		set_partida_presupuestaria.getTab_seleccion().getColumna("descripcion_clasificador_prcla")
				.setNombreVisual("Descripción");
		set_partida_presupuestaria.setRadio();

		set_partida_presupuestaria.getBot_aceptar().setMetodo("aceptarPartidaPresupuestaria");

		agregarComponente(set_partida_presupuestaria);

		set_partida_presupuestaria_reporte_fechas.setId("set_partida_presupuestaria_reporte_fechas");
		set_partida_presupuestaria_reporte_fechas
				.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla");
		set_partida_presupuestaria_reporte_fechas.setTitle("Seleccione la partida presupuestaria");
		set_partida_presupuestaria_reporte_fechas.getTab_seleccion().getColumna("codigo_clasificador_prcla")
				.setFiltroContenido();
		set_partida_presupuestaria_reporte_fechas.getTab_seleccion().getColumna("codigo_clasificador_prcla")
				.setNombreVisual("Código");
		set_partida_presupuestaria_reporte_fechas.getTab_seleccion().getColumna("descripcion_clasificador_prcla")
				.setFiltroContenido();
		set_partida_presupuestaria_reporte_fechas.getTab_seleccion().getColumna("descripcion_clasificador_prcla")
				.setNombreVisual("Descripción");
		set_partida_presupuestaria_reporte_fechas.setRadio();

		set_partida_presupuestaria_reporte_fechas.getBot_aceptar()
				.setMetodo("aceptarPartidaPresupuestariaReporteFechas");

		agregarComponente(set_partida_presupuestaria_reporte_fechas);

		set_tipo_transaccion.setId("set_tipo_transaccion");
		set_tipo_transaccion.setSeleccionTabla("bodt_inventario_tipo_transaccion", "ide_inttr", "detalle_inttr");
		set_tipo_transaccion.setTitle("Seleccione el tipo de transacción");

		set_tipo_transaccion.getBot_aceptar().setMetodo("aceptarTipoTransaccion");
		set_tipo_transaccion.setRadio();
		agregarComponente(set_tipo_transaccion);

	}

	public void aceptarPartidaPresupuestariaReporteFechas() {
		String ide_prcla = set_partida_presupuestaria_reporte_fechas.getValorSeleccionado();
		p_parametros = new HashMap<String, Object>();
		p_parametros.put("ide_prcla", Long.parseLong(ide_prcla));

		set_partida_presupuestaria_reporte_fechas.cerrar();
		set_tipo_transaccion.dibujar();
	}

	public void aceptarTipoTransaccion() {
		String ide_inttr = set_tipo_transaccion.getValorSeleccionado();
		p_parametros.put("ide_inttr", Long.parseLong(ide_inttr));
		p_parametros.put("titulo", "Resumen General Anual Cronológico por partida");
		p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
		p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
		p_parametros.put("ide_empr", 0);
		p_parametros.put("ide_sucu", 1);

		set_tipo_transaccion.cerrar();
		System.out.println(p_parametros);
		System.out.println("aceptarReporte " + rep_reporte.getPath());
		sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
		sef_reporte.dibujar();
	}

	public void aceptarItemCatalogo() {
		// Cuando se de clic en Aceptar en el dialogo
		String str_seleccionado = set_item_catalogo.getTab_seleccion().getFilasSeleccionadas(); // set_item_catalogo.getValorSeleccionado();

		if (str_seleccionado != null) {
			p_parametros = new HashMap<String, Object>();
			rep_reporte.cerrar();
			TablaGenerica tg = utilitario.consultar(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
			p_parametros.put("titulo", "KARDEX POR ÍTEMS " + tg.getValor("detalle_geani").toString());
			p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
			p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
			p_parametros.put("ide_empr", 0);
			p_parametros.put("ide_sucu", 1);

			p_parametros.put("items_ide_bocam", str_seleccionado);
			p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

			System.out.println(p_parametros);
			System.out.println("aceptarReporte " + rep_reporte.getPath());
			sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
			sef_reporte.dibujar();
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_item_catalogo.cerrar();
	}

	public void aceptarPartidaPresupuestaria() {
		// Cuando se de clic en Aceptar en el dialogo
		String str_seleccionado = set_partida_presupuestaria.getValorSeleccionado();

		if (str_seleccionado != null) {
			p_parametros = new HashMap<String, Object>();
			rep_reporte.cerrar();
			TablaGenerica tg = utilitario.consultar(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
			p_parametros.put("titulo", "RESUMEN GENERAL POR PARTIDA ");
			p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
			p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
			p_parametros.put("ide_empr", 0);
			p_parametros.put("ide_sucu", 1);

			p_parametros.put("ide_prcla", Long.valueOf(pckUtilidades.CConversion.CInt(str_seleccionado)));

			p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

			System.out.println(p_parametros);
			System.out.println("aceptarReporte " + rep_reporte.getPath());
			sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
			sef_reporte.dibujar();
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_partida_presupuestaria.cerrar();
	}

	/**
	 * Método ejecutado al aceptar el dialogo de la estimación de meses
	 */
	public void aceptarDialogo() {
		if (dialogo_estimacion.isVisible()) {
			dialogo_estimacion.cerrar();

			if (pckUtilidades.CConversion.CInt(txt_meses_estimacion.getValue().toString()) <= 0) {
				utilitario.agregarMensajeError("Error", "Ingrese un valor válido");
				return;
			}

			p_parametros = new HashMap<String, Object>();
			TablaGenerica tg = utilitario.consultar(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
			p_parametros.put("titulo", "ESTIMACIÓN DE CONSUMOS" + tg.getValor("detalle_geani").toString());
			p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
			p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
			p_parametros.put("ide_empr", 0);
			p_parametros.put("ide_sucu", 1);
			p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
			p_parametros.put("meses_estimacion", Integer.valueOf(txt_meses_estimacion.getValue().toString()));

			System.out.println(p_parametros);
			System.out.println("aceptarReporte " + rep_reporte.getPath());
			sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
			sef_reporte.dibujar();
		}

	}

	/**
	 * Ejecutar el recalculo del resumen del inventario para el ítem selecionado
	 */
	public void recalcularInventario() {
		String ide_geani = com_anio.getValue().toString();
		String ide_boubi = com_bodega.getValue().toString();
		String ide_bocam = tab_tabla_inventario.getValor("ide_bocam");

		ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, false);
		// System.out.println("ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi + "
		// ide_bocam: " + ide_bocam);
		utilitario.agregarMensaje("Item " + ide_bocam + " actualizado.", "");

	}

	/**
	 * Ejecutar el recalculo del resumen del inventario para el ítem selecionado
	 */
	public void recalcularInventarioYPMP() {
		String ide_geani = com_anio.getValue().toString();
		String ide_boubi = com_bodega.getValue().toString();
		String ide_bocam = tab_tabla_inventario.getValor("ide_bocam");

		ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, true);
		// System.out.println("ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi + "
		// ide_bocam: " + ide_bocam);
		utilitario.agregarMensaje("Item " + ide_bocam + " actualizado.", "");

	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		System.out.println("Reporte seleccionado: " + rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Resumen General Anual agrupado por partida")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();

				TablaGenerica tab_anio = utilitario
						.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+ com_anio.getValue().toString());
				p_parametros.put("titulo", "RESUMEN GENERAL ANUAL POR PARTIDA " + tab_anio.getValor("detalle_geani").toString());
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("ide_geani",
						Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));

				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen General Anual agrupado por familia")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();

				TablaGenerica tab_anio = utilitario
						.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="
								+ com_anio.getValue().toString());
				p_parametros.put("titulo", "RESUMEN GENERAL ANUAL POR FAMILIA " + tab_anio.getValor("detalle_geani").toString());
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("ide_geani",
						Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));

				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen General Anual por Bodega")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio = utilitario
						.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="
								+ com_anio.getValue().toString());
				p_parametros.put("titulo",
						"RESUMEN GENERAL ANUAL POR BODEGA " + tab_anio.getValor("detalle_geani").toString());
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("ide_geani",
						Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Kardex Individual Anual")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio = utilitario
						.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="
								+ com_anio.getValue().toString());
				p_parametros.put("titulo", "KARDEX INDIVIDUAL ANUAL " + tab_anio.getValor("detalle_geani").toString());
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_geani",
						Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Estimación de Consumos")) {
			if (rep_reporte.isVisible()) {

				rep_reporte.cerrar();
				dialogo_estimacion.dibujar();

			}
		} else if (rep_reporte.getReporteSelecionado().equals("Kardex por Items")) {
			if (rep_reporte.isVisible()) {

				set_item_catalogo.getTab_seleccion().setSql(ser_bodega.getCatalogoExistenciasItemsParaBusqueda());
				set_item_catalogo.getTab_seleccion().ejecutarSql();
				set_item_catalogo.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen General Anual por Partida Presupuestaria")) {
			if (rep_reporte.isVisible()) {
				set_partida_presupuestaria.getTab_seleccion()
						.setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true", com_anio.getValue().toString()));
				set_partida_presupuestaria.getTab_seleccion().ejecutarSql();
				set_partida_presupuestaria.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen General Anual Cronológico por partida")) {
			if (rep_reporte.isVisible()) {
				rep_reporte.cerrar();
				set_partida_presupuestaria_reporte_fechas.getTab_seleccion()
						.setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true", com_anio.getValue().toString()));
				set_partida_presupuestaria_reporte_fechas.getTab_seleccion().ejecutarSql();
				set_partida_presupuestaria_reporte_fechas.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen de ingresos cronológico por proveedor")) {
			// Ticket #588618
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio = utilitario
						.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="
								+ com_anio.getValue().toString());
				p_parametros.put("titulo", "RESUMEN INGRESOS POR PROVEEDOR " + tab_anio.getValor("detalle_geani").toString());
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_inttr", 1L);
				p_parametros.put("ide_geani",
						Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));

				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		}

	}

	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla_inventario.setSql(ser_bodega.getSqlInventarioActual(com_anio.getValue().toString(),
					com_bodega.getValue().toString()));
			tab_tabla_inventario.ejecutarSql();
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

		if (tab_tabla_inventario.isFocus()) {
			tab_tabla_inventario.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");

			tab_tabla_inventario.setValor("ide_gtemp", ide_gtempxx);
			tab_tabla_inventario.setValor("ide_geani", com_anio.getValue() + "");
			tab_tabla_inventario.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla_inventario.setValor("ide_inttr", ser_bodega.TRANSACCION_INGRESO.toString());
			tab_tabla_inventario.setValor("activo_ingeg", Boolean.TRUE.toString());
			tab_tabla_inventario.setValor("fecha_ingeg", utilitario.getFechaActual());
			tab_tabla_inventario.setValor("subtotal_ingeg", Double.valueOf(0).toString());
			tab_tabla_inventario.setValor("valor_iva_ingeg", Double.valueOf(0).toString());
			tab_tabla_inventario.setValor("total_ingeg", Double.valueOf(0).toString());

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
		// tab_tabla_inventario.eliminar();
	}
	

	public Tabla getTab_tabla_inventario() {
		return tab_tabla_inventario;
	}

	public void setTab_tabla_inventario(Tabla tab_tabla_inventario) {
		this.tab_tabla_inventario = tab_tabla_inventario;
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

	public Dialogo getDialogo_estimacion() {
		return dialogo_estimacion;
	}

	public void setDialogo_estimacion(Dialogo dialogo_estimacion) {
		this.dialogo_estimacion = dialogo_estimacion;
	}

	public Grid getGrid_dialogo() {
		return grid_dialogo;
	}

	public void setGrid_dialogo(Grid grid_dialogo) {
		this.grid_dialogo = grid_dialogo;
	}

	public SeleccionTabla getSet_item_catalogo() {
		return set_item_catalogo;
	}

	public void setSet_item_catalogo(SeleccionTabla set_item_catalogo) {
		this.set_item_catalogo = set_item_catalogo;
	}

	public Texto getTxt_meses_estimacion() {
		return txt_meses_estimacion;
	}

	public void setTxt_meses_estimacion(Texto txt_meses_estimacion) {
		this.txt_meses_estimacion = txt_meses_estimacion;
	}

	public String getESTILO_ETIQUETA() {
		return ESTILO_ETIQUETA;
	}

	public String getESTILO_ETIQUETA_ROJA() {
		return ESTILO_ETIQUETA_ROJA;
	}

	public SeleccionTabla getSet_partida_presupuestaria() {
		return set_partida_presupuestaria;
	}

	public void setSet_partida_presupuestaria(SeleccionTabla set_partida_presupuestaria) {
		this.set_partida_presupuestaria = set_partida_presupuestaria;
	}

	public ServicioPresupuesto getSer_presupuesto() {
		return ser_presupuesto;
	}

	public void setSer_presupuesto(ServicioPresupuesto ser_presupuesto) {
		this.ser_presupuesto = ser_presupuesto;
	}

	public SeleccionTabla getSet_partida_presupuestaria_reporte_fechas() {
		return set_partida_presupuestaria_reporte_fechas;
	}

	public void setSet_partida_presupuestaria_reporte_fechas(SeleccionTabla set_partida_presupuestaria_reporte_fechas) {
		this.set_partida_presupuestaria_reporte_fechas = set_partida_presupuestaria_reporte_fechas;
	}

	public SeleccionTabla getSet_tipo_transaccion() {
		return set_tipo_transaccion;
	}

	public void setSet_tipo_transaccion(SeleccionTabla set_tipo_transaccion) {
		this.set_tipo_transaccion = set_tipo_transaccion;
	}

}
