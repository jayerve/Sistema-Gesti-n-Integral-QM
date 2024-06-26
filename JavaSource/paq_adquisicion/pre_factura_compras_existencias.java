package paq_adquisicion;

import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
import framework.componentes.Upload;

public class pre_factura_compras_existencias extends Pantalla {
	private Tabla tab_adq_factura = new Tabla();
	private Tabla tab_adq_detalle = new Tabla();
	private AutoCompletar aut_adq_compra = new AutoCompletar();
	public static double par_iva;
	private SeleccionTabla set_proveedor = new SeleccionTabla();

	private Radio lis_recepcion = new Radio();
	private Radio lis_aplica_descuento = new Radio();
	private Check che_iva12 = new Check();
	private Combo com_anio = new Combo();
	private Dialogo dialogo = new Dialogo();
	private Upload upl_archivo = new Upload();

	@EJB
	private ServicioAdquisicion ser_Adquisicion = (ServicioAdquisicion) utilitario
			.instanciarEJB(ServicioAdquisicion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto) utilitario
			.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);

	public pre_factura_compras_existencias() {
		par_iva = pckUtilidades.CConversion.CDbl(utilitario.getVariable("p_valor_iva"));

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);

		dialogo.setId("dialogo");
		dialogo.setDialogo(upl_archivo);
		dialogo.getBot_aceptar().setMetodo("aceptarImportacion");

		agregarComponente(dialogo);

		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");

		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xml|XML)$/");
		upl_archivo.setUploadLabel("Cargar archivo");
		upl_archivo.setCancelLabel("Cancelar Seleccion");

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
		tab_adq_factura.getColumna("subtotal_adfac")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("base_iva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("base_iva_adfac")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("valor_iva_adfac").setMetodoChange("calcularSolicitud");
		tab_adq_factura.getColumna("valor_iva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("porcent_desc_adfac").setMetodoChange("calcularDescuentoPorce");
		tab_adq_factura.getColumna("valor_iva_adfac")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("porcent_desc_adfac").setVisible(false);
		tab_adq_factura.getColumna("valor_descuento_adfac").setVisible(false);
		tab_adq_factura.getColumna("aplica_descuento_adfac").setVisible(false);
		tab_adq_factura.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_adq_factura.getColumna("ide_tepro").setLectura(true);
		tab_adq_factura.getColumna("ide_tepro").setAutoCompletar();
		tab_adq_factura.getColumna("base_noiva_adfac").setEtiqueta();
		tab_adq_factura.getColumna("base_noiva_adfac")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo

		// AVACA - 2016-03-21
		tab_adq_factura.getColumna("IDE_PRTRA").setCombo(ser_Presupuesto.getTramite("true"));
		tab_adq_factura.getColumna("IDE_PRTRA").setAutoCompletar();
		tab_adq_factura.getColumna("IDE_PRTRA").setLectura(true);

		tab_adq_factura.getColumna("nro_autorizacion_sri_adq");
		// AVACA - 2016-03-21

		tab_adq_factura.getColumna("valor_descuento_adfac").setMetodoChange("calcularDescuento");
		tab_adq_factura.getColumna("total_adfac").setEtiqueta();
		tab_adq_factura.getColumna("total_adfac")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_factura.getColumna("activo_adfac").setLectura(true);
		tab_adq_factura.getColumna("activo_adfac").setValorDefecto("true");

		tab_adq_factura.getColumna("ide_srsuc").setCombo("sri_sustento_comprobante", "ide_srsuc",
				"codigo_srsuc || detalle_srsuc", "");
		tab_adq_factura.getColumna("ide_srsuc").setAutoCompletar();
		tab_adq_factura.setCondicion("ide_adfac=-1");

		tab_adq_factura.getColumna("ide_adsoc").setVisible(false);
		tab_adq_factura.getColumna("ide_prtra").setVisible(false);
		tab_adq_factura.getColumna("ide_tecpo").setVisible(false);
		// tab_adq_factura.getColumna("ide_srsuc").setVisible(false);

		tab_adq_factura.getColumna("ide_srsuc").setCombo("sri_sustento_comprobante", "ide_srsuc",
				"codigo_srsuc || detalle_srsuc", "");
		tab_adq_factura.getColumna("ide_srsuc").setAutoCompletar();
		tab_adq_factura.getColumna("ide_srsuc").setValorDefecto("2");

		tab_adq_factura.getColumna("fecha_factura_adfac").setRequerida(true);

		tab_adq_factura.getColumna("num_factura_adfac").setRequerida(true);

		tab_adq_factura.dibujar();
		PanelTabla pat_adq_factura = new PanelTabla();
		pat_adq_factura.setPanelTabla(tab_adq_factura);

		tab_adq_detalle.setId("tab_adq_detalle");
		tab_adq_detalle.setTabla("adq_detalle_factura", "ide_addef", 2);
		tab_adq_detalle.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("1", "true,false", ""));
		tab_adq_detalle.getColumna("ide_bomat").setAutoCompletar();
		tab_adq_detalle.getColumna("aplica_iva_addef").setVisible(false);
		tab_adq_detalle.getColumna("cantidad_addef").setMetodoChange("calcularDetallle");
		tab_adq_detalle.getColumna("valor_unitario_addef").setMetodoChange("calcularDetallle");

		tab_adq_detalle.getColumna("valor_total_addef")
				.setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_adq_detalle.getColumna("valor_total_addef").setEtiqueta();
		tab_adq_detalle.getColumna("activo_addef").setLectura(true);
		tab_adq_detalle.getColumna("activo_addef").setValorDefecto("true");
		tab_adq_detalle.getColumna("recibido_addef").setValorDefecto("true");

		tab_adq_detalle.getColumna("recibido_addef").setLectura(true);
		tab_adq_detalle.getColumna("por_descuento_addef").setVisible(false);
		tab_adq_detalle.getColumna("valor_descuento_addef").setVisible(false);
		tab_adq_detalle.getColumna("ide_bomat").setVisible(false);
		tab_adq_detalle.getColumna("recibido_addef").setVisible(false);
		tab_adq_detalle.getColumna("marca_addef").setVisible(false);
		tab_adq_detalle.getColumna("serie_addef").setVisible(false);
		tab_adq_detalle.getColumna("color_addef").setVisible(false);
		tab_adq_detalle.getColumna("modelo_addef").setVisible(false);
		tab_adq_detalle.getColumna("ide_adqdft").setVisible(false);
		tab_adq_detalle.getColumna("procesado_adfac").setVisible(false);

		tab_adq_detalle.getColumna("aplica_iva_adfac").setValorDefecto("true");

		tab_adq_detalle.dibujar();
		PanelTabla pat_adq_detalle = new PanelTabla();
		pat_adq_detalle.setPanelTabla(tab_adq_detalle);

		che_iva12.setId("che_iva12");
		che_iva12.setMetodoChange("cambiaIVA");
		Etiqueta eti_iva12 = new Etiqueta("IVA 14%");
		// bar_botones.agregarComponente(eti_iva12);
		// bar_botones.agregarComponente(che_iva12);

		Division div_division = new Division();
		div_division.dividir2(pat_adq_factura, pat_adq_detalle, "50%", "H");
		agregarComponente(div_division);

		// Inicializo componente dialogo para seleccionar EL TIPO DE RECEPCION

		Boton bot_proveedor = new Boton();
		bot_proveedor.setValue("Proveedor");
		bot_proveedor.setTitle("PROVEEDOR");
		bot_proveedor.setIcon("ui-icon-person");
		bot_proveedor.setMetodo("importarProveedor");
		bar_botones.agregarBoton(bot_proveedor);

		Boton bot_cargar_xml = new Boton();
		bot_cargar_xml.setValue("Importar XML");
		bot_cargar_xml.setTitle("IMPORTAR XML");
		bot_cargar_xml.setIcon("ui-icon-person");
		bot_cargar_xml.setMetodo("importarArchivo");
		bar_botones.agregarBoton(bot_cargar_xml);

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

	public void seleccionaElAnio() {
		tab_adq_factura.setCondicion(
				" extract(year from fecha_factura_adfac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="
						+ com_anio.getValue() + ") ");
		tab_adq_factura.ejecutarSql();
		tab_adq_detalle.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
	}

	public void importarProveedor() {

		if (!verificarSiSePuedeEditarFactura()) {
			return;
		}

		set_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
		set_proveedor.getTab_seleccion().ejecutarSql();
		set_proveedor.dibujar();

	}

	public void importarArchivo() {
		if (!verificarSiSePuedeEditarFactura()) {
			return;
		}
		dialogo.dibujar();

	}

	public void aceptarImportacion() {
		dialogo.cerrar();
	}

	public boolean verificarSiAplicaIva(Element elemento) {
		if (elemento != null) {
			Double porcentajeIva = elemento.getElementsByTagName("tarifa").getLength() > 0
					? pckUtilidades.CConversion.CDbl(elemento.getElementsByTagName("tarifa").item(0).getTextContent())
					: 0;
			System.out.println("porcentajeIva: " + porcentajeIva.toString() + " - "
					+ elemento.getElementsByTagName("tarifa").item(0).getTextContent());
			if (porcentajeIva > 0) {
				return true;
			}
		}
		return false;
	}

	public void validarArchivo(FileUploadEvent evt) {
		String ruc = null;
		String fecha_emision = null;
		String establecimiento = null;
		String ptoEmi = null;
		String secuencial = null;
		String numero_autorizacion = null;
		// establecimiento - ptoEmi - secuencial

		try {
			// tab_adq_factura.modificar(evt);
			if (com_anio.getValue() == null) {
				utilitario.agregarMensajeInfo("No se puede cargar el Archivo", "Favor Seleccione un Año");
				return;
			}

			if (tab_adq_detalle.getTotalFilas() > 0) {
				utilitario.agregarMensajeError("Existen registros cargados",
						"Favor limpie o borre los que se han cargado anteriormente...");
				return;
			}

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = null;
			InputStream inputStream = null;
			inputStream = evt.getFile().getInputstream();
			try {
				InputSource is = new InputSource(inputStream);
				is.setEncoding("UTF-8");
				doc = db.parse(is);
			} catch (org.xml.sax.SAXParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error al leer con UTF-8");

				InputSource is = new InputSource(inputStream);
				is.setEncoding("ISO-8859-1");
				doc = db.parse(is);
			}

			doc.getDocumentElement().normalize();

			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			if (doc.getElementsByTagName("estado").getLength() > 0) {
				if (doc.getElementsByTagName("estado").item(0).getTextContent().equals("AUTORIZADO")) {
					if (doc.getElementsByTagName("comprobante").getLength() > 0) {
						//NodeList docElements = doc.getElementsByTagName("comprobante");
						String xml_factura = doc.getElementsByTagName("comprobante").item(0).getTextContent();
						Document doc_factura = null;
						if (xml_factura.contains("<")) {
							doc_factura = db.parse(new InputSource(new StringReader(xml_factura)));
						} else {
							System.out.println("Utilizando la raiz");
							doc_factura = doc;
						}

						// System.out.println(xml_factura);
						// InputStream inputStream = new ByteArrayInputStream(xml_factura.getBytes());

						ruc = doc_factura.getElementsByTagName("ruc").getLength() > 0
								? doc_factura.getElementsByTagName("ruc").item(0).getTextContent()
								: null;
						establecimiento = doc_factura.getElementsByTagName("estab").getLength() > 0
								? doc_factura.getElementsByTagName("estab").item(0).getTextContent()
								: null;
						ptoEmi = doc_factura.getElementsByTagName("ptoEmi").getLength() > 0
								? doc_factura.getElementsByTagName("ptoEmi").item(0).getTextContent()
								: null;
						secuencial = doc_factura.getElementsByTagName("secuencial").getLength() > 0
								? doc_factura.getElementsByTagName("secuencial").item(0).getTextContent()
								: null;
						fecha_emision = doc_factura.getElementsByTagName("fechaEmision").getLength() > 0
								? doc_factura.getElementsByTagName("fechaEmision").item(0).getTextContent()
								: null;

						if (fecha_emision == null) {
							fecha_emision = doc_factura.getElementsByTagName("fechaEmisionDocSustento").getLength() > 0
									? doc_factura.getElementsByTagName("fechaEmisionDocSustento").item(0)
											.getTextContent()
									: null;
						}

						SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date date = dt.parse(fecha_emision);

						SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
						fecha_emision = dt1.format(date);

						// fecha_emision = utilitario.DateStringAString(fecha_emision, "");

						NodeList detalles = doc_factura.getElementsByTagName("detalle");
						for (int i = 0; i < detalles.getLength(); i++) {
							// System.out.println(detalles.item(i).getTextContent());
							Element detalle = (Element) detalles.item(i);
							Double cantidad = detalle.getElementsByTagName("cantidad").getLength() > 0
									? pckUtilidades.CConversion.CDbl(
											detalle.getElementsByTagName("cantidad").item(0).getTextContent())
									: 0;
							Double precio_unitario = detalle.getElementsByTagName("precioUnitario").getLength() > 0
									? pckUtilidades.CConversion.CDbl(
											detalle.getElementsByTagName("precioUnitario").item(0).getTextContent())
									: 0;
							Boolean aplica_iva = verificarSiAplicaIva(detalle);
							String descripcion = detalle.getElementsByTagName("descripcion").getLength() > 0
									? detalle.getElementsByTagName("descripcion").item(0).getTextContent()
									: null;

							System.out.println("aplica_iva:" + aplica_iva.toString());
							tab_adq_detalle.insertar();
							tab_adq_detalle.setValor("valor_unitario_addef", precio_unitario.toString());
							tab_adq_detalle.setValor("cantidad_addef", cantidad.toString());
							tab_adq_detalle.setValor("descripcion_addef", descripcion.toString());
							tab_adq_detalle.setValor("aplica_iva_adfac", aplica_iva.toString());

							tab_adq_detalle.modificar(0);
							calcular();

						}

					}

					numero_autorizacion = doc.getElementsByTagName("numeroAutorizacion").getLength() > 0
							? doc.getElementsByTagName("numeroAutorizacion").item(0).getTextContent()
							: null;

					tab_adq_factura.setValor("num_factura_adfac", establecimiento + "-" + ptoEmi + "-" + secuencial);
					tab_adq_factura.setValor("fecha_factura_adfac", utilitario.getFormatoFecha(fecha_emision));
					tab_adq_factura.setValor("nro_autorizacion_sri_adq", numero_autorizacion);

					calcular();

					// System.out.println(utilitario.getFormatoFecha(fecha_emision));

					TablaGenerica tg_proveedor = ser_bodega.getTablaProveedorPorRuc(ruc);

					// Importar proveedor
					if (tg_proveedor.getTotalFilas() > 0) {
						String ide_tepro = tg_proveedor.getValor("ide_tepro");
						tab_adq_factura.setValor("ide_tepro", ide_tepro);
					} else {
						utilitario.agregarMensajeError("No se encontro el proveedor", "No se encontro el proveedor");
					}

					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}

				} else {
					utilitario.agregarMensajeError("Factura No Autorizada", "Factura no autorizada");
				}
			} else {
				utilitario.agregarMensajeError("Factura No Autorizada", "Factura no autorizada");
			}

			// System.out.println(doc.getElementsByTagName("autorizacion").getLength());

			NodeList lista = doc.getDocumentElement().getChildNodes();

			/*
			 * for (int i = 0; i < lista.getLength(); i++) {
			 * System.out.println("---------");
			 * System.out.println(lista.item(i).getNodeName());
			 * //System.out.println(lista.item(i).getTextContent()); }
			 */

			// tab_perito.setDibujo(true);

			utilitario.addUpdate("tab_adq_factura");
			utilitario.addUpdate("tab_adq_detalle");

		} catch (Exception e) {
			// TODO: handle exception
			utilitario.agregarMensajeError("Factura No Autorizada", "Factura no autorizada, formato inválido.");
			e.printStackTrace();
		}
	}

	public void aceptarProveedor() {
		String str_seleccionado = set_proveedor.getValorSeleccionado();
		TablaGenerica tab_proveedor = ser_bodega.getTablaProveedor(str_seleccionado);
		if (str_seleccionado != null) {
			tab_adq_factura.setValor("ide_tepro", str_seleccionado);
			tab_adq_factura.modificar(tab_adq_factura.getFilaActual());
			/*
			 * if (formatearNumeroFactura()) { tab_adq_factura.guardar(); guardarPantalla();
			 * }
			 */
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

		utilitario.agregarMensajeInfo("No se Puede Cancelar",
				"Inicio un proceso de seleccion de compra que no lo puede cancelar en esta instancia, debe seleccionar una opciòn y continuar");
		return;
	}

	public boolean verificarSiSePuedeEditarFactura() {
		String ide_adfac = tab_adq_factura.getValor("ide_adfac");
		if (ide_adfac == null || ide_adfac == "") {
			// La factura no ha sido guardada aún
			return true;
		}

		TablaGenerica tg_retenciones = ser_Adquisicion.getRetencionesDeFactura(ide_adfac);
		if (tg_retenciones.getTotalFilas() > 0) {
			// Existen retenciones asociadas a la factura
			String numeros = "";
			String prefix = "";
			for (int i = 0; i < tg_retenciones.getTotalFilas(); i++) {
				String num = tg_retenciones.getValor(i, "num_retencion_teret");
				numeros += prefix + num;
				prefix = ",";
			}
			utilitario.agregarMensajeError("No puede editar la factura", "La factura tiene retenciones: " + numeros);
			return false;
		}
		// No existen retenciones asociadas, se puede modificar la factura
		return true;
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
			dou_valor_unitario_addef = pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor("valor_unitario_addef"));
		} catch (Exception e) {
		}
		// Calculamos el total
		dou_valor_total_addef = dou_valor_unitario_addef * dou_cantidad_addef;
		// Asignamos el total a la tabla detalle, con 3 decimales
		tab_adq_detalle.setValor("valor_total_addef", utilitario.getFormatoNumero(dou_valor_total_addef, 2));
		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_adq_detalle, "valor_total_addef", "tab_adq_factura");
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

			}

			if (tab_adq_detalle.getValor(i, "aplica_iva_adfac").trim().contentEquals("false")) {
				// Subtotal sin iva
				subtotal_sin_iva += pckUtilidades.CConversion.CDbl(tab_adq_detalle.getValor(i, "valor_total_addef"));

			}
		}

		double iva_adq_factura = subtotal_iva * duo_valor_iva;
		double subtotal_adq_factura = subtotal_iva + subtotal_sin_iva;
		double total_adq_factura = subtotal_adq_factura + iva_adq_factura;

		// Asigna el valor de la base imponible Sin Iva
		tab_adq_factura.setValor("base_noiva_adfac", utilitario.getFormatoNumero(subtotal_sin_iva, 2));

		// Asigna el valor de la base imponible Con Iva
		tab_adq_factura.setValor("base_iva_adfac", utilitario.getFormatoNumero(subtotal_iva, 2));

		tab_adq_factura.setValor("subtotal_adfac", utilitario.getFormatoNumero(subtotal_adq_factura, 2));

		tab_adq_factura.setValor("valor_iva_adfac", utilitario.getFormatoNumero(iva_adq_factura, 2));
		tab_adq_factura.setValor("total_adfac", utilitario.getFormatoNumero(total_adq_factura, 2));
		tab_adq_factura.modificar(tab_adq_factura.getFilaActual());// para que
																	// haga el
																	// update

		utilitario.addUpdateTabla(tab_adq_factura, "valor_iva_adfac,total_adfac,subtotal_adfac,base_iva_adfac",
				"tab_adq_detalle");
	}

	private boolean formatearNumeroFactura() {
		boolean correcto = false;
		try {
			String[] numeroFactura = tab_adq_factura.getValor("num_factura_adfac").split("-");
			String nuevoNumeroFactura = "";

			String strSucursal = pckUtilidades.Utilitario
					.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[0])), 3);
			String strPuntoEmision = pckUtilidades.Utilitario
					.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[1])), 3);
			String strSecuencial = pckUtilidades.Utilitario
					.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[2])), 9);

			nuevoNumeroFactura += strSucursal + "-";
			nuevoNumeroFactura += strPuntoEmision + "-";
			nuevoNumeroFactura += strSecuencial;

			tab_adq_factura.setValor("num_factura_adfac", nuevoNumeroFactura);

			if (tab_adq_factura.isFilaInsertada()) {
				if (!ser_Adquisicion.compruebaFacCompra(strSucursal, strPuntoEmision, strSecuencial,
						com_anio.getValue() + "", tab_adq_factura.getValor("ide_tepro")))
					correcto = true;
				else
					utilitario.agregarMensajeInfo("No se pudo guardar", "Número de factura repetido.");
			} else {
				correcto = true;
				// validar k no haya retencion
				TablaGenerica tab_retenciones = utilitario.consultar(
						"SELECT ide_teret, num_retencion_teret FROM tes_retencion where ide_coest=2 and ide_adfac="
								+ tab_adq_factura.getValor("ide_adfac"));
				if (tab_retenciones.getTotalFilas() > 0) {
					correcto = false;
					utilitario.agregarMensajeInfo("No se pudo guardar", "La factura cuenta con una retención...");
				}
			}

			// TODO: mejorar validación
			// correcto = true;

		} catch (Exception ex) {
			System.out.println("Error al padear el secuencial de la factura de compra: "
					+ tab_adq_factura.getValor("num_factura_adfac") + ". " + ex.getMessage());
			tab_adq_factura.setValor("num_factura_adfac", null);
			utilitario.agregarMensajeInfo("No se pudo guardar", "Valide el formato del número de la factura.");
		}
		return correcto;
	}

	private void refrescarTabla() {
		utilitario.addUpdateTabla(tab_adq_factura, "valor_iva_adfac, total_adfac, detalle_adfac, num_factura_adfac",
				"");
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
		if (!verificarSiSePuedeEditarFactura()) {
			return;
		}

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
				utilitario.agregarMensajeError("No se puede borrar",
						"El presente registro no se puede borrar existen detalles de factura");
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

	public Dialogo getDialogo() {
		return dialogo;
	}

	public void setDialogo(Dialogo dialogo) {
		this.dialogo = dialogo;
	}

	public Upload getUpl_archivo() {
		return upl_archivo;
	}

	public void setUpl_archivo(Upload upl_archivo) {
		this.upl_archivo = upl_archivo;
	}

}
