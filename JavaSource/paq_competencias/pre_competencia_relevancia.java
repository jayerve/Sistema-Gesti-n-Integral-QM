package paq_competencias;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_competencia_relevancia extends Pantalla {
   
	private Tabla tab_grupo_cargo_area=new Tabla();
	private Tabla tab_factor_competencia=new Tabla();
	private Tabla tab_detalle_competenca=new Tabla();
	private Tabla tab_competencia_relevancia=new Tabla();
	private Tabla tab_nueva=new Tabla();
	private Combo cmb_fact_eval = new Combo();


	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	

	public pre_competencia_relevancia() {

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		//bot_limpiar.setMetodo("limpiar");
		cmb_fact_eval.setId("cmb_fact_eval");
		cmb_fact_eval.setCombo("select IDE_EVFAE,DETALLE_EVFAE from EVL_FACTOR_EVALUACION");
		cmb_fact_eval.setMetodo("filtrarCompetenciaRelevancia");

		Etiqueta eti_colaborador=new Etiqueta("Factor Evaluacion:");
		//bar_botones.agregarComponente(eti_colaborador);
	//	bar_botones.agregarComponente(cmb_fact_eval);
		//bar_botones.agregarBoton(bot_limpiar);
		
		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);


		//  grupo_cargo_area (division 1)

		tab_grupo_cargo_area.setId("tab_grupo_cargo_area");		
		tab_grupo_cargo_area.setTabla("GEN_GRUPO_CARGO_AREA", "IDE_GEGCA", 1);		
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setCheck();
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setValorDefecto("true");
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setCombo("SELECT a.ide_gedep,b.detalle_geare,a.detalle_gedep FROM gen_departamento a, gen_area b " +
				"where a.ide_geare=b.ide_geare order by b.detalle_geare,a.detalle_gedep");
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setAutoCompletar();
		tab_grupo_cargo_area.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_grupo_cargo_area.getColumna("IDE_GEGRO").setAutoCompletar();
		tab_grupo_cargo_area.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_grupo_cargo_area.getColumna("IDE_GECAF").setAutoCompletar();		
		tab_grupo_cargo_area.getGrid().setColumns(4);
		tab_grupo_cargo_area.setTipoFormulario(true);		
		tab_grupo_cargo_area.setLectura(true);
		tab_grupo_cargo_area.agregarRelacion(tab_detalle_competenca);
		tab_grupo_cargo_area.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_grupo_cargo_area);
		pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel1.getMenuTabla().getItem_guardar().setRendered(false);
		pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);

		// factor_competencia
		//arb_arbol.setId("arb_arbol");
		//arb_arbol.setArbol("CMP_FACTOR_COMPETENCIA", "IDE_CMFAC", "DETALLE_CMFAC", "CMP_IDE_CMFAC");
		//arb_arbol.onSelect("seleccionar_arbol");	
		//arb_arbol.dibujar();
			
		tab_detalle_competenca.setId("tab_detalle_competenca");
		tab_detalle_competenca.setTabla("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", 3);
		//tab_detalle_competenca.setCondicion("IDE_CDMEC=-1");
		tab_detalle_competenca.getColumna("ACTIVO_CMDEC").setCheck();
		tab_detalle_competenca.getColumna("ACTIVO_CMDEC").setValorDefecto("true");
		tab_detalle_competenca.getColumna("IDE_GEGCA").setValorDefecto(tab_grupo_cargo_area.getValorSeleccionado());
		tab_detalle_competenca.getColumna("IDE_CMFAC").setVisible(false);
		tab_detalle_competenca.dibujar();

		
		PanelTabla pat_panel3 =new PanelTabla();
		pat_panel3.setPanelTabla(tab_detalle_competenca);
		
		
		tab_competencia_relevancia.setId("tab_competencia_relevancia");
		tab_competencia_relevancia.setTabla("EVL_COMPETENCIA_RELEVANCIA","IDE_EVCOR", 4);
		tab_competencia_relevancia.setCondicion("IDE_EVCOR=-1");
//		tab_competencia_relevancia.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA","IDE_CMDEC","DETALLE_CMDEC", "");
//		tab_competencia_relevancia.getColumna("IDE_EVFAE").setCombo("EVL_FACTOR_EVALUACION","IDE_EVFAE","DETALLE_EVFAE", "");
		tab_competencia_relevancia.getColumna("IDE_EVFAE").setVisible(false);
		//tab_competencia_relevancia.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA","IDE_EVREL","DETALLE_EVREL", "");
		tab_competencia_relevancia.getColumna("IDE_EVREL").setVisible(false);
		tab_competencia_relevancia.getColumna("IDE_CMDEC").setVisible(false);
		tab_competencia_relevancia.getColumna("ACTIVO_EVCOR").setCheck();
		tab_competencia_relevancia.getColumna("ACTIVO_EVCOR").setValorDefecto("true");
		tab_competencia_relevancia.dibujar();
		PanelTabla pat_panel4 =new PanelTabla();
		pat_panel4.setPanelTabla(tab_competencia_relevancia);
		
		
		tab_nueva.setId("tab_nueva");
		tab_nueva.setSql("SELECT ide_cmdec,detalle_cmdec,ide_gegca from cmp_detalle_competencia where ide_cmfac in (select ide_cmfac from cmp_factor_competencia ) " +
				"and  ide_cmdec=-1");
	
		tab_nueva.setLectura(true);
		tab_nueva.onSelect("filtrarFactor");
		tab_nueva.dibujar();
		PanelTabla pat_panel5 =new PanelTabla();
		pat_panel5.setPanelTabla(tab_nueva);
		Grid gri_fact_eval = new Grid();
		gri_fact_eval.setId("gri_fact_eval");
		gri_fact_eval.setColumns(1);
		gri_fact_eval.getChildren().add(new Etiqueta("Factor Evaluacion"));
		gri_fact_eval.getChildren().add(cmb_fact_eval);
		gri_fact_eval.getChildren().add(tab_nueva);
		
		Division div_aux=new Division();
		div_aux.setId("div_aux");
		div_aux.dividir2(gri_fact_eval,pat_panel4, "30%", "V");

		//  DIVISION DE LA PANTALLA
		
		Division div_division=new Division();
		div_division.dividir2(pat_panel1, div_aux, "50%", "H");		
		agregarComponente(div_division);
	}

	public void filtrarFactor(SelectEvent evt){
		tab_nueva.seleccionarFila(evt);
		if(cmb_fact_eval.getValue()!=null){
			tab_competencia_relevancia.setCondicion("IDE_CMDEC="+tab_nueva.getValorSeleccionado()+" AND "+"IDE_EVFAE="+cmb_fact_eval.getValue());
			tab_competencia_relevancia.ejecutarSql();
			utilitario.addUpdate("tab_nueva,tab_competencia_relevancia");
			}
		else {
			utilitario.addUpdate("tab_nueva,tab_competencia_relevancia");
		}
	}
	
	public void filtrarCompetenciaRelevancia(){

		if(cmb_fact_eval.getValue()!=null){
			tab_nueva.setSql("SELECT ide_cmdec,detalle_cmdec,ide_gegca from cmp_detalle_competencia where ide_cmfac in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+cmb_fact_eval.getValue()+") and ide_gegca="+tab_grupo_cargo_area.getValorSeleccionado());
//tab_nueva.setCondicion("IDE_GEGCA="+tab_detalle_competenca.getValorSeleccionado()+" AND "+"IDE_EVFAE="+cmb_fact_eval.getValue());
tab_nueva.imprimirSql();		
tab_nueva.ejecutarSql();

		tab_competencia_relevancia.setCondicion("IDE_CMDEC="+tab_nueva.getValorSeleccionado()+" AND "+"IDE_EVFAE="+cmb_fact_eval.getValue());
		tab_competencia_relevancia.ejecutarSql();
		
	}
		else {
			utilitario.agregarMensajeInfo("No se puede encontrar", "Primero debe escoger una Factor de Evaluacion");
		}
	}
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */

	@Override
	public void insertar() {		
	
			if (tab_competencia_relevancia.isFocus()){
				if (tab_grupo_cargo_area.getTotalFilas()>0) {
					tab_competencia_relevancia.insertar();
					tab_competencia_relevancia.setValor("IDE_CMDEC", tab_nueva.getValorSeleccionado());
					tab_competencia_relevancia.setValor("IDE_EVFAE",""+cmb_fact_eval.getValue());
		
			}
				else {
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe ingresar Cabecera");
				}
				
			}
			
		
		
	}

	@Override
	public void guardar() {		
	if(tab_grupo_cargo_area.getTotalFilas()>0){
		

		if(cmb_fact_eval.getValue()!=null){
			
		
		if (tab_competencia_relevancia.guardar()){	
		
		guardarPantalla();

		}
		
		}else {
			utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe escoger una Opcion Factor Evaluacion");
		}
	}
	else {
		utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe existir una Cabecera");
	}
	}


	@Override
	public void eliminar() {		
		if(tab_competencia_relevancia.isFocus()){
			tab_competencia_relevancia.eliminar();
		}						
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

	public Tabla getTab_grupo_cargo_area() {
		return tab_grupo_cargo_area;
	}

	public void setTab_grupo_cargo_area(Tabla tab_grupo_cargo_area) {
		this.tab_grupo_cargo_area = tab_grupo_cargo_area;
	}

	public Tabla getTab_factor_competencia() {
		return tab_factor_competencia;
	}

	public void setTab_factor_competencia(Tabla tab_factor_competencia) {
		this.tab_factor_competencia = tab_factor_competencia;
	}

	public Tabla getTab_detalle_competenca() {
		return tab_detalle_competenca;
	}

	public void setTab_detalle_competenca(Tabla tab_detalle_competenca) {
		this.tab_detalle_competenca = tab_detalle_competenca;
	}


	

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {	
	}





	public Combo getCmb_fact_eval() {
		return cmb_fact_eval;
	}





	public void setCmb_fact_eval(Combo cmb_fact_eval) {
		this.cmb_fact_eval = cmb_fact_eval;
	}



	public Tabla getTab_competencia_relevancia() {
		return tab_competencia_relevancia;
	}



	public void setTab_competencia_relevancia(Tabla tab_competencia_relevancia) {
		this.tab_competencia_relevancia = tab_competencia_relevancia;
	}



	public Tabla getTab_nueva() {
		return tab_nueva;
	}



	public void setTab_nueva(Tabla tab_nueva) {
		this.tab_nueva = tab_nueva;
	}
	
	
	
}


