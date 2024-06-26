package paq_bodega;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

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
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_ingreso_existencia_sin_factura extends Pantalla {


	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();

	
	
	private Map<String, Object> p_parametros = new HashMap<String, Object>();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();

	public static String par_sec_ingreso;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario
			.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario
			.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario
			.instanciarEJB(ServicioNomina.class);
	
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_ingreso_existencia_sin_factura() {
		System.out.println("pre_ingreso_existencia_sin_factura");
		bar_botones.agregarReporte();
		
		par_sec_ingreso = utilitario.getVariable("p_modulo_sec_bodega_ingreso_existencia");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		
		tab_tabla.setId("tab_tabla");
		tab_tabla.setCondicion("ide_ingeg=-1 AND ide_adfac IS NULL");
		tab_tabla.setTabla("bodt_ingreso_egreso", "ide_ingeg", 1);
		tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion",
				"ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setLectura(true);
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla.getColumna("ide_inttr").setCombo(
				"bodt_inventario_tipo_transaccion", "ide_inttr",
				"detalle_inttr", "");

		tab_tabla.getColumna("ide_inttr").setLectura(true);
		tab_tabla.getColumna("fecha_ingeg").setLectura(true);

		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_solicitante").setVisible(false);
		tab_tabla.getColumna("ide_gtemp_jefe_solicitante").setVisible(false);
		// Factura

		tab_tabla.getColumna("ide_adfac").setLectura(true);
		tab_tabla.getColumna("ide_adfac").setNombreVisual("FACTURA");
		tab_tabla.getColumna("ide_gtemp").setCombo(
				ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp").setLectura(true);
		tab_tabla.getColumna("ide_gtemp").setNombreVisual("EMPLEADO RESPONSABLE");
		
		tab_tabla.getColumna("ide_prcer").setCombo(ser_presupuesto.getCertificacion("true"));
		tab_tabla.getColumna("ide_prcer").setAutoCompletar();
		tab_tabla.getColumna("ide_prcer").setLectura(true);
		tab_tabla.getColumna("ide_prcer").setNombreVisual("NRO CERTIFICACION");
		
		tab_tabla.getColumna("numero_documento_ingeg").setLectura(true);

		tab_tabla.getColumna("subtotal_ingeg").setLectura(true);
		tab_tabla.getColumna("subtotal_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("subtotal_ingeg").setEtiqueta();
		
		tab_tabla.getColumna("valor_iva_ingeg").setLectura(true);
		tab_tabla.getColumna("valor_iva_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("valor_iva_ingeg").setEtiqueta();
		
		tab_tabla.getColumna("total_ingeg").setLectura(true);
		tab_tabla.getColumna("total_ingeg").setEstilo(ESTILO_ETIQUETA);
		tab_tabla.getColumna("total_ingeg").setEtiqueta();
		
		tab_tabla.getColumna("ide_adfac_antigua").setVisible(false);
		tab_tabla.getColumna("ide_boubi_transferencia").setVisible(false);
		tab_tabla.getColumna("ide_ingeg_ref").setVisible(false);

		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("DETALLE DEL INGRESO");
		tab_tabla_detalle.setTabla("bodt_ingreso_egreso_det", "ide_inegd", 2);

		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		tab_tabla_detalle.getColumna("ide_bocam").setMetodoChange("cargarCatalogo");
		tab_tabla_detalle.getColumna("ide_inttr").setLectura(true);
		tab_tabla_detalle.getColumna("ide_inttr").setVisible(false);
		tab_tabla_detalle.getColumna("ide_addef").setLectura(true);
		tab_tabla_detalle.getColumna("ide_addef").setVisible(false);
		// tab_tabla_detalle.getColumna("cantidad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("subtotal_inegd").setLectura(true);
		// tab_tabla_detalle.getColumna("costo_unitario_inegd").setLectura(true);
		// tab_tabla_detalle.getColumna("aplica_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("total_inegd").setLectura(true);
		
		tab_tabla_detalle.getColumna("cantidad_inegd").setMetodoChange("calcular");
		tab_tabla_detalle.getColumna("cantidad_inegd").setRequerida(true);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setMetodoChange("calcular");
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setRequerida(true);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setMetodoChange("calcular");
		
		tab_tabla_detalle.getColumna("marca_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("modelo_inegd").setLongitud(20);
		tab_tabla_detalle.getColumna("color_inegd").setLongitud(20);
		
		tab_tabla_detalle.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_tabla_detalle.getColumna("ide_bounm").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm").setLongitud(20);
		//tab_tabla_detalle.getColumna("ide_bounm").setLectura(true);
		
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLongitud(20);
		
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setCombo(ser_bodega.getMedidas());
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLongitud(20);
		// tab_tabla_detalle.getColumna("ide_bounm_presentacion").setLectura(true);
		
		tab_tabla_detalle.getColumna("peligro_salud_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_inflamabilidad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("peligro_reactividad_inegd").setLectura(true);
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setLectura(true);
		
		tab_tabla_detalle.getColumna("ide_afest").setCombo("afi_estado", "ide_afest", "detalle_afest", "activo_afest=true");
		tab_tabla_detalle.getColumna("ide_afest").setLongitud(15);
		
		tab_tabla_detalle.getColumna("saldo_disponible_inegd").setVisible(false);
		
		tab_tabla.getColumna("ide_inttr").setNombreVisual("TRANSACCIÓN");
		tab_tabla.getColumna("ide_adfac").setNombreVisual("FACTURA");
		tab_tabla.getColumna("ide_adfac_antigua").setNombreVisual("FACTURA ANTIGUA");
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");
		tab_tabla.getColumna("ide_gtemp").setNombreVisual("EMPLEADO RESPONSABLE");
		tab_tabla.getColumna("ide_prcer").setNombreVisual("NRO CERTIFICACION");
		tab_tabla.getColumna("fecha_ingeg").setNombreVisual("FECHA");
		tab_tabla.getColumna("subtotal_ingeg").setNombreVisual("SUBTOTAL");
		tab_tabla.getColumna("valor_iva_ingeg").setNombreVisual("VALOR IVA");
		tab_tabla.getColumna("total_ingeg").setNombreVisual("TOTAL");
		tab_tabla.getColumna("activo_ingeg").setNombreVisual("ACTIVO");
		tab_tabla.getColumna("numero_documento_ingeg").setNombreVisual("NÚMERO DOCUMENTO");
		tab_tabla.getColumna("observacion_ingeg").setNombreVisual("OBSERVACIÓN");
		tab_tabla.getColumna("numero_proceso_ingeg").setNombreVisual("NÚMERO DE PROCESO");
		
		tab_tabla_detalle.getColumna("ide_bocam").setNombreVisual("NOMBRE CATALOGO");
		tab_tabla_detalle.getColumna("cantidad_inegd").setNombreVisual("CANTIDAD");
		tab_tabla_detalle.getColumna("subtotal_inegd").setNombreVisual("SUBTOTAL");
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setNombreVisual("COSTO UNITARIO");
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setNombreVisual("COSTO UNITARIO INC IVA");
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setNombreVisual("APLICA IVA");
		tab_tabla_detalle.getColumna("valor_iva_inegd").setNombreVisual("VALOR IVA");
		tab_tabla_detalle.getColumna("total_inegd").setNombreVisual("TOTAL");
		tab_tabla_detalle.getColumna("marca_inegd").setNombreVisual("MARCA");
		tab_tabla_detalle.getColumna("modelo_inegd").setNombreVisual("MODELO");
		tab_tabla_detalle.getColumna("color_inegd").setNombreVisual("COLOR");
		tab_tabla_detalle.getColumna("peligro_salud_inegd").setNombreVisual("PELIGRO SALUD");
		tab_tabla_detalle.getColumna("peligro_inflamabilidad_inegd").setNombreVisual("PELIGRO INFLAMABILIDAD");
		tab_tabla_detalle.getColumna("peligro_reactividad_inegd").setNombreVisual("PELIGRO REACTIVIDAD");
		tab_tabla_detalle.getColumna("manejo_especial_inegd").setNombreVisual("MANEJO ESPECIAL");
		tab_tabla_detalle.getColumna("ide_bounm").setNombreVisual("UNIDAD MEDIDA");
		tab_tabla_detalle.getColumna("ide_bounm_presentacion").setNombreVisual("UNIDAD PRESENTACION");
		tab_tabla_detalle.getColumna("ide_afest").setNombreVisual("ESTADO");
		
		tab_tabla_detalle.getColumna("ide_inegd").setOrden(1);
		tab_tabla_detalle.getColumna("ide_bocam").setOrden(2);
		tab_tabla_detalle.getColumna("cantidad_inegd").setOrden(3);
		tab_tabla_detalle.getColumna("costo_unitario_inegd").setOrden(4);
		tab_tabla_detalle.getColumna("costo_unitario_inc_iva_inegd").setOrden(5);
		tab_tabla_detalle.getColumna("aplica_iva_inegd").setOrden(6);
		tab_tabla_detalle.getColumna("valor_iva_inegd").setOrden(7);
		tab_tabla_detalle.getColumna("subtotal_inegd").setOrden(8);
		tab_tabla_detalle.getColumna("total_inegd").setOrden(9);
		
		
		tab_tabla.agregarRelacion(tab_tabla_detalle);

		tab_tabla.setTipoFormulario(true);
		tab_tabla.dibujar();
		tab_tabla_detalle.dibujar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		com_bodega.setCombo(ser_bodega.getBodegas());
		com_bodega.setMetodo("seleccionaParametros");
		com_bodega.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarComponente(new Etiqueta("BODEGA:"));
		bar_botones.agregarComponente(com_bodega);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_tabla_detalle);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);


	}
	
	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {

		System.out.println("Reporte seleccionado: "+ rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Comprobante ingreso")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "INGRESO DE EXISTENCIAS No. " + tab_tabla.getValor("numero_documento_ingeg"));
				p_parametros.put("ide_ingeg", pckUtilidades.CConversion.CInt(tab_tabla.getValor("ide_ingeg")));
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				
				//p_parametros.put("num_ingreso", pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")));
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} 
	}
	
	public void cargarCatalogo(AjaxBehaviorEvent  event) {
		tab_tabla_detalle.modificar(event);
		
		String ide_bocam = tab_tabla_detalle.getValor("ide_bocam");
		System.out.println("selected ide_bocam:" + ide_bocam);
		TablaGenerica tg = ser_bodega.getCatalogoPorId(tab_tabla_detalle.getValor("ide_bocam"));
		
		String vidaUtil = tg.getValor("vida_util_bocam");
		String fechaVencimiento = tab_tabla_detalle.getValor("fecha_vencimiento_inegd");
		
		if (!vidaUtil.isEmpty() && !vidaUtil.equals("null") && !vidaUtil.equals("0.0000")) {
			if (fechaVencimiento == null || fechaVencimiento == "null" || fechaVencimiento.isEmpty()) {
				Date fechaActual = utilitario.getFecha(utilitario.getFechaActual());
				Calendar cal = Calendar.getInstance();
				cal.setTime(fechaActual);
				Double vida = Double.parseDouble(vidaUtil);
				Integer vidaInt = vida.intValue();
				if (vidaInt > 0) {
					cal.add(Calendar.DAY_OF_MONTH, vidaInt);
					SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
					String nuevaFecha = formatoFecha.format(cal.getTime());
					tab_tabla_detalle.setValor("fecha_vencimiento_inegd", nuevaFecha);
				}
			}
		}
		tab_tabla_detalle.setValor("ide_bounm", tg.getValor("ide_bounm"));
		System.out.println("ide_bocam:"+tg.getValor("ide_bounm"));
		tab_tabla_detalle.setValor("ide_bounm_presentacion", tg.getValor("ide_bounm_presentacion"));
		tab_tabla_detalle.setValor("peligro_salud_inegd", tg.getValor("peligro_salud_bocam"));
		tab_tabla_detalle.setValor("peligro_inflamabilidad_inegd", tg.getValor("peligro_inflamabilidad_bocam"));
		tab_tabla_detalle.setValor("peligro_reactividad_inegd", tg.getValor("peligro_reactividad_bocam"));
		tab_tabla_detalle.setValor("manejo_especial_inegd", tg.getValor("manejo_especial_bocam"));
		
		TablaGenerica tg_resumen = ser_bodega.getInventarioResumen(com_anio.getValue().toString(), tab_tabla_detalle.getValor("ide_bocam"));
		if (tg_resumen.getValor("costo_medio_unidad_inres") != tg_resumen.getValor("costo_medio_unidad_inc_iva_inres")) {
			System.out.println("aplica iva");
			tab_tabla_detalle.setValor("aplica_iva_inegd", "true");
		} else {
			System.out.println("no aplica iva");
			tab_tabla_detalle.setValor("aplica_iva_inegd", "false");
		}
		
		utilitario.addUpdate("tab_tabla_detalle");

	}
	
	public void calcular (AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);
		System.out.println("calcular");
		try {
			double cantidad= Double.parseDouble(tab_tabla_detalle.getValor("cantidad_inegd").toString());
			double valor_unitario= Double.parseDouble(tab_tabla_detalle.getValor("costo_unitario_inegd").toString());
			// double valor_unitario_inc_iva = valor_unitario;
			
			Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor("aplica_iva_inegd"));
			tab_tabla_detalle.setValor("subtotal_inegd", Double.valueOf(cantidad * valor_unitario).toString());
			
			double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));
			if (aplica_iva) {
				tab_tabla_detalle.setValor("valor_iva_inegd", String.valueOf(cantidad * valor_unitario * porcenjate_iva));
				tab_tabla_detalle.setValor("total_inegd", String.valueOf(cantidad * valor_unitario * (porcenjate_iva + 1)));
				tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd", String.valueOf(valor_unitario * (porcenjate_iva + 1)));
			}else {
				tab_tabla_detalle.setValor("valor_iva_inegd", String.valueOf(0));
				tab_tabla_detalle.setValor("total_inegd", String.valueOf(cantidad * valor_unitario ));
				tab_tabla_detalle.setValor("costo_unitario_inc_iva_inegd", String.valueOf(valor_unitario));
			}
			
			
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
		
		utilitario.addUpdate("tab_tabla_detalle");
		
		Double valor_iva= (double) 0;
		Double subtotal= (double) 0;
		Double total= (double) 0;
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			
			try {
				double cantidad= Double.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_inegd").toString());
				double valor_unitario= Double.parseDouble(tab_tabla_detalle.getValor(i, "costo_unitario_inegd").toString());
				
				Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor(i, "aplica_iva_inegd"));
				double porcenjate_iva = Double.valueOf(utilitario.getVariable("p_valor_iva"));
				
				subtotal += cantidad * valor_unitario;
								
				if (aplica_iva) {
					valor_iva += cantidad * valor_unitario * porcenjate_iva;
				}

			} catch (NumberFormatException e) {
				System.out.println("Error:" + e.getMessage());
			}
		}
		
		total = subtotal + valor_iva;
		tab_tabla.setValor("subtotal_ingeg", subtotal.toString());
		tab_tabla.setValor("valor_iva_ingeg", valor_iva.toString());
		tab_tabla.setValor("total_ingeg", total.toString());
		
		utilitario.addUpdate("tab_tabla");
	}


	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue()
					+ " AND ide_boubi=" + com_bodega.getValue()
					+ " AND ide_inttr=" + ser_bodega.TRANSACCION_INGRESO.toString()
					+ " AND ide_adfac IS NULL");
			tab_tabla.ejecutarSql();
			tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y bodega", "");
		}
	}

	@Override
	public void insertar() {

		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		
		if (tab_tabla.isFocus()) {
			tab_tabla.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");

			tab_tabla.setValor("numero_documento_ingeg",ser_contabilidad.numeroSecuencial(par_sec_ingreso));
			
			tab_tabla.setValor("ide_gtemp", ide_gtempxx);
			tab_tabla.setValor("ide_geani", com_anio.getValue() + "");
			tab_tabla.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla.setValor("ide_inttr", ser_bodega.TRANSACCION_INGRESO.toString());
			tab_tabla.setValor("activo_ingeg", Boolean.TRUE.toString());
			tab_tabla.setValor("fecha_ingeg", utilitario.getFechaActual());
			tab_tabla.setValor("subtotal_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("valor_iva_ingeg", Double.valueOf(0).toString());
			tab_tabla.setValor("total_ingeg", Double.valueOf(0).toString());
			
		}
		
		if (tab_tabla_detalle.isFocus()) {
			tab_tabla_detalle.insertar();
			tab_tabla_detalle.setValor("ide_inttr", ser_bodega.TRANSACCION_INGRESO.toString());
			tab_tabla_detalle.setValor("ide_afest", ser_bodega.ESTADO_PREDETERMINADO.toString());
		}
	}

	public boolean verificarSiExisteEnInventario (String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getTotalFilas() > 0;
	}
	
	public String obtenerItemInventarioPK (String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = ser_bodega.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);
		return tg.getValor("ide_boinv");
	}
	
	public void guardarInventario() {
		String ide_geani =  tab_tabla.getValor("ide_geani");
		
		String ide_boubi =  tab_tabla.getValor("ide_boubi");
		
		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			String ide_bocam =  tab_tabla_detalle.getValor(i, "ide_bocam");
			
			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, true);
		}
		
	}

	@Override
	public void guardar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		if (pckUtilidades.CConversion.CInt(com_anio.getValue().toString()) < 16 ) {
			utilitario.agregarMensajeInfo("Debe seleccionar el año 2023", "");
			return;
		}
		if(tab_tabla.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_ingreso), par_sec_ingreso);
		}
		if (tab_tabla.guardar()) {
			if (tab_tabla_detalle.guardar()) {
				guardarPantalla();
				guardarInventario();
			}
		}
	}

	@Override
	public void eliminar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_tabla_detalle() {
		return tab_tabla_detalle;
	}

	public void setTab_tabla_detalle(Tabla tab_tabla_detalle) {
		this.tab_tabla_detalle = tab_tabla_detalle;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Combo getCom_bodega() {
		return com_bodega;
	}

	public void setCom_bodega(Combo com_bodega) {
		this.com_bodega = com_bodega;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public ServicioPresupuesto getSer_presupuesto() {
		return ser_presupuesto;
	}

	public void setSer_presupuesto(ServicioPresupuesto ser_presupuesto) {
		this.ser_presupuesto = ser_presupuesto;
	}
	
	public Map<String, Object> getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map<String, Object> p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

}
