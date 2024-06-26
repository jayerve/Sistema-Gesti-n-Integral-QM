package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_movimiento extends Pantalla {
	private Tabla  tab_tipo_movimiento=new Tabla();
	
	public pre_tipo_movimiento(){
		
		tab_tipo_movimiento.setId("tab_tipo_movimiento");
		tab_tipo_movimiento.setNumeroTabla(1);
		tab_tipo_movimiento.setTabla("cont_tipo_movimiento", "ide_cotim", 1);
		tab_tipo_movimiento.dibujar();
		PanelTabla pat_tipo_movimiento=new PanelTabla();
		pat_tipo_movimiento.setPanelTabla(tab_tipo_movimiento);
		
		
		agregarComponente(pat_tipo_movimiento);
		
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_movimiento.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_movimiento.guardar();
		guardarPantalla();
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_movimiento.eliminar();
	}

	public Tabla getTab_tipo_movimiento() {
		return tab_tipo_movimiento;
	}

	public void setTab_tipo_movimiento(Tabla tab_tipo_movimiento) {
		this.tab_tipo_movimiento = tab_tipo_movimiento;
	}
	
}
	