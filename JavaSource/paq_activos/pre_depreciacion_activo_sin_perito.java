package paq_activos;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_activos.ejb.ServicioActivos;
import paq_sistema.aplicacion.Pantalla;

public class pre_depreciacion_activo_sin_perito extends Pantalla {
	private Tabla tab_depreciacion = new Tabla();
	private Calendario cal_fecha_corte = new Calendario();
	
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

	public pre_depreciacion_activo_sin_perito() {
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.agregarComponente(new Etiqueta("Fecha corte: "));
		cal_fecha_corte.setValue(utilitario.getDate());
		bar_botones.agregarComponente(cal_fecha_corte);
		
		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		
		Boton bot_calcular = new Boton();
		bot_calcular.setId("bot_calcular");
		bot_calcular.setTitle("CALCULAR");
		bot_calcular.setValue("CALCULAR");
		bot_calcular.setMetodo("calcular");
		bar_botones.agregarBoton(bot_calcular);
		

		tab_depreciacion.setId("tab_depreciacion");
		tab_depreciacion.setSql(ser_activos.getActivoDepreciacionSinPerito("1997-01-01"));
		tab_depreciacion.setNumeroTabla(1);

		tab_depreciacion.setPaginator(true);
		tab_depreciacion.setRows(20);
		tab_depreciacion.setCampoPrimaria("ide_afact");
		tab_depreciacion.setLectura(true);
		
		tab_depreciacion.getColumna("dias_periodo").setVisible(false);
		tab_depreciacion.getColumna("depreciacion_periodo").setVisible(false);
		tab_depreciacion.dibujar();
		PanelTabla pat_panel = new PanelTabla();

		pat_panel.setPanelTabla(tab_depreciacion);
		
		Division div_division = new Division();
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);

		
	}

	public void calcular() {
		String fecha_corte = cal_fecha_corte.getFecha();
		tab_depreciacion.setSql(ser_activos.getActivoDepreciacionSinPerito(fecha_corte));
		tab_depreciacion.setRows(20);
		tab_depreciacion.ejecutarSql();
		utilitario.addUpdate("tab_depreciacion");
	}
	
	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		String fecha_corte = cal_fecha_corte.getFecha();
		System.out.println("fecha_corte:"+ fecha_corte);
		System.out.println("Reporte seleccionado: "+ rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Reporte de depreciación")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "REPORTE DE DEPRECIACIÓN");
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("fecha_corte", fecha_corte);
				
				//p_parametros.put("num_ingreso", pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")));
				//p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				//System.out.println(pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")) + " aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Reporte de depreciación por partida")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "REPORTE DE DEPRECIACIÓN");
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("fecha_corte", fecha_corte);
				
				//p_parametros.put("num_ingreso", pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")));
				//p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				// p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
				//System.out.println(pckUtilidades.CConversion.CInt(tab_bodega.getValor("ide_bobod")) + " aceptarReporte " + rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void guardar() {
		String fecha_corte = cal_fecha_corte.getFecha();
		tab_depreciacion.setSql(ser_activos.getActualizarDepreciacionSinPerito(fecha_corte));
		tab_depreciacion.setRows(20);
		tab_depreciacion.ejecutarSql();
		utilitario.addUpdate("tab_depreciacion");
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}

	public Tabla getTab_depreciacion() {
		return tab_depreciacion;
	}

	public void setTab_depreciacion(Tabla tab_depreciacion) {
		this.tab_depreciacion = tab_depreciacion;
	}

	public Calendario getCal_fecha_corte() {
		return cal_fecha_corte;
	}

	public void setCal_fecha_corte(Calendario cal_fecha_corte) {
		this.cal_fecha_corte = cal_fecha_corte;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
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

	public ServicioActivos getSer_activos() {
		return ser_activos;
	}

	public void setSer_activos(ServicioActivos ser_activos) {
		this.ser_activos = ser_activos;
	}

		

}
