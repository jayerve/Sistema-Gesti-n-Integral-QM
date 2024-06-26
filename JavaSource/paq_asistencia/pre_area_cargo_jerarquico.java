package paq_asistencia;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_area_cargo_jerarquico extends Pantalla {
	private Tabla tab_tabla = new Tabla();	
	private Arbol arb_arbol = new Arbol();

	public pre_area_cargo_jerarquico() {

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("gen_area_cargo_jerarquia", "ide_geacj", 1);
		tab_tabla.setCampoNombre("(select DETALLE_GECAF from gen_cargo_funcional a where a.ide_gecaf = gen_area_cargo_jerarquia.ide_gecaf)"); // Para que se configure el arbol
		tab_tabla.setCampoPadre("GEN_IDE_GEACJ"); // Para que se configure el arbol
		tab_tabla.agregarArbol(arb_arbol); // Para que se configure el arbol
		tab_tabla.getColumna("IDE_GENIJ").setCombo( "gen_nivel_jerarquico", "IDE_GENIJ", "DETALLE_GENIJ", "");
		tab_tabla.getColumna("IDE_GECAF").setCombo( "GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_tabla.getColumna("IDE_GEARE").setCombo( "GEN_AREA", "IDE_GEARE", "DETALLE_GEARE", "");

		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);
		arb_arbol.setId("arb_arbol");
		arb_arbol.dibujar();
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_arbol, pat_panel, "21%", "V");
		agregarComponente(div_division);
	}

	public void seleccionar_arbol(NodeSelectEvent evt) {
		arb_arbol.seleccionarNodo(evt);
		tab_tabla.setValorPadre(arb_arbol.getValorSeleccionado());
		tab_tabla.ejecutarSql();
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		utilitario.getConexion().guardarPantalla();
		//tab_tabla.ejecutarSql();		
		//tab_permiso_justificacion.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
		
		arb_arbol.ejecutarSql();
		utilitario.addUpdate("arb_arbol");

	}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();

	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}
}
