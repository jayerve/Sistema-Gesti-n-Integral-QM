package paq_general;

import java.awt.image.BandedSampleModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.Painter;

import org.apache.poi.hssf.record.formula.TblPtg;
import org.primefaces.component.api.UIData;
import org.primefaces.component.paginator.PaginatorElementRenderer;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;



import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import groovy.util.IFileNameFinder;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;


public class pre_estructura_organizacional extends Pantalla{

	private Tabla tab_poa=new Tabla();
	private Tabla tab_area=new Tabla();
	private Confirmar con_aprobar_area= new Confirmar();
	private boolean area=false;
	private Tabla tab_vigente=new Tabla();
	private Tabla tab_grupo_ocupacional=new Tabla();
	private Tabla tab_cargo_funcional=new Tabla();
	private Tabla tab_departamento_sucursal=new Tabla();
	private Tabla tab_financiamiento=new Tabla();
	private Tabla tab_funcion_programa=new Tabla();
	private Tabla tab_financiamiento_reforma=new Tabla();
	private Tabla tab_financiamiento_ejecucion=new Tabla();
	private Combo com_anio=new Combo();

	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private SeleccionTabla set_clasificador_actua=new SeleccionTabla();
	
	private SeleccionTabla set_funcion=new SeleccionTabla();
	private SeleccionTabla set_actualizarfuncion=new SeleccionTabla();
	private SeleccionTabla set_sub_actividad=new SeleccionTabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_saldo_fuente=new SeleccionTabla();
	private Dialogo dia_area_departamento = new Dialogo();
	private Dialogo dia_area = new Dialogo();

	private Check chk_area_departamento = new Check();
	private Confirmar con_guardar=new Confirmar();
	private Arbol arb_arbol = new Arbol();
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private SeleccionTabla sel_poa= new SeleccionTabla();
	private SeleccionTabla sel_resolucion= new SeleccionTabla();
	private String nivel="";
	private Boolean bandNivel=false;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);


	public pre_estructura_organizacional(){
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

	
		Etiqueta eti_area_dept=new Etiqueta("Es una GERENCIA o AREA");
		chk_area_departamento.setId("chk_area_departamento");
		PanelGrid panGri = new PanelGrid();
		panGri.setColumns(2);
		panGri.getChildren().add(eti_area_dept);
		panGri.getChildren().add(chk_area_departamento);
		panGri.getChildren().add(eti_area_dept);
		panGri.getChildren().add(chk_area_departamento);
		
		//Dialogo confirmacion aplica o no aplica a vacacion
		
		dia_area_departamento.setId("dia_area_departamento");
		dia_area_departamento.setTitle("CREACIÓN DE NUEVO REGISTRO");
		dia_area_departamento.getBot_aceptar().setMetodo("insertarArea");
		dia_area_departamento.getGri_cuerpo().getChildren().add(panGri);
		dia_area_departamento.setWidth("20%");
		dia_area_departamento.setHeight("15%");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_area_departamento);
		
		
		
		
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_poa.setId("tab_poa");   
		tab_poa.setHeader("ESTRUCTURA ORGANIZACIONAL");
		tab_poa.setTabla("gen_estructura_organizacional","ide_geeso",1);
		tab_poa.setCampoPadre("pre_ide_geeso");
	    tab_poa.setCampoNombre("DETALLE_GEESO");
	    tab_poa.getColumna("DETALLE_GEESO").setLectura(true);
		tab_poa.getColumna("DETALLE_GEESO").setNombreVisual("DESCRIPCIÓN");
	    tab_poa.getColumna("IDE_GEARE").setCombo("SELECT IDE_GEARE,DETALLE_GEARE from GEN_AREA where ACTIVO_GEARE=TRUE AND ide_nivel_geare in(1,2,3,4,5) "
	    		+ "order by ide_nivel_geare asc,detalle_geare asc");
	    tab_poa.getColumna("IDE_GEARE").setMetodoChange("obtenerAreaDepartamento");
	    tab_poa.getColumna("IDE_GEDEP").setCombo("select ide_gedep,detalle_gedep from GEN_DEPARTAMENTO ");
	    tab_poa.getColumna("IDE_GEDEP").setMetodoChange("getDescripcionDepartamento");
	    tab_poa.getColumna("IDE_GEDEP").setNombreVisual("DEPARTAMENTO");
	    tab_poa.getColumna("ACTIVO_GEESO").setCheck();
	    tab_poa.getColumna("ACTIVO_GEESO").setValorDefecto("true");
	
	    tab_poa.setCondicion("ide_geeso=-1");

	    tab_poa.setCampoOrden("ide_gEESO desc");
	    tab_poa.agregarRelacion(tab_vigente);
	    tab_poa.agregarRelacion(tab_grupo_ocupacional);
        tab_poa.agregarArbol(arb_arbol);
        ///tab_poa.onSelect(metodo);
		tab_poa.dibujar();
		PanelTabla pat_poa=new PanelTabla();
 		pat_poa.setPanelTabla(tab_poa);
	
		
		// tabla deaÃ±os vigente
		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("AÑO VIGENTE");
		tab_vigente.setTabla("gen_vigente_estructura_organizacional", "ide_geveo", 2);
		tab_vigente.setCondicion("not ide_geeso is null");
		tab_vigente.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_vigente.getColumna("ide_geani").setNombreVisual("AÑO");
		tab_vigente.getColumna("ide_geani").setUnico(true);
		tab_vigente.getColumna("ide_geeso").setUnico(true);
		tab_vigente.getColumna("activo_geveo").setCheck();
		tab_vigente.getColumna("activo_geveo").setValorDefecto("true");
		tab_vigente.dibujar();
		PanelTabla pat_vigente=new PanelTabla();
		pat_vigente.setPanelTabla(tab_vigente);


		tab_grupo_ocupacional.setId("tab_grupo_ocupacional");
		tab_grupo_ocupacional.setHeader("GRUPO OCUPACIONAL / CARGO FUNCIONAL");

		tab_grupo_ocupacional.setTabla("gen_estructura_grupo_cargo", "ide_geegc", 3);
		tab_grupo_ocupacional.getColumna("activo_geegc").setCheck();
		tab_grupo_ocupacional.getColumna("activo_geegc").setValorDefecto("true");
		tab_grupo_ocupacional.getColumna("ide_gegro").setCombo("GEN_GRUPO_OCUPACIONAL","IDE_GEGRO" , "DETALLE_GEGRO", "");
		tab_grupo_ocupacional.getColumna("ide_gegro").setMetodoChange("getCargoFuncional");
		
		//tab_grupo_ocupacional.getColumna("ide_gecaf").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF" , "DETALLE_GECAF", "");
		
		tab_grupo_ocupacional.getColumna("ide_gecaf").setCombo("select GGC.IDE_GECAF,GGC.DETALLE_GECAF from GEN_CARGO_FUNCIONAL GGC "
	//			+ "LEFT JOIN GEN_CARGO_FUNCIONAL GCF ON GCF.IDE_GECAF=GGC.IDE_GEGRO "
				+ "WHERE GGC.DETALLE_GECAF IS NOT NULL");
		
		//tab_grupo_ocupacional.getColumna("DETALLE_GEGRO").setRequerida(true);
		//tab_grupo_ocupacional.getColumna("SIGLAS_GEGRO").setVisible(false);
		//tab_grupo_ocupacional.onSelect("seleccionarTabla1");
		//tab_grupo_ocupacional.getColumna("IDE_GTNIV").setCombo("GTH_NIVEL_VIATICO","IDE_GTNIV" , "DETALLE_GTNIV", "");
		//tab_grupo_ocupacional.getColumna("IDE_GTNIV").setVisible(false);
		//tab_grupo_ocupacional.getColumna("subrogacion_gegro").setVisible(false);
		//tab_grupo_ocupacional.getColumna("rmu_gegro").setVisible(false);

		
		 
		tab_grupo_ocupacional.dibujar();
		PanelTabla pat_grupo_ocupacional = new PanelTabla();
		pat_grupo_ocupacional.setPanelTabla(tab_grupo_ocupacional);
		
		
		tab_departamento_sucursal.setId("tab_departamento_sucursal");
		tab_departamento_sucursal.setGenerarPrimaria(false);
		tab_departamento_sucursal.setHeader("SUCURSAL / AREA / DEPARTAMENTO");
		tab_departamento_sucursal.setTabla("GEN_DEPARTAMENTO_SUCURSAL", "IDE_SUCU", 4);
		tab_departamento_sucursal.getColumna("IDE_GEDEP").setVisible(false);
		tab_departamento_sucursal.getColumna("IDE_GEARE").setVisible(false);
		tab_departamento_sucursal.getColumna("IDE_GEDEP").setUnico(true);
		tab_departamento_sucursal.getColumna("IDE_GEARE").setUnico(true);
		tab_departamento_sucursal.getColumna("IDE_SUCU").setUnico(true);
		tab_departamento_sucursal.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU","");
		tab_departamento_sucursal.getColumna("ACTIVO_GEDES").setCheck();
		tab_departamento_sucursal.getColumna("ACTIVO_GEDES").setValorDefecto("true");
		tab_departamento_sucursal.getColumna("IDE_GEESO").setVisible(false);;
		tab_departamento_sucursal.setMostrarcampoSucursal(true);
		tab_departamento_sucursal.setRows(10);
		tab_departamento_sucursal.setCondicion("IDE_GEARE=-1 and IDE_GEDEP=-1");
		tab_departamento_sucursal.dibujar();
		tab_departamento_sucursal.getColumna("IDE_SUCU").setExterna(false);
		PanelTabla pat_panel1 = new PanelTabla();
		PanelTabla pat_departamento_sucursal=new PanelTabla();
		pat_departamento_sucursal.setPanelTabla(tab_departamento_sucursal);
		
		tab_area.setId("tab_area");
		tab_area.setHeader("AREA");
		tab_area.setTabla("GEN_AREA", "IDE_GEARE", 5);
		tab_area.getColumna("IDE_GEARE").setVisible(false);
		tab_area.getColumna("IDE_PADRE_GEARE").setCombo("GEN_AREA", "IDE_GEARE", "DETALLE_GEARE","");
		tab_area.getColumna("ACTIVO_GEARE").setCheck();
		tab_area.getColumna("ACTIVO_GEARE").setValorDefecto("true");
		tab_area.setRows(10);
		tab_area.setCondicion("IDE_GEARE=-1");
		tab_area.dibujar();
		PanelTabla pat_area = new PanelTabla();
		pat_area.setPanelTabla(tab_area);
		/*
		tab_cargo_funcional.setId("tab_cargo_funcional");
		tab_cargo_funcional.setGenerarPrimaria(false);
		tab_cargo_funcional.setTabla("GEN_GRUPO_CARGO", "IDE_GEGRO,IDE_GECAF",4);// clave
		// primaria
		// compuesta
		tab_cargo_funcional.getColumna("0ACTIVO_GEGRC").setCheck();
		tab_cargo_funcional.getColumna("ACTIVO_GEGRC").setValorDefecto("true");
		tab_cargo_funcional.getColumna("IDE_GEGRO").setLectura(true);
		//tab_tabla2.getColumna("IDE_GEGRO").setVisible(false);
		tab_cargo_funcional.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL",
				"IDE_GECAF", "DETALLE_GECAF,SIGLAS_GECAF", "");
		tab_cargo_funcional.setValidarInsertar(true);// Para que solo inserte de una en
		tab_cargo_funcional.onSelect("seleccionarTabla2");
		// una
		filtrarGrupoCargos();
		tab_cargo_funcional.dibujar();
		PanelTabla pat_cargo_funcional = new PanelTabla();
		pat_cargo_funcional.setMensajeWarn("GRUPO CARGO ");
		pat_cargo_funcional.setPanelTabla(tab_cargo_funcional);

		
		
	*/	
		
	
		tab_tabulador.agregarTab("ESTRUCTURA ORGANIZACIONAL", pat_poa);//intancia los tabuladores 
		tab_tabulador.agregarTab("GRUPO OCUPACIONAL", pat_grupo_ocupacional);//intancia los tabuladores 
		//tab_tabulador.agregarTab("CARGO FUNCIONAL",pat_cargo_funcional);

		
		arb_arbol.setId("arb_arbol");
		arb_arbol.onSelect("seleccionoClasificador");
		arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where not ide_geeso is null and ide_geani=-1 )");
		arb_arbol.ejecutarSql();	
		arb_arbol.dibujar();
		
		Division div_depart2 = new Division(); //UNE OPCION Y DIV 2
		div_depart2.setId("div_depart2");
		div_depart2.dividir2(pat_poa,pat_departamento_sucursal,"70%","H");
		
		Division div2 = new Division(); //UNE OPCION Y DIV 2
		div2.setId("div2");
		div2.dividir2(div_depart2,pat_grupo_ocupacional,"50%","V");


		Division div3 = new Division(); //UNE OPCION Y DIV 2
		div3.setId("div3");

		//div3.dividir2(pat_poa, tab_tabulador, "50%", "H");
		
		div3.dividir2(div2, pat_vigente, "70%", "H");
		//div3.dividir1(pat_poa);
		Division div_division = new Division();
		div_division.setId("div_division");
	//	div_division.dividir2(arb_arbol, div3, "40%", "V");  //arbol y div3
		//div_division.dividir1(arb_arbol);
		div_division.dividir2(arb_arbol, div3, "25%", "V");
		agregarComponente(div_division);



		con_aprobar_area.setId("con_aprobar_area");
		con_aprobar_area.setMessage("Esta seguro que desea insertar");
		con_aprobar_area.setTitle("CONFIRMACION APROBACION DE INGRESO");
		con_aprobar_area.getBot_aceptar().setMetodo("insertarAreaAceptar");
	//	con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
		agregarComponente(con_aprobar_area);


	
	}
	public void actualizarEjecucionMensual(AjaxBehaviorEvent evt) {
	//	tab_mes.modificar(evt); //Siempre es la primera linea
//		tab_mes.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mes");
 
	}
	
	
	public void actualizarEjecucionMensualCodificado(AjaxBehaviorEvent evt) {
	//	tab_mes_codificado.modificar(evt); //Siempre es la primera linea
//		tab_mes_codificado.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mes_codificado");
 
	}
	//valor Financiamiento
	// para subir vaslores de un tabla a otra 
	public void  calcularValor(){
		double dou_valor_finan=0;
		double dou_valor_codificado=0;
		double dou_valor_refor=0;
		
		try {
			//Obtenemos el valor de la cantidad
	//		dou_valor_refor=pckUtilidades.CConversion.CDbl_2(tab_reforma.getValor("valor_reformado_prpor"));
		} catch (Exception e) {
		}
		
		
		
		String valor_financiamiento=tab_financiamiento.getSumaColumna("valor_financiamiento_prpof")+"";
		dou_valor_finan=pckUtilidades.CConversion.CDbl_2(valor_financiamiento);
		dou_valor_codificado=dou_valor_finan+dou_valor_refor;

		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_poa.setValor("presupuesto_inicial_prpoa",utilitario.getFormatoNumero(valor_financiamiento,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "presupuesto_inicial_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorFinanciamiento(AjaxBehaviorEvent evt) {
		tab_financiamiento.modificar(evt); //Siempre es la primera linea
		calcularValor();

	}

	/////valor reforma
	// para subir vaslores de un tabla a otra Financiamiento 
	public void  calcularReforma(){
		double dou_valor_refor=0;
		double dou_valor_codificado=0;
		double dou_valor_finan=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_finan=pckUtilidades.CConversion.CDbl_2(tab_financiamiento.getValor("valor_financiamiento_prpof"));
		} catch (Exception e) {
		}
		
	//	String valor_reforma=tab_reforma.getSumaColumna("valor_reformado_prpor")+"";
	//	dou_valor_refor=pckUtilidades.CConversion.CDbl_2(valor_reforma);
		//calcula valor codficado
//		dou_valor_codificado=dou_valor_finan+dou_valor_refor;
		
		//Asignamos el total a la tabla detalle, con 2 decimales
	//	tab_poa.setValor("reforma_prpoa",utilitario.getFormatoNumero(valor_reforma,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "reforma_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorReforma(AjaxBehaviorEvent evt) {
		//tab_reforma.modificar(evt); //Siempre es la primera linea
		calcularReforma();

	}


	public void seleccionar_arbol(NodeSelectEvent evt) {
		if(com_anio.getValue()==null){
		//	utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
		//	return;
		}

		tab_poa.limpiar();	
		//Asigna evento al arbol
		arb_arbol.seleccionarNodo(evt);
		//Filtra la tabla Padre
		tab_poa.ejecutarValorPadre(arb_arbol.getValorSeleccionado());
		//Filtra la tabla tab_vigente
		tab_vigente.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		tab_grupo_ocupacional.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		filtrarDepartamentoSucursal();
		//arb_arbol.seleccionarNodo(evt);
		//tab_poa.setCondicion("ide_geare="+arb_arbol.getValorSeleccionado());
		//tab_poa.setCondicion("ide_geare="+arb_arbol.getValorSeleccionado());
		//tab_poa.ejecutarSql();		
		//tab_poa.getColumna("IDE_geare").setValorDefecto(arb_arbol.getValorSeleccionado());
		//tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//	tab_mes_codificado.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_reforma.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_archivo.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		///tab_financiamiento.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_financiamiento_reforma.ejecutarValorForanea(tab_poa.getValorSeleccionado());
	//	tab_financiamiento_ejecucion.ejecutarValorForanea(tab_poa.getValorSeleccionado());
	
	}

	public void actualizadorClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
				
		set_clasificador_actua.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString()));
		set_clasificador_actua.getTab_seleccion().ejecutarSql();
		set_clasificador_actua.dibujar();
	}
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		/*
		//Si la tabla esta vacia
		if(tab_poa.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		*/
		//Filtrar los clasificadores del año seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString()));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
		if(set_clasificador.getValorSeleccionado()!=null){
			tab_poa.insertar();
			tab_poa.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			tab_poa.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_poa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}

	public void aceptarActualizaClasificador(){
		if(set_clasificador_actua.getValorSeleccionado()!=null){			
			tab_poa.setValor("ide_prcla", set_clasificador_actua.getValorSeleccionado());
			tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
			//Actualiza 
			tab_poa.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_poa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador_actua.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}	
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Plan Operativo Anual (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
				
			}
			else if(sel_poa.isVisible()) {
				
				if(sel_poa.getListaSeleccionados().size()>0){	
					p_parametros.put("ide_prpoa",sel_poa.getSeleccionados());
					p_parametros.put("titulo","Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_poa.cerrar();
				   self_reporte.dibujar();
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
		
		
		/////REFORMA
		else if(rep_reporte.getReporteSelecionado().equals("Reforma Plan Operativo Anual (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
			}
			else if(sel_resolucion.isVisible()) {
				
				if(sel_resolucion.getListaSeleccionados().size()>0){	
					p_parametros.put("pnro_resolucion",sel_resolucion.getSeleccionados());
					p_parametros.put("titulo","Reforma Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
	
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_resolucion.cerrar();
				   self_reporte.dibujar();
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
		
		//REFORMA Fuente
		else if(rep_reporte.getReporteSelecionado().equals("Reforma Fuentes (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
			}
			else if(sel_resolucion.isVisible()) {
				
				if(sel_resolucion.getListaSeleccionados().size()>0){	
					p_parametros.put("pnro_resolucion",sel_resolucion.getSeleccionados());
					p_parametros.put("titulo","Reforma-Fuentes Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
	
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_resolucion.cerrar();
				    self_reporte.dibujar();
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
		if (tab_poa.isFocus()) {
			tab_departamento_sucursal.limpiar();
			//System.out.println("valorrr"+tab_poa.getValorSeleccionado());
		//	tab
dia_area_departamento.dibujar();
			//tab_poa.insertar();
			
			
			//tab_poa.getColumna("DETALLE_GEESO").setLectura(true);

		}
		else if (tab_vigente.isFocus()) {
			tab_vigente.insertar();
		}
		else if (tab_grupo_ocupacional.isFocus()){
			//if (tab_poa.isFilaInsertada() == false && tab_poa.isEmpty() == false) {
			tab_grupo_ocupacional.insertar();
			//TablaGenerica tabPoa=utilitario.consultar("select ide_geeso,detalle_geeso where ide_geeso="+tab_poa.getValorSeleccionado());
			//utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional set IDE_GEGRO=0.0, nro_dias_ajuste_asvac=" + valor + " where ide_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));

//			tab_poa.setValor("IDE_GEGRO", tab_grupo_ocupacional.getValor("IDE_GEGRO"));
	//		tab_poa.setValor("IDE_GECAF", tab_grupo_ocupacional.getValor("IDE_GECAF"));
		}		else if (tab_area.isFocus()) {
			tab_area.insertar();
		}else if (tab_departamento_sucursal.isFocus()) {
			
			
if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().toString().equals("") || arb_arbol.getValorSeleccionado().toString().isEmpty()) {
	utilitario.agregarMensajeInfo("No se puede asignar sucursal", "El registro no contiene area o departamento");
	return;
}
			TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
					+ "ide_geare, ide_gedep "
					+ "FROM gen_estructura_organizacional  "
					+ "Where ide_geeso="+tab_poa.getValorSeleccionado());
			System.out.println("TABLA "+tab_poa.getValorSeleccionado());
			System.out.println("TArbol "+arb_arbol.getValorSeleccionado());
			
			if (tabEstructura.getTotalFilas()<=0) {
				utilitario.agregarMensajeInfo("No existen datos", "No se puede ingresar");
				return;
			
			}else {
						
			if (tabEstructura.getValor("IDE_GEARE")==null || tabEstructura.getValor("IDE_GEARE").equals("") || tabEstructura.getValor("IDE_GEARE").isEmpty() ) {
				utilitario.agregarMensajeInfo("No se puede asignar sucursal", "El registro no contiene area");
				return;
			}
			if (tabEstructura.getValor("IDE_GEDEP")==null || tabEstructura.getValor("IDE_GEDEP").equals("") || tabEstructura.getValor("IDE_GEDEP").isEmpty() ) {
				utilitario.agregarMensajeInfo("No se puede asignar sucursal", "El registro no contiene departamento");
				return;
			}
			tab_departamento_sucursal.insertar();
			tab_departamento_sucursal.setValor("IDE_GEESO", tab_poa.getValorSeleccionado());
			tab_departamento_sucursal.setValor("IDE_GEARE", tabEstructura.getValor("IDE_GEARE"));
			tab_departamento_sucursal.setValor("IDE_GEDEP", tabEstructura.getValor("IDE_GEDEP"));
		
			}
			}
		
	}

	@Override
	public  void guardar() {
   	if(tab_poa.isEmpty()){
			if (!tab_poa.guardar()) {
				   
				   /*if(tab_funcion_programa.isFilaInsertada()){
					   System.out.println("es uevo registro ");
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_programa)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_programa),par_sec_programa);

					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_proyecto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_proyecto),par_sec_proyecto);

					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_producto),par_sec_producto);
					
					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_fase)){
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_fase),par_sec_fase);

					}
				   }*/
				
				tab_vigente.guardar();
				//tab_grupo_ocupacional.setValor("IDE_GEESO", tab_poa.getValor("IDE_GEESO"));
				tab_grupo_ocupacional.guardar();
					guardarPantalla();
					//Actualizar el arbol
					arb_arbol.ejecutarSql();
					utilitario.addUpdate("arb_arbol");
					
			}
			return;
		}
		else if(tab_poa.isFocus()){
			if (area==true) {
			if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().toString().equals("")) {
				int nivel=1;

			/*	TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
						+ "ide_geare, ide_gedep "
						+ "FROM gen_estructura_organizacional  "
						+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
				*/		
				TablaGenerica tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
						+ "FROM "
						+ "GEN_AREA "
						+ "WHERE ACTIVO_GEARE=TRUE "
						+ "AND IDE_NIVEL_GEARE=0 "
						+ "ORDER BY IDE_GEARE ASC");
	
				if (tab_poa.getValor("DETALLE_GEESO")==null || tab_poa.getValor("DETALLE_GEESO").equals("") || tab_poa.getValor("DETALLE_GEESO").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el area");
					return;
				}else if(tab_poa.getValor("IDE_GEESO")!=null){
								
					TablaGenerica tabAreaIngresada= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "AND IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
							+ "ORDER BY IDE_GEARE ASC");
						
				if (tabAreaIngresada.getTotalFilas()>0) {
				if (!tabAreaIngresada.getValor("DETALLE_GEARE").equals(tab_poa.getValor("DETALLE_GEESO"))) {
				//utilitario.getConexion().ejecutarSql("update gen_area set DETALLE_GEARE='"+tab_poa.getValor("DETALLE_GEESO")+"' where ide_GEESO=" + tab_poa.getValor("IDE_GEESO"));
				utilitario.agregarMensajeInfo("No se puede cambiar", "El area seleccionada");
					return;	
				}
					}else {
						insertarArea(tab_poa.getValor("DETALLE_GEESO"), Integer.parseInt(tabArea.getValor("IDE_GEARE")), nivel);
						TablaGenerica tabArea1= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
								+ "FROM "
								+ "GEN_AREA "
								+ "WHERE ACTIVO_GEARE=TRUE "
								+ "ORDER BY IDE_GEARE DESC LIMIT 1");
					
						if (tabArea1.getTotalFilas()>0) {
							tab_poa.modificar(tab_poa.getFilaActual());
							tab_poa.getColumna("IDE_GEARE").setLectura(false);
							tab_poa.setValor("IDE_GEARE", tabArea1.getValor("IDE_GEARE"));
							tab_poa.guardar();
							guardarPantalla();
							utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
									+ "set ide_geare=" +Integer.parseInt(tabArea1.getValor("IDE_GEARE"))  + " "
									+ "where ide_geeso=" + tab_poa.getValorSeleccionado());
							insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));
						}			
					}
					
				}else {
					insertarArea(tab_poa.getValor("DETALLE_GEESO"), Integer.parseInt(tabArea.getValor("IDE_GEARE")), nivel);
					TablaGenerica tabArea1= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "ORDER BY IDE_GEARE DESC LIMIT 1");
				
					if (tabArea1.getTotalFilas()>0) {
						tab_poa.modificar(tab_poa.getFilaActual());
						tab_poa.getColumna("IDE_GEARE").setLectura(false);
						tab_poa.setValor("IDE_GEARE", tabArea1.getValor("IDE_GEARE"));
						tab_poa.guardar();
						guardarPantalla();
						utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
								+ "set ide_geare=" +Integer.parseInt(tabArea1.getValor("IDE_GEARE"))  + " "
								+ "where ide_geeso=" + tab_poa.getValorSeleccionado());
						insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));
					}			
					
				}
						
					//Actualizar el arbol
					arb_arbol.ejecutarSql();
					utilitario.addUpdate("arb_arbol");
				
		
			}else {

		      TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
						+ "ide_geare, ide_gedep "
						+ "FROM gen_estructura_organizacional  "
						+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
						
				TablaGenerica tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
						+ "FROM "
						+ "GEN_AREA "
						+ "WHERE ACTIVO_GEARE=TRUE "
						+ "AND IDE_GEARE="+tabEstructura.getValor("IDE_GEARE")
						+ "ORDER BY IDE_GEARE ASC");
		
				String parametro="12,13,14,15";
				String[] array = parametro.split(",");
				boolean banderaArea=true;
				if (array.length > 0) {
					for (int j = 0; j < array.length; j++) {
						   if (array[j].equals(tabArea.getValor("IDE_GEARE"))) {
									banderaArea=true;
									j=array.length;
								
						   }else {
									banderaArea=false;
								}		
			}
		
					
					if (banderaArea==false) {
						utilitario.agregarMensajeInfo("Revise el nivel jerarquico", "No se puede ingresar");
						return;
					}else {
						
					}
					
				}
				
					
				if (tab_poa.getValor("DETALLE_GEESO")==null || tab_poa.getValor("DETALLE_GEESO").equals("") || tab_poa.getValor("DETALLE_GEESO").isEmpty()) {
					//
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el area");
					return;
				}else if(tab_poa.getValor("IDE_GEESO")!=null || !tab_poa.getValor("IDE_GEESO").equals("") || !tab_poa.getValor("IDE_GEESO").isEmpty()){
								
					TablaGenerica tabAreaIngresada= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "AND IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
							+ "ORDER BY IDE_GEARE ASC");
						
				if (tabAreaIngresada.getTotalFilas()>0) {
				if (!tabAreaIngresada.getValor("DETALLE_GEARE").equals(tab_poa.getValor("DETALLE_GEESO"))) {
					//tiene ingresado en las dos tablas tanto estructura organizacional como en la de area
				//utilitario.getConexion().ejecutarSql("update gen_area set DETALLE_GEARE='"+tab_poa.getValor("DETALLE_GEESO")+"' where ide_GEESO=" + tab_poa.getValor("IDE_GEESO"));
					utilitario.agregarMensajeInfo("No se puede guardar no puede cambiar el area", "Debe ingresar el area");
					return;	
				}
					}else {
						//Si tiene ingresada en la tabla estructura organizacional pero no se encuentra en la tabla area
						TablaGenerica tabArea1= utilitario.consultar("SELECT IDE_GEARE,NIVEL_GEARE "
								+ "FROM "
								+ "GEN_AREA "
								+ "WHERE ACTIVO_GEARE=TRUE "
								+ "AND IDE_PADRE_GEARE="+tabArea.getValor("IDE_GEARE")
								+ "GROUP BY IDE_GEARE ASC");
				
						insertarArea(tab_poa.getValor("DETALLE_GEESO"), Integer.parseInt(tabArea.getValor("IDE_GEARE")), Integer.parseInt(tabArea1.getValor("nivel_geare")));
					
						TablaGenerica tabArea2= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
								+ "FROM "
								+ "GEN_AREA "
								+ "WHERE ACTIVO_GEARE=TRUE "
								+ "ORDER BY IDE_GEARE DESC LIMIT 1");
					
						if (tabArea2.getTotalFilas()>0) {
							tab_poa.setValor("IDE_GEARE", tabArea2.getValor("IDE_GEARE"));
							guardarPantalla();
							//Actualizar el arbol
							utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
									+ "set ide_geare=" +Integer.parseInt(tabArea2.getValor("IDE_GEARE"))  + " "
									+ "where ide_geeso=" + tab_poa.getValorSeleccionado());
							insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));

							arb_arbol.ejecutarSql();
							utilitario.addUpdate("arb_arbol");
						
						}			
					}
				
				
				}else {

					//Si no tiene ingresada el area
					TablaGenerica tabArea1= utilitario.consultar("SELECT IDE_GEARE,NIVEL_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "AND IDE_PADRE_GEARE="+tabArea.getValor("IDE_GEARE")
							+ "GROUP BY IDE_GEARE ASC");
			
					insertarArea(tab_poa.getValor("DETALLE_GEESO"), Integer.parseInt(tabArea.getValor("IDE_GEARE")), Integer.parseInt(tabArea1.getValor("nivel_geare")));
				
					TablaGenerica tabArea2= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "ORDER BY IDE_GEARE DESC LIMIT 1");
				
					if (tabArea2.getTotalFilas()>0) {
						tab_poa.setValor("IDE_GEARE", tabArea2.getValor("IDE_GEARE"));
						guardarPantalla();
						//Actualizar el arbol
						utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
								+ "set ide_geare=" +Integer.parseInt(tabArea2.getValor("IDE_GEARE"))  + " "
								+ "where ide_geeso=" + tab_poa.getValorSeleccionado());
						insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));

						arb_arbol.ejecutarSql();
						utilitario.addUpdate("arb_arbol");
					
					}
					
					
				}
		
				
			}
				
			
			
			}else {
// Si es departamento
				TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
							
					TablaGenerica tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
							+ "FROM "
							+ "GEN_AREA "
							+ "WHERE ACTIVO_GEARE=TRUE "
							+ "AND IDE_GEARE="+tabEstructura.getValor("IDE_GEARE")
							+ "ORDER BY IDE_GEARE ASC");
			
					if (tab_poa.getValor("DETALLE_GEESO")==null || tab_poa.getValor("DETALLE_GEESO").equals("") || tab_poa.getValor("DETALLE_GEESO").isEmpty()) {
						utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el departamento");
						return;
								
					}else if(tab_poa.getValor("IDE_GEESO")!=null){
						
		      		TablaGenerica tabDeptIngresada= utilitario.consultar("	SELECT ide_gedep, ide_geare, gen_ide_gedep, detalle_gedep, tipo_gedep,  "
		      				+ "nivel_gedep, nivel_organico_gedep, posicion_hijos_gedep, orden_gedep,  "
		      				+ "orden_imprime_gedep, activo_gedep, usuario_ingre, fecha_ingre,  "
		      				+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, abreviatura_gedep "
		      				+ "FROM gen_departamento "
		      				+ "WHERE ACTIVO_GEDEP=TRUE "
		      				+ "AND IDE_GEARE="+tab_poa.getValor("IDE_GEARE") 
		      				+ "AND IDE_GEDEP="+tab_poa.getValor("IDE_GEDEP") 
		      				+ "ORDER BY IDE_GEDEP ASC");
				
					if (tabDeptIngresada.getTotalFilas()>0) {
		if (!tabDeptIngresada.getValor("DETALLE_GEDEP").equals(tab_poa.getValor("DETALLE_GEESO"))) {
			//tiene ingresado en las dos tablas tanto estructura organizacional como en la de area
		//utilitario.getConexion().ejecutarSql("update gen_departamento set DETALLE_GEDEP='"+tab_poa.getValor("DETALLE_GEESO")+"' where ide_GEESO=" + tab_poa.getValor("IDE_GEESO"));
			utilitario.agregarMensajeInfo("No se puede guardar no puede cambiar el area", "Debe ingresar el area");
			return;	
		}
			}else {
				insertarDepartamento(Integer.parseInt(tabArea.getValor("IDE_GEARE")),tab_poa.getValor("DETALLE_GEESO"));

				TablaGenerica tabDept= utilitario.consultar("SELECT ide_gedep, ide_geare, gen_ide_gedep, detalle_gedep, tipo_gedep,  "
						+ "nivel_gedep, nivel_organico_gedep, posicion_hijos_gedep, orden_gedep,  "
						+ "orden_imprime_gedep, activo_gedep, usuario_ingre, fecha_ingre,  "
						+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, abreviatura_gedep "
						+ "FROM gen_departamento "
						+ "order by ide_gedep desc "
						+ "limit 1");
			
				if (tabDept.getTotalFilas()>0) {
					tab_poa.guardar();
					tab_poa.getColumna("IDE_GEDEP").setLectura(false);
					tab_poa.setValor("IDE_GEDEP", tabDept.getValor("IDE_GEDEP"));
					tab_poa.setValor("IDE_GEARE", tabDept.getValor("IDE_GEARE"));
					guardarPantalla();
					utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
							+ "set ide_gedep=" +Integer.parseInt(tabDept.getValor("IDE_GEDEP"))  + " "
							+ "where ide_geeso=" + tab_poa.getValorSeleccionado());

					insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));
					//Actualizar el arbol
					arb_arbol.ejecutarSql();
					utilitario.addUpdate("arb_arbol");
				
				}			
			}
	
	
					}else {
						insertarDepartamento(Integer.parseInt(tabArea.getValor("IDE_GEARE")),tab_poa.getValor("DETALLE_GEESO"));
								
					
					
					TablaGenerica tabDept= utilitario.consultar("SELECT ide_gedep, ide_geare, gen_ide_gedep, detalle_gedep, tipo_gedep,  "
							+ "nivel_gedep, nivel_organico_gedep, posicion_hijos_gedep, orden_gedep,  "
							+ "orden_imprime_gedep, activo_gedep, usuario_ingre, fecha_ingre,  "
							+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, abreviatura_gedep "
							+ "FROM gen_departamento "
							+ "order by ide_gedep desc "
							+ "limit 1");
				
					if (tabDept.getTotalFilas()>0) {
						tab_poa.guardar();
						tab_poa.getColumna("IDE_GEDEP").setLectura(false);
						tab_poa.setValor("IDE_GEDEP", tabDept.getValor("IDE_GEDEP"));
						tab_poa.setValor("IDE_GEARE", tabDept.getValor("IDE_GEARE"));
						guardarPantalla();
						utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional "
								+ "set ide_gedep=" +Integer.parseInt(tabDept.getValor("IDE_GEDEP"))  + " "
								+ "where ide_geeso=" + tab_poa.getValorSeleccionado());

						insertarEstructuraOrganizacionalVigente(Integer.parseInt(com_anio.getValue().toString()), true, Integer.parseInt(tab_poa.getValorSeleccionado()));
						//Actualizar el arbol
						arb_arbol.ejecutarSql();
						utilitario.addUpdate("arb_arbol");
					}
				}
			}
		
				
			}else if(tab_departamento_sucursal.isFocus()){
		//tab_grupo_ocupacional.setValor("IDE_GEESO", tab_poa.getValor("IDE_GEESO"));
				tab_departamento_sucursal.guardar();
				guardarPantalla();
	}
			
		}
	

			/*TablaGenerica nivel_funcion=utilitario.consultar("select * from gen_area where ide_geare="+tab_poa.getValor("ide_geare"));
			nivel_funcion.imprimirSql();
			int nivelpadre =0;
			if(tab_poa.getValor("pre_ide_geeso")==null){
				nivelpadre=0;
			}
			else {
				
				if(tab_poa.getValor("pre_ide_geeso").isEmpty()){
					nivelpadre=0;
				}
				else {
		TablaGenerica nivel_funcion_padre=utilitario.consultar("select * from GEN_AREA "
				+ "where ide_geare in (select ide_geare from gen_estructura_organizacional where ide_geeso="+tab_poa.getValor("pre_ide_geeso")+")");
					nivel_funcion_padre.imprimirSql();
					//nivelpadre=pckUtilidades.CConversion.CInt(nivel_funcion_padre.getValor("ide_prnfp")); //poa 2016
					///nivelpadre=pckUtilidades.CConversion.CInt(nivel_funcion_padre.getValor("nivel_prnfp")); //poa 2017
					nivelpadre=pckUtilidades.CConversion.CInt(nivel_funcion_padre.getValor("IDE_nivel_GEARE")); //poa 2017
				}
			}
			int nivel_restado=0;
			//int nivel = pckUtilidades.CConversion.CInt(nivel_funcion.getValor("ide_prnfp"));	//poa 2016
			int nivel = pckUtilidades.CConversion.CInt(nivel_funcion.getValor("ide_nivel_geare"));	//poa 2017
			if (tab_poa.getValor("IDE_GEDEP")==null || tab_poa.getValor("IDE_GEDEP").isEmpty() ||  tab_poa.getValor("IDE_GEDEP").equals("")) {
				nivel_restado=nivel-1;	
			}else {
				if (tab_poa.getValor("IDE_GEARE").equals(nivel_funcion.getValor("IDE_GEARE"))) {
					nivel_restado=nivel;	
				}else {
					utilitario.agregarMensajeError("No se puede Guardar", "Revice el nivel Area para la creación del presente registro");
					return;
				}
			}
			
			
			if((nivel_restado==nivelpadre))
			{	 
				if(tab_poa.isFilaInsertada()){
					   System.out.println("es uevo registro ");
				   }
				if (tab_poa.guardar()) {
					if (tab_vigente.guardar()) {
						//if (tab_grupo_ocupacional.guardar()){
					//	tab_grupo_ocupacional.setValor("IDE_GEESO", tab_poa.getValor("IDE_GEESO"));
						//System.out.println(" entre aguardar ");
						guardarPantalla();
						//Actualizar el arbol
						arb_arbol.ejecutarSql();
						utilitario.addUpdate("arb_arbol");
						//}
					}
				}
		}
		else {
			utilitario.agregarMensajeError("No se puede Guardar", "Revice el nivel jerarquico para la creación del presente registro");
			}
		}
		
		else if (tab_vigente.isFocus()){
				tab_vigente.guardar();
				guardarPantalla();

		}
		else if(tab_grupo_ocupacional.isFocus()){
			//tab_grupo_ocupacional.setValor("IDE_GEESO", tab_poa.getValor("IDE_GEESO"));
			tab_grupo_ocupacional.guardar();
			guardarPantalla();
		}
tab_departamento_sucursal.isFocus()
        */
	
        
        		
	

	public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }
	@Override
	public void eliminar() {
		/*utilitario.getTablaisFocus().eliminar();
			tab_poa.guardar();
			tab_vigente.guardar();
			guardarPantalla();
*/
		
		
		
		}
		

		
		
		/*
		else if (tab_mes_codificado.isFocus()){
			tab_mes_codificado.eliminar();
			tab_mes_codificado.sumarColumnas();
			utilitario.addUpdate("tab_mes_codificado");


		}
		else if (tab_reforma.isFocus()){
			tab_reforma.eliminar();
			calcularReforma();

		}
		else if (tab_financiamiento.isFocus()){
			tab_financiamiento.eliminar();
		}
		else if (tab_archivo.isFocus()){
			tab_archivo.eliminar();
		}
		else if(tab_financiamiento_reforma.isFocus()){
			tab_financiamiento_reforma.eliminar();
		}*/
	
	

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}
	public SeleccionTabla getSet_sub_actividad() {
		return set_sub_actividad;
	}
	public void setSet_sub_actividad(SeleccionTabla set_sub_actividad) {
		this.set_sub_actividad = set_sub_actividad;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}
	public Map getMap_parametros() {
		return map_parametros;
	}
	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}
	public SeleccionTabla getSel_poa() {
		return sel_poa;
	}
	public void setSel_poa(SeleccionTabla sel_poa) {
		this.sel_poa = sel_poa;
	}
	public SeleccionTabla getSel_resolucion() {
		return sel_resolucion;
	}
	public void setSel_resolucion(SeleccionTabla sel_resolucion) {
		this.sel_resolucion = sel_resolucion;
	}

	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}
	public SeleccionTabla getSet_saldo_fuente() {
		return set_saldo_fuente;
	}
	public void setSet_saldo_fuente(SeleccionTabla set_saldo_fuente) {
		this.set_saldo_fuente = set_saldo_fuente;
	}
	public SeleccionTabla getSet_clasificador_actua() {
		return set_clasificador_actua;
	}
	public void setSet_clasificador_actua(SeleccionTabla set_clasificador_actua) {
		this.set_clasificador_actua = set_clasificador_actua;
	}

	
	public void seleccionoClasificador(NodeSelectEvent evt){
	tab_poa.limpiar();	
	tab_departamento_sucursal.limpiar();
	tab_vigente.limpiar();
	//Asigna evento al arbol
	arb_arbol.seleccionarNodo(evt);
	//Filtra la tabla Padre
	tab_poa.ejecutarValorPadre(arb_arbol.getValorSeleccionado());
	
	//Filtra la tabla tab_vigente
	tab_vigente.ejecutarValorForanea(tab_poa.getValorSeleccionado());
	tab_grupo_ocupacional.ejecutarValorForanea(tab_poa.getValorSeleccionado());
	filtrarDepartamentoSucursal();
	System.out.println("ARBOL NIVEL"+arb_arbol.getValorSeleccionado());
	/*TablaGenerica tabDept=null;
	if (tab_poa.getValor("IDE_GEDEP")== null ||  tab_poa.getValor("IDE_GEDEP").isEmpty() || tab_poa.getValor("IDE_GEDEP").equals("")) {
		tabDept = utilitario.consultar("SELECT * FROM GEN_ESTRUCTURA_ORGANIZACIONAL WHERE ide_geeso="+tab_poa.getValor("ide_geeso"));
		if (tabDept.getValor("IDE_GEDEP")== null ||  tabDept.getValor("IDE_GEDEP").isEmpty() || tabDept.getValor("IDE_GEDEP").equals("")) {
	}else {
	//tabDept = utilitario.consultar("SELECT * FROM GEN_ESTRUCTURA_ORGANIZACIONAL WHERE IDE_GENEO="+tab_poa.getValor("IDE_GEDEP"));
	tab_poa.getColumna("IDE_GEDEP").setCombo("select IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE "
			+ " IDE_GEDEP in("+tabDept.getValor("IDE_GEDEP")+")" );
		utilitario.addUpdateTabla(tab_poa, "IDE_GEDEP","");
	   }*/		
		
	
	//actualizarNivel();
  }
	public Tabla getTab_vigente() {
		return tab_vigente;
	}
	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}


	public void obtenerAreaDepartamento(AjaxBehaviorEvent evt){
		tab_poa.modificar(evt);
		utilitario.getVariable("p_gerencia_estructura_organizacional");
		utilitario.getVariable("p_gerencia_general_estructura_general");
		
		TablaGenerica tabArea=utilitario.consultar("Select ide_geare,detalle_geare,ide_nivel_geare "
				+ "from gen_area  "
				+ "where ide_geare in("+tab_poa.getValor("IDE_GEARE")+")");
		
//		if (tabArea.getValor("ide_nivel_geare").equals("2") || tabArea.getValor("ide_nivel_geare").equals("4")) {
/*			tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_AREA b "
					+ " where b.ide_geare not in("+utilitario.getVariable("p_gerencia_estructura_organizacional")+","+utilitario.getVariable("p_gerencia_general_estructura_general")+")	"
					+"ORDER BY b.DETALLE_GEARE");
*/			
//			tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
//					+ "WHERE b.IDE_GEDEP=-1 "
//					+ "ORDER BY b.DETALLE_GEDEP" );
			
			
			//tab_poa.setValor("DESCRIPCION_GEESO",getDescripcionArea(tab_poa.getValor("IDE_GEARE")));
			//utilitario.agregarMensajeInfo("NO SE HA SELECCIONADO UNA AREA", "POR FAVOR REVISAR LA PARAMETRIZACIÓN DE AREAS");
			getDescripcionArea(tab_poa.getValor("IDE_GEARE"));
			//tab_poa.setValor("DESCRIPCION_GEESO",getDescripcionArea(tab_poa.getValor("IDE_GEARE")));
			
		//	utilitario.addUpdateTabla(tab_poa, "IDE_GEARE","");
		//	utilitario.addUpdateTabla(tab_poa, "IDE_GEDEP","");
	

			
			
		//}else {
			/*tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
					+ " ORDER BY b.DETALLE_GEDEP" );
			
			
			tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
					+ " ORDER BY b.DETALLE_GEDEP" );
		*/
			tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
					+ "ORDER BY b.DETALLE_GEDEP" );
			//getDescripcionDepartamento(tab_poa.getValor("IDE_GEDEP"));
		//	tab_poa.setValor("DESCRIPCION_GEESO",getDescripcionDepartamento(tab_poa.getColumna("IDE_GEDEP").toString()));
			
		//	utilitario.addUpdateTabla(tab_poa, "IDE_GEARE","");
			utilitario.addUpdateTabla(tab_poa, "IDE_GEDEP","");
	
	//	}
	/*	if (tabArea.getValor("ide_nivel_geare").equals("4")) {
			tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_AREA b "
					+ "where ide_geare=-1 " +
					"ORDER BY b.DETALLE_GEARE");

tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
		+ "ORDER BY b.DETALLE_GEDEP" );

//tab_poa.setValor("DESCRIPCION_GEESO",getDescripcionDepartamento(tab_poa.getValor("IDE_GEDEP")));

	//utilitario.addUpdateTabla(tab_poa, "IDE_GEARE","");
	//utilitario.addUpdateTabla(tab_poa, "IDE_GEDEP","");
			}
		
			
		if (tabArea.getValor("ide_nivel_geare").equals("1")) {
		/*	tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_AREA b "
					+ " where b.ide_geare in("+utilitario.getVariable("p_gerencia_general_estructura_general")+")	"
					+"ORDER BY b.DETALLE_GEARE");
	**/
			
		/*	tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEDEP=-1 "
					+ "ORDER BY b.DETALLE_GEDEP" );
			
		}
		
		
		else if (tabArea.getValor("ide_nivel_geare").equals("2")) {
			/*tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_AREA b "
					+ " where b.ide_geare in("+utilitario.getVariable("p_gerencia_estructura_organizacional")+")	"
					+"ORDER BY b.DETALLE_GEARE");*/
	

			
		/*	tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEDEP=-1 "
					+ "ORDER BY b.DETALLE_GEDEP" );
		}else {
			tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
					+ " ORDER BY b.DETALLE_GEDEP" );
			
		}
		tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT b.IDE_GEDEP,b.DETALLE_GEDEP FROM GEN_DEPARTAMENTO b "
					+ "WHERE b.IDE_GEARE="+tab_poa.getValor("IDE_GEARE")
					+ " ORDER BY b.DETALLE_GEDEP" );
			
		utilitario.addUpdateTabla(tab_poa, "IDE_GEDEP","");
*/
		
	}

	
	public void getCargoFuncional(AjaxBehaviorEvent evt){
		String cargoFuncional="";	
		
		TablaGenerica tabCargoFuncional=utilitario.consultar("select ggc.ide_gecaf,gcf.detalle_gecaf "
				+ "from GEN_GRUPO_CARGO   ggc "
				+ "left join gen_cargo_funcional gcf on gcf.ide_gecaf=ggc.ide_gecaf "
				+ "where ggc.ide_gegro in("+tab_grupo_ocupacional.getValor("IDE_GEGRO")+") and ggc.activo_gegrc=true and gcf.activo_gecaf=true "
				+ "and  gcf.detalle_gecaf is not null "
				+ "order by gcf.detalle_gecaf asc");
		
		if (tabCargoFuncional.getTotalFilas()>0) {

			tab_grupo_ocupacional.getColumna("IDE_GECAF").setCombo("select ggc.ide_gecaf,gcf.detalle_gecaf "
				+ "from GEN_GRUPO_CARGO   ggc "
				+ "left join gen_cargo_funcional gcf on gcf.ide_gecaf=ggc.ide_gecaf "
				+ "where ggc.ide_gegro in("+tab_grupo_ocupacional.getValor("IDE_GEGRO")+") and ggc.activo_gegrc=true and gcf.activo_gecaf=true "
				+ "and  gcf.detalle_gecaf is not null "
				+ "order by gcf.detalle_gecaf asc" );
			utilitario.addUpdateTabla(tab_grupo_ocupacional, "IDE_GECAF","");
			
		   }else {
		
			   utilitario.agregarMensajeInfo("No contiene nivel asignado", "Error en parametrizacion de Niveles");
			     return;
		   }
				
		
	}
	
	public void getDescripcionDepartamento(AjaxBehaviorEvent evt){
		String departamento="";
	
		if (tab_poa.getValor("IDE_GEDEP")==null || tab_poa.getValor("IDE_GEDEP").isEmpty() || tab_poa.getValor("IDE_GEDEP").equals("")) {
			
		}else {
			
		
		TablaGenerica tabDepartamento=utilitario.consultar("select AREA.IDE_GEDEP,AREA.DETALLE_GEDEP  "
				+ "from GEN_DEPARTAMENTO AREA "
				//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
				+ "WHERE AREA.IDE_GEDEP IN("+tab_poa.getValor("IDE_GEDEP")+") "
				+ "ORDER BY AREA.IDE_GEDEP ASC");
		

		
		
		departamento=tabDepartamento.getValor("DETALLE_GEDEP");
		tab_poa.getColumna("DETALLE_GEESO").setLectura(false);
		tab_poa.setValor("detalle_geeso",departamento);
		utilitario.addUpdateTabla(tab_poa, "DETALLE_GEESO",departamento);
		tab_poa.getColumna("DETALLE_GEESO").setLectura(true);
		utilitario.addUpdate("tab_poa");
		}
		//return tabDepartamento.getValor("DETALLE_GEDEP");
	}
	
	
public void getDescripcionArea(String ide_geare){
	String area="";	
	
	TablaGenerica tabArea=utilitario.consultar("select AREA.IDE_GEARE,AREA.DETALLE_GEARE,AREA.IDE_NIVEL_GEARE  "
			+ "from GEN_AREA AREA "
			//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
			+ "WHERE AREA.IDE_GEARE IN("+ide_geare+") "
			+ "ORDER BY AREA.IDE_GEARE ASC");
	area=tabArea.getValor("DETALLE_GEARE");
	/*if (tabArea.getValor("IDE_GEARE")==null || tabArea.getValor("IDE_GEARE").equals("") || tabArea.getValor("IDE_GEARE").isEmpty()) {
		area="";
	}else {
		if (tabArea.getValor("ide_nivel_geare")==null || tabArea.getValor("ide_nivel_geare").equals("") || tabArea.getValor("ide_nivel_geare").isEmpty()) {
	     utilitario.agregarMensajeInfo("No contiene nivel asignado", "Error en parametrizacion de Niveles");
	     return;
		}else {
			nivel=tabArea.getValor("ide_nivel_geare");		
		
		area=tabArea.getValor("detalle_geare");
		
		
		if (nivel.equals("6")) {
	bandNivel=true;
			tab_poa.getColumna("ide_estructura_organizacional").setCombo("select IDE_GEDEP,DEPT.DETALLE_GEDEP "
						+ "from gen_departamento DEPT "
						//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
						//+ "WHERE DEPT.IDE_GEDEP IN("+tab_poa.getValor("NIVEL_GEESO")+") "
						+ "ORDER BY DEPT.IDE_GEDEP ASC");
		}else {
			bandNivel=false;
			
			tab_poa.getColumna("ide_estructura_organizacional").setCombo("select AREA.IDE_GEARE,AREA.DETALLE_GEARE,AREA.IDE_NIVEL_GEARE  "
					+ "from GEN_AREA AREA "
					//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
					+ "WHERE AREA.IDE_GEARE IN("+tab_poa.getValor("NIVEL_GEESO")+") "
					+ "ORDER BY AREA.IDE_GEARE ASC");
			
			   /*tab_poa.getColumna("ide_estructura_organizacional").setCombo("select AREA.IDE_GEARE,AREA.DETALLE_GEARE,DEPT.IDE_GEDEP,DEPT.DETALLE_GEDEP,AREA.IDE_NIVEL_GEARE  "
						+ "from gen_departamento DEPT "
						+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
						+ "WHERE AREA.IDE_GEARE IN("+tab_poa.getValor("NIVEL_GEESO")+") "
						+ "ORDER BY AREA.IDE_GEARE ASC");
			*/
		//}
		//}
	 
	
	//}
	tab_poa.getColumna("DETALLE_GEESO").setLectura(false);
	tab_poa.setValor("detalle_geeso",area);
	utilitario.addUpdateTabla(tab_poa, "DETALLE_GEESO",area);
	tab_poa.getColumna("DETALLE_GEESO").setLectura(true);
	utilitario.addUpdate("tab_poa");
	//tab_poa.getColumna("DETALLE_GEESO").setLectura(true);
//return tabArea.getValor("DETALLE_GEARE");
}
	
	

public void getDescripcionEstructuraOrganizacional(AjaxBehaviorEvent evt){
	String departamento="";
// TablaGenerica tab_Dept=null;
	
	if (nivel.equals("6")) {
	TablaGenerica tabDept=utilitario.consultar("select DEPT.IDE_GEDEP,DEPT.DETALLE_GEDEP "
					+ "from gen_departamento DEPT "
					//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
					+ "WHERE DEPT.IDE_GEDEP IN("+tab_poa.getValor("ide_estructura_organizacional")+") "
					+ "ORDER BY DEPT.IDE_GEDEP ASC");
	departamento=tabDept.getValor("DETALLE_GEDEP");
	}else {
		TablaGenerica tabArea=utilitario.consultar("select AREA.IDE_GEARE,AREA.DETALLE_GEARE,AREA.IDE_NIVEL_GEARE  "
				+ "from GEN_AREA AREA "
				//+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
				+ "WHERE AREA.IDE_GEARE IN("+tab_poa.getValor("NIVEL_GEESO")+") "
				+ "ORDER BY AREA.IDE_GEARE ASC");
		departamento=tabArea.getValor("DETALLE_GEARE");
		   /*tab_poa.getColumna("ide_estructura_organizacional").setCombo("select AREA.IDE_GEARE,AREA.DETALLE_GEARE,DEPT.IDE_GEDEP,DEPT.DETALLE_GEDEP,AREA.IDE_NIVEL_GEARE  "
					+ "from gen_departamento DEPT "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DEPT.IDE_GEARE "
					+ "WHERE AREA.IDE_GEARE IN("+tab_poa.getValor("NIVEL_GEESO")+") "
					+ "ORDER BY AREA.IDE_GEARE ASC");
		*/
	}


	
tab_poa.getColumna("DETALLE_GEESO").setLectura(false);
tab_poa.setValor("detalle_geeso",departamento);
utilitario.addUpdateTabla(tab_poa, "DETALLE_GEESO",departamento);
tab_poa.getColumna("DETALLE_GEESO").setLectura(true);


}
public String getNivel() {
	return nivel;
}
public void setNivel(String nivel) {
	this.nivel = nivel;
}
public Boolean getBandNivel() {
	return bandNivel;
}
public void setBandNivel(Boolean bandNivel) {
	this.bandNivel = bandNivel;
}



public void seleccionarTabla2(SelectEvent evt){
	tab_cargo_funcional.seleccionarFila(evt);
}
/**
 * Se ejecuta cuando se selecciona una fila de la tabla1 de Grupo
 * Ocupacional, actualiza grupo cargos
 * 
 * @param evt
 */

public void seleccionarTabla1(SelectEvent evt) {
	tab_grupo_ocupacional.seleccionarFila(evt);
	filtrarGrupoCargos();
	tab_cargo_funcional.ejecutarSql();
}

/**
 * Filtra a los grupos ocupacionales y cargos funcionales seleccionados
 */

public void filtrarGrupoCargos() {
	String str_grupo = tab_grupo_ocupacional.getValor("IDE_GEGRO");
	if (str_grupo == null || str_grupo.isEmpty()) {
		str_grupo = "-1";
	}
	tab_cargo_funcional.setCondicion("IDE_GEGRO =" + str_grupo);
}
public Tabla getTab_poa() {
	return tab_poa;
}
public void setTab_poa(Tabla tab_poa) {
	this.tab_poa = tab_poa;
}
public Tabla getTab_grupo_ocupacional() {
	return tab_grupo_ocupacional;
}
public void setTab_grupo_ocupacional(Tabla tab_grupo_ocupacional) {
	this.tab_grupo_ocupacional = tab_grupo_ocupacional;
}
	

public void filtrarDepartamentoSucursal(){
	//tab_poa.getValor("IDE_GEEDP");
	if (tab_poa.getValor("IDE_GEDEP")==null || tab_poa.getValor("IDE_GEDEP").equals("") || tab_poa.getValor("IDE_GEDEP").isEmpty()) {
		tab_departamento_sucursal.setCondicion("IDE_GEARE=-1 and IDE_GEDEP=-1");
		tab_departamento_sucursal.ejecutarSql();		
		
	}else {
		tab_departamento_sucursal.setCondicion("IDE_GEARE="+tab_poa.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_poa.getValor("IDE_GEDEP"));
		tab_departamento_sucursal.ejecutarSql();		
	}

}
public Tabla getTab_departamento_sucursal() {
	return  tab_departamento_sucursal;
}
public void setTab_departamento_sucursal(Tabla tab_departamento_sucursal) {
	this.tab_departamento_sucursal = tab_departamento_sucursal;
}

public void importarEstructura(String anio){

	//dia_area.getGri_cuerpo().
	//Ingreso de Areas 
	
	TablaGenerica tabArea = utilitario.consultar("SELECT ide_geare, detalle_geare, codigo_core_geare, activo_geare, "
			+ "ide_padre_geare, siglas_geare, ide_nivel_geare "
			+ "FROM gen_area  "
			+ "where activo_geare=true "
			+ "order by ide_nivel_geare");
		
 	
for (int i = 0; i < tabArea.getTotalFilas(); i++) {	
	if (tabArea.getValor(i, "ide_nivel_geare").equals("0")) {
	}else if (tabArea.getValor(i, "ide_nivel_geare").equals("1")) {
		insertarEstructuraOrganizacionalArea(0, 
				tabArea.getValor(i, "DETALLE_GEARE"), true, 
				Integer.parseInt(tabArea.getValor(i, "ide_geare")));
		
	}else if (tabArea.getValor(i, "ide_nivel_geare").equals("2")){
		
		if (tabArea.getValor(i, "ide_padre_geare")==null || tabArea.getValor(i, "ide_padre_geare").equals("") || tabArea.getValor(i, "ide_padre_geare").isEmpty()) {
			
		}else {
			
			TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
					+ "ide_geare,ide_gedep "
					+ "FROM gen_estructura_organizacional"
					+ " WHERE IDE_GEARE="+tabArea.getValor(i, "ide_padre_geare")
					+ " and IDE_GEDEP is null "
					+ " order by ide_geare asc");
			
		
			insertarEstructuraOrganizacionalArea(Integer.parseInt(tabEstructura.getValor("IDE_GEESO")), 
					tabArea.getValor(i, "DETALLE_GEARE"), true, 
					Integer.parseInt(tabArea.getValor(i, "ide_geare")));

			
		}
				
		}else if (tabArea.getValor(i, "ide_nivel_geare").equals("3")){

			TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
					+ "ide_geare,ide_gedep "
					+ "FROM gen_estructura_organizacional "
					+ "WHERE IDE_GEARE="+tabArea.getValor(i, "ide_padre_geare")
					+ " and IDE_GEDEP is null "
					+ " order by ide_geare asc");
			insertarEstructuraOrganizacionalArea(Integer.parseInt(tabEstructura.getValor("IDE_GEESO")), 
				tabArea.getValor(i, "DETALLE_GEARE"), true, 
				Integer.parseInt(tabArea.getValor(i, "ide_geare")));
		
	}else if (tabArea.getValor(i, "ide_nivel_geare")==null || tabArea.getValor(i, "ide_nivel_geare").equals("") || tabArea.getValor(i, "ide_nivel_geare").isEmpty()){
		
	}


}



TablaGenerica tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
		+ "ide_geare,ide_gedep "
		+ "FROM gen_estructura_organizacional "
		+ "order by ide_geeso asc");



for (int i = 0; i < tabEstructura.getTotalFilas() ; i++) {
	
TablaGenerica tabDepartamento = utilitario.consultar("SELECT ide_gedep, ide_geare, gen_ide_gedep, detalle_gedep, tipo_gedep, "
		+ "nivel_gedep, nivel_organico_gedep, posicion_hijos_gedep, orden_gedep,  "
		+ "orden_imprime_gedep, activo_gedep, abreviatura_gedep "
		+ "FROM gen_departamento "
		+ "where ide_geare="+tabEstructura.getValor(i,"IDE_GEARE")
		+ " order by ide_gedep asc");
	

	if (tabDepartamento.getTotalFilas()<=0) {
		
	}else {
		for (int j = 0; j < tabDepartamento.getTotalFilas(); j++) {
			

			insertarEstructuraOrganizacionalDepartamento(Integer.parseInt(tabEstructura.getValor(i,"IDE_GEESO")), 
					tabDepartamento.getValor(j, "DETALLE_GEDEP"), true, 
					Integer.parseInt(tabDepartamento.getValor(j, "ide_geare")), Integer.parseInt(tabDepartamento.getValor(j, "ide_gedep")));
			}		
	}

	
}

TablaGenerica tab_anio= utilitario.consultar("select ide_geani,detalle_geani, " +
		" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani," +
		" CASE WHEN bloqueado_geani = true THEN 'Bloqueado' ELSE 'Habilitado' END AS bloqueado_geani" +
		" from gen_anio where activo_geani in(true)" +
		" and bloqueado_geani in (false)" +
		" order by detalle_geani desc") ;


TablaGenerica tabEstructuraOrganizacional= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
		+ "ide_geare,ide_gedep "
		+ "FROM gen_estructura_organizacional "
		+ "order by ide_geeso asc");

for (int i = 0; i < tabEstructuraOrganizacional.getTotalFilas(); i++) {
	insertarEstructuraOrganizacionalVigente(Integer.parseInt(anio), true,Integer.parseInt(tabEstructuraOrganizacional.getValor(i,"ide_geeso")));
	
}


TablaGenerica tabEstructuraOrganizacionalSucuAreaDept= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
		+ "ide_geare,ide_gedep "
		+ "FROM gen_estructura_organizacional "
		+ "where ide_gedep is not null "
		+ "order by ide_geeso asc");


for (int i = 0; i < tabEstructuraOrganizacionalSucuAreaDept.getTotalFilas(); i++) {
			TablaGenerica taGenPartidaGrupoCargo= utilitario.consultar("SELECT ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, ide_gedep, ide_geare, "
				+ "ide_gepap, ide_gttem, titulo_cargo_gepgc, salario_encargo_gepgc,  "
				+ "fecha_activacion_gepgc, fecha_desactiva_gepgc, motivo_gepgc,  "
				+ "activo_gepgc,vacante_gepgc,encargo_gepgc, ide_geeso "
				+ "FROM gen_partida_grupo_cargo "
				+ " where ide_geare="+tabEstructuraOrganizacionalSucuAreaDept.getValor(i, "IDE_GEARE")+"  and ide_gedep="+tabEstructuraOrganizacionalSucuAreaDept.getValor(i, "IDE_GEDEP"));
		for (int j = 0; j < taGenPartidaGrupoCargo.getTotalFilas(); j++) {		
		if (tabEstructuraOrganizacionalSucuAreaDept.getValor(i, "IDE_GEARE").equals(taGenPartidaGrupoCargo.getValor(j, "IDE_GEARE"))){
			if (tabEstructuraOrganizacionalSucuAreaDept.getValor(i, "IDE_GEDEP").equals(taGenPartidaGrupoCargo.getValor(j, "IDE_GEDEP"))){
				utilitario.getConexion().ejecutarSql("update gen_partida_grupo_cargo "
				+ "set ide_geeso=" +Integer.parseInt(tabEstructuraOrganizacionalSucuAreaDept.getValor(i, "IDE_GEESO"))  + " "
				+ "where ide_gepgc=" + taGenPartidaGrupoCargo.getValor(j, "ide_gepgc"));
			}
		}
	}
	
}



TablaGenerica tabSucuAreaDept= utilitario.consultar("SELECT ide_sucu, ide_gedep, ide_geare, activo_gedes,  "
+ "ide_geeso "
+ "FROM  "
+ "gen_departamento_sucursal "
+ "where ide_gedep is not null "
+ "and activo_gedes=true");


TablaGenerica taGenPartidaGrupoCargo= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, "
		+ "detalle_geeso, activo_geeso, "
		+ "ide_geare, ide_gedep "
		+ "FROM  "
		+ "gen_estructura_organizacional "
		+ "where ide_gedep is not null");




for (int i = 0; i < tabSucuAreaDept.getTotalFilas(); i++) {
for (int j = 0; j < taGenPartidaGrupoCargo.getTotalFilas(); j++) {

	if (tabSucuAreaDept.getValor(i, "IDE_GEARE").equals(taGenPartidaGrupoCargo.getValor(j, "IDE_GEARE"))){
		if (tabSucuAreaDept.getValor(i, "IDE_GEDEP").equals(taGenPartidaGrupoCargo.getValor(j, "IDE_GEDEP"))){
			utilitario.getConexion().ejecutarSql("update gen_departamento_sucursal "
					+ "set ide_geeso=" +Integer.parseInt(taGenPartidaGrupoCargo.getValor(j, "IDE_GEESO"))  + " "
					+ "where ide_sucu=" + tabSucuAreaDept.getValor(i, "ide_sucu"));
		}
		
	}
}	
	
}



}





public void insertarEstructuraOrganizacionalArea(
		int pre_ide_geeso,
		String detalle_geeso, 
		boolean activo_geeso,
		int ide_geare
		){
	 String padre="";

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_estructura_organizacional", "ide_geeso"));
		String codigo=tab_codigo.getValor("codigo");

	 if (pre_ide_geeso==0) {
		
			utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
					+ "ide_geeso, "
					+ "pre_ide_geeso, "
			  		+ "detalle_geeso, "
			  		+ "activo_geeso,"
			  		+ "ide_geare )" + 
			  		" values( " +codigo + ", "
			  	    + "null, "
			  		+ "'"+detalle_geeso+"', "
			  		+ ""+activo_geeso+", "
			  		+ ""+ide_geare+")"); 
		
			
		}else {
			utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
					+ "ide_geeso, "
					+ "pre_ide_geeso, "
			  		+ "detalle_geeso, "
			  		+ "activo_geeso,"
			  		+ "ide_geare )" + 
			  		" values( " +codigo + ", "
			  	    + ""+pre_ide_geeso+", "
			  		+ "'"+detalle_geeso+"', "
			  		+ ""+activo_geeso+", "
			  		+ ""+ide_geare+")"); 

		}
		 
}


public void insertarEstructuraOrganizacionalDepartamento(
		int pre_ide_geeso,
		String detalle_geeso, 
		boolean activo_geeso,
		int ide_geare,
		int ide_gedep
		){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_estructura_organizacional", "ide_geeso"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
					+ "ide_geeso, "
					+ "pre_ide_geeso, "
			  		+ "detalle_geeso, "
			  		+ "activo_geeso,"
			  		+ "ide_geare,"
			  		+ "ide_gedep )" + 
			  		" values( " +codigo + ", "
			  		+ ""+pre_ide_geeso+", "
			  		+ "'"+detalle_geeso+"', "
			  		+ ""+activo_geeso+", "
			  		+ ""+ide_geare+", "
			  		+ ""+ide_gedep+")"); 
	 
}



public void insertarEstructuraOrganizacionalVigente(
		int ide_geani, 
		boolean activo_geveo, 
	    int   ide_geeso
		){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_vigente_estructura_organizacional", "ide_geeso"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO gen_vigente_estructura_organizacional(" 
					+ "ide_geveo, "
					+ "ide_geani, "
			  		+ "bloqueado_geveo, "
			  		+ "activo_geveo,"
			  		+ "ide_geeso )" + 
			  		" values( " +codigo + ", "
			  		+ ""+ide_geani+", "
			  		+ "false, "
			  		+ ""+activo_geveo+", "
			  		+ ""+ide_geeso+")"); 
	 
}



public void insertarArea(
		String detalle_geare,
		int ide_padre_geare,
		int nivel_geare
		){
	 String padre="";

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_area", "ide_geare"));
		String codigo=tab_codigo.getValor("codigo");

			utilitario.getConexion().ejecutarSql("INSERT INTO gen_area(" 
					+ "ide_geare, "
					+ "detalle_geare, "
			  		+ "activo_geare, "
			  		+ "ide_padre_geare,"
			  		+ "ide_nivel_geare )" + 
			  		" values( " +codigo + ", "
			  	   + "'"+detalle_geare+"', "
			  	   + "true, "
			  	  	+ ""+ide_padre_geare+", "
			  		+ ""+nivel_geare+")"); 
			
	 
}



public void insertarDepartamento(
		int ide_geare, 
		String detalle_gedep
		){
	 String padre="";

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_departamento", "ide_gedep"));
		String codigo=tab_codigo.getValor("codigo");

			utilitario.getConexion().ejecutarSql("INSERT INTO gen_departamento(" 
					+ "ide_gedep, "
					+ "ide_geare, "
			  		+ "detalle_gedep, "
			  		+ "activo_gedep )" + 
			  		" values( " +codigo + ", "
			  	   + ""+ide_geare+", "
			  	   + "'"+detalle_gedep+"', "
			  	   + "true)"); 
			
}




public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}








/*
public void inicializaSetPoaReformar(){
	set_area.setId("set_area");
	set_area.setTitle("INGRESE LOS CAMPOS REQUERIDOS ");
	
	set_area.setSeleccionTabla(ser_presupuesto.getPoaSaldosFuenteFinanciamiento("-1","-1","0","1"),"codigo");
	set_area.getTab_seleccion().getColumna("detalle_prfuf").setNombreVisual("Fuente Financiamiento");
	set_area.getTab_seleccion().getColumna("valor_asignado").setNombreVisual("Asignación Inicial F.F.");
	set_area.getTab_seleccion().getColumna("codigo_clasificador_prcla").setNombreVisual("Partida Presupuestaria");
	set_area.getTab_seleccion().getColumna("codigo_subactividad").setNombreVisual("Codigo Sub-Actividad");
	set_area.getTab_seleccion().getColumna("detalle_subactividad").setNombreVisual("Sub-Actividad");
	set_area.getTab_seleccion().getColumna("num_resolucion_prpoa").setNombreVisual("Nro. Resolución");
	set_area.getTab_seleccion().getColumna("detalle_proyecto").setNombreVisual("PROYECTO");

	set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setFiltro(true);
	set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setFiltro(true);

	set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setVisible(false);
	set_reforma_fuente.getTab_seleccion().getColumna("ide_prfuf").setVisible(false);
	//set_reforma_fuente.setRadio();
	set_reforma_fuente.getTab_seleccion().ejecutarSql();
	set_reforma_fuente.getBot_aceptar().setMetodo("aceptarPoa");

	agregarComponente(set_reforma_fuente);

}*/




public void insertarArea(){
	dia_area_departamento.cerrar();
	utilitario.addUpdate("dia_area_departamento");
	
	if (chk_area_departamento.getValue().toString().equals("") || chk_area_departamento.getValue().toString()==null || chk_area_departamento.getValue().toString().equals("false") || tab_poa.getColumna("IDE_GEDEP").toString().isEmpty()) {

		area=false;
		TablaGenerica tabArea=null;
		TablaGenerica tabEstructura=null;
		
		tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
				+ "ide_geare, ide_gedep "
				+ "FROM gen_estructura_organizacional  "
				+ "Where ide_geeso="+arb_arbol.getValorSeleccionado()+" and ide_gedep is null");
		
		
		if (tabEstructura.getTotalFilas()<=0) {
			utilitario.agregarMensajeInfo("Revise el nivel jerarquico", "No se puede ingresar");
			return;
		
		}else {
			tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
					+ "FROM "
					+ "GEN_AREA "
					+ "WHERE ACTIVO_GEARE=TRUE "
					+ "AND IDE_GEARE="+tabEstructura.getValor("IDE_GEARE")
					+ "ORDER BY IDE_GEARE ASC");
						
		} 
		tab_poa.getColumna("IDE_GEARE").setLectura(false);
		tab_poa.getColumna("IDE_GEDEP").setLectura(true);
		tab_poa.getColumna("DETALLE_GEESO").setLectura(false);
		
		tab_poa.insertar();
		tab_poa.setValor("IDE_GEARE",tabEstructura.getValor("IDE_GEARE") );
		tab_poa.getColumna("IDE_GEARE").setLectura(true);
		

	}else {
		area=true;

		TablaGenerica tabArea=null;
		TablaGenerica tabEstructura=null;
		
		if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().toString().equals("")  || arb_arbol.getValorSeleccionado().toString().isEmpty()) {
			 tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
					+ "FROM "
					+ "GEN_AREA "
					+ "WHERE ACTIVO_GEARE=TRUE "
					+ "AND IDE_NIVEL_GEARE=0 "
					+ "ORDER BY IDE_GEARE ASC");
		
		}else {
			
			tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
								+ "ide_geare, ide_gedep "
								+ "FROM gen_estructura_organizacional  "
								+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
						
			tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE "
						+ "FROM "
						+ "GEN_AREA "
						+ "WHERE ACTIVO_GEARE=TRUE "
						+ "AND IDE_GEARE="+tabEstructura.getValor("IDE_GEARE")
						+ "ORDER BY IDE_GEARE ASC");

		}
		
			
		

		String parametro="12,13,14,15,27";
		String[] array = parametro.split(",");
		boolean banderaArea=true;
		if (array.length > 0) {
			for (int j = 0; j < array.length; j++) {
				   if (array[j].equals(tabArea.getValor("IDE_GEARE"))) {
							banderaArea=true;
							j=array.length;
						
				   }else {
							banderaArea=false;
						}		
	}

			
			if (banderaArea==false) {
				utilitario.agregarMensajeInfo("Revise el nivel jerarquico", "No se puede ingresar");
				return;
			}else {
				
			}
		}
		tab_poa.getColumna("IDE_GEDEP").setLectura(true);
		tab_poa.getColumna("IDE_GEARE").setLectura(true);
		tab_poa.getColumna("DETALLE_GEESO").setLectura(false);
		tab_poa.insertar();

	}
	

}


public void insertarAreaAceptar(){
tab_area.guardar();
guardarPantalla();
}

public Tabla getTab_area() {
	return tab_area;
}
public void setTab_area(Tabla tab_area) {
	this.tab_area = tab_area;
}

public Dialogo getDia_area_departamento() {
	return dia_area_departamento;
}
public void setDia_area_departamento(Dialogo dia_area_departamento) {
	this.dia_area_departamento = dia_area_departamento;
}
public Dialogo getDia_area() {
	return dia_area;
}
public void setDia_area(Dialogo dia_area) {
	this.dia_area = dia_area;
}
public Confirmar getCon_aprobar_area() {
	return con_aprobar_area;
}
public void setCon_aprobar_area(Confirmar con_aprobar_area) {
	this.con_aprobar_area = con_aprobar_area;
}
public boolean isArea() {
	return area;
}
public void setArea(boolean area) {
	this.area = area;
}


public void seleccionaElAnio(){
	if(com_anio.getValue()!=null){
		//arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where ide_geani="+com_anio.getValue()+")");		
		//arb_arbol.ejecutarSql();		
		
		TablaGenerica tabAnioValidar= utilitario.consultar("select ide_geani,detalle_geani,activo_geani " +
				" from gen_anio where activo_geani in(true,false)" +
				" and bloqueado_geani in (true,false) "
				+ "and ide_geani="+com_anio.getValue().toString()+
				" order by detalle_geani desc") ;

if (tabAnioValidar.getTotalFilas()>0) {
	
	if (tabAnioValidar.getValor("ACTIVO_GEANI").equals("false")) {
		TablaGenerica tabEstrucuturaVigente= utilitario.consultar("SELECT ide_geveo, ide_geani, bloqueado_geveo, activo_geveo, "
				+ "ide_geeso "
				+ "FROM  "
				+ "gen_vigente_estructura_organizacional "
				+ "WHERE IDE_GEANI="+com_anio.getValue().toString()) ;
		
	if (tabEstrucuturaVigente.getTotalFilas()>0) {
		
	}else {
		utilitario.agregarMensajeInfo("No contiene datos para el año seleccionado", "No se han registrados datos en la base de datos");
		return;
	}	
		
			
	}else if (tabAnioValidar.getValor("ACTIVO_GEANI").equals("true")){
		
		TablaGenerica tabEstrucuturaVigente= utilitario.consultar("SELECT ide_geveo, ide_geani, bloqueado_geveo, activo_geveo, "
				+ "ide_geeso "
				+ "FROM  "
				+ "gen_vigente_estructura_organizacional "
				+ "WHERE IDE_GEANI="+com_anio.getValue().toString()) ;
	

		if (tabEstrucuturaVigente.getTotalFilas()>0) {
			
		}else {
			importarEstructura(com_anio.getValue().toString());
				}	
	
	
	
	}
	

}
		
tab_poa.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where  ide_geani="+com_anio.getValue()+")");		
tab_poa.ejecutarSql();
arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where not ide_geeso is null and ide_geani=11 )");
arb_arbol.ejecutarSql();

	
		
		//tab_poa.getColumna("ide_geani").setValorDefecto(com_anio.getValue().toString());
	}
	else{
		arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where not ide_geeso is null and ide_geani=-1 )");
		arb_arbol.ejecutarSql();		
		
	}
	
	utilitario.addUpdate("arb_arbol,tab_poa");

	
}
public Combo getCom_anio() {
	return com_anio;
}
public void setCom_anio(Combo com_anio) {
	this.com_anio = com_anio;
}




}


