package paq_bodega;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_bodega_resumen extends Pantalla{
	
	private Tabla tab_tabla = new Tabla();

	
	// REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	
	private Combo com_anio=new Combo();
    private Combo com_mes=new Combo();
    
    @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_bodega_resumen(){
		System.out.println("pre_bodega_resumen");
		bar_botones.limpiar();
		
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
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		com_anio.setCombo(ser_contabilidad.getAnio("true,false","true,false"));		
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes.setCombo(ser_contabilidad.getMes("true"));	
		com_mes.setMetodo("seleccionaParametros");
		com_mes.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes:"));
		bar_botones.agregarComponente(com_mes);
		
		/*bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);*/
		

		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(ser_bodega.getResumenMensual("-1", "-1"));
		
		tab_tabla.getColumna("ide_geani").setVisible(false);
		tab_tabla.getColumna("ide_bocam").setVisible(false);
		tab_tabla.getColumna("padre_ide_bocam").setVisible(false);
		tab_tabla.getColumna("inicial_subtotal").setVisible(false);
		tab_tabla.getColumna("ingresos_subtotal").setVisible(false);
		tab_tabla.getColumna("egresos_subtotal").setVisible(false);
		tab_tabla.getColumna("saldo_subtotal").setVisible(false);
		
		tab_tabla.getColumna("cat_codigo_bocam").setNombreVisual("PARTIDA");
		tab_tabla.getColumna("padre_descripcion_bocam").setNombreVisual("FAMILIA");
		tab_tabla.getColumna("descripcion_bocam").setNombreVisual("CATÁLOGO");
		
		tab_tabla.getColumna("inicial_cantidad").setNombreVisual("INICIAL CANTIDAD");
		tab_tabla.getColumna("inicial_total").setNombreVisual("INICIAL TOTAL");
		tab_tabla.getColumna("ingresos_cantidad").setNombreVisual("INGRESOS CANTIDAD");
		tab_tabla.getColumna("ingresos_total").setNombreVisual("INGRESOS TOTAL");
		tab_tabla.getColumna("egresos_cantidad").setNombreVisual("EGRESOS CANTIDAD");
		tab_tabla.getColumna("egresos_total").setNombreVisual("EGRESOS TOTAL");
		tab_tabla.getColumna("saldo_cantidad").setNombreVisual("SALDO CANTIDAD");
		tab_tabla.getColumna("saldo_total").setNombreVisual("SALDO TOTAL");
		
		tab_tabla.getColumna("cat_codigo_bocam").setFiltroContenido();
		tab_tabla.getColumna("padre_descripcion_bocam").setFiltroContenido();
		tab_tabla.getColumna("descripcion_bocam").setFiltroContenido();
		
		
		/*tab_tabla.getColumna("ide_recli").setVisible(false);
		tab_tabla.getColumna("ruc_comercial_recli").setNombreVisual("RUC");
		tab_tabla.getColumna("ruc_comercial_recli").setLongitud(30);
		tab_tabla.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_tabla.getColumna("razon_social_recli").setNombreVisual("RAZON SOCIAL");
		tab_tabla.getColumna("razon_social_recli").setLongitud(150);
		tab_tabla.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_tabla.getColumna("secuencial_fafac").setFiltroContenido();
		tab_tabla.getColumna("comprobante_pago").setFiltroContenido();
		tab_tabla.getColumna("comprobante_aplicado").setFiltroContenido();
		tab_tabla.getColumna("detalle_bogrm").setLongitud(130);
		tab_tabla.getColumna("secuencial_fafac").setLongitud(30);
		tab_tabla.getColumna("comprobante_aplicado").setLongitud(30);
		tab_tabla.getColumna("detalle_coest").setNombreVisual("ESTADO");
		tab_tabla.getColumna("detalle_coest").setLongitud(20);
		tab_tabla.getColumna("detalle_retip").setNombreVisual("FORMA PAGO");
		tab_tabla.getColumna("detalle_retip").setLongitud(30);
		tab_tabla.setColumnaSuma("total,abono,saldo");*/
		tab_tabla.setRows(25);
		tab_tabla.setLectura(true);
		tab_tabla.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
	}
	
	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_mes.getValue() != null) {
			tab_tabla.setSql(ser_bodega.getResumenMensual(com_anio.getValue().toString(), com_mes.getValue().toString()));
			tab_tabla.ejecutarSql();
		} else {
			utilitario.agregarMensajeInfo("Selecione el año y mes", "");
		}
	}

	public void limpiar(){
		//aut_cliente.limpiar();
		tab_tabla.limpiar();
		
		tab_tabla.setSql(ser_tesoreria.comprobantesClientes("-1","1900-01-01","1900-01-01","false","false"));
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,aut_cliente,eti_devolucion");
	}

	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		System.out.println("rep_reporte.getReporteSelecionado(): " + rep_reporte.getReporteSelecionado());
		TablaGenerica tg_elaborado = ser_bodega.getTablaGenericaNombreEmpleadoPorIdUsuario(utilitario.getVariable("ide_usua")); 
		if (rep_reporte.getReporteSelecionado().equals("Resumen General Mensual"))
		{
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio =utilitario.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+com_anio.getValue().toString());
				TablaGenerica tg_mes = utilitario.consultar(ser_bodega.getMes(com_mes.getValue().toString()));
								
				try {
					p_parametros.put("titulo", "RESUMEN GENERAL MES " + tg_mes.getValor("detalle_gemes").toString() + " - " + tab_anio.getValor("detalle_geani").toString());
				} catch (NumberFormatException e) {
					System.out.println("Error: "+ e.getMessage());
				}
				
				
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("elaborado", tg_elaborado.getValor("nombre"));
				p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("mes", Long.valueOf(pckUtilidades.CConversion.CInt(com_mes.getValue().toString())));
				
				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Resumen General Mensual por Grupos"))
		{
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio =utilitario.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+com_anio.getValue().toString());
				TablaGenerica tg_mes = utilitario.consultar(ser_bodega.getMes(com_mes.getValue().toString()));
								
				try {
					p_parametros.put("titulo", "RESUMEN GENERAL MES " + tg_mes.getValor("detalle_gemes").toString() + " - " + tab_anio.getValor("detalle_geani").toString());
				} catch (NumberFormatException e) {
					System.out.println("Error: "+ e.getMessage());
				}
				
				
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("elaborado", tg_elaborado.getValor("nombre"));
				p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("mes", Long.valueOf(pckUtilidades.CConversion.CInt(com_mes.getValue().toString())));
				
				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Reporte de Consumos Mensual"))
		{
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tab_anio =utilitario.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+com_anio.getValue().toString());
				TablaGenerica tg_mes = utilitario.consultar(ser_bodega.getMes(com_mes.getValue().toString()));
				
								
				try {
					p_parametros.put("titulo", "REPORTE CONSUMOS MES " + tg_mes.getValor("detalle_gemes").toString() + " - " + tab_anio.getValor("detalle_geani").toString());
				} catch (NumberFormatException e) {
					System.out.println("Error: "+ e.getMessage());
				}
				
				
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("elaborado", tg_elaborado.getValor("nombre"));
				p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("mes", Long.valueOf(pckUtilidades.CConversion.CInt(com_mes.getValue().toString())));
				
				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}



	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public ServicioTesoreria getSer_tesoreria() {
		return ser_tesoreria;
	}

	public void setSer_tesoreria(ServicioTesoreria ser_tesoreria) {
		this.ser_tesoreria = ser_tesoreria;
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


}
