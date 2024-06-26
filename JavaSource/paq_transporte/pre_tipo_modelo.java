package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_modelo extends Pantalla{

	private Tabla tab_modelo=new Tabla();
	
	public pre_tipo_modelo() {
		tab_modelo.setId("tab_modelo");  
		tab_modelo.setTabla("veh_tipo_modelo", "ide_vemod", 1);	
		tab_modelo.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_modelo);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_modelo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_modelo.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_modelo.eliminar();
	}

	public Tabla gettab_modelo() {
		return tab_modelo;
	}

	public void settab_modelo(Tabla tab_modelo) {
		this.tab_modelo = tab_modelo;
	}


}
