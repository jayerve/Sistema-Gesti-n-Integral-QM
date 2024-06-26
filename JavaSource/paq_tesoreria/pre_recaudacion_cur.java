package paq_tesoreria;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.Fila;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import grandesclientes.AbonoContrato;
import grandesclientes.RespuestaContrato;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import pck_cliente.servicio;

public class pre_recaudacion_cur extends Pantalla{
	
	private Dialogo dia_cobro= new Dialogo();
	private Tabla tab_cur = new Tabla();
	private Tabla tab_pagos=new Tabla();
	private Tabla tab_recaudacion = new Tabla();
	private Tabla tab_abono=new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();

	private AutoCompletar aut_caja= new AutoCompletar();
	private Texto txt_total = new Texto();
	private Texto txt_documento= new Texto();
	private Texto txt_valor_entregado = new Texto();
	private AreaTexto txt_observacion = new AreaTexto();
	private Combo com_forma_pago = new Combo();
	private Etiqueta eti_total = new Etiqueta();
	private Confirmar con_guardar=new Confirmar();
    private VisualizarPDF vpdf_pago = new VisualizarPDF();
    private VisualizarPDF vpdf_pago_reimprimir = new VisualizarPDF();
    private int ambiente = 1; //1 Test: 2 Produccion
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

	public pre_recaudacion_cur(){
		
		ambiente=Integer.parseInt(utilitario.getVariable("p_ambiente_sri"));
		par_modulosec_recaudacion=utilitario.getVariable("p_modulo_secuencial_recaudacion");
		
		//bar_botones.limpiar();
		bar_botones.getBot_eliminar().setRendered(false);
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
		
    	//FORMA DE PAGO
    	com_forma_pago.setCombo("select ide_retip,detalle_retip from rec_tipo where activo_retip =true");
    	com_forma_pago.setValue("10");
    	com_forma_pago.setStyle("width: 200px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta("Forma de Pago:"));
    	bar_botones.agregarComponente(com_forma_pago);
    	
		Boton bot_pagar=new Boton();
		bot_pagar.setIcon("ui-icon-print");
		bot_pagar.setValue("COBRAR");
		bot_pagar.setMetodo("pagado");
		bar_botones.agregarBoton(bot_pagar);
    	
  	    ////////
    	tab_cur.setId("tab_cur");
    	tab_cur.setTipoFormulario(true);
    	tab_cur.getGrid().setColumns(4);
    	tab_cur.setTabla("fac_cur_pagos","ide_facur",1);	
    	tab_cur.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
    	tab_cur.getColumna("fecha_registro_facur").setValorDefecto(utilitario.getFechaActual());
    	tab_cur.getColumna("entidad_facur").setMascara("999-9999");
    	tab_cur.getColumna("archivo_facur").setUpload("cur_pagos");
    	tab_cur.getColumna("fecha_registro_facur").setLectura(true);
    	tab_cur.getColumna("ruc_cliente_facur").setLectura(true);
    	tab_cur.getColumna("procesado_facur").setLectura(true);
    	tab_cur.getColumna("procesado_facur").setValorDefecto("false");
    	tab_cur.getColumna("activo_facur").setValorDefecto("true");
    	tab_cur.getColumna("nro_cur_facur").setValorDefecto("0");
    	tab_cur.getColumna("cuenta_monetaria_facur").setValorDefecto("0");
    	tab_cur.getColumna("valor_acreditado_facur").setValorDefecto("0");
    	tab_cur.setCondicion("ide_facur=-1");
    	tab_cur.dibujar();
    	
    	PanelTabla pat_cur_pagos=new PanelTabla();
    	pat_cur_pagos.setMensajeWarn("DETALLE CUR PAGOS");
    	pat_cur_pagos.setPanelTabla(tab_cur);
    	
    	ItemMenu itm_recibo = new ItemMenu();
		itm_recibo.setValue("Ver Recibo");
		itm_recibo.setMetodo("reimprime");
		itm_recibo.setIcon("ui-icon-print");
		
    	tab_pagos.setId("tab_pagos");   	
    	tab_pagos.setSql(ser_tesoreria.getComprobantesCUR("-1"));
    	tab_pagos.setLectura(true);
    	tab_pagos.getColumna("ide_facur").setVisible(false);
    	tab_pagos.setColumnaSuma("cobro");    
    	tab_pagos.setRows(25);
    	tab_pagos.dibujar();
    	
    	PanelTabla pat_pagos=new PanelTabla();
    	pat_pagos.setMensajeWarn("DETALLE COMPROBANTES");
    	pat_pagos.setPanelTabla(tab_pagos);
    	pat_pagos.getMenuTabla().getChildren().add(itm_recibo);
    	   	
    	eti_total.setId("eti_total");
		eti_total.setStyle("font-size:18px;color:red;widht:80%");
		eti_total.setValue("TOTAL : 0.00");
		Division div_aux = new Division();
		div_aux.setFooter(pat_pagos, eti_total, "80%");
		
    	Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_cur_pagos,div_aux,"55%","H");
		agregarComponente(div_division);
		
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
		
        // DOCUMENTO PAGO
    	Etiqueta eti_documento = new Etiqueta("NRO DOCUMENTO COBRO:");
    	eti_documento.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_documento);
		txt_documento.setId("txt_documento");
       	txt_documento.setStyle("width:100px;text-aling:left");	
       	txt_documento.setValue("0");
       	txt_documento.setDisabled(true);
        gri_formulario.getChildren().add(txt_documento);
        
        // TOTAL COBRAR
        Etiqueta eti_titulo = new Etiqueta("MONTO COMPROBANTES:");
        eti_titulo.setStyle("font-size: 17px;color: red;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_titulo);
		txt_total.setId("txt_total");
		txt_total.setValue("0.00");
		txt_total.setDisabled(true);
		txt_total.setStyle("font-size: 14px;color: red;font-weight: bold;width:100px;text-aling:left");		
        gri_formulario.getChildren().add(txt_total);
        
        // TOTAL VALOR ENTREGADO
    	Etiqueta eti_valor_entregado = new Etiqueta("MONTO CUR:");
    	eti_valor_entregado.setStyle("font-size: 17px;color: blue;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_entregado);
		txt_valor_entregado.setId("txt_valor_entregado");
		txt_valor_entregado.setValue("0.00");
		txt_valor_entregado.setDisabled(true);
		txt_valor_entregado.setStyle("width:100px;text-aling:left");		
        gri_formulario.getChildren().add(txt_valor_entregado);
        
        // OBSERVACIONES
    	Etiqueta eti_observacion = new Etiqueta("OBSERVACIONES:");
    	eti_observacion.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_observacion);
       	txt_observacion.setId("txt_observacion");
       	txt_observacion.setValue("");
       	txt_observacion.setStyle("width:350px;text-aling:left");		
        gri_formulario.getChildren().add(txt_observacion);
            	
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
		tab_recaudacion.onSelectCheck("calculoTotal");
		tab_recaudacion.onUnselectCheck("calculoTotal");
		tab_recaudacion.setLectura(true);
		tab_recaudacion.setTipoSeleccion(true);
        tab_recaudacion.setRows(10);
		tab_recaudacion.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(boc_seleccion_inversa);
        pat_panel.setPanelTabla(tab_recaudacion);
        
        Grid gri_formulario_master = new Grid();
        gri_formulario_master.setColumns(1);
        gri_formulario_master.getChildren().add(gri_formulario);
        gri_formulario_master.getChildren().add(pat_panel);
        
        tab_abono.setId("tab_abono");
        tab_abono.setTabla("fac_cobro", "ide_facob", 4);
        tab_abono.setCondicion("ide_fafac=-1");
        tab_abono.setGenerarPrimaria(false);
        //tab_abono.getColumna("ide_facob").setLectura(true);
        //tab_abono.getColumna("ide_fafac").setVisible(false);
        //tab_abono.getColumna("ide_fanod").setVisible(false);
        tab_abono.getColumna("fecha_cobro_facob").setValorDefecto(utilitario.getFechaActual());
        tab_abono.getColumna("fecha_cobro_facob").setLectura(true);
        tab_abono.getColumna("valor_cobro_interes_facob").setValorDefecto("0.00");
        tab_abono.getColumna("valor_cobro_facob").setValorDefecto("0.00");
        tab_abono.getColumna("valor_cobro_iva_facob").setValorDefecto("0.00");
        tab_abono.getColumna("activo_facob").setValorDefecto("true");       
        tab_abono.dibujar();
		
        gri_formulario_master.getChildren().add(tab_abono);
        
		dia_cobro.setId("dia_cobro");
        dia_cobro.setTitle("PROCESANDO COBRO DEL CUR");
        dia_cobro.setWidth("70%");
        dia_cobro.setHeight("70%");
        dia_cobro.setDialogo(gri_formulario_master);
        dia_cobro.getBot_aceptar().setMetodo("pagado");      	
       	agregarComponente(dia_cobro);
       	
       	con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
		con_guardar.setTitle("CONFIRMACION DE COBRO");

		agregarComponente(con_guardar);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago);
        
        vpdf_pago_reimprimir.setId("vpdf_pago_reimprimir");
        vpdf_pago_reimprimir.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago_reimprimir);
        
	}
	
	public void actualizaDeudaCliente(){
		
		tab_cur.setCondicion(" ruc_cliente_facur like '"+aut_cliente.getValor()+"' ");
		tab_cur.ejecutarSql();		
		
		tab_pagos.setSql(ser_tesoreria.getComprobantesCUR(tab_cur.getValor("ide_facur")));
		tab_pagos.ejecutarSql();	

		double monto=pckUtilidades.CConversion.CDbl_2(tab_pagos.getSumaColumna("cobro"));
		eti_total.setValue("TOTAL : " + utilitario.getFormatoNumero(monto, 2));
		utilitario.addUpdate("tab_cur,tab_pagos,eti_total");
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_cur.limpiar();
		tab_pagos.limpiar();
		utilitario.addUpdate("aut_cliente,tab_cur,tab_pagos");
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
	
	public void calculoTotal(){

		double valor_total=0;	
		if (tab_recaudacion.getSeleccionados() != null) {
			if(tab_recaudacion.getSeleccionados().length>0){
				for(int i=0;i<tab_recaudacion.getSeleccionados().length;i++){
					valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"valor"));
					valor_total +=pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(tab_recaudacion.getSeleccionados()[i].getIndice(),"interes"));
				}
				
			} 
		}
		txt_total.setValue(utilitario.getFormatoNumero(valor_total,2));
		utilitario.addUpdate("txt_total");
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

	public boolean validar_requisitos()
	{

		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede registrar", "No esta autorizado para el cobro, favor contacte al administrador.");
			return false;
		}

		if(aut_cliente.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede registrar", "No existe un cliente seleccionado");
			return false;
		}
		
		if(ide_empleado==null || ide_empleado==""){
			utilitario.agregarMensajeInfo("No se puede registrar", "No existe un recaudador asignado");
			return false;
		}
		
		return true;
	}

	public void pagado(){
		
		
		if(dia_cobro.isVisible()){

			if(tab_recaudacion.getTotalFilas()<1){
				utilitario.agregarMensajeInfo("No se puede cobrar", "No existen cobros pendientes");
				return;
			}
			
			if(pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue())<=pckUtilidades.CConversion.CDbl_2(txt_total.getValue()))
			{
				utilitario.agregarMensajeError("No se puede Cobrar", "El Monto del CUR es Inferior al Monto de los Comprobantes.");
				return;
			}	
			
			con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
			con_guardar.setTitle("CONFIRMACION DE COBRO");
			con_guardar.getBot_aceptar().setMetodo("procesar_cobro");
			con_guardar.getBot_cancelar().setMetodo("cerrar");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
	
		}
		else 
		{
			System.out.println("Cobrando.... favor espere....");
			
			if(tab_cur.getFilas().size()<1){
				utilitario.agregarMensajeInfo("Error", "Registre el CUR de Pago antes de continuar.");
				return;
			}
			
			if(tab_cur.getValor("ide_facur")==null){
				utilitario.agregarMensajeError("No se puede Cobrar", "Debe Guardar el CUR de Pagos antes de continuar.");
				return;
			}					
			
			if (tab_cur.guardar()){
				guardarPantalla();
			}
			
			if(tab_cur.getValor("procesado_facur").equals("true")){
				utilitario.agregarMensajeInfo("Registro no Editable", "El CUR de PAGO se encuentra procesado.");
				return;
			}
			
			if(pckUtilidades.CConversion.CDbl_2(tab_cur.getValor("valor_acreditado_facur"))==0){
				utilitario.agregarMensajeError("No se puede Cobrar", "El CUR de Pagos no tiene un valor acreditado.");
				return;
			}
			
			validar_requisitos();
			
			tab_recaudacion.setSql(ser_tesoreria.getSqlDeudaClientesAbonos(aut_cliente.getValor()));
			tab_recaudacion.ejecutarSql();		
			txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
			txt_valor_entregado.setValue(tab_cur.getValor("valor_acreditado_facur"));
			txt_observacion.setValue(pckUtilidades.CConversion.CStr(tab_cur.getValor("observaciones_facur")));
						
			utilitario.addUpdate("tab_recaudacion,txt_documento,txt_valor_entregado");
			dia_cobro.dibujar();
		}
	}
	
	public void cerrar()
	{
		con_guardar.cerrar();
		dia_cobro.cerrar();
		vpdf_pago.cerrar();
	}
	
	public void procesar_cobro()
	{
		con_guardar.cerrar();
				
		Locale locale=new Locale("es","ES");
		
		System.out.println("Procesando Cobro.... favor espere....");
		
		String estado_pagado=utilitario.getVariable("p_factura_pagado");
		String estado_abonada=utilitario.getVariable("p_factura_abonada");
		
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
					
					tab_abono.insertar();
					tab_abono.setValor("ide_facur", tab_cur.getValor("ide_facur"));
					//tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
					tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(tab_cur.getValor("ruc_cliente_facur")).toString().toUpperCase());
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
			        //tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
			        tab_abono.setValor("documento_cobro", tab_cur.getValor("nro_cur_facur"));
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
						tab_abono.setValor("ide_facur", tab_cur.getValor("ide_facur"));
						//tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
						tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(tab_cur.getValor("ruc_cliente_facur")).toString().toUpperCase());
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
				        //tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
				        tab_abono.setValor("documento_cobro", tab_cur.getValor("nro_cur_facur"));
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
				tab_cur.modificar(tab_cur.getFilaActual());
				tab_cur.setValor("procesado_facur", "true");
				tab_cur.guardar();				
				guardarPantalla();	
				tab_recaudacion.ejecutarSql();
				utilitario.addUpdate("tab_cur,tab_pagos,tab_recaudacion");
				// cobroManual
				System.out.println("guardaSecuencial abonos: "+par_modulosec_recaudacion);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion), par_modulosec_recaudacion);
			}
			
			String sql="";

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
			
			System.out.println("Preparando ticket para su impresion...");
			//AQUI ABRE EL REPORTE
            Map p_parametros = new HashMap();
            p_parametros.put("titulo", "EMGIRS - EP");
            p_parametros.put("p_nro_comprobante", pckUtilidades.CConversion.CInt(txt_documento.getValue()));
            p_parametros.put("REPORT_LOCALE", locale);

            vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos_tk_cur.jasper", p_parametros);
            vpdf_pago.getBot_aceptar().setMetodo("cerrar");
            vpdf_pago.getBot_cancelar().setMetodo("cerrar");
            vpdf_pago.dibujar();
            utilitario.addUpdate("vpdf_pago");
            
            tab_recaudacion.limpiar();
            tab_abono.limpiar();
            txt_documento.limpiar();   	
    		txt_total.limpiar();   	
    		txt_observacion.limpiar();
    		//txt_valor_entregado.setValue("0.00");
    		//dia_cobro.cerrar();
    		actualizaDeudaCliente();
    		//utilitario.addUpdate("txt_documento,txt_total,txt_valor_entregado,txt_observacion");
			utilitario.agregarMensaje("Cobrado", "Se cobro con exito, recuerde imprimir su comprobante");
			System.out.println("Cobrado.... favor esperar a que el funcionario imprima el comprobante....");
		}
		else
		{
			utilitario.agregarMensajeError("NO Cobrado", "Secuencial ya utilizado, si esta haciendo abonos parciales favor usar el boton Re-Imprimir.");
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
			System.out.println("Error al autorizar aceptarDialogoNDelectronica: "+ex.getMessage());
		}
		utilitario.agregarNotificacionInfo("MENSAJE - NOTA DEBITO ELECTRÓNICA - SRI",respuestaMensaje);
	}
	
	public void reimprime()
	{
		int nro_documento=pckUtilidades.CConversion.CInt(tab_pagos.getValor("nro_documento_facob"));
		if(nro_documento>0)
		{
			Locale locale=new Locale("es","ES");	
			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_nro_comprobante", nro_documento);
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago_reimprimir.setVisualizarPDF("rep_facturacion/rep_recibo_caja_abonos_tk_cur.jasper", p_parametros);
	        vpdf_pago_reimprimir.dibujar();
	        utilitario.addUpdate("vpdf_pago_reimprimir");
		}
		else
			utilitario.agregarMensajeInfo("Comprobante no Registrado.", "");
	
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		//utilitario.agregarMensajeInfo("No se puede guardar el cobro", "Use el boton COBRAR.");
		if(validar_requisitos())
		{
			//utilitario.getTablaisFocus().insertar();
			tab_cur.insertar();
			tab_cur.setValor("ruc_cliente_facur", aut_cliente.getValor()+"");
			
		}
		//return;		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if(tab_cur.getValor("procesado_facur").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El CUR de PAGO se encuentra procesado.");
			return;
		}
		
		utilitario.agregarMensajeInfo("No se puede guardar el cobro", "Use el boton COBRAR para completar la transacción.");
		
		if (tab_cur.guardar()){
			guardarPantalla();
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_cur() {
		return tab_cur;
	}

	public void setTab_cur(Tabla tab_cur) {
		this.tab_cur = tab_cur;
	}

	public Tabla getTab_pagos() {
		return tab_pagos;
	}

	public void setTab_pagos(Tabla tab_pagos) {
		this.tab_pagos = tab_pagos;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	public Etiqueta getEti_total() {
		return eti_total;
	}

	public void setEti_total(Etiqueta eti_total) {
		this.eti_total = eti_total;
	}

	public Dialogo getDia_cobro() {
		return dia_cobro;
	}

	public void setDia_cobro(Dialogo dia_cobro) {
		this.dia_cobro = dia_cobro;
	}

	public Texto getTxt_total() {
		return txt_total;
	}

	public void setTxt_total(Texto txt_total) {
		this.txt_total = txt_total;
	}

	public Texto getTxt_documento() {
		return txt_documento;
	}

	public void setTxt_documento(Texto txt_documento) {
		this.txt_documento = txt_documento;
	}

	public Texto getTxt_valor_entregado() {
		return txt_valor_entregado;
	}

	public void setTxt_valor_entregado(Texto txt_valor_entregado) {
		this.txt_valor_entregado = txt_valor_entregado;
	}

	public AreaTexto getTxt_observacion() {
		return txt_observacion;
	}

	public void setTxt_observacion(AreaTexto txt_observacion) {
		this.txt_observacion = txt_observacion;
	}

	public Tabla getTab_recaudacion() {
		return tab_recaudacion;
	}

	public void setTab_recaudacion(Tabla tab_recaudacion) {
		this.tab_recaudacion = tab_recaudacion;
	}

	public Combo getCom_forma_pago() {
		return com_forma_pago;
	}

	public void setCom_forma_pago(Combo com_forma_pago) {
		this.com_forma_pago = com_forma_pago;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Tabla getTab_abono() {
		return tab_abono;
	}

	public void setTab_abono(Tabla tab_abono) {
		this.tab_abono = tab_abono;
	}

	public VisualizarPDF getVpdf_pago() {
		return vpdf_pago;
	}

	public void setVpdf_pago(VisualizarPDF vpdf_pago) {
		this.vpdf_pago = vpdf_pago;
	}

	public VisualizarPDF getVpdf_pago_reimprimir() {
		return vpdf_pago_reimprimir;
	}

	public void setVpdf_pago_reimprimir(VisualizarPDF vpdf_pago_reimprimir) {
		this.vpdf_pago_reimprimir = vpdf_pago_reimprimir;
	}

	
	

}
