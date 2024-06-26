package paq_tesoreria;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_caja extends Pantalla{
	private Tabla tab_caja = new Tabla();

	public pre_caja(){
		
		tab_caja.setId("tab_caja");
		tab_caja.setHeader("CAJAS DE RECAUDACIÓN");
		tab_caja.setTabla("tes_caja", "ide_tecaj", 1);
		tab_caja.setMostrarcampoSucursal(true);
		tab_caja.getColumna("ide_sucu").setCombo("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by nom_sucu");
		tab_caja.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_caja);
		//Division div_tabla = new Division();
		//div_tabla.dividir1(pat_panel);
		agregarComponente(pat_panel);
		
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_caja.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_caja.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_caja.eliminar();

	}


	public Tabla getTab_caja() {
		return tab_caja;
	}


	public void setTab_caja(Tabla tab_caja) {
		this.tab_caja = tab_caja;
	}

	

}