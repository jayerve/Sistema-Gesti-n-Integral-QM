package paq_transporte;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_transporte.ejb.ServicioTransporte;
import pckEntidades.EnvioMail;

/**
*
* @author Alex Becerra
*/ 
public class pre_jefe_aprobacion extends Pantalla {

	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	private Tabla tab_asolicitud = new Tabla();
	private Tabla tab_asolicitud_ruta = new Tabla();
	private Tabla tab_asolicitud_ocupante = new Tabla();
	private Tabla tab_asolicitud_archivo = new Tabla();
	
	private Confirmar con_guardar=new Confirmar();
	
	String ide_geedp_activo="";
	private String p_responsable_transporte = utilitario.getVariable("p_responsable_transporte");

	public pre_jefe_aprobacion() {
		
		//botones ocultos	
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
		System.out.println("ide_geedp_activo: "+ide_geedp_activo);
		
		Boton bot_aprobar = new Boton();
		bot_aprobar.setValue("Aprobar");
		bot_aprobar.setIcon("ui-icon-check");
		bot_aprobar.setMetodo("aprobar");
		bar_botones.agregarBoton(bot_aprobar);
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_asolicitud.setId("tab_asolicitud");
		tab_asolicitud.setTipoFormulario(true);
		tab_asolicitud.getGrid().setColumns(4);
		tab_asolicitud.setTabla("veh_solicitud", "ide_vesol", 1);

		tab_asolicitud.getColumna("activo_vesol").setNombreVisual("ACTIVO");
		tab_asolicitud.getColumna("departamento_vesol").setNombreVisual("ÁREA O DEPARTAMENTO");
		tab_asolicitud.getColumna("fecha_solicitud_vesol").setNombreVisual("FECHA DE INGRESO");
		tab_asolicitud.getColumna("motivo_vesol").setNombreVisual("MOTIVO DE LA SALIDA");
		tab_asolicitud.getColumna("fecha_salida_vesol").setNombreVisual("FECHA DE SALIDA");
		tab_asolicitud.getColumna("fecha_regreso_vesol").setNombreVisual("FECHA DE RETORNO");
		tab_asolicitud.getColumna("hora_salida_vesol").setNombreVisual("HORA DE SALIDA");
		tab_asolicitud.getColumna("hora_retorno_vesol").setNombreVisual("HORA DE RETORNO EN LA OFICINA");
		tab_asolicitud.getColumna("dias_solicitado_vesol").setNombreVisual("Número de días (dur.)");
		tab_asolicitud.getColumna("tiempo_solicitado_vesol").setNombreVisual("Número de horas (dur.)");
		tab_asolicitud.getColumna("ext_contacto_vesol").setNombreVisual("Num. de ext. de contacto");
		//tab_asolicitud.getColumna("ide_vecon").setCombo("veh_conductor", "ide_vecon", "detalle_vecon", ""); //genera error aki nose xq
		//tab_asolicitud.getColumna("ide_vecon").setCombo("veh_conductor", "ide_vecon", "email_vecon", "");
		tab_asolicitud.getColumna("ide_vetes").setCombo("veh_solicitud_estado", "ide_vetes", "detalle_vetes", "");
		tab_asolicitud.getColumna("ide_vetes").setNombreVisual("ESTADO DE LA SOLICITUD");
		tab_asolicitud.getColumna("ocupantes_vesol").setNombreVisual("OCUPANTES DE LA COMISIÓN");
		tab_asolicitud.getColumna("ide_gtemp").setNombreVisual("EMPLEADO SOLICITADO");
		tab_asolicitud.getColumna("ide_gtemp").setCombo(ser_transporte.getSqlEmpleado());
		tab_asolicitud.getColumna("ide_gtemp").setAutoCompletar();
		tab_asolicitud.getColumna("gen_ide_gtemp").setNombreVisual("A NOMBRE DE QUIEN ES LA COMISIÓN");
		tab_asolicitud.getColumna("gen_ide_gtemp").setCombo(ser_transporte.getSqlEmpleado());
		tab_asolicitud.getColumna("gen_ide_gtemp").setAutoCompletar();
		
		Object fila1[] = { "1", "DISPONIBILIDAD" };
		Object fila2[] = { "0", "CAPACIDAD" };
		List lista = new ArrayList();
		lista.add(fila1);
		lista.add(fila2);
		tab_asolicitud.getColumna("tipo_calculo_vesol").setRadio(lista, "1");
		tab_asolicitud.getColumna("tipo_calculo_vesol").setRadioVertical(true);
		tab_asolicitud.getColumna("tipo_calculo_vesol").setNombreVisual("VERIFICA VEHÍCULO");
		tab_asolicitud.getColumna("ide_veveh").setCombo("veh_vehiculo", "ide_veveh", "PLACA_veveh", "");
		tab_asolicitud.getColumna("ide_veveh").setNombreVisual("VEHÍCULO DISPONIBLE");

		tab_asolicitud.getColumna("observaciones_vesol").setNombreVisual("OBSERVACIONES");
		tab_asolicitud.getColumna("gen_ide_geedp").setNombreVisual("JEFE DE AREA");	
		tab_asolicitud.getColumna("ide_vetip").setCombo("veh_tipo_vehiculo", "ide_vetip", "detalle_vetip", "");
		tab_asolicitud.getColumna("ide_vetip").setAutoCompletar();
		tab_asolicitud.getColumna("ide_vetip").setNombreVisual("TIPO VEHICULO");	
		
		tab_asolicitud.setCondicion(" coalesce(aprobado_vesol,false)=false AND ide_geedp IN ("+ide_geedp_activo+")");
		//tab_asolicitud.setCondicion("ide_vetes=1 and coalesce(aprobado_vesol,false)=false ");
		tab_asolicitud.agregarRelacion(tab_asolicitud_ruta);
		tab_asolicitud.agregarRelacion(tab_asolicitud_ocupante);
		tab_asolicitud.agregarRelacion(tab_asolicitud_archivo);
		tab_asolicitud.setLectura(true);
		tab_asolicitud.dibujar();
		//tab_asolicitud.setLectura(true);
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_asolicitud);

		tab_asolicitud_ruta.setId("tab_asolicitud_ruta");
		tab_asolicitud_ruta.setIdCompleto("tab_tabulador:tab_asolicitud_ruta");
		tab_asolicitud_ruta.setTabla("veh_solicitud_ruta", "ide_vesru", 2);
		tab_asolicitud_ruta.getColumna("ide_vesru").setNombreVisual("CÓDIGO");			
		tab_asolicitud_ruta.getColumna("ide_salida_verut").setCombo("veh_ruta", "ide_verut", "detalle_verut", "");
		tab_asolicitud_ruta.getColumna("ide_salida_verut").setNombreVisual("PUNTO DE LA RUTA ORIGEN");
		tab_asolicitud_ruta.getColumna("detalle_salida_vesru").setNombreVisual("OBSERVACIÓN ORIGEN");
		tab_asolicitud_ruta.getColumna("ide_verut").setCombo("veh_ruta", "ide_verut", "detalle_verut", "");
		tab_asolicitud_ruta.getColumna("ide_verut").setNombreVisual("PUNTO DE LA RUTA DESTINO");		
		tab_asolicitud_ruta.getColumna("detalle_vesru").setNombreVisual("OBSERVACIÓN DESTINO");

		tab_asolicitud_ruta.getColumna("activo_vesru").setVisible(false);
		tab_asolicitud_ruta.setLectura(true);
		tab_asolicitud_ruta.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_asolicitud_ruta);

		tab_asolicitud_ocupante.setId("tab_asolicitud_ocupante");
		tab_asolicitud_ocupante.setIdCompleto("tab_tabulador:tab_asolicitud_ocupante");
		tab_asolicitud_ocupante.setTabla("veh_solicitud_ocupante", "ide_vesoc", 3);
		tab_asolicitud_ocupante.getColumna("IDE_GTEMP").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_asolicitud_ocupante.getColumna("IDE_GTEMP").setLongitud(200);

		tab_asolicitud_ocupante.setLectura(true);
		tab_asolicitud_ocupante.dibujar();

		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_asolicitud_ocupante);
		
		tab_asolicitud_archivo.setId("tab_asolicitud_archivo");
		tab_asolicitud_archivo.setIdCompleto("tab_tabulador:tab_asolicitud_archivo");
		tab_asolicitud_archivo.setTabla("veh_solicitud_archivo", "ide_vesoa", 4);
		tab_asolicitud_archivo.getColumna("archivo_vesoa").setUpload("agendamiento");
		tab_asolicitud_archivo.setLectura(true);
		tab_asolicitud_archivo.dibujar();

		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_asolicitud_archivo);

		tab_tabulador.agregarTab("RUTA", pat_panel2);
		tab_tabulador.agregarTab("OCUPANTES", pat_panel3);
		tab_tabulador.agregarTab("JUSTIFICACIÓN", pat_panel4);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, tab_tabulador, "70%", "H");
		agregarComponente(div_division);

		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
	}

	public void aprobar() {
		
		if(tab_asolicitud.getTotalFilas()>0){
			con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Agendamiento");
			con_guardar.setTitle("CONFIRMACION APROBACIÓN DE SOLICITUD DE AGENDAMIENTO");
			con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar ", "No contiene Solicitudes Pendientes");
		}	
		
	}
	
	public void aceptarAprobarSolicitud(){	
		
		utilitario.getConexion().agregarSqlPantalla("update veh_solicitud set aprobado_vesol=true where ide_vesol="+tab_asolicitud.getValorSeleccionado());
		//tab_asolicitud.setValor("aprobado_vesol", "true");	
		enviarCorreo();
		
		guardarPantalla();
		con_guardar.cerrar();	
		tab_asolicitud.ejecutarSql();
		tab_asolicitud_ruta.ejecutarValorForanea(tab_asolicitud.getValorSeleccionado());
		tab_asolicitud_ocupante.ejecutarValorForanea(tab_asolicitud.getValorSeleccionado());
		tab_asolicitud_archivo.ejecutarValorForanea(tab_asolicitud.getValorSeleccionado());
		utilitario.addUpdate("tab_asolicitud,tab_asolicitud_ruta,tab_asolicitud_ocupante,tab_asolicitud_archivo");
	}
	
	public void enviarCorreo() {
		
		TablaGenerica tab_correo_envio = utilitario.consultar("select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co " + "left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "
				+ "where activo_cpla = true and activo_corr = true and co.ide_corr=3");

		EnvioMail envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"), tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"), tab_correo_envio.getValor("clave_corr"));

		TablaGenerica tab_correo_plantilla = utilitario.consultar("select ide_cpla,co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co "
				+ "left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr " + "where activo_cpla = true and activo_corr = true and co.ide_corr=3 order by 1;");

		TablaGenerica tab_empleado = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_asolicitud.getValor("ide_gtemp")));
		TablaGenerica tab_jefe = ser_nomina.ideEmpleadoContrato(tab_asolicitud.getValor("ide_geedp"),"true,false");
		//TablaGenerica tab_jefe_correo = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_jefe.getValor("ide_gtemp")));
		
		TablaGenerica tab_asolicitud_estado = utilitario.consultar("select ide_vetes, detalle_vetes from veh_solicitud_estado where ide_vetes=" + tab_asolicitud.getValor("ide_vetes") + " ;");
		TablaGenerica tab_asolicitud_ruta = utilitario.consultar("SELECT ide_vesru, detalle_vesru,detalle_salida_vesru FROM veh_solicitud_ruta where ide_vesol=" + tab_asolicitud.getValor("ide_vesol") + " ;");

		String str_asunto = "APROBACIÓN DE SOLICITUD DE AGENDAMIENTO VEHICULAR";
		String str_mensaje = tab_correo_plantilla.getValor(0, "plantilla_cpla");
		String str_fecha = utilitario.getFechaLarga(tab_asolicitud.getValor("fecha_salida_vesol"));
		String str_hora = utilitario.DeDateAStringHora(utilitario.getHoraCalendario(tab_asolicitud.getValor("hora_salida_vesol")));
		String str_estado = pckUtilidades.CConversion.CStr(tab_asolicitud_estado.getValor("detalle_vetes"));
		String str_rutaDestino = pckUtilidades.CConversion.CStr(tab_asolicitud_ruta.getValor("detalle_vesru"));
		String str_rutaOrigen = pckUtilidades.CConversion.CStr(tab_asolicitud_ruta.getValor("detalle_salida_vesru"));
		String str_obs = pckUtilidades.CConversion.CStr(tab_asolicitud.getValor("observaciones_vesol"));
		String str_motivo=pckUtilidades.CConversion.CStr(tab_asolicitud.getValor("motivo_vesol"));
		String str_jefeAprobador = pckUtilidades.CConversion.CStr(tab_jefe.getValor("PRIMER_NOMBRE_GTEMP"))+" "+pckUtilidades.CConversion.CStr(tab_jefe.getValor("APELLIDO_PATERNO_GTEMP"));
		str_mensaje = str_mensaje.replaceAll("@FECHA", str_fecha);
		str_mensaje = str_mensaje.replaceAll("@HORA", str_hora);
		str_mensaje = str_mensaje.replaceAll("@ESTADO", str_estado);
		str_mensaje = str_mensaje.replaceAll("@APROBADO", "SI");
		str_mensaje = str_mensaje.replaceAll("@JEFEAPROBADOR", str_jefeAprobador);
		str_mensaje = str_mensaje.replaceAll("@OBSERVACION", str_obs);
		str_mensaje=str_mensaje.replaceAll("@PLACA", "");
		str_mensaje=str_mensaje.replaceAll("@CONDUCTOR", "");
		str_mensaje=str_mensaje.replaceAll("@SALIDA", str_rutaOrigen);
		str_mensaje=str_mensaje.replaceAll("@DESTINO", str_rutaDestino);
		str_mensaje=str_mensaje.replaceAll("@MOTIVO", str_motivo);
		
		String str_funcionario = "";
		String str_mail="";

		try {

			System.out.println("Ingresa a correo solicitante");
			str_funcionario = tab_empleado.getValor("nombres");
			str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
			str_mail = tab_empleado.getValor("detalle_gtcor");
			System.out.println("enviarCorreo agendamiento sol: "+str_mail);
			//str_mail = "alex.becerra@emgirs.gob.ec";	

			envMail.setAsunto(str_asunto);
			envMail.setCuerpoHtml(str_mensaje);
			envMail.setPara(str_mail);
			if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);		
			
			if (pckUtilidades.CConversion.CInt(tab_asolicitud.getValor("gen_ide_gtemp")) != pckUtilidades.CConversion.CInt(tab_asolicitud.getValor("ide_gtemp"))) {
				System.out.println("Ingresa a correo solicitante tab_emp_solicita");
				TablaGenerica tab_emp_solicita = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_asolicitud.getValor("gen_ide_gtemp") + ""));
				str_mail = tab_emp_solicita.getValor("detalle_gtcor");
				System.out.println("enviarCorreo agendamiento sol: "+str_mail);
				//str_mail = "alex.becerra@emgirs.gob.ec";	
				str_funcionario = tab_emp_solicita.getValor("nombres");
				str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
				//util.EnviaMailInterno(envMail, str_mail, str_asunto, str_mensaje1, null);
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
			System.out.println("Error al enviar el correo al solicitante: " + e.getMessage());
			utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo del solicitante: "+str_mail);
		}

		// correo del responsable de transporte
		try {
			System.out.println("Ingresa a correo responsable transporte");
			str_asunto = "NUEVA SOLICITUD DE AGENDAMIENTO VEHICULAR";
			str_mensaje = tab_correo_plantilla.getValor(1, "plantilla_cpla");
			str_mensaje = str_mensaje.replaceAll("@FECHA", str_fecha);
			str_mensaje = str_mensaje.replaceAll("@HORA", str_hora);
			str_mensaje = str_mensaje.replaceAll("@ESTADO", str_estado);
			str_mensaje = str_mensaje.replaceAll("@APROBADO", "SI");
			str_mensaje = str_mensaje.replaceAll("@JEFEAPROBADOR", str_jefeAprobador);
			str_mensaje = str_mensaje.replaceAll("@OBSERVACION", str_obs);
			str_mensaje = str_mensaje.replaceAll("@PLACA", "");
			str_mensaje = str_mensaje.replaceAll("@CONDUCTOR", "");
			str_mensaje = str_mensaje.replaceAll("@SALIDA", str_rutaOrigen);
			str_mensaje = str_mensaje.replaceAll("@DESTINO", str_rutaDestino);
			str_mensaje=str_mensaje.replaceAll("@MOTIVO", str_motivo);
			str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);

			str_mail = this.p_responsable_transporte; 
			System.out.println("enviarCorreo agendamiento transp: "+str_mail);
			//str_mail = "alexbec0000@gmail.com";	
			
			System.out.println("Correo enviado a: "+str_mail);
			envMail.setAsunto(str_asunto);
			envMail.setCuerpoHtml(str_mensaje);
			envMail.setPara(str_mail);
			if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);
		} catch (Exception ex) {
			System.err.println("Correo no Enviado responsable transporte " + ex.getMessage());
			utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo del responsable del transporte: "+str_mail);
		}
		

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

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		//actualizarTabla2();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		//actualizarTabla2();
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		//actualizarTabla2();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		//actualizarTabla2();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		//actualizarTabla2();
		
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		//actualizarTabla2();
	}

	public Tabla getTab_asolicitud() {
		return tab_asolicitud;
	}

	public void setTab_asolicitud(Tabla tab_asolicitud) {
		this.tab_asolicitud = tab_asolicitud;
	}

	public Tabla getTab_asolicitud_ruta() {
		return tab_asolicitud_ruta;
	}

	public void setTab_asolicitud_ruta(Tabla tab_asolicitud_ruta) {
		this.tab_asolicitud_ruta = tab_asolicitud_ruta;
	}

	public Tabla getTab_asolicitud_ocupante() {
		return tab_asolicitud_ocupante;
	}

	public void setTab_asolicitud_ocupante(Tabla tab_asolicitud_ocupante) {
		this.tab_asolicitud_ocupante = tab_asolicitud_ocupante;
	}

	public Tabla getTab_asolicitud_archivo() {
		return tab_asolicitud_archivo;
	}

	public void setTab_asolicitud_archivo(Tabla tab_asolicitud_archivo) {
		this.tab_asolicitud_archivo = tab_asolicitud_archivo;
	}



			
}
