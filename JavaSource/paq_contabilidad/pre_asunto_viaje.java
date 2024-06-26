package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_asunto_viaje extends Pantalla{
    private Tabla tab_asunto= new Tabla();
    
    public pre_asunto_viaje() {
		// TODO Auto-generated constructor stub
    	
    	tab_asunto.setId("tab_asunto");
    	tab_asunto.setTabla("cont_asunto_viaje", "ide_coasv", 1);
    	tab_asunto.dibujar();
    	PanelTabla pat_asunto = new PanelTabla();
    	pat_asunto.setPanelTabla(tab_asunto);
    	
    agregarComponente(tab_asunto);
    }
    	@Override
	public void insertar() {
		// TODO Auto-generated method stub
    		tab_asunto.insertar();
		
	}
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_asunto.guardar();
		guardarPantalla();
		
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_asunto.eliminar();
		
	}
	public Tabla getTab_asunto() {
		return tab_asunto;
	}
	public void setTab_asunto(Tabla tab_asunto) {
		this.tab_asunto = tab_asunto;
	}


}
