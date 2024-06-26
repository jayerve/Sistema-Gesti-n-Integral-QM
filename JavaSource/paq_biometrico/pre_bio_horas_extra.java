/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.poi.hssf.record.formula.Ptg;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.DateSelectEvent;
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
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

/**
 *
 * @author DELL-USER
 */
public class pre_bio_horas_extra extends Pantalla {


	private String ide_geare="";
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_empleado_total= new SeleccionTabla();
    private SeleccionTabla set_empleado_asignados= new SeleccionTabla();

    private SeleccionTabla set_empleado_reporte= new SeleccionTabla();
    private SeleccionTabla sel_por_empleado= new SeleccionTabla();
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();
    private StringBuilder str_ide_empleado_jefe_inmediato=new StringBuilder();
    private StringBuilder str_ide_jefe_inmediato=new StringBuilder();


    private SeleccionTabla set_departamento = new SeleccionTabla();
    @EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    @EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
    @EJB
    private ServicioEmpleado ser_empleado;
	private AutoCompletar aut_empleados=new AutoCompletar();
	TablaGenerica  obtenerTurnoEmpleado;
    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_empleado= new SeleccionTabla();

    
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros = new HashMap();
	String fechaConsultaInicio="";
    String fechaConsultaFin="";
    
    private Tabla tab_tabla_sumatoria =new Tabla();
    private Tabla tab_tabla_hora_extra =new Tabla();
    private Tabla tab_tabla_sumatoria_resumen =new Tabla();
	private Combo com_anio=new Combo();
	private String fechaReporteIni,fechaReporteFin;


	private String jefeAsignacionHorario="",ide_asjei="";
    String mes="";
  	String fechaIni="",fechaFin="";
  	String empleado="";
  	String fechaInicial="";
	String fechaFinal="";
	TablaGenerica tabEmpDep=null;
 	TablaGenerica tabEmpleado=null;
	TablaGenerica tabEmpDepAprobar=null;
	TablaGenerica tabEmpUsuario=null;

	StringBuilder str_ide_empleado=new StringBuilder();
	StringBuilder str_ide=new StringBuilder();

    private Calendario cal_fecha_inicial_marcaciones = new Calendario();
	private Calendario cal_fecha_final_marcaciones = new Calendario();
	
    private Calendario cal_fecha_inicial_marcaciones_reporte = new Calendario();
	private Calendario cal_fecha_final_marcaciones_reporte = new Calendario();
	
	private Dialogo dia_fecha_marcaciones = new Dialogo();
	private Dialogo dia_fecha_marcaciones_reporte = new Dialogo();

	String ide_gtempxx="";
	String ide_gtemp_jefe_inmediato="",cargo_elaborado="",area="",cargo_jefe_inmediato="";
    private String meses="",anios="",mesEditar="",anioEditar="",anio="",jefe_inmediato_planificacion="",tipo_perfil="",empleado_importar="",NombreEmpleado="",nombreMes="",NombreEmpleadoJefe="",NombreAprobador="";
    boolean bandRegistroPorEmpleado=false; 
    public pre_bio_horas_extra() {
    	

    	bloquearBotones();
		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
    	
    	//Fecha Actual
        String fecha =utilitario.getFechaActual();
        //Resto un mes a la fecha actual
        String fechaActual=getFechaAsyyyyMMdd(sumarRestarMeses(getFechaAsyyyyMMdd(fecha), -1));
        String mesDespliege="";
        //Obtengo mes 
        int mesTemp=utilitario.getMes(fechaActual);
        String mes="";
        if (mesTemp<10) {
			mes="0"+mesTemp;
		}else {
			mes=""+mesTemp;
		}
        

String empleado="";
       int diaFinMes=retornarDiasMes(1,Integer.parseInt(mes),utilitario.getAnio(fechaActual));
       fechaConsultaInicio=utilitario.getAnio(fechaActual)+"-"+mes+"-01";
       fechaConsultaFin=utilitario.getAnio(fechaActual)+"-"+mes+"-"+diaFinMes;
       tabEmpUsuario = utilitario.consultar("select ide_usua,ide_gtemp  from sis_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
       //Obtengo todos las acciones de personal
       
       if (tabEmpUsuario.getTotalFilas()>0) {
           tabEmpDep = utilitario.consultar("select ide_gtemp,ide_geedp,"
           		+ "ide_gegro, ide_gecaf, ide_sucu, "
           		+ "ide_gedep, ide_geare, ide_gttem  "
           		+ "from gen_empleados_departamento_par where ide_gtemp in ("+tabEmpUsuario.getValor("IDE_GTEMP")+") "
           		+ "order by ide_geedp asc");

	}else {
        tabEmpDep = utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_gtemp in (-1)  "
        		+ "order by ide_geedp asc");

	} 
		
      	int valor=0;
      	int valorEmp=0;
      	int valorJefe=0;
       boolean bandSinArea=false;
       int bandArea=0;

       if (tabEmpDep.getTotalFilas()>0) {
			bandSinArea=true;
	//Recorro la tabla con todas las acciones de personal efectuadas al empleado
         		for (int i = 0; i < tabEmpDep.getTotalFilas(); i++) 
         		{
         		//Voy anidando los ides de la accion
          	    str_ide_empleado.append(tabEmpDep.getValor(i, "IDE_GEEDP"));
               // valor++;
                if (tabEmpDep.getTotalFilas()==1) {
   			}else if (valor<=tabEmpDep.getTotalFilas()) {
   					valor++;
   					if(valor<(tabEmpDep.getTotalFilas())){
                    str_ide_empleado.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}
                
              //Si pertenece a la area de TICS o TTHH  
				if(!(tabEmpDep.getValor(i,"ide_geare").equals("10") || tabEmpDep.getValor(i,"ide_geare").equals("9")) ) { 
					bandArea=1;
				}else if((tabEmpDep.getValor(i,"ide_geare").equals("10") || tabEmpDep.getValor(i,"ide_geare").equals("9")) ) { 
					bandArea=2;
				}

                
    
   			 }
         		
         
         TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani "
         		+ "from gen_anio "
         		+ "where detalle_geani like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");	
         		
         //tomo las personas 
              	/*tabEmpleado= utilitario.consultar("SELECT ide_gtemp, "
              			+ "ide_geedp, "
              			+ "ide_geedp_cambio "
              			+ "FROM asi_horario_mes_empleado where ide_geedp in("+str_ide_empleado.toString()+") and ide_gemes="+mesTemp+" "
              					+ "and ide_geani="+tabAnio.getValor("IDE_GEANI"));*/
              	
              	
                //tomo las personas 
              	tabEmpleado= utilitario.consultar("SELECT ide_gtemp, "
              			+ "ide_geedp, "
              			+ "ide_geedp_cambio "
              			+ "FROM asi_horario_mes_empleado where ide_geedp in("+str_ide_empleado.toString()+") and ide_gemes="+mesTemp+" "
              					+ "and ide_geani="+tabAnio.getValor("IDE_GEANI"));
              	if (tabEmpleado.getTotalFilas()>0) {
			
         		for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) 
         		{
         		//Voy anidando los ides de la accion
          	    str_ide.append(tabEmpleado.getValor(i, "IDE_GTEMP"));
               // valor++;
                if (tabEmpleado.getTotalFilas()==1) {
   			}else if (valorEmp<=tabEmpleado.getTotalFilas()) {
   				valorEmp++;
   					if(valorEmp<(tabEmpleado.getTotalFilas())){
                    str_ide.append(",");
                    //System.out.println("str_ide:  "+str_ide);
   					}
    			}
    
   			 }
              	
            	}else if (bandArea==2) {
            		valor=0;
            		tabEmpleado= utilitario.consultar("SELECT ide_gtemp, "
                  			+ "ide_geedp, "
                  			+ "ide_geedp_cambio "
                  			+ "FROM asi_horario_mes_empleado where ide_gemes="+mesTemp+" "
                  			+ "and ide_geani="+tabAnio.getValor("IDE_GEANI"));
            		
            		
            		for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) 
             		{
             		//Voy anidando los ides de la accion
              	    str_ide.append(tabEmpleado.getValor(i, "IDE_GTEMP"));
                   // valor++;
                    if (tabEmpleado.getTotalFilas()==1) {
       			}else if (valorJefe<=tabEmpleado.getTotalFilas()) {
       					valorJefe++;
       					if(valorJefe<(tabEmpleado.getTotalFilas())){
                        str_ide.append(",");
                      //  System.out.println("str_ide:  "+str_ide);
       					}
        			}
        
       			 }
                  	
				}
              	else {
        			bandSinArea=false;
                    str_ide.append("-1");

				}	
			
		}else {
			bandSinArea=false;
            utilitario.agregarMensajeError("Usted no contiene permisos para esta pantalla", "Pongase en contacto con el Administrador");
            return;
		}
      	

   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       
       TablaGenerica tabEmpleadoXJefeInmediato=null,tabCargoJefeInmediato=null;
       
     ///VALIDACION ASIGNACION DE HXE JEFE INMEDIATO     	
     		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
         	TablaGenerica tabJefeInmediato=null;
         	
         	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,  "
         			+ "ide_gtemp_padre_asjei,cargo_padre_asjei "
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
                 
                 
                 TablaGenerica tabJefeInmediatoTemp=utilitario.consultar("SELECT ide_asjei, ide_gtemp, ide_geedp, tipo_asjei, ide_geare, activo_asjei, "
                 		+ "ide_gtemp_padre_asjei, cargo_padre_asjei  "
                 		+ "FROM asi_jefe_inmediato  "
                 		+ "where activo_asjei=true");
                 
                 
                 String int_num_col_idegetempmensualjefeinmediato="",int_num_col_jefeinmediato="";
                 for (int i = 0; i < tabJefeInmediatoTemp.getTotalFilas(); i++) {
                	 int_num_col_idegetempmensualjefeinmediato=tabJefeInmediatoTemp.getValor(i, "IDE_ASJEI");
                	 int_num_col_jefeinmediato=tabJefeInmediatoTemp.getValor(i, "IDE_GTEMP");
                	 if(str_ide_empleado_jefe_inmediato.toString().isEmpty()==false){
                		 str_ide_empleado_jefe_inmediato.append(",");
                		 str_ide_jefe_inmediato.append(",");
                	 }
                	 str_ide_empleado_jefe_inmediato.append(int_num_col_idegetempmensualjefeinmediato);
                	 str_ide_jefe_inmediato.append(int_num_col_jefeinmediato);
				}
                 
         		 
         		jefe_inmediato_planificacion=str_ide_empleado_jefe_inmediato.toString();
         		tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
         				+ "FROM asi_empleado_jefe_inmediato ");
         		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
         		meses=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
     			anios=utilitario.getVariable("p_anio_asignacion_horario_administrador");
     			mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
     			area=tabJefeInmediato.getValor("detalle_geare");
     			
     			tabCargoJefeInmediato=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
     			"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
     			"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
     			"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
     			"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
     			"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
     			"DEPA.DETALLE_GEDEP,GEPGC.titulo_cargo_gepgc "+
     			"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
     			"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
     			"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
     			"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
     			"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
     			+ "LEFT JOIN gen_partida_grupo_cargo GEPGC ON GEPGC.ide_gepgc=EPAR.ide_gepgc "
     			+ "where epar.ide_geare=+"+tabJefeInmediato.getValor("ide_geare")+"  and  "
     			+ "epar.ide_gegro in(4,8)"
     			+ "order by epar.ide_geedp desc,epar.ide_gegro asc limit 1" );
     			
     			//NombreEmpleadoJefe = obtenerNombresApellidosEmpleadoCorreo(tabCargoJefeInmediato.getValor("ide_gtemp"));				
     			//cargo_jefe_inmediato=tabCargoJefeInmediato.getValor("titulo_cargo_gepgc");
     			ide_gtemp_jefe_inmediato=tabCargoJefeInmediato.getValor("ide_gtemp");

     			NombreEmpleadoJefe = obtenerNombresApellidosEmpleadoCorreo(tabJefeInmediato.getValor("ide_gtemp_padre_asjei"));		
 				cargo_jefe_inmediato=tabJefeInmediato.getValor("cargo_padre_asjei");
     			
     			TablaGenerica tabCargoAprobador=utilitario.consultar("SELECT EMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
 	     				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
 	     				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
 	     				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
 	     				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
 	     				"GEPGC.titulo_cargo_gepgc "+
 	     				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
 	     				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
 	     				"LEFT JOIN gen_partida_grupo_cargo GEPGC ON GEPGC.ide_gepgc=EPAR.ide_gepgc "
 	     				+ "where epar.ide_gtemp="+tabJefeInmediato.getValor("ide_gtemp")+" "
 	     				+ "order by epar.ide_geedp desc limit 1" );
 	     	
     			NombreAprobador= obtenerNombresApellidosEmpleadoCorreo(tabJefeInmediato.getValor("ide_gtemp"));
 				cargo_elaborado=tabCargoAprobador.getValor("titulo_cargo_gepgc");
 				
     			
     			
         	}else{
         		
         		
         	
            tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
         				+ "FROM asi_empleado_jefe_inmediato "
         				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
     
     		String int_num_col_idegetempmensual;
     		for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
     			int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
      	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
      	  	str_ide_empleado_mensual.append(",");
              }
      	  str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
     		}
     		
     		
     		jefe_inmediato_planificacion=tabJefeInmediato.getValor("ide_asjei");

     		meses=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
     		anios=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
     		mesEditar=utilitario.getVariable("p_asi_mes_editar_horario_jefe_inmediato");
     		anioEditar=utilitario.getVariable("p_anio_asignacion_horario_jefe_inmediato");
     		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
     		area=tabJefeInmediato.getValor("detalle_geare");
     		//ide_gtemp_jefe_inmediato=tabJefeInmediato.getValor("ide_gtemp");
     		cargo_elaborado="COORDINADOR "+ide_geare;
     		
     		tabCargoJefeInmediato=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
     				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
     				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
     				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
     				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
     				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
     				"GEPGC.titulo_cargo_gepgc "+
     				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
     				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
     				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
     				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
     				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
     				+ "LEFT JOIN gen_partida_grupo_cargo GEPGC ON GEPGC.ide_gepgc=EPAR.ide_gepgc "
     				+ "where epar.ide_geare=+"+tabJefeInmediato.getValor("ide_geare")+"  and  "
     				+ "epar.ide_gegro in(4,8)"
     				+ "order by epar.ide_geedp desc limit 1" );
     				
     				NombreEmpleadoJefe = obtenerNombresApellidosEmpleadoCorreo(tabJefeInmediato.getValor("ide_gtemp_padre_asjei"));		
     				
     				cargo_jefe_inmediato=tabJefeInmediato.getValor("cargo_padre_asjei");
     				//ide_gtemp_jefe_inmediato=tabCargoJefeInmediato.getValor("ide_gtemp");

     				
     	     		TablaGenerica tabCargoAprobador=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
     	     				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
     	     				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
     	     				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
     	     				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
     	     				"GEPGC.titulo_cargo_gepgc "+
     	     				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
     	     				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
     	     				"LEFT JOIN gen_partida_grupo_cargo GEPGC ON GEPGC.ide_gepgc=EPAR.ide_gepgc "
     	     				+ "where epar.ide_gtemp=+"+tabJefeInmediato.getValor("ide_gtemp")+"  "
     	     				+ "order by epar.ide_geedp desc limit 1" );
     	     				
     				
     				NombreAprobador= obtenerNombresApellidosEmpleadoCorreo(tabJefeInmediato.getValor("ide_gtemp"));
     				cargo_elaborado=tabCargoAprobador.getValor("titulo_cargo_gepgc");
     				
         	}

       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////      
       
       
       

    	bar_botones.agregarComponente(new Etiqueta("OPCIONES DE HORAS EXTRA    "));
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	    	
    	  	//Boton Reporte de Empleado
    	Boton bot_rep_biometrico= new Boton();
    	bot_rep_biometrico.setIcon("ui-icon-calculator");
    	bot_rep_biometrico.setMetodo("seleccionarReporteTotal");
    	bot_rep_biometrico.setValue("CONSULTA HORAS EXTRA");
    	bot_rep_biometrico.setTitle("CONSULTA HORAS EXTRA");
    	bar_botones.agregarBoton(bot_rep_biometrico);
    	

    	
	bar_botones.agregarSeparador();
		
    	
    		// boton limpiar
    	Boton bot_alimentacion= new Boton();
    	bot_alimentacion.setIcon("ui-icon-calculator");
    	bot_alimentacion.setMetodo("abrirDialogoHoraExtra");
    	bot_alimentacion.setValue("ENVIAR HXE");
    	bot_alimentacion.setTitle("ENVIAR HXE");
    	if (tipo_perfil.equals("1")) {
    	bar_botones.agregarBoton(bot_alimentacion);
		}
    
    	
    	
   
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla(empleadosConsulta(jefe_inmediato_planificacion),"IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("APELLIDO_PATERNO_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("PRIMER_NOMBRE_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("SEGUNDO_NOMBRE_GTEMP").setFiltro(true);
		
    	set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_empleado);
		
    	
    	set_empleado_total.setId("set_empleado_total");
    	set_empleado_total.setSeleccionTabla(empleadosConsulta(jefe_inmediato_planificacion),"IDE_GTEMP");
    	set_empleado_total.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
    	set_empleado_total.getTab_seleccion().getColumna("APELLIDO_PATERNO_GTEMP").setFiltro(true);
    	set_empleado_total.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
    	set_empleado_total.getTab_seleccion().getColumna("PRIMER_NOMBRE_GTEMP").setFiltro(true);
    	set_empleado_total.getTab_seleccion().getColumna("SEGUNDO_NOMBRE_GTEMP").setFiltro(true);
    	
    	set_empleado_total.setTitle("Seleccione un Empleado");
    	set_empleado_total.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_empleado_total);
    	
		 if(tipo_perfil.equals("1")){
		set_empleado_asignados.setId("set_empleado_asignados");
		set_empleado_asignados.setSeleccionTabla(jefeConsulta(str_ide_jefe_inmediato.toString()),"IDE_GTEMP");
		set_empleado_asignados.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado_asignados.getTab_seleccion().getColumna("APELLIDO_PATERNO_GTEMP").setFiltro(true);
		set_empleado_asignados.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
		set_empleado_asignados.getTab_seleccion().getColumna("PRIMER_NOMBRE_GTEMP").setFiltro(true);
		set_empleado_asignados.getTab_seleccion().getColumna("SEGUNDO_NOMBRE_GTEMP").setFiltro(true);
		
		set_empleado_asignados.setTitle("Seleccione un Jefe Inmediato");
		set_empleado_asignados.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_empleado_asignados);
		 }
		set_empleado_reporte.setId("set_empleado_reporte");
    	set_empleado_reporte.setSeleccionTabla(empleadosConsulta(jefe_inmediato_planificacion),"IDE_GTEMP");
		set_empleado_reporte.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado_reporte.getTab_seleccion().getColumna("APELLIDO_PATERNO_GTEMP").setFiltro(true);
		set_empleado_reporte.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
		set_empleado_reporte.getTab_seleccion().getColumna("PRIMER_NOMBRE_GTEMP").setFiltro(true);
		set_empleado_reporte.getTab_seleccion().getColumna("SEGUNDO_NOMBRE_GTEMP").setFiltro(true);

		set_empleado_reporte.setTitle("Seleccione un Empleado");
		set_empleado_reporte.getBot_aceptar().setMetodo("seleccionarEnviar");
		agregarComponente(set_empleado_reporte);
    	
    	
    	//Boton Justificar
    	Boton bot_justificar= new Boton();
    	bot_justificar.setIcon("ui-icon-calculator");
    	bot_justificar.setMetodo("getMarcacionesEmpleadoHorasExtra");
    	bot_justificar.setValue("CALCULAR HORAS EXTRA");
    	bot_justificar.setTitle("CALCULAR HORAS EXTRA");
    	if (bandArea==2) {
    	bar_botones.agregarBoton(bot_justificar);
    	} 
       
		
    	
    	Boton bot_generar_por_empleado= new Boton();
    	bot_generar_por_empleado.setIcon("ui-icon-calculator");
    	bot_generar_por_empleado.setMetodo("getEmpleadoGenerar");
    	bot_generar_por_empleado.setValue("CALCULAR HORAS EXTRA POR EMPLEADO");
    	bot_generar_por_empleado.setTitle("CALCULAR HORAS EXTRA POR EMPLEADO");
    	bar_botones.agregarBoton(bot_generar_por_empleado);
    
    	
    	
    	
    	//Boton Generar horas Extra Nomina
	 	Boton bot_generarHorasNomina = new Boton();
	 	bot_generarHorasNomina.setIcon("ui-icon-cancel");
	 	bot_generarHorasNomina.setMetodo("generarHorasNominaXEmpleado");
	 	bot_generarHorasNomina.setValue("SUBIR HORAS EXTRA NÓMINA");
	 	bot_generarHorasNomina.setTitle("SUBIR HORAS EXTRA NÓMINA");
	 	if (bandArea==2) {
       	bar_botones.agregarBoton(bot_generarHorasNomina);   
	        	} 
       	
		sel_por_empleado.setId("sel_por_empleado");

		agregarComponente(sel_por_empleado);
       	
	 	
    	//Boton Limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar); 
    	
    	dia_fecha_marcaciones.setId("dia_fecha_marcaciones");
    	dia_fecha_marcaciones.setTitle("SELECCIONE FECHA");
    	dia_fecha_marcaciones.setWidth("20%");
    	dia_fecha_marcaciones.setHeight("15%");
    	dia_fecha_marcaciones.getBot_aceptar().setMetodo("aceptarReporte");
    	Grid gri_grid = new Grid();
    	gri_grid.getChildren().add(new Etiqueta("Fecha Inicio"));
    	gri_grid.getChildren().add(cal_fecha_inicial_marcaciones);
    	gri_grid.getChildren().add(new Etiqueta("Fecha Fin "));
    	gri_grid.getChildren().add(cal_fecha_final_marcaciones);
    	gri_grid.setStyle("height:" + (dia_fecha_marcaciones.getAltoPanel() - 10) + "px;overflow: auto;display: block;");
    	gri_grid.setColumns(2);
    	gri_grid.setWidth("98%");
    	dia_fecha_marcaciones.setDialogo(gri_grid);
    	agregarComponente(dia_fecha_marcaciones);
    	
		
    	dia_fecha_marcaciones_reporte.setId("dia_fecha_marcaciones_reporte");
    	dia_fecha_marcaciones_reporte.setTitle("SELECCIONE FECHA");
    	dia_fecha_marcaciones_reporte.setWidth("20%");
    	dia_fecha_marcaciones_reporte.setHeight("15%");
    	dia_fecha_marcaciones_reporte.getBot_aceptar().setMetodo("abrirDialogoEmpleadoHoraExtra");
    	Grid gri_grid1 = new Grid();
    	gri_grid1.getChildren().add(new Etiqueta("Fecha Inicio"));
    	gri_grid1.getChildren().add(cal_fecha_inicial_marcaciones_reporte);
    	gri_grid1.getChildren().add(new Etiqueta("Fecha Fin "));
    	gri_grid1.getChildren().add(cal_fecha_final_marcaciones_reporte);
    	gri_grid1.setStyle("height:" + (dia_fecha_marcaciones_reporte.getAltoPanel() - 10) + "px;overflow: auto;display: block;");
    	gri_grid1.setColumns(2);
    	gri_grid1.setWidth("98%");
    	dia_fecha_marcaciones_reporte.setDialogo(gri_grid1);
    	agregarComponente(dia_fecha_marcaciones_reporte);
    	
		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

    
		String mesHoraExtra=utilitario.getVariable("p_asi_mes_hora_extra");
		sel_mes.setId("sel_mes");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+mesHoraExtra+") ","IDE_GEMES");

    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("20");


		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.getBot_aceptar().setMetodo("abrirEmpleado");
		agregarComponente(sel_mes);
    	
    	
    	
	  	
			sel_empleado.setId("sel_empleado");
	    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes","IDE_GEMES");
	    	//sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes="+(utilitario.getMes(utilitario.getFechaActual())+1),"IDE_GEMES");
	    
			agregarComponente(sel_empleado);
    	
    	
    	
			tab_tabla_hora_extra.setId("tab_tabla_hora_extra");
			/*tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.horas25_loep_cobmr ,  "
					+ "res.horas60_loep_cobmr ,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.horas50_ct_cobmr ,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true   "  //and res.aprueba_hora_extra_cobmr=true
					//+ "and epar.ide_gtemp in("+str_ide.toString()+")  and "
					+ "and fecha_evento_cobmr between '"+fechaConsultaInicio+"' and '"+fechaConsultaFin+"'   "
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");*/
			
			
			
			
			

			tab_tabla_hora_extra.setSql("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "cobmr.fecha_evento_cobmr,  "
					+ "cobmr.dia_cobmr,  "
					//+ "res.actividades_cobph,  "
					//+ "res.productos_eperado_cobph,  "
					+ "cobmr.horainiciohorario_cobmr,  "
					+ "cobmr.horafinhorario_cobmr,   "
					+ "cobmr.horainiciobiometrico_cobmr,  "
					+ "cobmr.horafinbiometrico_cobmr,  "
					+ "cobmr.horas25_loep_cobmr ,   "
					+ "cobmr.horas60_loep_cobmr ,   "
					+ "cobmr.recargonocturno25_cobmr ,  	"
					+ "cobmr.horas50_ct_cobmr ,  "
					+ "cobmr.recargonocturno100_cobmr,  "
					+ "cobmr.dias_laborados_cobmr  "
					+ "FROM con_biometrico_marcaciones_resumen cobmr   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
					+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
					+ "where ide_cobmr=-1  "  
					//+ "where fecha_evento_cobmr between '2022-10-01' and '2022-10-31'   "  
					+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

			
				
			
			
			
			tab_tabla_hora_extra.setCampoPrimaria("ide_cobmr");
			tab_tabla_hora_extra.setLectura(true);

			
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").setLongitud(5);
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").alinearCentro();
			tab_tabla_hora_extra.getColumna("IDE_GTEMP").setNombreVisual("COD");


			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
			tab_tabla_hora_extra.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CÉDULA");
			

			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").alinearCentro();
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

			
			tab_tabla_hora_extra.getColumna("DIA_COBMR").setLongitud(15);
			tab_tabla_hora_extra.getColumna("DIA_COBMR").alinearCentro();
			tab_tabla_hora_extra.getColumna("DIA_COBMR").setNombreVisual("DIA");
	        
			tab_tabla_hora_extra.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
			tab_tabla_hora_extra.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
	        
	        
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").setNombreVisual("ENTRADA");
			tab_tabla_hora_extra.getColumna("horainiciohorario_cobmr").alinearCentro();

	        
	        
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").setNombreVisual("TIMBRE");
			tab_tabla_hora_extra.getColumna("horainiciobiometrico_cobmr").alinearCentro();

	        
        
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").setNombreVisual("SALIDA");
			tab_tabla_hora_extra.getColumna("horafinhorario_cobmr").alinearCentro();

	       
	        
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").setLongitud(15);
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").setNombreVisual("TIMBRE SALIDA");
			tab_tabla_hora_extra.getColumna("horafinbiometrico_cobmr").alinearCentro();

			tab_tabla_hora_extra.getColumna("horas25_loep_cobmr").setLongitud(45);
			tab_tabla_hora_extra.getColumna("horas25_loep_cobmr").setNombreVisual("HORAS AL 25% LOEP");
			tab_tabla_hora_extra.getColumna("horas25_loep_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("horas25_loep_cobmr").setDecimales(2);
			
			tab_tabla_hora_extra.getColumna("horas60_loep_cobmr").setLongitud(45);
			tab_tabla_hora_extra.getColumna("horas60_loep_cobmr").setNombreVisual("HORAS AL 60% LOEP");
			tab_tabla_hora_extra.getColumna("horas60_loep_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("horas60_loep_cobmr").setDecimales(2);
			
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setLongitud(45);
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setNombreVisual("HORAS AL 25% CT");
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("recargonocturno25_cobmr").setDecimales(2);
	        
			tab_tabla_hora_extra.getColumna("horas50_ct_cobmr").setLongitud(55);
			tab_tabla_hora_extra.getColumna("horas50_ct_cobmr").setNombreVisual("HORAS AL 50% CT");
			tab_tabla_hora_extra.getColumna("horas50_ct_cobmr").alinearCentro();
		
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").setLongitud(55);
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").alinearCentro();
			tab_tabla_hora_extra.getColumna("recargonocturno100_cobmr").setNombreVisual("HORAS AL 100% CT Y LOEP"); 

	                        
			//tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").setLongitud(25);
			//tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").alinearCentro();
			//tab_tabla_hora_extra.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMPLEADO");  
	        
			//tab_tabla_hora_extra.onSelect("actualizarSumatoriaHorasExtra");
			tab_tabla_hora_extra.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
			tab_tabla_hora_extra.getColumna("DIA_COBMR").setFiltro(true);
			tab_tabla_hora_extra.getColumna("FECHA_EVENTO_COBMR").setFiltro(true);

			tab_tabla_hora_extra.setRows(15);
			tab_tabla_hora_extra.dibujar();
			tab_tabla_hora_extra.setHeader("DETALLE HORAS EXTRA POR EMPLEADO");

	        PanelTabla pat_panel = new PanelTabla();
	        pat_panel.setPanelTabla(tab_tabla_hora_extra);
    	
    	
   		
			
			tab_tabla_sumatoria.setId("tab_tabla_sumatoria");
			//tab_tabla_sumatoria.setSql(getSumatoriaAtrasos(1,fechaConsultaInicio,fechaConsultaFin,str_ide.toString()));
	        
			tab_tabla_sumatoria.setTabla("asi_resumen_horas_extra", "ide_asrhe", 1);
			tab_tabla_sumatoria.setTipoFormulario(true);
			tab_tabla_sumatoria.setCampoPrimaria("ide_asrhe");
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setCombo("SELECT EMP.IDE_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
			+ "FROM GTH_EMPLEADO EMP ");
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setAutoCompletar();
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setLongitud(50);
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").alinearCentro();
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO");
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setFiltroContenido();
			tab_tabla_sumatoria.getColumna("IDE_GTEMP").setOrden(1);
			
			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setOrden(2);
			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(17);
			tab_tabla_sumatoria.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltroContenido();


			tab_tabla_sumatoria.getColumna("horas25_loep_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("horas25_loep_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("horas25_loep_asrhe").setNombreVisual("HORAS AL 25% LOEP");
			tab_tabla_sumatoria.getColumna("horas25_loep_asrhe").setOrden(3);
			tab_tabla_sumatoria.getColumna("horas60_loep_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("horas60_loep_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("horas60_loep_asrhe").setNombreVisual("HORAS AL 60% LOEP");
			tab_tabla_sumatoria.getColumna("horas60_loep_asrhe").setOrden(4);
			tab_tabla_sumatoria.getColumna("recargonocturno25_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("recargonocturno25_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("recargonocturno25_asrhe").setNombreVisual("HORAS AL 25% CT");
			tab_tabla_sumatoria.getColumna("recargonocturno25_asrhe").setOrden(5);
			tab_tabla_sumatoria.getColumna("horas50_ct_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("horas50_ct_asrhe").alinearCentro();
			
			tab_tabla_sumatoria.getColumna("horas50_ct_asrhe").setNombreVisual("HORAS AL 50% CT");
			tab_tabla_sumatoria.getColumna("horas50_ct_asrhe").setOrden(6);

			
			tab_tabla_sumatoria.getColumna("recargonocturno100_asrhe").setLongitud(25);
			tab_tabla_sumatoria.getColumna("recargonocturno100_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("recargonocturno100_asrhe").setNombreVisual("HORAS AL 100% CT Y LOEP");  
			tab_tabla_sumatoria.getColumna("recargonocturno100_asrhe").setOrden(7);

			
			tab_tabla_sumatoria.getColumna("observacion_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("observacion_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("observacion_asrhe").setNombreVisual("OBSERVACION");
			tab_tabla_sumatoria.getColumna("observacion_asrhe").setOrden(8);
			

			tab_tabla_sumatoria.getColumna("mes_asrhe").setCombo("SELECT IDE_GEMES,DETALLE_GEMES "
					+ "FROM GEN_MES ");
			tab_tabla_sumatoria.getColumna("mes_asrhe").setAutoCompletar();
	        
			tab_tabla_sumatoria.getColumna("mes_asrhe").setLongitud(7);
			tab_tabla_sumatoria.getColumna("mes_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("mes_asrhe").setNombreVisual("MES");
			tab_tabla_sumatoria.getColumna("mes_asrhe").setLectura(true);
			tab_tabla_sumatoria.getColumna("mes_asrhe").setOrden(9);
	    	
			tab_tabla_sumatoria.getColumna("ide_geani").setCombo("SELECT IDE_GEANI,DETALLE_GEANI "
					+ "FROM GEN_ANIO ");
			tab_tabla_sumatoria.getColumna("ide_geani").setAutoCompletar();
			tab_tabla_sumatoria.getColumna("ide_geani").setLongitud(7);
			tab_tabla_sumatoria.getColumna("ide_geani").alinearCentro();
			tab_tabla_sumatoria.getColumna("ide_geani").setNombreVisual("ANIO");
			tab_tabla_sumatoria.getColumna("ide_geani").setLectura(true);
			tab_tabla_sumatoria.getColumna("ide_geani").setOrden(10);

			tab_tabla_sumatoria.getColumna("aprobacion_asrhe").setCheck();
			tab_tabla_sumatoria.getColumna("aprobacion_asrhe").setOrden(11);

			
			
			tab_tabla_sumatoria.getColumna("activo_asrhe").setValorDefecto("true");
			tab_tabla_sumatoria.getColumna("activo_asrhe").setLectura(true);
			tab_tabla_sumatoria.getColumna("activo_asrhe").setOrden(12);


			tab_tabla_sumatoria.getColumna("horafinextra_asrhe").setLongitud(15);
			tab_tabla_sumatoria.getColumna("horafinextra_asrhe").alinearCentro();
			tab_tabla_sumatoria.getColumna("horafinextra_asrhe").setVisible(false);
			tab_tabla_sumatoria.getColumna("horafinextra_asrhe").setOrden(13);
			
			
			tab_tabla_sumatoria.getColumna("fecha_inicio_asrhe").setVisible(false);
			tab_tabla_sumatoria.getColumna("fecha_inicio_asrhe").setOrden(14);

			tab_tabla_sumatoria.getColumna("fecha_fin_asrhe").setVisible(false);
			tab_tabla_sumatoria.getColumna("fecha_fin_asrhe").setOrden(15);

	        
			tab_tabla_sumatoria.setCondicion("mes_asrhe=-1");
		//	tab_tabla_sumatoria.setCondicion("mes_asrhe="+(utilitario.getMes(cal_fecha_inicial.getFecha())-1));

			tab_tabla_sumatoria.setLectura(true);
			tab_tabla_sumatoria.setRows(5);
			tab_tabla_sumatoria.dibujar();
			tab_tabla_sumatoria.setHeader("RESUMEN HORAS EXTRA POR EMPLEADO");
			PanelTabla pat_panel11 = new PanelTabla();
		    pat_panel11.setPanelTabla(tab_tabla_sumatoria);
		        
		    
		        
		        Division div_division = new Division();
		        div_division.setId("div_division");
		        div_division.dividir2(pat_panel,pat_panel11,"75%","H");
		        agregarComponente(div_division);
			
		        
		      //  utilitario.getConexion().ejecutarSql("delete from asi_resumen_horas_extra");
		        //cuatroUno(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha(),1,str_ide.toString());

        
        
    }

    
  
    
    public void seleccionarReporteTotal(){
    	
    
    	
	  
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
 		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
 			fechaIni=(cal_fecha_inicial.getFecha());	
     		fechaFin=(cal_fecha_final.getFecha());
     		String str_ide_empleados="-1";
     		int mesIni=utilitario.getMes(cal_fecha_inicial.getFecha());
    		int mesFin=utilitario.getMes(cal_fecha_final.getFecha());
    		int anioIni=utilitario.getAnio(cal_fecha_inicial.getFecha());
    		int anioFin=utilitario.getAnio(cal_fecha_final.getFecha());


    		if (anioIni==anioFin) {
    			if (mesIni==mesFin) {
    				
    			}else {
    				utilitario.agregarMensajeInfo("Debe seleccionar un solo mes a la vez", "Debe coincidir el mes de la Fecha Inicio y Fin");
    				return;
    			}	
    		}else {
    			utilitario.agregarMensajeInfo("Debe seleccionar un solo anio a la vez", "Debe coincidir el anio de la Fecha Inicio y Fin");
    			return;
    		}
    		TablaGenerica tabAnioConsulta=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");
     		
     		
     		str_ide_empleados=retornaEmpleadosACargo(fechaIni,fechaFin.toString());
     		if (str_ide_empleados.equals("ERROR")) {
     			utilitario.agregarMensajeInfo("Usted no contiene permisos", "Pongase en contacto con el Administrador");
     			return;		
			}
 		
     		getMarcacionesEmpleadoHorasExtraConsulta();
     		
/*     		tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "					
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.horas25_loep_cobmr ,  "
					+ "res.horas60_loep_cobmr ,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.horas50_ct_cobmr ,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
					//+ "and epar.ide_gtemp in("+str_ide_empleados+") "
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
			




tab_tabla_hora_extra.setSql("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		+ "cobmr.ide_cobmr,  "
		+ "cobmr.fecha_evento_cobmr,  "
		+ "cobmr.dia_cobmr,  "
		+ "res.actividades_cobph,  "
		+ "cobmr.horainiciohorario_cobmr,  "
		+ "cobmr.horafinhorario_cobmr,   "
		+ "cobmr.horainiciobiometrico_cobmr,  "
		+ "cobmr.horafinbiometrico_cobmr,  "
		+ "cobmr.horas25_loep_cobmr ,   "
		+ "cobmr.horas60_loep_cobmr ,   "
		+ "cobmr.recargonocturno25_cobmr ,  	"
		+ "cobmr.horas50_ct_cobmr ,  "
		+ "cobmr.recargonocturno100_cobmr  "
		+ "FROM con_biometrico_marcaciones_resumen cobmr   "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
		+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
		+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "  
		//+ "and cobmr.ide_gtemp in("+str_ide_empleados+") "
		+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,cobmr.fecha_evento_cobmr asc");




			
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla_hora_extra");
			if (tab_tabla_hora_extra.getTotalFilas()<=0) {
				utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"No existen registros para las fechas seleccionadas");
				return;
			}
			
		
			//cuatroUno("","",1,str_ide.toString());
			tab_tabla_sumatoria.setCondicion("mes_asrhe="+mesIni+" AND ide_geani="+tabAnioConsulta.getValor("IDE_GEANI")+" AND IDE_GTEMP IN("+str_ide.toString()+") ");
			tab_tabla_sumatoria.ejecutarSql();
			utilitario.addUpdate("tab_tabla_sumatoria");
		
 		*/
 		
 		
 		}	 else {
			utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Fecha inicial debe ser menor a fecha final");
			return;
		
	} 	
	 	
  		}else {
				utilitario.agregarMensajeInfo("Rango de fechas inválidos",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
		}
    
    
    }
    
    
    
    
     public void seleccionarEmpleado(){
    
    		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    			
        		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
            		set_empleado.dibujar();
        		}else {
    				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

    			}
        		
    		}
    		else {
    			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
    		}		
        	
       }
    

	
	
    public void seleccionarDepartamento(){
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
			
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
    
    	
    	set_departamento.dibujar();

		}else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

		}
		
	}
	else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
	}		
    
 }
    

	
	
    @SuppressWarnings("unused")
	

  
  private TablaGenerica obtenerHorariosTurnoEmpleado(Integer ide_ashor){
    	
    	TablaGenerica horarioXEmpleado=utilitario.consultar("select hor.ide_astur,turno.ide_gtgre from asi_turnos_horario hor "
    			+ "left join asi_turnos turno on turno.ide_astur=hor.ide_astur "
    			+ " where hor.ide_ashor="+ide_ashor);
    	return horarioXEmpleado;
    	
    }
    
   private TablaGenerica obtenerTurnos(){
    	
    	TablaGenerica obtenerHorarios = utilitario.consultar("select ide_ashor,ide_astur from asi_turnos_horario");
    	return obtenerHorarios;
    	
    }
    
    

 	public TablaGenerica getEmpleadosDepartamento(String IDE_GEDEP){
  		return utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEDEP IN ("+IDE_GEDEP+") AND ACTIVO_GEEDP=TRUE");
  	}
    

    
public TablaGenerica  obtenerPermisoXEmpleadoEntrada(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
        + "from asi_permisos_vacacion_hext where "
  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
  	    + "and hora_desde_aspvh >= '"+horaInicio+"' ");
  
  obtenerPermisos.imprimirSql();
  return obtenerPermisos;
 	
}
 	



public TablaGenerica  obtenerPermisoXEmpleadoSalida(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
    TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
    + "from asi_permisos_vacacion_hext where "
		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
	    + "and hora_hasta_aspvh <= '"+horaInicio+"' ");

obtenerPermisos.imprimirSql();
return obtenerPermisos;
	
}
	


 	public void limpiar(){
 	tab_tabla_hora_extra.limpiar();
 	tab_tabla_sumatoria.limpiar();
 		utilitario.addUpdate("tab_tabla_hora_extra,tab_tabla_sumatoria");
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

    
    
    @Override
    public void insertar() {
		
	  
    }

    @Override
    public void guardar() {
  
		if (tab_tabla_sumatoria.guardar()){
            guardarPantalla();
		}

    	
    }

    @Override
    public void eliminar() {
        //tab_tabla.eliminar();
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

	
    private TablaGenerica obtenerHorariosEmpleado(Integer ide_getemp){
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
    	
    	
   	 public void updateTablaResumen(
   			 int ide_cobmr,
   			 String horainiciobiometrico_cobmr,
			 String horainicioband_cobmr,
			 String horainicioalmbio_cobmr,
			 String horafinalmbio_cobmr,
			 String tiempoalm_cobmr,
			 String horafinbiometrico_cobmr,
			 String horafinband_cobmr){
	
			utilitario.getConexion().ejecutarSql(""
					+ "UPDATE con_biometrico_marcaciones_resumen set "
					+ "horainiciobiometrico_cobmr='"+horainiciobiometrico_cobmr+"', " 
					+ "horainicioband_cobmr='"+horainicioband_cobmr+"', "
					+ "horainicioalmbio_cobmr='"+horainicioalmbio_cobmr+"',"
					+ "horafinalmbio_cobmr='"+horafinalmbio_cobmr+"',"
					+ "tiempoalm_cobmr='"+tiempoalm_cobmr+"',"
					+ "horafinbiometrico_cobmr='"+horafinbiometrico_cobmr+"',"
					+ "horafinband_cobmr='"+horafinband_cobmr+"' "
					+ "where ide_cobmr="+ide_cobmr+""); 

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

  //Sumar horas en fecha
  	 public Date sumarRestarHorasFecha(Date fecha, int horas){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(fecha); // Configuramos la fecha que se recibe
       calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
       return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

  	 }




    	private double obtenerDiferenciaMinutosTotal(Date fechaInicial, Date fechaFinal){
            double diferencia=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/1000);
            double horaExtra=0.00;
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
    		
    		
    		double tiempoHorasExtra100Auxiliar = total_diferencia_minutos;
    		 //double valorHoraDescanso=Double.parseDouble(horaDescanso); 
    		 String p="";
    		int pt_entera=(int)tiempoHorasExtra100Auxiliar;
    		double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;

    		if ((int)pt_decimal<10 && pt_decimal>-1) {
    			p="0.0"+(int)pt_decimal;
    		}else if ((int)pt_decimal<=-1) {
    			p="0.0";
    		}
    		else {
    			p="0."+(int)pt_decimal;
    		}

    		double p1=Double.parseDouble(p);
    		horaExtra=pt_entera+p1;							
    		if (horaExtra>=0) {
    			horaExtra=horaExtra/60;		
    		}
    		else {
    			horaExtra=0.00;
    		}	

    		///////////////////////////////////////////////////////////////////////////////////

    		double pt_decimal1=(horaExtra-((int)horaExtra))*60;
    		String p2="";
    		if ((int)pt_decimal1<10 && pt_decimal1>-1) {
    			p2="0.0"+(int)pt_decimal1;
    		}else if ((int)pt_decimal1<=-1) {
    			p2="0.0";
    		}
    		else {
    			p2="0."+(int)pt_decimal1;
    		}

    		double p3=Double.parseDouble(p2);
   			horaExtra=(int)horaExtra+p3;
    		return horaExtra;
    		
        }







	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new  SimpleDateFormat ("hh:mm:ss");

			String hora1 = utilitario.getFormatoHora(hora_ini);
			String hora2 = utilitario.getFormatoHora(hora_fin);

			int int_hora1=pckUtilidades.CConversion.CInt(hora1.replaceAll(":", ""));
			int int_hora2=pckUtilidades.CConversion.CInt(hora2.replaceAll(":", ""));


			if(int_hora1>int_hora2){
				return true;
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	



	public void actualizarMarcaciones(){

	
		
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null && aut_empleados.getValor()!=null) {
			if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
	    		String fechaIni=(cal_fecha_inicial.getFecha());	
	    		String fechaFin=(cal_fecha_final.getFecha());	
	    		tab_tabla_hora_extra.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
	       				+ "and ide_gtemp='"+aut_empleados.getValor()+"' and aprueba_hora_extra_cobmr=true ");
	    		tab_tabla_hora_extra.ejecutarSql();
    		utilitario.addUpdate("tab_tabla_hora_extra");
    		cuatroUno("","",1,str_ide.toString());
    		tab_tabla_sumatoria.setCondicion(" WHERE mes_asrhe=10 AND ide_geani=11 AND IDE_GTEMP IN("+str_ide.toString()+") ");
			tab_tabla_sumatoria.ejecutarSql();
			utilitario.addUpdate("tab_tabla_sumatoria");
			
    		
    		
    	 	
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
				
			}
		}
		

	}
	
	
	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
		actualizarMarcaciones();	
	}



	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}



	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
	
	
	




public TablaGenerica  obtenerPermisoXEmpleado(String FechaInicio, String FechaFin,String horaInicio, String horaFin,  Integer IDE_GTEMP){
    TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
    + "from asi_permisos_vacacion_hext where "
		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaFin+"' and ide_gtemp="+IDE_GTEMP+" "
	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"'");

obtenerPermisos.imprimirSql();
return obtenerPermisos;
}


private TablaGenerica obtenerMarcacionesEmpleado(Integer ide_gtemp){
	TablaGenerica marcacionesXEmpleado=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr");
	return marcacionesXEmpleado;
	
	
}



public String getConsultaHorario(String ide_astur){
	String tab_asi_vacacion="select  "
			+ "turhor.ide_astuh, "
			+ "hor.ide_ashor, "
			+ "hor.nom_ashor, "
			+ "hor.hora_inicial_ashor, "
			+ "hor.hora_final_ashor, "
			+ "hor.hora_inicio_almuerzo_ashor, "
			+ "hor.hora_fin_almuerzo_ashor,  "
			+ "hor.min_almuerzo_ashor "
			+ "from asi_turnos_horario turhor "
			+ "left join asi_horario hor on hor.ide_ashor=turhor.ide_ashor "
			+ "where turhor.ide_astur="+ide_astur;
			 return tab_asi_vacacion;		
}

  


public void getMarcacionesEmpleadoHorasExtra(){ 
	tab_tabla_hora_extra.setLectura(false);
	int contador = 0;

	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;
String apobracionHoras100="";
String apobracionHoraNormal="";
	 String fechaBiometrico="";
	
//	Tabla que contiene las fechas de timbre del empleado	

	 
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
  		fechaIni=(cal_fecha_inicial.getFecha());	
  		fechaFin=(cal_fecha_final.getFecha());
  		//cuatroUno_(fechaIni,fechaFin,1,str_ide.toString());
	
  		int mesIni=utilitario.getMes(cal_fecha_inicial.getFecha());
		int mesFin=utilitario.getMes(cal_fecha_final.getFecha());
		int anioIni=utilitario.getAnio(cal_fecha_inicial.getFecha());
		int anioFin=utilitario.getAnio(cal_fecha_final.getFecha());

		if (anioIni==anioFin) {
			if (mesIni==mesFin) {
				
			}else {
				utilitario.agregarMensajeInfo("Debe seleccionar un solo mes a la vez", "Debe coincidir el mes de la Fecha Inicio y Fin");
				return;
			}	
		}else {
			utilitario.agregarMensajeInfo("Debe seleccionar un solo anio a la vez", "Debe coincidir el anio de la Fecha Inicio y Fin");
			return;
		}
		TablaGenerica tabAnioConsulta=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");

	
		tab_tabla_hora_extra.setSql("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "cobmr.fecha_evento_cobmr,  "
				+ "cobmr.dia_cobmr,  "
				+ "res.actividades_cobph,  "
				+ "res.productos_eperado_cobph,  "
				+ "cobmr.horainiciohorario_cobmr,  "
				+ "cobmr.horafinhorario_cobmr,   "
				+ "cobmr.horainiciobiometrico_cobmr,  "
				+ "cobmr.horafinbiometrico_cobmr,  "
				+ "cobmr.horas25_loep_cobmr ,   "
				+ "cobmr.horas60_loep_cobmr ,   "
				+ "cobmr.recargonocturno25_cobmr ,  	"
				+ "cobmr.horas50_ct_cobmr ,  "
				+ "cobmr.recargonocturno100_cobmr,  "
				+ "cobmr.dias_laborados_cobmr  "
				+ "FROM con_biometrico_marcaciones_resumen cobmr   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
				+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   " 
			   // + "AND EMP.IDE_GTEMP in(1021,933,1195,1062) "  
			 //   + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) " 
			   
				
				
				
			    

//1062,194,193,
				+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

		tab_tabla_hora_extra.ejecutarSql();
		 utilitario.addUpdate("tab_tabla_hora_extra");
		
		 
		/*TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr,  "
				+ "horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
				+ "horafinband_cobmr, horafinextra_cobmr, recargonocturno25_cobmr, "
				+ "recargonocturno100_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr, "
				+ "dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,   "
				+ "ide_aspvh "
			//+ "FROM con_biometrico_marcaciones_resumen  where  fecha_evento_cobmr between '2018-04-01' and '2018-04-01' and novedad_cobmr =true  and ide_gtemp=284 "
			+ "FROM con_biometrico_marcaciones_resumen  where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' and novedad_cobmr =true "
			+ "order by ide_gtemp asc");*/
		 
		//and ide_gtemp in("+set_empleado.getSeleccionados()+")

		
	/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=false "
				+ "where fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");*/

		//and novedad_cobmr =false
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
							///////////////////////////////////////////empleados//////////////////////////////////////////////////////
		
		/*String empleados_a_consultar="";
		StringBuilder str_ide_empleado = new StringBuilder();
		StringBuilder str_ide_empleado_mensual = new StringBuilder();

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
		+ "where ide_geani="+tabAnioConsulta.getValor("ide_geani")+" and ide_gemes="+utilitario.getMes(fechaIni)+" and ide_gtemp in( "
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

		if (tab_empleados_horario_mensual.getTotalFilas()>0) {
	String int_num_col_idegetemp="";
			 for (int i = 0; i < tab_empleados_horario_mensual.getTotalFilas(); i++) {
					int_num_col_idegetemp=tab_empleados_horario_mensual.getValor(i, "IDE_GTEMP");
	          	  	if(str_ide_empleado_mensual.toString().isEmpty()==false){
	          	  	str_ide_empleado_mensual.append(",");
	                  }
	          	  str_ide_empleado_mensual.append(int_num_col_idegetemp);
	          }
				empleados_a_consultar=str_ide_empleado+","+str_ide_empleado_mensual;
		}else {
			empleados_a_consultar=""+str_ide_empleado;
	
		}*/
	
		
		
		
		
		
		

	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anioIni+")");
	   	
	
	
		
		TablaGenerica tab_plnificacion_hxe=utilitario.consultar("SELECT ide_gtemp, sum(horas25_loep_cobph)+ sum(horas60_loep_cobph) + sum(horas25_ct_cobph) + sum(horas50_ct_cobph) + sum(horas100_loep_ct_cobph) as TOTAL  "
				+ "FROM con_biometrico_plan_hxe "
				+ "WHERE ide_gemes="+mesIni+" and ide_geani="+tab_anio.getValor("ide_geani")
				+ " GROUP BY ide_gtemp  "
				+ "HAVING sum(horas25_loep_cobph)+ sum(horas60_loep_cobph) + sum(horas25_ct_cobph) + sum(horas50_ct_cobph) + sum(horas100_loep_ct_cobph) >0 "
				+ "ORDER BY ide_gtemp ASC");
	
		StringBuilder str_ide_empleado_ = new StringBuilder();

		String int_num_col_idegetemp="";
		 for (int i = 0; i < tab_plnificacion_hxe.getTotalFilas(); i++) {
				int_num_col_idegetemp=tab_plnificacion_hxe.getValor(i, "IDE_GTEMP");
         	  	if(str_ide_empleado.toString().isEmpty()==false){
                         str_ide_empleado.append(",");
                  }
                 str_ide_empleado.append(int_num_col_idegetemp);
         }
	
		 
		 
			///////////////////////////////////////////empleados//////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
//+ " novedad_cobmr =true  where horainicioband_cobmr like '%OK%' and horafinband_cobmr like '%OK%' "
//+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+str_ide_empleado+") ");


//and ide_gtemp in("+set_empleado.getSeleccionados()+")
//and novedad_cobmr =false	

//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
//+ " novedad_cobmr =true  where horainicioband_cobmr like '%ATRASADO%' and horafinband_cobmr like '%OK%' "
//+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+str_ide_empleado+") ");


//and ide_gtemp in("+set_empleado.getSeleccionados()+")
//and novedad_cobmr =false
//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
//+ " novedad_cobmr =true  where horainicioband_cobmr like '%EXTRA%' and horafinband_cobmr like '%EXTRA%' "
//+ " and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' and ide_gtemp in("+str_ide_empleado+") ");
		 
		 
//FERIADO
		 
		 
		//utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
		//+ "where ide_geani ="+15+" and mes_asrhe="+10+" ");
		//+ "AND ide_gtemp in("+str_ide_empleado+") ");
		 
		 

		 
		 
		 
		 TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov "
					+ "from asi_novedad  "
					+ "WHERE FECHA_INICIO_ASNOV >= '"+cal_fecha_inicial.getFecha()+"' AND  FECHA_FIN_ASNOV <='"+cal_fecha_final.getFecha()+"' "
					+ "and dia_feriado_asnov=true");

			
			String fechaInicioAsnov="",fechaFinAsnov="";
			
			if (tab_novedad.getTotalFilas()>0) {
				for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
					fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
					fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
					getFeriadoXFecha(fechaInicioAsnov, fechaFinAsnov);
				}		
			}
			
		 
		 


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


			TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "cobmr.fecha_evento_cobmr,  "
					+ "cobmr.dia_cobmr,  "
					+ "res.actividades_cobph,  "
					+ "res.productos_eperado_cobph,  "
					+ "cobmr.horainiciohorario_cobmr,  "
					+ "cobmr.horafinhorario_cobmr,   "
					+ "cobmr.horainiciobiometrico_cobmr,  "
					+ "cobmr.horafinbiometrico_cobmr,  "
					+ "cobmr.horas25_loep_cobmr ,   "
					+ "cobmr.horas60_loep_cobmr ,   "
					+ "cobmr.recargonocturno25_cobmr ,  	"
					+ "cobmr.horas50_ct_cobmr ,  "
					+ "cobmr.tiempohoralm_cobmr ,  "
					+ "cobmr.horainicioalm_cobmr ,  "
					+ "cobmr.horainicioband_cobmr ,  "
					+ "cobmr.horafinband_cobmr ,  "
					+ "cobmr.horainiciohorariohxe_cobmr ,  "
					+ "cobmr.horafinhorariohxe_cobmr ,  "
					+ "cobmr.recargonocturno100_cobmr,  "
					+ "cobmr.dias_laborados_cobmr  "
					+ "FROM con_biometrico_marcaciones_resumen cobmr   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
					+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
					+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
					//+ "AND EMP.IDE_GTEMP in(1021,933,1195,1062) "  
					//			    + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) "  
//1062,194,193,
					+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

					
					//+ "	 ide_gtemp in(SELECT  ide_gtemp  FROM con_biometrico_plan_hxe where ide_geani=15 and ide_gemes=10)  "
					//+ "	 order by ide_gtemp asc, fecha_evento_cobmr asc  ");

		
			

			
		 
			//296,198,138,189,
		
		//522 peña ligña
		//296 loja yupa
		//188 cachacago ramiro  MANTENIMIENTO
		// 149 morales cumbal diego  CONTROL DIARIO

		
		
		
		//str_ide_empleado
		
		
		if (tabConBiometricoMarcacionesResumen.getTotalFilas()<=0) {
			utilitario.agregarMensajeInfo("Rango de Fechas  Invalidas",	"No existen datos para el rango seleccionado");
			return;
		}else{
		
			double horas25_loep_cobph=0.00,horas60_loep_cobph=0.00,horas25_ct_cobph=0.00,horas50_ct_cobph=0.00,horas100_loep_ct_cobph=0.00;
		
			
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " recargonocturno25_cobmr ='',recargonocturno100_cobmr='',horafinextra_cobmr='',horas25_loep_cobmr='',horas60_loep_cobmr='',horas50_ct_cobmr=''  where "
+ " fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");

			

		
			for (int itemReporte=0; itemReporte< tabConBiometricoMarcacionesResumen.getTotalFilas(); itemReporte++) {
				Integer ide_gtemp = Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(itemReporte, "IDE_GTEMP"));
				Integer tipoEmpleado=getTipoEmpleado(ide_gtemp);//1 TIPO CODIGO DE TRABAJO Y 2 LOEP
				String fechaBiometricoAgrupadaXEmpleado = tabConBiometricoMarcacionesResumen.getValor(itemReporte,"fecha_evento_cobmr");
				Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
				String fechaHoraBiometrico = fechaBiometricoAgrupadaXEmpleado+" 01:00:00";
				Calendar calFechaHoraBiometrico = Calendar.getInstance();
				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 
				//Obtengo datos de marcaciones de Entrada y Salida por empleado

			    String fecha=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"FECHA_EVENTO_COBMR");
		        String horarioEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOHORARIO_COBMR");
			    String horaEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOBIOMETRICO_COBMR");
			    String horarioSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINHORARIO_COBMR");
			    String horaSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINBIOMETRICO_COBMR");
			    String empleado=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"ide_gtemp");
		        String horaDescanso=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"tiempohoralm_cobmr");
		        String horaSinAlmuerzo=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioalm_cobmr");
		        
		        
		        
		        
		        String estadoEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioband_cobmr");
		        String estadoSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinband_cobmr");
		        String horainiciohorariohxe_cobmr=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainiciohorariohxe_cobmr");
		        String horafinhorariohxe_cobmr =tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinhorariohxe_cobmr");
		        
		        boolean banderaAtraso=false,banderaAtrasoSalida=false;
		        System.out.println("EMPLEADO : "+empleado+" fecha : "+fecha);
				 double tiempoHorasExtra25=0.0;
		        boolean sinAlmHorario=false;
		        
		 		TablaGenerica tabAprobacion = this.retornaAprobacionHorasExtra(empleado, utilitario.getMes(fecha));
		 		
		 		
		 
		        
				
				if (tabAprobacion.getTotalFilas()<=0) {
				}else {
					horas25_loep_cobph=0.00;
					horas60_loep_cobph=0.00;
					horas25_ct_cobph=0.00;
					horas50_ct_cobph=0.00;
					horas100_loep_ct_cobph=0.00;
				for (int i = 0; i < tabAprobacion.getTotalFilas(); i++) {
					if (tabAprobacion.getValor(i, "horas25_loep_cobph")==null || tabAprobacion.getValor(i, "horas25_loep_cobph").equals("") || tabAprobacion.getValor(i, "horas25_loep_cobph").isEmpty()) {
						horas25_loep_cobph=0.00;
					}else {
						horas25_loep_cobph=Double.parseDouble(tabAprobacion.getValor(i, "horas25_loep_cobph"));
						}

					if (tabAprobacion.getValor(i, "horas60_loep_cobph")==null || tabAprobacion.getValor(i, "horas60_loep_cobph").equals("") || tabAprobacion.getValor(i, "horas60_loep_cobph").isEmpty()) {
						horas60_loep_cobph=0.00;
					}else {
						horas60_loep_cobph=Double.parseDouble(tabAprobacion.getValor(i, "horas60_loep_cobph"));
					}

					if (tabAprobacion.getValor(i, "horas25_ct_cobph")==null || tabAprobacion.getValor(i, "horas25_ct_cobph").equals("") || tabAprobacion.getValor(i, "horas25_ct_cobph").isEmpty()) {
						horas25_ct_cobph=0.00;
					}else {
						horas25_ct_cobph=Double.parseDouble(tabAprobacion.getValor(i, "horas25_ct_cobph"));
					}
					
					if (tabAprobacion.getValor(i, "horas50_ct_cobph")==null || tabAprobacion.getValor(i, "horas50_ct_cobph").equals("") || tabAprobacion.getValor(i, "horas50_ct_cobph").isEmpty()) {
						horas50_ct_cobph=0.00;
					}else {
						horas50_ct_cobph=Double.parseDouble(tabAprobacion.getValor(i, "horas50_ct_cobph"));
					}
					
					if (tabAprobacion.getValor(i, "horas100_loep_ct_cobph")==null || tabAprobacion.getValor(i, "horas100_loep_ct_cobph").equals("") || tabAprobacion.getValor(i, "horas100_loep_ct_cobph").isEmpty()) {
						horas100_loep_ct_cobph=0.00;
					}else {
						horas100_loep_ct_cobph=Double.parseDouble(tabAprobacion.getValor(i, "horas100_loep_ct_cobph"));
					}
				}
				
				}
				
				
				if ((horaEntrada==null || horaEntrada.equals("") )  || (horaSalida==null || horaSalida.equals(""))) {
				
				}else{
				
				
				
				TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR in(1,26) ");
		   		int turnoMatriz=0;
		   		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
		   			turnoMatriz=0;
					}else {
						turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
					} 
		        
		        if (horaDescanso == null || horaDescanso.isEmpty()) {
		        	horaDescanso="0";
				}else {
					horaDescanso=horaDescanso;
				}
		    
		        
		 // Se valida si se tiene hora de almuerzo       
		        
		       if (horaSinAlmuerzo.equals("S/A") || horaSinAlmuerzo.equals("") || horaSinAlmuerzo.isEmpty() || horaSinAlmuerzo==null) {
				sinAlmHorario=true;
			} else {
				sinAlmHorario=false;
			}
		       
		    boolean horaExtraEmpleado=false,horarioNocturno=false,horaExtraFeriado=false;
				//Sin no tiene hora de entrada horario entonces  hora al 100%
				if (horarioEntrada==null ||horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					//Si tiene EXTRA O FERIADO
					if (estadoEntrada.equals("EXTRA") ) {
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=false;
					}
				
					else  if (estadoEntrada.equals("FERIADO")){
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=true;

					}
					
				}else {
					if (estadoEntrada.equals("ATRASADO")) {
						horaExtraEmpleado=false;
						banderaAtraso=true;
						horaExtraFeriado=false;
					}
					else  if (estadoEntrada.equals("OK")) {
						horaExtraEmpleado=false;
						banderaAtraso=false;
						horaExtraFeriado=false;

					}
					
					if (estadoSalida.equals("ANTICIPADO")) {
						horaExtraEmpleado=false;
						banderaAtrasoSalida=true;
						horaExtraFeriado=false;

				}
					else  if (estadoSalida.equals("OK")) {
						horaExtraEmpleado=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=false;
				
					}
				
					else  if (estadoEntrada.equals("FERIADO")){
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=true;
				
					}
				
				
				
				}
				
				 
				String fechaEntradaTopeNocturno = fecha+" 19:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeNocturno));
			    
			    Calendar calendario = Calendar.getInstance();
			    int hora =calendario.get(Calendar.HOUR_OF_DAY);
			    int minutos = calendario.get(Calendar.MINUTE);
			    int segundos = calendario.get(Calendar.SECOND);
			    
				String fechaHoraHorarioInicioEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
				    
			    String fechaEntradaNocturno="";
			    if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					fechaEntradaNocturno = fecha+" "+horaEntrada;
				}else {
					fechaEntradaNocturno = fecha+" "+horarioEntrada;
				}

			    Calendar calFechaEntradaNocturno = Calendar.getInstance();
			    calFechaEntradaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaNocturno));
			    
			    String fechaSalidaNocturno = fecha+" 17:00:00";
			    Calendar calFechaSalidaNocturno = Calendar.getInstance();
			    calFechaSalidaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaNocturno));
			    
				
			     
			    ////////////////////if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
			   
			    if (calFechaEntradaNocturno.compareTo(calFechaSalidaNocturno) >= 0 ){			    	
			    horarioNocturno=true;
				}else {
				horarioNocturno=false;
				}
			    
			    Date fechaFinExtra=null,fechaIniExtra=null,fechaFinExtraFeriado=null;
			    
				//tiempo descanso en horas
				    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
				    String  permiso;
				    double tiempoHorasExtra=0.0;
				    double tiempoHorasExtra100=0.0;
				    double tiempoHorasCalculoExtra=0.0;
				   //calculo de hora extra
				    double horaExtra=0.0;
				    Date fechaFinExtra50;
				  //Calculo de horas Extra al 25%
				    if (horarioNocturno==true && horaExtraEmpleado==false && horaExtraFeriado==false) {
							    if (banderaAtraso==true) {
							    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									if (banderaAtrasoSalida) {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
									String fechaSalidaInicio = fecha+" 00:00:00";
								    Calendar calFechaHoraSalidaInicio = Calendar.getInstance();
								    calFechaHoraSalidaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaInicio));
									        
								    String fechaSalidaTopeAtraso = fecha+" "+horarioSalida;
									Calendar calFechaSalidaTopeAtraso = Calendar.getInstance();
									calFechaSalidaTopeAtraso.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaTopeAtraso));
								
									Calendar calFechaSalidaValidacion = Calendar.getInstance();
									calFechaSalidaValidacion.setTime(fechaFinExtra);
									if (calFechaSalidaValidacion.compareTo(calFechaSalidaTopeAtraso) > 0 ){
									fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);									
									}else {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);
									}
									
									}else {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);	
									}
									
								}else {
									//ASD
									
				     
				 
							    if (banderaAtraso==true) {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
								    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
								    	
								    }else {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
								    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									}

									

									
									
									 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
							    		String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									 fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horarioSalida);

										if (banderaAtrasoSalida) {
											fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
										String fechaSalidaInicio = fecha+" 00:00:00";
									    Calendar calFechaHoraSalidaInicio = Calendar.getInstance();
									    calFechaHoraSalidaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaInicio));
										        
									    String fechaSalidaTopeAtraso = fecha+" "+horarioSalida;
										Calendar calFechaSalidaTopeAtraso = Calendar.getInstance();
										calFechaSalidaTopeAtraso.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaTopeAtraso));
									
										Calendar calFechaSalidaValidacion = Calendar.getInstance();
										calFechaSalidaValidacion.setTime(fechaFinExtra);
										if (calFechaSalidaValidacion.compareTo(calFechaSalidaTopeAtraso) > 0 ){
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);									
										}else {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);			     
										}
										
								}else {
											fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);	
										}
								

								}	
								
						
							double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				 	        double valorHoraDescanso=Double.parseDouble(horaDescanso); 
				 	        String p="";
							int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.00;
							}	

							///////////////////////////////////////////////////////////////////////////////////
							
							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							double p3=Double.parseDouble(p2);
						
							//if (banderaAtraso==true) {
						//		horaExtra=(int)horaExtra+p3;
								
							//}else {
								horaExtra=(int)horaExtra+p3;
								
							//}
											
												
							
							//VERIFICAR 
							//&& !apobracionHoraNormal.equals("0")
							if (horaExtra>=1) {

								//Guardar calculo en Horas
								String horaExtraNueva="";
								//horaExtraNueva= actualizarTabla(""+horaExtra);
								String horaExtraNueva25="";
								horaExtraNueva25= actualizarTabla(""+horaExtra);
								/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");*/
							
								/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");		*/
								
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva25));
								
								
								
							}
							else if (horaExtra>=8) {
								horaExtra=(int)horaExtra+p3-8;
								String horaExtraNueva="";
								//horaExtraNueva= actualizarTabla(""+horaExtra);
								String horaExtraNueva50="";
								horaExtraNueva50= actualizarTabla(""+horaExtra);
								if (horaExtra>4) {
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='04:00:00'"
											+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr","04:00:00");

								}else  {
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva50)+"'"
											+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva50));

								}
					
							}
							
							
								
							
					    	String fechaHorarioEntrada = fecha+" "+horarioEntrada;
							Calendar calFechaHorarioEntrada = Calendar.getInstance();
							calFechaHorarioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHorarioEntrada));
					    	
							String fechaHoraEntrada = fecha+" "+horaEntrada;
							Calendar calFechaHoraEntrada = Calendar.getInstance();
							calFechaHoraEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada));
							
								
							if (calFechaHorarioEntrada.compareTo(calFechaHoraEntrada) > 0 ){
								
								tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada), getFechaAsyyyyMMddHHmmss(fechaHorarioEntrada)));
					 	       // valorHoraDescanso=Double.parseDouble(horaDescanso); 
					 	        p="";
								pt_entera=(int)tiempoHorasExtra100Auxiliar;
								pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
								
								if ((int)pt_decimal<10 && pt_decimal>-1) {
									p="0.0"+(int)pt_decimal;
								}else if ((int)pt_decimal<=-1) {
									p="0.0";
								}
								else {
									p="0."+(int)pt_decimal;
								}
								
								p1=Double.parseDouble(p);
								horaExtra=pt_entera+p1;							
								if (horaExtra>=0) {
							   	horaExtra=horaExtra/60;		
								}
								else {
									horaExtra=0.00;
								}	

								///////////////////////////////////////////////////////////////////////////////////
								
								pt_decimal1=(horaExtra-((int)horaExtra))*60;
								p2="";
								if ((int)pt_decimal1<10 && pt_decimal1>-1) {
									p2="0.0"+(int)pt_decimal1;
								}else if ((int)pt_decimal1<=-1) {
									p2="0.0";
								}
								else {
									p2="0."+(int)pt_decimal1;
								}
								
								p3=Double.parseDouble(p2);
							
								//if (banderaAtraso==true) {
								//	horaExtra=(int)horaExtra+p3-8;
									
								//}else {
									horaExtra=(int)horaExtra+p3;
									
								//}
								
								
								//horaExtra=(int)horaExtra+p3-8;
								String horaExtraNueva="";
								//horaExtraNueva= actualizarTabla(""+horaExtra);
								String horaExtraNueva50="";
								horaExtraNueva50= actualizarTabla(""+horaExtra);
								if (horaExtra>4) {
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='04:00:00'"
											+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr","04:00:00");

								}else {
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva50)+"'"
											+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva50));

								}
								
								
							}

							
							
							
							
							
							
								
								
								
/*	PRIMER BLOQUE */					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}else if(horaExtraEmpleado==true &&  horaExtraFeriado==false) {
						if (horarioNocturno==false) {
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				
						}else {
							 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
						     Date fechNocturno=utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada), 1);
						     String fec=getFechaAsyyyyMMdd(fechNocturno);
						     String fec1=fec+" "+horaSalida;
							 fechaFinExtra= getFechaAsyyyyMMddHHmmss(fec1);
						}
						

						double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				        String p="";
						int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						double p1=0.0;
							
							
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
						p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						 double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						 String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
					
							horaExtra=(int)horaExtra+p3;
								
									
						if (horaExtra>=1 ) {
							//Si se pasa de 8 horas le descuento una hora de almuerzo
							if(horaExtra>=9)
							{
	
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
						
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

							}else{
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
						
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
									+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

						}
						}
						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
						
					//CUANDO EL HORARIO ES DIURNO Y NO ES HORA EXTRA
					
			}else if(horaExtraEmpleado==true &&  horaExtraFeriado==true) {
				System.out.println("fecha: "+fecha);
				
			
				if (horarioNocturno==false) {
					fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
				     fechaFinExtra=null;
					// fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);		
					 fechaFinExtraFeriado=getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
				}else {
					// fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
					//Date fechaIniTope = getFechaAsyyyyMMddHHmmss(fecha+" 24:00:00");

					// Calendar calFechaEntrada = Calendar.getInstance();
					 //calFechaEntrada.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
					 
				//	Calendar calFechaEntradaTope = Calendar.getInstance();
				//	 calFechaEntradaTope.setTime(fechaIniTope);
				 //    fechaFinExtra=null;
				  //   Date fechNocturno=null;
					 
			//		    if (calFechaEntrada.compareTo(calFechaEntradaTope) >= 0){			    	
				//			fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					//    }else {
					//		fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);

					  //  }
					fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					 
				 
					 fechaFinExtraFeriado = getFechaAsyyyyMMddHHmmss(fecha+" "+"24:00:00");				

				}
				
System.out.println("fecha error: "+fecha);
				double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtraFeriado));
		        String p="";
				int pt_entera=(int)tiempoHorasExtra100Auxiliar;
				double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
				double p1=0.0;
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) { 
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				
				p1=Double.parseDouble(p);
				horaExtra=pt_entera+p1;
				//if (horaExtra>=8.75) {
			   	//horaExtra=horaExtra-8;		
				//}
				
				if (horaExtra>=0) {
			   	horaExtra=horaExtra/60;		
				}
				else {
					horaExtra=0.0;
				}	

				///////////////////////////////////////////////////////////////////////////////////
				
				 double pt_decimal1=(horaExtra-((int)horaExtra))*60;
				 String p2="";
				if ((int)pt_decimal1<10 && pt_decimal1>-1) {
					p2="0.0"+(int)pt_decimal1;
				}else if ((int)pt_decimal1<=-1) {
					p2="0.0";
				}
				else {
					p2="0."+(int)pt_decimal1;
				}
				
				double p3=Double.parseDouble(p2);
			
					horaExtra=(int)horaExtra+p3;
				
				if (horaExtra>=1 ) {
					//Si se pasa de 8 horas le descuento una hora de almuerzo
					if(horaExtra>=9)
					{

						String horaExtraNueva100="";
						horaExtraNueva100= actualizarTabla(""+(horaExtra));
				
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
								+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
						
						tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));


					}else{
						String horaExtraNueva100="";
						horaExtraNueva100= actualizarTabla(""+(horaExtra));
				
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
							+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

					tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

				}
				}
		
			
				if (horarioNocturno==true) {

					 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
					Date fechaIniTope = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					 Calendar calFechaEntrada = Calendar.getInstance();
					 calFechaEntrada.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
					 
					 Calendar calFechaEntradaTope = Calendar.getInstance();
					 calFechaEntradaTope.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
				     fechaFinExtra=null;
				     Date fechNocturno=null;
					 
					    if (calFechaEntrada.compareTo(calFechaEntradaTope) >= 0){			    	
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					    }else {
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					    }
					 
					 
				     fechNocturno=utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada), 1);
				     String fec=getFechaAsyyyyMMdd(fechNocturno);
				     String fec1=fec+" "+horaSalida;
					 fechaFinExtra= getFechaAsyyyyMMddHHmmss(fec1);
					 fechaFinExtraFeriado = getFechaAsyyyyMMddHHmmss(fecha+" "+"24:00:00");		
					
					
				tiempoHorasExtra100Auxiliar=0.00;pt_decimal=0.00;
				 tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaFinExtraFeriado, fechaFinExtra));
		         p="";
		         pt_entera=0;
				 pt_entera=(int)tiempoHorasExtra100Auxiliar;
				 pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
				 p1=0.0;
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) { 
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				p1=0.00;horaExtra=0.00;
				p1=Double.parseDouble(p);
				horaExtra=pt_entera+p1;
				//if (horaExtra>=8.75) {
			   	//horaExtra=horaExtra-8;		
				//}
				
				if (horaExtra>=0) {
			   	horaExtra=horaExtra/60;		
				}
				else {
					horaExtra=0.0;
				}	

				///////////////////////////////////////////////////////////////////////////////////
				
				  pt_decimal1=(horaExtra-((int)horaExtra))*60;
				  p2="";
				if ((int)pt_decimal1<10 && pt_decimal1>-1) {
					p2="0.0"+(int)pt_decimal1;
				}else if ((int)pt_decimal1<=-1) {
					p2="0.0";
				}
				else {
					p2="0."+(int)pt_decimal1;
				}
				
				 p3=Double.parseDouble(p2);
			
					horaExtra=(int)horaExtra+p3;
						
						
				if (horaExtra>=1 ) {
					//Si se pasa de 8 horas le descuento una hora de almuerzo
				
						String horaExtraNueva100="";
						horaExtraNueva100= actualizarTabla(""+(horaExtra));
				
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
								+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

						tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

				}
		
				}
				
				
				
				
				
				
				
				
			
			
			}else if(horarioNocturno==false && horaExtraEmpleado==false){
						int tiempoAlm=0; 
						if (turnoMatriz==0) {
					    	 tiempoAlm=0; 
						}else {
							tiempoAlm=60;	
						}
					     

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////						 
						 
						 if (tipoEmpleado==1) {
												
							String fechaEntradaInicioCodigo25 = fecha+" 19:00:00";
						    Calendar calFechaEntradaInicioCodigo25 = Calendar.getInstance();
						    calFechaEntradaInicioCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaInicioCodigo25));
							
							String fechaEntradaTopeCodigo25 = fecha+" 22:00:00";
						    Calendar calFechaEntradaTopeCodigo25 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo25));
						    
							String fechaEntradaTopeCodigo50 = fecha+" 24:00:00";
						    Calendar calFechaEntradaTopeCodigo50 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo50.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo50));
						    
						    
							String fechaEntradaInicio = fecha+" 00:00:00";
						    Calendar calfechaEntradaInicio = Calendar.getInstance();
						    calfechaEntradaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaInicio));
						    
						    
							String fechaEntradaFin = fecha+" 06:00:00";
						    Calendar calfechaEntradaFin = Calendar.getInstance();
						    calfechaEntradaFin.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaFin));

						    
						    		    
							String fechaHoraSalidaCodigo = fecha+" "+horarioSalida; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraSalidaCodigo = Calendar.getInstance();
						    calFechaHoraSalidaCodigo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraSalidaCodigo));


							String fechaHoraEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraEntrada = Calendar.getInstance();
						    calFechaHoraEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada));

						    
						    if (calFechaHoraSalidaCodigo.compareTo(calFechaEntradaTopeCodigo25) >= 0){
						    //fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							//fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				

						    	if (calFechaEntradaInicioCodigo25.compareTo(calFechaHoraEntrada) >= 0){
						    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+"19:00:00");
						      }else {
								 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							}
						      
						      
						      
			
						       
						      
						   fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
					    	
							
							 
							// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
							 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
								if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;

							}else {
								tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;

							}
							 

							 //int a=(int)tiempoHorasExtra100Auxiliar/60;
							 
							//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
							//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							String p="";
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
							//if (horaExtra>=8.75) {
						   	//horaExtra=horaExtra-8;		
							//}
							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	

							
							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							double p3=Double.parseDouble(p2);
							
							horaExtra=(int)horaExtra+p3;

							if (horaExtra>=1) {
									String horaExtraNueva100="";
									horaExtraNueva100= actualizarTabla(""+(horaExtra));
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									
									tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

									}else {
									}
							
							
/////////////////////////////////////////////////////////////////////////////50///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
							fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				

							 
							// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
							 tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
								if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
								tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;
								}
								else {
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;
								}
							 //int a=(int)tiempoHorasExtra100Auxiliar/60;
							 
							//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
							//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							horaExtra=0.0;
							pt_entera=0;
							pt_decimal=0.00;
							pt_entera=(int)tiempoHorasExtra100Auxiliar;
							pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							p="";
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
														
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	

							pt_decimal1=0.0;
							pt_decimal1=(horaExtra-((int)horaExtra))*60;
							p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							p3=0.00;
							p3=Double.parseDouble(p2);
							
							horaExtra=(int)horaExtra+p3-8;

							if (horaExtra>=1) {
									String horaExtraNueva100="";
									horaExtraNueva100= actualizarTabla(""+(horaExtra));
								//	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
								//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									}else {
							//			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
							//					+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									}

						    
						    }else if (calFechaHoraSalidaCodigo.compareTo(calFechaEntradaTopeCodigo25) < 0){
								

							      if (banderaAtraso==true) {
							    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							      }else {
									 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
								}
							     if (turnoMatriz==0) {
							    	 tiempoAlm=0; 
								}else {
									tiempoAlm=60;	
								}
							       
							      
							   fechaFinExtra=null;
								 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
						    	
							 
									// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
									 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
									 
										if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
											tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;

										}else {
											tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;
										}


									 //int a=(int)tiempoHorasExtra100Auxiliar/60;
									 
									//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
									//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
									String p="";
									if ((int)pt_decimal<10 && pt_decimal>-1) {
										p="0.0"+(int)pt_decimal;
									}else if ((int)pt_decimal<=-1) {
										p="0.0";
									}
									else {
										p="0."+(int)pt_decimal;
									}
									
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
							//if (horaExtra>=8.75) {
						   	//horaExtra=horaExtra-8;		
							//}
							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	

							///////////////////////////////////////////////////////////////////////////////////
							
							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							double p3=Double.parseDouble(p2);
						
							if (banderaAtraso==true) {
								horaExtra=(int)horaExtra+p3-8;
								
							}else {
								horaExtra=(int)horaExtra+p3;
								
							}
											
												
							
							
									//	&& !apobracionHoraNormal.equals("0")
									if (horaExtra>=1) {
										//if(horaExtra>=9)
										//{
											//String horaExtraNueva100="";
											//horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
									
											
											//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
											//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

										//}else{
											String horaExtraNueva100="";
											horaExtraNueva100= actualizarTabla(""+(horaExtra));
									///tipo codigo de trabajo=1  no tiene al 25 %
										//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
										//}

									}
									//&& apobracionHoraNormal.equals("0")
									if (horaExtra>=1 ) {
									//	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='00:00' "
									//			+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									}
									
									
									
								      if (banderaAtraso==true) {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
								      }else {
										 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
									}
									
									//fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
									fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				

									 
									// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
									 tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
										//tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;
							
									 //int a=(int)tiempoHorasExtra100Auxiliar/60;
									 
									//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
									//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									horaExtra=0.0;
									pt_entera=0;
									pt_decimal=0.00;
									pt_entera=(int)tiempoHorasExtra100Auxiliar;
									pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
									p="";
									if ((int)pt_decimal<10 && pt_decimal>-1) {
										p="0.0"+(int)pt_decimal;
									}else if ((int)pt_decimal<=-1) {
										p="0.0";
									}
									else {
										p="0."+(int)pt_decimal;
									}
									
									p1=Double.parseDouble(p);
									horaExtra=pt_entera+p1;
																
									if (horaExtra>=0) {
								   	horaExtra=horaExtra/60;		
									}
									else {
										horaExtra=0.0;
									}	

									pt_decimal1=0.0;
									pt_decimal1=(horaExtra-((int)horaExtra))*60;
									p2="";
									if ((int)pt_decimal1<10 && pt_decimal1>-1) {
										p2="0.0"+(int)pt_decimal1;
									}else if ((int)pt_decimal1<=-1) {
										p2="0.0";
									}
									else {
										p2="0."+(int)pt_decimal1;
									}

									p3=0.00;
									p3=Double.parseDouble(p2);
									
									if (horaExtra>8) {
										//horaExtra=horaExtra-1;
									}
									
								    if (banderaAtraso==true) {
										horaExtra=(int)horaExtra+p3-8;
										
									}else {
										horaExtra=(int)horaExtra+p3;
										
									}

									if (horaExtra>=1) {
											String horaExtraNueva100="";
											if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
												horaExtra=horaExtra-1;
												horaExtraNueva100= actualizarTabla(""+(horaExtra));

												if (horaExtra>=1) {
												utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							
												tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));
												}
												
												
											}else {
												horaExtraNueva100= actualizarTabla(""+(horaExtra));

											}
									
											
											}else {
										//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
										//				+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}
							

						    
						      if ((calFechaHoraEntrada.compareTo(calfechaEntradaInicio) >= 0)  && (calFechaHoraEntrada.compareTo(calfechaEntradaFin) <= 0)){
							   fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							   fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" 06:00:00");				
				    			 
							
						
							
						    tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
							//tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

						 //int a=(int)tiempoHorasExtra100Auxiliar/60;
						 
						//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
						//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						horaExtra=0.0;
						 pt_entera=0;
						 pt_decimal=0.00;
						 pt_entera=(int)tiempoHorasExtra100Auxiliar;
						pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						 p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						 p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
													
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						 pt_decimal1=0.0;
						pt_decimal1=(horaExtra-((int)horaExtra))*60;
						 p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						 p3=0.00;
						p3=Double.parseDouble(p2);
						
						if (horaExtra>8) {
							//horaExtra=horaExtra-1;
						}
						
					    if (banderaAtraso==true) {
							horaExtra=(int)horaExtra+p3-8;
							
						}else {
							horaExtra=(int)horaExtra+p3;
							
						}

						if (horaExtra>=1) {
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno100_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

								}else {
							//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
							//				+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								}			
						
						
			    
			    }
						    }    
						    
						    
						    
							 
							 //REVISAR
						}else if(tipoEmpleado==2){
							
							
						    if (banderaAtraso==true) {
						    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						      }else {
								 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
							}
						     if (turnoMatriz==0) {
						    	 tiempoAlm=0; 
							}else {
								tiempoAlm=60;	
							}
						       
						      
						   fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
					    	
						 
								// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
								 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

								 //int a=(int)tiempoHorasExtra100Auxiliar/60;
								 
								//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
								//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
								 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
								double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
								String p="";
								if ((int)pt_decimal<10 && pt_decimal>-1) {
									p="0.0"+(int)pt_decimal;
								}else if ((int)pt_decimal<=-1) {
									p="0.0";
								}
								else {
									p="0."+(int)pt_decimal;
								}
								
								double p1=Double.parseDouble(p);
								horaExtra=pt_entera+p1;
								//if (horaExtra>=8.75) {
							   	//horaExtra=horaExtra-8;		
								//}
								
								if (horaExtra>=0) {
							   	horaExtra=horaExtra/60;		
								}
								else {
									horaExtra=0.0;
								}	
							
								///////////////////////////////////////////////////////////////////////////////////
								
								double pt_decimal1=(horaExtra-((int)horaExtra))*60;
								String p2="";
								if ((int)pt_decimal1<10 && pt_decimal1>-1) {
									p2="0.0"+(int)pt_decimal1;
								}else if ((int)pt_decimal1<=-1) {
									p2="0.0";
								}
								else {
									p2="0."+(int)pt_decimal1;
								}
								
								double p3=Double.parseDouble(p2);
								
								if (banderaAtraso==true) {
									horaExtra=(int)horaExtra+p3-8;

								}else {
									horaExtra=(int)horaExtra+p3;
									
								}
			
																	
							
							
								//	&& !apobracionHoraNormal.equals("0")
								if (horaExtra>=1) {
									//if(horaExtra>=9)
						    	//	{
										//String horaExtraNueva100="";
										//horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
								
										
										//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

						    	//	}else {
										String horaExtraNueva100="";
										horaExtraNueva100= actualizarTabla(""+(horaExtra));
								
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas25_loep_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
											+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									
									tab_tabla_hora_extra.setValor(itemReporte,"horas25_loep_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

							//		}	
						    
								}
								//&& apobracionHoraNormal.equals("0")
								if (horaExtra>=1 ) {
									//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas25_loep_cobmr='00:00' "
									//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								}
					    
					    }
					    
							
						}
						 
						 
						 
						 
						 
						 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						 
					    /* if (turnoMatriz==0) {

							String fechaEntradaTopeCodigo25 = fecha+" 22:00:00";
						    Calendar calFechaEntradaTopeCodigo25 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo25));
						    
							String fechaEntradaTopeCodigo50 = fecha+" 24:00:00";
						    Calendar calFechaEntradaTopeCodigo50 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo50.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo50));			    
						    		    
							String fechaHoraSalidaCodigo = fecha+" "+horaSalida; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraSalidaCodigo = Calendar.getInstance();
						    calFechaHoraSalidaCodigo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraSalidaCodigo));
							    

						   
						    if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
						    horarioNocturno=true;
									    		}else {
							horarioNocturno=false;
												}	
							
						    
						    if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
							    horarioNocturno=true;
								}else {
								horarioNocturno=false;
								}
							    
						    
						 
					     }
						 
						 
						 
						 
						// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
						 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
							tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

						 //int a=(int)tiempoHorasExtra100Auxiliar/60;
						 
						//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
						//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						String p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						double p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
						
							if (banderaAtraso==true) {
							horaExtra=(int)horaExtra+p3-8;
							
						}else {
							horaExtra=(int)horaExtra+p3;
							
						}
						
						
						
						
						//	&& !apobracionHoraNormal.equals("0")
						if (horaExtra>=1) {
							if(horaExtra>=9)
							{
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
						
								
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
										
							}else {
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
						
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
									+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}

						}
						//&& apobracionHoraNormal.equals("0")
						if (horaExtra>=1 ) {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='00:00' "
									+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}
							
						/*else {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"',"
									+ "aprueba_hora_extra_cobmr=false where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

							
						}*/

						
						
					//		tab_tabla.ejecutarSql();
					//	utilitario.addUpdate("tab_tabla");
				
						
						
						
						
						
					     /*if (turnoMatriz!=0) {

						
						String horaNocInicio25="17:00:00";
						String horaNocFin25="06:00:00";
						String Hora="";
						

						
						
			    			//suma una hora a la fecha final 
			    			String horaFechaInicioConsulta= fecha+" "+horaNocFin25;    			
			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporte));
								
			    			String fechaHoraHorarioInicio = fecha+" "+horaNocInicio25; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
		 			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
							
						
							
		 				String horaSalidaValidacion25 = fecha+" "+horaSalida; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calhoraSalidaValidacion25 = Calendar.getInstance();//
		 			   calhoraSalidaValidacion25.setTime(getFechaAsyyyyMMddHHmmss(horaSalidaValidacion25));//
							
						
		 		
		 			   
		 			   
		 			    
		 			   if(calhoraSalidaValidacion25.compareTo(calFechaHoraHorarioInicio) >0 ){
		 				   
		 				   
		 				   
		 				Date fechaIniNoc25 = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
							 Date fechaFinNoc25=null;
							//tiempo descanso en horas
						 String fechaFinExtra25="",horaBase="24:00:00";
						    boolean horaSobrepasa=false;
							    
						if (horaSalida.compareTo(horaNocInicio25)>0  && horaSalida.compareTo(horaBase)<=0) {
					
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),0));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
						    tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaFinNoc25,fechaIniNoc25 ));
								        p="";
										pt_entera=(int)tiempoHorasExtra100Auxiliar;
										pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
										
										
										
										if ((int)pt_decimal<10 && pt_decimal>-1) {
											p="0.0"+(int)pt_decimal;
										}else if ((int)pt_decimal<=-1) {
											p="0.0";
										}
										else {
											p="0."+(int)pt_decimal;
										}
										
										p1=Double.parseDouble(p);
										horaExtra=pt_entera+p1;
										//if (horaExtra>=8.75) {
									   	//horaExtra=horaExtra-8;		
										//}
										
										if (horaExtra>=0) {
									   	horaExtra=horaExtra/60;		
										}
										else {
											horaExtra=0.0;
										}	

										///////////////////////////////////////////////////////////////////////////////////
										
										 pt_decimal1=(horaExtra-((int)horaExtra))*60;
										 p2="";
										if ((int)pt_decimal1<10 && pt_decimal1>-1) {
											p2="0.0"+(int)pt_decimal1;
										}else if ((int)pt_decimal1<=-1) {
											p2="0.0";
										}
										else {
											p2="0."+(int)pt_decimal1;
										}
										
										p3=Double.parseDouble(p2);
									
											horaExtra=(int)horaExtra+p3;
											
										
		  
							if (horaExtra>=1 && turnoMatriz==0 && tipoEmpleado==1) {
								
								if(horaExtra>=9)
								{
									String horaExtraNueva25="";
									horaExtraNueva25= actualizarTabla(""+(horaExtra-1));
							
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
											+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR") +" and fecha_evento_cobmr='"+fecha+"'");

								}else{
									String horaExtraNueva25="";
									horaExtraNueva25= actualizarTabla(""+(horaExtra));
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
										+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR") +" and fecha_evento_cobmr='"+fecha+"'");
								}
								
								
								
								
								
					
									
							}/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));nuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumen
								
							}*/
									    
									    
									    
						/*}else {
									    
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
							tiempoHorasExtra25 = (obtenerDiferenciaMinutos(fechaIniNoc25, fechaFinNoc25)/60);
							if (tiempoHorasExtra25>=1 && turnoMatriz==0 && tipoEmpleado==1) {
									    
			String horaExtraNueva25="";
								horaExtraNueva25= actualizarTabla(""+(tiempoHorasExtra25));
						
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
					+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}
							
							/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));					
								
							}*/
			
		//}    
						 
						 
						
				//Calculo de horas Extra al 100%		
						 
						 
						
						
					
		 			  // }	
			//}//else {
					//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+0+"'  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

					//}	
						
						
					//}
			   
				
					
					
				/*	if (horaExtra>=1 || tiempoHorasExtra25>=1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
						//tab_tabla.ejecutarSql();

					}else {
						
					}
					
					*/
					
				}
					
					
					}//FOR
			
		
			getAlimentacion(fechaIni, fechaFin,true);
			
			
			
	/*		int mesRestar = utilitario.getMes(utilitario.getFechaActual());
			mesRestar=mesRestar+1;
			TablaGenerica tabMes=utilitario.consultar("select ide_gemes,ide_geani from asi_horario_mes_empleado where ide_gemes not in ("+(mesRestar)+") group by ide_gemes,ide_geani order by ide_gemes,ide_geani desc");

			for (int i = 0; i < tabMes.getTotalFilas(); i++) {
				TablaGenerica tabAprobadoHorasExtra=utilitario.consultar("select ide_gtemp,ide_gemes from asi_horario_mes_empleado where ide_gemes="+Integer.parseInt(tabMes.getValor(i,"ide_gemes"))+" and "
						+ " ide_geani="+tabMes.getValor(i,"ide_geani")+" and aplica_hora_extra=true group by ide_gtemp,ide_gemes order by ide_gtemp asc");
		 String fechaInicio="";
		 String fechaFinal="";
		 int anioActualizar=0,mesActualizarEmpleado=0,mes=0;
		 String mesActualizar="";
		 anioActualizar=Integer.parseInt(tabMes.getValor(i,"ide_geani"));
		 mesActualizarEmpleado=Integer.parseInt(tabMes.getValor(i,"ide_gemes"));
		if (mesActualizarEmpleado<10) {
			mesActualizar="0"+mesActualizarEmpleado;
		}else {
			mesActualizar=""+mesActualizarEmpleado;
		}
		 
		TablaGenerica tabAnio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anioActualizar);

		 fechaInicio=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-01";
		 fechaFinal=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-31";

				
				for (int j = 0; j < tabAprobadoHorasExtra.getTotalFilas(); j++) {
		//			System.out.println("EMPLEADO INSERTADO"+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP"));
					
			/*		System.out.println("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" ");
*/
		/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" " );					
				}
			}*/


		//	getDiferenciaEntradaSalida(fechaIni,fechaFin);


/*tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
		+ "DEPA.DETALLE_GEDEP, "
		+ "res.ide_cobmr, "
		+ "res.fecha_evento_cobmr, "
		+ "res.dia_cobmr, "
		+ "res.horainiciohorario_cobmr, "
		+ "res.horafinhorario_cobmr,  "
		+ "res.horainiciobiometrico_cobmr, "
		+ "res.horafinbiometrico_cobmr,  "
		+ "res.horas25_loep_cobmr ,  "
		+ "res.horas60_loep_cobmr ,  "
		+ "res.recargonocturno25_cobmr ,  "	
		+ "res.horas50_ct_cobmr ,  "
		+ "res.recargonocturno100_cobmr, "
		+ "GTT.DETALLE_GTTEM  "
		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
		+ "where epar.activo_geedp=true "
		+ "and epar.ide_gtemp in("+1033+")   and "        // str_ide_empleado
		+ "fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");*/
			tab_tabla_sumatoria.setLectura(false);
		
			utilitario.getConexion().ejecutarSql("delete from asi_resumen_horas_extra where ide_geani="+tabAnioConsulta.getValor("IDE_GEANI")+" and mes_asrhe="+utilitario.getMes(fechaIni));
			cuatroUno_(fechaIni,fechaFin,1,str_ide.toString());
			tab_tabla_sumatoria.setCondicion("mes_asrhe="+mesIni+" AND ide_geani="+tabAnioConsulta.getValor("IDE_GEANI"));
			actualizaTotalHXE(fechaIni,fechaFin,mesIni,tabAnioConsulta.getValor("IDE_GEANI"));
			

			
			tab_tabla_sumatoria.ejecutarSql();
			tab_tabla_sumatoria.setLectura(true);
			utilitario.addUpdate("tab_tabla_sumatoria");
			System.out.println("Procesando Horas Extra");	
			System.out.println("Fin Procesamiento de Horas Extra");			
		}
		
		}	 else {
			utilitario.agregarMensajeInfo("Rango de Fechas Inv�lidas",	"Fecha Inicial debe ser menor a Fecha Final");
			return;
		
	}
	 	
		}else {
				utilitario.agregarMensajeInfo("Rango de Fechas Inv�lidas",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
 } 




	//cuatroUno();
/*	boolean extra=false,extra25=false,extra100=false;
	
	for (int i = 0; i <tab_tabla.getTotalFilas();i++) {
		
		
		if(tab_tabla.getValor(i,"horafinextra_cobmr")==null || tab_tabla.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla.getValor(i,"horafinextra_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"horafinextra_cobmr").equals("0") )
		{
			extra=true;
		}else {
			extra=false;

		}
			 
		if(tab_tabla.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno25_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("0"))
		{
			extra25=true;
			}else {
				extra25=false;

			}
			
		
		if(tab_tabla.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno100_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("0"))
		{
			extra100=true;
		}else {
			extra100=false;

		}
		
		
		if (extra==true && extra25==true && extra100==true) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));

		}
	       

	}
	
	tab_tabla.setCondicion("aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
			//and ide_gtemp in(447,583) ");
	tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
	*/
	
}	
		




public void getMarcacionesEmpleadoHorasExtraConsulta(){ 
	tab_tabla_hora_extra.setLectura(false);
	int contador = 0;

	int entrada=0,almuerzo=0,salida=0;
    int entradaNocturno=0,salidaNocturno=0;
String apobracionHoras100="";
String apobracionHoraNormal="";
	 String fechaBiometrico="";
	
//	Tabla que contiene las fechas de timbre del empleado	

	 
	 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
  		fechaIni=(cal_fecha_inicial.getFecha());	
  		fechaFin=(cal_fecha_final.getFecha());
  		//cuatroUno_(fechaIni,fechaFin,1,str_ide.toString());

  		int mesIni=utilitario.getMes(cal_fecha_inicial.getFecha());
		int mesFin=utilitario.getMes(cal_fecha_final.getFecha());
		int anioIni=utilitario.getAnio(cal_fecha_inicial.getFecha());
		int anioFin=utilitario.getAnio(cal_fecha_final.getFecha());

		if (anioIni==anioFin) {
			if (mesIni==mesFin) {
				
			}else {
				utilitario.agregarMensajeInfo("Debe seleccionar un solo mes a la vez", "Debe coincidir el mes de la Fecha Inicio y Fin");
				return;
			}	
		}else {
			utilitario.agregarMensajeInfo("Debe seleccionar un solo anio a la vez", "Debe coincidir el anio de la Fecha Inicio y Fin");
			return;
		}
		TablaGenerica tabAnioConsulta=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");

		
		tab_tabla_hora_extra.setSql("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "cobmr.fecha_evento_cobmr,  "
				+ "cobmr.dia_cobmr,  "
				+ "res.actividades_cobph,  "
				+ "res.productos_eperado_cobph,  "
				+ "cobmr.horainiciohorario_cobmr,  "
				+ "cobmr.horafinhorario_cobmr,   "
				+ "cobmr.horainiciobiometrico_cobmr,  "
				+ "cobmr.horafinbiometrico_cobmr,  "
				+ "cobmr.horas25_loep_cobmr ,   "
				+ "cobmr.horas60_loep_cobmr ,   "
				+ "cobmr.recargonocturno25_cobmr ,  	"
				+ "cobmr.horas50_ct_cobmr ,  "
				+ "cobmr.recargonocturno100_cobmr,  "
				+ "cobmr.dias_laborados_cobmr  "
				+ "FROM con_biometrico_marcaciones_resumen cobmr   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
				+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   " 
			   // + "AND EMP.IDE_GTEMP in(1021,933,1195,1062) "  
			  //  + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) "  
//1062,194,193,
				+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

		tab_tabla_hora_extra.ejecutarSql();
		 utilitario.addUpdate("tab_tabla_hora_extra");	

	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anioIni+")");
	   	
	
	
		
		TablaGenerica tab_plnificacion_hxe=utilitario.consultar("SELECT ide_gtemp, sum(horas25_loep_cobph)+ sum(horas60_loep_cobph) + sum(horas25_ct_cobph) + sum(horas50_ct_cobph) + sum(horas100_loep_ct_cobph) as TOTAL  "
				+ "FROM con_biometrico_plan_hxe "
				+ "WHERE ide_gemes="+mesIni+" and ide_geani="+tab_anio.getValor("ide_geani")
				+ " GROUP BY ide_gtemp  "
				+ "HAVING sum(horas25_loep_cobph)+ sum(horas60_loep_cobph) + sum(horas25_ct_cobph) + sum(horas50_ct_cobph) + sum(horas100_loep_ct_cobph) >0 "
				+ "ORDER BY ide_gtemp ASC");
	
		StringBuilder str_ide_empleado_ = new StringBuilder();

		String int_num_col_idegetemp="";
		 for (int i = 0; i < tab_plnificacion_hxe.getTotalFilas(); i++) {
				int_num_col_idegetemp=tab_plnificacion_hxe.getValor(i, "IDE_GTEMP");
         	  	if(str_ide_empleado.toString().isEmpty()==false){
                         str_ide_empleado.append(",");
                  }
                 str_ide_empleado.append(int_num_col_idegetemp);
         }
	
		 	 
		 
		 TablaGenerica tab_novedad=utilitario.consultar("select ide_asnov,fecha_inicio_asnov,fecha_fin_asnov "
					+ "from asi_novedad  "
					+ "WHERE FECHA_INICIO_ASNOV >= '"+cal_fecha_inicial.getFecha()+"' AND  FECHA_FIN_ASNOV <='"+cal_fecha_final.getFecha()+"' "
					+ "and dia_feriado_asnov=true");

			
			String fechaInicioAsnov="",fechaFinAsnov="";
			
			if (tab_novedad.getTotalFilas()>0) {
				for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
					fechaInicioAsnov=tab_novedad.getValor(i, "fecha_inicio_asnov");
					fechaFinAsnov=tab_novedad.getValor(i, "fecha_fin_asnov");
					getFeriadoXFecha(fechaInicioAsnov, fechaFinAsnov);
				}		
			}
			
		 

			TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "cobmr.fecha_evento_cobmr,  "
					+ "cobmr.dia_cobmr,  "
					+ "res.actividades_cobph,  "
					+ "res.productos_eperado_cobph,  "
					+ "cobmr.horainiciohorario_cobmr,  "
					+ "cobmr.horafinhorario_cobmr,   "
					+ "cobmr.horainiciobiometrico_cobmr,  "
					+ "cobmr.horafinbiometrico_cobmr,  "
					+ "cobmr.horas25_loep_cobmr ,   "
					+ "cobmr.horas60_loep_cobmr ,   "
					+ "cobmr.recargonocturno25_cobmr ,  	"
					+ "cobmr.horas50_ct_cobmr ,  "
					+ "cobmr.tiempohoralm_cobmr ,  "
					+ "cobmr.horainicioalm_cobmr ,  "
					+ "cobmr.horainicioband_cobmr ,  "
					+ "cobmr.horafinband_cobmr ,  "
					+ "cobmr.horainiciohorariohxe_cobmr ,  "
					+ "cobmr.horafinhorariohxe_cobmr ,  "
					+ "cobmr.recargonocturno100_cobmr,  "
					+ "cobmr.dias_laborados_cobmr  "
					+ "FROM con_biometrico_marcaciones_resumen cobmr   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
					+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
					+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
					//+ "AND EMP.IDE_GTEMP in(1021,933,1195,1062) "  
					//			    + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) "  
//1062,194,193,
					+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

			
			
			//296,198,138,189,
		
		//522 peña ligña
		//296 loja yupa
		//188 cachacago ramiro  MANTENIMIENTO
		// 149 morales cumbal diego  CONTROL DIARIO
		
		
		
		
		//str_ide_empleado
		
		
		if (tabConBiometricoMarcacionesResumen.getTotalFilas()<=0) {
			utilitario.agregarMensajeInfo("Rango de Fechas  Invalidas",	"No existen datos para el rango seleccionado");
			return;
		}else{
		
			double horas25_loep_cobph=0.00,horas60_loep_cobph=0.00,horas25_ct_cobph=0.00,horas50_ct_cobph=0.00,horas100_loep_ct_cobph=0.00;
		
			
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
+ " recargonocturno25_cobmr ='',recargonocturno100_cobmr='',horafinextra_cobmr='',horas25_loep_cobmr='',horas60_loep_cobmr='',horas50_ct_cobmr=''  where "
+ " fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");

			
			
			
			for (int itemReporte=0; itemReporte< tabConBiometricoMarcacionesResumen.getTotalFilas(); itemReporte++) {
				Integer ide_gtemp = Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(itemReporte, "IDE_GTEMP"));
				Integer tipoEmpleado=getTipoEmpleado(ide_gtemp);//1 TIPO CODIGO DE TRABAJO Y 2 LOEP
				String fechaBiometricoAgrupadaXEmpleado = tabConBiometricoMarcacionesResumen.getValor(itemReporte,"fecha_evento_cobmr");
				Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
				String fechaHoraBiometrico = fechaBiometricoAgrupadaXEmpleado+" 01:00:00";
				Calendar calFechaHoraBiometrico = Calendar.getInstance();
				calFechaHoraBiometrico.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraBiometrico)); 			
				//Obtengo datos de marcaciones de Entrada y Salida por empleado

			    String fecha=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"FECHA_EVENTO_COBMR");
		        String horarioEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOHORARIO_COBMR");
			    String horaEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOBIOMETRICO_COBMR");
			    String horarioSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINHORARIO_COBMR");
			    String horaSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINBIOMETRICO_COBMR");
			    String empleado=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"ide_gtemp");  
		        String horaDescanso=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"tiempohoralm_cobmr");
		        String horaSinAlmuerzo=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioalm_cobmr");
		        String estadoEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioband_cobmr");
		        String estadoSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinband_cobmr");
		        String horainiciohorariohxe_cobmr=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainiciohorariohxe_cobmr");
		        String horafinhorariohxe_cobmr =tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinhorariohxe_cobmr");
		        
		        boolean banderaAtraso=false,banderaAtrasoSalida=false;
		        System.out.println("EMPLEADO : "+empleado+" fecha : "+fecha);
				double tiempoHorasExtra25=0.0;
		        boolean sinAlmHorario=false;
		        
		 		TablaGenerica tabAprobacion = this.retornaAprobacionHorasExtra(empleado, utilitario.getMes(fecha));
				if (tabAprobacion.getTotalFilas()<=0) {
				}else {
				}
				
				
				if ((horaEntrada==null || horaEntrada.equals("") )  || (horaSalida==null || horaSalida.equals(""))) {
					
				}else{

				TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR in(1,26) ");
		   		int turnoMatriz=0;
		   		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
		   			turnoMatriz=0;
					}else {
					turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
					} 
		        
		        if (horaDescanso == null || horaDescanso.isEmpty()) {
		        	horaDescanso="0";
				}else {
					horaDescanso=horaDescanso;
				}
		    
		         
		 // Se valida si se tiene hora de almuerzo       
		        
		    if (horaSinAlmuerzo.equals("S/A") || horaSinAlmuerzo.equals("") || horaSinAlmuerzo.isEmpty() || horaSinAlmuerzo==null) {
		    	sinAlmHorario=true;
				}else {
				sinAlmHorario=false;
				}

		    boolean horaExtraEmpleado=false,horarioNocturno=false,horaExtraFeriado=false;
				//Sin no tiene hora de entrada horario entonces  hora al 100%
				if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					//Si tiene EXTRA O FERIADO
					if (estadoEntrada.equals("EXTRA") ) {
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=false;
					}
					
					else  if (estadoEntrada.equals("FERIADO")){
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=true;

					}
					
				}else {
					if (estadoEntrada.equals("ATRASADO")) {
						horaExtraEmpleado=false;
						banderaAtraso=true;
						horaExtraFeriado=false;
					}
					else  if (estadoEntrada.equals("OK")) {
						horaExtraEmpleado=false;
						banderaAtraso=false;
						horaExtraFeriado=false;

					}
					
					if (estadoSalida.equals("ANTICIPADO")) {
						horaExtraEmpleado=false;
						banderaAtrasoSalida=true;
						horaExtraFeriado=false;

					}
					else  if (estadoSalida.equals("OK")) {
						horaExtraEmpleado=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=false;

					}
					
					else  if (estadoEntrada.equals("FERIADO")){
						horaExtraEmpleado=true;
						banderaAtraso=false;
						banderaAtrasoSalida=false;
						horaExtraFeriado=true;

					}
					
					
					
				}
				
				
				String fechaEntradaTopeNocturno = fecha+" 19:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeNocturno));
			    
			    Calendar calendario = Calendar.getInstance();
			    int hora =calendario.get(Calendar.HOUR_OF_DAY);
			    int minutos = calendario.get(Calendar.MINUTE);
			    int segundos = calendario.get(Calendar.SECOND);
			    
			    String fechaHoraHorarioInicioEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
		
			    String fechaEntradaNocturno="";
			    if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					fechaEntradaNocturno = fecha+" "+horaEntrada;
				}else {
					fechaEntradaNocturno = fecha+" "+horarioEntrada;
				}

			    Calendar calFechaEntradaNocturno = Calendar.getInstance();
			    calFechaEntradaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaNocturno));
			    
			    String fechaSalidaNocturno = fecha+" 17:00:00";
			    Calendar calFechaSalidaNocturno = Calendar.getInstance();
			    calFechaSalidaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaNocturno));
			    
			   
			    
			    ////////////////////if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
			    
			    if (calFechaEntradaNocturno.compareTo(calFechaSalidaNocturno) >= 0 ){			    	
			    horarioNocturno=true;
				}else {
				horarioNocturno=false;
				}
			
			    Date fechaFinExtra=null,fechaIniExtra=null,fechaFinExtraFeriado=null;
			    
				//tiempo descanso en horas
				    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
				    String  permiso;
				    double tiempoHorasExtra=0.0;
				    double tiempoHorasExtra100=0.0;
				    double tiempoHorasCalculoExtra=0.0;
				   //calculo de hora extra
				    double horaExtra=0.0;
				    Date fechaFinExtra50;
				  //Calculo de horas Extra al 25%
				    if (horarioNocturno==true && horaExtraEmpleado==false && horaExtraFeriado==false) {
							    if (banderaAtraso==true) {
							    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									if (banderaAtrasoSalida) {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
									String fechaSalidaInicio = fecha+" 00:00:00";
								    Calendar calFechaHoraSalidaInicio = Calendar.getInstance();
								    calFechaHoraSalidaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaInicio));
									        
								    String fechaSalidaTopeAtraso = fecha+" "+horarioSalida;
									Calendar calFechaSalidaTopeAtraso = Calendar.getInstance();
									calFechaSalidaTopeAtraso.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaTopeAtraso));
								
									Calendar calFechaSalidaValidacion = Calendar.getInstance();
									calFechaSalidaValidacion.setTime(fechaFinExtra);
									if (calFechaSalidaValidacion.compareTo(calFechaSalidaTopeAtraso) > 0 ){
									fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);									
									}else {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);
									}
									
									}else {
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);	
									}
									
								}else {
									//ASD
									
									
									
								    if (banderaAtraso==true) {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
								    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
								    	
								    }else {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
								    	  String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									}

									
									
									
									
									 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
							    	 String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
									 fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horarioSalida);

										if (banderaAtrasoSalida) {
											fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
										String fechaSalidaInicio = fecha+" 00:00:00";
									    Calendar calFechaHoraSalidaInicio = Calendar.getInstance();
									    calFechaHoraSalidaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaInicio));
										        
									    String fechaSalidaTopeAtraso = fecha+" "+horarioSalida;
										Calendar calFechaSalidaTopeAtraso = Calendar.getInstance();
										calFechaSalidaTopeAtraso.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaTopeAtraso));
									
										Calendar calFechaSalidaValidacion = Calendar.getInstance();
										calFechaSalidaValidacion.setTime(fechaFinExtra);
										if (calFechaSalidaValidacion.compareTo(calFechaSalidaTopeAtraso) > 0 ){
										fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);									
										}else {
											fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);
										}
										
		}else {
											fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);	
										}
								
								
								}	
								
						
							double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				 	        double valorHoraDescanso=Double.parseDouble(horaDescanso); 
				 	        String p="";
							int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.00;
							}	

							///////////////////////////////////////////////////////////////////////////////////
							
							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							double p3=Double.parseDouble(p2);
						
							//if (banderaAtraso==true) {
						//		horaExtra=(int)horaExtra+p3;
								
							//}else {
								horaExtra=(int)horaExtra+p3;
								
						if (horaExtra>=1) {
								//Guardar calculo en Horas
								String horaExtraNueva="";
								String horaExtraNueva25="";
								horaExtraNueva25= actualizarTabla(""+horaExtra);
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva25));
								
							}
							else if (horaExtra>=8) {
								horaExtra=(int)horaExtra+p3-8;
								String horaExtraNueva="";
								String horaExtraNueva50="";
								horaExtraNueva50= actualizarTabla(""+horaExtra);
								if (horaExtra>4) {
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr","04:00:00");
								}else  {
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva50));
								}
							}
							
							
					    	String fechaHorarioEntrada = fecha+" "+horarioEntrada;
							Calendar calFechaHorarioEntrada = Calendar.getInstance();
							calFechaHorarioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHorarioEntrada));
					    	
							String fechaHoraEntrada = fecha+" "+horaEntrada;
							Calendar calFechaHoraEntrada = Calendar.getInstance();
							calFechaHoraEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada));
							
								
							if (calFechaHorarioEntrada.compareTo(calFechaHoraEntrada) > 0 ){
								
								tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada), getFechaAsyyyyMMddHHmmss(fechaHorarioEntrada)));
					 	       // valorHoraDescanso=Double.parseDouble(horaDescanso); 
					 	        p="";
								pt_entera=(int)tiempoHorasExtra100Auxiliar;
								pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
								
								if ((int)pt_decimal<10 && pt_decimal>-1) {
									p="0.0"+(int)pt_decimal;
								}else if ((int)pt_decimal<=-1) {
									p="0.0";
								}
								else {
									p="0."+(int)pt_decimal;
								}
								
								p1=Double.parseDouble(p);
								horaExtra=pt_entera+p1;							
								if (horaExtra>=0) {
							   	horaExtra=horaExtra/60;		
								}
								else {
									horaExtra=0.00;
								}	

								///////////////////////////////////////////////////////////////////////////////////
								
								pt_decimal1=(horaExtra-((int)horaExtra))*60;
								p2="";
								if ((int)pt_decimal1<10 && pt_decimal1>-1) {
									p2="0.0"+(int)pt_decimal1;
								}else if ((int)pt_decimal1<=-1) {
									p2="0.0";
								}
								else {
									p2="0."+(int)pt_decimal1;
								}
								
								p3=Double.parseDouble(p2);
							
								//if (banderaAtraso==true) {
								//	horaExtra=(int)horaExtra+p3-8;
									
								//}else {
									horaExtra=(int)horaExtra+p3;
									
								//}
								

								//horaExtra=(int)horaExtra+p3-8;
								String horaExtraNueva="";
								//horaExtraNueva= actualizarTabla(""+horaExtra);
								String horaExtraNueva50="";
								horaExtraNueva50= actualizarTabla(""+horaExtra);
								if (horaExtra>4) {
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr","04:00:00");
			
								}else {
									tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva50));
		}
								
								
					}
					
						
							
							
							
							
							
								
								
								
/*	PRIMER BLOQUE */					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}else if(horaExtraEmpleado==true &&  horaExtraFeriado==false) {
						if (horarioNocturno==false) {
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				
						}else {
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						     fechaFinExtra=null;
						     Date fechNocturno=utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada), 1);
						     String fec=getFechaAsyyyyMMdd(fechNocturno);
						     String fec1=fec+" "+horaSalida;
							 fechaFinExtra= getFechaAsyyyyMMddHHmmss(fec1);
						}
						

						double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
				        String p="";
						int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						double p1=0.0;
						
						
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						 double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						 String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
					
							horaExtra=(int)horaExtra+p3;
							
						
						if (horaExtra>=1 ) {
							//Si se pasa de 8 horas le descuento una hora de almuerzo
							if(horaExtra>=9)
							{
						
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));
						
							}else{
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
							tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));
						
						}
						}
						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						
						
					//CUANDO EL HORARIO ES DIURNO Y NO ES HORA EXTRA
						
			}else if(horaExtraEmpleado==true &&  horaExtraFeriado==true) {
				System.out.println("fecha: "+fecha);
						
						
				if (horarioNocturno==false) {
					fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
				     fechaFinExtra=null;
					// fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);		
					 fechaFinExtraFeriado=getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
				}else {
					// fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
					//Date fechaIniTope = getFechaAsyyyyMMddHHmmss(fecha+" 24:00:00");
						
					// Calendar calFechaEntrada = Calendar.getInstance();
					 //calFechaEntrada.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
						
				//	Calendar calFechaEntradaTope = Calendar.getInstance();
				//	 calFechaEntradaTope.setTime(fechaIniTope);
				 //    fechaFinExtra=null;
				  //   Date fechNocturno=null;
						
			//		    if (calFechaEntrada.compareTo(calFechaEntradaTope) >= 0){			    	
				//			fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						
					//    }else {
					//		fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioEntrada);
						
					  //  }
					fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						
						
						
					 fechaFinExtraFeriado = getFechaAsyyyyMMddHHmmss(fecha+" "+"24:00:00");				
						
				}
						
System.out.println("fecha error: "+fecha);
				double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtraFeriado));
		        String p="";
						int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
				double p1=0.0;
				
				
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
				p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
				//if (horaExtra>=8.75) {
			   	//horaExtra=horaExtra-8;		
				//}
				
				if (horaExtra>=0) {
			   	horaExtra=horaExtra/60;		
				}
				else {
					horaExtra=0.0;
				}	

				///////////////////////////////////////////////////////////////////////////////////
				
				 double pt_decimal1=(horaExtra-((int)horaExtra))*60;
				 String p2="";
				if ((int)pt_decimal1<10 && pt_decimal1>-1) {
					p2="0.0"+(int)pt_decimal1;
				}else if ((int)pt_decimal1<=-1) {
					p2="0.0";
				}
				else {
					p2="0."+(int)pt_decimal1;
				}
				
				double p3=Double.parseDouble(p2);
			
					horaExtra=(int)horaExtra+p3;
				
				if (horaExtra>=1 ) {
							//Si se pasa de 8 horas le descuento una hora de almuerzo
							if(horaExtra>=9)
							{
	
								String horaExtraNueva100="";
						horaExtraNueva100= actualizarTabla(""+(horaExtra));
						tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

					}else{
						String horaExtraNueva100="";
						horaExtraNueva100= actualizarTabla(""+(horaExtra));
						tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

				}
				}
		
			
				if (horarioNocturno==true) {

					 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
					Date fechaIniTope = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					 Calendar calFechaEntrada = Calendar.getInstance();
					 calFechaEntrada.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
					 
					 Calendar calFechaEntradaTope = Calendar.getInstance();
					 calFechaEntradaTope.setTime(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada));
				     fechaFinExtra=null;
				     Date fechNocturno=null;
						
					    if (calFechaEntrada.compareTo(calFechaEntradaTope) >= 0){			    	
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

							}else{
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);

					    }
					 
					 
				     fechNocturno=utilitario.sumarDiasFecha(getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada), 1);
				     String fec=getFechaAsyyyyMMdd(fechNocturno);
				     String fec1=fec+" "+horaSalida;
					 fechaFinExtra= getFechaAsyyyyMMddHHmmss(fec1);
					 fechaFinExtraFeriado = getFechaAsyyyyMMddHHmmss(fecha+" "+"24:00:00");		
					
					
				tiempoHorasExtra100Auxiliar=0.00;pt_decimal=0.00;
				 tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaFinExtraFeriado, fechaFinExtra));
		         p="";
		         pt_entera=0;
				 pt_entera=(int)tiempoHorasExtra100Auxiliar;
				 pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
				 p1=0.0;
				
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) { 
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				p1=0.00;horaExtra=0.00;
				p1=Double.parseDouble(p);
				horaExtra=pt_entera+p1;
				//if (horaExtra>=8.75) {
			   	//horaExtra=horaExtra-8;		
				//}
				
				if (horaExtra>=0) {
			   	horaExtra=horaExtra/60;		
				}
				else {
					horaExtra=0.0;
				}	

				///////////////////////////////////////////////////////////////////////////////////
				
				  pt_decimal1=(horaExtra-((int)horaExtra))*60;
				  p2="";
				if ((int)pt_decimal1<10 && pt_decimal1>-1) {
					p2="0.0"+(int)pt_decimal1;
				}else if ((int)pt_decimal1<=-1) {
					p2="0.0";
				}
				else {
					p2="0."+(int)pt_decimal1;
				}
				
				 p3=Double.parseDouble(p2);
			
					horaExtra=(int)horaExtra+p3;
						
						
				if (horaExtra>=1 ) {
					//Si se pasa de 8 horas le descuento una hora de almuerzo
				
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
						tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

				}
		
				}
				
				
				
				
				
				
				
				
			
			
			}else if(horarioNocturno==false && horaExtraEmpleado==false){
						int tiempoAlm=0; 
						if (turnoMatriz==0) {
					    	 tiempoAlm=0; 
						}else {
							tiempoAlm=60;	
						}
					     

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////						 
						 
						 if (tipoEmpleado==1) {
													
							String fechaEntradaInicioCodigo25 = fecha+" 19:00:00";
						    Calendar calFechaEntradaInicioCodigo25 = Calendar.getInstance();
						    calFechaEntradaInicioCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaInicioCodigo25));
							
							String fechaEntradaTopeCodigo25 = fecha+" 22:00:00";
						    Calendar calFechaEntradaTopeCodigo25 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo25));
						    
							String fechaEntradaTopeCodigo50 = fecha+" 24:00:00";
						    Calendar calFechaEntradaTopeCodigo50 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo50.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo50));
						    
						    
							String fechaEntradaInicio = fecha+" 00:00:00";
						    Calendar calfechaEntradaInicio = Calendar.getInstance();
						    calfechaEntradaInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaInicio));
						    
						    
							String fechaEntradaFin = fecha+" 06:00:00";
						    Calendar calfechaEntradaFin = Calendar.getInstance();
						    calfechaEntradaFin.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaFin));

						    
						    		    
							String fechaHoraSalidaCodigo = fecha+" "+horarioSalida; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraSalidaCodigo = Calendar.getInstance();
						    calFechaHoraSalidaCodigo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraSalidaCodigo));


							String fechaHoraEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraEntrada = Calendar.getInstance();
						    calFechaHoraEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraEntrada));

						    
						    if (calFechaHoraSalidaCodigo.compareTo(calFechaEntradaTopeCodigo25) >= 0){
						    //fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							//fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				

						    	if (calFechaEntradaInicioCodigo25.compareTo(calFechaHoraEntrada) >= 0){
						    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+"19:00:00");
						      }else {
								 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							}
						      
						      
						      
			
						       
						      
						   fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
					    	
							
							 
							// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
							 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
								if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;

							}else {
								tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;

							}
							 

							 //int a=(int)tiempoHorasExtra100Auxiliar/60;
							 
							//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
							//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							String p="";
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
							
							double p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
							//if (horaExtra>=8.75) {
						   	//horaExtra=horaExtra-8;		
							//}
							
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	
						

							double pt_decimal1=(horaExtra-((int)horaExtra))*60;
							String p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
						}
							else {
								p2="0."+(int)pt_decimal1;
						}
						
							double p3=Double.parseDouble(p2);
							
							horaExtra=(int)horaExtra+p3;

							if (horaExtra>=1) {
									String horaExtraNueva100="";
									horaExtraNueva100= actualizarTabla(""+(horaExtra));
									tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno25_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

									}else {
						}
						
							
/////////////////////////////////////////////////////////////////////////////50///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
																
							fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
							fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				
						
						
							// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
							 tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
								if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
								tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;
								}
								else {
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;
								}
							 //int a=(int)tiempoHorasExtra100Auxiliar/60;
					
							//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
							//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
							horaExtra=0.0;
							pt_entera=0;
							pt_decimal=0.00;
							pt_entera=(int)tiempoHorasExtra100Auxiliar;
							pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
							p="";
							if ((int)pt_decimal<10 && pt_decimal>-1) {
								p="0.0"+(int)pt_decimal;
							}else if ((int)pt_decimal<=-1) {
								p="0.0";
							}
							else {
								p="0."+(int)pt_decimal;
							}
						
							p1=Double.parseDouble(p);
							horaExtra=pt_entera+p1;
														
							if (horaExtra>=0) {
						   	horaExtra=horaExtra/60;		
							}
							else {
								horaExtra=0.0;
							}	
						 	
							pt_decimal1=0.0;
							pt_decimal1=(horaExtra-((int)horaExtra))*60;
							p2="";
							if ((int)pt_decimal1<10 && pt_decimal1>-1) {
								p2="0.0"+(int)pt_decimal1;
							}else if ((int)pt_decimal1<=-1) {
								p2="0.0";
							}
							else {
								p2="0."+(int)pt_decimal1;
							}
							
							p3=0.00;
							p3=Double.parseDouble(p2);
							
							horaExtra=(int)horaExtra+p3-8;

							if (horaExtra>=1) {
									String horaExtraNueva100="";
									horaExtraNueva100= actualizarTabla(""+(horaExtra));
								//	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
								//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									}else {
							//			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
							//					+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}
					     
						    
						    }else if (calFechaHoraSalidaCodigo.compareTo(calFechaEntradaTopeCodigo25) < 0){
								

					      if (banderaAtraso==true) {
					    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							      }else {
									 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
								}
							     if (turnoMatriz==0) {
							    	 tiempoAlm=0; 
								}else {
									tiempoAlm=60;
								}
								 
							     
							   fechaFinExtra=null;
								 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
						    	
							 
									// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
									 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
									 
										if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
											tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-1;

										}else {
											tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar;
										}


									 //int a=(int)tiempoHorasExtra100Auxiliar/60;
									 
									//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
									//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
									String p="";
									if ((int)pt_decimal<10 && pt_decimal>-1) {
										p="0.0"+(int)pt_decimal;
									}else if ((int)pt_decimal<=-1) {
										p="0.0";
									}
									else {
										p="0."+(int)pt_decimal;
									}
									
									double p1=Double.parseDouble(p);
									horaExtra=pt_entera+p1;
									//if (horaExtra>=8.75) {
								   	//horaExtra=horaExtra-8;		
									//}
									
									if (horaExtra>=0) {
								   	horaExtra=horaExtra/60;		
									}
									else {
										horaExtra=0.0;
									}	

									///////////////////////////////////////////////////////////////////////////////////
									
									double pt_decimal1=(horaExtra-((int)horaExtra))*60;
									String p2="";
									if ((int)pt_decimal1<10 && pt_decimal1>-1) {
										p2="0.0"+(int)pt_decimal1;
									}else if ((int)pt_decimal1<=-1) {
										p2="0.0";
									}
									else {
										p2="0."+(int)pt_decimal1;
									}
									
									double p3=Double.parseDouble(p2);
									
									if (banderaAtraso==true) {
										horaExtra=(int)horaExtra+p3-8;
										
									}else {
										horaExtra=(int)horaExtra+p3;
										
									}
									
									
									
									
									//	&& !apobracionHoraNormal.equals("0")
									if (horaExtra>=1) {
										//if(horaExtra>=9)
										//{
											//String horaExtraNueva100="";
											//horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
									
											
											//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
											//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

										//}else{
											String horaExtraNueva100="";
											horaExtraNueva100= actualizarTabla(""+(horaExtra));
									///tipo codigo de trabajo=1  no tiene al 25 %
										//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
										//}

									}
									//&& apobracionHoraNormal.equals("0")
									if (horaExtra>=1 ) {
									//	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='00:00' "
									//			+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
									}
									
									
									
								      if (banderaAtraso==true) {
								    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
								      }else {
										 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
									}
									
									//fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
									fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				

									 
									// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
									 tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
										//tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

									 //int a=(int)tiempoHorasExtra100Auxiliar/60;
									 
									//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
									//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
									horaExtra=0.0;
									pt_entera=0;
									pt_decimal=0.00;
									pt_entera=(int)tiempoHorasExtra100Auxiliar;
									pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
									p="";
									if ((int)pt_decimal<10 && pt_decimal>-1) {
										p="0.0"+(int)pt_decimal;
									}else if ((int)pt_decimal<=-1) {
										p="0.0";
									}
									else {
										p="0."+(int)pt_decimal;
									}
									
									p1=Double.parseDouble(p);
									horaExtra=pt_entera+p1;
																
									if (horaExtra>=0) {
								   	horaExtra=horaExtra/60;		
									}
									else {
										horaExtra=0.0;
									}	

									pt_decimal1=0.0;
									pt_decimal1=(horaExtra-((int)horaExtra))*60;
									p2="";
									if ((int)pt_decimal1<10 && pt_decimal1>-1) {
										p2="0.0"+(int)pt_decimal1;
									}else if ((int)pt_decimal1<=-1) {
										p2="0.0";
									}
									else {
										p2="0."+(int)pt_decimal1;
									}
									
									p3=0.00;
									p3=Double.parseDouble(p2);
									
									if (horaExtra>8) {
										//horaExtra=horaExtra-1;
									}
									
								    if (banderaAtraso==true) {
										horaExtra=(int)horaExtra+p3-8;
										
									}else {
										horaExtra=(int)horaExtra+p3;
										
									}

									if (horaExtra>=1) {
											String horaExtraNueva100="";
											if (horaDescanso.equals("1.0") || horaDescanso.equals("1.00")) {
												horaExtra=horaExtra-1;
												horaExtraNueva100= actualizarTabla(""+(horaExtra));

												if (horaExtra>=1) {
												tab_tabla_hora_extra.setValor(itemReporte,"horas50_ct_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));
												}
												
												
											}else {
												horaExtraNueva100= actualizarTabla(""+(horaExtra));

											}
									
											
											}else {
										//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
										//				+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
											}			
									
									
						    
						      if ((calFechaHoraEntrada.compareTo(calfechaEntradaInicio) >= 0)  && (calFechaHoraEntrada.compareTo(calfechaEntradaFin) <= 0)){
							   fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
							   fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" 06:00:00");				
				    			 
							
						
							
						    tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
							//tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

						 //int a=(int)tiempoHorasExtra100Auxiliar/60;
						 
						//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
						//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						horaExtra=0.0;
						 pt_entera=0;
						 pt_decimal=0.00;
						 pt_entera=(int)tiempoHorasExtra100Auxiliar;
						pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						 p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						 p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
													
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						 pt_decimal1=0.0;
						pt_decimal1=(horaExtra-((int)horaExtra))*60;
						 p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						 p3=0.00;
						p3=Double.parseDouble(p2);
						
						if (horaExtra>8) {
							//horaExtra=horaExtra-1;
						}
						
					    if (banderaAtraso==true) {
							horaExtra=(int)horaExtra+p3-8;
							
						}else {
							horaExtra=(int)horaExtra+p3;
							
						}

						if (horaExtra>=1) {
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
								tab_tabla_hora_extra.setValor(itemReporte,"recargonocturno100_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

								}else {
							//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas50_ct_cobmr='00:00' "
							//				+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								}			
						
						
			    
			    }
						    }    
						    
						    
						    
							 
							 //REVISAR
						}else if(tipoEmpleado==2){
							
							
						    if (banderaAtraso==true) {
						    	  fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
						}else {
							 fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horarioSalida);
							}
						     if (turnoMatriz==0) {
						     	tiempoAlm=0;
							}else {
								tiempoAlm=60;	
						}
					     
						      
					   fechaFinExtra=null;
							 fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
					    	
						 
								// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
								 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
									tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

								 //int a=(int)tiempoHorasExtra100Auxiliar/60;
								 
								//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
								//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
								 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
								double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
								String p="";
								if ((int)pt_decimal<10 && pt_decimal>-1) {
									p="0.0"+(int)pt_decimal;
								}else if ((int)pt_decimal<=-1) {
									p="0.0";
								}
								else {
									p="0."+(int)pt_decimal;
								}
								
								double p1=Double.parseDouble(p);
								horaExtra=pt_entera+p1;
								//if (horaExtra>=8.75) {
							   	//horaExtra=horaExtra-8;		
								//}
								
								if (horaExtra>=0) {
							   	horaExtra=horaExtra/60;		
								}
								else {
									horaExtra=0.0;
								}	

								///////////////////////////////////////////////////////////////////////////////////
								
								double pt_decimal1=(horaExtra-((int)horaExtra))*60;
								String p2="";
								if ((int)pt_decimal1<10 && pt_decimal1>-1) {
									p2="0.0"+(int)pt_decimal1;
								}else if ((int)pt_decimal1<=-1) {
									p2="0.0";
								}
								else {
									p2="0."+(int)pt_decimal1;
								}
								
								double p3=Double.parseDouble(p2);
								
								if (banderaAtraso==true) {
									horaExtra=(int)horaExtra+p3-8;
									
								}else {
									horaExtra=(int)horaExtra+p3;
									
								}
								
								
								
								
								//	&& !apobracionHoraNormal.equals("0")
								if (horaExtra>=1) {
									//if(horaExtra>=9)
									//{
										//String horaExtraNueva100="";
										//horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
								
										
										//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

									//}else{
										String horaExtraNueva100="";
										horaExtraNueva100= actualizarTabla(""+(horaExtra));								
									tab_tabla_hora_extra.setValor(itemReporte,"horas25_loep_cobmr",nuevaHoraExtraResumen(horaExtraNueva100));

									//}

								}
								//&& apobracionHoraNormal.equals("0")
								if (horaExtra>=1 ) {
									//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horas25_loep_cobmr='00:00' "
									//		+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
								}
					    
					    }
					    
							
						}
						 
						 
						 
						 
						 
						 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						 
					    /* if (turnoMatriz==0) {

							String fechaEntradaTopeCodigo25 = fecha+" 22:00:00";
						    Calendar calFechaEntradaTopeCodigo25 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo25.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo25));
						    
							String fechaEntradaTopeCodigo50 = fecha+" 24:00:00";
						    Calendar calFechaEntradaTopeCodigo50 = Calendar.getInstance();
						    calFechaEntradaTopeCodigo50.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeCodigo50));			    
						    		    
							String fechaHoraSalidaCodigo = fecha+" "+horaSalida; //cogo la hora y le concateno con la fecha del horario
						    Calendar calFechaHoraSalidaCodigo = Calendar.getInstance();
						    calFechaHoraSalidaCodigo.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraSalidaCodigo));
							    

						    
						    if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
						    horarioNocturno=true;
							}else {
							horarioNocturno=false;
							}
						    
					     
						    if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
							    horarioNocturno=true;
								}else {
								horarioNocturno=false;
								}
							    
						    
						 
					     }
						
						 
						 
						 
						// double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
						 double tiempoHorasExtra100Auxiliar= obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra);
							tiempoHorasExtra100Auxiliar=tiempoHorasExtra100Auxiliar-tiempoAlm;

						 //int a=(int)tiempoHorasExtra100Auxiliar/60;
						 
						//tiempoHorasExtra100Auxiliar = tiempoHorasExtra100Auxiliar;//-valorHoraDescanso;
						//int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						 int pt_entera=(int)tiempoHorasExtra100Auxiliar;
						double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
						String p="";
						if ((int)pt_decimal<10 && pt_decimal>-1) {
							p="0.0"+(int)pt_decimal;
						}else if ((int)pt_decimal<=-1) {
							p="0.0";
						}
						else {
							p="0."+(int)pt_decimal;
						}
						
						double p1=Double.parseDouble(p);
						horaExtra=pt_entera+p1;
						//if (horaExtra>=8.75) {
					   	//horaExtra=horaExtra-8;		
						//}
						
						if (horaExtra>=0) {
					   	horaExtra=horaExtra/60;		
						}
						else {
							horaExtra=0.0;
						}	

						///////////////////////////////////////////////////////////////////////////////////
						
						double pt_decimal1=(horaExtra-((int)horaExtra))*60;
						String p2="";
						if ((int)pt_decimal1<10 && pt_decimal1>-1) {
							p2="0.0"+(int)pt_decimal1;
						}else if ((int)pt_decimal1<=-1) {
							p2="0.0";
						}
						else {
							p2="0."+(int)pt_decimal1;
						}
						
						double p3=Double.parseDouble(p2);
						
						if (banderaAtraso==true) {
							horaExtra=(int)horaExtra+p3-8;
							
						}else {
							horaExtra=(int)horaExtra+p3;
							
						}
						
						
						
						
						//	&& !apobracionHoraNormal.equals("0")
						if (horaExtra>=1) {
							if(horaExtra>=9)
							{
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra-1));
						
								
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");

							}else{
								String horaExtraNueva100="";
								horaExtraNueva100= actualizarTabla(""+(horaExtra));
						
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva100)+"'"
									+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
							}

						}
						//&& apobracionHoraNormal.equals("0")
						if (horaExtra>=1 ) {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='00:00' "
									+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
						}
						
						/*else {
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+horaExtra+"',"
									+ "aprueba_hora_extra_cobmr=false where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

							
						}*/

						
						
					//		tab_tabla.ejecutarSql();
					//	utilitario.addUpdate("tab_tabla");
				
						
						
						
						
						
					     /*if (turnoMatriz!=0) {
						
						
						String horaNocInicio25="17:00:00";
						String horaNocFin25="06:00:00";
						String Hora="";
						

						
						
			    			//suma una hora a la fecha final 
			    			String horaFechaInicioConsulta= fecha+" "+horaNocFin25;    			
			    			String fechaFinReporte= getFechaAsyyyyMMddHHmmss(sumarRestarHorasFecha(getFechaAsyyyyMMddHHmmss(horaFechaInicioConsulta),1));
			    			Calendar calHoraFechaFinConsulta = Calendar.getInstance();
			    			calHoraFechaFinConsulta.setTime(getFechaAsyyyyMMddHHmmss(fechaFinReporte));
								
			    			String fechaHoraHorarioInicio = fecha+" "+horaNocInicio25; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calFechaHoraHorarioInicio = Calendar.getInstance();//
		 			    calFechaHoraHorarioInicio.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicio));//
							
						
		 			    
		 				String horaSalidaValidacion25 = fecha+" "+horaSalida; //cogo la hora y le concateno con la fecha del horario
		 			    Calendar calhoraSalidaValidacion25 = Calendar.getInstance();//
		 			   calhoraSalidaValidacion25.setTime(getFechaAsyyyyMMddHHmmss(horaSalidaValidacion25));//
							
						
		 		
		 			   
		 			   
		 			    
		 			   if(calhoraSalidaValidacion25.compareTo(calFechaHoraHorarioInicio) >0 ){
		 				   
		 				   
		 				   
		 				Date fechaIniNoc25 = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);
						 Date fechaFinNoc25=null;
						//tiempo descanso en horas
						 String fechaFinExtra25="",horaBase="24:00:00";
						    boolean horaSobrepasa=false;
				
						if (horaSalida.compareTo(horaNocInicio25)>0  && horaSalida.compareTo(horaBase)<=0) {
							 
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),0));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
						    tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaFinNoc25,fechaIniNoc25 ));
					        p="";
													pt_entera=(int)tiempoHorasExtra100Auxiliar;
													pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
													
													
													
													if ((int)pt_decimal<10 && pt_decimal>-1) {
														p="0.0"+(int)pt_decimal;
													}else if ((int)pt_decimal<=-1) {
														p="0.0";
													}
													else {
														p="0."+(int)pt_decimal;
													}
													
													p1=Double.parseDouble(p);
													horaExtra=pt_entera+p1;
													//if (horaExtra>=8.75) {
												   	//horaExtra=horaExtra-8;		
													//}
													
													if (horaExtra>=0) {
												   	horaExtra=horaExtra/60;		
													}
													else {
														horaExtra=0.0;
													}	

													///////////////////////////////////////////////////////////////////////////////////
													
													 pt_decimal1=(horaExtra-((int)horaExtra))*60;
													 p2="";
													if ((int)pt_decimal1<10 && pt_decimal1>-1) {
														p2="0.0"+(int)pt_decimal1;
													}else if ((int)pt_decimal1<=-1) {
														p2="0.0";
													}
													else { 
														p2="0."+(int)pt_decimal1;
													}
													
													p3=Double.parseDouble(p2);
												
														horaExtra=(int)horaExtra+p3;
														
													
					  
							if (horaExtra>=1 && turnoMatriz==0 && tipoEmpleado==1) {
								
								if(horaExtra>=9)
								{
									String horaExtraNueva25="";
									horaExtraNueva25= actualizarTabla(""+(horaExtra-1));
							
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
											+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR") +" and fecha_evento_cobmr='"+fecha+"'");

								}else{
									String horaExtraNueva25="";
									horaExtraNueva25= actualizarTabla(""+(horaExtra));
									utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
										+ "  where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR") +" and fecha_evento_cobmr='"+fecha+"'");
								}
								
								
								
								
								
					
									
							}/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));nuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumennuevaHoraExtraResumen
								
							}*/
						
					
						
						/*}else {
							 
						    String sumafechaNoc=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
						    fechaFinNoc25 = getFechaAsyyyyMMddHHmmss(sumafechaNoc+" "+horaNocInicio25);
							tiempoHorasExtra25 = (obtenerDiferenciaMinutos(fechaIniNoc25, fechaFinNoc25)/60);
							if (tiempoHorasExtra25>=1 && turnoMatriz==0 && tipoEmpleado==1) {
								
								String horaExtraNueva25="";
								horaExtraNueva25= actualizarTabla(""+(tiempoHorasExtra25));
						
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva25)+"'"
										+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");					
							}
							
							/*else {
								utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr=0.0"
										+ ",aprueba_hora_extra_cobmr=false  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));					
								
							}*/
			
		//}    
						 
						 
						
				//Calculo de horas Extra al 100%		
						 
						 
						
						
						
		 			  // }	
			//}//else {
					//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set recargonocturno25_cobmr='"+0+"'  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));

					//}	
						
						
					//}
			   
				
					
					
				/*	if (horaExtra>=1 || tiempoHorasExtra25>=1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(itemReporte,"IDE_COBMR"));
						//tab_tabla.ejecutarSql();

					}else {
						
					}
					
					*/
					
				}
					
					
					}//FOR
					
			
			
			
			
			
			
	/*		int mesRestar = utilitario.getMes(utilitario.getFechaActual());
			mesRestar=mesRestar+1;
			TablaGenerica tabMes=utilitario.consultar("select ide_gemes,ide_geani from asi_horario_mes_empleado where ide_gemes not in ("+(mesRestar)+") group by ide_gemes,ide_geani order by ide_gemes,ide_geani desc");
			
			for (int i = 0; i < tabMes.getTotalFilas(); i++) {
				TablaGenerica tabAprobadoHorasExtra=utilitario.consultar("select ide_gtemp,ide_gemes from asi_horario_mes_empleado where ide_gemes="+Integer.parseInt(tabMes.getValor(i,"ide_gemes"))+" and "
						+ " ide_geani="+tabMes.getValor(i,"ide_geani")+" and aplica_hora_extra=true group by ide_gtemp,ide_gemes order by ide_gtemp asc");
		 String fechaInicio="";
		 String fechaFinal="";
		 int anioActualizar=0,mesActualizarEmpleado=0,mes=0;
		 String mesActualizar="";
		 anioActualizar=Integer.parseInt(tabMes.getValor(i,"ide_geani"));
		 mesActualizarEmpleado=Integer.parseInt(tabMes.getValor(i,"ide_gemes"));
		if (mesActualizarEmpleado<10) {
			mesActualizar="0"+mesActualizarEmpleado;
		}else {
			mesActualizar=""+mesActualizarEmpleado;
		}
		 
		TablaGenerica tabAnio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+anioActualizar);

		 fechaInicio=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-01";
		 fechaFinal=tabAnio.getValor("DETALLE_GEANI")+"-"+mesActualizar+"-31";

				
				for (int j = 0; j < tabAprobadoHorasExtra.getTotalFilas(); j++) {
		//			System.out.println("EMPLEADO INSERTADO"+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP"));
					
			/*		System.out.println("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" ");
*/
		/*	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
					+" aprueba_hora_extra_cobmr=true where fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFinal+"'  "
							+ "and ide_gtemp="+tabAprobadoHorasExtra.getValor(j,"IDE_GTEMP")+" " );					
				}
			}*/


		//	getDiferenciaEntradaSalida(fechaIni,fechaFin);
			

/*tab_tabla_hora_extra.setSql("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "					
					+ "res.fecha_evento_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinbiometrico_cobmr,  "
		+ "res.horas25_loep_cobmr ,  "
		+ "res.horas60_loep_cobmr ,  "
					+ "res.recargonocturno25_cobmr,  "
		+ "res.horas50_ct_cobmr ,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
		+ "where epar.activo_geedp=true "
		+ "and epar.ide_gtemp in("+1033+")   and "        // str_ide_empleado
		+ "fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");*/
			tab_tabla_sumatoria.setCondicion("mes_asrhe="+mesIni+" AND ide_geani="+tabAnioConsulta.getValor("IDE_GEANI"));
			tab_tabla_sumatoria.ejecutarSql();
			utilitario.addUpdate("tab_tabla_sumatoria");
			System.out.println("Procesando Horas Extra");	
			System.out.println("Fin Procesamiento de Horas Extra");
					
			getAlimentacion(fechaIni, fechaFin, false);

			
			
		}
		
		}	 else {
			utilitario.agregarMensajeInfo("Rango de Fechas Inv�lidas",	"Fecha Inicial debe ser menor a Fecha Final");
			return;
		
	}
	 	
		}else {
				utilitario.agregarMensajeInfo("Rango de Fechas Inv�lidas",	"Debe seleccionar Fecha Inicial y Fecha Final");
				return;
			
 } 




	//cuatroUno();
/*	boolean extra=false,extra25=false,extra100=false;
	
	for (int i = 0; i <tab_tabla.getTotalFilas();i++) {
		
		
		if(tab_tabla.getValor(i,"horafinextra_cobmr")==null || tab_tabla.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla.getValor(i,"horafinextra_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"horafinextra_cobmr").equals("0") )
		{
			extra=true;
		}else {
			extra=false;

		}
			 
		if(tab_tabla.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno25_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno25_cobmr").equals("0"))
		{
			extra25=true;
			}else {
				extra25=false;

			}
			
		
		if(tab_tabla.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla.getValor(i,"recargonocturno100_cobmr").isEmpty()
				|| tab_tabla.getValor(i,"recargonocturno100_cobmr").equals("0"))
		{
			extra100=true;
		}else {
			extra100=false;

		}
		
		
		if (extra==true && extra25==true && extra100==true) {
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true  where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));

		}
	       

	}
	
	tab_tabla.setCondicion("aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'");
			//and ide_gtemp in(447,583) ");
	tab_tabla.ejecutarSql();
	utilitario.addUpdate("tab_tabla");
	*/
	
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

private List<Fila> obtenerTimbreBiometricoNocturno(Integer IDE_GTEM,String FECHA_INICIAL,String FECHA_INICIAL1, String FECHA_FINAL, String FECHA_FINAL1){
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
			+ "IDE_COBIM,"
			+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHA,"
			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') as HORA "
			+ "from  con_biometrico_marcaciones  "
			+ "where FECHA_EVENTO_COBIM between '"+FECHA_INICIAL+"' AND  '"+FECHA_INICIAL1+"'  "
			+ "or  FECHA_EVENTO_COBIM between '"+FECHA_FINAL+"' AND  '"+FECHA_FINAL1+"'  " 
			+ "ORDER BY FECHA ASC");    	
	
	return tabObteberTimbreXEmpleado.getFilas();
	
}

	
private List<Fila> obtenerTimbreBiometrico(Integer IDE_GTEM,Date FECHA_INICIAL, Date FECHA_FINAL){
	String fechaIni = getFechaAsyyyyMMdd(FECHA_INICIAL);
	String fechaFin = getFechaAsyyyyMMdd(FECHA_FINAL);
	TablaGenerica tabObteberTimbreXEmpleado=utilitario.consultar("select "
				+ "EMP.IDE_GTEMP,"
    			+ "TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
    			+ "TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM,BIO.EVENTO_RELOJ_COBIM  "
    			+ "from gth_empleado emp  "
    			+ "LEFT JOIN CON_BIOMETRICO_MARCACIONES BIO ON TRIM(BIO.IDE_PERSONA_COBIM)=EMP.TARJETA_MARCACION_GTEMP "
    			+ "where emp.ide_astur is not null  and TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' "
    			+ "AND EMP.IDE_GTEMP="+IDE_GTEM+"  "
    			+ " ORDER BY BIO.FECHA_EVENTO_COBIM ASC,HORAM asc");    	
	return tabObteberTimbreXEmpleado.getFilas();
}



public void seleccionarHorasExtra(){
if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null ) {
	if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
		
		if (aut_empleados.getValor()!=null) {
			
		
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());	
		//.setCondicion("aprueba_hora_extra_cobmr=true");
		//tab_tabla.ejecutarSql();
	
		tab_tabla_hora_extra.setCondicion("ide_gtemp="+aut_empleados.getValor()+" and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
		tab_tabla_hora_extra.ejecutarSql();
		utilitario.addUpdate("tab_tabla");
		}else {
			tab_tabla_hora_extra.setCondicion("fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"'");
			tab_tabla_hora_extra.imprimirSql();          		
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla");
		}
	
	}else {
		utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
		
	}
		
	
}
}



public String getFechaIni() {
	return fechaIni;
}



public void setFechaIni(String fechaIni) {
	this.fechaIni = fechaIni;
}



public String getFechaFin() {
	return fechaFin;
}



public void setFechaFin(String fechaFin) {
	this.fechaFin = fechaFin;
}



public SeleccionTabla getSel_mes() {
	return sel_mes;
}



public void setSel_mes(SeleccionTabla sel_mes) {
	this.sel_mes = sel_mes;
}



public void asignarMatriz(){
	
	sel_mes.dibujar();
}



public SeleccionTabla getSel_empleado() {
	return sel_empleado;
}



public void setSel_empleado(SeleccionTabla sel_empleado) {
	this.sel_empleado = sel_empleado;
}








public SeleccionTabla getSet_empleado_reporte() {
	return set_empleado_reporte;
}




public void setSet_empleado_reporte(SeleccionTabla set_empleado_reporte) {
	this.set_empleado_reporte = set_empleado_reporte;
}




public SeleccionTabla getSet_empleado_total() {
	return set_empleado_total;
}




public void setSet_empleado_total(SeleccionTabla set_empleado_total) {
	this.set_empleado_total = set_empleado_total;
}




public void abrirEmpleado(){
	int mesActual=0;
	  mes=sel_mes.getValorSeleccionado();
	    
	  
String anio="";
	  if (mes.equals("") ||  mes==null || mes.isEmpty()) {
		return;
	}else {
	mesActual=Integer.parseInt(mes);
	}
	  
	  
	  if (mesActual<10) {
		mes="0"+mesActual;
	}
	   
	  
	  
	  
	  if (mesActual>10) {
		anio="2017";
	}else {
		anio="2018";
	}
		
	  
	  
		sel_mes.cerrar();

	  
		
		
		
		
		
		
	  fechaInicial=anio+"-"+mes+"-01";
	  fechaFinal=anio+"-"+mes+"-31";

	  
		sel_empleado.setSeleccionTabla("select cor.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
    			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
    			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
    			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
    			+ "from con_biometrico_marcaciones_resumen cor "
    			+ "left join gth_empleado emp on emp.ide_gtemp =cor.ide_gtemp "
    			+ "where cor.fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' "
    			+ "and cor.ide_gtemp in(select ide_gtemp from gth_empleado where ide_astur=1 "
    			+ "and (aprueba_hora_extra_cobmr = FALSE OR aprueba_hora_extra_cobmr IS NULL)  "
    			+ "order by ide_gtemp asc) "
    			+ "group by cor.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP,emp.apellido_paterno_gtemp,emp.apellido_materno_gtemp,primer_nombre_gtemp,emp.segundo_nombre_gtemp "
    			+ "order by emp.apellido_paterno_gtemp,emp.apellido_materno_gtemp,primer_nombre_gtemp,emp.segundo_nombre_gtemp  ","IDE_GTEMP");

    	sel_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
    	sel_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);

		//sel_empleado.setRadio();
    	sel_empleado.setTitle("Seleccione Empleado");
    	sel_empleado.setWidth("50");
    	sel_empleado.setHeight("47");


		gru_pantalla.getChildren().add(sel_empleado);
		sel_empleado.getBot_aceptar().setMetodo("actualizarEmpleadoHorasExtra");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
		utilitario.addUpdate("sel_empleado");
	  
	  
	  
	  
	  

	
}










//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
public void cuatroUno(String fechaIni, String fechaFin,int bandera,String empleado_atrasos){
	
int mesIni=utilitario.getMes(fechaIni);
int mesFin=utilitario.getMes(fechaFin);
int anioIni=utilitario.getAnio(fechaIni);
int anioFin=utilitario.getAnio(fechaFin);

if (anioIni==anioFin) {
	if (mesIni==mesFin) {
		
	}else {
		utilitario.agregarMensajeInfo("Debe seleccionar un solo mes a la vez", "Debe coincidir el mes de la Fecha Inicio y Fin");
		return;
	}	
}else {
	utilitario.agregarMensajeInfo("Debe seleccionar un solo anio a la vez", "Debe coincidir el anio de la Fecha Inicio y Fin");
	return;
}

TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");


 TablaGenerica tab_resumen_horas=utilitario.consultar("SELECT ide_asrhe, ide_gtemp, observacion_asrhe, horafinextra_asrhe, "
		+ "recargonocturno25_asrhe, recargonocturno100_asrhe, activo_asrhe, "
		+ "mes_asrhe, ide_geani,horas25_loep_asrhe,horas60_loep_asrhe,horas50_ct_asrhe  "
		+ "FROM asi_resumen_horas_extra "
		+ "WHERE mes_asrhe="+mesIni+" and ide_geani="+tabAnio.getValor("IDE_GEANI")+"");
 
 

 



 if (tab_resumen_horas.getTotalFilas()<=0) {

	boolean salidaAlm=false,bandSalidaHorarioAlm=false;

	/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
			+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
	*/
	 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

	 
	 
	 
	String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//	double tiempoTrabajoXDia=0;
	double tiempoTrabajoXDia=0.0;
	int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
	double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,horaExtra25=0.0,horaExtra100=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	double acumuladoVacaciones25=0.0,acumuladoVacacionesTemp25=0.0,acumuladoVacaciones100=0.0,acumuladoVacacionesTemp100=0.0;
	double minutos=0.0,minutos25=0.0,minutos100=0.0;
	int horas=0,horas25=0,horas100=0;
	int p_t_enteraTemp=0,p_t_enteraTemp25=0,p_t_enteraTemp100=0;	
	double p_t_decimalTemp=0,p_t_decimalTemp25=0,p_t_decimalTemp100=0;	
    int valorFinProces=tab_tabla_sumatoria.getTotalFilas()-1;
    TablaGenerica tab_atrasos=this.getSumatoriaAtrasos(fechaIni,fechaFin,empleado_atrasos);
    
    double horaFinExtra_=0.00,horaRecargo25Entrada_=0.00,horaRecargo100Entrada_=0.00,horas25_loep_asrhe_=0.00,horas60_loep_asrhe_=0.00,horas50_ct_asrhe_=0.00;
    
    
	if (tab_atrasos.getTotalFilas()>0) {

	
	for (int i = 0; i < tab_tabla_hora_extra.getTotalFilas(); i++) {
			    
		    String horaFinExtra=tab_tabla_hora_extra.getValor(i,"horafinextra_cobmr");
		    String horaRecargo25Entrada=tab_tabla_hora_extra.getValor(i,"recargonocturno25_cobmr");
		    String  horaRecargo100Entrada=tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr");
					    
		    String horas25_loep_asrhe=tab_tabla_hora_extra.getValor(i,"horas25_loep_asrhe");
		    String horas60_loep_asrhe=tab_tabla_hora_extra.getValor(i,"horas60_loep_asrhe");
		    String  horas50_ct_asrhe=tab_tabla_hora_extra.getValor(i,"horas50_ct_asrhe");
		    
		    
		    String  fecha=tab_tabla_hora_extra.getValor(i,"fecha_evento_cobmr");
		    String mes="";
		    int mesTemporal=0;
		    mesTemporal=utilitario.getMes(fecha);
		    
		    //mes < 10 entonces le añado el 0 antes
		    if (mesTemporal<10) {
				mes="0"+mesTemporal;
			}else {
				mes=""+mesTemporal;

			}
		    
		    		
		    
			int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
			double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	

			//empleado temporal resumen
			empleadoTemporal=Integer.parseInt(tab_atrasos.getValor(y, "ide_gtemp"));
			//empleado de la tabla detalle
			empleado= Integer.parseInt(tab_tabla_hora_extra.getValor(i, "ide_gtemp"));

			//si quedan marcaciones por procesar del empleado
			if (empleado==empleadoTemporal) {
					//Si aun tiene marcacciones el empleado 
			anteriorEmpleado=1;
			}else {
					//Si ya se acbo las marcaciones	
			anteriorEmpleado=0;
					//sumo otro empleado
			y++;
			}	
			
			
			if (tab_tabla_hora_extra.getValor(i, "horas25_loep_cobph")==null || tab_tabla_hora_extra.getValor(i, "horas25_loep_cobph").equals("") || tab_tabla_hora_extra.getValor(i, "horas25_loep_cobph").isEmpty()) {
				horas25_loep_asrhe_=0.00;
			}else {
				horas25_loep_asrhe_=Double.parseDouble(tab_tabla_hora_extra.getValor(i, "horas25_loep_cobph"));
				}

			if (tab_tabla_hora_extra.getValor(i, "horas60_loep_cobph")==null || tab_tabla_hora_extra.getValor(i, "horas60_loep_cobph").equals("") || tab_tabla_hora_extra.getValor(i, "horas60_loep_cobph").isEmpty()) {
				horas60_loep_asrhe_=0.00;
			}else {
				horas60_loep_asrhe_=Double.parseDouble(tab_tabla_hora_extra.getValor(i, "horas60_loep_cobph"));
			}
			
			if (tab_tabla_hora_extra.getValor(i, "horas25_ct_cobph")==null || tab_tabla_hora_extra.getValor(i, "horas25_ct_cobph").equals("") || tab_tabla_hora_extra.getValor(i, "horas25_ct_cobph").isEmpty()) {
				horaRecargo25Entrada_=0.00;
			}else {
				horaRecargo25Entrada_=Double.parseDouble(tab_tabla_hora_extra.getValor(i, "horas25_ct_cobph"));
			}

			if (tab_tabla_hora_extra.getValor(i, "horas50_ct_cobmr")==null || tab_tabla_hora_extra.getValor(i, "horas50_ct_cobmr").equals("") || tab_tabla_hora_extra.getValor(i, "horas50_ct_cobmr").isEmpty()) {
				horas50_ct_asrhe_=0.00;
			}else {
				horas50_ct_asrhe_=Double.parseDouble(tab_tabla_hora_extra.getValor(i, "horas50_ct_cobph"));
			}
			 
			if (tab_tabla_hora_extra.getValor(i, "horas100_loep_ct_cobph")==null || tab_tabla_hora_extra.getValor(i, "horas100_loep_ct_cobph").equals("") || tab_tabla_hora_extra.getValor(i, "horas100_loep_ct_cobph").isEmpty()) {
				horaFinExtra_=0.00;
			}else {
				horaFinExtra_=Double.parseDouble(tab_tabla_hora_extra.getValor(i, "horas100_loep_ct_cobph"));
			}

			}
			
			

	}
 }
}

				

				
//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
public void cuatroUno_(String fechaIni, String fechaFin,int bandera,String empleado_atrasos){
				
int mesIni=utilitario.getMes(fechaIni);
int mesFin=utilitario.getMes(fechaFin);
int anioIni=utilitario.getAnio(fechaIni);
int anioFin=utilitario.getAnio(fechaFin);

if (anioIni==anioFin) {
	if (mesIni==mesFin) {
				
	}else {
		utilitario.agregarMensajeInfo("Debe seleccionar un solo mes a la vez", "Debe coincidir el mes de la Fecha Inicio y Fin");
		return;
	}	
}else {
	utilitario.agregarMensajeInfo("Debe seleccionar un solo anio a la vez", "Debe coincidir el anio de la Fecha Inicio y Fin");
	return;
}
				
TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");

				


 TablaGenerica tab_resumen_horas=utilitario.consultar("SELECT ide_asrhe, ide_gtemp, observacion_asrhe, horafinextra_asrhe, "
		+ "recargonocturno25_asrhe, recargonocturno100_asrhe, activo_asrhe, "
		+ "mes_asrhe, ide_geani,horas25_loep_asrhe,horas60_loep_asrhe,horas50_ct_asrhe  "
		+ "FROM asi_resumen_horas_extra "
		+ "WHERE mes_asrhe="+mesIni+" and ide_geani="+tabAnio.getValor("IDE_GEANI")+"");
							


					
								


 //if (tab_resumen_horas.getTotalFilas()<=0) {

	boolean salidaAlm=false,bandSalidaHorarioAlm=false;
								
	/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
			+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
	*/
	 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();

								

	 TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT ide_cobmr, ide_gtemp, fecha_evento_cobmr, horainiciohorario_cobmr,  "
				+ "	 horainiciobiometrico_cobmr, horainicioband_cobmr, horainicioalm_cobmr,  "
				+ "	 horafinalm_cobmr, horainicioalmbio_cobmr, horafinalmbio_cobmr,  "
				+ "	 tiempoalm_cobmr, tiempohoralm_cobmr, horafinhorario_cobmr, horafinbiometrico_cobmr,  "
				+ "	 horafinband_cobmr, novedad_cobmr, usuario_ingre, fecha_ingre,  "
				+ "	 hora_ingre, usuario_actua, fecha_actua, hora_actua, aprueba_hora_extra_cobmr,  "
				+ "  dia_cobmr, p_entrada_cobmr, p_salida_cobmr, p_alm_cobmr, inconsistencia_biometrico_cobmr,  "
				+ "	 ide_aspvh, horafinextra_cobmr, recargonocturno25_cobmr, recargonocturno100_cobmr,  "
				+ "  biometrico_entrada_cobmr, biometrico_alm_entrada, biometrico_alm_salida,  "
				+ "	 biometrico_salida_cobmr, tipo_horario_cobmr, horas25_loep_cobmr,  "
				+ "	 horas60_loep_cobmr, horas50_ct_cobmr "
				+ ", horainiciohorariohxe_cobmr,  "
				+ "	 horainiciobiometricohxe_cobmr, horainiciobandhxe_cobmr, horafinhorariohxe_cobmr, "
				+ "	 horafinbiometricohxe_cobmr, horafinbandhxe_cobmr, ide_gtgre_extra "
				+ "	 FROM con_biometrico_marcaciones_resumen  "
				+ "	 where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'    "
			//	+ "	 and horainiciobiometrico_cobmr !='' and horafinbiometrico_cobmr !=''   "
			//	+ "	 and ide_gtemp in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174)  "
				//+ "	and ide_gtemp in(1062,194,193,)  "
								
				+ "	 order by ide_gtemp asc, fecha_evento_cobmr asc  ");
							

	String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//	double tiempoTrabajoXDia=0;
	double tiempoTrabajoXDia=0.0;
	int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
	double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,horaExtra25=0.0,horaExtra100=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	double acumuladoVacaciones25=0.00,acumuladoVacacionesTemp25=0.00,acumuladoVacaciones100=0.00,acumuladoVacacionesTemp100=0.00,
	acumuladoVacaciones50=0.00,acumuladoVacacionesTemp50=0.00,acumuladoVacaciones60=0.00,acumuladoVacacionesTemp60=0.00
			,acumuladoVacacionesTemp25Codigo=0.00,acumuladoVacaciones25Codigo=0.00;

								
	double minutos=0.0,minutos25=0.0,minutos100=0.0;
	int horas=0,horas25=0,horas100=0;
	int p_t_enteraTemp=0,p_t_enteraTemp25=0,p_t_enteraTemp100=0;	
	double p_t_decimalTemp=0,p_t_decimalTemp25=0,p_t_decimalTemp100=0;	
    int valorFinProces=tab_tabla_sumatoria.getTotalFilas()-1;
    TablaGenerica tab_atrasos=this.getSumatoriaAtrasos(fechaIni,fechaFin,empleado_atrasos);
    int a=0;
    double horaFinExtra_=0.00,horaRecargo25Entrada_=0.00,horaRecargo100Entrada_=0.00,horas25_loep_asrhe_=0.00,horas60_loep_asrhe_=0.00,horas50_ct_asrhe_=0.00;
    int valorFin=0;

	//if (tab_atrasos.getTotalFilas()>0) {
								
	y=0;
	for (int i = 0; i < tabConBiometricoMarcacionesResumen.getTotalFilas(); i++) {
								
		    String horaFinExtra="";
		    String horaRecargo25Entrada="";
		    String  horaRecargo100Entrada="";
			String horas25_loep_asrhe="";
		    String horas60_loep_asrhe="";
		    String  horas50_ct_asrhe="";

		    String  fecha=tabConBiometricoMarcacionesResumen.getValor(i,"fecha_evento_cobmr");
		    String mes="";
		    int mesTemporal=0;
		    mesTemporal=utilitario.getMes(fecha);

		    //mes < 10 entonces le a�ado el 0 antes
		    if (mesTemporal<10) {
				mes="0"+mesTemporal;
								}else {
				mes=""+mesTemporal;

								}
								
					
								
			int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
			double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	
								
			//empleado temporal resumen
			empleadoTemporal=Integer.parseInt(tab_atrasos.getValor(y, "ide_gtemp"));
			System.out.println("empleadoTemporal"+empleadoTemporal);
			//empleado de la tabla detalle
			empleado= Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(i, "ide_gtemp"));
			System.out.println("empleado"+empleado);

			//si quedan marcaciones por procesar del empleado
			if (empleado==empleadoTemporal) {
					//Si aun tiene marcacciones el empleado 
			anteriorEmpleado=1;
								}else {
					//Si ya se acbo las marcaciones	
			anteriorEmpleado=0;
					//sumo otro empleado
			y++;
								}
							
								
			///String sCadena = "Hola Mundo";
			//String sSubCadena = sCadena.substring(5,10);
			//System.out.println(sSubCadena);


			if (tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobmr")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobmr").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobmr").isEmpty()) {
				horas25_loep_asrhe_=0.00;
			}else {
		
				 horas25_loep_asrhe=tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobmr");
				 String sSubCadena = horas25_loep_asrhe.substring(0,2);
				
				horas25_loep_asrhe_=Double.parseDouble(sSubCadena);
				}

			if (tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobmr")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobmr").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobmr").isEmpty()) {
				horas60_loep_asrhe_=0.00;
				}else {
				horas60_loep_asrhe=tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobmr");
				 String sSubCadena = horas60_loep_asrhe.substring(0,2);

				horas60_loep_asrhe_=Double.parseDouble(sSubCadena);
				}

			if (tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno25_cobmr")==null || tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno25_cobmr").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno25_cobmr").isEmpty()) {
				horaRecargo25Entrada_=0.00;
				}else {
				horaRecargo25Entrada=tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno25_cobmr");
				 String sSubCadena = horaRecargo25Entrada.substring(0,2);

				horaRecargo25Entrada_=Double.parseDouble(sSubCadena);
				}
				
			if (tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobmr")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobmr").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobmr").isEmpty()) {
				horas50_ct_asrhe_=0.00;
				}else {
				horas50_ct_asrhe=tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobmr");
				 String sSubCadena = horas50_ct_asrhe.substring(0,2);

				horas50_ct_asrhe_=Double.parseDouble(sSubCadena);
				}
				
			if (tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno100_cobmr")==null || tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno100_cobmr").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno100_cobmr").isEmpty()) {
				horaFinExtra_=0.00;
				}else {
				horaFinExtra=tabConBiometricoMarcacionesResumen.getValor(i, "recargonocturno100_cobmr");
				 String sSubCadena = horaFinExtra.substring(0,2);
				horaFinExtra_=Double.parseDouble(sSubCadena);
				}
				
			//getTipoEmpleado(int ide_gtemp)

			if (anteriorEmpleado==1) {
				//Si aun tiene marcacciones el empleado
				acumuladoVacaciones=horaExtra;
				acumuladoVacaciones25=horas25_loep_asrhe_+acumuladoVacaciones25;
				acumuladoVacaciones100=horaFinExtra_+acumuladoVacaciones100;
				acumuladoVacaciones50=horas50_ct_asrhe_+acumuladoVacaciones50;
				acumuladoVacaciones60=horas60_loep_asrhe_+acumuladoVacaciones60;
				acumuladoVacaciones25Codigo=horaRecargo25Entrada_+acumuladoVacaciones25Codigo;			
				valorFin++;
				if (tabConBiometricoMarcacionesResumen.getTotalFilas()==valorFin) {
					//acumuladoVacaciones=horaExtra;
					//acumuladoVacaciones25=horas25_loep_asrhe_+acumuladoVacaciones25;
					//acumuladoVacaciones100=horaFinExtra_+acumuladoVacaciones100;
					//acumuladoVacaciones50=horas50_ct_asrhe_+acumuladoVacaciones50;
					//acumuladoVacaciones60=horas60_loep_asrhe_+acumuladoVacaciones60;
					//acumuladoVacaciones25Codigo=horaRecargo25Entrada_+acumuladoVacaciones25Codigo;

					insertarTablaResumenHorasExtra(empleadoTemporal, "", ""+acumuladoVacacionesTemp, ""+acumuladoVacaciones25Codigo, ""+acumuladoVacaciones100, true, 
							mesIni,Integer.parseInt(tabAnio.getValor("IDE_GEANI")), true, 
							""+acumuladoVacaciones25, ""+acumuladoVacaciones60, ""+acumuladoVacaciones50);

					}
				
			}else {
				//Si el empleado ya no tiene marcaciones
				String valor="";	

				
				int empleadoIngreso=Integer.parseInt(tab_atrasos.getValor(a, "ide_gtemp"));
				//insertarTablaResumenHorasExtra(empleadoTemporal,"",""+acumuladoVacacionesTemp,""+acumuladoVacacionesTemp25,""+acumuladoVacacionesTemp100,true,mesIni,Integer.parseInt(tabAnio.getValor("IDE_GEANI")));  		
				
				insertarTablaResumenHorasExtra(empleadoTemporal, "", ""+acumuladoVacacionesTemp, ""+acumuladoVacaciones25Codigo, ""+acumuladoVacaciones100, true, 
						mesIni,Integer.parseInt(tabAnio.getValor("IDE_GEANI")), true, 
						""+acumuladoVacaciones25, ""+acumuladoVacaciones60, ""+acumuladoVacaciones50);

				

				a++;


				x++;
			valorFin++;	

			
				acumuladoVacaciones=0.00;
				acumuladoVacaciones25=0.00;
				acumuladoVacaciones100=0.00;
				acumuladoVacaciones50=0.00;
				acumuladoVacaciones60=0.00;
				acumuladoVacaciones25Codigo=0.00;

				
				acumuladoVacaciones=horaExtra;
				acumuladoVacaciones25=horas25_loep_asrhe_+acumuladoVacaciones25;
				acumuladoVacaciones100=horaFinExtra_+acumuladoVacaciones100;
				acumuladoVacaciones50=horas50_ct_asrhe_+acumuladoVacaciones50;
				acumuladoVacaciones60=horas60_loep_asrhe_+acumuladoVacaciones60;
				acumuladoVacaciones25Codigo=horaRecargo25Entrada_+acumuladoVacaciones25Codigo;
		

				empleadoTemporal=Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(i, "ide_gtemp"));
		
			
			}
			
	
			
		}
	    	
	//}    	
	
//}else {
	    	
//}


}


 /*

public void guardarHorasEmpleado(String fechaIni, String fechaFin){
	int mesFin=0,anioFin=0,anio=0;
	int mes=utilitario.getMes(fechaIni);
	int anioInicio=utilitario.getAnio(fechaIni);
	TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani  "
			+ "from gen_anio "
			+ "where detalle_geani like '%"+anioInicio+"%'");
	anio=Integer.parseInt(tabAnio.getValor("IDE_GEANI"));
	TablaGenerica tabResumenHorasExtraXEmpleado=utilitario.consultar("SELECT ide_asrhe, ide_gtemp, "
			+ "mes_asrhe, ide_geani "
			+ "FROM asi_resumen_horas_extra");
	if (tabResumenHorasExtraXEmpleado.getTotalFilas()>0) {
		utilitario.getConexion().ejecutarSql("delete from asi_resumen_horas_extra");
	}else {
		
	}
	
	if (tab_tabla_sumatoria.getTotalFilas()>0) {
		
				for (int i = 0; i < tab_tabla_sumatoria.getTotalFilas(); i++) {
					insertarTablaResumenHorasExtra(Integer.parseInt(tab_tabla_sumatoria.getValor(i,"IDE_GTEMP")),fechaIni,fechaFin,
								"",""+tab_tabla_sumatoria.getValor(i,"HORAS_NOCTURNAS"),""+tab_tabla_sumatoria.getValor(i,"HORAS_SUPLEMENTARIAS"),""+tab_tabla_sumatoria.getValor(i,"HORAS_EXTRA"),true);								
					}
		
	}else {
		
	}
	
}
*/

	

//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
public void cuatroUnoXEmpleado(String ide_gtemp,String fechaIni, String fechaFin){
	

	boolean salidaAlm=false,bandSalidaHorarioAlm=false;

	/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
			+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
			+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
	*/
	 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


	String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//	double tiempoTrabajoXDia=0;
	double tiempoTrabajoXDia=0.0;
	int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
	double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,horaExtra25=0.0,horaExtra100=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	double acumuladoVacaciones25=0.0,acumuladoVacacionesTemp25=0.0,acumuladoVacaciones100=0.0,acumuladoVacacionesTemp100=0.0;
	
	
	double minutos=0.0,minutos25=0.0,minutos100=0.0;
	int horas=0,horas25=0,horas100=0;
	int p_t_enteraTemp=0,p_t_enteraTemp25=0,p_t_enteraTemp100=0;	
	double p_t_decimalTemp=0,p_t_decimalTemp25=0,p_t_decimalTemp100=0;	

	int cont=0;
	
	//int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
	//double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	
	
	/*TablaGenerica  tab_tabla_hora_extraXEmpleado=utilitario.consultar("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP, "
			+ "res.ide_cobmr, "					
			+ "res.fecha_evento_cobmr, "
			+ "res.dia_cobmr, "
			+ "res.horainiciohorario_cobmr, "
			+ "res.horafinhorario_cobmr,  "
			+ "res.horainiciobiometrico_cobmr, "
			+ "res.horafinbiometrico_cobmr,  "
			+ "res.recargonocturno25_cobmr,  "
			+ "res.horafinextra_cobmr,  "
			+ "res.recargonocturno100_cobmr, "
			+ "GTT.DETALLE_GTTEM  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
			+ " and emp.ide_gtemp="+ide_gtemp+" "   
			+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
	*/
	
	

	TablaGenerica  tab_tabla_hora_extraXEmpleado=utilitario.consultar("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "cobmr.ide_cobmr,  "
			+ "cobmr.fecha_evento_cobmr,  "
			+ "cobmr.dia_cobmr,  "
			+ "res.actividades_cobph,  "
			+ "cobmr.horainiciohorario_cobmr,  "
			+ "cobmr.horafinhorario_cobmr,   "
			+ "cobmr.horainiciobiometrico_cobmr,  "
			+ "cobmr.horafinbiometrico_cobmr,  "
			+ "cobmr.horas25_loep_cobmr ,   "
			+ "cobmr.horas60_loep_cobmr ,   "
			+ "cobmr.recargonocturno25_cobmr ,  	"
			+ "cobmr.horas50_ct_cobmr ,  "
			+ "cobmr.recargonocturno100_cobmr,  "
			+ "cobmr.dias_laborados_cobmr  "
			+ "FROM con_biometrico_marcaciones_resumen cobmr   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
			+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
			+ "where fecha_evento_cobmr between '"+fechaConsultaInicio+"' and '"+fechaConsultaFin+"'   "  
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

	
	
	
    int valorFinProces=tab_tabla_hora_extraXEmpleado.getTotalFilas()-1;
	 
	
	
	for (int i = 0; i < tab_tabla_hora_extraXEmpleado.getTotalFilas(); i++) {
		
			//  System.out.println("cont  :"+cont);
			 // System.out.println("total : "+tab_tabla_hora_extraXEmpleado.getTotalFilas());

			  
				
				
				if (tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr").isEmpty()) {
					horaExtra=0.0;
				}else {
					horaExtra=Double.parseDouble(actualizarTablaDosPuntosAPunto(tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr")));

				}
				
				
				if (tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr").isEmpty()) {
					horaExtra25=0.0;
				}else {
					horaExtra25=Double.parseDouble(actualizarTablaDosPuntosAPunto(tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr")));

				}
				 
				 
				
				if (tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr").isEmpty()) {
					horaExtra100=0.0;
				}else {
					horaExtra100=Double.parseDouble(actualizarTablaDosPuntosAPunto(tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr")));
				//	System.out.println("100  recargo "+tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr"));

				}
				
				int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
				double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	
				

			    //Acumulo el valor de la hora extra para el nuevo empleado 
			    p_t_entera=(int)horaExtra+p_t_entera;
			    p_t_enteraTemp=p_t_enteraTemp+p_t_entera;
          //System.out.println("parte entera Acumulado: "+p_t_enteraTemp);

				p_t_decimal=(horaExtra-p_t_entera)+p_t_decimal;
				p_t_decimalTemp=p_t_decimalTemp+p_t_decimal;
				//System.out.println("parte entera Decimal: "+p_t_decimalTemp);

				p_t_entera25=(int)horaExtra25+p_t_entera25;
				p_t_enteraTemp25=p_t_enteraTemp25+p_t_entera25;
				//System.out.println("parte entera Acumulado25: "+p_t_enteraTemp25);

			
				p_t_decimal25=(horaExtra25-p_t_entera25)+p_t_decimal25;
				p_t_decimalTemp25=p_t_decimalTemp25+p_t_decimal25;
				//System.out.println("parte entera Decimal Acumulado25:  "+p_t_decimalTemp25);

				
				p_t_entera100=(int)horaExtra100+p_t_entera100;
				p_t_enteraTemp100=p_t_enteraTemp100+p_t_entera100;
				//System.out.println("parte entera  Acumulado100:  "+p_t_enteraTemp100);
		
				p_t_decimal100=(horaExtra100-p_t_entera100)+p_t_decimal100;
				p_t_decimalTemp100=p_t_decimalTemp100+p_t_decimal100;
				//System.out.println("parte entera  Decimal Acumulado100:  "+p_t_decimalTemp100);

				
				if ((i)==valorFinProces) {
				
				
				if (  (((p_t_decimalTemp)/0.60) >= 1.0) || (((p_t_decimalTemp)/0.60) >= 1.0)  ) {
					horas=(int)((p_t_decimalTemp)/0.60);
					minutos=(((p_t_decimalTemp)/0.60)-horas)*0.60;

				}else {
					horas=0;
					minutos=((p_t_decimalTemp));

				}
				acumuladoVacacionesTemp=(p_t_enteraTemp)+(horas)+(minutos);
				acumuladoVacaciones=acumuladoVacaciones+acumuladoVacacionesTemp;
				
				
				
				horas25=(int)((p_t_decimalTemp25)/0.60);
				minutos25=((p_t_decimalTemp25)/0.60)-horas25;
				
			
				if (  (((p_t_decimalTemp25)/0.60) >= 1.0) || (((p_t_decimalTemp25)/0.60) >= 1.0)  ) {
					horas25=(int)((p_t_decimalTemp25)/0.60);
					minutos25=(((p_t_decimalTemp25)/0.60)-horas25)*0.60;

				}else {
					horas25=0;
					minutos25=((p_t_decimalTemp25));
				}
				
				acumuladoVacacionesTemp25=(p_t_enteraTemp25)+(horas25)+(minutos25);
				acumuladoVacaciones25=acumuladoVacaciones25+acumuladoVacacionesTemp25;
				
				//Horas que se crean de los minutos 
				horas100=(int)((p_t_decimalTemp100)/0.60);
				minutos100=((p_t_decimalTemp100)/0.60)-horas100;
				
				if (  (((p_t_decimalTemp100)/0.60) >= 1.0) || (((p_t_decimalTemp100)/0.60) >= 1.0)  ) {
					horas100=(int)((p_t_decimalTemp100)/0.60);
					minutos100=(((p_t_decimalTemp100)/0.60)-horas100)*0.60;

				}else {
					horas100=0;
					minutos100=((p_t_decimalTemp100));
				}
				
				acumuladoVacacionesTemp100=(p_t_enteraTemp100)+(horas100)+(minutos100);
				acumuladoVacaciones100=acumuladoVacaciones100+acumuladoVacacionesTemp100;
				
				
				
				
				tab_tabla_sumatoria.setValor(0,"HORAS_NOCTURNAS",""+nuevaHoraExtraResumen(actualizarTabla(""+acumuladoVacaciones25)));
				tab_tabla_sumatoria.setValor(0,"HORAS_SUPLEMENTARIAS",""+nuevaHoraExtraResumen(actualizarTabla(""+acumuladoVacaciones)));
				tab_tabla_sumatoria.setValor(0,"HORAS_EXTRA",""+nuevaHoraExtraResumen(actualizarTabla(""+acumuladoVacaciones100)));
				cont++;
				}
				
				
				
				
				
				
				
	}
}



/*public void cuatroUnoXEmpleado2324244(String ide_gtemp){
	

	boolean salidaAlm=false,bandSalidaHorarioAlm=false;

	 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
	 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


	String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//	double tiempoTrabajoXDia=0;
	double tiempoTrabajoXDia=0.0;
	int empleadoTemporal=0,empleado=0,anteriorEmpleado=0,x=0,y=0;
	double tiempoHorasExtra100Auxiliar=0.0,horaExtra=0.0,horaExtra25=0.0,horaExtra100=0.0,acumuladoVacaciones=0.0,acumuladoVacacionesTemp=0.0;
	double acumuladoVacaciones25=0.0,acumuladoVacacionesTemp25=0.0,acumuladoVacaciones100=0.0,acumuladoVacacionesTemp100=0.0;
	double minutos=0.0,minutos25=0.0,minutos100=0.0;
	int horas=0,horas25=0,horas100=0;
	int p_t_enteraTemp=0,p_t_enteraTemp25=0,p_t_enteraTemp100=0;	
	double p_t_decimalTemp=0,p_t_decimalTemp25=0,p_t_decimalTemp100=0;	

	int cont=0;
	
	
	TablaGenerica  tab_tabla_hora_extraXEmpleado=utilitario.consultar("SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP, "
			+ "res.ide_cobmr, "					
			+ "res.fecha_evento_cobmr, "
			+ "res.dia_cobmr, "
			+ "res.horainiciohorario_cobmr, "
			+ "res.horafinhorario_cobmr,  "
			+ "res.horainiciobiometrico_cobmr, "
			+ "res.horafinbiometrico_cobmr,  "
			+ "res.recargonocturno25_cobmr,  "
			+ "res.horafinextra_cobmr,  "
			+ "res.recargonocturno100_cobmr, "
			+ "GTT.DETALLE_GTTEM  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' "
			+ " and emp.ide_gtemp="+ide_gtemp+" "   
			+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
	
	
	 
	
	
	for (int i = 0; i < tab_tabla_hora_extraXEmpleado.getTotalFilas(); i++) {
			  cont++;  
			//  System.out.println("cont  :"+cont);
			 // System.out.println("total : "+tab_tabla_hora_extraXEmpleado.getTotalFilas());

			  
				int p_t_entera=0,p_t_entera25=0,p_t_entera100=0;	
				double p_t_decimal=0,p_t_decimal25=0,p_t_decimal100=0;	

		
				
				

				if ((cont)==tab_tabla_hora_extraXEmpleado.getTotalFilas()) {
				//if ((cont)==tab_tabla_hora_extraXEmpleado.getTotalFilas()) {

				String valor="";
		//		System.out.println("ACUMULADO"+empleadoTemporal+"  --->   "+acumuladoVacacionesTemp);
				
				if (  (((p_t_decimalTemp)/0.60) >= 1.0) || (((p_t_decimalTemp)/0.60) >= 1.0)  ) {
					horas=(int)((p_t_decimalTemp)/0.60);
					minutos=(((p_t_decimalTemp)/0.60)-horas)*0.60;

				}else {
					horas=0;
					minutos=((p_t_decimalTemp));

				}

				
				acumuladoVacacionesTemp=(p_t_enteraTemp)+(horas)+(minutos);
				//System.out.println("acumuladoVacacionesTemp"+acumuladoVacacionesTemp);

				
				horas25=(int)((p_t_decimalTemp25)/0.60);
				//System.out.println("horas25"+horas25);

				minutos25=((p_t_decimalTemp25)/0.60)-horas25;
				
			
				if (  (((p_t_decimalTemp25)/0.60) >= 1.0) || (((p_t_decimalTemp25)/0.60) >= 1.0)  ) {
					horas25=(int)((p_t_decimalTemp25)/0.60);
					minutos25=(((p_t_decimalTemp25)/0.60)-horas25)*0.60;

				}else {
					horas25=0;
					minutos25=((p_t_decimalTemp25));

				}
				

				
				
				acumuladoVacacionesTemp25=(p_t_enteraTemp25)+(horas25)+(minutos25);


				horas100=(int)((p_t_decimalTemp100)/0.60);
				minutos100=((p_t_decimalTemp100)/0.60)-horas100;
				
				
				if (  (((p_t_decimalTemp100)/0.60) >= 1.0) || (((p_t_decimalTemp100)/0.60) >= 1.0)  ) {
					horas100=(int)((p_t_decimalTemp100)/0.60);
					minutos100=(((p_t_decimalTemp100)/0.60)-horas100)*0.60;

				}else {
					horas100=0;
					minutos100=((p_t_decimalTemp100));

				}
				
	
				
				

			
				
				acumuladoVacacionesTemp100=(p_t_enteraTemp100)+(horas100)+(minutos100);

				
				tab_tabla_sumatoria.setValor(0,"HORAS_NOCTURNAS",""+acumuladoVacacionesTemp25);
				tab_tabla_sumatoria.setValor(0,"HORAS_SUPLEMENTARIAS",""+acumuladoVacacionesTemp);
				tab_tabla_sumatoria.setValor(0,"HORAS_EXTRA",""+acumuladoVacacionesTemp100);


				
				acumuladoVacaciones=horaExtra;
				acumuladoVacacionesTemp=0;
				acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				//System.out.println("nuevoAcumulado"+acumuladoVacacionesTemp25);

				
				acumuladoVacaciones25=horaExtra25;
				acumuladoVacacionesTemp25=0;
				acumuladoVacacionesTemp25=acumuladoVacacionesTemp25+horaExtra25;
				//System.out.println("nuevoAcumulado25"+acumuladoVacacionesTemp25);
				
				acumuladoVacaciones100=horaExtra100;
				acumuladoVacacionesTemp100=0;
				acumuladoVacacionesTemp100=acumuladoVacacionesTemp100+horaExtra100;		
				//System.out.println("nuevoAcumulado100"+acumuladoVacacionesTemp25);

				empleadoTemporal=Integer.parseInt(tab_tabla_hora_extraXEmpleado.getValor(i, "ide_gtemp"));
				
	
				minutos=0.0;minutos25=0.0;minutos100=0.0;
				 horas=0;horas25=0;horas100=0;
				 p_t_decimalTemp=0.0;
				 p_t_decimalTemp25=0.0;
				 p_t_decimalTemp100=0.0;
			     p_t_enteraTemp=0;
			     p_t_enteraTemp25=0;
			     p_t_enteraTemp100=0;	

			    //Acumulo el valor de la hora extra para el nuevo empleado 
			    p_t_entera=(int)horaExtra+p_t_entera;
            p_t_enteraTemp=p_t_enteraTemp+p_t_entera;
            //System.out.println("parte entera Acumulado: "+p_t_enteraTemp);

				p_t_decimal=(horaExtra-p_t_entera)+p_t_decimal;
				p_t_decimalTemp=p_t_decimalTemp+p_t_decimal;
				//System.out.println("parte entera Decimal: "+p_t_decimalTemp);

				p_t_entera25=(int)horaExtra25+p_t_entera25;
            p_t_enteraTemp25=p_t_enteraTemp25+p_t_entera25;
            //System.out.println("parte entera Acumulado25: "+p_t_enteraTemp25);

			
				p_t_decimal25=(horaExtra25-p_t_entera25)+p_t_decimal25;
				p_t_decimalTemp25=p_t_decimalTemp25+p_t_decimal25;
				//System.out.println("parte entera Decimal Acumulado25:  "+p_t_decimalTemp25);

				
				p_t_entera100=(int)horaExtra100+p_t_entera100;
            p_t_enteraTemp100=p_t_enteraTemp100+p_t_entera100;
				//System.out.println("parte entera  Acumulado100:  "+p_t_enteraTemp100);
		
				p_t_decimal100=(horaExtra100-p_t_entera100)+p_t_decimal100;
				p_t_decimalTemp100=p_t_decimalTemp100+p_t_decimal100;
				//System.out.println("parte entera  Decimal Acumulado100:  "+p_t_decimalTemp100);

		
			
		
	}else {
		if(cont==tab_tabla_hora_extraXEmpleado.getTotalFilas()){
		}else {
			
		
		
		

		  String horaFinExtra=tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr");
		    String horaRecargo25Entrada=tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr");
		    String  horaRecargo100Entrada=tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr");
		//			    System.out.println("fecha"+tab_tabla_hora_extraXEmpleado.getValor(i,"fecha_evento_cobmr"));
		
		//empleadoTemporal=Integer.parseInt(tab_tabla_sumatoria.getValor(y, "ide_gtemp"));
//		System.out.println("empleadoTemporal   "+empleadoTemporal);
		//empleado= Integer.parseInt(tab_tabla_hora_extraXEmpleado.getValor(i, "ide_gtemp"));
//		System.out.println("empleado   "+empleado);


		//if (empleado==empleadoTemporal) {
		//anteriorEmpleado=1;
		//}else {
		//anteriorEmpleado=0;
		//y++;
		//}	
		
		
		
		if (tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr").isEmpty()) {
			horaExtra=0.0;
		}else {
			horaExtra=Double.parseDouble(tab_tabla_hora_extraXEmpleado.getValor(i,"horafinextra_cobmr"));

		}
		
		
		if (tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr").isEmpty()) {
			horaExtra25=0.0;
		}else {
			horaExtra25=Double.parseDouble(tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno25_cobmr"));

		}
		 
		 
		
		if (tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr")==null || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr").equals("") || tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr").isEmpty()) {
			horaExtra100=0.0;
		}else {
			horaExtra100=Double.parseDouble(tab_tabla_hora_extraXEmpleado.getValor(i,"recargonocturno100_cobmr"));
		//	System.out.println("100  recargo "+tab_tabla_hora_extra.getValor(i,"recargonocturno100_cobmr"));

		}
		
		
		
			//	System.out.println("fecha"+tab_tabla_hora_extra.getValor(i,"fecha_evento_cobmr"));

				acumuladoVacaciones=horaExtra;
				p_t_entera=(int)horaExtra+p_t_entera;
              p_t_enteraTemp=p_t_enteraTemp+p_t_entera;
			//	System.out.println("parte entera Acumulado: "+p_t_enteraTemp);

				p_t_decimal=(horaExtra-p_t_entera)+p_t_decimal;
				p_t_decimalTemp=p_t_decimalTemp+p_t_decimal;
			//	System.out.println("parte entera Decimal: "+p_t_decimalTemp);
				
			//	System.out.println("p_t_decimalTemp"+p_t_decimalTemp);

				//acumuladoVacacionesTemp=acumuladoVacacionesTemp+horaExtra;
				
				
				acumuladoVacaciones25=horaExtra25;
				p_t_entera25=(int)horaExtra25+p_t_entera25;
              p_t_enteraTemp25=p_t_enteraTemp25+p_t_entera25;
		//		System.out.println("parte entera Acumulado25: "+p_t_enteraTemp25);

				
				
				p_t_decimal25=(horaExtra25-p_t_entera25)+p_t_decimal25;
				p_t_decimalTemp25=p_t_decimalTemp25+p_t_decimal25;
			//	System.out.println("parte entera Decimal Acumulado25:  "+p_t_decimalTemp25);
			//	System.out.println("p_t_entera25"+p_t_entera25);
//				System.out.println("horaExtra100"+horaExtra100);

				
				acumuladoVacaciones100=horaExtra100;
				p_t_entera100=(int)horaExtra100+p_t_entera100;
              p_t_enteraTemp100=p_t_enteraTemp100+p_t_entera100;
			//	System.out.println("parte entera  Acumulado100:  "+p_t_enteraTemp100);


							
				p_t_decimal100=(horaExtra100-p_t_entera100)+p_t_decimal100;
				p_t_decimalTemp100=p_t_decimalTemp100+p_t_decimal100;
			//	System.out.println("parte entera  Decimal Acumulado100:  "+p_t_decimalTemp100);


				//	System.out.println("p_t_entera100"+p_t_entera100);
			//	System.out.println("p_t_decimal100"+p_t_decimal100);

				//acumuladoVacacionesTemp100=acumuladoVacacionesTemp100+horaExtra;
	

	
		}	
		}
	}
	    	


}



*/





	
	public TablaGenerica getSumatoriaAtrasos(String strFechaIniReporte, String strFechaFinReporte,String ide_gtemp){
		
		
		
		TablaGenerica tabAtrasos=utilitario.consultar("SELECT IDE_GTEMP, IDE_GTEMP "
				+ "FROM con_biometrico_marcaciones_resumen    "
				//+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
				//+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
				+ "where fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "  
				//+ "and cobmr.ide_gtemp in(270)  "
			//	+ "	 and ide_gtemp in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174)  "
				+ "group by IDE_GTEMP   "
				+ "ORDER BY ide_gtemp asc");

		
		
		
		/*TablaGenerica tabAtrasos=utilitario.consultar("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||    "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,  "
			+ "'' as HORAS_NOCTURNAS,  "
			+ "'' as HORAS_SUPLEMENTARIAS,   "
			+ "'' as HORAS_EXTRA  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "s
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
			+ "and res.aprueba_hora_extra_cobmr=true  and emp.ide_gtemp in("+ide_gtemp+") "  
			+ "GROUP BY EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP ,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM,HORAS_NOCTURNAS, HORAS_SUPLEMENTARIAS,HORAS_EXTRA "
			+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc");*/
		
		return tabAtrasos;
}
		
		
		
		

	public String getTotalXDepartamento(){
	
	
	String consulta ="SELECT   "
			+ "DEPA.IDE_GEDEP,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP, "
			+ "'' as HORAS_NOCTURNAS,  "
			+ "'' as HORAS_SUPLEMENTARIAS,   "
			+ "'' as HORAS_EXTRA  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU   "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
			+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '2018-02-02' and '2018-02-28'  "
			+ "and res.aprueba_hora_extra_cobmr=true    "
			+ "GROUP BY  DEPA.IDE_GEDEP,AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP,HORAS_NOCTURNAS, HORAS_SUPLEMENTARIAS,HORAS_EXTRA  "
			+ "ORDER BY AREA.DETALLE_GEARE asc ";	
return consulta;
		
		
		
	}

 

	
	
	
	
	
	
	
	
	
	
	
	
	
	
 

	public Tabla getTab_tabla_sumatoria() {
		return tab_tabla_sumatoria;
	}



	public void setTab_tabla_sumatoria(Tabla tab_tabla_sumatoria) {
		this.tab_tabla_sumatoria = tab_tabla_sumatoria;
	}



	
	
	
	
	public String getDiferenciaEntradaSalida(String strFechaIniReporte, String strFechaFinReporte){
		
		String sql="";
		if (strFechaFinReporte==null || strFechaFinReporte.equals("") || strFechaFinReporte.isEmpty() || strFechaIniReporte==null || strFechaIniReporte.equals("")
				|| strFechaIniReporte.isEmpty()) {
			
		
		
		sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.ide_cobmr, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horafinhorario_cobmr,  "
				+ "res.horafinbiometrico_cobmr,  "
				+ "res.horafinextra_cobmr,  "
				+ "res.recargonocturno25_cobmr,  "
				+ "res.recargonocturno100_cobmr, "
				+ "GTT.DETALLE_GTTEM  "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
				+ "where epar.activo_geedp=true  and res.aprueba_hora_extra_cobmr=true   "
				+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
		}else {
			sql="SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.ide_cobmr, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horafinhorario_cobmr,  "
					+ "res.horafinbiometrico_cobmr,  "
					+ "res.horafinextra_cobmr,  "
					+ "res.recargonocturno25_cobmr,  "
					+ "res.recargonocturno100_cobmr, "
					+ "GTT.DETALLE_GTTEM  "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM   "
					+ "where epar.activo_geedp=true  AND res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"' "
					+ " and res.aprueba_hora_extra_cobmr=true  " 
					+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
}
		return sql;
			
	}





	public Tabla getTab_tabla_hora_extra() {
		return tab_tabla_hora_extra;
	}





	public void setTab_tabla_hora_extra(Tabla tab_tabla_hora_extra) {
		this.tab_tabla_hora_extra = tab_tabla_hora_extra;
	}
	

	public void actualizarEmpleadoHorasExtra(){
		
		empleado=sel_empleado.getSeleccionados();
		sel_empleado.cerrar();
		
		
		int mesActual=Integer.parseInt(mes);
		
		  
		  
		  if (mesActual<10) {
			mes="0"+mesActual;
		}
		   
		  int anio=0;
		  
		  
		  if (mesActual>10) {
			anio=2017;
		}else {
			anio=2018;
		}
			
		  
		  
		  
      
	     String fechaConsultaInicio="";
	     String fechaConsultaFin="";

	     int diaFinMes=retornarDiasMes(1,Integer.parseInt(mes),anio);
	     fechaConsultaInicio=anio+"-"+mes+"-01";
	     fechaConsultaFin=anio+"-"+mes+"-"+diaFinMes;
		
		
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set aprueba_hora_extra_cobmr=true where ide_gtemp in("+empleado+") and fecha_evento_cobmr between '"+fechaConsultaInicio+"' and '"+fechaConsultaFin+"' ");

		utilitario.agregarMensajeInfo("Empleado(s) Asignado(s) Correctamente", "Hora(s) Extra(s) Generadas ");
		guardarPantalla();
		//tab_tabla.setCondicion("ide_gtemp in ("+empleado+") and fecha_evento_cobmr between '"+fechaInicial+"' and '"+fechaFinal+"' ");
		//tab_tabla.ejecutarSql();
		//utilitario.addUpdate("tab_tabla");
		

		
		
		
	}



	public void bloquearBotones(){
		
		bar_botones.getBot_eliminar().setRendered(false);
		//bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
	}
	
	
	
	public void actualizarSumatoriaHorasExtra(SelectEvent evt){
		tab_tabla_hora_extra.seleccionarFila(evt);
		String ide_gtemp=tab_tabla_hora_extra.getValor("IDE_GTEMP");
		String fecha=tab_tabla_hora_extra.getValor("fecha_evento_cobmr");
		int anioIni=utilitario.getAnio(fecha);
		TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anioIni+"%'");
		
		String fecIni="",fecFin="";
      //Resto un mes a la fecha actual
        String fechaActual=getFechaAsyyyyMMdd(sumarRestarMeses(getFechaAsyyyyMMdd(fecha), -1));
        String mesDespliege="";
        //Obtengo mes 
        
        
        int mesTemp=utilitario.getMes(fecha);
        String mes="";
        if (mesTemp<10) {
			mes="0"+mesTemp;
		}else {
			mes=""+mesTemp;
		}
        
       String fechaConsultaInicio="";
       String fechaConsultaFin="";
       String empleado="";
       //Retorna el dia final del mes
       int diaFinMes=retornarDiasMes(1,Integer.parseInt(mes),utilitario.getAnio(fechaActual));
      String  fechaConsultaInicioPorEmpleado=utilitario.getAnio(fechaActual)+"-"+mes+"-01";
       String fechaConsultaFinPorEmpleado=utilitario.getAnio(fechaActual)+"-"+mes+"-"+diaFinMes;
       	//cuatroUno_("","",1,str_ide.toString());
		tab_tabla_sumatoria.setCondicion("mes_asrhe="+mes+" AND ide_geani="+tabAnio.getValor("IDE_GEANI"));
		//+" AND IDE_GTEMP IN("+tab_tabla_hora_extra.getValor("ide_gtemp")+") "
		tab_tabla_sumatoria.ejecutarSql();
		utilitario.addUpdate("tab_tabla_sumatoria");
	
		//Calculo de Horas Extra
		System.out.println("Procesando Horas Extra Por Empleado");	
		//cuatroUnoXEmpleado(ide_gtemp,fechaConsultaInicioPorEmpleado,fechaConsultaFinPorEmpleado);
		System.out.println("Fin Procesamiento de Horas Extra ");	
	
		
	}
	

	

public int retornarDiasMes(int dia, int mes,int anio){

Calendar cal = new GregorianCalendar(anio, mes-1, 1); 
	// Get the number of days in that month 
	int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
	//System.out.println(days);
	return days;
	}


public TablaGenerica  retornaAprobacionHorasExtra(String ide_gtemp,int ide_gemes){
	
	/*TablaGenerica tabAprobacionHorasExtra= utilitario.consultar("SELECT ide_ashme, ide_gemes, ide_gtemp, dia1, dia2, dia3, dia4, dia5,  "
			+ "dia6, dia7, dia8, dia9, dia10, dia11, dia12, dia13, dia14, dia15, "
			+ "dia16, dia17, dia18, dia19, dia20, dia21, dia22, dia23, dia24,  "
			+ "dia25, dia26, dia27, dia28, dia29, dia30, dia31, aplica_hora_extra,  "
			+ "activo_ashme, ide_geani, ide_sucursal, ide_geare, ide_gedep,  "
			+ "num_extra_ashem, num_suple_ashem, usuario_ingre, fecha_ingre,  "
			+ "usuario_actua, fecha_actua, hora_ingre, hora_actua, ide_geedp,  "
			+ "ide_geedp_cambio "
			//+ "FROM asi_horario_mes_empleado where ide_gtemp=169 and ide_gemes="+ide_gemes);
			+ "FROM asi_horario_mes_empleado where ide_gtemp="+ide_gtemp+" and ide_gemes="+ide_gemes);
	*/
	
	
	TablaGenerica tabAprobacionHorasExtra= utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ "archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph, "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes, "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph "
			+ "FROM con_biometrico_plan_hxe  "
			+ "WHERE ide_gtemp in("+ide_gtemp+") and ide_gemes="+ide_gemes+" and ide_geani=15 ");

	
		return tabAprobacionHorasExtra;
	
}



public Date sumarRestarMeses(Date fecha , int meses){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(fecha);
	calendar.add(Calendar.MONTH, meses);
	return calendar.getTime();
	
	
}




public StringBuilder getStr_ide() {
	return str_ide;
}




public void setStr_ide(StringBuilder str_ide) {
	this.str_ide = str_ide;
}



/*
 * Metodo retorna el calculo de hora en formato  hora y minuto 00:00
 */
public String actualizarTabla(String cadena){
	String cadenaNueva="";
	return cadenaNueva = cadena.replace(".", ":");
	
	
}
/*
 * Metodo retorna el calculo de hora en formato double hora y minuto 0.0
 */
public String actualizarTablaDosPuntosAPunto(String cadena){
	String cadenaNueva="";
	return cadenaNueva = cadena.replace(":", ".");
	
	
}



/*
 * Metodo que retorna el tl tipo de contrato del empleado codigo de trabajo=1 y Losep=2
 */
public int getTipoEmpleado(int ide_gtemp){
int templeado=0;
TablaGenerica tipoEmpleado = utilitario.consultar("select ide_geedp,ide_gttem  "
		+ "from gen_empleados_departamento_par  "
		+ "where ide_gtemp="+ide_gtemp+ " "
		+ "order by ide_geedp desc  "
		+ "limit 1");

if (tipoEmpleado.getTotalFilas()>0) {
	templeado=Integer.parseInt(tipoEmpleado.getValor("IDE_GTTEM"));
}else {
	templeado=0;
}
return templeado;
}



/*
 * 	Metodo que recibe la Hora y le concatena 0 a la hora y minutos
 */
public String nuevaHoraExtraResumen(String horaExtra){

	String[] parts = horaExtra.split(":");
	String part1 = parts[0]; // 004
	String part2 = parts[1]; // 034556
	String part1New="",part2New="";
	
	if (part1.length()==1) {
		if (part1.equals("0")) {
			part1New="0"+part1;		
		}else {
			part1New="0"+part1;
		}
	
	}else if (part1.length()==0) {
		part1New="00"+part1;
	}
	
	else {
		part1New=part1;
	}
	
	if (part2.length()==1) {
		
		if (part2.equals("0")) {
			part2New="0"+part2;		
		}else {
			part2New=part2+"0";
		}
				
		
	}else if (part2.length()==0) {
		part2New="00"+part2;
	}
	
	else {
		part2New=part2;
	}
	
	//String nuevaHora=part1New+":"+utilitario.getFormatoNumero(part2New, 2);
	String nuevaHora=part1New+":00:00";
	
	return nuevaHora;
}


public String retornaEmpleadosACargo(String fecha_inicio, String fecha_fin){

	   String fechaActual =fecha_inicio;
       
       String mesDespliege="";
       int mesTemp=utilitario.getMes(fecha_inicio);
       String mes="";
       if (mesTemp<10) {
			mes="0"+mesTemp;
		}else {
			mes=""+mesTemp;
		}
       

      
      
		TablaGenerica tabAnioConsulta=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_inicio)+"%'");
		String anioFin=tabAnioConsulta.getValor("ide_geani");
      
      
      /*tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
      TablaGenerica tab_permisos=utilitario.consultar("select * from sis_opcion_usuario where ide_usua="+utilitario.getVariable("ide_usua"));
    
      String parametroJefe="393";
      String empleadosAsignados="-1";
      
      if (tab_permisos.getTotalFilas()>0) {
		if (tab_permisos.getValor("IDE_USUA").equals(utilitario.getVariable("ide_usua"))) {
		
			jefeAsignacionHorario=tabEmpDep.getValor("ide_geedp");
	     empleadosAsignados="ide_geedp="+jefeAsignacionHorario+" and";
		}else {
			return "ERROR";
		}
	}else {
		if (utilitario.getVariable("ide_usua").equals(parametroJefe)) {
			empleadosAsignados="";
		}else {
			return "ERROR";	
		}
		
		
	}
      
      boolean bandSinArea=false;
      if (tabEmpDep.getTotalFilas()>0) {
		bandSinArea=true;
	}else {
		bandSinArea=false;
      
	}*/
   	
   
      
     	
     	
    	tabEmpleado= utilitario.consultar("SELECT ide_gtemp, "
     			+ "ide_gemes "
     			+ "FROM con_biometrico_plan_hxe where ide_geani="+anioFin+ " and ide_gemes="+mes+"");
     
     	
     	
   	int valor=0;
		String horario="";
		
          
     	   	
     	if (tabEmpleado.getTotalFilas()>0) {
     		for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) 
			 {
      	  
            str_ide.append(tabEmpleado.getValor(i, "IDE_GTEMP"));
           // valor++;
            if (tabEmpleado.getTotalFilas()==1) {
			}else if (valor<=tabEmpleado.getTotalFilas()) {
					valor++;
					if(valor<(tabEmpleado.getTotalFilas())){
                str_ide.append(",");
                System.out.println("str_ide:  "+str_ide);
					}
			}

			 }
     		
     	}

	
	
	
	
	
	
	return str_ide.toString();
}

/*public String getSql(){
	return  "select gper.ide_gemes,mes.detalle_gemes,anio.detalle_geani "
			+ "from gen_periodo gper "
			+ "left join gen_anio anio on gper.ide_geani=anio.ide_geani "
			+ "left join gen_mes mes on mes.ide_gemes=gper.ide_gemes "
			+ "where gper.ide_geani=11 and gper.ide_gemes="+utilitario.getVariable(arg0);
}*/



public void insertarTablaResumenHorasExtra(
		 int ide_gtemp,
		 String observacion_asrhe,
		 String horafinextra_asrhe,
		 String recargonocturno25_asrhe,
		 String recargonocturno100_asrhe,
		 boolean activo_asrhe,
		 int mes_asrhe,
		 int ide_geani,
		 boolean aprobacion_asrhe,
		 String horas25_loep_asrhe,
		 String horas60_loep_asrhe,
		 String horas50_ct_asrhe	 
		 ){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_resumen_horas_extra", "ide_asrhe"));
		String codigo=tab_codigo.getValor("codigo");

		
		utilitario.getConexion().ejecutarSql("INSERT INTO asi_resumen_horas_extra(" 
					+ "ide_asrhe, "
					+ "ide_gtemp, "
			  		+ "observacion_asrhe, "
			  		+ "horafinextra_asrhe, "
			  		+ "recargonocturno25_asrhe, "
			  		+ "recargonocturno100_asrhe, "
			  		+ "activo_asrhe, "
			  		+ "mes_asrhe, "
			  		+ "ide_geani, "
			  		+ "aprobacion_asrhe, "
			  		+ "horas25_loep_asrhe, "
			  		+ "horas60_loep_asrhe, "
			  		+ "horas50_ct_asrhe)" + 

			  		" values( " +codigo + ", "
			  		+ ""+ ide_gtemp+", "
			  		+ "'"+observacion_asrhe+"', "
			  		+ ""+horafinextra_asrhe+", "
			  		+ ""+recargonocturno25_asrhe+", "
			  		+ ""+recargonocturno100_asrhe+", "
			  		+ ""+activo_asrhe+", "
			  		+ ""+mes_asrhe+", "
			  		+ ""+ide_geani+", "
			  		+ ""+aprobacion_asrhe+", " 
			  		+ ""+horas25_loep_asrhe+", "
			  		+ ""+horas60_loep_asrhe+", "		  		
			  		+ ""+horas50_ct_asrhe+")"); 
		
				
	 
}


 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}


	@Override
	public void aceptarReporte() {
		String ide_gtemp="";
		if (rep_reporte.getReporteSelecionado().equals("RESUMEN HORAS EXTRA POR EMPLEADO")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_fecha_marcaciones.dibujar();
				
			}else if (dia_fecha_marcaciones.isVisible()) {
				fechaReporteIni=cal_fecha_inicial_marcaciones.getFecha();
				fechaReporteFin=cal_fecha_final_marcaciones.getFecha();
				dia_fecha_marcaciones.cerrar();
				
		    	set_empleado.setSeleccionTabla(empleadosConsulta(jefe_inmediato_planificacion),"IDE_GTEMP");
		    	set_empleado.setRadio();
		    	set_empleado.redibujar();
			}else if (set_empleado.isVisible()) {
				
		      	   
		        
		        empleado=set_empleado.getValorSeleccionado();
				//obtengo el numero de "," dentro de los empleados seleccionados
				char[] arrayChar = empleado.toCharArray();
				int contComas=0;
				String empAux="";
				for(int i=0; i<arrayChar.length; i++){
				if( arrayChar[i] == ','){
						contComas++;	
						p_parametros.put("ide_gtemp",empAux);
						empAux="";
				}else {
					empAux=empAux+empleado.charAt(i);
					
				}
				
				
				}
				
				p_parametros.put("ide_gtemp",empleado);
		        
				//p_parametros.put("ide_gtemp",ide_gtemp);
				String strNombreEmpleado="",strNombreCoordinador="",strEmpleado="",area="";
				//if (tabEmpDep.getTotalFilas()>0) {
				//	strEmpleado=tabEmpDep.getValor("IDE_GTEMP");
				//}else {
				//	strEmpleado="";
				//}
				//strNombreEmpleado = retornaDatosEmpleado(strEmpleado);
				//strNombreCoordinador=retornaCoordinador(strEmpleado);
				//area=retornaArea(ide_geare);
				if (tipo_perfil.equals("1")) {
	
					
					p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

				}else {
					p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

				}
			
	
				p_parametros.put("fec_ini",fechaReporteIni);
				p_parametros.put("fec_fin",fechaReporteFin);
				//p_parametros.put("jefeInmediato",strNombreEmpleado);
				//p_parametros.put("tthh",strNombreCoordinador);
				p_parametros.put("area",this.area);
			   	TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(fechaReporteIni)+" ");
			   	p_parametros.put("ide_gemes",Integer.parseInt(tab_mes.getValor("ide_gemes")));
			   	
			   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='"+utilitario.getAnio(fechaReporteIni)+"' ");
			   	p_parametros.put("ide_geani",Integer.parseInt(tab_anio.getValor("ide_geani")));
			   	
			   	
			   	p_parametros.put("elaborado_por",NombreAprobador);
			   	p_parametros.put("cargo_elaborado_por",cargo_elaborado);

			  	p_parametros.put("aprobado_por",NombreEmpleadoJefe);
			   	p_parametros.put("cargo_aprobador",cargo_jefe_inmediato);
			   	
			   	
			   	
				p_parametros.put("mes",tab_mes.getValor("detalle_gemes"));
				p_parametros.put("titulo","DETALLE HORAS EXTRA POR EMPLEADO");
				set_empleado.cerrar();
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				//set_empleado.cerrar();
				sef_reporte.dibujar();
				
			}
		}else if (rep_reporte.getReporteSelecionado().equals("RESUMEN DE HORAS EXTRA GLOBAL")){
	
			
			
			if (tipo_perfil.equals("1")) {
				
				
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();
					rep_reporte.cerrar();
					
					
					
					
					dia_fecha_marcaciones.dibujar();
					
				}else if (dia_fecha_marcaciones.isVisible()) {
					fechaReporteIni=cal_fecha_inicial_marcaciones.getFecha();
					fechaReporteFin=cal_fecha_final_marcaciones.getFecha();
					dia_fecha_marcaciones.cerrar();
					set_empleado_asignados.setSeleccionTabla(jefeConsulta(str_ide_jefe_inmediato.toString()),"IDE_GTEMP");
					set_empleado_asignados.redibujar();
				 }else if (set_empleado_asignados.isVisible()) {
					 set_empleado_asignados.cerrar();
					
					 
					 TablaGenerica  tabJefeInmediatoTemp=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,  "
			         			+ "ide_gtemp_padre_asjei,cargo_padre_asjei "
			         			+ "FROM asi_jefe_inmediato  asjei "
			         			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
			         			+ "where ide_gtemp in("+set_empleado_asignados.getSeleccionados()+")");
					 
						StringBuilder str_ide_jefes=new StringBuilder();

				       String int_num_col_idegetempmensualjefes="";
		                 for (int i = 0; i < tabJefeInmediatoTemp.getTotalFilas(); i++) {
		                	 int_num_col_idegetempmensualjefes=tabJefeInmediatoTemp.getValor(i, "ide_asjei");
		                	 if(str_ide_jefes.toString().isEmpty()==false){
		                		 str_ide_jefes.append(",");
		                	 }
		                	 str_ide_jefes.append(int_num_col_idegetempmensualjefes);
		                 }
					 
					 
		
			    	set_empleado_total.setSeleccionTabla(empleadosConsulta(str_ide_jefes.toString()),"IDE_GTEMP");
			    	set_empleado_total.redibujar();
				 }else if (set_empleado_total.isVisible()) {


			        
			        empleado=set_empleado_total.getSeleccionados();
					//obtengo el numero de "," dentro de los empleados seleccionados
					char[] arrayChar = empleado.toCharArray();
					int contComas=0;
					String empAux="";
					for(int i=0; i<arrayChar.length; i++){
					if( arrayChar[i] == ','){
							contComas++;	
							p_parametros.put("ide_gtemp",empAux);
							empAux="";
					}else {
						empAux=empAux+empleado.charAt(i);
						
					}
					
					
					}
					
					p_parametros.put("ide_gtemp",empleado);
			        
					//p_parametros.put("ide_gtemp",ide_gtemp);
					String strNombreEmpleado="",strNombreCoordinador="",strEmpleado="",area="";
					//if (tabEmpDep.getTotalFilas()>0) {
					//	strEmpleado=tabEmpDep.getValor("IDE_GTEMP");
					//}else {
					//	strEmpleado="";
					//}
					//strNombreEmpleado = retornaDatosEmpleado(strEmpleado);
					//strNombreCoordinador=retornaCoordinador(strEmpleado);
					//area=retornaArea(ide_geare);
					if (tipo_perfil.equals("1")) {
		
						
						p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

					}else {
						p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

					}
				
		
					p_parametros.put("fec_ini",fechaReporteIni);
					p_parametros.put("fec_fin",fechaReporteFin);
					//p_parametros.put("jefeInmediato",strNombreEmpleado);
					//p_parametros.put("tthh",strNombreCoordinador);
					p_parametros.put("area",this.area);
				   	TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(fechaReporteIni)+" ");
				   	p_parametros.put("ide_gemes",Integer.parseInt(tab_mes.getValor("ide_gemes")));
				   	
				   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='"+utilitario.getAnio(fechaReporteIni)+"' ");
				   	p_parametros.put("ide_geani",Integer.parseInt(tab_anio.getValor("ide_geani")));
				   	
				   	
				   	p_parametros.put("elaborado_por",NombreAprobador);
				   	p_parametros.put("cargo_elaborado_por",cargo_elaborado);

				  	p_parametros.put("aprobado_por",NombreEmpleadoJefe);
				   	p_parametros.put("cargo_aprobador",cargo_jefe_inmediato);
				   	
				   	
				   	
					p_parametros.put("mes",tab_mes.getValor("detalle_gemes"));
					p_parametros.put("titulo","DETALLE HORAS EXTRA POR EMPLEADO");
					
					
				TablaGenerica tabSumatoria=utilitario.consultar("select sum(horafinextra_asrhe) as horafinextra_asrhe,sum(recargonocturno25_asrhe) as recargonocturno25_asrhe, "
						+ "sum(recargonocturno100_asrhe) as recargonocturno100_asrhe,sum(horas25_loep_asrhe) as horas25_loep_asrhe,  "
						+ "sum(horas60_loep_asrhe) as horas60_loep_asrhe ,sum(horas50_ct_asrhe) as horas50_ct_asrhe,sum(alimentacion_asrhe) as alimentacion_asrhe  "
						+ "FROM asi_resumen_horas_extra "
						+ "where ide_geani="+tab_anio.getValor("ide_geani")+" and mes_asrhe="+tab_mes.getValor("ide_gemes")+" and ide_gtemp in("+empleado+")");
				String total_25RecargoNocturno="",total_25Loep="",total_50Codigo="",total_60Loep="",total_100="",total_Alimentacion="";
					
				total_25RecargoNocturno=tabSumatoria.getValor("recargonocturno25_asrhe");
				total_25Loep=tabSumatoria.getValor("horas25_loep_asrhe");
				total_50Codigo=tabSumatoria.getValor("horas50_ct_asrhe");
				total_60Loep=tabSumatoria.getValor("horas60_loep_asrhe");
				total_100=tabSumatoria.getValor("recargonocturno100_asrhe");
				total_Alimentacion=tabSumatoria.getValor("alimentacion_asrhe");
				
				p_parametros.put("loep25",utilitario.getFormatoNumero(Double.parseDouble(total_25Loep), 2));
				p_parametros.put("loep60",utilitario.getFormatoNumero(Double.parseDouble(total_60Loep), 2));
				p_parametros.put("ct25",utilitario.getFormatoNumero(Double.parseDouble(total_25RecargoNocturno), 2));
				p_parametros.put("ct50",utilitario.getFormatoNumero(Double.parseDouble(total_50Codigo), 2));
				p_parametros.put("ctloep100",utilitario.getFormatoNumero(Double.parseDouble(total_100), 2));
				p_parametros.put("alimentacion",total_Alimentacion);

				
				
				
					set_empleado_total.cerrar();
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					//set_empleado.cerrar();
					sef_reporte.dibujar();
					
	                }

		        
				
				
			}else {
				
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();
					rep_reporte.cerrar();
					
					
					
					
					dia_fecha_marcaciones.dibujar();
					
				}else if (dia_fecha_marcaciones.isVisible()) {
					fechaReporteIni=cal_fecha_inicial_marcaciones.getFecha();
					fechaReporteFin=cal_fecha_final_marcaciones.getFecha();
					dia_fecha_marcaciones.cerrar();
					
			    	set_empleado_total.setSeleccionTabla(empleadosConsulta(jefe_inmediato_planificacion),"IDE_GTEMP");
			    	set_empleado_total.redibujar();
				 }else if (set_empleado_total.isVisible()) {


			        
			        empleado=set_empleado_total.getSeleccionados();
				//obtengo el numero de "," dentro de los empleados seleccionados
				char[] arrayChar = empleado.toCharArray();
				int contComas=0;
				String empAux="";
				for(int i=0; i<arrayChar.length; i++){
				if( arrayChar[i] == ','){
						contComas++;	
						p_parametros.put("ide_gtemp",empAux);
						empAux="";
				}else {
					empAux=empAux+empleado.charAt(i);
					
				}
				
				
				}
				
				p_parametros.put("ide_gtemp",empleado);
		        
				//p_parametros.put("ide_gtemp",ide_gtemp);
				String strNombreEmpleado="",strNombreCoordinador="",strEmpleado="",area="";
				//if (tabEmpDep.getTotalFilas()>0) {
				//	strEmpleado=tabEmpDep.getValor("IDE_GTEMP");
				//}else {
				//	strEmpleado="";
				//}
				//strNombreEmpleado = retornaDatosEmpleado(strEmpleado);
				//strNombreCoordinador=retornaCoordinador(strEmpleado);
				//area=retornaArea(ide_geare);
				if (tipo_perfil.equals("1")) {
		
						
						p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

				}else {
					p_parametros.put("ide_asjei",jefe_inmediato_planificacion);

				}
			
	
				p_parametros.put("fec_ini",fechaReporteIni);
				p_parametros.put("fec_fin",fechaReporteFin);
				//p_parametros.put("jefeInmediato",strNombreEmpleado);
				//p_parametros.put("tthh",strNombreCoordinador);
				p_parametros.put("area",this.area);
			   	TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(fechaReporteIni)+" ");
			   	p_parametros.put("ide_gemes",Integer.parseInt(tab_mes.getValor("ide_gemes")));
			   	
			   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='"+utilitario.getAnio(fechaReporteIni)+"' ");
			   	p_parametros.put("ide_geani",Integer.parseInt(tab_anio.getValor("ide_geani")));
			   	
			   	
			   	p_parametros.put("elaborado_por",NombreAprobador);
			   	p_parametros.put("cargo_elaborado_por",cargo_elaborado);

			  	p_parametros.put("aprobado_por",NombreEmpleadoJefe);
			   	p_parametros.put("cargo_aprobador",cargo_jefe_inmediato);
			   	
			   	
			   	
				p_parametros.put("mes",tab_mes.getValor("detalle_gemes"));
				p_parametros.put("titulo","DETALLE HORAS EXTRA POR EMPLEADO");
					
					
				TablaGenerica tabSumatoria=utilitario.consultar("select sum(horafinextra_asrhe) as horafinextra_asrhe,sum(recargonocturno25_asrhe) as recargonocturno25_asrhe, "
						+ "sum(recargonocturno100_asrhe) as recargonocturno100_asrhe,sum(horas25_loep_asrhe) as horas25_loep_asrhe,  "
						+ "sum(horas60_loep_asrhe) as horas60_loep_asrhe ,sum(horas50_ct_asrhe) as horas50_ct_asrhe,sum(alimentacion_asrhe) as alimentacion_asrhe  "
						+ "FROM asi_resumen_horas_extra "
						+ "where ide_geani="+tab_anio.getValor("ide_geani")+" and mes_asrhe="+tab_mes.getValor("ide_gemes")+" and ide_gtemp in("+empleado+")");
				String total_25RecargoNocturno="",total_25Loep="",total_50Codigo="",total_60Loep="",total_100="",total_Alimentacion="";
					
				total_25RecargoNocturno=tabSumatoria.getValor("recargonocturno25_asrhe");
				total_25Loep=tabSumatoria.getValor("horas25_loep_asrhe");
				total_50Codigo=tabSumatoria.getValor("horas50_ct_asrhe");
				total_60Loep=tabSumatoria.getValor("horas60_loep_asrhe");
				total_100=tabSumatoria.getValor("recargonocturno100_asrhe");
				total_Alimentacion=tabSumatoria.getValor("alimentacion_asrhe");
				
				p_parametros.put("loep25",utilitario.getFormatoNumero(Double.parseDouble(total_25Loep), 2));
				p_parametros.put("loep60",utilitario.getFormatoNumero(Double.parseDouble(total_60Loep), 2));
				p_parametros.put("ct25",utilitario.getFormatoNumero(Double.parseDouble(total_25RecargoNocturno), 2));
				p_parametros.put("ct50",utilitario.getFormatoNumero(Double.parseDouble(total_50Codigo), 2));
				p_parametros.put("ctloep100",utilitario.getFormatoNumero(Double.parseDouble(total_100), 2));
				p_parametros.put("alimentacion",total_Alimentacion);

				
				
				
					set_empleado_total.cerrar();
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				//set_empleado.cerrar();
				sef_reporte.dibujar();
				
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




	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}




	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}


public String retornaDatosEmpleado(String IDE_GTEMP){

		
		String detallePermiso="";
		String nombreTipoSolicitud;
		String nroHoras;
		String motivo;
		String obtieneNroDias;
	
		//Estructura de mensaje
		String strNombreEmpleado="",strNombreCoordinador="";
		
		//obtengo el empleado del cual requiero los datos
		TablaGenerica tab_empleado = utilitario.consultar("select  ide_gtemp, ide_gtgen, ide_gttdi, ide_gtesc, ide_gedip, ide_gttis, "
				+ "ide_gtnac, documento_identidad_gtemp, fecha_ingreso_pais_gtemp, "
				+ "carnet_extranjeria_gtemp, primer_nombre_gtemp, segundo_nombre_gtemp, "
				+ "apellido_paterno_gtemp, apellido_materno_gtemp "
				+ "from gth_empleado  "
				+ "where ide_gtemp in("+IDE_GTEMP+")");
		String documento = tab_empleado.getValor("documento_identidad_gtemp");
		String primer_nombre_empleado= tab_empleado.getValor("primer_nombre_gtemp").toString(); 
		String segundo_nombre_empleado= tab_empleado.getValor("segundo_nombre_gtemp").toString();
		String apellido_paterno_empleado= tab_empleado.getValor("apellido_paterno_gtemp").toString(); 
		String apellido_materno_empleado= tab_empleado.getValor("apellido_materno_gtemp").toString();
		strNombreEmpleado=primer_nombre_empleado+" "+segundo_nombre_empleado+" "+apellido_paterno_empleado+"  "+apellido_materno_empleado;
		return strNombreEmpleado;


	
	}
	
public String retornaCoordinador(String IDE_GTEMP){


	String detallePermiso="";
	String nombreTipoSolicitud;
	String nroHoras;
	String motivo;
	String obtieneNroDias;

	//Estructura de mensaje
	String strNombreEmpleado="",strNombreCoordinador="";
	
	TablaGenerica tab_empleado_departamento = utilitario.consultar("select ide_gtemp,ide_geedp,ide_geare "
			+ "from gen_empleados_departamento_par  "
			+ "where ide_gtemp in("+IDE_GTEMP+")  "
			+ "order by ide_geedp desc limit 1");
	
	
	TablaGenerica tab_coordinador = utilitario.consultar("select ide_gtemp,ide_geedp,ide_gegro,ide_geare "
			+ "from gen_empleados_departamento_par  "
			+ "where ide_gegro in(4) and ide_geare="+tab_empleado_departamento.getValor("IDE_GEARE")+" "  
			+ "order by ide_geedp desc limit 1");
	
	
	
	
	if (tab_empleado_departamento.getTotalFilas()>0) {
	
	TablaGenerica tab_empleado=null;
			//obtengo el empleado del cual requiero los datos
	tab_empleado = utilitario.consultar("select  ide_gtemp, ide_gtgen, ide_gttdi, ide_gtesc, ide_gedip, ide_gttis, "
				+ "ide_gtnac, documento_identidad_gtemp, fecha_ingreso_pais_gtemp, "
				+ "carnet_extranjeria_gtemp, primer_nombre_gtemp, segundo_nombre_gtemp, "
				+ "apellido_paterno_gtemp, apellido_materno_gtemp "
				+ "from gth_empleado  "
				+ "where ide_gtemp in("+tab_coordinador.getValor("IDE_GTEMP")+")");
		String documento = tab_empleado.getValor("documento_identidad_gtemp");
		String primer_nombre_empleado= tab_empleado.getValor("primer_nombre_gtemp").toString(); 
		String segundo_nombre_empleado= tab_empleado.getValor("segundo_nombre_gtemp").toString();
		String apellido_paterno_empleado= tab_empleado.getValor("apellido_paterno_gtemp").toString(); 
		String apellido_materno_empleado= tab_empleado.getValor("apellido_materno_gtemp").toString();
		strNombreEmpleado=primer_nombre_empleado+" "+segundo_nombre_empleado+" "+apellido_paterno_empleado+"  "+apellido_materno_empleado;
		ide_geare=tab_coordinador.getValor("IDE_GEARE");
		return strNombreEmpleado;
		
		
	}else {
		return strNombreEmpleado;
	}


}

public String retornaArea(String IDE_GEARE){

	
	//Estructura de mensaje
	String strNombreArea="";
	
	//obtengo el empleado del cual requiero los datos
	TablaGenerica tab_area = utilitario.consultar("select  ide_geare, detalle_geare  "
			+ "from gen_area  "
			+ "where ide_geare in("+IDE_GEARE+")");
	strNombreArea=tab_area.getValor("DETALLE_GEARE");
	return strNombreArea;



}


public String getIde_geare() {
	return ide_geare;
}




public void setIde_geare(String ide_geare) {
	this.ide_geare = ide_geare;
}




public Calendario getCal_fecha_inicial_marcaciones() {
	return cal_fecha_inicial_marcaciones;
}




public void setCal_fecha_inicial_marcaciones(
		Calendario cal_fecha_inicial_marcaciones) {
	this.cal_fecha_inicial_marcaciones = cal_fecha_inicial_marcaciones;
}




public Calendario getCal_fecha_final_marcaciones() {
	return cal_fecha_final_marcaciones;
}




public void setCal_fecha_final_marcaciones(
		Calendario cal_fecha_final_marcaciones) {
	this.cal_fecha_final_marcaciones = cal_fecha_final_marcaciones;
}






public Calendario getCal_fecha_inicial() {
	return cal_fecha_inicial;
}




public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
	this.cal_fecha_inicial = cal_fecha_inicial;
}




public Calendario getCal_fecha_final() {
	return cal_fecha_final;
}




public void setCal_fecha_final(Calendario cal_fecha_final) {
	this.cal_fecha_final = cal_fecha_final;
}




public Calendario getCal_fecha_inicial_marcaciones_reporte() {
	return cal_fecha_inicial_marcaciones_reporte;
}




public void setCal_fecha_inicial_marcaciones_reporte(
		Calendario cal_fecha_inicial_marcaciones_reporte) {
	this.cal_fecha_inicial_marcaciones_reporte = cal_fecha_inicial_marcaciones_reporte;
}




public Calendario getCal_fecha_final_marcaciones_reporte() {
	return cal_fecha_final_marcaciones_reporte;
}




public void setCal_fecha_final_marcaciones_reporte(
		Calendario cal_fecha_final_marcaciones_reporte) {
	this.cal_fecha_final_marcaciones_reporte = cal_fecha_final_marcaciones_reporte;
}




public String getFechaReporteIni() {
	return fechaReporteIni;
}




public void setFechaReporteIni(String fechaReporteIni) {
	this.fechaReporteIni = fechaReporteIni;
}




public String getFechaReporteFin() {
	return fechaReporteFin;
}




public void setFechaReporteFin(String fechaReporteFin) {
	this.fechaReporteFin = fechaReporteFin;
}




public String empleadosConsulta(String empleado){
	
	String sql_empleadosActivos="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "from  asi_empleado_jefe_inmediato asemp   "
				+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp    "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "where "
				//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
						+ "asemp.ide_asjei in("+empleado+") "
				//+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
				+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
				+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
	
	
/*	String sql_empleadosActivos="SELECT  "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP,  "
			+ "GTT.DETALLE_GTTEM "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM     "
			+ "where cbmr.fecha_evento_cobmr between '"+getFechaReporteIni()+"' AND '"+getFechaReporteFin()+"'  "
					+ "and cbmr.ide_gtemp in("+getStr_ide().toString()+") "
			+ "and  EPAR.ACTIVO_GEEDP=TRUE  "
			+ "group by "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
			+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM "
			+ "ORDER BY cbmr.ide_gtemp ASC";
	*/
	return sql_empleadosActivos;
	
}

// Devuelve true si  tiene asignado el dia festivo
private void getFeriadoXFecha(String fechaInicio, String fechaFin){
	String fechaInicioAsnov="";
	String fechaFinAsnov="";
	Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaFin);
	int diferenciaDias=0; 
	System.out.println("update con_biometrico_marcaciones_resumen set  "
				+ "novedad_cobmr =true  where where horainiciobiometrico_cobmr!='' and horafinbiometrico_cobmr!=''  "
				+ "and fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFin+"' ");
	
		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set  "
				+ "novedad_cobmr =true  where horainiciobiometrico_cobmr!='' and horafinbiometrico_cobmr!=''  "
				+ "and fecha_evento_cobmr between '"+fechaInicio+"' and '"+fechaFin+"' ");
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

public void actualizaTotalHXE(String fechaIni,String fechaFin,int ide_gemes,String ide_geani){
TablaGenerica planificacion_hxe=null;	
	
	TablaGenerica tab_actualiza_horas_hxe=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
			+ " archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
			+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
			+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
			+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph, "
			+ "registro_manual_cobph, productos_eperado_cobph  "
			+ "FROM con_biometrico_plan_hxe  "
			+ "where ide_geani="+ide_geani+"  and ide_gemes="+ide_gemes
			//+ " AND IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) "
			
			//+ "and ide_gtemp in(SELECT ide_gtemp  "
			//+ "FROM con_biometrico_marcaciones_resumen  "
			//+ "where  fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"'   "
			+ "order by ide_gtemp asc ");
	
StringBuilder str_ide_hxe=new StringBuilder();
	int valorEmp=0;

		for (int i = 0; i < tab_actualiza_horas_hxe.getTotalFilas(); i++) 
		{
		//Voy anidando los ides de la accion
			str_ide_hxe.append(tab_actualiza_horas_hxe.getValor(i, "IDE_GTEMP"));
   // valor++;
    if (tab_actualiza_horas_hxe.getTotalFilas()==1) {
	}else if (valorEmp<=tab_actualiza_horas_hxe.getTotalFilas()) {
		valorEmp++;
			if(valorEmp<(tab_actualiza_horas_hxe.getTotalFilas())){
				str_ide_hxe.append(",");
        //System.out.println("str_ide:  "+str_ide);
			}
	}

	 }
	
		double horas25_loep_cobph=0.00,horas60_loep_cobph=0.00,horas25_ct_cobph=0.00,horas50_ct_cobph=0.00,horas100_loep_ct_cobph=0.00;
		
		
		
		//198,138,202,204
	TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT ide_cobph, ide_gtemp, ide_gepgc, ide_geare, actividades_cobph, "
					+ " archivo_cobph, nombre_archivo_cobph, horas25_loep_cobph, horas60_loep_cobph,  "
					+ "horas25_ct_cobph, horas50_ct_cobph, horas100_loep_ct_cobph, ide_gemes,  "
					+ "ide_geani, fecha_registro_cobph, ide_usua, activo_cobph, "
					+ "ide_asjei, aprobado_cobph, aprobado_tthh_cobph, retorno_plan_cobph, "
					+ "registro_manual_cobph, productos_eperado_cobph  "
					+ "FROM con_biometrico_plan_hxe  "
					+ "where ide_geani="+ide_geani+"  and ide_gemes="+ide_gemes+" "
					+ "and ide_gtemp in("+str_ide_hxe.toString()+" )"
					+" order by ide_gtemp asc ");
			
			
			

	double recargonocturno25_asrhe=0.00,recargonocturno100_asrhe=0.00,horas25_loep_asrhe=0.00,horas60_loep_asrhe=0.00,horas50_ct_asrhe=0.00;
	
	
	for (int i = 0; i < tabConBiometricoMarcacionesResumen.getTotalFilas(); i++) {		
		if (tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobph")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobph").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobph").isEmpty()) {
			horas25_loep_cobph=0.00;
		}else {
			horas25_loep_cobph=Double.parseDouble(tabConBiometricoMarcacionesResumen.getValor(i, "horas25_loep_cobph"));
			}
			
		if (tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobph")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobph").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobph").isEmpty()) {
			horas60_loep_cobph=0.00;
		}else {
			horas60_loep_cobph=Double.parseDouble(tabConBiometricoMarcacionesResumen.getValor(i, "horas60_loep_cobph"));
		}

		if (tabConBiometricoMarcacionesResumen.getValor(i, "horas25_ct_cobph")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_ct_cobph").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas25_ct_cobph").isEmpty()) {
			horas25_ct_cobph=0.00;
		}else {
			horas25_ct_cobph=Double.parseDouble(tabConBiometricoMarcacionesResumen.getValor(i, "horas25_ct_cobph"));
		}
		
		if (tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobph")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobph").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobph").isEmpty()) {
			horas50_ct_cobph=0.00;
		}else {
			horas50_ct_cobph=Double.parseDouble(tabConBiometricoMarcacionesResumen.getValor(i, "horas50_ct_cobph"));
		}
		
		if (tabConBiometricoMarcacionesResumen.getValor(i, "horas100_loep_ct_cobph")==null || tabConBiometricoMarcacionesResumen.getValor(i, "horas100_loep_ct_cobph").equals("") || tabConBiometricoMarcacionesResumen.getValor(i, "horas100_loep_ct_cobph").isEmpty()) {
			horas100_loep_ct_cobph=0.00;
		}else {
			horas100_loep_ct_cobph=Double.parseDouble(tabConBiometricoMarcacionesResumen.getValor(i, "horas100_loep_ct_cobph"));
		}					
		
		planificacion_hxe=utilitario.consultar("SELECT ide_asrhe, ide_gtemp, fecha_inicio_asrhe, fecha_fin_asrhe, observacion_asrhe, "
				+ "horafinextra_asrhe, recargonocturno25_asrhe, recargonocturno100_asrhe,  "
				+ "activo_asrhe, mes_asrhe, ide_geani, aprobacion_asrhe, horas25_loep_asrhe,  "
				+ "horas60_loep_asrhe, horas50_ct_asrhe   "
				+ "FROM asi_resumen_horas_extra "
				+ "where ide_geani="+ide_geani+" and mes_asrhe="+ide_gemes+" "
				+ "and ide_gtemp IN ("+tabConBiometricoMarcacionesResumen.getValor(i,"ide_gtemp")+") "
				+ "order by ide_gtemp asc ");

		
for (int j = 0; j < planificacion_hxe.getTotalFilas(); j++) {
	
		if (planificacion_hxe.getValor(j, "horas25_loep_asrhe")==null || planificacion_hxe.getValor(j, "horas25_loep_asrhe").equals("") || planificacion_hxe.getValor(j, "horas25_loep_asrhe").isEmpty()) {
			horas25_loep_asrhe=0.00;
		}else {
			horas25_loep_asrhe=Double.parseDouble(planificacion_hxe.getValor(j, "horas25_loep_asrhe"));
		if (horas25_loep_asrhe<horas25_loep_cobph) {
			//No realizo nada
			
		}else if (horas25_loep_asrhe>horas25_loep_cobph) {
			//update 
			utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
			+ " horas25_loep_asrhe="+horas25_loep_cobph+"  where ide_asrhe="+planificacion_hxe.getValor(j,"ide_asrhe")+" ");
		}else {
			//NADA
			}
		}
			
		if (planificacion_hxe.getValor(j, "horas60_loep_asrhe")==null || planificacion_hxe.getValor(j, "horas60_loep_asrhe").equals("") || planificacion_hxe.getValor(j, "horas60_loep_asrhe").isEmpty()) {
			horas60_loep_asrhe=0.00;
		}else {
			horas60_loep_asrhe=Double.parseDouble(planificacion_hxe.getValor(j, "horas60_loep_asrhe"));

			if (horas60_loep_asrhe<horas60_loep_cobph) {
				//No realizo nada
				
			}else if (horas60_loep_asrhe>horas60_loep_cobph) {
				//update 
				utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
				+ " horas60_loep_asrhe="+horas60_loep_cobph+"  where ide_asrhe="+planificacion_hxe.getValor(j,"ide_asrhe")+" ");
			}else {
				//NADA
				}

		
		
		
		}

		if (planificacion_hxe.getValor(j, "recargonocturno25_asrhe")==null || planificacion_hxe.getValor(j, "recargonocturno25_asrhe").equals("") || planificacion_hxe.getValor(j, "recargonocturno25_asrhe").isEmpty()) {
			recargonocturno25_asrhe=0.00;
		}else {
			recargonocturno25_asrhe=Double.parseDouble(planificacion_hxe.getValor(j, "recargonocturno25_asrhe"));
		
			if (recargonocturno25_asrhe<horas25_ct_cobph) {
				//No realizo nada
				
			}else if (recargonocturno25_asrhe>horas25_ct_cobph) {
				//update 
				utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
				+ " recargonocturno25_asrhe="+horas25_ct_cobph+"  where ide_asrhe="+planificacion_hxe.getValor(j,"ide_asrhe")+" ");
			}else {
				//NADA
				}
		
		
		
		}
		
		if (planificacion_hxe.getValor(j, "horas50_ct_asrhe")==null || planificacion_hxe.getValor(j, "horas50_ct_asrhe").equals("") || planificacion_hxe.getValor(j, "horas50_ct_asrhe").isEmpty()) {
			horas50_ct_asrhe=0.00;
		}else {
			horas50_ct_asrhe=Double.parseDouble(planificacion_hxe.getValor(j, "horas50_ct_asrhe"));
		
			if (horas50_ct_asrhe<horas50_ct_cobph) {
				//No realizo nada
				
			}else if (horas50_ct_asrhe>horas50_ct_cobph) {
				//update 
				utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
				+ " horas50_ct_asrhe="+horas50_ct_cobph+"  where ide_asrhe="+planificacion_hxe.getValor(j,"ide_asrhe")+" ");
			}else {
				//NADA
				}
		
		
		}
		
		if (planificacion_hxe.getValor(j, "recargonocturno100_asrhe")==null || planificacion_hxe.getValor(j, "recargonocturno100_asrhe").equals("") || planificacion_hxe.getValor(j, "recargonocturno100_asrhe").isEmpty()) {
			recargonocturno100_asrhe=0.00;
		}else {
			recargonocturno100_asrhe=Double.parseDouble(planificacion_hxe.getValor(j, "recargonocturno100_asrhe"));
		
			if (recargonocturno100_asrhe<horas100_loep_ct_cobph) {
				//No realizo nada
				
			}else if (recargonocturno100_asrhe>horas100_loep_ct_cobph) {
				//update 
				utilitario.getConexion().ejecutarSql("update asi_resumen_horas_extra set "
				+ " recargonocturno100_asrhe="+horas100_loep_ct_cobph+"  where ide_asrhe="+planificacion_hxe.getValor(j,"ide_asrhe")+" ");
			}else {
				//NADA
				}
		
		
		
		}					


}		
		
		
		
	}
	
	
}



public void getAlimentacion(String fecIni, String fecFin, boolean tipo_consulta){

	
//public void getAlimentacion	getAlimentacion()
	
	
	
/*	tab_tabla_hora_extra.setSql("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "cobmr.fecha_evento_cobmr,  "
			+ "cobmr.dia_cobmr,  "
			+ "res.actividades_cobph,  "
			+ "res.productos_eperado_cobph,  "
			+ "cobmr.horainiciohorario_cobmr,  "
			+ "cobmr.horafinhorario_cobmr,   "
			+ "cobmr.horainiciobiometrico_cobmr,  "
			+ "cobmr.horafinbiometrico_cobmr,  "
			+ "cobmr.horas25_loep_cobmr ,   "
			+ "cobmr.horas60_loep_cobmr ,   "
			+ "cobmr.recargonocturno25_cobmr ,  	"
			+ "cobmr.horas50_ct_cobmr ,  "
			+ "cobmr.recargonocturno100_cobmr,  "
			+ "cobmr.dias_laborados_cobmr  "
			+ "FROM con_biometrico_marcaciones_resumen cobmr   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
			+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
			+ "where fecha_evento_cobmr between '2022-10-01' and '2022-10-31'   " 
		   // + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) " 
		   	+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");
			tab_tabla_hora_extra.ejecutarSql();
			utilitario.addUpdate("tab_tabla_hora_extra");
	
		
	*/
	
TablaGenerica tabConBiometricoMarcacionesResumen=utilitario.consultar("SELECT cobmr.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "cobmr.fecha_evento_cobmr,  "
			+ "cobmr.dia_cobmr,  "
			+ "res.actividades_cobph,  "
			+ "res.productos_eperado_cobph,  "
			+ "cobmr.horainiciohorario_cobmr,  "
			+ "cobmr.horafinhorario_cobmr,   "
			+ "cobmr.horainicioalm_cobmr, "
			+ "cobmr.horainiciobiometrico_cobmr,  "
			+ "cobmr.horafinbiometrico_cobmr,  "
			+ "cobmr.horas25_loep_cobmr ,   "
			+ "cobmr.horas60_loep_cobmr ,   "
			+ "cobmr.recargonocturno25_cobmr ,  	"
			+ "cobmr.horas50_ct_cobmr ,  "
			+ "cobmr.recargonocturno100_cobmr,  "
			+ "cobmr.dias_laborados_cobmr,  "
			+ "cobmr.tiempohoralm_cobmr, "
			+ "cobmr.horainicioband_cobmr, "
			+ "cobmr.horafinband_cobmr, "
			+ "cobmr.horainicioalmbio_cobmr, "
			+ "cobmr.horafinalmbio_cobmr "
			+ "FROM con_biometrico_marcaciones_resumen cobmr   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=cobmr.IDE_GTEMP   "
			+ "left join con_biometrico_plan_hxe res on res.ide_gtemp=cobmr.ide_gtemp  "
			+ "where fecha_evento_cobmr between '"+fecIni+"' and '"+fecFin+"'   " 
		   // + "AND EMP.IDE_GTEMP in(198,138,202,204,520,1021,933,1195,1062,565,172,178,171,522,208,519,206,205,188,174) " 
			+ "ORDER BY EMP.IDE_GTEMP asc,cobmr.fecha_evento_cobmr asc");

//horafinalmbio_cobmr,horainicioalmbio_cobmr
if (tabConBiometricoMarcacionesResumen.getTotalFilas()<=0) {
	utilitario.agregarMensajeInfo("Rango de Fechas  Invalidas",	"No existen datos para el rango seleccionado");
	return;
}else{	
	
	utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set dias_laborados_cobmr=null "
			+ "where fecha_evento_cobmr between '"+fecIni+"' and '"+fecFin+"'   ");

	
		for (int itemReporte=0; itemReporte< tabConBiometricoMarcacionesResumen.getTotalFilas(); itemReporte++) {
			Integer ide_gtemp = Integer.parseInt(tabConBiometricoMarcacionesResumen.getValor(itemReporte, "IDE_GTEMP"));
			Integer tipoEmpleado=getTipoEmpleado(ide_gtemp);//1 TIPO CODIGO DE TRABAJO Y 2 LOEP
			String fechaBiometricoAgrupadaXEmpleado = tabConBiometricoMarcacionesResumen.getValor(itemReporte,"fecha_evento_cobmr");
			Date dateFechaInicioReporteAgrupadaXEmpleado = getFechaAsyyyyMMdd(fechaBiometricoAgrupadaXEmpleado);
		    String fecha=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"FECHA_EVENTO_COBMR");
	        String horarioEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOHORARIO_COBMR");
		    String horaEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAINICIOBIOMETRICO_COBMR");
		    String horarioSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINHORARIO_COBMR");
		    String horaSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"HORAFINBIOMETRICO_COBMR");
		    String empleado=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"ide_gtemp");  
	        String horaDescanso=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"tiempohoralm_cobmr");
	        String horaSinAlmuerzo=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioalm_cobmr");
	        String estadoEntrada=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioband_cobmr");
	        String estadoSalida=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinband_cobmr");
	        String horainicioalmbio_cobmr=tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horainicioalmbio_cobmr");
	        String horafinalmbio_cobmr =tabConBiometricoMarcacionesResumen.getValor(itemReporte,"horafinalmbio_cobmr");

	        double horaExtra=0.00;
	        
	        boolean banderaAtraso=false,banderaAtrasoSalida=false;
	        System.out.println("EMPLEADO : "+empleado+" fecha : "+fecha);
	        boolean sinAlmHorario=false,banderaEntrada=false,banderaSalida=false,banderaAlm=false;
	        
	        
	       if (tipoEmpleado==1) {
			  
	    	TablaGenerica tabEmpleado = utilitario.consultar("select ide_astur,ide_gtemp from gth_empleado where ide_gtemp="+empleado+" AND IDE_ASTUR in(1,26) ");
	   		int turnoMatriz=0;
	   		if (tabEmpleado.getValor("IDE_ASTUR")==null || tabEmpleado.getValor("IDE_ASTUR").isEmpty() || tabEmpleado.getValor("IDE_ASTUR").equals("") ) {
	   			turnoMatriz=0;
				}else {
				turnoMatriz=Integer.parseInt(tabEmpleado.getValor("IDE_ASTUR"));
				} 
	        
			if (horaEntrada==null || horaEntrada.equals("")   || horaEntrada.isEmpty()) {
	        banderaEntrada=false;
			}else{
		    banderaEntrada=true;
			}
			
			
			if (horaSalida==null || horaSalida.equals("")   || horaSalida.isEmpty()) {
				banderaSalida=false;
				}else{
					banderaSalida=true;
				}
			
	        
	        if (horaDescanso == null || horaDescanso.isEmpty() || horaDescanso.equals("0.00")) {
	        	horaDescanso="0.00";
	        	banderaAlm=false;
			}else {
				horaDescanso=horaDescanso;
				banderaAlm=true;
			}
	        
	        
		    boolean horaExtraEmpleado=false,horarioNocturno=false,horaExtraFeriado=false,bandOk=false;
				//Sin no tiene hora de entrada horario entonces  hora al 100%
				if (horarioEntrada==null || horarioEntrada.equals("") || horarioEntrada.isEmpty()) {
					//Si tiene EXTRA O FERIADO
					if (estadoEntrada.equals("EXTRA") || estadoEntrada.equals("FERIADO")) {
						if (banderaEntrada==false) {
							if (banderaSalida==false) {
								bandOk=false;
		
							}else {
								bandOk=false;
							}
							
						}else {
								
							if (banderaSalida==false) {
								bandOk=false;

							}else {
							
								bandOk=true;
							}
							
							
							}//banderaEntrada si tiene marcaciones	
						
						}//Estado Extra o Feriado
					else{
						
					}
					
				}else {//Si no es dia Extra o Feriado
											
						if (banderaEntrada==false && banderaSalida==false) {
							bandOk=false;

							
					}else if (banderaEntrada==false && banderaSalida==true) {
						if (banderaAlm==true) {
							
						 if (horainicioalmbio_cobmr == null || horainicioalmbio_cobmr.isEmpty() || horainicioalmbio_cobmr.equals("") || horainicioalmbio_cobmr.equals("SIN TIMBRE") || horainicioalmbio_cobmr.equals("JUSTIFICADO")) {
							 if (horafinalmbio_cobmr == null || horafinalmbio_cobmr.isEmpty() || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.equals("SIN TIMBRE") || horafinalmbio_cobmr.equals("JUSTIFICADO")) {
									bandOk=false;

							 }else {
								
									horaEntrada=horafinalmbio_cobmr;
									bandOk=true;

							}

								
							}else {
								horaEntrada=horainicioalmbio_cobmr;
								bandOk=true;
	
							}
						 
						 }//Si no tiene almuerzo
						else{
							
							//No hace nada por que no hay entrada
							bandOk=false;
						}
						
						
						
						
					}else if (banderaEntrada==true && banderaSalida==false) {
						////////////////////////////////////////////////////////////////////////////////////////////////////////
						
						
						if (banderaAlm==true) {
							
							 if (horainicioalmbio_cobmr == null || horainicioalmbio_cobmr.isEmpty() || horainicioalmbio_cobmr.equals("") || horainicioalmbio_cobmr.equals("SIN TIMBRE") || horainicioalmbio_cobmr.equals("JUSTIFICADO")) {
								 if (horafinalmbio_cobmr == null || horafinalmbio_cobmr.isEmpty() || horafinalmbio_cobmr.equals("") || horafinalmbio_cobmr.equals("SIN TIMBRE") || horafinalmbio_cobmr.equals("JUSTIFICADO")) {
										bandOk=false;

								 }else {
									
									 horaSalida=horafinalmbio_cobmr;
										bandOk=true;

								}
								 
									
								}else {
									horaSalida=horainicioalmbio_cobmr;
									bandOk=true;	
								}
							 
							 }//Si no tiene almuerzo
							else{
								
								//No hace nada por que no hay entrada
								bandOk=false;
							}
						
						
						////////////////////////////////////////////////////////////////////////////////////////////////////////
					}else if (banderaEntrada==true && banderaSalida==true){ 
					
						bandOk=true;

					}else {
						bandOk=false;

					}
						
						
						
					}
					
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				Date fechaIniExtra=null,fechaFinExtra=null;
				if (bandOk==true) {
					
			
				String fechaEntradaTopeNocturno = fecha+" 19:00:00";
			    Calendar calFechaHoraHorarioFinEntrada = Calendar.getInstance();
			    calFechaHoraHorarioFinEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaTopeNocturno));
			    
			    Calendar calendario = Calendar.getInstance();
			    int hora =calendario.get(Calendar.HOUR_OF_DAY);
			    int minutos = calendario.get(Calendar.MINUTE);
			    int segundos = calendario.get(Calendar.SECOND);
			    
			    String fechaHoraHorarioInicioEntrada = fecha+" "+horaEntrada; //cogo la hora y le concateno con la fecha del horario
			    Calendar calFechaHoraHorarioInicioEntrada = Calendar.getInstance();
			    calFechaHoraHorarioInicioEntrada.setTime(getFechaAsyyyyMMddHHmmss(fechaHoraHorarioInicioEntrada));
		
			    String fechaEntradaNocturno="";
			    fechaEntradaNocturno = fecha+" "+horaEntrada;
				
			    
			    Calendar calFechaEntradaNocturno = Calendar.getInstance();
			    calFechaEntradaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaEntradaNocturno));
			    
			    String fechaSalidaNocturno = fecha+" 17:00:00";
			    Calendar calFechaSalidaNocturno = Calendar.getInstance();
			    calFechaSalidaNocturno.setTime(getFechaAsyyyyMMddHHmmss(fechaSalidaNocturno));
			    
			   
			    
			    ////////////////////if (calFechaHoraHorarioFinEntrada.compareTo(calFechaHoraHorarioInicioEntrada) <= 0 ){
			    
			    if (calFechaEntradaNocturno.compareTo(calFechaSalidaNocturno) >= 0 ){			    	
			    horarioNocturno=true;
				fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
				String sumafecha=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha),1));
				fechaFinExtra = getFechaAsyyyyMMddHHmmss(sumafecha+" "+horaSalida);
				}else {
				horarioNocturno=false;
				fechaIniExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaEntrada);
				fechaFinExtra = getFechaAsyyyyMMddHHmmss(fecha+" "+horaSalida);				
				}
				
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			    

			    
			   
				double tiempoHorasExtra100Auxiliar = (obtenerDiferenciaMinutos(fechaIniExtra, fechaFinExtra));
	 	        double valorHoraDescanso=Double.parseDouble(horaDescanso); 
	 	        String p="";
				int pt_entera=(int)tiempoHorasExtra100Auxiliar;
				double pt_decimal=(tiempoHorasExtra100Auxiliar-pt_entera)*60;
				
				if ((int)pt_decimal<10 && pt_decimal>-1) {
					p="0.0"+(int)pt_decimal;
				}else if ((int)pt_decimal<=-1) {
					p="0.0";
				}
				else {
					p="0."+(int)pt_decimal;
				}
				
				double p1=Double.parseDouble(p);
				horaExtra=pt_entera+p1;							
				if (horaExtra>=0) {
			   	horaExtra=horaExtra/60;		
				}
				else {
					horaExtra=0.00;
				}	

				///////////////////////////////////////////////////////////////////////////////////
				
				double pt_decimal1=(horaExtra-((int)horaExtra))*60;
				String p2="";
				if ((int)pt_decimal1<10 && pt_decimal1>-1) {
					p2="0.0"+(int)pt_decimal1;
				}else if ((int)pt_decimal1<=-1) {
					p2="0.0";
				}
				else {
					p2="0."+(int)pt_decimal1;
				}
				
				double p3=Double.parseDouble(p2);
			
				//if (banderaAtraso==true) {
			//		horaExtra=(int)horaExtra+p3;
					
				//}else {
					horaExtra=(int)horaExtra+p3;
					
				//}
								
									
				
				//VERIFICAR 
				//&& !apobracionHoraNormal.equals("0")
				if (horaExtra>=8) {

					//Guardar calculo en Horas
					String horaExtraNueva="";
					//horaExtraNueva= actualizarTabla(""+horaExtra);
					String horaExtraNueva25="";
					horaExtraNueva25= actualizarTabla(""+horaExtra);
					/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva)+"'"
							+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");*/
				
					/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinextra_cobmr='"+nuevaHoraExtraResumen(horaExtraNueva)+"'"
							+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");		*/
					if (tipo_consulta==true) {
						System.out.println("iNGRESA AQUI");
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set dias_laborados_cobmr=1 "
							+ " where ide_cobmr="+tabConBiometricoMarcacionesResumen.getValor(itemReporte,"IDE_COBMR")+" and fecha_evento_cobmr='"+fecha+"'");
					}
					tab_tabla_hora_extra.setValor(itemReporte,"dias_laborados_cobmr","1");
					
					    
			    
				}
			    
			    
			    
			    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			    
			    
			    
			    
						
				}
	       } 
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
								}//FOR
	        
	        
	        
	        
	        
	        
		
		}//Si existen datos
	 
	 
	 
		
}





public String getArea() {
	return area;
}




public void setArea(String area) {
	this.area = area;
}


public void abrirDialogoHoraExtra(){
	dia_fecha_marcaciones_reporte.dibujar();	
}


public void abrirDialogoEmpleadoHoraExtra(){
	fechaReporteIni=cal_fecha_inicial_marcaciones_reporte.getFecha();
	fechaReporteFin=cal_fecha_final_marcaciones_reporte.getFecha();
	dia_fecha_marcaciones_reporte.cerrar();
	set_empleado_reporte.dibujar();	
}





public void seleccionarEnviar() {
	int estados=0;
	

		boolean estado=false;
			// EnviarCorreo env_enviar = new EnviarCorreo();
			String str_asunto = "REPORTE DE HORAS EXTRA POR EMPLEADO ";
			// Proceso de enviar a cada empleado
			// GenerarReporte ger1 = new GenerarReporte();
			//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();
			TablaGenerica tab_correo_envio = utilitario.consultar("select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co  left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "
					+ " where activo_cpla = true and activo_corr = true and co.ide_corr=1 ");

			EnvioMail envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"), tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"),
					tab_correo_envio.getValor("clave_corr"));

		
			dia_fecha_marcaciones_reporte.cerrar();
			p_parametros=new HashMap();
			TablaGenerica tab_jefe_=null;
			
			if (tipo_perfil.equals("1")) {
				p_parametros.put("ide_asjei","1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
				
		

				 tab_jefe_=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,cor.detalle_gtcor "
						+ "FROM asi_jefe_inmediato asjei "
						+ "left join gth_correo cor on cor.ide_gtemp=asjei.ide_gtemp "
						+ "where asjei.ide_asjei in("+jefe_inmediato_planificacion+") and cor.activo_gtcor=true");
				
			}else {
				p_parametros.put("ide_asjei",jefe_inmediato_planificacion);
				 tab_jefe_=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,cor.detalle_gtcor "
						+ "FROM asi_jefe_inmediato asjei "
						+ "left join gth_correo cor on cor.ide_gtemp=asjei.ide_gtemp "
						+ "where asjei.ide_asjei="+jefe_inmediato_planificacion+" and cor.activo_gtcor=true");
			}
		

			p_parametros.put("fec_ini",fechaReporteIni);
			p_parametros.put("fec_fin",fechaReporteFin);
			//p_parametros.put("jefeInmediato",strNombreEmpleado);
			//p_parametros.put("tthh",strNombreCoordinador);
			p_parametros.put("area",this.area);
		   	TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,detalle_gemes from gen_mes where ide_gemes="+utilitario.getMes(fechaReporteIni)+" ");
		   	p_parametros.put("ide_gemes",Integer.parseInt(tab_mes.getValor("ide_gemes")));
		   	
		   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani='"+utilitario.getAnio(fechaReporteIni)+"' ");
		   	p_parametros.put("ide_geani",Integer.parseInt(tab_anio.getValor("ide_geani")));
		   	
		   	
		   	p_parametros.put("elaborado_por",NombreAprobador);
		   	p_parametros.put("cargo_elaborado_por",cargo_elaborado);

		  	p_parametros.put("aprobado_por",NombreEmpleadoJefe);
		   	p_parametros.put("cargo_aprobador",cargo_jefe_inmediato);
		   	
		   	
		   	
			p_parametros.put("mes",tab_mes.getValor("detalle_gemes"));
			p_parametros.put("titulo","DETALLE HORAS EXTRA POR EMPLEADO");

			
				
			TablaGenerica tab_mantenimientos=utilitario.consultar("select distinct(EMP.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "from  asi_empleado_jefe_inmediato asemp   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=asemp.IDE_GTEMP   "
				+ "where "
				+ "asemp.ide_asjei in("+jefe_inmediato_planificacion+") and "
				+ "asemp.ide_gtemp in("+set_empleado_reporte.getSeleccionados()+") "
				+ "GROUP BY EMP.ide_gtemp, EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
				+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC ");
			
				for (int j = 0; j < tab_mantenimientos.getTotalFilas(); j++) {
					try
					{
						// Busca el correo
							File fil_rol;
							//File fil_rol = generar(p_parametros, "/reportes/rep_rol_de_pagos/rep_rol_individual_consolidado_cambio.jasper", filaActual.getCampos()[0].toString());
							//File fil_rol = generar(p_parametros, "/reportes/rep_rol_de_pagos/rep_n_rol_pagos.jasper", filaActual.getCampos()[0].toString());
							
							//p_parametros.put("IDE_MTEQU",Long.parseLong(tab_mantenimientos.getValor(j,"ide_mtequ")));
							//p_parametros.put("IDE_MTMAN",Long.parseLong(tab_mantenimientos.getValor(j,"IDE_MTMAN")));
							p_parametros.put("logo_empr","/upload/logos/logo.png");
							p_parametros.put("direccion_empr"," OE3G - N51-84 y Av. Rio");
							p_parametros.put("telefono_empr"," (02) 3930-600");
							p_parametros.put("ide_gtemp",tab_mantenimientos.getValor(j,"ide_gtemp"));
					        
							//p_parametros.put("ubicacion",tab_mantenimientos.getValor(j,"detalle_afubi"));
							//sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
							//sef_reporte.dibujar();

							String str_mensaje = tab_correo_envio.getValor("plantilla_cpla");
							str_mensaje = str_mensaje.replaceAll("@FECHA", utilitario.getFechaLarga(utilitario.getFechaActual()));
							str_mensaje = str_mensaje.replaceAll("@HORA", utilitario.getHoraActual());
							str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO",tab_mantenimientos.getValor(j, "APELLIDO_PATERNO_GTEMP")+" "+tab_mantenimientos.getValor(j, "APELLIDO_MATERNO_GTEMP")+" "+tab_mantenimientos.getValor(j, "PRIMER_NOMBRE_GTEMP") + "");
				        	fil_rol = generar(p_parametros, "/reportes/rep_asistencia/horasExtra/rep_mensual_hxe_por_empleado.jasper", 
				        	tab_mantenimientos.getValor(j,"documento_identidad_gtemp")+" "+tab_mantenimientos.getValor(j, "APELLIDO_PATERNO_GTEMP")+" "+tab_mantenimientos.getValor(j, "APELLIDO_MATERNO_GTEMP")+" "+tab_mantenimientos.getValor(j, "PRIMER_NOMBRE_GTEMP"));
					       	//util.EnviaMailInterno(envMail,"juan.ayerve@emgirs.gob.ec", str_asunto, str_mensaje, fil_rol);
					       	
					       	String str_mail="juan.ayerve@emgirs.gob.ec";
							envMail.setAsunto(str_asunto);
							envMail.setCuerpoHtml(str_mensaje);
							envMail.setPara(str_mail);
							if(fil_rol!=null)
							{
								envMail.setNombreAdjunto("hxe_por_empleado.pdf");
								envMail.setAdjuntoArray64(pckUtilidades.Utilitario.fileConvertToArray64(fil_rol));
							}
							pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
							
							if(obj.getRespuesta())
							{
								utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
							}
							else
								utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
					       	
					       	
							} catch (Exception ex) {
							}// tab_jefe_.getValor("detalle_gtcor")
						}
				set_empleado_reporte.cerrar();	
				utilitario.agregarMensajeInfo("Se ha enviado correctamente", "Planificacion de HXE enviada");

			}
			


public File generar(Map parametros, String reporte, String IDE_EMPL) {
	try {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		JasperPrint jasperPrint = null;
		try {

			InputStream fis = ec.getResourceAsStream(reporte);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fis);

			try {
				parametros.put("ide_empr", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr")));
			} catch (Exception e) {
			}
			try {
				parametros.put("ide_sucu", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_sucu")));
			} catch (Exception e) {
			}

			try {
				parametros.put("usuario", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
			} catch (Exception e) {
			}

			parametros.put("SUBREPORT_DIR", utilitario.getURL());

			jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, utilitario.getConexion().getConnection());

		} catch (Exception e) {
			System.out.println("error ejecutar" + e.getMessage());
		}
	
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		File fil_reporte = new File(ec.getRealPath("/reportes/hxe_mensual_" + IDE_EMPL + ".pdf"));
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
		exporter.exportReport();
		return fil_reporte;
	} catch (Exception ex) {
		System.out.println("error" + ex.getMessage());
		ex.printStackTrace();
	}
	return null;
}


public Boolean retornoDiaSiguienteFeriado(String fec,int IDE_GTEMP){

	boolean bandDiaSiguiente=false;
	
	TablaGenerica Tab_fecha_siguiente=utilitario.consultar("SELECT ide_gtemp, fecha_evento_cobmr, horainicioband_cobmr, "
			+ "horafinband_cobmr,es_feriado_cobmr  "
			+ "FROM con_biometrico_marcaciones_resumen  "
			//+ "where fecha_evento_cobmr between '"+fec+"' and '"+fec+"' AND (HORAINICIOBIOMETRICO_COBMR !='') "
			+ "where fecha_evento_cobmr between '"+fec+"' and '"+fec+"' AND IDE_GTEMP="+IDE_GTEMP

			+ "order by ide_gtemp asc, fecha_evento_cobmr asc");

	if (Tab_fecha_siguiente.getTotalFilas()>0) {
		if (Tab_fecha_siguiente.getValor("es_feriado_cobmr")==null  || Tab_fecha_siguiente.getValor("es_feriado_cobmr").equals("") || Tab_fecha_siguiente.getValor("es_feriado_cobmr").isEmpty()) {
			bandDiaSiguiente=false;
		}else {
			bandDiaSiguiente=true;
		}
	}else {
		bandDiaSiguiente=false;
	}
 return bandDiaSiguiente;
}


public void getEmpleadoGenerar(){
	String sql="";
	 if(tipo_perfil.equals("1")){	

			sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
				+ "from  asi_empleado_jefe_inmediato asemp  "
				+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "WHERE asemp.activo_emjei=true "
				+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
				+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
				+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";

}else {
	

	if(tipo_perfil.equals("2")){
	sql="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
				+ "from  asi_empleado_jefe_inmediato asemp  "
				+ "left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR on epar.ide_gtemp=asemp.ide_gtemp   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "WHERE asemp.ide_asjei in ("+jefe_inmediato_planificacion+")  and asemp.activo_emjei=true "
				+ " GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp "
				+ " ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
				+ " EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
		
	}
}



//System.out.println(""+sql);




sel_por_empleado.setSeleccionTabla(sql, "IDE_GTEMP");
sel_por_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
sel_por_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
sel_por_empleado.getTab_seleccion().getColumna("APELLIDO_MATERNO_GTEMP").setFiltro(true);
sel_por_empleado.setHeight("60");
sel_por_empleado.setWidth("40");
sel_por_empleado.setTitle("Seleccione un Empleado");
gru_pantalla.getChildren().add(sel_empleado);
sel_por_empleado.getBot_aceptar().setMetodo("getMarcacionesEmpleadoHorasExtraPorEmpleado");
sel_por_empleado.dibujar();
sel_por_empleado.redibujar();
utilitario.addUpdate("sel_por_empleado");

	
}




public SeleccionTabla getSel_por_empleado() {
	return sel_por_empleado;
}




public void setSel_por_empleado(SeleccionTabla sel_por_empleado) {
	this.sel_por_empleado = sel_por_empleado;
}



public SeleccionTabla getSet_empleado_asignados() {
	return set_empleado_asignados;
}




public void setSet_empleado_asignados(SeleccionTabla set_empleado_asignados) {
	this.set_empleado_asignados = set_empleado_asignados;
}




public String jefeConsulta(String empleado){
	
	String sql_empleadosActivos="select distinct(epar.ide_gtemp), EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "from   GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "where "
				//+ "asemp.ide_geani="+sel_anio.getValorSeleccionado()+" and asemp.ide_gemes in("+mes+") AND "
						+ "emp.ide_gtemp in("+empleado+") "
				//+ "asemp.ide_gtemp not in("+str_ide_empleado_mensual_planificacion+") "
				+ "GROUP BY epar.ide_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,emp.documento_identidad_gtemp  "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC ,  "
				+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
	
	
/*	String sql_empleadosActivos="SELECT  "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP,  "
			+ "GTT.DETALLE_GTTEM "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "LEFT JOIN con_biometrico_marcaciones_resumen cbmr  ON 	cbmr.ide_gtemp=emp.ide_gtemp  "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM     "
			+ "where cbmr.fecha_evento_cobmr between '"+getFechaReporteIni()+"' AND '"+getFechaReporteFin()+"'  "
					+ "and cbmr.ide_gtemp in("+getStr_ide().toString()+") "
			+ "and  EPAR.ACTIVO_GEEDP=TRUE  "
			+ "group by "
			+ "cbmr.ide_gtemp, "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
			+ "DEPA.DETALLE_GEDEP,GTT.DETALLE_GTTEM "
			+ "ORDER BY cbmr.ide_gtemp ASC";
	*/
	return sql_empleadosActivos;
	
}


}
