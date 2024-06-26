package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_modulo_estado extends Pantalla{

	private Tabla tab_modulo_estado=new Tabla();
	
	public pre_modulo_estado() {
		tab_modulo_estado.setId("tab_modulo_estado");  
		tab_modulo_estado.setTabla("gen_modulo_estado", "ide_gemoe", 1);
		tab_modulo_estado.getColumna("ide_gemod").setCombo("gen_modulo", "ide_gemod", "detalle_gemod", "");
		tab_modulo_estado.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_modulo_estado.dibujar();
		PanelTabla pat_modulo_estado=new PanelTabla();
		pat_modulo_estado.setPanelTabla(tab_modulo_estado);
		
		agregarComponente(pat_modulo_estado);
	
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_modulo_estado.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_modulo_estado.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_modulo_estado.eliminar();
	}

	public Tabla gettab_modulo_estado() {
		return tab_modulo_estado;
	}

	public void settab_modulo_estado(Tabla tab_modulo_estado) {
		this.tab_modulo_estado = tab_modulo_estado;
	}


}
