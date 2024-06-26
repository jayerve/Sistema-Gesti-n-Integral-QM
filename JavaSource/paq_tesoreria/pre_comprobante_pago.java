package paq_tesoreria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.TablaGrid;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_comprobante_pago extends Pantalla {

	private Tabla tab_tablaXls = new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle_movimiento =new Tabla();
    private Tabla tab_retencion =new Tabla();
    private Tabla tab_detalle_retencion=new Tabla();
    private Tabla tab_pre_mensual=new Tabla();    
    private SeleccionTabla set_solicitud=new SeleccionTabla();
    private SeleccionTabla set_liquidacion=new SeleccionTabla();
    private SeleccionTabla set_tramite=new SeleccionTabla();
    private SeleccionTabla set_contabilizar=new SeleccionTabla();
    private SeleccionTabla set_anticipo_empleado=new SeleccionTabla();
    private Combo com_tipo_concepto = new Combo();
    private Calendario cal_fecha_inicial = new Calendario();
    private SeleccionTabla set_impuesto=new SeleccionTabla();
    private SeleccionTabla set_retencion=new SeleccionTabla();
    private SeleccionTabla set_presupuesto_devengado=new SeleccionTabla();
    public Radio rad_imprimir= new Radio();
    private Dialogo dia_tipo_anticipo=new Dialogo();
    private Dialogo dia_anticipo=new Dialogo();
	private Confirmar con_guardar=new Confirmar();
	private Dialogo dia_presupuesto = new  Dialogo();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Combo com_anio=new Combo();
	private Texto txt_monto = new Texto();

	public static String par_impuesto_renta;
	public static String par_impuesto_iva;
	public static String par_movimiento_devengado;
	public static String par_debe;
	public static String par_haber;
	public static String par_valor_devengar;
	public static String par_lugar_aplica_banco;
	public static String par_cuenta_banco;
	public static String par_lugar_aplica_iva;
	public static String par_cuenta_iva;
	public static String par_lugar_aplica_iva_cierra;
	public static String par_cuenta_iva_cierra;
	public static String par_sec_comprobante_pago;
	public static String par_anio_vigente;
	public static String par_asiento_anticipo;
	public static String par_asiento_otros_anticipo;
	public static String par_estado_devengado;
	private String empleado;
	
	public String ide_adlic;
	private int tipoRecalculo=0;
	
	public static double par_iva;

    @EJB
    private ServicioAdquisicion ser_Adquisicion=(ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);
    @EJB
    private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
    @EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    @EJB
    private ServicioPresupuesto ser_Presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
    @EJB
    private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
    @EJB
    private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    @EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
    
    public pre_comprobante_pago(){
    	
    	TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from now())");		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}
    	
    	//parametros
    	par_impuesto_iva=utilitario.getVariable("p_tes_impuesto_iva");
		par_impuesto_renta=utilitario.getVariable("p_tes_impuesto_renta");
		par_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva"));
		par_movimiento_devengado=utilitario.getVariable("p_mov_devengado");
		par_debe=utilitario.getVariable("p_gen_lugar_aplica_debe");
		par_haber=utilitario.getVariable("p_gen_lugar_aplica_haber");
		par_valor_devengar=utilitario.getVariable("p_valor_devenga");
		par_lugar_aplica_banco=utilitario.getVariable("p_lugar_ejecuta_banco");
		par_cuenta_banco=utilitario.getVariable("p_cuenta_banco");
		par_lugar_aplica_iva=utilitario.getVariable("p_lugar_ejecuta_iva");
		par_cuenta_iva=utilitario.getVariable("p_cuenta_iva");
		par_lugar_aplica_iva_cierra=utilitario.getVariable("p_lugar_ejecuta_iva_cierra");
		par_cuenta_iva_cierra=utilitario.getVariable("p_cuenta_iva_cierra");
		par_sec_comprobante_pago=utilitario.getVariable("p_modulo_comprobante_pago");
		par_anio_vigente=utilitario.getVariable("p_anio_vigente");
		par_asiento_anticipo=utilitario.getVariable("p_asiento_anticipo");
		par_estado_devengado=utilitario.getVariable("p_estado_devengado");
		par_asiento_otros_anticipo=utilitario.getVariable("p_asiento_otros_anticipos");
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado comprobante pago ide_gtemp: "+empleado +" - ide_usua: "+utilitario.getVariable("ide_usua"));
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro de Comprobantes de Pago");
			return;
		}

    	///tabuladores
    	Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
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

         
         tab_comprobante.setId("tab_comprobante");
         tab_comprobante.setHeader("COMPROBANTE DE PAGO");
         tab_comprobante.setTabla("tes_comprobante_pago", "ide_tecpo",  1);
         tab_comprobante.getColumna("IDE_COEST").setCombo("cont_estado","ide_coest","detalle_coest","");
         tab_comprobante.setCampoOrden("ide_tecpo desc");
         tab_comprobante.getColumna("IDE_PRTRA").setCombo(ser_Presupuesto.getTramite("true"));
         tab_comprobante.getColumna("IDE_PRTRA").setAutoCompletar();
         tab_comprobante.getColumna("IDE_PRTRA").setLectura(true);
         tab_comprobante.getColumna("IDE_COEST").setCombo("cont_estado","ide_coest","detalle_coest","");
         tab_comprobante.getColumna("IDE_TEPRO").setCombo(ser_Bodega.getProveedor("true,false"));
         tab_comprobante.getColumna("IDE_TEPRO").setAutoCompletar();
         tab_comprobante.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
         tab_comprobante.getColumna("IDE_GEDIP").setValorDefecto("57");
         tab_comprobante.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
         tab_comprobante.getColumna("IDE_GEEDP").setAutoCompletar();
         tab_comprobante.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
         //tab_comprobante.getColumna("ide_gtemp").setLectura(true);
         tab_comprobante.getColumna("ide_gtemp").setAutoCompletar();
         tab_comprobante.getColumna("IDE_ADSOC").setCombo(ser_Adquisicion.getSolicitudCompra("true"));
         tab_comprobante.getColumna("IDE_ADSOC").setAutoCompletar();
         tab_comprobante.getColumna("IDE_ADSOC").setLectura(true);
         tab_comprobante.getColumna("ide_tetic").setCombo("tes_tipo_concepto", "ide_tetic", "detalle_tetic,fecha_pago_tetic", "");
         tab_comprobante.getColumna("fecha_tecpo").setRequerida(true);
         tab_comprobante.getColumna("comprobante_egreso_tecpo").setRequerida(true);
         tab_comprobante.getColumna("detalle_tecpo").setRequerida(true);
         tab_comprobante.getColumna("aprobado_tecpo").setValorDefecto("false");
         tab_comprobante.getColumna("aprobado_tecpo").setLectura(true);
         tab_comprobante.getColumna("activo_tecpo").setValorDefecto("true");
         tab_comprobante.getColumna("activo_tecpo").setLectura(true);
         tab_comprobante.getColumna("fecha_tecpo").setRequerida(true);
         tab_comprobante.getColumna("fecha_tecpo").setValorDefecto(utilitario.getFechaActual()); //
         tab_comprobante.getColumna("subtotal_tecpo").setMetodoChange("valorPago");
         tab_comprobante.getColumna("valor_compra_tecpo").setEtiqueta();
         tab_comprobante.getColumna("valor_compra_tecpo").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
         tab_comprobante.getColumna("valor_iva_tecpo").setMetodoChange("valorPago");
         tab_comprobante.getColumna("valor_retencion_tecpo").setMetodoChange("valorPago");
         tab_comprobante.getColumna("valor_pago_tecpo").setEtiqueta();
         tab_comprobante.getColumna("valor_pago_tecpo").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
         tab_comprobante.getColumna("ide_geani").setVisible(false);
         tab_comprobante.setCondicion("ide_geani=-1");
         //tab_comprobante.agregarRelacion(tab_detalle_movimiento);
         tab_comprobante.setTipoFormulario(true);
         tab_comprobante.getGrid().setColumns(4);
         tab_comprobante.dibujar();
         PanelTabla pat_comprobante=new PanelTabla();
         pat_comprobante.setPanelTabla(tab_comprobante);
         
         ////detalle movimiento
 
         tab_detalle_movimiento.setId("tab_detalle_movimiento");
         //tab_detalle_movimiento.setIdCompleto("tab_tabulador:tab_detalle_movimiento");
         tab_detalle_movimiento.setHeader("DETALLE DE MOVIMIENTO");
         tab_detalle_movimiento.setTabla("cont_detalle_movimiento", "ide_codem", 2);
          //filtra por asiento contable cuando no tiene relacion a tes_comprovante_pago
         tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));    
         tab_detalle_movimiento.getColumna("ide_comov").setVisible(false);
         tab_detalle_movimiento.getColumna("detalle_codem").setVisible(false);
         tab_detalle_movimiento.getColumna("ide_prcla").setCombo(ser_Presupuesto.getCatalogoPresupuestario("true,false"));
         tab_detalle_movimiento.getColumna("ide_prcla").setAutoCompletar();
         tab_detalle_movimiento.getColumna("ide_prcla").setVisible(false);
         tab_detalle_movimiento.getColumna("ide_prpro").setVisible(false);
         tab_detalle_movimiento.getColumna("ide_tetic").setVisible(false);
         tab_detalle_movimiento.getColumna("ide_tepro").setVisible(false);
         tab_detalle_movimiento.getColumna("ide_gelua").setCombo("gen_lugar_aplica", "ide_gelua", "detalle_gelua", "");
        
         tab_detalle_movimiento.getColumna("conciliado_codem").setVisible(false);
         tab_detalle_movimiento.getColumna("transferencia_codem").setVisible(false);
         tab_detalle_movimiento.getColumna("fecha_concilia_codem").setVisible(false);
         tab_detalle_movimiento.getColumna("documento_codem").setVisible(false);

         tab_detalle_movimiento.getColumna("ide_prpro").setCombo("pre_programa", "ide_prpro", "cod_programa_prpro", "");
         tab_detalle_movimiento.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
         tab_detalle_movimiento.getColumna("ide_cocac").setAutoCompletar();

         tab_detalle_movimiento.getColumna("activo_codem").setLectura(true);
         tab_detalle_movimiento.getColumna("activo_codem").setValorDefecto("true");
         tab_detalle_movimiento.getColumna("haber_codem").setMetodoChange("calcularTotal");            
         tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem");            
         tab_detalle_movimiento.getColumna("debe_codem").setMetodoChange("calcularTotal");            
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
         tab_pre_mensual.getColumna("ide_pranu").setCombo(ser_Tesoreria.getCuentaPresupuestariaGastos());
         tab_pre_mensual.getColumna("ide_pranu").setNombreVisual("CUENTA PRESUPUESTARIA");
         tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
         tab_pre_mensual.getColumna("ide_comov").setVisible(false);
         tab_pre_mensual.getColumna("ide_gemes").setVisible(false);
         tab_pre_mensual.getColumna("ide_prtra").setVisible(false);
         tab_pre_mensual.getColumna("devengado_prmen").setNombreVisual("DEVENGADO");
         tab_pre_mensual.getColumna("cobrado_prmen").setNombreVisual("COBRADO");
         tab_pre_mensual.getColumna("pagado_prmen").setNombreVisual("PAGADO");
         tab_pre_mensual.getColumna("comprometido_prmen").setNombreVisual("COMPROMETIDO");
         tab_pre_mensual.getColumna("valor_anticipo_prmen").setNombreVisual("VALOR ANTICIPO");
         tab_pre_mensual.getColumna("certificado_prmen").setVisible(false);
         tab_pre_mensual.getColumna("ide_prcer").setVisible(false);
         tab_pre_mensual.getColumna("ide_tecpo").setVisible(false);
         tab_pre_mensual.setCondicion("ide_comov is not null ");
         tab_pre_mensual.setColumnaSuma("devengado_prmen,cobrado_prmen,pagado_prmen,comprometido_prmen,valor_anticipo_prmen");
         tab_pre_mensual.dibujar();
         PanelTabla pat_pre_mensual= new PanelTabla();
         pat_pre_mensual.setPanelTabla(tab_pre_mensual);
   
         Division div_division =new Division();
         //div_division.dividir3(pat_comprobante, pat_detalle_movimiento, "50%", "H");
         div_division.dividir3(pat_comprobante, pat_detalle_movimiento, pat_pre_mensual, "50%", "30%", "H");
         agregarComponente(div_division);
         
         Boton bot_buscaS=new Boton();
         bot_buscaS.setIcon("ui-icon-person");
         bot_buscaS.setValue("Buscar Solicitud Compra");
         bot_buscaS.setMetodo("importarSolicitudCompra");
         bar_botones.agregarBoton(bot_buscaS);
         
         set_solicitud.setId("set_solicitud");
         set_solicitud.setSeleccionTabla(ser_Adquisicion.getFacturasCompra("true","-1"),"ide_adfac");
         set_solicitud.setTitle("SELECCION UNA SOLICITUD/FACTURA DE COMPRA");   
         set_solicitud.getTab_seleccion().getColumna("proveedor").setFiltroContenido();
         set_solicitud.getTab_seleccion().getColumna("num_factura").setFiltroContenido();
         set_solicitud.getTab_seleccion().getColumna("nro_compromiso").setFiltro(true);
         set_solicitud.getBot_aceptar().setMetodo("aceptarCertificacionPresupuestaria");
         set_solicitud.setRadio();
         agregarComponente(set_solicitud);
         
         //
         Boton bot_buscaLq=new Boton();
         bot_buscaLq.setIcon("ui-icon-person");
         bot_buscaLq.setValue("Buscar Liq. Compra");
         bot_buscaLq.setMetodo("importarLiquidacionCompra");
         bar_botones.agregarBoton(bot_buscaLq);
         
         set_liquidacion.setId("set_liquidacion");
         set_liquidacion.setSeleccionTabla(ser_Adquisicion.getLiquidacionCompra("true"),"ide_adlic");
         set_liquidacion.setTitle("SELECCION UNA LIQUIDACIÓN DE COMPRA");   
         set_liquidacion.getTab_seleccion().getColumna("num_liquidacion_adlic").setFiltro(true);
         set_liquidacion.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
         set_liquidacion.getTab_seleccion().getColumna("compromiso").setFiltro(true);
         set_liquidacion.getBot_aceptar().setMetodo("aceptarLiquidacionCompra");
         set_liquidacion.setRadio();
         agregarComponente(set_liquidacion);
         
         //Compromiso presupuestario
         Boton bot_busca=new Boton();
         bot_busca.setIcon("ui-icon-person");
         bot_busca.setValue("Buscar Compromiso");
         bot_busca.setMetodo("importarCertificacionPresupuestaria");
         bar_botones.agregarBoton(bot_busca);

         set_tramite.setId("set_tramite");
         set_tramite.setSeleccionTabla(ser_Presupuesto.getTramite("true"),"ide_prtra");
         set_tramite.setTitle("SELECCION UN COMPROMISO PRESUPUESTARIO");        
         set_tramite.getTab_seleccion().getColumna("nro_compromiso").setFiltro(true);
         set_tramite.getTab_seleccion().getColumna("numero_oficio_prtra").setFiltro(true);
         set_tramite.getTab_seleccion().getColumna("observaciones_prtra").setFiltro(true);
         
         set_tramite.getBot_aceptar().setMetodo("aceptarCertificacionPresupuestaria");
         set_tramite.setRadio();
         agregarComponente(set_tramite);
         
         ///generar asiento conmtable
         Boton bot_contabilizar=new Boton();
         bot_contabilizar.setIcon("ui-icon-person");
         bot_contabilizar.setValue("Generar Asiento Contable");
         bot_contabilizar.setMetodo("seleccionarAsiento");
         bar_botones.agregarBoton(bot_contabilizar);

         set_contabilizar.setId("set_contabilizar");
         set_contabilizar.setSeleccionTabla(ser_Presupuesto.getCatalogoContabilizar("-1","-1"),"ide_prasp");
         set_contabilizar.setTitle("SELECCIONE LA CUENTA CONTABLE A EJECUTAR");        
         set_contabilizar.getTab_seleccion().getColumna("ide_prcla").setVisible(false);

         set_contabilizar.getBot_aceptar().setMetodo("aceptarAsiento");
         agregarComponente(set_contabilizar);
         
         //generar anticipos empleados
         Boton bot_anticipo=new Boton();
         bot_anticipo.setIcon("ui-icon-person");
         bot_anticipo.setValue("Generar Asiento Anticipos");
         bot_anticipo.setMetodo("seleccionarAnticipo");
         bar_botones.agregarBoton(bot_anticipo);
         
         set_anticipo_empleado.setId("set_anticipo_empleado");
         set_anticipo_empleado.setSeleccionTabla(ser_contabilidad.getConsultaAnticipos(),"codigo");
         set_anticipo_empleado.setTitle("SELECCIONE LOS EMPLEADOS");        
         set_anticipo_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
         set_anticipo_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
         set_anticipo_empleado.getTab_seleccion().getColumna("primer_nombre_gtemp").setFiltro(true);
         set_anticipo_empleado.getBot_aceptar().setMetodo("aceptarAnticipo");
         agregarComponente(set_anticipo_empleado);
         
         // grid datos del anticipo
         Grid gri_anticipo = new Grid();
         gri_anticipo.setColumns(2);
         gri_anticipo.getChildren().add(new Etiqueta("Concepto de Pago: "));
         com_tipo_concepto.setId("com_tipo_concepto");
         com_tipo_concepto.setCombo("select ide_tetic,detalle_tetic,codigo_tetic from tes_tipo_concepto order by detalle_tetic");
         gri_anticipo.getChildren().add(com_tipo_concepto);
         gri_anticipo.getChildren().add(new Etiqueta("Fecha: "));
         cal_fecha_inicial.setId("cal_fecha_inicial_debito");
		 cal_fecha_inicial.setFechaActual();
		 gri_anticipo.getChildren().add(cal_fecha_inicial);
		 gri_anticipo.getChildren().add(new Etiqueta("Monto: "));
		 txt_monto.setId("txt_monto");
		 txt_monto.setSoloNumeros();
		 txt_monto.setValue("0");
		 gri_anticipo.getChildren().add(txt_monto);
		 dia_anticipo.setId("dia_anticipo");
		 dia_anticipo.getBot_aceptar().setMetodo("aceptarAnticipo");
		 dia_anticipo.setDialogo(gri_anticipo);
		 agregarComponente(dia_anticipo);
		 
		 List<Object[]> listax = new ArrayList<Object[]>();
         Object fila1x[] = {"0", "ANTICIPO DE SUELDO"};
         Object fila2x[] = {"1", "OTROS ANTICIPOS" };
	        
         listax.add(fila1x);
         listax.add(fila2x);
         rad_imprimir.setRadio(listax);
		 
		 dia_tipo_anticipo.setId("dia_tipo_anticipo");
		 dia_tipo_anticipo.setTitle("Ingrese el tipo del anticipo");
		 dia_tipo_anticipo.setWidth("25%");
		 dia_tipo_anticipo.setHeight("20%");
		 dia_tipo_anticipo.setDialogo(rad_imprimir); 
		 dia_tipo_anticipo.getBot_aceptar().setMetodo("aceptarAnticipo");
		 agregarComponente(dia_tipo_anticipo);
		 
		 
		
	    con_guardar.setId("con_guardar");
		con_guardar.setMessage("EL COMPROBANTE DE PAGO NO REGISTRA RETENCIONES DESEA CONTINUAR, PRESIONE ACEPTAR CASO CONTRARIO CANCELAR");
		con_guardar.setTitle("CONFIRMACION DE RETENCIONES");
 		con_guardar.getBot_aceptar().setMetodo("aceptarCertificacionPresupuestaria");
 		con_guardar.getBot_cancelar().setMetodo("cancelarRetencion");
		agregarComponente(con_guardar);

			
         //generar cuenta presupuetso devengado
         Boton bot_devengado=new Boton();
         bot_devengado.setIcon("ui-icon-person");
         bot_devengado.setValue("Devengar");
         bot_devengado.setMetodo("devengarPresupuesto");
         bar_botones.agregarBoton(bot_devengado);
         
         set_presupuesto_devengado.setId("set_presupuesto_devengado");
         set_presupuesto_devengado.setSeleccionTabla(ser_Tesoreria.getSaldoDevengado("-1", "-1"),"codigo");
         set_presupuesto_devengado.setTitle("SELECCIONE LA CUENTA PRESUPUESTARIA A EJECUTAR");
         set_presupuesto_devengado.getTab_seleccion().getColumna("ide_prfup").setCombo(ser_Presupuesto.getCatalogoPoa("true")); 
         set_presupuesto_devengado.getTab_seleccion().getColumna("ide_prfup").setLongitud(250);
         set_presupuesto_devengado.getTab_seleccion().getColumna("ide_prfup").setNombreVisual("UBICACION POA");
         set_presupuesto_devengado.getTab_seleccion().getColumna("ide_prfuf").setVisible(false);
         set_presupuesto_devengado.getTab_seleccion().getColumna("ide_pranu").setVisible(false);
         set_presupuesto_devengado.getBot_aceptar().setMetodo("aceptarDevengarPresupuesto");
         agregarComponente(set_presupuesto_devengado);	
	         
         Boton bot_excel=new Boton();
 	     bot_excel.setValue("Exportar EXCEL");
 	     bot_excel.setIcon("ui-icon-calculator");
 	     bot_excel.setAjax(false);
 	     bot_excel.setMetodo("exportarExcel");
 	     bar_botones.agregarBoton(bot_excel);
 	    
         utilitario.agregarMensaje("Mensaje", "Pantalla obsoleta...");
         
     }
    
    public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_comprobante.setCondicion("ide_geani="+com_anio.getValue());
			tab_comprobante.ejecutarSql();
			//tab_detalle_movimiento.ejecutarValorForanea(tab_comprobante.getValorSeleccionado());
			tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));   
			tab_detalle_movimiento.ejecutarSql();
		}
		else{
			tab_comprobante.setCondicion("ide_geani=-1");
			tab_comprobante.ejecutarSql();
		}
	}
    
    public void devengarPresupuesto(){
    	try{
    		if(com_anio.getValue()==null){
    			utilitario.agregarMensajeInfo("Selecione un Año", "");
    			return;			
    		}
        	if( pckUtilidades.CConversion.CBol(tab_comprobante.getValor("aprobado_tecpo")))
    		{
    			utilitario.agregarMensajeInfo("Comprobante de PAGO-Aprobado", "Registro no Editable.!");
    			return;		
    		}
        	
	    	if(tab_comprobante.getValor("ide_tecpo")!=null)
	    	{
		    	set_presupuesto_devengado.getTab_seleccion().setSql(ser_Tesoreria.getSaldoDevengado(tab_detalle_movimiento.getFilaSeleccionada().getRowKey(),tab_comprobante.getValor("ide_tecpo")));
		    	set_presupuesto_devengado.getTab_seleccion().ejecutarSql();
		    	set_presupuesto_devengado.dibujar();
	    	}
    	}
    	catch (Exception ex)
    	{
    		System.out.println("Error al devengarPresupuesto "+ex.getMessage());
    	}
    }
    
    public void aceptarDevengarPresupuesto(){
    	if(set_presupuesto_devengado.isVisible()){
    		
    		if(set_presupuesto_devengado.getNumeroSeleccionados()<=0)
    		{
    			utilitario.agregarMensaje("Devengar", "Seleccione un Registro...");
    			return;
    		}
    		
    		for(int i=0;i<set_presupuesto_devengado.getTab_seleccion().getSeleccionados().length;i++)
    		{
    			int indice=set_presupuesto_devengado.getTab_seleccion().getSeleccionados()[i].getIndice();
    			
    			if(pckUtilidades.CConversion.CDbl_2(set_presupuesto_devengado.getTab_seleccion().getValor(indice, "saldo_compromiso"))<=0)
    			{
    				System.out.println("Devengar - No dispone de saldo...");
    				utilitario.agregarMensajeError("Devengar", "No dispone de saldo...");
    				set_presupuesto_devengado.cerrar();
    				return; // Ticket #108868
    			}
    			
    			if(pckUtilidades.CConversion.CDbl_2(set_presupuesto_devengado.getTab_seleccion().getValor(indice, "saldo_compromiso"))>0
    					|| pckUtilidades.CConversion.CInt(tab_comprobante.getValor("ide_coest"))==16)
    			{
	    			tab_pre_mensual.insertar();
	    			tab_pre_mensual.setValor("ide_pranu",set_presupuesto_devengado.getTab_seleccion().getValor(indice, "ide_pranu"));
	    			tab_pre_mensual.setValor("ide_prtra",tab_comprobante.getValor("ide_prtra"));
	    			tab_pre_mensual.setValor("ide_comov",tab_comprobante.getValor("ide_comov"));
	    			tab_pre_mensual.setValor("ide_codem",tab_detalle_movimiento.getFilaSeleccionada().getRowKey());
	    			tab_pre_mensual.setValor("fecha_ejecucion_prmen",tab_comprobante.getValor("fecha_tecpo"));
	    			tab_pre_mensual.setValor("comprobante_prmen",tab_comprobante.getValor("comprobante_egreso_tecpo"));
	    			tab_pre_mensual.setValor("devengado_prmen",set_presupuesto_devengado.getTab_seleccion().getValor(indice, "saldo_compromiso"));
	    			tab_pre_mensual.setValor("cobrado_prmen","0");
	    			tab_pre_mensual.setValor("cobradoc_prmen","0");
	    			if(pckUtilidades.CConversion.CInt(tab_comprobante.getValor("ide_coest"))==16)
	    			{
	    				tab_pre_mensual.setValor("pagado_prmen",set_presupuesto_devengado.getTab_seleccion().getValor(indice, "saldo_compromiso"));
	    				utilitario.agregarMensaje("Devengar", "No dispone de saldo... Favor usar solo para PAGOS.!");
	    			}
	    			else
	    				tab_pre_mensual.setValor("pagado_prmen","0");
	    			tab_pre_mensual.setValor("comprometido_prmen","0");
	    			tab_pre_mensual.setValor("valor_anticipo_prmen","0");
	    			tab_pre_mensual.setValor("activo_prmen","true");
	    			tab_pre_mensual.setValor("ide_prfuf",set_presupuesto_devengado.getTab_seleccion().getValor(indice, "ide_prfuf"));
	    			tab_pre_mensual.setValor("ide_tecpo",tab_comprobante.getValor("ide_tecpo"));
    			}
    			else
    				utilitario.agregarMensaje("Devengar", "No dispone de saldo...");
    		}
    		
			set_presupuesto_devengado.cerrar();

    	}
    	else {
        	set_presupuesto_devengado.getTab_seleccion().setSql(ser_Tesoreria.getSaldoDevengado(tab_detalle_movimiento.getFilaSeleccionada().getRowKey(),tab_comprobante.getValor("ide_tecpo")));
        	set_presupuesto_devengado.getTab_seleccion().ejecutarSql();
        	set_presupuesto_devengado.dibujar();
    	}
     }
    
    public void seleccionarAnticipo(){
    	
    	if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
    	if( pckUtilidades.CConversion.CBol(tab_comprobante.getValor("aprobado_tecpo")))
		{
			utilitario.agregarMensajeInfo("Comprobante de PAGO-Aprobado", "Registro no Editable.!");
			//return;		
		}
    	tipoRecalculo=0;
    	rad_imprimir.setValue(tipoRecalculo);
    	txt_monto.setValue("0");
    	dia_tipo_anticipo.dibujar();
    }
    
    String str_seleccionados="";
    
    public void aceptarAnticipo(){
    	//String str_seleccionados="";
    	String valor_debe="0";
        String valor_haber="0";
        
        if(dia_tipo_anticipo.isVisible()){
        	
        	if(rad_imprimir.getValue()==null){
				utilitario.agregarMensajeInfo("Selecione un Tipo", "");
				return;			
			}
        	
        	tipoRecalculo=pckUtilidades.CConversion.CInt(rad_imprimir.getValue());
        	
        	if(tipoRecalculo==0)
        	{
        		set_anticipo_empleado.getTab_seleccion().setSql(ser_contabilidad.getConsultaAnticipos());
        		txt_monto.setDisabled(true);
        	}
        	else
        	{
        		set_anticipo_empleado.getTab_seleccion().setSql(ser_contabilidad.getConsultaOtrosAnticiposEmpleados());
        		txt_monto.setDisabled(false);
        	}
        	
        	set_anticipo_empleado.getTab_seleccion().ejecutarSql();
        	set_anticipo_empleado.dibujar();
        	
        	dia_tipo_anticipo.cerrar();
        }
        
        else if(set_anticipo_empleado.isVisible()){
    		
    		if(set_anticipo_empleado.getSeleccionados() !=null){
    			str_seleccionados=set_anticipo_empleado.getSeleccionados();
 
    			set_anticipo_empleado.cerrar();
    			dia_anticipo.dibujar();
    		}
    		else {
    			utilitario.agregarMensajeError("Seleccione un Registro", "Debe seleccionar al menos un registro para continuar");
    		}
    	}
    	else if (dia_anticipo.isVisible()){
    		//System.out.println("entro a imprimir lso anteicipos "+str_seleccionados);
    		String sql_insert="";
            ser_contabilidad.limpiarAcceso("tes_comprobante_pago");
            ser_contabilidad.limpiarAcceso("cont_detalle_movimiento");
  
    		if(tipoRecalculo==0)
    		{
	   	 		TablaGenerica tab_anticipos=utilitario.consultar("select a.ide_nrant,a.ide_gtemp,a.ide_geedp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,detalle_nrmoa,fecha_solicitud_nrant,monto_aprobado_nrant"
	    				+" from nrh_anticipo a left join nrh_motivo_anticipo c on a.ide_nrmoa = c.ide_nrmoa left join gth_empleado d on a.ide_gtemp = d.ide_gtemp where ide_nrant  in ("+str_seleccionados+")");
	
	    		tab_anticipos.imprimirSql();
	
	    		for (int i=0;i<tab_anticipos.getTotalFilas();i++){
	    			String str_nombre_empleado=tab_anticipos.getValor(i, "apellido_paterno_gtemp")+" "+tab_anticipos.getValor(i, "apellido_materno_gtemp")+" "+tab_anticipos.getValor(i, "primer_nombre_gtemp")+" "+tab_anticipos.getValor(i, "segundo_nombre_gtemp");
	    			// Consulto codigo maximo de la cabecera del comprobante de pago
					TablaGenerica tab_maximo_comprobante =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("tes_comprobante_pago", "ide_tecpo"));
					String maximo_comprobante=tab_maximo_comprobante.getValor("codigo");
					// consulta el codigo maximo del secuencia del numero de comprobante  
					String secuencia_maximo=ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago);
	    			sql_insert="insert into tes_comprobante_pago (ide_tecpo,ide_geani,ide_tetic,ide_geedp,ide_coest,fecha_tecpo,comprobante_egreso_tecpo,detalle_tecpo,fecha_pago_tecpo,activo_tecpo,valor_pago_tecpo,ide_nrant,ide_gtemp,usuario_ingre,fecha_ingre,hora_ingre)" 
	    					+ " values ("+maximo_comprobante+","+com_anio.getValue()+","+com_tipo_concepto.getValue()+","+tab_anticipos.getValor(i, "ide_geedp")+","+par_estado_devengado+",'"+cal_fecha_inicial.getFecha()+"','"+secuencia_maximo+"','ANTICIPO SUELDO DEL FUNCIONARIO(A): "
	    					            +str_nombre_empleado+", MOTIVO DEL ANTICIPO: "+tab_anticipos.getValor(i, "detalle_nrmoa")+"','"+cal_fecha_inicial.getFecha()+"',true,"+tab_anticipos.getValor(i, "monto_aprobado_nrant")+","+tab_anticipos.getValor(i, "ide_nrant")+","+empleado+",'"+utilitario.getVariable("NICK")+"',now(),now())";
	    			utilitario.getConexion().ejecutarSql(sql_insert);
	    			// actualizamos el secuencial
	    			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago), par_sec_comprobante_pago);
	    			// consulto el numero de asiento generado por el disparador paracargar lso detalles del asiento contable
	    			TablaGenerica tab_consulta_movimiento=utilitario.consultar("select ide_tecpo, ide_comov from tes_comprobante_pago where ide_tecpo="+maximo_comprobante);
	    			TablaGenerica tab_cuenta_anticipo_empleado=utilitario.consultar("select * from gth_cuenta_anticipo where ide_gtemp="+tab_anticipos.getValor(i,"ide_gtemp")+" and ide_geani="+par_anio_vigente);
	    			//System.out.println("imprimo cuenta d eanticiopo");
	    			//tab_cuenta_anticipo_empleado.imprimirSql();
	    			// INSERTO EN LA TABLA DEL detalle del asiento contable
					// Consulto codigo maximo del detalle del movimiento
					TablaGenerica tab_maximo_detalle =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
					String maximo_detalle=tab_maximo_detalle.getValor("codigo");
	
					
	    			String sqlinsertaprimeralinea ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
	    					+" values ( "+maximo_detalle+","+tab_consulta_movimiento.getValor("ide_comov")+","+tab_anticipos.getValor(i, "monto_aprobado_nrant")+",0,true,"
	    					          +tab_cuenta_anticipo_empleado.getValor("ide_cocac")+","+par_debe+",'"+utilitario.getVariable("NICK")+"',now(),now());";
	    			utilitario.getConexion().ejecutarSql(sqlinsertaprimeralinea);
	
	    			//cosnulto cuenta que se mueve al ahaber del asiento tipo de anticipos
	    			TablaGenerica tab_asiento_tipo=utilitario.consultar("select ide_coast,ide_gelua,ide_cocac from cont_asiento_tipo where ide_conac ="+par_asiento_anticipo);
	    			
	    			for (int j=0;j<tab_asiento_tipo.getTotalFilas();j++){	
	    			//isnerta debe
	    					if(tab_asiento_tipo.getValor(j,"ide_gelua").equals(par_debe)){
	    						valor_debe=tab_anticipos.getValor(i, "monto_aprobado_nrant");
	    						valor_haber="0";
	    					}
	    					//isnerta haber
	    					else if(tab_asiento_tipo.getValor(j,"ide_gelua").equals(par_haber)){
	    						valor_haber=tab_anticipos.getValor(i, "monto_aprobado_nrant");   
	    						valor_debe="0";
	    					}
	    					
	    					// INSERTO EN LA TABLA DEL detalle del asiento contable
	    					// Consulto codigo maximo del detalle del movimiento
	    					TablaGenerica tab_maximo_detalle2 =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
	    					String maximo_detalle2=tab_maximo_detalle2.getValor("codigo");
	
	    					
	    	    			String sqlinsertaprimeralinea2 ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
	    	    					+" values ( "+maximo_detalle2+","+tab_consulta_movimiento.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+tab_asiento_tipo.getValor(j, "ide_cocac")+","
	    	    					             +tab_asiento_tipo.getValor(j, "ide_gelua")+",'"+utilitario.getVariable("NICK")+"',now(),now() );";
	    	    			utilitario.getConexion().ejecutarSql(sqlinsertaprimeralinea2);
	
	    			}
	    			
	    		}
	    		
    		}
    		else
    		{
    			TablaGenerica tab_anticipos=utilitario.consultar("select ide_geedp,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp "
																+" from gth_empleado em "
																+" join GEN_EMPLEADOS_DEPARTAMENTO_PAR emd on emd.ide_gtemp=em.ide_gtemp "
																+" where activo_geedp=true and em.ide_gtemp in ("+str_seleccionados+")");
	
	    		tab_anticipos.imprimirSql();
	
	    		for (int i=0;i<tab_anticipos.getTotalFilas();i++){
	    			String str_nombre_empleado=tab_anticipos.getValor(i, "apellido_paterno_gtemp")+" "+tab_anticipos.getValor(i, "apellido_materno_gtemp")+" "+tab_anticipos.getValor(i, "primer_nombre_gtemp")+" "+tab_anticipos.getValor(i, "segundo_nombre_gtemp");
	    			// Consulto codigo maximo de la cabecera del comprobante de pago
					TablaGenerica tab_maximo_comprobante =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("tes_comprobante_pago", "ide_tecpo"));
					String maximo_comprobante=tab_maximo_comprobante.getValor("codigo");
					// consulta el codigo maximo del secuencia del numero de comprobante  
					String secuencia_maximo=ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago);
	    			sql_insert="insert into tes_comprobante_pago (ide_tecpo,ide_geani,ide_tetic,ide_geedp,ide_coest,fecha_tecpo,comprobante_egreso_tecpo,detalle_tecpo,fecha_pago_tecpo,activo_tecpo,valor_pago_tecpo,ide_nrant,ide_gtemp,usuario_ingre,fecha_ingre,hora_ingre)" 
	    					+ " values ("+maximo_comprobante+","+com_anio.getValue()+","+com_tipo_concepto.getValue()+","+tab_anticipos.getValor(i, "ide_geedp")+","+par_estado_devengado+",'"+cal_fecha_inicial.getFecha()+"','"
	    					             +secuencia_maximo+"','ANTICIPO AL FUNCIONARIO(A): "+str_nombre_empleado+", MONTO: "+txt_monto.getValue()+"','"+cal_fecha_inicial.getFecha()+"',true,"+txt_monto.getValue()+",null,"+empleado+",'"+utilitario.getVariable("NICK")+"',now(),now())";
	    			utilitario.getConexion().ejecutarSql(sql_insert);
	    			// actualizamos el secuencial
	    			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago), par_sec_comprobante_pago);
	    			// consulto el numero de asiento generado por el disparador paracargar lso detalles del asiento contable
	    			TablaGenerica tab_consulta_movimiento=utilitario.consultar("select ide_tecpo, ide_comov from tes_comprobante_pago where ide_tecpo="+maximo_comprobante);
	    			
	    			//consulto cuenta que se mueve al ahaber del asiento tipo de anticipos
	    			TablaGenerica tab_asiento_tipo=utilitario.consultar("select ide_coast,ide_gelua,ide_cocac from cont_asiento_tipo where ide_conac ="+par_asiento_otros_anticipo);
	    			
	    			for (int j=0;j<tab_asiento_tipo.getTotalFilas();j++){	
	    			//isnerta debe
	    					if(tab_asiento_tipo.getValor(j,"ide_gelua").equals(par_debe)){
	    						valor_debe=pckUtilidades.CConversion.CDbl_2(txt_monto.getValue())+"";
	    						valor_haber="0";
	    					}
	    					//isnerta haber
	    					else if(tab_asiento_tipo.getValor(j,"ide_gelua").equals(par_haber)){
	    						valor_haber=pckUtilidades.CConversion.CDbl_2(txt_monto.getValue())+"";   
	    						valor_debe="0";
	    					}
	    					
	    					// INSERTO EN LA TABLA DEL detalle del asiento contable
	    					// Consulto codigo maximo del detalle del movimiento
	    					TablaGenerica tab_maximo_detalle2 =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
	    					String maximo_detalle2=tab_maximo_detalle2.getValor("codigo");
	
	    					
	    	    			String sqlinsertaprimeralinea2 ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
	    	    					+" values ( "+maximo_detalle2+","+tab_consulta_movimiento.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+tab_asiento_tipo.getValor(j, "ide_cocac")+","+tab_asiento_tipo.getValor(j, "ide_gelua")+",'"+utilitario.getVariable("NICK")+"',now(),now() );";
	    	    			utilitario.getConexion().ejecutarSql(sqlinsertaprimeralinea2);
	
	    			}
	    			
	    		}
    			
    		}
    		
    		
    		
    	
    		utilitario.agregarMensaje("Contabilizado Correctamente", "Asientos de Anticipos generedos");
    		dia_anticipo.cerrar();
    		tab_comprobante.ejecutarSql();
    		utilitario.addUpdate("tab_comprobante");
    		//System.out.println("nuemro de comprobante "+tab_comprobante.getValor("ide_comov"));
            tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));    
            tab_detalle_movimiento.ejecutarSql();
    		utilitario.addUpdate("tab_detalle_movimiento");
            tab_comprobante.imprimirSql();
            tab_detalle_movimiento.imprimirSql();
    	}
    	else{
    		utilitario.agregarMensaje("Un gusto saludarle", "Tenga un buen dia");
    		//dia_anticipo_tipo.dibujar();
    	}
     }
    
    public void aceptarAsiento(){
        String str_seleccionado = set_contabilizar.getSeleccionados();
        String valor_debe="0";
        String valor_haber="0";
        TablaGenerica tab_asociacion=utilitario.consultar("SELECT ide_prasp, ide_prcla, ide_cocac, ide_gelua, ide_prmop, activo_prasp, cuenta_padre_prasp,devengado FROM pre_asociacion_presupuestaria where ide_prasp in ("+str_seleccionado+");");
		TablaGenerica tab_valor_comprobante=utilitario.consultar("select ide_tecpo,"+par_valor_devengar+" as valor,valor_iva_tecpo,valor_pago_tecpo from tes_comprobante_pago where ide_tecpo ="+tab_comprobante.getValor("ide_tecpo"));
		//System.out.println("tab_asociacion ");
		//tab_asociacion.imprimirSql();
		//System.out.println("tab_valor_comprobante ");
		//tab_valor_comprobante.imprimirSql();

        ser_contabilidad.limpiarAcceso("cont_detalle_movimiento");
        if(tab_asociacion.getTotalFilas()>0){
    		for(int i= 0;i< tab_asociacion.getTotalFilas();i++){
    			
    			
    			//isnerta debe
    			if(tab_asociacion.getValor(i,"ide_gelua").equals(par_debe)){
    				valor_debe=tab_valor_comprobante.getValor("valor");
    				valor_haber="0";
    			}
    			//isnerta haber
    			else if(tab_asociacion.getValor(i,"ide_gelua").equals(par_haber)){
    				valor_haber=tab_valor_comprobante.getValor("valor");   
    				valor_debe="0";
    			}
    			
    			// INSERTO EN LA TABLA DEL detalle del asiento contable
				// Consulto codigo maximo de la cabecera del asiento factura
				TablaGenerica tab_maximo_detalle =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
				String maximo_detalle=tab_maximo_detalle.getValor("codigo");

				
    			String sqlinsertaprimeralinea ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
    					+" values ( "+maximo_detalle+","+tab_comprobante.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+tab_asociacion.getValor(i,"ide_cocac")+","+tab_asociacion.getValor(i,"ide_gelua")+",'"+utilitario.getVariable("NICK")+"',now(),now() );";
    			utilitario.getConexion().ejecutarSql(sqlinsertaprimeralinea);
    			//System.out.println("sqlinsertaprimeralinea "+sqlinsertaprimeralinea);
    			//inserto las cuentas por pagar tanto al debe como al haber
    	        TablaGenerica tab_cuenta_pagar=utilitario.consultar("SELECT ide_prasp, ide_prcla, ide_cocac, ide_gelua, ide_prmop, activo_prasp, cuenta_padre_prasp FROM pre_asociacion_presupuestaria where ide_prcla in ("+tab_asociacion.getValor(i,"devengado")+");");
    			//System.out.println("tab_cuenta_pagar ");
    			//tab_cuenta_pagar.imprimirSql();
    	        for(int j=0;j<tab_cuenta_pagar.getTotalFilas();j++){
    	        	//isnerta debe
        			if(tab_cuenta_pagar.getValor(j,"ide_gelua").equals(par_debe)){
        				valor_debe=tab_valor_comprobante.getValor("valor");
        				valor_haber="0";
        			}
        			//isnerta haber
        			else if(tab_cuenta_pagar.getValor(j,"ide_gelua").equals(par_haber)){
        				valor_haber=tab_valor_comprobante.getValor("valor");   
        				valor_debe="0";
        			}
        			
        			// INSERTO EN LA TABLA DEL detalle del asiento contable
    				// Consulto codigo maximo de la cabecera del asiento factura
    				TablaGenerica tab_maximo_detallej =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
    				String maximo_detallej=tab_maximo_detallej.getValor("codigo");

    				
        			String sqlinsertaprimeralineaj ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
        					+" values ( "+maximo_detallej+","+tab_comprobante.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+tab_cuenta_pagar.getValor(j,"ide_cocac")+","+tab_cuenta_pagar.getValor(j,"ide_gelua")+" ,'"+utilitario.getVariable("NICK")+"',now(),now());";
        			utilitario.getConexion().ejecutarSql(sqlinsertaprimeralineaj);
        			
    	        }
    		}
    	}
        
        
        if(par_lugar_aplica_banco.equals(par_debe)){
			valor_debe=tab_valor_comprobante.getValor("valor_pago_tecpo");
			valor_haber="0";
		}
		//isnerta haber
		else if(par_lugar_aplica_banco.equals(par_haber)){
			valor_haber=tab_valor_comprobante.getValor("valor_pago_tecpo");   
			valor_debe="0";
		}
        // INSERTO EN LA TABLA DEL detalle del asiento contable de bancos
    	// Consulto codigo maximo de la cabecera del asiento factura
    	TablaGenerica tab_maximo_detallebanco =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
    	String maximo_detallebanco=tab_maximo_detallebanco.getValor("codigo");

    				
		String sqlinsertaprimeralineabanco ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
				+" values ( "+maximo_detallebanco+","+tab_comprobante.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+par_cuenta_banco+","+par_lugar_aplica_banco+" ,'"+utilitario.getVariable("NICK")+"',now(),now());";
		utilitario.getConexion().ejecutarSql(sqlinsertaprimeralineabanco);

		//System.out.println("sqlinsertaprimeralineabanco "+sqlinsertaprimeralineabanco);

         // INSERTO EN LA TABLA DEL detalle del asiento contable de iva
        // Consulto codigo maximo de la cabecera del asiento factura
        
		 if(par_lugar_aplica_iva.equals(par_debe)){
			valor_debe=tab_valor_comprobante.getValor("valor_iva_tecpo");
			valor_haber="0";
		}
			//isnerta haber
		else if(par_lugar_aplica_iva.equals(par_haber)){
			valor_haber=tab_valor_comprobante.getValor("valor_iva_tecpo");   
			valor_debe="0";
		}			
        TablaGenerica tab_maximo_detalleiva =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
        String maximo_detalleiva=tab_maximo_detalleiva.getValor("codigo");
 				
		String sqlinsertaprimeralineaiva ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
		+" values ( "+maximo_detalleiva+","+tab_comprobante.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+par_cuenta_iva+","+par_lugar_aplica_iva+" ,'"+utilitario.getVariable("NICK")+"',now(),now());";
		utilitario.getConexion().ejecutarSql(sqlinsertaprimeralineaiva);
		//System.out.println("sqlinsertaprimeralineaiva "+sqlinsertaprimeralineaiva);

 		TablaGenerica tab_consulta_retencion=utilitario.consultar(ser_Tesoreria.getConsultaRetencion(tab_comprobante.getValor("ide_tecpo")));	
		//System.out.println("tab_consulta_retencion ");
		//tab_consulta_retencion.imprimirSql();
 		 		
 		for(int k=0;k<tab_consulta_retencion.getTotalFilas();k++){
 			TablaGenerica tab_ejecuta = utilitario.consultar(ser_Tesoreria.getConsultaRetencionCalcula(tab_comprobante.getValor("ide_tecpo"), tab_consulta_retencion.getValor(k,"formula_teast")));
 			
 			if(tab_consulta_retencion.getValor(k, "ide_gelua").equals(par_debe)){
					valor_debe=tab_ejecuta.getValor(k,"resultado");
					valor_haber="0";
				}
				//isnerta haber
				else if(tab_consulta_retencion.getValor(k, "ide_gelua").equals(par_haber)){
					valor_haber=tab_ejecuta.getValor(k,"resultado");   
					valor_debe="0";
				}			
 			 TablaGenerica tab_maximo_detalleretencion =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_detalle_movimiento", "ide_codem"));
 			 String maximo_detalleretencion=tab_maximo_detalleretencion.getValor("codigo");
		
 			 String sqlinsertaprimeralinearetencion ="insert into cont_detalle_movimiento (ide_codem,ide_comov,debe_codem,haber_codem,activo_codem,ide_cocac,ide_gelua,usuario_ingre,fecha_ingre,hora_ingre)"
 					 +" values ( "+maximo_detalleretencion+","+tab_comprobante.getValor("ide_comov")+","+valor_debe+","+valor_haber+",true,"+tab_consulta_retencion.getValor(k, "ide_cocac")
 					           +","+tab_consulta_retencion.getValor(k, "ide_gelua")+",'"+utilitario.getVariable("NICK")+"',now(),now() );";
 			 utilitario.getConexion().ejecutarSql(sqlinsertaprimeralinearetencion);

 		}
 		
 		
        set_contabilizar.cerrar();			
        //tab_comprobante.ejecutarSql();
        tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));    
        tab_detalle_movimiento.ejecutarSql();
        utilitario.addUpdate("tab_detalle_movimiento");
    }
    
    public void seleccionarAsiento(){
    	
    	if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
    	if( pckUtilidades.CConversion.CBol(tab_comprobante.getValor("aprobado_tecpo")))
		{
			utilitario.agregarMensajeInfo("Comprobante de PAGO-Aprobado", "Registro no Editable.!");
			return;		
		}
    	
    	set_contabilizar.getTab_seleccion().setSql(ser_Presupuesto.getCatalogoContabilizar(tab_comprobante.getValor("IDE_PRTRA"),par_movimiento_devengado));
    	set_contabilizar.getTab_seleccion().ejecutarSql();
    	set_contabilizar.dibujar();

    }
   ///sacar valores
 	
   	public void calcularTotal(AjaxBehaviorEvent evt){
   		tab_detalle_movimiento.modificar(evt);
   		tab_detalle_movimiento.sumarColumnas();
   		utilitario.addUpdate("tab_detalle_movimiento");
   	}

    public void importarSolicitudCompra(){
     	//System.out.println("entra a metodo impostar solicitud");
     	
         set_solicitud.getTab_seleccion().setSql(ser_Adquisicion.getFacturasCompra("true",com_anio.getValue()+""));
         set_solicitud.getTab_seleccion().ejecutarSql();
         set_solicitud.dibujar();

     }

    public void aceptarSolicitudCompra(){
     	System.out.println("entra a metodo aceptar solicitud");

         String str_seleccionado = set_solicitud.getValorSeleccionado();
         
         TablaGenerica tab_solicitud=ser_Adquisicion.getTablaGenericaSolicitud(str_seleccionado);
         if (str_seleccionado!=null){
        	 
			String id_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");	
			if(!empleado.equals(id_empleado)){
				System.out.println("actualizando empleado comprobante pago ide_gtemp");
				empleado=id_empleado;
			}
        	 
             tab_comprobante.insertar();
             tab_comprobante.setValor("ide_adsoc",str_seleccionado);
             tab_comprobante.setValor("ide_gtemp", empleado );
             tab_comprobante.setValor("valor_compra_tecpo",tab_solicitud.getValor("total_adfac"));
             tab_comprobante.setValor("valor_iva_tecpo", tab_solicitud.getValor("valor_iva_adfac"));
             tab_comprobante.setValor("ide_prtra", tab_solicitud.getValor("ide_prtra"));
             tab_comprobante.setValor("ide_tepro",tab_solicitud.getValor("ide_tepro"));
             tab_comprobante.setValor("subtotal_tecpo",tab_solicitud.getValor("subtotal_adfac"));
             tab_comprobante.setValor("ide_geani", com_anio.getValue()+"");
             tab_comprobante.setValor("comprobante_egreso_tecpo",ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago));
         }
         set_solicitud.cerrar();
         utilitario.addUpdate("tab_comprobante");
         
         
     }
     
    public void importarLiquidacionCompra(){

    	 set_liquidacion.getTab_seleccion().setSql(ser_Adquisicion.getLiquidacionCompra("true"));
    	 set_liquidacion.getTab_seleccion().ejecutarSql();
    	 set_liquidacion.dibujar();
    	 ide_adlic="0";
      }

    public void aceptarLiquidacionCompra()
      {
          String str_seleccionado = set_liquidacion.getValorSeleccionado();
          
          TablaGenerica tab_solicitud=ser_Adquisicion.getTablaGenericaLiquidacionCompra(str_seleccionado);
          if (str_seleccionado!=null){
        	  
        	  ide_adlic=str_seleccionado;
              String ide_prtra=tab_solicitud.getValor("ide_prtra");
              
        	  TablaGenerica tab_retenciones_facturas = utilitario.consultar(ser_Tesoreria.getFacturaRetencionLiq(ide_prtra, ide_adlic));
        	  
        	  if(tab_retenciones_facturas.getTotalFilas()<1)
        	  {
        		  utilitario.agregarMensajeError("Seleccione un Registro", "Debe seleccionar una liquidación de compra autorizada y con retención electronica vigente.");
        		  return;
        	  }
        	  
        	  String id_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");	
	  			if(!empleado.equals(id_empleado)){
	  				System.out.println("actualizando empleado comprobante pago ide_gtemp");
	  				empleado=id_empleado;
	  			}
        	  
              tab_comprobante.insertar();    
              tab_comprobante.setValor("ide_coest", "16");
              tab_comprobante.setValor("valor_iva_tecpo", "0");
              tab_comprobante.setValor("ide_gtemp", empleado );
              tab_comprobante.setValor("valor_compra_tecpo",tab_solicitud.getValor("total_adlic"));
              tab_comprobante.setValor("detalle_tecpo", tab_solicitud.getValor("detalle_adlic"));
              tab_comprobante.setValor("ide_prtra", ide_prtra);
              tab_comprobante.setValor("ide_tepro",tab_solicitud.getValor("ide_tepro"));
              tab_comprobante.setValor("subtotal_tecpo",tab_solicitud.getValor("total_adlic"));
              tab_comprobante.setValor("ide_geani", com_anio.getValue()+"");
              tab_comprobante.setValor("comprobante_egreso_tecpo",ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago));
                
              //Si tiene facturas y retenciones
         	 if(tab_retenciones_facturas.getTotalFilas()>0){        	 
 	             tab_detalle_movimiento.setCondicion("ide_comov is null");
 	             tab_detalle_movimiento.ejecutarSql();
 	             tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem"); 
 	             tab_comprobante.setValor("valor_retencion_tecpo", tab_retenciones_facturas.getValor("valor_retenido"));
 	             tab_comprobante.setValor("ide_tepro", tab_retenciones_facturas.getValor("ide_tepro"));
 	             tab_comprobante.setValor("detalle_tecpo", tab_retenciones_facturas.getValor("detalle"));
 	             tab_comprobante.setValor("valor_compra_tecpo", tab_retenciones_facturas.getValor("total_adlic"));
 	             tab_comprobante.setValor("valor_iva_tecpo","0");
 	             tab_comprobante.setValor("subtotal_tecpo", tab_retenciones_facturas.getValor("subtotal_adlic"));
 	             
 	             TablaGenerica tab_valor_pago = utilitario.consultar("select 1 as codigo, "+tab_retenciones_facturas.getValor("subtotal_adlic")+"-"+tab_retenciones_facturas.getValor("valor_retenido")+" as valor_pago");
 	             tab_comprobante.setValor("valor_pago_tecpo", tab_valor_pago.getValor("valor_pago"));
 	             
 	             utilitario.addUpdate("tab_detalle_movimiento");
 	             set_tramite.cerrar();
              }
         	 
          }
          set_liquidacion.cerrar();
          utilitario.addUpdate("tab_comprobante");
          
          
      }


     public void importarCertificacionPresupuestaria(){
     	//System.out.println("entra a metodo impostar cer. presu");

         set_tramite.getTab_seleccion().setSql("select * from ("+ser_Presupuesto.getTramite("true")+") a where extract(year from fecha_tramite_prtra)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
         set_tramite.getTab_seleccion().ejecutarSql();
         //set_tramite.getTab_seleccion().imprimirSql();
         set_tramite.dibujar();

     }
     String val_selecciondo ="";
     String val_ide_adfac ="";
     
     public void aceptarCertificacionPresupuestaria(){

     	if(set_tramite.isVisible()){
 	   	 val_selecciondo = set_tramite.getValorSeleccionado();
 	   	 val_ide_adfac="";
 	   	 cargarComprobante(val_selecciondo,"");
 	   	 
         set_tramite.cerrar();
     	}
     	 else if (set_solicitud.isVisible()){
    	   	 val_selecciondo = set_solicitud.getValorSeleccionado();
    	   	 val_ide_adfac = val_selecciondo;
    	   	 cargarComprobante("",val_selecciondo);
    	   	 
    	   	set_solicitud.cerrar();
        }
     	 else if (con_guardar.isVisible()){
     		tab_detalle_movimiento.setCondicion("ide_comov is null");;
            tab_detalle_movimiento.ejecutarSql();
            tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem");
            TablaGenerica tab_consulta_tramite=utilitario.consultar("select ide_prtra,ide_tepro,ide_geedp,observaciones_prtra,total_compromiso_prtra from pre_tramite where ide_prtra="+tab_comprobante.getValor("ide_prtra"));
     		tab_consulta_tramite.imprimirSql();
            tab_comprobante.setValor("valor_retencion_tecpo", "0.00");  
            tab_comprobante.setValor("ide_tepro", tab_consulta_tramite.getValor("ide_tepro"));
            tab_comprobante.setValor("detalle_tecpo", tab_consulta_tramite.getValor("observaciones_prtra"));
            tab_comprobante.setValor("valor_compra_tecpo", tab_consulta_tramite.getValor("total_compromiso_prtra"));
            tab_comprobante.setValor("valor_iva_tecpo","0.00");
            tab_comprobante.setValor("subtotal_tecpo", tab_consulta_tramite.getValor("total_compromiso_prtra"));
            tab_comprobante.setValor("valor_pago_tecpo", tab_consulta_tramite.getValor("total_compromiso_prtra"));
            tab_comprobante.setValor("ide_geedp", tab_consulta_tramite.getValor("ide_geedp"));
       	    
       	    utilitario.addUpdateTabla(tab_comprobante, "valor_retencion_tecpo,ide_tepro,detalle_tecpo,valor_iva_tecpo,subtotal_tecpo,valor_pago_tecpo,valor_compra_tecpo,ide_geedp", "");
            utilitario.addUpdate("tab_detalle_movimiento");
     		con_guardar.cerrar();
     	}
			
     }
     
     private void cargarComprobante(String ide_prtra, String ide_adfac)
     {
    	 TablaGenerica tab_tramite=ser_Presupuesto.getTablaGenericaTramite(ide_prtra, ide_adfac);
         tab_tramite.imprimirSql();
         if (val_selecciondo!=null){

        	 String id_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");	
	 			if(!empleado.equals(id_empleado)){
	 				System.out.println("actualizando empleado comprobante pago ide_gtemp");
	 				empleado=id_empleado;
	 			}
 			
             tab_comprobante.insertar();
             tab_comprobante.setValor("ide_gtemp", empleado );
             tab_comprobante.setValor("ide_prtra",tab_tramite.getValor("ide_prtra"));
             tab_comprobante.setValor("ide_geani", com_anio.getValue()+"");
             tab_comprobante.setValor("comprobante_egreso_tecpo",ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago));
        	 TablaGenerica tab_retenciones_facturas = utilitario.consultar(ser_Tesoreria.getFacturaRetencion(ide_prtra, ide_adfac));
             //Si tiene facturas y retenciones
        	 if(tab_retenciones_facturas.getTotalFilas()>0){        	 
	             tab_detalle_movimiento.setCondicion("ide_comov is null");;
	             tab_detalle_movimiento.ejecutarSql();
	             tab_detalle_movimiento.setColumnaSuma("haber_codem,debe_codem"); 
	             tab_comprobante.setValor("valor_retencion_tecpo", tab_retenciones_facturas.getValor("valor_retenido"));
	             tab_comprobante.setValor("ide_adsoc", tab_retenciones_facturas.getValor("ide_adsoc"));
	             tab_comprobante.setValor("ide_tepro", tab_retenciones_facturas.getValor("ide_tepro"));
	             tab_comprobante.setValor("detalle_tecpo", tab_retenciones_facturas.getValor("detalle"));
	             tab_comprobante.setValor("valor_compra_tecpo", tab_retenciones_facturas.getValor("total_adfac"));
	             tab_comprobante.setValor("valor_iva_tecpo", tab_retenciones_facturas.getValor("valor_iva_adfac"));
	             tab_comprobante.setValor("subtotal_tecpo", tab_retenciones_facturas.getValor("subtotal_adfac"));
	             //TablaGenerica tab_valor_pago = utilitario.consultar("select 1 as codigo, "+tab_retenciones_facturas.getValor("subtotal_adfac")+"+"+ tab_retenciones_facturas.getValor("valor_iva_adfac")+"-"+tab_retenciones_facturas.getValor("valor_retenido")+" as valor_pago");
	             //tab_comprobante.setValor("valor_pago_tecpo", tab_valor_pago.getValor("valor_pago"));
	             double valor_pago = pckUtilidades.CConversion.CDbl_2(tab_retenciones_facturas.getValor("subtotal_adfac")) + pckUtilidades.CConversion.CDbl_2(tab_retenciones_facturas.getValor("valor_iva_adfac")) - pckUtilidades.CConversion.CDbl_2(tab_retenciones_facturas.getValor("valor_retenido"));
	             tab_comprobante.setValor("valor_pago_tecpo", valor_pago + "");
	             utilitario.addUpdate("tab_detalle_movimiento");
	             set_tramite.cerrar();
             }
             else {        	
            	con_guardar.dibujar();
             }
         }
         else {
        	 utilitario.agregarMensajeError("Seleccione un Registro", "Debe seleccionar al menos un registro para continuar");
        	 return;
         }
     }
     
     public void cancelarRetencion(){
    	 tab_comprobante.actualizar();
         utilitario.addUpdate("tab_comprobante");
    	 con_guardar.cerrar();
     }
     ///calculo el valor de pago
     public void valorPago()
     {
    	 double dou_subtotal=0;
    	 double dou_valor_total=0;
    	 double dou_valor_iva=0;
    	 double dou_valor_retencion=0;
    	 double dou_valor_pago=0;
    	 try{
    		 dou_subtotal = pckUtilidades.CConversion.CDbl_2(tab_comprobante.getValor("subtotal_tecpo"));
    	 }catch (Exception e){}
    		 
		 try{//Arreglar
			 dou_valor_iva = pckUtilidades.CConversion.CDbl_2(tab_comprobante.getValor("valor_iva_tecpo"));
    	 }catch (Exception e){}//Arreglar
		 
    	 try{
    		 dou_valor_retencion = pckUtilidades.CConversion.CDbl_2(tab_comprobante.getValor("valor_retencion_tecpo"));
    	 }catch (Exception e){}
    	 
    	 //dou_valor_iva =dou_subtotal*0.12;  //Arreglar
    	 
    	 BigDecimal big_valor_iva=new BigDecimal(dou_valor_iva);
    	 big_valor_iva=big_valor_iva.setScale(2, RoundingMode.HALF_UP);
    	 
    	 dou_valor_total=dou_subtotal+big_valor_iva.doubleValue();
    	 dou_valor_pago=dou_valor_total-dou_valor_retencion;
    	 
    	 tab_comprobante.setValor("valor_iva_tecpo",big_valor_iva+ "");
    	 tab_comprobante.setValor("valor_compra_tecpo", dou_valor_total+"");
    	 tab_comprobante.setValor("valor_pago_tecpo", dou_valor_pago+"");
    	 utilitario.addUpdateTabla(tab_comprobante, "valor_iva_tecpo,valor_compra_tecpo,valor_pago_tecpo", "");
		 
     }
     ////boton impuesto
     public void importarImpuesto(){
     	System.out.println("entra a metodo import impues");
     	if(tab_comprobante.getValor("valor_compra_tecpo")==null||tab_comprobante.getValor("valor_compra_tecpo").equals("")){
     		utilitario.agregarMensaje("No puede generar una retencion", "Ingrese un valor de compra");
     		return;
     	}
     	if(tab_comprobante.getValor("VALOR_IVA_TECPO")==null||tab_comprobante.getValor("VALOR_IVA_TECPO").equals("")){
     		utilitario.agregarMensaje("No puede generar una retencion", "Ingrese valor Iva");
     		return;
     	}
         set_impuesto.getTab_seleccion().setSql("select ide_tetii,detalle_tetii from tes_tipo_impuesto order by ide_tetii");
         set_impuesto.getTab_seleccion().ejecutarSql();
         set_impuesto.dibujar();
         
     }
     String str_seleccionado="";
     public void aceptarImpuesto(){
     	//System.out.println("entra a metodo aceptar impues");

         if(set_impuesto.isVisible()){
             if (set_impuesto.getValorSeleccionado()!=null){
                 tab_detalle_retencion.insertar();

             	if(set_impuesto.getValorSeleccionado().equals(par_impuesto_iva)){
             		tab_detalle_retencion.setValor("base_imponible_teder", tab_comprobante.getValor("valor_iva_tecpo"));
             	}
             	else if(set_impuesto.getValorSeleccionado().equals(par_impuesto_renta)){
             		tab_detalle_retencion.setValor("base_imponible_teder", tab_comprobante.getValor("valor_compra_tecpo"));
             	}
              str_seleccionado= set_impuesto.getValorSeleccionado();
             //System.out.println("probando que valor me llega"+str_seleccionado);
             set_retencion.getTab_seleccion().setSql(ser_Tesoreria.getImpuesto("true","0",str_seleccionado));
             set_retencion.getTab_seleccion().ejecutarSql();
             set_retencion.dibujar();
             set_impuesto.cerrar();
             
             
             }
             else {
                 utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
             }
                 
         }
     
         else if (set_retencion.isVisible()){
             str_seleccionado= set_retencion.getValorSeleccionado();
             TablaGenerica tab_rentas= utilitario.consultar(ser_Tesoreria.getImpuestoCalculo(str_seleccionado));

             double dou_valor_impuesto=0;
            double dou_porcentaje_calculo=0;
            double dou_valor_resultado=0;

            dou_porcentaje_calculo=pckUtilidades.CConversion.CDbl_2(tab_rentas.getValor("porcentaje_teimp"));
            dou_valor_impuesto=pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getValor("base_imponible_teder"));
            dou_valor_resultado=(dou_porcentaje_calculo*dou_valor_impuesto)/100;
  
            if (set_retencion.getValorSeleccionado()!=null){

                 tab_detalle_retencion.setValor("ide_teimp",str_seleccionado);
                 tab_detalle_retencion.setValor("valor_retenido_teder", dou_valor_resultado+"");   
                 String valorx=tab_detalle_retencion.getSumaColumna("valor_retenido_teder")+"";
                tab_retencion.setValor("total_ret_teret", valorx);       
            }

             set_retencion.cerrar();
              utilitario.addUpdateTabla(tab_detalle_retencion, "valor_retenido_teder,base_imponible_teder,ide_teimp","");
              utilitario.addUpdateTabla(tab_retencion, "total_ret_teret","");
             calcularValorPago();
             utilitario.addUpdateTabla(tab_comprobante, "valor_pago_tecpo,","");

         }


     }
     //Calcular valor impuesto
     public void impuestoCalculo(AjaxBehaviorEvent evt){
    	 tab_detalle_retencion.modificar(evt); //Siempre es la primera linea
        TablaGenerica tab_rentas= utilitario.consultar(ser_Tesoreria.getImpuestoCalculo(tab_detalle_retencion.getValor("ide_teimp")));
        double dou_valor_impuesto=0;
        double dou_porcentaje_calculo=0;
        double dou_valor_resultado=0;

        dou_porcentaje_calculo=pckUtilidades.CConversion.CDbl_2(tab_rentas.getValor("porcentaje_teimp"));
        dou_valor_impuesto=pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getValor("base_imponible_teder"));
        dou_valor_resultado=(dou_porcentaje_calculo*dou_valor_impuesto)/100;
        tab_detalle_retencion.setValor("valor_retenido_teder", dou_valor_resultado+"");   
        String valorx=tab_detalle_retencion.getSumaColumna("valor_retenido_teder")+"";
       tab_retencion.setValor("total_ret_teret", valorx);  
       utilitario.addUpdateTabla(tab_detalle_retencion, "valor_retenido_teder,base_imponible_teder,ide_teimp","");
       utilitario.addUpdateTabla(tab_retencion, "total_ret_teret","");
      calcularValorPago();
      utilitario.addUpdateTabla(tab_comprobante, "valor_pago_tecpo,","");

    	 
     }
     ////calcular ValorPago
     public void calcularValorPago(){
     	double dou_val_compra=0;
     	double dou_total_retencion=0;
     	double dou_valor_pago=0;
     	double dou_valor_iva=0;
		double duo_iva=par_iva;


     	try {
 			//Obtenemos el valor de la cantidad
     		dou_val_compra=pckUtilidades.CConversion.CDbl_2(tab_comprobante.getValor("valor_compra_tecpo"));
 		} catch (Exception e) {
 		}
     	try {
 			//Obtenemos el valor de la cantidad
     		dou_total_retencion=pckUtilidades.CConversion.CDbl_2(tab_retencion.getValor("total_ret_teret"));
 		} catch (Exception e) {
 		}
     
     	dou_valor_pago=dou_val_compra-dou_total_retencion;
     //	dou_valor_iva=dou_val_compra*duo_iva;
     	
     	tab_comprobante.setValor("valor_pago_tecpo", utilitario.getFormatoNumero(dou_valor_pago,2));
     	//tab_comprobante.setValor("valor_iva_tecpo", utilitario.getFormatoNumero(dou_valor_iva,2));
     	tab_comprobante.modificar(tab_comprobante.getFilaActual());//para que haga el update
     	utilitario.addUpdateTabla(tab_comprobante,"valor_pago_tecpo,","");
     }

    public void exportarExcel()
 	{
 	      if(com_anio.getValue()==null){
 	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
 	        return;
 	      }
 	      tab_tablaXls.setSql(ser_contabilidad.getRptComprobantesPago(com_anio.getValue().toString()));
 	      tab_tablaXls.ejecutarSql();
 	      tab_tablaXls.exportarXLS();
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
		 if (tab_comprobante.isFocus()) {
			 utilitario.agregarMensajeInfo("No puede insertar", "Debe buscar Solicitud de Compra o una certificación presupuestaria");
			 //tab_comprobante.insertar();
	        }

	        else if (tab_detalle_movimiento.isFocus()){
	            tab_detalle_movimiento.insertar();
	            tab_detalle_movimiento.setValor("ide_comov", tab_comprobante.getValor("ide_comov"));
	            utilitario.addUpdate("tab_detalle_movimiento");
	        }
	        else if(tab_pre_mensual.isFocus()){
	        	
	        	utilitario.agregarMensajeInfo("No puede insertar", "Utilice el botón DEVENGAR...");
	        	return;
	        	/*
	        	tab_pre_mensual.insertar();
	        	tab_pre_mensual.setValor("ide_codem", tab_detalle_movimiento.getValor("ide_codem"));
	        	tab_pre_mensual.setValor("comprobante_prmen", tab_comprobante.getValor("comprobante_egreso_tecpo"));
	        	tab_pre_mensual.setValor("fecha_ejecucion_prmen", tab_comprobante.getValor("fecha_tecpo"));
	        	tab_pre_mensual.setValor("devengado_prmen", "0.00");
	        	tab_pre_mensual.setValor("cobrado_prmen", "0.00");
	        	tab_pre_mensual.setValor("cobradoc_prmen", "0.00");
	        	tab_pre_mensual.setValor("pagado_prmen", "0.00");
	        	tab_pre_mensual.setValor("comprometido_prmen", "0.00");
	        	tab_pre_mensual.setValor("valor_anticipo_prmen", "0.00");
	        	tab_pre_mensual.setValor("certificado_prmen", "0.00");*/
	        }    
	
	}

	/////botones fin,siguiente,atras,ultimo.inicio
	  @Override
    public void inicio() {
        super.inicio();

        // TODO Auto-generated method stub
        if (tab_comprobante.isFocus()){
            tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
            tab_detalle_movimiento.ejecutarSql();
            utilitario.addUpdate("tab_detalle_movimiento");
            tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());   
        }
    }
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
    	super.siguiente();
        if (tab_comprobante.isFocus()){
            tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
            tab_detalle_movimiento.ejecutarSql();
            utilitario.addUpdate("tab_detalle_movimiento");
            tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());
        }

        
    }
    @Override
    public void atras() {
        super.atras();

        // TODO Auto-generated method stub
        if (tab_comprobante.isFocus()){
            tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
            tab_detalle_movimiento.ejecutarSql();
            utilitario.addUpdate("tab_detalle_movimiento");
            tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());
        }

    }

    @Override
    public void fin() {
	        super.fin();

	        // TODO Auto-generated method stub
	        if (tab_comprobante.isFocus()){
	            tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
	            tab_detalle_movimiento.ejecutarSql();
	            utilitario.addUpdate("tab_detalle_movimiento");
	            tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());
	        }

	    }
    

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		//LIMPIAR SISOPCION PREMENSUAL
		ser_contabilidad.limpiarAcceso("tes_comprobante_pago");
		ser_contabilidad.limpiarAcceso("cont_detalle_movimiento");
		ser_contabilidad.limpiarAcceso("pre_mensual");
		if(tab_comprobante.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_comprobante_pago), par_sec_comprobante_pago);
		}
		
		if( pckUtilidades.CConversion.CBol(tab_comprobante.getValor("aprobado_tecpo")))
		{
			utilitario.agregarMensajeInfo("Comprobante de PAGO-Aprobado", "Registro no Editable.!");
			return;		
		}
		
		if(validar_movimiento_bloqueado(tab_comprobante.getValor("fecha_tecpo"))){
			utilitario.agregarMensajeInfo("Registro no Editable", "El mes se encuentra bloqueado.");
			return;
		}
		
		TablaGenerica tab_mes =utilitario.consultar("SELECT ide_gemes, detalle_gemes, coalesce(bloqueado_gemes,false) as bloqueado FROM gen_mes where ide_gemes=extract(month from cast('"+tab_comprobante.getValor("fecha_tecpo")+"' as date))");		
		
		if(tab_mes.getValor("bloqueado").equals("true")){
			utilitario.agregarNotificacionInfo("Registro no Editable", "El Mes se encuentra bloqueado");
			return;
		}
		
		 if (tab_comprobante.guardar()) {
	            if (tab_detalle_movimiento.guardar()) {
	                if( tab_pre_mensual.guardar()){
                        guardarPantalla();
                        
                        if(pckUtilidades.CConversion.CInt(val_ide_adfac)>0)
	                	{
		                	String sql = "update adq_factura set ide_tecpo="+ tab_comprobante.getValor("ide_tecpo")	+ " where ide_adfac="+ val_ide_adfac;
		                	System.out.println("guardar.sql() "+sql);
		        			utilitario.getConexion().ejecutarSql(sql);
	                	}
                        
                        if(pckUtilidades.CConversion.CInt(ide_adlic)>0)
	                	{
		                	String sql = "update adq_liquidacion_compra set ide_tecpo="+ tab_comprobante.getValor("ide_tecpo")	+ " where ide_adlic="+ ide_adlic;
		                	System.out.println("guardar.sql() "+sql);
		        			utilitario.getConexion().ejecutarSql(sql);
	                	}
                        
                        if(pckUtilidades.CConversion.CInt(tab_comprobante.getValor("ide_comov"))>0)
	                	{
		                	String sql = "update cont_movimiento set detalle_comov='"+ tab_comprobante.getValor("detalle_tecpo")+ "', mov_fecha_comov='"+tab_comprobante.getValor("fecha_tecpo")+"', ide_gtemp="+tab_comprobante.getValor("ide_gtemp")+"  where ide_comov="+ tab_comprobante.getValor("ide_comov");
		                	System.out.println("guardar.sql() "+sql);
		        			utilitario.getConexion().ejecutarSql(sql);
	                	}
                        
                        tab_comprobante.ejecutarSql();
                        tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));    
                        tab_detalle_movimiento.ejecutarSql();
                        val_ide_adfac="";
	                }
	            }

	        }
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if( pckUtilidades.CConversion.CBol(tab_comprobante.getValor("aprobado_tecpo")))
		{
			utilitario.agregarMensajeInfo("Comprobante de PAGO-Aprobado", "Registro no Editable.!");
			return;		
		}
		  utilitario.getTablaisFocus().eliminar();
	}


	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();  
		tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
        tab_detalle_movimiento.ejecutarSql();
        utilitario.addUpdate("tab_detalle_movimiento");
        tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());
	}
	
	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();  
		tab_detalle_movimiento.setCondicion("ide_comov="+tab_comprobante.getValor("ide_comov"));
        tab_detalle_movimiento.ejecutarSql();
        utilitario.addUpdate("tab_detalle_movimiento");
        tab_pre_mensual.ejecutarValorForanea(tab_detalle_movimiento.getValorSeleccionado());
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
					//TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+utilitario.getVariable("ide_usua"));
					TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+tab_comprobante.getValor("ide_gtemp"));
					if(tab_usuario.isEmpty())
						tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.nick_usua like '"+tab_comprobante.getValor("usuario_ingre")+"'");
					
					p_parametros=new HashMap();		
					rep_reporte.cerrar();	
					p_parametros.put("titulo", "EMGIRS - EP");
					//p_parametros.put("p_contador_general", utilitario.getVariable("p_nombre_contador"));
					p_parametros.put("p_contador_general", tab_usuario.getValor("nom_usua"));
					p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
					p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	
					p_parametros.put("p_valor_pagar",utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(tab_comprobante.getValor("valor_pago_tecpo"),2)));
					p_parametros.put("p_ide_comov",pckUtilidades.CConversion.CInt(tab_comprobante.getValor("ide_comov")));
					p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_usua")));
					p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_empr")));
					p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt(tab_usuario.getValor("ide_sucu")));
					p_parametros.put("REPORT_LOCALE", locale);
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();
						
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
			}
		}
	}
	
	public Tabla getTab_comprobante() {
		return tab_comprobante;
	}


	public void setTab_comprobante(Tabla tab_comprobante) {
		this.tab_comprobante = tab_comprobante;
	}


	public Tabla getTab_detalle_movimiento() {
		return tab_detalle_movimiento;
	}


	public void setTab_detalle_movimiento(Tabla tab_detalle_movimiento) {
		this.tab_detalle_movimiento = tab_detalle_movimiento;
	}


	public Tabla getTab_retencion() {
		return tab_retencion;
	}


	public void setTab_retencion(Tabla tab_retencion) {
		this.tab_retencion = tab_retencion;
	}


	public Tabla getTab_detalle_retencion() {
		return tab_detalle_retencion;
	}


	public void setTab_detalle_retencion(Tabla tab_detalle_retencion) {
		this.tab_detalle_retencion = tab_detalle_retencion;
	}

	public SeleccionTabla getSet_solicitud() {
		return set_solicitud;
	}

	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_solicitud = set_solicitud;
	}

	public SeleccionTabla getSet_tramite() {
		return set_tramite;
	}

	public void setSet_tramite(SeleccionTabla set_tramite) {
		this.set_tramite = set_tramite;
	}

	public SeleccionTabla getSet_impuesto() {
		return set_impuesto;
	}

	public void setSet_impuesto(SeleccionTabla set_impuesto) {
		this.set_impuesto = set_impuesto;
	}

	public SeleccionTabla getSet_retencion() {
		return set_retencion;
	}

	public void setSet_retencion(SeleccionTabla set_retencion) {
		this.set_retencion = set_retencion;
	}

	public SeleccionTabla getSet_contabilizar() {
		return set_contabilizar;
	}

	public void setSet_contabilizar(SeleccionTabla set_contabilizar) {
		this.set_contabilizar = set_contabilizar;
	}
	public SeleccionTabla getSet_anticipo_empleado() {
		return set_anticipo_empleado;
	}
	public void setSet_anticipo_empleado(SeleccionTabla set_anticipo_empleado) {
		this.set_anticipo_empleado = set_anticipo_empleado;
	}
	public Dialogo getDia_anticipo() {
		return dia_anticipo;
	}
	public void setDia_anticipo(Dialogo dia_anticipo) {
		this.dia_anticipo = dia_anticipo;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	public Tabla getTab_pre_mensual() {
		return tab_pre_mensual;
	}
	public void setTab_pre_mensual(Tabla tab_pre_mensual) {
		this.tab_pre_mensual = tab_pre_mensual;
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
	public Dialogo getDia_presupuesto() {
		return dia_presupuesto;
	}
	public void setDia_presupuesto(Dialogo dia_presupuesto) {
		this.dia_presupuesto = dia_presupuesto;
	}
	public SeleccionTabla getSet_presupuesto_devengado() {
		return set_presupuesto_devengado;
	}
	public void setSet_presupuesto_devengado(
			SeleccionTabla set_presupuesto_devengado) {
		this.set_presupuesto_devengado = set_presupuesto_devengado;
	}

	public SeleccionTabla getSet_liquidacion() {
		return set_liquidacion;
	}

	public void setSet_liquidacion(SeleccionTabla set_liquidacion) {
		this.set_liquidacion = set_liquidacion;
	}

	public Tabla getTab_tablaXls() {
		return tab_tablaXls;
	}

	public void setTab_tablaXls(Tabla tab_tablaXls) {
		this.tab_tablaXls = tab_tablaXls;
	}

	public Dialogo getDia_tipo_anticipo() {
		return dia_tipo_anticipo;
	}

	public void setDia_tipo_anticipo(Dialogo dia_tipo_anticipo) {
		this.dia_tipo_anticipo = dia_tipo_anticipo;
	}

	public Radio getRad_imprimir() {
		return rad_imprimir;
	}

	public void setRad_imprimir(Radio rad_imprimir) {
		this.rad_imprimir = rad_imprimir;
	}


}
