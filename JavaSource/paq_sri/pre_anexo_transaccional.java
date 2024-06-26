package paq_sri;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.component.blockui.BlockUI;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import paq_sri.ejb.ServicioSRI;

public class pre_anexo_transaccional extends Pantalla {
	
	private Tabla tab_ats_informante = new Tabla();
    private Tabla tab_detalle_compras_ats=new Tabla();
    private Tabla tab_compras_forma_pago_ats=new Tabla();
    private Tabla tab_compras_retencion_ats=new Tabla();
    private Tabla tab_detalle_ventas_ast=new Tabla();
    private Tabla tab_ventas_establecimiento_ast=new Tabla();
    private Tabla tab_anulados_ast=new Tabla();

    private Combo com_anio=new Combo();
    private Combo com_mes=new Combo();
    
    File result; 
	WritableWorkbook archivo_xls ; 
	WritableSheet hojaInformante_xls; 
	WritableSheet hojaCompraDetallada_xls; 
	WritableSheet hojaComprasFormasPago_xls; 
	WritableSheet hojaComprasRetenciones_xls; 
	WritableSheet hojaVentasCliente_xls; 
	WritableSheet hojaVentasFormasCobro_xls; 
	WritableSheet hojaVentasEstablecimiento_xls; 
	WritableSheet hojaAnulados_xls; 
	
	WritableFont fuente;
	WritableFont fuente_suc;
	WritableFont fuente_totales;
	
	WritableCellFormat formato_celda; 
	WritableCellFormat formato_celda_suc;
	WritableCellFormat formato_celda_totales; 
	WritableCellFormat formato_celda_valor_rubro ; 

	private double total_ventas=0;
	private double total_nc=0;
    
    @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	@EJB
	private ServicioSRI ser_sri = (ServicioSRI) utilitario.instanciarEJB(ServicioSRI.class);
	
	public pre_anexo_transaccional (){
		
		bar_botones.limpiar();
		
		com_anio.setCombo(ser_contabilidad.getAnio("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes.setCombo(ser_contabilidad.getMes("true"));	
		com_mes.setMetodo("seleccionaElAnio");
		com_mes.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes:"));
		bar_botones.agregarComponente(com_mes);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("GENERAR ATS");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
  		
  		
  		tab_ats_informante.setId("tab_ats_informante");
  		tab_ats_informante.setSql(ser_sri.getAtsInformante("2016","04","1500","-1"));
  		tab_ats_informante.getColumna("Tipo Id Informante").setLongitud(30);
  		tab_ats_informante.getColumna("Identificacion Informante").setLongitud(35);
  		tab_ats_informante.getColumna("Año").setLongitud(10);
  		tab_ats_informante.getColumna("Mes").setLongitud(10);
  		tab_ats_informante.getColumna("Numero Establecimientos").setLongitud(30);
  		tab_ats_informante.getColumna("Total Ventas").setLongitud(10);
  		tab_ats_informante.getColumna("Código Operativo").setLongitud(10);
  		tab_ats_informante.setLectura(true);
  		tab_ats_informante.dibujar();
		tab_ats_informante.setRows(20);
  		
  		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_ats_informante);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);

	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		tab_ats_informante.setSql(ser_sri.getAtsInformante(com_anio.getValue().toString(),com_mes.getValue().toString(),"0","0"));
		tab_ats_informante.ejecutarSql();
		tab_ats_informante.dibujar();
	}


	public void exportarExcel(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_mes.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Mes", "");
			return;			
		}
		
		tab_ats_informante.setSql(ser_sri.getAtsInformante(com_anio.getValue().toString(),com_mes.getValue().toString(),"1500","0"));
		tab_ats_informante.ejecutarSql();
		leerPlantillaATS_XLS("atsDetalleCompras.xls",tab_ats_informante.getValor("Año"),com_mes.getValue().toString());
		tab_ats_informante.dibujar();
		utilitario.addUpdate("tab_ats_informante");
	}
	
	public void leerPlantillaATS_XLS(String nombre,String anio,String mes) { 

		try { 
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 
			//Workbook archivo_xls2 = Workbook.getWorkbook(new File(utilitario.getPropiedad("rutaUpload")+"\\" + nombre)); 
			
			URL url = new URL(utilitario.getPropiedad("rutaDownload")+"upload/"+nombre);
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			Workbook archivo_xls2 = Workbook.getWorkbook(in);
			
			result = new File(extContext.getRealPath("/" + nombre)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2); 
			
			total_ventas=0;
			total_nc=0;
			hojaInformante_xls = archivo_xls.getSheet("Informante");
			hojaCompraDetallada_xls = archivo_xls.getSheet("Compras Detalladas"); 
			hojaComprasFormasPago_xls = archivo_xls.getSheet("Compras Formas Pago"); 
			hojaComprasRetenciones_xls = archivo_xls.getSheet("Compras Retenciones"); 
			hojaVentasCliente_xls = archivo_xls.getSheet("Ventas Cliente"); 
			hojaVentasFormasCobro_xls = archivo_xls.getSheet("Formas de Cobro"); 
			hojaVentasEstablecimiento_xls = archivo_xls.getSheet("Ventas Establecimiento"); 
			hojaAnulados_xls = archivo_xls.getSheet("Anulados"); 

			fuente = new WritableFont(WritableFont.TAHOMA, 10);
			formato_celda = new WritableCellFormat(fuente); 
			formato_celda.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda.setOrientation(Orientation.HORIZONTAL); 
			formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

//			fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
//			formato_celda_suc = new WritableCellFormat(fuente_suc); 
//			formato_celda_suc.setAlignment(jxl.format.Alignment.LEFT); 
//			formato_celda_suc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
//			formato_celda_suc.setOrientation(Orientation.HORIZONTAL); 
//			formato_celda_suc.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
//			
//			fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
//			formato_celda_totales = new WritableCellFormat(fuente_suc); 
//			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT); 
//			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
//			formato_celda_totales.setOrientation(Orientation.HORIZONTAL); 
//			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.RED);
//
//			formato_celda_valor_rubro = new WritableCellFormat(fuente); 
//			formato_celda_valor_rubro.setAlignment(jxl.format.Alignment.RIGHT); 
//			formato_celda_valor_rubro.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
//			formato_celda_valor_rubro.setOrientation(Orientation.HORIZONTAL); 
//			formato_celda_valor_rubro.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			construyeComprasDetalladas(anio,mes);
			construyeComprasFormaPago(anio,mes);
			construyeComprasRetencion(anio,mes);
			construyeVentasCliente(anio,mes);
			construyeVentasEstablecimiento(anio,mes);
			construyeAnulados(anio,mes);
			construyeInformante();

			archivo_xls.write(); 
			archivo_xls.close(); 
			archivo_xls2.close();
			in.close();
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nombre);
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
	}
		
	private void construyeInformante() throws RowsExceededException, WriteException
	{
//		tab_ats_informante.setSql(ser_sri.getAtsInformante(com_anio.getValue().toString(),com_mes.getValue().toString(),utilitario.getFormatoNumero(total_ventas,2),"0"));
//		tab_ats_informante.ejecutarSql();
//		tab_ats_informante.imprimirSql();
		
		jxl.write.Label lab2;
		
		for (int i = 0; i < tab_ats_informante.getTotalFilas(); i++) 
		{
			tab_ats_informante.setValor("Total Ventas",utilitario.getFormatoNumero(total_ventas-total_nc,2));

			lab2 = new jxl.write.Label(0, i+1,tab_ats_informante.getValor(i, "Identificacion Informante"), formato_celda);
			hojaInformante_xls.addCell(lab2);

			lab2 = new jxl.write.Label(1, i+1,pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_ats_informante.getValor(i, "Razón Social")).replace(".", "")), formato_celda);
			hojaInformante_xls.addCell(lab2);

			lab2 = new jxl.write.Label(2, i+1,tab_ats_informante.getValor(i, "Año"), formato_celda);
			hojaInformante_xls.addCell(lab2);

			lab2 = new jxl.write.Label(3, i+1,tab_ats_informante.getValor(i, "Mes"), formato_celda);
			hojaInformante_xls.addCell(lab2);

			lab2 = new jxl.write.Label(4, i+1,tab_ats_informante.getValor(i, "Numero Establecimientos"), formato_celda);
			hojaInformante_xls.addCell(lab2);

			lab2 = new jxl.write.Label(5, i+1,tab_ats_informante.getValor(i, "Total Ventas"), formato_celda);
			hojaInformante_xls.addCell(lab2);

		}
		
//		tab_ats_informante.dibujar();
//		utilitario.addUpdate("tab_ats_informante");
		
	}
	
	private void construyeComprasDetalladas(String anio, String mes) throws RowsExceededException, WriteException
	{
		tab_detalle_compras_ats.setSql(ser_sri.getAtsDetalleCompras(anio, mes));
		tab_detalle_compras_ats.ejecutarSql();
		tab_detalle_compras_ats.imprimirSql();
		
		for (int i = 0; i < tab_detalle_compras_ats.getTotalFilas(); i++) 
		{
			
			//Cargamos RUC
			jxl.write.Label lab2 = new jxl.write.Label(0, i+2,tab_detalle_compras_ats.getValor(i, "codigo_compra"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			//Cargamos REFRECNIA
			lab2 = new jxl.write.Label(1, i+2,tab_detalle_compras_ats.getValor(i, "sustento"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			//Cargamos PROVEEDRO
			lab2 = new jxl.write.Label(2, i+2,tab_detalle_compras_ats.getValor(i, "identificacion_proveedor"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(3, i+2,tab_detalle_compras_ats.getValor(i, "ruc_tepro"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);

			
			lab2 = new jxl.write.Label(4, i+2,tab_detalle_compras_ats.getValor(i, "tipo_comprobante"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);

			
			lab2 = new jxl.write.Label(5, i+2,tab_detalle_compras_ats.getValor(i, "tipo_proveedor"), formato_celda); //Renovacion ATS ya no va 
			hojaCompraDetallada_xls.addCell(lab2);

			//Cargamos razon social
			lab2 = new jxl.write.Label(6, i+2,pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_detalle_compras_ats.getValor(i, "razon_social_prov")).replace(".", "")), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(7, i+2,tab_detalle_compras_ats.getValor(i, "parte_relacionada"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);

			
			lab2 = new jxl.write.Label(8, i+2,utilitario.DateStringAString(tab_detalle_compras_ats.getValor(i, "fecha_registro"), "MM/dd/yyyy"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(9, i+2,tab_detalle_compras_ats.getValor(i, "establecimiento"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(10, i+2,tab_detalle_compras_ats.getValor(i, "punto_emision"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(11, i+2,tab_detalle_compras_ats.getValor(i, "secuencial"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(12, i+2,utilitario.DateStringAString(tab_detalle_compras_ats.getValor(i, "fecha_emision"), "MM/dd/yyyy"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(13, i+2,tab_detalle_compras_ats.getValor(i, "nro_autorizacion_sri_adq"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(14, i+2,tab_detalle_compras_ats.getValor(i, "base_imp_no_objeto_iva"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
				
			
			lab2 = new jxl.write.Label(15, i+2,tab_detalle_compras_ats.getValor(i, "base_imponible_tarifa_cero_iva"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(16, i+2,tab_detalle_compras_ats.getValor(i, "base_imponible_gravada"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(17, i+2,tab_detalle_compras_ats.getValor(i, "base_excenta"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(18, i+2,tab_detalle_compras_ats.getValor(i, "monto_ice"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(19, i+2,tab_detalle_compras_ats.getValor(i, "monto_iva"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(20, i+2,tab_detalle_compras_ats.getValor(i, "retencion10"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(21, i+2,tab_detalle_compras_ats.getValor(i, "retencion20"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(22, i+2,tab_detalle_compras_ats.getValor(i, "retencion30"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			//Cargamos RETENCION IVA 50
			lab2 = new jxl.write.Label(23, i+2,tab_detalle_compras_ats.getValor(i, "retencion50"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(24, i+2,tab_detalle_compras_ats.getValor(i, "retencion70"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(25, i+2,tab_detalle_compras_ats.getValor(i, "retencion100"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			//lab2 = new jxl.write.Label(26, i+2,tab_detalle_compras_ats.getValor(i, "total_bases_imponibles"), formato_celda);
			lab2 = new jxl.write.Label(26, i+2,"0", formato_celda); //nuevo ATS renombrado a totbasesImpReemb solo para reembolsos
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(27, i+2,tab_detalle_compras_ats.getValor(i, "pago_local_exterior"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			/*
			
			lab2 = new jxl.write.Label(26, i+2,tab_detalle_compras_ats.getValor(i, "pais_paga"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(27, i+2,tab_detalle_compras_ats.getValor(i, "regimen_fiscal"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(28, i+2,tab_detalle_compras_ats.getValor(i, "aplica_convenio"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(29, i+2,tab_detalle_compras_ats.getValor(i, "pago_exterior_noramtiva"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			*/
			
			
			lab2 = new jxl.write.Label(35, i+2,tab_detalle_compras_ats.getValor(i, "establecimiento_rte"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(36, i+2,tab_detalle_compras_ats.getValor(i, "punto_emision_rte"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(37, i+2,tab_detalle_compras_ats.getValor(i, "secuencial_rte"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
				
				
			
			lab2 = new jxl.write.Label(38, i+2,tab_detalle_compras_ats.getValor(i, "nro_autorizacion_sri_ret"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			

			lab2 = new jxl.write.Label(39, i+2,utilitario.DateStringAString(tab_detalle_compras_ats.getValor(i, "fecha_teret"), "MM/dd/yyyy"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);

			//////////NOTAS DE CREDITO
			lab2 = new jxl.write.Label(40, i+2,tab_detalle_compras_ats.getValor(i, "codigo_doc_nc"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			lab2 = new jxl.write.Label(41, i+2,tab_detalle_compras_ats.getValor(i, "establecimiento_nc"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(42, i+2,tab_detalle_compras_ats.getValor(i, "punto_emision_nc"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
			
			
			
			lab2 = new jxl.write.Label(43, i+2,tab_detalle_compras_ats.getValor(i, "secuencial_nc"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
				
				
			
			lab2 = new jxl.write.Label(44, i+2,tab_detalle_compras_ats.getValor(i, "nro_autorizacion_sri_adncr"), formato_celda);
			hojaCompraDetallada_xls.addCell(lab2);
					
		}
		
	}
	
	private void construyeComprasFormaPago(String anio, String mes) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		tab_compras_forma_pago_ats.setSql(ser_sri.getAtsComprasFormaPago(anio, mes));
		tab_compras_forma_pago_ats.ejecutarSql();
		tab_compras_forma_pago_ats.imprimirSql();
		for (int i = 0; i < tab_compras_forma_pago_ats.getTotalFilas(); i++) 
		{
			lab2 = new jxl.write.Label(0, i+2,tab_compras_forma_pago_ats.getValor(i, "codigo_compra"), formato_celda);
			hojaComprasFormasPago_xls.addCell(lab2);

			lab2 = new jxl.write.Label(1, i+2,tab_compras_forma_pago_ats.getValor(i, "forma_pago"), formato_celda);
			hojaComprasFormasPago_xls.addCell(lab2);

		}
		
	}
	
	private void construyeComprasRetencion(String anio, String mes) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		tab_compras_retencion_ats.setSql(ser_sri.getAtsComprasRetencion(anio, mes));
		tab_compras_retencion_ats.ejecutarSql();
		tab_compras_retencion_ats.imprimirSql();
		for (int i = 0; i < tab_compras_retencion_ats.getTotalFilas(); i++) 
		{
			lab2 = new jxl.write.Label(0, i+1,tab_compras_retencion_ats.getValor(i, "codigo_compra"), formato_celda);
			hojaComprasRetenciones_xls.addCell(lab2);

			lab2 = new jxl.write.Label(1, i+1,tab_compras_retencion_ats.getValor(i, "codigo_retencion"), formato_celda);
			hojaComprasRetenciones_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(2, i+1,tab_compras_retencion_ats.getValor(i, "base_imponible"), formato_celda);
			hojaComprasRetenciones_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(3, i+1,tab_compras_retencion_ats.getValor(i, "porcentaje_retencion"), formato_celda);
			hojaComprasRetenciones_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(4, i+1,tab_compras_retencion_ats.getValor(i, "valor_retenido"), formato_celda);
			hojaComprasRetenciones_xls.addCell(lab2);

		}
		
	}
	
	private void construyeVentasCliente(String anio, String mes) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		int formasPago=0;
		tab_detalle_ventas_ast.setSql(ser_sri.getAtsVentasCliente(anio, mes));
		tab_detalle_ventas_ast.ejecutarSql();
		tab_detalle_ventas_ast.imprimirSql();
		for (int i = 0; i < tab_detalle_ventas_ast.getTotalFilas(); i++) 
		{
			lab2 = new jxl.write.Label(0, i+1,tab_detalle_ventas_ast.getValor(i, "codigo"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(1, i+1,tab_detalle_ventas_ast.getValor(i, "tipo_identificacion"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);

			lab2 = new jxl.write.Label(2, i+1,tab_detalle_ventas_ast.getValor(i, "identificacion_cliente"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(3, i+1,tab_detalle_ventas_ast.getValor(i, "es_parte_relacionada"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(4, i+1,tab_detalle_ventas_ast.getValor(i, "tipo_cli"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(5, i+1,pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_detalle_ventas_ast.getValor(i, "razon_social_recli")).replace(".", "")), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(6, i+1,tab_detalle_ventas_ast.getValor(i, "codigo_tipo_comprobate"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(7, i+1,tab_detalle_ventas_ast.getValor(i, "tipo_emi"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(8, i+1,tab_detalle_ventas_ast.getValor(i, "total_comp_emitidos"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(9, i+1,tab_detalle_ventas_ast.getValor(i, "base_imp_nobjeto"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(10, i+1,tab_detalle_ventas_ast.getValor(i, "base_imp_tarf0"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(11, i+1,tab_detalle_ventas_ast.getValor(i, "base_imp_tarfiva"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(12, i+1,tab_detalle_ventas_ast.getValor(i, "valor_iva"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(13, i+1,"0", formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(14, i+1,"0", formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(15, i+1,"0", formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(16, i+1,tab_detalle_ventas_ast.getValor(i, "doc"), formato_celda);
			hojaVentasCliente_xls.addCell(lab2);
			
			if(tab_detalle_ventas_ast.getValor(i, "doc").toString().equals("FACTURA") || tab_detalle_ventas_ast.getValor(i, "doc").toString().equals("NOTA DEBITO"))
			{
				lab2 = new jxl.write.Label(0, formasPago+1,tab_detalle_ventas_ast.getValor(i, "codigo"), formato_celda);
				hojaVentasFormasCobro_xls.addCell(lab2);

				lab2 = new jxl.write.Label(1, formasPago+1,tab_detalle_ventas_ast.getValor(i, "forma_pago"), formato_celda);
				hojaVentasFormasCobro_xls.addCell(lab2);
				formasPago++;
			}

		}
		
	}
	
	private void construyeVentasEstablecimiento(String anio, String mes) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		tab_ventas_establecimiento_ast.setSql(ser_sri.getAtsVentasEstablecimiento(anio, mes));
		tab_ventas_establecimiento_ast.ejecutarSql();
		tab_ventas_establecimiento_ast.imprimirSql();
		for (int i = 0; i < tab_ventas_establecimiento_ast.getTotalFilas(); i++) 
		{
			lab2 = new jxl.write.Label(0, i+1,tab_ventas_establecimiento_ast.getValor(i, "codigo_establecimiento"), formato_celda);
			hojaVentasEstablecimiento_xls.addCell(lab2);

			lab2 = new jxl.write.Label(1, i+1,tab_ventas_establecimiento_ast.getValor(i, "ventas_establecimiento"), formato_celda);
			hojaVentasEstablecimiento_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(2, i+1,"0", formato_celda);
			hojaVentasEstablecimiento_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(3, i+1,tab_ventas_establecimiento_ast.getValor(i, "doc"), formato_celda);
			hojaVentasEstablecimiento_xls.addCell(lab2);
			
			if(tab_ventas_establecimiento_ast.getValor(i, "doc").equals("FACTURA"))
				total_ventas+=pckUtilidades.CConversion.CDbl_2(tab_ventas_establecimiento_ast.getValor(i, "ventas_establecimiento"));
			
			if(tab_ventas_establecimiento_ast.getValor(i, "doc").equals("NOTA DEBITO"))
				total_ventas+=pckUtilidades.CConversion.CDbl_2(tab_ventas_establecimiento_ast.getValor(i, "ventas_establecimiento"));
			
			if(tab_ventas_establecimiento_ast.getValor(i, "doc").equals("NOTA CREDITO"))
				total_nc+=pckUtilidades.CConversion.CDbl_2(tab_ventas_establecimiento_ast.getValor(i, "ventas_establecimiento"));
		}
		
	}
	
	private void construyeAnulados(String anio, String mes) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		tab_anulados_ast.setSql(ser_sri.getAtsAnulados(anio, mes));
		tab_anulados_ast.ejecutarSql();
		tab_anulados_ast.imprimirSql();
		for (int i = 0; i < tab_anulados_ast.getTotalFilas(); i++) 
		{
			lab2 = new jxl.write.Label(0, i+1,tab_anulados_ast.getValor(i, "codigo_tipo_comprobate"), formato_celda);
			hojaAnulados_xls.addCell(lab2);

			lab2 = new jxl.write.Label(1, i+1,tab_anulados_ast.getValor(i, "establecimiento"), formato_celda);
			hojaAnulados_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(2, i+1,tab_anulados_ast.getValor(i, "punto_emision"), formato_celda);
			hojaAnulados_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(3, i+1,tab_anulados_ast.getValor(i, "seciencial_inicio"), formato_celda);
			hojaAnulados_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(4, i+1,tab_anulados_ast.getValor(i, "seciencial_fin"), formato_celda);
			hojaAnulados_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(5, i+1,tab_anulados_ast.getValor(i, "autorizacion"), formato_celda);
			hojaAnulados_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(6, i+1,tab_anulados_ast.getValor(i, "doc"), formato_celda);
			hojaAnulados_xls.addCell(lab2);

		}
		
	}
	
	
	
	
	/////botones fin,siguiente,atras,ultimo.inicio
    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
    }
    
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
    }
    
    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
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
	
	public Tabla getTab_ats_informante() {
		return tab_ats_informante;
	}

	public void setTab_ats_informante(Tabla tab_ats_informante) {
		this.tab_ats_informante = tab_ats_informante;
	}
	
	public Tabla getTab_detalle_compras_ats() {
		return tab_detalle_compras_ats;
	}

	public void setTab_detalle_compras_ats(Tabla tab_detalle_compras_ats) {
		this.tab_detalle_compras_ats = tab_detalle_compras_ats;
	}

	public Tabla getTab_compras_forma_pago_ats() {
		return tab_compras_forma_pago_ats;
	}

	public void setTab_compras_forma_pago_ats(Tabla tab_compras_forma_pago_ats) {
		this.tab_compras_forma_pago_ats = tab_compras_forma_pago_ats;
	}
	
	public Tabla getTab_compras_retencion_ats() {
		return tab_compras_retencion_ats;
	}

	public void setTab_compras_retencion_ats(Tabla tab_compras_retencion_ats) {
		this.tab_compras_retencion_ats = tab_compras_retencion_ats;
	}

	public Tabla getTab_detalle_ventas_ast() {
		return tab_detalle_ventas_ast;
	}

	public void setTab_detalle_ventas_ast(Tabla tab_detalle_ventas_ast) {
		this.tab_detalle_ventas_ast = tab_detalle_ventas_ast;
	}

	public Tabla getTab_ventas_establecimiento_ast() {
		return tab_ventas_establecimiento_ast;
	}

	public void setTab_ventas_establecimiento_ast(Tabla tab_ventas_establecimiento_ast) {
		this.tab_ventas_establecimiento_ast = tab_ventas_establecimiento_ast;
	}
	
	public Tabla getTab_anulados_ast() {
		return tab_anulados_ast;
	}

	public void setTab_anulados_ast(Tabla tab_anulados_ast) {
		this.tab_anulados_ast = tab_anulados_ast;
	}

}
