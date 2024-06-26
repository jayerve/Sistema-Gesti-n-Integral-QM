/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */

public class pre_permisos_reporte_sin_timbre extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();
    private Tabla tab_personal_sin_timbre = new Tabla();

	String sql="";
    public pre_permisos_reporte_sin_timbre() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		

		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("CONSULTAR");
		bot_aprobar_solicitud.setMetodo("cambiarFecha");
		bar_botones.agregarBoton(bot_aprobar_solicitud);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
    	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	
    	
    	
    	
    	
		tab_permisos_reporte.setId("tab_permisos_reporte");
		tab_permisos_reporte.setSql(getDiferenciaEntradaSalida("",""));
		tab_permisos_reporte.setCampoPrimaria("ide_gtemp");
		tab_permisos_reporte.setNumeroTabla(1);
		tab_permisos_reporte.setLectura(true);

		
		tab_permisos_reporte.getColumna("IDE_GTEMP").setLongitud(5);
        tab_permisos_reporte.getColumna("IDE_GTEMP").alinearCentro();
        tab_permisos_reporte.getColumna("IDE_GTEMP").setNombreVisual("COD");


		tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
        tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
        tab_permisos_reporte.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("C�DULA");
		

		tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
        tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").alinearCentro();
        tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
		tab_permisos_reporte.getColumna("NOMBRES_APELLIDOS").setFiltro(true);

		

        
		tab_permisos_reporte.getColumna("IDE_COBMR").setLongitud(5);
        tab_permisos_reporte.getColumna("IDE_COBMR").alinearCentro();

        tab_permisos_reporte.getColumna("DIA_COBMR").setLongitud(15);
        tab_permisos_reporte.getColumna("DIA_COBMR").alinearCentro();
        tab_permisos_reporte.getColumna("DIA_COBMR").setNombreVisual("DIA");
        
        tab_permisos_reporte.getColumna("FECHA_EVENTO_COBMR").setLongitud(15);
        tab_permisos_reporte.getColumna("FECHA_EVENTO_COBMR").setNombreVisual("FECHA");
        
        
        tab_permisos_reporte.getColumna("horainiciohorario_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horainiciohorario_cobmr").setNombreVisual("ENTRADA");
        tab_permisos_reporte.getColumna("horainiciohorario_cobmr").alinearCentro();

        
        
        tab_permisos_reporte.getColumna("horainiciobiometrico_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horainiciobiometrico_cobmr").setNombreVisual("TIMBRE");
        tab_permisos_reporte.getColumna("horainiciobiometrico_cobmr").alinearCentro();

        
        tab_permisos_reporte.getColumna("horainicioband_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horainicioband_cobmr").setNombreVisual("ESTADO ENTRADA");
        tab_permisos_reporte.getColumna("horainicioband_cobmr").alinearCentro();

        tab_permisos_reporte.getColumna("biometrico_entrada_cobmr").setLongitud(20);
        tab_permisos_reporte.getColumna("biometrico_entrada_cobmr").alinearCentro();
        tab_permisos_reporte.getColumna("biometrico_entrada_cobmr").setNombreVisual("UBICACI�N"); 
      	
        
       

        
        tab_permisos_reporte.getColumna("horainicioalm_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horainicioalm_cobmr").setNombreVisual("H.INI.ALM");
        tab_permisos_reporte.getColumna("horainicioalm_cobmr").alinearCentro();

        
        tab_permisos_reporte.getColumna("horafinalm_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinalm_cobmr").setNombreVisual("H.FIN.ALM");
        tab_permisos_reporte.getColumna("horafinalm_cobmr").alinearCentro();

        
        
               
        tab_permisos_reporte.getColumna("horainicioalmbio_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horainicioalmbio_cobmr").setNombreVisual("TM.INI.ALM");
        tab_permisos_reporte.getColumna("horainicioalmbio_cobmr").alinearCentro();

        
        
        
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").setNombreVisual("TM.FIN.ALM");
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").alinearCentro();

       

        tab_permisos_reporte.getColumna("tiempoalm_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("tiempoalm_cobmr").setNombreVisual("TIM.TOMA.ALM");
        tab_permisos_reporte.getColumna("tiempoalm_cobmr").alinearCentro();
        tab_permisos_reporte.getColumna("tiempoalm_cobmr").setVisible(false);

        
        tab_permisos_reporte.getColumna("biometrico_alm_salida").setLongitud(20);
        tab_permisos_reporte.getColumna("biometrico_alm_salida").alinearCentro();
        tab_permisos_reporte.getColumna("biometrico_alm_salida").setNombreVisual("UBICAC�N SAL.ALM"); 


        tab_permisos_reporte.getColumna("biometrico_alm_entrada").setLongitud(20);
        tab_permisos_reporte.getColumna("biometrico_alm_entrada").alinearCentro();
        tab_permisos_reporte.getColumna("biometrico_alm_entrada").setNombreVisual("UBICACI�N ENT.ALM"); 
 

        
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").setNombreVisual("TM.FIN.ALM");
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").alinearCentro();
        tab_permisos_reporte.getColumna("horafinalmbio_cobmr").setVisible(false);



        
        tab_permisos_reporte.getColumna("horafinhorario_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinhorario_cobmr").setNombreVisual("SALIDA");
        tab_permisos_reporte.getColumna("horafinhorario_cobmr").alinearCentro();

        
        
        tab_permisos_reporte.getColumna("horafinbiometrico_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinbiometrico_cobmr").setNombreVisual("TIMBRE SALIDA");
        tab_permisos_reporte.getColumna("horafinbiometrico_cobmr").alinearCentro();

        
        tab_permisos_reporte.getColumna("horafinband_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("horafinband_cobmr").setNombreVisual("ESTADO SALIDA");
        tab_permisos_reporte.getColumna("horafinband_cobmr").alinearCentro();

        
        tab_permisos_reporte.getColumna("biometrico_salida_cobmr").setLongitud(20);
        tab_permisos_reporte.getColumna("biometrico_salida_cobmr").alinearCentro();
        tab_permisos_reporte.getColumna("biometrico_salida_cobmr").setNombreVisual("UBICACI�N SALIDA"); 

     
        
        
        

        tab_permisos_reporte.getColumna("tiempohoralm_cobmr").setLongitud(15);
        tab_permisos_reporte.getColumna("tiempohoralm_cobmr").setNombreVisual("MIN.ALM");
        tab_permisos_reporte.getColumna("tiempohoralm_cobmr").alinearCentro();
        tab_permisos_reporte.getColumna("tiempohoralm_cobmr").setVisible(false);

	
		//+ "res., "
	  
        

    
     /*   tab_permisos_reporte.getColumna("E_FECHA_SOLICITUD").setLongitud(15);
        tab_permisos_reporte.getColumna("E_FECHA_SOLICITUD").alinearCentro();
        tab_permisos_reporte.getColumna("E_FECHA_SOLICITUD").setNombreVisual("FECHA SOLICITUD");
        tab_permisos_reporte.getColumna("E_FECHA_SOLICITUD").setVisible(false);

        
        
        
        tab_permisos_reporte.getColumna("E_TTHH").setLongitud(15);
        tab_permisos_reporte.getColumna("E_TTHH").alinearCentro();
        tab_permisos_reporte.getColumna("E_TTHH").setNombreVisual("TTHH");
        tab_permisos_reporte.getColumna("E_TTHH").setVisible(false);

        
        tab_permisos_reporte.getColumna("E_JEFE_INMEDIATO").setLongitud(15);
        tab_permisos_reporte.getColumna("E_JEFE_INMEDIATO").alinearCentro();
        tab_permisos_reporte.getColumna("E_JEFE_INMEDIATO").setNombreVisual("JEFE INMEDIATO");
        tab_permisos_reporte.getColumna("E_JEFE_INMEDIATO").setVisible(false);
    
      	        
        tab_permisos_reporte.getColumna("E_FECHA_DESDE").setLongitud(15);
        tab_permisos_reporte.getColumna("E_FECHA_DESDE").alinearCentro();
        tab_permisos_reporte.getColumna("E_FECHA_DESDE").setNombreVisual("F.DESDE");
        tab_permisos_reporte.getColumna("E_FECHA_DESDE").setVisible(false);

        
        
        
        
         tab_permisos_reporte.getColumna("E_FECHA_HASTA").setLongitud(16);
        tab_permisos_reporte.getColumna("E_FECHA_HASTA").alinearCentro();
        tab_permisos_reporte.getColumna("E_FECHA_HASTA").setNombreVisual("F.HASTA");
        tab_permisos_reporte.getColumna("E_FECHA_HASTA").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("E_DETALLE").setLongitud(20);
        tab_permisos_reporte.getColumna("E_DETALLE").alinearCentro();
        tab_permisos_reporte.getColumna("E_DETALLE").setNombreVisual("DETALLE");  
        tab_permisos_reporte.getColumna("E_DETALLE").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("E_TIPO_PERMISO").setLongitud(20);
        tab_permisos_reporte.getColumna("E_TIPO_PERMISO").alinearCentro();
        tab_permisos_reporte.getColumna("E_TIPO_PERMISO").setNombreVisual("TIP.PERMISO");  
        tab_permisos_reporte.getColumna("E_TIPO_PERMISO").setVisible(false);

        
        tab_permisos_reporte.getColumna("E_NUM_HORAS").setLongitud(10);
        tab_permisos_reporte.getColumna("E_NUM_HORAS").alinearCentro();
        tab_permisos_reporte.getColumna("E_NUM_HORAS").setNombreVisual("NUM.HORAS");
        tab_permisos_reporte.getColumna("E_NUM_HORAS").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("E_HORA_DESDE").setLongitud(10);
        tab_permisos_reporte.getColumna("E_HORA_DESDE").alinearCentro();
        tab_permisos_reporte.getColumna("E_HORA_DESDE").setNombreVisual("H.DESDE"); 
        tab_permisos_reporte.getColumna("E_HORA_DESDE").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("E_HORA_HASTA").setLongitud(25);
        tab_permisos_reporte.getColumna("E_HORA_HASTA").alinearCentro();
        tab_permisos_reporte.getColumna("E_HORA_HASTA").setNombreVisual("H.HASTA");  
        tab_permisos_reporte.getColumna("E_HORA_HASTA").setVisible(false);

                

        
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_ALM").setLongitud(15);
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_ALM").setNombreVisual("FECHA SOL.ALM");
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_ALM").setVisible(false);


        tab_permisos_reporte.getColumna("TTHH_ALM").setLongitud(15);
        tab_permisos_reporte.getColumna("TTHH_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("TTHH_ALM").setNombreVisual("TTHH.ALM");
        tab_permisos_reporte.getColumna("TTHH_ALM").setVisible(false);
        
        
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_ALM").setLongitud(15);
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_ALM").setNombreVisual("JEFE INM.ALM");
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_ALM").setVisible(false);

      	        
        tab_permisos_reporte.getColumna("FECHA_DESDE_ALM").setLongitud(15);
        tab_permisos_reporte.getColumna("FECHA_DESDE_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_DESDE_ALM").setNombreVisual("F.DESDE.ALM");
        tab_permisos_reporte.getColumna("FECHA_DESDE_ALM").setVisible(false);

        
        
        
         tab_permisos_reporte.getColumna("FECHA_HASTA_ALM").setLongitud(16);
        tab_permisos_reporte.getColumna("FECHA_HASTA_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_HASTA_ALM").setNombreVisual("F.HASTA.ALM");
        tab_permisos_reporte.getColumna("FECHA_HASTA_ALM").setVisible(false);

        
        
        
        tab_permisos_reporte.getColumna("DETALLE_ALM").setLongitud(20);
        tab_permisos_reporte.getColumna("DETALLE_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("DETALLE_ALM").setNombreVisual("DETALLE_ALM");  
        tab_permisos_reporte.getColumna("DETALLE_ALM").setVisible(false);


        tab_permisos_reporte.getColumna("TIPO_PERMISO_ALM").setLongitud(20);
        tab_permisos_reporte.getColumna("TIPO_PERMISO_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("TIPO_PERMISO_ALM").setNombreVisual("TIP.PERMISO.ALM");  
        tab_permisos_reporte.getColumna("TIPO_PERMISO_ALM").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("NUM_HORAS_ALM").setLongitud(10);
        tab_permisos_reporte.getColumna("NUM_HORAS_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("NUM_HORAS_ALM").setNombreVisual("NUM.HORAS_ALM"); 
        tab_permisos_reporte.getColumna("NUM_HORAS_ALM").setVisible(false);

        
        tab_permisos_reporte.getColumna("HORA_DESDE_ALM").setLongitud(10);
        tab_permisos_reporte.getColumna("HORA_DESDE_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("HORA_DESDE_ALM").setNombreVisual("H.DESDE_ALM"); 
        tab_permisos_reporte.getColumna("HORA_DESDE_ALM").setVisible(false);

        
        tab_permisos_reporte.getColumna("HORA_HASTA_ALM").setLongitud(25);
        tab_permisos_reporte.getColumna("HORA_HASTA_ALM").alinearCentro();
        tab_permisos_reporte.getColumna("HORA_HASTA_ALM").setNombreVisual("H.HASTA_ALM");  
        tab_permisos_reporte.getColumna("HORA_HASTA_ALM").setVisible(false);

                
                
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_SAL").setLongitud(15);
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_SAL").setNombreVisual("FECHA SOL.SAL");
        tab_permisos_reporte.getColumna("FECHA_SOLICITUD_SAL").setVisible(false);

        
        

        tab_permisos_reporte.getColumna("TTHH_SAL").setLongitud(15);
        tab_permisos_reporte.getColumna("TTHH_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("TTHH_SAL").setNombreVisual("TTHH_SAL");     
        tab_permisos_reporte.getColumna("TTHH_SAL").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_SAL").setLongitud(15);
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_SAL").setNombreVisual("JEFE INM.SAL");
        tab_permisos_reporte.getColumna("JEFE_INMEDIATO_SAL").setVisible(false);

        
        
        
      	        
        tab_permisos_reporte.getColumna("FECHA_DESDE_SAL").setLongitud(15);
        tab_permisos_reporte.getColumna("FECHA_DESDE_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_DESDE_SAL").setNombreVisual("F.DESDE.SAL");
        tab_permisos_reporte.getColumna("FECHA_DESDE_SAL").setVisible(false);

               
        tab_permisos_reporte.getColumna("FECHA_HASTA_SAL").setLongitud(16);
        tab_permisos_reporte.getColumna("FECHA_HASTA_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("FECHA_HASTA_SAL").setNombreVisual("F.HASTA.SAL");
        tab_permisos_reporte.getColumna("FECHA_HASTA_SAL").setVisible(false);

        
        
        tab_permisos_reporte.getColumna("DETALLE_SAL").setLongitud(20);
        tab_permisos_reporte.getColumna("DETALLE_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("DETALLE_SAL").setNombreVisual("DETALLE.SAL");  
        tab_permisos_reporte.getColumna("DETALLE_SAL").setVisible(false);


        tab_permisos_reporte.getColumna("TIPO_PERMISO_SAL").setLongitud(20);
        tab_permisos_reporte.getColumna("TIPO_PERMISO_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("TIPO_PERMISO_SAL").setNombreVisual("TIP.PERMISO.SAL");  
        tab_permisos_reporte.getColumna("TIPO_PERMISO_SAL").setVisible(false);

        
        tab_permisos_reporte.getColumna("NUM_HORAS_SAL").setLongitud(10);
        tab_permisos_reporte.getColumna("NUM_HORAS_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("NUM_HORAS_SAL").setNombreVisual("NUM.HORAS.SAL"); 
        tab_permisos_reporte.getColumna("NUM_HORAS_SAL").setVisible(false);

        
        tab_permisos_reporte.getColumna("HORA_DESDE_SAL").setLongitud(10);
        tab_permisos_reporte.getColumna("HORA_DESDE_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("HORA_DESDE_SAL").setNombreVisual("H.DESDE.SAL"); 
        tab_permisos_reporte.getColumna("HORA_DESDE_SAL").setVisible(false);

        
        tab_permisos_reporte.getColumna("HORA_HASTA_SAL").setLongitud(25);
        tab_permisos_reporte.getColumna("HORA_HASTA_SAL").alinearCentro();
        tab_permisos_reporte.getColumna("HORA_HASTA_SAL").setNombreVisual("H.HASTA_SAL");  
        tab_permisos_reporte.getColumna("HORA_HASTA_SAL").setVisible(false);

        tab_permisos_reporte.getColumna("DIFERENCIAENTRADA").setLongitud(25);
        tab_permisos_reporte.getColumna("DIFERENCIAENTRADA").setVisible(false);
        
        tab_permisos_reporte.getColumna("diferenciaSalidaAlm").setLongitud(25);
        tab_permisos_reporte.getColumna("diferenciaSalidaAlm").setVisible(false);
         
        tab_permisos_reporte.getColumna("diferenciaEntradaAlm").setLongitud(25);
        tab_permisos_reporte.getColumna("diferenciaEntradaAlm").setVisible(false);
        
        
        tab_permisos_reporte.getColumna("p_entrada_cobmr").setLongitud(25);
        tab_permisos_reporte.getColumna("p_entrada_cobmr").setVisible(false);
        
        tab_permisos_reporte.getColumna("p_salida_cobmr").setLongitud(25);
        tab_permisos_reporte.getColumna("p_salida_cobmr").setVisible(false);
        
        tab_permisos_reporte.getColumna("p_alm_cobmr").setLongitud(25);
        tab_permisos_reporte.getColumna("p_alm_cobmr").setVisible(false);

                
        tab_permisos_reporte.getColumna("DETALLE_GTTEM").setLongitud(25);
        tab_permisos_reporte.getColumna("DETALLE_GTTEM").alinearCentro();
        tab_permisos_reporte.getColumna("DETALLE_GTTEM").setNombreVisual("T.EMPLEADO");  
        */
        
		tab_permisos_reporte.setRows(15);
		tab_permisos_reporte.dibujar();
		tab_permisos_reporte.setHeader("REPORTE RESUMEN BIOM�TRICO POR EMPLEADO");

    	
    	
    	
    	
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RESUMEN DE VACACI�N POR EMPLEADO");
		pat_panel.setPanelTabla(tab_permisos_reporte);
		
		
		
		
		tab_personal_sin_timbre.setId("tab_personal_sin_timbre");
		tab_personal_sin_timbre.setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.NOMBRES_APELLIDOS,   "
				+ "'' AS entrada, "
				+ "'' AS salida_almuerzo, "
				+ "'' AS entrada_almuerzo, "
				+ "'' AS salida "
				+ "from ("+getDiferenciaEntradaSalida("","")+" ) EMP GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.NOMBRES_APELLIDOS "
				+ "ORDER BY EMP.NOMBRES_APELLIDOS");
		
		


		
		tab_personal_sin_timbre.setCampoPrimaria("ide_gtemp");
		tab_personal_sin_timbre.setNumeroTabla(1);
		tab_personal_sin_timbre.setLectura(true);

		
		tab_personal_sin_timbre.getColumna("IDE_GTEMP").setLongitud(5);
		tab_personal_sin_timbre.getColumna("IDE_GTEMP").alinearCentro();
		tab_personal_sin_timbre.getColumna("IDE_GTEMP").setNombreVisual("COD");


		tab_personal_sin_timbre.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(7);
		tab_personal_sin_timbre.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearCentro();
		tab_personal_sin_timbre.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("C�DULA");
		

		tab_personal_sin_timbre.getColumna("NOMBRES_APELLIDOS").setLongitud(20);
		tab_personal_sin_timbre.getColumna("NOMBRES_APELLIDOS").alinearCentro();
		tab_personal_sin_timbre.getColumna("NOMBRES_APELLIDOS").setNombreVisual("EMPLEADO");
		tab_personal_sin_timbre.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
	
		
		tab_personal_sin_timbre.setRows(15);
		tab_personal_sin_timbre.dibujar();
		tab_personal_sin_timbre.setHeader("REPORTE RESUMEN POR EMPLEADO SIN MARCACION");

    	 	
		
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("REPORTE RESUMEN POR EMPLEADO SI");
		pat_panel1.setPanelTabla(tab_personal_sin_timbre);
				
				
				
		
		Division div_division = new Division();		
		//div_division.dividir1(pat_panel);
		div_division.dividir2(pat_panel, pat_panel1, "70%", "H");

		agregarComponente(div_division);
		
    	

    }

	public Tabla getTab_permisos_reporte() {
		return tab_permisos_reporte;
	}

	public void setTab_permisos_reporte(Tabla tab_permisos_reporte) {
		this.tab_permisos_reporte = tab_permisos_reporte;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public void cambiarFecha(){
		
    tab_permisos_reporte.setSql(getDiferenciaEntradaSalida(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha()));
 	tab_permisos_reporte.ejecutarSql();
 	
 	
 	
 	
   	
	tab_personal_sin_timbre.setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.NOMBRES_APELLIDOS,   "
			+ "'' AS entrada, "
			+ "'' AS salida_almuerzo, "
			+ "'' AS entrada_almuerzo, "
			+ "'' AS salida "
			+ "from ("+getDiferenciaEntradaSalida(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha())+" ) EMP GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.NOMBRES_APELLIDOS "
			+ "ORDER BY EMP.NOMBRES_APELLIDOS");
	
	tab_personal_sin_timbre.ejecutarSql();
	

	
	
	TablaGenerica tab=utilitario.consultar("select distinct EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.NOMBRES_APELLIDOS,   "
 			+ "CASE WHEN emp.horainicioband_cobmr='SIN TIMBRE' THEN  COUNT (emp.horainicioband_cobmr) ELSE 0 END entrada, "
 			+ "CASE WHEN emp.horainicioalmbio_cobmr='' THEN  COUNT (emp.horainicioalmbio_cobmr) ELSE 0 END salida_almuerzo, "
 			+ "CASE WHEN emp.horafinalmbio_cobmr='' THEN  COUNT (emp.horafinalmbio_cobmr) ELSE 0 END entrada_almuerzo, "
 			+ "CASE WHEN emp.horafinband_cobmr='SIN TIMBRE' THEN  COUNT (emp.horafinband_cobmr) ELSE 0 END salida "
 			+ "from ( "
 			+ " "+getDiferenciaEntradaSalida(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha())+" ) EMP  "
 		//	+ "WHERE EMP.IDE_GTEMP IN("+tab_personal_sin_timbre.getValor(i,"IDE_GTEMP")+")"
 			+ "GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, EMP.NOMBRES_APELLIDOS,emp.horainicioband_cobmr,emp.horainicioalmbio_cobmr,emp.horafinalmbio_cobmr,emp.horafinband_cobmr "
			+ "ORDER BY EMP.NOMBRES_APELLIDOS");
	
	
	
 	for (int i = 0; i < tab_personal_sin_timbre.getTotalFilas(); i++) {
 		int sumatoriaEntrada=0;
 		int sumatoriaSalidaAlmuerzo=0;
 		int sumatoriaEntradaAlmuerzo=0;
 		int sumatoriaSalida=0;

 
 	
 	String empleado1="",empleado2="";
 	empleado1=tab_personal_sin_timbre.getValor(i,"ide_gtemp");
 
 	 	for (int j = 0; j < tab.getTotalFilas(); j++) {
 	 		empleado2=tab.getValor(j,"ide_gtemp");
 	 	
 	 		if (empleado1.equals(empleado2)) {
 	 			sumatoriaEntrada=Integer.parseInt(tab.getValor(j,"entrada"))+sumatoriaEntrada;
		 		sumatoriaSalidaAlmuerzo=Integer.parseInt(tab.getValor(j,"salida_almuerzo"))+sumatoriaSalidaAlmuerzo;
		 		sumatoriaEntradaAlmuerzo=Integer.parseInt(tab.getValor(j,"entrada_almuerzo"))+sumatoriaEntradaAlmuerzo;
		 		sumatoriaSalida=Integer.parseInt(tab.getValor(j,"salida"))+sumatoriaSalida;
		 		if (j==(tab.getTotalFilas()-1)) {
		 			tab_personal_sin_timbre.setValor(i, "entrada", ""+sumatoriaEntrada);
	 	 			tab_personal_sin_timbre.setValor(i, "salida_almuerzo", ""+sumatoriaSalidaAlmuerzo);
	 	 			tab_personal_sin_timbre.setValor(i, "entrada_almuerzo", ""+sumatoriaEntradaAlmuerzo);
	 	 			tab_personal_sin_timbre.setValor(i, "salida", ""+sumatoriaSalida);
	 	 			i=tab_personal_sin_timbre.getTotalFilas();
				}

			}else {
 	 			tab_personal_sin_timbre.setValor(i, "entrada", ""+sumatoriaEntrada);
 	 			tab_personal_sin_timbre.setValor(i, "salida_almuerzo", ""+sumatoriaSalidaAlmuerzo);
 	 			tab_personal_sin_timbre.setValor(i, "entrada_almuerzo", ""+sumatoriaEntradaAlmuerzo);
 	 			tab_personal_sin_timbre.setValor(i, "salida", ""+sumatoriaSalida);
 	 			i++;
 	 		 	empleado1=tab_personal_sin_timbre.getValor(i,"ide_gtemp");
 	 			sumatoriaEntrada=0;
 	 			sumatoriaSalidaAlmuerzo=0;
 	 			sumatoriaEntradaAlmuerzo=0;
 	 			sumatoriaSalida=0;
		 		sumatoriaEntrada=Integer.parseInt(tab.getValor(j,"entrada"))+sumatoriaEntrada;
		 		sumatoriaSalidaAlmuerzo=Integer.parseInt(tab.getValor(j,"salida_almuerzo"))+sumatoriaSalidaAlmuerzo;
		 		sumatoriaEntradaAlmuerzo=Integer.parseInt(tab.getValor(j,"entrada_almuerzo"))+sumatoriaEntradaAlmuerzo;
		 		sumatoriaSalida=Integer.parseInt(tab.getValor(j,"salida"))+sumatoriaSalida;
		}

 	 	}
	}
 	
	

	
	}
	public void limpiar() {
		tab_permisos_reporte.limpiar();	
		utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
	}

	
	public String getDiferenciaEntradaSalida(String strFechaIniReporte, String strFechaFinReporte){
		

		if (strFechaFinReporte==null || strFechaFinReporte.equals("") || strFechaFinReporte.isEmpty() || strFechaIniReporte==null || strFechaIniReporte.equals("")
				|| strFechaIniReporte.isEmpty()) {
			sql="select  "
					+ "distinct a.IDE_GTEMP, a.DOCUMENTO_IDENTIDAD_GTEMP,  a.NOMBRES_APELLIDOS,  a.ide_cobmr, a.dia_cobmr, a.fecha_evento_cobmr,  "
					+ "a.horainiciohorario_cobmr, a.horainiciobiometrico_cobmr, a.horainicioband_cobmr, a.biometrico_entrada_cobmr, a.horainicioalm_cobmr,  "
					+ "a.horainicioalmbio_cobmr,  a.horafinalm_cobmr,  a.horafinalmbio_cobmr,  "
					+ "a.tiempoalm_cobmr, a.biometrico_alm_salida, a.biometrico_alm_entrada, a.tiempohoralm_cobmr, a.horafinhorario_cobmr, a.horafinbiometrico_cobmr, a.horafinband_cobmr,"
					+ "a.biometrico_salida_cobmr  "
					+ "from ( "
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, "
					+ "res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr, "
					+ "res.biometrico_salida_cobmr  "
					+ "FROM GTH_EMPLEADO EMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "where res.fecha_evento_cobmr between '"+utilitario.getFechaActual()+"' and '"+utilitario.getFechaActual()+"'   and res.horainicioband_cobmr ='SIN TIMBRE'  and  res.horafinband_cobmr ='SIN TIMBRE' "
					+ "UNION ALL "
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, res.horafinband_cobmr,res.biometrico_salida_cobmr  "
					+ "FROM  "
					+ "GTH_EMPLEADO EMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "where  "
					+ "res.fecha_evento_cobmr between '"+utilitario.getFechaActual()+"' and '"+utilitario.getFechaActual()+"'   and res.horainicioband_cobmr ='ATRASADO'  and  res.horafinband_cobmr ='SIN TIMBRE'  "
					+ "UNION ALL  "
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr, "
					+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr, res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, "
					+ "res.horafinbiometrico_cobmr, res.horafinband_cobmr, res.biometrico_salida_cobmr "
					+ "FROM GTH_EMPLEADO EMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "where  "
					+ "res.fecha_evento_cobmr between  '"+utilitario.getFechaActual()+"' and '"+utilitario.getFechaActual()+"'   and res.horainicioband_cobmr ='SIN TIMBRE'  AND  res.horafinband_cobmr ='ANTICIPADO'  "
					+ "UNION ALL  "
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr, "
					+ "res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr,  "
					+ "res.biometrico_salida_cobmr   "
					+ "FROM  GTH_EMPLEADO EMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "where  res.fecha_evento_cobmr between  '"+utilitario.getFechaActual()+"' and '"+utilitario.getFechaActual()+"'  AND res.horainicioalmbio_cobmr ='' AND res.horainicioband_cobmr !='LIBRE' AND res.horainicioband_cobmr !='FERIADO'  "
					+ "AND res.horainicioband_cobmr !='EXTRA' AND res.horafinband_cobmr !='FERIADO'  AND res.horafinband_cobmr !='EXTRA'   AND res.horafinband_cobmr !='LIBRE' "
					+ "UNION ALL  "
					+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
					+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
					+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,   "
					+ "res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr,  "
					+ "res.biometrico_salida_cobmr  "
					+ "FROM GTH_EMPLEADO EMP  "
					+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
					+ "where   "
					+ "res.fecha_evento_cobmr between '"+utilitario.getFechaActual()+"' and '"+utilitario.getFechaActual()+"'   AND  res.horafinalmbio_cobmr ='' AND res.horafinband_cobmr !='LIBRE' AND res.horafinband_cobmr !='FERIADO'   "
					+ "AND res.horafinband_cobmr !='EXTRA'  AND res.horainicioband_cobmr !='LIBRE' AND res.horainicioband_cobmr !='FERIADO'  AND res.horainicioband_cobmr !='EXTRA'  "
					+ ")as a  "
					+ "group by "
					+ "a.IDE_GTEMP, a.DOCUMENTO_IDENTIDAD_GTEMP,  a.NOMBRES_APELLIDOS,  a.ide_cobmr, a.dia_cobmr, a.fecha_evento_cobmr,  "
					+ "a.horainiciohorario_cobmr, a.horainiciobiometrico_cobmr, a.horainicioband_cobmr, a.biometrico_entrada_cobmr, a.horainicioalm_cobmr,  a.horainicioalmbio_cobmr,  "
					+ "a.horafinalm_cobmr,  a.horafinalmbio_cobmr,  "
					+ "a.tiempoalm_cobmr, a.biometrico_alm_salida, a.biometrico_alm_entrada, a.tiempohoralm_cobmr, a.horafinhorario_cobmr, a.horafinbiometrico_cobmr, a.horafinband_cobmr,  "
					+ "a.biometrico_salida_cobmr  "
					+ "ORDER BY a.NOMBRES_APELLIDOS asc,a.fecha_evento_cobmr asc ";
		}else{
		
		sql="select  "
				+ "distinct a.IDE_GTEMP, a.DOCUMENTO_IDENTIDAD_GTEMP,  a.NOMBRES_APELLIDOS,  a.ide_cobmr, a.dia_cobmr, a.fecha_evento_cobmr,  "
				+ "a.horainiciohorario_cobmr, a.horainiciobiometrico_cobmr, a.horainicioband_cobmr, a.biometrico_entrada_cobmr, a.horainicioalm_cobmr,  "
				+ "a.horainicioalmbio_cobmr,  a.horafinalm_cobmr,  a.horafinalmbio_cobmr,  "
				+ "a.tiempoalm_cobmr, a.biometrico_alm_salida, a.biometrico_alm_entrada, a.tiempohoralm_cobmr, a.horafinhorario_cobmr, a.horafinbiometrico_cobmr, a.horafinband_cobmr,"
				+ "a.biometrico_salida_cobmr  "
				+ "from ( "
				+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
				+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr, "
				+ "res.biometrico_salida_cobmr  "
				+ "FROM GTH_EMPLEADO EMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "where res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   and res.horainicioband_cobmr ='SIN TIMBRE'  and  res.horafinband_cobmr ='SIN TIMBRE' "
				+ "UNION ALL "
				+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
				+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, res.horafinband_cobmr,res.biometrico_salida_cobmr  "
				+ "FROM  "
				+ "GTH_EMPLEADO EMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "where  "
				+ "res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   and res.horainicioband_cobmr ='ATRASADO'  and  res.horafinband_cobmr ='SIN TIMBRE'  "
				+ "UNION ALL  "
				+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr, "
				+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr, res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, res.horafinband_cobmr, res.biometrico_salida_cobmr "
				+ "FROM GTH_EMPLEADO EMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "where  "
				+ "res.fecha_evento_cobmr between  '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   and res.horainicioband_cobmr ='SIN TIMBRE'  AND  res.horafinband_cobmr ='ANTICIPADO'  "
				+ "UNION ALL  "
				+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
				+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr, "
				+ "res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr,  "
				+ "res.biometrico_salida_cobmr   "
				+ "FROM  GTH_EMPLEADO EMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "where  res.fecha_evento_cobmr between  '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  AND res.horainicioalmbio_cobmr ='' AND res.horainicioband_cobmr !='LIBRE' AND res.horainicioband_cobmr !='FERIADO'  "
				+ "AND res.horainicioband_cobmr !='EXTRA' AND res.horafinband_cobmr !='FERIADO'  AND res.horafinband_cobmr !='EXTRA'   AND res.horafinband_cobmr !='LIBRE' "
				+ "UNION ALL  "
				+ "SELECT EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP,  EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, res.ide_cobmr, res.dia_cobmr, res.fecha_evento_cobmr,  "
				+ "res.horainiciohorario_cobmr, res.horainiciobiometrico_cobmr, res.horainicioband_cobmr, res.biometrico_entrada_cobmr, res.horainicioalm_cobmr,  res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  res.horafinalmbio_cobmr,   "
				+ "res.tiempoalm_cobmr, res.biometrico_alm_salida, res.biometrico_alm_entrada, res.tiempohoralm_cobmr, res.horafinhorario_cobmr, res.horafinbiometrico_cobmr, res.horafinband_cobmr,  "
				+ "res.biometrico_salida_cobmr  "
				+ "FROM GTH_EMPLEADO EMP  "
				+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp  "
				+ "where   "
				+ "res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'   AND  res.horafinalmbio_cobmr ='' AND res.horafinband_cobmr !='LIBRE' AND res.horafinband_cobmr !='FERIADO'   "
				+ "AND res.horafinband_cobmr !='EXTRA'  AND res.horainicioband_cobmr !='LIBRE' AND res.horainicioband_cobmr !='FERIADO'  AND res.horainicioband_cobmr !='EXTRA'  "
				+ ")as a  "
				+ "group by "
				+ "a.IDE_GTEMP, a.DOCUMENTO_IDENTIDAD_GTEMP,  a.NOMBRES_APELLIDOS,  a.ide_cobmr, a.dia_cobmr, a.fecha_evento_cobmr,  "
				+ "a.horainiciohorario_cobmr, a.horainiciobiometrico_cobmr, a.horainicioband_cobmr, a.biometrico_entrada_cobmr, a.horainicioalm_cobmr,  a.horainicioalmbio_cobmr,  "
				+ "a.horafinalm_cobmr,  a.horafinalmbio_cobmr,  "
				+ "a.tiempoalm_cobmr, a.biometrico_alm_salida, a.biometrico_alm_entrada, a.tiempohoralm_cobmr, a.horafinhorario_cobmr, a.horafinbiometrico_cobmr, a.horafinband_cobmr,  "
				+ "a.biometrico_salida_cobmr  "
				+ "ORDER BY a.NOMBRES_APELLIDOS asc,a.fecha_evento_cobmr asc ";
		
		
		}
		
		
		/*
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
				+ "res.horainicioband_cobmr, "
				+ "res.biometrico_entrada_cobmr, "			       
				+ "res.horainicioalm_cobmr,  "
				+ "res.horainicioalmbio_cobmr,  "
				+ "res.horafinalm_cobmr,  "
				+ "res.horafinalmbio_cobmr,  "
				+ "res.tiempoalm_cobmr, "
				+ "res.biometrico_alm_salida, "
				+ "res.biometrico_alm_entrada, "
				+ "res.tiempohoralm_cobmr, "
				+ "res.horafinhorario_cobmr, "
				+ "res.horafinbiometrico_cobmr, "
				+ "res.horafinband_cobmr, "
				+ "res.biometrico_salida_cobmr "
				
		
		+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
		+ "left join con_biometrico_marcaciones_resumen res on res.ide_gtemp=emp.ide_gtemp "
		+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
		+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
		+ "LEFT JOIN GTH_TIPO_EMPLEADO GTT ON GTT.IDE_GTTEM=EPAR.IDE_GTTEM  "
		+ "where epar.activo_geedp=true AND  res.fecha_evento_cobmr between '"+strFechaIniReporte+"' and '"+strFechaFinReporte+"'  "
		+ " OR res.horainicioband_cobmr ='SIN TIMBRE' "
		+ "OR res.horainicioalm_cobmr ='' "
		+ "OR res.horafinalmbio_cobmr ='' "
		+ "OR  res.horafinband_cobmr ='SIN TIMBRE' "
		+ "ORDER BY AREA.DETALLE_GEARE,EMP.APELLIDO_PATERNO_GTEMP asc,EMP.APELLIDO_MATERNO_GTEMP asc,EMP.PRIMER_NOMBRE_GTEMP asc,EMP.SEGUNDO_NOMBRE_GTEMP asc,res.fecha_evento_cobmr asc";
 
		*/
		
		
		
		System.out.println("Imprrimir sql "+sql);
		
		return sql;
			
	}

	public Tabla getTab_personal_sin_timbre() {
		return tab_personal_sin_timbre;
	}

	public void setTab_personal_sin_timbre(Tabla tab_personal_sin_timbre) {
		this.tab_personal_sin_timbre = tab_personal_sin_timbre;
	}
	

	
	
}
