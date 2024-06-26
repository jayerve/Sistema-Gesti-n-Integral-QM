package paq_sistema.aplicacion;

import java.util.*;

import javax.mail.*;
import javax.activation.*;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import framework.aplicacion.Framework;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import framework.aplicacion.TablaGenerica;
import framework.componentes.BuscarTabla;
import framework.componentes.FormatoTabla;
import framework.componentes.Grupo;
import framework.componentes.ImportarTabla;
import framework.componentes.Notificacion;
import framework.componentes.TerminalTabla;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataSource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

import javax.servlet.ServletOutputStream;


/**
 * 
 * @author DELL-USER
 */
public class Utilitario extends Framework {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isNumeroPositivo(String numero_validar) {
		try {
			double dou_num_val = Double.parseDouble(numero_validar);
			if (dou_num_val > 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public Date DeStringADate(String fecha) {
		// System.out.println("entre a convertir a "+fecha);

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			// System.out.println("entre a convertir strign a fecha "+fechaDate.toString());
			return fechaDate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}
	
	public String DateStringAString(String fecha, String formatoF) {
		// System.out.println("entre a convertir a "+fecha);
		formatoF="dd/MM/yyyy";
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = "";
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
			formato = new SimpleDateFormat(formatoF);
			strFecha=formato.format(fechaDate);
			return strFecha;
		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("DateStringAString "+strFecha+" "+fecha);
			return strFecha;
		}
	}

	public String DeDateAString(Date fecha) {
		String convertido = "1900-01-01";
		DateFormat format_fecha = new SimpleDateFormat("yyyy-MM-dd");
		convertido = format_fecha.format(fecha);
		// System.out.println(convertido);
		return convertido;
	}

	public Date DeStringAHora(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			System.out.println("entre a convertir strign a fecha " + fechaDate.toString());
			return fechaDate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}
	
	public String DeDateAStringHora(Date fecha) {
			
		SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");		
		String strFecha = "hh:mm:ss";

		try {
			strFecha = formato.format(fecha);
			return strFecha;
		} catch (Exception ex) {
			System.out.println("Error: DeDateAStringHora " + ex.getMessage());
			return strFecha;
		}
	}
	
	public String DateStringFormat(String fecha) {
		
		String fechaFormateada="11 DE JULIO DE 2017";
		
		try 
		{
			String[] dataTemp = fecha.split("-");
			
			fechaFormateada = pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[2])+"", 2)
					+ " DE " + getNombreMes(pckUtilidades.CConversion.CInt(dataTemp[1])) 
					+ " DE " + pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[0])+"", 4);
		} 
		catch (Exception ex) {}
		
		return fechaFormateada;
	}

	/*
	 * Valida el ingreso de solo numeros enteros positivos (telefono)
	 */
	public boolean isEnteroPositivo(String numero_validar) {
		try {
			int int_num_val = pckUtilidades.CConversion.CInt(numero_validar);
			double dou_num_val = Double.parseDouble(numero_validar);
			if (int_num_val == dou_num_val) {
				if (int_num_val > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isEnteroPositivoyCero(String numero_validar) {
		try {
			int int_num_val = pckUtilidades.CConversion.CInt(numero_validar);
			double dou_num_val = Double.parseDouble(numero_validar);
			if (int_num_val == dou_num_val) {
				if (int_num_val >= 0) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Agrega un mensaje de notificación que bloquea el menu y es necesario que
	 * el usuario la cierre
	 * 
	 * @param titulo
	 * @param mensaje
	 */
	public void agregarNotificacionInfo(String titulo, String mensaje) {
		Notificacion not_notificacion = (Notificacion) FacesContext
				.getCurrentInstance().getViewRoot()
				.findComponent("formulario:not_notificacion");
		if (not_notificacion != null) {
			not_notificacion.setNotificacion(titulo, mensaje, "");
			RequestContext requestContext = RequestContext.getCurrentInstance();
			addUpdate("not_notificacion");
			requestContext.execute("not_notificacion.show();");
		}
	}

	/**
	 * Agrega un mensaje de notificación que bloquea el menu y es necesario que
	 * el usuario la cierre
	 * 
	 * @param titulo
	 * @param mensaje
	 * @param pathImagen
	 *            ruta de imagen
	 */
	public void agregarNotificacion(String titulo, String mensaje,
			String pathImagen) {
		Notificacion not_notificacion = (Notificacion) FacesContext
				.getCurrentInstance().getViewRoot()
				.findComponent("formulario:not_notificacion");
		if (not_notificacion != null) {
			not_notificacion.setNotificacion(titulo, mensaje, pathImagen);
			addUpdate("not_notificacion");
			ejecutarJavaScript("not_notificacion.show();");
		}
	}

	/**
	 * Retorna el Panel asignado para mensajes
	 * 
	 * @return
	 */
	public Grupo getMensajes() {
		return (Grupo) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formulario:mensajes");
	}

	/**
	 * Retorna una lista con 2 registros uno PADRE y otro HIJO, es para tablas
	 * recursivas que necesitan un nivel
	 * 
	 * @return
	 */
	public List getListaNiveles() {
		// pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
		List lista = new ArrayList();
		Object fila1[] = { "HIJO", "HIJO" };
		Object fila2[] = { "PADRE", "PADRE" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}

	/**
	 * Verifica que un número de ruc sea válido
	 * 
	 * @param str_ruc
	 * @return
	 */
	public boolean validarRUC(String str_ruc) {
		boolean boo_correcto = false;
		try {
			if (str_ruc.length() == 13) {
				int[] int_digitos = new int[10];
				int int_coeficiente = 10;
				String str_valida = str_ruc.substring(10, 13);

				if (str_valida.equals("001")) {
					for (int i = 0; i < int_digitos.length; i++) {
						int_digitos[i] = pckUtilidades.CConversion.CInt(str_ruc.charAt(i)
								+ "");
					}
					int int_digito_verifica = int_digitos[9];
					int[] int_multiplica = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					if (int_digitos[2] == 9) {
						int[] int_multiplica9 = { 4, 3, 2, 7, 6, 5, 4, 3, 2 };
						int_multiplica = int_multiplica9;
						int_coeficiente = 11;
					}
					if (int_digitos[2] == 6) {
						int[] aint_multiplica6 = { 3, 2, 7, 6, 5, 4, 3, 2 };
						int_digito_verifica = int_digitos[8];
						int_multiplica = aint_multiplica6;
						int_coeficiente = 11;
					}
					int int_suma = 0;
					for (int i = 0; i < (int_digitos.length - 1); i++) {
						try {
							if (int_coeficiente == 10) {
								int mul = int_digitos[i] * int_multiplica[i];
								if (mul > 9) {
									String aux = mul + "";
									mul = pckUtilidades.CConversion.CInt(aux.charAt(0) + "")
											+ pckUtilidades.CConversion.CInt(aux.charAt(1)
													+ "");
								}
								int_suma += mul;
							} else {
								int_suma += (int_digitos[i] * int_multiplica[i]);
							}
						} catch (Exception ex) {
						}
					}
					int int_valida = 0;
					if (int_coeficiente == 10) {
						if (int_suma % 10 == 0) {
							int_valida = 0;
						} else {
							int_valida = 10 - (int_suma % 10);
						}
					} else {
						if (int_suma % 11 == 0) {
							int_valida = 0;
						} else {
							int_valida = 11 - (int_suma % 11);
						}
					}

					if (int_valida == 0) {
						int_digito_verifica = 0;
					}
					if (int_valida == int_digito_verifica) {
						boo_correcto = true;
					}
				}
			}
		} catch (Exception e) {
		}
		return boo_correcto;
	}

	/**
	 * Verifica que un número de cédula sea válido
	 * 
	 * @param str_cedula
	 * @return
	 */
	public boolean validarCedula(String str_cedula) {
		boolean boo_correcto = false;
		try {
			if (str_cedula.length() == 10) {

				if (!str_cedula.equals("2222222222")) {
					int lint_suma = 0;

					for (int i = 0; i < 9; i++) {
						int lstr_digito = pckUtilidades.CConversion.CInt(str_cedula.charAt(i)
								+ "");
						if (i % 2 == 0) {
							lstr_digito = lstr_digito * 2;
							if (lstr_digito > 9) {
								String lstr_aux = lstr_digito + "";
								lstr_digito = pckUtilidades.CConversion.CInt(lstr_aux
										.charAt(0) + "")
										+ pckUtilidades.CConversion.CInt(lstr_aux.charAt(1)
												+ "");
							}
						}
						lint_suma += lstr_digito;
					}
					if (str_cedula.charAt(9) != '0') {
						String lstr_aux = lint_suma + "";
						int lint_superior = (pckUtilidades.CConversion.CInt(lstr_aux
								.charAt(0) + "") + 1) * 10;
						int lint_ultimo_real = lint_superior - lint_suma;
						int lint_ultimo_digito = pckUtilidades.CConversion.CInt(str_cedula
								.charAt(9) + "");
						if (lint_ultimo_digito == lint_ultimo_real) {
							boo_correcto = true;
						}
					} else {
						// Para cedulas que terminan en 0
						if (lint_suma % 10 == 0) {
							boo_correcto = true;
						}
					}

				} else {
					boo_correcto = false;
				}

			}
		} catch (Exception ex) {
		}
		return boo_correcto;
	}

	/**
	 * Calcula la diferencia en número de días entre dos fechas
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public int getDiferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
		SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
		String fechaInicioString = formatoFecha.format(fechaInicial);
		try {
			fechaInicial = formatoFecha.parse(fechaInicioString);
		} catch (ParseException ex) {
		}

		String fechaFinalString = formatoFecha.format(fechaFinal);
		try {
			fechaFinal = formatoFecha.parse(fechaFinalString);
		} catch (ParseException ex) {
		}
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		//System.out.println("getDiferenciasDeFechas "+dias);
		return ((int) dias);
	}
	
	/**
	 * Retorna la diferencia en dias entre dos fechas año calendario todos los meses 30 dias
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public double getDiferenciaFechas360(Date fechaInicial, Date fechaFinal) {//awbecerra
		SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
		String fechaInicioString = formatoFecha.format(fechaInicial);
		
		int mes_inicial=getMes(fechaInicioString);
		int anio_inicial=getAnio(fechaInicioString);
		
		Date fechaFinMesInicio=getFecha(anio_inicial+"-"+mes_inicial+"-30");
		
		int meses=getDiferenciaMeses(fechaInicial,fechaFinal);
		int dias=getDiferenciasDeFechas(fechaInicial,fechaFinMesInicio)+1;
		int totalDias360=0;
		
		totalDias360=meses*30 + dias;

		return totalDias360;
	}
	
	public int getDiferenciaMeses(Date fechaInicio, Date fechaFin) {
        try {
            //Fecha inicio en objeto Calendar
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            //Fecha finalización en objeto Calendar
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            //Cálculo de meses para las fechas de inicio y finalización
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            //Diferencia en meses entre las dos fechas
            int diffMonth = endMes - startMes;
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
	}


	/**
	 * Calcula el número de día de la semana de una fecha, considera que el día
	 * lunes es el día 1
	 * 
	 * @param fecha
	 * @return
	 */
	public int getNumeroDiasSemana(Date fecha) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fecha.getTime());
		// Considera que lunes es el dia 1
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * Calcula el número de día de la semana de una fecha, considera que el día
	 * lunes es el día 1
	 * 
	 * @param fecha
	 * @return
	 */
	public String getUltimoDiaMesFecha(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		Date fch=DeStringADate(fecha);
		int diaMax=0;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
	    diaMax=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String hoy = formato.format(fch);
		String[] dataTemp = hoy.split("-");
		
		String fecha2=pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[0])+"", 4)  + "-" 
		+ pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[1])+"", 2)  + "-" + pckUtilidades.Utilitario.padLeft(diaMax+"", 2);
	
		return fecha2;
	}

	/**
	 * Suma dias a una fecha y retorna la nueva fecha
	 * 
	 * @param fch
	 * @param dias
	 * @return
	 */
	public Date sumarDiasFecha(Date fch, int numeroDiasSumar) {
		// CODIGO ANTERIOR
		// Calendar cal = new GregorianCalendar();
		// cal.setTimeInMillis(fch.getTime());
		// cal.add(Calendar.DATE, dias);

		// INICIO, Modificado por: Alex Becerra, fecha: 30/12/2015, motivo:
		// Control de sumas y restas de dias
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String hoy = formato.format(fch);
		String[] dataTemp = hoy.split("-");

		int diaActual, mesActual, añoActual, numeroDeMes;
		diaActual = pckUtilidades.CConversion.CInt(dataTemp[2]);
		mesActual = pckUtilidades.CConversion.CInt(dataTemp[1]);
		añoActual = pckUtilidades.CConversion.CInt(dataTemp[0]);
		int numDias = diasPorMes(mesActual, añoActual);

		if ((diaActual + numeroDiasSumar) > numDias) {
			diaActual = numDias - (diaActual + numeroDiasSumar);
			diaActual = diaActual < 0 ? diaActual * -1 : diaActual;

			numeroDeMes = mesActual;
			if ((numeroDeMes + 1) >= 13) {
				mesActual = numeroDeMes - mesActual + 1;
				añoActual = añoActual + 1;

			} else if ((numeroDeMes + 1) < 13) {
				mesActual = mesActual + 1;
			}
		} else
			diaActual += numeroDiasSumar;
	
		return DeStringADate(añoActual + "-" + mesActual + "-" + diaActual);
		// FIN, Modificado por: Alex Becerra, fecha: 30/12/2015, motivo: Control
		// de sumas y restas de dias
	}

	
	public Date sumarDiasFechaSinFinSemana(Date fch, int numeroDiasSumar) {
		
		Calendar fechaInicial = Calendar.getInstance();
		Calendar fechaInicialCalculo = Calendar.getInstance();

		fechaInicial.setTime(fch);
		int contador  = 1;
		while (contador <= numeroDiasSumar)
		{
			
		//	if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
			 if (fechaInicial.get(Calendar.DAY_OF_WEEK) != 7 && fechaInicial.get(Calendar.DAY_OF_WEEK) != 6) {

				contador++;
				
			}
			fechaInicial.add(Calendar.DATE, 1);
		
		}
		return fechaInicial.getTime();

	}
	
	public int numeroFinSemanaEntreFechas(Date fch, Date fchf) {
		
		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.setTime(fch);
		Calendar fechaFinal = Calendar.getInstance();
		fechaFinal.setTime(fchf);
		int contador  = 0;
		
		while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {
			
			
			if (fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ||  fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				contador++;		
			}
			
			fechaInicial.add(Calendar.DATE, 1);
		
		}
			
		return contador;

	}

	
	public Date siguienteFinSemana(Date fch) {
		
		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.setTime(fch);
		
		while (!(fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ||  fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
			fechaInicial.add(Calendar.DATE, 1);		
		}
		
		if(fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			fechaInicial.add(Calendar.DATE, -1);	
		
		return fechaInicial.getTime();

	}
	
	public boolean isDiaLaboral(Date fch)
	{
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(fch);
		
		/*System.out.println("fecha Calendar.DAY_OF_WEEK: "+fecha.get(Calendar.DAY_OF_WEEK));
		System.out.println("Calendar SUNDAY: "+Calendar.SUNDAY);
		System.out.println("Calendar SATURDAY: "+Calendar.SATURDAY);*/
		
		if (fecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ||  fecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return false;	
		}
		
		return true;
	}
	
	public boolean isHoraLaboral(Date hora, boolean solicitud)
	{
		boolean validar=false;
		int horaInicio=0,horaFin=0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hora);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutos = calendar.get(Calendar.MINUTE);
		//System.out.println("horaSeleccionada: "+hours);
		horaInicio=pckUtilidades.CConversion.CInt(getVariable("p_hora_inicio_laboral"));
		horaFin=pckUtilidades.CConversion.CInt(getVariable("p_hora_fin_laboral"));

		if (hours<horaInicio || hours>horaFin) 
			validar=true;
		
		if(hours==horaFin && minutos>0)
			validar=true;
		
		if(solicitud)
		{
			validar=false;
			horaFin=horaFin-1;
			
			if (hours<horaInicio || hours>horaFin) 
			validar=true;
		
			if(hours==horaFin && minutos>30)
				validar=true;
		}
		
		return validar;
	}
	
	private int diasPorMes(int diaMes, int anio) {
		int numDias = 0;
		if (diaMes == 4 || diaMes == 6 || diaMes == 9 || diaMes == 11) {
			numDias = 30;
		} else if (diaMes == 2) {

			// para cuando el año es bisiesto
			if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {// si
																				// el
																				// aï¿½o
																				// es
																				// bisiesto
																				// es
																				// divisible
																				// entre
																				// 4
																				// y
																				// 100
																				// o
																				// 400
				numDias = 29;
			}

			else {
				numDias = 28;// queda pendiente para año bisiesto
			}
		} else
			// todos los demas meses
			numDias = 31;

		return numDias;
	}

	/**
	 * Válida que un correo electrónico sea válido
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmailValido(String email) {
		/*Pattern pat = Pattern
				.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");*/
		Pattern pat = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		Matcher mat = pat.matcher(email);
		if (mat.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Asigna un formato con una cantidad de decimales a una cantidad numérica
	 * 
	 * @param numero
	 * @param numero_decimales
	 * @return
	 */
	public String getFormatoNumero(Object numero, int numero_decimales) {
		String lstr_formato = "#";
		for (int i = 0; i < numero_decimales; i++) {
			if (i == 0) {
				lstr_formato += ".";
			}
			lstr_formato += "#";
		}
		DecimalFormat formatoNumero;
		DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
		idfs_simbolos.setDecimalSeparator('.');
		formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);
		try {
			double ldob_valor = Double.parseDouble(numero.toString());
			return formatoNumero.format(ldob_valor);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public String getFormatoNumeroDown(Object numero, int numero_decimales)
	{
		BigDecimal number = BigDecimal.valueOf(Double.parseDouble(numero.toString()));
		return number.setScale(numero_decimales, RoundingMode.DOWN).toString();
	}
	/**
	 * Retorna un objeto de tipo Date con la fecha actual
	 * 
	 * @return
	 */
	public Date getDate() {	
		return new Date();
	}

	/**
	 * Retorna el componente Buscar del menú contextual (click derecho)
	 * 
	 * @return
	 */
	public BuscarTabla getBuscaTabla() {
		return (BuscarTabla) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formulario:bus_buscar");
	}

	/**
	 * Retorna el componente ImportarTabla del menú contextual (click derecho)
	 * 
	 * @return
	 */
	public ImportarTabla getImportarTabla() {
		return (ImportarTabla) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formulario:imt_importar");
	}

	/**
	 * Retorna el componente FormatoTabla del menú contextual (click derecho)
	 * 
	 * @return
	 */
	public FormatoTabla getFormatoTabla() {
		return (FormatoTabla) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formulario:fot_formato");
	}

	/**
	 * Retorna el componente Terminal del menú contextual (click derecho)
	 * 
	 * @return
	 */
	public TerminalTabla getTerminal() {
		return (TerminalTabla) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formulario:term_tabla");
	}

	/**
	 * Evalua una expresión aritmética, y retorna el resultado, ejemplo
	 * 2*2/(4-2) retorna 2
	 * 
	 * @param expresion
	 * @return
	 */
	public double evaluarExpresion(String expresion) {
		// Resuleve el valor de una expresion Ejemplo: 5+3-3
		double resultado = 0;
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object operacion;
		try {
			expresion = expresion.replace("[", "(");
			expresion = expresion.replace("]", ")");
			operacion = engine.eval(expresion);
			resultado = Double.parseDouble(operacion.toString());
		} catch (ScriptException e) {
			System.out.println("ERROR al evaluarExpresion( " + expresion
					+ " )  :" + e.toString());
		}
		return resultado;
	}

	/**
	 * Limpia todos los componentes a su estado original
	 */
	public void resetarPantalla() {
		RequestContext.getCurrentInstance().reset("formulario:dibuja");
	}

	/**
	 * Retorna en letras una cantidad numérica
	 * 
	 * @param numero
	 * @return
	 */
	public String getLetrasNumero(Object numero) {
		String letras = getFormatoNumero(numero);
		if (letras != null) {
			try {
				letras = recursivoNumeroLetras(pckUtilidades.CConversion.CInt(letras
						.substring(0, letras.lastIndexOf("."))))
						+ " CON"
						+ recursivoNumeroLetras(pckUtilidades.CConversion.CInt(letras
								.substring((letras.lastIndexOf(".") + 1),
										letras.length())));
				letras = letras.toUpperCase();
				letras = letras.trim();
			} catch (Exception e) {
			}
		}
		return letras;
	}

	/**
	 * Retorna en letras con dolares y centavos una cantidad numérica
	 * 
	 * @param numero
	 * @return
	 */
	public String getLetrasDolarNumero(Object numero) {
		String letras = getFormatoNumero(numero);
		if (letras != null) {
			try {
				String centavos = (pckUtilidades.CConversion.CInt(letras.substring(
						(letras.lastIndexOf(".") + 1), letras.length())))
						+ "";
				if (centavos.trim().length() == 1) {
					centavos = "0" + centavos;
				}
				letras = recursivoNumeroLetras(pckUtilidades.CConversion.CInt(letras
						.substring(0, letras.lastIndexOf("."))))
						+ " CON "
						+ centavos + "/100 ";
				letras = letras.toUpperCase();
				letras = letras.trim();
			} catch (Exception e) {
			}
		}
		return letras;
	}

	/**
	 * Metodo recursivo que calcula en letras una cantidad numerica
	 * 
	 * @param numero
	 * @return
	 */
	private String recursivoNumeroLetras(int numero) {
		String cadena = new String();
		// Aqui identifico si lleva millones
		if ((numero / 1000000) > 0) {
			if ((numero / 1000000) == 1) {
				cadena = " Un Millon" + recursivoNumeroLetras(numero % 1000000);
			} else {
				cadena = recursivoNumeroLetras(numero / 1000000) + " Millones"
						+ recursivoNumeroLetras(numero % 1000000);
			}
		} else {
			// Aqui identifico si lleva Miles
			if ((numero / 1000) > 0) {

				if ((numero / 1000) == 1) {
					cadena = " Mil" + recursivoNumeroLetras(numero % 1000);
				} else {
					cadena = recursivoNumeroLetras(numero / 1000) + " Mil"
							+ recursivoNumeroLetras(numero % 1000);
				}
			} else {
				// Aqui identifico si lleva cientos
				if ((numero / 100) > 0) {
					if ((numero / 100) == 1) {
						if ((numero % 100) == 0) {
							cadena = " Cien";
						} else {
							cadena = " Ciento"
									+ recursivoNumeroLetras(numero % 100);
						}
					} else {
						if ((numero / 100) == 5) {
							cadena = " Quinientos"
									+ recursivoNumeroLetras(numero % 100);
						} else {
							if ((numero / 100) == 9) {
								cadena = " Novecientos"
										+ recursivoNumeroLetras(numero % 100);
							} else {
								cadena = recursivoNumeroLetras(numero / 100)
										+ "cientos"
										+ recursivoNumeroLetras(numero % 100);
							}
						}
					}
				} // Aqui se identifican las Decenas
				else {
					if ((numero / 10) > 0) {
						switch (numero / 10) {
						case 1:
							switch (numero % 10) {
							case 0:
								cadena = " Diez";
								break;
							case 1:
								cadena = " Once";
								break;
							case 2:
								cadena = " Doce";
								break;
							case 3:
								cadena = " Trece";
								break;
							case 4:
								cadena = " Catorce";
								break;
							case 5:
								cadena = " Quince";
								break;
							default:
								cadena = " Diez y "
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 2:
							switch (numero % 10) {
							case 0:
								cadena = " Veinte";
								break;
							default:
								cadena = " Veinti"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 3:
							switch (numero % 10) {
							case 0:
								cadena = " Treinta";
								break;
							default:
								cadena = " Treinta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 4:
							switch (numero % 10) {
							case 0:
								cadena = " Cuarenta";
								break;
							default:
								cadena = " Cuarenta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 5:
							switch (numero % 10) {
							case 0:
								cadena = " Cincuenta";
								break;
							default:
								cadena = " Cincuenta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 6:
							switch (numero % 10) {
							case 0:
								cadena = " Sesenta";
								break;
							default:
								cadena = " Sesenta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 7:
							switch (numero % 10) {
							case 0:
								cadena = " Setenta";
								break;
							default:
								cadena = " Setenta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 8:
							switch (numero % 10) {
							case 0:
								cadena = " Ochenta";
								break;
							default:
								cadena = " Ochenta y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						case 9:
							switch (numero % 10) {
							case 0:
								cadena = " Noventa";
								break;
							default:
								cadena = " Noventa y"
										+ recursivoNumeroLetras(numero % 10);
								break;
							}
							break;
						}
					} else {
						switch (numero) {
						case 1:
							cadena = " Uno";
							break;
						case 2:
							cadena = " Dos";
							break;
						case 3:
							cadena = " Tres";
							break;
						case 4:
							cadena = " Cuatro";
							break;
						case 5:
							cadena = " Cinco";
							break;
						case 6:
							cadena = " Seis";
							break;
						case 7:
							cadena = " Siete";
							break;
						case 8:
							cadena = " Ocho";
							break;
						case 9:
							cadena = " Nueve";
							break;
						case 0:
							// cadena = " Cero";
							break;
						}
					}
				}
			}
		}
		return cadena;
	}

	/**
	 * Crea un archivo para que pueda ser descargado en el navegador, debe
	 * previamente existir el path de archivo
	 * 
	 * @param path
	 */
	public void crearArchivo(String path) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		StreamedContent content;
		InputStream stream = null;
		try {
			if (path.startsWith("/")) {
				stream = ((ServletContext) FacesContext.getCurrentInstance()
						.getExternalContext().getContext())
						.getResourceAsStream(path);
			} else {
				stream = new FileInputStream(path);
			}
		} catch (Exception e) {
			crearError("No se puede generar el archivo path: " + path,
					"crearArchivo()", e);
		}
		if (stream == null) {
			return;
		}
		content = new DefaultStreamedContent(stream);
		if (content == null) {
			return;
		}
		ExternalContext externalContext = facesContext.getExternalContext();
		String contentDispositionValue = "attachment";
		try {
			externalContext.setResponseContentType(content.getContentType());
			externalContext.setResponseHeader(
					"Content-Disposition",
					contentDispositionValue + ";filename=\""
							+ path.substring(path.lastIndexOf("/") + 1) + "\"");
			externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE,
					"true", new HashMap<String, Object>());
			byte[] buffer = new byte[2048];
			int length;
			InputStream inputStream = content.getStream();
			OutputStream outputStream = externalContext
					.getResponseOutputStream();
			while ((length = (inputStream.read(buffer))) != -1) {
				outputStream.write(buffer, 0, length);
			}
			externalContext.setResponseStatus(200);
			externalContext.responseFlushBuffer();
			content.getStream().close();
			facesContext.getApplication().getStateManager()
					.saveView(facesContext);
			facesContext.responseComplete();
		} catch (Exception e) {
			crearError("No se puede descargar :  path: " + path,
					"crearArchivo()", e);
		}
	}

	/**
	 * Convierte una expresion separada por comas en una expresion aumentada
	 * comilla simple, ejemplo 1,2,3 retorna '1','2','3'
	 * 
	 * @param cadena
	 * @return
	 */
	public String generarComillaSimple(String cadena) {
		String str_cadena = "";
		String[] vec = cadena.split(",");
		for (int i = 0; i < vec.length; i++) {
			if (!str_cadena.isEmpty()) {
				str_cadena += ",";
			}
			str_cadena += "'" + vec[i] + "'";
		}
		return str_cadena;
	}

	/**
	 * Busca un campo de la base de datos de la tabla sis_empresa
	 * 
	 * @param campo
	 * @return
	 */
	public String getCampoEmpresa(String campo) {
		String valor = null;
		TablaGenerica tab_empresa = consultar("SELECT IDE_EMPR," + campo
				+ " FROM SIS_EMPRESA WHERE IDE_EMPR=" + getVariable("IDE_EMPR"));
		if (tab_empresa.getTotalFilas() > 0) {
			valor = tab_empresa.getValor(0, campo);
		}
		return valor;
	}

	/**
	 * Permite instanciar a un EJB desde una clase
	 * 
	 * @param ejb
	 * @return
	 */
	public Object instanciarEJB(Class<?> ejb) {
		// Para cuando se necesite instanciar el EJB
		try {
			Context c = new InitialContext();
			return c.lookup("java:global/" + getNombreProyecto() + "/"
					+ ejb.getSimpleName());
		} catch (Exception e) {
			System.out.println("FALLO AL INSTANCIAR EL EJB "
					+ ejb.getSimpleName() + " :" + e.getMessage());
		}
		return null;
	}

	/**
	 * Retorna el nombre del proyecto de la aplicación web
	 * 
	 * @return
	 */
	public String getNombreProyecto() {
		ExternalContext iecx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
		String contexto = request.getContextPath() + "";
		contexto = contexto.replace("/", "");
		contexto = contexto.trim();
		return contexto;
	}

	/**
	 * Retorna el id de sessión asignado al usuario logeado
	 * 
	 * @return
	 */
	public String getIdSession() {
		String str_id = null;
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext()
					.getSession(false);
			str_id = session.getId();
		} catch (Exception e) {
		}

		return str_id;
	}

	/**
	 * Retorna la edad a partir de una fecha
	 * 
	 * @param fecha
	 * @return
	 */
	public int getEdad(String fecha) {
		Calendar fechaNacimiento = Calendar.getInstance();
		Calendar fechaActual = Calendar.getInstance();
		fechaNacimiento.setTime(getFecha(fecha));
		int anios = fechaActual.get(Calendar.YEAR)
				- fechaNacimiento.get(Calendar.YEAR);
		int mes = fechaActual.get(Calendar.MONTH)
				- fechaNacimiento.get(Calendar.MONTH);
		int dia = fechaActual.get(Calendar.DATE)
				- fechaNacimiento.get(Calendar.DATE);
		if (mes < 0 || (mes == 0 && dia < 0)) {
			anios--;
		}
		return anios;
	}

	/**
	 * Retorna el nombre de un mes a partir del número de mes
	 * 
	 * @param numero
	 * @return
	 */
	public String getNombreMes(int numero) {
		String meses[] = { "", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO",
				"JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE",
				"NOVIEMBRE", "DICIEMBRE" };
		return meses[numero];
	}
	
	/**
	 * Crea un archivo comprimido de varios archivo
	 * 
	 * @param archivos
	 *            arreglo de File con el numero de archivos
	 * @param nombrearchivo
	 *            nombre del archivo comprimido
	 */
	public void crearArchivoZIP(List<File> archivos, String nombrearchivo)
			throws Exception {
		// http://www.devtroce.com/2010/06/25/comprimir-y-descomprir-archivos-zip-con-java/
		int BUFFER_SIZE = 1024;
		// objetos en memoria
		FileInputStream fis = null;
		ZipOutputStream zipos = null;
		if (nombrearchivo.indexOf(".zip") < 0) {
			nombrearchivo.concat(".zip");
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
		resp.addHeader("Content-Disposition", "attachment; filename=\""
				+ nombrearchivo + "\"");

		// buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			// fichero comprimido
			zipos = new ZipOutputStream(resp.getOutputStream());
			for (File pFile : archivos) {
				// fichero a comprimir
				fis = new FileInputStream(pFile);
				ZipEntry zipEntry = new ZipEntry(pFile.getName());
				zipos.putNextEntry(zipEntry);
				int len = 0;
				// zippear
				while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zipos.write(buffer, 0, len);
				}
			}
			// volcar la memoria al disco
			zipos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			zipos.close();
			fis.close();
			fc.getApplication().getStateManager().saveView(fc);
			fc.responseComplete();
		}
	}

	/**
	 * Crea un archivo comprimido de varios archivo
	 * 
	 * @param archivos
	 *            arreglo de File con el numero de archivos
	 * @param nombrearchivo
	 *            nombre del archivo comprimido
	 */
	public void crearArchivoZIP(File[] archivos, String nombrearchivo)
			throws Exception {
		// http://www.devtroce.com/2010/06/25/comprimir-y-descomprir-archivos-zip-con-java/
		int BUFFER_SIZE = 1024;
		// objetos en memoria
		FileInputStream fis = null;
		ZipOutputStream zipos = null;
		if (nombrearchivo.indexOf(".zip") < 0) {
			nombrearchivo.concat(".zip");
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
		resp.addHeader("Content-Disposition", "attachment; filename=\""
				+ nombrearchivo + "\"");

		// buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			// fichero comprimido
			zipos = new ZipOutputStream(resp.getOutputStream());
			for (File pFile : archivos) {
				// fichero a comprimir
				fis = new FileInputStream(pFile);
				ZipEntry zipEntry = new ZipEntry(pFile.getName());
				zipos.putNextEntry(zipEntry);
				int len = 0;
				// zippear
				while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zipos.write(buffer, 0, len);
				}
			}
			// volcar la memoria al disco
			zipos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			zipos.close();
			fis.close();
			fc.getApplication().getStateManager().saveView(fc);
			fc.responseComplete();
		}
	}

	public void crearArchivo(File[] archivos, String nombrearchivo)
			throws Exception {
		// http://www.devtroce.com/2010/06/25/comprimir-y-descomprir-archivos-zip-con-java/
		int BUFFER_SIZE = 1024;
		// objetos en memoria
		FileInputStream fis = null;
		if (nombrearchivo.indexOf(".txt") < 0) {
			nombrearchivo.concat(".txt");
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
		resp.addHeader("Content-Disposition", "attachment; filename=\""
				+ nombrearchivo + "\"");

		// buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			ServletOutputStream out = resp.getOutputStream();

			// fichero comprimido
			for (File pFile : archivos) {
				// fichero a comprimir
				fis = new FileInputStream(pFile);
				int len = 0;
				// zippear
				while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					out.write(buffer, 0, len);
				}
			}
			// volcar la memoria al disco
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			fis.close();
			fc.getApplication().getStateManager().saveView(fc);
			fc.responseComplete();
		}
	}

	/**
	 * Crea un archivo comprimido de varios archivo
	 * 
	 * @param archivos
	 *            arreglo de File con el numero de archivos
	 * @param nombrearchivo
	 *            nombre del archivo comprimido
	 */
	public void downloadArchivo(String nombre_archivo) throws IOException {
		System.out.println("llega el archivo " + nombre_archivo);

		String archivo = "http://localhost:8080/sampu/" + nombre_archivo;
		File ficheroXLS = new File(archivo);
		System.out.println("path " + ficheroXLS.getPath());
		// System.out.println("path cano "+ficheroXLS.getCanonicalPath());
		System.out.println("path absolute " + ficheroXLS.getAbsolutePath());

		FacesContext ctx = FacesContext.getCurrentInstance();
		FileInputStream fis = new FileInputStream(ficheroXLS);
		byte[] bytes = new byte[2048];
		int read = 0;

		if (!ctx.getResponseComplete()) {
			String fileName = ficheroXLS.getName();
			// String contentType = "application/vnd.ms-excel";
			URL u = new URL("file:" + ficheroXLS);
			URLConnection uc = u.openConnection();
			String type = uc.getContentType();

			// System.out.println("Content type of file '"+ficheroXLS+"' is "+type);
			String contentType = type;
			HttpServletResponse response = (HttpServletResponse) ctx
					.getExternalContext().getResponse();

			response.setContentType(contentType);

			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ fileName + "\"");

			ServletOutputStream out = response.getOutputStream();

			while ((read = fis.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			out.close();
			ctx.responseComplete();
		}
	}
	
	public File descargarArchivo(String str_url, String nombre, String extensionURL)
	{
		try
		{
			
			URL url = new URL(str_url);
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			
			ByteArrayOutputStream ous = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }	
            
            if(nombre.length()>70)
            	nombre=nombre.substring(0, 70);
			
            String extension=".pdf";
            
            try {
            	extension=extensionURL.substring(extensionURL.indexOf("."),extensionURL.length());
            	//System.out.println("extension: "+extension);           	
            }catch(Exception e) {}
            
			File file = File.createTempFile(nombre, extension);

			FileOutputStream fos = new FileOutputStream(file);
            fos.write(ous.toByteArray());
            fos.flush();
            fos.close();
            
            ous.close();
            in.close();
            
            //System.out.println("descargarArchivo nombre: "+file.getName());
            file.deleteOnExit();
            
            return file;
		}
		catch(Exception ex)
		{
			System.out.println("ERROR descargarArchivo: "+ex.getMessage());
		}
		return null;
	}

	/**
	 * Retorna la diferencia en horas entre dos horas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public double getDiferenciaHoras(Date fechaInicio, Date fechaFin) {
		System.out.println("recibo lo siguiuente " + fechaInicio
				+ " fecha_fin " + fechaFin);
		double tiempoInicial = fechaInicio.getTime();
		System.out.println("tiempo_inicial " + tiempoInicial);

		double tiempoFinal = fechaFin.getTime();
		double dou_resta = tiempoFinal - tiempoInicial;
		// el metodo getTime te devuelve en mili segundos para saberlo en mins
		// debes hacer
		dou_resta = dou_resta / (1000 * 3600);
		System.out.println(" devuelvo metodo diferencia " + dou_resta);
		return dou_resta;
	}

	/**
	 * Retorna la diferencia en minutos entre dos horas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public double getDiferenciaMinutos(Date fechaInicio, Date fechaFin) {
		double tiempoInicial = fechaInicio.getTime();
		double tiempoFinal = fechaFin.getTime();
		double dou_resta = tiempoFinal - tiempoInicial;
		dou_resta = dou_resta / (1000 * 60);
		return dou_resta;
	}

	/**
	 * Válida que un rango de fecha sea correcto
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public boolean isFechasValidas(String fechaInicial, String fechaFinal) {

		if ((fechaInicial != null && isFechaValida(fechaInicial))
				&& (fechaFinal != null && isFechaValida(fechaFinal))) {
			// comparo que fecha2 es mayor a fecha1
			if (isFechaMayor(getFecha(fechaFinal), getFecha(fechaInicial))
					|| getFecha(fechaInicial).equals(getFecha(fechaFinal))) {
				return true;
			}
		} else
			agregarMensaje("Debe Seleccionar 2 Fechas", "Para Iniciar Busqueda");

		return false;
	}

	/**
	 * Retorna una fecha en formato largo
	 * 
	 * @param fecha
	 * @return
	 */
	public String getFechaLarga(String fecha) {
		SimpleDateFormat formateador = new SimpleDateFormat(
				"EEEE d 'de' MMMM 'del' yyyy");
		String str_fecha = formateador.format(getFecha(fecha));
		return str_fecha;
	}

	/**
	 * Convierte a Date una hora usando los métodos de la clase Calendar
	 * 
	 * @param hora
	 * @return
	 */
	public Date getHoraCalendario(String hora) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date date;
		try {
			date = formatter.parse(hora);
			cal.setTime(date);
		} catch (Exception e) {
			try {
				cal.setTime(getHora(hora));
			} catch (Exception e1) {
			}
		}
		//cal.set(Calendar.AM_PM, Calendar.PM);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		return cal.getTime();
	}
	
	public Date obtenerFechaInicioAnio()
	{
		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		Date fch = getDate();

		String hoy = formato.format(fch);
		String[] dataTemp = hoy.split("-");

		return DeStringADate(pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[0])+"", 4)  + "-01-01");
	}
	
	public Date obtenerFechaFinAnio()
	{
		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		Date fch= getDate();

		String hoy = formato.format(fch);
		String[] dataTemp = hoy.split("-");
		
		return DeStringADate(pckUtilidades.Utilitario.padLeft(pckUtilidades.CConversion.CInt(dataTemp[0])+"", 4)  + "-12-31");
	}
	
	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new  SimpleDateFormat ("hh:mm:ss");

			String hora1 = getFormatoHora(hora_ini);
			String hora2 = getFormatoHora(hora_fin);

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
	
	public List getListaGrupoCuentaPresupuesto() {
		// pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
		List lista = new ArrayList();
		Object fila1[] = { "G", "Cuenta General" };
		Object fila2[] = { "S", "Cuenta Grupal" };
		Object fila3[] = { "F", "Cuenta Final" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		return lista;
	}

	public List getListaClasificacionEconomica() {
		// pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
		List lista = new ArrayList();
		Object fila1[] = { "1", "INGRESOS CORRIENTES" };
		Object fila2[] = { "2", "INGRESOS DE CAPITAL" };
		Object fila3[] = { "3", "INGRESOS DE FINANCIAMIENTO" };
		Object fila4[] = { "4", "GASTOS CORRIENTES" };
		Object fila5[] = { "5", "GASTOS DE FINANCIAMIENTO" };
		Object fila6[] = { "6", "GASTOS DE PRODUCCION" };
		Object fila7[] = { "7", "GASTOS DE INVERSION" };
		Object fila8[] = { "8", "GASTOS DE CAPITAL" };
		Object fila9[] = { "9", "APLICACION DE FINANCIAMIENTO" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		lista.add(fila7);
		lista.add(fila8);
		lista.add(fila9);
		return lista;
	}

	public List getListaGrupoSigef() {
		// pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
		List lista = new ArrayList();
		Object fila1[] = { "1", "SI" };
		Object fila2[] = { "2", "NO" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}

	public List getListaFlujo() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = { "1", "1 Debito" };
		Object fila2[] = { "2", "2 Credito" };
		Object fila3[] = { "3", "3" };
		Object fila4[] = { "4", "4" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		return lista;
	}
	
	public List getListaSumaResta() {
		List lista = new ArrayList();
		Object fila1[] = { "-1", "NEGATIVO" };
		Object fila2[] = { "1", "POSITIVO" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}
	
	public List getListaTipoEfectivo() {
		List lista = new ArrayList();
		Object fila1[] = { "1", "BILLETE" };
		Object fila2[] = { "2", "MONEDA" };
		Object fila3[] = { "3", "CHEQUE" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		return lista;
	}
	
	public List getListaTipoGrupo() {
		List lista = new ArrayList();
		Object fila1[] = { "ACTIVOS", "ACTIVOS" };
		Object fila2[] = { "PASIVOS", "PASIVOS" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}

	public List getListaTipoGrupoFlujo() {
		List lista = new ArrayList();
		Object fila1[] = { "FUENTES", "FUENTES" };
		Object fila2[] = { "USOS", "USOS" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}


	public List getListaGruposNivelCuenta() {

		List lista = new ArrayList();
		Object fila1[] = { "1", "TITULO" };
		Object fila2[] = { "2", "GRUPO"};
		Object fila3[] = { "3", "SUBGRUPO"};
		Object fila4[] = { "4", "CUENTA NIVEL 1"};
		Object fila5[] = { "5", "CUENTA NIVEL 2"};
		Object fila6[] = { "6", "CUENTA NIVEL 3" };
		Object fila7[] = { "7", "CUENTA NIVEL 4" };
		Object fila8[] = { "8", "CUENTA NIVEL 5" };
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		lista.add(fila7);
		lista.add(fila8);
		return lista;
	}

	public List getListaGruposNivelPresupuesto() {

		List lista = new ArrayList();
		Object fila1[] = { "1", "1"};
		Object fila2[] = { "2", "2"};
		Object fila3[] = { "3", "3"};
		Object fila4[] = { "4", "4"};
		Object fila5[] = { "5", "5"};
		Object fila6[] = { "6", "6"};

		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		return lista;
	}

	public List getListaAperturaCierre() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"0", "N/A"};
        Object fila2[] = {"1", "APERTURA"};
        Object fila3[] = {"2", "CIERRE"};
        Object fila4[] = {"3", "APERTURA/CIERRE"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);

		return lista;
	}
	
	public List getListaTipoSaldo() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"1", "SALDO DEUDOR"};
        Object fila2[] = {"2", "SALDO ACREEDOR"};
        Object fila3[] = {"3", "SALDO DEUDOR - ACREEDOR"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);

		return lista;
	}

	public List getListaTipoCobro() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"1", "COMPROBANTES"};
        Object fila2[] = {"2", "CONTRATOS"};
        Object fila3[] = {"3", "OTROS"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);

		return lista;
	}
	
	public List getListaTipoComprobantes() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"0", "TODOS"};
        Object fila2[] = {"1", "FACTURAS"};
        Object fila3[] = {"2", "NOTAS CREDITO"};
        Object fila4[] = {"3", "NOTAS DEBITO"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);

		return lista;
	}
	
	public List getListaTipoEstados() {
		List lista = new ArrayList();
		Object fila1[] = {"0", "TODOS"};
        Object fila2[] = {"1", "ANULADOS"};
        Object fila3[] = {"2", "EMITIDOS"};
        Object fila4[] = {"24", "CON INTERES"};
        Object fila5[] = {"16", "PAGADOS"};
        Object fila6[] = {"30", "ABONADOS"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);

		return lista;
	}

	public List getListaTipoProducto() {

		List lista = new ArrayList();
		Object fila1[] = {"0", "NORMALIZADO"};
        Object fila2[] = {"1", "NO NORMALIZADO"};

		lista.add(fila1);
		lista.add(fila2);

		return lista;
	}

	public List getListaTipoRegimen() {

		List lista = new ArrayList();
		Object fila1[] = {"0", "ESPECIAL"};
        Object fila2[] = {"1", "COMUN"};

		lista.add(fila1);
		lista.add(fila2);

		return lista;
	}
	
	public List getListaAnios() {
		List lista = new ArrayList();
		lista.add(new Object []{ "2018", "2018" });
		lista.add(new Object []{ "2019", "2019" });
		lista.add(new Object []{ "2020", "2020" });
		lista.add(new Object []{ "2021", "2021" });
		lista.add(new Object []{ "2022", "2022" });
		lista.add(new Object []{ "2023", "2023" });
		lista.add(new Object []{ "2024", "2024" });
		lista.add(new Object []{ "2025", "2025" });
		lista.add(new Object []{ "2026", "2026" });
		lista.add(new Object []{ "2027", "2027" });
		lista.add(new Object []{ "2028", "2028" });
		lista.add(new Object []{ "2029", "2029" });
		lista.add(new Object []{ "2030", "2030" });
		return lista;
	}
	
	public List getListaTipoContratos() {
		List lista = new ArrayList();
		lista.add(new Object []{ "0", "Interno" });
		lista.add(new Object []{ "1", "Externo" });
		lista.add(new Object []{ "2", "Infima Cuantía" });
		lista.add(new Object []{ "3", "Civil" });
		lista.add(new Object []{ "4", "Regimen Especial" });
		lista.add(new Object []{ "5", "Convenios" });
		return lista;
	}
	
	
	public List getListaEstadoProceso() {
		List lista_estado = new ArrayList();
	    Object fila1[] = {"REGISTRADO", "REGISTRADO"};
	    Object fila2[] = {"EN PROCESO", "EN PROCESO"};
	    Object fila3[] = {"FINALIZADO ENVIADO A CONTRACTUAL", "FINALIZADO ENVIADO A CONTRACTUAL"};
	    Object fila4[] = {"NO APROBADO", "NO APROBADO"};
	    Object fila5[] = {"FINALIZADO DESIERTO", "FINALIZADO DESIERTO"};
	    Object fila6[] = {"FINALIZADO POR RESOLUCIÓN CANCELACIÓN", "FINALIZADO POR RESOLUCIÓN CANCELACIÓN"};
	    Object fila7[] = {"FINALIZADO ENVIADO A GESTIÓN PAGOS", "FINALIZADO ENVIADO A GESTIÓN PAGOS"};
	    Object fila8[] = {"FINALIZADO AL PUBLICAR EN PORTAL SERCOP", "FINALIZADO AL PUBLICAR EN PORTAL SERCOP"};
	    Object fila9[] = {"FINALIZADO POR APROBACIÓN SERCOP", "FINALIZADO POR APROBACIÓN SERCOP"};
	    Object fila10[] = {"ENVIADO", "ENVIADO"};
	    Object fila11[] = {"APROBADO", "APROBADO"};
	    
	    lista_estado.add(fila1);
	    lista_estado.add(fila2);
	    lista_estado.add(fila3);
	    lista_estado.add(fila4);
	    lista_estado.add(fila6);
	    lista_estado.add(fila7);
	    lista_estado.add(fila8);
	    lista_estado.add(fila9);
	    lista_estado.add(fila10);
	    lista_estado.add(fila11);
		return lista_estado;
	}
	
	public List getListaSiNo() {
		List lista = new ArrayList();
		Object fila1[] = { "SI", "SI" };
		Object fila2[] = { "NO", "NO" };
		lista.add(fila1);
		lista.add(fila2);
		return lista;
	}
	
	public List getListaTipoProcesoContracion(boolean seleccion) {
		List lista_producto = new ArrayList();
		Object fila_prd0[] = {"NA", "N/A"};
	    Object fila_prd[] = {"TR", "Servicios y Consultoría"};
	    Object fila_prd1[] = {"ET", "Bienes u Obras"};
	    Object fila_prd2[] = {"PG", "Preguntar"};
	    Object fila_prd3[] = {"DE", "Denuncia"};
	    if(seleccion)
	    {
	    	lista_producto.add(fila_prd);
	 	    lista_producto.add(fila_prd1);
	    }
	    else
	    {
		    lista_producto.add(fila_prd0);
		    lista_producto.add(fila_prd);
		    lista_producto.add(fila_prd1);
		    lista_producto.add(fila_prd2);
		    lista_producto.add(fila_prd3);

	    }
		return lista_producto;
	}
	
	
/*	
	
	// ///ENVIO DE EMAIL

	String miCorreo;
	String miContraseña;
	// String servidorSMTP = "smtp.gmail.com";
	// String puertoEnvio = "465";
	String servidorSMTP = "mail.emgirs.gob.ec";
	String puertoEnvio = "25";
	//String mailReceptor;
	//String asunto;
	//String cuerpo;
	//String filename = "C:/TEMP/reporte.pdf";

	// metodo que recibe y envia el email
	public void EnviaEmail(String miCorreo, String miContraseña,
			String mailReceptor, String asunto, String cuerpo, File filearchivo) {
		this.miCorreo = miCorreo;
		this.miContraseña = miContraseña;
		//this.mailReceptor = mailReceptor;
		//this.asunto = asunto;
		//this.cuerpo = cuerpo;

		Properties props = new Properties();// propiedades a agragar
		props.put("mail.smtp.user", this.miCorreo);// correo origen
		props.put("mail.smtp.host", servidorSMTP);// tipo de servidor
		props.put("mail.smtp.port", puertoEnvio);// puesto de salida
		props.put("mail.smtp.starttls.enableue", false);// inicializar el
														// servidor
		// props.put("mail.transport.protocol",smpt);
		props.put("mail.smtp.auth", "false");// autentificacion
		props.put("mail.smtp.socketFactory.port", puertoEnvio);// activar el
																// puerto
		// props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", false);

		SecurityManager security = System.getSecurityManager();

		try {
			Authenticator auth = new autentificadorSMTP();// autentificar el
															// correo
			Session session = Session.getInstance(props, auth);// se inica una
																// session

			// Se compone la parte del texto
			BodyPart texto = new MimeBodyPart();
			texto.setText(cuerpo);

			// Se compone el adjunto con la imagen
			BodyPart adjunto = new MimeBodyPart();
			DataSource source = new FileDataSource(filearchivo);
			adjunto.setDataHandler(new DataHandler(source));
			adjunto.setFileName(filearchivo.getName());

			// Una MultiParte para agrupar texto e imagen.
			MimeMultipart multiParte = new MimeMultipart();
			// BodyPart messageBodyPart = new MimeBodyPart();
			// messageBodyPart.setContent(texto, "text/html");

			multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);

			// Se compone el correo, dando to, from, subject y el
			// contenido.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.miCorreo));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailReceptor));
			message.setSubject(asunto);
			message.setContent(multiParte);

			Transport.send(message);// envia el mensaje

		} catch (Exception mex) {
			System.out.println("ff" + mex);
		}// fin try-catch
	}// fin metodo enviaEmail
*/

	public List<Object> getIVAListOptions(){
      // TODO Auto-generated method stub
	  String[] parameters = this.getVariable("p_tipos_iva").split(";");
	  List<Object> listax = new ArrayList<Object>();
	  for (String parameter : parameters) {
	    listax.add(parameter.split("-"));
      }
	  return listax;
	}

	
	public List getComprobanteListOptions(){
      // TODO Auto-generated method stub
      String[] parameters = this.getVariable("p_tipo_comprobante").split(";");
      List listax = new ArrayList<Object>();
      for (String parameter : parameters) {
        listax.add(parameter.split("-"));
      }
      return listax;
    }
    /*
	private class autentificadorSMTP extends javax.mail.Authenticator {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(miCorreo, miContraseña);
		}
	}*/
	
	public List<String> consumoRIDEJson(String clave_acceso) 
    {
           String[] mensaje=new String[3];
           mensaje[0]="Error";
           mensaje[1]="consumoRIDEJson";
           try {           
                URL url = new URL(pckUtilidades.Constantes.pathCORE+"ride/"+clave_acceso+".json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(5000); //set timeout to 5 seconds
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("tocken", pckUtilidades.Constantes.secret_key);

                if (conn.getResponseCode() != 200) {
                    mensaje[0]="Error";
                    System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                    mensaje[1]="Failed : HTTP error code : " + conn.getResponseCode();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output = br.readLine();
                //System.out.println("consumoRIDEJson: "+output); 
                conn.disconnect();
                
                Object obj=JSONValue.parse(output);  
                JSONObject jsonObject = (JSONObject) obj;        
                
                if(output.toUpperCase().contains("RIDE"))
                {
                   JSONObject objRide = (JSONObject)jsonObject.get("ride");

                   String arrayRide = pckUtilidades.CConversion.CStr(objRide.get("data"));
                   //System.out.println("arrayRide: "+arrayRide); 
                   mensaje[0]="OK";
                   mensaje[1]=arrayRide;
                }                
                    
           } catch (IOException ex) {
               System.out.println("consumoRIDEJson: "+ex.getMessage());
           }
           
           List<String> mensajeResp = new ArrayList<String>();
           mensajeResp.add(mensaje[0]);
           mensajeResp.add(mensaje[1]);
           
           return mensajeResp;
     }	

}
