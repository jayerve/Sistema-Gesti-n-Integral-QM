/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.io.File;
import java.util.Properties;

import javax.ejb.EJB;
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

import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.correo.EnviarCorreo;

/**
 *
 * @author DELL-USER
 */
public class seg_evaluacion extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_detalle_seguimiento = new Tabla();
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	String usuario="";
	String carpeta="informeCierre";
    public seg_evaluacion() {
    	
		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'seg_informe'");
		String empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		
		String ide_serec="",ide_seinf="";
	 TablaGenerica tab_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
					+ "usu_email, ide_usua, activo_seusu "
					+ "FROM seg_usuario "
					+ "where ide_usua="+utilitario.getVariable("ide_usua")+" and activo_seusu=true ") ;
	 usuario=tab_usuario.getValor("ide_seusu");
    

    	tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("seg_evaluacion", "ide_seeva", 1);
        
            
        tab_tabla.getColumna("ide_seeva").setNombreVisual("CODIGO");
        tab_tabla.getColumna("ide_sesui").setNombreVisual("SUSCRIBE");
        tab_tabla.getColumna("ide_sesui").setRequerida(true);
        tab_tabla.getColumna("ide_sesui").setCombo("SELECT ide_sesui, descripcion_sesui "
				+ "FROM seg_suscribe_informe"); 
        
  
        tab_tabla.getColumna("ide_seesi").setNombreVisual("ESTADO INFORME");
        tab_tabla.getColumna("ide_seesi").setRequerida(true);
        tab_tabla.getColumna("ide_seesi").setCombo("SELECT ide_seesi, descripcion_seesi "
				+ "FROM seg_estado_informe"); 
        
        tab_tabla.getColumna("ide_senii").setNombreVisual("NIVEL INFORME");
        tab_tabla.getColumna("ide_senii").setRequerida(true);
        tab_tabla.getColumna("numero_seeva").setLongitud(40);
        tab_tabla.getColumna("ide_senii").setCombo("SELECT ide_senii, descripcion_senii "
				+ "FROM seg_nivel_informe"); 
        
        
        
        tab_tabla.getColumna("numero_seeva").setNombreVisual("NUMERO");
        tab_tabla.getColumna("numero_seeva").setRequerida(true);
        tab_tabla.getColumna("numero_seeva").setLongitud(40);
        
        
        tab_tabla.getColumna("asunto_seeva").setNombreVisual("ASUNTO");
        tab_tabla.getColumna("asunto_seeva").setAncho(80);
        tab_tabla.getColumna("asunto_seeva").setRequerida(true);
        tab_tabla.getColumna("asunto_seeva").setLongitud(150);
        
        
        tab_tabla.getColumna("fecha_aprobacion_seeva").setNombreVisual("FEC.REGISTRO");
        tab_tabla.getColumna("fecha_aprobacion_seeva").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("fecha_aprobacion_seeva").setLectura(true);

        tab_tabla.getColumna("fecha_inicio_seeva").setNombreVisual("FEC.INI_EVALUACION");
        tab_tabla.getColumna("fecha_inicio_seeva").setLongitud(100);

        tab_tabla.getColumna("fecha_fin_seeva").setNombreVisual("FEC.FIN_EVALUACION");
        tab_tabla.getColumna("fecha_fin_seeva").setLongitud(100);

        
        tab_tabla.getColumna("ide_seusu").setNombreVisual("USUARIO REGISTRO");		
        tab_tabla.getColumna("ide_seusu").setCombo("SELECT  segus.ide_seusu,usua.nick_usua "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "order by segus.ide_seusu");
        tab_tabla.getColumna("ide_seusu").setAutoCompletar();
        tab_tabla.getColumna("ide_seusu").setLectura(true);
        tab_tabla.getColumna("ide_seusu").setRequerida(true);
        
        tab_tabla.getColumna("archivo_adjunto_seeva").setUpload(carpeta);
        tab_tabla.getColumna("archivo_adjunto_seeva").setNombreVisual("ADJUNTO");
        tab_tabla.getColumna("archivo_adjunto_seeva").setColumnaNombreArchivo("nombre_archivo_seeva");

        tab_tabla.getColumna("nombre_archivo_seeva").setLectura(true);
        tab_tabla.getColumna("nombre_archivo_seeva").setNombreVisual("NOMBRE_ADJUNTO");
        tab_tabla.getColumna("nombre_archivo_seeva").setValorDefecto("sin nombre");
        tab_tabla.getColumna("activo_seeva").setCheck();
        tab_tabla.getColumna("activo_seeva").setValorDefecto("true");
        tab_tabla.getColumna("activo_seeva").setNombreVisual("ACTIVO");
        tab_tabla.agregarRelacion(tab_detalle_seguimiento);
        tab_tabla.setCampoOrden("ide_seeva desc");
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("EVALUACION");
		



		tab_detalle_seguimiento.setId("tab_detalle_seguimiento");
		tab_detalle_seguimiento.setTabla("seg_detalle_evaluacion", "ide_sedee", 2);
        
            
		tab_detalle_seguimiento.getColumna("ide_sedee").setNombreVisual("CODIGO");
		tab_detalle_seguimiento.getColumna("ide_sedee").setOrden(1);
		tab_detalle_seguimiento.getColumna("ide_seinf").setNombreVisual("INFORME");
		tab_detalle_seguimiento.getColumna("ide_seinf").setCombo("SELECT ide_seinf, numero_seinf || ' ' || asunto_seinf "
				+ "FROM seg_informe"); 
		tab_detalle_seguimiento.getColumna("ide_seinf").setAutoCompletar();
		tab_detalle_seguimiento.getColumna("ide_seinf").setLectura(true);
		tab_detalle_seguimiento.getColumna("ide_seinf").setOrden(2);
		
		tab_detalle_seguimiento.getColumna("ide_serec").setNombreVisual("RECOMENDACION");
		tab_detalle_seguimiento.getColumna("ide_serec").setCombo("SELECT ide_serec, numero_serec || ' ' || asunto_serec "
				+ "FROM seg_recomendacion"); 
		tab_detalle_seguimiento.getColumna("ide_serec").setAutoCompletar();
		tab_detalle_seguimiento.getColumna("ide_serec").setLectura(true);
		tab_detalle_seguimiento.getColumna("ide_serec").setOrden(3);
		//tab_detalle_seguimiento.getColumna("ide_serec").setLectura(true);
        
		
		tab_detalle_seguimiento.getColumna("ide_seesr").setNombreVisual("ESTADO_REC");
		tab_detalle_seguimiento.getColumna("ide_seesr").setCombo("SELECT ide_seesr, descripcion_seesr  "
	        		+ "FROM seg_estado_recomendacion");
        
	//	tab_detalle_seguimiento.getColumna("ide_seesr").setMetodoChange("validarTipoAccion");
		tab_detalle_seguimiento.getColumna("ide_seesr").setOrden(4);
		tab_detalle_seguimiento.getColumna("ide_seesr").setRequerida(true);
        
		tab_detalle_seguimiento.getColumna("asunto_sedee").setNombreVisual("ASUNTO");
		tab_detalle_seguimiento.getColumna("asunto_sedee").setAncho(300);
		tab_detalle_seguimiento.getColumna("asunto_sedee").setLongitud(150);
		tab_detalle_seguimiento.getColumna("asunto_sedee").setOrden(6);
		tab_detalle_seguimiento.getColumna("asunto_sedee").setRequerida(true);

		
		tab_detalle_seguimiento.getColumna("ide_gtemp").setNombreVisual("USUARIO_RESPONSABLE");		
		tab_detalle_seguimiento.getColumna("ide_gtemp").setCombo("SELECT  emp.ide_gtemp, "
        		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
        		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
        		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
        		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
        		+ "FROM gth_empleado emp  "
        		//+ "where emp.activo_gtemp=true  "
        		+ "order by APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP");
		//tab_detalle_seguimiento.getColumna("ide_gtemp").setAutoCompletar();
		tab_detalle_seguimiento.getColumna("ide_gtemp").setOrden(5);
		tab_detalle_seguimiento.getColumna("ide_gtemp").setRequerida(true);
        
        
		tab_detalle_seguimiento.getColumna("activo_sedee").setCheck();
		tab_detalle_seguimiento.getColumna("activo_sedee").setValorDefecto("true");
		tab_detalle_seguimiento.getColumna("activo_sedee").setNombreVisual("ACTIVO");
		tab_detalle_seguimiento.getColumna("activo_sedee").setLectura(true);
		tab_detalle_seguimiento.getColumna("activo_sedee").setOrden(7);

		tab_detalle_seguimiento.setCampoOrden("ide_seinf asc,ide_serec asc");
		tab_detalle_seguimiento.getGrid().setColumns(4);
		tab_detalle_seguimiento.dibujar();


		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_detalle_seguimiento);
		pat_panel2.setMensajeWarn("EVALUACION");
		
		
		ItemMenu enviar_plan_accion = new ItemMenu();
		enviar_plan_accion.setValue("Importar Recomendaciones");
		enviar_plan_accion.setMetodo("importarRecomendaciones");
		enviar_plan_accion.setIcon("ui-icon-mail-closed");
		
		/*ItemMenu eliminarImportacion = new ItemMenu();
		eliminarImportacion.setValue("Eliminar Recomendaciones");
		eliminarImportacion.setMetodo("eliminarRecomendaciones");
		eliminarImportacion.setIcon("ui-icon-mail-closed");*/
		
		
		pat_panel2.getMenuTabla().getChildren().add(enviar_plan_accion);
		//pat_panel2.getMenuTabla().getChildren().add(eliminarImportacion);
		

		
		

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "40%", "H");
        agregarComponente(div_division);
		
		
		

        /*Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);*/
    }

   
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
        tab_tabla.insertar();
        	tab_tabla.setLectura(false);
        	tab_tabla.setValor("ide_seusu",usuario);
		}
    }

    @Override
    public void guardar() {
        if (tab_tabla.isFocus()) {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
        }
        if (tab_detalle_seguimiento.isFocus()) {
        if (tab_detalle_seguimiento.guardar()) {
        	
        	validarTipoAccion();
            guardarPantalla();
        }  

        	/*TablaGenerica tab_eva=utilitario.consultar("SELECT ide_sedee, ide_seeva, ide_seinf, ide_serec, ide_seesr, asunto_sedee,activo_sedee "
        			+ "FROM seg_detalle_evaluacion where ide_seeva="+tab_tabla.getValor("ide_seeva"));
        	
        	if (tab_eva.getTotalFilas()>0) {
				for (int i = 0; i < tab_eva.getTotalFilas(); i++) {
					utilitario.getConexion().ejecutarSql("update seg_recomendacion set ide_seesr="+tab_eva.getValor(i,"ide_seesr")+" where ide_serec=" +tab_eva.getValor(i,"ide_serec"));
				}
			}*/
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla.isFocus()) {
        	if(tab_detalle_seguimiento.getTotalFilas()>0){
        		utilitario.agregarMensaje("No se puede eliminar", "Detalle de cabecera contiene valores");
        		return;
        	}else {
        tab_tabla.eliminar();
    }
		}
    }


public void importarRecomendaciones(){
	
	String ide_seeva="";
	
	if (tab_tabla.getValor("ide_seeva")==null || tab_tabla.getValor("ide_seeva").isEmpty()) {
	}else {
		ide_seeva=tab_tabla.getValor("ide_seeva");
		if(tab_detalle_seguimiento.getTotalFilas()>0){
			utilitario.agregarMensaje("Ya se ha importado valores", "No se puede importar valores");
			return;
		}else {
			if (tab_detalle_seguimiento.getTotalFilas()>0) {
				utilitario.agregarMensaje("No se puede realizar esta accion", "Ya se han importado las recomendaciones");
				return;				
			}
			
			if (tab_tabla.getValor("ide_seesi").equals("2")) {
				utilitario.agregarMensaje("No sepuede importar", "Evaluacion se encuentra en estado de Finalizado");
				return;
			}
			copiarFila(ide_seeva);
		}
	}
	
	
	
	
}

public void eliminarRecomendaciones(){
String ide_seeva="";
	
	if (tab_tabla.getValor("ide_seeva")==null || tab_tabla.getValor("ide_seeva").isEmpty()) {
		utilitario.agregarMensaje("No se ha ingresado una cabecera válida", "No se puede eliminar");
		return;
	}else {
		if (tab_tabla.getValor("ide_seesi").equals("2")) {
			utilitario.agregarMensaje("No sepuede eliminar", "Evaluacion se encuentra en estado de Finalizado");
			return;
		}
		
		ide_seeva=tab_tabla.getValor("ide_seeva");
		if(tab_detalle_seguimiento.getTotalFilas()>0){
			utilitario.getConexion().ejecutarSql("delete from  seg_detalle_evaluacion  where ide_seeva=" + ide_seeva);
			tab_detalle_seguimiento.actualizar();
			utilitario.addUpdate("tab_detalle_seguimiento");
			utilitario.agregarMensaje("Ya se ha eliminado", "Todos los registros fueron eliminados");

		}else {
			utilitario.agregarMensaje("No se han encontrado registros", "No se encontraron datos");
			return;
		}

	}
	
	

}



public void copiarFila(String ide_seeva){ 	

	/*TablaGenerica tab_recomendaciones_cumplidas =utilitario.consultar("SELECT ide_sedee, ide_seeva, ide_seinf, ide_serec, ide_seesr, asunto_sedee, "
			+ "activo_sedee, ide_gtemp  "
			+ "FROM seg_detalle_evaluacion "
			+ "where ide_seeva>2 ");	
	
	for (int i = 0; i < tab_recomendaciones_cumplidas.getTotalFilas(); i++) {
		e
	}
	
	
TablaGenerica tab_recomendaciones = utilitario.consultar("select inf.ide_seinf,rec.ide_serec,rec.ide_seesr from  "
			+ "seg_recomendacion rec "
			+ "left join seg_informe inf on rec.ide_seinf=inf.ide_seinf "
			+ "where ide_serec not in(SELECT  ide_serec "
			+ "FROM seg_detalle_evaluacion "
			+ "where ide_seeva>2 and ide_seesr=2)");*/


	TablaGenerica tab_recomendaciones_ = utilitario.consultar("select ide_seeva,numero_seeva from seg_evaluacion order by ide_seeva desc");
String ide_seevaImportar =tab_recomendaciones_.getValor(1,"ide_seeva");		


	TablaGenerica tab_recomendaciones = utilitario.consultar("SELECT ide_sedee, ide_seeva, ide_seinf, ide_serec, ide_seesr, asunto_sedee, "
			+ "activo_sedee, ide_gtemp "
			+ "FROM seg_detalle_evaluacion "
			+ "where ide_seeva in("+ide_seevaImportar+") and ide_seesr not in(2) ");


	
			tab_detalle_seguimiento.setLectura(false);
	for (int i = 0; i < tab_recomendaciones.getTotalFilas(); i++) {
		insertarTablaResumen(ide_seeva, tab_recomendaciones.getValor(i,"ide_seinf"), tab_recomendaciones.getValor(i,"ide_serec"), tab_recomendaciones.getValor(i,"ide_seesr"));
	}
	tab_detalle_seguimiento.actualizar();
	utilitario.addUpdate("tab_detalle_seguimiento");
	utilitario.agregarMensaje("Registros migrados", "Se han importado los registros exitosamente");
}


public void insertarTablaResumen(
		 String ide_seeva ,
		 String ide_seinf,
		 String ide_serec,
		 String ide_seesr
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_detalle_evaluacion", "ide_sedee"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO seg_detalle_evaluacion(" 
					+ "ide_sedee, "
					+ "ide_seeva, "
					+ "ide_seinf, "
			  		+ "ide_serec, "
			  		+ "ide_seesr, "
			  		+ "asunto_sedee, "
			  		+ "activo_sedee)" + 
			  		" values( " +codigo + ", "
			  		+ ""+ ide_seeva+", "
			  		+ ""+ide_seinf+", "
			  		+ ""+ide_serec+", "
			  		+ ""+ide_seesr+", "
			  		+ ""+null+", "
			  		+ "true)"); 
	 
}

 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}


	public Tabla getTab_tabla() {
		return tab_tabla;
	}


	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}


	public Tabla getTab_detalle_seguimiento() {
		return tab_detalle_seguimiento;
	}


	public void setTab_detalle_seguimiento(Tabla tab_detalle_seguimiento) {
		this.tab_detalle_seguimiento = tab_detalle_seguimiento;
	}


	public String getCarpeta() {
		return carpeta;
	}


	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}

	public void validarTipoAccion(){
		//tab_detalle_seguimiento.modificar(evt);
			//Notificacion de Rechazo
					
				    TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
							+ "SEG_ASIGNACION ASI "
							+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
							+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
							+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
							+ "WHERE asi.IDE_seinf="+tab_detalle_seguimiento.getValor("ide_seinf")+" and asi.ide_serec="+tab_detalle_seguimiento.getValor("ide_serec")+" and usu.activo_seusu=true");

				    TablaGenerica tab_detalle=utilitario.consultar("SELECT * FROM  "
							+ "SEG_INFORME INF "
							+ "left join seg_RECOMENDACION REC on INF.IDE_SEINF=REC.IDE_SEINF "
							+ "WHERE INF.IDE_seinf="+tab_detalle_seguimiento.getValor("ide_seinf")+" and REC.ide_serec="+tab_detalle_seguimiento.getValor("ide_serec"));

				    String informe="",recomendacion="";
				    
				    informe=tab_detalle.getValor("numero_seinf");
				    recomendacion=tab_detalle.getValor("numero_serec")+" Asunto:"+tab_detalle.getValor("asunto_serec")+" ";
				    String ide_seesr=tab_detalle_seguimiento.getValor("ide_seesr");
				for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
					try {
						if (ide_seesr.equals("2")) {//CUMPLIDA
							//EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"CUMPLIDA");	
						}else if (ide_seesr.equals("3")) {//NO APLICA
						//	EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"NO APLICA");	
						}
						else if (ide_seesr.equals("4")) {//PENDIENTE
						//	EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"PENDIENTE");	

						}else if (ide_seesr.equals("5")) {//INCUMPLIDA
						//EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"INCUMPLIDA");	

						}else if (ide_seesr.equals("6")) {//INTEGRADA
							//EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"INTEGRADA");	
						}else if (ide_seesr.equals("1")) {//PROCESO
							//EnviarCorreo("", informe, recomendacion, tab_empleado.getValor(i,"ide_gtemp"), 1,"EN PROCESO");	
						}
						
						else {//NO APLICA
							
						}
		
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
					
				try {
					
					if (ide_seesr.equals("2")) {//CUMPLIDA
						//EnviarCorreo("", informe, recomendacion, tab_detalle_seguimiento.getValor("ide_gtemp"), 1,"CUMPLIDA");	
					}else if (ide_seesr.equals("3")) {//NO APLICA
						//EnviarCorreo("", informe, recomendacion, tab_detalle_seguimiento.getValor("ide_gtemp"), 1,"NO APLICA");	
					}
					else if (ide_seesr.equals("4")) {//PENDIENTE
						//EnviarCorreo("", informe, recomendacion, tab_detalle_seguimiento.getValor("ide_gtemp"), 1,"PENDIENTE");	

					}else if (ide_seesr.equals("5")) {//INCUMPLIDA
					//EnviarCorreo("", informe, recomendacion, tab_detalle_seguimiento.getValor("ide_gtemp"), 1,"INCUMPLIDA");	

					}else if (ide_seesr.equals("6")) {//INTEGRADA
						//EnviarCorreo("", informe, recomendacion, tab_detalle_seguimiento.getValor("ide_gtemp"), 1,"INTEGRADA");	
					}else {//NO APLICA
						
					}

					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				///utilitario.getConexion().ejecutarSql("update seg_recomendacion set ide_seesr="+tab_detalle_seguimiento.getValor("ide_seesr")+" where ide_serec=" +tab_detalle_seguimiento.getValor("ide_serec"));
				utilitario.getConexion().ejecutarSql("update seg_detalle_evaluacion set ide_seesr="+tab_detalle_seguimiento.getValor("ide_seesr")+" where ide_sedee=" +tab_detalle_seguimiento.getValor("ide_sedee"));
				utilitario.agregarMensaje("Se ha cambiado el estado de Recomendacion", "Se ha informado al Usuario Responsable");
				//utilitario.addUpdateTabla(tab_detalle_seguimiento,"ide_seesr","");
			//	tab_detalle_seguimiento.actualizar();
			//	utilitario.agregarMensajeInfo("Estado de Recomendacion Actualizada", "");
				
				
		

		
	}			
	


	   public String EnviaMailInterno(EnvioMail enviaMail, String mailReceptor, String asunto, String cuerpo, File filearchivo,String strNombreEmpleado ,String informe,String recomendacion,int tipo_rol,String tipoRespuesta) throws Exception
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
	           
	           if (tipo_rol==1) {
				 htmlText ="<p>Estimado(a) "+strNombreEmpleado+", "
			                + "</p>\n"
			                + "<p>&nbsp;</p>\n"
			                + "El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
			                + "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
			                + "y en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
			                + "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento "
			                + "y cumplimiento. </p>\n"
			                + "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento interno vigente, informa que conforme al "
			                + "Informe  "+informe+"  aprobado el "+tab_tabla.getValor("fecha_fin_seeva")+", en el que indica que el estado de la recomendaci&oacute;n No. "+recomendacion+" es "+tipoRespuesta+", por lo que se dispone a Usted "
			                + "continuar con el control interno, cumpliendo con las recomendaciones y la normativa legal vigente conforme a su competencia. </p>\n"
			                + "<table style=\"height: 144px;\" width=\"571\">\n"
			                + "<tbody>\n"
			                + "<tr>\n"
			                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
			                + "<td width=\"476\">\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
			                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
			                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
			                + "</td>\n"
			                + "</tr>\n"
			                + "</tbody>\n"
			                + "</table>";
				 
				 
				 //System.out.println("PRUEBA CORREO1: "+htmlText);
		
		                   
		           
		           
	           }else if (tipo_rol==2) {

		           htmlText="<p>Estimado(a) "+strNombreEmpleado+", "
		   		                + "</p>\n"
		   		                + "<p>&nbsp;</p>\n"
		   		                + "<p>Notificamos mediante la presente que se ha eliminado la asignacion de la recomendación Nro. "+recomendacion+", del informe Nro. "+informe+" .</p>\n"
		   		                + "<p>&nbsp;</p>\n" 
		   		                + "<p>Saludos cordiales,</p>\n"
		   		                + "<table style=\"height: 144px;\" width=\"571\">\n"
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
		           //System.out.println("PRUEBA CORREO1: "+htmlText);
			}else if (tipo_rol==3) {
				htmlText ="<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
		       	 		+ "auditoría realizadas mediante los diferentes exámenes especiales o auditorías, e informe a la gerencia de su seguimiento y cumplimiento."
		                + "</p>\n"
			            + "<p>Como delegado de Gerencia General con el fin de cumplir con lo establecido en el Procedimiento interno de Seguimiento y Control de Recomendaciones notifica que la recomendaci&oacute;n "+tab_detalle_seguimiento.getValor("numero_serec")+" del informe "+tab_detalle_seguimiento.getValor("numero_seinf")+" fue reasignada a  "+strNombreEmpleado+"   .</p>\n"
			            + "</p>\n"
		                + "<p>El Responsable de la Recomendaci&oacute;n y el Servidor designado son responsables de: </p>\n"
			            + "<p>&nbsp;&nbspa)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacute;n a su cargo. </p>\n"
			            + "<p>&nbsp;&nbspb)	El registro del avance de la documentaci&oacute;n que respalde el cumplimiento de la recomendaci&oacute;n, de acuerdo a los tiempos establecido en el Plan.  </p>\n"
			            + "<p>&nbsp;&nbspc)	Revisar y mantener actualizado la informaci&oacute;n de las recomendaciones que se encuentran cargada en el sistema.  </p>\n"
			            + "<p>&nbsp;&nbspd)	Llevar el control de las recomendaciones y su respectivo archivo f&iacute;sico y/o digital.   </p>\n"
			            + "<p>&nbsp;&nbspe)	Reportar el avance y el cumplimiento de la recomendaci&oacute;n a la M&aacute;xima Autoridad o su delegado.”  </p>\n"
	 	                + "<p>&nbsp;</p>\n"
		                + "<table style=\"height: 144px;\" width=\"571\">\n"
		                + "<tbody>\n"
		                + "<tr>\n"
		                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		                + "<td width=\"476\">\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		                + "</td>\n"
		                + "</tr>\n"
		                + "</tbody>\n"
		                + "</table>";
			}
	           
	            //String htmlText = cuerpo;
	            messageBodyPart.setContent(htmlText, "text/html");
	            multiParte.addBodyPart(messageBodyPart);
	            	            
	         // second part (the image)
	          /*  messageBodyPart = new MimeBodyPart();
	            DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
	            messageBodyPart.setDataHandler(new DataHandler(fds));
	            messageBodyPart.setHeader("Content-ID", "<image>");
	            multiParte.addBodyPart(messageBodyPart);
*/
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


	   
	   public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String informe,String recomendacion,int tipoRol,String tipoRespuesta) {
	        String html ="";
	        
	        if (tipoRol==1) {
		
	       	 html ="<p>Estimado(a) "+strNombreEmpleado+", "
		                + "</p>\n"
		                + "<p>&nbsp;</p>\n"
				                + "El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
				                + "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
				                + "y en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
				                + "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento "
				                + "y cumplimiento. </p>\n"
				                + "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento interno vigente, informa que conforme al "
				                + "Informe  "+informe+"  aprobado el "+tab_tabla.getValor("fecha_fin_seeva")+", en el que indica que el estado de la recomendaci&oacute;n No. "+recomendacion+" es "+tipoRespuesta+", por lo que se dispone a Usted "
				                + "continuar con el control interno, cumpliendo con las recomendaciones y la normativa legal vigente conforme a su competencia. </p>\n"
		                + "<table style=\"height: 144px;\" width=\"571\">\n"
		                + "<tbody>\n"
		                + "<tr>\n"
		                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		                + "<td width=\"476\">\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		                + "</td>\n"
		                + "</tr>\n"
		                + "</tbody>\n"
		                + "</table>";
	       	 
	       	 //System.out.println("PRUEBA CORREO1: "+html);
			 
	       
	        
	        }else if (tipoRol==2) {
	        	 html ="<p>Estimado(a) "+strNombreEmpleado+", "
	 	                + "</p>\n"
	 	                + "<p>&nbsp;</p>\n"
			            + "<p>Notificamos mediante la presente que se ha eliminado la asignacion de la recomendación Nro. "+recomendacion+" , del informe Nro. "+informe+" .</p>\n"
	 	                + "<p>&nbsp;</p>\n"
	 	                + "<p>Saludos cordiales,</p>\n"
	 	                + "<table style=\"height: 144px;\" width=\"571\">\n"
	 	                + "<tbody>\n"
	 	                + "<tr>\n"
	 	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	 	                + "<td width=\"476\">\n"
	 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	 	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	 	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	 	                + "</td>\n"
	 	                + "</tr>\n"
	 	                + "</tbody>\n"
	 	                + "</table>";
	        	 System.out.println("PRUEBA CORREO2: "+html);
	        }else if (tipoRol==3) {
					html ="<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
		       	 		+ "auditoría realizadas mediante los diferentes exámenes especiales o auditorías, e informe a la gerencia de su seguimiento y cumplimiento."
		                + "</p>\n"
			            + "<p>Como delegado de Gerencia General con el fin de cumplir con lo establecido en el Procedimiento interno de Seguimiento y Control de Recomendaciones notifica que la recomendaci&oacuten "+tab_detalle_seguimiento.getValor("numero_serec")+" del informe "+tab_detalle_seguimiento.getValor("numero_seinf")+" fue reasignada a  "+strNombreEmpleado+"   .</p>\n"
			            + "</p>\n"
		                + "<p>El Responsable de la Recomendaci&oacute;n y el Servidor designado son responsables de: </p>\n"
			            + "<p>&nbsp;&nbspa)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacute;n a su cargo. </p>\n"
			            + "<p>&nbsp;&nbspb)	El registro del avance de la documentaci&oacute;n que respalde el cumplimiento de la recomendaci&oacute;n, de acuerdo a los tiempos establecido en el Plan.  </p>\n"
			            + "<p>&nbsp;&nbspc)	Revisar y mantener actualizado la informaci&oacute;n de las recomendaciones que se encuentran cargada en el sistema.  </p>\n"
			            + "<p>&nbsp;&nbspd)	Llevar el control de las recomendaciones y su respectivo archivo f&iacute;sico y/o digital.   </p>\n"
			            + "<p>&nbsp;&nbspe)	Reportar el avance y el cumplimiento de la recomendaci&oacute;n a la M&aacute;xima Autoridad o su delegado.”  </p>\n"
	 	                + "<p>&nbsp;</p>\n"
		                + "<table style=\"height: 144px;\" width=\"571\">\n"
		                + "<tbody>\n"
		                + "<tr>\n"
		                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		                + "<td width=\"476\">\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		                + "</td>\n"
		                + "</tr>\n"
		                + "</tbody>\n"
		                + "</table>";
					System.out.println("PRUEBA CORREO3: "+html);
					
					
			}
		        return html;
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



public void EnviarCorreo(String ide_seasi,String informe, String recomendacion,String ide_gtemp,int tipoRol,String tipoRespuesta) throws Exception{
	String strNombreEmpleado="";

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
	strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				

	//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
	EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
	try {
		
		
		//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol,tipoRespuesta), null);
		String str_mail=correo_envio;
		envMail.setAsunto("SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP");
		envMail.setCuerpoHtml(emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol,tipoRespuesta));
		envMail.setPara(str_mail);
		pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
		
		if(obj.getRespuesta())
		{
			utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
		}
		else
			utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
		//no usar EnviaMailInterno
		EnviaMailInterno(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol, tipoRespuesta), null,strNombreEmpleado,informe,recomendacion,tipoRol, tipoRespuesta);
		System.out.println("Enviando Correo a: "+strNombreEmpleado);

		} catch (Exception e) {
		System.out.println("Error en el envio de correo"+e.getMessage());
			}
		} catch (Exception e) {
		//	e.printStackTrace();
			utilitario.agregarMensajeError("Ha ocurrido un error al envio de correo de asignacion funcionario: ", ""+strNombreEmpleado);
		}

			} 		

  
}
