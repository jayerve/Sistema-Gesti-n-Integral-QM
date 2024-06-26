package paq_sumillas;

import javax.ejb.EJB;

import org.primefaces.component.panelmenu.PanelMenu;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioAnio;
import paq_sumillas.ejb.ServicioDestinatario;
import paq_sumillas.ejb.ServicioSumTramite;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_tramite_bit extends Pantalla {

	private Tabla tab_tramite = new Tabla();
	private Tabla tab_tramite_bit = new Tabla();
	private Tabla tab_tramiteRespuesta_bit = new Tabla();
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private Combo com_anio=new Combo();
	private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();

	@EJB
	private ServicioDestinatario ser_nomina = (ServicioDestinatario ) utilitario.instanciarEJB(ServicioDestinatario.class);
	private ServicioSumTramite ser_tramite = (ServicioSumTramite ) utilitario.instanciarEJB(ServicioSumTramite.class);
	private ServicioAnio ser_anio = (ServicioAnio ) utilitario.instanciarEJB(ServicioAnio.class);
	
	
	public pre_tramite_bit() {
		
		Boton bot_enviar = new Boton();
		com_anio.setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		//Tabulador tab_tabulador = new Tabulador();
		//tab_tabulador.setId("tab_tabulador");
		tab_tramite.setId("tab_tramite");
		tab_tramite.setHeader("DOCUMENTO DE ENTRADA");
		tab_tramite.setTabla("sum_tramite", "ide_smtra", 1);
		tab_tramite.setTipoFormulario(true);
		tab_tramite.getGrid().setColumns(1);
		
		tab_tramite.getColumna("ide_smtra").setNombreVisual("Código");
		tab_tramite.getColumna("ide_smtra").setLectura(true);
		tab_tramite.getColumna("ide_smtra").setOrden(1);
		
		tab_tramite.getColumna("doc_respuesta_smtra").setNombreVisual("Documento de Respuesta");
		tab_tramite.getColumna("doc_respuesta_smtra").setVisible(false);
		
		tab_tramite.getColumna("doc_interno_smtra").setNombreVisual("Documento Interno");
		tab_tramite.getColumna("doc_interno_smtra").setVisible(false);
		
		tab_tramite.getColumna("num_tramite_smtra").setNombreVisual("N° Trámite");
		tab_tramite.getColumna("num_tramite_smtra").setOrden(2);
		tab_tramite.getColumna("num_tramite_smtra").setLectura(true);
		
		tab_tramite.getColumna("num_documento_smtra");
		tab_tramite.getColumna("num_documento_smtra").setNombreVisual("N° Documento");
		tab_tramite.getColumna("num_documento_smtra").setEstilo("width:200px");
		tab_tramite.getColumna("num_documento_smtra").setOrden(3);
		tab_tramite.getColumna("num_documento_smtra").setLectura(true);
		
		tab_tramite.getColumna("num_hojas_smtra").setNombreVisual("N° de hojas");
		tab_tramite.getColumna("num_hojas_smtra").setVisible(false);
		
		tab_tramite.getColumna("fecha_emision_doc_smtra");
		tab_tramite.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha de Emisión del documento");
		tab_tramite.getColumna("fecha_emision_doc_smtra").setOrden(4);
		tab_tramite.getColumna("fecha_emision_doc_smtra").setLectura(true);
		
		tab_tramite.getColumna("fecha_recep_doc_smtra");
		tab_tramite.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha de Recepción del documento");
		tab_tramite.getColumna("fecha_recep_doc_smtra").setOrden(5);
		tab_tramite.getColumna("fecha_recep_doc_smtra").setLectura(true);
		
		tab_tramite.getColumna("remitente_smtra");
		tab_tramite.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_tramite.getColumna("remitente_smtra").setOrden(6);
		tab_tramite.getColumna("remitente_smtra").setEstilo("width:200px");
		tab_tramite.getColumna("remitente_smtra").setLectura(true);
		
		tab_tramite.getColumna("destinatario_regdes");
		tab_tramite.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_tramite.getColumna("destinatario_regdes").setOrden(7);
		tab_tramite.getColumna("destinatario_regdes").setEstilo("width:200px");
		tab_tramite.getColumna("destinatario_regdes").setCombo(ser_nomina.getEmpleadoContrato("true,false"));
		tab_tramite.getColumna("destinatario_regdes").setLectura(true);
		
		tab_tramite.getColumna("cargo_destinatario_smtra").setNombreVisual("Cargo Destinatario");
		tab_tramite.getColumna("cargo_destinatario_smtra").setVisible(false);
		
		tab_tramite.getColumna("empresa_destinatario_smtra").setNombreVisual("Empresa Destinatario");
		tab_tramite.getColumna("empresa_destinatario_smtra").setVisible(false);
		
		tab_tramite.getColumna("asunto_smtra");
		tab_tramite.getColumna("asunto_smtra").setNombreVisual("Asunto-Comunicación");
		tab_tramite.getColumna("asunto_smtra").setEstilo("width:200px");
		tab_tramite.getColumna("asunto_smtra").setOrden(8);
		tab_tramite.getColumna("asunto_smtra").setLectura(true);
		
		tab_tramite.getColumna("ide_suest").setCombo("sum_estado_tramite", "ide_suest", "nombre_suest", "");
		tab_tramite.getColumna("ide_suest").setValorDefecto("PENDIENTE");
		tab_tramite.getColumna("ide_suest").setNombreVisual("Estado Trámite");
		tab_tramite.getColumna("ide_suest").setOrden(9);
		tab_tramite.getColumna("ide_suest").setLectura(true);
		
		tab_tramite.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_tramite.getColumna("ide_sumet").setNombreVisual("Etapa del Trámite");
		tab_tramite.getColumna("ide_sumet").setOrden(10);
		tab_tramite.getColumna("ide_sumet").setLectura(true);
				
		tab_tramite.getColumna("ide_geani").setNombreVisual("Año");
		tab_tramite.getColumna("ide_geani").setCombo(ser_anio.getAnioDetalle("true,false","true,false"));
		tab_tramite.getColumna("ide_geani").setOrden(11);
		tab_tramite.setCondicion("ide_geani=-1");
		
		tab_tramite.getColumna("adjunto_esttra").setVisible(false);
		tab_tramite.getColumna("cod_bpm_esttra").setVisible(false);
		tab_tramite.getColumna("activo_smtra").setVisible(false);
		tab_tramite.getColumna("doc_referencia_smtra").setVisible(false);
		tab_tramite.getColumna("trg_actualizacion_smtra").setVisible(false);
		
		tab_tramite.agregarRelacion(tab_tramite_bit);
		tab_tramite.dibujar();
		
		dibujarTramite_bit();
		
		PanelTabla pat_dat_gen=new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_tramite);
		PanelTabla pat_ing_sum=new PanelTabla();  
		pat_ing_sum.setPanelTabla(tab_tramite_bit);
		
		Division div_division = new Division();
		div_division.dividir2(pat_dat_gen, pat_ing_sum, "20%", "V"); 
		agregarComponente(div_division);
	}

	public void dibujarTramite_bit() {
		tab_tramite_bit.setId("tab_tramite_bit");
		tab_tramite_bit.setHeader("BITÁCORA DE TRÁMITES");
		tab_tramite_bit.setTabla("sum_tramite_bit", "ide_smtra_bit", 2);
		
		tab_tramite_bit.getColumna("ide_smtra_bit").setNombreVisual("Id");
		tab_tramite_bit.getColumna("ide_smtra_bit").setOrden(1);
		tab_tramite_bit.getColumna("ide_smtra_bit").setLectura(true);
		
		tab_tramite_bit.getColumna("ide_suest").setCombo("sum_estado_tramite", "ide_suest", "nombre_suest", "");
		tab_tramite_bit.getColumna("ide_suest").setValorDefecto("PENDIENTE");
		tab_tramite_bit.getColumna("ide_suest").setNombreVisual("Estado Trámite");
		tab_tramite_bit.getColumna("ide_suest").setOrden(2);
		tab_tramite_bit.getColumna("ide_suest").setLectura(true);
		
		tab_tramite_bit.getColumna("ide_sumet").setCombo("sum_etapa", "ide_sumet", "nombre_sumet", "");
		tab_tramite_bit.getColumna("ide_sumet").setValorDefecto("REGISTRO");
		tab_tramite_bit.getColumna("ide_sumet").setNombreVisual("Etapa del Trámite");
		tab_tramite_bit.getColumna("ide_sumet").setOrden(3);
		tab_tramite_bit.getColumna("ide_sumet").setLectura(true);
		
		tab_tramite_bit.getColumna("num_tramite_smtra").setNombreVisual("N° Trámite");
		tab_tramite_bit.getColumna("num_tramite_smtra").setOrden(4);
		tab_tramite_bit.getColumna("num_tramite_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("num_documento_smtra");
		tab_tramite_bit.getColumna("num_documento_smtra").setNombreVisual("N° Documento");
		tab_tramite_bit.getColumna("num_documento_smtra").setOrden(5);
		tab_tramite_bit.getColumna("num_documento_smtra").setLectura(true);
	
		tab_tramite_bit.getColumna("asunto_smtra");
		tab_tramite_bit.getColumna("asunto_smtra").setNombreVisual("Asunto-Comunicación");
		tab_tramite_bit.getColumna("asunto_smtra").setOrden(6);
		tab_tramite_bit.getColumna("asunto_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("DOC_REFERENCIA_smtra");
		tab_tramite_bit.getColumna("DOC_REFERENCIA_smtra").setNombreVisual("Documento de Referencia");
		tab_tramite_bit.getColumna("DOC_REFERENCIA_smtra").setOrden(7);
		tab_tramite_bit.getColumna("DOC_REFERENCIA_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("fecha_emision_doc_smtra");
		tab_tramite_bit.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha de Emisión del documento");
		tab_tramite_bit.getColumna("fecha_emision_doc_smtra").setOrden(8);
		tab_tramite_bit.getColumna("fecha_emision_doc_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("fecha_recep_doc_smtra");
		tab_tramite_bit.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha de Recepción del documento");
		tab_tramite_bit.getColumna("fecha_recep_doc_smtra").setOrden(9);
		tab_tramite_bit.getColumna("fecha_recep_doc_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("remitente_smtra");
		tab_tramite_bit.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_tramite_bit.getColumna("remitente_smtra").setOrden(10);
		tab_tramite_bit.getColumna("remitente_smtra").setEstilo("width:300px");
		tab_tramite_bit.getColumna("remitente_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("destinatario_regdes");
		tab_tramite_bit.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_tramite_bit.getColumna("destinatario_regdes").setOrden(11);
		tab_tramite_bit.getColumna("destinatario_regdes").setEstilo("width:300px");
		tab_tramite_bit.getColumna("destinatario_regdes").setCombo(ser_nomina.getEmpleadoContrato("true,false"));
		tab_tramite_bit.getColumna("destinatario_regdes").setLectura(true);
		
		tab_tramite_bit.getColumna("cargo_destinatario_smtra");
		tab_tramite_bit.getColumna("cargo_destinatario_smtra").setNombreVisual("Cargo Destinatario");
		tab_tramite_bit.getColumna("cargo_destinatario_smtra").setOrden(12);
		tab_tramite_bit.getColumna("cargo_destinatario_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("empresa_destinatario_smtra");
		tab_tramite_bit.getColumna("empresa_destinatario_smtra").setNombreVisual("Empresa Destinatario");
		tab_tramite_bit.getColumna("empresa_destinatario_smtra").setOrden(13);
		tab_tramite_bit.getColumna("empresa_destinatario_smtra").setLectura(true);
		
		tab_tramite_bit.getColumna("ide_smtra").setVisible(false);
		tab_tramite_bit.getColumna("cod_bpm_esttra").setVisible(false);
		tab_tramite_bit.getColumna("adjunto_esttra").setVisible(false);
		tab_tramite_bit.getColumna("num_hojas_smtra").setVisible(false);
		tab_tramite_bit.getColumna("doc_respuesta_smtra").setVisible(false);
		tab_tramite_bit.getColumna("doc_interno_smtra").setVisible(false);
		tab_tramite_bit.getColumna("activo_smtra").setVisible(false);
		tab_tramite_bit.getColumna("ide_geani").setVisible(false);
		tab_tramite_bit.setCampoForanea("ide_smtra");
		tab_tramite_bit.dibujar();		
	}
		
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("Información solo de consulta", "");
		return;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("No se puede guardar los cambios Información solo de Consulta", "");
		return; 
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("No se puede eliminar el resgistro! Información solo de Consulta", "");
		return;
	}

	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
				tab_tramite.setCondicion("ide_geani="+com_anio.getValue());
				tab_tramite.ejecutarSql();
		}
		else{
			tab_tramite.setCondicion("ide_geani=-1");
			tab_tramite.ejecutarSql();
		}
	}
	
	//Gets y Sets
	public Tabla getTab_tramite_bit() {
		return tab_tramite_bit;
	}
	public void setTab_tramite_bit(Tabla tab_tramite_bit) {
		this.tab_tramite_bit = tab_tramite_bit;
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

	public Tabla getTab_tramite() {
		return tab_tramite;
	}
	public void setTab_tramite(Tabla tab_tramite) {
		this.tab_tramite = tab_tramite;
	}

	public ServicioDestinatario getSer_nomina() {
		return ser_nomina;
	}
	public void setSer_nomina(ServicioDestinatario ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public ServicioSumTramite getSer_tramite() {
		return ser_tramite;
	}
	public void setSer_tramite(ServicioSumTramite ser_tramite) {
		this.ser_tramite = ser_tramite;
	}

	public ServicioAnio getSer_anio() {
		return ser_anio;
	}
	public void setSer_anio(ServicioAnio ser_anio) {
		this.ser_anio = ser_anio;
	}

	public Tabla getTab_tramiteRespuesta_bit() {
		return tab_tramiteRespuesta_bit;
	}
	public void setTab_tramiteRespuesta_bit(Tabla tab_tramiteRespuesta_bit) {
		this.tab_tramiteRespuesta_bit = tab_tramiteRespuesta_bit;
	}

}
