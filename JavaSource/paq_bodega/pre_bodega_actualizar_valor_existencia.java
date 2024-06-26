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
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_bodega_actualizar_valor_existencia extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color: blue";
	private final String ESTILO_ETIQUETA_ROJA = "font-size:15px;font-weight: bold;text-decoration: underline;color: red";

	private Tabla tab_tabla = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();

	private Map<String, Object> p_parametros = new HashMap<String, Object>();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	
	private SeleccionTabla set_item_catalogo = new SeleccionTabla();

	private Dialogo dialogo_estimacion = new Dialogo();

	private Grid grid_dialogo = new Grid();
	private Texto txt_meses_estimacion = new Texto();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_bodega_actualizar_valor_existencia() {
		System.out.println("pre_bodega_actualizar_valor_existencia");
		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		tab_tabla.setId("tab_tabla");
		// tab_tabla.setCondicion("ide_boinv=-1");
		tab_tabla.setSql(ser_bodega.getSqlInventarioActual("-1", "-1"));
		// tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setVisible(false);
		;
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla.getColumna("ide_bocam").setVisible(false);

		tab_tabla.getColumna("cat_codigo_bocam").setFiltroContenido();
		tab_tabla.getColumna("cat_codigo_bocam").setNombreVisual("COD. CATALOGO");

		tab_tabla.getColumna("descripcion_bocam").setFiltroContenido();
		tab_tabla.getColumna("descripcion_bocam").setNombreVisual("CATALOGO");

		tab_tabla.getColumna("cantidad_saldo_boinv").setEstilo(ESTILO_ETIQUETA);

		tab_tabla.getColumna("pmp_existencia_inres").setEstilo(ESTILO_ETIQUETA_ROJA);
		tab_tabla.getColumna("pmp_existencia_inres").setFormatoNumero(4);

		tab_tabla.setLectura(true);
		tab_tabla.dibujar();

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

		//bar_botones.agregarBoton(bot_recalcular);
		
		Boton bot_recalcular_resumen = new Boton();
		bot_recalcular_resumen.setValue("Recalcular resumen");
		bot_recalcular_resumen.setTitle("Recalcular resumen");
		bot_recalcular_resumen.setIcon("ui-icon-note");
		bot_recalcular_resumen.setMetodo("recalcularResumen");

		bar_botones.agregarBoton(bot_recalcular_resumen);


		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarComponente(new Etiqueta("BODEGA:"));
		bar_botones.agregarComponente(com_bodega);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

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
	}
	
	public void aceptarItemCatalogo() {
		// Cuando se de clic en Aceptar en el dialogo 
		String str_seleccionado =  set_item_catalogo.getTab_seleccion().getFilasSeleccionadas();  //set_item_catalogo.getValorSeleccionado();
		
		if (str_seleccionado != null) {
			p_parametros = new HashMap<String, Object>();
			rep_reporte.cerrar();
			TablaGenerica tg = utilitario.consultar(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
			p_parametros.put("titulo", "KARDEX POR ÍTEMS " + tg.getValor("detalle_geani").toString());
			p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
			p_parametros.put("ide_geani",
					Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
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

			p_parametros = new HashMap();
			p_parametros.put("titulo", "Codigo de Barras");
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
	 * Ejecutar el recalculo del resumen del inventario para todos los ítems
	 */
	public void recalcularResumen() {
		String ide_geani = com_anio.getValue().toString();
		TablaGenerica tg_ide_bocam = ser_bodega.itemsCatalogoPorAnio(ide_geani);
		
		for (int i = 0; i < tg_ide_bocam.getTotalFilas(); i++) {
			String ide_bocam = tg_ide_bocam.getValor(i, "ide_bocam");
			String ide_boubi = tg_ide_bocam.getValor(i, "ide_boubi");
			// System.out.println("ide_bocam: " + ide_bocam +  "  ide_boubi: " +ide_boubi);
			
			ser_bodega.trigActualizarIngresosInventario(ide_geani, ide_bocam, ide_boubi);
			ser_bodega.trigActualizarEgresosInventario(ide_geani, ide_bocam, ide_boubi);
			ser_bodega.trigActualizarSaldoInventario(ide_geani, ide_bocam, ide_boubi);
		}
		System.out.println("Finalizado recalcularResumen.");
	}
	



	/**
	 * 
	 */
	public void recalcularInventario() {
		if (true) {
			return;
		}
		String ide_geani = com_anio.getValue().toString();

		String fecha_ingeg = "2021-08-01";
		System.out.println(fecha_ingeg);
		TablaGenerica tg_ide_bocam = ser_bodega.itemsCatalogoTransaccionesDespuesDe(fecha_ingeg);
		
		for (int i = 0; i < tg_ide_bocam.getTotalFilas(); i++) {
			String ide_bocam = tg_ide_bocam.getValor(i, "ide_bocam");
			//System.out.println("ide_bocam: " + ide_bocam);
			TablaGenerica tg_transacciones = ser_bodega.transaccionesDeItemDespuesDe(ide_bocam, fecha_ingeg);
			Boolean hay_ingreso = false;
			Double pmp = (double) 0;
			for (int j = 0; j < tg_transacciones.getTotalFilas(); j++) {
				String ide_inegd = tg_transacciones.getValor(j, "ide_inegd");
				String ide_ingeg = tg_transacciones.getValor(j, "ide_ingeg");
				String ide_inttr = tg_transacciones.getValor(j, "ide_inttr");
				String ide_boubi = tg_transacciones.getValor(j, "ide_boubi");
				
				if (ide_inttr.equals("1")) {
					// Si es un ingreso
					hay_ingreso = true;
					TablaGenerica tg_pmp = ser_bodega.pmpDeItemHastaTransaccion(ide_bocam, ide_inegd); 
					pmp = pckUtilidades.CConversion.CDbl(tg_pmp.getValor("pmp_existencia"));
					ser_bodega.actualizarPmpDeItemHastaTransaccion(ide_bocam, ide_inegd);
					
					String valor_existencia_inegd = tg_transacciones.getValor(j, "valor_existencia_inegd");
					ser_bodega.actualizarValorExistencia(ide_inegd, valor_existencia_inegd.toString());
					ser_bodega.recalcularCabeceraDeTransaccion(ide_ingeg);
				} else {
					// Solo se debe cambiar si existe un ingreso previo
					if (hay_ingreso) {
						// Si existió un ingreso previo, cambiar el valor de existencia  por el nuevo pmp calculado.
						// Recalcular el total de bodt_ingreso_egreso_det y de bodt_ingreso_egreso
						ser_bodega.actualizarValorExistencia(ide_inegd, pmp.toString());
						ser_bodega.recalcularCabeceraDeTransaccion(ide_ingeg);
					}
				}
				ser_bodega.trigActualizarIngresosInventario(ide_geani, ide_bocam, ide_boubi);
				ser_bodega.trigActualizarEgresosInventario(ide_geani, ide_bocam, ide_boubi);
				ser_bodega.trigActualizarSaldoInventario(ide_geani, ide_bocam, ide_boubi);
			}
			
		}
		System.out.println("Finalizado recalcularInventario");

	

	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
	

	}
	
	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setSql(ser_bodega.getSqlInventarioActual(com_anio.getValue().toString(),
					com_bodega.getValue().toString()));
			tab_tabla.ejecutarSql();
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
		// tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
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

}
