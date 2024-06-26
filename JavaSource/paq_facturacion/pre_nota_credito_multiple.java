package paq_facturacion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import pck_cliente.servicio;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_nota_credito_multiple extends Pantalla {
	
	private  Tabla tab_nota_credito=new Tabla();
	private SeleccionTabla set_factura = new SeleccionTabla();
	private SeleccionTabla set_notaCredito = new SeleccionTabla();
	private Confirmar con_guardar = new Confirmar();
	private AutoCompletar aut_factura=new AutoCompletar(); 
	public static String p_modulo_facturacion;
	public static String p_notadecredito_anulado;
	public static String p_notadecredito_emitido;
	public static String p_factura_anulado;
	public static String p_factura_emitido;
	double duo_valor_iva=0.12;
	String str_notasC_seleccionado="";
	
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
	//Fin 30/12/2015 Facturacion Electronica - ABECERRA
		
	public pre_nota_credito_multiple(){
		
		duo_valor_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); //0.14 de IVA
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		//ambiente=1;

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
		
		p_modulo_facturacion=utilitario.getVariable("p_modulo_facturacion");
		p_notadecredito_anulado=utilitario.getVariable("p_notadecredito_anulado");
		p_notadecredito_emitido=utilitario.getVariable("p_notadecredito_emitido");
		p_factura_anulado=utilitario.getVariable("p_factura_anulado");
		p_factura_emitido=utilitario.getVariable("p_factura_emitido");
		
		// TODO Auto-generated constructor stub
		tab_nota_credito.setId("tab_nota_credito");
		tab_nota_credito.setHeader("NOTA DE CRÉDITO");
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
		tab_nota_credito.getColumna("nro_nota_credito_fanoc").setLongitud(50);
		tab_nota_credito.setCondicion("coalesce(autorizada_sri_fanoc,false) = false and extract(month from fecha_fanoc)=extract(month from now()) and extract(year from fecha_fanoc)=extract(year from now()) and extract(day from fecha_fanoc)=extract(day from now())");
		//tab_nota_credito.setCondicion(" extract(month from fecha_fanoc)>=(extract(month from now())-1) and extract(year from fecha_fanoc)=extract(year from now()) ");
		
		tab_nota_credito.dibujar();
		PanelTabla pat_nota_credito=new PanelTabla();
		pat_nota_credito.setPanelTabla(tab_nota_credito);
		

		Division div_division=new Division();
		div_division.dividir1(pat_nota_credito);
		agregarComponente(div_division);


		Boton bot_factura = new Boton();
		bot_factura.setValue("Agregar Facturas");
		bot_factura.setTitle("Factura");
		bot_factura.setIcon("ui-icon-person");
		bot_factura.setMetodo("importarFactura");
		bar_botones.agregarBoton(bot_factura);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_Facturacion.getCabeceraFactura("1",""),"ide_fafac");
		set_factura.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.getTab_seleccion().ejecutarSql();
		agregarComponente(set_factura);
		
		set_notaCredito.setId("set_notaCredito");
		set_notaCredito.setSeleccionTabla(ser_Facturacion.getCabeceraNotaCredito(),"ide_fanoc");
		set_notaCredito.getTab_seleccion().getColumna("nro_nota_credito_fanoc").setFiltro(true);
		set_notaCredito.getTab_seleccion().getColumna("detalle_fanoc").setFiltro(true);
		set_notaCredito.getBot_aceptar().setMetodo("abrirDialogoNCelectronica");
		set_notaCredito.getTab_seleccion().ejecutarSql();
		agregarComponente(set_notaCredito);

		bar_botones.agregarBoton(bot_factura);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);


		//Inicio 30/12/2015 Facturacion Electronica - ABECERRA
		// DIÁLOGO FACTURA ELECTRÓNICA --------------------------------
		che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);
 		
		nota_credito_elec_dialogo.setId("nota_credito_elec_dialogo");
		nota_credito_elec_dialogo.setTitle("GENERAR NOTAS DE CRÉDITO ELECTRÓNICA");
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
		bot_fac_elec.setValue("Notas de Crédito Electrónicas");
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
 		
 		System.out.println("cambia ambiente nota credito multiple: "+ambiente);
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
	
	// Abre el diálogo de confirmación para emitir la facturación electrónica
	public void abrirDialogoNCelectronica() {
		
		if(set_notaCredito.isVisible()){
			
			if(set_notaCredito.getSeleccionados() !=null){
				str_notasC_seleccionado=set_notaCredito.getSeleccionados();	
				set_notaCredito.cerrar();
				
				///////////////////////////////////////////////////////////////
				// Limpiando el grid existente
				nota_credito_elec_dialogo.getGri_cuerpo().getChildren().clear();

				// Configurando 2 columnas para el grid existente
				nota_credito_elec_dialogo.getGri_cuerpo().setColumns(2);

				// Agregando una etiqueta vacía
				nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(new Etiqueta(""));

				// Agregando una etiqueta con la información de la confirmación
				Etiqueta preguntaConfirmacion = new Etiqueta("¿Desea autorizar las notas de crédito electrónicas seleccionadas en el SRI?");
				preguntaConfirmacion.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

				nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(preguntaConfirmacion);

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
				Etiqueta etiqueta = new Etiqueta("Códigos: ");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

				// Agregando la etiqueta
				nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

				// Valor con Estilos
				Etiqueta valor = new Etiqueta(str_notasC_seleccionado);
				valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

				// Agregando el valor del campo
				nota_credito_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

				// Agregando la función al botón aceptar
				nota_credito_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoNCelectronica");
				// Dibujando en pantalla el diálogo
				nota_credito_elec_dialogo.dibujar();
				//////////////////////////////////////////////////////////////
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
			
		}
		else {
			//set_notaCredito.getTab_seleccion().ejecutarSql();
			set_notaCredito.dibujar();
		}

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
		String secuenciales=" - ";
		boolean autorizado=true;

		try {
			
			TablaGenerica tab_NotasCredito= utilitario.consultar("select ide_fanoc,nro_nota_credito_fanoc,ide_coest,autorizada_sri_fanoc from fac_nota_credito where ide_fanoc in ("+str_notasC_seleccionado+")");
			
			for(int i=0;i<tab_NotasCredito.getTotalFilas();i++){
				
				if(!pckUtilidades.CConversion.CBol(tab_NotasCredito.getValor(i,"autorizada_sri_fanoc")))
				{
					if(tab_NotasCredito.getValor(i,"nro_nota_credito_fanoc").length()>9)
					{
						//ser_Facturacion.actualizarNumeroNotaCredito(tab_nota_credito.getValor(i,"ide_fanoc"));
						if(p_notadecredito_emitido.equals(tab_NotasCredito.getValor(i,"ide_coest")) ) {
							
							respuestaAutorizacion = servicio.procesarNotaCreditoElectronica(ambiente, tab_NotasCredito.getValor(i,"nro_nota_credito_fanoc"),duo_valor_iva+"",autorizar);
							respuestaCabecera = respuestaAutorizacion.get(0);
							
							if(!respuestaCabecera.equals("AUTORIZADO"))
							{
								autorizado=false;
								break;
							}
							else
								secuenciales += tab_NotasCredito.getValor(i,"nro_nota_credito_fanoc")+" ; ";
						}
					}
				}
			}
			
			if(!autorizado)
			{
				for (String item : respuestaAutorizacion) {
					if (!item.contentEquals(respuestaCabecera)) {
						respuestaMensaje += " " + item + ".";
					}
				}
			}
			else
				respuestaMensaje="Las notas de credito: "+secuenciales+" han sido autorizadas.";

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
		respuesta_core_dialogo.getBot_cancelar().setStyle("width: 0px;height: 0px");

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
		String str_seleccionado = set_factura.getSeleccionados();
		System.out.println("entra al str  "+str_seleccionado);
		if (str_seleccionado!=null){
			TablaGenerica tab_aceptarfactura = ser_Facturacion.getTablaGenericaFacturaCabecera(str_seleccionado);		
			System.out.println("aceptarFactura tabla generica: "+tab_aceptarfactura.getSql());

			for(int i=0;i<tab_aceptarfactura.getTotalFilas();i++){
				tab_nota_credito.insertar();
				tab_nota_credito.setValor("ide_fafac", tab_aceptarfactura.getValor(i,"ide_fafac"));			
				tab_nota_credito.setValor("valor_referencial_fanoc", tab_aceptarfactura.getValor(i,"base_aprobada_fafac"));			
				tab_nota_credito.setValor("iva_fanoc", tab_aceptarfactura.getValor(i,"valor_iva_fafac"));
				tab_nota_credito.setValor("total_fanoc",tab_aceptarfactura.getValor(i,"total_fafac"));
				tab_nota_credito.setValor("detalle_fanoc","Se anula por error del sistema mientras se autorizaba en el SRI, se emite en remplazo de la FACTURA Nro. "+tab_aceptarfactura.getValor(i,"secuencial_fafac"));
				//tab_nota_credito.setValor("ide_fafac",str_seleccionado);
			}
		}
		set_factura.cerrar();
		utilitario.addUpdate("tab_nota_credito");
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("No puede Insertar una nota de crédito.", "Usar el boton Agregar Facturas");
		return;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		String sql="";
		
		if(tab_nota_credito.guardar())
		{
			for(int i=0;i<tab_nota_credito.getTotalFilas();i++){
			
				if(!pckUtilidades.CConversion.CBol(tab_nota_credito.getValor(i,"autorizada_sri_fanoc")))
				{
					if(pckUtilidades.CConversion.CStr(tab_nota_credito.getValor(i,"nro_nota_credito_fanoc")).length() <= 9)
						ser_Facturacion.actualizarNumeroNotaCredito(tab_nota_credito.getValor(i,"ide_fanoc"));
					
					if(p_notadecredito_emitido.equals(tab_nota_credito.getValor(i,"ide_coest")) ) {
						
						int ide_fafac=pckUtilidades.CConversion.CInt(tab_nota_credito.getValor(i,"ide_fafac"));
						sql="update fac_factura set ide_coest ="+ p_factura_anulado + ",razon_anulado_fafac='"+tab_nota_credito.getValor(i,"detalle_fanoc")+"', fecha_anulado_fafac=now()  where ide_fafac = "+ ide_fafac ;
		
						utilitario.getConexion().ejecutarSql(sql);
						System.out.println(" entre nota de c emitida "+sql);
					
					}else if (p_notadecredito_anulado.equals(tab_nota_credito.getValor(i,"ide_coest"))){
						sql="update fac_factura set ide_coest ="+ p_factura_emitido + " where ide_fafac = "+ tab_nota_credito.getValor(i,"ide_fafac");
						utilitario.getConexion().ejecutarSql(sql);	
						System.out.println(" entre nota de c anulado "+sql);
					
					}
				}
			}
			guardarPantalla();
			tab_nota_credito.ejecutarSql();
			utilitario.agregarMensajeInfo("Para Asignar un Numero de NC.", "Volver a presionar el Boton Guardar...");
		}
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

	public SeleccionTabla getSet_notaCredito() {
		return set_notaCredito;
	}

	public void setSet_notaCredito(SeleccionTabla set_notaCredito) {
		this.set_notaCredito = set_notaCredito;
	}

	

}
