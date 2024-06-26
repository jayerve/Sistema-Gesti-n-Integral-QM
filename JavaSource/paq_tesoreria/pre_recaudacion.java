package paq_tesoreria;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
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
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import pck_cliente.servicio;

public class pre_recaudacion extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Texto	txt_total = new Texto();
	private Texto	txt_cliente = new Texto();
	private Texto 	txt_documento= new Texto();
	private Texto	txt_valor_entregado = new Texto();
	private Texto	txt_cheque = new Texto();
	private Combo   com_forma_pago = new Combo();
	private Etiqueta eti_devolucion= new Etiqueta();
	private AutoCompletar aut_caja= new AutoCompletar();
	private AutoCompletar aut_recaudador = new AutoCompletar();
	private AutoCompletar aut_sucursal= new AutoCompletar();
	private AutoCompletar aut_lugar_cobro=new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
    private VisualizarPDF vpdf_pago = new VisualizarPDF();
    private int ambiente = 2; //1 Test: 2 Produccion
    private boolean autorizar = true; //true Produccion
    public static String par_modulosec_recaudacion;

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

	public pre_recaudacion(){
		
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		par_modulosec_recaudacion=utilitario.getVariable("p_modulo_secuencial_recaudacion");
		
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
		
		
    	Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
    	
    	// RECAUDADOR
        Etiqueta eti_recaudador = new Etiqueta("RECAUDADOR :");
        eti_recaudador.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_recaudador);
       	aut_recaudador.setId("aut_recaudador");
       	aut_recaudador.setStyle("text-aling:left");
       	aut_recaudador.setAutoCompletar("select ide_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp from gth_empleado");
       	aut_recaudador.setValor(ide_empleado);
       	aut_recaudador.setDisabled(true);
       	gri_formulario.getChildren().add(aut_recaudador);
       	
       	// SUCURSAL
        Etiqueta eti_sucursal = new Etiqueta("SUCURSAL :");
        eti_sucursal.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_sucursal);
       	aut_sucursal.setId("aut_sucursal");
       	aut_sucursal.setStyle("text-aling:left");
       	aut_sucursal.setAutoCompletar("select ide_sucu,nom_sucu from sis_sucursal");
       	aut_sucursal.setValor(ide_sucursal);
       	aut_sucursal.setDisabled(true);

       	gri_formulario.getChildren().add(aut_sucursal);

       	
       	// LUGAR
        Etiqueta eti_lugar = new Etiqueta("LUGAR FACTURACION :");
        eti_lugar.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_lugar);
       	aut_lugar_cobro.setId("aut_lugar_cobro");
       	aut_lugar_cobro.setStyle("text-aling:left");
       	aut_lugar_cobro.setAutoCompletar("select ide_falug,detalle_lugar_falug from fac_lugar");
       	aut_lugar_cobro.setValor(ide_lugar);
       	aut_lugar_cobro.setDisabled(true);

       	gri_formulario.getChildren().add(aut_lugar_cobro);
       	
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
    	Etiqueta eti_documento = new Etiqueta("DOCUMENTO COBRO:");
    	eti_documento.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_documento);
		txt_documento.setId("txt_documento");
		txt_documento.setValue("0");
       	txt_documento.setDisabled(true);
       	txt_documento.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_documento);
        
        // FORMA DE PAGO
    	com_forma_pago.setCombo("select ide_retip,detalle_retip from rec_tipo where activo_retip =true");
    	com_forma_pago.setValue("4");
    	com_forma_pago.setStyle("width: 200px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta("Seleccione La Forma de Pago:"));
    	bar_botones.agregarComponente(com_forma_pago);
    	
    	// DOCUEMNTO CHEQUE
    	Etiqueta eti_cheque = new Etiqueta("NRO CHEQUE:");
    	eti_cheque.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_cheque);
		txt_cheque.setId("txt_cheque");
       	txt_cheque.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_cheque);
       	
       	// TOTAL COBRAR
    	Etiqueta eti_titulo = new Etiqueta("TOTAL COBRAR:");
        eti_titulo.setStyle("font-size: 17px;color: red;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_titulo);
		txt_total.setId("txt_total");
		txt_total.setValue("0.00");
		txt_total.setDisabled(true);
		txt_total.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_total);
        
        
        // TOTAL VALOR ENTREGADO
    	Etiqueta eti_valor_entregado = new Etiqueta("VALOR ENTREGADO:");
    	eti_valor_entregado.setStyle("font-size: 17px;color: blue;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_entregado);
		txt_valor_entregado.setId("txt_valor_entregado");
		txt_valor_entregado.setValue("0.00");
		txt_valor_entregado.setMetodoChange("devolucion");
		txt_valor_entregado.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_valor_entregado);
        
        // TOTAL VALOR DEVOLUCION
    	Etiqueta eti_valor_devolucion = new Etiqueta("VALOR DEVOLVER:");
    	eti_valor_devolucion.setStyle("font-size: 17px;color: blue;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_devolucion);
       	eti_devolucion.setId("eti_devolucion");
       	eti_devolucion.setStyle("font-size: 20px;color: red;font-weight: bold;text-aling:left");		
       	eti_devolucion.setValue("0.00");
		gri_formulario.getChildren().add(eti_devolucion);
        
        //agregarComponente(txt_total);
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
		
		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientes("-1"));
		tab_recaudacion.getColumna("ide_fafac").setVisible(false);
		tab_recaudacion.getColumna("ide_recli").setVisible(false);
		tab_recaudacion.getColumna("grupo").setVisible(false);
		tab_recaudacion.getColumna("fecha_transaccion_fafac").setNombreVisual("FECHA EMISION");
		tab_recaudacion.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_recaudacion.getColumna("detalle_bogrm").setLongitud(100);
		tab_recaudacion.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_recaudacion.getColumna("secuencial_fafac").setLongitud(30);
		tab_recaudacion.getColumna("total_fafac").setNombreVisual("VALOR DEUDA");
		tab_recaudacion.getColumna("interes").setNombreVisual("INTERES GENERADO");
		tab_recaudacion.setColumnaSuma("total_fafac,interes");
		tab_recaudacion.onSelectCheck("calculoTotal");
		tab_recaudacion.onUnselectCheck("calculoTotal");
		tab_recaudacion.setLectura(true);
		tab_recaudacion.setTipoSeleccion(true);
        tab_recaudacion.setRows(25);
		tab_recaudacion.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setHeader(gri_formulario);
        pat_panel.getChildren().add(boc_seleccion_inversa);
        pat_panel.setPanelTabla(tab_recaudacion);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago);
        
        utilitario.agregarMensaje("Mensaje", "Pantalla obsoleta...");
		
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
			txt_valor_entregado.setValue("0");
			eti_devolucion.setValue("0.00");
			utilitario.addUpdate("eti_devolucion,txt_valor_entregado");
			return;
		}
		
	}
	
	public void pagado(){
		Locale locale=new Locale("es","ES");
		
		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No esta autorizado para el cobro, favor contacte al administrador.");
			return;
		}

		if(txt_documento.getValue().toString()==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "Ingrese el nro. de documento de cobro para poder recaudar");
			return;
		}
		if(aut_recaudador.getValue().toString()==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un recaudador asignado");
			return;
		}
		
		String estado_pagado=utilitario.getVariable("p_factura_pagado");
		String ide_caja=aut_caja.getValor();
		String ide_empleado_cobro=aut_recaudador.getValor();
		String ide_sucursal=aut_sucursal.getValor();
		String lugar_trabajo=aut_lugar_cobro.getValor();
		String usuario_pago=txt_cliente.getValue().toString();
		String documento_cobro=txt_documento.getValue().toString();
		String forma_pago=com_forma_pago.getValue().toString();
		String cheque=txt_cheque.getValue().toString();

		if(tab_recaudacion.getSeleccionados().length>0){
			// eliminamos la tabla de cobros para iniciar un nuevo cobro
			utilitario.getConexion().ejecutarSql("delete from fac_cobro where ide_caja="+ide_caja+" and ide_lugar_cobro="+lugar_trabajo);
			
			if (!con_guardar.isVisible()){

				con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
				con_guardar.setTitle("CONFIRMACION DE COBRO");
				con_guardar.getBot_aceptar().setMetodo("pagado");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();
				TablaGenerica tab_recaudador = utilitario.consultar("select ide_gtemp,coalesce(primer_nombre_gtemp,segundo_nombre_gtemp) || ' ' || coalesce(apellido_paterno_gtemp,apellido_materno_gtemp) as recaudador from gth_empleado where ide_gtemp = "+ide_empleado_cobro);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion), par_modulosec_recaudacion);
					
				for(int i=0;i<tab_recaudacion.getSeleccionados().length;i++){
					//System.out.println("  indice "+tab_recaudacion.getSeleccionados()[i].getIndice());
					String coma=",";
					if(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"grupo").equals("1")){
						utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest="+estado_pagado+",ide_tecaj="+ide_caja+",cliente_pago_fafac='"+usuario_pago+"',gth_ide_gtemp="+ide_empleado_cobro+",sis_ide_sucu="+ide_sucursal+",fac_ide_falug="+lugar_trabajo+",fecha_pago_fafac=now(),documento_conciliado_fafac='"+documento_cobro+"',ide_retip="+forma_pago+",documento_bancario_fafac='"+cheque+"',conciliado_fafac=true where ide_fafac="+tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"ide_fafac"));
						utilitario.getConexion().ejecutarSql("INSERT INTO fac_cobro(ide_caja, ide_factura, ide_lugar_cobro, tipo) VALUES ("+ide_caja+", "+tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"ide_fafac")+", "+lugar_trabajo+", "+tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"grupo")+");");
					}
					else{
						String ide_fanod=tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"ide_fafac");
						utilitario.getConexion().ejecutarSql("update fac_nota_debito set ide_coest="+estado_pagado+" ,fecha_actua=now(),fecha_emision_fanod=now(), documento_cobro_fanod='"+documento_cobro+"' where ide_fanod="+ide_fanod);
						//utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest="+estado_pagado+",ide_tecaj="+ide_caja+",cliente_pago_fafac='"+usuario_pago+"',gth_ide_gtemp="+ide_empleado_cobro+",sis_ide_sucu="+ide_sucursal+",fac_ide_falug="+lugar_trabajo+",fecha_pago_fafac=now(),documento_conciliado_fafac='"+documento_cobro+"',ide_retip="+forma_pago+",documento_bancario_fafac='"+cheque+"',conciliado_fafac=true where ide_fafac in (select ide_fafac from fac_detalle_debito where ide_fanod in ("+ide_fanod+"))");
						utilitario.getConexion().ejecutarSql("INSERT INTO fac_cobro(ide_caja, ide_factura, ide_lugar_cobro, tipo) VALUES ("+ide_caja+", "+tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"ide_fafac")+", "+lugar_trabajo+", "+tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"grupo")+");");
						aceptarDialogoNDelectronica(ide_fanod); //Envia al SRI la nota de debito electronica
					}
					
				}
				
				//AQUI ABRE EL REPORTE
	            Map parametros = new HashMap();
	            parametros.put("titulo", "RECIBO DE CAJA");
	            parametros.put("p_recaudador", pckUtilidades.CConversion.CStr(tab_recaudador.getValor("recaudador")));
	            parametros.put("p_cliente_cobro", pckUtilidades.CConversion.CStr(usuario_pago));
	            parametros.put("p_nro_comprobante", pckUtilidades.CConversion.CInt(documento_cobro));
	            
	            parametros.put("pide_fafac", pckUtilidades.CConversion.CInt(ide_caja));
	            parametros.put("plugar_cobro", pckUtilidades.CConversion.CInt(lugar_trabajo));
	            parametros.put("REPORT_LOCALE", locale);
	            //vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_2.jasper", parametros);
	            vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_ticket.jasper", parametros);
	            vpdf_pago.dibujar();
	            utilitario.addUpdate("vpdf_pago");
	            
	            txt_documento.limpiar();
	    		txt_cliente.limpiar();
	    		txt_total.limpiar();
	    		
	    		actualizaDeudaCliente();
				utilitario.agregarMensaje("Cobrado", "Se cobro con exito");
				
			} 
		}
		else {
			utilitario.agregarMensajeInfo("No existen registros seleccionados", "Seleccione un registro o el cliente no posee deudas");	
			return;
		}
		
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
			System.out.println(ex.getMessage());
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
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			lugar=String.valueOf(list_sql1.get(0));
		}
		return lugar;
	}
	
	public void calculoTotal(){
		//System.out.println(" entro al metodo ");
		double valor_total=0;
		if(tab_recaudacion.getSeleccionados().length>0){
			for(int i=0;i<tab_recaudacion.getSeleccionados().length;i++){
				//System.out.println("  indice "+tab_recaudacion.getSeleccionados()[i].getIndice());
				valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"total_fafac"));
				valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"interes"));
				//System.out.println("valor total "+valor_total);
			}
			
		} 
		txt_total.setValue(utilitario.getFormatoNumero(valor_total,2));
		utilitario.addUpdate("txt_total");
	}
	
	public void actualizaDeudaCliente(){
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientes(aut_cliente.getValor()));
		tab_recaudacion.ejecutarSql();
		txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
		//utilitario.addUpdate("tab_recaudacion");
		utilitario.addUpdate("tab_recaudacion,txt_documento");
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		txt_documento.limpiar();
		txt_cliente.limpiar();
		txt_total.limpiar();
		txt_valor_entregado.limpiar();
		txt_cheque.limpiar();
		com_forma_pago.setValue("4");
		eti_devolucion.setValue("0.00");
		tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientes("-1"));
		tab_recaudacion.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,com_forma_pago,txt_cheque,aut_cliente,txt_documento,txt_cliente,txt_total,txt_valor_entregado,eti_devolucion");
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

	/**DFJ**/
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

	/**DFJ**/
	public void seleccionarNinguna() {
		tab_recaudacion.setSeleccionados(null);
		txt_total.setValue(utilitario.getFormatoNumero(0,2));
		utilitario.addUpdate("txt_total");
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

	public AutoCompletar getAut_caja() {
		return aut_caja;
	}

	public void setAut_caja(AutoCompletar aut_caja) {
		this.aut_caja = aut_caja;
	}

	public AutoCompletar getAut_recaudador() {
		return aut_recaudador;
	}

	public void setAut_recaudador(AutoCompletar aut_recaudador) {
		this.aut_recaudador = aut_recaudador;
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

	

}
