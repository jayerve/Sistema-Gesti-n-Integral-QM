package paq_activos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.ImageIO;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import paq_activos.ejb.ServicioActivos;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import com.lowagie.text.pdf.Barcode128;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;

public class pre_activo_old extends Pantalla {
	private Tabla tab_activos_fijos = new Tabla();
	private Tabla tab_custodio = new Tabla();
	private Tabla tab_fecha = new Tabla();
	private Tabla tab_archivo = new Tabla();
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private SeleccionTabla set_empleado = new SeleccionTabla();
	private SeleccionTabla set_egreso = new SeleccionTabla();
	private Etiqueta eti_titulo = new Etiqueta();
	private Etiqueta eti_pie = new Etiqueta();
	private Confirmar con_guardar = new Confirmar();
	private Dialogo dia_egreso = new Dialogo();
	private Dialogo dia_actas = new Dialogo();
	private Texto tex_maximo = new Texto();
	private Dialogo seleccion_lote = new Dialogo();
	private Texto txt_numero_acta = new Texto();
	private Texto txt_documento = new Texto();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Grid grid_acta = new Grid();
	private Grid grid_acta_empleado = new Grid();
	private Dialogo dia_fecha = new Dialogo();
	private Dialogo dia_acta_nro = new Dialogo();
	private AreaTexto are_txt_base_legal = new AreaTexto();
	private AutoCompletar aut_empleado_acta = new AutoCompletar();
	private AreaTexto are_txt_observacion = new AreaTexto();
	private Check che_preliminar = new Check();
	private Combo comboComprobante = new Combo();
	private Combo comboUbicacion = new Combo();
	private Texto txt_cod_inicial = new Texto();
	private Texto txt_cod_final = new Texto();
	private Grid grid_lote = new Grid();
	public static String par_modulosec_actaCfisica;
	private double duo_iva;
	private String codigoUbicacion;

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_Contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

	private StreamedContent codBarras;
	private GraphicImage giBarra = new GraphicImage();

	public pre_activo_old() {

		par_modulosec_actaCfisica = utilitario.getVariable("p_modulo_secuencialActaCfisica");

		rep_reporte.setId("rep_reporte"); // id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");// ejecuta el
																	// metodo al
																	// aceptar
																	// reporte
		agregarComponente(rep_reporte);// agrega el componente a la pantalla
		bar_botones.agregarReporte();// aparece el boton de reportes en la barra
										// de botones
		self_reporte.setId("self_reporte"); // id
		agregarComponente(self_reporte);

		tab_activos_fijos.setId("tab_activos_fijos");
		tab_activos_fijos.setTabla("afi_activo", "ide_afact", 1);
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
		// tab_activos_fijos.getColumna("ide_prcla").setRequerida(true);
		tab_activos_fijos.getColumna("ide_prcla").setMetodoChange("cambiarCuentaContable");

		tab_activos_fijos.getColumna("ide_cocac").setCombo(ser_Contabilidad.getCuentaContable("true,false"));
		tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
		// tab_activos_fijos.getColumna("ide_cocac").setRequerida(true);

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
		tab_activos_fijos.getColumna("valor_unitario_afact").setMetodoChange("calcular");
		tab_activos_fijos.getColumna("cantidad_afact").setMetodoChange("calcular");
		tab_activos_fijos.getColumna("valor_neto_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_neto_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("valor_neto_afact").setMetodoChange("calcular");
		tab_activos_fijos.getColumna("valor_compra_afact").setEtiqueta();
		tab_activos_fijos.getColumna("valor_compra_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.getColumna("secuencial_afact").setEtiqueta();
		tab_activos_fijos.getColumna("secuencial_afact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_activos_fijos.setTipoFormulario(true);
		tab_activos_fijos.getGrid().setColumns(4);
		tab_activos_fijos.agregarRelacion(tab_custodio);
		tab_activos_fijos.agregarRelacion(tab_archivo);
		tab_activos_fijos.onSelect("seleccionarActivo");

		/* Iva Calculation */
		tab_activos_fijos.getColumna("valor_iva").setCombo(utilitario.getIVAListOptions());

		tab_activos_fijos.getColumna("valor_iva").setMetodoChange("seleccionaIVA");
		// tab_activos_fijos.getColumna("valor_iva").setRequerida(true);

		/* Combo comprobate */
		tab_activos_fijos.getColumna("tipo_comprobante").setCombo(utilitario.getComprobanteListOptions());

		tab_activos_fijos.dibujar();

		PanelTabla pat_activo_fijos = new PanelTabla();
		pat_activo_fijos.setPanelTabla(tab_activos_fijos);

		tab_custodio.setId("tab_custodio");
		tab_custodio.setIdCompleto("tab_tabulador:tab_custodio");
		tab_custodio.setTabla("afi_custodio", "ide_afcus", 2);
		tab_custodio.setCampoOrden("ide_afcus desc");
		tab_custodio.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_custodio.getColumna("ide_geedp").setLectura(true);
		tab_custodio.getColumna("ide_geedp").setAutoCompletar();
		tab_custodio.getColumna("ide_geedp").setUnico(true);
		tab_custodio.getColumna("ide_afact").setUnico(true);
		tab_custodio.getColumna("numero_acta_afcus").setUnico(true);
		tab_custodio.getColumna("gen_ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_custodio.getColumna("gen_ide_geedp").setLectura(true);
		tab_custodio.getColumna("gen_ide_geedp").setAutoCompletar();
		tab_custodio.getColumna("cod_barra_afcus").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_custodio.getColumna("cod_barra_afcus").setEtiqueta();

		tab_custodio.getColumna("activo_afcus").setValorDefecto("true");
		tab_custodio.getColumna("activo_afcus").setLectura(true);
		tab_custodio.setCampoOrden("activo_afcus desc");
		tab_custodio.setTipoFormulario(true);
		tab_custodio.getGrid().setColumns(4);
		tab_custodio.dibujar();
		PanelTabla pat_custodio = new PanelTabla();
		pat_custodio.setId("pat_custodio");
		pat_custodio.setPanelTabla(tab_custodio);

		tab_archivo.setId("tab_archivo");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		tab_archivo.setTabla("afi_archivo", "ide_afarc", 3);
		tab_archivo.getColumna("foto_afacr").setUpload("activos");
		tab_archivo.getColumna("activo_afacr").setValorDefecto("true");
		tab_archivo.setTipoFormulario(true);
		tab_archivo.getGrid().setColumns(4);
		tab_archivo.dibujar();
		PanelTabla pat_archivo = new PanelTabla();
		pat_archivo.setId("pat_archivo");
		pat_archivo.setPanelTabla(tab_archivo);

		// ////////imagen codigo de barra
		generarCodigoBarras(tab_custodio.getValor("ide_afact"));
		giBarra.setId("giBarra");
		giBarra.setWidth("300");
		giBarra.setHeight("120");
		giBarra.setTitle("EMGIRS-");
		giBarra.setValueExpression("value", crearValueExpression("pre_index.clase.codBarras"));

		// Inicio Dialogo CREDITO
		seleccion_lote.setId("dia_movimientos");
		seleccion_lote.setTitle("SELECCION CODIGO DE BARRAS LOTE");
		seleccion_lote.setHeight("20%");
		seleccion_lote.setWidth("30%");
		grid_lote.setColumns(2);
		txt_cod_inicial.setMaxlength(4);
		txt_cod_final.setMaxlength(4);
		txt_cod_inicial.setSoloEnteros();
		txt_cod_final.setSoloEnteros();
		grid_lote.getChildren().add(new Etiqueta("Inicio lote:"));
		grid_lote.getChildren().add(txt_cod_inicial);
		grid_lote.getChildren().add(new Etiqueta("Fin lote:"));
		grid_lote.getChildren().add(txt_cod_final);
		seleccion_lote.getBot_aceptar().setMetodo("abrirSeleccionLote");
		seleccion_lote.setDialogo(grid_lote);
		agregarComponente(seleccion_lote);

		// ///titulo emgirs
		Grid grid_titulo = new Grid();
		grid_titulo.setColumns(2);
		grid_titulo.setStyle("text-align:center;position:absolute;top:5px;left:55px;");
		eti_titulo.setStyle("font-size:22px;color:black;font-weight: bold;text-align:center;");
		eti_titulo.setValue("EMGIRS-EP ");
		grid_titulo.getChildren().add(eti_titulo);

		Grid grid_pie = new Grid();
		grid_pie.setColumns(2);
		grid_pie.setStyle("text-align:center;position:absolute;top:5px;left:55px;");
		eti_pie.setId("eti_pie");
		eti_pie.setStyle("font-size:22px;color:black;font-weight: bold;text-align:center;");
		// TODO: Codigo de barras nombre
		eti_pie.setValue(tab_custodio.getValor("ide_afact"));
		grid_pie.getChildren().add(eti_pie);

		Division divx = new Division();
		divx.setId("divx");
		divx.dividir3(grid_titulo, giBarra, grid_pie, "10%", "70%", "H");

		Division div = new Division();
		div.setId("div");
		div.dividir2(pat_custodio, divx, "70%", "V");

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		tab_tabulador.agregarTab("CUSTODIO", div);
		tab_tabulador.agregarTab("ARCHIVOS ANEXOS", pat_archivo);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_activo_fijos, tab_tabulador, "50%", "h");

		agregarComponente(div_division);

		// //boton empleado
		Boton bot_empleado = new Boton();
		bot_empleado.setIcon("ui-calendario");
		bot_empleado.setValue("Agregar Custodio");
		bot_empleado.setMetodo("importarEmpleado");
		//bar_botones.agregarBoton(bot_empleado);

		//

		// /empelado
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"), "ide_geedp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);

		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.setRadio();
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado);

		// /Boton depreciación activo
		Boton bot_depre = new Boton();
		bot_depre.setIcon("ui-icon-person");
		bot_depre.setValue("DEPRECIACION ACTIVO");
		bot_depre.setMetodo("abrirDialogo");
		//bar_botones.agregarBoton(bot_depre);

		/* Combo Ubicacion de Acta entrega recepcion */
		comboUbicacion.setId("combo-ubicacion");
		comboUbicacion.setValue(codigoUbicacion);
		comboUbicacion.setCombo("SELECT ide_afubi,detalle_afubi FROM  afi_ubicacion;");

		dia_fecha.setId("dia_fecha");
		dia_fecha.setTitle("FECHA DEPRECIACION ACTIVO");
		dia_fecha.setWidth("45%");
		dia_fecha.setHeight("45%");
		Grid gri_cuerpo = new Grid();

		tab_fecha.setId("tab_fecha");
		tab_fecha.setTabla("afi_custodio", "ide_afcus", 10);
		tab_fecha.setCondicion("ide_afcus=-1");// para que aparesca vacia
		tab_fecha.setTipoFormulario(true);
		tab_fecha.getGrid().setColumns(2);

		// oculto todos los campos
		tab_fecha.getColumna("fecha_entrega_afcus").setNombreVisual("FECHA CALCULO");
		tab_fecha.getColumna("fecha_entrega_afcus").setValorDefecto(utilitario.getFechaActual());
		tab_fecha.getColumna("ide_afcus").setVisible(false);
		tab_fecha.getColumna("ide_afact").setVisible(false);
		tab_fecha.getColumna("gen_ide_geedp").setVisible(false);
		tab_fecha.getColumna("detalle_afcus").setVisible(false);
		tab_fecha.getColumna("cod_barra_afcus").setVisible(false);
		tab_fecha.getColumna("nro_secuencial_afcus").setVisible(false);
		tab_fecha.getColumna("activo_afcus").setVisible(false);
		tab_fecha.getColumna("ide_geedp").setVisible(false);
		tab_fecha.getColumna("fecha_entrega_afcus").setVisible(true);
		tab_fecha.getColumna("fecha_descargo_afcus").setVisible(false);
		tab_fecha.getColumna("numero_acta_afcus").setVisible(false);
		tab_fecha.getColumna("razon_descargo_afcus").setVisible(false);
		tab_fecha.dibujar();
		gri_cuerpo.getChildren().add(tab_fecha);

		dia_fecha.getBot_aceptar().setMetodo("aceptarDialogo");

		dia_fecha.setDialogo(gri_cuerpo);
		agregarComponente(dia_fecha);

		// // set tabla egreso
		/*
		 * set_egreso.setId("set_egreso"); set_egreso.setSql("");
		 * set_egreso.getBot_aceptar().setMetodo("importar");
		 * agregarComponente(set_egreso);
		 */

		// //////boton bodega
		// Boton bot_egreso=new Boton();
		// bot_egreso.setIcon("ui-icon-person");
		// bot_egreso.setValue("EGREO BODEGA");
		// bot_egreso.setMetodo("importar");
		// bar_botones.agregarBoton(bot_egreso);

		dia_egreso.setId("dia_egreso");
		dia_egreso.setTitle("EGRESO BODEGA");
		dia_egreso.setWidth("30%");
		dia_egreso.setHeight("23%");
		dia_egreso.getBot_aceptar().setMetodo("importar");
		Grid gri_grid = new Grid();
		gri_grid.setStyle("height:" + (dia_egreso.getAltoPanel() - 10) + "px;overflow: auto;display: block;");
		gri_grid.setColumns(1);
		gri_grid.setWidth("98%");
		gri_grid.getChildren().add(new Etiqueta("INGRESE EL DOCUMENTO DE EGRESO DE BODEGA"));
		tex_maximo.setStyle("width:98%");
		// tex_maximo.setSoloEnteros();
		gri_grid.getChildren().add(tex_maximo);
		dia_egreso.setDialogo(gri_grid);
		agregarComponente(dia_egreso);
		// instancio autocompletar para cargar en el grid

		// Dialogo para Ingreso la Impresion de Actas
		dia_actas.setId("dia_actas");
		dia_actas.setTitle("ACTAS");
		dia_actas.setWidth("50%");
		dia_actas.setHeight("50%");
		dia_actas.getBot_aceptar().setMetodo("aceptarReporte");
		grid_acta.setColumns(2);
		are_txt_observacion.setId("are_txt_observacion");
		are_txt_observacion.setAutoResize(true);
		are_txt_observacion.setStyle("width:350px; height:70px");

		che_preliminar.setId("che_preliminar");

		grid_acta.getChildren().add(new Etiqueta("Observaciones"));
		grid_acta.getChildren().add(are_txt_observacion);
		grid_acta.getChildren().add(new Etiqueta("Buscar Empleado:"));
		aut_empleado_acta.setId("aut_empleado_acta");
		aut_empleado_acta.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP, APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  from GTH_EMPLEADO");

		grid_acta.getChildren().add(aut_empleado_acta);
		grid_acta.getChildren().add(new Etiqueta("Preliminar"));
		grid_acta.getChildren().add(che_preliminar);

		grid_acta.getChildren().add(new Etiqueta("Ubicaci\u00f3n"));
		grid_acta.getChildren().add(comboUbicacion);

		dia_actas.setDialogo(grid_acta);
		agregarComponente(dia_actas);

		are_txt_base_legal.setId("are_txt_base_legal");
		are_txt_base_legal.setAutoResize(true);
		are_txt_base_legal.setStyle("width:350px; height:70px");

		// Dialogo para Ingreso la Impresion de Actas
		dia_acta_nro.setId("dia_acta_nro");
		dia_acta_nro.setTitle("ACTAS");
		dia_acta_nro.setWidth("50%");
		dia_acta_nro.setHeight("50%");
		dia_acta_nro.getBot_aceptar().setMetodo("aceptarReporte");
		grid_acta_empleado.setColumns(2);
		grid_acta_empleado.getChildren().add(new Etiqueta("Observaciones"));
		grid_acta_empleado.getChildren().add(are_txt_base_legal);
		grid_acta_empleado.getChildren().add(new Etiqueta("Nro. Acta:"));
		grid_acta_empleado.getChildren().add(txt_numero_acta);
		grid_acta_empleado.getChildren().add(new Etiqueta("Documento:"));
		grid_acta_empleado.getChildren().add(txt_documento);
		dia_acta_nro.setDialogo(grid_acta_empleado);
		agregarComponente(dia_acta_nro);

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

	/*
	 * public void cambiarVehiculo(AjaxBehaviorEvent evt){
	 * tab_activos_fijos.modificar(evt);
	 * if(tab_activos_fijos.getValor("ide_afnoa").equals("108")){
	 * tab_activos_fijos.getColumna("marca_afact").getNombreVisual();
	 * tab_activos_fijos.getColumna("marca_afact").setNombreVisual("MOTOR");
	 * //utilitario.addUpdate("marca_afact");
	 * utilitario.addUpdateTabla(tab_activos_fijos, "marca_afact", "");
	 * //tab_activos_fijos.dibujar(); //utilitario.addUpdateTabla(arg0, arg1,
	 * arg2);(arg0, arg1, arg2); } }
	 */

	public void seleccionaIVA(AjaxBehaviorEvent evt) {
		tab_activos_fijos.modificar(evt);
		try {
			if (tab_activos_fijos.getValor("valor_iva") != null) {
				this.duo_iva = Double.parseDouble(tab_activos_fijos.getValor("valor_iva"));
				// ADD duo_iva
				this.calcular();
			}
		} catch (Exception e) {
			System.out.print("Error");
		}

	}

	public void importar() {
		if (tab_activos_fijos.isEmpty()) {
			// if(getMaximoSecuencialEmpleados()==0 && int_maximo_detalle==-1){
			if (dia_egreso.isVisible()) {
				if (tex_maximo.getValue() != null && !tex_maximo.getValue().toString().isEmpty()) {
					dia_egreso.cerrar();
				} else {
					utilitario.agregarMensajeInfo("Debe ingresar un valor en el campo  texto", "");
					return;
				}
			} else {
				dia_egreso.dibujar();
				return;
			}
		}

	}

	public void abrirDialogo() {
		// Hace aparecer el componente

		tab_fecha.limpiar();
		tab_fecha.insertar();
		dia_fecha.dibujar();
	}

	public void aceptarDialogo() {
		String fecha = tab_fecha.getValor("fecha_entrega_afcus");
		// TablaGenerica tab_consulta_fecha=
		// utilitario.consultar("select avalactivos('"+fecha+"')");
		utilitario.getConexion().ejecutarSql(
				"update afi_activo" + " set  vida_util_afact = 5" + " where vida_util_afact <=0;" + " update afi_activo" + " set fecha_calculo_afact ='" + fecha + "'" + " where fecha_calculo_afact is null;" + " update afi_activo"
						+ " set fecha_calculo_afact = '" + fecha + "';" + " update afi_activo" + " set valor_depre_mes_afact = valor_compra_afact/(vida_util_afact*12);" + " update afi_activo"
						+ " set val_depreciacion_periodo_afact = (valor_compra_afact/vida_util_afact) * EXTRACT( MONTH FROM fecha_calculo_afact)" + " where EXTRACT( year FROM fecha_calculo_afact) > EXTRACT( year FROM fecha_alta_afact);"
						+ " update afi_activo" + " set val_depreciacion_periodo_afact = (valor_compra_afact/vida_util_afact) *  EXTRACT( MONTH FROM age(fecha_calculo_afact,fecha_alta_afact))"
						+ " where EXTRACT( year FROM fecha_calculo_afact) = EXTRACT( year FROM fecha_alta_afact);" + " update afi_activo"
						+ " set valor_depreciacion_afact = (valor_compra_afact/vida_util_afact)* (EXTRACT( year FROM age(fecha_calculo_afact,fecha_alta_afact))*12 + EXTRACT( MONTH FROM age(fecha_calculo_afact,fecha_alta_afact)));" + " update afi_activo"
						+ " set valor_depreciacion_afact  = valor_compra_afact *0.9" + " where valor_depreciacion_afact >= valor_compra_afact;" + " update afi_activo" + " set valor_residual_afact = valor_compra_afact - valor_depreciacion_afact;");

		utilitario.agregarMensaje("Valoración", "Se ejecuto la valoracion con éxito");
		// utilitario.getConexion().consultar("select avalactivos('"+fecha+"')");
		dia_fecha.cerrar();
		// utilitario.addUpdateTabla(tab_activos_fijos,
		// "vida_util_afact,fecha_calculo_afact,valor_depre_mes_afact,val_depreciacion_periodo_afact,valor_depreciacion_afact,valor_residual_afact",
		// "");
		tab_activos_fijos.ejecutarSql();

	}

	public void importarEmpleado() {
		if (tab_activos_fijos.isEmpty()) {
			utilitario.agregarMensajeInfo("Activo no registrado", "Para agregar un custodio debe tener registrado un Activo Fijo");
			return;

		}
		if (tab_activos_fijos.getValor("secuencial_afact") == null) {
			utilitario.agregarMensajeInfo("Nro. secuencial no registrado", "Para agregar un custodio debe poseer un numero secuencial");
			return;
		}
		TablaGenerica numero_secuencial = utilitario.consultar(ser_activos.getActivosCodigo(tab_activos_fijos.getValor("ide_afact")));
		TablaGenerica secuencial_custodio = utilitario.consultar(ser_activos.getCustodioSecuencial(tab_activos_fijos.getValor("ide_afact")));
		Integer num_numero_secuencial = 0;
		try {
			num_numero_secuencial = pckUtilidades.CConversion.CInt(numero_secuencial.getValor("secuencial_afact"));
		} catch (Exception e) {
			System.out.println("Error num_numero_secuencial: " + e);
		}
		Integer num_secuencial_custodio = pckUtilidades.CConversion.CInt(secuencial_custodio.getValor("nro_secuencial_afcus")) + 1;
		if (num_numero_secuencial > 1) {

			if (num_secuencial_custodio > num_numero_secuencial) {
				utilitario.agregarMensajeInfo("Numero secuencial en uso", "No se puede agregar un custodio, existe un numero secuencial en uso, si desea cambiar de custodio utilice la opciòn Traspaso de Custodio");
				return;
			}

		}

		set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();
	}

	public void aceptarEmpleado() {
		String str_seleccionado = set_empleado.getValorSeleccionado();
		if (str_seleccionado != null) {
			// Inserto los empleados seleccionados en la tabla de resposable d
			// econtratacion
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionado, "true");
			TablaGenerica numero_secuencial = utilitario.consultar(ser_activos.getActivosCodigo(tab_activos_fijos.getValor("ide_afact")));
			TablaGenerica secuencial_custodio = utilitario.consultar(ser_activos.getCustodioSecuencial(tab_activos_fijos.getValor("ide_afact")));
			Integer num_numero_secuencial = pckUtilidades.CConversion.CInt(numero_secuencial.getValor("secuencial_afact"));
			Integer num_secuencial_custodio = Integer.parseInt(secuencial_custodio.getValor("nro_secuencial_afcus")) + 1;
			TablaGenerica codigo_cuenta = utilitario.consultar(ser_Contabilidad.getCuentaContableCodigo("true,false", tab_activos_fijos.getValor("ide_cocac")));

			// String codigo_barras
			// =codigo_cuenta.getValor("cue_codigo_remplazado")+"-"+tab_activos_fijos.getValor("ide_afact")+"-"+num_secuencial_custodio;
			// AWBECERRA cambio para la elaboracion del codigo de barras PAD
			// LEFT
			String codigo_barras = pckUtilidades.Utilitario.padLeft(tab_activos_fijos.getValor("ide_afact"), 4);

			System.out.println("codigo_barras " + codigo_barras);

			// System.out.println(" tabla generica"+tab_empleado_responsable.getSql());
			for (int i = 0; i < tab_empleado_responsable.getTotalFilas(); i++) {
				tab_custodio.insertar();
				tab_custodio.setValor("IDE_GEEDP", tab_empleado_responsable.getValor(i, "IDE_GEEDP"));
				if (num_numero_secuencial > 1) {
					tab_custodio.setValor("nro_secuencial_afcus", num_secuencial_custodio + "");
				} else {
					tab_custodio.setValor("nro_secuencial_afcus", num_numero_secuencial + "");
					// codigo_barras
					// =codigo_cuenta.getValor("cue_codigo_remplazado")+"-"+tab_activos_fijos.getValor("ide_afact")+"-"+num_numero_secuencial;
				}
				tab_custodio.setValor("cod_barra_afcus", codigo_barras);
			}
			set_empleado.cerrar();
			utilitario.addUpdateTabla(tab_custodio, "ide_geedp,nro_secuencial_afcus,cod_barra_afcus", "");
		} else {
			utilitario.agregarMensajeInfo("Debe agregar un Custodio", "");
		}

	}

	public void seleccionarActivo(SelectEvent evt) {
		tab_activos_fijos.seleccionarFila(evt);
		tab_custodio.ejecutarValorForanea(tab_activos_fijos.getValorSeleccionado());
		generarCodigoBarras(tab_custodio.getValor("ide_afact"));

	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		String cod = tab_custodio.getValor("ide_afact");
		if (cod == null || cod.isEmpty()) {
			codBarras = null;
			utilitario.addUpdate("tab_tabulador:giBarra");
			return;
		}
		/*
		 * try{ Barcode128 code128 = new Barcode128(); code128.setCode(cod);
		 * java.awt.Image img = code128.createAwtImage(Color.BLACK,
		 * Color.WHITE); BufferedImage outImage = new
		 * BufferedImage(img.getWidth(null), img.getHeight(null),
		 * BufferedImage.TYPE_INT_RGB); outImage.getGraphics().drawImage(img, 0,
		 * 0, null); ByteArrayOutputStream bytesOut = new
		 * ByteArrayOutputStream(); ImageIO.write(outImage, "jpeg", bytesOut);
		 * bytesOut.flush(); byte[] jpgImageData = bytesOut.toByteArray();
		 * codBarras= new DefaultStreamedContent(new
		 * ByteArrayInputStream(jpgImageData), "image/png");
		 * giBarra.setValue(codBarras);
		 * utilitario.addUpdate("tab_tabulador:giBarra"); }catch(Exception ex){
		 * System.out.println(ex.getMessage()); ex.printStackTrace(); }
		 * eti_pie.setValue(tab_custodio.getValor("cod_barra_afcus"));
		 * utilitario.addUpdate("tab_tabulador:eti_pie");
		 */
		generarCodigoBarras(cod);

	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		String cod = tab_custodio.getValor("ide_afact");
		if (cod == null || cod.isEmpty()) {
			codBarras = null;
			utilitario.addUpdate("tab_tabulador:giBarra");
			return;
		}
		/*
		 * try{ Barcode128 code128 = new Barcode128(); code128.setCode(cod);
		 * java.awt.Image img = code128.createAwtImage(Color.BLACK,
		 * Color.WHITE); BufferedImage outImage = new
		 * BufferedImage(img.getWidth(null), img.getHeight(null),
		 * BufferedImage.TYPE_INT_RGB); outImage.getGraphics().drawImage(img, 0,
		 * 0, null); ByteArrayOutputStream bytesOut = new
		 * ByteArrayOutputStream(); ImageIO.write(outImage, "jpeg", bytesOut);
		 * bytesOut.flush(); byte[] jpgImageData = bytesOut.toByteArray();
		 * codBarras= new DefaultStreamedContent(new
		 * ByteArrayInputStream(jpgImageData), "image/png");
		 * giBarra.setValue(codBarras);
		 * utilitario.addUpdate("tab_tabulador:giBarra"); }catch(Exception ex){
		 * System.out.println(ex.getMessage()); ex.printStackTrace(); }
		 * eti_pie.setValue(tab_custodio.getValor("cod_barra_afcus"));
		 * utilitario.addUpdate("tab_tabulador:eti_pie");
		 */
		generarCodigoBarras(cod);

	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		String cod = tab_custodio.getValor("ide_afact");
		if (cod == null || cod.isEmpty()) {
			codBarras = null;
			utilitario.addUpdate("tab_tabulador:giBarra");
			return;
		}
		/*
		 * try{ Barcode128 code128 = new Barcode128(); code128.setCode(cod);
		 * java.awt.Image img = code128.createAwtImage(Color.BLACK,
		 * Color.WHITE); BufferedImage outImage = new
		 * BufferedImage(img.getWidth(null), img.getHeight(null),
		 * BufferedImage.TYPE_INT_RGB); outImage.getGraphics().drawImage(img, 0,
		 * 0, null); ByteArrayOutputStream bytesOut = new
		 * ByteArrayOutputStream(); ImageIO.write(outImage, "jpeg", bytesOut);
		 * bytesOut.flush(); byte[] jpgImageData = bytesOut.toByteArray();
		 * codBarras= new DefaultStreamedContent(new
		 * ByteArrayInputStream(jpgImageData), "image/png");
		 * giBarra.setValue(codBarras);
		 * utilitario.addUpdate("tab_tabulador:giBarra"); }catch(Exception ex){
		 * System.out.println(ex.getMessage()); ex.printStackTrace(); }
		 * eti_pie.setValue(tab_custodio.getValor("cod_barra_afcus"));
		 * utilitario.addUpdate("tab_tabulador:eti_pie");
		 */
		generarCodigoBarras(cod);
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		String cod = tab_custodio.getValor("ide_afact");
		if (cod == null || cod.isEmpty()) {

			codBarras = null;
			utilitario.addUpdate("tab_tabulador:giBarra");
			return;
		}
		/*
		 * try{ Barcode128 code128 = new Barcode128(); code128.setCode(cod);
		 * java.awt.Image img = code128.createAwtImage(Color.BLACK,
		 * Color.WHITE); BufferedImage outImage = new
		 * BufferedImage(img.getWidth(null), img.getHeight(null),
		 * BufferedImage.TYPE_INT_RGB); outImage.getGraphics().drawImage(img, 0,
		 * 0, null); ByteArrayOutputStream bytesOut = new
		 * ByteArrayOutputStream(); ImageIO.write(outImage, "jpeg", bytesOut);
		 * bytesOut.flush(); byte[] jpgImageData = bytesOut.toByteArray();
		 * codBarras= new DefaultStreamedContent(new
		 * ByteArrayInputStream(jpgImageData), "image/png");
		 * giBarra.setValue(codBarras);
		 * utilitario.addUpdate("tab_tabulador:giBarra"); }catch(Exception ex){
		 * System.out.println(ex.getMessage()); ex.printStackTrace(); }
		 * eti_pie.setValue(tab_custodio.getValor("cod_barra_afcus"));
		 * utilitario.addUpdate("tab_tabulador:eti_pie");
		 */

		generarCodigoBarras(cod);
	}

	// reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void abrirSeleccionLote() {
		if (seleccion_lote.isVisible()) {
			seleccion_lote.cerrar();

			if (pckUtilidades.CConversion.CInt(txt_cod_inicial.getValue().toString()) > Integer.parseInt(txt_cod_final.getValue().toString())) {
				utilitario.agregarMensajeError("Error", "El c\u00f3digo de barras incial debe ser menor al final");
				return;
			}

			p_parametros = new HashMap();
			p_parametros.put("titulo", "Codigo de Barras");
			p_parametros.put("sec_inicial", Integer.valueOf(txt_cod_inicial.getValue().toString()));
			p_parametros.put("sec_final", Integer.valueOf(txt_cod_final.getValue().toString()));
			self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
			self_reporte.dibujar();
		}

	}

	public void aceptarReporte() {
		String str_director_administrativo = utilitario.getVariable("p_director_adminsitrativo");
		String str_jefe_activos_fijos = utilitario.getVariable("p_jefe_activos_fijos");
		if (rep_reporte.getReporteSelecionado().equals("Activo")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "Activo");
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt("7"));
				p_parametros.put("ide_empr", pckUtilidades.CConversion.CInt("0"));
				p_parametros.put("ide_sucu", pckUtilidades.CConversion.CInt("1"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Actividad")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "Actividad");
				p_parametros.put("pide_tipo", Integer.parseInt(tab_activos_fijos.getValor("afi_activo")));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Activo Actividad")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "Activo Actividad");
				p_parametros.put("pide_ubicacion", Integer.parseInt(tab_activos_fijos.getValor("ide_afubi")));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();

			}
		} else if (rep_reporte.getReporteSelecionado().equals("Codigo de Barras")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "Codigo de Barras");
				p_parametros.put("pide_barras", tab_custodio.getValor("ide_afact"));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Acta Constatación Física")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				dia_actas.dibujar();
			} else if (dia_actas.isVisible()) {
				dia_actas.cerrar();
				p_parametros.put("titulo", "ACTA DE CONSTATACIÓN FÍSICA");
				p_parametros.put("pide_empleado", pckUtilidades.CConversion.CInt(aut_empleado_acta.getValor()));
				p_parametros.put("pbase_legal", are_txt_observacion.getValue());
				p_parametros.put("pnum_acta", " ");
				// p_parametros.put("pnum_acta", txt_numero_acta.getValue());
				// p_parametros.put("pnum_acta", ser_Contabilidad
				// .numeroSecuencial(par_modulosec_actaCfisica));
				p_parametros.put("pdirector_administrativo", str_director_administrativo);
				p_parametros.put("pjefe_activos", str_jefe_activos_fijos);
				if (comboUbicacion.getValue() == null) {
					p_parametros.put("p_ubicacion", -1);
				} else {
					p_parametros.put("p_ubicacion", Integer.valueOf((String) comboUbicacion.getValue()));
				}

				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
				/*
				 * if (!che_preliminar.getValue().toString()
				 * .equalsIgnoreCase("true"))
				 * ser_Contabilidad.guardaSecuencial(ser_Contabilidad
				 * .numeroSecuencial(par_modulosec_actaCfisica),
				 * par_modulosec_actaCfisica);
				 */
				System.out.println("Secuencial acta constatacion fisica: " + ser_Contabilidad.numeroSecuencial(par_modulosec_actaCfisica));
			}

		}

		else if (rep_reporte.getReporteSelecionado().equals("Acta Entrega Recepción")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				dia_acta_nro.dibujar();
			} else if (dia_acta_nro.isVisible()) {
				dia_acta_nro.cerrar();
				p_parametros.put("titulo", "ACTA ENTREGA RECEPCION");
				p_parametros.put("pnum_acta", txt_numero_acta.getValue());
				p_parametros.put("pbase_legal", are_txt_base_legal.getValue());
				p_parametros.put("pdirector_administrativo", str_director_administrativo);
				p_parametros.put("pjefe_activos", str_jefe_activos_fijos);
				p_parametros.put("empleado_generacion", utilitario.getVariable("EMP_SESSION"));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}

		} else if (rep_reporte.getReporteSelecionado().equals("Acta Cambio Custodio")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				dia_acta_nro.dibujar();
			} else if (dia_acta_nro.isVisible()) {
				dia_acta_nro.cerrar();
				p_parametros.put("titulo", "ACTA DE TRASPASO DE BIENES");
				p_parametros.put("pnum_acta", txt_numero_acta.getValue());
				p_parametros.put("empleado_documento", txt_documento.getValue());
				p_parametros.put("pbase_legal", are_txt_base_legal.getValue());
				p_parametros.put("pdirector_administrativo", str_director_administrativo);
				p_parametros.put("pjefe_activos", str_jefe_activos_fijos);

				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Codigo de barras por LOTE")) {
			if (rep_reporte.isVisible()) {
				rep_reporte.cerrar();
				seleccion_lote.dibujar();
			}
		}

	}

	private void generarCodigoBarras(String cod) {
		if (cod == null || cod.isEmpty()) {
			codBarras = null;
			utilitario.addUpdate("tab_tabulador:giBarra");
			return;
		}
		try {
			cod = pckUtilidades.Utilitario.padLeft(cod, 4);
			Barcode128 code128 = new Barcode128();
			code128.setCode(cod);
			java.awt.Image img = code128.createAwtImage(Color.BLACK, Color.WHITE);
			BufferedImage outImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			outImage.getGraphics().drawImage(img, 0, 0, null);
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ImageIO.write(outImage, "jpeg", bytesOut);
			bytesOut.flush();
			byte[] jpgImageData = bytesOut.toByteArray();
			codBarras = new DefaultStreamedContent(new ByteArrayInputStream(jpgImageData), "image/png");
			giBarra.setValue(codBarras);
			utilitario.addUpdate("tab_tabulador:giBarra");
			eti_pie.setValue(cod);
			utilitario.addUpdate("tab_tabulador:eti_pie");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void calcular() {
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

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_activos_fijos.isFocus()) {
			tab_activos_fijos.insertar();
		} else if (tab_custodio.isFocus()) {
			tab_custodio.insertar();
		} else if (tab_archivo.isFocus()) {
			tab_archivo.insertar();
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		ser_Contabilidad.limpiarAcceso("afi_activo");
		ser_Contabilidad.limpiarAcceso("afi_custodio");

		if (tab_activos_fijos.guardar()) {
			if (tab_custodio.guardar()) {

				generarCodigoBarras(tab_custodio.getValor("ide_afact"));

				eti_pie.setValue(tab_custodio.getValor("ide_afact"));
				utilitario.addUpdate("eti_pie");
				if (tab_archivo.guardar()) {

				}
			}

		}

		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeError("Alerta", "No es posible eliminar el registro");
		// utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_activos_fijos() {
		return tab_activos_fijos;
	}

	public void setTab_activos_fijos(Tabla tab_activos_fijos) {
		this.tab_activos_fijos = tab_activos_fijos;
	}

	public Tabla getTab_custodio() {
		return tab_custodio;
	}

	public void setTab_custodio(Tabla tab_custodio) {
		this.tab_custodio = tab_custodio;
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

	public StreamedContent getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(StreamedContent codBarras) {
		this.codBarras = codBarras;
	}

	private ValueExpression crearValueExpression(String expresion) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{" + expresion + "}", Object.class);
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public Tabla getTab_fecha() {
		return tab_fecha;
	}

	public void setTab_fecha(Tabla tab_fecha) {
		this.tab_fecha = tab_fecha;
	}

	public Dialogo getDia_fecha() {
		return dia_fecha;
	}

	public void setDia_fecha(Dialogo dia_fecha) {
		this.dia_fecha = dia_fecha;
	}

	public SeleccionTabla getSet_egreso() {
		return set_egreso;
	}

	public void setSet_egreso(SeleccionTabla set_egreso) {
		this.set_egreso = set_egreso;
	}

	public Dialogo getDia_egreso() {
		return dia_egreso;
	}

	public void setDia_egreso(Dialogo dia_egreso) {
		this.dia_egreso = dia_egreso;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Dialogo getDia_actas() {
		return dia_actas;
	}

	public void setDia_actas(Dialogo dia_actas) {
		this.dia_actas = dia_actas;
	}

	public AutoCompletar getAut_empleado_acta() {
		return aut_empleado_acta;
	}

	public void setAut_empleado_acta(AutoCompletar aut_empleado_acta) {
		this.aut_empleado_acta = aut_empleado_acta;
	}

	public Dialogo getDia_acta_nro() {
		return dia_acta_nro;
	}

	public void setDia_acta_nro(Dialogo dia_acta_nro) {
		this.dia_acta_nro = dia_acta_nro;
	}

	public Tabla getTab_archivo() {
		return tab_archivo;
	}

	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public Texto getTxt_cod_inicial() {
		return txt_cod_inicial;
	}

	public void setTxt_cod_inicial(Texto txt_cod_inicial) {
		this.txt_cod_inicial = txt_cod_inicial;
	}

	public Texto getTxt_cod_final() {
		return txt_cod_final;
	}

	public void setTxt_cod_final(Texto txt_cod_final) {
		this.txt_cod_final = txt_cod_final;
	}

	public Combo getComboIVA() {
		return comboComprobante;
	}

	public void setComboIVA(Combo comboIVA) {
		this.comboComprobante = comboIVA;
	}

}
