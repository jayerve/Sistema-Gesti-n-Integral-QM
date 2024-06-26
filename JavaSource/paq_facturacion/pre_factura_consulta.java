
package paq_facturacion;

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

public class pre_factura_consulta extends Pantalla{
	
	private AutoCompletar aut_factura=new AutoCompletar(); 
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Check che_fecha_pago=new Check();
	private boolean repFecha_pag=false;
	private Check che_fecha_conc=new Check();
	private boolean repFecha_conc=false;
	private Check che_fecha_abo=new Check();
	private boolean repFecha_abo=false;
	private Combo com_comprobante = new Combo();
	private Combo com_estados = new Combo();
	
	private Tabla tab_factura_consulta = new Tabla();
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	public pre_factura_consulta(){
		
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
		aut_factura.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
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
		
		com_comprobante.setCombo(utilitario.getListaTipoComprobantes());
		com_comprobante.setValue("0");
		//com_comprobante.setMetodo("actualizaDeudaCliente");
		com_comprobante.setStyle("width: 130px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta(" Comprobante:"));
    	bar_botones.agregarComponente(com_comprobante);
    	
    	com_estados.setCombo(utilitario.getListaTipoEstados());
    	com_estados.setValue("0");
		//com_comprobante.setMetodo("actualizaDeudaCliente");
    	com_estados.setStyle("width: 130px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta(" Estados:"));
    	bar_botones.agregarComponente(com_estados);
		
		che_fecha_pago.setId("che_fecha_pago");
		che_fecha_pago.setMetodoChange("aplFechaPago");
		Etiqueta eti_fecha_pago=new Etiqueta("Aplica Fecha Pago");
		bar_botones.agregarComponente(eti_fecha_pago);
		bar_botones.agregarComponente(che_fecha_pago);
		
		che_fecha_conc.setId("che_fecha_conc");
		che_fecha_conc.setMetodoChange("aplFechaConciliacion");
		Etiqueta eti_fecha_conc=new Etiqueta("Aplica Conciliacion");
		bar_botones.agregarComponente(eti_fecha_conc);
		bar_botones.agregarComponente(che_fecha_conc);
		
		che_fecha_abo.setId("che_fecha_abo");
		che_fecha_abo.setMetodoChange("aplFechaAbono");
		Etiqueta eti_fecha_abo=new Etiqueta("Aplica Fecha Abono");
		bar_botones.agregarComponente(eti_fecha_abo);
		bar_botones.agregarComponente(che_fecha_abo);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_factura_consulta.setId("tab_factura_consulta");
		tab_factura_consulta.setSql(ser_facturacion.getComprobantes("0","","1900-01-01","1900-01-01",repFecha_pag,repFecha_conc,repFecha_abo,com_comprobante.getValue().toString(),com_estados.getValue().toString()));
		tab_factura_consulta.getColumna("estado").setLongitud(30);
		tab_factura_consulta.getColumna("secuencial").setLongitud(30);
		tab_factura_consulta.getColumna("comprobante_aplicado").setLongitud(30);
		tab_factura_consulta.getColumna("ruc").setLongitud(20);
		tab_factura_consulta.getColumna("ruc").setFiltro(true);
		tab_factura_consulta.getColumna("estado").setFiltro(true);
		tab_factura_consulta.getColumna("valor_cobro").setNombreVisual("ABONO");
		tab_factura_consulta.getColumna("secuencial").setFiltro(true);
		tab_factura_consulta.getColumna("comprobante_aplicado").setFiltro(true);
		tab_factura_consulta.getColumna("TIPO_DOCUMENTO").setFiltro(true);
		tab_factura_consulta.getColumna("observacion").setFiltro(true);
		tab_factura_consulta.getColumna("transaccion_bp").setFiltro(true);
		tab_factura_consulta.setColumnaSuma("sub_total,iva,total,valor_cobro,valor_cancelado,saldo");
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
	
	public void aplFechaConciliacion()
	{
		if(che_fecha_conc.getValue().toString().equalsIgnoreCase("true")){
			repFecha_conc=true;
		}
		else{
			repFecha_conc=false;
		}
	}
	
	public void aplFechaPago()
	{
		if(che_fecha_pago.getValue().toString().equalsIgnoreCase("true")){
			repFecha_pag=true;
		}
		else{
			repFecha_pag=false;
		}
	}
	
	public void aplFechaAbono()
	{
		if(che_fecha_abo.getValue().toString().equalsIgnoreCase("true")){
			repFecha_abo=true;
		}
		else{
			repFecha_abo=false;
		}
	}
	
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		/*aut_factura.onSelect(evt);
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		tab_factura_consulta.setSql(ser_facturacion.getComprobantes("1",aut_factura.getValor(),fecha_inicial,fecha_final,repFecha_pag,repFecha_conc));
		tab_factura_consulta.ejecutarSql();
		tab_factura_consulta.dibujar();*/
		
	}
	
	public void seleccionaOpcion (){
		tab_factura_consulta.limpiar();
		utilitario.addUpdate("aut_factura");
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		tab_factura_consulta.setSql(ser_facturacion.getComprobantes("1",aut_factura.getValor(),fecha_inicial,fecha_final,repFecha_pag,repFecha_conc,repFecha_abo,com_comprobante.getValue().toString(),com_estados.getValue().toString()));
		tab_factura_consulta.ejecutarSql();
		utilitario.addUpdate("tab_factura_consulta");
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

	public Combo getCom_estados() {
		return com_estados;
	}

	public void setCom_estados(Combo com_estados) {
		this.com_estados = com_estados;
	}
	
	
	
}