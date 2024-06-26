package paq_sumillas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioReporteSumillas;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;

public class pre_rep_sumillas_alertas extends Pantalla{
	
	private Tabla tab_ejecucionP = new Tabla();
	private Combo com_anio = new Combo();
	private Combo com_mes_ini = new Combo();
	private Combo com_mes_fin = new Combo();
    
    Date fecha = new Date();
    Calendar calendario = Calendar.getInstance();
    

    @EJB
    private ServicioReporteSumillas ser_reporte = (ServicioReporteSumillas) utilitario.instanciarEJB(ServicioReporteSumillas.class);

	public pre_rep_sumillas_alertas(){
		calendario.setTime(fecha);
		calendario.get(Calendar.YEAR);
		
		bar_botones.limpiar();
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		com_anio.setCombo("select distinct anio id_anio,anio from rep_sum_tramite_mes_anio order by anio");
		com_anio.setValue(1);
		//com_anio.setMetodo("actualizaMes");
		com_anio.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes_ini.setCombo(getListaMeses());
		com_mes_ini.setValue(1);
		//com_anio.setMetodo("actualizaMes");
		com_mes_ini.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Desde:"));
		bar_botones.agregarComponente(com_mes_ini);
		
		com_mes_fin.setCombo(getListaMeses());
		com_mes_fin.setValue(1);
		//com_anio.setMetodo("actualizaMes");
		com_mes_fin.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Hasta:"));
		bar_botones.agregarComponente(com_mes_fin);
		
		Boton bot_reimprimir=new Boton();
		bot_reimprimir.setIcon("ui-icon-image"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reimprimir.setValue("Visualizar");
		bot_reimprimir.setMetodo("reimprime");
		bar_botones.agregarBoton(bot_reimprimir);
		
		tab_ejecucionP.setId("tab_ejecucionP");
		tab_ejecucionP.setSql(ser_reporte.getDetalleAlertas("01/01/"+calendario.get(Calendar.YEAR), "31/12/"+calendario.get(Calendar.YEAR)));
		tab_ejecucionP.getColumna("semaforo").setNombreVisual("ATENCIÓN");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("PERIODO");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("DÍAS PARA FECHA LÍMITE");
		tab_ejecucionP.getColumna("num_tramite_smtra").setNombreVisual("Nº TRÁMITE");
		
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
        
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_ejecucionP);    
		Division div_division = new Division();  
		div_division.dividir1(pat_dat_gen);  
		agregarComponente(div_division); 
	}
	
	public List getListaMeses() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"1", "Enero"};
        Object fila2[] = {"2", "Febrero"};
        Object fila3[] = {"3", "Marzo"};
        Object fila4[] = {"4", "Abril"};
        Object fila5[] = {"5", "Mayo"};
        Object fila6[] = {"6", "Junio"};
        Object fila7[] = {"7", "Julio"};
        Object fila8[] = {"8", "Agosto"};
        Object fila9[] = {"9", "Septiembre"};
        Object fila10[] = {"10", "Octubre"};
        Object fila11[] = {"11", "Noviembre"};
        Object fila12[] = {"12", "Diciembre"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		lista.add(fila7);
		lista.add(fila8);
		lista.add(fila9);
		lista.add(fila10);
		lista.add(fila11);
		lista.add(fila12);

		return lista;
	}
	
	public void reimprime()
	{
		tab_ejecucionP.setId("tab_ejecucionP");
		//tab_ejecucionP.setTabla("sum_tramite","ide_tramite_smtra",1);	
		tab_ejecucionP.setSql(ser_reporte.getDetalleAlertas("01/"+pckUtilidades.CConversion.CInt(com_mes_ini.getValue()) +"/"+pckUtilidades.CConversion.CInt(com_anio.getValue()), "31/" +pckUtilidades.CConversion.CInt(com_mes_fin.getValue()) +"/"+pckUtilidades.CConversion.CInt(com_anio.getValue())));
		tab_ejecucionP.getColumna("semaforo").setNombreVisual("ATENCIÓN");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("PERIODO");
		tab_ejecucionP.getColumna("fecha_limite_smtre").setNombreVisual("DÍAS PARA FECHA LÍMITE");
		tab_ejecucionP.getColumna("num_tramite_smtra").setNombreVisual("Nº TRÁMITE");
		
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

	public Combo getCom_mes_ini() {
		return com_mes_ini;
	}

	public void setCom_mes_ini(Combo com_mes_ini) {
		this.com_mes_ini = com_mes_ini;
	}

	public Combo getCom_mes_fin() {
		return com_mes_fin;
	}

	public void setCom_mes_fin(Combo com_mes_fin) {
		this.com_mes_fin = com_mes_fin;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Calendar getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendar calendario) {
		this.calendario = calendario;
	}

	public ServicioReporteSumillas getSer_reporte() {
		return ser_reporte;
	}

	public void setSer_reporte(ServicioReporteSumillas ser_reporte) {
		this.ser_reporte = ser_reporte;
	}

	
}
