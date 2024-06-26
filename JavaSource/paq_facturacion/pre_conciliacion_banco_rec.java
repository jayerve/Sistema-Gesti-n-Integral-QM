/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_facturacion;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.event.FileUploadEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;

/**
 *
 * @author AWBECERRA
 * @Descripcion Conciliacion con el archivo enviado del banco del pacifico
 */
public class pre_conciliacion_banco_rec extends Pantalla {
	
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla_diferido = new Tabla();
    private Tabla tab_tabla_diferido_conciliado = new Tabla();
    private Tabla tab_tabla_rec = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Upload upl_archivo=new Upload();
	private Texto txt_documento_banco = new Texto();
	private Confirmar con_guardar=new Confirmar();
	private String transacciones;
	private List<String[]> lineas;

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

    public pre_conciliacion_banco_rec() { 
    	
    	bar_botones.limpiar();
    	
    	Boton bot_pagar=new Boton();
		bot_pagar.setIcon("ui-icon-person");
		bot_pagar.setValue("CONCILIAR");
		bot_pagar.setMetodo("conciliar");
		bar_botones.agregarBoton(bot_pagar);
    	
    	Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setValue("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarComponente(bot_limpiar);

    	Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(5);
    	
    	gri_formulario.getChildren().add(new Etiqueta("Fecha Archivo :"));
		//cal_fecha_inicial.setFechaActual();
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
    	gri_formulario.getChildren().add(cal_fecha_inicial);

    	Boton bot_fecha=new Boton();
    	bot_fecha.setIcon("ui-icon-person");
    	bot_fecha.setValue("Establecer Fecha");
    	bot_fecha.setMetodo("ingreso");
    	gri_formulario.getChildren().add(bot_fecha);
    	
    	gri_formulario.getChildren().add(new Etiqueta("Documento Bancario :"));
    	txt_documento_banco.setId("txt_documento_banco");
		txt_documento_banco.setSize(50);
		txt_documento_banco.setValue(0);
		txt_documento_banco.setMetodoChange("cambiatexto");
    	gri_formulario.getChildren().add(txt_documento_banco);	

		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");

	//	upl_archivo.setUpdate("gri_valida");		
		upl_archivo.setAuto(false);
		//upl_archivo.setAllowTypes("/(\\.|\\/)(*)$/");
		upl_archivo.setUploadLabel("Cargar archivo");
		upl_archivo.setCancelLabel("Cancelar Seleccion");
		//bar_botones.agregarComponente(upl_archivo);
    	gri_formulario.setFooter(upl_archivo);

    	ItemMenu itm_eliminarTodos = new ItemMenu();
    	itm_eliminarTodos.setValue("Eliminar Todos");
    	itm_eliminarTodos.setMetodo("eliminarTodos");
    	itm_eliminarTodos.setIcon("ui-icon-alert");
    	
    	tab_tabla.setId("tab_tabla");
    	tab_tabla.setHeader("CONCILIACIÓN BANCO PACIFICO");
    	tab_tabla.setTabla("fac_auditoria_pagos", "ide_faaup", 1);
    	tab_tabla.setCondicion("ide_faaup=-1");
    	tab_tabla.setGenerarPrimaria(false);
    	tab_tabla.getColumna("ide_tecaj").setValorDefecto("6");
    	tab_tabla.getColumna("ide_tecaj").setLectura(true);
    	tab_tabla.getColumna("ide_tecaj").setCombo("tes_caja", "ide_tecaj", "detalle_tecaj", "");
    	tab_tabla.getColumna("activo_faaup").setValorDefecto("true");
    	tab_tabla.getColumna("conciliado_faaup").setValorDefecto("false");
    	tab_tabla.getColumna("deposito_diferido_faaup").setValorDefecto("false");
    	tab_tabla.getColumna("validado_faaup").setValorDefecto("false");
    	tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("IDE_USUA"));
    	tab_tabla.getColumna("fecha_concilia_faaup").setValorDefecto(utilitario.getFechaActual());
    	tab_tabla.getColumna("ide_usua").setVisible(false);
    	tab_tabla.getColumna("deposito_diferido_faaup").setVisible(false);
    	tab_tabla.getColumna("activo_faaup").setLectura(true);
    	tab_tabla.getColumna("conciliado_faaup").setLectura(true);
    	tab_tabla.getColumna("validado_faaup").setLectura(true);
    	tab_tabla.getColumna("fecha_concilia_faaup").setLectura(true);
    	tab_tabla.getColumna("fecha_pago_archivo_faaup").setLectura(true);
    	tab_tabla.getColumna("ruc_cliente_faaup").setLectura(true);
    	tab_tabla.getColumna("cliente_faaup").setLectura(true);
    	tab_tabla.getColumna("agencia_faaup").setLectura(true);
    	tab_tabla.getColumna("valor_registrado_faaup").setLectura(true);
    	tab_tabla.getColumna("valor_archivo_faaup").setLectura(true);
    	tab_tabla.getColumna("id_transaccion_faaup").setLectura(true);
    	tab_tabla.getColumna("documento_conciliado_faaup").setLectura(true);
    	tab_tabla.getColumna("observacion_faaup").setLectura(true);
    	
    	tab_tabla.setColumnaSuma("valor_registrado_faaup,valor_archivo_faaup");
    	tab_tabla.setRows(10);
    	//tab_tabla.setLectura(true);
    	tab_tabla.dibujar();
    	
    	tab_tabla_diferido.setId("tab_tabla_diferido");
    	tab_tabla_diferido.setHeader("DEPOSITOS DIFERIDOS - NO CONCILIADOS");
    	tab_tabla_diferido.setTabla("fac_auditoria_pagos", "ide_faaup", 2);
    	tab_tabla_diferido.setGenerarPrimaria(false);
    	tab_tabla_diferido.setCondicion("ide_faaup=-1");
    	tab_tabla_diferido.getColumna("activo_faaup").setValorDefecto("true");
    	tab_tabla_diferido.getColumna("conciliado_faaup").setValorDefecto("false");
    	tab_tabla_diferido.getColumna("validado_faaup").setValorDefecto("false");
    	tab_tabla_diferido.getColumna("deposito_diferido_faaup").setValorDefecto("true");
    	tab_tabla_diferido.getColumna("deposito_diferido_faaup").setOrden(2);
    	tab_tabla_diferido.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("IDE_USUA"));
    	tab_tabla_diferido.getColumna("ide_usua").setVisible(false);
    	tab_tabla_diferido.getColumna("fecha_concilia_faaup").setVisible(false);
    	tab_tabla_diferido.getColumna("agencia_faaup").setVisible(false);
    	tab_tabla_diferido.getColumna("activo_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("conciliado_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("validado_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("deposito_diferido_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("fecha_pago_archivo_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("ruc_cliente_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("cliente_faaup").setLectura(true);
    	//tab_tabla_diferido.getColumna("agencia_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("valor_registrado_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("valor_archivo_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("id_transaccion_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("documento_conciliado_faaup").setLectura(true);
    	tab_tabla_diferido.getColumna("observacion_faaup").setLectura(true);
    	tab_tabla_diferido.setColumnaSuma("valor_registrado_faaup,valor_archivo_faaup");
    	tab_tabla_diferido.setRows(10);
    	//tab_tabla_diferido.setLectura(true);
    	tab_tabla_diferido.dibujar();
    	
    	
    	tab_tabla_diferido_conciliado.setId("tab_tabla_diferido_conciliado");
    	tab_tabla_diferido_conciliado.setHeader("DIFERIDOS CONCILIADOS");
    	tab_tabla_diferido_conciliado.setTabla("fac_auditoria_pagos", "ide_faaup", 3);
    	tab_tabla_diferido_conciliado.setGenerarPrimaria(false);
    	tab_tabla_diferido_conciliado.setCondicion("ide_faaup=-1");
    	tab_tabla_diferido_conciliado.getColumna("activo_faaup").setValorDefecto("true");
    	tab_tabla_diferido_conciliado.getColumna("conciliado_faaup").setValorDefecto("false");
    	tab_tabla_diferido_conciliado.getColumna("validado_faaup").setValorDefecto("false");
    	tab_tabla_diferido_conciliado.getColumna("deposito_diferido_faaup").setValorDefecto("true");
    	tab_tabla_diferido_conciliado.getColumna("deposito_diferido_faaup").setOrden(2);
    	tab_tabla_diferido_conciliado.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("IDE_USUA"));
    	tab_tabla_diferido_conciliado.getColumna("ide_usua").setVisible(false);
    	tab_tabla_diferido_conciliado.setColumnaSuma("valor_registrado_faaup,valor_archivo_faaup");
    	tab_tabla_diferido_conciliado.setRows(10);
    	tab_tabla_diferido_conciliado.setLectura(true);
    	tab_tabla_diferido_conciliado.dibujar();
    	
    	
    	con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REALIZAR LA CONCILIACION");
		con_guardar.setTitle("CONFIRMACION DE CONCILIACION");

		agregarComponente(con_guardar);
		
		
		tab_tabla_rec.setId("tab_tabla_rec");
		tab_tabla_rec.setHeader("RECAUDACIONES NO REPORTADAS EN EL ARCHIVO");
		tab_tabla_rec.setSql(ser_tesoreria.getSqlClienteCobro("2015-01-01"));
		tab_tabla_rec.setColumnaSuma("valor_recaudado");
		tab_tabla_rec.setLectura(true);
		tab_tabla_rec.setRows(5);
		tab_tabla_rec.dibujar();
    	
        PanelTabla pat_panel = new PanelTabla();       
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getChildren().add(itm_eliminarTodos);
        
        
        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir2(gri_formulario, tab_tabla_rec, "55%", "V");
        
        Division div1 = new Division();
        div1.setId("div1");
        div1.dividir2(tab_tabla_diferido, tab_tabla_diferido_conciliado, "50%", "V");

        Division div2 = new Division();
        div2.setId("div2");
        div2.dividir2(pat_panel, div1, "60%", "h");
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(div_division1,div2,"30%", "h");
        
        
        agregarComponente(div_division);
        
    }
    
    
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_tabla.limpiar();	
		utilitario.addUpdate("tab_tabla");// limpia y refresca el autocompletar
	}
	
	public void ingreso() {
		tab_tabla.setCondicion(" conciliado_faaup=true and validado_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"'");
        tab_tabla.ejecutarSql();
        tab_tabla_rec.limpiar();
        tab_tabla_diferido.setCondicion(" deposito_diferido_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"'");
        tab_tabla_diferido.ejecutarSql();
     
        tab_tabla_diferido_conciliado.setCondicion(" deposito_diferido_faaup=true and conciliado_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"'");
        tab_tabla_diferido_conciliado.ejecutarSql();
        tab_tabla_rec.sumarColumnas();
        tab_tabla_diferido.sumarColumnas();
        tab_tabla_diferido_conciliado.sumarColumnas();
        utilitario.addUpdate("tab_tabla,tab_tabla_rec,tab_tabla_diferido,tab_tabla_diferido_conciliado");
		utilitario.agregarMensajeInfo("Fecha Pago: ", cal_fecha_inicial.getFecha()+"");	
	}
	
	public void cambiatexto() {
		utilitario.agregarMensajeInfo("Documento Bancario", txt_documento_banco.getValue()+"");	
	}
	/**
	 * Valida el archivo para que pueda importar un rubro a la nomina
	 * @param evt
	 */
	
	public void eliminarTodos() {
		
		if(tab_tabla.getValor("validado_faaup").equals("true"))
		{
			utilitario.agregarMensajeInfo("Registros Conciliados: ", "No puede eliminar registros conciliados");	
			return;
		}
		
		utilitario.getConexion().ejecutarSql("delete from fac_auditoria_pagos where fecha_pago_archivo_faaup = '"+cal_fecha_inicial.getFecha()+"'");
		
	}
	
	public void validarArchivo(FileUploadEvent evt)
	{	
		int conciliados=0;
		int registrosLeidos=0;
		String fecha_pago_archivo_faaup;
		String ruc_cliente_faaup;
		String cliente_faaup;
		String agencia_faaup;
		double valor_registrado_faaup;
		double valor_archivo_faaup;
		String id_transaccion_faaup;
		String documento_conciliado_faaup;
		String observacion_faaup;
		lineas = new ArrayList<String[]>(); 
		
		StringBuilder ide_faaup = new StringBuilder();

		try {
			
			if(tab_tabla.getTotalFilas()>0)
			{
				utilitario.agregarMensajeError("Archivo Cargado ","No se puede validar el archivo porque existen registros...");
				return;
			}
			
			txt_documento_banco.setValue(evt.getFile().getFileName());
			TablaGenerica clientePago = utilitario.consultar(ser_tesoreria.getSqlClienteCobro(cal_fecha_inicial.getFecha()));
			TablaGenerica clientePagoAux = utilitario.consultar(ser_tesoreria.getSqlClienteCobro(utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(cal_fecha_inicial.getFecha()), -1))));
			TablaGenerica pagosDiferidos = utilitario.consultar(ser_tesoreria.getSqlPagosDiferidosPacifico());
			
			//clientePago.imprimirSql();
			
			if(clientePago.getTotalFilas()>0)
			{
				try 
				{
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(evt.getFile().getInputstream(), "UTF-8")); 
		            String line;
		            while ((line = bufferedReader.readLine()) != null) {
		                //System.out.println(line);
		                String[] linea = line.split ("\t");
		                registrosLeidos++;
		                
		                if(registrosLeidos>1)
		                {
		                	fecha_pago_archivo_faaup=pckUtilidades.CConversion.CStr(linea[6]).replaceAll("/", "-");
		                	//System.out.println("fecha_pago_archivo_faaup "+fecha_pago_archivo_faaup);
		                	if(!cal_fecha_inicial.getFecha().equals(fecha_pago_archivo_faaup))
		        			{	
		                		System.out.println("Archivo fuera de tiempo... No se puede validar el archivo porque es de una fecha diferente al requerido, Fecha en Archivo - "+fecha_pago_archivo_faaup+ ", Fecha esperada - "+cal_fecha_inicial.getFecha());
		        				utilitario.agregarNotificacionInfo("Archivo fuera de tiempo","No se puede validar el archivo porque es de una fecha diferente al requerido, Fecha en Archivo - "+fecha_pago_archivo_faaup+ ", Fecha esperada - "+cal_fecha_inicial.getFecha());
		        				return;
		        			}
		                	
		                	String valor_registrado = obtenerCobro(clientePago,pckUtilidades.CConversion.CStr(linea[0]), pckUtilidades.CConversion.CStr(linea[4]));
		                	valor_registrado_faaup=pckUtilidades.CConversion.CDbl_2(valor_registrado);

		                	ruc_cliente_faaup=pckUtilidades.CConversion.CStr(linea[0]);
		                	cliente_faaup=pckUtilidades.CConversion.CStr(linea[1]);
		                	agencia_faaup=pckUtilidades.CConversion.CStr(linea[2]);		                	
		                	valor_archivo_faaup=pckUtilidades.CConversion.CDbl_2(pckUtilidades.CConversion.CStr(linea[3]).replace(",", "."));
		                	id_transaccion_faaup=pckUtilidades.CConversion.CStr(linea[4]);
		                	documento_conciliado_faaup=pckUtilidades.CConversion.CStr(txt_documento_banco.getValue());
		                	observacion_faaup="Tipo Pago: "+pckUtilidades.CConversion.CStr(linea[5])+" - banco Pacifico. fecha: "+pckUtilidades.CConversion.CStr(linea[6]);
		                	
		                	if(valor_registrado_faaup<=0)//pagos no registrados
		                	{
		                		boolean diferido=false;
		                		for(int i=0;i<pagosDiferidos.getTotalFilas();i++)
		                		{
		                			if(id_transaccion_faaup.equals(pagosDiferidos.getValor(i, "id_transaccion_faaup")))
		                			{
		                				diferido=true;
		                				break;		
		                			}
		                		}
		                		if(!diferido)
		                		{
			                		valor_registrado = obtenerCobro(clientePagoAux,pckUtilidades.CConversion.CStr(linea[0]), pckUtilidades.CConversion.CStr(linea[4]));
			                		valor_registrado_faaup=pckUtilidades.CConversion.CDbl_2(valor_registrado);
			                		System.out.println("pago no registrado: "+valor_registrado_faaup);
		                		}
		                	}
		                	
		                	if(valor_registrado_faaup>0)
							{
		                		tab_tabla.insertar();	
								tab_tabla.setValor("fecha_pago_archivo_faaup",fecha_pago_archivo_faaup);
								//tab_tabla.setValor("fecha_concilia_faaup","");
								tab_tabla.setValor("ruc_cliente_faaup",ruc_cliente_faaup);
								tab_tabla.setValor("cliente_faaup",cliente_faaup);
								tab_tabla.setValor("agencia_faaup",agencia_faaup);
								tab_tabla.setValor("valor_registrado_faaup",valor_registrado_faaup+"");
								tab_tabla.setValor("valor_archivo_faaup",valor_archivo_faaup+"");
								tab_tabla.setValor("id_transaccion_faaup",id_transaccion_faaup);
								tab_tabla.setValor("documento_conciliado_faaup",documento_conciliado_faaup);
								tab_tabla.setValor("observacion_faaup",observacion_faaup);
							
								if(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_registrado_faaup"))!=pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_archivo_faaup")))
								{
									tab_tabla.setValor("observacion_faaup",tab_tabla.getValor("observacion_faaup")+" Diferencia: "+
											(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_registrado_faaup"))-pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_archivo_faaup"))));
								}
								
								tab_tabla.setValor("validado_faaup","true");
								conciliados++;
							}
		                	else
		                	{
		                		
		                		for(int i=0;i<pagosDiferidos.getTotalFilas();i++)
		                		{
		                			if(id_transaccion_faaup.equals(pagosDiferidos.getValor(i, "id_transaccion_faaup")))
		                			{
		                				if(ide_faaup.toString().isEmpty()==false){
		                					ide_faaup.append(",");
			            		        }
		                				//ide_faaup.append(clientePago.getValor(i,"idTran_cod_aut").toString());
		                				ide_faaup.append("'"+id_transaccion_faaup+"'");
		                				lineas.add(linea);
		                			}
		                		}
		                	}
		                	
		                	
		                }
		            }
	
		        } catch (Exception ex) {
		        	utilitario.agregarMensajeError("Error uploading the file "+ ex,"");
		        }
			}
			else
			{
				utilitario.agregarMensajeError("No se pudo conciliar el archivo", "No existen cobros registrados en el ERP de la fecha: "+cal_fecha_inicial.getFecha());
			}
			
			utilitario.addUpdate("tab_tabla,txt_documento_banco");
			registrosLeidos--;
			
			if(registrosLeidos != clientePago.getTotalFilas())
			{
				obtenerCobrosNoRegistrados(clientePago);
				utilitario.agregarNotificacionInfo("Validación","Existe diferencias de registros, Registros en el archivo: " +registrosLeidos+ ", De los cuales se validaron: "+conciliados + ", Pero en el ERP se tiene: "+clientePago.getTotalFilas()+", cobros registrados. "
						+ " Valor archivo: "+pckUtilidades.CConversion.CDbl_2(tab_tabla.getSumaColumna("valor_archivo_faaup")) + " Valor Registrado ERP: "+pckUtilidades.CConversion.CDbl_2(clientePago.getSumaColumna("valor_recaudado")));
			}
			else
			{
				utilitario.agregarNotificacionInfo("Validación", "Registros validados: "+conciliados+" de "+registrosLeidos+"...");
			}
			tab_tabla.sumarColumnas();
			tab_tabla_diferido.sumarColumnas();
			if(ide_faaup.toString().length()>0)
				//tab_tabla_diferido_conciliado.setCondicion(" ide_faaup in ("+ide_faaup.toString()+") ");
				tab_tabla_diferido_conciliado.setCondicion(" id_transaccion_faaup in ("+ide_faaup.toString()+") ");
			
			tab_tabla_diferido_conciliado.ejecutarSql();
			tab_tabla_diferido_conciliado.sumarColumnas();
	
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.agregarMensajeError("No se pudo conciliar el archivo", "Debido a una inconsistencia no se pudo culminar exitosamente con la conciliacion del archivo");
			System.out.println("Error conciliacion Bancaria: "+e);
		}	
		System.out.println("No Reg Validados: "+conciliados);
	}
	
	
	public String obtenerCobro(TablaGenerica clientePago, String ruc, String id_transaccion)
	{
		String valor="0";
		
		for(int i=0;i<clientePago.getTotalFilas();i++)
		{
			if(clientePago.getValor(i,"ruc_comercial").equals(ruc) && clientePago.getValor(i,"idTran_cod_aut").equals(id_transaccion))
			{
				valor="0";
				//valores.append(pckUtilidades.CConversion.CDbl_2(clientePago.getValor(i,"facturas"))+pckUtilidades.CConversion.CDbl_2(clientePago.getValor(i,"intereses")));
				valor=clientePago.getValor(i,"valor_recaudado");
			
				break;
			}
		}
		//System.out.println("obtenerDeuda ruc: "+ruc+" valores: "+valores.toString());
		return valor;
	}
	
	public void obtenerCobrosNoRegistrados(TablaGenerica clientePago)
	{
		transacciones="";
		StringBuilder id_tran=new StringBuilder();
		boolean registrado;
		
		for(int i=0;i<clientePago.getTotalFilas();i++)
		{
			registrado=false;
			for(int j=0;j<tab_tabla.getTotalFilas();j++)
			{
				if(clientePago.getValor(i,"idTran_cod_aut").equals(tab_tabla.getValor(j,"id_transaccion_faaup")) && !registrado)
				{
					registrado=true;
					break;
				}
			}
			
			if(!registrado)
			{
				if(id_tran.toString().isEmpty()==false){
					id_tran.append(",");
		        }
				id_tran.append("'").append(clientePago.getValor(i,"idTran_cod_aut").toString()).append("'");
			}
		}
		
		transacciones = id_tran.toString().length()>0 ? id_tran.toString():"'0'";
		
		System.out.println("id_tran: "+transacciones);
		
		tab_tabla_rec.setSql("select * from ( "+clientePago.getSql()+" ) a where idTran_cod_aut in ("+transacciones+") ");
		//tab_tabla_rec.setCondicion(" idTran_cod_aut in ("+id_tran.toString()+") ");
		tab_tabla_rec.ejecutarSql();
		
		for(int i=0;i<tab_tabla_rec.getTotalFilas();i++)
		{
			tab_tabla_diferido.insertar();	
    		tab_tabla_diferido.setValor("fecha_pago_archivo_faaup",cal_fecha_inicial.getFecha());
    		tab_tabla_diferido.setValor("ruc_cliente_faaup",tab_tabla_rec.getValor(i, "ruc_comercial"));
    		tab_tabla_diferido.setValor("cliente_faaup",tab_tabla_rec.getValor(i, "razon_social"));
    		//tab_tabla_diferido.setValor("agencia_faaup",agencia_faaup);
    		tab_tabla_diferido.setValor("valor_registrado_faaup",tab_tabla_rec.getValor(i, "valor_recaudado"));
    		tab_tabla_diferido.setValor("valor_archivo_faaup","0.0");
    		tab_tabla_diferido.setValor("id_transaccion_faaup",tab_tabla_rec.getValor(i, "idtran_cod_aut"));
    		tab_tabla_diferido.setValor("documento_conciliado_faaup",txt_documento_banco.getValue()+"");
    		tab_tabla_diferido.setValor("observacion_faaup",tab_tabla_rec.getValor(i, "forma_cobro"));
		}
		
	}
	
	public void conciliar()
	{
		
		if (!con_guardar.isVisible()){

			con_guardar.setMessage("ESTA SEGURO DE REALIZAR LA CONCILIACION");
			con_guardar.setTitle("CONFIRMACION DE PROCESO BATCH");
			con_guardar.getBot_aceptar().setMetodo("conciliar");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}
		else
		{
			con_guardar.cerrar();
			
			int exitos=0;
			for(int i=0;i<tab_tabla.getTotalFilas();i++)
			{
				if(tab_tabla.getValor(i,"validado_faaup").equals("true"))
				{
					tab_tabla.modificar(i);
					try
                    {	
						if(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor(i,"valor_registrado_faaup")) == pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor(i,"valor_archivo_faaup")))
						{
	
							tab_tabla.setValor(i,"conciliado_faaup","true");
							exitos++;
						}
						
                    }catch(Exception ex)
                    {
                    	System.out.println("Error al conciliar: "+ex.getMessage());
                    	tab_tabla.setValor(i,"activo_faaup","false");
                    }
				}
				else
					tab_tabla.setValor(i,"activo_faaup","false");
			}
			
			
			for(int i=0;i<tab_tabla_diferido.getTotalFilas();i++)
			{
				utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest=24, diferido_fafac=true, conciliado_fafac=false where ide_fafac in (select ide_fafac from fac_cobro where cod_autorizacion_facob like '"+tab_tabla_diferido.getValor(i,"id_transaccion_faaup")+"')");
			}
			
			for(int i=0;i<tab_tabla_diferido_conciliado.getTotalFilas();i++)
			{
				tab_tabla_diferido_conciliado.modificar(i);
				double valor_archivo_faaup=0;
				String agencia_faaup="";
				String observacion_faaup="";
				for (String [] linea: lineas)							
					if(pckUtilidades.CConversion.CStr(linea[4]).equals(tab_tabla_diferido_conciliado.getValor(i,"id_transaccion_faaup")))
					{
						agencia_faaup=pckUtilidades.CConversion.CStr(linea[2]);
						valor_archivo_faaup=pckUtilidades.CConversion.CDbl_2(pckUtilidades.CConversion.CStr(linea[3]).replace(",", "."));
						observacion_faaup="Tipo Pago: "+pckUtilidades.CConversion.CStr(linea[5])+" - banco Pacifico. fecha: "+pckUtilidades.CConversion.CStr(linea[6]);
					}
				
				tab_tabla_diferido_conciliado.setValor(i,"fecha_concilia_faaup",utilitario.getFechaActual());
				tab_tabla_diferido_conciliado.setValor(i,"deposito_diferido_faaup","false");
				tab_tabla_diferido_conciliado.setValor(i,"validado_faaup","true");
				tab_tabla_diferido_conciliado.setValor(i,"conciliado_faaup","true");
				tab_tabla_diferido_conciliado.setValor(i,"valor_archivo_faaup",valor_archivo_faaup+"");
				tab_tabla_diferido_conciliado.setValor(i,"documento_conciliado_faaup",pckUtilidades.CConversion.CStr(txt_documento_banco.getValue()));
				tab_tabla_diferido_conciliado.setValor(i,"agencia_faaup",agencia_faaup);
				tab_tabla_diferido_conciliado.setValor(i,"observacion_faaup","Conciliado Diferido - "+observacion_faaup);
				
				utilitario.getConexion().ejecutarSql("update fac_factura set ide_coest=30, diferido_fafac=false,conciliado_fafac=true  where ide_fafac in (select ide_fafac from fac_cobro where cod_autorizacion_facob like '"+tab_tabla_diferido_conciliado.getValor(i,"id_transaccion_faaup")+"')");
			}
			
			tab_tabla.guardar();
			tab_tabla_diferido.guardar();
			tab_tabla_diferido_conciliado.guardar();
	        guardarPantalla();
	        
	        tab_tabla.setCondicion(" conciliado_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"' and documento_conciliado_faaup like '"+txt_documento_banco.getValue()+"'");
	        tab_tabla.ejecutarSql();
	        tab_tabla_diferido.setCondicion(" deposito_diferido_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"' and documento_conciliado_faaup like '"+txt_documento_banco.getValue()+"'");
	        tab_tabla_diferido.ejecutarSql();
	        tab_tabla_diferido_conciliado.ejecutarSql();
	        utilitario.addUpdate("tab_tabla,tab_tabla_diferido,tab_tabla_diferido_conciliado");
	        
	        utilitario.agregarNotificacionInfo("Conciliacion", "Se conciliaron -"+exitos+"- registros...");
	        
		}
	}
	
	
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
        tab_tabla.setCondicion("fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"'");
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

	
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }


	public Tabla getTab_tabla_rec() {
		return tab_tabla_rec;
	}


	public void setTab_tabla_rec(Tabla tab_tabla_rec) {
		this.tab_tabla_rec = tab_tabla_rec;
	}


	public Tabla getTab_tabla_diferido() {
		return tab_tabla_diferido;
	}


	public void setTab_tabla_diferido(Tabla tab_tabla_diferido) {
		this.tab_tabla_diferido = tab_tabla_diferido;
	}


	public Tabla getTab_tabla_diferido_conciliado() {
		return tab_tabla_diferido_conciliado;
	}


	public void setTab_tabla_diferido_conciliado(Tabla tab_tabla_diferido_conciliado) {
		this.tab_tabla_diferido_conciliado = tab_tabla_diferido_conciliado;
	}


}
