package paq_adquisicion;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tiempo_entrega extends Pantalla{

	private Tabla tab_tiempo_entrega=new Tabla();
	
	public pre_tiempo_entrega() {
		tab_tiempo_entrega.setId("tab_tiempo_entrega");  
		tab_tiempo_entrega.setTabla("adq_tiempo_entrega", "ide_adtie", 1);	
		tab_tiempo_entrega.getColumna("activo_adtie").setValorDefecto("true");

		tab_tiempo_entrega.dibujar();
		PanelTabla pat_tipo_activo=new PanelTabla();
		pat_tipo_activo.setPanelTabla(tab_tiempo_entrega);
		
		agregarComponente(pat_tipo_activo);
		
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tiempo_entrega.insertar();	
		guardarPantalla();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tiempo_entrega.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tiempo_entrega.eliminar();
		
	}
	public Tabla getTab_tiempo_entrega() {
		return tab_tiempo_entrega;
	}
	public void setTab_tiempo_entrega(Tabla tab_tiempo_entrega) {
		this.tab_tiempo_entrega = tab_tiempo_entrega;
	}
	
}
