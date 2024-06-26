package paq_tesoreria;

import java.io.File;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_anticipos.ejb.ServicioAnticipo;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;


public class pre_cierre_caja extends Pantalla{
	
	private Tabla tab_cierre = new Tabla();
	private Tabla tab_cobro_facturas=new Tabla();
	private Tabla tab_cobro_ndebitos=new Tabla();
	private Tabla tab_cobro_contratos=new Tabla();
	private Tabla tab_cierres_sucursales=new Tabla();
	private Tabla tab_billetes=new Tabla();
	private Tabla tab_monedas=new Tabla();
	private Tabla tab_cheques=new Tabla();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();

	private Combo com_anio=new Combo();
    public static String par_modulosec_cierre;
    private String ide_empleado="";
    private String ide_caja="";
	
    private Etiqueta eti_billetes = new Etiqueta();
    private Etiqueta eti_monedas = new Etiqueta();
    private Etiqueta eti_cheques = new Etiqueta();
    
    
    ///
    WritableSheet hoja_xls;
    WritableCellFormat formato_celda_titulo;
    WritableCellFormat formato_celda;
    WritableCellFormat formato_celda_sub_titulo;
    double valor_tefec=0;
	double valor_tchq=0;
	double dou_total_billetes=0;
	double dou_total_monedas=0;
	double dou_total_cheques=0;
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina ) utilitario.instanciarEJB(ServicioNomina.class);	

	public pre_cierre_caja(){
		
		par_modulosec_cierre=utilitario.getVariable("p_modulo_secuencial_cierre_caja");
		ide_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		if(ide_empleado==null ||ide_empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro");
			return;
		}
		
		ide_caja=ser_tesoreria.obtenerCaja();
		
		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede cerrar la caja", "No esta autorizado o no posee una caja, favor contacte al administrador.");
			return;
		}

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_cierre.setId("tab_cierre");
		tab_cierre.setHeader("CIERRE DE CAJA");
		tab_cierre.setTabla("tes_cierre_caja", "ide_tecca", 1);
		tab_cierre.getColumna("fecha_tecca").setValorDefecto(utilitario.getFechaActual());
		tab_cierre.getColumna("fecha_deposito_tecca").setValorDefecto(utilitario.getFechaActual());
		tab_cierre.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_cierre.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_cierre.getColumna("ide_gtemp").setLectura(true);
		tab_cierre.getColumna("ide_gtemp").setAutoCompletar();
		tab_cierre.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_cierre.getColumna("ide_tecaj").setCombo("tes_caja","ide_tecaj","detalle_tecaj","");
		tab_cierre.getColumna("ide_tecaj").setLectura(true);
		tab_cierre.getColumna("ide_tecaj").setAutoCompletar();
		tab_cierre.getColumna("valor_efectivo_tecca").setValorDefecto("0.00");
		tab_cierre.getColumna("valor_efectivo_tecca").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_cierre.getColumna("valor_efectivo_tecca").setEtiqueta();
		tab_cierre.getColumna("valor_tecca").setValorDefecto("0.00");
		tab_cierre.getColumna("valor_tecca").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_cierre.getColumna("valor_tecca").setEtiqueta();
		tab_cierre.getColumna("activo_tecca").setValorDefecto("true");
		tab_cierre.setCondicion("ide_geani=-1"); 
		tab_cierre.setTipoFormulario(true);
		tab_cierre.getGrid().setColumns(4);
		tab_cierre.agregarRelacion(tab_billetes);
		tab_cierre.agregarRelacion(tab_monedas);
		tab_cierre.agregarRelacion(tab_cheques);
		tab_cierre.dibujar();
		PanelTabla pat_convenio=new PanelTabla();
		pat_convenio.setPanelTabla(tab_cierre);
		
		tab_cobro_facturas.setId("tab_cobro_facturas");
		tab_cobro_facturas.setIdCompleto("tab_tabulador:tab_cobro_facturas");
		tab_cobro_facturas.setHeader("DETALLE FACTURAS");
		tab_cobro_facturas.setSql(ser_tesoreria.getCierreCajaDet("-1","","","0"));
		tab_cobro_facturas.getColumna("documento").setLongitud(15);
		tab_cobro_facturas.getColumna("forma_cobro").setLongitud(50);
		tab_cobro_facturas.getColumna("secuencial").setLongitud(30);
		tab_cobro_facturas.getColumna("ide_tecca").setVisible(false);
		tab_cobro_facturas.setColumnaSuma("cobro"); 
		tab_cobro_facturas.setRows(10);
		tab_cobro_facturas.setLectura(true);
		tab_cobro_facturas.dibujar();
		PanelTabla pat_det_convenio=new PanelTabla();
		pat_det_convenio.setPanelTabla(tab_cobro_facturas);
		
		tab_cobro_ndebitos.setId("tab_cobro_ndebitos");
		tab_cobro_ndebitos.setIdCompleto("tab_tabulador:tab_cobro_ndebitos");
		tab_cobro_ndebitos.setHeader("DETALLE NOTAS DE DEBITO");
		tab_cobro_ndebitos.setSql(ser_tesoreria.getCierreCajaDet("-1","","","0"));
		tab_cobro_ndebitos.getColumna("documento").setLongitud(17);
		tab_cobro_ndebitos.getColumna("forma_cobro").setLongitud(50);
		tab_cobro_ndebitos.getColumna("secuencial").setLongitud(30);
		tab_cobro_ndebitos.getColumna("ide_tecca").setVisible(false);
		tab_cobro_ndebitos.setColumnaSuma("cobro"); 
		tab_cobro_ndebitos.setRows(10);
		tab_cobro_ndebitos.setLectura(true);
		tab_cobro_ndebitos.dibujar();
		PanelTabla pat_convenio_amort=new PanelTabla();
		pat_convenio_amort.setPanelTabla(tab_cobro_ndebitos);
		
		tab_cobro_contratos.setId("tab_cobro_contratos");
		tab_cobro_contratos.setIdCompleto("tab_tabulador:tab_cobro_contratos");
		tab_cobro_contratos.setHeader("DETALLE DE CONTRATOS");
		tab_cobro_contratos.setSql(ser_tesoreria.getCierreCajaDet("-1","","","0"));
		tab_cobro_contratos.getColumna("documento").setLongitud(15);
		tab_cobro_contratos.getColumna("forma_cobro").setLongitud(50);
		tab_cobro_contratos.getColumna("secuencial").setLongitud(30);
		tab_cobro_contratos.getColumna("ide_tecca").setVisible(false);
		tab_cobro_contratos.setColumnaSuma("cobro"); 
		tab_cobro_contratos.setRows(10);
		tab_cobro_contratos.setLectura(true);
		tab_cobro_contratos.dibujar();
		PanelTabla pat_cobro_contratos=new PanelTabla();
		pat_cobro_contratos.setPanelTabla(tab_cobro_contratos);
		
		tab_tabulador.agregarTab("FACTURAS", pat_det_convenio);
		tab_tabulador.agregarTab("NOTAS DEBITO", pat_convenio_amort);
		tab_tabulador.agregarTab("CONTRATOS", pat_cobro_contratos);
		
		
		tab_billetes.setId("tab_billetes");
		tab_billetes.setHeader("BILLETES");
		tab_billetes.setTabla("tes_efectivo", "ide_tefec", 2);
		tab_billetes.getColumna("ide_geins").setVisible(false);
		tab_billetes.getColumna("valor_tefec").setVisible(false);
		tab_billetes.getColumna("ide_ttefe").setCombo("tes_tipo_efectivo","ide_ttefe","detalle_ttefe","");
		tab_billetes.getColumna("ide_ttefe").setAutoCompletar();
		tab_billetes.getColumna("ide_ttefe").setLongitud_control(12);
		tab_billetes.getColumna("ide_ttefe").setLectura(true);
		tab_billetes.getColumna("cantidad_tefec").setValorDefecto("0");
		tab_billetes.getColumna("cantidad_tefec").setEstilo("width: 70px;");
		tab_billetes.getColumna("tipo_tefec").setVisible(false);
		tab_billetes.getColumna("tipo_tefec").setValorDefecto("1");
		tab_billetes.getColumna("activo_tefec").setVisible(false);
		tab_billetes.getColumna("activo_tefec").setValorDefecto("true");
		tab_billetes.getColumna("cantidad_tefec").setMetodoChange("re_calcular_b");
		tab_billetes.setCondicion("tipo_tefec=1 and ide_tecca=-1"); 
		tab_billetes.dibujar();
		
		eti_billetes.setId("eti_billetes");
		eti_billetes.setStyle("font-size:18px;color:red;widht:80%");
		eti_billetes.setValue("TOTAL : 0.00");
		
		PanelTabla pat_billetes=new PanelTabla();
		pat_billetes.setPanelTabla(tab_billetes);
		pat_billetes.setFooter(eti_billetes);
		
		tab_monedas.setId("tab_monedas");
		tab_monedas.setHeader("MONEDAS");
		tab_monedas.setTabla("tes_efectivo", "ide_tefec", 3);
		tab_monedas.getColumna("ide_geins").setVisible(false);
		tab_monedas.getColumna("valor_tefec").setVisible(false);
		tab_monedas.getColumna("ide_ttefe").setCombo("tes_tipo_efectivo","ide_ttefe","detalle_ttefe","");
		tab_monedas.getColumna("ide_ttefe").setAutoCompletar();
		tab_monedas.getColumna("ide_ttefe").setLongitud_control(12);
		tab_monedas.getColumna("ide_ttefe").setLectura(true);
		tab_monedas.getColumna("cantidad_tefec").setValorDefecto("0");
		tab_monedas.getColumna("cantidad_tefec").setEstilo("width: 70px;");
		tab_monedas.getColumna("tipo_tefec").setVisible(false);
		tab_monedas.getColumna("tipo_tefec").setValorDefecto("2");
		tab_monedas.getColumna("activo_tefec").setVisible(false);
		tab_monedas.getColumna("activo_tefec").setValorDefecto("true");
		tab_monedas.getColumna("cantidad_tefec").setMetodoChange("re_calcular_m");
		tab_monedas.setCondicion("tipo_tefec=2 and ide_tecca=-1"); 
		tab_monedas.dibujar();
		
		eti_monedas.setId("eti_monedas");
		eti_monedas.setStyle("font-size:18px;color:red;widht:80%");
		eti_monedas.setValue("TOTAL : 0.00");
		
		PanelTabla pat_monedas=new PanelTabla();
		pat_monedas.setPanelTabla(tab_monedas);
		pat_monedas.setFooter(eti_monedas);
		
		tab_cheques.setId("tab_cheques");
		tab_cheques.setHeader("CHEQUES");
		tab_cheques.setTabla("tes_efectivo", "ide_tefec", 4);
		tab_cheques.getColumna("ide_ttefe").setVisible(false);
		tab_cheques.getColumna("cantidad_tefec").setVisible(false);
		tab_cheques.getColumna("ide_geins").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL and IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));
		tab_cheques.getColumna("valor_tefec").setValorDefecto("0");
		tab_cheques.getColumna("valor_tefec").setEstilo("width: 70px;");
		tab_cheques.getColumna("tipo_tefec").setVisible(false);
		tab_cheques.getColumna("tipo_tefec").setValorDefecto("3");
		tab_cheques.getColumna("activo_tefec").setVisible(false);
		tab_cheques.getColumna("activo_tefec").setValorDefecto("true");
		tab_cheques.getColumna("valor_tefec").setMetodoChange("re_calcular_c");
		tab_cheques.setCondicion("tipo_tefec=3 and ide_tecca=-1"); 
		tab_cheques.dibujar();
		
		eti_cheques.setId("eti_cheques");
		eti_cheques.setStyle("font-size:18px;color:red;widht:80%");
		eti_cheques.setValue("TOTAL : 0.00");
		
		PanelTabla pat_cheques=new PanelTabla();
		pat_cheques.setPanelTabla(tab_cheques);
		pat_cheques.setFooter(eti_cheques);
		
		Division div_divi1=new Division();
		div_divi1.dividir2(pat_billetes, pat_monedas, "50%", "V");
		
		Division div_divi2=new Division();
		div_divi2.dividir2(div_divi1, pat_cheques, "60%", "H");
		
		Division div_divi3=new Division();
		div_divi3.dividir2(tab_tabulador, div_divi2, "60%", "V");
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_convenio, div_divi3, "40%", "H");
		
		agregarComponente(div_divi);
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-arrowthickstop-1-s");
		bot_actualizar.setValue("Importar datos");
		bot_actualizar.setMetodo("generarCierre");
		bar_botones.agregarBoton(bot_actualizar);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("Exportar Cierre");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
  		
		inicializaCalendario();
		
	}
	
	public void seleccionaElAnio(){
		if(com_anio.getValue()!=null){
			tab_cierre.setCondicion("ide_geani="+com_anio.getValue());
			tab_cierre.ejecutarSql();
			actualizaDetalles();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
			tab_cierre.setCondicion("ide_geani=-1");
			tab_cierre.ejecutarSql();
			actualizaDetalles();
		}
	}
	
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
//		sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
//		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFechaActual();
		sel_calendario.getBot_aceptar().setMetodo("generarCierre");
		agregarComponente(sel_calendario);
	}
	
	public void re_calcular_b(AjaxBehaviorEvent evt) {
		tab_billetes.modificar(evt); // Siempre es la primera linea
		tab_billetes.guardar();
		guardarPantalla();
		actualizaDetalles();
	}
	
	public void re_calcular_m(AjaxBehaviorEvent evt) {
		tab_monedas.modificar(evt); // Siempre es la primera linea
		tab_monedas.guardar();
		guardarPantalla();
		actualizaDetalles();
	}
	
	public void re_calcular_c(AjaxBehaviorEvent evt) {
		tab_cheques.modificar(evt); // Siempre es la primera linea
		tab_cheques.guardar();
		guardarPantalla();
		actualizaDetalles();
	}
	
	public void actualizaDetalles()
	{
		String ide_tecca="-1";
		
		if(tab_cierre.getValor("ide_tecca")!=null)
		{
			ide_tecca=tab_cierre.getValor("ide_tecca");
		}
		else
			return;
		
		tab_cobro_facturas.setSql(ser_tesoreria.getCierreCajaDet("1","","",ide_tecca));
		tab_cobro_facturas.ejecutarSql();		
		
		tab_cobro_ndebitos.setSql(ser_tesoreria.getCierreCajaDet("2","","",ide_tecca));
		tab_cobro_ndebitos.ejecutarSql();	
		
		tab_cobro_contratos.setSql(ser_tesoreria.getCierreCajaDet("3","","",ide_tecca));
		tab_cobro_contratos.ejecutarSql();	
		
		tab_billetes.setCondicion("tipo_tefec=1 and ide_tecca="+ide_tecca);
		tab_billetes.ejecutarSql();
		
		tab_monedas.setCondicion("tipo_tefec=2 and ide_tecca="+ide_tecca);
		tab_monedas.ejecutarSql();
		
		tab_cheques.setCondicion("tipo_tefec=3 and ide_tecca="+ide_tecca);
		tab_cheques.ejecutarSql();
		
		double valorFacturas=tab_cobro_facturas.getSumaColumna("cobro");
		double valorND=tab_cobro_ndebitos.getSumaColumna("cobro");
		double valorContratos=tab_cobro_contratos.getSumaColumna("cobro");
		boolean guardar=false;
		String valor_efectivo="0";
		String valor="0";
		dou_total_billetes=0;
		dou_total_monedas=0;
		dou_total_cheques=0;
		
		TablaGenerica tab_efectivo = utilitario.consultar(" select sum(coalesce(cantidad_tefec,0) * coalesce(valor_ttefe,0)) as valor, sum(coalesce(valor_tefec,0)) as valor_chqs, tipo_tefec from tes_efectivo te "
														+" left join tes_tipo_efectivo tte on tte.ide_ttefe=te.ide_ttefe "
														+" where ide_tecca="+ide_tecca
														+" group by tipo_tefec");
		
		for (int i = 0; i < tab_efectivo.getTotalFilas(); i++) 
		{
			if(pckUtilidades.CConversion.CInt(tab_efectivo.getValor(i, "tipo_tefec"))==1)
				dou_total_billetes+=pckUtilidades.CConversion.CDbl_2(tab_efectivo.getValor(i, "valor"));
			
			if(pckUtilidades.CConversion.CInt(tab_efectivo.getValor(i, "tipo_tefec"))==2)
				dou_total_monedas+=pckUtilidades.CConversion.CDbl_2(tab_efectivo.getValor(i, "valor"));	
			
			if(pckUtilidades.CConversion.CInt(tab_efectivo.getValor(i, "tipo_tefec"))==3)
				dou_total_cheques+=pckUtilidades.CConversion.CDbl_2(tab_efectivo.getValor(i, "valor_chqs"));	
		}
		
		valor_efectivo=utilitario.getFormatoNumero((dou_total_billetes+dou_total_monedas+dou_total_cheques),2);
		valor=utilitario.getFormatoNumero((valorFacturas+valorND+valorContratos),2);
		
		if(pckUtilidades.CConversion.CDbl_2(tab_cierre.getValor("valor_efectivo_tecca")) != pckUtilidades.CConversion.CDbl_2(valor_efectivo) 
				|| pckUtilidades.CConversion.CDbl_2(tab_cierre.getValor("valor_tecca")) != pckUtilidades.CConversion.CDbl_2(valor))
			guardar=true;
		
		tab_cierre.setValor("valor_efectivo_tecca",valor_efectivo);
		tab_cierre.setValor("valor_tecca",valor);
		tab_cierre.modificar(tab_cierre.getFilaActual());//para que haga el update
		tab_cierre.guardar();
		
		eti_billetes.setValue("TOTAL : " + utilitario.getFormatoNumero(dou_total_billetes, 2));
		eti_monedas.setValue("TOTAL : " + utilitario.getFormatoNumero(dou_total_monedas, 2));
		eti_cheques.setValue("TOTAL : " + utilitario.getFormatoNumero(dou_total_cheques, 2));
		
		if(guardar)
			guardarPantalla();
		
		utilitario.addUpdateTabla(tab_cierre, "valor_tecca,valor_efectivo_tecca", "");
		utilitario.addUpdate("tab_cobro_facturas,tab_cobro_ndebitos,tab_cobro_contratos,tab_billetes,tab_monedas,tab_cheques,eti_billetes,eti_monedas,eti_cheques");
		
		if(!valor_efectivo.equals(valor))
		{
			System.out.println("ide_tecaj: "+tab_cierre.getValor("ide_tecaj"));
			//utilitario.agregarNotificacionInfo("Cierre", "El cierre de caja no esta cuadrado... valor cierre: "+valor+", valor en efectivo: "+valor_efectivo);
			utilitario.agregarMensajeInfo("Cierre Descuadrado.!!", "El cierre de caja no esta cuadrado... valor cierre: "+valor+", valor en efectivo: "+valor_efectivo);
		}
	}
	
	public void generarCierre()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
		}
		else if(tab_cierre.getValor("ide_tecca")==null){
			utilitario.agregarMensajeError("No se puede importar", "Debe Guardar el cierre primero...");
			return;
		}
		
		if(sel_calendario.isVisible()){
			sel_calendario.cerrar();

			String ide_tecca = tab_cierre.getValor("ide_tecca");
			boolean insertar;
			
			TablaGenerica tab_cobros = utilitario.consultar("SELECT ide_facob, ide_fafac, ide_fanod, ide_prcon FROM fac_cobro where ide_tecca="+ide_tecca);
			
			if(tab_cobros.getTotalFilas()>0)
			{
				utilitario.agregarMensajeInfo("No se puede importar", "Ya existen cobros importados...");
			}
			else
			{
				String sql="update fac_cobro set ide_tecca="+ide_tecca+" where fecha_cobro_facob between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'";
				System.out.println("imprimir sql update cobros "+sql);
				utilitario.getConexion().ejecutarSql(sql);
			}
			
			TablaGenerica tab_plantilla_efectivo = utilitario.consultar("SELECT ide_ttefe, billete_ttefe FROM tes_tipo_efectivo where activo_ttefe=true order by 1;");
			TablaGenerica tab_efectivo = utilitario.consultar("SELECT ide_tefec, ide_ttefe, tipo_tefec FROM tes_efectivo where ide_tecca="+ide_tecca+" order by 2;");
			
			for(int index=0;index<tab_plantilla_efectivo.getTotalFilas();index++)
			{
				
				insertar=true;
				for(int index2=0;index2<tab_efectivo.getTotalFilas();index2++)
				{
					if(pckUtilidades.CConversion.CInt(tab_efectivo.getValor(index2,"tipo_tefec"))==3)
						insertar=false;
					
					if(tab_plantilla_efectivo.getValor(index,"ide_ttefe").equals(tab_efectivo.getValor(index2,"ide_ttefe")))
						insertar=false;
				}
				
				if(insertar)
				{
					tab_billetes.insertar();
					tab_billetes.setValor("ide_tecca",ide_tecca);
					tab_billetes.setValor("ide_ttefe", tab_plantilla_efectivo.getValor(index,"ide_ttefe"));
					tab_billetes.setValor("tipo_tefec", (pckUtilidades.CConversion.CBol(tab_plantilla_efectivo.getValor(index,"billete_ttefe"))?"1":"2"));
				}
			}

			if(tab_billetes.guardar())
			{
				tab_cierre.modificar(tab_cierre.getFilaActual());//para que haga el update
				tab_cierre.guardar();
			}
			guardarPantalla();	
			actualizaDetalles();
		}
		else {
			
			sel_calendario.setFecha1(utilitario.DeStringADate(tab_cierre.getValor("fecha_tecca")));
			sel_calendario.setFecha2(utilitario.DeStringADate(tab_cierre.getValor("fecha_tecca")));
			
			sel_calendario.dibujar();
		}
		
		
		
	}
	
	public void exportarExcel(){
		if (tab_cierre.getTotalFilas()>0){
				exportarXLS("cierreCaja.xls");
			
		}
	}
	
	public void exportarXLS(String nombre) { 
		try { 
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 
			String nom = nombre; 
			File result = new File(extContext.getRealPath("/" + nom)); 
			WritableWorkbook archivo_xls = Workbook.createWorkbook(result); 
			hoja_xls = archivo_xls.createSheet("Tabla"+tab_cierre.getValor("ide_tecaj"), 0); 
			
			WritableFont fuente_titulo = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
			formato_celda_titulo = new WritableCellFormat(fuente_titulo); 
			formato_celda_titulo.setAlignment(jxl.format.Alignment.CENTRE); 
			formato_celda_titulo.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_titulo.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_titulo.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
			formato_celda_titulo.setBackground(Colour.GREY_25_PERCENT);
			
			WritableFont fuente = new WritableFont(WritableFont.ARIAL, 10);
			formato_celda = new WritableCellFormat(fuente); 
			formato_celda.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda.setOrientation(Orientation.HORIZONTAL); 


			WritableFont fuente_sub_titulo = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			formato_celda_sub_titulo = new WritableCellFormat(fuente_sub_titulo); 
			formato_celda_sub_titulo.setAlignment(jxl.format.Alignment.CENTRE); 
			formato_celda_sub_titulo.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_sub_titulo.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_sub_titulo.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			
			WritableFont fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
			fuente_totales.setColour(Colour.RED);
			WritableCellFormat formato_celda_totales = new WritableCellFormat(fuente_totales); 
			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT); 
			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_totales.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);


			int int_columna = 1; 
			int int_fila=1;

			CellView cv=new CellView();
			jxl.write.Label lab1;
			
			TablaGenerica tab_tes_tipo_efectivo = utilitario.consultar("SELECT ide_ttefe, detalle_ttefe, valor_ttefe, billete_ttefe FROM tes_tipo_efectivo");
			TablaGenerica tab_gen_institucion = utilitario.consultar("select IDE_GEINS, DETALLE_GEINS from GEN_INSTITUCION where GEN_IDE_GEINS IS NOT NULL and IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));
			
			lab1 = new jxl.write.Label(int_columna, int_fila, "REPORTE DE CIERRE DE CAJA CENTRAL", formato_celda_titulo);
			hoja_xls.addCell(lab1);
			//cv=new CellView();
			//cv.setAutosize(true);
			//hoja_xls.setColumnView(int_columna,cv);
			//hoja_xls.mergeCells(columna, fila, columnaFin, filaFin);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 8, int_fila);
			
			////		
			int_fila+=2;
			lab1 = new jxl.write.Label(int_columna, int_fila, "CAJA:", formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			lab1 = new jxl.write.Label(int_columna+1, int_fila, tab_cierre.getValor("ide_tecaj"), formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "FECHA:", formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			lab1 = new jxl.write.Label(int_columna+1, int_fila, utilitario.getFechaLarga(tab_cierre.getValor("fecha_tecca")), formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			int_fila+=1;
			lab1 = new jxl.write.Label(int_columna, int_fila, "FECHA DEPOSITO:", formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			lab1 = new jxl.write.Label(int_columna+1, int_fila, utilitario.getFechaLarga(tab_cierre.getValor("fecha_deposito_tecca")), formato_celda);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna,cv);
			
			int_fila=detalleCobros(int_columna, int_fila, "FACTURAS",tab_cobro_facturas);
			int_fila=detalleCobros(int_columna, int_fila+1, "NOTAS DE DEBITO",tab_cobro_ndebitos);
			int_fila=detalleCobros(int_columna, int_fila+1, "CONTRATOS",tab_cobro_contratos);
			
			int_fila+=2;
			lab1 = new jxl.write.Label(int_columna, int_fila, "DETALLE CAJA", formato_celda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 8, int_fila);
			
			detalleEfectivo(int_columna, int_fila, "BILLETES",tab_billetes, tab_tes_tipo_efectivo, tab_gen_institucion);
			detalleEfectivo(int_columna+3, int_fila, "CHEQUES",tab_cheques, tab_tes_tipo_efectivo, tab_gen_institucion);
			int_fila=detalleEfectivo(int_columna+6, int_fila, "MONEDAS",tab_monedas, tab_tes_tipo_efectivo, tab_gen_institucion);
			
			//////////
			
			int_fila+=2;
			lab1 = new jxl.write.Label(int_columna, int_fila, "RESUMEN", formato_celda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 8, int_fila);
			
			int_columna+=2;
			int_fila++;
			lab1 = new jxl.write.Label(int_columna + 2, int_fila, "VALORES RECAUDADOS", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 2,cv);
			
			lab1 = new jxl.write.Label(int_columna + 3, int_fila, "VALORES EN CAJA", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 3,cv);
			
			lab1 = new jxl.write.Label(int_columna + 4, int_fila, "DIFERENCIAS", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 4,cv);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "VALOR TOTAL EN EFECTIVO", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			jxl.write.Number labnum = new jxl.write.Number(int_columna + 2, int_fila, valor_tefec, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 2,cv);
			
			labnum = new jxl.write.Number(int_columna + 3, int_fila, (dou_total_billetes+dou_total_monedas), formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 3,cv);
			
			labnum = new jxl.write.Number(int_columna + 4, int_fila, valor_tefec - (dou_total_billetes+dou_total_monedas), formato_celda_totales);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 4,cv);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "VALOR TOTAL EN CHEQUES", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			labnum = new jxl.write.Number(int_columna + 2, int_fila, valor_tchq, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 2,cv);
			
			labnum = new jxl.write.Number(int_columna + 3, int_fila, dou_total_cheques, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 3,cv);
			
			labnum = new jxl.write.Number(int_columna + 4, int_fila, valor_tchq - dou_total_cheques, formato_celda_totales);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 4,cv);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "VALOR TOTAL DEL DEPOSITO", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			labnum = new jxl.write.Number(int_columna + 2, int_fila, (valor_tefec+valor_tchq), formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 2,cv);
			
			labnum = new jxl.write.Number(int_columna + 3, int_fila, (dou_total_billetes+dou_total_monedas+dou_total_cheques), formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(int_columna + 3,cv);
			
			int_fila+=7;
			int_columna--;
			WritableCellFormat fcelda_titulo = new WritableCellFormat(formato_celda_sub_titulo);
			fcelda_titulo.setBorder(Border.NONE, BorderLineStyle.NONE);
			lab1 = new jxl.write.Label(int_columna, int_fila, "____________________________", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "ELABORADO POR:", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "RECAUDADOR OF. MATRIZ", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			
			int_fila-=2;
			int_columna+=5;
			lab1 = new jxl.write.Label(int_columna, int_fila, "____________________________", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "REVISADO POR:", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);
			
			int_fila++;
			lab1 = new jxl.write.Label(int_columna, int_fila, "ANALISTA TESORERÍA", fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(int_columna, int_fila, int_columna + 1, int_fila);

			archivo_xls.write(); 
			archivo_xls.close(); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom); 
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
			utilitario.agregarMensajeError("Error no se genero el XLS :", e.getMessage());
		} 
	}
	
	public int detalleCobros(int columna, int fila, String titulo, Tabla tab_dCobro)
	{
		CellView cv=new CellView();
		jxl.write.Label lab1;
		jxl.write.Number labnum;
		double valor=0;
		double valor_efec=0;
		double valor_chq=0;
		
		try { 
			fila+=2;
			
			lab1 = new jxl.write.Label(columna, fila, titulo, formato_celda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(columna, fila, columna + 8, fila);
			
			// NOMBRES DE COLUMNAS
			fila++;
			lab1 = new jxl.write.Label(columna, fila, "NO. DE COMPROBANTE", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+1, fila, "FECHA DE EMISIÓN", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+2, fila, "RUC", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);

			lab1 = new jxl.write.Label(columna+3, fila, "NOMBRE USUARIO", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);


			lab1 = new jxl.write.Label(columna+4, fila, "TOTAL EFECTIVO", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);

			lab1 = new jxl.write.Label(columna+5, fila, "TOTAL CHEQUE", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+6, fila, "VALOR DEPÓSITO", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+7, fila, "No. COMPROB. DE INGRESO CAJA", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+8, fila, "DETALLE", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			

			for (int i = 0; i < tab_dCobro.getTotalFilas(); i++) 
			{
				fila++;
				
				lab1 = new jxl.write.Label(columna, fila,tab_dCobro.getValor(i, "secuencial"), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna,cv);
				
				lab1 = new jxl.write.Label(columna+1, fila,tab_dCobro.getValor(i, "fecha_emision"), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+1,cv);
				
				lab1 = new jxl.write.Label(columna+2, fila,tab_dCobro.getValor(i, "ruc_comercial"), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+2,cv);
			
				lab1 = new jxl.write.Label(columna+3, fila,tab_dCobro.getValor(i, "razon_social"), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+3,cv);
		
				valor=(tab_dCobro.getValor(i, "forma_cobro").contains("EFECTIVO") ? pckUtilidades.CConversion.CDbl(tab_dCobro.getValor(i, "cobro")) : 0);
				valor_efec+=valor;
				labnum = new jxl.write.Number(columna+4, fila, valor, formato_celda);
				hoja_xls.addCell(labnum);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+4,cv);

				valor=(tab_dCobro.getValor(i, "forma_cobro").contains("CHEQUE") ? pckUtilidades.CConversion.CDbl(tab_dCobro.getValor(i, "cobro")) : 0);
				valor_chq+=valor;
				labnum = new jxl.write.Number(columna+5, fila, valor, formato_celda);
				hoja_xls.addCell(labnum);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+5,cv);
	
				labnum = new jxl.write.Number(columna+6, fila, 0, formato_celda);
				hoja_xls.addCell(labnum);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+6,cv);
				
				lab1 = new jxl.write.Label(columna+7, fila,tab_dCobro.getValor(i, "nro_documento_facob"), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+7,cv);
				
				lab1 = new jxl.write.Label(columna+8, fila, (pckUtilidades.CConversion.CStr(tab_dCobro.getValor(i, "establecimiento_operativo")) + " - "+ pckUtilidades.CConversion.CStr(tab_dCobro.getValor(i, "observacion"))), formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+8,cv);
			}
			
			fila++;
			valor_tefec+=valor_efec;
			valor_tchq+=valor_chq;
			
			lab1 = new jxl.write.Label(columna, fila, "TOTALES", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(columna, fila, columna + 3, fila);
			
			labnum = new jxl.write.Number(columna+4, fila, valor_efec, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna+4,cv);
			
			labnum = new jxl.write.Number(columna+5, fila, valor_chq, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna+5,cv);
			
			labnum = new jxl.write.Number(columna+6, fila, 0, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna+6,cv);
		
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS detalleCobros:" + e.getMessage()); 
			utilitario.agregarMensajeError("Error no se genero el XLS detalleCobros:", e.getMessage());
		} 
		
		return fila++;
	}
	
	
	public int detalleEfectivo(int columna, int fila, String titulo, Tabla tab_dEfectivo
			, TablaGenerica tab_tes_tipo_efectivo
			, TablaGenerica tab_gen_institucion)
	{
		CellView cv=new CellView();
		jxl.write.Label lab1;
		jxl.write.Number labnum;
		double valor_d;
		String denominacion="";
	
		try { 
			
			fila++;
			WritableCellFormat fcelda_titulo = new WritableCellFormat(formato_celda_titulo);// formato_celda_titulo;
			if(!titulo.contains("CHEQUE"))
				fcelda_titulo.setBackground(Colour.GOLD);
			
			lab1 = new jxl.write.Label(columna, fila, titulo, fcelda_titulo);
			hoja_xls.addCell(lab1);
			hoja_xls.mergeCells(columna, fila, columna + 2, fila);
			
			// NOMBRES DE COLUMNAS
			fila++;
			lab1 = new jxl.write.Label(columna, fila, "DENOMINACION", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+1, fila, "CANTIDAD", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);
			
			lab1 = new jxl.write.Label(columna+2, fila, "VALOR", formato_celda_sub_titulo);
			hoja_xls.addCell(lab1);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna,cv);

			for (int i = 0; i < tab_dEfectivo.getTotalFilas(); i++) 
			{
				fila++;
				valor_d=0;
				denominacion="";
				
				if(titulo.contains("CHEQUE"))
				{
					for (int j = 0; j < tab_gen_institucion.getTotalFilas(); j++) 
					{
						if(pckUtilidades.CConversion.CInt(tab_dEfectivo.getValor(i, "ide_geins"))==pckUtilidades.CConversion.CInt(tab_gen_institucion.getValor(j, "ide_geins")))
						{
							denominacion=pckUtilidades.CConversion.CStr(tab_gen_institucion.getValor(j, "DETALLE_GEINS"));
							break;
						}
					}

					valor_d = pckUtilidades.CConversion.CDbl(tab_dEfectivo.getValor(i, "valor_tefec"));
				}
				else
				{
					for (int j = 0; j < tab_tes_tipo_efectivo.getTotalFilas(); j++) 
					{
						if(pckUtilidades.CConversion.CInt(tab_dEfectivo.getValor(i, "ide_ttefe"))==pckUtilidades.CConversion.CInt(tab_tes_tipo_efectivo.getValor(j, "ide_ttefe")))
						{
							denominacion=pckUtilidades.CConversion.CStr(tab_tes_tipo_efectivo.getValor(j, "detalle_ttefe"));
							valor_d=pckUtilidades.CConversion.CDbl_2(tab_tes_tipo_efectivo.getValor(j, "valor_ttefe"));
							break;
						}
					}
					
					valor_d = pckUtilidades.CConversion.CDbl(pckUtilidades.CConversion.CDbl(tab_dEfectivo.getValor(i, "cantidad_tefec")) * valor_d);
				}
				
				lab1 = new jxl.write.Label(columna, fila, denominacion, formato_celda);
				hoja_xls.addCell(lab1);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna,cv);
				
				labnum = new jxl.write.Number(columna+1, fila, pckUtilidades.CConversion.CDbl(tab_dEfectivo.getValor(i, "cantidad_tefec")), formato_celda);
				hoja_xls.addCell(labnum);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+1,cv);
				
				
				labnum = new jxl.write.Number(columna+2, fila, valor_d, formato_celda);
				hoja_xls.addCell(labnum);
				cv=new CellView();
				cv.setAutosize(true);
				hoja_xls.setColumnView(columna+2,cv);
			}
		
			fila++;
			valor_d=0;
			if(titulo.contains("BILLETE"))
				valor_d=dou_total_billetes;
			
			if(titulo.contains("MONEDA"))
				valor_d=dou_total_monedas;
			
			if(titulo.contains("CHEQUE"))
				valor_d=dou_total_cheques;
			
			labnum = new jxl.write.Number(columna+2, fila, valor_d, formato_celda_sub_titulo);
			hoja_xls.addCell(labnum);
			cv=new CellView();
			cv.setAutosize(true);
			hoja_xls.setColumnView(columna+2,cv);
		
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS detalleCobros:" + e.getMessage()); 
			utilitario.agregarMensajeError("Error no se genero el XLS detalleCobros:", e.getMessage());
		} 
		
		return fila++;
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
		}
		
		if(tab_cierre.isFocus())
		{
			tab_cierre.insertar();
			tab_cierre.setValor("secuencial_cierre_tecca", ser_contabilidad.numeroSecuencial(par_modulosec_cierre));
			tab_cierre.setValor("ide_coest", "2");
			tab_cierre.setValor("ide_tecaj", ide_caja);
			tab_cierre.setValor("ide_geani", com_anio.getValue()+"");
			tab_cierre.setValor("ide_gtemp", ide_empleado);
			actualizaDetalles();
			utilitario.agregarMensajeInfo("Importante", "No se olvide de importar los datos.");
		}
		
		if(tab_cheques.isFocus())
		{
			tab_cheques.insertar();
			tab_cheques.setValor("cantidad_tefec", "1");
		}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_cierre.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_cierre), par_modulosec_cierre);
		}
		
		if (tab_cierre.guardar())
		{
		  if( tab_cheques.guardar())
		  {
			 guardarPantalla();			 
		  }
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void inicio() {
        super.inicio();
        // TODO Auto-generated method stub
        if (tab_cierre.isFocus()){
			actualizaDetalles();
		}
    }
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
    	super.siguiente();
    	if (tab_cierre.isFocus()){
			actualizaDetalles();
		}
    }
    @Override
    public void atras() {
        super.atras();
        // TODO Auto-generated method stub
        if (tab_cierre.isFocus()){
			actualizaDetalles();
		}
    }
    
	@Override
	public void fin()
	{
		super.fin();
		if (tab_cierre.isFocus()){
			actualizaDetalles();
		}
	}

	public Tabla getTab_cierre() {
		return tab_cierre;
	}

	public void setTab_cierre(Tabla tab_cierre) {
		this.tab_cierre = tab_cierre;
	}

	public Tabla getTab_cobro_facturas() {
		return tab_cobro_facturas;
	}

	public void setTab_cobro_facturas(Tabla tab_cobro_facturas) {
		this.tab_cobro_facturas = tab_cobro_facturas;
	}

	public Tabla getTab_cobro_ndebitos() {
		return tab_cobro_ndebitos;
	}

	public void setTab_cobro_ndebitos(Tabla tab_cobro_ndebitos) {
		this.tab_cobro_ndebitos = tab_cobro_ndebitos;
	}

	public Tabla getTab_cobro_contratos() {
		return tab_cobro_contratos;
	}

	public void setTab_cobro_contratos(Tabla tab_cobro_contratos) {
		this.tab_cobro_contratos = tab_cobro_contratos;
	}

	public Tabla getTab_cierres_sucursales() {
		return tab_cierres_sucursales;
	}

	public void setTab_cierres_sucursales(Tabla tab_cierres_sucursales) {
		this.tab_cierres_sucursales = tab_cierres_sucursales;
	}

	public Tabla getTab_billetes() {
		return tab_billetes;
	}

	public void setTab_billetes(Tabla tab_billetes) {
		this.tab_billetes = tab_billetes;
	}

	public Tabla getTab_monedas() {
		return tab_monedas;
	}

	public void setTab_monedas(Tabla tab_monedas) {
		this.tab_monedas = tab_monedas;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Tabla getTab_cheques() {
		return tab_cheques;
	}

	public void setTab_cheques(Tabla tab_cheques) {
		this.tab_cheques = tab_cheques;
	}


}
