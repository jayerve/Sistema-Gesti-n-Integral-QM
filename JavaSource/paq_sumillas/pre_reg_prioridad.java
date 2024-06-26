package paq_sumillas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_reg_prioridad extends Pantalla{

	private Tabla tab_reg_prioridad = new Tabla();
	

	public pre_reg_prioridad(){
		tab_reg_prioridad.setId("tab_reg_prioridad");
		tab_reg_prioridad.setHeader("REGISTRO DE PRIORIDAD");
		tab_reg_prioridad.setTabla("sum_prioridad", "ide_sumpr", 1);
		//tab_reg_prioridad.setTipoFormulario(true);
		//tab_reg_prioridad.getGrid().setColumns(4);
		
		tab_reg_prioridad.getColumna("ide_sumpr").setNombreVisual("Código");
		tab_reg_prioridad.getColumna("ide_sumpr").setOrden(1);
		tab_reg_prioridad.getColumna("ide_sumpr").setLectura(true);
		
		tab_reg_prioridad.getColumna("nombre_sumpr").setNombreVisual("Nombre");
		tab_reg_prioridad.getColumna("nombre_sumpr").setOrden(2);
		
		tab_reg_prioridad.getColumna("dias_sumpr").setNombreVisual("Días");
		tab_reg_prioridad.getColumna("dias_sumpr").setOrden(3);
		
		tab_reg_prioridad.getColumna("activo_sumpr").setNombreVisual("Activo");
		tab_reg_prioridad.getColumna("activo_sumpr").setValorDefecto("true");
		tab_reg_prioridad.getColumna("activo_sumpr").setOrden(4);
		
		tab_reg_prioridad.dibujar();
		
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_reg_prioridad );    
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
		if(tab_reg_prioridad .guardar())
		 {   
			 guardarPantalla();			
		 } 
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	
	
	public Tabla gettab_reg_prioridad () {
		return tab_reg_prioridad ;
	}
	public void settab_reg_prioridad (Tabla tab_reg_prioridad ) {
		this.tab_reg_prioridad  = tab_reg_prioridad ;
	}
	
}
