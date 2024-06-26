package paq_sri;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_sustento_comprobante extends Pantalla {

	private Tabla tab_sustentoComp=new Tabla();
	
	public pre_sustento_comprobante(){
		
		tab_sustentoComp.setId("tab_sustentoComp");
		tab_sustentoComp.setTabla("sri_sustento_comprobante", "ide_srsuc",1);
		tab_sustentoComp.dibujar();
		
		PanelTabla pat_tab1=new PanelTabla();
		pat_tab1.setPanelTabla(tab_sustentoComp);
		
		Division div=new Division();
		div.dividir1(pat_tab1);
		
		agregarComponente(div);
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_sustentoComp() {
		return tab_sustentoComp;
	}

	public void setTab_sustentoComp(Tabla tab_sustentoComp) {
		this.tab_sustentoComp = tab_sustentoComp;
	}


	
}
