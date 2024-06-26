package paq_precontractual;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class precon_fase extends Pantalla{
	private Tabla tab_fase=new Tabla();
	
	public precon_fase(){
		tab_fase.setId("tab_fase");
		tab_fase.setHeader("FASE");
		tab_fase.setTabla("precon_fase","ide_prfas",1);
		
		//tab_fase.setTipoFormulario(true);  //formulario 
		//tab_fase.getGrid().setColumns(2); //hacer  columnas
		tab_fase.getColumna("ide_prfas");
		tab_fase.getColumna("ide_prfas").setNombreVisual("Código");
		tab_fase.getColumna("descripcion_prfas");
		tab_fase.getColumna("descripcion_prfas").setNombreVisual("Nombre");
		tab_fase.getColumna("activo_prfas");
		tab_fase.getColumna("activo_prfas").setNombreVisual("ACTIVO");
		tab_fase.getColumna("activo_prfas").setValorDefecto("true");
		
		tab_fase.dibujar();
        PanelTabla pat_fase=new PanelTabla();
		pat_fase.setPanelTabla(tab_fase);
		

		Division div_division = new Division();
		div_division.dividir1(pat_fase);
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_fase.guardar()){
			guardarPantalla();
		}
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_fase() {
		return tab_fase;
	}

	public void setTab_fase(Tabla tab_fase) {
		this.tab_fase = tab_fase;
	}
}
