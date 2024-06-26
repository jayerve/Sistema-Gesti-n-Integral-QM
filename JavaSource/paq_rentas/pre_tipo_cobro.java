package paq_rentas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_cobro extends Pantalla{
	
	private Tabla tab_tipo_cobro=new Tabla();
	
	public pre_tipo_cobro(){
		
		tab_tipo_cobro.setId("tab_tipo_cobro");
		tab_tipo_cobro.setTabla("rec_tipo","ide_retip ", 1);
		tab_tipo_cobro.dibujar();
		PanelTabla pat_tipo_cobro = new PanelTabla();
		pat_tipo_cobro.setPanelTabla(tab_tipo_cobro);
		
		agregarComponente(pat_tipo_cobro);
		
		
		
	}
	
	
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_cobro.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_cobro.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_cobro.eliminar();
	}




	public Tabla getTab_tipo_cobro() {
		return tab_tipo_cobro;
	}




	public void setTab_tipo_cobro(Tabla tab_tipo_cobro) {
		this.tab_tipo_cobro = tab_tipo_cobro;
	}
 
}

