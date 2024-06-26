package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_tipo_compra extends Pantalla{

	private Tabla tab_tipo_compra=new Tabla();
	
	
	
	public pre_tipo_compra() {
		tab_tipo_compra.setId("tab_tipo_compra");  
		tab_tipo_compra.setTabla("cont_tipo_compra","ide_cotio", 1);	
		tab_tipo_compra.getColumna("tipo_cotio").setCombo(utilitario.getListaTipoProcesoContracion(false));
		tab_tipo_compra.getColumna("tipo_cotio").setNombreVisual("¿Defina si es un Bien o Servicio?");
		tab_tipo_compra.dibujar();
		PanelTabla pat_tipo_compra=new PanelTabla();
		pat_tipo_compra.setPanelTabla(tab_tipo_compra);
		
		agregarComponente(pat_tipo_compra);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_compra.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_compra.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_compra.eliminar();
	}

	public Tabla gettab_tipo_compra() {
		return tab_tipo_compra;
	}

	public void settab_tipo_compra(Tabla tab_tipo_compra) {
		this.tab_tipo_compra = tab_tipo_compra;
	}


}
