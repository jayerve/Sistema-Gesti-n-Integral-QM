package paq_activos;

import javax.ejb.EJB;

import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_empresa_externa_responsable extends Pantalla {
	private Tabla tab_empresa = new Tabla();
	private Tabla tab_responsable = new Tabla();
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public pre_empresa_externa_responsable() {
		tab_empresa.setId("tab_empresa");
		tab_empresa.setHeader("EMPRESA");
		tab_empresa.setTabla("afi_empresa", "ide_empre", 1);
		tab_empresa.setTipoFormulario(true);
		tab_empresa.getGrid().setColumns(4);
		tab_empresa.agregarRelacion(tab_responsable);
		tab_empresa.dibujar();
		PanelTabla pat_empresa = new PanelTabla();
		pat_empresa.setPanelTabla(tab_empresa);
		tab_responsable.setId("tab_responsable");
		tab_responsable.setHeader("RESPONSABLES REGISTRADOS");
		tab_responsable.setTabla("afi_empresa_responsable", "ide_empre_res", 2);
		tab_empresa.getGrid().setColumns(4);
		tab_responsable.dibujar();
		PanelTabla pat_responsable = new PanelTabla();
		pat_responsable.setPanelTabla(tab_responsable);
		Division div_divi = new Division();
		div_divi.dividir2(pat_empresa, pat_responsable, "50%", "H");
		agregarComponente(div_divi);
	}

	// reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub

	}

	public void aceptarReporte() {

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_empresa.isFocus()) {
			tab_empresa.insertar();
			utilitario.addUpdate("tab_responsable");
		} else if (tab_responsable.isFocus()) {
			tab_responsable.insertar();
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_empresa.guardar();
		tab_responsable.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensaje("Prohibición", "La presente pantalla no permite la eliminación de los registros.");
	}

	/**
	 * @return the tab_empresa
	 */
	public Tabla getTab_empresa() {
		return tab_empresa;
	}

	/**
	 * @param tab_empresa the tab_empresa to set
	 */
	public void setTab_empresa(Tabla tab_empresa) {
		this.tab_empresa = tab_empresa;
	}

	/**
	 * @return the tab_responsable
	 */
	public Tabla getTab_responsable() {
		return tab_responsable;
	}

	/**
	 * @param tab_responsable the tab_responsable to set
	 */
	public void setTab_responsable(Tabla tab_responsable) {
		this.tab_responsable = tab_responsable;
	}

	/**
	 * @return the ser_seguridad
	 */
	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	/**
	 * @param ser_seguridad the ser_seguridad to set
	 */
	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	
}
