package paq_bodega;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_producto extends Pantalla{
	
private Tabla tab_tipo_producto=new Tabla();
	
	
	public pre_tipo_producto(){
		tab_tipo_producto.setId("tab_tipo_producto");
		tab_tipo_producto.setTabla("bodt_tipo_producto","ide_botip", 1);
		tab_tipo_producto.dibujar();
		PanelTabla pat_tipo_producto=new PanelTabla();
		pat_tipo_producto.setPanelTabla(tab_tipo_producto);
		
		agregarComponente(pat_tipo_producto);
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_producto.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_producto.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_producto.eliminar();
	}


	public Tabla getTab_tipo_producto() {
		return tab_tipo_producto;
	}


	public void setTab_tipo_producto(Tabla tab_tipo_producto) {
		this.tab_tipo_producto = tab_tipo_producto;
	}

}
