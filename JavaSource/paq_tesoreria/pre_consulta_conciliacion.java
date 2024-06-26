package paq_tesoreria;

import javax.ejb.EJB;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_consulta_conciliacion extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Texto txt_numero = new Texto();

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);

	public pre_consulta_conciliacion(){

		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}
		
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
		
		bar_botones.agregarComponente(new Etiqueta(" ó Nro Referencia :"));
		txt_numero.setMaxlength(5);
		txt_numero.setSoloEnteros();
		txt_numero.setValue("0");
		bar_botones.agregarComponente(txt_numero);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaReporte");
		bar_botones.agregarBoton(bot_actualiza);

		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.getReporteConciliacionPich("-1","","","",""));
		tab_recaudacion.setColumnaSuma("valor_archivo_faauc,valor_deuda_faauc, interes_incluido_deuda_faauc, valor_vuelto_faauc");
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
	
	public void cargaReporte()
	{
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		String tipo="1";
		String ruc = pckUtilidades.CConversion.CStr(aut_cliente.getValor());
		
		if(pckUtilidades.CConversion.CInt(txt_numero.getValue().toString())>0)
			tipo="2";
		
		tab_recaudacion.setSql(ser_tesoreria.getReporteConciliacionPich(tipo,fecha_inicial,fecha_final,txt_numero.getValue().toString(),ruc));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion");
		
	}
	
	public void limpiar()
	{
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		tab_recaudacion.setSql(ser_tesoreria.getReporteConciliacionPich("-1","","","",""));
		tab_recaudacion.ejecutarSql();
		txt_numero.setValue("0");
		utilitario.addUpdate("tab_recaudacion,aut_cliente,txt_numero");
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
