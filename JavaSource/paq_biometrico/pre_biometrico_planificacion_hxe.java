/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;

/**
 *
 * @author DELL-USER
 */
public class pre_biometrico_planificacion_hxe extends Pantalla {
	
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
    @EJB
    private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	//TABLA
	//////////////////////////////////////////
    private Tabla tab_planificacion_hxe = new Tabla();
    private Tabla tab_planificacion_hxe_observacion = new Tabla();
    private Tabla tab_resumen_hxe= new Tabla();

	TablaGenerica tabEmpleadoXJefeInmediato;
	TablaGenerica tabCargoJefeInmediato;

	//VARIABLES
	//////////////////////////////////////////////////////////////
	StringBuilder str_ide_empleado_asignado=new StringBuilder();
    private Boolean bandEdit=false;
    int asignar =0,editarValidacion=0;
    private String meses="",anios="",mesEditar="",anioEditar="",mes="",anio="",empleado="",jefe_inmediato_planificacion="",tipo_perfil="",empleado_importar="",NombreEmpleado="";
    private Boolean num_copias=false,bandAsignar=false;
    //COMPONENTES
    ////////////////////////////////////////////////////////////    
    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_anio_editar= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    private SeleccionTabla sel_empleado= new SeleccionTabla();
    private SeleccionTabla sel_empleado_editar= new SeleccionTabla();
    private SeleccionTabla sel_empleado_importar= new SeleccionTabla();
    private SeleccionTabla sel_empleado_importarTotales= new SeleccionTabla();

    private SeleccionTabla sel_mes_editar= new SeleccionTabla();
    private SeleccionTabla sel_mes_importar= new SeleccionTabla();
    private SeleccionTabla sel_mes_importarTotales= new SeleccionTabla();

    private SeleccionTabla sel_anio_importar= new SeleccionTabla();
    private SeleccionTabla sel_anio_importarTotales= new SeleccionTabla();

    private SeleccionTabla sel_mes_reporte= new SeleccionTabla();
    private SeleccionTabla sel_anio_reporte= new SeleccionTabla();

/////////////////////////////////////////////////////////////////////////////    
	private Reporte rep_reporte = new Reporte();
	private Map p_parametros = new HashMap();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Confirmar con_guardar=new Confirmar();
   //////////////////////////////////////////////////////////////////////
	private  ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	private  ArrayList<Integer> listaAnio = new ArrayList<Integer>();
	private  ArrayList<Integer> listaMes = new ArrayList<Integer>();
	/////////////////////////////////////////////////////////////////////
	private Dialogo dia_importar = new Dialogo();
	private Dialogo dia_importar_total = new Dialogo();

    private SeleccionTabla sel_importar= new SeleccionTabla();
	private Upload upl_archivo = new Upload();
	private Upload upl_archivo_por_rubro = new Upload();

	private Check che_todas_nominas = new Check();
	private Editor edi_mensajes = new Editor();
	private Etiqueta eti_num_nomina = new Etiqueta();
	private Etiqueta eti_25N = new Etiqueta();
	private Etiqueta eti_60 = new Etiqueta();
	private Etiqueta eti_25 = new Etiqueta();
	private Etiqueta eti_50 = new Etiqueta();
	private Etiqueta eti_100 = new Etiqueta();

	private List<String[]> lis_importa = null; // Guardo los empleados y el
	private List<String[]> lis_importa60 = null; // Guardo los empleados y el
	private List<String[]> lis_importa25 = null; // Guardo los empleados y el
	private List<String[]> lis_importa50 = null; // Guardo los empleados y el
	private List<String[]> lis_importa100 = null; // Guardo los empleados y el


	private AutoCompletar aut_rubros = new AutoCompletar();
	private Dialogo dia_valida_empleado = new Dialogo();
	private Dialogo dia_valida_empleado_total = new Dialogo();

	private Grid grid_tabla_emp = new Grid();
	private Grid grid_tabla_emp_total = new Grid();
	private Grid grid_tabla_emp_sum_totales = new Grid();
	private Grid grid_tabla_emp_sum_totales25Loep = new Grid();
	private Grid grid_tabla_emp_sum_totales60 = new Grid();
	private Grid grid_tabla_emp_sum_totales25 = new Grid();
	private Grid grid_tabla_emp_sum_totales50 = new Grid();
	private Grid grid_tabla_emp_sum_totales100 = new Grid();

	private Tabla tab_emp = new Tabla();
	private Tabla tab_emp_total = new Tabla();
	String ide_gtempxx="";
	int hxe25=332;
	int hxeN25=391;
	int	hxe50=245;
	int	hxe100=237;
	int	hxe60=390;
	String ide_geare="",ide_gtemp_jefe_inmediato="",cargo_elaborado="",area="";
	String carpeta="hxe/planificacion";
	int tipo_hora_extra=0,x=0;
	boolean valorTotal=false;

    public pre_biometrico_planificacion_hxe() {
	
		utilitario.agregarNotificacionInfo("NOTA - ENVIO DE PLANIFICACION DE HORAS EXTRA","Recuerde que se debe remitir la  planificacion de horas extra para su aprobacion");

    	rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);



		
		bar_botones.agregarReporte();
		self_reporte.setId("self_reporte");
		agregarComponente(self_reporte);
		
		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
    	///VALIDACION ASIGNACION DE HXE JEFE INMEDIATO     	
		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	TablaGenerica tabJefeInmediato=null;
    	
    	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare  "
    			+ "FROM asi_jefe_inmediato  asjei "
    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
    			+ "where ide_gtemp="+ide_gtempxx);
    	
    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    		utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		return;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
        	area=tabJefeInmediato.getValor("detalle_geare");
		}
    	 	
    	 if(tipo_perfil.equals("1")){
    		jefe_inmediato_planificacion=tabJefeInmediato.getValor("ide_asjei");
    		tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
    				+ "FROM asi_empleado_jefe_inmediato ");
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
    		meses=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
			anios=utilitario.getVariable("p_anio_asignacion_horario_administrador");
			mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
			ide_geare=tabJefeInmediato.getValor("detalle_geare");
			
			tabCargoJefeInmediato=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
			"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
			"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
			"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
			"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
			"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
			"DEPA.DETALLE_GEDEP "+
			"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
			"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
			"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
			"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
			"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
			+ "where epar.ide_geare=+"+tabJefeInmediato.getValor("ide_geare")+"  and  "
			+ "epar.ide_gegro in(4,8)"
			+ "order by epar.ide_geedp desc,epar.ide_gegro asc limit 1" );
			
			NombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tabCargoJefeInmediato.getValor("ide_gtemp"));				
			cargo_elaborado=ide_geare;
			ide_gtemp_jefe_inmediato=tabCargoJefeInmediato.getValor("ide_gtemp");

			
    	}else{
    	
       tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
    				+ "FROM asi_empleado_jefe_inmediato "
    				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
		jefe_inmediato_planificacion=tabJefeInmediato.getValor("ide_asjei");

		meses=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
		anios=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
		mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
		anioEditar=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
		ide_geare=tabJefeInmediato.getValor("detalle_geare");
		//ide_gtemp_jefe_inmediato=tabJefeInmediato.getValor("ide_gtemp");
		cargo_elaborado="COORDINADOR "+ide_geare;
		
		tabCargoJefeInmediato=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP "+
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "where epar.ide_geare=+"+tabJefeInmediato.getValor("ide_geare")+"  and  "
				+ "epar.ide_gegro in(4,8)"
				+ "order by epar.ide_geedp desc limit 1" );
				
				NombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tabCargoJefeInmediato.getValor("ide_gtemp"));				
				cargo_elaborado="COORDINADOR "+ide_geare;
				ide_gtemp_jefe_inmediato=tabCargoJefeInmediato.getValor("ide_gtemp");

    	}

    	
    	//HORARIOS ASIGNADOS AL FUNCIONARIO str_ide_empleado_asignado
    	str_ide_empleado_asignado=new StringBuilder();
    		String int_num_col_ideasignado="";
    		for (int j = 0; j < tabEmpleadoXJefeInmediato.getTotalFilas(); j++) {
    			int_num_col_ideasignado=tabEmpleadoXJefeInmediato.getValor(j, "IDE_GTEMP");
    	  	if(str_ide_empleado_asignado.toString().isEmpty()==false){
    	  	str_ide_empleado_asignado.append(",");

    	  		  	}
    	  str_ide_empleado_asignado.append(int_num_col_ideasignado);
    		}
    	
    
    		
    		bar_botones.agregarComponente(new Etiqueta("Seleccione Opcion:"));

    	   	
    	/**
    	* Boton asignacion de horario por empleado
    	*/
    	    	Boton bot_asignar= new Boton();
    	    	bot_asignar.setIcon("ui-icon-pencil");
    	    	bot_asignar.setMetodo("abrirMes");
    	    	bot_asignar.setValue("Asignar");
    	    	bot_asignar.setTitle("Asignar");
    	    	bar_botones.agregarBoton(bot_asignar);	
    	    	
    	    	

    	    	// boton Asignar Turno Empleado Departamento
    			Boton bot_asignar_departamento = new Boton();
    			bot_asignar_departamento.setIcon("ui-icon-calculator");
    			bot_asignar_departamento.setMetodo("consultarDepartamento");
    			bot_asignar_departamento.setValue("Copiar X Dapartamento");
    			bot_asignar_departamento.setTitle("Copiar X Departamento");
    			//bar_botones.agregarBoton(bot_asignar_departamento);
    	    	
    	/**
    	 * Boton copia la fila ingresada anteriormente
    	 */
    	    	Boton bot_copiar= new Boton();
    	    	bot_copiar.setIcon("ui-icon-document");
    	    	bot_copiar.setMetodo("copiarHorario");
    	    	bot_copiar.setValue("Copiar Horario");
    	    	bot_copiar.setTitle("Copiar Horario");
    	    	//bar_botones.agregarBoton(bot_copiar);	
    	    	
    	    	//
    			//bar_botones.getBot_insertar().setRendered(false);

    	    	
    	    	// boton editar
    			Boton bot_editar = new Boton();
    			bot_editar.setIcon("ui-icon-cancel");
    	    	bot_editar.setValue("Editar");
    	    	bot_editar.setTitle("Editar");
    			bot_editar.setMetodo("editarPorMes");
    			bar_botones.agregarBoton(bot_editar);

    

    			

				Boton bot_impor_valores = new Boton();
				bot_impor_valores.setValue("Importar Valores");
				bot_impor_valores.setIcon("ui-icon-note");
				

				bot_impor_valores.setMetodo("abrirDialogoImportar");
				bar_botones.agregarBoton(bot_impor_valores);
				//abrirDialogoImportar
    			
    			
				Boton bot_impor_valores_total = new Boton();
				bot_impor_valores_total.setValue("Importar Todos los Valores");
				bot_impor_valores_total.setIcon("ui-icon-note");
				bot_impor_valores_total.setMetodo("abrirDialogoImportarTotal");
				bar_botones.agregarBoton(bot_impor_valores_total);
				
				
		    	Boton bot_enviar_plan= new Boton();
		    	bot_enviar_plan.setIcon("ui-icon-pencil");
		    	bot_enviar_plan.setMetodo("dibujarConfirmar");
		    	bot_enviar_plan.setValue("Remitir Planificacion HXE");
		    	bot_enviar_plan.setTitle("Remitir Planificacion HXE");
    	    	bar_botones.agregarBoton(bot_enviar_plan);	
    	    	
    	    	
    	    	// boton limpiar
    			Boton bot_limpiar = new Boton();
    			bot_limpiar.setIcon("ui-icon-cancel");
    	    	bot_limpiar.setValue("Limpiar");
    	    	bot_limpiar.setTitle("Limpiar");
    			bot_limpiar.setMetodo("limpiar");
    			bar_botones.agregarBoton(bot_limpiar);
				
				//Importacion de excel horas extra
				
				sel_importar.setId("sel_importar");
				//sel_departamento_empleado.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO", "IDE_GEDEP");
				
				sel_importar.setSeleccionTabla("SELECT rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub "
						+ "  FROM nrh_detalle_rubro drub "
						+ "  left join nrh_rubro rub on rub.ide_nrrub= drub.ide_nrrub  "
						+ "  where drub.ide_nrdtn  in (select ide_nrdtn from nrh_detalle_tipo_nomina where ide_nrtin=9 and ide_nrdtn=23) "
						+ "  and rub.ide_nrrub in (35,23,24,336,33) " 
						+ " group by  rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub","ide_nrrub");
				
				sel_importar.getTab_seleccion().getColumna("detalle_nrrub").setNombreVisual("RUBRO");

				sel_importar.setTitle("Seleccione Rubro para Validar");
				//gru_pantalla.getChildren().add(sel_importar);
				sel_importar.getBot_aceptar().setMetodo("seleccionoRubro");
				sel_importar.setWidth("60");
				sel_importar.setHeight("60");
				agregarComponente(sel_importar);
				

				
				
				// Dialogo para importar
						dia_importar.setId("dia_importar");
						dia_importar.setTitle("VALIDACION DE HORAS EXTRA");
						dia_importar.setPosition("left");
						dia_importar.setHeight("85%");
						dia_importar.getBot_aceptar().setRendered(false);
						dia_importar.setWidth("50%");
						dia_importar.getBot_cancelar().setMetodo("cancelarImportarEmpleados");
						
						
						
						Grid gri_cuerpo = new Grid();

						Grid gri_impo = new Grid();
						gri_impo.setColumns(2);

						//gri_impo.getChildren().add(new Etiqueta("Todas las Nominas'"));

						Grid gri_tn = new Grid();
						gri_tn.setColumns(2); 

						che_todas_nominas.setValue(true);
						che_todas_nominas.setMetodoChange("cambiaCheckAplicaTodasNominas");
					//	gri_tn.getChildren().add(che_todas_nominas);
						eti_num_nomina.setId("eti_num_nomina");
						eti_num_nomina.setStyle("font-size:8px;");
						gri_tn.getChildren().add(eti_num_nomina);
						gri_impo.getChildren().add(gri_tn);
						gri_impo.getChildren().add(new Etiqueta("Rubro tipo Constante o Teclado: "));
						aut_rubros.setId("aut_rubros");
						
						aut_rubros.setAutoCompletar("SELECT rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub "
								+ "FROM nrh_detalle_rubro drub "
								+ "left join nrh_rubro rub on rub.ide_nrrub= drub.ide_nrrub "
								+ "where drub.ide_nrdtn  in (select ide_nrdtn from nrh_detalle_tipo_nomina where ide_nrtin=13 and ide_nrdtn=27) "
								+ "and rub.ide_nrrub in (332,390,391,237,245) "
								+ "group by  rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub ORDER BY rub.detalle_nrrub ASC");
						
						
						aut_rubros.setMetodoChange("seleccionoRubro");
						
						gri_impo.getChildren().add(aut_rubros);

						gri_impo.getChildren().add(new Etiqueta("Seleccione el archivo: "));
						upl_archivo_por_rubro.setId("upl_archivo_por_rubro");
						upl_archivo_por_rubro.setMetodo("validarArchivo");

						upl_archivo_por_rubro.setUpdate("gri_valida");
						upl_archivo_por_rubro.setAuto(false);
						upl_archivo_por_rubro.setAllowTypes("/(\\.|\\/)(xls)$/");
						upl_archivo_por_rubro.setUploadLabel("Validar");
						upl_archivo_por_rubro.setCancelLabel("Cancelar Seleccion");

						gri_impo.getChildren().add(upl_archivo_por_rubro);
						gri_impo.setWidth("100%");

						Grid gri_valida = new Grid();
						gri_valida.setId("gri_valida");
						gri_valida.setColumns(3);

						Etiqueta eti_valida = new Etiqueta();
						eti_valida.setValueExpression("value", "pre_index.clase.upl_archivo.nombreReal");
						eti_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
						gri_valida.getChildren().add(eti_valida);

						Imagen ima_valida = new Imagen();
						ima_valida.setWidth("22");
						ima_valida.setHeight("22");
						ima_valida.setValue("/imagenes/im_excel.gif");
						ima_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
						gri_valida.getChildren().add(ima_valida);

						edi_mensajes.setControls("");
						edi_mensajes.setId("edi_mensajes");
						edi_mensajes.setStyle("overflow:auto;");
						edi_mensajes.setWidth(dia_importar.getAnchoPanel() - 15);
						edi_mensajes.setDisabled(true);
						gri_valida.setFooter(edi_mensajes);

						gri_cuerpo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
						gri_cuerpo.setMensajeInfo("Esta opci�n  permite subir valores a un rubro a partir de un archivo xls");
						gri_cuerpo.getChildren().add(gri_impo);
						gri_cuerpo.getChildren().add(gri_valida);
						gri_cuerpo.getChildren().add(edi_mensajes);
						gri_cuerpo.getChildren().add(new Espacio("0", "10"));

						dia_importar.setDialogo(gri_cuerpo);
						dia_importar.setDynamic(false);

						agregarComponente(dia_importar);
				
						
						
						
						
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

						che_todas_nominas.setValue(true);
						che_todas_nominas.setMetodoChange("cambiaCheckAplicaTodasNominas");
					//	gri_tn.getChildren().add(che_todas_nominas);
						eti_num_nomina.setId("eti_num_nomina");
						eti_num_nomina.setStyle("font-size:8px;");
						gri_tn_total.getChildren().add(eti_num_nomina);
						gri_impo_total.getChildren().add(gri_tn_total);
						//gri_impo_total.getChildren().add(new Etiqueta("Rubro tipo Constante o Teclado: "));
						/*aut_rubros.setId("aut_rubros");
						s
						aut_rubros.setAutoCompletar("SELECT rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub "
								+ "FROM nrh_detalle_rubro drub "
								+ "left join nrh_rubro rub on rub.ide_nrrub= drub.ide_nrrub "
								+ "where drub.ide_nrdtn  in (select ide_nrdtn from nrh_detalle_tipo_nomina where ide_nrtin=13 and ide_nrdtn=27) "
								+ "and rub.ide_nrrub in (332,390,391,237,245) "
								+ "group by  rub.ide_nrrub, drub.ide_nrdtn,rub.detalle_nrrub ORDER BY rub.detalle_nrrub ASC");
						
						
						aut_rubros.setMetodoChange("seleccionoRubro");*/
						
						//gri_impo.getChildren().add(aut_rubros);

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
				
						
						
						dia_valida_empleado.setId("dia_valida_empleado");
						dia_valida_empleado.getBot_aceptar().setMetodo("aceptarImportar");
						dia_valida_empleado.getBot_cancelar().setMetodo("cancelarImportarEmpleados");
						dia_valida_empleado.setModal(false);
						dia_valida_empleado.setPosition("right");
						dia_valida_empleado.setTitle("Colaboradores encontrados en el archivo");
						dia_valida_empleado.setWidth("50%");
						dia_valida_empleado.setHeight("85%");
						
						
					  tab_emp.setId("tab_emp");
					  tab_emp.setSql("SELECT EMP.ide_gtemp, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
							  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
							  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
							  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, "
							  		+ "	0 as VALOR_IMPORTAR  "
							  		+ "FROM asi_horario_mes_empleado ashme  "
							  		+ "left join gth_empleado emp on emp.ide_gtemp=ashme.ide_gtemp "
							  		+ "where EMP.ide_gtemp=-1");
					  
						
						//tab_emp.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , 0 as VALOR_IMPORTAR FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");
						tab_emp.setCampoPrimaria("IDE_GTEMP");
						tab_emp.setNumeroTabla(15);
						tab_emp.setColumnaSuma("VALOR_IMPORTAR");
						tab_emp.getColumna("NOMBRES").setLongitud(180);
						tab_emp.getColumna("VALOR_IMPORTAR").alinearDerecha();
						tab_emp.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);

						tab_emp.setRows(15);
						tab_emp.setLectura(true);
						tab_emp.dibujar();

						grid_tabla_emp.getChildren().add(tab_emp);
						grid_tabla_emp.setStyle("width:" + (dia_valida_empleado.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado.getAltoPanel() + "px;overflow:auto;display:block;");

						// dia_valida_empleado.setModal(true);
						dia_valida_empleado.setDialogo(grid_tabla_emp);
						dia_valida_empleado.setDynamic(false);

						agregarComponente(dia_valida_empleado);
						
		  	
						
				
				
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
					  		+ "0 as VALOR25N, "
							+ "0 as VALOR60, "
							+ "0 as VALOR25, "
							+ "0 as VALOR50, "
							+ "0 as VALOR100 "
					  		+ "FROM asi_horario_mes_empleado ashme  "
					  		+ "left join gth_empleado emp on emp.ide_gtemp=ashme.ide_gtemp "
					  		+ "where EMP.ide_gtemp=-1");
			  

				
			  tab_emp_total.setCampoPrimaria("IDE_GTEMP");
			  tab_emp_total.setNumeroTabla(16);
			 // tab_emp_total.setColumnaSuma("VALOR25N");
			 // tab_emp_total.setColumnaSuma("VALOR60");
			  //tab_emp_total.setColumnaSuma("VALOR25");
			  //tab_emp_total.setColumnaSuma("VALOR50");
			  //tab_emp_total.setColumnaSuma("VALOR100");
			  tab_emp_total.getColumna("NOMBRES").setLongitud(180);
			  tab_emp_total.getColumna("VALOR25N").alinearDerecha();
			  tab_emp_total.getColumna("VALOR60").alinearDerecha();
			  tab_emp_total.getColumna("VALOR25").alinearDerecha();
			  tab_emp_total.getColumna("VALOR50").alinearDerecha();
			  tab_emp_total.getColumna("VALOR100").alinearDerecha();
			  tab_emp_total.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);

			  tab_emp_total.setRows(15);
			  tab_emp_total.setLectura(true);
			  tab_emp_total.dibujar();

			  eti_25N.setId("eti_25N");
			  eti_25N.setStyle("font-size:10px;font-weight: bold; width: 130px;");
			  eti_60.setId("eti_60");
			  eti_60.setStyle("font-size:10px;font-weight: bold; width: 130px;");
			  eti_25.setId("eti_25");
			  eti_25.setStyle("font-size:10px;font-weight: bold; width: 130px;");
			  eti_50.setId("eti_50");
			  eti_50.setStyle("font-size:10px;font-weight: bold; width: 130px;");
			  eti_100.setId("eti_100");
			  eti_100.setStyle("font-size:10px;font-weight: bold; width: 130px;");

			  grid_tabla_emp_sum_totales25Loep.setId("grid_tabla_emp_sum_totales25Loep");
			  grid_tabla_emp_sum_totales60.setId("grid_tabla_emp_sum_totales60");
			  grid_tabla_emp_sum_totales25.setId("grid_tabla_emp_sum_totales25");
			  grid_tabla_emp_sum_totales50.setId("grid_tabla_emp_sum_totales50");
			  grid_tabla_emp_sum_totales100.setId("grid_tabla_emp_sum_totales100");

			  
			    grid_tabla_emp_sum_totales.setId("grid_tabla_emp_sum_totales");
				grid_tabla_emp_sum_totales.setColumns(5);
				//grid_tabla_emp_sum_totales.setMensajeInfo("DETALLE DE PLANIFICACION DE HORAS EXTRA ");
				grid_tabla_emp_sum_totales25Loep.setMensajeInfo("#HORAS 25 LOEP");
				//grid_tabla_emp_sum_totales25Loep.getChildren().add(eti_25N);
				grid_tabla_emp_sum_totales60.setMensajeInfo("#HORAS 60 LOEP");
				//grid_tabla_emp_sum_totales60.getChildren().add(eti_60);
				grid_tabla_emp_sum_totales25.setMensajeInfo("#HORAS 25 CT");
				//grid_tabla_emp_sum_totales25.getChildren().add(eti_25);
				grid_tabla_emp_sum_totales50.setMensajeInfo("#HORAS 50 CT");
			//	grid_tabla_emp_sum_totales50.getChildren().add(eti_50);
				grid_tabla_emp_sum_totales100.setMensajeInfo("#HORAS EXTRA 100");
				//grid_tabla_emp_sum_totales100.getChildren().add(eti_100);

				grid_tabla_emp_total.setStyle("width:" + (dia_valida_empleado_total.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado_total.getAltoPanel() + "px;overflow:auto;display:block;");

				// dia_valida_empleado.setModal(true);
				dia_valida_empleado_total.setDialogo(grid_tabla_emp_total);
				dia_valida_empleado_total.setDynamic(false);

				agregarComponente(dia_valida_empleado_total);
				
  	
						
						
    	    	sel_mes.setId("sel_mes");
    	    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+meses+") ","IDE_GEMES");
    	    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    			sel_mes.setRadio();
    	    	sel_mes.setTitle("Seleccione Mes Horario");
    	    	sel_mes.setWidth("20");
    	    	sel_mes.setHeight("50");
    			gru_pantalla.getChildren().add(sel_mes);
    			sel_mes.getBot_aceptar().setMetodo("obtenerMes");
    			agregarComponente(sel_mes);
    			
    			
    			sel_mes_editar.setId("sel_mes_editar");
    	    	sel_mes_editar.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+mesEditar+")","IDE_GEMES");
    	    	sel_mes_editar.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    	    	sel_mes_editar.setRadio();
    	    	sel_mes_editar.setTitle("Seleccione Mes Horario");
    			gru_pantalla.getChildren().add(sel_mes_editar);
    	    	sel_mes_editar.setWidth("20");
    	    	sel_mes_editar.setHeight("50");
    			sel_mes_editar.getBot_aceptar().setMetodo("obtenerMesEditar");
    			agregarComponente(sel_mes_editar);
    			
    			
    			sel_mes_importar.setId("sel_mes_importar");
    			sel_mes_importar.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+mesEditar+")","IDE_GEMES");
    			sel_mes_importar.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    			sel_mes_importar.setRadio();
    			sel_mes_importar.setTitle("Seleccione Mes Horario");
    			gru_pantalla.getChildren().add(sel_mes_importar);
    			sel_mes_importar.setWidth("20");
    			sel_mes_importar.setHeight("50");
    			sel_mes_importar.getBot_aceptar().setMetodo("obtenerMesImportar");
    			agregarComponente(sel_mes_importar);
    			
    			
    			sel_mes_importarTotales.setId("sel_mes_importarTotales");
    			sel_mes_importarTotales.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+mesEditar+")","IDE_GEMES");
    			sel_mes_importarTotales.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    			sel_mes_importarTotales.setRadio();
    			sel_mes_importarTotales.setTitle("Seleccione Mes Horario");
    			gru_pantalla.getChildren().add(sel_mes_importarTotales);
    			sel_mes_importarTotales.setWidth("20");
    			sel_mes_importarTotales.setHeight("45");
    			sel_mes_importarTotales.getBot_aceptar().setMetodo("obtenerMesImportarTotales");
    			agregarComponente(sel_mes_importarTotales);
    			
    			
    	    	sel_anio.setId("sel_anio");
    			sel_anio.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+") ","IDE_GEANI");
    			sel_anio.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    			sel_anio.setRadio();
    	    	sel_anio.setTitle("Seleccione Anio Horario");
    			gru_pantalla.getChildren().add(sel_anio);
    			sel_anio.setWidth("20");
    			sel_anio.setHeight("20");
    			sel_anio.getBot_aceptar().setMetodo("obtenerAnio");
    			agregarComponente(sel_anio);
    	    	
    			
    	    	sel_anio_editar.setId("sel_anio_editar");
    	    	sel_anio_editar.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")","IDE_GEANI");
    	    	sel_anio_editar.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	    	sel_anio_editar.setRadio();
    	    	sel_anio_editar.setTitle("Seleccione AnioHorario");
    			gru_pantalla.getChildren().add(sel_anio);
    			sel_anio_editar.setWidth("25");
    			sel_anio_editar.setHeight("30");
    			sel_anio_editar.getBot_aceptar().setMetodo("obtenerAnioEditar");
    			agregarComponente(sel_anio_editar);
    			
    			
    			
    	    	sel_anio_importar.setId("sel_anio_importar");
    	    	sel_anio_importar.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")","IDE_GEANI");
    	    	sel_anio_importar.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	    	sel_anio_importar.setRadio();
    	    	sel_anio_importar.setTitle("Seleccione Anio Importar");
    			gru_pantalla.getChildren().add(sel_anio_importar);
    			sel_anio_importar.setWidth("25");
    			sel_anio_importar.setHeight("30");
    			sel_anio_importar.getBot_aceptar().setMetodo("obtenerAnioImportar");
    			agregarComponente(sel_anio_importar);
    			
    			
    	    	sel_anio_importarTotales.setId("sel_anio_importarTotales");
    	    	sel_anio_importarTotales.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")","IDE_GEANI");
    	    	sel_anio_importarTotales.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	    	sel_anio_importarTotales.setRadio();
    	    	sel_anio_importarTotales.setTitle("Seleccione Anio Importar");
    			gru_pantalla.getChildren().add(sel_anio_importarTotales);
    			sel_anio_importarTotales.setWidth("25");
    			sel_anio_importarTotales.setHeight("30");
    			sel_anio_importarTotales.getBot_aceptar().setMetodo("obtenerAnioImportarTotales");
    			agregarComponente(sel_anio_importarTotales);
    			
    			
    			sel_mes_reporte.setId("sel_mes_reporte");
    			sel_mes_reporte.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+meses+") ","IDE_GEMES");
    			sel_mes_reporte.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    			sel_mes_reporte.setRadio();
    			sel_mes_reporte.setTitle("Seleccione Mes Reporte");
    			sel_mes_reporte.setWidth("20");
    			sel_mes_reporte.setHeight("20");
    			gru_pantalla.getChildren().add(sel_mes_reporte);
    			sel_mes_reporte.getBot_aceptar().setMetodo("obtenerMesReporte");
    			agregarComponente(sel_mes_reporte);
    			
    			
    			sel_anio_reporte.setId("sel_anio_reporte");
    			sel_anio_reporte.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")","IDE_GEANI");
    			sel_anio_reporte.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    			sel_anio_reporte.setRadio();
    			sel_anio_reporte.setTitle("Seleccione Anio Reporte");
    			gru_pantalla.getChildren().add(sel_anio_reporte);
    			sel_anio_reporte.setWidth("25");
    			sel_anio_reporte.setHeight("30");
    			sel_anio_reporte.getBot_aceptar().setMetodo("aceptarReporte");
    			agregarComponente(sel_anio_importar);

    			sel_empleado.setId("sel_empleado");
    			agregarComponente(sel_empleado);
    			
    			sel_empleado_editar.setId("sel_empleado_editar");
    			agregarComponente(sel_empleado_editar);
    			
    			
     			sel_empleado_importar.setId("sel_empleado_importar");
    			agregarComponente(sel_empleado_importar);
    	
    			sel_empleado_importarTotales.setId("sel_empleado_importarTotales");
    			agregarComponente(sel_empleado_importarTotales);
    	
    	tab_planificacion_hxe.setId("tab_planificacion_hxe");
        tab_planificacion_hxe.setTabla("con_biometrico_plan_hxe", "ide_cobph", 1);
        tab_planificacion_hxe.getColumna("ide_cobph").setNombreVisual("CODIGO");
        tab_planificacion_hxe.getColumna("ide_cobph").setOrden(1);
        
        
		  tab_planificacion_hxe.getColumna("documento_identidad_gtemp").setVisible(true);
	        tab_planificacion_hxe.getColumna("documento_identidad_gtemp").setLectura(true);
			tab_planificacion_hxe.getColumna("documento_identidad_gtemp").setFiltro(true);
	    	tab_planificacion_hxe.getColumna("documento_identidad_gtemp").setOrden(2);
	    	tab_planificacion_hxe.getColumna("documento_identidad_gtemp").setLongitud(15);

	       
        tab_planificacion_hxe.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");
		tab_planificacion_hxe.getColumna("ide_gtemp").setLectura(true);
			tab_planificacion_hxe.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
		"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
		"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
		"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
		"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
		" FROM  GTH_EMPLEADO EMP ");
		tab_planificacion_hxe.getColumna("ide_gtemp").setAutoCompletar();
		tab_planificacion_hxe.getColumna("ide_gtemp").setFiltro(true);
        tab_planificacion_hxe.getColumna("ide_gtemp").setOrden(3);

		

		tab_planificacion_hxe.getColumna("ide_gepgc").setNombreVisual("CARGO");
        tab_planificacion_hxe.getColumna("ide_gepgc").setCombo("SELECT ide_gepgc,titulo_cargo_gepgc from  gen_partida_grupo_cargo") ;
        tab_planificacion_hxe.getColumna("ide_gepgc").setAutoCompletar();
		tab_planificacion_hxe.getColumna("ide_gepgc").setLectura(true);
		tab_planificacion_hxe.getColumna("ide_gepgc").setOrden(4);

		
		tab_planificacion_hxe.getColumna("ide_geare").setNombreVisual("AREA");
        tab_planificacion_hxe.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare FROM gen_area ") ;
		tab_planificacion_hxe.getColumna("ide_geare").setAutoCompletar();
		tab_planificacion_hxe.getColumna("ide_geare").setLectura(true);
		tab_planificacion_hxe.getColumna("ide_geare").setOrden(5);

        
        tab_planificacion_hxe.getColumna("actividades_cobph").setNombreVisual("ACTIVIDADES");
        tab_planificacion_hxe.getColumna("actividades_cobph").setMetodoChange("validarActividad");
		tab_planificacion_hxe.getColumna("actividades_cobph").setOrden(6);

        
        tab_planificacion_hxe.getColumna("productos_eperado_cobph").setNombreVisual("PRODUCTOS ESPERADOS");
        tab_planificacion_hxe.getColumna("productos_eperado_cobph").setMetodoChange("validarActividad");
		tab_planificacion_hxe.getColumna("productos_eperado_cobph").setOrden(7);

        
     //  tab_planificacion_hxe.getColumna("actividades_cobph").setRequerida(true);

        tab_planificacion_hxe.getColumna("archivo_cobph").setVisible(false);
        //tab_planificacion_hxe.getColumna("archivo_cobph").setUpload(carpeta);
        //tab_planificacion_hxe.getColumna("archivo_cobph").setNombreVisual("ADJUNTO");
        //tab_planificacion_hxe.getColumna("archivo_cobph").setColumnaNombreArchivo("nombre_archivo_cobph");

      
        
        tab_planificacion_hxe.getColumna("nombre_archivo_cobph").setVisible(false);
        //tab_planificacion_hxe.getColumna("nombre_archivo_cobph").setNombreVisual("NOMBRE_ADJUNTO");
        //tab_planificacion_hxe.getColumna("nombre_archivo_cobph").setValorDefecto("sin nombre");
       // tab_planificacion_hxe.getColumna("nombre_archivo_cobph").setOrden(7);

	    tab_planificacion_hxe.getColumna("horas25_loep_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas25_loep_cobph").setNombreVisual("NRO.HORAS 25 LOEP");
        tab_planificacion_hxe.getColumna("horas25_loep_cobph").setMetodoChange("validarMaxHoras25Loep");
		tab_planificacion_hxe.getColumna("horas25_loep_cobph").setOrden(8);

        
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setNombreVisual("NRO.HORAS 60 LOEP");
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setMetodoChange("validarMaxHoras60Loep");
		tab_planificacion_hxe.getColumna("horas60_loep_cobph").setOrden(9);


        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setNombreVisual("NRO.HORAS 25 CT");
        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setMetodoChange("validarMaxHoras25ct");
		tab_planificacion_hxe.getColumna("horas25_ct_cobph").setOrden(10);

        
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setNombreVisual("NRO.HORAS 50 CT");
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setMetodoChange("validarMaxHoras50ct");
		tab_planificacion_hxe.getColumna("horas50_ct_cobph").setOrden(11);

        
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setNombreVisual("NRO.HORAS 100 LOEP Y CT");
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setMetodoChange("validarMaxHoras100Loepct");
		tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setOrden(12);

        
        tab_planificacion_hxe.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes FROM gen_mes");
        tab_planificacion_hxe.getColumna("ide_gemes").setVisible(false);
        
        tab_planificacion_hxe.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani FROM gen_anio");
        tab_planificacion_hxe.getColumna("ide_geani").setVisible(false);  
   
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setNombreVisual("FECHA REGISTRO");
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setValorDefecto(utilitario.getFechaActual());
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setLectura(true);
		tab_planificacion_hxe.getColumna("fecha_registro_cobph").setOrden(13);
              
        tab_planificacion_hxe.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
        tab_planificacion_hxe.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
        tab_planificacion_hxe.getColumna("IDE_USUA").setAutoCompletar();
        tab_planificacion_hxe.getColumna("IDE_USUA").setLectura(true);
        tab_planificacion_hxe.getColumna("IDE_USUA").setVisible(false);

        
        tab_planificacion_hxe.getColumna("activo_cobph").setNombreVisual("ACTIVO");
        tab_planificacion_hxe.getColumna("activo_cobph").setCheck();
        tab_planificacion_hxe.getColumna("activo_cobph").setValorDefecto("true");
        tab_planificacion_hxe.getColumna("activo_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("activo_cobph").setVisible(false);
		tab_planificacion_hxe.getColumna("activo_cobph").setOrden(14);


    
        
        tab_planificacion_hxe.getColumna("aprobado_cobph").setLectura(false);
        tab_planificacion_hxe.getColumna("aprobado_tthh_cobph").setLectura(false);

        tab_planificacion_hxe.getColumna("ide_asjei").setVisible(false);
        tab_planificacion_hxe.getColumna("retorno_plan_cobph").setLectura(false);
        
        
        
        
       // tab_planificacion_hxe.setLectura(true);
        tab_planificacion_hxe.setCondicion("ide_cobph=-1");
        tab_planificacion_hxe.setCampoOrden("ide_cobph asc");


        tab_planificacion_hxe.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_planificacion_hxe);
        pat_panel.setMensajeWarn("PLANIFICACION_HXE");

        
        

        tab_planificacion_hxe_observacion.setId("tab_planificacion_hxe_observacion");
        tab_planificacion_hxe_observacion.setTabla("con_biometrico_phxe_observacion", "ide_cobpo", 2);
        tab_planificacion_hxe_observacion.getColumna("ide_cobpo").setNombreVisual("CODIGO");
        tab_planificacion_hxe_observacion.getColumna("IDE_ASJEI").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("observacion_cobpo").setNombreVisual("OBSERVACION");     
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes FROM gen_mes");
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani FROM gen_anio");
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setVisible(false);  
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setAutoCompletar();
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setLectura(true);
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setNombreVisual("ACTIVO");
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setCheck();
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setValorDefecto("true");
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setLectura(true);
        tab_planificacion_hxe_observacion.setLectura(true);
        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
        tab_planificacion_hxe_observacion.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_planificacion_hxe_observacion);
        pat_panel2.setMensajeWarn("OBSERVACION PLANIFICACION_HXE");
        
      /*  tab_planificacion_hxe_observacion.setId("tab_planificacion_hxe_observacion");
        tab_planificacion_hxe_observacion.setTabla("con_biometrico_planhxe_observacion", "ide_cobho", 2);
        tab_planificacion_hxe_observacion.getColumna("ide_cobho").setNombreVisual("CODIGO");
        
        tab_planificacion_hxe_observacion.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");
        tab_planificacion_hxe_observacion.getColumna("ide_gtemp").setCombo("SELECT emp.ide_GTEMP, " +
		"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
		"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
		"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
		"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
		"FROM GTH_EMPLEADO EMP ") ;
        tab_planificacion_hxe_observacion.getColumna("ide_gtemp").setAutoCompletar();
        tab_planificacion_hxe_observacion.getColumna("ide_gtemp").setLectura(true);

        
        
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani "
            	+ "FROM gen_anio");
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setVisible(false);
        
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes "
            	+ "FROM gen_mes");
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setVisible(false);

        
        tab_planificacion_hxe_observacion.getColumna("observacionide_cobho").setNombreVisual("OBSERVACION");
        tab_planificacion_hxe_observacion.getColumna("observacionide_cobho").setRequerida(true);

        
        tab_planificacion_hxe_observacion.getColumna("archivo_cobho").setNombreVisual("ARCHIVO");
        tab_planificacion_hxe_observacion.getColumna("archivo_cobho").setUpload("hxeObservacion");


        
        tab_planificacion_hxe_observacion.getColumna("fecha_cobho").setValorDefecto(utilitario.getFechaActual());
        tab_planificacion_hxe_observacion.getColumna("fecha_cobho").setLectura(true);
		
       
        tab_planificacion_hxe_observacion.getColumna("ide_asjei").setNombreVisual("JEFE INMEDIATO");
        tab_planificacion_hxe_observacion.getColumna("ide_asjei").setCombo("SELECT asi.ide_asjei, EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS    "
				+ "FROM asi_jefe_inmediato asi "
				+ "left join gth_empleado emp on emp.ide_gtemp=asi.ide_gtemp");
        tab_planificacion_hxe_observacion.getColumna("ide_asjei").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("ide_cobph").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("activo_cobho").setNombreVisual("ACTIVO");
        tab_planificacion_hxe_observacion.getColumna("activo_cobho").setCheck();
        tab_planificacion_hxe_observacion.getColumna("activo_cobho").setValorDefecto("true");
             
        //tab_planificacion_hxe_observacion.setCondicion("ide_cobho=-1");

    	
        tab_planificacion_hxe_observacion.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_planificacion_hxe_observacion);
        pat_panel2.setMensajeWarn("OBSERVACION");


        tab_resumen_hxe.setId("tab_resumen_hxe");
        tab_resumen_hxe.setTabla("con_biometrico_resumen_hxe", "ide_cobrh", 2);
        tab_resumen_hxe.getColumna("ide_cobrh").setNombreVisual("CODIGO");
        
        tab_resumen_hxe.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");
        tab_resumen_hxe.getColumna("ide_gtemp").setCombo("SELECT emp.ide_GTEMP, " +
		"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
		"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
		"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
		"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
		"FROM GTH_EMPLEADO EMP ") ;
        tab_resumen_hxe.getColumna("ide_gtemp").setAutoCompletar();
        tab_resumen_hxe.getColumna("ide_gtemp").setLectura(true);
        tab_resumen_hxe.getColumna("ide_gtemp").setFiltro(true);

        
        
        tab_resumen_hxe.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani "
            	+ "FROM gen_anio");
        tab_resumen_hxe.getColumna("ide_geani").setVisible(false);
        
        tab_resumen_hxe.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes "
            	+ "FROM gen_mes");
        tab_resumen_hxe.getColumna("ide_gemes").setVisible(false);

        
        tab_resumen_hxe.getColumna("num_hxe25_cobrh").setNombreVisual("HXE_25%");    
        tab_resumen_hxe.getColumna("num_hxeNoc25_cobrh").setNombreVisual("HXE_NOCTURNO_25%");

        tab_resumen_hxe.getColumna("num_hxe50_cobrh").setNombreVisual("HXE_50%");    
        tab_resumen_hxe.getColumna("num_hxe60_cobrh").setNombreVisual("HXE_60%");
        tab_resumen_hxe.getColumna("num_hxe100_cobrh").setNombreVisual("HXE_100%");    
        tab_resumen_hxe.getColumna("fecha_registro_cobrh").setNombreVisual("FECHA_REGISTRO");    

        
        tab_resumen_hxe.getColumna("activo_cobrh").setNombreVisual("ACTIVO");
        tab_resumen_hxe.getColumna("activo_cobrh").setCheck();
        tab_resumen_hxe.getColumna("activo_cobrh").setValorDefecto("true");
        tab_resumen_hxe.setCondicion("ide_cobrh=-1");     
        tab_resumen_hxe.setLectura(true);

        tab_resumen_hxe.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_resumen_hxe);
        pat_panel3.setMensajeWarn("RESUMEN PLANIFICACION HORAS EXTRA");

        
        */
        
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "70%", "H");
       // div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
 
    }

  

	@Override
	public void insertar() {
	
		/*int emp=tab_planificacion_hxe.getFilaSeleccionada().getIndice();
			
		String emp_anio;
		Fila mp_anio = tab_planificacion_hxe.getFila(emp); 		
		if (mp_anio.getCampos()[0]==null || mp_anio.getCampos()[0].toString().equals("")) {
			utilitario.agregarMensajeError("No se puede realizar esta accion", "Debe guardar el registro de planificacion para ingresar la observacion");
			return;
		}
		
		int ide_tabla =Integer.parseInt(mp_anio.getRowKey());
		
		TablaGenerica tab_observacion= utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_geani, ide_gemes, fecha_cobph, dia_cobph,  "
				+ "ide_astur, num_hora25_cobph, num_horanocturno25_cobph, num_hora50_cobph, "
				+ "num_hora60_cobph, num_hora100_cobph, ide_asjei, activo_cobph "
				+ "FROM con_biometrico_plan_hxe "
				+ "where ide_cobph="+ide_tabla);
		
	
		/*if (tab_planificacion_hxe.getTotalFilas()>0) {	
		if(tab_planificacion_hxe_observacion.isFocus()){
			tab_planificacion_hxe_observacion.insertar();		
			tab_planificacion_hxe_observacion.setValor("ide_gtemp",tab_observacion.getValor("ide_gtemp"));
			tab_planificacion_hxe_observacion.setValor("ide_geani",tab_observacion.getValor("ide_geani"));
			tab_planificacion_hxe_observacion.setValor("ide_gemes", tab_observacion.getValor("ide_gemes"));
	        tab_planificacion_hxe_observacion.setValor("ide_asjei", jefe_inmediato_planificacion);
	        tab_planificacion_hxe_observacion.setValor("ide_cobph",""+ ide_tabla);

		}
		}*/
		
		
	}



	@Override
	public void guardar() {
        if (tab_planificacion_hxe.guardar()) {
     
    		guardarPantalla();		
        }
        
       // if (tab_planificacion_hxe_observacion.guardar()) {
    //		guardarPantalla();		
      //  }
	}



	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_planificacion_hxe.getTotalFilas()>0) {		
	   	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where  aprobado_cobph=false and retorno_plan_cobph=false and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	   	if (tab_planificacion_hxe_temp.getTotalFilas()>0) {
			
		}else {
			utilitario.agregarMensaje("No se puede Eliminar", "Contactese con el Administrador");
			return;
		}
		}else {
			utilitario.agregarMensaje("No se puede Eliminar", "No se han encontrado registros validos");
			return;
		}
	}

 



	


	public Tabla getTab_planificacion_hxe() {
		return tab_planificacion_hxe;
	}



	public void setTab_planificacion_hxe(Tabla tab_planificacion_hxe) {
		this.tab_planificacion_hxe = tab_planificacion_hxe;
	}



	public SeleccionTabla getSel_mes() {
		return sel_mes;
	}



	public void setSel_mes(SeleccionTabla sel_mes) {
		this.sel_mes = sel_mes;
	}



	public SeleccionTabla getSel_anio_editar() {
		return sel_anio_editar;
	}



	public void setSel_anio_editar(SeleccionTabla sel_anio_editar) {
		this.sel_anio_editar = sel_anio_editar;
	}



	public SeleccionTabla getSel_anio() {
		return sel_anio;
	}



	public void setSel_anio(SeleccionTabla sel_anio) {
		this.sel_anio = sel_anio;
	}



	public SeleccionTabla getSel_empleado() {
		return sel_empleado;
	}



	public void setSel_empleado(SeleccionTabla sel_empleado) {
		this.sel_empleado = sel_empleado;
	}



	public SeleccionTabla getSel_empleado_editar() {
		return sel_empleado_editar;
	}



	public void setSel_empleado_editar(SeleccionTabla sel_empleado_editar) {
		this.sel_empleado_editar = sel_empleado_editar;
	}



	public SeleccionTabla getSel_mes_editar() {
		return sel_mes_editar;
	}



	public void setSel_mes_editar(SeleccionTabla sel_mes_editar) {
		this.sel_mes_editar = sel_mes_editar;
	}



	public void abrirMes(){

    	asignar++;
		
    	if (asignar>1 && bandEdit==false) {
    		tab_planificacion_hxe.setCondicion("ide_cobph=-1");
			tab_planificacion_hxe.ejecutarSql();
	        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
	        tab_planificacion_hxe_observacion.ejecutarSql();
			utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
    		
		}else {
			if (bandEdit==true) {
				tab_planificacion_hxe.setCondicion("ide_cobph=-1");
				tab_planificacion_hxe.ejecutarSql();
		        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
		        tab_planificacion_hxe_observacion.ejecutarSql();
				utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
			}
		}
    	
    	/*
    if (empleadoSucursal.equals("612") || empleadoSucursal.equals("508")) {
	sel_mes.dibujar();
	bandEdit=false;
    	}else {
			utilitario.agregarMensajeInfo("No se puede asignar", "Fuera del per�odo de asignaci�n");
			return;
		}
	
    	
    	*/
    	
    	sel_mes.dibujar();
    	bandEdit=false;
	
		}
	
	
    

    
    public void obtenerMes(){
    				  mes=sel_mes.getValorSeleccionado();
			  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		 			return;
		 		}else {
		 			
  				  mes=sel_mes.getValorSeleccionado();
System.out.println("obtenerMes: "+mes);
		 			
			  sel_mes.cerrar();	
		      sel_anio.dibujar();
		 		}
		
    }
    
    
    public void obtenerMesReporte(){
		  mes=sel_mes_reporte.getValorSeleccionado();
if ((mes==null || mes.isEmpty() || mes.equals("") )) {
		utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		return;
	}else {
		
	  mes=sel_mes_reporte.getValorSeleccionado();
System.out.println("obtenerMes: "+mes);
		
sel_mes_reporte.cerrar();	
sel_anio_reporte.dibujar();
	}

}
    
    
    
    public void obtenerMesEditar(){
      	
			  mes=sel_mes_editar.getValorSeleccionado();
				 if ((mes==null || mes.isEmpty() || mes.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
			 			return;
			 		}else {
						  sel_mes_editar.cerrar();	
						    sel_anio_editar.dibujar();

		
		}
  
    }
    
    
    public void obtenerMesImportar(){
      	
		  mes=sel_mes_importar.getValorSeleccionado();
			 if ((mes==null || mes.isEmpty() || mes.equals(""))) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
		 			return;
		 		}else {
					  sel_mes_importar.cerrar();	
					    sel_anio_importar.dibujar();

	
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
    public void obtenerAnio(){
 	   utilitario.addUpdate("sel_empleado");

        anio=sel_anio.getValorSeleccionado();
        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
    			return;
    		}else {
	   	sel_anio.cerrar();
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")");
		int anioEmpleado=Integer.parseInt(sel_anio.getValorSeleccionado());    			

		
		//Aqui busco por el mes seleccionado 
 		String sql="";
 		if (mes.equals("1")){
			mes="13";
			int resultadoAnio=anioEmpleado-1;
			anioEmpleado=resultadoAnio;
		}else {
			int resultadoAnio=anioEmpleado;
			anioEmpleado=resultadoAnio;
		}
		int mesActual=(Integer.parseInt(mes));
	
		
		if (mes.equals("13")) {
			mes="1";
		}
		
		
		TablaGenerica tabEmpleadoMensual=null;
		TablaGenerica tabEmpleadoMensualAnual=null;
	///empleados asignados al jefe inmediatodfgg
		
   	 if(tipo_perfil.equals("1")){
		tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_asjei "
					+ "from asi_empleado_jefe_inmediato where activo_emjei=true");
					//+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") ");
        
        
		
		///horarios asignados
		
		//Empleados asignados en ese mes
 		StringBuilder str_ide_empleado_mensual=new StringBuilder();
 		String int_num_col_idegetempmensual="";
 		for (int j = 0; j < tabEmpleadoMensual.getTotalFilas(); j++) {
 			int_num_col_idegetempmensual=tabEmpleadoMensual.getValor(j, "IDE_GTEMP");
  	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
  	  	  	str_ide_empleado_mensual.append(",");

  	  		  	}
  	  	str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
  		}
			TablaGenerica tab_planificacion_hxe = null;
	 		if (str_ide_empleado_mensual==null || str_ide_empleado_mensual.toString().equals("") || str_ide_empleado_mensual.toString().isEmpty() || str_ide_empleado_mensual.length()==0) {
	 			return;
	 		}else{	
			tab_planificacion_hxe =utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
							+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph, "
							+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes, "
							+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph "
							+ "FROM con_biometrico_plan_hxe "
							+ "where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes="+mes);


					StringBuilder str_ide_empleado_mensual_planificacion=new StringBuilder();
			 		String int_num_col_idegetempmensualPlanificacion="";
			 		for (int j = 0; j < tab_planificacion_hxe.getTotalFilas(); j++) {
			 			int_num_col_idegetempmensualPlanificacion=tab_planificacion_hxe.getValor(j, "IDE_GTEMP");
			  	  	if(str_ide_empleado_mensual_planificacion.toString().isEmpty()==false){
			  	  	str_ide_empleado_mensual_planificacion.append(",");

			  	  		  	}
			  	  str_ide_empleado_mensual_planificacion.append(int_num_col_idegetempmensualPlanificacion);
			  		}
				
			 		
					if (str_ide_empleado_mensual_planificacion==null || str_ide_empleado_mensual_planificacion.toString().equals("") || str_ide_empleado_mensual_planificacion.toString().isEmpty() || str_ide_empleado_mensual_planificacion.length()==0) {
						/*sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
					 					+ "from  asi_horario_mes_empleado asemp   "
					 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
					 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
					 					+ "where "
					 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
					 							+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") "
					 					//+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
					 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
					 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
					 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
						
						
						sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "from  asi_empleado_jefe_inmediato asemp   "
			 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
			 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			 					+ "where "
			 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
			 							+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") and asemp.activo_emjei=true "
			 					//+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
			 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
			 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
					
	 				}else{
	 					/*sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "from  asi_horario_mes_empleado asemp   "
			 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
			 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			 					+ "where "
			 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
			 					+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") and "
			 					+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
			 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
			 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
	 					
	 					sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "from  asi_empleado_jefe_inmediato asemp   "
			 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
			 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			 					+ "where "
			 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
			 					+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") and "
			 					+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") and asemp.activo_emjei=true "
			 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
			 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
			 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
	 					
	 					
	 					
	 					
	 				}
	 		}	
			 		
					
				}
   	 
   	 //Perfil Diferente
   	 else if(tipo_perfil.equals("2")){
					/*tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
							
							+ "from asi_horario_mes_empleado  "
							+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and  ide_asjei="+jefe_inmediato_planificacion);*/
		        
					tabEmpleadoMensual=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp, activo_emjei "
							+ "FROM asi_empleado_jefe_inmediato  "
							+ "where ide_asjei="+jefe_inmediato_planificacion+" and activo_emjei=true ");
						
					
					
					
					

					
				///horarios asignados
				
				//Empleados asignados en ese mes
		 		StringBuilder str_ide_empleado_mensual=new StringBuilder();
		 		String int_num_col_idegetempmensual="";
		 		for (int j = 0; j < tabEmpleadoMensual.getTotalFilas(); j++) {
		 			int_num_col_idegetempmensual=tabEmpleadoMensual.getValor(j, "IDE_GTEMP");
		  	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
		  	  	  	str_ide_empleado_mensual.append(",");

		  	  		  	}
		  	  	str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
		  		}
					TablaGenerica tab_planificacion_hxe = null;
			 		if (str_ide_empleado_mensual==null || str_ide_empleado_mensual.toString().equals("") || str_ide_empleado_mensual.toString().isEmpty() || str_ide_empleado_mensual.length()==0) {
			 			return;
			 		}else{	
					tab_planificacion_hxe =utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
									+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph, "
									+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes, "
									+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph "
									+ "FROM con_biometrico_plan_hxe "
									+ "where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion);


							StringBuilder str_ide_empleado_mensual_planificacion=new StringBuilder();
					 		String int_num_col_idegetempmensualPlanificacion="";
					 		for (int j = 0; j < tab_planificacion_hxe.getTotalFilas(); j++) {
					 			int_num_col_idegetempmensualPlanificacion=tab_planificacion_hxe.getValor(j, "IDE_GTEMP");
					  	  	if(str_ide_empleado_mensual_planificacion.toString().isEmpty()==false){
					  	  	str_ide_empleado_mensual_planificacion.append(",");

					  	  		  	}
					  	  str_ide_empleado_mensual_planificacion.append(int_num_col_idegetempmensualPlanificacion);
					  		}
						
					 		
							if (str_ide_empleado_mensual_planificacion==null || str_ide_empleado_mensual_planificacion.toString().equals("") || str_ide_empleado_mensual_planificacion.toString().isEmpty() || str_ide_empleado_mensual_planificacion.length()==0) {
								sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
							 					+ "from  asi_empleado_jefe_inmediato asemp   "
							 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
							 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							 					+ "where "
							 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
							 					+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") and asemp.activo_emjei=true "
							 					//+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
							 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
							 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
							 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
							
			 				}else{
			 					sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
					 					+ "from  asi_empleado_jefe_inmediato asemp  "
					 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
					 					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
					 					+ "where "
					 					//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
					 					+ "asemp.ide_gtemp in("+str_ide_empleado_mensual+") and "
					 					+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") and asemp.activo_emjei=true "
					 					+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
					 					+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
					 					+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
			 				}
			 		}	

				//adicta a la fiesta
			 		}else {
						utilitario.agregarMensaje("La accion seleccionada no se puede realizar", "No contiene permisos");
						return;
					}
	 		
		System.out.println(""+sql);
		sel_empleado.setSeleccionTabla(sql, "IDE_GTEMP");
 		sel_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
 		sel_empleado.setHeight("60");
 		sel_empleado.setWidth("40");
 		sel_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(sel_empleado);
		sel_empleado.getBot_aceptar().setMetodo("getEmpleado");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
	   utilitario.addUpdate("sel_empleado");
 		
 		

         
    
    }
    
    }
    
    
    
    

    
    
	/**
	 * mETODO PARA OBTENER EL MES CON LOS EMPLEADOS SELECCIONADOS
	 */
	public void getEmpleado(){
		empleado="";
		empleado=sel_empleado.getSeleccionados();
		if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			sel_empleado.cerrar();
		      }
	try {
		
		/*TablaGenerica tab_planificacion = utilitario.consultar("SELECT phxe.ide_cobph, phxe.ide_gtemp, phxe.ide_gepgc, phxe.ide_geare, phxe.actividades_cobph, "
				+ "phxe.archivo_cobph, phxe.nombre_archivo_cobph, phxe.horas25_loep_cobph, phxe.horas60_loep_cobph, "
				+ "phxe.horas25_ct_cobph, phxe.horas50_ct_cobph, phxe.horas100_loep_ct_cobph, phxe.ide_gemes,  "
				+ "phxe.ide_geani, phxe.fecha_registro_cobph, phxe.ide_usua, phxe.activo_cobph,phxe.ide_asjei "
				+ "FROM con_biometrico_plan_hxe  phxe "
				+ "left join  asi_empleado_jefe_inmediato asjei on phxe.ide_gtemp=asjei.ide_gtemp "
				+ "where phxe.ide_geani=15 and phxe.ide_gemes=1 and ");*/
		
		
		empleado=sel_empleado.getSeleccionados()+",";
		//listaEmpleado=sel_empleado.getListaSeleccionados();
		int contenido=listaEmpleado.size();

		
		/**
 * Inicializo nuevamente la lista
 */
		//si no se encuentra vacia la lista de empleados 
			if (contenido>0) {
				listaEmpleado.removeAll(listaEmpleado);
				ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
				
			}


		
		//obtengo el numero de "," dentro de los empleados seleccionados
		char[] arrayChar = empleado.toCharArray();
		int contComas=0;
		String empAux="";
		for(int i=0; i<arrayChar.length; i++){
		if( arrayChar[i] == ','){
				contComas++;	
				listaEmpleado.add(Integer.parseInt((empAux)));
				empAux="";
		}else {
			empAux=empAux+empleado.charAt(i);
			
		}
		
		
		}
		
	
		//Validacion si se ha seleccionado copiar fila mediante la variable num_copias
		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
		
			 }else {
			
		int mesAsignado=Integer.parseInt(mes);
		int anioAsignado=Integer.parseInt(anio);
		
//En la asignacion solo se puede asignar uno a la ves
		//for (int i = 0; i < contComas; i++) {
			listaAnio.add(anioAsignado);
			listaMes.add(mesAsignado);
		//}
		
	

		     // if (contComas>1) {
		//		utilitario.agregarMensajeInfo("Solo se puede asignar un empleado a la vez", "Solo se puede asignar un empleado");
		//	return;
		 //     }
		
	int mesObtenido=0,empleadoObtenido=0;
			/*	for (int i = 0; i < contComas; i++) {
					
					TablaGenerica tabSucuAreaDept=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
							"SUCU.IDE_SUCU, AREA.IDE_GEARE, " +
							"DEPA.IDE_GEDEP " +
							"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
							"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
							"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
							"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
							"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
							+ "where epar.ide_gtemp="+listaEmpleado.get(i)+" " 
							+ "	ORDER BY DETALLE_GEARE ASC");
					mesObtenido=listaMes.get(i);
					empleadoObtenido=listaEmpleado.get(i);
				}
		
		
				tab_tabla.setCondicion("where ide_mes");
				
				*/
	
	TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
	String anioEscogido=tabAnio.getValor("detalle_geani");
	
	TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
	String mesEscogido=tabMes.getValor("detalle_gemes");
	String ide_gemes=tabMes.getValor("ide_gemes");
	
	int diaInicio=1,diaFin=0,diaSemana=0;
	String fechaBase="",fecha="",unicoEmpleado="";
	fechaBase=anioEscogido+"-"+mes+"-"+01;
	diaFin=utilitario.getDia(utilitario.getUltimaFechaMes(fechaBase));
	int valor=0;
	valor=Integer.parseInt(jefe_inmediato_planificacion);
		int dia=1;
		TablaGenerica tab_acciones_personal=null;
		/*if (contComas==1) {
			tab_planificacion_hxe.insertar();
			tab_planificacion_hxe.setValor("IDE_GTEMP", ""+listaEmpleado.get(0));
			tab_planificacion_hxe.setValor("IDE_GEANI", ""+listaAnio.get(0));
			tab_acciones_personal = utilitario.consultar("select ide_geedp,ide_geare,ide_gecaf,ide_gepgc  "
			+ "from gen_empleados_departamento_par where ide_gtemp="+listaEmpleado.get(0)+" order by ide_geedp desc limit 1");
			tab_planificacion_hxe.setValor( "IDE_GEMES", ""+ide_gemes);
			tab_planificacion_hxe.setValor( "ide_gepgc", ""+tab_acciones_personal.getValor("ide_gepgc"));
			tab_planificacion_hxe.setValor( "ide_geare", ""+tab_acciones_personal.getValor("ide_geare"));
			tab_planificacion_hxe.setValor( "ide_usua", ""+utilitario.getVariable("ide_usua"));
			tab_planificacion_hxe.setValor("fecha_registro_cobph",utilitario.getFechaActual());
			tab_planificacion_hxe.setValor( "horas25_loep_cobph","0");
			tab_planificacion_hxe.setValor( "horas60_loep_cobph","0");
			tab_planificacion_hxe.setValor( "horas25_ct_cobph","0");
			tab_planificacion_hxe.setValor( "horas50_ct_cobph","0");
			tab_planificacion_hxe.setValor( "horas100_loep_ct_cobph","0");
			tab_planificacion_hxe.setValor( "activo_cobph","true");
			tab_planificacion_hxe.setValor("ide_asjei",""+valor);
			
		}else{*/
			for (int i = 0; i < contComas; i++) {
			tab_planificacion_hxe.insertar();
				tab_planificacion_hxe.setValor(i, "IDE_GTEMP", ""+listaEmpleado.get(i));
				tab_planificacion_hxe.setValor(i, "IDE_GEANI", ""+listaAnio.get(0));
				tab_acciones_personal = utilitario.consultar("select ide_geedp,ide_geare,ide_gecaf,ide_gepgc  "
				+ "from gen_empleados_departamento_par where ide_gtemp="+listaEmpleado.get(i)+" order by ide_geedp desc limit 1");
				tab_planificacion_hxe.setValor(i, "IDE_GEMES", ""+ide_gemes);
				tab_planificacion_hxe.setValor(i, "ide_gepgc", ""+tab_acciones_personal.getValor("ide_gepgc"));
				tab_planificacion_hxe.setValor(i, "ide_geare", ""+tab_acciones_personal.getValor("ide_geare"));
				tab_planificacion_hxe.setValor(i, "ide_usua", ""+utilitario.getVariable("ide_usua"));
				tab_planificacion_hxe.setValor(i,"fecha_registro_cobph",utilitario.getFechaActual());
				tab_planificacion_hxe.setValor(i, "horas25_loep_cobph","0");
				tab_planificacion_hxe.setValor(i, "horas60_loep_cobph","0");
				tab_planificacion_hxe.setValor(i, "horas25_ct_cobph","0");
				tab_planificacion_hxe.setValor(i, "horas50_ct_cobph","0");
				tab_planificacion_hxe.setValor(i, "horas100_loep_ct_cobph","0");
				tab_planificacion_hxe.setValor(i, "activo_cobph","true");
				tab_planificacion_hxe.setValor("ide_asjei",""+valor);
					TablaGenerica tab_empleado = utilitario.consultar("select ide_gtemp,documento_identidad_gtemp from gth_empleado where ide_gtemp="+listaEmpleado.get(i));
					tab_planificacion_hxe.setValor("documento_identidad_gtemp",""+tab_empleado.getValor("documento_identidad_gtemp"));
					
			//	}
				
		        
				
			}
			tab_planificacion_hxe.guardar();
			guardarPantalla();
		
		
		
		//getCalendario(mes, anioEscogido);
		
		
		
		//tab_planificacion_hxe.setLectura(true);
		tab_planificacion_hxe.setHeader("PLANIFICACION DE HORAS EXTRA DE "+mesEscogido+"  DEL "+anioEscogido);
		tab_planificacion_hxe.setCondicion("ide_geani="+anio+" and ide_gemes="+mes+" and ide_gtemp in("+sel_empleado.getSeleccionados()+") and ide_asjei="+jefe_inmediato_planificacion);
        tab_planificacion_hxe.setCampoOrden("ide_cobph asc");
		tab_planificacion_hxe.ejecutarSql();
		tab_planificacion_hxe_observacion.setHeader("PLANIFICACION - OBSERVACION ");
  		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
  		tab_planificacion_hxe_observacion.ejecutarSql();
  		
  		//tab_planificacion_hxe_observacion.setHeader("TABLA HORARIO POR EMPLEADO  "+mesEscogido+"  DEL "+anioEscogido);
  		//tab_planificacion_hxe_observacion.actualizar();

 		utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion,sel_empleado");
  		
		}
		
		
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block

	System.out.println("ERROR METODO getEmpleado()");
	
	}
	
	
 			

}
    
   
	
	
    private String  obtenerHorarioMesEmpleado(String empleado,String mes,String anio,int i){
    	
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select ide_ashme,ide_gtemp,dia"+i+" from asi_horario_mes_empleado "
    			+ "where ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
    	
    	TablaGenerica tab_turnos=utilitario.consultar("SELECT ide_astur, ide_ashor, ide_gtgre, minuto_tolerancia_astur, activo_astur,  "
    			+ "nom_astur, descripcion_astur, turno_matriz_astur  FROM asi_turnos where ide_astur="+horarioXEmpleado.getValor("dia"+i));
    	  	
   	
    	return tab_turnos.getValor("ide_astur");
    }

    
      	 public String diaSemana (String fecha)
      	    {
      		 
      		 int dia=utilitario.getDia(fecha);
      		 int mes=utilitario.getMes(fecha);
      		 int anioEscogido=utilitario.getAnio(fecha);
      	    	String mesMenor="";
      	        String Valor_dia="";
      	        String diaMenor="";

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
      	            Valor_dia = "Domingo";
      	        } else if (diaSemana == 2) {
      	            Valor_dia = "Lunes";
      	        } else if (diaSemana == 3) {
      	            Valor_dia = "Martes";
      	        } else if (diaSemana == 4) {
      	            Valor_dia = "Miercoles";
      	        } else if (diaSemana == 5) {
      	            Valor_dia = "Jueves";
      	        } else if (diaSemana == 6) {
      	            Valor_dia = "Viernes";
      	        } else if (diaSemana == 7) {
      	            Valor_dia = "Sabado";
      	        }
      	        return Valor_dia;
      	    }
      	 
      	 
     	 public Integer diaSemanaEntero (String fecha)
   	    {
   		 
   		 int dia=utilitario.getDia(fecha);
   		 int mes=utilitario.getMes(fecha);
   		 int anioEscogido=utilitario.getAnio(fecha);
   		 int valorRetorno=0;
   	    	String mesMenor="";
   	        String Valor_dia="";
   	        String diaMenor="";

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
   	            Valor_dia = "Domingo";
   	            valorRetorno=7;
   	        } else if (diaSemana == 2) {
   	            Valor_dia = "Lunes";
   	            valorRetorno=1;

   	        } else if (diaSemana == 3) {
   	            Valor_dia = "Martes";
   	            valorRetorno=2;

   	        } else if (diaSemana == 4) {
   	            Valor_dia = "Miercoles";
   	            valorRetorno=3;

   	        } else if (diaSemana == 5) {
   	            Valor_dia = "Jueves";
   	            valorRetorno=4;

   	        } else if (diaSemana == 6) {
   	            Valor_dia = "Viernes";
   	            valorRetorno=5;

   	        } else if (diaSemana == 7) {
   	            Valor_dia = "Sabado";
   	            valorRetorno=6;
   	        }
   	        return valorRetorno;
   	    }
      	 
      	 
      	  private String getFechaAsyyyyMMddHHmmss(Date fecha){
      	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      	    return df.format(fecha);
      	    }
          

          private Date getFechaAsyyyyMMddHHmm(String fecha){
      	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

          
          private String getFechaAsyyyyMMddHHmm(Date fecha){
      	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      	    return df.format(fecha);
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

          private Date deStringADate(String fecha) throws ParseException{
          	Date date=null;
          	try {
      			String pattern = "yyyy-MM-dd";
      			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      			date = simpleDateFormat.parse(fecha);
      			
      		} catch (ParseException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
          	return date;
          }
          
          
          public void	editarPorMes(){
        		tab_planificacion_hxe.setCondicion("ide_cobph=-1");
        		tab_planificacion_hxe.ejecutarSql();
    	        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    	        tab_planificacion_hxe_observacion.ejecutarSql();
    			utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
        		bandEdit=true;
        		sel_mes_editar.dibujar();
        	}
          
          
          
        //Metodo distingue si es ingreso o edicion
          public void obtenerAnioEditar(){

              anio=sel_anio_editar.getValorSeleccionado();
              if (anio==null || (anio.isEmpty() || anio.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
          			return;
          		}else {
          			sel_anio_editar.cerrar();
          			sel_empleado_editar.setId("sel_empleado_editar");
          			
          			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
          			String sql="";
          			 if (tipo_perfil.equals("1")) {    				
          				
          					/*	sql="select emp.ide_gtemp, emp.apellido_paterno_gtemp,  "
              					+ "(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) as apellido_materno_gtemp,primer_nombre_gtemp,   "
              					+ "(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as segundo_nombre_gtemp,documento_identidad_gtemp "
              					+ "from gth_empleado emp   "
              					+ "left join con_biometrico_plan_hxe asemp on asemp.ide_gtemp=emp.ide_gtemp  "
              					+ " where "//emp.activo_gtemp in (true) "
              					+ " and ide_gemes="+mes+" and ide_geani in("+sel_anio_editar.getValorSeleccionado()+")  "
              					+ "order by apellido_paterno_gtemp, apellido_materno_gtemp";
              					sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");*/
          				 
          				sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_editar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") "  
      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");
              		
      				}else {
      					
      							sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_editar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") and asemp.ide_asjei="+jefe_inmediato_planificacion+" "  
      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");

      				  }
          			 
          			System.out.println(""+sql);

          			sel_empleado_editar.setSeleccionTabla(sql, "IDE_GTEMP");
          			sel_empleado_editar.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
          			sel_empleado_editar.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
        	 		sel_empleado.setHeight("60");
          	 		sel_empleado.setWidth("40");
          			sel_empleado_editar.setTitle("Seleccione un Empleado");
          			gru_pantalla.getChildren().add(sel_empleado_editar);
          			sel_empleado_editar.getBot_aceptar().setMetodo("getEmpleadoEditar");
          		   	sel_empleado_editar.redibujar();
          		   	utilitario.addUpdate("sel_empleado_editar");
          		   	
      	
          }
          
          
          }
          
          
          
        //Metodo distingue si es ingreso o edicion
          public void obtenerAnioImportar(){

              anio=sel_anio_importar.getValorSeleccionado();
              if (anio==null || (anio.isEmpty() || anio.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
          			return;
          		}else {
          			sel_anio_importar.cerrar();
          			sel_empleado_importar.setId("sel_empleado_importar");
          			
          			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
          			String sql="";
          			 if (tipo_perfil.equals("1")) {    				
          				
                    				 
          				sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_importar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") "  
      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");
              		
      				}else {
      					
      							sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_importar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") and asemp.ide_asjei="+jefe_inmediato_planificacion+" " 
      							 +" and (asemp.aprobado_cobph=false or asemp.aprobado_cobph is null) " 

      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");

      				  }
      
          			System.out.println(""+sql);
          			sel_empleado_importar.setSeleccionTabla(sql, "IDE_GTEMP");
          			sel_empleado_importar.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
          			sel_empleado_importar.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
          			sel_empleado_importar.setTitle("Seleccione un Empleado");
          			gru_pantalla.getChildren().add(sel_empleado_importar);
          			sel_empleado_importar.getBot_aceptar().setMetodo("getEmpleadoImportar");
          			sel_empleado_importar.redibujar();
          		   	utilitario.addUpdate("sel_empleado_importar");
          		  
      	
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
          			sel_empleado_importarTotales .setId("sel_empleado_importarTotales");
          			
          			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
          			String sql="";
          			 if (tipo_perfil.equals("1")) {    				
          				
                    				 
          				sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_importarTotales.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") "  
      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_importarTotales.setSeleccionTabla(sql,"IDE_GTEMP");
              		
      				}else {
      					
      							sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
      							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
      							+ "emp.documento_identidad_gtemp  "
      							+ "	 from  con_biometrico_plan_hxe asemp "
      							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
      							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
      							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
      							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
      							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
      							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
      							//+ "and emp.activo_gtemp=true  "
      							+ " and asemp.ide_geani in("+sel_anio_importarTotales.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") and asemp.ide_asjei="+jefe_inmediato_planificacion+" "
      							+" and (asemp.aprobado_cobph=false or asemp.aprobado_cobph is null) " 
      							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
      							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
      							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
      							sel_empleado_importarTotales.setSeleccionTabla(sql,"IDE_GTEMP");

      				  }
      
          			System.out.println(""+sql);
          			sel_empleado_importarTotales.setSeleccionTabla(sql, "IDE_GTEMP");
          			sel_empleado_importarTotales.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
          			sel_empleado_importarTotales.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
          			sel_empleado_importarTotales.setTitle("Seleccione Empleados");
          			gru_pantalla.getChildren().add(sel_empleado_importarTotales);
          			sel_empleado_importarTotales.getBot_aceptar().setMetodo("getEmpleadoImportarTotales");
          			sel_empleado_importarTotales.redibujar();
          		   	utilitario.addUpdate("sel_empleado_importarTotales");
          		  
      	
          }
          
          
          }
          
        //Metodo distingue si es ingreso o edicion
          public void obtenerAnioReporte(){

              anio=sel_anio_reporte.getValorSeleccionado();
              if (anio==null || (anio.isEmpty() || anio.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
          			return;
          		}else {
          			sel_anio_reporte.cerrar();     		  
          }
          
          
          }
          
          
          
          /**
           * Metodo para consulta de empleados por mes y a�o
           */
          public void getEmpleadoEditar(){
          	try {
          		empleado=sel_empleado_editar.getSeleccionados();
          		
          		 if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
      		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
      		 			tab_planificacion_hxe.setCondicion("ide_cobph=-1");
      		 			tab_planificacion_hxe.ejecutarSql();
      		 			utilitario.addUpdate("tab_tabla");
      		 			return;
      		 		}
          	    	sel_empleado_editar.cerrar();

          		//Validadcion si no existe datos
          	    	TablaGenerica tab_consultar_empleado;
          	    	
          			if(tipo_perfil.equals("1")){
          				 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
          	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+")");
          				}else {
          					 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
               	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
      				}


          		
          		/**
          		 * Validacion si no existen datos para los parametros 
          		 */
          		if (tab_consultar_empleado.getTotalFilas()<=0) {
          			utilitario.agregarMensajeError("No existen datos para este mes", "No se ha encontrado informaci�n");	
          			return;
      			}else {
      			     }
          		
          		
          		//Validacion si no se escogen datos
          		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
          			return;
          		}else {
          			
          		int mesAsignado=Integer.parseInt(mes);
          		int anioAsignado=Integer.parseInt(anio);
        

          		
      			if(tipo_perfil.equals("1")){

          		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
          		tab_planificacion_hxe.ejecutarSql();
          		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")");
          		tab_planificacion_hxe_observacion.ejecutarSql();

          		//tab_resumen_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")");
          		//tab_resumen_hxe.ejecutarSql();
      			}else {
      		 		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
              		tab_planificacion_hxe.ejecutarSql();
              		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
              		tab_planificacion_hxe_observacion.ejecutarSql();
              		//tab_resumen_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
              		//tab_resumen_hxe.ejecutarSql();
      			}

          		
          		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
          		String anioEscogido=tabAnio.getValor("detalle_geani");
          		
          		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+mes+")");
          		String mesEscogido=tabMes.getValor("detalle_gemes");
          		
          		tab_planificacion_hxe.setHeader("TABLA HORARIO POR EMPLEADO  "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe.actualizar();
          		
          		tab_planificacion_hxe_observacion.setHeader("TABLA OBSERVACION  "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe_observacion.actualizar();

         		utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
         		
          		}
          	} catch (NumberFormatException e) {
          		// TODO Auto-generated catch block

          	System.out.println("ERROR METODO getEmpleado()");
          	
          	}
          }
          
          
          
          
          /**
           * Metodo para consulta de empleados por mes y a�o
           */
          public void getEmpleadoImportar(){
          	try {
          		empleado=sel_empleado_importar.getSeleccionados();
          		
          		 if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
      		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
      		 			tab_planificacion_hxe.setCondicion("ide_cobph=-1");
      		 			tab_planificacion_hxe.ejecutarSql();
      		 			utilitario.addUpdate("tab_planificacion_hxe");
      		 			return;
      		 		}
          	    	sel_empleado_importar.cerrar();

          		//Validadcion si no existe datos
          	    	TablaGenerica tab_consultar_empleado;
          	    	
          			if(tipo_perfil.equals("1")){
          				 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
          	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+")");
          				}else {
          					 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
               	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
      				}


          		
          		/**
          		 * Validacion si no existen datos para los parametros 
          		 */
          		if (tab_consultar_empleado.getTotalFilas()<=0) {
          			utilitario.agregarMensajeError("No existen datos para este mes", "No se ha encontrado informaci�n");	
          			return;
      			}else {
      			     }
          		
          		
          		//Validacion si no se escogen datos
          		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
          			return;
          		}else {
          			
          		int mesAsignado=Integer.parseInt(mes);
          		int anioAsignado=Integer.parseInt(anio);
        
          		
      			if(tipo_perfil.equals("1")){
          		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
          		tab_planificacion_hxe.ejecutarSql();
      			}else {
      		 		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
              		tab_planificacion_hxe.ejecutarSql();
      			}
          		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
          		String anioEscogido=tabAnio.getValor("detalle_geani");
          		
          		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+mes+")");
          		String mesEscogido=tabMes.getValor("detalle_gemes");
          		
          		tab_planificacion_hxe.setHeader("TABLA HORARIO POR EMPLEADO  "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe.actualizar();
          		
          		tab_planificacion_hxe_observacion.setHeader("PLANIFICACION DE HORAS EXTRA DE "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
          		tab_planificacion_hxe_observacion.ejecutarSql();
          		
          		
         		utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
         		if (valorTotal) {
         			dia_importar.dibujar();
				}else{
         		dia_importar.dibujar();  }
          		}
          		 
          	} catch (NumberFormatException e) {
          		// TODO Auto-generated catch block

          	System.out.println("ERROR METODO getEmpleado()");
          	
          	}
          }
          
          
          
          
          /**
           * Metodo para consulta de empleados por mes y a�o
           */
          public void getEmpleadoImportarTotales(){
          	try {
          		empleado=sel_empleado_importarTotales.getSeleccionados();
          		
          		 if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
      		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
      		 			tab_planificacion_hxe.setCondicion("ide_cobph=-1");
      		 			tab_planificacion_hxe.ejecutarSql();
      		 			utilitario.addUpdate("tab_planificacion_hxe");
      		 			return;
      		 		}
          	    	sel_empleado_importarTotales.cerrar();

          		//Validadcion si no existe datos
          	    	TablaGenerica tab_consultar_empleado;
          	    	
          			if(tipo_perfil.equals("1")){
          				 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
          	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+")");
          				}else {
          					 tab_consultar_empleado=utilitario.consultar("SELECT ide_cobph, ide_gtemp  FROM con_biometrico_plan_hxe where ide_gtemp in ("+empleado+")  "
               	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
      				}


          		
          		/**
          		 * Validacion si no existen datos para los parametros 
          		 */
          		if (tab_consultar_empleado.getTotalFilas()<=0) {
          			utilitario.agregarMensajeError("No existen datos para este mes", "No se ha encontrado informaci�n");	
          			return;
      			}else {
      			     }
          		
          		
          		//Validacion si no se escogen datos
          		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
          			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
          			return;
          		}else {
          			
          		int mesAsignado=Integer.parseInt(mes);
          		int anioAsignado=Integer.parseInt(anio);
        
          		
      			if(tipo_perfil.equals("1")){
          		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
          		tab_planificacion_hxe.ejecutarSql();
      			}else {
      		 		tab_planificacion_hxe.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
              		tab_planificacion_hxe.ejecutarSql();
      			}
          		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
          		String anioEscogido=tabAnio.getValor("detalle_geani");
          		
          		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+mes+")");
          		String mesEscogido=tabMes.getValor("detalle_gemes");
          		
          		tab_planificacion_hxe.setHeader("TABLA HORARIO POR EMPLEADO  "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe.actualizar();
          		
          		tab_planificacion_hxe_observacion.setHeader("PLANIFICACION DE HORAS EXTRA DE "+mesEscogido+"  DEL "+anioEscogido);
          		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
          		tab_planificacion_hxe_observacion.ejecutarSql();
          		
          		
         		utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
         		if (valorTotal) {
         			dia_importar_total.dibujar();
				}else{
         		dia_importar_total.dibujar();  }
          		}
          	} catch (NumberFormatException e) {
          		// TODO Auto-generated catch block

          	System.out.println("ERROR METODO getEmpleado()");
          	
          	}
          }
          
          
          
          
 public void limpiar(){
	 tab_planificacion_hxe.setCondicion("ide_cobph=-1");
	 tab_planificacion_hxe.ejecutarSql();
	 tab_resumen_hxe.setCondicion("ide_cobpo=-1");
	 tab_resumen_hxe.ejecutarSql();
	 utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");

 }



public Dialogo getDia_importar() {
	return dia_importar;
}



public void setDia_importar(Dialogo dia_importar) {
	this.dia_importar = dia_importar;
}



public SeleccionTabla getSel_importar() {
	return sel_importar;
}



public void setSel_importar(SeleccionTabla sel_importar) {
	this.sel_importar = sel_importar;
}



public Upload getUpl_archivo() {
	return upl_archivo;
}



public void setUpl_archivo(Upload upl_archivo) {
	this.upl_archivo = upl_archivo;
}



public Check getChe_todas_nominas() {
	return che_todas_nominas;
}



public void setChe_todas_nominas(Check che_todas_nominas) {
	this.che_todas_nominas = che_todas_nominas;
}
	
	
public void abrirDialogoImportar() {
	tab_planificacion_hxe.setCondicion("ide_cobph=-1");
	tab_planificacion_hxe.ejecutarSql();
    tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
		sel_mes_importar.dibujar();
		valorTotal=false;
	}

public void abrirDialogoImportarTotal() {
	tab_planificacion_hxe.setCondicion("ide_cobph=-1");
	tab_planificacion_hxe.ejecutarSql();
    tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
		sel_mes_importarTotales.dibujar();
		valorTotal=true;
	}


/**
 * Valida el archivo para que pueda importar un rubro a la nomina
 * 
 * @param evt
 */

public void validarArchivo(FileUploadEvent evt) {
	if (aut_rubros.getValor() != null) {
		// Leer el archivo
		String str_msg_info = "";
		String str_msg_adve = "";
		String str_msg_erro = "";
		double dou_tot_valor_imp = 0;
		boolean bandTabVacia=false;
		int contErrores=0; 
		double valor25MaxLOEP=48.0,valor50MaxCT=48.0,valor100MaxCT=60.0;

		try {
			// V�lido que el rubro seleccionado este configurado en los tipo
			// de nomina
			String tipo_nom = "";
			
			Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
			Sheet hoja = archivoExcel.getSheet(0);// LEE LA PRIMERA HOJA
			if (hoja == null) {
				utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
				return;
			}
			int int_fin = hoja.getRows();
			upl_archivo_por_rubro.setNombreReal(evt.getFile().getFileName());
			str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
			lis_importa = new ArrayList<String[]>();

			tab_emp.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , 0 as VALOR_IMPORTAR FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");
			tab_emp.ejecutarSql();
			tab_emp.setLectura(false);
			tab_emp.setDibujo(false);
			int ide_gtemp=0;
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
				for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				String str_cedula = hoja.getCell(0, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				TablaGenerica tab_empleado = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);			
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos, fila " + (i + 1));
				} else {
				ide_gtemp=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
				int valorHex=0;
				String ide_cobph="";
				boolean band25=false,bandNoc25=false,band50=false,band60=false,band100=false;
				bandTabVacia=false;
				// Valido que el documento sea correcto
					if (!ser_empleado.isDocumentoIdentidadValido(utilitario.getVariable("p_gth_tipo_documento_cedula"), str_cedula)) {
						str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no es valido, fila " + (i + 1));
					} else {
						// Valido que el empleado se encuentre en la o las
						// nominas q va a importar
						String str_ide_gtemp = tab_empleado.getValor("IDE_GTEMP");
						TablaGenerica tabpartda = null;
						tabpartda = ser_empleado.getPartidaRoles(str_ide_gtemp);
						if (tabpartda.isEmpty() == false) {
							tab_hxe= utilitario.consultar("SELECT ide_cobph,ide_gtemp, ide_asjei "
									+ "FROM con_biometrico_plan_hxe "
									+ "where ide_gtemp="+ide_gtemp+" and ide_geani="+anio+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")");
							
							if (Integer.parseInt(aut_rubros.getValor())==hxe25) {
								band25=true;
								tipo_hora_extra=1;
								x=1;
							}else if (Integer.parseInt(aut_rubros.getValor())==hxeN25) {
								bandNoc25=true;
								tipo_hora_extra=2;
								x=2;

							}else if (Integer.parseInt(aut_rubros.getValor())==hxe50) {
								band50=true;
								tipo_hora_extra=3;
								x=3;

							}else if (Integer.parseInt(aut_rubros.getValor())==hxe60) {
								band60=true;
								tipo_hora_extra=4;
								x=4;

							}else if (Integer.parseInt(aut_rubros.getValor())==hxe100){
								band100=true;
								tipo_hora_extra=5;
								x=5;

							}
							
							
							if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
								str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
							}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene partida, fila " + (i + 1));
							}				
					}//validacion documento de identidad
				}// tab_emp
				
				String str_valor = hoja.getCell(1, i).getContents();
				str_valor = str_valor.replaceAll(",", ".");
				if (str_valor == null) {
					str_valor = "0.00";
				}
				double dou_valor = 0;
				try {
					// Valida que sea una cantidad numerica
					dou_valor = (Double.parseDouble(str_valor));
					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor numerico no valido , fila " + (i + 1));
				}
				//
				//TablaGenerica tab_emp;
				Object[] obj_cumula = getAcumulado(str_cedula);

				//if (obj_cumula == null) {
					tab_emp.insertar();
					tab_emp.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
					tab_emp.setValor("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
					tab_emp.setValor(
							"NOMBRES",
							new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ")
									.append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
									.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ")
									.append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
					if (x==1) {
						if (dou_valor>valor25MaxLOEP) {
							dou_valor=valor25MaxLOEP;
						}	
					}else if (x==2) {
						
					}else if (x==3) {
						
					}else if (x==4) {
						if (dou_valor>valor50MaxCT) {
							dou_valor=valor50MaxCT;
						}
					}else if (x==5) {
						if (dou_valor>valor100MaxCT) {
							dou_valor=valor100MaxCT;
						}
					}
				
					
					
					tab_emp.setValor("VALOR_IMPORTAR", utilitario.getFormatoNumero(dou_valor));
					lis_importa.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
				/*} else {
					// Acumula el valor
					try {
						int int_fila = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[0]));
						double dou_anterior = Double.parseDouble(String.valueOf(obj_cumula[1]));

						tab_emp.setValor(int_fila, "VALOR_IMPORTAR", utilitario.getFormatoNumero(dou_valor + dou_anterior));
						int int_indice = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[2]));
						lis_importa.set(int_indice, new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor + dou_anterior) });
					} catch (Exception e) {
						System.out.println("ERROR " + e.getMessage());
					}
				}*/

				dou_tot_valor_imp = dou_tot_valor_imp + dou_valor;

			}
			tab_emp.setLectura(true);
			tab_emp.setDibujo(true);
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
		grid_tabla_emp.getChildren().clear();
		tab_emp.getColumna("VALOR_IMPORTAR").setTotal(dou_tot_valor_imp);
		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp.getChildren().add(tab_emp);
		dia_valida_empleado.dibujar();
		} else {
			utilitario.agregarMensajeError("Debe seleccionar un rubro", "");
		}
		

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
		double dou_tot_valor_imp60 = 0;
		double dou_tot_valor_imp25 = 0;
		double dou_tot_valor_imp50 = 0;
		double dou_tot_valor_imp100 = 0;
		double valor25MaxLOEP=48.0,valor50MaxCT=48.0,valor100MaxCT=60.0;

		boolean bandTabVacia=false;
		int contErrores=0; 
		
		try {
			// V�lido que el rubro seleccionado este configurado en los tipo
			// de nomina
			String tipo_nom = "";
			
			Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
			Sheet hoja = archivoExcel.getSheet(0);// LEE LA PRIMERA HOJA
			if (hoja == null) {
				utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
				return;
			}
			int int_fin = hoja.getRows();
			upl_archivo.setNombreReal(evt.getFile().getFileName());
			str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
			lis_importa = new ArrayList<String[]>();
			lis_importa60 = new ArrayList<String[]>();
			lis_importa25 = new ArrayList<String[]>();
			lis_importa50 = new ArrayList<String[]>();
			lis_importa100 = new ArrayList<String[]>();


			tab_emp_total.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , "
					+ "0 as VALOR25N, "
					+ "0 as VALOR60, "
					+ "0 as VALOR25, "
					+ "0 as VALOR50, "
					+ "0 as VALOR100 "
					+ "FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");	
			
			tab_emp_total.ejecutarSql();
			tab_emp_total.setLectura(false);
			tab_emp_total.setDibujo(false);
			int ide_gtemp=0;
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
			x=0;
			double dou_valor = 0;

			for (x = 1; x <= 5; x++) {		
			for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				String str_cedula = hoja.getCell(0, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				TablaGenerica tab_empleado = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);			
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos, fila " + (i + 1));
				} else {
				ide_gtemp=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
				int valorHex=0;
				String ide_cobph="";
				boolean band25=false,bandNoc25=false,band50=false,band60=false,band100=false;
				bandTabVacia=false;
				// Valido que el documento sea correcto
					if (!ser_empleado.isDocumentoIdentidadValido(utilitario.getVariable("p_gth_tipo_documento_cedula"), str_cedula)) {
						str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no es valido, fila " + (i + 1));
					} else {
						// Valido que el empleado se encuentre en la o las
						// nominas q va a importar
						String str_ide_gtemp = tab_empleado.getValor("IDE_GTEMP");
						TablaGenerica tabpartda = null;
						tabpartda = ser_empleado.getPartidaRoles(str_ide_gtemp);
						if (tabpartda.isEmpty() == false) {
							tab_hxe= utilitario.consultar("SELECT ide_cobph,ide_gtemp, ide_asjei "
									+ "FROM con_biometrico_plan_hxe "
									+ "where ide_gtemp="+ide_gtemp+" and ide_geani="+anio+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")");
							if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
								str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
							}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene partida, fila " + (i + 1));
							}				
					}//validacion documento de identidad
				}// tab_emp
				
				
				
				
				String str_valor = hoja.getCell(x, i).getContents();
				str_valor = str_valor.replaceAll(",", ".");
				if (str_valor == null) {
					str_valor = "0.00";
				}
				dou_valor = 0;
				try {
					// Valida que sea una cantidad numerica
					dou_valor = (Double.parseDouble(str_valor));
					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor numerico no valido , fila " + (i + 1));
				}
				//
				//TablaGenerica tab_emp;
				Object[] obj_cumula = getAcumuladoTotal(str_cedula,x);
				Object[] obj_cumula60 = getAcumuladoTotal(str_cedula,x);
				Object[] obj_cumula25 = getAcumuladoTotal(str_cedula,x);
				Object[] obj_cumula50 = getAcumuladoTotal(str_cedula,x);
				Object[] obj_cumula100 = getAcumuladoTotal(str_cedula,x);
				if (x==1) {
				if (obj_cumula == null) {
						tab_emp_total.insertar();
						tab_emp_total.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
						tab_emp_total.setValor("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
						tab_emp_total.setValor(
								"NOMBRES",
								new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ")
										.append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
										.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ")
										.append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
						if (dou_valor>valor25MaxLOEP) {
							dou_valor=valor25MaxLOEP;
						}
						tab_emp_total.setValor("VALOR25N", utilitario.getFormatoNumero(dou_valor));
						lis_importa.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
				} else {
					// Acumula el valor
					try {
						int int_fila = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[0]));
						double dou_anterior = Double.parseDouble(String.valueOf(obj_cumula[1]));

						tab_emp_total.setValor(int_fila, "VALOR25N", utilitario.getFormatoNumero(dou_valor + dou_anterior));
						int int_indice = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[2]));
						lis_importa.set(int_indice, new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor + dou_anterior) });
					} catch (Exception e) {
						System.out.println("ERROR " + e.getMessage());
					}
				}
				dou_tot_valor_imp = dou_tot_valor_imp + dou_valor;				
				}
				eti_25N.setValue(dou_tot_valor_imp);

				
				if (x==2) {
			//if (obj_cumula60 == null) {
						tab_emp_total.setValor(i,"VALOR60", utilitario.getFormatoNumero(dou_valor));
						lis_importa60.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
						dou_tot_valor_imp60 = dou_tot_valor_imp60 + dou_valor;
						eti_60.setValue(dou_tot_valor_imp60);

				}else if(x==3){
					tab_emp_total.setValor(i,"VALOR25",utilitario.getFormatoNumero(dou_valor));
					lis_importa25.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					dou_tot_valor_imp25 = dou_tot_valor_imp25 + dou_valor;
					eti_25.setValue(dou_tot_valor_imp25);
				}else if(x==4){
					if (dou_valor>valor50MaxCT) {
						dou_valor=valor50MaxCT;
					}
					tab_emp_total.setValor(i,"VALOR50",utilitario.getFormatoNumero(dou_valor));
					lis_importa50.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					dou_tot_valor_imp50 = dou_tot_valor_imp50 + dou_valor;
					eti_50.setValue(dou_tot_valor_imp50);
				}else if(x==5){
					if (dou_valor>valor100MaxCT) {
						dou_valor=valor100MaxCT;
					}
					tab_emp_total.setValor(i,"VALOR100",utilitario.getFormatoNumero(dou_valor));
					lis_importa100.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					dou_tot_valor_imp100 = dou_tot_valor_imp100 + dou_valor;
					eti_100.setValue(+dou_tot_valor_imp100);
				}
				/*} else {
					// Acumula el valor
					try {
						int int_fila = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[0]));
						double dou_anterior = Double.parseDouble(String.valueOf(obj_cumula[1]));

						tab_emp_total.setValor(int_fila, "VALOR60", utilitario.getFormatoNumero(dou_valor + dou_anterior));
						int int_indice = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[2]));
						lis_importa60.set(int_indice, new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor + dou_anterior) });
					} catch (Exception e) {
						System.out.println("ERROR " + e.getMessage());
					}
				}*/
			
				
				
				
				/*}else if (x==2) {
					dou_tot_valor_imp60 = dou_tot_valor_imp60 + dou_valor;

				}else if (x==3) {
					dou_tot_valor_imp25 = dou_tot_valor_imp25 + dou_valor;

				}else if (x==4) {
					dou_tot_valor_imp50 = dou_tot_valor_imp50 + dou_valor;

				}else if (x==5) {
					dou_tot_valor_imp100 = dou_tot_valor_imp100 + dou_valor;
*/
				
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
		grid_tabla_emp_sum_totales.getChildren().clear();
	/*	if (x==1) {
			tab_emp_total.getColumna("VALOR25N").setTotal(dou_tot_valor_imp);
		if (x==2) {
			tab_emp_total.getColumna("VALOR60").setTotal(dou_tot_valor_imp60);
		}
		
		if (x==3) {
			tab_emp_total.getColumna("VALOR25").setTotal(dou_tot_valor_imp25);
		}
		
		if (x==4) {
			tab_emp_total.getColumna("VALOR50").setTotal(dou_tot_valor_imp50);

		}
		}	if (x==5) {
			tab_emp_total.getColumna("VALOR100").setTotal(dou_tot_valor_imp100);

		}*/
	
		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp_total.getChildren().add(tab_emp_total);
		//grid_tabla_emp_sum_totales25Loep.getChildren().add(eti_25N);
		//grid_tabla_emp_sum_totales60.getChildren().add(eti_60);
		//grid_tabla_emp_sum_totales25.getChildren().add(eti_25);
		//grid_tabla_emp_sum_totales50.getChildren().add(eti_50);
		//grid_tabla_emp_sum_totales100.getChildren().add(eti_100);
		
		grid_tabla_emp_sum_totales25Loep.setMensajeInfo("# HORAS EXTRA 25 LOEP: "+dou_tot_valor_imp+"");
		grid_tabla_emp_sum_totales60.setMensajeInfo("# HORAS EXTRA 60 LOEP: "+dou_tot_valor_imp60+"");
		grid_tabla_emp_sum_totales25.setMensajeInfo("# HORAS EXTRA 25 CT: "+dou_tot_valor_imp25+"");
		grid_tabla_emp_sum_totales50.setMensajeInfo("# HORAS EXTRA 50 CT: "+dou_tot_valor_imp50+"");
		grid_tabla_emp_sum_totales100.setMensajeInfo("# HORAS EXTRA 100 : "+dou_tot_valor_imp100+"");

		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales);
		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales25Loep);
		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales60);
		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales25);
		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales50);
		grid_tabla_emp_total.getChildren().add(grid_tabla_emp_sum_totales100);

		//dia_valida_empleado_total.getChildren().add(eti_25);
		//dia_valida_empleado_total.getChildren().add(eti_60);
		//dia_valida_empleado_total.getChildren().add(eti_25);
		//dia_valida_empleado_total.getChildren().add(eti_50);

		dia_valida_empleado_total.dibujar();
		/*} else {
			utilitario.agregarMensajeError("Debe seleccionar un rubro", "");
		}*/
	

	

		
		
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




private Object[] getAcumulado(String documento) {
	for (int i = 0; i < tab_emp.getTotalFilas(); i++) {
		if (tab_emp.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP").equalsIgnoreCase(documento)) {
			Object[] obj = new Object[3];
			obj[0] = i;
			obj[1] = tab_emp.getValor(i, "VALOR_IMPORTAR");
			for (int k = 0; k < lis_importa.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}

			return obj;
		}
	}
	return null;
}



private Object[] getAcumuladoTotal(String documento,int tipo_rubro) {
	for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
		if (tab_emp_total.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP").equalsIgnoreCase(documento)) {
			Object[] obj = new Object[3];
			obj[0] = i;
			if (tipo_rubro==1) {
				obj[1] = tab_emp_total.getValor(i, "VALOR25N");
	
			}else if (tipo_rubro==2) {
				obj[1] = tab_emp_total.getValor(i, "VALOR60");
	
			}else if (tipo_rubro==3) {
				obj[1] = tab_emp_total.getValor(i, "VALOR25");

			}else if (tipo_rubro==4) {
				obj[1] = tab_emp_total.getValor(i, "VALOR50");

			}else if (tipo_rubro==5) {
				obj[1] = tab_emp_total.getValor(i, "VALOR100");

			}
			
			if (tipo_rubro==1) {

			for (int k = 0; k < lis_importa.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			if (tipo_rubro==2) {
			for (int k = 0; k < lis_importa60.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			
			if (tipo_rubro==3) {
			for (int k = 0; k < lis_importa25.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}

			if (tipo_rubro==4) {
			for (int k = 0; k < lis_importa50.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			
			if (tipo_rubro==5) {
				for (int k = 0; k < lis_importa100.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			return obj;
		}
	}
	return null;
}


public AutoCompletar getAut_rubros() {
	return aut_rubros;
}



public void setAut_rubros(AutoCompletar aut_rubros) {
	this.aut_rubros = aut_rubros;
}

public void seleccionoRubro(SelectEvent evt) {
	aut_rubros.onSelect(evt);
}



public Dialogo getDia_valida_empleado() {
	return dia_valida_empleado;
}



public void setDia_valida_empleado(Dialogo dia_valida_empleado) {
	this.dia_valida_empleado = dia_valida_empleado;
}


public void aceptarImportar() {

	if (upl_archivo_por_rubro.getNombreReal() == null) {
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
	
	
	if (aut_rubros.getValor() == null) {
		utilitario.agregarMensajeInfo("Debe seleccionar un rubro", "");
		return;
	}


if (x==1) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==2) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==3) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==4) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==5) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}

	
}	
	tab_planificacion_hxe.setCondicion("IDE_asjei IN ("+jefe_inmediato_planificacion+") AND IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" and ide_gtemp in ("+empleado+")");
	tab_planificacion_hxe.ejecutarSql();
	tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
	tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
	dia_importar.cerrar();
	dia_valida_empleado.cerrar();
	//Recupera los empleados de la nomina

	/*TablaGenerica tab_emp_dep=utilitario.consultar("");
	//Recorre la tabla de empleados y compara con la lista obtenida del archivo xls


	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				//insertar
				lis_importa.remove(k);
				break;
			}
		}			d
	}*/
	//String ide_gtemp=obtenerEmpleadosHXE("1","15");

	
		//dia_importar.cerrar();
		//dia_valida_empleado.cerrar();
		// tab_detalle_rol.ejecutarValorForanea(tab_rol.getValorSeleccionado());
		//cargarEmpleadosDepartamento();
		//cargarDetallesRol();

//	}
/* else {
		// Aplica solo a la nomina seleccionada
		TablaGenerica tab_per_rol = ser_nomina.getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));
		String fecha_ini_gepro = tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
		String fecha_fin_gepro = tab_per_rol.getValor("FECHA_FINAL_GEPRO");

		
		boolean nomina_manual=false;
		String str_ide_nrdtn="";
		if (tab_rol.getValor("IDE_NRDTN").equals("20")) {
			str_ide_nrdtn="";
			str_ide_nrdtn="4";
			nomina_manual=true;	
		}else if(tab_rol.getValor("IDE_NRDTN").equals("21")){
			str_ide_nrdtn="";
			str_ide_nrdtn="2";					
			nomina_manual=true;		
		}else{
			nomina_manual=false;	
			str_ide_nrdtn=tab_rol.getValor("IDE_NRDTN");
		}
			
		String str_IDE_NRDER = ser_nomina.getDetalleRubro(str_ide_nrdtn, aut_rubros.getValor()).getValor("IDE_NRDER");

		if (str_IDE_NRDER != null && !str_IDE_NRDER.isEmpty()) {
			if (ser_nomina.getRol(tab_rol.getValor("IDE_NRROL")).getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))) {
				// solo estado pre nomina

				if (ser_nomina.importarValoresRubro(lis_importa, tab_rol.getValor("IDE_NRROL"), tab_rol.getValor("IDE_NRDTN"), str_IDE_NRDER, fecha_ini_gepro, fecha_fin_gepro,nomina_manual)) {
					utilitario.agregarMensaje("Los valores se importaron correctamente", "");
				}

				dia_importar.cerrar();
				dia_valida_empleado.cerrar();
				cargarEmpleadosDepartamento();
				cargarDetallesRol();
			}
		} else {
			utilitario.agregarMensajeInfo("No se puede importar", "La nomina seleccionada no tiene estado PRE-NOMINA");
		}
	}*/
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
	
	for (int x = 1; x <= 5; x++) {
		

if (x==1) {
	if (importarValoresRubro(lis_importa,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==2) {
	if (importarValoresRubro(lis_importa60,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==3) {
	if (importarValoresRubro(lis_importa25,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==4) {
	if (importarValoresRubro(lis_importa50,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==5) {
	if (importarValoresRubro(lis_importa100,x)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
	
}	
	tab_planificacion_hxe.setCondicion("IDE_asjei IN ("+jefe_inmediato_planificacion+") AND IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" and ide_gtemp in ("+empleado+")");
	tab_planificacion_hxe.ejecutarSql();
	tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
	tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
	dia_importar_total.cerrar();
	dia_valida_empleado_total.cerrar();
	//Recupera los empleados de la nomina

	/*TablaGenerica tab_emp_dep=utilitario.consultar("");
	//Recorre la tabla de empleados y compara con la lista obtenida del archivo xls


	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				//insertar
				lis_importa.remove(k);
				break;
			}
		}			d
	}*/
	//String ide_gtemp=obtenerEmpleadosHXE("1","15");

	
		//dia_importar.cerrar();
		//dia_valida_empleado.cerrar();
		// tab_detalle_rol.ejecutarValorForanea(tab_rol.getValorSeleccionado());
		//cargarEmpleadosDepartamento();
		//cargarDetallesRol();

//	}
/* else {
		// Aplica solo a la nomina seleccionada
		TablaGenerica tab_per_rol = ser_nomina.getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));
		String fecha_ini_gepro = tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
		String fecha_fin_gepro = tab_per_rol.getValor("FECHA_FINAL_GEPRO");

		
		boolean nomina_manual=false;
		String str_ide_nrdtn="";
		if (tab_rol.getValor("IDE_NRDTN").equals("20")) {
			str_ide_nrdtn="";
			str_ide_nrdtn="4";
			nomina_manual=true;	
		}else if(tab_rol.getValor("IDE_NRDTN").equals("21")){
			str_ide_nrdtn="";
			str_ide_nrdtn="2";					
			nomina_manual=true;		
		}else{
			nomina_manual=false;	
			str_ide_nrdtn=tab_rol.getValor("IDE_NRDTN");
		}
			
		String str_IDE_NRDER = ser_nomina.getDetalleRubro(str_ide_nrdtn, aut_rubros.getValor()).getValor("IDE_NRDER");

		if (str_IDE_NRDER != null && !str_IDE_NRDER.isEmpty()) {
			if (ser_nomina.getRol(tab_rol.getValor("IDE_NRROL")).getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))) {
				// solo estado pre nomina

				if (ser_nomina.importarValoresRubro(lis_importa, tab_rol.getValor("IDE_NRROL"), tab_rol.getValor("IDE_NRDTN"), str_IDE_NRDER, fecha_ini_gepro, fecha_fin_gepro,nomina_manual)) {
					utilitario.agregarMensaje("Los valores se importaron correctamente", "");
				}

				dia_importar.cerrar();
				dia_valida_empleado.cerrar();
				cargarEmpleadosDepartamento();
				cargarDetallesRol();
			}
		} else {
			utilitario.agregarMensajeInfo("No se puede importar", "La nomina seleccionada no tiene estado PRE-NOMINA");
		}
	}*/
}


public Tabla getTab_emp() {
	return tab_emp;
}



public void setTab_emp(Tabla tab_emp) {
	this.tab_emp = tab_emp;
}

public boolean importarValoresRubro(List lis_importa,int tipo_hora_extra){

	String str_sql="";
	str_sql="SELECT cbph.ide_cobph,  EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   anio.detalle_geani,mes.detalle_gemes,cbph.ide_asjei  "
			+ "FROM con_biometrico_plan_hxe cbph  "
			+ "left join gth_empleado emp on emp.ide_gtemp=cbph.ide_gtemp   "
			+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani  "
			+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
			+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+anio+" and  cbph.ide_asjei="+jefe_inmediato_planificacion+" and (cbph.aprobado_cobph=false or cbph.aprobado_cobph is null)" 
			+ " order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP";
	
	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);

	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				lis_importa.remove(k);
				break;
			}
		}			

		//Metodo Insertar Resumen

		if(str_valor!=null){
			String ide_cobrh="";
			ide_cobrh=tab_emp_dep.getValor(j, "ide_cobph");
				if (tipo_hora_extra==1) {
					utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set horas25_loep_cobph="+str_valor+" where ide_cobph=" + ide_cobrh);				
				}else if (tipo_hora_extra==2) {
					utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set horas60_loep_cobph="+str_valor+" where ide_cobph=" + ide_cobrh);				
				}else if (tipo_hora_extra==3) {
					utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set horas25_ct_cobph="+str_valor+" where ide_cobph=" + ide_cobrh);				

				}else if (tipo_hora_extra==4) {
					utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set horas50_ct_cobph="+str_valor+" where ide_cobph=" + ide_cobrh);				

				}else if (tipo_hora_extra==5) {
					utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set horas100_loep_ct_cobph="+str_valor+" where ide_cobph=" + ide_cobrh);				
				}	
		}
	}

	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;
}


 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}



public void insertarTablaResumen(
		 int ide_gtemp ,
		 String ide_gemes,
		 String ide_geani,
		 String num_hxe25_cobrh,
		 String num_hxeNoc25_cobrh,
		 String num_hxe50_cobrh,
		 String num_hxe60_cobrh,
		 String num_hxe100_cobrh,
		 String fecha_registro_cobrh
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("con_biometrico_resumen_hxe", "ide_cobrh"));
		String codigo=tab_codigo.getValor("codigo");
		

		utilitario.getConexion().ejecutarSql("INSERT INTO con_biometrico_resumen_hxe(" 
					+ "ide_cobrh, "
					+ "ide_gtemp, "
			  		+ "ide_gemes, "
			  		+ "ide_geani, "
			  		+ "num_hxe25_cobrh, "
			  		+ "num_hxenoc25_cobrh, "
			  		+ "num_hxe50_cobrh, "
			  		+ "num_hxe60_cobrh, "
			  		+ "num_hxe100_cobrh, "
			  		+ "activo_cobrh, "
			  		+ "fecha_registro_cobrh)" + 

			  		" values( " +codigo + ", "
			  		+ ""+ ide_gtemp+", "
			  		+ "'"+ide_gemes+"', "
			  		+ "'"+ide_geani+"', "
			  		+ "'"+num_hxe25_cobrh+"', "
			  		+ "'"+num_hxeNoc25_cobrh+"', "
			  		+ "'"+num_hxe50_cobrh+"', "
			  		+ "'"+num_hxe60_cobrh+"', "
			  		+ "'"+num_hxe100_cobrh+"', "
			  		+ "true, "
			  		+ "'"+fecha_registro_cobrh+"')"); 
	 
}




public  int getIngresoResumen(int ide_gtemp,String ide_gemes,String ide_geani){
	int retornaValor=0;
	TablaGenerica tabResumen=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes, "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, usuario_ingre,  "
			+ "ide_asjei "
			+ " FROM con_biometrico_plan_hxe  "
			+ "where ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" and ide_gtemp="+ide_gtemp);
if (tabResumen.getTotalFilas()>0) {
	retornaValor=Integer.parseInt(tabResumen.getValor("ide_cobph"));
}else {
	retornaValor=0;
}
	return retornaValor;
}



public Tabla getTab_resumen_hxe() {
	return tab_resumen_hxe;
}



public void setTab_resumen_hxe(Tabla tab_resumen_hxe) {
	this.tab_resumen_hxe = tab_resumen_hxe;
}



public String obtenerEmpleadosHXE(String ide_gemes, String ide_geani){
	String retornaValor="";
	
	///
	
	
	
	TablaGenerica tabResumen=utilitario.consultar("SELECT ashme.ide_ashme, ashme.ide_gemes, emp.ide_gtemp, EMP.DOCUMENTO_IDENTIDAD_GTEMP,ashme.dia1, ashme.dia2, ashme.dia3, ashme.dia4, ashme.dia5, "
			+ "ashme.dia6, ashme.dia7, ashme.dia8, ashme.dia9, ashme.dia10, ashme.dia11, ashme.dia12, ashme.dia13, ashme.dia14, ashme.dia15,  "
			+ "ashme.dia16, ashme.dia17, ashme.dia18, ashme.dia19, ashme.dia20, ashme.dia21, ashme.dia22, ashme.dia23, ashme.dia24,  "
			+ "ashme.dia25, ashme.dia26, ashme.dia27, ashme.dia28, ashme.dia29, ashme.dia30, ashme.dia31, ashme.aplica_hora_extra,  "
			+ "ashme.activo_ashme, ashme.ide_geani, ashme.ide_sucursal, ashme.ide_geare, ashme.ide_gedep,   "
			+ "ashme.num_extra_ashem, ashme.num_suple_ashem, ashme.ide_geedp,  "
			+ "ashme.ide_asjei  "
			+ "FROM asi_horario_mes_empleado ashme   "
			+ "left join gth_empleado emp on emp.ide_gtemp=ashme.ide_gtemp  "
			+ "where ide_geani= and ide_gemes= and ide_asjei=");
	
	/*TablaGenerica tabResumen=utilitario.consultar("SELECT ide_cobrh, ide_gtemp, ide_gemes, ide_geani, num_hxe25_cobrh, "
			+ "num_hxenoc25_cobrh, num_hxe50_cobrh, num_hxe60_cobrh, num_hxe100_cobrh,  "
			+ "activo_cobrh, fecha_registro_cobrh "
			+ "FROM con_biometrico_resumen_hxe "
			+ "where ide_gemes="+ide_gemes+" and ide_geani="+ide_geani);
	
	*/
	
	
	if (tabResumen.getTotalFilas()>0) {
		for (int i = 0; i < tabResumen.getTotalFilas(); i++) {
			if (tabResumen.getTotalFilas()==1) {
				retornaValor=tabResumen.getValor(i,"ide_gtemp");

			}else if((tabResumen.getTotalFilas()-1)==i){
				retornaValor+=tabResumen.getValor(i,"ide_gtemp");
			}else{
			retornaValor+=tabResumen.getValor("ide_gtemp")+",";
		}
		}
	}else {
		retornaValor="-1";
	}
		return retornaValor;
	
}

public void EnviarCorreoAccion(String ide_gtemp, String area, int tipo_rol) throws Exception{
	try {
	TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
	String correo="juan.ayerve@emgirs.gob.ec";//tab_correo.getValor("detalle_gtcor");
	String correo_tthh="miguel.ramon@emgirs.gob.ec";//tab_correo.getValor("detalle_gtcor");

	//TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
	//		+ "clave_corr from sis_correo where ide_corr=2"); 
	String smtp_correo="mail.emgirs.gob.ec"; 
	String puertoEnvio="25"; 
	String correo_envio="srecomendaciones@emgirs.gob.ec";
	String usuario_envio="srecomendaciones"; 
	String clave_correo="R3comendaciones.2022";
			
	
	//String correo="juan.ayerve@emgirs.gob.ec";//
	//String usuario_envio="jayerve"; 
	//String clave_correo="EmgirsJCA2023";
	
	/*TablaGenerica tab_correo_tthh= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+1161);
	TablaGenerica tab_correo_envio_tthh= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
			+ "clave_corr from sis_correo where ide_corr=2"); 
	String smtp_correo_tthh=tab_correo_envio.getValor("smtp_corr"); 
	String puertoEnvio_tthh=tab_correo_envio.getValor("puerto_corr"); 
	String correo_envio_tthh=tab_correo_envio.getValor("correo_corr");
	String usuario_envio_tthh=tab_correo_envio.getValor("usuario_corr"); 
	String clave_correo_tthh=tab_correo_envio.getValor("clave_corr");
	
	*/
	
	
	//TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
		//	+ "clave_corr from sis_correo where ide_corr=2"); 
	//String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
	//String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
	//String correo_envio=tab_correo_envio.getValor("correo_corr");
	//String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
	//String clave_correo=tab_correo_envio.getValor("clave_corr");
	
	
	//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
	EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
			String strNombreEmpleado="";
			strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				
	try {

	
		
		//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2), null);
		/*EnviaMailInternoAccion(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones, ide_gtemp,tipo_rol,fecha,descripcion), null,strNombreEmpleado,
		ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol,fecha,descripcion);*/
		//util.EnviaMailInterno(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,1), null);
		//util.EnviaMailInterno(envMail, correo_tthh, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2), null);

		enviaMailInterno(envMail, correo_envio, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2));
		enviaMailInterno(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,1));
		enviaMailInterno(envMail, correo_tthh, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2));

		System.out.println("Enviando Correo.........");

		} catch (Exception e) {
		System.out.println("Error en el envio de correo"+e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
		}

			} 		

	private void enviaMailInterno(EnvioMail envMail, String str_mail, String str_asunto, String str_mensaje)
	{
		envMail.setAsunto(str_asunto);
		envMail.setCuerpoHtml(str_mensaje);
		envMail.setPara(str_mail);
		pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
		
		if(obj.getRespuesta())
		{
			utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
		}
		else
			utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
	}

public void dibujarConfirmar(){
	if (tab_planificacion_hxe.getTotalFilas()==0) {
		utilitario.agregarMensaje("No se puede realizar el Envio", "No se hab encontrado registros validos");
		return;
	}

	con_guardar.setTitle("CONFIRMACION ENVIO DE PLAN DE ACCIÓN");
	con_guardar.setMessage("ESTA SEGURO DE ENVIAR EL PLAN DE ACCIÓN");
	con_guardar.getBot_aceptar().setMetodo("enviarPlan");
	con_guardar.dibujar();
	utilitario.addUpdate("con_guardar");
}



public void enviarPlan(){		
String ide_seinf="",ide_serec="";

try {
	EnviarCorreoAccion(ide_gtemp_jefe_inmediato,ide_geare,1);
	utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set aprobado_cobph=true where ide_gemes=" +mes+" and ide_geani="+anio+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")" );				
	con_guardar.cerrar();
	tab_planificacion_hxe.setCondicion("IDE_asjei IN ("+jefe_inmediato_planificacion+") AND IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" and ide_gtemp in ("+empleado+")");
	tab_planificacion_hxe.ejecutarSql();
	tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
	tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
	utilitario.addUpdate("con_guardar");
	utilitario.agregarMensaje("Planificacion de Horas Extra enviada", "Se ha enviado correctamente" );
} catch (Exception e) {
	// TODO Auto-generated catch block
System.out.println("Error al enviar plan");

}


}


public String obtenerNombresApellidosEmpleadoCorreo(String empleado){
	String retornoValor="";

	TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
			"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
			"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
			"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
			"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
			"FROM GTH_EMPLEADO EMP  " +
			" WHERE EMP.IDE_GTEMP="+empleado);
	
	return retornoValor=tabEmpleado.getValor("NOMBRES_APELLIDOS");

}






public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String area, int tipo_mensaje) {
	 
	String html="";
	 /*="<p>Estimado "+strNombreEmpleado+ ", "
             + "</p>\n"
             + "<p>&nbsp;</p>\n";*/
	if (tipo_mensaje==1) {
	//Tipo rol cambio estado accion
		html+="<p> Su planificación de horas extra ha sido enviada para aprobacion por parte de la Coordinacion de Talento Humano .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	         //    + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
		 html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
		 
	}else {
		html+="<p> Ha recibido un correo de planificacion de horas extra de "+area+", pendiente de revision .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	         //    + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
		 html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	}
		 
		 
         return html;

	}



@Override
public void abrirListaReportes() {
		rep_reporte.dibujar();
	
}


public void aceptarReporte() {
	if (rep_reporte.getReporteSelecionado().equals("PLANIFICACION MENSUAL HORAS EXTRA")) {
		if (rep_reporte.isVisible()) {
			p_parametros = new HashMap();
			rep_reporte.cerrar();
			sel_mes_reporte.dibujar();
		} else if (sel_mes_reporte.isVisible()) {
			try {
				if (sel_mes_reporte.getValorSeleccionado() != null && !sel_mes_reporte.getValorSeleccionado().isEmpty()) {
					sel_mes_reporte.cerrar();
					sel_anio_reporte.dibujar();
				} else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado Mes");
				}
			} catch (Exception e) {
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado Mes");
			}
		} else if (sel_anio_reporte.isVisible()) {
			try {
				if (sel_anio_reporte.getValorSeleccionado() != null && !sel_anio_reporte.getValorSeleccionado().isEmpty()) {
					p_parametros.put("IDE_GEMES", Integer.parseInt(mes));
					p_parametros.put("IDE_GEANI", Integer.parseInt(sel_anio_reporte.getValorSeleccionado()));
					if(tipo_perfil.equals("1")){
						p_parametros.put("ide_asjei","1,2,3,4,5,6,7,8,9,10");	
					}else {
						p_parametros.put("ide_asjei",jefe_inmediato_planificacion);
					}
					
					p_parametros.put("AREA",area);
					p_parametros.put("p_coordinador_tthh",utilitario.getVariable("p_gth_coordinador_tthh"));
					p_parametros.put("elaborado_por",NombreEmpleado);
					p_parametros.put("cargo_elaborado",cargo_elaborado);	
					p_parametros.put("titulo", "PLANIFICACIÓN EN HORAS NOCTURNAS, EXTRAORDINARIAS Y SUPLEMENTARIAS LOEP Y CÓDIGO DE TRABAJO");
					self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					System.out.print("reporte parametro..." + p_parametros);
					sel_anio_reporte.cerrar();
					self_reporte.dibujar();
					sel_anio_reporte.cerrar();
				} else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado el anio");
				}
			} catch (Exception e) {
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado el anio");
			}
		} 
	} 
	
}



public Reporte getRep_reporte() {
	return rep_reporte;
}



public void setRep_reporte(Reporte rep_reporte) {
	this.rep_reporte = rep_reporte;
}



public Map getP_parametros() {
	return p_parametros;
}



public void setP_parametros(Map p_parametros) {
	this.p_parametros = p_parametros;
}



public SeleccionFormatoReporte getSelf_reporte() {
	return self_reporte;
}



public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
	this.self_reporte = self_reporte;
}



public SeleccionTabla getSel_mes_importar() {
	return sel_mes_importar;
}



public void setSel_mes_importar(SeleccionTabla sel_mes_importar) {
	this.sel_mes_importar = sel_mes_importar;
}



public SeleccionTabla getSel_anio_importar() {
	return sel_anio_importar;
}



public void setSel_anio_importar(SeleccionTabla sel_anio_importar) {
	this.sel_anio_importar = sel_anio_importar;
}



public SeleccionTabla getSel_empleado_importar() {
	return sel_empleado_importar;
}



public void setSel_empleado_importar(SeleccionTabla sel_empleado_importar) {
	this.sel_empleado_importar = sel_empleado_importar;
}



public void validarActividad(AjaxBehaviorEvent evt){
	//MAXIMO 48 HORAS MES
	int valorMax25Loep=0,horas25_loep_cobph=0,horas60_loep_cobph=0,horas25_cobph=0,horas50_cobph,horas100_cobph=0;
	valorMax25Loep=48;
	tab_planificacion_hxe.modificar(evt);
	
   	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where  (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
		
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
		
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("actividades_cobph",tab_planificacion_hxe_temp1.getValor("actividades_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "actividades_cobph","");
		return;
	}	

}

public void validarMaxHoras25Loep(AjaxBehaviorEvent evt){
	//MAXIMO 48 HORAS MES
	int valorMax25Loep=0,horas25_loep_cobph=0,horas60_loep_cobph=0,horas25_cobph=0,horas50_cobph,horas100_cobph=0;
	valorMax25Loep=48;
	tab_planificacion_hxe.modificar(evt);
	
   	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	//and (retorno_plan_cobph in(false or retorno_plan_cobph is null )
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
		
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
		
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("horas25_loep_cobph",tab_planificacion_hxe_temp1.getValor("horas25_loep_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas25_loep_cobph","");
		return;
	}
		
	
	horas25_loep_cobph=Integer.parseInt(tab_planificacion_hxe.getValor("horas25_loep_cobph"));
	if (horas25_loep_cobph>valorMax25Loep) {
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "Valor de asignacion no puede ser mayor a "+valorMax25Loep+" horas");
		tab_planificacion_hxe.actualizar();
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas25_loep_cobph","");
		return;
	}
	

}
public void validarMaxHoras60Loep(AjaxBehaviorEvent evt){
	//NO TIENE RESTRICCION
	tab_planificacion_hxe.modificar(evt);
	int valorMax25Loep=0,horas25_loep_cobph=0,horas60_loep_cobph=0,horas25_cobph=0,horas50_cobph,horas100_cobph=0;

   	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where  (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	   	
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("horas60_loep_cobph",tab_planificacion_hxe_temp1.getValor("horas60_loep_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas60_loep_cobph","");
		return;
	}   	
	  
	tab_planificacion_hxe.getValor("horas60_loep_cobph");
	

}
public void validarMaxHoras25ct(AjaxBehaviorEvent evt){
	//NO TIENE RESTRICCION
	tab_planificacion_hxe.modificar(evt);

	int valorMax25Loep=0,horas25_loep_cobph=0,horas60_loep_cobph=0,horas25_cobph=0,horas50_cobph,horas100_cobph=0;

   	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where  (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	   	
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("horas25_ct_cobph",tab_planificacion_hxe_temp1.getValor("horas25_ct_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas25_ct_cobph","");
		return;
	}   
		
	tab_planificacion_hxe.getValor("horas25_ct_cobph");
	

}
public void validarMaxHoras50ct(AjaxBehaviorEvent evt){
	//MAXIMO 48 HORAS MES
	int valorMax50Ct=0,horas50_ct_cobph=0;
	valorMax50Ct=48;
	tab_planificacion_hxe.modificar(evt);
	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where  (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
		
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
		
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("horas50_ct_cobph",tab_planificacion_hxe_temp1.getValor("horas50_ct_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas50_ct_cobph","");
		return;
	}
		

	
	horas50_ct_cobph=Integer.parseInt(tab_planificacion_hxe.getValor("horas50_ct_cobph"));
	if (horas50_ct_cobph>horas50_ct_cobph) {
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "Valor de asignacion no puede ser mayor a "+valorMax50Ct+" horas");
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas50_ct_cobph","");
		return;
	}
	
	tab_planificacion_hxe.getValor("horas50_ct_cobph");


}
public void validarMaxHoras100Loepct(AjaxBehaviorEvent evt){
	//MAXIMO 100 HORAS MES
	int valorMax25Loep=0,horas25_loep_cobph=0,horas60_loep_cobph=0,horas25_cobph=0,horas50_cobph,horas100_cobph=0;
	int valorMax100LoepCt=0,horas100_loep_ct_cobph=0;
	valorMax100LoepCt=60;
	
	tab_planificacion_hxe.modificar(evt);
	
	TablaGenerica tab_planificacion_hxe_temp=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where  (aprobado_cobph=false or aprobado_cobph is null) and ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
	
	if (tab_planificacion_hxe_temp.getTotalFilas()==0) {
		
	   	TablaGenerica tab_planificacion_hxe_temp1=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
				+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
				+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
				+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
				+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph  "
				+ "FROM con_biometrico_plan_hxe  "
				+ "where ide_cobph="+tab_planificacion_hxe.getValor("ide_cobph"));
		
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
		tab_planificacion_hxe.setValor("horas100_loep_ct_cobph",tab_planificacion_hxe_temp1.getValor("horas100_loep_ct_cobph"));
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas100_loep_ct_cobph","");
		return;
	}
		
	
	
	horas100_loep_ct_cobph=Integer.parseInt(tab_planificacion_hxe.getValor("horas100_loep_ct_cobph"));
	if (horas100_loep_ct_cobph>valorMax100LoepCt) {
		utilitario.agregarMensajeInfo("No se puede asignar el valor", "Valor de asignacion no puede ser mayor a "+valorMax100LoepCt+" horas");
		utilitario.addUpdateTabla(tab_planificacion_hxe, "horas100_loep_ct_cobph","");
		return;
	}
	
	tab_planificacion_hxe.getValor("horas50_ct_cobph");
	

}


public void cancelarImportarEmpleados() {
	dia_valida_empleado.cerrar();
	dia_importar.cerrar();
	upl_archivo.limpiar();
    tab_planificacion_hxe.setCondicion("ide_cobph=-1");
    tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    tab_planificacion_hxe.ejecutarSql();
    tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion,dia_importar,dia_valida_empleado");
}

public void cancelarImportarTotal() {
	dia_valida_empleado_total.cerrar();
	dia_importar_total.cerrar();
	upl_archivo.limpiar();
    tab_planificacion_hxe.setCondicion("ide_cobph=-1");
    tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    tab_planificacion_hxe.ejecutarSql();
    tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion,dia_valida_empleado_total,dia_importar_total");
}

public Tabla getTab_planificacion_hxe_observacion() {
	return tab_planificacion_hxe_observacion;
}



public void setTab_planificacion_hxe_observacion(
		Tabla tab_planificacion_hxe_observacion) {
	this.tab_planificacion_hxe_observacion = tab_planificacion_hxe_observacion;
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



public Grid getGrid_tabla_emp_sum_totales() {
	return grid_tabla_emp_sum_totales;
}



public void setGrid_tabla_emp_sum_totales(Grid grid_tabla_emp_sum_totales) {
	this.grid_tabla_emp_sum_totales = grid_tabla_emp_sum_totales;
}



public Grid getGrid_tabla_emp_sum_totales25Loep() {
	return grid_tabla_emp_sum_totales25Loep;
}



public void setGrid_tabla_emp_sum_totales25Loep(
		Grid grid_tabla_emp_sum_totales25Loep) {
	this.grid_tabla_emp_sum_totales25Loep = grid_tabla_emp_sum_totales25Loep;
}



public Grid getGrid_tabla_emp_sum_totales60() {
	return grid_tabla_emp_sum_totales60;
}



public void setGrid_tabla_emp_sum_totales60(Grid grid_tabla_emp_sum_totales60) {
	this.grid_tabla_emp_sum_totales60 = grid_tabla_emp_sum_totales60;
}



public Grid getGrid_tabla_emp_sum_totales25() {
	return grid_tabla_emp_sum_totales25;
}



public void setGrid_tabla_emp_sum_totales25(Grid grid_tabla_emp_sum_totales25) {
	this.grid_tabla_emp_sum_totales25 = grid_tabla_emp_sum_totales25;
}



public Grid getGrid_tabla_emp_sum_totales50() {
	return grid_tabla_emp_sum_totales50;
}



public void setGrid_tabla_emp_sum_totales50(Grid grid_tabla_emp_sum_totales50) {
	this.grid_tabla_emp_sum_totales50 = grid_tabla_emp_sum_totales50;
}



public Grid getGrid_tabla_emp_sum_totales100() {
	return grid_tabla_emp_sum_totales100;
}



public void setGrid_tabla_emp_sum_totales100(Grid grid_tabla_emp_sum_totales100) {
	this.grid_tabla_emp_sum_totales100 = grid_tabla_emp_sum_totales100;
}



public SeleccionTabla getSel_mes_reporte() {
	return sel_mes_reporte;
}



public void setSel_mes_reporte(SeleccionTabla sel_mes_reporte) {
	this.sel_mes_reporte = sel_mes_reporte;
}



public SeleccionTabla getSel_anio_reporte() {
	return sel_anio_reporte;
}



public void setSel_anio_reporte(SeleccionTabla sel_anio_reporte) {
	this.sel_anio_reporte = sel_anio_reporte;
}



public SeleccionTabla getSel_empleado_importarTotales() {
	return sel_empleado_importarTotales;
}



public void setSel_empleado_importarTotales(
		SeleccionTabla sel_empleado_importarTotales) {
	this.sel_empleado_importarTotales = sel_empleado_importarTotales;
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



public void setSel_anio_importarTotales(SeleccionTabla sel_anio_importarTotales) {
	this.sel_anio_importarTotales = sel_anio_importarTotales;
}



public Grid getGrid_tabla_emp() {
	return grid_tabla_emp;
}



public void setGrid_tabla_emp(Grid grid_tabla_emp) {
	this.grid_tabla_emp = grid_tabla_emp;
}



public Grid getGrid_tabla_emp_total() {
	return grid_tabla_emp_total;
}



public void setGrid_tabla_emp_total(Grid grid_tabla_emp_total) {
	this.grid_tabla_emp_total = grid_tabla_emp_total;
}



public Upload getUpl_archivo_por_rubro() {
	return upl_archivo_por_rubro;
}



public void setUpl_archivo_por_rubro(Upload upl_archivo_por_rubro) {
	this.upl_archivo_por_rubro = upl_archivo_por_rubro;
}


/*
public boolean importarValoresRubro(List lis_importa,String ide_nrrol,String ide_nrdtn,String ide_nrder,String fecha_ini_gepro,String fecha_fin_gepro, boolean nomina_manual){

	String str_sql="";
	if (nomina_manual) {
		str_sql=getSqlEmpleadosTipoNominaManual(ide_nrdtn,fecha_fin_gepro);
	}else if(!ide_nrdtn.equals("2") || !ide_nrdtn.equals("4")){
		str_sql=getSqlEmpleadosTipoNominaRoles(ide_nrdtn,ide_nrrol);
	}else{	
	str_sql=getSqlEmpleadosTipoNomina(ide_nrdtn,fecha_fin_gepro);
	utilitario.getConexion().agregarSql("update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
			"where FECHA_VENCIMIENTO_NRAMO " +
			"BETWEEN to_date ('"+fecha_ini_gepro+"','yyyy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yyyy-mm-dd') " +
			"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
			"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
			"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
			"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
			"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
			"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
			"WHERE DTN.IDE_NRDTN IN ("+ide_nrdtn+"))))");
	}

	//Recupera los empleados de la nomina

	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);
	//Recorre la tabla de empleados y compara con la lista obtenida del archivo xls

	cargarRubrosRolVacia();

	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				lis_importa.remove(k);
				break;
			}
		}			


		if(str_valor!=null){
			//Cargo el rol del empleado seleccionado
			utilitario.getConexion().agregarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+str_valor+" " +
					"where IDE_NRROL="+ide_nrrol+" " +
					"and IDE_NRDER="+ide_nrder+" " +
					"and IDE_GEEDP="+tab_emp_dep.getValor(j,"ide_geedp"));
		}
	}

	utilitario.getConexion().agregarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=0 where IDE_NRROL="+ide_nrrol);

	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;
}*/


}
