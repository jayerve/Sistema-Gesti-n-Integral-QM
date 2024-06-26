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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_egreso_por_item_catalogo extends Pantalla {


	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color: blue";
	private final String ESTILO_ETIQUETA_ROJA = "font-size:15px;font-weight: bold;text-decoration: underline;color: red";

	private Tabla tab_tabla = new Tabla();
	private Combo com_anio = new Combo();
	//private Combo com_bodega = new Combo();
	
	private Map<String, Object> p_parametros = new HashMap<String, Object>();

	
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private AutoCompletar aut_item_catalogo = new AutoCompletar();
	

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_egreso_por_item_catalogo() {
		System.out.println("pre_egreso_por_item_catalogo");
	
		
		aut_item_catalogo.setId("aut_item_catalogo");		
		aut_item_catalogo.setAutoCompletar(ser_bodega.getCalalogoExistenciasItems());
		
		aut_item_catalogo.setMetodoChange("seleccionaParametros");


		Etiqueta eti_colaborador=new Etiqueta("Item catálogo:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_item_catalogo);
		

		tab_tabla.setId("tab_tabla");
		//tab_tabla.setCondicion("ide_boinv=-1");
		tab_tabla.setSql(ser_bodega.getSqlEgresosPorItemCatalogo("-1", "-1"));
		//tab_tabla.getGrid().setColumns(4);

		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion",
				"ide_boubi", "detalle_boubi", "");
		
		tab_tabla.getColumna("ide_boubi").setCombo("bodt_bodega_ubicacion",
				"ide_boubi", "detalle_boubi", "");
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
		

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false",
				"true,false"));
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
		if (com_anio.getValue() != null && aut_item_catalogo.getValor() != null) {
			tab_tabla.setSql(ser_bodega.getSqlEgresosPorItemCatalogo(com_anio.getValue().toString(), aut_item_catalogo.getValor().toString()));
			tab_tabla.ejecutarSql();
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y el ítem", "");
		}
	}

	@Override
	public void insertar() {

	}




	@Override
	public void guardar() {

		
	}

	@Override
	public void eliminar() {

		//tab_tabla.eliminar();
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


	public AutoCompletar getAut_item_catalogo() {
		return aut_item_catalogo;
	}



	public void setAut_item_catalogo(AutoCompletar aut_item_catalogo) {
		this.aut_item_catalogo = aut_item_catalogo;
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
