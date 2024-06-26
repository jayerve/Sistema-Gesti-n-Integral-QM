
package paq_comercializacion;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_comercializacion.ejb.ServicioClientes;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_cliente_consulta extends Pantalla{
	
	private AutoCompletar aut_factura=new AutoCompletar(); 
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Check che_fecha_pago=new Check();
	private Check che_fecha_contrato=new Check();
	private Tabla tab_cliente_consulta = new Tabla();
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	
	public pre_cliente_consulta(){
		
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
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial:"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final:"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		che_fecha_pago.setId("che_fecha_pago");
		//che_fecha_pago.setMetodoChange("aplFechaPago");che_fecha_pago
		che_fecha_pago.setValue(true);
		//che_fecha_pago.setDisabled(true);
		Etiqueta eti_fecha_pago=new Etiqueta("Aplica Fecha Facturación");
		bar_botones.agregarComponente(eti_fecha_pago);
		bar_botones.agregarComponente(che_fecha_pago);

//		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial Contrato:"));
//		cal_fec_ini.setFechaActual();
//		bar_botones.agregarComponente(cal_fec_ini);
//
//		bar_botones.agregarComponente(new Etiqueta("Fecha Final Contrato:"));
//		cal_fec_fin.setFechaActual();
//		bar_botones.agregarComponente(cal_fec_fin);
		
		che_fecha_contrato.setId("che_fecha_contrato");
		che_fecha_contrato.setMetodoChange("aplFechaContrato");
		//che_fecha_contrato.setDisabled(true);
		
		Etiqueta eti_fecha_contrato=new Etiqueta("Aplica Fecha Contrato");
		bar_botones.agregarComponente(eti_fecha_contrato);
		bar_botones.agregarComponente(che_fecha_contrato);

		
		
//		che_fecha_conc.setId("che_fecha_conc");
//		che_fecha_conc.setMetodoChange("aplFechaConciliacion");
//		Etiqueta eti_fecha_conc=new Etiqueta("Aplica Conciliacion");
//		bar_botones.agregarComponente(eti_fecha_conc);
//		bar_botones.agregarComponente(che_fecha_conc);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_cliente_consulta.setId("tab_cliente_consulta");
		tab_cliente_consulta.setSql(ser_cliente.getClientesFacturasContratos("-1", "0","1900-01-01","2017-01-01","true","false"));
		//tab_cliente_consulta.getColumna("ide_recli").setLongitud(10);
		//tab_cliente_consulta.getColumna("ide_recli").setNombreVisual("CÓDIGO");
		//tab_cliente_consulta.getColumna("tipo").setNombreVisual("TIPO");
		tab_cliente_consulta.getColumna("ruc_comercial_recli").setFiltroContenido();
		tab_cliente_consulta.getColumna("ruc_comercial_recli").setNombreVisual("RUC");
		tab_cliente_consulta.getColumna("ruc_comercial_recli").setLongitud(30);
		/*tab_cliente_consulta.getColumna("aplica_interes_recli").setNombreVisual("GENERA INTERES");
		
		tab_cliente_consulta.getColumna("tipo_contribuyente").setNombreVisual("TIPO DE CONTRIBUYENTE");
		tab_cliente_consulta.getColumna("razon_social_recli").setFiltroContenido();
		tab_cliente_consulta.getColumna("razon_social_recli").setNombreVisual("RAZÓN SOCIAL");
		tab_cliente_consulta.getColumna("establecimiento_operativo").setFiltroContenido();
		tab_cliente_consulta.getColumna("representante_legal_recli").setFiltroContenido();
		tab_cliente_consulta.getColumna("representante_legal_recli").setNombreVisual("REPRESENTANTE LEGAL");
		//tab_cliente_consulta.getColumna("nombre_comercial_recli").setNombreVisual("NOMBRE COMERCIAL");
		//tab_cliente_consulta.getColumna("parroquia").setNombreVisual("PARROQUIA");
		tab_cliente_consulta.getColumna("direccion_recld").setNombreVisual("DIRECCIÓN CLIENTE");
		tab_cliente_consulta.getColumna("telefono_factura_recli").setNombreVisual("TELÉFONO DE FACTURA");
		tab_cliente_consulta.getColumna("email_recle").setNombreVisual("E-MAIL");
		tab_cliente_consulta.getColumna("frecuencia").setNombreVisual("FRECUENCIA");
		tab_cliente_consulta.getColumna("estimado_desecho_recl").setNombreVisual("ESTIMADO DE DESECHO");
		tab_cliente_consulta.getColumna("detalle_bogrm").setNombreVisual("PUNTO DE VENTA");
		//tab_cliente_consulta.getColumna("detalle_tipcli").setNombreVisual("TIPO DE SERVICIO");
		tab_cliente_consulta.getColumna("numero_contrato").setNombreVisual("NRO. CONTRATO");
		tab_cliente_consulta.getColumna("numero_contrato").setFiltroContenido();
		//tab_cliente_consulta.getColumna("fecha_inicio_prcon").setNombreVisual("FECHA DE INICIO DE CONTRATO");
		tab_cliente_consulta.getColumna("ide_fafac").setVisible(false);;
		tab_cliente_consulta.getColumna("fecha_transaccion_fafac").setNombreVisual("FECHA DE FACTURA");
		tab_cliente_consulta.getColumna("valor_factura").setNombreVisual("VALOR DE FACTURA");
		tab_cliente_consulta.getColumna("estado").setNombreVisual("ESTADO");
	*/
		
		/*
		tab_cliente_consulta.getColumna("fecha_transaccion_fafac").setNombreVisual("FECHA DE FACTURA");
		tab_cliente_consulta.getColumna("fecha_transaccion_fafac").setLongitud(25);
		tab_cliente_consulta.getColumna("valor_factura").setNombreVisual("VALOR DE FACTURA");
		tab_cliente_consulta.getColumna("valor_factura").setLongitud(35);
		tab_cliente_consulta.getColumna("nombre_comercial").setVisible(false);
		tab_cliente_consulta.getColumna("nombre_comercial_recli").setVisible(false);
		tab_cliente_consulta.getColumna("nombre_comercial_factura").setNombreVisual("NOMBRE COMERCIAL");
		tab_cliente_consulta.getColumna("nombre_comercial_factura").setLongitud(50);
		tab_cliente_consulta.getColumna("codigo_zona_recli").setVisible(false);
		tab_cliente_consulta.getColumna("direccion_recli").setNombreVisual("DIRECCIÓN");
		tab_cliente_consulta.getColumna("direccion_recli").setLongitud(50);
		tab_cliente_consulta.getColumna("telefono_factura").setNombreVisual("TELÉFONO");
		tab_cliente_consulta.getColumna("telefono_factura").setLongitud(20);
		tab_cliente_consulta.getColumna("email").setNombreVisual("E-MAIL");
		tab_cliente_consulta.getColumna("email").setLongitud(50);
		tab_cliente_consulta.getColumna("numero_contrato_prcon").setNombreVisual("No. DE CONTRATO");
		tab_cliente_consulta.getColumna("numero_contrato_prcon").setLongitud(30);
		tab_cliente_consulta.getColumna("fecha_inicio_prcon").setNombreVisual("FECHA DE CONTRATO");
		tab_cliente_consulta.getColumna("fecha_inicio_prcon").setLongitud(40);
		tab_cliente_consulta.getColumna("detalle_bogrm").setLongitud(40);
		tab_cliente_consulta.getColumna("detalle_bogrm").setNombreVisual("PUNTO DE VENTA");
		tab_cliente_consulta.getColumna("ide_bogrm").setVisible(false);
		tab_cliente_consulta.getColumna("activo_recli").setLongitud(40);
		tab_cliente_consulta.getColumna("activo_recli").setNombreVisual("ESTADO");*/
		tab_cliente_consulta.setColumnaSuma("SUBTOTAL_ACUMULADO,IVA_ACUMULADO,TOTAL_ACUMULADO,INTERES_ACUMULADO,TOTAL_VENTA_SIN_IVA");
		tab_cliente_consulta.setLectura(true);
		tab_cliente_consulta.setRows(20);
		tab_cliente_consulta.dibujar();
		

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_cliente_consulta);
		
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
	
	
	/*
	public void aplFechaPago()
	{
		if(che_fecha_pago.getValue().toString().equalsIgnoreCase("true")){
			repFecha_pag=true;
		}
		else{
			repFecha_pag=false;
		}
	}
	*/
	public void aplFechaContrato()
	{
		/*if(che_fecha_contrato.getValue().toString().equalsIgnoreCase("true")){
			repFecha_conc=true;
		}
		else{
			repFecha_conc=false;
		}*/
		
		utilitario.agregarMensajeInfo("Opcion en Desarrollo.", "");
	}
	
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_factura.onSelect(evt);
		//tab_cliente_consulta.limpiar();
		//utilitario.addUpdate("aut_factura");
		//String fecha_inicial=cal_fecha_inicial.getFecha();
		//String fecha_final=cal_fecha_final.getFecha();		
		//tab_cliente_consulta.setSql(ser_cliente.getClientesFacturasContratos("1",aut_factura.getValor(), fecha_inicial, fecha_final, che_fecha_pago.getValue().toString(),che_fecha_contrato.getValue().toString()));
		tab_cliente_consulta.setSql(ser_cliente.getClientesFacturasContratos("1",aut_factura.getValor(), "", "", "false","false"));
		tab_cliente_consulta.ejecutarSql();
		//tab_cliente_consulta.dibujar();
		
	}
	
	public void seleccionaOpcion (){
		tab_cliente_consulta.limpiar();
		//utilitario.addUpdate("aut_factura");
		String establecimiento="0";
		
		if(aut_factura.getValor()!=null)
			establecimiento=aut_factura.getValor();
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		tab_cliente_consulta.setSql(ser_cliente.getClientesFacturasContratos("1",establecimiento, fecha_inicial, fecha_final, che_fecha_pago.getValue().toString(),che_fecha_contrato.getValue().toString()));
//		tab_cliente_consulta.setSql(ser_cliente.getConsultaClientes("1",aut_factura.getValor(), fecha_inicial, fecha_final, che_fecha_pago.getValue().toString()));
//		tab_cliente_consulta.setSql(ser_cliente.getConsultaClientes("1","0", fecha_inicial, fecha_final, che_fecha_pago.getValue().toString()));
		tab_cliente_consulta.ejecutarSql();
		//tab_cliente_consulta.dibujar();
	}
	
	public void limpiar(){
		aut_factura.limpiar();
		tab_cliente_consulta.limpiar();
		utilitario.addUpdate("aut_factura");
	}

	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}

	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}

	public Check getChe_fecha_pago() {
		return che_fecha_pago;
	}

	public void setChe_fecha_pago(Check che_fecha_pago) {
		this.che_fecha_pago = che_fecha_pago;
	}

	public Check getChe_fecha_contrato() {
		return che_fecha_contrato;
	}

	public void setChe_fecha_contrato(Check che_fecha_contrato) {
		this.che_fecha_contrato = che_fecha_contrato;
	}

	public Tabla getTab_cliente_consulta() {
		return tab_cliente_consulta;
	}

	public void setTab_cliente_consulta(Tabla tab_cliente_consulta) {
		this.tab_cliente_consulta = tab_cliente_consulta;
	}

	
	
	
}