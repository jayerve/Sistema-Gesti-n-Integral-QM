package paq_precontractual;

import java.io.File;
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
import org.primefaces.component.row.Row;
import org.primefaces.component.subtable.SubTable;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.SeleccionTabla;
import paq_precontractual.ejb.ServicioPrecontractual;
import paq_sistema.aplicacion.Pantalla;

public class precon_reporte_gerencial extends Pantalla {
	private List<Object> lis_datos_ejc = new ArrayList<Object>();

	private Division div_division = new Division();
	private DataTable tabla= new DataTable();
	private Grid gri=new Grid();
	private SeleccionTabla set_precontractual=new SeleccionTabla();

	@EJB
	private ServicioPrecontractual ser_presupuesto = (ServicioPrecontractual) utilitario
			.instanciarEJB(ServicioPrecontractual.class);
	@EJB
    private ServicioPrecontractual ser_precontractual = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);

	public precon_reporte_gerencial() {

		bar_botones.limpiar();
		
		Boton bot_importar = new Boton();
		bot_importar.setValue("Ver Proceso");
		bot_importar.setMetodo("aceptarContratacion");// implementar metodo filtrar
		bot_importar.setIcon("ui-icon-clock");
		bar_botones.agregarComponente(bot_importar);
		
		Boton bot_excel = new Boton();
		bot_excel.setValue("Exportar a Excel");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarExcel");
		bar_botones.agregarBoton(bot_excel);
		
						
		//Ventana emergente seleccionar tipo de contratacion
		set_precontractual.setId("set_precontractual");
		set_precontractual.setSeleccionTabla(ser_precontractual.getProceso(),"ide_prpre");
		set_precontractual.setTitle("Seleccione el Proceso de Contratación ");
		set_precontractual.getTab_seleccion().getColumna("descripcion_prpre").setNombreVisual("Proceso Contratación");
		set_precontractual.getTab_seleccion().getColumna("descripcion_prpre").setFiltroContenido();
		set_precontractual.getTab_seleccion().getColumna("estado_proceso_prpre").setNombreVisual("Estado Proceso");
		set_precontractual.getBot_aceptar().setMetodo("cargaEjecucion");
		set_precontractual.setRadio();
		agregarComponente(set_precontractual);
		
		cargarTabla(pckUtilidades.CConversion.CInt(set_precontractual.getValorSeleccionado()));
		
		div_division.setId("div_division");
		agregarComponente(div_division);

	}
		
	public void aceptarContratacion(){
		set_precontractual.dibujar();
	}
	
	public void cargaEjecucion(){
		set_precontractual.getValorSeleccionado();
		cargarTabla(pckUtilidades.CConversion.CInt(set_precontractual.getValorSeleccionado()));
		utilitario.addUpdate("div_division");
	}

	public void limpiar(){
		//cargarTabla(proceso);
		utilitario.addUpdate("div_division");
	}

	public void cargarTabla(int proceso){
		lis_datos_ejc.clear();
		llenarTabla(proceso);
		dibujarTabla(proceso);
	}
		
	public void llenarTabla(int proceso){
		TablaGenerica tab_rep_global=utilitario.consultar("select s.etapa_inicio_prseg,  (s.responsable_prseg || ' ' || s.departamento_prseg ) AS Responsable,  s.etapa_fin_prseg, (s.responsable_asignado_prseg || ' ' || s.departamento_asignado_prseg ) AS Responsable_final, s.fecha_asignacion_prseg, s.hora_asignacion_prseg, s.fecha_cambio_prseg,"
							+ "s.hora_cambio_prseg, (ep.numero_hora_dia_pretp || ' ' || ep.hora_dia_pretp) AS Tiempo_estimado,"
							+ "  CASE WHEN (ep.hora_dia_pretp = 'DIA')  THEN (SELECT EXTRACT(DAY FROM age(s.fecha_cambio_prseg , s.fecha_asignacion_prseg) )) "
							+ " WHEN (hora_dia_pretp = 'HORA') THEN (SELECT EXTRACT(HOUR FROM (s.hora_cambio_prseg - s.hora_asignacion_prseg) )) "
							+ " END AS Tiempo_ocupado, "
							+ " s.observacion_prseg, s.estado_actividad_prseg, s.estado_procedimiento_prseg  "
							+ " from  precon_etapa_procedimiento as ep, precon_etapa as e, precon_seguimiento as s "
							+ " where s.ide_preta_prseg = e.ide_preta  and e.ide_preta = ep.ide_preta and ep.ide_prpro in (1,(select ide_pretp from precon_precontractual where ide_prpre='"
							+ proceso 
							+ "')) and  s.ide_prpre ="
							+ proceso
							+ " order by s.ide_preta_prseg"); 
		lis_datos_ejc = new ArrayList<Object>();
		List<Object> lisq = new ArrayList<Object>();
		Object [] obj_columnas=new Object[13]; 
		double [] totales=new double[5];
		//System.out.println("sql rep_global "+tab_rep_global.getSql());
		if (tab_rep_global.getTotalFilas()>0){
			lisq = new ArrayList<Object>();
			for (int j = 0; j < tab_rep_global.getTotalFilas(); j++) {
				obj_columnas=new Object[13];
				obj_columnas[0]=tab_rep_global.getValor(j, "etapa_inicio_prseg");
				obj_columnas[1]=tab_rep_global.getValor(j, "responsable");
				obj_columnas[2]=tab_rep_global.getValor(j, "etapa_fin_prseg");
				obj_columnas[3]=tab_rep_global.getValor(j, "responsable_final");
				obj_columnas[4]=tab_rep_global.getValor(j, "fecha_asignacion_prseg");
				obj_columnas[5]=tab_rep_global.getValor(j, "hora_asignacion_prseg");
				obj_columnas[6] = tab_rep_global.getValor(j, "fecha_cambio_prseg");
				obj_columnas[7]=tab_rep_global.getValor(j, "hora_cambio_prseg");
				obj_columnas[8]=tab_rep_global.getValor(j, "tiempo_estimado");
				obj_columnas[9]=tab_rep_global.getValor(j, "tiempo_ocupado");
				obj_columnas[10]=tab_rep_global.getValor(j, "observacion_prseg");
				obj_columnas[11]=tab_rep_global.getValor(j, "estado_actividad_prseg");
				obj_columnas[12]=tab_rep_global.getValor(j, "estado_procedimiento_prseg");
				lisq.add(obj_columnas);
			}
			Object [] obj=new Object[2+totales.length]; 
			obj[0]=lisq;
			lis_datos_ejc.add(obj);
		}
	}

		
		public void dibujarTabla(int proceso){
			set_precontractual.cerrar();
			tabla=new DataTable();
			tabla.setId("tabla");
			tabla.setResizableColumns(true);
			tabla.setStyle("font-size:13px");
			//tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
			tabla.setVar("suc");
			tabla.setValueExpression("value", crearValueExpression("pre_index.clase.lis_datos_ejc"));

			ColumnGroup columnGroup=new ColumnGroup();
			columnGroup.setType("header");
			tabla.getChildren().add(columnGroup);

			Row r1=new Row();
			columnGroup.getChildren().add(r1);

			Column c1=new Column();
			c1.setHeaderText("Actividad Inicial");
			c1.setWidth(120);
			c1.setRowspan(3);
			c1.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c1);

			Column c2=new Column();
			c2.setHeaderText("Responsable");
			c2.setWidth(150);
			c2.setRowspan(3);
			c2.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c2);

			Column c3=new Column();
			c3.setHeaderText("Actividad Final");
			c3.setRowspan(3);
			c3.setWidth(200);
			c3.setStyle("text-align:center;font-weight:bold;");
			c3.setResizable(true);
			r1.getChildren().add(c3);

			Column c4=new Column();
			c4.setHeaderText("Responsable");
			c4.setWidth(100);
			c4.setRowspan(3);
			c4.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c4);

			Column c5=new Column();
			c5.setHeaderText("Fecha Asignación");
			c5.setWidth(120);
			c5.setRowspan(3);
			c5.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c5);
			
			Column c6=new Column();
			c6.setHeaderText("Hora Asignación");
			c6.setWidth(120);
			c6.setRowspan(3);
			c6.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c6);

			Column c7=new Column();
			c7.setHeaderText("Fecha Cambio");
			c7.setWidth(100);
			c7.setRowspan(3);
			c7.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c7);
			
			Column c8=new Column();
			c8.setHeaderText("Hora Cambio");
			c8.setWidth(100);
			c8.setRowspan(3);
			c8.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c8);
			
			Column c9=new Column();
			c9.setHeaderText("Tiempo Estimado");
			c9.setWidth(120);
			c9.setRowspan(3);
			c9.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c9);
			
			Column c10=new Column();
			c10.setHeaderText("Tiempo Ocupado");
			c10.setWidth(120);
			c10.setRowspan(3);
			c10.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c10);
			
			
			Column c11=new Column();
			c11.setHeaderText("Observación");
			c11.setWidth(100);
			c11.setRowspan(3);
			c11.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c11);
			
			Column c12=new Column();
			c12.setHeaderText("Estado Actividad");
			c12.setWidth(120);
			c12.setRowspan(3);
			c12.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c12);
			
			
			Column c13=new Column();
			c13.setHeaderText("Procedimiento");
			c13.setWidth(100);
			c13.setRowspan(3);
			c13.setStyle("text-align:center;font-weight:bold;");
			r1.getChildren().add(c13);
						
			SubTable subtable= new SubTable();
			subtable.setVar("emp");
			subtable.setValueExpression("value", crearValueExpression("suc[0]"));
			
			tabla.getChildren().add(subtable);
			for(int i=0;i<13;i++){
				Column c13_1=new Column();
				Etiqueta eti=new Etiqueta();
				eti.setValueExpression("value", "emp["+i+"]");
				eti.setStyle("text-align:center;color: black;font-size:9px;");
				if (i<8){
					tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
					//c13_1.setStyle("text-align:center;font-size:9px;font-weight:bold; ");
				}else if (i==8){
					tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
				}else {
					tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
				}
				c13_1.setResizable(true);

				c13_1.getChildren().add(eti);
				subtable.getChildren().add(c13_1);
			}

			tabla.setSelectionMode("single");

			Grid gri_cabecera=new Grid();
			gri_cabecera.setWidth("100%");

			Etiqueta eti_titulo=new Etiqueta();
			eti_titulo.setEstiloCabecera("font-size: 13px;color: white;font-weight: bold;text-align: center");
			eti_titulo.setValue("SEGUIMIENTO PROCESO FASE PREPARATORIA Y PRECONTRACTUAL ");
			
			Etiqueta eti_procedimiento=new Etiqueta();
			eti_procedimiento.setEstiloCabecera("font-size: 13px;color: white;font-weight: bold;text-align: center");
			eti_procedimiento.setValue(cargarProcedimeintoPrecontractual(proceso));
		
			gri_cabecera.getChildren().add(eti_titulo);
			gri_cabecera.getChildren().add(eti_procedimiento);
			
			gri=new Grid();
			gri.setWidth("100%");
			gri.setStyle("display:block;height:100%");
			gri.getChildren().add(tabla);
			gri.setHeader(gri_cabecera);
			
			div_division.getChildren().clear();
			div_division.dividir1(gri);

		}
		
		public String cargarProcedimeintoPrecontractual(Integer id_prpre){
			String procedimiento="";
			String ser_procedimiento=ser_presupuesto.getReportePrecontractual(id_prpre);;
			TablaGenerica tab_estadoProcedimiento=utilitario.consultar(ser_procedimiento);
			procedimiento=tab_estadoProcedimiento.getValor("descripcion_prpre");
			return procedimiento;
		}
		
		public void exportarExcel() {
			if (lis_datos_ejc.size() > 0) {
				exportarXLS("seguimiento_precontractual.xls",pckUtilidades.CConversion.CInt(set_precontractual.getValorSeleccionado()));
			}
		}
		
		public void exportarXLS(String nombre,Integer ide_prpre) {
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
				int int_fila = 3;
				CellView cv = new CellView();
				jxl.write.Label lab_titulo = new jxl.write.Label(0, 0, "SEGUIMIENTO PROCESO FASE PREPARATORIA Y PRECONTRACTUAL ", formato_celda_suc);
				//jxl.write.Label lab_titulo = new jxl.write.Label(1, 0, cargarProcedimeintoPrecontractual(ide_prpre), formato_celda_suc);
				hoja_xls.addCell(lab_titulo);
				cv.setAutosize(true);
				hoja_xls.setColumnView(0, cv);

				for (int i = 0; i < lis_datos_ejc.size(); i++) {
					Object[] fila = (Object[]) lis_datos_ejc.get(i);
					if (i == 0) {
						// NOMBRES DE COLUMNAS
						// CÓDIGO
						jxl.write.Label lab1 = new jxl.write.Label(0, 3, "Actividad Inicial", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(0, cv);

						// PROCESO
						lab1 = new jxl.write.Label(1, 3, "Responsable", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(1, cv);

						// ACTIVIDAD ACTUAL
						lab1 = new jxl.write.Label(2, 3, "Actividad Final", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(2, cv);

						// ESTADO
						lab1 = new jxl.write.Label(3, 3, "Responsable", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(3, cv);

						// TIEMPO ESTIMADO
						lab1 = new jxl.write.Label(4, 3, "Fecha Asignación", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(4, cv);

						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(5, 3, "Hora Asignación", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(5, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(6, 3, "Fecha Cambio", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(6, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(7, 3, "Hora Cambio", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(7, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(8, 3, "Tiempo Estimado", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(8, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(9, 3, "Tiempo Ocupado", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(9, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(10, 3, "Observación", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(10, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(11, 3, "Estado Actividad", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(11, cv);
						
						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(12, 3, "Procedimiento", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(12, cv);
					}
					// lista de objetos que contiene todos los datos
					List<Object> lis_datos = (List<Object>) fila[0];

					for (int j = 0; j < lis_datos.size(); j++) {
						Object[] fila_datos = (Object[]) lis_datos.get(j);
						for (int k = 0; k < 13; k++) {
							jxl.write.Label lab3 = new jxl.write.Label(k, i + int_fila + 1, fila_datos[k] + "", formato_celda);
							hoja_xls.addCell(lab3);
						}
						int_fila = int_fila + 1;

					}
					int_fila = int_fila + 1;
				}
				archivo_xls.write();
				archivo_xls.close();
				FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom);
			} catch (Exception e) {
				System.out.println("Error no se genero el XLS :" + e.getMessage());
			}
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
				
		public List<Object> getLis_datos_ejc() {
			return lis_datos_ejc;
		}

		public void setLis_datos_ejc(List<Object> lis_datos_ejc) {
			this.lis_datos_ejc = lis_datos_ejc;
		}
		
		public DataTable getTabla() {
			return tabla;
		}

		public void setTabla(DataTable tabla) {
			this.tabla = tabla;
		}

		private ValueExpression crearValueExpression(String valueExpression) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			return facesContext.getApplication().getExpressionFactory().createValueExpression(
					facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
		}

		public SeleccionTabla getSet_precontractual() {
			return set_precontractual;
		}

		public void setSet_precontractual(SeleccionTabla set_precontractual) {
			this.set_precontractual = set_precontractual;
		}
}

