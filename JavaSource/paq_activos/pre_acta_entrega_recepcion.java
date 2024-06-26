/**
 * 
 */
package paq_activos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_activos.ejb.ServicioActivos;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;

/**
 * Pantalla que permite la generaci&oacute;n de actas de
 * entrega/recepci&oacute;n
 * 
 * @author ccaceres
 * @since 2017/05/16
 * 
 */
public class pre_acta_entrega_recepcion extends Pantalla {

	private Tabla tab_cabecera = new Tabla();
	private Tabla tab_detalle_custodio = new Tabla();
	private Tabla tab_detalle_activo = new Tabla();

	private SeleccionTabla set_empleado = new SeleccionTabla();
	private SeleccionTabla set_ingresos_activos = new SeleccionTabla();
	private SeleccionTabla set_activos = new SeleccionTabla();

	private Combo com_anio = new Combo();
	private Radio lis_activo = new Radio();
	private Dialogo dia_bodega = new Dialogo();

	private String parametroSecuencial = "p_sec_acta_entrega_recepcion";
	private String tipoDeActa = "1";

	public static String par_secuencial_acta;
	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);
	private Dialogo dlg_observacion = new Dialogo();
	private Grid grid_observacion = new Grid();
	private AreaTexto txt_observacion = new AreaTexto();

	/**
	 * Abre un popup que permite editar o agregar una observaci&oacute;n al acta
	 * ya guardada
	 */
	public void abrirObservacion() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
		} else if (tab_cabecera.getValorSeleccionado() == null || tab_cabecera.getValorSeleccionado().equals("-1")) {
			utilitario.agregarMensaje("Prohibición", "El botón de edición de observación se puede utilizar cuando el acta se encuentra guardada.");
		} else {
			// TODO validar que el acta no se encuentra autorizada y con
			// prohibición de edición
			if (tab_cabecera.getValor("aprobacion_adm") == "false" || tab_cabecera.getValor("aprobacion_adm") == null) {
				txt_observacion.setValue(tab_cabecera.getValor("detalle_afdoc"));
				dlg_observacion.dibujar();
			} else {
				utilitario.agregarMensaje("Prohibición", "El acta se encuentra autorizada, por tanto no se pueden realizar modificaciones.");
			}
		}
	}

	/**
	 * Permite guardar la informai&oacute;n ingresada al campo observacion
	 */
	public void guardarObservacion() {
		System.out.println("guardarObservacion " + txt_observacion.getValue().toString());

		tab_cabecera.modificar(tab_cabecera.getFilaActual());
		tab_cabecera.setValor("detalle_afdoc", txt_observacion.getValue().toString());
		tab_cabecera.guardar();
		guardarPantalla();
		dlg_observacion.cerrar();
	}

	/**
	 * prepara la tabla que muestra la cabecera del acta
	 */
	private void preparaCabecera() {
		tab_cabecera = new Tabla();
		tab_cabecera.setId("tab_cabecera");
		tab_cabecera.setTabla("afi_docu", "ide_afdoc", 1);
		tab_cabecera.setCampoOrden("ide_afdoc desc");
		tab_cabecera.getColumna("rec_ide_afdoc").setVisible(false);
		tab_cabecera.getColumna("ide_aftidoc").setCombo("SELECT ide_aftidoc, detalle_aftidoc  FROM afi_tipo_docu");
		tab_cabecera.getColumna("ide_aftidoc").setAutoCompletar();
		tab_cabecera.getColumna("ide_aftidoc").setValorDefecto(tipoDeActa);
		tab_cabecera.getColumna("ide_aftidoc").setLectura(true);
		tab_cabecera.getColumna("ide_geani").setVisible(false);
		tab_cabecera.getColumna("nro_secuencial_afdoc").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_acta));
		// tab_cabecera.getColumna("nro_secuencial_afdoc").setLectura(true);
		tab_cabecera.getColumna("fecha_afdoc").setValorDefecto(utilitario.getFechaActual());
		tab_cabecera.getColumna("activo_afdoc").setValorDefecto("true");
		tab_cabecera.getColumna("activo_afdoc").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_cabecera.getColumna("ide_gtemp").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setAutoCompletar();
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setLectura(true);
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setAutoCompletar();
		tab_cabecera.getColumna("activo_afdoc").setValorDefecto("true");
		tab_cabecera.setCondicion("ide_geani=-1 and ide_aftidoc='" + tipoDeActa + "'");
		tab_cabecera.setTipoFormulario(true);
		tab_cabecera.getGrid().setColumns(6);
		tab_cabecera.dibujar();
	}

	/**
	 * prepara la tabla que muestra el detalle de custodios del acta
	 */
	private void preparaDetalleCustodio() {
		tab_detalle_custodio.setId("tab_detalle_custodio");
		tab_detalle_custodio.setTabla("afi_doc_detalle_custodio", "ide_afddc", 2);

		tab_detalle_custodio.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_detalle_custodio.getColumna("ide_gtemp").setLectura(true);
		tab_detalle_custodio.getColumna("ide_gtemp").setAutoCompletar();
		tab_detalle_custodio.getColumna("activo_afdda").setValorDefecto("true");
		tab_detalle_custodio.getColumna("activo_afdda").setVisible(false);
		tab_detalle_custodio.getColumna("recibe_afddc").setValorDefecto("true");
		tab_detalle_custodio.getColumna("recibe_afddc").setVisible(false);
		tab_detalle_custodio.getColumna("ide_afdoc").setVisible(true);
		tab_detalle_custodio.setCondicion("ide_afdoc=-1");
		tab_detalle_custodio.setTipoFormulario(false);
		tab_detalle_custodio.dibujar();
	}

	@Override
	public void buscar() {
		super.buscar();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	};

	/**
	 * prepara la tabla que muestra el detalle de activos del acta
	 */
	private void preparaDetalleActivo() {
		tab_detalle_activo.setId("tab_detalle_activo");
		tab_detalle_activo.setTabla("afi_doc_detalle_activo", "ide_afdda", 3);
		tab_detalle_activo.getColumna("ide_afact").setCombo(ser_activos.getDescripcionActivo());
		tab_detalle_activo.getColumna("ide_afact").setLectura(true);
		tab_detalle_activo.getColumna("ide_afact").setAutoCompletar();

		tab_detalle_activo.getColumna("ide_afubi").setCombo(ser_activos.getUbicaciones());
		// tab_detalle_activo.getColumna("ide_afubi").setAutoCompletar();

		tab_detalle_activo.getColumna("activo_afdda").setValorDefecto("true");
		tab_detalle_activo.getColumna("activo_afdda").setVisible(false);
		tab_detalle_activo.getColumna("ide_afdoc").setVisible(false);
		tab_detalle_activo.setCondicion("ide_afdoc=-1");
		tab_detalle_activo.setTipoFormulario(false);
		tab_detalle_activo.dibujar();
	}

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de custodios del
	 * acta
	 */
	private void prepararSelectorCustodio() {
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadosActivos("true"), "ide_gtemp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("primer_nombre_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("primer_nombre_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("segundo_nombre_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("segundo_nombre_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleado.setTitle("Seleccione uno o varios Empleados");
		set_empleado.getBot_aceptar().setMetodo("aceptarCustodios");
		agregarComponente(set_empleado);
	}

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de custodios del
	 * acta
	 */
	private void prepararSelectorPreActivos() {
		List lista = new ArrayList();
		Object fila1[] = { "0", "POR INGRESOS PENDIENTES" };
		Object fila2[] = { "1", "TODOS LOS ACTIVOS" };
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setRadio(lista);
		lis_activo.setVertical();
		dia_bodega.setId("dia_bodega");
		dia_bodega.setTitle("SELECCIONE COMO DESEA VISUALIZAR LOS ACTIVOS");
		dia_bodega.setHeight("20%");
		dia_bodega.setWidth("30%");

		dia_bodega.getBot_aceptar().setMetodo("verificarSeleccion");
		dia_bodega.setDialogo(lis_activo);
		agregarComponente(dia_bodega);

	}

	public void verificarSeleccion() {
		if (lis_activo.getValue() == null) {
			utilitario.agregarMensajeInfo("SELECTOR INCORRECTO", "Debe Seleccionar una Opción");
			return;
		} else {
			if (lis_activo.getValue().equals("0")) {
			set_ingresos_activos.getTab_seleccion().setSql(ser_activos.getIngresosConBienesSinEntregar());
			set_ingresos_activos.getTab_seleccion().ejecutarSql();
			set_ingresos_activos.dibujar();
			}else if(lis_activo.getValue().equals("1")){
				seleccionarActivos();
			}
		}
	}

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de activos del
	 * acta
	 */
	private void prepararSelectorActivo() {
		set_activos.setId("set_activos");
		set_activos.setSeleccionTabla(ser_activos.getActivosEnBodega(), "ide_afact");
		set_activos.getTab_seleccion().getColumna("cod_anterior_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("cod_anterior_afact").setLongitud(10);
		set_activos.getTab_seleccion().getColumna("identificador_activo").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("identificador_activo").setLongitud(10);
		set_activos.getTab_seleccion().getColumna("detalle_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("detalle_afact").setLongitud(15);
		set_activos.getTab_seleccion().getColumna("observaciones_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("observaciones_afact").setLongitud(15);
		set_activos.getTab_seleccion().getColumna("color_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("color_afact").setLongitud(10);
		set_activos.getTab_seleccion().getColumna("marca_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("marca_afact").setLongitud(6);
		set_activos.getTab_seleccion().getColumna("serie_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("serie_afact").setLongitud(6);
		set_activos.getTab_seleccion().getColumna("modelo_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("modelo_afact").setLongitud(6);
		set_activos.getTab_seleccion().getColumna("chasis_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("chasis_afact").setLongitud(6);
		set_activos.getTab_seleccion().getColumna("motor_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("motor_afact").setLongitud(6);
		set_activos.setTitle("Seleccione uno o varios Activos");
		set_activos.getBot_aceptar().setMetodo("aceptarActivos");
		agregarComponente(set_activos);
	}
	

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de activos del
	 * acta
	 */
	private void prepararSelectorIngresos() {
		set_ingresos_activos.setId("set_ingresos_activos");
		set_ingresos_activos.setSeleccionTabla(ser_activos.getIngresosConBienesSinEntregar(), "ide_bobod");

		set_ingresos_activos.getTab_seleccion().getColumna("identificador_ingreso").setFiltro(true);
		set_ingresos_activos.getTab_seleccion().getColumna("identificador_ingreso").setLongitud(6);
		set_ingresos_activos.getTab_seleccion().getColumna("ingreso_numero").setFiltro(true);
		set_ingresos_activos.getTab_seleccion().getColumna("ingreso_numero").setLongitud(6);
		
		set_ingresos_activos.setTitle("Seleccione uno o más ingresos que desea visualizar");
		set_ingresos_activos.getBot_aceptar().setMetodo("aceptarIngresosActivos");
		agregarComponente(set_ingresos_activos);
	}

	/**
	 * Funcion que permite la filtracion de las actas correspondientes a cada
	 * año
	 */
	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_cabecera.setCondicion("ide_geani=" + com_anio.getValue() + " and ide_aftidoc='" + tipoDeActa + "'");
			tab_cabecera.ejecutarSql();
			seleccionarDetalles();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}

	/**
	 * Funcion que permite verificar si se esta trabajando con un documento
	 * guardado o no... para poder asegurar que no se pueda editar
	 * 
	 * @return
	 */
	public boolean verificacionEdita() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return false;
		}
		if (tab_cabecera.getValorSeleccionado() == null) {
			tab_cabecera.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_cabecera.setValor("ide_gtemp", ide_gtempxx);
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.setValor("ide_geani", com_anio.getValue() + "");
		}
		if (tab_cabecera.getValorSeleccionado().equals("-1")) {
			if (tab_cabecera.getValor("fecha_afdoc") == null || tab_cabecera.getValor("fecha_afdoc").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese la Fecha del Acta");
				return false;
			}
			return true;
		} else {
			utilitario.agregarMensaje("Prohibición", "La presente pantalla no permite la modificación de los registros de las actas de Entrega/Recepción.");
			return false;
		}
	}

	/**
	 * Muestra el popup con la seleccion de custodios actualizando el query
	 */
	public void seleccionarCustodios() {
		if (verificacionEdita()) {
			set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadosActivos("true"));
			set_empleado.getTab_seleccion().ejecutarSql();
			set_empleado.dibujar();
		}
	}

	/**
	 * Muestra el popup con la seleccion de activos actualizando el query
	 */
	public void seleccionarActivos() {
		if (verificacionEdita()) {
			dia_bodega.cerrar();
			set_activos.getTab_seleccion().setSql(ser_activos.getActivosEnBodega());
			set_activos.getTab_seleccion().ejecutarSql();
			set_activos.dibujar();
		}
	}

	/**
	 * Muestra el popup con la seleccion de activos segun acta de ingreso
	 */
	public void seleccionarActivos(String ide_bobod) {
		if (verificacionEdita()) {
			dia_bodega.cerrar();
			set_ingresos_activos.cerrar();
			set_activos.getTab_seleccion().setSql(ser_activos.getActivosEnActaIngreso(ide_bobod));
			set_activos.getTab_seleccion().ejecutarSql();
			set_activos.dibujar();
		}
	}

	/**
	 * Muestra el popup con la seleccion de activos segun acta de ingreso
	 */
	public void seleccionarTipoNecesidad() {
		// TODO generar el popup para elegir entre acta o todos
		if (verificacionEdita()) {
			dia_bodega.dibujar();
		}
	}

	/**
	 * Prepara los botones y reportes que se van a mostrar en pantalla
	 */
	private void preparaBotones() {
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);

		// boton empleado
		Boton bot_empleado = new Boton();
		bot_empleado.setIcon("ui-user");
		bot_empleado.setValue("Agregar Custodios");
		bot_empleado.setMetodo("seleccionarCustodios");
		bar_botones.agregarBoton(bot_empleado);

		// boton activo
		Boton bot_sctivo = new Boton();
		bot_sctivo.setIcon("ui-user");
		bot_sctivo.setValue("Agregar Activos");
		bot_sctivo.setMetodo("seleccionarTipoNecesidad");
		bar_botones.agregarBoton(bot_sctivo);

		// boton editar observacion
		Boton bot_edit_obseravion = new Boton();
		bot_edit_obseravion.setIcon("ui-pencil");
		bot_edit_obseravion.setValue("Agregar/Editar Observación");
		bot_edit_obseravion.setMetodo("abrirObservacion");
		bar_botones.agregarBoton(bot_edit_obseravion);
	}

	/**
	 * Constructor
	 */
	public pre_acta_entrega_recepcion() {

		// set_det_tip_nomina.setId("set_det_tip_nomina");
		// set_det_tip_nomina.setTitle("Seleccion de Parametros");
		// set_det_tip_nomina.setSeleccionTabla("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit "
		// +
		// "FROM NRH_DETALLE_TIPO_NOMINA a " +
		// "INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
		// "inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin " +
		// "inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit","IDE_NRDTN");
		// set_det_tip_nomina.getBot_aceptar().setMetodo("aceptarTipoNomina");
		// set_det_tip_nomina.setDynamic(false);
		// agregarComponente(set_det_tip_nomina);

		par_secuencial_acta = utilitario.getVariable(parametroSecuencial);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		preparaCabecera();
		preparaDetalleCustodio();
		preparaDetalleActivo();
		prepararSelectorCustodio();
		prepararSelectorActivo();
		preparaBotones();
		prepararSelectorPreActivos();
		prepararSelectorIngresos();

		// Inicio Dialogo Editar Observacion
		dlg_observacion.setId("dlg_observacion");
		dlg_observacion.setTitle("AGREGAR / EDITAR OBSERVACIÓN");
		dlg_observacion.setHeight("20%");
		dlg_observacion.setWidth("30%");
		grid_observacion.setColumns(2);
		txt_observacion.setMaxlength(250);
		grid_observacion.getChildren().add(new Etiqueta("Observación:"));
		grid_observacion.getChildren().add(txt_observacion);
		dlg_observacion.getBot_aceptar().setMetodo("guardarObservacion");
		dlg_observacion.setDialogo(grid_observacion);
		agregarComponente(dlg_observacion);

		tab_cabecera.agregarRelacion(tab_detalle_custodio);
		tab_cabecera.agregarRelacion(tab_detalle_activo);

		PanelTabla panelCabecera = new PanelTabla();
		panelCabecera.setPanelTabla(tab_cabecera);

		PanelTabla panelDetalleCustodio = new PanelTabla();
		panelDetalleCustodio.setPanelTabla(tab_detalle_custodio);
		panelDetalleCustodio.setTitle("En esta sección se presentan el o los custodios a los cuales se les va a entregar los activos listados.");
		panelDetalleCustodio.setMensajeInfo("Custodios");

		PanelTabla panelDetalleActivo = new PanelTabla();
		panelDetalleActivo.setPanelTabla(tab_detalle_activo);
		panelDetalleActivo.setTitle("En esta sección se presentan el o los bienes que se van a hacer entrega al o los custodios listados.");
		panelDetalleActivo.setMensajeInfo("Bienes");

		Division div_cabeceraDetalle = new Division();
		Division div_detalles = new Division();
		div_detalles.dividir2(panelDetalleActivo, panelDetalleCustodio, "60%", "v");
		div_cabeceraDetalle.dividir2(panelCabecera, div_detalles, "40%", "h");
		agregarComponente(div_cabeceraDetalle);

	}

	/**
	 * Verifica si el valor se encuentra en el array
	 * 
	 * @param array
	 * @param valor
	 * @return
	 */
	private boolean enSeleccionado(String[] array, String valor) {
		for (String str : array) {
			if (str.trim().equals(valor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Permite sacar un valor encontrado dentro del array
	 * 
	 * @param array
	 * @param valor
	 * @return
	 */
	private String[] quitarDeArray(String[] array, String valor) {
		List<String> result = new ArrayList<String>();
		for (String item : array) {
			if (!valor.equals(item.trim())) {
				result.add(item.trim());
			}
		}
		return result.toString().replace("[", "").replace("]", "").split(",");
	}

	/**
	 * Permite actualizar los detalles cuando se navega
	 */
	public void seleccionarDetalles() {
		if (tab_cabecera.getValor("ide_afdoc") != null) {
			tab_detalle_custodio.setCondicion("ide_afdoc =" + tab_cabecera.getValor("ide_afdoc"));
			tab_detalle_custodio.ejecutarSql();
			tab_detalle_activo.setCondicion("ide_afdoc =" + tab_cabecera.getValor("ide_afdoc"));
			tab_detalle_activo.ejecutarSql();
		}
	}

	/**
	 * Procesa la selecci&oacute;n en el popup de custodios
	 */
	public void aceptarCustodios() {
		String str_seleccionado = set_empleado.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		if (arrray.length > 0) {
			int numFil = tab_detalle_custodio.getTotalFilas();
			for (int i = 0; i < numFil; i++) {
				tab_detalle_custodio.setFilaActual(i);
				if (enSeleccionado(arrray, tab_detalle_custodio.getValor("ide_gtemp"))) {
					arrray = quitarDeArray(arrray, tab_detalle_custodio.getValor("ide_gtemp"));
				}
			}

		}
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_custodio.insertar();
					tab_detalle_custodio.setValor("ide_gtemp", str.trim());
				}
			}
		}
		set_empleado.cerrar();
	}

	public void aceptarIngresosActivos(){
		String str_seleccionado = set_ingresos_activos.getSeleccionados();
		seleccionarActivos(str_seleccionado);
	}
	/**
	 * Procesa la selecci&oacute;n en el popup de activos
	 */
	public void aceptarActivos() {
		String str_seleccionado = set_activos.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		if (arrray.length > 0) {
			int numFil = tab_detalle_activo.getTotalFilas();
			for (int i = 0; i < numFil; i++) {
				tab_detalle_activo.setFilaActual(i);
				if (enSeleccionado(arrray, tab_detalle_activo.getValor("ide_afact"))) {
					arrray = quitarDeArray(arrray, tab_detalle_activo.getValor("ide_afact"));
				}
			}

		}
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_activo.insertar();
					tab_detalle_activo.setValor("ide_afact", str.trim());
					TablaGenerica tabCampos = ser_activos.getCampo(str.trim());
					tab_detalle_activo.setValor("ide_afubi", tabCampos.getValor("ide_afubi"));
					tab_detalle_activo.setValor("afi_ubicacion_otro", tabCampos.getValor("afi_ubicacion_otro"));
				}
			}
		}
		set_activos.cerrar();
	}

	/**
	 * Dibujar Reportes
	 */
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	/**
	 * Aceptar Reportes
	 */
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Acta Entrega/Recepción")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "ACTA ENTREGA RECEPCIÓN DE BIENES No. " + tab_cabecera.getValor("nro_secuencial_afdoc"));
				p_parametros.put("ide_afdoc", pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc")));
				p_parametros.put("pjefe_activos", utilitario.getVariable("p_jefe_activos_fijos"));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 */
	@Override
	public void insertar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (tab_cabecera.isFocus()) {
			tab_cabecera.isFocus();
			tab_cabecera.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_cabecera.setValor("ide_gtemp", ide_gtempxx);
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.setValor("ide_geani", com_anio.getValue() + "");
		}
		if (!verificacionEdita()) {
			return;
		}
		if (tab_detalle_activo.isFocus()) {
			seleccionarActivos();
		}
		if (tab_detalle_custodio.isFocus()) {
			seleccionarCustodios();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#guardar()
	 */
	@Override
	public void guardar() {
		if (verificacionEdita()) {
			int numFilDA = tab_detalle_activo.getTotalFilas();
			int numFilCU = tab_detalle_custodio.getTotalFilas();
			if (numFilCU <= 0) {
				utilitario.agregarMensajeError("Detalle de Custodios", "El acta debe tener al menos un custodio");
				return;
			}
			if (numFilDA <= 0) {
				utilitario.agregarMensajeError("Detalle de Activos", "El acta debe tener al menos un activo");
				return;
			}
			// TODO quitar el comentario cuando ya no esten haciendo manual
			// tab_cabecera.setValor("nro_secuencial_afdoc",
			// ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.guardar();
			tab_detalle_activo.guardar();
			tab_detalle_custodio.guardar();
			guardarPantalla();
			ser_contabilidad.guardaSecuencial(tab_cabecera.getValor("nro_secuencial_afdoc"), par_secuencial_acta);
			actualizaActaActivos();
		}
	}

	/**
	 * Funci&oacute;n que permite actualizar a los bienes su ultima acta de
	 * custodios.
	 */
	public void actualizaActaActivos() {
		int acta = pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc"));
		int numFil = tab_detalle_activo.getTotalFilas();
		// Ticket #684922
		TablaGenerica area = ser_activos.getAreaActa(tab_cabecera.getValor("ide_afdoc"));
		String ide_geare = "null";
		if (area.getTotalFilas() > 0) {
			area.setFilaActual(0);
			ide_geare = area.getValor("ide_geare");
		}
		// ide_geare="+ide_geare+",

		for (int i = 0; i < numFil; i++) {
			tab_detalle_activo.setFilaActual(i);
			utilitario.getConexion().ejecutarSql(
					"UPDATE  afi_activo SET ide_geare=" + ide_geare + ",  ide_afubi=" + pckUtilidades.CConversion.CInt(tab_detalle_activo.getValor("ide_afubi")) + ",afi_ubicacion_otro='" + tab_detalle_activo.getValor("afi_ubicacion_otro")
							+ "',  afi_ultima_acta=" + acta + " WHERE ide_afact=" + pckUtilidades.CConversion.CInt(tab_detalle_activo.getValor("ide_afact")));
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#eliminar()
	 */
	@Override
	public void eliminar() {
		if (verificacionEdita()) {
			if (tab_detalle_activo.isFocus()) {
				tab_detalle_activo.eliminar();
			}
			if (tab_detalle_custodio.isFocus()) {
				tab_detalle_activo.eliminar();
			}
		}
	}

	@Override
	public void aceptarBuscar() {
		super.aceptarBuscar();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void actualizar() {
		super.actualizar();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void inicio() {
		super.inicio();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void siguiente() {
		super.siguiente();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void fin() {
		super.fin();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void atras() {
		super.atras();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	/**
	 * @return the tab_cabecera
	 */
	public Tabla getTab_cabecera() {
		return tab_cabecera;
	}

	/**
	 * @param tab_cabecera
	 *            the tab_cabecera to set
	 */
	public void setTab_cabecera(Tabla tab_cabecera) {
		this.tab_cabecera = tab_cabecera;
	}

	/**
	 * @return the tab_detalle_custodio
	 */
	public Tabla getTab_detalle_custodio() {
		return tab_detalle_custodio;
	}

	/**
	 * @param tab_detalle_custodio
	 *            the tab_detalle_custodio to set
	 */
	public void setTab_detalle_custodio(Tabla tab_detalle_custodio) {
		this.tab_detalle_custodio = tab_detalle_custodio;
	}

	/**
	 * @return the tab_detalle_activo
	 */
	public Tabla getTab_detalle_activo() {
		return tab_detalle_activo;
	}

	/**
	 * @param tab_detalle_activo
	 *            the tab_detalle_activo to set
	 */
	public void setTab_detalle_activo(Tabla tab_detalle_activo) {
		this.tab_detalle_activo = tab_detalle_activo;
	}

	/**
	 * @return the set_empleado
	 */
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	/**
	 * @param set_empleado
	 *            the set_empleado to set
	 */
	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	/**
	 * @return the set_activos
	 */
	public SeleccionTabla getSet_activos() {
		return set_activos;
	}

	/**
	 * @param set_activos
	 *            the set_activos to set
	 */
	public void setSet_activos(SeleccionTabla set_activos) {
		this.set_activos = set_activos;
	}

	
	/**
	 * @return the set_ingresos_activos
	 */
	public SeleccionTabla getSet_ingresos_activos() {
		return set_ingresos_activos;
	}

	/**
	 * @param set_ingresos_activos the set_ingresos_activos to set
	 */
	public void setSet_ingresos_activos(SeleccionTabla set_ingresos_activos) {
		this.set_ingresos_activos = set_ingresos_activos;
	}

	/**
	 * @return the lis_activo
	 */
	public Radio getLis_activo() {
		return lis_activo;
	}

	/**
	 * @param lis_activo the lis_activo to set
	 */
	public void setLis_activo(Radio lis_activo) {
		this.lis_activo = lis_activo;
	}

	/**
	 * @return the dia_bodega
	 */
	public Dialogo getDia_bodega() {
		return dia_bodega;
	}

	/**
	 * @param dia_bodega the dia_bodega to set
	 */
	public void setDia_bodega(Dialogo dia_bodega) {
		this.dia_bodega = dia_bodega;
	}

	/**
	 * @return the par_secuencial_acta
	 */
	public static String getPar_secuencial_acta() {
		return par_secuencial_acta;
	}

	/**
	 * @param par_secuencial_acta the par_secuencial_acta to set
	 */
	public static void setPar_secuencial_acta(String par_secuencial_acta) {
		pre_acta_entrega_recepcion.par_secuencial_acta = par_secuencial_acta;
	}

	/**
	 * @return the dlg_observacion
	 */
	public Dialogo getDlg_observacion() {
		return dlg_observacion;
	}

	/**
	 * @param dlg_observacion the dlg_observacion to set
	 */
	public void setDlg_observacion(Dialogo dlg_observacion) {
		this.dlg_observacion = dlg_observacion;
	}

	/**
	 * @return the grid_observacion
	 */
	public Grid getGrid_observacion() {
		return grid_observacion;
	}

	/**
	 * @param grid_observacion the grid_observacion to set
	 */
	public void setGrid_observacion(Grid grid_observacion) {
		this.grid_observacion = grid_observacion;
	}

	/**
	 * @return the txt_observacion
	 */
	public AreaTexto getTxt_observacion() {
		return txt_observacion;
	}

	/**
	 * @param txt_observacion the txt_observacion to set
	 */
	public void setTxt_observacion(AreaTexto txt_observacion) {
		this.txt_observacion = txt_observacion;
	}

	/**
	 * @return the com_anio
	 */
	public Combo getCom_anio() {
		return com_anio;
	}

	/**
	 * @param com_anio
	 *            the com_anio to set
	 */
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	/**
	 * @return the parametroSecuencial
	 */
	public String getParametroSecuencial() {
		return parametroSecuencial;
	}

	/**
	 * @param parametroSecuencial
	 *            the parametroSecuencial to set
	 */
	public void setParametroSecuencial(String parametroSecuencial) {
		this.parametroSecuencial = parametroSecuencial;
	}

	/**
	 * @return the tipoDeActa
	 */
	public String getTipoDeActa() {
		return tipoDeActa;
	}

	/**
	 * @param tipoDeActa
	 *            the tipoDeActa to set
	 */
	public void setTipoDeActa(String tipoDeActa) {
		this.tipoDeActa = tipoDeActa;
	}

	/**
	 * @return the ser_nomina
	 */
	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	/**
	 * @param ser_nomina
	 *            the ser_nomina to set
	 */
	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	/**
	 * @return the ser_contabilidad
	 */
	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	/**
	 * @param ser_contabilidad
	 *            the ser_contabilidad to set
	 */
	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	/**
	 * @return the ser_seguridad
	 */
	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	/**
	 * @param ser_seguridad
	 *            the ser_seguridad to set
	 */
	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	/**
	 * @return the rep_reporte
	 */
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	/**
	 * @param rep_reporte
	 *            the rep_reporte to set
	 */
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	/**
	 * @return the self_reporte
	 */
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	/**
	 * @param self_reporte
	 *            the self_reporte to set
	 */
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	/**
	 * @return the ser_activos
	 */
	public ServicioActivos getSer_activos() {
		return ser_activos;
	}

	/**
	 * @param ser_activos
	 *            the ser_activos to set
	 */
	public void setSer_activos(ServicioActivos ser_activos) {
		this.ser_activos = ser_activos;
	}

	/**
	 * @return the p_parametros
	 */
	public Map getP_parametros() {
		return p_parametros;
	}

	/**
	 * @param p_parametros
	 *            the p_parametros to set
	 */
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

}
