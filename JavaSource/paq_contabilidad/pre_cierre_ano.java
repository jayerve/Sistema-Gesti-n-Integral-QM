package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_cierre_ano extends Pantalla{

	private Tabla tab_cierre_año=new Tabla();
	
	
	
	public pre_cierre_ano() {
		tab_cierre_año.setId("tab_cierre_año");  
		tab_cierre_año.setTabla("cont_cierre_ano","ide_cocia", 1);	
	    tab_cierre_año.getColumna("ide_cocim").setCombo("cont_cierre_movimiento", "ide_cocim", "detalle_cocim", "");
        tab_cierre_año.getColumna("ide_geani").setCombo("gen_anio", "ide_geani", "detalle_geani", "");
		tab_cierre_año.dibujar();
		PanelTabla pat_cierre_año=new PanelTabla();
		pat_cierre_año.setPanelTabla(tab_cierre_año);
		
		agregarComponente(pat_cierre_año);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cierre_año.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_cierre_año.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cierre_año.eliminar();
	}

	public Tabla gettab_cierre_año() {
		return tab_cierre_año;
	}

	public void settab_cierre_año(Tabla tab_cierre_año) {
		this.tab_cierre_año = tab_cierre_año;
	}


}
