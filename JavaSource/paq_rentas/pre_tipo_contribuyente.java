package paq_rentas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_contribuyente extends Pantalla {
private Tabla tab_tipo_contribuyente=new Tabla();



public pre_tipo_contribuyente() {
	

	tab_tipo_contribuyente.setId("tab_tipo_contribuyente");  
	tab_tipo_contribuyente.setTabla("rec_tipo_contribuyente", "ide_retic", 1);	
	tab_tipo_contribuyente.dibujar();
	PanelTabla pat_tipo_contribuyente=new PanelTabla();
	pat_tipo_contribuyente.setPanelTabla(tab_tipo_contribuyente);
	
	agregarComponente(pat_tipo_contribuyente);
}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_contribuyente.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_contribuyente.guardar();
		guardarPantalla();	
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_contribuyente.eliminar();
		
	}
	public Tabla getTab_tipo_contribuyente() {
		return tab_tipo_contribuyente;
	}
	public void setTab_tipo_contribuyente(Tabla tab_tipo_contribuyente) {
		this.tab_tipo_contribuyente = tab_tipo_contribuyente;
	}
	

}
