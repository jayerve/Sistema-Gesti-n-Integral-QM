package paq_tesoreria;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import grandesclientes.AbonoContrato;
import grandesclientes.RespuestaContrato;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import pck_cliente.servicio;

public class pre_recaudacion_parcial extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private Tabla tab_abono=new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Texto	txt_total = new Texto();
	private Texto	txt_cliente = new Texto();
	private Texto 	txt_documento= new Texto();
	private Texto	txt_valor_entregado = new Texto();
	private AreaTexto	txt_observacion = new AreaTexto();
	private Texto	txt_cheque = new Texto();
	private Combo   com_forma_pago = new Combo();
	private Combo   com_forma_cobro = new Combo();
	//private Etiqueta eti_valor_pendiente= new Etiqueta();
	private Etiqueta eti_devolucion= new Etiqueta();
	private AutoCompletar aut_caja= new AutoCompletar();
	private AutoCompletar aut_sucursal= new AutoCompletar();
	private AutoCompletar aut_lugar_cobro=new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
    private VisualizarPDF vpdf_pago = new VisualizarPDF();
    private int ambiente = 1; //1 Test: 2 Produccion
    private boolean autorizar = true; //true Produccion
    public static String par_modulosec_recaudacion;
    private boolean cobroManual=false;
    double valor_pendiente=0;
    double vuelto=0;

	String ide_caja="";
	String ide_empleado="";
	String ide_sucursal="";
	String ide_lugar="";
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_recaudacion_parcial(){
		
		ambiente=Integer.parseInt(utilitario.getVariable("p_ambiente_sri"));
		par_modulosec_recaudacion=utilitario.getVariable("p_modulo_secuencial_recaudacion");
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}
		
		bar_botones.limpiar();
		ide_caja=obtenerCaja();
		ide_empleado=obtenerEmpleado();
		ide_sucursal=obtenerSucursal();
		ide_lugar=obtenerLugarCobro();
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getDatosBasicosClientes("0,1"));
		aut_cliente.setMetodoChange("actualizaDeudaCliente");
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
		
		 // FORMA DE cobro
		com_forma_cobro.setCombo(utilitario.getListaTipoCobro());
		com_forma_cobro.setValue("1");
		com_forma_cobro.setMetodo("actualizaDeudaCliente");
		com_forma_cobro.setStyle("width: 200px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta(" Tipo Cobro:"));
    	bar_botones.agregarComponente(com_forma_cobro);
		
		
    	Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
    		
        // CAJAS
        Etiqueta eti_caja = new Etiqueta("CAJA :");
        eti_caja.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_caja);
       	aut_caja.setId("aut_caja");
       	aut_caja.setStyle("text-aling:left");
       	aut_caja.setAutoCompletar("select ide_tecaj,detalle_tecaj from tes_caja ");
        aut_caja.setValor(ide_caja);
       	aut_caja.setDisabled(true);
       	gri_formulario.getChildren().add(aut_caja);
       	
       	// USUARIO REALIZA PAGO
        Etiqueta eti_usuario = new Etiqueta("NOMBRE REALIZA PAGO :");
        eti_usuario.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_usuario);
       	txt_cliente.setId("txt_cliente");
       	txt_cliente.setStyle("width:350px;text-aling:left");
       	//txt_cliente.setDisabled(true);
       	gri_formulario.getChildren().add(txt_cliente);
 
       	
       	// DOCUEMNTO PAGO
    	Etiqueta eti_documento = new Etiqueta("NRO DOCUMENTO COBRO:");
    	eti_documento.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_documento);
		txt_documento.setId("txt_documento");
       	txt_documento.setStyle("width:350px;text-aling:left");	
       	txt_documento.setValue("0");
       	txt_documento.setDisabled(true);
        gri_formulario.getChildren().add(txt_documento);
        
        // DOCUEMNTO CHEQUE
    	Etiqueta eti_cheque = new Etiqueta("NRO CHEQUE/TRASNFERENCIA:");
    	eti_cheque.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_cheque);
		txt_cheque.setId("txt_cheque");
       	txt_cheque.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_cheque);
        
        Etiqueta eti_titulo = new Etiqueta("TOTAL COBRAR:");
        eti_titulo.setStyle("font-size: 17px;color: red;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_titulo);
		txt_total.setId("txt_total");
		txt_total.setValue("0.00");
		txt_total.setDisabled(true);
		txt_total.setStyle("font-size: 14px;color: red;font-weight: bold;width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_total);
        
        
        // FORMA DE PAGO
    	com_forma_pago.setCombo("select ide_retip,detalle_retip from rec_tipo where activo_retip =true");
    	com_forma_pago.setValue("4");
    	com_forma_pago.setStyle("width: 200px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta("Forma de Pago:"));
    	bar_botones.agregarComponente(com_forma_pago);

        // TOTAL VALOR ENTREGADO
    	Etiqueta eti_valor_entregado = new Etiqueta("VALOR ENTREGADO:");
    	eti_valor_entregado.setStyle("font-size: 17px;color: blue;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_entregado);
		txt_valor_entregado.setId("txt_valor_entregado");
		txt_valor_entregado.setValue("0.00");
		txt_valor_entregado.setMetodoChange("devolucion");
		//txt_valor_entregado.setMetodoKeyPress("devolucion");
		txt_valor_entregado.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_valor_entregado);
        
        // OBSERVACIONES
    	Etiqueta eti_observacion = new Etiqueta("OBSERVACIONES:");
    	eti_observacion.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_observacion);
       	txt_observacion.setId("txt_observacion");
       	txt_observacion.setValue("");
       	txt_observacion.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_observacion);
        
        // VALOR PENDIENTE
    	/*Etiqueta eti_valor_devolucion = new Etiqueta("VALOR PENDIENTE:");
    	eti_valor_devolucion.setStyle("font-size: 17px;color: red;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_devolucion);
       	eti_valor_pendiente.setId("eti_valor_pendiente");
       	eti_valor_pendiente.setStyle("font-size: 20px;color: red;font-weight: bold;text-aling:left");		
       	eti_valor_pendiente.setValue("0.00");
		gri_formulario.getChildren().add(eti_valor_pendiente);*/
        
        // TOTAL VALOR DEVOLUCION
    	Etiqueta eti_valor_devolucion = new Etiqueta("VALOR DEVOLVER:");
    	eti_valor_devolucion.setStyle("font-size: 17px;color: blue;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_devolucion);
       	eti_devolucion.setId("eti_devolucion");
       	eti_devolucion.setStyle("font-size: 20px;color: red;font-weight: bold;text-aling:left");		
       	eti_devolucion.setValue("0.00");
		gri_formulario.getChildren().add(eti_devolucion);

        /////PAgar
		Boton bot_pagar=new Boton();
		bot_pagar.setIcon("ui-icon-person");
		bot_pagar.setValue("COBRAR");
		bot_pagar.setMetodo("pagado");
		bar_botones.agregarBoton(bot_pagar);
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
		con_guardar.setTitle("CONFIRMACION DE COBRO");

		agregarComponente(con_guardar);
		
		
		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_recaudacion");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_recaudacion");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo( "seleccionarNinguna");
        itm_niguna.setUpdate("tab_recaudacion");
        boc_seleccion_inversa.agregarBoton(itm_niguna);
        
		
		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos("-1"));
		//tab_recaudacion.getColumna("codigo").setVisible(false);
		tab_recaudacion.getColumna("ide_fafac").setVisible(false);
		tab_recaudacion.getColumna("ide_recli").setVisible(false);
		tab_recaudacion.getColumna("ide_fanod").setVisible(false);
		tab_recaudacion.getColumna("ide_prcon").setVisible(false);
		tab_recaudacion.getColumna("ruc_comercial_recli").setVisible(false);
		tab_recaudacion.getColumna("fecha_transaccion_fafac").setNombreVisual("FECHA EMISION");
		tab_recaudacion.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_recaudacion.getColumna("detalle_bogrm").setLongitud(40);
		tab_recaudacion.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_recaudacion.getColumna("secuencial_fafac").setLongitud(30);
		tab_recaudacion.getColumna("total_fafac").setNombreVisual("VALOR_F");
		tab_recaudacion.getColumna("valor").setNombreVisual("SALDO");
		tab_recaudacion.getColumna("interes").setNombreVisual("INTERES GENERADO");
		tab_recaudacion.setColumnaSuma("total_fafac,valor,valor_iva,interes,abonada");
		//tab_recaudacion.onSelect("verAbono");
		tab_recaudacion.onSelectCheck("calculoTotal");
		tab_recaudacion.onUnselectCheck("calculoTotal");
		tab_recaudacion.setLectura(true);
		tab_recaudacion.setTipoSeleccion(true);
        tab_recaudacion.setRows(25);
		tab_recaudacion.dibujar();
		
		PanelTabla pat_formulario=new PanelTabla();
		pat_formulario.setId("pat_formulario");
		pat_formulario.setHeader(gri_formulario);
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(boc_seleccion_inversa);
        pat_panel.setPanelTabla(tab_recaudacion);
        
        tab_abono.setId("tab_abono");
        tab_abono.setTabla("fac_cobro", "ide_facob", 2);
        tab_abono.setCondicion("ide_fafac=-1");
        tab_abono.setGenerarPrimaria(false);
        tab_abono.getColumna("ide_facob").setNombreVisual("CODIGO");
        tab_abono.getColumna("ide_facob").setLectura(true);
        tab_abono.getColumna("ide_fafac").setVisible(false);
        tab_abono.getColumna("ide_fanod").setVisible(false);
        tab_abono.getColumna("ide_tecaj").setVisible(false);
        tab_abono.getColumna("ide_prcon").setVisible(false);
        //tab_abono.getColumna("ide_facoa").setVisible(false);
        //tab_abono.getColumna("ide_caja").setVisible(false);
        //tab_abono.getColumna("ide_factura").setVisible(false);
        //tab_abono.getColumna("ide_lugar_cobro").setVisible(false);
        //tab_abono.getColumna("tipo").setVisible(false);
        
        tab_abono.getColumna("fecha_cobro_facob").setValorDefecto(utilitario.getFechaActual());
        tab_abono.getColumna("fecha_cobro_facob").setLectura(true);
        tab_abono.getColumna("valor_cobro_interes_facob").setValorDefecto("0.00");
        tab_abono.getColumna("valor_cobro_interes_facob").setLectura(true);
        tab_abono.getColumna("ide_sucu").setCombo("sis_sucursal","ide_sucu","nom_sucu","");
        tab_abono.getColumna("ide_sucu").setLectura(true);
        tab_abono.getColumna("ide_gtemp").setCombo("gth_empleado","ide_gtemp","apellido_paterno_gtemp || ' ' ||  primer_nombre_gtemp ","");
        tab_abono.getColumna("ide_gtemp").setLectura(true);
        tab_abono.getColumna("ide_gtemp").setLongitud(50);
        tab_abono.getColumna("ide_retip").setCombo("rec_tipo","ide_retip","detalle_retip","");
        tab_abono.getColumna("ide_retip").setLectura(true);
        tab_abono.getColumna("ide_retip").setLongitud(50);
        tab_abono.getColumna("nro_documento_facob").setLectura(true);
        tab_abono.getColumna("cliente_pago_facob").setLectura(true);
        tab_abono.getColumna("cliente_pago_facob").setLongitud(50);
        tab_abono.getColumna("valor_cobro_facob").setValorDefecto("0.00");
        //tab_abono.getColumna("valor_cobro_facob").setLectura(true);
        tab_abono.getColumna("valor_cobro_iva_facob").setValorDefecto("0.00");
        tab_abono.getColumna("activo_facob").setValorDefecto("true");
        //tab_abono.setLectura(true);
        tab_abono.setColumnaSuma("valor_cobro_facob,valor_cobro_iva_facob,valor_cobro_interes_facob");
        
        tab_abono.dibujar();
        
		PanelTabla pat_abono=new PanelTabla();
		pat_abono.setId("pat_abono");
		pat_abono.setPanelTabla(tab_abono);
		
		Division div1 = new Division();
		div1.setId("div1");
		div1.dividir2(pat_panel,pat_abono, "75%", "v");

		Division div_recaudacion=new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir2(pat_formulario,div1, "33%", "h");
		
		agregarComponente(div_recaudacion);
		
		Boton bot_reimprimir=new Boton();
		bot_reimprimir.setIcon("ui-icon-print"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reimprimir.setValue("REIMPRIMIR");
		bot_reimprimir.setMetodo("reimprime");
		bar_botones.agregarBoton(bot_reimprimir);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago);
        
        cobroManual=false;
		
	}
	
	public void devolucion(){
		if(txt_total.getValue().toString()==""){
			utilitario.agregarMensajeInfo("No existe devolucion", "No existe un valor a cobrar por lo cual no se puede calcular el valor a devolver");
			return;	
		}
		double dou_valor_total=pckUtilidades.CConversion.CDbl_2(txt_total.getValue().toString());
		double dou_valor_entregado=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue().toString());
		double dou_resultado=0;
		if(dou_valor_entregado>dou_valor_total){
			dou_resultado=dou_valor_entregado-dou_valor_total;
			eti_devolucion.setValue(utilitario.getFormatoNumero(dou_resultado, 2));
			utilitario.addUpdate("eti_devolucion");

		}
		else{
			utilitario.agregarMensajeError("No existe devolucion", "El valor entregado por el cliente es inferior al valor a cobrar");
			//txt_valor_entregado.setValue("0");
			eti_devolucion.setValue("0.00");
			utilitario.addUpdate("eti_devolucion");
			//return;
		}
		
	}
	
	public void pagado(){
		Locale locale=new Locale("es","ES");
		
		System.out.println("Cobrando.... favor espere....");

		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No esta autorizado para el cobro, favor contacte al administrador.");
			return;
		}

		if(aut_cliente.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un cliente asignado");
			return;
		}
		
		if(ide_empleado==null || ide_empleado==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un recaudador asignado");
			return;
		}
		
		if(txt_documento.getValue()==null || txt_documento.getValue().toString().length()==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un numero de comprobante asignado");
			return;
		}
		
		if(tab_recaudacion.getTotalFilas()<1){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existen cobros pendientes");
			return;
		}
		
		/*if(txt_cliente.getValue()==null || txt_cliente.getValue().toString().length()==0){
			utilitario.agregarMensajeError("No se puede cobrar", "Ingrese un cliente de cobro");
			return;
		}*/
		
		if(pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue())<=0)
		{
			utilitario.agregarMensajeError("No se puede cobrar", "Ingrese un monto mayor a cero");
			return;
		}
		
		if(tab_recaudacion.getSeleccionados().length <= 0){
			utilitario.agregarMensajeError("No se puede cobrar", "Seleccione al menos un registro...");
			return;
		}

		String estado_pagado=utilitario.getVariable("p_factura_pagado");
		String estado_abonada=utilitario.getVariable("p_factura_abonada");
		
		if(pckUtilidades.CConversion.CInt(com_forma_pago.getValue()) == 4)
			vuelto=pckUtilidades.CConversion.CDbl_2(eti_devolucion.getValue());
		
		if (!con_guardar.isVisible()){

			con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
			con_guardar.setTitle("CONFIRMACION DE COBRO");
			con_guardar.getBot_aceptar().setMetodo("pagado");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
			con_guardar.cerrar();
			
			if(!cobroManual)
			{
				if(validaDocumentoCobro(txt_documento.getValue().toString()))
				{
					System.out.println("Duplicado de Secuencial abonos: " + txt_documento.getValue().toString());
					int secuencial=pckUtilidades.CConversion.CInt(txt_documento.getValue().toString()) + 1;
					System.out.println("Actualizando el Secuencial abonos: " + secuencial);
					System.out.println("guardaSecuencial abonos: "+par_modulosec_recaudacion);
					ser_contabilidad.guardaSecuencial(secuencial+"", par_modulosec_recaudacion);
					
					txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
					System.out.println("Secuencial guardado de abonos: "+txt_documento.getValue().toString());
				}
				
				if(!validaDocumentoCobro(txt_documento.getValue().toString()))
				{
					double valorEntregado=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
					double minimoPagar=0;
					double valorIva=0;
					double interes=0;
					double deuda=0;
					double abono=0;
					boolean cobre=false;
					int i=0;
					
					for(int j=0;j<tab_recaudacion.getSeleccionados().length;j++){
						i = tab_recaudacion.getSeleccionados()[j].getIndice();
						abono=0;
						
						interes=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"interes"));
						valorIva=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor_iva"));
						minimoPagar=interes+valorIva;
						deuda=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor"));
						
						if(valorEntregado>=minimoPagar && valorEntregado>0 && deuda>0)
						{
							valorEntregado=valorEntregado-interes;
							
							if(valorEntregado>=deuda)
							   abono=deuda;
							else
							   abono=valorEntregado;
							
							abono = abono-valorIva;
							
							if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==2)
							{
					        	AbonoContrato objAbContrato = new AbonoContrato();
					        	objAbContrato.setIdeRecli(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(i,"ide_recli")));
					        	objAbContrato.setMonto((float)pckUtilidades.CConversion.CDbl_2(abono));
					        	objAbContrato.setRucComercial(tab_recaudacion.getValor(i,"ruc_comercial_recli"));
					        	objAbContrato.setNumeroContrato(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(i,"secuencial_fafac")));
					        	objAbContrato.setTipoContrato(2);
					        	objAbContrato.setTipoPago(pckUtilidades.CConversion.CInt(com_forma_pago.getValue()));
					        	objAbContrato.setNumeroComprobantePago(pckUtilidades.CConversion.CStr(txt_documento.getValue()));
					        	
					        	try
					        	{
						        	RespuestaContrato resp=servicio.procesarAbonoContrato(objAbContrato);
						        	
						        	if(!resp.isValid())
						        	{
						        		utilitario.agregarMensaje("Respuesta Servicio", resp.getMessage());
						        		return;
						        	}
					        	}catch(Exception ex)
					        	{
					        		System.out.println("Error al consumir el Servicio: "+ex.getMessage());
					        		utilitario.agregarMensajeError("Error al consumir el Servicio", "No se pudo establecer conexion con la plataforma de grandes clientes...");
					        		return;
					        	}
					        	
							}
							
							tab_abono.insertar();
							tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
					        tab_abono.setValor("nro_documento_facob",txt_documento.getValue().toString());
					        tab_abono.setValor("ide_retip",com_forma_pago.getValue().toString());
					        tab_abono.setValor("ide_tecaj",ide_caja);
					        tab_abono.setValor("ide_sucu",ide_sucursal);
					        tab_abono.setValor("ide_gtemp",ide_empleado);
					        tab_abono.setValor("ide_fafac",tab_recaudacion.getValor(i,"ide_fafac"));
					        tab_abono.setValor("ide_prcon",tab_recaudacion.getValor(i,"ide_prcon"));
					        tab_abono.setValor("valor_cobro_facob",pckUtilidades.CConversion.CDbl_2(abono)+"");
					        tab_abono.setValor("valor_cobro_interes_facob",interes+"");
					        tab_abono.setValor("valor_cobro_iva_facob",valorIva+"");
					        tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
					        tab_abono.setValor("observaciones_facob", txt_observacion.getValue().toString());
					        
					        cobre=true;
					        
					        valorEntregado=valorEntregado-abono-valorIva;
					        
					        if(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(i,"ide_fanod")) > 0){
					        	tab_abono.setValor("ide_fanod",tab_recaudacion.getValor(i,"ide_fanod"));
					        	
								utilitario.getConexion().ejecutarSql("update fac_nota_debito set ide_coest="+estado_pagado
										+" ,fecha_actua=now(),fecha_emision_fanod=now(), documento_cobro_fanod='"+txt_documento.getValue().toString()
										+"' where ide_fanod="+tab_recaudacion.getValor(i,"ide_fanod"));
								
								if(abono<deuda && valorEntregado<=0)
								{
									if(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"total_fafac"))>0)
									{
									     utilitario.getConexion().ejecutarSql("update fac_factura set fecha_vencimiento_fafac='"+utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual())
											+"' ,observacion_fafac=observacion_fafac||' nota de debito cancelada pero aun existe saldo que pagar.' where ide_fafac="+tab_recaudacion.getValor(i,"ide_fafac"));
									}
								}
								
								aceptarDialogoNDelectronica(tab_recaudacion.getValor(i,"ide_fanod")); //Envia al SRI la nota de debito electronica
							}
	
						}
						
						if(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(i,"ide_fanod")) > 0)
						{
							if(valorEntregado>=minimoPagar && valorEntregado>0 && deuda==0 && interes==minimoPagar) //solo notas de debito
							{
								valorEntregado=valorEntregado-interes;
								
								tab_abono.insertar();
								tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
						        tab_abono.setValor("nro_documento_facob",txt_documento.getValue().toString());
						        tab_abono.setValor("ide_retip",com_forma_pago.getValue().toString());
						        tab_abono.setValor("ide_tecaj",ide_caja);
						        tab_abono.setValor("ide_sucu",ide_sucursal);
						        tab_abono.setValor("ide_gtemp",ide_empleado);
						        tab_abono.setValor("ide_fafac",tab_recaudacion.getValor(i,"ide_fafac"));
						        tab_abono.setValor("ide_fanod",tab_recaudacion.getValor(i,"ide_fanod"));
						        tab_abono.setValor("ide_prcon",tab_recaudacion.getValor(i,"ide_prcon"));
						        tab_abono.setValor("valor_cobro_facob",pckUtilidades.CConversion.CDbl_2(abono)+"");
						        tab_abono.setValor("valor_cobro_interes_facob",interes+"");
						        tab_abono.setValor("valor_cobro_iva_facob",valorIva+"");
						        tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
						        tab_abono.setValor("observaciones_facob", txt_observacion.getValue().toString());

						        cobre=true;
					        
								utilitario.getConexion().ejecutarSql("update fac_nota_debito set ide_coest="+estado_pagado
										+" ,fecha_actua=now(),fecha_emision_fanod=now(), documento_cobro_fanod='"+txt_documento.getValue().toString()
										+"' where ide_fanod="+tab_recaudacion.getValor(i,"ide_fanod"));

								aceptarDialogoNDelectronica(tab_recaudacion.getValor(i,"ide_fanod")); //Envia al SRI la nota de debito electronica
							}
						}
						
					}
					
					if(!cobre)
					{
						utilitario.agregarMensajeError("NO Cobrado", "El valor entregado debe de al menos cubrir el valor del iva y del interes..");
						return;
					}
					
					if (tab_abono.guardar()){
						guardarPantalla();				
						tab_recaudacion.ejecutarSql();
						tab_abono.ejecutarSql();
						utilitario.addUpdate("tab_recaudacion,tab_abono");
						// cobroManual
						System.out.println("guardaSecuencial abonos: "+par_modulosec_recaudacion);
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion), par_modulosec_recaudacion);
					}
					
					String sql="";
					
					if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==2)
					{
						for(int j=0;j<tab_recaudacion.getTotalFilas();j++){

							sql=" update pre_contrato set activo_prcon=true, ide_coest="+(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"valor"))==0 ? estado_pagado:estado_abonada)
									+ " ,fecha_anticipo_prcon=now(),monto_anticipo_prcon="+pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"abonada"))
									+ " from (select ide_prcon, nro_documento_facob from fac_cobro where ide_prcon="+tab_recaudacion.getValor(j,"ide_prcon")
									+" and nro_documento_facob="+pckUtilidades.CConversion.CStr(txt_documento.getValue())+") a "
									+" where a.ide_prcon=pre_contrato.ide_prcon ;";
							System.out.println("act contratos: "+sql);
							utilitario.getConexion().ejecutarSql(sql);
	
						}
					}
					else
					{
						for(int j=0;j<tab_recaudacion.getTotalFilas();j++){
							if(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"total_fafac"))>0)
							{
								sql=" update fac_factura set ide_coest="+(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"valor"))==0 ? estado_pagado:estado_abonada)
										+ (pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"valor"))==0 ? " ,fecha_pago_fafac=now(),conciliado_fafac=true, documento_conciliado_fafac='"+pckUtilidades.CConversion.CStr(txt_documento.getValue())+"' ":" ,iva_cobrado_fafac=true ")
										+ " from (select ide_fafac, nro_documento_facob from fac_cobro where ide_fafac="+tab_recaudacion.getValor(j,"ide_fafac")
										+" and nro_documento_facob="+pckUtilidades.CConversion.CStr(txt_documento.getValue())+") a "
										+" where a.ide_fafac=fac_factura.ide_fafac ;";
								
								utilitario.getConexion().ejecutarSql(sql);
							}
						}
					}
					
					//AQUI ABRE EL REPORTE
		            Map p_parametros = new HashMap();
		            p_parametros.put("titulo", "EMGIRS - EP");
		            p_parametros.put("p_cliente_ruc", pckUtilidades.CConversion.CStr(aut_cliente.getValor()));
		            p_parametros.put("p_cliente_cobro", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
		            p_parametros.put("p_nro_comprobante", pckUtilidades.CConversion.CInt(txt_documento.getValue()));
		            p_parametros.put("p_observacion", pckUtilidades.CConversion.CStr(txt_observacion.getValue())); 
		            p_parametros.put("p_ide_tecaj", pckUtilidades.CConversion.CInt(ide_caja));
		            p_parametros.put("p_ide_gtemp", pckUtilidades.CConversion.CInt(ide_empleado));
		            p_parametros.put("p_recibido", pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue()));
		            p_parametros.put("p_saldo_i", valor_pendiente);
		            p_parametros.put("p_vuelto", vuelto);
		            
		            p_parametros.put("REPORT_LOCALE", locale);

		            vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos_tk.jasper", p_parametros);
		            vpdf_pago.dibujar();
		            utilitario.addUpdate("vpdf_pago");
		            
		            txt_documento.limpiar();
		    		txt_cliente.limpiar();
		    		txt_total.limpiar();
		    		txt_cheque.limpiar();
		    		txt_observacion.limpiar();
		    		txt_valor_entregado.setValue("0.00");

		    		actualizaDeudaCliente();
		    		utilitario.addUpdate("txt_documento,txt_cliente,txt_total,txt_valor_entregado,txt_observacion,txt_cheque");
					utilitario.agregarMensaje("Cobrado", "Se cobro con exito, recuerde imprimir su comprobante");
					System.out.println("Cobrado.... favor esperar a que el funcionario imprima el comprobante....");
				}
				else
				{
					utilitario.agregarMensajeError("NO Cobrado", "Secuencial ya utilizado, si esta haciendo abonos parciales favor usar el boton Re-Imprimir.");
				}
			}
			else
			{
				utilitario.agregarMensajeError("NO Cobrado", "Si esta usando cobro aleatorio, Use clic derecho, opción guardar y luego Re-Imprimir.");
			}
		}
		
		
	}
	
	public void reimprime()
	{
		Locale locale=new Locale("es","ES");
		//double valorPendiente=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
		
		//AQUI ABRE EL REPORTE
        Map p_parametros = new HashMap();
        p_parametros.put("titulo", "EMGIRS - EP");
        p_parametros.put("p_cliente_ruc", pckUtilidades.CConversion.CStr(aut_cliente.getValor()));
        p_parametros.put("p_cliente_cobro", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
        p_parametros.put("p_nro_comprobante", pckUtilidades.CConversion.CInt(txt_documento.getValue()));
        p_parametros.put("p_observacion", pckUtilidades.CConversion.CStr(txt_observacion.getValue())); 
        p_parametros.put("p_ide_tecaj", pckUtilidades.CConversion.CInt(ide_caja));
        p_parametros.put("p_ide_gtemp", pckUtilidades.CConversion.CInt(ide_empleado));
        p_parametros.put("p_recibido", pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue()));
        p_parametros.put("p_saldo_i", valor_pendiente);
        p_parametros.put("p_vuelto", vuelto);
        p_parametros.put("REPORT_LOCALE", locale);
        //vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos.jasper", p_parametros);
        vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos_tk.jasper", p_parametros);
        vpdf_pago.dibujar();
        utilitario.addUpdate("vpdf_pago");
	}
	
	// Abre el diálogo con la respuesta del core de facturación (NOTA DE DEBITO)
	public void aceptarDialogoNDelectronica(String ide_fanod) {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();
		
		String numeroNotaDebito="";
		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			
			numeroNotaDebito=ser_facturacion.actualizarNumeroNotaDebito(ide_fanod);
			
			respuestaAutorizacion = servicio.procesarNotaDebitoElectronica(ambiente, numeroNotaDebito,autorizar);

			respuestaCabecera = respuestaAutorizacion.get(0);

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replace("Recepcion: ", "");
			
		} catch (Exception ex) {
			System.out.println("Error al autorizar aceptarDialogoNDelectronica: "+ex.getMessage());
		}
		utilitario.agregarNotificacionInfo("MENSAJE - NOTA DEBITO ELECTRÓNICA - SRI",respuestaMensaje);
	}

	
	public String obtenerCaja(){
		String caja="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_tecaj from fac_usuario_lugar where ide_usua ="+utilitario.getVariable("IDE_USUA")+" and activo_fausl=true and recauda_fausl =true limit 1");	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			caja=String.valueOf(list_sql1.get(0));
		}
		return caja;
	}
	
	public String obtenerEmpleado(){
		String empleado="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_gtemp from sis_usuario where ide_usua= "+utilitario.getVariable("IDE_USUA"));	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			empleado=String.valueOf(list_sql1.get(0));
		}
		return empleado;
	}
	
	public String obtenerSucursal(){
		String sucursal="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_sucu from sis_usuario_sucursal where ide_usua= "+utilitario.getVariable("IDE_USUA"));	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			sucursal=String.valueOf(list_sql1.get(0));
		}
		return sucursal;
	}
	
	public String obtenerLugarCobro(){
		String lugar="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_falug from fac_usuario_lugar where ide_usua ="+utilitario.getVariable("IDE_USUA")+" and activo_fausl=true and recauda_fausl =true limit 1");	
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			lugar=String.valueOf(list_sql1.get(0));
		}
		return lugar;
	}
	
	public boolean validaDocumentoCobro(String nro_documento){
		boolean existe=false;
		List list_sql1 = utilitario.getConexion().consultar("select ide_facob FROM fac_cobro fa WHERE nro_documento_facob = "+nro_documento);	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			existe=true;
		}
		return existe;
	}
	

	public void calculoTotal(){

		double valor_total=0;
		verAbono(-1);
		if (tab_recaudacion.getSeleccionados() != null) {
			if(tab_recaudacion.getSeleccionados().length>0){
				for(int i=0;i<tab_recaudacion.getSeleccionados().length;i++){
					valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"valor"));
					valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"interes"));
					if(tab_recaudacion.getSeleccionados().length==1)
					{
						verAbono(tab_recaudacion.getSeleccionados()[i].getIndice());
					}
				}
				
			} 
		}
		txt_total.setValue(utilitario.getFormatoNumero(valor_total,2));
		utilitario.addUpdate("txt_total");
	}
	
	public void verAbono(int indice)//(SelectEvent evt)
	{
		//System.out.println(" indice "+indice);
		//tab_recaudacion.seleccionarFila(evt);
		tab_abono.setCondicion("ide_fafac=-1");
		
		if(indice >= 0)
		{
			if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==1)
			{
				tab_abono.setCondicion("ide_fafac="+tab_recaudacion.getValor(indice,"ide_fafac"));
			}
			
			if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==2)
			{
				tab_abono.setCondicion("ide_prcon="+tab_recaudacion.getValor(indice,"ide_prcon"));
			}
		}
		tab_abono.ejecutarSql();
		utilitario.addUpdate("tab_abono");
	}
	
	private int obtenerIndiceSeleccionado()
	{
		int indice=-1;
		if(tab_recaudacion.getSeleccionados().length==1)
		{
			indice=tab_recaudacion.getSeleccionados()[0].getIndice();
		}
		return indice;
	}
	
	public void actualizaDeudaCliente(){
		
		if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==1)
		{
			tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos(aut_cliente.getValor()));
			tab_recaudacion.ejecutarSql();		
			txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
			utilitario.addUpdate("tab_recaudacion,txt_documento");
		}
		
		if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==2)
		{
			tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesContratos(aut_cliente.getValor()));
			tab_recaudacion.ejecutarSql();		
			txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
			utilitario.addUpdate("tab_recaudacion,txt_documento");
		}
		
		valor_pendiente=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getSumaColumna("valor"))+pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getSumaColumna("interes"));
		//eti_valor_pendiente.setValue(utilitario.getFormatoNumero(valor_pendiente, 2));
		//utilitario.addUpdate("eti_valor_pendiente");
		vuelto=0;
		cobroManual=false;
		seleccionarTodas();
	}
	
	public void seleccionarTodas() {
		tab_recaudacion.setSeleccionados(null);
		Fila seleccionados[] = new Fila[tab_recaudacion.getTotalFilas()];
		for (int i = 0; i < tab_recaudacion.getFilas().size(); i++) {
			seleccionados[i] = tab_recaudacion.getFilas().get(i);
		}
		tab_recaudacion.setSeleccionados(seleccionados);
		calculoTotal();
	}
	
	public void seleccinarInversa() {
		if (tab_recaudacion.getSeleccionados() == null) {
			seleccionarTodas();
		} else if (tab_recaudacion.getSeleccionados().length == tab_recaudacion.getTotalFilas()) {
			seleccionarNinguna();
		} else {
			Fila seleccionados[] = new Fila[tab_recaudacion.getTotalFilas() - tab_recaudacion.getSeleccionados().length];
			int cont = 0;
			for (int i = 0; i < tab_recaudacion.getFilas().size(); i++) {
				boolean boo_selecionado = false;
				for (int j = 0; j < tab_recaudacion.getSeleccionados().length; j++) {
					if (tab_recaudacion.getSeleccionados()[j].equals(tab_recaudacion.getFilas().get(i))) {
						boo_selecionado = true;
						break;
					}
				}
				if (boo_selecionado == false) {
					seleccionados[cont] = tab_recaudacion.getFilas().get(i);
					cont++;
				}
			}
			tab_recaudacion.setSeleccionados(seleccionados);
		}
		calculoTotal();
	}

	public void seleccionarNinguna() {
		tab_recaudacion.setSeleccionados(null);
		txt_total.setValue(utilitario.getFormatoNumero(0,2));
		utilitario.addUpdate("txt_total");
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		txt_documento.limpiar();
		txt_cliente.limpiar();
		txt_total.limpiar();
		txt_valor_entregado.limpiar();
		txt_valor_entregado.setValue("0.00");
		txt_observacion.limpiar();
		txt_cheque.limpiar();
		com_forma_cobro.setValue("1");
		com_forma_pago.setValue("4");
		eti_devolucion.setValue("0.00");
		valor_pendiente=0;
		vuelto=0;
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos("-1"));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,com_forma_pago,txt_cheque,aut_cliente,txt_documento,txt_cliente,txt_total,txt_valor_entregado,eti_devolucion,txt_observacion");
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		utilitario.agregarMensajeInfo("No se puede guardar el cobro", "Use el boton COBRAR.");
		return;
		
		/*int indice = obtenerIndiceSeleccionado();
		
		if(indice < 0)
		{
			utilitario.agregarMensajeInfo("No se puede cobrar", "Seleccione un registro para continuar...");
			return;
		}
		
		double valorEntregado=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
		double interes=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"interes"));
		double valorIva=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"valor_iva"));
		
		utilitario.agregarMensajeInfo("Monto Minimo", "El valor minimo a pagar es: "+(interes+valorIva));
		
		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No esta autorizado para el cobro, favor contacte al administrador.");
			return;
		}

		if(aut_cliente.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un cliente asignado");
			return;
		}
		
		if(ide_empleado==null || ide_empleado==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un recaudador asignado");
			return;
		}
		
		if(txt_cliente.getValue()==null || txt_cliente.getValue().toString().length()==0){
			utilitario.agregarMensajeError("Seleccion", "Ingrese un cliente de cobro");
			return;
		}

		if(txt_documento.getValue().toString()==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "Ingrese el nro. de documento de cobro para poder recaudar");
			return;
		}
		
		if(valorEntregado<=0){
			utilitario.agregarMensajeError("Seleccion", "Ingrese un valor de cobro");
			return;
		}
		else
		{
			if(valorEntregado < (interes+valorIva))
			{
				utilitario.agregarMensajeError("Seleccion", "Ingrese un valor de cobro mayor o igual al interes generado mas el IVA de la factura.");
				return;
			}
			else
				if( valorEntregado > (interes + pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"valor"))) )
				{
					utilitario.agregarMensajeError("Seleccion", "Ingrese un valor de cobro menor o igual al monto a pagar");
					return;
				}
		}
		
		tab_abono.insertar();
		tab_abono.setValor("cliente_pago_facob", txt_cliente.getValue().toString());
        tab_abono.setValor("nro_documento_facob",txt_documento.getValue().toString());
        tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
        tab_abono.setValor("ide_retip",com_forma_pago.getValue().toString());
        tab_abono.setValor("ide_tecaj",ide_caja);
        tab_abono.setValor("ide_sucu",ide_sucursal);
        tab_abono.setValor("ide_gtemp",ide_empleado);
        tab_abono.setValor("ide_fafac",tab_recaudacion.getValor(indice,"ide_fafac"));
        tab_abono.setValor("ide_fanod",tab_recaudacion.getValor(indice,"ide_fanod"));
        tab_abono.setValor("ide_prcon",tab_recaudacion.getValor(indice,"ide_prcon"));
        tab_abono.setValor("valor_cobro_facob",pckUtilidades.CConversion.CDbl_2(valorEntregado - interes - valorIva)+"");
        tab_abono.setValor("valor_cobro_interes_facob",interes+"");
        tab_abono.setValor("valor_cobro_iva_facob",valorIva+"");
        tab_abono.setValor("observaciones_facob", txt_observacion.getValue().toString());
        cobroManual=true;
        */
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		utilitario.agregarMensajeInfo("No se puede guardar el cobro", "Use el boton COBRAR.");
		return;
		
		/*
		if(tab_abono.isFocus() && tab_abono.isFilaInsertada())
		{
			int indice = obtenerIndiceSeleccionado();
			
			if(indice < 0)
			{
				utilitario.agregarMensajeInfo("No se puede guardar el cobro", "Seleccione un registro para continuar...");
				return;
			}
			
			double valorEntregado=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
			double minimoPagar=0;
			double valorIva=0;
			double interes=0;
			double deuda=0;
	
			String estado_pagado=utilitario.getVariable("p_factura_pagado");
			String estado_abonada=utilitario.getVariable("p_factura_abonada");

			interes=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"interes"));
			deuda=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"valor"));
			valorIva=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(indice,"valor_iva"));
			minimoPagar=interes+valorIva;
			deuda += interes;
			
			if(valorEntregado>=minimoPagar && valorEntregado <= deuda)
			{
				
				
				if(pckUtilidades.CConversion.CInt(com_forma_cobro.getValue())==2)
				{
		        	AbonoContrato objAbContrato = new AbonoContrato();
		        	objAbContrato.setIdeRecli(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(indice,"ide_recli")));
		        	objAbContrato.setMonto((float)pckUtilidades.CConversion.CDbl_2(valorEntregado));
		        	objAbContrato.setRucComercial(tab_recaudacion.getValor(indice,"ruc_comercial_recli"));
		        	objAbContrato.setNumeroContrato(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(indice,"secuencial_fafac")));
		        	objAbContrato.setTipoContrato(2);
		        	objAbContrato.setTipoPago(pckUtilidades.CConversion.CInt(com_forma_pago.getValue()));
		        	objAbContrato.setNumeroComprobantePago(pckUtilidades.CConversion.CStr(txt_documento.getValue()));
		        	
		        	RespuestaContrato resp=servicio.procesarAbonoContrato(objAbContrato);
		        	
		        	if(!resp.isValid())
		        	{
		        		utilitario.agregarMensaje("Respuesta Servicio", resp.getMessage());
		        		return;
		        	}
		        	
				}
				
				
		        if(pckUtilidades.CConversion.CInt(tab_recaudacion.getValor(indice,"ide_fanod")) > 0){
		        	tab_abono.setValor("ide_fanod",tab_recaudacion.getValor(indice,"ide_fanod"));
		        	
					utilitario.getConexion().ejecutarSql("update fac_nota_debito set ide_coest="+estado_pagado
							+" ,fecha_actua=now(),fecha_emision_fanod=now(), documento_cobro_fanod='"+txt_documento.getValue().toString()
							+"' where ide_fanod="+tab_recaudacion.getValor(indice,"ide_fanod"));
					
					if(valorEntregado < deuda)
					{
						utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest=2, fecha_vencimiento_fafac='"+utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual())
								+"' ,observacion_fafac=observacion_fafac||' nota de debito cancelada pero aun existe saldo que pagar.' where ide_fafac="+tab_recaudacion.getValor(indice,"ide_fafac"));
					}
					
					aceptarDialogoNDelectronica(tab_recaudacion.getValor(indice,"ide_fanod")); //Envia al SRI la nota de debito electronica
				}
			}
			
			if (tab_abono.guardar()){
				guardarPantalla();				
				tab_recaudacion.ejecutarSql();
				tab_abono.ejecutarSql();

				utilitario.addUpdate("tab_recaudacion,tab_abono");

				if(!validaDocumentoCobro(txt_documento.getValue().toString()))
				{
					System.out.println("guardaSecuencial2 abonos: "+par_modulosec_recaudacion);
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion), par_modulosec_recaudacion); //TODO validar el secuencial de las recaudaciones ojo
				}
			}
			
			String sql="";
			for(int i=0;i<tab_recaudacion.getTotalFilas();i++){
				
				//if(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor"))==0)
				//{
					//utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest="+(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor"))==0 ? estado_pagado:estado_abonada)
					//		+",fecha_pago_fafac=now(),conciliado_fafac=true where ide_fafac="+tab_recaudacion.getValor(i,"ide_fafac"));
				//}
				
				sql=" update fac_factura set ide_coest="+(pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor"))==0 ? estado_pagado:estado_abonada)
						+ (pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(i,"valor"))==0 ? " ,fecha_pago_fafac=now(),conciliado_fafac=true ":" ,iva_cobrado_fafac=true ")
						+ " from (select ide_fafac, nro_documento_facob from fac_cobro where ide_fafac="+tab_recaudacion.getValor(i,"ide_fafac")
						+" and nro_documento_facob="+pckUtilidades.CConversion.CStr(txt_documento.getValue())+") a "
						+" where a.ide_fafac=fac_factura.ide_fafac ;";
				
				utilitario.getConexion().ejecutarSql(sql);
			}

			utilitario.agregarMensaje("Cobrado", "Se cobro con exito, recuerde imprimir su comprobante");

		}
		*/
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

	public AutoCompletar getAut_caja() {
		return aut_caja;
	}

	public void setAut_caja(AutoCompletar aut_caja) {
		this.aut_caja = aut_caja;
	}

	public AutoCompletar getAut_sucursal() {
		return aut_sucursal;
	}

	public void setAut_sucursal(AutoCompletar aut_sucursal) {
		this.aut_sucursal = aut_sucursal;
	}

	public AutoCompletar getAut_lugar_cobro() {
		return aut_lugar_cobro;
	}

	public void setAut_lugar_cobro(AutoCompletar aut_lugar_cobro) {
		this.aut_lugar_cobro = aut_lugar_cobro;
	}

	public Tabla getTab_abono() {
		return tab_abono;
	}

	public void setTab_abono(Tabla tab_abono) {
		this.tab_abono = tab_abono;
	}

	public Combo getCom_forma_cobro() {
		return com_forma_cobro;
	}

	public void setCom_forma_cobro(Combo com_forma_cobro) {
		this.com_forma_cobro = com_forma_cobro;
	}

	

}
