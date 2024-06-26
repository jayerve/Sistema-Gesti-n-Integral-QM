package paq_bodega;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_ingreso_lote_bodega extends Pantalla {
	private Tabla tab_bodega = new Tabla();
	private Tabla tab_anio = new Tabla();

	private Tabla tab_activos_fijos = new Tabla();
	private double duo_iva;
	private Combo com_anio = new Combo();
	public static String par_grupo_material;
	public static String par_secuencial_modulo;

	private SeleccionTabla set_material = new SeleccionTabla();
	private SeleccionTabla set_proveedor = new SeleccionTabla();
	private SeleccionTabla set_actualizaproveedor = new SeleccionTabla();
	private SeleccionTabla set_actualizamaterial = new SeleccionTabla();
	private SeleccionTabla set_guardar = new SeleccionTabla();
	private Dialogo dia_bodega = new Dialogo();
	private Radio lis_activo = new Radio();
	private Confirmar con_guardar = new Confirmar();
	private Confirmar con_guardar_material = new Confirmar();
	private SeleccionTabla set_inventario_saldo = new SeleccionTabla();

	double dou_cantidad_ingreso_bobod = 0;
	double dou_valor_unitario_bobod = 0;
	double dou_valor_total_bobod = 0;
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	@EJB
	private ServicioContabilidad ser_Contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_ingreso_lote_bodega() {
		par_secuencial_modulo = utilitario.getVariable("p_modulo_sec_bod_ingresos");

		rep_reporte.setId("rep_reporte"); // id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);// agrega el componente a la pantalla
		bar_botones.agregarReporte();// aparece el boton de reportes en la barra
										// de botones
		self_reporte.setId("self_reporte"); // id
		agregarComponente(self_reporte);

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_bodega.setId("tab_bodega");
		tab_bodega.setTabla("bodt_bodega", "ide_bobod", 1);
		tab_bodega.setCampoOrden("ide_bobod desc");
		tab_bodega.getColumna("ide_geani").setVisible(false);
		tab_bodega.getColumna("ide_tepro").setCombo(ser_Bodega.getProveedor("true,false"));
		tab_bodega.getColumna("ide_tepro").setAutoCompletar();
		tab_bodega.getColumna("ide_tepro").setLectura(true);
		tab_bodega.getColumna("ide_bomat").setCombo("select ide_bomat,codigo_bomat,detalle_bomat,iva_bomat from bodt_material order by detalle_bomat");
		tab_bodega.getColumna("ide_bomat").setAutoCompletar();
		tab_bodega.getColumna("ide_bomat").setLectura(true);
		tab_bodega.getColumna("ide_coest").setVisible(false);
		tab_bodega.getColumna("existencia_anterior_bobod").setVisible(false);
		tab_bodega.getColumna("saldo_bobod").setVisible(false);
		tab_bodega.getColumna("ide_adsoc").setVisible(false);
		tab_bodega.getColumna("ide_boinv").setVisible(false);
		tab_bodega.getColumna("cantidad_ingreso_bobod").setMetodoChange("calcular");
		tab_bodega.getColumna("valor_unitario_bobod").setMetodoChange("calcular");
		tab_bodega.getColumna("valor_total_bobod").setEtiqueta();
		tab_bodega.getColumna("valor_total_bobod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_bodega.getColumna("numero_ingreso_bobod").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
		tab_bodega.getColumna("recibido_bobod").setValorDefecto("true");
		tab_bodega.getColumna("recibido_bobod").setLectura(true);
		tab_bodega.getColumna("activo_bobod").setValorDefecto("true");
		tab_bodega.getColumna("activo_bobod").setLectura(true);

		// Schubert D. Rodriguez
		tab_bodega.getColumna("color_bobod").setVisible(false);
		tab_bodega.getColumna("valor_unitario_bobod").setVisible(false);
		tab_bodega.getColumna("marca_bobod").setVisible(false);
		tab_bodega.getColumna("serie_bobod").setVisible(false);
		tab_bodega.getColumna("saldo_bobod").setVisible(false);
		tab_bodega.getColumna("ide_comov").setVisible(false);
		tab_bodega.getColumna("num_doc_bobod").setVisible(false);
		tab_bodega.getColumna("valor_total_bobod").setVisible(false);
		tab_bodega.getColumna("modelo_bobod").setVisible(false);

		List listax = new ArrayList();
		Object fila6[] = { "0", "CONSUMO INTERNO" };
		Object fila7[] = { "1", "CONSUMO EXTERNO" };
		// Object fila8[] = {"2", "ACTIVOS FIJOS"};
		listax.add(fila6);
		listax.add(fila7);
		// listax.add(fila8);

		tab_bodega.getColumna("tipo_ingreso_bobod").setRadio(listax, "0");
		tab_bodega.getColumna("tipo_ingreso_bobod").setRadioVertical(true);

		tab_bodega.setCondicion("ide_geani=-1 and tipo_ingreso_bobod=2");
		tab_bodega.setTipoFormulario(true);
		tab_bodega.getGrid().setColumns(6);

		/* Schubert D. Rodriguez => Agregacion de relacion */

		tab_bodega.agregarRelacion(tab_activos_fijos);
		tab_bodega.dibujar();

		/*
		 * Schubert D. Rodriguez => Aumento de tab_activos como relacion de
		 * detalle de activos
		 */
		tab_activos_fijos.setId("tab_activos_fijos");
		tab_activos_fijos.setTabla("afi_activo", "ide_afact", 2);
		tab_activos_fijos.setCampoForanea("ide_bobod");
		tab_activos_fijos.getColumna("nro_factura_afact").setVisible(false);
		tab_activos_fijos.setCampoOrden("ide_afact desc");
		tab_activos_fijos.getColumna("ide_afubi").setCombo("afi_ubicacion", "ide_afubi", "detalle_afubi", "");
		tab_activos_fijos.getColumna("ide_aftia").setCombo("afi_tipo_activo", "ide_aftia", "detalle_aftia", "");
		tab_activos_fijos.getColumna("ide_aftip").setCombo("afi_tipo_propiedad", "ide_aftip", "detalle_aftip", "");
		tab_activos_fijos.getColumna("ide_afseg").setCombo("afi_seguro", "ide_afseg", "detalle_afseg", "");

		tab_activos_fijos.getColumna("ide_afnoa").setCombo("afi_nombre_activo", "ide_afnoa", "detalle_afnoa", "");

		// Schubert D. Rodriguez
		tab_activos_fijos.getColumna("ide_afnoa").setMetodoChange("cambiarVehiculo");

		tab_activos_fijos.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_activos_fijos.getColumna("ide_afacd").setCombo("afi_actividad", "ide_afacd", "detalle_afacd", "");

		// FIXME: Schubert Rodriguez Add pre_clasificador
		tab_activos_fijos.getColumna("ide_prcla").setCombo(ser_Contabilidad.getPreClasificacion());
		tab_activos_fijos.getColumna("ide_prcla").setAutoCompletar();
		tab_activos_fijos.getColumna("ide_prcla").setRequerida(true);
		tab_activos_fijos.getColumna("ide_prcla").setMetodoChange("cambiarCuentaContable");

		tab_activos_fijos.getColumna("ide_cocac").setCombo(ser_Contabilidad.getCuentaContable("true,false"));
		tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
		tab_activos_fijos.getColumna("ide_cocac").setRequerida(true);

		tab_activos_fijos.getColumna("activo_afact").setValorDefecto("true");

		// tab_activos_fijos.getColumna("ide_afest").setCombo("afi_estado",
		// "ide_afest",
		// "detalle_afest,porcentaje_afest", "");
		tab_activos_fijos.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest", "activo_afest = true");

		tab_activos_fijos.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_activos_fijos.getColumna("ide_tepro").setAutoCompletar();
		tab_activos_fijos.getColumna("foto_bien_afact").setUpload("ACTIVOS");
		tab_activos_fijos.getColumna("foto_bien_afact").setValorDefecto("imagenes/activo_jpg");
		tab_activos_fijos.getColumna("foto_bien_afact").setImagen("128", "128");
		tab_activos_fijos.getColumna("valor_unitario_afact").setMetodoChange("calcularAF");
		tab_activos_fijos.getColumna("cantidad_afact").setMetodoChange("calcularAF");
		tab_activos_fijos.getColumna("valor_neto_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_neto_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("valor_neto_afact").setMetodoChange("calcularAF");
		tab_activos_fijos.getColumna("valor_compra_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_compra_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("secuencial_afact").setEtiqueta();
		tab_activos_fijos.getColumna("secuencial_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.setTipoFormulario(true);
		tab_activos_fijos.getGrid().setColumns(4);
		tab_activos_fijos.onSelect("seleccionarActivo");

		/* Iva Calculation */
		tab_activos_fijos.getColumna("valor_iva").setCombo(utilitario.getIVAListOptions());

		tab_activos_fijos.getColumna("valor_iva").setMetodoChange("seleccionaIVA");
		tab_activos_fijos.getColumna("valor_iva").setRequerida(true);

		/* Combo comprobate */
		tab_activos_fijos.getColumna("tipo_comprobante").setCombo(utilitario.getComprobanteListOptions());

		tab_activos_fijos.dibujar();

		tab_activos_fijos.setCondicion("ide_bobod=-1");

		tab_activos_fijos.dibujar();

		/************************************************************/

		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Material");
		bot_material.setTitle("MATERIAL");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarMaterial");
		bar_botones.agregarBoton(bot_material);
		par_grupo_material = utilitario.getVariable("p_grupo_material");

		set_material.setId("set_material");
		set_material.setSeleccionTabla(ser_Bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
		set_material.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_material.getBot_aceptar().setMetodo("aceptarMaterial");
		set_material.getTab_seleccion().ejecutarSql();
		agregarComponente(set_material);

		bar_botones.agregarBoton(bot_material);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		Boton bot_actualizar = new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Material");
		bot_actualizar.setMetodo("actualizarMaterial");
		bar_botones.agregarBoton(bot_actualizar);

		set_actualizamaterial.setId("set_actualizamaterial");
		set_actualizamaterial.setSeleccionTabla(ser_Bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
		set_actualizamaterial.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_actualizamaterial.getBot_aceptar().setMetodo("modificarMaterial");
		set_actualizamaterial.setRadio();
		agregarComponente(set_actualizamaterial);

		// /// BOTONES AGREGAR Y MODIFICAR PROVEEDOR
		Boton bot_proveedor = new Boton();
		bot_proveedor.setValue("Agregar Proveedor");
		bot_proveedor.setTitle("MATERIAL");
		bot_proveedor.setIcon("ui-icon-person");
		bot_proveedor.setMetodo("importarProveedor");

		set_proveedor.setId("set_proveedor");
		set_proveedor.setSeleccionTabla(ser_Bodega.getProveedor("true"), "");
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.getTab_seleccion().ejecutarSql();

		bar_botones.agregarBoton(bot_material);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		Boton bot_actualizarproveedor = new Boton();
		bot_actualizarproveedor.setIcon("ui-icon-person");
		bot_actualizarproveedor.setValue("Registrar Proveedor");
		bot_actualizarproveedor.setMetodo("actualizarProveedor");
		bar_botones.agregarBoton(bot_actualizarproveedor);

		set_actualizaproveedor.setId("set_actualizaproveedor");
		set_actualizaproveedor.setSeleccionTabla(ser_Bodega.getProveedor("true"), "");
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
		dia_bodega.setId("dia_bodega");
		dia_bodega.setTitle("SELECCIONE AÑADIR AL INVENTARIO / NO AÑADIR AL INVENTARIO ");
		dia_bodega.getBot_aceptar().setMetodo("aceptaInventario");
		dia_bodega.setDialogo(lis_activo);
		dia_bodega.setHeight("40%");
		dia_bodega.setWidth("40%");
		dia_bodega.setDynamic(false);
		agregarComponente(dia_bodega);

		// /Agregamos boton saldos inventario

		Boton bot_inventario_saldo = new Boton();
		bot_inventario_saldo.setValue("Consultar Saldo Inventario");
		bot_inventario_saldo.setTitle("SALDOS INVENTARIO");
		bot_inventario_saldo.setIcon("ui-icon-person");
		bot_inventario_saldo.setMetodo("importarSaldo");
		bar_botones.agregarBoton(bot_inventario_saldo);
		set_inventario_saldo.setId("set_inventario_saldo");
		set_inventario_saldo.setSeleccionTabla(ser_Bodega.getDatosInventarioAnio("-1"), "ide_boinv");
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
		pat_activo_fijos.setPanelTabla(tab_activos_fijos);

		Division div_aux = new Division();
		div_aux.dividir1(pat_activo_fijos);

		Division div_division = new Division();
		div_division.dividir2(pat_bodega, div_aux, "50%", "h");

		agregarComponente(div_division);

	}

	public void seleccionaIVA(AjaxBehaviorEvent evt) {
		tab_activos_fijos.modificar(evt);
		try {
			if (tab_activos_fijos.getValor("valor_iva") != null) {
				this.duo_iva = Double.parseDouble(tab_activos_fijos.getValor("valor_iva"));
				// ADD duo_iva
				this.calcularAF();
			}
		} catch (Exception e) {
			System.out.print("Error");
		}

	}

	public void cambiarCuentaContable(AjaxBehaviorEvent evt) {
		tab_activos_fijos.modificar(evt);
		if (tab_activos_fijos.getValor("ide_prcla") == null) {
			return;
		}
		tab_activos_fijos.getColumna("ide_cocac").setCombo(ser_Contabilidad.getCuentaContable(Integer.valueOf(tab_activos_fijos.getValor("ide_prcla"))));
		utilitario.addUpdate("ide_cocac");
		// tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
	}

	public void importarSaldo() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_inventario_saldo.getTab_seleccion().setSql(ser_Bodega.getDatosInventarioAnio(com_anio.getValue().toString()));
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
				resultado = ser_Bodega.registraInventarioIngresos(tab_bodega.getValor("ide_bomat"), com_anio.getValue().toString(), tab_bodega.getValor("cantidad_ingreso_bobod"), tab_bodega.getValor("valor_total_bobod"));
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
		System.out.println("Entra a actualizar1...");
		set_actualizamaterial.getTab_seleccion().setSql(ser_Bodega.getInventario("0", "true", par_grupo_material));
		set_actualizamaterial.getTab_seleccion().ejecutarSql();
		set_actualizamaterial.dibujar();
	}

	public void modificarMaterial() {
		String str_materialActualizado = set_actualizamaterial.getValorSeleccionado();
		TablaGenerica tab_materialModificado = ser_Bodega.getTablaInventario(str_materialActualizado);
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

		set_material.getTab_seleccion().setSql(ser_Bodega.getInventario("0", "true", par_grupo_material));
		set_material.getTab_seleccion().ejecutarSql();
		set_material.dibujar();

	}

	public void aceptarMaterial() {
		String str_seleccionados = set_material.getSeleccionados();
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
		System.out.println("Entra a actualizar...");
		if (tab_bodega.getValor("ide_geani") == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un año ", "");
			return;
		}
		set_actualizaproveedor.getTab_seleccion().setSql(ser_Bodega.getProveedor("true"));
		set_actualizaproveedor.getTab_seleccion().ejecutarSql();
		set_actualizaproveedor.dibujar();
	}

	public void modificarProveedor() {
		String str_proveedorActualizado = set_actualizaproveedor.getValorSeleccionado();
		TablaGenerica tab_materialModificado = ser_Bodega.getTablaProveedor(str_proveedorActualizado);
		tab_bodega.setValor("IDE_TEPRO", tab_materialModificado.getValor("IDE_TEPRO"));
		utilitario.addUpdate("tab_bodega");
		set_actualizaproveedor.cerrar();
	}

	public void importarProveedor() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		set_proveedor.setSeleccionTabla(ser_Bodega.getProveedor("true"), "");
		set_proveedor.getTab_seleccion().ejecutarSql();
		set_proveedor.dibujar();

	}

	public void aceptarProveedor() {
		String str_seleccionados = set_proveedor.getSeleccionados();
		if (str_seleccionados != null) {
			tab_bodega.insertar();
			tab_bodega.setValor("ide_tepro", str_seleccionados);
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_bodega");
	}

	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_bodega.setCondicion("ide_geani=" + com_anio.getValue());
			tab_bodega.ejecutarSql();
			seleccionaBodega();
			// tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");

		}
	}

	// Schubert D. Rodriguez
	public void seleccionaBodega() {
		if (tab_bodega.getValor("ide_bobod") != null) {
			tab_activos_fijos.setCondicion("ide_bobod =" + tab_bodega.getValor("ide_bobod"));
			tab_activos_fijos.ejecutarSql();
		}
	}

	// /CALCULAR LOS MATERIALES
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
		// double duo_iva=0.12;

		// double
		// duo_iva=Double.parseDouble(utilitario.getVariable("p_valor_iva"));
		double duo_total = 0;

		tab_activos_fijos.setValor("secuencial_afact", tab_activos_fijos.getValor("cantidad_afact"));
		try {
			// Obtenemos el valor de la cantidad
			duo_cantidad_afact = Double.parseDouble(tab_activos_fijos.getValor("cantidad_afact"));
		} catch (Exception e) {

		}

		try {
			// Obtenemos el valor unitari
			duo_valor_unitario_afact = Double.parseDouble(tab_activos_fijos.getValor("valor_unitario_afact"));
		} catch (Exception e) {

		}

		// Calculamos el total
		duo_valor_neto_afact = duo_cantidad_afact * duo_valor_unitario_afact;
		duo_valor_compra_afact = duo_valor_neto_afact * duo_iva;
		duo_total = duo_valor_compra_afact + duo_valor_neto_afact;
		// Asignamos el total a la tabla detalle, con 2 decimales
		tab_activos_fijos.setValor("valor_neto_afact", utilitario.getFormatoNumero(duo_valor_neto_afact, 3));
		tab_activos_fijos.setValor("valor_compra_afact", utilitario.getFormatoNumero(duo_total, 3));

		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_activos_fijos, "valor_neto_afact,valor_compra_afact,secuencial_afact", "");

	}

	public void calcular(AjaxBehaviorEvent evt) {
		tab_bodega.modificar(evt); // Siempre es la primera linea
		calcular();
	}

	// reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Registro de Ingresos"))
		{
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("Titulo", "Registro de Ingresos");
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt("7"));
				p_parametros.put("ide_empr", pckUtilidades.CConversion.CInt("0"));
				p_parametros.put("ide_sucu", pckUtilidades.CConversion.CInt("1"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
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
		// TODO Auto-generated method stub
		super.siguiente();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		if (tab_bodega.isFocus()) {
			this.seleccionaBodega();
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		if (tab_activos_fijos.isFocus()) {
			tab_activos_fijos.insertar();
			return;
		}

		if (tab_bodega.isFocus()) {
			utilitario.agregarMensaje("No se puede insertar", "Debe Agregar Material");
		} else {
			tab_bodega.isFocus();
			tab_bodega.insertar();
			tab_bodega.setValor("numero_ingreso_bobod", ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");

		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_bodega.getValorSeleccionado().equals("-1")) {
			if (tab_bodega.getValor("ide_tepro") == null) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese el Proveedor");
				return;
			}
			if (tab_bodega.getValor("ide_bomat") == null) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Seleccione el Material de Bodega");
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
			if (tab_bodega.getValor("valor_unitario_bobod") == null || tab_bodega.getValor("valor_unitario_bobod").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese el Valor Unitario");
				return;
			} else {
				dia_bodega.dibujar();
			}
		} else {

			tab_bodega.guardar();
			// tab_activos_fijos.setValor("ide_bobod",
			// tab_bodega.getValor("ide_bobod"));
			tab_activos_fijos.guardar();

			utilitario.agregarMensaje("Ingreso Material Individual", "La presente opción solo le permite realizar el registro de ingreso de materiales, mas no actualizar el registro del ingreso");
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_bodega.isFocus()) {
			tab_bodega.eliminar();
		}
		if (tab_activos_fijos.isFocus()) {
			tab_activos_fijos.eliminar();
		}
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

	public Tabla getTab_activos_fijos() {
		return tab_activos_fijos;
	}

	public void setTab_activos_fijos(Tabla tab_activos_fijos) {
		this.tab_activos_fijos = tab_activos_fijos;
	}

}
