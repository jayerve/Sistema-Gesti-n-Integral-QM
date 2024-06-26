package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_vehiculo extends Pantalla{

	private Tabla tab_tipo_vehiculo=new Tabla();
	
	public pre_tipo_vehiculo() {
		tab_tipo_vehiculo.setId("tab_tipo_vehiculo");  
		tab_tipo_vehiculo.setTabla("veh_tipo_vehiculo", "ide_vetip", 1);	
		tab_tipo_vehiculo.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_tipo_vehiculo);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_vehiculo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_vehiculo.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_vehiculo.eliminar();
	}

	public Tabla gettab_tipo_vehiculo() {
		return tab_tipo_vehiculo;
	}

	public void settab_tipo_vehiculo(Tabla tab_tipo_vehiculo) {
		this.tab_tipo_vehiculo = tab_tipo_vehiculo;
	}


}
