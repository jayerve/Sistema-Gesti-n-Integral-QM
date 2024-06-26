package paq_sumillas;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_est_sumilla extends Pantalla{

	private Tabla tab_est_sumilla = new Tabla();
	

	public pre_est_sumilla(){
		tab_est_sumilla.setId("tab_est_sumilla");
		tab_est_sumilla.setHeader("ESTADOS DE SUMILLA");
		tab_est_sumilla.setTabla("sum_estado_sumilla", "ide_suesu", 1);
		//tab_est_sumilla.setTipoFormulario(true);
		//tab_est_sumilla.getGrid().setColumns(2);
		
		tab_est_sumilla.getColumna("ide_suesu").setNombreVisual("Código");
		tab_est_sumilla.getColumna("ide_suesu").setOrden(1);
		
		tab_est_sumilla.getColumna("nombre_suesu").setNombreVisual("Nombre");
		tab_est_sumilla.getColumna("nombre_suesu").setOrden(2);
		
		tab_est_sumilla.getColumna("activo_suesu").setNombreVisual("Activo");
		tab_est_sumilla.getColumna("activo_suesu").setValorDefecto("true");
		tab_est_sumilla.getColumna("activo_suesu").setOrden(3);
		tab_est_sumilla.dibujar();
		
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_est_sumilla);    
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
		if(tab_est_sumilla.guardar()){   
			 guardarPantalla();
		 } 
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	public Tabla gettab_est_sumilla() {
		return tab_est_sumilla;
	}
	public void settab_est_sumilla(Tabla tab_est_sumilla) {
		this.tab_est_sumilla = tab_est_sumilla;
	}
	
}
