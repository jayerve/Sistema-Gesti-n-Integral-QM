package paq_biometrico;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.tabview.Tab;
import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imprimir;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_horas_extra_aprobacion_resumen extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private List<String[]> lisHorasEmpleado;
	private AutoCompletar aut_empleados=new AutoCompletar();


    
    public pre_horas_extra_aprobacion_resumen() {
    	
    	
    	bar_botones.agregarComponente(new Etiqueta("Reportes de Biometrico"));
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

    	    	

    	
    	
		aut_empleados.setId("aut_empleados");
		aut_empleados.setAutoCompletar("SELECT emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM  GTH_EMPLEADO EMP " +
				"WHERE EMP.ACTIVO_GTEMP=true");
		aut_empleados.setMetodoChange("seleccionoEmpleado");
		
		//bar_botones.agregarComponente(aut_empleados);

    	
    	//Boton Reporte de Empleado
    	Boton bot_rep_biometrico= new Boton();
    	bot_rep_biometrico.setIcon("ui-icon-calculator");
    	bot_rep_biometrico.setMetodo("validarHorasExtra");
    	bot_rep_biometrico.setValue("Generar Hora Extra");
    	bot_rep_biometrico.setTitle("Generar Horas Extra");
    	bar_botones.agregarBoton(bot_rep_biometrico);
    	
    	
    		// boton limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	//bar_botones.agregarBoton(bot_limpiar);     	
       
    	
    	
    	
    	
    	try {
    		
    		

    		tab_consulta.setId("tab_consulta");
    		tab_consulta.setSql(getConsultaHorasExtra("1900-01-01", "1900-01-01",""));
    		tab_consulta.getColumna("CODIGO").setLongitud(5);
    		tab_consulta.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(40);
    		tab_consulta.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
    		tab_consulta.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CÉDULA");
    
    		tab_consulta.getColumna("NOMBRES_APELLIDOS").setLongitud(60);
    		tab_consulta.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

    		tab_consulta.getColumna("HORAS_25").setLongitud(40);
    		tab_consulta.getColumna("HORAS_25").setFormatoNumero(2);
    		
    		tab_consulta.getColumna("HORAS_25").alinearCentro();
    		tab_consulta.getColumna("HORAS_25").setNombreVisual("HORAS AL 25%");
    		
    		tab_consulta.getColumna("HORAS_50").setLongitud(40);
    		tab_consulta.getColumna("HORAS_50").setFormatoNumero(2);
    		tab_consulta.getColumna("HORAS_50").alinearCentro();
    		tab_consulta.getColumna("HORAS_50").setNombreVisual("HORAS AL 50%");
    		
    		tab_consulta.getColumna("HORAS_100").setLongitud(40);
    		tab_consulta.getColumna("HORAS_100").setFormatoNumero(2);
    		tab_consulta.getColumna("HORAS_100").alinearCentro();
    		tab_consulta.getColumna("HORAS_100").setNombreVisual("HORAS AL 100%");
    		tab_consulta.setHeader("REPORTE Y APROBACIÓN HORAS EXTRA POR EMPLEADO");

    		tab_consulta.setLectura(true);
    		tab_consulta.setRows(20);
    		tab_consulta.dibujar();
    		
		
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    
    public void seleccionoEmpleado(SelectEvent evt){
    	
		aut_empleados.onSelect(evt);
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());
		tab_consulta.setSql(getConsultaHorasExtra(fechaIni,fechaFin,aut_empleados.getValor()));
		tab_consulta.ejecutarSql();
		utilitario.addUpdate("tab_consulta");
		
	}
   
     
    
    
    public void validarHorasExtra(){
        
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
        		tab_consulta.setSql(getConsultaHorasExtra(fechaIni,fechaFin,""));
        		tab_consulta.ejecutarSql();
        		tab_consulta.imprimirSql();
    			utilitario.addUpdate("tab_consulta");
				
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

			}
    		
		}
		else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
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
    
	public void getEmpleado(){
		
	  	
		StringBuilder str_ide=new StringBuilder();
        int int_num_col_idegtemp=set_empleado.getTab_seleccion().getNumeroColumna("IDE_GTEMP");
        for (Fila filaActual:set_empleado.getTab_seleccion().getSeleccionados()) {
                if(str_ide.toString().isEmpty()==false){
                        str_ide.append(",");
                }
                str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
        }

       getMarcacionesEmpleado(str_ide.toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
        set_empleado.cerrar();		
	
    			
    		
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
    
	
	public void getDepartamento(){

			
		
		StringBuilder str_ide=new StringBuilder();
         int int_num_col_idegedep=set_departamento.getTab_seleccion().getNumeroColumna("IDE_GEDEP");
         for (Fila filaActual:set_departamento.getTab_seleccion().getSeleccionados()) {
      	   
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
         
          
          getMarcacionesEmpleado(str_ide_empleado.toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
          set_departamento.cerrar();		
 			
	}

	
	
    @SuppressWarnings("unused")
	public void getMarcacionesEmpleado(String IDE_GTEMP,String fechaInicial, String fechaFinal ){
		int contador = 0;

		int entrada=0,almuerzo=0,salida=0;
	    int entradaNocturno=0,salidaNocturno=0;

		 String fechaBiometrico="";

 		tab_tabla.setLectura(false);
	
 		
    	TablaGenerica tab_reporte;
    	if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null) {
 			tab_tabla.setCondicion("recargonocturno100_cobmr is not null or horafinextra_cobmr is not null and horafinextra_cobmr!='0.0' ");

    		 tab_tabla.ejecutarSql();
    		 if (tab_tabla.getTotalFilas()<0) {
				utilitario.agregarMensajeInfo("No existen Registro(s)", "La fechas seleccionadas no contienen datos ");
			}
    		 
    		 
		}else{
			 tab_tabla.setCondicion("IDE_GTEMP='"+IDE_GTEMP+"' AND FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' ");
    		 tab_tabla.ejecutarSql();
    		 if (tab_tabla.getTotalFilas()<0) {
 				utilitario.agregarMensajeInfo("No existen Registro(s)", "El empleado no contienen datos ");
 			}
  		}

 		
    	TablaGenerica  tabPermisosHorasExtra;
    	String solicitudHorasExtra="";
    	String horaExtra="";
    	String horaExtra100="";
    	for (int itemReporte=0; itemReporte< tab_tabla.getTotalFilas(); itemReporte++) {
    		//Obtengo el ide del empleado con el cual obtendrenmos los horarios
    		Integer ide_gtemp = Integer.parseInt(tab_tabla.getValor(itemReporte, "IDE_GTEMP"));
    	//Obtengo los permisos
    	  tabPermisosHorasExtra=utilitario.consultar("select ide_aspvh,ide_gtemp from asi_permisos_vacacion_hext where tipo_aspvh=3 and  "
    			  + "fecha_desde_aspvh >= '"+fechaInicial+"' and  fecha_hasta_aspvh <='"+fechaFinal+"' and  ide_gtemp=508 and "
  			  	//+ "fecha_desde_aspvh >= '"+fechaInicial+"' and  fecha_hasta_aspvh <='"+fechaFinal+"' and  ide_gtemp="+ide_gtemp+" and "
    						+ "fecha_desde_aspvh is not null and fecha_hasta_aspvh is not null");
    	
        	//Obtengo las horas solicitadas y aprobadas
    	for (int i = 0; i < tabPermisosHorasExtra.getTotalFilas(); i++) {
			solicitudHorasExtra=tabPermisosHorasExtra.getValor(i,"IDE_ASPVH");
		    TablaGenerica tabDetalleHorasExtra=utilitario.consultar("SELECT IDE_ASDHE,NRO_HORAS_APROBADAS_ASDHE,IDE_ASGRI,APROBADO_ASDHE  "
		    		+ "FROM asi_detalle_horas_extras WHERE  "
		    		+ "APROBADO_ASDHE=TRUE  AND  "
		    		//+ "IDE_ASPVH =5554" 
		    		+ "IDE_ASPVH ="+solicitudHorasExtra+" "
		    		+ "order by ide_asgri");
		//Asgri 1=50%
		//Asgri 4=100%
			     
		    
			String ide_asgri=tabDetalleHorasExtra.getValor(i,"IDE_ASGRI");
			String horasAprobadas=tabDetalleHorasExtra.getValor(i,"NRO_HORAS_APROBADAS_ASDHE");
			String aprobadasHorasExtra=tabDetalleHorasExtra.getValor(i,"APROBADO_ASDHE");

			Double horasExtra50Aprobadas=0.0;
		    if (ide_asgri.equals("1")) {
				//Hora al 50%
		    	if (aprobadasHorasExtra.equals("true")) {
		    		
		    		if (Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"))>=Double.parseDouble(horasAprobadas)) {
		    			horasExtra50Aprobadas=Double.parseDouble(horasAprobadas);	
		    			//UPDATE
					}else if (Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"))<Double.parseDouble(horasAprobadas)) {
						horasExtra50Aprobadas=Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"));	
		    			//UPDATE
					}	
		    	}
		    	
		    	
			}
		    else if (ide_asgri.equals("4")) {
			//Hora al 100%
		    	
		    	if (aprobadasHorasExtra.equals("true")) {
		    		if (Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"))>=Double.parseDouble(horasAprobadas)) {
		    			horasExtra50Aprobadas=Double.parseDouble(horasAprobadas);	
		    			//UPDATE
		    		}else if (Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"))<Double.parseDouble(horasAprobadas)) {
						horasExtra50Aprobadas=Double.parseDouble(tab_tabla.getValor(i, "horafinextra_cobmr"));	
		    			//UPDATE
		    		}	
		    	}

			}
			
    	
    	}//PRIMER FOR
    	
    	}   	
    	
    	
    	

    }	

  
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
    

    
public TablaGenerica  obtenerPermisoXEmpleado(String FechaInicio, String FechaFin,String horaInicio, String horaFin,  Integer IDE_GTEMP){
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
        + "from asi_permisos_vacacion_hext where "
  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaFin+"' and ide_gtemp="+IDE_GTEMP+" "
  	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"'");
  
  obtenerPermisos.imprimirSql();
  return obtenerPermisos;
 	
}
 	
 	
 	public void limpiar(){
 		tab_tabla.limpiar();
 		utilitario.addUpdate("tab_tabla,cal_fecha_final,cal_fecha_inicial,aut_empleados");
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
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
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



	public Tabla getTab_consulta() {
		return tab_consulta;
	}



	public void setTab_consulta(Tabla tab_consulta) {
		this.tab_consulta = tab_consulta;
	}
  	 
  	 public String getConsultaHorasExtra(String FechaInicio, String FechaFin, String IDE_GTEMP){
  		 
  		 if (IDE_GTEMP.equals("") || IDE_GTEMP==null || IDE_GTEMP.isEmpty()) {
			String tab_horas_extra="select  emp.ide_gtemp as CODIGO,EMP.DOCUMENTO_IDENTIDAD_GTEMP, EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
  					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' ||  "
  					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
  					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
  					+ "trunc((((sum(res.recargonocturno25_cobmr)-sum(trunc(res.recargonocturno25_cobmr,0)))*100)/60),0)+ sum(trunc(res.recargonocturno25_cobmr,0))+(((((sum(res.recargonocturno25_cobmr)-sum(trunc(res.recargonocturno25_cobmr,0)))*100)/60)-trunc((((sum(res.recargonocturno25_cobmr)-sum(trunc(recargonocturno25_cobmr,0)))*100)/60),0))*60)/100 as HORAS_25, "
  					+ "trunc((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60),0)+ sum(trunc(res.horafinextra_cobmr,0))+(((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60)-trunc((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60),0))*60)/100 as HORAS_50, "
  					+ "trunc((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60),0)+ sum(trunc(res.recargonocturno100_cobmr,0))+(((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60)-trunc((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60),0))*60)/100 as HORAS_100 "
  					+ "from con_biometrico_marcaciones_resumen res "
  					+ "left join gth_empleado emp on emp.ide_gtemp=res.ide_gtemp "
  					+ "where aprueba_hora_extra_cobmr=true and fecha_evento_cobmr between '"+FechaInicio+"' and '"+FechaFin+"' "
  					+ "group by emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP "
  					+ "order by emp.ide_gtemp";
	
			return tab_horas_extra;
  		 }else {
  			String tab_horas_extra="select  emp.ide_gtemp as CODIGO,EMP.DOCUMENTO_IDENTIDAD_GTEMP, EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
  					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' ||  "
  					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
  					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
  					+ "trunc((((sum(res.recargonocturno25_cobmr)-sum(trunc(res.recargonocturno25_cobmr,0)))*100)/60),0)+ sum(trunc(res.recargonocturno25_cobmr,0))+(((((sum(res.recargonocturno25_cobmr)-sum(trunc(res.recargonocturno25_cobmr,0)))*100)/60)-trunc((((sum(res.recargonocturno25_cobmr)-sum(trunc(recargonocturno25_cobmr,0)))*100)/60),0))*60)/100 as HORAS_25, "
  					+ "trunc((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60),0)+ sum(trunc(res.horafinextra_cobmr,0))+(((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60)-trunc((((sum(res.horafinextra_cobmr)-sum(trunc(res.horafinextra_cobmr,0)))*100)/60),0))*60)/100 as HORAS_50, "
  					+ "trunc((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60),0)+ sum(trunc(res.recargonocturno100_cobmr,0))+(((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60)-trunc((((sum(res.recargonocturno100_cobmr)-sum(trunc(res.recargonocturno100_cobmr,0)))*100)/60),0))*60)/100 as HORAS_100 "
  					+ "from con_biometrico_marcaciones_resumen res "
  					+ "left join gth_empleado emp on emp.ide_gtemp=res.ide_gtemp "
  					+ "where res.aprueba_hora_extra_cobmr=true and res.fecha_evento_cobmr between '"+FechaInicio+"' and '"+FechaFin+"' and emp.ide_gtemp="+IDE_GTEMP
  					+ "group by emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP ,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP "
  					+ "order by emp.ide_gtemp";
  			return tab_horas_extra;
		}			
  					
  					
  		}


	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}


	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
  		 
  	 
   	 
}
