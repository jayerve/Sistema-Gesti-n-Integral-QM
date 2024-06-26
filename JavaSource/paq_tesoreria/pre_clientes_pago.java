package paq_tesoreria;

import java.io.File;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_clientes_pago extends Pantalla{
	
	private Tabla tab_cab_spi = new Tabla();
	private Tabla tab_det_spi = new Tabla();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
	private SeleccionTabla sel_transaccion = new SeleccionTabla();
	private Etiqueta eti_totales=new Etiqueta();
	private Combo com_anio=new Combo();
	private Confirmar con_guardar=new Confirmar();
	
	private double dou_total=0;
	private String cuenta_transferencia="0";
	
	private String str_asunto = "CONFIRMACIÓN DE TRANSFERENCIA";
	private TablaGenerica tab_correo_envio;
	private EnvioMail envMail;
	//private pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();
	
	@EJB
	private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_clientes_pago ()
	{
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}
		
		cuenta_transferencia=utilitario.getVariable("p_tipos_cta_tran");
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_consulta_factura = new Boton();
		bot_consulta_factura.setValue("Consultar Pagos");
		bot_consulta_factura.setMetodo("importar");
		bar_botones.agregarBoton(bot_consulta_factura);
		
		Boton bot_excel=new Boton();
		bot_excel.setValue("Exportar EXCEL");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarExcel");
		bar_botones.agregarBoton(bot_excel);
		
		Boton bot_enviar = new Boton();
		bot_enviar.setMetodo("enviarCorreo");
		bot_enviar.setValue("Enviar E-mail");
		bot_enviar.setIcon("ui-icon-mail-closed");
		bar_botones.agregarBoton(bot_enviar);
		
		tab_cab_spi.setId("tab_cab_spi");
		tab_cab_spi.setTabla("SPI_TRANSFERENCIAS", "IDE_SPTRA", 1);
		tab_cab_spi.getColumna("ACTIVO_SPTRA").setCheck();
		tab_cab_spi.getColumna("ACTIVO_SPTRA").setValorDefecto("true");	
		tab_cab_spi.getColumna("pago_nomina_sptra").setValorDefecto("false");	
		tab_cab_spi.getColumna("FECHA_SPTRA").setValorDefecto(utilitario.getFechaActual());
		tab_cab_spi.getColumna("HORA_SPTRA").setValorDefecto(utilitario.getFormatoHora("12:00:00"));
		tab_cab_spi.getColumna("ide_tetic").setCombo(ser_Tesoreria.getConsultaTipoConcepto("true,false"));
		tab_cab_spi.setCondicion("pago_nomina_sptra=false and ide_geani=-1"); 
		//tab_cab_spi.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_cab_spi.agregarRelacion(tab_det_spi);		
		tab_cab_spi.onSelect("seleccionarTabla1");
		tab_cab_spi.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_cab_spi);
		
		tab_det_spi.setId("tab_det_spi");
		tab_det_spi.setTabla("SPI_TRANSFERENCIAS_DETALLE", "IDE_SPTRD", 2);	
		tab_det_spi.setRows(15);
		tab_det_spi.getColumna("ACTIVO_SPTRD").setCheck();
		tab_det_spi.getColumna("ACTIVO_SPTRD").setValorDefecto("true");
		tab_det_spi.getColumna("notificado_sptrd").setValorDefecto("false");
		tab_det_spi.setLectura(true);
		tab_det_spi.dibujar();	
		
		ItemMenu itm_envia_correo = new ItemMenu();
		itm_envia_correo.setValue("Enviar E-mail");
		itm_envia_correo.setMetodo("enviarCorreoIndividual");
		itm_envia_correo.setIcon("ui-icon-mail-closed");
		
		ItemMenu itm_importar_email = new ItemMenu();
		itm_importar_email.setValue("Importar E-mails");
		itm_importar_email.setMetodo("importarCorreo");
		itm_importar_email.setIcon("ui-icon-arrowrefresh-1-w");
		
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_det_spi);
		pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
		pat_panel2.getMenuTabla().getItem_eliminar().setValue("Eliminar Todos");
		pat_panel2.getMenuTabla().getItem_eliminar().setMetodo("eliminarDetalles");
		pat_panel2.getMenuTabla().getChildren().add(itm_envia_correo);
		pat_panel2.getMenuTabla().getChildren().add(itm_importar_email);
		tab_det_spi.setLectura(false);
		Division div_division = new Division();
		div_division.setId("div_division");
		
		eti_totales.setId("eti_totales");		
		eti_totales.setEstiloCabecera("width:%;font-size: 16px;font-weight: bold;display: block;padding-left:10px;");

		Grid gri_totales=new Grid();		
		gri_totales.setWidth("100%");
		gri_totales.getChildren().add(eti_totales);
		gri_totales.setStyle("overflow:hidden;");;
		Division div_div=new Division();
		div_div.setFooter(pat_panel2, gri_totales, "90%");			

		div_division.dividir2(pat_panel1, div_div, "30%", "H");
		agregarComponente(div_division);
		
		sel_transaccion.setId("sel_transaccion");
		sel_transaccion.setTitle("PAGOS");
		sel_transaccion.setSeleccionTabla(ser_Tesoreria.getActualizarConsultaPagos(false,"","",cuenta_transferencia), "reff");
		sel_transaccion.getTab_seleccion().getColumna("comprobante_egreso").setFiltro(true);
		sel_transaccion.getTab_seleccion().getColumna("ruc").setFiltro(true);
		sel_transaccion.getTab_seleccion().getColumna("ruc").setLongitud(20);
		sel_transaccion.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		sel_transaccion.getTab_seleccion().getColumna("nombre_tepro").setLongitud(50);
		//sel_transaccion.getTab_seleccion().ejecutarSql();
		sel_transaccion.getBot_aceptar().setMetodo("importar");
		agregarComponente(sel_transaccion);
		

		dou_total=tab_det_spi.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
		eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE ENVIAR MAILS A TODOS LOS PROVEEDORES");
		con_guardar.setTitle("CONFIRMACION DE ENVIO");
		con_guardar.getBot_aceptar().setMetodo("enviarCorreo");
		
		agregarComponente(con_guardar);
		
		inicializaCalendario();
		
		tab_correo_envio = utilitario.consultar("select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co  left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "
				+ " where activo_cpla = true and activo_corr = true and co.ide_corr=4 ");
		
		envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"), tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"),
				tab_correo_envio.getValor("clave_corr"));

	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_cab_spi.setCondicion("pago_nomina_sptra=false and ide_geani="+com_anio.getValue());
			tab_cab_spi.ejecutarSql();
			tab_det_spi.ejecutarValorForanea(tab_cab_spi.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("importar");
		agregarComponente(sel_calendario);
	}
	
	public void importarCorreo() {
		

		for (int i=0; i < tab_det_spi.getTotalFilas();i++){
			
			if(pckUtilidades.CConversion.CInt(tab_det_spi.getValor(i,"ide_tecpo"))>0) //solo pagos no NOMINA
			{
				//if(pckUtilidades.CConversion.CInt(tab_det_spi.getValor(i,"cod_concepto_pago_sptrd"))==40102)
				//{
					TablaGenerica tab_infoProv = utilitario.consultar("select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro, detalle_tecor as correo "
							+" from tes_proveedor b "
							+" join tes_correo cor on cor.ide_tepro=b.ide_tepro and coalesce(notificacion_tecor,false)=true and coalesce(activo_tecor,false)=true "
							+" where coalesce(ruc_representante_tepro,ruc_tepro) like '"+pckUtilidades.CConversion.CStr(tab_det_spi.getValor(i,"cedula_sptrd"))+"'");
					
					TablaGenerica tab_infoCorreoAdmin = utilitario.consultar("SELECT ide_gtcor,detalle_gtcor"
							+" FROM tes_comprobante_pago cp"
							+" left join pre_archivo ar on ar.ide_prtra=cp.ide_prtra"
							+" left join pre_contrato_administrador pa on pa.ide_prcon=ar.ide_prcon"
							+" left join GEN_EMPLEADOS_DEPARTAMENTO_PAR epar on epar.IDE_GEEDP=pa.IDE_GEEDP"
							+" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP"
							+" left join gth_correo co on co.ide_gtemp=emp.ide_gtemp and co.notificacion_gtcor=true and co.activo_gtcor=true"
							+" where cp.ide_tecpo= "+tab_det_spi.getValor(i,"ide_tecpo"));
					
					
					//tab_infoProv.imprimirSql();
					if(tab_infoProv.getTotalFilas()>0)
					{
						
						tab_det_spi.modificar(i);
						String correo=pckUtilidades.CConversion.CStr(tab_infoProv.getValor(0,"correo"));
						//tab_det_spi.setValor(i,"correo_sptrd",pckUtilidades.CConversion.CStr(tab_infoProv.getValor(0,"correo")));
						if(tab_infoCorreoAdmin.getTotalFilas()>0)
						{
							if(pckUtilidades.CConversion.CStr(tab_infoCorreoAdmin.getValor(0,"detalle_gtcor")).length()>4)
								correo+=","+pckUtilidades.CConversion.CStr(tab_infoCorreoAdmin.getValor(0,"detalle_gtcor"));
						}
						
						tab_det_spi.setValor(i,"correo_sptrd",correo);
					}
				//}
					else
					{
						TablaGenerica tab_infoEmpleado = utilitario.consultar("select emp.ide_gtemp, documento_identidad_gtemp, detalle_gtcor as correo "
								+" from gth_empleado emp "
								+" join gth_correo cor on cor.ide_gtemp=emp.ide_gtemp and coalesce(activo_gtcor,false)=true and coalesce(notificacion_gtcor,false)=true "
								+" where documento_identidad_gtemp like '"+pckUtilidades.CConversion.CStr(tab_det_spi.getValor(i,"cedula_sptrd"))+"'");
						
						//tab_infoEmpleado.imprimirSql();
						
						if(tab_infoEmpleado.getTotalFilas()>0)
						{
							tab_det_spi.modificar(i);
							tab_det_spi.setValor(i,"correo_sptrd",pckUtilidades.CConversion.CStr(tab_infoEmpleado.getValor(0,"correo")));
						}
					}
			}
			
		}
		
		tab_det_spi.guardar();
		guardarPantalla();
		
		utilitario.agregarMensajeInfo("Importacion de correo finalizada...", "");
		
	}
	
	public void enviarCorreo() {
		
		if(tab_cab_spi.getValorSeleccionado() == null)
		{
			utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar (Cabecera)");
			return;
		}

		if (!con_guardar.isVisible())
		{
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
			con_guardar.cerrar();
			//utilitario.agregarMensajeInfo("Envio de correo masivo en costruccion", "....");
			
			
			for(int j=0;j<tab_det_spi.getTotalFilas();j++){

				tab_det_spi.setFilaActual(j); 
				enviarCorreoIndividual();

			}
			
		}
	}
	
	public void enviarCorreoIndividual() {
		
		if(pckUtilidades.CConversion.CStr(tab_det_spi.getValor("notificado_sptrd")).equals("true"))
		{
			utilitario.agregarMensajeInfo("No se pudo enviar el correo", "Ya se ha notificado al destinatario anteriormente...");
			return;
		}
		
		if(pckUtilidades.CConversion.CStr(tab_det_spi.getValor("correo_sptrd")).length()<1)
		{
			utilitario.agregarMensajeInfo("No se pudo enviar el correo", "El destinatario no tiene importado/registrado un correo...");
			return;
		}

		String str_mail = pckUtilidades.CConversion.CStr(tab_det_spi.getValor("correo_sptrd"));

		try {
			
			String str_mensaje = tab_correo_envio.getValor("plantilla_cpla");
			
			//System.out.println(" email: " + str_mail);

			//util.EnviaMailInterno(envMail, str_mail, str_asunto, str_mensaje, null);
			
			envMail.setAsunto(str_asunto);
			envMail.setCuerpoHtml(str_mensaje);
			envMail.setPara(str_mail);
			//envMail.setCopia(envMail.getCorreoEnvio());
			
			pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
			
			if(obj.getRespuesta())
			{
				tab_det_spi.modificar(tab_det_spi.getFilaActual());
				tab_det_spi.setValor("notificado_sptrd","true");
				tab_det_spi.guardar();
				guardarPantalla();
				
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + tab_det_spi.getValor("empleado_sptrd") + " email: " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo no enviado a : " + tab_det_spi.getValor("empleado_sptrd") + " email: " + str_mail , " msjError: " + obj.getDescripcion());
			
		} catch (Exception ex) {
			utilitario.agregarMensajeError("Correo no enviado a : " + tab_det_spi.getValor("empleado_sptrd") + " email: " + str_mail , " msjError: " + ex.getMessage());
		}
		
		
	}
	
	public void importar()
	{
		if(tab_cab_spi.getValorSeleccionado()!=null)
		 {
			if(tab_det_spi.isEmpty()){
				 if (sel_calendario.isVisible()){
					 
					 sel_calendario.cerrar();
					 sel_transaccion.setTitle("SELECCIONE LOS PAGOS A GENERAR");
					 sel_transaccion.setSql(ser_Tesoreria.getActualizarConsultaPagos(true, sel_calendario.getFecha1String(), sel_calendario.getFecha2String(), cuenta_transferencia));
					 sel_transaccion.getTab_seleccion().ejecutarSql();
					 sel_transaccion.seleccionarTodas();
					 sel_transaccion.dibujar();
				
				 }
				 else if(sel_transaccion.isVisible()){
					 
					 sel_transaccion.cerrar();
					 String selecionados = sel_transaccion.getSeleccionados();
					 
					 if(selecionados!=null && !selecionados.isEmpty()){
						 
						 //generar_archivo_plano("TRANSFERENCIA","TRANSFERENCIA_",sec_importar.getFecha2String(),sel_transaccion.getTab_seleccion());	
						 
						 llenar_detalle(selecionados,sel_transaccion.getTab_seleccion());
						 
						 dou_total=tab_det_spi.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
						 eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));
						 tab_det_spi.setDibujo(true);	  
						 sumarValores();
						 utilitario.addUpdate("tab_det_spi");
					 }
					 else{
				 		utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			 		 }
					 
				 }
				 else {
					 sel_calendario.dibujar();
					}
			}
			else{
				utilitario.agregarMensajeInfo("No se puede Importar ", "Ya existen  datos");
			}
		 }
		 else
			 utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar (Cabecera)");
		
	}
	
	private void llenar_detalle(String selecionados, Tabla tabla)
	{
		int secuencial=1;
		String p_sri_tipo_moneda_spi=utilitario.getVariable("p_sri_tipo_moneda_spi");
		String p_sri_tipo_pago_spi=utilitario.getVariable("p_sri_tipo_pago_spi");
		String [] seleccionado =selecionados.split(",");
		System.out.println("selecionados: "+selecionados);
		
		 for(int i=0;i<tabla.getTotalFilas();i++){
			 //System.out.println("llenar_detalle reff: "+tabla.getValor(i,"reff"));

			 for(int j=0;j<seleccionado.length;j++){
				 if(seleccionado[j].equals(tabla.getValor(i,"reff")))
				 {
					 //System.out.println("EMPLEADO_SPTRD: "+tabla.getValor(i,"nombre_tepro"));
					 tab_det_spi.insertar();
					 tab_det_spi.setValor("secuencial_sptrd",pckUtilidades.CConversion.CStr(secuencial));
					 tab_det_spi.setValor("COD_ORIGEN_PAGO_SPTRD",p_sri_tipo_pago_spi);///////?????via internet
					 tab_det_spi.setValor("CODIGO_MONEDA_SPTRD",p_sri_tipo_moneda_spi);////////???? moneda dolar
					 tab_det_spi.setValor("MONTO_TRANSFERIDO_SPTRD",tabla.getValor(i,"valor_pago_tecpo"));
					 tab_det_spi.setValor("COD_CONCEPTO_PAGO_SPTRD",tabla.getValor(i,"codigo_tetic"));
					 tab_det_spi.setValor("CONCEPTO_PAGO_SPTRD",tabla.getValor(i,"detalle_tecpo"));
					 tab_det_spi.setValor("COD_BANCO_SPTRD",tabla.getValor(i,"codigo_banco_geins"));
					 tab_det_spi.setValor("NRO_CUENTA_SPTRD",tabla.getValor(i,"numero_cuenta_tepcb"));
					 tab_det_spi.setValor("TIPO_CUENTA_SPTRD",tabla.getValor(i,"codigo_gttcb"));
					 tab_det_spi.setValor("EMPLEADO_SPTRD",tabla.getValor(i,"nombre_tepro"));
					 tab_det_spi.setValor("CEDULA_SPTRD",tabla.getValor(i,"ruc"));		
					 tab_det_spi.setValor("IDE_TECPO",tabla.getValor(i,"ide_tecpo"));	
					 tab_det_spi.setValor("IDE_SPTRA",tab_cab_spi.getValor("IDE_SPTRA"));
					 secuencial++;
				 }
			 }
			 
		}
		 
	}

	public void exportarExcel(){
		if (tab_det_spi.getTotalFilas()>0){
			exportarXLS("spi.xls","1","1");
			System.out.println("Se exporto correctamente el archivo spi nomina.");
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
			//formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

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

			CellView cv=new CellView();
			for (int i = 0; i < tab_det_spi.getTotalFilas(); i++) {
					// NOMBRES DE COLUMNAS
					// DEPARTAMENTO
					jxl.write.Label lab1 = new jxl.write.Label(0, 0, "CEDULA/RUC", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0,cv);

					// CEDULA
					lab1 = new jxl.write.Label(1, 0, "REFERENCIA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1,cv);

					// NOMBRE 
					lab1 = new jxl.write.Label(2, 0, "NOMBRE", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2,cv);

					// INSTITUCION FINANCIERA
					lab1 = new jxl.write.Label(3, 0, "INSTITUCION FINANCIERA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3,cv);

					// CUENTA BENEFICIARIO
					lab1 = new jxl.write.Label(4, 0, "CUENTA BENEFICIARIO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4,cv);		
					
					// TIPOCUENTA
					lab1 = new jxl.write.Label(5, 0, "TIPOCUENTA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(5,cv);

					// VALOR
					lab1 = new jxl.write.Label(6, 0, "VALOR", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(6,cv);

					// CONCEPTO
					lab1 = new jxl.write.Label(7, 0, "CONCEPTO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(7,cv);
					
					// DETALLE
					lab1 = new jxl.write.Label(8, 0, "DETALLE", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(8,cv);	
					
					
						//Cargamos RUC
						jxl.write.Label lab2 = new jxl.write.Label(0, i+1,tab_det_spi.getValor(i, "cedula_sptrd"), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(0,cv);
						//Cargamos REFRECNIA
						jxl.write.Number labnum1 = new jxl.write.Number(1, i+1,pckUtilidades.CConversion.CDbl(tab_det_spi.getValor(i, "secuencial_sptrd")), formato_celda);
						hoja_xls.addCell(labnum1);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(1,cv);

						//Cargamos PROVEEDRO
						String empleado=pckUtilidades.CConversion.CStr(pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_det_spi.getValor(i, "empleado_sptrd")))).toUpperCase();
						lab2 = new jxl.write.Label(2, i+1,(empleado.length()>29? empleado.substring(0, 29): empleado), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(2,cv);
						//Cargamos INSITUCION FINANCIERA
						jxl.write.Number labnum2 = new jxl.write.Number(3, i+1,pckUtilidades.CConversion.CDbl(tab_det_spi.getValor(i, "cod_banco_sptrd")), formato_celda);
						hoja_xls.addCell(labnum2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(3,cv);
						
						//Cargamos CUENTA BENEFICIARIO
						lab2 = new jxl.write.Label(4, i+1, tab_det_spi.getValor(i, "nro_cuenta_sptrd") , formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(4,cv);
						
						//Cargamos tipo cuenta bancaria
						jxl.write.Number labnum4 = new jxl.write.Number(5, i+1, pckUtilidades.CConversion.CDbl(tab_det_spi.getValor(i, "tipo_cuenta_sptrd")), formato_celda);
						hoja_xls.addCell(labnum4);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(5,cv);
						
						//Cargamos VALOR PAGAR
						jxl.write.Number labnum = new jxl.write.Number(6, i+1,pckUtilidades.CConversion.CDbl(tab_det_spi.getValor(i, "monto_transferido_sptrd")), formato_celda);
						hoja_xls.addCell(labnum);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(6,cv);
						
						//Cargamos CODIGO CONCEPTO
						jxl.write.Number labnum5 = new jxl.write.Number(7, i+1,pckUtilidades.CConversion.CDbl(tab_det_spi.getValor(i, "cod_concepto_pago_sptrd")), formato_celda);
						hoja_xls.addCell(labnum5);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(7,cv);
						
						//Cargamos CONCEPTO
						String detalle=pckUtilidades.CConversion.CStr(pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_det_spi.getValor(i, "concepto_pago_sptrd")))).toUpperCase();
						lab2 = new jxl.write.Label(8, i+1,(detalle.length()>30? detalle.substring(0, 30): detalle), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(8,cv);
					
					
				int_fila=int_fila+1;
			}

			archivo_xls.write(); 
			archivo_xls.close(); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom); 
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
			utilitario.agregarMensajeError("Error no se genero el XLS :", e.getMessage());
		} 
	}


	private void sumarValores(){
		dou_total=tab_det_spi.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
		eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));
		utilitario.addUpdate("eti_totales");
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cab_spi.insertar();		
		tab_cab_spi.setValor("FECHA_SPTRA", utilitario.getFechaHoraActual());
		tab_cab_spi.setValor("ide_geani", com_anio.getValue()+"");
		//tab_cab_spi.setValor("SECUECIAL_SPI_SPTRA", String.valueOf("0"));

		sumarValores();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_cab_spi.guardar()){
			tab_det_spi.guardar();
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cab_spi.eliminar();
		sumarValores();
		
	}
	
	public void eliminarDetalles() {
		if(tab_cab_spi.getValorSeleccionado()!=null){
			String str_mensaje=utilitario.getConexion().ejecutarSql("DELETE FROM SPI_TRANSFERENCIAS_DETALLE WHERE IDE_SPTRA="+tab_cab_spi.getValorSeleccionado());
			if(	str_mensaje.isEmpty()){
				utilitario.addUpdate("tab_det_spi");
				utilitario.agregarMensaje("Se Guardo Correctamente", "Se eliminaron todos los detalles");
				tab_det_spi.limpiar();
			}
			else{
				utilitario.agregarMensajeError("No se pudo eliminar los detalles", str_mensaje);
			}		
		}
		sumarValores();

	}

	public void seleccionarTabla1(SelectEvent evt){
		tab_cab_spi.seleccionarFila(evt);
		sumarValores();
	}
	public void seleccionarTabla1(AjaxBehaviorEvent evt){
		tab_cab_spi.seleccionarFila(evt);
		sumarValores();
	}

	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}

	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}

	public Tabla getTab_cab_spi() {
		return tab_cab_spi;
	}

	public void setTab_cab_spi(Tabla tab_cab_spi) {
		this.tab_cab_spi = tab_cab_spi;
	}

	public Tabla getTab_det_spi() {
		return tab_det_spi;
	}

	public void setTab_det_spi(Tabla tab_det_spi) {
		this.tab_det_spi = tab_det_spi;
	}

	public SeleccionTabla getSel_transaccion() {
		return sel_transaccion;
	}

	public void setSel_transaccion(SeleccionTabla sel_transaccion) {
		this.sel_transaccion = sel_transaccion;
	}

}
