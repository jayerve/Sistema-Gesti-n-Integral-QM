package paq_facturacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import pck_cliente.servicio;

public class pre_nota_debito extends Pantalla{
	private Tabla tab_debito=new Tabla ();
	private Tabla tab_detalle_debito=new Tabla ();
	private AutoCompletar aut_cliente=new AutoCompletar();
	private SeleccionTabla set_factura = new SeleccionTabla();
	public static String p_valor_interes_mora_nd;
	public static String p_modulo_facturacion;

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA	
	private VisualizarPDF vpdf_rideND = new VisualizarPDF();
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo nota_debito_elec_dialogo = new Dialogo();
	private int ambiente = 2; //1 Test: 2 Produccion
	private boolean autorizar = true; //true Produccion
	//Fin 30/12/2015 Facturacion Electronica - ABECERRA

	public pre_nota_debito(){
		
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		p_modulo_facturacion=utilitario.getVariable("p_modulo_facturacion");
		
		tab_debito.setId("tab_debito");
		tab_debito.setHeader("NOTA DE DEBITO");
		tab_debito.setTabla("fac_nota_debito", "ide_fanod", 1);
		tab_debito.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", p_modulo_facturacion));
		tab_debito.getColumna("base_imponible_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("base_imponible_fanod").setEtiqueta();
		tab_debito.getColumna("interes_generado_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("interes_generado_fanod").setEtiqueta();
		tab_debito.getColumna("valor_iva_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("valor_iva_fanod").setEtiqueta();
		tab_debito.getColumna("total_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("total_fanod").setEtiqueta();
		tab_debito.getColumna("interes_aplicado_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("interes_aplicado_fanod").setEtiqueta();
		tab_debito.getColumna("fecha_emision_fanod").setValorDefecto(utilitario.getFechaActual());
		tab_debito.getColumna("ide_recli").setVisible(false);
		tab_debito.getColumna("nro_autorizacion_sri_fanod").setLongitud(50);
		tab_debito.getColumna("activo_fanod").setValorDefecto("true");
		tab_debito.setCondicion("ide_recli=-1");
		tab_debito.setTipoFormulario(true);
		tab_debito.getGrid().setColumns(4);
		tab_debito.agregarRelacion(tab_detalle_debito);
		tab_debito.dibujar();
		PanelTabla pat_debito=new PanelTabla();
		pat_debito.setPanelTabla(tab_debito);

		///// DETALLE DEBITO  
		tab_detalle_debito.setId("tab_detalle_debito");
		tab_detalle_debito.setHeader("NOTA DETALLE DEBITO");
		tab_detalle_debito.setTabla("fac_detalle_debito", "ide_faded", 2);
		//tab_detalle_debito.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_detalle_debito.getColumna("ide_fafac").setCombo(ser_facturacion.getCabeceraFactura("0","-1"));
		tab_detalle_debito.getColumna("ide_fafac").setAutoCompletar();
		tab_detalle_debito.getColumna("ide_fafac").setLectura(true);
		tab_detalle_debito.getColumna("base_imponible_faded").setEtiqueta();
		tab_detalle_debito.getColumna("base_imponible_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("interes_generado_faded").setMetodoChange("calcularDetalle");
		//tab_detalle_debito.getColumna("interes_generado_faded").setEtiqueta();
		//tab_detalle_debito.getColumna("interes_generado_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("valor_iva_faded").setEtiqueta();
		tab_detalle_debito.getColumna("valor_iva_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("total_faded").setEtiqueta();
		tab_detalle_debito.getColumna("total_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("interes_aplicado_faded").setEtiqueta();
		tab_detalle_debito.getColumna("interes_aplicado_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("activo_faded").setValorDefecto("true");
		tab_detalle_debito.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle_debito);

		Boton bot_limpiar=new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getClientesDatosBasicos("0,1"));
		aut_cliente.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar

		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);


		Division div_division=new Division();
		div_division.dividir2(pat_debito, pat_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_factura = new Boton();
		bot_factura.setValue("Buscar Factura");
		bot_factura.setTitle("Factura");
		bot_factura.setIcon("ui-icon-person");
		bot_factura.setMetodo("importarFactura");
		bar_botones.agregarBoton(bot_factura);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_facturacion.getCabeceraFactura("0","-1"),"ide_fafac");
		set_factura.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.setRadio();
		agregarComponente(set_factura);
		
		//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
		// DIÁLOGO FACTURA ELECTRÓNICA --------------------------------
		nota_debito_elec_dialogo.setId("nota_debito_elec_dialogo");
		nota_debito_elec_dialogo.setTitle("GENERAR NOTA DE DÉBITO ELECTRÓNICA");
		nota_debito_elec_dialogo.setWidth("45%");
		nota_debito_elec_dialogo.setHeight("30%");

		// GRID FACTURA ELECTRÓNICA
		Grid gri_fac_elec = new Grid();
		gri_fac_elec.setColumns(2);
		nota_debito_elec_dialogo.setDialogo(gri_fac_elec);
		nota_debito_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoNDelectronica");
		agregarComponente(nota_debito_elec_dialogo);


		// DIÁLOGO RESPUESTA DEL CORE --------------------------------
		respuesta_core_dialogo.setId("respuesta_core_dialogo");
		respuesta_core_dialogo.setTitle("RESPUESTA DEL SERVIDOR");
		respuesta_core_dialogo.setWidth("45%");
		respuesta_core_dialogo.setHeight("30%");

		// GRID RESPUESTA DEL CORE
		Grid gri_respuesta = new Grid();
		gri_respuesta.setColumns(2);
		respuesta_core_dialogo.setDialogo(gri_respuesta);
		respuesta_core_dialogo.getBot_aceptar().setMetodo(
				"aceptarDialogoRespuestaCore");
		agregarComponente(respuesta_core_dialogo);

		// BOTÓN FACTURA ELECTRÓNICA ---------------------------------
		Boton bot_fac_elec = new Boton();
		bot_fac_elec.setIcon("ui-icon-newwin");
		bot_fac_elec.setValue("Nota Débito Electrónica");
		bot_fac_elec.setMetodo("abrirDialogoNDelectronica");
		bar_botones.agregarBoton(bot_fac_elec);
		//Fin 30/12/2015 Facturacion Electronica - ABECERRA
		
		//REPORTE
		vpdf_rideND.setId("vpdf_rideND");
		vpdf_rideND.setTitle("Nota Debito Electrónica");
        agregarComponente(vpdf_rideND);
        
        Boton bot_ride = new Boton();
        bot_ride.setIcon("ui-icon-print");
        bot_ride.setValue("Imprimir Nota Debito");
        bot_ride.setMetodo("verRide");
        //bot_ride.setAjax(false);
 		bar_botones.agregarBoton(bot_ride);

	}

	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle_debito.modificar(evt); //Siempre es la primera linea
		calcular();
	}
	public void calcular(){
		//Variables para almacenar y calcular el total del detalle
		double dou_interes_generado=0;
		double dou_base_imponible= pckUtilidades.CConversion.CDbl_2(tab_detalle_debito.getValor("base_imponible_faded"));
		double dou_valor_iva=pckUtilidades.CConversion.CDbl_2(tab_detalle_debito.getValor("valor_iva_faded"));
		double dou_total=0;

		try {
			//Obtenemos el valor
			dou_interes_generado=pckUtilidades.CConversion.CDbl_2(tab_detalle_debito.getValor("interes_generado_faded"));
		} catch (Exception e) {
		}

		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_debito.setValor("interes_generado_fanod",utilitario.getFormatoNumero(dou_interes_generado,2));//cambiar a 2 produccion
		//Actualizamos el campo de la tabla AJAX
		dou_total=dou_base_imponible+dou_valor_iva+dou_interes_generado;
		tab_debito.setValor("base_imponible_fanod",dou_base_imponible+"");
		tab_debito.setValor("valor_iva_fanod",dou_valor_iva+"");
		tab_debito.setValor("interes_aplicado_fanod",utilitario.getVariable("p_valor_interes_mora_nd"));
		tab_debito.setValor("total_fanod",utilitario.getFormatoNumero(dou_total,2));
		tab_detalle_debito.setValor("total_faded",utilitario.getFormatoNumero(dou_total,2));
		//utilitario.addUpdateTabla(tab_detalle_debito, "interes_generado_faded", "tab_debito");
		utilitario.addUpdate("tab_debito");
		utilitario.addUpdate("tab_detalle_debito");
		tab_debito.modificar(tab_debito.getFilaActual());//para que haga el update
	}
	
	// Abre el diálogo de confirmación para emitir la facturación electrónica
	public void abrirDialogoNDelectronica() {
		// Código del estado de la factura seleccionada
		int estadoFactura = 0;
		try { estadoFactura = Integer.valueOf(tab_debito.getValor("ide_coest")); }
		catch(Exception ex){}
		// Estados: 2 - Emitido
		// 16 - Pagado

		// Solo se autorizan las facturas emitidas o emitidas nota débito
		if (estadoFactura == 16) {
			// Limpiando el grid existente
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 2 columnas para el grid existente
			nota_debito_elec_dialogo.getGri_cuerpo().setColumns(2);

			// Agregando una etiqueta vacía
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));

			// Agregando una etiqueta con la información de la confirmación
			Etiqueta preguntaConfirmacion = new Etiqueta(
					"¿Desea autorizar la siguiente nota de débito electrónica en el SRI?");
			preguntaConfirmacion
					.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

			nota_debito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(preguntaConfirmacion);

			// Etiqueta con Estilos Ambiente
			Etiqueta etiqueta1 = new Etiqueta("Ambiente: ");
			etiqueta1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta1);

			// Valor con Estilos
			Etiqueta valor1;
			
			if(ambiente==1)
				valor1 = new Etiqueta("PRUEBAS");
			else
				valor1 = new Etiqueta("PRODUCCION");
			
			valor1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:green;border-width: 0px");

			// Agregando el valor del campo
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(valor1);
			
			// Etiqueta con Estilos Secuencial
			Etiqueta etiqueta = new Etiqueta("Secuencial: ");
			etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Valor con Estilos
			Etiqueta valor = new Etiqueta(tab_debito.getValor("nro_nota_debito_elect_fanod"));
			valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

			// Agregando el valor del campo
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

			// Agregando la función al botón aceptar
			nota_debito_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoNDelectronica");
		} else {
			// Limpiando el grid existente
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 1 columna para el grid existente
			nota_debito_elec_dialogo.getGri_cuerpo().setColumns(1);

			// Etiqueta
			Etiqueta etiqueta;

			// Mostrando un mensaje con el estado de la factura
			switch (estadoFactura) {
			case 0:
				etiqueta = new Etiqueta("Seleccione una Nota de Débito");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 1:
				etiqueta = new Etiqueta("La nota de débito fue anulada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 2:
				etiqueta = new Etiqueta("La nota de débito debe ser cancelada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			default:
				etiqueta = new Etiqueta();
				break;
			}

			// Etiqueta del título
			Etiqueta titulo = new Etiqueta("");
			titulo.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration:none; color:black; border-width: 0px");

			// Agregando las etiquetas dentro del grid
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(titulo);
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));
			nota_debito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Agregando la función al botón aceptar
			nota_debito_elec_dialogo.getBot_aceptar().setMetodo(
					"cerrarDialogoNCelectronica");
		}

		// Dibujando en pantalla el diálogo
		nota_debito_elec_dialogo.dibujar();
	}

	// Cierra la confirmación para emitirla facturación electrónica
	public void cerrarDialogoNCelectronica() {
		// Cerrando el diálogo
		nota_debito_elec_dialogo.cerrar();
	}

	// Abre el diálogo con la respuesta del core de facturación (Factura
	// Electrónica)
	public void aceptarDialogoNDelectronica() {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();

		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			respuestaAutorizacion = servicio.procesarNotaDebitoElectronica(ambiente, tab_debito.getValor("nro_nota_debito_elect_fanod"),autorizar);

			respuestaCabecera = respuestaAutorizacion.get(0);
			
			if(respuestaCabecera.toUpperCase().contains("AUTORIZADO"))
			{
				boolean existeClave=false;
				String clave_acceso=pckUtilidades.CConversion.CStr(respuestaAutorizacion.get(1)).toUpperCase();
				
				try {
					if(clave_acceso.contains("CLAVE DE ACCESO") || clave_acceso.contains("CLAVE ACCESO"))
					{
						String [] strResp=clave_acceso.split("-");
						String [] strRespCA=pckUtilidades.CConversion.CStr(strResp[clave_acceso.contains("AUTORIZACION")?6:1]).split(":");
						clave_acceso=pckUtilidades.CConversion.CStr(strRespCA[1]);
						existeClave=true;
					}
					
				}
				catch(Exception ex)
				{
					System.out.println("ND Autorizada clave acceso error: "+ex.getMessage());
				}
				
				System.out.println("ND Autorizada: "+tab_debito.getValor("nro_nota_debito_elect_fanod")+" clave_acceso: "+clave_acceso);

				tab_debito.modificar(tab_debito.getFilaActual());
				tab_debito.setValor("autorizada_sri_fanod","true");
				if(existeClave)
					tab_debito.setValor("nro_autorizacion_sri_fanod",clave_acceso);
				tab_debito.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_debito");
			}

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replace("Recepcion: ", "");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// Limpiando el grid existente
		respuesta_core_dialogo.getGri_cuerpo().getChildren().clear();

		// Configurando 2 columnas para el grid existente
		respuesta_core_dialogo.getGri_cuerpo().setColumns(2);

		// Cabecera de la respuesta del core con Estilos
		Etiqueta cabecera = new Etiqueta(respuestaCabecera);
		cabecera.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:green;border-width: 0px");

		// Agregando la etiqueta
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(cabecera);

		// Mensaje del core con Estilos
		Etiqueta mensaje = new Etiqueta(respuestaMensaje);
		mensaje.setEstiloContenido("font-size:15px; border-width: 0px");

		// Ocultando el botón cancelar
		respuesta_core_dialogo.getBot_cancelar().setStyle(
				"width: 0px;height: 0px");

		// Agregando el mensaje de respuesta del core
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(mensaje);

		// Dibujando el mensaje de respuesta del core
		respuesta_core_dialogo.dibujar();

		// Cerrando el diálogo
		nota_debito_elec_dialogo.cerrar();
	}

	// Respuesta del Core de Facturación Electrónica ---------------------------
	// Cierra la respuesta del core de facturación
	public void aceptarDialogoRespuestaCore() {
		// Cerrando el diálogo
		respuesta_core_dialogo.cerrar();
	}
	//Fin 30/12/2015 Facturacion Electronica - ABECERRA
	
	public void verRide() 
	 {
		List<String> respuestaRide = new ArrayList<String>();
		
		String nro_autorizacion= pckUtilidades.CConversion.CStr(tab_debito.getValor("nro_autorizacion_sri_fanod"));
		String nombreDoc="ND_"+pckUtilidades.CConversion.CStr(tab_debito.getValor("nro_nota_debito_elect_fanod"));

		try {
			if(nro_autorizacion.length()>5)
			{
				//System.out.println("DN: "+nro_autorizacion);
				respuestaRide = utilitario.consumoRIDEJson( nro_autorizacion );
	
				if(respuestaRide.get(0).toUpperCase().contains("OK"))
				{
					vpdf_rideND.setVisualizarPDF(respuestaRide.get(1), nombreDoc);			
					vpdf_rideND.dibujar();
		            utilitario.addUpdate("vpdf_rideND");
				}
			}
			else
			{
				utilitario.agregarMensajeInfo("ND no disponible", "La Nota de debito electrónica no cuenta con un numero de autorización...");
			}

			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		

			
	}
		
	public void importarFactura(){
		//System.out.println(" ingresar al importar");
		if((aut_cliente.getValor()==null)){
			utilitario.agregarMensajeInfo("Debe insertar un registro ", "");
			return;
			}
		set_factura.getTab_seleccion().setSql(ser_facturacion.getCabeceraFactura("0",aut_cliente.getValor()));
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.dibujar();
	}


	public  void aceptarFactura(){
		double dou_interes_generado=0;
		double dou_base_imponible=0;
		double dou_valor_iva=0;
		double dou_total=0;
		double dou_interes_aplicado=0;
		double dias_retrasado=0;

		String str_seleccionado = set_factura.getValorSeleccionado();
		System.out.println("entra al str  "+str_seleccionado);
		if (str_seleccionado!=null){
			TablaGenerica tab_aceptarfactura = ser_facturacion.getTablaGenericaFacturaCabecera(str_seleccionado);		
			System.out.println(" tabla generica "+tab_aceptarfactura.getSql());
			//for(int i=0;i<tab_aceptarfactura.getTotalFilas();i++){
			dou_base_imponible = pckUtilidades.CConversion.CDbl_2(tab_aceptarfactura.getValor("base_aprobada_fafac")) - pckUtilidades.CConversion.CDbl_2(tab_aceptarfactura.getValor("valor_cobro"));
			
			tab_debito.setValor("detalle_fenod", tab_aceptarfactura.getValor("secuencial_fafac"));
			tab_debito.setValor("ide_transaccion_fanod", "0");
			
			tab_detalle_debito.insertar();
			tab_detalle_debito.setValor("ide_fafac", tab_aceptarfactura.getValor("ide_fafac"));
			tab_detalle_debito.setValor("base_imponible_faded",dou_base_imponible+"");
			tab_detalle_debito.setValor("fecha_emision_factura_faded",tab_aceptarfactura.getValor("fecha_vencimiento_fafac"));			
			

			Date fecha_inicio= utilitario.getFecha(tab_aceptarfactura.getValor("fecha_vencimiento_fafac"));
			Date fecha_fin=utilitario.getFecha(tab_aceptarfactura.getValor("fecha_pago_fafac"));
			
			if(fecha_fin==null)
				fecha_fin= utilitario.getFecha(utilitario.getFechaActual());
			
			if(fecha_inicio.getYear()<fecha_fin.getYear())
			{
				dias_retrasado=((fecha_fin.getYear() - fecha_inicio.getYear()) * 12) - (fecha_inicio.getMonth()+1) + (fecha_fin.getMonth()+1) + 1; 
			}
			else
				dias_retrasado=(fecha_fin.getMonth()+1) - (fecha_inicio.getMonth()+1)  + 1;
			//Revisar el año el otro año
			String dias_retra=dias_retrasado+"";

			tab_detalle_debito.setValor("dias_retraso_faded",dias_retra );
			dou_interes_aplicado=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_interes_mora_nd"));
			String inte_apli=dou_interes_aplicado+"";
			tab_detalle_debito.setValor("interes_aplicado_faded",utilitario.getFormatoNumero(inte_apli,2));

			dou_interes_generado=(((dou_interes_aplicado/12)*dou_base_imponible)*dias_retrasado)/100;
			String inte_generado=dou_interes_generado+"";
			tab_detalle_debito.setValor("interes_generado_faded",utilitario.getFormatoNumero(inte_generado,2));
			
			//AWBECERRA COMENTADO POR CAMBIO DE FORMULA INICIO
			//dou_valor_iva=(dou_interes_generado+dou_base_imponible)*0.12;
			dou_valor_iva=pckUtilidades.CConversion.CDbl_2(tab_aceptarfactura.getValor("valor_iva_fafac")); //no se cobra IVA en las notas de debito
			String val_iva=dou_valor_iva+"";
			tab_detalle_debito.setValor("valor_iva_faded",utilitario.getFormatoNumero(val_iva,2));
			dou_total=dou_base_imponible+dou_valor_iva+dou_interes_generado;
			String val_total=dou_total+"";
			
			//AWBECERRA COMENTADO POR CAMBIO DE FORMULA FIN
			
			tab_detalle_debito.setValor("total_faded",utilitario.getFormatoNumero( val_total,2));
			String valo_total=tab_detalle_debito.getSumaColumna("total_faded")+"";
			tab_debito.setValor("total_fanod",valo_total );
			String valo_iva=dou_valor_iva+"";
			tab_detalle_debito.setValor("valor_iva_faded",utilitario.getFormatoNumero(valo_iva,2));
			String iva_valo=tab_detalle_debito.getSumaColumna("valor_iva_faded")+"";
			tab_debito.setValor("valor_iva_fanod",iva_valo );

			String inte_gene=dou_interes_generado+"";
			tab_detalle_debito.setValor("interes_generado_faded",utilitario.getFormatoNumero(inte_gene,2));
			String inte_gener=tab_detalle_debito.getSumaColumna("interes_generado_faded")+"";
			tab_debito.setValor("interes_generado_fanod",inte_gener );

			String base_impo=dou_base_imponible+"";
			tab_detalle_debito.setValor("base_imponible_faded",utilitario.getFormatoNumero(base_impo,2));
			String base_imponi=tab_detalle_debito.getSumaColumna("base_imponible_faded")+"";
			tab_debito.setValor("base_imponible_fanod",base_imponi );
			//dou_interes_aplicado
			String inte_aplica=dou_interes_aplicado+"";
			tab_detalle_debito.setValor("interes_aplicado_faded",utilitario.getFormatoNumero(inte_aplica,2));
			String inte_aplicad=tab_detalle_debito.getSumaColumna("interes_aplicado_faded")+"";
			tab_debito.setValor("interes_aplicado_fanod",inte_aplicad );
			tab_debito.modificar(tab_debito.getFilaActual());

			//}
			set_factura.cerrar();
			utilitario.addUpdate("tab_debito");
			utilitario.addUpdate("tab_detalle_debito");
		}
	}


	public void limpiar(){
		aut_cliente.limpiar();
		tab_debito.limpiar();
		tab_detalle_debito.limpiar();
		utilitario.addUpdate("aut_cliente");
	}

	//METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_cliente.onSelect(evt);
		tab_debito.setCondicion("ide_recli="+aut_cliente.getValor());
		tab_debito.ejecutarSql();
		
		tab_detalle_debito.getColumna("ide_fafac").setCombo(ser_facturacion.getCabeceraFactura("0",aut_cliente.getValor()));
		
		tab_detalle_debito.ejecutarValorForanea(tab_debito.getValorSeleccionado());
	}


	///CALCULAR 
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(aut_cliente.getValor()!=null){
			if(tab_debito.isFocus()){
				tab_debito.getColumna("ide_recli").setValorDefecto(aut_cliente.getValor());
				tab_debito.insertar();

			}
			else if(tab_detalle_debito.isFocus()){
				tab_detalle_debito.insertar();
				tab_detalle_debito.setValor("base_imponible_faded","0");
				tab_detalle_debito.setValor("interes_generado_faded","0");
				tab_detalle_debito.setValor("valor_iva_faded","0");
				tab_detalle_debito.setValor("total_faded","0");
				tab_detalle_debito.setValor("interes_aplicado_faded",utilitario.getVariable("p_valor_interes_mora_nd"));
			}
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_debito.guardar()){
			tab_detalle_debito.guardar();
			guardarPantalla();
		}
		
		if(tab_debito.isFocus())
			if(ser_facturacion.actualizarNumeroNotaDebito(tab_debito.getValor("ide_fanod")).length()>3)
			{
				tab_debito.actualizar();
				//tab_debito.ejecutarSql();
			}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_debito.isFocus()){
			utilitario.agregarMensajeInfo("No puede Eliminar una nota de débito.", "");
			return;
		}
		utilitario.getTablaisFocus().eliminar();


	}

	public Tabla getTab_debito() {
		return tab_debito;
	}

	public void setTab_debito(Tabla tab_debito) {
		this.tab_debito = tab_debito;

	}

	public Tabla getTab_detalle_debito() {
		return tab_detalle_debito;
	}

	public void setTab_detalle_debito(Tabla tab_detalle_debito) {
		this.tab_detalle_debito = tab_detalle_debito;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	public SeleccionTabla getSet_factura() {
		return set_factura;
	}

	public void setSet_factura(SeleccionTabla set_factura) {
		this.set_factura = set_factura;
	}


}
