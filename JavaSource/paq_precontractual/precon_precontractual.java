package paq_precontractual;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_precontractual.ejb.ServicioDocumentoRequisito;
import paq_precontractual.ejb.ServicioEtapa;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_precontractual.ejb.ServicioEtapaRequisito;
import paq_precontractual.ejb.ServicioFase;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_precontractual.ejb.ServicioPrecontractual;
import paq_precontractual.ejb.ServicioProcedimiento;
import paq_precontractual.ejb.ServicioRequisito;
import paq_precontractual.ejb.ServicioRuta;
import paq_precontractual.ejb.ServicioSeguimiento;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;

public class precon_precontractual extends Pantalla{
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	@EJB
	private ServicioRequisito ser_Requisito = (ServicioRequisito) utilitario.instanciarEJB(ServicioRequisito.class);
	@EJB
	private ServicioSeguimiento ser_Seguimiento = (ServicioSeguimiento) utilitario.instanciarEJB(ServicioSeguimiento.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
    private ServicioPrecontractual ser_precontractual = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);
	@EJB
    private ServicioEtapaRequisito ser_etapa_requisito = (ServicioEtapaRequisito) utilitario.instanciarEJB(ServicioEtapaRequisito.class);
	@EJB
    private ServicioEtapa ser_etapa = (ServicioEtapa) utilitario.instanciarEJB(ServicioEtapa.class);
	@EJB
    private ServicioRuta ser_ruta = (ServicioRuta) utilitario.instanciarEJB(ServicioRuta.class);
	@EJB
    private ServicioProcedimiento ser_procedimiento = (ServicioProcedimiento) utilitario.instanciarEJB(ServicioProcedimiento.class);
	@EJB
    private ServicioDocumentoRequisito ser_docrquisto = (ServicioDocumentoRequisito) utilitario.instanciarEJB(ServicioDocumentoRequisito.class);
	@EJB
    private ServicioFase ser_fase = (ServicioFase) utilitario.instanciarEJB(ServicioFase.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	private Tabla tab_precontractual=new Tabla();
	private Tabla tab_documento_requisito=new Tabla();
	private Tabla tab_seguimiento=new Tabla();

	private SeleccionTabla set_tipo_actividad=new SeleccionTabla();
	private SeleccionTabla set_tipo_actividad_union=new SeleccionTabla();
	private SeleccionTabla set_monto_contratacion=new SeleccionTabla();
	
	private Combo com_anio=new Combo();
	
	public static String p_sec_precontractual;
	public boolean cumpleRequisitos;
	public boolean cumpleAprobacion;
	public boolean enviar_correo=false;
	
	public precon_precontractual(){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		p_sec_precontractual =utilitario.getVariable("p_sec_precontractual");
		Boton bot_enviar = new Boton();
		bot_enviar.setValue("Enviar Actividad");
		bot_enviar.setMetodo("enviarEtapaSiguiente");
		bot_enviar.setIcon("ui-icon-suitcase");
		bar_botones.agregarBoton(bot_enviar);
		
		tab_precontractual.setId("tab_precontractual");
		tab_precontractual.setHeader("PROCESO");
		tab_precontractual.setTabla("precon_precontractual","ide_prpre",1);
		tab_precontractual.setTipoFormulario(true);  //formulario 
		tab_precontractual.getGrid().setColumns(6); //hacer  columnas
		tab_precontractual.getColumna("ide_prpre").setNombreVisual("Código");
		tab_precontractual.getColumna("ide_prpre").setOrden(1);
		tab_precontractual.getColumna("codigo_prpre").setNombreVisual("Número de Registro");
		tab_precontractual.getColumna("codigo_prpre").setOrden(2);
		tab_precontractual.getColumna("codigo_prpre").setLectura(true);
		tab_precontractual.getColumna("codigo_prpre").setEstilo("width:200px");
		//tab_precontractual.getColumna("fecha_prpre");
		tab_precontractual.getColumna("fecha_prpre").setNombreVisual("Fecha de Registro");
		tab_precontractual.getColumna("fecha_prpre").setOrden(3);
		tab_precontractual.getColumna("fecha_prpre").setLectura(true);
		//tab_precontractual.getColumna("hora_prpre");
		tab_precontractual.getColumna("hora_prpre").setVisible(false);
		tab_precontractual.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_precontractual.getColumna("ide_pretp").setNombreVisual("Procedimiento");
		tab_precontractual.getColumna("ide_pretp").setLectura(true);
		tab_precontractual.getColumna("ide_pretp").setOrden(4);
		tab_precontractual.getColumna("ide_pretp").setEstilo("width:300px");
		tab_precontractual.getColumna("descripcion_prpre").setNombreVisual("Descripción");
		tab_precontractual.getColumna("descripcion_prpre").setOrden(5);
		tab_precontractual.getColumna("descripcion_prpre").setRequerida(true);
		
	    tab_precontractual.getColumna("estado_proceso_prpre").setCombo(utilitario.getListaEstadoProceso());
	    tab_precontractual.getColumna("estado_proceso_prpre").setNombreVisual("Estado");
	    tab_precontractual.getColumna("estado_proceso_prpre").setOrden(6);
	    tab_precontractual.getColumna("estado_proceso_prpre").setLectura(true);
	    //tab_precontractual.getColumna("monto_prpre");
		tab_precontractual.getColumna("monto_prpre").setNombreVisual("Monto");
		tab_precontractual.getColumna("monto_prpre").setOrden(7);
		tab_precontractual.getColumna("monto_prpre").setLectura(true);
		//tab_precontractual.getColumna("ide_actual_preta");
		tab_precontractual.getColumna("ide_actual_preta").setVisible(false);
		tab_precontractual.getColumna("actividad_actual_prpre").setNombreVisual("Actividad Actual");
		tab_precontractual.getColumna("actividad_actual_prpre").setOrden(8);
		tab_precontractual.getColumna("actividad_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("actividad_actual_prpre").setEstilo("width:300px");
		//tab_precontractual.getColumna("ide_actual_geedp");
		tab_precontractual.getColumna("ide_actual_geedp").setVisible(false);
		tab_precontractual.getColumna("responsable_actual_prpre");
		tab_precontractual.getColumna("responsable_actual_prpre").setNombreVisual("Responsable Actual");
		tab_precontractual.getColumna("responsable_actual_prpre").setOrden(9);
		tab_precontractual.getColumna("responsable_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("departamento_actual_prpre").setNombreVisual("Departamento Actual");
		tab_precontractual.getColumna("departamento_actual_prpre").setOrden(10);
		tab_precontractual.getColumna("departamento_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("departamento_actual_prpre").setEstilo("width:150px");
		tab_precontractual.getColumna("ide_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "activo_preta=true");
		tab_precontractual.getColumna("ide_preta").setNombreVisual("Actividad a Enviar");
		tab_precontractual.getColumna("ide_preta").setRequerida(false);
		tab_precontractual.getColumna("ide_preta").setLectura(true);
		tab_precontractual.getColumna("ide_preta").setOrden(11);
		tab_precontractual.getColumna("ide_preta").setEstilo("width:300px");
		//tab_precontractual.getColumna("observacion_prpre");
		tab_precontractual.getColumna("observacion_prpre").setNombreVisual("Observación");
		tab_precontractual.getColumna("observacion_prpre").setOrden(12);
		tab_precontractual.getColumna("ide_tepro").setCombo(ser_generalAdm.getProveedores());
		tab_precontractual.getColumna("ide_tepro").setAutoCompletar();
		tab_precontractual.getColumna("ide_tepro").setOrden(13);
		tab_precontractual.getColumna("ide_tepro").setNombreVisual("Proveedor");

	    tab_precontractual.getColumna("aprueba_informetm_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setNombreVisual("¿Aprueba Informe Técnico Motivado?");
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setOrden(14);
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(true);
	    
	    tab_precontractual.getColumna("termino_especificacion_prpre").setCombo(utilitario.getListaTipoProcesoContracion(false));
	    tab_precontractual.getColumna("termino_especificacion_prpre").setNombreVisual("¿Defina si es un Bien o Servicio?");
	    tab_precontractual.getColumna("termino_especificacion_prpre").setOrden(15);
	    tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(true);
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setNombreVisual("¿Bien o Servicio se encuentra en Catálogo Electrónico?");
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setOrden(16);
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(true);
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setNombreVisual("¿Aprueba?");
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setOrden(17);
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(true);
	    tab_precontractual.getColumna("consta_poa_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("consta_poa_prpre").setNombreVisual("¿Consta en POA?");
	    tab_precontractual.getColumna("consta_poa_prpre").setOrden(18);
	    tab_precontractual.getColumna("consta_poa_prpre").setLectura(true);
	    tab_precontractual.getColumna("consta_pac_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("consta_pac_prpre").setNombreVisual("¿Consta en PAC?");
	    tab_precontractual.getColumna("consta_pac_prpre").setOrden(19);
	    tab_precontractual.getColumna("consta_pac_prpre").setLectura(true);
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setNombreVisual("¿Es Favorable?");
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setOrden(20);
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(true);
		//tab_precontractual.getColumna("activo_prpre");
		tab_precontractual.getColumna("activo_prpre").setNombreVisual("ACTIVO");
		tab_precontractual.getColumna("activo_prpre").setValorDefecto("true");
		tab_precontractual.getColumna("activo_prpre").setLectura(true);
		tab_precontractual.getColumna("activo_prpre").setVisible(false);
		tab_precontractual.getColumna("activo_prpre").setOrden(21);
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre");
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setVisible(false);
		tab_precontractual.getColumna("es_proceso_infima_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("es_proceso_infima_prpre").setNombreVisual("¿Es Proceso de Ínfimas?");
		//tab_precontractual.getColumna("es_proceso_infima_prpre").setVisible(false);
		tab_precontractual.getColumna("es_proceso_infima_prpre").setLectura(true);
		tab_precontractual.getColumna("solicitar_alcance_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("solicitar_alcance_prpre").setNombreVisual("¿Se necesita solicitar alcance?");
		tab_precontractual.getColumna("solicitar_alcance_prpre").setLectura(true);
		//tab_precontractual.getColumna("solicitar_alcance_prpre").setVisible(false);
		tab_precontractual.getColumna("esta_ok_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("esta_ok_prpre").setNombreVisual("¿Esta OK?");
		tab_precontractual.getColumna("esta_ok_prpre").setLectura(true);
		tab_precontractual.getColumna("comision_tecnica_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("comision_tecnica_prpre").setNombreVisual("¿Requiere Comisión Técnica?");
		tab_precontractual.getColumna("comision_tecnica_prpre").setLectura(true);
		tab_precontractual.getColumna("es_viable_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("es_viable_prpre").setNombreVisual("¿Viable?");
		tab_precontractual.getColumna("es_viable_prpre").setLectura(true);
		tab_precontractual.getColumna("error_forma_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("error_forma_prpre").setNombreVisual("¿Existe Errores de Forma?");
		tab_precontractual.getColumna("error_forma_prpre").setLectura(true);
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setNombreVisual("¿Convalidación Satisfactoria?");
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(true);
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setNombreVisual("¿Existe más de una oferta?");
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setNombreVisual("¿Recomienda adjudicar?");
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(true);
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setNombreVisual("¿Es necesario reaperturar?");
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setLectura(true);
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setNombreVisual("¿Se recibe la oferta?");
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setNombreVisual("¿Convalidación Satisfactoria y recomienda adjudicar?");
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setLectura(true);
		List lista_monto = new ArrayList();
		    Object fila_monto[] = {
		           "SI", "150.000 - SI"
		    };
		    Object fila_monto1[] = {
		         "NO", "150.000 - NO"
		    };
		    lista_monto.add(fila_monto);
		    lista_monto.add(fila_monto1);
		tab_precontractual.getColumna("es_superior_prpre").setCombo(lista_monto);
		tab_precontractual.getColumna("es_superior_prpre").setNombreVisual("¿Es Superior a?");
		tab_precontractual.getColumna("es_superior_prpre").setLectura(true);
		tab_precontractual.getColumna("manifestacion_interes_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("manifestacion_interes_prpre").setNombreVisual("¿Existen manifestaciones de interés?");
		tab_precontractual.getColumna("manifestacion_interes_prpre").setLectura(true);
		tab_precontractual.getColumna("cumple_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("cumple_prpre").setNombreVisual("¿Cumple?");
		tab_precontractual.getColumna("cumple_prpre").setLectura(true);
		tab_precontractual.setCondicion("ide_prpre=-1");
		tab_precontractual.agregarRelacion(tab_documento_requisito);
		tab_precontractual.agregarRelacion(tab_seguimiento);
		tab_precontractual.dibujar();
		
        PanelTabla pat_precontractual=new PanelTabla();
        pat_precontractual.setPanelTabla(tab_precontractual);
        
        Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
		// detalle documento requisito
		tab_documento_requisito.setTipoFormulario(true);  //formulario 
		tab_documento_requisito.getGrid().setColumns(6); //hacer  columnas
		tab_documento_requisito.setId("tab_documento_requisito");
		tab_documento_requisito.setIdCompleto("tab_tabulador:tab_documento_requisito");
		tab_documento_requisito.setTabla("precon_documento_requisito","ide_prdoc", 2);
		tab_documento_requisito.getColumna("ide_prdoc");
		tab_documento_requisito.getColumna("ide_prdoc").setNombreVisual("Código");
		tab_documento_requisito.getColumna("ide_prreq").setCombo("precon_requisito","ide_prreq","descripcion_prreq","");
		tab_documento_requisito.getColumna("ide_prreq").setNombreVisual("Requisito Actividad");
		tab_documento_requisito.getColumna("ide_prreq").setLectura(true);
		tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLongitud(40);
		tab_documento_requisito.getColumna("fecha_presenta_prdoc").setNombreVisual("Fecha Carga");
		tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(true);
		List lista_presenta_requisito = new ArrayList();
	    Object fila_apr1[] = {
	           "SI", "SI"
	    };
	    Object fila_apr2[] = {
	         "NO", "NO"
	    };
	    lista_presenta_requisito.add(fila_apr1);
	    lista_presenta_requisito.add(fila_apr2);
	    tab_documento_requisito.getColumna("presenta_prdoc").setLongitud(10);
	    tab_documento_requisito.getColumna("presenta_prdoc").setCombo(lista_presenta_requisito);
		tab_documento_requisito.getColumna("presenta_prdoc").setNombreVisual("Subió el Archivo?");
		tab_documento_requisito.getColumna("presenta_prdoc").setLectura(true);
		tab_documento_requisito.getColumna("documentoadjunto_prdoc").setNombreVisual("Archivo Anexo");
		tab_documento_requisito.getColumna("documentoadjunto_prdoc").setUpload("documento");
		tab_documento_requisito.getColumna("descripcion_prdoc").setNombreVisual("Link Portal Archivo Anexo SERCOP");
		tab_documento_requisito.getColumna("descripcion_prdoc").setComentario("Se registra el link donde se subio el archivo del portal de la SERCOP.");

		if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
			tab_documento_requisito.getColumna("descripcion_prdoc").setVisible(true);
		}else{
			tab_documento_requisito.getColumna("descripcion_prdoc").setVisible(false);
		}
		tab_documento_requisito.getColumna("etapa_prdoc");
		tab_documento_requisito.getColumna("etapa_prdoc").setNombreVisual("Actividad");
		tab_documento_requisito.getColumna("etapa_prdoc").setLectura(true);
		tab_documento_requisito.getColumna("activo_prdoc").setNombreVisual("Activo");
		tab_documento_requisito.getColumna("activo_prdoc").setValorDefecto("true");
		tab_documento_requisito.getColumna("activo_prdoc").setLectura(true);
		tab_documento_requisito.dibujar();
		PanelTabla pat_documento_requisito = new PanelTabla();
		pat_documento_requisito.setPanelTabla(tab_documento_requisito);
		
		// detalle seguimiento
		tab_seguimiento.setId("tab_seguimiento");
		tab_seguimiento.setIdCompleto("tab_tabulador:tab_seguimiento");
		tab_seguimiento.setTabla("precon_seguimiento","ide_prseg", 3);
		tab_seguimiento.setSql(ser_Seguimiento.getSeguimientoPorPrecontractual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
		tab_seguimiento.getColumna("ide_prseg").setLongitud(10);
		tab_seguimiento.getColumna("ide_prseg").setNombreVisual("CÓDIGO");
		tab_seguimiento.getColumna("fecha_asignacion_prseg").setLongitud(40);
		tab_seguimiento.getColumna("fecha_asignacion_prseg").setNombreVisual("FECHA ASIGNACIÓN");
		tab_seguimiento.getColumna("hora_asignacion_prseg").setLongitud(40);
		tab_seguimiento.getColumna("hora_asignacion_prseg").setNombreVisual("HORA ASIGNACIÓN");
		tab_seguimiento.getColumna("fecha_cambio_prseg").setLongitud(40);
		tab_seguimiento.getColumna("fecha_cambio_prseg").setNombreVisual("FECHA CAMBIO");
		tab_seguimiento.getColumna("hora_cambio_prseg").setLongitud(40);
		tab_seguimiento.getColumna("hora_cambio_prseg").setNombreVisual("HORA CAMBIO");
		tab_seguimiento.getColumna("etapa_inicio_prseg").setNombreVisual("ACTIVIDAD INICIO");
		tab_seguimiento.getColumna("responsable_prseg").setNombreVisual("RESPONSABLE");
		tab_seguimiento.getColumna("departamento_prseg").setNombreVisual("DEPARTAMENTO");
		tab_seguimiento.getColumna("etapa_fin_prseg").setNombreVisual("ACTIVIDAD FIN");
		tab_seguimiento.getColumna("responsable_asignado_prseg").setNombreVisual("RESPONSABLE ASIGNADO");
		tab_seguimiento.getColumna("departamento_asignado_prseg").setNombreVisual("DEPARTAMENTO");
		tab_seguimiento.getColumna("observacion_prseg").setNombreVisual("OBSERVACIÓN");
		tab_seguimiento.getColumna("estado_actividad_prseg").setNombreVisual("ESTADO ACTIVIDAD");
		tab_seguimiento.getColumna("estado_procedimiento_prseg").setNombreVisual("ESTADO PROCEDIMIENTO");	
		tab_seguimiento.getColumna("activo_prseg").setLongitud(40);
		tab_seguimiento.getColumna("activo_prseg").setNombreVisual("ACTIVO");
		tab_seguimiento.getColumna("ide_preta_prseg").setVisible(false);
		tab_seguimiento.setLectura(true);
		tab_seguimiento.dibujar();
		tab_seguimiento.setLectura(false);
		PanelTabla pat_seguimiento = new PanelTabla();
		pat_seguimiento.setPanelTabla(tab_seguimiento);
		
		tab_Tabulador.agregarTab("REQUISITOS", pat_documento_requisito);
		tab_Tabulador.agregarTab("SEGUIMIENTO", pat_seguimiento);
		
		Division div_division = new Division();
		div_division.dividir2(pat_precontractual,tab_Tabulador,"70%","h");
		agregarComponente(div_division);

		
		//Ventana emergente seleccionar actividad paralela
		set_tipo_actividad.setId("set_tipo_actividad");
		set_tipo_actividad.setSeleccionTabla(ser_EtapaProcedimiento.getEtapaInicioSeguimientoParalelo("SI"),"ide_actual_preta");
		set_tipo_actividad.getTab_seleccion().getColumna("descripcion_preta").setNombreVisual("Actividad");
		set_tipo_actividad.setTitle("Seleccione la Actividad Inicial");
		set_tipo_actividad.getBot_aceptar().setMetodo("aceptarActividad");
		set_tipo_actividad.getBot_cancelar().setMetodo("cancelarActividad");
		set_tipo_actividad.setRadio();
		agregarComponente(set_tipo_actividad);
		//Ventana emergente seleccionar tipo de contratacion
		set_monto_contratacion.setId("set_monto_contratacion");
		set_monto_contratacion.setSeleccionTabla(ser_procedimiento.getProcedimientoContratacion(),"ide_prpro");
		set_monto_contratacion.getTab_seleccion().getColumna("descripcion").setNombreVisual("Tipo de Contratación");
		set_monto_contratacion.getTab_seleccion().getColumna("monto_inferior_prpro").setNombreVisual("Monto Inferior");
		set_monto_contratacion.getTab_seleccion().getColumna("monto_superior_prpro").setNombreVisual("Monto Superior");
		set_monto_contratacion.setTitle("Seleccione el Tipo de Contratación ");
		set_monto_contratacion.getBot_aceptar().setMetodo("aceptarContratacion");
		set_monto_contratacion.getBot_cancelar().setMetodo("cancelarContratacion");
		set_monto_contratacion.setRadio();
		agregarComponente(set_monto_contratacion);
		
		//Ventana emergente seleccionar actividad paralela para la respectiva union
		set_tipo_actividad_union.setId("set_tipo_actividad_union");
		set_tipo_actividad_union.setSeleccionTabla(ser_precontractual.getProcesoParaleloUnion(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_usuario_iniciop_prpre"))),"ide_prpre");
		set_tipo_actividad_union.getTab_seleccion().getColumna("descripcion_prpre").setNombreVisual("Poceso Paralelo");
		set_tipo_actividad_union.setTitle("Seleccione el Proceso a Unir");
		set_tipo_actividad_union.getBot_aceptar().setMetodo("aceptarProcesoUnion");
		//set_tipo_actividad_union.getBot_cancelar().setMetodo("cancelarProcesoUnion");
		set_tipo_actividad_union.setRadio();
		agregarComponente(set_tipo_actividad_union);
		
		//cargarInformacionDependiendoAreaYUsuario("-1");
		mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
		if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))!=0){
			tab_documento_requisito.setSql(ser_docrquisto.getDocumentoRequisito(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
			tab_documento_requisito.dibujar();
			tab_seguimiento.setSql(ser_Seguimiento.getSeguimientoPorPrecontractual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
			tab_seguimiento.setLectura(true);
			tab_seguimiento.dibujar();
			tab_seguimiento.setLectura(false);
		}
		if(tab_precontractual.getValor("ide_prpre")!=null){
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}
	
	public void seleccionaElAnio (){   	
    	cargarInformacionDependiendoAreaYUsuario();
	}


	
	@Override
	public void insertar() {
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		// TODO Auto-generated method stub
		if(tab_precontractual.isFocus()){
			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtempxx));
			TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
			String departamento_actual=ser_generalAdm.servicioDepartamento("true", ide_gtempxx);
			TablaGenerica tab_departamento= utilitario.consultar(departamento_actual);
			String codigo_secuencia=ser_EtapaProcedimiento.getProcedimientos();
			TablaGenerica tab_codigo_secuencia=utilitario.consultar(codigo_secuencia);
			tab_precontractual.getColumna("ide_actual_geedp").setValorDefecto(tab_responsable.getValor("ide_gtemp"));
			tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setValorDefecto(tab_responsable.getValor("ide_gtemp"));
			tab_precontractual.getColumna("responsable_actual_prpre").setValorDefecto(tab_responsable.getValor("empleado"));
			tab_precontractual.getColumna("departamento_actual_prpre").setValorDefecto(tab_departamento.getValor("DETALLE_GEDEP"));
			tab_precontractual.getColumna("fecha_prpre").setValorDefecto(utilitario.getFechaActual());
			tab_precontractual.getColumna("hora_prpre").setValorDefecto(utilitario.getHoraActual());
			tab_precontractual.getColumna("ide_pretp").setValorDefecto("1");
			tab_precontractual.getColumna("codigo_prpre").setValorDefecto(tab_codigo_secuencia.getValor("codigo_secuencia_prpro")+"-"+ser_precontractual.numeroSecuencial(p_sec_precontractual));
			tab_precontractual.getColumna("estado_proceso_prpre").setValorDefecto("REGISTRADO");
			set_tipo_actividad.getTab_seleccion().setSql(ser_EtapaProcedimiento.getEtapaInicioSeguimientoParalelo("SI"));
			set_tipo_actividad.getTab_seleccion().ejecutarSql();
			set_tipo_actividad.dibujar();
		}else if(tab_documento_requisito.isFocus()){
			String requisitoNulo="";
			String ide_noRegistrado="";
			Integer contadorRequisito=0;
			TablaGenerica tab_etapa_req = null;
			String ser_etapa_requisitos=ser_Requisito.getRequisitoPorEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			TablaGenerica tab_requisitoPrecontractual=utilitario.consultar(ser_etapa_requisitos);
			if(tab_documento_requisito.getValor("ide_prreq") != null){
				for(int i = 0; i < tab_requisitoPrecontractual.getTotalFilas(); i++){
					tab_documento_requisito.setValor("ide_prreq", tab_requisitoPrecontractual.getValor(i, "ide_prreq"));
					String ser_etapa_req=ser_etapa_requisito.noRegistradoEnRequisitoPrecontractualYEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")), pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")), pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor("ide_prreq")));
					tab_etapa_req=utilitario.consultar(ser_etapa_req);
					if(!tab_etapa_req.isEmpty()){
						contadorRequisito=contadorRequisito+1;
					}else{
						ide_noRegistrado=tab_requisitoPrecontractual.getValor(i, "ide_prreq");
					}
					
				}
				if(contadorRequisito==tab_requisitoPrecontractual.getTotalFilas()){
					tab_documento_requisito.getColumna("ide_prreq").setValorDefecto("Null");
					requisitoNulo="NULO";
				}else{
					tab_documento_requisito.getColumna("ide_prreq").setValorDefecto(ide_noRegistrado);
				}
				if(requisitoNulo!="NULO"){
					utilitario.addUpdateTabla(tab_documento_requisito, "ide_prreq", "");
					tab_documento_requisito.getColumna("fecha_presenta_prdoc").setValorDefecto(utilitario.getFechaActual());
					String etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
					tab_documento_requisito.getColumna("etapa_prdoc").setValorDefecto(etapa_actual);
					if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
						tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(false);
					}
					tab_documento_requisito.insertar();
				}
				
			}else{
				String ser_prreq=ser_Requisito.getRequisitoPorEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
				TablaGenerica tab_prreq=utilitario.consultar(ser_prreq);
				tab_documento_requisito.getColumna("ide_prreq").setValorDefecto(tab_prreq.getValor("ide_prreq"));
				tab_documento_requisito.getColumna("fecha_presenta_prdoc").setValorDefecto(utilitario.getFechaActual());
				if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
					tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(false);
				}
				String etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
				tab_documento_requisito.getColumna("etapa_prdoc").setValorDefecto(etapa_actual);
				tab_documento_requisito.insertar();
			}
		}
	}
	
	@Override
	public void guardar() {
		List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		boolean finalizado=false;
		if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
			if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")!=null && tab_precontractual.getValor("aprueba_informetm_prpre")==null){
				utilitario.agregarMensajeInfo("El proceso no puede guardar ya tiene que unirse, es actividad paralela.", "");
			}
		}
		if(tab_precontractual.getValor("estado_proceso_prpre").equals("REGISTRADO") || tab_precontractual.getValor("estado_proceso_prpre").equals("EN PROCESO")){
			finalizado=true;
		}
		if(!finalizado){
			utilitario.agregarMensajeInfo("El proceso ya ha finalizado, no se puede modificar.", "");
		}else{
			if(tab_precontractual.guardar()){
				if(tab_precontractual.isFilaInsertada()){
					ser_precontractual.guardaSecuencial(ser_precontractual.numeroSecuencial(p_sec_precontractual), p_sec_precontractual);
				}
				if(tab_documento_requisito.isFocus()){
					if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
						if(tab_documento_requisito.getValor("documentoadjunto_prdoc")==null && tab_documento_requisito.getValor("descripcion_prdoc")==null){
							tab_documento_requisito.setValor("presenta_prdoc","NO");
						}else{
							tab_documento_requisito.setValor("presenta_prdoc","SI");
						}
					}else{
						if(tab_documento_requisito.getValor("documentoadjunto_prdoc")==null){
							tab_documento_requisito.setValor("presenta_prdoc","NO");
						}else{
							tab_documento_requisito.setValor("presenta_prdoc","SI");
						}
					}
					tab_documento_requisito.guardar();
				}
			}
			guardarPantalla();
		}
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	/**
	 * Carga la actividad actual seleccionada en el popup
	 */
	public void aceptarActividad(){
		if(set_tipo_actividad.getValorSeleccionado()!=null){
			cargarEtapaInicial(1,pckUtilidades.CConversion.CInt(set_tipo_actividad.getValorSeleccionado()));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(set_tipo_actividad.getValorSeleccionado()));
			tab_precontractual.insertar();
			set_tipo_actividad.cerrar();
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar una actividad.", "");
		}

	}
	/**
	 * Cancela el poppup de seleccion de actividad actual
	 */
	public void cancelarActividad(){
		tab_precontractual.ejecutarSql();
		set_tipo_actividad.cerrar();
	}
	/**
	 * Carga el proceso a unir actual seleccionada en el popup
	 */
	public void aceptarProcesoUnion(){
		if(set_tipo_actividad_union.getValorSeleccionado()!=null){
			unionActividadParalela(pckUtilidades.CConversion.CInt(set_tipo_actividad_union.getValorSeleccionado()),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			set_tipo_actividad_union.cerrar();
			utilitario.addUpdate("tab_precontractual");
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar el Proceso a Unir.", "");
		}

	}
	/**
	 * Cancela el poppup de seleccion de proceso a unir
	 */
	public void cancelarProcesoUnion(){
		//tab_precontractual.ejecutarSql();
		set_tipo_actividad_union.cerrar();
	}
	
	/**
	 * Metodo que permite cargar la informacion de acuerdo al usuario conectado o asignado a la actividad
	 */
	public void cargarInformacionDependiendoAreaYUsuario(){
		try {

			String sqlAnio="-1";
			if(com_anio.getValue()!=null){
				sqlAnio=" extract(year from fecha_prpre)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ";				
			}
			else{
				utilitario.agregarMensajeInfo("Selecione un año", "");
				return;
			}
			
			List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
			if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR")){
				//tab_precontractual.setCondicion("activo_prpre=true");
				String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
				tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and ide_actual_geedp="+ide_gtempxx+"");
				tab_precontractual.ejecutarSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}else if(validarPerfil(perfilUsuarioConectado,"PRECONTRACTUAL AVANZADO")){
				String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
				tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and ide_actual_geedp="+ide_gtempxx+"");
				tab_precontractual.ejecutarSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}else if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
				String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
				tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and ide_actual_geedp="+ide_gtempxx+"");
				tab_precontractual.ejecutarSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}
			
		} catch (Exception e) {
			System.out.println("Error cargarInformacionDependiendoAreaYUsuario: "+e);// TODO: handle exception
		}
		
	}
	/**
	 * Metodo que permite cargar la actividad para iniciar la fase preparatoria
	 */
	public void cargarEtapaInicial(Integer ide_prpro,Integer ide_preta){
		String ser_etapa_inicial=ser_EtapaProcedimiento.getEtapaInicioSeguimiento("SI",ide_preta);
		TablaGenerica tab_etapa_inicial= utilitario.consultar(ser_etapa_inicial);
		tab_precontractual.getColumna("ide_actual_preta").setValorDefecto(tab_etapa_inicial.getValor("ide_preta"));
		tab_precontractual.getColumna("actividad_actual_prpre").setValorDefecto(tab_etapa_inicial.getValor("descripcion_preta"));
		utilitario.addUpdateTabla(tab_precontractual,"ide_actual_preta","");
		utilitario.addUpdateTabla(tab_precontractual,"actividad_actual_prpre","");
		mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_etapa_inicial.getValor("ide_preta")));
		String ide_etapa_enviar=cargarEtapaSiguienteAEnviar(ide_prpro,pckUtilidades.CConversion.CInt(tab_etapa_inicial.getValor("ide_preta")));
		tab_precontractual.getColumna("ide_preta").setValorDefecto(ide_etapa_enviar);
		utilitario.addUpdateTabla(tab_precontractual,"ide_preta","");
	}
	/**
	 * Metodo que permite la union de dos procesos paralelos
	 * @param ide_prpre_seleccionado
	 * @param ide_prpre_actual
	 */
	public void unionActividadParalela(Integer ide_prpre_seleccionado,Integer ide_prpre_actual){
		String update_precontractual=ser_precontractual.getProcesoParaleloUnionUpdate(ide_prpre_actual);
		utilitario.getConexion().ejecutarSql(update_precontractual);
		TablaGenerica tab_seg=utilitario.consultar("select 1 as codigo,count(ide_prseg) as cont from precon_seguimiento where ide_prpre="+ide_prpre_seleccionado+";");
		int cont_seg=pckUtilidades.CConversion.CInt(tab_seg.getValor("cont"));
		for(int i=0;i <cont_seg;i++){
			String update_seguimiento=ser_Seguimiento.getSeguimientoUpdateParalelo(ide_prpre_seleccionado,ide_prpre_actual);
			utilitario.getConexion().ejecutarSql(update_seguimiento);
		}
		TablaGenerica tab_req=utilitario.consultar("select 1 as codigo,count(ide_prdoc) as conta from precon_documento_requisito where ide_prpre="+ide_prpre_seleccionado+" and con_ide_prdoc is null;");
		int cont_req=pckUtilidades.CConversion.CInt(tab_req.getValor("conta"));
		for(int i=0;i <cont_req;i++){
			String update_requisito=ser_Requisito.getRequisitoUpdateParalelo(ide_prpre_seleccionado, ide_prpre_actual);
			utilitario.getConexion().ejecutarSql(update_requisito);
		}
		String update_preparalelo=ser_precontractual.getProcesoParaleloUpdateEstadoFalse(ide_prpre_seleccionado);
		utilitario.getConexion().ejecutarSql(update_preparalelo);
		tab_precontractual.modificar(tab_precontractual.getFilaActual());
		tab_precontractual.setValor("se_encuentra_catalogoe_prpre","NO");
		tab_precontractual.guardar();
		tab_seguimiento.dibujar();
		utilitario.agregarMensajeInfo("Realizo la unión de Actividades Paralelas, continue con el proceso.", "");
		utilitario.addUpdate("tab_precontractual");
	}
	/**
	 * Metodo que permite cargar la siguiente actividad
	 * @param ide_procedimiento 
	 * @param ide_actividad_actual
	 */
	public String cargarEtapaSiguienteAEnviar(Integer ide_procedimiento,Integer ide_actividad_actual) {
		String ide_etapa_actual="";
		String ser_etapa_siguiente_enviar=ser_EtapaProcedimiento.getEtapaSiguiente(ide_procedimiento, ide_actividad_actual,"NA");
		TablaGenerica tab_ide_etapa_actual =utilitario.consultar(ser_etapa_siguiente_enviar);
		ide_etapa_actual=tab_ide_etapa_actual.getValor("ide_preta");
		return ide_etapa_actual;
	}
	/**
	 * Metodo que permite cargar la actividad actual en el proceso
	 * @return
	 */
	public String cargarEtapaActual(Integer ide_preta){
		String etapa_actual="";
		String ser_etapa_actual=ser_etapa.getEtapa(ide_preta);
		TablaGenerica tab_etapa_actual=utilitario.consultar(ser_etapa_actual);
		etapa_actual=tab_etapa_actual.getValor("descripcion_preta");
		return etapa_actual;
	}
	/**
	 * Metodo que verifica los requisitos necesarios de la actividad en proceso
	 * @param ide_prpre
	 * @param ide_preta
	 * @return
	 */
	public boolean verificarRequisitoActividadActual(Integer ide_prpre,Integer ide_preta){
		Integer contadorNo=0;
		String ser_requisito=ser_Requisito.getRequisitoPorEtapa(ide_preta);
		String ser_etapa_requisitos=ser_etapa_requisito.getContarEtapaRequisitoPorPrecontractual(ide_prpre,ide_preta);
		List listRequisito=utilitario.getConexion().consultar(ser_requisito);
		List listRequisitoPrecontractual=utilitario.getConexion().consultar(ser_etapa_requisitos);
		if(listRequisitoPrecontractual.size()==listRequisito.size()){
			TablaGenerica tab_etapa_presenta = utilitario.consultar(ser_etapa_requisitos);
			for(int i = 0; i < tab_etapa_presenta.getTotalFilas(); i++){
				tab_documento_requisito.setValor("presenta_prdoc", tab_etapa_presenta.getValor(i, "presenta_prdoc"));
				if(tab_documento_requisito.getValor("presenta_prdoc").equals("NO")){
					contadorNo=contadorNo+1;
				}
			}
			if(contadorNo>0){
				cumpleRequisitos=false;
			}else{
				cumpleRequisitos=true;
			}
		}else{
			cumpleRequisitos=false;
		}
		return cumpleRequisitos;
	}
	/**
	 * Metodo que permite enviar la actividad siguiente
	 */
	public void enviarEtapaSiguiente(){
		boolean finalizado=false;
		if(tab_precontractual.getValor("ide_prpre")!=null){
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")!=null && tab_precontractual.getValor("aprueba_informetm_prpre")==null){
					utilitario.agregarMensajeInfo("El proceso no puede enviar a la siguiente actividad ya tiene que unirse, es actividad paralela.", "");
					return;
				}
			}
			if(tab_precontractual.getValor("estado_proceso_prpre").equals("REGISTRADO") || tab_precontractual.getValor("estado_proceso_prpre").equals("EN PROCESO")){
				finalizado=true;
			}
			if(!finalizado){
				utilitario.agregarMensajeInfo("El proceso ya ha finalizado, no se puede modificar.", "");
			}else {
				if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==3){
					if(tab_precontractual.getValor("aprueba_informetm_prpre")!=null){
						if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
							//APRUEBA INFORME TECNICO MOTIVADO (SI) sigue la siguiente actividad 9.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,9);
						}else{
							//NO APRUEBA INFORME TECNICO MOTIVADO (NO) Termina el proceso.
							cambiarEstadoProceso("NO APROBADO","FINALIZA EL PROCESO YA QUE NO SE APROBO EL INFORME TÉCNICO MOTIVADO");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Aprueba o no el Informe Técnico Motivado.", "");
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==6){
					if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")!=null){
						if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
							//SE ENCUENTRA EN CATALOGO ELECTRÓNICO (SI) va a la actividad 7 
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,7);
						}else{
							//NO ESTA EN CATALOGO ELECTRÓNICO va a la actividad 9
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,9);
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado si el Bien o Servicio se encuentra en Catálogo Electrónico.", "");
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==7){
					//PRECONTRACTUAL CATALOGO ELECTRÓNICO(2) va a la actividad 27 
					guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,27);
					//ACTIVIDAD 8 o 9
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
					if(tab_precontractual.getValor("termino_especificacion_prpre")!=null){
						if(!tab_precontractual.getValor("monto_prpre").isEmpty()){
							if(pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre"))>0){
								//DEFINE SI ES UN BIEN O UN SERVICIO (SI) sigue la siguiente actividad
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que el monto debe ser mayor a cero", "");
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha registrado el monto del proceso.", "");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado la Definición si es un Bien o un Servicio.", "");
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==16){
					if(tab_precontractual.getValor("consta_poa_prpre")!=null && tab_precontractual.getValor("consta_pac_prpre")!=null){
						if(tab_precontractual.getValor("consta_poa_prpre").equals("SI") && tab_precontractual.getValor("consta_pac_prpre").equals("SI")){
							//SI CONSTA EN EL POA Y PAC sigue la siguiente actividad 17
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,17);
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado la constatación del POA o PAC.", "");
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==18){
						if(tab_precontractual.getValor("aprueba_proyecto_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//APRUEBA PROYECTO (SI) sigue la siguiente actividad 20 y 22.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
							}else{
								//NO APRUEBA PROYECTO REGRESA A LA ACTIVIDAD 19.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,19);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Aprueba el proyecto o no lo aprueba.", "");
						}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==25){
					if(tab_precontractual.getValor("es_favorable_informej_prpre")!=null){
						if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
							//APRUEBA JURIDICO PROYECTO  (SI) sigue la siguiente actividad.Fase Precontractual
							////validar montos y depende de eso enviar a la fase precontractual correspodiente
							ventanaSeleccionContratacionPrecontractual();
						}else{
							//NO APRUEBA JURIDICO PROYECTO REGRESA A LA ACTIVIDAD 26.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1,26);
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es favorable informe juridico.", "");
					}
					//ACTIVIDAD 27
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==27){
					Integer error=0;
					// CONTRATACION CATALOGO ELECTRONICO (2)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
						if(tab_precontractual.getValor("esta_ok_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ESTA OK  (SI) sigue la siguiente actividad .Fase Precontractual 29
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,29);
							}else{
								//NO ESTA OK (NO) SIGUE A LA ACTIVIDAD 74.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,74);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si esta OK.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 31 
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==31){
					//CONTRATACIÓN DIRECTA  - CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}else
					//MENOR CUANTÍA  - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}else{
						if(tab_precontractual.getValor("es_superior_prpre")!=null){
							//CONTRATACION CATALOGO ELECTRONICO (2)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,33);
									}
							}
							//SUBASTA INVERSA (3)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,33);
									}
							}
							//MENOR CUANTIA - OBRAS (8)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,33);
									}
							}
							//LISTA CORTA - CONSULTORÍA(13)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,33);
									}
							}
							//CONCURSO PÚBLICO - CONSULTORÍA(14)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,33);
									}
							}
							//REGIMEN ESPECIAL (16)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,33);
									}
							}
							//LICITACIÓN - BIENES (7)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,33);
									}
							}
							//LICITACIÓN - OBRAS (10)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,33);
									}
							}
							//COTIZACIÓN - BIENES (6)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,33);
									}
							}
							//COTIZACIÓN - OBRAS (9)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI ES SUPERIOR AL MONTO DEFINIDO  (SI) sigue la siguiente actividad 32
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,32);
									}else{
										//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 33.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,33);
									}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es superior al monto definido.", "");
						}
					}
					//ACTIVIDAD 38 
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==38){
					Integer error=0;
					//CONTRATACION producto nacional (15)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
						if(tab_precontractual.getValor("manifestacion_interes_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE INTERES  (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(15,42);
							}else{
								//NO EXISTE INTERES (NO) SIGUE A LA ACTIVIDAD 98.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(15,98);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe manifestación de interés.", "");
							error=1;
						}
					}
					//CONTRATACION SUBASTA INVERSA(3)
					else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("comision_tecnica_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI REQUIERE COMISION  (SI) sigue la siguiente actividad 39
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,39);
							}else{
								//NO REQUIERE COMISION (NO) SIGUE A LA ACTIVIDAD 98.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,40);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Requiere Comisión Técnica.", "");
							error=1;
						}
					}
					else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 39
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==39){
					Integer error=0;
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("es_viable_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ES VIABLE (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,42);
							}else{
								//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 40
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==40){
					Integer error=0;
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("es_viable_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ES VIABLE (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,42);
							}else{
								//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 41
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==41){
					Integer error=0;
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,52);
							}
						}
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							//NO EXISTE O SI EXISTE ERROR DE FORMA
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,59);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,60);
										}
									}else{
										//NO RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,51);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,52);
										}
									}
								}else{
									utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda adjudicar.", "");
									error=1;
								}
							}
						}
					}
					//MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,52);
							}
						}
					}
					//LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,52);
							}
						}else{
							if(tab_precontractual.getValor("error_forma_prpre")!=null){
						
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									//NO EXISTE O SI EXISTE ERROR DE FORMA
										if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
											if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
												//SI RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,59);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,60);
												}
											}else{
												//NO RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,51);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,52);
												}
											}
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
										error=1;
									}
								}
						}
					}
					//LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,52);
							}
						}else{
							if(tab_precontractual.getValor("error_forma_prpre")!=null){
						
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									//NO EXISTE O SI EXISTE ERROR DE FORMA
										if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
											if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
												//SI RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,59);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,60);
												}
											}else{
												//NO RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,51);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,52);
												}
											}
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
										error=1;
									}
								}
						}
					}
					//COTIZACION - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,52);
							}
						}else{
							if(tab_precontractual.getValor("error_forma_prpre")!=null){
						
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									//NO EXISTE O SI EXISTE ERROR DE FORMA
										if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
											if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
												//SI RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,59);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,60);
												}
											}else{
												//NO RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,51);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,52);
												}
											}
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
										error=1;
									}
								}
						}
					}
					//COTIZACION - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,52);
							}
						}else{
							if(tab_precontractual.getValor("error_forma_prpre")!=null){
						
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									//NO EXISTE O SI EXISTE ERROR DE FORMA
										if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
											if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
												//SI RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,59);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,60);
												}
											}else{
												//NO RECOMIENDA ADJUDICAR  (SI)
												if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
													//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,51);
												}else{
													//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
													guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,52);
												}
											}
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
										error=1;
									}
								}
						}
					}
					//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,51);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre")!=null){
								//NO EXISTE O SI EXISTE ERROR DE FORMA
								if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
										if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
											//SI RECOMIENDA ADJUDICAR  (SI) sigue la siguiente actividad 59
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,59);
										}else{
											//NO RECOMIENDA ADJUDICAR  (NO) sigue la siguiente actividad 51
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,51);
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda adjudicar.", "");
										error=1;
									}
								}
							}
						}
					}
					//LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,52);
							}
						}
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							//NO EXISTE O SI EXISTE ERROR DE FORMA
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,59);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,60);
										}
									}else{
										//NO RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,51);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,52);
										}
									}
								}else{
									utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda adjudicar.", "");
									error=1;
								}
							}
						}
					}
					//CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,52);
							}
						}
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							//NO EXISTE O SI EXISTE ERROR DE FORMA
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,59);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,60);
										}
									}else{
										//NO RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,51);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,52);
										}
									}
								}else{
									utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda adjudicar.", "");
									error=1;
								}
							}
						}
					}
					//REGIMEN ESPECIAL
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,51);
							}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,52);
							}
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 42
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==42){
					Integer error=0;
					//MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("se_recibe_oferta_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SE RECIBE LA OFERTA (SI)//SIGUE A LA ACTIVIDAD 60. 
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,43);
							}else{
								//NO RECIBE LA OFERTA (NO) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,51);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,52);
								}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si se recibe la oferta.", "");
							error=1;
						}
					}
					//MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						if(tab_precontractual.getValor("se_recibe_oferta_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SE RECIBE LA OFERTA (SI)//SIGUE A LA ACTIVIDAD 60. 
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,43);
							}else{
								//NO RECIBE LA OFERTA (NO) sigue la siguiente actividad 51
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,51);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si se recibe la oferta.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 43
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==43){
					Integer error=0;
					// CONTRATACION producto nacional (15)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
						if(tab_precontractual.getValor("cumple_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI CUMPLE  (SI) sigue la siguiente actividad 45
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(15,45);
							}else{
								//NO CUMPLE (NO) SIGUE A LA ACTIVIDAD 98.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(15,98);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Cumple.", "");
							error=1;
						}
					}
					// CONTRATACION SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION MENOR CUANTÍA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION DIRECTA - CONSULTORÍA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION LISTA CORTA - CONSULTORÍA(13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION CONCURSO PÚBLICO - CONSULTORÍA(14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,107);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION MENOR CUANTÍA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}
					// CONTRATACION COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI EXISTE ERRORES DE FORMA  (SI) sigue la siguiente actividad 44
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,44);
							}else{
								//NO EXISTE ERRORES DE FORMA (NO) SIGUE A LA ACTIVIDAD 49.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,49);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe errores de forma.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 48
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==48){
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}else
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					else if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
						//MENOR CUANTÍA - OBRAS (8)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) 
									if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
										//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,51);
									}else{
										//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,52);
									}
								}
						}
						//LICITACIÓN - BIENES (7)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) 
									if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
										//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,51);
									}else{
										//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,52);
									}
								}
						}
						//LICITACIÓN - OBRAS (10)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) 
									if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
										//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,51);
									}else{
										//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,52);
									}
								}
						}
						//CONTRATACIÓN - BIENES (6)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) 
									if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
										//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,51);
									}else{
										//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,52);
									}
								}
						}
						//CONTRATACIÓN - OBRAS (9)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) 
									if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
										//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,51);
									}else{
										//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,52);
									}
								}
						}
						//MENOR CUANTÍA - BIENES (5)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//CONVALIDACION SATISFACTORIA (SI) 
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,49);
								}else{
									//NO ES CONVALIDACION SATISFACTORIA (NO) sigue la siguiente actividad 51
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,51);
								}
							
						}
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria.", "");
					}
					//ACTIVIDAD 50
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==50){
					//MENOR CUANTÍA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,59);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,60);
							}
					}
					guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					//ACTIVIDAD 51
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==51){
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
								}
							}
						}
					}
					//LICITACIÓN- BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
								}
							}
						}
					}
					//LICITACIÓN- OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
								}
							}
						}
					}
					//COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
								}
							}
						}
					}
					//COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
								}
							}
						}
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,102);
						}else{
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null || tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null || tab_precontractual.getValor("se_recibe_oferta_prpre")!=null){
								if(tab_precontractual.getValor("se_recibe_oferta_prpre").equals("NO")){
									//NO SE RECIBE LA OFERTA
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
								}else
								//NO RECOMIENDA ADJUDICAR
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null && tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
									//sigue la siguiente actividad 110
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
								}else if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 110
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
								}
							}
						}
					}
					// CONTRATACIÓN DIRECTA - CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,102);
						}else{
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null || tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null || tab_precontractual.getValor("se_recibe_oferta_prpre")!=null){
								//NO RECOMIENDA ADJUDICAR
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null && tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
									//sigue la siguiente actividad 110
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,110);
								}else if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 110
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,110);
								}
							}
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
								}
							}
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
								}
							}
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,102);
						}else{
							// REGIMEN ESPECIAL (16)
							if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
								//NO ES VIABLE
								if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
									//sigue la siguiente actividad 102
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,102);
								}
								if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									if(tab_precontractual.getValor("convalida_recadjudicar_prpre")!=null){
										//NO RECOMIENDA ADJUDICAR
										if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("NO")){
											//sigue la siguiente actividad 114
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,110);
										}
									}
								}else{
									if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("NO")){
										//sigue la siguiente actividad 110
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,110);
									}
								}
							}
						}
					}
					// MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,102);
						}else{
						/////SI ES VIABLE 
							//NO RECIBE OFERTA
							if(tab_precontractual.getValor("se_recibe_oferta_prpre").equals("NO")){
								//sigue la siguiente actividad 110
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,110);
							}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								//SI RECIBE OFERTA Y NO EXISTE ERROR
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,110);
								}
							}else if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null && tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
								//SI RECIBE OFERTA Y SI EXISTE ERROR	
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,110);
							}else if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,110);
							}
						}
					}
					//ACTIVIDAD 52
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==52){
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,110);
								}
							}
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,110);
								}
							}
						}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,110);
								}
							}
						}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,110);
								}
							}
						}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,110);
								}
							}
						}
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,102);
						}else{
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null || tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null || tab_precontractual.getValor("se_recibe_oferta_prpre")!=null){
								if(tab_precontractual.getValor("se_recibe_oferta_prpre").equals("NO")){
									//NO SE RECIBE LA OFERTA
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
								}else
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null && tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 110
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
									}else if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
										//sigue la siguiente actividad 110
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,110);
									}
							}
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,110);
								}
							}
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
									}else{
										if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null ){
											if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
												guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
											}
										}
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 114
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,110);
								}
							}
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//NO ES VIABLE
						if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
							//sigue la siguiente actividad 102
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,102);
						}else{
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								if(tab_precontractual.getValor("convalida_recadjudicar_prpre")!=null){
									//NO RECOMIENDA ADJUDICAR
									if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("NO")){
										//sigue la siguiente actividad 114
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,110);
									}
								}
							}else{
								if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("NO")){
									//sigue la siguiente actividad 110
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,110);
								}
							}
						}
					}
					//ACTIVIDAD 49
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==49){
					Integer error=0;
					//MENOR CUANTIA - OBRAS (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
								//NO EXISTE O SI EXISTE ERROR DE FORMA
								if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI RECOMIENDA ADJUDICAR  (SI) sigue la siguiente actividad 50
										guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,50);
									}else{
										//NO RECOMIENDA ADJUDICAR  (NO) sigue la siguiente actividad 51
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,51);
									}
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
								error=1;
							}
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 55
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==55){
					Integer error=0;
					//MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("error_forma_prpre")!=null){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")!=null){
							//NO EXISTE O SI EXISTE ERROR DE FORMA
								if(tab_precontractual.getValor("error_forma_prpre").equals("NO") || tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
										//SI RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,59);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,60);
										}
									}else{
										//NO RECOMIENDA ADJUDICAR  (SI)
										if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
											//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,51);
										}else{
											//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,52);
										}
									}
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Recomienda Adjudicar.", "");
								error=1;
							}
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 59
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==59){
					Integer error=0;
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									if(tab_precontractual.getValor("existe_mas_oferta_prpre")!=null){
										if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
											//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,108);
										}else{
											//NO EXISTE MAS DE UNA OFERTA (NO) SIGUE A LA ACTIVIDAD 109.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,109);
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe mas de una oferta", "");
										error=1;
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,61);
							}
						}
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,61);
								}
							}
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,61);
								}
							}
						}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,61);
								}
							}
						}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,108);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,108);
								error=1;
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,61);
									error=1;
								}
							}
						}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,108);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,108);
								error=1;
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,61);
									error=1;
								}
							}
						}
					}
					// CONTRATACIÓN DIRECTA- CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,108);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,108);
								error=1;
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,61);
									error=1;
								}
							}
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,109);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,61);
							}
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,109);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,61);
							}
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,108);
								}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,108);
							}
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 60
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==60){
					Integer error=0;
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									if(tab_precontractual.getValor("existe_mas_oferta_prpre")!=null){
										if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
											//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,108);
										}else{
											//NO EXISTE MAS DE UNA OFERTA (NO) SIGUE A LA ACTIVIDAD 109.
											guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,109);
										}
									}else{
										utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Existe mas de una oferta", "");
										error=1;
									}
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,61);
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,61);
							}
						}
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,61);
									error=1;
								}
							}
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,61);
									error=1;
								}
							}
						}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,108);
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,108);
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,61);
									error=1;
								}
							}
						}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,108);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,108);
								error=1;
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,61);
									error=1;
								}
							}
						}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,108);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//sigue la siguiente actividad 108
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,108);
								error=1;
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,61);
									error=1;
								}
							}
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,109);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,61);
								error=1;
							}
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
									//SI EXISTE MAS DE UNA OFERTA (SI) sigue la siguiente actividad 108
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,109);
									error=1;
								}
							}else{
								if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
									//sigue la siguiente actividad 61
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,61);
									error=1;
								}
							}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,61);
								error=1;
							}
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								//SI ES CONVALIDACION SATISFACTORIA
								if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("SI")){
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,108);
								}
						}else if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("SI")){
								//sigue la siguiente actividad 61
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,108);
							}
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 62
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==62){
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,64);
						}
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,64);
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,64);
						}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,64);
						}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,64);
						}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,64);
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,64);
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,64);
						}
					}
					// REGIMEN ESPECIAL (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 63
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,63);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 64.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,64);
						}
					}
					// MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					// CONTRATACIÓN DIRECTA - CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 66
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==66){
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL DE SUBASTA INVERSA");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - OBRAS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL REGIMEN ESPECIAL");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - BIENES Y SERVICIOS NO NORMALIZADOS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - BIENES Y SERVICIOS NO NORMALIZADOS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - OBRAS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL CONTRATACIÓN DIRECTA - CONSULTORÍA");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL LISTA CORTA - CONSULTORÍA");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL CONCURSO PÚBLICO - CONSULTORÍA");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN  - BIENES Y SERVICIOS NO NORMALIZADOS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// COTIZACIÓN - BIENES (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN  - OBRAS");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
					// PRODUCCION NACIONAL (15)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
						cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL PRODUCCIÓN NACIONAL");
						cargarDatosEnContrato(tab_precontractual.getValor("termino_especificacion_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CDbl(tab_precontractual.getValor("monto_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_tepro")));
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==68){
					Integer error=0;
					//ACTIVIDAD 68 CONTRATACION CATALOGO ELECTRONICO (2)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
						if(tab_precontractual.getValor("es_superior_prpre")!=null){
							if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES SUPERIOR AL MONTO DEFINIDO OK  (SI) sigue la siguiente actividad 69
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,69);
							}else{
								//NO ES SUPERIOR AL MONTO DEFINIDO (NO) SIGUE A LA ACTIVIDAD 70.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(2,70);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es superior al monto definicdo.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==76){
					Integer error=0;
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==4){
						if(tab_precontractual.getValor("es_proceso_infima_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//APRUEBA es proceso infima  (SI) sigue la siguiente actividad 78
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,78);
							}else{
								//NO APRUEBA VA A LA ACTIVIDAD 77.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,77);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es proceso de ínfima.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,77);
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==77){
					cambiarEstadoProceso("FINALIZADO","FINALIZA EL PROCESO PRECONTRACTUAL YA QUE NO ES UN PROCESO DE INFIMA");
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==79){
					if(tab_precontractual.getValor("solicitar_alcance_prpre")!=null){
						if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
							//solicita alcance proceso infima  (SI) sigue la siguiente actividad 80
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,80);
						}else{
							//NO VA A LA ACTIVIDAD 82.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,82);
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si se necesita solicitar alcance.", "");
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==86){
					if(tab_precontractual.getValor("termino_especificacion_prpre").equals("TR")){
						//si es servicio sigue la siguiente actividad 88
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,88);
					}else{
						//NO es servicio es un bien VA A LA ACTIVIDAD 87.
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(4,87);
					}
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==94){
					cambiarEstadoProceso("FINALIZADO AL PUBLICAR EN PORTAL SERCOP","FINALIZA EL PROCESO PRECONTRACTUAL DE ÍNFIMA CUANTÍA");
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==96){
					cambiarEstadoProceso("FINALIZADO ENVIADO A GESTIÓN PAGOS ","FINALIZA EL PROCESO PRECONTRACTUAL CATÁLOGO ELÉCTRONICO");
				/*}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==97){
					cambiarEstadoProceso("FINALIZADO ENVIADO A CONTRACTUAL","FINALIZA EL PROCESO PRECONTRACTUAL PRODUCCIÓN NACIONAL");*/
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==100){
					cambiarEstadoProceso("FINALIZADO POR APROBACIÓN SERCOP","FINALIZA EL PROCESO PRECONTRACTUAL PRODUCCIÓN NACIONAL");
					//ACTIVIDAD 101
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==101){
					Integer error=0;
					// MENOR CUANTIA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("es_viable_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ES VIABLE (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,42);
							}else{
								//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
							error=1;
						}
					}
					//REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("es_viable_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ES VIABLE (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,42);
							}else{
								//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
							error=1;
						}
					}
					// MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						if(tab_precontractual.getValor("es_viable_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//SI ES VIABLE (SI) sigue la siguiente actividad 42
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,42);
							}else{
								//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,41);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
							error=1;
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// LISTA CORTA- CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
							if(tab_precontractual.getValor("es_viable_prpre")!=null){
								if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
									//SI ES VIABLE (SI) sigue la siguiente actividad 42
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,42);
								}else{
									//NO ES VIABLE (NO) SIGUE A LA ACTIVIDAD 41.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,41);
								}
							}else{
								utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si Es Viable.", "");
								error=1;
							}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 103
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==103){
					// SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,105);
						}
					}
					// MENOR CUNATÍA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,105);
						}
					}
					// REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,105);
						}
					}
					// LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,105);
						}
					}
					// LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,105);
						}
					}
					// COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,105);
						}
					}
					// COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,105);
						}
					}
					// LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,105);
						}
					}
					// CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
								//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 104
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,104);
						}else{
								//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 105.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,105);
						}
					}
					// MENOR CUANTÍA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,104);
					}
					// CONTRATACIÓN DIRECTA - CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,104);
					}
					//ACTIVIDAD 106
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==106){
					//SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL SUBASTA INVERSA");
					}
					//MENOR CUANTÍA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - OBRAS");
					}
					//REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL REGIMEN ESPECIAL");
					}
					//MENOR CUANTÍA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - BIENES Y SERVICIOS NO NORMALIZADOS");
					}
					//LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - BIENES Y SERVICIOS NO NORMALIZADOS");
					}
					//LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - OBRAS");
					}
					//CONTRATACIÓN DIRECTA - CONSULTORIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL CONTRATACIÓN DIRECTA - CONSULTORÍA");
					}
					//LISTA CORTA - CONSULTORIA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL LISTA CORTA - CONSULTORÍA");
					}
					//CONCURSO PÚBLICO - CONSULTORIA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL CONCURSO PÚBLICO - CONSULTORÍA");
					}
					//COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN -  BIENES Y SERVICIOS NO NORMALIZADOS");
					}
					//COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						cambiarEstadoProceso("FINALIZADO POR RESOLUCIÓN CANCELACIÓN","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN - OBRAS");
					}
					//ACTIVIDAD 107
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==107){
					Integer error=0;
					//SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//CONVALIDACION SATISFACTORIA (SI) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,59);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,60);
								}
							}else{
								//NO ES CONVALIDACION SATISFACTORIA (NO) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,51);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,52);
								}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria", "");
							error=1;
						}
					}
					//LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,59);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,60);
						}
					}
					//LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,59);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,60);
						}
					}
					//COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,59);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,60);
						}
					}
					//COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,59);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,60);
						}
					}
					//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//CONVALIDACION SATISFACTORIA (SI) 
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,59);
							}else{
								//CONVALIDACION SATISFACTORIA (NO)  SIGUE A LA ACTIVIDAD 52.
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,51);
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria", "");
							error=1;
						}
					}
					//LISTA CORTA - CONSULTORÍA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//CONVALIDACION SATISFACTORIA (SI) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,59);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,60);
								}
							}else{
								//NO ES CONVALIDACION SATISFACTORIA (NO) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,51);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,52);
								}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria", "");
							error=1;
						}
					}
					//CONCURSO PÚBLICO - CONSULTORÍA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//CONVALIDACION SATISFACTORIA (SI) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,59);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,60);
								}
							}else{
								//NO ES CONVALIDACION SATISFACTORIA (NO) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,51);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,52);
								}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria", "");
							error=1;
						}
					}
					//REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("convalida_recadjudicar_prpre")!=null){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//CONVALIDACION SATISFACTORIA (SI) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 59
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,59);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 60.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,60);
								}
							}else{
								//NO ES CONVALIDACION SATISFACTORIA (NO) 
								if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
									//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 51
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,51);
								}else{
									//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 52.
									guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,52);
								}
							}
						}else{
							utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es Convalidación Satisfactoria y recomienda adjudicar.", "");
							error=1;
						}
					}else if(error==0){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 111
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==111){
					//SUBASTA INVERSA (3)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,113);
						}
					}
					//MENOR CUANTÍA - OBRAS (8)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,113);
						}
					}
					//LICITACIÓN - BIENES (7)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,113);
						}
					}
					//LICITACIÓN - OBRAS (10)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,113);
						}
					}
					//COTIZACIÓN - BIENES (6)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,113);
						}
					}
					//COTIZACIÓN - OBRAS (9)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(9,113);
						}
					}
					//REGIMEN ESPECIAL (16)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,113);
						}
					}
					//LISTA CORTA - CONSULTORIA (13)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,113);
						}
					}
					//CONCURSO PÚBLICO - CONSULTORIA (14)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						if(tab_precontractual.getValor("es_superior_prpre").equals("SI")){
							//SI ES MONTO SUPERIOR (SI) sigue la siguiente actividad 112
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,112);
						}else{
							//NO ES MONTO SUPERIOR (NO) SIGUE A LA ACTIVIDAD 113.
							guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,113);
						}
					}
					//CONTRATACIÓN DIRECTA - CONSULTOTIA (12)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//MENOR CUANTIA - BIENES (5)
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
					}
					//ACTIVIDAD 114
				}else if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==114){
					if(tab_precontractual.getValor("necesario_reaperturar_prpre")!=null){
						//SUBASTA INVERSA (3)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(3,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL SUBASTA INVERSA");
							}
						}
						//MENOR CUANTÍA - OBRAS(8)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(8,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - OBRAS");
							}
						}
						//LICITACIÓN - BIENES(7)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(7,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - BIENES Y SERVICIOS NO NORMALIZADOS");
							}
						}
						//LICITACIÓN - OBRAS(10)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(10,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL LICITACIÓN - OBRAS");
							}
						}
						//COTIZACIÓN - BIENES(6)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN - BIENES Y SERVICIOS NO NORMALIZADOS");
							}
						}
						//COTIZACIÓN - OBRAS(9)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(6,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL COTIZACIÓN - OBRAS");
							}
						}
						//REGIMEN ESPECIAL (16)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(16,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL SUBASTA INVERSA");
							}
						}
						//MENOR CUANTÍA - BIENES(5)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(5,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL MENOR CUANTÍA - BIENES Y SERVICIOS NO NORMALIZADOS");
							}
						}
						//CONTRATACIÓN DIRECTA - CONSULTORIA(12)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(12,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL CONTRATACIÓN DIRRECTA - CONSULTORÍA");
							}
						}
						//LISTA CORTA - CONSULTORIA(13)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(13,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL LISTA CORTA - CONSULTORÍA");
							}
						}
						//CONCURSO PÚBLICO - CONSULTORIA(14)
						if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
							if(validarAprobacionEnActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
								//ES NECESARIO REAPERTURAR (SI) sigue la siguiente actividad 34
								guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(14,34);
							}else{
								//NO ES NECESARIO REAPERTURAR (NO) FINALIZA PROCESO.
								cambiarEstadoProceso("FINALIZADO DESIERTO","FINALIZA EL PROCESO PRECONTRACTUAL CONCURSO PÚBLICO - CONSULTORÍA");
							}
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no ha selecionado Si es necesario Reaperturar.", "");
					}
				}else{
					guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual();
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que todavía no hay un registro guardado.", "");
		}
	}
	/**
	 * Mostramos pantalla emergente para que seleccione el tipo de Contratación para la Fase Precontractual
	 */
	public void ventanaSeleccionContratacionPrecontractual(){
		set_monto_contratacion.getTab_seleccion().setSql(ser_procedimiento.getProcedimientoContratacion());
		set_monto_contratacion.getTab_seleccion().ejecutarSql();
		set_monto_contratacion.dibujar();
	}
	public void ventanaSeleccionPrecontractualParalela(){
		set_tipo_actividad_union.setSeleccionTabla(ser_precontractual.getProcesoParaleloUnion(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_usuario_iniciop_prpre"))),"ide_prpre");
		set_tipo_actividad_union.getTab_seleccion().getColumna("descripcion_prpre").setNombreVisual("Poceso Paralelo");
		set_tipo_actividad_union.getTab_seleccion().ejecutarSql();
		set_tipo_actividad_union.dibujar();
	}
	/**
	 * Carga la actividad actual seleccionada en el popup
	 */
	public void aceptarContratacion(){
		if(set_monto_contratacion.getValorSeleccionado()!=null){
			Integer actividadPrecontractual = null;
			switch (pckUtilidades.CConversion.CInt(set_monto_contratacion.getValorSeleccionado())) {
			////BIENES Y SERVICIOS NORMALIZADOS	
				//CATÁLOGO ELECTRÓNICO
				case 2: actividadPrecontractual=27;
		        		break;
		        //SUBASTA INVERSA
				case 3: actividadPrecontractual=27;
		        		break;
		        //ÍNFIMA CUANTÍA
				case 4: actividadPrecontractual=75;
	    				break;
	    	////BIENES Y SERVICIOS NO NORMALIZADOS
	    		//MENOR CUANTÍA
			    case 5:	actividadPrecontractual=27;
			    		break;
			    //COTIZACIÓN
			    case 6: actividadPrecontractual=27;
	            		break;
	            //LICITACIÓN
			    case 7:	actividadPrecontractual=27;
			    		break;
			////OBRAS
			    //MENOR CUANTÍA
			    case 8:	actividadPrecontractual=27;
	    				break;
	    		//COTIZACIÓN
			    case 9:	actividadPrecontractual=27;
			    		break;
			    //LICITACIÓN
				case 10:actividadPrecontractual=27;
						break;
				//CONTRATACIÓN INTEGRAL POR PRECIO FIJO
				case 11:actividadPrecontractual=0;
	    				break;
	    	////CONSULTORÍA
	    		//CONTRATACIÓN DIRECTA
				case 12:actividadPrecontractual=27;
	    				break;
	    		//LISTA CORTA
				case 13:actividadPrecontractual=27;
	    				break;
	    		//CONCURSO PÚBLICO
				case 14:actividadPrecontractual=27;
	    				break;
	    	////VEHÍCULOS Y OTROS ESPECIFICOS 
	    		//PRODUCCIÓN NACIONAL
				case 15:actividadPrecontractual=34;
						break;
			////REGIMEN ESPECIAL 
			    		//REGIMEN ESPECIAL
				case 16:actividadPrecontractual=27;
						break;
	        }
			guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(pckUtilidades.CConversion.CInt(set_monto_contratacion.getValorSeleccionado()),actividadPrecontractual);
			//mostrarNotificacionActividad(actividadPrecontractual);
			tab_precontractual.insertar();
			set_monto_contratacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar el Tipo de Contratación.", "");
		}

	}
	/**
	 * Cancela el poppup de seleccion de actividad actual
	 */
	public void cancelarContratacion(){
		tab_precontractual.ejecutarSql();
		set_monto_contratacion.cerrar();
	}
	/**
	 * Metodo que guarda la información de seguimiento de ese proceso validando si en esa actividad
	 * tiene que subir documentos y cumple con los mismos en actividad actual
	 */
	public void guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(){
		if(verificarRequisitoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
			//primero guardamos los datos actuales antes de enviar a la siguiente actividad
			guardarSeguimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")));
			//Cargamos los datos nuevos de la etapa siguiente
			tab_precontractual.setValor("ide_actual_preta", tab_precontractual.getValor("ide_preta"));
			String descripcion_etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")));
			tab_precontractual.setValor("actividad_actual_prpre", descripcion_etapa_actual);
			tab_precontractual.setValor("estado_proceso_prpre","EN PROCESO");
			tab_precontractual.setValor("fecha_prpre", utilitario.getFechaActual());
			tab_precontractual.setValor("hora_prpre", utilitario.getHoraActual());
			tab_precontractual.setValor("observacion_prpre", " ");
			String ide_geedp_enviar=cargarIdUsuario(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")));
			if(ide_geedp_enviar==null){
				//insertamos el usuario que inicio el proceso
				String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
				String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",ide_gtemp);
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}else{
				String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",tab_ide_departamento.getValor("IDE_GTEMP"));
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}
			tab_precontractual.modificar(tab_precontractual.getFilaActual());
			String ser_etapa_siguiente_enviar=cargarEtapaSiguienteAEnviar(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			tab_precontractual.setValor("ide_preta",ser_etapa_siguiente_enviar);
			tab_precontractual.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_precontractual");
			//ENVIO CORREO
			if(enviar_correo){
				enviarMailActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_geedp")),tab_precontractual.getValor("codigo_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")), tab_precontractual.getValor("actividad_actual_prpre"),tab_precontractual.getValor("fecha_prpre"),tab_precontractual.getValor("hora_prpre"));
			}
			cargarInformacionDependiendoAreaYUsuario();
			if(tab_precontractual.getValor("ide_prpre")!=null){
				mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no cumple con los requisitos solicitados en la actividad actual, o no subió el archivo anexo.", "");
		}
	}
	/**
	 * Metodo que guarda la información de seguimiento de ese proceso validando si en esa actividad
	 * tiene que subir documentos y cumple con los mismos en actividad actual y si es cambio de fase lo cambia  precontractual
	 */
	public void guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(Integer ide_pretp,Integer ide_preta){
		if(verificarRequisitoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")))){
			//primero guardamos los datos actuales antes de enviar a la siguiente actividad
			guardarSeguimiento(ide_preta);
			//Cargamos los datos nuevos de la etapa siguiente
			String ide_preta_actual=pckUtilidades.CConversion.CStr(ide_preta);
			tab_precontractual.setValor("ide_actual_preta", ide_preta_actual);
			String descripcion_etapa_actual=cargarEtapaActual(ide_preta);
			tab_precontractual.setValor("actividad_actual_prpre", descripcion_etapa_actual);
			tab_precontractual.setValor("estado_proceso_prpre","EN PROCESO");
			tab_precontractual.setValor("fecha_prpre", utilitario.getFechaActual());
			tab_precontractual.setValor("hora_prpre", utilitario.getHoraActual());
			tab_precontractual.setValor("observacion_prpre", " ");
			String ide_geedp_enviar=cargarIdUsuario(ide_pretp,ide_preta);
			if(ide_geedp_enviar==null){
				//insertamos el usuario que inicio el proceso
				String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
				String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",ide_gtemp);
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}else{
				String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",tab_ide_departamento.getValor("IDE_GTEMP"));
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}
			tab_precontractual.modificar(tab_precontractual.getFilaActual());
			String ser_etapa_siguiente_enviar=cargarEtapaSiguienteAEnviar(ide_pretp,pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			tab_precontractual.setValor("ide_preta",ser_etapa_siguiente_enviar);
			String idEstadoProcedimiento=pckUtilidades.CConversion.CStr(ide_pretp);
			tab_precontractual.setValor("ide_pretp",idEstadoProcedimiento);
			tab_precontractual.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_precontractual");
			//ENVIO CORREO
			if(enviar_correo){
				enviarMailActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_geedp")),tab_precontractual.getValor("codigo_prpre"),tab_precontractual.getValor("descripcion_prpre"),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")), tab_precontractual.getValor("actividad_actual_prpre"),tab_precontractual.getValor("fecha_prpre"),tab_precontractual.getValor("hora_prpre"));
			}
			cargarInformacionDependiendoAreaYUsuario();
			if(tab_precontractual.getValor("ide_prpre")!=null){
				mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no cumple con los requisitos solicitados en la actividad actual, o no subió el archivo anexo.", "");
		}
	}
	/**
	 * Metodo que permite devolver el id usuario en esa etapa 
	 * @param ide_etapaEnviar
	 * @return
	 */
	public String cargarIdUsuario(Integer ide_prpro,Integer ide_etapaEnviar){
		String ide_geedp="";
		String sr_ide_geedp=ser_precontractual.cargarInformacionUsuarioEtapaAEnviar(ide_prpro,ide_etapaEnviar);
		TablaGenerica tab_ide_geedp=utilitario.consultar(sr_ide_geedp);
		ide_geedp=tab_ide_geedp.getValor("ide_geedp");
		return ide_geedp;
				
	}
	/**
	 * Metodo que muestra campos en opcion lectura de acuerdo a la actividad actual
	 * @param ide_actual_preta
	 */
	public void mostrarCamposDeAcuerdoActividadActual(Integer ide_actual_preta){
		tab_precontractual.getColumna("descripcion_prpre").setLectura(true);
		tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(true);
		tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(true);
		tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(true);
		tab_precontractual.getColumna("monto_prpre").setLectura(true);
		tab_precontractual.getColumna("consta_poa_prpre").setLectura(true);
		tab_precontractual.getColumna("consta_pac_prpre").setLectura(true);
		tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(true);
		tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(true);
		tab_precontractual.getColumna("esta_ok_prpre").setLectura(true);
		tab_precontractual.getColumna("es_superior_prpre").setLectura(true);
		tab_precontractual.getColumna("manifestacion_interes_prpre").setLectura(true);
		tab_precontractual.getColumna("cumple_prpre").setLectura(true);
		tab_precontractual.getColumna("comision_tecnica_prpre").setLectura(true);
		tab_precontractual.getColumna("es_viable_prpre").setLectura(true);
		tab_precontractual.getColumna("error_forma_prpre").setLectura(true);
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(true);
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setLectura(true);
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(true);
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setLectura(true);
		switch (ide_actual_preta) {
			case 1: tab_precontractual.getColumna("descripcion_prpre").setLectura(false);
	        		break;
			case 3: tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(false);
	        		break;
			case 4: tab_precontractual.getColumna("descripcion_prpre").setLectura(false);
    				break;
		    case 6:	tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(false);
		    		break;
		    case 8: tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(false);
		    		tab_precontractual.getColumna("monto_prpre").setLectura(false);
            		break;
		    case 9:	tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(false);
		    		tab_precontractual.getColumna("monto_prpre").setLectura(false);
		    		break;
		    case 16:tab_precontractual.getColumna("consta_poa_prpre").setLectura(false);
		    		tab_precontractual.getColumna("consta_pac_prpre").setLectura(false);
    				break;
		    case 18:tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(false);
		    		break;
			case 25:tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(false);
					break;
			case 27:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
						//CATALOGO ELECTRONICO
						tab_precontractual.getColumna("esta_ok_prpre").setLectura(false);
						break;
					}
			case 31:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
						//CATALOGO ELECTRONICO
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVERSA
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//MENOR CUANTIA - OBRAS
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//REGIMEN ESPECIAL
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//LICITACIÓN - BIENES
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//LICITACIÓN - OBRAS
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//COTIZACIÓN - BIENES
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//COTIZACIÓN - OBRAS
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//LISTA CORTA - CONSULTORÍA
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//CONCURSO PÚBLICO - CONSULTORÍA
						tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
						break;
					}
			case 38:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVERSA
						tab_precontractual.getColumna("comision_tecnica_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
						//PRODUCCIÓN NACIONAL
						tab_precontractual.getColumna("manifestacion_interes_prpre").setLectura(false);
						break;
					}
					break;
			case 39:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVRESA
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
			case 40:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVERSA
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
			case 41:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
					//SUBASTA INVERSA
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//LICITACIÓN - BIENES
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//LICITACIÓN - OBRAS
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//COTIZACIÓN - BIENES
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//COTIZACIÓN - OBRAS
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//CONTRATACIÓN DIRECTA - CONSULTORÍA
						if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
							if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
							if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
								tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
								break;
							}
						}
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//LISTA CORTA - CONSULTORÍA
							if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
								if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
									tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
									break;
								}
								if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
									break;
								}
							}
						}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//CONCURSO PÚBLICO - CONSULTORÍA
							if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
								if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
									tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
									break;
								}
								if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
									tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
									break;
								}
							}
						}
			case 42:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				//MENOR CUANTÍA - OBRAS
					tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(false);
					break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						//MENOR CUANTÍA - BIENES
							tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(false);
							break;
					}
			case 43:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
						//PRODUCCIÓN NACIONAL
						tab_precontractual.getColumna("cumple_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVERSA
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//REGIMEN ESPECIAL
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//MENOR CUANTÍA - OBRAS
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						//MENOR CUANTÍA - BIENES
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//LICITACIÓN - BIENES
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//LICITACIÓN - OBRAS
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//COTIZACIÓN - BIENES
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//COTIZACIÓN - OBRAS
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//CONTRATACIÓN DIRECTA - CONSULTORÍA
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//LISTA CORTA - CONSULTORÍA
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//CONCURSO PÚBLICO - CONSULTORÍA
						tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
						break;
					}
			case 48:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				//MENOR CUANTÍA - OBRAS
					tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
					break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
					//MENOR CUANTÍA - BIENES
						tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//LICITACIÓN - BIENES
							tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
							break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//LICITACIÓN - OBRAS
							tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
							break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//COTIZACIÓN - BIENES
							tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
							break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//COTIZACIÓN - OBRAS
							tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
							break;
					}
			case 49:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				//MENOR CUANTÍA - BIENES
					if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
						if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
							break;
						}
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
							break;
						}
					}
				}
			case 55:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				//MENOR CUANTÍA - OBRAS
					if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
						if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
							break;
						}
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
							break;
						}
					}
				}
			case 59:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SUBASTA INVERSA //SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							//SI ES CONVALIDACION SATISFACTORIA
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
										tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(false);
										break;
									}
								}
							}
						}
					}
			case 60:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SUBASTA INVERSA //SI ES ERROR FORMA
						if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							//SI ES CONVALIDACION SATISFACTORIA
							if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
								if(tab_precontractual.getValor("convalidacion_satisfac_prpre")!=null){
									if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
										tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(false);
										break;
									}
								}
							}
						}
					}
			case 76://tab_precontractual.getColumna("es_proceso_infima_prpre").setVisible(true);
					tab_precontractual.getColumna("es_proceso_infima_prpre").setLectura(false);
					utilitario.addUpdate("tab_precontractual");
					break;
			case 79:tab_precontractual.getColumna("solicitar_alcance_prpre").setLectura(false);
					//tab_precontractual.getColumna("solicitar_alcance_prpre").setVisible(true);
					utilitario.addUpdate("tab_precontractual");
					break;
			case 101:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
						//MENOR CUANTÍA - OBRAS
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//REGIMEN ESPECIAL
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
						//MENOR CUANTÍA - BIENES
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
						//LICITACIÓN - BIENES
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
						//LICITACIÓN - OBRAS
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
						//COTIZACIÓN - BIENES
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
						//COTIZACIÓN - OBRAS
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//CONTRATACIÓN DIRECTA - CONSULTORÍA
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//LISTA CORTA - CONSULTORÍA
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//CONCURSO PÚBLICO - CONSULTORÍA
						tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
						break;
					}
			case 107:if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
						//SUBASTA INVERSA
						tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
						//REGIMEN ESPECIAL
						tab_precontractual.getColumna("convalida_recadjudicar_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
						//CONTRATACIÓN DIRECTA - CONSULTORÍA
						tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
						//LISTA CORTA - CONSULTORÍA
						tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
						break;
					}
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
						//CONCURSO PÚBLICO - CONSULTORÍA
						tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
						break;
					}
			case 114:tab_precontractual.getColumna("necesario_reaperturar_prpre").setLectura(false);
					 break;
		}
	}
	/**
	 * Metodo que valida aprobaciones en la Actividad actual
	 * @param ide_actual_preta
	 * @return true si fue aprobado, false si
	 */
	public boolean validarAprobacionEnActividadActual(Integer ide_actual_preta){
		Integer contadorNo=0;
		if(ide_actual_preta==3){
			if(tab_precontractual.getValor("aprueba_informetm_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==6){
			if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==18){
			if(tab_precontractual.getValor("aprueba_proyecto_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==25){
			if(tab_precontractual.getValor("es_favorable_informej_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==27){
			//CATALOGO ELECTRONICO (2)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
				if(tab_precontractual.getValor("esta_ok_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==31){
			//CATALOGO ELECTRONICO (2)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTIA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//REGIMEN ESPECIAL (16)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("es_superior_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==38){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("comision_tecnica_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//PRODUCCIÓN NACIONAL (15)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
				if(tab_precontractual.getValor("manifestacion_interes_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==39){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==40){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==41){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//LISTA CORTA - CONSULTORÍA  (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA  (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
		}
		if(ide_actual_preta==42){
			//MENOR CUANTÍA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("se_recibe_oferta_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("se_recibe_oferta_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==43){
			//PRODUCCIÓN NACIONAL (15)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
				if(tab_precontractual.getValor("cumple_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//SUBASTA INVERSA (16)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - BIENES (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONCURSO PÚBLICO- CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==48){
			//MENOR CUNATIA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUNATIA - BIENES (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==49){
			//MENOR CAUNTÍA - OBRAS (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
		}
		if(ide_actual_preta==55){
			//MENOR CAUNTÍA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
						if(tab_precontractual.getValor("recomienda_adjudicar_prpre").equals("NO")){
								contadorNo=contadorNo+1;
						}
					}
				}
			}
		}
		if(ide_actual_preta==59){
			//SUBASTA INVERSA 
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SI ES ERROR FORMA
				if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
					//SI ES CONVALIDACION SATISFACTORIA
					if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
						if(tab_precontractual.getValor("existe_mas_oferta_prpre").equals("NO")){
							contadorNo=contadorNo+1;
						}
					}
				}
			}
		}
		if(ide_actual_preta==60){
			//SUBASTA INVERSA 
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SI ES ERROR FORMA
				if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
					//SI ES CONVALIDACION SATISFACTORIA
					if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
						if(tab_precontractual.getValor("existe_mas_oferta_prpre").equals("NO")){
							contadorNo=contadorNo+1;
						}
					}
				}
			}
		}
		if(ide_actual_preta==76){
			if(tab_precontractual.getValor("es_proceso_infima_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==79){
			if(tab_precontractual.getValor("solicitar_alcance_prpre").equals("NO")){
					contadorNo=contadorNo+1;
			}
		}
		if(ide_actual_preta==101){
			//MENOR CUANTÍA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//REGIMEN ESPECIAL (16)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - BIENES (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("es_viable_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==107){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//REGIMEN ESPECIAL (16)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				if(tab_precontractual.getValor("convalida_recadjudicar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(ide_actual_preta==114){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - OBRAS (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//REGIMEN ESPECIAL (16)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//MENOR CUANTÍA - OBRAS (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("necesario_reaperturar_prpre").equals("NO")){
						contadorNo=contadorNo+1;
				}
			}
		}
		if(contadorNo>0){
			cumpleAprobacion=false;
		}else{
			cumpleAprobacion=true;
		}
		return cumpleAprobacion;
	}
	/**
	 * Metodo que muestra notificación de las tareas que debe hacer en la actividad actual
	 * @param ide_actual_preta
	 */
	public void mostrarNotificacionActividad(Integer ide_actual_preta){
		String concatenacion="";
		String etapa_actual=cargarEtapaActual(ide_actual_preta);
		String ser_etapa_requisitos=ser_etapa_requisito.getEtapaConRequisito(ide_actual_preta);
		TablaGenerica tab_etapa_presenta = utilitario.consultar(ser_etapa_requisitos);
		for(int i = 0; i < tab_etapa_presenta.getTotalFilas(); i++){
			String requisito=tab_etapa_presenta.getValor(i, "descripcion_prreq");
			concatenacion=concatenacion+requisito+" -- ";
		}
		if(tab_etapa_presenta.getTotalFilas()!=0){
			utilitario.agregarNotificacionInfo("ACTIVIDAD ACTUAL: "+etapa_actual+" -- "+mensajeNotificacionEtapa(ide_actual_preta),"REQUISITOS A SUBIR: "+concatenacion);
		}else{
			utilitario.agregarNotificacionInfo("ACTIVIDAD ACTUAL: "+etapa_actual+" -- "+mensajeNotificacionEtapa(ide_actual_preta),"REQUISITOS A SUBIR: No tiene que subir ningún requisito");
		}
	}
	/**
	 * Metodo que devuelve un mensaje para mostrar en la notificación de acuerdo a la actividad actual
	 * @param ide_actual_preta
	 * @return
	 */
	public String mensajeNotificacionEtapa(Integer ide_actual_preta){
		String aprobacion="";
		if(ide_actual_preta==3){
			aprobacion="Debe Seleccionar SI o NO en pregunta Aprueba Informe Técnico Motivado";
		}
		if(ide_actual_preta==6){
			aprobacion="Debe Seleccionar SI o NO en pregunta Bien o Servicio se Encuentra en Catálogo Electrónico";
		}
		if(ide_actual_preta==8 || ide_actual_preta==9){
			aprobacion="Debe registrar el monto, y seleccionar SI o NO en pregunta Defina si es un bien o Servicio ";
		}
		if(ide_actual_preta==16){
			aprobacion="Debe Seleccionar SI o NO en preguntas Consta en POA y Consta en PAC";
		}
		if(ide_actual_preta==18){
			aprobacion="Debe Seleccionar SI o NO en pregunta Aprueba";
		}
		if(ide_actual_preta==25){
			aprobacion="Debe Seleccionar SI o NO en pregunta Es Favorable";
		}
		if(ide_actual_preta==76){
			aprobacion="Debe Seleccionar SI o NO en pregunta Es Proceso de Ínfimas";
		}
		if(ide_actual_preta==79){
			aprobacion="Debe Seleccionar SI o NO en pregunta Se necesita solicitar alcance?";
		}
		if(ide_actual_preta==27){
			//CATÁLOGO ELECTRÓNICO
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
				aprobacion="Debe Seleccionar SI o NO en pregunta Esta OK?";
			}
		}
		if(ide_actual_preta==31){
			//CATÁLOGO ELECTRÓNICO
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==2){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//MENOR CUANTÍA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//REGIMEN ESPECIAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//LICITACIÓN- BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//LICITACIÓN- OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//COTIZACIÓN- BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//COTIZACIÓN- OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//LISTA CORTA- CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
			//CONCURSO PÚBLICO - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Superior al monto definido?";
			}
		}
		if(ide_actual_preta==38){
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Requiere Comisión Técnica?";
			}
			//PRODUCTO NACIONAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existen manifestaciones de interés?";
			}
		}
		if(ide_actual_preta==39){
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
		}
		if(ide_actual_preta==40){
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
		}
		if(ide_actual_preta==41){
			//SUBASTA INVERSA (3)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//LICITACIÓN - BIENES (7)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//LICITACIÓN - OBRAS (10)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//COTIZACIÓN - BIENES (6)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//COTIZACIÓN - OBRAS (9)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA (12)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//LISTA CORTA - CONSULTORÍA (13)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
			//CONCURSO PÚBLICO - CONSULTORÍA (14)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
		}
		if(ide_actual_preta==42){
			//MENOR CUANTÍA - OBRAS  
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Se recibe la oferta?";
			}
			//MENOR CUANTÍA - BIENES  
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				aprobacion="Debe Seleccionar SI o NO en pregunta Se recibe la oferta?";
			}
		}
		if(ide_actual_preta==43){
			//PRODUCTO NACIONAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==15){
				aprobacion="Debe Seleccionar SI o NO en pregunta Cumple?";
			}
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//MENOR CUANTÍA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//MENOR CUANTÍA - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//LICITACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//LICITACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//COTIZACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//COTIZACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//LISTA CORTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
			//CONCRUSO PÚBLICO - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				aprobacion="Debe Seleccionar SI o NO en pregunta Existe errores de forma?";
			}
		}
		if(ide_actual_preta==48){
			//MENOR CUANTÍA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//MENOR CUANTÍA - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//LICITACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//LICITACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//COTIZACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//COTIZACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
		}
		if(ide_actual_preta==49){
			//MENOR CUANTÍA - BIENES (5)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
		}
		if(ide_actual_preta==55){
			//MENOR CUANTÍA - OBRAS (8)
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				if(tab_precontractual.getValor("es_viable_prpre").equals("SI")){
					if(tab_precontractual.getValor("error_forma_prpre").equals("NO")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
					if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Recomienda adjudicar?";
					}
				}
			}
		}
		if(ide_actual_preta==59){
			//SUBASTA INVERSA 
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SI ES ERROR FORMA
				if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
					if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
						//SI ES CONVALIDACION SATISFACTORIA
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Existe mas de una oferta?";
						}
					}
				}
			}
		}
		if(ide_actual_preta==60){
			//SUBASTA INVERSA 
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				//SI ES ERROR FORMA
				if(tab_precontractual.getValor("error_forma_prpre").equals("SI")){
					//SI ES CONVALIDACION SATISFACTORIA
					if(tab_precontractual.getValor("recomienda_adjudicar_prpre")==null){
						//SI ES CONVALIDACION SATISFACTORIA
						if(tab_precontractual.getValor("convalidacion_satisfac_prpre").equals("SI")){
							aprobacion="Debe Seleccionar SI o NO en pregunta Existe mas de una oferta?";
						}
					}
				}
			}
		}
		if(ide_actual_preta==101){
			//MENOR CUANTÍA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//REGIMEN ESPECIAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//MENOR CUANTÍA - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//LICITACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//LICITACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//COTIZACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//COTIZACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//LISTA CORTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
			//CONCURSO PÚBLICO - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es Viable?";
			}
		}
		if(ide_actual_preta==107){
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//REGIMEN ESPECIAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria Y recomienda adjudicar?";
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//LISTA CORTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
			//CONCURSO PÚBLICO - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				aprobacion="Debe Seleccionar SI o NO en pregunta Convalidación Satisfactoria?";
			}
		}
		if(ide_actual_preta==114){
			//SUBASTA INVERSA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==3){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//MENOR CUANTIA - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==8){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//REGIMEN ESPECIAL
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==16){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//MENOR CUANTIA - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==5){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//LICITACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==7){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//LICITACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==10){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//COTIZACIÓN - BIENES
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==6){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//COTIZACIÓN - OBRAS
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==9){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//CONTRATACIÓN DIRECTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==12){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//LISTA CORTA - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==13){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
			//CONCURSO PÚBLICO - CONSULTORÍA
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))==14){
				aprobacion="Debe Seleccionar SI o NO en pregunta Es necesario reaperturar";
			}
		}
		return aprobacion;
	}
	/**
	 * Metodo que permite guardar el seguimiento, de cada actividad que se realice en el proceso
	 */
	public void guardarSeguimiento(Integer ide_preta){
		tab_seguimiento.insertar();
		tab_seguimiento.setValor("ide_prpre",tab_precontractual.getValor("ide_prpre"));//ID PRECONTRACTUAL
		tab_seguimiento.setValor("fecha_asignacion_prseg",tab_precontractual.getValor("fecha_prpre"));//FECHA ASIGNACIÓN");
		tab_seguimiento.setValor("hora_asignacion_prseg",tab_precontractual.getValor("hora_prpre"));//HORA ASIGNACIÓN");
		tab_seguimiento.setValor("fecha_cambio_prseg",utilitario.getFechaActual());//FECHA CAMBIO");
		tab_seguimiento.setValor("hora_cambio_prseg",utilitario.getHoraActual());//HORA CAMBIO");
		tab_seguimiento.setValor("etapa_inicio_prseg",tab_precontractual.getValor("actividad_actual_prpre"));//("ACTIVIDAD INICIO");
		tab_seguimiento.setValor("responsable_prseg",tab_precontractual.getValor("responsable_actual_prpre"));//"RESPONSABLE");
		tab_seguimiento.setValor("departamento_prseg",tab_precontractual.getValor("departamento_actual_prpre"));//DEPARTAMENTO ACTUAL
		tab_seguimiento.setValor("ide_preta_prseg",tab_precontractual.getValor("ide_actual_preta"));//ID ACTIVIDAD INICIO 
		String etapa_final=cargarEtapaSiguientesAntesdeEnviar(ide_preta);
		tab_seguimiento.setValor("etapa_fin_prseg",etapa_final);//ACTIVIDAD FIN");
		String ide_geedp_enviar=cargarIdUsuario(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),ide_preta);
		//Si es igual a null quiere decir que no tiene la etapa un usuario asignado 
		if(ide_geedp_enviar==null){
			String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
			String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
			TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
			String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
			TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
			tab_seguimiento.setValor("responsable_asignado_prseg",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
			tab_seguimiento.setValor("departamento_asignado_prseg",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
		}else{
			String ide_geedp_enviar_serial=ide_geedp_enviar;
			String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar_serial);
			TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
			String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
			TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
			tab_seguimiento.setValor("responsable_asignado_prseg",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
			tab_seguimiento.setValor("departamento_asignado_prseg",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
		}
		tab_seguimiento.setValor("observacion_prseg",tab_precontractual.getValor("observacion_prpre"));//"OBSERVACION");
		tab_seguimiento.setValor("estado_actividad_prseg",tab_precontractual.getValor("estado_proceso_prpre"));//ESTADO ACTIVIDAD");
		String estadoProcedimiento=cargarEstadoProcedimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")));
		tab_seguimiento.setValor("estado_procedimiento_prseg",estadoProcedimiento);//ESTADO PROCEDIMIENTO");	
		tab_seguimiento.setValor("activo_prseg","true");
		tab_seguimiento.guardar();
		guardarPantalla();
		tab_seguimiento.dibujar();
		utilitario.addUpdate("tab_seguimiento");
	}
	/**
	 * Metodo que permite guardar el seguimiento, de finalización del proceso
	 */
	public void guardarSeguimientoFin(String observacion){
		tab_seguimiento.insertar();
		tab_seguimiento.setValor("ide_prpre",tab_precontractual.getValor("ide_prpre"));//ID PRECONTRACTUAL
		tab_seguimiento.setValor("fecha_asignacion_prseg",tab_precontractual.getValor("fecha_prpre"));//FECHA ASIGNACIÓN");
		tab_seguimiento.setValor("hora_asignacion_prseg",tab_precontractual.getValor("hora_prpre"));//HORA ASIGNACIÓN");
		tab_seguimiento.setValor("fecha_cambio_prseg",utilitario.getFechaActual());//FECHA CAMBIO");
		tab_seguimiento.setValor("hora_cambio_prseg",utilitario.getHoraActual());//HORA CAMBIO");
		tab_seguimiento.setValor("etapa_inicio_prseg",tab_precontractual.getValor("actividad_actual_prpre"));//("ACTIVIDAD INICIO");
		tab_seguimiento.setValor("responsable_prseg",tab_precontractual.getValor("responsable_actual_prpre"));//"RESPONSABLE");
		tab_seguimiento.setValor("departamento_prseg",tab_precontractual.getValor("departamento_actual_prpre"));//DEPARTAMENTO ACTUAL
		tab_seguimiento.setValor("ide_preta_prseg",tab_precontractual.getValor("ide_actual_preta"));//ID ACTIVIDAD INICIO 
		tab_seguimiento.setValor("observacion_prseg",observacion);//"OBSERVACION");
		tab_seguimiento.setValor("estado_actividad_prseg",tab_precontractual.getValor("estado_proceso_prpre"));//ESTADO ACTIVIDAD");
		String estadoProcedimiento=cargarEstadoProcedimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")));
		tab_seguimiento.setValor("estado_procedimiento_prseg",estadoProcedimiento);//ESTADO PROCEDIMIENTO");	
		tab_seguimiento.setValor("activo_prseg","true");
		tab_seguimiento.guardar();
		guardarPantalla();
		tab_seguimiento.dibujar();
		utilitario.addUpdate("tab_seguimiento");
	}
	/**
	 * Metodo que permite cargar el estado del procedimiento
	 * @return
	 */
	public String cargarEstadoProcedimiento(Integer ide_pretp){
		String estado_procedimiento="";
		String ser_estadoProcedimiento=ser_EtapaProcedimiento.getProcedimientos(ide_pretp);
		TablaGenerica tab_estadoProcedimiento=utilitario.consultar(ser_estadoProcedimiento);
		estado_procedimiento=tab_estadoProcedimiento.getValor("descripcion");
		return estado_procedimiento;
	}
	/**
	 * Metodo que permite cargar la actividad antes de enviar de acuerdo a su id
	 * @param ide_preta
	 * @return
	 */
	public String cargarEtapaSiguientesAntesdeEnviar(Integer ide_preta){
		String etapa;
		String ser_cargaEtapa=ser_etapa.getEtapa(ide_preta);
		TablaGenerica tab_cargaEtapa=utilitario.consultar(ser_cargaEtapa);
		etapa=tab_cargaEtapa.getValor("descripcion_preta");
		return etapa;
	}
	/**
	 * Metodo que permite cambiar el estado del proceso y guardar su seguimiento
	 * @param estado FINALIZADO
	 * @param mensajeObservacion que se guarda en el seguimiento
	 */
	public void cambiarEstadoProceso(String estado, String mensajeObservacion){
		tab_precontractual.setValor("estado_proceso_prpre",estado);
		tab_precontractual.modificar(tab_precontractual.getFilaActual());
		tab_precontractual.guardar();
		guardarPantalla();
		utilitario.addUpdate("tab_precontractual");
		guardarSeguimientoFin(mensajeObservacion);
	}
	/**
	 * Metodo que permite realizar un insert a la tabla pre_contrato, para empezar con la fase contractual
	 * @param tipocontrato
	 * @param objeto_contrato
	 * @param monto
	 * @param ide_proveedor
	 */
	public void cargarDatosEnContrato(String tipocontrato,String objeto_contrato,Double monto,Integer ide_proveedor){
		Integer max_ide_prcon=0;
		Integer ide_tipocontrato=0;
		TablaGenerica tab_max_ide=utilitario.consultar("select 1 as id, max(ide_prcon) as max_ide from pre_contrato;");
		max_ide_prcon=pckUtilidades.CConversion.CInt(tab_max_ide.getValor("max_ide"))+1;
		if(tipocontrato.equals("TR")){
			ide_tipocontrato=4;
		}else{
			ide_tipocontrato=5;
		}
		if(ide_proveedor==0){
			ide_proveedor=null;
		}
		String insert_contrato=ser_generalAdm.getInsertContrato(max_ide_prcon,ide_tipocontrato, objeto_contrato, monto, ide_proveedor);
		utilitario.getConexion().ejecutarSql(insert_contrato);
	}
	/**
	 * Metodo que carga la fase actual segun en id del procedimiento
	 * @param ide_pretp
	 * @return
	 */
	public String cargarFaseActual(Integer ide_prpre){
		String fase_actual="";
		String ser_fase_actual=ser_fase.getFase(ide_prpre);
		TablaGenerica tab_fase_actual=utilitario.consultar(ser_fase_actual);
		fase_actual=tab_fase_actual.getValor("descripcion_prfas");
		return fase_actual;
	}
	
	public boolean validarPerfil(List perfilUsuarioConectado, String strPerfil)
	{		
		for(Object perfil : perfilUsuarioConectado)
		{
			if(perfil.toString().equals(strPerfil))
				return true;		
		}		
		return false;
	}
	
	public void enviarMailActividad(Integer ide_usuario,String codigo_proceso,String proceso_contratacion,Integer procedimiento_contratacion,String actividad,String fecha_asignacion,String hora_asignacion){
		String concatenacionFechaHoraAsignacionActividad=fecha_asignacion+" - "+hora_asignacion;
		
		String procedimientoContratacion="";
		String ser_etapa_proce=ser_EtapaProcedimiento.getProcedimientos(procedimiento_contratacion);
		TablaGenerica tab_etapa_actual=utilitario.consultar(ser_etapa_proce);
		procedimientoContratacion=tab_etapa_actual.getValor("descripcion");
				
		TablaGenerica tab_correos= utilitario.consultar("select nom_usua, mail_usua from sis_usuario where ide_gtemp='"+ide_usuario+"'");
		
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=3"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		
			
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
		
		for (int i = 0; i < tab_correos.getTotalFilas(); i++) {
			
			/*try {
				if(tab_correos.getValor(i,"mail_usua")!=null)
				//util.EnviaMail(envMail, tab_correos.getValor(i,"mail_usua"), 
								"CONTROL DE SEGUIMIENTO CONTRATACIÓN",
								emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad),null);
			}catch (Exception e) {
				System.out.println("Error en el envío de correo"+e.getMessage());
			}*/
			
			try {
				if(tab_correos.getValor(i,"mail_usua")!=null)
				{
					/*
					  //util.EnviaMail(envMail, tab_correos.getValor(i,"mail_usua"),"CONTROL DE SEGUIMIENTO CONTRATACIÓN",
									emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad),null);*/
						
					String str_mail=tab_correos.getValor(i,"mail_usua");//"alex.becerra@emgirs.gob.ec", 
					envMail.setAsunto("CONTROL DE SEGUIMIENTO CONTRATACIÓN");
					envMail.setCuerpoHtml(emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad));
					envMail.setPara(str_mail);
					pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
					
					if(obj.getRespuesta())
					{
						utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
					}
					else
						utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
				}
			}catch (Exception e) {
				System.out.println("Error en el envío de correo"+e.getMessage());
			}
			
			System.out.println("envMail: "+tab_correos.getValor(i,"mail_usua"));
			//System.out.println("envMail: "+emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad));
		}
	}
	
	//CUERPO DEL MENSAJE A ENVIAR AL EMPLEADO ASIGNADO
	public String emailNotificacionCambioActividad(String nombreEmpleado,String codigo_proceso,String proceso_contratacion,String procedimiento_contratacion,String actividad,String fechaHora_asignacion) {
		      String html = "<p>Estimado(a), "
		              + "</p>\n"
		              + "<p>"+nombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>El Proceso "+procedimiento_contratacion+" de Contratación: "+codigo_proceso.toLowerCase()+" "+proceso_contratacion+", se le asignado, para que realice la Actividad: "+actividad.toLowerCase()+". Con Fecha y Hora de Asignacion: "+fechaHora_asignacion+"</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
		      return html;
	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		if(tab_precontractual.isFocus()){
			mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		if(tab_precontractual.isFocus()){
			mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		if(tab_precontractual.isFocus()){
			mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		if(tab_precontractual.isFocus()){
			mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}
	
	@Override
	public void actualizar(){
		super.actualizar();
		if(tab_precontractual.isFocus()){
			mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")==null){
					ventanaSeleccionPrecontractualParalela();
				}
			}
		}
	}
	
	public Tabla getTab_precontractual() {
		return tab_precontractual;
	}

	public void setTab_precontractual(Tabla tab_precontractual) {
		this.tab_precontractual = tab_precontractual;
	}

	public Tabla getTab_documento_requisito() {
		return tab_documento_requisito;
	}

	public void setTab_documento_requisito(Tabla tab_documento_requisito) {
		this.tab_documento_requisito = tab_documento_requisito;
	}

	public Tabla getTab_seguimiento() {
		return tab_seguimiento;
	}

	public void setTab_seguimiento(Tabla tab_seguimiento) {
		this.tab_seguimiento = tab_seguimiento;
	}

	public ServicioEtapaProcedimiento getSer_EtapaProcedimiento() {
		return ser_EtapaProcedimiento;
	}

	public void setSer_EtapaProcedimiento(
			ServicioEtapaProcedimiento ser_EtapaProcedimiento) {
		this.ser_EtapaProcedimiento = ser_EtapaProcedimiento;
	}

	public ServicioRequisito getSer_Requisito() {
		return ser_Requisito;
	}

	public void setSer_Requisito(ServicioRequisito ser_Requisito) {
		this.ser_Requisito = ser_Requisito;
	}

	public ServicioSeguimiento getSer_Seguimiento() {
		return ser_Seguimiento;
	}

	public void setSer_Seguimiento(ServicioSeguimiento ser_Seguimiento) {
		this.ser_Seguimiento = ser_Seguimiento;
	}

	public ServicioGeneralAdmPrecon getSer_generalAdm() {
		return ser_generalAdm;
	}

	public void setSer_generalAdm(ServicioGeneralAdmPrecon ser_generalAdm) {
		this.ser_generalAdm = ser_generalAdm;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioPrecontractual getSer_precontractual() {
		return ser_precontractual;
	}

	public void setSer_precontractual(ServicioPrecontractual ser_precontractual) {
		this.ser_precontractual = ser_precontractual;
	}

	public ServicioEtapaRequisito getSer_etapa_requisito() {
		return ser_etapa_requisito;
	}

	public void setSer_etapa_requisito(ServicioEtapaRequisito ser_etapa_requisito) {
		this.ser_etapa_requisito = ser_etapa_requisito;
	}

	public ServicioEtapa getSer_etapa() {
		return ser_etapa;
	}

	public void setSer_etapa(ServicioEtapa ser_etapa) {
		this.ser_etapa = ser_etapa;
	}

	public ServicioRuta getSer_ruta() {
		return ser_ruta;
	}

	public void setSer_ruta(ServicioRuta ser_ruta) {
		this.ser_ruta = ser_ruta;
	}

	public SeleccionTabla getSet_tipo_actividad() {
		return set_tipo_actividad;
	}

	public void setSet_tipo_actividad(SeleccionTabla set_tipo_actividad) {
		this.set_tipo_actividad = set_tipo_actividad;
	}

	public SeleccionTabla getSet_monto_contratacion() {
		return set_monto_contratacion;
	}

	public void setSet_monto_contratacion(SeleccionTabla set_monto_contratacion) {
		this.set_monto_contratacion = set_monto_contratacion;
	}

	public ServicioProcedimiento getSer_procedimiento() {
		return ser_procedimiento;
	}

	public void setSer_procedimiento(ServicioProcedimiento ser_procedimiento) {
		this.ser_procedimiento = ser_procedimiento;
	}

	public SeleccionTabla getSet_tipo_actividad_union() {
		return set_tipo_actividad_union;
	}

	public void setSet_tipo_actividad_union(SeleccionTabla set_tipo_actividad_union) {
		this.set_tipo_actividad_union = set_tipo_actividad_union;
	}

}
