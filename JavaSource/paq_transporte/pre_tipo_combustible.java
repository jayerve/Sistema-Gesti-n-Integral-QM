package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_combustible extends Pantalla{

	private Tabla tab_combustible=new Tabla();
	
	public pre_tipo_combustible() {
		tab_combustible.setId("tab_combustible");  
		tab_combustible.setTabla("veh_tipo_combustible", "ide_vecom", 1);	
		tab_combustible.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_combustible);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_combustible.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_combustible.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_combustible.eliminar();
	}

	public Tabla gettab_combustible() {
		return tab_combustible;
	}

	public void settab_combustible(Tabla tab_combustible) {
		this.tab_combustible = tab_combustible;
	}


}
