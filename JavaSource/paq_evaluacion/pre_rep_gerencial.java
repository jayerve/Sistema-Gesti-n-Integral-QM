package paq_evaluacion;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_nomina.cls_graficas;
import paq_sistema.aplicacion.Pantalla;


/**
 *
 * @author HP-USER
 */
public class pre_rep_gerencial extends Pantalla {

   
	private Division div_division = new Division();

	private TablaGenerica tab_rep_grafico=new TablaGenerica();
	private BarChart barchart = new BarChart();
	private CartesianChartModel categoryModel;
	private Calendario cal_fecha_inicio= new Calendario();
	private Calendario cal_fecha_fin= new Calendario();
	private Combo cmb_parametros = new Combo();
	private Tabla tab_datos = new Tabla();
	private SeleccionTabla set_sucursal=new SeleccionTabla();
	
	cls_graficas grafico=new cls_graficas();
	private Boton bot_exportar=new Boton();

	public pre_rep_gerencial() {        
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicio.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicio);
		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_fin.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_fin);

		Boton bot_filtrar_Fechas = new Boton();
		bot_filtrar_Fechas.setValue("Graficar Reporte");
		bot_filtrar_Fechas.setIcon("ui-icon-refresh");
		bot_filtrar_Fechas.setMetodo("graficarReporte");
		bar_botones.agregarBoton(bot_filtrar_Fechas);

		div_division.setId("div_division");
		List lista = new ArrayList();
		Object fila1[] = {
				"1","AREA"
		};
		Object fila2[] = {
				"2","DEPARTAMENTO"
		};
		Object fila3[] = {
				"3","GRUPO OCUPACIONAL"
		};
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);


		cmb_parametros.setId("cmb_parametros");
		cmb_parametros.setCombo(lista);
		cmb_parametros.setMetodo("cambiaParametro");
		cmb_parametros.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("Escoga Parametro :"));
		bar_botones.agregarComponente(cmb_parametros);


		bot_exportar=new Boton();
		bot_exportar.setId("bot_exportar");
		bot_exportar.setValue("Exportar");
		bot_exportar.setType("button");
		bot_exportar.setIcon("ui-icon-extlink");

		bar_botones.agregarBoton(bot_exportar);
		
		
		Dialog dia_expor=new Dialog();
		dia_expor.setModal(true);
		dia_expor.setHeader("GRAFICO");
		dia_expor.setShowEffect("fade");
		dia_expor.setWidgetVar("dlg");
		dia_expor.setAppendToBody(true);



		OutputPanel out_panel=new OutputPanel();
		out_panel.setId("output");
		out_panel.setLayout("block");
		out_panel.setStyle("width:100%;height:100%");
		dia_expor.getChildren().add(out_panel);

		agregarComponente(dia_expor);
		
		
		set_sucursal.setId("set_sucursal");
		set_sucursal.setSeleccionTabla("select ide_sucu,nom_sucu AS SUCURSAL from sis_sucursal ","IDE_SUCU");
		set_sucursal.setTitle("SELECCION SUCURSAL");
		set_sucursal.setDynamic(false);
		agregarComponente(set_sucursal);
		
		//cmb_sucursal.setMetodo("");

	
	


		Etiqueta eti = new Etiqueta("GRÁFICOS ESTADÍSTICOS EMGIRS-EP");
		eti.setStyle("font-size:18px;");
		Etiqueta eti1 = new Etiqueta("PARÁMETROS RUBROS");
		eti1.setStyle("font-size:10px; aling:left;");
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");
		grid_grafico.getChildren().add(eti);
		

		div_division.dividir1(grid_grafico);

		
		agregarComponente(div_division);

	}
	
	public void aceptarSucursales(){

		if (set_sucursal.getListaSeleccionados().size()==0){
			utilitario.agregarMensajeInfo("Debe seleccionar al menos una sucursal", "");
			return;
		}
		
		if (cmb_parametros.getValue().equals("1")){
			// area 
			tab_rep_grafico=utilitario.consultar("select ide_geare as  ide_label, " +
					"detalle_geare as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_geare,detalle_geare " +
				"order by nom_col_ejex");

			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}
			construirGrafico(tab_rep_grafico, "GRAFICO POR AREAS","AREA","SUCURSAL");
			
			
		}else if (cmb_parametros.getValue().equals("2")){
			// departamento
			tab_rep_grafico=utilitario.consultar("select ide_gedep as  ide_label, " +
					"detalle_gedep as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_gedep,detalle_gedep " +
				"order by nom_col_ejex");
			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}
			
			construirGrafico(tab_rep_grafico,  "GRAFICO POR DEPARTAMENTOS","DEPARTAMENTO","SUCURSAL");

		}else if (cmb_parametros.getValue().equals("3")){
			// grupo
			tab_rep_grafico=utilitario.consultar("select ide_gegro as  ide_label, " +
					"detalle_gegro as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_gegro,detalle_gegro " +
				"order by nom_col_ejex");
			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}
			
			construirGrafico(tab_rep_grafico,  "GRAFICO POR GRUPO OCUPACIONAL","GRUPO","SUCURSAL");
			
		}
		
		set_sucursal.cerrar();
	}
	
	public void graficarReporte(){
		
		if (cal_fecha_inicio.getFecha()==null || cal_fecha_inicio.getFecha().isEmpty()){
			utilitario.agregarMensajeInfo("Seleccione una fecha inicial", "");
			return;
		}
		if (cal_fecha_fin.getFecha()==null || cal_fecha_fin.getFecha().isEmpty()){
			utilitario.agregarMensajeInfo("Seleccione una fecha final", "");
			return;
		}
		if (cmb_parametros.getValue()==null || cmb_parametros.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("Seleccione un parametro de consulta", "");
			return;
		}
		
		set_sucursal.getBot_aceptar().setMetodo("aceptarSucursales");
		set_sucursal.dibujar();
		utilitario.addUpdate("set_sucursal");
	}
	
	
	public SeleccionTabla getSet_sucursal() {
		return set_sucursal;
	}

	public void setSet_sucursal(SeleccionTabla set_sucursal) {
		this.set_sucursal = set_sucursal;
	}

	public String getSqlReporte(String fecha_ini,String fecha_fin,String ide_sucu){
		String str_sql_rep_ger="select a.ide_evdes,ide_gegro,ide_gecaf,ide_geare,ide_gedep,to_char(fecha_desde_evdes,'yyyy-mm-dd') as fecha_desde_evdes, " +
				"to_char(fecha_hasta_evdes,'yyyy-mm-dd') as fecha_hasta_evdes,empleado_evaluado, detalle_gecaf,detalle_gegro,nom_sucu,detalle_gedep, " +
				"detalle_geare,resultado_evaluacion,detalle_evecr,ide_evecr,detalle_gttco,ide_sucu " +
				"from ( select ide_evdes,c.ide_gegro,fecha_desde_evdes,fecha_hasta_evdes,ide_gecaf,ide_geare,ide_gedep, ide_sucu, " +
				"apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado_evaluado, " +
				"detalle_gecaf,detalle_gegro,nom_sucu,detalle_gedep,detalle_geare,detalle_gttco from evl_desempenio a, gth_empleado b , " +
				"( select ide_geedp,c.ide_gegro,b.ide_gecaf, detalle_gecaf,detalle_gegro,nom_sucu,detalle_gedep,detalle_geare,detalle_gttco, " +
				"e.ide_gedep,f.ide_geare,d.ide_sucu from  gen_empleados_departamento_par a " +
				"left join gen_cargo_funcional b on a.ide_gecaf = b.ide_gecaf left join gen_grupo_ocupacional c on a.ide_gegro = c.ide_gegro " +
				"left join sis_sucursal d on a.ide_sucu = d.ide_sucu left join gen_departamento e on a.ide_gedep = e.ide_gedep " +
				"left join gen_area f on a.ide_geare = f.ide_geare left join gth_tipo_contrato g on a.ide_gttco= g.ide_gttco )  c " +
				"where a.ide_gtemp = b.ide_gtemp and a.ide_geedp = c.ide_geedp ) a " +
				"left join (   select ide_evdes,resultado_evaluacion,detalle_evecr,b.ide_evecr " +
				"from ( select ide_evdes, sum(resultado_evres) as resultado_evaluacion " +
				"from ( select a.ide_eveva,a.evaluador,ide_evdes ,por_peso_eveva,fecha_evaluacion_eveva,evaluador ,peso_factor_evres,resultado_evres, " +
				"detalle_evfae from ( select ide_eveva,a.ide_geedp as ide_evaluador,ide_evdes,por_peso_eveva,fecha_evaluacion_eveva, " +
				"apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as evaluador " +
				"from evl_evaluadores a, gen_empleados_departamento_par b,gth_empleado c where a.ide_geedp = b.ide_geedp  " +
				"and b.ide_gtemp = c.ide_gtemp ) a, ( select ide_eveva,a.ide_evfae,peso_factor_evres,resultado_evres,detalle_evfae " +
				"from evl_resultado a, evl_factor_evaluacion b where a.ide_evfae = b.ide_evfae ) b where a.ide_eveva=b.ide_eveva ) a group by ide_evdes " +
				") a, evl_escala_calif_resul b where resultado_evaluacion between por_inicio_evecr and por_fin_evecr ) c on a.ide_evdes = c.ide_evdes " +
				"where not ide_evecr is null " +
				"and fecha_desde_evdes between to_date('"+fecha_ini+"','yyyy-mm-dd') and to_date('"+fecha_fin+"','yyyy-mm-dd') " +
				"and ide_sucu in ("+ide_sucu+") ";
		return str_sql_rep_ger;
	}
	
	private Grid grid_grafico = new Grid();
	public void construirGrafico(TablaGenerica tab_rep,String titulo,String nombre_label, String nombre_col_ejex){

		categoryModel=new CartesianChartModel();		

		Ajax evt=new Ajax();
		evt.setMetodo("itemSelect");

		barchart=new BarChart();
		barchart=grafico.getBarchar(tab_rep,titulo,500,570);
		
		barchart.addClientBehavior("itemSelect", evt);

		Etiqueta eti = new Etiqueta("GRÁFICOS ESTADÍSTICOS BIESS");
		eti.setStyle("font-size:18px;");
		grid_grafico.getChildren().clear();
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");
		grid_grafico.getChildren().add(barchart);


		bot_exportar.setOnclick("$('#formulario\\\\:output').empty().append(basic.exportAsImage());dlg.show(); ");
		tab_datos=new Tabla();
		
	tab_datos.setId("tab_datos");
	tab_datos.setSql(tab_rep.getSql());
	tab_datos.setNumeroTabla(1);
	tab_datos.getColumna("ide_col_ejex").setVisible(false);
	tab_datos.getColumna("ide_label").setVisible(false);
	tab_datos.getColumna("detalle_label").setNombreVisual(nombre_label);
	tab_datos.getColumna("nom_col_ejex").setNombreVisual(nombre_col_ejex);
	tab_datos.setCampoPrimaria("IDE_LABEL");
	tab_datos.setLectura(true);
	
	tab_datos.dibujar();
		
	PanelTabla pat_datos = new PanelTabla();
	pat_datos.setPanelTabla(tab_datos);
	pat_datos.setMensajeWarn(titulo);
	
		div_division.getChildren().clear();
		div_division.dividir2(pat_datos,grid_grafico,"35%","V");
		utilitario.addUpdate("div_division,bot_exportar");

		
		
	}

	
	public void cambiaParametro(){
		
		if (set_sucursal.getSeleccionados()==null || set_sucursal.getSeleccionados().isEmpty()){
			set_sucursal.getBot_aceptar().setMetodo("aceptarSucursales");
			set_sucursal.dibujar();
			utilitario.addUpdate("set_sucursal");
			return;
		}
		
		System.out.println("par "+cmb_parametros.getValue());
		if (cmb_parametros.getValue().equals("1")){
			// area 
			tab_rep_grafico=utilitario.consultar("select ide_geare as ide_label, " +
					"detalle_geare as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_geare,detalle_geare " +
				"order by nom_col_ejex");
			
			
			
			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}

			construirGrafico(tab_rep_grafico, "GRAFICO POR AREAS","AREA","SUCURSAL");
			
			
		}else if (cmb_parametros.getValue().equals("2")){
			// departamento
			tab_rep_grafico=utilitario.consultar("select ide_gedep as  ide_label, " +
					"detalle_gedep as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_gedep,detalle_gedep " +
				"order by nom_col_ejex");
			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}
			
			construirGrafico(tab_rep_grafico, "GRAFICO POR DEPARTAMENTOS","DEPARTAMENTO","SUCURSAL");

		}else if (cmb_parametros.getValue().equals("3")){
			// grupo
			tab_rep_grafico=utilitario.consultar("select ide_gegro as  ide_label, " +
					"detalle_gegro as detalle_label, " +
					"ide_sucu as ide_col_ejex, " +
					"nom_sucu as nom_col_ejex, " +
					"avg(resultado_evaluacion)  as monto " +				
				"from (  " +
				""+getSqlReporte(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(),set_sucursal.getSeleccionados())+" " +
				") a " +
				"group by ide_sucu,nom_sucu,ide_gegro,detalle_gegro " +
				"order by nom_col_ejex");
			if (tab_rep_grafico.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No se puede graficar", "No existen datos con los parametros seleccionados");
				return;
			}
			
			construirGrafico(tab_rep_grafico, "GRAFICO POR GRUPO OCUPACIONAL","GRUPO","SUCURSAL");
			
		}
			
		
		
	}


	public void itemSelect(ItemSelectEvent event) {  
		categoryModel=grafico.getCategoryModel(); 

		Map map= categoryModel.getSeries().get(0).getData();
		Iterator it = map.entrySet().iterator();
		List lis_nom_col_eje_x=new ArrayList();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			lis_nom_col_eje_x.add(e.getKey()+"");
		}	     


		String nomina=categoryModel.getSeries().get(event.getSeriesIndex()).getLabel();	     
		map= categoryModel.getSeries().get(event.getSeriesIndex()).getData();

		System.out.println("label "+nomina+" map "+map);
		int int_num_dep=event.getItemIndex();
		String str_nom_col_eje_x="";
		for (int i = 0; i < lis_nom_col_eje_x.size(); i++) {
			if (i==int_num_dep){
				str_nom_col_eje_x=lis_nom_col_eje_x.get(i)+"";
				break;
			}
		}

		it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			if (e.getKey().toString().equalsIgnoreCase(str_nom_col_eje_x)){
				utilitario.agregarMensaje("Item seleccionado: "+nomina, e.getKey() + " " + e.getValue());
			}
		}	     

	}  



	@Override
	public void insertar() {
		//		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		guardarPantalla();
	}

	@Override
	public void eliminar() {
	}


	private ValueExpression crearValueExpression(String valueExpression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
	}


	private ValueExpression crearValueExpression(String valueExpression,Class c) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", c);
	}


	public BarChart getBarchart() {
		return barchart;
	}


	public void setBarchart(BarChart barchart) {
		this.barchart = barchart;
	}



	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}


	public void setCategoryModel(CartesianChartModel categoryModel) {
		this.categoryModel = categoryModel;
	}

	public Tabla getTab_datos() {
		return tab_datos;
	}

	public void setTab_datos(Tabla tab_datos) {
		this.tab_datos = tab_datos;
	}





}


