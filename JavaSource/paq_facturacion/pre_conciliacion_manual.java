package paq_facturacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.primefaces.event.FileUploadEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;

import java.util.List;
/**
 *
 * @author AWBECERRA
 */
public class pre_conciliacion_manual extends Pantalla {
	
    private Tabla tab_tabla = new Tabla();
	private Combo com_anio=new Combo();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Radio rad_imprimir= new Radio();
	private Upload upl_archivo=new Upload();
	private Tabla tab_cliente = new Tabla();
	private Texto txt_documento_banco = new Texto();
	private Dialogo dia_detalle = new Dialogo();
	private Tabla tab_resultado = new Tabla();
	private AutoCompletar aut_factura=new AutoCompletar(); 

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

    public pre_conciliacion_manual() { 
    	
    	//bar_botones.limpiar();
    	Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiarE");
		aut_factura.setId("aut_factura");     
		aut_factura.setAutoCompletar(ser_facturacion.getDatosSucursales()); 
		//aut_factura.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
    	Etiqueta eti_colaborador=new Etiqueta("ESTABLECIMIENTO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);
    	
    	Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
    	
    	gri_formulario.getChildren().add(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
    	gri_formulario.getChildren().add(cal_fecha_inicial);

    	gri_formulario.getChildren().add(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
    	gri_formulario.getChildren().add(cal_fecha_final);
    	
    	gri_formulario.getChildren().add(new Etiqueta("Documento Bancario :"));
    	txt_documento_banco.setId("txt_documento_banco");
		txt_documento_banco.setSize(50);
		txt_documento_banco.setMetodoChange("cambiatexto");
    	gri_formulario.getChildren().add(txt_documento_banco);		
		List lista = new ArrayList();
	       Object fila1[] = {
	           "0", "NO CONCILIADO"
	       };
	       Object fila2[] = {
	           "1", "CONCILIADO"
	       };
	       
       lista.add(fila1);
       lista.add(fila2);
       rad_imprimir.setId("rad_imprimir");
       rad_imprimir.setRadio(lista);
       rad_imprimir.setValue(fila2);
//	       bar_botones.agregarComponente(rad_imprimir);
    	gri_formulario.getChildren().add(rad_imprimir);
		
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("actualizarLista");
		bot_filtrar.setIcon("ui-icon-refresh");
//		bar_botones.agregarBoton(bot_filtrar);
    	gri_formulario.getChildren().add(bot_filtrar);
	
		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");

	//	upl_archivo.setUpdate("gri_valida");		
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Validar");
		upl_archivo.setCancelLabel("Cancelar Seleccion");
//		bar_botones.agregarComponente(upl_archivo);
    	gri_formulario.setFooter(upl_archivo);
		
    	
    	
    	
    	tab_tabla.setId("tab_tabla");
    	tab_tabla.setTabla("fac_factura", "ide_fafac", 1);
//    	tab_tabla.setCampoOrden("ide_fafac limit 2000");
    	tab_tabla.setCondicion("ide_fafac=-1");
//    	tab_tabla.setCondicion("not conciliado_fafac is true");
    	tab_tabla.getColumna("ide_comov").setVisible(false);
    	tab_tabla.getColumna("ide_gtemp").setVisible(false);
    	tab_tabla.getColumna("ide_tedar").setVisible(false);
    	tab_tabla.getColumna("ide_retip").setVisible(false);
    	tab_tabla.getColumna("ide_tetid").setVisible(false);
    	tab_tabla.getColumna("ide_geins").setVisible(false);
    	tab_tabla.getColumna("fecha_vencimiento_fafac").setVisible(false);
    	tab_tabla.getColumna("fecha_emision_fafac").setVisible(false);
    	tab_tabla.getColumna("direccion_fafac").setVisible(false);
    	tab_tabla.getColumna("observacion_fafac").setVisible(false);
    	tab_tabla.getColumna("base_no_iva_fafac").setVisible(false);
    	tab_tabla.getColumna("base_cero_fafac").setVisible(false);
    	tab_tabla.getColumna("razon_anulado_fafac").setVisible(false);
    	tab_tabla.getColumna("fecha_anulado_fafac").setVisible(false);
    	tab_tabla.getColumna("ide_falug").setVisible(false);
    	tab_tabla.getColumna("codigo_faclinea_fafac").setVisible(false);
    	tab_tabla.getColumna("responsable_faclinea_fafac").setVisible(false);
    	tab_tabla.getColumna("num_comprobante_fafac").setVisible(false);
    	tab_tabla.getColumna("ide_fadaf").setVisible(false);
    	//tab_tabla.getColumna("ide_coest").setVisible(false);
    	tab_tabla.getColumna("secuencial_fafac").setLectura(true);
    	tab_tabla.getColumna("base_aprobada_fafac").setLectura(true);
    	tab_tabla.getColumna("base_aprobada_fafac").setFormatoNumero(2);
    	tab_tabla.getColumna("valor_iva_fafac").setLectura(true);
    	tab_tabla.getColumna("valor_iva_fafac").setFormatoNumero(2);
    	tab_tabla.getColumna("total_fafac").setLectura(true);
    	tab_tabla.getColumna("total_fafac").setFormatoNumero(2);
    	//tab_tabla.getColumna("conciliado_fafac").setLectura(true);
    	tab_tabla.getColumna("activo_fafac").setLectura(true);
    	tab_tabla.getColumna("factura_fisica_fafac").setLectura(true);
    	tab_tabla.getColumna("fecha_transaccion_fafac").setLectura(true);

    	tab_tabla.getColumna("ide_recli").setCombo(ser_facturacion.getClientesConciliacion("0,1"));
    	tab_tabla.getColumna("ide_recli").setAutoCompletar();
    	tab_tabla.getColumna("ide_recli").setLectura(true);
    	tab_tabla.getColumna("ide_recli").setNombreVisual("CLIENTE");
    	tab_tabla.getColumna("ide_recli").setFiltro(true);
    	tab_tabla.getColumna("secuencial_fafac").setFiltro(true);
    	tab_tabla.getColumna("documento_conciliado_fafac").setFiltro(true);
    	tab_tabla.getColumna("con_ide_coest").setCombo("select ide_coest,detalle_coest from cont_estado");
    	tab_tabla.getColumna("con_ide_coest").setAutoCompletar();
    	tab_tabla.getColumna("con_ide_coest").setLectura(true);
    	tab_tabla.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", utilitario.getVariable("p_modulo_facturacion")));
    	tab_tabla.getColumna("ide_coest").setNombreVisual("ESTADO");
    	tab_tabla.getColumna("ide_recli").setLongitud(200);
    	//tab_tabla.getColumna("ide_fadaf").setCombo(ser_facturacion.getDatosFactura("1",""));
    	//tab_tabla.getColumna("ide_fadaf").setAutoCompletar();
    	//tab_tabla.getColumna("ide_fadaf").setLectura(true);
    	//tab_tabla.setRows(200);
    	//tab_tabla.setLectura(true);
		    
	    tab_tabla.getColumna("documento_conciliado_fafac").setCombo("select documento_conciliado_fafac,documento_conciliado_fafac from fac_factura group by documento_conciliado_fafac");
	    //tab_tabla.getColumna("documento_conciliado_fafac").setValorDefecto("CAJA MATRIZ");
	    tab_tabla.getColumna("documento_conciliado_fafac").setAutoCompletar();
    	tab_tabla.dibujar();
    	
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setHeader(gri_formulario);
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
        tab_resultado.setId("tab_resultado");
        tab_resultado.setIdCompleto("dia_detalle:tab_resultado");
        tab_resultado.setTabla("fac_auditoria_conciliacion", "ide_faauc", 3);
        tab_resultado.setCondicion("ide_faauc=-1");
        tab_resultado.setLectura(true);
        tab_resultado.dibujar();
        
        
		dia_detalle.setId("dia_detalle");
		dia_detalle.setTitle("RESULTADO DE CONCILIACION");
		dia_detalle.setWidth("80%");
		dia_detalle.setDynamic(false);
		dia_detalle.setDialogo(tab_resultado);
		
		agregarComponente(dia_detalle);
    }
    
    
	public void actualizarLista(){
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null || aut_factura.getValor()!= null) 
		{
			if (rad_imprimir.getValue().equals("0")){
				tab_tabla.setCondicion(" not conciliado_fafac is true  and fecha_transaccion_fafac between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+
						"' and ide_fadaf in (select ide_fadaf from bodt_grupo_material gm, fac_datos_factura facd  where gm.ide_bogrm=facd.ide_bogrm and gm.ide_bogrm ="+aut_factura.getValor()+") ");	
			}
			else if (rad_imprimir.getValue().equals("1")){
				tab_tabla.setCondicion(" conciliado_fafac is true and fecha_transaccion_fafac between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+
						"' and ide_fadaf in (select ide_fadaf from bodt_grupo_material gm, fac_datos_factura facd  where gm.ide_bogrm=facd.ide_bogrm and gm.ide_bogrm ="+aut_factura.getValor()+") ");	
			}
			tab_tabla.ejecutarSql();
		}
		else {
			utilitario.agregarMensajeInfo("Filtros no válidos","Debe ingresar los fitros Rangos de fechas");
		}		
	}
    
    public void seleccionaOpcion (){
		
		String sql ="select orden,secuencial,fecha_transaccion_fafac, ruc_comercial_recli,rpad,base_aprobada_fafac, valor_iva_fafac, total_fafac,detalle_bogrm,doc_identidad,"
				+" repeat ('0',(15 - length (tot_sin_punto)))||tot_sin_punto as valor_sin_punto,repeat ('0',(15 - length (secuencial)))||secuencial as factura_sin_punto,repeat ('0',(13 - length (ruc_comercial_recli)))||ruc_comercial_recli as ruc_sin_punto,repeat ('0',(7 - length (nuevo_iva)))||nuevo_iva as iva_sin_punto"
				+" from ("
				+" SELECT row_number() over( order by secuencial_fafac) as orden,replace (secuencial_fafac,'-','') as secuencial,fecha_transaccion_fafac, ruc_comercial_recli,rpad(razon_social_recli,30,' ') as rpad,base_aprobada_fafac, valor_iva_fafac, total_fafac,"
				+" detalle_bogrm,replace(round(total_fafac,2)||'','.','') as tot_sin_punto, ( case when ide_gttdi = 1  then 'P' when ide_gttdi= 2 then 'R' when ide_gttdi= 3 then 'C' end) as doc_identidad,replace((round(valor_iva_fafac,2))||'','.','') as nuevo_iva "
				+" FROM fac_factura fac"
				+" join rec_clientes cli on cli.ide_recli=fac.ide_recli"
				+" join (select ide_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm) b on fac.ide_fadaf = b.ide_fadaf"
				+" where ide_coest=2 "
				+" and activo_fafac=true "
				+" and ide_tetid=4 ";
				if (rad_imprimir.getValue().equals("1")){
				sql+=" and fac.ide_fadaf ="+com_anio.getValue();	
				}
				sql+=" and fecha_transaccion_fafac between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'";
				sql+=" order by secuencial_fafac"
				+" ) a"
				+" order by secuencial";
		System.out.println("actualiza archvo "+sql);
		tab_tabla.setSql(sql);
		tab_tabla.ejecutarSql();
				
		utilitario.addUpdate("tab_tabla");
	}
    /**
	 * Genera el archivo zip que contiene dos archivos el .txt y el .md5
	 */
    
	public void generarArchivo(){		
		if(tab_tabla.getValor("orden")==null){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}

		//Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
		try {
			//StringBuilder str_spi=new StringBuilder();
			String str_nombre_archivo=utilitario.getVariable("p_fac_nombre_archivo_banco");
			String str_localidad=utilitario.getVariable("p_fac_localidad_archivo_banco");
			String str_transaccion =utilitario.getVariable("p_fac_transaccion_archivo_banco");
			String str_codservicio=utilitario.getVariable("p_fac_codservicio_archivo_banco");
			String str_referencia=utilitario.getVariable("p_fac_referencia_archivo_banco");
			String str_formapago=utilitario.getVariable("p_fac_formapago_archivo_banco");
			String str_moneda=utilitario.getVariable("p_fac_moneda_archivo_banco");
			String str_tipo_cueta="  ";
			String str_numero_cuenta="        ";
			String str_referencia_blanco="   ";
			String str_localidad_cheque="  ";
			String str_agencia_cheque="  ";
			String str_telefono="";
					
			
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= str_nombre_archivo+utilitario.getFechaActual(); //utilitario.getNombreMes(utilitario.getMes(str_fecha_hora))+utilitario.getAnio(str_fecha_hora);
			String path= extContext.getRealPath("/");			

			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
	
			tab_tabla.ejecutarSql();			
			for(int i=0;i<tab_tabla.getTotalFilas();i++){
		//		str_spi.append("\r\n");
				StringBuilder str_detalle=new StringBuilder();
				str_detalle.append(str_localidad);			
				str_detalle.append(str_transaccion);
				str_detalle.append(str_codservicio);
				str_detalle.append(str_tipo_cueta);
				str_detalle.append(str_numero_cuenta);
				str_detalle.append(tab_tabla.getValor(i, "valor_sin_punto"));
				str_detalle.append(tab_tabla.getValor(i, "factura_sin_punto"));

				str_detalle.append(str_referencia).append(str_referencia_blanco);
				str_detalle.append(tab_tabla.getValor(i, "iva_sin_punto"));
				str_detalle.append(str_formapago);
				str_detalle.append(str_moneda);
				str_detalle.append(tab_tabla.getValor(i, "rpad"));
				str_detalle.append(str_localidad_cheque);
				str_detalle.append(str_agencia_cheque);
				str_detalle.append(tab_tabla.getValor(i, "doc_identidad"));
				str_detalle.append(tab_tabla.getValor(i, "ruc_sin_punto"));
				str_detalle.append(str_telefono);


				String str_spi_encr="";
				try {
					 str_spi_encr=str_detalle.toString();
					str_spi_encr=str_spi_encr.replaceAll("Á", "A");
					str_spi_encr=str_spi_encr.replaceAll("É", "E");
					str_spi_encr=str_spi_encr.replaceAll("Í", "I");
					str_spi_encr=str_spi_encr.replaceAll("Ó", "O");
					str_spi_encr=str_spi_encr.replaceAll("Ú", "U");
					str_spi_encr=str_spi_encr.replaceAll("á", "a");
					str_spi_encr=str_spi_encr.replaceAll("é", "e");
					str_spi_encr=str_spi_encr.replaceAll("í", "i");
					str_spi_encr=str_spi_encr.replaceAll("ó", "o");
					str_spi_encr=str_spi_encr.replaceAll("ú", "u");
					str_spi_encr=str_spi_encr.replaceAll("Ñ", "N");
					str_spi_encr=str_spi_encr.replaceAll("ñ", "n");
				} catch (Exception e) {
					// TODO: handle exception
				}				
				//str_spi.append(str_spi_encr);
				escribir.write(str_spi_encr.toString());
				escribir.newLine();

			}
			escribir.close();
			String str_spi_encr="";
			BufferedReader entrada;
			try {
			entrada = new BufferedReader( new FileReader( archivotxt ) );
			String linea;
			while(entrada.ready()){
			linea = entrada.readLine();
			if(str_spi_encr.isEmpty()==false){
				str_spi_encr+="\r\n";
			}
			str_spi_encr+=linea;
			}
			}catch (IOException e) {
			e.printStackTrace();
			}
			
			
			System.out.println("str spi "+str_spi_encr.toString());
			utilitario.crearArchivo(new File[]{archivotxt}, fileName.concat(".txt"));						
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.crearError("xxx", "xxss", e);
		}
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_tabla.limpiar();	
		utilitario.addUpdate("tab_tabla");// limpia y refresca el autocompletar
	}
	public void cambiatexto() {
	utilitario.agregarMensajeInfo("Documento Bancario", txt_documento_banco.getValue()+"");	
	}
	/**
	 * Valida el archivo para que pueda importar un rubro a la nomina
	 * @param evt
	 */
	
	public void validarArchivo(FileUploadEvent evt){	
			//Leer el archivo
//			String str_msg_info="";
//			String str_msg_adve="";
//			String str_msg_erro="";
//			double dou_tot_valor_imp=0;
			String sql_inserta_auditoria=""; 
			
			int conciliados=0;
			if(txt_documento_banco.getValue().equals("")){
				utilitario.agregarMensajeInfo("No se puede conciliar el Archivo", "Favor Ingrese el numero del documento de conciliacion");
				return;
			}
			
			try {
				//Válido que el rubro seleccionado este configurado en los tipo de nomina

				Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
				Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
				if (hoja == null) {
					utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
					return;
				}
				int int_fin = hoja.getRows();
				upl_archivo.setNombreReal(evt.getFile().getFileName());

				//insertaAuditoria(evt);

//				str_msg_info+=getFormatoInformacion("El archivo "+upl_archivo.getNombreReal()+" contiene "+int_fin+" filas");
				
				for(int k=0; k< tab_tabla.getTotalFilas();k ++){
					//System.out.println("factura "+tab_tabla.getValor(k, "ide_fafac"));
					// extraigo los datos de la factura
					TablaGenerica tab_factura=ser_facturacion.getDatosClienteFactura(tab_tabla.getValor(k, "ide_fafac"));
					//INICIO, Modificado por: Alex Becerra, fecha: 23/11/2015, motivo: Manejo de excepciones al momento de conciliar facturas.
					String str_codigo_tercero=null;
					try
					{
					//FIN, Modificado por: Alex Becerra, fecha: 23/11/2015, motivo: Manejo de excepciones al momento de conciliar facturas.
						for (int i = 0; i < int_fin; i++) {
							//codigo tercero remplaza a str_cedula permite leer el codigo de la factutra
							str_codigo_tercero = hoja.getCell(4, i).getContents();	
							str_codigo_tercero=str_codigo_tercero.trim(); 
							String str_cedula_cliente =hoja.getCell(32, i).getContents();
							str_cedula_cliente = str_cedula_cliente.trim();
							String str_valor_conciliar = hoja.getCell(3, i).getContents();
							String str_valor = hoja.getCell(19, i).getContents();
							double double_valor_conciliar= pckUtilidades.CConversion.CDbl_2(str_valor_conciliar.replace(",", "."));
							String str_fecha_pago= hoja.getCell(6, i).getContents();
							
							if(tab_factura.getValor("factura_sin_punto").equals(str_codigo_tercero) && tab_factura.getValor("ruc_sin_punto").equals(str_cedula_cliente)){
								String update="update fac_factura set conciliado_fafac=true,"
										+"fecha_pago_fafac='"+str_fecha_pago+"',"
										+"valor_conciliado_fafac="+double_valor_conciliar+","
										+"fecha_conciliado_fafac='"+utilitario.getFechaActual()+"',"
										+"documento_conciliado_fafac='"+str_valor+"',"
										+"documento_bancario_fafac='"+txt_documento_banco.getValue()+"',"
										+"ide_coest="+utilitario.getVariable("p_factura_pagado")+","
										+"con_ide_coest="+utilitario.getVariable("p_estado_conciliacion_bancaria")
										+" where ide_fafac="+tab_tabla.getValor(k,"ide_fafac");
								utilitario.getConexion().ejecutarSql(update);
								conciliados++;
							}
						}
					//INICIO, Modificado por: Alex Becerra, fecha: 23/11/2015, motivo: Manejo de excepciones al momento de conciliar facturas.
					}catch (Exception e) {
						 System.out.println("Error al conciliar la factura ide_fafac: "+tab_tabla.getValor(k,"ide_fafac")+" Secuencial: " +str_codigo_tercero+" Msj: "+e);
					 }
					//FIN, Modificado por: Alex Becerra, fecha: 23/11/2015, motivo: Manejo de excepciones al momento de conciliar facturas.
				}
				utilitario.addUpdate("tab_tabla");
				//tab_tabla.guardar();
				//guardarPantalla();
				tab_tabla.ejecutarSql();
				utilitario.agregarMensaje("Conciliado", "Actualice los registros conciliados");
				
				tab_resultado.setCondicion("validado_faauc=false");
				tab_resultado.ejecutarSql();
				tab_resultado.exportarXLS();
				//dia_detalle.dibujar();
	
				} catch (Exception e) {
					// TODO: handle exception
					utilitario.agregarMensajeError("No se pudo conciliar el archivo", "Debido a una inconsistencia no se pudo culminar exitosamente con la conciliacion del archivo");
					System.out.println("Error conciliacion Bancaria: "+e);
				}	
			System.out.println("No CONCILIADOS: "+conciliados);
	}
	
	public String insertaAuditoria(FileUploadEvent evt){
		String sql_inserta_auditoria=""; 

		String resultado="";
		try {
		Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
		Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
		if (hoja == null) {
			utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
			return resultado;
		}
		String sql_updatevalida="update fac_auditoria_conciliacion set validado_faauc=true where validado_faauc=false";
		utilitario.getConexion().ejecutarSql(sql_updatevalida);
		int int_fin = hoja.getRows();
		for (int i = 0; i < int_fin; i++) {
			//codigo tercero remplaza a str_cedula permite leer el codigo de la factutra
			String str_codigo_tercero = hoja.getCell(4, i).getContents();	
			str_codigo_tercero=str_codigo_tercero.trim(); 
			String str_cedula_cliente =hoja.getCell(32, i).getContents();
			str_cedula_cliente = str_cedula_cliente.trim();
			String str_valor_conciliar = hoja.getCell(3, i).getContents();
			String str_valor = hoja.getCell(19, i).getContents();
			double double_valor_conciliar= pckUtilidades.CConversion.CDbl_2(str_valor_conciliar.replace(",", "."));
			String str_fecha_pago= hoja.getCell(6, i).getContents();
			// Consulto codigo maximo de la cabecera de la tabla auditoria conciliacion
			TablaGenerica tab_maximo_factura =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("fac_auditoria_conciliacion", "ide_faauc"));
			String maximo_factura_auditoria=tab_maximo_factura.getValor("codigo");
			// sql inserta en la tabla de auditorias
			
			sql_inserta_auditoria="insert into fac_auditoria_conciliacion (ide_faauc,codigo_factura_faauc,cedula_archivo_faauc,valor_archivo_faauc,fecha_pago_archivo_faauc,ide_usua,"
					+" fecha_hora_concilia_faauc,documento_conciliado_faauc,nro_fila_excel_faauc,fecha_inicial_faauc,fecha_final_faauc,validado_faauc)"
					+" values ("+maximo_factura_auditoria+",'"+str_codigo_tercero+"','"+str_cedula_cliente+"',"+double_valor_conciliar+",'"+str_fecha_pago
					+"',"+utilitario.getVariable("IDE_USUA")+",(select now()),'"+str_valor+"',"+int_fin+",'"+cal_fecha_inicial.getFecha()+"','"+cal_fecha_final.getFecha()+"',false); ";
			utilitario.getConexion().ejecutarSql(sql_inserta_auditoria);

		}
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error conciliacion Bancaria inserta: "+e);
		}	
		return resultado;		
	}
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }
    /**
	 * Genera un mensaje de información color azul
	 * @param mensaje
	 * @return
	 */
	private String getFormatoInformacion(String mensaje){
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}
	/**
	 * Genera un mensaje de Error color rojo
	 * @param mensaje
	 * @return
	 */
	private String getFormatoError(String mensaje){
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}
	
	public void limpiarE(){
		aut_factura.limpiar();
		//tab_factura_consulta.limpiar();
		utilitario.addUpdate("aut_factura");
	}
	
	
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
	public Combo getCom_anio() {
		return com_anio;
	}
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	public Tabla getTab_cliente() {
		return tab_cliente;
	}
	public void setTab_cliente(Tabla tab_cliente) {
		this.tab_cliente = tab_cliente;
	}


	public Dialogo getDia_detalle() {
		return dia_detalle;
	}


	public void setDia_detalle(Dialogo dia_detalle) {
		this.dia_detalle = dia_detalle;
	}


	public Tabla getTab_resultado() {
		return tab_resultado;
	}


	public void setTab_resultado(Tabla tab_resultado) {
		this.tab_resultado = tab_resultado;
	}
	
	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}

	
}
