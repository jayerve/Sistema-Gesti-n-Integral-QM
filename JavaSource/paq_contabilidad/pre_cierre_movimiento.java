package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_cierre_movimiento extends Pantalla{

	private Tabla tab_cierre_movimiento=new Tabla();
	
	
	
	public pre_cierre_movimiento() {
		tab_cierre_movimiento.setId("tab_cierre_movimiento");  
		tab_cierre_movimiento.setTabla("cont_cierre_movimiento", "ide_cocim", 1);	
		tab_cierre_movimiento.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_cierre_movimiento.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_cierre_movimiento.getColumna("activo_cocim").setValorDefecto("false");
		tab_cierre_movimiento.dibujar();
		PanelTabla pat_cierre_movimiento=new PanelTabla();
		pat_cierre_movimiento.setPanelTabla(tab_cierre_movimiento);
		
		agregarComponente(pat_cierre_movimiento);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cierre_movimiento.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_cierre_movimiento.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cierre_movimiento.eliminar();
	}

	public Tabla gettab_cierre_movimiento() {
		return tab_cierre_movimiento;
	}

	public void settab_cierre_movimiento(Tabla tab_cierre_movimiento) {
		this.tab_cierre_movimiento = tab_cierre_movimiento;
	}


}
