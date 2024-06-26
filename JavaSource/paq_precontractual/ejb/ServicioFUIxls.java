package paq_precontractual.ejb;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import paq_sistema.aplicacion.Utilitario;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;

@Stateless
public class ServicioFUIxls {
	
	File result; 
	WritableWorkbook archivo_xls ; 
	WritableSheet hojaFUI_xls; 
	WritableFont fuente;
	WritableCellFormat formato_celda;
	//String nombre="formato_unico_de_inventario.xls";
	String nombre="FUI.xls";
	Utilitario utilitario = new Utilitario();
	
	public ServicioFUIxls(){
		
	}
	
	public void exportarFUI(String ide_prpre, String subSerie, String nombreProceso, String responsable, String area, boolean expediente)
	{
		try { 

			//System.out.println("responsable: "+responsable);
			//System.out.println("area: "+area);
			
			TablaGenerica tab_documento_requisito=utilitario.consultar("SELECT pdr.*,descripcion_prreq,coalesce(descripcion_prtip,'N/A') as descripcion_prtip FROM precon_documento_requisito pdr "+
																		" left join precon_requisito pr on pr.ide_prreq=pdr.ide_prreq "+
																		" left join precon_tipologia tip on tip.ide_prtip=pdr.ide_prtip "+
																		" where con_ide_prdoc is null and ide_prpre="+ide_prpre+"  order by fecha_presenta_prdoc, ide_prdoc ");

			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 

			URL url = new URL(utilitario.getPropiedad("rutaDownload")+"upload/"+nombre);
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			Workbook archivo_xls2 = Workbook.getWorkbook(in);
			
			result = new File(extContext.getRealPath("/"+nombre)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2); 

			hojaFUI_xls = archivo_xls.getSheet("FUI FASE PRECONTRACTUAL");

			fuente = new WritableFont(WritableFont.TIMES, 10);
			formato_celda = new WritableCellFormat(fuente); 
			formato_celda.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda.setOrientation(Orientation.HORIZONTAL); 
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
			
			jxl.write.Label lab2;
			
			lab2 = new jxl.write.Label(2, 6,area, formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(2, 8,responsable, formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(14, 8,utilitario.getFechaActual(), formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			int fila=0;

			for (int i = 0; i < tab_documento_requisito.getTotalFilas(); i++) 
			{
				//Cargamos nro secuencial
				lab2 = new jxl.write.Label(0, fila+14,(fila+1)+"", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(1, fila+14,pckUtilidades.Utilitario.quitarAcentos("EXPEDIENTE DE CONTRATACIÓN PÚBLICA").toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(2, fila+14,pckUtilidades.Utilitario.quitarAcentos(subSerie).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(3, fila+14,pckUtilidades.Utilitario.quitarAcentos(nombreProceso).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				//lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValorArreglo(i, "ide_prreq", 1)).toUpperCase(), formato_celda);
				//lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "descripcion_prreq")).toUpperCase(), formato_celda);
				lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "descripcion_prtip")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(5, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "nro_documento_prdoc")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(6, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "asunto_prdoc")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(7, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "de_nombre_prdoc")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(8, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "para_nombre_prdoc")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				if(pckUtilidades.CConversion.CBol(tab_documento_requisito.getValor(i, "original_prdoc")))
				{	
					lab2 = new jxl.write.Label(9, fila+14,"X", formato_celda);
					hojaFUI_xls.addCell(lab2);
					
					lab2 = new jxl.write.Label(25, fila+14,"ELECTRONICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}
				else
				{
					lab2 = new jxl.write.Label(10, fila+14,"X", formato_celda);
					hojaFUI_xls.addCell(lab2);
					
					lab2 = new jxl.write.Label(25, fila+14,"FISICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}
				
				lab2 = new jxl.write.Label(11, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "nro_fojas_prdoc")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(12, fila+14,tab_documento_requisito.getValor(i, "fecha_presenta_prdoc"), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				//lab2 = new jxl.write.Label(13, fila+14,tab_documento_requisito.getValor(i, "fecha_presenta_prdoc"), formato_celda);
				//hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(18, fila+14,"X", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				/*if(pckUtilidades.CConversion.CBol(tab_documento_requisito.getValor(i, "electronico_prdoc")))
				{
					lab2 = new jxl.write.Label(26, fila+14,"ELECTRONICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}
				else
				{
					lab2 = new jxl.write.Label(26, fila+14,"FISICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}*/
				
				lab2 = new jxl.write.Label(26, fila+14,"PUBLICA", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				String anexos="";
				String comentarios="";
				TablaGenerica tab_documento_req_sec=utilitario.consultar("select  1 as codigo, textcat_all(nro_documento_prdoc || ' \n ') as anexos, textcat_all(DETALLE_ANEXOS_PRDOC || ' \n ') as comentarios from precon_documento_requisito where coalesce(anexo_prdoc,false)=false and con_ide_prdoc="+pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor(i, "ide_prdoc")));
				if(tab_documento_req_sec.getTotalFilas()>0)
				{
					anexos=tab_documento_req_sec.getValor("anexos");
					comentarios=tab_documento_req_sec.getValor("comentarios");
				}
				
				//lab2 = new jxl.write.Label(34, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "detalle_anexos_prdoc")).toUpperCase(), formato_celda);
				lab2 = new jxl.write.Label(33, fila+14,pckUtilidades.Utilitario.quitarAcentos(anexos).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				lab2 = new jxl.write.Label(34, fila+14,pckUtilidades.Utilitario.quitarAcentos(comentarios).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				fila++;
				
				if(!expediente)
				{
					TablaGenerica tab_documento_req_secundario=utilitario.consultar("select * from precon_documento_requisito where coalesce(anexo_prdoc,false)=true and con_ide_prdoc="+tab_documento_requisito.getValor(i, "ide_prdoc"));
					
					if(tab_documento_req_secundario.getTotalFilas()>0)
					{
						for (int j = 0; j < tab_documento_req_secundario.getTotalFilas(); j++) 
						{
							//Cargamos nro secuencial
							lab2 = new jxl.write.Label(0, fila+14,(fila+1)+"", formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(1, fila+14,pckUtilidades.Utilitario.quitarAcentos("EXPEDIENTE DE CONTRATACIÓN PÚBLICA").toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(2, fila+14,pckUtilidades.Utilitario.quitarAcentos(subSerie).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(3, fila+14,pckUtilidades.Utilitario.quitarAcentos(nombreProceso).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "descripcion_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(5, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "nro_documento_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(6, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "asunto_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(7, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "de_nombre_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(8, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "para_nombre_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							if(pckUtilidades.CConversion.CBol(tab_documento_req_secundario.getValor(j, "original_prdoc")))
							{	
								lab2 = new jxl.write.Label(9, fila+14,"X", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}
							else
							{
								lab2 = new jxl.write.Label(10, fila+14,"X", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}
							
							lab2 = new jxl.write.Label(11, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "nro_fojas_prdoc")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(12, fila+14,tab_documento_req_secundario.getValor(j, "fecha_presenta_prdoc"), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							//lab2 = new jxl.write.Label(13, fila+14,tab_documento_req_secundario.getValor(j, "fecha_presenta_prdoc"), formato_celda);
							//hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(18, fila+14,"X", formato_celda);
							hojaFUI_xls.addCell(lab2);
							/*
							if(pckUtilidades.CConversion.CBol(tab_documento_req_secundario.getValor(j, "electronico_prdoc")))
							{
								lab2 = new jxl.write.Label(26, fila+14,"ELECTRONICO", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}
							else
							{
								lab2 = new jxl.write.Label(26, fila+14,"FISICO", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}*/
							
							lab2 = new jxl.write.Label(26, fila+14,"PUBLICA", formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							//lab2 = new jxl.write.Label(34, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "detalle_anexos_prdoc")).toUpperCase(), formato_celda);
							//hojaFUI_xls.addCell(lab2);
						
							fila++;
						}
					}
				}

			}
			
			archivo_xls.write(); 
			archivo_xls.close(); 
			archivo_xls2.close();
			in.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() +"/"+nombre);
			//FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/FUI_"+nombreProceso+".xls");
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
	}

	public void exportarFUIejecucion(String ide_prcej, String subSerie, String nombreProceso, String responsable, String area, boolean expediente)
	{
		try { 

			TablaGenerica tab_documento_requisito=utilitario.consultar("SELECT pdr.*,descripcion_prreq,coalesce(descripcion_prtip,'N/A') as descripcion_prtip "
																		+ " FROM pre_contrato_archivo pdr "+
																		" left join precon_requisito pr on pr.ide_prreq=pdr.ide_prreq "+
																		" left join precon_tipologia tip on tip.ide_prtip=pdr.ide_prtip "+
																		" where con_ide_prcar is null and ide_prcej="+ide_prcej+"  order by fecha_prcar, ide_prcar ");

			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 

			URL url = new URL(utilitario.getPropiedad("rutaDownload")+"upload/"+nombre);
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			Workbook archivo_xls2 = Workbook.getWorkbook(in);
			
			result = new File(extContext.getRealPath("/"+nombre)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2); 

			hojaFUI_xls = archivo_xls.getSheet("FUI FASE CONTRACTUAL");

			fuente = new WritableFont(WritableFont.TIMES, 10);
			formato_celda = new WritableCellFormat(fuente); 
			formato_celda.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda.setOrientation(Orientation.HORIZONTAL); 
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
			
			jxl.write.Label lab2;
			
			lab2 = new jxl.write.Label(2, 6,area, formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(2, 8,responsable, formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(14, 8,utilitario.getFechaActual(), formato_celda);
			hojaFUI_xls.addCell(lab2);
			
			int fila=0;

			for (int i = 0; i < tab_documento_requisito.getTotalFilas(); i++) 
			{
				//Cargamos nro secuencial
				lab2 = new jxl.write.Label(0, fila+14,(fila+1)+"", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(1, fila+14,pckUtilidades.Utilitario.quitarAcentos("EXPEDIENTE DE CONTRATACIÓN PÚBLICA").toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(2, fila+14,pckUtilidades.Utilitario.quitarAcentos(subSerie).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(3, fila+14,pckUtilidades.Utilitario.quitarAcentos(nombreProceso).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);

				lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "descripcion_prtip")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(5, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "nro_documento_prcar")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(6, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "asunto_prcar")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(7, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "de_nombre_prcar")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(8, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "para_nombre_prcar")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				if(pckUtilidades.CConversion.CBol(tab_documento_requisito.getValor(i, "original_prcar")))
				{	
					lab2 = new jxl.write.Label(9, fila+14,"X", formato_celda);
					hojaFUI_xls.addCell(lab2);
					
					lab2 = new jxl.write.Label(25, fila+14,"ELECTRONICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}
				else
				{
					lab2 = new jxl.write.Label(10, fila+14,"X", formato_celda);
					hojaFUI_xls.addCell(lab2);
					
					lab2 = new jxl.write.Label(25, fila+14,"FISICO", formato_celda);
					hojaFUI_xls.addCell(lab2);
				}
				
				lab2 = new jxl.write.Label(11, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "nro_fojas_prcar")).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(12, fila+14,tab_documento_requisito.getValor(i, "fecha_prcar"), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(18, fila+14,"X", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				lab2 = new jxl.write.Label(26, fila+14,"PUBLICA", formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				String anexos="";
				String comentarios="";
				TablaGenerica tab_documento_req_sec=utilitario.consultar("select  1 as codigo, textcat_all(nro_documento_prcar || ' \n ') as anexos, ' - ' as comentarios from pre_contrato_archivo where coalesce(anexo_prcar,false)=false and con_ide_prcar="+pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor(i, "ide_prcar")));
				if(tab_documento_req_sec.getTotalFilas()>0)
				{
					anexos=tab_documento_req_sec.getValor("anexos");
					comentarios=tab_documento_req_sec.getValor("comentarios");
				}
				
				//lab2 = new jxl.write.Label(34, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_requisito.getValor(i, "detalle_anexos_prdoc")).toUpperCase(), formato_celda);
				lab2 = new jxl.write.Label(33, fila+14,pckUtilidades.Utilitario.quitarAcentos(anexos).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				lab2 = new jxl.write.Label(34, fila+14,pckUtilidades.Utilitario.quitarAcentos(comentarios).toUpperCase(), formato_celda);
				hojaFUI_xls.addCell(lab2);
				
				fila++;
				
				if(!expediente)
				{
					TablaGenerica tab_documento_req_secundario=utilitario.consultar("select * from pre_contrato_archivo where coalesce(anexo_prcar,false)=true and con_ide_prcar="+tab_documento_requisito.getValor(i, "ide_prcar"));
					
					if(tab_documento_req_secundario.getTotalFilas()>0)
					{
						for (int j = 0; j < tab_documento_req_secundario.getTotalFilas(); j++) 
						{
							//Cargamos nro secuencial
							lab2 = new jxl.write.Label(0, fila+14,(fila+1)+"", formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(1, fila+14,pckUtilidades.Utilitario.quitarAcentos("EXPEDIENTE DE CONTRATACIÓN PÚBLICA").toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(2, fila+14,pckUtilidades.Utilitario.quitarAcentos(subSerie).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(3, fila+14,pckUtilidades.Utilitario.quitarAcentos(nombreProceso).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(4, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "nro_documento_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(5, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "nro_documento_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(6, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "asunto_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(7, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "de_nombre_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(8, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "para_nombre_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							if(pckUtilidades.CConversion.CBol(tab_documento_req_secundario.getValor(j, "original_prcar")))
							{	
								lab2 = new jxl.write.Label(9, fila+14,"X", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}
							else
							{
								lab2 = new jxl.write.Label(10, fila+14,"X", formato_celda);
								hojaFUI_xls.addCell(lab2);
							}
							
							lab2 = new jxl.write.Label(11, fila+14,pckUtilidades.Utilitario.quitarAcentos(tab_documento_req_secundario.getValor(j, "nro_fojas_prcar")).toUpperCase(), formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(12, fila+14,tab_documento_req_secundario.getValor(j, "presenta_prcar"), formato_celda);
							hojaFUI_xls.addCell(lab2);

							
							lab2 = new jxl.write.Label(18, fila+14,"X", formato_celda);
							hojaFUI_xls.addCell(lab2);
							
							lab2 = new jxl.write.Label(26, fila+14,"PUBLICA", formato_celda);
							hojaFUI_xls.addCell(lab2);
							

						
							fila++;
						}
					}
				}

			}
			
			archivo_xls.write(); 
			archivo_xls.close(); 
			archivo_xls2.close();
			in.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() +"/"+nombre);
			//FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/FUI_"+nombreProceso+".xls");
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
	}

}
