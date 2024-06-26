package paq_servicios_basicos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_modulo extends Pantalla{

	private Tabla tab_modulo=new Tabla();
	
	
	
	public pre_modulo() {
		tab_modulo.setId("tab_modulo");  
		tab_modulo.setTabla("gen_modulo", "ide_gemod", 1);	
		tab_modulo.dibujar();
		PanelTabla pat_modulo=new PanelTabla();
		pat_modulo.setPanelTabla(tab_modulo);
		
		agregarComponente(pat_modulo);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_modulo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_modulo.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_modulo.eliminar();
	}

	public Tabla gettab_modulo() {
		return tab_modulo;
	}

	public void settab_modulo(Tabla tab_modulo) {
		this.tab_modulo = tab_modulo;
	}


}
