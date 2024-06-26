package paq_facturacion;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

import org.apache.poi.hssf.record.CalcCountRecord;
import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Etiqueta;


public class pre_facturacion_lugar extends Pantalla{

	private Tabla tab_factura = new Tabla();
	private Tabla tab_detalle_factura=new Tabla();
	private AutoCompletar aut_factura=new AutoCompletar();
	private Dialogo crear_cliente_dialogo=new Dialogo();
	private Tabla tab_cliente=new Tabla();
	private Tabla tab_direccion= new Tabla();

	//SELCCION TABLA
	private SeleccionTabla set_pantalla_dias= new SeleccionTabla();
	private SeleccionTabla set_insertarbodega = new SeleccionTabla();
	private SeleccionTabla set_pantallacliente= new SeleccionTabla();
	private SeleccionTabla set_actualizar_cliente = new SeleccionTabla();
	private SeleccionTabla set_crear_cliente = new SeleccionTabla();
	private Confirmar con_guardar_cliente=new Confirmar();

	private Etiqueta eti_total= new Etiqueta();


	//CALENDARIO
	private SeleccionCalendario sec_rango_fechas = new SeleccionCalendario();
	private String srt_fecha_inicio;
	private String srt_fecha_fin;
	private double dou_por_iva=0.12;
	double dou_base_no_iva=0;
	double dou_base_cero=0;
	double dou_base_aprobada=0;
	double dou_valor_iva=0;
	double dou_total=0;
	private String valor;
	private String mensaje;
	private List<Fila> lis_fechas_seleccionadas;

	private Dialogo dia_valor=new Dialogo();
	private Texto tex_valor=new Texto();
	String str_smaterial_seleccionado;
	
	//REPORTE
		private Map p_parametros = new HashMap();
		private Reporte rep_reporte = new Reporte();
		private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
		private Map map_parametros = new HashMap();


	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	
	
	public pre_facturacion_lugar() {
		
		dou_por_iva=Double.parseDouble(utilitario.getVariable("p_valor_iva"));
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);

		

		
		// TODO Auto-generated constructor stub

		tab_factura.setHeader("FACTURACIÓN");
		tab_factura.setId("tab_factura");
		tab_factura.setTabla("fac_factura", "ide_fafac", 1);
		tab_factura.setGenerarPrimaria(false);
		tab_factura.getColumna("ide_fafac").setLectura(true);
		//para q no se dibuje antes q seleccione el autocompletar
		tab_factura.setCondicion("ide_fadaf=-1");
		tab_factura.setTipoFormulario(true);
		tab_factura.getGrid().setColumns(6);
		tab_factura.getColumna("ide_fadaf").setVisible(false);
		tab_factura.getColumna("ide_comov").setVisible(false);
		tab_factura.getColumna("ide_sucu").setCombo("sis_sucursal", "ide_sucu", "nom_sucu", "");
		tab_factura.getColumna("ide_tetid").setVisible(false);
		tab_factura.getColumna("ide_tedar").setVisible(false);
		tab_factura.getColumna("ide_coest").setVisible(false);
		tab_factura.getColumna("ide_geins").setVisible(false);
		tab_factura.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_factura.getColumna("ide_recli").setAutoCompletar();
		tab_factura.getColumna("ide_recli").setLectura(true);
		tab_factura.getColumna("ide_falug").setCombo("fac_lugar", "ide_falug", "detalle_lugar_falug", "");
		tab_factura.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		tab_factura.getColumna("ide_retip").setValorDefecto(utilitario.getVariable("p_tipo_cobro_factura"));
		tab_factura.getColumna("ide_retip").setValorDefecto("4");
		tab_factura.getColumna("ide_gtemp").setVisible(false);
		tab_factura.getColumna("ide_falug").setVisible(false);
		
		//TOTALES DE COLOR ROJO--ESTILO DE COLOR ROJO Y NEGRILLA
		tab_factura.getColumna("base_no_iva_fafac").setEtiqueta();
		tab_factura.getColumna("base_no_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.setCampoOrden("ide_fafac desc");
		tab_factura.getColumna("base_cero_fafac").setEtiqueta();
		tab_factura.getColumna("base_cero_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("base_aprobada_fafac").setEtiqueta();
		tab_factura.getColumna("base_aprobada_fafac").setFormatoNumero(2);
		tab_factura.getColumna("base_aprobada_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("valor_iva_fafac").setEtiqueta();
		tab_factura.getColumna("valor_iva_fafac").setFormatoNumero(2);
		tab_factura.getColumna("valor_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("total_fafac").setEtiqueta();
		tab_factura.getColumna("total_fafac").setFormatoNumero(2);
		tab_factura.getColumna("total_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("secuencial_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.getColumna("secuencial_fafac").setEtiqueta();
		tab_factura.getColumna("fecha_transaccion_fafac").setMetodoChange("fechaVencimiento");
		tab_factura.getColumna("fecha_emision_fafac").setVisible(false);
		tab_factura.getColumna("direccion_fafac").setVisible(false);
		tab_factura.getColumna("responsable_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("codigo_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("activo_fafac").setValorDefecto("true");
		tab_factura.getColumna("activo_fafac").setLectura(true);
		tab_factura.getColumna("conciliado_fafac").setLectura(true);
		tab_factura.getColumna("num_comprobante_fafac").setVisible(false);
		tab_factura.getColumna("fecha_transaccion_fafac").setValorDefecto(utilitario.getFechaActual());
		tab_factura.getColumna("base_no_iva_fafac").setVisible(false);
		tab_factura.getColumna("observacion_fafac").setVisible(false);
		tab_factura.getColumna("razon_anulado_fafac").setVisible(false);
		tab_factura.getColumna("conciliado_fafac").setVisible(false);
		tab_factura.getColumna("fecha_anulado_fafac").setVisible(false);
		tab_factura.getColumna("codigo_faclinea_fafac").setVisible(false);
		tab_factura.getColumna("factura_fisica_fafac").setVisible(true);
		tab_factura.getColumna("responsable_faclinea_fafac").setVisible(false);

		tab_factura.dibujar();
		tab_factura.agregarRelacion(tab_detalle_factura);

		PanelTabla pat_factura=new PanelTabla();
		pat_factura.setPanelTabla(tab_factura);

		// TABLA 2
		tab_detalle_factura.setId("tab_detalle_factura");
		tab_detalle_factura.setTabla("fac_detalle_factura", "ide_fadef", 2);
		tab_detalle_factura.setGenerarPrimaria(false);
		tab_detalle_factura.getColumna("cantidad_fadef").setValorDefecto("0");
		tab_detalle_factura.getColumna("activo_fadef").setValorDefecto("true");
		tab_detalle_factura.getColumna("activo_fadef").setLectura(true);

		tab_detalle_factura.setCampoForanea("ide_fafac");

		// ide_bomat---setcombo y set autocompletar
		tab_detalle_factura.getColumna("ide_bomat").setCombo(ser_bodega.getInventario("1","true,false",""));
		tab_detalle_factura.getColumna("ide_bomat").setAutoCompletar();
		//definimos el metodo que va a ejecutar cuando el usuario seleccione del Autocompletar
		tab_detalle_factura.getColumna("ide_bomat").setMetodoChange("seleccionoProducto");


		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		String condicion="select ide_bogrm from fac_venta_lugar a, fac_usuario_lugar b where a.ide_falug=b.ide_falug and ide_usua in ("+utilitario.getVariable("ide_usua")+")";
	//	System.out.println(" gfgfgf "+condicion);
		aut_factura.setId("aut_factura");     
		aut_factura.setAutoCompletar(ser_facturacion.getDatosFactura("0",condicion)); //1 carga todos, 0 carga por grupos enviados
		aut_factura.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar

		Etiqueta eti_colaborador=new Etiqueta("FACTURACIÓN:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);


		//DETALLE FACTURA
		tab_detalle_factura.getColumna("total_fadef").setEtiqueta();
		tab_detalle_factura.getColumna("total_fadef").setEstilo("font-size:13px;font-weight:bold;");
		tab_detalle_factura.getColumna("valor_fadef").setEtiqueta();
		tab_detalle_factura.getColumna("valor_fadef").setEstilo("font-size:13px;font-weight:bold;");
		//LLAMAR A ESTE METODO CUANDO EL USUARIO, MODIFIQUE LA CANTIDAD O EL VALOR DESDE LA APLICACION
		tab_detalle_factura.getColumna("cantidad_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("valor_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("fecha_fadef").setVisible(false);
		tab_detalle_factura.getColumna("observacion_fadef").setVisible(false);
		tab_detalle_factura.getColumna("activo_fadef").setValorDefecto("true");
		tab_detalle_factura.getColumna("activo_fadef").setVisible(false);
		tab_detalle_factura.dibujar();


		PanelTabla pat_detalle_factura= new PanelTabla();
		pat_detalle_factura.setMensajeWarn("DETALLE FACTURACION");
		pat_detalle_factura.setPanelTabla(tab_detalle_factura);

		eti_total.setId("eti_total");
		eti_total.setStyle("font-size:18px;color:red;widht:100%");
		eti_total.setValue("TOTAL : 0.00");
		Division div_aux=new Division();
		div_aux.setFooter(pat_detalle_factura, eti_total,"85%");


		Division div_division=new Division();
		div_division.dividir2(pat_factura, div_aux, "50%", "h");
		agregarComponente(div_division);


		//BOTON FACRURAR PERIODO
		Boton bot_abrir_periodos= new Boton();
		bot_abrir_periodos.setValue("Facturar Periódo");
		bot_abrir_periodos.setIcon("ui-calendario");
		bot_abrir_periodos.setMetodo("abrirRango");
		bar_botones.agregarBoton(bot_abrir_periodos);
		sec_rango_fechas.setId("sec_rango_fechas");
		sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRango");
		sec_rango_fechas.setFechaActual();



		//---SELECCION TABLA
		set_pantalla_dias.setId("set_pantalla_dias");
		set_pantalla_dias.setTitle("PANTALLA SELECCIONAR RANGO");
		//CONSULTA
		set_pantalla_dias.setSeleccionTabla("select ide_empr,''  as dia, '' as fecha from sis_empresa where ide_empr=-1",  "ide_empr");
		//set_pantalla_dias.setSql("select fecha_ingre as fecha from fac_detalle_factura");
		set_pantalla_dias.getBot_aceptar().setMetodo("aceptarSeleccionFechas");
		//BOTON PARA ABRIR LA TABLA
		Boton bot_abrir_dias= new Boton();
		bot_abrir_dias.setMetodo("abrirSeleccionTabla");
		agregarComponente(set_pantalla_dias);
		agregarComponente(sec_rango_fechas);



		//PANTALLA ESCOGER BODEGA
		set_insertarbodega.setId("set_insertarbodega");
		set_insertarbodega.setTitle("SELECCIONE MATERIAL DE BODEGA");
		set_insertarbodega.setRadio();
		//CONSULTA
		set_insertarbodega.setSeleccionTabla("bodt_material", "ide_bomat", "detalle_bomat,aplica_valor_bomat");
		set_insertarbodega.getBot_aceptar().setMetodo("aceptarBodega");
		set_insertarbodega.getTab_seleccion().getColumna("detalle_bomat").setNombreVisual("MATERIAL");
		set_insertarbodega.getTab_seleccion().getColumna("aplica_valor_bomat").setVisible(false);//Oculta campo
		//set_insertarBodega.getTab_seleccion().getColumna("valor_bomat").setVisible(false);//Oculta campo
		agregarComponente(set_insertarbodega);


		//PANTALLA TEXT0
		dia_valor.setId("dia_valor");
		dia_valor.setTitle("INGRESE VALOR DEL MATERIAL");
		dia_valor.getBot_aceptar().setMetodo("AceptarValor");
		dia_valor.setWidth("25%");
		dia_valor.setHeight("18%");

		Grid gri_valor=new Grid();
		gri_valor.setColumns(2);
		gri_valor.getChildren().add(new Etiqueta("Valor Aplica: "));
		tex_valor.setSoloNumeros();
		gri_valor.getChildren().add(tex_valor);

		dia_valor.setDialogo(gri_valor);		
		agregarComponente(dia_valor);

		// PARA Q SALGA EN UNA SOLA PAGINA
		set_pantalla_dias.getTab_seleccion().setRows(200);

		//Boton Actualizar Cliente
		Boton bot_actualizar_cliente=new Boton();
		bot_actualizar_cliente.setIcon("ui-icon-person");
		bot_actualizar_cliente.setValue("Actualizar Cliente");
		bot_actualizar_cliente.setMetodo("actualizarCliente");
		bar_botones.agregarBoton(bot_actualizar_cliente);

		con_guardar_cliente.setId("con_guardar_cliente");
		agregarComponente(con_guardar_cliente);

		/**
		 *  ESTE COMPONENTE NO ESTAN OCUPANDO, en lugar de este se visualiza   set_pantallacliente
		 *  
		 *  OJO
		 */
		set_actualizar_cliente.setId("set_actualizar_cliente");
		set_actualizar_cliente.setSeleccionTabla(ser_facturacion.getClientes("0,1"),"ide_recli");
		set_actualizar_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_actualizar_cliente.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		set_actualizar_cliente.setRadio();
		set_actualizar_cliente.getBot_aceptar().setMetodo("modificarCliente");
		agregarComponente(set_actualizar_cliente);

		//

		//BOTON AGREGAR CLIENTE
		Boton bot_agregarCliente=new Boton();
		bot_agregarCliente.setValue("Agregar Cliente");
		bot_agregarCliente.setIcon("ui-icon-person");
		bot_agregarCliente.setMetodo("agregarCliente");
		bar_botones.agregarBoton(bot_agregarCliente);

		//PANTALLA SELECIONAR CLIENTE
		set_pantallacliente.setId("set_pantallacliente");
		set_pantallacliente.setTitle("SELECCIONE CLIENTES");
		set_pantallacliente.getBot_aceptar().setMetodo("aceptarCliente");
		set_pantallacliente.setSeleccionTabla(ser_facturacion.getClientes("0,1"),"ide_recli");
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantallacliente.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		set_pantallacliente.setRadio();
		set_pantallacliente.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantallacliente);




		//PANTALLA SELECIONAR CLIENTE
		set_crear_cliente.setId("set_crear_cliente");
		set_crear_cliente.setTitle("SELECCIONE CLIENTES");
		set_crear_cliente.getBot_aceptar().setMetodo("aceptarCliente");
		set_crear_cliente.setSeleccionTabla(ser_facturacion.getClientes("0,1"),"ide_recli");
		set_crear_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_crear_cliente.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		set_crear_cliente.setRadio();
		set_crear_cliente.getTab_seleccion().ejecutarSql();
		agregarComponente(set_crear_cliente);


		//PANTALLA CREAR CLIENTE
		crear_cliente_dialogo.setId("crear_cliente_dialogo");
		crear_cliente_dialogo.setTitle("CREAR CLIENTE");
		crear_cliente_dialogo.setWidth("45%");
		crear_cliente_dialogo.setHeight("45%");
		Grid gri_cuerpo=new Grid();
		tab_cliente.setId("tab_cliente");
		tab_cliente.setTabla("rec_clientes", "ide_recli",10);
		tab_cliente.setTipoFormulario(true);
		tab_cliente.setCondicion("ide_recli=-1");//para que aparesca vacia
		tab_cliente.getGrid().setColumns(2);
		//oculto todos los campos
		for(int i=0;i<tab_cliente.getTotalColumnas();i++){
			tab_cliente.getColumnas()[i].setVisible(false);
		}
		//Muestro solo los campos necesarios
		tab_cliente.getColumna("nombre_comercial_recli").setVisible(true);	
		tab_cliente.getColumna("nombre_comercial_recli").setNombreVisual("NOMBRE COMERCIAL");
		tab_cliente.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_cliente.getColumna("ide_gttdi").setVisible(true);
		tab_cliente.getColumna("ide_gttdi").setValorDefecto("3");
		tab_cliente.getColumna("ide_gttdi").setNombreVisual("DOCUMENTO IDENTIDAD");
		tab_cliente.getColumna("ruc_comercial_recli").setVisible(true);
		tab_cliente.getColumna("ruc_comercial_recli").setNombreVisual("RUC COMERCIAL DEL CLIENTE");
		tab_cliente.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_cliente.getColumna("telefono_factura_recli").setVisible(true);
		tab_cliente.getColumna("telefono_factura_recli").setNombreVisual("TELEFONO");
		tab_cliente.getColumna("factura_datos_recli").setValorDefecto("1");
		tab_cliente.getColumna("aplica_mtarifa_recli").setValorDefecto("false");
		tab_cliente.setMostrarNumeroRegistros(false);//PARA Q NO MUESTRE EL TITULO REGSITRO 1 DE 1
		//tab_cliente.getColumna("matriz_sucursal_recli").setValorDefecto("1");
		tab_cliente.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_cliente.getColumna("ide_tetar").setVisible(true);
		tab_cliente.getColumna("ide_tetar").setValorDefecto("1");
		tab_cliente.getColumna("ide_tetar").setNombreVisual("TARIFA");
		tab_cliente.dibujar();

		gri_cuerpo.getChildren().add(tab_cliente);

		//DIRECCION 
		tab_direccion.setId("tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion", "ide_recld", 11);
		tab_direccion.setMostrarNumeroRegistros(false);//PARA Q NO MUESTRE EL TITULO REGSITRO 1 DE 1
		tab_direccion.setTipoFormulario(true);
		//oculto todos los campos
		for(int i=0;i<tab_direccion.getTotalColumnas();i++){
			tab_direccion.getColumnas()[i].setVisible(false);
		}
		tab_direccion.getColumna("direccion_recld").setNombreVisual("Dirección");
		tab_direccion.getColumna("direccion_recld").setVisible(true);
		tab_direccion.getColumna("activo_recld").setValorDefecto("true");
		tab_direccion.setCondicion("ide_recld=-1");
		tab_direccion.dibujar();

		gri_cuerpo.getChildren().add(tab_direccion);



		crear_cliente_dialogo.getBot_aceptar().setMetodo("aceptarDialogoCliente");

		crear_cliente_dialogo.setDialogo(gri_cuerpo);
		agregarComponente(crear_cliente_dialogo);

		//BOTON CREAR CLIENTE
		Boton bot_crearCliente=new Boton();
		bot_crearCliente.setValue("Crear Cliente");
		bot_crearCliente.setIcon("ui-icon-person");
		bot_crearCliente.setMetodo("abrirDialogoCliente");
		bar_botones.agregarBoton(bot_crearCliente);

	}
	
	public void fechaVencimiento(AjaxBehaviorEvent evt){		
		tab_factura.modificar(evt);
					
		int numero_dias=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_dias_calculo_interes_mora_nd"));
		String fecha= tab_factura.getValor("fecha_transaccion_fafac");
		Date fecha_a_sumar=utilitario.DeStringADate(fecha);		
		Date nueva_fecha=utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
		String str_fecha=utilitario.DeDateAString(nueva_fecha);
		tab_factura.setValor("fecha_vencimiento_fafac", str_fecha);
		utilitario.addUpdateTabla(tab_factura, "fecha_vencimiento_fafac", "");

	}
	
	
	public void fechaVencimientoInserta(){		
			
		int numero_dias=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_dias_calculo_interes_mora_nd"));
		String fecha= tab_factura.getValor("fecha_transaccion_fafac");
		Date fecha_a_sumar=utilitario.DeStringADate(fecha);		
		Date nueva_fecha=utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
		String str_fecha=utilitario.DeDateAString(nueva_fecha);
		tab_factura.setValor("fecha_vencimiento_fafac", str_fecha);
		utilitario.addUpdate("tab_factura");
	}

	 
	
	public void abrirDialogoCliente(){

		//Hace aparecer el component
		if(aut_factura.getValor()!=null){
			tab_cliente.limpiar();
			tab_cliente.insertar();
			tab_direccion.limpiar();
			tab_direccion.insertar();
			crear_cliente_dialogo.dibujar();
		}
		else{
			utilitario.agregarMensaje("Requiere ingresar una factura para crear el cliente", "");
		}

	}

	public void aceptarDialogoCliente(){
		
		if(!validarDocumentoIdentidad(tab_cliente.getValor("ide_gttdi"), tab_cliente.getValor("ruc_comercial_recli"))){
			System.out.println("entre a validar cedula");
			tab_cliente.setValor("ruc_comercial_recli","");
			utilitario.addUpdate("tab_clientes");
			return;
			
		}

		if(tab_cliente.guardar()){ //si guarda el gliente cierra el dialogo

			tab_direccion.setValor("ide_recli",  tab_cliente.getValor("ide_recli"));
			if(tab_direccion.guardar())
			{
				if(guardarPantalla().isEmpty()){
					crear_cliente_dialogo.cerrar();	
					tab_factura.actualizarCombos();//actualiza los combos para que aparezca el nuevo cliente	
					if(tab_factura.isFilaInsertada()==false){
						tab_factura.insertar();	
					}
					//CARGA EL CLIENTE Q SE INSERTO
					tab_factura.setValor("ide_recli",  tab_cliente.getValor("ide_recli"));
					tab_factura.setValor("ide_fadaf",  aut_factura.getValor());

				}				
			}

		}

	}

	public void agregarCliente(){
		utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		//Hace aparecer el componente
		if(aut_factura.getValor()!=null){
			set_pantallacliente.getTab_seleccion().setSql(ser_facturacion.getClientes("0,1"));
			set_pantallacliente.getTab_seleccion().ejecutarSql();
			set_pantallacliente.dibujar();
		}	
	}

	public void aceptarCliente(){
		String str_seleccionado=set_pantallacliente.getValorSeleccionado();
		System.out.println("Entrar al aceptar"+str_seleccionado);
		if(str_seleccionado!=null){
			//Inserto los cleintes seleccionados en la tabla  
			if(tab_factura.isFilaInsertada()==false){
				//Controla que si ya esta insertada no vuelva a insertar
				tab_factura.insertar();	
			}

			tab_factura.setValor("ide_recli", str_seleccionado);				
			tab_factura.setValor("ide_fadaf", aut_factura.getValor());
			set_pantallacliente.cerrar();
			utilitario.addUpdate("tab_factura");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}

	//ACTUALIZAR CLIENTE
	public void actualizarCliente(){
	
		set_actualizar_cliente.getTab_seleccion().setSql(ser_facturacion.getClientes("0,1"));
		set_actualizar_cliente.getTab_seleccion().ejecutarSql();
		set_actualizar_cliente.dibujar();
	}

	public void modificarCliente(){
		String str_clienteActualizado=set_actualizar_cliente.getValorSeleccionado();
		if(str_clienteActualizado!=null){

			tab_factura.modificar(tab_factura.getFilaActual());
			tab_factura.setValor("ide_recli", str_clienteActualizado);
			utilitario.addUpdate("tab_factura");

			con_guardar_cliente.setMessage("Esta Seguro de Actualizar el Cliente");
			con_guardar_cliente.setTitle("Confirmación de actualizar");
			con_guardar_cliente.getBot_aceptar().setMetodo("guardarActualizarCliente");
			con_guardar_cliente.dibujar();
			utilitario.addUpdate("con_guardar_cliente");
		}

	}

	public void guardarActualizarCliente(){
		tab_factura.guardar();
		con_guardar_cliente.cerrar();
		set_actualizar_cliente.cerrar();

		guardarPantalla();
	}

	private void insertarMaterial(String ide_bomat,String valor){
		//Inserta en la tabla de detalles el matiial seleccionado
		for (int j = 0; j < lis_fechas_seleccionadas.size(); j++) {
			Object[] fila =lis_fechas_seleccionadas.get(j).getCampos();
			//Obtenemos el campo de la fecha seleccionada
			System.out.println("entre a este metodo");
			String str_fecha_actual =fila[2]+"";
			tab_detalle_factura.insertar(); //inserto
			tab_detalle_factura.setValor("ide_bomat",ide_bomat);//asigno material
			tab_detalle_factura.setValor("fecha_fadef",str_fecha_actual);//asig fecha
			tab_detalle_factura.setValor("observacion_fadef",
			tab_detalle_factura.getValorArreglo("ide_bomat",2)+" "+str_fecha_actual);
			tab_detalle_factura.setValor("valor_fadef",valor);
			tab_detalle_factura.setValor("total_fadef",valor);

		}
		calcularFactura();//calcula los totales
	}


	public void aceptarBodega(){
		String str_seleccionado=set_insertarbodega.getValorSeleccionado();//x q es radio
		TablaGenerica validarTarifaUnica=utilitario.consultar("select ide_recli, aplica_mtarifa_recli from rec_clientes where aplica_mtarifa_recli=false and ide_recli="+tab_factura.getValor("ide_recli"));
		TablaGenerica retornarValorUnico= utilitario.consultar(" select a.ide_recli, a.aplica_mtarifa_recli, valor_temat from rec_clientes a, tes_material_tarifa c  where  a.ide_tetar= c.ide_tetar  and a.ide_recli="+tab_factura.getValor("ide_recli")+" and ide_bomat="+set_insertarbodega.getValorSeleccionado());
		TablaGenerica retornaValorMultiple=utilitario.consultar("select ide_teclt,a.ide_recli,ide_bomat,valor_temat from tes_cliente_tarifa a, rec_clientes b,tes_material_tarifa c"+
				" where a.ide_recli = b.ide_recli and a.ide_temat = c.ide_temat and a.ide_recli ="+tab_factura.getValor("ide_recli")+" and ide_bomat="+set_insertarbodega.getValorSeleccionado());

		if(validarTarifaUnica.isEmpty()){
			valor=retornaValorMultiple.getValor("valor_temat");
			System.out.println("Multiple "+valor);

		}
		else {
			valor=retornarValorUnico.getValor("valor_temat");
			System.out.println("Valor Unico "+valor);
		}

		if(str_seleccionado!=null){// valido que seleccione

			for (int j = 0; j < lis_fechas_seleccionadas.size(); j++) {
				Object[] fila =lis_fechas_seleccionadas.get(j).getCampos();
				//Obtenemos el campo de la fecha seleccionada
				String str_fecha_actual =fila[2]+"";

				tab_detalle_factura.insertar(); //inserto
				tab_detalle_factura.setValor("ide_bomat",str_seleccionado);//asigno material
				tab_detalle_factura.setValor("fecha_fadef",str_fecha_actual);//asig material
				tab_detalle_factura.setValor("valor_fadef",valor);
				tab_detalle_factura.setValor("total_fadef",valor);
				tab_detalle_factura.setValor("observacion_fadef",
				tab_detalle_factura.getValorArreglo("ide_bomat", 2)+""+str_fecha_actual);
				tab_detalle_factura.setValor("cantidad_fadef", "1");
				

			}
			set_insertarbodega.cerrar();
			calcular();
			utilitario.addUpdate("tab_detalle_factura");
			utilitario.addUpdate("tab_factura");
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar un material", "");
		}
	}



	public void AceptarValor(){
		if(tex_valor.getValue()!=null){
			String str_valor=tex_valor.getValue()+"";
			insertarMaterial(str_smaterial_seleccionado,str_valor+"");
			dia_valor.cerrar();
			utilitario.addUpdate("tab_detalle_factura");
			utilitario.addUpdate("tab_factura");
		}
		else{
			utilitario.agregarMensajeError("Debe Ingresar un valor", "");	
		}

	}

	public void aceptarSeleccionFechas(){
		String str_seleccionados=set_pantalla_dias.getSeleccionados();
		if(str_seleccionados!=null){ //valida que seleccione almenos 1 dia
			utilitario.agregarMensaje("Buscar dias",set_pantalla_dias.getSeleccionados()+"");
			//Fechas que selecciono
			lis_fechas_seleccionadas=set_pantalla_dias.getListaSeleccionados(); // seleccionar filas
			set_pantalla_dias.cerrar(); //cierro seleccion dias
			set_insertarbodega.getTab_seleccion().ejecutarSql();
			set_insertarbodega.dibujar(); //abro seleccion bodega
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar almenos un día", "");
		}
	}

	public void abrirRango(){
		utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		//Hace aparecer el componente
		if(aut_factura.getValor()!=null){
			sec_rango_fechas.dibujar();
		}
	}


	public void aceptarRango(){
		//Si las fechas seleccionadas son validas, muestra las fechas seleccionadas
		if(sec_rango_fechas.isFechasValidas()){
			//Almacenamos las fechas seleccionadas en variables
			srt_fecha_inicio=sec_rango_fechas.getFecha1String();
			srt_fecha_fin=sec_rango_fechas.getFecha2String();
			//Valiada que maxiomo seleccione un mes
			int int_num_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(srt_fecha_inicio),utilitario.getFecha(srt_fecha_fin));
			if(int_num_dias<=31){
				//Cerramos el seleccionCalendario
				sec_rango_fechas.cerrar();       

				//Abrimos el seleccionTabla
				set_pantalla_dias.setDynamic(false);
				set_pantalla_dias.dibujar();
				insertarDias();  // llenamos la tabla
			}
			else{
				utilitario.agregarMensajeError("El rango debe ser máximo de un mes(31 días)", " Seleccione un rango menor");
			}

		}

		else{
			utilitario.agregarMensajeError("Las fecha seleccionadas no son válidas", "");
		}
	}

	public void insertarDias(){
		//limpiamos el seleccion tabla
		set_pantalla_dias.getTab_seleccion().limpiar();
		set_pantalla_dias.getTab_seleccion().setLectura(false);//para qpermita insertar
		//Obtenemos el numero de dias entre las dos fechas
		int int_num_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(srt_fecha_inicio), utilitario.getFecha(srt_fecha_fin));
		//System.out.println("NUM DIAS: "+int_num_dias);
		//Insertamos el rango de dias
		Date dat_fecha_actual=utilitario.getFecha(srt_fecha_inicio);//fecha q vamos a restar los dias
		for(int i=0;i<=int_num_dias;i++){
			//insertamos en la tabla seleccion
			set_pantalla_dias.getTab_seleccion().insertar();
			set_pantalla_dias.getTab_seleccion().getFilaSeleccionada().setRowKey((i+1)+"");
			//asignamos valores a los capos insertados
			set_pantalla_dias.getTab_seleccion().setValor("ide_empr", i+"");
			//set_pantalla_dias.getTab_seleccion().setValor("dia",utilitario.getFormatoFecha(dat_fecha_actual));
			//FECHA LARGA
			set_pantalla_dias.getTab_seleccion().setValor("dia",utilitario.getFechaLarga(utilitario.getFormatoFecha(dat_fecha_actual)));
			//FECHA
			set_pantalla_dias.getTab_seleccion().setValor("fecha",utilitario.getFormatoFecha(dat_fecha_actual));

			//resto un dia a la fecha
			dat_fecha_actual=utilitario.sumarDiasFecha(dat_fecha_actual,1 );
		}
	}

	public void limpiar(){
		aut_factura.limpiar();
		tab_factura.limpiar();
		tab_detalle_factura.limpiar();
		utilitario.addUpdate("aut_factura");
	}


	//METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_factura.onSelect(evt);
		tab_factura.setCondicion("ide_fadaf="+aut_factura.getValor());
		tab_factura.ejecutarSql();
		tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		//calcularFactura();

	}

	// metodo tieneIvaProducto
	private  boolean tieneIvaProducto(String ide_bodtmat){
		//Declaramos un String con la consulta que vamos a ejecutar
		String str_sql="Select * from bodt_material where ide_bomat="+ide_bodtmat;
		//Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta=utilitario.consultar(str_sql);

		//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
		if ( tab_consulta.isEmpty()==false) {
			//Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_bomat= tab_consulta.getValor("aplica_valor_bomat");
			//Preguntamos si el valor de la variable es true
			if(str_aplica_valor_bomat!=null && str_aplica_valor_bomat.equalsIgnoreCase("true")){
				return true; //Si carga iva
			}
		}
		//System.out.println(tab_consulta.getValor("aplica_valor_bomat"));
		return false;  //retorna false
	}

	//Metodo metodo cuando se seleccione algun producto del autocompletar
	public void seleccionoProducto(SelectEvent evt){
		tab_detalle_factura.modificar(evt); //simepre que se ejecuta un metodoChange
		//Consultamos si el producto seleccionado carga iva
		boolean boo_iva=tieneIvaProducto(tab_detalle_factura.getValor("ide_bomat"));
		String str_seleccionado=tab_detalle_factura.getValor("ide_bomat");
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
			tab_detalle_factura.setValor("valor_fadef",valor);	
			utilitario.addUpdateTabla(tab_detalle_factura, "valor_fadef", "");
		
		}
		else{
			//Mensaje 
			utilitario.agregarMensajeInfo("No existen tarifas para el cliente y el articulo seleccionado", "");
		}

		calcular();

	}

	//total_fadef
	public void calcular(){
		//Variables para almacenar y calcular el total del detalle
		double dou_cantidad_fadef=0;
		double dou_valor_fadef=0;
		double dou_total_fadef=0;

		try {
			//Obtenemos el valor de la cantidad
			dou_cantidad_fadef=Double.parseDouble(tab_detalle_factura.getValor("cantidad_fadef"));
		} catch (Exception e) {
		}

		try {
			//Obtenemos el valor
			dou_valor_fadef=Double.parseDouble(tab_detalle_factura.getValor("valor_fadef"));
		} catch (Exception e) {
		}

		//Calculamos el total
		dou_total_fadef=dou_cantidad_fadef*dou_valor_fadef;

		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_detalle_factura.setValor("total_fadef",utilitario.getFormatoNumero(dou_total_fadef,3));

		//Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_detalle_factura, "total_fadef", "tab_factura");
		calcularFactura();
	}

	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle_factura.modificar(evt); //Siempre es la primera linea
		calcular();
	}

	//Vamos a calcular los totales tanto de iva como de valores de toda la factura

	private void calcularFactura(){
		//Enceramos las variables
		dou_base_no_iva=0;
		dou_base_cero=0;
		dou_base_aprobada=0;
		dou_valor_iva=0;
		dou_total=0;

		//Reccoremos todos los detalles de la factura
		for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
			//Obtenemos si el producto actual tiene iva
			boolean boo_iva=tieneIvaProducto(tab_detalle_factura.getValor(i,"ide_bomat"));
			if(boo_iva){
				//CARGA IVA
				try {
					//Acumulamos la base aprobada
					dou_base_aprobada+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
					//Acumulamos el valor del iva de cada detalle
					dou_valor_iva+=(Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef")) *  dou_por_iva);
				} catch (Exception e) {
				}
			}
			else{
				//NO CARGA IVA
				try {
					//Acumulamos la base no iva
					dou_base_no_iva+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
					//Acumulamos la base cero
					dou_base_cero+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
				} catch (Exception e) {
				}
			}
		}
		dou_total=dou_valor_iva+dou_base_aprobada+dou_base_no_iva;
		tab_factura.setValor("base_no_iva_fafac",utilitario.getFormatoNumero(dou_base_no_iva,3));
		tab_factura.setValor("base_cero_fafac",utilitario.getFormatoNumero(dou_base_cero,3));
		tab_factura.setValor("base_aprobada_fafac",utilitario.getFormatoNumero(dou_base_aprobada,3));
		tab_factura.setValor("valor_iva_fafac",utilitario.getFormatoNumero(dou_valor_iva,3));
		tab_factura.setValor("total_fafac",utilitario.getFormatoNumero(dou_total,3));
		tab_factura.modificar(tab_factura.getFilaActual());//para que haga el update
		eti_total.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total,3));
		utilitario.addUpdate("eti_total");
	}


	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 * metodo booleano para validar el tipo de documento de identidad cedula y ruc
	 */
	public boolean validarDocumentoIdentidad(String ide_gttdi,String documento_identidad){
		if (ide_gttdi!=null && !ide_gttdi.isEmpty()){
			if (documento_identidad!=null && !documento_identidad.isEmpty()){
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
					if (!utilitario.validarCedula(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de cedula ingresado no es valido");
						return false;
					}
				}else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
					if (!utilitario.validarRUC(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de RUC ingresado no es valido");
						return false;
					}
				}
			}
		}
		return true;
	}

	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		
		
		if (aut_factura.getValor()!=null){
			if(tab_factura.isFocus()){
				tab_factura.getColumna("ide_fadaf").setValorDefecto(aut_factura.getValor());
				tab_factura.insertar();
			}
			else if(tab_detalle_factura.isFocus()){
				if (tab_detalle_factura.getValorForanea().toString().equals("-1")){
					utilitario.agregarMensaje("No puede insertar", "Debe guardar registro de la factura");
				}
				else{
				tab_detalle_factura.insertar();
				}
			}
			fechaVencimientoInserta();
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar los datos de Facturación","");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_factura.guardar()){
			if(tab_detalle_factura.guardar()){
				guardarPantalla();
				tab_factura.ejecutarSql();
				tab_detalle_factura.ejecutarSql();
			}
		}
		
	}
	

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		if(tab_detalle_factura.isFocus()){
			calcularFactura();//calcula los totales
			utilitario.addUpdate("tab_factura"); // actualiza la tabla
			if(tab_factura.isFilaModificada()){
				//Para que haga el update
				tab_factura.modificar(tab_factura.getFilaActual());
			}
		}

	}

	//REPORTE
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Factura"));{
			if (tab_factura.getTotalFilas()>0){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();				
			p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_factura.getValor("ide_fafac")));
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			self_reporte.dibujar();
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una Factura");

		}
	}
	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		calcularFactura();
	}
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		calcularFactura();
	}
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		calcularFactura();
	}
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		calcularFactura();
	}


	public Tabla gettab_factura() {
		return tab_factura;
	}

	public void settab_factura(Tabla tab_factura) {
		this.tab_factura = tab_factura;
	}

	public Tabla gettab_detalle_factura() {
		return tab_detalle_factura;
	}

	public void settab_detalle_factura(Tabla tab_detalle_factura) {
		this.tab_detalle_factura = tab_detalle_factura;
	}


	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}


	public SeleccionCalendario getSec_rango_fechas() {
		return sec_rango_fechas;
	}


	public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
		this.sec_rango_fechas = sec_rango_fechas;
	}

	public SeleccionTabla getSet_pantalla_dias() {
		return set_pantalla_dias;
	}

	public void setSet_pantalla_dias(SeleccionTabla set_pantalla_dias) {
		this.set_pantalla_dias = set_pantalla_dias;
	}


	public Dialogo getDia_valor() {
		return dia_valor;
	}


	public void setDia_valor(Dialogo dia_valor) {
		this.dia_valor = dia_valor;
	}



	public SeleccionTabla getSet_insertarbodega() {
		return set_insertarbodega;
	}


	public void setSet_insertarbodega(SeleccionTabla set_insertarbodega) {
		this.set_insertarbodega = set_insertarbodega;
	}


	public SeleccionTabla getSet_pantallacliente() {
		return set_pantallacliente;
	}


	public void setSet_pantallacliente(SeleccionTabla set_pantallacliente) {
		this.set_pantallacliente = set_pantallacliente;
	}


	public SeleccionTabla getSet_actualizar_cliente() {
		return set_actualizar_cliente;
	}


	public void setSet_actualizar_cliente(SeleccionTabla set_actualizar_cliente) {
		this.set_actualizar_cliente = set_actualizar_cliente;
	}


	public Confirmar getCon_guardar_cliente() {
		return con_guardar_cliente;
	}


	public void setCon_guardar_cliente(Confirmar con_guardar_cliente) {
		this.con_guardar_cliente = con_guardar_cliente;
	}


	public Dialogo getCrear_cliente_dialogo() {
		return crear_cliente_dialogo;
	}


	public void setCrear_cliente_dialogo(Dialogo crear_cliente_dialogo) {
		this.crear_cliente_dialogo = crear_cliente_dialogo;
	}
	public Tabla getTab_cliente() {
		return tab_cliente;
	}
	public void setTab_cliente(Tabla tab_cliente) {
		this.tab_cliente = tab_cliente;
	}
	public Tabla getTab_direccion() {
		return tab_direccion;
	}
	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}
	public Map getP_parametros() {
		return p_parametros;
	}
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
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



}
