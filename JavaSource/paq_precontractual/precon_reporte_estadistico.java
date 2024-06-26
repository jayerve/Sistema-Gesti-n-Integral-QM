package paq_precontractual;

import javax.ejb.EJB;

import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.model.chart.CartesianChartModel;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_nomina.cls_graficas;
import paq_precontractual.ejb.ServicioPrecontractual;
import paq_precontractual.ejb.ServicioProcedimiento;
import paq_sistema.aplicacion.Pantalla;

public class precon_reporte_estadistico extends Pantalla {
	
	private Grid grid_grafico = new Grid();
	private Division div_division = new Division();
	private Tabla tab_datos=new Tabla();

	public BarChart barchart = new BarChart();
	public CartesianChartModel categoryModel;
	cls_graficas grafico=new cls_graficas();
	private PanelAcordion pac_acordion = new PanelAcordion();

	private Tabla tab_tabla = new Tabla();
	private SeleccionTabla set_tipo_contratacion=new SeleccionTabla();
	private SeleccionTabla set_precontractual=new SeleccionTabla();
	private SeleccionCalendario sec_rango_fechas=new SeleccionCalendario();
		
	TablaGenerica tab_rep_pantalla=new TablaGenerica();

	@EJB
    private ServicioProcedimiento ser_procedimiento = (ServicioProcedimiento) utilitario.instanciarEJB(ServicioProcedimiento.class);
	@EJB
    private ServicioPrecontractual ser_precontractual = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);

	public precon_reporte_estadistico() {

		bar_botones.limpiar();
			
		Boton bot_ver_procedimiento_contratacion=new Boton();
		bot_ver_procedimiento_contratacion.setMetodo("graficarReporte");
		bot_ver_procedimiento_contratacion.setValue("GRAFICAR REPORTE");
		
		bar_botones.agregarBoton(bot_ver_procedimiento_contratacion);

		//Ventana emergente seleccionar tipo de contratacion
		set_tipo_contratacion.setId("set_tipo_contratacion");
		set_tipo_contratacion.setSeleccionTabla(ser_procedimiento.getReporteProcedimientoContratacion(),"ide_prpro");
		set_tipo_contratacion.getTab_seleccion().getColumna("descripcion").setNombreVisual("Tipo de Contratación");
		set_tipo_contratacion.setTitle("Seleccione el Tipo de Contratación ");
		set_tipo_contratacion.getBot_aceptar().setMetodo("aceptarContratacion");
		set_tipo_contratacion.getBot_cancelar().setMetodo("cancelarContratacion");
		set_tipo_contratacion.setRadio();
		agregarComponente(set_tipo_contratacion);
			
		//Ventana emergente seleccionar tipo de contratacion
		set_precontractual.setId("set_precontractual");
		set_precontractual.setSeleccionTabla(ser_precontractual.getProceso(),"ide_prpre");
		set_precontractual.setTitle("Seleccione el Proceso de Contratación ");
		//set_precontractual.setRadio();
		agregarComponente(set_precontractual);
		
		//Ventana emergente rango de fechas
		sec_rango_fechas.setId("sec_rango_fechas");
		sec_rango_fechas.getBot_aceptar().setMetodo("graficarPrecontractualYFechas");
		sec_rango_fechas.setTitle("Seleccion Rango de Fechas");
		sec_rango_fechas.setDynamic(false);
		agregarComponente(sec_rango_fechas);
		
		///componentes grafica
		div_division.setId("div_division");
		dibujarTabla();
		agregarComponente(div_division);
		
	}
	
	public void graficarReporte(){
		sec_rango_fechas.dibujar();
	}
	
	public void aceptarContratacion(){
		if(set_tipo_contratacion.getValorSeleccionado()!=null){
			sec_rango_fechas.setFecha1(null);
			sec_rango_fechas.setFecha2(null);
			set_precontractual.setSeleccionTabla(ser_precontractual.getReportePrecontractualEstado(pckUtilidades.CConversion.CInt(set_tipo_contratacion.getValorSeleccionado())),"ide_prpre");
			set_precontractual.getTab_seleccion().getColumna("descripcion_prpre").setNombreVisual("Proceso Contratación");
			set_precontractual.getBot_aceptar().setMetodo("graficarPrecontractualYFechas()");
			set_precontractual.dibujar();
			utilitario.addUpdate("set_precontractual");
			set_tipo_contratacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar el Tipo de Contratación.", "");
		}

	}
	/**
	 * Cancela el poppup de seleccion de tipo contratación
	 */
	public void cancelarContratacion(){
		set_tipo_contratacion.cerrar();
	}
	
	public void graficarPrecontractualYFechas(){
		sec_rango_fechas.cerrar();
		if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
				&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
			String titulo = "PROCESO FASE PREPARATORIA Y PRECONTRACTUAL";
			tab_rep_pantalla=utilitario.consultar("select estado_proceso_prpre as ide_label, estado_proceso_prpre as detalle_label,"
					+ " estado_proceso_prpre as ide_col_ejex, estado_proceso_prpre as nom_col_ejex,count(estado_proceso_prpre) as monto "
					+ "from precon_precontractual "
					+ "where fecha_prpre between '"+sec_rango_fechas.getFecha1String()+"' and '"+sec_rango_fechas.getFecha2String()+"' "
					+ "and activo_prpre=true group by estado_proceso_prpre "
					+ " order by ide_label,ide_col_ejex");
			if (tab_rep_pantalla.getTotalFilas()>0){
				construirGrafico(tab_rep_pantalla,titulo);
			}else{
				utilitario.agregarMensajeInfo("No se puede graficar","No existen datos");
				return;
			}
		}else{
			utilitario.agregarMensajeInfo("No ha seleccionado el rango de fechas","");
			return;
		}

	}
	
	public void construirGrafico(TablaGenerica tab_rep,String titulo){

		categoryModel=new CartesianChartModel();		

		Ajax evt=new Ajax();
		evt.setMetodo("itemSelect");

		barchart=new BarChart();
		barchart=grafico.getBarchar(tab_rep,titulo,300,400);
		
		barchart.addClientBehavior("itemSelect", evt);

		Etiqueta eti = new Etiqueta("GRÁFICO ESTADÍSTICO");
		eti.setStyle("font-size:12px;");
		grid_grafico.getChildren().clear();
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");
		grid_grafico.getChildren().add(barchart);

		pac_acordion=new PanelAcordion();
		pac_acordion.setId("pac_acordion");

		tab_datos=new Tabla();
		tab_datos.setId("tab_datos");
		tab_datos.setIdCompleto("pac_acordion:tab_datos");
		tab_datos.setSql(tab_rep_pantalla.getSql()); 
		tab_datos.setCampoPrimaria("ide_label");
		tab_datos.setNumeroTabla(1);
		tab_datos.setLectura(true);
		tab_datos.setColumnaSuma("MONTO");
		tab_datos.setHeader(barchart.getTitle());
		tab_datos.getColumna("MONTO").alinearDerecha();
		tab_datos.getColumna("MONTO").setNombreVisual("NRO. PROCESO ");
		tab_datos.getColumna("MONTO").setAncho(30);
		tab_datos.getColumna("ide_label").setVisible(false);
		tab_datos.getColumna("ide_col_ejex").setVisible(false);
		tab_datos.getColumna("detalle_label").setVisible(false);
		tab_datos.getColumna("nom_col_ejex").setNombreVisual("ESTADOS PROCESO ");

		tab_datos.dibujar();

		PanelTabla pat_datos=new PanelTabla();
		pat_datos.setPanelTabla(tab_datos);

		pac_acordion.agregarPanel("INFORMACIÓN EN TABLA", pat_datos);
		pac_acordion.setRendered(true);

		//bot_exportar.setOnclick("$('#formulario\\\\:output').empty().append(basic.exportAsImage());dlg.show(); ");

		Grid gri_cabecera=new Grid();

		gri_cabecera.setWidth("100%");

		Etiqueta eti_titulo=new Etiqueta(titulo);
		eti_titulo.setEstiloCabecera("text-align:center;font-size:10px;font-weight:bold");
		gri_cabecera.getChildren().add(eti_titulo);

		Etiqueta eti_periodo=new Etiqueta("Desde: "+sec_rango_fechas.getFecha1String()+" Hasta: "+sec_rango_fechas.getFecha2String());
		eti_periodo.setEstiloCabecera("text-align:center;font-size:10px;font-weight:bold");
		gri_cabecera.getChildren().add(eti_periodo);

		gri_cabecera.getChildren().add(pac_acordion);

		div_division.getChildren().clear();
		div_division.dividir2(gri_cabecera,grid_grafico,"40%","V");
		div_division.getDivision1().setHeader("MENU DE DATOS");
		div_division.getDivision1().setCollapsible(true);
		utilitario.addUpdate("div_division,bot_exportar");

	}
	
	public void dibujarTabla(){
		Etiqueta eti = new Etiqueta("GRÁFICO ESTADÍSTICO");
		eti.setStyle("font-size:18px;");
		Etiqueta eti1 = new Etiqueta("PARÁMETROS RUBROS");
		eti1.setStyle("font-size:10px; aling:left;");
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");
		div_division.getChildren().clear();
		div_division.dividir1(grid_grafico);
	}

	public Tabla getTab_datos() {
		return tab_datos;
	}

	public void setTab_datos(Tabla tab_datos) {
		this.tab_datos = tab_datos;
	}

	public SeleccionTabla getSet_tipo_contratacion() {
		return set_tipo_contratacion;
	}

	public void setSet_tipo_contratacion(SeleccionTabla set_tipo_contratacion) {
		this.set_tipo_contratacion = set_tipo_contratacion;
	}

	public ServicioProcedimiento getSer_procedimiento() {
		return ser_procedimiento;
	}

	public void setSer_procedimiento(ServicioProcedimiento ser_procedimiento) {
		this.ser_procedimiento = ser_procedimiento;
	}
	
	public SeleccionTabla getSet_precontractual() {
		return set_precontractual;
	}

	public void setSet_precontractual(SeleccionTabla set_precontractual) {
		this.set_precontractual = set_precontractual;
	}

	public SeleccionCalendario getSec_rango_fechas() {
		return sec_rango_fechas;
	}

	public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
		this.sec_rango_fechas = sec_rango_fechas;
	}

	public TablaGenerica getTab_rep_pantalla() {
		return tab_rep_pantalla;
	}

	public void setTab_rep_pantalla(TablaGenerica tab_rep_pantalla) {
		this.tab_rep_pantalla = tab_rep_pantalla;
	}

	public ServicioPrecontractual getSer_precontractual() {
		return ser_precontractual;
	}

	public void setSer_precontractual(ServicioPrecontractual ser_precontractual) {
		this.ser_precontractual = ser_precontractual;
	}

	@Override
	public void insertar() {

		tab_tabla.insertar();
	}

	@Override
	public void guardar() {

	}

	@Override
	public void eliminar() {

		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

}
