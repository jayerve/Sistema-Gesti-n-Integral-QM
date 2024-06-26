package paq_activos;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_ubicacion  extends Pantalla{


	private Tabla tab_ubicaca=new Tabla();

	private Arbol arb_ubica=new Arbol();


	public pre_ubicacion(){



		tab_ubicaca.setId("tab_ubicaca");
		tab_ubicaca.setTabla("afi_ubicacion", "ide_afubi", 1);
		
		tab_ubicaca.setCampoPadre("afi_ide_afubi"); //necesarios para el arbol
		tab_ubicaca.setCampoNombre("detalle_afubi"); //necesarios para el arbol
		tab_ubicaca.agregarArbol(arb_ubica);//necesarios para el arbol
		
		tab_ubicaca.getColumna("ide_aftid").setCombo("afi_tipo_dependencia", "ide_aftid", "detalle_aftid", "");
		
		
		tab_ubicaca.dibujar();
		PanelTabla pat_ubica = new PanelTabla();
		pat_ubica.setPanelTabla(tab_ubicaca);

		arb_ubica.setId("arb_ubica");
		arb_ubica.dibujar();


		Division div_division=new Division();
		div_division.dividir2(arb_ubica, pat_ubica, "25%", "v");

		agregarComponente(div_division);

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_ubicaca.insertar();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_ubicaca.guardar();
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_ubicaca.eliminar();
	}

	public Tabla getTab_ubicaca() {
		return tab_ubicaca;
	}

	public void setTab_ubicaca(Tabla tab_ubicaca) {
		this.tab_ubicaca = tab_ubicaca;
	}

	public Arbol getArb_ubica() {
		return arb_ubica;
	}

	public void setArb_ubica(Arbol arb_ubica) {
		this.arb_ubica = arb_ubica;
	}



}
