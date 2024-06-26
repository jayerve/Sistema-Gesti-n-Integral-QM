package paq_rentas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_asistencia extends Pantalla {
	private Tabla tab_tipo_asistencia=new Tabla();

	public pre_tipo_asistencia() {
		

		tab_tipo_asistencia.setId("tab_tipo_asistencia");  
		tab_tipo_asistencia.setTabla("rec_tipo_asistencia","ide_retia", 1);	
		tab_tipo_asistencia.dibujar();
		PanelTabla pat_tipo_asistencia=new PanelTabla();
		pat_tipo_asistencia.setPanelTabla(tab_tipo_asistencia);
		
		agregarComponente(pat_tipo_asistencia);
	
	
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_asistencia.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stubç
		tab_tipo_asistencia.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_asistencia.eliminar();
		
	}
	public Tabla getTab_tipo_asistencia() {
		return tab_tipo_asistencia;
	}
	public void setTab_tipo_asistencia(Tabla tab_tipo_asistencia) {
		this.tab_tipo_asistencia = tab_tipo_asistencia;
	}

}
