/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import javax.ejb.EJB;

import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_biometrico_consulta_jefe_inmediato extends Pantalla {

    private Tabla tab_planificacion_hxe = new Tabla();
    private Tabla tab_planificacion_hxe_observacion = new Tabla();
	TablaGenerica tabEmpleadoXJefeInmediato;

    
    private SeleccionTabla sel_empleado= new SeleccionTabla();
    private SeleccionTabla sel_empleado_asignacion= new SeleccionTabla();
    private SeleccionTabla sel_empleado_registro_manual= new SeleccionTabla();


    private String meses="",anios="",mesEditar="",anioEditar="",mes="",anio="",empleado="",jefe_inmediato_planificacion="",tipo_perfil="";
    private SeleccionTabla sel_mes= new SeleccionTabla();
    private SeleccionTabla sel_anio= new SeleccionTabla();
    private SeleccionTabla sel_mes_registro_manual= new SeleccionTabla();
    private SeleccionTabla sel_anio_registro_manual= new SeleccionTabla();
	
    
	private Confirmar con_guardar=new Confirmar();
	String ide_geare="",ide_gtemp_jefe_inmediato="";
	String empleado_asignado="",empleado_registro_manual="";

	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	
    public pre_biometrico_consulta_jefe_inmediato() {
    	
		meses=utilitario.getVariable("p_asi_mes_editar_horario_tthh");
		anios=utilitario.getVariable("p_anio_asignacion_horario_administrador");
		
		
		///VALIDACION ASIGNACION DE HXE JEFE INMEDIATO     	
				String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
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
					ide_gtemp_jefe_inmediato=tabJefeInmediato.getValor("ide_gtemp");
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
				ide_gtemp_jefe_inmediato=tabJefeInmediato.getValor("ide_gtemp");

		    	
		    	}

		
		
		
		
		
		bar_botones.agregarComponente(new Etiqueta("Seleccione Opcion:"));

		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
    	Boton bot_asignar= new Boton();
    	bot_asignar.setIcon("ui-icon-pencil");
    	bot_asignar.setMetodo("abrirMes");
    	bot_asignar.setValue("1. CONSULTA PLANIFICACION");
    	bot_asignar.setTitle("1. CONSULTA PLANIFICACION");
    	bar_botones.agregarBoton(bot_asignar);	
    	
    	
    	
    	Boton bot_devolucion= new Boton();
    	bot_devolucion.setIcon("ui-icon-pencil");
    	bot_devolucion.setMetodo("dibujarConfirmar");
    	bot_devolucion.setValue("2. DEVOLVER PLANIFICACION");
    	bot_devolucion.setTitle("2. DEVOLVER PLANIFICACION");
    	bar_botones.agregarBoton(bot_devolucion);	
    	
    	Boton bot_aprobar_planificacion= new Boton();
    	bot_aprobar_planificacion.setIcon("ui-icon-pencil");
    	bot_aprobar_planificacion.setMetodo("aprobarPlanificacion");
    	bot_aprobar_planificacion.setValue("3. APROBAR PLANIFICACION");
    	bot_aprobar_planificacion.setTitle("3. APROBAR PLANIFICACION");
    	bar_botones.agregarBoton(bot_aprobar_planificacion);
    	
    	
    	Boton bot_registro_marcacion_manual= new Boton();
    	bot_registro_marcacion_manual.setIcon("ui-icon-pencil");
    	bot_registro_marcacion_manual.setMetodo("registroManualBiometrico");
    	bot_registro_marcacion_manual.setValue("4. REGISTRO MANUAL");
    	bot_registro_marcacion_manual.setTitle("4. REGISTRO MANUAL");
    	bar_botones.agregarBoton(bot_registro_marcacion_manual);
  
    	
    	
    	
    	sel_mes.setId("sel_mes");
    	sel_mes.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in("+meses+") ","IDE_GEMES");

    	sel_mes.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes.setRadio();
    	sel_mes.setTitle("Seleccione Mes Horario");
    	sel_mes.setWidth("20");
    	sel_mes.setHeight("20");


		gru_pantalla.getChildren().add(sel_mes);
		sel_mes.getBot_aceptar().setMetodo("obtenerMes");
		agregarComponente(sel_mes);
		
		
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
    			
		
		
    	sel_empleado.setId("sel_empleado");
		agregarComponente(sel_empleado);
    	
	   	sel_empleado_asignacion.setId("sel_empleado_asignacion");
		agregarComponente(sel_empleado_asignacion);

		
	   	sel_empleado_registro_manual.setId("sel_empleado_registro_manual");
			agregarComponente(sel_empleado_registro_manual);
		
		
        tab_planificacion_hxe.setId("tab_planificacion_hxe");
        tab_planificacion_hxe.setTabla("con_biometrico_plan_hxe", "ide_cobph", 1);
        tab_planificacion_hxe.getColumna("ide_cobph").setNombreVisual("CODIGO");
        tab_planificacion_hxe.getColumna("ide_gtemp").setNombreVisual("EMPLEADO");

		tab_planificacion_hxe.getColumna("ide_gtemp").setLectura(true);
		tab_planificacion_hxe.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
		"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
		"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
		"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
		"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
		" FROM  GTH_EMPLEADO EMP ");

		
		tab_planificacion_hxe.getColumna("ide_gtemp").setAutoCompletar();
		tab_planificacion_hxe.getColumna("ide_gtemp").setFiltroContenido();
		tab_planificacion_hxe.getColumna("ide_gtemp").setLectura(true);

		tab_planificacion_hxe.getColumna("ide_gepgc").setNombreVisual("CARGO");
        tab_planificacion_hxe.getColumna("ide_gepgc").setCombo("SELECT ide_gepgc,titulo_cargo_gepgc from  gen_partida_grupo_cargo") ;
        tab_planificacion_hxe.getColumna("ide_gepgc").setAutoCompletar();
		//tab_planificacion_hxe.getColumna("ide_gepgc").setLongitud(10);*/
		tab_planificacion_hxe.getColumna("ide_gepgc").setLectura(true);
		tab_planificacion_hxe.getColumna("ide_gepgc").setLectura(true);
		
		tab_planificacion_hxe.getColumna("ide_geare").setNombreVisual("AREA");
        tab_planificacion_hxe.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare FROM gen_area ") ;
		tab_planificacion_hxe.getColumna("ide_geare").setAutoCompletar();
		tab_planificacion_hxe.getColumna("ide_geare").setLectura(true);
		tab_planificacion_hxe.getColumna("ide_geare").setLectura(true);
        
        tab_planificacion_hxe.getColumna("actividades_cobph").setNombreVisual("ACTIVIDADES");
        tab_planificacion_hxe.getColumna("actividades_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("archivo_cobph").setVisible(false);  
        tab_planificacion_hxe.getColumna("nombre_archivo_cobph").setVisible(false);
      //  tab_planificacion_hxe.getColumna("activo_cobph").setLectura(true);

	    tab_planificacion_hxe.getColumna("horas25_loep_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas25_loep_cobph").setNombreVisual("NRO.HORAS 25 LOEP");
        tab_planificacion_hxe.getColumna("horas25_loep_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setNombreVisual("NRO.HORAS 60 LOEP");
        tab_planificacion_hxe.getColumna("horas60_loep_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setNombreVisual("NRO.HORAS 25 CT");
        tab_planificacion_hxe.getColumna("horas25_ct_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setNombreVisual("NRO.HORAS 50 CT");
        tab_planificacion_hxe.getColumna("horas50_ct_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setValorDefecto("0");
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setNombreVisual("NRO.HORAS 100 LOEP Y CT");
        tab_planificacion_hxe.getColumna("horas100_loep_ct_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes FROM gen_mes");
        tab_planificacion_hxe.getColumna("ide_gemes").setVisible(false);
        tab_planificacion_hxe.getColumna("ide_gemes").setLectura(true);
        tab_planificacion_hxe.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani FROM gen_anio");
        tab_planificacion_hxe.getColumna("ide_geani").setVisible(false);  
        tab_planificacion_hxe.getColumna("ide_geani").setLectura(true);
   
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setNombreVisual("FECHA REGISTRO");
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setValorDefecto(utilitario.getFechaActual());
        tab_planificacion_hxe.getColumna("fecha_registro_cobph").setLectura(true);
              
        tab_planificacion_hxe.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
        tab_planificacion_hxe.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
        tab_planificacion_hxe.getColumna("IDE_USUA").setAutoCompletar();
        tab_planificacion_hxe.getColumna("IDE_USUA").setLectura(true);
        
        tab_planificacion_hxe.getColumna("activo_cobph").setNombreVisual("ACTIVO");
        tab_planificacion_hxe.getColumna("activo_cobph").setCheck();
        tab_planificacion_hxe.getColumna("activo_cobph").setValorDefecto("true");
        tab_planificacion_hxe.getColumna("activo_cobph").setLectura(true);
        tab_planificacion_hxe.getColumna("IDE_ASJEI").setVisible(false);
        
        //tab_planificacion_hxe.setLectura(true);
        tab_planificacion_hxe.setCondicion("IDE_ASJEI=-1");
        tab_planificacion_hxe.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_planificacion_hxe);
        pat_panel.setMensajeWarn("PLANIFICACION_HXE");
        
        
        


        tab_planificacion_hxe_observacion.setId("tab_planificacion_hxe_observacion");
        tab_planificacion_hxe_observacion.setTabla("con_biometrico_phxe_observacion", "ide_cobpo", 2);
        tab_planificacion_hxe_observacion.getColumna("ide_cobpo").setNombreVisual("CODIGO");
        tab_planificacion_hxe_observacion.getColumna("IDE_ASJEI").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("observacion_cobpo").setNombreVisual("OBSERVACION");
        tab_planificacion_hxe_observacion.getColumna("observacion_cobpo").setRequerida(true);
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setCombo("SELECT ide_gemes, detalle_gemes FROM gen_mes");
        tab_planificacion_hxe_observacion.getColumna("ide_gemes").setVisible(false);
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani FROM gen_anio");
        tab_planificacion_hxe_observacion.getColumna("ide_geani").setVisible(false);  
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setAutoCompletar();
        tab_planificacion_hxe_observacion.getColumna("IDE_USUA").setLectura(true);
        tab_planificacion_hxe_observacion.getColumna("fecha_registro_cobpo").setValorDefecto(utilitario.getFechaActual());;
        tab_planificacion_hxe_observacion.getColumna("fecha_registro_cobpo").setLectura(true);
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setNombreVisual("ACTIVO");
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setCheck();
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setValorDefecto("true");
        tab_planificacion_hxe_observacion.getColumna("activo_cobpo").setLectura(true);
        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
        tab_planificacion_hxe_observacion.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_planificacion_hxe_observacion);
        pat_panel2.setMensajeWarn("OBSERVACION PLANIFICACION_HXE");
        
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "70%", "H");
       // div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
        
       /* Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);*/
    }


   

    
    


	public void getJefesInmediatos(){
    	sel_empleado.dibujar();
    }
	
	
	public void abrirMes(){
		tab_planificacion_hxe.setCondicion("ide_cobph=-1");
		tab_planificacion_hxe.ejecutarSql();
        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
        tab_planificacion_hxe_observacion.ejecutarSql();
    	sel_mes.dibujar();
		}
	
	
	public void registroManualBiometrico(){
		tab_planificacion_hxe.setCondicion("ide_cobph=-1");
		tab_planificacion_hxe.ejecutarSql();
        tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
        tab_planificacion_hxe_observacion.ejecutarSql();
    	sel_mes_registro_manual.dibujar();
		}
	
	
	
	
	public void getEmpleado(){
		empleado="";
		empleado=sel_empleado.getSeleccionados();
		if ((empleado==null || empleado.isEmpty() || empleado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			
			 		String 	sql="SELECT cbph.ide_cobph,  "
			 				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			 				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			 				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			 				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
			 				+ "anio.detalle_geani,mes.detalle_gemes,cbph.ide_asjei  "
			 				+ "FROM con_biometrico_plan_hxe cbph "
			 				+ "left join gth_empleado emp on emp.ide_gtemp=cbph.ide_gtemp  "
			 				+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani "
			 				+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
			 				+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+anio+" and  cbph.ide_asjei="+empleado+" and cbph.aprobado_cobph=true  "
			 				+ " order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP "; 


			 	    	System.out.println(""+sql);
			 			sel_empleado_asignacion.setId("sel_empleado_asignacion");
			 			sel_empleado_asignacion.setSeleccionTabla(sql, "ide_cobph");
			 			sel_empleado_asignacion.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
			 			sel_empleado_asignacion.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
			 			//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
			 			sel_empleado_asignacion.setHeight("60");
			 			sel_empleado_asignacion.setWidth("40");
			 			sel_empleado_asignacion.setTitle("Seleccione Empleados");
			 			sel_empleado_asignacion.getBot_aceptar().setMetodo("getEmpleadoAsignacion");
			 			sel_empleado_asignacion.dibujar();
			 			sel_empleado_asignacion.redibujar();
			 			sel_empleado.cerrar();
			 			utilitario.addUpdate("sel_empleado_asignacion,sel_empleado");  

			 			
			 			
			 	   

		      }
	}

	public void getEmpleadoAsignacion(){
		empleado_asignado=sel_empleado_asignacion.getSeleccionados();
		if ((empleado_asignado==null || empleado_asignado.isEmpty() || empleado_asignado.equals(""))) {
			 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado empleado");
			 			return;
			 		}else {
			 			sel_empleado_asignacion.cerrar();	
			 			tab_planificacion_hxe.setCondicion("ide_cobph in("+empleado_asignado+") and ide_gemes="+mes+" and ide_geani="+anio+" and ide_asjei="+empleado);
			 			tab_planificacion_hxe.ejecutarSql();
			 			tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
	              		tab_planificacion_hxe_observacion.ejecutarSql();
			 			utilitario.addUpdate("tab_planificacion_hxe,sel_empleado_asignacion,tab_planificacion_hxe_observacion");
			 			}
	}
	
	
	

	public void getEmpleadoRegistroManual(){
		empleado_registro_manual="";
		empleado_registro_manual=sel_empleado_registro_manual.getSeleccionados();
				 			
				 			utilitario.getConexion().ejecutarSql("update con_biometrico_plan_hxe set "
									+ " registro_manual_cobph =true  where ide_gtemp in("+empleado_registro_manual+") "
									+ " and ide_gemes="+mes+" and ide_geani= "+anio);
				 			System.out.println("empleados "+empleado_registro_manual);
				 			sel_empleado_registro_manual.cerrar();
				 			tab_planificacion_hxe.setCondicion("registro_manual_cobph =true and ide_gtemp in("+empleado_registro_manual+") "
							+ " and ide_gemes="+mes+" and ide_geani= "+anio);
				 			tab_planificacion_hxe.ejecutarSql();
				 			tab_planificacion_hxe_observacion.setCondicion("ide_gemes=-1");
		              		tab_planificacion_hxe_observacion.ejecutarSql();
				 			utilitario.addUpdate("tab_planificacion_hxe,sel_empleado_registro_manual,tab_planificacion_hxe_observacion");  
				 			utilitario.agregarMensaje("Se realizo actualizacion correctamente", "Se ha cambiado de estado a Registro Manual");
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
	
	
	 //Metodo distingue si es ingreso o edicion
    public void obtenerAnio(){
        anio=sel_anio.getValorSeleccionado();
        if ((anio==null ||  anio.isEmpty() || anio.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
    			return;
    		}else {
	   	sel_anio.cerrar();
	   	TablaGenerica tab_anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anios+")");
		int anioEmpleado=Integer.parseInt(sel_anio.getValorSeleccionado());   
		
		
		String sql="";
		sql="SELECT asjei.ide_asjei,  "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
				+ "mes.detalle_gemes,anio.detalle_geani  "
				+ "FROM con_biometrico_plan_hxe cbph  "
				+ "left join asi_jefe_inmediato  asjei on asjei.ide_asjei=cbph.ide_asjei "
				+ "left join gth_empleado emp on emp.ide_gtemp= asjei.ide_gtemp  "
				+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani "
				+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
				+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+sel_anio.getValorSeleccionado()+" and cbph.ide_asjei is not null "
				+ "group by asjei.ide_asjei, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, anio.detalle_geani,mes.detalle_gemes "
				+ "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 


    	System.out.println(""+sql);
		sel_empleado.setId("sel_empleado");
		sel_empleado.setSeleccionTabla(sql, "ide_asjei");
 		sel_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
 		sel_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
 		sel_empleado.setHeight("60");
 		sel_empleado.setWidth("40");
 		sel_empleado.setTitle("Seleccione Jefe Inmediato");
		sel_empleado.getBot_aceptar().setMetodo("getEmpleado");
		sel_empleado.dibujar();
		sel_empleado.redibujar();
		utilitario.addUpdate("sel_empleado");       
		
    		}	
		}
		
	
	
	
	
	 //Metodo distingue si es ingreso o edicion
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
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   "
				+ "mes.detalle_gemes,anio.detalle_geani  "
				+ " FROM con_biometrico_plan_hxe ashor "
				+ "left join gth_empleado emp on emp.ide_gtemp=ashor.ide_gtemp "
				+ "left join gen_anio anio on anio.ide_geani=ashor.ide_geani "
				+ "left join gen_mes mes on mes.ide_gemes=ashor.ide_gemes  "
				+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+sel_anio_registro_manual.getValorSeleccionado()+"  "
			//	+ "group by asjei.ide_asjei, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP, anio.detalle_geani,mes.detalle_gemes "
				+ "order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP"; 


   	System.out.println(""+sql);
		sel_empleado_registro_manual.setId("sel_empleado_registro_manual");
		sel_empleado_registro_manual.setSeleccionTabla(sql, "IDE_GTEMP");
		sel_empleado_registro_manual.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		sel_empleado_registro_manual.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		//set_empleado.getTab_seleccion().getColumna("nombre_apellido").setFiltro(true);
		sel_empleado_registro_manual.setHeight("60");
		sel_empleado_registro_manual.setWidth("40");
		sel_empleado_registro_manual.setTitle("Seleccione Empleado");
		sel_empleado_registro_manual.getBot_aceptar().setMetodo("getEmpleadoRegistroManual");
		sel_empleado_registro_manual.dibujar();
		sel_empleado_registro_manual.redibujar();
		utilitario.addUpdate("sel_empleado_registro_manual");       
		
   		}	
		}
		
    
    
    
    
	
	
	public SeleccionTabla getSel_empleado() {
		return sel_empleado;
	}

	public void setSel_empleado(SeleccionTabla sel_empleado) {
		this.sel_empleado = sel_empleado;
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

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		super.abrirListaReportes();
	}

	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		super.aceptarReporte();
	}
    
	
	
	public void devolverPlanificacion(){
		try {
		
			
		for (int i = 0; i < tab_planificacion_hxe.getTotalFilas(); i++) {
	utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set retorno_plan_cobph=true,aprobado_cobph=false where ide_cobph="+tab_planificacion_hxe.getValor(i,"ide_cobph") );				
	}
	tab_planificacion_hxe.setCondicion("ide_cobph in("+empleado_asignado+") and ide_gemes="+mes+" and ide_geani="+anio+" and ide_asjei="+jefe_inmediato_planificacion);
		tab_planificacion_hxe.ejecutarSql();
		tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
		tab_planificacion_hxe_observacion.ejecutarSql();
		utilitario.addUpdate("tab_planificacion_hxe,sel_empleado_asignacion,tab_planificacion_hxe_observacion");

			EnviarCorreoAccion(ide_gtemp_jefe_inmediato,ide_geare,1);
	con_guardar.cerrar();
	utilitario.agregarMensaje("Planificacion de HXE con observaciones", "Se ha devuelto la planficacion de HXE");
	utilitario.addUpdate("con_guardar");

		} catch (Exception e) {
			// TODO Auto-generated catch block
		System.out.println("Error al enviar plan");

		}

		
		}

	public void dibujarConfirmar(){
		if(tab_planificacion_hxe.getTotalFilas()>0){
		if(tab_planificacion_hxe_observacion.getTotalFilas()>0){
			con_guardar.setTitle("CONFIRMACION RETORNO DE PLANIFICACION DE HXE");
			con_guardar.setMessage("ESTA SEGURO DE RETORNAR DE PLANIFICACION DE HXE");
			con_guardar.getBot_aceptar().setMetodo("devolverPlanificacion");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else {
			utilitario.agregarMensajeInfo("Debe registrar una observacion", "Devolucion de planificacion Invalida");

			return;
		}
		
		}else {
			utilitario.agregarMensajeInfo("Se debe seleccionar planificacion a devolver", "Presione Consulta Planificacion");
			return;
		}
		
		
			}

	public SeleccionTabla getSel_empleado_asignacion() {
		return sel_empleado_asignacion;
	}

	public void setSel_empleado_asignacion(SeleccionTabla sel_empleado_asignacion) {
		this.sel_empleado_asignacion = sel_empleado_asignacion;
	}








	public Tabla getTab_planificacion_hxe() {
		return tab_planificacion_hxe;
	}








	public void setTab_planificacion_hxe(Tabla tab_planificacion_hxe) {
		this.tab_planificacion_hxe = tab_planificacion_hxe;
	}








	public Tabla getTab_planificacion_hxe_observacion() {
		return tab_planificacion_hxe_observacion;
	}








	public void setTab_planificacion_hxe_observacion(
			Tabla tab_planificacion_hxe_observacion) {
		this.tab_planificacion_hxe_observacion = tab_planificacion_hxe_observacion;
	}








	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_planificacion_hxe.getTotalFilas()>0) {
        	tab_planificacion_hxe_observacion.insertar();
			tab_planificacion_hxe_observacion.setValor("fecha_registro_cobpo", utilitario.getFechaActual());
			tab_planificacion_hxe_observacion.setValor("IDE_USUA", utilitario.getVariable("ide_usua"));
			tab_planificacion_hxe_observacion.setValor("IDE_ASJEI", empleado);
			tab_planificacion_hxe_observacion.setValor("ide_gemes", mes);
			tab_planificacion_hxe_observacion.setValor("IDE_geani", anio);
    	 }else {
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar una planificacion de HXE");
				return;
			}
	}








	@Override
	public void guardar() {
		// TODO Auto-generated method stub
	       if (tab_planificacion_hxe_observacion.guardar()) {
	        	if (tab_planificacion_hxe.getTotalFilas()>0) {
	            guardarPantalla();
	        	  }else {
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar una planificacion de HXE");
					return;
				}
	        }

		
	}








	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
    	tab_planificacion_hxe_observacion.eliminar();

	}

	
public void aprobarPlanificacion(){
		try {
	if (tab_planificacion_hxe.getTotalFilas()==0) {
		utilitario.agregarMensaje("No existen registros para aprobacion", "Debe seleccionar los registros que se aprobaran");
		return;
	}
	for (int i = 0; i < tab_planificacion_hxe.getTotalFilas(); i++) {
	utilitario.getConexion().ejecutarSql("UPDATE con_biometrico_plan_hxe set aprobado_tthh_cobph=true,retorno_plan_cobph=false where ide_cobph="+tab_planificacion_hxe.getValor(i,"ide_cobph") );				
	}
	
	tab_planificacion_hxe.setCondicion("ide_cobph in("+empleado_asignado+") and ide_gemes="+mes+" and ide_geani="+anio+" and ide_asjei="+jefe_inmediato_planificacion);
	tab_planificacion_hxe.ejecutarSql();
	tab_planificacion_hxe_observacion.setCondicion("ide_gemes="+mes+" and ide_geani in("+anio+") and ide_asjei="+jefe_inmediato_planificacion);
	tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("tab_planificacion_hxe,sel_empleado_asignacion,tab_planificacion_hxe_observacion");
	
	EnviarCorreoAccion(ide_gtemp_jefe_inmediato,ide_geare,1);

	utilitario.agregarMensaje("Planificacion de HXE aprobada", "Se ha realizado la aprobacion de planficacion de HXE");
	utilitario.addUpdate("con_guardar");
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
	System.out.println("Error al enviar plan");

	}
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

	
		if (tipo_rol==1) {
			enviaMailInterno(envMail, correo_envio, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,1));
			enviaMailInterno(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,1));
			enviaMailInterno(envMail, correo_tthh, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,1));

		}else {
			//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2), null);
			//util.EnviaMailInterno(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2), null);
			//util.EnviaMailInterno(envMail, correo_tthh, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2), null);
			
			enviaMailInterno(envMail, correo_envio, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2));
			enviaMailInterno(envMail, correo, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2));
			enviaMailInterno(envMail, correo_tthh, "SISTEMA DE PLANIFICACION DE HORARIOS", emailLinkEncuestaCorreo(strNombreEmpleado,area,2));
		}

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
		html+="<p> Su planificación de horas extra se encuentra con observaciones por parte de la Coordinacion de Talento Humano .</p>\n"
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
		html+="<p> Su planificación de horas extras ha sido aprobada de manera correcta.</p>\n"
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

	






public SeleccionTabla getSel_empleado_registro_manual() {
	return sel_empleado_registro_manual;
}








public void setSel_empleado_registro_manual(
		SeleccionTabla sel_empleado_registro_manual) {
	this.sel_empleado_registro_manual = sel_empleado_registro_manual;
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

	
}
