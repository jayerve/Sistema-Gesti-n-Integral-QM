package paq_contabilidad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_contabilidad.ejb.ServicioCliente;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

public class pre_clientes_establecimientos extends Pantalla {

	private Tabla tab_direccion = new Tabla();
	private Tabla tab_telefono = new Tabla();
	private Tabla tab_email = new Tabla();
	private Tabla tab_documento = new Tabla();
	private Tabla tab_tarifa = new Tabla();
	private Tabla tab_clientes = new Tabla();
	private Tabla tab_establecimiento = new Tabla();
	private Tabla tab_establecimiento_obj = new Tabla();
	private SeleccionTabla set_pantalla_sucursal = new SeleccionTabla();
	private SeleccionTabla set_actualizar = new SeleccionTabla();
	private Confirmar con_guardar = new Confirmar();
	private AutoCompletar aut_factura = new AutoCompletar();
	private Check che_todos = new Check();
	private Division div_division = new Division();
	private String str_valor = new String();
	private SeleccionTabla set_cliente = new SeleccionTabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private Map p_parametros = new HashMap();
	private Consulta con_ver_establecimiento = new Consulta();
	private Dialogo dia_establecimiento_cliente = new Dialogo();

	public static String str_ide_retia_asistencia = "15"; // tipo de asistencia
	public static String str_ide_retil_tipo = "1"; // tipo de cliente
	public static String str_ide_reclr_ruta = "5"; // ruta
	public static String str_ide_tetar_tarifa = "1"; // tarifa

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);

	@EJB
	private ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);

	public pre_clientes_establecimientos() {

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiarE");
		aut_factura.setId("aut_factura");
		aut_factura.setAutoCompletar(ser_cliente.getDatosTiposServicios());
		aut_factura.setMetodoChange("seleccionoAutocompletar");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();

		che_todos.setId("che_todos");
		che_todos.setMetodoChange("seleccionaTodos");
		Etiqueta eti_todos = new Etiqueta("TODOS");
		bar_botones.agregarComponente(eti_todos);
		bar_botones.agregarComponente(che_todos);

		Etiqueta eti_colaborador = new Etiqueta("PUNTO DE VENTA:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
		// ingreso del boton

		clienteIngreso();
		detalleEstablecimiento();
		dialogoEstablecimiento();

		Boton bot_establecimiento = new Boton();
		bot_establecimiento.setValue("Ingreso de Establecimiento");
		bot_establecimiento.setTitle("Permite ingresar Establecimientos al Cliente seleccionado");
		bot_establecimiento.setIcon("ui-icon-person");
		bot_establecimiento.setMetodo("mostrarDialogoEstablecimiento");
		bar_botones.agregarBoton(bot_establecimiento);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion", "ide_recld", 2);
		// tab_direccion.dibujar();

		PanelTabla pat_direccion = new PanelTabla();
		pat_direccion.setPanelTabla(tab_direccion);

		tab_telefono.setId("tab_telefono");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("rec_cliente_telefono", "ide_reclt", 3);
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora", "ide_reteo", "detalle_reteo", "");
		// tab_telefono.dibujar();
		PanelTabla pat_telefono = new PanelTabla();
		pat_telefono.setPanelTabla(tab_telefono);

		tab_email.setId("tab_email");
		tab_email.setIdCompleto("tab_tabulador:tab_email");
		tab_email.setTabla("rec_cliente_email", "ide_recle", 4);
		// tab_email.dibujar();
		PanelTabla pat_email = new PanelTabla();
		pat_email.setPanelTabla(tab_email);

		tab_documento.setId("tab_documento");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTipoFormulario(true);
		tab_documento.getGrid().setColumns(4);
		tab_documento.setTabla("rec_cliente_archivo", "ide_recla", 5);
		tab_documento.getColumna("foto_recla").setUpload("clientes");
		tab_documento.getColumna("activo_recla").setLectura(true);

		// tab_documento.dibujar();
		PanelTabla pat_doc = new PanelTabla();
		pat_doc.setPanelTabla(tab_documento);

		tab_tarifa.setId("tab_tarifa");
		// tab_tarifa.setIdCompleto("tab_tabulador:tab_tarifa");
		tab_tarifa.setTabla("tes_cliente_tarifa", "ide_teclt", 6);
		tab_tarifa.getColumna("ide_temat").setCombo("select a.ide_temat,detalle_bomat,detalle_tetar,valor_temat from tes_material_tarifa a,bodt_material b,tes_tarifas c where a.ide_bomat = b.ide_bomat and  a.ide_tetar = c.ide_tetar");
		// tab_tarifa.dibujar();
		PanelTabla pat_tarifa = new PanelTabla();
		pat_tarifa.setPanelTabla(tab_tarifa);

		PanelTabla pat_establecimiento_obj = new PanelTabla();
		pat_establecimiento_obj.setPanelTabla(tab_establecimiento_obj);

		dia_establecimiento_cliente.setId("dia_establecimiento_cliente");
		dia_establecimiento_cliente.setTitle("ESTABLECIMIENTO DEL CLIENTE");
		dia_establecimiento_cliente.getBot_aceptar().setMetodo("ingresarEstablecimiento");
		dia_establecimiento_cliente.setDialogo(pat_establecimiento_obj);
		dia_establecimiento_cliente.setHeight("78%");
		dia_establecimiento_cliente.setWidth("94%");
		dia_establecimiento_cliente.setDynamic(false);
		agregarComponente(dia_establecimiento_cliente);

		PanelTabla pat_clientes = new PanelTabla();
		pat_clientes.setPanelTabla(tab_clientes);

		PanelTabla pat_establecimiento = new PanelTabla();
		pat_establecimiento.setPanelTabla(tab_establecimiento);
		// pat_establecimiento.setPanelTabla(tab_establecimiento);
		pat_establecimiento.setMensajeWarn("-- ESTABLECIMIENTOS -- ");

		Division div_aux = new Division();
		div_aux.dividir1(pat_establecimiento);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes, div_aux, "40%", "H");
		agregarComponente(div_division);

		/*
		 * // BOTON AGREGAR ESTABLECIMIENTO Boton bot_agregarSucursal = new
		 * Boton(); bot_agregarSucursal.setValue("AGREGAR ESTABLECIMIENTO");
		 * bot_agregarSucursal.setIcon("ui-icon-person");
		 * bot_agregarSucursal.setMetodo("ingresarEstablecimiento");
		 * bar_botones.agregarBoton(bot_agregarSucursal);
		 * 
		 * set_pantalla_sucursal.setId("con_ver_establecimiento");
		 * set_pantalla_sucursal.setTitle("INFORMACIÓN DEL ESTABLECIMIENTO");
		 * set_pantalla_sucursal.setWidth("60%");
		 * set_pantalla_sucursal.setHeight("50%");
		 * 
		 * set_pantalla_sucursal.getTab_seleccion().setTipoFormulario(true);
		 * set_pantalla_sucursal.getTab_seleccion().getGrid().setColumns(4); //
		 * para contruir los radios List lista_establecimiento1 = new
		 * ArrayList(); Object obj_fila_1[] = { "1", "MATRIZ" }; Object
		 * obj_fila_2[] = { "0", "SUCURSAL" };
		 * 
		 * lista_establecimiento1.add(obj_fila_1);
		 * lista_establecimiento1.add(obj_fila_2);
		 * set_pantalla_sucursal.getTab_seleccion()
		 * .getColumna("matriz_sucursal_reest").setRadio(lista_establecimiento1,
		 * "1"); set_pantalla_sucursal.getTab_seleccion()
		 * .getColumna("matriz_sucursal_reest").setRadioVertical(true);
		 * set_pantalla_sucursal.getTab_seleccion()
		 * .getColumna("matriz_sucursal_reest").setLectura(false);
		 * 
		 * set_pantalla_sucursal.getTab_seleccion().getColumna(
		 * "establecimiento_operativo_reest");
		 * set_pantalla_sucursal.getTab_seleccion().getColumna(
		 * "nombre_comercial_reest");
		 * set_pantalla_sucursal.getTab_seleccion().getColumna(
		 * "nro_establecimiento_reest");
		 * set_pantalla_sucursal.getTab_seleccion()
		 * .getColumna("codigo_zona_reest");
		 * set_pantalla_sucursal.getTab_seleccion().getColumna("coordx_reest");
		 * set_pantalla_sucursal
		 * .getTab_seleccion().getColumna("direccion_reest");
		 * set_pantalla_sucursal.getTab_seleccion().getColumna(
		 * "direccion_operativa_reest");
		 * set_pantalla_sucursal.getTab_seleccion()
		 * .getColumna("referencia_reest"); set_pantalla_sucursal
		 * .getTab_seleccion() .getColumna("ide_bogrm")
		 * .setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm",
		 * "autorizacion_sri_bogrm is not null"); set_pantalla_sucursal
		 * .getTab_seleccion() .getColumna("ide_tipcli")
		 * .setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli",
		 * ""); set_pantalla_sucursal.getBot_aceptar().setMetodo(
		 * "aceptaConsultaEstablecimiento");
		 * agregarComponente(con_ver_establecimiento);
		 */
	}

	public void agregarMatriz() {
		// Hace aparecer el componente

		if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("0")) {
			set_pantalla_sucursal.getTab_seleccion().setSql(ser_facturacion.getClientes("1"));
			set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
			set_pantalla_sucursal.setRadio();
			set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
			set_pantalla_sucursal.dibujar();
		} else {
			utilitario.agregarMensaje("No se puede registrar matriz a una matriz", "");
		}
	}

	public void aceptarMatriz() {
		String str_seleccionado = set_pantalla_sucursal.getValorSeleccionado();
		if (str_seleccionado != null) {
			// Inserto la sucursal en la tabla
			if (tab_clientes.isFilaInsertada() == false) {
				// Controla que si ya esta insertada no vuelva a insertar
				tab_clientes.insertar();
			}
			tab_clientes.setValor("rec_ide_recli", str_seleccionado);

			// actualizo la pantalla principal de cliente con los datos de la
			// matriz seleccionada
			TablaGenerica tab_cliente_matriz = ser_cliente.getTodoCliente(str_seleccionado);
			tab_clientes.setValor("ide_gttdi", tab_cliente_matriz.getValor("ide_gttdi"));
			tab_clientes.setValor("gth_ide_gttdi", tab_cliente_matriz.getValor("gth_ide_gttdi"));
			tab_clientes.setValor("nombre_comercial_recli", tab_cliente_matriz.getValor("nombre_comercial_recli"));
			tab_clientes.setValor("razon_social_recli", tab_cliente_matriz.getValor("razon_social_recli"));
			tab_clientes.setValor("ruc_comercial_recli", tab_cliente_matriz.getValor("ruc_comercial_recli"));
			tab_clientes.setValor("representante_legal_recli", tab_cliente_matriz.getValor("representante_legal_recli"));
			tab_clientes.setValor("ruc_representante_recli", tab_cliente_matriz.getValor("ruc_representante_recli"));
			tab_clientes.setValor("matriz_sucursal_recli", "0");
			tab_clientes.setValor("ide_retil", tab_cliente_matriz.getValor("ide_retil"));
			tab_clientes.setValor("ide_retic", tab_cliente_matriz.getValor("ide_retic"));
			tab_clientes.setValor("establecimiento_operativo_recli", tab_cliente_matriz.getValor("establecimiento_operativo_recli"));
			tab_clientes.setValor("ide_reclr", tab_cliente_matriz.getValor("ide_reclr"));
			tab_clientes.setValor("ide_tetar", tab_cliente_matriz.getValor("ide_tetar"));
			tab_clientes.setValor("ide_retia", tab_cliente_matriz.getValor("ide_retia"));
			tab_clientes.setValor("ide_bogrm", tab_cliente_matriz.getValor("ide_bogrm"));
			tab_clientes.setValor("ide_tipcli", tab_cliente_matriz.getValor("ide_tipcli"));

			set_pantalla_sucursal.cerrar();
			utilitario.addUpdate("tab_clientes");
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}

	public void aceptarSucursal() {
		String str_seleccionado = set_pantalla_sucursal.getValorSeleccionado();
		if (str_seleccionado != null) {
			// Inserto la sucursal en la tabla
			if (tab_clientes.isFilaInsertada() == false) {
				// Controla que si ya esta insertada no vuelva a insertar
				tab_clientes.insertar();
			}

			// actualizo la pantalla principal de cliente con los datos de la
			// matriz seleccionada
			TablaGenerica tab_cliente_matriz = ser_cliente.getTodoCliente(str_seleccionado);
			tab_clientes.setValor("ide_gttdi", tab_cliente_matriz.getValor("ide_gttdi"));
			tab_clientes.setValor("gth_ide_gttdi", tab_cliente_matriz.getValor("gth_ide_gttdi"));
			tab_clientes.setValor("nombre_comercial_recli", tab_cliente_matriz.getValor("nombre_comercial_recli"));
			tab_clientes.setValor("razon_social_recli", tab_cliente_matriz.getValor("razon_social_recli"));
			tab_clientes.setValor("ruc_comercial_recli", tab_cliente_matriz.getValor("ruc_comercial_recli"));
			tab_clientes.setValor("representante_legal_recli", tab_cliente_matriz.getValor("representante_legal_recli"));
			tab_clientes.setValor("ruc_representante_recli", tab_cliente_matriz.getValor("ruc_representante_recli"));
			tab_clientes.setValor("matriz_sucursal_recli", "0");
			tab_clientes.setValor("ide_retil", tab_cliente_matriz.getValor("ide_retil"));
			tab_clientes.setValor("ide_retic", tab_cliente_matriz.getValor("ide_retic"));
			tab_clientes.setValor("establecimiento_operativo_recli", tab_cliente_matriz.getValor("establecimiento_operativo_recli"));
			tab_clientes.setValor("ide_reclr", tab_cliente_matriz.getValor("ide_reclr"));
			tab_clientes.setValor("ide_tetar", tab_cliente_matriz.getValor("ide_tetar"));
			tab_clientes.setValor("ide_retia", tab_cliente_matriz.getValor("ide_retia"));
			tab_clientes.setValor("ide_bogrm", tab_cliente_matriz.getValor("ide_bogrm"));
			tab_clientes.setValor("ide_tipcli", tab_cliente_matriz.getValor("ide_tipcli"));
			// tab_clientes.setValor("rec_ide_recli",
			// tab_cliente_matriz.getValor("ide_recli"));
			tab_clientes.setValor("rec_ide_recli", str_seleccionado);

			System.out.println("tab_cliente_get" + tab_cliente_matriz.getValor("ide_tipcli") + "o el strselecciona" + str_seleccionado);
			set_pantalla_sucursal.cerrar();
			utilitario.addUpdate("tab_clientes");
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}

	public void limpiarE() {
		tab_clientes.limpiar();
		aut_factura.limpiar();
		tab_direccion.limpiar();
		tab_documento.limpiar();
		tab_email.limpiar();
		tab_tarifa.limpiar();
		tab_telefono.limpiar();
		tab_establecimiento.limpiar();
		// tab_factura_consulta.limpiar();
		utilitario.addUpdate("aut_factura");
	}

	/**
	 * Metodo para buscar los clientes de acuerdo al punto de venta 15 mayo 2017
	 * CC
	 * 
	 * @param evt
	 */

	public void seleccionoAutocompletar(SelectEvent evt) {
		String str_valor = aut_factura.getValor();
		aut_factura.onSelect(evt);
		tab_clientes.setCondicion(" activo_recli in (true) and ide_tipcli=" + aut_factura.getValor());
		tab_clientes.ejecutarSql();
		che_todos.setValue(false);
		// che_todos.isRendered();

		if (!aut_factura.getValor().equals("1")) {
			System.out.println("no hospitalarios");
			dibujaClienteNoHospi();
		} else {
			System.out.println("presenta los hospitalarios");
			dibujaClienteHospi();
		}
	}

	// METDO AUTOCOMPLETAR
	public void seleccionoRuc() {
		tab_clientes.setCondicion("ruc_comercial_recli");
		tab_clientes.ejecutarSql();

	}

	// /////actualizar matriz
	public void actualizarMatriz() {
		// Hace aparecer el componente
		if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("0")) {
			set_actualizar.getTab_seleccion().setSql(ser_facturacion.getClientes("1"));
			set_actualizar.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			set_actualizar.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
			set_actualizar.setRadio();
			set_actualizar.getTab_seleccion().ejecutarSql();
			set_actualizar.dibujar();
		} else {
			utilitario.agregarMensaje("No se puede registrar matriz a una matriz", "");
		}
	}

	public void modificarMatriz() {
		String str_matriz = set_actualizar.getValorSeleccionado();
		tab_clientes.setValor("rec_ide_recli", (str_matriz));
		tab_clientes.modificar(tab_clientes.getFilaActual());
		utilitario.addUpdate("tab_cliente");

		con_guardar.setMessage("Esta Seguro de Actualizar la matriz");
		con_guardar.setTitle("CONFIRMCION DE ACTUALIZAR");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzar");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");

	}

	public void guardarActualilzar() {
		System.out.println("Entra a guardar...");
		tab_clientes.guardar();
		con_guardar.cerrar();
		set_actualizar.cerrar();
		guardarPantalla();

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (aut_factura.getValor() != null) {
			utilitario.getTablaisFocus().insertar();
			tab_clientes.setValor("ide_bogrm", aut_factura.getValor());
			tab_establecimiento.insertar();
			// tab_establecimiento.setValor("ide_bogrm",
			// aut_factura.getValor());

		} else {
			utilitario.agregarMensajeError("Debe seleccionar el Punto de Venta", "");
		}

	}

	public void blanquear() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	/**
	 * Metodo para guardar el cliente validando el ruc y el número de
	 * establecimiento
	 */

	@Override
	public void guardar() {
		if (tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {

			String codigo = tab_clientes.getValor("ide_recli");
			if (codigo == null) {
				boolean boo_ruc = ser_cliente.validarRuc(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"));
				if (!boo_ruc) {
					guardarDatos();
				} else {
					utilitario.agregarMensajeInfo("No se puede guardar", "Existe un Cliente Registrado con el número de:" + tab_clientes.getValor("ruc_comercial_recli"));
					return;
				}
			} else {
				guardarDatos();
			}
		}
		/*
		 * else { boolean boo_nro_establecimiento =
		 * ser_cliente.validarSucursalEstablecimiento
		 * (tab_clientes.getValor("ruc_comercial_recli"
		 * ),Integer.parseInt(tab_clientes
		 * .getValor("nro_establecimiento_recli"))); if
		 * (!boo_nro_establecimiento) { guardarDatos(); }else {
		 * utilitario.agregarMensajeInfo("No se puede guardar",
		 * "Existe un Establecimiento registrado con el número de:"
		 * +tab_clientes.getValor("nro_establecimiento_recli")); return; } }
		 */
	}

	public void guardarDatos() {
		// TODO Auto-generated method stub
		if (tab_clientes.guardar()) {
			if (tab_establecimiento.guardar()) {
				// if (tab_telefono.guardar()) {
				// if (tab_email.guardar()) {
				// if (tab_tarifa.guardar()) {
				// tab_documento.guardar();
				// }
				// }

				// }
			}
		}
		guardarPantalla();

	}

	public void validaDocumento(AjaxBehaviorEvent evt) {
		tab_clientes.modificar(evt);
		if (!validarDocumentoIdentidad(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"))) {
			System.out.println("entre a validar cedula");
			tab_clientes.setValor("ruc_comercial_recli", "");
			utilitario.addUpdate("tab_clientes");
		}

		if (validarRuc(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"))) {
			System.out.println("si hay ruc repetidos");
			// utilitario.agregarMensajeInfo("Atencion",
			// "El numero de RUC /CÉDULA se encuentra REGISTRADO");
		} else {
			System.out.println("no hay ruc repetidos");
		}

	}

	public void validaRucRepetido() {

		if (!validarRuc(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"))) {

			// método para que realice la validación.
			tab_clientes.setValor("ruc_comercial_recli", "");
			// utilitario.addUpdate("tab_clientes");
			utilitario.agregarMensajeInfo("Atencion", "El numero de RUC/C.I. ingresado, ya EXISTE");
		}

	}

	public boolean validarRuc(String ide_gttdi, String documento_identidad) {

		String str_sql = "Select * from rec_clientes where ruc_comercial_recli=" + utilitario.generarComillaSimple(documento_identidad);

		// Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta = utilitario.consultar(str_sql);

		// Preguntamos si la tabla no esta vacia es decir que si retorno un
		// resultado la consulta
		if (!tab_consulta.isEmpty()) {
			// Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_ruc = tab_consulta.getValor("ruc_comercial_recli");
			// utilitario.agregarMensajeInfo("Atencion",
			// "El numero de RUC/C.I. ingresado, ya EXISTE");
			return true; //
		}
		return false; // retorna false

	}

	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 *         metodo booleano para validar el tipo de documento de identidad
	 *         cedula y ruc
	 */
	public boolean validarDocumentoIdentidad(String ide_gttdi, String documento_identidad) {
		if (ide_gttdi != null && !ide_gttdi.isEmpty()) {
			if (documento_identidad != null && !documento_identidad.isEmpty()) {
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))) {
					if (!utilitario.validarCedula(documento_identidad)) {
						utilitario.agregarMensajeInfo("Atencion", "El numero de cedula ingresado no es valido");
						return false;
					}
				} else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))) {
					if (!utilitario.validarRUC(documento_identidad)) {
						utilitario.agregarMensajeInfo("Atencion", "El numero de RUC ingresado no es valido");
						return false;
					}
				}
			}
		}
		return true;
	}

	/* Función para validar el ingreso de la Matriz */

	public boolean validarMatrizSucursal(String ruc_cliente) {

		if (tab_clientes.getValor("matriz_sucursal_recli") == "1") {
			System.out.println("entre a validar cedula");
			tab_clientes.setValor("ruc_comercial_recli", "");
			utilitario.addUpdate("tab_clientes");
		} else {

		}

		return true;
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public void seleccionaTodos() {

		limpiarE();

		if (che_todos.getValue().toString().equalsIgnoreCase("true")) {

			tab_clientes.setCondicion("");
			tab_clientes.ejecutarSql();
			dibujaClienteHospi();
		} else {

			tab_clientes.setCondicion("ide_bogrm=" + aut_factura.getValor());
			tab_clientes.ejecutarSql();
			che_todos.setOnchange("false");
		}
	}

	/* Método para agregar una Sucursal */

	public void ingresarSucursal() {

		tab_clientes.getColumna("matriz_sucursal_recli").setValorDefecto("1");

		if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {
			set_pantalla_sucursal.getTab_seleccion().setSql(ser_cliente.getClientes("1")); // se
																							// cambio
																							// al
																							// método
																							// del
																							// Servicio
																							// Cliente
																							// que
																							// devuelve
																							// los
																							// registros
																							// activos
			set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
			// set_pantalla_sucursal.setRadio();
			set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
			set_pantalla_sucursal.dibujar();
		} else {
			utilitario.agregarMensaje("No se puede registrar la Sucursal, debe seleccionar un punto de Venta", "");
		}
	}

	/**
	 * Metodo para validar el establecimiento y seguir con el metodo grabar
	 */
	public void ingresarEstablecimiento() {

		if (aut_factura.getValor() != null && !aut_factura.getValor().isEmpty()) {
			if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {
				boolean boo_nro_establecimiento = ser_cliente.validarNroEstablecimiento(tab_clientes.getValor("ide_recli"), tab_establecimiento_obj.getValor("nro_establecimiento_reest"));
				boolean boo_sucursal_establecimiento = ser_cliente.validarMatrizEstablecimiento(tab_clientes.getValor("ide_recli"), tab_establecimiento_obj.getValor("matriz_sucursal_reest"));
				if (!boo_nro_establecimiento) {
					if (!boo_sucursal_establecimiento) {
						ingresarEstablecimientoObj();
					}
				} else {
					// utilitario.agregarMensajeInfo("No se puede agregar",
					// "Existe número de Establecimiento:"+tab_establecimiento.getValor("nro_establecimiento_reest"));
					return;
				}

			} else {
				utilitario.agregarMensaje("No se puede registrar el Establecimiento, debe seleccionar Cliente", "");
			}
		} else {
			utilitario.agregarMensaje("No se puede registrar el Establecimiento, debe seleccionar Cliente", "");
		}

	}

	/**
	 * Metodo para ingresar un nuevo Establecimiento
	 */
	public void ingresarEstablecimientoObj() {

		if (aut_factura.getValor() != null && !aut_factura.getValor().isEmpty()) {
			if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {
				System.out.println("................");
				tab_establecimiento.insertar();
				tab_establecimiento.setValor("matriz_sucursal_reest", tab_establecimiento_obj.getValor("matriz_sucursal_reest"));
				tab_establecimiento.setValor("establecimiento_operativo_reest", tab_establecimiento_obj.getValor("establecimiento_operativo_reest"));
				tab_establecimiento.setValor("nombre_comercial_reest", tab_establecimiento_obj.getValor("nombre_comercial_reest"));
				tab_establecimiento.setValor("nro_establecimiento_reest", tab_establecimiento_obj.getValor("nro_establecimiento_reest"));
				tab_establecimiento.setValor("codigo_zona_reest", tab_establecimiento_obj.getValor("codigo_zona_reest"));
				tab_establecimiento.setValor("coordx_reest", tab_establecimiento_obj.getValor("coordx_reest"));
				tab_establecimiento.setValor("direccion_reest", tab_establecimiento_obj.getValor("direccion_reest"));
				tab_establecimiento.setValor("direccion_operativa_reest", tab_establecimiento_obj.getValor("direccion_operativa_reest"));
				tab_establecimiento.setValor("referencia_reest", tab_establecimiento_obj.getValor("referencia_reest"));
				tab_establecimiento.setValor("ide_bogrm", tab_establecimiento_obj.getValor("ide_bogrm"));
				tab_establecimiento.setValor("ide_tipcli", tab_establecimiento_obj.getValor("ide_tipcli"));
				tab_establecimiento.setValor("telefono_reest", tab_establecimiento_obj.getValor("telefono_reest"));
				tab_establecimiento.setValor("email_reest", tab_establecimiento_obj.getValor("email_reest"));
				tab_establecimiento.setValor("aplica_mtarifa_reest", tab_establecimiento_obj.getValor("aplica_mtarifa_reest"));
				tab_establecimiento.setValor("celular_reest", tab_establecimiento_obj.getValor("celular_reest"));
				tab_establecimiento.setValor("ide_retia", tab_establecimiento_obj.getValor("ide_retia"));
				tab_establecimiento.setValor("ide_retil", tab_establecimiento_obj.getValor("ide_retil"));
				tab_establecimiento.setValor("ide_reclr", tab_establecimiento_obj.getValor("ide_reclr"));
				dia_establecimiento_cliente.cerrar();
			} else {
				utilitario.agregarMensaje("No se puede registrar el Establecimiento, debe seleccionar Cliente", "");
			}
		} else {
			utilitario.agregarMensaje("No se puede registrar el Establecimiento, NO TIENE punto de venta", "");
		}
	}

	public void ingresarMatriz() {

		limpiarE();

		tab_clientes.getColumna("matriz_sucursal_recli").setValorDefecto("1");
		// tab_clientes.setValor("matriz_sucursal_recli", "1");
		aut_factura.setId("aut_factura");

		utilitario.getTablaisFocus().insertar();
	}

	public void importarMatriz() {
		if (tab_clientes.isEmpty()) {
			utilitario.agregarMensajeInfo("Debe escribir un ruc para depurar", "");
			return;
		}

		@SuppressWarnings("unused")
		String documento_identidad = tab_clientes.getValor("ruc_comercial_recli");

		String str_sql = "select a.ide_recli, ruc_comercial_recli,razon_social_recli,nombre_comercial_recli,establecimiento_operativo_recli," + "  nro_establecimiento_recli, " + "  direccion_recld from rec_clientes a "
				+ " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where ruc_comercial_recli in (" + utilitario.generarComillaSimple(documento_identidad) + ")"
				+ " and matriz_sucursal_recli = 1 and activo_recli = true ORDER BY  nombre_comercial_recli";

		set_cliente.getTab_seleccion().setSql(str_sql);
		set_cliente.getTab_seleccion().ejecutarSql();

		int a = set_cliente.getTab_seleccion().getTotalFilas();
		System.out.println("filas " + a);

		if (set_cliente.getTab_seleccion().getTotalFilas() > 1) {
			set_cliente.getTab_seleccion().getColumna("ide_recli");
			set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setNombreVisual("RUC");
			set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(50);
			set_cliente.getTab_seleccion().getColumna("razon_social_recli").setNombreVisual("Razon Social");
			set_cliente.getTab_seleccion().getColumna("razon_social_recli").setLongitud(70);
			set_cliente.getTab_seleccion().getColumna("nombre_comercial_recli").setNombreVisual("Nombre Comercial");
			set_cliente.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(70);
			set_cliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setNombreVisual("Establecimiento");
			set_cliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(50);
			set_cliente.getTab_seleccion().getColumna("nro_establecimiento_recli").setNombreVisual("Nro Establecimiento Operativo");
			set_cliente.getTab_seleccion().getColumna("nro_establecimiento_recli").setLongitud(30);
			set_cliente.getTab_seleccion().getColumna("direccion_recld").setNombreVisual("Dirección");
			set_cliente.getTab_seleccion().getColumna("direccion_recld").setLongitud(60);
			set_cliente.dibujar();
		} else {
			utilitario.agregarMensajeInfo("No existe registros duplicados", "");
			return;
		}
	}

	// seleccionar clientes de acuerdo al tipo (Matriz o Sucursal) por el RUC

	public void importarEntidad() {
		if (tab_clientes.isEmpty()) {
			utilitario.agregarMensajeInfo("Debe escribir un RUC/Cédula para depurar", "");
			return;
		}

		set_cliente.getTab_seleccion().setSql(ser_cliente.getClientesRucMoS(tab_clientes.getValor("ruc_comercial_recli"), tab_clientes.getValor("matriz_sucursal_recli")));
		set_cliente.getTab_seleccion().ejecutarSql();

		int a = set_cliente.getTab_seleccion().getTotalFilas();
		// System.out.println("filas " + a);

		if (set_cliente.getTab_seleccion().getTotalFilas() > 1) {
			set_cliente.getTab_seleccion().getColumna("ide_recli");
			// set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setNombreVisual("RUC");
			set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(50);
			set_cliente.getTab_seleccion().getColumna("razon_social_recli").setNombreVisual("Razon Social");
			set_cliente.getTab_seleccion().getColumna("razon_social_recli").setLongitud(70);
			set_cliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setNombreVisual("Establecimiento");
			set_cliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(20);
			set_cliente.getTab_seleccion().getColumna("nro_establecimiento_recli").setNombreVisual("Nro Establecimiento Operativo");
			set_cliente.getTab_seleccion().getColumna("nro_establecimiento_recli").setLongitud(20);
			set_cliente.getTab_seleccion().getColumna("nombre_comercial_recli").setNombreVisual("Nombre Comercial");
			set_cliente.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(50);
			set_cliente.getTab_seleccion().getColumna("direccion_recld").setNombreVisual("Dirección");
			set_cliente.getTab_seleccion().getColumna("direccion_recld").setLongitud(50);
			set_cliente.dibujar();
		} else {
			utilitario.agregarMensajeInfo("No existe registros duplicados", "");
			return;
		}
	}

	public void depurarEntidad() {

		String num = tab_clientes.getValor("matriz_sucursal_recli");
		// aut_factura.getValor().equals("1")
		if (tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {
			depurarMatriz();
		} else {
			depurarSucursal();
		}
	}

	public void depurarMatriz() {

		int num_seleccionado = set_cliente.getNumeroSeleccionados();
		String str_seleccionados = set_cliente.getSeleccionados();
		set_cliente.seleccinarInversa();
		String str_noseleccionados = set_cliente.getSeleccionados();
		int num_noseleccionado = set_cliente.getNumeroSeleccionados();

		System.out.println("los seleccionados: " + str_seleccionados + "no seleccionados : " + str_noseleccionados);

		if (!str_seleccionados.contentEquals("")) {
			if (num_noseleccionado >= 1) {

				TablaGenerica tab_cliente_generica = ser_cliente.getTablaGenericaCliente(str_seleccionados);
				TablaGenerica tab_sucursal_generica = ser_cliente.getTablaGenericaSucursal(str_seleccionados);

				System.out.println(" tabla generica" + tab_cliente_generica.getSql());
				System.out.println(" tabla sucursal" + tab_sucursal_generica.getSql());
				// actualizo (borro) los registros
				for (int i = 0; i < tab_cliente_generica.getTotalFilas(); i++) {
					System.out.println("valor cliente" + tab_cliente_generica.getValor(i, "ide_recli"));
					ser_cliente.borrarClienteMatriz(tab_cliente_generica.getValor(i, "ide_recli"));
				}
				// actualizo las sucursales con el campo rec_ide_recli
				int max = ser_cliente.getMaxMatriz(str_noseleccionados);

				for (int i = 0; i < tab_sucursal_generica.getTotalFilas(); i++) {

					System.out.println("valor sucursal" + tab_sucursal_generica.getValor(i, "ide_recli"));
					ser_cliente.actualizoSucursal(tab_sucursal_generica.getValor(i, "ide_recli"), max); // borrarClienteMatriz(tab_cliente.getValor(i,
																										// "ide_recli"));
				}

				set_cliente.cerrar();
				utilitario.addUpdate("tab_cliente");
				utilitario.agregarMensajeInfo("Se eliminaron exitosamente", "");
			} else {
				utilitario.agregarMensajeInfo("Debe quedar como mínimo un registro", "");
			}
		} else {

			utilitario.agregarMensajeInfo("Debe seleccionar algun registro", "");
		}
	}

	public void depurarSucursal() {

		int num_seleccionado = set_cliente.getNumeroSeleccionados();
		String str_seleccionados = set_cliente.getSeleccionados();
		set_cliente.seleccinarInversa();
		String str_noseleccionados = set_cliente.getSeleccionados();
		int num_noseleccionado = set_cliente.getNumeroSeleccionados();

		if (!str_seleccionados.contentEquals("")) {
			if (num_noseleccionado >= 1) {

				TablaGenerica tab_cliente_generica = ser_cliente.getTablaGenericaCliente(str_seleccionados);
				// actualizo (borro) los registros
				for (int i = 0; i < tab_cliente_generica.getTotalFilas(); i++) {
					System.out.println("valor cliente" + tab_cliente_generica.getValor(i, "ide_recli"));
					ser_cliente.borrarClienteSucursal(tab_cliente_generica.getValor(i, "ide_recli"));
				}
				set_cliente.cerrar();
				utilitario.addUpdate("tab_cliente");
				utilitario.agregarMensajeInfo("Se eliminaron exitosamente", "");
			} else {
				utilitario.agregarMensajeInfo("Debe quedar como mínimo un registro", "");
			}
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar algun registro", "");
		}
	}

	/**
	 * Metodo para dibujar los campos de los clientes tipo Hospitalario 15 mayo
	 * 2017 CC
	 */
	public void dibujaClienteHospi() {

		tab_clientes.getColumna("activo_recli").setVisible(true);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);
		tab_clientes.getColumna("fecha_ingreso_recli").setVisible(false);
		tab_clientes.getColumna("ide_retic").setCombo("rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_clientes.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_clientes.getColumna("ide_retia").setVisible(true);
		tab_clientes.getColumna("ide_gtgen").setCombo("gth_genero", "ide_gtgen", "detalle_gtgen", "");
		tab_clientes.getColumna("ide_gtgen").setVisible(false);
		tab_clientes.getColumna("ide_gedip").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
		tab_clientes.getColumna("ide_gedip").setAutoCompletar();
		tab_clientes.getColumna("ide_gedip").setVisible(true);
		tab_clientes.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", " detalle_gttdi", "");
		tab_clientes.getColumna("gth_ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_clientes.getColumna("ide_gtesc").setCombo("gth_estado_civil", " ide_gtesc ", "detalle_gtesc", "");
		tab_clientes.getColumna("ide_gtesc").setVisible(false);
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_clientes.getColumna("representante_legal_recli"); // .setMetodoChange("validaDocumentoRepre");
		tab_clientes.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_clientes.getColumna("ide_retil").setVisible(true);
		tab_clientes.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_clientes.getColumna("ide_reclr").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_clientes.getColumna("rec_ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_clientes.getColumna("rec_ide_recli").setAutoCompletar();
		tab_clientes.getColumna("rec_ide_recli").setVisible(false);
		tab_clientes.getColumna("ide_bogrm").setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm", "autorizacion_sri_bogrm is not null");
		tab_clientes.getColumna("ide_bogrm").setVisible(true);
		tab_clientes.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_clientes.getColumna("ide_tipcli").setVisible(true);
		tab_clientes.getColumna("codigo_zona_recli").setVisible(true);
		tab_clientes.getColumna("aplica_mtarifa_recli").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setVisible(true);
		tab_clientes.getColumna("telefono_factura_recli").setVisible(true);
		tab_clientes.getColumna("estimado_desecho_recl").setVisible(true);
		tab_clientes.getColumna("num_generador_desecho_recli").setVisible(true);
		tab_clientes.getColumna("razon_social_recli").setVisible(true);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(true);
		tab_clientes.getColumna("aplica_interes_recli").setVisible(true);
		tab_clientes.getColumna("fac_mensual_recli").setVisible(true);
		tab_clientes.getColumna("fecha_emision_generador_recli").setVisible(true);
		tab_clientes.getColumna("TELEFONO_CELU_RECLI").setNombreVisual("Fono Celular:");
		tab_clientes.getColumna("TELEFONO_CELU_RECLI").setVisible(true);
		tab_clientes.getColumna("EMAIL_RECLI").setNombreVisual("Email");
		tab_clientes.getColumna("EMAIL_RECLI").setVisible(true);
		tab_clientes.getColumna("DIRECCION_RECLI").setNombreVisual("Dirección:");
		tab_clientes.getColumna("DIRECCION_RECLI").setVisible(true);
		tab_clientes.getColumna("VALOR_FACTURA_RECLI").setNombreVisual("Valor de última factura");
		tab_clientes.getColumna("VALOR_FACTURA_RECLI").setLectura(true);
		tab_clientes.getColumna("FECHA_FACTURA_RECLI").setNombreVisual("Fecha de última factura");
		tab_clientes.getColumna("FECHA_FACTURA_RECLI").setLectura(true);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);
		tab_clientes.getColumna("nro_contrato_recli").setVisible(false);
		tab_clientes.getColumna("coordx_recli").setVisible(false);
		tab_clientes.getColumna("coordy_recli").setVisible(false);
		tab_clientes.getColumna("estado_recli").setVisible(false);
		tab_clientes.getColumna("nro_establecimiento_recli").setVisible(false);
		tab_clientes.getColumna("ide_gtgen").setVisible(true);
		tab_clientes.getColumna("ide_gedip").setVisible(true);
		tab_clientes.getColumna("nombre_comercial_recli").setVisible(true);
		tab_clientes.getColumna("ide_retil").setVisible(true);
		tab_clientes.getColumna("ide_reclr").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setVisible(true);
		tab_clientes.getColumna("matriz_sucursal_recli").setVisible(false);
		tab_clientes.dibujar();
		utilitario.addUpdate("tab_clientes");
		System.out.println("metodo dibuajr cliente hospitalario");
	}

	/**
	 * Metodo para dibujar los clientes no hospitalarios 15 mayo 2017 CC
	 */
	public void dibujaClienteNoHospi() {

		tab_clientes.getColumna("ide_retia").setVisible(false);
		tab_clientes.getColumna("ide_gtgen").setVisible(false);
		tab_clientes.getColumna("ide_gedip").setVisible(false);
		tab_clientes.getColumna("nombre_comercial_recli").setVisible(false);
		tab_clientes.getColumna("ide_retil").setValorDefecto(str_ide_retil_tipo);
		tab_clientes.getColumna("ide_retil").setVisible(false);
		tab_clientes.getColumna("ide_reclr").setValorDefecto(str_ide_reclr_ruta);
		tab_clientes.getColumna("ide_tetar").setValorDefecto(str_ide_tetar_tarifa);
		tab_clientes.getColumna("ide_tetar").setVisible(false);
		tab_clientes.getColumna("matriz_sucursal_recli").setVisible(false);
		tab_clientes.getColumna("ide_bogrm").setValorDefecto(aut_factura.getValor());
		tab_clientes.getColumna("nro_contrato_recli").setVisible(false);
		tab_clientes.getColumna("coordx_recli").setVisible(false);
		tab_clientes.getColumna("coordy_recli").setVisible(false);
		tab_clientes.getColumna("estimado_desecho_recl").setVisible(false);
		tab_clientes.getColumna("num_generador_desecho_recli").setVisible(false);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(false);
		tab_clientes.getColumna("fecha_emision_generador_recli").setVisible(false);
		tab_clientes.getColumna("codigo_zona_recli").setVisible(false);
		tab_clientes.getColumna("ide_tipcli").setVisible(false);
		tab_clientes.getColumna("estado_recli").setVisible(false);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);

		tab_clientes.getColumna("ide_reclr").setVisible(true);
		tab_clientes.getColumna("fac_mensual_recli").setVisible(true);
		tab_clientes.getColumna("aplica_interes_recli").setVisible(true);
		tab_clientes.getColumna("telefono_factura_recli").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setVisible(true);

		tab_clientes.dibujar();
		utilitario.addUpdate("tab_clientes");
	}

	/**
	 * Metodo para dibujar datos del cliente 10-05-2017 CC
	 */

	private void clienteIngreso() {
		tab_clientes.setId("tab_clientes");
		tab_clientes.setTipoFormulario(true);
		tab_clientes.getGrid().setColumns(4);
		tab_clientes.setTabla("rec_clientes", "ide_recli", 1);
		tab_clientes.setCondicion("rec_ide_recli=-1");
		tab_clientes.getColumna("activo_recli").setValorDefecto("true");
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);
		tab_clientes.getColumna("fecha_ingreso_recli").setVisible(false);
		tab_clientes.getColumna("ide_retic").setCombo("rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_clientes.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_clientes.getColumna("ide_retia").setVisible(true);
		tab_clientes.getColumna("ide_gtgen").setCombo("gth_genero", "ide_gtgen", "detalle_gtgen", "");
		tab_clientes.getColumna("ide_gtgen").setVisible(false);
		tab_clientes.getColumna("ide_gedip").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
		tab_clientes.getColumna("ide_gedip").setAutoCompletar();
		tab_clientes.getColumna("ide_gedip").setVisible(true);
		tab_clientes.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", " detalle_gttdi", "");
		tab_clientes.getColumna("gth_ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_clientes.getColumna(" ide_gtesc ").setCombo("gth_estado_civil", " ide_gtesc ", "detalle_gtesc", "");
		tab_clientes.getColumna(" ide_gtesc ").setVisible(false);
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaRucRepetido");
		tab_clientes.getColumna("representante_legal_recli"); // .setMetodoChange("validaDocumentoRepre");
		tab_clientes.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_clientes.getColumna("ide_retil").setVisible(true);
		tab_clientes.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_clientes.getColumna("ide_reclr").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_clientes.getColumna("rec_ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_clientes.getColumna("rec_ide_recli").setAutoCompletar();
		tab_clientes.getColumna("rec_ide_recli").setVisible(false);
		// tab_clientes.getColumna("ide_bogrm").setCombo(ser_facturacion.);
		tab_clientes.getColumna("ide_bogrm").setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm", "autorizacion_sri_bogrm is not null");
		tab_clientes.getColumna("ide_bogrm").setVisible(true);
		tab_clientes.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_clientes.getColumna("ide_tipcli").setVisible(true);
		// para contruir los radios
		List lista = new ArrayList();
		Object fila1[] = { "1", "MATRIZ" };
		Object fila2[] = { "0", "SUCURSAL" };

		lista.add(fila1);
		lista.add(fila2);
		tab_clientes.getColumna("matriz_sucursal_recli").setRadio(lista, "1");
		tab_clientes.getColumna("matriz_sucursal_recli").setRadioVertical(true);
		tab_clientes.getColumna("matriz_sucursal_recli").setLectura(true);
		tab_clientes.getColumna("matriz_sucursal_recli").setVisible(false);
		// RADIOS
		// para contruir los radios
		List lista1 = new ArrayList();
		Object fila3[] = { "1", "MATRIZ" };
		Object fila4[] = { "0", "SUCURSAL" };
		lista1.add(fila3);
		lista1.add(fila4);
		tab_clientes.getColumna("factura_datos_recli").setRadio(lista1, "2");
		tab_clientes.getColumna("factura_datos_recli").setRadioVertical(true);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);

		tab_clientes.getColumna("codigo_zona_recli").setVisible(true);
		tab_clientes.getColumna("aplica_mtarifa_recli").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setVisible(true);
		tab_clientes.getColumna("telefono_factura_recli").setVisible(true);
		tab_clientes.getColumna("estimado_desecho_recl").setVisible(true);
		tab_clientes.getColumna("num_generador_desecho_recli").setVisible(true);
		tab_clientes.getColumna("razon_social_recli").setVisible(true);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(true);
		tab_clientes.getColumna("aplica_interes_recli").setVisible(true);
		tab_clientes.getColumna("fac_mensual_recli").setVisible(true);
		tab_clientes.getColumna("fecha_emision_generador_recli").setVisible(true);
		tab_clientes.getColumna("TELEFONO_CELU_RECLI").setNombreVisual("Fono Celular:");
		tab_clientes.getColumna("TELEFONO_CELU_RECLI").setVisible(true);
		tab_clientes.getColumna("EMAIL_RECLI").setNombreVisual("Email");
		tab_clientes.getColumna("EMAIL_RECLI").setVisible(true);
		tab_clientes.getColumna("DIRECCION_RECLI").setNombreVisual("Dirección:");
		tab_clientes.getColumna("DIRECCION_RECLI").setVisible(true);
		tab_clientes.getColumna("VALOR_FACTURA_RECLI").setNombreVisual("Valor de última factura");
		tab_clientes.getColumna("VALOR_FACTURA_RECLI").setLectura(true);
		tab_clientes.getColumna("FECHA_FACTURA_RECLI").setNombreVisual("Fecha de última factura");
		tab_clientes.getColumna("FECHA_FACTURA_RECLI").setLectura(true);

		tab_clientes.getColumna("ide_gtgen").setVisible(false);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);
		tab_clientes.getColumna("nro_contrato_recli").setVisible(false);
		tab_clientes.getColumna("coordx_recli").setVisible(false);
		tab_clientes.getColumna("coordy_recli").setVisible(false);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(false);
		tab_clientes.getColumna("estado_recli").setVisible(false);
		tab_clientes.getColumna("nro_establecimiento_recli").setVisible(false);

		tab_clientes.agregarRelacion(tab_direccion);
		tab_clientes.agregarRelacion(tab_telefono);
		tab_clientes.agregarRelacion(tab_email);
		tab_clientes.agregarRelacion(tab_tarifa);
		tab_clientes.agregarRelacion(tab_documento);
		tab_clientes.agregarRelacion(tab_establecimiento);
		tab_clientes.dibujar();
		// utilitario.addUpdate("tab_clientes");
	}

	/**
	 * Metodo para dinujar el establecimiento 10-05-107 CC
	 */
	private void detalleEstablecimiento() {
		tab_establecimiento.setId("tab_establecimiento");
		tab_establecimiento.setTipoFormulario(true);
		tab_establecimiento.getGrid().setColumns(4);
		tab_establecimiento.setTabla("rec_clientes_establecimiento", "ide_reest", 7);
		tab_establecimiento.setCampoForanea("ide_recli");
		tab_establecimiento.getColumna("ide_reest").setNombreVisual("CODIGO");
		tab_establecimiento.getColumna("ide_reest").setVisible(true);
		tab_establecimiento.getColumna("activo_reest").setNombreVisual("ACTIVO");
		tab_establecimiento.getColumna("activo_reest").setValorDefecto("true");
		tab_establecimiento.getColumna("activo_reest").setVisible(true);

		// para contruir los radios
		List lista_establecimiento = new ArrayList();
		Object obj_fila1[] = { "1", "MATRIZ" };
		Object obj_fila2[] = { "0", "SUCURSAL" };

		lista_establecimiento.add(obj_fila1);
		lista_establecimiento.add(obj_fila2);
		tab_establecimiento.getColumna("matriz_sucursal_reest").setRadio(lista_establecimiento, "1");
		tab_establecimiento.getColumna("matriz_sucursal_reest").setRadioVertical(true);
		tab_establecimiento.getColumna("matriz_sucursal_reest").setLectura(false);

		tab_establecimiento.getColumna("establecimiento_operativo_reest").setNombreVisual("ESTABLECIMIENTO OPERATIVO");
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setVisible(true);
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setLongitud(75);
		tab_establecimiento.getColumna("nombre_comercial_reest").setNombreVisual("NOMBRE COMERCIAL");
		tab_establecimiento.getColumna("nombre_comercial_reest").setVisible(true);
		tab_establecimiento.getColumna("nro_establecimiento_reest").setNombreVisual("Nro. ESTABLECIMIENTO");
		tab_establecimiento.getColumna("nro_establecimiento_reest").setVisible(true);
		tab_establecimiento.getColumna("codigo_zona_reest").setNombreVisual("CÓDIGO DE ZONA");
		tab_establecimiento.getColumna("codigo_zona_reest").setVisible(true);
		tab_establecimiento.getColumna("coordx_reest").setVisible(false);
		tab_establecimiento.getColumna("coordy_reest").setVisible(false);
		tab_establecimiento.getColumna("direccion_reest").setNombreVisual("DIRECCIÓN DE ESTABLECIMIENTO");
		tab_establecimiento.getColumna("direccion_reest").setVisible(true);
		tab_establecimiento.getColumna("direccion_operativa_reest").setNombreVisual("DIRECCIÓN OPERATIVA");
		tab_establecimiento.getColumna("direccion_operativa_reest").setVisible(false);
		tab_establecimiento.getColumna("referencia_reest").setNombreVisual("REFERENCIA");
		tab_establecimiento.getColumna("referencia_reest").setVisible(true);
		tab_establecimiento.getColumna("ide_bogrm").setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm", "autorizacion_sri_bogrm is not null");
		tab_establecimiento.getColumna("ide_bogrm").setNombreVisual("PUNTO DE VENTA");
		tab_establecimiento.getColumna("ide_bogrm").setVisible(true);
		tab_establecimiento.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_establecimiento.getColumna("ide_tipcli").setNombreVisual("TIPO DE SERVICIO");
		tab_establecimiento.getColumna("ide_tipcli").setVisible(true);
		tab_establecimiento.getColumna("email_reest").setNombreVisual("E-MAIL");
		tab_establecimiento.getColumna("email_reest").setVisible(true);
		tab_establecimiento.getColumna("telefono_reest").setNombreVisual("TELÉFONO");
		tab_establecimiento.getColumna("telefono_reest").setVisible(true);

		tab_establecimiento.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_establecimiento.getColumna("ide_retia").setVisible(true);
		tab_establecimiento.getColumna("ide_retia").setNombreVisual("TIPO DE FRECUENCIA");
		tab_establecimiento.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_establecimiento.getColumna("ide_retil").setVisible(true);
		tab_establecimiento.getColumna("ide_retil").setNombreVisual("TIPO DE ESTABLECIMIENTO");
		tab_establecimiento.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_establecimiento.getColumna("ide_reclr").setVisible(true);
		tab_establecimiento.getColumna("ide_reclr").setNombreVisual("RUTA");
		tab_establecimiento.getColumna("notificacion_reest").setVisible(false);

		tab_establecimiento.dibujar();
	}

	private void dialogoEstablecimiento() {
		tab_establecimiento_obj.setId("tab_establecimiento_obj");
		tab_establecimiento_obj.setTabla("rec_clientes_establecimiento", "ide_reest", 8);
		tab_establecimiento_obj.setCampoForanea("ide_recli");

		tab_establecimiento_obj.getColumna("ide_reest").setNombreVisual("CODIGO");
		tab_establecimiento_obj.getColumna("ide_reest").setOrden(0);
		tab_establecimiento_obj.getColumna("ide_reest").setVisible(true);

		tab_establecimiento_obj.getColumna("activo_reest").setNombreVisual("ACTIVO");
		tab_establecimiento_obj.getColumna("activo_reest").setValorDefecto("true");
		tab_establecimiento_obj.getColumna("activo_reest").setOrden(1);
		tab_establecimiento_obj.getColumna("activo_reest").setVisible(true);

		// para contruir los radios
		List lista_establecimiento = new ArrayList();
		Object obj_fila1[] = { "1", "MATRIZ" };
		Object obj_fila2[] = { "0", "SUCURSAL" };

		lista_establecimiento.add(obj_fila1);
		lista_establecimiento.add(obj_fila2);
		tab_establecimiento_obj.getColumna("matriz_sucursal_reest").setRadio(lista_establecimiento, "1");
		tab_establecimiento_obj.getColumna("matriz_sucursal_reest").setRadioVertical(true);
		tab_establecimiento_obj.getColumna("matriz_sucursal_reest").setLectura(false);

		tab_establecimiento_obj.getColumna("establecimiento_operativo_reest").setNombreVisual("ESTABLECIMIENTO OPERATIVO");
		tab_establecimiento_obj.getColumna("establecimiento_operativo_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("establecimiento_operativo_reest").setLongitud(75);
		tab_establecimiento_obj.getColumna("nombre_comercial_reest").setNombreVisual("NOMBRE COMERCIAL");
		tab_establecimiento_obj.getColumna("nombre_comercial_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("nombre_comercial_reest").setValorDefecto(tab_clientes.getValor("nombre_comercial_recli"));
		tab_establecimiento_obj.getColumna("nro_establecimiento_reest").setNombreVisual("Nro. ESTABLECIMIENTO");
		tab_establecimiento_obj.getColumna("nro_establecimiento_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("codigo_zona_reest").setNombreVisual("CÓDIGO DE ZONA");
		tab_establecimiento_obj.getColumna("codigo_zona_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("coordx_reest").setVisible(false);
		tab_establecimiento_obj.getColumna("coordy_reest").setVisible(false);
		tab_establecimiento_obj.getColumna("direccion_reest").setNombreVisual("DIRECCIÓN DE ESTABLECIMIENTO");
		tab_establecimiento_obj.getColumna("direccion_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("direccion_operativa_reest").setNombreVisual("DIRECCIÓN OPERATIVA");
		tab_establecimiento_obj.getColumna("direccion_operativa_reest").setVisible(false);
		tab_establecimiento_obj.getColumna("referencia_reest").setNombreVisual("REFERENCIA");
		tab_establecimiento_obj.getColumna("referencia_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("ide_bogrm").setValorDefecto(tab_clientes.getValor("ide_bogrm"));
		tab_establecimiento_obj.getColumna("ide_bogrm").setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm", "autorizacion_sri_bogrm is not null");
		tab_establecimiento_obj.getColumna("ide_bogrm").setNombreVisual("PUNTO DE VENTA");
		tab_establecimiento_obj.getColumna("ide_bogrm").setVisible(true);
		tab_establecimiento_obj.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_establecimiento_obj.getColumna("ide_tipcli").setNombreVisual("TIPO DE SERVICIO");
		tab_establecimiento_obj.getColumna("ide_tipcli").setVisible(true);
		tab_establecimiento_obj.getColumna("email_reest").setNombreVisual("E-MAIL");
		tab_establecimiento_obj.getColumna("email_reest").setVisible(true);
		tab_establecimiento_obj.getColumna("telefono_reest").setNombreVisual("TELÉFONO");
		tab_establecimiento_obj.getColumna("telefono_reest").setVisible(true);

		tab_establecimiento_obj.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_establecimiento_obj.getColumna("ide_retia").setVisible(true);
		tab_establecimiento_obj.getColumna("ide_retia").setNombreVisual("TIPO DE FRECUENCIA");
		tab_establecimiento_obj.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_establecimiento_obj.getColumna("ide_retil").setVisible(true);
		tab_establecimiento_obj.getColumna("ide_retil").setNombreVisual("TIPO DE ESTABLECIMIENTO");
		tab_establecimiento_obj.getColumna("ide_retil").setOrden(50);
		tab_establecimiento_obj.getColumna("ide_retil").setLongitud(50);
		tab_establecimiento_obj.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_establecimiento_obj.getColumna("ide_reclr").setVisible(true);
		tab_establecimiento_obj.getColumna("ide_reclr").setOrden(49);
		tab_establecimiento_obj.getColumna("ide_reclr").setNombreVisual("RUTA");
		tab_establecimiento_obj.getColumna("notificacion_reest").setVisible(false);
		tab_establecimiento_obj.getColumna("aplica_mtarifa_reest").setVisible(false);
		tab_establecimiento_obj.getColumna("ide_gedip").setVisible(false);
		tab_establecimiento_obj.getColumna("factura_datos_reest").setVisible(false);
		/*
		 * // para contruir los radios List lista_establecimiento = new
		 * ArrayList(); Object obj_fila1[] = { "1", "MATRIZ" }; Object
		 * obj_fila2[] = { "0", "SUCURSAL" };
		 * 
		 * lista_establecimiento.add(obj_fila1);
		 * lista_establecimiento.add(obj_fila2);
		 * tab_establecimiento_obj.getColumna
		 * ("matriz_sucursal_reest").setRadio(lista_establecimiento, "1");
		 * tab_establecimiento_obj
		 * .getColumna("matriz_sucursal_reest").setRadioVertical(true);
		 * tab_establecimiento_obj
		 * .getColumna("matriz_sucursal_reest").setLectura(false);
		 * tab_establecimiento_obj
		 * .getColumna("ide_reest").setNombreVisual("CODIGO");
		 * tab_establecimiento_obj
		 * .getColumna("ide_reest").setValorDefecto("true");
		 * 
		 * tab_establecimiento_obj.getColumna("establecimiento_operativo_reest").
		 * setNombreVisual("Establecimiento Operativo");
		 * tab_establecimiento_obj.
		 * getColumna("establecimiento_operativo_reest").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("nombre_comercial_reest").setNombreVisual
		 * ("Nombre Comercial");
		 * tab_establecimiento_obj.getColumna("nombre_comercial_reest"
		 * ).setVisible(true);
		 * tab_establecimiento_obj.getColumna("nro_establecimiento_reest"
		 * ).setNombreVisual("Nro. Establecimiento");
		 * tab_establecimiento_obj.getColumna
		 * ("nro_establecimiento_reest").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("codigo_zona_reest").setNombreVisual("Código de Zona");
		 * tab_establecimiento_obj
		 * .getColumna("codigo_zona_reest").setVisible(true);
		 * tab_establecimiento_obj.getColumna("coordx_reest").setVisible(false);
		 * tab_establecimiento_obj.getColumna("coordy_reest").setVisible(false);
		 * tab_establecimiento_obj
		 * .getColumna("direccion_reest").setNombreVisual(
		 * "Dirección Establecimiento");
		 * tab_establecimiento_obj.getColumna("direccion_reest"
		 * ).setVisible(true);
		 * tab_establecimiento_obj.getColumna("direccion_operativa_reest"
		 * ).setNombreVisual("Dirección Operativa");
		 * tab_establecimiento_obj.getColumna
		 * ("direccion_operativa_reest").setVisible(false);
		 * tab_establecimiento_obj
		 * .getColumna("referencia_reest").setNombreVisual("Referencia");
		 * tab_establecimiento_obj
		 * .getColumna("referencia_reest").setVisible(true);
		 * tab_establecimiento_obj.getColumna("ide_bogrm").setCombo(
		 * "bodt_grupo_material", "ide_bogrm", "detalle_bogrm",
		 * "autorizacion_sri_bogrm is not null");
		 * tab_establecimiento_obj.getColumna
		 * ("referencia_reest").setNombreVisual("REFERENCIA");
		 * tab_establecimiento_obj
		 * .getColumna("referencia_reest").setVisible(true);
		 * tab_establecimiento_obj.getColumna("ide_tipcli").setCombo(
		 * "rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		 * tab_establecimiento_obj
		 * .getColumna("ide_tipcli").setNombreVisual("TIPO DE SERVICIO");
		 * tab_establecimiento_obj.getColumna("ide_tipcli").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("email_reest").setNombreVisual("E-mail");
		 * tab_establecimiento_obj.getColumna("email_reest").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("telefono_reest").setNombreVisual("Teléfono");
		 * tab_establecimiento_obj
		 * .getColumna("telefono_reest").setVisible(true);
		 * 
		 * tab_establecimiento_obj.getColumna("ide_retia").setCombo(
		 * "rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		 * tab_establecimiento_obj.getColumna("ide_retia").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("ide_retia").setNombreVisual("Tipo de frecuencia");
		 * tab_establecimiento_obj
		 * .getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil",
		 * "detalle_retil", "");
		 * tab_establecimiento_obj.getColumna("ide_retil").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("ide_retil").setNombreVisual("Tipo de Establecimiento");
		 * tab_establecimiento_obj
		 * .getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr",
		 * "detalle_reclr", "");
		 * tab_establecimiento_obj.getColumna("ide_reclr").setVisible(true);
		 * tab_establecimiento_obj
		 * .getColumna("ide_reclr").setNombreVisual("Ruta");
		 * tab_establecimiento_obj
		 * .getColumna("notificacion_reest").setVisible(false);
		 */
		tab_establecimiento_obj.setTipoFormulario(true);
		tab_establecimiento_obj.getGrid().setColumns(4);
		tab_establecimiento_obj.dibujar();
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	/**
	 * Metodo para realizar el reporte de clientes CC
	 */

	@Override
	public void aceptarReporte() {
		System.out.print("p_parametros...  " + p_parametros);
		if (rep_reporte.getReporteSelecionado().equals("Clientes Activos e Inactivos")) {
			// if (tab_clientes.getTotalFilas() > 0) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				// p_parametros.put("ide_recli",pckUtilidades.CConversion.CInt("ide_recli"));
				// p_parametros.put("razon_social_recli",tab_clientes.getValor("razon_social_recli"));
				// p_parametros.put("ruc_comercial_recli",tab_clientes.getValor("ruc_comercial_recli"));
				// p_parametros.put("activo_recli",tab_clientes.getValor("ruc_comercial_recli"));
				p_parametros.put("titulo", " EMGIRS GERENCIA DE OPERACIONES - CLIENTES ACTIVOS E INACTIVOS");

				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
			// } else {
			// utilitario.agregarMensajeInfo("No se puede continuar",
			// "No contiene registro de permisos");
			// }
		}

		else if (rep_reporte.getReporteSelecionado().equals("Clientes por Punto de Pago")) {
			// if (tab_clientes.getTotalFilas() > 0) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				// p_parametros.put("ide_recli",pckUtilidades.CConversion.CInt("ide_recli"));
				// p_parametros.put("razon_social_recli",tab_clientes.getValor("razon_social_recli"));
				// p_parametros.put("ruc_comercial_recli",tab_clientes.getValor("ruc_comercial_recli"));
				// p_parametros.put("activo_recli",tab_clientes.getValor("ruc_comercial_recli"));
				p_parametros.put("titulo", " CLIENTES POR PUNTO DE PAGO");

				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
			// } else {
			// utilitario.agregarMensajeInfo("No se puede continuar",
			// "No contiene registro de permisos");
			// }
		}

		else if (rep_reporte.getReporteSelecionado().equals("Clientes con Contratos")) {
			// if (tab_clientes.getTotalFilas() > 0) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				// p_parametros.put("ide_recli",pckUtilidades.CConversion.CInt("ide_recli"));
				// p_parametros.put("razon_social_recli",tab_clientes.getValor("razon_social_recli"));
				// p_parametros.put("ruc_comercial_recli",tab_clientes.getValor("ruc_comercial_recli"));
				// p_parametros.put("activo_recli",tab_clientes.getValor("ruc_comercial_recli"));
				p_parametros.put("titulo", " CONTRATOS DE CLIENTES");

				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
			// } else {
			// utilitario.agregarMensajeInfo("No se puede continuar",
			// "No contiene registro de permisos");
			// }
		}

	}

	/**
	 * Metodo para mostrar el diálogo del Establecimiento 10-05-2017 CC
	 */
	public void mostrarDialogoEstablecimiento() {

		if (tab_clientes.getValor("ide_recli") == null) {
			utilitario.agregarMensajeError("NO SE PUEDE INGRESAR", "Debe seleccionar un cliente");
			return;
		}
		tab_establecimiento_obj.insertar();
		tab_establecimiento_obj.setValor("nombre_comercial_reest", tab_clientes.getValor("nombre_comercial_recli"));
		tab_establecimiento_obj.setValor("ide_bogrm", tab_clientes.getValor("ide_bogrm"));
		tab_establecimiento_obj.setValor("ide_retil", tab_clientes.getValor("ide_retil"));

		dia_establecimiento_cliente.dibujar();
	}

	public Tabla getTab_tarifa() {
		return tab_tarifa;
	}

	public void setTab_tarifa(Tabla tab_tarifa) {
		this.tab_tarifa = tab_tarifa;
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

	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	public Check getChe_todos() {
		return che_todos;
	}

	public void setChe_todos(Check che_todos) {
		this.che_todos = che_todos;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public String getStr_valor() {
		return str_valor;
	}

	public void setStr_valor(String str_valor) {
		this.str_valor = str_valor;
	}

	public ServicioGestion getSer_gestion() {
		return ser_gestion;
	}

	public void setSer_gestion(ServicioGestion ser_gestion) {
		this.ser_gestion = ser_gestion;
	}

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public SeleccionTabla getSet_cliente() {
		return set_cliente;
	}

	public void setSet_cliente(SeleccionTabla set_cliente) {
		this.set_cliente = set_cliente;
	}

	public Tabla getTab_establecimiento() {
		return tab_establecimiento;
	}

	public void setTab_establecimiento(Tabla tab_establecimiento) {
		this.tab_establecimiento = tab_establecimiento;
	}

	public ServicioCliente getSer_cliente() {
		return ser_cliente;
	}

	public void setSer_cliente(ServicioCliente ser_cliente) {
		this.ser_cliente = ser_cliente;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Consulta getCon_ver_establecimiento() {
		return con_ver_establecimiento;
	}

	public void setCon_ver_establecimiento(Consulta con_ver_establecimiento) {
		this.con_ver_establecimiento = con_ver_establecimiento;
	}

	public Tabla getTab_establecimiento_obj() {
		return tab_establecimiento_obj;
	}

	public void setTab_establecimiento_obj(Tabla tab_establecimiento_obj) {
		this.tab_establecimiento_obj = tab_establecimiento_obj;
	}

}
