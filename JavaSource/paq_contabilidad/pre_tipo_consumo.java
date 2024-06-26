package paq_contabilidad;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_tipo_consumo extends Pantalla{
	
	private Tabla  tab_tipo_consumo=new Tabla();
	
	public pre_tipo_consumo(){
				
		tab_tipo_consumo.setId("tab_tipo_consumo");
		tab_tipo_consumo.setNumeroTabla(1);
		tab_tipo_consumo.setTabla("cont_tipo_consumo", "ide_cotic", 1);
		tab_tipo_consumo.dibujar();
		PanelTabla pat_tipo_consumo=new PanelTabla();
		pat_tipo_consumo.setPanelTabla(tab_tipo_consumo);
		
		
		agregarComponente(pat_tipo_consumo);
		
		
		
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_consumo.insertar();
		
	}


	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_consumo.guardar();
		guardarPantalla();
	}


	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_consumo.eliminar();
		
	}


	public Tabla getTab_tipo_consumo() {
		return tab_tipo_consumo;
	}


	public void setTab_tipo_consumo(Tabla tab_tipo_consumo) {
		this.tab_tipo_consumo = tab_tipo_consumo;
	}
	
	

}
