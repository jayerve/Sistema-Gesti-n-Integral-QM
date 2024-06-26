package paq_bodega;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
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
 * @since 2017/04/06
 */
public class pre_compras_ingreso extends Pantalla {

	private Tabla tab_bodega = new Tabla();
	private Tabla tab_anio = new Tabla();
	private Tabla tab_activos_fijos = new Tabla();
	private Tabla tab_act_fij_obj = new Tabla();
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
	private Dialogo dia_activo_lote = new Dialogo();
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

	private String empleado;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_Contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
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
		tab_bodega.getColumna("ide_tepro").setCombo(ser_Bodega.getProveedor("true,false"));
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
		tab_bodega.getColumna("es_existencia").setValorDefecto("false");
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

		List listax = new ArrayList();
		Object fila6[] = { "0", "CONSUMO INTERNO" };
		Object fila7[] = { "1", "CONSUMO EXTERNO" };
		// Object fila8[] = {"2", "ACTIVOS FIJOS"};
		listax.add(fila6);
		listax.add(fila7);
		// listax.add(fila8);
		tab_bodega.getColumna("tipo_ingreso_bobod").setRadio(listax, "0");
		tab_bodega.getColumna("tipo_ingreso_bobod").setRadioVertical(true);
		tab_bodega.setCondicion("ide_geani=-1 and es_existencia=false");
		tab_bodega.setTipoFormulario(true);
		tab_bodega.getGrid().setColumns(6);
		tab_bodega.agregarRelacion(tab_activos_fijos);
		tab_bodega.dibujar();
	}

	/**
	 * Funci&oacute;n que define todo el comportamiento del detalle en el
	 * ingreso en pantalla
	 */
	private void detalleIngreso() {
		tab_activos_fijos.setId("tab_activos_fijos");
		tab_activos_fijos.setTabla("afi_activo", "ide_afact", 3);
		tab_activos_fijos.setCampoForanea("ide_bobod");
		tab_activos_fijos.getColumna("nro_factura_afact").setVisible(false);
		tab_activos_fijos.setCampoOrden("ide_afact desc");


		tab_activos_fijos.getColumna("ide_bocam").setCombo(ser_bodega.getCatalagoBodega("true"));
		tab_activos_fijos.getColumna("ide_bocam").setAutoCompletar();
		tab_activos_fijos.getColumna("ide_bocam").setLongitud(5);
		tab_activos_fijos.getColumna("ide_bocam").setRequerida(true);

		tab_activos_fijos.getColumna("ide_afubi").setCombo("afi_ubicacion", "ide_afubi", "detalle_afubi", "");
		tab_activos_fijos.getColumna("ide_aftia").setCombo("afi_tipo_activo", "ide_aftia", "detalle_aftia", "");
		tab_activos_fijos.getColumna("ide_aftip").setCombo("afi_tipo_propiedad", "ide_aftip", "detalle_aftip", "");
		tab_activos_fijos.getColumna("ide_afseg").setCombo("afi_seguro", "ide_afseg", "detalle_afseg", "");
		tab_activos_fijos.getColumna("ide_afnoa").setCombo("afi_nombre_activo", "ide_afnoa", "detalle_afnoa", "");
		// tab_activos_fijos.getColumna("ide_afnoa").setMetodoChange("cambiarVehiculo");
		tab_activos_fijos.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_activos_fijos.getColumna("ide_afacd").setCombo("afi_actividad", "ide_afacd", "detalle_afacd", "");
		tab_activos_fijos.getColumna("ide_prcla").setCombo(ser_Contabilidad.getPreClasificacion());
		tab_activos_fijos.getColumna("ide_prcla").setAutoCompletar();
		// tab_activos_fijos.getColumna("ide_prcla").setRequerida(true);
		// tab_activos_fijos.getColumna("ide_prcla").setMetodoChange("cambiarCuentaContable");
		tab_activos_fijos.getColumna("ide_cocac").setCombo(ser_Contabilidad.getCuentaContable("true,false"));
		tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
		tab_activos_fijos.getColumna("ide_cocac").setRequerida(true);
		tab_activos_fijos.getColumna("activo_afact").setValorDefecto("true");
		tab_activos_fijos.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest", "activo_afest = true");
		tab_activos_fijos.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_activos_fijos.getColumna("ide_tepro").setAutoCompletar();
		tab_activos_fijos.getColumna("foto_bien_afact").setUpload("ACTIVOS");
		tab_activos_fijos.getColumna("foto_bien_afact").setValorDefecto("imagenes/activo_jpg");
		tab_activos_fijos.getColumna("foto_bien_afact").setImagen("128", "128");
		tab_activos_fijos.getColumna("valor_unitario_afact").setMetodoChange("calcularAF");
		// tab_activos_fijos.getColumna("cantidad_afact").setMetodoChange("calcularAF");
		tab_activos_fijos.getColumna("cantidad_afact").setLectura(true);
		tab_activos_fijos.getColumna("valor_neto_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_neto_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("valor_neto_afact").setMetodoChange("calcularAF");
		tab_activos_fijos.getColumna("valor_compra_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_compra_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("secuencial_afact").setEtiqueta();
		tab_activos_fijos.getColumna("secuencial_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.setTipoFormulario(false);
		// tab_activos_fijos.getGrid().setColumns(4);
		// tab_activos_fijos.onSelect("seleccionarActivo");
		/* Iva Calculation */
		tab_activos_fijos.getColumna("valor_iva").setCombo(utilitario.getIVAListOptions());
		tab_activos_fijos.getColumna("valor_iva").setMetodoChange("seleccionaIVA");
		tab_activos_fijos.getColumna("valor_iva").setRequerida(true);
		/* Combo comprobate */
		tab_activos_fijos.getColumna("tipo_comprobante").setCombo(utilitario.getComprobanteListOptions());

		tab_activos_fijos.getColumna("foto_bien_afact").setVisible(false);
		tab_activos_fijos.getColumna("valor_residual_afact").setVisible(false);
		tab_activos_fijos.getColumna("VALOR_DEPRE_MES_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("VAL_DEPRECIACION_PERIODO_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("VALOR_DEPRECIACION_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("VALOR_DEPRECIACION_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("ACTIVO_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("FECHA_REAVALUO_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("FECHA_CALCULO_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("FECHA_GARANTIA_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("VALOR_REVALUO_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("FECHA_BAJA_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("RAZON_BAJA_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("EGRESO_BODEGA_AFACT").setVisible(false);
		// tab_activos_fijos.getColumna("IDE_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("DEPRECIACION_ACUMULADA_AFACT").setVisible(false);
		tab_activos_fijos.getColumna("IDE_BOEGR").setVisible(false);
		tab_activos_fijos.getColumna("IDE_TEPRO").setVisible(false);
		tab_activos_fijos.dibujar();
		tab_activos_fijos.setCondicion("ide_bobod=-1");
		tab_activos_fijos.dibujar();
	}

	/**
	 * Funci&oacute;n que define todo el comportamiento del detalle en el
	 * ingreso en pantalla
	 */
	private void dialogoActivoLote() {
		tab_act_fij_obj.setId("tab_act_fij_obj");
		tab_act_fij_obj.setTabla("afi_activo", "ide_afact", 2);
		tab_act_fij_obj.setCampoForanea("ide_bobod");
		tab_act_fij_obj.getColumna("nro_factura_afact").setVisible(false);
		tab_act_fij_obj.setCampoOrden("ide_afact desc");

		tab_act_fij_obj.getColumna("ide_bocam").setCombo(ser_bodega.getCatalagoBodega("true"));
		tab_act_fij_obj.getColumna("ide_bocam").setAutoCompletar();
		tab_act_fij_obj.getColumna("ide_bocam").setLongitud(5);
		tab_act_fij_obj.getColumna("ide_bocam").setRequerida(true);

		tab_act_fij_obj.getColumna("ide_afubi").setCombo("afi_ubicacion", "ide_afubi", "detalle_afubi", "");
		tab_act_fij_obj.getColumna("ide_aftia").setCombo("afi_tipo_activo", "ide_aftia", "detalle_aftia", "");
		tab_act_fij_obj.getColumna("ide_aftip").setCombo("afi_tipo_propiedad", "ide_aftip", "detalle_aftip", "");
		tab_act_fij_obj.getColumna("ide_afseg").setCombo("afi_seguro", "ide_afseg", "detalle_afseg", "");
		tab_act_fij_obj.getColumna("ide_afnoa").setCombo("afi_nombre_activo", "ide_afnoa", "detalle_afnoa", "");
		// tab_act_fij_obj.getColumna("ide_afnoa").setMetodoChange("cambiarVehiculo");
		tab_act_fij_obj.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_act_fij_obj.getColumna("ide_afacd").setCombo("afi_actividad", "ide_afacd", "detalle_afacd", "");
		tab_act_fij_obj.getColumna("ide_prcla").setCombo(ser_Contabilidad.getPreClasificacion());
		tab_act_fij_obj.getColumna("ide_prcla").setAutoCompletar();
		tab_act_fij_obj.getColumna("cantidad_afact").setRequerida(true);
		// tab_act_fij_obj.getColumna("ide_prcla").setMetodoChange("cambiarCuentaContable");
		tab_act_fij_obj.getColumna("ide_cocac").setCombo(ser_Contabilidad.getCuentaContable("true,false"));
		tab_act_fij_obj.getColumna("ide_cocac").setAutoCompletar();
		tab_act_fij_obj.getColumna("ide_cocac").setRequerida(true);
		tab_act_fij_obj.getColumna("activo_afact").setValorDefecto("true");
		tab_act_fij_obj.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest", "activo_afest = true");
		tab_act_fij_obj.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_act_fij_obj.getColumna("ide_tepro").setAutoCompletar();
		// tab_act_fij_obj.getColumna("foto_bien_afact").setUpload("ACTIVOS");
		// tab_act_fij_obj.getColumna("foto_bien_afact").setValorDefecto("imagenes/activo_jpg");
		// tab_act_fij_obj.getColumna("foto_bien_afact").setImagen("128",
		// "128");
		// tab_act_fij_obj.getColumna("valor_unitario_afact").setMetodoChange("calcularAF");
		// tab_act_fij_obj.getColumna("cantidad_afact").setMetodoChange("calcularAF");
		// tab_act_fij_obj.getColumna("valor_neto_afact").setMetodoChange("calcularAF");

		tab_act_fij_obj.getColumna("valor_neto_afact").setEtiqueta();
		tab_act_fij_obj.getColumna("valor_neto_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_act_fij_obj.getColumna("valor_compra_afact").setEtiqueta();
		tab_act_fij_obj.getColumna("valor_compra_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_act_fij_obj.getColumna("secuencial_afact").setEtiqueta();
		tab_act_fij_obj.getColumna("secuencial_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_act_fij_obj.setTipoFormulario(true);
		tab_act_fij_obj.getGrid().setColumns(4);
		// tab_act_fij_obj.onSelect("seleccionarActivo");
		/* Iva Calculation */

		tab_act_fij_obj.getColumna("valor_iva").setCombo(utilitario.getIVAListOptions());
		// tab_act_fij_obj.getColumna("valor_iva").setMetodoChange("seleccionaIVA");
		tab_act_fij_obj.getColumna("valor_iva").setRequerida(true);
		/* Combo comprobate */
		tab_act_fij_obj.getColumna("tipo_comprobante").setCombo(utilitario.getComprobanteListOptions());

		tab_act_fij_obj.getColumna("foto_bien_afact").setVisible(false);
		tab_act_fij_obj.getColumna("valor_residual_afact").setVisible(false);
		tab_act_fij_obj.getColumna("VALOR_DEPRE_MES_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("VAL_DEPRECIACION_PERIODO_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("VALOR_DEPRECIACION_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("VALOR_DEPRECIACION_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("ACTIVO_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("FECHA_REAVALUO_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("FECHA_CALCULO_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("FECHA_GARANTIA_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("VALOR_REVALUO_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("FECHA_BAJA_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("RAZON_BAJA_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("EGRESO_BODEGA_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("IDE_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("DEPRECIACION_ACUMULADA_AFACT").setVisible(false);
		tab_act_fij_obj.getColumna("IDE_BOEGR").setVisible(false);
		tab_act_fij_obj.getColumna("IDE_TEPRO").setVisible(false);
		tab_act_fij_obj.dibujar();
		tab_act_fij_obj.setCondicion("ide_bobod=-1");
		tab_act_fij_obj.dibujar();
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

		empleado = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		TablaGenerica area = utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true", empleado));

		if (empleado == null || empleado.isEmpty()) {
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro de compras");
			return;
		}

		Integer numeroIngresos = 0;
		try {
			numeroIngresos = Integer.parseInt(tab_act_fij_obj.getValor("CANTIDAD_AFACT"));
		} catch (NumberFormatException ex) {

		}
		if (numeroIngresos <= 0) {
			utilitario.agregarNotificacionInfo("Faltan Campos", "La cantidad no debe ser menor a cero");
			return;
		}
		if (tab_act_fij_obj.getValor("VALOR_IVA") == null || tab_act_fij_obj.getValor("VALOR_IVA").isEmpty()) {
			utilitario.agregarNotificacionInfo("Faltan Campos", "Selecciona el porcentaje de IVA");
			return;
		}
		if (tab_act_fij_obj.getValor("ide_bocam") == null || tab_act_fij_obj.getValor("ide_bocam").isEmpty()) {
			utilitario.agregarNotificacionInfo("Faltan Campos", "Selecciona una descripcion del Catálogo de Bienes");
			return;
		}

		for (int i = 0; i < numeroIngresos; i++) {
			tab_activos_fijos.insertar();

			tab_activos_fijos.setValor("IDE_AFUBI", tab_act_fij_obj.getValor("IDE_AFUBI"));
			tab_activos_fijos.setValor("IDE_AFTIA", tab_act_fij_obj.getValor("IDE_AFTIA"));
			tab_activos_fijos.setValor("IDE_GEARE", tab_act_fij_obj.getValor("IDE_GEARE"));
			tab_activos_fijos.setValor("IDE_AFTIP", tab_act_fij_obj.getValor("IDE_AFTIP"));
			tab_activos_fijos.setValor("IDE_AFNOA", tab_act_fij_obj.getValor("IDE_AFNOA"));
			tab_activos_fijos.setValor("IDE_AFACD", tab_act_fij_obj.getValor("IDE_AFACD"));
			tab_activos_fijos.setValor("IDE_AFSEG", tab_act_fij_obj.getValor("IDE_AFSEG"));
			tab_activos_fijos.setValor("IDE_TEPRO", tab_act_fij_obj.getValor("IDE_TEPRO"));
			tab_activos_fijos.setValor("CANTIDAD_AFACT", "1");
			tab_activos_fijos.setValor("MARCA_AFACT", tab_act_fij_obj.getValor("MARCA_AFACT"));
			tab_activos_fijos.setValor("SERIE_AFACT", tab_act_fij_obj.getValor("SERIE_AFACT"));
			tab_activos_fijos.setValor("MODELO_AFACT", tab_act_fij_obj.getValor("MODELO_AFACT"));
			tab_activos_fijos.setValor("VALOR_UNITARIO_AFACT", tab_act_fij_obj.getValor("VALOR_UNITARIO_AFACT"));
			tab_activos_fijos.setValor("EGRESO_BODEGA_AFACT", tab_act_fij_obj.getValor("EGRESO_BODEGA_AFACT"));
			tab_activos_fijos.setValor("VIDA_UTIL_AFACT", tab_act_fij_obj.getValor("VIDA_UTIL_AFACT"));
			tab_activos_fijos.setValor("DETALLE_AFACT", tab_act_fij_obj.getValor("DETALLE_AFACT"));
			tab_activos_fijos.setValor("FECHA_ALTA_AFACT", tab_act_fij_obj.getValor("FECHA_ALTA_AFACT"));
			tab_activos_fijos.setValor("VALOR_NETO_AFACT", tab_act_fij_obj.getValor("VALOR_NETO_AFACT"));
			tab_activos_fijos.setValor("FECHA_REAVALUO_AFACT", tab_act_fij_obj.getValor("FECHA_REAVALUO_AFACT"));
			tab_activos_fijos.setValor("VALOR_COMPRA_AFACT", tab_act_fij_obj.getValor("VALOR_COMPRA_AFACT"));
			tab_activos_fijos.setValor("FECHA_CALCULO_AFACT", tab_act_fij_obj.getValor("FECHA_CALCULO_AFACT"));
			tab_activos_fijos.setValor("VALOR_REVALUO_AFACT", tab_act_fij_obj.getValor("VALOR_REVALUO_AFACT"));
			tab_activos_fijos.setValor("FECHA_GARANTIA_AFACT", tab_act_fij_obj.getValor("FECHA_GARANTIA_AFACT"));
			tab_activos_fijos.setValor("IDE_COCAC", tab_act_fij_obj.getValor("IDE_COCAC"));
			tab_activos_fijos.setValor("IDE_AFEST", tab_act_fij_obj.getValor("IDE_AFEST"));
			tab_activos_fijos.setValor("TIPO_COMPROBANTE", tab_act_fij_obj.getValor("TIPO_COMPROBANTE"));
			tab_activos_fijos.setValor("VALOR_IVA", tab_act_fij_obj.getValor("VALOR_IVA"));
			tab_activos_fijos.setValor("IDE_PRCLA", tab_act_fij_obj.getValor("IDE_PRCLA"));
			tab_activos_fijos.setValor("OBSERVACIONES_AFACT", tab_act_fij_obj.getValor("OBSERVACIONES_AFACT"));
			tab_activos_fijos.setValor("COLOR_AFACT", tab_act_fij_obj.getValor("COLOR_AFACT"));
			tab_activos_fijos.setValor("IDE_BOEGR", tab_act_fij_obj.getValor("IDE_BOEGR"));
			tab_activos_fijos.setValor("DEPRECIACION_ACUMULADA_AFACT", tab_act_fij_obj.getValor("DEPRECIACION_ACUMULADA_AFACT"));
			tab_activos_fijos.setValor("IDE_ADSOC", tab_act_fij_obj.getValor("IDE_ADSOC"));
			tab_activos_fijos.setValor("COD_ANTERIOR_AFACT", tab_act_fij_obj.getValor("COD_ANTERIOR_AFACT"));
			tab_activos_fijos.setValor("CHASIS_AFACT", tab_act_fij_obj.getValor("CHASIS_AFACT"));
			tab_activos_fijos.setValor("MOTOR_AFACT", tab_act_fij_obj.getValor("MOTOR_AFACT"));
			tab_activos_fijos.setValor("ide_boubi", tab_act_fij_obj.getValor("ide_boubi"));
			System.out.println("IDE_BOCAM "+tab_act_fij_obj.getValor("IDE_BOCAM"));
			System.out.println("IDE_BOCAM. "+tab_activos_fijos.getValor("IDE_BOCAM"));
			System.out.println("ide_boubi "+tab_act_fij_obj.getValor("ide_boubi"));
			tab_activos_fijos.setValor("IDE_BOCAM", tab_act_fij_obj.getValor("IDE_BOCAM"));
			if (tab_activos_fijos.getValor("valor_iva") != null) {
				this.duo_iva = Double.parseDouble(tab_activos_fijos.getValor("valor_iva"));
				this.calcularAF();
			}
		}
		this.cuadrarDetalle();
		dia_activo_lote.cerrar();
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
		tab_act_fij_obj.insertar();
		tab_act_fij_obj.setValor("IDE_AFUBI", "2");
		tab_act_fij_obj.setValor("IDE_AFTIA", "1");
		tab_act_fij_obj.setValor("IDE_GEARE", "2");
		tab_act_fij_obj.setValor("IDE_AFTIP", "1");
		tab_act_fij_obj.setValor("IDE_AFACD", "1");
		tab_act_fij_obj.setValor("IDE_AFSEG", "1");
		tab_act_fij_obj.setValor("IDE_AFEST", "8");
		tab_act_fij_obj.setValor("TIPO_COMPROBANTE", "FAC");
		tab_act_fij_obj.setValor("CANTIDAD_AFACT", "1");
		tab_act_fij_obj.setValor("IDE_COCAC", "1");
		tab_act_fij_obj.setValor("IDE_TEPRO", tab_bodega.getValor("IDE_TEPRO"));
		tab_act_fij_obj.setValor("FECHA_ALTA_AFACT", tab_bodega.getValor("FECHA_INGRESO_BOBOD"));

		dia_activo_lote.dibujar();
	}

	public pre_compras_ingreso() {
		par_secuencial_modulo = utilitario.getVariable("p_modulo_sec_bod_ingresos");

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
		dialogoActivoLote();

		/************************************************************/
		Boton bot_lotes = new Boton();
		bot_lotes.setValue("Ingreso Por Lotes");
		bot_lotes.setTitle("Permite ingresar mas de un activo cuando este se repite varias veces");
		bot_lotes.setIcon("ui-icon-person");
		bot_lotes.setMetodo("mostrarDialogoLotes");
		bar_botones.agregarBoton(bot_lotes);

		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Material");
		bot_material.setTitle("MATERIAL");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarMaterial");
		// bar_botones.agregarBoton(bot_material);
		par_grupo_material = utilitario.getVariable("p_grupo_material");

		set_material.setId("set_material");
		set_material.setSeleccionTabla(ser_Bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
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
		set_actualizamaterial.setSeleccionTabla(ser_Bodega.getInventario("0", "true", par_grupo_material), "ide_bomat");
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
		set_proveedor.setSeleccionTabla(ser_Bodega.getProveedor("true"), "");
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

		// dia_bodega.setId("dia_bodega");
		// dia_bodega.setTitle("SELECCIONE AÑADIR AL INVENTARIO / NO AÑADIR AL INVENTARIO ");
		// dia_bodega.getBot_aceptar().setMetodo("aceptaInventario");
		// dia_bodega.setDialogo(set_actualizaproveedor);
		// dia_bodega.setHeight("40%");
		// dia_bodega.setWidth("40%");
		// dia_bodega.setDynamic(false);
		// agregarComponente(dia_bodega);

		PanelTabla dia_activo_lotept = new PanelTabla();
		dia_activo_lotept.setPanelTabla(tab_act_fij_obj);

		dia_activo_lote.setId("dia_activo_lote");
		dia_activo_lote.setTitle("ACTIVO LOTE");
		dia_activo_lote.getBot_aceptar().setMetodo("IngresoLotes");
		dia_activo_lote.setDialogo(dia_activo_lotept);
		//dia_activo_lote.setHeight("60%");
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
		div_division.dividir2(pat_bodega, div_aux, "35%", "h");

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
		// System.out.println("Entra a actualizar1...");
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
		set_actualizaproveedor.getTab_seleccion().setSql(ser_Bodega.getProveedor("true"));
		set_actualizaproveedor.getTab_seleccion().ejecutarSql();
		set_actualizaproveedor.dibujar();
		// dia_bodega.dibujar();
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
			// tab_bodega.insertar();
			tab_bodega.setValor("ide_tepro", str_seleccionados);
			tab_bodega.setValor("ide_geani", com_anio.getValue() + "");
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_bodega");
		utilitario.addUpdate("tab_activos_fijos");
	}

	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_bodega.setCondicion("ide_geani=" + com_anio.getValue()+" and es_existencia=false");
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
				p_parametros.put("titulo", "INGRESO DE BIENES No. " + tab_bodega.getValor("numero_ingreso_bobod"));
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
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		if (tab_activos_fijos.isFocus()) {
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
			tab_activos_fijos.insertar();
			tab_activos_fijos.setValor("IDE_AFUBI", "2");
			tab_activos_fijos.setValor("IDE_AFTIA", "1");
			tab_activos_fijos.setValor("IDE_GEARE", "2");
			tab_activos_fijos.setValor("IDE_AFTIP", "1");
			tab_activos_fijos.setValor("IDE_AFACD", "1");
			tab_activos_fijos.setValor("IDE_AFSEG", "1");
			tab_activos_fijos.setValor("IDE_AFEST", "8");
			tab_activos_fijos.setValor("TIPO_COMPROBANTE", "FAC");
			tab_activos_fijos.setValor("CANTIDAD_AFACT", "1");
			tab_activos_fijos.setValor("IDE_COCAC", "1");
			tab_activos_fijos.setValor("IDE_TEPRO", tab_bodega.getValor("IDE_TEPRO"));

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
		this.cuadrarDetalle();
	}

	public void cuadrarDetalle() {
		int numFil = tab_activos_fijos.getTotalFilas();
		double valorSinIva = 0;
		double valorConIva = 0;
		double acSinIva = 0;
		double acConIva = 0;

		for (int i = 0; i < numFil; i++) {
			tab_activos_fijos.setFilaActual(i);
			try {
				acSinIva = Double.parseDouble(tab_activos_fijos.getValor("VALOR_UNITARIO_AFACT"));
				acConIva = Double.parseDouble(tab_activos_fijos.getValor("VALOR_IVA"));
				// System.out.println("Fila " + i + " Valor " + acSinIva +
				// " Iva " + acConIva);
			} catch (Exception e) {
				// System.out.println("Problema con convercion fila " + i);
				acSinIva = 0;
				acConIva = 0;
			}
			valorSinIva += acSinIva;
			valorConIva += acSinIva + (acSinIva * acConIva);

		}
		tab_bodega.setValor("valor_unitario_bobod", utilitario.getFormatoNumero(valorSinIva, 3));
		tab_bodega.setValor("VALOR_TOTAL_BOBOD", utilitario.getFormatoNumero(valorConIva, 3));
		tab_bodega.setValor("CANTIDAD_INGRESO_BOBOD", String.valueOf(tab_activos_fijos.getTotalFilas()));
		System.out.println("CANTIDAD_INGRESO_BOBOD " + tab_activos_fijos.getTotalFilas());
		System.out.println("valor_unitario_bobod " + String.valueOf(valorSinIva));
		System.out.println("VALOR_TOTAL_BOBOD " + String.valueOf(valorConIva));

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
			//tab_bodega.setValor("numero_ingreso_bobod", ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
			tab_bodega.guardar();
			tab_activos_fijos.guardar();
			guardarPantalla();
			ser_contabilidad.guardaSecuencial(tab_bodega.getValor("numero_ingreso_bobod"), par_secuencial_modulo);
			// System.out.println("Se guardó un ingreso por lote");
		} else {
			//tab_bodega.guardar();
			//tab_activos_fijos.guardar();
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

	public Tabla getTab_activos_fijos() {
		return tab_activos_fijos;
	}

	public void setTab_activos_fijos(Tabla tab_activos_fijos) {
		this.tab_activos_fijos = tab_activos_fijos;
	}

	/**
	 * @return the tab_act_fij_obj
	 */
	public Tabla getTab_act_fij_obj() {
		return tab_act_fij_obj;
	}

	/**
	 * @param tab_act_fij_obj
	 *            the tab_act_fij_obj to set
	 */
	public void setTab_act_fij_obj(Tabla tab_act_fij_obj) {
		this.tab_act_fij_obj = tab_act_fij_obj;
	}

}
