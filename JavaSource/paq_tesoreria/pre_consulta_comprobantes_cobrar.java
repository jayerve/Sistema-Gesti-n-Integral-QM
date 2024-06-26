
package paq_tesoreria;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_consulta_comprobantes_cobrar extends Pantalla{
	

	private Tabla tab_factura_consulta = new Tabla();
	private AutoCompletar aut_factura = new AutoCompletar();
	private Combo com_anio=new Combo();
	private String valorAut_factura;
	private VisualizarPDF vpdf_pago = new VisualizarPDF();
	public static String par_sec_notificacion;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	
	public pre_consulta_comprobantes_cobrar(){
		
		bar_botones.limpiar();
		par_sec_notificacion =utilitario.getVariable("p_modulo_secuencialnotificacion");
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		aut_factura.setId("aut_factura");
		aut_factura.setAutoCompletar(ser_facturacion.getDatosFactura("1", "")); // 1
																				// carga
																				// todos,
																				// 0
																				// carga
																				// por
																				// grupos
																				// enviados
		aut_factura.setMetodoChange("seleccionoAutocompletar"); // ejecuta el
																// metodo
																// seleccionoAutocompletar

		Etiqueta eti_colaborador = new Etiqueta("PUNTO FACTURACIÓN:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
		
		ItemMenu itm_notificacion = new ItemMenu();
		itm_notificacion.setValue("Generar Notificación");
		itm_notificacion.setMetodo("primera");
		itm_notificacion.setIcon("ui-icon-print");
		
		tab_factura_consulta.setId("tab_factura_consulta");
		tab_factura_consulta.setHeader("DETALLE DE COMPROBANTES POR COBRAR");
		tab_factura_consulta.setCampoPrimaria("codigo");
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados(0,0));
		tab_factura_consulta.getColumna("servicio_facturado").setFiltroContenido();
		tab_factura_consulta.getColumna("ruc_comercial_recli").setFiltroContenido();
		tab_factura_consulta.getColumna("razon_social_recli").setFiltroContenido();
		tab_factura_consulta.setColumnaSuma("saldo,interes,total");
		tab_factura_consulta.setRows(20);
		tab_factura_consulta.setLectura(true);
		tab_factura_consulta.dibujar();

		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_factura_consulta);
		pat_panel.getMenuTabla().getChildren().add(itm_notificacion);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Notificación Extrajudicial");
        agregarComponente(vpdf_pago);
	}

	public void seleccionaElAnio (){
		
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados(pckUtilidades.CConversion.CInt(valorAut_factura),pckUtilidades.CConversion.CInt(com_anio.getValue())));
		tab_factura_consulta.ejecutarSql();

	}
	
	// METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt) {
		aut_factura.onSelect(evt);
		valorAut_factura=aut_factura.getValor();	
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados(pckUtilidades.CConversion.CInt(valorAut_factura),pckUtilidades.CConversion.CInt(com_anio.getValue())));
		tab_factura_consulta.ejecutarSql();		
	}
	
	public void primera()
	{
		TablaGenerica tab_deuda = utilitario.consultar(ser_facturacion.getDeudaCliente(0,0,tab_factura_consulta.getValor("ruc_comercial_recli")));
		
		double total = pckUtilidades.CConversion.CDbl(tab_deuda.getValor("total"));
		
		if(total>0)
		{
			Locale locale=new Locale("es","ES");	

			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_fecha_larga", utilitario.DateStringFormat(utilitario.DeDateAString(utilitario.getDate())).toLowerCase());
	        p_parametros.put("p_notificacion_nro", utilitario.getAnio(utilitario.getFechaActual())+ "-"+pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(par_sec_notificacion),4));
	        p_parametros.put("p_notificacion", "PRIMERA NOTIFICACIÓN");
	        p_parametros.put("p_jurisdiccion", "aplicará la correspondiente jurisdicción coactiva");
	        p_parametros.put("p_ruc", tab_factura_consulta.getValor("ruc_comercial_recli"));
	        p_parametros.put("p_total", total);
	        p_parametros.put("p_total_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(total,2)));
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago.setVisualizarPDF("rep_juridico/rep_notificacion_extrajudicial.jasper", p_parametros);
	        vpdf_pago.dibujar();
	        utilitario.addUpdate("vpdf_pago");
	        
	        utilitario.agregarMensaje("Guardando secuencial primera notificacion ", "Nro: "+ser_contabilidad.numeroSecuencial(par_sec_notificacion));
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_notificacion), par_sec_notificacion);
		}
		else
			utilitario.agregarMensajeInfo("Cliente no posee valores pendientes.", "");
	
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
	
	
	public void limpiar(){
		tab_factura_consulta.limpiar();
		aut_factura.limpiar();
		com_anio.setValue(0);
		utilitario.addUpdate("tab_factura_consulta,aut_factura,com_anio");
	}

	public Tabla getTab_factura_consulta() {
		return tab_factura_consulta;
	}

	public void setTab_factura_consulta(Tabla tab_factura_consulta) {
		this.tab_factura_consulta = tab_factura_consulta;
	}

	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	
	
}