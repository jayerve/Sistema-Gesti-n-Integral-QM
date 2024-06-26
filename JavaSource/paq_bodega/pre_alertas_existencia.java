package paq_bodega;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_alertas_existencia extends Pantalla {

	private Tabla tab_reglas = new Tabla();
	private Tabla tab_usuarios = new Tabla();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario
			.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_alertas_existencia() {
		System.out.println("pre_alertas_existencia");

		tab_reglas.setId("tab_reglas");
		tab_reglas.setTabla("bodt_alerta_regla", "ide_boalr", 1);

		// tab_tabla_reglas.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true"));
		tab_reglas.getColumna("ide_prcla").setCombo("pre_clasificador", "ide_prcla", "codigo_clasificador_prcla",
				"activo_prcla=true");
		tab_reglas.getColumna("cantidad_minima_stock_boalr").setRequerida(true);

		tab_reglas.setHeader("Reglas");

		tab_usuarios.setId("tab_usuarios");
		tab_usuarios.setTabla("bodt_alerta_usuario", "ide_boalu", 2);

		// tab_usuarios.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,
		// false"));
		tab_usuarios.getColumna("ide_gtemp").setCombo(ser_bodega.getEmpleadosActivos());
		tab_usuarios.getColumna("ide_gtemp").setFiltroContenido();

		tab_usuarios.setHeader("Usuarios a notificar");

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_reglas);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_usuarios);

		tab_reglas.dibujar();
		tab_usuarios.dibujar();

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);

	}

	@Override
	public void insertar() {
		if (tab_reglas.isFocus()) {
			tab_reglas.insertar();

			tab_reglas.setValor("activo_boalr", Boolean.TRUE.toString());
			tab_reglas.setValor("cantidad_minima_stock_boalr", String.valueOf(1));
		} else if (tab_usuarios.isFocus()) {
			tab_usuarios.insertar();

			tab_usuarios.setValor("activo_boalu", Boolean.TRUE.toString());
		}

	}

	@Override
	public void guardar() {
		System.out.println("pre_alertas_existencia:guardar");

		if (tab_reglas.guardar() && tab_usuarios.guardar()) {
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_reglas() {
		return tab_reglas;
	}

	public void setTab_reglas(Tabla tab_reglas) {
		this.tab_reglas = tab_reglas;
	}

	public Tabla getTab_usuarios() {
		return tab_usuarios;
	}

	public void setTab_usuarios(Tabla tab_usuarios) {
		this.tab_usuarios = tab_usuarios;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public ServicioNomina getSer_nomina() {
		return ser_nomina;
	}

	public void setSer_nomina(ServicioNomina ser_nomina) {
		this.ser_nomina = ser_nomina;
	}

	public ServicioPresupuesto getSer_presupuesto() {
		return ser_presupuesto;
	}

	public void setSer_presupuesto(ServicioPresupuesto ser_presupuesto) {
		this.ser_presupuesto = ser_presupuesto;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

}
