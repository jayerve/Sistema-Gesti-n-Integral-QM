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
import paq_contabilidad.ejb.ServicioContabilidad;

import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;
import org.primefaces.component.subtable.SubTable;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_precontractual.ejb.ServicioPrecontractual;
import paq_sistema.aplicacion.Pantalla;

public class precon_reporte_gerencial_general extends Pantalla {
	private List<Object> lis_datos_ejc = new ArrayList<Object>();

	private Division div_division = new Division();
	private DataTable tabla= new DataTable();
	private Grid gri=new Grid();
	//private SeleccionTabla set_precontractual=new SeleccionTabla();
	private Combo com_anio=new Combo();

	@EJB
	private ServicioPrecontractual ser_presupuesto = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);
	@EJB
    private ServicioPrecontractual ser_precontractual = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);
	
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	public precon_reporte_gerencial_general() {

		bar_botones.limpiar();
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_excel = new Boton();
		bot_excel.setValue("Exportar a Excel");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarExcel");
		bar_botones.agregarBoton(bot_excel);
		
		Boton bot_excelPac = new Boton();
		bot_excelPac.setValue("Exportar EXCEL PAC");
		bot_excelPac.setAjax(false);
		bot_excelPac.setMetodo("exportarExcel");
		bar_botones.agregarBoton(bot_excel);
		
		Boton bot_excel2=new Boton();
		bot_excel2.setValue("Exportar EXCEL POA");
		bot_excel2.setIcon("ui-icon-calculator");
		bot_excel2.setAjax(false);
		bot_excel2.setMetodo("exportarExcelPOA");
	    bar_botones.agregarBoton(bot_excel2);
		
		cargarTabla(16);
		
		div_division.setId("div_division");
		agregarComponente(div_division);

	}
	
	public void seleccionaElAnio (){
		cargarTabla(pckUtilidades.CConversion.CInt(com_anio.getValue()));
	}
		
	public void cargarTabla(int ide_geani){
		lis_datos_ejc.clear();
		llenarTabla(ide_geani);
		//dibujarTabla();
	}
		
	public void llenarTabla(int ide_geani){
		TablaGenerica tab_procesos=utilitario.consultar("SELECT ide_prpac, fecha_inicio_prpac, trimestre, estado_proceso, objeto, "
				+ "       procedimiento, area, responsable, total_tareas, total_cargado, "
				+ "       avance, actividad_actual_prpre, fecha_limite, fecha_presenta_prdoc, "
				+ "       dias_retraso "
				+ "  FROM matriz_contrataciones_resumen;");
		
		TablaGenerica tab_procesos_tareas=utilitario.consultar("SELECT ide_prpac, ide_preta,  "
				+ "       cast(fecha_limite-fecha_plazo as date) as fecha_inicio "
				+ "       ,fecha_limite as fecha_fin "
				+ "       ,case when etapa_inicio_prseg is null then false else true end as cumplida "
				+ "       ,max(fecha_presenta_prdoc) as fecha_documento "
				+ "       ,max(fecha_cambio_prseg) as fecha_carga_erp "
				+ "       ,(max(fecha_presenta_prdoc)-fecha_limite) as dias_retraso "
				+ "  FROM matriz_contrataciones_tareas "
				+ "  group by ide_prpac, ide_preta, fecha_limite,fecha_plazo,etapa_inicio_prseg "
				+ "  order by ide_prpac,ide_preta;");
		
		TablaGenerica tab_procesos_actividades=utilitario.consultar("SELECT ROW_NUMBER() OVER (ORDER BY descripcion_preta) AS orden, ide_preta, descripcion_preta FROM precon_etapa where ide_preta in (select distinct ide_preta from matriz_contrataciones_tareas) order by descripcion_preta;");
		
		lis_datos_ejc = new ArrayList<Object>();
		List<Object> lisp = new ArrayList<Object>();

		if (tab_procesos.getTotalFilas()>0){
			lisp = new ArrayList<Object>();
			for (int j = 0; j < tab_procesos.getTotalFilas(); j++) 
			{
				int ide_prpac = pckUtilidades.CConversion.CInt(tab_procesos.getValor(j, "ide_prpac"));
				int ide_preta = 0;
				
				Object [] obj_columnas_p=new Object[tab_procesos.getTotalColumnas()+tab_procesos_actividades.getTotalFilas()+1];
				
				for (int c = 0; c < tab_procesos.getTotalColumnas(); c++) {				
					obj_columnas_p[c]=tab_procesos.getValor(j,pckUtilidades.CConversion.CStr(tab_procesos.getColumnas()[c].getNombre()));	
					//System.out.println(obj_columnas_p[c]);
				}
				
				for (int a = 0; a < tab_procesos_tareas.getTotalFilas(); a++) 
				{
					if(ide_prpac==pckUtilidades.CConversion.CInt(tab_procesos_tareas.getValor(a, "ide_prpac")))
					{
						ide_preta=pckUtilidades.CConversion.CInt(tab_procesos_tareas.getValor(a, "ide_preta"));
						Object [] obj_columnas_t=new Object[tab_procesos_tareas.getTotalColumnas()];
						
						for (int c = 0; c < tab_procesos_tareas.getTotalColumnas(); c++) 
						{									
							obj_columnas_t[c]=tab_procesos_tareas.getValor(a,pckUtilidades.CConversion.CStr(tab_procesos_tareas.getColumnas()[c].getNombre()));	
							//System.out.println(obj_columnas_p[c]);
						}		
						
						for (int b = 0; b < tab_procesos_actividades.getTotalFilas(); b++) 
						{
							if(ide_preta==pckUtilidades.CConversion.CInt(tab_procesos_actividades.getValor(b, "ide_preta")))
							{
								int indice=pckUtilidades.CConversion.CInt(tab_procesos_actividades.getValor(b, "orden"));
								obj_columnas_p[tab_procesos.getTotalColumnas()+indice]=obj_columnas_t;		
							}
						}
						
					}
				}
				
				
				lisp.add(obj_columnas_p);
			}
			Object [] obj=new Object[2]; 
			obj[0]=lisp;
			lis_datos_ejc.add(obj);
			dibujarTabla(tab_procesos,tab_procesos_tareas,tab_procesos_actividades);
		}
		
	}

		
		public void dibujarTabla(TablaGenerica tab_procesos,TablaGenerica tab_procesos_tareas,TablaGenerica tab_procesos_actividades){
			//set_precontractual.cerrar();
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
			
			Column c1; 
			for (int c = 0; c < tab_procesos.getTotalColumnas(); c++) {				
				String nomCab=pckUtilidades.CConversion.CStr(tab_procesos.getColumnas()[c].getNombre());	
				//System.out.println(nomCab);				
				c1=new Column();
				c1.setHeaderText(nomCab.toUpperCase());
				c1.setWidth(120);
				c1.setRowspan(3);
				c1.setStyle("text-align:center;font-weight:bold;");
				r1.getChildren().add(c1);
				
			}

			SubTable subtable= new SubTable();
			subtable.setVar("emp");
			subtable.setValueExpression("value", crearValueExpression("suc[0]"));
			
			tabla.getChildren().add(subtable);
			for(int i=0;i<tab_procesos.getTotalColumnas();i++){
				Column c13_1=new Column();
				Etiqueta eti=new Etiqueta();
				eti.setValueExpression("value", "emp["+i+"]");
				eti.setStyle("text-align:center;color: black;font-size:9px;");
				/*if (i<8){
					tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
					//c13_1.setStyle("text-align:center;font-size:9px;font-weight:bold; ");
				}else */
				/*if (i==5){
					tabla.setRowStyleClass("background-color: #FFFFFF; color: #7f7f7f;");
				}else {*/
					tabla.setRowStyleClass("background-color: #FF0000; color: #FFFFFF;");
				//}
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
			
			gri_cabecera.getChildren().add(eti_titulo);
			//gri_cabecera.getChildren().add(eti_procedimiento);
			
			gri=new Grid();
			gri.setWidth("100%");
			gri.setStyle("display:block;height:100%");
			gri.getChildren().add(tabla);
			gri.setHeader(gri_cabecera);
			
			div_division.getChildren().clear();
			div_division.dividir1(gri);
		}
		public void exportarExcel() {
			if (lis_datos_ejc.size() > 0) {
				exportarXLS("seguimiento_precontractual.xls");
			}
		}
		public void exportarXLS(String nombre) {
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
				jxl.write.Label lab_titulo = new jxl.write.Label(1, 0, "SEGUIMIENTO PROCESO FASE PREPARATORIA Y PRECONTRACTUAL ", formato_celda_suc);
				hoja_xls.addCell(lab_titulo);
				cv.setAutosize(true);
				hoja_xls.setColumnView(0, cv);

				for (int i = 0; i < lis_datos_ejc.size(); i++) {
					Object[] fila = (Object[]) lis_datos_ejc.get(i);
					if (i == 0) {
						// NOMBRES DE COLUMNAS
						// CÓDIGO
						jxl.write.Label lab1 = new jxl.write.Label(0, 3, "Código", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(0, cv);

						// PROCESO
						lab1 = new jxl.write.Label(1, 3, "Proceso", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(1, cv);

						// ACTIVIDAD ACTUAL
						lab1 = new jxl.write.Label(2, 3, "Actividad Actual", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(2, cv);

						// ESTADO
						lab1 = new jxl.write.Label(3, 3, "Estado", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(3, cv);

						// TIEMPO ESTIMADO
						lab1 = new jxl.write.Label(4, 3, "Tiempo Estimado", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(4, cv);

						// TIEMPO OCUPADO
						lab1 = new jxl.write.Label(5, 3, "Tiempo Ocupado", formato_celda);
						hoja_xls.addCell(lab1);
						cv = new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(5, cv);
					}
					// lista de objetos que contiene todos los datos
					List<Object> lis_datos = (List<Object>) fila[0];

					for (int j = 0; j < lis_datos.size(); j++) {
						Object[] fila_datos = (Object[]) lis_datos.get(j);
						for (int k = 0; k < 6; k++) {
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
		
		public void exportarExcelPOA()
		{
			String sql="select a.ide_prpoa,detalle_geani,descripcion_prpre as proceso, actividad_actual_prpre, estado_proceso_prpre,a.ide_prpoa as codigoPOA,detalle_programa,detalle_proyecto,detalle_producto_mc as detalle_producto,detalle_actividad_mc as detalle_actividad,"
					+" codigo_subactividad,detalle_subactividad,"
					+" codigo_clasificador_prcla,descripcion_clasificador_prcla,"
					+" detalle_prfuf as fuente_financiamiento,detalle_geare as area,NOMBRES_APELLIDOS as tecnico_responsable, responsable_actual_prpre as responsable_actual"
					+" from pre_pac pac"
					+" left join pre_partida_pac ppac on ppac.ide_prpac=pac.ide_prpac"
					+" left join pre_poa a on a.ide_prpoa=ppac.ide_prpoa"
					+" left join gen_anio b on a.ide_geani= b.ide_geani"
					+" left join pre_clasificador c on a.ide_prcla = c.ide_prcla"
					+" left join pre_poa_financiamiento d on a.ide_prpoa = d.ide_prpoa"
					+" left join pre_fuente_financiamiento e on d.ide_prfuf = e.ide_prfuf"
					+" left join ("
					
					+"             select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,codigo_actividad,detalle_actividad,actividad,codigo_producto,cod_prod,detalle_producto,producto,"
					+"              codigo_actividad_mc,detalle_actividad_mc,actividad_mc,codigo_producto_mc,detalle_producto_mc,producto_mc,"
					+"              coalesce(pry1.codigo_proyecto,pry2.codigo_proyecto) as codigo_proyecto,"
					+"               coalesce(pry1.cod_pry,pry2.cod_pry) as cod_pry,"
					+"               coalesce(pry1.detalle_proyecto,pry2.detalle_proyecto) as detalle_proyecto,"
					+"               coalesce(pry1.proyecto,pry2.proyecto) as proyecto,"
					+"               coalesce(pry1.codigo_programa,pry2.codigo_programa) as codigo_programa,"
					+"               coalesce(pry1.detalle_programa,pry2.detalle_programa) as detalle_programa,"
					+"               coalesce(pry1.programa,pry2.programa) as programa from"
					
					+"              (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,"
					+"              detalle_prfup as detalle_subactividad, detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"              where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =7) a"
					
					+"              left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,"
					+"              detalle_prfup as detalle_actividad, detalle_prnfp as actividad from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"              where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =6) b on a.pre_ide_prfup = b.ide_prfup"
					
					+"              left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,"
					+"              detalle_prfup as detalle_producto, detalle_prnfp as producto, codigo_pry_prd_prfup as cod_prod from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"              where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =5) c on b.pre_ide_prfup = c.ide_prfup"
					
					+"              left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad_mc,"
					+"              detalle_prfup as detalle_actividad_mc,detalle_prnfp as actividad_mc from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"              where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =4) d on c.pre_ide_prfup = d.ide_prfup"
					
					+"              left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto_mc,"
					+"              detalle_prfup as detalle_producto_mc,detalle_prnfp as producto_mc from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"              where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =3 ) e on d.pre_ide_prfup = e.ide_prfup"
					
					+"              left join (select pr.ide_prfup, pr.pre_ide_prfup, codigo_proyecto,"
					+"                detalle_proyecto,proyecto, cod_pry,codigo_programa, detalle_programa,programa"
					+"                   from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,"
					+"                     detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto, codigo_pry_prd_prfup as cod_pry"
					+"                     from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"                     where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =2) pr"
					+"                   left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,"
					+"                     detalle_prfup as detalle_programa,detalle_prnfp as programa from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"                     where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry1 on c.pre_ide_prfup = pry1.ide_prfup"
					
					+"             left join (select pr.ide_prfup, pr.pre_ide_prfup, codigo_proyecto,"
					+"                detalle_proyecto,proyecto, cod_pry,codigo_programa, detalle_programa,programa"
					+"                   from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,"
					+"                     detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto, codigo_pry_prd_prfup as cod_pry"
					+"                     from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"                     where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =2) pr"
					+"                   left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,"
					+"                     detalle_prfup as detalle_programa,detalle_prnfp as programa from pre_funcion_programa a, pre_nivel_funcion_programa b"
					+"                     where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry2 on e.pre_ide_prfup = pry2.ide_prfup"
					
					+" ) f on a.ide_prfup = f.ide_prfup"
					+"   left join (select sum(dc.valor_certificado_prpoc) as certificado,dc.ide_prpoa,dc.ide_prfuf from pre_poa_certificacion dc,pre_certificacion c where c.ide_prcer=dc.ide_prcer group by dc.ide_prpoa,dc.ide_prfuf) h on a.ide_prpoa = h.ide_prpoa "
					+" left join gen_area g on a.ide_geare=g.ide_geare"
					+" left join pre_responsable_contratacion rpac on rpac.ide_prpac=pac.ide_prpac and rpac.activo_prrec=true"
					+" left join (SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP, coalesce(PRIMER_NOMBRE_GTEMP,'') || ' ' || coalesce(SEGUNDO_NOMBRE_GTEMP,'') || ' ' || coalesce(APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(APELLIDO_MATERNO_GTEMP,'') AS NOMBRES_APELLIDOS"
					+" 	   FROM GTH_EMPLEADO EMP ) erpac on erpac.ide_gtemp=rpac.ide_gtemp"
					
					+" join precon_precontractual pcon on pcon.ide_prpac=pac.ide_prpac"
					+" where ppac.certificado_poa_prpap=true "
					+" order by ide_prpre ";
			
			
		      Tabla tab_tablaXls = new Tabla();
		      tab_tablaXls.setSql(sql);
		      tab_tablaXls.ejecutarSql();
		      tab_tablaXls.exportarXLS();
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

		/*public SeleccionTabla getSet_precontractual() {
			return set_precontractual;
		}

		public void setSet_precontractual(SeleccionTabla set_precontractual) {
			this.set_precontractual = set_precontractual;
		}*/
}

