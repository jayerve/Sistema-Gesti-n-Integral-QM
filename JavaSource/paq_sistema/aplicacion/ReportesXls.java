package paq_sistema.aplicacion;

import java.io.File;

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
import framework.componentes.Tabla;

public class ReportesXls {
	
	private static final long serialVersionUID = 1L;
	
	public void exportarInventarioXLS(Tabla tab_tabla2,String nombre,String tipo_nomina,String mes){
	      try {
	        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 
	        String nom = nombre; 
	        File result = new File(extContext.getRealPath("/" + nom));
	        WritableWorkbook archivo_xls = Workbook.createWorkbook(result); 
	        WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0); 
	        WritableFont fuente = new WritableFont(WritableFont.TAHOMA, 10);
	        WritableCellFormat formato_celda = new WritableCellFormat(fuente); 
	        formato_celda.setAlignment(jxl.format.Alignment.CENTRE);
	        formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
	        formato_celda.setOrientation(Orientation.HORIZONTAL); 
	        
	        WritableFont fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
	        WritableCellFormat formato_celda_black = new WritableCellFormat(fuente_suc); 
	        formato_celda_black.setAlignment(jxl.format.Alignment.CENTRE); 
	        formato_celda_black.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
	        formato_celda_black.setOrientation(Orientation.HORIZONTAL); 
	        formato_celda_black.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
	        
	        WritableCellFormat formato_celda_black_left = new WritableCellFormat(fuente_suc); 
	        formato_celda_black_left.setAlignment(jxl.format.Alignment.LEFT); 
	        formato_celda_black_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
	        formato_celda_black_left.setOrientation(Orientation.HORIZONTAL); 
	        formato_celda_black_left.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
	                
	        
	        WritableFont fuente_suc_red = new WritableFont(WritableFont.ARIAL, 11);
	        fuente_suc_red.setColour(Colour.RED);
	        WritableCellFormat formato_celda_red = new WritableCellFormat(fuente_suc_red); 
	        formato_celda_red.setAlignment(jxl.format.Alignment.CENTRE); 
	        formato_celda_red.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
	        formato_celda_red.setOrientation(Orientation.HORIZONTAL); 
	        formato_celda_red.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

	        CellView cv=new CellView();
	        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
	            //NOMBRES DE COLUMNAS
	            jxl.write.Label lab1 = new jxl.write.Label(0, 0, "SALDO MATERIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(0,cv);
	            
	          //NOMBRES DE COLUMNAS
	            jxl.write.Label lab3 = new jxl.write.Label(1, 0, "EXISTENCIA INICIAL", formato_celda);
	            hoja_xls.addCell(lab3);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(1,cv);
	            
	            //CODIGO MATERIAL 
	            lab1 = new jxl.write.Label(2, 0, "CODIGO MATERIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(2,cv);

	            // NOMBRE MATERIAL
	            lab1 = new jxl.write.Label(3, 0, "NOMBRE MATERIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(3,cv);
	            
	            //INGRESO MATERIAL
	            lab1 = new jxl.write.Label(4, 0, "INGRESO MATERIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(4,cv);
	            
	            //EGRESO MATERIAL
	            lab1 = new jxl.write.Label(5, 0, "EGRESO MATERIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(5,cv);
	            
	            //EGRESO MATERIAL
	            lab1 = new jxl.write.Label(6, 0, "COSTO INICIAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(6,cv);
	            
	            //EGRESO MATERIAL
	            lab1 = new jxl.write.Label(7, 0, "COSTO ANTERIOR", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(7,cv);
	            
	          //EGRESO MATERIAL
	            lab1 = new jxl.write.Label(8, 0, "COSTO ACTUAL", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(8,cv);
	            
	            //EGRESO MATERIAL
	            lab1 = new jxl.write.Label(9, 0, "FECHA INGRESO ARTICULO INVENTARIO", formato_celda);
	            hoja_xls.addCell(lab1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(9,cv);
	            
	            
	            
	            //Existencia actual desde el SQL
	            jxl.write.Label lab2 = new jxl.write.Label(0, i+1,tab_tabla2.getValor(i, "existencia_actual"), formato_celda_red);
	            hoja_xls.addCell(lab2);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(0,cv);
	            
	            //Codigo bomat desde el SQL
	            jxl.write.Label lab3_1 = new jxl.write.Label(1, i+1,tab_tabla2.getValor(i, "existencia_inicial_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(1,cv);
	            
	            //Codigo bomat desde el SQL
	            jxl.write.Label lab5 = new jxl.write.Label(2, i+1,tab_tabla2.getValor(i, "codigo_bomat"), formato_celda_black);
	            hoja_xls.addCell(lab5);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(2,cv);
	            
	            //Cargamos REFRECNIA
	            jxl.write.Label labnum1 = new jxl.write.Label(3, i+1,tab_tabla2.getValor(i, "detalle_bomat"), formato_celda_black_left);
	            hoja_xls.addCell(labnum1);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(3,cv);
	            
	            //Codigo bomat desde el SQL
	            jxl.write.Label lab3_2 = new jxl.write.Label(4, i+1,tab_tabla2.getValor(i, "ingreso_material_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_2);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(4,cv);
	            
	            jxl.write.Label lab3_3 = new jxl.write.Label(5, i+1,tab_tabla2.getValor(i, "egreso_material_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_3);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(5,cv);
	            
	            jxl.write.Label lab3_4 = new jxl.write.Label(6, i+1,tab_tabla2.getValor(i, "costo_inicial_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_4);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(6,cv);
	            
	            jxl.write.Label lab3_5 = new jxl.write.Label(7, i+1,tab_tabla2.getValor(i, "costo_anterior_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_5);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(7,cv);
	            
	            jxl.write.Label lab3_6 = new jxl.write.Label(8, i+1,tab_tabla2.getValor(i, "costo_actual_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_6);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(8,cv);
	            
	            jxl.write.Label lab3_7 = new jxl.write.Label(9, i+1,tab_tabla2.getValor(i, "fecha_ingr_articulo_boinv"), formato_celda_black);
	            hoja_xls.addCell(lab3_7);
	            cv=new CellView();
	            cv.setAutosize(true);
	            hoja_xls.setColumnView(9,cv);
	            
	        }
	        
	        archivo_xls.write(); 
	        archivo_xls.close(); 
	        FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom); 
	        
	      }catch(Exception e){
	        
	      }
	    }

	
	/*public void exportarCompromisosXLS(Tabla tab_tabla,String nombre)
    {
        try {
        	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 
	        String nom = nombre; 
	        File result = new File(extContext.getRealPath("/" + nom));
	        WritableWorkbook archivo_xls = Workbook.createWorkbook(result); 
          
            WritableSheet hoja_xls = archivo_xls.createSheet("Compromisos", 0);
            WritableFont fuente = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat formato_celda = new WritableCellFormat(fuente);
            formato_celda.setAlignment(jxl.format.Alignment.LEFT);
            formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formato_celda.setOrientation(Orientation.HORIZONTAL);
            formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);

            WritableFont fuente2 = new WritableFont(WritableFont.ARIAL, 12);
            WritableCellFormat formato_celda2 = new WritableCellFormat(fuente2);
            formato_celda2.setAlignment(jxl.format.Alignment.CENTRE);
            formato_celda2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formato_celda2.setOrientation(Orientation.HORIZONTAL);
            formato_celda2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);

            WritableFont fuente3 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat formato_celda3 = new WritableCellFormat(fuente3);
            formato_celda3.setAlignment(jxl.format.Alignment.CENTRE);
            formato_celda3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formato_celda3.setOrientation(Orientation.HORIZONTAL);
            formato_celda3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);

            int int_columna = 0;
            int int_fila = 0;
            jxl.write.Label lab;
            CellView cv;

                //cabecera
                ResultSetMetaData nameOfcolumn = eventos.getMetaData();
                int columnCount = nameOfcolumn.getColumnCount();

                for (int i = 1; i <= columnCount; i++ ) {
                    
                    String name = nameOfcolumn.getColumnLabel(i);
                    
                    lab = new Label(int_columna, int_fila, name, formato_celda2);
                    hoja_xls.addCell(lab);
                    cv = new CellView();
                    cv.setAutosize(true);
                    hoja_xls.setColumnView(int_columna, cv);
                    int_columna++;
                }

                /////////FIN CABECERA

                /////////////
                ///////////////////////DETALLES
                ////////////

                int_columna=0;
                int_fila++;

                while (eventos.next()) {
                    int_columna=0;
                    
                    for (int i = 1; i <= columnCount; i++ ) {
                        lab = new Label(int_columna, int_fila, CConversion.CStr(eventos.getString(i)), formato_celda);
                        hoja_xls.addCell(lab);
                        int_columna++;
                    }         
                    int_fila++;
                    //jxl.write.Number num = new jxl.write.Number(int_columna, int_fila, CConversion.CInt(eventos.getString("idUsuario")), formato_celda);
                    //hoja_xls.addCell(num);
                }
                eventos.close();
                archivo_xls.write();
                archivo_xls.close();
                System.out.println("exportarEventosXLS ok:" );
                FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/resources/" + this.nombreExcel + ".xls");
            
        } catch (IOException | WriteException | SQLException e) {
            retorno=0;
            System.out.println("Error no se genero el XLS :" + e.getMessage());
        }
        try {eventos.close();} catch (SQLException ex) {}
        return retorno;
    }*/
	
	
}
