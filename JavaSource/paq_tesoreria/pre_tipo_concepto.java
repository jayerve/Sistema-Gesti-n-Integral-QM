package paq_tesoreria;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_concepto extends Pantalla{
	
	private Tabla tab_tipo_documento=new Tabla ();
	public pre_tipo_concepto (){
		
		tab_tipo_documento.setId("tab_tipo_documento");
		tab_tipo_documento.setNumeroTabla(1);
		tab_tipo_documento.setTabla("tes_tipo_concepto", "ide_tetic", 1);
		tab_tipo_documento.dibujar();
		PanelTabla pat_tipo_docuemto=new PanelTabla(); 
		pat_tipo_docuemto.setPanelTabla(tab_tipo_documento);
		
		agregarComponente(pat_tipo_docuemto);
		
		
	}
	
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_documento.insertar();
		
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_documento.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_documento.eliminar();
		
	}



	public Tabla getTab_tipo_documento() {
		return tab_tipo_documento;
	}



	public void setTab_tipo_documento(Tabla tab_tipo_documento) {
		this.tab_tipo_documento = tab_tipo_documento;
	}

}
