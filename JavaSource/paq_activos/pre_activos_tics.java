package paq_activos;

import javax.ejb.EJB;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

public class pre_activos_tics extends Pantalla {

	private Tabla tab_activo_tics = new Tabla();

	private SeleccionTabla set_partidas = new SeleccionTabla();
	private SeleccionTabla set_activos = new SeleccionTabla();

	private Dialogo dialogo = new Dialogo();
	private AreaTexto txt_ide_afact = new AreaTexto();

	private String selected_ide_prcla;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_Contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_activos_tics() {
		tab_activo_tics.setId("tab_activo_tics");
		tab_activo_tics.setTabla("afi_activo_tics", "ide_actic", 1);

		tab_activo_tics.getColumna("ide_afact").setNombreVisual("COD. ACTIVO");
		tab_activo_tics.getColumna("ide_afact").setLectura(true);

		tab_activo_tics.getColumna("ide_geare").setNombreVisual("ÁREA");
		tab_activo_tics.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_activo_tics.getColumna("ide_afubi").setNombreVisual("UBICACIÓN");
		tab_activo_tics.getColumna("ide_afubi").setCombo("afi_ubicacion", "ide_afubi", "detalle_afubi", "");
		tab_activo_tics.getColumna("ide_afest").setNombreVisual("ESTADO");
		tab_activo_tics.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest", "");

		tab_activo_tics.getColumna("detalle_actic").setNombreVisual("DETALLE");
		tab_activo_tics.getColumna("marca_actic").setNombreVisual("MARCA");
		tab_activo_tics.getColumna("modelo_actic").setNombreVisual("MODELO");
		tab_activo_tics.getColumna("serie_actic").setNombreVisual("SERIE");
		tab_activo_tics.getColumna("observaciones_actic").setNombreVisual("OBSERVACIONES");
		tab_activo_tics.getColumna("fecha_alta_actic").setNombreVisual("FECHA ALTA");
		tab_activo_tics.getColumna("valor_compra_actic").setNombreVisual("VALOR COMPRA");
		tab_activo_tics.getColumna("vida_util_actic").setNombreVisual("VIDA UTIL");
		tab_activo_tics.getColumna("intangible_actic").setNombreVisual("INTANGIBLE");
		tab_activo_tics.getColumna("descripcion_ubicacion_actic").setNombreVisual("DESCRIPCIÓN UBICACIÓN");
		tab_activo_tics.getColumna("dado_de_baja_actic").setNombreVisual("DADO DE BAJA");

		tab_activo_tics.getColumna("dado_de_baja_actic").setLectura(true);

		tab_activo_tics.getColumna("activo_actic").setVisible(false);

		Boton bot_importar_activo = new Boton();
		bot_importar_activo.setValue("Importar activos");
		bot_importar_activo.setTitle("IMPORTAR");
		bot_importar_activo.setIcon("ui-icon-person");
		bot_importar_activo.setMetodo("mostrarDialogoSeleccionPartida");

		bar_botones.agregarBoton(bot_importar_activo);
		Boton bot_importar_csv = new Boton();
		bot_importar_csv.setValue("Importar activos csv");
		bot_importar_csv.setTitle("IMPORTAR CSV");
		bot_importar_csv.setIcon("ui-icon-person");
		bot_importar_csv.setMetodo("mostrarDialogoImportarCsv");

		bar_botones.agregarBoton(bot_importar_csv);

		tab_activo_tics.setRows(20);
		tab_activo_tics.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_activo_tics);

		agregarComponente(pat_panel);

		set_partidas.setId("set_partidas");
		set_partidas.setTitle("Seleccione la partida presupuestaria");
		set_partidas.setSeleccionTabla(getSqlPartidasDeActivos(), "ide_prcla");
		set_partidas.setRadio();
		set_partidas.getBot_aceptar().setMetodo("mostrarDialogoActivos");

		agregarComponente(set_partidas);

		set_activos.setId("set_activos");
		set_activos.setTitle("Seleccione los activos para importar");
		set_activos.setSeleccionTabla(getSqlResumenActivos("-1"), "ide_afact");
		set_activos.getBot_aceptar().setMetodo("importarActivos");

		agregarComponente(set_activos);

		dialogo.setId("dialogo");
		dialogo.setTitle("INGRESE LOS códigos de bien separados por comas");
		// dialogo.setHeight("20%");
		// dialogo.setWidth("30%");

		dialogo.getBot_aceptar().setMetodo("aceptarImportacionPorCodigo");
		dialogo.setDialogo(txt_ide_afact);
		agregarComponente(dialogo);

	}

	public void aceptarImportacionPorCodigo() {
		String lista_ide_afact = txt_ide_afact.getValue().toString();

		System.out.println("Cantidad de activos a importar: " + lista_ide_afact.split(",").length);
		System.out.println("Importando los activos: " + lista_ide_afact);
		if (lista_ide_afact.equals("")) {
			utilitario.agregarMensajeError("", "Debe seleccionar al menos un activo para importar");
			return;
		}
		TablaGenerica tg_activos_importar = utilitario
				.consultar(getSqlResumenActivosParaImportar(lista_ide_afact));

		for (int i = 0; i < tg_activos_importar.getTotalFilas(); i++) {
			String ide_afact = tg_activos_importar.getValor(i, "ide_afact");
			String detalle_afact = tg_activos_importar.getValor(i, "detalle_afact");
			String marca_afact = tg_activos_importar.getValor(i, "marca_afact");
			String modelo_afact = tg_activos_importar.getValor(i, "modelo_afact");
			String serie_afact = tg_activos_importar.getValor(i, "serie_afact");
			String ide_prcla = tg_activos_importar.getValor(i, "ide_prcla");
			String valor_compra_afact = tg_activos_importar.getValor(i, "valor_compra_afact");
			String fecha_alta_afact = tg_activos_importar.getValor(i, "fecha_alta_afact");
			String observaciones_afact = tg_activos_importar.getValor(i, "observaciones_afact");
			String ide_afest = tg_activos_importar.getValor(i, "ide_afest");
			String vida_util_afact = tg_activos_importar.getValor(i, "vida_util_afact");
			String ide_afubi = tg_activos_importar.getValor(i, "ide_afubi");
			String afi_ubicacion_otro = tg_activos_importar.getValor(i, "afi_ubicacion_otro");
			String ide_geare = tg_activos_importar.getValor(i, "ide_geare");
			String dado_de_baja = tg_activos_importar.getValor(i, "dado_de_baja");

			if (estaImportado(ide_afact)) {
				utilitario.agregarMensajeError("", "Omitiendo el activo " + ide_afact);
				System.out.println("Omitiendo el activo " + ide_afact);
				continue;
			}

			tab_activo_tics.insertar();
			tab_activo_tics.modificar(tab_activo_tics.getFilaActual());
			tab_activo_tics.setValor("ide_afact", ide_afact);
			tab_activo_tics.setValor("detalle_actic", detalle_afact);
			tab_activo_tics.setValor("marca_actic", marca_afact);
			tab_activo_tics.setValor("modelo_actic", modelo_afact);
			tab_activo_tics.setValor("serie_actic", serie_afact);
			tab_activo_tics.setValor("observaciones_actic", observaciones_afact);
			tab_activo_tics.setValor("fecha_alta_actic", fecha_alta_afact);
			tab_activo_tics.setValor("valor_compra_actic", valor_compra_afact);
			tab_activo_tics.setValor("vida_util_actic", vida_util_afact);
			tab_activo_tics.setValor("intangible_actic", esIntangible(ide_prcla).toString());
			tab_activo_tics.setValor("ide_afubi", ide_afubi);
			tab_activo_tics.setValor("ide_geare", ide_geare);
			tab_activo_tics.setValor("descripcion_ubicacion_actic", afi_ubicacion_otro);
			tab_activo_tics.setValor("ide_afest", ide_afest);
			tab_activo_tics.setValor("dado_de_baja_actic", dado_de_baja);

		}

		utilitario.addUpdate("tab_activo_tics");
		dialogo.cerrar();
	}

	private String getSqlPartidasDeActivos() {
		return "SELECT catalogo.ide_prcla, codigo_clasificador_prcla, descripcion_clasificador_prcla, COUNT(*) FROM afi_activo AS activo "
				+ "LEFT JOIN bodt_catalogo_material AS catalogo ON catalogo.ide_bocam = activo.ide_bocam "
				+ "LEFT JOIN pre_clasificador as clasificador ON clasificador.ide_prcla = catalogo.ide_prcla "
				+ "GROUP BY 1,2,3 " + "ORDER BY 2";
	}

	private String getSqlResumenActivos(String ide_prcla) {
		return String.format("WITH activos AS ( " + "    SELECT  "
				+ "        ide_afact, detalle_afact, marca_afact, modelo_afact, serie_afact, catalogo.ide_prcla, valor_compra_afact,  "
				+ "        fecha_alta_afact, observaciones_afact, ide_afest, vida_util_afact, ide_afubi, afi_ubicacion_otro, ide_geare "
				+ "    FROM afi_activo AS activo "
				+ "    LEFT JOIN bodt_catalogo_material AS catalogo ON catalogo.ide_bocam = activo.ide_bocam "
				+ "    LEFT JOIN pre_clasificador as clasificador ON clasificador.ide_prcla = catalogo.ide_prcla "
				+ "    WHERE catalogo.ide_prcla IN (%s) " + "), " + "ultima_acta AS ( " + "    SELECT  "
				+ "        detalle_activo.ide_afact, MAX(ide_afdoc) as ide_afdoc " + "    FROM activos  "
				+ "    LEFT JOIN afi_doc_detalle_activo AS detalle_activo ON activos.ide_afact = detalle_activo.ide_afact "
				+ "    GROUP BY 1 " + "), " + "ultima_acta_tipo AS ( "
				+ "    SELECT ide_afact, ultima_acta.ide_afdoc, afi_tipo_docu.ide_aftidoc, saca_del_inventario_empresarial "
				+ "    FROM ultima_acta " + "    LEFT JOIN afi_docu ON afi_docu.ide_afdoc = ultima_acta.ide_afdoc "
				+ "    LEFT JOIN afi_tipo_docu ON afi_tipo_docu.ide_aftidoc = afi_docu.ide_aftidoc " + "), "
				+ "resumen_activos AS ( " + "    SELECT "
				+ "        activos.*, saca_del_inventario_empresarial as dado_de_baja " + "    FROM activos "
				+ "    LEFT JOIN ultima_acta_tipo ON ultima_acta_tipo.ide_afact = activos.ide_afact "
				+ "    LEFT JOIN afi_activo_tics ON afi_activo_tics.ide_afact = activos.ide_afact "
				+ "    WHERE afi_activo_tics.ide_afact IS NULL " + ") "
				+ "SELECT * FROM resumen_activos WHERE resumen_activos.ide_afact IS NOT NULL ", ide_prcla);
	}

	private String getSqlResumenActivosParaImportar(String ide_prcla, String ide_afact) {
		return String.format("WITH activos AS ( " + "    SELECT  "
				+ "        ide_afact, detalle_afact, marca_afact, modelo_afact, serie_afact, catalogo.ide_prcla, valor_compra_afact,  "
				+ "        fecha_alta_afact, observaciones_afact, ide_afest, vida_util_afact, ide_afubi, afi_ubicacion_otro, ide_geare "
				+ "    FROM afi_activo AS activo "
				+ "    LEFT JOIN bodt_catalogo_material AS catalogo ON catalogo.ide_bocam = activo.ide_bocam "
				+ "    LEFT JOIN pre_clasificador as clasificador ON clasificador.ide_prcla = catalogo.ide_prcla "
				+ "    WHERE catalogo.ide_prcla IN (%s) AND activo.ide_afact IN (%s) " + "), " + "ultima_acta AS ( "
				+ "    SELECT  " + "        detalle_activo.ide_afact, MAX(ide_afdoc) as ide_afdoc "
				+ "    FROM activos  "
				+ "    LEFT JOIN afi_doc_detalle_activo AS detalle_activo ON activos.ide_afact = detalle_activo.ide_afact "
				+ "    GROUP BY 1 " + "), " + "ultima_acta_tipo AS ( "
				+ "    SELECT ide_afact, ultima_acta.ide_afdoc, afi_tipo_docu.ide_aftidoc, saca_del_inventario_empresarial "
				+ "    FROM ultima_acta " + "    LEFT JOIN afi_docu ON afi_docu.ide_afdoc = ultima_acta.ide_afdoc "
				+ "    LEFT JOIN afi_tipo_docu ON afi_tipo_docu.ide_aftidoc = afi_docu.ide_aftidoc " + "), "
				+ "resumen_activos AS ( " + "    SELECT "
				+ "        activos.*, saca_del_inventario_empresarial as dado_de_baja " + "    FROM activos "
				+ "    LEFT JOIN ultima_acta_tipo ON ultima_acta_tipo.ide_afact = activos.ide_afact "
				+ "    LEFT JOIN afi_activo_tics ON afi_activo_tics.ide_afact = activos.ide_afact "
				+ "    WHERE afi_activo_tics.ide_afact IS NULL " + ") "
				+ "SELECT * FROM resumen_activos WHERE resumen_activos.ide_afact IS NOT NULL ", ide_prcla, ide_afact);
	}

	private String getSqlResumenActivosParaImportar(String ide_afact) {
		return String.format("WITH activos AS ( " + "    SELECT  "
				+ "        ide_afact, detalle_afact, marca_afact, modelo_afact, serie_afact, catalogo.ide_prcla, valor_compra_afact,  "
				+ "        fecha_alta_afact, observaciones_afact, ide_afest, vida_util_afact, ide_afubi, afi_ubicacion_otro, ide_geare "
				+ "    FROM afi_activo AS activo "
				+ "    LEFT JOIN bodt_catalogo_material AS catalogo ON catalogo.ide_bocam = activo.ide_bocam "
				+ "    LEFT JOIN pre_clasificador as clasificador ON clasificador.ide_prcla = catalogo.ide_prcla "
				+ "    WHERE activo.ide_afact IN (%s) " + "), " + "ultima_acta AS ( " + "    SELECT  "
				+ "        detalle_activo.ide_afact, MAX(ide_afdoc) as ide_afdoc " + "    FROM activos  "
				+ "    LEFT JOIN afi_doc_detalle_activo AS detalle_activo ON activos.ide_afact = detalle_activo.ide_afact "
				+ "    GROUP BY 1 " + "), " + "ultima_acta_tipo AS ( "
				+ "    SELECT ide_afact, ultima_acta.ide_afdoc, afi_tipo_docu.ide_aftidoc, saca_del_inventario_empresarial "
				+ "    FROM ultima_acta " + "    LEFT JOIN afi_docu ON afi_docu.ide_afdoc = ultima_acta.ide_afdoc "
				+ "    LEFT JOIN afi_tipo_docu ON afi_tipo_docu.ide_aftidoc = afi_docu.ide_aftidoc " + "), "
				+ "resumen_activos AS ( " + "    SELECT "
				+ "        activos.*, saca_del_inventario_empresarial as dado_de_baja " + "    FROM activos "
				+ "    LEFT JOIN ultima_acta_tipo ON ultima_acta_tipo.ide_afact = activos.ide_afact "
				+ "    LEFT JOIN afi_activo_tics ON afi_activo_tics.ide_afact = activos.ide_afact "
				+ "    WHERE afi_activo_tics.ide_afact IS NULL " + ") "
				+ "SELECT * FROM resumen_activos WHERE resumen_activos.ide_afact IS NOT NULL ", ide_afact);
	}

	public void mostrarDialogoSeleccionPartida() {
		set_partidas.dibujar();
	}

	public void mostrarDialogoImportarCsv() {
		dialogo.dibujar();
	}

	public void mostrarDialogoActivos() {
		if (set_partidas.getTab_seleccion().getFilaSeleccionada() == null) {
			utilitario.agregarMensajeError("", "Debe seleccionar una partida");
			return;
		}
		selected_ide_prcla = set_partidas.getValorSeleccionado();
		System.out.println("ide_prcla: " + selected_ide_prcla);
		set_partidas.cerrar();
		set_activos.getTab_seleccion().setSql(getSqlResumenActivos(selected_ide_prcla));
		set_activos.getTab_seleccion().ejecutarSql();
		set_activos.dibujar();
	}

	public void importarActivos() {
		String lista_ide_afact = set_activos.getTab_seleccion().getFilasSeleccionadas();
		System.out.println("Importando los activos: " + lista_ide_afact);
		if (lista_ide_afact.equals("")) {
			utilitario.agregarMensajeError("", "Debe seleccionar al menos un activo para importar");
			return;
		}
		TablaGenerica tg_activos_importar = utilitario
				.consultar(getSqlResumenActivosParaImportar(selected_ide_prcla, lista_ide_afact));

		for (int i = 0; i < tg_activos_importar.getTotalFilas(); i++) {
			String ide_afact = tg_activos_importar.getValor(i, "ide_afact");
			String detalle_afact = tg_activos_importar.getValor(i, "detalle_afact");
			String marca_afact = tg_activos_importar.getValor(i, "marca_afact");
			String modelo_afact = tg_activos_importar.getValor(i, "modelo_afact");
			String serie_afact = tg_activos_importar.getValor(i, "serie_afact");
			String ide_prcla = tg_activos_importar.getValor(i, "ide_prcla");
			String valor_compra_afact = tg_activos_importar.getValor(i, "valor_compra_afact");
			String fecha_alta_afact = tg_activos_importar.getValor(i, "fecha_alta_afact");
			String observaciones_afact = tg_activos_importar.getValor(i, "observaciones_afact");
			String ide_afest = tg_activos_importar.getValor(i, "ide_afest");
			String vida_util_afact = tg_activos_importar.getValor(i, "vida_util_afact");
			String ide_afubi = tg_activos_importar.getValor(i, "ide_afubi");
			String afi_ubicacion_otro = tg_activos_importar.getValor(i, "afi_ubicacion_otro");
			String ide_geare = tg_activos_importar.getValor(i, "ide_geare");
			String dado_de_baja = tg_activos_importar.getValor(i, "dado_de_baja");

			if (estaImportado(ide_afact)) {
				utilitario.agregarMensajeError("", "Omitiendo el activo " + ide_afact);
				System.out.println("Omitiendo el activo " + ide_afact);
				continue;
			}

			tab_activo_tics.insertar();
			tab_activo_tics.modificar(tab_activo_tics.getFilaActual());
			tab_activo_tics.setValor("ide_afact", ide_afact);
			tab_activo_tics.setValor("detalle_actic", detalle_afact);
			tab_activo_tics.setValor("marca_actic", marca_afact);
			tab_activo_tics.setValor("modelo_actic", modelo_afact);
			tab_activo_tics.setValor("serie_actic", serie_afact);
			tab_activo_tics.setValor("observaciones_actic", observaciones_afact);
			tab_activo_tics.setValor("fecha_alta_actic", fecha_alta_afact);
			tab_activo_tics.setValor("valor_compra_actic", valor_compra_afact);
			tab_activo_tics.setValor("vida_util_actic", vida_util_afact);
			tab_activo_tics.setValor("intangible_actic", esIntangible(ide_prcla).toString());
			tab_activo_tics.setValor("ide_afubi", ide_afubi);
			tab_activo_tics.setValor("ide_geare", ide_geare);
			tab_activo_tics.setValor("descripcion_ubicacion_actic", afi_ubicacion_otro);
			tab_activo_tics.setValor("ide_afest", ide_afest);
			tab_activo_tics.setValor("dado_de_baja_actic", dado_de_baja);

		}

		utilitario.addUpdate("tab_activo_tics");
		set_activos.cerrar();
	}

	public Boolean esIntangible(String ide_prcla) {
		System.out.println("esIntangible: " + ide_prcla);
		String partida = obtenerPartida(ide_prcla);
		System.out.println("partida: " + partida);
		if (partida.equals("84.04.01") || partida.equals("84.04.02") || partida.equals("84.04.03")
				|| partida.equals("84.04.04") || partida.equals("63.07.02") || partida.equals("73.07.02")
				|| partida.equals("53.07.02")) {
			return true;
		}
		return false;
	}

	public String obtenerPartida(String ide_prcla) {
		String sql = String.format(
				"SELECT ide_prcla, codigo_clasificador_prcla " + "FROM pre_clasificador WHERE ide_prcla = %s",
				ide_prcla);
		TablaGenerica tg = utilitario.consultar(sql);
		if (tg.getTotalFilas() > 0)
			return tg.getValor("codigo_clasificador_prcla");
		return "";
	}

	/**
	 * Verifica si un activo ya fue importado a afi_activo_tics
	 * 
	 * @param ide_afact
	 */
	public boolean estaImportado(String ide_afact) {
		String sql = String.format("SELECT ide_afact, marca_actic FROM afi_activo_tics WHERE ide_afact = %s",
				ide_afact);
		TablaGenerica tg = utilitario.consultar(sql);
		if (tg.getTotalFilas() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void insertar() {
		tab_activo_tics.insertar();
		tab_activo_tics.setValor("activo_actic", Boolean.TRUE.toString());
	}

	@Override
	public void guardar() {
		System.out.println("pre_activos_tics:guardar");
		tab_activo_tics.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		System.out.println("pre_activos_tics:eliminar");
		tab_activo_tics.eliminar();
	}

	public Tabla getTab_activo_tics() {
		return tab_activo_tics;
	}

	public void setTab_activo_tics(Tabla tab_activo_tics) {
		this.tab_activo_tics = tab_activo_tics;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public ServicioBodega getSer_Bodega() {
		return ser_Bodega;
	}

	public void setSer_Bodega(ServicioBodega ser_Bodega) {
		this.ser_Bodega = ser_Bodega;
	}

	public ServicioContabilidad getSer_Contabilidad() {
		return ser_Contabilidad;
	}

	public void setSer_Contabilidad(ServicioContabilidad ser_Contabilidad) {
		this.ser_Contabilidad = ser_Contabilidad;
	}

	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public SeleccionTabla getSet_activos() {
		return set_activos;
	}

	public void setSet_activos(SeleccionTabla set_activos) {
		this.set_activos = set_activos;
	}

	public SeleccionTabla getSet_partidas() {
		return set_partidas;
	}

	public void setSet_partidas(SeleccionTabla set_partidas) {
		this.set_partidas = set_partidas;
	}

	public String getSelected_ide_prcla() {
		return selected_ide_prcla;
	}

	public void setSelected_ide_prcla(String selected_ide_prcla) {
		this.selected_ide_prcla = selected_ide_prcla;
	}

	public Dialogo getDialogo() {
		return dialogo;
	}

	public void setDialogo(Dialogo dialogo) {
		this.dialogo = dialogo;
	}

	public AreaTexto getTxt_ide_afact() {
		return txt_ide_afact;
	}

	public void setTxt_ide_afact(AreaTexto txt_ide_afact) {
		this.txt_ide_afact = txt_ide_afact;
	}

}
