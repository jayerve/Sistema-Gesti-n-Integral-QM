package paq_adquisicion;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;

public class pre_proveedor extends Pantalla {
	private Tabla tab_proveedor = new Tabla();
	private Tabla tab_direccion = new Tabla();
	private Tabla tab_telefono = new Tabla();
	private Tabla tab_correo = new Tabla();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario
			.instanciarEJB(ServicioFacturacion.class);

	public pre_proveedor() {
		// TODO Auto-generated constructor stub
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_proveedor.setId("tab_proveedor");
		tab_proveedor.setTabla("tes_proveedor", "ide_tepro", 1);
		tab_proveedor.setTipoFormulario(true);
		tab_proveedor.getGrid().setColumns(4);
		// tab_proveedor.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_proveedor.getColumna("ide_coest").setVisible(false);
		tab_proveedor.getColumna("ide_retic").setCombo(
				"rec_tipo_contribuyente", "ide_retic", "detalle_retic", "");
		tab_proveedor.getColumna("ide_tetpp").setCombo("tes_tipo_proveedor",
				"ide_tetpp", "detalle_tetpp", "");
		tab_proveedor.getColumna("activo_tepro").setValorDefecto("true");
		tab_proveedor.agregarRelacion(tab_direccion);// agraga relacion para los
														// tabuladores
		tab_proveedor.agregarRelacion(tab_telefono);
		tab_proveedor.agregarRelacion(tab_correo);
		tab_proveedor.dibujar();
		PanelTabla pat_proveedor = new PanelTabla();
		pat_proveedor.setPanelTabla(tab_proveedor);

		// /direccion
		tab_direccion.setId("tab_direccion");
		tab_direccion.setHeader("DIRECCIÒN");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("tes_direccion", "ide_tedir", 2);
		tab_direccion.getColumna("activo_tedir").setValorDefecto("true");
		tab_direccion.setCampoForanea("ide_tepro");
		tab_direccion.dibujar();
		PanelTabla pat_direccion = new PanelTabla();
		pat_direccion.setPanelTabla(tab_direccion);
		// telefono
		tab_telefono.setId("tab_telefono");
		tab_telefono.setHeader("TELEFONO");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("tes_telefono", "ide_tetel", 3);
		tab_telefono.setCampoForanea("ide_tepro");
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora",
				"ide_reteo", "detalle_reteo", "");
		tab_telefono.getColumna("activo_tetel").setValorDefecto("true");
		tab_telefono.dibujar();
		PanelTabla pat_telefono = new PanelTabla();
		pat_telefono.setPanelTabla(tab_telefono);
		// correo
		tab_correo.setId("tab_correo");
		tab_correo.setHeader("CORREO");
		tab_correo.setIdCompleto("tab_tabulador:tab_correo");
		tab_correo.setTabla("tes_correo", "ide_tecor", 4);
		tab_correo.setCampoForanea("ide_tepro");
		tab_correo.getColumna("activo_tecor").setValorDefecto("true");
		tab_correo.dibujar();
		PanelTabla pat_correo = new PanelTabla();
		pat_correo.setPanelTabla(tab_correo);

		tab_tabulador.agregarTab("DIRECCION", pat_direccion);// intancia los
																// tabuladores
		tab_tabulador.agregarTab("TELEFONO", pat_telefono);
		tab_tabulador.agregarTab("CORREO", pat_correo);

		// division2

		Division div_division = new Division();
		div_division.dividir2(pat_proveedor, tab_tabulador, "50%", "H");
		agregarComponente(div_division);

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_proveedor.isFocus()) {
			tab_proveedor.insertar();
		} else if (tab_direccion.isFocus()) {
			tab_direccion.insertar();

		} else if (tab_telefono.isFocus()) {
			tab_telefono.insertar();

		}

		else if (tab_correo.isFocus()) {
			tab_correo.insertar();

		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		
		if (tab_correo.getTotalFilas() > 0) {

			if (!tab_correo.getValor("activo_tecor").equals("true")) {
				utilitario.agregarMensajeInfo("Recuerde",
						"El proveedor no cuenta con un correo activo.");
			}

			if (!tab_correo.getValor("notificacion_tecor").equals("true")) {
				utilitario
						.agregarMensajeInfo("Recuerde",
								"El proveedor no cuenta con un correo activo para notificación.");
			}

			if (!pckUtilidades.Utilitario.validarEmailFuerte(tab_correo
					.getValor("detalle_tecor"))) {
				utilitario.agregarMensajeInfo("Recuerde",
						"Debe de ingresar un correo eléctronico valido.");
			}
		}

		if (tab_proveedor.guardar()) {
			if (tab_direccion.guardar()) {
				if (tab_telefono.guardar()) {
					if (tab_correo.guardar()) {

					}
				}
			}
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_proveedor.eliminar();
	}

	public Tabla getTab_proveedor() {
		return tab_proveedor;
	}

	public void setTab_proveedor(Tabla tab_proveedor) {
		this.tab_proveedor = tab_proveedor;
	}

	public Tabla getTab_direccion() {
		return tab_direccion;
	}

	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}

	public Tabla getTab_telefono() {
		return tab_telefono;
	}

	public void setTab_telefono(Tabla tab_telefono) {
		this.tab_telefono = tab_telefono;
	}

	public Tabla getTab_correo() {
		return tab_correo;
	}

	public void setTab_correo(Tabla tab_correo) {
		this.tab_correo = tab_correo;
	}

}
