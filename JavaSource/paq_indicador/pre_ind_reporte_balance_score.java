/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_reporte_balance_score extends Pantalla {
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

    private Tabla tab_balance_score_reporte = new Tabla();
    private Tabla tab_balance_score_reporte_archivo_adjunto = new Tabla();

	private Combo com_anio=new Combo();
	private Combo com_mes=new Combo();
	private Combo com_area=new Combo();
	
	private Confirmar con_guardar = new Confirmar();
	String carpeta="indicador";
	String area="",anio="",mes="",ide_gtempxx="",empleado="",jefe_inmediato_planificacion="",tipo_perfil="",ide_geare;
;
    public pre_ind_reporte_balance_score() {

		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	TablaGenerica tabJefeInmediato=null;
    	
    	tabJefeInmediato=utilitario.consultar("SELECT usu.ide_indus, usu.ide_gtemp, area.ide_geare,area.detalle_geare, usu.tipo_indus, usu.activo_indus "
    			+ "FROM ind_usuario usu "
    			+ "left join gen_area area on area.ide_geare=usu.ide_geare "
    			+ "where usu.ide_gtemp="+ide_gtempxx);
    	
    	if (tabJefeInmediato.getValor("tipo_indus")==null || tabJefeInmediato.getValor("tipo_indus").equals("") || tabJefeInmediato.getValor("tipo_indus").isEmpty()) {
    		utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		return;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_indus");
        	area=tabJefeInmediato.getValor("ide_geare");
		}
    	 	
    jefe_inmediato_planificacion=tabJefeInmediato.getValor("ide_indus");
    ide_geare=tabJefeInmediato.getValor("ide_geare");
	    	
    	if (tipo_perfil.equals("1")) {
    		com_area.setCombo("select ide_geare,abreviatura_geare  || ' ' || detalle_geare  from gen_area where activo_geare=true");
        	com_area.setMetodo("seleccionaElArea");
        	com_area.setStyle("width: 100px; margin: 0 0 -8px 0;");
    		bar_botones.agregarComponente(new Etiqueta("Seleccione El Area:"));
    		bar_botones.agregarComponente(com_area);	
		}
    	
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Anio:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes.setCombo(getMes());
		com_mes.setMetodo("seleccionaMes");
		com_mes.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes:"));
		bar_botones.agregarComponente(com_mes);
    	
		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		con_guardar.setTitle("CONFIRMACION REGISTRO DE DATOS");
		con_guardar.setMessage("¿ESTA SEGURO DE GUARDAR?. LA INFORMACION REGISTRADA NO PODRA SER MODIFICADA");
		con_guardar.getBot_aceptar().setMetodo("guardarDatosIndicador");
		con_guardar.getBot_cancelar().setMetodo("cancelarDatosIndicador");

		agregarComponente(con_guardar);
		
    	if (tipo_perfil.equals("1")) {
		Boton bot_aplica_vacacion=new Boton();
		bot_aplica_vacacion.setValue("REPORTE GENERAL");
		bot_aplica_vacacion.setMetodo("obtenerDatos");
	//	bar_botones.agregarBoton(bot_aplica_vacacion); 
    	}
		
    	tab_balance_score_reporte.setId("tab_balance_score_reporte");
    	tab_balance_score_reporte.setNumeroTabla(1);
    	
    	tab_balance_score_reporte.setTabla("ind_balance_score_card", "ide_inbsc", 1);
    	tab_balance_score_reporte.getColumna("ide_inbsc").setNombreVisual("CODIGO");
    	tab_balance_score_reporte.getColumna("ide_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inbsc").setOrden(1);

    	
     	if (tipo_perfil.equals("1")) {
    	tab_balance_score_reporte.getColumna("ide_inind").setCombo("SELECT ide_inind, detalle_inind  "
    			+ "FROM ind_indicador  "
    			+ "WHERE activo_inind=true");
     	}else {
     	 	tab_balance_score_reporte.getColumna("ide_inind").setCombo("SELECT ind.ide_inind,ind.detalle_inind  "
     	 			+ "FROM ind_balance_score_card_parametro  rbsc "
     	 			+ "left join ind_indicador ind on ind.ide_inind=rbsc.ide_inind "
     	 			+ "where ide_geare="+ide_geare);
		}
    	
    	
    	tab_balance_score_reporte.getColumna("ide_inind").setNombreVisual("INDICADORES");
    	tab_balance_score_reporte.getColumna("ide_inind").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inind").setMetodoChange("cargarDatos");
    	tab_balance_score_reporte.getColumna("ide_inind").setOrden(2);

    	
    	   	
    	tab_balance_score_reporte.getColumna("ide_inper").setCombo("SELECT ide_inper, detalle_inper  "
    			+ "FROM ind_perspectiva  "
    			+ "WHERE activo_inper=true ");
    	tab_balance_score_reporte.getColumna("ide_inper").setAutoCompletar(); 	
    	tab_balance_score_reporte.getColumna("ide_inper").setNombreVisual("PERSPECTIVA");
    	tab_balance_score_reporte.getColumna("ide_inper").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inper").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inper").setOrden(3);

    	
    	tab_balance_score_reporte.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare  "
    			+ "FROM gen_area  "
    			+ "WHERE activo_geare=true ");
    	tab_balance_score_reporte.getColumna("ide_geare").setAutoCompletar(); 	
    	tab_balance_score_reporte.getColumna("ide_geare").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_geare").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_geare").setNombreVisual("AREA");
    	tab_balance_score_reporte.getColumna("ide_geare").setOrden(4);

    	
    	tab_balance_score_reporte.getColumna("ide_indus").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_indus").setNombreVisual("USURIO RESPONSABLE");
    	tab_balance_score_reporte.getColumna("ide_indus").setCombo("SELECT ide_indus,   "
    			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
    			+ "FROM ind_usuario DUS "
    			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=DUS.IDE_GTEMP");   	

    	tab_balance_score_reporte.getColumna("ide_indus").setAutoCompletar();
     	tab_balance_score_reporte.getColumna("ide_indus").setOrden(5);

     	tab_balance_score_reporte.getColumna("ide_inobj").setCombo("SELECT ide_inobj, detalle_inobj  "
    			+ "FROM ind_objetivo  "
    			+ "WHERE activo_inobj=true ");
     	

     	
     	
    	tab_balance_score_reporte.getColumna("ide_inobj").setAutoCompletar();
    	tab_balance_score_reporte.getColumna("ide_inobj").setNombreVisual("OBJETIVO");
    	tab_balance_score_reporte.getColumna("ide_inobj").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inobj").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inobj").setOrden(6);

    	
     	
     	
     	tab_balance_score_reporte.getColumna("ide_inesa").setCombo("SELECT ide_inesa, detalle_inesa  "
    			+ "FROM ind_estrategias_actuales  "
    			+ "WHERE activo_inesa=true ");
     	tab_balance_score_reporte.getColumna("ide_inesa").setAutoCompletar();
    	tab_balance_score_reporte.getColumna("ide_inesa").setNombreVisual("ESTRATEGIAS ACTUALES");
    	tab_balance_score_reporte.getColumna("ide_inesa").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inesa").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inesa").setOrden(7);

    	



    	   	
    	tab_balance_score_reporte.getColumna("ide_inmec").setCombo("SELECT ide_inmec, detalle_inmec  "
    			+ "FROM ind_metodo_calculo  "
    			+ "WHERE activo_inmec=true ");
    	
    	tab_balance_score_reporte.getColumna("ide_inmec").setNombreVisual("METODO DE CALCULO");
    	tab_balance_score_reporte.getColumna("ide_inmec").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inmec").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inmec").setAutoCompletar(); 	
    	tab_balance_score_reporte.getColumna("ide_inmec").setOrden(8);
    	
    	
    	
       	tab_balance_score_reporte.getColumna("ide_inobi").setCombo("SELECT ide_inobi, detalle_inobi  "
    			+ "FROM ind_objetivo_indicador  "
    			+ "WHERE activo_inobi=true ");
    	
    	tab_balance_score_reporte.getColumna("ide_inobi").setNombreVisual("OBJETIVO INDICADOR");
    	tab_balance_score_reporte.getColumna("ide_inobi").setAutoCompletar();
    	tab_balance_score_reporte.getColumna("ide_inobi").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inobi").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inobi").setOrden(9);
    	
    	
    	tab_balance_score_reporte.getColumna("ind_indes").setCombo("SELECT ind_indes, detalle_indes  "
    			+ "FROM ind_desarrolla  "
    			+ "WHERE activo_indes=true ");
    	
    	tab_balance_score_reporte.getColumna("ind_indes").setNombreVisual("OBJETIVO DEL  INDICADOR");
    	tab_balance_score_reporte.getColumna("ind_indes").setRequerida(false);
    	tab_balance_score_reporte.getColumna("ind_indes").setLectura(true);
    	tab_balance_score_reporte.getColumna("ind_indes").setOrden(11);
    	tab_balance_score_reporte.getColumna("ind_indes").setVisible(false);

    	
    	/*tab_balance_score_reporte.getColumna("ind_inrec").setCombo("SELECT ind_inrec, detalle_inrec  "
    			+ "FROM ind_recursos  "
    			+ "WHERE activo_inrec=true ");*/
    	
    	tab_balance_score_reporte.getColumna("recursos_inbsc").setNombreVisual("RECURSOS");
    	tab_balance_score_reporte.getColumna("recursos_inbsc").setRequerida(true);
    	tab_balance_score_reporte.getColumna("recursos_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("recursos_inbsc").setOrden(10);
    	tab_balance_score_reporte.getColumna("ind_inrec").setVisible(false);;

    	tab_balance_score_reporte.getColumna("ind_inrec").setRequerida(false);


    	//substring(detalle_inobi,1,60) 
 





    	tab_balance_score_reporte.getColumna("ide_infre").setCombo("SELECT ide_infre, detalle_infre  "
    			+ "FROM ind_frecuencia  "
    			+ "WHERE activo_infre=true ");
    	
      	tab_balance_score_reporte.getColumna("fecha_periocidad_inbsc").setNombreVisual("FECHA PERIOCIDAD");
    	tab_balance_score_reporte.getColumna("fecha_periocidad_inbsc").setLectura(true);
    	if (tipo_perfil.equals("1")) {
        	tab_balance_score_reporte.getColumna("fecha_periocidad_inbsc").setLectura(false);
    	}else {
        	tab_balance_score_reporte.getColumna("fecha_periocidad_inbsc").setLectura(true);
		}
    	
    	tab_balance_score_reporte.getColumna("fecha_periocidad_inbsc").setOrden(12);

    	
      	tab_balance_score_reporte.getColumna("ide_infre").setNombreVisual("FRECUENCIA");
    	tab_balance_score_reporte.getColumna("ide_infre").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_infre").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_infre").setAutoCompletar(); 	
    	tab_balance_score_reporte.getColumna("ide_infre").setOrden(13);

     	
    	tab_balance_score_reporte.getColumna("ide_inmed").setCombo("SELECT ide_inmed, detalle_inmed  "
    			+ "FROM ind_medida  "
    			+ "WHERE activo_inmed=true ");
    	
      	tab_balance_score_reporte.getColumna("ide_inmed").setNombreVisual("MEDIDA");
    	tab_balance_score_reporte.getColumna("ide_inmed").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inmed").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_inmed").setOrden(14);


   	
     	tab_balance_score_reporte.getColumna("ide_inana").setCombo("SELECT ind_inana, detalle_inana FROM ind_analisis WHERE activo_inana=true");
      	tab_balance_score_reporte.getColumna("ide_inana").setNombreVisual("ANALISIS");
    	//tab_balance_score_reporte.getColumna("ide_inana").setRequerida(true);
    	tab_balance_score_reporte.getColumna("ide_inana").setOrden(15);
     	tab_balance_score_reporte.getColumna("observacion_analisis_inbsc").setNombreVisual("OBSERVACION ANALISIS");
    	tab_balance_score_reporte.getColumna("observacion_analisis_inbsc").setOrden(16);
    	
      	tab_balance_score_reporte.getColumna("mes_porcentaje").setNombreVisual("MES");
      	tab_balance_score_reporte.getColumna("mes_porcentaje").setVisible(false);

    	tab_balance_score_reporte.getColumna("meta_inbsc").setNombreVisual("META");
    	tab_balance_score_reporte.getColumna("meta_inbsc").setFormatoNumero(2);
    	tab_balance_score_reporte.getColumna("meta_inbsc").setMetodoChange("calacularMeta");
    	if (tipo_perfil.equals("1")) {
        	tab_balance_score_reporte.getColumna("meta_inbsc").setLectura(false);
    	}else {
        	tab_balance_score_reporte.getColumna("meta_inbsc").setLectura(true);
		}
    	tab_balance_score_reporte.getColumna("meta_inbsc").setOrden(17);

    	
    	
      	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setNombreVisual("EJECUTADO");
    	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setFormatoNumero(2);
    	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setMetodoChange("calacularMeta");
      	if (tipo_perfil.equals("1")) {
        	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setLectura(true);
        	
    	}else {
        	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setLectura(false);
          	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setRequerida(true);


		}
    	tab_balance_score_reporte.getColumna("ejecutado_inbsc").setOrden(18);

      	
      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setNombreVisual("EVALUADO");
      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("eveluado_inbsc").setOrden(19);
     	
    	
    	tab_balance_score_reporte.getColumna("semaforo_inbsc").setNombreVisual("SEMÁFORO");
      	tab_balance_score_reporte.getColumna("semaforo_inbsc").setEtiqueta();
      	tab_balance_score_reporte.getColumna("semaforo_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("semaforo_inbsc").setOrden(20);
    	
    	
    	tab_balance_score_reporte.getColumna("adjunto_inbsc").setUpload(carpeta);
    	tab_balance_score_reporte.getColumna("adjunto_inbsc").setNombreVisual("DOCUMENTO ADJUNTO");
    	tab_balance_score_reporte.getColumna("adjunto_inbsc").setOrden(21);
    	tab_balance_score_reporte.getColumna("adjunto_inbsc").setColumnaNombreArchivo("nombre_archivo_inbsc");


		tab_balance_score_reporte.getColumna("nombre_archivo_inbsc").setLectura(true);
		tab_balance_score_reporte.getColumna("nombre_archivo_inbsc").setNombreVisual("NOMBRE_ADJUNTO");
		tab_balance_score_reporte.getColumna("nombre_archivo_inbsc").setValorDefecto("sin nombre");
		tab_balance_score_reporte.getColumna("nombre_archivo_inbsc").setEtiqueta();
		tab_balance_score_reporte.getColumna("nombre_archivo_inbsc").setOrden(22);

   
    	tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setNombreVisual("FECHA_REGISTRO");
    	//tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setValorDefecto(utilitario.getFechaActual());
        tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setOrden(23);

    	
    	
    	
     	tab_balance_score_reporte.getColumna("observacion_inbsc").setNombreVisual("OBSERVACION");
    	tab_balance_score_reporte.getColumna("observacion_inbsc").setOrden(24);

      	tab_balance_score_reporte.getColumna("plan_accion_inbsc").setNombreVisual("PLAN ACCION");
    	tab_balance_score_reporte.getColumna("plan_accion_inbsc").setOrden(25);

      	
      	
      	tab_balance_score_reporte.getColumna("evaluacion_negativa_inbsc").setNombreVisual("EVALUADO NEGATIVA");
    	tab_balance_score_reporte.getColumna("evaluacion_negativa_inbsc").setOrden(26);

      	
      	tab_balance_score_reporte.getColumna("evaluacion_positiva_inbsc").setNombreVisual("EVALUADO POSITIVO");	
    	tab_balance_score_reporte.getColumna("evaluacion_positiva_inbsc").setOrden(27);

      	
    	tab_balance_score_reporte.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani  "
    			+ "FROM gen_anio  ");
    	tab_balance_score_reporte.getColumna("ide_geani").setAutoCompletar();
    	tab_balance_score_reporte.getColumna("ide_geani").setLectura(true);
    	tab_balance_score_reporte.getColumna("ide_geani").setNombreVisual("ANIO");
    	tab_balance_score_reporte.getColumna("ide_geani").setVisible(false);

 
    	tab_balance_score_reporte.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes where ide_gemes not in  (13) order by ide_gemes");   	
    	tab_balance_score_reporte.getColumna("ide_gemes").setAutoCompletar();
    	tab_balance_score_reporte.getColumna("ide_gemes").setLectura(true);
    	//tab_balance_score_reporte.getColumna("ide_gemes").setVisible(true);
    	tab_balance_score_reporte.getColumna("ide_gemes").setVisible(false);



    	tab_balance_score_reporte.getColumna("modifica_inbsc").setNombreVisual("REG PLAN DE ACCION");
    	tab_balance_score_reporte.getColumna("modifica_inbsc").setCheck();
    	tab_balance_score_reporte.getColumna("modifica_inbsc").setLectura(true);
    	tab_balance_score_reporte.getColumna("modifica_inbsc").setOrden(28);

    	
    	tab_balance_score_reporte.getColumna("activo_inbsc").setCheck();
    	tab_balance_score_reporte.getColumna("activo_inbsc").setValorDefecto("TRUE");
    	tab_balance_score_reporte.getColumna("activo_inbsc").setNombreVisual("ACTIVO");
    	tab_balance_score_reporte.getColumna("activo_inbsc").setOrden(29);
       	tab_balance_score_reporte.getGrid().setColumns(2);
    	tab_balance_score_reporte.setTipoFormulario(true);
    	
    	
    	
    	//tab_balance_score_reporte.agregarRelacion(tab_balance_score_reporte_archivo_adjunto);

    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
    	tab_balance_score_reporte.setCampoOrden("ide_inbsc asc");
 
    	tab_balance_score_reporte.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_balance_score_reporte);
        pat_panel.setMensajeWarn("REGISTRO DE INDICADORES "+tabJefeInmediato.getValor("detalle_geare").toUpperCase());

        
        tab_balance_score_reporte_archivo_adjunto.setId("tab_balance_score_reporte_archivo_adjunto");
        tab_balance_score_reporte_archivo_adjunto.setNumeroTabla(2);
    	
        tab_balance_score_reporte_archivo_adjunto.setTabla("ind_archivo", "ide_inarc", 2);
        tab_balance_score_reporte_archivo_adjunto.getColumna("ide_inarc").setNombreVisual("CODIGO");
        tab_balance_score_reporte_archivo_adjunto.getColumna("ide_inarc").setLectura(true);

    	
		tab_balance_score_reporte_archivo_adjunto.getColumna("adjunto_inarc").setUpload("indicador");
        tab_balance_score_reporte_archivo_adjunto.getColumna("adjunto_inarc").setNombreVisual("DOCUMENTO ADJUNTO");

        tab_balance_score_reporte_archivo_adjunto.getColumna("fecha_inarc").setNombreVisual("FECHA_REGISTRO");
        tab_balance_score_reporte_archivo_adjunto.getColumna("fecha_inarc").setValorDefecto(utilitario.getFechaActual());
        tab_balance_score_reporte_archivo_adjunto.getColumna("fecha_inarc").setLectura(true);

        
        tab_balance_score_reporte_archivo_adjunto.getColumna("observaciones_inarc").setNombreVisual("OBSERVACIONES");

        
    	
        tab_balance_score_reporte_archivo_adjunto.getColumna("activo_inarc").setCheck();
        tab_balance_score_reporte_archivo_adjunto.getColumna("activo_inarc").setValorDefecto("true");
        tab_balance_score_reporte_archivo_adjunto.getColumna("activo_inarc").setNombreVisual("ACTIVO");

    	
        tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inarc=-1");
        tab_balance_score_reporte_archivo_adjunto.dibujar();
        
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_balance_score_reporte_archivo_adjunto);
        pat_panel2.setMensajeWarn("REGISTRO DE ARCHIVOS");

         Division div_division = new Division();
        div_division.setId("div_division");
		//div_division.dividir2(pat_panel, pat_panel2, "70%", "H");
        
       div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
		if (tab_balance_score_reporte.isFocus()){

    	if(com_anio.getValue()==null || com_mes.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar parametros de busqueda");
	    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
			return;
			
			}else {
		    	tab_balance_score_reporte.insertar();
		    	tab_balance_score_reporte.setValor("ide_geani",""+com_anio.getValue());
		    	tab_balance_score_reporte.setValor("ide_gemes",""+com_mes.getValue());
		    	tab_balance_score_reporte.setValor("ide_geare",ide_geare);
		    	tab_balance_score_reporte.setValor("ide_indus",jefe_inmediato_planificacion);
		    	tab_balance_score_reporte.setValor("ide_inana","1");

		    	
		    	TablaGenerica tab_anio=utilitario.consultar("Select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
		    	String anio=tab_anio.getValor("detalle_geani");
		    	String fec_="";
		    	
		    	
		    	if (Integer.parseInt(com_mes.getValue().toString())<10) {
		    		fec_=anio+"-0"+com_mes.getValue()+"-01";
			    	tab_balance_score_reporte.setValor("fecha_periocidad_inbsc",fec_);
				}else {
					fec_=anio+"-"+com_mes.getValue()+"-01";
			    	tab_balance_score_reporte.setValor("fecha_periocidad_inbsc",fec_);

				}

			}
		}else if (tab_balance_score_reporte_archivo_adjunto.isFocus()){
	    	if (tab_balance_score_reporte.getTotalFilas()>0) {
	    		if (tab_balance_score_reporte.getValor("ide_inbsc")==null || tab_balance_score_reporte.getValor("ide_inbsc").isEmpty() || tab_balance_score_reporte.getValor("ide_inbsc").equals("")) {
	    			utilitario.agregarMensaje("No se puede insertar", "Debe guardar el indicador");
					return;
				}
	    		
	    		tab_balance_score_reporte_archivo_adjunto.insertar();
	    		tab_balance_score_reporte_archivo_adjunto.setValor("ide_inbsc",tab_balance_score_reporte.getValor("ide_inbsc"));

			}else {
				utilitario.agregarMensaje("No se puede insertar", "Debe seleccionar un indicador o registro válido");
				return;
			}
			

		}
    	
    }

    @Override
    public void guardar() {
        if (tab_balance_score_reporte.guardar()) {
		String eveluado_inbsc="";
		
		if (tipo_perfil.equals("1")) {
			guardarPantalla();
		}else{
			
			if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"modifica_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"modifica_inbsc").equals("")
					|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"modifica_inbsc").isEmpty()  || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"modifica_inbsc").equals("false")) {
			
			}else {
				utilitario.agregarMensaje("No se puede guardar", "El registro no puede ser actualizado");
				return;
			}	
			
			
	if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"ide_inana")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"ide_inana").equals("")
			|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"ide_inana").isEmpty()) {
		utilitario.agregarMensaje("No se puede continuar", "Campo analisis vacio");
		return;
	}	
			
	if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"ide_inana").equals("2")) {
		if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_analisis_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_analisis_inbsc").equals("")
				|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_analisis_inbsc").isEmpty()) {
			utilitario.agregarMensaje("No se puede continuar", "Campo observacion Análisis vacio");
			return;				
			}else {

			}	
	}	
	
	if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"ide_inana").equals("1")) {
    	tab_balance_score_reporte.setValor("observacion_analisis_inbsc","");
    	utilitario.addUpdateTabla(tab_balance_score_reporte,"observacion_analisis_inbsc","");
	}

			
			
		if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc").equals("")
				|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc").isEmpty()) {
			utilitario.agregarMensaje("No se puede guardar", "Campo evaluado sin datos");
			return;
		}else {
		
			
			double evaluado=0.00;
			evaluado=Double.parseDouble(tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc"));
			if (evaluado<96.00) {				  
				  if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_inbsc").equals("")
					|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_inbsc").isEmpty()) {		
				utilitario.agregarMensaje("No se puede guardar", "Campo observacion sin datos");
				return;
		}else {
			if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"observacion_inbsc").length()<35) {
				utilitario.agregarMensaje("No se puede guardar", "Debe registrar minimo 35 caracteres");
				return;
			}else {
				
			}
		}
				
				if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"plan_accion_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"plan_accion_inbsc").equals("")
						|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"plan_accion_inbsc").isEmpty()) {		
					utilitario.agregarMensaje("No se puede guardar", "Campo plan accion sin datos");
					return;
			}else {
				if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"plan_accion_inbsc").length()<35) {
					utilitario.agregarMensaje("No se puede guardar", "Debe registrar minimo 35 caracteres");
					return;
				}else {
					
				}
			}
				
				if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc").equals("")
						|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc").isEmpty()) {		
					utilitario.agregarMensaje("No se puede guardar", "Debe registrar el documento adjunto");
					return;}
				
			}else {
				
				
				if (tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc")==null || tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc").equals("")
				|| tab_balance_score_reporte.getValor(tab_balance_score_reporte.getFilaActual(),"adjunto_inbsc").isEmpty()) {		
			utilitario.agregarMensaje("No se puede guardar", "Debe registrar el documento adjunto");
			return;
				}
				
				
			}   
			

			con_guardar.dibujar();
			
			
			
			
           // guardarPantalla();
        } 
		
		 //if (tab_balance_score_reporte_archivo_adjunto.guardar()) {
	       //     guardarPantalla();
	        //}
		
        }
        
        }
       
    }

    @Override
    public void eliminar() {
    //	tab_balance_score_reporte.eliminar();
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
    }



	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			anio=com_anio.getValue().toString();
			if(com_mes.getValue()!=null){
				if (tipo_perfil.equals("1")) {
					if(com_area.getValue()!=null){
						String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
								+ "FROM ind_balance_score_card "
								+ "where ide_geani="+com_anio.getValue()+" ";
					    		sql+= "and ide_geare="+com_area.getValue()+" ";	
								sql+= "and ide_gemes="+com_mes.getValue()+" "
								+ "order by ide_inbsc asc";
								//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

								TablaGenerica tab_respuesta=utilitario.consultar(sql);		
								if (tab_respuesta.getTotalFilas()>0) {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+com_area.getValue());
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
								}else {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+com_area.getValue());
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
								}
						
						
						
						
					}else{
						
						String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
								+ "FROM ind_balance_score_card "
								+ "where ide_geani="+com_anio.getValue()+" ";
					    		//sql+= "and ide_geare="+com_area.getValue()+" ";	
								sql+= "and ide_gemes="+com_mes.getValue()+" "
								+ "order by ide_inbsc asc";
								//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

								TablaGenerica tab_respuesta=utilitario.consultar(sql);		
								if (tab_respuesta.getTotalFilas()>0) {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());//+" and  ide_geare="+com_area.getValue()	
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
								}else {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());//+" and  ide_geare="+com_area.getValue()	
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
								}
						
					//tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());//+" and  ide_geare="+com_area.getValue()	
					//tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_balance_score_reporte.getValor("ide_inbsc"));	
					}
				}else {
					tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+ide_geare);
					String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
							+ "FROM ind_balance_score_card "
							+ "where ide_geani="+com_anio.getValue()+" ";
				    		sql+= "and ide_geare="+ide_geare+" ";	
							sql+= "and ide_gemes="+com_mes.getValue()+" "
							+ "order by ide_inbsc asc";
							//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

							TablaGenerica tab_respuesta=utilitario.consultar(sql);		
							if (tab_respuesta.getTotalFilas()>0) {
								tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
								//actualizarTabla2();
							}else {
								tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
							}	
				}
				
				tab_balance_score_reporte.ejecutarSql();
				tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
			}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga mes");
		    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
				tab_balance_score_reporte.ejecutarSql();
				tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
				tab_balance_score_reporte_archivo_adjunto.ejecutarSql();

				return;		
			}
			
			
	
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga anio ");
	    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
					tab_balance_score_reporte.ejecutarSql();
					tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
					tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
			return;			
		}
			
			
	}
	
	

	public void seleccionaMes(){
		if(com_mes.getValue()!=null){
			mes=com_mes.getValue().toString();
			//if(com_mes.getValue()!=null){
				if (tipo_perfil.equals("1")) {
					if(com_area.getValue()!=null){
						if(com_anio.getValue()!=null){
						String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
								+ "FROM ind_balance_score_card "
								+ "where ide_geani="+com_anio.getValue()+" ";
					    		sql+= "and ide_geare="+com_area.getValue()+" ";	
								sql+= "and ide_gemes="+com_mes.getValue()+" "
								+ "order by ide_inbsc asc";
								//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

								TablaGenerica tab_respuesta=utilitario.consultar(sql);		
								if (tab_respuesta.getTotalFilas()>0) {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+com_area.getValue());
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));
								}else {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+com_area.getValue());
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
								}
						}else {
							tab_balance_score_reporte.setCondicion("IDE_GEANI=-1");
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
						}	
						
					}else{
						if(com_anio.getValue()!=null){
						String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
								+ "FROM ind_balance_score_card "
								+ "where ide_geani="+com_anio.getValue()+" ";
					    		//sql+= "and ide_geare="+ide_geare+" ";	
								sql+= "and ide_gemes="+com_mes.getValue()+" "
								+ "order by ide_inbsc asc";
								//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

								TablaGenerica tab_respuesta=utilitario.consultar(sql);		
								if (tab_respuesta.getTotalFilas()>0) {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());//+" and  ide_geare="+com_area.getValue(
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));		
								}else {
									tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());//+" and  ide_geare="+com_area.getValue(
									tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");									
								}
						}else {
							tab_balance_score_reporte.setCondicion("IDE_GEANI=-1");
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
						}

					}
				}else {
					
					if(com_anio.getValue()!=null){
					tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+ide_geare);
					String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
							+ "FROM ind_balance_score_card "
							+ "where ide_geani="+com_anio.getValue()+" ";
				    		sql+= "and ide_geare="+ide_geare+" ";	
							sql+= "and ide_gemes="+com_mes.getValue()+" "
							+ "order by ide_inbsc asc";
							//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

							TablaGenerica tab_respuesta=utilitario.consultar(sql);		
							if (tab_respuesta.getTotalFilas()>0) {
								//tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue()+" and  ide_geare="+ide_geare);
								tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
								//actualizarTabla2();
							}else {
								tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
							}	

				}else {
					tab_balance_score_reporte.setCondicion("IDE_GEANI=-1");
					tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");
				}

				}
				
				tab_balance_score_reporte.ejecutarSql();
				tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
			/*}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga mes");
		    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
				tab_balance_score_reporte.ejecutarSql();
				tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
				tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
				return;		
			}*/
			
			
	
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga mes");
	    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
					tab_balance_score_reporte.ejecutarSql();
					tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
					tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
			return;			
		}
			
			
	}
	
	
	
	public void seleccionaElArea (){
		if(com_area.getValue()!=null){
			area=com_area.getValue().toString();
			if(com_anio.getValue()!=null && com_mes.getValue()==null){
				String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
						+ "FROM ind_balance_score_card "
						+ "where  ide_geani="+com_anio.getValue()+" ";
			    		sql+= "and ide_geare="+com_area.getValue()+" ";	
						//sql+= "and ide_gemes="+com_mes.getValue()+" "
			    		sql+= "order by ide_inbsc asc";
						//+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

						TablaGenerica tab_respuesta=utilitario.consultar(sql);		
						if (tab_respuesta.getTotalFilas()>0) {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and  ide_geare="+com_area.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
						}else {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and  ide_geare="+com_area.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
						}
				
		
				
			}else if(com_anio.getValue()!=null && com_mes.getValue()!=null){
				
				String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
						+ "FROM ind_balance_score_card "
						+ "where  ide_geani="+com_anio.getValue()+" ";
			    		sql+= "and ide_geare="+com_area.getValue()+" ";	
						sql+= "and ide_gemes="+com_mes.getValue()+" ";
			    		sql+= "order by ide_inbsc asc";
						TablaGenerica tab_respuesta=utilitario.consultar(sql);		
						if (tab_respuesta.getTotalFilas()>0) {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and  ide_geare="+com_area.getValue()+" and ide_gemes="+com_mes.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();

						}else {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and  ide_geare="+com_area.getValue()+" and ide_gemes="+com_mes.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
						}
				
				
			
			}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "");
		    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
				return;		
			}
	
		}else{
			if(com_anio.getValue()!=null && com_mes.getValue()!=null){
				
				String sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
						+ "FROM ind_balance_score_card "
						+ "where  ide_geani="+com_anio.getValue()+" ";
			    		//sql+= "and ide_geare="+com_area.getValue()+" ";	
						sql+= "and ide_gemes="+com_mes.getValue()+" ";
			    		sql+= "order by ide_inbsc asc";
						TablaGenerica tab_respuesta=utilitario.consultar(sql);		
						if (tab_respuesta.getTotalFilas()>0) {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+"  and ide_gemes="+com_mes.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_respuesta.getValor("ide_inbsc"));	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
						}else {
							tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+"  and ide_gemes="+com_mes.getValue());	
							tab_balance_score_reporte.ejecutarSql();
							tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc=-1");	
							tab_balance_score_reporte_archivo_adjunto.ejecutarSql();
						}

			}else {
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga area ");
	    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
			return;
			}
		}
			
			
	}
	
	
	

	
	public void cargarDatos(AjaxBehaviorEvent evt){
		
		String ide_setre="",fecha_desde_seres="",fecha_hasta_seres="",mecanismo_reporte_seres="",fecha_actual="",sql="",fecha_periocidad_anterior="";
		int estadoRespuesta=0;
		boolean bandReg=false;
		tab_balance_score_reporte.modificar(evt);
		//if (tab_balance_score_reporte.getValor("ide_inbsc")==null || tab_balance_score_reporte.getValor("ide_inbsc").equals("")) {
			estadoRespuesta=0;
			sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
			+ "FROM ind_balance_score_card "
			+ "where ide_geani="+com_anio.getValue()+" ";
    	if (tipo_perfil.equals("1")) {
			sql+= "and ide_geare="+com_area.getValue()+" ";
			}else {
				sql+= "and ide_geare="+ide_geare+" ";	
			}			
    		sql+= "and ide_gemes="+com_mes.getValue()+" "
			+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

			TablaGenerica tab_respuesta=utilitario.consultar(sql);		
			if (tab_respuesta.getTotalFilas()>0) {
				String fecha_periocidad="",fecha_nueva="",fechaLimiteInicio="",fechaLimiteFin="";
				bandReg=false;
				fecha_periocidad_anterior="";
			if (tab_respuesta.getValor("fecha_periocidad_inbsc")==null || tab_respuesta.getValor("fecha_periocidad_inbsc").isEmpty() || tab_respuesta.getValor("fecha_periocidad_inbsc").equals("")) {
				utilitario.agregarMensaje("El indicador seleccionado", "Ya se encuentra registrado");
		    	tab_balance_score_reporte.setValor("ide_inobj","");
		    	tab_balance_score_reporte.setValor("ide_inesa","");
		    	tab_balance_score_reporte.setValor("ide_inmec","");
				tab_balance_score_reporte.setValor("ide_inobi","");
		    	tab_balance_score_reporte.setValor("ide_infre","");
		    	tab_balance_score_reporte.setValor("ide_inmed","");
		    //	tab_balance_score_reporte.setValor("meta_inbsc","");

		    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
				return;
			}
			
			if (tab_respuesta.getValor("ide_infre")==null || tab_respuesta.getValor("ide_infre").isEmpty() || tab_respuesta.getValor("ide_infre").equals("")) {
					utilitario.agregarMensaje("El indicador seleccionado", "Ya se encuentra registrado");
			    	tab_balance_score_reporte.setValor("ide_inobj","");
			    	tab_balance_score_reporte.setValor("ide_inesa","");
			    	tab_balance_score_reporte.setValor("ide_inmec","");
					tab_balance_score_reporte.setValor("ide_inobi","");
			    	tab_balance_score_reporte.setValor("ide_infre","");
			    	tab_balance_score_reporte.setValor("ide_inmed","");
			    //	tab_balance_score_reporte.setValor("meta_inbsc","");

			    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
					return;
				}else {
					
					/*TablaGenerica tab_frecuencia =utilitario.consultar("SELECT ide_infre, detalle_infre, activo_infre,multiplicador_infre "
							+ "FROM ind_frecuencia "
							+ "where ide_infre="+tab_respuesta.getValor("ide_infre"));
					int multiplicadoPeriocidad=0;
					multiplicadoPeriocidad=Integer.parseInt("multiplicador_infre");
					fecha_periocidad=tab_respuesta.getValor("fecha_periocidad_inbsc");
					fecha_nueva=utilitario.getAnio(tab_respuesta.getValor("fecha_periocidad_inbsc"))+"-"+utilitario.getAnio(tab_respuesta.getValor("fecha_periocidad_inbsc")+"-01");
					fechaLimiteInicio="";
					fechaLimiteFin="";
					
					Date fechaLimite=sumarMesFecha(utilitario.DeStringADate(fecha_nueva), multiplicadoPeriocidad);
					Date fechaLimiteInicioPeriocidad=utilitario.sumarDiasFecha(fechaLimite, -2);
					Date fechaLimiteFinPeriocidad=utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_respuesta.getValor("fecha_periocidad_inbsc")),4);
				
					
			    	Calendar calFechaComparacion = Calendar.getInstance();
					Calendar calFechaComparacionInicio = Calendar.getInstance();
					Calendar calFechaComparacionFin = Calendar.getInstance();
					calFechaComparacion.setTime(getFechaAsyyyyMMdd(utilitario.DeDateAString(fechaLimite)));
					calFechaComparacionInicio.setTime(fechaLimiteInicioPeriocidad);
					calFechaComparacionFin.setTime(fechaLimiteInicioPeriocidad);

	 				
	 				if (calFechaComparacion.compareTo(calFechaComparacionInicio) >= 0 && calFechaComparacion.compareTo(calFechaComparacionFin)<=0){
	 				
	 						
				
				}else {*/
					utilitario.agregarMensaje("El indicador seleccionado", "Ya se encuentra registrado");
			    	tab_balance_score_reporte.setValor("ide_inobj","");
			    	tab_balance_score_reporte.setValor("ide_inesa","");
			    	tab_balance_score_reporte.setValor("ide_inmec","");
					tab_balance_score_reporte.setValor("ide_inobi","");
			    	tab_balance_score_reporte.setValor("ide_infre","");
			    	tab_balance_score_reporte.setValor("ide_inmed","");
			    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
			    	return;
				}
			}else {
				
				int mes_temp=0,mesConsulta=0,anioConsulta=0;
				mes_temp=Integer.parseInt(mes)-1;
				String fec1="";
				fecha_periocidad_anterior="";
				if (mes_temp==0) {
					mesConsulta=12;
					TablaGenerica tabAnio=utilitario.consultar("SELECT ide_geani, detalle_geani "
							+ "FROM gen_anio  "
							+ "where ide_geani="+com_anio.getValue());
				
					fec1=tabAnio.getValor("detalle_geani")+"-02-01";
					anioConsulta=utilitario.getAnio(utilitario.DeDateAString(sumarAnioFecha(utilitario.DeStringADate(fec1), -1)));
					
					TablaGenerica tabAnioAnterior=utilitario.consultar("SELECT ide_geani, detalle_geani "
							+ "FROM gen_anio  "
							+ "where detalle_geani='"+anioConsulta+"'");
					
					
					sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
							+ "FROM ind_balance_score_card "
							+ "where ide_geani="+tabAnioAnterior.getValor("ide_geani")+" ";
				    //	if (tipo_perfil.equals("1")) {
					//		sql+= "and ide_geare="+com_area.getValue()+" ";
						//	}else {
								sql+= "and ide_geare="+ide_geare+" ";	
					//		}			
				    		sql+= "and ide_gemes="+mesConsulta+" "
							+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

					TablaGenerica tab_indicador_anterior=utilitario.consultar(sql);		
							if (tab_indicador_anterior.getTotalFilas()>0) {
								fecha_periocidad_anterior=tab_indicador_anterior.getValor("fecha_periocidad_inbsc");

								String fecha_periocidad="",fecha_nueva="",fechaLimiteInicio="",fechaLimiteFin="";
								
								if (tab_indicador_anterior.getValor("fecha_periocidad_inbsc")==null || tab_indicador_anterior.getValor("fecha_periocidad_inbsc").isEmpty() || tab_indicador_anterior.getValor("fecha_periocidad_inbsc").equals("")) {
									utilitario.agregarMensaje("Error Fecha Periocidad", "No se ha asignado un valor");
							    	tab_balance_score_reporte.setValor("ide_inobj","");
							    	tab_balance_score_reporte.setValor("ide_inesa","");
							    	tab_balance_score_reporte.setValor("ide_inmec","");
									tab_balance_score_reporte.setValor("ide_inobi","");
							    	tab_balance_score_reporte.setValor("ide_infre","");
							    	tab_balance_score_reporte.setValor("ide_inmed","");
							    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
							    	bandReg=false;
							    	fecha_periocidad_anterior="";
									return;
								}else {
									
									if (tab_indicador_anterior.getValor("ide_infre")==null || tab_indicador_anterior.getValor("ide_infre").isEmpty() || tab_indicador_anterior.getValor("ide_infre").equals("")) {
										utilitario.agregarMensaje("Error Frecuencia", "No se ha asignado un valor");
								    	tab_balance_score_reporte.setValor("ide_inobj","");
								    	tab_balance_score_reporte.setValor("ide_inesa","");
								    	tab_balance_score_reporte.setValor("ide_inmec","");
										tab_balance_score_reporte.setValor("ide_inobi","");
								    	tab_balance_score_reporte.setValor("ide_infre","");
								    	tab_balance_score_reporte.setValor("ide_inmed","");
								    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
								    	bandReg=false;
								    	fecha_periocidad_anterior="";
										return;
									}else {
										
										TablaGenerica tab_frecuencia =utilitario.consultar("SELECT ide_infre, detalle_infre, activo_infre,multiplicador_infre "
												+ "FROM ind_frecuencia "
												+ "where ide_infre="+tab_indicador_anterior.getValor("ide_infre"));
										int multiplicadoPeriocidad=0;
										multiplicadoPeriocidad=Integer.parseInt(tab_frecuencia.getValor("multiplicador_infre"));
										fecha_periocidad=tab_indicador_anterior.getValor("fecha_periocidad_inbsc");
										fecha_nueva=utilitario.getAnio(tab_indicador_anterior.getValor("fecha_periocidad_inbsc"))+"-"+utilitario.getMes(tab_indicador_anterior.getValor("fecha_periocidad_inbsc"))+"-01";
										fechaLimiteInicio="";
										fechaLimiteFin="";
										
										Date fechaLimite=sumarMesFecha(utilitario.DeStringADate(fecha_nueva), multiplicadoPeriocidad);
										Date fechaLimiteInicioPeriocidad=utilitario.sumarDiasFecha(fechaLimite, Integer.parseInt(utilitario.getVariable("p_dias_max_ingreso_indicadores")));
										Date fechaLimiteFinPeriocidad=utilitario.sumarDiasFecha(fechaLimite,Integer.parseInt(utilitario.getVariable("p_dias_max_fin_indicadores")));
																																																																																																																																																																																																																																																																																																																																																																																																																															
										
								    	Calendar calFechaComparacion = Calendar.getInstance();
										Calendar calFechaComparacionInicio = Calendar.getInstance();
										Calendar calFechaComparacionFin = Calendar.getInstance();
										calFechaComparacion.setTime(getFechaAsyyyyMMdd(utilitario.getFechaActual()));
										calFechaComparacionInicio.setTime(fechaLimiteInicioPeriocidad);
										calFechaComparacionFin.setTime(fechaLimiteFinPeriocidad);

						 				
						 				if (calFechaComparacion.compareTo(calFechaComparacionInicio) >= 0 && calFechaComparacion.compareTo(calFechaComparacionFin)<=0){
									    	bandReg=true;
									    	fecha_periocidad_anterior=utilitario.DeDateAString(fechaLimite);
						 						
									
									}else {
										utilitario.agregarMensaje("Fecha de registro del indicador", "Desde: "+utilitario.DeDateAString(fechaLimiteInicioPeriocidad)+" Hasta: "+utilitario.DeDateAString(fechaLimiteFinPeriocidad));
								    	tab_balance_score_reporte.setValor("ide_inobj","");
								    	tab_balance_score_reporte.setValor("ide_inesa","");
								    	tab_balance_score_reporte.setValor("ide_inmec","");
										tab_balance_score_reporte.setValor("ide_inobi","");
								    	tab_balance_score_reporte.setValor("ide_infre","");
								    	tab_balance_score_reporte.setValor("ide_inmed","");
								    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
								    	bandReg=false;
								    	fecha_periocidad_anterior="";
										return;	
									}
								
									
								}
									
								}			
								
								
								
								
								
								
								
								
							}else{
							//	fecha_periocidad_anterior="";
						    	bandReg=true;

							}
								
								
							
							
							
							
							
							
							
							
				
					
					
				}else {
					mesConsulta=mes_temp;
					sql="SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind,fecha_periocidad_inbsc,ide_infre "
							+ "FROM ind_balance_score_card "
							+ "where ide_geani="+com_anio.getValue()+" ";
				    //	if (tipo_perfil.equals("1")) {
					//		sql+= "and ide_geare="+com_area.getValue()+" ";
						//	}else {
								sql+= "and ide_geare="+ide_geare+" ";	
					//		}			
				    		sql+= "and ide_gemes="+mesConsulta+" "
							+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ";

					TablaGenerica tab_indicador_anterior=utilitario.consultar(sql);		
							if (tab_indicador_anterior.getTotalFilas()>0) {
								fecha_periocidad_anterior="";	
								String fecha_periocidad="",fecha_nueva="",fechaLimiteInicio="",fechaLimiteFin="";
								
								if (tab_indicador_anterior.getValor("fecha_periocidad_inbsc")==null || tab_indicador_anterior.getValor("fecha_periocidad_inbsc").isEmpty() || tab_indicador_anterior.getValor("fecha_periocidad_inbsc").equals("")) {
									utilitario.agregarMensaje("Error Fecha Periocidad", "Debe asignar una fecha de periocidad en el mes anterior");
							    	tab_balance_score_reporte.setValor("ide_inobj","");
							    	tab_balance_score_reporte.setValor("ide_inesa","");
							    	tab_balance_score_reporte.setValor("ide_inmec","");
									tab_balance_score_reporte.setValor("ide_inobi","");
							    	tab_balance_score_reporte.setValor("ide_infre","");
							    	tab_balance_score_reporte.setValor("ide_inmed","");
							    	bandReg=false;
							    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
							    	fecha_periocidad_anterior="";
									return;
								}else {
									
									if (tab_indicador_anterior.getValor("ide_infre")==null || tab_indicador_anterior.getValor("ide_infre").isEmpty() || tab_indicador_anterior.getValor("ide_infre").equals("")) {
										utilitario.agregarMensaje("Error Frecuencia", "No se ha asignado un valor");
								    	tab_balance_score_reporte.setValor("ide_inobj","");
								    	tab_balance_score_reporte.setValor("ide_inesa","");
								    	tab_balance_score_reporte.setValor("ide_inmec","");
										tab_balance_score_reporte.setValor("ide_inobi","");
								    	tab_balance_score_reporte.setValor("ide_infre","");
								    	tab_balance_score_reporte.setValor("ide_inmed","");
								    	bandReg=false;
								    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
								    	fecha_periocidad_anterior="";
										return;
									}else {
										fecha_periocidad_anterior=tab_indicador_anterior.getValor("fecha_periocidad_inbsc");
										TablaGenerica tab_frecuencia =utilitario.consultar("SELECT ide_infre, detalle_infre, activo_infre,multiplicador_infre "
												+ "FROM ind_frecuencia "
												+ "where ide_infre="+tab_indicador_anterior.getValor("ide_infre"));
										int multiplicadoPeriocidad=0;
										multiplicadoPeriocidad=Integer.parseInt(tab_frecuencia.getValor("multiplicador_infre"));
										fecha_periocidad=tab_indicador_anterior.getValor("fecha_periocidad_inbsc");
										fecha_nueva=utilitario.getAnio(tab_indicador_anterior.getValor("fecha_periocidad_inbsc"))+"-"+utilitario.getMes(tab_indicador_anterior.getValor("fecha_periocidad_inbsc"))+"-01";
										fechaLimiteInicio="";
										fechaLimiteFin="";
										
										Date fechaLimite=sumarMesFecha(utilitario.DeStringADate(fecha_nueva), multiplicadoPeriocidad);
										Date fechaLimiteInicioPeriocidad=utilitario.sumarDiasFecha(fechaLimite, Integer.parseInt(utilitario.getVariable("p_dias_max_ingreso_indicadores")));
										Date fechaLimiteFinPeriocidad=utilitario.sumarDiasFecha(fechaLimite,Integer.parseInt(utilitario.getVariable("p_dias_max_fin_indicadores")));
									
										
								    	Calendar calFechaComparacion = Calendar.getInstance();
										Calendar calFechaComparacionInicio = Calendar.getInstance();
										Calendar calFechaComparacionFin = Calendar.getInstance();
										calFechaComparacion.setTime(getFechaAsyyyyMMdd(utilitario.getFechaActual()));
										calFechaComparacionInicio.setTime(fechaLimiteInicioPeriocidad);
										calFechaComparacionFin.setTime(fechaLimiteFinPeriocidad);

						 				
						 				if (calFechaComparacion.compareTo(calFechaComparacionInicio) >= 0 && calFechaComparacion.compareTo(calFechaComparacionFin)<=0){
									    	bandReg=true;
									    	fecha_periocidad_anterior=utilitario.DeDateAString(fechaLimite);

						 						
									
									}else {
										utilitario.agregarMensaje("Fecha para el registro de indicador", "Desde: "+utilitario.DeDateAString(fechaLimiteInicioPeriocidad)+" Hasta: "+utilitario.DeDateAString(fechaLimiteFinPeriocidad));
								    	tab_balance_score_reporte.setValor("ide_inobj","");
								    	tab_balance_score_reporte.setValor("ide_inesa","");
								    	tab_balance_score_reporte.setValor("ide_inmec","");
										tab_balance_score_reporte.setValor("ide_inobi","");
								    	tab_balance_score_reporte.setValor("ide_infre","");
								    	tab_balance_score_reporte.setValor("ide_inmed","");
								    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed","");
								    	bandReg=false;
								    	fecha_periocidad_anterior="";
										return;	
									}
								
									
								}
									
								}
								
								
								
								
								
								
							}else{
								fecha_periocidad_anterior="";
						    	bandReg=true;
							}
					
					
					
				}
								
				
			}
			
			if (bandReg) {
			
		
			
			TablaGenerica tab_respuestaAnterior=utilitario.consultar("SELECT ide_inbap, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind, "
					+ "ide_inmec, ide_inobi, ide_infre, ide_inmed, ide_geani,"
					+ "ind_indes,ind_inrec,meta_inbap,recursos_inbap,meta_inbap  "
					+ "FROM ind_balance_score_card_parametro  "
					+ "where ide_geani="+com_anio.getValue()+" "
					+ "and ide_geare="+area+" "
					+ "and ide_inind in("+tab_balance_score_reporte.getValor("ide_inind")+") ");
			
			tab_balance_score_reporte.setValor("ide_inper",tab_respuestaAnterior.getValor("ide_inper"));
	    	tab_balance_score_reporte.setValor("ide_inobj",tab_respuestaAnterior.getValor("ide_inobj"));
	    	tab_balance_score_reporte.setValor("ide_inesa",tab_respuestaAnterior.getValor("ide_inesa"));
	    	tab_balance_score_reporte.setValor("ide_inmec",tab_respuestaAnterior.getValor("ide_inmec"));
			tab_balance_score_reporte.setValor("ide_inobi",tab_respuestaAnterior.getValor("ide_inobi"));
	    	tab_balance_score_reporte.setValor("ide_infre",tab_respuestaAnterior.getValor("ide_infre"));
	    	tab_balance_score_reporte.setValor("ide_inmed",tab_respuestaAnterior.getValor("ide_inmed"));
	    	tab_balance_score_reporte.setValor("ind_indes",tab_respuestaAnterior.getValor("ind_indes"));
	    //	tab_balance_score_reporte.setValor("ind_inrec",tab_respuestaAnterior.getValor("ind_inrec"));
	    	tab_balance_score_reporte.setValor("recursos_inbsc",tab_respuestaAnterior.getValor("recursos_inbap"));
	     boolean contieneComa=false,contienePunto=false;
	     contieneComa=tab_respuestaAnterior.getValor("meta_inbap").contains(",");
	     contienePunto=tab_respuestaAnterior.getValor("meta_inbap").contains(".");
	     double valorNuevo=0.00;
	     int tamano=0;
	     String nuevaCadenaTemp="",nuevaCadena="";
	     if (contieneComa==true) {  	 
	    	 tamano=tab_respuestaAnterior.getValor("meta_inbap").length()-1;
	    	 nuevaCadenaTemp=tab_respuestaAnterior.getValor("meta_inbap").substring(0, tamano);
	    	 nuevaCadena = nuevaCadenaTemp.replace(",", ".");
	    	 valorNuevo=Double.parseDouble(nuevaCadena);
	    
		}else if (contienePunto==true) {
			tamano=tab_respuestaAnterior.getValor("meta_inbap").length()-1;
			 nuevaCadena=tab_respuestaAnterior.getValor("meta_inbap").substring(0, tamano);
			 valorNuevo=Double.parseDouble(nuevaCadena);
		}else {
			tamano=tab_respuestaAnterior.getValor("meta_inbap").length()-1;
			 nuevaCadena=tab_respuestaAnterior.getValor("meta_inbap").substring(0, tamano);
			 valorNuevo=Double.parseDouble(nuevaCadena);
		}
	    
	 	tab_balance_score_reporte.getColumna("meta_inbsc").setLectura(false);

	    	tab_balance_score_reporte.setValor("meta_inbsc",""+valorNuevo);
		 	tab_balance_score_reporte.getColumna("meta_inbsc").setLectura(true);

	    
	    	
	    	tab_balance_score_reporte.setValor("ide_inana","1");
	    	tab_balance_score_reporte.setValor("mes_porcentaje","");
	    	tab_balance_score_reporte.setValor("ejecutado_inbsc","");
	    	tab_balance_score_reporte.setValor("eveluado_inbsc","");
	    	tab_balance_score_reporte.setValor("observacion_inbsc","");
	    	tab_balance_score_reporte.setValor("plan_accion_inbsc","");
	    	tab_balance_score_reporte.setValor("evaluacion_negativa_inbsc","");
	    	tab_balance_score_reporte.setValor("evaluacion_positiva_inbsc","");
	    	//tab_balance_score_reporte.setValor("fecha_periocidad_inbsc",fecha_periocidad_anterior);
	    	utilitario.addUpdateTabla(tab_balance_score_reporte,"ide_inper,ide_inobj,ide_inesa,ide_inmec,ide_inobi,ide_infre,ide_inmed,ide_inana,mes_porcentaje,ejecutado_inbsc,eveluado_inbsc,observacion_inbsc,plan_accion_inbsc,evaluacion_negativa_inbsc,evaluacion_positiva_inbsc,ind_indes,ind_inrec,recursos_inbsc,meta_inbsc","");
			}
		else {
				}
					
	}
	
	
	public String getMes (){
		String tab_mes= "select ide_gemes,detalle_gemes from gen_mes where ide_gemes not in  (13) order by ide_gemes";
		return tab_mes;
	
		}


	public void obtenerDatos(){
		if(com_anio.getValue()!=null){
			anio=com_anio.getValue().toString();
			if(com_mes.getValue()!=null){
			tab_balance_score_reporte.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_gemes="+com_mes.getValue());	
			tab_balance_score_reporte.ejecutarSql();
			}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga mes");
		    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
				return;		
			}
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga anio ");
	    	tab_balance_score_reporte.setCondicion("ide_geani=-1");
			return;			
		}
	}


	public Tabla getTab_balance_score_reporte_archivo_adjunto() {
		return tab_balance_score_reporte_archivo_adjunto;

	
	}

	public void setTab_balance_score_reporte_archivo_adjunto(
			Tabla tab_balance_score_reporte_archivo_adjunto) {
		this.tab_balance_score_reporte_archivo_adjunto = tab_balance_score_reporte_archivo_adjunto;
	}
	
	
	
	public Tabla getTab_balance_score_reporte() {
		return tab_balance_score_reporte;
	}

	public void setTab_balance_score_reporte(Tabla tab_balance_score_reporte) {
		this.tab_balance_score_reporte = tab_balance_score_reporte;
	}

	public  void calacularMeta(AjaxBehaviorEvent evt){
		try {
		tab_balance_score_reporte.modificar(evt);		
		if(tab_balance_score_reporte.getValor("meta_inbsc")!=null && !tab_balance_score_reporte.getValor("meta_inbsc").isEmpty()
				&& tab_balance_score_reporte.getValor("ejecutado_inbsc")!=null && !tab_balance_score_reporte.getValor("ejecutado_inbsc").isEmpty()){					
			calculoPorcentajeMeta(tab_balance_score_reporte.getValor("meta_inbsc"), tab_balance_score_reporte.getValor("ejecutado_inbsc"));
		}else {
			tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc","");
			utilitario.addUpdateTabla(tab_balance_score_reporte,"eveluado_inbsc","");
			utilitario.agregarMensajeInfo("El valor del campo EVALUADO no se puede calcular", "Ingrese valores en META Y EJECUTADO");

		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR METODO CalcularMeta()");
		}
	}

	public  void calacularMeta(SelectEvent evt){
		try {
			tab_balance_score_reporte.modificar(evt);
			if(tab_balance_score_reporte.getValor("meta_inbsc")!=null && !tab_balance_score_reporte.getValor("meta_inbsc").isEmpty()
					&& tab_balance_score_reporte.getValor("ejecutado_inbsc")!=null && !tab_balance_score_reporte.getValor("ejecutado_inbsc").isEmpty()){					
				calculoPorcentajeMeta(tab_balance_score_reporte.getValor("meta_inbsc"), tab_balance_score_reporte.getValor("ejecutado_inbsc"));
			}else {
		      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setLectura(false);
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc","");
				utilitario.addUpdateTabla(tab_balance_score_reporte,"eveluado_inbsc","");
				utilitario.agregarMensajeInfo("El valor del campo EVALUADO no se puede calcular", "Ingrese valores en META Y EJECUTADO");
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR METODO CalcularMeta()");
		}
	}

	

	
	

	public void calculoPorcentajeMeta(String meta , String ejecutado){
		try {
			
			double meta_inbsc=0.00,ejecutado_inbsc=0.00;
			meta_inbsc=Double.parseDouble(meta);
			ejecutado_inbsc=Double.parseDouble(ejecutado);
			
			if (meta_inbsc<ejecutado_inbsc) {
		      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setLectura(false);
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc","100.00");
				utilitario.addUpdateTabla(tab_balance_score_reporte,"eveluado_inbsc", "100.00");
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"semaforo_inbsc","VERDE");
				utilitario.addUpdateTabla(tab_balance_score_reporte,"semaforo_inbsc", "VERDE");
		      	tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setLectura(false);
				utilitario.addUpdateTabla(tab_balance_score_reporte,"fecha_registro_adjunto_inbsc", utilitario.getFechaActual());

			}else if (meta_inbsc>ejecutado_inbsc) {
				double valorEvaluado=0.00,division=0.00;
				division=ejecutado_inbsc/meta_inbsc;
				valorEvaluado=Double.parseDouble(utilitario.getFormatoNumero(division*100,2));
		      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setLectura(false);
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc",valorEvaluado+"");
				utilitario.addUpdateTabla(tab_balance_score_reporte,"eveluado_inbsc", valorEvaluado+"");
				String etiqueta="";
				if (valorEvaluado<=100.00 && valorEvaluado>=98.00) {
					etiqueta="VERDE";
					tab_balance_score_reporte.getColumna("semaforo_inbsc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:green");

				}else if (valorEvaluado<98.00 && valorEvaluado>=96.00) {
					etiqueta="AMARILLO";
					tab_balance_score_reporte.getColumna("semaforo_inbsc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:yellow");

				}else {
					etiqueta="ROJO";
					tab_balance_score_reporte.getColumna("semaforo_inbsc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");

				}
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"semaforo_inbsc",etiqueta);
		      	tab_balance_score_reporte.getColumna("fecha_registro_adjunto_inbsc").setLectura(false);
				tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"fecha_registro_adjunto_inbsc",utilitario.getFechaActual());

				utilitario.addUpdateTabla(tab_balance_score_reporte,"semaforo_inbsc", etiqueta);
				utilitario.addUpdateTabla(tab_balance_score_reporte,"fecha_registro_adjunto_inbsc", utilitario.getFechaActual());


			}else {
				if (meta_inbsc==0.00 && ejecutado_inbsc==0.00 ) {
			      	tab_balance_score_reporte.getColumna("eveluado_inbsc").setLectura(false);
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"meta_inbsc","");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"ejecutado_inbsc","");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc","");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"fecha_registro_adjunto_inbsc","");
					utilitario.addUpdateTabla(tab_balance_score_reporte,"meta_inbsc,ejecutado_inbsc,eveluado_inbsc,fecha_registro_adjunto_inbsc", "");
				}else {
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"meta_inbsc",meta_inbsc+"");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"ejecutado_inbsc",meta_inbsc+"");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"eveluado_inbsc","100.00");
					utilitario.addUpdateTabla(tab_balance_score_reporte,"eveluado_inbsc", "100.00");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"semaforo_inbsc","VERDE");
					tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"fecha_registro_adjunto_inbsc",utilitario.getFechaActual());
					utilitario.addUpdateTabla(tab_balance_score_reporte,"semaforo_inbsc", "VERDE");
					utilitario.addUpdateTabla(tab_balance_score_reporte,"fecha_registro_adjunto_inbsc", utilitario.getFechaActual());

				}

			}
			
		} catch (Exception e) {
			// TODO: handle exception
		System.out.println("ERROR CALCULO DE HORAS");
		}
	}
	
	
/*	public boolean obtenerPeriocidad(int ide_gemes,int ide_geani,int ide_inind){
		boolean valorRetorno=false;
		TablaGenerica tab_periocidad=utilitario.consultar("SELECT ide_inbsc, ide_inper, ide_geare, ide_inobj, ide_inesa, ide_inind, "
				+ "ide_gemes, ide_geani, activo_inbsc, ide_indus,fecha_periocidad_inbsc "
				+ "FROM ind_balance_score_card  "
				+ "where ide_gemes in("+ide_gemes+") and ide_geani in("+ide_geani+") and ide_inind="+ide_inind);
		
		if (tab_periocidad.getTotalFilas()>0) {
			String fecha_periocidad="";
			//ide_infre
			
			if (tab_periocidad.getValor("fecha_periocidad_inbsc")==null || tab_periocidad.getValor("fecha_periocidad_inbsc").isEmpty() || tab_periocidad.getValor("fecha_periocidad_inbsc").isEmpty()) {
				
			}
			
			
			fecha_periocidad=tab_periocidad.getValor("fecha_periocidad_inbsc");
			

		}else {
		valorRetorno=true;
		}
		
		return valorRetorno;
		
	}*/
	
	
	
	public Date sumarMesFecha(Date fch, int numeroMesesSumar) {
		Calendar fechaInicial = Calendar.getInstance();
		Calendar fechaInicialCalculo = Calendar.getInstance();
		fechaInicial.setTime(fch);
		fechaInicial.add(Calendar.MONTH, numeroMesesSumar);
		return fechaInicial.getTime();

	}
	
	public Date sumarAnioFecha(Date fch, int numeroAniosSumar) {
		Calendar fechaInicial = Calendar.getInstance();
		Calendar fechaInicialCalculo = Calendar.getInstance();
		fechaInicial.setTime(fch);
		fechaInicial.add(Calendar.YEAR, numeroAniosSumar);
		return fechaInicial.getTime();

	}
	
    private Date getFechaAsyyyyMMdd(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date fechaDate = new Date();
	    try {	
	    	fechaDate = df.parse(fecha);
	    	return fechaDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;

    }
    private String getFechaAsyyyyMMdd(Date fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(fecha);
    }
    
	public void actualizarTabla2(){
		tab_balance_score_reporte_archivo_adjunto.setCondicion("ide_inbsc="+tab_balance_score_reporte.getValorSeleccionado());
		tab_balance_score_reporte_archivo_adjunto.ejecutarSql();		

	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarTabla2();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarTabla2();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizarTabla2();

	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarTabla2();

	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		actualizarTabla2();

	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		actualizarTabla2();

	}

	public void guardarDatosIndicador(){
		if (con_guardar.isVisible()){
		   	tab_balance_score_reporte.getColumna("modifica_inbsc").setLectura(false);
			tab_balance_score_reporte.setValor(tab_balance_score_reporte.getFilaActual(),"modifica_inbsc","true");
			utilitario.addUpdateTabla(tab_balance_score_reporte,"modifica_inbsc", "true");
		   	tab_balance_score_reporte.getColumna("modifica_inbsc").setLectura(true);
			utilitario.getConexion().guardarPantalla();
			guardarPantalla();

			con_guardar.cerrar();

		}
		
	}
	
	public void cancelarDatosIndicador(){
		//String ide_indbsc=tab_balance_score_reporte.getValor("ide_inbsc");
		tab_balance_score_reporte.actualizar();
		con_guardar.cerrar();

	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	
	
}
