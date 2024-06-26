package paq_activos;

import framework.componentes.PanelTabla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;


public class pre_seguro_activo extends Pantalla{
	private Tabla tab_seguro= new Tabla();
	
	public pre_seguro_activo() {
		tab_seguro.setId("tab_seguro");
		tab_seguro.setTabla("afi_seguro", "ide_afseg", 1);
		
		tab_seguro.setTipoFormulario(true);
		tab_seguro.getGrid().setColumns(4);
		
		tab_seguro.getColumna("ide_afseg");
		tab_seguro.getColumna("ide_afseg").setNombreVisual("CÓDIGO");
		tab_seguro.getColumna("ide_afseg").setOrden(1);
		tab_seguro.getColumna("activo_afseg").setValorDefecto("true");
		tab_seguro.getColumna("activo_afseg").setNombreVisual("ACTIVO");
		tab_seguro.getColumna("activo_afseg").setOrden(2);
		tab_seguro.getColumna("numero_poliza_afseg");
		tab_seguro.getColumna("numero_poliza_afseg").setNombreVisual("Número de Póliza");
		tab_seguro.getColumna("numero_poliza_afseg").setOrden(3);
		tab_seguro.getColumna("numero_poliza_afseg").setLongitud(30);
		tab_seguro.getColumna("detalle_afseg");
		tab_seguro.getColumna("detalle_afseg").setNombreVisual("Detalle del bien Asegurado");
		tab_seguro.getColumna("detalle_afseg").setOrden(4);
		tab_seguro.getColumna("fecha_inicio_afseg");
		tab_seguro.getColumna("fecha_inicio_afseg").setNombreVisual("Fecha de Inicio");
		tab_seguro.getColumna("fecha_inicio_afseg").setOrden(5);
		tab_seguro.getColumna("fecha_inicio_afseg").setValorDefecto(utilitario.getFechaActual());
		tab_seguro.getColumna("fecha_vigencia_afseg");
		tab_seguro.getColumna("fecha_vigencia_afseg").setNombreVisual("Fecha de Vigencia");
		tab_seguro.getColumna("fecha_vigencia_afseg").setOrden(6);
		tab_seguro.getColumna("ide_tease");
		tab_seguro.getColumna("ide_tease").setNombreVisual("Aseguradora");
		tab_seguro.getColumna("ide_tease").setCombo("tes_aseguradora", "ide_tease", "detalle_tease", "activo_tease='true'");
		tab_seguro.getColumna("ide_tease").setOrden(7);
		tab_seguro.getColumna("monto_asegurado_afseg");
		tab_seguro.getColumna("monto_asegurado_afseg").setNombreVisual("Monto del bien Asegurado");
		tab_seguro.getColumna("monto_asegurado_afseg").setOrden(8);
		tab_seguro.getColumna("porcentaje_asegurable_afseg");
		tab_seguro.getColumna("porcentaje_asegurable_afseg").setNombreVisual("Tasa");
		tab_seguro.getColumna("porcentaje_asegurable_afseg").setOrden(9);
		tab_seguro.getColumna("prima_neta_afseg");
		tab_seguro.getColumna("prima_neta_afseg").setNombreVisual("Prima Neta");
		tab_seguro.getColumna("prima_neta_afseg").setOrden(10);
		tab_seguro.getColumna("tasa_afseg").setVisible(false);
		tab_seguro.getColumna("observacion_afseg");
		tab_seguro.getColumna("observacion_afseg").setNombreVisual("Observaciones");
		tab_seguro.getColumna("observacion_afseg").setOrden(11);
		tab_seguro.getColumna("ide_coest");
		tab_seguro.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_seguro.getColumna("ide_coest").setNombreVisual("Estado");
		tab_seguro.getColumna("ide_coest").setOrden(12);
		tab_seguro.getColumna("archivo_afseg");
		tab_seguro.getColumna("archivo_afseg").setNombreVisual("Archivo");
		tab_seguro.getColumna("archivo_afseg").setOrden(13);
		tab_seguro.getColumna("archivo_afseg").setUpload("polizas");
		tab_seguro.dibujar();
		PanelTabla pat_seguro = new PanelTabla();
		pat_seguro.setPanelTabla(tab_seguro);
		agregarComponente(pat_seguro);
			
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	tab_seguro.insertar();	
	
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_seguro.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_seguro.eliminar();
	
	}

	public Tabla getTab_seguro() {
		return tab_seguro;
	}

	public void setTab_seguro(Tabla tab_seguro) {
		this.tab_seguro = tab_seguro;
	}
	

}
