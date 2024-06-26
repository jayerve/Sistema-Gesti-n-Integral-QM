﻿/**
 * 
 */
package paq_activos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.OutputLink;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_activos.ejb.ServicioActivos;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;

/**
 * Pantalla que permite la generaci&oacute;n de actas de
 * entrega/recepci&oacute;n
 * 
 * @author ccaceres
 * @since 2017/05/16
 * 
 */
public class pre_devolucion_herramientas extends Pantalla {

	private Tabla tab_cabecera = new Tabla();
	private Tabla tab_detalle_custodio = new Tabla();
	private Tabla tab_detalle_activo = new Tabla();
	private Tabla tab_cabecera_prestamo= new Tabla();

	private SeleccionTabla set_empleado = new SeleccionTabla();
	private SeleccionTabla set_activos = new SeleccionTabla();
	
	private  SeleccionTabla set_prestamo= new SeleccionTabla();
	private  SeleccionTabla set_items_actas= new SeleccionTabla();

	// SELECTOR PARA BUSCAR POR SOLICITANTE
	private SeleccionTabla set_empleadoSolicitante = new SeleccionTabla();
	private  SeleccionTabla set_items_actas_solicitante= new SeleccionTabla();

	private Combo com_anio = new Combo();



	private String parametroSecuencial = "p_sec_acta_devolucion";
	private String tipoDeActa = "5";
	
	private String tipoDeActaPrestamo = "4";
	
	private Integer nroActa=0;

	public static String par_secuencial_acta;
	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

	/**
	 * prepara la tabla que muestra la cabecera del acta
	 */
	private void preparaCabecera() {
		tab_cabecera = new Tabla();
		tab_cabecera.setId("tab_cabecera");
		tab_cabecera.setTabla("afi_docu", "ide_afdoc", 1);
		tab_cabecera.setCampoOrden("ide_afdoc desc");
		tab_cabecera.getColumna("ide_aftidoc").setCombo("SELECT ide_aftidoc, detalle_aftidoc  FROM afi_tipo_docu");
		tab_cabecera.getColumna("ide_aftidoc").setAutoCompletar();
		tab_cabecera.getColumna("ide_aftidoc").setValorDefecto(tipoDeActa);
		tab_cabecera.getColumna("ide_aftidoc").setLectura(true);
		tab_cabecera.getColumna("ide_geani").setVisible(false);
		tab_cabecera.getColumna("nro_secuencial_afdoc").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_acta));
		// tab_cabecera.getColumna("nro_secuencial_afdoc").setLectura(true);
		tab_cabecera.getColumna("fecha_afdoc").setValorDefecto(utilitario.getFechaActual());
		tab_cabecera.getColumna("activo_afdoc").setValorDefecto("true");
		tab_cabecera.getColumna("activo_afdoc").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
		tab_cabecera.getColumna("ide_gtemp").setLectura(true);
		tab_cabecera.getColumna("ide_gtemp").setAutoCompletar();
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setLectura(true);
		tab_cabecera.getColumna("IDE_GTEMP_AUT").setAutoCompletar();
		
		
		
		tab_cabecera.getColumna("foto_firma").setUpload("fotos");
		//tab_cabecera.getColumna("foto_firma").setValorDefecto("/imagenes/im_empleado.png");
		tab_cabecera.getColumna("foto_firma").setImagen("128", "128");
		tab_cabecera.getColumna("foto_firma").setNombreVisual("FIRMA DEL SOLICITANTE");
		
		
		tab_cabecera.getColumna("activo_afdoc").setValorDefecto("true");
		tab_cabecera.setCondicion("ide_geani=-1 and ide_aftidoc='" + tipoDeActa + "'");
		tab_cabecera.setTipoFormulario(true);
		tab_cabecera.getGrid().setColumns(6);
		tab_cabecera.dibujar();
	}
	
	/**
	 * prepara la tabla que muestra el detalle de custodios del acta
	 */
	private void preparaDetalleCustodio() {
		tab_detalle_custodio.setId("tab_detalle_custodio");
		tab_detalle_custodio.setTabla("afi_doc_detalle_custodio", "ide_afddc", 2);

		tab_detalle_custodio.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true"));
		tab_detalle_custodio.getColumna("ide_gtemp").setLectura(true);
		tab_detalle_custodio.getColumna("ide_gtemp").setAutoCompletar();
		tab_detalle_custodio.getColumna("activo_afdda").setValorDefecto("true");
		tab_detalle_custodio.getColumna("activo_afdda").setVisible(false);
		tab_detalle_custodio.getColumna("recibe_afddc").setValorDefecto("false");
		tab_detalle_custodio.getColumna("recibe_afddc").setVisible(false);
		tab_detalle_custodio.getColumna("ide_afdoc").setVisible(true);
		tab_detalle_custodio.setCondicion("ide_afdoc=-1");
		tab_detalle_custodio.setTipoFormulario(false);
		tab_detalle_custodio.dibujar();
	}

	/**
	 * prepara la tabla que muestra el detalle de activos del acta
	 */
	private void preparaDetalleActivo() {
		tab_detalle_activo.setId("tab_detalle_activo");
		tab_detalle_activo.setTabla("afi_doc_detalle_activo", "ide_afdda", 3);
		/*tab_detalle_activo.getColumna("ide_afact").setCombo(ser_activos.getDescripcionActivo());
		tab_detalle_activo.getColumna("ide_afact").setLectura(true);
		tab_detalle_activo.getColumna("ide_afact").setAutoCompletar();*/
		tab_detalle_activo.getColumna("ide_afher").setCombo(ser_activos.getDescripcionHerramienta());
		tab_detalle_activo.getColumna("ide_afher").setLectura(true);
		tab_detalle_activo.getColumna("ide_afher").setAutoCompletar();
		tab_detalle_activo.getColumna("activo_afdda").setValorDefecto("true");
		tab_detalle_activo.getColumna("activo_afdda").setVisible(false);
		tab_detalle_activo.getColumna("ide_afdoc").setVisible(false);
		tab_detalle_activo.setCondicion("ide_afdoc=-1");
		tab_detalle_activo.setTipoFormulario(false);
		tab_detalle_activo.dibujar();
	}

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de custodios del
	 * acta
	 */
	private void prepararSelectorCustodio() {
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadosActivos("true"), "ide_gtemp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("primer_nombre_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("primer_nombre_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("segundo_nombre_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("segundo_nombre_gtemp").setLongitud(15);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleado.setTitle("Seleccione uno o varios Empleados");
		set_empleado.getBot_aceptar().setMetodo("aceptarCustodios");
		agregarComponente(set_empleado);
	}

	/**
	 * prepara la tabla que se muestra en el popup de seleccion de activos del
	 * acta
	 */
	private void prepararSelectorActivo() {
		set_activos.setId("set_activos");
		set_activos.setSeleccionTabla(ser_activos.getHerramientasPrestadas(), "ide_afact");
		set_activos.getTab_seleccion().getColumna("identificador_activo").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("identificador_activo").setFiltroContenido();
		set_activos.getTab_seleccion().getColumna("marca_afact").setFiltro(true);
		set_activos.getTab_seleccion().getColumna("serie_afact").setFiltro(true);
		set_activos.setTitle("Seleccione uno o varios Activos");
		set_activos.getBot_aceptar().setMetodo("aceptarActivos");
		agregarComponente(set_activos);
	}
	
	
	private void prepararSelectorActivoPrestamo() {
		set_prestamo.setId("set_prestamo");
		set_prestamo.setTitle("SELECCIONE UNA ACTA");
		set_prestamo.setSeleccionTabla("Select ide_afdoc, nro_secuencial_afdoc,detalle_afdoc,apellido_paterno_gtemp,primer_nombre_gtemp from afi_docu,gth_empleado  where afi_docu.ide_gtemp_aut=gth_empleado.ide_gtemp and ide_aftidoc=4", "ide_afdoc");
		set_prestamo.getTab_seleccion().getColumna("nro_secuencial_afdoc").setFiltro(true);
		set_prestamo.getTab_seleccion().getColumna("nro_secuencial_afdoc").setNombreVisual("Nro. Acta");
		set_prestamo.getTab_seleccion().getColumna("detalle_afdoc");
		set_prestamo.getTab_seleccion().getColumna("detalle_afdoc").setNombreVisual("Observación Acta");
		set_prestamo.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_prestamo.getTab_seleccion().getColumna("apellido_paterno_gtemp").setNombreVisual("Apellido");
		set_prestamo.getTab_seleccion().getColumna("primer_nombre_gtemp").setFiltro(true);
		set_prestamo.getTab_seleccion().getColumna("primer_nombre_gtemp").setNombreVisual("Nombre");
		set_prestamo.getTab_seleccion().getColumna("ide_afdoc");
		set_prestamo.getBot_aceptar().setMetodo("aceptarActaSeleccionada");

		agregarComponente(set_prestamo);
	}
	
	// PINTAR USUARIOS SOLICITANTES
	private void prepararSelectorSolicitante() {
		set_empleadoSolicitante.setId("set_empleadoSolicitante");
		set_empleadoSolicitante.setTitle("SELECCIONE UN SOLICITANTE");
		set_empleadoSolicitante.setSeleccionTabla(ser_nomina.servicioEmpleadosActivos("true"), "ide_gtemp");
		set_empleadoSolicitante.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleadoSolicitante.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("apellido_paterno_gtemp").setLongitud(15);
		set_empleadoSolicitante.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("apellido_materno_gtemp").setLongitud(15);
		set_empleadoSolicitante.getTab_seleccion().getColumna("primer_nombre_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("primer_nombre_gtemp").setLongitud(15);
		set_empleadoSolicitante.getTab_seleccion().getColumna("segundo_nombre_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("segundo_nombre_gtemp").setLongitud(15);
		set_empleadoSolicitante.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleadoSolicitante.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(15);
		set_empleadoSolicitante.setTitle("Seleccione un empleado");
		set_empleadoSolicitante.getBot_aceptar().setMetodo("aceptarSolicitanteSeleccionada");
		agregarComponente(set_empleadoSolicitante);
	}
	
	
	
	public void seleccionarActasPrestamo() {
				
		System.out.println("Entra para seleccionar las actas de prestamo");
		if (verificacionEdita()) {
			set_prestamo.dibujar();
		}
	}
	
	
	public void seleccionarSolicitante() {
		
		System.out.println("Entra para seleccionar el solicitante");
		if (verificacionEdita()) {
			set_empleadoSolicitante.dibujar();
		}
	}
	
	public void abrirFirmador() throws IOException {
		if (verificacionEdita()) {
			Runtime.getRuntime().exec("mspaint.exe");
		}
	}
	
	
	public void aceptarActaSeleccionada(){
		
			String str_seleccionado = set_prestamo.getSeleccionados();
			String[] arrray = str_seleccionado.split(",");
			
			System.out.println("Acta seleccionada "+str_seleccionado );
				nroActa=Integer.parseInt(str_seleccionado);
		
				set_prestamo.cerrar();
				
				set_items_actas.setId("set_items_actas");
				set_items_actas.setTitle("SELECCIONE LAS HERRAMIENTAS DE LA ACTA SELECCIONADA");
				//set_items_actas.setSeleccionTabla(ser_activos.obtenerActivosPorNumeroDeActaTipoPrestamo(str_seleccionado),"ide_afact");
				set_items_actas.setSeleccionTabla(ser_activos.obtenerHerramientasPorNumeroDeActaTipoPrestamo(str_seleccionado),"ide_afher");
				set_items_actas.getTab_seleccion().getColumna("identificador_acta_detalle").setFiltro(true);
				set_items_actas.getTab_seleccion().getColumna("identificador_activo");
				set_items_actas.getTab_seleccion().getColumna("identificador_activo").setFiltro(true);
				set_items_actas.getTab_seleccion().getColumna("identificador_activo").setLongitud(10);
				//agregarComponente(set_items_actas);
				set_items_actas.dibujar();
				set_items_actas.redibujar();
		
				//set_items_actas.cerrar();
				
	}
	
	
	public void aceptarSolicitanteSeleccionada(){
		String idSolicitante = set_empleadoSolicitante.getSeleccionados();
		String[] arrray = idSolicitante.split(",");
		
		System.out.println("Usuario solicitado"+idSolicitante );
		

		set_empleadoSolicitante.cerrar();
		
		set_items_actas_solicitante.setId("set_items_actas_solicitante");
		set_items_actas_solicitante.setTitle("SELECCIONE LAS HERRAMIENTAS DEL USUARIO SOLICITANTE");
		///set_items_actas_solicitante.setSeleccionTabla(ser_activos.obtenerActivosPorIdSolicitante(idSolicitante),"ide_afact");
		set_items_actas_solicitante.setSeleccionTabla(ser_activos.obtenerHerramientasPorIdSolicitante(idSolicitante),"ide_afact");
		set_items_actas_solicitante.getTab_seleccion().getColumna("identificador_activo");
		System.out.println("Pinta las columnas");	
		set_items_actas_solicitante.getTab_seleccion().getColumna("identificador_activo").setFiltro(true);
		System.out.println("Nombre ");
		set_items_actas_solicitante.getTab_seleccion().getColumna("identificador_activo").setLongitud(10);
		//agregarComponente(set_items_actas);
		set_items_actas_solicitante.dibujar();
		System.out.println("Pinta las columnas dibuja");
		set_items_actas_solicitante.redibujar();
		////cargarCustodioDevolucion(set_items_actas_solicitante.getListaSeleccionados().get(0).getCampos());

		//set_items_actas.cerrar();
		
	}
	
	private void prepararSelectorDetalleActa() {
		set_items_actas.setId("set_items_actas");
		set_items_actas.getBot_aceptar().setMetodo("aceptarItemsDeActa");
		agregarComponente(set_items_actas);	
	}
	
	private void prepararSelectorItemsPorSolicitante() {
		set_items_actas_solicitante.setId("set_items_actas_solicitante");
		set_items_actas_solicitante.getBot_aceptar().setMetodo("aceptarItemsDeActaSolicitante");
		agregarComponente(set_items_actas_solicitante);	
	}
	
	public  void aceptarItemsDeActa(){
		
		System.out.println(" entran a los items");
	String str_seleccionado = set_items_actas.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		
		System.out.println("Items seleccionados"+str_seleccionado );
		
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_activo.insertar();
					///tab_detalle_activo.setValor("ide_afact", str.trim());
					tab_detalle_activo.setValor("ide_afher", str.trim());
					TablaGenerica tabCampos = ser_activos.getCampo(str.trim());
					tab_detalle_activo.setValor("ide_afubi", tabCampos.getValor("ide_afubi"));
					tab_detalle_activo.setValor("afi_ubicacion_otro", tabCampos.getValor("afi_ubicacion_otro"));
				}
			}
		}
		
		set_items_actas.cerrar();
		utilitario.addUpdate("set_items_actas");
		System.out.println("id ACTA.."+nroActa);
		TablaGenerica tab_custodio=utilitario.consultar("select ide_afdoc,ide_gtemp_aut from afi_docu where ide_afdoc="+nroActa);
		System.out.println("CUSTODIO---: "+tab_custodio.getValor("ide_gtemp_aut"));
		tab_detalle_custodio.insertar();
		tab_detalle_custodio.setValor("ide_gtemp", tab_custodio.getValor("ide_gtemp_aut"));
	}
	
	public  void aceptarItemsDeActaSolicitante(){
		
		System.out.println(" entran a los items");
	String str_seleccionado = set_items_actas_solicitante.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		
		System.out.println("Items seleccionados"+str_seleccionado );
		
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_activo.insertar();
					//tab_detalle_activo.setValor("ide_afact", str.trim());
					tab_detalle_activo.setValor("ide_afher", str.trim());
					TablaGenerica tabCampos = ser_activos.getCampo(str.trim());
					tab_detalle_activo.setValor("ide_afubi", tabCampos.getValor("ide_afubi"));
					tab_detalle_activo.setValor("afi_ubicacion_otro", tabCampos.getValor("afi_ubicacion_otro"));
				}
			}
		}
		
		set_items_actas_solicitante.cerrar();
		utilitario.addUpdate("set_items_actas_solicitante");
	}
	

	/**
	 * Funcion que permite la filtracion de las actas correspondientes a cada
	 * año
	 */
	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_cabecera.setCondicion("ide_geani=" + com_anio.getValue() + " and ide_aftidoc='" + tipoDeActa + "'");
			tab_cabecera.ejecutarSql();
			seleccionarDetalles();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}

	/**
	 * Funcion que permite verificar si se esta trabajando con un documento
	 * guardado o no... para poder asegurar que no se pueda editar
	 * 
	 * @return
	 */
	public boolean verificacionEdita() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return false;
		}
		if (tab_cabecera.getValorSeleccionado() == null) {
			tab_cabecera.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_cabecera.setValor("ide_gtemp", ide_gtempxx);
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.setValor("ide_geani", com_anio.getValue() + "");
		}
		if (tab_cabecera.getValorSeleccionado().equals("-1")) {
			if (tab_cabecera.getValor("fecha_afdoc") == null || tab_cabecera.getValor("fecha_afdoc").isEmpty()) {
				utilitario.agregarMensajeInfo("Requisito Ingreso", "Ingrese la Fecha del Acta");
				return false;
			}
			return true;
		} else {
			utilitario.agregarMensaje("Prohibición", "La presente pantalla no permite la modificación de los registros de las actas de Entrega/Recepción.");
			return false;
		}
	}

	/**
	 * Muestra el popup con la seleccion de custodios actualizando el query
	 */
	public void seleccionarCustodios() {
		if (verificacionEdita()) {
			set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadosActivos("true"));
			set_empleado.getTab_seleccion().ejecutarSql();
			set_empleado.dibujar();
		}
	}
	/**
	 * Muestra el popup con la seleccion de custodios actualizando el query
	 */
	public void cargarCustodioDevolucion() {
			set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoCustodio("true"));
			set_empleado.getTab_seleccion().ejecutarSql();
			set_empleado.dibujar();
	}

	/**
	 * Muestra el popup con la seleccion de activos actualizando el query
	 */
	public void seleccionarActivos() {
		if (verificacionEdita()) {
			/////set_activos.getTab_seleccion().setSql(ser_activos.getHerramientasPrestadas());
			set_activos.getTab_seleccion().setSql(ser_activos.getHerramientasNuevaTablaPrestadas());
			set_activos.getTab_seleccion().ejecutarSql();
			set_activos.dibujar();
		}
	}
	
	


	/**
	 * Prepara los botones y reportes que se van a mostrar en pantalla
	 */
	private void preparaBotones() {
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);

		// boton empleado
		/***Boton bot_empleado = new Boton();
		bot_empleado.setIcon("ui-user");
		bot_empleado.setValue("Agregar Custodios");
		bot_empleado.setMetodo("seleccionarCustodios");
		bar_botones.agregarBoton(bot_empleado);*/

		/* boton activo
		Boton bot_sctivo = new Boton();
		bot_sctivo.setIcon("ui-user");
		bot_sctivo.setValue("Agregar Herramientas");
		bot_sctivo.setMetodo("seleccionarActivos");
		bar_botones.agregarBoton(bot_sctivo);*/
		
		
		
		// boton prestamo
		Boton bot_prestamos = new Boton();
		bot_prestamos.setIcon("ui-user");
		bot_prestamos.setValue("Buscar acta de préstamo");
		bot_prestamos.setMetodo("seleccionarActasPrestamo");
		bar_botones.agregarBoton(bot_prestamos);
		
		
		// boton para buscar items del usuario solicitante
		/***Boton bot_bucar_solicitante = new Boton();
		bot_bucar_solicitante.setIcon("ui-user");
		bot_bucar_solicitante.setValue("Buscar items por  Solicitante");
		bot_bucar_solicitante.setMetodo("seleccionarSolicitante");
		bar_botones.agregarBoton(bot_bucar_solicitante);**/
		
		OutputLink bot_portal_remates=new OutputLink();
		bot_portal_remates.setId("bot_portal_remates");
		bot_portal_remates.setTarget("_blank");
		bot_portal_remates.setValue("http://administrativo.emgirs.gob.ec:83/");
		bot_portal_remates.setStyle("display: inline-block; margin: 0 0 -8px 0;");
		bot_portal_remates.getChildren().add(new Etiqueta("Firmar Acta"));
		
		bar_botones.agregarComponente(bot_portal_remates);

		
	}

	/**
	 * Constructor
	 */
	public pre_devolucion_herramientas() {
		par_secuencial_acta = utilitario.getVariable(parametroSecuencial);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		preparaCabecera();
		
	
		preparaDetalleCustodio();
		preparaDetalleActivo();
		prepararSelectorCustodio();
		prepararSelectorActivo();
		
		// Buscar por acta ITEMS prestados
		prepararSelectorActivoPrestamo();
		prepararSelectorDetalleActa();
		
		
		// Buscar por solicitante ITEMS prestados
		prepararSelectorSolicitante();
		prepararSelectorItemsPorSolicitante();
		
		
		
		preparaBotones();

		tab_cabecera.agregarRelacion(tab_detalle_custodio);
		tab_cabecera.agregarRelacion(tab_detalle_activo);

		PanelTabla panelCabecera = new PanelTabla();
		panelCabecera.setPanelTabla(tab_cabecera);

		PanelTabla panelDetalleCustodio = new PanelTabla();
		panelDetalleCustodio.setPanelTabla(tab_detalle_custodio);
		panelDetalleCustodio.setTitle("En esta sección se presentan el o los custodios a los cuales se les va a entregar los activos listados.");
		panelDetalleCustodio.setMensajeInfo("Custodios");

		PanelTabla panelDetalleActivo = new PanelTabla();
		panelDetalleActivo.setPanelTabla(tab_detalle_activo);
		panelDetalleActivo.setTitle("En esta sección se presentan el o los bienes que se van a hacer entrega al o los custodios listados.");
		panelDetalleActivo.setMensajeInfo("Herramientas");

		Division div_cabeceraDetalle = new Division();
		Division div_detalles = new Division();
		div_detalles.dividir2(panelDetalleActivo, panelDetalleCustodio, "60%", "v");
		div_cabeceraDetalle.dividir2(panelCabecera, div_detalles, "35%", "h");
		agregarComponente(div_cabeceraDetalle);

	}

	/**
	 * Verifica si el valor se encuentra en el array
	 * 
	 * @param array
	 * @param valor
	 * @return
	 */
	private boolean enSeleccionado(String[] array, String valor) {
		for (String str : array) {
			if (str.trim().equals(valor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Permite sacar un valor encontrado dentro del array
	 * 
	 * @param array
	 * @param valor
	 * @return
	 */
	private String[] quitarDeArray(String[] array, String valor) {
		List<String> result = new ArrayList<String>();
		for (String item : array) {
			if (!valor.equals(item.trim())) {
				result.add(item.trim());
			}
		}
		return result.toString().replace("[", "").replace("]", "").split(",");
	}

	/**
	 * Permite actualizar los detalles cuando se navega
	 */
	public void seleccionarDetalles() {
		if (tab_cabecera.getValor("ide_afdoc") != null) {
			tab_detalle_custodio.setCondicion("ide_afdoc =" + tab_cabecera.getValor("ide_afdoc"));
			tab_detalle_custodio.ejecutarSql();
			tab_detalle_activo.setCondicion("ide_afdoc =" + tab_cabecera.getValor("ide_afdoc"));
			tab_detalle_activo.ejecutarSql();
		}
	}

	/**
	 * Procesa la selecci&oacute;n en el popup de custodios
	 */
	public void aceptarCustodios() {
		String str_seleccionado = set_empleado.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		if (arrray.length > 0) {
			int numFil = tab_detalle_custodio.getTotalFilas();
			for (int i = 0; i < numFil; i++) {
				tab_detalle_custodio.setFilaActual(i);
				if (enSeleccionado(arrray, tab_detalle_custodio.getValor("ide_gtemp"))) {
					arrray = quitarDeArray(arrray, tab_detalle_custodio.getValor("ide_gtemp"));
				}
			}

		}
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_custodio.insertar();
					tab_detalle_custodio.setValor("ide_gtemp", str.trim());
				}
			}
		}
		set_empleado.cerrar();
	}

	/**
	 * Procesa la selecci&oacute;n en el popup de activos
	 */
	public void aceptarActivos() {
		String str_seleccionado = set_activos.getSeleccionados();
		String[] arrray = str_seleccionado.split(",");
		if (arrray.length > 0) {
			int numFil = tab_detalle_activo.getTotalFilas();
			for (int i = 0; i < numFil; i++) {
				tab_detalle_activo.setFilaActual(i);
				/*if (enSeleccionado(arrray, tab_detalle_activo.getValor("ide_afact"))) {
					arrray = quitarDeArray(arrray, tab_detalle_activo.getValor("ide_afact"));
				}*/
				if (enSeleccionado(arrray, tab_detalle_activo.getValor("ide_afher"))) {
					arrray = quitarDeArray(arrray, tab_detalle_activo.getValor("ide_afher"));
				}
			}

		}
		if (arrray.length > 0) {
			for (String str : arrray) {
				if (!str.trim().equals("")) {
					tab_detalle_activo.insertar();
					///tab_detalle_activo.setValor("ide_afact", str.trim());
					tab_detalle_activo.setValor("ide_afher", str.trim());
				}
			}
		}
		
		
		set_activos.cerrar();
		
		utilitario.addUpdate("set_activos");
	}

	/**
	 * Dibujar Reportes
	 */
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	/**
	 * Aceptar Reportes
	 */
	public void aceptarReporte() {
		

	    String entregadoPor="RECIBÍ CONFORME";
	    String recibiConforme="DEVUELTO POR ";
	    String tipoPrestamo="DEVOLUCIÓN";
		
		if (rep_reporte.getReporteSelecionado().equals("Ticket")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "DEVOLUCIÓN DE HERRAMIENTAS No. " + tab_cabecera.getValor("nro_secuencial_afdoc"));
				p_parametros.put("ide_afdoc", pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc")));
				p_parametros.put("pjefe_activos", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("entregadoPor", entregadoPor);
				p_parametros.put("recibiConforme", recibiConforme);
				p_parametros.put("tipo", tipoPrestamo);
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 */
	@Override
	public void insertar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
	
		
		if (tab_cabecera.isFocus()) {
			tab_cabecera.isFocus();
			tab_cabecera.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_cabecera.setValor("ide_gtemp", ide_gtempxx);
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.setValor("ide_geani", com_anio.getValue() + "");
		}
		if (!verificacionEdita()) {
			return;
		}
		if (tab_detalle_activo.isFocus()) {
			seleccionarActivos();
		}
		if (tab_detalle_custodio.isFocus()) {
			seleccionarCustodios();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#guardar()
	 */
	@Override
	public void guardar() {
		if (verificacionEdita()) {
			int numFilDA = tab_detalle_activo.getTotalFilas();
			int numFilCU = tab_detalle_custodio.getTotalFilas();
			if (numFilCU <= 0) {
				utilitario.agregarMensajeError("Detalle de Custodios", "El acta debe tener al menos un custodio");
				return;
			}
			if (numFilDA <= 0) {
				utilitario.agregarMensajeError("Detalle de Activos", "El acta debe tener al menos un activo");
				return;
			}
			if (tab_cabecera.getValor("foto_firma") == null) {
				utilitario.agregarMensajeInfo("Debe Cargar la FIRMA", "");
				return;
			}
			
			
			
			tab_cabecera.setValor("nro_secuencial_afdoc", ser_contabilidad.numeroSecuencial(par_secuencial_acta));
			tab_cabecera.guardar();
			tab_detalle_activo.guardar();
			tab_detalle_custodio.guardar();
			guardarPantalla();
			ser_contabilidad.guardaSecuencial(tab_cabecera.getValor("nro_secuencial_afdoc"), par_secuencial_acta);
			actualizaActaActivos();
		}
	}

	/**
	 * Funci&oacute;n que permite actualizar a los bienes su ultima acta de
	 * custodios.
	 */
	public void actualizaActaActivos() {
		int acta = pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc"));
		int numFil = tab_detalle_activo.getTotalFilas();
		for (int i = 0; i < numFil; i++) {
			tab_detalle_activo.setFilaActual(i);
			///utilitario.getConexion().ejecutarSql("UPDATE  afi_activo SET afi_prestado=false, afi_ultima_acta=" + acta + " WHERE ide_afact=" + pckUtilidades.CConversion.CInt(tab_detalle_activo.getValor("ide_afact")));
			utilitario.getConexion().ejecutarSql("UPDATE  afi_herramienta SET prestado_afher=false, acta_afher=" + acta + " WHERE ide_afher=" + pckUtilidades.CConversion.CInt(tab_detalle_activo.getValor("ide_afher")));
		}
	}
	
	
	
	

	public SeleccionTabla getSet_items_actas() {
		return set_items_actas;
	}



	public void setSet_items_actas(SeleccionTabla set_items_actas) {
		this.set_items_actas = set_items_actas;
	}



	/**
	 * (non-Javadoc)
	 * 
	 * @see paq_sistema.aplicacion.Pantalla#eliminar()
	 */
	@Override
	public void eliminar() {
		if (verificacionEdita()) {
			if (tab_detalle_activo.isFocus()) {
				tab_detalle_activo.eliminar();
			}
			if (tab_detalle_custodio.isFocus()) {
				tab_detalle_activo.eliminar();
			}
		}
	}

	@Override
	public void aceptarBuscar() {
		super.aceptarBuscar();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void actualizar() {
		super.actualizar();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();			
		}
	}
	
	@Override
	public void inicio() {
		super.inicio();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void siguiente() {
		super.siguiente();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void fin() {
		super.fin();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}

	@Override
	public void atras() {
		super.atras();
		if (tab_cabecera.isFocus()) {
			seleccionarDetalles();
		}
	}
	
	

	public SeleccionTabla getSet_empleadoSolicitante() {
		return set_empleadoSolicitante;
	}



	public void setSet_empleadoSolicitante(SeleccionTabla set_empleadoSolicitante) {
		this.set_empleadoSolicitante = set_empleadoSolicitante;
	}



	/**
	 * @return the tab_cabecera
	 */
	public Tabla getTab_cabecera() {
		return tab_cabecera;
	}

	/**
	 * @param tab_cabecera
	 *            the tab_cabecera to set
	 */
	public void setTab_cabecera(Tabla tab_cabecera) {
		this.tab_cabecera = tab_cabecera;
	}

	/**
	 * @return the tab_detalle_custodio
	 */
	public Tabla getTab_detalle_custodio() {
		return tab_detalle_custodio;
	}

	/**
	 * @param tab_detalle_custodio
	 *            the tab_detalle_custodio to set
	 */
	public void setTab_detalle_custodio(Tabla tab_detalle_custodio) {
		this.tab_detalle_custodio = tab_detalle_custodio;
	}

	/**
	 * @return the tab_detalle_activo
	 */
	public Tabla getTab_detalle_activo() {
		return tab_detalle_activo;
	}

	/**
	 * @param tab_detalle_activo
	 *            the tab_detalle_activo to set
	 */
	public void setTab_detalle_activo(Tabla tab_detalle_activo) {
		this.tab_detalle_activo = tab_detalle_activo;
	}

	/**
	 * @return the set_empleado
	 */
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	/**
	 * @param set_empleado
	 *            the set_empleado to set
	 */
	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	/**
	 * @return the set_activos
	 */
	public SeleccionTabla getSet_activos() {
		return set_activos;
	}

	/**
	 * @param set_activos
	 *            the set_activos to set
	 */
	public void setSet_activos(SeleccionTabla set_activos) {
		this.set_activos = set_activos;
	}

	/**
	 * @return the com_anio
	 */
	public Combo getCom_anio() {
		return com_anio;
	}

	/**
	 * @param com_anio
	 *            the com_anio to set
	 */
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	/**
	 * @return the parametroSecuencial
	 */
	public String getParametroSecuencial() {
		return parametroSecuencial;
	}

	/**
	 * @param parametroSecuencial
	 *            the parametroSecuencial to set
	 */
	public void setParametroSecuencial(String parametroSecuencial) {
		this.parametroSecuencial = parametroSecuencial;
	}

	/**
	 * @return the tipoDeActa
	 */
	public String getTipoDeActa() {
		return tipoDeActa;
	}

	/**
	 * @param tipoDeActa
	 *            the tipoDeActa to set
	 */
	public void setTipoDeActa(String tipoDeActa) {
		this.tipoDeActa = tipoDeActa;
	}

	/**
	 * @return the ser_nomina
	 */
	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	/**
	 * @param ser_nomina
	 *            the ser_nomina to set
	 */
	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	/**
	 * @return the ser_contabilidad
	 */
	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	/**
	 * @param ser_contabilidad
	 *            the ser_contabilidad to set
	 */
	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	/**
	 * @return the ser_seguridad
	 */
	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	/**
	 * @param ser_seguridad
	 *            the ser_seguridad to set
	 */
	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	/**
	 * @return the rep_reporte
	 */
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	/**
	 * @param rep_reporte
	 *            the rep_reporte to set
	 */
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	/**
	 * @return the self_reporte
	 */
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	/**
	 * @param self_reporte
	 *            the self_reporte to set
	 */
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	/**
	 * @return the ser_activos
	 */
	public ServicioActivos getSer_activos() {
		return ser_activos;
	}

	/**
	 * @param ser_activos
	 *            the ser_activos to set
	 */
	public void setSer_activos(ServicioActivos ser_activos) {
		this.ser_activos = ser_activos;
	}

	/**
	 * @return the p_parametros
	 */
	public Map getP_parametros() {
		return p_parametros;
	}

	/**
	 * @param p_parametros
	 *            the p_parametros to set
	 */
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	
	public Tabla getTab_cabecera_prestamo() {
		return tab_cabecera_prestamo;
	}

	public void setTab_cabecera_prestamo(Tabla tab_cabecera_prestamo) {
		this.tab_cabecera_prestamo = tab_cabecera_prestamo;
	}
	


	public SeleccionTabla getSet_prestamo() {
		return set_prestamo;
	}



	public void setSet_prestamo(SeleccionTabla set_prestamo) {
		this.set_prestamo = set_prestamo;
	}



	public SeleccionTabla getSet_items_actas_solicitante() {
		return set_items_actas_solicitante;
	}



	public void setSet_items_actas_solicitante(
			SeleccionTabla set_items_actas_solicitante) {
		this.set_items_actas_solicitante = set_items_actas_solicitante;
	}



	public String getTipoDeActaPrestamo() {
		return tipoDeActaPrestamo;
	}

	public void setTipoDeActaPrestamo(String tipoDeActaPrestamo) {
		this.tipoDeActaPrestamo = tipoDeActaPrestamo;
	}

	public Integer getNroActa() {
		return nroActa;
	}

	public void setNroActa(Integer nroActa) {
		this.nroActa = nroActa;
	}

}