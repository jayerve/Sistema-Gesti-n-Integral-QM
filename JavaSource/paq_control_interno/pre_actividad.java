package paq_control_interno;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_actividad extends 	Pantalla{
	
	private Tabla tab_actividad = new Tabla();
	private Tabla tab_detalle_actividad=new Tabla();

	public pre_actividad() {
		// TODO Auto-generated constructor stub
		tab_actividad.setId("tab_actividad");
		tab_actividad.setTabla("crt_actividad", "ide_cra", 1);
		//tab_actividad.getColumna("ide_crd").setCombo("crt_detalle_actividad", "ide_crd", "detalle_crd", "");
		tab_actividad.getColumna("ide_crf").setCombo("crt_funcionario", "ide_crf", "nombre_crf", "");
		tab_actividad.getColumna("ide_crf").setLongitud(150);
		tab_actividad.getColumna("ide_crf").setAutoCompletar();
		tab_actividad.getColumna("ide_opci").setCombo("sis_opcion", "ide_opci", "nom_opci", "");
		tab_actividad.getColumna("ide_opci").setLongitud(150);
		tab_actividad.getColumna("ide_opci").setAutoCompletar();
		tab_actividad.getColumna("ide_repo").setCombo("sis_reporte", "ide_repo", "nom_repo", "");
		tab_actividad.getColumna("ide_repo").setLongitud(150);
		tab_actividad.getColumna("ide_repo").setAutoCompletar();
		tab_actividad.dibujar();
		tab_actividad.agregarRelacion(tab_detalle_actividad);
		
		PanelTabla pat_actividad=new PanelTabla();
		pat_actividad.setPanelTabla(tab_actividad);
		
		tab_detalle_actividad.setId("tab_detalle_actividad");
		tab_detalle_actividad.setTabla("crt_detalle_actividad", "ide_crd", 2);
		tab_detalle_actividad.setCampoForanea("ide_cra");
		tab_detalle_actividad.dibujar();
			
		PanelTabla pat_detalle_actividad= new PanelTabla();
		pat_detalle_actividad.setMensajeWarn("DETALLE DE ACTIVIDAD");
		pat_detalle_actividad.setPanelTabla(tab_detalle_actividad);
		
		
		Division div_division=new Division();
		div_division.dividir2(pat_actividad, pat_detalle_actividad, "50%", "h");
		agregarComponente(div_division);
		
	}

	public Tabla getTab_actividad() {
		return tab_actividad;
	}

	public void setTab_actividad(Tabla tab_actividad) {
		this.tab_actividad = tab_actividad;
	}

	public Tabla getTab_detalle_actividad() {
		return tab_detalle_actividad;
	}

	public void setTab_detalle_actividad(Tabla tab_detalle_actividad) {
		this.tab_detalle_actividad = tab_detalle_actividad;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_actividad.isFocus()){
			tab_actividad.insertar();
			}
		else if(tab_detalle_actividad.isFocus()){
			tab_detalle_actividad.insertar();
			
		}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_actividad.guardar()){
			tab_detalle_actividad.guardar();
		
		}
		
		guardarPantalla();
	
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_actividad.isFocus()){
			tab_actividad.eliminar();
			
			
	}
		else if(tab_actividad.isFocus()){
			tab_actividad.eliminar();

			
		}
			
		}

	
	

}
