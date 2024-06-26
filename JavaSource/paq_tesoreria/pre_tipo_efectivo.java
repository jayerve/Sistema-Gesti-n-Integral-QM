package paq_tesoreria;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_tipo_efectivo extends Pantalla{
	private Tabla tab_tipo_efectivo = new Tabla();

	public pre_tipo_efectivo(){
		
		tab_tipo_efectivo.setId("tab_tipo_efectivo");
		tab_tipo_efectivo.setTabla("tes_tipo_efectivo", "ide_ttefe", 1);
		//tab_tipo_efectivo.getColumna("tipo_ttefe").setCombo(utilitario.getListaTipoEfectivo());	
		tab_tipo_efectivo.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tipo_efectivo);
		agregarComponente(pat_panel);
		
	}

	public Tabla getTab_caja() {
		return tab_tipo_efectivo;
	}

	public void setTab_caja(Tabla tab_tipo_efectivo) {
		this.tab_tipo_efectivo = tab_tipo_efectivo;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_efectivo.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_efectivo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_efectivo.eliminar();

	}

	public Tabla getTab_tipo_efectivo() {
		return tab_tipo_efectivo;
	}

	public void setTab_tipo_efectivo(Tabla tab_tipo_efectivo) {
		this.tab_tipo_efectivo = tab_tipo_efectivo;
	}

	
	
}