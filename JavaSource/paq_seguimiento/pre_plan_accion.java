/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.awt.TextArea;
import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.primefaces.event.SelectEvent;

import com.sun.mail.handlers.text_html;


import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.correo.EnviarCorreo;


/**
 *
 * @author DELL-USER
 */
public class pre_plan_accion extends Pantalla {

    private Tabla tab_plan_accion = new Tabla();
    private Tabla tab_recomendacion = new Tabla();
    private Tabla tab_respuesta = new Tabla();
    private Tabla tab_informe = new Tabla();
	String usuario_responsable="",empresa="",empleado="",ide_secar="",fecha_inicio_seres="",fecha_fin_seres="",descripcion_seres="";	
	boolean bandAccion=false;
	String carpeta="respuesta";
	private Confirmar con_aprobar_solicitud=new Confirmar();
	private Confirmar con_aprobar_periocidad=new Confirmar();
	private Confirmar con_aprobar_cambio_estado=new Confirmar();
    int bandNotificacionAplicada=0;
	//private TextArea  text= new TextArea();
	
	private int estadoRespuesta=0;

	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
    public pre_plan_accion() {
    	//noti();

    	
    //O	tipo respueta 
    	
    	
    	bar_botones.getBot_insertar().setRendered(false);

    	
    	con_aprobar_solicitud.setId("con_aprobar_solicitud");
    	con_aprobar_solicitud.setMessage("Esta seguro que desea cambiar de estado");
    	con_aprobar_solicitud.setTitle("CONFIRMACION CAMBIO DE ESTADO DE PLAN DE ACCION");
    	con_aprobar_solicitud.getBot_aceptar().setMetodo("aprobarPlan");
    	con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
    	agregarComponente(con_aprobar_solicitud);

    	con_aprobar_periocidad.setId("con_aprobar_periocidad");
    	con_aprobar_periocidad.setMessage("Esta seguro que desea aplicar la Periocidad");
    	con_aprobar_periocidad.setTitle("CONFIRMACION CAMBIO DE ESTADO DE PLAN DE ACCION");
    	con_aprobar_periocidad.getBot_aceptar().setMetodo("aprobarPeriocidad");
    	con_aprobar_periocidad.getBot_cancelar().setMetodo("cancelarPeriocidad");

    	agregarComponente(con_aprobar_periocidad);
    	
    	con_aprobar_cambio_estado.setId("con_aprobar_cambio_estado");
    	con_aprobar_cambio_estado.setMessage("Esta seguro que desea cambiar el estado");
    	con_aprobar_cambio_estado.setTitle("CONFIRMACION CAMBIO DE ESTADO DE PLAN DE ACCION");
    	con_aprobar_cambio_estado.getBot_aceptar().setMetodo("cambiarEstadoPlan");
    	con_aprobar_cambio_estado.getBot_cancelar().setMetodo("cancelarEstadoPlan");
    	agregarComponente(con_aprobar_cambio_estado);
    	
    	utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'seg_informe'");
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		bandAccion=false;
		String ide_serec="",ide_seinf="";
	 TablaGenerica tab_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
					+ "usu_email, ide_usua, activo_seusu "
					+ "FROM seg_usuario "
					+ "where ide_usua="+utilitario.getVariable("ide_usua")+" and activo_seusu=true ") ;
	 empresa=tab_usuario.getValor("ide_seemp");	
	 usuario_responsable=tab_usuario.getValor("ide_seusu");		 
	 ide_secar=tab_usuario.getValor("ide_secar");
	usuario_responsable=tab_usuario.getValor("ide_seusu");
	 TablaGenerica tab_asignacion;
	 //System.out.println("Cargo: "+ide_secar);
	 

		Boton bot_aprobar_plan=new Boton();
		bot_aprobar_plan.setValue("APROBAR PLAN ACCIÓN");
		bot_aprobar_plan.setMetodo("dibujarConformidad");
		
		Boton bot_aplicar_plan=new Boton();
		bot_aplicar_plan.setValue("APLICAR PLAN ACCIÓN");
		bot_aplicar_plan.setMetodo("aplicarPlan");
	 
	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
			bar_botones.agregarComponente(new Etiqueta("BIENVENIDO A LA APROBACION DE PLAN DE ACCION"));
			bar_botones.agregarBoton(bot_aprobar_plan);
			//bar_botones.agregarBoton(bot_aplicar_plan);

			tab_asignacion=utilitario.consultar("select plan.ide_sepla,reco.ide_serec,inf.ide_seinf,asi.ide_seemp   "
					+ "from seg_plan_accion  plan "
					+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf "
					+ "left join seg_recomendacion reco on reco.ide_serec=plan.ide_serec "
					+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=reco.ide_serec  "
					//+ "where asi.ide_seemp in("+empresa+")  "
					+ "group by plan.ide_sepla,reco.ide_serec,inf.ide_seinf,asi.ide_seemp   "
					+ "order by plan.ide_sepla asc");
			
		
			tab_asignacion.imprimirSql();
			if (tab_asignacion.getTotalFilas()>0) {
				for (int i = 0; i < tab_asignacion.getTotalFilas(); i++) {
					if (tab_asignacion.getTotalFilas()==1) {
						ide_serec=tab_asignacion.getValor(i,"ide_serec");
						ide_seinf=tab_asignacion.getValor(i,"ide_seinf");
					}else if((tab_asignacion.getTotalFilas()-1)==i){
						ide_serec+=tab_asignacion.getValor(i,"ide_serec");
						ide_seinf+=tab_asignacion.getValor(i,"ide_seinf");	
					}else{
					
					ide_serec+=tab_asignacion.getValor(i,"ide_serec")+",";
					ide_seinf+=tab_asignacion.getValor(i,"ide_seinf")+",";
					}
				}
			}
		 
		 
			}else{
				bar_botones.agregarComponente(new Etiqueta("RECUERDE QUE DEBE SELECCIONAR LA PERIOCIDAD PARA LOS PLANES APLICADOS"));

				tab_asignacion=utilitario.consultar("select plan.ide_sepla,reco.ide_serec,inf.ide_seinf,asi.ide_seemp   "
						+ "from seg_plan_accion  plan "
						+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf "
						+ "left join seg_recomendacion reco on reco.ide_serec=plan.ide_serec "
						+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=reco.ide_serec  "
						+ "where asi.ide_seemp in("+empresa+")  "
						+ "group by plan.ide_sepla,reco.ide_serec,inf.ide_seinf,asi.ide_seemp   "
						+ "order by plan.ide_sepla asc");
				
				
				
				
				tab_asignacion.imprimirSql();
				if (tab_asignacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_asignacion.getTotalFilas(); i++) {
						if (tab_asignacion.getTotalFilas()==1) {
							ide_serec=tab_asignacion.getValor(i,"ide_serec");
							ide_seinf=tab_asignacion.getValor(i,"ide_seinf");
						}else if((tab_asignacion.getTotalFilas()-1)==i){
							ide_serec+=tab_asignacion.getValor(i,"ide_serec");
							ide_seinf+=tab_asignacion.getValor(i,"ide_seinf");	
						}else{
						
						ide_serec+=tab_asignacion.getValor(i,"ide_serec")+",";
						ide_seinf+=tab_asignacion.getValor(i,"ide_seinf")+",";
						}
					}
				}
				
			} 
	

        tab_recomendacion.setId("tab_recomendacion");
       // tab_recomendacion.setIdCompleto("tab_tabulador:tab_recomendacion");
        tab_recomendacion.setTabla("seg_recomendacion", "ide_serec", 1);
        tab_recomendacion.getColumna("ide_serec").setNombreVisual("CODIGO");
        tab_recomendacion.getColumna("ide_seinf").setCombo("SELECT ide_seinf, numero_seinf || '  |  ' || asunto_seinf "
        		+ "FROM seg_informe");
        tab_recomendacion.getColumna("ide_seinf").setNombreVisual("NUM_INFORME");
        tab_recomendacion.getColumna("ide_seinf").setAncho(200);
      //  tab_recomendacion.getColumna("ide_seinf").setLongitud(150);
        //tab_recomendacion.getColumna("ide_seinf").setLongitud_control(200);
        
        
        tab_recomendacion.getColumna("ide_setir").setCombo("SELECT ide_setir, descripcion_setir "
            	+ "FROM seg_tipo_recomendacion");
        tab_recomendacion.getColumna("ide_setir").setRequerida(true);
        tab_recomendacion.getColumna("ide_setir").setNombreVisual("TIPO");
        tab_recomendacion.getColumna("ide_setir").setRequerida(true);		
        tab_recomendacion.getColumna("ide_setir").setAutoCompletar();
        tab_recomendacion.getColumna("ide_seesr").setCombo("SELECT ide_seesr, descripcion_seesr  "
        		+ "FROM seg_estado_recomendacion");
        tab_recomendacion.getColumna("ide_seesr").setAutoCompletar();
        tab_recomendacion.getColumna("ide_seesr").setRequerida(true);
        tab_recomendacion.getColumna("ide_seesr").setNombreVisual("ESTADO");
        tab_recomendacion.getColumna("ide_seesr").setRequerida(true);	
        tab_recomendacion.getColumna("numero_serec").setNombreVisual("NUMERO");
        tab_recomendacion.getColumna("asunto_serec").setNombreVisual("ASUNTO");
        tab_recomendacion.getColumna("asunto_serec").setAncho(80);
        //tab_recomendacion.getColumna("asunto_serec").set
        tab_recomendacion.getColumna("fecha_aprobacion_serec").setNombreVisual("FECHA APROBACION");
        tab_recomendacion.getColumna("fecha_inicio_serec").setNombreVisual("FECHA INICIO");
        tab_recomendacion.getColumna("fecha_fin_serec").setNombreVisual("FEECHA FIN");
        tab_recomendacion.getColumna("activo_serec").setNombreVisual("ACTIVO");
        tab_recomendacion.setLectura(true);
        tab_recomendacion.agregarRelacion(tab_plan_accion);
       // tab_recomendacion.agregarRelacion(tab_respuesta);
        tab_recomendacion.getGrid().setColumns(4);
        
        
        tab_recomendacion.setCondicion("IDE_SEREC IN("+ide_serec+")");
        tab_recomendacion.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_recomendacion);
		pat_panel1.setMensajeWarn("1.R E C O M E N D A C I Ó N");

		

		tab_plan_accion.setId("tab_plan_accion");
		//tab_plan_accion.setIdCompleto("tab_tabulador:tab_plan_accion");
		tab_plan_accion.setTabla("seg_plan_accion", "ide_sepla", 2);
		tab_plan_accion.getColumna("ide_sepla").setNombreVisual("CODIGO");
		tab_plan_accion.getColumna("ide_seusu").setVisible(false);
		tab_plan_accion.getColumna("ide_serec").setVisible(false);
		tab_plan_accion.getColumna("ide_sepla").setOrden(1);
		tab_plan_accion.getColumna("asunto_sepla").setNombreVisual("ASUNTO");
		tab_plan_accion.getColumna("asunto_sepla").setOrden(2);
		
	
		
		tab_plan_accion.getColumna("ide_seesp").setCombo("SELECT ide_seesp, descripcion_seesp  "
        		+ "FROM seg_estado_plan_accion");
		tab_plan_accion.getColumna("ide_seesp").setRequerida(true);
		tab_plan_accion.getColumna("ide_seesp").setNombreVisual("ESTADO");
		tab_plan_accion.getColumna("ide_seesp").setOrden(3);
		tab_plan_accion.getColumna("ide_seper").setNombreVisual("PERIOCIDAD MESES");
		tab_plan_accion.getColumna("ide_seper").setCombo("SELECT ide_seper, descripcion_seper "
				+ "FROM seg_periocidad");

		tab_plan_accion.getColumna("ide_seper").setRequerida(true);
		tab_plan_accion.getColumna("ide_seper").setOrden(4);
		//tab_plan_accion.getColumna("ide_seper").setMetodoChange("validarTipoAccion");


		tab_plan_accion.getColumna("ide_seemp").setCombo("SELECT ide_seemp, descripcion_seemp, sigla_seemp FROM seg_empresa where  activo_seemp=true");
		tab_plan_accion.getColumna("ide_seemp").setNombreVisual("AREA ASIGNADA");;
		tab_plan_accion.getColumna("ide_seemp").setOrden(6);
		tab_plan_accion.getColumna("ide_seemp").setMetodoChange("guardarEmpresa");

		
		
		
		

		tab_plan_accion.getColumna("observacion_sepla").setNombreVisual("OBSERVACION PLAN ACCION");
		tab_plan_accion.getColumna("observacion_sepla").setOrden(7);
		
		tab_plan_accion.getColumna("fecha_registro_sepla").setOrden(8);
		tab_plan_accion.getColumna("fecha_registro_sepla").setNombreVisual("FECHA REGSTRO");
		tab_plan_accion.getColumna("fecha_registro_sepla").setValorDefecto(utilitario.getFechaActual());
		tab_plan_accion.getColumna("fecha_registro_sepla").setLectura(true);
		tab_plan_accion.getColumna("fecha_aprobacion_sepla").setNombreVisual("FECHA APLICA PLAN ACCION");
		tab_plan_accion.getColumna("fecha_aprobacion_sepla").setOrden(9);
		tab_plan_accion.getColumna("fecha_aprobacion_sepla").setLectura(true);

	
		
		tab_plan_accion.getColumna("activo_sepla").setCheck();
		tab_plan_accion.getColumna("activo_sepla").setValorDefecto("true");
		tab_plan_accion.getColumna("activo_sepla").setNombreVisual("ACTIVO");
		tab_plan_accion.getColumna("activo_sepla").setLectura(true);
		tab_plan_accion.getColumna("activo_sepla").setOrden(10);

		tab_plan_accion.getColumna("ide_seinf").setVisible(false);
		tab_plan_accion.getColumna("envio_plan").setVisible(false);

		
		 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
				tab_plan_accion.getColumna("porcentaje_sepla").setLectura(false);
				tab_plan_accion.getColumna("ide_seper").setLectura(false);
				tab_plan_accion.getColumna("ide_seesp").setLectura(false);
				tab_plan_accion.getColumna("ide_seesp").setMetodoChange("validarAccion");

				tab_plan_accion.getColumna("asunto_sepla").setLectura(false);
				tab_plan_accion.getColumna("ide_seemp").setLectura(false);
				tab_plan_accion.getColumna("observacion_sepla").setLectura(false);
				tab_plan_accion.getColumna("descripcion_repro_sepla").setLectura(false);
				tab_plan_accion.getColumna("fecha_repro_sepla").setLectura(false);
				tab_plan_accion.getColumna("reprogramacion_parcial_sepla").setLectura(false);
				tab_plan_accion.getColumna("reprogramacion_total_sepla").setLectura(false);

				tab_plan_accion.getColumna("fecha_inicio_control_periocidad_sepla").setOrden(5);
				tab_plan_accion.getColumna("fecha_inicio_control_periocidad_sepla").setNombreVisual("FECHA CONTROL PERIOCIDAD");
				tab_plan_accion.getColumna("fecha_inicio_control_periocidad_sepla").setValorDefecto(utilitario.getFechaActual());				
		 }else {
				tab_plan_accion.getColumna("porcentaje_sepla").setLectura(true);
				tab_plan_accion.getColumna("ide_seper").setLectura(true);
				tab_plan_accion.getColumna("ide_seesp").setLectura(true);
				tab_plan_accion.getColumna("asunto_sepla").setLectura(true);
				tab_plan_accion.getColumna("ide_seemp").setLectura(true);
				tab_plan_accion.getColumna("observacion_sepla").setLectura(false);
				tab_plan_accion.getColumna("descripcion_repro_sepla").setLectura(true);
				tab_plan_accion.getColumna("fecha_repro_sepla").setLectura(true);
				tab_plan_accion.getColumna("reprogramacion_parcial_sepla").setLectura(true);
				tab_plan_accion.getColumna("reprogramacion_total_sepla").setLectura(true);
				
				tab_plan_accion.getColumna("fecha_inicio_control_periocidad_sepla").setLectura(true);
		}

			 
			tab_plan_accion.getColumna("porcentaje_sepla").setNombreVisual("% AVANCE");
			tab_plan_accion.getColumna("porcentaje_sepla").setFormatoNumero(0);
			tab_plan_accion.getColumna("porcentaje_sepla").setOrden(20);
			
			
			tab_plan_accion.getColumna("notificacion_sepla").setLectura(true);
			
			
			
		tab_plan_accion.onSelect("actualizarRespuesta");
		
		//tab_plan_accion.agregarRelacion(tab_respuesta);
 		tab_plan_accion.setCondicion("ide_seinf="+tab_recomendacion.getValor("ide_seinf")+" and ide_serec="+tab_recomendacion.getValor("IDE_SEREC"));
 		tab_plan_accion.setCampoOrden("ide_sepla desc");
		
		tab_plan_accion.dibujar();
        PanelTabla pat_panel2= new PanelTabla();
        pat_panel2.setPanelTabla(tab_plan_accion);
		pat_panel2.setMensajeWarn("2. PLAN_ACCION");

    	
    	
		tab_respuesta.setId("tab_respuesta");
		tab_respuesta.setIdCompleto("tab_tabulador:tab_respuesta");
		tab_respuesta.setTabla("seg_respuesta", "ide_seres", 3);
		tab_respuesta.setId("tab_respuesta");
		//tab_respuesta.setIdCompleto("tab_tabulador:tab_respuesta");
		tab_respuesta.setTabla("seg_respuesta", "ide_seres", 5);
		tab_respuesta.getColumna("ide_seres").setNombreVisual("CODIGO");
		tab_respuesta.getColumna("ide_seres").setOrden(1);
		tab_respuesta.getColumna("ide_serec").setVisible(false);
		tab_respuesta.getColumna("ide_setre").setNombreVisual("TIPO");
		if (ide_secar.equals("1") || empresa.equals("1") || empresa.equals("2")) {
			tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
					+ "FROM seg_tipo_respuesta"); 
			tab_respuesta.getColumna("ide_setre").setLectura(false);
			tab_respuesta.getColumna("ide_setre").setMetodoChange("validarTipoAccion");
			
		}else {
			tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
					+ "FROM seg_tipo_respuesta");//+ "FROM seg_tipo_respuesta where ide_setre=1 "); 
			tab_respuesta.getColumna("ide_setre").setLectura(true);
		}
		tab_respuesta.getColumna("ide_setre").setOrden(2);
	
		tab_respuesta.getColumna("descripcion_seres").setNombreVisual("ACTIVIDADES_PROGRAMADAS");
		tab_respuesta.getColumna("descripcion_seres").setRequerida(true);
		tab_respuesta.getColumna("descripcion_seres").setOrden(3);
		tab_respuesta.getColumna("medio_verificacion_seres").setNombreVisual("MEDIO_VERIFICACION");
		tab_respuesta.getColumna("medio_verificacion_seres").setRequerida(true);
		tab_respuesta.getColumna("medio_verificacion_seres").setOrden(4);
		tab_respuesta.getColumna("fecha_desde_seres").setNombreVisual("FECHA.INICIO");
		tab_respuesta.getColumna("fecha_desde_seres").setRequerida(true);		
		tab_respuesta.getColumna("fecha_desde_seres").setOrden(5);
		tab_respuesta.getColumna("fecha_hasta_seres").setNombreVisual("FECHA.FIN");
		tab_respuesta.getColumna("fecha_hasta_seres").setRequerida(true);
		tab_respuesta.getColumna("fecha_hasta_seres").setOrden(6);
		tab_respuesta.getColumna("nombre_archivo_seres").setLectura(true);
		tab_respuesta.getColumna("nombre_archivo_seres").setNombreVisual("NOMBRE_ADJUNTO");
		tab_respuesta.getColumna("nombre_archivo_seres").setOrden(7);
		tab_respuesta.getColumna("mecanismo_reporte_seres").setUpload(carpeta);
		tab_respuesta.getColumna("mecanismo_reporte_seres").setNombreVisual("ADJUNTO");
		tab_respuesta.getColumna("mecanismo_reporte_seres").setOrden(8);

		


		//tab_respuesta.getColumna("mecanismo_reporte_seres").setRequerida(true);
		
	//	tab_respuesta.getColumna("mecanismo_reporte_seres").setMetodoChange(metodo);("validarEstadoAsignacion");

		tab_respuesta.getColumna("ide_seemp").setNombreVisual("AREA");		
        tab_respuesta.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
        		+ "FROM seg_empresa where  activo_seemp=true");
		tab_respuesta.getColumna("ide_seemp").setLectura(true);	
		tab_respuesta.getColumna("ide_seemp").setVisible(false);
		tab_respuesta.getColumna("ide_seemp").setOrden(9);
		tab_respuesta.getColumna("ide_gtemp_responsable").setNombreVisual("USUARIO_RESPONSABLE");		
        tab_respuesta.getColumna("ide_gtemp_responsable").setCombo("SELECT  emp.ide_gtemp, "
        		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
        		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
        		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
        		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
        		+ "FROM gth_empleado emp  "
        	//	+ "where emp.activo_gtemp=true  "
        		+ "order by APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP");
        tab_respuesta.getColumna("ide_gtemp_responsable").setAutoCompletar();
		tab_respuesta.getColumna("ide_gtemp_responsable").setOrden(10);
		tab_respuesta.getColumna("observacion_seres").setOrden(11);


		tab_respuesta.getColumna("ide_seinf").setVisible(false);
		tab_respuesta.getColumna("ide_sepla").setVisible(false);


		tab_respuesta.getColumna("descripcion_repro_seres").setNombreVisual("DESCRIP-REPRO");
		tab_respuesta.getColumna("descripcion_repro_seres").setLectura(true);
		tab_respuesta.getColumna("descripcion_repro_seres").setOrden(12);
		
		tab_respuesta.getColumna("reprogramacion_seres").setNombreVisual("RE-PROGRAMACION");
		tab_respuesta.getColumna("reprogramacion_seres").setLectura(true);
		tab_respuesta.getColumna("reprogramacion_seres").setOrden(13);

		tab_respuesta.getColumna("ide_seusu").setNombreVisual("USUARIO REGISTRO");		
        tab_respuesta.getColumna("ide_seusu").setCombo("SELECT  segus.ide_seusu,segus.login_seusu "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "order by segus.ide_seusu");
        tab_respuesta.getColumna("ide_seusu").setAutoCompletar();
        tab_respuesta.getColumna("ide_seusu").setLectura(true);
		tab_respuesta.getColumna("ide_seusu").setOrden(14);
		
		//tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setNombreVisual("FEC_INICIO_REPRO");
	//	tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setLectura(true);
		//tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setOrden(15);

		//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setNombreVisual("FEC_FIN_REPRO");
		//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setLectura(true);
		//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setOrden(16);


		
		tab_respuesta.getColumna("hora_seres").setNombreVisual("HORA");
		tab_respuesta.getColumna("hora_seres").setValorDefecto(utilitario.getHoraActual());
		tab_respuesta.getColumna("hora_seres").setLectura(true);
		tab_respuesta.getColumna("hora_seres").setOrden(15);
		tab_respuesta.getColumna("fecha_seres").setNombreVisual("FEC_REG_ACTIVIDAD");
		tab_respuesta.getColumna("fecha_seres").setValorDefecto(utilitario.getFechaActual());
		tab_respuesta.getColumna("fecha_seres").setLectura(true);
		tab_respuesta.getColumna("fecha_seres").setOrden(16);
		
		
		tab_respuesta.getColumna("activo_seres").setCheck();
		tab_respuesta.getColumna("activo_seres").setValorDefecto("true");
		tab_respuesta.getColumna("activo_seres").setNombreVisual("ACTIVO");
		tab_respuesta.getColumna("activo_seres").setOrden(17);
		tab_respuesta.getColumna("activo_seres").setLectura(true);
		
		
		
			tab_respuesta.getColumna("reprogramacion_seres").setLectura(false);
			tab_respuesta.getColumna("reprogramacion_seres").setCheck();
			tab_respuesta.getColumna("reprogramacion_seres").setValorDefecto("false");
			tab_respuesta.getColumna("reprogramacion_seres").setNombreVisual("REPROGRAMAR");
			tab_respuesta.getColumna("reprogramacion_seres").setMetodoChange("validarTipoAccion");
			tab_respuesta.getColumna("aceptar_repro_seres").setVisible(false);
			tab_respuesta.getColumna("aceptar_repro_seres").setValorDefecto("false");
			tab_respuesta.getColumna("ide_seres_anterior").setVisible(false);
			tab_respuesta.getColumna("mecanismo_reporte_seres_enlace").setVisible(false);

			tab_respuesta.setCondicion("IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" AND IDE_SEPLA="+tab_plan_accion.getValor("IDE_SEPLA")+" and reprogramacion_seres=false");
			tab_respuesta.setCampoOrden("fecha_desde_seres asc");

			tab_respuesta.setLectura(true);
	
		tab_respuesta.dibujar();
        PanelTabla pat_panel3= new PanelTabla();
        pat_panel3.setPanelTabla(tab_respuesta);
		pat_panel3.setMensajeWarn("ACCIONES");


    	
    	
    	
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel3, "40%", "20%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {

   	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {

        if (tab_plan_accion.guardar()) {
            guardarPantalla();
        }
        
   	 }else {
		utilitario.agregarMensaje("No se puede guardar", "Usted no contiene esta opcion");
		return;
	}
    }

    @Override
    public void eliminar() {
  //      utilitario.getTablaisFocus().eliminar();
    }

	public Tabla getTab_plan_accion() {
		return tab_plan_accion;
	}

	public void setTab_plan_accion(Tabla tab_plan_accion) {
		this.tab_plan_accion = tab_plan_accion;
	}

	public Tabla getTab_recomendacion() {
		return tab_recomendacion;
	}

	public void setTab_recomendacion(Tabla tab_recomendacion) {
		this.tab_recomendacion = tab_recomendacion;
	}

	public Tabla getTab_respuesta() {
		return tab_respuesta;
	}

	public void setTab_respuesta(Tabla tab_respuesta) {
		this.tab_respuesta = tab_respuesta;
	}

	public Tabla getTab_informe() {
		return tab_informe;
	}

	public void setTab_informe(Tabla tab_informe) {
		this.tab_informe = tab_informe;
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarTablas();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarTablas();

	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizarTablas();

	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarTablas();

	}

	
	public void actualizarTablas(){

	  	 try {
	  		//validarAccionInvalida();
			//if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
	 		tab_plan_accion.setCondicion("ide_seinf="+tab_recomendacion.getValor("ide_seinf")+" and ide_serec="+tab_recomendacion.getValor("IDE_SEREC"));
	 		TablaGenerica tab_plan_accion_temp=utilitario.consultar("Select * from seg_plan_accion where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_seinf="+tab_recomendacion.getValor("IDE_SEINF")+" order by ide_sepla desc");
	 				
	 		if (tab_plan_accion_temp.getTotalFilas()>0) {
	 			//System.out.println("ide_serec  "+tab_recomendacion.getValor("ide_serec"));
	 			//System.out.println("ide_seinf  "+tab_recomendacion.getValor("ide_seinf"));
				
	 			 tab_respuesta.setCondicion("ide_sepla="+tab_plan_accion_temp.getValor("ide_sepla")+" and IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC"));	
		 		//System.out.println("ide_sepla_  "+tab_plan_accion_temp.getValor("ide_sepla"));

			}
	 		
	  	
	  		 tab_plan_accion.ejecutarSql();
	
				tab_respuesta.ejecutarSql();


		//		tab_respuesta.setCondicion("IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" AND IDE_SEPLA="+tab_plan_accion.getValor("IDE_SEPLA")+" and activo_seres=true");
				
				
				
				utilitario.addUpdate("tab_respuesta");

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		}
	
	
	
	public void guardarEmpresa(){
		
		try {
			
			String ide_sepla ="",ide_seesp="",ide_setre="",ide_setre_aprobado="",ide_setre_aplicado="",ide_setre_incumplida="";
			ide_sepla =tab_plan_accion.getValor("ide_sepla");
			ide_seesp =tab_plan_accion.getValor("ide_seesp");
			ide_setre_aprobado="6";
			ide_setre_aplicado="5";
			ide_setre_incumplida="8";

			int cont_aprobado=0,cont_aplicado=0,cont_aprobado_=0,cont_aplicado_=0,cont_incumplido=0,cont_incumplido_=0;
			if (ide_sepla==null ) {
				utilitario.agregarMensaje("No ha seleccionado un plan de accion valido", "Seleccione registro valido");
				return;
			}
			
			if (ide_seesp.equals("1") || ide_seesp.equals("2") || ide_seesp.equals("5") ||  ide_seesp.equals("8")) {
				TablaGenerica tab_respuestas_estado=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
						+ "ide_seemp, ide_setre, activo_seres,  "
						+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres,  "
						+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres  "
						+ "FROM seg_respuesta "
						+ "where ide_sepla="+tab_plan_accion.getValor("ide_sepla")+" and ide_setre not in(3,4)");
				
				if (tab_respuestas_estado.getTotalFilas()>0) {
					for (int i = 0; i < tab_respuestas_estado.getTotalFilas(); i++) {
						if (ide_setre_aprobado.equals(tab_respuestas_estado.getValor(i,"ide_setre"))) {
							cont_aprobado++;
						}
					}
				
				
				if (cont_aprobado==tab_respuestas_estado.getTotalFilas()) {
					utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seemp="+tab_plan_accion.getValor("ide_seemp")+" where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
					tab_plan_accion.actualizar();
					utilitario.agregarMensaje("Se ha guardado correctamente", "Registro Actualizado");

				}else {
					for (int i = 0; i < tab_respuestas_estado.getTotalFilas(); i++) {
						if (ide_setre_aplicado.equals(tab_respuestas_estado.getValor(i,"ide_setre"))) {
							cont_aplicado++;
						}
					}
					
					if (cont_aplicado==tab_respuestas_estado.getTotalFilas()) {
						utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seemp="+tab_plan_accion.getValor("ide_seemp")+" where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
						tab_plan_accion.actualizar();
						utilitario.agregarMensaje("Se ha guardado correctamente", "Registro Actualizado");
					}
					else {
					
						for (int i = 0; i < tab_respuestas_estado.getTotalFilas(); i++) {
							if (ide_setre_incumplida.equals(tab_respuestas_estado.getValor(i,"ide_setre"))) {
								cont_incumplido++;
							}
							}
						
						
						
						int suma=cont_aprobado+cont_aplicado+cont_incumplido;
						if (suma==tab_respuestas_estado.getTotalFilas()) {
							utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seemp="+tab_plan_accion.getValor("ide_seemp")+" where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
							tab_plan_accion.actualizar();
							utilitario.agregarMensaje("Se ha guardado correctamente", "Registro Actualizado");
						}else {
							utilitario.agregarMensaje("Registro no puede ser actualizado", "Los registros deben estar en APROBADO O APLICADA");
							tab_plan_accion.actualizar();
						}
						
					}

					
					
				}
				}
				
			}else {
				utilitario.agregarMensaje("Registro no puede ser actualizado", "Los registros deben estar en APROBADO O APLICADA");
				tab_plan_accion.actualizar();
				return;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		}
		
	
	
	
	
	
	public void dibujarConformidad(){
		con_aprobar_solicitud.dibujar();
	}
	
	public void dibujarPeriocidad(){
		con_aprobar_periocidad.dibujar();
	}
	
	
	public void aprobarPlan (){
		
		if (tab_plan_accion.getTotalFilas()>0) {
			TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_serec,ide_seinf from seg_respuesta where ide_sepla in("+tab_plan_accion.getValor("ide_sepla")+") and ide_setre=1");
			if (tab_recomendaciones_plan.getTotalFilas()>0) {
				utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=6 where ide_sepla="+tab_plan_accion.getValor("ide_sepla")+" and ide_setre=1");

				TablaGenerica tab_informe =utilitario.consultar("select * from seg_informe where ide_seinf="+tab_recomendacion.getValor("ide_seinf"));
				TablaGenerica tab_respuesta_aprobadas=utilitario.consultar("select * from seg_respuesta where ide_setre=6 and ide_sepla="+tab_plan_accion.getValor("ide_sepla"));

					     TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
							+ "SEG_ASIGNACION ASI "
							+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
							+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
							+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
							+ "WHERE asi.IDE_seinf="+tab_recomendacion.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

					if (tab_empleado.getTotalFilas()>0) {
					try {
						for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
						if (tab_empleado.getTotalFilas()>0) {
		//					EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_recomendacion.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), tab_respuesta_aprobadas.getValor("IDE_SERES"), tab_empleado.getValor(i,"ide_gtemp"), 1);					
						}
						}
						con_aprobar_solicitud.cerrar();
						utilitario.addUpdate("tab_plan_accion,tab_respuesta,con_aprobar_solicitud");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("No se pudo enviar el correo de aprobacion de plan de accion");	}

				}else {
					
				}
				
				tab_respuesta.actualizar();
				utilitario.addUpdate("tab_respuesta");
				utilitario.agregarMensajeInfo("El Plan de Accion ha sido aprobado correctamente", "Se ha aprobado el plan de accion");
				
			}else {
				utilitario.agregarMensajeInfo("No se ha encontrado acciones validas", "El Plan de Accion no contiene acciones en estado de Avance");
				return;
			}
			
				
		}else {
			utilitario.agregarMensajeInfo("No se ha encontrado un Plan de Accion", "Por favor verifique el estado del Plan de Accion");
			return;
		}
		
	}
	
	
	public void aplicarPlan (){
		
		if (tab_plan_accion.getTotalFilas()>0) {
			TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_serec,ide_seinf from seg_recomendacion where ide_sepla in("+tab_plan_accion.getValor("ide_sepla")+")");
			if (tab_recomendaciones_plan.getTotalFilas()>0) {
				utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
				utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seesp=1,porcentaje_sepla=100 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
			
				TablaGenerica tab_asignacion_temp_=utilitario.consultar("select  ide_seasi, ide_serec, ide_seusu, ide_seemp, ide_seinf, ide_periodo_nuevo_seasi "
				  		+ "FROM seg_asignacion "
				  		+ "where ide_serec ="+tab_recomendacion.getValor("ide_serec") +" "
				  		+ "and ide_seinf="+tab_recomendacion.getValor("ide_seinf"));
					
				
				TablaGenerica TabUsuario=null; 
				for (int i = 0; i < tab_asignacion_temp_.getTotalFilas(); i++) {
				TabUsuario=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,usua.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where ide_seemp="+tab_asignacion_temp_.getValor(i,"ide_seemp")+" and segus.activo_seusu=true "
		        		+ "order by segus.ide_seusu");
				
				if (TabUsuario.getTotalFilas()>0) {				
				try {
				//	EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_recomendacion.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), "", TabUsuario.getValor(i,"ide_gtemp"), 15);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				}
				
				utilitario.addUpdate("tab_respuesta,tab_plan_accion");
				utilitario.agregarMensajeInfo("El Plan de Accion ha sido aplicado correctamente", "Se ha aprobado el plan de accion");
				
			}else {
				utilitario.agregarMensajeInfo("El Plan de Accion se encuentra vacio", "El Plan de Accion no contiene acciones registradas");
				return;
			}
			
				
		}else {
			utilitario.agregarMensajeInfo("No se ha encontrado un Plan de Accion", "Por favor verifique el estado del Plan de Accion");
			return;
		}
		
	}
	
	

public void aceptarAprobarSolicitudTalento(){
int ide_sepla=0,ide_sepla_=0;	
	try {//NOTIFICACION DE RECHAZO

		TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
	        		+ " order by segus.ide_seusu");
		tabEmpleado.imprimirSql();
		
		if (estadoRespuesta==4) {
			tab_respuesta.guardar();
			
			guardarPantalla();
		ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 4);
			
		utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A RECHAZADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
		//NOTIFICACION DE APLICADA
		}else if (estadoRespuesta==5) {
			//CAMBIO DE ESTADO APLICADO POR ADMINISTRADOR
			tab_respuesta.guardar();
			
			guardarPantalla();
			
			ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			//EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 5);
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A APLICADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			//tab_respuesta.guardar();
			
			//guardarPantalla();
			String mensajeCorreo=tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec");
		//	actualizaPlanAccion(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),mensajeCorreo,5);
			
			}else if (estadoRespuesta==3) {//NOTIFICACION
				tab_respuesta.guardar();	
				guardarPantalla();
				ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
		//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 3);
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO APROBADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
			else if (estadoRespuesta==6) {//CAMBIO DE ESTADO APROBADA
				tab_respuesta.guardar();	
				guardarPantalla();
				ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	//		EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 6);
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO APROBADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
			else if (estadoRespuesta==7) {
			//Reprogramacion
			tab_respuesta.guardar();	
			guardarPantalla();
			ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	//		EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 7);
			//actualizaPlanAccion(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),"",7);
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO APROBADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
		
			else if (estadoRespuesta==8) {
				//Reprogramacion
				tab_respuesta.guardar();	
				guardarPantalla();
				ide_sepla=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));//(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 8);
				//actualizaPlanAccion(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),"",8);
					
				utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A INCUMPLIDA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
				}
		
			
			else if (estadoRespuesta==11){
				//REPROGRAMACION
			tab_respuesta.guardar();	
			guardarPantalla();
		//	ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
		//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Accion Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 11);

		}
	
			
			con_aprobar_solicitud.cerrar();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("ERROR CAMBIO DE ESTADO DE RESPUESTA");
	}
	
}



public void EnviarCorreoAccion(String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp,int tipo_rol) throws Exception{
	try {
	TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
	String correo=tab_correo.getValor("detalle_gtcor");//"juan.ayerve@emgirs.gob.ec";//
	TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
			+ "clave_corr from sis_correo where ide_corr=2"); 
	String smtp_correo="mail.emgirs.gob.ec"; 
	String puertoEnvio="25"; 
	String correo_envio="srecomendaciones@emgirs.gob.ec";
	String usuario_envio="srecomendaciones"; 
	String clave_correo="R3comendaciones.2022";
			
	//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
	EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
			String strNombreEmpleado="";
			strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				
	try {

		TablaGenerica tab_tipo_respuesta=utilitario.consultar("SELECT ide_setre, descripcion_setre "
					+ "FROM seg_tipo_respuesta where ide_setre="+tab_respuesta.getValor("ide_setre"));
		String descripcion_setre=tab_tipo_respuesta.getValor("descripcion_setre");
		
		//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol), null);
		
		String str_mail=correo_envio;
		envMail.setAsunto("SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP");
		envMail.setCuerpoHtml(emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol));
		envMail.setPara(str_mail);
		pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
		
		if(obj.getRespuesta())
		{
			utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
		}
		else
			utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
		//no usar EnviaMailInterno
		EnviaMailInternoAccion(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol), null,strNombreEmpleado,
		ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol);
		System.out.println("Enviando Correo.........");

		} catch (Exception e) {
		System.out.println("Error en el envio de correo"+e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
		}

			} 		


public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_rol) {
	 
	String html="";
	/* ="<p>Estimado "+strNombreEmpleado+ ", "
             + "</p>\n"
             + "<p>&nbsp;</p>\n";*/
    
	if (tipo_rol==1) {
		//APROBACION PLAN
		TablaGenerica tab_informe=utilitario.consultar("SELECT ide_seinf, ide_sesui, ide_seesi, ide_senii, numero_seinf, asunto_seinf  "
				+ "FROM seg_informe "
				+ "where ide_seinf="+tab_recomendacion.getValor("ide_seinf")+"");
	//Aprobacion de Plan de Accion
		html+="<p> Estimado(a):  "+strNombreEmpleado+"</p>\n"
				+ "<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
				+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
				+ "y en representaci&iocuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento "
				+ "mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de "
				+ "su seguimiento y cumplimiento. </p>\n"
				+ "La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones "
				+ "emitidas por la Contralor&iacutea General del Estado vigente, se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
				+ "<p>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>"
				+ "<p>&nbsp;a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. </p> "
				+ "<p>&nbsp;b)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan. </p> "
				+ "<p>&nbsp;c)	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones. </p> "
				+ "<p>&nbsp;d)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema. </p> "
				+ "<p>&nbsp;e)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.</p> "
				+ "<p>&nbsp;f)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. </p>\n"

				+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se acepta y dispongo a Usted la ejecuci&oacuten de las actividades "
				+ "y reporte de los avances establecidos en el plan de acci&oacuten  de la Recomendaci&oacuten No. "+recomendacion+"  del informe No.  "+informe+" conforme a los tiempos establecidos.  </p> "
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
		
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	    //System.out.println("aprobado: "+html);
	}
	else if (tipo_rol==2) {
	//Aplicacion de Plan de Accion
        html+="<p>Estimado Delegado(a) "+strNombreEmpleado+" del Seguimiento de Recomendación"
      	+ "En atención a la notificación y disposición de cumplimiento de la recomendación N."+recomendacion+" , me permito informar que se ha ingresado "
      	+ "el plan de acción para dar cumplimiento a la recomendación, solicito la aceptación del mismo. "
       //	+ "Notificamos mediante la presente que se ha registrado la siguiente accion : <strong>"+acciones+" .</strong> en el Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+"  para su aprobacion</p>\n"
        + "<p>&nbsp;</p>\n" 
        + "<p>Recuerde que debe registrar la documentacion necesaria que valide la accion registrada.</p>\n"
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
        }
	
	
	
	
	else if (tipo_rol==3) {
	//Tipo rol adjunto accion
		
        html+="<p>Notificamos mediante la presente que se ha remitido el comunicado de la accion : <strong>"+acciones+" .</strong> del Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
       + "<p>&nbsp;</p>\n" 
        + "<p>Recuerde que toda disposicion debe ser cumplida </p>\n"
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";

	}	
	else if (tipo_rol==4) {
	//Plan de accion
		html+="<p>Notificamos mediante la presente que se ha que se ha rechazado la:  "+acciones +", de la recomendación Nro. "+recomendacion+", del informe Nro. "+informe+" .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	             + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion en un lapso de 5 dias habiles</p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	
	}else if (tipo_rol==5) {
	//Plan de accion
		html+="<p> Notificamos mediante la presente que se ha cambiado el estado de la accion:  "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
  		             + "<p>&nbsp;</p>\n" 
  		             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
  		             + "<p>&nbsp;</p>\n"
  		             + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
			
	}else if (tipo_rol==6) {
	//Plan de accion
        html+="<p>Notificamos mediante la presente que se ha cambiado de estado la accion : <strong>"+acciones+" .</strong> del Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
       + "<p>&nbsp;</p>\n" 
        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	}
	
	else if (tipo_rol==7) {
		//Reprogramcion de actividad Administrador
	        html+="<p>Notificamos mediante la presente se informa que se ha reprogramado la accion "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
		    html+="<table style=\"height: 144px;\" width=\"571\">\n"
		             + "<tbody>\n"
		             + "<tr>\n"
		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		             + "<td width=\"476\">\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		             + "</tr>\n"
		             + "</tbody>\n"
		             + "</table>";
		}
	
	
	else if (tipo_rol==8) {
		//Reprogramcion de actividad Administrador
	        html+="<p>Notificamos mediante la presente se informa que se ha imcumplido con la accion "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
		    html+="<table style=\"height: 144px;\" width=\"571\">\n"
		             + "<tbody>\n"
		             + "<tr>\n"
		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		             + "<td width=\"476\">\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		             + "</tr>\n"
		             + "</tbody>\n"
		             + "</table>";
		}
	
	else if (tipo_rol==11) {
		//Plan de accion Reprogramado Accion 
	        html+="<p>Notificamos mediante la presente que se ha reprogramado la "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
	        
		    html+="<table style=\"height: 144px;\" width=\"571\">\n"
		             + "<tbody>\n"
		             + "<tr>\n"
		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		             + "<td width=\"476\">\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACI&OacuteN DE PROCESOS Y PROYECTOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		             + "</tr>\n"
		             + "</tbody>\n"
		             + "</table>";
		}

	
	else if (tipo_rol==12) {
		//Plan de accion EN ESTADO DE APLICADA
		html+="<p> Estimado(a) responsable del cumplimiento de la recomendaci&oacuten:</p>\n"
	       + "<p>&nbsp;</p>\n" 
	             + "<p>Como Delegado de la Gerencia General y en funci&oacuten a lo que establece el Procedimiento interno de Seguimiento y Control de "
	             + "Recomendaciones emitidas por la Contralor&iacutea General del Estado , se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
		         + "</p>\n"
		         +"<p> El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </p>\n"
		         +"<p><strong>Fase de elaboraci&oacuten del plan de acci&oacuten </strong></p>\n"
		         + "<p>a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. "
		         + "<p>b)	Establecer un plan de acci&oacuten por cada recomendaci&oacuten en el sistema e informar mediante memorando al delegado de la M&aacutexima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan. "
		         +"<p><strong>Fase de reporte al cumplimiento al plan de acci&oacuten </strong></p>\n"
		         + "<p>c)	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  "
		         + "<p>d)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  "
		         + "<p>e)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  "
		         + "<p>f)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  "
		         + "<p>g)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. "
		         + "</p>\n"
		         + "<p>Con este antecedente, y en calidad de Delegado(a) del Seguimiento al Cumplimiento de las Recomendaciones se aplica el plan de acci&oacuten para la recomendacion No. "+recomendacion+"  del informe No.  "+informe+"  "
		         + "dispongo a Usted la ejecuci&oacuten de las actividades y reporte de los avances establecidos en el plan de acci&oacuten conforme a los tiempos establecidos.  </p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
		}
	
	else if (tipo_rol==13) {
		//Plan de accion EN ESTADO DE REPROGRAMADO
	        html+="<p>Notificamos mediante la presente que se ha reprogramado todo el plan de accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
	
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
             + "<tbody>\n"
             + "<tr>\n"
             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
             + "<td width=\"476\">\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
             + "</tr>\n"
             + "</tbody>\n"
             + "</table>";
		    
		    
		    
		}else if (tipo_rol==14) {
		//Plan de accion INCUMPLIDA
	        html+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
	        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
	        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y "
	        		+ "en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
	        		+ "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
	        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a General "
	        		+ "del Estado vigente, me permito informar que la recomendaci&oacute;n se encuentra con estado INCUMPLIDO, por lo que se solicita dispongo a Usted la ejecuci&oacute;n de las actividades "
	        		+ "y reporte de los avances establecidos en el plan de acci&oacute;n conforme a los tiempos establecidos o en su defecto realizar la solicitud de reprogramaci&oacute;n.</p>\n";
		    html+="<table style=\"height: 144px;\" width=\"571\">\n"
		             + "<tbody>\n"
		             + "<tr>\n"
		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		             + "<td width=\"476\">\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		             + "</tr>\n"
		             + "</tbody>\n"
		             + "</table>";
		}else if (tipo_rol==15) {
			
			
			TablaGenerica tab_notificaciones=utilitario.consultar("SELECT pla.ide_sepla, pla.ide_seusu, pla.ide_serec, pla.fecha_registro_sepla, pla.fecha_aprobacion_sepla,peri.descripcion_seper,pla.fecha_inicio_control_periocidad_sepla, "
					+ "pla.porcentaje_sepla, pla.ide_seesp, pla.activo_sepla,pla.ide_seinf, "
					+ "pla.ide_seper, pla.asunto_sepla, pla.envio_plan, pla.ide_seemp, pla.observacion_sepla,peri.valor_seper "
					+ "FROM seg_plan_accion pla "
					+ "left join seg_periocidad peri on pla.ide_seper=peri.ide_seper "
					+ "where pla.activo_sepla=true "
					+ "and pla.ide_sepla="+ide_sepla+ " "
					//+ "and pla.ide_serec="+tab_resumen_reco.getValor("IDE_SEREC")+ " "
					+ "order by pla.ide_sepla");
					//+ "and pla.ide_seper!=6 order by ide_sepla");
			
			
			html+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
	        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
	        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacute;n "
	        		+ "de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones "
	        		+ "de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento.</p>\n  "
	        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a "
	        		+ "General del Estado vigente, me permito informar que conforme a las actividades y medios de verificaci&oacute;n planteados para el cumplimiento de la "
	        		+ "recomendación No. "+recomendacion+" del Informe "+informe+" se encuentra con estado APLICADA, por lo que se solicita a Usted continuar con el cumplimiento de la misma conforme al "
	        		+ "mecanismo de reporte ("+tab_notificaciones.getValor("descripcion_seper")+"), tiempo que corre a partir del "
	        		+ "mes de ( sumar periodicidad XXX – esto va en funci&oacute;n a la fecha de su cumplimiento hasta el 02 de cada mes </p>\n";
		    html+="<table style=\"height: 144px;\" width=\"571\">\n"
		             + "<tbody>\n"
		             + "<tr>\n"
		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		             + "<td width=\"476\">\n"
		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
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

public String EnviaMailInternoAccion(EnvioMail enviaMail, String mailReceptor, String asunto, String cuerpo, File filearchivo,String strNombreEmpleado ,
		   String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_correo) throws Exception
   {
    String mensaje = "";
     
     Properties props = new Properties();
     props.put("mail.smtp.user", enviaMail.getUsuarioEnvio());
     props.put("mail.smtp.host", enviaMail.getServidorSMTP());
     props.put("mail.smtp.port", enviaMail.getPuertoEnvio());
     props.put("mail.smtp.socketFactory.fallback", "false");
     props.put("mail.smtp.auth", "false");
     props.put("mail.smtp.starttls.enable", Boolean.valueOf(false));
     props.put("mail.smtp.socketFactory.port", enviaMail.getPuertoEnvio());
     props.put("mail.smtp.ssl.trust", enviaMail.getServidorSMTP());
     try
     {
      Authenticator auth = new autentificadorParametersSMTP(enviaMail.getUsuarioEnvio(), enviaMail.getClaveCorreo());
      Session session = Session.getInstance(props, auth);
       BodyPart texto = new MimeBodyPart();
       MimeMultipart multiParte = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText="";
         /*  	 ="<p>Estimado "+strNombreEmpleado+ ", "
                        + "</p>\n"
                        + "<p>&nbsp;</p>\n";*/
               
        if (tipo_correo==1) {
     		//Tipo rol cambio estado accion
        		//Aprobacion de Plan de Accion
        	TablaGenerica tab_informe=utilitario.consultar("SELECT ide_seinf, ide_sesui, ide_seesi, ide_senii, numero_seinf, asunto_seinf  "
    				+ "FROM seg_informe "
    				+ "where ide_seinf="+tab_recomendacion.getValor("ide_seinf")+"");
    	//Aprobacion de Plan de Accion
    		htmlText+="<p> Estimado(a):  "+strNombreEmpleado+"</p>\n"
    				+ "<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
    				+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
    				+ "y en representaci&iocuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento "
    				+ "mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de "
    				+ "su seguimiento y cumplimiento. </p>\n"
    				+ "La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones "
    				+ "emitidas por la Contralor&iacutea General del Estado vigente, se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
    				+ "<p>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>"
    				+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. </p> "
    				+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan. </p> "
    				+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones. </p> "
    				+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema. </p> "
    				+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.</p> "
    				+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado.” </p>\n"

    				+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se acepta y dispongo a Usted la ejecuci&oacuten de las actividades "
    				+ "y reporte de los avances establecidos en el plan de acci&oacuten  de la Recomendaci&oacuten No. "+recomendacion+"  del informe No.  "+informe+"conforme a los tiempos establecidos.  </p> "
        		             + "<p>&nbsp;</p>\n"
        		             + "<p>Saludos cordiales,</p>\n";
    	    htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
    	             + "<tbody>\n"
    	             + "<tr>\n"
    	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
    	             + "<td width=\"476\">\n"
    	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
    	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
    	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
    	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
    	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
    	             + "</tr>\n"
    	             + "</tbody>\n"
    	             + "</table>";
    	    //System.out.println("aprobado: "+htmlText);
     		}
     		else if (tipo_correo==2) {
     		//Tipo rol registro de nueva accion	
     	        htmlText+="<p>Notificamos mediante la presente que se ha registrado la siguiente accion : <strong>"+acciones+" .</strong> en el Plan de Accion Nro.:"+ide_sepla+" de la recomendación Nro. "+recomendacion+"  del informe Nro. "+informe+" </p>\n"
     	        + "<p>&nbsp;</p>\n" 
     	        + "<p>Recuerde que debe registrar la documentacion que valide la accion registrada.</p>\n"
     	        + "<p>&nbsp;</p>\n"
     	        + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";

     		}
     		else if (tipo_correo==3) {
     		//Tipo rol adjunto accion
     			
     			 htmlText+="<p>Notificamos mediante la presente que se ha remitido el comunicado de la accion : <strong>"+acciones+" .</strong> del Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
     		    	       + "<p>&nbsp;</p>\n" 
     		    	        + "<p>Recuerde que toda disposicion debe ser cumplida </p>\n"
     		    	        + "<p>&nbsp;</p>\n"
     		    	        + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";

     		}	
     		else if (tipo_correo==4) {
     		//Plan de accionss
     			htmlText+="<p>Notificamos mediante la presente que se ha que se ha rechazado la:  "+acciones +", de la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
     		             + "<p>&nbsp;</p>\n" 
     		             + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion en un lapso de 5 dias habiles</p>\n"
     		             + "<p>&nbsp;</p>\n"
     		             + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
     		}

        		//Notificacion de rechazo
     		else if (tipo_correo==5) {
         		//Plan de accion
     			htmlText+="<p> Notificamos mediante la presente que se ha cambiado el estado de la accion:  "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
    		             + "<p>&nbsp;</p>\n" 
    		             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
    		             + "<p>&nbsp;</p>\n"
    		             + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
         		}  
        		//Aplicada
     		else if (tipo_correo==6) {
     			    			
         		//Plan de accion
     			htmlText+="<p> Estimado(a) "+strNombreEmpleado+" responsable del cumplimiento de la recomendación  "
     		         + "<p>&nbsp;</p>\n"
     		         + "Como Delegado de la Gerencia General y en función a lo que establece el Procedimiento interno de Seguimiento y Control de Recomendaciones emitidas por la Contraloría General del Estado , se da la conformidad del plan de acción y se recuerda lo siguiente:"
     		         + "“El Responsable de la Recomendación y el Servidor designado son responsables de:"
     		         + "<strong> Fase de elaboración del plan de acción</strong>"
     		         + "<p>a)	Dar estricto cumplimiento a lo establecido en la recomendación a su cargo."
     		         + "<p>b)	Establecer un plan de acción por cada recomendación en el sistema e informar mediante memorando al delegado de la Máxima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan."
     		         + "<strong> Fase de reporte al cumplimiento al plan de acción </strong>"
     		         + "<p>c)	Deberán validar y verificar la pertinencia de la información previo al registrado en el sistema de recomendaciones."
     		         + "<p>d)	El registro del avance de la documentación que respalde el cumplimiento de la recomendación, de acuerdo a los tiempos establecido en el Plan."
     		         + "<p>f)	Llevar el control de las recomendaciones y su respectivo archivo físico y/o digital."
     		         + "<p>g)	Reportar el avance y el cumplimiento de la recomendación a la Máxima Autoridad o su delegado."
    		             + "<p>Con este antecedente, y en calidad de Delegado(a) del Seguimiento al Cumplimiento de las Recomendaciones se acepta el plan de acción presentado y dispongo a Usted la ejecución de las actividades y reporte de los avances establecidos en el plan de acción conforme a los tiempos establecidos. </p>\n"
    		             + "<p>&nbsp;</p>\n"
    		             + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
         		}  
     		else if (tipo_correo==7) {
         		//Reprogramacion de actividad
     	        htmlText+="<p>Notificamos mediante la presente se informa que se ha reprogramado la accion "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
     	     	       + "<p>&nbsp;</p>\n" 
     	     	        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
     	     	        + "<p>&nbsp;</p>\n"
     	     	        + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
         		} 
     		else if (tipo_correo==8) {
     			//Reprogramcion de actividad Administrador
     			htmlText+="<p>Notificamos mediante la presente se informa que se ha imcumplido con la accion "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
     		       + "<p>&nbsp;</p>\n" 
     		        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
     		        + "<p>&nbsp;</p>\n"
     		        + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
     			}
        
     		else if (tipo_correo==11) {
     			//Plan de accion Reprogramado Accion 
     			htmlText+="<p>Notificamos mediante la presente que se ha reprogramado la "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
     		       + "<p>&nbsp;</p>\n" 
     		        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
     		        + "<p>&nbsp;</p>\n"
     		        + "<p>Saludos cordiales,</p>\n";
     	     	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     	                + "<tbody>\n"
     	                + "<tr>\n"
     	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     	                + "<td width=\"476\">\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     	                + "</td>\n"
     	                + "</tr>\n"
     	                + "</tbody>\n"
     	                + "</table>";
     			}

     		
     		else if (tipo_correo==12) {
     			//Plan de accion EN ESTADO DE APLICADA
     			htmlText+="<p> Estimado(a) responsable del cumplimiento de la recomendaci&oacuten:</p>\n"
     		       + "<p>&nbsp;</p>\n" 
     		             + "<p>Como Delegado de la Gerencia General y en funci&oacuten a lo que establece el Procedimiento interno de Seguimiento y Control de "
     		             + "Recomendaciones emitidas por la Contralor&iacutea General del Estado , se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
     			         + "</p>\n"
     			         +"<p> El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </p>\n"
     			         +"<p><strong>Fase de elaboraci&oacuten del plan de acci&oacuten </strong></p>\n"
     			         + "<p>a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. "
     			         + "<p>b)	Establecer un plan de acci&oacuten por cada recomendaci&oacuten en el sistema e informar mediante memorando al delegado de la M&aacutexima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan. "
     			         +"<p><strong>Fase de reporte al cumplimiento al plan de acci&oacuten </strong></p>\n"
     			         + "<p>c)	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  "
     			         + "<p>d)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  "
     			         + "<p>e)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  "
     			         + "<p>f)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  "
     			         + "<p>g)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. "
     			         + "</p>\n"
     			         + "<p>Con este antecedente, y en calidad de Delegado(a) del Seguimiento al Cumplimiento de las Recomendaciones se aplica el plan de acci&oacuten para la recomendacion No. "+recomendacion+"  del informe No.  "+informe+"  "
     			         + "dispongo a Usted la ejecuci&oacuten de las actividades y reporte de los avances establecidos en el plan de acci&oacuten conforme a los tiempos establecidos.  </p>\n"
     		        + "<p>&nbsp;</p>\n"
     		        + "<p>Saludos cordiales,</p>\n";
     		    htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     		             + "<tbody>\n"
     		             + "<tr>\n"
     		             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     		             + "<td width=\"476\">\n"
     		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
     		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     		             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     		             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     		             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     		             + "</tr>\n"
     		             + "</tbody>\n"
     		             + "</table>";
     		          
     			}
     		
     		else if (tipo_correo==13) {
     			//Plan de accion EN ESTADO DE REPROGRAMADO
     			htmlText+="<p>Notificamos mediante la presente que se ha reprogramado todo el plan de accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
     		       + "<p>&nbsp;</p>\n" 
     		        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
     		        + "<p>&nbsp;</p>\n"
     		        + "<p>Saludos cordiales,</p>\n";
           	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
     			}else if (tipo_correo==14) {
     				//Plan de accion INCUMPLIDA
     			   htmlText+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
     		        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
     		        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y "
     		        		+ "en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
     		        		+ "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
     		        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a General "
     		        		+ "del Estado vigente, me permito informar que la recomendaci&oacute;n se encuentra con estado INCUMPLIDO, por lo que se solicita dispongo a Usted la ejecuci&oacute;n de las actividades "
     		        		+ "y reporte de los avances establecidos en el plan de acci&oacute;n conforme a los tiempos establecidos o en su defecto realizar la solicitud de reprogramaci&oacute;n.</p>\n";
     			    htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     			             + "<tbody>\n"
     			             + "<tr>\n"
     			             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     			             + "<td width=\"476\">\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     			             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     			             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     			             + "</tr>\n"
     			             + "</tbody>\n"
     			             + "</table>";
     			}else if (tipo_correo==15) {
     				
     				htmlText+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
     		        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
     		        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacute;n "
     		        		+ "de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones "
     		        		+ "de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento.</p>\n  "
     		        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a "
     		        		+ "General del Estado vigente, me permito informar que conforme a las actividades y medios de verificaci&oacute;n planteados para el cumplimiento de la "
     		        		+ "recomendación No. "+recomendacion+" del Informe "+informe+" se encuentra con estado APLICADA, por lo que se solicita a Usted continuar con el cumplimiento de la misma conforme al "
     		        		+ "mecanismo de reporte (mensual, bimensual, trimestral, semestral, anual), tiempo que corre a partir del "
     		        		+ "mes de (XXX – esto va en funci&oacute;n a la fecha de su cumplimiento si cumple el 20 de junio entonces se cuenta julio, agosto y septiembre "
     		        		+ "05 d&iacute;as antes del 30 de septiembre y hasta 4 d&iacute;a de octubre el sistema debe generar alerta)</p>\n";
     			    htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
     			             + "<tbody>\n"
     			             + "<tr>\n"
     			             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
     			             + "<td width=\"476\">\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
     			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
     			             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
     			             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
     			             + "</tr>\n"
     			             + "</tbody>\n"
     			             + "</table>";

     				


     				
     			}
else if (tipo_correo==16) {
                
	//Notificacion de Aplicada de la accion aprobada
		htmlText+="<p>Estimado(a) "+strNombreEmpleado+", </p>\n"
				+ "<p> El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
				+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten "
				+ "de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
				+ "auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
				+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacutea General del Estado vigente, "
				+ "se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>\n"
				+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo.  </p>"
				+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>"
				+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  </p>"
				+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>"
				+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  </p>"
				+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. </p>\n"
				+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se "
				+ "da la conformidad del (medio de verificaci&oacuten presentado) de la recomendaci&oacuten No. "+recomendacion+" del Informe No. "+informe+" presentado. </p>\n";
      

		htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
			             + "<tbody>\n"
			             + "<tr>\n"
			             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
			             + "<td width=\"476\">\n"
			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
			             + "PBX: (02) 3930-600&nbsp; ext. 1614\n"
			             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
			             + "</tr>\n"
			             + "</tbody>\n"
			             + "</table>";
			
		 //System.out.println("notificacion de rechazo"+htmlText);



     				
     			}
     		
        
         //String htmlText = cuerpo;
         messageBodyPart.setContent(htmlText, "text/html");
         multiParte.addBodyPart(messageBodyPart);
         	            
      // second part (the image)
       /*  messageBodyPart = new MimeBodyPart();
         DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
         messageBodyPart.setDataHandler(new DataHandler(fds));
         messageBodyPart.setHeader("Content-ID", "<image>");
         multiParte.addBodyPart(messageBodyPart);*/

         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(enviaMail.getCorreoEnvio()));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailReceptor));
         message.setSubject(asunto);
         message.setContent(multiParte);
       
         Transport.send(message);
         System.out.println("Correo enviado exitosamente a: " + mailReceptor);
     }
     catch (Exception mex) {
       System.out.println("Error EnviaEmail: " + mex);
       mensaje = "Error EnviaEmail: " + mex;
     }
     return mensaje;
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

public Confirmar getCon_aprobar_solicitud() {
	return con_aprobar_solicitud;
}

public void setCon_aprobar_solicitud(Confirmar con_aprobar_solicitud) {
	this.con_aprobar_solicitud = con_aprobar_solicitud;
} 

public void autentificadorParametersSMTP(String usuarioEnvio, String claveCorreo) {
    usuarioEnvio = usuarioEnvio;
    claveCorreo = claveCorreo;
  }
  
private class autentificadorParametersSMTP extends Authenticator {
     private String usuarioEnvio;
     private String claveCorreo;
      public autentificadorParametersSMTP(String usuarioEnvio, String claveCorreo) {
        this.usuarioEnvio = usuarioEnvio;
        this.claveCorreo = claveCorreo;
     }
}


public void cancelarAprobarSolicitud(){
	tab_respuesta.actualizar();
	con_aprobar_solicitud.cerrar();
	utilitario.addUpdate("tab_plan_accion,con_aprobar_solicitud");
}


public void cancelarPeriocidad(){
	tab_plan_accion.actualizar();
	con_aprobar_periocidad.cerrar();
	utilitario.addUpdate("tab_plan_accion,con_aprobar_periocidad");
}

public void cancelarEstadoPlan(){
	tab_plan_accion.actualizar();
	con_aprobar_cambio_estado.cerrar();
	utilitario.addUpdate("tab_plan_accion,con_aprobar_cambio_estado");
}




public Confirmar getCon_aprobar_periocidad() {
	return con_aprobar_periocidad;
}

public void setCon_aprobar_periocidad(Confirmar con_aprobar_periocidad) {
	this.con_aprobar_periocidad = con_aprobar_periocidad;
}

public void aprobarPeriocidad(){
	estadoRespuesta=0;

	try {
		utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seper="+tab_plan_accion.getValor("ide_seper")+",envio_plan=true where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
	//EnviarCorreoAccion(ide_sepla, informe, recomendacion, acciones, ide_gtemp, tipo_rol);
		con_aprobar_periocidad.cerrar();
		utilitario.agregarMensaje("Se ha cambiado la Periocidad del Plan de Accion", "Transaccion realizada con exito");
		tab_plan_accion.actualizar();
		utilitario.addUpdate("tab_plan_accion,con_aprobar_periocidad");
	} catch (Exception e) {
		// TODO Auto-generated catch block
System.out.println("No se puede enviar correo de cambio de periocidad");
	
	}


				
}


public void cambiarEstadoPlan(){
	if (estadoRespuesta==1) {
		//Aplicada
		try {
			TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,mecanismo_reporte_seres,ide_setre "
					+ "from seg_respuesta  "
					+ "where ide_sepla in("+tab_plan_accion.getValor("ide_sepla")+") and ide_setre in(8,6,1) ");
			if (tab_recomendaciones_plan.getTotalFilas()>0) {
				for (int i = 0; i < tab_recomendaciones_plan.getTotalFilas(); i++) {
						if (tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres")==null || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").equals("")) {
							tab_plan_accion.actualizar();
							utilitario.addUpdate("tab_plan_accion,con_aprobar_cambio_estado");
							con_aprobar_cambio_estado.cerrar();
							utilitario.agregarMensaje("No se puede actualiar el plan de acción", "Acciones sin anexos");
							return;
						}
					}	
				
		
				utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla")+" and ide_setre in(6,8,1) ");
				utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seesp=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
				/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
		        		+ " order by segus.ide_seusu");*/
			     TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
							+ "SEG_ASIGNACION ASI "
							+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
							+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
							+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
							+ "WHERE asi.IDE_seinf="+tab_recomendacion.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

			     
			     
			    
					if (tab_empleado.getTotalFilas()>0) {
					TablaGenerica tab_informe_recomendacion =utilitario.consultar("select pla.ide_sepla,rec.numero_serec,inf.numero_seinf "
							+ "from seg_plan_accion pla "
							+ "left join seg_informe inf on inf.ide_seinf=pla.ide_seinf  "
							+ "left join seg_recomendacion rec on rec.ide_serec=pla.ide_serec  "
							+ "where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
					
						for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
				//EnviarCorreoAccion(tab_informe_recomendacion.getValor("ide_sepla"), tab_informe_recomendacion.getValor("numero_seinf"), tab_informe_recomendacion.getValor("numero_serec"), "", tab_empleado.getValor("ide_gtemp"), 12);
						}
						//System.out.println("No se a enviado notificacion de plan de accion aplicado");
					}
					tab_plan_accion.guardar();
					guardarPantalla();
				con_aprobar_cambio_estado.cerrar();
				
			tab_respuesta.actualizar();
			utilitario.addUpdate("tab_plan_accion,tab_respuesta,con_aprobar_cambio_estado");
			}else {
				utilitario.agregarMensaje("No se puede cambiar estado de Plan de Accion", "No se han encontrado registros validos");
				cancelarEstadoPlan();
				return;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error al remitir correo de plan de accion aplicado");		}

		/*}else 	if (estadoRespuesta==4) {
		//Reprogramacion
		try {
			utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla")+" and ide_setre in(6) ");
//EnviarCorreoAccion(ide_sepla, informe, recomendacion, acciones, ide_gtemp, tipo_rol);
			tab_plan_accion.actualizar();
			utilitario.addUpdate("tab_plan_accion,con_aprobar_periocidad");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error al remitir correo de plan de accion reprogramado");		

		}*/


	}else 	if (estadoRespuesta==5) {
		//Incumplida
		try {//aprobado y avance
			boolean band_resp=false;
			TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,mecanismo_reporte_seres,ide_setre from seg_respuesta where ide_sepla in("+tab_plan_accion.getValor("ide_sepla")+") and ide_setre in(1,6,8) ");
			if (tab_recomendaciones_plan.getTotalFilas()>0) {
			String fechaActual=utilitario.getFechaActual();
			for (int i = 0; i < tab_recomendaciones_plan.getTotalFilas(); i++) {
				if(fechaActual.compareTo(tab_recomendaciones_plan.getValor(i,"fecha_hasta_seres"))>0){
					if (tab_recomendaciones_plan.getValor(i,"ide_setre").equals("6")) {
						//if (tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres")==null || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").equals("")) {
						//	band_resp=true;
							utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=8 where ide_seres="+tab_recomendaciones_plan.getValor(i,"ide_seres")+" and activo_seres=true ");
							utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seesp=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));

						//}else {
						//}
					}else {
						band_resp=true;
						utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=8 where ide_seres="+tab_recomendaciones_plan.getValor(i,"ide_seres")+" and activo_seres=true ");
					}

				}
			}
				if (band_resp==true) {
			utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seesp=5 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
				}
			con_aprobar_cambio_estado.cerrar();
			try {
/*				TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
		        		+ " order by segus.ide_seusu");*/
				
		     TablaGenerica tabEmpleado=utilitario.consultar("SELECT * FROM  "
						+ "SEG_ASIGNACION ASI "
						+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
						+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
						+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
						+ "WHERE asi.IDE_seinf="+tab_recomendacion.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

				if (tabEmpleado.getTotalFilas()>0) {
			for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) {
		//	EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), "", tabEmpleado.getValor(i,"ide_gtemp"), 14);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tab_plan_accion.actualizar();
			tab_respuesta.actualizar();
			utilitario.addUpdate("tab_plan_accion,tab_respuesta,con_aprobar_cambio_estado");
			}else {
				utilitario.agregarMensaje("No se puede cambiar estado de Plan de Accion", "No se han encontrado registros validos");
				cancelarEstadoPlan();
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error al remitir correo de plan de accion incumplido");		
		}

	}else if (estadoRespuesta==2){
		int contador1=0,contador2=0;
		TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,mecanismo_reporte_seres,ide_setre from seg_respuesta where ide_sepla in("+tab_plan_accion.getValor("ide_sepla")+") and ide_setre in(1,6,8) ");
		if (tab_recomendaciones_plan.getTotalFilas()>0) {
			for (int j = 0; j < tab_recomendaciones_plan.getTotalFilas(); j++) {
			if (tab_recomendaciones_plan.getValor(j, "ide_setre").equals("1") || tab_recomendaciones_plan.getValor(j, "ide_setre").equals("6")) {
				contador1++;
			}else 	if (tab_recomendaciones_plan.getValor(j, "ide_setre").equals("8")){
				contador2++;
			}
			}
			
			if (contador1>0 && contador2==0) {
				utilitario.getConexion().ejecutarSql("update seg_plan_accion  set ide_seesp=2 where ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
				utilitario.agregarMensaje("Plan de accion en ejecución", "El plan ha sido actualizado");
				tab_plan_accion.actualizar();			
				con_aprobar_cambio_estado.cerrar();
				utilitario.addUpdate("con_aprobar_cambio_estado");
			}else {
				utilitario.agregarMensaje("No se pudo completar actualizar el plan de accion", "Por favor revisar los egistros invalidos");
				tab_plan_accion.actualizar();
				return;
			}
	
		}else {
			utilitario.agregarMensaje("No se pudo completar la accion", "El plan no contiene registros validos");
			tab_plan_accion.actualizar();
			return;
		}	
		
	}
		
}

public void validarTipoAccion(AjaxBehaviorEvent evt){
	tab_plan_accion.modificar(evt);
	if (tab_plan_accion.getValor("ide_seesp")==null || tab_plan_accion.getValor("ide_seesp").equals("")) {
		utilitario.agregarMensaje("No se puede continuar", "Estado del Plan de Accion invalido");
		tab_plan_accion.actualizar();
		return;
	}
		
	if (tab_plan_accion.getValor("envio_plan")==null || tab_plan_accion.getValor("envio_plan").equals("") || tab_plan_accion.getValor("envio_plan").equals("false") || tab_plan_accion.getValor("envio_plan").isEmpty()) {
	if (tab_plan_accion.getValor("ide_seesp").equals("1")){
			con_aprobar_periocidad.dibujar();

		
		}else {
		utilitario.agregarMensaje("No se puede guardar", "Estado del Plan de Accion invalido");
		tab_plan_accion.actualizar();
		return;
		
		
	}
		
		}else{
			utilitario.agregarMensaje("No se puede guardar", "El plan de accion ya contiene Periocidad Asignada");
			tab_plan_accion.actualizar();
			return;
		}

	
}


public void validarAccion(AjaxBehaviorEvent evt){
	tab_plan_accion.modificar(evt);
	estadoRespuesta=0;
	if (tab_plan_accion.getValor("ide_seesp")==null || tab_plan_accion.getValor("ide_seesp").equals("")) {
		utilitario.agregarMensaje("No se puede continuar", "Estado del Plan de Accion invalido");
		tab_plan_accion.actualizar();
		return;
	}
		
	if (tab_plan_accion.getValor("ide_seesp").equals("1")){
		if (tab_plan_accion.getValor("ide_seper")==null || tab_plan_accion.getValor("ide_seper").isEmpty()) {
			utilitario.agregarMensaje("No se puede guardar", "Debe seleccionar la Periocidad");
			tab_plan_accion.actualizar();
			return;
			
		}
		
		if (tab_plan_accion.getValor("ide_seemp")==null || tab_plan_accion.getValor("ide_seemp").isEmpty()) {
			utilitario.agregarMensaje("No se puede guardar", "Debe seleccionar la Area Encargada");
			tab_plan_accion.actualizar();
			return;
		}
		
			
		if (tab_plan_accion.getValor("fecha_inicio_control_periocidad_sepla")==null || tab_plan_accion.getValor("fecha_inicio_control_periocidad_sepla").isEmpty()) {
			utilitario.agregarMensaje("No se puede guardar", "Debe seleccionar la fecha de control de periocidad");
			tab_plan_accion.actualizar();
			return;
		}

		
		con_aprobar_cambio_estado.dibujar();
			estadoRespuesta=1;
			//Aplicada

		
		/*}else  if(tab_respuesta.getValor("ide_seesp").equals("4")){
			//Reprogramacion
			estadoRespuesta=4;
			con_aprobar_cambio_estado.dibujar();
*/
		}else  if(tab_plan_accion.getValor("ide_seesp").equals("5")){
		//incumplida
			estadoRespuesta=5;
			con_aprobar_cambio_estado.dibujar();

		}else  if(tab_plan_accion.getValor("ide_seesp").equals("2")){
		//incumplida
			estadoRespuesta=2;
			con_aprobar_cambio_estado.dibujar();

		}else{
		//utilitario.agregarMensaje("No se puede realizar la accion solicitada", "Unicamente puede cambiar a Estado APLICADA O INCUMPLIDA");
		//tab_plan_accion.actualizar();
		//estadoRespuesta=0;
		//return;
		
		
	}
		


	
}

public Confirmar getCon_aprobar_cambio_estado() {
	return con_aprobar_cambio_estado;
}

public void setCon_aprobar_cambio_estado(Confirmar con_aprobar_cambio_estado) {
	this.con_aprobar_cambio_estado = con_aprobar_cambio_estado;
}

@Override
public void aceptarBuscar() {
	// TODO Auto-generated method stub
	super.aceptarBuscar();
	actualizarTablas();

}

/*
public void notificacionesERP()throws SchedulerException{
	
	// TODO Auto-generated method stub
	JobDetail job=JobBuilder.newJob(QuartzJobSeguimiento.class).build();
	//Simple Trigger
	//Trigger t1=   TriggerBuilder.newTrigger().withIdentity("SimpleTrigger").startNow().build();
	//Complex Trigger
	//Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();
	//Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(05).repeatForever()).build();
	Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(2).repeatForever()).build();
	
	
	try {
		Scheduler sc= StdSchedulerFactory.getDefaultScheduler();
		sc.start();
		///////////////////////////INICIO
		/////////////FIN
		sc.scheduleJob(job, t1);
	} catch (SchedulerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
*/

public boolean revisarFechasSeguimiento(String fecha_desde_seres, String valor_seper,String ide_sepla,String ide_seres,int tipo){
	      boolean valorBand=false; 
		   String fechaApertura=fecha_desde_seres;
		   Integer valor= Integer.parseInt(valor_seper)*30;
		   String fechaTope= utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaApertura), valor));
		  if(fechaTope.compareTo(utilitario.getFechaActual())>=0){
				//System.out.println("NO CUMPLE LA PERIOCIDAD LA FECHA SELECCIONADA");
				valorBand=false; 
		  }else {
			  String ide_sepla_anterios="";
			  valorBand=true; 
			  //System.out.println("actualizacion de aplicada con periocidad del plan: "+ide_sepla);

			  //utilitario.getConexion().ejecutarSql("update seg_respuesta set activo_seres=false where ide_seres=" + ide_seres);
		
		  }	
		  return valorBand;
}



public String revisarFechasSeguimientoAplicada(String fecha_desde_seres, String valor_seper,String ide_sepla,int tipo){
    boolean valorBand=false; 
	   String fechaApertura=fecha_desde_seres;
	   String fecha="";
	   bandNotificacionAplicada=0;

	   Integer valor= Integer.parseInt(valor_seper)*30;
	   String fechaTope= utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaApertura), valor));
	  if(fechaTope.compareTo(utilitario.getFechaActual())>=0){
		//	System.out.println("NO CUMPLE LA PERIOCIDAD LA FECHA SELECCIONADA IDE_SEPLA: "+ide_sepla);
			valorBand=false; 
			bandNotificacionAplicada=1;
			fecha=fechaTope;
			
			
			
	  }else {
		  String ide_sepla_anterios="";
		  valorBand=true; 
		 // System.out.println("Plan de accion aplicado ha vencido la periodicidad del plan: "+ide_sepla);
		  fecha=fechaTope;
		  bandNotificacionAplicada=2;
		  //utilitario.getConexion().ejecutarSql("update seg_respuesta set activo_seres=false where ide_seres=" + ide_seres);
	
	  }	
	  return fecha;
}

public boolean revisarFechasSeguimientoAprobado(String fecha_desde_seres, String mecanismo_reporte,String ide_sepla,String ide_seres,int tipo){
    boolean valorBand=false; 
	   String fechaApertura=fecha_desde_seres;
	  // Integer valor= Integer.parseInt(valor_seper)*30;
	   String fechaTope= utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fechaApertura), 1));
	  if(fechaTope.compareTo(utilitario.getFechaActual())>=0){
			valorBand=false; 
	  }else {
		  String ide_sepla_anterios="";
		   
		  if (mecanismo_reporte==null || mecanismo_reporte.isEmpty() || mecanismo_reporte.equals("")) {
			  //utilitario.getConexion().ejecutarSql("update seg_respuesta set ide_setre=8 where ide_seres=" + ide_seres);		
			  
			  valorBand=true;
		  }else {
			  //System.out.println("actualizacion de incumplida por accion en estado de aprobada y sin adjunto");
			  valorBand=false;
			//System.out.println("registro respuesta : "+ide_seres+" del plan de accion: "+ide_sepla);
		}
	
	  }	
	  return valorBand;
}



public void insertarTablaRespuesta(String ide_sepla_anterior, int sumaDias,int ide_sepla){


	
	TablaGenerica tab_empleados=utilitario.consultar("SELECT "
			+ "ide_seres, "
			+ "ide_serec, "
			+ "descripcion_seres, "
			+ "hora_seres, "
			+ "ide_seusu, "
			+ "ide_seemp, "
			+ "ide_setre, "
			+ "activo_seres, "
			+ "fecha_seres, "
			+ "ide_sepla, "
			+ "ide_seinf, "
			+ "fecha_desde_seres, "
			+ "fecha_hasta_seres, "
			+ "medio_verificacion_seres, "
			+ "ide_gtemp_responsable, "
			+ "mecanismo_reporte_seres, "
			+ "observacion_seres, "
			+ "reprogramacion_seres, "
			+ "descripcion_repro_seres, "
			+ "aceptar_repro_seres, "
			+ "nombre_archivo_seres, "
			+ "ide_seres_anterior "
			+ "FROM seg_respuesta "
			+ "where ide_sepla="+ide_sepla_anterior+" and ide_setre=5 "
			+ "order by fecha_desde_seres asc, fecha_hasta_seres asc");
	

	String ide_serec="",descripcion_seres="",ide_seusu="",hora_seres="",ide_seemp="",ide_seinf="",medio_verificacion_seres="",ide_gtemp_responsable="";
	for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {

	 ide_serec=tab_empleados.getValor(i,"ide_serec"); 
	 descripcion_seres=tab_empleados.getValor(i,"descripcion_seres");
	hora_seres=utilitario.getHoraActual();
	ide_seusu=tab_empleados.getValor(i,"ide_seusu");
	ide_seemp=tab_empleados.getValor(i,"ide_seemp");
	String ide_setre="6";
	String activo_seres="true";
	String fecha_seres=utilitario.getFechaActual();
	int ide_sepla_=ide_sepla;
	ide_seinf=tab_empleados.getValor(i,"ide_seinf");
	String fecha_desde_nueva="",fecha_hasta_nueva="";
	fecha_desde_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_empleados.getValor(i,"fecha_desde_seres")), sumaDias));
	fecha_hasta_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_empleados.getValor(i,"fecha_hasta_seres")), sumaDias));
	medio_verificacion_seres=tab_empleados.getValor(i,"medio_verificacion_seres");
	ide_gtemp_responsable=tab_empleados.getValor(i,"ide_gtemp_responsable");
	String mecanismo_reporte_seres=null;
	String observacion_seres=null;
	boolean reprogramacion_seres=false;
	String descripcion_repro_seres=null;
	boolean aceptar_repro_seres=false;
	String nombre_archivo_seres=null;
	String ide_seres_anterior=null; 

	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_respuesta", "ide_seres"));
	String codigo=tab_codigo.getValor("codigo");
	
	utilitario.getConexion().ejecutarSql("INSERT INTO seg_respuesta(" 
	+ " ide_seres, "
	+ "ide_serec, "
	+ "descripcion_seres, "
	+ "hora_seres, "
	+ "ide_seusu, "
	+ "ide_seemp, "
	+ "ide_setre, "
	+ "activo_seres, "
	+ "fecha_seres,"
	+ "ide_sepla, "
	+ "ide_seinf, "
	+ "fecha_desde_seres, "
	+ "fecha_hasta_seres, "
	+ "medio_verificacion_seres,  "
	+ "ide_gtemp_responsable, "
	+ "mecanismo_reporte_seres, "
	+ "observacion_seres, "
	+ "reprogramacion_seres, "
	+ "descripcion_repro_seres, "
	+ "aceptar_repro_seres, "
	+ "nombre_archivo_seres, "
	+ "ide_seres_anterior) "
		+" values( " +codigo + ", "
		+ ""+ ide_serec+", "
		+ "'"+descripcion_seres+"', "
		+ "'"+utilitario.getHoraActual()+"', "
		+ ""+ide_seusu+", "
		+ ""+ide_seemp+", "	  		
		+ ""+ide_setre+", "
		+ "true, "
		+ "'"+utilitario.getFechaActual()+"', "
		+ ""+ide_sepla+", "
		+ ""+ide_seinf+", "
		+ "'"+fecha_desde_nueva+"', "
		+ "'"+fecha_hasta_nueva+"', "
		+ "'"+medio_verificacion_seres+"', "
		+ ""+ide_gtemp_responsable+", "
		+ "null, "
		+ "null, "
		+ "false, "
		+ "null, "
		+ "false, "
		+ "null, "
		+ "null)"); 
	}

	
}



public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}



public void insertarTablaPlanAccion(
		 String ide_seusu,
		 String ide_serec,
		 String fecha_registro_sepla,
		 String fecha_aprobacion_sepla,
		 int ide_seesp,
		 String activo_sepla,
		 String ide_seinf,
		 String ide_seper,
		 String asunto_sepla


		 
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_plan_accion", "ide_sepla"));
		String codigo=tab_codigo.getValor("codigo");
		

		utilitario.getConexion().ejecutarSql("INSERT INTO seg_plan_accion(" 
					+ "ide_sepla, "
					+ "ide_seusu, "
			  		+ "ide_serec, "
			  		+ "fecha_registro_sepla, "
			  		+ "fecha_aprobacion_sepla, "
			  		+ "porcentaje_sepla, "
			  		+ "ide_seesp, "
			  		+ "activo_sepla, "
			  		+ "ide_seinf, "
			  		+ "ide_seper, "
			  		+ "asunto_sepla) "  

			  		+" values( " +codigo + ", "
			  		+ ""+ ide_seusu+", "
			  		+ ""+ide_serec+", "
			  		+ "'"+fecha_registro_sepla+"', "
			  		+ ""+fecha_aprobacion_sepla+", "
			  		+ "0.00, "
			  		+ ""+ide_seesp+", "
			  		+ "true, "
			  		+ ""+ide_seinf+", "
			  		+ ""+null+", "
	    	  		+ "'"+asunto_sepla+"')"); 
	 
		

}




public int retornaIdeSepla(String ide_serec, String ide_seinf){
	int retornaValor=0;
	TablaGenerica tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
			+ "porcentaje_sepla, ide_seesp, activo_sepla, "
			+ "ide_seinf,ide_seper, asunto_sepla "
			+ "FROM seg_plan_accion "
			+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and activo_sepla=true");
	
	if (tab_plan_accion.getTotalFilas()>0){
		retornaValor=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));
	}else {
		retornaValor=0;
	}
	
	return retornaValor; 
}


public void noti()  {
	TablaGenerica tab_notificaciones=utilitario.consultar("SELECT pla.ide_sepla, pla.ide_seusu, pla.ide_serec, pla.fecha_registro_sepla, pla.fecha_aprobacion_sepla, "
			+ "pla.porcentaje_sepla, pla.ide_seesp, pla.activo_sepla,pla.ide_seinf, "
			+ "pla.ide_seper, pla.asunto_sepla, pla.envio_plan, pla.ide_seemp, pla.observacion_sepla,peri.valor_seper "
			+ "FROM seg_plan_accion pla "
			+ "left join seg_periocidad peri on pla.ide_seper=peri.ide_seper "
			+ "where pla.activo_sepla=true and pla.ide_seper!=6 order by ide_sepla");
	
	if (tab_notificaciones.getTotalFilas()>0) {
				
	
		for (int i = 0; i < tab_notificaciones.getTotalFilas(); i++) {
		
			
		if (tab_notificaciones.getValor(i,"ide_seesp").equals("1") && !tab_notificaciones.getValor(i,"valor_seper").equals("6")) {//APLICADA
			 TablaGenerica tabRespuesta=utilitario.consultar("select * "
				 		+ "from seg_respuesta  "
				 		+"WHERE IDE_SEPLA="+tab_notificaciones.getValor(i,"IDE_SEPLA")+" and ide_setre=5 order by fecha_desde_seres asc, fecha_hasta_seres asc");
				 
				 
				if (tabRespuesta.getTotalFilas()<=0) {
		
					System.out.println("plan de accion :"+tab_notificaciones.getValor(i,"IDE_SEPLA")+" sin actividades");
				}else {
					
					TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
			        		+ "FROM seg_usuario segus "
			        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
			        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
			        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
			        		+ " order by segus.ide_seusu");
					String usuario_responsable=tabEmpleado.getValor("ide_seusu");
				int cont=0;
					for (int j = 0; j < tabRespuesta.getTotalFilas(); j++) {
						if(revisarFechasSeguimiento(tabRespuesta.getValor(j,"fecha_desde_seres"), tab_notificaciones.getValor(i,"valor_seper"), tab_notificaciones.getValor(i,"ide_sepla"), tabRespuesta.getValor(j,"ide_seres"),1))
						{
							cont++;
						}else{
							
						}
					
					}	
					if (cont==0) {
						//enviar correo de registro de plan de accion 
					}else {
						  utilitario.getConexion().ejecutarSql("update seg_plan_accion set activo_sepla=false where ide_sepla=" + tab_notificaciones.getValor(i,"IDE_SEPLA"));
							 insertarTablaPlanAccion(usuario_responsable, tab_notificaciones.getValor(i,"IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_notificaciones.getValor(i,"ide_seinf"), null, "PLAN DE ACCION");
							 int ide_sepla=retornaIdeSepla(tab_notificaciones.getValor(i,"IDE_SEREC"),tab_notificaciones.getValor("i,IDE_SEINF"));
							 insertarTablaRespuesta(tab_notificaciones.getValor(i,"IDE_SEPLA"), Integer.parseInt(tab_notificaciones.getValor(i,"valor_serper")),ide_sepla);	
							 System.out.println("Cambio de estado de accion aplicada a plan en ejecucion ide_sepla anterior :"+tab_notificaciones.getValor(i,"IDE_SEPLA") +" y ide_sepla_nuevo: "+ide_sepla); 
					}
					
				
				
				
				}
				
		}else if (tab_notificaciones.getValor(i,"ide_seesp").equals("1") && tab_notificaciones.getValor(i,"valor_seper").equals("6")) {//
					
			 System.out.println("Plan de Accion para reportar unica vez ide_sepla :"+tab_notificaciones.getValor(i,"IDE_SEPLA")); 

				
		}else if (tab_notificaciones.getValor(i,"ide_seesp").equals("2")) {//EN EJECUCION
			 TablaGenerica tabRespuesta=utilitario.consultar("select * "
				 		+ "from seg_respuesta  "
				 		+"WHERE IDE_SEPLA="+tab_notificaciones.getValor(i,"IDE_SEPLA")+" and ide_setre  in (6) order by fecha_desde_seres asc, fecha_hasta_seres asc");
			int cont=0;
			if (tabRespuesta.getTotalFilas()>0) {
				TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
		        		+ " order by segus.ide_seusu");
				String usuario_responsable=tabEmpleado.getValor("ide_seusu");
				
				System.out.println("Plan de Accion  contiene accion en estado de aprobado ide_sepla :"+tab_notificaciones.getValor(i,"IDE_SEPLA")); 
				 TablaGenerica tabRespuesta_aprobado=utilitario.consultar("select * "
					 		+ "from seg_respuesta  "
					 		+"WHERE IDE_SEPLA="+tab_notificaciones.getValor(i,"IDE_SEPLA")+" and ide_setre  in (6) order by fecha_desde_seres asc, fecha_hasta_seres asc");
					 if (tabRespuesta_aprobado.getTotalFilas()>0) {
						for (int j = 0; j < tabRespuesta_aprobado.getTotalFilas(); j++) {
							
						
						 if(revisarFechasSeguimientoAprobado(tabRespuesta_aprobado.getValor(j,"fecha_desde_seres"), tabRespuesta_aprobado.getValor(j,"mecanismo_reporte_seres"), tab_notificaciones.getValor(i,"ide_sepla"), tabRespuesta_aprobado.getValor(j,"ide_seres"),1))
							{
								cont++;
							}else{
								
							}
						 
						 if (cont>0) {
							 utilitario.getConexion().ejecutarSql("update seg_plan_accion set ide_seesp=5 where ide_sepla=" + tab_notificaciones.getValor(i,"IDE_SEPLA"));
						}
						 
					}
						}	
					 
				 
			}else {
				//CORREO DE ENVIO DE plan de accion
				 System.out.println("Plan de Accion  no contiene acciones ide_sepla :"+tab_notificaciones.getValor(i,"IDE_SEPLA")); 
					/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
			        		+ "FROM seg_usuario segus "
			        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
			        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
			        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
			        		+ " order by segus.ide_seusu");
					String usuario_responsable=tabEmpleado.getValor("ide_seusu");*/
			}
			
			
			
		}else if (tab_notificaciones.getValor(i,"ide_seesp").equals("5")) {//INCUMPLIDA
			 System.out.println("Plan de Accion  incumplido acciones ide_sepla :"+tab_notificaciones.getValor(i,"IDE_SEPLA"));
			 TablaGenerica tabRespuesta=utilitario.consultar("select * "
				 		+ "from seg_respuesta  "
				 		+"WHERE IDE_SEPLA="+tab_notificaciones.getValor(i,"IDE_SEPLA")+" and ide_setre  in (6) order by fecha_desde_seres asc, fecha_hasta_seres asc");

		int cont=0;
		if (tabRespuesta.getTotalFilas()>0) {
			TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
	        		+ " order by segus.ide_seusu");
			String usuario_responsable=tabEmpleado.getValor("ide_seusu");
		 
	 //EnviarCorreoAccion(ide_sepla, informe, recomendacion, acciones, ide_gtemp, tipo_rol);			
	 /*System.out.println("Plan de Accion  contiene accion en estado de incumplida :"+tab_notificaciones.getValor(i,"IDE_SEPLA")); 
		TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
        		+ " order by segus.ide_seusu");
		String usuario_responsable=tabEmpleado.getValor("ide_seusu");*/
}			


			 
			 //EnviarCorreoAccion(ide_sepla, informe, recomendacion, acciones, ide_gtemp, tipo_rol);			
			 /*System.out.println("Plan de Accion  contiene accion en estado de incumplida :"+tab_notificaciones.getValor(i,"IDE_SEPLA")); 
				TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where segus.ide_seusu="+tabRespuesta.getValor("ide_seusu")
		        		+ " order by segus.ide_seusu");
				String usuario_responsable=tabEmpleado.getValor("ide_seusu");*/
		}			
		
		
		
		
		}//for	
		
		

		
		
	}else {
		utilitario.agregarMensaje("No se han encontrado planes de accion activos", "Pongase en contacto con el administrador");
		return;
	}

	
}



public void actualizarRespuesta(SelectEvent evt) {
	tab_plan_accion.seleccionarFila(evt);
	tab_respuesta.setCondicion("ide_sepla="+tab_plan_accion.getValor("ide_sepla"));
	tab_respuesta.ejecutarSql();
	utilitario.addUpdate("tab_respuesta");
}

public int getBandNotificacionAplicada() {
	return bandNotificacionAplicada;
}

public void setBandNotificacionAplicada(int bandNotificacionAplicada) {
	this.bandNotificacionAplicada = bandNotificacionAplicada;
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

@Override
public void abrirSeleccionarArchivo(ActionEvent evt) {
	// TODO Auto-generated method stub
	super.abrirSeleccionarArchivo(evt);
}




}
