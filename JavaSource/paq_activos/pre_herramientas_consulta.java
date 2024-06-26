package paq_activos;

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

public class pre_herramientas_consulta extends Pantalla {

	private Tabla tab_consulta_herramienta = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
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
		if (rep_reporte.getReporteSelecionado().equals("Reporte global de herramientas")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "REPORTE DE HERRAMIENTAS GLOBAL");
				//p_parametros.put("ide_afdoc", pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc")));
				p_parametros.put("pjefe_activos", utilitario.getVariable("p_jefe_activos_fijos"));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}else if (rep_reporte.getReporteSelecionado().equals("Reporte por bodega de herramientas")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("titulo", "REPORTE DE HERRAMIENTAS POR BODEGA");
				//p_parametros.put("ide_afdoc", pckUtilidades.CConversion.CInt(tab_cabecera.getValor("ide_afdoc")));
				p_parametros.put("pjefe_activos", utilitario.getVariable("p_jefe_activos_fijos"));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}

	public pre_herramientas_consulta() {
		bar_botones.limpiar();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);
		System.out.println("Pantalla pre_activos_consulta ");
		tab_consulta_herramienta.setId("tab_consulta_herramienta");
		tab_consulta_herramienta.setSql(ser_activo.getHerramientasEstado());

		tab_consulta_herramienta.setLectura(true);
		tab_consulta_herramienta.dibujar();
		tab_consulta_herramienta.setRows(30);
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta_herramienta);

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

	public Tabla getTab_consulta_herramienta() {
		return tab_consulta_herramienta;
	}

	public void setTab_consulta_herramienta(Tabla tab_consulta_herramienta) {
		this.tab_consulta_herramienta = tab_consulta_herramienta;
	}

	/**
	 * @return the rep_reporte
	 */
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	/**
	 * @param rep_reporte
	 *            the rep_reporte to set
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
	 * @param self_reporte
	 *            the self_reporte to set
	 */
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	/**
	 * @return the p_parametros
	 */
	public Map getP_parametros() {
		return p_parametros;
	}

	/**
	 * @param p_parametros
	 *            the p_parametros to set
	 */
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
}
