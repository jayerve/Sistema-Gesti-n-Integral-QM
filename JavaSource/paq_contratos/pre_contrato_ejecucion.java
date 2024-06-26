package paq_contratos;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_contratos.ejb.ServicioContrato;
import paq_nomina.ejb.ServicioNomina;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_precontractual.ejb.ServicioFUIxls;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;


public class pre_contrato_ejecucion extends Pantalla { //Expediente del Proceso

	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioContrato ser_contrato = (ServicioContrato) utilitario.instanciarEJB(ServicioContrato.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega ) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioAdquisicion ser_Adquisicion =(ServicioAdquisicion)utilitario.instanciarEJB(ServicioAdquisicion.class);
	@EJB
    private ServicioFUIxls ser_fui_xls = (ServicioFUIxls) utilitario.instanciarEJB(ServicioFUIxls.class);
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	
	private Tabla tab_contratos =new Tabla();
	private Tabla tab_administrador2=new Tabla();
	private Tabla tab_ejecucion =new Tabla();
	private Tabla tab_compromiso =new Tabla();
	private Tabla tab_ingresos =new Tabla();
	private Tabla tab_poliza =new Tabla();
	private Tabla tab_requisitos =new Tabla();
	private Tabla tab_requisitos_secundario =new Tabla();
	
	private Combo com_anio=new Combo();

	private String empleado;


	public pre_contrato_ejecucion() {

	/*	if(!pckUtilidades.Utilitario.obtenerIPhost().contains("SRV103ERP02"))
		{
			//utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor ERP desde la pagina servicios emgirs");
			utilitario.agregarMensajeInfo("Permisos", "Contacte con el adminsitrador... o use el servidor ERP...");
			//return;
		}*/
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado: "+empleado);
		
		//bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		Boton bot_cargar_req = new Boton();
		bot_cargar_req.setValue("Subir Habilitante");
		bot_cargar_req.setMetodo("insertarRequisito");
		bot_cargar_req.setIcon("ui-icon-plusthick");
		bar_botones.agregarBoton(bot_cargar_req);
		
		Boton bot_cargar_req_sec = new Boton();
		bot_cargar_req_sec.setValue("Subir Anexos");
		bot_cargar_req_sec.setMetodo("insertarRequisitoSecundario");
		bot_cargar_req_sec.setIcon("ui-icon-plusthick");
		bar_botones.agregarBoton(bot_cargar_req_sec);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("GENERAR FUI");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
  		
  		Boton bot_generar= new Boton();
  		bot_generar.setIcon("ui-icon-folder-collapsed");
		bot_generar.setValue("Exportar Expediente");
		bot_generar.setMetodo("generarArchivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		tab_contratos.setId("tab_contratos");
		tab_contratos.setTipoFormulario(true);
		tab_contratos.getGrid().setColumns(1);
		tab_contratos.setHeader("MIS CONTRATOS");
		tab_contratos.setTabla("pre_contrato", "ide_prcon", 1);
		
		tab_contratos.setCondicion("tipo_int_ext_prcon in (1,2) and ide_prcon=-1");
		tab_contratos.getColumna("ide_prcon").setNombreVisual("CÓDIGO");
		tab_contratos.getColumna("activo_prcon");
		tab_contratos.getColumna("activo_prcon").setNombreVisual("ACTIVO");
		tab_contratos.getColumna("activo_prcon").setValorDefecto("true");
		
		Object fila1[] = { "0", "Contrato Interno" };
		Object fila2[] = { "1", "Contrato Externo" };
		Object fila3[] = { "2", "Infima Cuantía" };
		Object fila4[] = { "3", "APP_WEB" };
		
		List lista = new ArrayList();
		lista.add(fila2);
		lista.add(fila3);
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadio(lista, "1");
		tab_contratos.getColumna("tipo_int_ext_prcon").setValorDefecto("1");
		tab_contratos.getColumna("tipo_int_ext_prcon").setRadioVertical(true);
		tab_contratos.getColumna("tipo_int_ext_prcon").setNombreVisual("Tipo Contrato");
		tab_contratos.getColumna("ide_prtsc").setVisible(false);
		tab_contratos.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_contratos.getColumna("fecha_firma_prcon");
		tab_contratos.getColumna("fecha_firma_prcon").setNombreVisual("Firma de Contrato");
		tab_contratos.getColumna("fecha_firma_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contratos.getColumna("fecha_inicio_prcon");
		tab_contratos.getColumna("fecha_inicio_prcon").setNombreVisual("Fecha de Inicio");
		tab_contratos.getColumna("fecha_inicio_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contratos.getColumna("OBSERVACION_PRCON");
		tab_contratos.getColumna("OBSERVACION_PRCON").setNombreVisual("Objeto del Contrato");
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON");
		tab_contratos.getColumna("NUMERO_CONTRATO_PRCON").setNombreVisual("Número de Contrato");
		tab_contratos.getColumna("MONTO_ORIGINAL_PRCON");
		tab_contratos.getColumna("MONTO_ORIGINAL_PRCON").setNombreVisual("Monto Original del Contrato");
		tab_contratos.getColumna("MONTO_PRCON");
		tab_contratos.getColumna("MONTO_PRCON").setNombreVisual("Monto del Contrato");
		tab_contratos.getColumna("FECHA_ANTICIPO_PRCON");
		tab_contratos.getColumna("FECHA_ANTICIPO_PRCON").setNombreVisual("Fecha de Anticipo");
		tab_contratos.getColumna("MONTO_ANTICIPO_PRCON");
		tab_contratos.getColumna("MONTO_ANTICIPO_PRCON").setNombreVisual("Monto del Anticipo");
		tab_contratos.getColumna("ide_pretc").setVisible(false);
		tab_contratos.getColumna("IDE_TETAR").setVisible(false);
		tab_contratos.getColumna("CON_IDE_PRCON").setVisible(false);
		tab_contratos.getColumna("ide_recli").setVisible(false);
		tab_contratos.getColumna("IDE_PERDE");
		tab_contratos.getColumna("IDE_PERDE").setVisible(false);
		tab_contratos.getColumna("IDE_PERRE");
		tab_contratos.getColumna("IDE_PERRE").setVisible(false);
		tab_contratos.getColumna("plazo_prcon");
		tab_contratos.getColumna("plazo_prcon").setNombreVisual("Plazo del Contrato");
		tab_contratos.getColumna("plazo_prcon").setVisible(false);
		tab_contratos.getColumna("ide_prtip").setVisible(false);
		tab_contratos.getColumna("fecha_fin_prcon");
		tab_contratos.getColumna("fecha_fin_prcon").setNombreVisual("Fecha fín de Contrato");
		tab_contratos.getColumna("fecha_cierre_prcon");
		tab_contratos.getColumna("fecha_cierre_prcon").setNombreVisual("Fecha Cierre Contrato");
		tab_contratos.getColumna("fecha_suspension_prcon");
		tab_contratos.getColumna("fecha_suspension_prcon").setNombreVisual("Fec. de Suspención Contrato");
		tab_contratos.getColumna("motivo_suspension_prcon");
		tab_contratos.getColumna("motivo_suspension_prcon").setNombreVisual("Motivo de Suspención");
		tab_contratos.getColumna("num_generador_desecho_prcon").setVisible(false);
		tab_contratos.getColumna("ide_reclr").setVisible(false);
		tab_contratos.getColumna("estimado_desecho_prcon").setVisible(false);
		tab_contratos.getColumna("ESTIMADO_DESECHO").setVisible(false);
		tab_contratos.getColumna("ide_coest").setCombo("cont_estado", " ide_coest", "detalle_coest", "");
		tab_contratos.getColumna("ide_coest").setValorDefecto("31");
		tab_contratos.getColumna("ide_coest").setNombreVisual("Estado");
		tab_contratos.getColumna("nro_contrato_prcon");
		tab_contratos.getColumna("nro_contrato_prcon").setVisible(false);
		tab_contratos.getColumna("ide_tepro").setCombo(ser_contrato.getProveedores());
		tab_contratos.getColumna("ide_tepro").setAutoCompletar();
		tab_contratos.getColumna("ide_tepro").setLectura(false);
		tab_contratos.getColumna("ide_tepro").setNombreVisual("Proveedor");
		tab_contratos.getColumna("ide_tepro").setLectura(true);
		tab_contratos.getColumna("ide_prcop").setVisible(false);
		tab_contratos.getColumna("IDE_GEPLF").setVisible(false);
		tab_contratos.getColumna("contrato_prdoc").setUpload("Contratos");
		
		tab_contratos.agregarRelacion(tab_poliza);
		tab_contratos.agregarRelacion(tab_administrador2);//agraga relacion para los tabuladores
		tab_contratos.agregarRelacion(tab_ejecucion);
		tab_contratos.setLectura(true);
		tab_contratos.dibujar();
		
		PanelTabla pat_contratacion=new PanelTabla();
		pat_contratacion.setPanelTabla(tab_contratos);
		
		//tabla administrador_contrato
		tab_administrador2.setId("tab_administrador2");
		tab_administrador2.setHeader("ADMINISTRADOR CONTRATO");
		tab_administrador2.setTabla("pre_contrato_administrador", "ide_pradc", 2);
		tab_administrador2.setTipoFormulario(true);
		tab_administrador2.getGrid().setColumns(1);		
		tab_administrador2.setCampoForanea("ide_prcon");
		tab_administrador2.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_administrador2.getColumna("IDE_GEEDP").setAutoCompletar();		
		tab_administrador2.getColumna("fecha_inicio_pradc").setVisible(false);
		tab_administrador2.getColumna("fecha_fin_pradc").setVisible(false);
		tab_administrador2.getColumna("IDE_GTEMP").setVisible(false);	
		tab_administrador2.setCondicion(" activo_pradc=true ");
		tab_administrador2.setLectura(true);
		tab_administrador2.dibujar();		
		PanelTabla pat_administrador =new PanelTabla();
		pat_administrador.setPanelTabla(tab_administrador2);
		
		tab_ejecucion.setId("tab_ejecucion");
		tab_ejecucion.setHeader("EJECUCION - ENTREGAS");
		tab_ejecucion.setTabla("pre_contrato_ejecucion","ide_prcej",3);	
		tab_ejecucion.getColumna("ide_prcfa").setCombo("pre_contrato_fase","ide_prcfa","descripcion_prcfa","");
		tab_ejecucion.getColumna("fecha_prcej").setValorDefecto(utilitario.getFechaActual());
		tab_ejecucion.getColumna("monto_ejecutado_prcej").setValorDefecto("0");
		tab_ejecucion.getColumna("saldo_por_ejecutar_prcej").setValorDefecto("0");
		tab_ejecucion.getColumna("activo_prcej").setValorDefecto("true");
		tab_ejecucion.onSelect("seleccionarEjecucion");
		tab_ejecucion.setCampoForanea("ide_prcon");
		tab_ejecucion.agregarRelacion(tab_requisitos);
		tab_ejecucion.dibujar();
		PanelTabla pat_ejecucion = new PanelTabla();
		pat_ejecucion.setPanelTabla(tab_ejecucion);
		
		tab_compromiso.setHeader("PAGOS");
		tab_compromiso.setId("tab_compromiso");
		tab_compromiso.setIdCompleto("tab_tabulador:tab_compromiso");
		tab_compromiso.setSql(ser_presupuesto.getRptEjecucionPresupuestoContratos("-1"));
		tab_compromiso.setLectura(true);
		tab_compromiso.dibujar();
		PanelTabla pat_compromiso= new PanelTabla();
		pat_compromiso.setPanelTabla(tab_compromiso);
		
		tab_ingresos.setHeader("INGRESOS");
		tab_ingresos.setId("tab_ingresos");
		tab_ingresos.setIdCompleto("tab_tabulador:tab_ingresos");
		tab_ingresos.setSql(ser_Adquisicion.getFacturasCompra("-1"));
		tab_ingresos.setLectura(true);
		tab_ingresos.dibujar();
		PanelTabla pat_ingreso= new PanelTabla();
		pat_ingreso.setPanelTabla(tab_ingresos);
		
		tab_poliza.setId("tab_poliza");
		tab_poliza.setHeader("POLIZAS");
		tab_poliza.setTabla("tes_poliza", "ide_tepol", 5);
		tab_poliza.setIdCompleto("tab_tabulador:tab_poliza");
		tab_poliza.setTipoFormulario(true);
		tab_poliza.getGrid().setColumns(4);
		tab_poliza.getColumna("ide_tepol").setNombreVisual("CÓDIGO");
		tab_poliza.getColumna("ide_tepol").setOrden(1);
		tab_poliza.getColumna("activo_tepol");
		tab_poliza.getColumna("activo_tepol").setNombreVisual("ACTIVO");
		tab_poliza.getColumna("activo_tepol").setOrden(2);
		tab_poliza.getColumna("activo_tepol").setValorDefecto("true");
		tab_poliza.getColumna("ide_prcon").setCombo(ser_contrato.getContratos());
		tab_poliza.getColumna("ide_prcon").setAutoCompletar();
		tab_poliza.getColumna("ide_prcon").setLectura(false);
		tab_poliza.getColumna("ide_prcon").setNombreVisual("Contrato");
		tab_poliza.getColumna("ide_prcon").setOrden(3);
		tab_poliza.getColumna("ide_tetip").setCombo("tes_tipo_poliza", "ide_tetip", "detalle_tetip", "");
		tab_poliza.getColumna("ide_tetip").setOrden(4);
		tab_poliza.getColumna("ide_tetip").setNombreVisual("Tipo de Póliza");
		tab_poliza.getColumna("ide_geins").setCombo("gen_institucion", "ide_geins", "detalle_geins", " ide_getii in (1,14) and gen_ide_geins is not null ");
		tab_poliza.getColumna("ide_geins").setOrden(5);
		tab_poliza.getColumna("ide_geins").setNombreVisual("Emisor");
		tab_poliza.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_poliza.getColumna("ide_coest").setOrden(6);
		tab_poliza.getColumna("ide_coest").setNombreVisual("Estado");
		tab_poliza.getColumna("ide_gemos").setCombo("gen_modulo_secuencial", "ide_gemos", "numero_secuencial_gemos", "");
		tab_poliza.getColumna("ide_gemos").setVisible(false);
		tab_poliza.getColumna("numero_poliza_tepol");
		tab_poliza.getColumna("numero_poliza_tepol").setNombreVisual("Número Póliza");
		tab_poliza.getColumna("numero_poliza_tepol").setOrden(7);
		tab_poliza.getColumna("fecha_emision_tepol");
		tab_poliza.getColumna("fecha_emision_tepol").setNombreVisual("Fecha de Emisión");
		tab_poliza.getColumna("fecha_emision_tepol").setOrden(8);
		tab_poliza.getColumna("valor_tepol");
		tab_poliza.getColumna("valor_tepol").setNombreVisual("Valor Póliza");
		tab_poliza.getColumna("valor_tepol").setOrden(9);
		tab_poliza.getColumna("vigencia_desde_tepol");
		tab_poliza.getColumna("vigencia_desde_tepol").setNombreVisual("Vigencia Desde");
		tab_poliza.getColumna("vigencia_desde_tepol").setOrden(10);
		tab_poliza.getColumna("vigencia_hasta_tepol");
		tab_poliza.getColumna("vigencia_hasta_tepol").setNombreVisual("Vigencia Hasta");
		tab_poliza.getColumna("vigencia_hasta_tepol").setOrden(11);
		tab_poliza.getColumna("secuencial_tepol");
		tab_poliza.getColumna("secuencial_tepol").setNombreVisual("Número Secuencia");
		tab_poliza.getColumna("secuencial_tepol").setOrden(12);		
		tab_poliza.getColumna("afianzado_tepol").setNombreVisual("Afianzado");
		tab_poliza.getColumna("afianzado_tepol").setCombo(ser_bodega.getProveedor("true,false"));
		tab_poliza.getColumna("afianzado_tepol").setAutoCompletar();		
		tab_poliza.getColumna("beneficiario_tepol").setNombreVisual("Beneficiario");
		tab_poliza.getColumna("beneficiario_tepol").setCombo("gen_institucion", "ide_geins", "detalle_geins", " ide_getii in (13) and gen_ide_geins is not null ");		
		tab_poliza.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_poliza.setCampoForanea("ide_prcon");
		tab_poliza.setLectura(true);
		tab_poliza.dibujar();
		PanelTabla pat_poliza= new PanelTabla();
		pat_poliza.setPanelTabla(tab_poliza);
		
		
		
		tab_requisitos.setId("tab_requisitos");
		tab_requisitos.setHeader("DOCUMENTOS EJECUCION");
		tab_requisitos.setTipoFormulario(true);
		tab_requisitos.getGrid().setColumns(2);
		tab_requisitos.setIdCompleto("tab_tabulador:tab_requisitos");
		tab_requisitos.setTabla("pre_contrato_archivo","ide_prcar",10);
		tab_requisitos.getColumna("documentoadjunto_prcar").setUpload("ejecucioncontrato");
		tab_requisitos.getColumna("ide_prreq").setCombo("select ide_prreq,descripcion_prreq from precon_requisito req "
														+ "join precon_etapa eta on eta.ide_preta=req.ide_preta "
														+ "where activo_prreq=true and fase_ejecucion_preta=true ");
		tab_requisitos.getColumna("ide_prreq").setFiltroContenido();
		tab_requisitos.getColumna("ide_prtip").setCombo("precon_tipologia","ide_prtip","descripcion_prtip","");
		tab_requisitos.getColumna("fecha_prcar").setValorDefecto(utilitario.getFechaActual());
		tab_requisitos.getColumna("presenta_prcar").setCombo(utilitario.getListaSiNo());
		tab_requisitos.getColumna("activo_prcar").setLectura(true);
		tab_requisitos.getColumna("nro_fojas_prcar").setValorDefecto("1");
		tab_requisitos.getColumna("original_prcar").setValorDefecto("true");
		tab_requisitos.getColumna("activo_prcar").setValorDefecto("true");
		tab_requisitos.getColumna("con_ide_prcar").setVisible(false);
		tab_requisitos.getColumna("ide_prcon").setVisible(false);
		tab_requisitos.getColumna("anexo_prcar").setVisible(false);
		tab_requisitos.setCondicion("con_ide_prcar is null");
		tab_requisitos.setCampoOrden("ide_prcar desc");
		tab_requisitos.setCampoForanea("ide_prcej");
		tab_requisitos.dibujar();
		PanelTabla pat_requisito= new PanelTabla();
		pat_requisito.setPanelTabla(tab_requisitos);
		
		tab_requisitos_secundario.setId("tab_requisitos_secundario");
		tab_requisitos_secundario.setHeader("DOCUMENTOS EJECUCION (SECUNDARIO)");
		tab_requisitos_secundario.setIdCompleto("tab_tabulador:tab_requisitos_secundario");
		tab_requisitos_secundario.setTipoFormulario(true);
		tab_requisitos_secundario.getGrid().setColumns(2);
		tab_requisitos_secundario.setTabla("pre_contrato_archivo","ide_prcar",11);
		tab_requisitos_secundario.getColumna("documentoadjunto_prcar").setUpload("ejecucioncontrato");
		tab_requisitos_secundario.getColumna("ide_prreq").setCombo("select ide_prreq,descripcion_prreq from precon_requisito req "
																	+ "join precon_etapa eta on eta.ide_preta=req.ide_preta "
																	+ "where activo_prreq=true and fase_ejecucion_preta=true ");
		tab_requisitos_secundario.getColumna("ide_prreq").setFiltroContenido();
		tab_requisitos_secundario.getColumna("ide_prtip").setCombo("precon_tipologia","ide_prtip","descripcion_prtip","");
		tab_requisitos_secundario.getColumna("fecha_prcar").setValorDefecto(utilitario.getFechaActual());
		tab_requisitos_secundario.getColumna("presenta_prcar").setCombo(utilitario.getListaSiNo());
		tab_requisitos_secundario.getColumna("activo_prcar").setLectura(true);
		tab_requisitos_secundario.getColumna("original_prcar").setValorDefecto("true");
		tab_requisitos_secundario.getColumna("activo_prcar").setValorDefecto("true");
		tab_requisitos_secundario.getColumna("con_ide_prcar").setVisible(false);
		tab_requisitos_secundario.getColumna("ide_prcon").setVisible(false);
		tab_requisitos_secundario.getColumna("asunto_prcar").setVisible(false);
		tab_requisitos_secundario.getColumna("de_nombre_prcar").setVisible(false);
		tab_requisitos_secundario.getColumna("para_nombre_prcar").setVisible(false);
		tab_requisitos_secundario.getColumna("nro_fojas_prcar").setVisible(false);
		tab_requisitos_secundario.setCondicion("con_ide_prcar=-1");
		tab_requisitos_secundario.setCampoOrden("ide_prcar desc");
		tab_requisitos_secundario.setCampoForanea("ide_prcej");
		tab_requisitos_secundario.dibujar();
		PanelTabla pat_requisito_sec= new PanelTabla();
		pat_requisito_sec.setPanelTabla(tab_requisitos_secundario);
		
		Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
		
		Division div_division = new Division();  
		div_division.dividir2(pat_contratacion, pat_administrador, "70%", "H"); 
		
		Division div_division2 = new Division();  
		div_division2.dividir2(pat_requisito, pat_requisito_sec, "65%", "V"); 
		
		tab_Tabulador.agregarTab("DOCUMENTOS", div_division2);
		tab_Tabulador.agregarTab("COMPROMISOS", pat_compromiso);
		tab_Tabulador.agregarTab("POLIZAS", pat_poliza);
		tab_Tabulador.agregarTab("INGRESOS", pat_ingreso);
		
		Division div_division3 = new Division();  
		div_division3.dividir2(pat_ejecucion, tab_Tabulador, "35%", "H"); 
		
		Division div_div = new Division();  
		div_div.dividir2(div_division, div_division3, "20%", "V"); 
		agregarComponente(div_div); 	

	}
	
	public void seleccionaElAnio (){   	
		String sqlAnio="tipo_int_ext_prcon in (1,2)";
		if(com_anio.getValue()!=null){
			sqlAnio+=" and extract(year from fecha_firma_prcon)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ";				
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
			return;
		}

		tab_contratos.setCondicion(sqlAnio);
		tab_contratos.ejecutarSql();
		tab_administrador2.ejecutarValorForanea(tab_contratos.getValorSeleccionado());
		tab_ejecucion.ejecutarValorForanea(tab_contratos.getValorSeleccionado());
		tab_compromiso.setSql(ser_presupuesto.getRptEjecucionPresupuestoContratos(tab_contratos.getValorSeleccionado()));
		tab_compromiso.ejecutarSql();
		tab_ingresos.setSql(ser_Adquisicion.getFacturasCompra(tab_contratos.getValorSeleccionado()));
		tab_ingresos.ejecutarSql();
		tab_poliza.ejecutarValorForanea(tab_contratos.getValorSeleccionado());
		tab_requisitos.setCondicion("con_ide_prcar is null and ide_prcej="+tab_ejecucion.getValorSeleccionado());
		tab_requisitos.ejecutarSql();
		if(tab_requisitos.getTotalFilas()>0)
		{
			tab_requisitos_secundario.setCondicion("con_ide_prcar="+tab_requisitos.getValor("ide_prcar"));
			tab_requisitos_secundario.ejecutarSql();
		}
	}
	
	public void seleccionarEjecucion(SelectEvent evt) {
		tab_ejecucion.seleccionarFila(evt);
		tab_requisitos.ejecutarValorForanea(tab_ejecucion.getValorSeleccionado());
		tab_compromiso.setSql(ser_presupuesto.getRptEjecucionPresupuestoContratos(tab_contratos.getValorSeleccionado()));
		tab_compromiso.ejecutarSql();
		tab_ingresos.setSql(ser_Adquisicion.getFacturasCompra(tab_contratos.getValorSeleccionado()));
		tab_ingresos.ejecutarSql();
		//tab_requisitos.setCondicion("con_ide_prcar is null and ide_prcej="+tab_ejecucion.getValorSeleccionado());
		//tab_requisitos.imprimirSql();
		//tab_requisitos.ejecutarSql();
		//utilitario.addUpdate("tab_requisitos");		
		if(tab_requisitos.getTotalFilas()>0)
		{
			tab_requisitos_secundario.setCondicion("con_ide_prcar="+tab_requisitos.getValor("ide_prcar"));
			tab_requisitos_secundario.ejecutarSql();
		}
	}
	
	public void insertarRequisito()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(tab_ejecucion.getTotalFilas()<=0)
		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Guardar una ejecución");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_ejecucion.getValor("ide_prcej"))<=0)
		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Guardar una ejecución");
			return;
		}
		
		utilitario.agregarMensaje("Cargar Archivo", "Utilice el boton Cargar Archivo");
		
		tab_requisitos.insertar();
		tab_requisitos.setValor("ide_prcon", tab_contratos.getValor("ide_prcon"));
		
	}
	
	public void insertarRequisitoSecundario()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(tab_ejecucion.getTotalFilas()<=0)
		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Guardar una ejecución");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_ejecucion.getValor("ide_prcej"))<=0)
		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Guardar una ejecución");
			return;
		}

		if(tab_requisitos.getTotalFilas()>0)
		{
			utilitario.agregarMensaje("Cargar Archivo", "Utilice el boton Cargar Archivo");
			
			if( pckUtilidades.CConversion.CInt(tab_requisitos.getValor("ide_prcar"))>0)
			{
				tab_requisitos_secundario.insertar();
				tab_requisitos_secundario.setValor("con_ide_prcar", tab_requisitos.getValor("ide_prcar"));
				tab_requisitos_secundario.setValor("ide_prcon", tab_contratos.getValor("ide_prcon"));
				tab_requisitos_secundario.setValor("ide_prcej", tab_ejecucion.getValor("ide_prcej"));
			}
			else
				utilitario.agregarMensajeInfo("Guarde el requisito antes de continuar...", "");
		}
	}
	
	public void exportarExcel(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(tab_requisitos.getTotalFilas()<1){
			utilitario.agregarMensajeInfo("Sin registros", "");
			//return;			
		}

		ser_fui_xls.exportarFUIejecucion(tab_ejecucion.getValor("ide_prcej"), tab_contratos.getValorArreglo(tab_contratos.getFilaActual(), "ide_pretp", 1), 
								tab_contratos.getValor("observacion_prcon"),
								tab_administrador2.getValorArreglo(tab_administrador2.getFilaActual(), "IDE_GEEDP", 2),
								tab_administrador2.getValorArreglo(tab_administrador2.getFilaActual(), "IDE_GEEDP", 3),
								false);
		System.out.println("FUI Exportado exitosamente...");
	}
	
	public void generarArchivo(){	
		try {
			
			if(tab_ejecucion.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("Selecione un Proceso", "");
				return;
			}	
			
			TablaGenerica tab_archivos=utilitario.consultar("SELECT ide_prcar,nro_documento_prcar, descripcion_prreq, documentoadjunto_prcar, con_ide_prcar "
															+" FROM pre_contrato_archivo pdr "
															+" left join precon_requisito pr on pr.ide_prreq=pdr.ide_prreq "
															+" where activo_prcar=true and ide_prcej= "+pckUtilidades.CConversion.CInt(tab_ejecucion.getValor("ide_prcej"))
															+" order by 1 ");
			
			if(tab_archivos.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("SIN ARCHIVOS DEL PROCESO", "");
				return;
			}
			
			String fileName = tab_contratos.getValor("numero_contrato_prcon")+"_"+tab_archivos.getValor("descripcion_prreq");
			List<File> anexosPrincipales=new ArrayList();
			int item=1;
			for(int i=0;i<tab_archivos.getTotalFilas();i++){
				
				if(pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "documentoadjunto_prcar")).length()>4)
				{
					String ruta=utilitario.getPropiedad("rutaDownload")+(tab_archivos.getValor(i, "documentoadjunto_prcar"));	
					//String nombre=tab_archivos.getValor(i, "etapa_prdoc");s 
					String nombre=pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "nro_documento_prcar")) + " - "+fileName;
					
					try{anexosPrincipales.add(utilitario.descargarArchivo(ruta, item+"_" +pckUtilidades.Utilitario.quitarCaracteresSpeciales(nombre), ruta.substring(ruta.length()-5, ruta.length())));}catch(Exception e){}
					item++;
				}
			}

			utilitario.crearArchivoZIP(anexosPrincipales, fileName.concat(".zip"));		
			
			
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.crearError("PROCESOS", "generarArchivo", e);
		}
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_ejecucion.isFocus()) {
			tab_ejecucion.insertar();
		}
		
		if (tab_requisitos.isFocus()) {
			tab_requisitos.insertar();
			tab_requisitos.setValor("ide_prcon", tab_contratos.getValor("ide_prcon"));
		}
		
		if (tab_requisitos_secundario.isFocus()) 
		{
			if(tab_requisitos.getTotalFilas()>0)
			{
				if( pckUtilidades.CConversion.CInt(tab_requisitos.getValor("ide_prcar"))>0)
				{
					tab_requisitos_secundario.insertar();
					tab_requisitos_secundario.setValor("con_ide_prcar", tab_requisitos.getValor("ide_prcar"));
					tab_requisitos_secundario.setValor("ide_prcon", tab_contratos.getValor("ide_prcon"));
					tab_requisitos_secundario.setValor("ide_prcej", tab_ejecucion.getValor("ide_prcej"));
				}
				else
					utilitario.agregarMensajeInfo("Guarde el requisito antes de continuar...", "");
			}
		}
		
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_ejecucion.guardar()) {
			if (tab_requisitos.guardar()) {
				if( tab_requisitos_secundario.guardar()){
					guardarPantalla();
				}
			}
		}
		
	}
	
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_contratos() {
		return tab_contratos;
	}

	public void setTab_contratos(Tabla tab_contratos) {
		this.tab_contratos = tab_contratos;
	}

	public Tabla getTab_administrador2() {
		return tab_administrador2;
	}

	public void setTab_administrador2(Tabla tab_administrador2) {
		this.tab_administrador2 = tab_administrador2;
	}

	public Tabla getTab_ejecucion() {
		return tab_ejecucion;
	}

	public void setTab_ejecucion(Tabla tab_ejecucion) {
		this.tab_ejecucion = tab_ejecucion;
	}

	public Tabla getTab_requisitos() {
		return tab_requisitos;
	}

	public void setTab_requisitos(Tabla tab_requisitos) {
		this.tab_requisitos = tab_requisitos;
	}

	public Tabla getTab_requisitos_secundario() {
		return tab_requisitos_secundario;
	}

	public void setTab_requisitos_secundario(Tabla tab_requisitos_secundario) {
		this.tab_requisitos_secundario = tab_requisitos_secundario;
	}

	public Tabla getTab_compromiso() {
		return tab_compromiso;
	}

	public void setTab_compromiso(Tabla tab_compromiso) {
		this.tab_compromiso = tab_compromiso;
	}

	public Tabla getTab_poliza() {
		return tab_poliza;
	}

	public void setTab_poliza(Tabla tab_poliza) {
		this.tab_poliza = tab_poliza;
	}

	public Tabla getTab_ingresos() {
		return tab_ingresos;
	}

	public void setTab_ingresos(Tabla tab_ingresos) {
		this.tab_ingresos = tab_ingresos;
	}

}
