package paq_existencia;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_existencia.ejb.ServicioExistencias;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;

public class exi_kardex extends Pantalla {

	private Tabla tab_consulta_activo = new Tabla();

	private AutoCompletar aut_empleado = new AutoCompletar();
	public static String par_catalogo_bodega;

	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

	private Combo com_anio = new Combo();
	@EJB
	private ServicioExistencias ser_existencia = (ServicioExistencias) utilitario.instanciarEJB(ServicioExistencias.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public exi_kardex() {
		
		par_catalogo_bodega = utilitario.getVariable("p_item_presupuestarios_existencias");
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		String items = "'" + par_catalogo_bodega.replace(";", "','") + "'";
		System.out.println("Items para catalogo de existencias: " + items);

		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar(ser_bodega.getCatalagoBodega("true", items));
		aut_empleado.setMetodoChange("filtrarItemCatalogo");

		bar_botones.agregarComponente(new Etiqueta("Item Presupuestario:"));
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
	
		System.out.println("Pantalla pre_activos_consulta ");
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setSql(ser_existencia.getKardexExistencia("0","0"));
////		 tab_consulta_activo.getColumna("ide_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("ide_afact").setLongitud(5);
//		
////		 tab_consulta_activo.getColumna("ide_bocam").setFiltroContenido();
//		 tab_consulta_activo.getColumna("ide_bocam").setLongitud(10);
////		 tab_consulta_activo.getColumna("cat_codigo_bocam").setFiltroContenido();
//		 tab_consulta_activo.getColumna("cat_codigo_bocam").setLongitud(10);
////		 tab_consulta_activo.getColumna("descripcion_bocam").setFiltroContenido();
//		 tab_consulta_activo.getColumna("descripcion_bocam").setLongitud(100);
////	     tab_consulta_activo.getColumna("detalle_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("detalle_afact").setLongitud(150);
////		 tab_consulta_activo.getColumna("cod_anterior_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("cod_anterior_afact").setLongitud(35);
////		 tab_consulta_activo.getColumna("color_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("color_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("marca_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("marca_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("serie_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("serie_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("modelo_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("modelo_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("chasis_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("chasis_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("motor_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("motor_afact").setLongitud(10);
////		 tab_consulta_activo.getColumna("observaciones_afact").setFiltroContenido();
//		 tab_consulta_activo.getColumna("observaciones_afact").setLongitud(150);
//		
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

	public void seleccionaElAnio() {
		if (com_anio.getValue() != null) {
			tab_consulta_activo.setSql(ser_existencia.getKardexExistencia(aut_empleado.getValor(),com_anio.getValue().toString()));
			tab_consulta_activo.ejecutarSql();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
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
			if (rep_reporte.getReporteSelecionado().equals("Kardex")) {
				if (rep_reporte.isVisible()) {
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					p_parametros.put("titulo", "MOVIMIENTOS KARDEX");
					p_parametros.put("ide_gtemp", pckUtilidades.CConversion.CInt(aut_empleado.getValor()));
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					self_reporte.dibujar();
				}
			}
		}else{
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

	public void filtrarItemCatalogo(SelectEvent evt) {
		
		aut_empleado.onSelect(evt);
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		tab_consulta_activo.setSql(ser_existencia.getKardexExistencia(aut_empleado.getValor(),com_anio.getValue().toString()));
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
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

}
