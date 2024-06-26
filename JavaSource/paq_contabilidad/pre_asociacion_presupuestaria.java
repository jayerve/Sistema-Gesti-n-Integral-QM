package paq_contabilidad;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_asociacion_presupuestaria extends Pantalla {

	private Tabla tab_asociacion_presupuestaria =new Tabla();
	private Tabla tab_vigente =new Tabla();
	private Tabla tab_tipo_catalogo_cuenta = new Tabla();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);



	public pre_asociacion_presupuestaria (){
		
		tab_asociacion_presupuestaria.setId("tab_asociacion_presupuestaria");
		tab_asociacion_presupuestaria.setHeader("ASOCIACION PRESUPUESTARIA");
		tab_asociacion_presupuestaria.setTabla("pre_asociacion_presupuestaria", "ide_prasp", 1);
		tab_asociacion_presupuestaria.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("ide_prcla").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("devengado").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("devengado").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("pagado").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("pagado").setAutoCompletar();	
		tab_asociacion_presupuestaria.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true,false"));
		tab_asociacion_presupuestaria.getColumna("ide_cocac").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_asociacion_presupuestaria.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario","ide_prmop","detalle_prmop","");
		tab_asociacion_presupuestaria.agregarRelacion(tab_vigente);
		

		tab_asociacion_presupuestaria.dibujar();
		PanelTabla pat_asociacion_presupuestaria=new PanelTabla();
		pat_asociacion_presupuestaria.setPanelTabla(tab_asociacion_presupuestaria);

		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("AÑO VIGENTE");
		tab_vigente.setTabla("cont_vigente", "ide_covig", 2);
		tab_vigente.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_vigente.setCondicion("not ide_prasp is null");
		tab_vigente.getColumna("ide_geani").setUnico(true);
		tab_vigente.getColumna("ide_prasp").setUnico(true);
		tab_vigente.dibujar();
		PanelTabla pat_vigente = new PanelTabla();
		pat_vigente.setPanelTabla(tab_vigente);

		Division div_Division=new Division();
		div_Division.setId("div_Division");
		div_Division.dividir2(pat_asociacion_presupuestaria, pat_vigente, "50%", "H");
		agregarComponente(div_Division);

	}
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		tab_asociacion_presupuestaria.guardar();
		tab_vigente.guardar();
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_asociacion_presupuestaria() {
		return tab_asociacion_presupuestaria;
	}

	public void setTab_asociacion_presupuestaria(Tabla tab_asociacion_presupuestaria) {
		this.tab_asociacion_presupuestaria = tab_asociacion_presupuestaria;
	}


	public Tabla getTab_tipo_catalogo_cuenta() {
		return tab_tipo_catalogo_cuenta;
	}

	public void setTab_tipo_catalogo_cuenta(Tabla tab_tipo_catalogo_cuenta) {
		this.tab_tipo_catalogo_cuenta = tab_tipo_catalogo_cuenta;
	}


	public Tabla getTab_vigente() {
		return tab_vigente;
	}

	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}
	
}
