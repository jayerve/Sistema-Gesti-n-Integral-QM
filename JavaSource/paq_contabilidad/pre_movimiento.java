package paq_contabilidad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import paq_bodega.ejb.ServicioBodega; 
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
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

public class pre_movimiento extends Pantalla{
	
	private Tabla tab_movimiento=new Tabla();
	private Tabla tab_detalle_movimiento=new Tabla();
	private Tabla tab_pre_mensual=new Tabla(); 
	private SeleccionTabla sel_asientos = new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Combo com_lugar_aplica=new Combo();
	private Combo com_tipo_concepto= new Combo();
	private AutoCompletar aut_catalogo=new AutoCompletar();
	private AutoCompletar aut_proveedor = new AutoCompletar();
	private Texto txt_valor = new Texto();
	private Check chk_transferencia=new Check();
	private String p_modulo_factruracion="";
	private String p_modulo_contabilidad="";
	private String empleado;

	private String str_asiento_seleccionado="";
	private String p_debe="";
	private String p_haber="";	
	private Dialogo dia_movimientos=new Dialogo();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	
	private Confirmar con_generar_apertura=new Confirmar();
	private Confirmar con_generar_cierre=new Confirmar();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	
	public pre_movimiento ()
	{
		TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from now())");		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}
		
		p_modulo_factruracion=utilitario.getVariable("p_modulo_facturacion");
		p_debe=utilitario.getVariable("p_gen_lugar_aplica_debe");
		p_haber=utilitario.getVariable("p_gen_lugar_aplica_haber");		
		p_modulo_contabilidad=utilitario.getVariable("p_modulo_contabilidad");
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para generar asientos contables de ingreso");
			return;
		}

		tab_movimiento.setId("tab_movimiento");
		tab_movimiento.setHeader("MOVIMIENTOS");
		tab_movimiento.setTabla("cont_movimiento", "ide_comov", 1);
		tab_movimiento.setCondicion("ide_geani in (select ide_geani from gen_anio where activo_geani=true and bloqueado_geani=false)");
		tab_movimiento.setCampoOrden("ide_comov desc");
		tab_movimiento.getColumna("ide_gemod").setCombo("gen_modulo", "ide_gemod", "detalle_gemod", "");
		tab_movimiento.getColumna("ide_cotim").setCombo("cont_tipo_movimiento", "ide_cotim", "detalle_cotim", "");
		tab_movimiento.getColumna("ide_cotia").setCombo("cont_tipo_asiento", "ide_cotia", "detalle_cotia", "");
		tab_movimiento.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_movimiento.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_movimiento.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		//tab_movimiento.getColumna("ide_gtemp").setLectura(true);
		tab_movimiento.getColumna("ide_gtemp").setAutoCompletar();
		tab_movimiento.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_movimiento.getColumna("ide_geedp").setAutoCompletar();
		tab_movimiento.getColumna("ide_geedp").setLectura(true);
		tab_movimiento.getColumna("mov_fecha_comov").setValorDefecto(utilitario.getFechaActual());
		tab_movimiento.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_movimiento.getColumna("ide_geani").setLectura(true);
		tab_movimiento.setCondicion("ide_geani=-1"); 
		tab_movimiento.getColumna("ide_tecpo").setLectura(true);
		tab_movimiento.getColumna("activo_comov").setValorDefecto("false");
		tab_movimiento.getColumna("activo_comov").setLectura(true);
		tab_movimiento.getColumna("activo_comov").setNombreVisual("MAYORIZADO:");
		tab_movimiento.setTipoFormulario(true);
		tab_movimiento.getGrid().setColumns(4);
		tab_movimiento.agregarRelacion(tab_detalle_movimiento);
		tab_movimiento.dibujar();
		PanelTabla pat_movimiento=new PanelTabla();
		pat_movimiento.setPanelTabla(tab_movimiento);
		
		/////detalle movinto
		tab_detalle_movimiento.setId("tab_detalle_movimiento");
		tab_detalle_movimiento.setHeader("DETALLE DE MOVIMIENTO");
		tab_detalle_movimiento.setTabla("cont_detalle_movimiento", "ide_codem", 2);
		//tab_detalle_movimiento.getColumna("ide_prcla").setCombo(ser_Presupuesto.getCatalogoPresupuestario("true,false"));
		//tab_detalle_movimiento.getColumna("ide_prcla").setAutoCompletar();
		//tab_detalle_movimiento.getColumna("ide_prpro").setCombo("pre_programa", "ide_prpro", "cod_programa_prpro", "");
		tab_detalle_movimiento.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_detalle_movimiento.getColumna("ide_cocac").setAutoCompletar();
		
		tab_detalle_movimiento.getColumna("activo_codem").setLectura(true);
		tab_detalle_movimiento.getColumna("activo_codem").setValorDefecto("true");
		tab_detalle_movimiento.getColumna("haber_codem").setMetodoChange("calcularTotal");			
		tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem");			
		tab_detalle_movimiento.getColumna("debe_codem").setMetodoChange("calcularTotal");			
		tab_detalle_movimiento.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_detalle_movimiento.getColumna("ide_gelua").setMetodoChange("lugarAplica");
		tab_detalle_movimiento.getColumna("ide_prcla").setVisible(false);		
		tab_detalle_movimiento.getColumna("ide_prpro").setVisible(false);		
		
		tab_detalle_movimiento.agregarRelacion(tab_pre_mensual);
		tab_detalle_movimiento.getGrid().setColumns(4);
		tab_detalle_movimiento.dibujar();
		PanelTabla pat_detalle_movimiento=new PanelTabla();
		pat_detalle_movimiento.setPanelTabla(tab_detalle_movimiento);
		
		tab_pre_mensual.setId("tab_pre_mensual");
        tab_pre_mensual.setHeader("EJECUCION PRESUPUESTARIA");
        tab_pre_mensual.setTabla("pre_mensual", "ide_prmen", 3);
        tab_pre_mensual.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento", "ide_prfuf","detalle_prfuf","");
        tab_pre_mensual.getColumna("ide_prfuf").setNombreVisual("FUENTE FINANCIAMIENTO");
        tab_pre_mensual.getColumna("ide_pranu").setCombo(ser_Tesoreria.getCuentaPresupuestariaMov());
        tab_pre_mensual.getColumna("ide_pranu").setNombreVisual("CUENTA PRESUPUESTARIA");
        //tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
        //tab_pre_mensual.getColumna("ide_comov").setVisible(false);
        //tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
        //tab_pre_mensual.getColumna("ide_prtra").setVisible(false);
        tab_pre_mensual.getColumna("devengado_prmen").setNombreVisual("DEVENGADO");
        tab_pre_mensual.getColumna("devengado_prmen").setMetodoChange("calcularTotal2");	
        tab_pre_mensual.getColumna("cobrado_prmen").setNombreVisual("COBRADO");
        tab_pre_mensual.getColumna("cobrado_prmen").setMetodoChange("calcularTotal2");	
        tab_pre_mensual.getColumna("pagado_prmen").setNombreVisual("PAGADO");
        tab_pre_mensual.getColumna("comprometido_prmen").setNombreVisual("COMPROMETIDO");
        tab_pre_mensual.getColumna("valor_anticipo_prmen").setNombreVisual("VALOR ANTICIPO");
        //tab_pre_mensual.getColumna("comprometido_prmen").setVisible(false);
        //tab_pre_mensual.getColumna("certificado_prmen").setVisible(false);
        //tab_pre_mensual.getColumna("ide_prcer").setVisible(false);
        //tab_pre_mensual.getColumna("ide_tecpo").setVisible(false);
        tab_pre_mensual.setCondicion("(abs(coalesce(certificado_prmen,0))+abs(coalesce(comprometido_prmen,0)))=0 "); 
        tab_pre_mensual.setColumnaSuma("devengado_prmen,cobrado_prmen,pagado_prmen,comprometido_prmen,valor_anticipo_prmen");
        tab_pre_mensual.dibujar();
        PanelTabla pat_pre_mensual= new PanelTabla();
        pat_pre_mensual.setPanelTabla(tab_pre_mensual);
		
		Division div_division =new Division();
		//div_division.dividir2(pat_movimiento, pat_detalle_movimiento, "50%", "H");
		div_division.dividir3(pat_movimiento, pat_detalle_movimiento, pat_pre_mensual, "50%", "30%", "H");
		agregarComponente(div_division);
		
		
		//Inicio Dialogo CREDITO
		dia_movimientos.setId("dia_movimientos");
		dia_movimientos.setTitle("GENERACION DE MOVIMIENTOS FINANCIEROS");
		dia_movimientos.setHeight("45%");
		dia_movimientos.setWidth("40%");
		
		
		//inicio del grid
		Grid gri_datos_asiento = new Grid();
		gri_datos_asiento.setColumns(2);
		gri_datos_asiento.getChildren().add(new Etiqueta("Proveedor: "));
		aut_proveedor.setId("aut_proveedor");
		aut_proveedor.setAutoCompletar(ser_Bodega.getProveedor("true,false"));
		gri_datos_asiento.getChildren().add(aut_proveedor);
		gri_datos_asiento.getChildren().add(new Etiqueta("Concepto de Pago: "));
		com_tipo_concepto.setId("com_tipo_concepto");
		com_tipo_concepto.setCombo(ser_Tesoreria.getConsultaTipoConcepto("true,false"));
		gri_datos_asiento.getChildren().add(com_tipo_concepto);
		gri_datos_asiento.getChildren().add(new Etiqueta("Cuenta Contable: "));
		aut_catalogo.setId("aut_catalogo");
		aut_catalogo.setAutoCompletar(ser_contabilidad.getCuentaContable("true,false"));
		gri_datos_asiento.getChildren().add(aut_catalogo);
		gri_datos_asiento.getChildren().add(new Etiqueta("Debe / Haber: "));
		com_lugar_aplica.setId("com_lugar_aplica");
		com_lugar_aplica.setCombo("Select ide_gelua,detalle_gelua from gen_lugar_aplica");
		gri_datos_asiento.getChildren().add(com_lugar_aplica);
		gri_datos_asiento.getChildren().add(new Etiqueta("Valor: "));
		txt_valor.setId("txt_valor");
		txt_valor.setSoloNumeros();
		gri_datos_asiento.getChildren().add(txt_valor);
		gri_datos_asiento.getChildren().add(new Etiqueta("Cuenta Transferencia: "));
		chk_transferencia.setId("chk_transferencia");
		gri_datos_asiento.getChildren().add(chk_transferencia);
		dia_movimientos.getBot_aceptar().setMetodo("generarTransacciones");
		dia_movimientos.getBot_cancelar().setMetodo("guardarCerrar");
		dia_movimientos.setDialogo(gri_datos_asiento);
		agregarComponente(dia_movimientos);
		
		Boton bot_generar_transacciones = new Boton();
		bot_generar_transacciones.setValue("Generar Transacciones");
		bot_generar_transacciones.setMetodo("generarTransacciones");
		bar_botones.agregarBoton(bot_generar_transacciones);
		
		Boton bot_generar = new Boton();
		bot_generar.setValue("Generar Archivo Apertura");
		bot_generar.setMetodo("generar_archivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		Boton bot_generarAsiento = new Boton();
		bot_generarAsiento.setValue("Generar Apertura");
		bot_generarAsiento.setMetodo("generar_asiento_apertura");
		bar_botones.agregarBoton(bot_generarAsiento);
		
		Boton bot_generarAsientoC = new Boton();
		bot_generarAsientoC.setValue("Generar Cierre");
		bot_generarAsientoC.setMetodo("generar_asiento_cierre");
		bar_botones.agregarBoton(bot_generarAsientoC);
		
		con_generar_apertura.setId("con_generar_apertura");
		con_generar_apertura.setMessage("¿Esta Seguro de Generar el Asiento de Apertura? Recuerde generar todos los estados financieros del año anterior antes de continuar.");
		con_generar_apertura.setTitle("Confirmación de Generación");
		con_generar_apertura.getBot_aceptar().setMetodo("generar_asiento_apertura");
		agregarComponente(con_generar_apertura);
		
		con_generar_cierre.setId("con_generar_cierre");
		con_generar_cierre.setMessage("¿Esta Seguro de Generar el Asiento de Cierre? Recuerde generar todos los estados financieros del año anterior antes de continuar.");
		con_generar_cierre.setTitle("Confirmación de Generación");
		con_generar_cierre.getBot_aceptar().setMetodo("generar_asiento_cierre");
		agregarComponente(con_generar_cierre);
		
		Boton bot_clonar_asiento = new Boton();
		bot_clonar_asiento.setValue("Clonar Asiento");
		bot_clonar_asiento.setMetodo("clonarAsiento");
		bar_botones.agregarBoton(bot_clonar_asiento);
		inicializaAsientos();
	}
	
	public void inicializaAsientos(){
		sel_asientos.setId("sel_asientos");
		sel_asientos.setTitle("Seleccione el Asiento Contable a Clonar");
		sel_asientos.setSeleccionTabla(ser_contabilidad.getMovimientosContables("-1", "-1", "-1"), "ide_comov");
		sel_asientos.getTab_seleccion().getColumna("ide_geani").setVisible(false);
		sel_asientos.getTab_seleccion().getColumna("ide_gemes").setVisible(false);
		sel_asientos.getTab_seleccion().getColumna("nro_comprobante_comov").setFiltroContenido();
		sel_asientos.getTab_seleccion().getColumna("detalle_comov").setFiltroContenido();
		sel_asientos.setFooter("NO SE CLONARÁ LA EJECUCIÓN PRESUPUESTARIA...!");
		sel_asientos.setRadio();
		sel_asientos.getTab_seleccion().ejecutarSql();
		sel_asientos.getBot_aceptar().setMetodo("clonarAsiento");
		agregarComponente(sel_asientos);
	}
	
	public void clonarAsiento(){
		
		if(sel_asientos.isVisible()){
			if(sel_asientos.getValorSeleccionado()!=null)
			{
				str_asiento_seleccionado=sel_asientos.getValorSeleccionado();	
				TablaGenerica tab_asiento_seleccionado= utilitario.consultar("select * from cont_movimiento where ide_comov ="+str_asiento_seleccionado);
				TablaGenerica tab_det_asiento_seleccionado = utilitario.consultar("select * from cont_detalle_movimiento where ide_comov ="+str_asiento_seleccionado);
				//str_tipo_asiento = tab_asiento_seleccionado.getValor("ide_coest");

				for (int i = 0; i < tab_asiento_seleccionado.getTotalFilas(); i++) {
					tab_movimiento.insertar();
					tab_movimiento.setValor("ide_cotim", tab_asiento_seleccionado.getValor(i, "ide_cotim"));	
					tab_movimiento.setValor("ide_gemes", utilitario.getMes(utilitario.getFechaActual())+"");	
					tab_movimiento.setValor("ide_cotia", tab_asiento_seleccionado.getValor(i, "ide_cotia"));
					tab_movimiento.setValor("ide_geare", tab_asiento_seleccionado.getValor(i, "ide_geare"));
					//tab_movimiento.setValor("ide_cotim", tab_asiento_seleccionado.getValor(i, "mov_fecha_comov"));
					tab_movimiento.setValor("detalle_comov", tab_asiento_seleccionado.getValor(i, "detalle_comov"));
					//tab_movimiento.setValor("ide_cotim", tab_asiento_seleccionado.getValor(i, "nro_comprobante_comov"));
					tab_movimiento.setValor("ide_tecpo", tab_asiento_seleccionado.getValor(i, "ide_tecpo"));
					tab_movimiento.setValor("ide_geani", com_anio.getValue()+"");
					tab_movimiento.setValor("ide_gemod", tab_asiento_seleccionado.getValor(i, "ide_gemod"));
					tab_movimiento.setValor("ide_conac", tab_asiento_seleccionado.getValor(i, "ide_conac"));
					tab_movimiento.setValor("ide_gtemp", empleado);
				}

				for (int i = 0; i < tab_det_asiento_seleccionado.getTotalFilas(); i++) {
					tab_detalle_movimiento.insertar();					
					tab_detalle_movimiento.setValor("debe_codem", tab_det_asiento_seleccionado.getValor(i, "debe_codem"));
					tab_detalle_movimiento.setValor("detalle_codem", tab_det_asiento_seleccionado.getValor(i, "detalle_codem"));
					tab_detalle_movimiento.setValor("haber_codem", tab_det_asiento_seleccionado.getValor(i, "haber_codem"));
					tab_detalle_movimiento.setValor("ide_cocac", tab_det_asiento_seleccionado.getValor(i, "ide_cocac"));
					tab_detalle_movimiento.setValor("ide_gelua", tab_det_asiento_seleccionado.getValor(i, "ide_gelua"));

				}

				sel_asientos.cerrar();
				
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else {
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
				return;
			}
			
			sel_asientos.getTab_seleccion().setSql(ser_contabilidad.getMovimientosContables(com_anio.getValue()+"", "-1", "-1"));
			sel_asientos.getTab_seleccion().ejecutarSql();
			sel_asientos.getBot_aceptar().setMetodo("clonarAsiento");
			sel_asientos.dibujar();
		}
		
	}
	
	public void guardarCerrar(){
		//guardarPantalla();
		dia_movimientos.cerrar();
	}
	
	// generara transacciones contables 
	public void generarTransacciones(){
		double dou_valor_debe=0;
		double dou_valor_haber=0;
		String str_tipo_concepto=null;
		System.out.println("entre a fromar "+tab_movimiento.getValor("ide_comov"));
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		if(tab_movimiento.getValor("ide_comov")==null){
			utilitario.agregarMensajeError("No se puede insertar", "Debe Guardaruna Cabecera de Movimiento Contable");
			return;
		}
		
		//if(com_tipo_concepto.getValue().toString()!=null){
			//str_tipo_concepto=com_tipo_concepto.getValue().toString();
		//}
		
		if(dia_movimientos.isVisible()){
			
			tab_detalle_movimiento.insertar();
			if(com_lugar_aplica.getValue().equals(p_debe)){
				dou_valor_debe=pckUtilidades.CConversion.CDbl_2(txt_valor.getValue().toString());
			}
			if(com_lugar_aplica.getValue().equals(p_haber)){
				dou_valor_haber=pckUtilidades.CConversion.CDbl_2(txt_valor.getValue().toString());
			}
			
			tab_detalle_movimiento.setValor("ide_gelua", com_lugar_aplica.getValue().toString());
			tab_detalle_movimiento.setValor("debe_codem", dou_valor_debe+"");
			tab_detalle_movimiento.setValor("haber_codem", dou_valor_haber+"");
			tab_detalle_movimiento.setValor("ide_cocac",aut_catalogo.getValor());
			tab_detalle_movimiento.setValor("ide_tepro", aut_proveedor.getValor());
			tab_detalle_movimiento.setValor("ide_tetic",com_tipo_concepto.getValue()+"" );
			tab_detalle_movimiento.setValor("ide_comov", tab_movimiento.getValor("ide_comov"));
			tab_detalle_movimiento.setValor("transferencia_codem", chk_transferencia.getValue().toString());

			//tab_detalle_movimiento.guardar();
			tab_detalle_movimiento.sumarColumnas();
			utilitario.addUpdate("tab_detalle_movimiento");

		}
		else{
			dia_movimientos.dibujar();
		}
	}
	
	public void generar_asiento_apertura(){
		
		if(con_generar_apertura.isVisible())
		{
			con_generar_apertura.cerrar();
			
			double dou_valor_debe=0;
			double dou_valor_haber=0;
			
			System.out.println("generar asiento de apertura "+tab_movimiento.getValor("ide_comov"));
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un Año");
				return;
			}
			if(tab_movimiento.getValor("ide_comov")==null){
				utilitario.agregarMensajeError("No se puede insertar", "Debe Guardar una Cabecera de Movimiento Contable");
				return;
			}
			
			if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_cotim"))!=6){
				utilitario.agregarMensajeError("No se puede insertar", "El tipo de movimiento contable no es de tipo INICIAL");
				return;
			}
			
			if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_cotia"))!=4){
				utilitario.agregarMensajeError("No se puede insertar", "El movimiento contable no es de tipo asiento APERTURA");
				return;
			}
			
			TablaGenerica tab_validaApertura=ser_contabilidad.getTablaValidaAsientoApertura(com_anio.getValue().toString());
			
			//if(!tab_validaApertura.isEmpty()){
			if(tab_validaApertura.getTotalFilas()>1){
				utilitario.agregarMensajeError("No se puede generar el asiento", "Ya existe un movimiento contable de apertura para el año seleccionado.");
				return;
			}
			
			TablaGenerica tab_asientoApertura=ser_contabilidad.getTablaDetalleAsientoApertura();
			
			if(tab_asientoApertura.isEmpty()){
				if(tab_asientoApertura.getTotalFilas()>1)
				{
					utilitario.agregarMensajeError("No se puede generar el asiento", "No existen registros para la generación del asiento favor genere el Balance General.");
					return;
				}
			}
	
			for(int i=0;i<tab_asientoApertura.getTotalFilas();i++){
				
				dou_valor_debe += pckUtilidades.CConversion.CDbl_2(tab_asientoApertura.getValor(i, "debe_saldo"));
				dou_valor_haber += pckUtilidades.CConversion.CDbl_2(tab_asientoApertura.getValor(i, "haber_saldo"));
	
				tab_detalle_movimiento.insertar();
				
				tab_detalle_movimiento.setValor("ide_comov", tab_movimiento.getValor("ide_comov"));
				tab_detalle_movimiento.setValor("ide_gelua", pckUtilidades.CConversion.CDbl_2(tab_asientoApertura.getValor(i, "debe_saldo"))>0?"1":"2" );
				tab_detalle_movimiento.setValor("debe_codem", tab_asientoApertura.getValor(i, "debe_saldo"));
				tab_detalle_movimiento.setValor("haber_codem", tab_asientoApertura.getValor(i, "haber_saldo"));
				tab_detalle_movimiento.setValor("ide_cocac", tab_asientoApertura.getValor(i, "ide_cocac"));			
			}
	
			//tab_detalle_movimiento.guardar();
			tab_detalle_movimiento.sumarColumnas();
			utilitario.addUpdate("tab_detalle_movimiento");
			
			if(dou_valor_debe!=dou_valor_haber){
				utilitario.agregarMensajeError("Error en diferencias", "El asiento no esta cuadrado.!!!");
			}
			utilitario.agregarMensajeInfo("Detalles generados", "Favor revisar los detalles de los asientos.");
			
		}
		else
		{
			con_generar_apertura.dibujar();
		}
	}

	public void generar_asiento_cierre(){
		
		if(con_generar_cierre.isVisible())
		{
			con_generar_cierre.cerrar();
			
			double dou_valor_debe=0;
			double dou_valor_haber=0;
			
			System.out.println("generar asiento de cierre "+tab_movimiento.getValor("ide_comov"));
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un Año");
				return;
			}
			if(tab_movimiento.getValor("ide_comov")==null){
				utilitario.agregarMensajeError("No se puede insertar", "Debe Guardar una Cabecera de Movimiento Contable");
				return;
			}
			
			if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_cotim"))!=27){
				utilitario.agregarMensajeError("No se puede insertar", "El tipo de movimiento contable no es de tipo FINANCIERO");
				return;
			}
			
			if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_cotia"))!=2){
				utilitario.agregarMensajeError("No se puede insertar", "El movimiento contable no es de tipo asiento CIERRE");
				return;
			}
			
			TablaGenerica tab_validaCierre=ser_contabilidad.getTablaValidaAsientoCierre(com_anio.getValue().toString());
			
			if(!tab_validaCierre.isEmpty()){
				if(tab_validaCierre.getTotalFilas()>1)
				{
					utilitario.agregarMensajeError("No se puede generar el asiento", "Ya existe un movimiento contable de cierre para el año seleccionado.");
					return;
				}
			}
			
			////////
			TablaGenerica tab_asientoCierre=ser_contabilidad.getTablaDetalleAsientoCierre();
			
			if(tab_asientoCierre.isEmpty()){
				utilitario.agregarMensajeError("No se puede generar el asiento", "No existen registros para la generación del asiento favor genere el Balance General.");
				return;
			}
			////////
			
			for(int i=0;i<tab_asientoCierre.getTotalFilas();i++){
				
				dou_valor_debe += pckUtilidades.CConversion.CDbl_2(tab_asientoCierre.getValor(i, "debe_saldo"));
				dou_valor_haber += pckUtilidades.CConversion.CDbl_2(tab_asientoCierre.getValor(i, "haber_saldo"));
	
				tab_detalle_movimiento.insertar();
				
				tab_detalle_movimiento.setValor("ide_comov", tab_movimiento.getValor("ide_comov"));
				tab_detalle_movimiento.setValor("ide_gelua", pckUtilidades.CConversion.CDbl_2(tab_asientoCierre.getValor(i, "debe_saldo"))>0?"1":"2" );
				tab_detalle_movimiento.setValor("debe_codem", tab_asientoCierre.getValor(i, "debe_saldo"));
				tab_detalle_movimiento.setValor("haber_codem", tab_asientoCierre.getValor(i, "haber_saldo"));
				tab_detalle_movimiento.setValor("ide_cocac", tab_asientoCierre.getValor(i, "ide_cocac"));			
			}
	
			//tab_detalle_movimiento.guardar();
			tab_detalle_movimiento.sumarColumnas();
			utilitario.addUpdate("tab_detalle_movimiento");
			
			if(dou_valor_debe!=dou_valor_haber){
				utilitario.agregarMensajeError("Error en diferencias", "El asiento no esta cuadrado.!!!");
			}
			utilitario.agregarMensajeInfo("Detalles generados", "Favor revisar los detalles de los asientos.");
			
		}
		else
		{
			con_generar_cierre.dibujar();
		}
	}
	
	public void generar_archivo(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_cotia"))!=4){
			utilitario.agregarMensajeInfo("El asiento no es de tipo APERTURA.", "");
			return;			
		}
		
		
		TablaGenerica tab_asientoApertura=ser_contabilidad.getTablaAsientoAperturaArchivoPlano(tab_movimiento.getValor("ide_comov"));
		
		if(tab_asientoApertura.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		TablaGenerica tab_anio =utilitario.consultar("SELECT ide_geani, detalle_geani FROM gen_anio where ide_geani="+com_anio.getValue());
		
		String anio=tab_anio.getValor("detalle_geani");
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= "ASIENTO_APERTURA_"+utilitario.getNombreMes(1)+anio;
			String path= extContext.getRealPath("/");	
			String[] cuentaContable;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			
			for(int i=0;i<tab_asientoApertura.getTotalFilas();i++){

				StringBuilder str_detalle=new StringBuilder();
				str_detalle.append( pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tab_asientoApertura.getValor(i, "periodo"),1),2)).append("|");

				cuentaContable=tab_asientoApertura.getValor(i, "cuenta").split(Pattern.quote("."));

				str_detalle.append(cuentaContable[0]).append("|");
				str_detalle.append(cuentaContable[1]).append("|");
				str_detalle.append(cuentaContable[2]).append("|");
				
				str_detalle.append(tab_asientoApertura.getValor(i, "debe_codem")).append("|");
				str_detalle.append(tab_asientoApertura.getValor(i, "haber_codem"));

				String str_spi_encr=str_detalle.toString();
			
				escribir.write(str_spi_encr.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo error: "+ e);
		}	
		
	}
	
	//// lugar aplica
	public void lugarAplica(AjaxBehaviorEvent evt){

		tab_detalle_movimiento.modificar(evt);
		if (tab_detalle_movimiento.getValor("ide_gelua") != null) {
		    if(tab_detalle_movimiento.getValor("ide_gelua").equals(p_debe)){
			tab_detalle_movimiento.setValor("haber_codem", "0");
		}
		    if(tab_detalle_movimiento.getValor("ide_gelua").equals(p_haber)){
			tab_detalle_movimiento.setValor("debe_codem", "0");
		}
		}
		utilitario.addUpdate("debe_codem,haber_codem");

	}
	////metodo año
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_movimiento.setCondicion("ide_geani="+com_anio.getValue());
			tab_movimiento.ejecutarSql();
			tab_detalle_movimiento.ejecutarValorForanea(tab_movimiento.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	///sacar valores
	
	public void calcularTotal(AjaxBehaviorEvent evt){
		tab_detalle_movimiento.modificar(evt);
	
		tab_detalle_movimiento.sumarColumnas();
		utilitario.addUpdate("tab_detalle_movimiento");
	}
	
	public void calcularTotal2(AjaxBehaviorEvent evt){
		tab_pre_mensual.modificar(evt);
	
		tab_pre_mensual.sumarColumnas();
		utilitario.addUpdate("tab_pre_mensual");
	}
	
	public boolean validar_movimiento_bloqueado(String fecha)
	{
		TablaGenerica tab_mov = utilitario.consultar("SELECT ide_cocim, detalle_cocim, activo_cocim FROM cont_cierre_movimiento " +
								" where bloqueado_cocim=true and ide_gemes=extract(month from cast('"+fecha+"' as date)) " +
								" and ide_geani in (SELECT ide_geani FROM gen_anio where cast(detalle_geani as integer) = extract(year from cast('"+fecha+"' as date)))");
		tab_mov.imprimirSql();
		if(tab_mov.getTotalFilas()>0)
			return true;
		
		return false;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		else if (tab_movimiento.isFocus()) {
			tab_movimiento.insertar();
			tab_movimiento.setValor("ide_geani", com_anio.getValue()+"");
			tab_movimiento.setValor("ide_gtemp", empleado );
			tab_movimiento.setValor("nro_comprobante_comov", ser_contabilidad.numeroSecuencial(p_modulo_contabilidad));
            utilitario.addUpdateTabla(tab_movimiento, "ide_geani", "");
		}
		
		else if (tab_detalle_movimiento.isFocus()) {
			tab_detalle_movimiento.insertar();
		}
		else if(tab_pre_mensual.isFocus()){
        	tab_pre_mensual.insertar();
        	tab_pre_mensual.setValor("ide_codem", tab_detalle_movimiento.getValor("ide_codem"));
        	tab_pre_mensual.setValor("comprobante_prmen", tab_movimiento.getValor("nro_comprobante_comov"));
        	tab_pre_mensual.setValor("fecha_ejecucion_prmen", tab_movimiento.getValor("mov_fecha_comov"));
        	tab_pre_mensual.setValor("devengado_prmen", "0.00");
        	tab_pre_mensual.setValor("cobrado_prmen", "0.00");
        	tab_pre_mensual.setValor("cobradoc_prmen", "0.00");
        	tab_pre_mensual.setValor("pagado_prmen", "0.00");
        	tab_pre_mensual.setValor("comprometido_prmen", "0.00");
        	tab_pre_mensual.setValor("valor_anticipo_prmen", "0.00");
        	tab_pre_mensual.setValor("certificado_prmen", "0.00");
        }    
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		ser_contabilidad.limpiarAcceso("cont_movimiento");
		ser_contabilidad.limpiarAcceso("cont_detalle_movimiento");
		ser_contabilidad.limpiarAcceso("pre_mensual");
		
		TablaGenerica tab_anio =utilitario.consultar("select ide_geani,activo_geani,bloqueado_geani from gen_anio where ide_geani ="+com_anio.getValue());
		TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from cast('"+tab_movimiento.getValor("mov_fecha_comov")+"' as date))");		
		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}
	
		if(!tab_anio.getValor("activo_geani").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Inactivo");
			return;
		}
		
		if(tab_anio.getValor("bloqueado_geani").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El Año se encuentra Bloqueado");
			return;
		}
		
		if(tab_movimiento.getValor("activo_comov").equals("true")){
			utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra mayorizado no se puede modificar");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_geedp"))>0){
			utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra aprobado.");
			return;
		}
		
		if(validar_movimiento_bloqueado(tab_movimiento.getValor("mov_fecha_comov"))){
			utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
			return;
		}
		
		
		if(pckUtilidades.CConversion.CInt(empleado)!=1170 || pckUtilidades.CConversion.CInt(empleado)!=22) //Armijos Lissete		
			if(pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_gtemp"))!=pckUtilidades.CConversion.CInt(empleado)){
				utilitario.agregarMensajeInfo("Registro no Editable", "El asiento se encuentra generado por otro usuario.");
				return;
			}
		
		double dou_valor_debe=0;
		double dou_valor_haber=0;
		
		dou_valor_debe=pckUtilidades.CConversion.CDbl_2(tab_detalle_movimiento.getSumaColumna("debe_codem"));
		dou_valor_haber=pckUtilidades.CConversion.CDbl_2(tab_detalle_movimiento.getSumaColumna("haber_codem"));
		
		System.out.println("Guadar movimiento: debe: "+dou_valor_debe+" haber: "+dou_valor_haber);
			
		if(dou_valor_debe!=dou_valor_haber){
			utilitario.agregarMensajeError("Error al guardar", "Error en diferencias El asiento no esta cuadrado.!!!");
			//return;
		}
		
		
		if (tab_movimiento.guardar()){
			if (tab_detalle_movimiento.guardar())
			  if( tab_pre_mensual.guardar())
				 guardarPantalla();
		}
		//tab_detalle_movimiento.sumarColumnas();
		//utilitario.addUpdate("tab_detalle_movimiento");
		//guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
	}
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}


	@Override
	public void aceptarReporte() {
		Locale locale=new Locale("es","ES");

		if (rep_reporte.getReporteSelecionado().equals("Comprobante Pago")){ 
								
					if (rep_reporte.isVisible()){
						p_parametros=new HashMap();				
						rep_reporte.cerrar();				
						//		p_parametros.put("IDE_GTEMP",Long.parseLong(tab_anticipo.getValor("IDE_GTEMP")));
					TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+tab_movimiento.getValor("ide_gtemp"));
					if(tab_usuario.isEmpty())
						tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.nick_usua like '"+tab_movimiento.getValor("usuario_ingre")+"'");
					
						TablaGenerica tab_valor = utilitario.consultar(ser_Tesoreria.getConsulValorPagoContabilidad(tab_movimiento.getValor("ide_comov")));
						p_parametros.put("titulo", "EMGIRS - EP");
					p_parametros.put("p_contador_general", tab_usuario.getValor("nom_usua"));
					p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
					p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	
						p_parametros.put("p_cuota_mensual",utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_valor.getValor("valor"),2)));
						p_parametros.put("p_ide_comov",pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_comov")));
						
						p_parametros.put("REPORT_LOCALE", locale);
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						sef_reporte.dibujar();
					
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del movimiento");
			}
		}
		
		if (rep_reporte.getReporteSelecionado().equals("Movimiento Contable")){ 
			
			if (rep_reporte.isVisible()){
				TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+utilitario.getVariable("ide_usua"));
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo", "EMGIRS - EP");
				p_parametros.put("p_contador_general", tab_usuario.getValor("nom_usua"));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	
				p_parametros.put("p_ide_comov",pckUtilidades.CConversion.CInt(tab_movimiento.getValor("ide_comov")));
				p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_usua")));
				p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_empr")));
				p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_sucu")));
				p_parametros.put("REPORT_LOCALE", locale);
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del movimiento");
		}
	}
	
		
	}
	public Tabla getTab_movimiento() {
		return tab_movimiento;
	}

	public void setTab_movimiento(Tabla tab_movimiento) {
		this.tab_movimiento = tab_movimiento;
	}

	public Tabla getTab_detalle_movimiento() {
		return tab_detalle_movimiento;
	}

	public void setTab_detalle_movimiento(Tabla tab_detalle_movimiento) {
		this.tab_detalle_movimiento = tab_detalle_movimiento;
	}
	public Dialogo getDia_movimientos() {
		return dia_movimientos;
	}
	public void setDia_movimientos(Dialogo dia_movimientos) {
		this.dia_movimientos = dia_movimientos;
	}
	public AutoCompletar getAut_catalogo() {
		return aut_catalogo;
	}
	public void setAut_catalogo(AutoCompletar aut_catalogo) {
		this.aut_catalogo = aut_catalogo;
	}
	public AutoCompletar getAut_proveedor() {
		return aut_proveedor;
	}
	public void setAut_proveedor(AutoCompletar aut_proveedor) {
		this.aut_proveedor = aut_proveedor;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}
	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}
	public Map getP_parametros() {
		return p_parametros;
	}
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Tabla getTab_pre_mensual() {
		return tab_pre_mensual;
	}
	public void setTab_pre_mensual(Tabla tab_pre_mensual) {
		this.tab_pre_mensual = tab_pre_mensual;
	}

	public Confirmar getCon_generar_apertura() {
		return con_generar_apertura;
	}

	public void setCon_generar_apertura(Confirmar con_generar_apertura) {
		this.con_generar_apertura = con_generar_apertura;
	}

	public Confirmar getCon_generar_cierre() {
		return con_generar_cierre;
	}

	public void setCon_generar_cierre(Confirmar con_generar_cierre) {
		this.con_generar_cierre = con_generar_cierre;
	}

	public SeleccionTabla getSel_asientos() {
		return sel_asientos;
	}

	public void setSel_asientos(SeleccionTabla sel_asientos) {
		this.sel_asientos = sel_asientos;
	}
	
	

}
