
package paq_tesoreria;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_consulta_comprobantes_asiento extends Pantalla{
	
	private AutoCompletar aut_factura=new AutoCompletar(); 
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Combo com_comprobante = new Combo();
	private Tabla tab_factura_consulta = new Tabla();
	private Check che_aplica_fecha=new Check();
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);

	public pre_consulta_comprobantes_asiento(){
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}
		
		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_factura.setId("aut_factura");     
		aut_factura.setAutoCompletar(ser_facturacion.getDatosSucursales()); 

		Etiqueta eti_colaborador=new Etiqueta("ESTABLECIMIENTO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		cal_fecha_inicial.setStyle("width: 100px;");
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		che_aplica_fecha.setId("che_aplica_fecha");
		Etiqueta eti_aplica_fecha=new Etiqueta("Aplica Fecha de Pago");
		bar_botones.agregarComponente(eti_aplica_fecha);
		bar_botones.agregarComponente(che_aplica_fecha);
		
		com_comprobante.setCombo(utilitario.getListaTipoComprobantes());
		com_comprobante.setValue("0");
		com_comprobante.setStyle("width: 130px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta(" Comprobante:"));
    	bar_botones.agregarComponente(com_comprobante);
    	
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_factura_consulta.setId("tab_factura_consulta");
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados("0","","1900-01-01","1900-01-01",com_comprobante.getValue().toString(),"false"));
		
		tab_factura_consulta.getColumna("cliente").setFiltroContenido();
		tab_factura_consulta.getColumna("ruc").setFiltroContenido();
		tab_factura_consulta.getColumna("secuencial").setFiltroContenido();
		
		tab_factura_consulta.setColumnaSuma("neto,iva,valor_cobrado,valor_cobrado_iva,neto_contabilizado_emision,iva_contabilizado_emision,neto_contabilizado_recaudado,iva_contabilizado_recaudado");
		tab_factura_consulta.setRows(20);
		tab_factura_consulta.setLectura(true);
		tab_factura_consulta.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_factura_consulta);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
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
	
	public void seleccionaOpcion (){
		tab_factura_consulta.limpiar();
		//utilitario.addUpdate("aut_factura");
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados("1",aut_factura.getValor(),fecha_inicial,fecha_final,com_comprobante.getValue().toString(),che_aplica_fecha.getValue().toString()));
		tab_factura_consulta.ejecutarSql();
		//utilitario.addUpdate("tab_factura_consulta");
		utilitario.agregarMensajeInfo("Busqueda", "La fecha inicio y fecha fin son aplicadas en base a la fecha de emision del comprobante.");
	}
	
	public void limpiar(){
		aut_factura.limpiar();
		tab_factura_consulta.limpiar();
		utilitario.addUpdate("tab_factura_consulta,aut_factura");
	}
	
	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	public Tabla getTab_factura_consulta() {
		return tab_factura_consulta;
	}

	public void setTab_factura_consulta(Tabla tab_factura_consulta) {
		this.tab_factura_consulta = tab_factura_consulta;
	}

	public Combo getCom_comprobante() {
		return com_comprobante;
	}

	public void setCom_comprobante(Combo com_comprobante) {
		this.com_comprobante = com_comprobante;
	}

	
}