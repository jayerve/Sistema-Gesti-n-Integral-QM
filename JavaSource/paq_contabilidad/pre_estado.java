package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_estado extends Pantalla{

	private Tabla tab_estado=new Tabla();
	
	public pre_estado() {
		tab_estado.setId("tab_estado");  
		tab_estado.setTabla("cont_estado", "ide_coest", 1);	
		tab_estado.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_estado);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_estado.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_estado.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_estado.eliminar();
	}

	public Tabla gettab_estado() {
		return tab_estado;
	}

	public void settab_estado(Tabla tab_estado) {
		this.tab_estado = tab_estado;
	}


}
