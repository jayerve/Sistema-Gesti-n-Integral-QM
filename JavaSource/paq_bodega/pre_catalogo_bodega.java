package paq_bodega;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeSelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_catalogo_bodega extends Pantalla {

	private Tabla tab_catalogo_material = new Tabla();
	private Tabla tab_item_padre = new Tabla();
	private Tabla tab_asociacion_presupuestaria = new Tabla();
	private Division div_division = new Division();
	private Division div_division2 = new Division();

	private Arbol arb_catalogo_material = new Arbol();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_catalogo_bodega() {
		System.out.println("pre_catalogo_bodega");
		tab_item_padre.setId("tab_item_padre");
		tab_item_padre.setHeader("ITEM DEL ARBOL SELECCIONADO");
		tab_item_padre.setTipoFormulario(true); // formulario
		tab_item_padre.getGrid().setColumns(4); // hacer columnas
		tab_item_padre.setTabla("bodt_catalogo_material", "ide_bocam", 2);
		tab_item_padre.getColumna("nivel_bocam").setCombo(utilitario.getListaGruposNivelCuenta());
		tab_item_padre.getColumna("grupo_nivel_bocam").setVisible(false);
		tab_item_padre.getColumna("activo_bocam").setValorDefecto("true");
		tab_item_padre.getColumna("ide_prcla").setCombo(ser_contabilidad.getPreClasificacion());
		tab_item_padre.getColumna("ide_prcla").setAutoCompletar();
		tab_item_padre.getColumna("ide_prcla").setMetodoChange("onChangePadreClasificador");
		tab_item_padre.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_item_padre.getColumna("ide_bounm_presentacion").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		
		tab_item_padre.setCondicion("ide_bocam=-1");

		tab_catalogo_material.setId("tab_catalogo_material");
		tab_catalogo_material.setHeader("HIJOS DEL ITEM SELECCIONADO");
		tab_catalogo_material.setTipoFormulario(false); // formulario
		tab_catalogo_material.getGrid().setColumns(4); //hacer columnas
		tab_catalogo_material.setTabla("bodt_catalogo_material", "ide_bocam", 1);
		tab_catalogo_material.getColumna("nivel_bocam").setCombo(utilitario.getListaGruposNivelCuenta());
		tab_catalogo_material.getColumna("grupo_nivel_bocam").setVisible(false);
		tab_catalogo_material.getColumna("activo_bocam").setValorDefecto("true");
		tab_catalogo_material.getColumna("ide_prcla").setCombo(ser_contabilidad.getPreClasificacion());
		tab_catalogo_material.getColumna("ide_prcla").setAutoCompletar();
		tab_catalogo_material.getColumna("ide_prcla").setMetodoChange("onChangeClasificador");
		tab_catalogo_material.getColumna("ide_prcla").setLectura(false);
		
		tab_item_padre.getColumna("ide_prcla").setNombreVisual("CATALOGO PRESUPUESTARIO");
		tab_catalogo_material.getColumna("ide_prcla").setNombreVisual("CATALOGO PRESUPUESTARIO");
		
		
		tab_catalogo_material.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		tab_catalogo_material.getColumna("ide_bounm_presentacion").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm", "");
		//tab_catalogo_material.setCondicion("ide_bocam=-1");
		tab_catalogo_material.setCampoPadre("con_ide_bocam");
		tab_catalogo_material.setCampoNombre("(select ide_bocam||' '||cat_codigo_bocam||' '|| descripcion_bocam as descripcion_bocam from bodt_catalogo_material b where b.ide_bocam=bodt_catalogo_material.ide_bocam)");
		tab_catalogo_material.agregarArbol(arb_catalogo_material);

		tab_item_padre.dibujar();
		PanelTabla pat_tab_item_padre = new PanelTabla();
		pat_tab_item_padre.setPanelTabla(tab_item_padre);

		tab_catalogo_material.dibujar();
		PanelTabla pat_tipo_catalogo_cuenta = new PanelTabla();
		pat_tipo_catalogo_cuenta.setPanelTabla(tab_catalogo_material);

		div_division2 = new Division();
		div_division2.setId("div_division2");
		div_division2.dividir2(pat_tab_item_padre, pat_tipo_catalogo_cuenta, "35%", "h");
		agregarComponente(div_division2);

		arb_catalogo_material.setId("arb_catalogo_material");
		arb_catalogo_material.onSelect("seleccionoItem");
		arb_catalogo_material.dibujar();

		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_catalogo_material, div_division2, "25%", "v");
		agregarComponente(div_division);

	}

	public void onChangePadreClasificador (AjaxBehaviorEvent event) {
		tab_item_padre.modificar(event);
		String ide_prcla = tab_item_padre.getValor("ide_prcla");
		TablaGenerica tg = ser_bodega.getTablaGenericaPreClasificacionPorId(ide_prcla);
		String codigo_clasificador_prcla = tg.getValor("codigo_clasificador_prcla");
		tab_item_padre.setValor("cat_codigo_bocam", codigo_clasificador_prcla);
		utilitario.addUpdate("tab_item_padre");
	}

	public void onChangeClasificador (AjaxBehaviorEvent event) {
		tab_catalogo_material.modificar(event);
		String ide_prcla = tab_catalogo_material.getValor("ide_prcla");
		TablaGenerica tg = ser_bodega.getTablaGenericaPreClasificacionPorId(ide_prcla);
		String codigo_clasificador_prcla = tg.getValor("codigo_clasificador_prcla");
		tab_catalogo_material.setValor("cat_codigo_bocam", codigo_clasificador_prcla);
		utilitario.addUpdate("tab_catalogo_material");
	}

	public void seleccionoItem(NodeSelectEvent evt) {
		tab_item_padre.limpiar();
		tab_catalogo_material.limpiar();
		// Asigna evento al arbol
		arb_catalogo_material.seleccionarNodo(evt);
		// Filtra la tabla tab_item_padre
		System.out.println("arb_catalogo_material.getValorSeleccionado() " + arb_catalogo_material.getValorSeleccionado());
		tab_catalogo_material.ejecutarValorPadre(arb_catalogo_material.getValorSeleccionado());
		// Filtra la tabla tab_catalogo_material
		tab_item_padre.setCondicion("ide_bocam=" + arb_catalogo_material.getValorSeleccionado());
		tab_item_padre.ejecutarSql();
		tab_item_padre.actualizar();
	}
	
	/**
	 * Verificar si ya existe un item en el catalogo con la misma descripcion y partida
	 * @return
	 */
	public boolean existeItemCatalogo(String descripcion_bocam, String ide_prcla) {
		TablaGenerica tg = ser_bodega.getTablaGenericaCatalogoDuplicado(descripcion_bocam, ide_prcla);
		if (tg.getTotalFilas()>0) {
			return true;
		}
		return false;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_catalogo_material.setFocus();
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		// if (tab_catalogo_material.guardar()) {
		if (!existeItemCatalogo(tab_catalogo_material.getValor("descripcion_bocam"), tab_catalogo_material.getValor("ide_prcla")) || !tab_catalogo_material.isFilaInsertada()) {
			tab_item_padre.guardar();
			tab_catalogo_material.guardar();
			guardarPantalla();
			arb_catalogo_material.ejecutarSql();
			utilitario.addUpdate("arb_arbol");
		} else {
			utilitario.agregarMensajeError("Ya existe un ítem con la misma partida y descripción en el catálogo", "");
		}
		
		// }
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	/**
	 * @return the tab_item_padre
	 */
	public Tabla getTab_item_padre() {
		return tab_item_padre;
	}

	/**
	 * @param tab_item_padre
	 *            the tab_item_padre to set
	 */
	public void setTab_item_padre(Tabla tab_item_padre) {
		this.tab_item_padre = tab_item_padre;
	}

	public Tabla getTab_catalogo_material() {
		return tab_catalogo_material;
	}

	public void setTab_catalogo_material(Tabla tab_catalogo_material) {
		this.tab_catalogo_material = tab_catalogo_material;
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

}
