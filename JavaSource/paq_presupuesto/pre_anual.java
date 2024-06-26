package paq_presupuesto;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_anual extends Pantalla{
	
	private Tabla tab_anual= new Tabla();
	private Tabla tab_mensual= new Tabla();
	private Tabla tab_reforma= new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_clasificador=new SeleccionTabla();

	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private SeleccionTabla sel_resolucion= new SeleccionTabla();

	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_anual(){
		
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);		
		bar_botones.agregarReporte();		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_anual.setId("tab_anual");
		tab_anual.setHeader("PRESUPUESTO ANUAL DE INGRESOS");
		tab_anual.setTabla("pre_anual", "ide_pranu", 1);
		tab_anual.setCondicion("not ide_prcla is null and ide_geani=-1");
		tab_anual.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_anual.getColumna("ide_prcla").setLectura(true);
		tab_anual.getColumna("ide_prcla").setAutoCompletar();
		tab_anual.getColumna("ide_prpro").setVisible(false);
		tab_anual.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_anual.getColumna("ide_geani").setVisible(false);
		tab_anual.getColumna("ide_prfuf").setCombo("select ide_prfuf,detalle_prfuf from pre_fuente_financiamiento order by detalle_prfuf");
		tab_anual.getColumna("valor_reformado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_codificado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_codificado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_codificado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_codificado_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_reformado_h_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_h_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_h_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_d_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_d_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_d_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_devengado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_devengado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_devengado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_precomprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_precomprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_precomprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_recaudado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_inicial_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_precomprometido_pranu").setVisible(false);
		tab_anual.getColumna("valor_eje_comprometido_pranu").setVisible(false);
		tab_anual.getColumna("ide_prpoa").setVisible(false);
		
		tab_anual.getColumna("activo_pranu").setValorDefecto("true");
		tab_anual.agregarRelacion(tab_mensual);
		tab_anual.agregarRelacion(tab_reforma);
		//requeridas
		tab_anual.getColumna("ide_prcla").setRequerida(true);
		tab_anual.getColumna("valor_inicial_pranu").setRequerida(true);

		tab_anual.setTipoFormulario(true);
		tab_anual.getGrid().setColumns(4);
		tab_anual.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anual);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		///// mensual

		tab_mensual.setId("tab_mensual");
		tab_mensual.setHeader("EJECUCION PRESUPUESTARIA MENSUAL");
		tab_mensual.setIdCompleto("tab_tabulador:tab_mensual");
		tab_mensual.setTabla("pre_mensual", "ide_prmen", 2);
		tab_mensual.getColumna("ide_prtra").setLectura(true);
		tab_mensual.getColumna("ide_comov").setLectura(true);
		tab_mensual.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_mensual.getColumna("ide_codem").setLectura(true);
		tab_mensual.getColumna("activo_prmen").setValorDefecto("true");
		tab_mensual.setCondicion("ide_pranu is not null");
		//tab_mensual.setTipoFormulario(true);
		//tab_mensual.getGrid().setColumns(6);
		tab_mensual.setRows(20);
		tab_mensual.setLectura(true);
		tab_mensual.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_mensual);
		
        //REFORMA MES
		tab_reforma.setId("tab_reforma");
		tab_reforma.setHeader("REFORMA PRESUPUESTARIA MENSUAL");
		tab_reforma.setIdCompleto("tab_tabulador:tab_reforma");
		tab_reforma.setTabla("pre_reforma_mes", "ide_prrem", 3);
		tab_reforma.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_reforma.getColumna("val_reforma_h_prrem").setMetodoChange("calcular");
		tab_reforma.getColumna("val_reforma_d_prrem").setMetodoChange("calcular");
		tab_reforma.getColumna("activo_prrem").setValorDefecto("true");
		tab_reforma.dibujar();
		
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);
		
		tab_tabulador.agregarTab("EJECUCION MENSUAL", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA PRESUPUESTARIA MENSUAL",pat_panel3);

		Division div_division =new Division ();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "h");
		agregarComponente(div_division);

		Boton bot_agregar=new Boton();
		bot_agregar.setValue("Agregar Partida Presupuestaria");
		bot_agregar.setMetodo("agregarClasificador");
		bar_botones.agregarBoton(bot_agregar);

		set_clasificador.setId("set_clasificador");
		set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_clasificador.setRadio(); //solo selecciona una opcion
		set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla"); 
		set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true); //pone filtro
		set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);//pone filtro
		set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
		agregarComponente(set_clasificador);
		
		inicializarSelResolucion();
	}
	
	public void seleccionaElAnio(){
		if(com_anio.getValue()!=null){
			tab_anual.setCondicion("not ide_prcla is null and ide_geani="+com_anio.getValue());
			tab_anual.ejecutarSql();
			tab_mensual.ejecutarValorForanea(tab_anual.getValorSeleccionado());
			tab_reforma.ejecutarValorForanea(tab_anual.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		//Si la tabla esta vacia
		if(tab_anual.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		//Filtrar los clasificadores del año seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString()));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
		if(set_clasificador.getValorSeleccionado()!=null){
			tab_anual.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_anual");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}
	
	///// para subir vaslores de un tabla a otra 
	public void  calcularValor(){
		double dou_valor_h=0;
		double dou_valor_d=0;
		double dou_valor_reformado_debito=0;
		double dou_valor_codificado=0;
		double dou_valor_inicial=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_inicial=pckUtilidades.CConversion.CDbl_2(tab_anual.getValor("valor_inicial_pranu"));
		} catch (Exception e) {
		}
		
		String valor1=tab_reforma.getSumaColumna("val_reforma_h_prrem")+"";
		dou_valor_h=pckUtilidades.CConversion.CDbl_2(valor1);

		String valor2=tab_reforma.getSumaColumna("val_reforma_d_prrem")+"";
		dou_valor_d=pckUtilidades.CConversion.CDbl_2(valor2);
		dou_valor_reformado_debito=dou_valor_d-dou_valor_h;
		dou_valor_codificado=dou_valor_inicial+dou_valor_reformado_debito;

		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_anual.setValor("valor_reformado_h_pranu",utilitario.getFormatoNumero(valor1,3));
		tab_anual.setValor("valor_reformado_d_pranu",utilitario.getFormatoNumero(valor2,3));
		tab_anual.setValor("valor_reformado_pranu",utilitario.getFormatoNumero(dou_valor_reformado_debito,3));
		tab_anual.setValor("valor_codificado_pranu",utilitario.getFormatoNumero(dou_valor_codificado,3));
		
		tab_anual.modificar(tab_anual.getFilaActual());//para que haga el update

		utilitario.addUpdateTabla(tab_anual, "valor_reformado_h_pranu,valor_reformado_d_pranu,valor_reformado_pranu,valor_codificado_pranu", "tab_reforma");	
	
	}
/// 
	public void calcular(AjaxBehaviorEvent evt) {
		tab_reforma.modificar(evt); //Siempre es la primera linea
		calcularValor();

	}
	
	public void inicializarSelResolucion(){
		//dialogo para reporte
		sel_resolucion.setId("sel_resolucion");
		sel_resolucion.setSeleccionTabla("select ide_gemes, detalle_gemes, activo_gemes from gen_mes order by ide_gemes","ide_gemes");
		sel_resolucion.setRadio();
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_resolucion);
	
	}
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		rep_reporte.dibujar();
	}
	
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if(rep_reporte.getReporteSelecionado().equals("Reforma Ingreso")){
			
			if (rep_reporte.isVisible()){
				map_parametros=new HashMap();		
				rep_reporte.cerrar();
				map_parametros.clear();
				sel_resolucion.dibujar();
			}
			else if(sel_resolucion.isVisible()) {
				
				if(sel_resolucion.getValorSeleccionado()!=null){	
					map_parametros.put("titulo","REFORMA DE INGRESOS");
					map_parametros.put("ide_gemes",  pckUtilidades.CConversion.CInt(sel_resolucion.getValorSeleccionado()));
					map_parametros.put("ide_geani",  pckUtilidades.CConversion.CInt(com_anio.getValue()+""));	
					map_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
					map_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
					map_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));
					map_parametros.put("pie_especialista_pres",  utilitario.getVariable("p_pie_especialista_pres"));
	
					sel_resolucion.cerrar();
					
					sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
					sel_rep.dibujar(); 
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}

		} 
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;

		}
		if(tab_anual.isFocus()){
			tab_anual.insertar();
			tab_anual.setValor("ide_geani",com_anio.getValue()+"");
			}
		else if(tab_mensual.isFocus()){
			tab_mensual.insertar();
		}
			else if(tab_reforma.isFocus()){
				tab_reforma.insertar();
				
			}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_anual.guardar()){
			if(tab_mensual.guardar()){
				if(tab_reforma.guardar()){
					
				}
			}
		}
		guardarPantalla();
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_anual() {
		return tab_anual;
	}

	public void setTab_anual(Tabla tab_anual) {
		this.tab_anual = tab_anual;
	}

	public Tabla getTab_mensual() {
		return tab_mensual;
	}

	public void setTab_mensual(Tabla tab_mensual) {
		this.tab_mensual = tab_mensual;
	}

	public Tabla getTab_reforma() {
		return tab_reforma;
	}

	public void setTab_reforma(Tabla tab_reforma) {
		this.tab_reforma = tab_reforma;
	}
	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}
	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSel_rep() {
		return sel_rep;
	}

	public void setSel_rep(SeleccionFormatoReporte sel_rep) {
		this.sel_rep = sel_rep;
	}

	public SeleccionTabla getSel_resolucion() {
		return sel_resolucion;
	}

	public void setSel_resolucion(SeleccionTabla sel_resolucion) {
		this.sel_resolucion = sel_resolucion;
	}
	

}
