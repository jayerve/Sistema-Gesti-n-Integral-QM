package paq_facturacion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import pckUtilidades.CConversion;
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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_nota_credito_parcial extends Pantalla {
	private Tabla tab_nota_credito=new Tabla();
	private Tabla tab_det_nota_credito=new Tabla();
	private SeleccionTabla set_factura = new SeleccionTabla();
	private SeleccionTabla set_actualizarfactura=new SeleccionTabla();
	private Confirmar con_guardar = new Confirmar();
	private AutoCompletar aut_factura=new AutoCompletar(); 
	public static String p_modulo_facturacion;
	public static String p_notadecredito_anulado;
	public static String p_notadecredito_emitido;
	public static String p_factura_anulado;
	public static String p_factura_emitido;
	
	private String valor;
	double dou_valor_referencial_fanoc=0;
	double duo_valor_iva=0.12;
	double dou_iva_fanoc=0;
	double dou_total_fanoc=0;
	private String factura_sel="";
	
	@EJB
	private ServicioFacturacion ser_Facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA	
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo nota_credito_elec_dialogo = new Dialogo();
	private int ambiente = 2; //1 Test: 2 Produccion
	private boolean autorizar = true; //true Produccion
	private Check che_iva12=new Check();
	private Check che_ambiente=new Check();
	//Fin 30/12/2015 Facturacion Electronica - ABECERRA
	
	private Combo com_anio=new Combo();
		
	public pre_nota_credito_parcial(){
		
		duo_valor_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); //0.14 de IVA
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		//ambiente=1;
		
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
		Etiqueta eti_iva12=new Etiqueta("IVA 12%");
		bar_botones.agregarComponente(eti_iva12);
		bar_botones.agregarComponente(che_iva12);
		
		p_modulo_facturacion=utilitario.getVariable("p_modulo_facturacion");
		p_notadecredito_anulado=utilitario.getVariable("p_notadecredito_anulado");
		p_notadecredito_emitido=utilitario.getVariable("p_notadecredito_emitido");
		p_factura_anulado=utilitario.getVariable("p_factura_anulado");
		p_factura_emitido=utilitario.getVariable("p_factura_emitido");
		
		// TODO Auto-generated constructor stub
		tab_nota_credito.setId("tab_nota_credito");
		tab_nota_credito.setHeader("NOTA DE CRÉDITO");
		tab_nota_credito.setTipoFormulario(true);
		tab_nota_credito.getGrid().setColumns(4);
		tab_nota_credito.setTabla("fac_nota_credito","ide_fanoc", 1);
		tab_nota_credito.getColumna("ide_fafac").setCombo(ser_Facturacion.getClientesFactura("1,2"));
		tab_nota_credito.getColumna("ide_fafac").setAutoCompletar();
		tab_nota_credito.getColumna("ide_fafac").setLectura(true);
		tab_nota_credito.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", p_modulo_facturacion));
		tab_nota_credito.getColumna("ide_coest").setValorDefecto("2");
		tab_nota_credito.getColumna("valor_referencial_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("valor_referencial_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("iva_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("iva_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("total_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("activo_fanoc").setValorDefecto("true");
		tab_nota_credito.getColumna("total_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("fecha_fanoc").setValorDefecto(utilitario.getFechaActual());
		tab_nota_credito.getColumna("descuento_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("descuento_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("irbpnr_fanoc").setEtiqueta();
		tab_nota_credito.getColumna("irbpnr_fanoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_nota_credito.getColumna("nro_nota_credito_fanoc").setLongitud(50);
		tab_nota_credito.setCondicion("ide_fanoc=-1");
		tab_nota_credito.agregarRelacion(tab_det_nota_credito);
		tab_nota_credito.dibujar();
		PanelTabla pat_nota_credito=new PanelTabla();
		pat_nota_credito.setPanelTabla(tab_nota_credito);
		
		
		///// DETALLE DEBITO  
		tab_det_nota_credito.setId("tab_det_nota_credito");
		tab_det_nota_credito.setHeader("DETALLE NOTA CREDITO");
		tab_det_nota_credito.setTabla("fac_detalle_ncredito", "ide_fadec", 2);
		tab_det_nota_credito.getColumna("fecha_fadec").setValorDefecto(utilitario.getFechaActual());
		tab_det_nota_credito.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("0","true,false","2,9"));
		tab_det_nota_credito.getColumna("ide_bomat").setAutoCompletar();
		tab_det_nota_credito.getColumna("ide_bomat").setMetodoChange("seleccionoProducto");
		tab_det_nota_credito.getColumna("activo_fadec").setValorDefecto("true");
		tab_det_nota_credito.getColumna("cantidad_fadec").setMetodoChange("calcularDetalle");
		tab_det_nota_credito.getColumna("descuento_fadec").setMetodoChange("calcularDetalle");
		tab_det_nota_credito.getColumna("irbpnr_fadec").setLectura(true);
		tab_det_nota_credito.getColumna("valor_fadec").setLectura(true);
		//tab_det_nota_credito.getColumna("ide_bomat").setLectura(true);
		tab_det_nota_credito.getColumna("ide_fadef").setVisible(false);
		tab_det_nota_credito.getColumna("apliva_iva_fadec").setValorDefecto("true");
		tab_det_nota_credito.getColumna("total_fadec").setLectura(true);
		tab_det_nota_credito.getColumna("valor_fadec").setNombreVisual("Precio U.");
		
		
		//tab_detalle_debito.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
//		tab_det_nota_credito.getColumna("ide_fadef").setCombo(ser_facturacion.getCabeceraFactura("-1",""));
//		tab_det_nota_credito.getColumna("ide_fadef").setAutoCompletar();
//		tab_det_nota_credito.getColumna("ide_fadef").setLectura(true);
		/*tab_detalle_debito.getColumna("base_imponible_faded").setEtiqueta();
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
		
		*/
		tab_det_nota_credito.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_det_nota_credito);
		

		Division div_division=new Division();
		div_division.dividir2(pat_nota_credito, pat_detalle, "50%", "H");
		agregarComponente(div_division);
		
		che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);

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

	}
	
	//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
	public void cambiaAmbiente(){
 		if(che_ambiente.getValue().toString().equalsIgnoreCase("true")){
 			ambiente=1; //test
 		}
 		else{
 			ambiente=2; //produccion
 		}
 		
 		System.out.println("cambia ambiente nota credito parcial: "+ambiente);
 	}
	
	public void seleccionaElAnio (){
		
		tab_nota_credito.setCondicion(" extract(year from fecha_fanoc)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
		tab_nota_credito.ejecutarSql();
		//tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		
	}
	
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_factura.onSelect(evt);
		//aut_factura.getValor()
	}
	
	public void limpiar(){
		aut_factura.limpiar();
		utilitario.addUpdate("aut_factura");
	}
	
	public void cambiaIVA(){
		if(che_iva12.getValue().toString().equalsIgnoreCase("true")){
			duo_valor_iva=0.12;
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
			
			respuestaAutorizacion = servicio.procesarNotaCreditoElectronicaParcial(ambiente, tab_nota_credito.getValor("nro_nota_credito_fanoc"),duo_valor_iva+"",autorizar);

			respuestaCabecera = respuestaAutorizacion.get(0);

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replace("Recepcion: ", "");
			
			//respuestaMensaje="EN CONSTRUCCION";
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
			TablaGenerica tab_aceptarfacturaDet = ser_Facturacion.getTablaGenericaDetFactura(str_seleccionado);		
			
			System.out.println(" tabla generica"+tab_aceptarfactura.getSql());
			tab_nota_credito.insertar();
			factura_sel=str_seleccionado;
			tab_nota_credito.setValor("ide_fafac", tab_aceptarfactura.getValor("ide_fafac"));			
			tab_nota_credito.setValor("valor_referencial_fanoc", tab_aceptarfactura.getValor("base_aprobada_fafac"));			
			tab_nota_credito.setValor("iva_fanoc", tab_aceptarfactura.getValor("valor_iva_fafac"));
			tab_nota_credito.setValor("total_fanoc",tab_aceptarfactura.getValor("total_fafac"));
			tab_nota_credito.setValor("descuento_fanoc",tab_aceptarfactura.getValor("descuento_fafac"));
			tab_nota_credito.setValor("irbpnr_fanoc",tab_aceptarfactura.getValor("irbpnr_fafac"));
			
			for(int i=0;i<tab_aceptarfacturaDet.getTotalFilas();i++){
				tab_det_nota_credito.insertar();
				tab_det_nota_credito.setValor("ide_fadef", tab_aceptarfacturaDet.getValor(i,"ide_fadef"));			
				tab_det_nota_credito.setValor("ide_bomat", tab_aceptarfacturaDet.getValor(i,"ide_bomat"));			
				tab_det_nota_credito.setValor("cantidad_fadec", tab_aceptarfacturaDet.getValor(i,"cantidad_fadef"));
				tab_det_nota_credito.setValor("descuento_fadec", tab_aceptarfacturaDet.getValor(i,"descuento_fadef"));
				tab_det_nota_credito.setValor("irbpnr_fadec", tab_aceptarfacturaDet.getValor(i,"irbpnr_fadef"));
				tab_det_nota_credito.setValor("valor_fadec",tab_aceptarfacturaDet.getValor(i,"valor_fadef"));
				tab_det_nota_credito.setValor("total_fadec",tab_aceptarfacturaDet.getValor(i,"total_fadef"));
			}
		}
		set_factura.cerrar();
		utilitario.addUpdate("tab_nota_credito");
		utilitario.addUpdate("tab_det_nota_credito");
	}


	///ACTUALIZAR FACTURA
	public void actualizarFactura(){
		set_actualizarfactura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("2",aut_factura.getValor()));
		//set_actualizarfactura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("1",""));
		set_actualizarfactura.getTab_seleccion().ejecutarSql();
		set_actualizarfactura.dibujar();
	}

		
	public void seleccionoProducto(SelectEvent evt){
		tab_det_nota_credito.modificar(evt); //simepre que se ejecuta un metodoChange
			//Consultamos si el producto seleccionado carga iva
			//boolean boo_iva=tieneIvaProducto(tab_det_nota_credito.getValor("ide_bomat"));
			String str_seleccionado=tab_det_nota_credito.getValor("ide_bomat");
			TablaGenerica tab_factura = ser_Facturacion.getTablaGenericaFactura(tab_nota_credito.getValor("ide_fafac"));	
			
			TablaGenerica validarTarifaUnica=utilitario.consultar("select ide_recli, aplica_mtarifa_recli from rec_clientes where aplica_mtarifa_recli=false and ide_recli="+tab_factura.getValor("ide_recli"));
			TablaGenerica retornarValorUnico= utilitario.consultar(" select a.ide_recli, a.aplica_mtarifa_recli, valor_temat from rec_clientes a, tes_material_tarifa c  where  a.ide_tetar= c.ide_tetar  and a.ide_recli="+tab_factura.getValor("ide_recli")+" and ide_bomat="+str_seleccionado);
			TablaGenerica retornaValorMultiple=utilitario.consultar("select ide_teclt,a.ide_recli,ide_bomat,valor_temat from tes_cliente_tarifa a, rec_clientes b,tes_material_tarifa c"+
					" where a.ide_recli = b.ide_recli and a.ide_temat = c.ide_temat and a.ide_recli ="+tab_factura.getValor("ide_recli")+" and ide_bomat="+str_seleccionado);

			if(validarTarifaUnica.isEmpty()){
				valor=retornaValorMultiple.getValor("valor_temat");
				System.out.println("Multiple "+valor);

			}
			else {
				valor=retornarValorUnico.getValor("valor_temat");
				System.out.println("Valor Unico "+valor);
			}

			if (valor!=null){ 
				tab_det_nota_credito.setValor("valor_fadec",valor);	
				
				utilitario.addUpdateTabla(tab_det_nota_credito, "valor_fadec", "");
			
			}
			else{
				//Mensaje 
				utilitario.agregarMensajeInfo("No existen tarifas para el cliente y el articulo seleccionado", "");
			}

			calcular();

		}
	
		public void calcularDetalle(AjaxBehaviorEvent evt) {
			tab_det_nota_credito.modificar(evt); //Siempre es la primera linea
			calcular();
		}
		
		///CALCULAR 		
		public void calcular() {

			/*double dou_valor_referencial_fanoc=0;
			//double duo_valor_iva=0.12;
			double dou_iva_fanoc=0;
			double dou_total_fanoc=0;
			
			dou_valor_referencial_fanoc=Double.parseDouble(tab_nota_credito.getValor("valor_referencial_fanoc"));
			dou_iva_fanoc=dou_valor_referencial_fanoc*duo_valor_iva;
			dou_total_fanoc=dou_valor_referencial_fanoc+dou_iva_fanoc;
			tab_nota_credito.setValor("iva_fanoc",utilitario.getFormatoNumero(dou_iva_fanoc,3));
			tab_nota_credito.setValor("total_fanoc",utilitario.getFormatoNumero(dou_total_fanoc,3));
			utilitario.addUpdateTabla(tab_nota_credito, "iva_fanoc,total_fanoc", "");	*/
			
			
			//Variables para almacenar y calcular el total del detalle
			double dou_cantidad=0;
			double dou_valor=0;
			double dou_descuento=0;
			double dou_descuentoAux=0;
			double dou_totAux=0;
			double dou_total=0;
			double dou_iva=0;
			
			//double dou_cantidad=CConversion.CDbl_2(tab_det_nota_credito.getSumaColumna("cantidad_fadec"));
			//double dou_valor=CConversion.CDbl_2(tab_det_nota_credito.getSumaColumna("valor_fadec"));
			//double dou_descuento=CConversion.CDbl_2(tab_det_nota_credito.getSumaColumna("descuento_fadec"));
			
			for (int i = 0; i < tab_det_nota_credito.getTotalFilas(); i++) {
				
				dou_cantidad=CConversion.CDbl_2(tab_det_nota_credito.getValor(i, "cantidad_fadec"));
				dou_valor=CConversion.CDbl_2(tab_det_nota_credito.getValor(i, "valor_fadec"));
				dou_descuento=CConversion.CDbl_2(tab_det_nota_credito.getValor(i, "descuento_fadec"));
				dou_descuentoAux+=dou_descuento;
				dou_totAux=(dou_cantidad*dou_valor)-dou_descuento;
				dou_total+=dou_totAux;
				if(CConversion.CBol(tab_det_nota_credito.getValor(i, "apliva_iva_fadec")))
					dou_iva+=CConversion.CDbl_2(duo_valor_iva*dou_totAux);
				tab_det_nota_credito.setValor(i, "total_fadec",CConversion.CDbl_2(dou_totAux)+"");
				
			}
			utilitario.addUpdate("tab_det_nota_credito");
			
			//double dou_total=CConversion.CDbl_2(tab_det_nota_credito.getSumaColumna("total_fadec"));
			double dou_totalIva=0;			
			dou_totalIva = dou_iva; //CConversion.CDbl_2(duo_valor_iva*dou_total);
			tab_nota_credito.setValor("descuento_fanoc",utilitario.getFormatoNumero(dou_descuentoAux,3));
			tab_nota_credito.setValor("valor_referencial_fanoc",utilitario.getFormatoNumero(dou_total,3));
			tab_nota_credito.setValor("iva_fanoc",utilitario.getFormatoNumero(dou_totalIva,3));
			tab_nota_credito.setValor("total_fanoc",utilitario.getFormatoNumero(CConversion.CDbl_2(dou_total+dou_totalIva),3));
			//utilitario.addUpdateTabla(tab_nota_credito, "valor_referencial_fanoc,iva_fanoc,total_fanoc,descuento_fanoc", "");
			utilitario.addUpdate("tab_nota_credito");
			tab_nota_credito.modificar(tab_nota_credito.getFilaActual());//para que haga el update		
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
				tab_det_nota_credito.guardar();
				guardarPantalla();
				
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
			
			if(CConversion.CBol(tab_nota_credito.getValor("autorizada_sri_fanoc")))
			{
				utilitario.agregarMensajeInfo("No puede Eliminar una nota de crédito autorizada.", "");
				return;
			}
			
			utilitario.getTablaisFocus().eliminar();
			
			calcular();

			//tab_nota_credito.eliminar();

		}

		public Tabla getTab_nota_credito() {
			return tab_nota_credito;
		}

		public void setTab_nota_credito(Tabla tab_nota_credito) {
			this.tab_nota_credito = tab_nota_credito;
		}
		

		public Tabla getTab_det_nota_credito() {
			return tab_det_nota_credito;
		}

		public void setTab_det_nota_credito(Tabla tab_det_nota_credito) {
			this.tab_det_nota_credito = tab_det_nota_credito;
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
		
		public AutoCompletar getAut_factura() {
			return aut_factura;
		}

		public void setAut_factura(AutoCompletar aut_factura) {
			this.aut_factura = aut_factura;
		}

		public Combo getCom_anio() {
			return com_anio;
		}

		public void setCom_anio(Combo com_anio) {
			this.com_anio = com_anio;
		}


	}
