package paq_tesoreria;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_impuesto extends Pantalla{
	private Tabla tab_tipo_impuesto=new Tabla();
	
	public pre_tipo_impuesto(){
		
		tab_tipo_impuesto.setId("tab_tipo_impuesto");
		tab_tipo_impuesto.setHeader("TIPO DE IMPUESTO");
		tab_tipo_impuesto.setTabla("tes_tipo_impuesto", "ide_tetii", 1);
		tab_tipo_impuesto.dibujar();
		PanelTabla pat_panel =new PanelTabla();
		pat_panel.setPanelTabla(tab_tipo_impuesto);
		agregarComponente(pat_panel);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_impuesto.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_impuesto.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_impuesto.eliminar();
		
	}

	public Tabla getTab_tipo_impuesto() {
		return tab_tipo_impuesto;
	}

	public void setTab_tipo_impuesto(Tabla tab_tipo_impuesto) {
		this.tab_tipo_impuesto = tab_tipo_impuesto;
	}
	



}
