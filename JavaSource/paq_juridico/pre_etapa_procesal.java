package paq_juridico;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_etapa_procesal extends Pantalla {

	private Tabla tab_etapa_procesal = new Tabla();

	public pre_etapa_procesal() {
		// TODO Auto-generated constructor stub
		tab_etapa_procesal.setId("tab_etapa_procesal");
		tab_etapa_procesal.setTabla("jur_etapa_procesal", "ide_juepr", 1);
		tab_etapa_procesal.getColumna("activo_juepr").setValorDefecto("true");
		tab_etapa_procesal.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_etapa_procesal);
        Division div_division = new Division();

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_etapa_procesal.isFocus()) {
			tab_etapa_procesal.insertar();
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_etapa_procesal.guardar()) {
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_etapa_procesal.isFocus()) {
			tab_etapa_procesal.eliminar();

		}
	}

	public Tabla getTab_etapa_procesal() {
		return tab_etapa_procesal;
	}

	public void setTab_etapa_procesal(Tabla tab_etapa_procesal) {
		this.tab_etapa_procesal = tab_etapa_procesal;
	}

	
	
}
