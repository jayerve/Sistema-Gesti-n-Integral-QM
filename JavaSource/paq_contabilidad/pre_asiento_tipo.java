package paq_contabilidad;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_asiento_tipo extends Pantalla{

	private Tabla tab_estado=new Tabla();
	private Tabla tab_asiento_contable = new Tabla();
	private Tabla tab_reglas_asiento = new Tabla();
	
	   @EJB
	    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_asiento_tipo() {
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_asiento_contable.setId("tab_asiento_contable");
		tab_asiento_contable.setTabla("cont_nombre_asiento_contable", "ide_conac", 1);
		tab_asiento_contable.getColumna("ide_gemod").setCombo("select ide_gemod,detalle_gemod from gen_modulo");
		tab_asiento_contable.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_asiento_contable.getColumna("ide_bogrm").setCombo("select ide_bogrm,detalle_bogrm from bodt_grupo_material order by detalle_bogrm");
		tab_asiento_contable.getColumna("ide_bogrm").setAutoCompletar();
		tab_asiento_contable.agregarRelacion(tab_estado);
		tab_asiento_contable.agregarRelacion(tab_reglas_asiento);
		tab_asiento_contable.dibujar();
		PanelTabla pat_asiento_contable= new PanelTabla();
		pat_asiento_contable.setPanelTabla(tab_asiento_contable);
		

		
		tab_estado.setId("tab_estado");  
		tab_estado.setIdCompleto("tab_tabulador:tab_estado");
		tab_estado.setTabla("cont_asiento_tipo", "ide_coast", 2);	
		tab_estado.getColumna("ide_bogrm").setCombo("select ide_bogrm,detalle_bogrm from bodt_grupo_material order by detalle_bogrm");
		tab_estado.getColumna("ide_bogrm").setAutoCompletar();
		tab_estado.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_estado.getColumna("ide_cocac").setAutoCompletar();
		tab_estado.getColumna("ide_gelua").setCombo("select ide_gelua,detalle_gelua from gen_lugar_aplica");
		tab_estado.getColumna("ide_coest").setCombo("select ide_coest,detalle_coest from cont_estado order by detalle_coest");
		tab_estado.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_estado);
		
		tab_reglas_asiento.setId("tab_reglas_asiento");
		tab_reglas_asiento.setIdCompleto("tab_tabulador:tab_reglas_asiento");
		tab_reglas_asiento.setTabla("cont_reglas_asiento_contable", "ide_corac", 3);
		tab_reglas_asiento.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_reglas_asiento.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario", "ide_prmop", "detalle_prmop", "");
		tab_reglas_asiento.dibujar();
		PanelTabla pat_reglas_asiento = new PanelTabla();
		pat_reglas_asiento.setPanelTabla(tab_reglas_asiento);
		

		tab_tabulador.agregarTab("ASIENTO TIPO",pat_estado);
		tab_tabulador.agregarTab("REGLAS ASIENTO", pat_reglas_asiento);
		
		Division div_division = new Division();
		div_division.dividir2(pat_asiento_contable,tab_tabulador,"50%","H");
		agregarComponente(div_division);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_asiento_contable.guardar()){
			if(tab_reglas_asiento.guardar()){
				if(tab_estado.guardar()){
					guardarPantalla();		
				}
			}
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla gettab_estado() {
		return tab_estado;
	}

	public void settab_estado(Tabla tab_estado) {
		this.tab_estado = tab_estado;
	}

	public Tabla getTab_asiento_contable() {
		return tab_asiento_contable;
	}

	public void setTab_asiento_contable(Tabla tab_asiento_contable) {
		this.tab_asiento_contable = tab_asiento_contable;
	}

	public Tabla getTab_reglas_asiento() {
		return tab_reglas_asiento;
	}

	public void setTab_reglas_asiento(Tabla tab_reglas_asiento) {
		this.tab_reglas_asiento = tab_reglas_asiento;
	}


}
