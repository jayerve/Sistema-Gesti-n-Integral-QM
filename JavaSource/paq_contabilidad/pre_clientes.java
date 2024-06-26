package paq_contabilidad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_comercializacion.ejb.ServicioClientes;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Pantalla;
import pckEntidades.EnvioMail;


public class pre_clientes extends Pantalla {

	private Tabla tab_direccion=new Tabla();
	private Tabla tab_telefono=new Tabla();
	private Tabla tab_email=new Tabla();
	private Tabla tab_documento=new Tabla();
	private Tabla tab_tarifa=new Tabla();
	private Tabla tab_clientes=new Tabla();
	private Tabla tab_contratos = new Tabla();
	private Tabla tab_servicio = new Tabla();
	private Tabla tab_establecimiento = new Tabla();
	private Tabla tab_notificaciones = new Tabla();
	private SeleccionTabla set_pantalla_sucursal= new SeleccionTabla();
	private SeleccionTabla set_actualizar=new SeleccionTabla();
	private Confirmar con_guardar= new Confirmar();
	private SeleccionTabla set_buscar_cliente = new SeleccionTabla();
	
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);

	public pre_clientes (){

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_clientes.setId("tab_clientes");
		tab_clientes.setTipoFormulario(true);  //formulario 
		tab_clientes.getGrid().setColumns(4); //hacer  columnas 
		tab_clientes.setTabla("rec_clientes", "ide_recli",1);
		tab_clientes.getColumna("ide_retic").setCombo("rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_clientes.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_clientes.getColumna("ide_gtgen").setCombo("gth_genero", "ide_gtgen", "detalle_gtgen", "");
		tab_clientes.getColumna("ide_gedip").setCombo(ser_gestion.getSqlDivisionPoliticaCiudadParroquia());
		tab_clientes.getColumna("ide_gedip").setAutoCompletar();
		tab_clientes.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", " detalle_gttdi", "");
		tab_clientes.getColumna("gth_ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_clientes.getColumna("ide_gtesc").setCombo("gth_estado_civil", " ide_gtesc ", "detalle_gtesc", "");
		tab_clientes.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_clientes.getColumna("representante_legal_recli");
		tab_clientes.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_clientes.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_clientes.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_clientes.getColumna("ide_geplf").setCombo("gen_plazo_facturas", "ide_geplf", "dias_geplf", "");
		tab_clientes.getColumna("activo_recli").setValorDefecto("true");
		tab_clientes.getColumna("rec_ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_clientes.getColumna("rec_ide_recli").setAutoCompletar();
		tab_clientes.getColumna("rec_ide_recli").setLectura(true);
		// para contruir los radios
		List lista = new ArrayList();
		Object fila1[] = {
				"1", "MATRIZ"
		};
		Object fila2[] = {
				"0", "SUCURSAL"
		};

		lista.add(fila1);
		lista.add(fila2);
		tab_clientes.getColumna("matriz_sucursal_recli").setRadio(lista, "1");
		tab_clientes.getColumna("matriz_sucursal_recli").setRadioVertical(true);

		//RADIOS 
		// para contruir los radios
		List lista1 = new ArrayList();
		Object fila3[] = {
				"1", "MATRIZ"
		};
		Object fila4[] = {
				"0", "SUCURSAL"
		};

		lista1.add(fila3);
		lista1.add(fila4);
		tab_clientes.getColumna("factura_datos_recli").setRadio(lista1, "2");
		tab_clientes.getColumna("factura_datos_recli").setRadioVertical(true);
		
		tab_clientes.getColumna("ide_regec").setCombo("select ide_regec,nombre_regec from rec_gestor_comercial");
		tab_clientes.getColumna("ide_regec").setAutoCompletar();

		tab_clientes.agregarRelacion(tab_direccion);//agraga relacion para los tabuladores
		tab_clientes.agregarRelacion(tab_telefono);
		tab_clientes.agregarRelacion(tab_email);
		tab_clientes.agregarRelacion(tab_tarifa);
		tab_clientes.agregarRelacion(tab_documento);
		tab_clientes.agregarRelacion(tab_contratos);
		tab_clientes.agregarRelacion(tab_servicio);
		tab_clientes.agregarRelacion(tab_establecimiento);
		// System.out.println("sql pc"+tab_clientes.getSql());

		
		tab_clientes.dibujar();
		PanelTabla pat_clientes=new PanelTabla ();
		pat_clientes.setPanelTabla(tab_clientes);


		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion","ide_recld",2);
		tab_direccion.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_direccion);


		tab_telefono.setId("tab_telefono");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("rec_cliente_telefono","ide_reclt",3);
		tab_telefono.getColumna("ide_reclt").setOrden(1);
		tab_telefono.getColumna("nombre_contacto_reclt").setOrden(2);
		tab_telefono.getColumna("cargo_contacto_reclt").setOrden(3);
		tab_telefono.getColumna("ide_reteo").setOrden(4);
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora", "ide_reteo", "detalle_reteo", "");
		tab_telefono.getColumna("telefono_reclt").setOrden(5);
		tab_telefono.getColumna("notificacion_reclt").setOrden(6);
		tab_telefono.getColumna("activo_reclt").setOrden(7);
		tab_telefono.getColumna("ide_recli").setOrden(8);
		tab_telefono.getColumna("ide_recli").setVisible(false);

		
		tab_telefono.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_telefono);
		
		

		tab_email.setId("tab_email");
		tab_email.setIdCompleto("tab_tabulador:tab_email");
		tab_email.setTabla("rec_cliente_email","ide_recle",4);
		tab_email.dibujar();
		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_email);

		tab_documento.setId("tab_documento");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTipoFormulario(true);
		tab_documento.getGrid().setColumns(4);
		tab_documento.setTabla("rec_cliente_archivo","ide_recla",5);	
		tab_documento.getColumna("foto_recla").setUpload("clientes");
		tab_documento.getColumna("activo_recla").setLectura(true);
		
		tab_documento.dibujar();
		PanelTabla pat_panel5 = new PanelTabla();
		pat_panel5.setPanelTabla(tab_documento);


		tab_tarifa.setId("tab_tarifa");
		tab_tarifa.setIdCompleto("tab_tabulador:tab_tarifa");
		tab_tarifa.setTabla("tes_cliente_tarifa","ide_teclt",6);
		tab_tarifa.getColumna("ide_temat").setCombo("select a.ide_temat,detalle_bomat,detalle_tetar,valor_temat from tes_material_tarifa a,bodt_material b,tes_tarifas c where a.ide_bomat = b.ide_bomat and  a.ide_tetar = c.ide_tetar order by detalle_bomat");
		tab_tarifa.dibujar();
		PanelTabla pat_panel6 = new PanelTabla();
		pat_panel6.setPanelTabla(tab_tarifa);
		
		detalleContrato();
		detalleEstablecimiento();
		
		PanelTabla pat_contrato = new PanelTabla();
		pat_contrato.setPanelTabla(tab_contratos);
		
		PanelTabla pat_establecimiento = new PanelTabla();
		pat_establecimiento.setPanelTabla(tab_establecimiento);
		
		tab_servicio.setId("tab_servicio");
		tab_servicio.setIdCompleto("tab_tabulador:tab_servicio");
		tab_servicio.setTabla("tes_cliente_servicio", "ide_tecls", 8);
		tab_servicio.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_servicio.dibujar();
		PanelTabla pat_servicio = new PanelTabla();
		pat_servicio.setPanelTabla(tab_servicio);
		
		tab_notificaciones.setId("tab_notificaciones");
		tab_notificaciones.setIdCompleto("tab_tabulador:tab_notificaciones");
		tab_notificaciones.setTabla("rec_cliente_notificaciones", "ide_recln", 10);
		tab_notificaciones.setGenerarPrimaria(false);
		tab_notificaciones.getColumna("ide_regec").setCombo("rec_gestor_comercial", "ide_regec", "nombre_regec", "");
		tab_notificaciones.setCondicion("ide_recln=-1");
		tab_notificaciones.setLectura(true);
		tab_notificaciones.dibujar();
		PanelTabla pat_notificaciones = new PanelTabla();
		pat_notificaciones.setPanelTabla(tab_notificaciones);


		tab_tabulador.agregarTab("CONTRATOS", pat_contrato);
		tab_tabulador.agregarTab("DIRECCION", pat_panel2);//intancia los tabuladores
		tab_tabulador.agregarTab("ESTABLECIMIENTOS", pat_establecimiento);
		tab_tabulador.agregarTab("CONTACTOS", pat_panel3);
		tab_tabulador.agregarTab("EMAIL-FACTURACION", pat_panel4);
		tab_tabulador.agregarTab("DOCUMENTO", pat_panel5);
		tab_tabulador.agregarTab("TARIFA", pat_panel6);
		tab_tabulador.agregarTab("SERVICIOS", pat_servicio);
		tab_tabulador.agregarTab("NOTIFICACIONES", pat_notificaciones);

		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes,tab_tabulador,"70%","H");
		agregarComponente(div_division);

		//BOTON AGREGAR CLIENTE
		Boton bot_agregarSucursal=new Boton();
		bot_agregarSucursal.setValue("AGREGAR MATRIZ");
		bot_agregarSucursal.setIcon("ui-icon-person");
		bot_agregarSucursal.setMetodo("agregarMatriz");
		bar_botones.agregarBoton(bot_agregarSucursal);

		//PANTALLA SELECIONAR CLIENTE
		set_pantalla_sucursal.setId("set_pantalla_sucursal");
		set_pantalla_sucursal.setTitle("SELECCIONE MATRIZ");
		set_pantalla_sucursal.getBot_aceptar().setMetodo("aceptarMatriz");
		
		set_pantalla_sucursal.setSeleccionTabla(ser_facturacion.getClientes("1"),"ide_recli");
		System.out.println("Devuelve servicio...."+ser_facturacion.getClientes("1"));
		//set_pantalla_sucursal.setSeleccionTabla("rec_clientes", "ide_recli", "nombre_comercial_recli,ruc_comercial_recli");
		//set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantalla_sucursal.setRadio();
		set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantalla_sucursal);
		
		//////modificar
		Boton bot_modificar=new Boton();
		bot_modificar.setValue("ACTUALIZAR MATRIZ");
		bot_modificar.setIcon("ui-icon-person");
		bot_modificar.setMetodo("actualizarMatriz");
		bar_botones.agregarBoton(bot_modificar);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		
		set_actualizar.setId("set_actualizar");
		set_actualizar.setSeleccionTabla(ser_facturacion.getClientes("1"),"ide_recli");
		//set_actualizar.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_actualizar.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		//set_actualizar.getTab_seleccion().getColumna("codigo").setFiltro(true);
		//set_actualizar.getTab_seleccion().getColumna("nro_contrato_recli").setFiltro(true);
		set_actualizar.setRadio();
		set_actualizar.getBot_aceptar().setMetodo("modificarMatriz");
		agregarComponente(set_actualizar);	
		
		
		Boton bot_buscar_cliente = new Boton();
		bot_buscar_cliente.setIcon("ui-icon-person");
		bot_buscar_cliente.setValue("Buscar Cliente");
		bot_buscar_cliente.setMetodo("buscarCliente");
		bar_botones.agregarBoton(bot_buscar_cliente);
		
		set_buscar_cliente.setId("set_buscar_cliente");
		set_buscar_cliente.setTitle("SELECCIONE EL CLIENTE");
		set_buscar_cliente.setSeleccionTabla(ser_cliente.getClientesAvCorp(), "codigo");
		set_buscar_cliente.getTab_seleccion().getColumna("nro_contrato").setFiltroContenido();
		set_buscar_cliente.getTab_seleccion().getColumna("nro_contrato").setLongitud(20);
		set_buscar_cliente.getTab_seleccion().getColumna("ruc_comercial_factura").setFiltroContenido();
		set_buscar_cliente.getTab_seleccion().getColumna("ruc_comercial_factura").setNombreVisual("RUC");
		set_buscar_cliente.getTab_seleccion().getColumna("ruc_comercial_factura").setLongitud(30);
		set_buscar_cliente.getTab_seleccion().getColumna("nombre_comercial_factura").setFiltroContenido();
		set_buscar_cliente.getTab_seleccion().getColumna("num_generador_desecho").setFiltroContenido();
		set_buscar_cliente.getTab_seleccion().getColumna("establecimiento_operativo").setFiltroContenido();
		set_buscar_cliente.getTab_seleccion().getColumna("email").setFiltroContenido();
		set_buscar_cliente.setRadio();
		set_buscar_cliente.getBot_aceptar().setMetodo("localizaCliente");
		agregarComponente(set_buscar_cliente);
	
		verNotificaciones();
		
		utilitario.agregarMensaje("Mensaje", "Pantalla obsoleta...");

	}
	
	private void detalleContrato()
	{
		tab_contratos.setId("tab_contratos");
		tab_contratos.setIdCompleto("tab_tabulador:tab_contratos");
		tab_contratos.setTabla("pre_contrato", "ide_prcon", 7);
		tab_contratos.getColumna("ide_prcon").setNombreVisual("CÓDIGO");
		tab_contratos.getColumna("ide_prcon").setOrden(1);
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON");
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON").setNombreVisual("Número de Contrato");
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON").setLongitud(10);
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON").setLongitud_control(10);
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON").setOrden(2);
		tab_contratos.getColumna("activo_prcon");
		tab_contratos.getColumna("activo_prcon").setNombreVisual("ACTIVO");
		tab_contratos.getColumna("activo_prcon").setOrden(2);
		tab_contratos.getColumna("activo_prcon").setValorDefecto("true");
		
		/*Object fila1[] = { "0", "Interno" };
		Object fila2[] = { "1", "Externo" };
		Object fila3[] = { "2", "Infima Cuantía" };
		Object fila4[] = { "3", "Civil" };
		Object fila5[] = { "4", "Regimen Especial" };
		Object fila6[] = { "5", "Convenios" };
		
		List lista = new ArrayList();
		//lista.add(fila1);
		//lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);*/
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadio(utilitario.getListaTipoContratos(), "0");
		tab_contratos.getColumna("tipo_int_ext_prcon").setOrden(3);
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadioVertical(true);
		tab_contratos.getColumna("tipo_int_ext_prcon").setNombreVisual("Tipo Contrato");
		tab_contratos.getColumna("ide_prtsc").setCombo("pre_tipo_servicio_contrato", "ide_prtsc", "detalle_prtsc", "");
		tab_contratos.getColumna("ide_prtsc").setNombreVisual("Tipo de Servicio");
		tab_contratos.getColumna("ide_prtsc").setOrden(4);
		/*tab_contratos.getColumna("fecha_firma_prcon");
		tab_contratos.getColumna("fecha_firma_prcon").setNombreVisual("Firma de Contrato");
		tab_contratos.getColumna("fecha_firma_prcon").setOrden(5);
		tab_contratos.getColumna("fecha_firma_prcon").setValorDefecto(utilitario.getFechaActual());*/
		tab_contratos.getColumna("fecha_firma_prcon").setVisible(false);
		tab_contratos.getColumna("fecha_inicio_prcon").setNombreVisual("Fecha de Inicio");
		tab_contratos.getColumna("fecha_inicio_prcon").setOrden(6);
		tab_contratos.getColumna("fecha_inicio_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contratos.getColumna("OBSERVACION_PRCON").setVisible(false);
		tab_contratos.getColumna("OBSERVACION_PRCON").setNombreVisual("Objeto del Contrato");
		tab_contratos.getColumna("OBSERVACION_PRCON").setOrden(7);

		tab_contratos.getColumna("MONTO_PRCON");
		tab_contratos.getColumna("MONTO_PRCON").setNombreVisual("Monto del Contrato");
		tab_contratos.getColumna("MONTO_PRCON").setOrden(10);
		tab_contratos.getColumna("MONTO_PRCON").setVisible(false);
		tab_contratos.getColumna("MONTO_ORIGINAL_PRCON").setVisible(false);
		tab_contratos.getColumna("monto_anticipo_prcon").setVisible(false);
		tab_contratos.getColumna("FECHA_ANTICIPO_PRCON").setVisible(false);
		tab_contratos.getColumna("ide_pretc").setVisible(false);
		tab_contratos.getColumna("IDE_TETAR").setVisible(false);
		tab_contratos.getColumna("CON_IDE_PRCON").setVisible(false);

		tab_contratos.getColumna("ide_recli").setVisible(false);

		tab_contratos.getColumna("IDE_PERDE").setVisible(false);
		
		tab_contratos.getColumna("IDE_PERRE").setVisible(false);

		tab_contratos.getColumna("plazo_prcon").setVisible(false);

		tab_contratos.getColumna("ide_prtip").setVisible(false);

		tab_contratos.getColumna("fecha_fin_prcon");
		tab_contratos.getColumna("fecha_fin_prcon").setNombreVisual("Fecha fín de Contrato");
		tab_contratos.getColumna("fecha_fin_prcon").setOrden(21);
		tab_contratos.getColumna("fecha_cierre_prcon").setVisible(false);

		tab_contratos.getColumna("fecha_suspension_prcon").setVisible(false);
		tab_contratos.getColumna("fecha_suspension_prcon").setNombreVisual("Fec. de Suspención Contrato");
		tab_contratos.getColumna("fecha_suspension_prcon").setOrden(23);
		tab_contratos.getColumna("motivo_suspension_prcon").setVisible(false);
		tab_contratos.getColumna("motivo_suspension_prcon").setNombreVisual("Motivo de Suspención");
		tab_contratos.getColumna("motivo_suspension_prcon").setOrden(24);
		tab_contratos.getColumna("num_generador_desecho_prcon").setVisible(false);
		tab_contratos.getColumna("ide_reclr").setVisible(false);

		tab_contratos.getColumna("estimado_desecho_prcon").setVisible(false);

		tab_contratos.getColumna("ide_coest").setCombo("cont_estado", " ide_coest", "detalle_coest", "");
		tab_contratos.getColumna("ide_coest").setOrden(28);
		tab_contratos.getColumna("ide_coest").setNombreVisual("Estado");

		tab_contratos.getColumna("fecha_terminacion_prcon").setVisible(false);
		tab_contratos.getColumna("nro_contrato_prcon").setVisible(false);
		tab_contratos.getColumna("ide_tepro").setVisible(false);
		tab_contratos.getColumna("ide_prcop").setVisible(false);
		
		tab_contratos.dibujar();
	}
	
	private void detalleEstablecimiento() 
	{
		tab_establecimiento.setId("tab_establecimiento");
		tab_establecimiento.setIdCompleto("tab_tabulador:tab_establecimiento");
		//tab_establecimiento.setTipoFormulario(true);
		tab_establecimiento.getGrid().setColumns(4);
		tab_establecimiento.setTabla("rec_clientes_establecimiento", "ide_reest", 9);
		tab_establecimiento.setCampoForanea("ide_recli");
		tab_establecimiento.getColumna("ide_reest").setNombreVisual("CODIGO");
		tab_establecimiento.getColumna("ide_reest").setVisible(true);
		tab_establecimiento.getColumna("activo_reest").setNombreVisual("ACTIVO");
		tab_establecimiento.getColumna("activo_reest").setValorDefecto("true");
		tab_establecimiento.getColumna("activo_reest").setVisible(true);

		tab_establecimiento.getColumna("establecimiento_operativo_reest").setNombreVisual("ESTABLECIMIENTO OPERATIVO");
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setVisible(true);
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setLongitud(75);
		tab_establecimiento.getColumna("nro_establecimiento_reest").setNombreVisual("Nro. ESTABLECIMIENTO");
		tab_establecimiento.getColumna("nro_establecimiento_reest").setVisible(true);
		tab_establecimiento.getColumna("codigo_zona_reest").setNombreVisual("CÓDIGO DE ZONA");
		tab_establecimiento.getColumna("codigo_zona_reest").setVisible(true);
		tab_establecimiento.getColumna("direccion_reest").setNombreVisual("DIRECCIÓN DE ESTABLECIMIENTO");
		tab_establecimiento.getColumna("direccion_reest").setVisible(true);
		tab_establecimiento.getColumna("direccion_operativa_reest").setNombreVisual("DIRECCIÓN OPERATIVA");
		tab_establecimiento.getColumna("referencia_reest").setNombreVisual("REFERENCIA");
		tab_establecimiento.getColumna("referencia_reest").setVisible(true);
		tab_establecimiento.getColumna("email_reest").setNombreVisual("E-MAIL");
		tab_establecimiento.getColumna("email_reest").setVisible(true);
		tab_establecimiento.getColumna("telefono_reest").setNombreVisual("TELÉFONO");
		tab_establecimiento.getColumna("telefono_reest").setVisible(true);

		tab_establecimiento.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_establecimiento.getColumna("ide_retia").setVisible(true);
		tab_establecimiento.getColumna("ide_retia").setNombreVisual("TIPO DE FRECUENCIA");
		tab_establecimiento.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_establecimiento.getColumna("ide_reclr").setVisible(true);
		tab_establecimiento.getColumna("ide_reclr").setNombreVisual("RUTA");
		
		tab_establecimiento.getColumna("codigo_zona_reest").setVisible(false);
		tab_establecimiento.getColumna("coordx_reest").setVisible(false);
		tab_establecimiento.getColumna("coordy_reest").setVisible(false);
		tab_establecimiento.getColumna("direccion_operativa_reest").setVisible(false);
		tab_establecimiento.getColumna("telefono_reest").setVisible(false);
		//tab_establecimiento.getColumna("email_reest").setVisible(false);

		tab_establecimiento.dibujar();
	}
	
	private void verNotificaciones()
	{
		tab_notificaciones.setCondicion("ruc_comercial_recli like '"+pckUtilidades.CConversion.CStr(tab_clientes.getValor("ruc_comercial_recli"))+"' ");
		//tab_notificaciones.imprimirSql();
		tab_notificaciones.ejecutarSql();
	}
	
	public void buscarCliente() {

		//utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		// Hace aparecer el componente
		//set_buscar_cliente.getTab_seleccion().setSql(ser_facturacion.getClientesActivos("0,1"));
		set_buscar_cliente.getTab_seleccion().ejecutarSql();
		set_buscar_cliente.dibujar();
	}
	
	public void localizaCliente() {

		String str_clienteActualizado = set_buscar_cliente.getValorSeleccionado();
		if (str_clienteActualizado != null) {
			tab_clientes.setFilaActual(str_clienteActualizado);
			utilitario.addUpdate("tab_clientes");
			set_buscar_cliente.cerrar();
		}
	}
	
	public void agregarMatriz(){
		//Hace aparecer el componente
		if(tab_clientes.getValor("matriz_sucursal_recli")!=null && tab_clientes.getValor("matriz_sucursal_recli").equals("0")){
		set_pantalla_sucursal.getTab_seleccion().setSql(ser_facturacion.getClientes("1"));
		set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		//set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_pantalla_sucursal.setRadio();
		set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
		set_pantalla_sucursal.dibujar();	
		}else{
			utilitario.agregarMensaje("No se puede registrar matriz a una matriz","");
		}
	}

	public void aceptarMatriz(){
		String str_seleccionado=set_pantalla_sucursal.getValorSeleccionado();
		if(str_seleccionado!=null){
			//Inserto la sucursal en la tabla  
			if(tab_clientes.isFilaInsertada()==false){
				//Controla que si ya esta insertada no vuelva a insertar
				tab_clientes.insertar();	
			}
			
			tab_clientes.setValor("rec_ide_recli", str_seleccionado);				

			set_pantalla_sucursal.cerrar();
			utilitario.addUpdate("tab_clientes");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}


	public Tabla getTab_tarifa() {
		return tab_tarifa;
	}

	


	public void setTab_tarifa(Tabla tab_tarifa) {
		this.tab_tarifa = tab_tarifa;
	}

///////actualizar matriz
	public void actualizarMatriz(){
		//Hace aparecer el componente
		if(tab_clientes.getValor("matriz_sucursal_recli")!=null && tab_clientes.getValor("matriz_sucursal_recli").equals("0")){
		set_actualizar.getTab_seleccion().setSql(ser_facturacion.getClientes("1"));
		set_actualizar.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		//set_actualizar.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_actualizar.setRadio();
		set_actualizar.getTab_seleccion().ejecutarSql();
		set_actualizar.dibujar();	
		}else{
			utilitario.agregarMensaje("No se puede registrar matriz a una matriz","");
		}
		}
	
		
	
	public void modificarMatriz(){
		String str_matriz=set_actualizar.getValorSeleccionado();
	    tab_clientes.setValor("rec_ide_recli",(str_matriz));			
	    tab_clientes.modificar(tab_clientes.getFilaActual());
		utilitario.addUpdate("tab_cliente");	

		con_guardar.setMessage("Esta Seguro de Actualizar la matriz");
		con_guardar.setTitle("CONFIRMCION DE ACTUALIZAR");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzar");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");


	}
	
	public void guardarActualilzar(){
		System.out.println("Entra a guardar...");
		tab_clientes.guardar();
		con_guardar.cerrar();
		set_actualizar.cerrar();
		guardarPantalla();

	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		verNotificaciones();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		verNotificaciones();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		verNotificaciones();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		verNotificaciones();
	}
	
	@Override
	public void actualizar(){
		super.actualizar();
		verNotificaciones();
	}
	
	@Override
	public void aceptarBuscar(){
		super.aceptarBuscar();
		verNotificaciones();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_telefono.getTotalFilas()<=0) {
			utilitario.agregarMensajeInfo("Pestaña Teléfono inválida", "Recuerde debe ingresar todos los campos en esta pestaña");
			return;
			}
		validarIngresoContacto();
		if (tab_clientes.guardar()) {
			tab_contratos.guardar();
			tab_establecimiento.guardar();
			tab_servicio.guardar();
			if (tab_direccion.guardar()) {
				if (tab_telefono.guardar()) {
					if( tab_email.guardar()){
						if(tab_tarifa.guardar()){
							tab_documento.guardar();  
							//enviar correo de modificacion a ecuambiente
							//enviarMailActividad();
						}  
					}  

				}
			}
		}
		guardarPantalla();
		verNotificaciones();
	}
	public void validaDocumento(AjaxBehaviorEvent evt){
		tab_clientes.modificar(evt);
		if(!validarDocumentoIdentidad(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"))){
			System.out.println("entre a validar cedula");
			tab_clientes.setValor("ruc_comercial_recli","");
			utilitario.addUpdate("tab_clientes");
		}			

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


	public void enviarMailActividad(){
		
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=10"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		String str_asunto="NOTIFICACION AUTOMATICA EMGIRS-ERP";
		String str_mail="desechos.sanitarios@ecuambiente.com"; //desechos.sanitarios@ecuambiente.com
		String str_mensaje="<p>El cliente: $1 con RUC: $2 ha sido modificado en el sistema ERP.</p>";
		
		str_mensaje=str_mensaje.replace("$1", pckUtilidades.CConversion.CStr(tab_clientes.getValor("razon_social_recli")));
		str_mensaje=str_mensaje.replace("$2", pckUtilidades.CConversion.CStr(tab_clientes.getValor("ruc_comercial_recli")));
		
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);		
		
		envMail.setAsunto(str_asunto);
		envMail.setCuerpoHtml(str_mensaje);
		envMail.setPara(str_mail);
		//envMail.setCopia(envMail.getCorreoEnvio());
		envMail.setCopia("alex.becerra@emgirs.gob.ec");
		
		try {
			
			pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
			if(obj.getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a email: " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo no enviado a email: " + str_mail , " msjError: " + obj.getDescripcion());
		
		}catch (Exception e) {
			System.out.println("Error en el envío de correo"+e.getMessage());
		}
			
		
	}


	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}



	public Tabla getTab_clientes() {
		return tab_clientes;
	}



	public void setTab_clientes(Tabla tab_clientes) {
		this.tab_clientes = tab_clientes;
	}



	public Tabla getTab_direccion() {
		return tab_direccion;
	}



	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}



	public Tabla getTab_telefono() {
		return tab_telefono;
	}



	public void setTab_telefono(Tabla tab_telefono) {
		this.tab_telefono = tab_telefono;
	}



	public Tabla getTab_email() {
		return tab_email;
	}



	public void setTab_email(Tabla tab_email) {
		this.tab_email = tab_email;
	}

	public Tabla getTab_documento() {
		return tab_documento;
	}



	public Tabla getTab_establecimiento() {
		return tab_establecimiento;
	}

	public void setTab_establecimiento(Tabla tab_establecimiento) {
		this.tab_establecimiento = tab_establecimiento;
	}

	public Tabla getTab_servicio() {
		return tab_servicio;
	}

	public void setTab_servicio(Tabla tab_servicio) {
		this.tab_servicio = tab_servicio;
	}

	public Tabla getTab_contratos() {
		return tab_contratos;
	}

	public void setTab_contratos(Tabla tab_contratos) {
		this.tab_contratos = tab_contratos;
	}

	public void setTab_documento(Tabla tab_documento) {
		this.tab_documento = tab_documento;
	}

	public SeleccionTabla getSet_pantalla_sucursal() {
		return set_pantalla_sucursal;
	}

	public void setSet_pantalla_sucursal(SeleccionTabla set_pantalla_sucursal) {
		this.set_pantalla_sucursal = set_pantalla_sucursal;
	}

	public SeleccionTabla getSet_actualizar() {
		return set_actualizar;
	}

	public void setSet_actualizar(SeleccionTabla set_actualizar) {
		this.set_actualizar = set_actualizar;
	}

	
	public SeleccionTabla getSet_buscar_cliente() {
		return set_buscar_cliente;
	}

	public void setSet_buscar_cliente(SeleccionTabla set_buscar_cliente) {
		this.set_buscar_cliente = set_buscar_cliente;
	}
	
	public Tabla getTab_notificaciones() {
		return tab_notificaciones;
	}

	public void setTab_notificaciones(Tabla tab_notificaciones) {
		this.tab_notificaciones = tab_notificaciones;
	}

	public void validarIngresoContacto(){

			if (tab_telefono.getValor("nombre_contacto_reclt")==null || tab_telefono.getValor("nombre_contacto_reclt").equals("") || tab_telefono.getValor("nombre_contacto_reclt").isEmpty() ) {
				utilitario.agregarMensajeInfo("Campo Nonmbre vacio", "Debe ingresar datos en este campo");
				return;
			}
			
			if (tab_telefono.getValor("cargo_contacto_reclt")==null || tab_telefono.getValor("cargo_contacto_reclt").equals("") || tab_telefono.getValor("cargo_contacto_reclt").isEmpty() ) {
				utilitario.agregarMensajeInfo("Campo Cargo vacio", "Debe ingresar datos en este campo");
				return;
			}
			
			if (tab_telefono.getValor("ide_reteo")==null || tab_telefono.getValor("ide_reteo").equals("") || tab_telefono.getValor("ide_reteo").isEmpty() ) {
				utilitario.agregarMensajeInfo("Campo Operadora vacio", "Debe ingresar datos en este campo");
				return;
			}
			
			if (tab_telefono.getValor("telefono_reclt")==null || tab_telefono.getValor("telefono_reclt").equals("") || tab_telefono.getValor("telefono_reclt").isEmpty() ) {
				utilitario.agregarMensajeInfo("Campo Teléfono vacio", "Debe ingresar datos en este campo");
				return;
			}
			
			if (tab_telefono.getValor("telefono_reclt")==null || tab_telefono.getValor("telefono_reclt").equals("") || tab_telefono.getValor("telefono_reclt").isEmpty() ) {
				utilitario.agregarMensajeInfo("Campo Teléfono vacio", "Debe ingresar datos en este campo");
				return;
			}
		
			
		
		
	}

}

