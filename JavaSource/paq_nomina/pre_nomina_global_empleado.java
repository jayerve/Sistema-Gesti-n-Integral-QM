/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.export.DataExporter;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.row.Row;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;



import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

public class pre_nomina_global_empleado extends Pantalla {

	private Division div_division = new Division();
	private SeleccionTabla set_det_tip_nomina = new SeleccionTabla();
	private Combo com_periodo_rol = new Combo();
	private SeleccionTabla set_tipo_rubro = new SeleccionTabla();

	private Dialogo dia_rubros = new Dialogo();
	private SeleccionTabla set_rubros = new SeleccionTabla();
	private String IDE_NRRUB = "";
	private String IDE_NRDTN = "";
	private List lis_nom_columnas = new ArrayList();
	private List lis_totales_consolidado = new ArrayList();
	private List<Object> lis_datos_rol = new ArrayList<Object>();
	//Lista de rubros generados en nomina POR EMPLEADO
	private List lis_nom_columnas_orden = new ArrayList();
	private List<Object> lis_datos_rol_orden = new ArrayList<Object>();
	private List<Object> lis_datos_rol_orden_rol_cambio = new ArrayList<Object>();

	private DataTable tabla = new DataTable();
	private Dialogo dia_formula = new Dialogo();
	private Etiqueta eti_formula = new Etiqueta();
	private Etiqueta eti_mensaje = new Etiqueta();
	private AutoCompletar aut_rubros_tip_formula = new AutoCompletar();
	private Grid gri = new Grid();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private SeleccionTabla sel_tab_periodo_boleta= new SeleccionTabla();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_nomina_global_empleado() {

		Boton bot_ver_nomina_global = new Boton();
		bot_ver_nomina_global.setMetodo("pedirParametros");
		bot_ver_nomina_global.setValue("VER ROL GLOBAL");

		bar_botones.agregarComponente(new Etiqueta("Periodo Rol: "));
		bar_botones.agregarComponente(com_periodo_rol);
		bar_botones.agregarBoton(bot_ver_nomina_global);

		rep_reporte.setId("rep_reporte"); 
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		bar_botones.agregarReporte();

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_excel = new Boton();
		bot_excel.setValue("EXCEL");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarNominaExcel");

		bar_botones.agregarBoton(bot_excel);

		Boton bot_excel_orden = new Boton();
		bot_excel_orden.setValue("EXCEL POR EMPLEADO");
		bot_excel_orden.setAjax(false);
		bot_excel_orden.setMetodo("exportarNominaExcelOrden");
		bar_botones.agregarBoton(bot_excel_orden);

		
		Boton bot_rol = new Boton();
		bot_rol.setValue("ROL EMPLEADO");
		bot_rol.setAjax(false);
		bot_rol.setMetodo("exportarNominaExcelRol");
		bar_botones.agregarBoton(bot_rol);
		
		sel_tab_periodo_boleta.setId("sel_tab_periodo_boleta");
		sel_tab_periodo_boleta.setSeleccionTabla(ser_nomina.getSqlSeleccionTablaPeriodoTipoRol(), "IDE_GEPRO");
		sel_tab_periodo_boleta.setRadio();
		sel_tab_periodo_boleta.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
		sel_tab_periodo_boleta.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		gru_pantalla.getChildren().add(sel_tab_periodo_boleta);
		sel_tab_periodo_boleta.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_periodo_boleta);
		
		Boton bot_cambiar_filtro = new Boton();
		bot_cambiar_filtro.setMetodo("cambiarFiltroRubros");
		bot_cambiar_filtro.setValue("FILTRO RUBROS");
		bar_botones.agregarBoton(bot_cambiar_filtro);

		// Dialogo para ver la Formula

		Boton bot_ver_formula = new Boton();

		bot_ver_formula.setValue("Ver Formula");
		bot_ver_formula.setMetodo("abrirDialogoVerFormula");
		bar_botones.agregarBoton(bot_ver_formula);

		dia_formula.setId("dia_formula");
		dia_formula.setTitle("Visualizador de Formula");
		dia_formula.setWidth("40%");
		dia_formula.setHeight("25%");
		dia_formula.setModal(false);

		aut_rubros_tip_formula.setId("aut_rubros_tip_formula");
		aut_rubros_tip_formula.setAutoCompletar("select IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO where IDE_NRFOC=" + utilitario.getVariable("p_nrh_forma_calculo_formula"));
		aut_rubros_tip_formula.setMetodoChange("verFormulaLetras");

		Grupo gru_cuerpo = new Grupo();
		eti_mensaje.setValue("Rubro: ");
		eti_mensaje.setStyle("font-size: 14px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
		eti_formula.setId("eti_formula");
		eti_formula.setStyle("font-size: 15px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");

		Grid gri_clave = new Grid();
		gri_clave.setWidth("100%");
		gri_clave.setStyle("text-align: center;");
		gri_clave.getChildren().add(eti_formula);

		gru_cuerpo.getChildren().add(eti_mensaje);
		gru_cuerpo.getChildren().add(aut_rubros_tip_formula);
		gru_cuerpo.getChildren().add(gri_clave);
		dia_formula.getBot_aceptar().setMetodo("aceptarFormula");
		dia_formula.setDialogo(gru_cuerpo);
		agregarComponente(dia_formula);

		cargarTablaVacia();
		div_division.setId("div_division");

		agregarComponente(div_division);

		com_periodo_rol.setId("com_periodo_rol");
		com_periodo_rol.setCombo(ser_nomina.getSqlComboPeriodoRol());
		com_periodo_rol.setStyle("width: 300px; margin: 0 0 -8px 0;");
		com_periodo_rol.setMetodo("cambiaPeriodoRol");

		set_det_tip_nomina.setId("set_det_tip_nomina");
		set_det_tip_nomina.setTitle("Seleccion de Parametros");
		set_det_tip_nomina.setSeleccionTabla("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit " + "FROM NRH_DETALLE_TIPO_NOMINA a " + "INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM "
				+ "inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin " + "inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit " + "where a.ide_nrdtn in (select IDE_NRDTN from NRH_ROL where IDE_GEPRO=-1) ", "IDE_NRDTN");
		set_det_tip_nomina.getBot_aceptar().setMetodo("aceptarTipoNomina");
		set_det_tip_nomina.setDynamic(false);

		agregarComponente(set_det_tip_nomina);

		set_tipo_rubro.setId("set_tipo_rubro");
		set_tipo_rubro.setSeleccionTabla("select IDE_NRTIR,DETALLE_NRTIR from NRH_TIPO_RUBRO order by IDE_NRTIR", "IDE_NRTIR");
		set_tipo_rubro.getBot_aceptar().setMetodo("aceptarTipoRubro");
		agregarComponente(set_tipo_rubro);

		set_rubros.setId("set_rubros");
		set_rubros.setSeleccionTabla("select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB " + "INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " + "WHERE DER.IDE_NRDTN IN (-1) AND IMPRIME_NRDER=true "
				+ "GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER " + "ORDER BY ORDEN_IMPRIME_NRDER ASC", "IDE_NRRUB");
		set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
		set_rubros.getTab_seleccion().onSelectCheck("seleccionaRubro");
		set_rubros.getTab_seleccion().onUnselectCheck("quitaSeleccionRubro");
		set_rubros.getBot_aceptar().setMetodo("aceptarRubros");
		agregarComponente(set_rubros);

	}

	public void aceptarFormula() {
		dia_formula.cerrar();
	}

	public void verFormulaLetras(SelectEvent evt) {
		aut_rubros_tip_formula.onSelect(evt);
		String formula = utilitario.consultar("select * from NRH_DETALLE_RUBRO where FORMULA_NRDER is not NULL and IDE_NRRUB=" + aut_rubros_tip_formula.getValor()).getValor(0, "FORMULA_NRDER");
		if (formula != null && !formula.isEmpty()) {
			if (formula.startsWith("=")) {
				eti_formula.setValue(ser_nomina.getFormulaEnLetras(0, "", "", "", formula, false));
				utilitario.addUpdate("eti_formula");
			} else {
				utilitario.agregarMensajeInfo("Atencion", "El rubro seleccionado no tiene formula de calculo");
			}
		} else {
			utilitario.agregarMensajeInfo("Atencion 55", "El rubro seleccionado no tiene formula de calculo");
		}
	}

	public void abrirDialogoVerFormula() {
		dia_formula.dibujar();
	}

	public void seleccionaRubro(SelectEvent evt) {
		System.out.println("sel rubro " + set_rubros.getListaSeleccionados().size());
	}

	public void quitaSeleccionRubro(UnselectEvent evt) {

		System.out.println("un sel rubro " + set_rubros.getListaSeleccionados().size());
	}

	public void cambiarFiltroRubros() {
		if (lis_datos_rol.size() > 0) {

			set_rubros.getTab_seleccion().setSql(ser_nomina.getRubrosOrden(IDE_NRDTN, "").getSql());

			// set_rubros.getTab_seleccion().setSql("select a.ide_nrrub,a.detalle_nrrub from ( select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB "
			// +
			// "INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB "
			// +
			// "WHERE DER.IDE_NRDTN IN ("+IDE_NRDTN+") AND IMPRIME_NRDER=true "
			// +
			// "GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
			// "ORDER BY ORDEN_IMPRIME_NRDER ASC)a " +
			// "GROUP BY a.ide_nrrub,a.detalle_nrrub ");
			set_rubros.getTab_seleccion().ejecutarSql();
			set_rubros.dibujar();
		} else {
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar los parametros para Visualizar el Rol");
		}

	}

	private String tipo_nomina = "";

	public void aceptarTipoNomina() {
		System.out.println("tipo nomina " + set_det_tip_nomina.getSeleccionados());
		if (com_periodo_rol.getValue() != null) {
			if (set_det_tip_nomina.getSeleccionados() != null && !set_det_tip_nomina.getSeleccionados().isEmpty()) {

				tipo_nomina = "";
				for (int i = 0; i < set_det_tip_nomina.getListaSeleccionados().size(); i++) {
					Fila fila = set_det_tip_nomina.getListaSeleccionados().get(i);
					tipo_nomina += ser_nomina.getTipoEmpleado(ser_nomina.getDetalleTipoNomina(fila.getRowKey() + "").getValor("IDE_GTTEM")).getValor("DETALLE_GTTEM");
					if (i > 0 && i < (set_det_tip_nomina.getListaSeleccionados().size() - 1)) {
						tipo_nomina += ",";
					}
				}

				IDE_NRDTN = set_det_tip_nomina.getSeleccionados();
				set_tipo_rubro.dibujar();
			} else {
				utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Tipo de Nomina");
			}
		} else {
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar un periodo de rol");
		}

	}

	public void limpiar() {
		com_periodo_rol.setValue(null);
		IDE_NRDTN = "";
		IDE_NRRUB = "";
		cargarTablaVacia();
		utilitario.addUpdate("com_periodo_rol,div_division");
	}

	public void cambiaPeriodoRol() {
		if (com_periodo_rol.getValue() != null) {
			lis_datos_rol.clear();
			lis_nom_columnas.clear();
			lis_totales_consolidado.clear();

			llenarTabla(IDE_NRDTN, com_periodo_rol.getValue() + "", IDE_NRRUB);

			dibujarTabla();
			utilitario.addUpdate("div_division");

		} else {
			cargarTablaVacia();
			utilitario.addUpdate("div_division");
		}

	}

	public void pedirParametros() {

		if (com_periodo_rol.getValue() != null) {
			set_det_tip_nomina.getTab_seleccion().setSql(
					"SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit " + "FROM NRH_DETALLE_TIPO_NOMINA a " + "INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " + "inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin "
							+ "inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit " + "where a.ide_nrdtn in (select IDE_NRDTN from NRH_ROL where IDE_GEPRO=" + com_periodo_rol.getValue() + ") ");
			set_det_tip_nomina.getTab_seleccion().ejecutarSql();
			set_det_tip_nomina.getTab_seleccion().setSeleccionados(null);
			set_det_tip_nomina.dibujar();
		} else {
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar un periodo de rol");
		}
	}

	public void dibujarTabla() {

		tabla = new DataTable();
		tabla.setId("tabla");
		tabla.setResizableColumns(true);
		tabla.setStyle("font-size:13px");
		tabla.setVar("suc");
		tabla.setValueExpression("value", crearValueExpression("pre_index.clase.lis_datos_rol"));

		ColumnGroup columnGroup = new ColumnGroup();
		columnGroup.setType("header");
		tabla.getChildren().add(columnGroup);

		Row r1 = new Row();
		columnGroup.getChildren().add(r1);

		Column c1 = new Column();
		c1.setHeaderText("Departamento");
		c1.setWidth(120);
		c1.setRowspan(3);
		r1.getChildren().add(c1);

		Column c2 = new Column();
		c2.setHeaderText("Cédula");
		c2.setWidth(60);
		c2.setRowspan(3);
		r1.getChildren().add(c2);

		Column c3 = new Column();
		c3.setHeaderText("Empleado");
		c3.setRowspan(3);
		c3.setWidth(200);
		c3.setResizable(true);
		r1.getChildren().add(c3);

		Column c4 = new Column();
		c4.setHeaderText("Genero");
		c4.setWidth(100);
		c4.setRowspan(3);
		r1.getChildren().add(c4);

		Column c5 = new Column();
		c5.setHeaderText("Discapacidad");
		c5.setWidth(100);
		c5.setRowspan(3);
		r1.getChildren().add(c5);

		Column c51 = new Column();
		c51.setHeaderText("Acumula Decimo");
		c51.setWidth(100);
		c51.setRowspan(3);
		r1.getChildren().add(c51);

		Row r2 = new Row();
		columnGroup.getChildren().add(r2);
		Column c6 = new Column();
		c6.setHeaderText("Rubros");
		c6.setColspan(lis_nom_columnas.size());
		r2.getChildren().add(c6);

		Row r3 = new Row();
		columnGroup.getChildren().add(r3);

		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c7 = new Column();
			c7.setStyle("font-weight:bold");
			c7.setValueExpression("headerText", crearValueExpression("pre_index.clase.lis_nom_columnas[" + i + "]"));
			// c7.setWidth(120);
			c7.setRowspan(5);
			c7.setResizable(true);
			r3.getChildren().add(c7);
		}

		SubTable subtable = new SubTable();
		subtable.setVar("emp");
		subtable.setValueExpression("value", crearValueExpression("suc[1]"));
		tabla.getChildren().add(subtable);

		Etiqueta eti_sucursal = new Etiqueta();
		eti_sucursal.setValueExpression("value", "suc[0]");
		eti_sucursal.setStyle("font-size:14px");
		subtable.getFacets().put("header", eti_sucursal);

		for (int i = 0; i < (6 + lis_nom_columnas.size()); i++) {
			Column c8 = new Column();
			Etiqueta eti = new Etiqueta();

			if (i >= 6) {
				eti.setTitle(lis_nom_columnas.get(i - 6) + "");
			}

			eti.setValueExpression("value", "emp[" + i + "]");
			if (i > 4) {
				c8.setStyle("text-align:center;font-size:10px");
			} else if (i == 2) {
				c8.setStyle("text-align:left;font-size:9px");
			} else {
				c8.setStyle("text-align:center;font-size:9px");
			}
			c8.setResizable(true);

			c8.getChildren().add(eti);
			subtable.getChildren().add(c8);
		}

		ColumnGroup columnGroupTotales = new ColumnGroup();
		columnGroupTotales.setType("footer");
		subtable.getChildren().add(columnGroupTotales);

		Row r4 = new Row();
		columnGroupTotales.getChildren().add(r4);

		Column c9 = new Column();
		c9.setColspan(6);

		c9.setFooterText("Total: ");
		c9.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r4.getChildren().add(c9);
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c10 = new Column();
			c10.setValueExpression("footerText", crearValueExpression("suc[" + (i + 2) + "]"));
			c10.setStyle("text-align:right;font-size:14px;padding-right:10px");
			r4.getChildren().add(c10);
		}

		ColumnGroup columnGroupTotalesC = new ColumnGroup();
		columnGroupTotalesC.setType("footer");
		tabla.getChildren().add(columnGroupTotalesC);

		Row r5 = new Row();
		columnGroupTotalesC.getChildren().add(r5);

		Column c11 = new Column();
		c11.setColspan(6);
		c11.setFooterText("Total Consolidado: ");
		c11.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r5.getChildren().add(c11);

		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c12 = new Column();
			c12.setValueExpression("footerText", crearValueExpression("pre_index.clase.lis_totales_consolidado[" + i + "]"));
			c12.setStyle("text-align:right;font-size:14px;padding-right:10px");
			r5.getChildren().add(c12);
		}

		tabla.setSelectionMode("single");

		Grid gri_cabecera = new Grid();
		gri_cabecera.setWidth("100%");

		Etiqueta eti_titulo = new Etiqueta();
		eti_titulo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_titulo.setValue("REPORTE DE NOMINA GLOBAL DE EMPLEADOS ");

		TablaGenerica tab_gepro = ser_nomina.getPeriodoRol(com_periodo_rol.getValue() + "");

		Etiqueta eti_periodo = new Etiqueta();
		eti_periodo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_periodo.setValue("desde " + tab_gepro.getValor("FECHA_INICIAL_GEPRO") + " hasta " + tab_gepro.getValor("FECHA_FINAL_GEPRO"));

		String str_tip_nomina = "";

		try {
			for (int i = 0; i < set_det_tip_nomina.getListaSeleccionados().size(); i++) {
				Fila fila = set_det_tip_nomina.getListaSeleccionados().get(i);
				str_tip_nomina += fila.getCampos()[1] + "-" + fila.getCampos()[2] + ",";
			}
			try {
				str_tip_nomina = str_tip_nomina.substring(0, str_tip_nomina.length() - 1);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		Etiqueta eti_tip_nomina = new Etiqueta();
		eti_tip_nomina.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_tip_nomina.setValue("NOMINAS: " + str_tip_nomina);

		gri_cabecera.getChildren().add(eti_titulo);
		gri_cabecera.getChildren().add(eti_periodo);
		gri_cabecera.getChildren().add(eti_tip_nomina);

		gri = new Grid();
		gri.setWidth("100%");
		gri.setStyle("display:block;height:100%");
		gri.getChildren().add(tabla);
		gri.setHeader(gri_cabecera);

		div_division.getChildren().clear();

		div_division.dividir1(gri);

	}

	public void exportarNominaExcel() {
		if (lis_datos_rol.size() > 0) {
			if (lis_nom_columnas.size() > 0) {
				String ide_gemes = ser_nomina.getPeriodoRol(com_periodo_rol.getValue() + "").getValor("IDE_GEMES");
				exportarXLS("nomina.xls", tipo_nomina, ide_gemes);
			}
		}
	}

	public void exportarXLS(String nombre, String tipo_nomina, String mes) {
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String nom = nombre;
			File result = new File(extContext.getRealPath("/" + nom));
			WritableWorkbook archivo_xls = Workbook.createWorkbook(result);
			WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0);
			WritableFont fuente = new WritableFont(WritableFont.TAHOMA, 10);
			WritableCellFormat formato_celda = new WritableCellFormat(fuente);
			formato_celda.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda.setOrientation(Orientation.HORIZONTAL);
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_suc = new WritableCellFormat(fuente_suc);
			formato_celda_suc.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda_suc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_suc.setOrientation(Orientation.HORIZONTAL);
			formato_celda_suc.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_totales = new WritableCellFormat(fuente_suc);
			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_totales.setOrientation(Orientation.HORIZONTAL);
			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.RED);

			WritableCellFormat formato_celda_valor_rubro = new WritableCellFormat(fuente);
			formato_celda_valor_rubro.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_valor_rubro.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_valor_rubro.setOrientation(Orientation.HORIZONTAL);
			formato_celda_valor_rubro.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			int int_columna = 0;
			int int_fila = 4;

			jxl.write.Label lab_titulo = new jxl.write.Label(0, 0, "NOMINA: ", formato_celda_suc);
			hoja_xls.addCell(lab_titulo);
			CellView cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_tip_nomina = new jxl.write.Label(1, 0, tipo_nomina, formato_celda_suc);
			hoja_xls.addCell(lab_tip_nomina);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			jxl.write.Label lab_mes = new jxl.write.Label(0, 1, "MES: ", formato_celda_suc);
			hoja_xls.addCell(lab_mes);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_mes1 = new jxl.write.Label(1, 1, mes, formato_celda_suc);
			hoja_xls.addCell(lab_mes1);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			for (int i = 0; i < lis_datos_rol.size(); i++) {
				Object[] fila = (Object[]) lis_datos_rol.get(i);
				if (i == 0) {
					// NOMBRES DE COLUMNAS
					// DEPARTAMENTO
					jxl.write.Label lab1 = new jxl.write.Label(0, 3, "DEPARTAMENTO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0, cv);

					// CEDULA
					lab1 = new jxl.write.Label(1, 3, "CEDULA", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1, cv);

					// NOMBRE EMPLEADO
					lab1 = new jxl.write.Label(2, 3, "EMPLEADO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2, cv);

					// CARGO
					lab1 = new jxl.write.Label(3, 3, "GENERO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3, cv);

					// DENOMINACION
					lab1 = new jxl.write.Label(4, 3, "DISCAPACIDAD", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4, cv);

					// CONTRATO
					lab1 = new jxl.write.Label(5, 3, "ACUMULA DECIMO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(5, cv);

					for (int j = 0; j < lis_nom_columnas.size(); j++) {

						jxl.write.Label lab2 = new jxl.write.Label(j + 6, 3, lis_nom_columnas.get(j) + "", formato_celda);
						hoja_xls.addCell(lab2);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(j + 6, cv);
					}
				}

				// SUCURSAL
				jxl.write.Label lab = new jxl.write.Label(int_columna, i + int_fila, fila[0] + "", formato_celda_suc);
				hoja_xls.addCell(lab);
				cv = new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(int_columna, cv);

				// lista de objetos que contiene todos los datos
				List<Object> lis_datos = (List<Object>) fila[1];

				for (int j = 0; j < lis_datos.size(); j++) {
					Object[] fila_datos = (Object[]) lis_datos.get(j);
					for (int k = 0; k < 6; k++) {
						jxl.write.Label lab3 = new jxl.write.Label(k, i + int_fila + 1, fila_datos[k] + "", formato_celda);
						hoja_xls.addCell(lab3);
					}
					for (int l = 0; l < lis_nom_columnas.size(); l++) {

						try {
							jxl.write.Number num = new jxl.write.Number(l + 6, i + int_fila + 1, pckUtilidades.CConversion.CDbl_2(String.valueOf(fila_datos[l + 6])), formato_celda_valor_rubro);
							hoja_xls.addCell(num);
						} catch (Exception e) {
							// TODO: handle exception
							jxl.write.Label lab3 = new jxl.write.Label(l + 6, i + int_fila + 1, fila_datos[l + 6] + "", formato_celda_valor_rubro);
							hoja_xls.addCell(lab3);

						}

					}
					int_fila = int_fila + 1;

				}

				// vector con los totales por sucursal

				jxl.write.Label lab_tot = new jxl.write.Label(5, i + int_fila + 1, "TOTAL " + fila[0] + ": ", formato_celda_totales);
				hoja_xls.addCell(lab_tot);

				for (int j = 0; j < lis_nom_columnas.size(); j++) {
					jxl.write.Number num = new jxl.write.Number(j + 6, i + int_fila + 1, pckUtilidades.CConversion.CDbl_2(String.valueOf(fila[j + 2])), formato_celda_totales);
					hoja_xls.addCell(num);

					// lab_tot = new jxl.write.Label(j+5, i+int_fila+1,
					// fila[j+2]+"", formato_celda_totales);
					// hoja_xls.addCell(lab_tot);
				}

				int_fila = int_fila + 1;
			}
			int_fila = int_fila + 1 + lis_datos_rol.size();
			jxl.write.Label lab_tot = new jxl.write.Label(5, int_fila, "TOTAL CONSOLIDADO:", formato_celda_totales);
			hoja_xls.addCell(lab_tot);

			// lista que contiene los totales
			for (int j = 0; j < lis_totales_consolidado.size(); j++) {
				jxl.write.Number num = new jxl.write.Number(j + 6, int_fila, pckUtilidades.CConversion.CDbl_2(String.valueOf(lis_totales_consolidado.get(j))), formato_celda_totales);
				hoja_xls.addCell(num);
			}

			archivo_xls.write();
			archivo_xls.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom);
		} catch (Exception e) {
			System.out.println("Error no se genero el XLS :" + e.getMessage());
		}
	}

	public void aceptarRubros() {
		if (set_rubros.getSeleccionados() != null && !set_rubros.getSeleccionados().isEmpty()) {
			String IDE_GEPRO = com_periodo_rol.getValue() + "";
			IDE_NRRUB = set_rubros.getSeleccionados();
			lis_datos_rol.clear();
			lis_nom_columnas.clear();
			lis_totales_consolidado.clear();
			
			TablaGenerica tab_roles=utilitario.consultar("select ide_nrrol,ide_gepro from nrh_rol where ide_gepro in("+IDE_GEPRO+")");
			String roles="",ide_geedp="";
			for (int i = 0; i < tab_roles.getTotalFilas(); i++) {
				if ((tab_roles.getTotalFilas()-1)==i) {
					roles+=tab_roles.getValor(i,"IDE_NRROL");
				}else{
				roles+=tab_roles.getValor(i,"IDE_NRROL")+",";}
			}
			TablaGenerica tab_periodo=utilitario.consultar("select ide_gepro,tipo_rol from gen_perido_rol where ide_gepro in("+IDE_GEPRO+")");

			int tipoRol=ser_nomina.validarTipoRol(tab_periodo.getValor("tipo_rol"));
			
			if(tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra")) || 
			    tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_alimentacion"))    || 
				tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_tipo_nonima_horas_suplementarias"))
				|| tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_hxa"))) {
				
				

			
			TablaGenerica tab_rubros_empleado=utilitario.consultar("select a.ide_geedp,a.valor_nrdro  "
					+ "from (select ide_geedp,sum(valor_nrdro) as valor_nrdro from nrh_detalle_rol  "
					+ "where ide_nrrol in("+roles+")  "
					+ "and ide_nrder in(select ide_nrder from nrh_detalle_rubro where ide_nrrub in(255,256))  "
					+ "group by ide_geedp "
					+ " ORDER BY ide_geedp ASC) a "
					+ "where a.valor_nrdro=0.0 "
					+ "order by ide_geedp asc ");
					
			
			for (int i = 0; i < tab_rubros_empleado.getTotalFilas(); i++) {
				if ((tab_rubros_empleado.getTotalFilas()-1)==i) {
					ide_geedp+=tab_rubros_empleado.getValor(i,"ide_geedp");
				}else{
					ide_geedp+=tab_rubros_empleado.getValor(i,"ide_geedp")+",";}
			}

			
			
			
			/*utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol  "
					+ "where ide_nrrol in("+roles+") and ide_geedp in("+ide_geedp+")");*/
			
			System.out.println("delete from nrh_detalle_rol  "
					+ "where ide_nrrol in("+roles+") and ide_geedp in("+ide_geedp+")");}
			
			/*TablaGenerica tab_rubros=utilitario.consultar("select ide_nrder,ide_nrdtn from  nrh_detalle_rubro "
					+ "where ide_nrrub in(select ide_nrrub from nrh_rubro where ide_nrrub in (131)) and ide_nrdtn in("+IDE_NRDTN.toString()+")");
			String rubros="";
			for (int i = 0; i < tab_rubros.getTotalFilas(); i++) {
				if ((tab_rubros.getTotalFilas()-1)==i) {
					rubros+=tab_rubros.getValor(i,"IDE_NRDER");
				}else{
				rubros+=tab_rubros.getValor(i,"IDE_NRDER")+",";}
			}
			
			System.out.println(""+utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol  "
					+ "where ide_geedp in( "
					+ "select ide_geedp from nrh_detalle_rol  "
					+ "where ide_nrrol in("+roles+") "
					+ "and ide_nrder in("+rubros+") "
					+ "and valor_nrdro=0.0 group by ide_geedp) and ide_nrrol in("+roles+")"));
			
			
			
			
			utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol  "
					+ "where ide_geedp in( "
					+ "select ide_geedp from nrh_detalle_rol  "
					+ "where ide_nrrol in("+roles+") "
					+ "and ide_nrder in("+rubros+") "
					+ "and valor_nrdro=0.0 group by ide_geedp) and ide_nrrol in("+roles+")");*/
			
			llenarTabla(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB);
			set_det_tip_nomina.cerrar();
			set_rubros.cerrar();
			set_tipo_rubro.cerrar();

			dibujarTabla();
			utilitario.addUpdate("div_division");
		} else {
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Rubro");
		}
	}

	public void aceptarTipoRubro() {

		if (set_tipo_rubro.getSeleccionados() != null && !set_tipo_rubro.getSeleccionados().isEmpty()) {
			String IDE_GEPRO = com_periodo_rol.getValue() + "";
			String IDE_NRTIR = set_tipo_rubro.getSeleccionados();

			TablaGenerica tab_rub_sel = utilitario.consultar("select RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER from NRH_RUBRO RUB " + "INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " + "WHERE DER.IDE_NRDTN IN (" + IDE_NRDTN
					+ ") AND IMPRIME_NRDER=true and IDE_NRTIR in (" + IDE_NRTIR + ") " + "GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER " + "ORDER BY ORDEN_IMPRIME_NRDER ASC ");

			String ide_nrrub = "";
			for (int i = 0; i < tab_rub_sel.getTotalFilas(); i++) {
				ide_nrrub += tab_rub_sel.getValor(i, "IDE_NRRUB") + ",";
			}
			try {
				ide_nrrub = ide_nrrub.substring(0, ide_nrrub.length() - 1);
			} catch (Exception e) {
				// TODO: handle exception
			}

			set_rubros.getTab_seleccion().setSql(ser_nomina.getRubrosOrden(IDE_NRDTN, ide_nrrub).getSql());
			set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
			set_rubros.getTab_seleccion().ejecutarSql();
			System.out.println("rubros " + set_rubros.getTab_seleccion().getSql());
			set_rubros.dibujar();
		} else {
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Tipo de Rubro");
		}
	}

	public void cargarTablaVacia() {
		lis_datos_rol.clear();
		lis_nom_columnas.clear();
		lis_totales_consolidado.clear();
		llenarTabla(null, null, null);
		dibujarTabla();
	}

	public String getCondicionSqlTablaVirtual(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		String str_sql_condicion_tabla_virtual = " from ( " + "select IDE_SUCU,sucursal,ide_gtemp,empleado " + "from ( " + "SELECT " + "SUCU.IDE_SUCU,EMP.IDE_GTEMP,sucu.nom_sucu as sucursal,dep.detalle_gedep as departamento, "
				+ "EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| "
				+ "EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS EMPLEADO, " + "EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, " + "funcional.detalle_gecaf as cargo, "
				+ "contrato.detalle_gttco as contrato, " + "RUB.DETALLE_NRRUB AS RUBROS, " + "derubro.orden_nrder,rub.ide_nrrub, " + "DETA.VALOR_NRDRO AS MONTO " + "FROM NRH_DETALLE_ROL DETA " + "LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL "
				+ "LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " + "LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP "
				+ "LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " + "LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " + "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU "
				+ "LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " + "LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " + "LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO "
				+ "LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " + "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE "
				+ "LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " + "LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR " + "LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO "
				+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " + "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " + "WHERE detatipono.ide_nrdtn IN (" + IDE_NRDTN + ") " + "AND ROL.IDE_GEPRO IN (" + IDE_GEPRO + ") "
				+ "AND RUB.IDE_NRRUB IN (" + IDE_NRRUB + ") " + "order by sucu.nom_sucu DESC,EMPLEADO ,derubro.orden_nrder " + ") a " + "group by IDE_SUCU,sucursal,ide_gtemp,empleado " + ") a";
		return str_sql_condicion_tabla_virtual;
	}

	public String getSqlNominaGlobalEmpleados(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		String str_sql = "SELECT SUCU.IDE_SUCU,EMP.IDE_GTEMP, "
				+ "sucu.nom_sucu as sucursal,dep.detalle_gedep as departamento, "
				+ "EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| "
				+ "EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS EMPLEADO, "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, "
				+ "funcional.detalle_gecaf as cargo, DENOMINACION.titulo_cargo_gepgc AS DENOMINACION, "
				+ "CONTRATO.detalle_gttco as contrato, "
				+ " (case when EMP.ide_gtgen=1 then 'M' else 'F' end) as genero, "
				+ " (case when coalesce(EMP.discapacitado_gtemp,false)=true then 'SI' else 'NO' end) as discapacidad, "
				+ " (case when coalesce(EMP.acumula_decimo_gtemp,false)=true then 'SI' else 'NO' end) as acumula_decimo, "
				// "replace(replace(replace(replace(DETALLE_NRRUB,' ','_'),'.',''),'%',''),'-','') as rubros, "+
				+"DETALLE_NRRUB as rubros, "
				+
				// "derubro.orden_imprime_nrder, " +
				"rub.ide_nrrub, " + "sum (DETA.VALOR_NRDRO) AS MONTO "
				+ "FROM NRH_DETALLE_ROL DETA "
				+ "LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL "
				+ "LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN "
				+ "LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP "
				+ "LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO "
				+ "INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM "
				+
				// "AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " + "LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " + "LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " + "LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " + "LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " + "LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR "
				+ "LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " + "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " + "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
				+ "LEFT JOIN gen_partida_grupo_cargo DENOMINACION ON DENOMINACION.ide_gepgc = PAR.ide_gepgc " + "WHERE detatipono.ide_nrdtn IN (" + IDE_NRDTN + ") " + "AND ROL.IDE_GEPRO IN (" + IDE_GEPRO + ") " + "AND RUB.IDE_NRRUB IN (" + IDE_NRRUB
				+ ") " + "GROUP BY SUCU.IDE_SUCU,EMP.IDE_GTEMP, sucu.nom_sucu, " + "dep.detalle_gedep , " + "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP , " + "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP , funcional.detalle_gecaf, DENOMINACION.titulo_cargo_gepgc, " + "CONTRATO.detalle_gttco , DETALLE_NRRUB , " +
				// "derubro.orden_imprime_nrder, " +
				" rub.ide_nrrub, DETA.VALOR_NRDRO " +
				//+ "HAVING DETA.VALOR_NRDRO!=0 " +
				// "order by sucursal DESC,EMPLEADO,derubro.orden_imprime_nrder ";
				"order by sucursal DESC,EMPLEADO ";
		
		System.out.println("getSqlNominaGlobalEmpleados "+str_sql);
		return str_sql;
	}

	public int getIndiceRubro(String nom_rubro) {
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			if (String.valueOf(lis_nom_columnas.get(i)).equalsIgnoreCase(nom_rubro)) {
				return (i + 6);
			}
		}
		return -1;
	}

	// public List llenarTabla(String IDE_NRDTN,String IDE_GEPRO,String
	// IDE_NRRUB){
	// if (IDE_NRDTN!=null && !IDE_NRDTN.isEmpty()
	// && IDE_GEPRO!=null && !IDE_GEPRO.isEmpty()
	// && IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){
	//
	// Tabla tab_rep_global=utilitario.consultar(getSqlNominaGlobal(IDE_NRDTN,
	// IDE_GEPRO, IDE_NRRUB));
	// lis_nom_columnas=new ArrayList();
	// lis_datos_rol = new ArrayList<Object>();
	// System.out.println("sql rep_global "+tab_rep_global.getSql());
	//
	// if (tab_rep_global.getTotalFilas()>0){
	// Tabla tab_rubros=ser_nomina.getRubro(IDE_NRRUB);
	// for (int i = 0; i < tab_rubros.getTotalFilas(); i++) {
	// lis_nom_columnas.add(tab_rubros.getValor(i, "DETALLE_NRRUB"));
	// }
	//
	// List<Object> lisq = new ArrayList<Object>();
	// Object [] obj_columnas=new Object[5+lis_nom_columnas.size()];
	// int int_count_objeto=5;
	//
	// obj_columnas[0]=tab_rep_global.getValor(0, "DEPARTAMENTO");
	// obj_columnas[1]=tab_rep_global.getValor(0, "CEDULA");
	// obj_columnas[2]=tab_rep_global.getValor(0, "EMPLEADO");
	// obj_columnas[3]=tab_rep_global.getValor(0, "CARGO");
	// obj_columnas[4]=tab_rep_global.getValor(0, "CONTRATO");
	//
	// for (int i = 0; i < lis_nom_columnas.size(); i++) {
	// obj_columnas[i+5]="0.00";
	// }
	//
	//
	// String str_ide_gtemp="";
	//
	// lis_datos_rol = new ArrayList<Object>();
	//
	//
	// double [] totales=new double[lis_nom_columnas.size()];
	// for (int i = 0; i < lis_nom_columnas.size(); i++) {
	// totales[i]=0;
	// }
	//
	// double [] totales_consolidado=new double[lis_nom_columnas.size()];
	// for (int i = 0; i < lis_nom_columnas.size(); i++) {
	// totales_consolidado[i]=0;
	// }
	// int int_indice_totales_c=0;
	// int int_indice_totales=0;
	//
	//
	// String str_ide_sucu="";
	// if (tab_rep_global.getTotalFilas()>0){
	// str_ide_gtemp=tab_rep_global.getValor(0, "IDE_GTEMP");
	// str_ide_sucu=tab_rep_global.getValor(0, "IDE_SUCU");
	// }
	// int bandera=0;
	// int int_indice_sucursal=0;
	// for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
	// if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i,
	// "IDE_SUCU"))){
	//
	// if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i,
	// "IDE_GTEMP"))){
	// try {
	//
	// BigDecimal big_valor=new
	// BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	// int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	// if (indice>-1){
	// obj_columnas[indice]=""+big_valor;
	// }
	// // obj_columnas[int_count_objeto]=""+big_valor;
	// // int_count_objeto=int_count_objeto+1;
	// totales[int_indice_totales]=totales[int_indice_totales]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// } catch (Exception e) {
	// }
	// int_indice_totales=int_indice_totales+1;
	// int_indice_totales_c=int_indice_totales_c+1;
	// }else{
	// str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
	// lisq.add(obj_columnas);
	//
	// obj_columnas=new Object[5+lis_nom_columnas.size()];
	// obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
	// obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
	// obj_columnas[2]=tab_rep_global.getValor(i, "EMPLEADO");
	// obj_columnas[3]=tab_rep_global.getValor(i, "CARGO");
	// obj_columnas[4]=tab_rep_global.getValor(i, "CONTRATO");
	//
	// for (int k = 0; k < lis_nom_columnas.size(); k++) {
	// obj_columnas[k+5]="0.00";
	// }
	//
	// BigDecimal big_valor=new
	// BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	// int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	// if (indice>-1){
	// obj_columnas[indice]=""+big_valor;
	// }
	//
	//
	// // obj_columnas[int_count_objeto]=""+big_valor;
	// // int_count_objeto=int_count_objeto+1;
	//
	// int_indice_totales=0;
	// int_indice_totales_c=0;
	// try {
	// totales[int_indice_totales]=totales[int_indice_totales]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// } catch (Exception e) {
	// }
	// int_indice_totales=int_indice_totales+1;
	// int_indice_totales_c=int_indice_totales_c+1;
	// }
	// }else{
	// lisq.add(obj_columnas);
	// bandera=0;
	// str_ide_sucu=tab_rep_global.getValor(i, "IDE_SUCU");
	// int_indice_sucursal=i;
	// Object [] obj=new Object[2+totales.length];
	// obj[0]=tab_rep_global.getValor(i-1, "SUCURSAL");
	// obj[1]=lisq;
	// for (int j = 0; j < totales.length; j++) {
	// BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales[j]+""));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	// obj[j+2]=""+big_valor;
	// }
	// lis_datos_rol.add(obj);
	//
	// lisq = new ArrayList<Object>();
	// for (int k = 0; k < lis_nom_columnas.size(); k++) {
	// totales[k]=0;
	// }
	//
	// obj_columnas=new Object[5+lis_nom_columnas.size()];
	// obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
	// obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
	// obj_columnas[2]=tab_rep_global.getValor(i, "EMPLEADO");
	// obj_columnas[3]=tab_rep_global.getValor(i, "CARGO");
	// obj_columnas[4]=tab_rep_global.getValor(i, "CONTRATO");
	//
	// for (int k = 0; k < lis_nom_columnas.size(); k++) {
	// obj_columnas[k+5]="0.00";
	// }
	//
	// BigDecimal big_valor=new
	// BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	// int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	// if (indice>-1){
	// obj_columnas[indice]=""+big_valor;
	// }
	//
	// // obj_columnas[int_count_objeto]=""+big_valor;
	// // int_count_objeto=int_count_objeto+1;
	//
	// int_indice_totales=0;
	// int_indice_totales_c=0;
	// try {
	// totales[int_indice_totales]=totales[int_indice_totales]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i,
	// "MONTO"));
	// } catch (Exception e) {
	// }
	// int_indice_totales=int_indice_totales+1;
	// int_indice_totales_c=int_indice_totales_c+1;
	//
	// str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
	//
	// }
	//
	// }
	//
	// if (bandera==0){
	// lisq.add(obj_columnas);
	// Object [] obj=new Object[2+totales.length];
	// obj[0]=tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
	// obj[1]=lisq;
	// for (int j = 0; j < totales.length; j++) {
	// // BigDecimal big_valor=new BigDecimal(totales[j]);
	// BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales[j]+""));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	// obj[j+2]=""+big_valor;
	// }
	// lis_datos_rol.add(obj);
	// }
	// lis_totales_consolidado=new ArrayList();
	// for (int j = 0; j < totales_consolidado.length; j++) {
	// BigDecimal big_valor=new
	// BigDecimal(pckUtilidades.CConversion.CDbl_2(totales_consolidado[j]+""));
	// big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	// lis_totales_consolidado.add(""+big_valor);
	// }
	// return lis_datos_rol;
	// }else{
	// return null;
	// }
	//
	// }else{
	// return null;
	// }
	// }

	public boolean validarRubroRepetido(String nom_rubro) {
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			if (String.valueOf(lis_nom_columnas.get(i)).equalsIgnoreCase(nom_rubro)) {
				return false;
			}
		}
		return true;
	}

	public List llenarTabla(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		if (IDE_NRDTN != null && !IDE_NRDTN.isEmpty() && IDE_GEPRO != null && !IDE_GEPRO.isEmpty() && IDE_NRRUB != null && !IDE_NRRUB.isEmpty()) {

			TablaGenerica tab_rep_global = utilitario.consultar(getSqlNominaGlobalEmpleados(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB));
			lis_nom_columnas = new ArrayList();
			lis_datos_rol = new ArrayList<Object>();
			System.out.println("sql rep_global " + tab_rep_global.getSql());

			if (tab_rep_global.getTotalFilas() > 0) {

				TablaGenerica tab_rubros_agrupados = utilitario.consultar("select a.ide_nrrub,a.rubros from ( " + "" + tab_rep_global.getSql() + "" + ")a " + "group by a.ide_nrrub,a.rubros ");

				String ide_nrrub = "";
				for (int i = 0; i < tab_rubros_agrupados.getTotalFilas(); i++) {
					ide_nrrub += tab_rubros_agrupados.getValor(i, "IDE_NRRUB") + ",";
				}
				try {
					ide_nrrub = ide_nrrub.substring(0, ide_nrrub.length() - 1);
				} catch (Exception e) {
					// TODO: handle exception
				}

				TablaGenerica tab_rubros_orden = ser_nomina.getRubrosOrden(IDE_NRDTN, ide_nrrub);

				for (int i = 0; i < tab_rubros_orden.getTotalFilas(); i++) {
					if (validarRubroRepetido(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"))) {
						lis_nom_columnas.add(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"));
					}
				}

				List<Object> lisq = new ArrayList<Object>();
				Object[] obj_columnas = new Object[6 + lis_nom_columnas.size()];

				obj_columnas[0] = tab_rep_global.getValor(0, "DEPARTAMENTO");
				obj_columnas[1] = tab_rep_global.getValor(0, "CEDULA");
				obj_columnas[2] = tab_rep_global.getValor(0, "EMPLEADO");
				obj_columnas[3] = tab_rep_global.getValor(0, "genero");
				obj_columnas[4] = tab_rep_global.getValor(0, "discapacidad");
				obj_columnas[5] = tab_rep_global.getValor(0, "acumula_decimo");

				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					obj_columnas[i + 6] = "0.00";
				}

				String str_ide_gtemp = "";

				lis_datos_rol = new ArrayList<Object>();

				double[] totales = new double[lis_nom_columnas.size()];
				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					totales[i] = 0;
				}

				double[] totales_consolidado = new double[lis_nom_columnas.size()];
				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					totales_consolidado[i] = 0;
				}

				String str_ide_sucu = "";
				if (tab_rep_global.getTotalFilas() > 0) {
					str_ide_gtemp = tab_rep_global.getValor(0, "IDE_GTEMP");
					str_ide_sucu = tab_rep_global.getValor(0, "IDE_SUCU");
				}
				int bandera = 0;
				int int_indice_sucursal = 0;
				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
					if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_SUCU"))) {
						if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_GTEMP"))) {
							try {

								BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
								big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

								int indice = getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
								if (indice > -1) {
									obj_columnas[indice] = "" + big_valor;
									totales[indice - 6] = totales[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
									totales_consolidado[indice - 6] = totales_consolidado[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
								}
							} catch (Exception e) {
							}
						} else {
							str_ide_gtemp = tab_rep_global.getValor(i, "IDE_GTEMP");
							lisq.add(obj_columnas);

							obj_columnas = new Object[6 + lis_nom_columnas.size()];
							obj_columnas[0] = tab_rep_global.getValor(i, "DEPARTAMENTO");
							obj_columnas[1] = tab_rep_global.getValor(i, "CEDULA");
							obj_columnas[2] = tab_rep_global.getValor(i, "EMPLEADO");
							obj_columnas[3] = tab_rep_global.getValor(i, "genero");
							obj_columnas[4] = tab_rep_global.getValor(i, "discapacidad");
							obj_columnas[5] = tab_rep_global.getValor(i, "acumula_decimo");

							for (int k = 0; k < lis_nom_columnas.size(); k++) {
								obj_columnas[k + 6] = "0.00";
							}

							BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
							big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

							int indice = getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
							if (indice > -1) {
								obj_columnas[indice] = "" + big_valor;
								totales[indice - 6] = totales[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
								totales_consolidado[indice - 6] = totales_consolidado[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
							}
						}
					} else {
						lisq.add(obj_columnas);
						bandera = 0;
						str_ide_sucu = tab_rep_global.getValor(i, "IDE_SUCU");
						int_indice_sucursal = i;
						Object[] obj = new Object[2 + totales.length];
						obj[0] = tab_rep_global.getValor(i - 1, "SUCURSAL");
						obj[1] = lisq;
						for (int j = 0; j < totales.length; j++) {
							BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales[j] + ""));
							big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);
							obj[j + 2] = "" + big_valor;
						}
						lis_datos_rol.add(obj);

						lisq = new ArrayList<Object>();
						for (int k = 0; k < lis_nom_columnas.size(); k++) {
							totales[k] = 0;
						}

						obj_columnas = new Object[6 + lis_nom_columnas.size()];
						obj_columnas[0] = tab_rep_global.getValor(i, "DEPARTAMENTO");
						obj_columnas[1] = tab_rep_global.getValor(i, "CEDULA");
						obj_columnas[2] = tab_rep_global.getValor(i, "EMPLEADO");
						obj_columnas[3] = tab_rep_global.getValor(i, "genero");
						obj_columnas[4] = tab_rep_global.getValor(i, "discapacidad");
						obj_columnas[5] = tab_rep_global.getValor(i, "acumula_decimo");

						for (int k = 0; k < lis_nom_columnas.size(); k++) {
							obj_columnas[k + 6] = "0.00";
						}

						BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
						big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

						int indice = getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
						if (indice > -1) {
							obj_columnas[indice] = "" + big_valor;
							totales[indice - 6] = totales[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
							totales_consolidado[indice - 6] = totales_consolidado[indice - 6] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
						}
						str_ide_gtemp = tab_rep_global.getValor(i, "IDE_GTEMP");
					}

				}

				if (bandera == 0) {
					lisq.add(obj_columnas);
					Object[] obj = new Object[2 + totales.length];
					obj[0] = tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
					obj[1] = lisq;
					for (int j = 0; j < totales.length; j++) {
						// BigDecimal big_valor=new BigDecimal(totales[j]);
						BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales[j] + ""));
						big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);
						obj[j + 2] = "" + big_valor;
					}
					lis_datos_rol.add(obj);
				}
				lis_totales_consolidado = new ArrayList();
				for (int j = 0; j < totales_consolidado.length; j++) {
					BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales_consolidado[j] + ""));
					big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);
					lis_totales_consolidado.add("" + big_valor);
				}
				return lis_datos_rol;
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public void insertar() {
		// tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		guardarPantalla();
	}

	@Override
	public void eliminar() {
	}

	private ValueExpression crearValueExpression(String valueExpression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
	}

	private ValueExpression crearValueExpression(String valueExpression, Class c) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{" + valueExpression + "}", c);
	}

	public SeleccionTabla getSet_tipo_rubro() {
		return set_tipo_rubro;
	}

	public void setSet_tipo_rubro(SeleccionTabla set_tipo_rubro) {
		this.set_tipo_rubro = set_tipo_rubro;
	}

	public SeleccionTabla getSet_rubros() {
		return set_rubros;
	}

	public void setSet_rubros(SeleccionTabla set_rubros) {
		this.set_rubros = set_rubros;
	}

	public List getLis_nom_columnas() {
		return lis_nom_columnas;
	}

	public void setLis_nom_columnas(List lis_nom_columnas) {
		this.lis_nom_columnas = lis_nom_columnas;
	}

	public List<Object> getLis_datos_rol() {
		return lis_datos_rol;
	}

	public void setLis_datos_rol(List<Object> lis_datos_rol) {
		this.lis_datos_rol = lis_datos_rol;
	}

	public DataTable getTabla() {
		return tabla;
	}

	public void setTabla(DataTable tabla) {
		this.tabla = tabla;
	}

	public SeleccionTabla getSet_det_tip_nomina() {
		return set_det_tip_nomina;
	}

	public void setSet_det_tip_nomina(SeleccionTabla set_det_tip_nomina) {
		this.set_det_tip_nomina = set_det_tip_nomina;
	}

	public List getLis_totales_consolidado() {
		return lis_totales_consolidado;
	}

	public void setLis_totales_consolidado(List lis_totales_consolidado) {
		this.lis_totales_consolidado = lis_totales_consolidado;
	}

	public Dialogo getDia_formula() {
		return dia_formula;
	}

	public void setDia_formula(Dialogo dia_formula) {
		this.dia_formula = dia_formula;
	}

	public AutoCompletar getAut_rubros_tip_formula() {
		return aut_rubros_tip_formula;
	}

	public void setAut_rubros_tip_formula(AutoCompletar aut_rubros_tip_formula) {
		this.aut_rubros_tip_formula = aut_rubros_tip_formula;
	}

	public List getLis_nom_columnas_orden() {
		return lis_nom_columnas_orden;
	}

	public void setLis_nom_columnas_orden(List lis_nom_columnas_orden) {
		this.lis_nom_columnas_orden = lis_nom_columnas_orden;
	}

	public List<Object> getLis_datos_rol_orden() {
		return lis_datos_rol_orden;
	}

	public void setLis_datos_rol_orden(List<Object> lis_datos_rol_orden) {
		this.lis_datos_rol_orden = lis_datos_rol_orden;
	}

	public boolean validarRubroRepetidoOrden(String nom_rubro) {
		for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
			if (String.valueOf(lis_nom_columnas_orden.get(i)).equalsIgnoreCase(nom_rubro)) {
				return false;
			}
		}
		return true;
	}	
	
	
	/**
	 * Método recibe
	 * @param IDE_NRDTN : tipo nomina (2,4)LOEP Y CODIGO DE RABAJO
	 * @param IDE_GEPRO: PERIODO DEL ROL
	 * @param IDE_NRRUB:  RUBROS GENERADOS EN NOMINA
	 * @return devuelve un string con todos los empleados generados de acuerdo a los parametros ingresados 
	 */
	public String getSqlNominaGlobalEmpleadosOrden(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		String str_sql = "SELECT SUCU.IDE_SUCU,EMP.IDE_GTEMP, "
				+ "sucu.nom_sucu as sucursal,dep.detalle_gedep as departamento, "
				+ "EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| "
				+ "EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS EMPLEADO, "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, "
				+ "funcional.detalle_gecaf as cargo, DENOMINACION.titulo_cargo_gepgc AS DENOMINACION, "
				+ "CONTRATO.detalle_gttco as contrato, "
				//+ " (case when EMP.ide_gtgen=1 then 'M' else 'F' end) as genero, "
				//+ " (case when coalesce(EMP.discapacitado_gtemp,false)=true then 'SI' else 'NO' end) as discapacidad, "
				//+ " (case when coalesce(EMP.acumula_decimo_gtemp,false)=true then 'SI' else 'NO' end) as acumula_decimo, "
				// "replace(replace(replace(replace(DETALLE_NRRUB,' ','_'),'.',''),'%',''),'-','') as rubros, "+
				+"DETALLE_NRRUB as rubros, "
				+
				// "derubro.orden_imprime_nrder, " +
				"rub.ide_nrrub, " + "sum (DETA.VALOR_NRDRO) AS MONTO "
				+ "FROM NRH_DETALLE_ROL DETA "
				+ "LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL "
				+ "LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN "
				+ "LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP "
				+ "LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO "
				+ "INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM "
				+
				// "AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " + "LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " + "LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " + "LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " + "LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " + "LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR "
				+ "LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " + "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " + "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
				+ "LEFT JOIN gen_partida_grupo_cargo DENOMINACION ON DENOMINACION.ide_gepgc = PAR.ide_gepgc " + "WHERE detatipono.ide_nrdtn IN (" + IDE_NRDTN + ") " + "AND ROL.IDE_GEPRO IN (" + IDE_GEPRO + ") " + "AND RUB.IDE_NRRUB IN (" + IDE_NRRUB
				+ ") " + "GROUP BY SUCU.IDE_SUCU,EMP.IDE_GTEMP, sucu.nom_sucu, " + "dep.detalle_gedep , " + "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP , " + "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP , funcional.detalle_gecaf, DENOMINACION.titulo_cargo_gepgc, " + "CONTRATO.detalle_gttco , DETALLE_NRRUB , " +
				// "derubro.orden_imprime_nrder, " +
				" rub.ide_nrrub, DETA.VALOR_NRDRO " +
				//+ "HAVING DETA.VALOR_NRDRO!=0 " +
				// "order by sucursal DESC,EMPLEADO,derubro.orden_imprime_nrder ";
				"order by EMPLEADO ASC ";
		
		System.out.println("getSqlNominaGlobalEmpleadosOrden "+str_sql);
		return str_sql;
	}	
	
	
	/**
	 * Metodo que genera en una lista de objetos con los datos de los empleados y sus rubros 
	 * @param IDE_NRDTN: tipo de nomina
	 * @param IDE_GEPRO: periodo del rol
	 * @param IDE_NRRUB: rubros que intervienen
	 * @return  informacion de los empleados con sus rubros 
	 */
	public List llenarTablaOrden(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		if (IDE_NRDTN != null && !IDE_NRDTN.isEmpty() && IDE_GEPRO != null && !IDE_GEPRO.isEmpty() && IDE_NRRUB != null && !IDE_NRRUB.isEmpty()) {
int tipo=1;
			TablaGenerica tab_rep_global = utilitario.consultar(getSqlNominaGlobalEmpleadosOrden(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB));

			//
			lis_nom_columnas_orden = new ArrayList();
			lis_datos_rol_orden = new ArrayList<Object>();
			int contadorEmpleado=1;
			System.out.println("sql rep_global " + tab_rep_global.getSql());

			if (tab_rep_global.getTotalFilas() > 0) {

				TablaGenerica tab_rubros_agrupados = utilitario.consultar("select a.ide_nrrub,a.rubros from ( " + "" + tab_rep_global.getSql() + "" + ")a " + "group by a.ide_nrrub,a.rubros ");

				String ide_nrrub = "";
				for (int i = 0; i < tab_rubros_agrupados.getTotalFilas(); i++) {
					ide_nrrub += tab_rubros_agrupados.getValor(i, "IDE_NRRUB") + ",";
				}
				try {
					ide_nrrub = ide_nrrub.substring(0, ide_nrrub.length() - 1);
				} catch (Exception e) {
					// TODO: handle exception
				}

				TablaGenerica tab_rubros_orden = ser_nomina.getRubrosOrden(IDE_NRDTN, ide_nrrub);

				for (int i = 0; i < tab_rubros_orden.getTotalFilas(); i++) {
					if (validarRubroRepetidoOrden(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"))) {
						lis_nom_columnas_orden.add(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"));
					}
				}

				
				
				List<Object> lisq_orden = new ArrayList<Object>();
				Object[] obj_columnas_orden ;
			//	if (tipo==1) {
				//	obj_columnas_orden = new Object[2 + lis_nom_columnas_orden.size()];
					//obj_columnas[0] = tab_rep_global.getValor(0, "DEPARTAMENTO");
				//	obj_columnas_orden[0] = tab_rep_global.getValor(0, "CEDULA");
				//	obj_columnas_orden[1] = tab_rep_global.getValor(0, "EMPLEADO");


					//for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
					//	obj_columnas_orden[i + 2] = "0.00";
					//}
				//}else{
				    //contadorEmpleado++;
					obj_columnas_orden = new Object[3 + lis_nom_columnas_orden.size()];
				//obj_columnas[0] = tab_rep_global.getValor(0, "DEPARTAMENTO");
					obj_columnas_orden[0] =contadorEmpleado;//tab_rep_global.getValor(0, "Nº");
					obj_columnas_orden[1] = tab_rep_global.getValor(1, "CEDULA");
					obj_columnas_orden[2] = tab_rep_global.getValor(2, "EMPLEADO");
					//obj_columnas_orden[2] = tab_rep_global.getValor(0, "genero");
					//obj_columnas_orden[3] = tab_rep_global.getValor(0, "discapacidad");
					//obj_columnas_orden[4] = tab_rep_global.getValor(0, "acumula_decimo");

				for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
						obj_columnas_orden[i + 3] = "0.00";
					//}
				}

				
		

				String str_ide_gtemp = "";

				lis_datos_rol_orden = new ArrayList<Object>();

			
				double[] totales_consolidado = new double[lis_nom_columnas_orden.size()];
				for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
					totales_consolidado[i] = 0;
				}

				if (tab_rep_global.getTotalFilas() > 0) {
					str_ide_gtemp = tab_rep_global.getValor(0, "IDE_GTEMP");
				}
				
				int bandera = 0;
				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
					//if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_SUCU"))) {
						if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_GTEMP"))) {
							try {
								BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
								big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

								int indice = getIndiceRubroOrden(tab_rep_global.getValor(i, "RUBROS"));
								if (indice > -1) {
									obj_columnas_orden[indice] = "" + big_valor;
										totales_consolidado[indice - 3] = totales_consolidado[indice - 3] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
								}
							} catch (Exception e) {
							}
							
							if (i==(tab_rep_global.getTotalFilas()-1)) {
								lisq_orden.add(obj_columnas_orden);
							}
							
						} else {
							contadorEmpleado++;
							str_ide_gtemp = tab_rep_global.getValor(i, "IDE_GTEMP");
							lisq_orden.add(obj_columnas_orden);
								obj_columnas_orden = new Object[3 + lis_nom_columnas_orden.size()];
								obj_columnas_orden[0] = contadorEmpleado;
								obj_columnas_orden[1] = tab_rep_global.getValor(i, "CEDULA");
								obj_columnas_orden[2] = tab_rep_global.getValor(i, "EMPLEADO");

							for (int k = 0; k < lis_nom_columnas_orden.size(); k++) {
									obj_columnas_orden[k + 3] = "0.00";
							}

							BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
							big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

							int indice = getIndiceRubroOrden(tab_rep_global.getValor(i, "RUBROS"));
							if (indice > -1) {
								obj_columnas_orden[indice] = "" + big_valor;
									totales_consolidado[indice - 3] = totales_consolidado[indice - 3] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
							}							
						}
					}

				if (bandera == 0) {
					//lisq_orden.add(obj_columnas_orden);
					Object[] obj = new Object[3];
					//obj[0] = tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
					obj[1] = lisq_orden;
					lis_datos_rol_orden.add(obj);
				}

				lis_totales_consolidado = new ArrayList();
				for (int j = 0; j < totales_consolidado.length; j++) {
					BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales_consolidado[j] + ""));
					big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);
					lis_totales_consolidado.add("" + big_valor);
				}
				return lis_datos_rol_orden;
			} else {
				return null;
			}
 
		} else {
			return null;
		}
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////77

	
	
	/**
	 * Metodo que genera en una lista de objetos con los datos de los empleados y sus rubros 
	 * @param IDE_NRDTN: tipo de nomina
	 * @param IDE_GEPRO: periodo del rol
	 * @param IDE_NRRUB: rubros que intervienen
	 * @return  informacion de los empleados con sus rubros 
	 */
	public List llenarTablaOrdenRol(String IDE_NRDTN, String IDE_GEPRO, String IDE_NRRUB) {
		if (IDE_NRDTN != null && !IDE_NRDTN.isEmpty() && IDE_GEPRO != null && !IDE_GEPRO.isEmpty() && IDE_NRRUB != null && !IDE_NRRUB.isEmpty()) {

			TablaGenerica  tab_periodo=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+IDE_GEPRO);
			TablaGenerica tab_rep_global=null;
			String fecha_inicial_gepro="",ide_gepro_consolidado="",ide_gepro_normal="";
			fecha_inicial_gepro=tab_periodo.getValor("fecha_inicial_gepro");
				
		        double valorDecimoCuarto,valorDecimoTercero=0.0,valorFondosReserva=0.0,valorIessPersonal=0.0,valorIessPatronal=0.0,valorIessPersonalBase=0.0,
		        		nro_horas_suplementarias_50=0.0;
		        int mes =0,mes_aux=0,anio=0,anio_aux=0;
		        String ide_gepro="",ide_nrdtn="",ide_gepro_extra="",ide_nrdtn_extra="",ide_gepro_fondos="",ide_nrdtn_fondos="",ide_nrdtn_normal="",ide_nrdtn_consolidado="",ide_gepro_alimentacion="";
		

		        ide_gepro_normal=IDE_GEPRO;
		        ide_nrdtn_normal=IDE_NRDTN;
		        
		        mes_aux=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"));
		        mes=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"));
		        //anio periodo rol
		        anio_aux=utilitario.getAnio(tab_periodo.getValor("fecha_inicial_gepro"));
		       
		        //Valido el mes de rol
		        //if (mes_aux==12) {
		        	//si es 12 le sumo uno
				//	mes=1;
					//al anio le sumo uno
				//  anio=anio_aux+1;
		        //}else {
				//	mes=mes+1;
				//	anio=anio_aux;
				//}	
		        
		        
		        TablaGenerica tab_anio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%'");
		        anio=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_geani"));
		        
			    /*    
		        //Rol tipo HORAS EXTRA
		        TablaGenerica tab_periodo_roles=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		       // + "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(9) order by tipo_rol desc ");
		 		+ "where ide_geani="+anio+"  and ide_gemes="+pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"))+" and tipo_rol in(9) order by tipo_rol desc ");
		        
		      //Rol tipo FONDOS DE RESERVA
		        TablaGenerica tab_anio_fondos =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio_aux+"%'");
		        TablaGenerica tab_fondos_reserva=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		+ "where ide_geani="+tab_anio.getValor("ide_geani")+"  and ide_gemes="+mes+" and tipo_rol in(7) order by tipo_rol desc ");
		        
		        TablaGenerica tab_alimentacion=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");
		
		        */
		       
		        
		    /*    TablaGenerica tab_periodo_roles=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		       // + "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(9) order by tipo_rol desc ");
		 		+ "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(9) order by tipo_rol desc ");
		        
		      //Rol tipo FONDOS DE RESERVA
		        TablaGenerica tab_anio_fondos =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio_aux+"%'");
		        TablaGenerica tab_fondos_reserva=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		+ "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(7) order by tipo_rol desc ");
		        
		        TablaGenerica tab_alimentacion=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		//+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");
		        		+ "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(11) order by tipo_rol desc ");
*/
		       
		        //Rol tipo HORAS EXTRA
		        TablaGenerica tab_periodo_roles=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		       // + "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(9) order by tipo_rol desc ");
		 		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(9,12) order by tipo_rol desc ");
		        
		      //Rol tipo FONDOS DE RESERVA
		        TablaGenerica tab_anio_fondos =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio_aux+"%'");
		        TablaGenerica tab_fondos_reserva=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(7) order by tipo_rol desc ");
		        
		        TablaGenerica tab_alimentacion=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
		        		//+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");
		        		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");	        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        

		        
		       
		        
				boolean estado=false;
				
				if (mes_aux==8 && anio_aux==2019) {
					ide_gepro=IDE_GEPRO;
					ide_gepro+=","+tab_fondos_reserva.getValor("IDE_GEPRO");	
				}else if(mes_aux<8 && anio_aux<=2019){ 
				ide_gepro=IDE_GEPRO;
				}else if(anio_aux<2019){
				ide_gepro=IDE_GEPRO;
				}else{
					ide_gepro=IDE_GEPRO+",";
					if (tab_periodo_roles.getValor("IDE_GEPRO")==null || tab_periodo_roles.getValor("IDE_GEPRO").equals("") || tab_periodo_roles.getValor("IDE_GEPRO").isEmpty()) {				     
					//Si no contiene un periodo de horas extra
					}else{
						//Si contiene horas extra le añade
	
						for (int i = 0; i < tab_periodo_roles.getTotalFilas(); i++) {
							if(i==(tab_periodo_roles.getTotalFilas()-1)) {
								ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO")+",";			
							}else{
							ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO")+",";			

						}
						}
						
						
						
						//ide_gepro+=","+tab_periodo_roles.getValor("IDE_GEPRO");			
					}
					
					
					if (tab_fondos_reserva.getValor("IDE_GEPRO")==null || tab_fondos_reserva.getValor("IDE_GEPRO").equals("") || tab_fondos_reserva.getValor("IDE_GEPRO").isEmpty()) {				     
							
						} else {
							ide_gepro_fondos=tab_fondos_reserva.getValor("IDE_GEPRO");
				   		ide_gepro+=ide_gepro_fondos;   
							}
					
					if (tab_alimentacion.getValor("IDE_GEPRO")==null || tab_alimentacion.getValor("IDE_GEPRO").equals("") || tab_alimentacion.getValor("IDE_GEPRO").isEmpty()) {				     
						
					} else {
						char ultimo_caracter;
						ultimo_caracter = ide_gepro.charAt(ide_gepro.length()-1);
						if (Character.toString(ultimo_caracter).equals(",")) {
							ide_gepro=ide_gepro.substring(0, ide_gepro.length() - 1);
						}else {
							
						}
						
						ide_gepro_alimentacion=tab_alimentacion.getValor("IDE_GEPRO");
						ide_gepro+=","+ide_gepro_alimentacion;   
						}
			
					char ultimo_caracter;
							ultimo_caracter = ide_gepro.charAt(ide_gepro.length()-1);
							if (Character.toString(ultimo_caracter).equals(",")) {
								ide_gepro=ide_gepro.substring(0, ide_gepro.length() - 1);
							}else {
								
							}
				}

				
				
				
				
		        ide_nrdtn=IDE_NRDTN;
		        TablaGenerica tab_detalle_tipo_nomina=null;
		        					        
		        
		        
		        if (mes_aux==8 && anio_aux==2019) {
		        for (int i = 0; i < tab_detalle_tipo_nomina.getTotalFilas(); i++) {
		           tab_detalle_tipo_nomina=utilitario.consultar("SELECT ide_nrdtn, ide_nrtin, ide_gttem, ide_gttco, ide_sucu, ide_nrtit, "
			        		+ "activo_nrdtn  "
			        		+ "FROM nrh_detalle_tipo_nomina  "
			        		+ "where ide_nrtin in(7) and  "
			        		+ "ide_gttem in(SELECT ide_gttem FROM nrh_detalle_tipo_nomina where ide_nrdtn in("+IDE_NRDTN+"))"
			        				+ " order by ide_nrdtn desc");
			    
		        	if (i==(tab_detalle_tipo_nomina.getTotalFilas()-1)) {
		        		ide_nrdtn+=","+tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
					}else{

		        	}
				}
		        
				}else if(mes_aux<8 && anio_aux<=2019){ 
					ide_nrdtn=IDE_NRDTN;	        
				}else if(anio_aux<2019){
					ide_nrdtn=IDE_NRDTN;
				}else{
				     tab_detalle_tipo_nomina=utilitario.consultar("SELECT ide_nrdtn, ide_nrtin, ide_gttem, ide_gttco, ide_sucu, ide_nrtit, "
				        		+ "activo_nrdtn  "
				        		+ "FROM nrh_detalle_tipo_nomina  "
				        		+ "where ide_nrtin in(7,9,11,12) and  "
				        		+ "ide_gttem in(SELECT ide_gttem FROM nrh_detalle_tipo_nomina where ide_nrdtn in("+IDE_NRDTN+"))"
				        				+ " order by ide_nrdtn desc");
				    
					
			        for (int i = 0; i < tab_detalle_tipo_nomina.getTotalFilas(); i++) {
			        	if (i==(tab_detalle_tipo_nomina.getTotalFilas()-1)) {
			        		ide_nrdtn+=","+tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
						}else{										
							ide_nrdtn+=","+tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
								}
					}					        	
		        	
		        	
		        }
				
				
				
				
				
				
				
								
		        
		        TablaGenerica TablaRubros=utilitario.consultar("SELECT "
		        		+ "ide_nrrub,ide_nrdtn "
		        		+ "FROM nrh_detalle_rubro "
		        		+ "WHERE IDE_NRDTN in(4)");
	
		        StringBuilder str_ide_nrrub =new StringBuilder();
		        
		        for (int i = 0; i < TablaRubros.getTotalFilas(); i++) {
					if (i==(TablaRubros.getTotalFilas()-1)) {
						str_ide_nrrub.append(TablaRubros.getValor(i,"IDE_NRRUB"));
					}else {
						str_ide_nrrub.append(TablaRubros.getValor(i,"IDE_NRRUB"));
						str_ide_nrrub.append(",");	
					}
		        	
				
		        }
		        
		        
		        
				
			//	IDE_NRDTN="";
				IDE_GEPRO="";
			//	IDE_NRDTN=ide_nrdtn;
				IDE_GEPRO=ide_gepro;
				IDE_NRRUB="";
				IDE_NRRUB=str_ide_nrrub.toString();
				
				tab_rep_global = utilitario.consultar(getSqlNominaGlobalEmpleadosOrden(ide_nrdtn, ide_gepro, IDE_NRRUB));
			
			
			
			lis_nom_columnas_orden = new ArrayList();
			lis_datos_rol_orden = new ArrayList<Object>();
			System.out.println("sql rep_global " + tab_rep_global.getSql());

			if (tab_rep_global.getTotalFilas() > 0) {
/*
				TablaGenerica tab_rubros_agrupados = utilitario.consultar("select a.ide_nrrub,a.rubros from ( " + "" + tab_rep_global.getSql() + "" + ")a " + "group by a.ide_nrrub,a.rubros ");

				String ide_nrrub = "";
				for (int i = 0; i < tab_rubros_agrupados.getTotalFilas(); i++) {
					ide_nrrub += tab_rubros_agrupados.getValor(i, "IDE_NRRUB") + ",";
				}
				try {
					ide_nrrub = ide_nrrub.substring(0, ide_nrrub.length() - 1);
				} catch (Exception e) {
					// TODO: handle exception
				}*/

				TablaGenerica tab_rubros_orden = ser_nomina.getRubrosOrden("4,25,24,23,22,19,18", IDE_NRRUB);

				for (int i = 0; i < tab_rubros_orden.getTotalFilas(); i++) {
					//if (validarRubroRepetidoOrden(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"))) {
						lis_nom_columnas_orden.add(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"));
					//}
				}

				List<Object> lisq_orden = new ArrayList<Object>();
				Object[] obj_columnas_orden = new Object[5 + lis_nom_columnas_orden.size()];

				//obj_columnas[0] = tab_rep_global.getValor(0, "DEPARTAMENTO");
				obj_columnas_orden[0] = tab_rep_global.getValor(0, "CEDULA");
				obj_columnas_orden[1] = tab_rep_global.getValor(0, "EMPLEADO");
				obj_columnas_orden[2] = tab_rep_global.getValor(0, "genero");
				obj_columnas_orden[3] = tab_rep_global.getValor(0, "discapacidad");
				obj_columnas_orden[4] = tab_rep_global.getValor(0, "acumula_decimo");

				for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
					obj_columnas_orden[i + 5] = "0.00";
				}

				String str_ide_gtemp = "";

				lis_datos_rol_orden = new ArrayList<Object>();

			
				double[] totales_consolidado = new double[lis_nom_columnas_orden.size()];
				for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
					totales_consolidado[i] = 0;
				}

				if (tab_rep_global.getTotalFilas() > 0) {
					str_ide_gtemp = tab_rep_global.getValor(0, "IDE_GTEMP");
				}
				
				int bandera = 0;
				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
					//if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_SUCU"))) {
						if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_GTEMP"))) {
							try {

								BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
								big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

								int indice = getIndiceRubroOrden(tab_rep_global.getValor(i, "RUBROS"));
								if (indice > -1) {
									obj_columnas_orden[indice] = "" + big_valor;
									totales_consolidado[indice - 5] = totales_consolidado[indice - 5] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
								}
							} catch (Exception e) {
							}
							
							if (i==(tab_rep_global.getTotalFilas()-1)) {
								lisq_orden.add(obj_columnas_orden);
							}
							
						} else {
							str_ide_gtemp = tab_rep_global.getValor(i, "IDE_GTEMP");
							lisq_orden.add(obj_columnas_orden);
							obj_columnas_orden = new Object[5 + lis_nom_columnas_orden.size()];
							obj_columnas_orden[0] = tab_rep_global.getValor(i, "CEDULA");
							obj_columnas_orden[1] = tab_rep_global.getValor(i, "EMPLEADO");
							obj_columnas_orden[2] = tab_rep_global.getValor(i, "genero");
							obj_columnas_orden[3] = tab_rep_global.getValor(i, "discapacidad");
							obj_columnas_orden[4] = tab_rep_global.getValor(i, "acumula_decimo");

							for (int k = 0; k < lis_nom_columnas_orden.size(); k++) {
								obj_columnas_orden[k + 5] = "0.00";
							}

							BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO")));
							big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);

							int indice = getIndiceRubroOrden(tab_rep_global.getValor(i, "RUBROS"));
							if (indice > -1) {
								obj_columnas_orden[indice] = "" + big_valor;
								totales_consolidado[indice - 5] = totales_consolidado[indice - 5] + pckUtilidades.CConversion.CDbl_2(tab_rep_global.getValor(i, "MONTO"));
							}							
						}
					}

				if (bandera == 0) {
					//lisq_orden.add(obj_columnas_orden);
					Object[] obj = new Object[2];
					//obj[0] = tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
					obj[1] = lisq_orden;
					lis_datos_rol_orden.add(obj);
				}

				lis_totales_consolidado = new ArrayList();
				for (int j = 0; j < totales_consolidado.length; j++) {
					BigDecimal big_valor = new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales_consolidado[j] + ""));
					big_valor = big_valor.setScale(2, RoundingMode.HALF_UP);
					lis_totales_consolidado.add("" + big_valor);
				}
				return lis_datos_rol_orden;
			} else {
				return null;
			}
 
		} else {
			return null;
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////77
	
	
	

	public void exportarNominaExcelRol() {
		
		String IDE_GEPRO = com_periodo_rol.getValue() + "";
		//IDE_NRRUB = set_rubros.getSeleccionados();
		
		
		
		
		lis_datos_rol_orden_rol_cambio.clear();
		lis_datos_rol_orden.clear();
		lis_nom_columnas_orden.clear();
		lis_totales_consolidado.clear();
		
		/*if (IDE_GEPRO.equals("") || IDE_GEPRO.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar el Periodo Rol");
			return;
		}
		
		if (IDE_NRDTN.equals("") || IDE_NRDTN.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar el Tipo de Nomina");
			return;
		}
		
	
		if (IDE_NRRUB.equals("") || IDE_NRRUB.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar los Rubros a Generar");
			return;
		}
		
		if (lis_datos_rol_orden.size()==0) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "No se contiene Datos");
			return;
		}
		
		
		if (lis_nom_columnas_orden.size()==0) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "No existen rubros");
			return;
		}*/
		
		
		
		
		llenarTablaOrdenRol(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB);

		if (lis_datos_rol_orden.size() > 0) {
			if (lis_nom_columnas_orden.size() > 0) {
				String ide_gemes = ser_nomina.getPeriodoRol(com_periodo_rol.getValue() + "").getValor("IDE_GEMES");
				exportarXLSOrden("nominaOrden.xls", tipo_nomina, ide_gemes,0);
			}
		}
	}

	
	
		public void exportarNominaExcelOrden() {
		String IDE_GEPRO = com_periodo_rol.getValue() + "";
		//IDE_NRRUB = set_rubros.getSeleccionados();

	
		lis_datos_rol_orden_rol_cambio.clear();
		lis_datos_rol_orden.clear();
		lis_nom_columnas_orden.clear();
		lis_totales_consolidado.clear();
		
		/*if (IDE_GEPRO.equals("") || IDE_GEPRO.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar el Periodo Rol");
			return;
		}
		
		if (IDE_NRDTN.equals("") || IDE_NRDTN.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar el Tipo de Nomina");
			return;
		}
		
	
		if (IDE_NRRUB.equals("") || IDE_NRRUB.isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "Debe seleccionar los Rubros a Generar");
			return;
		}
		
		if (lis_datos_rol_orden.size()==0) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "No se contiene Datos");
			return;
		}
		
		
		if (lis_nom_columnas_orden.size()==0) {
			utilitario.agregarMensajeInfo("No se puede generar el reporte nomina ", "No existen rubros");
			return;
		}*/
		
		llenarTablaOrden(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB);
//Borrar datos en cero
			


		if (lis_datos_rol_orden.size() > 0) {
			if (lis_nom_columnas_orden.size() > 0) {
				String ide_gemes = ser_nomina.getPeriodoRol(com_periodo_rol.getValue() + "").getValor("IDE_GEMES");
				exportarXLSOrden("nominaOrden.xls", tipo_nomina, ide_gemes,1);
			}
		}
	}

/*	public void exportarXLSOrden(String nombre, String tipo_nomina, String mes,int tipo) {
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String nom = nombre;
			File result = new File(extContext.getRealPath("/" + nom));
			WritableWorkbook archivo_xls = Workbook.createWorkbook(result);
			WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0);
			WritableFont fuente = new WritableFont(WritableFont.TAHOMA, 10);
			WritableCellFormat formato_celda = new WritableCellFormat(fuente);
			formato_celda.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda.setOrientation(Orientation.HORIZONTAL);
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_suc = new WritableCellFormat(fuente_suc);
			formato_celda_suc.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda_suc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_suc.setOrientation(Orientation.HORIZONTAL);
			formato_celda_suc.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_totales = new WritableCellFormat(fuente_suc);
			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_totales.setOrientation(Orientation.HORIZONTAL);
			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.RED);

			WritableCellFormat formato_celda_valor_rubro = new WritableCellFormat(fuente);
			formato_celda_valor_rubro.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_valor_rubro.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_valor_rubro.setOrientation(Orientation.HORIZONTAL);
			formato_celda_valor_rubro.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			int int_columna = 0;
			int int_fila = 4;

			jxl.write.Label lab_titulo = new jxl.write.Label(0, 0, "NOMINA: ", formato_celda_suc);
			hoja_xls.addCell(lab_titulo);
			CellView cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_tip_nomina = new jxl.write.Label(1, 0, tipo_nomina, formato_celda_suc);
			hoja_xls.addCell(lab_tip_nomina);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			jxl.write.Label lab_mes = new jxl.write.Label(0, 1, "MES: ", formato_celda_suc);
			hoja_xls.addCell(lab_mes);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_mes1 = new jxl.write.Label(1, 1, mes, formato_celda_suc);
			hoja_xls.addCell(lab_mes1);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			for (int i = 0; i < lis_datos_rol_orden.size(); i++) {
				Object[] fila = (Object[]) lis_datos_rol_orden.get(i);
				if (i == 0) {
					// CEDULA
					jxl.write.Label lab1;
					lab1 = new jxl.write.Label(0, 3, "CEDULA", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0, cv);

					// NOMBRE EMPLEADO
					lab1 = new jxl.write.Label(1, 3, "EMPLEADO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1, cv);

					if (tipo==1) {
						
					}else{
					
					// CARGO
					lab1 = new jxl.write.Label(2, 3, "GENERO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2, cv);

					// DENOMINACION
					lab1 = new jxl.write.Label(3, 3, "DISCAPACIDAD", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3, cv);

					// CONTRATO
					lab1 = new jxl.write.Label(4, 3, "ACUMULA DECIMO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4, cv);
					}
					for (int j = 0; j < lis_nom_columnas_orden.size(); j++) {
 
						jxl.write.Label lab2 = new jxl.write.Label(j + 2, 3, lis_nom_columnas_orden.get(j) + "", formato_celda);
						hoja_xls.addCell(lab2);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(j + 2, cv);
					}
				}
				// lista de objetos que contiene todos los datos
				List<Object> lis_datos_orden = (List<Object>) fila[1];

				for (int j = 0; j < lis_datos_orden.size(); j++) {
					Object[] fila_datos_orden = (Object[]) lis_datos_orden.get(j);
					
					if (tipo==1) {

					for (int k = 0; k < 2; k++) {
						jxl.write.Label lab3 = new jxl.write.Label(k, i + int_fila + 1, fila_datos_orden[k] + "", formato_celda);
						hoja_xls.addCell(lab3);
					}
					
					for (int l = 0; l < lis_nom_columnas_orden.size(); l++) {

						try {
							jxl.write.Number num = new jxl.write.Number(l + 2, i + int_fila + 1, pckUtilidades.CConversion.CDbl_2(String.valueOf(fila_datos_orden[l + 2])), formato_celda_valor_rubro);
							hoja_xls.addCell(num);
						} catch (Exception e) {
							// TODO: handle exception
							jxl.write.Label lab3 = new jxl.write.Label(l + 2, i + int_fila + 1, fila_datos_orden[l + 2] + "", formato_celda_valor_rubro);
							hoja_xls.addCell(lab3);
						}
					}
					}else{
					for (int k = 0; k < 5; k++) {
						jxl.write.Label lab3 = new jxl.write.Label(k, i + int_fila + 1, fila_datos_orden[k] + "", formato_celda);
						hoja_xls.addCell(lab3);
					}

						
					for (int l = 0; l < lis_nom_columnas_orden.size(); l++) {

						try {
							jxl.write.Number num = new jxl.write.Number(l + 5, i + int_fila + 1, pckUtilidades.CConversion.CDbl_2(String.valueOf(fila_datos_orden[l + 5])), formato_celda_valor_rubro);
							hoja_xls.addCell(num);
						} catch (Exception e) {
							// TODO: handle exception
							jxl.write.Label lab3 = new jxl.write.Label(l + 5, i + int_fila + 1, fila_datos_orden[l + 5] + "", formato_celda_valor_rubro);
							hoja_xls.addCell(lab3);
						}
					}
					}
					
					

					int_fila = int_fila + 1;

				}
			}
			
			int_fila = int_fila + 1 + lis_datos_rol_orden.size();
			if (tipo==1) {
				jxl.write.Label lab_tot = new jxl.write.Label(2, int_fila, "TOTAL CONSOLIDADO:", formato_celda_totales);
				hoja_xls.addCell(lab_tot);
				for (int j = 0; j < lis_totales_consolidado.size(); j++) {
					jxl.write.Number num = new jxl.write.Number(j + 2, int_fila, pckUtilidades.CConversion.CDbl_2(String.valueOf(lis_totales_consolidado.get(j))), formato_celda_totales);
					hoja_xls.addCell(num);
					
				}
			}else{
			jxl.write.Label lab_tot = new jxl.write.Label(5, int_fila, "TOTAL CONSOLIDADO:", formato_celda_totales);
			hoja_xls.addCell(lab_tot);
			for (int j = 0; j < lis_totales_consolidado.size(); j++) {
				jxl.write.Number num = new jxl.write.Number(j + 5, int_fila, pckUtilidades.CConversion.CDbl_2(String.valueOf(lis_totales_consolidado.get(j))), formato_celda_totales);
				hoja_xls.addCell(num);
				
			}
			}
			// lista que contiene los totales
			

			archivo_xls.write();
			archivo_xls.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom);
		} catch (Exception e) {
			System.out.println("Error no se genero el XLS :" + e.getMessage());
		}
	}
*/
	
	public void exportarXLSOrden(String nombre, String tipo_nomina, String mes,int tipo) {
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String nom = nombre;
			File result = new File(extContext.getRealPath("/" + nom));
			WritableWorkbook archivo_xls = Workbook.createWorkbook(result);
			WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0);
			WritableFont fuente = new WritableFont(WritableFont.TAHOMA, 10);
			WritableCellFormat formato_celda = new WritableCellFormat(fuente);
			formato_celda.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda.setOrientation(Orientation.HORIZONTAL);
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
			//formato_celda.setBackground(Colour.BLUE);
			
			WritableFont fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_suc = new WritableCellFormat(fuente_suc);
			formato_celda_suc.setAlignment(jxl.format.Alignment.LEFT);
			formato_celda_suc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_suc.setOrientation(Orientation.HORIZONTAL);
			formato_celda_suc.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_totales = new WritableCellFormat(fuente_suc);
			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_totales.setOrientation(Orientation.HORIZONTAL);
			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.RED);

			WritableCellFormat formato_celda_valor_rubro = new WritableCellFormat(fuente);
			formato_celda_valor_rubro.setAlignment(jxl.format.Alignment.RIGHT);
			formato_celda_valor_rubro.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formato_celda_valor_rubro.setOrientation(Orientation.HORIZONTAL);
			formato_celda_valor_rubro.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			int int_columna = 0;
			int int_fila = 4;

			jxl.write.Label lab_titulo = new jxl.write.Label(0, 0, "NOMINA: ", formato_celda_suc);
			hoja_xls.addCell(lab_titulo);
			CellView cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_tip_nomina = new jxl.write.Label(1, 0, tipo_nomina, formato_celda_suc);
			hoja_xls.addCell(lab_tip_nomina);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			jxl.write.Label lab_mes = new jxl.write.Label(0, 1, "MES: ", formato_celda_suc);
			hoja_xls.addCell(lab_mes);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0, cv);

			jxl.write.Label lab_mes1 = new jxl.write.Label(1, 1, mes, formato_celda_suc);
			hoja_xls.addCell(lab_mes1);
			cv = new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1, cv);

			for (int i = 0; i < lis_datos_rol_orden.size(); i++) {
				Object[] fila = (Object[]) lis_datos_rol_orden.get(i);
				//if (i == 0) {
				
				
					// CEDULA
					jxl.write.Label lab1;
					lab1 = new jxl.write.Label(0, 3, "Nº", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0, cv);

					// NOMBRE EMPLEADO
					lab1 = new jxl.write.Label(1, 3, "CEDULA", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1, cv);

					// NOMBRE EMPLEADO
					lab1 = new jxl.write.Label(2, 3, "EMPLEADO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2, cv);
					
					// CARGO
					/*lab1 = new jxl.write.Label(2, 3, "GENERO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2, cv);

					// DENOMINACION
					lab1 = new jxl.write.Label(3, 3, "DISCAPACIDAD", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3, cv);

					// CONTRATO
					lab1 = new jxl.write.Label(4, 3, "ACUMULA DECIMO", formato_celda);
					hoja_xls.addCell(lab1);
					cv = new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4, cv);*/
					
					for (int j = 0; j < lis_nom_columnas_orden.size(); j++) {
 
						jxl.write.Label lab2 = new jxl.write.Label(j + 3, 3, lis_nom_columnas_orden.get(j) + "", formato_celda);
						hoja_xls.addCell(lab2);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(j + 3, cv);
					}
				//}
				// lista de objetos que contiene todos los datos
				List<Object> lis_datos_orden = (List<Object>) fila[1];

				for (int j = 0; j < lis_datos_orden.size(); j++) {
					Object[] fila_datos_orden = (Object[]) lis_datos_orden.get(j);
					for (int k = 0; k < 4; k++) {
						jxl.write.Label lab3 = new jxl.write.Label(k, i + int_fila + 0, fila_datos_orden[k] + "", formato_celda);
						hoja_xls.addCell(lab3);
					}
					for (int l = 0; l < lis_nom_columnas_orden.size(); l++) {

						try {
							jxl.write.Number num = new jxl.write.Number(l + 3, i + int_fila + 0, pckUtilidades.CConversion.CDbl_2(String.valueOf(fila_datos_orden[l + 3])), formato_celda_valor_rubro);
							hoja_xls.addCell(num);
						} catch (Exception e) {
							// TODO: handle exception
							jxl.write.Label lab3 = new jxl.write.Label(l + 3, i + int_fila + 0, fila_datos_orden[l + 3] + "", formato_celda_valor_rubro);
							hoja_xls.addCell(lab3);
						}
					}
					int_fila = int_fila + 1;

				}
			}
			int_fila = int_fila + (-1) + lis_datos_rol_orden.size();
			jxl.write.Label lab_tot = new jxl.write.Label(3, int_fila, "TOTAL CONSOLIDADO:", formato_celda_totales);
			hoja_xls.addCell(lab_tot);

			// lista que contiene los totales
			for (int j = 0; j < lis_totales_consolidado.size(); j++) {
				jxl.write.Number num = new jxl.write.Number(j + 3, int_fila, pckUtilidades.CConversion.CDbl_2(String.valueOf(lis_totales_consolidado.get(j))), formato_celda_totales);
				hoja_xls.addCell(num);
				
			}

			archivo_xls.write();
			archivo_xls.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom);
		} catch (Exception e) {
			System.out.println("Error no se genero el XLS :" + e.getMessage());
		}
	}

	

	
	public int getIndiceRubroOrden(String nom_rubro) {
		for (int i = 0; i < lis_nom_columnas_orden.size(); i++) {
			if (String.valueOf(lis_nom_columnas_orden.get(i)).equalsIgnoreCase(nom_rubro)) {
				return (i + 3);
			}
		}
		return -1;
	}
		
	public SeleccionTabla getSel_tab_periodo_boleta() {
		return sel_tab_periodo_boleta;
	}

	public void setSel_tab_periodo_boleta(SeleccionTabla sel_tab_periodo_boleta) {
		this.sel_tab_periodo_boleta = sel_tab_periodo_boleta;
	}

	public List<Object> getLis_datos_rol_orden_rol_cambio() {
		return lis_datos_rol_orden_rol_cambio;
	}

	public void setLis_datos_rol_orden_rol_cambio(
			List<Object> lis_datos_rol_orden_rol_cambio) {
		this.lis_datos_rol_orden_rol_cambio = lis_datos_rol_orden_rol_cambio;
	}
		
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		 if (rep_reporte.getReporteSelecionado().equals("ROL DE ALIMENTACIÓN Y HORAS EXTRA")){

				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();		
					rep_reporte.cerrar();
					
					sel_tab_periodo_boleta.dibujar();
				} else if (sel_tab_periodo_boleta.isVisible()) {
					try {
						if (sel_tab_periodo_boleta.getValorSeleccionado() != null && !sel_tab_periodo_boleta.getValorSeleccionado().isEmpty()) {
    TablaGenerica tab_perido_rol=utilitario.consultar("select ide_gemes,ide_geani,tipo_rol,fecha_inicial_gepro from gen_perido_rol where IDE_gepro="+sel_tab_periodo_boleta.getValorSeleccionado()+"");
    int mes_perido=utilitario.getMes(tab_perido_rol.getValor("fecha_inicial_gepro"));
	TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where IDE_gemes="+mes_perido+"");
	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where IDE_geani="+tab_perido_rol.getValor("IDE_GEANI")+"");
	TablaGenerica tab_ROL=utilitario.consultar("SELECT ide_nrdtn, ide_nrtin, ide_gttem, ide_gttco, ide_sucu, ide_nrtit, "
			+ "activo_nrdtn "
			+ "FROM nrh_detalle_tipo_nomina "
			+ "where ide_nrtin in("+tab_perido_rol.getValor("tipo_rol")+")");
	
	String IDE_NRDTN="";
	for (int i = 0; i < tab_ROL.getTotalFilas(); i++) {
		if ((tab_ROL.getTotalFilas()-1)==i) {
			IDE_NRDTN+=tab_ROL.getValor(i,"IDE_NRDTN");
		}else {
			IDE_NRDTN+=tab_ROL.getValor(i,"IDE_NRDTN")+",";

		}
	}

							p_parametros.put("titulo", " EMPRESA PUBLICA METROPOLITANA DE GESTION INTEGRAL DE RESIDUOS SOLIDOS EMGIRS EP  "
									+"\n "
									+ "ROL DE PAGO DE HORAS EXTRA Y ALIMENTACIÓN  "
									+"\n "
									+ ""+tab_mes.getValor("DETALLE_GEMES").toUpperCase()+" DEL "+tab_anio.getValor("DETALLE_GEANI")+"");
							p_parametros.put("IDE_GEPRO", sel_tab_periodo_boleta.getValorSeleccionado().toString());
							p_parametros.put("IDE_NRDTN",IDE_NRDTN);
							p_parametros.put("p_coordinador_tthh",utilitario.getVariable("p_gth_coordinador_tthh"));
							p_parametros.put("p_analista_tthh",utilitario.getVariable("p_gth_analista_tthh_vacaciones"));
							p_parametros.put("p_especialiista_tthh",utilitario.getVariable("p_especialiista_tthh"));
							sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
							System.out.print("reporte parametro..." + p_parametros);
							sel_tab_periodo_boleta.cerrar();
							sef_reporte.dibujar();
						} else {
							utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
						}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("error en generacion de reporte aceptarReporte()");
				}

				}
			}
	}

		
	
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}
		

	
	
	
}
