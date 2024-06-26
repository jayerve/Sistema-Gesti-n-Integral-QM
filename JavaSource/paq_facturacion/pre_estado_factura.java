package paq_facturacion;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_estado_factura extends Pantalla{

	private Tabla tab_cont_estado=new Tabla();


	public pre_estado_factura(){
		tab_cont_estado.setHeader("FACTURACÓN");
		tab_cont_estado.setId("tab_cont_estado");
		tab_cont_estado.setTabla("cont_estado","ide_coest", 1);
		
		tab_cont_estado.dibujar();
		PanelTabla pat_estado_factura=new PanelTabla();
		pat_estado_factura.setPanelTabla(tab_cont_estado);

		agregarComponente(pat_estado_factura);
		
	}


	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cont_estado.insertar();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_cont_estado.guardar();
		guardarPantalla();
		

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cont_estado.eliminar();

	}


	public Tabla getTab_estado_factura() {
		return tab_cont_estado;
	}


	public void setTab_estado_factura(Tabla tab_estado_factura) {
		this.tab_cont_estado = tab_estado_factura;
	}

}
