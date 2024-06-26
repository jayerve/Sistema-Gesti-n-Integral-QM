package paq_adquisicion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_factura_compras_old extends Pantalla {
	private Tabla tab_adq_factura = new Tabla();
	private Tabla tab_adq_detalle = new Tabla();
	private AutoCompletar aut_adq_compra = new AutoCompletar();
	private SeleccionTabla set_solicitud = new SeleccionTabla();
	public static double par_iva;
	private Dialogo dia_recepcion = new Dialogo();
	private Dialogo dia_aplica_descuento = new Dialogo();
	private SeleccionTabla set_proveedor = new SeleccionTabla();

	private Radio lis_recepcion = new Radio();
	private Radio lis_aplica_descuento = new Radio();
	private Check che_iva12 = new Check();
	private Combo com_anio=new Combo();

	@EJB
	private ServicioAdquisicion ser_Adquisicion = (ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_factura_compras_old() {
		par_iva = pckUtilidades.CConversion.CDbl(utilitario.getVariable("p_valor_iva"));
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		 
		tab_adq_factura.setId("tab_adq_factura");
		tab_adq_factura.setTabla("adq_factura", "ide_adfac", 1);
		tab_adq_factura.agregarRelacion(tab_adq_detalle);
		tab_adq_factura.setTipoFormulario(true);
		tab_adq_factura.getGrid().setColumns(4);
		tab_adq_factura.setCampoOrden("ide_adfac desc");
		tab_adq_factura.getColumna("ide_adsoc").setCombo(ser_Adquisicion.getComprasCombo("true,false"));
		tab_adq_factura.getColumna("ide_adsoc").setAutoCompletar();
		tab_adq_factura.getColumna("ide_adsoc").setLectura(true);
		tab_adq_factura.getColumna("subtotal_adfac").setEtiqueta();
		tab_adq_factura.getColumna("subtotal_adfac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("base_iva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("base_iva_adfac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("valor_iva_adfac").setMetodoChange("calcularSolicitud");
		tab_adq_factura.getColumna("valor_iva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("porcent_desc_adfac").setMetodoChange("calcularDescuentoPorce");
		tab_adq_factura.getColumna("valor_iva_adfac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("porcent_desc_adfac").setVisible(false);
		tab_adq_factura.getColumna("valor_descuento_adfac").setVisible(false);
		tab_adq_factura.getColumna("aplica_descuento_adfac").setVisible(false);
		tab_adq_factura.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_adq_factura.getColumna("ide_tepro").setLectura(true);
		tab_adq_factura.getColumna("ide_tepro").setAutoCompletar();
		tab_adq_factura.getColumna("base_noiva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("base_noiva_adfac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo

		// AVACA - 2016-03-21
		tab_adq_factura.getColumna("IDE_PRTRA").setCombo(ser_Presupuesto.getTramite("true"));
		tab_adq_factura.getColumna("IDE_PRTRA").setAutoCompletar();
		tab_adq_factura.getColumna("IDE_PRTRA").setLectura(true);

		tab_adq_factura.getColumna("nro_autorizacion_sri_adq");
		// AVACA - 2016-03-21

		tab_adq_factura.getColumna("valor_descuento_adfac").setMetodoChange("calcularDescuento");
		tab_adq_factura.getColumna("total_adfac").setEtiqueta();
		tab_adq_factura.getColumna("total_adfac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("activo_adfac").setLectura(true);
		tab_adq_factura.getColumna("activo_adfac").setValorDefecto("true");
		List listay = new ArrayList();
		Object filay1[] = { "1", "DESCUENTO FACTURA" };
		Object filay2[] = { "0", "DESCUENTO PRODUCTO" };

		listay.add(filay1);
		listay.add(filay2);
		tab_adq_factura.getColumna("aplica_descuento_adfac").setRadio(listay, "1");
		tab_adq_factura.getColumna("aplica_descuento_adfac").setRadioVertical(true);
		tab_adq_factura.getColumna("aplica_descuento_adfac").setLectura(true);
		tab_adq_factura.getColumna("ide_srsuc").setCombo("sri_sustento_comprobante", "ide_srsuc", "codigo_srsuc || detalle_srsuc", "");
		tab_adq_factura.getColumna("ide_srsuc").setAutoCompletar();
		tab_adq_factura.setCondicion("ide_adfac=-1");
		tab_adq_factura.dibujar();
		PanelTabla pat_adq_factura = new PanelTabla();
		pat_adq_factura.setPanelTabla(tab_adq_factura);

		tab_adq_detalle.setId("tab_adq_detalle");
		tab_adq_detalle.setTabla("adq_detalle_factura", "ide_addef", 2);
		tab_adq_detalle.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("1", "true,false", ""));
		tab_adq_detalle.getColumna("ide_bomat").setAutoCompletar();
		tab_adq_detalle.getColumna("aplica_iva_addef").setVisible(false);
		tab_adq_detalle.getColumna("cantidad_addef").setMetodoChange("calcularDetallle");
		tab_adq_detalle.getColumna("valor_total_addef").setMetodoChange("calcularDetallle");
		tab_adq_detalle.getColumna("valor_unitario_addef").setEtiqueta();
		tab_adq_detalle.getColumna("valor_unitario_addef").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_detalle.getColumna("activo_addef").setLectura(true);
		tab_adq_detalle.getColumna("activo_addef").setValorDefecto("true");
		tab_adq_detalle.getColumna("recibido_addef").setValorDefecto("true");
		tab_adq_detalle.getColumna("marca_addef").setValorDefecto("S/M");
		tab_adq_detalle.getColumna("serie_addef").setValorDefecto("S/S");
		tab_adq_detalle.getColumna("color_addef").setValorDefecto("S/C");
		tab_adq_detalle.getColumna("modelo_addef").setValorDefecto("S/M");
		tab_adq_detalle.getColumna("marca_addef").setRequerida(true);
		tab_adq_detalle.getColumna("serie_addef").setRequerida(true);
		tab_adq_detalle.getColumna("color_addef").setRequerida(true);
		tab_adq_detalle.getColumna("modelo_addef").setRequerida(true);

		tab_adq_detalle.getColumna("recibido_addef").setLectura(true);
		tab_adq_detalle.getColumna("por_descuento_addef").setVisible(false);
		tab_adq_detalle.getColumna("valor_descuento_addef").setVisible(false);
		tab_adq_detalle.getColumna("recibido_addef").setVisible(false);

		// Aplica IVA 2016-03-29
		tab_adq_detalle.getColumna("aplica_iva_adfac").setValorDefecto("true");
		// Aplica IVA 2016-03-29

		tab_adq_detalle.dibujar();
		PanelTabla pat_adq_detalle = new PanelTabla();
		pat_adq_detalle.setPanelTabla(tab_adq_detalle);

		che_iva12.setId("che_iva12");
		che_iva12.setMetodoChange("cambiaIVA");
		Etiqueta eti_iva12 = new Etiqueta("IVA 14%");
		bar_botones.agregarComponente(eti_iva12);
		bar_botones.agregarComponente(che_iva12);

		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Solicitud de Compras");
		bot_buscar.setMetodo("importarSolicitudCompra");
		bar_botones.agregarBoton(bot_buscar);

		set_solicitud.setId("set_solicitud");
		set_solicitud.setSeleccionTabla(ser_Adquisicion.getCompras("true,false"), "ide_adsoc");
		set_solicitud.setTitle("SELECCIONE UNA SOLICITUD DE COMPRA");
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setNombreVisual("Proveedor");
		set_solicitud.getTab_seleccion().getColumna("detalle_adsoc").setNombreVisual("Detalle Solicitud Compra");
		set_solicitud.getTab_seleccion().getColumna("nro_solicitud_adsoc").setNombreVisual("Nro. Solicitud");
		set_solicitud.getTab_seleccion().getColumna("ide_tepro").setVisible(false);
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("detalle_adsoc").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("nro_solicitud_adsoc").setFiltro(true);

		set_solicitud.getBot_aceptar().setMetodo("aceptarSolicitudCompra");
		set_solicitud.setRadio();
		agregarComponente(set_solicitud);

		Division div_division = new Division();
		div_division.dividir2(pat_adq_factura, pat_adq_detalle, "50%", "H");
		agregarComponente(div_division);

		// Inicializo componente dialogo para seleccionar EL TIPO DE RECEPCION

		List lista = new ArrayList();
		Object fila1[] = { "0", "TOTAL" };
		Object fila2[] = { "1", "PARCIAL" };
		lista.add(fila1);
		lista.add(fila2);

		lis_recepcion.setRadio(lista);
		lis_recepcion.setVertical();
		dia_recepcion.setId("dia_recepcion");
		dia_recepcion.setTitle("SELECCIONE EL TIPO DE RECEPCION");
		dia_recepcion.getBot_aceptar().setMetodo("aceptaRecepcion");
		dia_recepcion.getBot_cancelar().setMetodo("cancelarFactura");
		dia_recepcion.setDialogo(lis_recepcion);
		dia_recepcion.setHeight("40%");
		dia_recepcion.setWidth("40%");
		dia_recepcion.setDynamic(false);
		agregarComponente(dia_recepcion);

		// Inicializo componente dialogo para seleccionar EL TIPO DE DESCUENTO
		// APLICAR

		List listax = new ArrayList();
		Object filax1[] = { "0", "DESCUENTO PRODUCTO" };
		Object filax2[] = { "1", "DESCUENTO FACTURA" };
		listax.add(filax1);
		listax.add(filax2);

		lis_aplica_descuento.setRadio(listax);
		lis_aplica_descuento.setVertical();
		dia_aplica_descuento.setId("dia_aplica_descuento");
		dia_aplica_descuento.setTitle("SELECCIONE EL TIPO DE DESCUENTO APLICAR");
		dia_aplica_descuento.getBot_aceptar().setMetodo("aceptaDescuento");
		dia_aplica_descuento.getBot_cancelar().setMetodo("cancelarFactura");
		dia_aplica_descuento.setDialogo(lis_aplica_descuento);
		dia_aplica_descuento.setHeight("40%");
		dia_aplica_descuento.setWidth("40%");
		dia_aplica_descuento.setDynamic(false);
		agregarComponente(dia_aplica_descuento);

		Boton bot_proveedor = new Boton();
		bot_proveedor.setValue("Proveedor");
		bot_proveedor.setTitle("PROVEEDOR");
		bot_proveedor.setIcon("ui-icon-person");
		bot_proveedor.setMetodo("importarProveedor");
		bar_botones.agregarBoton(bot_proveedor);

		set_proveedor.setId("set_proveedor");
		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("null"), "ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setNombreVisual("Nombre Proveedor");
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setNombreVisual("Ruc Proveedor");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setFiltro(true);
		set_proveedor.setTitle("Seleccione Proveedor");
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.setRadio();
		agregarComponente(set_proveedor);

	}

	public void seleccionaElAnio (){
    	tab_adq_factura.setCondicion(" extract(year from fecha_factura_adfac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
    	tab_adq_factura.ejecutarSql();
    	tab_adq_detalle.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
	}

	public void importarProveedor() {
		set_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
		set_proveedor.getTab_seleccion().ejecutarSql();
		set_proveedor.dibujar();

	}

	public void aceptarProveedor() {
		String str_seleccionado = set_proveedor.getValorSeleccionado();
		TablaGenerica tab_proveedor = ser_bodega.getTablaProveedor(str_seleccionado);
		if (str_seleccionado != null) {
			tab_adq_factura.setValor("ide_tepro", str_seleccionado);
			tab_adq_factura.modificar(tab_adq_factura.getFilaActual());
			if (formatearNumeroFactura()) {
				tab_adq_factura.guardar();
				guardarPantalla();
			}
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_adq_factura");
	}

	public void cambiaIVA() {
		if (che_iva12.getValue().toString().equalsIgnoreCase("true")) {
			par_iva = 0.14;
		} else {
			par_iva = pckUtilidades.CConversion.CDbl(utilitario.getVariable("p_valor_iva")); // 0.14
																								// de
																								// IVA
		}

		System.out.println("cambiaIVA FacCompras " + par_iva);
	}

	public void cancelarFactura() {

		utilitario.agregarMensajeInfo("No se Puede Cancelar", "Inicio un proceso de seleccion de compra que no lo puede cancelar en esta instancia, debe seleccionar una opciòn y continuar");
		return;
	}

	public void importarSolicitudCompra() {

		set_solicitud.getTab_seleccion().setSql(ser_Adquisicion.getCompras("true"));
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.dibujar();

	}

	public void aceptaRecepcion() {
		System.out.println("entre acpetar");
		if (lis_recepcion.getValue() == null) {
			utilitario.agregarMensajeInfo("Recepciòn", "Debe Seleccionar una Opción");
			return;
		} else {
			utilitario.getConexion().ejecutarSql("update adq_solicitud_compra  set tipo_recepcion_adsoc = " + lis_recepcion.getValue() + " where ide_adsoc =" + tab_adq_factura.getValor("ide_adsoc"));
			tab_adq_factura.ejecutarSql();
			dia_recepcion.cerrar();
		}
	}

	public void aceptaDescuento() {
		System.out.println("entre acpetar");
		if (lis_aplica_descuento.getValue() == null) {
			utilitario.agregarMensajeInfo("Descuento", "Debe Seleccionar una Opción");
			return;
		} else {
			tab_adq_factura.setValor("aplica_descuento_adfac", lis_aplica_descuento.getValue().toString());
			utilitario.addUpdate("tab_adq_factura");
			tab_adq_factura.guardar();
			guardarPantalla();
			dia_aplica_descuento.cerrar();
		}
	}

	public void aceptarSolicitudCompra() {

		String str_seleccionado = set_solicitud.getValorSeleccionado();
		TablaGenerica tab_solicitud = utilitario.consultar(ser_Adquisicion.getComprasCodigo(str_seleccionado));
		if (str_seleccionado != null) {
			tab_adq_factura.insertar();
			tab_adq_factura.setValor("ide_adsoc", str_seleccionado);
			tab_adq_factura.setValor("detalle_adfac", tab_solicitud.getValor("detalle_adsoc"));
			tab_adq_factura.setValor("ide_tepro", tab_solicitud.getValor("ide_tepro"));
		}
		utilitario.addUpdateTabla(tab_adq_factura, "ide_adsoc,detalle_adfac", "");
		set_solicitud.cerrar();
		tab_adq_factura.guardar();
		guardarPantalla();
		dia_recepcion.dibujar();
	}

	// /CALCULAR
	public void calcular() {
		// Variables para almacenar y calcular el total del detalle
		double dou_cantidad_addef = 0;
		double dou_valor_unitario_addef = 0;
		double dou_valor_total_addef = 0;
		try {
			// Obtenemos el valor de la cantidad
			dou_cantidad_addef = pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor("cantidad_addef"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_valor_total_addef = pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor("valor_total_addef"));
		} catch (Exception e) {
		}
		// Calculamos el total
		dou_valor_unitario_addef = dou_valor_total_addef / dou_cantidad_addef;
		// Asignamos el total a la tabla detalle, con 3 decimales
		tab_adq_detalle.setValor("valor_unitario_addef", utilitario.getFormatoNumero(dou_valor_unitario_addef, 3));
		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_adq_detalle, "valor_unitario_addef", "tab_adq_factura");
		calcularSolicitud();
	}

	public void calcularDetallle(AjaxBehaviorEvent evt) {
		tab_adq_detalle.modificar(evt); // Siempre es la primera linea
		calcular();
	}

	public void calcularSolicitud() {

		double dou_subtotal_adfac = 0;
		double duo_valor_iva = par_iva;
		double dou_valor_iva_adfac = 0;
		double dou_total_adfac = 0;
		double dou_valor_total_addef = 0;

		// Aplica IVA 2016-03-29
		double subtotal_iva = 0;
		double subtotal_sin_iva = 0;

		for (int i = 0; i < tab_adq_detalle.getTotalFilas(); i++) {
			// Subtotal con iva
			if (tab_adq_detalle.getValor(i, "aplica_iva_adfac").trim().contentEquals("true")) {
				subtotal_iva += pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor(i, "valor_total_addef"));
				// System.out.println("> con iva: "+pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor(i,"valor_total_addef"))
				// );
			}

			if (tab_adq_detalle.getValor(i, "aplica_iva_adfac").trim().contentEquals("false")) {
				// Subtotal sin iva
				subtotal_sin_iva += pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor(i, "valor_total_addef"));
				// System.out.println("> sin iva: "+pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor(i,"valor_total_addef"))
				// );
			}
		}

		double iva_adq_factura = subtotal_iva * duo_valor_iva;
		double subtotal_adq_factura = subtotal_iva + subtotal_sin_iva;
		double total_adq_factura = subtotal_adq_factura + iva_adq_factura;

		// Asigna el valor de la base imponible Sin Iva
		tab_adq_factura.setValor("base_noiva_adfac", String.valueOf(subtotal_sin_iva));

		// Asigna el valor de la base imponible Con Iva
		tab_adq_factura.setValor("base_iva_adfac", String.valueOf(subtotal_iva));

		System.out.println("subtotal_sin_iva: " + subtotal_sin_iva);
		System.out.println("subtotal_iva: " + subtotal_iva);
		System.out.println("iva_adq_factura: " + iva_adq_factura);
		System.out.println("subtotal_adq_factura: " + subtotal_adq_factura);
		System.out.println("total_adq_factura: " + total_adq_factura);
		System.out.println("");
		// Aplica IVA 2016-03-29

		tab_adq_factura.setValor("subtotal_adfac", String.valueOf(subtotal_adq_factura));

		tab_adq_factura.setValor("valor_iva_adfac", utilitario.getFormatoNumero(iva_adq_factura, 3));
		tab_adq_factura.setValor("total_adfac", utilitario.getFormatoNumero(total_adq_factura, 3));
		tab_adq_factura.modificar(tab_adq_factura.getFilaActual());// para que
																	// haga el
																	// update

		utilitario.addUpdateTabla(tab_adq_factura, "valor_iva_adfac,total_adfac,subtotal_adfac,base_iva_adfac", "tab_adq_detalle");
	}

	public void calcularDescuento() {
		double duo_subtotal = 0;
		double dou_subtotal_adfac = 0;
		double duo_valor_descuento = 0;
		double duo_total_iva = 0;
		double duo_total = 0;
		double porcentaje_descuento = 0;
		double duo_iva = par_iva;
		calcularSolicitud();

		// tab_adq_factura.getValor("valor_descuento_adfac");
		duo_subtotal = pckUtilidades.CConversion.CDbl(tab_adq_factura.getValor("valor_descuento_adfac"));

		// tab_adq_factura.getValor("total_adfac");
		dou_subtotal_adfac = pckUtilidades.CConversion.CDbl(tab_adq_factura.getValor("subtotal_adfac"));
		if (duo_subtotal > dou_subtotal_adfac) {
			utilitario.agregarMensajeInfo("Excedido Valor ", "El valor del descuento no puedo superar el valor del subtotal");
			tab_adq_factura.setValor("valor_descuento_adfac", "0");
			utilitario.addUpdateTabla(tab_adq_factura, "valor_descuento_adfac", "");

			return;

		}
		porcentaje_descuento = (duo_subtotal * 100) / dou_subtotal_adfac;
		duo_valor_descuento = dou_subtotal_adfac - duo_subtotal;
		duo_total_iva = duo_valor_descuento * duo_iva;
		duo_total = duo_valor_descuento + duo_total_iva;
		tab_adq_factura.setValor("porcent_desc_adfac", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_factura.setValor("subtotal_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_factura.setValor("base_iva_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));

		tab_adq_factura.setValor("total_adfac", utilitario.getFormatoNumero(duo_total, 2));
		tab_adq_factura.setValor("VALOR_IVA_ADFAC", utilitario.getFormatoNumero(duo_total_iva, 2));

		utilitario.addUpdateTabla(tab_adq_factura, "total_adfac,subtotal_adfac,VALOR_IVA_ADFAC,porcent_desc_adfac", "tab_adq_detalle");

	}

	public void calcularDescuentoPorce() {
		double duo_subtotal = 0;
		double dou_subtotal_adfac = 0;
		double duo_valor_descuento = 0;
		double duo_total_iva = 0;
		double duo_total = 0;
		double porcentaje_descuento = 0;
		double duo_iva = par_iva;
		calcularSolicitud();

		// tab_adq_factura.getValor("valor_descuento_adfac");
		duo_subtotal = pckUtilidades.CConversion.CDbl(tab_adq_factura.getValor("porcent_desc_adfac"));
		// tab_adq_factura.getValor("total_adfac");
		if (duo_subtotal > 100) {
			utilitario.agregarMensajeInfo("Excedido Valor ", "El porcentaje de descuento no puede superar el 100%");
			tab_adq_factura.setValor("porcent_desc_adfac", "0");
			utilitario.addUpdateTabla(tab_adq_factura, "porcent_desc_adfac", "");

			return;
		}
		dou_subtotal_adfac = pckUtilidades.CConversion.CDbl(tab_adq_factura.getValor("subtotal_adfac"));
		porcentaje_descuento = (dou_subtotal_adfac * duo_subtotal) / 100;
		duo_valor_descuento = dou_subtotal_adfac - porcentaje_descuento;
		duo_total_iva = duo_valor_descuento * duo_iva;
		duo_total = duo_valor_descuento + duo_total_iva;
		tab_adq_factura.setValor("valor_descuento_adfac", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_factura.setValor("subtotal_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_factura.setValor("base_iva_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_factura.setValor("total_adfac", utilitario.getFormatoNumero(duo_total, 2));
		tab_adq_factura.setValor("VALOR_DESCUENTO_ADFAC", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_factura.setValor("VALOR_IVA_ADFAC", utilitario.getFormatoNumero(duo_total_iva, 2));

		utilitario.addUpdateTabla(tab_adq_factura, "total_adfac,subtotal_adfac,VALOR_IVA_ADFAC,porcent_desc_adfac,VALOR_DESCUENTO_ADFAC", "");

	}

	// AVACA - 2016-03-21
	private boolean formatearNumeroFactura() {
		boolean correcto = false;
		try {
			String[] numeroFactura = tab_adq_factura.getValor("num_factura_adfac").split("-");
			String nuevoNumeroFactura = "";

			String strSucursal = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[0])), 3);
			String strPuntoEmision = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[1])), 3);
			String strSecuencial = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[2])), 9);

			nuevoNumeroFactura += strSucursal + "-";
			nuevoNumeroFactura += strPuntoEmision + "-";
			nuevoNumeroFactura += strSecuencial;

			tab_adq_factura.setValor("num_factura_adfac", nuevoNumeroFactura);

			if (tab_adq_factura.isFilaInsertada()) {
				if (!ser_Adquisicion.compruebaFacCompra(strSucursal, strPuntoEmision, strSecuencial, com_anio.getValue()+"",tab_adq_factura.getValor("ide_tepro"))) 
					correcto = true;
				else
					utilitario.agregarMensajeInfo("No se pudo guardar", "Número de factura repetido.");
			} else
			{
				correcto = true;
				//validar k no haya retencion
				TablaGenerica tab_retenciones = utilitario.consultar("SELECT ide_teret, num_retencion_teret FROM tes_retencion where ide_coest=2 and ide_adfac="+tab_adq_factura.getValor("ide_adfac"));
				if(tab_retenciones.getTotalFilas()>0)
				{
					correcto = false;
					utilitario.agregarMensajeInfo("No se pudo guardar", "La factura cuenta con una retención...");
				}
			}

		} catch (Exception ex) {
			System.out.println("Error al padear el secuencial de la factura de compra: " + tab_adq_factura.getValor("num_factura_adfac") + ". " + ex.getMessage());
			tab_adq_factura.setValor("num_factura_adfac", null);
			utilitario.agregarMensajeInfo("No se pudo guardar", "Valide el formato del número de la factura.");
		}
		return correcto;
	}

	private void refrescarTabla() {
		utilitario.addUpdateTabla(tab_adq_factura, "valor_iva_adfac, total_adfac, detalle_adfac, num_factura_adfac", "");
	}

	// AVACA - 2016-03-21

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();

	}

	@Override
	public void guardar() {
		// AVACA - 2016-03-21
		// Formato del número de la factura (001-001-000123456)
		if (formatearNumeroFactura()) {
			if (tab_adq_factura.guardar()) {
				if (tab_adq_detalle.guardar()) {
					guardarPantalla();
					tab_adq_factura.ejecutarSql();
					tab_adq_detalle.ejecutarSql();
				}
			}
		}

		// AVACA - 2016-03-21
	}

	@Override
	public void eliminar() {
		if (tab_adq_factura.isFocus()) {
			if (tab_adq_detalle.getTotalFilas() > 0) {
				utilitario.agregarMensajeError("No se puede borrar", "El presente registro no se puede borrar existen detalles de factura");
				return;
			}
			tab_adq_factura.eliminar();
		}
		if (tab_adq_detalle.isFocus()) {
			tab_adq_detalle.eliminar();
		}
	}

	public Tabla getTab_adq_factura() {
		return tab_adq_factura;
	}

	public void setTab_adq_factura(Tabla tab_adq_factura) {
		this.tab_adq_factura = tab_adq_factura;
	}

	public Tabla getTab_adq_detalle() {
		return tab_adq_detalle;
	}

	public void setTab_adq_detalle(Tabla tab_adq_detalle) {
		this.tab_adq_detalle = tab_adq_detalle;
	}

	public SeleccionTabla getSet_solicitud() {
		return set_solicitud;
	}

	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_solicitud = set_solicitud;
	}

	public Dialogo getDia_recepcion() {
		return dia_recepcion;
	}

	public void setDia_recepcion(Dialogo dia_recepcion) {
		this.dia_recepcion = dia_recepcion;
	}

	public Dialogo getDia_aplica_descuento() {
		return dia_aplica_descuento;
	}

	public void setDia_aplica_descuento(Dialogo dia_aplica_descuento) {
		this.dia_aplica_descuento = dia_aplica_descuento;
	}

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	
}
