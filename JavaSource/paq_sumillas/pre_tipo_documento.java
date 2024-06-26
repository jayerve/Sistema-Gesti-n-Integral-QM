package paq_sumillas;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_tipo_documento extends Pantalla{

	private Tabla tab_tipo_documento = new Tabla();
	

	public pre_tipo_documento(){
		
		tab_tipo_documento.setId("tab_tipo_documento");
		tab_tipo_documento.setHeader("TIPOS DE DOCUMENTO");
		tab_tipo_documento.setTabla("sum_tipo_documento", "ide_sumtd", 1);
		//tab_tipo_documento.setTipoFormulario(true);
		//tab_tipo_documento.getGrid().setColumns(2);
		
		tab_tipo_documento.getColumna("ide_sumtd");
		tab_tipo_documento.getColumna("ide_sumtd").setNombreVisual("Código");
		tab_tipo_documento.getColumna("ide_sumtd").setOrden(1);
		
		tab_tipo_documento.getColumna("nombre_sumtd");
		tab_tipo_documento.getColumna("nombre_sumtd").setNombreVisual("Nombre");
		tab_tipo_documento.getColumna("nombre_sumtd").setOrden(2);
		
		tab_tipo_documento.getColumna("activo_sumtd").setNombreVisual("Activo");
		tab_tipo_documento.getColumna("activo_sumtd").setValorDefecto("true");
		tab_tipo_documento.getColumna("activo_sumtd").setOrden(3);
		tab_tipo_documento.dibujar();
		
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_tipo_documento);    
		Division div_division = new Division();  
		div_division.dividir1(pat_dat_gen);  
		agregarComponente(div_division); 
	}
	
	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();	
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_tipo_documento.guardar()){   
			 guardarPantalla();
		 } 
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	
	
	public Tabla gettab_tipo_documento() {
		return tab_tipo_documento;
	}
	public void settab_tipo_documento(Tabla tab_tipo_documento) {
		this.tab_tipo_documento = tab_tipo_documento;
	}
	
}
