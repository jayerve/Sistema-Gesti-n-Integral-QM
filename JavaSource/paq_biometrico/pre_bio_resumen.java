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
import java.util.LinkedList;
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
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_bio_resumen extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_Coordinadores = new Tabla();
    private Tabla tab_calendario = new Tabla();
    private Tabla tab_sql = new Tabla();



    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    private SeleccionTabla set_area = new SeleccionTabla();
    private SeleccionTabla set_areaDepartamento = new SeleccionTabla();
    private String area;
 
    
    
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	private boolean novedad=false,sinNovedad=false;
	
	private boolean matriz=false,operativo=false;
    
    public pre_bio_resumen() {
    	
    	
    	  
	bar_botones.agregarComponente(new Etiqueta("BÚSQUEDA POR SUCURSAL"));
	bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
	cal_fecha_inicial.setFechaActual();
	bar_botones.agregarComponente(cal_fecha_inicial);

	bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
	cal_fecha_final.setFechaActual();
	bar_botones.agregarComponente(cal_fecha_final);

	
	
	
	Boton bot_copiar= new Boton();
	bot_copiar.setIcon("ui-icon-document");
	bot_copiar.setMetodo("getDiferenciaEntradaSalida");
//	bot_copiar.setMetodo("seleccionArea");
	bot_copiar.setValue("REPORTE GENERAL");
	bot_copiar.setTitle("REPORTE GENERAL");
	bar_botones.agregarBoton(bot_copiar);	
    	
    	
    	
	Boton bot_opertivo= new Boton();
	bot_opertivo.setIcon("ui-icon-document");
	bot_opertivo.setMetodo("buscarPersonalMensualOperativo");
	bot_opertivo.setValue("OPERATIVO");
	bot_opertivo.setTitle("OPERATIVO");
//	bar_botones.agregarBoton(bot_opertivo);	
    	

	
	Boton bot_AreaDepartamentoEmpleado= new Boton();
	bot_AreaDepartamentoEmpleado.setIcon("ui-icon-document");
	bot_AreaDepartamentoEmpleado.setMetodo("seleccionArea");
	bot_AreaDepartamentoEmpleado.setValue("BÚSQUEDA");
	bot_AreaDepartamentoEmpleado.setTitle("BÚSQUEDA");
	bar_botones.agregarBoton(bot_AreaDepartamentoEmpleado);	
    	

	Boton bot_entrada= new Boton();
	bot_entrada.setIcon("ui-icon-document");
	bot_entrada.setMetodo("buscarSinEntrada");
	bot_entrada.setValue("SIN ENTRADA");
	bot_entrada.setTitle("SIN ENTRADA");
	bar_botones.agregarBoton(bot_entrada);	
    
	
	Boton bot_salida= new Boton();
	bot_salida.setIcon("ui-icon-document");
	bot_salida.setMetodo("buscarSinSalida");
	bot_salida.setValue("SIN SALIDA");
	bot_salida.setTitle("SIN SALIDA");
	bar_botones.agregarBoton(bot_salida);	
    
	Boton bot_cambioHorario= new Boton();
	bot_cambioHorario.setIcon("ui-icon-document");
	bot_cambioHorario.setMetodo("buscarCambioHorario");
	bot_cambioHorario.setValue("CAMBIO TURNO");
	bot_cambioHorario.setTitle("CAMBIO TURNO");
	bar_botones.agregarBoton(bot_cambioHorario);	
    
	
	
	
    		// boton limpiar
    	Boton bot_limpiar = new Boton();
    	bot_limpiar.setIcon("ui-icon-cancel");
    	bot_limpiar.setMetodo("limpiar");
    	bar_botones.agregarBoton(bot_limpiar);     	
       
    	
    	set_empleado.setId("set_empleado");
    	set_empleado.setSeleccionTabla(ser_empleado.servicioEmpleadosActivos("true"), "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("apellido_materno_gtemp").setFiltro(true);
    	set_empleado.setTitle("Seleccione un Empleado");
		gru_pantalla.getChildren().add(set_empleado);
		set_empleado.getBot_aceptar().setMetodo("getEmpleado");
		agregarComponente(set_empleado);

		
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT IDE_GEDEP,DETALLE_GEDEP FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=TRUE", "IDE_GEDEP");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		set_departamento.setTitle("Seleccione Departamento");
		gru_pantalla.getChildren().add(set_departamento);
		set_departamento.getBot_aceptar().setMetodo("getDepartamento");
		agregarComponente(set_departamento);

    	
    	
   	
    	
    	
   	
    	
		set_area.setId("set_area");
		set_area.setSeleccionTabla("SELECT AREA.IDE_GEARE,  "
				+ "AREA.DETALLE_GEARE "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "WHERE AREA.IDE_GEARE NOT IN (16) "
				+ "GROUP BY AREA.IDE_GEARE,  "
				+ "AREA.DETALLE_GEARE "
				+ "order by detalle_geare asc", "IDE_GEARE");
		set_area.setRadio();
		set_area.getTab_seleccion().getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		set_area.getTab_seleccion().getColumna("DETALLE_GEARE").setFiltro(true);
		set_area.setTitle("Seleccione AREA");
		gru_pantalla.getChildren().add(set_area);
		set_area.getBot_aceptar().setMetodo("getArea");
		agregarComponente(set_area);

    	
    	
		
		set_areaDepartamento.setId("set_areaDepartamento");
		
		agregarComponente(set_areaDepartamento);

		
    	
    	
    	
	    
	    
		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(getConsultaSucursal());
		tab_tabla.setCampoPrimaria("ide_cobmr");
		tab_tabla.setNumeroTabla(1);
		tab_tabla.setLectura(true);

		
		tab_tabla.getColumna("IDE_COBMR").setLongitud(5);

		tab_tabla.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
        tab_tabla.getColumna("NOMBRES_APELLIDOS").alinearCentro();
        tab_tabla.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
		tab_tabla.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

		
		tab_tabla.getColumna("NOM_SUCU").setLongitud(15);
        tab_tabla.getColumna("NOM_SUCU").alinearCentro();
        tab_tabla.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");
		tab_tabla.getColumna("NOM_SUCU").setFiltro(true);
        
        
    	
		tab_tabla.getColumna("DETALLE_GEARE").setLongitud(15);
        tab_tabla.getColumna("DETALLE_GEARE").alinearCentro();
        tab_tabla.getColumna("DETALLE_GEARE").setNombreVisual("AREA");
		tab_tabla.getColumna("DETALLE_GEARE").setFiltro(true);
        
        
        tab_tabla.getColumna("DETALLE_GEDEP").setLongitud(15);
        tab_tabla.getColumna("DETALLE_GEDEP").alinearCentro();
        tab_tabla.getColumna("DETALLE_GEDEP").setNombreVisual("DEPT");
		tab_tabla.getColumna("DETALLE_GEDEP").setFiltro(true);


        tab_tabla.getColumna("DIA_COBMR").setLongitud(15);
        tab_tabla.getColumna("DIA_COBMR").alinearCentro();
        tab_tabla.getColumna("DIA_COBMR").setNombreVisual("DIA");
        
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
         
        
        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setLongitud(15);
        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setNombreVisual("ENTRADA");

        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setLongitud(15);
        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setNombreVisual("TIMBRE");
        
        
        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setLongitud(15);
        tab_tabla.getColumna("HORAINICIOBAND_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setNombreVisual("ESTADO ENTRADA");
      	        
        tab_tabla.getColumna("HORAINICIOALM_COBMR").setLongitud(15);
        tab_tabla.getColumna("HORAINICIOALM_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAINICIOALM_COBMR").setNombreVisual("H.INI.ALM");
 
             

        tab_tabla.getColumna("HORAFINALM_COBMR").setLongitud(16);
        tab_tabla.getColumna("HORAFINALM_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAFINALM_COBMR").setNombreVisual("H.FIN.ALM");
        
        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setLongitud(20);
        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setNombreVisual("TIM.INI.ALM");  

        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setLongitud(20);
        tab_tabla.getColumna("HORAFINALMBIO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setNombreVisual("TIM.FIN.ALM");  
        
        tab_tabla.getColumna("TIEMPOALM_COBMR").setLongitud(10);
        tab_tabla.getColumna("TIEMPOALM_COBMR").alinearCentro();
        tab_tabla.getColumna("TIEMPOALM_COBMR").setNombreVisual("TIM.TOMA.ALM"); 
        
        
        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setLongitud(10);
        tab_tabla.getColumna("TIEMPOHORALM_COBMR").alinearCentro();
        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setNombreVisual("TIEMPOHORALM"); 
        
                
        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setLongitud(25);
        tab_tabla.getColumna("HORAFINHORARIO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setNombreVisual("HOR.SAL");  
        
        
        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setLongitud(20);
        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setNombreVisual("TIM.HOR.SAL");  

        tab_tabla.getColumna("HORAFINBAND_COBMR").setLongitud(20);
        tab_tabla.getColumna("HORAFINBAND_COBMR").alinearCentro();
        tab_tabla.getColumna("HORAFINBAND_COBMR").setNombreVisual("ESTADO SALIDA");  
       
        
        tab_tabla.getColumna("DETALLE_GTTEM").setLongitud(20);
        tab_tabla.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMP");  
        tab_tabla.getColumna("DETALLE_GTTEM").alinearCentro();

        
        tab_tabla.getColumna("DIFERENCIAENTRADA").setLongitud(20);
        tab_tabla.getColumna("DIFERENCIAENTRADA").setNombreVisual("ENT VS SAL");  
        tab_tabla.getColumna("DIFERENCIAENTRADA").alinearCentro();

        
        
        tab_tabla.getColumna("DIFERENCIAENTRADATIMBRADO").setLongitud(20);
        tab_tabla.getColumna("DIFERENCIAENTRADATIMBRADO").setNombreVisual("ENT VS TENT");  
        tab_tabla.getColumna("DIFERENCIAENTRADATIMBRADO").alinearCentro();

        

        tab_tabla.getColumna("DIFERENCIASALIDATIMBRADO").setLongitud(20);
        tab_tabla.getColumna("DIFERENCIASALIDATIMBRADO").setNombreVisual("SAL VS TSAL");
        tab_tabla.getColumna("DIFERENCIASALIDATIMBRADO").alinearCentro();


        tab_tabla.getColumna("DIFERENCIASALIDAALM").setLongitud(20);
        tab_tabla.getColumna("DIFERENCIASALIDAALM").setNombreVisual("SALM VS TALMS");  
        tab_tabla.getColumna("DIFERENCIASALIDAALM").alinearCentro();


        tab_tabla.getColumna("DIFERENCIAENTRADAALM").setLongitud(20);
        tab_tabla.getColumna("DIFERENCIAENTRADAALM").setNombreVisual("EALM VS TALME");  
        tab_tabla.getColumna("DIFERENCIAENTRADAALM").alinearCentro();

        
        
      
		tab_tabla.setCondicion("ide_cobmr=-1");
	
		
		tab_tabla.setRows(20);
		tab_tabla.dibujar();
		tab_tabla.setHeader("REPORTE RESUMEN BIOMÉTRICO POR EMPLEADO");

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
    	
        
        
		tab_Coordinadores.setId("tab_Coordinadores");
		tab_Coordinadores.setSql(getCoordinadores());
		tab_Coordinadores.setCampoPrimaria("ide_geedp");
		tab_Coordinadores.setNumeroTabla(2);
		tab_Coordinadores.setLectura(true);
		
	      
		tab_Coordinadores.getColumna("IDE_GEEDP").setLongitud(10);
		tab_Coordinadores.getColumna("IDE_GEEDP").alinearCentro();
		tab_Coordinadores.getColumna("IDE_GEEDP").setNombreVisual("COD"); 
        
                
		tab_Coordinadores.getColumna("DETALLE_GECAF").setLongitud(20);
		tab_Coordinadores.getColumna("DETALLE_GECAF").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GECAF").setNombreVisual("PUESTO");  
        
        
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").setLongitud(100);
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").alinearCentro();
		tab_Coordinadores.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");  

		tab_Coordinadores.getColumna("NOM_SUCU").setLongitud(50);
		tab_Coordinadores.getColumna("NOM_SUCU").alinearCentro();
		tab_Coordinadores.getColumna("NOM_SUCU").setNombreVisual("SUCURSAL");  
		
			
		tab_Coordinadores.getColumna("DETALLE_GEARE").setLongitud(120);
		tab_Coordinadores.getColumna("DETALLE_GEARE").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GEARE").setNombreVisual("AREA");  
		
		tab_Coordinadores.getColumna("DETALLE_GEDEP").setLongitud(140);
		tab_Coordinadores.getColumna("DETALLE_GEDEP").alinearCentro();
		tab_Coordinadores.getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");  
		tab_Coordinadores.onSelect("actualizarJefe");
		
		tab_Coordinadores.setRows(5);
		tab_Coordinadores.dibujar();
		
		tab_Coordinadores.setHeader("APROBADORES POR SUCURSAL");

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_Coordinadores);
    	
           
        
        tab_calendario.setId("tab_calendario");
        tab_calendario.setSql(getAsignacionEmpleado());
        tab_calendario.setCampoPrimaria("ide_astur");
        tab_calendario.getColumna("IDE_ASTUR").setLongitud(10);
        tab_calendario.getColumna("IDE_ASTUR").alinearCentro();
        tab_calendario.getColumna("IDE_ASTUR").setNombreVisual("COD");  
		
        tab_calendario.getColumna("DESCRIPCION_ASTUR").setLongitud(120);
        tab_calendario.getColumna("DESCRIPCION_ASTUR").alinearCentro();
        tab_calendario.getColumna("DESCRIPCION_ASTUR").setNombreVisual("DESCRIPCIÓN");  
	    
        
        tab_calendario.setNumeroTabla(3);
        tab_calendario.setLectura(true);
        tab_calendario.dibujar();
        tab_calendario.setHeader("HORARIO POR DÍA");

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_calendario);
    	

        /*
        
        String sql="select ide_gtemp, "
        		+ "'' as horas "
        		+ "from con_biometrico_marcaciones_resumen	 "
        		+ "order by ide_gtemp asc, fecha_evento_cobmr asc";
        
        
        
        
        
        
   		tab_sql.setId("tab_sql");
   		tab_sql.setSql(sql);
   		tab_sql.setCampoPrimaria("cod");
   		tab_sql.setLectura(true);
   		tab_sql.dibujar();
        PanelTabla pat_panelSQL = new PanelTabla();
        pat_panelSQL.setPanelTabla(tab_sql);
        */
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1,pat_panel2,"75%","V");

        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir1(pat_panel);
        
        agregarComponente(div_division1);
    }

    
    
    public void justificarTimbradas(){
    	
		
    	try {
    		tab_tabla.setLectura(false);

    		
		    int banderaJustificaEntrada=0;
		    String justificacionEntrada="";
		    String justificacionAlmuerzoSalida="";
		    String justificacionAlmuerzoEntrada="";
		    String justificacionSalida="";
		    int banderaEntrada=0;
		    int banderaSalida=0;

    		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			
    		//Obtengo los horarios de cada empleado de acuerdo a su ide_gtemp
    		TablaGenerica horariosEmpleado = this.obtenerHorariosEmpleado(Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
    		
    		//para turno nocturnos
    		TablaGenerica  obtenerTurnoEmpleado=this.obtenerHorariosTurnoEmpleado(Integer.parseInt(horariosEmpleado.getValor("IDE_ASHOR").toString()));
    		String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();

    		TablaGenerica obtenerPermisoXEmpleadoJustificacion;
    		//Para justificacion de permiso de entrada y salida
    		String permisoEntradaNocturno="";
    		String permisoSalidaNocturno="";
			if (ideGrupoEmpleado.equals("3")) {
			//	System.out.println("TIPO NOCTURNO");
				String fechaBiometrico=utilitario.getFormatoFecha(tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));
				String entradaNocturno =tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
				String entradaTimbreNocturno =tab_tabla.getValor(i,"horainiciobiometrico_cobmr");
				String salidaTimbreNocturno =tab_tabla.getValor(i,"horafinbiometrico_cobmr");
				String salidaNocturno =tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
			    String banderaEntradaNocturno=tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");
			    String banderaSalidaNocturno=tab_tabla.getValor(i,"HORAFINBAND_COBMR");
				
			    
				
			//	System.out.println("IDE_GTEMP :"+tab_tabla.getValor(i,"IDE_GTEMP")+" FECHA:"+tab_tabla.getValor(i,"FECHA_EVENTO_COBMR")+"HORA INICIO: "+entradaTimbreNocturno+"HORA FIN: "+salidaTimbreNocturno);
				if (banderaEntradaNocturno==null || banderaEntradaNocturno.isEmpty()) {
   			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,entradaNocturno,"24:00:00",Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
   			    permisoEntradaNocturno=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");	
   			    if (permisoEntradaNocturno!=null) {
   			    	
   	 				utilitario.getConexion().ejecutarSql(""
   	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
   	  						+ "horainiciobiometrico_cobmr='"+entradaNocturno+"', " 
   	  						+ "horainicioband_cobmr='"+permisoEntradaNocturno+"' "
   	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
   	  					utilitario.addUpdate("tab_tabla");
   			    }
				}
				
				
				
				 if(banderaEntradaNocturno.equals("ATRASADA"))
				{
 			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,entradaNocturno,entradaTimbreNocturno,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
 			    permisoEntradaNocturno=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");	
  			    if (permisoEntradaNocturno!=null) {
  					utilitario.getConexion().ejecutarSql(""
   	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
   	  						+ "horainiciobiometrico_cobmr='"+entradaNocturno+"', " 
   	  						+ "horainicioband_cobmr='"+permisoEntradaNocturno+"' "
   	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
   	  					
   	  					utilitario.addUpdate("tab_tabla");
					}
			     
			    

				}
				 
				
			
				if (banderaSalidaNocturno==null || banderaSalidaNocturno.isEmpty()) {
	   			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,"00:00:00",salidaNocturno,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
	   			   	permisoSalidaNocturno=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
	   			    if (permisoSalidaNocturno!=null) {
	   					utilitario.getConexion().ejecutarSql(""
	   	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
	   	  						+ "horafinbiometrico_cobmr='"+salidaNocturno+"', " 
	   	  						+ "horafinband_cobmr='"+permisoSalidaNocturno+"' "
	   	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
	   	  					utilitario.addUpdate("tab_tabla");
				}
				}
				
				if (banderaSalidaNocturno.equals("ANTICIPADA")) {
	   			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,"00:00:00",salidaNocturno,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
	   			  	permisoSalidaNocturno=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
	   			    if (permisoSalidaNocturno!=null) {
	   			    	utilitario.getConexion().ejecutarSql(""
	   	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
	   	  						+ "horafinbiometrico_cobmr='"+salidaNocturno+"', " 
	   	  						+ "horafinband_cobmr='"+permisoSalidaNocturno+"' "
	   	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
	   	  					utilitario.addUpdate("tab_tabla");
	   			    }
	   			    
				
				
				}
				 

			}else {

				
		String fechaBiometrico=utilitario.getFormatoFecha(tab_tabla.getValor(i,"FECHA_EVENTO_COBMR"));
		String entradaHorario=tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
		String entradaNormal =tab_tabla.getValor(i,"horainiciobiometrico_cobmr");
		String entradaBandera =tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");

		
		String salidaAlmuerzoHorario =tab_tabla.getValor(i,"HORAINICIOALM_COBMR");
		String salidaAlmuerzo =tab_tabla.getValor(i,"horainicioalmbio_cobmr");
		
		
		String entradaAlmuerzoHorario=tab_tabla.getValor(i,"HORAFINALM_COBMR");
		String entradaAlmuerzo =tab_tabla.getValor(i,"horafinalmbio_cobmr");
		
		String tiempoAlmuerzoTomado =tab_tabla.getValor(i,"tiempoalm_cobmr");
		String tiempoAlmuerzoAignado=tab_tabla.getValor(i,"tiempohoralm_cobmr");
		
		String salidaHorario=tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
		String salidaNormal =tab_tabla.getValor(i,"horafinbiometrico_cobmr");
		String salidaBandera =tab_tabla.getValor(i,"HORAFINBAND_COBMR");     
		
		
		

//Entrada		
		
	
	 	if (tab_tabla.getValor(i,"HORAINICIOBAND_COBMR")==null || tab_tabla.getValor(i,"HORAINICIOBAND_COBMR").isEmpty()) {
	 
			
			if ((tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR")==null || tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR").isEmpty()) ) {
				break;
			}
			
			if (tab_tabla.getValor(i,"HORAINICIOALM_COBMR")==null  || tab_tabla.getValor(i,"HORAINICIOALM_COBMR").isEmpty()) {
				break;
			}
			
		    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,entradaHorario,salidaAlmuerzoHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  			 justificacionEntrada=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  			if (justificacionEntrada!=null) {
  				utilitario.getConexion().ejecutarSql(""
  						+ "UPDATE con_biometrico_marcaciones_resumen set "
  						+ "horainiciobiometrico_cobmr='"+entradaHorario+"', " 
  						+ "horainicioband_cobmr='OK' "
  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
  					banderaEntrada=1;
  					utilitario.addUpdate("tab_tabla");
  			}
		}
		else {
			
		}
		
			
	 	if (entradaBandera.equals("ATRASADA")) {
  			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,entradaHorario,entradaNormal,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  	  			 justificacionEntrada=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  	  			if (justificacionEntrada!=null) {
  	  				utilitario.getConexion().ejecutarSql(""
  	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
  	  						+ "horainiciobiometrico_cobmr='"+entradaHorario+"', " 
  	  						+ "horainicioband_cobmr='OK' "
  	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
  		
  	  			banderaEntrada=1;
  	  			}
  		
		}
	 	
	 	
		
	//   ALMUERZO
  		
  	int banderaSalidaAlmuerzo=0;
  	int banderaEntradaAlmuerzo=0;
  	//String valorSalidaAlmuerzo=0;	

  	
  	if (salidaAlmuerzo==null || salidaAlmuerzo.isEmpty()) {
		    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,salidaAlmuerzoHorario,entradaAlmuerzoHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  			 justificacionAlmuerzoSalida=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  			if (justificacionAlmuerzoSalida!=null) {
  				
  	//				utilitario.getConexion().ejecutarSql(""
  	//						+ "UPDATE con_biometrico_marcaciones_resumen set "
  //							+ "horainicioalmbio_cobmr='"+entradaHorario+"'");
  	   banderaSalidaAlmuerzo=1;
  		}
  													}
  	
  	else {
  		banderaSalidaAlmuerzo=2;
	}
  	
 
  	
  	
  	
  	
  	
  	
  	//sumar una hora a la de entrada
  	if (banderaSalidaAlmuerzo==1) {
  	  	if (entradaAlmuerzo==null || entradaAlmuerzo.isEmpty()) {
  		obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,salidaAlmuerzoHorario,	entradaAlmuerzoHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  	  			 justificacionAlmuerzoEntrada=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  	  			if (justificacionAlmuerzoEntrada!=null) {
  	  				utilitario.getConexion().ejecutarSql(""
  	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
  	  						+ "horainicioalmbio_cobmr='JUSTIFICADO', "
  	  						+ "horafinalmbio_cobmr ='JUSTIFICADO', "
  	  						+ "tiempoalm_cobmr='60' "
  	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
  	  			}
  	  			}
  	  			
  	  
  	}
  	
  	if (banderaSalidaAlmuerzo==2) {
  	  	if (entradaAlmuerzo==null || entradaAlmuerzo.isEmpty()) {
  		obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,salidaAlmuerzo,entradaAlmuerzoHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  	  			 justificacionAlmuerzoEntrada=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  	  			if (justificacionAlmuerzoEntrada!=null) {
  	  			utilitario.getConexion().ejecutarSql(""
	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
	  						+ "horainicioalmbio_cobmr="+salidaAlmuerzo
	  						+ " ,horafinalmbio_cobmr ='JUSTIFICADO', "
	  						+ "tiempoalm_cobmr='60' "
	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
	  		  		banderaEntradaAlmuerzo=1;
  	  				}
  	  			}
  	}
		  		
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 		
  

			
  	//Salida		
   		if (salidaBandera==null ||  salidaBandera.isEmpty()) {
  			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,entradaAlmuerzoHorario,salidaHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  	  			 justificacionSalida=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  	  			if (justificacionSalida!=null) {
  	  				utilitario.getConexion().ejecutarSql(""
  	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
  	  						+ "HORAFINBIOMETRICO_COBMR='"+salidaHorario+"', " 
  	  						+ "HORAFINBAND_COBMR='OK' "
  	  						+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
  	  			}
  	  			
  			}
  			
  				
  	  		if (salidaBandera.equals("ANTICIPADA")) {
  	  			    obtenerPermisoXEmpleadoJustificacion=obtenerPermisoXEmpleado(fechaBiometrico,fechaBiometrico,salidaNormal,salidaHorario,Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP")));
  	  	  			 justificacionSalida=obtenerPermisoXEmpleadoJustificacion.getValor("IDE_ASPVH");
  	  	  			if (justificacionEntrada!=null) {
  	  	  				utilitario.getConexion().ejecutarSql(""
  	  	  						+ "UPDATE con_biometrico_marcaciones_resumen set "
  	  	  						+ "HORAFINBIOMETRICO_COBMR='"+salidaHorario+"', " 
  	  	  						+ "HORAFINBAND_COBMR='OK' "
  	  							+ "where ide_cobmr="+tab_tabla.getValor(i,"IDE_COBMR"));
  	  	  			}
  	  		
  			}
  		
  		
  	  	
  		
  		
			}//else	
    		}//for
    		
    		
    		
    		tab_tabla.setCondicion("horainicioband_cobmr=''  or tiempoalm_cobmr ='' or horafinband_cobmr='' ");
    		tab_tabla.ejecutarSql();
    		
    		
    		utilitario.addUpdate("tab_tabla");
    		
    	} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}		
			
			
    }
    
    
    
    public void seleccionarReporteTotal(){
    	novedad=false;
    	sinNovedad=false;
        
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        		String fechaIni=(cal_fecha_inicial.getFecha());	
        		String fechaFin=(cal_fecha_final.getFecha());	
                tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaIni+"' and '"+fechaFin+"' ");
                tab_tabla.ejecutarSql();
                utilitario.addUpdate("tab_tabla");
        		//getMarcacionesEmpleado("",fechaIni,fechaFin);
				
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

		 //tab_tabla.setLectura(false);
 		//System.out.println(tab_tabla.getValor("IDE_COBMR"));
 		//System.out.println(tab_tabla.getValorSeleccionado());
		 
 		
 		
    	TablaGenerica tab_reporte;
    	if (IDE_GTEMP.isEmpty() || IDE_GTEMP==null) {
    		 tab_tabla.setCondicion("FECHA_EVENTO_COBMR BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' ");
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
 		utilitario.addUpdate("aut_empleados");
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

   	 
    	//Para resta de Horas

   	private String obtenerDiferenciaMinutos(Date fechaInicial, Date fechaFinal){
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
		//Diferencia en horas 
		double total_diferencia_minutos = total_diferencia_horas * 60;

		
		String retornaValor="";
		if (total_diferencia_minutos>=60) {
			int part_ent=(int)total_diferencia_horas;
			double resultado=total_diferencia_horas-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" hor";
		
		
		}else {
			int part_ent=(int)total_diferencia_minutos;
			double resultado=total_diferencia_minutos-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" min";
			
		}
		
		
		
		
		
		
		//retorno la diferencia en horas
		//return total_diferencia_horas;
		return retornaValor;
    }
   	
   	
   	
   	//Para resta de Minutos
   	
   	private String obtenerDiferenciaMinutos1(Date fechaInicial, Date fechaFinal){
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
		//Diferencia en horas 
		double total_diferencia_minutos = total_diferencia_horas * 60;
		String retornaValor="";
		if (total_diferencia_minutos>=60) {
			int part_ent=(int)total_diferencia_horas;
			double resultado=total_diferencia_horas-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" hor";
		
		
		}else {
			int part_ent=(int)total_diferencia_minutos;
			double resultado=total_diferencia_minutos-part_ent;
			double resultadoHorasMinutos=resultado*60;
			String min="0."+(int)resultadoHorasMinutos;
			double minTotal=pckUtilidades.CConversion.CDbl(min);
			total_diferencia_minutos=part_ent+minTotal;
			retornaValor=""+total_diferencia_minutos+" min";
			
    }

		
		//retorno la diferencia en horas
		//return total_diferencia_minutos;
		return retornaValor;
    }
   	
   	
   	

  //Sumar horas en fecha
  	 public Date sumarRestarHorasFecha(Date fecha, int horas){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(fecha); // Configuramos la fecha que se recibe
       calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
       return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

  	 }



	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}



	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
  	 
  	 

	public void  seleccionarNovedades(){
       	String sql="SELECT res.ide_cobmr, "
    				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
    				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
    				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
    				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
    				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
    				+ "DEPA.DETALLE_GEDEP, "
    				+ "res.dia_cobmr, "
    				+ "res.fecha_evento_cobmr, "
    				+ "res.horainiciohorario_cobmr, "
    				+ "res.horainiciobiometrico_cobmr, "
    				+ "res.horainicioband_cobmr, "
    				+ "res.horainicioalm_cobmr, "
    				+ "res.horafinalm_cobmr, "
    				+ "res.horainicioalmbio_cobmr, "
    				+ "res.horafinalmbio_cobmr, "
    				+ "res.tiempoalm_cobmr, "
    				+ "res.tiempohoralm_cobmr, "
    				+ "res.horafinhorario_cobmr, "
    				+ "res.horafinbiometrico_cobmr, "
    				+ "res.horafinband_cobmr "
    				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
    				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
    				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
    				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
    				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
    				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
    				+ "where SUCU.IDE_SUCU=-1 and epar.activo_geedp=true "
    				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
    				+ "AND horainicioband_cobmr!='OK' AND horafinband_cobmr!='OK'  and tiempoalm_cobmr is null "
    				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc";
    	tab_tabla.setSql(sql);	
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    		
	}

	
	public void seleccionarSinNovedades(){
		sinNovedad=true;
		novedad=false;

        tab_tabla.setCondicion("novedad_cobmr=true and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and  '"+cal_fecha_final.getFecha()+"'");
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");		
	}
	
	
	
	public void seleccionoEmpleado(SelectEvent evt){
	
		aut_empleados.onSelect(evt);
		String fechaIni=(cal_fecha_inicial.getFecha());	
		String fechaFin=(cal_fecha_final.getFecha());
		actualizarMarcaciones(fechaIni,fechaFin,novedad,sinNovedad);	
	}


	public void actualizarMarcaciones(String fechaIni,String fechaFin,boolean novedad,boolean sinNovedad){
		
		
		
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null && aut_empleados.getValor()!=null) {
			if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
				String fechaInicioNovedad=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaIni),-1));
				String fechaFinNovedad =getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fechaFin),1));

				if (novedad==true) {
			 		tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' "
			 				+ "and novedad_cobmr=false ");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");
						
				}
				if (sinNovedad==true) {
			 		tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' "
			 				+ "and novedad_cobmr=true");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");
				}
				
				if (sinNovedad==false && novedad==false){
					tab_tabla.setCondicion("fecha_evento_cobmr > '"+fechaInicioNovedad+"' and fecha_evento_cobmr < '"+fechaFinNovedad+"' and  ide_gtemp='"+aut_empleados.getValor()+"' ");
		    		tab_tabla.ejecutarSql();
		    		utilitario.addUpdate("tab_tabla");	
				}
	    	 	
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");
				
			}
		}
		
				

	}



	public boolean isNovedad() {
		return novedad;
	}



	public void setNovedad(boolean novedad) {
		this.novedad = novedad;
	}



	public boolean isSinNovedad() {
		return sinNovedad;
	}



	public void setSinNovedad(boolean sinNovedad) {
		this.sinNovedad = sinNovedad;
	}


	public void getDiferenciaEntradaSalida(){
		
		tab_tabla.setSql("SELECT res.ide_cobmr,emp.ide_gtemp,emp.documento_identidad_gtemp, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP, "
			+ "res.dia_cobmr, "
			+ "res.fecha_evento_cobmr, "
			+ "res.horainiciohorario_cobmr, "
			+ "res.horainiciobiometrico_cobmr, "
			+ "res.horainicioband_cobmr, "
			+ "res.horainicioalm_cobmr, "
			+ "res.horafinalm_cobmr, "
			+ "res.horainicioalmbio_cobmr, "
			+ "res.horafinalmbio_cobmr, "
			+ "res.tiempoalm_cobmr, "
			+ "res.tiempohoralm_cobmr, "
			+ "res.horafinhorario_cobmr, "
			+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr,  "
				+ "GTT.DETALLE_GTTEM, "
				+ "'' as DIFERENCIAENTRADA, "
				+ "'' as diferenciaEntradaTimbrado, "
				+ "'' as diferenciaSalidaTimbrado, "
				+ "'' as diferenciaSalidaAlm, "
				+ "'' as diferenciaEntradaAlm "

				
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM "
				+ "where epar.activo_geedp=true "
			+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc");
		tab_tabla.ejecutarSql();
				utilitario.addUpdate("tab_tabla");
	
		//Una vez que traigo los datos calculo las horas trabajadas
		uno();
		


		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		dos();
		
		//Una vez que traigo los datos calculo las horas entrada vs la hora timbrada
		tres();
		
		
	cuatro();
		
		quinto();
		
	}
	
	//DIFERENCIA ENTRE ENTRADA Y SALIDA
	
	public void uno(){
		boolean entrada=false,salida=false;

		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		String tiempoTrabajoXDia="";
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
		
			if ((entradaTimbre==null) || (entradaTimbre.isEmpty()) || (entradaTimbre.equals(""))  || (entradaTimbre.equals("JUSTIFICADO"))
					
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				entrada=true;
			}else {
				entrada=false;

			}
	
			if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) || (salidaTimbre.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}
			
			Date fechaIniAlmBio = null;
	    	Date fechaFinAlmBio = null;
			//Si hay salida y entrada 
			if (entrada==false && salida==false) {
				
				
				//Validamos si el timbre de la entrada es es mayor o menro q el horario planificado
				if (entradaTimbre.compareTo(entradaHorario)<=0) {
				// Si es menor le pongo la misma hora que el horario
					fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
					}else {
				//	Si es mayor escogo la hora de timbrado	
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);

					}	
				
				
				String horaTipoTurno="20:00:00";
				boolean tipoTurno=false;
				if (entradaTimbre.compareTo(horaTipoTurno)>=0) {
					tipoTurno=true;
				}else {
					tipoTurno=false;
				}
				
				if (tipoTurno==false) {
					
				
		    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
		    	tiempoTrabajoXDia = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}else {

					
					
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaBiometrico),1))+" "+salidaTimbre);
			    	tiempoTrabajoXDia = obtenerDiferenciaMinutos(fechaIniAlmBio, fechaFinAlmBio);
			    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
			    	listaEmpleado.add(empleado);
					tab_tabla.setValor(i,"DIFERENCIAENTRADA",""+tiempoTrabajoXDia);
				}
		    				}
			else {
				
			}
		}
	}
	
	
	//DIFERENCIA ENTRE ENTRADA HORARIO Y ENTRADA TIMBRE

	public void dos(){
		boolean entrada=false,bandEntradaHorario=false;
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		String tiempoTrabajoXDia="";
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			//salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			//salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
		
			if ((entradaTimbre==null) || (entradaTimbre.isEmpty()) || (entradaTimbre.equals("")) || (entradaTimbre.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))		
					) {
				//si entrada vacia o sin datos
				entrada=true;
			}else {
				entrada=false;

			}
			
			
// Si el horario no se ha pegado en las columnas
			if ((entradaHorario==null) || (entradaHorario.isEmpty()) || (entradaHorario.equals("")) || (entradaHorario.equals("JUSTIFICADO"))
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandEntradaHorario=true;
			}else {
				bandEntradaHorario=false;

			}
			
			
	
			
			//Si hay salida y entrada 
			if (entrada==false && bandEntradaHorario==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
				
				//Si hora de timbre es menor a la hora de entrada
				//if (entradaTimbre.compareTo(entradaHorario)<=0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
					//}	
				/*if (entradaTimbre.compareTo(entradaHorario)>0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorario);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbre);
        		}*/
        		
        		//Fin de tipo horas
	          // System.out.println("impresion de empleado :"+empleado+" horaEntrada  :"+fechaIniAlmBio+"    HoraTimbre: "+fechaFinAlmBio);
		    	tiempoTrabajoXDia = obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaEntradaTimbrado",""+tiempoTrabajoXDia);

		    				}
			else {
				
			}
		}
		
	}
	
	
	
	//DIFERENCIA ENTRE SALIDA HORARIO Y SALIDA TIMBRE

	public void tres(){
		boolean salida=false,bandSalidaHorario=false;

		/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
		*/
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();
		String entradaHorario="",entradaTimbre="",salidaHorario="",salidaTimbre="",fechaBiometrico="";
		//double tiempoTrabajoXDia=0;
		String tiempoTrabajoXDia="";

		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			//entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			//entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorario=tab_tabla.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_tabla.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
		
			if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) || (salidaTimbre.equals("JUSTIFICADO"))
					|| (entradaTimbre.equals("INGRESADO")) ) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}
			
// Si el horario no se ha pegado en las columnas
			if ((salidaHorario==null) || (salidaHorario.isEmpty()) || (salidaHorario.equals("")) || (salidaHorario.equals("JUSTIFICADO")) 
					|| (entradaTimbre.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandSalidaHorario=true;
			}else {
				bandSalidaHorario=false;

			}
			
			
			//Si hay salida y entrada 
			if (salida==false && bandSalidaHorario==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
				
			    	 
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
        		//Fin de tipo horas
	          // System.out.println("impresion de empleado :"+empleado+" horaSalida  :"+fechaIniAlmBio+"    HoraTimbre: "+fechaFinAlmBio);
		    	tiempoTrabajoXDia = obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaSalidaTimbrado",""+tiempoTrabajoXDia);

		    				}
			else {
				
			}
		}
		
	}
	
	
	
	//DIFERENCIA ENTRE SALIDA DEL ALMUERZO Y SALIDA ALMUERZO TIMBRE
	public void cuatro(){
		boolean salidaAlm=false,bandSalidaHorarioAlm=false;

		/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
				+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
				+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
		*/
		 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
		 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


		String entradaHorario="",entradaTimbre="",salidaHorarioAlm="",salidaTimbreAlm="",fechaBiometrico="";
//		double tiempoTrabajoXDia=0;
		String tiempoTrabajoXDia="";

		/*for (int i = 0; i < tab_marcaciones.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_marcaciones.getValor(i, "IDE_GTEMP"));
			entradaHorario=tab_marcaciones.getValor(i, "HORAINICIOHORARIO_COBMR");
			entradaTimbre=tab_marcaciones.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorario=tab_marcaciones.getValor(i, "HORAFINHORARIO_COBMR");
			salidaTimbre=tab_marcaciones.getValor(i, "HORAFINBIOMETRICO_COBMR");
			fechaBiometrico=tab_marcaciones.getValor(i, "FECHA_EVENTO_COBMR");*/
		
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
			//entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
			//entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
			salidaHorarioAlm=tab_tabla.getValor(i, "HORAINICIOALM_COBMR");
			salidaTimbreAlm=tab_tabla.getValor(i, "HORAINICIOALMBIO_COBMR");
			fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
			
	
		
			if ((salidaTimbreAlm==null) || (salidaTimbreAlm.isEmpty()) || (salidaTimbreAlm.equals("")) || (salidaTimbreAlm.equals("S/A")) || (salidaTimbreAlm.equals("JUSTIFICADO"))
					
					|| (salidaTimbreAlm.equals("INGRESADO"))	) {
				//Si la salida no se encuentra marcada 
				salidaAlm=true;
			}else {
				salidaAlm=false;
			}
			
// Si el horario no se ha pegado en las columnas
			if ((salidaHorarioAlm==null) || (salidaHorarioAlm.isEmpty()) || (salidaHorarioAlm.equals("")) || (salidaHorarioAlm.equals("S/A")) || (salidaHorarioAlm.equals("JUSTIFICADO")) 
					|| (salidaTimbreAlm.equals("INGRESADO"))) {
				//si entrada vacia o sin datos
				bandSalidaHorarioAlm=true;
			}else {
				bandSalidaHorarioAlm=false;

			}
			
		
		
		
			/*if ((salidaTimbre==null) || (salidaTimbre.isEmpty()) || (salidaTimbre.equals("")) ) {
				//Si la salida no se encuentra marcada 
				salida=true;
			}else {
				salida=false;
			}*/
		
			String[] parts = salidaTimbreAlm.split(" ");
			String part1 = parts[0]; // 123
			
			if (part1.equals("JUSTIFICADO")) {
			}else if (salidaTimbreAlm.equals("INGRESADO")) {
				System.out.println();	
				}
				
			else{

			//Si hay salida y entrada 
			if (salidaAlm==false && bandSalidaHorarioAlm==false) {
				Date fechaIniAlmBio=null;
				Date fechaFinAlmBio=null;
		
				//Si hora de timbre es menor a la hora de entrada
				//if (salidaTimbreAlm.compareTo(salidaHorarioAlm)<=0) {
					// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
			    	// fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
			    	 
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorarioAlm);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbreAlm);
					//}	
				/*if (salidaTimbreAlm.compareTo(salidaHorarioAlm)>0) {
					 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorarioAlm);
			    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbreAlm);
        		}*/
        		//Fin de tipo horas
	        //   System.out.println("impresion de empleado :"+empleado+" horaSalidaAlm  :"+fechaIniAlmBio+"    HoraTimbreAlm: "+fechaFinAlmBio);
		    	tiempoTrabajoXDia = obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio);
		    	//listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
		    	listaEmpleado.add(empleado);
				tab_tabla.setValor(i,"diferenciaSalidaAlm",""+tiempoTrabajoXDia);

		    				}
			else {
				
			}
		}
			
		}
		
	}
	
	
	
	//DIFERENCIA ENTRE ENTRADA DEL ALMUERZO Y ENTRADA ALMUERZO TIMBRE
		public void quinto(){
			boolean entradaAlm=false,bandEntradaHorarioAlm=false;

			/*TablaGenerica tab_marcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen "
					+ "where fecha_evento_cobmr  between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' "
					+ "order by ide_gtemp asc, fecha_evento_cobmr asc");
			*/
			 ArrayList<Integer> listaEmpleado = new ArrayList<Integer>();
			 ArrayList<Double> listaEmpleadoHorasTrabajadas = new ArrayList<Double>();


			String entradaHorarioAlm="",entradaTimbreAlm="",fechaBiometrico="";
			//double tiempoTrabajoXDia=0;
			String tiempoTrabajoXDia="";

			/*for (int i = 0; i < tab_marcaciones.getTotalFilas(); i++) {
				int empleado= Integer.parseInt(tab_marcaciones.getValor(i, "IDE_GTEMP"));
				entradaHorario=tab_marcaciones.getValor(i, "HORAINICIOHORARIO_COBMR");
				entradaTimbre=tab_marcaciones.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
				salidaHorario=tab_marcaciones.getValor(i, "HORAFINHORARIO_COBMR");
				salidaTimbre=tab_marcaciones.getValor(i, "HORAFINBIOMETRICO_COBMR");
				fechaBiometrico=tab_marcaciones.getValor(i, "FECHA_EVENTO_COBMR");*/

			for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
				int empleado= Integer.parseInt(tab_tabla.getValor(i, "ide_gtemp"));
				//entradaHorario=tab_tabla.getValor(i, "HORAINICIOHORARIO_COBMR");
				//entradaTimbre=tab_tabla.getValor(i, "HORAINICIOBIOMETRICO_COBMR");
				entradaHorarioAlm=tab_tabla.getValor(i, "HORAFINALM_COBMR");
				entradaTimbreAlm=tab_tabla.getValor(i, "HORAFINALMBIO_COBMR");
				fechaBiometrico=tab_tabla.getValor(i, "FECHA_EVENTO_COBMR");
				
		
			
				if ((entradaTimbreAlm==null) || (entradaTimbreAlm.isEmpty()) || (entradaTimbreAlm.equals("")) || (entradaTimbreAlm.equals("S/A")) 
						|| (entradaTimbreAlm.equals("INGRESADO"))	)  {
					//Si la salida no se encuentra marcada 
					entradaAlm=true;
				}else {
					entradaAlm=false;
				}
				
	// Si el horario no se ha pegado en las columnas
				if ((entradaHorarioAlm==null) || (entradaHorarioAlm.isEmpty()) || (entradaHorarioAlm.equals("")) || (entradaHorarioAlm.equals("S/A")) 
						|| (entradaTimbreAlm.equals("INGRESADO"))) {
					//si entrada vacia o sin datos
					bandEntradaHorarioAlm=true;
				}else {
					bandEntradaHorarioAlm=false;

				}
				

				
				String[] parts = entradaTimbreAlm.split(" ");
				String part1 = parts[0]; // 123
				
				if (part1.equals("JUSTIFICADO")) {
				}else if (entradaHorarioAlm.equals("INGRESADO")) {
					System.out.println();	
					
				}else{
				//Si hay salida y entrada 
				if (entradaAlm==false && bandEntradaHorarioAlm==false) {
					Date fechaIniAlmBio=null;
					Date fechaFinAlmBio=null;
					

					
					
					//Si hora de timbre es menor a la hora de entrada
					//if (entradaTimbreAlm.compareTo(entradaHorarioAlm)<=0) {
						// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaTimbre);
				    	// fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+salidaHorario);
				    	 
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
						//}	
					/*if (entradaTimbreAlm.compareTo(entradaHorarioAlm)>0) {
						// fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
				    	 //fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
						 fechaIniAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaTimbreAlm);
				    	 fechaFinAlmBio = getFechaAsyyyyMMddHHmmss(fechaBiometrico+" "+entradaHorarioAlm);
					
					}*/
					
					
					//Fin de tipo horas
		       //    System.out.println("impresion de empleado :"+empleado+" horaEntradaAlm  :"+fechaIniAlmBio+"    HoraTimbreEntradaAlm: "+fechaFinAlmBio);
			    	tiempoTrabajoXDia = obtenerDiferenciaMinutos1(fechaIniAlmBio, fechaFinAlmBio);
			    //	listaEmpleadoHorasTrabajadas.add(tiempoTrabajoXDia);
			    	listaEmpleado.add(empleado);
					tab_tabla.setValor(i,"diferenciaEntradaAlm",""+tiempoTrabajoXDia);
		

			    				}
				else {
		
	}
			}
			}
			}
	
	
	
	public void buscarPersonalMensualOperativo(){
		matriz=false;
		operativo=true;
		tab_tabla.setSql("SELECT res.ide_cobmr,documento_identidad_gtemp, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horainicioband_cobmr, "
				+ "res.horainicioalm_cobmr, "
				+ "res.horafinalm_cobmr, "
				+ "res.horainicioalmbio_cobmr, "
				+ "res.horafinalmbio_cobmr, "
				+ "res.tiempoalm_cobmr, "
				+ "res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
			tab_tabla.ejecutarSql();
			
			
			tab_Coordinadores.setSql("SELECT epar.ide_geedp,CAF.DETALLE_GECAF, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
					+ "LEFT JOIN gen_cargo_funcional CAF ON  CAF.IDE_GECAF =EPAR.IDE_GECAF "
					+ "where epar.activo_geedp=true and emp.activo_gtemp=true "
					+ "and EPAR.IDE_GEEDP IN(487,444,618,667,823,838,746,1024,581) "
					//+ "AND CAF.IDE_GECAF IN (11,2,18,13) AND EPAR.IDE_GEEDP IN(487,444,618,667,823,838,746,1101,1024,581) "
					//+ "and sucu.ide_sucu !=1 "
					+ "ORDER BY AREA.DETALLE_GEARE asc");
			
			
			
					tab_Coordinadores.ejecutarSql();

			
			utilitario.addUpdate("tab_tabla,tab_Coordinadores");
	}

	public String  getConsultaSucursal(){
	String sql="SELECT res.ide_cobmr,EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP, "
			+ "res.dia_cobmr, "
			+ "res.fecha_evento_cobmr, "
			+ "res.horainiciohorario_cobmr, "
			+ "res.horainiciobiometrico_cobmr, "
			+ "res.horainicioband_cobmr, "
			+ "res.horainicioalm_cobmr, "
			+ "res.horafinalm_cobmr, "
			+ "res.horainicioalmbio_cobmr, "
			+ "res.horafinalmbio_cobmr, "
			+ "res.tiempoalm_cobmr, "
			+ "res.tiempohoralm_cobmr, "
			+ "res.horafinhorario_cobmr, "
			+ "res.horafinbiometrico_cobmr, "
			+ "res.horafinband_cobmr, "
			+ "GTT.DETALLE_GTTEM, "
			+ "'' as diferenciaEntrada, "
			+ "'' as diferenciaEntradaTimbrado, "

			+ "'' as diferenciaSalidaTimbrado, "
			+ "'' as diferenciaSalidaAlm, "
			+ "'' as diferenciaEntradaAlm "
			
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
			+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
			+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM "

			+ "where SUCU.IDE_SUCU=-1 and epar.activo_geedp=true "
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
	
	
	return sql;
	}


	public String  getCoordinadores(){
		
		String sql="SELECT epar.ide_geedp,CAF.DETALLE_GECAF, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,DEPA.DETALLE_GEDEP "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "LEFT JOIN gen_cargo_funcional CAF ON  CAF.IDE_GECAF =EPAR.IDE_GECAF "
				+ "where epar.activo_geedp=true and emp.activo_gtemp=true "
				+ "AND CAF.IDE_GECAF IN (11,2,18) "
				+ "and sucu.ide_sucu =-1 "
				+ "ORDER BY AREA.DETALLE_GEARE asc";
		
		
		return sql;
	}
	
	
	
	public void  retornaAsignacionEmpleado(){
		
		TablaGenerica  tabAsignacion=utilitario.consultar("select ide_cobmr,ide_gtemp,fecha_evento_cobmr from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
		
		if (tabAsignacion.getTotalFilas()>0) {
			
			String sql="SELECT *  FROM asi_horario_mes_empleado WHERE ide_gtemp="+tabAsignacion.getValor("IDE_GTEMP")+" AND IDE_GEMES=9 AND IDE_GEANI=10";
			tab_calendario.ejecutarSql();
			utilitario.addUpdate("tab_calendarios");
		}
		
		
	}
	
	
	
	public String   getAsignacionEmpleado(){
		String sql="SELECT ide_astur,descripcion_astur FROM asi_turnos WHERE ide_astur=-1";
		
		
		return sql;
	}
	
	public Tabla getTab_Coordinadores() {
		return tab_Coordinadores;
	}



	public void setTab_Coordinadores(Tabla tab_Coordinadores) {
		this.tab_Coordinadores = tab_Coordinadores;
	}



	public Tabla getTab_calendario() {
		return tab_calendario;
	}



	public void setTab_calendario(Tabla tab_calendario) {
		this.tab_calendario = tab_calendario;
	}

	
	public void   getHorarioEmpleado(SelectEvent evt){
		tab_tabla.seleccionarFila(evt);
		
		TablaGenerica tabEmpleado =utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());

		TablaGenerica tabDia=utilitario.consultar("SELECT ide_ashme, ide_gtemp,dia"+utilitario.getDia(tabEmpleado.getValor("fecha_evento_cobmr"))+" FROM asi_horario_mes_empleado WHERE ide_gtemp="+tabEmpleado.getValor("IDE_GTEMP"));
		String ide_astur=tabDia.getValor("DIA"+utilitario.getDia(tabEmpleado.getValor("fecha_evento_cobmr")));
		
		String sql="SELECT ide_astur, descripcion_astur FROM asi_turnos WHERE ide_astur="+ide_astur;
		tab_calendario.setSql(sql);
		tab_calendario.ejecutarSql();
		utilitario.addUpdate("tab_calendario");
	}
	
	
	public void buscarSinEntrada(){
		
		if (matriz) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true  "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horainicioband_cobmr='' and  res.horafinband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823)  "
					+ "ORDER BY DEPA.DETALLE_GEDEP asc");
				tab_tabla.ejecutarSql();
		}else {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horainicioband_cobmr='' and  res.horafinband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823) "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
					tab_tabla.ejecutarSql();
				
		}
		
	}
	
	
	public void buscarSinSalida(){
		
		if (matriz) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true  "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr='' and  res.horainicioband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823,310,311,101) "
					+ "ORDER BY DEPA.DETALLE_GEDEP asc");
				tab_tabla.ejecutarSql();
		}else {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horafinband_cobmr ='' and  res.horainicioband_cobmr not like '%SIN%' and epar.ide_geedp not in(487,823,310,311,101) "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
					tab_tabla.ejecutarSql();
				
		}
		
	}
	

	
	public void buscarCambioHorario(){
		
		if (matriz==true) {
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU=1 and epar.activo_geedp=true   "
					+ "AND RES.FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr like'%SIN%' AND res.horainicioband_cobmr like'%SIN%'  and epar.ide_geedp not in(487,823,310,311,101) "
					+ "GROUP BY  "
					+ "res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "EMP.APELLIDO_PATERNO_GTEMP, "
					+ "EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr,  "
					+ "res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr,  "
					+ "res.horainicioband_cobmr,  "
					+ "res.horainicioalm_cobmr,  "
					+ "res.horafinalm_cobmr,  "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr,  "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
				tab_tabla.ejecutarSql();
		}
		
		
		if (operativo==true) {
		/*	tab_tabla.setSql("SELECT res.ide_cobmr, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU not in(1) and epar.activo_geedp=true "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
					+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");*/
			tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where SUCU.IDE_SUCU!=1 and epar.activo_geedp=true   "
					+ "AND RES.FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  "
					+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' and epar.ide_geedp not in(487,823,310,311,101)  "
					+ "GROUP BY  "
					+ "res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
					+ "EMP.APELLIDO_PATERNO_GTEMP, "
					+ "EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr,  "
					+ "res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr,  "
					+ "res.horainicioband_cobmr,  "
					+ "res.horainicioalm_cobmr,  "
					+ "res.horafinalm_cobmr,  "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr,  "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
		
					tab_tabla.ejecutarSql();
				
		}		
	
	}
	
	
	
	public void actualizarJefe(SelectEvent evt){
		tab_Coordinadores.seleccionarFila(evt);

		tab_Coordinadores.getValorSeleccionado();
		
		tab_tabla.setSql("SELECT res.ide_cobmr,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "res.dia_cobmr, "
				+ "res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, "
				+ "res.horainiciobiometrico_cobmr, "
				+ "res.horainicioband_cobmr, "
				+ "res.horainicioalm_cobmr, "
				+ "res.horafinalm_cobmr, "
				+ "res.horainicioalmbio_cobmr, "
				+ "res.horafinalmbio_cobmr, "
				+ "res.tiempoalm_cobmr, "
				+ "res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr "

				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "where epar.activo_geedp=true "
				+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
				+ "and res.ide_geedp="+tab_Coordinadores.getValorSeleccionado()+ "  " 
				//+ "and res.horafinband_cobmr like'%SIN%' OR res.horainicioband_cobmr like'%SIN%' "
				+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
				tab_tabla.ejecutarSql();
			
		
		
		
	}
	
	
	
	public void seleccionArea(){
	set_area.dibujar();


	//utilitario.addUpdate("set_area");
		
	}
	
	

	
	public void getArea(){
		  area=set_area.getValorSeleccionado();
		 set_area.cerrar();
	
		
		  set_areaDepartamento.setSeleccionTabla("SELECT EPAR.IDE_GEEDP,   "
		  		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||    "
		  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
		  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
		  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
		  		+ "DEPA.DETALLE_GEDEP,gecaf.detalle_gecaf "
		  		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
		  		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
		  		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
		  		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
		  		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
		  		+ "left join gen_cargo_funcional gecaf on gecaf.ide_gecaf=epar.ide_gecaf  "
		  		+ "where epar.ide_geare="+area+" and epar.activo_geedp=true "
		  		+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ","IDE_GEEDP");
		
		
		
		set_areaDepartamento.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("NOMBRES");
		set_areaDepartamento.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		
		set_areaDepartamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setNombreVisual("DEPARTAMENTO");
		set_areaDepartamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
		
		set_areaDepartamento.getTab_seleccion().getColumna("detalle_gecaf").setNombreVisual("CARGO");
		set_areaDepartamento.getTab_seleccion().getColumna("detalle_gecaf").setFiltro(true);
		
		
		//set_area.setRadio();
		set_areaDepartamento.setTitle("Seleccione Empleados");
		gru_pantalla.getChildren().add(set_areaDepartamento);
		set_areaDepartamento.getBot_aceptar().setMetodo("getEmpleadoDepartamento");
    	set_areaDepartamento.redibujar();
	   	utilitario.addUpdate("set_areaDepartamento,set_area");

    	

	}

	
	public void  getEmpleadoDepartamento(){
    	set_areaDepartamento.cerrar();

		
		  String empleados=set_areaDepartamento.getSeleccionados();
		  
		  tab_tabla.setSql("SELECT res.ide_cobmr, documento_identidad_gtemp,"
				  	+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
					+ "DEPA.DETALLE_GEDEP, "
					+ "res.dia_cobmr, "
					+ "res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, "
					+ "res.horainiciobiometrico_cobmr, "
					+ "res.horainicioband_cobmr, "
					+ "res.horainicioalm_cobmr, "
					+ "res.horafinalm_cobmr, "
					+ "res.horainicioalmbio_cobmr, "
					+ "res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, "
					+ "res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, "
					+ "res.horafinband_cobmr "
					
					+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR   "
					+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
					+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where epar.activo_geedp=true and "
					+ "epar.ide_geedp in("+set_areaDepartamento.getSeleccionados()+") "
					+ "AND FECHA_EVENTO_COBMR BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
 
					+ "ORDER BY AREA.DETALLE_GEARE asc,res.fecha_evento_cobmr asc");
		  tab_tabla.ejecutarSql();
			set_areaDepartamento.cerrar();
			utilitario.addUpdate("tab_tabla,set_areaDepartamento");
			
			if (tab_tabla.getTotalFilas()<=0) {
				utilitario.agregarMensaje("No existen datos para la fecha selecciona", "Fecha inicio: "+cal_fecha_inicial.getFecha()+" y Fecha Fin: "+cal_fecha_final.getFecha());
				return;
			}


	}


	public SeleccionTabla getSet_area() {
		return set_area;
	}



	public void setSet_area(SeleccionTabla set_area) {
		this.set_area = set_area;
	}



	public SeleccionTabla getSet_areaDepartamento() {
		return set_areaDepartamento;
	}



	public void setSet_areaDepartamento(SeleccionTabla set_areaDepartamento) {
		this.set_areaDepartamento = set_areaDepartamento;
	}



	public void setArea(String area) {
		this.area = area;
	}
	
	
	
	public Tabla getTab_sql() {
		return tab_sql;
	}



	public void setTab_sql(Tabla tab_sql) {
		this.tab_sql = tab_sql;
	}
	
	
	
}

