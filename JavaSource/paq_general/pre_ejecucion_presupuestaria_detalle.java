package paq_general;

import javax.ejb.EJB;

import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_ejecucion_presupuestaria_detalle extends Pantalla{
	
	private Tabla tab_ejecucionP = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Check che_grupo_fuentes=new Check();
	private boolean grupos_fuentes=false;
	private Check che_grupo_compromisos=new Check();
	private boolean grupos_compromisos=false;
	private Check che_grupo_certificaciones=new Check();
	private boolean grupos_certificaciones=false;
	private Check che_grupo_comprobantes=new Check();
	private boolean grupos_comprobantes=false;
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);

	public pre_ejecucion_presupuestaria_detalle(){

		bar_botones.limpiar();

		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);
		
		che_grupo_fuentes.setId("che_grupo_fuentes");
		che_grupo_fuentes.setMetodoChange("grupos_fuentes");
		Etiqueta eti_grupo_fuent=new Etiqueta("AGRUPAR FUENTES");
		bar_botones.agregarComponente(eti_grupo_fuent);
		bar_botones.agregarComponente(che_grupo_fuentes);
		
		che_grupo_certificaciones.setId("che_grupo_certificaciones");
		che_grupo_certificaciones.setMetodoChange("grupos_certificaciones");
		Etiqueta eti_grupo_cert=new Etiqueta("AGRUPAR CERTIFICACIONES");
		bar_botones.agregarComponente(eti_grupo_cert);
		bar_botones.agregarComponente(che_grupo_certificaciones);
		
		che_grupo_compromisos.setId("che_grupo_compromisos");
		che_grupo_compromisos.setMetodoChange("grupos_compromisos");
		Etiqueta eti_grupo_comp=new Etiqueta("AGRUPAR COMPROMISOS");
		bar_botones.agregarComponente(eti_grupo_comp);
		bar_botones.agregarComponente(che_grupo_compromisos);
		
		che_grupo_comprobantes.setId("che_grupo_comprobantes");
		che_grupo_comprobantes.setMetodoChange("grupos_comprobantes");
		Etiqueta eti_grupo=new Etiqueta("AGRUPAR COMPROBANTES");
		bar_botones.agregarComponente(eti_grupo);
		bar_botones.agregarComponente(che_grupo_comprobantes);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaEjecucion");
		bar_botones.agregarBoton(bot_actualiza);
    			
		tab_ejecucionP.setId("tab_ejecucionP");
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria_detallado("1900-01-01","1900-01-01",grupos_fuentes,grupos_compromisos,grupos_comprobantes));
		//tab_ejecucionP.getColumna("ide_prpoa").setVisible(false);
		tab_ejecucionP.getColumna("ide_prcla").setVisible(false);
		tab_ejecucionP.getColumna("fecha_inicial").setLongitud(15);
		tab_ejecucionP.getColumna("fecha_corte").setLongitud(15);
		tab_ejecucionP.getColumna("ide_prpoa").setFiltro(true);
		tab_ejecucionP.getColumna("codigo_subactividad").setFiltro(true);
		tab_ejecucionP.getColumna("codigo_clasificador_prcla").setFiltro(true);
		tab_ejecucionP.getColumna("nro_certificacion_prcer").setFiltro(true);
		tab_ejecucionP.getColumna("nro_certificacion_prcer").setLongitud(28);
		tab_ejecucionP.getColumna("nro_compromiso").setFiltro(true);
		tab_ejecucionP.getColumna("nro_compromiso").setLongitud(25);
		tab_ejecucionP.getColumna("nro_comprobante").setFiltro(true);
		tab_ejecucionP.getColumna("nro_comprobante").setLongitud(28);
		tab_ejecucionP.getColumna("descripcion_clasificador_prcla").setNombreVisual("DESCRIPCION PARTIDA");
		tab_ejecucionP.getColumna("fecha_corte").setLongitud(15);
		tab_ejecucionP.setColumnaSuma("inicial,reforma,codificado,certificado,compromiso,devengado");
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_ejecucionP);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
	}
	
	public void cargaEjecucion(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria_detallado(fecha_inicial,fecha_final,grupos_fuentes,grupos_compromisos,grupos_comprobantes));
		tab_ejecucionP.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionP");
	}
	
	public void limpiar(){
		
		tab_ejecucionP.limpiar();
		
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria_detallado("1900-01-01","1900-01-01",grupos_fuentes,grupos_compromisos,grupos_comprobantes));
		tab_ejecucionP.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionP");
	}
	
	public void grupos_fuentes(){
		if(che_grupo_fuentes.getValue().toString().equalsIgnoreCase("true")){
			che_grupo_compromisos.setValue(true);
			grupos_compromisos=true;
			
			che_grupo_comprobantes.setValue(true);
			grupos_comprobantes=true;
			
			grupos_fuentes=true;
		}
		else{
			che_grupo_compromisos.setValue(false);
			grupos_compromisos=false;
			
			che_grupo_comprobantes.setValue(false);
			grupos_comprobantes=false;
			grupos_fuentes=false;
		}

	}
	
	public void grupos_certificaciones(){
		if(che_grupo_certificaciones.getValue().toString().equalsIgnoreCase("true")){
			che_grupo_certificaciones.setValue(true);
			grupos_certificaciones=true;
			grupos_comprobantes=true;
			grupos_compromisos=true;
		}
		else{
			che_grupo_certificaciones.setValue(false);
			grupos_certificaciones=false;
			grupos_comprobantes=false;
			grupos_compromisos=false;
		}

	}

	public void grupos_compromisos(){
		if(che_grupo_compromisos.getValue().toString().equalsIgnoreCase("true")){
			che_grupo_comprobantes.setValue(true);
			grupos_comprobantes=true;
			
			grupos_compromisos=true;
		}
		else{
			che_grupo_comprobantes.setValue(false);
			grupos_comprobantes=false;
			
			grupos_compromisos=false;
		}

	}
	
	public void grupos_comprobantes(){
		if(che_grupo_comprobantes.getValue().toString().equalsIgnoreCase("true")){
			grupos_comprobantes=true;
		}
		else{
			grupos_comprobantes=false;
		}

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


}
