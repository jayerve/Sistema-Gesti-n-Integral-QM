package paq_salud;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_aptitud_preocupacional extends Pantalla{
	
	private Tabla tab_aptitud=new Tabla();
	
	
	public pre_aptitud_preocupacional(){
		tab_aptitud.setId("tab_aptitud");
		tab_aptitud.setTabla("sao_actitud_preocupacional", "ide_saapp", 1);
		tab_aptitud.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_aptitud);
		agregarComponente(pat_panel);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_aptitud.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_aptitud.guardar();
		
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_aptitud.eliminar();
	}

	public Tabla getTab_aptitud() {
		return tab_aptitud;
	}

	public void setTab_aptitud(Tabla tab_aptitud) {
		this.tab_aptitud = tab_aptitud;
	}

}
