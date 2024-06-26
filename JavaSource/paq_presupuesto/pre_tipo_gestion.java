package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_gestion extends Pantalla {
	
	private Tabla tab_tipo_gestion=new Tabla();
	
	
	public pre_tipo_gestion (){
		tab_tipo_gestion.setId("tab_tipo_gestion");
		tab_tipo_gestion.setTabla("pre_tipo_gestion", "ide_prtge", 1);
		tab_tipo_gestion.setNumeroTabla(1);
		tab_tipo_gestion.dibujar();
		PanelTabla pat_grupo_economico=new PanelTabla();
		pat_grupo_economico.setPanelTabla(tab_tipo_gestion);
		agregarComponente(pat_grupo_economico);
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_gestion.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_gestion.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_gestion.eliminar();
	}


	public Tabla getTab_tipo_gestion() {
		return tab_tipo_gestion;
	}


	public void setTab_tipo_gestion(Tabla tab_tipo_gestion) {
		this.tab_tipo_gestion = tab_tipo_gestion;
	}


	

}
