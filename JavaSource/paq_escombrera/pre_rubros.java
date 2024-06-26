package paq_escombrera;

import javax.ejb.EJB;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_escombrera.ejb.ServicioEscombrera;
import paq_sistema.aplicacion.Pantalla;

public class pre_rubros extends Pantalla {

	private Tabla tab_item_padre = new Tabla();
	private Tabla tab_tarifas=new Tabla();
	private Division div_division = new Division();
	private Division div_tarifa= new Division();
	private Arbol arb_rubros = new Arbol();

	@EJB
	private ServicioEscombrera ser_escombrera = (ServicioEscombrera) utilitario.instanciarEJB(ServicioEscombrera.class);

	public pre_rubros() {

		tab_item_padre.setId("tab_item_padre");
		tab_item_padre.setHeader("ITEM DEL RUBRO SELECCIONADO");
		tab_item_padre.setTipoFormulario(true); // formulario
		tab_item_padre.getGrid().setColumns(4); // hacer columnas
		tab_item_padre.setTabla("apu_rubro", "ide_aprub", 2);
		tab_item_padre.getColumna("nivel_aprub").setCombo(utilitario.getListaGruposNivelCuenta());
		tab_item_padre.getColumna("activo_aprub").setValorDefecto("true");
		tab_item_padre.agregarRelacion(tab_tarifas);
		tab_item_padre.setCampoPadre("con_ide_aprub"); //necesarios para el arbol ide recursivo
		tab_item_padre.setCampoNombre("(select coalesce(cat_codigo_aprub,'')||' '|| coalesce(descripcion_aprub,'') as descripcion_aprub from apu_rubro b where b.ide_aprub=apu_rubro.ide_aprub)"); //necesarios para el arbol campo a mostrarse
		tab_item_padre.agregarArbol(arb_rubros);//necesarios para el arbol
		tab_item_padre.dibujar();
		
		PanelTabla pat_tab_item_padre = new PanelTabla();
		pat_tab_item_padre.setPanelTabla(tab_item_padre);

		tab_tarifas.setId("tab_tarifas");
		tab_tarifas.setHeader("TARIFAS");
		tab_tarifas.setTabla("apu_rubro_tarifa", "ide_aprut", 3);
		tab_tarifas.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_tarifas.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_tarifas.getColumna("ide_geani").setValorDefecto("12");
		tab_tarifas.getColumna("ide_bounm").setValorDefecto("1");
		tab_tarifas.getColumna("valor_aprut").setValorDefecto("0.00");
		tab_tarifas.getColumna("activo_aprut").setValorDefecto("true");
		tab_tarifas.dibujar();
		PanelTabla pat_tarifas = new PanelTabla();
		pat_tarifas.setPanelTabla(tab_tarifas); 
	    
		div_tarifa =new Division();
	    div_tarifa.setId("div_tarifa");
	    div_tarifa.dividir2(pat_tab_item_padre,pat_tarifas,"50%", "h");
		agregarComponente(div_tarifa);

		arb_rubros.setId("arb_rubros");
		arb_rubros.onSelect("seleccionar_arbol");
		arb_rubros.dibujar();

		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_rubros, div_tarifa, "25%", "v");
		agregarComponente(div_division);

	}

	public void seleccionar_arbol(NodeSelectEvent evt) 
	{
		arb_rubros.seleccionarNodo(evt);
		tab_item_padre.limpiar();
		tab_tarifas.limpiar();
		//System.out.println("arb_rubros.getValorSeleccionado() " + arb_rubros.getValorSeleccionado());
		tab_item_padre.ejecutarValorPadre(arb_rubros.getValorSeleccionado());
		tab_tarifas.ejecutarValorForanea(tab_item_padre.getValorSeleccionado());
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
		if(tab_item_padre.isFocus())
		{
			tab_item_padre.setValor("cat_codigo_aprub", ser_escombrera.getCodigoRubro(arb_rubros.getValorSeleccionado()).getValor("cod"));
			utilitario.addUpdate("tab_item_padre");
		}
		
		if(tab_tarifas.isFocus())
		{
			tab_tarifas.setValor("ide_aprub", tab_item_padre.getValor("ide_aprub"));
		}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_item_padre.guardar();
		tab_tarifas.guardar();
		guardarPantalla();
		arb_rubros.ejecutarSql();
		utilitario.addUpdate("arb_rubros");
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

	public Arbol getArb_rubros() {
		return arb_rubros;
	}


	public void setArb_rubros(Arbol arb_rubros) {
		this.arb_rubros = arb_rubros;
	}


	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}


	public Tabla getTab_tarifas() {
		return tab_tarifas;
	}


	public void setTab_tarifas(Tabla tab_tarifas) {
		this.tab_tarifas = tab_tarifas;
	}


	public Division getDiv_tarifa() {
		return div_tarifa;
	}


	public void setDiv_tarifa(Division div_tarifa) {
		this.div_tarifa = div_tarifa;
	}

	
	

}
