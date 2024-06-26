package paq_facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import pck_cliente.servicio;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;

public class pre_nota_credito extends Pantalla {
	private  Tabla tab_nota_credito=new Tabla();
	private SeleccionTabla set_factura = new SeleccionTabla();
	private SeleccionTabla set_actualizarfactura=new SeleccionTabla();
	private Confirmar con_guardar = new Confirmar();
	private AutoCompletar aut_factura=new AutoCompletar(); 
	public static String p_modulo_facturacion;
	public static String p_notadecredito_anulado;
	public static String p_notadecredito_emitido;
	public static String p_factura_anulado;
	public static String p_factura_emitido;
	public static String p_notadecredito_estados;
	
	//REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	double dou_valor_referencial_fanoc=0;
	double duo_valor_iva=0.12;
	double dou_iva_fanoc=0;
	double dou_total_fanoc=0;
	private String factura_sel="";
	
	@EJB
	private ServicioFacturacion ser_Facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA	
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo nota_credito_elec_dialogo = new Dialogo();
	private int ambiente = 2; //1 Test: 2 Produccion
	private boolean autorizar = true; //true Produccion
	private Check che_iva12=new Check();
	private Check che_ambiente=new Check();
	private VisualizarPDF vpdf_rideNC = new VisualizarPDF();
	//Fin 30/12/2015 Facturacion Electronica - ABECERRA
		
	private Combo com_anio=new Combo();
	
	public pre_nota_credito(){
		
		duo_valor_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); //0.14 de IVA
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_factura.setId("aut_factura");     
		aut_factura.setAutoCompletar(ser_Facturacion.getDatosFactura("1","")); //1 carga todos, 0 carga por grupos enviados
		aut_factura.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
		Etiqueta eti_colaborador=new Etiqueta("ESTABLECIMIENTO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
		
		che_iva12.setId("che_iva12");
		che_iva12.setMetodoChange("cambiaIVA");
		Etiqueta eti_iva12=new Etiqueta("IVA 14%");
		bar_botones.agregarComponente(eti_iva12);
		bar_botones.agregarComponente(che_iva12);
		
		p_modulo_facturacion=utilitario.getVariable("p_modulo_facturacion");
		p_notadecredito_anulado=utilitario.getVariable("p_notadecredito_anulado");
		p_notadecredito_emitido=utilitario.getVariable("p_notadecredito_emitido");
		p_factura_anulado=utilitario.getVariable("p_factura_anulado");
		p_factura_emitido=utilitario.getVariable("p_factura_emitido");
		p_notadecredito_estados=utilitario.getVariable("p_notadecredito_estados");
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		// TODO Auto-generated constructor stub
		tab_nota_credito.setId("tab_nota_credito");
		tab_nota_credito.setHeader("NOTA DE CRÉDITO");
		tab_nota_credito.setTipoFormulario(true);
		tab_nota_credito.getGrid().setColumns(4);
		tab_nota_credito.setTabla("fac_nota_credito","ide_fanoc", 1);
		tab_nota_credito.getColumna("ide_fafac").setCombo(ser_Facturacion.getClientesFactura(p_notadecredito_estados));
		tab_nota_credito.getColumna("ide_fafac").setAutoCompletar();
		tab_nota_credito.getColumna("ide_fafac").setLectura(true);
		tab_nota_credito.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", p_modulo_facturacion));
		tab_nota_credito.getColumna("ide_coest").setValorDefecto("2");
		//tab_nota_credito.getColumna("valor_referencial_fanoc").setMetodoChange("calcular");	
		tab_nota_credito.getColumna("valor_referencial_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("valor_referencial_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("iva_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("iva_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("total_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("activo_fanoc").setValorDefecto("true");
		tab_nota_credito.getColumna("total_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("fecha_fanoc").setValorDefecto(utilitario.getFechaActual());
		tab_nota_credito.getColumna("nro_nota_credito_fanoc").setLongitud(50);
		tab_nota_credito.setCondicion("ide_fanoc=-1");
		tab_nota_credito.dibujar();
		PanelTabla pat_nota_credito=new PanelTabla();
		pat_nota_credito.setPanelTabla(tab_nota_credito);
		

		Division div_division=new Division();
		div_division.dividir1(pat_nota_credito);
		agregarComponente(div_division);


		Boton bot_factura = new Boton();
		bot_factura.setValue("Agregar Factura");
		bot_factura.setTitle("Factura");
		bot_factura.setIcon("ui-icon-person");
		bot_factura.setMetodo("importarFactura");
		bar_botones.agregarBoton(bot_factura);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_Facturacion.getCabeceraFactura("1",""),"ide_fafac");
		//set_factura.getTab_seleccion().getColumna("factura_fisica_fafac").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.setRadio();
		agregarComponente(set_factura);

		bar_botones.agregarBoton(bot_factura);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		/*Boton bot_actualizarfactura=new Boton();
		bot_actualizarfactura.setIcon("ui-icon-person");
		bot_actualizarfactura.setValue("Actualizar Factura");
		bot_actualizarfactura.setMetodo("actualizarFactura");
		bar_botones.agregarBoton(bot_actualizarfactura);*/	

		set_actualizarfactura.setId("set_actualizarfactura");
		set_actualizarfactura.setSeleccionTabla(ser_Facturacion.getCabeceraFactura("1",""),"ide_fafac");
		//set_actualizarfactura.getTab_seleccion().getColumna("factura_fisica_fafac").setFiltro(true);
		set_actualizarfactura.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		set_actualizarfactura.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_actualizarfactura.getBot_aceptar().setMetodo("modificarFactura");
		set_actualizarfactura.setRadio();
		agregarComponente(set_actualizarfactura);

		//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
		// DIÁLOGO FACTURA ELECTRÓNICA --------------------------------
		che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);
 		
		nota_credito_elec_dialogo.setId("nota_credito_elec_dialogo");
		nota_credito_elec_dialogo.setTitle("GENERAR NOTA DE CRÉDITO ELECTRÓNICA");
		nota_credito_elec_dialogo.setWidth("45%");
		nota_credito_elec_dialogo.setHeight("30%");

		// GRID FACTURA ELECTRÓNICA
		Grid gri_fac_elec = new Grid();
		gri_fac_elec.setColumns(2);
		nota_credito_elec_dialogo.setDialogo(gri_fac_elec);
		nota_credito_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoNCelectronica");
		agregarComponente(nota_credito_elec_dialogo);


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
		bot_fac_elec.setValue("Nota Crédito Electrónica");
		bot_fac_elec.setMetodo("abrirDialogoNCelectronica");
		bar_botones.agregarBoton(bot_fac_elec);
		//Fin 30/12/2015 Facturacion Electronica - ABECERRA

		//REPORTE
		vpdf_rideNC.setId("vpdf_rideNC");
        vpdf_rideNC.setTitle("Nota de Credito Electrónica");
        agregarComponente(vpdf_rideNC);
        
        Boton bot_ride = new Boton();
        bot_ride.setIcon("ui-icon-print");
        bot_ride.setValue("Imprimir Nota Credito");
        bot_ride.setMetodo("verRide");
        //bot_ride.setAjax(false);
 		bar_botones.agregarBoton(bot_ride);
	}
	
	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
	 public void cambiaAmbiente(){
 		if(che_ambiente.getValue().toString().equalsIgnoreCase("true")){
 			ambiente=1; //test
 		}
 		else{
 			ambiente=2; //produccion
 		}
 		
 		System.out.println("cambia ambiente nota credito: "+ambiente);
 	}
	 
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_factura.onSelect(evt);
		//aut_factura.getValor()
	
	}
	
	public void seleccionaElAnio (){
		
		tab_nota_credito.setCondicion(" extract(year from fecha_fanoc)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
		tab_nota_credito.ejecutarSql();
		//tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		
	}
	
	public void limpiar(){
		aut_factura.limpiar();
		utilitario.addUpdate("aut_factura");
	}
	
	public void cambiaIVA(){
		if(che_iva12.getValue().toString().equalsIgnoreCase("true")){
			duo_valor_iva=0.14;
		}
		else{
			duo_valor_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); //0.14 de IVA
		}
		
		System.out.println("cambiaIVA NC "+duo_valor_iva);
	}
	
	// Abre el diálogo de confirmación para emitir la facturación electrónica
	public void abrirDialogoNCelectronica() {
		// Código del estado de la factura seleccionada
		int estadoFactura = 0;
		try { estadoFactura = Integer.valueOf(tab_nota_credito.getValor("ide_coest")); }
		catch(Exception ex){}
		// Estados de la Factura: 2 - Emitido
		// 24 - Emitido Nota Debito

		// Solo se autorizan las facturas emitidas o emitidas nota débito
		if (estadoFactura == 2 || estadoFactura == 24) {
			
			TablaGenerica detallesFactura=utilitario.consultar("SELECT 1 as codigo, sum(total_fadef) as totalDet FROM fac_detalle_factura where ide_fafac="+tab_nota_credito.getValor("ide_fafac"));
			if(detallesFactura.getTotalFilas()>0)
			{
				if(!(pckUtilidades.CConversion.CDbl_2(tab_nota_credito.getValor("valor_referencial_fanoc"))==pckUtilidades.CConversion.CDbl_2(detallesFactura.getValor("totalDet"))))
				 {
					 utilitario.agregarMensajeError("Error grave", "La suma de detalles de la factura No concuerda con el valor de la retención... contactese con el administrador");
					 return;
				 }
			}
			else
			{
				utilitario.agregarMensajeError("Error grave","Factura sin Detalles... contactese con el administrador");
				return;
			}
			// Limpiando el grid existente
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 2 columnas para el grid existente
			nota_credito_elec_dialogo.getGri_cuerpo().setColumns(2);

			// Agregando una etiqueta vacía
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));

			// Agregando una etiqueta con la información de la confirmación
			Etiqueta preguntaConfirmacion = new Etiqueta(
					"¿Desea autorizar la siguiente nota de crédito electrónica en el SRI?");
			preguntaConfirmacion
					.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

			nota_credito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(preguntaConfirmacion);

			// Etiqueta con Estilos Ambiente
			Etiqueta etiqueta1 = new Etiqueta("Ambiente: ");
			etiqueta1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta1);

			// Valor con Estilos
			Etiqueta valor1;
			
			if(ambiente==1)
				valor1 = new Etiqueta("PRUEBAS");
			else
				valor1 = new Etiqueta("PRODUCCION");
			
			valor1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:green;border-width: 0px");

			// Agregando el valor del campo
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(valor1);
			
			// Etiqueta con Estilos Secuencial
			Etiqueta etiqueta = new Etiqueta("Secuencial: ");
			etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Valor con Estilos
			Etiqueta valor = new Etiqueta(tab_nota_credito.getValor("nro_nota_credito_fanoc"));
			valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

			// Agregando el valor del campo
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

			// Agregando la función al botón aceptar
			nota_credito_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoNCelectronica");
		} else {
			// Limpiando el grid existente
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 1 columna para el grid existente
			nota_credito_elec_dialogo.getGri_cuerpo().setColumns(1);

			// Etiqueta
			Etiqueta etiqueta;
			
			// Mostrando un mensaje con el estado de la factura
			switch (estadoFactura) {
			case 0:
				etiqueta = new Etiqueta("Seleccione una Nota de Crédito");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 1:
				etiqueta = new Etiqueta("La nota de crédito fue anulada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 16:
				etiqueta = new Etiqueta("La nota de crédito fue pagada");
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
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(titulo);
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));
			nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Agregando la función al botón aceptar
			nota_credito_elec_dialogo.getBot_aceptar().setMetodo(
					"cerrarDialogoNCelectronica");
		}

		// Dibujando en pantalla el diálogo
		nota_credito_elec_dialogo.dibujar();
	}

	// Cierra la confirmación para emitirla facturación electrónica
	public void cerrarDialogoNCelectronica() {
		// Cerrando el diálogo
		nota_credito_elec_dialogo.cerrar();
	}

	// Abre el diálogo con la respuesta del core de facturación (Factura
	// Electrónica)
	public void aceptarDialogoNCelectronica() {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();

		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			respuestaAutorizacion = servicio.procesarNotaCreditoElectronica(ambiente, tab_nota_credito.getValor("nro_nota_credito_fanoc"),duo_valor_iva+"",autorizar);

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
					System.out.println("NC Autorizada clave acceso error: "+ex.getMessage());
				}
				
				System.out.println("NC Autorizada: "+tab_nota_credito.getValor("nro_nota_credito_fanoc")+" clave_acceso: "+clave_acceso);
				
				tab_nota_credito.modificar(tab_nota_credito.getFilaActual());
				tab_nota_credito.setValor("autorizada_sri_fanoc","true");
				if(existeClave)
					tab_nota_credito.setValor("nro_autorizacion_sri_fanoc",clave_acceso);
				
				tab_nota_credito.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_retencion");
			}

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replaceAll("Recepcion: ", "");
			
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
		nota_credito_elec_dialogo.cerrar();
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
		
		String nro_autorizacion= pckUtilidades.CConversion.CStr(tab_nota_credito.getValor("nro_autorizacion_sri_fanoc"));
		String nombreDoc="NC_"+pckUtilidades.CConversion.CStr(tab_nota_credito.getValor("nro_nota_credito_fanoc"));

		try {
			if(nro_autorizacion.length()>5)
			{
				respuestaRide = utilitario.consumoRIDEJson( nro_autorizacion );
	
				if(respuestaRide.get(0).toUpperCase().contains("OK"))
				{
					vpdf_rideNC.setVisualizarPDF(respuestaRide.get(1), nombreDoc);			
					vpdf_rideNC.dibujar();
		            utilitario.addUpdate("vpdf_rideNC");
				}
			}
			else
			{
				utilitario.agregarMensajeInfo("NC no disponible", "La Nota de Credito electrónica no cuenta con un numero de autorización...");
			}

			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		

			
	}
	
	public void importarFactura(){
		set_factura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("2",aut_factura.getValor()));
		//set_factura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("1",""));
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.dibujar();

	}

	public  void aceptarFactura(){
		String str_seleccionado = set_factura.getValorSeleccionado();
		System.out.println("entra al str  "+str_seleccionado);
		if (str_seleccionado!=null){
			TablaGenerica tab_aceptarfactura = ser_Facturacion.getTablaGenericaFacturaCabecera(str_seleccionado);		
			System.out.println(" tabla generica"+tab_aceptarfactura.getSql());
			tab_nota_credito.insertar();
			factura_sel=str_seleccionado;
			tab_nota_credito.setValor("ide_fafac", tab_aceptarfactura.getValor("ide_fafac"));			
			tab_nota_credito.setValor("valor_referencial_fanoc", tab_aceptarfactura.getValor("base_aprobada_fafac"));			
			tab_nota_credito.setValor("iva_fanoc", tab_aceptarfactura.getValor("valor_iva_fafac"));
			tab_nota_credito.setValor("total_fanoc",tab_aceptarfactura.getValor("total_fafac"));
			//tab_nota_credito.setValor("ide_fafac",str_seleccionado);

		}
		set_factura.cerrar();
		utilitario.addUpdate("tab_nota_credito");
	}


	///ACTUALIZAR FACTURA
	public void actualizarFactura(){
		set_actualizarfactura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("2",aut_factura.getValor()));
		//set_actualizarfactura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("1",""));
		set_actualizarfactura.getTab_seleccion().ejecutarSql();
		set_actualizarfactura.dibujar();
	}

	public void modificarFactura(){
		String str_facturaActualizado=set_actualizarfactura.getValorSeleccionado();
		System.out.println("Entra a guardar..."+str_facturaActualizado);
			TablaGenerica tab_actualizarfactura = ser_Facturacion.getTablaGenericaFacturaCabecera(str_facturaActualizado);		
			System.out.println(" tabla generica"+tab_actualizarfactura.getSql());
			factura_sel=str_facturaActualizado;
			tab_nota_credito.setValor("ide_fafac", tab_actualizarfactura.getValor("ide_fafac"));			
			tab_nota_credito.setValor("detalle_fanoc", "Factura Anulada: "+ tab_actualizarfactura.getValor("secuencial_fafac"));			
			tab_nota_credito.setValor("valor_referencial_fanoc", tab_actualizarfactura.getValor("base_aprobada_fafac"));			
			tab_nota_credito.setValor("iva_fanoc", tab_actualizarfactura.getValor("valor_iva_fafac"));
			tab_nota_credito.setValor("total_fanoc",tab_actualizarfactura.getValor("total_fafac"));
			tab_nota_credito.modificar(tab_nota_credito.getFilaActual());
			utilitario.addUpdate("tab_nota_credito");	

			con_guardar.setMessage("Esta Seguro de Actualizar La Factura");
			con_guardar.setTitle("CONFIRMACION ");
			con_guardar.getBot_aceptar().setMetodo("guardarActualilzarFactura");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}

		
		public void guardarActualilzarFactura(){
			System.out.println("Entra a guardar...");
			tab_nota_credito.guardar();
			con_guardar.cerrar();
			set_actualizarfactura.cerrar();

			guardarPantalla();

		}
		///CALCULAR 
		
		public void calcular(AjaxBehaviorEvent evt) {
			tab_nota_credito.modificar(evt); //Siempre es la primera lineadoubler

			double dou_valor_referencial_fanoc=0;
			//double duo_valor_iva=0.12;
			double dou_iva_fanoc=0;
			double dou_total_fanoc=0;
			
			dou_valor_referencial_fanoc=pckUtilidades.CConversion.CDbl_2(tab_nota_credito.getValor("valor_referencial_fanoc"));
			dou_iva_fanoc=dou_valor_referencial_fanoc*duo_valor_iva;
			dou_total_fanoc=dou_valor_referencial_fanoc+dou_iva_fanoc;
			tab_nota_credito.setValor("iva_fanoc",utilitario.getFormatoNumero(dou_iva_fanoc,3));
			tab_nota_credito.setValor("total_fanoc",utilitario.getFormatoNumero(dou_total_fanoc,3));
			utilitario.addUpdateTabla(tab_nota_credito, "iva_fanoc,total_fanoc", "");	
					
		}
		//REPORTE
		public void abrirListaReportes() {
			// TODO Auto-generated method stub
			rep_reporte.dibujar();
		}
		public void aceptarReporte(){
			if(rep_reporte.getReporteSelecionado().equals("Factura Credito"));{
			   if (tab_nota_credito.getTotalFilas()>0){
				if (rep_reporte.isVisible()){
						p_parametros=new HashMap();		
						rep_reporte.cerrar();				
					p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_nota_credito.getValor("ide_fanoc")));
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					self_reporte.dibujar();
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una Factura");
		
				}
			}
		}


		@Override
		public void insertar() {
			// TODO Auto-generated method stub
			utilitario.agregarMensajeInfo("No puede Insertar una nota de crédito.", "Usar el boton Agregar Factura");
			return;

		}

		@Override
		public void guardar() {
			// TODO Auto-generated method stub
			
			String sql="";
			
			if(!Boolean.parseBoolean(tab_nota_credito.getValor("autorizada_sri_fanoc")))
			{
				tab_nota_credito.guardar();
				guardarPantalla();
				
				System.out.println("longitud nro_nota_credito_fanoc "+pckUtilidades.CConversion.CStr(tab_nota_credito.getValor("nro_nota_credito_fanoc")).length());
				
				if(pckUtilidades.CConversion.CStr(tab_nota_credito.getValor("nro_nota_credito_fanoc")).length() <= 9)
					ser_Facturacion.actualizarNumeroNotaCredito(tab_nota_credito.getValor("ide_fanoc"));
				
				if(p_notadecredito_emitido.equals(tab_nota_credito.getValor("ide_coest")) ) {
					
					int ide_fafac=(pckUtilidades.CConversion.CInt(tab_nota_credito.getValor("ide_fafac")) > 0 ? pckUtilidades.CConversion.CInt(tab_nota_credito.getValor("ide_fafac")) :
						pckUtilidades.CConversion.CInt(factura_sel));
					
					sql="update fac_factura set ide_coest ="+ p_factura_anulado + ",razon_anulado_fafac='"+tab_nota_credito.getValor("detalle_fanoc")+"', fecha_anulado_fafac=now()  where ide_fafac = "+ ide_fafac ;

					utilitario.getConexion().ejecutarSql(sql);
					System.out.println(" entre nota de c emitida "+sql);
					sql="update fac_nota_credito set ide_fafac="+ide_fafac+" where ide_fanoc="+tab_nota_credito.getValor("ide_fanoc");
					utilitario.getConexion().ejecutarSql(sql);
					System.out.println(" entre nota de c emitida "+sql);
					tab_nota_credito.actualizar();
				
				}else if (p_notadecredito_anulado.equals(tab_nota_credito.getValor("ide_coest"))){
					sql="update fac_factura set ide_coest ="+ p_factura_emitido + " where ide_fafac = "+ tab_nota_credito.getValor("ide_fafac");
					utilitario.getConexion().ejecutarSql(sql);	
					System.out.println(" entre nota de c anulado "+sql);
					tab_nota_credito.actualizar();
				}
			}
			else
				utilitario.agregarMensajeInfo("No es posible editar una nota de credito autorizada", "");
		}

		@Override
		public void eliminar() {
			// TODO Auto-generated method stub
			
			utilitario.agregarMensajeInfo("No puede Eliminar una nota de crédito.", "");
			return;
			
			//tab_nota_credito.eliminar();

		}

		public Tabla getTab_nota_credito() {
			return tab_nota_credito;
		}

		public void setTab_nota_credito(Tabla tab_nota_credito) {
			this.tab_nota_credito = tab_nota_credito;
		}

		public SeleccionTabla getSet_factura() {
			return set_factura;
		}

		public void setSet_factura(SeleccionTabla set_factura) {
			this.set_factura = set_factura;
		}
		public SeleccionTabla getSet_actualizarfactura() {
			return set_actualizarfactura;
		}
		public void setSet_actualizarfactura(SeleccionTabla set_actualizarfactura) {
			this.set_actualizarfactura = set_actualizarfactura;
		}
		public Confirmar getCon_guardar() {
			return con_guardar;
		}
		public void setCon_guardar(Confirmar con_guardar) {
			this.con_guardar = con_guardar;
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
		public Map getMap_parametros() {
			return map_parametros;
		}
		public void setMap_parametros(Map map_parametros) {
			this.map_parametros = map_parametros;
		}
		public Map getP_parametros() {
			return p_parametros;
		}
		public void setP_parametros(Map p_parametros) {
			this.p_parametros = p_parametros;
		}
		
		public AutoCompletar getAut_factura() {
			return aut_factura;
		}

		public void setAut_factura(AutoCompletar aut_factura) {
			this.aut_factura = aut_factura;
		}


	}
