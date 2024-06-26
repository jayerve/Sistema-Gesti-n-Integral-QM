/**
 * 
 */
package paq_existencia;

import javax.ejb.EJB;

import paq_existencia.ejb.ServicioExistencias;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 * Pantalla que permite la generaci&oacute;n de actas de traspaso
 * 
 * @author ccaceres
 * @since 2017/08/07
 * 
 */
public class exi_saldos_detallado extends Pantalla {

private Tabla tab_consulta_activo = new Tabla();
	
	@EJB
	private ServicioExistencias ser_activo = (ServicioExistencias ) utilitario.instanciarEJB(ServicioExistencias.class);
	
	public exi_saldos_detallado(){
		System.out.println("Pantalla exi_saldos ");
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setSql(ser_activo.getExistenciasEnBodega("1"));
		
		tab_consulta_activo.getColumna("bodega").setFiltro(true);
		tab_consulta_activo.getColumna("codigo_catalogo").setFiltro(true);
		tab_consulta_activo.getColumna("item_presupuestario").setFiltro(true);
		tab_consulta_activo.getColumna("nombre_catalogo").setFiltro(true);
		tab_consulta_activo.getColumna("familia").setFiltro(true);
		tab_consulta_activo.getColumna("unidad_medida").setFiltro(true);
		tab_consulta_activo.getColumna("presentacion").setFiltro(true);
		
		tab_consulta_activo.setLectura(true);
		tab_consulta_activo.dibujar();
		tab_consulta_activo.setRows(30);
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta_activo);

		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
	}

	public Tabla getTab_consulta_activo() {
		return tab_consulta_activo;
	}

	public void setTab_consulta_activo(Tabla tab_consulta_activo) {
		this.tab_consulta_activo = tab_consulta_activo;
	}
}
