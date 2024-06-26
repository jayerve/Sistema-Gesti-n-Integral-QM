package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_color extends Pantalla{

	private Tabla tab_color=new Tabla();
	
	public pre_tipo_color() {
		tab_color.setId("tab_color");  
		tab_color.setTabla("veh_tipo_color", "ide_vecol", 1);	
		tab_color.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_color);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_color.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_color.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_color.eliminar();
	}

	public Tabla gettab_color() {
		return tab_color;
	}

	public void settab_color(Tabla tab_color) {
		this.tab_color = tab_color;
	}


}
