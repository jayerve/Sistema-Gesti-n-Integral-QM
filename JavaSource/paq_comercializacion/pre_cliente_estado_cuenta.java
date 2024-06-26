
package paq_comercializacion;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_comercializacion.ejb.ServicioClientes;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_cliente_estado_cuenta extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Etiqueta eti_total = new Etiqueta();
	
	// REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

	public pre_cliente_estado_cuenta(){
		
		bar_botones.limpiar();
		
		rep_reporte.setId("rep_reporte"); // id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");// ejecuta el  metodo al  aceptar  reporte
		agregarComponente(rep_reporte);// agrega el componente a la pantalla
		bar_botones.agregarReporte();// aparece el boton de reportes en la barra  de botones
		self_reporte.setId("self_reporte"); // id
		agregarComponente(self_reporte);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getDatosBasicosClientes("0,1"));
		aut_cliente.setMetodoChange("cargaCliente");
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
	

		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos("-1"));
		tab_recaudacion.getColumna("ide_fafac").setVisible(false);
		tab_recaudacion.getColumna("ide_recli").setVisible(false);
		tab_recaudacion.getColumna("ide_fanod").setVisible(false);
		tab_recaudacion.getColumna("ide_prcon").setVisible(false);
		tab_recaudacion.getColumna("valor_iva").setVisible(false);
		tab_recaudacion.getColumna("ruc_comercial_recli").setVisible(false);
		tab_recaudacion.getColumna("fecha_transaccion_fafac").setNombreVisual("FECHA EMISION");
		tab_recaudacion.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_recaudacion.getColumna("detalle_bogrm").setLongitud(60);
		tab_recaudacion.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_recaudacion.getColumna("secuencial_fafac").setLongitud(30);
		tab_recaudacion.getColumna("total_fafac").setNombreVisual("VALOR_F");
		tab_recaudacion.getColumna("valor").setNombreVisual("SALDO");
		tab_recaudacion.getColumna("interes").setNombreVisual("INTERES GENERADO");
		tab_recaudacion.setColumnaSuma("total_fafac,valor,interes,abonada");
		tab_recaudacion.setLectura(true);
        tab_recaudacion.setRows(25);
		tab_recaudacion.dibujar();

		eti_total.setId("eti_total");
		eti_total.setStyle("font-size:20px;color:red;widht:80%");
		eti_total.setValue("TOTAL A PAGAR : 0.00");
		
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_recaudacion);
        pat_panel.setFooter(eti_total);
		
        Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
	}
	
	public void cargaCliente(){
				
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos(aut_cliente.getValor()));
		tab_recaudacion.ejecutarSql();	
		utilitario.addUpdate("tab_recaudacion");
		calculoTotal();
	}
	
	public void calculoTotal(){

		double valor_total = pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getSumaColumna("valor"));
			   valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getSumaColumna("interes"));	
		eti_total.setValue("TOTAL A PAGAR : " + utilitario.getFormatoNumero(valor_total, 2));
		utilitario.addUpdate("eti_total");
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		eti_total.setValue("TOTAL A PAGAR : 0.00");
		
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos("-1"));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,aut_cliente,eti_total");
	}

	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Estado de Cuenta"))
		{
			if (aut_cliente.getValor()!=null) {
				if (rep_reporte.isVisible()) {
					Locale locale=new Locale("es","ES");
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					
					TablaGenerica clienteEDC = utilitario.consultar(ser_tesoreria.getSqlEstadoCuentaCliente(aut_cliente.getValor()));
					
					p_parametros.put("p_fecha_emision", utilitario.getDate());
                    p_parametros.put("p_ciudad", "QUITO");
                    p_parametros.put("p_ruc", clienteEDC.getValor("ruc_comercial_recli"));
                    p_parametros.put("p_razon_social", clienteEDC.getValor("razon_social").toUpperCase());
                    p_parametros.put("p_direccion", clienteEDC.getValor("direccion").toUpperCase());
                    p_parametros.put("p_total_pagar", pckUtilidades.CConversion.CStr(clienteEDC.getValor("valor")));
                    p_parametros.put("REPORT_LOCALE", locale);
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					self_reporte.dibujar();
				}
			} else {
				utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar un Cliente");

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