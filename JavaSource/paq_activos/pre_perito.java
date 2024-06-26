package paq_activos;

import javax.ejb.EJB;

import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.event.FileUploadEvent;

import paq_activos.ejb.ServicioActivos;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;

public class pre_perito extends Pantalla{

	private Tabla tab_perito=new Tabla();
	private Combo com_anio=new Combo();
	private Upload upl_archivo=new Upload();
	private Confirmar con_guardar=new Confirmar();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);
	
	public pre_perito() {
		
		bar_botones.limpiar();
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_pagar=new Boton();
		bot_pagar.setIcon("ui-icon-circle-arrow-s");
		bot_pagar.setValue("IMPORTAR");
		bot_pagar.setMetodo("importar");
		bar_botones.agregarBoton(bot_pagar);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setValue("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarComponente(bot_limpiar);
		
		Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
		
    	upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");
	
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Cargar archivo");
		upl_archivo.setCancelLabel("Cancelar Seleccion");
    	gri_formulario.setFooter(upl_archivo);
    	
    	con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REALIZAR LA IMPORTACION");
		con_guardar.setTitle("CONFIRMACION DE IMPORTACION");
		agregarComponente(con_guardar);
		
		tab_perito.setId("tab_perito");  
		tab_perito.setTabla("afi_perito", "ide_afper", 1);	
		tab_perito.getColumna("ide_geani").setVisible(false);
		//tab_perito.getColumna("ide_afact").setCombo(ser_activos.getDescripcionActivo());
		//tab_perito.getColumna("ide_afact").setAutoCompletar();
		tab_perito.getColumna("ide_afact").setLectura(true);
		tab_perito.getColumna("ide_afact").setLongitud(90);
		tab_perito.getColumna("cantidad_afper").setLectura(true);
		tab_perito.getColumna("nombre_afper").setLectura(true);
		tab_perito.getColumna("nombre_afper").setLongitud(40);
		tab_perito.getColumna("item_pres_afper").setLectura(true);
		tab_perito.getColumna("item_pres_afper").setLongitud(30);
		tab_perito.getColumna("tipo_afper").setLectura(true);
		tab_perito.getColumna("tipo_afper").setLongitud(20);
		tab_perito.getColumna("descripcion_afper").setLectura(true);
		tab_perito.getColumna("estado_afper").setLectura(true);
		tab_perito.getColumna("estado_afper").setLongitud(20);
		tab_perito.getColumna("fecha_reavaluo_afper").setLectura(true);
		tab_perito.getColumna("fecha_alta_afper").setLectura(true);
		tab_perito.getColumna("edad_afper").setLectura(true);
		tab_perito.getColumna("vida_util_afper").setLectura(true);
		tab_perito.getColumna("vida_util_restante_afper").setLectura(true);
		tab_perito.getColumna("valor_compra_afper").setLectura(true);
		tab_perito.getColumna("factor_afper").setLectura(true);
		tab_perito.getColumna("valor_revaluo_comercial_afper").setLectura(true);
		tab_perito.getColumna("valor_realizacion_afper").setLectura(true);
		tab_perito.getColumna("valor_residual_afper").setLectura(true);
		tab_perito.getColumna("activo_afper").setValorDefecto("true");
		tab_perito.setCondicion("ide_geani=-1"); 
		tab_perito.setColumnaSuma("valor_compra_afper,valor_revaluo_comercial_afper,valor_realizacion_afper,valor_residual_afper"); 
		//tab_perito.setLectura(true);
		tab_perito.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_perito);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
        pat_panel.getMenuTabla().getItem_eliminar().setValue("Eliminar Todos");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodo("eliminarDetalles");
		//tab_perito.setLectura(false);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(gri_formulario,pat_panel,"20%", "h");

        agregarComponente(div_division);
		

		
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_perito.setCondicion("ide_geani="+com_anio.getValue());
			tab_perito.ejecutarSql();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void limpiar() {
		tab_perito.limpiar();	
		utilitario.addUpdate("tab_tabla");
	}
	
	public void validarArchivo(FileUploadEvent evt)
	{	
		int registrosLeidos=0;

		try {
			
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeInfo("No se puede cargar el Archivo", "Favor Seleccione un Año");
				return;
			}
			
			if(tab_perito.getTotalFilas()>0){
				utilitario.agregarMensajeError("Existen registros cargados", "Favor limpie o borre los que se han cargado anteriormente...");
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
			int codigo=0;
	
			for (int i = 1; i < int_fin; i++)
			//for (int i = 1; i < 5; i++)
			{
				codigo=pckUtilidades.CConversion.CInt(pckUtilidades.CConversion.CStr(hoja.getCell(0, i).getContents()));
				if(codigo>0)
				{
					registrosLeidos++;
					tab_perito.insertar();
					tab_perito.setValor("ide_geani",com_anio.getValue()+"");
					tab_perito.setValor("ide_afact",pckUtilidades.CConversion.CStr(hoja.getCell(0, i).getContents()));
					tab_perito.setValor("cantidad_afper",pckUtilidades.CConversion.CStr(hoja.getCell(5, i).getContents()));
					tab_perito.setValor("nombre_afper",pckUtilidades.CConversion.CStr(hoja.getCell(1, i).getContents()));
					tab_perito.setValor("item_pres_afper",pckUtilidades.CConversion.CStr(hoja.getCell(2, i).getContents()));
					tab_perito.setValor("tipo_afper",pckUtilidades.CConversion.CStr(hoja.getCell(3, i).getContents()));
					tab_perito.setValor("descripcion_afper",pckUtilidades.CConversion.CStr(hoja.getCell(4, i).getContents()));
					tab_perito.setValor("estado_afper",pckUtilidades.CConversion.CStr(hoja.getCell(6, i).getContents()));
					tab_perito.setValor("fecha_reavaluo_afper",pckUtilidades.CConversion.CStr(hoja.getCell(7, i).getContents()));
					tab_perito.setValor("fecha_alta_afper",pckUtilidades.CConversion.CStr(hoja.getCell(8, i).getContents()));
					tab_perito.setValor("edad_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(9, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("vida_util_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(10, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("vida_util_restante_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(11, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("valor_compra_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(12, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("factor_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(13, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("valor_revaluo_comercial_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(14, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("valor_realizacion_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(15, i).getContents().replace(".", "").replace(",", "."))+"");
					tab_perito.setValor("valor_residual_afper",pckUtilidades.CConversion.CDbl(hoja.getCell(16, i).getContents().replace(".", "").replace(",", "."))+"");
				}
				
			}
			tab_perito.sumarColumnas();
			//tab_perito.setDibujo(true);
			utilitario.addUpdate("tab_perito");
			
			if(registrosLeidos != (int_fin-2) )
			{
				utilitario.agregarNotificacionInfo("Validación", registrosLeidos + " Registros validados de "+(int_fin)+" en total del archivo de Excel Subido... ERROR... NO SE LEYERON TODOS LOS REGISTROS VUELVA A INTENTARLO NUEVAMENTE... ");
			}
			else
			{
				utilitario.agregarNotificacionInfo("Validación", registrosLeidos + " Registros validados de "+(int_fin)+" en total del archivo de Excel Subido...");
			}
				 

		} catch (Exception e) {
			// TODO: handle exception
			utilitario.agregarMensajeError("No se pudo conciliar el archivo", "Debido a una inconsistencia no se pudo culminar exitosamente con la conciliacion del archivo");
			System.out.println("Error carga informacion perito: "+e);
		}	
		System.out.println("No Reg Validados: "+registrosLeidos);
	}

	
	public void importar()
	{
		
		if (!con_guardar.isVisible())
		{
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeInfo("No se puede cargar el Archivo", "Favor Seleccione un Año");
				return;
			}
			
			//con_guardar.setMessage("ESTA SEGURO DE REALIZAR LA CONCILIACION");
			//con_guardar.setTitle("CONFIRMACION DE PROCESO BATCH");
			con_guardar.getBot_aceptar().setMetodo("importar");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}
		else
		{
			con_guardar.cerrar();
			
			if(!tab_perito.isFilaInsertada()){
				utilitario.agregarMensajeError("No existen registros nuevos", "Carge un Excel");
				return;
			}
			
			tab_perito.guardar();
	        guardarPantalla();
	        tab_perito.setCondicion("ide_geani="+com_anio.getValue());
			tab_perito.ejecutarSql();
	        utilitario.addUpdate("tab_perito");
	        
		}
	}
	
	public void eliminarDetalles() {
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede cargar el Archivo", "Favor Seleccione un Año");
			return;
		}
		
		String str_mensaje=utilitario.getConexion().ejecutarSql("DELETE FROM afi_perito WHERE ide_geani="+com_anio.getValue());
		if(	str_mensaje.isEmpty()){
			utilitario.addUpdate("tab_perito");
			utilitario.agregarMensaje("Se Guardo Correctamente", "Se eliminaron todos los detalles");
			tab_perito.limpiar();
		}
		else{
			utilitario.agregarMensajeError("No se pudo eliminar los detalles", str_mensaje);
		}		

	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//tab_perito.insertar();	
		//guardarPantalla();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		//tab_perito.guardar();
		//guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		//tab_perito.eliminar();
		
	}
	public Tabla getTab_perito() {
		return tab_perito;
	}
	public void setTab_perito(Tabla tab_perito) {
		this.tab_perito = tab_perito;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Upload getUpl_archivo() {
		return upl_archivo;
	}

	public void setUpl_archivo(Upload upl_archivo) {
		this.upl_archivo = upl_archivo;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	
	
	
	
}
