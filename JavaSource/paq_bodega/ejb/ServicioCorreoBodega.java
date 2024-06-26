package paq_bodega.ejb;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;


@Stateless
@LocalBean
public class ServicioCorreoBodega  {
	
	private Utilitario utilitario = new Utilitario();

	
	
	public void enviarCorreoSolicitud(String idSolicitante, String numeroSolicitud,int tipo, String estado){
		
		String sqlCorreos="select ide_gtemp,detalle_gtcor  "
				+ "from gth_correo  "
				+ "where ide_gtemp="+"'" +idSolicitante+ "'"+" "
				+ "and activo_gtcor=true and notificacion_gtcor=true";
		
		String sqlInfoUsuario="select *"
				+ "from gth_empleado  "
				+ "where ide_gtemp="+"'" +idSolicitante+ "'"+" ";
		
		TablaGenerica tab_correo= utilitario.consultar(sqlCorreos);
		TablaGenerica tab_usuario= utilitario.consultar(sqlInfoUsuario);
		
		String correo=tab_correo.getValor("detalle_gtcor");
		String nombreSolicitante=tab_usuario.getValor("primer_nombre_gtemp");
		String apellidoSolicitante=tab_usuario.getValor("apellido_paterno_gtemp");
		String nombreCompleto= nombreSolicitante+" " +apellidoSolicitante;
		
		
		
		System.out.println("Correo CORREO " + correo );
		System.out.println("Correo Nombre Solicitante " + nombreSolicitante );

		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=3"); 
		
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		
		java.util.Date fecha = new Date();
		System.out.println (fecha);

		EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);
		
		switch (tipo) { 
	    case 1:
	    	try {
				envMail.setAsunto("SOLICITUD MATERIALES E INVENTARIOS");
				envMail.setCuerpoHtml(emailNotificacionUsuario(nombreCompleto, "Solicitud de materiales", fecha.toString(), numeroSolicitud,estado));
				envMail.setPara(correo);

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
				
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
			}

	     break;
	    case 2:
	    	try {
				envMail.setAsunto("SOLICITUD MATERIALES E INVENTARIOS");
				envMail.setCuerpoHtml(emailNotificacionAprobador(nombreCompleto, "Solicitud de materiales", fecha.toString(), numeroSolicitud,estado));
				envMail.setPara(correo);

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
				
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
			}
	     break;

	    case 3 :
	    	try {
				envMail.setAsunto("SOLICITUD MATERIALES E INVENTARIOS");
				envMail.setCuerpoHtml(emailNotificacionEstadoAprobadoAprobador(nombreCompleto, "Solicitud de materiales", fecha.toString(), numeroSolicitud,estado));
				envMail.setPara(correo);

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
				
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
			}
	     break;
	     
	    case 4 :
	    	try {
				envMail.setAsunto("SOLICITUD MATERIALES E INVENTARIOS");
				envMail.setCuerpoHtml(emailNotificacionEstadoAprobadoBodega(nombreCompleto, "Solicitud de materiales", fecha.toString(), numeroSolicitud,estado));
				envMail.setPara(correo);

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
				
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
			}
	    default:
	
	  }
		
		
		
		
	}
	
	
	public String emailNotificacionUsuario(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh, String numeroSolicitud, String estado) {
	      String html = "<p>Estimado: "+strNombreEmpleado+  "</p>"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Usted  ha realizado la solicitud No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros).</p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Nota: se requiere autorización </p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Saludos cordiales,</p>\n"
	              + "<table style=\"height: 144px;\" width=\"571\">\n"
	              + "<tbody>\n"
	              + "<tr>\n"
	              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	              + "<td width=\"476\">\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
	              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	              + "</td>\n"
	              + "</tr>\n"
	              + "</tbody>\n"
	              + "</table>";
	      return html;
	  }
	
	public String emailNotificacionAprobador(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh, String numeroSolicitud, String estado) {
	      String html = "<p>Estimado: "+strNombreEmpleado+  "</p>"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Se ha  generado una nueva solicitu No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros).</p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Nota: Es necesario que usted la autorice en el sistema ERP</p>\n"
	              + "<p>&nbsp;</p>\n"
	              + "<p>Saludos cordiales,</p>\n"
	              + "<table style=\"height: 144px;\" width=\"571\">\n"
	              + "<tbody>\n"
	              + "<tr>\n"
	              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	              + "<td width=\"476\">\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
	              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	              + "</td>\n"
	              + "</tr>\n"
	              + "</tbody>\n"
	              + "</table>";
	      return html;
	  }
	
	public String emailNotificacionEstadoAprobadoAprobador(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh, String numeroSolicitud, String estado) {
		 String html="";
		if(estado.contains("APROBADA")){
		       html = "<p>Estimado: "+strNombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>La solicitud  No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros) ha sido "+estado+".</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Nota: se requiere autorización de bodega para el despacho </p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
		      
		}
		if(estado.contains("RECHAZADA")){
			html = "<p>Estimado: "+strNombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>La solicitud  No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros) ha sido "+estado+".</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Nota: Puede generear una nueva solicitud </p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
			
			
		}
		return html;	

	  }
	
	public String emailNotificacionEstadoAprobadoBodega(String strNombreEmpleado ,String detallePermiso,String fecha_solicitud_aspvh, String numeroSolicitud, String estado) {
		 String html="";
		if(estado.contains("APROBADA BODEGA")){
		       html = "<p>Estimado: "+strNombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>La solicitud  No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros) ha sido APROBADA POR BODEGA.</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Nota: Por favor acercarse a la unidad de bodega </p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
		      
		}
		if(estado.contains("RECHAZADA BODEGA")){
			html = "<p>Estimado: "+strNombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>La solicitud  No." +numeroSolicitud+" , para el requerimiento de inventarios (materiales suministros) ha sido RECHAZADA POR BODEGA.</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Nota: Puede generear una nueva solicitud </p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>ADMINISTRACION</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
			
			
		}
		return html;	
	  }

}
