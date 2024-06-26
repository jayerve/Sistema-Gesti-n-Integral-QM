package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_lugar_suministro extends Pantalla {
	
	private Tabla tab_lugar =new Tabla();
	
	public pre_lugar_suministro(){
		tab_lugar.setId("tab_lugar");
		tab_lugar.setTabla("cont_lugar_suministro", "ide_colus", 1);
		tab_lugar.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_lugar);
		agregarComponente(pat_panel1);
			
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_lugar.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_lugar.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_lugar.eliminar();
	}

	public Tabla getTab_lugar() {
		return tab_lugar;
	}

	public void setTab_lugar(Tabla tab_lugar) {
		this.tab_lugar = tab_lugar;
	}

}
