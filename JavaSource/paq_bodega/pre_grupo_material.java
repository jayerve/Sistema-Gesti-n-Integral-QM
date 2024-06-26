package paq_bodega;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_grupo_material extends Pantalla{
	
private Tabla tab_grupo_material=new Tabla();
private Tabla tab_cont_asiento=new Tabla();

@EJB
private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	
	public pre_grupo_material(){
		tab_grupo_material.setId("tab_grupo_material");
		tab_grupo_material.setHeader("GRUPO MATERIAL");
		tab_grupo_material.setTabla("bodt_grupo_material","ide_bogrm", 1);
		tab_grupo_material.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_grupo_material.agregarRelacion(tab_cont_asiento);
		tab_grupo_material.dibujar();
		PanelTabla pat_grupo_material=new PanelTabla();
		pat_grupo_material.setPanelTabla(tab_grupo_material);
		
		agregarComponente(pat_grupo_material);
		tab_cont_asiento.setId("tab_cont_asiento");
		tab_cont_asiento.setHeader("ASIENTO CONTABLE");
		tab_cont_asiento.setTabla("cont_asiento_tipo","ide_coast", 2);
		tab_cont_asiento.getColumna("ide_gemod").setCombo("gen_modulo","ide_gemod","detalle_gemod","");
		tab_cont_asiento.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true"));
		tab_cont_asiento.getColumna("ide_cocac").setAutoCompletar();
		tab_cont_asiento.getColumna("ide_bogrm").setCombo("bodt_grupo_material","ide_bogrm","detalle_bogrm","");
		tab_cont_asiento.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_cont_asiento.dibujar();
		PanelTabla pat_asiento=new PanelTabla();
		pat_asiento.setPanelTabla(tab_cont_asiento);
		
		Division div_division = new Division();
		div_division.dividir2(pat_grupo_material,pat_asiento, "50%", "H");
		agregarComponente(div_division);
			
		
	}


	public Tabla getTab_cont_asiento() {
		return tab_cont_asiento;
	}


	public void setTab_cont_asiento(Tabla tab_cont_asiento) {
		this.tab_cont_asiento = tab_cont_asiento;
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}
	

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_grupo_material.guardar()){
			tab_cont_asiento.guardar();
		}
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
	}


	public Tabla getTab_grupo_material() {
		return tab_grupo_material;
	}


	public void setTab_grupo_material(Tabla tab_grupo_material) {
		this.tab_grupo_material = tab_grupo_material;
	}

}
