package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

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
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

public class pre_clientes_nuevos extends Pantalla {

	private Tabla tab_direccion = new Tabla();
	private Tabla tab_telefono = new Tabla();
	private Tabla tab_email = new Tabla();
	private Tabla tab_documento = new Tabla();
	private Tabla tab_tarifa = new Tabla();
	private Tabla tab_clientes = new Tabla();
	private SeleccionTabla set_pantalla_sucursal = new SeleccionTabla();
	private SeleccionTabla set_actualizar = new SeleccionTabla();
	private Confirmar con_guardar = new Confirmar();
	private AutoCompletar aut_factura = new AutoCompletar();
	private Check che_todos = new Check();
	private Division div_division = new Division();
	private String str_valor = "1";
	private SeleccionTabla set_cliente = new SeleccionTabla();

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

	public pre_clientes_nuevos() {

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiarE");
		aut_factura.setId("aut_factura");
		aut_factura.setAutoCompletar(ser_cliente.getDatosTiposServicios());
		aut_factura.setMetodoChange("seleccionoAutocompletar"); // ejecuta el
																// metodo
																// seleccionoAutocompletar

		che_todos.setId("che_todos");
		che_todos.setMetodoChange("seleccionaTodos");
		Etiqueta eti_todos = new Etiqueta("TODOS");
		bar_botones.agregarComponente(eti_todos);
		bar_botones.agregarComponente(che_todos);

		Etiqueta eti_colaborador = new Etiqueta("PUNTO DE VENTA:"); // agrega el
																	// campo del
																	// grupo
																	// material
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
		// ingreso del boton

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_clientes.setId("tab_clientes");
		tab_clientes.setTipoFormulario(true); // formulario
		tab_clientes.getGrid().setColumns(4); // hacer columnas

		tab_clientes.setTabla("rec_clientes", "ide_recli", 1);
		tab_clientes.setCondicion("rec_ide_recli=-1"); // para que no se dibuje
														// antes que seleccione
														// el autocompletar
		tab_clientes.getColumna("ide_retic").setCombo("rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_clientes.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_clientes.getColumna("ide_gtgen").setCombo("gth_genero", "ide_gtgen", "detalle_gtgen", "");
		tab_clientes.getColumna("ide_gedip").setCombo(ser_gestion.getSqlDivisionPoliticaCiudadParroquia());
		tab_clientes.getColumna("ide_gedip").setAutoCompletar();
		tab_clientes.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", " detalle_gttdi", "");
		tab_clientes.getColumna("gth_ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_clientes.getColumna(" ide_gtesc ").setCombo("gth_estado_civil", " ide_gtesc ", "detalle_gtesc", "");
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		// tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaRucRepetido");
		tab_clientes.getColumna("representante_legal_recli"); // .setMetodoChange("validaDocumentoRepre");
		tab_clientes.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_clientes.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_clientes.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_clientes.getColumna("activo_recli").setValorDefecto("true");
		tab_clientes.getColumna("rec_ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_clientes.getColumna("rec_ide_recli").setAutoCompletar();
		tab_clientes.getColumna("rec_ide_recli").setLectura(true);
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

		// RADIOS
		// para contruir los radios
		List lista1 = new ArrayList();
		Object fila3[] = { "1", "MATRIZ" };
		Object fila4[] = { "0", "SUCURSAL" };

		lista1.add(fila3);
		lista1.add(fila4);
		tab_clientes.getColumna("factura_datos_recli").setRadio(lista1, "2");
		tab_clientes.getColumna("factura_datos_recli").setRadioVertical(true);

		tab_clientes.agregarRelacion(tab_direccion);// agraga relacion para los
													// tabuladores
		tab_clientes.agregarRelacion(tab_telefono);
		tab_clientes.agregarRelacion(tab_email);
		tab_clientes.agregarRelacion(tab_tarifa);
		tab_clientes.agregarRelacion(tab_documento);
		// System.out.println("sql pc"+tab_clientes.getSql());

		tab_clientes.dibujar();

		PanelTabla pat_clientes = new PanelTabla();
		pat_clientes.setPanelTabla(tab_clientes);

		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion", "ide_recld", 2);
		tab_direccion.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_direccion);

		tab_telefono.setId("tab_telefono");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("rec_cliente_telefono", "ide_reclt", 3);
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora", "ide_reteo", "detalle_reteo", "");
		tab_telefono.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_telefono);

		tab_email.setId("tab_email");
		tab_email.setIdCompleto("tab_tabulador:tab_email");
		tab_email.setTabla("rec_cliente_email", "ide_recle", 4);
		tab_email.dibujar();
		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_email);

		tab_documento.setId("tab_documento");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTipoFormulario(true);
		tab_documento.getGrid().setColumns(4);
		tab_documento.setTabla("rec_cliente_archivo", "ide_recla", 5);
		tab_documento.getColumna("foto_recla").setUpload("clientes");
		tab_documento.getColumna("activo_recla").setLectura(true);

		tab_documento.dibujar();
		PanelTabla pat_panel5 = new PanelTabla();
		pat_panel5.setPanelTabla(tab_documento);

		tab_tarifa.setId("tab_tarifa");
		tab_tarifa.setIdCompleto("tab_tabulador:tab_tarifa");
		tab_tarifa.setTabla("tes_cliente_tarifa", "ide_teclt", 6);
		tab_tarifa.getColumna("ide_temat").setCombo("select a.ide_temat,detalle_bomat,detalle_tetar,valor_temat from tes_material_tarifa a,bodt_material b,tes_tarifas c where a.ide_bomat = b.ide_bomat and  a.ide_tetar = c.ide_tetar");
		tab_tarifa.dibujar();
		PanelTabla pat_panel6 = new PanelTabla();
		pat_panel6.setPanelTabla(tab_tarifa);

		tab_tabulador.agregarTab("DIRECCION", pat_panel2);// intancia los
															// tabuladores
		tab_tabulador.agregarTab("TELEFONO", pat_panel3);
		tab_tabulador.agregarTab("EMAIL", pat_panel4);
		tab_tabulador.agregarTab("DOCUMENTO", pat_panel5);
		tab_tabulador.agregarTab("TARIFA", pat_panel6);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes, tab_tabulador, "70%", "H");
		agregarComponente(div_division);

		// // ////ingresar una nueva matriz
		// Boton bot_nuevo = new Boton();
		// bot_nuevo.setValue("AGREGAR MATRIZ");
		// bot_nuevo.setIcon("ui-icon-person");
		// bot_nuevo.setMetodo("ingresarMatriz");
		// bar_botones.agregarBoton(bot_nuevo);
		// con_guardar.setId("con_guardar");
		// agregarComponente(con_guardar);
		//
		// set_actualizar.setId("set_actualizar");
		// set_actualizar.setSeleccionTabla(ser_facturacion.getClientes("1"),
		// "ide_recli");
		// set_actualizar.getTab_seleccion().getColumna("nombre_comercial_recli")
		// .setFiltro(true);
		// set_actualizar.getTab_seleccion().getColumna("ruc_comercial_recli")
		// .setFiltro(true);
		// set_actualizar.setRadio();
		// set_actualizar.getBot_aceptar().setMetodo("modificarMatriz");
		// agregarComponente(set_actualizar);

		// BOTON AGREGAR SUCURSAL
		Boton bot_agregarSucursal = new Boton();
		bot_agregarSucursal.setValue("AGREGAR SUCURSAL");
		bot_agregarSucursal.setIcon("ui-icon-person");
		bot_agregarSucursal.setMetodo("ingresarSucursal");
		bar_botones.agregarBoton(bot_agregarSucursal);

		// PANTALLA SELECIONAR CLIENTE
		set_pantalla_sucursal.setId("set_pantalla_sucursal");
		set_pantalla_sucursal.setTitle("SELECCIONE MATRIZ");
		set_pantalla_sucursal.getBot_aceptar().setMetodo("aceptarSucursal");

		set_pantalla_sucursal.setSeleccionTabla(ser_facturacion.getClientes("1"), "ide_recli");
		System.out.println("Devuelve servicio...." + ser_facturacion.getClientes("1"));
		// set_pantalla_sucursal.setSeleccionTabla("rec_clientes", "ide_recli",
		// "nombre_comercial_recli,ruc_comercial_recli");
		//set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantalla_sucursal.setRadio();
		set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantalla_sucursal);

		// actualizar la matriz de la sucursal
		/*
		 * Boton bot_modificar=new Boton();
		 * bot_modificar.setValue("ACTUALIZAR SUCURSAL");
		 * bot_modificar.setIcon("ui-icon-person");
		 * bot_modificar.setMetodo("actualizarSucursal");
		 * bar_botones.agregarBoton(bot_modificar);
		 * con_guardar.setId("con_guardar"); agregarComponente(con_guardar);
		 * 
		 * set_actualizar.setId("set_actualizar");
		 * set_actualizar.setSeleccionTabla
		 * (ser_facturacion.getClientes("1"),"ide_recli");
		 * set_actualizar.getTab_seleccion
		 * ().getColumna("nombre_comercial_recli").setFiltro(true);
		 * set_actualizar
		 * .getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro
		 * (true); set_actualizar.setRadio();
		 * set_actualizar.getBot_aceptar().setMetodo("modificarMatriz");
		 * agregarComponente(set_actualizar);
		 */

		// depurar los clientes Matriz o Sucursales
		Boton bot_limpiarMatriz = new Boton();
		bot_limpiarMatriz.setValue("Depurar Matriz o Sucursal");
		bot_limpiarMatriz.setIcon("ui-icon-person");
		bot_limpiarMatriz.setMetodo("importarEntidad");
		bar_botones.agregarBoton(bot_limpiarMatriz);

		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		String documento_identidad = tab_clientes.getValor("ruc_comercial_recli");
		set_cliente.setId("set_cliente");
		// set_cliente.setSeleccionTabla(ser_cliente.getClientesRuc(documento_identidad),
		// "ide_recli");
		set_cliente.setSeleccionTabla(ser_facturacion.getClientes("1"), "ide_recli");
		set_cliente.getTab_seleccion().getColumna("ruc_comercial_recli");// .setFiltro(true);
		set_cliente.getTab_seleccion().getColumna("razon_social_recli");// .setFiltro(true);
		set_cliente.setTitle("Seleccione los Registros a eliminar");
		set_cliente.getBot_aceptar().setMetodo("depurarEntidad");
		set_cliente.getTab_seleccion().ejecutarSql(); // ingresado 13 abril
														// 11:37
		agregarComponente(set_cliente);

	}

	public void agregarMatriz() {
		// Hace aparecer el componente

		if (tab_clientes.getValor("matriz_sucursal_recli") != null && tab_clientes.getValor("matriz_sucursal_recli").equals("0")) {
			set_pantalla_sucursal.getTab_seleccion().setSql(ser_facturacion.getClientes("1"));
			set_pantalla_sucursal.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
			//set_pantalla_sucursal.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
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
		che_todos.setValue(false);
		// tab_factura_consulta.limpiar();
		utilitario.addUpdate("che_todos");
		utilitario.addUpdate("aut_factura");
	}

	// METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt) {
		// Cuando selecciona una opcion del autocompletar siempre debe hacerse
		// el onSelect(evt)
		/*String str_valor = aut_factura.getValor();

		aut_factura.onSelect(evt);

		tab_clientes.setCondicion("ide_bogrm=" + aut_factura.getValor());
		tab_clientes.ejecutarSql();
		// tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());
		che_todos.setValue(false);
		// che_todos.isRendered();
		if (!aut_factura.getValor().equals("1")) {
			dibujaClienteNoHospi();
		} else {
			dibujaClienteHospi();
		}*/
		
		String str_valor = aut_factura.getValor();
		aut_factura.onSelect(evt);
		tab_clientes.setCondicion(" ide_tipcli=" + aut_factura.getValor());
		tab_clientes.ejecutarSql();
		che_todos.setValue(false);
		// che_todos.isRendered();
		
		  if (!aut_factura.getValor().equals("1")) { 
			  System.out.println("no hospitalarios");
			  dibujaClienteNoHospi(); }
		  else { 
			  System.out.println("presenta los hospitalarios");
			  dibujaClienteHospi(); }
		  utilitario.addUpdate("che_todos");
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
		} else {
			utilitario.agregarMensajeError("Debe seleccionar el Punto de Venta", "");
		}

	}

	public void blanquear() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	public void guardarDatos() {
		// TODO Auto-generated method stub
		if (tab_clientes.guardar()) {
			if (tab_direccion.guardar()) {
				if (tab_telefono.guardar()) {
					if (tab_email.guardar()) {
						if (tab_tarifa.guardar()) {
							tab_documento.guardar();
						}
					}

				}
			}
		}
		guardarPantalla();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {
			boolean boo_ruc = ser_cliente.validarRuc(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"));
			if (!boo_ruc) {
				guardarDatos();
			} else {
				 utilitario.agregarMensajeInfo("No se puede guardar",
				 "Existe un Cliente Registrado con el número de:"+tab_clientes.getValor("ruc_comercial_recli"));
				return;
			}
		} else {
			boolean boo_nro_establecimiento = ser_cliente.validarSucursalEstablecimiento(tab_clientes.getValor("ruc_comercial_recli"), Integer.parseInt(tab_clientes.getValor("nro_establecimiento_recli")));
			if (!boo_nro_establecimiento) {
				guardarDatos();
			} else {
				 utilitario.agregarMensajeInfo("No se puede guardar",
				 "Existe un Establecimiento registrado con el número de:"+tab_clientes.getValor("nro_establecimiento_recli"));
				return;
			}
		}
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
			System.out.println("entre a validar ");
			// método para que realice la validación.
			tab_clientes.setValor("ruc_comercial_recli", "");
			// utilitario.addUpdate("tab_clientes");
		}

	}

	public boolean validarRuc(String ide_gttdi, String documento_identidad) {

		String str_sql = "Select * from rec_clientes where ruc_comercial_recli=" + utilitario.generarComillaSimple(documento_identidad);

		// Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta = utilitario.consultar(str_sql);

		// Preguntamos si la tabla no esta vacia es decir que si retorno un
		// resultado la consulta
		if (tab_consulta.isEmpty() == false) {
			// Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_ruc = tab_consulta.getValor("ruc_comercial_recli");
			utilitario.agregarMensajeInfo("Atencion", "El numero de RUC/C.I. ingresado, ya EXISTE");
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

		//limpiarE();

		if (che_todos.getValue().toString().equalsIgnoreCase("true")) {

			tab_clientes.setCondicion("");
			tab_clientes.ejecutarSql();
			dibujaClienteHospi();
			//utilitario.addUpdate("tab_clientes");
			System.out.println("Todos");
		} else {

			tab_clientes.setCondicion("ide_bogrm=" + aut_factura.getValor());
			tab_clientes.ejecutarSql();
			//che_todos.setOnchange("false");
			//utilitario.addUpdate("tab_clientes");
		}
		
		
		utilitario.addUpdate("tab_clientes");
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
				long max = ser_cliente.getMaxMatriz(str_noseleccionados);
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

	public void dibujaClienteHospi() {

		// tab_clientes.getColumna("ide_retia").setValorDefecto("15"); // valor
		// N/A
		tab_clientes.getColumna("ide_retia").setVisible(true);
		tab_clientes.getColumna("ide_gtgen").setVisible(true);
		tab_clientes.getColumna("ide_gedip").setVisible(true);
		tab_clientes.getColumna("nombre_comercial_recli").setVisible(true);
		// tab_clientes.getColumna("ide_retil").setValorDefecto("614"); // valor
		// por defecto,
		tab_clientes.getColumna("ide_retil").setVisible(true);
		// tab_clientes.getColumna("ide_reclr").setValorDefecto("5"); // valor
		// por defecto N/A
		tab_clientes.getColumna("ide_reclr").setVisible(true);
		// tab_clientes.getColumna("ide_tetar").setValorDefecto("1"); // valor
		// por defecto normal
		tab_clientes.getColumna("ide_tetar").setVisible(true);
		tab_clientes.getColumna("matriz_sucursal_recli").setVisible(true);
		tab_clientes.getColumna("factura_datos_recli").setVisible(true);
		tab_clientes.getColumna("ide_bogrm").setValorDefecto(aut_factura.getValor()); // agrega
																						// el
																						// id
																						// del
																						// grupo
																						// de
																						// material
		tab_clientes.getColumna("nro_contrato_recli").setVisible(true);
		tab_clientes.getColumna("coordx_recli").setVisible(true);
		tab_clientes.getColumna("coordy_recli").setVisible(true);
		tab_clientes.getColumna("estimado_desecho_recl").setVisible(true);
		tab_clientes.getColumna("num_generador_desecho_recli").setVisible(true);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(true);
		tab_clientes.getColumna("establecimiento_operativo_recli").setValorDefecto("1");
		tab_clientes.getColumna("aplica_interes_recli").setVisible(true);
		tab_clientes.getColumna("fecha_emision_generador_recli").setVisible(true);
		tab_clientes.getColumna("fac_mensual_recli").setVisible(true);
		tab_clientes.getColumna("codigo_zona_recli").setVisible(true);
		tab_clientes.getColumna("telefono_factura_recli").setVisible(true);
		tab_clientes.getColumna("ide_tipcli").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setVisible(true);
		tab_clientes.getColumna("estado_recli").setVisible(true);
		tab_clientes.dibujar();
		
		tab_direccion.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_telefono.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_email.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_tarifa.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_documento.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		
		//utilitario.addUpdate("tab_clientes");

		System.out.println("metodo dibuajr cliente hospitalario");
	}

	public void dibujaClienteNoHospi() {

		tab_clientes.getColumna("ide_retia").setValorDefecto(str_ide_retia_asistencia); // valor
																						// N/A
		tab_clientes.getColumna("ide_retia").setVisible(false);
		tab_clientes.getColumna("ide_gtgen").setVisible(false);
		tab_clientes.getColumna("ide_gedip").setVisible(false);
		tab_clientes.getColumna("nombre_comercial_recli").setVisible(false);
		tab_clientes.getColumna("ide_retil").setValorDefecto(str_ide_retil_tipo); // valor
																					// por
																					// defecto,
		tab_clientes.getColumna("ide_retil").setVisible(false);
		tab_clientes.getColumna("ide_reclr").setValorDefecto(str_ide_reclr_ruta); // valor
																					// por
																					// defecto
																					// N/A
		tab_clientes.getColumna("ide_reclr").setVisible(true);
		tab_clientes.getColumna("ide_tetar").setValorDefecto(str_ide_tetar_tarifa); // valor
																					// por
																					// defecto
																					// normal
		tab_clientes.getColumna("ide_tetar").setVisible(false);

		tab_clientes.getColumna("matriz_sucursal_recli").setVisible(true);
		tab_clientes.getColumna("factura_datos_recli").setVisible(false);
		tab_clientes.getColumna("ide_bogrm").setValorDefecto(aut_factura.getValor()); // agrega
																						// el
																						// id
																						// del
																						// grupo
																						// de
																						// material
		tab_clientes.getColumna("nro_contrato_recli").setVisible(false);
		tab_clientes.getColumna("coordx_recli").setVisible(false);
		tab_clientes.getColumna("coordy_recli").setVisible(false);
		tab_clientes.getColumna("estimado_desecho_recl").setVisible(false);
		tab_clientes.getColumna("num_generador_desecho_recli").setVisible(false);
		tab_clientes.getColumna("establecimiento_operativo_recli").setVisible(false);
		tab_clientes.getColumna("establecimiento_operativo_recli").setValorDefecto("1");
		tab_clientes.getColumna("aplica_interes_recli").setVisible(false);
		tab_clientes.getColumna("fecha_emision_generador_recli").setVisible(false);
		tab_clientes.getColumna("codigo_zona_recli").setVisible(false);
		tab_clientes.getColumna("telefono_factura_recli").setVisible(false);
		tab_clientes.getColumna("ide_tipcli").setVisible(false);
		tab_clientes.getColumna("ide_tetar").setVisible(false);
		tab_clientes.getColumna("estado_recli").setVisible(false);

		tab_clientes.dibujar();
		
		tab_direccion.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_telefono.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_email.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_tarifa.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		tab_documento.ejecutarValorForanea(tab_clientes.getValorSeleccionado());
		
		utilitario.addUpdate("tab_clientes");

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

	public ServicioCliente getSer_cliente() {
		return ser_cliente;
	}

	public void setSer_cliente(ServicioCliente ser_cliente) {
		this.ser_cliente = ser_cliente;
	}

}
