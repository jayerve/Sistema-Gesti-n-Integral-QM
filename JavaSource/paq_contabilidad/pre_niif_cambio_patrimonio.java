package paq_contabilidad;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_niif_cambio_patrimonio extends Pantalla{

	private Tabla tab_cambio_patrimonio=new Tabla();
	private Combo com_anio=new Combo();
	private Combo com_mesI=new Combo();
	private Combo com_mesF=new Combo();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_niif_cambio_patrimonio() {

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));	
		com_anio.setValue(utilitario.getVariable("p_anio_vigente"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mesI.setCombo(ser_contabilidad.getMes("true,false"));	
		com_mesI.setValue("1");
		com_mesI.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes Inicial:"));
		bar_botones.agregarComponente(com_mesI);
		
		com_mesF.setCombo(ser_contabilidad.getMes("true,false"));	
		com_mesF.setValue("1");
		com_mesF.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes Final:"));
		bar_botones.agregarComponente(com_mesF);
		
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		
		bar_botones.agregarReporte();
		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);	

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
				
		tab_cambio_patrimonio.setId("tab_cambio_patrimonio");  
		tab_cambio_patrimonio.setSql(ser_contabilidad.niif_patrimonio("-1","-1","-1"));
		tab_cambio_patrimonio.setColumnaSuma("saldo_inicial,incremento,decremento,saldo_final"); 
		tab_cambio_patrimonio.setRows(20);
		tab_cambio_patrimonio.setLectura(true);
		tab_cambio_patrimonio.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_cambio_patrimonio);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar");
		bot_actualizar.setMetodo("generar");
		bar_botones.agregarBoton(bot_actualizar);	
		
		
	}
	
	
	public void generar(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(com_mesI.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes Inicial", "");
			return;			
		}
		
		if(com_mesF.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes Final", "");
			return;			
		}
		
		tab_cambio_patrimonio.setSql(ser_contabilidad.niif_patrimonio(com_anio.getValue().toString(),com_mesI.getValue().toString(),com_mesF.getValue().toString()));
		tab_cambio_patrimonio.ejecutarSql();
		utilitario.addUpdate("tab_cambio_patrimonio");
		utilitario.agregarMensajeInfo("IMPORTANTE", "Genere el Balance General antes de imprimir el reporte de Cambio de Patrimonio.");
		
	}

	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_cambio_patrimonio.limpiar();	
		utilitario.addUpdate("tab_cambio_patrimonio");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//tab_cambio_patrimonio.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		rep_reporte.dibujar();
	}
	
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		
		if(rep_reporte.getReporteSelecionado().equals("NIIF Cambio Patrimonio")){
			TablaGenerica tab_anio =utilitario.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			int anio = pckUtilidades.CConversion.CInt(tab_anio.getValor("detalle_geani"));
			int mesI = pckUtilidades.CConversion.CInt(com_mesI.getValue().toString());
			int mesF = pckUtilidades.CConversion.CInt(com_mesF.getValue().toString());
			String fecha1=(utilitario.DateStringFormat(tab_cambio_patrimonio.getValor(tab_cambio_patrimonio.getTotalFilas()-1,"fecha_inicial"))).toLowerCase();
			String fecha2=utilitario.DateStringFormat(tab_cambio_patrimonio.getValor(tab_cambio_patrimonio.getTotalFilas()-1,"fecha_final")).toLowerCase();
			
			String cabecera = "Del "+fecha1.substring(0, fecha1.length()-8)+" al "+fecha2;
			
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				map_parametros.put("titulo","ESTADO DE CAMBIOS EN EL PATRIMONIO");
				map_parametros.put("contador_general",  utilitario.getVariable("p_nombre_contador"));				
				map_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				map_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	
				map_parametros.put("cargo_gerente",  utilitario.getVariable("p_cargo_gerente"));
				map_parametros.put("gerente",  utilitario.getVariable("p_nombre_gerente"));	
				map_parametros.put("anio", anio);
				map_parametros.put("mesI", mesI);
				map_parametros.put("mesF", mesF);
				map_parametros.put("cabecera", cabecera);

				sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
				sel_rep.dibujar();
			}
		} 
	}
	
	

	public Tabla getTab_cambio_patrimonio() {
		return tab_cambio_patrimonio;
	}


	public void setTab_cambio_patrimonio(Tabla tab_cambio_patrimonio) {
		this.tab_cambio_patrimonio = tab_cambio_patrimonio;
	}


	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSel_rep() {
		return sel_rep;
	}
	public void setSel_rep(SeleccionFormatoReporte sel_rep) {
		this.sel_rep = sel_rep;
	}


	public Combo getCom_mesI() {
		return com_mesI;
	}


	public void setCom_mesI(Combo com_mesI) {
		this.com_mesI = com_mesI;
	}


	public Combo getCom_mesF() {
		return com_mesF;
	}


	public void setCom_mesF(Combo com_mesF) {
		this.com_mesF = com_mesF;
	}

}
