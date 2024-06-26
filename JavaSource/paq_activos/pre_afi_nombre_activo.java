package paq_activos;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_afi_nombre_activo extends Pantalla {
	
	private Tabla tab_nombre_activo=new Tabla();
	
	
	public pre_afi_nombre_activo(){
		tab_nombre_activo.setId("tab_nombre_activo");
		tab_nombre_activo.setTabla("afi_nombre_activo","ide_afnoa", 1);
		tab_nombre_activo.getColumna("ide_afgra").setCombo("afi_grupo_activo", "ide_afgra", "detalle_afgra,vida_util_afgra", "");
		tab_nombre_activo.dibujar();
		PanelTabla pat_nombre_activo=new PanelTabla();
		pat_nombre_activo.setPanelTabla(tab_nombre_activo);
		
		agregarComponente(pat_nombre_activo);
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_nombre_activo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_nombre_activo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_nombre_activo.eliminar();
	}

	public Tabla getTab_nombre_activo() {
		return tab_nombre_activo;
	}

	public void setTab_nombre_activo(Tabla tab_nombre_activo) {
		this.tab_nombre_activo = tab_nombre_activo;
	}
	
	
}
