package paq_servicios_basicos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_suministro extends Pantalla{

	private Tabla tab_suministro=new Tabla();
	
	
	
	public pre_suministro() {
		tab_suministro.setId("tab_suministro");  
		//tab_suministro.setTabla("cont_suministro", "", 1);	
		tab_suministro.dibujar();
		PanelTabla pat_suministro=new PanelTabla();
		pat_suministro.setPanelTabla(tab_suministro);
		
		agregarComponente(pat_suministro);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_suministro.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_suministro.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_suministro.eliminar();
	}

	public Tabla gettab_suministro() {
		return tab_suministro;
	}

	public void settab_suministro(Tabla tab_suministro) {
		this.tab_suministro = tab_suministro;
	}


}
