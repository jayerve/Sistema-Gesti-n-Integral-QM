package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_asiento extends Pantalla {

	private Tabla tab_asiento = new Tabla();
	
	
	public pre_tipo_asiento (){
		tab_asiento.setId("tab_asiento");
		tab_asiento.setHeader("TIPO ASIENTO");
		tab_asiento.setTabla("cont_tipo_asiento", "ide_cotia", 1);
		tab_asiento.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_asiento);
		agregarComponente(pat_panel);
		
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_asiento.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_asiento.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_asiento.eliminar();
		
	}
	public Tabla getTab_asiento() {
		return tab_asiento;
	}
	public void setTab_asiento(Tabla tab_asiento) {
		this.tab_asiento = tab_asiento;
	}

}
