package paq_activos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_activo extends Pantalla{

	private Tabla tab_tipo_activo=new Tabla();
	
	public pre_tipo_activo() {
		tab_tipo_activo.setId("tab_tipo_activo");  
		tab_tipo_activo.setTabla("afi_tipo_activo", "ide_aftia", 1);	
		tab_tipo_activo.dibujar();
		PanelTabla pat_tipo_activo=new PanelTabla();
		pat_tipo_activo.setPanelTabla(tab_tipo_activo);
		
		agregarComponente(pat_tipo_activo);
		
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_activo.insertar();	
		guardarPantalla();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_activo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_activo.eliminar();
		
	}
	public Tabla getTab_tipo_activo() {
		return tab_tipo_activo;
	}

	public void setTab_estado(Tabla tab_tipo_activo) {
		this.tab_tipo_activo = tab_tipo_activo;
	}
}
