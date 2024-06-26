package paq_precontractual;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class precon_contratacion extends Pantalla{
	private Tabla tab_precontractual=new Tabla();
	
	public precon_contratacion(){
		tab_precontractual.setId("tab_precontractual");
		tab_precontractual.setHeader("CONTRATACIÓN");
		tab_precontractual.setTabla("precon_contratacion","ide_prcon",1);
		
		//tab_precontractual.setTipoFormulario(true);  //formulario 
		//tab_precontractual.getGrid().setColumns(2); //hacer  columnas
		tab_precontractual.getColumna("ide_prcon");
		tab_precontractual.getColumna("ide_prcon").setNombreVisual("Código");
		tab_precontractual.getColumna("descripcion_prcon");
		tab_precontractual.getColumna("descripcion_prcon").setNombreVisual("Nombre");
		tab_precontractual.getColumna("activo_prcon");
		tab_precontractual.getColumna("activo_prcon").setNombreVisual("ACTIVO");
		tab_precontractual.getColumna("activo_prcon").setValorDefecto("true");
		
		tab_precontractual.dibujar();
        PanelTabla pat_pctr=new PanelTabla();
		pat_pctr.setPanelTabla(tab_precontractual);
		

		Division div_division = new Division();
		div_division.dividir1(pat_pctr);
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
		if(tab_precontractual.guardar()){
			guardarPantalla();
		}
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_precontractual() {
		return tab_precontractual;
	}

	public void setTab_precontractual(Tabla tab_precontractual) {
		this.tab_precontractual = tab_precontractual;
	}
}
