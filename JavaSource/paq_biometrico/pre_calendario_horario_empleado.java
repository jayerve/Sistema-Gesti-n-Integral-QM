/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.codehaus.groovy.tools.groovydoc.FileOutputTool;
import org.jfree.util.Log;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.Parametro;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;

import java.util.Calendar;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;


/**
 *
 * @author Juan
 */
public class pre_calendario_horario_empleado extends Pantalla {
    @EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    
    
    @EJB
    private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
    
    
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_calendario= new Tabla(); 
    private Tabla tab_turnos= new Tabla(); 
    private Tabla tab_tabla3= new Tabla();
	private Tabla tab_emp_total = new Tabla();

private boolean copirDepartamento,copiar;

private String empleado1;
private String empleadoSucursal;
private String meses="",anios="",mesEditar="",anioEditar="";
int asignar =0,editarValidacion=0,ultimoDia=0;

    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_mes_editar= new SeleccionTabla();
    private SeleccionTabla sel_anio_editar= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    private SeleccionTabla sel_empleado= new SeleccionTabla();
    private SeleccionTabla sel_empleado_editar= new SeleccionTabla();
    private SeleccionTabla sel_departamento_empleado = new SeleccionTabla();
    private String sucursal="";

    private Dialogo  dia_num_copiar = new Dialogo();
    private Boolean num_copias=false,bandAsignar=false;
    private Boolean bandEdit=false;
    
    private String mes;
    private String anio;
	 private String empleado;
	private  ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	private  ArrayList<Integer> listaAnio = new ArrayList<Integer>();
	private  ArrayList<Integer> listaMes = new ArrayList<Integer>();
	private Calendario cal_fecha_inicial = new Calendario();
	private AutoCompletar aut_empleados=new AutoCompletar();
	private boolean editar=false;
	private Grid gri=new Grid();
	private  ArrayList<String> listaNueva = new ArrayList<String>();
	private  ArrayList<String> listaAnterior = new ArrayList<String>();
	private Check che_todas_nominas = new Check();
	private Etiqueta eti_num_nomina = new Etiqueta();
	private Editor edi_mensajes = new Editor();
	
    private SeleccionTabla sel_mes_importarTotales= new SeleccionTabla();
    private SeleccionTabla sel_anio_importarTotales= new SeleccionTabla();
    private SeleccionTabla sel_empleado_importarTotales= new SeleccionTabla();
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();
	private Dialogo dia_importar_total = new Dialogo();
	private Dialogo dia_valida_empleado_total = new Dialogo();

	private String sucu="";
	private	String area="";
	private String depa="";
	private	String ide_geare="",jefe_padre="",cargo_padre="";
	private String ide_gtempxx="",tipo_perfil="";
	String ide_asjei="";     
	TablaGenerica tabJefeInmediato=null;
	TablaGenerica tabJefeInmediatos=null;
	private Upload upl_archivo = new Upload();
StringBuilder ide_empleados_crear=new StringBuilder();
	
	private Etiqueta eti_25N = new Etiqueta();
	private Etiqueta eti_60 = new Etiqueta();
	private Etiqueta eti_25 = new Etiqueta();
	private Etiqueta eti_50 = new Etiqueta();
	private Etiqueta eti_100 = new Etiqueta();
	
	
	
	
	private Etiqueta eti_dia1 = new Etiqueta();
	private Etiqueta eti_dia2 = new Etiqueta();
	private Etiqueta eti_dia3 = new Etiqueta();
	private Etiqueta eti_dia4 = new Etiqueta();
	private Etiqueta eti_dia5 = new Etiqueta();
	private Etiqueta eti_dia6 = new Etiqueta();
	private Etiqueta eti_dia7 = new Etiqueta();
	private Etiqueta eti_dia8 = new Etiqueta();
	private Etiqueta eti_dia9 = new Etiqueta();
	private Etiqueta eti_dia10 = new Etiqueta();
	private Etiqueta eti_dia11 = new Etiqueta();
	private Etiqueta eti_dia12 = new Etiqueta();
	private Etiqueta eti_dia13 = new Etiqueta();
	private Etiqueta eti_dia14 = new Etiqueta();
	private Etiqueta eti_dia15 = new Etiqueta();
	private Etiqueta eti_dia16 = new Etiqueta();
	private Etiqueta eti_dia17 = new Etiqueta();
	private Etiqueta eti_dia18 = new Etiqueta();
	private Etiqueta eti_dia19 = new Etiqueta();
	private Etiqueta eti_dia20 = new Etiqueta();
	private Etiqueta eti_dia21 = new Etiqueta();
	private Etiqueta eti_dia22 = new Etiqueta();
	private Etiqueta eti_dia23 = new Etiqueta();
	private Etiqueta eti_dia24 = new Etiqueta();
	private Etiqueta eti_dia25 = new Etiqueta();
	private Etiqueta eti_dia26 = new Etiqueta();
	private Etiqueta eti_dia27 = new Etiqueta();
	private Etiqueta eti_dia28 = new Etiqueta();
	private Etiqueta eti_dia29 = new Etiqueta();
	private Etiqueta eti_dia30 = new Etiqueta();
	private Etiqueta eti_dia31 = new Etiqueta();

	
	
	
	

	private List<String[]> lis_importa = null; // Guardo los empleados y el
	private List<String[]> lis_importa60 = null; // Guardo los empleados y el
	private List<String[]> lis_importa25 = null; // Guardo los empleados y el
	private List<String[]> lis_importa50 = null; // Guardo los empleados y el
	private List<String[]> lis_importa100 = null; // Guardo los empleados y el
	
	
	
	private List<String[]> lis_importadia1 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia2 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia3 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia4 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia5 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia6 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia7 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia8 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia9 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia10 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia11 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia12 = null; // Guardo los empleados y el

	private List<String[]> lis_importadia13 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia14 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia15 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia16 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia17 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia18 = null; // Guardo los empleados y el 	
	private List<String[]> lis_importadia19 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia20 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia21 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia22 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia23 = null; // Guardo los empleados y el
	
	private List<String[]> lis_importadia24 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia25 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia26 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia27 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia28 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia29 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia30 = null; // Guardo los empleados y el
	private List<String[]> lis_importadia31 = null; // Guardo los empleados y el
	
	
	private Grid grid_tabla_emp_sum_totales25Loep = new Grid();
	private Grid grid_tabla_emp_sum_totales60 = new Grid();
	private Grid grid_tabla_emp_sum_totales25 = new Grid();
	private Grid grid_tabla_emp_sum_totales50 = new Grid();
	private Grid grid_tabla_emp_sum_totales100 = new Grid();
	private Grid grid_tabla_emp_sum_totales = new Grid();
	private Grid grid_tabla_emp_total = new Grid();

	
    public Grid getGri() {
		return gri;
	}



	public void setGri(Grid gri) {
		this.gri = gri;
	}



	public pre_calendario_horario_empleado() {
    

		//Consulta empleado loggeado
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

		
		
		
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
          				+ "FROM asi_empleado_jefe_inmediato "
          				+ "where activo_emjei=true");
      
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
             				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and activo_emjei=true ");
         
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
		
		
		
		
		
		
		
		
		
		
		//Consulta de empleado por sucursal
		
		TablaGenerica tabSucursal=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EPAR.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.IDE_SUCU,SUCU.NOM_SUCU, AREA.IDE_GEARE,AREA.DETALLE_GEARE,  "
				+ "DEPA.IDE_GEDEP,DEPA.DETALLE_GEDEP   "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "where epar.ide_geedp="+ide_geedp_activo
				+ " ORDER BY DETALLE_GEARE ASC");
		 area=tabSucursal.getValor("IDE_GEARE");
		sucu=tabSucursal.getValor("IDE_SUCU");
		empleadoSucursal=tabSucursal.getValor("IDE_GTEMP");
		
		
		
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
		bar_botones.agregarBoton(bot_asignar_departamento);
    	
/**
 * Boton copia la fila ingresada anteriormente
 */
    	Boton bot_copiar= new Boton();
    	bot_copiar.setIcon("ui-icon-document");
    	bot_copiar.setMetodo("copiarHorario");
    	bot_copiar.setValue("Copiar Horario");
    	bot_copiar.setTitle("Copiar Horario");
    	bar_botones.agregarBoton(bot_copiar);	
    	
    	//
		bar_botones.getBot_insertar().setRendered(false);

    	
    	// boton editar
		Boton bot_editar = new Boton();
		bot_editar.setIcon("ui-icon-cancel");
    	bot_editar.setValue("Editar");
    	bot_editar.setTitle("Editar");
		bot_editar.setMetodo("editarPorMes");
		bar_botones.agregarBoton(bot_editar);

    	
    	// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setValue("Limpiar");
    	bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		
		
	
			aut_empleados.setId("aut_empleados");
			aut_empleados.setAutoCompletar("SELECT asemp.ide_gedep, "
					+ "SUCU.nom_SUCU, AREA.DETALLE_GEARE,depa.DETALLE_gedep "
					+ "from asi_horario_mes_empleado asemp   "
					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=asemp.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=asemp.IDE_SUCURSAL  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=asemp.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=asemp.IDE_GEARE  "
					+ "where asemp.ide_gemes="+mes+" and asemp.ide_geani in("+anio+") and asemp.ide_geedp="+ide_geedp_activo
					+ " GROUP BY asemp.ide_gedep,SUCU.nom_SUCU,AREA.DETALLE_GEARE,depa.DETALLE_gedep");
	
			aut_empleados.setMetodoChange("seleccionoEmpleado");
	
		
		//bar_botones.agregarComponente(aut_empleados);

			if(empleadoSucursal.equals("508") || area.equals("9"))
			{
				meses=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
				anios=utilitario.getVariable("p_anio_asignacion_horario_administrador");
				mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_tthh");

			}else {
				meses=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
				anios=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
				mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
				anioEditar=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
			}
			
    	
    	sel_mes.setId("sel_mes");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+meses+") ","IDE_GEMES");

    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("20");


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
    	sel_mes_editar.setHeight("45");
		sel_mes_editar.getBot_aceptar().setMetodo("obtenerMesEditar");
		agregarComponente(sel_mes_editar);
		
    	sel_anio.setId("sel_anio");
    	//sel_anio.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    			//+ "'%"+pckUtilidades.CConversion.CStr(utilitario.getAnio((utilitario.getFechaActual())))+"%'","IDE_GEANI");
		
		
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
    	
		sel_empleado.setId("sel_empleado");

		agregarComponente(sel_empleado);
    	
		
		
		sel_empleado_editar.setId("sel_empleado_editar");
		
		agregarComponente(sel_empleado_editar);
		
		
		
		//Seleccionar por Departamento
		
		
		sel_departamento_empleado.setId("sel_departamento_empleado");
		//sel_departamento_empleado.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO", "IDE_GEDEP");
		
		sel_departamento_empleado.setSeleccionTabla("SELECT DEPA.IDE_GEDEP,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,SUCU.NOM_SUCU FROM gen_departamento_sucursal DESUC "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=DESUC.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=DESUC.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=DESUC.IDE_GEARE  " 
				+ "where sucu.ide_sucu in("+sucu+")  " 
				+ "	ORDER BY DETALLE_GEARE ASC", "IDE_GEDEP");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		sel_departamento_empleado.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
		sel_departamento_empleado.getTab_seleccion().getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
		sel_departamento_empleado.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);

		sel_departamento_empleado.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(sel_departamento_empleado);
		sel_departamento_empleado.getBot_aceptar().setMetodo("getDepartamento");
		sel_departamento_empleado.setWidth("60");
		sel_departamento_empleado.setHeight("60");
		agregarComponente(sel_departamento_empleado);
	  	
		
		
	
		
		
    	// boton limpiar
    	Boton bot_importar_valores = new Boton();
    	bot_importar_valores.setIcon("ui-icon-cancel");
    	bot_importar_valores.setValue("Importar Valores");
    	bot_importar_valores.setTitle("Importar Valores");
    	bot_importar_valores.setMetodo("abrirDialogoImportar");
    	bar_botones.agregarBoton(bot_importar_valores);
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////IMPORTAR VALORES CALENDARIO//////////////////////////////////////////////////////////////
    	
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
		
		sel_empleado_importarTotales.setId("sel_empleado_importarTotales");
		agregarComponente(sel_empleado_importarTotales);
		
		
		
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
			  		+ "0 as DIA1, "
					+ "0 as DIA2, "
					+ "0 as DIA3, "
					+ "0 as DIA4, "
					+ "0 as DIA5, "
					+ "0 as DIA6, "
					+ "0 as DIA7, "
					+ "0 as DIA8, "	 
					+ "0 as DIA9, "
					+ "0 as DIA10, "
					+ "0 as DIA11, "
					+ "0 as DIA12, "
					+ "0 as DIA13, "
					+ "0 as DIA14, "
					+ "0 as DIA15, "
					+ "0 as DIA16, "
					+ "0 as DIA17, "
					+ "0 as DIA18, "
					+ "0 as DIA19, "
					+ "0 as DIA20, "
					+ "0 as DIA21, "
					+ "0 as DIA22, "
					+ "0 as DIA23, "
					+ "0 as DIA24, "
					+ "0 as DIA25, "
					+ "0 as DIA26, "
					+ "0 as DIA27, "
					+ "0 as DIA28, "
					+ "0 as DIA29, "
					+ "0 as DIA30, "
					+ "0 as DIA31 "
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
	  
	  
	  tab_emp_total.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
	  tab_emp_total.getColumna("DIA1").alinearDerecha();
	  tab_emp_total.getColumna("DIA2").alinearDerecha();
	  tab_emp_total.getColumna("DIA3").alinearDerecha();
	  tab_emp_total.getColumna("DIA4").alinearDerecha();
	  tab_emp_total.getColumna("DIA5").alinearDerecha();
	  tab_emp_total.getColumna("DIA6").alinearDerecha();
	  tab_emp_total.getColumna("DIA7").alinearDerecha();
	  tab_emp_total.getColumna("DIA8").alinearDerecha();
	  tab_emp_total.getColumna("DIA9").alinearDerecha();
	  tab_emp_total.getColumna("DIA10").alinearDerecha();
	  
	  
	  tab_emp_total.getColumna("DIA11").alinearDerecha();
	  tab_emp_total.getColumna("DIA12").alinearDerecha();
	  tab_emp_total.getColumna("DIA13").alinearDerecha();
	  tab_emp_total.getColumna("DIA14").alinearDerecha();
	  tab_emp_total.getColumna("DIA15").alinearDerecha();
	  tab_emp_total.getColumna("DIA16").alinearDerecha();
	  tab_emp_total.getColumna("DIA17").alinearDerecha();
	  tab_emp_total.getColumna("DIA18").alinearDerecha();
	  tab_emp_total.getColumna("DIA19").alinearDerecha();
	  tab_emp_total.getColumna("DIA20").alinearDerecha();
	  

	  tab_emp_total.getColumna("DIA21").alinearDerecha();
	  tab_emp_total.getColumna("DIA22").alinearDerecha();
	  tab_emp_total.getColumna("DIA23").alinearDerecha();
	  tab_emp_total.getColumna("DIA24").alinearDerecha();
	  tab_emp_total.getColumna("DIA25").alinearDerecha();
	  tab_emp_total.getColumna("DIA26").alinearDerecha();
	  tab_emp_total.getColumna("DIA27").alinearDerecha();
	  tab_emp_total.getColumna("DIA28").alinearDerecha();
	  tab_emp_total.getColumna("DIA29").alinearDerecha();
	  tab_emp_total.getColumna("DIA30").alinearDerecha();
	  tab_emp_total.getColumna("DIA31").alinearDerecha();

 
	  
	// tab_emp_total.getColumna("VALOR25N").alinearDerecha();
	 // tab_emp_total.getColumna("VALOR60").alinearDerecha();
	 // tab_emp_total.getColumna("VALOR25").alinearDerecha();
	 // tab_emp_total.getColumna("VALOR50").alinearDerecha();
	 // tab_emp_total.getColumna("VALOR100").alinearDerecha();
	 // 

	  tab_emp_total.setRows(15);
	  tab_emp_total.setLectura(true);
	  tab_emp_total.dibujar();

	  eti_dia1.setId("eti_dia1");
	  eti_dia1.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia2.setId("eti_dia2");
	  eti_dia2.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia3.setId("eti_dia3");
	  eti_dia3.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia4.setId("eti_dia4");
	  eti_dia4.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia5.setId("eti_dia5");
	  eti_dia5.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  
	  
	  
	  eti_dia6.setId("eti_dia6");
	  eti_dia6.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia7.setId("eti_dia7");
	  eti_dia7.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia8.setId("eti_dia8");
	  eti_dia8.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia9.setId("eti_dia9");
	  eti_dia9.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia10.setId("eti_dia10");
	  eti_dia10.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  
	  
	  
	  eti_dia11.setId("eti_dia11");
	  eti_dia11.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia12.setId("eti_dia12");
	  eti_dia12.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia13.setId("eti_dia13");
	  eti_dia13.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia14.setId("eti_dia14");
	  eti_dia14.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia15.setId("eti_dia15");
	  eti_dia15.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  
	  
	  eti_dia16.setId("eti_dia16");
	  eti_dia16.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia17.setId("eti_dia17");
	  eti_dia17.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia18.setId("eti_dia18");
	  eti_dia18.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia19.setId("eti_dia19");
	  eti_dia19.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia20.setId("eti_dia20");
	  eti_dia20.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  

	  eti_dia21.setId("eti_dia21");
	  eti_dia21.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia22.setId("eti_dia22");
	  eti_dia22.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia23.setId("eti_dia23");
	  eti_dia23.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia24.setId("eti_dia24");
	  eti_dia24.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia25.setId("eti_dia25");
	  eti_dia25.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  
	  eti_dia26.setId("eti_dia26");
	  eti_dia26.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia27.setId("eti_dia27");
	  eti_dia27.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia28.setId("eti_dia28");
	  eti_dia28.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia29.setId("eti_dia29");
	  eti_dia29.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia30.setId("eti_dia30");
	  eti_dia30.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_dia31.setId("eti_dia31");
	  eti_dia31.setStyle("font-size:10px;font-weight: bold; width: 130px;");

	  
	  
	//  grid_tabla_emp_sum_totales25Loep.setId("grid_tabla_emp_sum_totales25Loep");
	 // grid_tabla_emp_sum_totales60.setId("grid_tabla_emp_sum_totales60");
	 // grid_tabla_emp_sum_totales25.setId("grid_tabla_emp_sum_totales25");
	  //grid_tabla_emp_sum_totales50.setId("grid_tabla_emp_sum_totales50");
	  //grid_tabla_emp_sum_totales100.setId("grid_tabla_emp_sum_totales100");

	  
	//    grid_tabla_emp_sum_totales.setId("grid_tabla_emp_sum_totales");
	//	grid_tabla_emp_sum_totales.setColumns(5);
		//grid_tabla_emp_sum_totales.setMensajeInfo("DETALLE DE PLANIFICACION DE HORAS EXTRA ");
//		grid_tabla_emp_sum_totales25Loep.setMensajeInfo("#HORAS 25 LOEP");
		//grid_tabla_emp_sum_totales25Loep.getChildren().add(eti_25N);
//		grid_tabla_emp_sum_totales60.setMensajeInfo("#HORAS 60 LOEP");
//		//grid_tabla_emp_sum_totales60.getChildren().add(eti_60);
	//	grid_tabla_emp_sum_totales25.setMensajeInfo("#HORAS 25 CT");
		//grid_tabla_emp_sum_totales25.getChildren().add(eti_25);
	//	grid_tabla_emp_sum_totales50.setMensajeInfo("#HORAS 50 CT");
	//	grid_tabla_emp_sum_totales50.getChildren().add(eti_50);
	//	grid_tabla_emp_sum_totales100.setMensajeInfo("#HORAS EXTRA 100");
		//grid_tabla_emp_sum_totales100.getChildren().add(eti_100);

		grid_tabla_emp_total.setStyle("width:" + (dia_valida_empleado_total.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado_total.getAltoPanel() + "px;overflow:auto;display:block;");

		// dia_valida_empleado.setModal(true);
		dia_valida_empleado_total.setDialogo(grid_tabla_emp_total);
		dia_valida_empleado_total.setDynamic(false);

		agregarComponente(dia_valida_empleado_total);
		
		
		
		
		tab_tabla.setId("tab_tabla");
  	    tab_tabla.setTabla("asi_horario_mes_empleado", "ide_ashme", 1);
  	    tab_tabla.getColumna("ide_ashme").setNombreVisual("COD");
  	    tab_tabla.getColumna("ide_ashme").setLongitud(5);
  	    tab_tabla.getColumna("ide_ashme").alinearCentro();
  	    tab_tabla.getColumna("ide_ashme").setOrden(1);

        tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLectura(true);
  	    tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setOrden(2);

  	    tab_tabla.getColumna("IDE_GTEMP").setCombo("SELECT EPAR.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE");
  	  	tab_tabla.getColumna("IDE_GTEMP").setLectura(true);
        tab_tabla.getColumna("IDE_GTEMP").setLongitud(15);
        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
  	    tab_tabla.getColumna("IDE_GTEMP").setOrden(3);

        
        tab_tabla.getColumna("IDE_GEMES").setCombo("select ide_gemes,detalle_gemes from gen_mes ");
        tab_tabla.getColumna("IDE_GEMES").setVisible(false);
        tab_tabla.getColumna("IDE_GEANI").setCombo("select ide_geani,detalle_geani from gen_anio");
        tab_tabla.getColumna("IDE_GEANI").setVisible(false);
        //tab_tabla.getColumna("registro_manual_cobph").setVisible(false);
        //tab_tabla.getColumna("registro_manual_cobph").setCheck();
        //tab_tabla.getColumna("registro_manual_cobph").setValorDefecto("false");    
       int ultimo_dia=0;
       ultimo_dia=31;//utilitario.getDia(utilitario.getUltimaFechaMes(utilitario.getFechaActual()));
       int dia_no_visibles=0;
       dia_no_visibles=31;
       
       //ultimo_dia
       
       
       for (int i = 1; i <=ultimo_dia; i++) {
           tab_tabla.getColumna("dia"+i).setCombo("SELECT astur.ide_astur,astur.nom_astur "
              		+ "FROM asi_turnos  astur  "
              		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
              	//	+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
              		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) "
              		+ "order by astur.nom_astur asc ");
              		//+ "and (hxe_astur=false or hxe_astur is null)");
       	}
       
       for (int i = (ultimo_dia+1); i <=31; i++) {
    	   tab_tabla.getColumna("dia"+i).setVisible(false);
       }
       
       /*tab_tabla.getColumna("dia1").setCombo("SELECT astur.ide_astur,astur.nom_astur "
       		+ "FROM asi_turnos  astur  "
       		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
       		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
    

       
        tab_tabla.getColumna("dia2").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        
        tab_tabla.getColumna("dia3").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        
        tab_tabla.getColumna("dia4").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia5").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia6").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia7").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        

        
        tab_tabla.getColumna("dia8").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia9").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia10").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia11").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia12").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia13").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia14").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        
        tab_tabla.getColumna("dia15").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia16").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia17").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia18").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        tab_tabla.getColumna("dia19").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia20").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
        
        
        tab_tabla.getColumna("dia21").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia22").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia23").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia24").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia25").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");


        tab_tabla.getColumna("dia26").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia27").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia28").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia29").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia30").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");

        tab_tabla.getColumna("dia31").setCombo("SELECT astur.ide_astur,astur.nom_astur "
           		+ "FROM asi_turnos  astur  "
           		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
           		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");*/
	   tab_tabla.getColumna("dia31").setMetodoChange("validarFinMes");

        //tab_tabla.getColumna("activo_ashme").setNombreVisual("ACTIVO");
        //tab_tabla.getColumna("activo_ashme").setLongitud(5);
  	    //tab_tabla.getColumna("IDE_GTEMP").setOrden(3);

        tab_tabla.getColumna("activo_ashme").setCheck();
        tab_tabla.getColumna("activo_ashme").setValorDefecto("true");
        tab_tabla.getColumna("activo_ashme").alinearCentro();

        tab_tabla.getColumna("num_extra_ashem").setVisible(false);
        tab_tabla.getColumna("num_suple_ashem").setVisible(false);
        tab_tabla.getColumna("ide_asjei").setVisible(false);
        tab_tabla.getColumna("aplica_hora_extra").setVisible(false);

        
        
        tab_tabla.getColumna("aplica_hora_extra").setNombreVisual("H.EXT");
        tab_tabla.getColumna("aplica_hora_extra").setCheck();
        tab_tabla.getColumna("aplica_hora_extra").alinearCentro();
        tab_tabla.getColumna("IDE_SUCURSAL").setVisible(false);
        tab_tabla.getColumna("IDE_GEARE").setVisible(false);
        tab_tabla.getColumna("IDE_GEDEP").setVisible(false);
        
        tab_tabla.getColumna("IDE_GEEDP").setVisible(false);

        
        
        tab_tabla.getColumna("IDE_GEEDP_CAMBIO").setVisible(false);
        tab_tabla.setRows(10);
        tab_tabla.setCondicion("ide_ashme=-1");
        tab_tabla.dibujar();          
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);
        
        
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("ASI_TURNOS", "IDE_ASTUR", 2);
        tab_tabla2.getColumna("ide_astur").setLongitud(8);
        tab_tabla2.getColumna("ide_astur").setOrden(1);
        tab_tabla2.getColumna("ide_astur").setNombreVisual("CODIGO");


        tab_tabla2.getColumna("nom_astur").setLongitud(40);
        tab_tabla2.getColumna("nom_astur").setOrden(2);
        tab_tabla2.getColumna("nom_astur").setNombreVisual("NOMBRE");
        tab_tabla2.getColumna("nom_astur").setLectura(true);
        
        tab_tabla2.getColumna("ide_gtgre").setLongitud(8);
        tab_tabla2.getColumna("ide_gtgre").setCombo("select ide_gtgre,detalle_gtgre from gth_grupo_empleado");
        tab_tabla2.getColumna("ide_gtgre").setAutoCompletar();
        tab_tabla2.getColumna("ide_gtgre").setNombreVisual("TIPO");
        tab_tabla2.getColumna("ide_gtgre").setOrden(3);
        tab_tabla2.getColumna("ide_gtgre").setLectura(true);

        tab_tabla2.getColumna("minuto_tolerancia_astur").setLongitud(8);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setOrden(4);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setNombreVisual("TOL.MIN");
        tab_tabla2.getColumna("minuto_tolerancia_astur").setLectura(true);
        tab_tabla2.getColumna("minuto_tolerancia_astur").setVisible(false);

        tab_tabla2.getColumna("descripcion_astur").setVisible(false);

        tab_tabla2.getColumna("activo_astur").setLongitud(8);
        tab_tabla2.getColumna("activo_astur").setOrden(5);
        tab_tabla2.getColumna("activo_astur").setLectura(true);

        
        tab_tabla2.getColumna("ide_ashor").setOrden(6);
        tab_tabla2.getColumna("ide_ashor").setVisible(false);
        tab_tabla2.getColumna("turno_matriz_astur").setVisible(false);
        tab_tabla2.getColumna("hxe_astur").setVisible(false);

        //tab_tabla2.setCondicion("turno_matriz_astur=false");
        tab_tabla2.setCondicion("turno_matriz_astur=false");
//and (hxe_astur=false or hxe_astur is null)

        tab_tabla2.onSelect("cargarHorario");

        tab_tabla2.setHeader("T U R N O S");
        
        tab_tabla2.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla2);
      
      
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ASI_HORARIO", "IDE_ASHOR", 3);
        tab_tabla3.getColumna("IDE_ASGRI").setVisible(false);
        tab_tabla3.getColumna("IDE_ASGRI").setVisible(false);
        tab_tabla3.setLectura(true);
        tab_tabla3.setCondicion("ide_ashor=-1");
        tab_tabla3.dibujar();
        tab_tabla3.setHeader("H O R A R I O S");
        
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla3);
      
      
      
      
      
    	String dia_final=utilitario.getUltimoDiaMesFecha(utilitario.getFechaActual());
        //int diaFin=utilitario.getDia(dia_final);
        int diaFin=31;

		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '"+utilitario.getAnio(utilitario.getFechaActual())+"'");
		String anioEscogido=tabAnio.getValor("ide_geani");
//TABLA DIAS DEL MES      
  	String sql ="SELECT "
  			+ "''anio , "
  			+ "''mes , ";
  			for (int i = 1; i <= diaFin; i++) {
  	  		if (diaFin==i) {
  				sql+="''dia"+i+" ";

			}else {
  				sql+="''dia"+i+" , ";
			}

			}
  			/*
  			+ "''dia1 , "
  			+ "''dia2 , "
  			+ "''dia3 , "
  			+ "''dia4 , "
  			+ "''dia5 , "
  			+ "''dia6 , "
  			+ "''dia7 , "
  			+ "''dia8 , "
  			+ "''dia9 , "
  			+ "''dia10 , "
  			+ "''dia11 , "
  			+ "''dia12 , "
  			+ "''dia13 , "
  			+ "''dia14 , "
  			+ "''dia15 , "
  			+ "''dia16 , "
  			+ "''dia17 , "
  			+ "''dia18 , "
  			+ "''dia19 , "
  			+ "''dia20 , "
  			+ "''dia21 , "
  			+ "''dia22 , "
  			+ "''dia23 , "
  			+ "''dia24 , "
  			+ "''dia25 , "
  			+ "''dia26 , "
  			+ "''dia27 , "
  			+ "''dia28 , "
  			+ "''dia29 , "
  			+ "''dia30 , "
  			+ "''dia31 " +*/
  			sql+="from gen_anio where ide_geani="+anioEscogido;
  	
    tab_calendario.setId("tab_calendario");
    tab_calendario.setSql(sql);
    
 	tab_calendario.getColumna("anio").setLongitud(21);
 	tab_calendario.getColumna("anio").alinearCentro();
 	tab_calendario.getColumna("anio").setNombreVisual("ANIO");

 	tab_calendario.getColumna("mes").setLongitud(85);
 	tab_calendario.getColumna("mes").alinearCentro();
 	tab_calendario.getColumna("mes").setNombreVisual("MES");

 	tab_calendario.setNumeroTabla(4);
    
		for (int i = 1; i <= diaFin; i++) {
			tab_calendario.getColumna("dia"+i).setLongitud(75);
		 	tab_calendario.getColumna("dia"+i).alinearCentro();                                                      	
		}

 	
 	
 	
 	/*
 	tab_calendario.getColumna("dia1").setLongitud(75);
 	tab_calendario.getColumna("dia1").alinearCentro();
  	tab_calendario.getColumna("dia2").setLongitud(75);
 	tab_calendario.getColumna("dia2").alinearCentro();
  	tab_calendario.getColumna("dia3").setLongitud(75);
 	tab_calendario.getColumna("dia3").alinearCentro();
  	tab_calendario.getColumna("dia4").setLongitud(75);
 	tab_calendario.getColumna("dia4").alinearCentro();

  	tab_calendario.getColumna("dia5").setLongitud(75);
 	tab_calendario.getColumna("dia5").alinearCentro();

  	tab_calendario.getColumna("dia6").setLongitud(75);
 	tab_calendario.getColumna("dia6").alinearCentro();

  	tab_calendario.getColumna("dia7").setLongitud(75);
 	tab_calendario.getColumna("dia7").alinearCentro();

  	tab_calendario.getColumna("dia8").setLongitud(75);
 	tab_calendario.getColumna("dia8").alinearCentro();

  	tab_calendario.getColumna("dia9").setLongitud(75);
 	tab_calendario.getColumna("dia9").alinearCentro();

  	tab_calendario.getColumna("dia10").setLongitud(75);
 	tab_calendario.getColumna("dia10").alinearCentro();

  	tab_calendario.getColumna("dia11").setLongitud(75);
 	tab_calendario.getColumna("dia11").alinearCentro();

  	tab_calendario.getColumna("dia12").setLongitud(75);
 	tab_calendario.getColumna("dia12").alinearCentro();

  	tab_calendario.getColumna("dia13").setLongitud(75);
 	tab_calendario.getColumna("dia13").alinearCentro();

  	tab_calendario.getColumna("dia14").setLongitud(75);
 	tab_calendario.getColumna("dia14").alinearCentro();

  	tab_calendario.getColumna("dia15").setLongitud(75);
 	tab_calendario.getColumna("dia15").alinearCentro();

  	tab_calendario.getColumna("dia16").setLongitud(75);
 	tab_calendario.getColumna("dia16").alinearCentro();

  	tab_calendario.getColumna("dia17").setLongitud(75);
 	tab_calendario.getColumna("dia17").alinearCentro();

  	tab_calendario.getColumna("dia18").setLongitud(75);
 	tab_calendario.getColumna("dia18").alinearCentro();

  	tab_calendario.getColumna("dia19").setLongitud(75);
 	tab_calendario.getColumna("dia19").alinearCentro();

  	tab_calendario.getColumna("dia20").setLongitud(75);
 	tab_calendario.getColumna("dia20").alinearCentro();

  	tab_calendario.getColumna("dia21").setLongitud(75);
 	tab_calendario.getColumna("dia21").alinearCentro();

  	tab_calendario.getColumna("dia22").setLongitud(75);
 	tab_calendario.getColumna("dia22").alinearCentro();

  	tab_calendario.getColumna("dia23").setLongitud(75);
 	tab_calendario.getColumna("dia23").alinearCentro();

  	tab_calendario.getColumna("dia24").setLongitud(75);
 	tab_calendario.getColumna("dia24").alinearCentro();

  	tab_calendario.getColumna("dia25").setLongitud(75);
 	tab_calendario.getColumna("dia25").alinearCentro();

  	tab_calendario.getColumna("dia26").setLongitud(75);
 	tab_calendario.getColumna("dia26").alinearCentro();

  	tab_calendario.getColumna("dia27").setLongitud(75);
 	tab_calendario.getColumna("dia27").alinearCentro();

  	tab_calendario.getColumna("dia28").setLongitud(75);
 	tab_calendario.getColumna("dia28").alinearCentro();

  	tab_calendario.getColumna("dia29").setLongitud(75);
 	tab_calendario.getColumna("dia29").alinearCentro();

  	tab_calendario.getColumna("dia30").setLongitud(75);
 	tab_calendario.getColumna("dia30").alinearCentro();

  	tab_calendario.getColumna("dia31").setLongitud(75);
 	tab_calendario.getColumna("dia31").alinearCentro();
 	
 	*/
 	tab_calendario.setLectura(true);
    tab_calendario.dibujar();

//Actualizo mi tabla calendario con el mes actual 
    getCalendario(""+utilitario.getMes(utilitario.getFechaActual()),""+utilitario.getAnio(utilitario.getFechaActual()));

/*Grupo gru = new Grupo();
gru.getChildren().add(tab_calendario);
gru.getChildren().add(tab_tabla);
   *
   */ 
	 
    Grid gri_cabecera=new Grid();
	gri_cabecera.setWidth("100%");

	gri=new Grid();
	gri.setWidth("100%");
	gri.setStyle("display:block;height:100%");
	gri.getChildren().add(tab_calendario);
	gri.getChildren().add(tab_tabla);
	
	Division div_division = new Division();
	//div_division.getChildren().clear();
	div_division.dividir1(gri);

	Division div_division2 = new Division();
	div_division2.dividir2(pat_panel3,pat_panel5,"50%","V");
	Division div_division1 = new Division();
	 div_division1.dividir2(div_division,div_division2,  "60%", "H");     
     agregarComponente(div_division1);
     
    }
    
    
    public void abrirMes(){
		//tab_tabla.setLectura(false);
    	//tab_tabla.setCondicion("ide_ashme=-1");
    	//tab_tabla.ejecutarSql();
    	// tab_tabla.actualizar();
    	//tab_tabla.dibujar();
    	//utilitario.addUpdate("tab_tabla");
    	
    	//SD
    	asignar++;
		
    	if (asignar>1 && bandEdit==false) {
    		tab_calendario.setLectura(false);
    		tab_tabla.setLectura(false);

    		getCalendario("4", "2018");
        	tab_tabla.actualizar();
        	utilitario.addUpdate("tab_tabla,tab_calendario");
		}else {
			if (bandEdit==true) {
				tab_tabla.setLectura(false);
    	tab_tabla.setCondicion("ide_ashme=-1");
    	tab_tabla.ejecutarSql();
    	utilitario.addUpdate("tab_tabla");
		    	bandEdit=false;
    	
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
    
    //Metodo distingue si es ingreso o edicion
    public void obtenerAnio(){
 	   utilitario.addUpdate("sel_empleado");

        anio=sel_anio.getValorSeleccionado();
        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
    			return;
    		}else {
	   	sel_anio.cerrar();
	/*   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    			+ "'%"+pckUtilidades.CConversion.CStr(utilitario.getAnio((utilitario.getFechaActual())))+"%'");
	*/	
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")");

	   	
	   	
		int anioEmpleado=Integer.parseInt(sel_anio.getValorSeleccionado());    			
	//Aqui busco por el mes seleccionado 
    //	
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
		//Mes Base de Personas Asignadas  el mes de marzo en la asignacion mensual 
		TablaGenerica tabEmpleadoMensualBase=utilitario.consultar("select ide_gtemp,ide_ashme "
				+ "from asi_horario_mes_empleado  "
				+ " where ide_geani in("+anioEmpleado+") and ide_gemes in ("+mesActual+") and ide_geedp="+ide_geedp_activo);
		
			
		StringBuilder str_ide_empleado_mensualBase=new StringBuilder();
		String int_num_col_idegetempmensualBase="";
		for (int i = 0; i < tabEmpleadoMensualBase.getTotalFilas(); i++) {
			int_num_col_idegetempmensualBase=tabEmpleadoMensualBase.getValor(i, "IDE_GTEMP");
			if(str_ide_empleado_mensualBase.toString().isEmpty()==false){
				str_ide_empleado_mensualBase.append(",");
		 }
			str_ide_empleado_mensualBase.append(int_num_col_idegetempmensualBase);
		}
		
		
		
		if (mes.equals("13")) {
			mes="1";
		}
		
		
		
		
		TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
		TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				+ "FROM asi_jefe_inmediato   "
				+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp")+" and activo_asjei=true");
		
		TablaGenerica tabEmpleadoXJefeInmediato=null;
		if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
			utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
		}else if(tabJefeInmediato.getValor("tipo_asjei").equals("1")){

			tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
					+ "FROM asi_empleado_jefe_inmediato where activo_emjei=true ");
			
		}else{
		tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT aseji.ide_emjei, aseji.ide_asjei, aseji.ide_gtemp "
					+ "FROM asi_empleado_jefe_inmediato aseji "
					+ "left join asi_jefe_inmediato asjei on asjei.ide_asjei=aseji.ide_asjei  "
					+ "where asjei.ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and asjei.activo_asjei=true and aseji.activo_emjei=true");
		}

		StringBuilder str_ide_empleado_asignado=new StringBuilder();
 		String int_num_col_ideasignado="";
 		for (int j = 0; j < tabEmpleadoXJefeInmediato.getTotalFilas(); j++) {
 			int_num_col_ideasignado=tabEmpleadoXJefeInmediato.getValor(j, "IDE_GTEMP");
  	  	if(str_ide_empleado_asignado.toString().isEmpty()==false){
  	  	str_ide_empleado_asignado.append(",");

  	  		  	}
  	  str_ide_empleado_asignado.append(int_num_col_ideasignado);
  		}
		
		//
 		TablaGenerica tabEmpleadoMensual=null;
		/*if (str_ide_empleado_asignado.length()==0) {
            /*abEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
					+ "from asi_horario_mes_empleado  "
					+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and ide_geedp="+ide_geedp_activo);
	
			
			
			
		
		}else{*/
			//Empleados que se encuentran asignado horario para el mes y anio seleccionado 
 		tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
				+ "from asi_horario_mes_empleado  "
				//+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and ide_geedp="+ide_geedp_activo);
				+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and ide_gtemp in("+str_ide_empleado_asignado.toString()+") ");
 		//}
		
		
		
		
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
 		
 		

	///Validacion empleado con horario de anual
	
		TablaGenerica tabEmpleadoAnual=utilitario.consultar("select ide_gtemp,ide_astur from gth_empleado where ide_astur is not null");
		//Empleados asignados en ese mes
 		StringBuilder str_ide_empleado_anual=new StringBuilder();
 		String int_num_col_idegetempanual="";
 		for (int j = 0; j < tabEmpleadoAnual.getTotalFilas(); j++) {
 			int_num_col_idegetempanual=tabEmpleadoAnual.getValor(j, "IDE_GTEMP");
  	  	if(str_ide_empleado_anual.toString().isEmpty()==false){
  	  	  	str_ide_empleado_anual.append(",");

  	  		  	}
  	  	str_ide_empleado_anual.append(int_num_col_idegetempanual);
  		}
 		
 		
		
	
	if (str_ide_empleado_mensual.length()==1  &&  str_ide_empleado_mensual.equals(",")) {
			str_ide_empleado_mensual=null;
	str_ide_empleado_mensual.append("");
	}
	

	if (str_ide_empleado_anual.length()==1  &&  str_ide_empleado_anual.equals(",")) {
		str_ide_empleado_anual=null;
		str_ide_empleado_anual.append("");
}
 		
 		
 		
		
 		
			 		if (str_ide_empleado_mensual==null || str_ide_empleado_mensual.toString().equals("") || str_ide_empleado_mensual.toString().isEmpty() || str_ide_empleado_mensual.length()==0) {
 			
			/*	sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
				+ "emp.documento_identidad_gtemp  "
				+ "	 from  asi_horario_mes_empleado asemp "
				+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true  "
				+ " and asemp.ide_geani in("+anioEmpleado+") and asemp.ide_gemes="+mesActual+" and asemp.ide_geedp="+ide_geedp_activo+" "  
				+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
				+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
				+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
			 			
				if(tabJefeInmediato.getValor("tipo_asjei").equals("1")){

			 			sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
								+ "from  asi_empleado_jefe_inmediato asemp  "
								+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
								+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
								+ "WHERE asemp.activo_emjei=true "
								+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
								+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
								+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
				}else{
					sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
							+ "from  asi_empleado_jefe_inmediato asemp  "
							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
							+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							+ "WHERE asemp.ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and asemp.activo_emjei=true  "
							+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
					
				}

 			 
 			
 			
 	 		
 	 		}else {
 	 			
 	 			//if (str_ide_empleado_mensual.length()==0) {

 	 			/*		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
	 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
	 					+ "emp.documento_identidad_gtemp  "
	 					+ "	 from  asi_horario_mes_empleado asemp "
	 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
	 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
	 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
	 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
	 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
	 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in ("+str_ide_empleado_anual+") "
	 					+ " and asemp.ide_geani="+anioEmpleado+" and asemp.ide_gemes="+(mesActual)+" and asemp.ide_geedp="+ide_geedp_activo+" "  
	 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
	 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
 	 			}else{*/
 	 				
 	 				
 	 				//if (copiar) {
						
				//	}
 	 				
 	 				/*sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
 		 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
 		 					+ "emp.documento_identidad_gtemp  "
 		 					+ "	 from  asi_horario_mes_empleado asemp "
 		 					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
 		 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 		 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
 		 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
 		 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
 		 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in ("+str_ide_empleado_mensual+","+str_ide_empleado_anual+") "
 		 					+ " and asemp.ide_geani="+anioEmpleado+" and asemp.ide_gemes="+(mesActual)+" and asemp.ide_geedp="+ide_geedp_activo+" "  
 		 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
 		 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 		 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
 	
 	 			
 				if(tabJefeInmediato.getValor("tipo_asjei").equals("2")){
 	 			
 	 			
 	 			sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
 							+ "from  asi_empleado_jefe_inmediato asemp  "
 							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
 							+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 							+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+") and ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and asemp.activo_emjei=true "
 							+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
 							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
 	 				
 	 			}else{
 	 				
 	 				sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
 							+ "from  asi_empleado_jefe_inmediato asemp  "
 							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
 							+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 							+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+") and asemp.activo_emjei=true "
 							+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
 							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
 	 				
 	 			}
 			}
 		
 		
 		/*if (area.equals("9")  || empleadoSucursal.equals("508")) {
 			
 			
 			if (str_ide_empleado_mensual.length()==0) {
 				sql="select epar.ide_gtemp,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
 	 					+ "emp.documento_identidad_gtemp "
 	 					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
 	 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 	 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
 	 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
 	 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
 	 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "
 	 					+ " and emp.ide_gtemp  not in("+str_ide_empleado_anual+")"  
 	 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
 	 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 	 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
			}else{
 			/*sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
 					+ "emp.documento_identidad_gtemp "
 					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
 					+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
 					+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
 					+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "
 					+ " and emp.ide_gtemp  not in("+str_ide_empleado_mensual+","+str_ide_empleado_anual+")"  
 					+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
				
			/*	if (tabJefeInmediato.getValor("ide_asjei")==null || tabJefeInmediato.getValor("ide_asjei").equals("") || tabJefeInmediato.getValor("ide_asjei").isEmpty() ) {
					/*sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
							+ "from  asi_empleado_jefe_inmediato asemp  "
							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
							+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							+ "WHERE ide_asjei="+tabJefeInmediato.getValor("ide_asjei")
							+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
							+ " ORDER BY epar.ide_gtemp,EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
				/*	sql="select epar.ide_gtemp,  "
		 					+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
		 					+ "emp.documento_identidad_gtemp "
		 					+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
		 					+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
		 					//+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		 					//+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
		 					//+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
		 					+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "
		 					+ " and emp.ide_gtemp  not in("+str_ide_empleado_mensual+","+str_ide_empleado_anual+")"  
		 					+ " GROUP BY  epar.ide_gtemp, EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
		 					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
		 					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
				}else{
				sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
						+ "from  asi_empleado_jefe_inmediato asemp  "
						+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
						+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
						+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+") and ide_asjei="+tabJefeInmediato.getValor("ide_asjei")
						+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
						+ " ORDER BY epar.ide_gtemp,EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
						+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";}
				
			
		}*/
 		

 		
 		
 		
 		
 		
 		
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
    
    
    //Metodo distingue si es ingreso o edicion
    public void obtenerAnioEditar(){

        anio=sel_anio_editar.getValorSeleccionado();
        if (anio==null || (anio.isEmpty() || anio.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
    			return;
    		}else {
    			sel_anio_editar.cerrar();
    			TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    	    			+ "'%"+pckUtilidades.CConversion.CStr(utilitario.getAnio((utilitario.getFechaActual())))+"%'");
    			//int anioEmpleado=Integer.parseInt(tab_anio.getValor("ide_geani"));    			
    			TablaGenerica tablaEmpleado= utilitario.consultar("select * from asi_horario_mes_empleado  where ide_geani in "
    					+ "("+anio+") and ide_gemes="+mes);

    			sel_empleado_editar.setId("sel_empleado_editar");
    			
    			
    			TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
    			TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
    					+ "FROM asi_jefe_inmediato   "
    					+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
    			
    			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
    			String sql="";
    			if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    			}else if (tabJefeInmediato.getValor("tipo_asjei").equals("1")) {    				
    				
    						sql="select emp.ide_gtemp, emp.apellido_paterno_gtemp,  "
        					+ "(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) as apellido_materno_gtemp,primer_nombre_gtemp,   "
        					+ "(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as segundo_nombre_gtemp,emp.documento_identidad_gtemp "
        					+ "from gth_empleado emp   "
        					+ "left join asi_horario_mes_empleado asemp on asemp.ide_gtemp=emp.ide_gtemp  "
        					+ " where emp.activo_gtemp in (true) "
        					+ " and ide_gemes="+mes+" and ide_geani in("+sel_anio_editar.getValorSeleccionado()+")  "
        					+ "order by apellido_paterno_gtemp, apellido_materno_gtemp";
        					sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");
        		
				}else {
					
				//	TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
				//	TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				//			+ "FROM asi_jefe_inmediato   "
				//			+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
					
		
			    						
							sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
							+ "emp.documento_identidad_gtemp  "
							+ "	 from  asi_horario_mes_empleado asemp "
							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true  "
							+ " and asemp.ide_geani in("+sel_anio_editar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") and asemp.ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" "  
							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

							sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");

				  }
					
					
					
    			/*sel_empleado_editar.setSeleccionTabla("select emp.ide_gtemp, emp.apellido_paterno_gtemp,  "
    					+ "(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) as apellido_materno_gtemp,primer_nombre_gtemp,   "
    					+ "(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as segundo_nombre_gtemp,documento_identidad_gtemp    "
    					+ "from asi_horario_mes_empleado asemp  "
    					+ "left join  gth_empleado emp  on emp.ide_gtemp=asemp.ide_gtemp   "
    					+ "where emp.activo_gtemp = true "
    					+ "and ide_gemes=11 and ide_geani=10 and asemp.ide_geedp="+ide_geedp_activo+" "
    					+ "order by apellido_paterno_gtemp, apellido_materno_gtemp,IDE_GTEMP","IDE_GTEMP");
    			*/
    			System.out.println(""+sql);
    			sel_empleado_editar.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
    			sel_empleado_editar.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
    			
    			sel_empleado_editar.setTitle("Seleccione un Empleado");
    			gru_pantalla.getChildren().add(sel_empleado_editar);
    			sel_empleado_editar.getBot_aceptar().setMetodo("getEmpleadoEditar");
    		   	sel_empleado_editar.redibujar();
    		   	utilitario.addUpdate("sel_empleado_editar");
    		   	
	
    }
    
    
    }
    
    
    public void copiarHorario(){

//No existe empleado para realizar la copia del horario 		
    	
    	if (tab_tabla.getTotalFilas() <=0 ) {
			utilitario.agregarMensajeError("No se puede realizar la copia", "Debe guardar la fila a copiar o seleccionar una fila v�lida");
			return;
		}
    	if (tab_tabla.getValorSeleccionado().equals("") || tab_tabla.getValorSeleccionado().isEmpty() || tab_tabla.getValorSeleccionado().equals("-1") ) {
			utilitario.agregarMensajeError("No se puede realizar la copia", "Debe guardar la fila a copiar");
			return;
		}
    	int anioEmpleado=0,resultadoAnio=0;
    	anioEmpleado=Integer.parseInt(tab_tabla.getValor("IDE_GEANI"));
    	String sql="";
    	int mesConsulta=0;
 		if (mes.equals("1")){
			mes="13";
			mesConsulta=13;
			resultadoAnio=anioEmpleado-1;
			anioEmpleado=resultadoAnio;
		}else {
		    resultadoAnio=anioEmpleado;
			anioEmpleado=resultadoAnio;
			mesConsulta=Integer.parseInt(mes);
		}
		int mesActual=(Integer.parseInt(mes)-1);
		//Mes Base de Personas Asignadas  
		TablaGenerica tabEmpleadoMensualBase=utilitario.consultar("select ide_gtemp,ide_ashme "
				+ "from asi_horario_mes_empleado  "
				+ " where ide_geani in("+anioEmpleado+") and ide_gemes in ("+mesActual+") and ide_geedp="+ide_geedp_activo);
    	
    
    	    	
		StringBuilder str_ide_empleado_mensualBase=new StringBuilder();
		String int_num_col_idegetempmensualBase="";
		for (int i = 0; i < tabEmpleadoMensualBase.getTotalFilas(); i++) {
			int_num_col_idegetempmensualBase=tabEmpleadoMensualBase.getValor(i, "IDE_GTEMP");
			if(str_ide_empleado_mensualBase.toString().isEmpty()==false){
				str_ide_empleado_mensualBase.append(",");
		 }
			str_ide_empleado_mensualBase.append(int_num_col_idegetempmensualBase);
		}
		
		
	
		if (mes.equals("13")) {
			mes="1";
		}
    	    	
    //	String sql="";
		    	
    	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like  "
    			+ "'%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");
		anioEmpleado=Integer.parseInt(tab_anio.getValor("ide_geani"));    			

		
		
		int mes=Integer.parseInt(tab_tabla.getValor("ide_gemes"));
		int anio=Integer.parseInt(tab_tabla.getValor("ide_geani"));

		TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
		TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				+ "FROM asi_jefe_inmediato   "
				+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
		
		/*TablaGenerica tabEmpleadoMensual=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
				+ "FROM asi_empleado_jefe_inmediato "
				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));*/
		
		
		TablaGenerica tabEmpleadoXJefeInmediato=null;
		if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
			utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
		}else if(tabJefeInmediato.getValor("tipo_asjei").equals("1")){

			tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
					+ "FROM asi_empleado_jefe_inmediato "
					+ "where activo_emjei=true");
			
		}else{
		tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
					+ "FROM asi_empleado_jefe_inmediato "
					+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and activo_emjei=true");
		}

		StringBuilder str_ide_empleado_asignado=new StringBuilder();
 		String int_num_col_ideasignado="";
 		for (int j = 0; j < tabEmpleadoXJefeInmediato.getTotalFilas(); j++) {
 			int_num_col_ideasignado=tabEmpleadoXJefeInmediato.getValor(j, "IDE_GTEMP");
  	  	if(str_ide_empleado_asignado.toString().isEmpty()==false){
  	  	str_ide_empleado_asignado.append(",");

  	  		  	}
  	  str_ide_empleado_asignado.append(int_num_col_ideasignado);
  		}

		
		
		
		
		/*if (tabJefeInmediato.getValor("tipo_asjei")==null   || tabJefeInmediato.getValor("tipo_asjei").equals("")   || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
		utilitario.agregarMensajeInfo("Usted no contiene permisos para esta panatalla", "Por favor pongase en contacto con el Administrador");
			return;
		}else if(tabJefeInmediato.getValor("tipo_asjei").equals("1")){	
		tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
				+ "from asi_horario_mes_empleado  "
				+ " where ide_geani in ("+anio+") and ide_gemes in("+mes+") ");
		}else{
			tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
					+ "from asi_horario_mes_empleado  "
					+ " where ide_geani in ("+anio+") and ide_gemes in("+mes+") "
					//+ "and ide_geedp="+ide_geedp_activo);
				+ "and ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
		}*/
TablaGenerica tabEmpleadoMensual=null;
/*if (str_ide_empleado_asignado.length()==0) {
    /*abEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
			+ "from asi_horario_mes_empleado  "
			+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and ide_geedp="+ide_geedp_activo);

	
	
	

}else{*/
	//Empleados que se encuentran asignado horario para el mes y anio seleccionado 
	tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashme "
		+ "from asi_horario_mes_empleado  "
		//+ " where ide_geani="+sel_anio.getValorSeleccionado()+" and ide_gemes in("+mes+") and ide_geedp="+ide_geedp_activo);
		+ " where ide_geani="+anio+" and ide_gemes in("+mes+") and ide_gtemp in("+str_ide_empleado_asignado.toString()+") ");
	//}

		         
 		StringBuilder str_ide_empleado_mensual=new StringBuilder();
 		String int_num_col_idegetempmensual;
 		for (int i = 0; i < tabEmpleadoMensual.getTotalFilas(); i++) {
 			int_num_col_idegetempmensual=tabEmpleadoMensual.getValor(i, "IDE_GTEMP");
  	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
  	  	str_ide_empleado_mensual.append(",");
          }
  	  str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
  }

 	
 		

 		
 		

 		if (str_ide_empleado_mensual==null || str_ide_empleado_mensual.toString().equals("") || str_ide_empleado_mensual.toString().isEmpty()) {
	
	/*sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
	+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
	+ "emp.documento_identidad_gtemp  "
	+ "	 from  asi_horario_mes_empleado asemp "
	+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
	+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
	+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
	+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
	+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
	+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true  "
	+ " and asemp.ide_geani in("+anioEmpleado+") and asemp.ide_gemes="+(mes)+" and asemp.ide_geedp="+ide_geedp_activo+" "  
	+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
	+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/

		
	if(tabJefeInmediato.getValor("tipo_asjei").equals("1")) {
		sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
				+ "emp.documento_identidad_gtemp "
				+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
				+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_astur is null and emp.ide_gtemp not in ("+str_ide_empleado_mensual+") "  
				+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
				+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
				+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

	}

	}else {

		if(tabJefeInmediato.getValor("tipo_asjei").equals("2")) {
			sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
						+ "from  asi_empleado_jefe_inmediato asemp  "
						+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
						+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
						+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+") and ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and asemp.activo_emjei=true "  
						+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
						+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
						+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
				
				
		
		
		
		}else {
					/*sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
							+ "emp.documento_identidad_gtemp "
							+ "from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true and emp.ide_gtemp not in ("+str_ide_empleado_mensual+") and emp.ide_astur is null "  
							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/

			
			sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
					+ "from  asi_empleado_jefe_inmediato asemp  "
					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
					+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+") and asemp.activo_emjei=true "
					+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
					+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
					+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
				}
	}






    	sel_empleado.setSeleccionTabla(sql, "IDE_GTEMP");
	   
	   
 		sel_empleado.getTab_seleccion().getColumna("APELLIDO_PATERNO_GTEMP").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
 		sel_empleado.setHeight("60");
 		sel_empleado.setWidth("40");

 		sel_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(sel_empleado);
		sel_empleado.getBot_aceptar().setMetodo("getEmpleado");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
	   utilitario.addUpdate("sel_empleado");
       	num_copias=true;
    	
        //Se va al metodo getEmpleado()
    }
    	
    /**
     * 	
     * @param empleado
     * @param numIngresos
     * Metodo para copiar las filas de acuerdo al empleado y numero de repeticioe
     */
   public void copiarFila(ArrayList listaEmpleado,int numIngresos,String empleadoCopiar,String mes,String anio){ 	


       	TablaGenerica  tab_empleados=utilitario.consultar("select * from asi_horario_mes_empleado where ide_ashme="+empleadoCopiar+" and ide_geani="+anio+" and "
       			+ "ide_gemes="+mes);
      	for (int i = 0; i < numIngresos; i++) {
           	TablaGenerica  tab_empleadosTurno=utilitario.consultar("select * from asi_horario_mes_empleado where ide_gtemp="+listaEmpleado.get(i)+" and "
           			+ "ide_geani in ("+anio+") and ide_gemes in ("+mes+")");
          if (tab_empleadosTurno.getTotalFilas()>0) {
	
}else {
	
	
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
	
	
	 sucursal=tabSucuAreaDept.getValor("IDE_SUCU").toString();
	area=tabSucuAreaDept.getValor("IDE_GEARE").toString();
	depa=tabSucuAreaDept.getValor("IDE_GEDEP").toString();
tab_tabla.setLectura(false);
    	tab_tabla.insertar();
    	tab_tabla.setValor("ide_gemes",tab_empleados.getValor("ide_gemes"));
    	tab_tabla.setValor("ide_gtemp",""+listaEmpleado.get(i));
    	tab_tabla.setValor("dia1",tab_empleados.getValor("dia1"));
    	tab_tabla.setValor("dia2",tab_empleados.getValor("dia2"));
    	tab_tabla.setValor("dia3",tab_empleados.getValor("dia3"));
    	tab_tabla.setValor("dia4",tab_empleados.getValor("dia4"));
    	tab_tabla.setValor("dia5",tab_empleados.getValor("dia5"));
    	tab_tabla.setValor("dia6",tab_empleados.getValor("dia6"));
    	tab_tabla.setValor("dia7",tab_empleados.getValor("dia7"));
    	tab_tabla.setValor("dia8",tab_empleados.getValor("dia8"));
    	tab_tabla.setValor("dia9",tab_empleados.getValor("dia9"));
    	tab_tabla.setValor("dia10",tab_empleados.getValor("dia10"));
    	tab_tabla.setValor("dia11",tab_empleados.getValor("dia11"));
    	tab_tabla.setValor("dia12",tab_empleados.getValor("dia12"));
    	tab_tabla.setValor("dia13",tab_empleados.getValor("dia13"));
    	tab_tabla.setValor("dia14",tab_empleados.getValor("dia14"));
    	tab_tabla.setValor("dia15",tab_empleados.getValor("dia15"));
    	tab_tabla.setValor("dia16",tab_empleados.getValor("dia16"));
    	tab_tabla.setValor("dia17",tab_empleados.getValor("dia17"));
    	tab_tabla.setValor("dia18",tab_empleados.getValor("dia18"));
    	tab_tabla.setValor("dia19",tab_empleados.getValor("dia19"));
    	tab_tabla.setValor("dia20",tab_empleados.getValor("dia20"));
    	tab_tabla.setValor("dia21",tab_empleados.getValor("dia21"));
    	tab_tabla.setValor("dia22",tab_empleados.getValor("dia22"));
    	tab_tabla.setValor("dia23",tab_empleados.getValor("dia23"));
    	tab_tabla.setValor("dia24",tab_empleados.getValor("dia24"));
    	tab_tabla.setValor("dia25",tab_empleados.getValor("dia25"));
    	tab_tabla.setValor("dia26",tab_empleados.getValor("dia26"));
    	tab_tabla.setValor("dia27",tab_empleados.getValor("dia27"));
    	tab_tabla.setValor("dia28",tab_empleados.getValor("dia28"));
    	tab_tabla.setValor("dia29",tab_empleados.getValor("dia29"));
    	tab_tabla.setValor("dia30",tab_empleados.getValor("dia30"));
    	tab_tabla.setValor("dia31",tab_empleados.getValor("dia31"));
    	tab_tabla.setValor("aplica_hora_extra",tab_empleados.getValor("aplica_hora_extra"));
    	tab_tabla.setValor("activo_ashme",tab_empleados.getValor("activo_ashme"));
    	tab_tabla.setValor("ide_geani",tab_empleados.getValor("ide_geani"));
		tab_tabla.setValor("IDE_SUCURSAL",sucu);
		tab_tabla.setValor("ide_GEARE",area);
		tab_tabla.setValor("ide_GEDEP",depa);
		
		//tab_tabla.setValor("registro_manual_cobph","false");

       		// INGRESO EMPLEADO
	     tab_tabla.setValor("IDE_GEEDP",""+ Integer.parseInt(ide_geedp_activo));
	     TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
		TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				+ "FROM asi_jefe_inmediato   "
				+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
		tab_tabla.setValor("ide_asjei", tabJefeInmediato.getValor("ide_asjei"));
		tab_tabla.setValor("DOCUMENTO_IDENTIDAD_GTEMP", tabSucuAreaDept.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));

    	}

          
}
    	//System.out.println("asi es");
    	num_copias=false;
		//utilitario.addUpdate("tab_tabla,tab_calendario,sel_empleado");

    	utilitario.addUpdate("sel_empleado,aut_empleados");

    }
    
	String ide_geedp_activo="";

    
    
    /**
     * Metodo para consulta de empleados por mes y a�o
     */
    public void getEmpleadoEditar(){
    	try {
    		empleado=sel_empleado_editar.getSeleccionados();
    		
    		 if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
		 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
		 			tab_tabla.setCondicion("ide_ashme=-1");
		 			tab_tabla.ejecutarSql();
		 			utilitario.addUpdate("tab_tabla");
		 			return;
		 		}
    	    	sel_empleado_editar.cerrar();

    		//Validadcion si no existe datos
    	    	TablaGenerica tab_consultar_empleado;
    	    	
    			if(empleadoSucursal.equals("508") || area.equals("9") || tipo_perfil.equals("1")  ){
    				 tab_consultar_empleado=utilitario.consultar("select ide_ashme,ide_gtemp from asi_horario_mes_empleado where ide_gtemp in ("+empleado+")  "
    	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+")");
    	    		
    			}else {
    			     TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
    					TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
    							+ "FROM asi_jefe_inmediato   "
    							+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
    	    		tab_consultar_empleado=utilitario.consultar("select ide_ashme,ide_gtemp from asi_horario_mes_empleado where ide_gtemp in ("+empleado+")  "
    	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+") and ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
				}


    		
    		/**
    		 * Validacion si no existen datos para los parametros 
    		 */
    		if (tab_consultar_empleado.getTotalFilas()<=0) {
    			utilitario.agregarMensajeError("No existen datos para este mes", "No se ha encontrado informaci�n");	
    			return;
			}else {
	    		int dia=utilitario.getDia(utilitario.getFechaActual());
				/*for (int i = 1; i < dia+1; i++) {
					tab_tabla.getColumna("DIA"+i).setLectura(true);
					
					System.out.println(i+" "+tab_tabla.getColumna("dia"+i));
				    	    		                }*/
			     }
    		
    		
    		//Validacion si no se escogen datos
    		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
    			return;
    		}else {
    			
    		int mesAsignado=Integer.parseInt(mes);
    		int anioAsignado=Integer.parseInt(anio);
  

    		tab_tabla.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
      		tab_tabla.ejecutarSql();


    		
    		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
    		String anioEscogido=tabAnio.getValor("detalle_geani");
    		
    		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+mes+")");
    		String mesEscogido=tabMes.getValor("detalle_gemes");
    		
    		getCalendario(mes, anioEscogido);
    		tab_calendario.setLectura(true);
    		tab_calendario.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
			tab_tabla.actualizar();
    		//tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
   		utilitario.addUpdate("tab_tabla,tab_calendario");
    		}
    	} catch (NumberFormatException e) {
    		// TODO Auto-generated catch block

    	System.out.println("ERROR METODO getEmpleado()");
    	
    	}
    	
     			    }
    
    
    
	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
	//	System.out.println("SI ingresa");
	    tab_tabla.setCondicion("ide_gedep="+aut_empleados.getValor());
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_tabla");
	}
    
	
	/**
	 * mETODO PARA OBTENER EL MES CON LOS EMPLEADOS SELECCIONADOS
	 */
	public void getEmpleado(){
		
		tab_calendario.setLectura(false);
		tab_tabla.setLectura(false);
		
		empleado="";

		
		empleado=sel_empleado.getSeleccionados();
    /**
     * Validacion empleado
     */
		if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			sel_empleado.cerrar();
					      }
		

	
	try {
		
		
		
		empleado=sel_empleado.getSeleccionados()+",";
/**
 * Validacion que limpia la lista para la asignacion de horarios para nuevos empleados
 */
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
		
		/**
		 * Validacion si se quiere asignar con otros parametros
		 */
		
	/**
	 *Validacion busquedaa de empleado asignado
	 */      		   StringBuilder str_idempleado=new StringBuilder();
		//Validacion cuando quiere asignar a ese empleado y ya se encuentra asignado
	 	TablaGenerica  obtenerHorarioMesEmpleadoAsignado=this.obtenerHorarioMesEmpleado(sel_empleado.getSeleccionados(),mes,anio);
        	if (obtenerHorarioMesEmpleadoAsignado.getTotalFilas()>0) {
        		utilitario.agregarMensajeError("Empleado(s) asignado(s)", "Horario asignado solo se podr� editar"); 
        		return;
        	}
		//Validacion si se ha seleccionado copiar fila mediante la variable num_copias
		if (num_copias) {
			/*
			 * ListaEmpleado se encuentran todos los empleados seleccionados para copiar
			 * contComas me retorna el numero de ingresos 
			 * Tercer parametro obtiene la fila seleccionada para copiar
			 */
			copiarFila(listaEmpleado,contComas,tab_tabla.getValorSeleccionado(),mes,anio);
			
		}else {
			//Si se asigna un empleado o varios
		
		
		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
		
			 }else {
			
		int mesAsignado=Integer.parseInt(mes);
		int anioAsignado=Integer.parseInt(anio);
		
//En la asignacion solo se puede asignar uno a la ves
		for (int i = 0; i < contComas; i++) {
			listaAnio.add(anioAsignado);
			listaMes.add(mesAsignado);
		}
		
	

		      if (contComas>1) {
				utilitario.agregarMensajeInfo("Solo se puede asignar un empleado a la vez", "Solo se puede asignar un empleado");
			return;
		      }
		
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
		
			for (int i = 0; i < contComas; i++) {
				//Inserto al nuevo personal 
				TablaGenerica tabSucuAreaDept=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				
				tab_tabla.insertar();
				tab_tabla.setValor(i, "IDE_GTEMP", ""+listaEmpleado.get(i));
				tab_tabla.setValor(i, "IDE_GEMES", ""+mes);
				tab_tabla.setValor(i, "IDE_GEANI", ""+listaAnio.get(i));
				tab_tabla.setValor(i, "IDE_SUCURSAL",tabSucuAreaDept.getValor("IDE_SUCU").toString());
				tab_tabla.setValor(i, "ide_GEARE",tabSucuAreaDept.getValor("IDE_GEARE").toString());
				tab_tabla.setValor(i, "ide_GEDEP",tabSucuAreaDept.getValor("IDE_GEDEP").toString());
		        tab_tabla.setValor(i,"IDE_GEEDP", ide_geedp_activo);
		        
				TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");

				TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
						+ "FROM asi_jefe_inmediato   "
						+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
				tab_tabla.setValor(i,"ide_asjei", tabJefeInmediato.getValor("ide_asjei"));
				tab_tabla.setValor(i,"DOCUMENTO_IDENTIDAD_GTEMP", tabSucuAreaDept.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));

		        
			}

		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
		String anioEscogido=tabAnio.getValor("detalle_geani");
		
		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
		String mesEscogido=tabMes.getValor("detalle_gemes");
		
		getCalendario(mes, anioEscogido);
		
		
		
		tab_calendario.setLectura(true);
		tab_calendario.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
		tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
		utilitario.addUpdate("tab_tabla,tab_calendario,sel_empleado");
		


		}
		
		}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block

	System.out.println("ERROR METODO getEmpleado()");
	
	}
	
	
 			

}
	

	
	
	
	
	public void getDepartamento(){
		
		copirDepartamento=true;
		empleado="";
    /**
     * Validacion empleado
     */
		
		
		
		
		
		
		StringBuilder str_ide=new StringBuilder();
        int int_num_col_idegedep=sel_departamento_empleado.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
        for (Fila filaActual:sel_departamento_empleado.getTab_seleccion().getSeleccionados()) {
     	   
                if(str_ide.toString().isEmpty()==false){
                        str_ide.append(",");
                }
                str_ide.append(filaActual.getCampos()[int_num_col_idegedep]);
        			}

        	TablaGenerica tabEmpleadosDeparatamento=getEmpleadosDepartamento(str_ide.toString());
        
         
        	
        	StringBuilder str_ide_empleado=new StringBuilder();
         String int_num_col_idegetemp;
         for (int i = 0; i < tabEmpleadosDeparatamento.getTotalFilas(); i++) {
				int_num_col_idegetemp=tabEmpleadosDeparatamento.getValor(i, "IDE_GTEMP");
         	  	if(str_ide_empleado.toString().isEmpty()==false){
                         str_ide_empleado.append(",");
                 }
                 str_ide_empleado.append(int_num_col_idegetemp);
         }
         


 		empleado=str_ide_empleado.toString();

 		empleado1=str_ide_empleado.toString();

		
		
		
		if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			sel_empleado.cerrar();
					      }
		

	
		
		
		empleado=empleado+",";
/**
 * Validacion que limpia la lista para la asignacion de horarios para nuevos empleados
 */
		int contenido=listaEmpleado.size();
/**
 * Inicializo nuevamente la lista
 */
		
			if (contenido>0) {
				listaEmpleado.removeAll(listaEmpleado);
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
		
			copiarFila(listaEmpleado,contComas,tab_tabla.getValorSeleccionado(),mes,anio);
		   
			
		
		
		TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' ");	
		int anio=Integer.parseInt(tab_anio.getValor("IDE_GEANI"));
		int mesAsignado=utilitario.getMes(utilitario.getFechaActual());
		int anioAsignado=utilitario.getAnio(utilitario.getFechaActual());
		for (int i = 0; i < contComas; i++) {
			listaAnio.add(anioAsignado);
			listaMes.add(mesAsignado);
		}
		
			
		TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
		String anioEscogido=tabAnio.getValor("detalle_geani");
		
		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mesAsignado);
		String mesEscogido=tabMes.getValor("detalle_gemes");
		
		getCalendario(""+mesAsignado, anioEscogido);
		
		
		
		tab_calendario.setLectura(false);
		tab_calendario.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
	    tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
      	//sel_departamento_empleado.cerrar();

		utilitario.addUpdate("tab_calendario,sel_departamento_empleado");
		
	
			
 			

}

	
		
	
	
	
	
	/**
	 * 
	 * @param mes caido
	 * @param anio
	 */
	public void  getCalendario(String mes,String anio){
		tab_calendario.setLectura(false);
		int diaInicio=1;
		int diaFin=0;
		String fecha=anio+"-"+mes+"-01";
		String dia_final=utilitario.getUltimoDiaMesFecha(fecha);
        diaFin=utilitario.getDia(dia_final);
		int ani=Integer.parseInt(anio);
		
		

		int dias=0;
		int diasSinValor=31-diaFin;
		int suma=0;
		tab_calendario.setLectura(false);
		String valorSinDato="";
		if ((31-diaFin)>0) {
			valorSinDato="SIN DATO";
			suma=31-diaFin;
		}
		//tab_calendario.limpiar();
		int valorNuevo=diaFin+1;
		for (int i = 1; i < (valorNuevo); i++) {
			
			
			String diaSemanaPalabra=diaSemana(i, Integer.parseInt(mes), ani);
			
			
			tab_calendario.setValor("dia"+i, diaSemanaPalabra);
	
		}
		
		TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+mes);
		String mesEscogido=tabMes.getValor("detalle_gemes");		
		tab_calendario.setValor("anio",anio );
		tab_calendario.setValor("mes",mesEscogido);
		//utilitario.addUpdate("tab_calendario");
		
	}
    		
    
	
	
	
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
      //  TablaGenerica tabJefeAsignado=this.obtenerCambioTurnoDia();

        if (tab_tabla.guardar()) {
    		
        	
        	
        	//guardarPantalla();
      
    		
    		/*for (int j = 0; j < tab.getTotalFilas(); j++) {
    			utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null where ide_gtemp in("+tab.getValor(j, "IDE_GTEMP")+")");
                
    		}
            if (copirDepartamento) {
    			utilitario.getConexion().ejecutarSql("update gth_empleado set ide_astur=null where ide_gtemp in("+empleado1+")");

    		}*/
            
      
  
            tab_tabla.setLectura(true);
            
            
            

        }
       // tab_tabla.actualizar();
		guardarPantalla();
		TablaGenerica tab=utilitario.consultar("select ide_ashme,ide_gtemp,ide_geedp from asi_horario_mes_empleado where ide_gemes in("+mes+") and ide_geani in("+anio+")");

		
		/*if (tab_tabla.getTotalFilas()>0) {
			for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
				String extra=tab_tabla.getValor(i, "num_extra_ashem");
				String suple=tab_tabla.getValor(i, "num_suple_ashem");
				
				if(extra==null || extra.equals("")|| extra.isEmpty())
				{
				
			}
			
			
		}
		}*/
		
           if (bandEdit) {
       		for (int j = 0; j < tab_tabla.getTotalFilas(); j++) {
        		tab_tabla.setLectura(false);
        		tab_tabla.getValor(j,"IDE_GTEMP");
       			if (tab.getValor(j, "IDE_GTEMP")!=null || !tab.getValor(j, "IDE_GTEMP").isEmpty()) {
       			utilitario.getConexion().ejecutarSql("update asi_horario_mes_empleado set ide_geedp_cambio ="+ide_geedp_activo+" where ide_gtemp in("+tab_tabla.getValor(j, "IDE_GTEMP")+") "
       					+ "and ide_gemes="+mes);
       			}
			}
		}
  
        utilitario.addUpdate("tab_tabla");
        
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }






	public Tabla getTab_tabla() {
		return tab_tabla;
	}



	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}



	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}



	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}



	public SeleccionTabla getSel_mes() {
		return sel_mes;
	}

	public void setSel_mes(SeleccionTabla sel_mes) {
		this.sel_mes = sel_mes;
	}



	public SeleccionTabla getSel_empleado() {
		return sel_empleado;
	}



	public SeleccionTabla getSel_empleado_editar() {
		return sel_empleado_editar;
	}



	public void setSel_empleado_editar(SeleccionTabla sel_empleado_editar) {
		this.sel_empleado_editar = sel_empleado_editar;
	}



	public void setSel_empleado(SeleccionTabla sel_empleado) {
		this.sel_empleado = sel_empleado;
	}



	public SeleccionTabla getSel_anio() {
		return sel_anio;
	}



	public void setSel_anio(SeleccionTabla sel_anio) {
		this.sel_anio = sel_anio;
	}





	public String getAnio() {
		return anio;
	}



	public void setAnio(String anio) {
		this.anio = anio;
	}



	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}



	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}



	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
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
	    
	  
	    
	    /**
	     * 
	     * @param dia
	     * @param mes
	     * @param anioEscogido
	     * @return
	     */
	    public String diaSemana (int dia, int mes, int anioEscogido)
	    {
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



		public Tabla getTab_calendario() {
			return tab_calendario;
		}



		public void setTab_calendario(Tabla tab_calendario) {
			this.tab_calendario = tab_calendario;
		}
	    
		public static String dateMonth(Calendar date){
			 String result="";
			 int month=0;
			 
			 try{
			   month=date.get(Calendar.MONTH);
			 }catch(Exception ex){}
			 switch(month){
			  case 0:
			    {
			      result="Enero";
			      break;
			    }
			  case 1:
			    {
			      result="Febrero";
			      break;
			    }
			  case 2:
			    {
			      result="Marzo";
			      break;
			    }
			  case 3:
			    {
			      result="Abril";
			      break;
			    }
			  case 4:
			    {
			      result="Mayo";
			      break;
			    }
			  case 5:
			    {
			      result="Junio";
			      break;
			    }
			  case 6:
			    {
			      result="Julio";
			      break;
			    }
			  case 7:
			    {
			      result="Agosto";
			      break;
			    }
			  case 8:
			    {
			      result="Septiembre";
			      break;
			    }
			  case 9:
			    {
			      result="Octubre";
			      break;
			    }
			  case 10:
			    {
			      result="Noviembre";
			      break;
			    }
			  case 11:
			    {
			      result="Diciembre";
			      break;
			    }
			  default:
			    {
			      result="Error";
			      break;
			    }
			 }
			 return result;
			}
		
		
		/**
		 * limpia toda la pantalla incluyendo el autocompletar
		 */
		public void limpiar() {
			//Consulta empleado loggeado
			TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

			//Consulta de empleado por sucursal
			
			TablaGenerica tabSucursal=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.IDE_SUCU,SUCU.NOM_SUCU, AREA.IDE_GEARE,AREA.DETALLE_GEARE,  "
					+ "DEPA.IDE_GEDEP,DEPA.DETALLE_GEDEP   "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "where epar.ide_geedp="+ide_geedp_activo
					+ " ORDER BY DETALLE_GEARE ASC");
			
			sucu=tabSucursal.getValor("IDE_SUCU");
			tab_tabla.setCondicion("ide_ashme=-1");
			tab_tabla.ejecutarSql();
		    getCalendario("11",""+utilitario.getAnio(utilitario.getFechaActual()));
			utilitario.addUpdate("tab_tabla,tab_calendario,aut_empleados");// limpia y refresca el autocompletar
		}		
	    
		
		
	public void	verFechaMes(AjaxBehaviorEvent evt){
			tab_tabla.modificar(evt);
	
			try {
				String mes=tab_tabla.getValor("IDE_GEMES");
				String anio=tab_tabla.getValor("IDE_GEANI");

				TablaGenerica tabAnio = utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
				String anioEscogido=tabAnio.getValor("detalle_geani");
				
				TablaGenerica tabMes = utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes in("+mes+")");
				String mesEscogido=tabMes.getValor("detalle_gemes");
				
				getCalendario(mes, anioEscogido);
				tab_calendario.setLectura(true);
			    tab_calendario.setHeader("TABLA FECHAS DEL MES "+mesEscogido+"  DEL "+anioEscogido);
			    tab_tabla.setHeader("TABLA HORARIO POR EMPLEADO DE "+mesEscogido+"  DEL "+anioEscogido);
				utilitario.addUpdate("tab_calendario,tab_tabla");
			} catch (Exception e) {
				// TODO: handle exception
			}
		

	}

	
public void	editarPorMes(){
	
	/*if (sucu.equals("1") && !empleadoSucursal.equals("513") ) {

		utilitario.agregarMensajeInfo("Empleado no puede realizar esta acci�n", "Por favor contactese con Talento Humano");
		return;
	}*/
	
	
	tab_tabla.setCondicion("ide_ashme=-1");
	tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");

bandEdit=true;
	sel_mes_editar.dibujar();
}


	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}



	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}



	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}



	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}



	@Override
	public void buscar() {
		// TODO Auto-generated method stub
		super.buscar();
	}



	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
	}




	public Boolean getEditar() {
		return editar;
	}



	public void setEditar(Boolean editar) {
		this.editar = editar;
	}









	public Dialogo getDia_num_copiar() {
		return dia_num_copiar;
	}



	public void setDia_num_copiar(Dialogo dia_num_copiar) {
		this.dia_num_copiar = dia_num_copiar;
	}
	
	
		
    private TablaGenerica obtenerHorarioMesEmpleado(String empleado,String mes,String anio){
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select ide_ashme,ide_gtemp from asi_horario_mes_empleado "
    			+ "where ide_gemes="+mes+" and ide_geani in("+anio+") and ide_gtemp in("+empleado+")");
    	return horarioXEmpleado;
    }



	public SeleccionTabla getSel_mes_editar() {
		return sel_mes_editar;
	}



	public void setSel_mes_editar(SeleccionTabla sel_mes_editar) {
		this.sel_mes_editar = sel_mes_editar;
	}



	public SeleccionTabla getSel_anio_editar() {
		return sel_anio_editar;
	}



	public void setSel_anio_editar(SeleccionTabla sel_anio_editar) {
		this.sel_anio_editar = sel_anio_editar;
	}
	
    
    public void buscarHorario(){
    	
    	String estado=tab_tabla2.getValor("ACTIVO_ASHOR");
    	
    	if (estado.equals("false")) {
			TablaGenerica tabHorario=utilitario.consultar("select emp.ide_gtemp,emp.ide_astur from gth_empleado emp "
					+ "left join asi_turnos_horario tuh on tuh.ide_astur=emp.ide_astur "
					+ "left join asi_horario hor on hor.ide_ashor=tuh.ide_ashor "
					+ "where tuh.ide_ashor="+tab_tabla2.getValorSeleccionado());
		
    	if (tabHorario.getTotalFilas()>0) {
			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=true where ide_ashor="+tab_tabla2.getValorSeleccionado());
			tab_tabla2.actualizar();
			utilitario.agregarMensajeError("No se puede desactivar ", "Hay empleados asociados con este horario");
			return;
		}
    	else {
			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=false where ide_ashor="+tab_tabla2.getValorSeleccionado());
			tab_tabla2.actualizar();
			utilitario.agregarMensajeError("Se ha  desactivado el horario", "Cambio realizado correcto");

		}
    	}else {			
    		
    			TablaGenerica tabHorario=utilitario.consultar("select emp.ide_gtemp,emp.ide_astur from gth_empleado emp "
    					+ "left join asi_turnos_horario tuh on tuh.ide_astur=emp.ide_astur "
    					+ "left join asi_horario hor on hor.ide_ashor=tuh.ide_ashor "
    					+ "where tuh.ide_ashor="+tab_tabla2.getValorSeleccionado());
    		
        	if (tabHorario.getTotalFilas()==0) {
    			utilitario.getConexion().ejecutarSql("update asi_horario set activo_ashor=true where ide_ashor="+tab_tabla2.getValorSeleccionado());
    			tab_tabla2.actualizar();
    			utilitario.agregarMensaje("Se ha activado correctamente ", "Horario sin asignaci�n de horarios");
    			return;
    		}
        
    		
    		
		}
    	
    }
    
    
    private TablaGenerica obtenerNombreEmpleado(String empleado){
    	TablaGenerica nombreEmpleado=utilitario.consultar("SELECT EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
    			+ "FROM  GTH_EMPLEADO EMP  "
    			+ "WHERE EMP.ACTIVO_GTEMP=true and emp.ide_gtemp in("+empleado+")");
    	return nombreEmpleado;
    }



	public Tabla getTab_turnos() {
		return tab_turnos;
	}



	public void setTab_turnos(Tabla tab_turnos) {
		this.tab_turnos = tab_turnos;
	}



	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}



	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
    
	
	public void cargarHorario(SelectEvent evt){
		tab_tabla2.seleccionarFila(evt);
		tab_tabla2.getValorSeleccionado();
		
		if (tab_tabla2.getValorSeleccionado()==null || tab_tabla2.getValorSeleccionado().isEmpty()) {
			utilitario.agregarMensajeInfo("No se ha seleccionado turno", "Debe seleccionar un turno");
			return;
		}
		TablaGenerica tab_horario=utilitario.consultar("select ide_astur,ide_ashor from asi_turnos_horario where ide_astur="+tab_tabla2.getValorSeleccionado());
	
		if (tab_horario.getTotalFilas()<=0 || tab_horario.isEmpty()) {
			utilitario.agregarMensajeInfo("No se han encontrado horarios asignados", "Debe asignar un turno al o los horario(s)");
			return;
		}	
		
		
		int valor=0;
		String horario="";
		
		   StringBuilder str_ide=new StringBuilder();
           for (int i = 0; i < tab_horario.getTotalFilas(); i++) 
			 {
        	  
              str_ide.append(tab_horario.getValor(i, "IDE_ASHOR"));
              valor++;
              if (tab_horario.getTotalFilas()==1) {
			}else if (valor<=tab_horario.getTotalFilas()) {
                  str_ide.append(",");
                  valor=valor+1;
  			}

			 }

           
           

		
		tab_tabla3.setCondicion("ide_ashor in("+str_ide.toString()+")");
		tab_tabla3.ejecutarSql();
		tab_tabla3.actualizar();
	
	}

	
	
    public void consultarDepartamento(){
		
	//	if (sucu.equals("1")) {
			utilitario.agregarMensajeInfo("Empleado no puede realizar esta acci�n", "Por favor contactese con el Administrador");
			return;
		//}
      	//sel_departamento_empleado.dibujar();
      	
      	
}



	public SeleccionTabla getSel_departamento_empleado() {
		return sel_departamento_empleado;
	}



	public void setSel_departamento_empleado(
			SeleccionTabla sel_departamento_empleado) {
		this.sel_departamento_empleado = sel_departamento_empleado;
	}
    
    
    
	 public void aceptarAsignarEmpleadoDepartamento(){
	    	
	    	
  	   StringBuilder str_ide=new StringBuilder();
         int int_num_col_idegedep=sel_departamento_empleado.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
         for (Fila filaActual:sel_departamento_empleado.getTab_seleccion().getSeleccionados()) {
      	   
                 if(str_ide.toString().isEmpty()==false){
                         str_ide.append(",");
                 }
                 str_ide.append(filaActual.getCampos()[int_num_col_idegedep]);
         			}

         	TablaGenerica tabEmpleadosDeparatamento=getEmpleadosDepartamento(str_ide.toString());
         
          
         	
         	StringBuilder str_ide_empleado=new StringBuilder();
          String int_num_col_idegetemp;
          for (int i = 0; i < tabEmpleadosDeparatamento.getTotalFilas(); i++) {
				int_num_col_idegetemp=tabEmpleadosDeparatamento.getValor(i, "IDE_GTEMP");
          	  	if(str_ide_empleado.toString().isEmpty()==false){
                          str_ide_empleado.append(",");
                  }
                  str_ide_empleado.append(int_num_col_idegetemp);
          }
          
          
             
         
         		utilitario.agregarMensajeInfo("Se han Actualizado correctamente los empleados seleccionados", "");
  	
  	
  }

	
	  	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
	  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
	  	}



		public Boolean getBandEdit() {
			return bandEdit;
		}



		public void setBandEdit(Boolean bandEdit) {
			this.bandEdit = bandEdit;
		}



		public String getSucu() {
			return sucu;
		}



		public void setSucu(String sucu) {
			this.sucu = sucu;
		}



		public String getArea() {
			return area;
		}



		public void setArea(String area) {
			this.area = area;
		}



		public String getDepa() {
			return depa;
		}



		public void setDepa(String depa) {
			this.depa = depa;
		}



		public boolean isCopirDepartamento() {
			return copirDepartamento;
		}



		public void setCopirDepartamento(boolean copirDepartamento) {
			this.copirDepartamento = copirDepartamento;
		}



		public String getEmpleado1() {
			return empleado1;
		}



		public void setEmpleado1(String empleado1) {
			this.empleado1 = empleado1;
		}



		public String getSucursal() {
			return sucursal;
		}



		public void setSucursal(String sucursal) {
			this.sucursal = sucursal;
		}



		public AutoCompletar getAut_empleados() {
			return aut_empleados;
		}



		public void setAut_empleados(AutoCompletar aut_empleados) {
			this.aut_empleados = aut_empleados;
		}
		
	    
	    private TablaGenerica obtenerCambioTurnoDia(Integer ide_getemp){
	    	TablaGenerica horarioXEmpleado=utilitario.consultar("select EMP.IDE_GTEMP, "
	    			+ "horario.ide_ashor, "
	    			+ "HORARIO.HORA_INICIAL_ASHOR, "
	    			+ "HORARIO.HORA_FINAL_ASHOR, "
	    			+ "HORARIO.HORA_INICIO_ALMUERZO_ASHOR, "
	    			+ "HORARIO.HORA_FIN_ALMUERZO_ASHOR, "
	    			+ "HORARIO.MIN_ALMUERZO_ASHOR, "
	    			+ "HORARIO.NOM_ASHOR "
	    			+ "from gth_empleado emp "
	    			+ "left join asi_turnos_horario turnos on turnos.ide_astur=emp.ide_astur "
	    			+ "left join asi_horario horario  on horario.ide_ashor=turnos.ide_ashor "
	    			+ "where emp.ide_astur is not null  AND EMP.IDE_GTEMP="+ide_getemp
	    			+ " ORDER BY HORARIO.IDE_ASHOR ASC");
	    	return horarioXEmpleado;
	    }
	    
	  	
	    public int retornarDiasMes(int dia, int mes,int anio){

	    Calendar cal = new GregorianCalendar(anio, mes-1, 1); 
	    	// Get the number of days in that month 
	    	int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
	    	//System.out.println(days);
	    	return days;
	    	}

	    
	    
	    
	    
	    
	    public void validarFinMes(AjaxBehaviorEvent evt){
	    	
	    	
	        int ultimo_dia=0;
		   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");

		       ultimo_dia=utilitario.getDia(utilitario.getUltimaFechaMes(tab_anio.getValor("detalle_geani")+"-"+mes+"-01"));
			
	    	if (ultimo_dia==30)  { 			
	    			utilitario.agregarMensajeInfo("No se puede asignar el valor", "La planificacion no se puede modificar");
	    			tab_tabla.setValor("dia31","");
	    			utilitario.addUpdateTabla(tab_tabla, "dia31","");
	    			return;
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
	      			
	      			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
	      			String sql="";
	      			 if (tipo_perfil.equals("1")) {    				
	      				
	                				 
	      				sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
	      						+ "from  GTH_EMPLEADO EMP  "
	      						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")   "
	      						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
	      						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	      						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
	      						
	      						
	      						/*+ "select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
	  							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
	  							+ "emp.documento_identidad_gtemp  "
	  							+ "	 from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
	  							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
	  							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
	  							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
	  							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
	  							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
	  							+ " and emp.ide_gtemp in("+str_ide_empleado_mensual+") "  
	  							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
	  							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	  							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/
	          		
	  				}else {
	  					
	  					sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
	      						+ "from  GTH_EMPLEADO EMP  "
	      						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")   "
	      						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
	      						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	      						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
	      						
	  					
	  					
	  							/*sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
	  							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
	  							+ "emp.documento_identidad_gtemp  "
	  							+ "	 from  GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
	  							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
	  							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
	  							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
	  							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
	  							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE   "
	  							+ " and emp.ide_gtemp in("+str_ide_empleado_mensual+") "  
	  							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
	  							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
	  							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";*/

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



		public SeleccionTabla getSel_empleado_importarTotales() {
			return sel_empleado_importarTotales;
		}



		public void setSel_empleado_importarTotales(
				SeleccionTabla sel_empleado_importarTotales) {
			this.sel_empleado_importarTotales = sel_empleado_importarTotales;
		}



		public Upload getUpl_archivo() {
			return upl_archivo;
		}



		public void setUpl_archivo(Upload upl_archivo) {
			this.upl_archivo = upl_archivo;
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
		}  
	    
	 
		
		

/**
 * Valida el archivo para que pueda importar un rubro a la nomina
 * 
 * @param evt
 */
public void validarArchivoTotal(FileUploadEvent evt) {
	
	
	
	String fecIn="",fecOut="";
	
	TablaGenerica tabMes=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anio);
	if (Integer.parseInt(mes)<10) {
		fecIn=tabMes.getValor("detalle_geani")+"-0"+mes+"-01";
		fecIn=utilitario.DeDateAString(sumarDiasFechaSinFinSemana(utilitario.DeStringADate(fecIn), -5));
		fecOut=tabMes.getValor("detalle_geani")+"-0"+mes+"-"+utilitario.getVariable("p_dias_fin_registro_asignacion_mensual");

	}else {
		fecIn=tabMes.getValor("detalle_geani")+"-"+mes+"-01";
		fecIn=utilitario.DeDateAString(sumarDiasFechaSinFinSemana(utilitario.DeStringADate(fecIn), 5));
		fecOut=tabMes.getValor("detalle_geani")+"-"+mes+"-"+utilitario.getVariable("p_dias_fin_registro_asignacion_mensual");
	}
	
	
	
	if (utilitario.getFechaActual().compareTo(fecIn)>=0 && utilitario.getFechaActual().compareTo(fecOut)<=0) {
		
	}else {
		utilitario.agregarMensajeError("Rango de fechas invalido", "Fecha habilitadas para registro de horario "+fecIn+" y  "+fecOut);
		return;
	}
	
	
	
	
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
			ide_empleados_crear=new StringBuilder();
			upl_archivo.setNombreReal(evt.getFile().getFileName());
			str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
			
			lis_importa = new ArrayList<String[]>();
			lis_importa60 = new ArrayList<String[]>();
			lis_importa25 = new ArrayList<String[]>();
			lis_importa50 = new ArrayList<String[]>();
			lis_importa100 = new ArrayList<String[]>();
			
			lis_importadia1 = new ArrayList<String[]>();
			lis_importadia2 = new ArrayList<String[]>();
			lis_importadia3 = new ArrayList<String[]>();
			lis_importadia4 = new ArrayList<String[]>();
			lis_importadia5 = new ArrayList<String[]>();

			lis_importadia6 = new ArrayList<String[]>();
			lis_importadia7 = new ArrayList<String[]>();
			lis_importadia8 = new ArrayList<String[]>();
			lis_importadia9 = new ArrayList<String[]>();
			lis_importadia10 = new ArrayList<String[]>();
			
			lis_importadia11 = new ArrayList<String[]>();
			lis_importadia12 = new ArrayList<String[]>();
			lis_importadia13 = new ArrayList<String[]>();
			lis_importadia14 = new ArrayList<String[]>();
			lis_importadia15 = new ArrayList<String[]>();
			
			lis_importadia16 = new ArrayList<String[]>();
			lis_importadia17 = new ArrayList<String[]>();
			lis_importadia18 = new ArrayList<String[]>();
			lis_importadia19 = new ArrayList<String[]>();
			lis_importadia20 = new ArrayList<String[]>();
			
			lis_importadia21 = new ArrayList<String[]>();
			lis_importadia22 = new ArrayList<String[]>();
			lis_importadia23 = new ArrayList<String[]>();
			lis_importadia24 = new ArrayList<String[]>();
			lis_importadia25 = new ArrayList<String[]>();
			
			lis_importadia26 = new ArrayList<String[]>();
			lis_importadia27 = new ArrayList<String[]>();
			lis_importadia28 = new ArrayList<String[]>();
			lis_importadia29 = new ArrayList<String[]>();
			lis_importadia30 = new ArrayList<String[]>();
			lis_importadia31 = new ArrayList<String[]>();

			
			
			
			
			tab_emp_total.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , "
							  		+ "0 as DIA1, "
									+ "0 as DIA2, "
									+ "0 as DIA3, "
									+ "0 as DIA4, "
									+ "0 as DIA5, "
									+ "0 as DIA6, "
									+ "0 as DIA7, "
									+ "0 as DIA8, "	 
									+ "0 as DIA9, "
									+ "0 as DIA10, "
									+ "0 as DIA11, "
									+ "0 as DIA12, "
									+ "0 as DIA13, "
									+ "0 as DIA14, "
									+ "0 as DIA15, "
									+ "0 as DIA16, "
									+ "0 as DIA17, "
									+ "0 as DIA18, "
									+ "0 as DIA19, "
									+ "0 as DIA20, "
									+ "0 as DIA21, "
									+ "0 as DIA22, "
									+ "0 as DIA23, "
									+ "0 as DIA24, "
									+ "0 as DIA25, "
									+ "0 as DIA26, "
									+ "0 as DIA27, "
									+ "0 as DIA28, "
									+ "0 as DIA29, "
									+ "0 as DIA30, "
									+ "0 as DIA31 "
									+ "FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");	
			
			tab_emp_total.ejecutarSql();
			tab_emp_total.setLectura(false);
			tab_emp_total.setDibujo(false);
			int ide_gtemp=0;
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
			int x=0;
			double dou_valor = 0;
			TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
			int nuevoMes=0;
			nuevoMes=Integer.parseInt(mes);
			String fecha="";
			if (Integer.parseInt(mes)>0) {
				fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-01";
			}else {
				fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-01";
			}

			ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
			
			
			for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				String str_cedula = hoja.getCell(0, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				//TablaGenerica tab_empleado = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
				
				//Si existe empleados
				
				TablaGenerica tab_empleado =utilitario.consultar("select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  GTH_EMPLEADO EMP  "
  						+ " left join asi_empleado_jefe_inmediato ashem on emp.ide_gtemp=ashem.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+empleado+")  and  emp.documento_identidad_gtemp='"+str_cedula+"' and ashem.activo_emjei=true "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
  					//	tab_empleado.imprimirSql();
				 
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos de horarios, fila " + (i + 1));
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
									+ "FROM con_biometrico_plan_hxe ");
								//	+ "where ide_gtemp="+ide_gtemp+" and ide_geani="+anio+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")");
							if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
							//	str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
							}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene partida, fila " + (i + 1));
							}				
					}//validacion documento de identidad
					
					//if (int_fin==(i-1)) {
					//	ide_empleados_crear.append(ide_gtemp);	
					//}else {
						ide_empleados_crear.append(ide_gtemp+",");
					//}
					
					
					
				}// tab_emp
				
				
				
				
				
				for (x = 1; x <= ultimoDia; x++) {		


				
				
				
				String str_valor = hoja.getCell(x, i).getContents();
				str_valor = str_valor.replaceAll(",", ".");
				if (str_valor == null || str_valor.equals("")) {
					str_valor = "0";
				}
				dou_valor = 0.00;
				try {
					// Valida que sea una cantidad numerica
					dou_valor = Integer.parseInt(str_valor);
					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor numerico no valido , fila " + (i + 1));
				}
				//
				//TablaGenerica tab_emp;
			//	Object[] obj_cumula = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula60 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula25 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula50 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula100 = getAcumuladoTotal(str_cedula,x);
				
				if (x==1) {
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

					
					lis_importadia1.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA1", utilitario.getFormatoNumero(dou_valor));
				// Acumula el valor
				}else if (x==2) {
					lis_importadia2.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA2", utilitario.getFormatoNumero(dou_valor));
				}else if (x==3) {
					lis_importadia3.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA3", utilitario.getFormatoNumero(dou_valor));
				}else if (x==4) {
					lis_importadia4.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA4", utilitario.getFormatoNumero(dou_valor));
				}else if (x==5) {
					lis_importadia5.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA5", utilitario.getFormatoNumero(dou_valor));
				}else if (x==6) {
					lis_importadia6.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA6", utilitario.getFormatoNumero(dou_valor));
				}else if (x==7) {
					lis_importadia7.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA7", utilitario.getFormatoNumero(dou_valor));
				}else if (x==8) {
					lis_importadia8.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA8", utilitario.getFormatoNumero(dou_valor));
				}else if (x==9) {
					lis_importadia9.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA9", utilitario.getFormatoNumero(dou_valor));
				}else if (x==10) {
					lis_importadia10.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA10", utilitario.getFormatoNumero(dou_valor));
				}else if (x==11) {
					lis_importadia11.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA11", utilitario.getFormatoNumero(dou_valor));
				}else if (x==12) {
					lis_importadia12.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA12", utilitario.getFormatoNumero(dou_valor));
				}else if (x==13) {
					lis_importadia13.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor("DIA13", utilitario.getFormatoNumero(dou_valor));
				}else if (x==14) {
					lis_importadia14.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
			  		 tab_emp_total.setValor("DIA14", utilitario.getFormatoNumero(dou_valor));
				}else if (x==15) {
					lis_importadia15.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA15", utilitario.getFormatoNumero(dou_valor));
				}else if (x==16) {
					lis_importadia16.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA16", utilitario.getFormatoNumero(dou_valor));
				}else if (x==17) {
					lis_importadia17.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA17", utilitario.getFormatoNumero(dou_valor));
				}else if (x==18) {
					lis_importadia18.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA18", utilitario.getFormatoNumero(dou_valor));
				}else if (x==19) {
					lis_importadia19.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA19", utilitario.getFormatoNumero(dou_valor));
				}else if (x==20) {
					lis_importadia20.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA20", utilitario.getFormatoNumero(dou_valor));
				}else if (x==21) {
					lis_importadia21.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA21", utilitario.getFormatoNumero(dou_valor));
				}else if (x==22) {
					lis_importadia22.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA22", utilitario.getFormatoNumero(dou_valor));
				}else if (x==23) {
					lis_importadia23.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA23", utilitario.getFormatoNumero(dou_valor));
				}else if (x==24) {
					lis_importadia24.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA24", utilitario.getFormatoNumero(dou_valor));
					eti_dia24.setValue(dou_tot_valor_imp60);	
				}else if (x==25) {
					lis_importadia25.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA25", utilitario.getFormatoNumero(dou_valor));
				}else if (x==26) {
					lis_importadia26.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA26", utilitario.getFormatoNumero(dou_valor));
				}else if (x==27) {
					lis_importadia27.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA27", utilitario.getFormatoNumero(dou_valor));
				}else if (x==28 && ultimoDia>=28) {
					lis_importadia28.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA28", utilitario.getFormatoNumero(dou_valor));
				}else if (x==29 && ultimoDia>=29) {
					lis_importadia29.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA29", utilitario.getFormatoNumero(dou_valor));
				}else if (x==30 && ultimoDia>=30) {
					lis_importadia30.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA30", utilitario.getFormatoNumero(dou_valor));
				}else if (x==31 && ultimoDia>=31) {
					lis_importadia31.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					tab_emp_total.setValor(tab_emp_total.getFilaActual(),"DIA31", utilitario.getFormatoNumero(dou_valor));
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
		grid_tabla_emp_sum_totales.getChildren().clear();

		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp_total.getChildren().add(tab_emp_total);
		dia_valida_empleado_total.dibujar();
		
		
		
		
}
	


private Object[] getAcumuladoTotal(String documento,int tipo_rubro) {
	for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
		if (tab_emp_total.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP").equalsIgnoreCase(documento)) {
			Object[] obj = new Object[3];
			obj[0] = i;
			if (tipo_rubro==1) {
				obj[1] = tab_emp_total.getValor(i, "DIA1");
	
			}else if (tipo_rubro==2) {
				obj[1] = tab_emp_total.getValor(i, "DIA2");
	
			}else if (tipo_rubro==3) {
				obj[1] = tab_emp_total.getValor(i, "DIA3");

			}else if (tipo_rubro==4) {
				obj[1] = tab_emp_total.getValor(i, "DIA4");

			}else if (tipo_rubro==5) {
				obj[1] = tab_emp_total.getValor(i, "DIA5");

			}else if (tipo_rubro==6) {
				obj[1] = tab_emp_total.getValor(i, "DIA6");

			}else if (tipo_rubro==7) {
				obj[1] = tab_emp_total.getValor(i, "DIA7");

			}else if (tipo_rubro==8) {
				obj[1] = tab_emp_total.getValor(i, "DIA8");

			}else if (tipo_rubro==9) {
				obj[1] = tab_emp_total.getValor(i, "DIA9");

			}else if (tipo_rubro==10) {
				obj[1] = tab_emp_total.getValor(i, "DIA10");

			}else if (tipo_rubro==11) {
				obj[1] = tab_emp_total.getValor(i, "DIA11");

			}else if (tipo_rubro==12) {
				obj[1] = tab_emp_total.getValor(i, "DIA12");

			}else if (tipo_rubro==13) {
				obj[1] = tab_emp_total.getValor(i, "DIA13");

			}else if (tipo_rubro==14) {
				obj[1] = tab_emp_total.getValor(i, "DIA14");

			}else if (tipo_rubro==15) {
				obj[1] = tab_emp_total.getValor(i, "DIA15");

			}else if (tipo_rubro==16) {
				obj[1] = tab_emp_total.getValor(i, "DIA16");

			}else if (tipo_rubro==17) {
				obj[1] = tab_emp_total.getValor(i, "DIA17");

			}else if (tipo_rubro==18) {
				obj[1] = tab_emp_total.getValor(i, "DIA18");

			}else if (tipo_rubro==19) {
				obj[1] = tab_emp_total.getValor(i, "DIA19");

			}else if (tipo_rubro==20) {
				obj[1] = tab_emp_total.getValor(i, "DIA20");

			}else if (tipo_rubro==21) {
				obj[1] = tab_emp_total.getValor(i, "DIA21");

			}else if (tipo_rubro==22) {
				obj[1] = tab_emp_total.getValor(i, "DIA22");

			}else if (tipo_rubro==23) {
				obj[1] = tab_emp_total.getValor(i, "DIA23");

			}else if (tipo_rubro==24) {
				obj[1] = tab_emp_total.getValor(i, "DIA24");

			}else if (tipo_rubro==25) {
				obj[1] = tab_emp_total.getValor(i, "DIA25");

			}else if (tipo_rubro==26) {
				obj[1] = tab_emp_total.getValor(i, "DIA26");

			}else if (tipo_rubro==27) {
				obj[1] = tab_emp_total.getValor(i, "DIA27");

			}else if (tipo_rubro==28) {
				obj[1] = tab_emp_total.getValor(i, "DIA28");

			}else if (tipo_rubro==29) {
				obj[1] = tab_emp_total.getValor(i, "DIA29");

			}else if (tipo_rubro==30) {
				obj[1] = tab_emp_total.getValor(i, "DIA30");

			}else if (tipo_rubro==31) {
				obj[1] = tab_emp_total.getValor(i, "DIA31");

			}

			
			
			
			
			if (tipo_rubro==1) {

			for (int k = 0; k < lis_importadia1.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			if (tipo_rubro==2) {
			for (int k = 0; k < lis_importadia2.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			
			if (tipo_rubro==3) {
			for (int k = 0; k < lis_importadia3.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
			}
			}
			}

			if (tipo_rubro==4) {
			for (int k = 0; k < lis_importadia4.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[2] = k;
			break;
				}
			}
			}
			
			if (tipo_rubro==5) {
				for (int k = 0; k < lis_importadia5.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==6) {
				for (int k = 0; k < lis_importadia6.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==7) {
				for (int k = 0; k < lis_importadia7.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==8) {
				for (int k = 0; k < lis_importadia8.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==9) {
				for (int k = 0; k < lis_importadia9.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==10) {
				for (int k = 0; k < lis_importadia10.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==11) {
				for (int k = 0; k < lis_importadia11.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==12) {
				for (int k = 0; k < lis_importadia12.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==13) {
				for (int k = 0; k < lis_importadia13.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==14) {
				for (int k = 0; k < lis_importadia14.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==15) {
				for (int k = 0; k < lis_importadia15.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==16) {
				for (int k = 0; k < lis_importadia16.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==17) {
				for (int k = 0; k < lis_importadia17.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==18) {
				for (int k = 0; k < lis_importadia18.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==19) {
				for (int k = 0; k < lis_importadia19.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==20) {
				for (int k = 0; k < lis_importadia20.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			
			if (tipo_rubro==21) {
				for (int k = 0; k < lis_importadia21.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==22) {
				for (int k = 0; k < lis_importadia22.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==23) {
				for (int k = 0; k < lis_importadia23.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==24) {
				for (int k = 0; k < lis_importadia24.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==25) {
				for (int k = 0; k < lis_importadia25.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			if (tipo_rubro==26) {
				for (int k = 0; k < lis_importadia26.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==27) {
				for (int k = 0; k < lis_importadia27.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==28) {
				for (int k = 0; k < lis_importadia28.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			if (tipo_rubro==29) {
				for (int k = 0; k < lis_importadia29.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			
			if (tipo_rubro==30) {
				for (int k = 0; k < lis_importadia30.size(); k++) {
					// busco posicion en la lista
					if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
						obj[2] = k;
				break;
					}
				}
				}
			
			
			if (tipo_rubro==31) {
				for (int k = 0; k < lis_importadia31.size(); k++) {
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



private String getFormatoInformacion(String mensaje) {
	return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
}

private String getFormatoError(String mensaje) {
	return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
}
private String getFormatoAdvertencia(String mensaje) {
	return "<div><font color='#ffcc33'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
}



public boolean importarValoresRubro(List lis_importa,int tipo_hora_extra){

	String str_sql="";
	str_sql="SELECT cbph.ide_ashme,  EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   anio.detalle_geani,mes.detalle_gemes,cbph.ide_asjei  "
			+ "FROM asi_horario_mes_empleado cbph  "
			+ "left join gth_empleado emp on emp.ide_gtemp=cbph.ide_gtemp   "
			+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani  "
			+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
			+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+anio+" and cbph.ide_gtemp in("+empleado+") " 
			+ " order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP";
	
	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);
	//jefe_inmediato_planificacion
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
			ide_cobrh=tab_emp_dep.getValor(j, "ide_ashme");
			if (str_valor.equals("0") || str_valor.equals("0.0") || str_valor.equals("0.00")) {
				str_valor=null;
			}
				if (tipo_hora_extra==1) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia1="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==2) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia2="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==3) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia3="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==4) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia4="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==5) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia5="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==6) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia6="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==7) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia7="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==8) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia8="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==9) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia9="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==10) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia10="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==11) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia11="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==12) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia12="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==13) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia13="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==14) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia14="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==15) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia15="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==16) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia16="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==17) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia17="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==18) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia18="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==19) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia19="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==20) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia20="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==21) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia21="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==22) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia22="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==23) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia23="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==24) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia24="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==25) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia25="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}	else if (tipo_hora_extra==26) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia26="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==27) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia27="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==28) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia28="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==29) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia29="+str_valor+" where ide_ashme=" + ide_cobrh);				
				}else if (tipo_hora_extra==30) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia30="+str_valor+" where ide_ashme=" + ide_cobrh);				

				}else if (tipo_hora_extra==31) {
					utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado set dia31="+str_valor+" where ide_ashme=" + ide_cobrh);						
				}
		}
	}

	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;
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
	if (ide_empleados_crear.length()==0) {
		
	}else {
		

		
		
		validacionEmpleado=ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length());
		if (validacionEmpleado.equals(",")) {
			utilitario.getConexion().ejecutarSql("DELETE FROM asi_horario_mes_empleado_historial WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
		
		}else {
			utilitario.getConexion().ejecutarSql("DELETE FROM asi_horario_mes_empleado_historial WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear+")");
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear+")");					
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear+")");
		}
	}
	
	
	
	
	
	

	if (tab_empleado.getTotalFilas()>0) {
		for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
			insertarAsignacionMensualHorario(Integer.parseInt(anio), Integer.parseInt(mes), tab_empleado.getValor(i, "ide_gtemp"), ide_asjei, tab_empleado.getValor(i, "documento_identidad_gtemp"));	
		}
		
	}
	
	
	
	for (int x = 1; x <= ultimoDia; x++) {
		

if (x==1) {
	if (importarValoresRubro(lis_importadia1,x)) {
	//	utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==2) {
	if (importarValoresRubro(lis_importadia2,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==3) {
	if (importarValoresRubro(lis_importadia3,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==4) {
	if (importarValoresRubro(lis_importadia4,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==5) {
	if (importarValoresRubro(lis_importadia5,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}



if (x==6) {
	if (importarValoresRubro(lis_importadia6,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==7) {
	if (importarValoresRubro(lis_importadia7,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==8) {
	if (importarValoresRubro(lis_importadia8,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==9) {
	if (importarValoresRubro(lis_importadia9,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==10) {
	if (importarValoresRubro(lis_importadia10,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==11) {
	if (importarValoresRubro(lis_importadia11,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==12) {
	if (importarValoresRubro(lis_importadia12,x)) {
	//	utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}



if (x==13) {
	if (importarValoresRubro(lis_importadia13,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==14) {
	if (importarValoresRubro(lis_importadia14,x)) {
	//	utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==15) {
	if (importarValoresRubro(lis_importadia15,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==16) {
	if (importarValoresRubro(lis_importadia16,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==17) {
	if (importarValoresRubro(lis_importadia17,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==18) {
	if (importarValoresRubro(lis_importadia18,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}
if (x==19) {
	if (importarValoresRubro(lis_importadia19,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}




if (x==20) {
	if (importarValoresRubro(lis_importadia20,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}


if (x==21) {
	if (importarValoresRubro(lis_importadia21,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==22) {
	if (importarValoresRubro(lis_importadia22,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}



if (x==23) {
	if (importarValoresRubro(lis_importadia23,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==24) {
	if (importarValoresRubro(lis_importadia24,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}


if (x==25) {
	if (importarValoresRubro(lis_importadia25,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}



if (x==26) {
	if (importarValoresRubro(lis_importadia26,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}



if (x==27) {
	if (importarValoresRubro(lis_importadia27,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}


if (x==28) {
	if (importarValoresRubro(lis_importadia28,x)) {
		//utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}

if (x==29) {
	if (importarValoresRubro(lis_importadia29,x)) {
	//	utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}


if (x==30) {
	if (importarValoresRubro(lis_importadia30,x)) {
//		utilitario.agregarMensaje("Los valores se importaron correctamente", "");
	}
}


if (x==31) {
	if (importarValoresRubro(lis_importadia31,x)) {
	}
}


	
}	
	
	validacionEmpleado=ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length());
	if (validacionEmpleado.equals(",")) {
		tab_tabla.setCondicion("IDE_asjei IN ("+ide_asjei+") AND IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" and ide_gtemp in ("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
	
	}else {
		tab_tabla.setCondicion("IDE_asjei IN ("+ide_asjei+") AND IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" and ide_gtemp in ("+ide_empleados_crear+")");
		}
	
	
		//	+ "and ide_gtemp in ("+empleado+")");
	
	
	TablaGenerica tabHorario=null;
	if(tipo_perfil.equals("1")){
		ide_asjei=tabJefeInmediato.getValor("ide_asjei");
	}
	String sqlHorario="SELECT ide_ashme, ide_gemes, ide_gtemp, dia1, dia2, dia3, dia4, dia5, "
			+ "dia6, dia7, dia8, dia9, dia10, dia11, dia12, dia13, dia14, dia15, "
			+ "dia16, dia17, dia18, dia19, dia20, dia21, dia22, dia23, dia24, "
			+ "dia25, dia26, dia27, dia28, dia29, dia30, dia31, aplica_hora_extra, "
			+ "activo_ashme, ide_geani, ide_sucursal, ide_geare, ide_gedep,  "
			+ "num_extra_ashem, num_suple_ashem, usuario_ingre, fecha_ingre,  "
			+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, ide_geedp,  "
			+ "ide_geedp_cambio, ide_asjei, documento_identidad_gtemp "
			+ "FROM asi_horario_mes_empleado "
			+ "where ide_gemes="+mes+" and ide_geani="+anio+" and ide_asjei="+ide_asjei;
	
	tabHorario=utilitario.consultar(sqlHorario);
	
	int ide_gemes=0; 
	int ide_gtemp=0; 
	int dia1=0; 
	int dia2=0;
	int dia3=0; 
	int dia4=0; 
	int dia5=0; 
	int dia6=0;
	int dia7=0; 
	int dia8=0;
	int dia9=0;
	int dia10=0; 
	int dia11=0;
	int dia12=0; 
	int dia13=0; 
	int dia14=0; 
	int dia15=0;
	int dia16=0;
	int dia17=0; 
	int dia18=0; 
	int dia19=0;
	int dia20=0; 
	int dia21=0; 
	int dia22=0; 
	int dia23=0; 
	int dia24=0; 
	int dia25=0;
	int dia26=0; 
	int dia27=0; 
	int dia28=0; 
	int dia29=0; 
	int dia30=0;
	int dia31=0; 
	boolean activo_ashex=false; 
	int ide_geani=0; 
	int ide_sucursal=0;
	int ide_geare=0; 
	int ide_gedep=0; 
    String usuario_ingre=""; 
    String fecha_ingre=""; 
    String usuario_actua=""; 
    String fecha_actua="";
    String hora_ingre=""; 
    String hora_actua=""; 
    int ide_asjei=0; 
    String documento_identidad_gtemp="";
	
	
	for (int i = 0; i < tabHorario.getTotalFilas(); i++) {
		
		ide_gemes=Integer.parseInt(tabHorario.getValor(i,"ide_gemes")); 
		ide_gtemp=Integer.parseInt(tabHorario.getValor(i,"ide_gtemp")); 
		if (tabHorario.getValor(i,"dia1")==null) {
			dia1=0;
		}else {
			dia1=Integer.parseInt(tabHorario.getValor(i,"dia1")); 
		}
		
		if (tabHorario.getValor(i,"dia2")==null) {
			dia2=0;
		}else {
			dia2=Integer.parseInt(tabHorario.getValor(i,"dia2")); 
		}
		
		
		if (tabHorario.getValor(i,"dia3")==null) {
			dia3=0;
		}else {
			dia3=Integer.parseInt(tabHorario.getValor(i,"dia3")); 
		}
		
		
		if (tabHorario.getValor(i,"dia4")==null) {
			dia4=0;
		}else {
			dia4=Integer.parseInt(tabHorario.getValor(i,"dia4")); 
		}
		
		if (tabHorario.getValor(i,"dia5")==null) {
			dia5=0;
		}else {
			dia5=Integer.parseInt(tabHorario.getValor(i,"dia5")); 
		}
		
		if (tabHorario.getValor(i,"dia6")==null) {
			dia6=0;
		}else {
			dia6=Integer.parseInt(tabHorario.getValor(i,"dia6")); 
		}
		
		if (tabHorario.getValor(i,"dia7")==null) {
			dia7=0;
		}else {
			dia7=Integer.parseInt(tabHorario.getValor(i,"dia7")); 
		}
		
		if (tabHorario.getValor(i,"dia8")==null) {
			dia8=0;
		}else {
			dia8=Integer.parseInt(tabHorario.getValor(i,"dia8")); 
		}
		
		if (tabHorario.getValor(i,"dia9")==null) {
			dia9=0;
		}else {
			dia9=Integer.parseInt(tabHorario.getValor(i,"dia9"));
		}
		
		if (tabHorario.getValor(i,"dia10")==null) {
			dia10=0;
		}else {
			dia10=Integer.parseInt(tabHorario.getValor(i,"dia10")); 
		}
		
		if (tabHorario.getValor(i,"dia11")==null) {
			dia11=0;
		}else {
			dia11=Integer.parseInt(tabHorario.getValor(i,"dia11")); 
		}
		
		
		if (tabHorario.getValor(i,"dia12")==null) {
			dia12=0;
		}else {
			dia12=Integer.parseInt(tabHorario.getValor(i,"dia12")); 
		}
		
		
		if (tabHorario.getValor(i,"dia13")==null) {
			dia13=0;
		}else {
			dia13=Integer.parseInt(tabHorario.getValor(i,"dia13")); 
		}
		
		
		if (tabHorario.getValor(i,"dia14")==null) {
			dia14=0;
		}else {
			dia14=Integer.parseInt(tabHorario.getValor(i,"dia14")); 
		}
		
		
		if (tabHorario.getValor(i,"dia15")==null) {
			dia15=0;
		}else {
			dia15=Integer.parseInt(tabHorario.getValor(i,"dia15")); 
		}
		
		
		if (tabHorario.getValor(i,"dia16")==null) {
			dia16=0;
		}else {
			dia16=Integer.parseInt(tabHorario.getValor(i,"dia16")); 
		}
		
		if (tabHorario.getValor(i,"dia17")==null) {
			dia17=0;
		}else {
			dia17=Integer.parseInt(tabHorario.getValor(i,"dia17")); 
		}
		
		if (tabHorario.getValor(i,"dia18")==null) {
			dia18=0;
		}else {
			dia18=Integer.parseInt(tabHorario.getValor(i,"dia18")); 
		}
		
		if (tabHorario.getValor(i,"dia19")==null) {
			dia19=0;
		}else {
			dia19=Integer.parseInt(tabHorario.getValor(i,"dia19")); 
		}
		
		if (tabHorario.getValor(i,"dia20")==null) {
			dia20=0;
		}else {
			dia20=Integer.parseInt(tabHorario.getValor(i,"dia20")); 
		}
		
		if (tabHorario.getValor(i,"dia21")==null) {
			dia21=0;
		}else {
			dia21=Integer.parseInt(tabHorario.getValor(i,"dia21")); 
		}
		
		if (tabHorario.getValor(i,"dia22")==null) {
			dia22=0;
		}else {
			dia22=Integer.parseInt(tabHorario.getValor(i,"dia22")); 
		}
		
		if (tabHorario.getValor(i,"dia23")==null) {
			dia23=0;
		}else {
			dia23=Integer.parseInt(tabHorario.getValor(i,"dia23")); 
		}
		
		if (tabHorario.getValor(i,"dia24")==null) {
			dia24=0;
		}else {
			dia24=Integer.parseInt(tabHorario.getValor(i,"dia24")); 
		}
		
		if (tabHorario.getValor(i,"dia25")==null) {
			dia25=0;
		}else {
			dia25=Integer.parseInt(tabHorario.getValor(i,"dia25")); 
		}
		
		if (tabHorario.getValor(i,"dia26")==null) {
			dia26=0;
		}else {
			dia26=Integer.parseInt(tabHorario.getValor(i,"dia26")); 
		}
		
		if (tabHorario.getValor(i,"dia27")==null) {
			dia27=0;
		}else {
			dia27=Integer.parseInt(tabHorario.getValor(i,"dia27")); 
		}
		
		if (tabHorario.getValor(i,"dia28")==null) {
			dia28=0;
		}else {
			dia28=Integer.parseInt(tabHorario.getValor(i,"dia28")); 
		}
		
		if (tabHorario.getValor(i,"dia29")==null) {
			dia29=0;
		}else {
			dia29=Integer.parseInt(tabHorario.getValor(i,"dia29")); 
		}
		
		if (tabHorario.getValor(i,"dia30")==null) {
			dia30=0;
		}else {
			dia30=Integer.parseInt(tabHorario.getValor(i,"dia30")); 
		}
		
		if (tabHorario.getValor(i,"dia31")==null) {
			dia31=0;
		}else {
			dia31=Integer.parseInt(tabHorario.getValor(i,"dia31")); 
		}
		
		activo_ashex=true; 
		ide_geani=Integer.parseInt(tabHorario.getValor(i,"ide_geani")); 
		ide_sucursal=Integer.parseInt(tabHorario.getValor(i,"ide_sucursal")); 
		ide_geare=Integer.parseInt(tabHorario.getValor(i,"ide_geare")); 
		ide_gedep=Integer.parseInt(tabHorario.getValor(i,"ide_gedep")); 
		usuario_ingre=tabHorario.getValor(i,"usuario_ingre"); 
		fecha_ingre=tabHorario.getValor(i,"fecha_ingre"); 
		usuario_actua=""; 
		fecha_actua=""; 
		hora_ingre=tabHorario.getValor(i,"hora_ingre"); 
		hora_actua=""; 
		ide_asjei=Integer.parseInt(tabHorario.getValor(i,"ide_asjei")); 
		documento_identidad_gtemp=tabHorario.getValor(i,"documento_identidad_gtemp");
		
	insertarAsignacionMensualHorarioHistorial(ide_gemes, 
			ide_gtemp, 
			dia1, 
			dia2, 
			dia3, 
			dia4, 
			dia5, 
			dia6, 
			dia7, 
			dia8, 
			dia9, 
			dia10, 
			dia11, 
			dia12, 
			dia13, 
			dia14, 
			dia15, 
			dia16, 
			dia17, 
			dia18, 
			dia19, 
			dia20, 
			dia21, 
			dia22, 
			dia23, 
			dia24, 
			dia25, 
			dia26, 
			dia27, 
			dia28, 
			dia29, 
			dia30, 
			dia31, 
			activo_ashex, 
			ide_geani, 
			ide_sucursal, 
			ide_geare, 
			ide_gedep, 
			usuario_ingre, 
			fecha_ingre, 
			usuario_actua, 
			fecha_actua, 
			hora_ingre, 
			hora_actua, 
			ide_asjei, 
			documento_identidad_gtemp);
	}
	
	
	tab_tabla.ejecutarSql();
	getCalendario(mes, anio);
	utilitario.addUpdate("tab_tabla,tab_calendario");

	//tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+")  and ide_asjei="+jefe_inmediato_planificacion);
	///tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
	dia_importar_total.cerrar();
	dia_valida_empleado_total.cerrar();
	utilitario.agregarMensaje("Valores Importados con exito", "Se ha importado los valores correctamente");
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



public Tabla getTab_emp_total() {
	return tab_emp_total;
}



public void setTab_emp_total(Tabla tab_emp_total) {
	this.tab_emp_total = tab_emp_total;
}
 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}



public void insertarAsignacionMensualHorario(int ide_geani, int ide_gemes, String ide_gtemp, String ide_asjei,String cedula){
	
	
	
	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_horario_mes_empleado", "ide_ashme"));
	String codigo=tab_codigo.getValor("codigo");
	if(tipo_perfil.equals("1")){
		ide_asjei=tabJefeInmediato.getValor("ide_asjei");
	}
	
	TablaGenerica tabUsuario=utilitario.consultar("select ide_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
	TablaGenerica tabSucuAreaDept=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
			+ "where epar.ide_gtemp="+ide_gtemp+" " 
			+ "	ORDER BY DETALLE_GEARE ASC");
			
	
	utilitario.getConexion().ejecutarSql("INSERT INTO asi_horario_mes_empleado(" 
				+ "ide_ashme, "
				+ "ide_gemes, "
				+ "ide_gtemp, "
				+ "dia1, "
				+ "dia2, "
				+ "dia3, "
				+ "dia4, "
				+ "dia5, "
				+ "dia6, "
				+ "dia7, "
				+ "dia8, "
				+ "dia9, "
				+ "dia10, "
				+ "dia11, "
				+ "dia12, "
				+ "dia13, "
				+ "dia14, "
				+ "dia15, "
				+ "dia16, "
				+ "dia17, "
				+ "dia18, "
				+ "dia19, "
				+ "dia20, "
				+ "dia21, "
				+ "dia22, "
				+ "dia23, "
				+ "dia24, "
				+ "dia25,  "
				+ "dia26,  "
				+ "dia27,  "
				+ "dia28, "
				+ "dia29, "
				+ "dia30, "
				+ "dia31, "
				+ "aplica_hora_extra, "
				+ "activo_ashme, "
				+ "ide_geani, "
				+ "ide_sucursal, "
				+ "ide_geare, "
				+ "ide_gedep, "
				+ "num_extra_ashem, "
				+ "num_suple_ashem, "
				+ "usuario_ingre, "
				+ "fecha_ingre, "
				+ "usuario_actua, "
				+ "fecha_actua, "
				+ "hora_ingre, "
				+ "hora_actua, "
				+ "ide_geedp, "
				+ "ide_geedp_cambio, "
				+ "ide_asjei, "
				+ "documento_identidad_gtemp)" 

		  		+" values( " +codigo + ", "
		  		+ ""+ ide_gemes+", "
		  		+ ""+ide_gtemp+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ ""+false+", "
		  		+ ""+true+", "
		  		+ ""+ide_geani+", "		  		
		  		+ ""+tabSucuAreaDept.getValor("IDE_SUCU").toString()+", "
		  		+ ""+tabSucuAreaDept.getValor("IDE_GEARE").toString()+", "
		  		+ ""+tabSucuAreaDept.getValor("IDE_GEDEP").toString()+", "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ "'"+tabUsuario.getValor("nick_usua")+"', "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ ""+null+", "
		  		+ ""+null+", "
		  		+ "'"+utilitario.getHoraActual()+"', "
		  		+ ""+null+", "
		  		+ ""+ide_geedp_activo+", "
		  		+ ""+null+", "		  		
		  		+ ""+ide_asjei+", "
		  		+ "'"+cedula+"')"); 
	

}
	    

public void insertarAsignacionMensualHorarioHistorial(
	int ide_gemes, 
	int ide_gtemp, 
	int dia1, 
	int dia2, 
	int dia3, 
	int dia4, 
	int dia5, 
	int dia6, 
	int dia7, 
	int dia8, 
	int dia9, 
	int dia10, 
	int dia11, 
	int dia12, 
	int dia13, 
	int dia14, 
	int dia15, 
	int dia16, 
	int dia17, 
	int dia18, 
	int dia19, 
	int dia20, 
	int dia21, 
	int dia22, 
	int dia23, 
	int dia24, 
	int dia25, 
	int dia26, 
	int dia27, 
	int dia28, 
	int dia29, 
	int dia30, 
	int dia31, 
	boolean activo_ashex, 
	int ide_geani, 
	int ide_sucursal, 
	int ide_geare, 
	int ide_gedep, 
    String usuario_ingre, 
    String fecha_ingre, 
    String usuario_actua, 
    String fecha_actua, 
    String hora_ingre, 
    String hora_actua, 
    int ide_asjei, 
    String documento_identidad_gtemp){
	
	
	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_horario_mes_empleado_historial", "ide_asheh"));
	String codigo=tab_codigo.getValor("codigo");
	
	String sql="INSERT INTO asi_horario_mes_empleado_historial(" 
			+ "ide_asheh, "
			+ "ide_gemes, "
			+ "ide_gtemp, "
			+ "dia1, "
			+ "dia2, "
			+ "dia3, "
			+ "dia4, "
			+ "dia5, "
			+ "dia6, "
			+ "dia7, "
			+ "dia8, "
			+ "dia9, "
			+ "dia10, "
			+ "dia11, "
			+ "dia12, "
			+ "dia13, "
			+ "dia14, "
			+ "dia15, "
			+ "dia16, "
			+ "dia17, "
			+ "dia18, "
			+ "dia19, "
			+ "dia20, "
			+ "dia21, "
			+ "dia22, "
			+ "dia23, "
			+ "dia24, "
			+ "dia25,  "
			+ "dia26,  "
			+ "dia27,  "
			+ "dia28, "
			+ "dia29, "
			+ "dia30, "
			+ "dia31, "
			+ "activo_asheh, "
			+ "ide_geani, "
			+ "ide_sucursal, "
			+ "ide_geare, "
			+ "ide_gedep, "
			+"usuario_ingre, "
			+ "fecha_ingre, "
			+ "usuario_actua, "
			+ "fecha_actua, "
			+ "hora_ingre, "
			+ "hora_actua, "
			+ "ide_asjei, "
			+ "documento_identidad_gtemp)" 

	  		+" values( " +codigo + ", "
	  		+ ""+ ide_gemes+", "
	  		+ ""+ide_gtemp+", ";
	  		if (dia1==0) {
		  	sql+=""+null+", ";
			}else {
			 sql+=""+dia1+", ";
			}
	  		
	  		if (dia2==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia2+", ";
				}
	  		
	  		
	  		if (dia3==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia3+", ";
				}
	  		
	  		
	  		if (dia4==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia4+", ";
				}
	  		
	  		
	  		if (dia5==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia5+", ";
				}
	  		
	  		
	  		if (dia6==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia6+", ";
				}
	  		
	  		if (dia7==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia7+", ";
				}
	  		
	  		if (dia8==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia8+", ";
				}
	  		
	  		if (dia9==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia9+", ";
				}
	  		
	  		if (dia10==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia10+", ";
				}
	  		
	  		if (dia11==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia11+", ";
				}
	  		
	  		if (dia12==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia12+", ";
				}
	  		
	  		if (dia13==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia13+", ";
				}
	  		
	  		if (dia14==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia14+", ";
				}
	  		
	  		if (dia15==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia15+", ";
				}
	  		
	  		if (dia16==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia16+", ";
				}
	  		
	  		if (dia17==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia17+", ";
				}
	  		
	  		if (dia18==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia18+", ";
				}
	  		
	  		
	  		
			if (dia19==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia19+", ";
				}
			
			if (dia20==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia20+", ";
				}
			
			if (dia21==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia21+", ";
				}
			
			if (dia22==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia22+", ";
				}
			
			if (dia23==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia23+", ";
				}
			
			if (dia24==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia24+", ";
				}
			
			if (dia25==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia25+", ";
				}
			
			if (dia26==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia26+", ";
				}
	  		
			
			if (dia27==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia27+", ";
				}
			
			if (dia28==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia28+", ";
				}
			
			if (dia29==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia29+", ";
				}
			
			if (dia30==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia30+", ";
				}
			
			if (dia31==0) {
			  	sql+=""+null+", ";
				}else {
				 sql+=""+dia31+", ";
				}
			
 		
	  		
	  		sql+= ""+true+", "
	  		+ ""+ide_geani+", "		  		
	  		+ ""+ide_sucursal+", "
	  		+ ""+ide_geare+", "
	  		+ ""+ide_gedep+", "
	  	//	+ ""+null+", "
	  	//	+ ""+null+", "
	  		+ "'"+usuario_ingre+"', "
	  		+ "'"+fecha_ingre+"', "
	  		+ ""+null+", "
	  		+ ""+null+", "
	  		+ "'"+hora_ingre+"', "
	  		+ ""+null+", "
	  		+ ""+ide_asjei+", "
	  		+ "'"+documento_identidad_gtemp+"')"; 
	
	
		utilitario.getConexion().ejecutarSql(sql);
	

}


public void abrir(){
	TablaGenerica tabHorario=null;
	if(tipo_perfil.equals("1")){
		ide_asjei=tabJefeInmediato.getValor("ide_asjei");
	}
	String sqlHorario="SELECT ide_ashme, ide_gemes, ide_gtemp, dia1, dia2, dia3, dia4, dia5, "
			+ "dia6, dia7, dia8, dia9, dia10, dia11, dia12, dia13, dia14, dia15, "
			+ "dia16, dia17, dia18, dia19, dia20, dia21, dia22, dia23, dia24, "
			+ "dia25, dia26, dia27, dia28, dia29, dia30, dia31, aplica_hora_extra, "
			+ "activo_ashme, ide_geani, ide_sucursal, ide_geare, ide_gedep,  "
			+ "num_extra_ashem, num_suple_ashem, usuario_ingre, fecha_ingre,  "
			+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, ide_geedp,  "
			+ "ide_geedp_cambio, ide_asjei, documento_identidad_gtemp "
			+ "FROM asi_horario_mes_empleado "
			+ "where ide_gemes="+8+" and ide_geani="+16; //and ide_asjei="+ide_asjei;
	
	tabHorario=utilitario.consultar(sqlHorario);
	
	int ide_gemes=0; 
	int ide_gtemp=0; 
	int dia1=0; 
	int dia2=0;
	int dia3=0; 
	int dia4=0; 
	int dia5=0; 
	int dia6=0;
	int dia7=0; 
	int dia8=0;
	int dia9=0;
	int dia10=0; 
	int dia11=0;
	int dia12=0; 
	int dia13=0; 
	int dia14=0; 
	int dia15=0;
	int dia16=0;
	int dia17=0; 
	int dia18=0; 
	int dia19=0;
	int dia20=0; 
	int dia21=0; 
	int dia22=0; 
	int dia23=0; 
	int dia24=0; 
	int dia25=0;
	int dia26=0; 
	int dia27=0; 
	int dia28=0; 
	int dia29=0; 
	int dia30=0;
	int dia31=0; 
	boolean activo_ashex=false; 
	int ide_geani=0; 
	int ide_sucursal=0;
	int ide_geare=0; 
	int ide_gedep=0; 
    String usuario_ingre=""; 
    String fecha_ingre=""; 
    String usuario_actua=""; 
    String fecha_actua="";
    String hora_ingre=""; 
    String hora_actua=""; 
    int ide_asjei=0; 
    String documento_identidad_gtemp="";
	
	
	for (int i = 0; i < tabHorario.getTotalFilas(); i++) {
		
		ide_gemes=Integer.parseInt(tabHorario.getValor(i,"ide_gemes")); 
		ide_gtemp=Integer.parseInt(tabHorario.getValor(i,"ide_gtemp")); 
		if (tabHorario.getValor(i,"dia1")==null) {
			dia1=0;
		}else {
			dia1=Integer.parseInt(tabHorario.getValor(i,"dia1")); 
		}
		
		if (tabHorario.getValor(i,"dia2")==null) {
			dia2=0;
		}else {
			dia2=Integer.parseInt(tabHorario.getValor(i,"dia2")); 
		}
		
		
		if (tabHorario.getValor(i,"dia3")==null) {
			dia3=0;
		}else {
			dia3=Integer.parseInt(tabHorario.getValor(i,"dia3")); 
		}
		
		
		if (tabHorario.getValor(i,"dia4")==null) {
			dia4=0;
		}else {
			dia4=Integer.parseInt(tabHorario.getValor(i,"dia4")); 
		}
		
		if (tabHorario.getValor(i,"dia5")==null) {
			dia5=0;
		}else {
			dia5=Integer.parseInt(tabHorario.getValor(i,"dia5")); 
		}
		
		if (tabHorario.getValor(i,"dia6")==null) {
			dia6=0;
		}else {
			dia6=Integer.parseInt(tabHorario.getValor(i,"dia6")); 
		}
		
		if (tabHorario.getValor(i,"dia7")==null) {
			dia7=0;
		}else {
			dia7=Integer.parseInt(tabHorario.getValor(i,"dia7")); 
		}
		
		if (tabHorario.getValor(i,"dia8")==null) {
			dia8=0;
		}else {
			dia8=Integer.parseInt(tabHorario.getValor(i,"dia8")); 
		}
		
		if (tabHorario.getValor(i,"dia9")==null) {
			dia9=0;
		}else {
			dia9=Integer.parseInt(tabHorario.getValor(i,"dia9"));
		}
		
		if (tabHorario.getValor(i,"dia10")==null) {
			dia10=0;
		}else {
			dia10=Integer.parseInt(tabHorario.getValor(i,"dia10")); 
		}
		
		if (tabHorario.getValor(i,"dia11")==null) {
			dia11=0;
		}else {
			dia11=Integer.parseInt(tabHorario.getValor(i,"dia11")); 
		}
		
		
		if (tabHorario.getValor(i,"dia12")==null) {
			dia12=0;
		}else {
			dia12=Integer.parseInt(tabHorario.getValor(i,"dia12")); 
		}
		
		
		if (tabHorario.getValor(i,"dia13")==null) {
			dia13=0;
		}else {
			dia13=Integer.parseInt(tabHorario.getValor(i,"dia13")); 
		}
		
		
		if (tabHorario.getValor(i,"dia14")==null) {
			dia14=0;
		}else {
			dia14=Integer.parseInt(tabHorario.getValor(i,"dia14")); 
		}
		
		
		if (tabHorario.getValor(i,"dia15")==null) {
			dia15=0;
		}else {
			dia15=Integer.parseInt(tabHorario.getValor(i,"dia15")); 
		}
		
		
		if (tabHorario.getValor(i,"dia16")==null) {
			dia16=0;
		}else {
			dia16=Integer.parseInt(tabHorario.getValor(i,"dia16")); 
		}
		
		if (tabHorario.getValor(i,"dia17")==null) {
			dia17=0;
		}else {
			dia17=Integer.parseInt(tabHorario.getValor(i,"dia17")); 
		}
		
		if (tabHorario.getValor(i,"dia18")==null) {
			dia18=0;
		}else {
			dia18=Integer.parseInt(tabHorario.getValor(i,"dia18")); 
		}
		
		if (tabHorario.getValor(i,"dia19")==null) {
			dia19=0;
		}else {
			dia19=Integer.parseInt(tabHorario.getValor(i,"dia19")); 
		}
		
		if (tabHorario.getValor(i,"dia20")==null) {
			dia20=0;
		}else {
			dia20=Integer.parseInt(tabHorario.getValor(i,"dia20")); 
		}
		
		if (tabHorario.getValor(i,"dia21")==null) {
			dia21=0;
		}else {
			dia21=Integer.parseInt(tabHorario.getValor(i,"dia21")); 
		}
		
		if (tabHorario.getValor(i,"dia22")==null) {
			dia22=0;
		}else {
			dia22=Integer.parseInt(tabHorario.getValor(i,"dia22")); 
		}
		
		if (tabHorario.getValor(i,"dia23")==null) {
			dia23=0;
		}else {
			dia23=Integer.parseInt(tabHorario.getValor(i,"dia23")); 
		}
		
		if (tabHorario.getValor(i,"dia24")==null) {
			dia24=0;
		}else {
			dia24=Integer.parseInt(tabHorario.getValor(i,"dia24")); 
		}
		
		if (tabHorario.getValor(i,"dia25")==null) {
			dia25=0;
		}else {
			dia25=Integer.parseInt(tabHorario.getValor(i,"dia25")); 
		}
		
		if (tabHorario.getValor(i,"dia26")==null) {
			dia26=0;
		}else {
			dia26=Integer.parseInt(tabHorario.getValor(i,"dia26")); 
		}
		
		if (tabHorario.getValor(i,"dia27")==null) {
			dia27=0;
		}else {
			dia27=Integer.parseInt(tabHorario.getValor(i,"dia27")); 
		}
		
		if (tabHorario.getValor(i,"dia28")==null) {
			dia28=0;
		}else {
			dia28=Integer.parseInt(tabHorario.getValor(i,"dia28")); 
		}
		
		if (tabHorario.getValor(i,"dia29")==null) {
			dia29=0;
		}else {
			dia29=Integer.parseInt(tabHorario.getValor(i,"dia29")); 
		}
		
		if (tabHorario.getValor(i,"dia30")==null) {
			dia30=0;
		}else {
			dia30=Integer.parseInt(tabHorario.getValor(i,"dia30")); 
		}
		
		if (tabHorario.getValor(i,"dia31")==null) {
			dia31=0;
		}else {
			dia31=Integer.parseInt(tabHorario.getValor(i,"dia31")); 
		}
		
		activo_ashex=true; 
		ide_geani=Integer.parseInt(tabHorario.getValor(i,"ide_geani")); 
		ide_sucursal=Integer.parseInt(tabHorario.getValor(i,"ide_sucursal")); 
		ide_geare=Integer.parseInt(tabHorario.getValor(i,"ide_geare")); 
		ide_gedep=Integer.parseInt(tabHorario.getValor(i,"ide_gedep")); 
		usuario_ingre=tabHorario.getValor(i,"usuario_ingre"); 
		fecha_ingre=tabHorario.getValor(i,"fecha_ingre"); 
		usuario_actua=""; 
		fecha_actua=""; 
		hora_ingre=tabHorario.getValor(i,"hora_ingre"); 
		hora_actua=""; 
		ide_asjei=Integer.parseInt(tabHorario.getValor(i,"ide_asjei")); 
		documento_identidad_gtemp=tabHorario.getValor(i,"documento_identidad_gtemp");
		
	insertarAsignacionMensualHorarioHistorial(ide_gemes, 
			ide_gtemp, 
			dia1, 
			dia2, 
			dia3, 
			dia4, 
			dia5, 
			dia6, 
			dia7, 
			dia8, 
			dia9, 
			dia10, 
			dia11, 
			dia12, 
			dia13, 
			dia14, 
			dia15, 
			dia16, 
			dia17, 
			dia18, 
			dia19, 
			dia20, 
			dia21, 
			dia22, 
			dia23, 
			dia24, 
			dia25, 
			dia26, 
			dia27, 
			dia28, 
			dia29, 
			dia30, 
			dia31, 
			activo_ashex, 
			ide_geani, 
			ide_sucursal, 
			ide_geare, 
			ide_gedep, 
			usuario_ingre, 
			fecha_ingre, 
			usuario_actua, 
			fecha_actua, 
			hora_ingre, 
			hora_actua, 
			ide_asjei, 
			documento_identidad_gtemp);
	}
	
}


public Date sumarDiasFechaSinFinSemana(Date fch, int numeroDiasSumar) {
	
	Calendar fechaInicial = Calendar.getInstance();
	Calendar fechaInicialCalculo = Calendar.getInstance();

	fechaInicial.setTime(fch);
	int contador  = 1;
	while (contador <= numeroDiasSumar)
	{
		
	//	if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
		 if (fechaInicial.get(Calendar.DAY_OF_WEEK) != 7 && fechaInicial.get(Calendar.DAY_OF_WEEK) != 6) {

			 contador++;

		}
			fechaInicial.add(Calendar.DATE, -1);

	}
	return fechaInicial.getTime();

}

	    
}
