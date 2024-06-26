package paq_rentas;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_tipo_cliente extends Pantalla{
	
	private Tabla tab_tipo_cliente=new Tabla();
	
	public pre_tipo_cliente (){
		tab_tipo_cliente.setId("tab_tipo_cliente");
		tab_tipo_cliente.setTabla("rec_tipo_cliente", "ide_retil", 1);
		tab_tipo_cliente.dibujar();
		PanelTabla pat_tipo_cliente =new PanelTabla();
		pat_tipo_cliente.setPanelTabla(tab_tipo_cliente);
		agregarComponente(pat_tipo_cliente);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_cliente.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_cliente.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_cliente.eliminar();
		
	}

	public Tabla getTab_tipo_cliente() {
		return tab_tipo_cliente;
	}

	public void setTab_tipo_cliente(Tabla tab_tipo_cliente) {
		this.tab_tipo_cliente = tab_tipo_cliente;
	}
	
	

}
