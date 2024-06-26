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

import org.apache.commons.collections.functors.ForClosure;
import org.codehaus.groovy.tools.groovydoc.FileOutputTool;
import org.jfree.util.Log;
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
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author Juan
 */
public class pre_calendario_horario_empleado_hxe extends Pantalla {
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_calendario= new Tabla(); 
    private Tabla tab_turnos= new Tabla(); 
    private Tabla tab_tabla3= new Tabla(); 
private boolean copirDepartamento,copiar;

private String empleado1;
private String empleadoSucursal;
private String meses="",anios="",mesEditar="",anioEditar="";
int asignar =0,editarValidacion=0;

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


private    	String sucu="";
private	String area="";
private String depa="";
     	
    public Grid getGri() {
		return gri;
	}



	public void setGri(Grid gri) {
		this.gri = gri;
	}



	public pre_calendario_horario_empleado_hxe() {
    

		//Consulta empleado loggeado
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

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
					+ "from asi_horario_mes_empleado_hxe asemp   "
					+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=asemp.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=asemp.IDE_SUCURSAL  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=asemp.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=asemp.IDE_GEARE  "
					+ "where asemp.ide_gemes="+mes+" and asemp.ide_geani in("+anio+") "
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
	  	
		
		
	
		
		
		
		tab_tabla.setId("tab_tabla");
  	    tab_tabla.setTabla("asi_horario_mes_empleado_hxe", "ide_ashex", 1);
  	    tab_tabla.getColumna("ide_ashex").setNombreVisual("COD");
  	    tab_tabla.getColumna("ide_ashex").setLongitud(5);
  	    tab_tabla.getColumna("ide_ashex").alinearCentro();
  	    tab_tabla.getColumna("ide_ashex").setOrden(1);

		tab_tabla.getColumna("documento_identidad_gtemp").setNombreVisual("DOC.IDENTIDAD");
		tab_tabla.getColumna("documento_identidad_gtemp").setFiltroContenido();
  	    tab_tabla.getColumna("documento_identidad_gtemp").setLongitud(15);
  	    tab_tabla.getColumna("documento_identidad_gtemp").setOrden(2);
  	  	tab_tabla.getColumna("documento_identidad_gtemp").setLectura(true);

 
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
		tab_tabla.getColumna("IDE_GTEMP").setFiltroContenido();

        
        
        tab_tabla.getColumna("IDE_GEMES").setCombo("select ide_gemes,detalle_gemes from gen_mes ");
        tab_tabla.getColumna("IDE_GEMES").setVisible(false);
        tab_tabla.getColumna("IDE_GEANI").setCombo("select ide_geani,detalle_geani from gen_anio");
        tab_tabla.getColumna("IDE_GEANI").setVisible(false);
        
       int ultimo_dia=0;
       ultimo_dia=31;//utilitario.getDia(utilitario.getUltimaFechaMes(utilitario.getFechaActual()));
       int dia_no_visibles=0;
       dia_no_visibles=31;
       
       //ultimo_dia
       
       
       for (int i = 1; i <=ultimo_dia; i++) {
           tab_tabla.getColumna("dia"+i).setCombo("SELECT astur.ide_astur,astur.nom_astur "
              		+ "FROM asi_turnos  astur  "
              		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
              		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) and hxe_astur=true");
       	}
       
       for (int i = (ultimo_dia+1); i <=31; i++) {
    	   tab_tabla.getColumna("dia"+i).setVisible(false);
       }
       
       /*tab_tabla.getColumna("dia1").setCombo("SELECT astur.ide_astur,astur.nom_astur "
       		+ "FROM asi_turnos  astur  "
       		+ "left join asi_turnos_horario tur on tur.ide_astur=astur.ide_astur  "
       		+ "where tur.ide_ashor is not null and astur.ide_astur not in(1) ");
    
x
       
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

        tab_tabla.getColumna("activo_ashex").setNombreVisual("ACTIVO");
        tab_tabla.getColumna("activo_ashex").setLongitud(5);

        tab_tabla.getColumna("activo_ashex").setCheck();
        tab_tabla.getColumna("activo_ashex").setValorDefecto("true");
        tab_tabla.getColumna("activo_ashex").alinearCentro();
        
        tab_tabla.getColumna("ide_asjei").setVisible(false);    
        tab_tabla.getColumna("IDE_SUCURSAL").setVisible(false);
        tab_tabla.getColumna("IDE_GEARE").setVisible(false);
                
        tab_tabla.setRows(10);
        tab_tabla.setCondicion("ide_ashex=-1");
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
        tab_tabla2.setCondicion("turno_matriz_astur=false and hxe_astur=true");


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
    	tab_tabla.setCondicion("ide_ashex=-1");
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
		/*TablaGenerica tabEmpleadoMensualBase=utilitario.consultar("select ide_gtemp,ide_ashex "
				+ "from asi_horario_mes_empleado_hxe  "
				+ " where ide_geani in("+anioEmpleado+") and ide_gemes in ("+mesActual+") and ide_geedp="+ide_geedp_activo);
		
			
		StringBuilder str_ide_empleado_mensualBase=new StringBuilder();
		String int_num_col_idegetempmensualBase="";
		for (int i = 0; i < tabEmpleadoMensualBase.getTotalFilas(); i++) {
			int_num_col_idegetempmensualBase=tabEmpleadoMensualBase.getValor(i, "IDE_GTEMP");
			if(str_ide_empleado_mensualBase.toString().isEmpty()==false){
				str_ide_empleado_mensualBase.append(",");
		 }
			str_ide_empleado_mensualBase.append(int_num_col_idegetempmensualBase);
		}*/
		
		
		
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
 		tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashex "
				+ "from asi_horario_mes_empleado_hxe  "
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
							+ "WHERE asemp.ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and asemp.activo_emjei=true "
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
 							+ "WHERE asemp.ide_gtemp not in ("+str_ide_empleado_mensual.toString()+")  and asemp.activo_emjei=true "
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
    			TablaGenerica tablaEmpleado= utilitario.consultar("select * from asi_horario_mes_empleado_hxe  where ide_geani in "
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
        					+ "left join asi_horario_mes_empleado_hxe asemp on asemp.ide_gtemp=emp.ide_gtemp  "
        					+ " where emp.activo_gtemp in (true) "
        					+ " and ide_gemes="+mes+" and ide_geani in("+sel_anio_editar.getValorSeleccionado()+")  "
        					+ "order by apellido_paterno_gtemp, apellido_materno_gtemp";
        				//	sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");
        		
				}else {
					
				//	TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
				//	TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				//			+ "FROM asi_jefe_inmediato   "
				//			+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
					
		
			    						
							sql="select epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP,  "
							+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,   "
							+ "emp.documento_identidad_gtemp  "
							+ "	 from  asi_horario_mes_empleado_hxe asemp "
							+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp  "
							+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
							+ " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
							+ " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP   "
							+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE   "
							+ " WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true  "
							+ " and asemp.ide_geani in("+sel_anio_editar.getValorSeleccionado()+") and asemp.ide_gemes in("+mes+") and asemp.ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+"  "  
							+ " GROUP BY  epar.ide_gtemp,SUCU.NOM_SUCU ,DEPA.DETALLE_GEDEP , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp   "
							+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
							+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

							//sel_empleado_editar.setSeleccionTabla(sql,"IDE_GTEMP");

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
    			
    			sel_empleado_editar.setSeleccionTabla(sql, "IDE_GTEMP");

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
		/*TablaGenerica tabEmpleadoMensualBase=utilitario.consultar("select ide_gtemp,ide_ashex "
				+ "from asi_horario_mes_empleado_hxe  "
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
		
	*/	
	
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
					+ "FROM asi_empleado_jefe_inmediato  where activo_emjei=true ");
			
		}else{
		tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
					+ "FROM asi_empleado_jefe_inmediato "
					+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei")+" and activo_emjei=true ");
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
	tabEmpleadoMensual=utilitario.consultar("select ide_gtemp,ide_ashex "
		+ "from asi_horario_mes_empleado_hxe  "
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


       	TablaGenerica  tab_empleados=utilitario.consultar("select * from asi_horario_mes_empleado_hxe where ide_ashex="+empleadoCopiar+" and ide_geani="+anio+" and "
       			+ "ide_gemes="+mes);
      	for (int i = 0; i < numIngresos; i++) {
           	TablaGenerica  tab_empleadosTurno=utilitario.consultar("select * from asi_horario_mes_empleado_hxe where ide_gtemp="+listaEmpleado.get(i)+" and "
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
    	tab_tabla.setValor("activo_ashex",tab_empleados.getValor("activo_ashex"));
    	tab_tabla.setValor("ide_geani",tab_empleados.getValor("ide_geani"));
		tab_tabla.setValor("IDE_SUCURSAL",sucu);
		tab_tabla.setValor("ide_GEARE",area);
		tab_tabla.setValor("ide_GEDEP",depa);
       		// INGRESO EMPLEADO
	     TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
		TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
				+ "FROM asi_jefe_inmediato   "
				+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
		tab_tabla.setValor("ide_asjei", tabJefeInmediato.getValor("ide_asjei"));
		tab_tabla.setValor("documento_identidad_gtemp", tabSucuAreaDept.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));

		

	     			
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
		 			tab_tabla.setCondicion("ide_ashex=-1");
		 			tab_tabla.ejecutarSql();
		 			utilitario.addUpdate("tab_tabla");
		 			return;
		 		}
    	    	sel_empleado_editar.cerrar();

    		//Validadcion si no existe datos
    	    	TablaGenerica tab_consultar_empleado;
    	    	
    			if(empleadoSucursal.equals("508") || area.equals("9")   ){
    				 tab_consultar_empleado=utilitario.consultar("select ide_ashex,ide_gtemp from asi_horario_mes_empleado_hxe where ide_gtemp in ("+empleado+")  "
    	    				+ "and ide_gemes in ("+mes+") and ide_geani in("+anio+")");
    	    		
    			}else {
    			     TablaGenerica  tab_emp =utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp_activo+" limit 1");
    					TablaGenerica tabJefeInmediato=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei  "
    							+ "FROM asi_jefe_inmediato   "
    							+ "where ide_gtemp="+tab_emp.getValor("ide_gtemp"));
    	    		tab_consultar_empleado=utilitario.consultar("select ide_ashex,ide_gtemp from asi_horario_mes_empleado_hxe where ide_gtemp in ("+empleado+")  "
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
		TablaGenerica tab=utilitario.consultar("select ide_ashex,ide_gtemp from asi_horario_mes_empleado_hxe where ide_gemes in("+mes+") and ide_geani in("+anio+")");

		
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
       			//utilitario.getConexion().ejecutarSql("update asi_horario_mes_empleado_hxe set ide_geedp_cambio ="+ide_geedp_activo+" where ide_gtemp in("+tab_tabla.getValor(j, "IDE_GTEMP")+") "
       			//		+ "and ide_gemes="+mes);
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
			tab_tabla.setCondicion("ide_ashex=-1");
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
	
	
	tab_tabla.setCondicion("ide_ashex=-1");
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
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select ide_ashex,ide_gtemp from asi_horario_mes_empleado_hxe "
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

	
	public void cargarHorario(AjaxBehaviorEvent evt){
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
	    
}














