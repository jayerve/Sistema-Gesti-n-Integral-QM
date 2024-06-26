package paq_salud;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_codigo_sie10 extends Pantalla {
	private Tabla tab_codigo= new Tabla();
	
	public pre_codigo_sie10(){
		tab_codigo.setId("tab_codigo");
		tab_codigo.setTabla("sao_codigo_sie10", "ide_sacos", 1);
		tab_codigo.dibujar();
		PanelTabla pat_panel1 =new PanelTabla();
		pat_panel1.setPanelTabla(tab_codigo);
		agregarComponente(tab_codigo);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_codigo.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_codigo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_codigo.eliminar();
	}

	public Tabla getTab_codigo() {
		return tab_codigo;
	}

	public void setTab_codigo(Tabla tab_codigo) {
		this.tab_codigo = tab_codigo;
	}

}
