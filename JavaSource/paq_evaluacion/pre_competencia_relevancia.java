/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_evaluacion;

import org.primefaces.event.SelectEvent;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_competencia_relevancia extends Pantalla {
    
	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Combo cmb_fact_eval = new Combo();
	public pre_competencia_relevancia() {
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		cmb_fact_eval.setId("cmb_fact_eval");
		cmb_fact_eval.setCombo("select IDE_EVFAE,DETALLE_EVFAE from EVL_FACTOR_EVALUACION");
		cmb_fact_eval.setMetodo("filtrarCompetenciaRelevancia");

		Etiqueta eti_colaborador=new Etiqueta("Factor Evaluacion:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(cmb_fact_eval);
		bar_botones.agregarBoton(bot_limpiar);

		//	agregarComponente(aut_fact_eval);
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("EVL_RELEVANCIA", "IDE_EVREL", 1);
		// tab_tabla1.setCondicion("IDE_EVREL=-1");
		tab_tabla1.getColumna("ACTIVO_EVREL").setCheck();
		tab_tabla1.getColumna("ACTIVO_EVREL").setValorDefecto("true");
		tab_tabla1.setLectura(true);
		tab_tabla1.onSelect("filtrarTabla2");
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);
		pat_panel1.getMenuTabla().getItem_eliminar().setRendered(true);
		pat_panel1.getMenuTabla().getItem_insertar().setRendered(true);
		pat_panel1.getMenuTabla().getItem_guardar().setRendered(true);
		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("EVL_COMPETENCIA_RELEVANCIA","IDE_EVCOR", 2);
		tab_tabla2.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC","DETALLE_CMDEC","");
		tab_tabla2.setCondicion("IDE_EVCOR=-1");

		tab_tabla2.getColumna("IDE_EVFAE").setVisible(false);
		tab_tabla2.getColumna("IDE_EVREL").setVisible(false);


		tab_tabla2.getColumna("ACTIVO_EVCOR").setCheck();
		//tab_tabla2.setValor("IDE_EVREL", tab_tabla1.getValorSeleccionado());
		tab_tabla2.getColumna("ACTIVO_EVCOR").setValorDefecto("true");
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);
	}

	public void filtrarTabla2(SelectEvent evt){
		tab_tabla1.seleccionarFila(evt);
		if(cmb_fact_eval.getValue()!=null){
			tab_tabla2.setCondicion("IDE_EVREL="+tab_tabla1.getValorSeleccionado()+" AND "+"IDE_EVFAE="+cmb_fact_eval.getValue());
			tab_tabla2.ejecutarSql();
			utilitario.addUpdate("tab_tabla2");
		}
	}

	public void filtrarCompetenciaRelevancia(){
		tab_tabla1.ejecutarSql();
		tab_tabla2.setCondicion("IDE_EVREL="+tab_tabla1.getValorSeleccionado()+" AND "+"IDE_EVFAE="+cmb_fact_eval.getValue());
		tab_tabla2.ejecutarSql();
	}
	public void limpiar() {
		tab_tabla1.limpiar();
		tab_tabla2.limpiar();
		
		utilitario.addUpdate("aut_fact_eval,tab_tabla1,tab_tabla2");// limpia y refresca el autocompletar
	}


	@Override
	public void insertar() {
		System.out.println("ingresa a Insertar...  ");
		System.out.println("antes del if cmb_fact_eval...  "+cmb_fact_eval);
		
		if (cmb_fact_eval.getValue()!=null) {
			System.out.println("ingresa a if cmb_fact_eval...  "+cmb_fact_eval);
			
			if (tab_tabla2.isFocus()) {
				tab_tabla2.insertar();
				tab_tabla2.setValor("IDE_EVFAE", ""+cmb_fact_eval.getValue());
				tab_tabla2.setValor("IDE_EVREL", tab_tabla1.getValorSeleccionado());
			}
		}
		else {
			utilitario.agregarMensajeInfo("No se puede insetar ","No ha seleccionado un Factor de Evaluacion");
		}
	}

	@Override
	public void guardar() {

		if(cmb_fact_eval.getValue()!=null)
		{
			if (tab_tabla2.guardar()) {
				guardarPantalla();
			}
		}else {
			utilitario.agregarMensajeInfo("No ha seleccionado ningun Factor Evaluacion", "");
		}
	}

	@Override
	public void eliminar() {

		if(tab_tabla2.isFocus()){
			tab_tabla2.eliminar();
		}

	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}


}
