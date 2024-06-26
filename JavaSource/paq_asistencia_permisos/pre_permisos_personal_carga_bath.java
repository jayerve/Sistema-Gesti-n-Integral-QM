/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
/**
 *
 * @author Diego
 */
public class pre_permisos_personal_carga_bath extends Pantalla {
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);	
	private ServicioEmpleado ser_empleado1 = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
    private Tabla tab_permisos = new Tabla();
    private Tabla tab_tabla_detalle_vacaccion = new Tabla();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private AutoCompletar aut_anio_mes = new AutoCompletar();
	int ide_gemes=0,ide_geani=0,ultimoDia=0;
	String ide_gtemp="",ide_gtempxx="",tipo_perfil="",area="",ide_asjei="",ide_geare,jefe_padre,cargo_padre,mes="",anio="",meses="",anios="",mesEditar="",anioEditar="",empleado="";
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();
    StringBuilder ide_empleados_crear=new StringBuilder(); 
    StringBuilder ide_empleados=new StringBuilder();
	TablaGenerica tabJefeInmediato=null;
	private Upload upl_archivo = new Upload();
    private SeleccionTabla sel_mes_importarTotales= new SeleccionTabla();
    private SeleccionTabla sel_anio_importarTotales= new SeleccionTabla();

    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    
    private SeleccionTabla sel_empleado_importarTotales= new SeleccionTabla();
	private Dialogo dia_importar_total = new Dialogo();
	private Dialogo dia_valida_empleado_total = new Dialogo();
	private Dialogo dia_filtro_activo = new Dialogo();
	String fecha="";
	private Etiqueta eti_num_nomina = new Etiqueta();
	private Editor edi_mensajes = new Editor();
	private Tabla tab_emp_total = new Tabla();
	private List<String[]> lis_importa = null; // Guardo los empleados y el
	private List<String[]> lis_turno = null; // Guardo los empleados y el
	private List<String[]> lis_empleado = null; // Guardo los empleados y el
	private Grid grid_tabla_emp_total = new Grid();
	private List<String[]> lis_importadia1 = null; // Guardo los empleados y el
	private Confirmar con_guardar = new Confirmar();
	private Etiqueta eti_dia = new Etiqueta();
	private Etiqueta eti_turno = new Etiqueta();
	boolean filtroActivoInactivo=false;
	public pre_permisos_personal_carga_bath() {
    TablaGenerica  tab_ingresos=null;
    	
		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,ide_gtemp_padre_asjei,cargo_padre_asjei  "
    			+ "FROM asi_jefe_inmediato  asjei "
    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
    			+ "where ide_gtemp="+ide_gtempxx);
    	
    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    		utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		return;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
        	area=tabJefeInmediato.getValor("detalle_geare");
        	TablaGenerica tabEmpleadoXJefeInmediato=null;
           	
        	if(tipo_perfil.equals("1")){
        		ide_asjei=""; 
   				TablaGenerica tab_ide_geedp=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare  "
   		    			+ "FROM asi_jefe_inmediato  asjei "
   		    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare ");
				if (tab_ide_geedp.getTotalFilas()>0) {
					for (int j = 0; j < tab_ide_geedp.getTotalFilas(); j++) {
						if (j==(tab_ide_geedp.getTotalFilas()-1)) {
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
						}else{
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
							ide_asjei+=",";

						}
					}
        		
	    	 }
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
                tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
          				+ "FROM asi_empleado_jefe_inmediato ");
          			//	+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
      
                 String int_num_col_idegetempmensual="";
                 for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                		 str_ide_empleado_mensual.append(",");
                	 }
                	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                 }
        		
        		
        		
        	}
        	
        	if(tipo_perfil.equals("2")){
        		ide_asjei=tabJefeInmediato.getValor("ide_asjei"); 
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
        	       tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
             				+ "FROM asi_empleado_jefe_inmediato "
             				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
         
                    String int_num_col_idegetempmensual="";
                    for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                   	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                   	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                   		 str_ide_empleado_mensual.append(",");
                   	 }
                   	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                    }
                    }
        	}
    		
		Etiqueta eti_anio_mes=new Etiqueta("Seleccione Empleado:");
		bar_botones.agregarComponente(eti_anio_mes);
	//	aut_anio_mes.setId("aut_anio_mes");
	//	aut_anio_mes.setAutoCompletar(obtenerPeriodo());		
	//	aut_anio_mes.setMetodoChange("filtrarHorarioEmpleado");
	//	bar_botones.agregarComponente(aut_anio_mes);
    	
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);		
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);



		Boton bot_carga_notificacion = new Boton();
		bot_carga_notificacion.setIcon("ui-icon-cancel");
		bot_carga_notificacion.setValue("Importar Valores");
		bot_carga_notificacion.setTitle("Importar Valores");
		bot_carga_notificacion.setMetodo("abrirDialogoImportar");
		bar_botones.agregarBoton(bot_carga_notificacion);
		
		
		Boton bot_envio_notificacion = new Boton();
		bot_envio_notificacion.setIcon("ui-icon-cancel");
		bot_envio_notificacion.setValue("Enviar Notificaciones");
		bot_envio_notificacion.setTitle("Enviar Notificaciones");
		bot_envio_notificacion.setMetodo("enviarNotificacion");
	//	bar_botones.agregarBoton(bot_envio_notificacion);
		    
		
		Boton bot_consultar_permisos = new Boton();
		bot_consultar_permisos.setIcon("ui-icon-cancel");
		bot_consultar_permisos.setValue("Consultar Permisos");
		bot_consultar_permisos.setTitle("Consultar Permisos");
		bot_consultar_permisos.setMetodo("consultarPermisos");
		bar_botones.agregarBoton(bot_consultar_permisos);
		
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
    	
    	
		
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////IMPORTAR VALORES CALENDARIO//////////////////////////////////////////////////////////////
    	
    	sel_mes_importarTotales.setId("sel_mes_importarTotales");
		sel_mes_importarTotales.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in(1,2,3,4,5,6,7,8,9,10,11,12) order by ide_gemes asc","IDE_GEMES");
		sel_mes_importarTotales.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes_importarTotales.setRadio();
		sel_mes_importarTotales.setTitle("Seleccione Mes Horario");
		gru_pantalla.getChildren().add(sel_mes_importarTotales);
		sel_mes_importarTotales.setWidth("20");
		sel_mes_importarTotales.setHeight("45");
		sel_mes_importarTotales.getBot_aceptar().setMetodo("obtenerMesImportarTotales");
		agregarComponente(sel_mes_importarTotales);
	
		
	 	sel_anio_importarTotales.setId("sel_anio_importarTotales");
    	sel_anio_importarTotales.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in(15,16)","IDE_GEANI");
    	sel_anio_importarTotales.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	sel_anio_importarTotales.setRadio();
    	sel_anio_importarTotales.setTitle("Seleccione Anio Importar");
		gru_pantalla.getChildren().add(sel_anio_importarTotales);
		sel_anio_importarTotales.setWidth("25");
		sel_anio_importarTotales.setHeight("30");
		sel_anio_importarTotales.getBot_aceptar().setMetodo("obtenerAnioImportarTotales");
		agregarComponente(sel_anio_importarTotales);
		
		
		
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////IMPORTAR VALORES CALENDARIO//////////////////////////////////////////////////////////////
    	
    	sel_mes.setId("sel_mes");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in(1,2,3,4,5,6,7,8,9,10,11,12) order by ide_gemes asc ","IDE_GEMES");
    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    	sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.setWidth("20");
		sel_mes.setHeight("45");
		sel_mes.getBot_aceptar().setMetodo("obtenerMes");
		agregarComponente(sel_mes);
	
		
	 	sel_anio.setId("sel_anio");
	 	sel_anio.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+16+")","IDE_GEANI");
	 	sel_anio.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
	 	sel_anio.setRadio();
	 	sel_anio.setTitle("Seleccione Anio Importar");
		gru_pantalla.getChildren().add(sel_anio);
		sel_anio.setWidth("25");
		sel_anio.setHeight("30");
		sel_anio.getBot_aceptar().setMetodo("obtenerAnio");
		agregarComponente(sel_anio);
		
		
		
	
		sel_empleado_importarTotales.setId("sel_empleado_importarTotales");
		agregarComponente(sel_empleado_importarTotales);
		
		List lista = new ArrayList();
		Object fila1[] = {
				"false", "INACTIVO"
		};
		Object fila2[] = {
				"true", "ACTIVO"
		};
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setListaSeleccion(lista);
		lis_activo.setVertical();
		dia_filtro_activo.setId("dia_filtro_activo");
		dia_filtro_activo.setTitle("SELECCIONE EMPLEADO ACTIVO / INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("seleccionaEmpleado");
		dia_filtro_activo.setDialogo(lis_activo);
		dia_filtro_activo.setHeight("40%");
		dia_filtro_activo.setWidth("40%");
		dia_filtro_activo.setDynamic(false);
		agregarComponente(dia_filtro_activo);
		
		dia_importar_total.setId("dia_importar_total");
		dia_importar_total.setTitle("VALIDACION TOTAL DE HORAS EXTRA");
		dia_importar_total.setPosition("left");
		dia_importar_total.setHeight("85%");
		dia_importar_total.getBot_aceptar().setRendered(false);
		dia_importar_total.setWidth("50%");
		dia_importar_total.getBot_cancelar().setMetodo("cancelarImportarTotal");
		
								
		Grid gri_cuerpo_total = new Grid();

		Grid gri_impo_total = new Grid();
		gri_impo_total.setColumns(2);

		//gri_impo_total.getChildren().add(new Etiqueta("Todas las Nominas'"));

		Grid gri_tn_total = new Grid();
		gri_tn_total.setColumns(2);

	
		eti_num_nomina.setId("eti_num_nomina");
		eti_num_nomina.setStyle("font-size:8px;");
		gri_tn_total.getChildren().add(eti_num_nomina);
		gri_impo_total.getChildren().add(gri_tn_total);


		gri_impo_total.getChildren().add(new Etiqueta("Seleccione el archivo: "));
		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivoTotal");

		upl_archivo.setUpdate("gri_valida_total");
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Validar");
		upl_archivo.setCancelLabel("Cancelar Seleccion");

		gri_impo_total.getChildren().add(upl_archivo);
		gri_impo_total.setWidth("100%");

		Grid gri_valida_total = new Grid();
		gri_valida_total.setId("gri_valida_total");
		gri_valida_total.setColumns(3);

		Etiqueta eti_valida_total = new Etiqueta();
		eti_valida_total.setValueExpression("value", "pre_index.clase.upl_archivo.nombreReal");
		eti_valida_total.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida_total.getChildren().add(eti_valida_total);

		Imagen ima_valida_total = new Imagen();
		ima_valida_total.setWidth("22");
		ima_valida_total.setHeight("22");
		ima_valida_total.setValue("/imagenes/im_excel.gif");
		ima_valida_total.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida_total.getChildren().add(ima_valida_total);

		edi_mensajes.setControls("");
		edi_mensajes.setId("edi_mensajes");
		edi_mensajes.setStyle("overflow:auto;");
		edi_mensajes.setWidth(dia_importar_total.getAnchoPanel() - 15);
		edi_mensajes.setDisabled(true);
		gri_valida_total.setFooter(edi_mensajes);

		gri_cuerpo_total.setStyle("width:" + (dia_importar_total.getAnchoPanel() - 5) + "px;");
		gri_cuerpo_total.setMensajeInfo("Esta opcion  permite subir valores a un rubro a partir de un archivo xls");
		gri_cuerpo_total.getChildren().add(gri_impo_total);
		gri_cuerpo_total.getChildren().add(gri_valida_total);
		gri_cuerpo_total.getChildren().add(edi_mensajes);
		gri_cuerpo_total.getChildren().add(new Espacio("0", "10"));

		dia_importar_total.setDialogo(gri_cuerpo_total);
		dia_importar_total.setDynamic(false);

		agregarComponente(dia_importar_total);

		
		
		
		dia_valida_empleado_total.setId("dia_valida_empleado_total");
		dia_valida_empleado_total.getBot_aceptar().setMetodo("aceptarImportarTotal");
		dia_valida_empleado_total.getBot_cancelar().setMetodo("cancelarImportarTotal");
		dia_valida_empleado_total.setModal(false);
		dia_valida_empleado_total.setPosition("right");
		dia_valida_empleado_total.setTitle("Colaboradores encontrados en el archivo");
		dia_valida_empleado_total.setWidth("50%");
		dia_valida_empleado_total.setHeight("85%");
		
		
	  tab_emp_total.setId("tab_emp_total");
	  tab_emp_total.setSql("SELECT EMP.ide_gtemp, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, "
			  		+ "0 as VALOR, "
			  		+ "'' as DESCRIPCION "
					+ "FROM gth_empleado EMP  "
			  		+ "where EMP.ide_gtemp=-1");
	  tab_emp_total.setCampoPrimaria("IDE_GTEMP");

	  tab_emp_total.setRows(15);
	  tab_emp_total.setLectura(true);
	  tab_emp_total.dibujar();

		
	  eti_dia.setId("eti_dia1");
	  eti_dia.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_turno.setId("eti_dia2");
	  eti_turno.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	 	grid_tabla_emp_total.setStyle("width:" + (dia_valida_empleado_total.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado_total.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_valida_empleado_total.setDialogo(grid_tabla_emp_total);
		dia_valida_empleado_total.setDynamic(false);

		agregarComponente(dia_valida_empleado_total);
		
		
		
		
		
		
    	
	//  PERMISOS (division 1)

			tab_permisos.setId("tab_permisos");
			tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
			tab_permisos.getColumna("IDE_ASPVH").setNombreVisual("CODIGO");	
			tab_permisos.getColumna("IDE_ASPVH").setOrden(1);
			tab_permisos.getColumna("TIPO_ASPVH").setValorDefecto("1");// 1 permisos 
			tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
			//tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
			tab_permisos.getColumna("IDE_GTEMP").setCombo("SELECT EMP.IDE_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
					+ "from gth_empleado EMP ");
			tab_permisos.getColumna("IDE_GTEMP").setNombreVisual("SOLICITANTE");
			tab_permisos.getColumna("IDE_GTEMP").setOrden(3);

			tab_permisos.getColumna("IDE_GEEDP").setVisible(false);

			tab_permisos.getColumna("IDE_GEMES").setCombo("SELECT ide_gemes, detalle_gemes  FROM gen_mes;");
			tab_permisos.getColumna("IDE_GEMES").setNombreVisual("MES");
			tab_permisos.getColumna("IDE_GEMES").setOrden(7);
			tab_permisos.getColumna("IDE_GEMES").setVisible(false);

			tab_permisos.getColumna("IDE_GEANI").setCombo("SELECT ide_geani, detalle_geani FROM gen_anio");
			tab_permisos.getColumna("IDE_GEANI").setNombreVisual("ANIO");
			tab_permisos.getColumna("IDE_GEANI").setOrden(8);
			tab_permisos.getColumna("IDE_GEANI").setVisible(false);

			
			
			tab_permisos.getColumna("IDE_GEEST").setVisible(false);		
			tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);	
			tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setVisible(false);
			tab_permisos.getColumna("IDE_GTEMP_APROBADOR_DOC").setVisible(false);

			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setNombreVisual("FECHA SOLICITUD");
			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setOrden(9);

			tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
			tab_permisos.getColumna("ACTIVO_ASPVH").setNombreVisual("ACTIVO");
			tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
			tab_permisos.getColumna("ACTIVO_ASPVH").setOrden(16);

			tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO where ide_asmot=34 order by DETALLE_ASMOT");		
			tab_permisos.getColumna("IDE_ASMOT").setNombreVisual("MOTIVO AUSENCIA");
			tab_permisos.getColumna("IDE_ASMOT").setRequerida(true);	
			tab_permisos.getColumna("IDE_ASMOT").setOrden(2);

			tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
					"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
					"DEPA.DETALLE_GEDEP, " +
					"CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END " +

					"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
					"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
					"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
					"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
					"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
					//+ "WHERE EPAR.ACTIVO_GEEDP=TRUE ");

			tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
			tab_permisos.getColumna("GEN_IDE_GEEDP").setNombreVisual("GERENTE DE AREA");
			tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP").setOrden(4);

			tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setNombreVisual("JEFE INMEDIATO");
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP2").setOrden(5);

			//GERENTE DE AREA GEN_IDE_GEEDP3
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setNombreVisual("RESPONSABLE TH");
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);
			tab_permisos.getColumna("GEN_IDE_GEEDP3").setOrden(6);
			
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
			tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setNombreVisual("APLICA VACACION");

			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("CargarFechaHasta");
			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setNombreVisual("FECHA DESDE");
			tab_permisos.getColumna("FECHA_DESDE_ASPVH").setOrden(10);

			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);
			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setNombreVisual("FECHA HASTA");
			tab_permisos.getColumna("FECHA_HASTA_ASPVH").setVisible(false);
			
			tab_permisos.getColumna("HORA_DESDE_ASPVH").setRequerida(true);
			tab_permisos.getColumna("HORA_DESDE_ASPVH").setNombreVisual("HORA DESDE");
			tab_permisos.getColumna("HORA_DESDE_ASPVH").setVisible(false);

			tab_permisos.getColumna("HORA_HASTA_ASPVH").setRequerida(true);
			tab_permisos.getColumna("HORA_HASTA_ASPVH").setNombreVisual("HORA HASTA");
			tab_permisos.getColumna("HORA_HASTA_ASPVH").setVisible(false);

			tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);

			
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
			tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setNombreVisual("NRO DIAS");
			tab_permisos.getColumna("NRO_DIAS_ASPVH").setOrden(12);
			
			
			tab_permisos.getColumna("NRO_HORAS_ASPVH").setNombreVisual("NRO HORAS");
			tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
			tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
			tab_permisos.getColumna("NRO_HORAS_ASPVH").setOrden(13);
			
			tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
			tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
			tab_permisos.getColumna("IDE_GEEST").setVisible(false);
			tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
			tab_permisos.getColumna("ANULADO_ASPVH").setNombreVisual("ANULADO");
			tab_permisos.getColumna("ANULADO_ASPVH").setVisible(false);

	    	tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
			tab_permisos.getColumna("APROBADO_ASPVH").setNombreVisual("APROBADO");
			tab_permisos.getColumna("APROBADO_ASPVH").setOrden(14);

	    	tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setNombreVisual("APROBADO TTHH");
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(15);

			tab_permisos.getColumna("DETALLE_ASPVH").setNombreVisual("DETALLE");
			tab_permisos.getColumna("DETALLE_ASPVH").setOrden(11);

			tab_permisos.getColumna("FECHA_ANULA_ASPVH").setNombreVisual("FEC. ANULA");
			tab_permisos.getColumna("FECHA_ANULA_ASPVH").setVisible(false);

			tab_permisos.getColumna("RAZON_ANULA_ASPVH").setNombreVisual("RAZ�N ANULA");
			tab_permisos.getColumna("RAZON_ANULA_ASPVH").setVisible(false);
			
			tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setVisible(false);
			tab_permisos.getColumna("estado_preaprobado_aspvh").setVisible(false);
			tab_permisos.getColumna("ide_asjei").setVisible(false);
			
			//tab_permisos.onSelect(metodo);
			tab_permisos.setLectura(true);
	    	tab_permisos.getGrid().setColumns(4);
			tab_permisos.setTipoFormulario(true);
			tab_permisos.setCondicion("IDE_ASPVH=1");
			tab_permisos.dibujar();

			PanelTabla pat_panel=new PanelTabla();
			pat_panel.setPanelTabla(tab_permisos);
			pat_panel.setMensajeWarn("SOLICITUD DE PERMISOS");


			tab_tabla_detalle_vacaccion.setId("tab_tabla_detalle_vacaccion");
			tab_tabla_detalle_vacaccion.setTabla("ASI_DETALLE_VACACION", "IDE_ASDEV", 2);
			tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setCheck();
			tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setValorDefecto("TRUE");
			tab_tabla_detalle_vacaccion.getColumna("ACTIVO_ASDEV").setNombreVisual("ACTIVO");
			tab_tabla_detalle_vacaccion.getColumna("ANULADO_ASDEV").setCheck();
			tab_tabla_detalle_vacaccion.getColumna("ANULADO_ASDEV").setNombreVisual("ANULADO");
			tab_tabla_detalle_vacaccion.getColumna("IDE_ASDEV").setNombreVisual("COD DETALLE PERMISO");
			tab_tabla_detalle_vacaccion.getColumna("IDE_ASVAC").setNombreVisual("COD VACACI�N");
			tab_tabla_detalle_vacaccion.getColumna("IDE_ASPVH").setNombreVisual("COD PERMISO");
			tab_tabla_detalle_vacaccion.getColumna("FECHA_NOVEDAD_ASDEV").setNombreVisual("FECHA NOVEDAD");
			tab_tabla_detalle_vacaccion.getColumna("DIA_SOLICITADO_ASDEV").setNombreVisual("DIAS TOMADOS HASTA 31 DE MAYO");
			tab_tabla_detalle_vacaccion.getColumna("DIA_ADICIONAL_ASDEV").setNombreVisual("DIAS PENDIENTES HASTA 31 DE MAYO");
			tab_tabla_detalle_vacaccion.getColumna("DIA_DESCONTADO_ASDEV").setNombreVisual("DIAS SOLICITADO");
			tab_tabla_detalle_vacaccion.getColumna("DIA_ACUMULADO_ASDEV").setNombreVisual("DIAS RECALCULADOS");
			tab_tabla_detalle_vacaccion.getColumna("OBSERVACION_ASDEV").setNombreVisual("OBSERVACI�N");
	    	tab_tabla_detalle_vacaccion.getColumna("IDE_ASNOD").setVisible(false);
			tab_tabla_detalle_vacaccion.getColumna("IDE_ASESV").setVisible(false);
			tab_tabla_detalle_vacaccion.setLectura(true);		
			tab_tabla_detalle_vacaccion.setCondicion("IDE_ASDEV=-1");

			tab_tabla_detalle_vacaccion.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setMensajeWarn("DETALLE VACACI�N");
			pat_panel2.setPanelTabla(tab_tabla_detalle_vacaccion);
		
	

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "50%", "H");
        agregarComponente(div_division);
    }

    
    
    
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void guardar() {
		
	}




	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}



	




public Tabla getTab_permisos() {
		return tab_permisos;
	}




	public void setTab_permisos(Tabla tab_permisos) {
		this.tab_permisos = tab_permisos;
	}




	public Tabla getTab_tabla_detalle_vacaccion() {
		return tab_tabla_detalle_vacaccion;
	}




	public void setTab_tabla_detalle_vacaccion(Tabla tab_tabla_detalle_vacaccion) {
		this.tab_tabla_detalle_vacaccion = tab_tabla_detalle_vacaccion;
	}




public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}




	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}




	public AutoCompletar getAut_anio_mes() {
		return aut_anio_mes;
	}




	public void setAut_anio_mes(AutoCompletar aut_anio_mes) {
		this.aut_anio_mes = aut_anio_mes;
	}
	
		

	public SeleccionTabla getSel_mes_importarTotales() {
		return sel_mes_importarTotales;
	}




	public void setSel_mes_importarTotales(SeleccionTabla sel_mes_importarTotales) {
		this.sel_mes_importarTotales = sel_mes_importarTotales;
	}




	public SeleccionTabla getSel_anio_importarTotales() {
		return sel_anio_importarTotales;
	}




	public SeleccionTabla getSel_mes() {
		return sel_mes;
	}




	public void setSel_mes(SeleccionTabla sel_mes) {
		this.sel_mes = sel_mes;
	}




	public SeleccionTabla getSel_anio() {
		return sel_anio;
	}




	public void setSel_anio(SeleccionTabla sel_anio) {
		this.sel_anio = sel_anio;
	}




	public void setSel_anio_importarTotales(SeleccionTabla sel_anio_importarTotales) {
		this.sel_anio_importarTotales = sel_anio_importarTotales;
	}




	public void limpiar() {
		tab_permisos.limpiar();
		tab_tabla_detalle_vacaccion.limpiar();
		aut_empleado.limpiar();
		aut_anio_mes.limpiar();
		utilitario.addUpdate("tab_permisos,tab_tabla_detalle_vacaccion,aut_empleado,aut_anio_mes");// limpia y refresca el autocompletar
	}



public String empleadoNovedadHorario(String ide_gtemp,int ide_geani,int ide_gemes,String ide_asjei,String perfil){
	
	
	
	String sql="";
			
			if (ide_gtemp=="") {
			sql="SELECT  EMP.IDE_GTEMP,   "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
			+ "FROM  asi_horario_mes_empleado ASHME "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=ASHME.IDE_GTEMP "
			+ "WHERE IDE_GEMES=-1 AND IDE_GEANI=-1  "
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP ";
			}else{
			
			sql="SELECT   EMP.IDE_GTEMP, "
						+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
						+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
						+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
						+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
						+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
						+ "FROM  asi_horario_mes_empleado ASHME "
						+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=ASHME.IDE_GTEMP "
						+ "WHERE IDE_GEMES="+ide_gemes+" AND IDE_GEANI="+ide_geani+" ";
							
				if (perfil.equals("1")) {
					//sql+="and ide_asjei in("+ide_asjei+") ";
				}else {
					sql+="and ide_asjei in("+ide_asjei+") ";


				}
				sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP ";
			}
			
	

	return sql;
	
	
}
/*
public void filtrarHorarioEmpleado(SelectEvent evt){
	aut_empleado.onSelect(evt);
	
	if (aut_anio_mes.getValue()==null || aut_anio_mes.getValue().equals("") ) {
		utilitario.agregarMensajeInfo("No se puede realizar la accion", "Debe seleccionar el Año y Mes");
		return;	
	}
	
	
	
	
	
	TablaGenerica tabAnioMes=utilitario.consultar("SELECT MES.IDE_GEMES, ANIO.IDE_geani "
	  		+ "FROM asi_horario_periodo PERIODO "
	  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
	  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
	  		+ "WHERE PERIODO.ide_ashop="+aut_anio_mes.getValor());
	String anio="",mes="";
	anio=tabAnioMes.getValor("ide_geani");
	mes=tabAnioMes.getValor("ide_gemes");
	ide_geani=Integer.parseInt(anio);
	ide_gemes=Integer.parseInt(mes);
	
	aut_empleado.setId("aut_empleado");
	String str_sql_emp=empleadoNovedadHorario("1",ide_geani,ide_gemes,ide_asjei,tipo_perfil);
	aut_empleado.setAutoCompletar(str_sql_emp);		
	

	if (aut_empleado.getValue()==null || aut_empleado.getValue().equals("") ) {
		utilitario.agregarMensajeInfo("No se puede realizar la accion", "Debe seleccionar un empleado");
		return;	
	}
	tab_horario_novedades.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes+" AND notificacion_ashmn=false");
	tab_horario_novedades.ejecutarSql();
	tab_horario_novedades.actualizar();
	tab_horario.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes);
	tab_horario.ejecutarSql();
	tab_horario.actualizar();
	
	
	tab_horario_consulta.setSql("SELECT mes.ide_ashmn,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
			  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
			  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
			  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
			  + "mes.fecha_ashmn, turno.nom_astur || '' || turno.descripcion_astur as descripcion_astur "
				+ "FROM asi_horario_mes_notificaciones mes "
				+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
				+ "left join gth_empleado emp on emp.ide_gtemp=mes.ide_gtemp "
			+ "where mes.ide_geani="+ide_geani+" and mes.ide_gemes="+ide_gemes+" and mes.notificacion_ashmn=true and mes.IDE_GTEMP="+aut_empleado.getValor()+"" );
	tab_horario_consulta.ejecutarSql();
	tab_horario_consulta.actualizar();
	tab_horario_novedades.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes+" AND notificacion_ashmn=false AND ACTIVO_ASHMN=true");
	tab_horario_novedades.ejecutarSql();
	tab_horario_novedades.actualizar();
	
	utilitario.addUpdate("tab_horario,tab_horario_novedades");
}*/

  public String obtenerPeriodo(){
	  String sql="";
	  
	  sql="SELECT PERIODO.ide_ashop,MES.DETALLE_GEMES, ANIO.DETALLE_geani "
	  		+ "FROM asi_horario_periodo PERIODO "
	  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
	  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
	  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ";
	  return sql;
	  
  }
  
  public void abrirDialogoImportar() {
		//tab_planificacion_hxe.setCondicion("ide_cobph=-1");
		//tab_planificacion_hxe.ejecutarSql();
	    //tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
	    //tab_planificacion_hxe_observacion.ejecutarSql();
		//utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
			sel_mes_importarTotales.dibujar();
			//valorTotal=false;
		}
  
  
  public void getEmpleadoImportarTotales(){
    	try {
    		empleado=sel_empleado_importarTotales.getSeleccionados();
    		if (filtroActivoInactivo==false) {
				
			}
    	
    		/*TablaGenerica tab=utilitario.consultar("select EMP.APELLIDO_PATERNO_GTEMP, EMP.PRIMER_NOMBRE_GTEMP,vacacion.es_liquidacion "
    				+ "from asi_vacacion  vacacion "
    				+ "left join gth_empleado emp on emp.ide_gtemp=vacacion.ide_gtemp "
    				+ "where emp.ide_gtemp in("+empleado+") order by emp.ide_gtemp asc,vacacion.ide_asvac asc ");
    		if (tab.getTotalFilas()>0) {
    			
    			for (int i = 0; i < tab.getTotalFilas(); i++) {
					if (tab.getValor(i,"es_liquidacion")==null || tab.getValor(i,"es_liquidacion").isEmpty()) {
						
					}else {
						utilitario.agregarMensajeError("No se puede realizar esta accion", "El empleado "+tab.getValor(i,"APELLIDO_PATERNO_GTEMP")+"  "+tab.getValor(i,"PRIMER_NOMBRE_GTEMP")+" ya fue liquidado");
		    			return;					
		    			}
				}
    		
			}*/
    		
    		
    		sel_empleado_importarTotales.cerrar();

    		
    		//Validacion si no se escogen datos
    		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
    			return;
    		}else {
    			
    		int mesAsignado=Integer.parseInt(mes);
    		int anioAsignado=Integer.parseInt(anio);
  		dia_importar_total.dibujar();
			}
    		
    	} catch (NumberFormatException e) {
    		// TODO Auto-generated catch block

    	System.out.println("ERROR METODO getEmpleado()");
    	
    	}
    }
    
  
  public void obtenerMesImportarTotales(){
    	
	  mes=sel_mes_importarTotales.getValorSeleccionado();
		 if ((mes==null || mes.isEmpty() || mes.equals(""))) {
	 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
	 			return;
	 		}else {
				  sel_mes_importarTotales.cerrar();	
				    sel_anio_importarTotales.dibujar();


}

}
  //Metodo distingue si es ingreso o edicion
  public void obtenerAnioImportarTotales(){

      anio=sel_anio_importarTotales.getValorSeleccionado();
      if (anio==null || (anio.isEmpty() || anio.equals(""))) {
  			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
  			return;
  		}else {
  			sel_anio_importarTotales.cerrar();
  			dia_filtro_activo.dibujar();
  		}
	}	

  
  
  public void obtenerMes(){
	  mes=sel_mes.getValorSeleccionado();
		 if ((mes==null || mes.isEmpty() || mes.equals(""))) {
	 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
	 			return;
	 		}else {
				  sel_mes.cerrar();	
				    sel_anio.dibujar();


}

}
  //Metodo distingue si es ingreso o edicion
  public void obtenerAnio(){

      anio=sel_anio.getValorSeleccionado();
      if (anio==null || (anio.isEmpty() || anio.equals(""))) {
  			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
  			return;
  		}else {
  			sel_anio.cerrar();
  			tab_permisos.setCondicion("ide_aspvh in(select ide_aspvh from asi_detalle_vacacion where carga_batch_asdev=true) and ide_asmot=34 and ide_gemes="+mes+" and ide_geani="+anio);
  			tab_permisos.ejecutarSql();
  			tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH in(select ide_aspvh from asi_permisos_vacacion_hext where ide_asmot=34 and ide_gemes="+mes+" and ide_geani="+anio+")");
  			tab_tabla_detalle_vacaccion.ejecutarSql();
  			tab_tabla_detalle_vacaccion.actualizar();
  			
  		}
	}	

  
  
  
  
 
  public void seleccionaEmpleado(){
  			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
  			String sql="";
  			if(lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()){
				System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
				if (lis_activo.getSeleccionados().toString().equals("'true'")) {
					filtroActivoInactivo=true;	
					//utilitario.agregarMensaje("No se puede realizar esta accion", "Periodo liquidado");
					//sel_empleado_importarTotales.cerrar();
					//return;
				}else {
					filtroActivoInactivo=false;	
				}
				
				
				
				sql="SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
						"APELLIDO_PATERNO_GTEMP || ' ' || " +
						"APELLIDO_MATERNO_GTEMP || ' ' || " +
						"PRIMER_NOMBRE_GTEMP || ' ' || " +
						"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
						"ORDER BY NOMBRES ASC";
				
	  			sel_empleado_importarTotales.setSeleccionTabla(sql, "IDE_GTEMP");
				sel_empleado_importarTotales.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
	  			//sel_empleado_importarTotales.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
	  			sel_empleado_importarTotales.setTitle("Seleccione Empleados");
	  			gru_pantalla.getChildren().add(sel_empleado_importarTotales);
	  			sel_empleado_importarTotales.getBot_aceptar().setMetodo("getEmpleadoImportarTotales");
	  			sel_empleado_importarTotales.redibujar();
	  		   	utilitario.addUpdate("sel_empleado_importarTotales");
				dia_filtro_activo.cerrar();
				//sel_empleado_importarTotales.dibujar();						
  		
  			
  			/*if (tipo_perfil.equals("1")) {    				
  	
  				 
  				 
  				 
  				 
  				sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  asi_horario_mes_empleado asi "
  						+ "LEFT JOIN GTH_EMPLEADO EMP  on emp.ide_gtemp=asi.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")  and asi.ide_gemes="+mes+" and ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
 
				}else {
					
					sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
						+ "from  asi_horario_mes_empleado asi "
	  					+ "LEFT JOIN GTH_EMPLEADO EMP  on emp.ide_gtemp=asi.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")  and asi.ide_gemes="+mes+" and ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
  					 }*/

  			
  			//System.out.println(""+sql);
  			//sel_empleado_importarTotales.setSeleccionTabla(sql, "IDE_GTEMP");
  			
	
  }
  
  
  }
  
  
  
  private String getFormatoInformacion(String mensaje) {
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}

	private String getFormatoError(String mensaje) {
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}
	private String getFormatoAdvertencia(String mensaje) {
		return "<div><font color='#ffcc33'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}  
  

/**
 * Valida el archivo para que pueda importar un rubro a la nomina
 * 
 * @param evt
 */
public void validarArchivoTotal(FileUploadEvent evt) {
//	if (aut_rubros.getValor() != null) {
		// Leer el archivo
		String str_msg_info = "";
		String str_msg_adve = "";
		String str_msg_erro = "";
		double dou_tot_valor_imp = 0;

		boolean bandTabVacia=false;
		int contErrores=0; 
		
		try {
			String tipo_nom = "";
			Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
			Sheet hoja = archivoExcel.getSheet(0);// LEE LA PRIMERA HOJA
			if (hoja == null) {
				utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
				return;
			}
			int int_fin = hoja.getRows();
			ide_empleados_crear=new StringBuilder();
			upl_archivo.setNombreReal(evt.getFile().getFileName());
			str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
			
			{
			lis_importa = new ArrayList<String[]>();
			lis_turno = new ArrayList<String[]>();
			lis_empleado = new ArrayList<String[]>();

			int num_seleccionados=sel_empleado_importarTotales.getNumeroSeleccionados();
			tab_emp_total.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , "
							  		+ "0 as VALOR, "
							  		+ "'' as DESCRIPCION "
									+ "FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");	
			tab_emp_total.ejecutarSql();
			tab_emp_total.setLectura(false);
			tab_emp_total.setDibujo(false);
			int ide_gtemp=0;
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
			int x=0;
			double dou_valor = 0d;
			String dou_valor_turno="";
			int in_valor=0;
			TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
			int nuevoMes=0;
			nuevoMes=Integer.parseInt(mes);
			
			if (Integer.parseInt(mes)>9) {
				fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-01";
			}else {
				fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-01";
			}

			ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
			if (Integer.parseInt(mes)>9) {
				fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-"+ultimoDia;
			}else {
				fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-"+ultimoDia;
			}
			
			
			for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				String str_cedula = hoja.getCell(0, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				//TablaGenerica tab_empleado = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
				String tiempoDescontar=hoja.getCell(1, i).getContents();
				//Si existe empleados
				String detalleDescuento=hoja.getCell(2, i).getContents();

				TablaGenerica tab_empleado =utilitario.consultar("select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,"
						+ "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  GTH_EMPLEADO EMP  "
  						//+ " left join asi_horario_mes_empleado ashem on emp.ide_gtemp=ashem.ide_gtemp "
  						+ "WHERE emp.documento_identidad_gtemp='"+str_cedula+"' "
  						//+ "ashem.ide_gemes="+mes+" and ashem.ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
							
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos de horarios, fila " + (i + 1));
				} else {}
					
				ide_gtemp=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
				int valorHex=0;
				String ide_cobph="";
				boolean band25=false,bandNoc25=false,band50=false,band60=false,band100=false;
				bandTabVacia=false;
				// Valido que el documento sea correcto
					if (!ser_empleado1.isDocumentoIdentidadValido(utilitario.getVariable("p_gth_tipo_documento_cedula"), str_cedula)) {
						str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no es valido, fila " + (i + 1));
					} else {
						// Valido que el empleado se encuentre en la o las
						// nominas q va a importar
						String str_ide_gtemp = tab_empleado.getValor("IDE_GTEMP");
						
						
						TablaGenerica tabpartda = null;
						tabpartda = ser_empleado1.getPartidaRoles(str_ide_gtemp);
						if (tabpartda.isEmpty() == false) {
							
							
							
							
							//tab_hxe= utilitario.consultar("SELECT ide_cobph,ide_gtemp, ide_asjei "
						//			+ "FROM con_biometrico_plan_hxe ");
								//	+ "where ide_gtemp="+ide_gtemp+" and ide_geani="+anio+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")");
					//		if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
							//	str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
						//	}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene horario asginado, fila " + (i + 1));
							}				
						
					

				
				
				//for (x = 0; x <= num_seleccionados; x++) {		
			
				String str_valor = hoja.getCell(1, i).getContents();
				String str_valor_turno = hoja.getCell(2, i).getContents();

				str_valor = str_valor.replaceAll(",", ".");
				if (str_valor == null || str_valor.equals("")) {
					str_valor = "0";
				}
				dou_valor = 0.00;
				in_valor=0;
				str_valor_turno = str_valor_turno.replaceAll(",", ".");
				if (str_valor_turno == null || str_valor_turno.equals("")) {
					str_valor_turno = "";
				}
				dou_valor_turno = "";
				
				
				try {
					// Valida que sea una cantidad numerica
					dou_valor = Double.parseDouble(str_valor);
					dou_valor_turno = str_valor_turno;

					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor numerico no valido , fila " + (i + 1));
				}
	

				//if (obj_cumula == null) {
					tab_emp_total.insertar();
					tab_emp_total.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
					tab_emp_total.setValor("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
					tab_emp_total.setValor(
							"NOMBRES",
							new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ")
									.append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
									.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ")
									.append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
					lis_importa.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					lis_turno.add(new String[] { str_cedula, dou_valor_turno });

					tab_emp_total.setValor("VALOR", utilitario.getFormatoNumero(dou_valor));
					tab_emp_total.setValor("DESCRIPCION", dou_valor_turno);
					}
	
				
			}

		}
			tab_emp_total.setLectura(true);
			tab_emp_total.setDibujo(true);
	
			
			archivoExcel.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String str_resultado = "";
		if (!str_msg_info.isEmpty()) {
			str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
		}
		if (!str_msg_adve.isEmpty()) {
			str_resultado += "<strong><font color='#ffcc33'>ADVERTENCIAS</font></strong>" + str_msg_adve;
		}
		if (!str_msg_erro.isEmpty()) {
			str_resultado += "<strong><font color='#ff0000'>ERRORES</font></strong>" + str_msg_erro;
		}

		edi_mensajes.setValue(str_resultado);
		grid_tabla_emp_total.getChildren().clear();
	//	grid_tabla_emp_sum_totales.getChildren().clear();

		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp_total.getChildren().add(tab_emp_total);
		dia_valida_empleado_total.dibujar();
		

		
		
}




public TablaGenerica getTabJefeInmediato() {
	return tabJefeInmediato;
}




public void setTabJefeInmediato(TablaGenerica tabJefeInmediato) {
	this.tabJefeInmediato = tabJefeInmediato;
}




public Upload getUpl_archivo() {
	return upl_archivo;
}




public void setUpl_archivo(Upload upl_archivo) {
	this.upl_archivo = upl_archivo;
}




public SeleccionTabla getSel_empleado_importarTotales() {
	return sel_empleado_importarTotales;
}




public void setSel_empleado_importarTotales(
		SeleccionTabla sel_empleado_importarTotales) {
	this.sel_empleado_importarTotales = sel_empleado_importarTotales;
}




public Dialogo getDia_importar_total() {
	return dia_importar_total;
}




public void setDia_importar_total(Dialogo dia_importar_total) {
	this.dia_importar_total = dia_importar_total;
}




public Dialogo getDia_valida_empleado_total() {
	return dia_valida_empleado_total;
}




public void setDia_valida_empleado_total(Dialogo dia_valida_empleado_total) {
	this.dia_valida_empleado_total = dia_valida_empleado_total;
}




public Tabla getTab_emp_total() {
	return tab_emp_total;
}




public void setTab_emp_total(Tabla tab_emp_total) {
	this.tab_emp_total = tab_emp_total;
}




public Grid getGrid_tabla_emp_total() {
	return grid_tabla_emp_total;
}




public void setGrid_tabla_emp_total(Grid grid_tabla_emp_total) {
	this.grid_tabla_emp_total = grid_tabla_emp_total;
}
  
public void cancelarImportarTotal() {
	dia_valida_empleado_total.cerrar();
	dia_importar_total.cerrar();
	upl_archivo.limpiar();
	edi_mensajes.setValue("");
    //tab_planificacion_hxe.setCondicion("ide_cobph=-1");
    //tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    //tab_planificacion_hxe.ejecutarSql();
    //tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("dia_valida_empleado_total,dia_importar_total,edi_mensajes");
	
	String valor="DELETE FROM asi_horario_mes_notificaciones_historial ";
	utilitario.getConexion().ejecutarSql(valor);

}  

  

public void aceptarImportarTotal() {

	if (upl_archivo.getNombreReal() == null) {
		utilitario.agregarMensajeInfo("Debe seleccionar un archivo", "");
		return;
	}
	if (edi_mensajes.getValue() == null || edi_mensajes.getValue().toString().isEmpty()) {
		utilitario.agregarMensajeInfo("Debe validar el archivo", "");
		return;
	} else {
		if (edi_mensajes.getValue().toString().indexOf("#ff0000") > 0) {
			utilitario.agregarMensajeInfo("Debe solucionar los errores del archivo", "");
			return;
		}
	}
	
	String emp="";
	TablaGenerica tab_empleado=null;
	String validacionEmpleado="";
	String valor="";
	/*if (ide_empleados_crear.length()==0) {
		
	}else {
		validacionEmpleado=ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length());
		if (validacionEmpleado.equals(",")) {
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
		
		}else {
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear+")");					
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear+")");
		}
	}*/
	
	
	
	
	/*tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
	 TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
	 			+ "PERIODO.ide_ashop,"
	 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
	 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
		  		+ "FROM asi_horario_periodo PERIODO "
		  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
		  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
		  		+ "WHERE ANIO.ide_GEANI="+anio+" AND MES.IDE_GEMES="+mes
		  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
	String fecha_inicio="",fecha_fin="";
	int mesValida=0,diaValida=0;

if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
if ((int)Double.parseDouble(tab_emp_total.getValor(i,"DIA").toString())<10) {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}else {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}
}else {
if ((int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"))<10) {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}else {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}

}

	utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_notificaciones set activo_ashmn=false WHERE IDE_GTEMP="+tab_emp_total.getValor(i,"ide_gtemp")+" AND fecha_ashmn='"+fecha_inicio+"' and ide_gemes="+mes+" and ide_geani="+anio+" and activo_ashmn=true");				

}

	for (int x = 0; x < tab_emp_total.getTotalFilas(); x++) {
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_horario_mes_notificaciones", "ide_ashmn"));
		String codigo=tab_codigo.getValor("codigo");
		
	/*	TablaGenerica tablaRegistro=null;
		 tablaRegistro=utilitario.consultar("SELECT ide_ashmn, ide_gtemp, fecha_ashmn, ide_astur, notificacion_ashmn, "
				+ "ide_usua, activo_ashmn, ide_geani, ide_gemes, ide_asjei "
				+ "FROM asi_horario_mes_notificaciones "
				+ "where fecha_ashmn='"+tab_emp_total.getValor("fecha_ashmn")+"' and ide_gtemp="+tab_emp_total.getValor("ide_gtemp")+" and notificacion_ashmn=false and activo_ashmn=true "
				+ "order by ide_ashmn");*/

		/* TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
		 			+ "PERIODO.ide_ashop,"
		 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
		 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
			  		+ "FROM asi_horario_periodo PERIODO "
			  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
			  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
			  		+ "WHERE ANIO.ide_GEANI="+anio+" AND MES.IDE_GEMES="+mes
			  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
		String fecha_inicio="",fecha_fin="";
		int mesValida=0,diaValida=0;

	if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
	if ((int)Double.parseDouble(tab_emp_total.getValor(x,"DIA").toString())<10) {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}else {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}
	}else {
	if ((int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"))<10) {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}else {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}

	}
		
		
		
	valor="INSERT INTO asi_horario_mes_notificaciones( "
			+ "ide_ashmn, ide_gtemp, fecha_ashmn, ide_astur, notificacion_ashmn, "
			+ "ide_usua, activo_ashmn, usuario_ingre, fecha_ingre, usuario_actua, " 
			+ "fecha_actua, hora_ingre, hora_actua, ide_geani, ide_gemes, ide_asjei)   "
			+ "VALUES ("+codigo+", "+tab_emp_total.getValor(x,"ide_gtemp")+", '"+fecha_inicio+"', "+0+", "+false+", "
			+ "'"+utilitario.getVariable("ide_usua")+"', "+true+", '"+utilitario.getVariable("ide_usua")+"', '"+utilitario.getFechaActual()+"', "+null+", ";
			if (tipo_perfil.equals("1")) {
				valor+=	""+null+", '"+utilitario.getHoraActual()+"', "+null+", "+anio+", "+mes+", "+tabJefeInmediato.getValor("ide_asjei")+")";
			}else {
				valor+=	""+null+", '"+utilitario.getHoraActual()+"', "+null+", "+anio+", "+mes+", "+ide_asjei+")";
			}
			ide_empleados.append(codigo+",");

	utilitario.getConexion().ejecutarSql(valor);
	}

	
*/
	int mes=0;
		mes=utilitario.getMes(fecha);
		int anioTemp=0;
		anioTemp=utilitario.getAnio(fecha);
		//TablaGenerica tab_Anio=utilitario.consultar("select ide_geani, detalle_geani from gen_anio where detalle_geani like '%"+anioTemp+"%'");
		//int anio=Integer.parseInt(tab_Anio.getValor("IDE_GEANI"));
		String fec_ini="";
		if (mes>9) {
			fec_ini=anioTemp+"-"+mes+"-01";
		}else {
			fec_ini=anioTemp+"-0"+mes+"-01";
		}
	
	TablaGenerica tablaRegistro=utilitario.consultar("SELECT ide_geedp,ide_gtemp "
			+ "from gen_empleados_departamento_par "
			+ "where ide_gegro in(4) and ide_gedep in(95) and activo_geedp=true "
			+ "order by ide_geedp desc limit 1");
	String gen_ide_geedp=tablaRegistro.getValor("ide_geedp");
	
	if (tab_emp_total.getTotalFilas()>0) {
		for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
		//	eliminarNotificacion(tablaRegistro.getValor(i,"ide_gtemp"), tablaRegistro.getValor(i,"fecha_ashmn"), ide_gemes, ide_geani);	
			//importarValoresRubro(lis_importa,1,i,tab_emp_total.getValor(i,"ide_gtemp"),lis_turno);
			importarValoresRubro(lis_importa, tab_emp_total.getValor(i, "ide_gtemp"), lis_turno, gen_ide_geedp, gen_ide_geedp, gen_ide_geedp, fecha, fecha,fec_ini,tab_emp_total.getValor(i, "documento_identidad_gtemp"));
			//importarValoresRubro(lis_turno,2,i,tab_emp_total.getValor(i,"ide_gtemp"));
			}
		
	}

	if (ide_empleados_crear==null || ide_empleados_crear.equals("") || ide_empleados_crear.length()==0) {
		tab_permisos.setCondicion("IDE_ASPVH=-1 ");
		tab_permisos.ejecutarSql();
		tab_permisos.actualizar();
		tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH=-1");
		tab_tabla_detalle_vacaccion.ejecutarSql();
		tab_tabla_detalle_vacaccion.actualizar();
	}else{
	tab_permisos.setCondicion("IDE_ASPVH in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
	tab_permisos.ejecutarSql();
	tab_permisos.actualizar();
	tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
	tab_tabla_detalle_vacaccion.ejecutarSql();
	tab_tabla_detalle_vacaccion.actualizar();
	utilitario.agregarMensaje("Valores Importados con exito", "Se ha importado los valores correctamente");
	}
	utilitario.addUpdate("tab_permisos,tab_tabla_detalle_vacaccion");
	
	
	
	
	dia_importar_total.cerrar();
	dia_valida_empleado_total.cerrar();

}
  




public boolean importarValoresRubro(List lis_importa,String ide_gtemp,List lis_turno,String gen_ide_geedp,String gen_ide_geedp2,String gen_ide_geedp3,String fecha_desde, String fecha_hasta,String fec_ini,String DOCUMENTO_IDENTIDAD_GTEMP){

	
	//	eliminarNotificacion(tablaRegistro.getValor(i,"ide_gtemp"), tablaRegistro.getValor(i,"fecha_ashmn"), ide_gemes, ide_geani);	

	
/*	String str_sql="";
	str_sql="SELECT cbph.ide_ashmn,  EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   anio.detalle_geani,mes.detalle_gemes  " //,cbph.ide_asjei
			+ "FROM asi_horario_mes_notificaciones cbph  "
			+ "left join gth_empleado emp on emp.ide_gtemp=cbph.ide_gtemp   "
			+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani  "
			+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
			+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+anio+" and cbph.ide_gtemp in("+ide_gtemp+")  " 
			+ " order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP";
			//ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length()
	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);
	//jefe_inmediato_planificacion
	 **/
	String str_valor=null,str_descripcion=null;

	//for (int j = 0; j < tab_emp_total.getTotalFilas(); j++) {	
		String str_documento=DOCUMENTO_IDENTIDAD_GTEMP;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				lis_importa.remove(k);
				break;
			}
		}		
		
		for (int k = 0; k < lis_turno.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_turno.get(k))[0])){
				str_descripcion=((String[])lis_turno.get(k))[1];
				lis_turno.remove(k);
				break;
			}
		}		
		
	//}
		
		//508,81,976,1304
		TablaGenerica tab_acciones= utilitario.consultar("select * from asi_vacacion where ide_gtemp="+ide_gtemp+" order by ide_asvac asc");	
		TablaGenerica tab_acciones2= utilitario.consultar("select * from asi_vacacion where ide_gtemp="+ide_gtemp+" order by ide_asvac asc");	
		String fec_in="",fec_fin="";
    	Calendar calFecha = Calendar.getInstance();
       	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
    	Calendar calFechaNocturno = Calendar.getInstance();
		Calendar calFechaSiguienteDia = Calendar.getInstance();	
		int ide_asvac1=0;
		boolean bandInicio=false;
		for (int i = 0; i < tab_acciones.getTotalFilas(); i++) {
			
			for (int j = 0; j < tab_acciones2.getTotalFilas(); j++) {
				if (j==0) {
					if (tab_acciones.getValor(i,"ide_asvac").equals(tab_acciones2.getValor(j,"ide_asvac"))) {
						if (tab_acciones.getValor(j,"es_continuidad")==null ||  tab_acciones.getValor(j,"es_continuidad").equals("") ||  tab_acciones.getValor(j,"es_continuidad").equals("false") ||  tab_acciones.getValor(j,"es_continuidad").isEmpty()){//1.
							fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							if (tab_acciones2.getValor(j, "fecha_finiquito_asvac")==null || tab_acciones2.getValor(j, "fecha_finiquito_asvac").equals("") || tab_acciones2.getValor(j, "fecha_finiquito_asvac").isEmpty()) {
								fec_fin=utilitario.getFechaActual();
							}else{
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							}
							calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
							calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
							calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
					   		
					   		
							if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
								ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
								i=tab_acciones.getTotalFilas();
								j=tab_acciones2.getTotalFilas();
								break;
							}else {
									bandInicio=false;
									//fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							}
						}//1. es continuidad
						else{
							bandInicio=true;
							fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							
							//*********************
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							
							calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
							calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
							calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
					   		
					   		
							if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
								ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
								i=tab_acciones.getTotalFilas();
								j=tab_acciones2.getTotalFilas();
								break;
							}else {
									//fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							}
							
							
							
							//guardar ide_asvac
						}
						
					}
				}else {//j>0					
					//if (tab_acciones.getValor(j,"es_continuidad").equals("true")) {
						if (tab_acciones.getValor(j,"es_continuidad")==null ||  tab_acciones.getValor(j,"es_continuidad").equals("") ||  tab_acciones.getValor(j,"es_continuidad").equals("false") ||  tab_acciones.getValor(j,"es_continuidad").isEmpty()){//1.

						if (bandInicio==false) {
							fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							if (tab_acciones2.getValor(j, "fecha_finiquito_asvac")==null || tab_acciones2.getValor(j, "fecha_finiquito_asvac").equals("") || tab_acciones2.getValor(j, "fecha_finiquito_asvac").isEmpty()) {
								fec_fin=utilitario.getFechaActual();
							}else{
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							}
							//fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
							calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
							calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
					   		
					   		
							if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
								ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
								i=tab_acciones.getTotalFilas();
								j=tab_acciones2.getTotalFilas();
								break;
							}else {
								bandInicio=false;
								//fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");

							}
							
							
							
						}else {
							if (tab_acciones2.getValor(j, "fecha_finiquito_asvac")==null || tab_acciones2.getValor(j, "fecha_finiquito_asvac").equals("") || tab_acciones2.getValor(j, "fecha_finiquito_asvac").isEmpty()) {
								fec_fin=utilitario.getFechaActual();
							}else{
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							}
							//fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
							calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
							calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
					   		
					   		
							if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
								ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
								i=tab_acciones.getTotalFilas();
								j=tab_acciones2.getTotalFilas();
								break;
							}else{bandInicio=false;}
						}
					}else {

						if (bandInicio==false) {
							fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							bandInicio=true;
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
						
						calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
						calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
						calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
				   		
				   		
						if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
							ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
							i=tab_acciones.getTotalFilas();
							j=tab_acciones2.getTotalFilas();
							break;
						}else {
								//fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
						}
							
							
							
						}else {
							
							fec_fin=tab_acciones2.getValor(j, "fecha_finiquito_asvac");
							
							calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
							calFechaInicio.setTime(getFechaAsyyyyMMdd(fec_in));
							calFechaFin.setTime(getFechaAsyyyyMMdd(fec_fin));
					   		
					   		
							if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
								ide_asvac1=Integer.parseInt(tab_acciones2.getValor(j,"ide_asvac"));
								i=tab_acciones.getTotalFilas();
								j=tab_acciones2.getTotalFilas();
								break;
							}else {
									//fec_in=tab_acciones2.getValor(j, "fecha_ingreso_asvac");
							}
							
							
							
							
						}
					}//Es continuidad
				
						
						
						
						
				}
			}
		}
		
		
		
		
		
    	//Calendar calFecha = Calendar.getInstance();
       	//Calendar calFechaInicio = Calendar.getInstance();
    	//Calendar calFechaFin = Calendar.getInstance();
    	//Calendar calFechaNocturno = Calendar.getInstance();
		//Calendar calFechaSiguienteDia = Calendar.getInstance();	
		String ide_aspvh="",fecha_ingreso_asvac="",fecha_finiquito_asvac="";
		//Metodo Insertar Resumen
		if(str_valor!=null){
		String[] parts = str_valor.split("\\.");
			int part1 = Integer.parseInt(parts[0]); // 123
			double part2 =Double.parseDouble("0."+parts[1]); // -654321
			int ide_asvac=0;
//	TablaGenerica tabPeriodoVacacion= utilitario.consultar("select * from asi_vacacion where ide_gtemp="+ide_gtemp+" order by ide_asvac asc");	
//	TablaGenerica tabPeriodoVacacion= utilitario.consultar("select * from asi_vacacion where ide_gtemp="+ide_gtemp+" order by ide_asvac desc limit 1");	

	
/*	for (int i = 0; i < tabPeriodoVacacion.getTotalFilas(); i++) {
		fecha_ingreso_asvac=tabPeriodoVacacion.getValor(i, "fecha_ingreso_asvac");
		if (tabPeriodoVacacion.getValor(i, "fecha_finiquito_asvac")==null || tabPeriodoVacacion.getValor(i, "fecha_finiquito_asvac").equals("") || tabPeriodoVacacion.getValor(i, "fecha_finiquito_asvac").isEmpty()) {
			fecha_finiquito_asvac=utilitario.getFechaActual();
		}else{
		fecha_finiquito_asvac=tabPeriodoVacacion.getValor(i, "fecha_finiquito_asvac");}
		//Setteo la fecha y hora timbrada en el biometrico de tipo calendario
		calFecha.setTime(getFechaAsyyyyMMdd(fec_ini));
		calFechaInicio.setTime(getFechaAsyyyyMMdd(fecha_ingreso_asvac));
		calFechaFin.setTime(getFechaAsyyyyMMdd(fecha_finiquito_asvac));
   		
   		
		if (calFechaInicio.compareTo(calFecha) <= 0 && calFecha.compareTo(calFechaFin)<=0){
			ide_asvac=Integer.parseInt(tabPeriodoVacacion.getValor("ide_asvac"));
			break;
		}
	}
	*/
	
	TablaGenerica tab_mes=	utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
	TablaGenerica tab=null;
	if (filtroActivoInactivo==true) {
		tab=utilitario.consultar("select ide_asvac,es_liquidacion from asi_vacacion where ide_gtemp="+ide_gtemp+" and activo_asvac=true order by ide_asvac desc");
		if (tab.getTotalFilas()>0) {
			ide_asvac1=Integer.parseInt(tab.getValor("ide_asvac"));
		}
	}else {	
		tab=utilitario.consultar("select ide_asvac,es_liquidacion from asi_vacacion where ide_asvac="+ide_asvac1+" order by ide_asvac desc");
	}
	
		
	if (ide_asvac1==0) {
		utilitario.agregarMensaje("No existe periodo de vacacion valido", ""+retornaDatosCorreoEmpleado(Integer.parseInt(ide_gtemp)));
	}else{
	if (part1>0) {
	if (tab.getValor("es_liquidacion")==null || tab.getValor("es_liquidacion").equals("") || tab.getValor("es_liquidacion").isEmpty() || tab.getValor("es_liquidacion").equals("false")) {
		ide_aspvh=insertarAsignacionMensualHorario(Integer.parseInt(anio), Integer.parseInt(mes), ide_gtemp, gen_ide_geedp, gen_ide_geedp2, gen_ide_geedp3, fecha_desde, fecha_hasta, str_descripcion,part1,part2);
		insertardetalleVacacion(ide_asvac1, ide_aspvh, Double.parseDouble(str_valor),str_descripcion);
		Correo(Integer.parseInt(ide_gtemp), str_descripcion+" se le ha cargado "+str_valor+" dia(s)  a vacacion para el mes de "+ucFirst(tab_mes.getValor("detalle_gemes"))+" "+utilitario.getAnio(fecha)+"");
		ide_empleados_crear.append(ide_aspvh+",");

	}else {
		utilitario.agregarMensajeError("Empleado se encuentra liquidado", "No se puede realizar esta accion");
		dia_importar_total.cerrar();
		dia_valida_empleado_total.cerrar();
		tab_permisos.setCondicion("IDE_ASPVH=-1 ");
		tab_permisos.ejecutarSql();
		tab_permisos.actualizar();
		tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH=-1");
		tab_tabla_detalle_vacaccion.ejecutarSql();
		tab_tabla_detalle_vacaccion.actualizar();
		ide_empleados_crear=null;
		
	}

	
	
	}else {
		if (tab.getValor("es_liquidacion")==null || tab.getValor("es_liquidacion").equals("") || tab.getValor("es_liquidacion").isEmpty() || tab.getValor("es_liquidacion").equals("false")) {
			ide_aspvh=insertarAsignacionMensualHorario(Integer.parseInt(anio), Integer.parseInt(mes), ide_gtemp, gen_ide_geedp, gen_ide_geedp2, gen_ide_geedp3, fecha_desde, fecha_hasta, str_descripcion,part1,part2);
			insertardetalleVacacion(ide_asvac1, ide_aspvh, Double.parseDouble(str_valor),str_descripcion);
			Correo(Integer.parseInt(ide_gtemp), str_descripcion+" se le ha cargado "+str_valor+" dia(s)  a vacacion para el mes de "+ucFirst(tab_mes.getValor("detalle_gemes"))+" "+utilitario.getAnio(fecha)+"");
			ide_empleados_crear.append(ide_aspvh+",");

		}else{
		utilitario.agregarMensajeError("Empleado se encuentra liquidado", "No se puede realizar esta accion");	
		dia_importar_total.cerrar();
		dia_valida_empleado_total.cerrar();
		tab_permisos.setCondicion("IDE_ASPVH=-1 ");
		tab_permisos.ejecutarSql();
		tab_permisos.actualizar();
		tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH=-1");
		tab_tabla_detalle_vacaccion.ejecutarSql();
		tab_tabla_detalle_vacaccion.actualizar();
		ide_empleados_crear=null;
		}
			}	

		}
			
	
}
			/*
		

			
		}*/
	//}
	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;

}


private Object[] getAcumulado(String documento) {
	for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
		if (tab_emp_total.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP").equalsIgnoreCase(documento)) {
			Object[] obj = new Object[5];
			obj[0] = i;
			obj[1] = tab_emp_total.getValor(i, "DIA");
			obj[2] = tab_emp_total.getValor(i, "TURNO");

			for (int k = 0; k < lis_importa.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[3] = k;
					break;
				}
			}
			
			for (int k = 0; k < lis_turno.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_turno.get(k))[0])) {
					obj[4] = k;
					break;
				}
			}

			return obj;
		}
	}
	return null;
}


 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}

 	


	public Confirmar getCon_guardar() {
		return con_guardar;
	}




	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}




	public StringBuilder getIde_empleados() {
		return ide_empleados;
	}




	public void setIde_empleados(StringBuilder ide_empleados) {
		this.ide_empleados = ide_empleados;
	}
		
	
	
	 public void eliminarNotificacion(String ide_gtemp,String fecha, int ide_gemes,int ide_geani){
		 
utilitario.getConexion().ejecutarSql("DELETE FROM asi_horario_mes_notificaciones WHERE IDE_GTEMP="+ide_gtemp+" AND fecha_ashmn='"+fecha+"' and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani);				

		 
	 }
	 
	 
		public String diaSemana (String fecha)
	    {
		 
		 int dia=utilitario.getDia(fecha);
		 int mes=utilitario.getMes(fecha);
		 int anioEscogido=utilitario.getAnio(fecha);
	    	String mesMenor="";
	        int Valor_dia=0;
	        String diaMenor="";
	        String retornoDia="";
	        if (dia<10) {
				diaMenor="0";
			}else {
				diaMenor="";
			}
	        
	        if (mes<10) {
				mesMenor="0";
			}else {
				mesMenor="";
			}
	        

			String fechaHoraBiometrico=anioEscogido+"-"+mesMenor+mes+"-"+diaMenor+dia;
		    //Calendario 
			Calendar c = Calendar.getInstance();
		    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
			c.setTime(getFechaAsyyyyMMdd(fechaHoraBiometrico)); 
	    	
	    	int diaSemana = c.get(Calendar.DAY_OF_WEEK);
	        if (diaSemana == 1) {
	           // Valor_dia = "Domingo";
	            Valor_dia = 1;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -6));

	        } else if (diaSemana == 2) {
	           // Valor_dia = "Lunes";
	            Valor_dia = 2;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -0));

	        } else if (diaSemana == 3) {
	           // Valor_dia = "Martes";
	            Valor_dia = 3;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -1));

	        } else if (diaSemana == 4) {
	           // Valor_dia = "Miercoles";
	            Valor_dia = 4;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -2));
 
	        } else if (diaSemana == 5) {
	           // Valor_dia = "Jueves";
	            Valor_dia = 5;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -3));

	        } else if (diaSemana == 6) {
	          // Valor_dia = "Viernes";
	            Valor_dia = 6;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -4));

	        } else if (diaSemana == 7) {
	            //Valor_dia = "Sabado";
	            Valor_dia = 7;
	            retornoDia=	utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -5));

	        }
	        return retornoDia;
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
		 
		 
	
		 

public String  insertarAsignacionMensualHorario(int ide_geani, int ide_gemes, String ide_gtemp, String gen_ide_geedp,String gen_ide_geedp2,String gen_ide_geedp3,String fecha_desde,
		String fecha_hasta,String motivo,int dias,double horas){
	
	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_permisos_vacacion_hext", "ide_aspvh"));
	String codigo=tab_codigo.getValor("codigo");
	TablaGenerica tabUsuario=utilitario.consultar("select ide_usua,nick_usua,ide_gtemp from sis_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
	TablaGenerica tabSucuAreaDept=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EPAR.IDE_GTEMP "
			+ "from GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
			+ "where epar.ide_gtemp="+ide_gtemp+" " 
			+ "	ORDER BY EPAR.IDE_GEEDP DESC LIMIT 1");
			
	
	TablaGenerica tabAccion=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EPAR.IDE_GTEMP "
			+ "from GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
			+ "where epar.ide_gtemp="+tabUsuario.getValor("ide_gtemp")+" " 
			+ "	ORDER BY EPAR.IDE_GEEDP DESC LIMIT 1");
	
	
	utilitario.getConexion().ejecutarSql("INSERT INTO asi_permisos_vacacion_hext( "
			+ "ide_aspvh, "
			+ "ide_asmot, "
			+ "ide_gtemp, "
			+ "ide_geedp, "
			+ "gen_ide_geedp, "
			+ "gen_ide_geedp2, "
			+ "ide_sucu, "
			+ "ide_gemes, "
			+ "ide_geani, "
			+ "gen_ide_geedp3, "
			+ "ide_geest, "
			+ "fecha_solicitud_aspvh, "
			+ "fecha_desde_aspvh, "
			+ "fecha_hasta_aspvh, "
			+ "detalle_aspvh, "
			+ "nro_dias_aspvh, "
			+ "tipo_aspvh, "
			+ "nro_horas_aspvh, "
			+ "activo_aspvh, "
			+ "usuario_ingre, "
			+ "fecha_ingre, "
			+ "hora_ingre, "
			+ "aprobado_aspvh, "
			+ "registro_novedad_aspvh, "
			+ "aprobado_tthh_aspvh) "

			+" values( " +codigo + ", "
		  		+ ""+ 34+", "
		  		+ ""+ide_gtemp+", "
		  		+ ""+tabSucuAreaDept.getValor("ide_geedp")+", "
		  		+ ""+gen_ide_geedp+", "
		  		+ ""+gen_ide_geedp2+", "
		  		+ ""+null+", "
		  		+ ""+ide_gemes+", "
		  		+ ""+ide_geani+", "
		  		+ ""+tabAccion.getValor("ide_geedp")+", "
		  		+ ""+null+", "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ "'"+fecha_desde+"', "
		  		+ "'"+fecha_hasta+"', "
		  		+ "'"+motivo+"', "
		  		+ ""+dias+", "
		  		+ ""+6+", "
		  		+ ""+horas+", "
		  		+ ""+true+", "
		  		+ "'"+tabUsuario.getValor("nick_usua")+"', "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ "'"+utilitario.getHoraActual()+"', "
		  		+ ""+true+", "
		  		+ ""+true+", "
		  		+ ""+true+")");		  		
	
	return codigo;

}
		 
		 

public void insertardetalleVacacion(int ide_asvac, String ide_aspvh, double valor,String descripcion){
	
	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
	String codigo=tab_codigo.getValor("codigo");
	TablaGenerica tabUsuario=utilitario.consultar("select ide_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
	
	utilitario.getConexion().ejecutarSql("INSERT INTO asi_detalle_vacacion( "
			+ "ide_asdev, "
			+ "ide_asesv, "
			+ "ide_asnod, "
			+ "ide_aspvh, "
			+ "ide_asvac, "
			+ "fecha_novedad_asdev,"
			+ "dia_solicitado_asdev, "
			+ "dia_acumulado_asdev, "
			+ "dia_adicional_asdev, "
			+ "dia_descontado_asdev, "
			+ "observacion_asdev, "
			+ "anulado_asdev, "
			+ "activo_asdev, "
			+ "usuario_ingre, "
			+ "fecha_ingre, "
			+ "hora_ingre, "
			+ "usuario_actua, "
			+ "fecha_actua,"
			+ "hora_actua, "
			+ "fin_semana_asdev,"
			+ "carga_batch_asdev)"
			+" values( " +codigo + ", "
		  		+ ""+ null+", "
		  		+ ""+null+", "
		  		+ ""+ide_aspvh+", "
		  		+ ""+ide_asvac+", "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+valor+", "
		  		+ "'"+descripcion+"', "
		  		+ ""+false+", "
		  		+ ""+true+", "
		  		+ "'"+tabUsuario.getValor("nick_usua")+"', "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ "'"+utilitario.getHoraActual()+"', "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+false+", "
		  		+ ""+true+")");		  		

}
		 		 
		 
public void consultarPermisos(){
	sel_mes.dibujar();
}
		 
		 
public void Correo(int ide_gtemp,String detallePermiso) {
	try {
		TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, " + "clave_corr from sis_correo where ide_corr=2");
		boolean bandCorreoAviso=false;
		String smtp_correo = tab_correo_envio.getValor("smtp_corr");
		String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
		String correo_envio = tab_correo_envio.getValor("correo_corr");
		String usuario_envio = tab_correo_envio.getValor("usuario_corr");
		String clave_correo = tab_correo_envio.getValor("clave_corr");
		String detalleAspvh="",strNombreEmpleado = "";

		EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

		TablaGenerica tab_correo = utilitario.consultar("select ide_gtemp,detalle_gtcor "
				+ "from gth_correo  "
				+ "where ide_gtemp=" + ide_gtemp+" "
				+ "and activo_gtcor=true and notificacion_gtcor=true");
		String correo = "juan.ayerve@emgirs.gob.ec";//tab_correo.getValor("detalle_gtcor");
		strNombreEmpleado = retornaDatosCorreoEmpleado(ide_gtemp);
		System.out.println(detallePermiso);

		try {
			envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
			envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado, detallePermiso, utilitario.getFechaActual()));
			envMail.setPara(correo);

			if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
			}
			else
				utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
			
		} catch (Exception e) {
			utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
			System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
		}

	

	} catch (Exception e) {
		System.out.println("Ha ocurrido un error al enviar el email()" + e.getMessage()); 
		utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email", "");
	}

}

public static String ucFirst(String str) {
    if (str.isEmpty()) {
        return str;            
    } else {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1); 
    }
}	

public String retornaDatosCorreoEmpleado(int IDE_GTEMP) {
	String detallePermiso = "";
	String nombreTipoSolicitud;
	String nroHoras;
	String motivo;
	String obtieneNroDias;
	// Estructura de mensaje
	String strNombreEmpleado = "";
	// obtengo el empleado del cual requiero los datos
	TablaGenerica tab_empleado = ser_empleado1.getEmpleado(""+IDE_GTEMP);
	String documento = tab_empleado.getValor("documento_identidad_gtemp");
	String primer_nombre_empleado = tab_empleado.getValor("primer_nombre_gtemp").toString();
	String segundo_nombre_empleado = tab_empleado.getValor("segundo_nombre_gtemp").toString();
	String apellido_paterno_empleado = tab_empleado.getValor("apellido_paterno_gtemp").toString();
	String apellido_materno_empleado = tab_empleado.getValor("apellido_materno_gtemp").toString();
	strNombreEmpleado = ucFirst(primer_nombre_empleado) + " " + ucFirst(segundo_nombre_empleado) + " " + ucFirst(apellido_paterno_empleado) + "  " + ucFirst(apellido_materno_empleado);
	return strNombreEmpleado;

}

	
public String emailNotificacionAprobado(String strNombreEmpleado, String detallePermiso, String fecha_solicitud_aspvh) {
	String html = "<p>Estimado/a   "+strNombreEmpleado+", " + "<p>&nbsp;</p>\n" + "<p>Notificamos mediante la presente que de acuerdo:  " + detallePermiso + "  .</p>\n"
			+ "<p>&nbsp;</p>\n" + "<p>Saludos cordiales,</p>\n" + "<table style=\"height: 144px;\" width=\"571\">\n" + "<tbody>\n" + "<tr>\n"
			+ "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n" + "<td width=\"476\">\n"
			+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">" + " </p>\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
			+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
			+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. R�o Amazonas</strong></p>\n" + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
			+ "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n" + "</td>\n" + "</tr>\n" + "</tbody>\n" + "</table>";
	return html;
}


public void filtrarAnticiposEmpleado(SelectEvent evt){
	aut_empleado.onSelect(evt);
	tab_permisos.setCondicion("TIPO_ASPVH=6 AND IDE_GTEMP="+aut_empleado.getValor());
	tab_permisos.ejecutarSql();
	
	tab_tabla_detalle_vacaccion.setCondicion("IDE_ASPVH in(select ide_aspvh from asi_permisos_vacacion_hext where tipo_aspvh=6 and ide_gtemp="+aut_empleado.getValor()+")");
	tab_tabla_detalle_vacaccion.ejecutarSql();
	utilitario.addUpdate("tab_permisos,tab_tabla_detalle_vacaccion");

}

}
