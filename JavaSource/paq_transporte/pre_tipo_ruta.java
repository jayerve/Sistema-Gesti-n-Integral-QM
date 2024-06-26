package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_ruta extends Pantalla{

	private Tabla tab_ruta=new Tabla();
	
	public pre_tipo_ruta() {
		tab_ruta.setId("tab_ruta");  
		tab_ruta.setTabla("veh_ruta", "ide_verut", 1);	
		tab_ruta.setRows(10);
		tab_ruta.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_ruta);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_ruta.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_ruta.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_ruta.eliminar();
	}

	public Tabla getTab_ruta() {
		return tab_ruta;
	}

	public void setTab_ruta(Tabla tab_ruta) {
		this.tab_ruta = tab_ruta;
	}



}
