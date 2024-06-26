package paq_sumillas;

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
import pckEntidades.EnvioMail;
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

public class pre_sumilla extends Pantalla {

	private AutoCompletar aut_tramite = new AutoCompletar();
	private Tabla tab_sumilla = new Tabla();
	private Tabla tab_tramite_bit = new Tabla();
	private Tabla tab_tramite_respuesta_bit = new Tabla();
	private Tabla tab_tramite_respuesta_bit1 = new Tabla();
	private Tabla tab_tramite_respuesta_aux = new Tabla();
	private Tabla tab_registro_sumilla = new Tabla();
	
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	private Combo com_anio=new Combo();
	
	@EJB
	private ServicioDestinatario ser_nomina = (ServicioDestinatario ) utilitario.instanciarEJB(ServicioDestinatario.class);
	private ServicioGerenciaCoordinacion ser_gercoo = (ServicioGerenciaCoordinacion ) utilitario.instanciarEJB(ServicioGerenciaCoordinacion.class);
	private ServicioPrioridad ser_prior = (ServicioPrioridad ) utilitario.instanciarEJB(ServicioPrioridad.class);
	private ServicioSumilla ser_sumilla = (ServicioSumilla ) utilitario.instanciarEJB(ServicioSumilla.class);
	private ServicioSumTramite ser_tramite = (ServicioSumTramite ) utilitario.instanciarEJB(ServicioSumTramite.class);
	private ServicioSumTramite_bit sum_tramitebit = (ServicioSumTramite_bit ) utilitario.instanciarEJB(ServicioSumTramite_bit.class);
	private ServicioSumilla_bit ser_sumilla_bit = (ServicioSumilla_bit ) utilitario.instanciarEJB(ServicioSumilla_bit.class);
	private ServicioAnio ser_anio = (ServicioAnio ) utilitario.instanciarEJB(ServicioAnio.class);
	
	
	public pre_sumilla() {
		
		Boton bot_enviar = new Boton();
		bot_enviar.setValue("Enviar a Respuestas");
		bot_enviar.setMetodo("cambiarEtapa");
		bot_enviar.setTitle("Enviar Trámite a Respuestas");
		
		com_anio.setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarBoton(bot_enviar);
	
		tab_sumilla.setId("tab_sumilla");
		tab_sumilla.setHeader("DOCUMENTO DE ENTRADA");
		tab_sumilla.setTabla("sum_tramite", "ide_smtra", 1);
		
		tab_sumilla.setTipoFormulario(true);
		
		// formulario
		tab_sumilla.getGrid().setColumns(1);
		// hacer columnas
		tab_sumilla.getColumna("ide_smtra").setNombreVisual("Código");
		tab_sumilla.getColumna("ide_smtra").setLectura(true);
		tab_sumilla.getColumna("ide_smtra").setOrden(1);
		
		tab_sumilla.getColumna("doc_respuesta_smtra").setNombreVisual("Documento de Respuesta");
		tab_sumilla.getColumna("doc_respuesta_smtra").setOrden(2);
		tab_sumilla.getColumna("doc_respuesta_smtra").setLectura(true);
		
		tab_sumilla.getColumna("doc_interno_smtra").setNombreVisual("Documento Interno");
		tab_sumilla.getColumna("doc_interno_smtra").setOrden(3);
		tab_sumilla.getColumna("doc_interno_smtra").setLectura(true);
		
		tab_sumilla.getColumna("doc_referencia_smtra").setNombreVisual("Documento de Referencia");
		tab_sumilla.getColumna("doc_referencia_smtra").setOrden(4);
		tab_sumilla.getColumna("doc_referencia_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_sumtd").setNombreVisual("Tipo Documento");
		tab_sumilla.getColumna("ide_sumtd").setOrden(5);
		tab_sumilla.getColumna("ide_sumtd").setEstilo("width:250px");
		tab_sumilla.getColumna("ide_sumtd").setCombo("sum_tipo_documento", "ide_sumtd", "nombre_sumtd", "");
		tab_sumilla.getColumna("ide_sumtd").setLectura(true);
		
		tab_sumilla.getColumna("num_tramite_smtra").setNombreVisual("N° Trámite");
		tab_sumilla.getColumna("num_tramite_smtra").setOrden(6);
		tab_sumilla.getColumna("num_tramite_smtra").setLectura(true);
		
		tab_sumilla.getColumna("num_documento_smtra");
		tab_sumilla.getColumna("num_documento_smtra").setNombreVisual("N° Documento");
		tab_sumilla.getColumna("num_documento_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("num_documento_smtra").setOrden(7);
		tab_sumilla.getColumna("num_documento_smtra").setLectura(true);
		
		tab_sumilla.getColumna("num_hojas_smtra");
		tab_sumilla.getColumna("num_hojas_smtra").setNombreVisual("N° de hojas");
		tab_sumilla.getColumna("num_hojas_smtra").setOrden(8);
		tab_sumilla.getColumna("num_hojas_smtra").setLectura(true);
		
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
		tab_sumilla.getColumna("asunto_smtra").setEstilo("width:200px");
		tab_sumilla.getColumna("asunto_smtra").setOrden(15);
		tab_sumilla.getColumna("asunto_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_suest").setCombo("sum_estado_tramite", "ide_suest", "nombre_suest", "");
		tab_sumilla.getColumna("ide_suest").setValorDefecto("PENDIENTE");
		tab_sumilla.getColumna("ide_suest").setNombreVisual("Estado Trámite");
		tab_sumilla.getColumna("ide_suest").setOrden(16);
		tab_sumilla.getColumna("ide_suest").setLectura(true);
		
		tab_sumilla.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_sumilla.getColumna("ide_sumet").setNombreVisual("Etapa del Trámite");
		tab_sumilla.getColumna("ide_sumet").setOrden(17);
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));
		
		if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")){
			tab_sumilla.getColumna("ide_sumet").setLectura(true);
		}
		
		tab_sumilla.getColumna("observacion_smtra").setNombreVisual("Observación");
		tab_sumilla.getColumna("observacion_smtra").setOrden(18);
		tab_sumilla.getColumna("observacion_smtra").setLectura(true);
		
		tab_sumilla.getColumna("ide_geani").setNombreVisual("Año");
		tab_sumilla.getColumna("ide_geani").setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		tab_sumilla.getColumna("ide_geani").setOrden(19);
		tab_sumilla.getColumna("ide_geani").setLectura(true);
		
		tab_sumilla.getColumna("activo_smtra").setNombreVisual("Activo");
		tab_sumilla.getColumna("activo_smtra").setLectura(true);
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
		
		
		tab_sumilla.agregarRelacion(tab_registro_sumilla);
		tab_sumilla.dibujar();
		
		dibujarSumillas();
		
		PanelTabla pat_dat_gen=new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_sumilla); 
		PanelTabla pat_ing_sum=new PanelTabla();  
		pat_ing_sum.setPanelTabla(tab_registro_sumilla); 
		
		Division div_division = new Division();  
		div_division.dividir2(pat_dat_gen, pat_ing_sum, "20%", "V"); 
		agregarComponente(div_division); 
	}

	public void dibujarSumillas()
	{
		tab_registro_sumilla.setId("tab_registro_sumilla");
		tab_registro_sumilla.setHeader("REGISTRO DE SUMILLAS");
		tab_registro_sumilla.setTabla("sum_tramite_respuesta", "ide_smtre", 2);
		
		tab_registro_sumilla.setTipoFormulario(true); //formulario
		tab_registro_sumilla.getGrid().setColumns(6); //hacer columnas
		
		tab_registro_sumilla.getColumna("ide_smtre").setNombreVisual("Código");
		tab_registro_sumilla.getColumna("ide_smtre").setOrden(1);
		
		tab_registro_sumilla.getColumna("ide_suesu").setNombreVisual("Estado Sumilla");
		tab_registro_sumilla.getColumna("ide_suesu").setCombo("sum_estado_sumilla", "ide_suesu", "nombre_suesu", "");
		tab_registro_sumilla.getColumna("ide_suesu").setValorDefecto("1");
		tab_registro_sumilla.getColumna("ide_suesu").setOrden(2);
		
		tab_registro_sumilla.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_registro_sumilla.getColumna("ide_sumet").setValorDefecto("2");
		tab_registro_sumilla.getColumna("ide_sumet").setNombreVisual("Etapa del Trámite");
		tab_registro_sumilla.getColumna("ide_sumet").setOrden(3);
		
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));
		
		if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")){
			tab_registro_sumilla.getColumna("ide_sumet").setLectura(true);
		}
		
		tab_registro_sumilla.getColumna("disposicion_smtre").setNombreVisual("Disposición");
		tab_registro_sumilla.getColumna("disposicion_smtre").setEstilo("width:200px");
		tab_registro_sumilla.getColumna("disposicion_smtre").setOrden(4);
		
		tab_registro_sumilla.getColumna("fecha_sumilla_smtre").setNombreVisual("Fecha de Sumilla");
		tab_registro_sumilla.getColumna("fecha_sumilla_smtre").setOrden(5);
		
		tab_registro_sumilla.getColumna("ide_sumpr").setNombreVisual("Prioridad");
		tab_registro_sumilla.getColumna("ide_sumpr").setCombo("sum_prioridad", "ide_sumpr", "nombre_sumpr", "");
		tab_registro_sumilla.getColumna("ide_sumpr").setMetodoChange("seleccionaFechaEntrega");
		tab_registro_sumilla.getColumna("ide_sumpr").setOrden(6);
		
		tab_registro_sumilla.getColumna("ide_geareg").setNombreVisual("Gerencia");
		tab_registro_sumilla.getColumna("ide_geareg").setCombo(ser_gercoo.getGerencia());
		tab_registro_sumilla.getColumna("ide_geareg").setMetodoChange("seleccionaCoordinacion");
		tab_registro_sumilla.getColumna("ide_geareg").setEstilo("width:200px");
		tab_registro_sumilla.getColumna("ide_geareg").setOrden(7);
		
		tab_registro_sumilla.getColumna("ide_gearec").setNombreVisual("Coordinación");
		tab_registro_sumilla.getColumna("ide_gearec").setCombo(ser_gercoo.getCoordinacionesTotas());
		tab_registro_sumilla.getColumna("ide_gearec").setEstilo("width:200px");
		tab_registro_sumilla.getColumna("ide_gearec").setOrden(8);
		
		tab_registro_sumilla.getColumna("recibido_smtre").setNombreVisual("Recibido Por");
		tab_registro_sumilla.getColumna("recibido_smtre").setEstilo("width:200px");
		tab_registro_sumilla.getColumna("recibido_smtre").setOrden(9);
		
		tab_registro_sumilla.getColumna("fecha_entrega_smtre").setNombreVisual("Fecha de Entrega");
		tab_registro_sumilla.getColumna("fecha_entrega_smtre").setMetodoChange("seleccionaFechaEntrega");
		tab_registro_sumilla.getColumna("fecha_entrega_smtre").setOrden(10);
		
		tab_registro_sumilla.getColumna("fecha_vencimiento_smtre").setNombreVisual("Fecha de Vencimiento");
		tab_registro_sumilla.getColumna("fecha_vencimiento_smtre").setLectura(true);
		tab_registro_sumilla.getColumna("fecha_vencimiento_smtre").setOrden(11);
				
		tab_registro_sumilla.getColumna("cod_bpm_smtre").setNombreVisual("Código Documental");
		tab_registro_sumilla.getColumna("cod_bpm_smtre").setEstilo("width:200px");
		tab_registro_sumilla.getColumna("cod_bpm_smtre").setOrden(12);
		
		tab_registro_sumilla.getColumna("dias1_smtre").setNombreVisual("Prórroga 1");
		tab_registro_sumilla.getColumna("dias1_smtre").setMetodoChange("calculaFechaProrroga1");  
		tab_registro_sumilla.getColumna("dias1_smtre").setEstilo("width:30px");
		tab_registro_sumilla.getColumna("dias1_smtre").setOrden(13);
		
		tab_registro_sumilla.getColumna("fecha_calculo1_smtre").setNombreVisual("Fec Ini Prórroga1");
		tab_registro_sumilla.getColumna("fecha_calculo1_smtre").setMetodoChange("calculaFechaProrroga1");
		tab_registro_sumilla.getColumna("fecha_calculo1_smtre").setOrden(14);
		
		tab_registro_sumilla.getColumna("fecha_vencimiento1_smtre").setNombreVisual("Fecha Vencimiento1");
		tab_registro_sumilla.getColumna("fecha_vencimiento1_smtre").setOrden(15);
		
		tab_registro_sumilla.getColumna("dias2_smtre").setNombreVisual("Prórroga 2");
		tab_registro_sumilla.getColumna("dias2_smtre").setMetodoChange("calculaFechaProrroga2");
		tab_registro_sumilla.getColumna("dias2_smtre").setEstilo("width:30px");
		tab_registro_sumilla.getColumna("dias2_smtre").setOrden(16);
		
		tab_registro_sumilla.getColumna("fecha_calculo2_smtre").setNombreVisual("Fec Ini Prórroga2");
		tab_registro_sumilla.getColumna("fecha_calculo2_smtre").setMetodoChange("calculaFechaProrroga2");
		tab_registro_sumilla.getColumna("fecha_calculo2_smtre").setOrden(17);
		
		tab_registro_sumilla.getColumna("fecha_vencimiento2_smtre").setNombreVisual("Fecha Vencimiento2");
		tab_registro_sumilla.getColumna("fecha_vencimiento2_smtre").setOrden(18);
		
		tab_registro_sumilla.getColumna("dias3_smtre").setNombreVisual("Prórroga 3");
		tab_registro_sumilla.getColumna("dias3_smtre").setMetodoChange("calculaFechaProrroga3");
		tab_registro_sumilla.getColumna("dias3_smtre").setEstilo("width:30px");
		tab_registro_sumilla.getColumna("dias3_smtre").setOrden(19);
		
		tab_registro_sumilla.getColumna("fecha_calculo3_smtre").setNombreVisual("Fec Ini Prórroga3");
		tab_registro_sumilla.getColumna("fecha_calculo3_smtre").setMetodoChange("calculaFechaProrroga3");
		tab_registro_sumilla.getColumna("fecha_calculo3_smtre").setOrden(20);
		
		tab_registro_sumilla.getColumna("fecha_vencimiento3_smtre").setNombreVisual("Fecha Vencimiento3");
		tab_registro_sumilla.getColumna("fecha_vencimiento3_smtre").setOrden(21);
		
		tab_registro_sumilla.getColumna("dias4_smtre").setNombreVisual("Prórroga 4");
		tab_registro_sumilla.getColumna("dias4_smtre").setMetodoChange("calculaFechaProrroga4");
		tab_registro_sumilla.getColumna("dias4_smtre").setEstilo("width:30px");
		tab_registro_sumilla.getColumna("dias4_smtre").setOrden(22);
		
		tab_registro_sumilla.getColumna("fecha_calculo4_smtre").setNombreVisual("Fec Ini Prórroga4");
		tab_registro_sumilla.getColumna("fecha_calculo4_smtre").setMetodoChange("calculaFechaProrroga4");
		tab_registro_sumilla.getColumna("fecha_calculo4_smtre").setOrden(23);
		
		tab_registro_sumilla.getColumna("fecha_vencimiento4_smtre").setNombreVisual("Fecha Vencimiento 4");
		tab_registro_sumilla.getColumna("fecha_vencimiento4_smtre").setOrden(24);
		
		tab_registro_sumilla.getColumna("adjunto_smtre").setNombreVisual("Archivo Adjunto");
		tab_registro_sumilla.getColumna("adjunto_smtre").setUpload("documento");
		tab_registro_sumilla.getColumna("adjunto_smtre").setOrden(25);
		
		tab_registro_sumilla.getColumna("observacion_sum_smtre").setNombreVisual("Observación");
		tab_registro_sumilla.getColumna("observacion_sum_smtre").setEstilo("width:250px");
		tab_registro_sumilla.getColumna("observacion_sum_smtre").setOrden(26);
		
		tab_registro_sumilla.getColumna("activo_smtre").setNombreVisual("Activo");
		tab_registro_sumilla.getColumna("activo_smtre").setValorDefecto("true");
		tab_registro_sumilla.getColumna("activo_smtre").setOrden(27);
		
		
		
		//ocultas prorroga
		tab_registro_sumilla.getColumna("observacion1_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("observacion2_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("observacion3_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("observacion4_smtre").setVisible(false);
		//ocultas respuesta
	    tab_registro_sumilla.getColumna("fecha_documento_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("num_documento_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("destinatario_regdes").setVisible(false);
	    tab_registro_sumilla.getColumna("cargo_destinatario_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("empresa_destinatario_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("asunto_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("fecha_entrega_res_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("fecha_limite_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("fecha_limite_prog_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("gabinete_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("adjunto_res_smtre").setVisible(false);
	    tab_registro_sumilla.getColumna("TRG_ACTUALIZACION_SMTRE").setVisible(false);
	    tab_registro_sumilla.getColumna("OBSERVACION_REVRES_SMTRE").setVisible(false);
	    tab_registro_sumilla.getColumna("CARGO_REMITENTE_SMTRE").setVisible(false);
	    tab_registro_sumilla.getColumna("EMPRESA_REMITENTE_SMTRE").setVisible(false);
	    tab_registro_sumilla.getColumna("REMITENTE_SMTRE").setVisible(false);
	    tab_registro_sumilla.getColumna("IDE_SUMTD").setVisible(false);
	    tab_registro_sumilla.getColumna("envio_mail_smtre").setVisible(false);
	    
	    tab_registro_sumilla.setCampoForanea("ide_smtra");
		tab_registro_sumilla.dibujar();
	}
	
	public String seleccionaCoordinacion() {
		String sqlcoo = "";
		if (tab_registro_sumilla.getValor("ide_geareg")!= null) {
			//utilitario.agregarMensajeInfo("gerencia = " + tab_registro_sumilla.getValor("ide_geareg").toString(), "");
			sqlcoo = ser_gercoo.getCoordinacion(Integer.parseInt(tab_registro_sumilla.getValor("ide_geareg").toString()));
			tab_registro_sumilla.getColumna("ide_gearec").setCombo(sqlcoo);
			utilitario.addUpdateTabla(tab_registro_sumilla, "ide_gearec", "");
			//tab_registro_sumilla.getColumna("ide_gearec").setSqlCombo(sqlcoo);
			//tab_registro_sumilla.getColumna("ide_gearec").actualizarCombo();
		} else {
			//utilitario.agregarMensajeInfo("No se pudo cargar la lista de Coordinaciones", "");
			sqlcoo = ser_gercoo.getVacio();
		}
		return sqlcoo;
	}
	
	public void seleccionaFechaEntrega() {
		if (tab_registro_sumilla.getValor("fecha_entrega_smtre")!= null&&tab_registro_sumilla.getValor("ide_sumpr")!= null) {
			Integer prioridad=Integer.parseInt(tab_registro_sumilla.getValor("ide_sumpr"));
			List dias=utilitario.getConexion().consultar(ser_prior.getPrioridad(prioridad));
			Date fechaVen=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_registro_sumilla.getValor("fecha_entrega_smtre")),(Integer) dias.get(0));
			tab_registro_sumilla.setValor("fecha_vencimiento_smtre", utilitario.DeDateAString(fechaVen));
			utilitario.addUpdateTabla(tab_registro_sumilla, "fecha_vencimiento_smtre", "");
		} 
	}
	
	public void calculaFechaProrroga1() {
		if (tab_registro_sumilla.getValor("dias1_smtre")!= null && tab_registro_sumilla.getValor("fecha_calculo1_smtre")!= null) {
			Integer prorroga=Integer.parseInt(tab_registro_sumilla.getValor("dias1_smtre"));
			Date fechaVen1=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_registro_sumilla.getValor("fecha_calculo1_smtre")),prorroga);
			tab_registro_sumilla.setValor("fecha_vencimiento1_smtre", utilitario.DeDateAString(fechaVen1));
			utilitario.addUpdateTabla(tab_registro_sumilla, "fecha_vencimiento1_smtre", "");
		} 
	}
	
	public void calculaFechaProrroga2() {
		if (tab_registro_sumilla.getValor("dias2_smtre")!= null && tab_registro_sumilla.getValor("fecha_calculo2_smtre")!= null) {
			Integer prorroga=Integer.parseInt(tab_registro_sumilla.getValor("dias2_smtre"));
			Date fechaVen2=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_registro_sumilla.getValor("fecha_calculo2_smtre")),prorroga);
			tab_registro_sumilla.setValor("fecha_vencimiento2_smtre", utilitario.DeDateAString(fechaVen2));
			utilitario.addUpdateTabla(tab_registro_sumilla, "fecha_vencimiento2_smtre", "");
		}
	}
	
	public void calculaFechaProrroga3() {
		if (tab_registro_sumilla.getValor("dias3_smtre")!= null && tab_registro_sumilla.getValor("fecha_calculo3_smtre")!= null) {
			Integer prorroga=Integer.parseInt(tab_registro_sumilla.getValor("dias3_smtre"));
			Date fechaVen3=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_registro_sumilla.getValor("fecha_calculo3_smtre")),prorroga);
			tab_registro_sumilla.setValor("fecha_vencimiento3_smtre", utilitario.DeDateAString(fechaVen3));
			utilitario.addUpdateTabla(tab_registro_sumilla, "fecha_vencimiento3_smtre", "");
		}
	}
	
	public void calculaFechaProrroga4() {
		if (tab_registro_sumilla.getValor("dias4_smtre")!= null && tab_registro_sumilla.getValor("fecha_calculo4_smtre")!= null) {
			Integer prorroga=Integer.parseInt(tab_registro_sumilla.getValor("dias4_smtre"));
			Date fechaVen4=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_registro_sumilla.getValor("fecha_calculo4_smtre")),prorroga);
			tab_registro_sumilla.setValor("fecha_vencimiento4_smtre", utilitario.DeDateAString(fechaVen4));
			utilitario.addUpdateTabla(tab_registro_sumilla, "fecha_vencimiento4_smtre", "");
		}
	}
	
	/**
	 * Cambia el registro a etapa RESPUESTA
	 */
	public void cambiarEtapa() {
		
		if (tab_registro_sumilla.getValor("ide_sumet").equals("SUMILLA") || tab_registro_sumilla.getValor("ide_sumet").equals("2")) {
			utilitario.agregarMensaje("Se cambio la Etapa de la Sumilla a RESPUESTA", "");
			tab_registro_sumilla.setValor("ide_sumet", "3");
			tab_registro_sumilla.modificar(tab_registro_sumilla.getFilaActual());
			utilitario.addUpdate("tab_registro_sumilla");
			tab_registro_sumilla.guardar();
			guardarPantalla();
			
			//validación para saber si ya existe un registro con el id de sumilla y el estado Respuesta no ingresar
			Integer padreSum = Integer.parseInt(tab_registro_sumilla.getValor("ide_smtre"));
			Integer etapaSum = 3;
			List listEtapaResSum=utilitario.getConexion().consultar(ser_sumilla_bit.getTramiteRespuestaBitPorTramiteRespuestaYEtapa(padreSum,etapaSum));
			Integer auxRegSum = listEtapaResSum.size();
			if (auxRegSum <= 0){
				//insertar sumilla con etapa Respuesta a la bitacora
				generarTramiteRespuesta1();
			}
			
			//actualizar tramite a "Respuesta"
			Integer padre=Integer.parseInt(tab_sumilla.getValor("ide_smtra"));
			Integer etapa = 3;
			/*List listEtapa=utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardreYEtapa(padre,etapa));
			Integer le = listEtapa.size();
			List listHija=utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardre(padre));
			Integer lh = listHija.size();
			if (le.equals(lh)){*/
				utilitario.agregarMensaje("Se cambio la Etapa del Trámite a RESPUESTA", "");
				tab_sumilla.setValor("ide_sumet", "3");
				tab_sumilla.modificar(tab_sumilla.getFilaActual());
				utilitario.addUpdate("tab_sumilla");
				tab_sumilla.guardar();
				guardarPantalla();
				enviarMail();
				//validar que ya se haya se haya generado
				List listEtapaRes=utilitario.getConexion().consultar(sum_tramitebit.getTramiteBitPorTramiteYEtapa(padre,etapa));
				Integer auxReg = listEtapaRes.size();
				if (auxReg <= 0){
					generarTramite();
				}
			//}
		} else{
			utilitario.agregarMensaje("No es posible el cambio de Etapa la etapa actual no es SUMILLA", "");
		}
	}
	
	/**
	 * Método para generar la bitácora de trámite
	 */
	public void generarTramite(){
		
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
	 * Método para generar la bitácora de trámite_respuesta
	 */
	public void generarTramiteRespuesta(){
		tab_tramite_respuesta_bit.setId("tab_tramite_respuesta_bit");
		tab_tramite_respuesta_bit.setTabla("sum_tramite_respuesta_bit", "ide_smtre_bit", 1);
		
		TablaGenerica tab_tramiteResB = ser_sumilla.getTramiteRespuestaPorId(Integer.parseInt(tab_registro_sumilla.getValor("ide_smtre")));
		
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
	public void generarTramiteRespuesta1(){
		tab_tramite_respuesta_bit1.setId("tab_tramite_respuesta_bit1");
		tab_tramite_respuesta_bit1.setTabla("sum_tramite_respuesta_bit", "ide_smtre_bit", 1);
		
		TablaGenerica tab_tramiteResB = ser_sumilla.getTramiteRespuestaPorId(Integer.parseInt(tab_registro_sumilla.getValor("ide_smtre")));
		
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
		if (tab_sumilla.isFocus()) {
			utilitario.agregarMensajeInfo("No se puede insertar un nuevo Trámite en esta opción ", "");
		}
		else if (tab_registro_sumilla.isFocus()) {
			//verificar solo se puede insertar mientras exista sumillas en etapa SUMILLA
			Integer padre=Integer.parseInt(tab_sumilla.getValor("ide_smtra"));
			Integer etapa = 2;
			List listEtapa=utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardreYEtapa(padre,etapa));
			List listhija=utilitario.getConexion().consultar(ser_sumilla.getSumillaPorPardre(padre)); 
			if (listEtapa.size()>0 || listhija.size()<=0){
				//poner valores por defecto al nuevo registro si ya se ha ingresado un registro
				tab_tramite_respuesta_aux.setId("tab_tramite_respuesta_aux");
				tab_tramite_respuesta_aux.setTabla("sum_tramite_respuesta", "ide_smtre", 1);
				TablaGenerica tab_tramiteRes = ser_sumilla.getSumillaPorPadreCmp(padre);
				for (int i = 0; i < tab_tramiteRes.getTotalFilas(); i++) {
					if (i<=0) {
						tab_registro_sumilla.getColumna("fecha_sumilla_smtre").setValorDefecto(tab_tramiteRes.getValor(i, "fecha_sumilla_smtre"));
						tab_registro_sumilla.getColumna("disposicion_smtre").setValorDefecto(tab_tramiteRes.getValor(i, "disposicion_smtre"));
						tab_registro_sumilla.getColumna("ide_sumpr").setValorDefecto(tab_tramiteRes.getValor(i, "ide_sumpr"));
						tab_registro_sumilla.getColumna("fecha_entrega_smtre").setValorDefecto(tab_tramiteRes.getValor(i, "fecha_entrega_smtre"));
					}
				}
				tab_registro_sumilla.insertar();
			} else {
				utilitario.agregarMensajeInfo("No se puede insertar, todas los datos superaron la etapa de SUMILLA", "");
			}
		}
	}
	
public void enviarMail(){
		String id = tab_registro_sumilla.getValor("ide_smtre");
		String sql = "select nom_usua, mail_usua from sis_usuario, sum_etapa_usuario where ide_sumet=3 ";
		sql = sql + " and sum_etapa_usuario.ide_usua=sis_usuario.ide_usua and sum_etapa_usuario.activo_sumeu=true ";
		sql = sql + " and (sum_etapa_usuario.ide_geareg = (select ide_geareg from sum_tramite_respuesta where ide_smtre =" + id + ") ";
		sql = sql + " or sum_etapa_usuario.ide_gearec = (select ide_gearec from sum_tramite_respuesta where ide_smtre =" + id + ")) ";
		
		TablaGenerica tab_correos= utilitario.consultar(sql);
		
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
				
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				
		/*for (int i = 0; i < tab_correos.getTotalFilas(); i++) {
			
			try {
				if(tab_correos.getValor(i,"mail_usua")!=null)
				//util.EnviaMail(envMail, tab_correos.getValor(i,"mail_usua"), 
								"CONTROL DE SUMILLAS",
								emailNotificacionCambioEtapa(tab_correos.getValor(i,"nom_usua"),tab_sumilla.getValor("num_tramite_smtra"),tab_sumilla.getValor("num_documento_smtra"),"RESPUESTA"),null);
				} catch (Exception e) {
				System.out.println("Error en el envío de correo"+e.getMessage());
				}		
		}*/
	}
	
	
	public String emailNotificacionCambioEtapa(String strNombreEmpleado ,String numTramite,String numDocumento,String nombreEtapa) {
	      String html = "<p>Estimado.(a), "
	              + "</p>\n"
	              + "<p>"+strNombreEmpleado+"</p>"
	              + "<p>&nbsp;</p>\n"
	              + "<p>El trámite N°:  "+numTramite.toLowerCase()+" , N° de Documento: "+numDocumento.toLowerCase()+" ha cambiado a la Etapa: "+nombreEtapa.toLowerCase()+".</p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Saludos cordiales,</p>\n"
	              + "<table style=\"height: 144px;\" width=\"571\">\n"
	              + "<tbody>\n"
	              + "<tr>\n"
	              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	              + "<td width=\"476\">\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
	              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	              + "</td>\n"
	              + "</tr>\n"
	              + "</tbody>\n"
	              + "</table>";
	      return html;
	  }

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		String auxId = tab_registro_sumilla.getValor("ide_smtre");
		//System.out.println("-----------------> "+auxId);
		 if(tab_sumilla.guardar())
		 {   
			 guardarPantalla();  
		 } 
		 if(tab_registro_sumilla.guardar())
		 {   
			 guardarPantalla();
			//adicionar a bitacora cuando se guarda el registro
			 if (auxId == null){
				 generarTramiteRespuesta();
			 }
		 }  
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public void seleccionaElAnio (){
		
		if(com_anio.getValue()!=null){
			List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_gercoo.getPerfilConectado(utilitario.getVariable("IDE_USUA")));
			
			if (!perfilUsuarioConectado.get(0).toString().equals("ADM. SUMILLAS")){
			tab_sumilla.setCondicion("ide_sumet = 2 and "+"ide_geani="+com_anio.getValue());
				tab_sumilla.ejecutarSql();
				tab_registro_sumilla.ejecutarSql();				
			}
			else
			{
				tab_sumilla.setCondicion("ide_geani="+com_anio.getValue());
				tab_sumilla.ejecutarSql();
				tab_registro_sumilla.ejecutarSql();		
			}
		}
		else{
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

	public Tabla getTab_registro_sumilla() {
		return tab_registro_sumilla;
	}
	public void setTab_registro_sumilla(Tabla tab_registro_sumilla) {
		this.tab_registro_sumilla = tab_registro_sumilla;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

}
