package paq_sumillas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_est_tramite extends Pantalla{

	private Tabla tab_est_tramite = new Tabla();
	

	public pre_est_tramite(){
		tab_est_tramite.setId("tab_est_tramite");
		tab_est_tramite.setHeader("ESTADOS DEL TRÁMITE");
		tab_est_tramite.setTabla("sum_estado_tramite", "ide_suest", 1);
		//tab_est_tramite.setTipoFormulario(true);
		//tab_est_tramite.getGrid().setColumns(2);
		
		tab_est_tramite.getColumna("ide_suest");
		tab_est_tramite.getColumna("ide_suest").setNombreVisual("Código");
		tab_est_tramite.getColumna("ide_suest").setOrden(1);
		
		tab_est_tramite.getColumna("nombre_suest");
		tab_est_tramite.getColumna("nombre_suest").setNombreVisual("Nombre");
		tab_est_tramite.getColumna("nombre_suest").setOrden(2);
		
		tab_est_tramite.getColumna("activo_suest").setNombreVisual("Activo");
		tab_est_tramite.getColumna("activo_suest").setValorDefecto("true");
		tab_est_tramite.getColumna("activo_suest").setOrden(3);
		tab_est_tramite.dibujar();
		
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_est_tramite);    
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
		if(tab_est_tramite.guardar()){   
			 guardarPantalla();
		 } 
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	
	
	public Tabla gettab_est_tramite() {
		return tab_est_tramite;
	}
	public void settab_est_tramite(Tabla tab_est_tramite) {
		this.tab_est_tramite = tab_est_tramite;
	}
	
}
