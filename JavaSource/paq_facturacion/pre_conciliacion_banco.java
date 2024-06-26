/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_facturacion;


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
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;

/**
 *
 * @author AWBECERRA
 */
public class pre_conciliacion_banco extends Pantalla {//banco pichincha
	
    private Tabla tab_tabla = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Upload upl_archivo=new Upload();
	private Texto txt_documento_banco = new Texto();
	private Confirmar con_guardar=new Confirmar();

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

    public pre_conciliacion_banco() { 
    	
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
    	gri_formulario.setColumns(4);
    	
    	gri_formulario.getChildren().add(new Etiqueta("Fecha Archivo :"));
		cal_fecha_inicial.setFechaActual();
    	gri_formulario.getChildren().add(cal_fecha_inicial);

    	
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

    	
    	tab_tabla.setId("tab_tabla");
    	tab_tabla.setTabla("fac_auditoria_conciliacion", "ide_faauc", 1);
    	tab_tabla.setCondicion("ide_faauc=-1");
    	tab_tabla.getColumna("activo_faauc").setValorDefecto("true");
    	tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("IDE_USUA"));
    	tab_tabla.getColumna("fecha_hora_concilia_faauc").setValorDefecto(utilitario.getFechaActual());
    	tab_tabla.getColumna("ide_usua").setVisible(false);
    	tab_tabla.setColumnaSuma("valor_archivo_faauc,valor_deuda_faauc,interes_incluido_deuda_faauc,valor_vuelto_faauc");

    	//tab_tabla.setLectura(true);
    	tab_tabla.dibujar();
    	
    	con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REALIZAR EL COBRO");
		con_guardar.setTitle("CONFIRMACION DE COBRO");

		agregarComponente(con_guardar);
    	
        PanelTabla pat_panel = new PanelTabla();
        //pat_panel.setHeader(gri_formulario);
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(gri_formulario,pat_panel,"20%", "h");
        agregarComponente(div_division);
        
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
	
	public void validarArchivo(FileUploadEvent evt)
	{	
		int conciliados=0;
		int registrosLeidos=0;

		try {
			
			if(txt_documento_banco.getValue().equals("")){
				utilitario.agregarMensajeInfo("No se puede conciliar el Archivo", "Favor Ingrese el numero del documento de conciliacion");
				return;
			}
			
			Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
			Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
			if (hoja == null) {
				utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
				return;
			}
			int int_fin = hoja.getRows();
			System.out.println("getRows: "+int_fin);
			upl_archivo.setNombreReal(evt.getFile().getFileName());
			String contrapartida="";
			TablaGenerica clienteDeuda = utilitario.consultar(ser_tesoreria.getSqlDeudaClientesArchivo("1"));
	
			for (int i = 12; i < int_fin; i++) 
			{
				contrapartida=pckUtilidades.CConversion.CStr(hoja.getCell(4, i).getContents());
				if(contrapartida.length()>0)
				{
					registrosLeidos++;
					tab_tabla.insertar();
					//tab_tabla.setValor("documento_conciliado_faauc",pckUtilidades.CConversion.CStr(txt_documento_banco.getValue()));
					tab_tabla.setValor("fecha_pago_archivo_faauc",cal_fecha_inicial.getFecha());
					tab_tabla.setValor("contrapartida_faauc",contrapartida);	
					
					String[] array = obtenerDeuda(clienteDeuda,tab_tabla.getValor("contrapartida_faauc")).split(",");
					tab_tabla.setValor("ruc_cliente_faauc",pckUtilidades.CConversion.CStr(hoja.getCell(3, i).getContents()).replaceAll("R-", ""));
					tab_tabla.setValor("cliente_faauc",pckUtilidades.CConversion.CStr(hoja.getCell(5, i).getContents()));
					tab_tabla.setValor("valor_archivo_faauc",pckUtilidades.CConversion.CDbl_2(pckUtilidades.CConversion.CStr(hoja.getCell(7, i).getContents()).replace(",", "."))+"");
					tab_tabla.setValor("valor_deuda_faauc",pckUtilidades.CConversion.CDbl_2(array[0])+"");
					tab_tabla.setValor("interes_incluido_deuda_faauc",pckUtilidades.CConversion.CDbl_2(array[1])+"");
					tab_tabla.setValor("referencia_faauc",pckUtilidades.CConversion.CStr(hoja.getCell(12, i).getContents()));
					tab_tabla.setValor("documento_conciliado_faauc",pckUtilidades.CConversion.CStr(hoja.getCell(16, i).getContents()));

					tab_tabla.setValor("conciliado_faauc","false");
					
					if(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor("valor_deuda_faauc"))>0)
					{
						tab_tabla.setValor("validado_faauc","true");
						conciliados++;
					}
					else
						tab_tabla.setValor("validado_faauc","false");
				}
				
			}
		
			utilitario.addUpdate("tab_tabla");
			if(registrosLeidos != (int_fin-15) )
			{
				utilitario.agregarNotificacionInfo("Validación", conciliados + " Registros validados de "+(int_fin-15)+" en total del archivo de Excel Subido... ERROR... NO SE LEYERON TODOS LOS COBROS VUELVA A INTENTARLO NUEVAMENTE... Registros leidos: "+registrosLeidos);
			}
			else
			{
				utilitario.agregarNotificacionInfo("Validación", conciliados + " Registros validados de "+(int_fin-15)+" en total del archivo de Excel Subido...");
			}
			tab_tabla.sumarColumnas();
	
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.agregarMensajeError("No se pudo conciliar el archivo", "Debido a una inconsistencia no se pudo culminar exitosamente con la conciliacion del archivo");
			System.out.println("Error conciliacion Bancaria: "+e);
		}	
		System.out.println("No Reg Validados: "+conciliados);
	}
	
	public String obtenerDeuda(TablaGenerica clienteDeuda, String contrapartida)
	{
		StringBuilder valores=new StringBuilder("0,0");
		
		for(int i=0;i<clienteDeuda.getTotalFilas();i++)
		{
			if(clienteDeuda.getValor(i,"doc_identidad").equals(contrapartida))
			{
				valores=new StringBuilder();
				valores.append(pckUtilidades.CConversion.CDbl_2(clienteDeuda.getValor(i,"valor"))+pckUtilidades.CConversion.CDbl_2(clienteDeuda.getValor(i,"interes")));
				valores.append(",");
				valores.append(pckUtilidades.CConversion.CDbl_2(clienteDeuda.getValor(i,"interes")));
				break;
			}
		}
		System.out.println("obtenerDeuda contrapartida: "+contrapartida+" valores: "+valores.toString());
		return valores.toString();
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
				if(tab_tabla.getValor(i,"validado_faauc").equals("true"))
				{
					tab_tabla.modificar(i);
					try
                    {
						Object[] resp = pckRecaudacion.Recaudacion.grabarPagoProdBank(pckUtilidades.CConversion.CStr(tab_tabla.getValor(i,"contrapartida_faauc")),"","",
								pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor(i,"valor_archivo_faauc")), 4, 1, "Banco Pichincha", 5, true);
						
						if(resp!=null)
						{
							if(pckUtilidades.CConversion.CInt(resp[1])==1 || pckUtilidades.CConversion.CInt(resp[1])==7)
							{
								tab_tabla.setValor(i,"observacion_faauc",pckUtilidades.CConversion.CStr(resp[2]));
								tab_tabla.setValor(i,"valor_vuelto_faauc",pckUtilidades.CConversion.CStr(resp[3]));
								tab_tabla.setValor(i,"cod_autorizacion_faauc",pckUtilidades.CConversion.CStr(resp[0]));
								tab_tabla.setValor(i,"conciliado_faauc","true");
								exitos++;
							}
						}
                    }catch(Exception ex)
                    {
                    	System.out.println("Error al conciliar: "+ex.getMessage());
                    	tab_tabla.setValor(i,"activo_faauc","false");
                    }
				}
				else
					tab_tabla.setValor(i,"activo_faauc","false");
			}
			
			tab_tabla.guardar();
	        guardarPantalla();
	        tab_tabla.setCondicion("fecha_pago_archivo_faauc='"+cal_fecha_inicial.getFecha()+"' and documento_conciliado_faauc like '"+txt_documento_banco.getValue()+"'");
	        tab_tabla.ejecutarSql();
	        utilitario.addUpdate("tab_tabla");
	        
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
        tab_tabla.setCondicion("fecha_pago_archivo_faauc='"+cal_fecha_inicial.getFecha()+"'");
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


}
