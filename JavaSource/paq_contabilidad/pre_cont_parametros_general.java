package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_cont_parametros_general extends Pantalla{
	private Tabla tab_parametros_general= new Tabla();

	public pre_cont_parametros_general() {
		tab_parametros_general.setId("tab_parametros_general");
		tab_parametros_general.setTabla("cont_parametros_general", "ide_copag", 1);
		tab_parametros_general.dibujar();
		
		PanelTabla pat_parametro_general= new PanelTabla();
		pat_parametro_general.setPanelTabla(tab_parametros_general);
		agregarComponente(tab_parametros_general);
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		if(tab_parametros_general.guardar()){
			guardarPantalla();
		}
		
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
		
	}

	public Tabla getTab_parametros_general() {
		return tab_parametros_general;
	}

	public void setTab_parametros_general(Tabla tab_parametros_general) {
		this.tab_parametros_general = tab_parametros_general;
	}

}
