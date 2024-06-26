/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.awt.image.BandedSampleModel;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.validation.constraints.Size;

import jxl.Sheet;
import jxl.Workbook;

import org.codehaus.groovy.tools.shell.commands.SetCommand;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Imprimir;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;

/**
 *
 * @author DELL-USER
 */
public class pre_biometrico_marcaciones_registro_manual extends Pantalla {

	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);	
	

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_resumen_marcaciones = new Tabla(); 
	private Dialogo dia_valida_empleado = new Dialogo();

    
    private Tabla tab_novedad = new Tabla();
    private Tabla tab_archi_adjunto = new Tabla();

    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private boolean banderaFeriados=false,sinIngreso=false;
    @EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    @EJB
    private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	private Grid grid_tabla_emp = new Grid();

    
    TablaGenerica tabDiasFeriados;
    
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	String horaAnterior="",horaAnteriorAlm="",horaAnteriorAlm1="",tarjeta_marcacion_gtemp="";
	int ide_gtgre=0,turno=0;
	
	private boolean estado_empleado_nocturno=false;
	private boolean banderaCambioHorario,banderaCambioHorarioPermisoOnline;
    private Integer ide_geedp=0,empleadoRegistroManual=0;
	private String  pos_marcacionEntrada="",pos_marcacionTarde="",pos_marcacionNocturno="",horaActualizarPegado="";
    private Integer estadoMarcacionBiometrico=0,numMarcacionesBiometrico=0;
    String empleadoSeleccionado="",listEmpleadoHorarioAnual="",listEmpleadoHorarioMensual="";
    
	StringBuilder str_ide_empleado_asignado=new StringBuilder();

    private SeleccionTabla sel_mes_registro_manual= new SeleccionTabla();
    private SeleccionTabla sel_anio_registro_manual= new SeleccionTabla();
    private SeleccionTabla sel_empleado_registro_manual= new SeleccionTabla();
    private SeleccionTabla sel_empleado_consulta= new SeleccionTabla();

	String ide_gtempxx="";
	int tipo_hora_extra=0,x=0;
	String ide_geare="",ide_gtemp_jefe_inmediato="",cargo_elaborado="",area="";
    private String meses="",anios="",mesEditar="",anioEditar="",mes="",anio="",empleado="",jefe_inmediato_planificacion="",tipo_perfil="",empleado_importar="",NombreEmpleado="";
	TablaGenerica tabEmpleadoXJefeInmediato;
	TablaGenerica tabCargoJefeInmediato;
	String empleado_asignado="",empleado_registro_manual="";
	String ide_persona_cobim="";
    private SeleccionTabla sel_mes_importar= new SeleccionTabla();
    private SeleccionTabla sel_anio_importar= new SeleccionTabla();
	private Dialogo dia_importar = new Dialogo();
	private Upload upl_archivo_por_rubro = new Upload();
	private Upload upl_archivo = new Upload();
	private List<String[]> lis_importa = null; // Guardo los empleados y el

	private Editor edi_mensajes = new Editor();
	private Tabla tab_emp = new Tabla();

    public pre_biometrico_marcaciones_registro_manual() {
    	
    	
    	
		
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
			
			//NombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tabCargoJefeInmediato.getValor("ide_gtemp"));				
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
				
				//NombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tabCargoJefeInmediato.getValor("ide_gtemp"));				
				cargo_elaborado="COORDINADOR "+ide_geare;
				ide_gtemp_jefe_inmediato=tabCargoJefeInmediato.getValor("ide_gtemp");
	    		jefe_inmediato_planificacion=tabJefeInmediato.getValor("ide_asjei");

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
    	
    
    	
    	
    	
		cal_fecha_inicial.setFechaActual();
	//	bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		//bar_botones.agregarComponente(cal_fecha_final);

    	    	
    	//Boton Reporte de Empleado
    //	Boton bot_rep_biometrico= new Boton();
    //	bot_rep_biometrico.setIcon("ui-icon-calculator");
    //	bot_rep_biometrico.setMetodo("importarMarcaciones");
    //	bot_rep_biometrico.setValue("Importar Marcaciones");
    //	bot_rep_biometrico.setTitle("Importar Marcaciones");
    //	bar_botones.agregarBoton(bot_rep_biometrico);
    	

    	//Boton Reporte de Empleado
    //	Boton bot_rep_biometrico_eliminar= new Boton();
    //	bot_rep_biometrico_eliminar.setIcon("ui-icon-calculator");
    //	bot_rep_biometrico_eliminar.setMetodo("eliminarMarcaciones");
    //	bot_rep_biometrico_eliminar.setValue("Eliminar Marcaciones");
    //	bot_rep_biometrico_eliminar.setTitle("Eliminar Marcaciones");
    //	bar_botones.agregarBoton(bot_rep_biometrico_eliminar);
    	
    	
    	
    	Boton bot_departamento_biometrico= new Boton();
    	bot_departamento_biometrico.setIcon("ui-icon-calculator");
    	bot_departamento_biometrico.setMetodo("registroManualBiometrico");
    	bot_departamento_biometrico.setValue("Consultar Empleados");
    	bot_departamento_biometrico.setTitle("Ver Reporte de Imòrtaciones");
    	bar_botones.agregarBoton(bot_departamento_biometrico);
    	
    	
    	
    	
    	Boton bot_empleado= new Boton();
    	bot_empleado.setIcon("ui-icon-calculator");
    	bot_empleado.setMetodo("seleccionarEmpleado");
    	bot_empleado.setValue("Importar X Empleado");
    	bot_empleado.setTitle("Importar X Empleado");
    	//bar_botones.agregarBoton(bot_empleado);
    	
    	
    	
    	Boton bot_dias_libres_sin_timbre= new Boton();
    	bot_dias_libres_sin_timbre.setIcon("ui-icon-calculator");
    	bot_dias_libres_sin_timbre.setMetodo("marcacionesLibreSinTimbre");
    	bot_dias_libres_sin_timbre.setValue("Importar Marcaciones Libre /Sin Timbre");
    	bot_dias_libres_sin_timbre.setTitle("Importar Marcaciones Libre /Sin Timbre");
    	//bar_botones.agregarBoton(bot_dias_libres_sin_timbre);
    	
    	
    	
    	Boton bot_empleados_sin_marcacion= new Boton();
    	bot_empleados_sin_marcacion.setIcon("ui-icon-calculator");
    	bot_empleados_sin_marcacion.setMetodo("empleadoSinMarcacion");
    	bot_empleados_sin_marcacion.setValue("Importar Empleados Sin Marcaciones");
    	bot_empleados_sin_marcacion.setTitle("Importar Empleados Sin Marcaciones");
    	///bar_botones.agregarBoton(bot_empleados_sin_marcacion);
    	
    	
    	Boton bot_impor_valores = new Boton();
		bot_impor_valores.setValue("Importar Valores");
		bot_impor_valores.setIcon("ui-icon-note");
		bot_impor_valores.setMetodo("abrirDialogoImportar");
		bar_botones.agregarBoton(bot_impor_valores);
    	
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
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
	  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES,  "
	  		+ "cobim.fecha_evento_cobim, cobim.ide_persona_cobim  "
	  		+ "FROM con_biometrico_marcaciones  cobim  "
	  		+ "LEFT JOIN GTH_EMPLEADO EMP on emp.tarjeta_marcacion_gtemp=trim(cobim.ide_persona_cobim) "
	  		+ "where cobim.ide_cobim=-1");
	  
  
		
		tab_emp.setCampoPrimaria("ide_gtemp");
		tab_emp.setNumeroTabla(15);
		tab_emp.setRows(15);
		tab_emp.setLectura(true);
		tab_emp.dibujar();

		grid_tabla_emp.getChildren().add(tab_emp);
		grid_tabla_emp.setStyle("width:" + (dia_valida_empleado.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_valida_empleado.setDialogo(grid_tabla_emp);
		dia_valida_empleado.setDynamic(false);
		agregarComponente(dia_valida_empleado);

		
		
		
		
		
		dia_importar.setId("dia_importar");
		dia_importar.setTitle("VALIDACION DE REGISTRO MANUAL");
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

	
		gri_impo.getChildren().add(gri_tn);
		gri_impo.getChildren().add(new Etiqueta("Importar Marcaciones Manuales: "));

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
		gri_cuerpo.setMensajeInfo("Esta opcion  permite subir valores a un rubro a partir de un archivo xls");
		gri_cuerpo.getChildren().add(gri_impo);
		gri_cuerpo.getChildren().add(gri_valida);
		gri_cuerpo.getChildren().add(edi_mensajes);
		gri_cuerpo.getChildren().add(new Espacio("0", "10"));

		dia_importar.setDialogo(gri_cuerpo);
		dia_importar.setDynamic(false);

		agregarComponente(dia_importar);
		
		
			
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
			
    	
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);    	
    	
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());
		String fecha_inicio_asnov= fechaIni;
		String fecha_fin_asnov= fechaFin;
		
		
    	sel_mes_registro_manual.setId("sel_mes_registro_manual");
    	sel_mes_registro_manual.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+meses+") ","IDE_GEMES");

    	sel_mes_registro_manual.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
    	sel_mes_registro_manual.setRadio();
    	sel_mes_registro_manual.setTitle("Seleccione Mes Horario");
    	sel_mes_registro_manual.setWidth("20");
    	sel_mes_registro_manual.setHeight("20");
		gru_pantalla.getChildren().add(sel_mes_registro_manual);
		sel_mes_registro_manual.getBot_aceptar().setMetodo("obtenerMesRegistroManual");
		agregarComponente(sel_mes_registro_manual);
		
		 
		sel_anio_registro_manual.setId("sel_anio_registro_manual");
		sel_anio_registro_manual.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+") ","IDE_GEANI");
		sel_anio_registro_manual.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);

		sel_anio_registro_manual.setRadio();
    	sel_anio_registro_manual.setTitle("Seleccione Anio Horario");
		gru_pantalla.getChildren().add(sel_anio_registro_manual);
		sel_anio_registro_manual.setWidth("20");
		sel_anio_registro_manual.setHeight("20");
		sel_anio_registro_manual.getBot_aceptar().setMetodo("obtenerAnioRegistroManual");
		agregarComponente(sel_anio_registro_manual);
		
		
		
	   	sel_empleado_registro_manual.setId("sel_empleado_registro_manual");
		agregarComponente(sel_empleado_registro_manual);
		
		sel_empleado_consulta.setId("sel_empleado_consulta");
		agregarComponente(sel_empleado_consulta);
		
		
		int anioTemp=utilitario.getAnio(fecha_inicio_asnov);
    	TablaGenerica getAnio= utilitario.consultar("select  ide_geani,detalle_geani from gen_anio where detalle_geani  like '%"+anioTemp+"%'");
	    int anio=pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
    	
String sql1="select ide_gtemp,ide_astur  "
				+ "from gth_empleado "
				+ "where ide_gtemp in(select par.ide_gtemp "
				+ "from gen_empleados_departamento_par par "
				+ "where par.activo_geedp=true "
				+ "group by par.ide_gtemp "
				+ "UNION all  "
				+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2 "
				+ "where par2.fecha_finctr_geedp between '"+fechaIni+"' and '"+fechaFin+"' "
				+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true "
				+ "group by ide_gtemp ) "
				+ "group by par2.ide_gtemp "
				+ "order by ide_gtemp ) "
				+ "and ide_astur is not null "
				+ "order by ide_astur asc,ide_gtemp asc";
		
		TablaGenerica tab_empleados_horario_matriz=utilitario.consultar(sql1);
		StringBuilder str_ide_empleado = new StringBuilder();

		if (tab_empleados_horario_matriz.getTotalFilas()>0) {
	String int_num_col_idegetemp="";
			 for (int i = 0; i < tab_empleados_horario_matriz.getTotalFilas(); i++) {
					int_num_col_idegetemp=tab_empleados_horario_matriz.getValor(i, "IDE_GTEMP");
	          	  	if(str_ide_empleado.toString().isEmpty()==false){
	                          str_ide_empleado.append(",");
	                  }
	                  str_ide_empleado.append(int_num_col_idegetemp);
	          }
				
		}else {
			utilitario.agregarMensaje("No se puede realizar esta accion", "Fechas invalidas");
			return;
		}
		
		
		int estado=0;
		
		String sql_turnoMensual="";
			

		sql_turnoMensual="SELECT ide_gtemp, ide_gemes "
		+ "FROM asi_horario_mes_empleado "
		+ "where ide_geani="+anio+" and ide_gemes="+utilitario.getMes(fechaIni)+" and ide_gtemp in( "
		+ "select ide_gtemp   "
		+ "from gth_empleado  "
		+ "where ide_gtemp in(select par.ide_gtemp  "
		+ "from gen_empleados_departamento_par par  "
		+ "where par.activo_geedp=true  "
		+ "group by par.ide_gtemp  "
		+ "UNION all  "
		+ "select par2.ide_gtemp from gen_empleados_departamento_par  par2  "
		+ "where par2.fecha_finctr_geedp between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'  "
		+ "and par2.ide_gtemp not in(select ide_gtemp  from gen_empleados_departamento_par where activo_geedp=true  "
		+ "group by ide_gtemp )  "
		+ "group by par2.ide_gtemp  "
		+ "order by ide_gtemp )  "
		+ "and ide_astur is null  "
		+ "order by ide_astur asc,ide_gtemp asc) "
		+ "group by  ide_gtemp,ide_gemes "
		+ "order by ide_gtemp asc";
		
		String empleados="";
		TablaGenerica tab_empleados_horario_mensual=utilitario.consultar(sql_turnoMensual);
		StringBuilder str_ide_empleado_mensual = new StringBuilder();

		if (tab_empleados_horario_mensual.getTotalFilas()>0) {
	String int_num_col_idegetemp="";
			 for (int i = 0; i < tab_empleados_horario_mensual.getTotalFilas(); i++) {
					int_num_col_idegetemp=tab_empleados_horario_mensual.getValor(i, "IDE_GTEMP");
	          	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
	          	  	str_ide_empleado_mensual.append(",");
	                  }
	          	  str_ide_empleado_mensual.append(int_num_col_idegetemp);
	          }
			estado=1;	
		}else {
			estado=0;
	    	set_empleado.setId("set_empleado");
	    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
					" AS NOMBRES_APELLIDOS "
					+ "FROM GTH_EMPLEADO EMP "
					+ "where ide_gtemp in("+str_ide_empleado+") " +
					" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
			set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
			set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
	    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
			gru_pantalla.getChildren().add(set_empleado);
			set_empleado.getBot_aceptar().setMetodo("getEmpleado");
			agregarComponente(set_empleado);			
			
			
		}
		if (estado==1) {		
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS "
				+ "FROM GTH_EMPLEADO EMP "
				+ "where ide_gtemp in("+str_ide_empleado+","+str_ide_empleado_mensual+") " +
				" ORDER by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP","IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
    	set_empleado.setTitle("Seleccione Empleado(s) a Reprocesar");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("getEmpleado");
		agregarComponente(set_empleado);			
		
		}
	
	

		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

    	
    	
    	set_departamento.setId("set_departamento");

    	String sql = 
    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
    	"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
    	"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
    	"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
    	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD'), "
        + "'' AS HORAINICIOHORARIO, "
    	+ "'' AS HORAINICIOBIOMETRICO, "
    	+ "'' AS HORAINICIOBAND, "		
    	+ "'' AS HORAINICIOALM, "
    	+ "'' AS HORAFINALM, "
    	+ "'' AS HORAINICIOALMBIO, "		
    	+ "'' AS HORAFINALMBIO, "		
    	+ "'' AS TIEMPOALM, "
    	+ "'' AS TIEMPOHORALM, "
    	+ "'' AS HORAFINHORARIO, "
    	+ "'' AS HORAFINBIOMETRICO, "
    	+ "'' AS HORAFINBAND "		
		+ "from gth_empleado emp  " 
		+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP " 
		+ "where emp.ide_gtemp=-1 "  
		+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') "
		+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC ";
    	
    	
    
        
    	
    	tab_resumen_marcaciones.setId("tab_resumen_marcaciones");
        tab_resumen_marcaciones.setTabla("con_biometrico_marcaciones", "ide_cobim", 1);

		tab_resumen_marcaciones.getColumna("ide_cobim").setLongitud(5);
		tab_resumen_marcaciones.getColumna("ide_cobim").setOrden(1);
        tab_resumen_marcaciones.getColumna("ide_cobim").setNombreVisual("CODIGO");

        tab_resumen_marcaciones.getColumna("ide_corel").setLongitud(15);
        tab_resumen_marcaciones.getColumna("ide_corel").alinearCentro();
		tab_resumen_marcaciones.getColumna("ide_corel").setOrden(2);
		tab_resumen_marcaciones.getColumna("ide_corel").setVisible(false);
        
  
	    tab_resumen_marcaciones.getColumna("ide_persona_cobim").setLongitud(15);
	    tab_resumen_marcaciones.getColumna("ide_persona_cobim").alinearCentro();
	    tab_resumen_marcaciones.getColumna("ide_persona_cobim").setOrden(2);
		tab_resumen_marcaciones.getColumna("ide_persona_cobim").setVisible(false);

		
        tab_resumen_marcaciones.getColumna("evento_reloj_cobim").setLongitud(15);
        tab_resumen_marcaciones.getColumna("evento_reloj_cobim").alinearCentro();
		tab_resumen_marcaciones.getColumna("evento_reloj_cobim").setOrden(3);
		tab_resumen_marcaciones.getColumna("evento_reloj_cobim").setLectura(true);
		
		tab_resumen_marcaciones.getColumna("estatus_cobim").setVisible(false);

			
		tab_resumen_marcaciones.getColumna("fecha_evento_cobim").setLongitud(50);
		tab_resumen_marcaciones.getColumna("fecha_evento_cobim").setOrden(4);
        tab_resumen_marcaciones.getColumna("fecha_evento_cobim").setNombreVisual("FECHA");
		tab_resumen_marcaciones.getColumna("fecha_evento_cobim").setLectura(false);

		
		

        tab_resumen_marcaciones.getColumna("activo_cobim").setValorDefecto("true");
        tab_resumen_marcaciones.getColumna("activo_cobim").setLongitud(25);
        tab_resumen_marcaciones.getColumna("activo_cobim").alinearCentro();
        tab_resumen_marcaciones.getColumna("activo_cobim").setNombreVisual("ACTIVO");
        tab_resumen_marcaciones.getColumna("activo_cobim").setLectura(true);

        tab_resumen_marcaciones.getColumna("teletrabajo_cobim").setValorDefecto("false");
		tab_resumen_marcaciones.getColumna("teletrabajo_cobim").setVisible(false);

        
        tab_resumen_marcaciones.getColumna("ide_codigo_validador_cobim").setVisible(false);
        tab_resumen_marcaciones.getColumna("marcacion_validada_cobim").setVisible(false);

       
        
        
        tab_resumen_marcaciones.setCondicion("ide_cobim=-1");  
		tab_resumen_marcaciones.setCampoOrden("fecha_evento_cobim ASC");

        tab_resumen_marcaciones.setHeader("RESUMEN DE MARCACIONES EN BIOMÉTRICO POR EMPLEADO");
        tab_resumen_marcaciones.setLectura(false);
        tab_resumen_marcaciones.setRows(15);
        tab_resumen_marcaciones.dibujar();
    	PanelTabla pat_panel2 = new PanelTabla();
    	pat_panel2.setPanelTabla(tab_resumen_marcaciones);
    	
    	
    	tab_archi_adjunto.setId("tab_archi_adjunto");
    	tab_archi_adjunto.setTabla("con_archivo_registro_manual", "ide_corem", 2);
    	tab_archi_adjunto.getColumna("ide_corem").setLongitud(5);
    	tab_archi_adjunto.getColumna("ide_corem").alinearCentro();
    	tab_archi_adjunto.getColumna("ide_corem").setNombreVisual("COD");
    	tab_archi_adjunto.getColumna("ide_asjei").setLongitud(40);
    	tab_archi_adjunto.getColumna("ide_asjei").setNombreVisual("JEFE INMEDIATO");
    	//tab_archi_adjunto.getColumna("ide_asjei").setFiltro(true);
    	tab_archi_adjunto.getColumna("ide_asjei").setVisible(false);

    	tab_archi_adjunto.getColumna("ide_gemes").setLongitud(20);
    	tab_archi_adjunto.getColumna("ide_gemes").setNombreVisual("MES");
    	//tab_archi_adjunto.getColumna("ide_gemes").setFiltro(true);
    	tab_archi_adjunto.getColumna("ide_gemes").setVisible(false);

    	tab_archi_adjunto.getColumna("ide_geani").setLongitud(25);
    	tab_archi_adjunto.getColumna("ide_geani").alinearCentro();
    	tab_archi_adjunto.getColumna("ide_geani").setNombreVisual("ANIO");
    	tab_archi_adjunto.getColumna("ide_geani").setVisible(false);

    	tab_archi_adjunto.getColumna("ide_gtemp").setLongitud(25);
    	tab_archi_adjunto.getColumna("ide_gtemp").alinearCentro();
    	tab_archi_adjunto.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
 				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
 				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
 				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
 				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
 				"FROM GTH_EMPLEADO EMP");
    	tab_archi_adjunto.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");
    	tab_archi_adjunto.getColumna("ide_gtemp").setLectura(true);

        tab_archi_adjunto.getColumna("fecha_corem").setLongitud(25);
    	tab_archi_adjunto.getColumna("fecha_corem").alinearCentro();
    	tab_archi_adjunto.getColumna("fecha_corem").setNombreVisual("FECHA REGISTRO");
    	tab_archi_adjunto.getColumna("fecha_corem").setValorDefecto(utilitario.getFechaActual());
    	tab_archi_adjunto.getColumna("fecha_corem").setLectura(true);

    	
    	tab_archi_adjunto.getColumna("archivo_corem").setUpload("bitacora");
		//tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setImagen("128", "128");
    	tab_archi_adjunto.getColumna("archivo_corem").setNombreVisual("DETALLE JUSTIFICACION");

        
    	tab_archi_adjunto.getColumna("observaciones_corem").setLongitud(150);
    	tab_archi_adjunto.getColumna("observaciones_corem").alinearCentro();
    	tab_archi_adjunto.getColumna("observaciones_corem").setNombreVisual("OBSERVACION");
    	tab_archi_adjunto.getColumna("observaciones_corem").setRequerida(true);

 
    	tab_archi_adjunto.getColumna("activo_corem").setValorDefecto("true");
    	tab_archi_adjunto.getColumna("activo_corem").setLongitud(25);
    	tab_archi_adjunto.getColumna("activo_corem").alinearCentro();
    	tab_archi_adjunto.getColumna("activo_corem").setNombreVisual("ACTIVO");
    	tab_archi_adjunto.getColumna("activo_corem").setLectura(true);
        
    	tab_archi_adjunto.setHeader("REGISTRO DE ARCHIVO ADJUNTO");     
    	tab_archi_adjunto.setCondicion("ide_corem=-1");
    	//tab_archi_adjunto.ejecutarSql();
    	tab_archi_adjunto.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_archi_adjunto);
        
   Division div_division=new Division();
   div_division.setId("div_division");
   div_division.dividir2(pat_panel2,pat_panel, "80%", "H");

   
   agregarComponente(div_division);

        
    }

   
	public void registroManualBiometrico(){
	
	tab_resumen_marcaciones.setCondicion("IDE_COBIM=-1");
	tab_archi_adjunto.setCondicion("IDE_COREM=-1");
	tab_resumen_marcaciones.ejecutarSql();
	tab_archi_adjunto.ejecutarSql();
    sel_mes_registro_manual.dibujar();
		}
	
    
	   public void getConsultaImportaMarcaciones(){
		   String fec_inicio="",fec_fin="";
		   empleado=sel_empleado_consulta.getValorSeleccionado();
		   TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
		   String fechaIni="",fechaFin="";
		   if (Integer.parseInt(mes)<10) {
			   fechaIni=tab_anio.getValor("detalle_geani")+"-0"+mes+"-01";
			   fechaFin=tab_anio.getValor("detalle_geani")+"-0"+mes+"-"+utilitario.getDia(utilitario.getUltimaFechaMes(fechaIni));
		}else {
			   fechaIni=tab_anio.getValor("detalle_geani")+"-"+mes+"-01";
			   fechaFin=tab_anio.getValor("detalle_geani")+"-"+mes+"-"+utilitario.getDia(utilitario.getUltimaFechaMes(fechaIni));
		}
		   
		 
		 TablaGenerica tabEmpleado = utilitario.consultar("select ide_gtemp,tarjeta_marcacion_gtemp from gth_empleado where ide_gtemp="+sel_empleado_consulta.getValorSeleccionado());
		 	   
		tab_resumen_marcaciones.setCondicion("TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' and  "
				+ "ide_persona_cobim='"+tabEmpleado.getValor("tarjeta_marcacion_gtemp")+"' AND IDE_COREL=18");
		tab_resumen_marcaciones.ejecutarSql();
		tab_resumen_marcaciones.actualizar();
    	if (tab_resumen_marcaciones.getTotalFilas()>0) {
    		String apellido_paterno="",apellido_materno="",primer_nombre="",segundo_nombre="";
    		TablaGenerica tab_persona=utilitario.consultar("SELECT  emp.ide_gtemp,"
    				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
    				+ "FROM GTH_EMPLEADO EMP   "
    				+ "where  EMP.ide_gtemp="+sel_empleado_consulta.getValorSeleccionado());
    				
    		apellido_paterno=tab_persona.getValor("NOMBRES_APELLIDOS");
    	    tab_resumen_marcaciones.setHeader("RESUMEN DE MARCACIONES EN BIOMÉTRICO POR EMPLEADO "+apellido_paterno);

    		
    		
    	}else {
			utilitario.agregarMensajeInfo("No existen datos", "Los parametros seleccionados no contienen datos");
			sel_empleado_consulta.cerrar();		   
			utilitario.addUpdate("tab_resumen_marcaciones,tab_archi_adjunto,sel_empleado_consulta");

			return;
		}

		
		   tab_archi_adjunto.setCondicion("ide_gemes="+mes+ " AND ide_geani="+tab_anio.getValor("ide_geani")+" and ide_gtemp="+empleado);
		   tab_archi_adjunto.ejecutarSql();
		   tab_archi_adjunto.actualizar();
			sel_empleado_consulta.cerrar();		   

		   utilitario.addUpdate("tab_resumen_marcaciones,tab_archi_adjunto,sel_empleado_consulta");
			utilitario.agregarMensajeInfo("Datos generados correctamente", "");


	   }
	
    
   public void getImportacionesPorNovedad (){
	 empleado=sel_empleado_registro_manual.getValorSeleccionado();
     dia_importar.dibujar();
    }
   
    
   			   	    		
   		   			   	       
    

    private double obtenerDiferenciaMinutos(Date fechaInicial, Date fechaFinal){
        double diferencia=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/1000);
 
        int dias=0;
        double horas=0;
        double minutos=0;
        if(diferencia>86400) {
            dias=(int)Math.floor(diferencia/86400);
            diferencia=diferencia-(dias*86400);
        }
        if(diferencia>3600) {
            horas=(int)Math.floor(diferencia/3600);
            diferencia=diferencia-(horas*3600);
        }
        if(diferencia>60) {
            minutos=(int)Math.floor(diferencia/60);
            diferencia=diferencia-(minutos*60);
        }
    	
    	double total_diferencia_segundos = ((horas * 3600) + (minutos * 60) + diferencia);
		double total_diferencia_horas = total_diferencia_segundos / 3600;
		
		double total_diferencia_minutos = total_diferencia_horas * 60;
		
		
		return total_diferencia_minutos;
    }
    
    private Map<Integer, Boolean> obtenerDiasByHorario(TablaGenerica diasXHorario, Integer ide_horario){

    	Map<Integer, Boolean> mapDias = new HashMap<Integer, Boolean>();
    	for (int i=0 ; i<diasXHorario.getTotalFilas();i++) {
			if (ide_horario.equals(Integer.parseInt(diasXHorario.getValor(i, "IDE_ASHOR")))){
				mapDias.put(Integer.parseInt(diasXHorario.getValor(i, "IDE_GEDIA")), Boolean.TRUE);
			}
		}
    	return mapDias;
    }
    
    

    private Date getFechaAsyyyyMMddHHmmss(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    
    
    
    

   

    
    

    
    
    
	public void seleccionarJustificar() {
		
		tab_tabla.getFilaSeleccionada();
	}
 
   

    
    
    
    @Override
    public void insertar() {
    //	TablaGenerica tab_novedadInsertar=utilitario.consultar("select * from asi_");
    	
    	
    	if (tab_resumen_marcaciones.isFocus()) {
    		tab_resumen_marcaciones.insertar();
    	}
    	
    
    	if (tab_archi_adjunto.isFocus()) {
    	if (tab_resumen_marcaciones.getTotalFilas()>0) {
                tab_archi_adjunto.insertar();
                tab_archi_adjunto.setLectura(false);
                tab_archi_adjunto.setValor("ide_asjei",jefe_inmediato_planificacion);
                tab_archi_adjunto.setValor("ide_gtemp",empleado);             
                
     		   TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani ='"+utilitario.getAnio(tab_resumen_marcaciones.getValor("fecha_evento_cobim"))+"'");
     		   TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+utilitario.getMes(tab_resumen_marcaciones.getValor("fecha_evento_cobim"))+")");

                tab_archi_adjunto.setValor("ide_geani",tab_anio.getValor("ide_geani"));
                tab_archi_adjunto.setValor("ide_gemes",tab_mes.getValor("ide_gemes"));   		
		}
    	}
    	
    	
	/*	else {
			utilitario.agregarMensajeInfo("No se puede realizar esta accion","Debe seleccionar un empleado");
			return;
		}		*/
    	


    	
    }

    @Override
    public void guardar() {
    	java.util.Date temp=null;
    	if (tab_resumen_marcaciones.getTotalFilas()>0) { 
    		
        	if (tab_resumen_marcaciones.isFocus()) { 

    	tab_resumen_marcaciones.getValor("FECHA_EVENTO_COBIM");	
		Timestamp ts = null;

			 // biometrico.setFechaEventoCobim(new Timestamp(temp.getTime()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//tab_resumen_marcaciones.getTotalFilas()
		for (int i = 0; i < tab_resumen_marcaciones.getTotalFilas(); i++) {
			if (tab_resumen_marcaciones.getValor(i,"ide_cobim")==null || tab_resumen_marcaciones.getValor(i,"ide_cobim").isEmpty()) {
				try {
					temp =dateFormat.parse(tab_resumen_marcaciones.getValor(i,"FECHA_EVENTO_COBIM"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertarTablaResumen(18, ide_persona_cobim, "1", temp, 1, true);
			}else {
				Timestamp t1=null;
				try {
					temp =dateFormat.parse(tab_resumen_marcaciones.getValor(i,"FECHA_EVENTO_COBIM"));
				 	 t1=	new Timestamp(temp.getTime());

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				utilitario.getConexion().ejecutarSql(""
						+ "UPDATE con_biometrico_marcaciones set "
						+ "fecha_evento_cobim='"+t1.toString().substring(0,19)+"' " 
						+ "where ide_cobim="+tab_resumen_marcaciones.getValor(i,"ide_cobim")+""); 	
				
				utilitario.agregarMensaje("Se guardo correctamente", "");
			}
			
			if (i==(tab_resumen_marcaciones.getTotalFilas()-1)) {
				TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
				   String fechaIni=tab_anio.getValor("detalle_geani")+"-"+mes+"-01";
				   String fechaFin=tab_anio.getValor("detalle_geani")+"-"+mes+"-"+utilitario.getDia(utilitario.getUltimaFechaMes(fechaIni));
				tab_resumen_marcaciones.actualizar();
				tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBIM BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' and  ide_persona_cobim='"+ide_persona_cobim+"' AND IDE_COREL=18");
				tab_resumen_marcaciones.ejecutarSql();
				utilitario.agregarMensaje("Se guardo correctamente", "");
				if (tipo_perfil.equals("1")) {
					tab_archi_adjunto.setCondicion("ide_gemes ="+mes+" and ide_geani="+anio+" AND ide_gtemp in("+empleado_registro_manual+")");
					tab_archi_adjunto.ejecutarSql();
				}else{			
									
					tab_archi_adjunto.setCondicion("ide_gemes ="+mes+" and ide_geani="+anio+" and ide_asjei in("+jefe_inmediato_planificacion+") and ide_gtemp in("+empleado+")");
				
				tab_archi_adjunto.ejecutarSql();
				}
			}
		}
        
        	}
        	if (tab_archi_adjunto.isFocus()) { 

        if (tab_archi_adjunto.guardar()) {
            guardarPantalla();
        }}
    }	else {
		utilitario.agregarMensajeInfo("No se puede realizar esta accion","Debe seleccionar un empleado");
		return;
	}		
    }
    @Override
    public void eliminar() {
    	if (tab_resumen_marcaciones.isFocus()) {
    		tab_resumen_marcaciones.eliminar();	
		}
    	
    	if (tab_archi_adjunto.isFocus()) {
    		tab_archi_adjunto.eliminar();	
		}
    	
    }

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}


	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public SeleccionTabla getSet_departamento() {
		return set_departamento;
	}

	public void setSet_departamento(SeleccionTabla set_departamento) {
		this.set_departamento = set_departamento;
	}
 	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    

 	public void limpiar(){
 		tab_resumen_marcaciones.limpiar();
 		tab_archi_adjunto.limpiar();

 		utilitario.addUpdate("tab_resumen_marcaciones,tab_archi_adjunto,sel_anio_registro_manual");
 	}
 	
 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}




 	
 	
public Tabla getTab_novedad() {
		return tab_novedad;
	}



	public void setTab_novedad(Tabla tab_novedad) {
		this.tab_novedad = tab_novedad;
	}



	//Sumar horas en fecha
	 public Date sumarRestarHorasFecha(Date fecha, int horas){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(fecha); // Configuramos la fecha que se recibe
     calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
		  }
	 
	 
	//Sumar horas en fecha
		 public Date sumarRestarMinutosFecha(Date fecha, int horas){
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(fecha); // Configuramos la fecha que se recibe
	     calendar.add(Calendar.MINUTE, horas);  // numero de horas a añadir, o restar en caso de horas<0
	     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
			  }
 	

	 public void insertarTablaResumen(
			 int ide_corel ,
			 String ide_persona_cobim,
			 String evento_reloj_cobim,
			 java.util.Date fecha_evento_cobim,
			 int estatus_cobim,
			 boolean activo_cobim
		
			 ){
		 	Timestamp t1=	new Timestamp(fecha_evento_cobim.getTime());
		 	
		 	
			TablaGenerica tabUsuario=utilitario.consultar("select ide_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
			

		  		
		  		
			TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("con_biometrico_marcaciones", "ide_cobim"));
			String codigo=tab_codigo.getValor("codigo");
			utilitario.getConexion().ejecutarSql("INSERT INTO con_biometrico_marcaciones(" 
						+ "ide_cobim, "
						+ "ide_corel, "
				  		+ "ide_persona_cobim, "
				  		+ "evento_reloj_cobim, "
				  		+ "fecha_evento_cobim, "
				  		+ "estatus_cobim, "
				  		+ "activo_cobim, "
				  		+ "usuario_ingre, "
				  		+ "fecha_ingre, "
				  		+ "hora_ingre)" + 



				  		" values( " +codigo + ", "
				  		+ ""+ ide_corel+", "
				  		+ "'"+ide_persona_cobim+"', "
				  		+ "'"+evento_reloj_cobim+"', "
				  		+ "'"+t1.toString().substring(0,19)+"', "
				  		+ ""+estatus_cobim+", "
				  		+ ""+activo_cobim+", "
						+ "'"+tabUsuario.getValor("nick_usua")+"', "
						+ "'"+utilitario.getFechaActual()+"', "
				  		+ "'"+utilitario.getHoraActual()+"')");

		 
	 }
	
	 	 
	 

	 public void insertarTablaPlantilla(
			 int ide_gtemp ,
			 String fecha_evento_cobmr,
			 String horainiciohorario_cobmr,
			String horainiciobiometrico_cobmr,
			 String horainicioband_cobmr,
			 String horainicioalm_cobmr,
			 String horafinalm_cobmr,
			 String horainicioalmbio_cobmr,
			 String horafinalmbio_cobmr,
			 double tiempoalm_cobmr,
			 String tiempohoralm_cobmr,
			 String horafinhorario_cobmr,
			 String horafinbiometrico_cobmr,
			 String horafinband_cobmr,
			 String dia_cobmr,
		  	 int tipo_horario_cobmr

			 ){
	
			TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("con_biometrico_marcaciones_resumen", "ide_cobmr"));
			String codigo=tab_codigo.getValor("codigo");
			utilitario.getConexion().ejecutarSql("INSERT INTO con_biometrico_marcaciones_resumen(" 
						+ "ide_cobmr, "
						+ "ide_gtemp, "
				  		+ "fecha_evento_cobmr, "
				  		+ "horainiciohorario_cobmr, "
				  		+ "horainiciobiometrico_cobmr, "
				  		+ "horainicioband_cobmr, "
				  		+ "horainicioalm_cobmr, "
				  		+ "horafinalm_cobmr, "
				  		+ "horainicioalmbio_cobmr, "
				  		+ "horafinalmbio_cobmr, "
				  		+ "tiempoalm_cobmr, "
				  		+ "tiempohoralm_cobmr, "
				  		+ "horafinhorario_cobmr, "
				  		+ "horafinbiometrico_cobmr, "
				  		+ "horafinband_cobmr,"
				  		+ "dia_cobmr,"
				  		+ "tipo_horario_cobmr)" + 

				  		" values( " +codigo + ", "
				  		+ ""+ ide_gtemp+", "
				  		+ "'"+fecha_evento_cobmr+"', "
				  		+ "'"+horainiciohorario_cobmr+"', "
				  		+ "'"+horainiciobiometrico_cobmr+"', "
				  		+ "'"+horainicioband_cobmr+"', "
				  		+ "'"+horainicioalm_cobmr+"', "
				  		+ "'"+horafinalm_cobmr+"', "
				  		+ "'"+horainicioalmbio_cobmr+"', "
				  		+ "'"+horafinalmbio_cobmr+"', "
				  		+ " '"+tiempoalm_cobmr+"', "
				  		+ "'"+tiempohoralm_cobmr+"', "
				  		+ "'"+horafinhorario_cobmr+"', "
				  		+ "'"+horafinbiometrico_cobmr+"', "
				  		+ "'"+horafinband_cobmr+"', "
				  		+ "'"+dia_cobmr+"', "
				  		+ ""+tipo_horario_cobmr+") ");
		 
	 }
	
	 
	 
	 
	 public TablaGenerica getTablaReporte(String IDE_GTEMP, String strFechaIniReporte, String strFechaFinReporte ){
	
		 TablaGenerica tab_reporte;
		 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
		        tab_reporte= utilitario.consultar(
		    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
		     	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') , "		
		    	+ "'' AS HORAINICIOHORARIO, "
		    	+ "'' AS HORAINICIOBIOMETRICO, "
		    	+ "'' AS HORAINICIOBAND, "		
		    	+ "'' AS HORAINICIOALM, "
		    	+ "'' AS HORAFINALM, "
		    	+ "'' AS HORAINICIOALMBIO, "		
		    	+ "'' AS HORAFINALMBIO, "		
		    	+ "'' AS TIEMPOALM, "
		    	+ "'' AS TIEMPOHORALM, "
		    	+ "'' AS HORAFINHORARIO, "
		    	+ "'' AS HORAFINBIOMETRICO, "
		    	+ "'' AS HORAFINBAND, "
		    	+ "'' AS biometrico_entrada_cobmr, "
		    	+ "'' AS biometrico_alm_entrada,  "
		    	+ "'' AS biometrico_alm_salida, "
		    	+ "'' AS biometrico_salida_cobmr "	
				+ "from gth_empleado emp "
				+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
				+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'   "
				+ "AND IDE_COREL NOT IN(11,12,14,16,13)  "
				+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD')  "
				+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC");			
				
	}else{
	
		
		
		tab_reporte= utilitario.consultar(
		    	"select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "  
		     	+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') , "		
		    	+ "'' AS HORAINICIOHORARIO, "
		    	+ "'' AS HORAINICIOBIOMETRICO, "
		    	+ "'' AS HORAINICIOBAND, "		
		    	+ "'' AS HORAINICIOALM, "
		    	+ "'' AS HORAFINALM, "
		    	+ "'' AS HORAINICIOALMBIO, "		
		    	+ "'' AS HORAFINALMBIO, "		
		    	+ "'' AS TIEMPOALM, "
		    	+ "'' AS TIEMPOHORALM, "
		    	+ "'' AS HORAFINHORARIO, "
		    	+ "'' AS HORAFINBIOMETRICO, "
		    	+ "'' AS HORAFINBAND, "
		    	+ "'' AS biometrico_entrada_cobmr, "
		    	+ "'' AS biometrico_alm_entrada,  "
		    	+ "'' AS biometrico_alm_salida, "
		    	+ "'' AS biometrico_salida_cobmr "	
				+ "from  gth_empleado emp  "
				+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
				+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				//+ "and emp.ide_gtemp IN(1093) "
				+ "and emp.ide_gtemp IN("+IDE_GTEMP+") "
				+ "AND IDE_COREL NOT IN(11,12,14,16,13)  "
				+ "GROUP BY  EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD')  "
				+ "ORDER BY EMP.IDE_GTEMP,TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') ASC");			
				
	}
			//	tab_reporte.imprimirSql();
		 return tab_reporte;
		 
	 }
	 
	 
	 public void insertarTablaNovedad(
			  int ide_usua, 
			  String fecha_inicio_asnov,
			  String fecha_fin_asnov,
			  String observacion_asnov ,
			  boolean activo_asnov, 
			  String fecha_asnov,
			  boolean importacion_asnov,
			  boolean dia_feriado_asnov){
			 TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_novedad", "ide_asnov"));
			String codigo=tab_codigo.getValor("codigo");
			utilitario.getConexion().ejecutarSql("INSERT INTO asi_novedad(" 
						+ "ide_asnov, "
						+ "ide_usua, "
				  		+ "fecha_inicio_asnov, "
				  		+ "fecha_fin_asnov, "
				  		+ "observacion_asnov, "
				  		+ "activo_asnov, "
				  		+ "fecha_asnov, "
				  		+ "importacion_asnov,"
				  		+ "dia_feriado_asnov)" + 
				  		" values( " +codigo + ", "
				  		+ ""+ ide_usua+", "
				  		+ "'"+fecha_inicio_asnov+"', "
				  		+ "'"+fecha_fin_asnov+"', "
				  		+ "'"+observacion_asnov+"', "
				  		+ "'"+activo_asnov+"', "
				  		+ "'"+fecha_asnov+"', "
				  		+ ""+importacion_asnov+" , "
				  		+ ""+dia_feriado_asnov+" )"); 
 
	 }




	



	public Tabla getTab_resumen_marcaciones() {
		return tab_resumen_marcaciones;
	}


	public void setTab_resumen_marcaciones(Tabla tab_resumen_marcaciones) {
		this.tab_resumen_marcaciones = tab_resumen_marcaciones;
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
	 
	 
	   
	    
	    private TablaGenerica obtenerTurnosMatriz(int ide_astur){
	    	
	    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario where ide_astur in("+ide_astur+")");
	    	return obtenerHorarios;
	    	
	    }


		public boolean isEstado_empleado_nocturno() {
			return estado_empleado_nocturno;
		}


		public void setEstado_empleado_nocturno(boolean estado_empleado_nocturno) {
			this.estado_empleado_nocturno = estado_empleado_nocturno;
		}
	    
	    
	    //
		//retorna si el dia tiene turno 
		public int getHorarioXDia(int ide_empleado,int dia,int mes ,int anio){
	    
		TablaGenerica tabEmpleadoMatrizMensual = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+ide_empleado+"  "
				+ "and ide_gemes="+mes+" and ide_geani="+anio);
         int contieneDiaTurno=0;
		
        //Si contiene Matriz
         if (tabEmpleadoMatrizMensual.getValor("DIA"+dia)==null || tabEmpleadoMatrizMensual.getValor("DIA"+dia).isEmpty() || tabEmpleadoMatrizMensual.getValor("DIA"+dia).equals("")) {
        	 contieneDiaTurno=0;
        	 }else {
        		 //contiene el ide del turno
       	 contieneDiaTurno=Integer.parseInt(tabEmpleadoMatrizMensual.getValor("DIA"+dia));

		}
         
         return contieneDiaTurno;
	 
		}

		





//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado


public TablaGenerica getTablaReporteEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){
	
	 TablaGenerica tab_reporte;
	
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
	   tab_reporte= utilitario.consultar(
	    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
			+ "from gth_empleado emp  "
			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
			//   + "and EMP.ide_gtemp IN(SELECT IDE_GTEMP GTH_EMPLEADO WHERE IDE_ASTUR=1) "
			  // + "and EMP.ide_gtemp IN(select ide_gtemp from asi_horario_mes_empleado where ide_gtemp  in( "
			  // + "select ide_gtemp from gen_empleados_departamento_par where ide_gttem=1 and activo_geedp=true "
			  // + "order by ide_gtemp) "
			  // + "and ide_gemes=9 and ide_geani=11 order by ide_gtemp asc) "
			+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
			+ "ORDER BY EMP.IDE_astur,EMP.IDE_GTEMP");		
	 }else {
		 tab_reporte= utilitario.consultar(
			    	"select EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "		
					+ "from gth_empleado emp  "
					+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP  "
					+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+strFechaIniReporte+"' AND '"+strFechaFinReporte+"'    "
				   + "and EMP.ide_gtemp IN("+IDE_GTEMP+") "
				   //+ "and EMP.ide_gtemp IN(101,103,104,107,108) "
					+ "GROUP BY  EMP.IDE_GTEMP,EMP.TARJETA_MARCACION_GTEMP "
					+ "ORDER BY EMP.IDE_astur,EMP.IDE_GTEMP");		
	}
	   
		//tab_reporte.imprimirSql();
		 return tab_reporte;	   
			}




public Integer getIde_geedp() {
	return ide_geedp;
}


public void setIde_geedp(Integer ide_geedp) {
	this.ide_geedp = ide_geedp;
}


public String getHoraAnterior() {
	return horaAnterior;
}


public void setHoraAnterior(String horaAnterior) {
	this.horaAnterior = horaAnterior;
}


public String getHoraAnteriorAlm() {
	return horaAnteriorAlm;
}


public void setHoraAnteriorAlm(String horaAnteriorAlm) {
	this.horaAnteriorAlm = horaAnteriorAlm;
}


public String getPos_marcacionEntrada() {
	return pos_marcacionEntrada;
}


public void setPos_marcacionEntrada(String pos_marcacionEntrada) {
	this.pos_marcacionEntrada = pos_marcacionEntrada;
}


public String getPos_marcacionTarde() {
	return pos_marcacionTarde;
}


public void setPos_marcacionTarde(String pos_marcacionTarde) {
	this.pos_marcacionTarde = pos_marcacionTarde;
}


public String getPos_marcacionNocturno() {
	return pos_marcacionNocturno;
}


public void setPos_marcacionNocturno(String pos_marcacionNocturno) {
	this.pos_marcacionNocturno = pos_marcacionNocturno;
}


public String getHoraActualizarPegado() {
	return horaActualizarPegado;
}


public void setHoraActualizarPegado(String horaActualizarPegado) {
	this.horaActualizarPegado = horaActualizarPegado;
}






private TablaGenerica obtenerTimbreBiometricoExtra(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
				+ "EMP.IDE_GTEMP,"
    			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
    			+ "from gth_empleado emp  "
    			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
    			+ "where TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
    			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
    			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");    	
	//tabObteberTimbreXEmpleado.imprimirSql();
	return tabObteberTimbreXEmpleado;
	
}

public String retornaIdeAnio(String anio){
	String anioActual="";
	TablaGenerica  tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%' ");
	return anioActual=tabAnio.getValor("ide_geani");
	
}


public  String  retornaMes(int mes){
	String mes_cobmr1="";
	if (mes<10) {
			mes_cobmr1= "0"+mes;
	}else {
			mes_cobmr1=""+mes;

	}
return mes_cobmr1;
	
}


public  String  retornaDia(int dia){
	String dia_cobmr1="";
	if (dia<10) {
		dia_cobmr1= "0"+dia;
}else {
		dia_cobmr1= ""+dia;

}
	return dia_cobmr1;
}



public int fechaFinSemana(Date fch) {
	
	Calendar fechaInicial = Calendar.getInstance();
	fechaInicial.setTime(fch);
	int contador  = 0;
		if (fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ||  fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			contador= fechaInicial.get(Calendar.DATE);
		}
		return contador;
}





//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado
public TablaGenerica getTablaResumenMarcacionesEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){

	 TablaGenerica tab_reporte=null;
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
	   tab_reporte= utilitario.consultar("SELECT COUNT(ide_gtemp), ide_gtemp  "
	   		+ "FROM con_biometrico_marcaciones_resumen "
	   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
	   		//+ "and ide_gtemp in (147) "
	   		//+ "and ide_gtemp in (select ide_gtemp from gth_empleado emp where ide_astur=1) "
	   		// + "and ide_gtemp in (SELECT IDE_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
	   		 //+ "SELECT  ide_gtemp  "
	   		 //+ "FROM con_biometrico_marcaciones_resumen  "
	   		 //+ "where  fecha_evento_cobmr between '2018-09-01' and '2018-09-30'  "
	   		// + "group by ide_gtemp  "
	   		// + "order by ide_gtemp asc)) "
	   		+ "group by ide_gtemp  "
	   		+ "order by ide_gtemp asc");
	 }else {
		   tab_reporte= utilitario.consultar("SELECT COUNT(ide_gtemp), ide_gtemp  "
			   		+ "FROM con_biometrico_marcaciones_resumen "
			   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' and ide_gtemp in("+IDE_GTEMP+") "
			   		//+ "and ide_gtemp in (147) "
			   		//+ "and ide_gtemp in (select ide_gtemp from gth_empleado emp where ide_astur=1) "
			   		// + "and ide_gtemp in (SELECT IDE_GTEMP FROM GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE AND IDE_GTEMP NOT IN( "
			   		 //+ "SELECT  ide_gtemp  "
			   		 //+ "FROM con_biometrico_marcaciones_resumen  "
			   		 //+ "where  fecha_evento_cobmr between '2018-09-01' and '2018-09-30'  "
			   		// + "group by ide_gtemp  "
			   		// + "order by ide_gtemp asc)) "
			   		+ "group by ide_gtemp  "
			   		+ "order by ide_gtemp asc");
	 }
		 return tab_reporte;	   
			}



//Tenemos solo los ide_geedp de cada empleado activo para saber que se cambia de empleado
public TablaGenerica getTablaMarcacionesEmpleado(String strFechaIniReporte,String strFechaFinReporte,String IDE_GTEMP){
	
	 TablaGenerica tab_reporte;
	 
	   tab_reporte= utilitario.consultar("SELECT ide_cobmr, fecha_evento_cobmr "
	   		+ "FROM con_biometrico_marcaciones_resumen  "
	   		+ "where  fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   "
	   		+ "and ide_gtemp in("+IDE_GTEMP+") "
	   		//+ "and ide_gtemp in(SELECT ide_gtemp "
	   		//+ "FROM asi_horario_mes_empleado WHERE IDE_GEMES=9 AND IDE_GEANI=11 order by ide_gtemp asc) " 
  			+ "ORDER by ide_gtemp asc,fecha_evento_cobmr asc");

		 return tab_reporte;	   
}










public void getMarcacionesAlmuerzo(String fechaIni,String fechaFin, String IDE_GTEMP){
//Variablesf
	String horainicioalm_cobmr="",horainicioalmbio_cobmr="",horafinalmbio_cobmr="";

	TablaGenerica tab_almuerzo=null;
	 if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null || IDE_GTEMP.equals("")) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='', horafinalmbio_cobmr='' "
					 + "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and (horainicioalmbio_cobmr like '%SIN TIMBRE%' or horafinalmbio_cobmr='%SIN TIMBRE%') ");
					   	
			 
			tab_almuerzo=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
				+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
				+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr "
				+ "FROM con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
						//+ "and ide_gtemp=13 "
				+ "order by ide_gtemp asc ,fecha_evento_cobmr  asc");
	 }else {
		 
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='', horafinalmbio_cobmr='' "
					 + "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and (horainicioalmbio_cobmr like '%SIN TIMBRE%' or horafinalmbio_cobmr='%SIN TIMBRE%')  "
					 		+ "AND IDE_GTEMP IN("+IDE_GTEMP+") ");
					   	
			 
		tab_almuerzo=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr, "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr, "
				+ "horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
				+ "ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr "
				+ "FROM con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
				+ "and ide_gtemp in("+IDE_GTEMP+") "
				+ "order by ide_gtemp asc ,fecha_evento_cobmr  asc");
	 }

	
if (tab_almuerzo.getTotalFilas()==0) {
	
}else{

for (int i = 0; i < tab_almuerzo.getTotalFilas(); i++) {
	horainicioalm_cobmr=tab_almuerzo.getValor(i,"horainicioalm_cobmr");
	horainicioalmbio_cobmr=tab_almuerzo.getValor(i,"horainicioalmbio_cobmr");
	horafinalmbio_cobmr=tab_almuerzo.getValor(i,"horafinalmbio_cobmr");
	
	if (horainicioalm_cobmr.equals("S/A") || horainicioalm_cobmr.equals("") || horainicioalm_cobmr.isEmpty() || horainicioalm_cobmr==null) {
		//Si no existe marcacion en salida al almuerzo
	}else {
		if (horainicioalmbio_cobmr==null || horainicioalmbio_cobmr.equals("") || horainicioalmbio_cobmr.isEmpty() ) {
			if (horafinalmbio_cobmr==null || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.isEmpty() ) {
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='SIN TIMBRE', horafinalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));

		}else {
			
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));
			
		}
	}else {
		if (horafinalmbio_cobmr==null || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.isEmpty() ) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  horafinalmbio_cobmr='SIN TIMBRE' where ide_cobmr=" + tab_almuerzo.getValor(i,"ide_cobmr"));
	
	}
	}

	}
}

}



}


public void getMarcacionesFeriado(String fecInicia, String fecFinal){
	TablaGenerica tabDiasFeriados=utilitario.consultar("SELECT ide_asnov, ide_usua, fecha_inicio_asnov, fecha_fin_asnov, observacion_asnov,  "
			+ "activo_asnov, fecha_asnov, importacion_asnov, dia_feriado_asnov, "
			+ "cambio_asnov, ide_astur "
			+ "FROM asi_novedad where dia_feriado_asnov=true "
			+ "order by ide_asnov asc");

for (int i = 0; i < tabDiasFeriados.getTotalFilas(); i++) {
	
}


}


public String formatearHora(String hora){
	String horaNueva="",horaTemp="",minutoTemp="",segundosTemp="";
	String[] parts = hora.split(":");
	String part1 = parts[0]; // 123
	String part2 = parts[1]; // -654321
	String part3 = parts[2]; // -654321
	
	if (Integer.parseInt(part1)<10) {
			horaTemp="0"+part1;
    		}else {
			horaTemp=""+part1;
    		}
		
	if (Integer.parseInt(part2)<10) {
		minutoTemp="0"+part2;
		}else {
		minutoTemp=""+part1;
		}

	
	if (Integer.parseInt(part3)<10) {
		segundosTemp="0"+part3;
		}else {
		segundosTemp=""+part3;
		}

			horaNueva=horaTemp+":"+minutoTemp+":"+segundosTemp;
	
	return horaNueva;
}





	public void empleadoSinMarcacion(){
		
		try {
			TablaGenerica tabActivoSinMarcacion=utilitario.consultar("select ide_gtemp,activo_gtemp,ide_astur from gth_empleado where activo_gtemp = true  "
					+ "and ide_gtemp not in (SELECT ide_gtemp  "
					+ "biometrico_salida_cobmr   "
					+ "FROM con_biometrico_marcaciones_resumen  "
					+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"')");
			

			TablaGenerica tablaMarcaciones=utilitario.consultar("SELECT ide_gtemp,ide_gtemp as uno  "
					+ "FROM con_biometrico_marcaciones_resumen  "
					+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
			
			if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
				if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
				}else {
					//Si las fechas o son validas
				utilitario.agregarMensajeInfo("Fechas inválidas ","No se puede procesar transacción" );
			   return;
				}	
			
		}else {
			//Si las fechas de entrada y salida aun no han sido seleciionadp
			utilitario.agregarMensajeInfo("Revisar Fecha Inicio y Fin ","Debe seleccionar una fecha válida" );
			return;
		}
			
			
			if ((tablaMarcaciones.getTotalFilas()==0) || tablaMarcaciones.isEmpty()) {
				utilitario.agregarMensajeInfo("	No existe importación de marcaciones ","Debe importar marcaciones" );	
				return;
			}
			
			
			
			if (tabActivoSinMarcacion.getTotalFilas()>0) {
			//Si existen empleados sin marcaciones 	
		
				
				TablaGenerica tablaMarcacionesTope=utilitario.consultar("SELECT ide_gtemp,"
						+ "fecha_evento_cobmr  "
						+ "FROM con_biometrico_marcaciones_resumen  "
						+ "where  fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
						+ "order by fecha_evento_cobmr desc "
						+ "limit 1");
			
				
				
				
			    		String fechaIni=(cal_fecha_inicial.getFecha());	
			    		String fechaFin=(tablaMarcacionesTope.getValor("fecha_evento_cobmr"));
						int diferencia_dias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fechaIni),utilitario.DeStringADate(fechaFin));
				
						Date DiaSuma=null;
						int  ide_gemes=0,anio=0;				
					    String  fecha_evento_cobmr="";
			  			String dia_cobmr="";
			  			int ide_geani=0;
			  			String ide_astur="";
			  			String empleado="";
					 
				    	
				    	  			
			
			  	for (int i = 0; i < tabActivoSinMarcacion.getTotalFilas(); i++) {
			  		ide_astur=tabActivoSinMarcacion.getValor(i,"IDE_ASTUR");
			  		empleado=tabActivoSinMarcacion.getValor(i,"IDE_GTEMP");		  		
					for (int j = 0; j < (diferencia_dias+1); j++) {
						
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  	  				
						DiaSuma=utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaIni), j);
						fecha_evento_cobmr= utilitario.DeDateAString(DiaSuma);
						dia_cobmr=diaSemana(fecha_evento_cobmr);
						ide_gemes=utilitario.getMes(fecha_evento_cobmr) ;
						
						
						
						
						if (ide_astur==null ||  ide_astur.equals("") || ide_astur.isEmpty()) {
						//Si contiene turno diario	
			  			TablaGenerica  tabAnioReporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_evento_cobmr)+"%' ");
					 	ide_geani=Integer.parseInt(tabAnioReporte.getValor("ide_geani"));		
						TablaGenerica tabEmpleadoMatrizMensual1 = utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+empleado+"  "
								+ "and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani+" and dia"+(i+1)+" is not null ");

						
						if (tabEmpleadoMatrizMensual1.getTotalFilas()>0) {
							//Si contiene asignado un horario mensual 
				    		TablaGenerica  tabAnioReporteMensual=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
									+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
									+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
									+ "FROM asi_turnos_horario astuh  "
									+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
									+ "where astuh.ide_astur="+tabEmpleadoMatrizMensual1.getValor("dia"+(i+1))+" "
									+ "order by ide_astuh asc");	
				    		
				    		
				    	if (tabAnioReporteMensual.getTotalFilas()>0) {
				    		//Contiene turno para el dia 
				    		//insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporteMensual.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporteMensual.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporteMensual.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporteMensual.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","","");
							
						}else {
							//No contiene turno asignado en ese dia
							//insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");	
							}	
						}else{
						//Si no contiene turno asignado a ese dia 
							//insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");	
						}
						
						
						}else {
						//Si contiene turno anual
							
						    //Hora desde Inicio de Entrada de Horario
	             			     Calendar calHoraFechaBase= Calendar.getInstance();
	           	    		     calHoraFechaBase.setTime(DiaSuma);
	           	    		     
	           	    		     
					//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");    
					//		Calendar cini = Calendar.getInstance();
					//               cini.setTime(formatter.parse(utilitario.DeDateAString(DiaSuma)));
					      if (calHoraFechaBase.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
					    		TablaGenerica  tabAnioReporte=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
										+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
										+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
										+ "FROM asi_turnos_horario astuh  "
										+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
										+ "where astuh.ide_astur=1 and horario.ide_ashor=2 "
										+ "order by ide_astuh asc");
								
					    	//  insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporte.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporte.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporte.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporte.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","","");  										
						
					      }else if(calHoraFechaBase.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
							//	insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, "", "", "SIN TIMBRE", "", "", "","", 0, "", "", "", "SIN TIMBRE", false, dia_cobmr,"","","","");				    	  
					      }else			      
					      {
					    		TablaGenerica  tabAnioReporte=utilitario.consultar("SELECT astuh.ide_astuh, astuh.ide_astur, astuh.ide_ashor, "
										+ "horario.hora_inicial_ashor, horario.hora_final_ashor, horario.hora_inicio_almuerzo_ashor,  "
										+ "horario.hora_fin_almuerzo_ashor, horario.min_almuerzo_ashor, horario.nom_ashor  "
										+ "FROM asi_turnos_horario astuh  "
										+ "LEFT JOIN asi_horario horario on horario.ide_ashor=astuh.ide_ashor  "
										+ "where astuh.ide_astur=1 and horario.ide_ashor=3 "
										+ "order by ide_astuh asc");
							
							//insertarTablaResumen(Integer.parseInt(empleado), fecha_evento_cobmr, tabAnioReporte.getValor("hora_inicial_ashor"), "", "SIN TIMBRE", tabAnioReporte.getValor("hora_inicio_almuerzo_ashor"), tabAnioReporte.getValor("hora_fin_almuerzo_ashor"),"","", 0, "",  tabAnioReporte.getValor("hora_final_ashor"), "", "SIN TIMBRE", false, dia_cobmr,"","","",""); 	
						}				      
					}//fin if si contiene turno anual
					
				}//Primer for
					
			  	}//Segundo for	
				
			
			
			
			}else {
			//Si no  empleados sin marcaciones 	
				utilitario.agregarMensajeInfo("Ya han sido procesadas todas las marcaciones","No existen empleado(s) para importar en las fechas seleccionadas");
				return;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR NUMERO");		
		} }		
		













public Tabla getTab_archi_adjunto() {
	return tab_archi_adjunto;
}


public void setTab_archi_adjunto(Tabla tab_archi_adjunto) {
	this.tab_archi_adjunto = tab_archi_adjunto;
}


public void obtenerMesRegistroManual(){
	  mes=sel_mes_registro_manual.getValorSeleccionado();
	  if ((mes==null || mes.isEmpty() || mes.equals("") )) {
			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
			return;
		}else {
			
		  mes=sel_mes_registro_manual.getValorSeleccionado();
System.out.println("obtenerMes: "+mes);
			
	  sel_mes_registro_manual.cerrar();	
sel_anio_registro_manual.dibujar();
		}

}
	

public void obtenerAnioRegistroManual(){
    anio=sel_anio_registro_manual.getValorSeleccionado();
    if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
			return;
		}else {
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")");
		int anioEmpleado=Integer.parseInt(sel_anio_registro_manual.getValorSeleccionado());   
	   	sel_anio_registro_manual.cerrar();

		  	
	   	
		String sql="";
		sql="SELECT EMP.IDE_GTEMP,  "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS   "
				+ " FROM asi_empleado_jefe_inmediato ashor "
				+ "left join gth_empleado emp on emp.ide_gtemp=ashor.ide_gtemp ";

			if (tipo_perfil.equals("1")) {
				sql+= "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 

			}else {
				sql+= "where ide_asjei="+jefe_inmediato_planificacion+" "
					+ "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 

			}
		
	sel_empleado_consulta.setId("sel_empleado_consulta");
	sel_empleado_consulta.setSeleccionTabla(sql, "IDE_GTEMP");
	sel_empleado_consulta.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
	sel_empleado_consulta.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
	sel_empleado_consulta.setRadio();
	sel_empleado_consulta.setHeight("60");
	sel_empleado_consulta.setWidth("40");
	sel_empleado_consulta.setTitle("Seleccione Empleado");
	sel_empleado_consulta.getBot_aceptar().setMetodo("getConsultaImportaMarcaciones");
	sel_empleado_consulta.dibujar();
	sel_empleado_consulta.redibujar();
	utilitario.addUpdate("sel_empleado_consulta");       
		
		}	
		}


public SeleccionTabla getSel_mes_registro_manual() {
	return sel_mes_registro_manual;
}


public void setSel_mes_registro_manual(SeleccionTabla sel_mes_registro_manual) {
	this.sel_mes_registro_manual = sel_mes_registro_manual;
}


public SeleccionTabla getSel_anio_registro_manual() {
	return sel_anio_registro_manual;
}


public void setSel_anio_registro_manual(SeleccionTabla sel_anio_registro_manual) {
	this.sel_anio_registro_manual = sel_anio_registro_manual;
}


public SeleccionTabla getSel_empleado_registro_manual() {
	return sel_empleado_registro_manual;
}


public void setSel_empleado_registro_manual(
		SeleccionTabla sel_empleado_registro_manual) {
	this.sel_empleado_registro_manual = sel_empleado_registro_manual;
}



public void abrirDialogoImportar() {
	tab_resumen_marcaciones.setCondicion("ide_cobim=-1");
	tab_resumen_marcaciones.ejecutarSql();

	tab_archi_adjunto.setCondicion("ide_corem=-1");
	tab_archi_adjunto.ejecutarSql();
	utilitario.addUpdate("tab_resumen_marcaciones,tab_archi_adjunto");
	
	
	
	
	String sql="";
	sql="SELECT EMP.IDE_GTEMP,  "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS   "
			//+ "mes.detalle_gemes,anio.detalle_geani  "
			//+ " FROM con_biometrico_plan_hxe ashor "
			+ " FROM asi_empleado_jefe_inmediato ashor "
			
			+ "left join gth_empleado emp on emp.ide_gtemp=ashor.ide_gtemp ";
			//+ "left join gen_anio anio on anio.ide_geani=ashor.ide_geani "
			//+ "left join gen_mes mes on mes.ide_gemes=ashor.ide_gemes  ";
			//+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+sel_anio_registro_manual.getValorSeleccionado()+"  "
		if (tipo_perfil.equals("1")) {
			sql+= "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 

		}else {
/*				sql+= "where mes.ide_gemes="+mes+" and anio.ide_geani="+sel_anio_registro_manual.getValorSeleccionado()+" and ide_asjei="+jefe_inmediato_planificacion+" "
					+ "and registro_manual_cobph=true" */

			sql+= "where ide_asjei="+jefe_inmediato_planificacion+" "
				+ "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 

		}
	

			
		


	sel_empleado_registro_manual.setId("sel_empleado_registro_manual");
	sel_empleado_registro_manual.setSeleccionTabla(sql, "IDE_GTEMP");
	sel_empleado_registro_manual.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
	sel_empleado_registro_manual.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
	sel_empleado_registro_manual.setRadio();
	sel_empleado_registro_manual.setHeight("60");
	sel_empleado_registro_manual.setWidth("40");
	sel_empleado_registro_manual.setTitle("Seleccione Empleado");
	sel_empleado_registro_manual.getBot_aceptar().setMetodo("getImportacionesPorNovedad");
	sel_empleado_registro_manual.dibujar();
	sel_empleado_registro_manual.redibujar();
	utilitario.addUpdate("sel_empleado_registro_manual");       
	
	
	
sel_empleado_registro_manual.dibujar();
	
	
		//dia_importar.dibujar();
		//valorTotal=false;
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




/**
 * Valida el archivo para que pueda importar un rubro a la nomina
 * 
 * @param evt
 */

public void validarArchivo(FileUploadEvent evt) {
//	if (aut_rubros.getValor() != null) {
		// Leer el archivo
		String str_msg_info = "";
		String str_msg_adve = "";
		String str_msg_erro = "";
		double dou_tot_valor_imp = 0;
		boolean bandTabVacia=false;
		int contErrores=0; 

		try {
			// Valido que el rubro seleccionado este configurado en los tipo
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

		  tab_emp.setSql("SELECT EMP.ide_gtemp, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES,  "
				  		+ "cobim.fecha_evento_cobim, cobim.ide_persona_cobim   "
				  		+ "FROM con_biometrico_marcaciones  cobim  "
				  		+ "LEFT JOIN GTH_EMPLEADO EMP on emp.tarjeta_marcacion_gtemp=trim(cobim.ide_persona_cobim) "
				  		+ "where cobim.ide_cobim=-1");
			  

			tab_emp.ejecutarSql();
			tab_emp.setLectura(false);
			tab_emp.setDibujo(false);
			int ide_gtemp=0;
			String fecha_evento_cobim="",hora_evento_cobim="",ide_corel="";
			ide_corel="17";
			tarjeta_marcacion_gtemp="";
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
				for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				//String str_fecha = hoja.getCell(1, i).getContents();
				//String str_hora = hoja.getCell(2, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				
				//TablaGenerica tab_empleado=utilitario.consultar("select * from gth_empleado where ide_gtemp="+empleado);
				
				
				TablaGenerica tab_empleado =utilitario.consultar("select  emp.ide_gtemp,emp.documento_identidad_gtemp,emp.tarjeta_marcacion_gtemp, EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  GTH_EMPLEADO EMP  "
  						+ " left join asi_empleado_jefe_inmediato ashem on emp.ide_gtemp=ashem.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+empleado+")  and ashem.activo_emjei=true "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
				
				
				String str_cedula = tab_empleado.getValor("DOCUMENTO_IDENTIDAD_GTEMP");
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				//TablaGenerica tab_empleado_ = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
				
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos, fila " + (i + 1));
				} else {
					
				empleadoRegistroManual=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
				tarjeta_marcacion_gtemp=tab_empleado.getValor("tarjeta_marcacion_gtemp");
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
						String str_tarjeta_marcacion_gtemp = tab_empleado.getValor("tarjeta_marcacion_gtemp");
						TablaGenerica tabpartda = null;
						tabpartda = ser_empleado.getPartidaRoles(str_ide_gtemp);
						if (tabpartda.isEmpty() == false) {
						
							
							
						//	if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
						//		str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
						//	}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene partida, fila " + (i + 1));
							}				
					}//validacion documento de identidad
				}// tab_emp
				
				//String str_valor = hoja.getCell(1, i).getContents();
				String str_fecha = hoja.getCell(0, i).getContents();
				String str_hora = hoja.getCell(1, i).getContents();
				String horaTemp="";
				String fecha_hora="";
				//str_valor = str_valor.replaceAll(",", ".");
				if (str_fecha == null) {
					str_fecha = "";
				}
				
				if (str_hora == null) {
					str_hora = "";
				}
				
				String[] partsFecha = str_fecha.split("-");
				String anioFecha = partsFecha[0]; // 123
				String mesFecha = partsFecha[1];
				String diaFecha = partsFecha[2];
				String nuevaFecha=diaFecha+"-"+mesFecha+"-"+anioFecha;
				String nuevaFechaHora="";		
				
				String[] parts = str_hora.split(":");
				String part1 = parts[0]; // 123
				String partMin = parts[1];
				if (Integer.parseInt(part1)<10) {
						horaTemp="0"+part1+":"+partMin+":00";
			    		}else {
						horaTemp=""+part1+":"+partMin+":00";
			    		}
				nuevaFechaHora=nuevaFecha+" "+horaTemp;
				
		    	java.util.Date temp=null;
				//double dou_valor = 0;
				try {
					// Valida que sea una cantidad numerica
					fecha_hora=str_fecha+" "+horaTemp;
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					temp =dateFormat.parse(fecha_hora);
										
					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor fecha no valido , fila " + (i + 1));
				}
				//ff
				//TablaGenerica tab_emp;
			//	Object[] obj_cumula = getAcumulado(str_cedula);
			 	Timestamp t1=	new Timestamp(temp.getTime());

					tab_emp.insertar();
					tab_emp.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
					tab_emp.setValor("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
					tab_emp.setValor(
							"NOMBRES",
							new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ")
									.append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
									.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ")
									.append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
					tab_emp.setValor("fecha_evento_cobim",t1.toString().substring(0,19));
					tab_emp.setValor("ide_persona_cobim",horaTemp);

					
					
					lis_importa.add(new String[] { str_cedula, fecha_hora});
			

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
		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp.getChildren().add(tab_emp);
		dia_valida_empleado.dibujar();


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


public void cancelarImportarEmpleados() {
	dia_importar.cerrar();
	dia_valida_empleado.cerrar();
	upl_archivo.limpiar();
    tab_resumen_marcaciones.setCondicion("ide_cobim=-1");
    tab_resumen_marcaciones.ejecutarSql();
    tab_archi_adjunto.setCondicion("ide_corem=-1");
    tab_archi_adjunto.ejecutarSql();
	sel_empleado_registro_manual.cerrar();
	utilitario.addUpdate("tab_resumen_marcaciones,tab_archi_adjunto,dia_importar,dia_valida_empleado,sel_empleado_registro_manual");
}


public Dialogo getDia_importar() {
	return dia_importar;
}


public void setDia_importar(Dialogo dia_importar) {
	this.dia_importar = dia_importar;
}


public Upload getUpl_archivo() {
	return upl_archivo;
}


public void setUpl_archivo(Upload upl_archivo) {
	this.upl_archivo = upl_archivo;
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
	
	if (importarValoresRubro(lis_importa,empleadoRegistroManual)) {
		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
	
	TablaGenerica TabEmpleadoMarcacion=utilitario.consultar("select ide_gtemp,tarjeta_marcacion_gtemp from gth_empleado where ide_gtemp="+empleadoRegistroManual);
	
	tab_resumen_marcaciones.setCondicion("ide_persona_cobim='"+TabEmpleadoMarcacion.getValor("tarjeta_marcacion_gtemp")+"' AND IDE_COREL=18");
	tab_resumen_marcaciones.setCampoOrden("fecha_evento_cobim desc limit "+lis_importa.size()+" ");
	tab_resumen_marcaciones.ejecutarSql();
	//insertarTablaResumen(18, tarjeta_marcacion_gtemp, "1", temp, 1, true);

	sel_empleado_registro_manual.cerrar();
	dia_importar.cerrar();
	dia_valida_empleado.cerrar();
	
	TablaGenerica tab_persona=utilitario.consultar("SELECT  emp.ide_gtemp,"
	+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
	+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
	+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
	+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
	+ "FROM GTH_EMPLEADO EMP   "
	+ "where  EMP.ide_gtemp="+empleado);
	
	
	
	String apellido_paterno="",apellido_materno="",primer_nombre="",segundo_nombre="";
	
	apellido_paterno=tab_persona.getValor("NOMBRES_APELLIDOS");

	
    tab_resumen_marcaciones.setHeader("RESUMEN DE MARCACIONES EN BIOMÉTRICO POR EMPLEADO "+apellido_paterno);

	

	
}







public Tabla getTab_emp() {
	return tab_emp;
}


public void setTab_emp(Tabla tab_emp) {
	this.tab_emp = tab_emp;
}


public boolean importarValoresRubro(List lis_importa,int empleadoRegistroManual){

	String str_sql="";
	
	if (tipo_perfil.equals("1")) {
		str_sql="SELECT ide_gtemp, documento_identidad_gtemp, primer_nombre_gtemp, segundo_nombre_gtemp,  "
				+ "apellido_paterno_gtemp, apellido_materno_gtemp,activo_gtemp "
				+ "FROM gth_empleado "
				+ "where ide_gtemp in(select ide_gtemp from asi_empleado_jefe_inmediato)";
		
	}else {
		str_sql="SELECT ide_gtemp, documento_identidad_gtemp, primer_nombre_gtemp, segundo_nombre_gtemp,  "
				+ "apellido_paterno_gtemp, apellido_materno_gtemp,activo_gtemp "
				+ "FROM gth_empleado  "
				+ "where ide_gtemp in(select ide_gtemp from asi_empleado_jefe_inmediato where ide_asjei in("+jefe_inmediato_planificacion+"))";
		
	}
	String fec1="",fec2="";
	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);

	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
			  	java.util.Date temp=null;

				if(str_valor!=null){
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						temp =dateFormat.parse(str_valor);
						insertarTablaResumen(18, tarjeta_marcacion_gtemp, "1", temp, 1, true);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//lis_importa.remove(k);
				//break;
			}
		}			

		//Metodo Insertar Resumen
  
	}

	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;
}


public SeleccionTabla getSel_empleado_consulta() {
	return sel_empleado_consulta;
}


public void setSel_empleado_consulta(SeleccionTabla sel_empleado_consulta) {
	this.sel_empleado_consulta = sel_empleado_consulta;
}










}
