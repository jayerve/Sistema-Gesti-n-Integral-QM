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

public class pre_activos_sin_bajas extends Pantalla {

	private Tabla tab_consulta_activo = new Tabla();
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

	public pre_activos_sin_bajas() {
		bar_botones.limpiar();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);
		System.out.println("Pantalla pre_activos_sin_bajas ");
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setSql(ser_activo.getConsultaCustodiosYBienesSinBajas());

		tab_consulta_activo.getColumna("codigo").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo").setLongitud(5);
		
		tab_consulta_activo.getColumna("codigo_anterior").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo_anterior").setLongitud(7);
		
		tab_consulta_activo.getColumna("proveedor_ruc").setFiltroContenido();
		tab_consulta_activo.getColumna("proveedor_ruc").setLongitud(10);
		
		tab_consulta_activo.getColumna("proveedor_nombre").setFiltroContenido();
		tab_consulta_activo.getColumna("proveedor_nombre").setLongitud(25);
		
		tab_consulta_activo.getColumna("proveedor_representante").setFiltroContenido();
		tab_consulta_activo.getColumna("proveedor_representante").setLongitud(25);

		tab_consulta_activo.getColumna("ingreso_fecha_ingreso").setFiltroContenido();
		tab_consulta_activo.getColumna("ingreso_fecha_ingreso").setLongitud(10);
		
		tab_consulta_activo.getColumna("fecha_alta").setFiltroContenido();
		tab_consulta_activo.getColumna("fecha_alta").setLongitud(10);
		
		tab_consulta_activo.getColumna("factura_numero").setFiltroContenido();
		tab_consulta_activo.getColumna("factura_numero").setLongitud(15);
		
		tab_consulta_activo.getColumna("ingreso_fecha_factura").setFiltroContenido();
		tab_consulta_activo.getColumna("ingreso_fecha_factura").setLongitud(10);
		
		tab_consulta_activo.getColumna("factura_anterior").setFiltroContenido();
		tab_consulta_activo.getColumna("factura_anterior").setLongitud(15);
		
		tab_consulta_activo.getColumna("forma_ingreso").setFiltroContenido();
		tab_consulta_activo.getColumna("forma_ingreso").setLongitud(7);
		
		tab_consulta_activo.getColumna("ingreso_numero").setFiltroContenido();
		tab_consulta_activo.getColumna("ingreso_numero").setLongitud(5);
		
		tab_consulta_activo.getColumna("ingreso_novedad").setFiltroContenido();
		tab_consulta_activo.getColumna("ingreso_novedad").setLongitud(35);

		tab_consulta_activo.getColumna("ingreso_observacion").setFiltroContenido();
		tab_consulta_activo.getColumna("ingreso_observacion").setLongitud(35);

		tab_consulta_activo.getColumna("codigo_catalogo").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo_catalogo").setLongitud(7);
		
		tab_consulta_activo.getColumna("nombre_catalogo").setFiltroContenido();
		tab_consulta_activo.getColumna("nombre_catalogo").setLongitud(25);
		
		tab_consulta_activo.getColumna("item_presupuestario").setFiltroContenido();
		tab_consulta_activo.getColumna("item_presupuestario").setLongitud(10);
		
		tab_consulta_activo.getColumna("tipo_activo").setFiltroContenido();
		tab_consulta_activo.getColumna("tipo_activo").setLongitud(5);

		tab_consulta_activo.getColumna("descripcion_caracteristicas").setFiltroContenido();
		tab_consulta_activo.getColumna("descripcion_caracteristicas").setLongitud(35);
		
		tab_consulta_activo.getColumna("cantidad").setFiltroContenido();
		tab_consulta_activo.getColumna("cantidad").setLongitud(35);
			
		tab_consulta_activo.getColumna("componentes").setFiltroContenido();
		tab_consulta_activo.getColumna("componentes").setLongitud(35);
		
		tab_consulta_activo.getColumna("serie").setFiltroContenido();
		tab_consulta_activo.getColumna("serie").setLongitud(7);
		
		tab_consulta_activo.getColumna("marca").setFiltroContenido();
		tab_consulta_activo.getColumna("marca").setLongitud(7);
		
		tab_consulta_activo.getColumna("modelo").setFiltroContenido();
		tab_consulta_activo.getColumna("modelo").setLongitud(7);
		
		tab_consulta_activo.getColumna("modelo").setFiltroContenido();
		tab_consulta_activo.getColumna("modelo").setLongitud(7);
		
		tab_consulta_activo.getColumna("color").setFiltroContenido();
		tab_consulta_activo.getColumna("color").setLongitud(7);

		tab_consulta_activo.getColumna("chasis").setFiltroContenido();
		tab_consulta_activo.getColumna("chasis").setLongitud(7);

		tab_consulta_activo.getColumna("motor").setFiltroContenido();
		tab_consulta_activo.getColumna("motor").setLongitud(7);

		tab_consulta_activo.getColumna("placa").setFiltroContenido();
		tab_consulta_activo.getColumna("placa").setLongitud(7);
		
		tab_consulta_activo.getColumna("estado").setFiltroContenido();
		tab_consulta_activo.getColumna("estado").setLongitud(7);

		tab_consulta_activo.getColumna("ubicacion").setFiltroContenido();
		tab_consulta_activo.getColumna("ubicacion").setLongitud(7);

		tab_consulta_activo.getColumna("referencia_ubicacion").setFiltroContenido();
		tab_consulta_activo.getColumna("referencia_ubicacion").setLongitud(35);

		tab_consulta_activo.getColumna("numero_custodios").setFiltroContenido();
		tab_consulta_activo.getColumna("numero_custodios").setLongitud(7);
		
		tab_consulta_activo.getColumna("nombres_del_o_los_custodios").setFiltroContenido();
		tab_consulta_activo.getColumna("nombres_del_o_los_custodios").setLongitud(45);
		
		
		tab_consulta_activo.getColumna("novedad_del_bien").setFiltroContenido();
		tab_consulta_activo.getColumna("novedad_del_bien").setLongitud(35);
		
		tab_consulta_activo.getColumna("ultima_acta_erp").setFiltroContenido();
		tab_consulta_activo.getColumna("ultima_acta_erp").setLongitud(7);
		
		tab_consulta_activo.getColumna("tipo_ultima_acta").setFiltroContenido();
		tab_consulta_activo.getColumna("tipo_ultima_acta").setLongitud(25);
		
		tab_consulta_activo.getColumna("numero_ultima_acta").setFiltroContenido();
		tab_consulta_activo.getColumna("numero_ultima_acta").setLongitud(7);

		tab_consulta_activo.getColumna("fecha_ultima_acta").setFiltroContenido();
		tab_consulta_activo.getColumna("fecha_ultima_acta").setLongitud(10);
		
		tab_consulta_activo.getColumna("acta_ultima_constacion_erp").setFiltroContenido();
		tab_consulta_activo.getColumna("acta_ultima_constacion_erp").setLongitud(5);
		
		tab_consulta_activo.getColumna("numero_ultima_constatacion").setFiltroContenido();
		tab_consulta_activo.getColumna("numero_ultima_constatacion").setLongitud(5);
		
		tab_consulta_activo.getColumna("fecha_ultima_constatacion").setFiltroContenido();
		tab_consulta_activo.getColumna("fecha_ultima_constatacion").setLongitud(10);
		
		tab_consulta_activo.getColumna("observacion_ultima_constatacion").setFiltroContenido();
		tab_consulta_activo.getColumna("observacion_ultima_constatacion").setLongitud(35);

		tab_consulta_activo.getColumna("constatacion_2015").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2015").setLongitud(5);
		
		tab_consulta_activo.getColumna("constatacion_2016").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2016").setLongitud(5);
		
		tab_consulta_activo.getColumna("constatacion_2017").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2017").setLongitud(5);
		
		tab_consulta_activo.getColumna("constatacion_2018").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2018").setLongitud(5);

		tab_consulta_activo.getColumna("constatacion_2019").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2019").setLongitud(5);
		
		tab_consulta_activo.getColumna("constatacion_2020").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2020").setLongitud(5);

		tab_consulta_activo.getColumna("constatacion_2021").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2021").setLongitud(5);

		tab_consulta_activo.getColumna("constatacion_2022").setFiltroContenido();
		tab_consulta_activo.getColumna("constatacion_2022").setLongitud(5);

		tab_consulta_activo.setRows(30);
		tab_consulta_activo.setLectura(true);
		
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

	/**
	 * @return the p_parametros
	 */
	public Map getP_parametros() {
		return p_parametros;
	}

	/**
	 * @param p_parametros the p_parametros to set
	 */
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
}
