package paq_tesoreria;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_poliza extends Pantalla{
	private Tabla tab_tipo_poliza= new Tabla();
	private Tabla tab_renovacion_poliza= new Tabla();
	
	public pre_tipo_poliza(){
		tab_tipo_poliza.setId("tab_tipo_poliza");
		tab_tipo_poliza.setNumeroTabla(1);
		tab_tipo_poliza.setTabla("tes_tipo_poliza", "ide_tetip", 1);
		tab_tipo_poliza.dibujar();
		PanelTabla pat_tipo_poliza=new PanelTabla();
		pat_tipo_poliza.setPanelTabla(tab_tipo_poliza);
		
		agregarComponente(pat_tipo_poliza);
	}
	
	
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_poliza.insertar();
			
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_poliza.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_poliza.eliminar();
		
	}
	
	public Tabla getTab_tipo_poliza() {
		return tab_tipo_poliza;
	}

	public void setTab_tipo_poliza(Tabla tab_tipo_poliza) {
		this.tab_tipo_poliza = tab_tipo_poliza;
	}


	public Tabla getTab_renovacion_poliza() {
		return tab_renovacion_poliza;
	}


	public void setTab_renovacion_poliza(Tabla tab_renovacion_poliza) {
		this.tab_renovacion_poliza = tab_renovacion_poliza;
	}



}
