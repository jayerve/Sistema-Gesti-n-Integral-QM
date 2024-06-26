package paq_tesoreria;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;


public class pre_estado_retencion extends Pantalla {
	
	private Tabla tab_estado_retencion=new Tabla();
	
	
	public pre_estado_retencion (){
		
		tab_estado_retencion.setId("tab_estado_retencion");
		tab_estado_retencion.setNumeroTabla(1);
		tab_estado_retencion.setTabla("tes_estado_retencion", "ide_teesr", 1);
		tab_estado_retencion.dibujar();
		PanelTabla pat_estado_retencion=new PanelTabla();
		pat_estado_retencion.setPanelTabla(tab_estado_retencion);
		
		agregarComponente(tab_estado_retencion);
		
		

		
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_estado_retencion.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_estado_retencion.guardar();
		guardarPantalla();
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_estado_retencion.eliminar();
		
		
	}

	public Tabla getTab_estado_retencion() {
		return tab_estado_retencion;
	}

	public void setTab_estado_retencion(Tabla tab_estado_retencion) {
		this.tab_estado_retencion = tab_estado_retencion;
	}

}
