package paq_tesoreria;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import pck_cliente.servicio;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.VisualizarPDF;

public class pre_adq_factura_retencion extends Pantalla {

    private Tabla tab_adq_factura = new Tabla();
    
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle_movimiento =new Tabla();
    private Tabla tab_retencion =new Tabla();
    private Tabla tab_detalle_retencion=new Tabla();
    private SeleccionTabla set_solicitud=new SeleccionTabla();
    private SeleccionTabla set_tramite=new SeleccionTabla();
    private SeleccionTabla set_impuesto=new SeleccionTabla();
    private SeleccionTabla set_retencion=new SeleccionTabla();
	public static String par_impuesto_renta;
	public static String par_impuesto_iva;
	public static double par_iva;
	
	//Inicio Retencion Electronica - ABECERRA	
	private Dialogo respuesta_core_dialogo = new Dialogo();
	private Dialogo retencion_elec_dialogo = new Dialogo();
	private int ambiente = 2; //1 Test: 2 Produccion
	private boolean autorizar = true; //true Produccion
	private Check che_ambiente=new Check();
	//private String str_ide_retencion;
	//Fin Retencion Electronica - ABECERRA
	private VisualizarPDF vpdf_rideRet = new VisualizarPDF();
		
	private Combo com_anio=new Combo();
	private String str_seleccionado="";

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

    public pre_adq_factura_retencion (){
    	
    	//parametros
    	par_impuesto_iva=utilitario.getVariable("p_tes_impuesto_iva");
		par_impuesto_renta=utilitario.getVariable("p_tes_impuesto_renta");
		par_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva"));
		ambiente=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_ambiente_sri"));
		
    	///tabuladores
    	 Tabulador tab_tabulador = new Tabulador();
         tab_tabulador.setId("tab_tabulador");

         // tabla factura        
         tab_adq_factura.setId("tab_adq_factura");
         tab_adq_factura.setHeader("FACTURA - COMPRA");
         tab_adq_factura.setTabla("adq_factura", "ide_adfac",  1);
         tab_adq_factura.setCampoOrden("ide_adfac desc");
         tab_adq_factura.getColumna("IDE_ADSOC").setCombo(ser_Adquisicion.getSolicitudCompra("true"));
         tab_adq_factura.getColumna("IDE_ADSOC").setAutoCompletar();
         tab_adq_factura.getColumna("IDE_ADSOC").setLectura(true);
         tab_adq_factura.getColumna("subtotal_adfac").setLectura(true);
         //tab_adq_factura.getColumna("valor_iva_adfac").setLectura(true);
         tab_adq_factura.getColumna("num_factura_adfac").setLectura(true);
         tab_adq_factura.getColumna("fecha_factura_adfac").setLectura(true);
         tab_adq_factura.getColumna("detalle_adfac").setLectura(true);
         //tab_adq_factura.getColumna("total_adfac").setLectura(true);
         tab_adq_factura.getColumna("base_iva_adfac").setLectura(true);
         tab_adq_factura.getColumna("activo_adfac").setLectura(true);
         tab_adq_factura.getColumna("valor_descuento_adfac").setVisible(false);
         tab_adq_factura.getColumna("porcent_desc_adfac").setVisible(false);
         tab_adq_factura.getColumna("aplica_descuento_adfac").setVisible(false);
 	     tab_adq_factura.getColumna("ide_tepro").setCombo(ser_Bodega.getProveedor("true,false"));
 		 tab_adq_factura.getColumna("ide_tepro").setLectura(true);
 		 tab_adq_factura.getColumna("ide_tepro").setAutoCompletar();
 		 tab_adq_factura.getColumna("IDE_PRTRA").setCombo(ser_Presupuesto.getTramite("true"));
 		 tab_adq_factura.getColumna("IDE_PRTRA").setAutoCompletar();
 		 tab_adq_factura.getColumna("IDE_PRTRA").setLectura(true);
 		 tab_adq_factura.getColumna("ide_srsuc").setCombo("sri_sustento_comprobante", "ide_srsuc", "codigo_srsuc || detalle_srsuc", "");
	     tab_adq_factura.getColumna("ide_srsuc").setAutoCompletar();
	     tab_adq_factura.setCondicion("ide_adfac=-1");
 		 tab_adq_factura.agregarRelacion(tab_retencion);
         //tab_adq_factura.onSelect("actualizaPantallas2");
         tab_adq_factura.setTipoFormulario(true);
         tab_adq_factura.getGrid().setColumns(4);
         tab_adq_factura.dibujar();
         PanelTabla pat_factura=new PanelTabla();
         pat_factura.setPanelTabla(tab_adq_factura);
         
         ///RETENCION
         tab_retencion.setId("tab_retencion");
         tab_retencion.setTabla("tes_retencion", "ide_teret", 2);
         tab_retencion.getColumna("total_ret_teret").setEtiqueta();
         tab_retencion.getColumna("total_ret_teret").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
         tab_retencion.getColumna("total_ret_teret").setValorDefecto("0.00");
         tab_retencion.getColumna("activo_teret").setValorDefecto("true");
         tab_retencion.getColumna("activo_teret").setLectura(true);
         tab_retencion.getColumna("autorizada_sri_teret").setLectura(true);
         tab_retencion.getColumna("autorizada_sri_teret").setValorDefecto("false");
         tab_retencion.getColumna("ide_tecpo").setVisible(false);
         tab_retencion.getColumna("fecha_teret").setValorDefecto(utilitario.getFechaActual());
         tab_retencion.getColumna("ide_coest").setValorDefecto("2");
         tab_retencion.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
         tab_retencion.setCampoOrden("fecha_teret, ide_coest desc");
         tab_retencion.agregarRelacion(tab_detalle_retencion);
         tab_retencion.setTipoFormulario(true);
         tab_retencion.getGrid().setColumns(4);
         tab_retencion.dibujar();
         PanelTabla pat_retencion =new PanelTabla();
         pat_retencion.setPanelTabla(tab_retencion);
         
         /////etiqueta
         Etiqueta eti_retencion=new Etiqueta(); 
         eti_retencion.setValue("RETENCION");
         eti_retencion.setStyle("font-size: 13px;color: red;font-weight: bold");
         pat_retencion.setHeader(eti_retencion);
      
         ///DETALLE RETENCION
         tab_detalle_retencion.setId("tab_detalle_retencion");
         tab_detalle_retencion.setTabla("tes_detalle_retencion", "ide_teder", 3);
         tab_detalle_retencion.getColumna("ide_teimp").setCombo("tes_impuesto", "ide_teimp", "codigo_teimp,porcentaje_teimp,detalle_teimp", "");
         tab_detalle_retencion.getColumna("ide_teimp").setLectura(true);
         tab_detalle_retencion.getColumna("ide_teimp").setAutoCompletar();
         tab_detalle_retencion.getColumna("base_imponible_teder").setValorDefecto("0.00");
         tab_detalle_retencion.getColumna("base_imponible_teder").setMetodoChange("recalcular");
         tab_detalle_retencion.getColumna("valor_retenido_teder").setEtiqueta();
         tab_detalle_retencion.getColumna("valor_retenido_teder").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
         tab_detalle_retencion.getColumna("valor_retenido_teder").setValorDefecto("0.00");
         tab_detalle_retencion.getColumna("activo_teder").setValorDefecto("true");
         tab_detalle_retencion.getColumna("activo_teder").setLectura(true);
         tab_detalle_retencion.setTipoFormulario(true);
         tab_detalle_retencion.getGrid().setColumns(4);
         tab_detalle_retencion.dibujar();
         PanelTabla pat_detalle_retencion=new PanelTabla();
         pat_detalle_retencion.setPanelTabla(tab_detalle_retencion);
         
         ////para obteber las dos ventanas retencion y detalla retención
         Etiqueta eti_detalle_retencion=new Etiqueta(); 
         eti_detalle_retencion.setValue("DETALLE RETENCION");
         eti_detalle_retencion.setStyle("font-size: 13px;color: red;font-weight: bold");
         pat_detalle_retencion.setHeader(eti_detalle_retencion);


         /*Grid gri=new Grid();
         gri.setColumns(2);
         gri.getChildren().add(pat_retencion);
         gri.getChildren().add(pat_detalle_retencion);*/

         Division div_division =new Division();
         div_division.dividir3(pat_factura, pat_retencion, pat_detalle_retencion, "45%", "30%", "H");
         agregarComponente(div_division);
         
         com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
 		 com_anio.setMetodo("seleccionaElAnio");
 		 com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
 		 bar_botones.agregarComponente(new Etiqueta("AÑO:"));
 		 bar_botones.agregarComponente(com_anio);
         
         ///boton tipo impuesto
         Boton bot_impuesto=new Boton();
         bot_impuesto.setIcon("ui-icon-person");
         bot_impuesto.setValue("Generar Retencion");
         bot_impuesto.setMetodo("importarImpuesto");
         bar_botones.agregarBoton(bot_impuesto);
         
         set_impuesto.setId("set_impuesto");
         set_impuesto.setSeleccionTabla("tes_tipo_impuesto", "ide_tetii", "detalle_tetii");
         set_impuesto.setTitle("SELECCIONE UN IMPUESTO");        
         set_impuesto.getBot_aceptar().setMetodo("aceptarImpuesto");
         set_impuesto.setRadio();
         agregarComponente(set_impuesto);
         // retencion
         set_retencion.setId("set_retencion");
         set_retencion.setSeleccionTabla(ser_Tesoreria.getImpuesto("true","1","0"),"ide_teimp");
         set_retencion.setTitle("SELECCIONE UNA RETENCIÓN");        
         set_retencion.getBot_aceptar().setMetodo("aceptarImpuesto");
         set_retencion.getTab_seleccion().getColumna("CODIGO_TEIMP").setFiltro(true);
 		 set_retencion.getTab_seleccion().getColumna("DETALLE_TEIMP").setFiltro(true);
 		 set_retencion.setRadio();
         agregarComponente(set_retencion);
         
         //agregar compromiso presupuesto
         Boton bot_busca=new Boton();
         bot_busca.setIcon("ui-icon-person");
         bot_busca.setValue("Agregar Compromiso Presupuestario");
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
         
         
       //Inicio Facturacion Electronica - ABECERRA
        che_ambiente.setId("che_ambiente");
        che_ambiente.setMetodoChange("cambiaAmbiente");
 		Etiqueta eti_ambiente=new Etiqueta("PRUEBAS");
 		bar_botones.agregarComponente(eti_ambiente);
 		bar_botones.agregarComponente(che_ambiente);
 		
 		// BOTÓN RETENCION ELECTRÓNICA ---------------------------------
 		Boton bot_fac_elec = new Boton();
 		bot_fac_elec.setIcon("ui-icon-newwin");
 		bot_fac_elec.setValue("Retención Electrónica");
 		bot_fac_elec.setMetodo("abrirDialogoRetElectronica");
 		bar_botones.agregarBoton(bot_fac_elec);
 		
 		// DIÁLOGO RETENCION ELECTRÓNICA --------------------------------
 		retencion_elec_dialogo.setId("retencion_elec_dialogo");
 		retencion_elec_dialogo.setTitle("GENERAR RETENCION ELECTRÓNICA");
 		retencion_elec_dialogo.setWidth("45%");
 		retencion_elec_dialogo.setHeight("30%");

 		// GRID RETENCION ELECTRÓNICA
 		Grid gri_fac_elec = new Grid();
 		gri_fac_elec.setColumns(2);
 		retencion_elec_dialogo.setDialogo(gri_fac_elec);
 		retencion_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoRetElectronica");
 		agregarComponente(retencion_elec_dialogo);

 		// DIÁLOGO RESPUESTA DEL CORE --------------------------------
 		respuesta_core_dialogo.setId("respuesta_core_dialogo");
 		respuesta_core_dialogo.setTitle("RESPUESTA DEL SERVIDOR");
 		respuesta_core_dialogo.setWidth("45%");
 		respuesta_core_dialogo.setHeight("30%");

 		// GRID RESPUESTA DEL CORE
 		Grid gri_respuesta = new Grid();
 		gri_respuesta.setColumns(2);
 		respuesta_core_dialogo.setDialogo(gri_respuesta);
 		respuesta_core_dialogo.getBot_aceptar().setMetodo("aceptarDialogoRespuestaCore");
 		agregarComponente(respuesta_core_dialogo);
 		//FIN Facturacion Electronica - ABECERRA
 		
 		//REPORTE
 		vpdf_rideRet.setId("vpdf_rideRet");
 		vpdf_rideRet.setTitle("Retención Electrónica");
        agregarComponente(vpdf_rideRet);
        
        Boton bot_ride = new Boton();
        bot_ride.setIcon("ui-icon-print");
        bot_ride.setValue("Imprimir Retención");
        bot_ride.setMetodo("verRide");
        //bot_ride.setAjax(false);
 		bar_botones.agregarBoton(bot_ride);

     }
    
    public void seleccionaElAnio (){

    	tab_adq_factura.setCondicion(" extract(year from fecha_factura_adfac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
    	tab_adq_factura.ejecutarSql();
    	tab_retencion.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
    	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
	}
	
    ///recalcular valores
 	
   	public void recalcular(AjaxBehaviorEvent evt){
   		tab_detalle_retencion.modificar(evt);
        TablaGenerica tab_rentas= utilitario.consultar(ser_Tesoreria.getImpuestoCalculo(tab_detalle_retencion.getValor("ide_teimp")));

		double dou_valor_impuesto=0;
        double dou_porcentaje_calculo=0;
        double dou_valor_resultado=0;

        dou_porcentaje_calculo=pckUtilidades.CConversion.CDbl_2(tab_rentas.getValor("porcentaje_teimp"));
        dou_valor_impuesto=pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getValor("base_imponible_teder"));
        dou_valor_resultado=pckUtilidades.CConversion.CDbl_2((dou_porcentaje_calculo*dou_valor_impuesto)/100);
  
        tab_detalle_retencion.setValor("valor_retenido_teder",utilitario.getFormatoNumero( dou_valor_resultado,2)+"");   
        String valorx=tab_detalle_retencion.getSumaColumna("valor_retenido_teder")+"";
        tab_retencion.setValor("total_ret_teret", utilitario.getFormatoNumero(valorx,2));   
        tab_retencion.modificar(tab_retencion.getFilaActual());
        utilitario.addUpdateTabla(tab_detalle_retencion, "valor_retenido_teder,base_imponible_teder,ide_teimp","");
        utilitario.addUpdateTabla(tab_retencion, "total_ret_teret",""); 
    }  
   
    ///sacar valores
 	
   	 public void calcularTotal(AjaxBehaviorEvent evt){
   		tab_detalle_movimiento.modificar(evt);
   		tab_detalle_movimiento.sumarColumnas();
   		utilitario.addUpdate("tab_detalle_movimiento");
   	}

     public void aceptarSolicitudCompra(){
     	 System.out.println("entra a metodo aceptar solicitud");

         String str_seleccionado = set_solicitud.getValorSeleccionado();
         TablaGenerica tab_solicitud=ser_Adquisicion.getTablaGenericaSolicitud(str_seleccionado);
         if (str_seleccionado!=null){
             tab_comprobante.insertar();
             tab_comprobante.setValor("ide_adsoc",str_seleccionado);
             tab_comprobante.setValor("valor_compra_tecpo",tab_solicitud.getValor("total_adfac"));
             tab_comprobante.setValor("valor_iva_tecpo", tab_solicitud.getValor("valor_iva_adfac"));
             tab_comprobante.setValor("ide_prtra", tab_solicitud.getValor("ide_prtra"));
             tab_comprobante.setValor("ide_tepro",tab_solicitud.getValor("ide_tepro"));
         }
         set_solicitud.cerrar();
         utilitario.addUpdate("tab_comprobante");
     }

     public void importarCertificacionPresupuestaria(){
         set_tramite.getTab_seleccion().setSql(ser_Presupuesto.getTramite("true"));
         set_tramite.getTab_seleccion().ejecutarSql();
         set_tramite.dibujar();

     }

     public void aceptarCertificacionPresupuestaria(){

         String str_seleccionado = set_tramite.getValorSeleccionado();
         TablaGenerica tab_tramite=ser_Presupuesto.getTablaGenericaTramite(str_seleccionado,"");
         if (str_seleccionado!=null){
             tab_adq_factura.setValor("ide_prtra",tab_tramite.getValor("ide_prtra"));
             tab_adq_factura.modificar(tab_adq_factura.getFilaActual());
             tab_adq_factura.guardar();
             guardarPantalla();
         }
         set_tramite.cerrar();
         utilitario.addUpdate("tab_adq_factura");
     }
     
     ////boton impuesto
     public void importarImpuesto(){
     	//System.out.println("entra a metodo import impues");
     	if(tab_adq_factura.getValor("subtotal_adfac")==null||tab_adq_factura.getValor("subtotal_adfac").equals("")){
     		utilitario.agregarMensaje("No puede generar una retencion", "Ingrese un valor de compra");
     		return;
     	}
     	if(tab_adq_factura.getValor("VALOR_IVA_ADFAC")==null||tab_adq_factura.getValor("VALOR_IVA_ADFAC").equals("")){
     		utilitario.agregarMensaje("No puede generar una retencion", "Ingrese valor Iva");
     		return;
     	}
     	if(tab_retencion.getTotalFilas()<1){
     		utilitario.agregarMensaje("No puede generar continuar", "Favor cree primero una retención");
     		return;
     	}
     	if(tab_retencion.getValor("autorizada_sri_teret").equals("true") && tab_retencion.getValor("ide_coest").equals("2"))
		{
			utilitario.agregarMensajeInfo("Registro no Editable", "La retención electrónica fue autorizada por el SRI");
			return;
		}
     	
         set_impuesto.getTab_seleccion().setSql("select ide_tetii,detalle_tetii from tes_tipo_impuesto order by ide_tetii");
         set_impuesto.getTab_seleccion().ejecutarSql();
         set_impuesto.dibujar();
         
     }
     
     public void aceptarImpuesto(){

         if(set_impuesto.isVisible()){
             if (set_impuesto.getValorSeleccionado()!=null){
            	 str_seleccionado = set_impuesto.getValorSeleccionado();
 				 System.out.println("probando que valor me llega: "+str_seleccionado);
                 tab_detalle_retencion.insertar();

             	if(set_impuesto.getValorSeleccionado().equals(par_impuesto_iva)){
             		tab_detalle_retencion.setValor("base_imponible_teder", tab_adq_factura.getValor("valor_iva_adfac"));
             	}
             	else if(set_impuesto.getValorSeleccionado().equals(par_impuesto_renta)){
             		tab_detalle_retencion.setValor("base_imponible_teder", tab_adq_factura.getValor("subtotal_adfac"));
             	}

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
			
			if (set_retencion.getValorSeleccionado()!=null){
				str_seleccionado = set_retencion.getValorSeleccionado();
				TablaGenerica tab_rentas= utilitario.consultar(ser_Tesoreria.getImpuestoCalculo(str_seleccionado));
				
				if(tab_rentas.getTotalFilas()>0)
				{
					double dou_valor_impuesto=0;
					double dou_porcentaje_calculo=0;
					double dou_valor_resultado=0;
					
					dou_porcentaje_calculo=pckUtilidades.CConversion.CDbl_2(tab_rentas.getValor("porcentaje_teimp"));
					dou_valor_impuesto=pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getValor("base_imponible_teder"));
		            dou_valor_resultado=pckUtilidades.CConversion.CDbl_2((dou_porcentaje_calculo*dou_valor_impuesto)/100);
	  
	                tab_detalle_retencion.setValor("ide_teimp",str_seleccionado);
					tab_detalle_retencion.setValor("valor_retenido_teder",utilitario.getFormatoNumero( dou_valor_resultado,2)+"");   
					String valorx=tab_detalle_retencion.getSumaColumna("valor_retenido_teder")+"";
					tab_retencion.setValor("total_ret_teret", utilitario.getFormatoNumero(valorx,2));   
					tab_retencion.modificar(tab_retencion.getFilaActual());
					tab_detalle_retencion.modificar(tab_detalle_retencion.getFilaActual());
	
					//utilitario.addUpdateTabla(tab_detalle_retencion, "valor_retenido_teder,base_imponible_teder,ide_teimp","");
					//utilitario.addUpdateTabla(tab_retencion, "total_ret_teret","");
					calcularValorPago();
					
					tab_retencion.guardar();
					tab_detalle_retencion.guardar();
					guardarPantalla();
					
					if(tab_retencion.getTotalFilas()>0)
						if(ser_Adquisicion.actualizarNumeroRetencion(tab_retencion.getValor("ide_teret")).length()>3)
						{
							tab_retencion.actualizar();
							tab_retencion.ejecutarSql();
						}
				}
				else {
	                utilitario.agregarMensajeInfo("No existe impuesto", "Favor configure el impuesto de codigo: "+str_seleccionado);
	            }
				set_retencion.cerrar();
			}
			else {
                utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
            }
         }


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
     		dou_val_compra=pckUtilidades.CConversion.CDbl_2(tab_adq_factura.getValor("subtotal_adfac"));
 		} catch (Exception e) {
 		}
     	try {
 			//Obtenemos el valor de la cantidad
     		dou_total_retencion=pckUtilidades.CConversion.CDbl_2(tab_retencion.getValor("total_ret_teret"));
 		} catch (Exception e) {
 		}
     
     	dou_valor_pago=dou_val_compra-dou_total_retencion;
     //	dou_valor_iva=dou_val_compra*duo_iva;
     	
     /* COMENTADO PORQUE YA SE REALIZA LA RETENCION A LA FACTURA
     	tab_comprobante.setValor("valor_pago_tecpo", utilitario.getFormatoNumero(dou_valor_pago,2));
     	//tab_comprobante.setValor("valor_iva_tecpo", utilitario.getFormatoNumero(dou_valor_iva,2));
     	tab_comprobante.modificar(tab_comprobante.getFilaActual());//para que haga el update
     	utilitario.addUpdateTabla(tab_comprobante,"valor_pago_tecpo,","");
     */
     }

 
	@Override
	 public void insertar() {
		// TODO Auto-generated method stub
		 if (tab_adq_factura.getTotalFilas()>0) {
			 String str_observacion="";
			 boolean insertar=true;
			 if(tab_adq_factura.getValor("detalle_adfac").length()>100){
				 str_observacion=tab_adq_factura.getValor("detalle_adfac").substring(100);
			 }
			 else {
				 str_observacion=tab_adq_factura.getValor("detalle_adfac");
			 }
			 
			 for(int j=0;j<tab_retencion.getTotalFilas();j++){
				 if(pckUtilidades.CConversion.CInt(tab_retencion.getValor(j,"ide_coest"))==2) // estado emitida
					 insertar=false;
			 }
			 
			 if(insertar)
			 {
				 tab_retencion.insertar();
				 tab_retencion.setValor("observacion_teret",str_observacion );
			 }
			 else
				 utilitario.agregarMensajeInfo("No puede insertar", "La factura ya cuenta con una retencion emitida.");
        }
        else if (tab_detalle_retencion.isFocus()) {
            utilitario.agregarMensajeInfo("No puede insertar", "Debe generar una Retencion");

        }    

	}
	
	 public void actualizaPantallas(){
		tab_retencion.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
		tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
	 }
	 
     public void actualizaPantallas2(SelectEvent evt){
		tab_adq_factura.seleccionarFila(evt);
		tab_retencion.ejecutarValorForanea(tab_adq_factura.getValor("ide_adfac"));
		tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValor("ide_teret"));
	 }
	
	 // Retencion Electrónica -----------------------------------------------------
     public void cambiaAmbiente(){
 		if(che_ambiente.getValue().toString().equalsIgnoreCase("true")){
 			ambiente=1; //test
 		}
 		else{
 			ambiente=2; //produccion
 		}
 		
 		System.out.println("cambia ambiente Retencion: "+ambiente);
 	}
	 // Abre el diálogo de confirmación para emitir la Retencion electrónica
	 public void abrirDialogoRetElectronica() {
		 
		 if(!(pckUtilidades.CConversion.CDbl_2(tab_retencion.getValor("total_ret_teret"))==pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getSumaColumna("valor_retenido_teder"))))
 		 //if(!(tab_retencion.getValor("total_ret_teret").equalsIgnoreCase(tab_detalle_retencion.getSumaColumna("valor_retenido_teder")+"")))
		 {
			 utilitario.agregarMensajeInfo("La suma de detalles de la retención", "No concuerda con el total retenido.");
			 return;
		 }
	
		 if(!(pckUtilidades.CConversion.CDbl_2(tab_adq_factura.getValor("total_adfac"))==pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getSumaColumna("base_imponible_teder"))))
		 {
			 utilitario.agregarMensajeInfo("La suma de las bases imponibles de los detalles de la retención", "No concuerda con el total de la factura.");
			 return;
		 }
		 
		// Código del estado de la factura seleccionada
		int estadoRet = 0;
		try { estadoRet = Integer.valueOf(tab_retencion.getValor("ide_coest")); }
		catch(Exception ex){}
		// Estados de la Retencion: 2 - Emitido

		// Solo se autorizan las retenciones emitidas
		if (estadoRet == 2 ) {
			// Limpiando el grid existente
			retencion_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 2 columnas para el grid existente
			retencion_elec_dialogo.getGri_cuerpo().setColumns(2);

			// Agregando una etiqueta vacía
			retencion_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));

			// Agregando una etiqueta con la información de la confirmación
			Etiqueta preguntaConfirmacion = new Etiqueta(
					"¿Desea autorizar la siguiente retención electrónica en el SRI?");
			preguntaConfirmacion
					.setEstiloContenido("font-size:15px;text-decoration: none;color:black;border-width: 0px");

			retencion_elec_dialogo.getGri_cuerpo().getChildren()
					.add(preguntaConfirmacion);

			// Etiqueta con Estilos Ambiente
			Etiqueta etiqueta1 = new Etiqueta("Ambiente: ");
			etiqueta1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta1);

			// Valor con Estilos
			Etiqueta valor1;
			
			if(ambiente==1)
				valor1 = new Etiqueta("PRUEBAS");
			else
				valor1 = new Etiqueta("PRODUCCION");
			
			valor1.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:green;border-width: 0px");

			// Agregando el valor del campo
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(valor1);
			
			// Etiqueta con Estilos Secuencial
			Etiqueta etiqueta = new Etiqueta("Secuencial: ");
			etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");

			// Agregando la etiqueta
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Valor con Estilos
			Etiqueta valor = new Etiqueta(tab_retencion.getValor("num_retencion_teret"));
			valor.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red;border-width: 0px");

			// Agregando el valor del campo
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(valor);

			// Agregando la función al botón aceptar
			retencion_elec_dialogo.getBot_aceptar().setMetodo("aceptarDialogoRetElectronica");
		} else {
			// Limpiando el grid existente
			retencion_elec_dialogo.getGri_cuerpo().getChildren().clear();

			// Configurando 1 columna para el grid existente
			retencion_elec_dialogo.getGri_cuerpo().setColumns(1);

			// Etiqueta
			Etiqueta etiqueta;

			// Mostrando un mensaje con el estado de la retencion
			switch (estadoRet) {
			case 0:
				etiqueta = new Etiqueta("Seleccione una Retención");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			case 1:
				etiqueta = new Etiqueta("La Retención fue anulada");
				etiqueta.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:black;border-width: 0px");
				break;
			default:
				etiqueta = new Etiqueta();
				break;
			}

			// Etiqueta del título
			Etiqueta titulo = new Etiqueta("");
			titulo.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration:none; color:black; border-width: 0px");

			// Agregando las etiquetas dentro del grid
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(titulo);
			retencion_elec_dialogo.getGri_cuerpo().getChildren()
					.add(new Etiqueta(""));
			retencion_elec_dialogo.getGri_cuerpo().getChildren().add(etiqueta);

			// Agregando la función al botón aceptar
			retencion_elec_dialogo.getBot_aceptar().setMetodo(
					"cerrarDialogoRetElectronica");
		}

		// Dibujando en pantalla el diálogo
		retencion_elec_dialogo.dibujar();
	}

	 // Cierra la confirmación para emitirla facturación electrónica
	 public void cerrarDialogoRetElectronica() {
		// Cerrando el diálogo
		retencion_elec_dialogo.cerrar();
	 }

	 // Abre el diálogo con la respuesta del core de facturación (Factura
	 // Electrónica)
	 public void aceptarDialogoRetElectronica() {
		// Autorizando la factura en el SRI
		List<String> respuestaAutorizacion = new ArrayList<String>();

		String respuestaCabecera = "";
		String respuestaMensaje = "";

		try {
			respuestaAutorizacion = servicio.procesarRetencionElectronica(ambiente, tab_retencion.getValor("num_retencion_teret"),autorizar);
			respuestaCabecera = respuestaAutorizacion.get(0);			
			
			if(respuestaCabecera.toUpperCase().contains("AUTORIZADO"))
			{
				boolean existeClave=false;
				String clave_acceso=pckUtilidades.CConversion.CStr(respuestaAutorizacion.get(1)).toUpperCase();
				
				try {
					if(clave_acceso.contains("CLAVE DE ACCESO") || clave_acceso.contains("CLAVE ACCESO"))
					{
						String [] strResp=clave_acceso.split("-");
						String [] strRespCA=pckUtilidades.CConversion.CStr(strResp[clave_acceso.contains("AUTORIZACION")?6:1]).split(":");
						clave_acceso=pckUtilidades.CConversion.CStr(strRespCA[1]);
						existeClave=true;
					}
					
				}
				catch(Exception ex)
				{
					System.out.println("Retencion Autorizada clave acceso error: "+ex.getMessage());
				}
				
				System.out.println("Retencion Autorizada: "+tab_retencion.getValor("num_retencion_teret")+" clave_acceso: "+clave_acceso);
				
				tab_retencion.modificar(tab_retencion.getFilaActual());
				tab_retencion.setValor("autorizada_sri_teret","true");
				
				if(existeClave)
					tab_retencion.setValor("nro_autorizacion_sri_ret",clave_acceso);
				
				tab_retencion.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_adq_factura,tab_retencion,tab_detalle_retencion");
				
				tab_adq_factura.actualizar();
				tab_adq_factura.actualizarRelaciones();
				tab_retencion.actualizar();
				tab_retencion.actualizarRelaciones();
				tab_retencion.ejecutarSql();
				utilitario.addUpdate("tab_adq_factura,tab_retencion,tab_detalle_retencion");
			}

			for (String item : respuestaAutorizacion) {
				if (!item.contentEquals(respuestaCabecera)) {
					respuestaMensaje += " " + item + ".";
				}
			}

			respuestaMensaje.replaceAll("Recepcion: ", "");	
			System.out.println("respuestaMensaje: "+respuestaMensaje);
			
		} catch (Exception ex) {
			System.out.println("Error aceptarDialogoRetElectronica: "+ex.getMessage());
		}		

		// Limpiando el grid existente
		respuesta_core_dialogo.getGri_cuerpo().getChildren().clear();

		// Configurando 2 columnas para el grid existente
		respuesta_core_dialogo.getGri_cuerpo().setColumns(2);

		// Cabecera de la respuesta del core con Estilos
		Etiqueta cabecera = new Etiqueta(respuestaCabecera);
		cabecera.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: none;color:green;border-width: 0px");

		// Agregando la etiqueta
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(cabecera);

		// Mensaje del core con Estilos
		Etiqueta mensaje = new Etiqueta(respuestaMensaje);
		mensaje.setEstiloContenido("font-size:15px; border-width: 0px");

		// Ocultando el botón cancelar
		respuesta_core_dialogo.getBot_cancelar().setStyle(
				"width: 0px;height: 0px");

		// Agregando el mensaje de respuesta del core
		respuesta_core_dialogo.getGri_cuerpo().getChildren().add(mensaje);

		// Dibujando el mensaje de respuesta del core
		respuesta_core_dialogo.dibujar();

		// Cerrando el diálogo
		retencion_elec_dialogo.cerrar();
	}

	 // Respuesta del Core de Facturación Electrónica ---------------------------
	 // Cierra la respuesta del core de facturación
	 public void aceptarDialogoRespuestaCore() {
		// Cerrando el diálogo
		tab_retencion.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
		tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
		respuesta_core_dialogo.cerrar();
	 }
    //FIN Facturacion Electronica - AVACA
	 
	 
	 public void verRide() 
	 {
		List<String> respuestaRide = new ArrayList<String>();
		
		String nro_autorizacion= pckUtilidades.CConversion.CStr(tab_retencion.getValor("nro_autorizacion_sri_ret"));
		String nombreDoc="Retencion_"+pckUtilidades.CConversion.CStr(tab_retencion.getValor("num_retencion_teret"));

		try {
			if(nro_autorizacion.length()>5)
			{
				respuestaRide = utilitario.consumoRIDEJson( nro_autorizacion );
	
				if(respuestaRide.get(0).toUpperCase().contains("OK"))
				{
					vpdf_rideRet.setVisualizarPDF(respuestaRide.get(1), nombreDoc);			
					vpdf_rideRet.dibujar();
		            utilitario.addUpdate("vpdf_rideRet");
				}
			}
			else
			{
				utilitario.agregarMensajeInfo("Retención no disponible", "La retención electrónica no cuenta con un numero de autorización...");
			}

			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		

			
	}

	/////botones fin,siguiente,atras,ultimo.inicio
    @Override
    public void inicio() {
        // TODO Auto-generated method stub
    
        super.inicio();
        if(!tab_detalle_retencion.isFocus())
        	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
       // actualizaPantallas();
    }
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
      

        super.siguiente();
        if(!tab_detalle_retencion.isFocus())
        	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());

      //  actualizaPantallas();
    }
    @Override
    public void atras() {
        // TODO Auto-generated method stub
    
        super.atras();
        if(!tab_detalle_retencion.isFocus())
        	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());

        //actualizaPantallas();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        if(!tab_detalle_retencion.isFocus())
        	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());

        //actualizaPantallas();
    }
    
    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
        tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
        //actualizaPantallas();
    }
    
    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
        tab_retencion.ejecutarValorForanea(tab_adq_factura.getValorSeleccionado());
        tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
    }

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_retencion.getTotalFilas()>0)
		{
			if(tab_retencion.getValor("autorizada_sri_teret").equals("true"))
			{
				utilitario.agregarMensajeInfo("Registro no Editable", "La retención electrónica fue autorizada por el SRI");
				return;
			}
		}
		
		if (tab_adq_factura.guardar()) 
		{
            if(tab_retencion.guardar()){
                if(tab_detalle_retencion.guardar()){
                    guardarPantalla();
                }
            }
		}
		
		if(tab_retencion.getTotalFilas()>0)
			if(tab_retencion.isFocus())
				if(ser_Adquisicion.actualizarNumeroRetencion(tab_retencion.getValor("ide_teret")).length()>3)
				{
					tab_retencion.actualizar();
					tab_retencion.ejecutarSql();
				}
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		try
		{
			if(tab_retencion.getValor("autorizada_sri_teret").equals("true"))
			{
				utilitario.agregarMensajeInfo("Registro no Editable", "La retención electrónica fue autorizada por el SRI");
				return;
			}
			else
				utilitario.agregarMensajeInfo("Registro no Editable", "No puede eliminar registros");
		}
		catch(Exception ex)
		{
			System.out.println("Retencion eliminar error: "+ex.getMessage());
			utilitario.agregarMensajeError("Retencion Error", ex.getMessage());
			
		}
		utilitario.getTablaisFocus().eliminar();
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

	public Tabla getTab_adq_factura() {
		return tab_adq_factura;
	}

	public void setTab_adq_factura(Tabla tab_adq_factura) {
		this.tab_adq_factura = tab_adq_factura;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

}
