package paq_activos;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_activos.ejb.ServicioActivos;
import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;

public class pre_activos_consulta_por_custodio extends Pantalla {

	private Tabla tab_consulta_activo = new Tabla();

	private AutoCompletar aut_empleado = new AutoCompletar();
	
	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	@EJB
	private ServicioActivos ser_activo = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);
	@EJB
	private ServicioEmpleado serv_empleado;

	public pre_activos_consulta_por_custodio() {
		bar_botones.limpiar();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP, APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");

		Etiqueta eti_colaborador = new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);

		System.out.println("Pantalla pre_activos_consulta_por_custodio ");
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setSql(ser_activo.getConsultaActivosPorCustodio("0"));
		// tab_consulta_activo.getColumna("codigo").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo").setLongitud(5);

		// tab_consulta_activo.getColumna("item_presupuestario").setFiltroContenido();
		tab_consulta_activo.getColumna("item_presupuestario").setLongitud(10);
		// tab_consulta_activo.getColumna("nombre_catalogo").setFiltroContenido();
		tab_consulta_activo.getColumna("nombre_catalogo").setLongitud(100);
		// tab_consulta_activo.getColumna("descripcion_caracteristicas").setFiltroContenido();
		tab_consulta_activo.getColumna("descripcion_caracteristicas").setLongitud(150);
		// tab_consulta_activo.getColumna("codigo_anterior").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo_anterior").setLongitud(35);
		// tab_consulta_activo.getColumna("color").setFiltroContenido();
		tab_consulta_activo.getColumna("color").setLongitud(10);
		// tab_consulta_activo.getColumna("marca").setFiltroContenido();
		tab_consulta_activo.getColumna("marca").setLongitud(10);
		// tab_consulta_activo.getColumna("serie").setFiltroContenido();
		tab_consulta_activo.getColumna("serie").setLongitud(10);
		// tab_consulta_activo.getColumna("modelo").setFiltroContenido();
		tab_consulta_activo.getColumna("modelo").setLongitud(10);
		// tab_consulta_activo.getColumna("chasis").setFiltroContenido();
		tab_consulta_activo.getColumna("chasis").setLongitud(10);
		// tab_consulta_activo.getColumna("motor").setFiltroContenido();
		tab_consulta_activo.getColumna("motor").setLongitud(10);
		// tab_consulta_activo.getColumna("placa").setFiltroContenido();
		tab_consulta_activo.getColumna("placa").setLongitud(10);
		// tab_consulta_activo.getColumna("componentes").setFiltroContenido();
		tab_consulta_activo.getColumna("componentes").setLongitud(150);

		tab_consulta_activo.setLectura(true);
		tab_consulta_activo.setPaginator(true);
		tab_consulta_activo.dibujar();
		tab_consulta_activo.setRows(20);
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta_activo);

		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
	}

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
		if (aut_empleado.getValor() != null) {
			if (rep_reporte.getReporteSelecionado().equals("Consulta Custodio")) {
				if (rep_reporte.isVisible()) {
				
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					p_parametros.put("titulo", "REPORTE DE BIENES VINCULADOS AL CUSTODIO");
					p_parametros.put("ide_gtemp", pckUtilidades.CConversion.CInt(aut_empleado.getValor().toString()));
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					self_reporte.dibujar();
				}
			}
		} else {
			utilitario.agregarMensajeError("Activos de Custodio", "Debe de realizar la busqueda de un empleado");
			return;
		}
	}

	public void limpiar() {
		aut_empleado.limpiar();
		tab_consulta_activo.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el
												// autocompletar
	}

	public void filtrarEmpleado(SelectEvent evt) {
		aut_empleado.onSelect(evt);
		tab_consulta_activo.setSql(ser_activo.getConsultaActivosPorCustodio(aut_empleado.getValor()));
		tab_consulta_activo.ejecutarSql();
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

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
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