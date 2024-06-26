package paq_bodega;

import javax.ejb.EJB;

import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_bodega_ubicacion extends Pantalla {

	private Tabla tab_bodegas_ubicacion = new Tabla();
	private Tabla tab_asociacion_presupuestaria = new Tabla();
	private Division div_division = new Division();
	private Division div_division2 = new Division();

	private Tabla tab_tabla1 = new Tabla();
	private SeleccionTabla set_empleado = new SeleccionTabla();

	private Arbol arb_catalogo_material = new Arbol();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public void agregarEmpleado() {

		set_empleado.getTab_seleccion().setSql(ser_empleado.servicioEmpleadosActivos("true"));
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();

	}

	public void aceptarEmpleado() {
		String str_seleccionados = set_empleado.getValorSeleccionado();
		if (str_seleccionados != null) {
			// Inserto los empleados seleccionados en la tabla de resposable d
			// econtratacion
			// TablaGenerica tab_empleado_responsable =
			// ser_empleado.servicioEmpleadosActivos());
			tab_tabla1.setValor("ide_gtemp", str_seleccionados);

			set_empleado.cerrar();
			tab_tabla1.modificar(tab_tabla1.getFilaActual());
			utilitario.addUpdate("tab_tabla1");
			tab_tabla1.guardar();
			guardarPantalla();

		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}

	public pre_bodega_ubicacion() {
		System.out.println("pre_bodega_ubicacion");
		Boton bot_agrearemple = new Boton();

		bot_agrearemple.setValue("Agregar / Editar Empleado");
		bot_agrearemple.setTitle("Agregar Empleado al usuario actual");
		bot_agrearemple.setMetodo("agregarEmpleado");
		// bar_botones.agregarBoton(bot_agrearemple);

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_empleado.servicioEmpleadosActivos("true"), "ide_gtemp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
		set_empleado.setRadio();
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado);

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTipoFormulario(true);
		tab_tabla1.setTabla("sis_usuario", "ide_usua", 1);
		tab_tabla1.getColumna("IDE_PERF").setCombo("SIS_PERFIL", "IDE_PERF", "NOM_PERF", "ACTIVO_PERF=true");
		tab_tabla1.getColumna("IDE_PERF").setRequerida(true);
		tab_tabla1.getColumna("NICK_USUA").setNombreVisual("NICK NAME");
		tab_tabla1.getColumna("NICK_USUA").setUnico(true);
		tab_tabla1.getColumna("ACTIVO_USUA").setCheck();
		tab_tabla1.getColumna("ACTIVO_USUA").setValorDefecto("true");
		tab_tabla1.getColumna("ACTIVO_USUA").setLectura(true);
		tab_tabla1.getColumna("BLOQUEADO_USUA").setLectura(true);
		tab_tabla1.getColumna("TEMA_USUA").setLectura(true);
		tab_tabla1.getColumna("TEMA_USUA").setValorDefecto("sam");
		tab_tabla1.getColumna("BLOQUEADO_USUA").setCheck();
		tab_tabla1.getColumna("FECHA_REG_USUA").setValorDefecto(utilitario.getFechaActual());
		tab_tabla1.getColumna("FECHA_REG_USUA").setLectura(true);
		tab_tabla1.getColumna("IDE_GTEMP").setCombo(ser_empleado.servicioEmpleadosActivos("true,false"));
		tab_tabla1.getColumna("IDE_GTEMP").setAutoCompletar();
		tab_tabla1.getColumna("IDE_GTEMP").setLectura(true);
		tab_tabla1.getColumna("NICK_USUA").setMetodoChange("asignarClave");
		tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setValorDefecto("true");
		tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setLectura(true);
		tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setCheck();
		// tab_tabla1.agregarRelacion(tab_tabla2);
		// tab_tabla1.agregarRelacion(tab_tabla3);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("Cuando se crean un usuario nuevo la clave es la misma que el valor del campo NICK NAME");
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_bodegas_ubicacion.setId("tab_bodegas_ubicacion");
		tab_bodegas_ubicacion.setHeader("UBICACIÓN DE BODEGAS");
		tab_bodegas_ubicacion.setTipoFormulario(true); // formulario
		tab_bodegas_ubicacion.getGrid().setColumns(2); // hacer columnas
		tab_bodegas_ubicacion.setTabla("bodt_bodega_ubicacion", "ide_boubi", 1);
		tab_bodegas_ubicacion.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_bodegas_ubicacion.getColumna("ide_afubi").setCombo("afi_ubicacion", "ide_afubi", "detalle_afubi", "");
		tab_bodegas_ubicacion.getColumna("ide_afubi").setRequerida(true);

		tab_bodegas_ubicacion.getColumna("ide_gtemp").setAutoCompletar();
		tab_bodegas_ubicacion.setCampoPadre("con_ide_boubi"); // necesarios para
																// el arbol ide
																// recursivo

		// tab_bodegas_ubicacion.setCampoNombre("(select cat_codigo_bocam||' '|| descripcion_bocam as descripcion_bocam from bodt_catalogo_material b where b.ide_bocam=bodt_catalogo_material.ide_bocam)");
		// //necesarios para el arbol campo a mostrarse
		tab_bodegas_ubicacion.setCampoNombre("(select detalle_boubi from bodt_bodega_ubicacion b where b.ide_boubi=bodt_bodega_ubicacion.ide_boubi)"); // necesarios
																																						// para
																																						// el
																																						// arbol
																																						// campo
																																						// a
																																						// mostrarse
		tab_bodegas_ubicacion.agregarArbol(arb_catalogo_material);// necesarios
																	// para el
																	// arbol

		tab_bodegas_ubicacion.dibujar();
		PanelTabla pat_tipo_catalogo_cuenta = new PanelTabla();
		pat_tipo_catalogo_cuenta.setPanelTabla(tab_bodegas_ubicacion);

		div_division2 = new Division();
		div_division2.setId("div_division2");
		div_division2.dividir1(pat_tipo_catalogo_cuenta);
		agregarComponente(div_division2);

		arb_catalogo_material.setId("arb_catalogo_material");
		arb_catalogo_material.dibujar();
		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_catalogo_material, div_division2, "25%", "v");
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
		if (tab_bodegas_ubicacion.guardar()) {
			guardarPantalla();
			// actualiza el arbol
			arb_catalogo_material.ejecutarSql();
			utilitario.addUpdate("arb_arbol");
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();

	}

	public Tabla gettab_bodegas_ubicacion() {
		return tab_bodegas_ubicacion;
	}

	public void settab_bodegas_ubicacion(Tabla tab_bodegas_ubicacion) {
		this.tab_bodegas_ubicacion = tab_bodegas_ubicacion;
	}

	public Tabla getTab_asociacion_presupuestaria() {
		return tab_asociacion_presupuestaria;
	}

	public void setTab_asociacion_presupuestaria(Tabla tab_asociacion_presupuestaria) {
		this.tab_asociacion_presupuestaria = tab_asociacion_presupuestaria;
	}

	public Arbol getArb_catalogo_material() {
		return arb_catalogo_material;
	}

	public void setArb_catalogo_material(Arbol arb_catalogo_material) {
		this.arb_catalogo_material = arb_catalogo_material;
	}

	/**
	 * @return the tab_bodegas_ubicacion
	 */
	public Tabla getTab_bodegas_ubicacion() {
		return tab_bodegas_ubicacion;
	}

	/**
	 * @param tab_bodegas_ubicacion
	 *            the tab_bodegas_ubicacion to set
	 */
	public void setTab_bodegas_ubicacion(Tabla tab_bodegas_ubicacion) {
		this.tab_bodegas_ubicacion = tab_bodegas_ubicacion;
	}

	/**
	 * @return the div_division
	 */
	public Division getDiv_division() {
		return div_division;
	}

	/**
	 * @param div_division
	 *            the div_division to set
	 */
	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	/**
	 * @return the div_division2
	 */
	public Division getDiv_division2() {
		return div_division2;
	}

	/**
	 * @param div_division2
	 *            the div_division2 to set
	 */
	public void setDiv_division2(Division div_division2) {
		this.div_division2 = div_division2;
	}

	/**
	 * @return the tab_tabla1
	 */
	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	/**
	 * @param tab_tabla1
	 *            the tab_tabla1 to set
	 */
	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	/**
	 * @return the set_empleado
	 */
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	/**
	 * @param set_empleado
	 *            the set_empleado to set
	 */
	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	/**
	 * @return the ser_contabilidad
	 */
	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	/**
	 * @param ser_contabilidad
	 *            the ser_contabilidad to set
	 */
	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	/**
	 * @return the ser_empleado
	 */
	public ServicioNomina getSer_empleado() {
		return ser_empleado;
	}

	/**
	 * @param ser_empleado
	 *            the ser_empleado to set
	 */
	public void setSer_empleado(ServicioNomina ser_empleado) {
		this.ser_empleado = ser_empleado;
	}

	/**
	 * @return the ser_bodega
	 */
	public ServicioBodega getSer_bodega() {
		return ser_bodega;
	}

	/**
	 * @param ser_bodega
	 *            the ser_bodega to set
	 */
	public void setSer_bodega(ServicioBodega ser_bodega) {
		this.ser_bodega = ser_bodega;
	}

}
