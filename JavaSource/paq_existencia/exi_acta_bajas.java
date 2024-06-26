/**
 * 
 */
package paq_existencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_existencia.ejb.ServicioExistencias;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import pckUtilidades.*;

/**
 * Pantalla que permite la generaci&oacute;n de actas de
 * entrega/recepci&oacute;n
 * 
 * @author ccaceres
 * @since 2017/05/16
 * 
 */
public class exi_acta_bajas extends Pantalla {

	private Tabla tab_cabecera = new Tabla();
	private Tabla tab_detalle_custodio = new Tabla();
	private Tabla tab_detalle_existencia = new Tabla();

	private SeleccionTabla set_empleado = new SeleccionTabla();
	private SeleccionTabla set_existencias = new SeleccionTabla();

	private Combo com_anio = new Combo();

	private String parametroSecuencial = "p_sec_acta_baja_existencia";
	private String tipoDeActa = "13";

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
	private ServicioExistencias ser_existencias = (ServicioExistencias) utilitario.instanciarEJB(ServicioExistencias.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	/**
	 * prepara la tabla que muestra la cabecera del acta
	 */
	private void preparaCabecera() {
		tab_cabecera = new Tabla();
		tab_cabecera.setId("tab_cabecera");
		tab_cabecera.setTabla("afi_docu", "ide_afdoc", 1);
		tab_cabecera.setCampoOrden("ide_afdoc desc");
		tab_cabecera.getColumna("ide_aftidoc").setCombo("SELECT ide_aftidoc, detalle_aftidoc  FROM afi_tipo_docu");
		tab_cabecera.getColumna("ide_aftidoc").setAutoCompletar();
		tab_cabecera.getColumna("ide_aftidoc").setValorDefecto(tipoDeActa);
		tab_cabecera.getColumna("ide_aftidoc").setLectura(true);
		tab_cabecera.getColumna("ide_geani").setVisible(false);
		tab_cabecera.getColumna("ide_boubi").setCombo(ser_bodega.getBodegas());
		tab_cabecera.getColumna("ide_boubi").setValorDefecto("1");
		tab_cabecera.getColumna("nro_secuencial_afdoc").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_acta));
		// tab_cabecera.getColumna("nro_secuencial_afdoc").setLectura(true);
		tab_cabecera.getColumna("fecha_afdoc").setValorDefecto(utilitario.getFechaActual());
		tab_cabecera.getColumna("activo_afdoc").setValorDefecto("true");
		tab_cabecera.getColumna("activo_afdoc").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
		tab_cabecera.getColumna("ide_gtemp").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setAutoCompletar();
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
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

		tab_detalle_custodio.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
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

	public double cantidadSobrante(String cant_sol, String ide_exist) {
		System.out.println("cantidadSobrante() --> cant_sol:" + cant_sol + ", ide_exist:" + ide_exist);
		double cant_sol_d = pckUtilidades.CConversion.CDbl_2(cant_sol);
		TablaGenerica tab_saldos = ser_existencias.getSaldoItem(ide_exist);
		double cant_exi = 0;
		cant_exi = pckUtilidades.CConversion.CDbl_2(tab_saldos.getValor("exist_cantidad_saldo"));
		System.out.println("cantidadSobrante() --> return:" + (cant_exi - cant_sol_d));
		return cant_exi - cant_sol_d;
	}

	public void validaSaldo() {
		double cant_sol = pckUtilidades.CConversion.CDbl_2(tab_detalle_existencia.getValor("exist_cantidad"));
		double cant_exi = 0;
		TablaGenerica tab_saldos = ser_existencias.getSaldoItem(tab_detalle_existencia.getValor("ide_exist"));
		if (tab_saldos.getTotalFilas() > 0) {
			cant_exi = pckUtilidades.CConversion.CDbl_2(tab_saldos.getValor("exist_cantidad_saldo"));
			// System.out.println("validaSaldo cant_sol:" + cant_sol +
			// "; cant_exi:" + cant_exi);
			if (cant_sol > cant_exi) {
				// System.out.println("Si es mayor");
				tab_detalle_existencia.setValor("exist_cantidad", Double.toString(cant_exi));
				utilitario.addUpdateTabla(tab_detalle_existencia, "exist_cantidad", "");
				utilitario.agregarMensaje("FALTA EXISTENCIA", "La cantidad existente es " + Double.toString(cant_exi) + " por lo cual no puede requerir " + Double.toString(cant_sol));
			}
		} else {
			utilitario.agregarMensaje("404", "NO SE ENCUENTRA EXISTENCIA");
		}
	}

	/**
	 * prepara la tabla que muestra el detalle de existencias del acta
	 */
	private void preparaDetalleExistencia() {
		tab_detalle_existencia.setId("tab_detalle_existencia");
		tab_detalle_existencia.setTabla("afi_doc_detalle_existencia", "ide_afdde", 3);

		tab_detalle_existencia.getColumna("ide_exist").setCombo(ser_existencias.getDetalleExistencia());
		tab_detalle_existencia.getColumna("ide_exist").setLectura(true);
		tab_detalle_existencia.getColumna("ide_exist").setAutoCompletar();

		tab_detalle_existencia.getColumna("ide_exist").setLongitud(20);

		tab_detalle_existencia.getColumna("activo_afdde").setValorDefecto("true");
		tab_detalle_existencia.getColumna("activo_afdde").setVisible(false);

		tab_detalle_existencia.getColumna("exist_cantidad").setLongitud(5);
		tab_detalle_existencia.getColumna("exist_cantidad").setValorDefecto("1");
		tab_detalle_existencia.getColumna("exist_cantidad").setMetodoChange("validaSaldo");

		tab_detalle_existencia.getColumna("ide_afdoc").setVisible(false);
		tab_detalle_existencia.setCondicion("ide_afdoc=-1");
		tab_detalle_existencia.setTipoFormulario(false);
		tab_detalle_existencia.dibujar();
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
	 * prepara la tabla que se muestra en el popup de seleccion de existencias
	 * del acta
	 */
	private void prepararSelectorExistencia() {
		set_existencias.setId("set_existencias");
		// TODO colocar el anio
		set_existencias.setSeleccionTabla(ser_existencias.getExistencias(tab_cabecera.getValor("ide_boubi")), "ide_exist");
		set_existencias.getTab_seleccion().getColumna("bodega").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("bodega").setLongitud(5);
		set_existencias.getTab_seleccion().getColumna("item_presupuestario").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("item_presupuestario").setLongitud(5);
		set_existencias.getTab_seleccion().getColumna("familia").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("familia").setLongitud(10);
		set_existencias.getTab_seleccion().getColumna("descripcion_bocam").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("descripcion_bocam").setLongitud(10);
		set_existencias.getTab_seleccion().getColumna("marca").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("marca").setLongitud(5);
		set_existencias.getTab_seleccion().getColumna("modelo").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("modelo").setLongitud(5);
		set_existencias.getTab_seleccion().getColumna("presentacion").setFiltro(true);
		set_existencias.getTab_seleccion().getColumna("presentacion").setLongitud(5);
		set_existencias.setTitle("Seleccione uno o varias Existencias");
		set_existencias.getBot_aceptar().setMetodo("aceptarExistencias");
		agregarComponente(set_existencias);
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
			if (tab_cabecera.getValor("ide_boubi") == null || tab_cabecera.getValor("ide_boubi").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese la bodega de la cual va a hacer el acta");
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
	 * Muestra el popup con la seleccion de existencias actualizando el query
	 */
	public void seleccionarExistencias() {
		if (verificacionEdita()) {
			set_existencias.getTab_seleccion().setSql(ser_existencias.getExistencias(tab_cabecera.getValor("ide_boubi")));
			set_existencias.getTab_seleccion().ejecutarSql();
			set_existencias.dibujar();
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

		// boton existencia
		Boton bot_sctivo = new Boton();
		bot_sctivo.setIcon("ui-user");
		bot_sctivo.setValue("Agregar Inventarios");
		bot_sctivo.setMetodo("seleccionarExistencias");
		bar_botones.agregarBoton(bot_sctivo);
	}

	/**
	 * Constructor
	 */
	public exi_acta_bajas() {
		par_secuencial_acta = utilitario.getVariable(parametroSecuencial);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		preparaCabecera();
		preparaDetalleCustodio();
		preparaDetalleExistencia();
		prepararSelectorCustodio();
		prepararSelectorExistencia();
		preparaBotones();

		tab_cabecera.agregarRelacion(tab_detalle_custodio);
		tab_cabecera.agregarRelacion(tab_detalle_existencia);

		PanelTabla panelCabecera = new PanelTabla();
		panelCabecera.setPanelTabla(tab_cabecera);

		PanelTabla panelDetalleCustodio = new PanelTabla();
		panelDetalleCustodio.setPanelTabla(tab_detalle_custodio);
		panelDetalleCustodio.setTitle("En esta sección se presentan el o los custodios a los cuales se les va a entregar las existencias listadas.");
		panelDetalleCustodio.setMensajeInfo("Custodios");

		PanelTabla panelDetalleExistencia = new PanelTabla();
		panelDetalleExistencia.setPanelTabla(tab_detalle_existencia);
		panelDetalleExistencia.setTitle("En esta sección se presentan las existencias que se van a hacer entrega al o los custodios listados.");
		panelDetalleExistencia.setMensajeInfo("Existencias");

		Division div_cabeceraDetalle = new Division();
		Division div_detalles = new Division();
		div_detalles.dividir2(panelDetalleExistencia, panelDetalleCustodio, "60%", "v");
		div_cabeceraDetalle.dividir2(panelCabecera, div_detalles, "35%", "h");
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
			tab_detalle_existencia.setCondicion("ide_afdoc =" + tab_cabecera.getValor("ide_afdoc"));
			tab_detalle_existencia.ejecutarSql();
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

	/**
	 * Procesa la selecci&oacute;n en el popup de existencias
	 */
	public void aceptarExistencias() {
		String str_seleccionado = set_existencias.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		if (arrray.length > 0) {
			int numFil = tab_detalle_existencia.getTotalFilas();
			for (int i = 0; i < numFil; i++) {
				tab_detalle_existencia.setFilaActual(i);
				if (enSeleccionado(arrray, tab_detalle_existencia.getValor("ide_exist"))) {
					arrray = quitarDeArray(arrray, tab_detalle_existencia.getValor("ide_exist"));
				}
			}
		}
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_existencia.insertar();
					tab_detalle_existencia.setValor("ide_exist", str.trim());
				}
			}
		}
		set_existencias.cerrar();
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
		if (rep_reporte.getReporteSelecionado().equals("Acta de Baja")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "ACTA DE BAJA DE EXISTENCIAS No. " + tab_cabecera.getValor("nro_secuencial_afdoc"));
				p_parametros.put("ide_afdoc", pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc")));
				p_parametros.put("pjefe_existencias", utilitario.getVariable("p_jefe_existencias_fijos"));
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
		if (tab_detalle_existencia.isFocus()) {
			seleccionarExistencias();
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
			int numFilDA = tab_detalle_existencia.getTotalFilas();
			int numFilCU = tab_detalle_custodio.getTotalFilas();
			if (numFilCU <= 0) {
				utilitario.agregarMensajeError("Detalle de Custodios", "El acta debe tener al menos un custodio");
				return;
			}
			if (numFilDA <= 0) {
				utilitario.agregarMensajeError("Detalle de Existencias", "El acta debe tener al menos una existencia");
				return;
			}
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.guardar();
			tab_detalle_existencia.guardar();
			tab_detalle_custodio.guardar();
			guardarPantalla();
			ser_contabilidad.guardaSecuencial(tab_cabecera.getValor("nro_secuencial_afdoc"), par_secuencial_acta);
			actualizaActaExistencias();
		}
	}

	/**
	 * Funci&oacute;n que permite actualizar a los bienes su ultima acta de
	 * custodios.
	 */
	public void actualizaActaExistencias() {
		int acta = pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc"));
		int numFil = tab_detalle_existencia.getTotalFilas();
		String ide_Exi = "";
		for (int i = 0; i < numFil; i++) {
			tab_detalle_existencia.setFilaActual(i);
			utilitario.getConexion().ejecutarSql(
					"UPDATE  bodt_existencia SET exist_cantidad_saldo=" + cantidadSobrante(tab_detalle_existencia.getValor("exist_cantidad"), tab_detalle_existencia.getValor("ide_exist")) + " WHERE ide_exist="
							+ pckUtilidades.CConversion.CInt(tab_detalle_existencia.getValor("ide_exist")));
			ide_Exi = ser_existencias.unidadPresentacion(tab_detalle_existencia.getValor("ide_exist"));
			inventariarItem(tab_cabecera.getValor("ide_boubi"), tab_detalle_existencia.getValor("ide_exist"), tab_cabecera.getValor("ide_geani"), ide_Exi, tab_detalle_existencia.getValor("exist_cantidad"));
		}
	}

	public void inventariarItem(String ide_boubi, String ide_exist, String ide_geani, String ide_bounm, String exist_cantidad) {
		System.out.println("inventariarItem() --> ide_boubi:" + ide_boubi + ", ide_exist:" + ide_exist + ", ide_geani:" + ide_geani + ", ide_bounm:" + ide_bounm + ", exist_cantidad:" + exist_cantidad);
		TablaGenerica tab_exis = utilitario.consultar("SELECT ide_exist, exist_cantidad, ide_boubi, ide_bocam, ide_geani FROM bodt_existencia WHERE ide_exist= " + ide_exist);

		String exi_ide_boubi = tab_exis.getValor("ide_boubi");
		String exi_ide_bocam = tab_exis.getValor("ide_bocam");
		String exi_exist_cantidad = tab_exis.getValor("exist_cantidad");
		String exi_ide_geani = tab_exis.getValor("ide_geani");

		TablaGenerica tab_bodega_stock = utilitario.consultar(ser_existencias.getSaldoItem(ide_boubi, exi_ide_bocam, ide_geani, ide_bounm));
		if (tab_bodega_stock.getTotalFilas() > 0) {// saldo_existencia_bosto
			double egreso = pckUtilidades.CConversion.CDbl(tab_bodega_stock.getValor("cantidad_egreso_bosto"));
			double saldo = pckUtilidades.CConversion.CDbl(tab_bodega_stock.getValor("saldo_existencia_bosto"));
			double cantidad = pckUtilidades.CConversion.CDbl(exist_cantidad);
			double egresototal = egreso + cantidad;
			double saltoTotal = saldo - cantidad;
			// TODO COMPROBAR

			utilitario.getConexion().ejecutarSql(ser_existencias.updateSaldoItem(ide_boubi, exi_ide_bocam, ide_geani, ide_bounm, Double.toString(egresototal), Double.toString(saltoTotal)));
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
			if (tab_detalle_existencia.isFocus()) {
				tab_detalle_existencia.eliminar();
			}
			if (tab_detalle_custodio.isFocus()) {
				tab_detalle_existencia.eliminar();
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
	 * @return the tab_detalle_existencia
	 */
	public Tabla getTab_detalle_existencia() {
		return tab_detalle_existencia;
	}

	/**
	 * @param tab_detalle_existencia
	 *            the tab_detalle_existencia to set
	 */
	public void setTab_detalle_existencia(Tabla tab_detalle_existencia) {
		this.tab_detalle_existencia = tab_detalle_existencia;
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
	 * @return the set_existencias
	 */
	public SeleccionTabla getSet_existencias() {
		return set_existencias;
	}

	/**
	 * @param set_existencias
	 *            the set_existencias to set
	 */
	public void setSet_existencias(SeleccionTabla set_existencias) {
		this.set_existencias = set_existencias;
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
	 * @return the ser_existencias
	 */
	public ServicioExistencias getSer_existencias() {
		return ser_existencias;
	}

	/**
	 * @param ser_existencias
	 *            the ser_existencias to set
	 */
	public void setSer_existencias(ServicioExistencias ser_existencias) {
		this.ser_existencias = ser_existencias;
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
