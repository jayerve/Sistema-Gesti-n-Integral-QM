/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_facturacion;


import javax.ejb.EJB;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.event.FileUploadEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
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
 * @Descripcion Conciliacion con el archivo enviado del banco del produbanco
 */
public class pre_conciliacion_banco_produ extends Pantalla {
	
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla_rec = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Upload upl_archivo=new Upload();
	private Texto txt_documento_banco = new Texto();
	private Confirmar con_guardar=new Confirmar();
	private String transacciones;
	private String ide_tecaj="3";
	
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

    public pre_conciliacion_banco_produ() { 
    	
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
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Cargar archivo");
		upl_archivo.setCancelLabel("Cancelar Seleccion");
		//bar_botones.agregarComponente(upl_archivo);
    	gri_formulario.setFooter(upl_archivo);

    	ItemMenu itm_eliminarTodos = new ItemMenu();
    	itm_eliminarTodos.setValue("Eliminar Todos");
    	itm_eliminarTodos.setMetodo("eliminarTodos");
    	itm_eliminarTodos.setIcon("ui-icon-alert");
    	
    	tab_tabla.setId("tab_tabla");
    	tab_tabla.setHeader("CONCILIACIÓN BANCO PRODUBANCO");
    	tab_tabla.setTabla("fac_auditoria_pagos", "ide_faaup", 1);
    	tab_tabla.setCondicion("ide_faaup=-1");
    	tab_tabla.setGenerarPrimaria(false);
    	tab_tabla.getColumna("ide_tecaj").setLectura(true);
    	tab_tabla.getColumna("ide_tecaj").setValorDefecto("3");
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
    	tab_tabla.getColumna("secuencial_banco_faaup").setLectura(true);
    	tab_tabla.getColumna("comprobante_faaup").setLectura(true);
    	
    	tab_tabla.setColumnaSuma("valor_registrado_faaup,valor_archivo_faaup");
    	tab_tabla.setRows(10);
    	//tab_tabla.setLectura(true);
    	tab_tabla.dibujar();
    	
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
        
        PanelTabla pat_panel_rec = new PanelTabla();       
        pat_panel_rec.setPanelTabla(tab_tabla_rec);
        
        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir2(gri_formulario, pat_panel_rec, "55%", "V");        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(div_division1,pat_panel,"30%", "h");
        
        
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
		tab_tabla.setCondicion(" ide_tecaj="+ide_tecaj+" and conciliado_faaup=true and validado_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"'");
        tab_tabla.ejecutarSql();
        tab_tabla_rec.limpiar();
        tab_tabla_rec.sumarColumnas();
        utilitario.addUpdate("tab_tabla,tab_tabla_rec");
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
		String contrapartida="";
		String secuencial_banco="";
		String tiposervicio="";
		
		try {
			
			if(tab_tabla.getTotalFilas()>0)
			{
				utilitario.agregarMensajeError("Archivo Cargado ","No se puede validar el archivo porque existen registros...");
				return;
			}
			
			txt_documento_banco.setValue(evt.getFile().getFileName());
			
			try 
			{

				Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
				Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
				if (hoja == null) {
					utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
					return;
				}
				int int_fin = hoja.getRows();
				upl_archivo.setNombreReal(evt.getFile().getFileName());
				System.out.println("getRows: "+int_fin);
				
				TablaGenerica clientePago = utilitario.consultar(ser_tesoreria.getSqlClienteCobroProdubanco(cal_fecha_inicial.getFecha(),cal_fecha_inicial.getFecha())); //"2022-01-31"
				//TablaGenerica clientePago = utilitario.consultar(ser_tesoreria.getSqlClienteCobroProdubanco(cal_fecha_inicial.getFecha(),"2022-03-31")); 
				clientePago.imprimirSql();
				
				if(clientePago.getTotalFilas()>0)
				{

					for (int i = 7; i < int_fin; i++) 
					{
						registrosLeidos++;
						contrapartida=pckUtilidades.CConversion.CStr(hoja.getCell(21, i).getContents());
						System.out.println("validando contrapartida: "+contrapartida);
						if(contrapartida.length()>0)
						{
		                	fecha_pago_archivo_faaup=pckUtilidades.CConversion.CStr(hoja.getCell(9, i).getContents()).replaceAll("/", "-");
		                	//System.out.println("fecha_pago_archivo_faaup "+fecha_pago_archivo_faaup);
		                	if(!cal_fecha_inicial.getFecha().equals(fecha_pago_archivo_faaup))
		        			{	
		                		System.out.println("Archivo fuera de tiempo... No se puede validar el archivo porque es de una fecha diferente al requerido, Fecha en Archivo - "+fecha_pago_archivo_faaup+ ", Fecha esperada - "+cal_fecha_inicial.getFecha());
		        				utilitario.agregarNotificacionInfo("Archivo fuera de tiempo","No se puede validar el archivo porque es de una fecha diferente al requerido, Fecha en Archivo - "+fecha_pago_archivo_faaup+ ", Fecha esperada - "+cal_fecha_inicial.getFecha());
		                		utilitario.agregarMensajeInfo("Archivo fuera de tiempo","No se puede validar el archivo porque es de una fecha diferente al requerido, Fecha en Archivo - "+fecha_pago_archivo_faaup+ ", Fecha esperada - "+cal_fecha_inicial.getFecha());
		        				tab_tabla.sumarColumnas();
		        		        utilitario.addUpdate("tab_tabla");	 
		        				return;
		        			}
		                	
		                	secuencial_banco=pckUtilidades.CConversion.CStr(hoja.getCell(14, i).getContents());
		                	agencia_faaup=pckUtilidades.CConversion.CStr(hoja.getCell(16, i).getContents());
		                	valor_archivo_faaup=pckUtilidades.CConversion.CDbl_2(pckUtilidades.CConversion.CStr(hoja.getCell(7, i).getContents()).replace(",", "."));
		                	observacion_faaup="Tipo Pago: "+pckUtilidades.CConversion.CStr(hoja.getCell(8, i).getContents())+" - banco Produbanco. fecha: "+pckUtilidades.CConversion.CStr(hoja.getCell(19, i).getContents());
		                	documento_conciliado_faaup=pckUtilidades.CConversion.CStr(txt_documento_banco.getValue());
		                	
		                	//validar el id de transaccion
		                	id_transaccion_faaup=pckUtilidades.CConversion.CStr(hoja.getCell(20, i).getContents()); //validar el id de transaccion
		                	String valor_registrado = obtenerCobro(clientePago,pckUtilidades.CConversion.CStr(hoja.getCell(21, i).getContents()), pckUtilidades.CConversion.CStr(hoja.getCell(20, i).getContents()));
		                	valor_registrado_faaup=pckUtilidades.CConversion.CDbl_2(valor_registrado);
		                	ruc_cliente_faaup=pckUtilidades.CConversion.CStr(hoja.getCell(21, i).getContents());
		                	cliente_faaup=pckUtilidades.CConversion.CStr(hoja.getCell(23, i).getContents());
		                	tiposervicio=pckUtilidades.CConversion.CStr(hoja.getCell(24, i).getContents());
		                	
		                	if(valor_registrado_faaup<=0)//pagos no registrados
		                	{
		                		System.out.println("id_transaccion_faaup: "+id_transaccion_faaup+" - pago no registrado: "+valor_registrado_faaup);
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
								tab_tabla.setValor("secuencial_banco_faaup",secuencial_banco);
								tab_tabla.setValor("comprobante_faaup", (!tiposervicio.toUpperCase().contains("CONTRATOS"))+"");
							
								if(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_registrado_faaup"))!=pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_archivo_faaup")))
								{
									tab_tabla.setValor("observacion_faaup",tab_tabla.getValor("observacion_faaup")+" Diferencia: "+
											(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_registrado_faaup"))-pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_archivo_faaup"))));
								}
								
								tab_tabla.setValor("validado_faaup","true");
								conciliados++;
							}		                	
		                	
		                }
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
				
			} catch (Exception ex) {
	        	utilitario.agregarMensajeError("Error uploading the file "+ ex,"");
	        }
	
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
		tab_tabla_rec.sumarColumnas();
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
			
			if(transacciones.length()>0)
			{
				TablaGenerica pagosNoReportados = utilitario.consultar("select * from ("+ser_tesoreria.getSqlClienteCobroProdubanco(cal_fecha_inicial.getFecha(),cal_fecha_inicial.getFecha())+") a where idTran_cod_aut in ("+transacciones+") ");
				//TablaGenerica pagosNoReportados = utilitario.consultar("select * from ("+ser_tesoreria.getSqlClienteCobroProdubanco(cal_fecha_inicial.getFecha(),"2022-03-31")+") a where idTran_cod_aut in ("+transacciones+") ");
				//reversar
				String origen="BANCO-PRODUBANCO";
				boolean reversoAuto=false;
				boolean produccion=true;
				
				for(int i=0;i<pagosNoReportados.getTotalFilas();i++)
				{
					System.out.println("Reversar idTran_cod_aut: "+pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"idTran_cod_aut")));
					System.out.println("Reversar ruc_comercial: "+pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"ruc_comercial")));
					
					
					try
	                {
						boolean resultado = pckRecaudacion.Recaudacion.reversoPagoProdBank(pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"idTran_cod_aut")),
								"",
								pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"ruc_comercial")),
								pckUtilidades.CConversion.CDbl_2(pagosNoReportados.getValor(i,"valor_recaudado")),
								origen,pckUtilidades.CConversion.CInt(ide_tecaj),reversoAuto,produccion);
						
						if(!resultado)
						{
							System.out.println("Reversar idTran_cod_aut: "+pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"idTran_cod_aut")));
		                	utilitario.agregarMensajeError("Reversar Reversar"," idTran_cod_aut: "+pckUtilidades.CConversion.CStr(pagosNoReportados.getValor(i,"idTran_cod_aut")));
						}
	                }
					catch(Exception ex)
	                {
	                	System.out.println("Error al reversar: "+ex.getMessage());
	                	utilitario.agregarMensajeError("Error al reversar", ex.getMessage());
	                }	
	                
				}
			}
			
			tab_tabla.guardar();
	        guardarPantalla();
	        
	        tab_tabla.setCondicion(" ide_tecaj="+ide_tecaj+" and conciliado_faaup=true and fecha_pago_archivo_faaup='"+cal_fecha_inicial.getFecha()+"' and documento_conciliado_faaup like '"+txt_documento_banco.getValue()+"'");
	        tab_tabla.ejecutarSql();
	        tab_tabla.sumarColumnas();
	        tab_tabla_rec.limpiar();
	        tab_tabla_rec.sumarColumnas();
	        utilitario.addUpdate("tab_tabla,tab_tabla_rec");        
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

}
