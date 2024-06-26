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

public class pre_carga_inicial_existencia extends Pantalla {


	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();

	

	//public static String par_sec_ingreso;

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

	public pre_carga_inicial_existencia() {

		// bar_botones.agregarReporte();
		System.out.println("pre_carga_inicial_existencia");
		
	
		tab_tabla.setId("tab_tabla");
		tab_tabla.setCondicion("ide_boinv=-1");
		tab_tabla.setTabla("bodt_bodega_inventario", "ide_boinv", 1);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion",
				"ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setLectura(true);
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla.getColumna("ide_bocam").setCombo(ser_bodega.getCalalogoExistencias1());
		tab_tabla.getColumna("ide_bocam").setRequerida(true);
		tab_tabla.getColumna("ide_bocam").setFiltroContenido();

		tab_tabla.getColumna("ide_geani").setLectura(true);
		
		tab_tabla.getColumna("descripcion_boinv").setLectura(true);
		tab_tabla.getColumna("descripcion_boinv").setVisible(false);
		
		tab_tabla.getColumna("activo_boinv").setLectura(true);
		tab_tabla.getColumna("activo_boinv").setVisible(false);

		tab_tabla.getColumna("cantidad_ingreso_boinv").setLectura(true);
		tab_tabla.getColumna("costo_ingreso_boinv").setLectura(true);
		tab_tabla.getColumna("cantidad_egreso_boinv").setLectura(true);
		tab_tabla.getColumna("costo_egreso_boinv").setLectura(true);
		tab_tabla.getColumna("cantidad_saldo_boinv").setLectura(true);
		tab_tabla.getColumna("costo_saldo_boinv").setLectura(true);
		
		tab_tabla.getColumna("costo_inicial_inc_iva_boinv").setLectura(true);
		tab_tabla.getColumna("costo_ingreso_inc_iva_boinv").setLectura(true);
		tab_tabla.getColumna("costo_egreso_inc_iva_boinv").setLectura(true);
		tab_tabla.getColumna("costo_saldo_inc_iva_boinv").setLectura(true);
		
		tab_tabla.getColumna("cantidad_inicial_boinv").setMetodoChange("calcular");
		tab_tabla.getColumna("costo_inicial_boinv").setMetodoChange("calcular");
		

		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("PMP");
		tab_tabla_detalle.setTabla("bodt_inventario_resumen", "ide_inres", 2);
		tab_tabla_detalle.setLectura(true);
		tab_tabla_detalle.setCondicion("ide_inres=-1");
		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		// System.out.println(tab_tabla_detalle.getColumna("ide_bocam").getSqlCombo());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		// tab_tabla_detalle.getColumna("ide_bocam").setMetodoChange("cargarCatalogo");

		tab_tabla_detalle.getColumna("ide_geani").setLectura(true);
		
		tab_tabla_detalle.getColumna("costo_medio_unidad_inres").setLectura(true);
		
		
		

		//tab_tabla.setTipoFormulario(true);
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
	
	
	public void calcular (AjaxBehaviorEvent event) {
		tab_tabla.modificar(event);
		System.out.println("calcular");
		try {
			double cantidad= pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("cantidad_inicial_boinv").toString());
			double valor_unitario= pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("costo_inicial_boinv").toString());

			
			//Boolean aplica_iva = Boolean.valueOf(tab_tabla_detalle.getValor("aplica_iva_inegd"));
			//tab_tabla_detalle.setValor("subtotal_inegd", Double.valueOf(cantidad * valor_unitario).toString());
			
			double porcenjate_iva = pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva"));
			
			tab_tabla.setValor("costo_inicial_inc_iva_boinv", String.valueOf(pckUtilidades.CConversion.CDbl_2(valor_unitario * (porcenjate_iva + 1))));
			
			
			
			
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
		
		utilitario.addUpdate("tab_tabla");
		
		
		
	}


	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue()
					+ " AND ide_boubi=" + com_bodega.getValue());
			tab_tabla.ejecutarSql();

			tab_tabla_detalle.setCondicion("ide_geani=" + com_anio.getValue());
			tab_tabla_detalle.ejecutarSql();
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

			tab_tabla.setValor("ide_geani", com_anio.getValue() + "");
			tab_tabla.setValor("ide_boubi", com_bodega.getValue() + "");
			tab_tabla.setValor("activo_boinv", Boolean.TRUE.toString());
			tab_tabla.setValor("cantidad_inicial_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_inicial_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_inicial_inc_iva_boinv", Double.valueOf(0).toString());
			
			tab_tabla.setValor("cantidad_ingreso_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_ingreso_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_ingreso_inc_iva_boinv", Double.valueOf(0).toString());
			
			tab_tabla.setValor("cantidad_egreso_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_egreso_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_egreso_inc_iva_boinv", Double.valueOf(0).toString());
			
			tab_tabla.setValor("cantidad_saldo_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_saldo_boinv", Double.valueOf(0).toString());
			tab_tabla.setValor("costo_saldo_inc_iva_boinv", Double.valueOf(0).toString());

			
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
	
	public void actualizarInventario() {
		String ide_geani =  com_anio.getValue().toString();
		
		String ide_boubi =  com_bodega.getValue().toString();
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			String ide_bocam =  tab_tabla.getValor(i, "ide_bocam");
			
			ser_bodega.triggerIngreso(ide_geani, ide_bocam, ide_boubi, true);
		}
		
		tab_tabla.ejecutarSql();
		tab_tabla_detalle.ejecutarSql();
		
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
		/*if(tab_tabla.isFilaInsertada()){
			//ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_ingreso), par_sec_ingreso);
		}
		if (tab_tabla.guardar()) {
			if (tab_tabla_detalle.guardar()) {
				guardarPantalla();
				actualizarInventario();
			}
		}*/
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
		//tab_tabla.eliminar();
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
	

}
