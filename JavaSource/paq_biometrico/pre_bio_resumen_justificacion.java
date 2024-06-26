/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.tabview.Tab;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import pckUtilidades.Utilitario;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_bio_resumen_justificacion extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_permisos = new Tabla();
    
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
    private SeleccionTabla set_empleado= new SeleccionTabla();
    private SeleccionTabla set_departamento = new SeleccionTabla();
    @EJB
    private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private AutoCompletar aut_empleados=new AutoCompletar();
	TablaGenerica  obtenerTurnoEmpleado;
private boolean justificado=false,novedad=false;
    
    public pre_bio_resumen_justificacion() {
    	
    	
    	bar_botones.agregarComponente(new Etiqueta("BÚSQUEDA DE NOVEDADES"));
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
    	bot_rep_biometrico.setValue("Importar Registro Manual");
    	bot_rep_biometrico.setTitle("Importar Registro Manual");
    	bar_botones.agregarBoton(bot_rep_biometrico);
    	

    	
    	Boton bot_empleado_biometrico= new Boton();
    	bot_empleado_biometrico.setIcon("ui-icon-calculator");
    	bot_empleado_biometrico.setMetodo("seleccionarJustificado");
    	bot_empleado_biometrico.setValue("Modificar Biométrico");
    	bot_empleado_biometrico.setTitle("Reporte Justificado");
    //	bar_botones.agregarBoton(bot_empleado_biometrico);
    	
    	
    	    	
    	
   /* 	
    	Boton bot_departamento_biometrico= new Boton();
    	bot_departamento_biometrico.setIcon("ui-icon-calculator");
    	bot_departamento_biometrico.setMetodo("seleccionarDepartamento");
    	bot_departamento_biometrico.setValue("Ver Novedades");
    	bot_departamento_biometrico.setTitle("Reporte Novedades");
    	bar_botones.agregarBoton(bot_departamento_biometrico);
*/
    	
    	
	bar_botones.agregarSeparador();
		
	/*	aut_empleados.setId("aut_empleados");
		aut_empleados.setAutoCompletar("SELECT emp.ide_gtemp,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM  GTH_EMPLEADO EMP " +
				"WHERE EMP.ACTIVO_GTEMP=true");
		aut_empleados.setMetodoChange("seleccionoEmpleado");
		
		bar_botones.agregarComponente(aut_empleados);
	*/
    	
    	//Boton Justificar
    	Boton bot_justificar= new Boton();
    	bot_justificar.setIcon("ui-icon-calculator");
    	bot_justificar.setMetodo("justificarTimbradas");
    	bot_justificar.setValue("Justificar");
    	bot_justificar.setTitle("Justificación Masiva");
    	//bar_botones.agregarBoton(bot_justificar);
    	
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

    	
    	
    	set_departamento.setId("set_departamento");
    	
    	
    	
    	
    	
    	
			tab_tabla.setId("tab_tabla");
			tab_tabla.setTabla("con_biometrico_marcaciones_resumen", "ide_cobmr", 1);
			tab_tabla.setCampoPrimaria("ide_cobmr");
			tab_tabla.setNumeroTabla(1);
			tab_tabla.getColumna("ide_cobmr").setLongitud(5);
			tab_tabla.getColumna("ide_cobmr").setOrden(1);


			tab_tabla.getColumna("aprueba_hora_extra_cobmr").setVisible(false);
			tab_tabla.getColumna("aprueba_hora_extra_cobmr").setOrden(23);

			
			
			
			tab_tabla.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
					"FROM GTH_EMPLEADO EMP");
			tab_tabla.getColumna("ide_gtemp").setAutoCompletar();
			tab_tabla.getColumna("IDE_GTEMP").setLongitud(28);
	        tab_tabla.getColumna("IDE_GTEMP").alinearCentro();
	        tab_tabla.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO");
			tab_tabla.getColumna("IDE_GTEMP").setOrden(2);
			//tab_tabla.getColumna("IDE_GTEMP").setFiltro(true);


	        tab_tabla.getColumna("dia_cobmr").setNombreVisual("DÍA");
	        tab_tabla.getColumna("dia_cobmr").alinearCentro();
			tab_tabla.getColumna("dia_cobmr").setLongitud(15);
			tab_tabla.getColumna("dia_cobmr").setOrden(3);

	        
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
			tab_tabla.getColumna("FECHA_EVENTO_COBMR").setOrden(4);
	        tab_tabla.getColumna("FECHA_EVENTO_COBMR").alinearCentro();

	        
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setNombreVisual("ENTRADA");
			tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setOrden(5);

	     //   tab_tabla.getColumna("HORAINICIOHORARIO_COBMR").setVisible(false);
	        

	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setNombreVisual("TIMBRE ENTRADA");
			tab_tabla.getColumna("HORAINICIOBIOMETRICO_COBMR").setOrden(6);

	        
	        
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setLongitud(15);
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOBAND_COBMR").setNombreVisual("ESTADO ENTRADA");
			tab_tabla.getColumna("HORAINICIOBAND_COBMR").setOrden(7);
			//tab_tabla.getColumna("HORAINICIOBAND_COBMR").setFiltro(true);

	        
	        
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOALM_COBMR").setNombreVisual("H.INI.ALM");
	       // tab_tabla.getColumna("HORAINICIOALM_COBMR").setVisible(false);
			tab_tabla.getColumna("HORAINICIOALM_COBMR").setOrden(8);

	             

	        tab_tabla.getColumna("HORAFINALM_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAFINALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINALM_COBMR").setNombreVisual("H.FIN.ALM");
	        //tab_tabla.getColumna("HORAFINALM_COBMR").setVisible(false);
			tab_tabla.getColumna("HORAFINALM_COBMR").setOrden(9);

	        
	        
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setNombreVisual("TIM.INI.ALM");  
			tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setOrden(10);
			//tab_tabla.getColumna("HORAINICIOALMBIO_COBMR").setFiltro(true);

			
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINALMBIO_COBMR").setNombreVisual("TIM.FIN.ALM");  
			tab_tabla.getColumna("HORAFINALMBIO_COBMR").setOrden(11);
			//tab_tabla.getColumna("HORAFINALMBIO_COBMR").setFiltro(true);

			
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("TIEMPOALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("TIEMPOALM_COBMR").setNombreVisual("TIM.TOMA.ALM"); 
			tab_tabla.getColumna("TIEMPOALM_COBMR").setOrden(12);

	        
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setLongitud(15);
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").alinearCentro();
	        tab_tabla.getColumna("TIEMPOHORALM_COBMR").setNombreVisual("TIEMPOHORALM"); 
			tab_tabla.getColumna("TIEMPOHORALM_COBMR").setOrden(13);

	                
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINHORARIO_COBMR").setNombreVisual("HOR.SAL");  
	        //tab_tabla.getColumna("HORAFINHORARIO_COBMR").setVisible(false);  
			tab_tabla.getColumna("HORAFINHORARIO_COBMR").setOrden(14);
	        
	        
	        
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setLongitud(20);
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setNombreVisual("TIM.HOR.SAL");  
			tab_tabla.getColumna("HORAFINBIOMETRICO_COBMR").setOrden(15);

	        
	        tab_tabla.getColumna("HORAFINBAND_COBMR").alinearCentro();
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setNombreVisual("ESTADO SALIDA");  
	        tab_tabla.getColumna("HORAFINBAND_COBMR").setLongitud(15);
			tab_tabla.getColumna("HORAFINBAND_COBMR").setOrden(16);
			tab_tabla.getColumna("HORAFINBAND_COBMR").alinearCentro();

	        
	        tab_tabla.getColumna("horafinextra_cobmr").setLongitud(20);
	        tab_tabla.getColumna("horafinextra_cobmr").alinearCentro();
	        tab_tabla.getColumna("horafinextra_cobmr").setVisible(false);
			tab_tabla.getColumna("horafinextra_cobmr").setOrden(17);

	        
	        tab_tabla.getColumna("recargonocturno25_cobmr").setLongitud(20);
	        tab_tabla.getColumna("recargonocturno25_cobmr").alinearCentro();
	        tab_tabla.getColumna("recargonocturno25_cobmr").setVisible(false);
			tab_tabla.getColumna("recargonocturno25_cobmr").setOrden(18);
			
	        
	        tab_tabla.getColumna("recargonocturno100_cobmr").setLongitud(20);
	        tab_tabla.getColumna("recargonocturno100_cobmr").alinearCentro();
	        tab_tabla.getColumna("recargonocturno100_cobmr").setVisible(false);
			tab_tabla.getColumna("recargonocturno100_cobmr").setOrden(19);
	        
	        
	        tab_tabla.getColumna("recargonocturno100_cobmr").setLongitud(20);
	        tab_tabla.getColumna("recargonocturno100_cobmr").alinearCentro();
	        tab_tabla.getColumna("recargonocturno100_cobmr").setVisible(false);
			tab_tabla.getColumna("recargonocturno100_cobmr").setOrden(20);
	        
	       // tab_tabla.getColumna("aprobacio_horas_extra_cobmr").setLongitud(25);
	       // tab_tabla.getColumna("aprobacio_horas_extra_cobmr").alinearCentro();
	        //tab_tabla.getColumna("aprobacio_horas_extra_cobmr").setVisible(false);
	        
			tab_tabla.getColumna("p_entrada_cobmr").setVisible(false);;
			tab_tabla.getColumna("p_salida_cobmr").setVisible(false);;
			tab_tabla.getColumna("p_alm_cobmr").setVisible(false);;

			
			
			tab_tabla.getColumna("novedad_cobmr").setOrden(20);
			tab_tabla.getColumna("novedad_cobmr").setLongitud(21);
		    tab_tabla.getColumna("novedad_cobmr").alinearCentro();
		    tab_tabla.getColumna("novedad_cobmr").setNombreVisual("ACTUALIZAR MARCACIÓN");  
			
			
			 tab_tabla.getColumna("inconsistencia_biometrico_cobmr").setLongitud(21);
		        tab_tabla.getColumna("inconsistencia_biometrico_cobmr").alinearCentro();
				tab_tabla.getColumna("inconsistencia_biometrico_cobmr").setOrden(22);
				 tab_tabla.getColumna("inconsistencia_biometrico_cobmr").setVisible(false);
 
				
				tab_tabla.getColumna("ide_aspvh").setLongitud(20);
			        tab_tabla.getColumna("ide_aspvh").alinearCentro();
					tab_tabla.getColumna("ide_aspvh").setOrden(22);
					 tab_tabla.getColumna("ide_aspvh").setVisible(false);
	        
	        
	        
			tab_tabla.setCondicion("ide_cobmr=-1");
			tab_tabla.setRows(25);
			//tab_tabla.setLectura(true);
			//tab_tabla.setRecuperarLectura(true);

			//tab_tabla.setTipoSeleccion(true);

			tab_tabla.dibujar();
	        tab_tabla.setHeader("REPORTE RESUMEN NOVEDADES JUSTIFICACIÓN BIOMÉTRICO");

			PanelTabla pat_panel = new PanelTabla();
			pat_panel.setPanelTabla(tab_tabla);
			pat_panel.getMenuTabla().getItem_insertar().setRendered(true);			
			pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
			
			ItemMenu itm_modificar=new ItemMenu();
			itm_modificar.setMetodo("modificarMarcacion");
			itm_modificar.setValue("Modificar");
			//pat_panel.getMenuTabla().getChildren().add(itm_modificar);
        
			
	


        Division div_div1 = new Division();
        div_div1.dividir1(pat_panel);        
        agregarComponente(div_div1);

        
        
    }

    
    
    public void justificarTimbradas(){
    	
		
    	try {
    		//System.out.println("seleccionado : "+tab_tabla.getValorSeleccionado());
    		
    		
    		tab_tabla.setLectura(false);

		    int banderaJustificaEntrada=0;
		    String justificacionEntrada="";
		    String justificacionAlmuerzoSalida="";
		    String justificacionAlmuerzoEntrada="";
		    String justificacionSalida="";
		   // int banderaEntrada=0;
		    //int banderaSalida=0;

/*		    if (aut_empleados.getValor()==null) {
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

	*/	    
		    
    		//Si hay datos en la pantalla las recorro uno a uno por empleado
		    
		      
		    for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
		    	
		   // TablaGenerica  tabMarcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
		    
		    //Obtengo los datos de cada fila que son entrada ,salida, salida almuerzo, entrada salida para validar
		    	//Variables de inicio
		    String ide_cobmr=tab_tabla.getValor(i,"IDE_COBMR");
		    int empleado= Integer.parseInt(tab_tabla.getValor(i,"IDE_GTEMP"));
	        String fecha=tab_tabla.getValor(i,"FECHA_EVENTO_COBMR");
		    String horaEntrada=tab_tabla.getValor(i,"HORAINICIOBIOMETRICO_COBMR");
		    String banderaEntrada=tab_tabla.getValor(i,"HORAINICIOBAND_COBMR");
		    String almuerzoSalidaTimbre =tab_tabla.getValor(i,"HORAINICIOALMBIO_COBMR");
		    String almuerzoEntradaTimbre=tab_tabla.getValor(i,"HORAFINALMBIO_COBMR");
		    String almuerzoSalida=tab_tabla.getValor(i,"horainicioalm_cobmr");
		    String almuerzoEntrada=tab_tabla.getValor(i,"horafinalm_cobmr");
		    String tiempoAlmuerzo =tab_tabla.getValor(i,"TIEMPOALM_COBMR");
		    //tiempo de almuerzo
		    String tiempohoralm=tab_tabla.getValor(i,"TIEMPOHORALM_COBMR");
		    
		    String banderaSalida=tab_tabla.getValor(i,"HORAFINBAND_COBMR");
		    String horaFin=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");

		 		    
		    String horaSalida=tab_tabla.getValor(i,"HORAFINBIOMETRICO_COBMR");
	
		    
		    String horarioSalida=tab_tabla.getValor(i,"HORAFINHORARIO_COBMR");
	        String horarioEntrada=tab_tabla.getValor(i,"HORAINICIOHORARIO_COBMR");
	        
	        

		    TablaGenerica obtenerPermisoXEmpleadoJustificacion;
		    String  permiso;
		    String  nro_horas;
		    boolean banderaEntradaIngreso=false,banderaSalidaIngreso=false,banderaAlmuerzo=false;
		    int valorTipo=0;
    		
    		//Bandera de Entrada
		    if (banderaEntrada=="" || banderaEntrada.isEmpty()) {
    			//Si la bandera se encuentra vacia
		    	if (horarioEntrada.equals("") || horarioEntrada.equals("")) {
    				//Busco si contiene horario asignado
		    		banderaEntradaIngreso=false;
				}else {
					//Si tiene horario
					banderaEntradaIngreso=true;
					horaEntrada=horarioEntrada;
				}
    		}
    			

		    
		    if (banderaSalida.equals("") || banderaSalida.isEmpty()) {
        			if (horarioSalida.equals("") || horarioSalida.isEmpty()) {
            				banderaSalidaIngreso=false;
        				}else {
        					banderaSalidaIngreso=true;
        					horaSalida=horarioSalida;
        				}
				
    			}
		    
		    
		    double tiempoJustificadoAlm=0.0;
		    
    		//Reviso si tengo almuerzo para lo cual tengo tres casos 30,60,0
    		if (tiempohoralm.equals("30") || tiempohoralm.equals("60")) {
    			//Si tengo almuerzo
    			banderaAlmuerzo=true;    	
    			if (tiempohoralm.equals("30")) {
    				tiempoJustificadoAlm=30;
				}
    			if (tiempohoralm.equals("60")) {
    				tiempoJustificadoAlm=60;
				}
    			
    		}else {
    			//No tengo almuerzo
    			banderaAlmuerzo=false;    		    		
			}


    	
    		
    		
    	if (banderaAlmuerzo==true) {
    		//Si tengo almuerzo
    		if (!almuerzoSalidaTimbre.equals("")) {
    			
    			getPermisoHorarioAlmuerzoEntrada(fecha, empleado, almuerzoSalidaTimbre, almuerzoSalida ,horarioSalida,ide_cobmr,tiempoJustificadoAlm);
			}else {
				getPermisoHorarioAlmuerzo(fecha, empleado, almuerzoSalida, horarioEntrada,horarioSalida,ide_cobmr,tiempoJustificadoAlm);

			}
		}
    		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   		
    		
    		
    	//Si tiene horario y hora de entrada	
    			if (banderaEntradaIngreso==true) {
    			getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,ide_cobmr);
    		}
    		
    			if (banderaEntrada.equals("SIN TIMBRE")) {
	    			getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,ide_cobmr);		
        		}
    		
    		
	if (banderaEntrada.equals("ATRASADO")) {
    	getPermisoHorario(fecha, empleado, horaEntrada, horarioEntrada,horarioSalida,ide_cobmr);		
    		}
	
			if (banderaSalidaIngreso==true) {
   			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);
			}
			
		
    		if (banderaSalida.equals("ANTICIPADO")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		    			
			}
    		
    		
    		if (banderaSalida.equals("SIN TIMBRE")) {
    			getPermisoHorarioSalida(fecha, empleado, horaSalida, horarioEntrada,horarioSalida,ide_cobmr);		
    		}
    		
    		    		
    		    		


  
		}

    		
		      tab_tabla.setCondicion("fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and  '"+cal_fecha_final.getFecha()+"'");
		      	//	tab_tabla.imprimirSql();
		      		tab_tabla.ejecutarSql();
		      		utilitario.addUpdate("tab_tabla");

		    
		    for (int j = 0; j < tab_tabla.getTotalFilas(); j++) {
				
	
		    
    		
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("JUSTIFICADO") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
	}

//	if (tab_tabla.getValor(i, "HORAINICIOBAND_COBMR").contains("OK") && tab_tabla.getValor(i, "HORAINICIOALMBIO_COBMR").contains("S/A") && tab_tabla.getValor(i, "HORAFINBAND_COBMR").contains("OK")) {
//		utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+ide_cobmr);

//	}

if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("OK") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}


if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("ATRASADO") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("OK")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}

if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("EXTRA") && tab_tabla.getValor(j, "HORAFINBAND_COBMR").contains("EXTRA")) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=true where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}

//nOVEDAD ES FALSA
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("EXTRA") && (tab_tabla.getValor(j, "HORAFINBAND_COBMR")==null || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("") || tab_tabla.getValor(j, "HORAFINBAND_COBMR").isEmpty())) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=false where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}
if (tab_tabla.getValor(j, "HORAINICIOBAND_COBMR").contains("SIN TIMBRE") && (tab_tabla.getValor(j, "HORAFINBAND_COBMR")==null || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("") || tab_tabla.getValor(j, "HORAFINBAND_COBMR").equals("SIN TIMBRE"))) {
utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set novedad_cobmr=false where ide_cobmr="+tab_tabla.getValor(j, "IDE_COBMR"));
}








			}
	    guardarPantalla();
		    
		    
		    
		  //  }

		    
    	} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}		
			
			
    }
    
    
    
    public void seleccionarReporteTotal(){
        
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
    			String fechaInicio= cal_fecha_inicial.getFecha().toString();
        		String fechaFin=cal_fecha_final.getFecha();	
        		novedad=true;
        		justificado=false;
                tab_tabla.setCondicion("inconsistencia_biometrico_cobmr in(1,2) and fecha_evento_cobmr between '"+cal_fecha_inicial.getFecha()+"' and  '"+cal_fecha_final.getFecha()+"'");
        	//	tab_tabla.imprimirSql();
        		tab_tabla.ejecutarSql();
        		utilitario.addUpdate("tab_tabla");
			}else {
				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

			}
    		
		}
		else {
			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
		}		
    
    
    }
    
   /*  public void seleccionarJustificado(){
    
    		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    			
    			
    			
    			
        		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
        			String fechaInicio= cal_fecha_inicial.getFecha();
            		String fechaFin=(cal_fecha_final.getFecha());	
            		novedad=false;
            		justificado=true;
            		tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaInicio+"' and  '"+fechaFin+"' and horainicioband_cobmr like '%JUSTIFICADO%' or  "
            				+ "horafinband_cobmr LIKE '%JUSTIFICADO%'");
            		//tab_tabla.imprimirSql();
            		tab_tabla.ejecutarSql();
            		utilitario.addUpdate("tab_tabla");
    			}else {
    				utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Fecha inicial debe ser menor a fecha final");

    			}
        		
    		}
    		else {
    			utilitario.agregarMensajeInfo("Rango de fechas no válidos",	"Debe ingresar el rango de fechas");
    		}	
       }
    */
	public void getEmpleado(){
		
	  	
		StringBuilder str_ide=new StringBuilder();
       getMarcacionesEmpleado(aut_empleados.getValor().toString(),cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha());
        set_empleado.cerrar();		
	
    			
    		
    		}
	
	
    public void seleccionarDepartamento(){
    	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
    		
    		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
    			String fechaInicio= getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(cal_fecha_inicial.getFecha().toString()),-1));
        		String fechaFin=(cal_fecha_final.getFecha());	
        		tab_tabla.setCondicion("fecha_evento_cobmr between '"+fechaInicio+"' and  '"+fechaFin+"' and horainicioband_cobmr =='' and horafinband_cobmr == '' and horafinalmbio_cobmr=='' ");
        		//tab_tabla.imprimirSql();
        		tab_tabla.ejecutarSql();
        		utilitario.addUpdate("tab_tabla");
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
    	if (aut_empleados.getValor()==null) {
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
    

    
public TablaGenerica  obtenerPermisoXEmpleadoEntrada(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
        TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
        + "from asi_permisos_vacacion_hext where "
  		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
  	    + "and hora_desde_aspvh >= '"+horaInicio+"' ");
  
 // obtenerPermisos.imprimirSql();
  return obtenerPermisos;
 	
}
 	



public TablaGenerica  obtenerPermisoXEmpleadoSalida(String FechaInicio,String horaInicio , Integer IDE_GTEMP){
    TablaGenerica obtenerPermisos= utilitario.consultar("select ide_aspvh,nro_horas_aspvh,aprobado_tthh_aspvh "
    + "from asi_permisos_vacacion_hext where "
		+ "fecha_desde_aspvh between '"+FechaInicio+"' and '"+FechaInicio+"' and ide_gtemp="+IDE_GTEMP+" "
	    + "and hora_hasta_aspvh <= '"+horaInicio+"' ");

//obtenerPermisos.imprimirSql();
return obtenerPermisos;
	
}
	







 	public void limpiar(){
 		aut_empleados.limpiar();
 		tab_tabla.limpiar();
 		utilitario.addUpdate("tab_tabla,cal_fecha_final,cal_fecha_inicial,set_departamento,set_empleado,aut_empleados");
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
		/*if (tab_permisos.isFocus()) {
		/*	String ideGrupoEmpleado=obtenerTurnoEmpleado.getValor("IDE_GTGRE").toString();
			if (ideGrupoEmpleado=="3") {
				
			}else {
				tab_permisos.getColumna("FECHA_DESDE_ASPVH").setValor("");

				//tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
				tab_permisos.getColumna("FECHA_HASTA_ASPVH").setMetodoChange("calcularDiasPermisos");
				tab_permisos.getColumna("FECHA_HASTA_ASPVH").setVisible(false);

			}
			
			
	   		
    		if (tab_tabla.getValorSeleccionado()==null || tab_tabla.getValorSeleccionado().isEmpty()) {
				utilitario.agregarMensaje("No se ha seleccionado evento", "Debe seleccionar un registro");
				}
    		
			
		    TablaGenerica  tabMarcaciones=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr="+tab_tabla.getValorSeleccionado());
			int empleado= Integer.parseInt(tabMarcaciones.getValor("IDE_GTEMP"));
		    
			
		tab_permisos.insertar();
		tab_permisos.setValor("IDE_GTEMP", ""+empleado);

		} */
    	
    	utilitario.agregarMensajeInfo("No se puede insertar", "Acción inválida");
    	
    	
    	
    }

    @Override
    public void guardar() {
            if (tab_tabla.guardar()) {
                guardarPantalla();
        		//tab_tabla.getFilas().get(tab_tabla.getFilaActual()).setLectura(true);
				utilitario.addUpdate("tab_tabla");
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
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



	public Tabla getTab_tabla() {
		return tab_tabla;
	}



	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}



	public Tabla getTab_permisos() {
		return tab_permisos;
	}



	public void setTab_permisos(Tabla tab_permisos) {
		this.tab_permisos = tab_permisos;
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
	public void actualizarMarcaciones(boolean novedad,boolean justificado){
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha())) {
			
			//String fechaInicio= getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(cal_fecha_inicial.getFecha().toString()),-1));
			if(aut_empleados.getValor()!=null){
	
			if (novedad==true) {
				tab_tabla.setCondicion("IDE_GTEMP="+aut_empleados.getValor()+" and novedad_cobmr=false and fecha_evento_cobmr BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
			}			
			
			
			if (justificado==true) {
				tab_tabla.setCondicion("IDE_GTEMP="+aut_empleados.getValor()+" and novedad_cobmr=true and fecha_evento_cobmr BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
			}	
			if (novedad==false && justificado==false){
				tab_tabla.setCondicion("fecha_evento_cobmr BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
			}
				
			tab_tabla.ejecutarSql();
		}
		}else {
			utilitario.agregarMensajeInfo("Rangos de fechas no válidos",
					"");
		}		
	}
	
	
	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
		actualizarMarcaciones(novedad,justificado);	
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
	    + "and hora_desde_aspvh >= '"+horaInicio+"' and hora_hasta_aspvh  <= '"+horaFin+"' and aprobado_tthh_aspvh=true");

    
    
    
//obtenerPermisos.imprimirSql();
return obtenerPermisos;
}


private TablaGenerica obtenerMarcacionesEmpleado(Integer ide_gtemp){
	TablaGenerica marcacionesXEmpleado=utilitario.consultar("select * from con_biometrico_marcaciones_resumen where ide_cobmr");
	return marcacionesXEmpleado;
	
}


public void calcularDiasPermisos(AjaxBehaviorEvent evt){
	
	
	try {
		tab_permisos.modificar(evt);
		
		    if (utilitario.isFechasValidas(utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")))) {
		        tab_permisos.setValor("NRO_DIAS_ASPVH", (new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)) + ""));
				double nro_horas=(new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)))*8;
				tab_permisos.setValor("NRO_HORAS_ASPVH", +nro_horas+"");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
		   		
		   
		    } else {
		    	tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
		    }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//System.out.println("eRROR EN calcularDiasPermisos ");
		}
    }




public void calcularDiasPermisos(DateSelectEvent evt){

	
	try {
		tab_permisos.modificar(evt);

		    if (utilitario.isFechasValidas(utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFormatoFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")))) {
		        tab_permisos.setValor("NRO_DIAS_ASPVH", (new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)) + ""));
				double nro_horas=(new Integer((utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.DeStringADate(tab_permisos.getValor("FECHA_HASTA_ASPVH"))) + 1)))*8;
				tab_permisos.setValor("NRO_HORAS_ASPVH", +nro_horas+"");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
		   		
		   
		    } else {
		    	tab_permisos.setValor("NRO_DIAS_ASPVH", "0");
				tab_permisos.setValor("NRO_HORAS_ASPVH", "0");
				utilitario.addUpdateTabla(tab_permisos,"NRO_DIAS_ASPVH,NRO_HORAS_ASPVH", "");
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
		    }
	} catch (Exception e) {
		// TODO Auto-generated catch block
System.out.println("eRROR EN calcularDiasPermisos ");	}
    

		
		
}



public boolean isJustificado() {
	return justificado;
}



public void setJustificado(boolean justificado) {
	this.justificado = justificado;
}



public boolean isNovedad() {
	return novedad;
}



public void setNovedad(boolean novedad) {
	this.novedad = novedad;
}







//Devuelve los valores true si tiene y false si no 
/**
* 
* @param fecha
* @param empleado
* @param horaTimbre
* @param horaHorarioEntrada
* @param horaHorarioSalida
* @param ide_cobmr
* @return
*/
private Boolean getPermisoHorarioAlmuerzoEntrada(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr, double tiempoJustificado){

	//System.out.println("Ingresa");
	
	
	String fechaInicioAsnov="";
	String fechaFinAsnov="";
	boolean valorRetorno=false;
	Calendar calFecha = Calendar.getInstance();
	Calendar calFechaInicio = Calendar.getInstance();
	Calendar calFechaFin = Calendar.getInstance();
	//tabla que contiene los permisos para justificar
	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
			+ "from asi_permisos_vacacion_hext "
			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
					
			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 4))+"' and anulado_aspvh =false");
	

	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
	
	if (tab_novedad.getTotalFilas()<0) {
		//si no tiene permisos ingresados en ese rango de fechas
		valorRetorno=false;
		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

	}else {
		
	
	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
	
		//Obtengo los dias de los permisos consultados hasta tres dias antes
		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

	
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
				//cALFECHAiNICIO ES MENOR QUE  FECHA 
				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
			//Encuentro si existe permiso para esa fecha
					//Encuentro si existe permiso para esa fecha
					
					//Obtengo el tipo de permiso si es por dias o por horas
					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
					String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
					String horaDesdeAspvh="";
					String horaHastaAspvh="";
					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
					
			
					
					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
					
			//Obtengo el tipo de permiso si es por dias o por horas

			//tipo dias justifica todo
			if (tipoPermiso.equals("4")) {
				//saco si es de tipo de permiso si es por dias 4  o horas 1
				String mensaje="";
				int banderaAprobacion=0;
				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
				
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
						+ "  "+detalleAspvh;
			
		
				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{
				
			/*	mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
						+ "  "+detalleAspvh;*/
			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='JUSTIFICADO TTHH',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
				banderaAprobacion=1;
				
				
				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='JUSTIFICADO TTHH',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);*/
				
				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true"))
					{
					/*System.out.println("empleado :"+empleado+" fecha inicio: "+fechaInicioAsnov+"  fecha fin: "+fechaFinAsnov+" ide_aspvh: "+ide_aspvh);
					mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
							+ "  "+detalleAspvh;
				*/
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
							+ "p_alm_cobmr='"+mensaje+"', horafinalmbio_cobmr='JUSTIFICADO JF.INM',tiempoalm_cobmr="+tiempoJustificado+"  where ide_cobmr="+ide_cobmr);
					banderaAprobacion=2;

									
									
				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
							+ "p_alm_cobmr='"+mensaje+"', horafinalmbio_cobmr='INGRESADO',tiempoalm_cobmr="+tiempoJustificado+"  where ide_cobmr="+ide_cobmr);
					banderaAprobacion=3;
				}
	
				tab_tabla.ejecutarSql();
				utilitario.addUpdate("tab_tabla");
				
				
				
				valorRetorno= true;
				//System.out.println("permiso ingresado por dias"+valorRetorno);
				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
					tab_tabla.ejecutarSql();
					utilitario.addUpdate("tab_tabla");
				*/
			}else {
				//permiso ingresadio por dias
				
				//Obtengo los valores 
				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
				
	
				hora_desde_aspvh =horaHorarioEntrada;
				hora_hasta_aspvh =horaHorarioSalida;
				String mensaje="";
				int estadoPermiso=0;
					
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;
				
				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{
					
				/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;*/
						estadoPermiso=1;

				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true"))
					{
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=2;

				}else 	if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
				/*	mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=3;
				}else {

				}
				
				
				if (hora_desde_aspvh.compareTo(horaTimbre)<0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
					//justifico;horaHorarioSalidahoraHorarioSalida
					valorRetorno= true;
					
					
					if (estadoPermiso==1) {
				

						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='JUSTIFICADO TTHH',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
												
					}
					if (estadoPermiso==2) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='JUSTIFICADO JF.INM',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
					}
					
					if (estadoPermiso==3) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"',horafinalmbio_cobmr='INGRESADO',tiempoalm_cobmr="+tiempoJustificado+"   where ide_cobmr="+ide_cobmr);
					}
					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
					/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
					tab_tabla.ejecutarSql();
					utilitario.addUpdate("tab_tabla");*/

				}	
				else {
					valorRetorno=false;
					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
					break;
				}
				
				}//Fin de tipo horas
			
			
			
			
			}else {
				//Si no se encuentra en esa fecha no hace nada
				valorRetorno=false;
				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
			} 
				
			

	}//Termina For
		
		
		
	}// Termina else si tiene permiso en el rango de fechas ingresado
	return valorRetorno;

	
	
}//Fin de if si cumple







//Devuelve los valores true si tiene y false si no 
/**
* 
* @param fecha
* @param empleado
* @param horaTimbre
* @param horaHorarioEntrada
* @param horaHorarioSalida
* @param ide_cobmr
* @return
*/
private Boolean getPermisoHorarioAlmuerzo(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr,double tiempoJustificado){

	
	
	
	String fechaInicioAsnov="";
	String fechaFinAsnov="";
	boolean valorRetorno=false;
	Calendar calFecha = Calendar.getInstance();
	Calendar calFechaInicio = Calendar.getInstance();
	Calendar calFechaFin = Calendar.getInstance();
	//tabla que contiene los permisos para justificar
	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
			+ "from asi_permisos_vacacion_hext "
			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
					
			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 4))+"' and anulado_aspvh =false");

	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
	
	
	if (tab_novedad.getTotalFilas()<0) {
		//si no tiene permisos ingresados en ese rango de fechas
		valorRetorno=false;
		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

	}else {
		
	
	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
	
		//Obtengo los dias de los permisos consultados hasta tres dias antes
		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

	
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
				//cALFECHAiNICIO ES MENOR QUE  FECHA 
				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
			//Encuentro si existe permiso para esa fecha
					//Obtengo el tipo de permiso si es por dias o por horas
					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
					String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
					String horaDesdeAspvh="";
					String horaHastaAspvh="";
					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
					
			
					
					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
				
	
			//tipo dias justifica todo
			if (tipoPermiso.equals("4")) {
				//saco si es de tipo de permiso si es por dias 4  o horas 1
			
				String mensaje="";
				int banderaAprobacion=0;
				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
				
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
						+ "  "+detalleAspvh;
		
		if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{
					banderaAprobacion=1;
			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "p_alm_cobmr='"+mensaje+"',horainicioalmbio_cobmr='JUSTIFICADO TTHH',horafinalmbio_cobmr='JUSTIFICADO TTHH',tiempoalm_cobmr="+tiempoJustificado+"  "
								+ "where ide_cobmr="+ide_cobmr);
						
				
				
			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){

					//System.out.println("empleado :"+empleado+" fecha inicio: "+fechaInicioAsnov+"  fecha fin: "+fechaFinAsnov+" ide_aspvh: "+ide_aspvh);
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
							+ "  "+detalleAspvh;*/
				
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
							+ "p_alm_cobmr='"+mensaje+"', horainicioalmbio_cobmr='JUSTIFICADO JF.INM',horafinalmbio_cobmr='JUSTIFICADO JF.INM',tiempoalm_cobmr="+tiempoJustificado+" "
									+ "where ide_cobmr="+ide_cobmr);
					banderaAprobacion=2;

						}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
							utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
									+ "p_alm_cobmr='"+mensaje+"', horainicioalmbio_cobmr='INGRESADO',horafinalmbio_cobmr='INGRESADO',tiempoalm_cobmr="+tiempoJustificado+" "
											+ "where ide_cobmr="+ide_cobmr);
							banderaAprobacion=3;
			
						}else {
									
				}
				
				
				
				
				valorRetorno= true;
				//System.out.println("permiso ingresado por dias"+valorRetorno);
				/*utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"',horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
					tab_tabla.ejecutarSql();
					utilitario.addUpdate("tab_tabla");*/
				
			}else {
				//permiso ingresadio por dias
				
				//Obtengo los valores 
				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
				
	
				
				hora_desde_aspvh =horaHorarioEntrada;
				hora_hasta_aspvh =horaHorarioSalida;
				String mensaje="";
				int estadoPermiso=0;
		
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;
				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{
			
						estadoPermiso=1;

				}
				
				else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")) {
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=2;

				}
				
				
				
				
				else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")) {
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=3;

				}else {

				}
		
		
				if (hora_desde_aspvh.compareTo(horaTimbre)<0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
				
										
					if (estadoPermiso==1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"',horainicioalmbio_cobmr='JUSTIFICADO TTHH',horafinalmbio_cobmr='JUSTIFICADO TTHH',tiempoalm_cobmr="+tiempoJustificado+" "
										+ "where ide_cobmr="+ide_cobmr);
							}
					if (estadoPermiso==2) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"', horainicioalmbio_cobmr='JUSTIFICADO JF.INM',horafinalmbio_cobmr='JUSTIFICADO JF.INM',tiempoalm_cobmr="+tiempoJustificado+" "
										+ "where ide_cobmr="+ide_cobmr);
					}
					if (estadoPermiso==3) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_alm_cobmr='"+mensaje+"', horainicioalmbio_cobmr='INGRESADO',horafinalmbio_cobmr='INGRESADO',tiempoalm_cobmr="+tiempoJustificado+" "
										+ "where ide_cobmr="+ide_cobmr);
					}
					//justifico;horaHorarioSalidahoraHorarioSalida
				
					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
					//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainicioalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"',horafinalmbio_cobmr='JUSTIFICADO CON PERMISO: "+ide_aspvh+"'   where ide_cobmr="+ide_cobmr);
					tab_tabla.ejecutarSql();
					utilitario.addUpdate("tab_tabla");
					valorRetorno= true;
				}	
				else {
					valorRetorno=false;
					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
					break;
				}
				
				}//Fin de tipo horas
			
			
			
			
			}else {
				//Si no se encuentra en esa fecha no hace nada
				valorRetorno=false;
				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
			} 
				
			

	}//Termina For
		
		
		
	}// Termina else si tiene permiso en el rango de fechas ingresado
	return valorRetorno;

	
	
}//Fin de if si cumple













private Boolean getPermisoHorario(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr){
	String fechaInicioAsnov="";
	String fechaFinAsnov="";
	boolean valorRetorno=false;
	Calendar calFecha = Calendar.getInstance();
	Calendar calFechaInicio = Calendar.getInstance();
	Calendar calFechaFin = Calendar.getInstance();
	//tabla que contiene los permisos para justificar
	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh,aprobado_aspvh  "
			+ "from asi_permisos_vacacion_hext "
			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
    		+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 4))+"' and anulado_aspvh =false");
	
	
	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
	
	if (tab_novedad.getTotalFilas()<0) {
		//si no tiene permisos ingresados en ese rango de fechas
		valorRetorno=false;
		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

	}else {
		
	
	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
	
		//Obtengo los dias de los permisos consultados hasta tres dias antes
		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

	
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
				//cALFECHAiNICIO ES MENOR QUE  FECHA 
				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
			//Encuentro si existe permiso para esa fecha
			
			//Obtengo el tipo de permiso si es por dias o por horas
			String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
			String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
			String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
			String detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
			String horaDesdeAspvh="";
			String horaHastaAspvh="";
			String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
			
	
			
			String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
			String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
			
		

			
			//tipo dias justifica todo
			if (tipoPermiso.equals("4")) {
			
			String mensaje="";
			int banderaAprobacion=0;
			String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
			
			mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
					+ "  "+detalleAspvh;

	
	
			if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
			{
				banderaAprobacion=1;
		
			utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
			+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);
				
			
			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
				banderaAprobacion=2;
			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);

				
			}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
				banderaAprobacion=3;
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"  
				+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);
				
			}else {
				
			}
			
			tab_tabla.ejecutarSql();
			utilitario.addUpdate("tab_tabla");
			
				
				//saco si es de tipo de permiso si es por dias 4  o horas 1
				valorRetorno= true;
				//System.out.println("permiso ingresado por dias"+valorRetorno);
		
			}else {
				//permiso ingresadio por dias
			
				//Obtengo los valores 
				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
				String mensaje="";
				int estadoPermiso=0;
				
				
					
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;
				
				
				
				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{estadoPermiso=1;
					

				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
 
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=2;

				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=3;

				}else {

				}
				
				
				
				
				
				
				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaTimbre)>=0) {
					//justifico;horaHorarioSalidahoraHorarioSalida
					valorRetorno= true;
					//System.out.println("si existe justificacion en ese rango de horas"+valorRetorno);
					
					if (estadoPermiso==1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"   
						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO TTHH'   where ide_cobmr="+ide_cobmr);

					}
					if (estadoPermiso==2) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='JUSTIFICADO JF.INM'   where ide_cobmr="+ide_cobmr);

					}
					if (estadoPermiso==3) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set horainiciobiometrico_cobmr='"+horaHorarioEntrada+"'"    
						+ ",p_entrada_cobmr='"+mensaje+"',horainicioband_cobmr='INGRESADO'   where ide_cobmr="+ide_cobmr);

					}
					
					tab_tabla.ejecutarSql();
					utilitario.addUpdate("tab_tabla");

				}	
				else {
					valorRetorno=false;
					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
					break;
				}
				
				}//Fin de tipo horas
			
			
			
			
			}else {
				//Si no se encuentra en esa fecha no hace nada
				valorRetorno=false;
				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
			} 
				
			

	}//Termina For
		
		
		
	}// Termina else si tiene permiso en el rango de fechas ingresado
	return valorRetorno;

	
	
}//Fin de if si cumple















//Devuelve los valores true si tiene y false si no 
/**
* 
* @param fecha
* @param empleado
* @param horaTimbre
* @param horaHorarioEntrada
* @param horaHorarioSalida
* @param ide_cobmr
* @return
*/
private Boolean getPermisoHorarioSalida(String fecha,int empleado,String horaTimbre,String horaHorarioEntrada,String horaHorarioSalida,String ide_cobmr){

	
	
	String fechaInicioAsnov="";
	String fechaFinAsnov="";
	boolean valorRetorno=false;
	Calendar calFecha = Calendar.getInstance();
	Calendar calFechaInicio = Calendar.getInstance();
	Calendar calFechaFin = Calendar.getInstance();
	//tabla que contiene los permisos para justificar
	TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
			+ "from asi_permisos_vacacion_hext "
			+ "where ide_gtemp="+empleado+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), -20))+"' and "
					
			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha), 4))+"' and anulado_aspvh =false");
	
	
	TablaGenerica  tab_motivo =utilitario.consultar("Select ide_asmot,detalle_asmot from asi_motivo where "
			+ " ide_asmot ="+pckUtilidades.CConversion.CInt(tab_novedad.getValor("ide_asmot")));
	String detalleMotivo=tab_motivo.getValor("detalle_asmot");
	
	
	
	if (tab_novedad.getTotalFilas()<0) {
		//si no tiene permisos ingresados en ese rango de fechas
		valorRetorno=false;
		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

	}else {
		
	
	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
	
		
		fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
		fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");

	
			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
				calFecha.setTime(getFechaAsyyyyMMdd(fecha));
				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
				//cALFECHAiNICIO ES MENOR QUE  FECHA 
				if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0){
			//Encuentro si existe permiso para esa fecha
					//Obtengo el tipo de permiso si es por dias o por horas
					String tipoPermiso=tab_novedad.getValor(i, "tipo_aspvh");
					String ide_aspvh=tab_novedad.getValor(i, "ide_aspvh");
					String nroHorasAspvh=tab_novedad.getValor(i,"nro_horas_aspvh");
					String detalleAspvh="";
							
					if (tab_novedad.getValor(i,"detalle_aspvh")==null) {
						detalleAspvh="";
					}else {
						detalleAspvh=tab_novedad.getValor(i,"detalle_aspvh");
					}
					
					String horaDesdeAspvh="";
					String horaHastaAspvh="";
					String aprobadoJefeInmediato =	tab_novedad.getValor(i,"APROBADO_ASPVH");
					
			
					
					String aprobadoTthhAspvh =	tab_novedad.getValor(i,"APROBADO_TTHH_ASPVH");
					String fechaSolicitudAspvh =	tab_novedad.getValor(i,"FECHA_SOLICITUD_ASPVH");
					
				

			//Obtengo el tipo de permiso si es por dias o por horas

			//tipo dias justifica todo
			if (tipoPermiso.equals("4")) {
				
				
				String mensaje="";
				int banderaAprobacion=0;
				String nroDiasAspvh=tab_novedad.getValor(i,"nro_dias_aspvh");
				
						
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
						+ "  "+detalleAspvh;
						
				if(aprobadoTthhAspvh.equals("true") && aprobadoJefeInmediato.equals("true"))
				{
					banderaAprobacion=1;
				
			
				utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
				+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='JUSTIFICADO TTHH',horafinbiometrico_cobmr='"+horaHorarioSalida+"'  "
						+ "where ide_cobmr="+ide_cobmr);

				
				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
					//System.out.println("empleado :"+empleado+" fecha inicio: "+fechaInicioAsnov+"  fecha fin: "+fechaFinAsnov+" ide_aspvh: "+ide_aspvh);
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
							+ "  "+detalleAspvh;*/
				
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
							+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='JUSTIFICADO JF.INM',horafinbiometrico_cobmr='"+horaHorarioSalida+"' "
									+ "where ide_cobmr="+ide_cobmr);
		
				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroDiasAspvh+" dias"
							+ "  "+detalleAspvh;*/
				
					utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
							+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='INGRESADO',horafinbiometrico_cobmr='"+horaHorarioSalida+"' "
									+ "where ide_cobmr="+ide_cobmr);
					
				}else {
					
				}
				
				tab_tabla.ejecutarSql();
				utilitario.addUpdate("tab_tabla");
				
				
			}else {
				//permiso ingresadio por dias
				
				//Obtengo los valores 
				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
				
	
				String mensaje="";
				int estadoPermiso=0;
					
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;
				if(aprobadoTthhAspvh.equals("true")&& aprobadoJefeInmediato.equals("true"))
				{
				/*	
				mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
						+ "  "+detalleAspvh;*/
						estadoPermiso=1;

				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("true")){
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=2;

				}else if(aprobadoTthhAspvh.equals("false") && aprobadoJefeInmediato.equals("false")){
					/*mensaje ="Permiso "+detalleMotivo+" nro: "+ide_aspvh+" desde "+fechaInicioAsnov+" hasta "+fechaFinAsnov+" por un total de"+nroHorasAspvh+" horas"
							+ "  "+detalleAspvh;*/
					estadoPermiso=3;
				}else {

				}
				
				if (hora_desde_aspvh.compareTo(horaTimbre)<=0 && hora_hasta_aspvh.compareTo(horaHorarioSalida)<=0) {
					//justifico;horaHorarioSalidahoraHorarioSalida
					if (estadoPermiso==1) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
						+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='JUSTIFICADO TTHH',horafinbiometrico_cobmr='"+horaHorarioSalida+"'   "
								+ "where ide_cobmr="+ide_cobmr);

					}
					if (estadoPermiso==2) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='JUSTIFICADO JF.INM' ,horafinbiometrico_cobmr='"+horaHorarioSalida+"'  "
										+ "where ide_cobmr="+ide_cobmr);

					}
					if (estadoPermiso==3) {
						utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones_resumen set "
								+ "p_salida_cobmr='"+mensaje+"',HORAFINBAND_COBMR='INGRESADO' ,horafinbiometrico_cobmr='"+horaHorarioSalida+"'  "
										+ "where ide_cobmr="+ide_cobmr);

					}
					
					valorRetorno= true;
					
					
				}	
				else {
					valorRetorno=false;
					//System.out.println("no existe justificacion en ese rango de horas"+valorRetorno);
					break;
				}
				
				}//Fin de tipo horas
			
			
			
			
			}else {
				//Si no se encuentra en esa fecha no hace nada
				valorRetorno=false;
				//System.out.println("no existe justificacion ingresada para la fecha="+fecha+" "+valorRetorno);
			} 
				
			

	}//Termina For
		
		
		
	}// Termina else si tiene permiso en el rango de fechas ingresado
	return valorRetorno;

	
	
}//Fin de if si cumple






public void modificarMarcacion(){

	//Consultar permiso generado
	
	/*if (tab_tabla.getTotalFilas()>=0) {
		utilitario.agregarMensajeInfo("No se puede realizar cambios", "No hay datos");
		return;
	}*/
	
	TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where cambio_horario_asmot=true");
	int ide_asmot;
	ide_asmot=pckUtilidades.CConversion.CInt(tab_motivo.getValor("ide_asmot"));
	
	
	TablaGenerica tab_permisoEmpleado=utilitario.consultar("select ide_aspvh,aprobado_aspvh from asi_permisos_vacacion_hext where aprobado_aspvh=true and ide_asmot="+ide_asmot);
	int permiso;
	if (tab_permisoEmpleado.getTotalFilas()>0) {
		permiso=pckUtilidades.CConversion.CInt(tab_permisoEmpleado.getValor("ide_aspvh"));
		tab_tabla.setLectura(false);
		tab_tabla.getFilas().get(tab_tabla.getFilaActual()).setLectura(false);
		utilitario.addUpdate("tab_tabla");
	}else {
		utilitario.agregarMensajeInfo("No se puede realizar cambios", "No contine permisos para cambiar");
		return;
	}
	
	
	


}







}

