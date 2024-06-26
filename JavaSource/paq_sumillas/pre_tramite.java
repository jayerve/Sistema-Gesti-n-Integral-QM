package paq_sumillas;

import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.editor.Editor;
import org.primefaces.component.panelmenu.PanelMenu;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioAnio;
import paq_sumillas.ejb.ServicioDestinatario;
import paq_sumillas.ejb.ServicioGerenciaCoordinacion;
import paq_sumillas.ejb.ServicioSumTramite;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;



public class pre_tramite extends Pantalla {
	
	private Tabla tab_tramite = new Tabla();
	private Tabla tab_tramite_bit = new Tabla();
	private Tabla tab_tramite_bit1 = new Tabla();
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private Combo com_anio=new Combo();
	private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	private Editor edi_msj = new Editor();
	
	public static String p_sec_sumilla;

	@EJB
	private ServicioDestinatario ser_nomina = (ServicioDestinatario ) utilitario.instanciarEJB(ServicioDestinatario.class);
	private ServicioSumTramite ser_tramite = (ServicioSumTramite ) utilitario.instanciarEJB(ServicioSumTramite.class);
	private ServicioAnio ser_anio = (ServicioAnio ) utilitario.instanciarEJB(ServicioAnio.class);
	
	@EJB
	private ServicioGerenciaCoordinacion ser_gercoo = (ServicioGerenciaCoordinacion ) utilitario.instanciarEJB(ServicioGerenciaCoordinacion.class);
	
	public pre_tramite() {
		p_sec_sumilla =utilitario.getVariable("p_sec_sumilla");
		
		Boton bot_enviar = new Boton();
		bot_enviar.setValue("Enviar a Sumillas");
		bot_enviar.setMetodo("cambiarEtapa");
		bot_enviar.setTitle("Enviar Trámite a Sumillas");
		
		
		com_anio.setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarBoton(bot_enviar);
		
		tab_tramite.setId("tab_tramite");
		tab_tramite.setHeader("REGISTRO DEL DOCUMENTO DE ENTRADA");
		tab_tramite.setTabla("sum_tramite", "ide_smtra", 1);
		
		tab_tramite.setTipoFormulario(true); // formulario
		tab_tramite.getGrid().setColumns(4); // hacer columnas
		
		tab_tramite.getColumna("ide_smtra").setNombreVisual("Código");
		tab_tramite.getColumna("ide_smtra").setOrden(1);
		
		tab_tramite.getColumna("num_tramite_smtra").setNombreVisual("N° Trámite");
		tab_tramite.getColumna("num_tramite_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("num_tramite_smtra").setOrden(2);
		
		tab_tramite.getColumna("doc_respuesta_smtra").setNombreVisual("Documento de Respuesta");
		tab_tramite.getColumna("doc_respuesta_smtra").setOrden(3);
		
		tab_tramite.getColumna("doc_interno_smtra").setNombreVisual("Documento Interno");
		tab_tramite.getColumna("doc_interno_smtra").setOrden(4);
		
		tab_tramite.getColumna("ide_sumtd");
		tab_tramite.getColumna("ide_sumtd").setNombreVisual("Tipo Documento");
		tab_tramite.getColumna("ide_sumtd").setOrden(5);
		tab_tramite.getColumna("ide_sumtd").setEstilo("width:250px");
		tab_tramite.getColumna("ide_sumtd").setCombo("sum_tipo_documento", "ide_sumtd", "nombre_sumtd", "");
		
		tab_tramite.getColumna("num_documento_smtra");
		tab_tramite.getColumna("num_documento_smtra").setNombreVisual("N° Documento");
		tab_tramite.getColumna("num_documento_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("num_documento_smtra").setOrden(6);
		
		tab_tramite.getColumna("num_hojas_smtra");
		tab_tramite.getColumna("num_hojas_smtra").setNombreVisual("N° de hojas");
		tab_tramite.getColumna("num_hojas_smtra").setOrden(7);
		
		tab_tramite.getColumna("doc_referencia_smtra");
		tab_tramite.getColumna("doc_referencia_smtra").setNombreVisual("Documento de Referencia");
		tab_tramite.getColumna("doc_referencia_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("doc_referencia_smtra").setOrden(8);
		
		tab_tramite.getColumna("fecha_emision_doc_smtra");
		tab_tramite.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha de Emisión del documento");
		tab_tramite.getColumna("fecha_emision_doc_smtra").setOrden(9);
		
		tab_tramite.getColumna("fecha_recep_doc_smtra");
		tab_tramite.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha de Recepción del documento");
		tab_tramite.getColumna("fecha_recep_doc_smtra").setOrden(10);
		
		tab_tramite.getColumna("remitente_smtra");
		tab_tramite.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_tramite.getColumna("remitente_smtra").setOrden(11);
		tab_tramite.getColumna("remitente_smtra").setEstilo("width:250px");
		
		tab_tramite.getColumna("cargo_remitente_smtra");
		tab_tramite.getColumna("cargo_remitente_smtra").setNombreVisual("Cargo Remitente");
		tab_tramite.getColumna("cargo_remitente_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("cargo_remitente_smtra").setOrden(12);
		
		tab_tramite.getColumna("empresa_remitente_smtra");
		tab_tramite.getColumna("empresa_remitente_smtra").setNombreVisual("Empresa Remitente");
		tab_tramite.getColumna("empresa_remitente_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("empresa_remitente_smtra").setOrden(13);
		
		tab_tramite.getColumna("destinatario_regdes");
		tab_tramite.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_tramite.getColumna("destinatario_regdes").setOrden(14);
		tab_tramite.getColumna("destinatario_regdes").setEstilo("width:250px");
		tab_tramite.getColumna("destinatario_regdes").setCombo(ser_nomina.getEmpleadoContrato("true"));
		tab_tramite.getColumna("destinatario_regdes").setMetodoChange("seleccionaElCargo");
		
		tab_tramite.getColumna("cargo_destinatario_smtra");
		tab_tramite.getColumna("cargo_destinatario_smtra").setNombreVisual("Cargo Destinatario");
		tab_tramite.getColumna("cargo_destinatario_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("cargo_destinatario_smtra").setOrden(15);
		
		tab_tramite.getColumna("empresa_destinatario_smtra");
		tab_tramite.getColumna("empresa_destinatario_smtra").setNombreVisual("Empresa Destinatario");
		tab_tramite.getColumna("empresa_destinatario_smtra").setEstilo("width:250px");
		tab_tramite.getColumna("empresa_destinatario_smtra").setOrden(16);
		
		tab_tramite.getColumna("asunto_smtra");
		tab_tramite.getColumna("asunto_smtra").setNombreVisual("Asunto-Comunicación");
		tab_tramite.getColumna("asunto_smtra").setOrden(17);
		
		tab_tramite.getColumna("cod_bpm_esttra");
		tab_tramite.getColumna("cod_bpm_esttra").setNombreVisual("Código Documental");
		tab_tramite.getColumna("cod_bpm_esttra").setEstilo("width:250px");
		tab_tramite.getColumna("cod_bpm_esttra").setOrden(18);
		
		tab_tramite.getColumna("adjunto_esttra");
		tab_tramite.getColumna("adjunto_esttra").setNombreVisual("Documento Adjunto");
		tab_tramite.getColumna("adjunto_esttra").setUpload("adjuntoTramite");
		tab_tramite.getColumna("adjunto_esttra").setOrden(19);
		
		tab_tramite.getColumna("ide_suest").setCombo("sum_estado_tramite", "ide_suest", "nombre_suest", "");
		tab_tramite.getColumna("ide_suest").setValorDefecto("PENDIENTE");
		tab_tramite.getColumna("ide_suest").setNombreVisual("Estado Trámite");
		tab_tramite.getColumna("ide_suest").setOrden(20);
		
		tab_tramite.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_tramite.getColumna("ide_sumet").setValorDefecto("REGISTRO");
		tab_tramite.getColumna("ide_sumet").setNombreVisual("Etapa del Trámite");
		tab_tramite.getColumna("ide_sumet").setOrden(21);
		
		tab_tramite.getColumna("ide_geani").setNombreVisual("Año");
		tab_tramite.getColumna("ide_geani").setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		tab_tramite.getColumna("ide_geani").setOrden(22);

		tab_tramite.getColumna("observacion_smtra").setNombreVisual("Observación");
		tab_tramite.getColumna("observacion_smtra").setOrden(23);
		
		tab_tramite.getColumna("activo_smtra").setNombreVisual("Activo");
		tab_tramite.getColumna("activo_smtra").setValorDefecto("true");
		tab_tramite.getColumna("activo_smtra").setOrden(24);
		
		tab_tramite.setCondicion("ide_geani=-1"); 
		tab_tramite.getColumna("trg_actualizacion_smtra").setVisible(false);
		tab_tramite.dibujar();
		
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_tramite);    
		Division div_division = new Division();  
		div_division.dividir1(pat_dat_gen);  
		agregarComponente(div_division); 

	}

	/**
	 * Selecciona el cargo del Destinatario
	 */
	public void seleccionaElCargo() {
		if (tab_tramite.getValor("destinatario_regdes")!= null) {
			List cargo=utilitario.getConexion().consultar(ser_nomina.getEmpleadoContratoCargo(tab_tramite.getValor("destinatario_regdes")));
			tab_tramite.setValor("cargo_destinatario_smtra", cargo.get(0).toString());
			utilitario.addUpdateTabla(tab_tramite, "cargo_destinatario_smtra", "");
		} 
	}
	
	/**
	 * Cambia el registro a etapa SUMILLAS
	 */
	public void cambiarEtapa() {
		if (tab_tramite.getValor("ide_sumet").equals("REGISTRO") || tab_tramite.getValor("ide_sumet").equals("1")) {
			utilitario.agregarMensaje("Se cambio la Etapa del Trámite a: SUMILLA", "");
			tab_tramite.setValor("ide_sumet", "2");
			tab_tramite.modificar(tab_tramite.getFilaActual());
			utilitario.addUpdate("tab_tramite");
			tab_tramite.guardar();
			guardarPantalla();
			generarTramite1();
			enviarMail();
		} else{
			utilitario.agregarMensaje("No es posible el cambio de Etapa la etapa actual no es REGISTRO", "");
		} 
		
	}
	
	/**
	 * Método para generar la bitácora de trámite
	 */
	public void generarTramite(){
		
		tab_tramite_bit.setId("tab_tramite_bit");
		tab_tramite_bit.setTabla("sum_tramite_bit", "ide_smtra_bit", 1);
		
		TablaGenerica tab_tramiteB = ser_tramite.getTramitePorId(Integer.parseInt(tab_tramite.getValor("ide_smtra")));
		
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
	public void generarTramite1(){
		
		tab_tramite_bit1.setId("tab_tramite_bit1");
		tab_tramite_bit1.setTabla("sum_tramite_bit", "ide_smtra_bit", 1);
		
		TablaGenerica tab_tramiteB = ser_tramite.getTramitePorId(Integer.parseInt(tab_tramite.getValor("ide_smtra")));
		
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
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		utilitario.getTablaisFocus().insertar();
		tab_tramite.setValor("ide_suest", "1");
		tab_tramite.setValor("ide_sumet", "1");
		tab_tramite.setValor("ide_geani", com_anio.getValue()+"");
		List anio=utilitario.getConexion().consultar(ser_anio.getAnioPoId(Integer.parseInt(com_anio.getValue().toString())));
		tab_tramite.setValor("num_tramite_smtra", anio.get(0).toString() + "-" +ser_tramite.numeroSecuencial(p_sec_sumilla));
		//System.out.println("secuencial---> "+ ser_tramite.numeroSecuencial(p_sec_sumilla));
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		String auxId = tab_tramite.getValor("ide_smtra");
		//System.out.println("-----------------> "+auxId);
		 if(tab_tramite.guardar())
		 {   
			 guardarPantalla();
			 //adicionar a bitacora cuando se guarda el registro
			 if (auxId == null){
				 generarTramite(); 
				 ser_tramite.guardaSecuencial(ser_tramite.numeroSecuencial(p_sec_sumilla), p_sec_sumilla);
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
				tab_tramite.setCondicion("ide_sumet = 1 and "+"ide_geani="+com_anio.getValue());
				tab_tramite.ejecutarSql();
			}else{
				tab_tramite.setCondicion("ide_geani="+com_anio.getValue());
				tab_tramite.ejecutarSql();
			}
		}
		else{
			tab_tramite.setCondicion("ide_geani=-1");
			tab_tramite.ejecutarSql();
		}
	}
	
	
	public void enviarMail(){		
		
		TablaGenerica tab_correos= utilitario.consultar("select nick_usua, nom_usua, mail_usua from sis_usuario, sum_etapa_usuario where ide_sumet=2 and sum_etapa_usuario.activo_sumeu=true and sum_etapa_usuario.ide_usua=sis_usuario.ide_usua");
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, clave_corr from sis_correo where ide_corr=2"); 
		
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
				
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
		
		StringBuilder str_resultado = new StringBuilder();
				
		/*for (int i = 0; i < tab_correos.getTotalFilas(); i++) {	
			try {
				if(tab_correos.getValor(i,"mail_usua")!=null)
				//util.EnviaMail(envMail, tab_correos.getValor(i,"mail_usua"), "CONTROL DE SUMILLAS",
						emailNotificacionCambioEtapa(tab_correos.getValor(i,"nom_usua"),tab_tramite.getValor("num_tramite_smtra"),tab_tramite.getValor("num_documento_smtra"),tab_tramite.getValor("asunto_smtra"),"SUMILLA"),null);
				} catch (Exception e) {
					System.out.println("Error en el envío de correo"+e.getMessage());
				}
		}*/
	}
	
	private String getFormatoError(String mensaje) {
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}
	
	public String emailNotificacionCambioEtapa(String strNombreEmpleado ,String numTramite,String numDocumento,String nombreEtapa, String strAsunto) {
	      String html = "<p>Estimado.(a), "
	              + "</p>\n"
	              + "<p>"+strNombreEmpleado+"</p>"
	              + "<p>&nbsp;</p>\n"
	              + "<p>El trámite N°:  "+numTramite.toLowerCase()+" , N° de Documento: "+numDocumento.toLowerCase()+" ha cambiado a la Etapa: "+nombreEtapa.toLowerCase()+", Asunto: "+ strAsunto.toLowerCase()+".</p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Saludos cordiales,</p>\n"
	              + "<table style=\"height: 144px;\" width=\"571\">\n"
	              + "<tbody>\n"
	              + "<tr>\n"
	              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	              + "<td width=\"476\">\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>MODULO DE SUMILLAS</strong></p>\n"
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

	//Gets y Sets
	public Tabla getTab_tramite() {
		return tab_tramite;
	}
	public void setTab_tramite(Tabla tab_tramite) {
		this.tab_tramite = tab_tramite;
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

	public Division getDiv_division() {
		return div_division;
	}
	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}	

	
}
