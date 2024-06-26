package paq_activos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_grupo_activo extends Pantalla {
	private Tabla tab_grupo = new Tabla();
	
	
	
	public pre_grupo_activo (){
		tab_grupo.setId("tab_grupo");
		tab_grupo.setTabla("afi_grupo_activo", "ide_afgra", 1);
		tab_grupo.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_grupo);
		
		agregarComponente(pat_panel);
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_grupo.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_grupo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_grupo.eliminar();
	}
	public Tabla getTab_grupo() {
		return tab_grupo;
	}
	public void setTab_grupo(Tabla tab_grupo) {
		this.tab_grupo = tab_grupo;
	}
	

}
