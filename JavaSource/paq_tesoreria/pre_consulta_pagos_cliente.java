package paq_tesoreria;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_consulta_pagos_cliente extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private VisualizarPDF vpdf_pago = new VisualizarPDF();

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);

	public pre_consulta_pagos_cliente(){

		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");		
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getDatosBasicosClientes("0,1"));
		aut_cliente.setMetodoChange("cargaReporte");
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaReporte");
		bar_botones.agregarBoton(bot_actualiza);
		
		ItemMenu itm_recibo = new ItemMenu();
		itm_recibo.setValue("Ver Recibo");
		itm_recibo.setMetodo("reimprime");
		itm_recibo.setIcon("ui-icon-print");

		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.getReportePagosCliente("01-01-2020","01-01-2020","-1"));
		tab_recaudacion.setColumnaSuma("monto");
		tab_recaudacion.setRows(25);
		tab_recaudacion.setLectura(true);
		tab_recaudacion.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_recaudacion);
        pat_panel.getMenuTabla().getChildren().add(itm_recibo);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);		
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago);
	}
	
	public void cargaReporte()
	{		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		String ruc = pckUtilidades.CConversion.CStr(aut_cliente.getValor());
				
		tab_recaudacion.setSql(ser_tesoreria.getReportePagosCliente(fecha_inicial,fecha_final,ruc));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion");
		
	}
	
	public void limpiar()
	{
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		tab_recaudacion.setSql(ser_tesoreria.getReportePagosCliente("01-01-2020","01-01-2020","-1"));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,aut_cliente");
	}

	public void reimprime()
	{
		int nro_documento=pckUtilidades.CConversion.CInt(tab_recaudacion.getValor("nro_documento"));
		if(nro_documento>0)
		{
			Locale locale=new Locale("es","ES");	
			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_nro_comprobante", nro_documento);
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos_tk_copia.jasper", p_parametros);
	        vpdf_pago.dibujar();
	        utilitario.addUpdate("vpdf_pago");
		}
		else
			utilitario.agregarMensajeInfo("Comprobante no Registrado.", "");
	
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

	public Tabla getTab_recaudacion() {
		return tab_recaudacion;
	}

	public void setTab_recaudacion(Tabla tab_recaudacion) {
		this.tab_recaudacion = tab_recaudacion;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	
	

}
