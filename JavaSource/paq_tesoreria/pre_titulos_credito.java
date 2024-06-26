package paq_tesoreria;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;


public class pre_titulos_credito extends Pantalla{
	
	private Tabla tab_titulo = new Tabla();
	private Tabla tab_detalle_titulo=new Tabla();
	private SeleccionTabla sel_clientes = new SeleccionTabla();
	private SeleccionTabla sel_comp_vencidos = new SeleccionTabla();

	private Combo com_anio=new Combo();
    public static String par_modulosec_titulos;
    private String ide_empleado="";
	
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	private String str_ruc_cliente_seleccionado="";
	private String str_comprobantes_seleccionado="";
	private Texto txt_nro_orden = new Texto();
	private Dialogo dia_nro_orden = new Dialogo();
	
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

	public pre_titulos_credito(){
		
		par_modulosec_titulos=utilitario.getVariable("p_modulo_secuencial_titulo");
		ide_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		if(ide_empleado==null ||ide_empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro");
			return;
		}

		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:")); 
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_agregar = new Boton();
		bot_agregar.setValue("Nuevo Titulo de Credito");
		bot_agregar.setMetodo("agregar_cliente");
		bar_botones.agregarBoton(bot_agregar);
		
		tab_titulo.setId("tab_titulo");
		tab_titulo.setHeader("TITULO DE CREDITO");
		tab_titulo.setTabla("fac_convenio", "ide_facon", 1);
		tab_titulo.setCondicion("titulo_credito_facon=true and ide_geani=-1"); 
		tab_titulo.getColumna("titulo_credito_facon").setValorDefecto("true");
		tab_titulo.getColumna("activo_facon").setValorDefecto("true");
		tab_titulo.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_titulo.getColumna("fecha_titulo_facon").setValorDefecto(utilitario.getFechaActual());
		tab_titulo.getColumna("fecha_facon").setVisible(false);
		tab_titulo.getColumna("nro_convenio_facon").setVisible(false);
		tab_titulo.getColumna("nro_titulo_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_titulo.getColumna("nro_titulo_facon").setEtiqueta();
		tab_titulo.getColumna("numero_meses_facon").setVisible(false);
		tab_titulo.getColumna("valor_cuotas_facon").setVisible(false);
		tab_titulo.getColumna("monto_entrada_facon").setVisible(false);
		tab_titulo.getColumna("interes_facon").setVisible(false);
		tab_titulo.getColumna("fecha_aprobacion_facon").setVisible(false);
		tab_titulo.getColumna("saldo_facon").setVisible(false);
		tab_titulo.getColumna("titulo_credito_facon").setVisible(false);
		tab_titulo.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_titulo.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_titulo.getColumna("ide_gtemp").setLectura(true);
		tab_titulo.getColumna("ide_gtemp").setAutoCompletar();
		tab_titulo.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("-1"));
		tab_titulo.getColumna("ide_recli").setAutoCompletar();
		tab_titulo.getColumna("ide_recli").setLectura(true);
		tab_titulo.getColumna("monto_convenio_facon").setValorDefecto("0.00");
		tab_titulo.setTipoFormulario(true);
		tab_titulo.getGrid().setColumns(4);
		tab_titulo.agregarRelacion(tab_detalle_titulo);
		tab_titulo.dibujar();
		PanelTabla pat_titulo=new PanelTabla();
		pat_titulo.setPanelTabla(tab_titulo);
		
		
		tab_detalle_titulo.setId("tab_detalle_titulo");
		tab_detalle_titulo.setHeader("DETALLE TITULO");
		tab_detalle_titulo.setTabla("fac_detalle_convenio", "ide_facof", 2);
		tab_detalle_titulo.getColumna("activo_facof").setValorDefecto("true");
		tab_detalle_titulo.getColumna("ide_fafac").setCombo("fac_factura","ide_fafac","secuencial_fafac"," ide_fafac in (SELECT distinct ide_fafac FROM fac_detalle_convenio) ");
		//tab_detalle_titulo.getColumna("ide_fafac").setCombo("fac_factura","ide_fafac","secuencial_fafac","");
		tab_detalle_titulo.getColumna("ide_fafac").setLectura(true);
		tab_detalle_titulo.getColumna("ide_fafac").setAutoCompletar();
		
		tab_detalle_titulo.getColumna("ide_fanod").setCombo("fac_nota_debito","ide_fanod","'Nota Debito '||coalesce(nro_nota_debito_elect_fanod,'')"," ide_fanod in (SELECT distinct ide_fanod FROM fac_detalle_convenio) ");
		//tab_detalle_titulo.getColumna("ide_fanod").setCombo("fac_nota_debito","ide_fanod","'Nota Debito '||coalesce(nro_nota_debito_elect_fanod,'')","");
		tab_detalle_titulo.getColumna("ide_fanod").setLectura(true);
		tab_detalle_titulo.getColumna("ide_fanod").setAutoCompletar();
		tab_detalle_titulo.getColumna("valor_facof").setLectura(true);
		tab_detalle_titulo.setColumnaSuma("valor_facof"); 
		tab_detalle_titulo.setRows(10);
		tab_detalle_titulo.dibujar();
		PanelTabla pat_det_titulo=new PanelTabla();
		pat_det_titulo.setPanelTabla(tab_detalle_titulo);
		
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_titulo, pat_det_titulo, "49%", "H");
		agregarComponente(div_divi);
		
		inicializaClientes();
		inicializaDeudas();
		
		txt_nro_orden.setId("txt_nro_orden");
		txt_nro_orden.setMaxlength(3);
		txt_nro_orden.setValue("000-CF-0000");
		txt_nro_orden.setSoloEnteros();
		
		dia_nro_orden.setId("dia_nro_orden");
		dia_nro_orden.setTitle("NUMERO ORDEN DE COBRO");
		dia_nro_orden.setWidth("25%");
		dia_nro_orden.setHeight("20%");
		dia_nro_orden.setFooter("Ingrese unicamente el numero correspondiente, el sistema lo formateara a: 000-CF-202X");
		
		Grid grid_dia = new Grid();		
		grid_dia.setColumns(2);
		grid_dia.getChildren().add(new Etiqueta("Nro. Orden Cobro"));
		grid_dia.getChildren().add(txt_nro_orden);		
		dia_nro_orden.getBot_aceptar().setMetodo("aceptarReporte");		
		dia_nro_orden.setDialogo(grid_dia);
		agregarComponente(dia_nro_orden);
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_titulo.setCondicion("titulo_credito_facon=true and ide_geani="+com_anio.getValue());
			tab_titulo.ejecutarSql();
			tab_detalle_titulo.ejecutarValorForanea(tab_titulo.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void inicializaClientes(){
		sel_clientes.setId("sel_clientes");
		sel_clientes.setTitle("LISTA DE CLIENTES");
		sel_clientes.setSeleccionTabla(ser_facturacion.getDatosBasicosClientesLite("0,1"), "ide_recli");
		sel_clientes.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_clientes.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(30);
		sel_clientes.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		sel_clientes.getTab_seleccion().getColumna("razon_social_recli").setLongitud(100);
		//sel_clientes.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		//sel_clientes.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(100);
		sel_clientes.getTab_seleccion().setCondicion("1=-1");
		sel_clientes.setRadio();
		sel_clientes.getTab_seleccion().ejecutarSql();
		sel_clientes.getBot_aceptar().setMetodo("agregar_cliente");
		agregarComponente(sel_clientes);
	}
	
	public void inicializaDeudas(){
		sel_comp_vencidos.setId("sel_comp_vencidos");
		sel_comp_vencidos.setTitle("COMPROBANTES IMPAGOS");
		sel_comp_vencidos.setSeleccionTabla(ser_tesoreria.getSqlDeudaClientesAbonos("-1"), "codigo");
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_fafac").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_recli").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_fanod").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ruc_comercial_recli").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("detalle_bogrm").setLongitud(50);
		sel_comp_vencidos.getTab_seleccion().getColumna("secuencial_fafac").setLongitud(40);
		//sel_comp_vencidos.seleccionarTodas();
		sel_comp_vencidos.getTab_seleccion().ejecutarSql();
		sel_comp_vencidos.getBot_aceptar().setMetodo("agregar_cliente");
		agregarComponente(sel_comp_vencidos);
	}
	
	public void agregar_cliente(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(sel_clientes.isVisible()){
			if(sel_clientes.getValorSeleccionado()!=null){
				str_ruc_cliente_seleccionado=sel_clientes.getValorSeleccionado();	
				sel_clientes.cerrar();	

				sel_comp_vencidos.setSql(ser_tesoreria.getSqlDeudaClientesAbonos(str_ruc_cliente_seleccionado));
				sel_comp_vencidos.getTab_seleccion().ejecutarSql();
				sel_comp_vencidos.dibujar();
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else if(sel_comp_vencidos.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_comp_vencidos.getSeleccionados() !=null){
				str_comprobantes_seleccionado=sel_comp_vencidos.getSeleccionados();
				sel_comp_vencidos.cerrar();
				//todo aqui
				
				tab_titulo.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("1,0"));
				tab_titulo.getColumna("ide_recli").setAutoCompletar();
				tab_titulo.getColumna("ide_recli").setLectura(true);
				tab_titulo.dibujar();
				
				tab_detalle_titulo.getColumna("ide_fafac").setCombo("fac_factura","ide_fafac","secuencial_fafac","");
				tab_detalle_titulo.getColumna("ide_fanod").setCombo("fac_nota_debito","ide_fanod","'Nota Debito '||coalesce(nro_nota_debito_elect_fanod,'')","");
				tab_detalle_titulo.getColumna("activo_facof").setValorDefecto("true");
				tab_detalle_titulo.dibujar();
				utilitario.addUpdate("tab_titulo,tab_detalle_titulo");
				
				TablaGenerica tab_cliente_seleccionado= utilitario.consultar("select ide_recli as codigo, ide_recli "
						+ " from rec_clientes where ruc_comercial_recli like '"+str_ruc_cliente_seleccionado+"' and coalesce(nro_establecimiento_recli,0)=1 and activo_recli in (true,false) order by activo_recli limit 1 ");
				
			//	tab_cliente_seleccionado.imprimirSql();
				
				if(tab_cliente_seleccionado.getTotalFilas()<=0)
				{
					utilitario.agregarMensajeInfo("El cliente seleccionado no posee una matriz", "");
					return;
				}
				
				tab_titulo.insertar();
				tab_titulo.setValor("nro_titulo_facon", ser_contabilidad.numeroSecuencial(par_modulosec_titulos));
				tab_titulo.setValor("ide_geani", com_anio.getValue()+"");
				tab_titulo.setValor("ide_recli", tab_cliente_seleccionado.getValor("ide_recli"));
				tab_titulo.setValor("ide_gtemp", ide_empleado);
				tab_titulo.setValor("ide_coest", "2");
				
				String[] str_comp=str_comprobantes_seleccionado.split(",");
				
				for(int i=0;i<sel_comp_vencidos.getTab_seleccion().getTotalFilas();i++){
					
					for(int j=0;j<str_comp.length;j++)
					{
						if(str_comp[j].equals(sel_comp_vencidos.getTab_seleccion().getValor(i,"codigo")))
						{
							tab_detalle_titulo.insertar();
							tab_detalle_titulo.setValor("ide_fafac", sel_comp_vencidos.getTab_seleccion().getValor(i, "ide_fafac")+"");
							tab_detalle_titulo.setValor("valor_facof", sel_comp_vencidos.getTab_seleccion().getValor(i, "valor")+"");
							
							if(pckUtilidades.CConversion.CInt(sel_comp_vencidos.getTab_seleccion().getValor(i,"ide_fanod")) > 0){
								tab_detalle_titulo.insertar();
								tab_detalle_titulo.setValor("ide_fanod", sel_comp_vencidos.getTab_seleccion().getValor(i, "ide_fanod")+"");
								tab_detalle_titulo.setValor("valor_facof", sel_comp_vencidos.getTab_seleccion().getValor(i, "interes")+"");
							 }
						}
					}
				}
				tab_detalle_titulo.sumarColumnas();
				tab_titulo.setValor("monto_convenio_facon",tab_detalle_titulo.getSumaColumna("valor_facof")+"");
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else
		{
			sel_clientes.getTab_seleccion().setCondicion("1=1");
			sel_clientes.getTab_seleccion().ejecutarSql();
			sel_clientes.dibujar();
		}
	}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("titulos de credito"))
		{
			//TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				Locale locale=new Locale("es","ES");
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				TablaGenerica tab_cliente_ruc=utilitario.consultar("select ide_recli,ruc_comercial_recli from rec_clientes where ide_recli="+tab_titulo.getValor("ide_recli"));
				
				p_parametros.put("titulo","TITULO DE CREDITO");
				p_parametros.put("p_cliente_ruc", pckUtilidades.CConversion.CStr(tab_cliente_ruc.getValor("ruc_comercial_recli")));
				p_parametros.put("p_nro",  pckUtilidades.CConversion.CStr(pckUtilidades.Utilitario.padLeft(tab_titulo.getValor("nro_titulo_facon"),7)));				
				p_parametros.put("p_ide_facon", pckUtilidades.CConversion.CInt(tab_titulo.getValor("ide_facon")));
				p_parametros.put("p_fecha_emision",  pckUtilidades.CConversion.CStr("QUITO, "+ utilitario.DateStringFormat(tab_titulo.getValor("fecha_titulo_facon"))));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("REPORT_LOCALE", locale);
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();
			
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
		else
			if(rep_reporte.getReporteSelecionado().equals("orden de cobro"))
			{
				
				if (rep_reporte.isVisible()){					
					p_parametros = new HashMap();
					rep_reporte.cerrar();
					txt_nro_orden.setValue("0");
					utilitario.addUpdate("txt_nro_orden");
					dia_nro_orden.dibujar();
				} else if (dia_nro_orden.isVisible()) {
					dia_nro_orden.cerrar();
					
					Locale locale=new Locale("es","ES");
					TablaGenerica tab_cliente_ruc=utilitario.consultar("select ide_recli,ruc_comercial_recli from rec_clientes where ide_recli="+tab_titulo.getValor("ide_recli"));
					TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
					/*TablaGenerica tab_rango=utilitario.consultar("SELECT 1 as codigo,max(fecha_vencimiento_fafac) as hasta, min(fecha_vencimiento_fafac) as desde,string_agg(distinct detalle_bogrm, ', ') as concepto , "
																+"max(fecha_vencimiento_fafac)-min(fecha_vencimiento_fafac) as dias FROM fac_detalle_convenio fdc "
																+" join fac_factura fac on fac.ide_fafac=fdc.ide_fafac "
																+" join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm "
																+"       from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm) dfac on dfac.ide_fadaf=fac.ide_fadaf "
																+"  where ide_facon="+tab_titulo.getValor("ide_facon"));*/
					TablaGenerica tab_concepto=utilitario.consultar("SELECT 1 as codigo,string_agg(distinct detalle_bogrm, ', ') as concepto FROM fac_detalle_convenio fdc "
							+" join fac_factura fac on fac.ide_fafac=fdc.ide_fafac "
							+" join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm "
							+"       from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm) dfac on dfac.ide_fadaf=fac.ide_fadaf "
							+"  where ide_facon="+tab_titulo.getValor("ide_facon"));
					//tab_concepto.imprimirSql();
					String nro_orden = pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(txt_nro_orden.getValue())+"",3)+"-CF-"+tab_anio.getValor("detalle_geani");
					
					p_parametros.put("titulo","ORDEN DE COBRO");
					p_parametros.put("p_cliente_ruc", pckUtilidades.CConversion.CStr(tab_cliente_ruc.getValor("ruc_comercial_recli")));
					p_parametros.put("p_nro", nro_orden);
					p_parametros.put("p_nro_titulo", pckUtilidades.CConversion.CStr(pckUtilidades.Utilitario.padLeft(tab_titulo.getValor("nro_titulo_facon"),7)));	
					//p_parametros.put("p_periodo", pckUtilidades.CConversion.CStr("DESDE "+utilitario.DateStringFormat(tab_rango.getValor("desde"))+" - HASTA "+utilitario.DateStringFormat(tab_rango.getValor("hasta"))+", DÍAS VENCIDOS: "+tab_rango.getValor("dias")+""));
					p_parametros.put("p_periodo", pckUtilidades.CConversion.CStr("DESDE LA FECHA DE GENERACIÓN DE LA OBLIGACIÓN HASTA LA FECHA DE CORTE DEL TÍTULO DE CREDITO"));
					p_parametros.put("p_monto", pckUtilidades.CConversion.CDbl_2(tab_titulo.getValor("monto_convenio_facon")));
					p_parametros.put("p_concepto",tab_concepto.getValor("concepto"));
					p_parametros.put("p_fecha_emision",  pckUtilidades.CConversion.CStr("QUITO, "+ utilitario.DateStringFormat(tab_titulo.getValor("fecha_titulo_facon"))));
					p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
					p_parametros.put("REPORT_LOCALE", locale);
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					self_reporte.dibujar();

				}
				else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
		
				}
			}
			
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("No se puede continuar", "Favor, utilice el boton de Nuevo Titulo...");
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if(tab_titulo.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_titulos), par_modulosec_titulos);
		}
		
		if (tab_titulo.guardar())
		  if( tab_detalle_titulo.guardar())
		  {
			 guardarPantalla();			 
		  }
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_titulo() {
		return tab_titulo;
	}

	public void setTab_titulo(Tabla tab_titulo) {
		this.tab_titulo = tab_titulo;
	}

	public Tabla getTab_detalle_titulo() {
		return tab_detalle_titulo;
	}

	public void setTab_detalle_titulo(Tabla tab_detalle_titulo) {
		this.tab_detalle_titulo = tab_detalle_titulo;
	}
	
	

	public SeleccionTabla getSel_clientes() {
		return sel_clientes;
	}

	public void setSel_clientes(SeleccionTabla sel_clientes) {
		this.sel_clientes = sel_clientes;
	}

	public SeleccionTabla getSel_comp_vencidos() {
		return sel_comp_vencidos;
	}

	public void setSel_comp_vencidos(SeleccionTabla sel_comp_vencidos) {
		this.sel_comp_vencidos = sel_comp_vencidos;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	public Map getMap_parametros() {
		return map_parametros;
	}

	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}

	public Texto getTxt_nro_orden() {
		return txt_nro_orden;
	}

	public void setTxt_nro_orden(Texto txt_nro_orden) {
		this.txt_nro_orden = txt_nro_orden;
	}

	public Dialogo getDia_nro_orden() {
		return dia_nro_orden;
	}

	public void setDia_nro_orden(Dialogo dia_nro_orden) {
		this.dia_nro_orden = dia_nro_orden;
	}



}
