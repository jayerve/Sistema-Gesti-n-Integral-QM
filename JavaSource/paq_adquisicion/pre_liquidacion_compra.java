package paq_adquisicion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import pck_cliente.servicio;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_liquidacion_compra extends Pantalla {
	private Tabla tab_liquidacion_compra = new Tabla();
	private Tabla tab_detalle_liquidacion_compra = new Tabla();
	private SeleccionTabla set_proveedor = new SeleccionTabla();
	private SeleccionTabla set_tramite=new SeleccionTabla();

	@EJB
	private ServicioAdquisicion ser_Adquisicion = (ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);

	//Inicio Liquidacion Electronica	
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo liquidacion_elec_dialogo = new Dialogo();
	private int ambiente = 2; //1 Test: 2 Produccion
	private boolean autorizar = true; //true Produccion
	private Check che_ambiente=new Check();
	//Fin Liquidacion Electronica
	
	public pre_liquidacion_compra() {

		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		
		tab_liquidacion_compra.setId("tab_liquidacion_compra");
		tab_liquidacion_compra.setTabla("adq_liquidacion_compra", "ide_adlic", 1);
		tab_liquidacion_compra.agregarRelacion(tab_detalle_liquidacion_compra);
		tab_liquidacion_compra.setTipoFormulario(true);
		tab_liquidacion_compra.getGrid().setColumns(4);
		tab_liquidacion_compra.setCampoOrden("ide_adlic desc");

		tab_liquidacion_compra.getColumna("fecha_adlic").setValorDefecto(utilitario.getFechaActual());
		tab_liquidacion_compra.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_liquidacion_compra.getColumna("ide_tepro").setLectura(true);
		tab_liquidacion_compra.getColumna("ide_tepro").setAutoCompletar();
		
		tab_liquidacion_compra.getColumna("ide_prtra").setCombo(ser_Presupuesto.getTramite("true"));
		tab_liquidacion_compra.getColumna("ide_prtra").setAutoCompletar();
		tab_liquidacion_compra.getColumna("ide_prtra").setLectura(true);
		
		tab_liquidacion_compra.getColumna("ide_coest").setValorDefecto("2");
		tab_liquidacion_compra.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		
		tab_liquidacion_compra.getColumna("num_liquidacion_adlic").setLectura(true);
		tab_liquidacion_compra.getColumna("ide_tecpo").setLectura(true);
		tab_liquidacion_compra.getColumna("autorizada_sri_adlic").setLectura(true);

		tab_liquidacion_compra.getColumna("subtotal_adlic").setValorDefecto("0");
		tab_liquidacion_compra.getColumna("subtotal_adlic").setEtiqueta();
		tab_liquidacion_compra.getColumna("subtotal_adlic").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_liquidacion_compra.getColumna("total_adlic").setValorDefecto("0");
		tab_liquidacion_compra.getColumna("total_adlic").setEtiqueta();
		tab_liquidacion_compra.getColumna("total_adlic").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		tab_liquidacion_compra.getColumna("activo_adlic").setLectura(true);
		tab_liquidacion_compra.getColumna("activo_adlic").setValorDefecto("true");
		
		tab_liquidacion_compra.dibujar();
		PanelTabla pat_adq_factura = new PanelTabla();
		pat_adq_factura.setPanelTabla(tab_liquidacion_compra);

		tab_detalle_liquidacion_compra.setId("tab_detalle_liquidacion_compra");
		tab_detalle_liquidacion_compra.setTabla("adq_liquidacion_detalle", "ide_adlid", 2);
		tab_detalle_liquidacion_compra.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("1", "true,false", ""));
		tab_detalle_liquidacion_compra.getColumna("ide_bomat").setAutoCompletar();
		tab_detalle_liquidacion_compra.getColumna("cantidad_adlid").setMetodoChange("calcularDetalle");
		tab_detalle_liquidacion_compra.getColumna("valor_total_adlid").setMetodoChange("calcularDetalle");
		tab_detalle_liquidacion_compra.getColumna("cantidad_adlid").setValorDefecto("0");
		tab_detalle_liquidacion_compra.getColumna("valor_total_adlid").setValorDefecto("0");
		tab_detalle_liquidacion_compra.getColumna("valor_total_adlid").setValorDefecto("0");
		tab_detalle_liquidacion_compra.getColumna("valor_unitario_adlid").setValorDefecto("0");
		tab_detalle_liquidacion_compra.getColumna("valor_unitario_adlid").setEtiqueta();
		tab_detalle_liquidacion_compra.getColumna("valor_unitario_adlid").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");// Estilo
		
		tab_detalle_liquidacion_compra.getColumna("activo_adlid").setLectura(true);
		tab_detalle_liquidacion_compra.getColumna("activo_adlid").setValorDefecto("true");

		tab_detalle_liquidacion_compra.dibujar();
		PanelTabla pat_adq_detalle = new PanelTabla();
		pat_adq_detalle.setPanelTabla(tab_detalle_liquidacion_compra);


		Division div_division = new Division();
		div_division.dividir2(pat_adq_factura, pat_adq_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_proveedor = new Boton();
		bot_proveedor.setValue("Proveedor");
		bot_proveedor.setTitle("PROVEEDOR");
		bot_proveedor.setIcon("ui-icon-person");
		bot_proveedor.setMetodo("importarProveedor");
		bar_botones.agregarBoton(bot_proveedor);

		set_proveedor.setId("set_proveedor");
		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("null"), "ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setNombreVisual("Nombre Proveedor");
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setNombreVisual("Ruc Proveedor");
		set_proveedor.getTab_seleccion().getColumna("NOMBRE_TEPRO").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("RUC_TEPRO").setFiltro(true);
		set_proveedor.setTitle("Seleccione Proveedor");
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.setRadio();
		agregarComponente(set_proveedor);
		
		//agregar compromiso presupuesto
        Boton bot_busca=new Boton();
        bot_busca.setIcon("ui-icon-person");
        bot_busca.setValue("Agregar Compromiso Presupuestario");
        bot_busca.setMetodo("importarCompromiso");
        bar_botones.agregarBoton(bot_busca);
        
        set_tramite.setId("set_tramite");
        set_tramite.setSeleccionTabla(ser_Presupuesto.getTramite("true"),"ide_prtra");
        set_tramite.setTitle("SELECCION UN COMPROMISO PRESUPUESTARIO");  
        set_tramite.getTab_seleccion().getColumna("nro_compromiso").setFiltro(true);
        set_tramite.getTab_seleccion().getColumna("numero_oficio_prtra").setFiltro(true);
        set_tramite.getTab_seleccion().getColumna("observaciones_prtra").setFiltro(true);
        
        set_tramite.getBot_aceptar().setMetodo("aceptarCompromiso");
        set_tramite.setRadio();
        agregarComponente(set_tramite);
        
        //Inicio Liquidacion Electronica 
        che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);
 		
 		// BOTÓN LIQUIDACION ELECTRÓNICA ---------------------------------
 		Boton bot_fac_elec = new Boton();
 		bot_fac_elec.setIcon("ui-icon-newwin");
 		bot_fac_elec.setValue("Liquidación Electrónica");
 		bot_fac_elec.setMetodo("abrirDialogoLiqElectronica");
 		bar_botones.agregarBoton(bot_fac_elec);
 		
 		// DIÁLOGO LIQUIDACION ELECTRÓNICA --------------------------------
 		liquidacion_elec_dialogo.setId("liquidacion_elec_dialogo");
 		liquidacion_elec_dialogo.setTitle("GENERAR LIQUIDACIÓN ELECTRÓNICA");
 		liquidacion_elec_dialogo.setWidth("45%");
 		liquidacion_elec_dialogo.setHeight("30%");

 		// GRID LIQUIDACION ELECTRÓNICA
 		Grid gri_fac_elec = new Grid();
 		gri_fac_elec.setColumns(2);
 		liquidacion_elec_dialogo.setDialogo(gri_fac_elec);
 		liquidacion_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoLiqElectronica");
 		agregarComponente(liquidacion_elec_dialogo);

 		// DIÁLOGO RESPUESTA DEL CORE --------------------------------
 		respuesta_core_dialogo.setId("respuesta_core_dialogo");
 		respuesta_core_dialogo.setTitle("RESPUESTA DEL SERVIDOR");
 		respuesta_core_dialogo.setWidth("45%");
 		respuesta_core_dialogo.setHeight("30%");

 		// GRID RESPUESTA DEL CORE
 		Grid gri_respuesta = new Grid();
 		gri_respuesta.setColumns(2);
 		respuesta_core_dialogo.setDialogo(gri_respuesta);
 		respuesta_core_dialogo.getBot_aceptar().setMetodo("aceptarDialogoRespuestaCore");
 		agregarComponente(respuesta_core_dialogo);
 		//FIN Liquidacion Electronica 

	}

	public void importarProveedor() {
		set_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
		set_proveedor.getTab_seleccion().ejecutarSql();
		set_proveedor.dibujar();

	}

	public void aceptarProveedor() {
		String str_seleccionado = set_proveedor.getValorSeleccionado();
		if (!tab_liquidacion_compra.isFilaInsertada()) {
			tab_liquidacion_compra.insertar();
		}
		if (str_seleccionado != null) {
			tab_liquidacion_compra.setValor("ide_tepro", str_seleccionado);
			tab_liquidacion_compra.modificar(tab_liquidacion_compra.getFilaActual());
			if (formatearNumeroLiquidacion()) {
				tab_liquidacion_compra.guardar();
				guardarPantalla();
			}
		}
		set_proveedor.cerrar();
		utilitario.addUpdate("tab_liquidacion_compra");
	}

	public void importarCompromiso(){
		
		if(tab_liquidacion_compra.isEmpty()){
			utilitario.agregarMensajeInfo("No puede buscar un Compromiso", "Debe tener una Liquidación de Compras Guardada");
			return;
		}
		
         set_tramite.getTab_seleccion().setSql(ser_Presupuesto.getTramite("true"));
         set_tramite.getTab_seleccion().ejecutarSql();
         set_tramite.dibujar();

     }
	
	public void aceptarCompromiso(){

        String str_seleccionado = set_tramite.getValorSeleccionado();
        TablaGenerica tab_tramite=ser_Presupuesto.getTablaGenericaTramite(str_seleccionado,"");
        if (str_seleccionado!=null){
        	tab_liquidacion_compra.setValor("ide_prtra",tab_tramite.getValor("ide_prtra"));
            tab_liquidacion_compra.modificar(tab_liquidacion_compra.getFilaActual());
            tab_liquidacion_compra.guardar();
            guardarPantalla();
        }
        set_tramite.cerrar();
        utilitario.addUpdate("tab_liquidacion_compra");
    }

	// /CALCULAR
	public void calcular() {
		// Variables para almacenar y calcular el total del detalle
		double dou_cantidad = 0;
		double dou_valor_unitario = 0;
		double dou_valor_total = 0;
		try {
			// Obtenemos el valor de la cantidad
			dou_cantidad = pckUtilidades.CConversion.CDbl(tab_detalle_liquidacion_compra.getValor("cantidad_adlid"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_valor_total = pckUtilidades.CConversion.CDbl(tab_detalle_liquidacion_compra.getValor("valor_total_adlid"));
		} catch (Exception e) {
		}
		// Calculamos el total
		dou_valor_unitario = dou_valor_total / dou_cantidad;
		// Asignamos el total a la tabla detalle, con 3 decimales
		tab_detalle_liquidacion_compra.setValor("valor_unitario_adlid", utilitario.getFormatoNumero(dou_valor_unitario, 3));
		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_detalle_liquidacion_compra, "valor_unitario_adlid", "tab_detalle_liquidacion_compra");
		
		String valorTotal=tab_detalle_liquidacion_compra.getSumaColumna("valor_total_adlid")+"";
		tab_liquidacion_compra.setValor("subtotal_adlic", utilitario.getFormatoNumero(valorTotal, 2));
		tab_liquidacion_compra.setValor("total_adlic", utilitario.getFormatoNumero(valorTotal, 2));
		tab_liquidacion_compra.modificar(tab_liquidacion_compra.getFilaActual());
		utilitario.addUpdateTabla(tab_liquidacion_compra, "subtotal_adlic,total_adlic", "tab_liquidacion_compra");
	}

	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle_liquidacion_compra.modificar(evt); // Siempre es la primera linea
		calcular();
	}

	private boolean formatearNumeroLiquidacion() {
		boolean correcto = false;
		try {
			String[] numeroFactura = tab_liquidacion_compra.getValor("num_liquidacion_adlic").split("-");
			String nuevoNumeroFactura = "";

			String strSucursal = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[0])), 3);
			String strPuntoEmision = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[1])), 3);
			String strSecuencial = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[2])), 9);

			nuevoNumeroFactura += strSucursal + "-";
			nuevoNumeroFactura += strPuntoEmision + "-";
			nuevoNumeroFactura += strSecuencial;

			tab_liquidacion_compra.setValor("num_liquidacion_adlic", nuevoNumeroFactura);

			if (tab_liquidacion_compra.isFilaInsertada()) {
				if (!ser_Adquisicion.compruebaLiqCompra(strSucursal, strPuntoEmision, strSecuencial))
					correcto = true;
				else
					utilitario.agregarMensajeInfo("No se pudo guardar", "Número de liquidación repetido.");
			} else
				correcto = true;

		} catch (Exception ex) {
			System.out.println("Error al padear el secuencial de la liquidación de compra: " + tab_liquidacion_compra.getValor("num_liquidacion_adlic") + ". " + ex.getMessage());
			tab_liquidacion_compra.setValor("num_liquidacion_adlic", null);
			utilitario.agregarMensajeInfo("No se pudo guardar", "Valide el formato del número de la liquidación de Compra.");
		}
		return correcto;
	}

	// Liquidacion Electrónica -----------------------------------------------------
    public void cambiaAmbiente(){
		if(che_ambiente.getValue().toString().equalsIgnoreCase("true")){
			ambiente=1; //test
		}
		else{
			ambiente=2; //produccion
		}
		
		System.out.println("cambia ambiente Liquidacion: "+ambiente);
	}
	 // Abre el diálogo de confirmación para emitir la liquidacion electrónica
	 public void abrirDialogoLiqElectronica() 
	 {

		 if(!(pckUtilidades.CConversion.CDbl_2(tab_liquidacion_compra.getValor("total_adlic"))==pckUtilidades.CConversion.CDbl_2(tab_detalle_liquidacion_compra.getSumaColumna("valor_total_adlid"))))
		 {
			 utilitario.agregarMensajeInfo("La suma de los totales de los detalles", "No concuerda con el total de la liquidación de compra.");
			 return;
		 }
		 
		// Código del estado de la liquidacion seleccionada
		int estadoLiq = 0;
		try { estadoLiq = Integer.valueOf(tab_liquidacion_compra.getValor("ide_coest")); }
		catch(Exception ex){}
		// Estados de la Liquidación: 2 - Emitido

		// Solo se autorizan las retenciones emitidas
		if (estadoLiq == 2 ) {
			// Limpiando el grid existente
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 2 columnas para el grid existente
			liquidacion_elec_dialogo.getGri_cuerpo().setColumns(2);

			// Agregando una etiqueta vacía
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(new Etiqueta(""));

			// Agregando una etiqueta con la información de la confirmación
			Etiqueta preguntaConfirmacion = new Etiqueta("¿Desea autorizar la siguiente liquidación de compras electrónica en el SRI?");
			preguntaConfirmacion.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(preguntaConfirmacion);

			// Etiqueta con Estilos Ambiente
			Etiqueta etiqueta1 = new Etiqueta("Ambiente: ");
			etiqueta1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta1);

			// Valor con Estilos
			Etiqueta valor1;
			
			if(ambiente==1)
				valor1 = new Etiqueta("PRUEBAS");
			else
				valor1 = new Etiqueta("PRODUCCION");
			
			valor1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:green;border-width: 0px");

			// Agregando el valor del campo
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(valor1);
			
			// Etiqueta con Estilos Secuencial
			Etiqueta etiqueta = new Etiqueta("Secuencial: ");
			etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Valor con Estilos
			Etiqueta valor = new Etiqueta(tab_liquidacion_compra.getValor("num_liquidacion_adlic"));
			valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

			// Agregando el valor del campo
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

			// Agregando la función al botón aceptar
			liquidacion_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoLiqElectronica");
		} else {
			// Limpiando el grid existente
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 1 columna para el grid existente
			liquidacion_elec_dialogo.getGri_cuerpo().setColumns(1);

			// Etiqueta
			Etiqueta etiqueta;

			// Mostrando un mensaje con el estado de la liquidacion
			switch (estadoLiq) {
			case 0:
				etiqueta = new Etiqueta("Seleccione una Liquidación de Compra Válida");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 1:
				etiqueta = new Etiqueta("La Liquidación de Compra fue anulada");
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
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(titulo);
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(new Etiqueta(""));
			liquidacion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Agregando la función al botón aceptar
			liquidacion_elec_dialogo.getBot_aceptar().setMetodo("cerrarDialogoLiqElectronica");
		}

		// Dibujando en pantalla el diálogo
		liquidacion_elec_dialogo.dibujar();
	}

	 // Cierra la confirmación para emitirla liquidacion electrónica
	 public void cerrarDialogoLiqElectronica() {
		// Cerrando el diálogo
		 liquidacion_elec_dialogo.cerrar();
	 }

	 // Abre el diálogo con la respuesta del core de facturación (Liquidacion Electrónica)
	 public void aceptarDialogoLiqElectronica() {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();

		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			//construccion
			respuestaAutorizacion = servicio.procesarLiquidacionCompraElectronica(ambiente, tab_liquidacion_compra.getValor("num_liquidacion_adlic"),autorizar);

			respuestaCabecera = respuestaAutorizacion.get(0);

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replace("Recepcion: ", "");
			
			//respuestaMensaje = "En construccion...";
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
		respuesta_core_dialogo.getBot_cancelar().setStyle("width: 0px;height: 0px");

		// Agregando el mensaje de respuesta del core
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(mensaje);

		// Dibujando el mensaje de respuesta del core
		respuesta_core_dialogo.dibujar();

		// Cerrando el diálogo
		liquidacion_elec_dialogo.cerrar();
	}

	 // Respuesta del Core de Facturación Electrónica ---------------------------
	 // Cierra la respuesta del core de facturación
	 public void aceptarDialogoRespuestaCore() {
		// Cerrando el diálogo
		respuesta_core_dialogo.cerrar();
	 }
   //FIN Facturacion Electronica - AVACA
	 
	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();

	}

	@Override
	public void guardar() {

		// Formato del número de la factura (001-001-000123456)
	
		if (tab_liquidacion_compra.guardar()) {
			if (tab_detalle_liquidacion_compra.guardar()) {
				guardarPantalla();
				tab_liquidacion_compra.ejecutarSql();
				tab_detalle_liquidacion_compra.ejecutarSql();
			}
		}
		
		if(pckUtilidades.CConversion.CStr(tab_liquidacion_compra.getValor("num_liquidacion_adlic")).length() <= 9)
		{
			System.out.println("longitud num_liquidacion_adlic "+pckUtilidades.CConversion.CStr(tab_liquidacion_compra.getValor("num_liquidacion_adlic")).length());
			ser_Adquisicion.actualizarNumeroLiquidacionCompra(tab_liquidacion_compra.getValor("ide_adlic"));
			tab_liquidacion_compra.actualizar();
		}
		else
			formatearNumeroLiquidacion();

	}

	@Override
	public void eliminar() {
		if (tab_liquidacion_compra.isFocus()) {
			if (tab_detalle_liquidacion_compra.getTotalFilas() > 0) {
				utilitario.agregarMensajeError("No se puede borrar", "El presente registro no se puede borrar existen detalles de factura");
				return;
			}
			tab_liquidacion_compra.eliminar();
		}
		if (tab_detalle_liquidacion_compra.isFocus()) {
			tab_detalle_liquidacion_compra.eliminar();
		}
	}



	public SeleccionTabla getSet_tramite() {
		return set_tramite;
	}

	public void setSet_tramite(SeleccionTabla set_tramite) {
		this.set_tramite = set_tramite;
	}

	public Dialogo getRespuesta_core_dialogo() {
		return respuesta_core_dialogo;
	}

	public void setRespuesta_core_dialogo(Dialogo respuesta_core_dialogo) {
		this.respuesta_core_dialogo = respuesta_core_dialogo;
	}

	public Dialogo getLiquidacion_elec_dialogo() {
		return liquidacion_elec_dialogo;
	}

	public void setLiquidacion_elec_dialogo(Dialogo liquidacion_elec_dialogo) {
		this.liquidacion_elec_dialogo = liquidacion_elec_dialogo;
	}

	public Check getChe_ambiente() {
		return che_ambiente;
	}

	public void setChe_ambiente(Check che_ambiente) {
		this.che_ambiente = che_ambiente;
	}

	public Tabla getTab_liquidacion_compra() {
		return tab_liquidacion_compra;
	}

	public void setTab_liquidacion_compra(Tabla tab_liquidacion_compra) {
		this.tab_liquidacion_compra = tab_liquidacion_compra;
	}

	public Tabla getTab_detalle_liquidacion_compra() {
		return tab_detalle_liquidacion_compra;
	}

	public void setTab_detalle_liquidacion_compra(Tabla tab_detalle_liquidacion_compra) {
		this.tab_detalle_liquidacion_compra = tab_detalle_liquidacion_compra;
	}

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

}
