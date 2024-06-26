package paq_bodega;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_egreso_por_solicitante extends Pantalla {

	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color: blue";
	private final String ESTILO_ETIQUETA_ROJA = "font-size:15px;font-weight: bold;text-decoration: underline;color: red";

	private Tabla tab_tabla = new Tabla();
	private Combo com_anio = new Combo();
	// private Combo com_bodega = new Combo();

	private Map<String, Object> p_parametros = new HashMap<String, Object>();

	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();

	private SeleccionTabla set_empleado = new SeleccionTabla();
	private AutoCompletar aut_empleado = new AutoCompletar();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_egreso_por_solicitante() {
		System.out.println("pre_egreso_por_solicitante");
		
		bar_botones.agregarReporte();
		
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar(
				"select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP, APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("seleccionaParametros");

		Etiqueta eti_colaborador = new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);

		tab_tabla.setId("tab_tabla");
		// tab_tabla.setCondicion("ide_boinv=-1");
		tab_tabla.setSql(ser_bodega.getSqlEgresosPorSolicitante("-1", "-1"));
		// tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion", "ide_boubi", "detalle_boubi", "");
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");
		tab_tabla.getColumna("ide_boubi").setNombreVisual("BODEGA");

		tab_tabla.getColumna("ide_gtemp_solicitante").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_solicitante").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_solicitante").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_solicitante").setRequerida(true);
		tab_tabla.getColumna("ide_gtemp_solicitante").setNombreVisual("EMPLEADO SOLICITANTE");

		tab_tabla.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla.getColumna("ide_bocam").setRequerida(true);
		tab_tabla.getColumna("ide_bocam").setFiltroContenido();

		tab_tabla.setLectura(true);
		tab_tabla.setRows(30);
		tab_tabla.dibujar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false", "true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);

	}

	public void seleccionaParametros() {
		if (com_anio.getValue() != null && aut_empleado.getValor() != null) {
			tab_tabla.setSql(ser_bodega.getSqlEgresosPorSolicitante(com_anio.getValue().toString(),
					aut_empleado.getValor().toString()));
			tab_tabla.ejecutarSql();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y el solicitante", "");
		}
	}

	@Override
	public void insertar() {

	}

	@Override
	public void abrirListaReportes() {
		System.out.println("pre_egreso_por_solicitante: abrirListaReportes");
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {

		System.out.println("Reporte seleccionado: " + rep_reporte.getReporteSelecionado());
		if (rep_reporte.getReporteSelecionado().equals("Resumen de egresos por solicitante anual")) {
			if (rep_reporte.isVisible()) {
				try {
					p_parametros = new HashMap<String, Object>();
					rep_reporte.cerrar();
					p_parametros.put("titulo", "EGRESOS POR SOLICITANTE");
					p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
					p_parametros.put("ide_empr", 0);
					p_parametros.put("ide_sucu", 1);
					p_parametros.put("ide_geani", Long.parseLong(com_anio.getValue().toString()));
					p_parametros.put("ide_inttr", 2L);
					p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
					System.out.println("aceptarReporte " + rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();
				} catch (NumberFormatException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	@Override
	public void guardar() {

	}

	@Override
	public void eliminar() {

		// tab_tabla.eliminar();
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

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public String getESTILO_ETIQUETA() {
		return ESTILO_ETIQUETA;
	}

	public String getESTILO_ETIQUETA_ROJA() {
		return ESTILO_ETIQUETA_ROJA;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public Map<String, Object> getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map<String, Object> p_parametros) {
		this.p_parametros = p_parametros;
	}

}
