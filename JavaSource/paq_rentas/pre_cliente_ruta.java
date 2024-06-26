package paq_rentas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_cliente_ruta extends Pantalla {
	private Tabla tab_cliente_ruta=new Tabla();
	
	public pre_cliente_ruta (){
		tab_cliente_ruta.setId("tab_cliente_ruta");
		tab_cliente_ruta.setTabla("rec_cliente_ruta", "ide_reclr", 1);
		tab_cliente_ruta.dibujar();
		PanelTabla pat_cliente_ruta=new PanelTabla();
		pat_cliente_ruta.setPanelTabla(tab_cliente_ruta);
		agregarComponente(pat_cliente_ruta);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cliente_ruta.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_cliente_ruta.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cliente_ruta.eliminar();
		
	}

	public Tabla getTab_cliente_ruta() {
		return tab_cliente_ruta;
	}

	public void setTab_cliente_ruta(Tabla tab_cliente_ruta) {
		this.tab_cliente_ruta = tab_cliente_ruta;
	}

}
