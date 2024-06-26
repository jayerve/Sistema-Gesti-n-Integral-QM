package paq_sumillas;

import javax.ejb.EJB;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioReporteSumillas;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_rep_sumillas_detallado extends Pantalla{
	
	private Tabla tab_ejecucionP = new Tabla();
	private Combo com_anio = new Combo();
	private Calendario cal_fecha_ini = new Calendario();
	private Calendario cal_fecha_fin = new Calendario(); 

    @EJB
    private ServicioReporteSumillas ser_reporte = (ServicioReporteSumillas) utilitario.instanciarEJB(ServicioReporteSumillas.class);

	public pre_rep_sumillas_detallado(){
		bar_botones.limpiar();
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		//com_anio.setMetodo("actualizaMes");
		cal_fecha_ini.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(new Etiqueta(" Desde:"));
		bar_botones.agregarComponente(cal_fecha_ini);
		
	
		bar_botones.agregarComponente(new Etiqueta(" Hasta:"));
		cal_fecha_ini.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_ini);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_fin.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_fin);
		
		
		Boton bot_reimprimir=new Boton();
		bot_reimprimir.setIcon("ui-icon-image"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reimprimir.setValue("Visualizar");
		bot_reimprimir.setMetodo("reimprime");
		bar_botones.agregarBoton(bot_reimprimir);
		
		tab_ejecucionP.setId("tab_ejecucionP");
		//tab_ejecucionP.setTabla("sum_tramite","ide_tramite_smtra",1);	
		tab_ejecucionP.setSql(ser_reporte.getDetalleSumillas(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		
		tab_ejecucionP.getColumna("num_tramite_smtra").setNombreVisual("No Tramite");
		tab_ejecucionP.getColumna("num_documento_smtra").setNombreVisual("No Documento");
		tab_ejecucionP.getColumna("num_hojas_smtra").setNombreVisual("No Hojas");
		tab_ejecucionP.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha emisión doc");
		tab_ejecucionP.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha recepción doc");
		tab_ejecucionP.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_ejecucionP.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_ejecucionP.getColumna("asunto_smtra").setNombreVisual("Asunto");
		tab_ejecucionP.getColumna("fecha_sumilla_smtre").setNombreVisual("Fecha sumilla");
		tab_ejecucionP.getColumna("disposicion_smtre").setNombreVisual("Dispocisión");
		tab_ejecucionP.getColumna("gerencia").setNombreVisual("Gerencia");
		tab_ejecucionP.getColumna("coordinacion").setNombreVisual("Coordinación");
		tab_ejecucionP.getColumna("nombre_sumpr").setNombreVisual("Prioridad");
		tab_ejecucionP.getColumna("recibido_smtre").setNombreVisual("Recibido por");
		tab_ejecucionP.getColumna("fecha_entrega_smtre").setNombreVisual("Fecha entrega");
		tab_ejecucionP.getColumna("fecha_vencimiento_smtre").setNombreVisual("Fecha vencimiento");
		tab_ejecucionP.getColumna("fecha_documento_smtre").setNombreVisual("Fecha del documento");
		tab_ejecucionP.getColumna("num_documento_smtre").setNombreVisual("Documento generado");
		tab_ejecucionP.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_ejecucionP.getColumna("cargo_destinatario_smtre").setNombreVisual("Cargo");
		tab_ejecucionP.getColumna("asunto_smtre").setNombreVisual("Asunto");
		tab_ejecucionP.getColumna("fecha_entrega_res_smtre").setNombreVisual("Fecha entrega doc");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("Fecha límite entrega");
		tab_ejecucionP.getColumna("fecha_limite_prog_smtre").setNombreVisual("Fecha límite prorroga");
		tab_ejecucionP.getColumna("prorrogas").setNombreVisual("No. Prorrogas");
		tab_ejecucionP.getColumna("estado_sumilla").setNombreVisual("Estado de proceso");
		tab_ejecucionP.getColumna("gabinete_smtre").setNombreVisual("Gabinete");
		
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
        
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_ejecucionP);    
		Division div_division = new Division();  
		div_division.dividir1(pat_dat_gen);  
		agregarComponente(div_division); 
	}
	
	public void reimprime()
	{
		tab_ejecucionP.setId("tab_ejecucionP");
		//tab_ejecucionP.setTabla("sum_tramite","ide_tramite_smtra",1);	
		tab_ejecucionP.setSql(ser_reporte.getDetalleSumillas(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		
		tab_ejecucionP.getColumna("num_tramite_smtra").setNombreVisual("No Tramite");
		tab_ejecucionP.getColumna("num_documento_smtra").setNombreVisual("No Documento");
		tab_ejecucionP.getColumna("num_hojas_smtra").setNombreVisual("No Hojas");
		tab_ejecucionP.getColumna("fecha_emision_doc_smtra").setNombreVisual("Fecha emisión doc");
		tab_ejecucionP.getColumna("fecha_recep_doc_smtra").setNombreVisual("Fecha recepción doc");
		tab_ejecucionP.getColumna("remitente_smtra").setNombreVisual("Remitente");
		tab_ejecucionP.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_ejecucionP.getColumna("asunto_smtra").setNombreVisual("Asunto");
		tab_ejecucionP.getColumna("fecha_sumilla_smtre").setNombreVisual("Fecha sumilla");
		tab_ejecucionP.getColumna("disposicion_smtre").setNombreVisual("Dispocisión");
		tab_ejecucionP.getColumna("gerencia").setNombreVisual("Gerencia");
		tab_ejecucionP.getColumna("coordinacion").setNombreVisual("Coordinación");
		tab_ejecucionP.getColumna("nombre_sumpr").setNombreVisual("Prioridad");
		tab_ejecucionP.getColumna("recibido_smtre").setNombreVisual("Recibido por");
		tab_ejecucionP.getColumna("fecha_entrega_smtre").setNombreVisual("Fecha entrega");
		tab_ejecucionP.getColumna("fecha_vencimiento_smtre").setNombreVisual("Fecha vencimiento");
		tab_ejecucionP.getColumna("fecha_documento_smtre").setNombreVisual("Fecha del documento");
		tab_ejecucionP.getColumna("num_documento_smtre").setNombreVisual("Documento generado");
		tab_ejecucionP.getColumna("destinatario_regdes").setNombreVisual("Destinatario");
		tab_ejecucionP.getColumna("cargo_destinatario_smtre").setNombreVisual("Cargo");
		tab_ejecucionP.getColumna("asunto_smtre").setNombreVisual("Asunto");
		tab_ejecucionP.getColumna("fecha_entrega_res_smtre").setNombreVisual("Fecha entrega doc");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("Fecha límite entrega");
		tab_ejecucionP.getColumna("fecha_limite_prog_smtre").setNombreVisual("Fecha límite prorroga");
		tab_ejecucionP.getColumna("prorrogas").setNombreVisual("No. Prorrogas");
		tab_ejecucionP.getColumna("estado_sumilla").setNombreVisual("Estado de proceso");
		tab_ejecucionP.getColumna("gabinete_smtre").setNombreVisual("Gabinete");
		
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
        utilitario.addUpdate("tab_sumillas");
        
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_ejecucionP() {
		return tab_ejecucionP;
	}

	public void setTab_ejecucionP(Tabla tab_ejecucionP) {
		this.tab_ejecucionP = tab_ejecucionP;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public ServicioReporteSumillas getSer_reporte() {
		return ser_reporte;
	}

	public void setSer_reporte(ServicioReporteSumillas ser_reporte) {
		this.ser_reporte = ser_reporte;
	}

	public Calendario getCal_fecha_ini() {
		return cal_fecha_ini;
	}

	public void setCal_fecha_ini(Calendario cal_fecha_ini) {
		this.cal_fecha_ini = cal_fecha_ini;
	}

	public Calendario getCal_fecha_fin() {
		return cal_fecha_fin;
	}

	public void setCal_fecha_fin(Calendario cal_fecha_fin) {
		this.cal_fecha_fin = cal_fecha_fin;
	}
	
}
