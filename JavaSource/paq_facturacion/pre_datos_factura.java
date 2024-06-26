package paq_facturacion;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_datos_factura extends Pantalla{
 	
private Tabla tab_datos_factura=new Tabla();

public pre_datos_factura (){
	tab_datos_factura.setId("tab_datos_factura");
	tab_datos_factura.setTabla("fac_datos_factura", "ide_fadaf", 1);
	tab_datos_factura.getColumna("ide_sucu").setVisible(false);
	tab_datos_factura.getColumna("ide_gedep").setVisible(false);
	tab_datos_factura.getColumna("ide_bogrm").setCombo("select ide_bogrm,detalle_bogrm,autorizacion_sri_bogrm from bodt_grupo_material order by detalle_bogrm");		
	tab_datos_factura.setTipoFormulario(true);
	tab_datos_factura.getGrid().setColumns(4);
	tab_datos_factura.dibujar();
	PanelTabla pat_datos=new PanelTabla();
	pat_datos.setPanelTabla(tab_datos_factura);
	agregarComponente(pat_datos);
	
}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_datos_factura.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_datos_factura.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_datos_factura.eliminar();
		
	}
	public Tabla getTab_datos_factura() {
		return tab_datos_factura;
	}
	public void setTab_datos_factura(Tabla tab_datos_factura) {
		this.tab_datos_factura = tab_datos_factura;
	}
	
}