package paq_asistencia;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_nivel_jerarquico_asistencia extends Pantalla {
	private Tabla tab_tabla = new Tabla();
	

	public pre_nivel_jerarquico_asistencia() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_NIVEL_JERARQUICO", "IDE_GENIJ", 1);
        tab_tabla.getColumna("ACTIVO_GENIJ").setCheck();
        tab_tabla.getColumna("ACTIVO_GENIJ").setValorDefecto("true");
        tab_tabla.getColumna("DETALLE_GENIJ").setRequerida(true);
        tab_tabla.getColumna("NIVEL_GENIJ").setRequerida(false);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("NIVEL JERARQUICO");
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
	}


	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		utilitario.getConexion().guardarPantalla();
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

}
