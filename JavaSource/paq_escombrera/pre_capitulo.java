package paq_escombrera;

import javax.ejb.EJB;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_escombrera.ejb.ServicioEscombrera;
import paq_sistema.aplicacion.Pantalla;

public class pre_capitulo extends Pantalla 
{
	private Tabla tab_item_padre = new Tabla();
	private Division div_division = new Division();
	private Arbol arb_capitulos = new Arbol();

	@EJB
	private ServicioEscombrera ser_escombrera = (ServicioEscombrera) utilitario.instanciarEJB(ServicioEscombrera.class);

	public pre_capitulo() {

		tab_item_padre.setId("tab_item_padre");
		tab_item_padre.setHeader("ITEM DEL CAPITULO SELECCIONADO");
		tab_item_padre.setTipoFormulario(true); // formulario
		tab_item_padre.getGrid().setColumns(4); // hacer columnas
		tab_item_padre.setTabla("apu_capitulo", "ide_apcap", 1);
		tab_item_padre.getColumna("nivel_apcap").setCombo(utilitario.getListaGruposNivelCuenta());
		tab_item_padre.getColumna("activo_apcap").setValorDefecto("true");
		tab_item_padre.setCampoPadre("con_ide_apcap"); //necesarios para el arbol ide recursivo
		tab_item_padre.setCampoNombre("(select coalesce(cat_codigo_apcap,'')||' '|| coalesce(descripcion_apcap,'') as descripcion_apcap from apu_capitulo b where b.ide_apcap=apu_capitulo.ide_apcap)"); //necesarios para el arbol campo a mostrarse
		tab_item_padre.agregarArbol(arb_capitulos);//necesarios para el arbol
		tab_item_padre.dibujar();
		
		PanelTabla pat_tab_item_padre = new PanelTabla();
		pat_tab_item_padre.setPanelTabla(tab_item_padre);


		arb_capitulos.setId("arb_capitulos");
		arb_capitulos.dibujar();

		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_capitulos, pat_tab_item_padre, "25%", "v");
		agregarComponente(div_division);

	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		if(tab_item_padre.isFocus())
		{
			tab_item_padre.setValor("cat_codigo_apcap", ser_escombrera.getCodigoCapitulos(arb_capitulos.getValorSeleccionado()).getValor("cod"));
			utilitario.addUpdate("tab_item_padre");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_item_padre.guardar();
		guardarPantalla();
		arb_capitulos.ejecutarSql();
		utilitario.addUpdate("arb_capitulos");
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	
	public Tabla getTab_item_padre() {
		return tab_item_padre;
	}

	public void setTab_item_padre(Tabla tab_item_padre) {
		this.tab_item_padre = tab_item_padre;
	}

	public Arbol getArb_capitulos() {
		return arb_capitulos;
	}

	public void setArb_capitulos(Arbol arb_capitulos) {
		this.arb_capitulos = arb_capitulos;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	
	

}
