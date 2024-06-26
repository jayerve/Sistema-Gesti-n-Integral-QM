package paq_sumillas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.panelmenu.PanelMenu;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioAnio;
import paq_sumillas.ejb.ServicioDestinatario;
import paq_sumillas.ejb.ServicioGerenciaCoordinacion;
import paq_sumillas.ejb.ServicioPrioridad;
import paq_sumillas.ejb.ServicioSumTramite;
import paq_sumillas.ejb.ServicioSumTramite_bit;
import paq_sumillas.ejb.ServicioSumilla;
import paq_sumillas.ejb.ServicioSumilla_bit;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_revision_respuesta extends Pantalla {

	private AutoCompletar aut_tramite = new AutoCompletar();
	private Tabla tab_sumilla = new Tabla();
	private Tabla tab_registro_respuesta = new Tabla();
	private Tabla tab_reingreso_tramite = new Tabla();
	private Tabla tab_tramite_bit = new Tabla();
	private Tabla tab_tramite_bit1 = new Tabla();
	private Tabla tab_tramite_respuesta_bit = new Tabla();
	private Tabla tab_tramite_respuesta_bit1 = new Tabla();

	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private String str_opcion = "";// sirve para identificar la opcion que se
									// encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	private Combo com_anio = new Combo();

	@EJB
	private ServicioDestinatario ser_nomina = (ServicioDestinatario) utilitario.instanciarEJB(ServicioDestinatario.class);
	@EJB
	private ServicioGerenciaCoordinacion ser_gercoo = (ServicioGerenciaCoordinacion) utilitario.instanciarEJB(ServicioGerenciaCoordinacion.class);
	@EJB
	private ServicioPrioridad ser_prior = (ServicioPrioridad) utilitario.instanciarEJB(ServicioPrioridad.class);
	@EJB
	private ServicioSumilla ser_sumilla = (ServicioSumilla) utilitario.instanciarEJB(ServicioSumilla.class);
	@EJB
	private ServicioAnio ser_anio = (ServicioAnio) utilitario.instanciarEJB(ServicioAnio.class);
	@EJB
	private ServicioSumTramite ser_tramite = (ServicioSumTramite) utilitario.instanciarEJB(ServicioSumTramite.class);
	@EJB
	private ServicioSumTramite_bit sum_tramitebit = (ServicioSumTramite_bit) utilitario.instanciarEJB(ServicioSumTramite_bit.class);
	@EJB
	private ServicioSumilla_bit ser_sumilla_bit = (ServicioSumilla_bit) utilitario.instanciarEJB(ServicioSumilla_bit.class);

	public pre_revision_respuesta() {

		Boton bot_finalizar = new Boton();
		bot_finalizar.setValue("Finalizar");
		bot_finalizar.setMetodo("cambiarEtapa");
		bot_finalizar.setTitle("Finalizar");

		Boton bot_reing = new Boton();
		bot_reing.setValue("Reingreso");
		bot_reing.setMetodo("reingresarTramite");
		bot_reing.setTitle("Reingreso");

		com_anio.setCombo(ser_anio.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarBoton(bot_finalizar);
		bar_botones.agregarBoton(bot_reing);

		tab_sumilla.setId("tab_sumilla");
		tab_sumilla.setHeader("DOCUMENTO DE ENTRADA");
		tab_sumilla.setTabla("sum_tramite", "ide_smtra", 1);

		tab_sumilla.setTipoFormulario(true); //formulario
		tab_sumilla.getGrid().setColumns(6); // hacer columnas
		
		tab_sumilla.getColumna("ide_smtra").setNombreVisual("Código");
		tab_sumilla.getColumna("ide_smtra").setOrden(1);
		
		tab_sumilla.getColumna("doc_respuesta_smtra").setNombreVisual("Documento de Respuesta");
		tab_sumilla.getColumna("doc_respuesta_smtra").setOrden(2);
		tab_sumilla.getColumna("doc_respuesta_smtra").setLectura(true);
		
		tab_sumilla.getColumna("doc_interno_smtra").setNombreVisual("Documento Interno");
		tab_sumilla.getColumna("doc_interno_smtra").setOrden(3);
		tab_sumilla.getColumna("doc_interno_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_sumtd").setNombreVisual("Tipo Documento");
		tab_sumilla.getColumna("ide_sumtd").setOrden(4);
		tab_sumilla.getColumna("ide_sumtd").setEstilo("width:200px");
		tab_sumilla.getColumna("ide_sumtd").setCombo("sum_tipo_documento", "ide_sumtd", "nombre_sumtd", "");
		tab_sumilla.getColumna("ide_sumtd").setLectura(true);
		
		tab_sumilla.getColumna("num_tramite_smtra").setNombreVisual("N° Trámite");
		tab_sumilla.getColumna("num_tramite_smtra").setOrden(5);
		tab_sumilla.getColumna("num_tramite_smtra").setLectura(true);
		
		tab_sumilla.getColumna("num_documento_smtra");
		tab_sumilla.getColumna("num_documento_smtra").setNombreVisual("N° Documento");
		tab_sumilla.getColumna("num_documento_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("num_documento_smtra").setOrden(6);
		tab_sumilla.getColumna("num_documento_smtra").setLectura(true);
		
		tab_sumilla.getColumna("num_hojas_smtra");
		tab_sumilla.getColumna("num_hojas_smtra").setNombreVisual("N° de hojas");
		tab_sumilla.getColumna("num_hojas_smtra").setOrden(7);
		tab_sumilla.getColumna("num_hojas_smtra").setLectura(true);
		
		tab_sumilla.getColumna("doc_referencia_smtra");
		tab_sumilla.getColumna("doc_referencia_smtra").setNombreVisual("Documento de Referencia");
		tab_sumilla.getColumna("doc_referencia_smtra").setOrden(8);
		tab_sumilla.getColumna("doc_referencia_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("doc_referencia_smtra").setLectura(true);
		
		tab_sumilla.getColumna("fecha_emision_doc_smtra");
		tab_sumilla.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha de Emisión del documento");
		tab_sumilla.getColumna("fecha_emision_doc_smtra").setOrden(9);
		tab_sumilla.getColumna("fecha_emision_doc_smtra").setLectura(true);
		
		tab_sumilla.getColumna("fecha_recep_doc_smtra");
		tab_sumilla.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha de Recepción del documento");
		tab_sumilla.getColumna("fecha_recep_doc_smtra").setOrden(10);
		tab_sumilla.getColumna("fecha_recep_doc_smtra").setLectura(true);
		
		tab_sumilla.getColumna("remitente_smtra");
		tab_sumilla.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_sumilla.getColumna("remitente_smtra").setOrden(11);
		tab_sumilla.getColumna("remitente_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("remitente_smtra").setLectura(true);
		
		tab_sumilla.getColumna("destinatario_regdes");
		tab_sumilla.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_sumilla.getColumna("destinatario_regdes").setOrden(12);
		tab_sumilla.getColumna("destinatario_regdes").setEstilo("width:200px");
		tab_sumilla.getColumna("destinatario_regdes").setCombo(ser_nomina.getEmpleadoContrato("true,false"));
		tab_sumilla.getColumna("destinatario_regdes").setMetodoChange("seleccionaElCargo");
		tab_sumilla.getColumna("destinatario_regdes").setLectura(true);
		
		tab_sumilla.getColumna("cargo_destinatario_smtra");
		tab_sumilla.getColumna("cargo_destinatario_smtra").setNombreVisual("Cargo Destinatario");
		tab_sumilla.getColumna("cargo_destinatario_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("cargo_destinatario_smtra").setOrden(13);
		tab_sumilla.getColumna("cargo_destinatario_smtra").setLectura(true);
		
		tab_sumilla.getColumna("empresa_destinatario_smtra");
		tab_sumilla.getColumna("empresa_destinatario_smtra").setNombreVisual("Empresa Destinatario");
		tab_sumilla.getColumna("empresa_destinatario_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("empresa_destinatario_smtra").setOrden(14);
		tab_sumilla.getColumna("empresa_destinatario_smtra").setLectura(true);
		
		tab_sumilla.getColumna("asunto_smtra");
		tab_sumilla.getColumna("asunto_smtra").setNombreVisual("Asunto-Comunicación");
		tab_sumilla.getColumna("asunto_smtra").setOrden(15);
		tab_sumilla.getColumna("asunto_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_suest").setCombo("sum_estado_tramite", "ide_suest", "nombre_suest", "");
		tab_sumilla.getColumna("ide_suest").setValorDefecto("PENDIENTE");
		tab_sumilla.getColumna("ide_suest").setNombreVisual("Estado Trámite");
		tab_sumilla.getColumna("ide_suest").setOrden(16);
		tab_sumilla.getColumna("ide_suest").setLectura(true);
		
		tab_sumilla.getColumna("ide_sumet").setNombreVisual("Etapa");
		tab_sumilla.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));
		if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")){
			tab_sumilla.getColumna("ide_sumet").setLectura(true);
		}
		tab_sumilla.getColumna("ide_sumet").setOrden(17);
		
		tab_sumilla.getColumna("observacion_smtra").setNombreVisual("Observación");
		tab_sumilla.getColumna("observacion_smtra").setOrden(18);
		tab_sumilla.getColumna("observacion_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_geani").setNombreVisual("Año");
		tab_sumilla.getColumna("ide_geani").setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		tab_sumilla.getColumna("ide_geani").setOrden(19);
		tab_sumilla.getColumna("ide_geani").setLectura(true);
		
		tab_sumilla.getColumna("activo_smtra").setNombreVisual("Activo");
		tab_sumilla.getColumna("activo_smtra").setOrden(20);
		
		tab_sumilla.getColumna("adjunto_esttra").setNombreVisual("Documento Adjunto");
		tab_sumilla.getColumna("adjunto_esttra").setUpload("adjuntoTramite");
		tab_sumilla.getColumna("adjunto_esttra").setOrden(21);
		tab_sumilla.getColumna("adjunto_esttra").setLectura(true);
		
		tab_sumilla.setCondicion("ide_geani=-1"); 
		tab_sumilla.getColumna("cod_bpm_esttra").setVisible(false);
		tab_sumilla.getColumna("TRG_ACTUALIZACION_SMTRA").setVisible(false);
		tab_sumilla.getColumna("CARGO_REMITENTE_SMTRA").setVisible(false);
		tab_sumilla.getColumna("EMPRESA_REMITENTE_SMTRA").setVisible(false);

		tab_sumilla.agregarRelacion(tab_registro_respuesta);
		tab_sumilla.dibujar();

		dibujarRespuestas();

		PanelTabla pat_dat_gen = new PanelTabla();
		pat_dat_gen.setPanelTabla(tab_sumilla);
		PanelTabla pat_ing_res = new PanelTabla();
		pat_ing_res.setPanelTabla(tab_registro_respuesta);

		Division div_division = new Division();
		div_division.dividir2(pat_dat_gen, pat_ing_res, "40%", "H");
		agregarComponente(div_division);

	}

	public void dibujarRespuestas() {
		
		tab_registro_respuesta.setId("tab_registro_respuesta");
		tab_registro_respuesta.setHeader("DOCUMENTOS DE RESPUESTA");
		tab_registro_respuesta.setTabla("sum_tramite_respuesta","ide_smtre", 2);

		tab_registro_respuesta.setTipoFormulario(true); // formulario
		tab_registro_respuesta.getGrid().setColumns(4); // hacer columnas
		
		tab_registro_respuesta.getColumna("ide_smtre").setNombreVisual("Código");
		tab_registro_respuesta.getColumna("ide_smtre").setOrden(1);

		tab_registro_respuesta.getColumna("ide_sumet").setNombreVisual("Etapa");
		tab_registro_respuesta.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_registro_respuesta.getColumna("ide_sumet").setOrden(2);
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));
		if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")){
			tab_registro_respuesta.getColumna("ide_sumet").setLectura(true);
		}

		tab_registro_respuesta.getColumna("ide_suesu").setNombreVisual("Estado Sumilla");
		tab_registro_respuesta.getColumna("ide_suesu").setCombo("sum_estado_sumilla", "ide_suesu", "nombre_suesu", "");
		tab_registro_respuesta.getColumna("ide_suesu").setLectura(true);
		tab_registro_respuesta.getColumna("ide_suesu").setOrden(3);

		tab_registro_respuesta.getColumna("disposicion_smtre").setNombreVisual("Disposición");
		tab_registro_respuesta.getColumna("disposicion_smtre").setLectura(true);
		tab_registro_respuesta.getColumna("disposicion_smtre").setOrden(4);

		tab_registro_respuesta.getColumna("fecha_vencimiento_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("dias1_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("fecha_vencimiento1_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("dias2_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("fecha_vencimiento2_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("dias3_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("fecha_vencimiento3_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("dias4_smtre").setVisible(false);

		tab_registro_respuesta.getColumna("fecha_vencimiento4_smtre").setVisible(false);

		tab_registro_respuesta.setCampoForanea("ide_smtra");
		
		tab_registro_respuesta.getColumna("ide_sumtd");
		tab_registro_respuesta.getColumna("ide_sumtd").setNombreVisual("Tipo Documento");
		tab_registro_respuesta.getColumna("ide_sumtd").setOrden(5);
		tab_registro_respuesta.getColumna("ide_sumtd").setCombo("sum_tipo_documento", "ide_sumtd", "nombre_sumtd", "");
		
		tab_registro_respuesta.getColumna("FECHA_DOCUMENTO_SMTRE").setNombreVisual("Fecha Documento");
		tab_registro_respuesta.getColumna("FECHA_DOCUMENTO_SMTRE").setOrden(6);
		
		tab_registro_respuesta.getColumna("NUM_DOCUMENTO_SMTRE").setNombreVisual("Documento Generado");
		tab_registro_respuesta.getColumna("NUM_DOCUMENTO_SMTRE").setOrden(7);
		
		tab_registro_respuesta.getColumna("remitente_smtre").setNombreVisual("Remitente");
		tab_registro_respuesta.getColumna("remitente_smtre").setOrden(8);
		
		tab_registro_respuesta.getColumna("cargo_remitente_smtre").setNombreVisual("Cargo Remitente");
		tab_registro_respuesta.getColumna("cargo_remitente_smtre").setOrden(9);
		
		tab_registro_respuesta.getColumna("empresa_remitente_smtre").setNombreVisual("Empresa Remitente");
		tab_registro_respuesta.getColumna("empresa_remitente_smtre").setOrden(10);
		
		tab_registro_respuesta.getColumna("DESTINATARIO_REGDES").setNombreVisual("Destinatario");
		tab_registro_respuesta.getColumna("DESTINATARIO_REGDES").setOrden(11);
		
		tab_registro_respuesta.getColumna("CARGO_DESTINATARIO_SMTRE").setNombreVisual("Cargo Destinatario");
		tab_registro_respuesta.getColumna("CARGO_DESTINATARIO_SMTRE").setOrden(12);
		tab_registro_respuesta.getColumna("EMPRESA_DESTINATARIO_SMTRE").setNombreVisual("Empresa Destinatario");
		tab_registro_respuesta.getColumna("EMPRESA_DESTINATARIO_SMTRE").setOrden(13);
		
		tab_registro_respuesta.getColumna("ASUNTO_SMTRE").setNombreVisual("Asunto");
		tab_registro_respuesta.getColumna("ASUNTO_SMTRE").setOrden(14);
		
		tab_registro_respuesta.getColumna("FECHA_ENTREGA_RES_SMTRE").setNombreVisual("Fecha Entrega Doc. Respuesta");
		tab_registro_respuesta.getColumna("FECHA_ENTREGA_SMTRE").setOrden(15);

		tab_registro_respuesta.getColumna("GABINETE_SMTRE").setNombreVisual("Observación Respuesta");
		tab_registro_respuesta.getColumna("GABINETE_SMTRE").setOrden(16);
		
		tab_registro_respuesta.getColumna("OBSERVACION_REVRES_SMTRE").setNombreVisual("Observación Revisión");
		tab_registro_respuesta.getColumna("OBSERVACION_REVRES_SMTRE").setOrden(17);
		
		tab_registro_respuesta.getColumna("adjunto_smtre").setNombreVisual("Adjunto Sumilla");
		tab_registro_respuesta.getColumna("adjunto_smtre").setUpload("documento");
		tab_registro_respuesta.getColumna("adjunto_smtre").setLectura(true);
		tab_registro_respuesta.getColumna("adjunto_smtre").setOrden(18);
		
		tab_registro_respuesta.getColumna("adjunto_res_smtre").setNombreVisual("Adjunto Respuesta");
		tab_registro_respuesta.getColumna("adjunto_res_smtre").setUpload("documento");
		tab_registro_respuesta.getColumna("adjunto_res_smtre").setOrden(19);
		
		tab_registro_respuesta.getColumna("FECHA_LIMITE_SMTRE").setNombreVisual("Fecha Límite");;
		tab_registro_respuesta.getColumna("FECHA_LIMITE_SMTRE").setOrden(20);

		// Campos ocultos de sumilla
		tab_registro_respuesta.getColumna("fecha_sumilla_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("ide_sumpr").setVisible(false);
		tab_registro_respuesta.getColumna("ide_geareg").setVisible(false);
		tab_registro_respuesta.getColumna("ide_gearec").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_entrega_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("recibido_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("cod_bpm_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("observacion1_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("observacion2_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("observacion3_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("observacion4_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_calculo1_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_calculo2_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_calculo3_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_calculo4_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("TRG_ACTUALIZACION_SMTRE").setVisible(false);
		tab_registro_respuesta.getColumna("fecha_limite_prog_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("activo_smtre").setVisible(false);
		tab_registro_respuesta.getColumna("OBSERVACION_SUM_SMTRE").setVisible(false);
		tab_registro_respuesta.getColumna("envio_mail_smtre").setVisible(false);
		
		tab_registro_respuesta.setCampoForanea("ide_smtra");
		tab_registro_respuesta.setScrollable(true);
		tab_registro_respuesta.dibujar();
	}

	/**
	 * Cambia el registro a etapa RESPUESTA
	 */
	public void cambiarEtapa() {

		if (tab_registro_respuesta.getValor("ide_sumet").equals("FINAL") || tab_registro_respuesta.getValor("ide_sumet").equals("4")) {

			tab_registro_respuesta.setValor("ide_suesu", "2");
			tab_registro_respuesta.modificar(tab_registro_respuesta.getFilaActual());
			utilitario.addUpdate("tab_registro_respuesta");
			tab_registro_respuesta.guardar();
			guardarPantalla();
			utilitario.agregarMensaje("Se cambio el estado de la Sumilla a CUMPLIDO", "");

			// validación para saber si ya existe un registro con el id de sumilla y el estado Respuesta no ingresar
			Integer padreSum = Integer.parseInt(tab_registro_respuesta.getValor("ide_smtre"));
			Integer etapaSum = 4;
			Integer estadoSum = 2;
			List listEtapaResSum = utilitario.getConexion().consultar(ser_sumilla_bit.getTramiteRespuestaBitPorTramiteRespuestaYEtapaYEstado(padreSum, etapaSum, estadoSum));
			Integer auxRegSum = listEtapaResSum.size();
			if (auxRegSum <= 0) {
				// insertar sumilla con etapa Respuesta a la bitacora
				generarTramiteRespuesta();
			}

			// actualizar tramite a "Archivado"
			Integer padre = Integer.parseInt(tab_sumilla.getValor("ide_smtra"));
			Integer estado = 2;
			List listEtapa = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPadreYEstadoSumilla(padre,estado));
			Integer le = listEtapa.size();
			List listHija = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardre(padre));
			Integer lh = listHija.size();
			if (le.equals(lh)) {
				utilitario.agregarMensaje("Se cambio el estado del Trámite a ARCHIVADO", "");
				tab_sumilla.setValor("ide_suest", "3");
				tab_sumilla.modificar(tab_sumilla.getFilaActual());
				utilitario.addUpdate("tab_sumilla");
				tab_sumilla.guardar();
				guardarPantalla();
				// validar que ya se haya se haya generado el registro con
				// estado CUMPLIDO
				Integer est = 3;
				Integer etap = 4;
				List listEstadoCumplido = utilitario.getConexion().consultar(sum_tramitebit.getTramiteBitPorTramiteYEtapaYEstado(padre, etap, est));
				Integer auxReg = listEstadoCumplido.size();
				if (auxReg <= 0) {
					// insertar tramite bit cuanso todos los hijos hayan cambiado a FINAL
					generarTramite();
				}
			}

		} else {
			utilitario.agregarMensaje("No es posible el cambio de Etapa la etapa actual no es FiNAL","");
		}
	}

	/**
	 * Formato de fecha actual
	 */
	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
		return formateador.format(ahora);
	}
	
	/**
	 * Reingreso de tramite
	 */
	public void reingresarTramite() {
		//validar que se pueda reingresar solo una vez
		if (tab_registro_respuesta.getValor("fecha_limite_prog_smtre") != null) {
			//bot_reing.setReadonly(true);
			utilitario.agregarMensaje("Ya se reingreso este tramite, no es posible un nuevo Reingreso", " ");
		} else {
		
			if (tab_registro_respuesta.getValor("ide_sumet").equals("FINAL") || tab_registro_respuesta.getValor("ide_sumet").equals("4")) {
				tab_registro_respuesta.setValor("fecha_limite_prog_smtre",getFechaActual());
				tab_registro_respuesta.setValor("ide_suesu", "2");
				tab_registro_respuesta.modificar(tab_registro_respuesta.getFilaActual());
				utilitario.addUpdate("tab_registro_respuesta");
				tab_registro_respuesta.guardar();
				guardarPantalla();
				utilitario.agregarMensaje("Se cambio el estado de la Sumilla a CUMPLIDO", "");
	
				// validación para saber si ya existe un registro con el id de sumilla y el estado Respuesta no ingresar
				Integer padreSum = Integer.parseInt(tab_registro_respuesta.getValor("ide_smtre"));
				Integer etapaSum = 4;
				Integer estadoSum = 3;
				List listEtapaResSum = utilitario.getConexion().consultar(ser_sumilla_bit.getTramiteRespuestaBitPorTramiteRespuestaYEtapaYEstado(padreSum, etapaSum, estadoSum));
				Integer auxRegSum = listEtapaResSum.size();
				if (auxRegSum <= 0) {
					// insertar sumilla con etapa Respuesta a la bitacora
					generarTramiteRespuesta1();
				}
	
				// actualizar tramite a "Archivado"
				Integer padre = Integer.parseInt(tab_sumilla.getValor("ide_smtra"));
				Integer estado = 2;
				List listEtapa = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPadreYEstadoSumilla(padre,estado));
				Integer le = listEtapa.size();
				List listHija = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardre(padre));
				Integer lh = listHija.size();
				if (le.equals(lh)) {
					utilitario.agregarMensaje("Se cambio el estado del Trámite a ARCHIVADO", "");
					tab_sumilla.setValor("ide_suest", "3");
					tab_sumilla.modificar(tab_sumilla.getFilaActual());
					utilitario.addUpdate("tab_sumilla");
					tab_sumilla.guardar();
					guardarPantalla();
					// validar que ya se haya se haya generado el registro con estado CUMPLIDO
					Integer est = 3;
					Integer etap = 4;
					List listEstadoCumplido = utilitario.getConexion().consultar(sum_tramitebit.getTramiteBitPorTramiteYEtapaYEstado(padre, etap, est));
					Integer auxReg = listEstadoCumplido.size();
					if (auxReg <= 0) {
						// insertar tramite bit cuanso todos los hijos hayan ambiado a FINAL
						generarTramite1();
					}
				}
	
				tab_reingreso_tramite.setId("tab_reingreso_tramite");
				tab_reingreso_tramite.setTabla("sum_tramite", "ide_smtra",3);
				TablaGenerica tab_tramiteB = ser_tramite.getTramitePorId(Integer.parseInt(tab_sumilla.getValor("ide_smtra")));
	
				for (int i = 0; i < tab_tramiteB.getTotalFilas(); i++) {
					tab_reingreso_tramite.insertar();
					tab_reingreso_tramite.setValor("ide_smtra",tab_tramiteB.getValor(i, "ide_smtra"));
					tab_reingreso_tramite.setValor("doc_respuesta_smtra",tab_tramiteB.getValor(i, "doc_respuesta_smtra"));
					tab_reingreso_tramite.setValor("doc_interno_smtra",tab_tramiteB.getValor(i, "doc_interno_smtra"));
					tab_reingreso_tramite.setValor("doc_referencia_smtra",tab_tramiteB.getValor(i, "num_documento_smtra"));
					tab_reingreso_tramite.setValor("num_tramite_smtra",tab_tramiteB.getValor(i, "num_tramite_smtra"));
					tab_reingreso_tramite.setValor("num_documento_smtra",tab_tramiteB.getValor(i, "num_documento_smtra"));
					tab_reingreso_tramite.setValor("num_hojas_smtra",tab_tramiteB.getValor(i, "num_hojas_smtra"));
					tab_reingreso_tramite.setValor("fecha_emision_doc_smtra",tab_tramiteB.getValor(i, "fecha_emision_doc_smtra"));
					tab_reingreso_tramite.setValor("fecha_recep_doc_smtra",tab_tramiteB.getValor(i, "fecha_recep_doc_smtra"));
					tab_reingreso_tramite.setValor("remitente_smtra",tab_tramiteB.getValor(i, "remitente_smtra"));
					tab_reingreso_tramite.setValor("destinatario_regdes",tab_tramiteB.getValor(i, "destinatario_regdes"));
					tab_reingreso_tramite.setValor("cargo_destinatario_smtra",tab_tramiteB.getValor(i, "cargo_destinatario_smtra"));
					tab_reingreso_tramite.setValor("empresa_destinatario_smtra",tab_tramiteB.getValor(i, "empresa_destinatario_smtra"));
					tab_reingreso_tramite.setValor("asunto_smtra",tab_tramiteB.getValor(i, "asunto_smtra"));
					tab_reingreso_tramite.setValor("activo_smtra",tab_tramiteB.getValor(i, "activo_smtra"));
					tab_reingreso_tramite.setValor("adjunto_esttra",tab_tramiteB.getValor(i, "adjunto_esttra"));
					tab_reingreso_tramite.setValor("cod_bpm_esttra",tab_tramiteB.getValor(i, "cod_bpm_esttra"));
					tab_reingreso_tramite.setValor("ide_suest", "1");
					tab_reingreso_tramite.setValor("ide_geani", com_anio.getValue().toString());
					tab_reingreso_tramite.setValor("ide_sumet", "1");
	
					tab_reingreso_tramite.modificar(tab_reingreso_tramite.getFilaActual());
					tab_reingreso_tramite.guardar();
					guardarPantalla();
					utilitario.agregarMensaje("Se ha realizado el ingreso del trámite", "");
				}
			} else {
				utilitario.agregarMensaje("No es posible el cambio de Etapa la etapa actual no es FiNAL","");
			}
		}
	}

	/**
	 * Método para generar la bitácora de trámite
	 */
	public void generarTramite() {

		tab_tramite_bit.setId("tab_tramite_bit");
		tab_tramite_bit.setTabla("sum_tramite_bit", "ide_smtra_bit", 1);
		
		TablaGenerica tab_tramiteB = ser_tramite.getTramitePorId(Integer.parseInt(tab_sumilla.getValor("ide_smtra")));
		
		for (int i = 0; i < tab_tramiteB.getTotalFilas(); i++) {
			tab_tramite_bit.insertar();
			tab_tramite_bit.setValor("ide_smtra", tab_tramiteB.getValor(i, "ide_smtra"));
			tab_tramite_bit.setValor("doc_respuesta_smtra", tab_tramiteB.getValor(i, "doc_respuesta_smtra"));
			tab_tramite_bit.setValor("doc_interno_smtra", tab_tramiteB.getValor(i, "doc_interno_smtra"));
			tab_tramite_bit.setValor("doc_referencia_smtra", tab_tramiteB.getValor(i, "doc_referencia_smtra"));
			tab_tramite_bit.setValor("num_tramite_smtra", tab_tramiteB.getValor(i, "num_tramite_smtra"));
			tab_tramite_bit.setValor("num_documento_smtra", tab_tramiteB.getValor(i, "num_documento_smtra"));
			tab_tramite_bit.setValor("num_hojas_smtra", tab_tramiteB.getValor(i, "num_hojas_smtra"));
			tab_tramite_bit.setValor("fecha_emision_doc_smtra", tab_tramiteB.getValor(i, "fecha_emision_doc_smtra"));
			tab_tramite_bit.setValor("fecha_recep_doc_smtra", tab_tramiteB.getValor(i, "fecha_recep_doc_smtra"));
			tab_tramite_bit.setValor("remitente_smtra", tab_tramiteB.getValor(i, "remitente_smtra"));
			tab_tramite_bit.setValor("destinatario_regdes", tab_tramiteB.getValor(i, "destinatario_regdes"));
			tab_tramite_bit.setValor("cargo_destinatario_smtra", tab_tramiteB.getValor(i, "cargo_destinatario_smtra"));
			tab_tramite_bit.setValor("empresa_destinatario_smtra", tab_tramiteB.getValor(i, "empresa_destinatario_smtra"));
			tab_tramite_bit.setValor("asunto_smtra", tab_tramiteB.getValor(i, "asunto_smtra"));
			tab_tramite_bit.setValor("activo_smtra", tab_tramiteB.getValor(i, "activo_smtra"));
			tab_tramite_bit.setValor("adjunto_esttra", tab_tramiteB.getValor(i, "adjunto_esttra"));
			tab_tramite_bit.setValor("cod_bpm_esttra", tab_tramiteB.getValor(i, "cod_bpm_esttra"));
			tab_tramite_bit.setValor("ide_sumet", tab_tramiteB.getValor(i, "ide_sumet"));
			tab_tramite_bit.setValor("ide_suest", tab_tramiteB.getValor(i, "ide_suest"));
			
			tab_tramite_bit.modificar(tab_tramite_bit.getFilaActual());
			utilitario.addUpdate("tab_tramite_bit");
			tab_tramite_bit.guardar();
			guardarPantalla();
		}
	}

	/**
	 * Método para generar la bitácora de trámite
	 */
	public void generarTramite1() {

		tab_tramite_bit1.setId("tab_tramite_bit1");
		tab_tramite_bit1.setTabla("sum_tramite_bit", "ide_smtra_bit", 1);
		
		TablaGenerica tab_tramiteB = ser_tramite.getTramitePorId(Integer.parseInt(tab_sumilla.getValor("ide_smtra")));
		
		for (int i = 0; i < tab_tramiteB.getTotalFilas(); i++) {
			tab_tramite_bit1.insertar();
			tab_tramite_bit1.setValor("ide_smtra", tab_tramiteB.getValor(i, "ide_smtra"));
			tab_tramite_bit1.setValor("doc_respuesta_smtra", tab_tramiteB.getValor(i, "doc_respuesta_smtra"));
			tab_tramite_bit1.setValor("doc_interno_smtra", tab_tramiteB.getValor(i, "doc_interno_smtra"));
			tab_tramite_bit1.setValor("doc_referencia_smtra", tab_tramiteB.getValor(i, "doc_referencia_smtra"));
			tab_tramite_bit1.setValor("num_tramite_smtra", tab_tramiteB.getValor(i, "num_tramite_smtra"));
			tab_tramite_bit1.setValor("num_documento_smtra", tab_tramiteB.getValor(i, "num_documento_smtra"));
			tab_tramite_bit1.setValor("num_hojas_smtra", tab_tramiteB.getValor(i, "num_hojas_smtra"));
			tab_tramite_bit1.setValor("fecha_emision_doc_smtra", tab_tramiteB.getValor(i, "fecha_emision_doc_smtra"));
			tab_tramite_bit1.setValor("fecha_recep_doc_smtra", tab_tramiteB.getValor(i, "fecha_recep_doc_smtra"));
			tab_tramite_bit1.setValor("remitente_smtra", tab_tramiteB.getValor(i, "remitente_smtra"));
			tab_tramite_bit1.setValor("destinatario_regdes", tab_tramiteB.getValor(i, "destinatario_regdes"));
			tab_tramite_bit1.setValor("cargo_destinatario_smtra", tab_tramiteB.getValor(i, "cargo_destinatario_smtra"));
			tab_tramite_bit1.setValor("empresa_destinatario_smtra", tab_tramiteB.getValor(i, "empresa_destinatario_smtra"));
			tab_tramite_bit1.setValor("asunto_smtra", tab_tramiteB.getValor(i, "asunto_smtra"));
			tab_tramite_bit1.setValor("activo_smtra", tab_tramiteB.getValor(i, "activo_smtra"));
			tab_tramite_bit1.setValor("adjunto_esttra", tab_tramiteB.getValor(i, "adjunto_esttra"));
			tab_tramite_bit1.setValor("cod_bpm_esttra", tab_tramiteB.getValor(i, "cod_bpm_esttra"));
			tab_tramite_bit1.setValor("ide_sumet", tab_tramiteB.getValor(i, "ide_sumet"));
			tab_tramite_bit1.setValor("ide_suest", tab_tramiteB.getValor(i, "ide_suest"));
			
			tab_tramite_bit1.modificar(tab_tramite_bit1.getFilaActual());
			utilitario.addUpdate("tab_tramite_bit1");
			tab_tramite_bit1.guardar();
			guardarPantalla();
		}
	}

	/**
	 * Método para generar la bitácora de trámite_respuesta
	 */
	public void generarTramiteRespuesta() {
		tab_tramite_respuesta_bit.setId("tab_tramite_respuesta_bit");
		tab_tramite_respuesta_bit.setTabla("sum_tramite_respuesta_bit", "ide_smtre_bit", 1);
		
		TablaGenerica tab_tramiteResB = ser_sumilla.getTramiteRespuestaPorId(Integer.parseInt(tab_registro_respuesta.getValor("ide_smtre")));
		
		for (int i = 0; i < tab_tramiteResB.getTotalFilas(); i++) {
			tab_tramite_respuesta_bit.insertar();
			tab_tramite_respuesta_bit.setValor("ide_smtre", tab_tramiteResB.getValor(i, "ide_smtre"));
			tab_tramite_respuesta_bit.setValor("ide_smtra", tab_tramiteResB.getValor(i, "ide_smtra"));
			tab_tramite_respuesta_bit.setValor("fecha_sumilla_smtre", tab_tramiteResB.getValor(i, "fecha_sumilla_smtre"));
			tab_tramite_respuesta_bit.setValor("disposicion_smtre", tab_tramiteResB.getValor(i, "disposicion_smtre"));
			tab_tramite_respuesta_bit.setValor("ide_geareg", tab_tramiteResB.getValor(i, "ide_geareg"));
			tab_tramite_respuesta_bit.setValor("ide_gearec", tab_tramiteResB.getValor(i, "ide_gearec"));
			tab_tramite_respuesta_bit.setValor("recibido_smtre", tab_tramiteResB.getValor(i, "recibido_smtre"));
			tab_tramite_respuesta_bit.setValor("ide_suesu", tab_tramiteResB.getValor(i, "ide_suesu"));
			tab_tramite_respuesta_bit.setValor("ide_sumpr", tab_tramiteResB.getValor(i, "ide_sumpr"));
			tab_tramite_respuesta_bit.setValor("fecha_entrega_smtre", tab_tramiteResB.getValor(i, "fecha_entrega_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_vencimiento_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento_smtre"));
			tab_tramite_respuesta_bit.setValor("adjunto_smtre", tab_tramiteResB.getValor(i, "adjunto_smtre"));
			tab_tramite_respuesta_bit.setValor("cod_bpm_smtre", tab_tramiteResB.getValor(i, "cod_bpm_smtre"));
			tab_tramite_respuesta_bit.setValor("ide_sumet", tab_tramiteResB.getValor(i, "ide_sumet"));
			tab_tramite_respuesta_bit.setValor("dias1_smtre", tab_tramiteResB.getValor(i, "dias1_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_vencimiento1_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento1_smtre"));
			tab_tramite_respuesta_bit.setValor("observacion1_smtre", tab_tramiteResB.getValor(i, "observacion1_smtre"));
			tab_tramite_respuesta_bit.setValor("dias2_smtre", tab_tramiteResB.getValor(i, "dias2_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_vencimiento2_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento2_smtre"));
			tab_tramite_respuesta_bit.setValor("observacion2_smtre", tab_tramiteResB.getValor(i, "observacion2_smtre"));
			tab_tramite_respuesta_bit.setValor("dias3_smtre", tab_tramiteResB.getValor(i, "dias3_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_vencimiento3_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento3_smtre"));
			tab_tramite_respuesta_bit.setValor("observacion3_smtre", tab_tramiteResB.getValor(i, "observacion3_smtre"));
			tab_tramite_respuesta_bit.setValor("dias4_smtre", tab_tramiteResB.getValor(i, "dias4_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_vencimiento4_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento4_smtre"));
			tab_tramite_respuesta_bit.setValor("observacion4_smtre", tab_tramiteResB.getValor(i, "observacion4_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_documento_smtre", tab_tramiteResB.getValor(i, "fecha_documento_smtre"));
			tab_tramite_respuesta_bit.setValor("num_documento_smtre", tab_tramiteResB.getValor(i, "num_documento_smtre"));
			tab_tramite_respuesta_bit.setValor("destinatario_regdes", tab_tramiteResB.getValor(i, "destinatario_regdes"));
			tab_tramite_respuesta_bit.setValor("cargo_destinatario_smtre", tab_tramiteResB.getValor(i, "cargo_destinatario_smtre"));
			tab_tramite_respuesta_bit.setValor("empresa_destinatario_smtre", tab_tramiteResB.getValor(i, "empresa_destinatario_smtre"));
			tab_tramite_respuesta_bit.setValor("asunto_smtre", tab_tramiteResB.getValor(i, "asunto_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_entrega_res_smtre", tab_tramiteResB.getValor(i, "fecha_entrega_res_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_limite_smtre", tab_tramiteResB.getValor(i, "fecha_limite_smtre"));
			tab_tramite_respuesta_bit.setValor("fecha_limite_prog_smtre", tab_tramiteResB.getValor(i, "fecha_limite_prog_smtre"));
			tab_tramite_respuesta_bit.setValor("gabinete_smtre", tab_tramiteResB.getValor(i, "gabinete_smtre"));
			tab_tramite_respuesta_bit.setValor("adjunto_res_smtre", tab_tramiteResB.getValor(i, "adjunto_res_smtre"));
						
			tab_tramite_respuesta_bit.modificar(tab_tramite_respuesta_bit.getFilaActual());
			utilitario.addUpdate("tab_tramite_respuesta_bit");
			tab_tramite_respuesta_bit.guardar();
			guardarPantalla();
		}
	}

	/**
	 * Método para generar la bitácora de trámite_respuesta
	 */
	public void generarTramiteRespuesta1() {
		tab_tramite_respuesta_bit1.setId("tab_tramite_respuesta_bit1");
		tab_tramite_respuesta_bit1.setTabla("sum_tramite_respuesta_bit", "ide_smtre_bit", 1);
		
		TablaGenerica tab_tramiteResB = ser_sumilla.getTramiteRespuestaPorId(Integer.parseInt(tab_registro_respuesta.getValor("ide_smtre")));
		
		for (int i = 0; i < tab_tramiteResB.getTotalFilas(); i++) {
			tab_tramite_respuesta_bit1.insertar();
			tab_tramite_respuesta_bit1.setValor("ide_smtre", tab_tramiteResB.getValor(i, "ide_smtre"));
			tab_tramite_respuesta_bit1.setValor("ide_smtra", tab_tramiteResB.getValor(i, "ide_smtra"));
			tab_tramite_respuesta_bit1.setValor("fecha_sumilla_smtre", tab_tramiteResB.getValor(i, "fecha_sumilla_smtre"));
			tab_tramite_respuesta_bit1.setValor("disposicion_smtre", tab_tramiteResB.getValor(i, "disposicion_smtre"));
			tab_tramite_respuesta_bit1.setValor("ide_geareg", tab_tramiteResB.getValor(i, "ide_geareg"));
			tab_tramite_respuesta_bit1.setValor("ide_gearec", tab_tramiteResB.getValor(i, "ide_gearec"));
			tab_tramite_respuesta_bit1.setValor("recibido_smtre", tab_tramiteResB.getValor(i, "recibido_smtre"));
			tab_tramite_respuesta_bit1.setValor("ide_suesu", tab_tramiteResB.getValor(i, "ide_suesu"));
			tab_tramite_respuesta_bit1.setValor("ide_sumpr", tab_tramiteResB.getValor(i, "ide_sumpr"));
			tab_tramite_respuesta_bit1.setValor("fecha_entrega_smtre", tab_tramiteResB.getValor(i, "fecha_entrega_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_vencimiento_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento_smtre"));
			tab_tramite_respuesta_bit1.setValor("adjunto_smtre", tab_tramiteResB.getValor(i, "adjunto_smtre"));
			tab_tramite_respuesta_bit1.setValor("cod_bpm_smtre", tab_tramiteResB.getValor(i, "cod_bpm_smtre"));
			tab_tramite_respuesta_bit1.setValor("ide_sumet", tab_tramiteResB.getValor(i, "ide_sumet"));
			tab_tramite_respuesta_bit1.setValor("dias1_smtre", tab_tramiteResB.getValor(i, "dias1_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_vencimiento1_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento1_smtre"));
			tab_tramite_respuesta_bit1.setValor("observacion1_smtre", tab_tramiteResB.getValor(i, "observacion1_smtre"));
			tab_tramite_respuesta_bit1.setValor("dias2_smtre", tab_tramiteResB.getValor(i, "dias2_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_vencimiento2_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento2_smtre"));
			tab_tramite_respuesta_bit1.setValor("observacion2_smtre", tab_tramiteResB.getValor(i, "observacion2_smtre"));
			tab_tramite_respuesta_bit1.setValor("dias3_smtre", tab_tramiteResB.getValor(i, "dias3_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_vencimiento3_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento3_smtre"));
			tab_tramite_respuesta_bit1.setValor("observacion3_smtre", tab_tramiteResB.getValor(i, "observacion3_smtre"));
			tab_tramite_respuesta_bit1.setValor("dias4_smtre", tab_tramiteResB.getValor(i, "dias4_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_vencimiento4_smtre", tab_tramiteResB.getValor(i, "fecha_vencimiento4_smtre"));
			tab_tramite_respuesta_bit1.setValor("observacion4_smtre", tab_tramiteResB.getValor(i, "observacion4_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_documento_smtre", tab_tramiteResB.getValor(i, "fecha_documento_smtre"));
			tab_tramite_respuesta_bit1.setValor("num_documento_smtre", tab_tramiteResB.getValor(i, "num_documento_smtre"));
			tab_tramite_respuesta_bit1.setValor("destinatario_regdes", tab_tramiteResB.getValor(i, "destinatario_regdes"));
			tab_tramite_respuesta_bit1.setValor("cargo_destinatario_smtre", tab_tramiteResB.getValor(i, "cargo_destinatario_smtre"));
			tab_tramite_respuesta_bit1.setValor("empresa_destinatario_smtre", tab_tramiteResB.getValor(i, "empresa_destinatario_smtre"));
			tab_tramite_respuesta_bit1.setValor("asunto_smtre", tab_tramiteResB.getValor(i, "asunto_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_entrega_res_smtre", tab_tramiteResB.getValor(i, "fecha_entrega_res_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_limite_smtre", tab_tramiteResB.getValor(i, "fecha_limite_smtre"));
			tab_tramite_respuesta_bit1.setValor("fecha_limite_prog_smtre", tab_tramiteResB.getValor(i, "fecha_limite_prog_smtre"));
			tab_tramite_respuesta_bit1.setValor("gabinete_smtre", tab_tramiteResB.getValor(i, "gabinete_smtre"));
			tab_tramite_respuesta_bit1.setValor("adjunto_res_smtre", tab_tramiteResB.getValor(i, "adjunto_res_smtre"));
						
			tab_tramite_respuesta_bit1.modificar(tab_tramite_respuesta_bit1.getFilaActual());
			utilitario.addUpdate("tab_tramite_respuesta_bit1");
			tab_tramite_respuesta_bit1.guardar();
			guardarPantalla();
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		// verificar solo se puede insertar mientras exista sumillas en etapa SUMILLA
		Integer padre = Integer.parseInt(tab_sumilla.getValor("ide_smtra"));
		Integer etapa = 2;
		List listEtapa = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardreYEtapa(padre, etapa));
		List listhija = utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardre(padre));
		if (listEtapa.size() > 0 || listhija.size() <= 0) {
			// solo insertar en datos de detalle Respuesta
			if (tab_registro_respuesta.isFocus()) {
				//utilitario.getTablaisFocus().insertar();
				utilitario.agregarMensajeInfo("No se puede insertar datos ", "");
			}
		} else {
			utilitario.agregarMensajeInfo("No se puede insertar, todas los datos superaron la etapa de SUMILLA","");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_sumilla.guardar()) {
			guardarPantalla();
		}
		if (tab_registro_respuesta.guardar()) {
			guardarPantalla();
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		//utilitario.getTablaisFocus().eliminar();
		utilitario.agregarMensajeInfo("No se puede eliminar datos ", "");
	}

	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			List perfilUsuarioConectado = utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));

			if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")) {
				tab_sumilla.setCondicion("ide_sumet = 4 and "+ "ide_geani=" + com_anio.getValue());
				tab_sumilla.ejecutarSql();
				tab_registro_respuesta.ejecutarSql();
			}else {
				tab_sumilla.setCondicion("ide_geani=" + com_anio.getValue());
				tab_sumilla.ejecutarSql();
				tab_registro_respuesta.ejecutarSql();
			}
		} else {
			tab_sumilla.setCondicion("ide_geani=-1");
			tab_sumilla.ejecutarSql();
		}
	}

	
	public PanelMenu getPam_menu() {
		return pam_menu;
	}

	public void setPam_menu(PanelMenu pam_menu) {
		this.pam_menu = pam_menu;
	}

	public Panel getPan_opcion() {
		return pan_opcion;
	}

	public void setPan_opcion(Panel pan_opcion) {
		this.pan_opcion = pan_opcion;
	}

	public String getStr_opcion() {
		return str_opcion;
	}

	public void setStr_opcion(String str_opcion) {
		this.str_opcion = str_opcion;
	}

	public Efecto getEfecto() {
		return efecto;
	}

	public void setEfecto(Efecto efecto) {
		this.efecto = efecto;
	}

	public Tabla getTab_sumilla() {
		return tab_sumilla;
	}

	public void setTab_sumilla(Tabla tab_sumilla) {
		this.tab_sumilla = tab_sumilla;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public AutoCompletar getAut_tramite() {
		return aut_tramite;
	}

	public void setAut_tramite(AutoCompletar aut_tramite) {
		this.aut_tramite = aut_tramite;
	}

	public Tabla getTab_registro_respuesta() {
		return tab_registro_respuesta;
	}

	public void setTab_registro_respuesta(Tabla tab_registro_respuesta) {
		this.tab_registro_respuesta = tab_registro_respuesta;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Tabla getTab_reingreso_tramite() {
		return tab_reingreso_tramite;
	}

	public void setTab_reingreso_tramite(Tabla tab_reingreso_tramite) {
		this.tab_reingreso_tramite = tab_reingreso_tramite;
	}

}
