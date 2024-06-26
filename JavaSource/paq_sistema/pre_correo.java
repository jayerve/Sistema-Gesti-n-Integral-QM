/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import java.io.File;

import javax.ejb.EJB;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import framework.correo.EnviarCorreo;

import org.primefaces.component.editor.Editor;
import org.primefaces.component.overlaypanel.OverlayPanel;

import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;

/**
 * 
 * @author DELL-USER
 */
public class pre_correo extends Pantalla {

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_plantilla = new Tabla();

	private Boton bot_enviar = new Boton();
	private Editor edi_msj = new Editor();

	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public pre_correo() {

		bot_enviar.setIcon("ui-icon-mail-closed");
		bot_enviar.setValue("Enviar A Todos");
		bot_enviar.setMetodo("enviar");
		bar_botones.agregarBoton(bot_enviar);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("sis_correo", "ide_corr", 1);
		tab_tabla.setTipoFormulario(true);
		tab_tabla.getGrid().setColumns(4);
		tab_tabla.getColumna("activo_corr").setValorDefecto("true");
		tab_tabla.agregarRelacion(tab_tabla_plantilla);
		tab_tabla.setRows(15);
		tab_tabla.dibujar();

		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("CORREOS");
		pat_panel1.setPanelTabla(tab_tabla);

		tab_tabla_plantilla.setId("tab_tabla_plantilla");
		tab_tabla_plantilla.setTabla("sis_correo_plantilla", "ide_cpla", 2);
		tab_tabla_plantilla.setTipoFormulario(true);
		tab_tabla_plantilla.getGrid().setColumns(4);
		tab_tabla_plantilla.getColumna("activo_cpla").setValorDefecto("true");
		tab_tabla_plantilla.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla_plantilla);
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);
	}

	public void enviar() {
		/*
		 * TablaGenerica tab_correo_envio = utilitario.consultar(
		 * "select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co "
		 * + "left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "+
		 * "where activo_cpla = true and activo_corr = true and co.ide_corr=");
		 * 
		 * EnvioMail envMail = new
		 * EnvioMail(tab_correo_envio.getValor("smtp_corr"),
		 * tab_correo_envio.getValor("puerto_corr"),
		 * tab_correo_envio.getValor("correo_corr"),
		 * tab_correo_envio.getValor("usuario_corr"),
		 * tab_correo_envio.getValor("clave_corr"));
		 */

		EnvioMail envMail = new EnvioMail(tab_tabla.getValor("smtp_corr"), tab_tabla.getValor("puerto_corr"), tab_tabla.getValor("correo_corr"), tab_tabla.getValor("usuario_corr"), tab_tabla.getValor("clave_corr"));

		StringBuilder str_resultado = new StringBuilder();

		//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();

		TablaGenerica tab_empleados = utilitario
				.consultar("SELECT emp.ide_gtemp, coalesce(primer_nombre_gtemp, segundo_nombre_gtemp) as nombre, coalesce(apellido_paterno_gtemp, apellido_materno_gtemp) as apellido,EMP.APELLIDO_PATERNO_GTEMP ||' '|| "
						+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| EMP.PRIMER_NOMBRE_GTEMP ||' '|| (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS nombres, detalle_gtcor "
						+ "FROM gth_empleado emp " + "left join gth_correo cor on cor.ide_gtemp=emp.ide_gtemp and cor.activo_gtcor=true and cor.notificacion_gtcor=true " + "where activo_gtemp=true ");

		for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
			String str_mail = tab_empleados.getValor(i, "detalle_gtcor"); // "alex.becerra@emgirs.gob.ec";
																			// //
			String str_asunto = "CREDENCIAL DE ACCESO AL PORTAL DEL SISTEMA ERP";
			String str_funcionario = tab_empleados.getValor(i, "nombres");
			String str_usuario = "";

			try {
				str_usuario = ser_seguridad.validarUsuario(tab_empleados.getValor(i, "ide_gtemp"), pckUtilidades.CConversion.CStr(tab_empleados.getValor(i, "nombre").replaceAll("\\s", "")),
						pckUtilidades.CConversion.CStr(tab_empleados.getValor(i, "apellido").replaceAll("\\s", "")), str_funcionario, str_mail);

				String str_mensaje = tab_tabla_plantilla.getValor("plantilla_cpla");
				str_mensaje = str_mensaje.replaceAll("@FECHA", utilitario.getFechaLarga(utilitario.getFechaActual()));
				str_mensaje = str_mensaje.replaceAll("@HORA", utilitario.getHoraActual());
				str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
				str_mensaje = str_mensaje.replaceAll("@USUARIO", str_usuario);

				enviaMailInterno(envMail, str_mail, str_asunto, str_mensaje, null);
			} catch (Exception ex) {
				str_resultado.append(getFormatoError("Correo no enviado a : " + str_funcionario + " email: " + str_mail + " msjError: " + ex.getMessage()));
			}
		}
		edi_msj.setValue(str_resultado);

	}

	private void enviaMailInterno(EnvioMail envMail, String str_mail, String str_asunto, String str_mensaje, File fil_rol)
	{
		envMail.setAsunto(str_asunto);
		envMail.setCuerpoHtml(str_mensaje);
		envMail.setPara(str_mail);
		if(fil_rol!=null)
		{
			envMail.setNombreAdjunto("adjunto.pdf");
			envMail.setAdjuntoArray64(pckUtilidades.Utilitario.fileConvertToArray64(fil_rol));
		}
		pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
		
		if(obj.getRespuesta())
		{
			utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
		}
		else
			utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
	}

	private String getFormatoInformacion(String mensaje) {
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}

	private String getFormatoError(String mensaje) {
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		if (tab_tabla.guardar()) {
			if (tab_tabla_plantilla.guardar()) {
				guardarPantalla();
			}
		}
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_tabla_plantilla() {
		return tab_tabla_plantilla;
	}

	public void setTab_tabla_plantilla(Tabla tab_tabla_plantilla) {
		this.tab_tabla_plantilla = tab_tabla_plantilla;
	}

}
