﻿/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

/**
 *
 * @author DELL-USER
 */
public class pre_nomina_global_empleado_periodos extends Pantalla {



	private Division div_division = new Division();
	private SeleccionTabla set_det_tip_nomina=new SeleccionTabla();
	private SeleccionTabla set_tipo_rubro=new SeleccionTabla();

	private Dialogo dia_rubros=new Dialogo();
	private SeleccionTabla set_rubros=new SeleccionTabla();
	private String IDE_NRRUB="";
	private String IDE_NRDTN="";
	private List lis_nom_columnas=new ArrayList();
	private List lis_totales_consolidado=new ArrayList();
	private List<Object> lis_datos_rol = new ArrayList<Object>();

	private DataTable tabla= new DataTable();
	private Dialogo dia_formula = new Dialogo();
	private Etiqueta eti_formula = new Etiqueta();
	private Etiqueta eti_mensaje = new Etiqueta();
	private AutoCompletar aut_rubros_tip_formula=new AutoCompletar();
	private Grid gri=new Grid();

	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_nomina_global_empleado_periodos() {        

		Boton bot_ver_nomina_global=new Boton();
		bot_ver_nomina_global.setMetodo("pedirParametros");
		bot_ver_nomina_global.setValue("VER ROL GLOBAL");

		//ecevallos
		Boton bot_ver_excel_decimos=new Boton();
		bot_ver_excel_decimos.setValue("ARCHIVO DECIMOS");
		//bot_ver_excel_decimos.setAjax(false);
		bot_ver_excel_decimos.setMetodo("pedirFecha");

		bar_botones.agregarBoton(bot_ver_nomina_global);
		bar_botones.agregarBoton(bot_ver_excel_decimos);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_excel=new Boton();
		bot_excel.setValue("EXCEL");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarNominaExcel");

		bar_botones.agregarBoton(bot_excel);

		Boton bot_cambiar_filtro=new Boton();
		bot_cambiar_filtro.setMetodo("cambiarFiltroRubros");
		bot_cambiar_filtro.setValue("FILTRO RUBROS");
		bar_botones.agregarBoton(bot_cambiar_filtro);


		//Dialogo para ver la Formula

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
		aut_rubros_tip_formula.setAutoCompletar("select IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO where IDE_NRFOC="+utilitario.getVariable("p_nrh_forma_calculo_formula"));
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


		set_det_tip_nomina.setId("set_det_tip_nomina");
		set_det_tip_nomina.setTitle("Seleccion de Parametros");
		set_det_tip_nomina.setSeleccionTabla("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit " +
				"FROM NRH_DETALLE_TIPO_NOMINA a " +
				"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
				"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin " +
				"inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit","IDE_NRDTN");
		set_det_tip_nomina.getBot_aceptar().setMetodo("aceptarTipoNomina");
		set_det_tip_nomina.setDynamic(false);

		//		Grid gri1=new Grid();
		//		gri1.setColumns(2);
		//		gri1.getChildren().add(new Etiqueta("Periodo Rol: "));
		//		gri1.getChildren().add(com_periodo_rol);
		//		set_det_tip_nomina.getGri_cuerpo().getChildren().add(gri1);
		//		set_det_tip_nomina.getGri_cuerpo().getChildren().add(new Etiqueta("Selecione Tipo de Nomina"));

		agregarComponente(set_det_tip_nomina);



		set_tipo_rubro.setId("set_tipo_rubro");
		set_tipo_rubro.setSeleccionTabla("select IDE_NRTIR,DETALLE_NRTIR from NRH_TIPO_RUBRO order by IDE_NRTIR", "IDE_NRTIR");
		set_tipo_rubro.getBot_aceptar().setMetodo("aceptarTipoRubro");
		agregarComponente(set_tipo_rubro);

		set_rubros.setId("set_rubros");
		set_rubros.setSeleccionTabla("select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN IN (-1) AND IMPRIME_NRDER=true " +
				"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
				"ORDER BY ORDEN_IMPRIME_NRDER ASC","IDE_NRRUB");
		set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
		set_rubros.getTab_seleccion().onSelectCheck("seleccionaRubro");
		set_rubros.getTab_seleccion().onUnselectCheck("quitaSeleccionRubro");
		set_rubros.getBot_aceptar().setMetodo("aceptarRubros");
		agregarComponente(set_rubros);

		sec_rango.setId("sec_rango");
		sec_rango.setDynamic(false);
		agregarComponente(sec_rango);
		
	}
	

	public void aceptarFormula() {
		dia_formula.cerrar();
	}

	public void verFormulaLetras(SelectEvent evt){
		aut_rubros_tip_formula.onSelect(evt);
		String formula =utilitario.consultar("select * from NRH_DETALLE_RUBRO where FORMULA_NRDER is not NULL and IDE_NRRUB="+aut_rubros_tip_formula.getValor()).getValor(0,"FORMULA_NRDER");		
		if (formula != null && !formula.isEmpty()) {
			if (formula.startsWith("=")) {
				eti_formula.setValue(ser_nomina.getFormulaEnLetras(0,"","","",formula,false));
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

	public void seleccionaRubro(SelectEvent evt){
		System.out.println("sel rubro "+set_rubros.getListaSeleccionados().size());
	}

	public void quitaSeleccionRubro(UnselectEvent evt){

		System.out.println("un sel rubro "+set_rubros.getListaSeleccionados().size());
	}

	public void cambiarFiltroRubros(){
		if (lis_datos_rol.size()>0){
			
			set_rubros.getTab_seleccion().setSql(ser_nomina.getRubrosOrden(IDE_NRDTN,"").getSql());

//			set_rubros.getTab_seleccion().setSql("select a.ide_nrrub,a.detalle_nrrub from ( select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB " +
//					"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " +
//					"WHERE DER.IDE_NRDTN IN ("+IDE_NRDTN+") AND IMPRIME_NRDER=true " +
//					"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
//					"ORDER BY ORDEN_IMPRIME_NRDER ASC)a " +
//					"GROUP BY a.ide_nrrub,a.detalle_nrrub ");
			set_rubros.getTab_seleccion().ejecutarSql();
			set_rubros.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar los parametros para Visualizar el Rol");
		}

	}

	private String tipo_nomina="";
	public void aceptarTipoNomina(){
		System.out.println("tipo nomina "+set_det_tip_nomina.getSeleccionados());
			if (set_det_tip_nomina.getSeleccionados()!=null && !set_det_tip_nomina.getSeleccionados().isEmpty()){

				tipo_nomina="";
				for (int i = 0; i < set_det_tip_nomina.getListaSeleccionados().size(); i++) {
					Fila fila=set_det_tip_nomina.getListaSeleccionados().get(i);
					tipo_nomina+=ser_nomina.getTipoEmpleado(ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_GTTEM")).getValor("DETALLE_GTTEM");
					if (i>0 && i<(set_det_tip_nomina.getListaSeleccionados().size()-1)){
						tipo_nomina+=",";
					}
				}

				IDE_NRDTN=set_det_tip_nomina.getSeleccionados();
				set_tipo_rubro.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Tipo de Nomina");
			}


	}
	public void limpiar(){
		
		IDE_NRDTN="";
		IDE_NRRUB="";
		cargarTablaVacia();
		utilitario.addUpdate("com_periodo_rol,div_division");
	}



	public void pedirParametros(){

		if (!sec_rango.isVisible()){
		sec_rango.setTitle("SELECCIONE RANGO DE FECHAS");
		sec_rango.getBot_aceptar().setMetodo("pedirParametros");
		sec_rango.setFecha1(null);
		sec_rango.setFecha2(null);
		sec_rango.dibujar();
		utilitario.addUpdate("sec_rango");
		}else{
			
			if (!sec_rango.isFechasValidas()){
			utilitario.agregarMensajeInfo("Fechas invalidas","No se puede continuar");
				return;
			}
			
			set_det_tip_nomina.getTab_seleccion().setSeleccionados(null);
			set_det_tip_nomina.dibujar();
		}
		
	}

	//ecevallos
	public void pedirFecha(){

		if (!sec_rango.isVisible()){
			sec_rango.setTitle("SELECCIONE RANGO DE FECHAS");
			sec_rango.getBot_aceptar().setMetodo("pedirFecha");
			sec_rango.getBot_aceptar().setAjax(false);
			sec_rango.setFecha1(null);
			sec_rango.setFecha2(null);
			sec_rango.dibujar();
			utilitario.addUpdate("sec_rango");
		}else{
			
			if (!sec_rango.isFechasValidas()){
				utilitario.agregarMensajeInfo("Fechas invalidas","No se puede continuar");
				return;
			}
			String ide_gepro=ser_nomina.getPeriodosRol(sec_rango.getFecha1String(), sec_rango.getFecha2String());
			
			try {
				generarArchivoDecimo(ide_gepro, sec_rango.getFecha2String());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//set_det_tip_nomina.getTab_seleccion().setSeleccionados(null);
			//set_det_tip_nomina.dibujar();
			sec_rango.cerrar();
		}
		
	}
	
	//ecevallos
	public void generarArchivoDecimo(String ide_gepro, String fecha_fin ) throws Exception
	{
		
		String anioHasta =utilitario.getAnio(fecha_fin)+"";
		TablaGenerica tab_rep_decimo=utilitario.consultar(getSqlNominaGlobalDecimos(IDE_NRDTN, ide_gepro, IDE_NRRUB,anioHasta));
		
		String fileName= "archivoDecimos";
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		String path= extContext.getRealPath("/");			
		
		File archivotxt=new File(path+fileName.concat(".csv"));
		BufferedWriter escribir;
		try {
			escribir = new BufferedWriter(new FileWriter(archivotxt));
			
			escribir.write("NOMBRE, APELLIDO, CEDULA, GENERO, DECIMO CUARTO, DIAS DECIMO CUARTO, DECINO TERCERO, DIAS DECIMO TERCERO");
			
			String linea  ="";
			
			for (int i = 0; i < tab_rep_decimo.getTotalFilas(); i++) {
				escribir.newLine();
				linea+= tab_rep_decimo.getValor(i, "NOMBRE")+",";
				linea+= tab_rep_decimo.getValor(i, "APELLIDO")+",";
				linea+= tab_rep_decimo.getValor(i, "CEDULA")+",";
				linea+= tab_rep_decimo.getValor(i, "GENERO")+",";
				linea+= tab_rep_decimo.getValor(i, "DecimoCuarto")+",";
				linea+= tab_rep_decimo.getValor(i, "diasDecimoCuarto")+",";
				linea+= tab_rep_decimo.getValor(i, "DecimoTercer")+",";
				linea+= tab_rep_decimo.getValor(i, "diasDecimoTercero");
				escribir.write(linea);
				System.out.println(linea);
				linea="";
			
			}
			
			escribir.close();

			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".csv"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println(e.getMessage().toString());
		}
		return;
	}
	
	
	
	public void dibujarTabla(){


		tabla=new DataTable();
		tabla.setId("tabla");
		tabla.setResizableColumns(true);
		tabla.setStyle("font-size:13px");
		tabla.setVar("suc");
		tabla.setValueExpression("value", crearValueExpression("pre_index.clase.lis_datos_rol"));

		ColumnGroup columnGroup=new ColumnGroup();
		columnGroup.setType("header");
		tabla.getChildren().add(columnGroup);

		Row r1=new Row();
		columnGroup.getChildren().add(r1);

		Column c1=new Column();
		c1.setHeaderText("Departamento");
		c1.setWidth(120);
		c1.setRowspan(3);
		r1.getChildren().add(c1);

		Column c2=new Column();
		c2.setHeaderText("Cédula");
		c2.setWidth(60);
		c2.setRowspan(3);
		r1.getChildren().add(c2);

		Column c3=new Column();
		c3.setHeaderText("Apellido");
		c3.setRowspan(3);
		c3.setWidth(200);
		c3.setResizable(true);
		r1.getChildren().add(c3);

		Column c31=new Column();
		c31.setHeaderText("Nombre");
		c31.setRowspan(3);
		c31.setWidth(200);
		c31.setResizable(true);
		r1.getChildren().add(c31);
		
		
		Column c32=new Column();
		c32.setHeaderText("Genero");
		c32.setRowspan(3);
		c32.setWidth(200);
		c32.setResizable(true);
		r1.getChildren().add(c32);

		Column c4=new Column();
		c4.setHeaderText("Cargo");
		c4.setWidth(100);
		c4.setRowspan(3);
		r1.getChildren().add(c4);

		Column c5=new Column();
		c5.setHeaderText("Contrato");
		c5.setWidth(100);
		c5.setRowspan(3);
		r1.getChildren().add(c5);


		Row r2=new Row();	
		columnGroup.getChildren().add(r2);
		Column c6=new Column();
		c6.setHeaderText("Rubros");
		c6.setColspan(lis_nom_columnas.size());
		r2.getChildren().add(c6);

		Row r3=new Row();	
		columnGroup.getChildren().add(r3);

		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c7=new Column();
			c7.setStyle("font-weight:bold");
			c7.setValueExpression("headerText", crearValueExpression("pre_index.clase.lis_nom_columnas["+i+"]"));				
			//			c7.setWidth(120);
			c7.setRowspan(5);
			c7.setResizable(true);
			r3.getChildren().add(c7);
		}


		SubTable subtable= new SubTable();
		subtable.setVar("emp");
		subtable.setValueExpression("value", crearValueExpression("suc[1]"));
		tabla.getChildren().add(subtable);

		Etiqueta eti_sucursal=new Etiqueta();
		eti_sucursal.setValueExpression("value", "suc[0]");
		eti_sucursal.setStyle("font-size:14px");
		subtable.getFacets().put("header", eti_sucursal);


		for(int i=0;i<(7+lis_nom_columnas.size());i++){
			Column c8=new Column();
			Etiqueta eti=new Etiqueta();

			if (i>=7){
				eti.setTitle(lis_nom_columnas.get(i-7)+"");
			}

			eti.setValueExpression("value", "emp["+i+"]");
			if (i>4){
				c8.setStyle("text-align:center;font-size:10px");
			}else if (i==2){
				c8.setStyle("text-align:left;font-size:9px");
			}else {
				c8.setStyle("text-align:center;font-size:9px");
			}
			c8.setResizable(true);

			c8.getChildren().add(eti);
			subtable.getChildren().add(c8);
		}


		ColumnGroup columnGroupTotales=new ColumnGroup();
		columnGroupTotales.setType("footer");
		subtable.getChildren().add(columnGroupTotales);

		Row r4=new Row();
		columnGroupTotales.getChildren().add(r4);

		Column c9=new Column(); 
		c9.setColspan(7);

		c9.setFooterText("Total: ");
		c9.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r4.getChildren().add(c9);
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c10=new Column();
			c10.setValueExpression("footerText", crearValueExpression("suc["+(i+2)+"]"));
			c10.setStyle("text-align:right;font-size:14px;padding-right:10px");			
			r4.getChildren().add(c10);
		}

		ColumnGroup columnGroupTotalesC=new ColumnGroup();
		columnGroupTotalesC.setType("footer");
		tabla.getChildren().add(columnGroupTotalesC);

		Row r5=new Row();
		columnGroupTotalesC.getChildren().add(r5);

		Column c11=new Column(); 
		c11.setColspan(7);
		c11.setFooterText("Total Consolidado: ");
		c11.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r5.getChildren().add(c11);

		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			Column c12=new Column();
			c12.setValueExpression("footerText", crearValueExpression("pre_index.clase.lis_totales_consolidado["+i+"]"));				
			c12.setStyle("text-align:right;font-size:14px;padding-right:10px");
			r5.getChildren().add(c12);
		}


		tabla.setSelectionMode("single");


		Grid gri_cabecera=new Grid();
		gri_cabecera.setWidth("100%");

		Etiqueta eti_titulo=new Etiqueta();
		eti_titulo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_titulo.setValue("REPORTE DE NOMINA GLOBAL DE EMPLEADOS ");

		
		String ide_gepro=ser_nomina.getPeriodosRol(sec_rango.getFecha1String(), sec_rango.getFecha2String());

		Etiqueta eti_periodo=new Etiqueta();
		eti_periodo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_periodo.setValue("desde "+sec_rango.getFecha1String()+" hasta "+sec_rango.getFecha2String());

		String str_tip_nomina="";

		try {
			for (int i = 0; i < set_det_tip_nomina.getListaSeleccionados().size(); i++) {
				Fila fila=set_det_tip_nomina.getListaSeleccionados().get(i);
				str_tip_nomina+=fila.getCampos()[1]+"-"+fila.getCampos()[2]+",";
			}
			try {
				str_tip_nomina=str_tip_nomina.substring(0,str_tip_nomina.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		Etiqueta eti_tip_nomina=new Etiqueta();
		eti_tip_nomina.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_tip_nomina.setValue("NOMINAS: "+str_tip_nomina);

		gri_cabecera.getChildren().add(eti_titulo);
		gri_cabecera.getChildren().add(eti_periodo);
		gri_cabecera.getChildren().add(eti_tip_nomina);


		gri=new Grid();
		gri.setWidth("100%");
		gri.setStyle("display:block;height:100%");
		gri.getChildren().add(tabla);
		gri.setHeader(gri_cabecera);



		div_division.getChildren().clear();

		div_division.dividir1(gri);



	}

	public void exportarNominaExcel(){
		if (lis_datos_rol.size()>0){
			if (lis_nom_columnas.size()>0){
				exportarXLS("nomina.xls",tipo_nomina,"");
			}
		}
	}
	public void exportarXLS(String nombre,String tipo_nomina,String mes) { 
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
			int int_fila=4;

			jxl.write.Label lab_titulo = new jxl.write.Label(0, 0, "NOMINA: ", formato_celda_suc);
			hoja_xls.addCell(lab_titulo);
			CellView cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0,cv);

			jxl.write.Label lab_tip_nomina = new jxl.write.Label(1, 0, tipo_nomina, formato_celda_suc);
			hoja_xls.addCell(lab_tip_nomina);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1,cv);


			jxl.write.Label lab_mes = new jxl.write.Label(0, 1, "MES: ", formato_celda_suc);
			hoja_xls.addCell(lab_mes);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(0,cv);

			jxl.write.Label lab_mes1 = new jxl.write.Label(1, 1, mes, formato_celda_suc);
			hoja_xls.addCell(lab_mes1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(1,cv);

			for (int i = 0; i < lis_datos_rol.size(); i++) {
				Object []fila=(Object []) lis_datos_rol.get(i);
				if (i==0){
					// NOMBRES DE COLUMNAS
					// DEPARTAMENTO
					jxl.write.Label lab1 = new jxl.write.Label(0, 3, "DEPARTAMENTO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0,cv);

					// CEDULA
					lab1 = new jxl.write.Label(1, 3, "CEDULA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1,cv);

					// NOMBRE EMPLEADO
					lab1 = new jxl.write.Label(2, 3, "APELLIDO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2,cv);

					lab1 = new jxl.write.Label(3, 3, "NOMBRE", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3,cv);

					lab1 = new jxl.write.Label(4, 3, "GENERO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4,cv);

					
					// CARGO
					lab1 = new jxl.write.Label(5, 3, "CARGO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(5,cv);

					// CONTRATO
					lab1 = new jxl.write.Label(6, 3, "CONTRATO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(6,cv);


					for (int j = 0; j < lis_nom_columnas.size(); j++) {


						jxl.write.Label lab2 = new jxl.write.Label(j+7, 3, lis_nom_columnas.get(j)+"", formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(j+7,cv);
					}
				}


				// SUCURSAL
				jxl.write.Label lab = new jxl.write.Label(int_columna, i+int_fila, fila[0]+"", formato_celda_suc);
				hoja_xls.addCell(lab);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(int_columna,cv);



				// lista de objetos que contiene todos los datos
				List <Object> lis_datos=(List<Object>) fila[1];

				for (int j = 0; j < lis_datos.size(); j++) {
					Object []fila_datos=(Object []) lis_datos.get(j);
					for (int k = 0; k < 7; k++) {
						jxl.write.Label lab3 = new jxl.write.Label(k, i+int_fila+1, fila_datos[k]+"", formato_celda);
						hoja_xls.addCell(lab3); 
					}
					for (int l = 0; l < lis_nom_columnas.size(); l++) {

						try {
							jxl.write.Number num = new jxl.write.Number(l+7, i+int_fila+1, Double.parseDouble(String.valueOf(fila_datos[l+7])), formato_celda_valor_rubro); 
							hoja_xls.addCell(num);
						} catch (Exception e) {
							// TODO: handle exception
							jxl.write.Label lab3 = new jxl.write.Label(l+7, i+int_fila+1, fila_datos[l+7]+"", formato_celda_valor_rubro);
							hoja_xls.addCell(lab3); 

						}

					}
					int_fila=int_fila+1;

				}

				// vector con los totales por sucursal 


				jxl.write.Label lab_tot = new jxl.write.Label(4, i+int_fila+1, "TOTAL "+fila[0]+": ", formato_celda_totales);
				hoja_xls.addCell(lab_tot); 


				for (int j = 0; j < lis_nom_columnas.size(); j++) {
					jxl.write.Number num = new jxl.write.Number(j+7, i+int_fila+1, Double.parseDouble(String.valueOf(fila[j+2])), formato_celda_totales); 
					hoja_xls.addCell(num);

					//					lab_tot = new jxl.write.Label(j+5, i+int_fila+1, fila[j+2]+"", formato_celda_totales);
					//					hoja_xls.addCell(lab_tot); 
				}

				int_fila=int_fila+1;
			}
			int_fila=int_fila+1+lis_datos_rol.size();
			jxl.write.Label lab_tot = new jxl.write.Label(4, int_fila, "TOTAL CONSOLIDADO:", formato_celda_totales);
			hoja_xls.addCell(lab_tot); 

			// lista que contiene los totales
			for (int j = 0; j < lis_totales_consolidado.size(); j++) {
				jxl.write.Number num = new jxl.write.Number(j+7, int_fila, Double.parseDouble(String.valueOf(lis_totales_consolidado.get(j))), formato_celda_totales); 
				hoja_xls.addCell(num);

				//				lab_tot = new jxl.write.Label(j+5, int_fila, lis_totales_consolidado.get(j)+"", formato_celda_totales);
				//				hoja_xls.addCell(lab_tot); 
			}

			archivo_xls.write(); 
			archivo_xls.close(); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom); 
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
	}

	public void aceptarRubros(){
		if (set_rubros.getSeleccionados()!=null && !set_rubros.getSeleccionados().isEmpty()){
			IDE_NRRUB=set_rubros.getSeleccionados();
			lis_datos_rol.clear();
			lis_nom_columnas.clear();
			lis_totales_consolidado.clear();
			String ide_gepro=ser_nomina.getPeriodosRol(sec_rango.getFecha1String(), sec_rango.getFecha2String());

			llenarTabla(IDE_NRDTN, ide_gepro,IDE_NRRUB);
			set_det_tip_nomina.cerrar();
			set_rubros.cerrar();
			set_tipo_rubro.cerrar();
			sec_rango.cerrar();
			dibujarTabla();
			utilitario.addUpdate("div_division");
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Rubro");
		}
	}

	public void aceptarTipoRubro(){

		if (set_tipo_rubro.getSeleccionados()!=null && !set_tipo_rubro.getSeleccionados().isEmpty()){
			String ide_gepro=ser_nomina.getPeriodosRol(sec_rango.getFecha1String(), sec_rango.getFecha2String());

			String IDE_NRTIR=set_tipo_rubro.getSeleccionados();

			
			TablaGenerica tab_rub_sel=utilitario.consultar("select RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER from NRH_RUBRO RUB " +
					"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " +
					"WHERE DER.IDE_NRDTN IN ("+IDE_NRDTN+") AND IMPRIME_NRDER=true and IDE_NRTIR in ("+IDE_NRTIR+") " +
					"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
					"ORDER BY ORDEN_IMPRIME_NRDER ASC ");

			String ide_nrrub="";
			for (int i = 0; i < tab_rub_sel.getTotalFilas(); i++) {
				ide_nrrub+=tab_rub_sel.getValor(i, "IDE_NRRUB")+",";
			}
			try {
				ide_nrrub=ide_nrrub.substring(0,ide_nrrub.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			

			set_rubros.getTab_seleccion().setSql(ser_nomina.getRubrosOrden(IDE_NRDTN,ide_nrrub).getSql());
			set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
			set_rubros.getTab_seleccion().ejecutarSql();
			System.out.println("rubros "+set_rubros.getTab_seleccion().getSql());
			set_rubros.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar el Rol", "Debe seleccionar al menos un Tipo de Rubro");
		}
	}


	public void cargarTablaVacia(){
		lis_datos_rol.clear();
		lis_nom_columnas.clear();
		lis_totales_consolidado.clear();
		llenarTabla(null,null,null);
		dibujarTabla();
	}

	public String getCondicionSqlTablaVirtual(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB){
		String str_sql_condicion_tabla_virtual=" from ( " +
				"select IDE_SUCU,sucursal,ide_gtemp,empleado " +
				"from ( " +
				"SELECT " +
				"SUCU.IDE_SUCU,EMP.IDE_GTEMP,sucu.nom_sucu as sucursal,dep.detalle_gedep as departamento, " +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '||(case when  EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS EMPLEADO, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, " +
				"funcional.detalle_gecaf as cargo, " +
				"contrato.detalle_gttco as contrato, " +
				"RUB.DETALLE_NRRUB AS RUBROS, " +
				"derubro.orden_nrder,rub.ide_nrrub, " +
				"DETA.VALOR_NRDRO AS MONTO " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
				"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
				"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
				"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR " +
				"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
				"AND RUB.IDE_NRRUB IN ("+IDE_NRRUB+") " +
				"order by sucu.nom_sucu DESC,EMPLEADO ,derubro.orden_nrder " +
				") a " +
				"group by IDE_SUCU,sucursal,ide_gtemp,empleado " +
				") a";
		return str_sql_condicion_tabla_virtual;
	}


	public String getSqlNominaGlobalEmpleados(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB){
		String str_sql="SELECT SUCU.IDE_SUCU,EMP.IDE_GTEMP, " +
				"sucu.nom_sucu as sucursal,dep.detalle_gedep as departamento, "+
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  as APELLIDO, " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRE, " +
				"substring(GENEROEMP.detalle_gtgen from 1 for 1) as GENERO, "+
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, " +
				"funcional.detalle_gecaf as cargo, "+
				"CONTRATO.detalle_gttco as contrato, "+
				//				"replace(replace(replace(replace(DETALLE_NRRUB,' ','_'),'.',''),'%',''),'-','') as rubros, "+
				"DETALLE_NRRUB as rubros, "+
//				"derubro.orden_imprime_nrder, " +
				"rub.ide_nrrub, " +
				"sum (DETA.VALOR_NRDRO) AS MONTO " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
//				"AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
				"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
				"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
				"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR " +
				"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "+
				"LEFT JOIN gth_genero GENEROEMP ON GENEROEMP.ide_gtgen=EMP.ide_gtgen "+
				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
				"AND RUB.IDE_NRRUB IN ("+IDE_NRRUB+")  AND  DETA.VALOR_NRDRO!=0 " +
				"GROUP BY SUCU.IDE_SUCU,EMP.IDE_GTEMP, sucu.nom_sucu, " +
				"dep.detalle_gedep , GENEROEMP.detalle_gtgen," +
				"EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP , " +
				"EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP , funcional.detalle_gecaf , " +
				"CONTRATO.detalle_gttco , DETALLE_NRRUB , " +
//				"derubro.orden_imprime_nrder, " +
				"rub.ide_nrrub "+//, DETA.VALOR_NRDRO " +
				//"HAVING DETA.VALOR_NRDRO!=0 " +
//				"order by sucursal DESC,EMPLEADO,derubro.orden_imprime_nrder ";
				"order by sucursal DESC,APELLIDO ";
		return str_sql;
	}

	public int getIndiceRubro(String nom_rubro){
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			if (String.valueOf(lis_nom_columnas.get(i)).equalsIgnoreCase(nom_rubro)){
				return (i+7);
			}
		}
		return -1;
	}

	//ecevallos
	public String getSqlNominaGlobalDecimos(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB, String anioHasta){
		String str_sql="Select * from (SELECT EMP.IDE_GTEMP,  ide_gttem ,  "+
		" EMP.PRIMER_NOMBRE_GTEMP ||' '||(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRE,    "+
		" EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  as APELLIDO,   "+
		" substring(GENEROEMP.detalle_gtgen from 1 for 1) as GENERO,   "+
		" EMP.DOCUMENTO_IDENTIDAD_GTEMP AS CEDULA, COALESCE (decimoCuarto,0.00) as DecimoCuarto,   "+
		" COALESCE (diasD4,0.00) as diasDecimoCuarto,   "+
		
		"  COALESCE ( COALESCE (DecimoTercero,0.00)- COALESCE (decimoTercerAjuste,0.00),0.00) as DecimoTercer,   "+
		" COALESCE (diasD3,0.00) as diasDecimoTercero   "+
		
		" from gth_empleado as emp    "+
		" left join gth_genero GENEROEMP ON GENEROEMP.ide_gtgen=EMP.ide_gtgen   "+
		" left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero    FROM NRH_DETALLE_ROL detarol    "+ 
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL     "+
		" in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+IDE_GEPRO+"))      "+
		" and detrubro.IDE_NRRUB in (334,368,367) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP      "+
		" left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste        "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "+
		" (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+IDE_GEPRO+"))     "+
		" and detrubro.IDE_NRRUB in (369) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP     "+
		
		" left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuarto       "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "+
		" (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+IDE_GEPRO+"))     "+
		" and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP     "+
		
		
		" left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS diasD3       "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "+
		" (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+IDE_GEPRO+"))     "+
		" and detrubro.IDE_NRRUB in (274) group by IDE_GTEMP) diasDecimoT on diasdecimoT.IDE_GTEMP = emp.IDE_GTEMP   "+  
		
		
		" left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS diasD4       "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "+ 
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "+
		" (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+IDE_GEPRO+"))   "+
		" and detrubro.IDE_NRRUB in (65,358) group by IDE_GTEMP) diasdecimoC on diasdecimoC.IDE_GTEMP = emp.IDE_GTEMP "+
		" left join  "+
		
	       
		"        (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, "+
		"        STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar "+
		"        left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded "+
		"        where  activo_geded = true  "+
		"        and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  "+
		"        is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+") "+
		"        group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP) as depar on depar.ide_gtemp= emp.ide_gtemp ) y  where ide_gttem not like '%3%'  order by APELLIDO ";
		return str_sql;
	}


	//	public List llenarTabla(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB){
	//		if (IDE_NRDTN!=null && !IDE_NRDTN.isEmpty() 
	//				&& IDE_GEPRO!=null && !IDE_GEPRO.isEmpty()
	//				&& IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){
	//
	//			Tabla tab_rep_global=utilitario.consultar(getSqlNominaGlobal(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB)); 
	//			lis_nom_columnas=new ArrayList();
	//			lis_datos_rol = new ArrayList<Object>();
	//			System.out.println("sql rep_global "+tab_rep_global.getSql());
	//
	//			if (tab_rep_global.getTotalFilas()>0){
	//				Tabla tab_rubros=ser_nomina.getRubro(IDE_NRRUB);
	//				for (int i = 0; i < tab_rubros.getTotalFilas(); i++) {
	//					lis_nom_columnas.add(tab_rubros.getValor(i, "DETALLE_NRRUB"));
	//				}
	//
	//				List<Object> lisq = new ArrayList<Object>();
	//				Object [] obj_columnas=new Object[5+lis_nom_columnas.size()]; 
	//				int int_count_objeto=5;
	//
	//				obj_columnas[0]=tab_rep_global.getValor(0, "DEPARTAMENTO");
	//				obj_columnas[1]=tab_rep_global.getValor(0, "CEDULA");
	//				obj_columnas[2]=tab_rep_global.getValor(0, "EMPLEADO");
	//				obj_columnas[3]=tab_rep_global.getValor(0, "CARGO");
	//				obj_columnas[4]=tab_rep_global.getValor(0, "CONTRATO");
	//				
	//				for (int i = 0; i < lis_nom_columnas.size(); i++) {
	//					obj_columnas[i+5]="0.00";
	//				}
	//
	//
	//				String str_ide_gtemp="";
	//
	//				lis_datos_rol = new ArrayList<Object>();
	//
	//
	//				double [] totales=new double[lis_nom_columnas.size()];
	//				for (int i = 0; i < lis_nom_columnas.size(); i++) {
	//					totales[i]=0;
	//				}
	//
	//				double [] totales_consolidado=new double[lis_nom_columnas.size()];
	//				for (int i = 0; i < lis_nom_columnas.size(); i++) {
	//					totales_consolidado[i]=0;
	//				}
	//				int int_indice_totales_c=0;
	//				int int_indice_totales=0;
	//
	//
	//				String str_ide_sucu="";
	//				if (tab_rep_global.getTotalFilas()>0){
	//					str_ide_gtemp=tab_rep_global.getValor(0, "IDE_GTEMP");
	//					str_ide_sucu=tab_rep_global.getValor(0, "IDE_SUCU");
	//				}
	//				int bandera=0;
	//				int int_indice_sucursal=0;
	//				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
	//					if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_SUCU"))){
	//
	//						if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_GTEMP"))){
	//							try {
	//
	//								BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
	//								big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	//								int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	//								if (indice>-1){
	//								obj_columnas[indice]=""+big_valor;
	//								}
	//								//							obj_columnas[int_count_objeto]=""+big_valor;
	//								//							int_count_objeto=int_count_objeto+1;
	//								totales[int_indice_totales]=totales[int_indice_totales]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
	//								totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));							
	//							} catch (Exception e) {
	//							}
	//							int_indice_totales=int_indice_totales+1;
	//							int_indice_totales_c=int_indice_totales_c+1;
	//						}else{
	//							str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
	//							lisq.add(obj_columnas);
	//
	//							obj_columnas=new Object[5+lis_nom_columnas.size()];
	//							obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
	//							obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
	//							obj_columnas[2]=tab_rep_global.getValor(i, "EMPLEADO");
	//							obj_columnas[3]=tab_rep_global.getValor(i, "CARGO");
	//							obj_columnas[4]=tab_rep_global.getValor(i, "CONTRATO");
	//
	//							for (int k = 0; k < lis_nom_columnas.size(); k++) {
	//								obj_columnas[k+5]="0.00";
	//							}
	//							
	//							BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
	//							big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	//							int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	//							if (indice>-1){
	//							obj_columnas[indice]=""+big_valor;
	//							}
	//
	//							
	////							obj_columnas[int_count_objeto]=""+big_valor;
	////							int_count_objeto=int_count_objeto+1;
	//
	//							int_indice_totales=0;
	//							int_indice_totales_c=0;
	//							try {
	//								totales[int_indice_totales]=totales[int_indice_totales]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
	//								totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));							
	//							} catch (Exception e) {
	//							}
	//							int_indice_totales=int_indice_totales+1;
	//							int_indice_totales_c=int_indice_totales_c+1;
	//						}
	//					}else{
	//						lisq.add(obj_columnas);
	//						bandera=0;
	//						str_ide_sucu=tab_rep_global.getValor(i, "IDE_SUCU");
	//						int_indice_sucursal=i;
	//						Object [] obj=new Object[2+totales.length]; 
	//						obj[0]=tab_rep_global.getValor(i-1, "SUCURSAL");
	//						obj[1]=lisq;
	//						for (int j = 0; j < totales.length; j++) {
	//							BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales[j]+""));
	//							big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//							obj[j+2]=""+big_valor;
	//						}
	//						lis_datos_rol.add(obj);
	//
	//						lisq = new ArrayList<Object>();
	//						for (int k = 0; k < lis_nom_columnas.size(); k++) {
	//							totales[k]=0;
	//						}
	//
	//						obj_columnas=new Object[5+lis_nom_columnas.size()];
	//						obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
	//						obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
	//						obj_columnas[2]=tab_rep_global.getValor(i, "EMPLEADO");
	//						obj_columnas[3]=tab_rep_global.getValor(i, "CARGO");
	//						obj_columnas[4]=tab_rep_global.getValor(i, "CONTRATO");
	//
	//						for (int k = 0; k < lis_nom_columnas.size(); k++) {
	//							obj_columnas[k+5]="0.00";
	//						}
	//						
	//						BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
	//						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//
	//						int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
	//						if (indice>-1){
	//							obj_columnas[indice]=""+big_valor;
	//						}
	//
	//						//					obj_columnas[int_count_objeto]=""+big_valor;
	////						int_count_objeto=int_count_objeto+1;
	//
	//						int_indice_totales=0;
	//						int_indice_totales_c=0;
	//						try {
	//							totales[int_indice_totales]=totales[int_indice_totales]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
	//							totales_consolidado[int_indice_totales_c]=totales_consolidado[int_indice_totales_c]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
	//						} catch (Exception e) {
	//						}
	//						int_indice_totales=int_indice_totales+1;
	//						int_indice_totales_c=int_indice_totales_c+1;
	//
	//						str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
	//
	//					}
	//
	//				}
	//
	//				if (bandera==0){
	//					lisq.add(obj_columnas);
	//					Object [] obj=new Object[2+totales.length]; 
	//					obj[0]=tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
	//					obj[1]=lisq;
	//					for (int j = 0; j < totales.length; j++) {
	//						//					BigDecimal big_valor=new BigDecimal(totales[j]);
	//						BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales[j]+""));
	//						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//						obj[j+2]=""+big_valor;
	//					}
	//					lis_datos_rol.add(obj);
	//				}
	//				lis_totales_consolidado=new ArrayList();
	//				for (int j = 0; j < totales_consolidado.length; j++) {
	//					BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales_consolidado[j]+""));
	//					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
	//					lis_totales_consolidado.add(""+big_valor);
	//				}
	//				return lis_datos_rol;
	//			}else{
	//				return null;
	//			}
	//			
	//		}else{
	//			return null;
	//		}
	//	}

	public boolean validarRubroRepetido(String nom_rubro){
		for (int i = 0; i < lis_nom_columnas.size(); i++) {
			if (String.valueOf(lis_nom_columnas.get(i)).equalsIgnoreCase(nom_rubro)){
				return false;
			}
		}
		return true;
	}
	

	
	public List llenarTabla(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB){
		if (IDE_NRDTN!=null && !IDE_NRDTN.isEmpty() 
				&& IDE_GEPRO!=null && !IDE_GEPRO.isEmpty()
				&& IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){

			TablaGenerica tab_rep_global=utilitario.consultar(getSqlNominaGlobalEmpleados(IDE_NRDTN, IDE_GEPRO, IDE_NRRUB)); 
			lis_nom_columnas=new ArrayList();
			lis_datos_rol = new ArrayList<Object>();
			System.out.println("sql rep_global "+tab_rep_global.getSql());

			if (tab_rep_global.getTotalFilas()>0){

				TablaGenerica tab_rubros_agrupados=utilitario.consultar("select a.ide_nrrub,a.rubros from ( " +
				""+tab_rep_global.getSql()+"" +
				")a "+
				"group by a.ide_nrrub,a.rubros ");

				String ide_nrrub="";
				for (int i = 0; i < tab_rubros_agrupados.getTotalFilas(); i++) {
					ide_nrrub+=tab_rubros_agrupados.getValor(i, "IDE_NRRUB")+",";
				}
				try {
					ide_nrrub=ide_nrrub.substring(0,ide_nrrub.length()-1);
				} catch (Exception e) {
					// TODO: handle exception
				}

				TablaGenerica tab_rubros_orden=ser_nomina.getRubrosOrden(IDE_NRDTN, ide_nrrub);

				for (int i = 0; i < tab_rubros_orden.getTotalFilas(); i++) {
					if (validarRubroRepetido(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"))){
						lis_nom_columnas.add(tab_rubros_orden.getValor(i, "DETALLE_NRRUB"));
					}
				}
				
				List<Object> lisq = new ArrayList<Object>();
				Object [] obj_columnas=new Object[7+lis_nom_columnas.size()]; 

				obj_columnas[0]=tab_rep_global.getValor(0, "DEPARTAMENTO");
				obj_columnas[1]=tab_rep_global.getValor(0, "CEDULA");
				obj_columnas[2]=tab_rep_global.getValor(0, "APELLIDO");
				obj_columnas[3]=tab_rep_global.getValor(0, "NOMBRE");
				obj_columnas[4]=tab_rep_global.getValor(0, "GENERO");
				obj_columnas[5]=tab_rep_global.getValor(0, "CARGO");
				obj_columnas[6]=tab_rep_global.getValor(0, "CONTRATO");

				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					obj_columnas[i+7]="0.00";
				}

				String str_ide_gtemp="";

				lis_datos_rol = new ArrayList<Object>();


				double [] totales=new double[lis_nom_columnas.size()];
				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					totales[i]=0;
				}

				double [] totales_consolidado=new double[lis_nom_columnas.size()];
				for (int i = 0; i < lis_nom_columnas.size(); i++) {
					totales_consolidado[i]=0;
				}

				String str_ide_sucu="";
				if (tab_rep_global.getTotalFilas()>0){
					str_ide_gtemp=tab_rep_global.getValor(0, "IDE_GTEMP");
					str_ide_sucu=tab_rep_global.getValor(0, "IDE_SUCU");
				}
				int bandera=0;
				int int_indice_sucursal=0;
				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
					if (str_ide_sucu.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_SUCU"))){
						if (str_ide_gtemp.equalsIgnoreCase(tab_rep_global.getValor(i, "IDE_GTEMP"))){
							try {

								BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
								big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);

								int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
								if (indice>-1){
									obj_columnas[indice]=""+big_valor;
									totales[indice-7]=totales[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
									totales_consolidado[indice-7]=totales_consolidado[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
								}
							} catch (Exception e) {
							}
						}else{
							str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
							lisq.add(obj_columnas);

							obj_columnas=new Object[7+lis_nom_columnas.size()];
							obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
							obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
							obj_columnas[2]=tab_rep_global.getValor(i, "APELLIDO");
							obj_columnas[3]=tab_rep_global.getValor(i, "NOMBRE");
							obj_columnas[4]=tab_rep_global.getValor(i, "GENERO");
							obj_columnas[5]=tab_rep_global.getValor(i, "CARGO");
							obj_columnas[6]=tab_rep_global.getValor(i, "CONTRATO");

							for (int k = 0; k < lis_nom_columnas.size(); k++) {
								obj_columnas[k+7]="0.00";
							}

							BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
							big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);

							int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
							if (indice>-1){
								obj_columnas[indice]=""+big_valor;
								totales[indice-7]=totales[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
								totales_consolidado[indice-7]=totales_consolidado[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));							
							}
						}
					}else{
						lisq.add(obj_columnas);
						bandera=0;
						str_ide_sucu=tab_rep_global.getValor(i, "IDE_SUCU");
						int_indice_sucursal=i;
						Object [] obj=new Object[2+totales.length]; 
						obj[0]=tab_rep_global.getValor(i-1, "SUCURSAL");
						obj[1]=lisq;
						for (int j = 0; j < totales.length; j++) {
							BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales[j]+""));
							big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
							obj[j+2]=""+big_valor;
						}
						lis_datos_rol.add(obj);

						lisq = new ArrayList<Object>();
						for (int k = 0; k < lis_nom_columnas.size(); k++) {
							totales[k]=0;
						}

						obj_columnas=new Object[7+lis_nom_columnas.size()];
						obj_columnas[0]=tab_rep_global.getValor(i, "DEPARTAMENTO");
						obj_columnas[1]=tab_rep_global.getValor(i, "CEDULA");
						obj_columnas[2]=tab_rep_global.getValor(i, "APELLIDO");
						obj_columnas[3]=tab_rep_global.getValor(i, "NOMBRE");
						obj_columnas[4]=tab_rep_global.getValor(i, "GENERO");
						obj_columnas[5]=tab_rep_global.getValor(i, "CARGO");
						obj_columnas[6]=tab_rep_global.getValor(i, "CONTRATO");

						for (int k = 0; k < lis_nom_columnas.size(); k++) {
							obj_columnas[k+7]="0.00";
						}

						BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_rep_global.getValor(i, "MONTO")));
						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);

						int indice=getIndiceRubro(tab_rep_global.getValor(i, "RUBROS"));
						if (indice>-1){
							obj_columnas[indice]=""+big_valor;
							totales[indice-7]=totales[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));
							totales_consolidado[indice-7]=totales_consolidado[indice-7]+Double.parseDouble(tab_rep_global.getValor(i, "MONTO"));							
						}
						str_ide_gtemp=tab_rep_global.getValor(i, "IDE_GTEMP");
					}

				}

				if (bandera==0){
					lisq.add(obj_columnas);
					Object [] obj=new Object[2+totales.length]; 
					obj[0]=tab_rep_global.getValor(int_indice_sucursal, "SUCURSAL");
					obj[1]=lisq;
					for (int j = 0; j < totales.length; j++) {
						//					BigDecimal big_valor=new BigDecimal(totales[j]);
						BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales[j]+""));
						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
						obj[j+2]=""+big_valor;
					}
					lis_datos_rol.add(obj);
				}
				lis_totales_consolidado=new ArrayList();
				for (int j = 0; j < totales_consolidado.length; j++) {
					BigDecimal big_valor=new BigDecimal(Double.parseDouble(totales_consolidado[j]+""));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					lis_totales_consolidado.add(""+big_valor);
				}
				return lis_datos_rol;
			}else{
				return null;
			}

		}else{
			return null;
		}
	}

	@Override
	public void insertar() {
		//		tab_tabla.insertar();
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
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
	}


	private ValueExpression crearValueExpression(String valueExpression,Class c) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", c);
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


	public SeleccionCalendario getSec_rango() {
		return sec_rango;
	}


	public void setSec_rango(SeleccionCalendario sec_rango) {
		this.sec_rango = sec_rango;
	}


}
