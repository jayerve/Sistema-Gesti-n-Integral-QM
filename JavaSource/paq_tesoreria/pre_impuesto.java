package paq_tesoreria;

import javax.ejb.EJB;
import javax.sound.midi.Patch;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_impuesto extends Pantalla {
	
	private Tabla tab_impuesto =new Tabla();
	private Tabla tab_cont_asiento=new Tabla();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	
	public pre_impuesto(){
		tab_impuesto.setId("tab_impuesto");
		tab_impuesto.setHeader("IMPUESTO");
		tab_impuesto.setTabla("tes_impuesto", "ide_teimp", 1);
		tab_impuesto.getColumna("ide_tetii").setCombo("tes_tipo_impuesto", "ide_tetii", "detalle_tetii", "");
		tab_impuesto.agregarRelacion(tab_cont_asiento);

		tab_impuesto.dibujar();
		PanelTabla pat_panel =new PanelTabla();
		pat_panel.setPanelTabla(tab_impuesto);
		
		tab_cont_asiento.setId("tab_cont_asiento");
		tab_cont_asiento.setHeader("ASIENTO CONTABLE");
		tab_cont_asiento.setTabla("tes_asiento_tipo","ide_teast", 2);
		tab_cont_asiento.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true"));
		tab_cont_asiento.getColumna("ide_cocac").setAutoCompletar();
		tab_cont_asiento.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_cont_asiento.dibujar();
		PanelTabla pat_asiento=new PanelTabla();
		pat_asiento.setPanelTabla(tab_cont_asiento);
		
		Division div_division = new Division();
		div_division.dividir2(pat_panel,pat_asiento, "50%", "H");
		agregarComponente(div_division);	}

	@Override
	public void insertar() {
		if(tab_impuesto.isFocus()){
		tab_impuesto.insertar();
		}
		if(tab_cont_asiento.isFocus()){
			tab_cont_asiento.insertar();
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_impuesto.guardar();
		tab_cont_asiento.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_impuesto.eliminar();
	}

	public Tabla getTab_impuesto() {
		return tab_impuesto;
	}

	public void setTab_impuesto(Tabla tab_impuesto) {
		this.tab_impuesto = tab_impuesto;
	}

	public Tabla getTab_cont_asiento() {
		return tab_cont_asiento;
	}

	public void setTab_cont_asiento(Tabla tab_cont_asiento) {
		this.tab_cont_asiento = tab_cont_asiento;
	}

}
