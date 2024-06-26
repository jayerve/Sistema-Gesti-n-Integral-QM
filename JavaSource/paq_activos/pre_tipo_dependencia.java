package paq_activos;


import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_tipo_dependencia extends Pantalla{
	
	private Tabla tab_tipo_dependencia= new Tabla();
	
	public pre_tipo_dependencia() {
	
	tab_tipo_dependencia.setId("tab_tipo_dependencia");  
	tab_tipo_dependencia.setTabla("afi_tipo_dependencia", "ide_aftid", 1);	
	tab_tipo_dependencia.dibujar();
	PanelTabla pat_tipo_dependencia=new PanelTabla();
	pat_tipo_dependencia.setPanelTabla(tab_tipo_dependencia);
	
    agregarComponente(tab_tipo_dependencia);

	}

	public Tabla getTab_tipo_dependencia() {
		return tab_tipo_dependencia;
	}

	public void setTab_tipo_dependencia(Tabla tab_tipo_dependencia) {
		this.tab_tipo_dependencia = tab_tipo_dependencia;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_dependencia.insertar();
		guardarPantalla();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_dependencia.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_dependencia.eliminar();
		guardarPantalla();
	}
	

}
