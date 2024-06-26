/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.model.UploadedFile;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;
import pckEntidades.EnvioMail;
import persistencia.Conexion;
import portal.entidades.AsiPermisoJustificacion;
import portal.entidades.GenEmpleadosDepartamentoPar;
import portal.entidades.GthEmpleado;
import portal.entidades.VehRuta;
import portal.entidades.VehSolicitud;
import portal.entidades.VehSolicitudArchivo;
import portal.entidades.VehSolicitudEstado;
import portal.entidades.VehSolicitudOcupante;
import portal.entidades.VehSolicitudRuta;
import portal.servicios.ServicioEmpleadoJPA;
import portal.servicios.ServicioVehiculoJPA;
import framework.aplicacion.TablaGenerica;

/**
 * 
 * @author Alex Becerra
 */

@ManagedBean
@ViewScoped
public class ControladorVehiculo {

	private GthEmpleado empleado;
	private VehSolicitudEstado estado = new VehSolicitudEstado(Long.parseLong("1"));
	private String strOpcion = "1";
	private Utilitario utilitario = new Utilitario();

	//private String p_responsable_transporte = utilitario.getVariable("p_responsable_transporte");
	
	@EJB
	private ServicioContabilidad ser_contabilidad;
	@EJB
	private ServicioEmpleadoJPA servicioEmpleado;
	@EJB
	private ServicioVehiculoJPA servicioVehiculo;
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	private VehSolicitud solicitudVehiculo;
	private VehSolicitudRuta solicitudVehiculoRuta;

	@EJB
	private ServicioEmpleado ser_empleado;
	
	private Integer totalSegundosHoraEntrada = 0;
	private Integer totalSegundosHoraSalida = 612000;
	//private String strPathReporte;
	String hora_inicial_reporte = "";
	String hora_final_reporte = "";
	String nuemro_horas_reporte = "";

	int obtieneNroDias;
	String nombreTipoSolicitud;
	double nroHoras;
	String ide_aspvh_correo;
	String motivo;

	// Define el ide del empleado que genero la solicitud
	private List listaGeneraSolicitud;
	// A nombre de quien sale la solicitud
	private List listaNombreSolicita;
	// A nombre de quien sale la solicitud
	private List listDepartamentos;

	private Integer codigoEmpleado = 0;
	private List<VehSolicitud> listaSolicitudes;
	private List<VehSolicitud> listaSolicitudesRecibidas;
	private String descripcionDepartamento;
	private List listaRuta;
	private String descripcionRuta;
	private int ideRuta;
	private Boolean esOtro = true;
	private int ideRutaSalida;
	private Boolean esOtroSalida = true;
	private String descripcionSalidaRuta;
	private List listaTipoVehiculo;
	private List listaSucursal;
	private int ideTipoVehiculo;
	private String descripcionTipoVehiculo;
	private int ideGtempOc;
	private List<VehSolicitudOcupante> listaOcupantes; //lista de telefonos del usuario
    private String ocupanteEliminado;
    private VehSolicitudArchivo solicitudJustificacion;
    private UploadedFile adjunto;

	@PostConstruct
	public void cargarDatos() {

		empleado = servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP"));
		
		listaGeneraSolicitud = servicioVehiculo.getGeneraSolicitud(empleado.getIdeGtemp().toString());
		listaNombreSolicita = servicioVehiculo.getNombreSolicitud();
		listDepartamentos = servicioVehiculo.getDepartamentos(empleado.getIdeGtemp().toString());

		
		solicitudVehiculo = new VehSolicitud();
		solicitudVehiculo.setFechaSolicitudVesol(utilitario.getDate());
		solicitudVehiculo.setIdeVetes(new VehSolicitudEstado(Long.parseLong("1")));
		// -----------------
		solicitudVehiculo.setIdeGtemp(empleado);
		solicitudVehiculo.setGenIdeGtemp(BigInteger.valueOf(empleado.getIdeGtemp()));
//		solicitudVehiculo.setGenIdeGtemp(servicioEmpleado.getEmpleadoDepartamentoPartida(solicitudVehiculo.getIdeGtemp().getIdeGtemp().toString()).getIdeGtemp());
		solicitudVehiculo.setIdeGeedp(new GenEmpleadosDepartamentoPar());
		
		listaSolicitudes = servicioVehiculo.getSolicitudesVacaciones(empleado.getIdeGtemp().toString(),"ideGtemp");
		
		if (listaSolicitudes == null) {
			listaSolicitudes = new ArrayList<VehSolicitud>();
		} 
		
		listaOcupantes= new ArrayList<VehSolicitudOcupante>();

		listaRuta = servicioVehiculo.getRutaVehiculo();
		listaTipoVehiculo=servicioVehiculo.getTipoVehiculo();
		listaSucursal=servicioVehiculo.getSucursales();

		seleccionaEmpleadoDefault();

		solicitudVehiculoRuta = new VehSolicitudRuta();
		solicitudVehiculoRuta.setIdeVerut(new VehRuta());
		solicitudVehiculoRuta.setIdeVesol(new VehSolicitud());
		solicitudVehiculoRuta.setIdeSalidaVerut(7l);
		solicitudVehiculoRuta.getIdeVerut().setIdeVerut(7l);
		esOtro = false;
		esOtroSalida = false;
		solicitudVehiculoRuta.setDetalleSalidaVesru("Ciudad, Parroquia/Sector y direccion");
		solicitudVehiculoRuta.setDetalleVesru("Ciudad, Parroquia/Sector y direccion");

		solicitudJustificacion = new VehSolicitudArchivo();
	}

	public void calcularHoras(AjaxBehaviorEvent evt) {
		calcularHoras();
	}

	public void calcularHoras(DateSelectEvent evt) {
		calcularHoras();
	}

	public void calcularHoras() {
		try {
			if (solicitudVehiculo.getHoraSalidaVesol() == null || solicitudVehiculo.getHoraRetornoVesol() == null)
				return;

			if(utilitario.isHoraLaboral(solicitudVehiculo.getHoraSalidaVesol(),false)){
				utilitario.agregarMensajeInfo("Rango de Horas fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA ");
				if(solicitudJustificacion.getDetalleVesoa().length()<5 || adjunto==null)
				{
					utilitario.agregarMensajeInfo("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
					//return;
				}
			}
			
			if(utilitario.isHoraLaboral(solicitudVehiculo.getHoraRetornoVesol(),false)){
				utilitario.agregarMensajeInfo("Rango de Horas fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA ");
				if(solicitudJustificacion.getDetalleVesoa().length()<5 || adjunto==null)
				{
					utilitario.agregarMensajeInfo("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
					//return;
				}
			}

			double horasSolicitud = servicioVehiculo.calcularHoras(
					solicitudVehiculo.getHoraSalidaVesol(),
					solicitudVehiculo.getHoraRetornoVesol());
//			System.out.println("el tiempo es" + horasSolicitud);

			solicitudVehiculo.setTiempoSolicitadoVesol(new BigDecimal(horasSolicitud).setScale(2, RoundingMode.HALF_UP));


		} catch (Exception e) {
			System.out.println("Error calcularHoras: "+ e.getMessage());
			solicitudVehiculo.setTiempoSolicitadoVesol(null);
			utilitario.agregarMensajeInfo("Horas no válidas", "No se pudo calcular el número de horas " + e.getMessage());
		}
	}

	public void calcularDias(DateSelectEvent evt) {
		
//		System.out.println("fecha salida antes del calculo"+ solicitudVehiculo.getFechaSalidaVesol());
//		solicitudVehiculo.setFechaRegresoVesol(solicitudVehiculo.getFechaSalidaVesol());
		calcularDias();
	}

	public void calcularDias(AjaxBehaviorEvent evt) {
//		System.out.println("fecha salida antes del calculo"+ solicitudVehiculo.getFechaRegresoVesol());
//		solicitudVehiculo.setFechaRegresoVesol(solicitudVehiculo.getFechaSalidaVesol());
		calcularDias();
	}

	private void calcularDias() {
		
		if (solicitudVehiculo.getFechaSalidaVesol() == null	|| solicitudVehiculo.getFechaRegresoVesol() == null) {
			return;
		}
		
		/*if(!utilitario.isDiaLaboral(solicitudVehiculo.getFechaSalidaVesol())){
			utilitario.agregarMensajeInfo("Solicitud fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA...");
			return;
		}*/

		if (utilitario.isFechasValidas(utilitario.getFormatoFecha(solicitudVehiculo.getFechaSalidaVesol()),
				utilitario.getFormatoFecha(solicitudVehiculo.getFechaRegresoVesol()))) {
			solicitudVehiculo.setDiasSolicitadoVesol(new Integer((utilitario.getDiferenciasDeFechas(
							solicitudVehiculo.getFechaSalidaVesol(),solicitudVehiculo.getFechaRegresoVesol()) + 1)+ ""));
			//double dou_solicita = pckUtilidades.CConversion.CDbl_2(solicitudVehiculo.getDiasSolicitadoVesol()) * 8;

		} else {
			utilitario.agregarMensajeInfo("Fechas no válidas","La Fecha Hasta debe ser mayor o igual a la Fecha Desde");
		}
	}
	
	public void agregarOcupante(ActionEvent evt) {
		//private GthEmpleado ocupante;
		if(ideGtempOc>0)
		{
			VehSolicitudOcupante vso=new VehSolicitudOcupante();
			GthEmpleado ocupante = servicioEmpleado.getEmpleado(ideGtempOc+"");
			vso.setIdeGtemp(ocupante);	       
	        listaOcupantes.add(vso);
	        ideGtempOc=0;
		}else {
            utilitario.agregarMensaje("No se puede agregar", "Seleccione un ocupante");
        }
    }
	
	public void eliminarOcupante(VehSolicitudOcupante elOc) {
		utilitario.agregarMensajeInfo("Lista de Ocupantes", "Se quito el ocupante: "+elOc.getIdeGtemp().getPrimerNombreGtemp()+" "+elOc.getIdeGtemp().getApellidoPaternoGtemp());
		listaOcupantes.remove(elOc);
    }

	public void guardarSolicitud() {
				
		if(pckUtilidades.CConversion.CDbl(solicitudVehiculo.getTiempoSolicitadoVesol())<=0)
		{
			utilitario.agregarMensajeError("No se puede guardar", "El tiempo solicitado debe de ser mayor a CERO.");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(solicitudVehiculo.getOcupantesVesol())<=0)
		{
			utilitario.agregarMensajeError("No se puede guardar", "El número minimo de ocupantes es 1.");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(solicitudVehiculo.getOcupantesVesol())>4)
		{
			utilitario.agregarMensajeError("No se puede guardar", "El número maximo de ocupantes es 4.");
			return;
		}
		
		if(listaOcupantes.size()<=0)
		{
			utilitario.agregarMensajeError("No se puede guardar", "Favor ingrese almenos un ocupante.");
			return;
		}
		
		if(listaOcupantes.size() != pckUtilidades.CConversion.CInt(solicitudVehiculo.getOcupantesVesol()))
		{
			utilitario.agregarMensajeError("No se puede guardar", "Favor revise la lista de ocupante. Debe ser igual al numero de: (Ocupantes : "+solicitudVehiculo.getOcupantesVesol()+")");
			return;
		}
		
		if(utilitario.getDiferenciasDeFechas(solicitudVehiculo.getFechaSolicitudVesol(), solicitudVehiculo.getFechaSalidaVesol())<1)
		{
			utilitario.agregarMensajeError("No se puede guardar", "Las solicitudes se ingresan con un 1 dias de anticipacion.");
			return;
		}
		
		if(!utilitario.isDiaLaboral(solicitudVehiculo.getFechaSolicitudVesol())){
			utilitario.agregarMensajeInfo("Solicitud fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA...");
			if(pckUtilidades.CConversion.CStr(solicitudJustificacion.getDetalleVesoa()).length()<1 || adjunto==null)
			{
				utilitario.agregarMensajeError("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
				return;
			}
		}
		
		if(utilitario.isHoraLaboral(utilitario.getDate(),true)){
			utilitario.agregarMensajeInfo("Rango de Horas fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA ");
			if(pckUtilidades.CConversion.CStr(solicitudJustificacion.getDetalleVesoa()).length()<1 || adjunto==null)
			{
				utilitario.agregarMensajeError("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
				return;
			}
		}
		
		if(pckUtilidades.CConversion.CInt(solicitudVehiculo.getGenIdeGtemp())<1)
		{
			utilitario.agregarMensajeError("No se puede guardar", "Debe seleccionar una persona solicitante.");
			return;
		}

		if(utilitario.isHoraLaboral(solicitudVehiculo.getHoraRetornoVesol(),false)==true){
			utilitario.agregarMensajeInfo("Rango de Horas fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA ");
			if(pckUtilidades.CConversion.CStr(solicitudJustificacion.getDetalleVesoa()).length()<1 || adjunto==null)
			{
				utilitario.agregarMensajeError("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
				return;
			}
		}

		if(utilitario.isHoraLaboral(solicitudVehiculo.getHoraSalidaVesol(),false)==true){
			utilitario.agregarMensajeInfo("Rango de Horas fuera de horario laboral", "El agendamiento solicitado debe ser realizado mediante SITRA ");
			if(pckUtilidades.CConversion.CStr(solicitudJustificacion.getDetalleVesoa()).length()<1 || adjunto==null)
			{
				utilitario.agregarMensajeError("Carge su Justificación", "Favor carge el archivo del SITRA y su Justificación...");
				return;
			}
		}
		
		if (adjunto!=null) {
			if(pckUtilidades.CConversion.CStr(solicitudJustificacion.getDetalleVesoa()).length()<1)
			{
				utilitario.agregarMensajeError("Carge su Justificación", "Favor ingrese un detalle de justificación conforme al archivo adjunto.");
				return;
			}
			
		}

		solicitudVehiculo.setDepartamentoVesol(descripcionDepartamento);
		solicitudVehiculo.setActivoVesol(true);
		solicitudVehiculo.setIdeVetes(new VehSolicitudEstado(Long.parseLong("1")));
		solicitudVehiculo.setTipoCalculoVesol(Integer.parseInt("1"));

		if (this.solicitudVehiculo.getGenIdeGtemp().compareTo(
				new BigInteger("0")) == 0) {
			solicitudVehiculo.setGenIdeGtemp(null);
		}
		
		solicitudJustificacion.setFechaVesoa(new Date());
		String str_mensaje="";
		System.out.println("Intentando generar el agendamiento vehicular...." );
		if(subirAdjunto())
		{
			
			if (solicitudJustificacion.getArchivoVesoa()==null) {
				
			}else if(solicitudJustificacion.getArchivoVesoa().length()>0){
				utilitario.agregarMensaje("Archivo Adjunto", "Se guardo correctamente...");
			}
			
			str_mensaje = servicioVehiculo.guardarSolicitudVehiculo(
				solicitudVehiculo, solicitudVehiculoRuta, listaOcupantes, solicitudJustificacion);
		}

		
		Correo();
		if (str_mensaje.isEmpty()) {
			utilitario.agregarMensaje("Se guardo Correctamente", "");
			cargarDatos();
		} else {
			utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
		}

	}

	/**
	 * metodo para enviar correo
	 */
	
	public void Correo() {
		try {
			TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, clave_corr from sis_correo where ide_corr=3");

			TablaGenerica tab_correo_plantilla = utilitario.consultar("select ide_cpla,co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co "+
					"left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "+
					"where activo_cpla = true and activo_corr = true and co.ide_corr=3 order by 1;");

			EnvioMail envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"),
					tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"), tab_correo_envio.getValor("clave_corr"));

			//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();
			
			TablaGenerica tab_empleado = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(solicitudVehiculo.getIdeGtemp().getIdeGtemp()+""));
			TablaGenerica tab_solicitud_estado = utilitario.consultar("select ide_vetes, detalle_vetes from veh_solicitud_estado where ide_vetes="+solicitudVehiculo.getIdeVetes().getIdeVetes()+" ;");
			TablaGenerica tab_jefe = ser_nomina.ideEmpleadoContrato(solicitudVehiculo.getIdeGeedp().getIdeGeedp()+"","true,false");
			TablaGenerica tab_jefe_correo = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_jefe.getValor("IDE_GTEMP")));
			
			System.out.println("Ingresa a correo");
			String str_asunto="SOLICITUD DE AGENDAMIENTO VEHICULAR";
			String str_mensaje=tab_correo_plantilla.getValor(0,"plantilla_cpla");
			String str_fecha=utilitario.getFechaLarga(utilitario.DeDateAString(solicitudVehiculo.getFechaSalidaVesol()));
			String str_hora=utilitario.DeDateAStringHora(solicitudVehiculo.getHoraSalidaVesol());
			String str_estado=tab_solicitud_estado.getValor("detalle_vetes");
			String str_motivo=solicitudVehiculo.getMotivoVesol();
			String str_jefeAprobador = pckUtilidades.CConversion.CStr(tab_jefe.getValor("PRIMER_NOMBRE_GTEMP"))+" "+pckUtilidades.CConversion.CStr(tab_jefe.getValor("APELLIDO_PATERNO_GTEMP"));
			
			str_mensaje=str_mensaje.replaceAll("@FECHA", str_fecha);
			str_mensaje=str_mensaje.replaceAll("@HORA", str_hora);
			str_mensaje=str_mensaje.replaceAll("@PLACA", "");
			str_mensaje=str_mensaje.replaceAll("@CONDUCTOR", "");
			str_mensaje=str_mensaje.replaceAll("@ESTADO", str_estado);
			str_mensaje = str_mensaje.replaceAll("@APROBADO", "NO");
			str_mensaje = str_mensaje.replaceAll("@JEFEAPROBADOR", str_jefeAprobador);
			str_mensaje=str_mensaje.replaceAll("@SALIDA", solicitudVehiculoRuta.getDetalleSalidaVesru());
			str_mensaje=str_mensaje.replaceAll("@DESTINO", solicitudVehiculoRuta.getDetalleVesru());
			str_mensaje=str_mensaje.replaceAll("@MOTIVO", str_motivo);
			String str_funcionario="";
			String str_mail="";

			//correo de la persona que le hacen la solicitud
			try {
				
				//Funcionario quien solicita
		    	TablaGenerica tab_emp_solicita = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(solicitudVehiculo.getGenIdeGtemp()+""));
		    			
				str_mail= tab_emp_solicita.getValor("detalle_gtcor"); //tab_solicitud.getValor("gen_ide_gtemp"); 
				System.out.println("enviarCorreo agendamiento tab_emp_solicita: "+str_mail);
				//str_mail = "alex.becerra@emgirs.gob.ec";	
				
				str_funcionario=tab_emp_solicita.getValor("nombres");
				str_mensaje=str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
	
				envMail.setAsunto(str_asunto);
				envMail.setCuerpoHtml(str_mensaje);
				envMail.setPara(str_mail);
				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);
			    
				try {
				    //Funcionario quien da haciendo la solicitud
				    if (pckUtilidades.CConversion.CInt(solicitudVehiculo.getGenIdeGtemp()) != solicitudVehiculo.getIdeGtemp().getIdeGtemp()) {
						str_mail=tab_empleado.getValor("detalle_gtcor");
						System.out.println("enviarCorreo agendamiento tab_empleado: "+str_mail);
						//str_mail = "alex.becerra@emgirs.gob.ec";	
					    //util.EnviaMailInterno(envMail,str_mail,str_asunto,str_mensaje, null);
						envMail.setAsunto(str_asunto);
						envMail.setCuerpoHtml(str_mensaje);
						envMail.setPara(str_mail);
						if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
						{
							utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
						}
						else
							utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);
				    }
				} catch (Exception e) {
					utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + str_mail, "");
					System.out.println("Ha ocurrido un error al enviar el email emailNotificacionAprobado1: " + e.getMessage()); 
				}
					
			} catch (Exception e) {
				System.out.println("Error al enviar el correo tab_empleado: "+e.getMessage());
			}		
			
			// correo para el jefe inmediato para su aprobacion
			try {
				System.out.println("Ingresa a correo jefe inmediato");
				str_asunto="SOLICITUD DE APROBACIÓN AGENDAMIENTO VEHICULAR";
				/*str_mensaje=tab_correo_plantilla.getValor(1,"plantilla_cpla");
				str_mensaje=str_mensaje.replaceAll("@FECHA", str_fecha);
				str_mensaje=str_mensaje.replaceAll("@HORA", str_hora);
				str_mensaje=str_mensaje.replaceAll("@PLACA", "");
				str_mensaje=str_mensaje.replaceAll("@CONDUCTOR", "");
				str_mensaje=str_mensaje.replaceAll("@ESTADO", str_estado);
				str_mensaje=str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
				str_mensaje = str_mensaje.replaceAll("@APROBADO", "NO");
				str_mensaje = str_mensaje.replaceAll("@JEFEAPROBADOR", str_jefeAprobador);
				str_mensaje=str_mensaje.replaceAll("@SALIDA", solicitudVehiculoRuta.getDetalleSalidaVesru());
				str_mensaje=str_mensaje.replaceAll("@DESTINO", solicitudVehiculoRuta.getDetalleVesru());*/
				
				str_mail= tab_jefe_correo.getValor("detalle_gtcor"); 
				System.out.println("enviarCorreo agendamiento tab_jefe: "+str_mail);
				//str_mail = "alexbec0000@gmail.com";
			    //util.EnviaMailInterno(envMail,str_mail,str_asunto,str_mensaje, null);
				envMail.setAsunto(str_asunto);
				envMail.setCuerpoHtml(str_mensaje);
				envMail.setPara(str_mail);
				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);

			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + str_mail, "");
				System.out.println("Ha ocurrido un error al enviar el email emailNotificacionAprobado1: " + e.getMessage()); 
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Ha ocurrido un error al enviar el email()" + e.getMessage()); 
			utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email", "");
		}

	}

	public void seleccionaEmpleado(AjaxBehaviorEvent evt) {
		try {

			if ((!this.solicitudVehiculo.getGenIdeGtemp().toString().isEmpty() || this.solicitudVehiculo
					.getGenIdeGtemp().toString() != null)) {
				codigoEmpleado = this.solicitudVehiculo.getGenIdeGtemp()
						.intValue();
				TablaGenerica tab_departamento = utilitario
						.consultar("select par.ide_gtemp,depar.detalle_gedep from gen_empleados_departamento_par par  "
								+ "left join gen_departamento depar on depar.ide_gedep=par.ide_gedep "
								+ "left join gen_area area on area.ide_geare=par.ide_geare "
								+ "where par.activo_geedp=true and par.ide_gtemp=+"
								+ codigoEmpleado
								+ " "
								+ "group by  par.ide_gtemp,depar.detalle_gedep "
								+ "order by  par.ide_gtemp");
				tab_departamento.ejecutarSql();
				descripcionDepartamento = tab_departamento.getValor(
						"detalle_gedep").toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void seleccionaEmpleadoDefault() {
		try {
			codigoEmpleado = Integer.parseInt(utilitario.getVariable(
					"IDE_GTEMP").toString());
			TablaGenerica tab_departamento = utilitario
					.consultar("select par.ide_gtemp,depar.detalle_gedep from gen_empleados_departamento_par par  "
							+ "left join gen_departamento depar on depar.ide_gedep=par.ide_gedep "
							+ "left join gen_area area on area.ide_geare=par.ide_geare "
							+ "where par.activo_geedp=true and par.ide_gtemp=+"
							+ codigoEmpleado
							+ " "
							+ "group by  par.ide_gtemp,depar.detalle_gedep "
							+ "order by  par.ide_gtemp");
			tab_departamento.ejecutarSql();
			descripcionDepartamento = tab_departamento
					.getValor("detalle_gedep").toString();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void seleccionaRuta(AjaxBehaviorEvent evt) {

		this.solicitudVehiculoRuta.getIdeVerut();
		esOtro = true;
		
		final long codigoRuta = this.solicitudVehiculoRuta.getIdeVerut().getIdeVerut();
		Object obj = CollectionUtils.find(this.listaRuta, new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				// Declaro lista de objetos
				Object[] obj = (Object[]) arg0;
				if (obj[0].equals(codigoRuta)) {
					return true;
				}
				return false;
			}
		});

		Object[] motivoSel = (Object[]) obj;

		ideRuta = pckUtilidades.CConversion.CInt(motivoSel[0]);
		descripcionRuta = (String) motivoSel[1];
		//System.out.println("seleccionaRuta "+ideRuta);
		//System.out.println("seleccionaRuta "+descripcionRuta);
		solicitudVehiculoRuta.setDetalleVesru(descripcionRuta);

		if (ideRuta == 7) {
			esOtro = false;
			solicitudVehiculoRuta.setDetalleVesru("Ciudad, Parroquia/Sector y dirección");
		}

	}

	public void seleccionaRutaSalida(AjaxBehaviorEvent evt) {

		this.solicitudVehiculoRuta.getIdeSalidaVerut();
		esOtroSalida = true;
		
		final long codigoRutaSalida = this.solicitudVehiculoRuta.getIdeSalidaVerut();
		Object obj = CollectionUtils.find(this.listaRuta, new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				// Declaro lista de objetos
				Object[] obj = (Object[]) arg0;
				if (obj[0].equals(codigoRutaSalida)) {
					return true;
				}
				return false;
			}
		});

		Object[] motivoSel = (Object[]) obj;
		ideRutaSalida = pckUtilidades.CConversion.CInt(motivoSel[0]);
		descripcionSalidaRuta = (String) motivoSel[1];
		//System.out.println("seleccionaRuta "+ideRuta);
		//System.out.println("seleccionaRuta "+descripcionRuta);
		solicitudVehiculoRuta.setDetalleSalidaVesru(descripcionSalidaRuta);
		if (ideRutaSalida == 7) {
			esOtroSalida = false;
			solicitudVehiculoRuta.setDetalleSalidaVesru("Ciudad, Parroquia/Sector y dirección");
		}

	}
	
	
	public void seleccionaTipoVehiculo(AjaxBehaviorEvent evt) {
		this.solicitudVehiculo.getIdeVetip();
		final long codigoTipoVehiculo = this.solicitudVehiculo.getIdeVetip();
		Object obj = CollectionUtils.find(this.listaTipoVehiculo, new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				// Declaro lista de objetos
				Object[] obj = (Object[]) arg0;
				if (obj[0].equals(codigoTipoVehiculo)) {
					return true;
				}
				return false;
			}
		});

		Object[] motivoSel = (Object[]) obj;
		ideTipoVehiculo = pckUtilidades.CConversion.CInt(motivoSel[0]);
		descripcionTipoVehiculo = (String) motivoSel[1];
		//System.out.println("seleccionaRuta "+ideRuta);
		//System.out.println("seleccionaRuta "+descripcionRuta);
		//solicitudVehiculo.setDetalleVesol(descripcionTipoVehiculo);
	}

	public GthEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(GthEmpleado empleado) {
		this.empleado = empleado;
	}
	
	public boolean subirAdjunto() {
		boolean subir=false;
		try {

			boolean cargo=false;
			
			try
			{
				if(adjunto.getSize() != 0)
					cargo=true;
			} catch (Exception e) {}
			
			if (solicitudJustificacion.getDetalleVesoa().length()>0 && cargo)
			{
				String carpeta="/agendamiento/"+utilitario.getVariable("IDE_GTEMP")+"/";
				String str_nombre = utilitario.getVariable("IDE_USUA") + utilitario.getFechaActual().replace("-", "") + utilitario.getHoraActual().replace(":", "") + adjunto.getFileName().substring(adjunto.getFileName().lastIndexOf("."), adjunto.getFileName().length());
				
				String str_path = utilitario.getPropiedad("rutaUpload") + carpeta;
				File path = new File(str_path);
				path.mkdirs();// Creo el Directorio
				File result = new File(str_path + "/" + str_nombre);
				// /Para el .war
				ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
				str_path = extContext.getRealPath("/upload"+carpeta);
				// empleado.setPathFotoGtemp("/upload/fotos/" + str_nombre);
				path = new File(str_path);
				path.mkdirs();// Creo el Directorio
				File result1 = new File(str_path + "/" + str_nombre);
	
				int BUFFER_SIZE = 6124;
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(result);
					byte[] buffer = new byte[BUFFER_SIZE];
					int bulk;
					InputStream inputStream = adjunto.getInputstream();
					while (true) {
						bulk = inputStream.read(buffer);
						if (bulk < 0) {
							break;
						}
						fileOutputStream.write(buffer, 0, bulk);
						fileOutputStream.flush();
					}
					fileOutputStream.close();
					inputStream.close();
				} catch (IOException e) {
					System.out.println("subirAdjunto() "+e.getMessage());
				}
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(result1);
					byte[] buffer = new byte[BUFFER_SIZE];
					int bulk;
					InputStream inputStream = adjunto.getInputstream();
					while (true) {
						bulk = inputStream.read(buffer);
						if (bulk < 0) {
							break;
						}
						fileOutputStream.write(buffer, 0, bulk);
						fileOutputStream.flush();
					}
					fileOutputStream.close();
					inputStream.close();
					// solicitudJustificacion.setArchivoAspej(str_path);
					solicitudJustificacion.setArchivoVesoa("/upload" +carpeta+ str_nombre);
				} catch (IOException e) {
					System.out.println("Error: subirAdjunto() "+e.getMessage());
				}
				subir=true;
			}
			else
			{
				utilitario.agregarMensajeInfo("Archivo Adjunto", "Usted no adjunto ningun archivo...");
				System.out.println("No se ha seleccionado un archivo adjunto ...");
				subir=true;
			}
			
		} catch (Exception ex) {
			utilitario.agregarMensajeError("Error en el Archivo Adjunto", "Usted no adjunto ningun archivo...");
			System.out.println("Error subirAdjunto: " + ex.getMessage());
			ex.printStackTrace();
		}
		return subir;
	}

	public VehSolicitud getSolicitudVehiculo() {
		return solicitudVehiculo;
	}

	public void setSolicitudVehiculo(VehSolicitud solicitudVehiculo) {
		this.solicitudVehiculo = solicitudVehiculo;
	}

	public Integer getTotalSegundosHoraEntrada() {
		return totalSegundosHoraEntrada;
	}

	public void setTotalSegundosHoraEntrada(Integer totalSegundosHoraEntrada) {
		this.totalSegundosHoraEntrada = totalSegundosHoraEntrada;
	}

	public Integer getTotalSegundosHoraSalida() {
		return totalSegundosHoraSalida;
	}

	public void setTotalSegundosHoraSalida(Integer totalSegundosHoraSalida) {
		this.totalSegundosHoraSalida = totalSegundosHoraSalida;
	}

	public String getStrOpcion() {
		return strOpcion;
	}

	public void setStrOpcion(String strOpcion) {
		this.strOpcion = strOpcion;
	}

	public String getHora_inicial_reporte() {
		return hora_inicial_reporte;
	}

	public void setHora_inicial_reporte(String hora_inicial_reporte) {
		this.hora_inicial_reporte = hora_inicial_reporte;
	}

	public String getHora_final_reporte() {
		return hora_final_reporte;
	}

	public void setHora_final_reporte(String hora_final_reporte) {
		this.hora_final_reporte = hora_final_reporte;
	}

	public String getNuemro_horas_reporte() {
		return nuemro_horas_reporte;
	}

	public void setNuemro_horas_reporte(String nuemro_horas_reporte) {
		this.nuemro_horas_reporte = nuemro_horas_reporte;
	}

	public List getListaGeneraSolicitud() {
		return listaGeneraSolicitud;
	}

	public void setListaGeneraSolicitud(List listaGeneraSolicitud) {
		this.listaGeneraSolicitud = listaGeneraSolicitud;
	}

	public List getListaNombreSolicita() {
		return listaNombreSolicita;
	}

	public void setListaNombreSolicita(List listaNombreSolicita) {
		this.listaNombreSolicita = listaNombreSolicita;
	}

	public List getListDepartamentos() {
		return listDepartamentos;
	}

	public void setListDepartamentos(List listDepartamentos) {
		this.listDepartamentos = listDepartamentos;
	}

	public List<VehSolicitud> getListaSolicitudes() {
		return listaSolicitudes;
	}

	public void setListaSolicitudes(List<VehSolicitud> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

	public List<VehSolicitud> getListaSolicitudesRecibidas() {
		return listaSolicitudesRecibidas;
	}

	public void setListaSolicitudesRecibidas(
			List<VehSolicitud> listaSolicitudesRecibidas) {
		this.listaSolicitudesRecibidas = listaSolicitudesRecibidas;
	}

	public String getDescripcionDepartamento() {
		return descripcionDepartamento;
	}

	public void setDescripcionDepartamento(String descripcionDepartamento) {
		this.descripcionDepartamento = descripcionDepartamento;
	}

	public Integer getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(Integer codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public List getListaRuta() {
		return listaRuta;
	}

	public void setListaRuta(List listaRuta) {
		this.listaRuta = listaRuta;
	}

	public VehSolicitudRuta getSolicitudVehiculoRuta() {
		return solicitudVehiculoRuta;
	}

	public void setSolicitudVehiculoRuta(VehSolicitudRuta solicitudVehiculoRuta) {
		this.solicitudVehiculoRuta = solicitudVehiculoRuta;
	}


	public Boolean getEsOtro() {
		return esOtro;
	}

	public void setEsOtro(Boolean esOtro) {
		this.esOtro = esOtro;
	}

	public VehSolicitudEstado getEstado() {
		return estado;
	}

	public void setEstado(VehSolicitudEstado estado) {
		this.estado = estado;
	}

	public Boolean getEsOtroSalida() {
		return esOtroSalida;
	}

	public void setEsOtroSalida(Boolean esOtroSalida) {
		this.esOtroSalida = esOtroSalida;
	}

	public List getListaTipoVehiculo() {
		return listaTipoVehiculo;
	}

	public void setListaTipoVehiculo(List listaTipoVehiculo) {
		this.listaTipoVehiculo = listaTipoVehiculo;
	}

	public List getListaSucursal() {
		return listaSucursal;
	}

	public void setListaSucursal(List listaSucursal) {
		this.listaSucursal = listaSucursal;
	}

	public int getIdeGtempOc() {
		return ideGtempOc;
	}

	public void setIdeGtempOc(int ideGtempOc) {
		this.ideGtempOc = ideGtempOc;
	}

	public List<VehSolicitudOcupante> getListaOcupantes() {
		return listaOcupantes;
	}

	public void setListaOcupantes(List<VehSolicitudOcupante> listaOcupantes) {
		this.listaOcupantes = listaOcupantes;
	}

	public String getOcupanteEliminado() {
		return ocupanteEliminado;
	}

	public void setOcupanteEliminado(String ocupanteEliminado) {
		this.ocupanteEliminado = ocupanteEliminado;
	}
	
	public UploadedFile getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(UploadedFile adjunto) {
		this.adjunto = adjunto;
	}

	public VehSolicitudArchivo getSolicitudJustificacion() {
		return solicitudJustificacion;
	}

	public void setSolicitudJustificacion(VehSolicitudArchivo solicitudJustificacion) {
		this.solicitudJustificacion = solicitudJustificacion;
	}

	
}
