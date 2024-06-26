package paq_transporte;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_parametro_mantenimiento extends Pantalla{

	private Tabla tab_parametro_vehiculo=new Tabla();
	
	public pre_parametro_mantenimiento() {
		tab_parametro_vehiculo.setId("tab_parametro_vehiculo");  
		tab_parametro_vehiculo.setTabla("veh_parametro_vehiculo", "ide_vepav", 1);
		tab_parametro_vehiculo.getColumna("ide_vepav").setNombreVisual("CÓDIGO");
		tab_parametro_vehiculo.getColumna("ide_vepav").setOrden(1);
		tab_parametro_vehiculo.getColumna("activo_vepav");
		tab_parametro_vehiculo.getColumna("activo_vepav").setNombreVisual("ACTIVO");
		tab_parametro_vehiculo.getColumna("activo_vepav").setOrden(2);
		tab_parametro_vehiculo.getColumna("activo_vepav").setValorDefecto("TRUE");
		
		tab_parametro_vehiculo.getColumna("detalle_vepav");
		tab_parametro_vehiculo.getColumna("detalle_vepav").setNombreVisual("Variable");
		tab_parametro_vehiculo.getColumna("detalle_vepav").setOrden(3);
		tab_parametro_vehiculo.getColumna("valor_frecuencia_vepav");
		tab_parametro_vehiculo.getColumna("valor_frecuencia_vepav").setNombreVisual("Frecuencia de cambio");
		tab_parametro_vehiculo.getColumna("valor_frecuencia_vepav").setOrden(4);
		tab_parametro_vehiculo.getColumna("valor_alerta_vepav");
		tab_parametro_vehiculo.getColumna("valor_alerta_vepav").setNombreVisual("Aviso de cambio");
		tab_parametro_vehiculo.getColumna("valor_alerta_vepav").setOrden(5);
		
		tab_parametro_vehiculo.getColumna("ide_vetip").setCombo("veh_tipo_vehiculo", "ide_vetip", "detalle_vetip", "");
		tab_parametro_vehiculo.getColumna("ide_vetip").setOrden(6);
		tab_parametro_vehiculo.getColumna("ide_vetip").setNombreVisual("Tipo de vehiculo");
		
		tab_parametro_vehiculo.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_parametro_vehiculo);
		
		agregarComponente(pat_estado);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_parametro_vehiculo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_parametro_vehiculo.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_parametro_vehiculo.eliminar();
	}

	public Tabla gettab_parametro_vehiculo() {
		return tab_parametro_vehiculo;
	}

	public void settab_parametro_vehiculo(Tabla tab_parametro_vehiculo) {
		this.tab_parametro_vehiculo = tab_parametro_vehiculo;
	}


}
