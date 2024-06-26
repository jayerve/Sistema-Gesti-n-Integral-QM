package paq_activos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_propiedad extends Pantalla{

	private Tabla tab_tipo_propiedad=new Tabla();
	
	
	
	public pre_tipo_propiedad() {
		tab_tipo_propiedad.setId("tab_tipo_propiedad");  
		tab_tipo_propiedad.setTabla("afi_tipo_propiedad", "ide_aftip", 1);	
		tab_tipo_propiedad.dibujar();
		PanelTabla pat_tipo_propiedad=new PanelTabla();
		pat_tipo_propiedad.setPanelTabla(tab_tipo_propiedad);
		
		agregarComponente(pat_tipo_propiedad);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_propiedad.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_propiedad.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_propiedad.eliminar();
	}

	public Tabla gettab_tipo_propiedad() {
		return tab_tipo_propiedad;
	}

	public void settab_tipo_propiedad(Tabla tab_tipo_propiedad) {
		this.tab_tipo_propiedad = tab_tipo_propiedad;
	}


}
