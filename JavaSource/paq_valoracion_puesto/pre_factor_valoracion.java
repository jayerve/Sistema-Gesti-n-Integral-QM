package paq_valoracion_puesto;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

public class pre_factor_valoracion extends Pantalla {
	
	private Tabla tab_factor =new Tabla();
	private Tabla tab_valora=new Tabla();
	private Tabla tab_grupo=new Tabla();
	
	
	public pre_factor_valoracion (){
		Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
		tab_factor.setId("tab_factor");
		tab_factor.setHeader("FACTOR VALORACIÓN PUESTO");
		tab_factor.setTabla("gth_factor_valoracion", "ide_gtfav", 1);
		tab_factor.getColumna("activo_gtfav").setValorDefecto("true");
		tab_factor.agregarRelacion(tab_grupo);
		tab_factor.agregarRelacion(tab_valora);
		tab_factor.dibujar();
		PanelTabla pat_factor=new PanelTabla();
		pat_factor.setPanelTabla(tab_factor);
		///valora grupo 
		tab_valora.setId("tab_valora");
		tab_valora.setIdCompleto("tab_tabulador:tab_valora");
		tab_valora.setTabla("gth_valora_grupo", "ide_gtvag", 2);
		tab_valora.getColumna("activo_gtvag").setValorDefecto("true");
		tab_valora.dibujar();
		PanelTabla pat_valora=new PanelTabla();
		pat_valora.setPanelTabla(tab_valora);
		
		///valora grupo 
		tab_grupo.setId("tab_grupo");
		tab_grupo.setIdCompleto("tab_tabulador:tab_grupo");
		tab_grupo.setTabla("gth_grupo_valora", "ide_gtgrv", 3);
		tab_grupo.getColumna("ide_gegro").setCombo("gen_grupo_ocupacional", "ide_gegro", "detalle_gegro", "");
		tab_grupo.getColumna("activo_gtgrv").setValorDefecto("true");
		tab_grupo.dibujar();
		PanelTabla pat_grupo=new PanelTabla();
		pat_grupo.setPanelTabla(tab_grupo);
				
		
	
		tab_Tabulador.agregarTab("DETALLE VALORACIÓN", pat_valora);
		tab_Tabulador.agregarTab("GRUPO OCUPACIONAL", pat_grupo);
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_factor, tab_Tabulador, "50%", "H");
		agregarComponente(div_divi);
		
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_factor.guardar()){
			if(tab_valora.guardar()){
				if(tab_grupo.guardar()){
				}
			}
		}
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_factor() {
		return tab_factor;
	}

	public void setTab_factor(Tabla tab_factor) {
		this.tab_factor = tab_factor;
	}

	public Tabla getTab_valora() {
		return tab_valora;
	}

	public void setTab_valora(Tabla tab_valora) {
		this.tab_valora = tab_valora;
	}

	public Tabla getTab_grupo() {
		return tab_grupo;
	}

	public void setTab_grupo(Tabla tab_grupo) {
		this.tab_grupo = tab_grupo;
	}
	

}
