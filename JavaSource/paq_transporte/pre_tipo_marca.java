package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_marca extends Pantalla{

	private Tabla tab_marca=new Tabla();
	
	public pre_tipo_marca() {
		tab_marca.setId("tab_marca");  
		tab_marca.setTabla("veh_tipo_marca", "ide_vemar", 1);	
		tab_marca.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_marca);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_marca.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_marca.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_marca.eliminar();
	}

	public Tabla gettab_marca() {
		return tab_marca;
	}

	public void settab_marca(Tabla tab_marca) {
		this.tab_marca = tab_marca;
	}


}
