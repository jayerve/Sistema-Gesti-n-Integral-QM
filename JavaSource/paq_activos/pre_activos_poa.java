package paq_activos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import paq_activos.ejb.ServicioActivos;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;

public class pre_activos_poa extends Pantalla {

	private Tabla tab_consulta_activo = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private Map<String, Serializable> p_parametros = new HashMap<String, Serializable>();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	@EJB
	private ServicioActivos ser_activo = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

	/**
	 * Dibujar Reportes
	 */
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	/**
	 * Aceptar Reportes
	 */
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Reporte Total")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "Reporte Total");
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}

	public pre_activos_poa() {
		bar_botones.limpiar();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);
		System.out.println("Pantalla pre_activos_poa ");
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setTabla("afi_poa", "ide_afpoa", 1);

		
		
		tab_consulta_activo.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta_activo);

		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
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

	public Tabla getTab_consulta_activo() {
		return tab_consulta_activo;
	}

	public void setTab_consulta_activo(Tabla tab_consulta_activo) {
		this.tab_consulta_activo = tab_consulta_activo;
	}

	/**
	 * @return the rep_reporte
	 */
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	/**
	 * @param rep_reporte the rep_reporte to set
	 */
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	/**
	 * @return the self_reporte
	 */
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	/**
	 * @param self_reporte the self_reporte to set
	 */
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	public Map<String, Serializable> getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map<String, Serializable> p_parametros) {
		this.p_parametros = p_parametros;
	}

	public ServicioActivos getSer_activo() {
		return ser_activo;
	}

	public void setSer_activo(ServicioActivos ser_activo) {
		this.ser_activo = ser_activo;
	}


}
