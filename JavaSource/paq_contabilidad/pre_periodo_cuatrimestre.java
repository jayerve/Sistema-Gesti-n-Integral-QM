package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_periodo_cuatrimestre extends Pantalla{

	private Tabla tab_periodo_cuatrimestre=new Tabla();
	
	
	
	public pre_periodo_cuatrimestre() {
		tab_periodo_cuatrimestre.setId("tab_periodo_cuatrimestre");  
		tab_periodo_cuatrimestre.setTabla("cont_periodo_cuatrimestre","ide_copec", 1);	
	
		tab_periodo_cuatrimestre.dibujar();
		PanelTabla pat_tipo_compra=new PanelTabla();
		pat_tipo_compra.setPanelTabla(tab_periodo_cuatrimestre);
		
		agregarComponente(pat_tipo_compra);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_periodo_cuatrimestre.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_periodo_cuatrimestre.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_periodo_cuatrimestre.eliminar();
	}

	public Tabla gettab_periodo_cuatrimestre() {
		return tab_periodo_cuatrimestre;
	}

	public void settab_periodo_cuatrimestre(Tabla tab_periodo_cuatrimestre) {
		this.tab_periodo_cuatrimestre = tab_periodo_cuatrimestre;
	}


}
