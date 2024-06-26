package paq_tesoreria;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

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
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_consulta_facturas extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Check che_aplica_fecha_pago=new Check();
	private Check che_conciliados=new Check();

	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	// REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

	public pre_consulta_facturas(){
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}

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
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getClientesDatosBasicos());
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
		
		che_conciliados.setId("che_conciliados");
		Etiqueta eti_conciliados=new Etiqueta("Ver Conciliados");
		bar_botones.agregarComponente(eti_conciliados);
		bar_botones.agregarComponente(che_conciliados);
		
		che_aplica_fecha_pago.setId("che_aplica_fecha");
		Etiqueta eti_aplica_fecha=new Etiqueta("Aplica Fecha de Pago");
		bar_botones.agregarComponente(eti_aplica_fecha);
		bar_botones.agregarComponente(che_aplica_fecha_pago);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaCliente");
		bar_botones.agregarBoton(bot_actualiza);

		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.comprobantesClientes("-1","1900-01-01","1900-01-01","false","false"));
		tab_recaudacion.getColumna("ide_recli").setVisible(false);
		tab_recaudacion.getColumna("ruc_comercial_recli").setNombreVisual("RUC");
		tab_recaudacion.getColumna("ruc_comercial_recli").setLongitud(30);
		tab_recaudacion.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_recaudacion.getColumna("razon_social_recli").setNombreVisual("RAZON SOCIAL");
		tab_recaudacion.getColumna("razon_social_recli").setLongitud(150);
		tab_recaudacion.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_recaudacion.getColumna("secuencial_fafac").setFiltroContenido();
		tab_recaudacion.getColumna("comprobante_pago").setFiltroContenido();
		tab_recaudacion.getColumna("comprobante_aplicado").setFiltroContenido();
		tab_recaudacion.getColumna("detalle_bogrm").setLongitud(130);
		tab_recaudacion.getColumna("secuencial_fafac").setLongitud(30);
		tab_recaudacion.getColumna("comprobante_aplicado").setLongitud(30);
		tab_recaudacion.getColumna("detalle_coest").setNombreVisual("ESTADO");
		tab_recaudacion.getColumna("detalle_coest").setLongitud(20);
		tab_recaudacion.getColumna("detalle_retip").setNombreVisual("FORMA PAGO");
		tab_recaudacion.getColumna("detalle_retip").setLongitud(30);
		tab_recaudacion.setColumnaSuma("total,abono,saldo");
		tab_recaudacion.setRows(25);
		tab_recaudacion.setLectura(true);
		tab_recaudacion.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_recaudacion);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
	}
	
	public void cargaCliente(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_recaudacion.setSql(ser_tesoreria.comprobantesClientes(aut_cliente.getValor(),fecha_inicial,fecha_final,che_aplica_fecha_pago.getValue().toString(),che_conciliados.getValue().toString()));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion");
		
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		
		tab_recaudacion.setSql(ser_tesoreria.comprobantesClientes("-1","1900-01-01","1900-01-01","false","false"));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,aut_cliente,eti_devolucion");
	}

	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Comprobantes Clientes"))
		{
			if (aut_cliente.getValor()!=null) {
				if (rep_reporte.isVisible()) {
					Locale locale=new Locale("es","ES");
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					p_parametros.put("titulo", "DETALLE DE COMPROBANTES");
					p_parametros.put("p_cliente_ruc", aut_cliente.getValor());
					p_parametros.put("p_fecha_emision", utilitario.DateStringFormat(utilitario.getFechaActual()));
					p_parametros.put("p_fecha_i", utilitario.DeStringADate(cal_fecha_inicial.getFecha()));
					p_parametros.put("p_fecha_f", utilitario.DeStringADate(cal_fecha_final.getFecha()));
					p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
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
