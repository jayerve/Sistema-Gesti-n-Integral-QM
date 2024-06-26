package paq_bodega;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_unidad_medida extends Pantalla{
	
private Tabla tab_unidad_medida=new Tabla();
	
	
	public pre_unidad_medida(){
		
		tab_unidad_medida.setId("tab_unidad_medida");
		tab_unidad_medida.setTabla("bodt_unidad_medida","ide_bounm", 1);
		tab_unidad_medida.dibujar();
		PanelTabla pat_unidad_medida=new PanelTabla();
		pat_unidad_medida.setPanelTabla(tab_unidad_medida);
		
		agregarComponente(pat_unidad_medida);
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_unidad_medida.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_unidad_medida.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_unidad_medida.eliminar();
		
	}


	public Tabla getTab_unidad_medida() {
		return tab_unidad_medida;
	}


	public void setTab_unidad_medida(Tabla tab_unidad_medida) {
		this.tab_unidad_medida = tab_unidad_medida;
	}


}
