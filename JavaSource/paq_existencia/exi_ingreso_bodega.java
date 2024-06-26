package paq_existencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_existencia.ejb.ServicioExistencias;
import paq_nomina.ejb.ServicioNomina;
import paq_existencia.ejb.ServicioExistencias;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 * Pantalla de ingreso por lotes de activos
 * 
 * @author ccaceres
 * @since 2018/04/06
 */
public class exi_ingreso_bodega extends Pantalla {

	private Tabla tab_bodega = new Tabla();
	private Tabla tab_anio = new Tabla();
	private Tabla tab_existencias = new Tabla();
	private Tabla tab_obj_existencia = new Tabla();
	private double duo_iva;

	private Combo com_anio = new Combo();
	public static String par_grupo_material;
	public static String par_secuencial_modulo;
	public static String par_catalogo_bodega;
	public static String p_valor_iva;

	private SeleccionTabla set_material = new SeleccionTabla();
	private SeleccionTabla set_proveedor = new SeleccionTabla();
	private SeleccionTabla set_actualizaproveedor = new SeleccionTabla();
	private SeleccionTabla set_actualizamaterial = new SeleccionTabla();
	private SeleccionTabla set_guardar = new SeleccionTabla();
	private Dialogo dia_bodega = new Dialogo();
	private Dialogo dia_activo_lote = new Dialogo();
	private Radio lis_activo = new Radio();
	private Confirmar con_guardar = new Confirmar();
	private Confirmar con_guardar_material = new Confirmar();
	private SeleccionTabla set_inventario_saldo = new SeleccionTabla();
	private SeleccionTabla set_solicitud = new SeleccionTabla();

	double dou_cantidad_ingreso_bobod = 0;
	double dou_valor_unitario_bobod = 0;
	double dou_valor_total_bobod = 0;
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();

	private String empleado;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioExistencias ser_existencia = (ServicioExistencias) utilitario.instanciarEJB(ServicioExistencias.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	/**
	 * Funci&oacute;n que define todo el comportamiento de la cabecera del
	 * ingreso en pantalla
	 */
	private void cabeceraIngreso() {
		tab_bodega.setId("tab_bodega");
		tab_bodega.setTabla("bodt_bodega", "ide_bobod", 1);
		tab_bodega.setCampoOrden("ide_bobod desc");
		tab_bodega.getColumna("ide_geani").setVisible(false);
		tab_bodega.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_bodega.getColumna("ide_tepro").setAutoCompletar();
		tab_bodega.getColumna("ide_tepro").setLectura(true);
		tab_bodega.getColumna("CANTIDAD_INGRESO_BOBOD").setValorDefecto("0");

		tab_bodega.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_bodega.getColumna("ide_gtemp").setLectura(true);
		tab_bodega.getColumna("ide_gtemp").setAutoCompletar();

		tab_bodega.getColumna("ide_bomat").setCombo("select ide_bomat,codigo_bomat,detalle_bomat,iva_bomat from bodt_material order by detalle_bomat");
		tab_bodega.getColumna("ide_bomat").setAutoCompletar();
		tab_bodega.getColumna("ide_bomat").setLectura(true);
		tab_bodega.getColumna("ide_coest").setVisible(false);
		tab_bodega.getColumna("existencia_anterior_bobod").setVisible(false);
		tab_bodega.getColumna("saldo_bobod").setVisible(false);
		tab_bodega.getColumna("ide_adsoc").setVisible(false);
		tab_bodega.getColumna("ide_boinv").setVisible(false);
		tab_bodega.getColumna("cantidad_ingreso_bobod").setLectura(true);
		// tab_bodega.getColumna("cantidad_ingreso_bobod").setMetodoChange("calcular");
		tab_bodega.getColumna("numero_ingreso_bobod").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
		tab_bodega.getColumna("recibido_bobod").setValorDefecto("true");
		tab_bodega.getColumna("recibido_bobod").setLectura(true);
		tab_bodega.getColumna("activo_bobod").setValorDefecto("true");
		tab_bodega.getColumna("activo_bobod").setLectura(true);
		tab_bodega.getColumna("color_bobod").setVisible(false);
		tab_bodega.getColumna("valor_unitario_bobod").setLectura(true);
		tab_bodega.getColumna("valor_unitario_bobod").setValorDefecto("0");
		tab_bodega.getColumna("es_existencia").setVisible(false);
		tab_bodega.getColumna("es_existencia").setValorDefecto("true");
		tab_bodega.getColumna("VALOR_TOTAL_BOBOD").setLectura(true);
		tab_bodega.getColumna("VALOR_TOTAL_BOBOD").setValorDefecto("0");
		tab_bodega.getColumna("marca_bobod").setVisible(false);
		tab_bodega.getColumna("serie_bobod").setVisible(false);
		tab_bodega.getColumna("saldo_bobod").setVisible(false);
		tab_bodega.getColumna("ide_comov").setVisible(false);
		tab_bodega.getColumna("num_doc_bobod").setVisible(false);
		tab_bodega.getColumna("modelo_bobod").setVisible(false);
		tab_bodega.getColumna("ide_bomat").setVisible(false);
		tab_bodega.getColumna("TIPO_INGRESO_BOBOD").setVisible(false);
		// tab_bodega.getColumna("CANTIDAD_INGRESO_BOBOD").setVisible(false);
		tab_bodega.getColumna("RECIBIDO_BOBOD").setVisible(false);
		// tab_bodega.getColumna("NUMERO_INGRESO_BOBOD").setVisible(false);
		tab_bodega.getColumna("ACTIVO_BOBOD").setVisible(false);
		tab_bodega.getColumna("FECHA_INGRESO_BOBOD").setValorDefecto(utilitario.getFechaActual());

		tab_bodega.getColumna("ide_boubi").setCombo("SELECT ide_boubi, detalle_boubi FROM bodt_bodega_ubicacion;");
		tab_bodega.getColumna("ide_boubi").setAutoCompletar();
		tab_bodega.getColumna("ide_boubi").setValorDefecto("1");

		tab_bodega.getColumna("es_ingreso_por_cierre").setLectura(true);

		List listax = new ArrayList();
		Object fila6[] = { "0", "CONSUMO INTERNO" };
		Object fila7[] = { "1", "CONSUMO EXTERNO" };
		// Object fila8[] = {"2", "ACTIVOS FIJOS"};
		listax.add(fila6);
		listax.add(fila7);
		// listax.add(fila8);
		tab_bodega.getColumna("tipo_ingreso_bobod").setRadio(listax, "1");
		tab_bodega.getColumna("tipo_ingreso_bobod").setRadioVertical(true);
		tab_bodega.setCondicion("ide_geani=-1 and es_existencia=true ");
		tab_bodega.setTipoFormulario(true);
		tab_bodega.getGrid().setColumns(6);
		tab_bodega.agregarRelacion(tab_existencias);
		tab_bodega.dibujar();
	}

	/**
	 * Funci&oacute;n que define todo el comportamiento del detalle en el
	 * ingreso en pantalla
	 */
	private void detalleIngreso() {
		tab_existencias.setId("tab_existencias");
		tab_existencias.setTabla("bodt_existencia", "ide_exist", 3);
		tab_existencias.setCampoForanea("ide_bobod");

		String items = "'" + par_catalogo_bodega.replace(";", "','") + "'";
		System.out.println("Items para catalogo de existencias: " + items);

		tab_existencias.getColumna("ide_bocam").setCombo(ser_bodega.getCatalagoBodega("true", items));
		tab_existencias.getColumna("ide_bocam").setAutoCompletar();
		tab_existencias.getColumna("ide_bocam").setLongitud(5);
		tab_existencias.getColumna("ide_bocam").setRequerida(true);

		tab_existencias.getColumna("ide_bounm").setCombo(ser_bodega.getMedidas());
		tab_existencias.getColumna("ide_bounm").setAutoCompletar();
		tab_existencias.getColumna("ide_bounm").setLongitud(5);
		tab_existencias.getColumna("ide_bounm").setRequerida(true);
		tab_existencias.getColumna("ide_bounm").setValorDefecto("1");

		// true para cuando ingresa, false cuando sale
		tab_existencias.getColumna("exist_ingresa").setValorDefecto("true");
		tab_existencias.getColumna("exist_ingresa").setVisible(false);

		tab_existencias.getColumna("activo_exist").setValorDefecto("true");
		tab_existencias.getColumna("activo_exist").setVisible(false);

		tab_existencias.getColumna("ide_boubi").setVisible(false);
		tab_existencias.getColumna("ide_geani").setVisible(false);

		tab_existencias.getColumna("exist_cantidad").setValorDefecto("1");
		tab_existencias.getColumna("exist_cantidad").setMetodoChange("calcularDetalle");

		tab_existencias.getColumna("exist_valor_total_sin_iva").setValorDefecto("1");
		tab_existencias.getColumna("exist_valor_total_sin_iva").setMetodoChange("calcularDetalle");

		tab_existencias.getColumna("exist_valor_unit_sin_iva").setEtiqueta();
		tab_existencias.getColumna("exist_valor_unit_con_iva").setEtiqueta();
		tab_existencias.getColumna("exist_valor_total_con_iva").setEtiqueta();

		tab_existencias.getColumna("ide_afdoc").setValorDefecto(null);
		tab_existencias.getColumna("ide_afdoc").setVisible(false);

		tab_existencias.getColumna("valor_iva").setCombo(utilitario.getIVAListOptions());
		tab_existencias.getColumna("valor_iva").setMetodoChange("calcularDetalle");
		tab_existencias.getColumna("valor_iva").setRequerida(true);
		tab_existencias.getColumna("valor_iva").setValorDefecto(p_valor_iva);

		tab_existencias.getColumna("esta_inventariado").setValorDefecto("false");
		tab_existencias.getColumna("esta_inventariado").setVisible(false);

		tab_existencias.getColumna("exist_cantidad_saldo").setVisible(false);

		tab_existencias.getColumna("ide_exist").setOrden(1);
		tab_existencias.getColumna("ide_bocam").setOrden(2);
		tab_existencias.getColumna("exist_cantidad").setOrden(3);
		tab_existencias.getColumna("exist_valor_total_sin_iva").setOrden(4);
		tab_existencias.getColumna("exist_valor_unit_sin_iva").setOrden(5);
		tab_existencias.getColumna("valor_iva").setOrden(6);
		tab_existencias.getColumna("exist_valor_unit_con_iva").setOrden(7);
		tab_existencias.getColumna("exist_valor_total_con_iva").setOrden(8);

		tab_existencias.getColumna("ide_bnpeli_salud").setCombo(ser_bodega.getPeligrosidad("1"));
		tab_existencias.getColumna("ide_bnpeli_salud").setRequerida(true);
		tab_existencias.getColumna("ide_bnpeli_salud").setValorDefecto("1");

		tab_existencias.getColumna("ide_bnpeli_inflama").setCombo(ser_bodega.getPeligrosidad("2"));
		tab_existencias.getColumna("ide_bnpeli_inflama").setRequerida(true);
		tab_existencias.getColumna("ide_bnpeli_inflama").setValorDefecto("6");

		tab_existencias.getColumna("ide_bnpeli_inesta").setCombo(ser_bodega.getPeligrosidad("3"));
		tab_existencias.getColumna("ide_bnpeli_inesta").setRequerida(true);
		tab_existencias.getColumna("ide_bnpeli_inesta").setValorDefecto("11");
		// tab_existencias.getColumna("riesgo_especifico_exist");

		tab_existencias.dibujar();
		tab_existencias.setCondicion("ide_bobod=-1");
		tab_existencias.dibujar();
	}

	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_existencias.modificar(evt); // Siempre es la primera linea
		this.calcularValores();
	}

	public void calcularValores() {
		if (tab_existencias.getValor("valor_iva") != null) {
			double cantidad = 0;
			double valor_total_sin_iva = 0;
			double valor_unit_sin_iva = 0;
			double valor_unit_con_iva = 0;
			double valor_total_con_iva = 0;
			double valor_iva = 0;

			try {

				cantidad = pckUtilidades.CConversion.CDbl_2(tab_existencias.getValor("exist_cantidad"));
			} catch (Exception e) {

			}

			try {

				valor_total_sin_iva = pckUtilidades.CConversion.CDbl_2(tab_existencias.getValor("exist_valor_total_sin_iva"));
			} catch (Exception e) {

			}
			try {

				valor_iva = pckUtilidades.CConversion.CDbl_2(tab_existencias.getValor("valor_iva"));
			} catch (Exception e) {

			}

			if (cantidad > 0 && valor_total_sin_iva > 0) {
				valor_unit_sin_iva = valor_total_sin_iva / cantidad;
				valor_unit_con_iva = (valor_unit_sin_iva * valor_iva) + valor_unit_sin_iva;
				valor_total_con_iva = cantidad * valor_unit_con_iva;

				tab_existencias.setValor("exist_valor_unit_sin_iva", utilitario.getFormatoNumero(valor_unit_sin_iva, 3));
				tab_existencias.setValor("exist_valor_unit_con_iva", utilitario.getFormatoNumero(valor_unit_con_iva, 3));
				tab_existencias.setValor("exist_valor_total_con_iva", utilitario.getFormatoNumero(valor_total_con_iva, 3));

				utilitario.addUpdateTabla(tab_existencias, "exist_valor_unit_sin_iva,exist_valor_unit_con_iva,exist_valor_total_con_iva", "");
			}
			this.cuadrarDetalle();
		}
	}

	/**
	 * 
	 */
	private void botones() {

	}

	/**
	 * Funci&oacute;n que define todo el comportamiento del registro de activo
	 * para duplicarse en el ingreso en pantalla
	 */
	public void IngresoLotes() {

	}

	public void mostrarDialogoLotes() {

		if (tab_bodega.getValor("ide_bobod") != null) {
			utilitario.agregarMensajeError("NO SE PUEDE EDITAR ESTE REGISTRO", "Esta pantalla solo permite el ingreso de nuevos registros");
			return;
		}

		if (tab_bodega.getValor("IDE_TEPRO") == null) {
			utilitario.agregarMensajeInfo("Debe registrar un Proveedor", "");
			return;
		}
		if (tab_bodega.getValor("FECHA_INGRESO_BOBOD") == null || tab_bodega.getValor("FECHA_INGRESO_BOBOD").isEmpty()) {
			utilitario.agregarMensajeInfo("Debe registrar la fecha de ingreso", "");
			return;
		}
		tab_obj_existencia.insertar();
		tab_obj_existencia.setValor("IDE_AFUBI", "2");
		tab_obj_existencia.setValor("IDE_AFTIA", "1");
		tab_obj_existencia.setValor("IDE_GEARE", "2");
		tab_obj_existencia.setValor("IDE_AFTIP", "1");
		tab_obj_existencia.setValor("IDE_AFACD", "1");
		tab_obj_existencia.setValor("IDE_AFSEG", "1");
		tab_obj_existencia.setValor("IDE_AFEST", "8");
		tab_obj_existencia.setValor("TIPO_COMPROBANTE", "FAC");
		tab_obj_existencia.setValor("CANTIDAD_AFACT", "1");
		tab_obj_existencia.setValor("IDE_COCAC", "1");
		tab_obj_existencia.setValor("IDE_TEPRO", tab_bodega.getValor("IDE_TEPRO"));
		tab_obj_existencia.setValor("FECHA_ALTA_AFACT", tab_bodega.getValor("FECHA_INGRESO_BOBOD"));

		dia_activo_lote.dibujar();
	}

	public exi_ingreso_bodega() {
		par_secuencial_modulo = utilitario.getVariable("p_modulo_sec_bod_ingresos_existencia");
		par_catalogo_bodega = utilitario.getVariable("p_item_presupuestarios_existencias");
		p_valor_iva = utilitario.getVariable("p_valor_iva");
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		bar_botones.agregarReporte();

		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		cabeceraIngreso();
		detalleIngreso();

		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Factura de Compras");
		bot_buscar.setMetodo("importarSolicitudCompra");
		bar_botones.agregarBoton(bot_buscar);

		set_solicitud.setId("set_solicitud");
		set_solicitud.setSeleccionTabla(ser_existencia.getCompras("true,false"), "ide_adfac");
		set_solicitud.setTitle("SELECCIONE UNA FACTURA DE COMPRA");
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setNombreVisual("Proveedor");
		set_solicitud.getTab_seleccion().getColumna("detalle_adfac").setNombreVisual("Detalle Factura Compra");
		set_solicitud.getTab_seleccion().getColumna("num_factura_adfac").setNombreVisual("Nro. Factura");
		set_solicitud.getTab_seleccion().getColumna("ide_tepro").setVisible(false);
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		// set_solicitud.getTab_seleccion().getColumna("detalle_adsoc").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("num_factura_adfac").setFiltro(true);

		set_solicitud.getBot_aceptar().setMetodo("aceptarSolicitudCompra");
		set_solicitud.setRadio();
		agregarComponente(set_solicitud);
		/************************************************************/
		Boton bot_lotes = new Boton();
		bot_lotes.setValue("Agregar detalle");
		bot_lotes.setIcon("ui-icon-person");
		bot_lotes.setMetodo("insertarDetalle");
		bar_botones.agregarBoton(bot_lotes);

		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Material");
		bot_material.setTitle("MATERIAL");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarMaterial");
		// bar_botones.agregarBoton(bot_material);
		par_grupo_material = utilitario.getVariable("p_grupo_material");

		set_material.setId("set_material");
		set_material.setSeleccionTabla(ser_bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
		set_material.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_material.getBot_aceptar().setMetodo("aceptarMaterial");
		set_material.getTab_seleccion().ejecutarSql();
		agregarComponente(set_material);

		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		// Boton bot_actualizar = new Boton();
		// bot_actualizar.setIcon("ui-icon-person");
		// bot_actualizar.setValue("Actualizar Material");
		// bot_actualizar.setMetodo("actualizarMaterial");
		// bar_botones.agregarBoton(bot_actualizar);

		set_actualizamaterial.setId("set_actualizamaterial");
		set_actualizamaterial.setSeleccionTabla(ser_bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
		set_actualizamaterial.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_actualizamaterial.getBot_aceptar().setMetodo("modificarMaterial");
		set_actualizamaterial.setRadio();
		agregarComponente(set_actualizamaterial);

		// /// BOTONES AGREGAR Y MODIFICAR PROVEEDOR
		// Boton bot_proveedor = new Boton();
		// bot_proveedor.setValue("Agregar Proveedor");
		// bot_proveedor.setTitle("PROVEEDOR");
		// bot_proveedor.setIcon("ui-icon-person");
		// bot_proveedor.setMetodo("importarProveedor");

		set_proveedor.setId("set_proveedor");
		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("true"), "");
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.getTab_seleccion().ejecutarSql();

		// bar_botones.agregarBoton(bot_proveedor);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		Boton bot_actualizarproveedor = new Boton();
		bot_actualizarproveedor.setIcon("ui-icon-person");
		bot_actualizarproveedor.setValue("Registrar Proveedor");
		bot_actualizarproveedor.setMetodo("actualizarProveedor");
		bar_botones.agregarBoton(bot_actualizarproveedor);

		set_actualizaproveedor.setId("set_actualizaproveedor");
		set_actualizaproveedor.setSeleccionTabla(ser_bodega.getProveedor("true"), "");
		set_actualizaproveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_actualizaproveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_actualizaproveedor.getBot_aceptar().setMetodo("modificarProveedor");
		set_actualizaproveedor.setRadio();
		agregarComponente(set_actualizaproveedor);

		List lista = new ArrayList();
		Object fila1[] = { "0", "AÑADIR AL INVENTARIO" };
		Object fila2[] = { "1", "NO AÑADIR AL INVENTARIO" };
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setRadio(lista);
		lis_activo.setVertical();

		// dia_bodega.setId("dia_bodega");
		// dia_bodega.setTitle("SELECCIONE AÑADIR AL INVENTARIO / NO AÑADIR AL INVENTARIO ");
		// dia_bodega.getBot_aceptar().setMetodo("aceptaInventario");
		// dia_bodega.setDialogo(set_actualizaproveedor);
		// dia_bodega.setHeight("40%");
		// dia_bodega.setWidth("40%");
		// dia_bodega.setDynamic(false);
		// agregarComponente(dia_bodega);

		PanelTabla dia_activo_lotept = new PanelTabla();
		// dia_activo_lotept.setPanelTabla(tab_obj_existencia);

		dia_activo_lote.setId("dia_activo_lote");
		dia_activo_lote.setTitle("ACTIVO LOTE");
		dia_activo_lote.getBot_aceptar().setMetodo("IngresoLotes");
		dia_activo_lote.setDialogo(dia_activo_lotept);
		// dia_activo_lote.setHeight("60%");
		dia_activo_lote.setWidth("90%");
		dia_activo_lote.setDynamic(true);
		agregarComponente(dia_activo_lote);
		// /Agregamos boton saldos inventario

		Boton bot_inventario_saldo = new Boton();
		bot_inventario_saldo.setValue("Consultar Saldo Inventario");
		bot_inventario_saldo.setTitle("SALDOS INVENTARIO");
		bot_inventario_saldo.setIcon("ui-icon-person");
		bot_inventario_saldo.setMetodo("importarSaldo");
		// bar_botones.agregarBoton(bot_inventario_saldo);
		set_inventario_saldo.setId("set_inventario_saldo");
		set_inventario_saldo.setSeleccionTabla(ser_bodega.getDatosInventarioAnio("-1"), "ide_boinv");
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_actual").setNombreVisual("SALDO MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_actual").setEstilo("font-size: 14px;color: red;font-weight: bold");
		set_inventario_saldo.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_inventario_saldo.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_inventario_saldo.getTab_seleccion().getColumna("codigo_bomat").setNombreVisual("CODIGO MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("detalle_bomat").setNombreVisual("NOMBRE MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("ide_geani").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("ingreso_material_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("egreso_material_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_inicial_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_anterior_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_actual_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("fecha_ingr_articulo_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_inicial_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("ide_bomat").setVisible(false);
		set_inventario_saldo.getBot_aceptar().setRendered(false);
		set_inventario_saldo.getTab_seleccion().ejecutarSql();
		agregarComponente(set_inventario_saldo);

		PanelTabla pat_bodega = new PanelTabla();
		pat_bodega.setPanelTabla(tab_bodega);

		PanelTabla pat_activo_fijos = new PanelTabla();
		pat_activo_fijos.setPanelTabla(tab_existencias);

		Division div_aux = new Division();
		div_aux.dividir1(pat_activo_fijos);

		Division div_division = new Division();
		div_division.dividir2(pat_bodega, div_aux, "35%", "h");

		agregarComponente(div_division);

	}

	public void importarSolicitudCompra() {
		if (tab_bodega.getValor("ide_bobod") != null) {
			utilitario.agregarMensajeError("NO SE PUEDE EDITAR ESTE REGISTRO", "Esta pantalla solo permite el ingreso de nuevos registros");
			return;
		}

		if (tab_bodega.getValor("ide_geani") == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un año ", "");
			return;
		}
		set_solicitud.getTab_seleccion().setSql(ser_existencia.getCompras("true"));
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.dibujar();
	}

	public void aceptarSolicitudCompra() {

		String str_seleccionado = set_solicitud.getValorSeleccionado();
		// Traigo la cabecera para poder rellenar datos del ingreso
		TablaGenerica tab_solicitud = utilitario.consultar(ser_existencia.getComprasCodigo(str_seleccionado));
		if (str_seleccionado != null) {
			TablaGenerica tab_solicitud_detalle = utilitario.consultar(ser_existencia.getDetalleComprasCodigo(str_seleccionado));
			tab_solicitud.setFilaActual(0);
			// TODO columna de la factura vinculada
			// tab_bodega.setValor("ide_adsoc", str_seleccionado);

			tab_bodega.setValor("ide_tepro", tab_solicitud.getValor("ide_tepro"));
			tab_bodega.setValor("num_factura_bobod", tab_solicitud.getValor("num_factura_adfac"));
			tab_bodega.setValor("descripcion_bobod", tab_solicitud.getValor("detalle_adfac"));
			tab_bodega.setValor("fecha_compra_bobod", tab_solicitud.getValor("fecha_factura_adfac"));

			int size = 0;
			size = tab_solicitud_detalle.getTotalFilas();
			for (int i = 0; i < size; i++) {
				tab_solicitud_detalle.setFilaActual(i);
				tab_existencias.insertar();
				tab_existencias.setValor("exist_cantidad", tab_solicitud_detalle.getValor("cantidad_addef"));
				tab_existencias.setValor("marca_exist", tab_solicitud_detalle.getValor("marca_addef"));
				tab_existencias.setValor("observacion_exist", tab_solicitud_detalle.getValor("descripcion_addef"));
				tab_existencias.setValor("color_exist", tab_solicitud_detalle.getValor("color_addef"));
				tab_existencias.setValor("modelo_exist", tab_solicitud_detalle.getValor("modelo_addef"));

				tab_existencias.setValor("exist_valor_total_sin_iva", tab_solicitud_detalle.getValor("valor_total_addef"));
				tab_existencias.setValor("exist_valor_unit_sin_iva", tab_solicitud_detalle.getValor("valor_unitario_addef"));

				tab_existencias.setValor("exist_valor_total_con_iva", tab_solicitud_detalle.getValor("valor_total_addef"));
				tab_existencias.setValor("exist_valor_total_con_iva", tab_solicitud_detalle.getValor("valor_total_addef"));
				
				if (tab_solicitud_detalle.getValor("valor_total_addef") == "false") {
					tab_existencias.setValor("valor_iva", "0");
				}
				this.calcularValores();
			}
		}
		// utilitario.addUpdateTabla(tab_adq_factura, "ide_adsoc,detalle_adfac",
		// "");
		utilitario.addUpdateTabla(tab_bodega, "ide_tepro,num_factura_bobod,descripcion_bobod,fecha_compra_bobod", "");
		utilitario.addUpdateTabla(tab_existencias, "ide_bocam,marca_exist,modelo_exist,color_exist,valor_iva,exist_valor_unit_sin_iva,exist_valor_unit_con_iva,exist_valor_total_con_iva", "");
		//this.cuadrarDetalle();
		set_solicitud.cerrar();
		// tab_adq_factura.guardar();
		// guardarPantalla();
		// dia_recepcion.dibujar();
	}

	public void seleccionaIVA(AjaxBehaviorEvent evt) {
		tab_existencias.modificar(evt);
		try {
			if (tab_existencias.getValor("valor_iva") != null) {
				this.duo_iva = Double.parseDouble(tab_existencias.getValor("valor_iva"));
				// ADD duo_iva
				this.calcularAF();
			}
		} catch (Exception e) {
			System.out.print("Error");
		}
	}

	public void cambiarCuentaContable(AjaxBehaviorEvent evt) {
		tab_existencias.modificar(evt);
		if (tab_existencias.getValor("ide_prcla") == null) {
			return;
		}
		tab_existencias.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable(Integer.valueOf(tab_existencias.getValor("ide_prcla"))));
		utilitario.addUpdate("ide_cocac");
		// tab_existencias.getColumna("ide_cocac").setAutoCompletar();
	}

	public void importarSaldo() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_inventario_saldo.getTab_seleccion().setSql(ser_bodega.getDatosInventarioAnio(com_anio.getValue().toString()));
		set_inventario_saldo.getTab_seleccion().ejecutarSql();
		set_inventario_saldo.dibujar();

	}

	public void aceptaInventario() {
		if (lis_activo.getValue() == null) {
			utilitario.agregarMensajeInfo("Inventario / No-Inventario", "Debe Seleccionar una Opción");
			return;
		} else {
			tab_bodega.setValor("tipo_ingreso_bobod", lis_activo.getValue().toString());
			if (lis_activo.getValue().equals("0")) {
				boolean resultado;
				resultado = ser_bodega.registraInventarioIngresos(tab_bodega.getValor("ide_bomat"), com_anio.getValue().toString(), tab_bodega.getValor("cantidad_ingreso_bobod"), tab_bodega.getValor("valor_total_bobod"));
				if (resultado) {
					utilitario.agregarMensaje("Se guardo correctamente", "El inventario se registro satisfactoriamente.");
					ser_contabilidad.guardaSecuencial(tab_bodega.getValor("numero_ingreso_bobod"), par_secuencial_modulo);

					tab_bodega.guardar();
					dia_bodega.cerrar();
				} else {
					utilitario.agregarMensaje("Error en el Registro", "El material guardadano no se registro en inventarios");
					return;
				}

			} else {
				ser_contabilidad.guardaSecuencial(tab_bodega.getValor("numero_ingreso_bobod"), par_secuencial_modulo);

				tab_bodega.guardar();
				dia_bodega.cerrar();
			}
			guardarPantalla();
		}
	}

	public void actualizarMaterial() {
		if (tab_bodega.getValor("ide_geani") == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un año ", "");
			return;

		}
		// System.out.println("Entra a actualizar1...");
		set_actualizamaterial.getTab_seleccion().setSql(ser_bodega.getInventario("0", "true", par_grupo_material));
		set_actualizamaterial.getTab_seleccion().ejecutarSql();
		set_actualizamaterial.dibujar();
	}

	public void modificarMaterial() {
		String str_materialActualizado = set_actualizamaterial.getValorSeleccionado();
		TablaGenerica tab_materialModificado = ser_bodega.getTablaInventario(str_materialActualizado);
		tab_bodega.setValor("IDE_BOMAT", tab_materialModificado.getValor("IDE_BOMAT"));
		tab_bodega.modificar(tab_bodega.getFilaActual());
		utilitario.addUpdate("tab_bodega");

		con_guardar.setMessage("Esta Seguro de Actualizar el Material");
		con_guardar.setTitle("CONFIRMACION ");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzarMaterial");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");

	}

	public void guardarActualilzarMaterial() {
		tab_bodega.guardar();
		con_guardar.cerrar();
		set_actualizamaterial.cerrar();
		guardarPantalla();

	}

	public void importarMaterial() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		set_material.getTab_seleccion().setSql(ser_bodega.getInventario("0", "true", par_grupo_material));
		set_material.getTab_seleccion().ejecutarSql();
		set_material.dibujar();

	}

	public void insertarDetalle() {
		tab_existencias.setFocus();
		this.insertar();
	}

	public void aceptarMaterial() {
		String str_seleccionados = set_material.getSeleccionados();
		System.out.println("aceptarMaterial " + str_seleccionados);
		if (str_seleccionados != null) {
			tab_bodega.insertar();
			tab_bodega.setValor("numero_ingreso_bobod", ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
			tab_bodega.setValor("ide_bomat", str_seleccionados);
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");
		}
		set_material.cerrar();
		utilitario.addUpdateTabla(tab_bodega, "numero_ingreso_bobod", "");
	}

	// //// BOTONES PROVEEDOR
	public void actualizarProveedor() {
		// System.out.println("Entra a actualizar...");
		if (tab_bodega.getValor("ide_bobod") != null) {
			utilitario.agregarMensajeError("NO SE PUEDE EDITAR ESTE REGISTRO", "Esta pantalla solo permite el ingreso de nuevos registros");
			return;
		}

		if (tab_bodega.getValor("ide_geani") == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un año ", "");
			return;
		}
		set_actualizaproveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
		set_actualizaproveedor.getTab_seleccion().ejecutarSql();
		set_actualizaproveedor.dibujar();
		// dia_bodega.dibujar();
	}

	public void modificarProveedor() {

		String str_proveedorActualizado = set_actualizaproveedor.getValorSeleccionado();
		TablaGenerica tab_materialModificado = ser_bodega.getTablaProveedor(str_proveedorActualizado);
		tab_bodega.setValor("IDE_TEPRO", tab_materialModificado.getValor("IDE_TEPRO"));
		utilitario.addUpdate("tab_bodega");
		set_actualizaproveedor.cerrar();

	}

	public void importarProveedor() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("true"), "");
		set_proveedor.getTab_seleccion().ejecutarSql();
		set_proveedor.dibujar();

	}

	public void aceptarProveedor() {
		String str_seleccionados = set_proveedor.getSeleccionados();
		if (str_seleccionados != null) {
			// tab_bodega.insertar();
			tab_bodega.setValor("ide_tepro", str_seleccionados);
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_bodega");
		utilitario.addUpdate("tab_existencias");
	}

	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_bodega.setCondicion("ide_geani=" + com_anio.getValue() + " and es_existencia=true");
			tab_bodega.ejecutarSql();
			seleccionaBodega();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}

	public void seleccionaBodega() {
		if (tab_bodega.getValor("ide_bobod") != null) {
			tab_existencias.setCondicion("ide_bobod =" + tab_bodega.getValor("ide_bobod"));
			tab_existencias.ejecutarSql();
		}
	}

	public void calcular() {
		// Variables para almacenar y calcular el total del detalle
		double dou_cantidad_ingreso_bobod = 0;
		double dou_valor_unitario_bobod = 0;
		double dou_valor_total_bobod = 0;

		try {
			// Obtenemos el valor de la cantidad
			dou_cantidad_ingreso_bobod = Double.parseDouble(tab_bodega.getValor("cantidad_ingreso_bobod"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_valor_unitario_bobod = Double.parseDouble(tab_bodega.getValor("valor_unitario_bobod"));
		} catch (Exception e) {
		}

		// Calculamos el total
		dou_valor_total_bobod = dou_cantidad_ingreso_bobod * dou_valor_unitario_bobod;

		// Asignamos el total a la tabla detalle, con 2 decimales
		tab_bodega.setValor("valor_total_bobod", utilitario.getFormatoNumero(dou_valor_total_bobod, 3));

		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_bodega, "valor_total_bobod", "");

	}

	public void calcularAF() {
		// Variables para almacenar y calcular el total del detalle
		double duo_cantidad_afact = 0;
		double duo_valor_unitario_afact = 0;
		double duo_valor_neto_afact = 0;
		double duo_valor_compra_afact = 0;
		double duo_total = 0;

		try {
			// Obtenemos el valor de la cantidad
			duo_cantidad_afact = Double.parseDouble(tab_existencias.getValor("cantidad_afact"));
		} catch (Exception e) {

		}

		try {
			// Obtenemos el valor unitari
			duo_valor_unitario_afact = Double.parseDouble(tab_existencias.getValor("valor_unitario_afact"));
		} catch (Exception e) {

		}

		// Calculamos el total
		duo_valor_neto_afact = duo_cantidad_afact * duo_valor_unitario_afact;
		duo_valor_compra_afact = duo_valor_neto_afact * duo_iva;
		duo_total = duo_valor_compra_afact + duo_valor_neto_afact;
		// Asignamos el total a la tabla detalle, con 2 decimales
		tab_existencias.setValor("valor_neto_afact", utilitario.getFormatoNumero(duo_valor_neto_afact, 3));
		tab_existencias.setValor("valor_compra_afact", utilitario.getFormatoNumero(duo_total, 3));

		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_existencias, "valor_neto_afact,valor_compra_afact,secuencial_afact", "");
		this.cuadrarDetalle();

	}

	public void calcular(AjaxBehaviorEvent evt) {
		tab_bodega.modificar(evt); // Siempre es la primera linea
		calcular();
	}

	/**
	 * Reportes
	 */
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Comprabante de Ingreso")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "INGRESO DE EXISTENCIAS No. " + tab_bodega.getValor("numero_ingreso_bobod"));
				p_parametros.put("num_ingreso", pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")));
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				System.out.println(pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")) + " aceptarReporte " + rep_reporte.getPath());
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}

	@Override
	public void aceptarBuscar() {
		super.aceptarBuscar();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void actualizar() {
		super.actualizar();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();

		}
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();

		}
	}

	@Override
	public void siguiente() {
		super.siguiente();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void fin() {
		super.fin();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void atras() {
		super.atras();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void insertar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (tab_existencias.isFocus()) {
			if (tab_bodega.getValor("ide_bobod") != null) {
				utilitario.agregarMensajeError("NO SE PUEDE EDITAR ESTE REGISTRO", "Esta pantalla solo permite el ingreso de nuevos registros");
				return;
			}
			if (tab_bodega.getValor("IDE_TEPRO") == null) {
				utilitario.agregarMensajeInfo("Debe registrar un Proveedor", "");
				return;
			}
			if (tab_bodega.getValor("FECHA_INGRESO_BOBOD") == null || tab_bodega.getValor("FECHA_INGRESO_BOBOD").isEmpty()) {
				utilitario.agregarMensajeInfo("Debe registrar la fecha de ingreso", "");
				return;
			}
			tab_existencias.insertar();
			tab_existencias.getColumna("ide_geani").setValorDefecto(tab_bodega.getValor("ide_geani"));
			tab_existencias.getColumna("ide_boubi").setValorDefecto(tab_bodega.getValor("ide_boubi"));
			// TODO VALORES POR DEFECTO

			this.calcularValores();
			return;
		}

		if (tab_bodega.isFocus()) {
			// utilitario.agregarMensaje("No se puede insertar",
			// "Debe Agregar Material");
			// } else {
			tab_bodega.isFocus();
			tab_bodega.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_bodega.setValor("ide_gtemp", ide_gtempxx);
			tab_bodega.setValor("numero_ingreso_bobod", ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");
		}
	}

	public void cuadrarDetalle() {
		int numFil = tab_existencias.getTotalFilas();
		double valorSinIva = 0;
		double valorConIva = 0;
		double cantTotal = 0;
		double acSinIva = 0;
		double acConIva = 0;
		double cant = 0;
		// TODO: validar cuadre con cabecera
		for (int i = 0; i < numFil; i++) {
			tab_existencias.setFilaActual(i);
			tab_existencias.setValor("ide_geani", tab_bodega.getValor("ide_geani"));
			tab_existencias.setValor("ide_boubi", tab_bodega.getValor("ide_boubi"));
			tab_existencias.setValor("exist_cantidad_saldo", tab_existencias.getValor("exist_cantidad"));
			try {
				acSinIva = Double.parseDouble(tab_existencias.getValor("exist_valor_total_sin_iva"));
				acConIva = Double.parseDouble(tab_existencias.getValor("exist_valor_total_con_iva"));
				cant = Double.parseDouble(tab_existencias.getValor("exist_cantidad"));
			} catch (Exception e) {
				acSinIva = 0;
				acConIva = 0;
				cant = 0;
			}
			valorSinIva += acSinIva;
			valorConIva += acConIva;
			cantTotal += cant;
		}
		tab_bodega.setValor("valor_unitario_bobod", utilitario.getFormatoNumero(valorSinIva, 3));
		tab_bodega.setValor("VALOR_TOTAL_BOBOD", utilitario.getFormatoNumero(valorConIva, 3));
		// tab_bodega.setValor("CANTIDAD_INGRESO_BOBOD",String.valueOf(tab_existencias.getTotalFilas()));
		tab_bodega.setValor("CANTIDAD_INGRESO_BOBOD", utilitario.getFormatoNumero(cantTotal, 0));
		utilitario.addUpdateTabla(tab_bodega, "CANTIDAD_INGRESO_BOBOD,valor_unitario_bobod,VALOR_TOTAL_BOBOD", "");
	}

	@Override
	public void guardar() {
		this.cuadrarDetalle();

		// TODO Auto-generated method stub

		// System.out.println("Guardar valorseleccionado " +
		// tab_bodega.getValorSeleccionado());
		if (tab_bodega.getValorSeleccionado().equals("-1")) {
			if (tab_bodega.getValor("ide_tepro") == null) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese el Proveedor");
				return;
			}
			if (tab_bodega.getValor("fecha_ingreso_bobod") == null || tab_bodega.getValor("fecha_ingreso_bobod").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese la Fecha Ingreso del Material");
				return;
			}
			if (tab_bodega.getValor("cantidad_ingreso_bobod") == null || tab_bodega.getValor("cantidad_ingreso_bobod").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese la Cantidad");
				return;
			}
			// tab_bodega.setValor("numero_ingreso_bobod",
			// ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
			tab_bodega.guardar();
			tab_existencias.guardar();
			guardarPantalla();
			ser_contabilidad.guardaSecuencial(tab_bodega.getValor("numero_ingreso_bobod"), par_secuencial_modulo);
			this.actualizarInventario();
			// System.out.println("Se guardó un ingreso por lote");
		} else {
			// tab_bodega.guardar();
			// tab_existencias.guardar();
			utilitario.agregarMensaje("Ingreso Material Individual", "La presente opción solo le permite realizar el registro de ingreso de materiales, mas no actualizar el registro del ingreso");
		}
	}

	public void inventariarItem(String ide_boubi, String ide_bocam, String ide_geani, String ide_bounm, String exist_cantidad, String exist_valor_total_con_iva) {
		TablaGenerica tab_bodega_stock = utilitario.consultar(ser_existencia.getSaldoItem(ide_boubi, ide_bocam, ide_geani, ide_bounm));
		double costo_actual_bosto = 0, saldo_existencia_bosto = 0, acConIva = 0, cant = 0, cantidad_ingreso_bosto = 0, ppp = 0;
		// Parseo el item que se va a inventariar
		try {
			acConIva = Double.parseDouble(exist_valor_total_con_iva);
			cant = Double.parseDouble(exist_cantidad);
		} catch (Exception e) {
			acConIva = 0;
			cant = 0;
		}
		System.out.println("exi_ingreso_bodega.inventariarItem() --> {ide_boubi:" + ide_boubi + ",ide_bocam:" + ide_bocam + ",ide_geani:" + ide_geani + ",ide_bounm:" + ide_bounm + ",exist_cantidad:" + exist_cantidad + ",exist_valor_total_con_iva:"
				+ exist_valor_total_con_iva + "}");
		if (tab_bodega_stock.getTotalFilas() > 0) {
			// Obtenigo el saldo anterior
			System.out.println("exi_ingreso_bodega.inventariarItem() --> ACTUALIZAR registro; filas encontradas: " + tab_bodega_stock.getTotalFilas());
			try {
				costo_actual_bosto = Double.parseDouble(tab_bodega_stock.getValor("costo_actual_bosto"));
				saldo_existencia_bosto = Double.parseDouble(tab_bodega_stock.getValor("saldo_existencia_bosto"));
				cantidad_ingreso_bosto = Double.parseDouble(tab_bodega_stock.getValor("cantidad_ingreso_bosto"));
			} catch (Exception e) {
				costo_actual_bosto = 0;
				saldo_existencia_bosto = 0;
				cantidad_ingreso_bosto = 0;
			}
			double vCant = 0, vVal = 0;
			// valor total para poder calcular ppp
			vVal = acConIva + (costo_actual_bosto * saldo_existencia_bosto);
			vCant = saldo_existencia_bosto + cant;// nuevo saldo
			// se suma la cantidad que ingresó
			cantidad_ingreso_bosto = cantidad_ingreso_bosto + cant;
			if (vCant > 0) {
				ppp = vVal / vCant;
			}

			utilitario.getConexion().ejecutarSql(ser_existencia.updateSaldoItem(ide_boubi, ide_bocam, ide_geani, ide_bounm, Double.toString(vCant), Double.toString(cantidad_ingreso_bosto), Double.toString(costo_actual_bosto), Double.toString(ppp)));
		} else {
			// Realizo un insert del item en saldosExistencias
			System.out.println("exi_ingreso_bodega.inventariarItem() --> INSERTAR nuevo registro ");
			if (cant > 0) {
				ppp = acConIva / cant;
			}
			utilitario.getConexion().ejecutarSql(ser_existencia.insertSaldoItem(ide_boubi, ide_bocam, ide_geani, ide_bounm, Double.toString(cant), Double.toString(ppp)));
		}
	}

	public void actualizarInventario() {
		int numFil = tab_existencias.getTotalFilas();
		for (int i = 0; i < numFil; i++) {
			tab_existencias.setFilaActual(i);
			inventariarItem(tab_existencias.getValor("ide_boubi"), tab_existencias.getValor("ide_bocam"), tab_existencias.getValor("ide_geani"), tab_existencias.getValor("ide_bounm"), tab_existencias.getValor("exist_cantidad"),
					tab_existencias.getValor("exist_valor_total_con_iva"));
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_bodega.isFocus()) {
			tab_bodega.eliminar();
		}
		if (tab_existencias.isFocus()) {
			tab_existencias.eliminar();
		}
		this.cuadrarDetalle();
	}

	public Tabla getTab_bodega() {
		return tab_bodega;
	}

	public void setTab_bodega(Tabla tab_bodega) {
		this.tab_bodega = tab_bodega;
	}

	public Tabla getTab_anio() {
		return tab_anio;
	}

	public void setTab_anio(Tabla tab_anio) {
		this.tab_anio = tab_anio;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_inventario_saldo() {
		return set_inventario_saldo;
	}

	public void setSet_inventario_saldo(SeleccionTabla set_inventario_saldo) {
		this.set_inventario_saldo = set_inventario_saldo;
	}

	public SeleccionTabla getSet_material() {
		return set_material;
	}

	public void setSet_material(SeleccionTabla set_material) {
		this.set_material = set_material;
	}

	public SeleccionTabla getSet_actualizamaterial() {
		return set_actualizamaterial;
	}

	public void setSet_actualizamaterial(SeleccionTabla set_actualizamaterial) {
		this.set_actualizamaterial = set_actualizamaterial;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

	public SeleccionTabla getSet_actualizaproveedor() {
		return set_actualizaproveedor;
	}

	public void setSet_actualizaproveedor(SeleccionTabla set_actualizaproveedor) {
		this.set_actualizaproveedor = set_actualizaproveedor;
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

	public Map getMap_parametros() {
		return map_parametros;
	}

	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}

	public Tabla getTab_existencias() {
		return tab_existencias;
	}

	public void setTab_existencias(Tabla tab_existencias) {
		this.tab_existencias = tab_existencias;
	}

	/**
	 * @return the tab_obj_existencia
	 */
	public Tabla getTab_obj_existencia() {
		return tab_obj_existencia;
	}

	/**
	 * @param tab_obj_existencia
	 *            the tab_obj_existencia to set
	 */
	public void setTab_obj_existencia(Tabla tab_obj_existencia) {
		this.tab_obj_existencia = tab_obj_existencia;
	}

	public SeleccionTabla getSet_solicitud() {
		return set_solicitud;
	}

	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_solicitud = set_solicitud;
	}

}
