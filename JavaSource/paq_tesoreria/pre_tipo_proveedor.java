package paq_tesoreria;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_proveedor extends Pantalla {
	private Tabla tab_tipo_proveedor=new Tabla();
	
	
	public pre_tipo_proveedor(){
		tab_tipo_proveedor.setId("tab_tipo_proveedor");
		tab_tipo_proveedor.setTabla("tes_tipo_proveedor", "ide_tetpp", 1);
		tab_tipo_proveedor.dibujar();
		PanelTabla pat_tipo=new PanelTabla();
		pat_tipo.setPanelTabla(tab_tipo_proveedor);
		agregarComponente(pat_tipo);
		
}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_proveedor.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_proveedor.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_proveedor.eliminar();
		
	}

	public Tabla getTab_tipo_proveedor() {
		return tab_tipo_proveedor;
	}

	public void setTab_tipo_proveedor(Tabla tab_tipo_proveedor) {
		this.tab_tipo_proveedor = tab_tipo_proveedor;
	}

}
