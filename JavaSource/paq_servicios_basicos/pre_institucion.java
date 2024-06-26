package paq_servicios_basicos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_institucion extends Pantalla{

	private Tabla tab_institucion=new Tabla();
	
	public pre_institucion() {
		tab_institucion.setId("tab_institucion");  
		tab_institucion.setTabla("gen_institucion", "ide_geins", 1);	
		tab_institucion.dibujar();
		PanelTabla pat_institucion=new PanelTabla();
		pat_institucion.setPanelTabla(tab_institucion);
		
		agregarComponente(pat_institucion);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_institucion.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_institucion.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_institucion.eliminar();
	}

	public Tabla gettab_institucion() {
		return tab_institucion;
	}

	public void settab_institucion(Tabla tab_institucion) {
		this.tab_institucion = tab_institucion;
	}


}
