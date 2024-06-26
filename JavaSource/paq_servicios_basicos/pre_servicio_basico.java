package paq_servicios_basicos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_servicio_basico extends Pantalla{

	private Tabla tab_servicio_basico=new Tabla();
	
	
	
	public pre_servicio_basico() {
		tab_servicio_basico.setId("tab_servicio_basico");  
		tab_servicio_basico.setTabla("cont_sevicio_basico", "ide_coseb", 1);	
		tab_servicio_basico.dibujar();
		PanelTabla pat_servicio_basico=new PanelTabla();
		pat_servicio_basico.setPanelTabla(tab_servicio_basico);
		
		agregarComponente(pat_servicio_basico);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_servicio_basico.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_servicio_basico.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_servicio_basico.eliminar();
	}

	public Tabla gettab_servicio_basico() {
		return tab_servicio_basico;
	}

	public void settab_servicio_basico(Tabla tab_servicio_basico) {
		this.tab_servicio_basico = tab_servicio_basico;
	}


}
