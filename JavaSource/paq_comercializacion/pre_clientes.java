package paq_comercializacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_comercializacion.ejb.ServicioClientes;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

public class pre_clientes extends Pantalla {

	private Tabla tab_direccion = new Tabla();
	private Tabla tab_telefono = new Tabla();
	private Tabla tab_email = new Tabla();
	private Tabla tab_documento = new Tabla();
	private Tabla tab_tarifa = new Tabla();
	private Tabla tab_servicio = new Tabla();
	private Tabla tab_clientes = new Tabla();
	private Tabla tab_contratos = new Tabla();
	private Division div_division = new Division();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private Map p_parametros = new HashMap();

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);

	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);

	public pre_clientes() {

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();

		clienteIngreso();
		tabuladores();
		detalleContrato();
		
		PanelTabla pat_clientes = new PanelTabla();
		pat_clientes.setPanelTabla(tab_clientes);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		PanelTabla pat_direccion = new PanelTabla();
		pat_direccion.setPanelTabla(tab_direccion);
		
		PanelTabla pat_telefono = new PanelTabla();
		pat_telefono.setPanelTabla(tab_telefono);
		
		PanelTabla pat_email = new PanelTabla();
		pat_email.setPanelTabla(tab_email);
		
		PanelTabla pat_doc = new PanelTabla();
		pat_doc.setPanelTabla(tab_documento);
		
		PanelTabla pat_tarifa = new PanelTabla();
		pat_tarifa.setPanelTabla(tab_tarifa);
		
		PanelTabla pat_servicio = new PanelTabla();
		pat_servicio.setPanelTabla(tab_servicio);
		
		PanelTabla pat_contrato = new PanelTabla();
		pat_contrato.setPanelTabla(tab_contratos);
		
		tab_tabulador.agregarTab("CONTRATOS", pat_contrato);
		tab_tabulador.agregarTab("DIRECCION", pat_direccion);
		tab_tabulador.agregarTab("TELEFONO", pat_telefono);
		tab_tabulador.agregarTab("EMAIL", pat_email);
		tab_tabulador.agregarTab("DOCUMENTO", pat_doc);
		tab_tabulador.agregarTab("TARIFA", pat_tarifa);
		tab_tabulador.agregarTab("SERVICIOS", pat_servicio);
		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes, tab_tabulador, "60%", "H");
		agregarComponente(div_division);

	}
	
	private void clienteIngreso() 
	{
		tab_clientes.setId("tab_clientes");
		tab_clientes.setTipoFormulario(true);
		tab_clientes.getGrid().setColumns(4);
		tab_clientes.setTabla("rec_clientes", "ide_recli", 1);
		tab_clientes.setCondicion("activo_recli=true");
		tab_clientes.getColumna("activo_recli").setValorDefecto("true");
		
		tab_clientes.getColumna("ide_retic").setCombo("rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_clientes.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_clientes.getColumna("ide_gedip").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
		tab_clientes.getColumna("ide_gedip").setAutoCompletar();
		tab_clientes.getColumna("ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", " detalle_gttdi", "");
		tab_clientes.getColumna("gth_ide_gttdi").setCombo("gth_tipo_documento_identidad", "ide_gttdi", "detalle_gttdi", "");
		tab_clientes.getColumna("ruc_comercial_recli").setMetodoChange("validaDocumento");
		tab_clientes.getColumna("representante_legal_recli"); // .setMetodoChange("validaDocumentoRepre");
		tab_clientes.getColumna("ide_retil").setCombo("rec_tipo_cliente", "ide_retil", "detalle_retil", "");
		tab_clientes.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_clientes.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		//tab_clientes.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_clientes.getColumna("ide_geplf").setCombo("gen_plazo_facturas", "ide_geplf", "dias_geplf", "");

		// para contruir los radios
		List lista1 = new ArrayList();
		Object fila3[] = { "1", "MATRIZ" };
		Object fila4[] = { "0", "SUCURSAL" };
		lista1.add(fila3);
		lista1.add(fila4);
		tab_clientes.getColumna("factura_datos_recli").setRadio(lista1, "1");

		
		tab_clientes.getColumna("factura_datos_recli").setValorDefecto("1");
		tab_clientes.getColumna("NRO_ESTABLECIMIENTO_RECLI").setValorDefecto("1");
		tab_clientes.getColumna("FECHA_INGRESO_RECLI").setValorDefecto(utilitario.getFechaActual());
		tab_clientes.getColumna("ide_tetar").setValorDefecto("1");
		tab_clientes.getColumna("ide_geplf").setValorDefecto("1");
		
		tab_clientes.getColumna("ide_geplf").setNombreVisual("PLAZO");
		tab_clientes.getColumna("ide_tetar").setNombreVisual("TARIFA");
		//tab_clientes.getColumna("ide_retil").setNombreVisual("TIPO ESTABLECIMIENTO:");
		
		
		//tab_clientes.getColumna("FECHA_FACTURA_RECLI").setLectura(true);
		//tab_clientes.getColumna("VALOR_FACTURA_RECLI").setLectura(true);
		//tab_clientes.getColumna("matriz_sucursal_recli").setLectura(true);
		tab_clientes.getColumna("NRO_ESTABLECIMIENTO_RECLI").setLectura(true);
		
		
		tab_clientes.getColumna("IDE_RETIA").setVisible(false);
		tab_clientes.getColumna("ide_retil").setVisible(false);
		tab_clientes.getColumna("IDE_RECLR").setVisible(false);
		tab_clientes.getColumna("CODIGO_ZONA_RECLI").setVisible(false);
		tab_clientes.getColumna("COORDX_RECLI").setVisible(false);
		tab_clientes.getColumna("COORDY_RECLI").setVisible(false);
		tab_clientes.getColumna("ESTIMADO_DESECHO_RECL").setVisible(false);
		tab_clientes.getColumna("NUM_GENERADOR_DESECHO_RECLI").setVisible(false);
		tab_clientes.getColumna("FECHA_EMISION_GENERADOR_RECLI").setVisible(false);
		tab_clientes.getColumna("TELEFONO_FACTURA_RECLI").setVisible(false);
		

		tab_clientes.agregarRelacion(tab_direccion);
		tab_clientes.agregarRelacion(tab_telefono);
		tab_clientes.agregarRelacion(tab_email);
		tab_clientes.agregarRelacion(tab_tarifa);
		tab_clientes.agregarRelacion(tab_servicio);
		tab_clientes.agregarRelacion(tab_documento);

		tab_clientes.agregarRelacion(tab_contratos);
		tab_clientes.dibujar();
		// utilitario.addUpdate("tab_clientes");
	}
	
	public void tabuladores()
	{
		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion", "ide_recld", 2);
		tab_direccion.dibujar();

		tab_telefono.setId("tab_telefono");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("rec_cliente_telefono", "ide_reclt", 3);
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora", "ide_reteo", "detalle_reteo", "");
		tab_telefono.dibujar();

		tab_email.setId("tab_email");
		tab_email.setIdCompleto("tab_tabulador:tab_email");
		tab_email.setTabla("rec_cliente_email", "ide_recle", 4);
		tab_email.dibujar();
		
		tab_documento.setId("tab_documento");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTipoFormulario(true);
		tab_documento.getGrid().setColumns(4);
		tab_documento.setTabla("rec_cliente_archivo", "ide_recla", 5);
		tab_documento.getColumna("foto_recla").setUpload("clientes");
		tab_documento.getColumna("activo_recla").setLectura(true);
		tab_documento.dibujar();
		
		tab_tarifa.setId("tab_tarifa");
		tab_tarifa.setIdCompleto("tab_tabulador:tab_tarifa");
		tab_tarifa.setTabla("tes_cliente_tarifa", "ide_teclt", 6);
		tab_tarifa.getColumna("ide_temat").setCombo("select a.ide_temat,detalle_bomat,detalle_tetar,valor_temat from tes_material_tarifa a,bodt_material b,tes_tarifas c where a.ide_bomat = b.ide_bomat and  a.ide_tetar = c.ide_tetar");
		tab_tarifa.dibujar();
		
		tab_servicio.setId("tab_servicio");
		tab_servicio.setIdCompleto("tab_tabulador:tab_servicio");
		tab_servicio.setTabla("tes_cliente_servicio", "ide_tecls", 7);
		tab_servicio.getColumna("ide_tipcli").setCombo("rec_tipo_cliente_emgirs", "ide_tipcli", "detalle_tipcli", "");
		tab_servicio.dibujar();
		
	}
	
	private void detalleContrato()
	{
		tab_contratos.setId("tab_contratos");
		tab_contratos.setIdCompleto("tab_tabulador:tab_contratos");
		tab_contratos.setTabla("pre_contrato", "ide_prcon", 8);
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
		
		List lista = new ArrayList();
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);*/
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadio(utilitario.getListaTipoContratos(), "0");
		tab_contratos.getColumna("tipo_int_ext_prcon").setOrden(3);
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadioVertical(true);
		tab_contratos.getColumna("tipo_int_ext_prcon").setNombreVisual("Tipo Contrato");
		tab_contratos.getColumna("ide_prtsc").setCombo("pre_tipo_servicio_contrato", "ide_prtsc", "detalle_prtsc", "");
		tab_contratos.getColumna("ide_prtsc").setNombreVisual("Tipo de Servicio");
		tab_contratos.getColumna("ide_prtsc").setOrden(4);
		tab_contratos.getColumna("fecha_firma_prcon");
		tab_contratos.getColumna("fecha_firma_prcon").setNombreVisual("Firma de Contrato");
		tab_contratos.getColumna("fecha_firma_prcon").setOrden(5);
		tab_contratos.getColumna("fecha_firma_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contratos.getColumna("fecha_inicio_prcon").setVisible(false);
		tab_contratos.getColumna("fecha_inicio_prcon").setNombreVisual("Fecha de Inicio");
		tab_contratos.getColumna("fecha_inicio_prcon").setOrden(6);
		tab_contratos.getColumna("fecha_inicio_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contratos.getColumna("OBSERVACION_PRCON").setVisible(false);
		tab_contratos.getColumna("OBSERVACION_PRCON").setNombreVisual("Objeto del Contrato");
		tab_contratos.getColumna("OBSERVACION_PRCON").setOrden(7);

		tab_contratos.getColumna("MONTO_PRCON");
		tab_contratos.getColumna("MONTO_PRCON").setNombreVisual("Monto del Contrato");
		tab_contratos.getColumna("MONTO_PRCON").setOrden(10);
		tab_contratos.getColumna("MONTO_ORIGINAL_PRCON").setVisible(false);
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
		
		tab_contratos.setLectura(true);
		tab_contratos.dibujar();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		/*if (aut_factura.getValor() != null) {
			utilitario.getTablaisFocus().insertar();
			tab_clientes.setValor("ide_bogrm", aut_factura.getValor());
			tab_establecimiento.insertar();
			// tab_establecimiento.setValor("ide_bogrm",
			// aut_factura.getValor());

		} else {
			utilitario.agregarMensajeError("Debe seleccionar el Punto de Venta", "");
		}*/

		utilitario.getTablaisFocus().insertar();
		
	}


	/**
	 * Metodo para guardar el cliente validando el ruc y el número de
	 * establecimiento
	 */

	@Override
	public void guardar() {
		//if (tab_clientes.getValor("matriz_sucursal_recli").equals("1")) {

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
		//}
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

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	
	public void guardarDatos() {
		// TODO Auto-generated method stub
		if (tab_clientes.guardar()) 
		{
			tab_telefono.guardar();
			tab_email.guardar(); 
			tab_tarifa.guardar(); 
			tab_direccion.guardar();
			tab_documento.guardar();
			tab_servicio.guardar();
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

		/*if (validarRuc(tab_clientes.getValor("ide_gttdi"), tab_clientes.getValor("ruc_comercial_recli"))) {
			System.out.println("si hay ruc repetidos");
			// utilitario.agregarMensajeInfo("Atencion",
			// "El numero de RUC /CÉDULA se encuentra REGISTRADO");
		} else {
			System.out.println("no hay ruc repetidos");
		}*/

		validaRucRepetido();
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

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
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

}
