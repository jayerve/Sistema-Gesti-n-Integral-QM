package paq_adquisicion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_compras_areas extends Pantalla {

	public static String par_estado_modulo_compra;
	public static String par_modulo_adquisicion;
	public static String par_adquisicion;
	public static String par_certificacion;
	public static String par_solicitud;
	public static String par_empleado_adjudicador;
	public static String par_empleado_adjudica_infima;
	public static String par_empleado_adjudica_todos;
	public static String par_tipo_contrata_infima;

	private SeleccionTabla set_tipo_compra = new SeleccionTabla();
	private SeleccionTabla set_certificacion = new SeleccionTabla();
	private SeleccionTabla set_adjudicado = new SeleccionTabla();
	private SeleccionTabla set_proveedor = new SeleccionTabla();

	private Tabla tab_compras = new Tabla();
	private Tabla tab_detalle_compras = new Tabla();
	private Tabla tab_archivo_compras = new Tabla();
	private Tabla tab_poa_solicitud = new Tabla();

	@EJB
	private ServicioAdquisicion ser_Adquisicion = (ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_estados = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);

	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private String empleado;
	private Division div_division = new Division();

	public pre_compras_areas() {

		empleado = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		TablaGenerica area = utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true", empleado));
		System.out.println("empleado" + empleado);

		if (empleado == null || empleado.isEmpty()) {
			utilitario.agregarNotificacionInfo("Mensaje", "No exixte usuario registrado para el registro de compras");
			return;
		}
		if (area.getValor("ide_geare") == null || area.getValor("ide_geare").isEmpty()) {
			utilitario.agregarNotificacionInfo("Mensaje", "El Usuario asignado no posee un Area asignada dentro de la Instituciòn");
			return;
		}
		par_modulo_adquisicion = utilitario.getVariable("p_modulo_adquisicion");
		par_estado_modulo_compra = utilitario.getVariable("p_estado_modulo_compra");
		par_adquisicion = utilitario.getVariable("p_cotizacion_adquisicion");
		par_certificacion = utilitario.getVariable("p_certificacion_adquisicion");
		par_solicitud = utilitario.getVariable("p_solicitud_pago_adquisicion");
		par_empleado_adjudicador = utilitario.getVariable("p_empleado_adjudicador");
		par_empleado_adjudica_infima = utilitario.getVariable("p_empleado_adjudica_infima");
		par_empleado_adjudica_todos = utilitario.getVariable("p_empleado_adjudica_todos");
		par_tipo_contrata_infima = utilitario.getVariable("p_tipo_contrata_infima");
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

		tab_compras.setId("tab_compras");
		tab_compras.setHeader("SOLICITUD DE COMPRAS ( BIENES / SERVICIOS )");
		tab_compras.setTabla("adq_solicitud_compra", "ide_adsoc", 1);
		tab_compras.setCampoOrden("ide_adsoc desc");
		tab_compras.setCondicion("ide_geare=" + area.getValor("ide_geare"));
		tab_compras.getColumna("ide_adtic").setCombo("adq_tipo_contratacion", "ide_adtic", "detalle_adtic", "");
		tab_compras.getColumna("ide_adtic").setRequerida(true);
		tab_compras.getColumna("ide_adtic").setMetodoChange("aceptaAdjudicador");
		tab_compras.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_compras.getColumna("ide_coest").setLectura(true);
		tab_compras.getColumna("ide_coest").setAutoCompletar();
		tab_compras.getColumna("ide_copag").setCombo("cont_parametros_general", "ide_copag", "detalle_copag", "");
		tab_compras.getColumna("ide_copag").setLectura(true);
		tab_compras.getColumna("ide_copag").setAutoCompletar();
		tab_compras.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_compras.getColumna("ide_geedp").setLectura(true);
		tab_compras.getColumna("ide_geedp").setAutoCompletar();
		tab_compras.getColumna("ide_tepro").setCombo(ser_Bodega.getProveedor("true,false"));
		tab_compras.getColumna("ide_tepro").setLectura(true);
		tab_compras.getColumna("ide_tepro").setAutoCompletar();
		tab_compras.getColumna("ide_prtra").setCombo(ser_Adquisicion.getTramite("true,false"));
		tab_compras.getColumna("ide_prtra").setLectura(true);
		tab_compras.getColumna("ide_prtra").setAutoCompletar();
		tab_compras.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_compras.getColumna("ide_geare").setAutoCompletar();
		tab_compras.getColumna("fecha_proforma2_adsoc").setVisible(false);
		tab_compras.getColumna("valor_proforma2_adsoc").setVisible(false);
		tab_compras.getColumna("factura_proforma2_adsoc").setVisible(false);
		tab_compras.getColumna("oferente2_adsoc").setVisible(false);
		tab_compras.getColumna("fecha_proforma1_adsoc").setVisible(false);
		tab_compras.getColumna("factura_proforma1_adsoc").setVisible(false);
		tab_compras.getColumna("valor_proforma1_adsoc").setVisible(false);
		tab_compras.getColumna("proforma_proveedor_adsoc").setVisible(false);
		tab_compras.getColumna("fecha_proforma_proveedor_adsoc").setVisible(false);
		tab_compras.getColumna("oferente1_adsoc").setVisible(false);
		tab_compras.getColumna("fecha_adjudicacion_adsoc").setVisible(false);
		tab_compras.getColumna("tipo_recepcion_adsoc").setVisible(false);
		tab_compras.getColumna("ide_tepro").setVisible(false);
		tab_compras.getColumna("aprobado_adsoc").setVisible(false);
		tab_compras.getColumna("aprobado_adsoc").setValorDefecto("false");
		tab_compras.getColumna("ide_geare").setValorDefecto(area.getValor("ide_geare"));
		tab_compras.getColumna("fecha_registro").setValorDefecto(utilitario.getFechaActual());
		tab_compras.getColumna("fecha_solicitud_adsoc").setValorDefecto(utilitario.getFechaActual());
		tab_compras.getColumna("activo_adsoc").setValorDefecto("true");
		tab_compras.getColumna("activo_adsoc").setLectura(true);
		tab_compras.getColumna("aprobado_adsoc").setLectura(true);
		tab_compras.getColumna("ide_geare").setLectura(true);

		tab_compras.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_compras.getColumna("ide_gtemp").setLectura(true);
		tab_compras.getColumna("ide_gtemp").setAutoCompletar();
		tab_compras.getColumna("ide_adtie").setCombo("adq_tiempo_entrega", "ide_adtie", "detalle_adtie", "");
		tab_compras.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		List lista = new ArrayList();
		Object fila1[] = { "1", "PARCIAL" };
		Object fila2[] = { "0", "TOTAL" };
		Object fila3[] = { "2", "NINGUNO" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		tab_compras.getColumna("tipo_recepcion_adsoc").setRadio(lista, "2");
		tab_compras.getColumna("tipo_recepcion_adsoc").setRadioVertical(true);
		tab_compras.getColumna("tipo_recepcion_adsoc").setLectura(true);
		tab_compras.agregarRelacion(tab_detalle_compras);
		tab_compras.agregarRelacion(tab_archivo_compras);
		tab_compras.agregarRelacion(tab_poa_solicitud);

		tab_compras.setTipoFormulario(true);
		tab_compras.getGrid().setColumns(4);
		tab_compras.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_compras);

		Tabulador tab_Tabulador = new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		// detalle de solicitu de compra
		tab_detalle_compras.setId("tab_detalle_compras");
		tab_detalle_compras.setIdCompleto("tab_tabulador:tab_detalle_compras");
		tab_detalle_compras.setTabla("adq_detalle_solicitud", "ide_addes", 2);
		tab_detalle_compras.getColumna("activo_addes").setValorDefecto("true");
		tab_detalle_compras.getColumna("activo_addes").setLectura(true);
		tab_detalle_compras.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_detalle_compras);

		// agregar archivos
		tab_archivo_compras.setId("tab_archivo_compras");
		tab_archivo_compras.setIdCompleto("tab_tabulador:tab_archivo_compras");
		tab_archivo_compras.setTabla("adq_archivo", "ide_adarc", 3);
		tab_archivo_compras.getColumna("foto_adacr").setUpload("compras");
		tab_archivo_compras.getColumna("activo_adacr").setValorDefecto("true");
		tab_archivo_compras.getColumna("activo_adacr").setLectura(true);
		tab_archivo_compras.setTipoFormulario(true);
		tab_archivo_compras.getGrid().setColumns(4);
		tab_archivo_compras.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_archivo_compras);

		// agregar poa
		tab_poa_solicitud.setId("tab_poa_solicitud");
		tab_poa_solicitud.setIdCompleto("tab_tabulador:tab_poa_solicitud");
		tab_poa_solicitud.setTabla("adq_poa_solicitud", "ide_poaso", 4);
		tab_poa_solicitud.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_poa_solicitud.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_solicitud.getColumna("activo_poaso").setValorDefecto("true");
		tab_poa_solicitud.getColumna("valor_referencial_poaso").setLectura(true);
		tab_poa_solicitud.getColumna("ide_prpoa").setLectura(true);
		tab_poa_solicitud.getColumna("activo_poaso").setLectura(true);

		tab_poa_solicitud.dibujar();
		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_poa_solicitud);
		pat_panel4.getMenuTabla().getItem_eliminar().setDisabled(true);
		pat_panel4.getMenuTabla().getItem_insertar().setDisabled(true);
		pat_panel4.getMenuTabla().getItem_guardar().setDisabled(true);

		tab_Tabulador.agregarTab("DETALLE DE SOLICITUD", pat_panel2);
		tab_Tabulador.agregarTab("ARCHIVOS ADJUNTOS SOLICITUD", pat_panel3);
		tab_Tabulador.agregarTab("PARTIDA PRESUPUESTARIA", pat_panel4);

		div_division = new Division();
		div_division.dividir2(pat_panel1, tab_Tabulador, "50%", "h");

		agregarComponente(div_division);

		set_tipo_compra.setId("set_tipo_compra");
		set_tipo_compra.setSeleccionTabla(ser_contabilidad.getModuloParametros("true", par_modulo_adquisicion), "ide_copag");
		set_tipo_compra.getTab_seleccion().getColumna("detalle_copag").setNombreVisual("Tipo Compra");
		// set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
		set_tipo_compra.setTitle("Seleccione el tipo de compra");
		set_tipo_compra.getBot_aceptar().setMetodo("aceptarCompra");
		set_tipo_compra.getBot_cancelar().setMetodo("cancelarCompra");
		set_tipo_compra.setRadio();
		agregarComponente(set_tipo_compra);

		Boton bot_certificacion = new Boton();
		bot_certificacion.setValue("Agregar Certificaciòn Presupuestaria");
		bot_certificacion.setTitle("CERTIFICACION PRESUPUESTARIA");
		bot_certificacion.setIcon("ui-icon-person");
		bot_certificacion.setMetodo("importarCertificacion");
		bar_botones.agregarBoton(bot_certificacion);

		set_certificacion.setId("set_certificacion");
		set_certificacion.setSeleccionTabla(ser_Adquisicion.getTramite("true,false"), "ide_prtra");
		set_certificacion.getTab_seleccion().getColumna("fecha_tramite_prtra").setFiltro(true);
		set_certificacion.getTab_seleccion().getColumna("numero_oficio_prtra").setFiltro(true);
		set_certificacion.setTitle("Seleccione Certificaciòn");
		set_certificacion.getBot_aceptar().setMetodo("aceptarCertificacion");
		set_certificacion.setRadio();
		agregarComponente(set_certificacion);

		set_proveedor.setId("set_proveedor");
		set_proveedor.setSeleccionTabla(ser_Bodega.getProveedor("null"), "ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setNombreVisual("Nombre Proveedor");
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setNombreVisual("Ruc Proveedor");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setFiltro(true);
		set_proveedor.setTitle("Seleccione Proveedor");
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.setRadio();
		agregarComponente(set_proveedor);

		set_adjudicado.setId("set_adjudicado");
		set_adjudicado.setSeleccionTabla(ser_nomina.servicioEmpleadoContratoCodigo("false", "-1"), "ide_geedp");
		set_adjudicado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("Apellidos y Nombres");
		set_adjudicado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("Documento Identidad");
		set_adjudicado.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("Area");
		set_adjudicado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("Departamento");
		set_adjudicado.getTab_seleccion().getColumna("NOM_SUCU").setNombreVisual("Lugar Trabajo");
		set_adjudicado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_adjudicado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_adjudicado.setTitle("Seleccione Adjudicado");
		set_adjudicado.getBot_aceptar().setMetodo("aceptarAdjudicado");
		set_adjudicado.setRadio();
		agregarComponente(set_adjudicado);
	}

	public void aceptaAdjudicador(AjaxBehaviorEvent evt) {
		tab_compras.modificar(evt); // Siempre es la primera linea
		if (tab_compras.getValor("ide_adtic").equals(par_tipo_contrata_infima)) {
			TablaGenerica empleado_adjudica = utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true", par_empleado_adjudica_infima));

			tab_compras.setValor("ide_geedp", empleado_adjudica.getValor("IDE_GEEDP"));
			utilitario.addUpdateTabla(tab_compras, "ide_geedp,", "");

		} else {
			TablaGenerica empleado_adjudica = utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true", par_empleado_adjudica_todos));

			tab_compras.setValor("ide_geedp", empleado_adjudica.getValor("IDE_GEEDP"));
			utilitario.addUpdateTabla(tab_compras, "ide_geedp,", "");

		}

	}

	public void importarProveedor() {

		if (tab_compras.getValor("ide_coest").equals(par_adquisicion)) {

			set_proveedor.getTab_seleccion().setSql(ser_Bodega.getProveedor("true"));
			set_proveedor.getTab_seleccion().ejecutarSql();
			set_proveedor.dibujar();
		} else {
			utilitario.agregarMensajeInfo("Proveedor", "Para agregar un Proveedor la Solicitud de Compra debe encontrarse cotizada");
		}

	}

	public void aceptarProveedor() {
		String str_seleccionado = set_proveedor.getValorSeleccionado();
		TablaGenerica tab_proveedor = ser_Bodega.getTablaProveedor(str_seleccionado);
		if (str_seleccionado != null) {
			tab_compras.setValor("ide_tepro", str_seleccionado);
			tab_compras.modificar(tab_compras.getFilaActual());
			tab_compras.guardar();
			guardarPantalla();
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_compras");
	}

	public void aceptarCotizacion() {

		if (tab_compras.getValor("ide_coest").equals(par_certificacion)) {

			tab_compras.setValor("ide_coest", par_adquisicion);
			tab_compras.modificar(tab_compras.getFilaActual());
			tab_compras.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_compras");
		} else {
			utilitario.agregarMensajeInfo("Cotizacion", "Para cotizar la Solicitud de Compra debe encontrarse en estado certficacion presupuestaria");

		}

	}

	public void importarCertificacion() {
		if (tab_detalle_compras.getTotalFilas() > 0) {
			if (tab_compras.getValor("ide_coest").equals(par_estado_modulo_compra)) {
				set_certificacion.getTab_seleccion().setSql(ser_Adquisicion.getTramite("true"));
				set_certificacion.getTab_seleccion().ejecutarSql();
				set_certificacion.dibujar();
			} else {
				utilitario.agregarMensajeInfo("Certificación Presupuestaria", "Para agregar una Certificación Presupuestaria la solicitud de compra debe encontrarse en estado de compra ");

			}
		} else {
			utilitario.agregarNotificacionInfo("No Existe Detalle de Solicitud", "Ingrese un Detalle de Solicitud de Compra para agregar una partida presupuestaria");
		}
	}

	public void aceptarCertificacion() {

		String str_seleccionado = set_certificacion.getValorSeleccionado();
		TablaGenerica tab_certificacion = ser_Adquisicion.getTablaGenericaTramite(str_seleccionado);
		TablaGenerica tab_poa_certificacion = ser_Adquisicion.getTablaGenericaTramitePOA(str_seleccionado);
		if (tab_poa_certificacion.getTotalFilas() > 0) {
			if (str_seleccionado != null) {
				tab_compras.setValor("ide_coest", par_certificacion);
				tab_compras.setValor("ide_prtra", str_seleccionado);
				tab_compras.setValor("valor_adsoc", tab_certificacion.getValor("total_compromiso_prtra"));
				tab_compras.modificar(tab_compras.getFilaActual());
				tab_compras.guardar();
				for (int i = 0; i < tab_poa_certificacion.getTotalFilas(); i++) {
					tab_poa_solicitud.insertar();
					tab_poa_solicitud.setValor("ide_prpoa", tab_poa_certificacion.getValor(i, "ide_prpoa"));
					tab_poa_solicitud.setValor("valor_referencial_poaso", tab_poa_certificacion.getValor(i, "comprometido_prpot"));
				}
				tab_poa_solicitud.guardar();
				guardarPantalla();

			}
			set_certificacion.cerrar();
			utilitario.addUpdate("tab_compras");
		} else {
			set_certificacion.cerrar();
			utilitario.agregarNotificacionInfo("Certificaciòn Presupuestaria no es Vàlida", "La certificaciòn presupuestaria seleccionada no registra partidas presupuestarias para proceder con la solicitud de compra ");
		}
	}

	public void importarAdjudicado() {
		if (tab_compras.getValor("ide_tepro") == null) {
			utilitario.agregarMensajeInfo("Adjudicador", "Para agregar un Adjudicador la Solicitud de Compra debe poseer un Proveedor Adjudicado");
			return;
		} else if (tab_compras.getValor("ide_coest").equals(par_certificacion)) {
			set_adjudicado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContratoCodigo("true", par_empleado_adjudicador));
			set_adjudicado.getTab_seleccion().ejecutarSql();
			set_adjudicado.dibujar();
		} else {
			utilitario.agregarMensajeInfo("Adjudicador", "Para agregar un Adjudicador la Solicitud de Compra debe poseer Certificación Presupuestaria");
		}
	}

	public void aceptarAdjudicado() {

		String str_seleccionado = set_adjudicado.getValorSeleccionado();
		TablaGenerica tab_empleado = ser_nomina.ideEmpleadoContrato(str_seleccionado, "true");
		if (str_seleccionado != null) {
			tab_compras.setValor("ide_coest", par_solicitud);
			tab_compras.setValor("ide_geedp", str_seleccionado);
			tab_compras.setValor("aprobado_adsoc", "true");
			tab_compras.setValor("fecha_adjudicacion_adsoc", utilitario.getFechaActual());
			tab_compras.modificar(tab_compras.getFilaActual());

			tab_compras.guardar();
			guardarPantalla();

		}
		set_adjudicado.cerrar();
		utilitario.addUpdate("tab_compras");
	}

	public void aceptarCompra() {
		tab_compras.setValor("ide_copag", set_tipo_compra.getValorSeleccionado());
		utilitario.addUpdate("tab_compras");
		set_tipo_compra.cerrar();

	}

	public void cancelarCompra() {
		tab_compras.ejecutarSql();
		set_tipo_compra.cerrar();
	}

	// reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Solicitud Compra")) {
			System.out.println("entra reporte");

			if (rep_reporte.isVisible()) {
				System.out.println("entra reporte a imporimir " + rep_reporte.getPath().toString());

				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.clear();
				p_parametros.put("titulo", "SOLICITUD DE COMPRA");
				p_parametros.put("ide_adsoc", pckUtilidades.CConversion.CInt(tab_compras.getValor("ide_adsoc")));
				System.out.println("entra reporte a imporimir xxx " + p_parametros.toString());

				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();

			}
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if (tab_compras.isFocus()) {
			tab_compras.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			TablaGenerica area = utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true", empleado));

			tab_compras.setValor("ide_gtemp", ide_gtempxx);
			tab_compras.setValor("ide_coest", par_estado_modulo_compra);
			tab_compras.setValor("ide_geare", area.getValor("ide_geare"));
			set_tipo_compra.getTab_seleccion().setSql(ser_contabilidad.getModuloParametros("true", par_modulo_adquisicion));
			set_tipo_compra.getTab_seleccion().ejecutarSql();
			set_tipo_compra.dibujar();
			utilitario.addUpdate("tab_compras");
		} else if (tab_detalle_compras.isFocus()) {
			if (tab_compras.getValor("aprobado_adsoc").equals("true")) {
				utilitario.agregarNotificacionInfo("Registro no Editable", "La solicitud de compra seleccionada se encuentra aprobada y no puede ser editada");
				return;
			}
			tab_detalle_compras.insertar();
		} else if (tab_archivo_compras.isFocus()) {
			if (tab_compras.getValor("aprobado_adsoc").equals("true")) {
				utilitario.agregarNotificacionInfo("Registro no Editable", "La solicitud de compra seleccionada se encuentra aprobada y no puede ser editada");
				return;
			}
			tab_archivo_compras.insertar();
		} else if (tab_poa_solicitud.isFocus()) {
			if (tab_compras.getValor("aprobado_adsoc").equals("true")) {
				utilitario.agregarNotificacionInfo("Registro no Editable", "La solicitud de compra seleccionada se encuentra aprobada y no puede ser editada");
				return;
			}
			tab_poa_solicitud.insertar();
		}
	}

	@Override
	public void guardar() {

		// TODO Auto-generated method stub
		if (tab_compras.getValor("aprobado_adsoc").equals("true")) {
			utilitario.agregarNotificacionInfo("Registro no Editable", "La solicitud de compra seleccionada se encuentra aprobada y no puede ser editada");
			return;
		}
		if (tab_compras.guardar()) {
			if (tab_detalle_compras.guardar()) {
				if (tab_archivo_compras.guardar()) {
					tab_poa_solicitud.guardar();
				}
			}
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		if (tab_compras.getValor("aprobado_adsoc").equals("true")) {
			utilitario.agregarNotificacionInfo("Registro no Editable", "La solicitud de compra seleccionada se encuentra aprobada y no puede ser editada");
			return;
		}
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_compras() {
		return tab_compras;
	}

	public void setTab_compras(Tabla tab_compras) {
		this.tab_compras = tab_compras;
	}

	public Tabla getTab_poa_solicitud() {
		return tab_poa_solicitud;
	}

	public void setTab_poa_solicitud(Tabla tab_poa_solicitud) {
		this.tab_poa_solicitud = tab_poa_solicitud;
	}

	public Tabla getTab_detalle_compras() {
		return tab_detalle_compras;
	}

	public void setTab_detalle_compras(Tabla tab_detalle_compras) {
		this.tab_detalle_compras = tab_detalle_compras;
	}

	public Tabla getTab_archivo_compras() {
		return tab_archivo_compras;
	}

	public void setTab_archivo_compras(Tabla tab_archivo_compras) {
		this.tab_archivo_compras = tab_archivo_compras;
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

	public SeleccionTabla getSet_certificacion() {
		return set_certificacion;
	}

	public void setSet_certificacion(SeleccionTabla set_certificacion) {
		this.set_certificacion = set_certificacion;
	}

	public SeleccionTabla getSet_tipo_compra() {
		return set_tipo_compra;
	}

	public void setSet_tipo_compra(SeleccionTabla set_tipo_compra) {
		this.set_tipo_compra = set_tipo_compra;
	}

	public SeleccionTabla getSet_adjudicado() {
		return set_adjudicado;
	}

	public void setSet_adjudicado(SeleccionTabla set_adjudicado) {
		this.set_adjudicado = set_adjudicado;
	}

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

}
