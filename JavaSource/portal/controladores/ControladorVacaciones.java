/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.io.BufferedInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Utilitario;
import pckEntidades.EnvioMail;
import pckUtilidades.Constantes;
import persistencia.Conexion;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisoJustificacion;
import portal.entidades.AsiPermisosVacacionHext;
import portal.entidades.AsiVacacion;
import portal.entidades.ConReloj;
import portal.entidades.GthEmpleado;
import portal.entidades.SisUsuario;
import portal.servicios.ServicioEmpleadoJPA;
import portal.servicios.ServicioVacacionesJPA;
import framework.aplicacion.TablaGenerica;
import paq_sistema.parametros.Parametros;
import paq_sistema.ejb.ServicioSeguridad;
import paq_contabilidad.ejb.*;
import portal.entidades.ConBiometricoMarcaciones;

import java.sql.Timestamp;
import java.util.Date;
/**
 * 
 * @author Juan Ayerve
 */
@ManagedBean
@ViewScoped
public class ControladorVacaciones {

	// variables para calculo de vacaciones
	private Integer nda;
	private Double numeroDiasVacacionXAnio = 0.0;
	private Integer ide_gttem;
	private Integer nde;
	private Integer ndeInicialFechaCalculoDiasTomados;
	private double numeroDiasTomados;
	private Object objMatriz;
	private List<Double[]> matriz;
	private Double[] obj = new Double[4];
	private Date date = null;
	private List<String[]> lisPeriodosEmpleado;
	private String Fecha = "";
	private double sumatotal_vacaciones = 0.0;
	private double sumatotal_vacacionesCalculoInicial = 0.0;
	private int dias_acumulados_inicial = 0;
	private double resultado_detalle = 0.0;
	private String fecha_ingreso = "";
	double anterior_dias_pendientes;
	double dias_sumados_aplicados_vacacion;
	private TablaGenerica tab_novedad;
	private int p_asi_dias_max_vacaciones_losep;
	private int p_asi_dias_max_vacaciones_codigo_trabajo;
	private int p_asi_dias_anio;

	// Calculo de de dias tomados por el empleado
	private double numeroDiasTomadosInicial;
	private double totalNumeroDiasTomadosInicial;
	private double diasPendientesInicialAjuste;
	private double nroDiasAjustePeriodo;

	private int acumulaFinesSemana;
    private String apruebaJefeInmediato;
	// crear en parametros
	private double calculo_dias_max_aplica = 0.0;
	private double diasGraciaPuedeTomar = 0.0;
	private int numeroInicioFinesSemana = 0;
	private int numeroInicioFinesSemanaSolicitados = 0;
	private int numeroInicioFinesSemanaPendientes = 0;

	// Variables de Correo
	int obtieneNroDias;
	private String nombreTipoSolicitud;
	double nroHoras;
	private int tipo_solicitud;
	private String ide_aspvh_correo;
	private String motivo;

	private GthEmpleado empleado;

	private String strOpcion = "1",p_encargado_revision_permisos_enfermedad="0",	p_permisos_doc_enfermedad="0",p_encargado_revision_permisos_enfermedad_trabajador_social="0";
;

	private Utilitario utilitario = new Utilitario();

	private AsiPermisosVacacionHext solicitudVacaciones;
	private AsiPermisoJustificacion solicitudJustificacion;
	private SisUsuario usuario;
	private AsiVacacion solicitudVacacion;
	private ConBiometricoMarcaciones biometrico;
	private Integer intCodigoEnviado=0;
	private Boolean estadoMarcacion=false;//HABILITAR O DESHABILITAR BOTON GUARDAR MARCACIONES
	TimeZone retornaValor=TimeZone.getDefault();
	int	validaIngreso=0;
	boolean	banderaPermisoExcedido=false,bandPermisoTeletraajo=false;
	String empleadoPreAprobador="";
	private int numeroSumarFinesSemana = 0;

	/**
	 * IDE_ASMOT,DETALLE_ASMOT,APLICA_VACACIONES_ASMOT,DESCRIPCION_ASMOT,
	 * NUM_MAX_DIAS_ASMOT,esvacacion Obj
	 * [IDE_ASMOT,DETALLE_ASMOT,TIEMPO_PERMISO_ASMOT
	 * ,APLICA_HORAS_DIAS_ASMOT,DESCRIPCION_ASMOT
	 * ,NUM_MAX_DIAS_ASMOT,ARCHIVO_ADJUNTO_ASPVH]
	 */
	private List listaMotivos;
	private List listaPermisos;
	private List listaAnios;
	private List listaMeses;
	private List listaJefesInmediatos;
	private List listaJustificacion;

	private List<AsiPermisosVacacionHext> listaSolicitudes;
	private List<AsiPermisosVacacionHext> listaSolicitudesRecibidas;
	private List<AsiPermisoJustificacion> listaJustificaciones;
	private List<ConBiometricoMarcaciones> listaMarcaciones;

	private boolean aplica_dias;
	private boolean no_aplica_dias;
	private String estadoJefe = "EN CURSO";

	private boolean aplicavacacion;
	private Boolean esvacacion;
	private String descripcionMotivo;
	private Integer numMaximoDiasPermiso;
	private Integer tiempo_permiso;

	private Integer numMaxRegistroPermiso=0;
	private Integer numMaxAprobacionPermiso=0;

	// variables para cambiar las horas de ingreso y salida
	// private Integer totalSegundosHoraEntrada = 28800;
	// private Integer totalSegundosHoraSalida = 61200;
	private Integer totalSegundosHoraEntrada = 0;
	private Integer totalSegundosHoraSalida = 612000;

	private Boolean enabledDias = true;
	private boolean puedeGuardarSolicitud = true;
	// private double diasTotales =0;
	private int numeroFinesSemana = 0;
	private int numeroFinesSemanaTotal = 0;
	private int descuentoFinesSemana = 0;
	private double numeroDiasTomadosTemporal;

	// Ajuste inicial de dias

	private double calculaParteDecimal = 0.0;
	private int calcula_dias_vacacion = 0;
	private int enteroDias = 0;
	private double valor = 0.0;
	private int calculoDias = enteroDias = 0;
	private int enteroSuma = 0;

	private Object objDiasVacacionesActiva;
	private List lisResumenVacaciones;
	private List lisVacacionesEmpleados;
	private Map parametros = new HashMap();
	private JasperPrint jasperPrint;
	private String strPathReporte;
	private String hora_inicial_reporte = "";
	private String hora_final_reporte = "";
	private String nuemro_horas_reporte = "";
	private Connection conn;
	private double totalNumeroDiasAjuste;
	private double numeroDiasTomadosFinSemana;
	private double numeroDiasTomadosTemp=0.0;
	private int dias_final_extra= 0;
	private String ver_dias_extra="";
	/**
	 * variables para calculo de horas extra
	 */
	private String fechaEvento="";
	private double dias_pendientes;
    private String fechaHoraMarcacion="";
	private UploadedFile adjunto;
	//Detalle de vacaciones de empleado
	private String part1="";
	private String part2=""; 
	private int numCol=0;
	private int ide_asmot;
	private String str_dias_pendientes="";
	@EJB
	private ServicioEmpleado ser_empleado;
	@EJB
	private ServicioContabilidad ser_contabilidad;
	@EJB
	private ServicioEmpleadoJPA servicioEmpleado;
	@EJB
	private ServicioVacacionesJPA servicioVacaciones;
//	@EJB
//	private ServicioVacacionesJPA servicioVacaciones;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	private boolean enabledDiasHoras=true;
	private boolean enabledHasta=true;
	private String banderaCodigo="";


	@PostConstruct
	public void cargarDatos() {
		boolean	banderaPermisoExcedido=false;
		enabledDiasHoras=true;
		Double dias_pendientes_resumen=0.0,dias_acumulados=0.00,dias_acumulados_temp=0.00;
		Double total_dias_extra=0.00,dias_extra_temp=0.00;
		int valor_entero_dias_extra=0,valor_total_dias_extra=0;
		boolean bandera=false;
		String detalleVacacionesEmp="";
		String str=utilitario.getVariable("p_vacacion_cambio_contrato_empleados");
	    String[] splitUno =str.split(",");
		String[] parts;
		numeroDiasTomados=0.00;
		double dias1=0.0,dias_extra=0.0;
		TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
		
		
	    for (int x=0; x<splitUno.length; x++) 
	    {
	       if (splitUno[x].equals(utilitario.getVariable("IDE_GTEMP"))) {
			bandera=true;
			x=splitUno.length;
		}else {
			bandera=false;
		}
	    }
			
		if (bandera==true) {
		detalleVacacionesEmp=detalleVacaciones(utilitario.getVariable("IDE_GTEMP"));
		if (detalleVacacionesEmp==null || detalleVacacionesEmp.equals("") || detalleVacacionesEmp.isEmpty()) {
			numCol=0;	
		}else {
			numCol=2;
			String string = detalleVacacionesEmp;
			parts = string.split(",");
			part1 = parts[0];// 123
			part2= parts[1]; // 654321
	
		}
		}else {
			numCol=0;
		}
		
		inicializar();
		biometrico = new ConBiometricoMarcaciones();
		biometrico.setActivoCobim(true);
		biometrico.setEstatusCobim(1);
		biometrico.setEventoRelojCobim("1");
		biometrico.setIdeCorel(new ConReloj(15));
		biometrico.setIdeCodigoValidadorCobim(0);
		
//if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==4){
		
		//biometrico.setIdeCorel(15);
		//biometrico.setEstatusCobim(1);
		//biometrico.setEventoRelojCobim("01");
		
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//System.out.println("timestamp "+timestamp);
		Date fecha= utilitario.DeStringADate(utilitario.getFechaActual());
		 //System.out.println("fecha actual "+new Timestamp(fecha.getTime()));

        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
        //System.out.println("timestamp getTime "+timestamp.getTime());

        //format timestamp
        //System.out.println("formato "+sdf.format(timestamp)); 
    
        fechaEvento="";

		nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		empleado = servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP"));
		solicitudVacaciones = new AsiPermisosVacacionHext();
		solicitudVacaciones.setIdeAsmot(new AsiMotivo());
		// solicitudVacaciones.setGenPeriodo(new GenPeriodo());
		// solicitudVacaciones.getGenPeriodo().setGenPeriodoPK(new
		// GenPeriodoPK());
		// solicitudVacaciones.getGenPeriodo().setGenAnio(new GenAnio());
		// solicitudVacaciones.getGenPeriodo().setGenMes(new GenMes());
		//solicitudVacaciones.setTipoAspvh(new Integer("2")); // Tipo vacaciones
		solicitudVacaciones.setIdeGtemp(servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP")));
		solicitudVacaciones.setIdeGeedp(servicioEmpleado.getEmpleadoDepartamentoPartida(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString()));
		solicitudVacaciones.setGenIdeGeedp2(servicioEmpleado.getEmpleadoDepartamentoPartida(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString()));
       	solicitudVacaciones.setUsuarioIngre(usuario.getValor("nick_usua").toString());

		// solicitudVacaciones.setIdeSucu(solicitudVacaciones.getIdeGeedp().getGenPartidaGrupoCargo().GETIDE);
		solicitudVacaciones.setFechaSolicitudAspvh(utilitario.getDate());

		solicitudVacaciones.setActivoAspvh(new Boolean(true));
		solicitudVacaciones.setAprobadoAspvh(new Boolean(false));
		solicitudVacaciones.setAprobadoTthhAspvh(new Boolean(false));
		solicitudVacaciones.setAnuladoAspvh(new Boolean(false));
		solicitudVacaciones.setRegistroNovedadAspvh(new Boolean(false));

		listaAnios = servicioVacaciones.getAnios();
		listaMeses = servicioVacaciones.getMeses();
		listaMotivos = servicioVacaciones.getMotivosVacaciones();
		listaJustificacion = servicioVacaciones.getJustificacion();

		listaPermisos = servicioVacaciones.getTipoPermiso();

		TablaGenerica tabEmpDep = getEmpledoPartida(utilitario.getVariable("ide_usua"));
		String IDE_GECAF =""; 
		IDE_GECAF =tabEmpDep.getValor("IDE_GECAF");
		String IDE_GEGRO="";
		IDE_GEGRO=tabEmpDep.getValor("IDE_GEGRO");
		
		listaJefesInmediatos = servicioVacaciones.getJefesInmediatos();

/*		if (IDE_GEGRO.equals("7") || IDE_GEGRO.equals("6") || IDE_GEGRO.equals("4") || IDE_GEGRO.equals("2")) {
		listaJefesInmediatos = servicioVacaciones.getJefesInmediatos();

		}else {
			listaJefesInmediatos = servicioVacaciones.getJefesInmediatosSinGerente();
 
		}*/
		
		// listaJefesInmediatos =
		// servicioVacaciones.getJefesInmediatos(IDE_GEARE,IDE_GEGRO);

		if (IDE_GEGRO.equals("6")) {
			solicitudVacaciones.setAprobadoAspvh(true);
		}
		
		/*
		 * //para escribir el archivo le mando el campo de tipo bite if
		 * (escribirArchivo(listaSolicitudes.get(0).getArchivoAdjuntoAspvh(),
		 * "C://ejemplo.pdf")) { try { downloadFile("C://ejemplo.pdf"); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */

		estadoJefeSolicitud();
		solicitudJustificacion = new AsiPermisoJustificacion();

		TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
		if (tab_partida != null) {
			// Obtengo la fecha de ingreso de la persona seleccionada

	TablaGenerica tab_codigo_vacacion1 = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,"
															+ "nro_dias_ajuste_periodo_asvac "
															+ "from asi_vacacion where ide_gtemp=" + utilitario.getVariable("IDE_GTEMP")+" "
					+ "AND ACTIVO_ASVAC=true");
			
			
			 int  bandEntrada=0;
			 int bandSalida=0;
			String IDE_GTEMP=utilitario.getVariable("IDE_GTEMP");
			 String ide_asvac="";
			 ide_asvac=tab_codigo_vacacion1.getValor("ide_asvac");
			 
			 
			 
				nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
				String fecha_ingreso_continuidad="",fecha_fin_continuidad="",ide_asvac_continuidad="",fecha_ingreso_continuidad_temp="";
				ide_asvac_continuidad=ide_asvac;
				boolean bandFecha=false;
			
				TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,"
						+ "fecha_finiquito_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,activo_asvac "
						+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" "
						+ "AND IDE_ASVAC="+ide_asvac);
				
				
				TablaGenerica tab_vacacion_continuidad_temp = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
						+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
						+ " and fecha_ingreso_asvac < '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"' and es_continuidad=false "
						+ "order by ide_asvac desc limit 1");
				
				
				TablaGenerica tab_vacacion_continuidad=null;
				
				if (tab_vacacion_continuidad_temp.getTotalFilas()>0) {
					tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
							+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
							+ " and fecha_ingreso_asvac  between '"+utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_vacacion_continuidad_temp.getValor("fecha_ingreso_asvac")), 1))+"'  and '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"'  "
							+ "order by ide_asvac desc");
				}else {
					tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
							+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false and ide_asvac not in ("+tab_codigo_vacacion.getValor("ide_asvac")+") "
							+ " and fecha_ingreso_asvac < '"+tab_codigo_vacacion.getValor("fecha_ingreso_asvac")+"' "
							+ "order by ide_asvac desc");

				}
				fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
				
				
				if (tab_vacacion_continuidad.getTotalFilas()>0) {
					for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
						if ( tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
						//SI TIENE CONTINUIDAD
							if (bandFecha==true) {
								
							}else
							if (tab_vacacion_continuidad.getTotalFilas()==1) {
							//	ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac");
								fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");

							}else {
								//ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac")+",";	
								//fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");
							}
							 
							break;
						}else{
						//SI ES LIQUIDACION
							fecha_ingreso_continuidad_temp=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
							fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");

							bandFecha=true;
							if((bandFecha== true && i>0) && (fecha_ingreso_continuidad_temp.compareTo(tab_vacacion_continuidad.getValor((i-1),"fecha_ingreso_asvac"))>0)){
								//if(bandFecha== true && (fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==0 || fecha_ingreso_continuidad_temp.compareTo(fecha_fin_continuidad)==-1)){
								fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor((i-1),"fecha_ingreso_asvac");
							}else {
								fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
							} 
							
							
							if ((tab_vacacion_continuidad.getTotalFilas()-1)==i) {
								ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");
							}else {
								ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");	
							}

						}
					}
				}else{
					fecha_ingreso_continuidad=tab_codigo_vacacion.getValor("fecha_ingreso_asvac");				
				}
				
				if (tab_codigo_vacacion.getValor("activo_asvac")==null || tab_codigo_vacacion.getValor("activo_asvac").equals("false")) {
					fecha_fin_continuidad=tab_codigo_vacacion.getValor("fecha_finiquito_asvac");
				}else {
					//fecha_fin_continuidad=null;
					fecha_fin_continuidad=utilitario.getFechaActual();
				}
				
			
			
			
			
			

			
			
			
			
		StringBuilder str_ide_geedp= new StringBuilder();
			int contador_=0;
		       //TablaGenerica tab_partidaTemporal = ser_empleado.getPartidaEmpleado(utilitario.getVariable("IDE_GTEMP"));
		       TablaGenerica tab_partidaTemporal=retornaTablaAccionResumenVacacion(fecha_ingreso_continuidad,utilitario.getFechaActual(),utilitario.getVariable("IDE_GTEMP"));
	 
	        if (tab_partidaTemporal != null) {
	        	if (tab_partidaTemporal.getTotalFilas()>0) {
	         		for (int i = 0; i < tab_partidaTemporal.getTotalFilas(); i++) 
	   			 {
	         			str_ide_geedp.append(tab_partidaTemporal.getValor(i, "IDE_GEEDP"));
	                if (tab_partidaTemporal.getTotalFilas()==1) {
		   			}else if (contador_<=tab_partidaTemporal.getTotalFilas()) {
		   					contador_++;
		   					if(contador_<(tab_partidaTemporal.getTotalFilas())){
	   						str_ide_geedp.append(",");
	                    //System.out.println("str_ide:  "+str_ide_geedp);
	   					}
	    			}
	    
	   			 }

	         	}
	        }else {
	        	str_ide_geedp.append("-1");

			}

		listaSolicitudes = servicioVacaciones.getSolicitudesVacaciones(str_ide_geedp.toString(), "ideGeedp");
		listaSolicitudesRecibidas = servicioVacaciones.getSolicitudesVacacionesSolicitadas(str_ide_geedp.toString(), "genIdeGeedp2");
		if (listaSolicitudes == null) {
			listaSolicitudes = new ArrayList<AsiPermisosVacacionHext>();
		} else {
			listaSolicitudes = servicioVacaciones.getSolicitudesVacaciones(str_ide_geedp.toString(), "ideGeedp");
		}

		if (listaSolicitudesRecibidas == null) {
			listaSolicitudesRecibidas = new ArrayList<AsiPermisosVacacionHext>();
		} else {
			listaSolicitudesRecibidas = servicioVacaciones.getSolicitudesVacacionesSolicitadas(str_ide_geedp.toString(), "genIdeGeedp2");

		}



			TablaGenerica tabCodigo = utilitario.consultar("select ide_gtemp,tarjeta_marcacion_gtemp from gth_empleado where ide_gtemp="+utilitario.getVariable("IDE_GTEMP"));
			String IDE_CODIGOMARCACION =""; 
			IDE_CODIGOMARCACION =tabCodigo.getValor("tarjeta_marcacion_gtemp");

			if (IDE_CODIGOMARCACION==null || IDE_CODIGOMARCACION.equals("") || IDE_CODIGOMARCACION.isEmpty()) {
				IDE_CODIGOMARCACION="-1";
			}
			listaMarcaciones = servicioVacaciones.getMarcionesTeletrabajo(IDE_CODIGOMARCACION);
			if (listaMarcaciones == null) {
				listaMarcaciones = new ArrayList<ConBiometricoMarcaciones>();

			} else {
				listaMarcaciones = servicioVacaciones.getMarcionesTeletrabajo(IDE_CODIGOMARCACION);
			}
			
			
			
			
			fecha_ingreso =fecha_ingreso_continuidad;
			//= servicioVacaciones.getFechaIngresoEmpleado(tab_partida.getValor("IDE_GEEDP"));

			// Obtengo el numero de dias a partir de la fecha de entrada del
			// empleado hasta la fecha actual
			//nde = servicioVacaciones.getNumeroDiasEmpleado(utilitario.getVariable("IDE_GTEMP")eee);
			nde = servicioVacaciones.getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac_continuidad,fecha_ingreso_continuidad,fecha_fin_continuidad);

			// Obtengo el numero de dias tomados a vacacion por el empleado
			// desde la fecha de ingreso hasta 30 de abril 2017
			ndeInicialFechaCalculoDiasTomados = servicioVacaciones.getNumeroDiasEmpleadoCalculoInicial(utilitario.getVariable("IDE_GTEMP"));

			// Obtener los periodos de cada empleado 1,2,3 etc
			List<Integer> peridos = servicioVacaciones.getPeriodos(nda, nde);
			// Obtengo los periodos para el ajuste de dias pendientes del
			// empleado hasta el 30 de abril del 2017
			List<Integer> peridosCalculoIncial = servicioVacaciones.getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);

			/*
			 * for(Integer periodo:peridos){
			 * System.out.println("periodos del empleado hasta 30 de abril "
			 * +peridosCalculoIncial); }
			 */

			// Realizo la consulta para obtener el grupo al q pertenece el
			// trabajador 1 codigo de trabajo, 2losep
			ide_gttem = servicioVacaciones.getGrupoTipoEmpleado(utilitario.getVariable("IDE_GTEMP"));

			if (ide_gttem == 1) {
				// Asigno el numero de dias max de vacaciones al año 15 dias
				numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
				// Los dias maximo q puede acumular 45 dias por tres periodos
				p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
				//dias1=getDias360(tab_codigo_vacacion.getValor("FECHA_INGRESO_ASVAC"), utilitario.getFechaActual());
				//dias_extra=dias1/360;
				//if (dias_extra>=5) {
				//	dias_final_extra= (int)dias_extra;	
				//}else {
				//	dias_final_extra= 0;	
				//}
			}

			if (ide_gttem == 2) {
				// Asigno el numero de dias max de vacaciones al año 15 dias
				numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
				// Los dias maximo q puede acumular 45 dias por tres periodos
				p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));
				dias_final_extra= 0;
			}
			

			

			// Obtengo el total de mis dias pendientes al restar los dias
			// acumulados-dias tomados
			double sumatotalDiasGenerados = 0.0;
			// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
			// que le corresponde y los dias generados hasta el presente
			List<Double> vacacionesPeriodo = servicioVacaciones.getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
			// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
			// que le corresponde y los dias generados hasta el 30 de abril de
			// 2017
			List<Double> vacacionesPeriodoCalculoInicial = servicioVacaciones.getVacacionesXPeriodo(nda, ndeInicialFechaCalculoDiasTomados, peridosCalculoIncial, numeroDiasVacacionXAnio);

			// Asigno la sumatoria de total de mis dias acumulados a vacacion
			// desde la fecha de ingreso hasta la fecha de hoy
			//for (int i = 0; i < vacacionesPeriodo.size(); i++) {
			//	sumatotal_vacaciones = sumatotal_vacaciones + vacacionesPeriodo.get(i);
			//}

			//System.out.println("Suma vacaciones hasta Fecha Actual: " + sumatotal_vacaciones);

			// Asigno la sumatoria de total de mis dias acumulados a vacacion
			// desde la fecha de ingreso hasta el 30 de Abril de 2017
			for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
				sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial + vacacionesPeriodoCalculoInicial.get(i);
			}
			// Devuelve el numero de dias pendientes obtenidos de excel hasta el
			// 30 de abril
			numeroDiasTomadosInicial = 0.00;
			numeroDiasTomadosInicial = servicioVacaciones.getNumeroDiasPendientesInicial(utilitario.getVariable("IDE_GTEMP"),ide_asvac_continuidad);
			// System.out.println("numeroDiasTomados1: "+
			// numeroDiasTomadosInicial);

			// devuelve el numero de dias ajustados para el calculo inicial de
			// dias pendientes de cada empleado
			diasPendientesInicialAjuste = 0.00;
			diasPendientesInicialAjuste = servicioVacaciones.getNumeroDiasPendientesInicialAjuste(utilitario.getVariable("IDE_GTEMP"),ide_asvac_continuidad);
			// System.out.println("numeroDiasTomadosajuste: "+
			// diasPendientesInicialAjuste);
			totalNumeroDiasAjuste = 0.00;
			totalNumeroDiasAjuste = servicioVacaciones.getNumeroDiasAjusteEmpleado(utilitario.getVariable("IDE_GTEMP"),ide_asvac_continuidad);
			// System.out.println("numeroDXXXXXXiasTomados: " +
			// numeroDiasTomados);
			nroDiasAjustePeriodo = 0.00;
			nroDiasAjustePeriodo = servicioVacaciones.nroDiasAjustePeriodo(utilitario.getVariable("IDE_GTEMP"),ide_asvac_continuidad);

			if ((int)numeroDiasTomadosInicial != 0) {

				if ((int)numeroDiasTomadosInicial < 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
					// valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac=" + valor + " where ide_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));
					totalNumeroDiasAjuste = valor;

				}
				if ((int)numeroDiasTomadosInicial > 0) {
					double valor = 0.0;
					double valor1 = 0.0;
					valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
					// valor=valor1-sumatotal_vacaciones;
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac=" + valor + " where ide_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));
					totalNumeroDiasAjuste = valor;

				}

			}

			
			
			totalNumeroDiasTomadosInicial =0.00; 
			totalNumeroDiasTomadosInicial = servicioVacaciones.getNumeroDiasTomados(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString(),ide_asvac_continuidad);
			numeroDiasTomados = 0.00;
			numeroDiasTomados = totalNumeroDiasAjuste;
			// Guardo el valor de dias tomados para el calculo de diasGracia
			numeroDiasTomadosTemporal = 0.00;
			numeroDiasTomadosTemporal = numeroDiasTomados;

			// suma el valor de los dias descontados de la tabla
			// asi_detalle_vacacion
			// Aqui se encuentra el cuadre
			dias_sumados_aplicados_vacacion = 0.00; 
			dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
			int p_etn1 =0;
			p_etn1 =((int) dias_sumados_aplicados_vacacion / 5);
			double descuento =0.00; 
			descuento =dias_sumados_aplicados_vacacion + (p_etn1 * 2);

			double resultado_descuento =0.00;
			resultado_descuento =descuento + numeroDiasTomados;


				//Consulto los dias 
			if (tab_codigo_vacacion.getTotalFilas()==0) {
				utilitario.agregarMensajeInfo("No se puede continuar", "No contiene un periodo de vacaciones activo");
				return;
			}else {
				//numeroDiasTomadosFinSemana = servicioVacaciones.getNumeroDiasTomadosFinSemana(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString(),ide_asvac_continuidad);
				}

				

			/*if (ide_gttem == 2) {
				
				double nro_dias_ajuste_periodo_asvac = 0;
				//obtengo el ide de la tabla vacacion
				
				if ((sumatotal_vacaciones - (numeroDiasTomados + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana)) > 60) {
					//System.out.println("Ingreso a descuento mayor a 60");

					BigDecimal sumatotal_vacaciones1 = new BigDecimal(sumatotal_vacaciones);
					BigDecimal numeroDiasTomados1 = new BigDecimal(numeroDiasTomados);
					BigDecimal descuento1 = new BigDecimal(descuento);
					BigDecimal nroDiasAjustePeriodo1 = new BigDecimal(nroDiasAjustePeriodo);
					BigDecimal dias = new BigDecimal(60);
					BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
					BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

					if (calculoPasaDiasPendientes.doubleValue() >= 0.01) {
						TablaGenerica tab_codigo = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
						utilitario.getConexion().ejecutarSql(
								"insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)" + "values ( " + tab_codigo.getValor("codigo") + ","
										+ tab_codigo_vacacion.getValor("IDE_ASVAC") + ",'" + utilitario.getFechaActual() + "'," + (calculoPasaDiasPendientes) + ",'APLICACIï¿½N DESCUENTO POR EXCESO DE DIAS' ,true, false)");
					}
					nroDiasAjustePeriodo = calculoPasaDiasPendientes.doubleValue() + nroDiasAjustePeriodo;

				}
			}

			if (ide_gttem == 1) {

				/*double nro_dias_ajuste_periodo_asvac = 0;

				
				
				

				if (((sumatotal_vacaciones+dias_final_extra) - (numeroDiasTomados + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana)) > 45) {

					//System.out.println("Ingreso a descuento mayor a 45");

					BigDecimal sumatotal_vacaciones1 = new BigDecimal(sumatotal_vacaciones);
					BigDecimal numeroDiasTomados1 = new BigDecimal(numeroDiasTomados);
					BigDecimal descuento1 = new BigDecimal(descuento);
					BigDecimal nroDiasAjustePeriodo1 = new BigDecimal(nroDiasAjustePeriodo);
					BigDecimal dias = new BigDecimal(45);
					BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);
					BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));


					if (calculoPasaDiasPendientes.doubleValue() >= 0.01) {

						TablaGenerica tab_codigo = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
						utilitario.getConexion().ejecutarSql(
								"insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)" + "values ( " + tab_codigo.getValor("codigo") + ","
										+ tab_codigo_vacacion.getValor("IDE_ASVAC") + ",'" + utilitario.getFechaActual() + "'," + (calculoPasaDiasPendientes) + ",'APLICACIï¿½N DESCUENTO POR EXCESO DE DIAS' ,true, false)");
					}
					nroDiasAjustePeriodo = calculoPasaDiasPendientes.doubleValue() + nroDiasAjustePeriodo;

				}
			}*/
		
			numeroDiasTomadosFinSemana = 0.00;
			numeroDiasTomadosFinSemana = servicioVacaciones.getNumeroDiasTomadosFinSemana(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString(),ide_asvac_continuidad);
			numeroDiasTomadosTemp=0.00;
			numeroDiasTomadosTemp=numeroDiasTomados;
			numeroDiasTomados = Double.parseDouble(utilitario.getFormatoNumero((numeroDiasTomadosTemp + descuento + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana),2));

			//List<Double> vacacionesTomadas = servicioVacaciones.getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos, numeroDiasVacacionXAnio);

			/**
			 * Obtencion de dias pendientes del empleado
			 */
			

			TablaGenerica tabVacacionInicial=utilitario.consultar("select ide_asvac,ide_gtemp,fecha_ingreso_asvac,activo_asvac,fecha_finiquito_asvac  from asi_vacacion where ide_asvac="+ide_asvac);						
		    utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac="+ide_asvac);
			TablaGenerica tab_asi_vacacion_resumen=null;
			boolean banderaSalidaEmpleado=false;

			String parametro_dia_extra=utilitario.getVariable("p_asi_empleados_codigo_trabajo_dia_extra");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=parametro_dia_extra.split(",");
			boolean empleado_dia_extra_vacacion=false;
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				if(tabVacacionInicial.getValor("IDE_GTEMP").equals(listaAccionesEmpleado[i].toString())){
				empleado_dia_extra_vacacion=true;
				i=listaAccionesEmpleado.length;
				}else {
					empleado_dia_extra_vacacion=false;
				}
			}
			
			///dias_pendientes = servicioVacaciones.getNumeroDiasPendientes(nde, nda, numeroDiasTomados, numeroDiasVacacionXAnio);
			utilitario.getConexion().ejecutarSql("delete from asi_vacacion_resumen_empleado WHERE ide_asvac="+ide_asvac);
		    boolean fin_accion=false,fin_accion_continua=false;
			TablaGenerica acciones=retornaTablaAccionResumenVacacion(fecha_ingreso_continuidad,fecha_fin_continuidad,IDE_GTEMP);
			double dias_=0.0;
			dias_=numeroDiasTomados;
			double valor_retorno=0.0;
			int contador=0;
			for (int i = 0; i < acciones.getTotalFilas(); i++) {
				String fec_geedp="",fec_fin_geedp="";
				dias_=dias_-valor_retorno;
				if ((acciones.getTotalFilas()-1)==i) {
				if (acciones.getValor(i,"fecha_liquidacion_geedp")==null || acciones.getValor(i,"fecha_liquidacion_geedp").isEmpty()) {
					contador++;
					fec_geedp=acciones.getValor(i,"fecha_geedp");
					fec_fin_geedp=utilitario.getFechaActual();
					valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
					}else {
						contador++;
						fec_geedp=acciones.getValor(i,"fecha_geedp");
						fec_fin_geedp=acciones.getValor(i,"fecha_liquidacion_geedp");	
						valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
					}
				}else {
				fec_geedp=acciones.getValor(i,"fecha_geedp");
					if (acciones.getValor((i+1),"fecha_liquidacion_geedp")==null || acciones.getValor((i+1),"fecha_liquidacion_geedp").isEmpty()) {
						fec_geedp=acciones.getValor(i,"fecha_geedp");
						while(fec_geedp.equals(acciones.getValor((i+1),"fecha_geedp"))){
							i++;
							if (((i+1)==acciones.getTotalFilas())) {
								if(acciones.getValor((i),"fecha_liquidacion_geedp")==null || acciones.getValor((i),"fecha_liquidacion_geedp").isEmpty()){
									fin_accion=true;
									fin_accion_continua=true;
								}else{
									fin_accion=false;	
									fin_accion_continua=true;
								}
								break;
							}
						}
						contador++;
						if (fin_accion) {
						fec_fin_geedp=utilitario.getFechaActual();
						}else if (fin_accion==false && fin_accion_continua==true){
						fec_fin_geedp=acciones.getValor((i),"fecha_liquidacion_geedp");
						}else{
						fec_fin_geedp=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(acciones.getValor((i+1),"fecha_geedp")),-1));
						}
						valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
						}else {
							contador++;
							fec_geedp=acciones.getValor(i,"fecha_geedp");
							fec_fin_geedp=acciones.getValor((i+1),"fecha_liquidacion_geedp");
							valor_retorno=getDatosEmpleado1(IDE_GTEMP, false, ide_asvac_continuidad, fec_geedp, fec_fin_geedp, acciones.getValor(i,"ide_geedp"), dias_, contador,ide_asvac);
							i=i+1;
						}
				}
			}
			
			
			
			//asdsdasdsd
			
			
//			Integer dimension = vacacionesPeriodo.size();
			//Variables para el calculo de dias extra
			 total_dias_extra=0.00;
			 dias_extra_temp=0.00;
			 valor_entero_dias_extra=0;
			 valor_total_dias_extra=0;
			dias_pendientes_resumen=0.0;
			dias_acumulados=0.00;
			try {
				tab_asi_vacacion_resumen=utilitario.consultar(//"select IDE_ASVRE,IDE_ASVAC from asi_vacacion_resumen_empleado where activo_asvre=true"
						"select ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
						+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre, "
						+ "ide_periodo_asvre, usuario_ingre, fecha_ingre, usuario_actua, "                                        
						+ "fecha_actua, hora_ingre, hora_actua, base_imponible_asvre, dias_trabajados_liquidacion_asvre, "
						+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac, "
						+ "ide_geedp_asvac,dia_extra_asvre "
						+ "from asi_vacacion_resumen_empleado "
						+ "where activo_asvre=true"
						+ " and ide_asvac="+ide_asvac+" order by ide_asvre asc");
				if (tabVacacionInicial.getValor("ACTIVO_ASVAC").equals("true")) {
                       banderaSalidaEmpleado=false;
				}else{
					banderaSalidaEmpleado=true;
			}
			} catch (Exception e1) {
				System.out.println("ERROR CONSULTA RESUMEN VACACION EMPLEADO");
			}

			tab_asi_vacacion_resumen=utilitario.consultar("select IDE_ASVRE,IDE_ASVAC "
														  + "from asi_vacacion_resumen_empleado "
														  + "where activo_asvre is null"
														  + " and ide_asvac="+ide_asvac);
			
			if (tab_asi_vacacion_resumen.getTotalFilas()>0) {
				
			utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado SET "
												+ "activo_asvre=true where "
												+ "ide_asvac="+ide_asvac);
												}
		
			
			/**
			 * Obtencion de dias pendientes del empleado
			 */
			// System.out.println("numero dias pendientes vacacion: "+
			// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
			
	
				double dias=0.0,vacaciones_extra=0.0;
				dias_extra=0.0;
			
				dias_acumulados=0.00;
				dias_acumulados_temp=0.00;
								
								
			TablaGenerica  tab_resumen=null;
			 try {
			tab_resumen=utilitario.consultar("select a.nro_dias_vacacion_asvre,a.nro_dias_pendientes_asvre,a.nro_dias_vacacion_asvre  "
					+ "from  "
					+ "(select ide_asvac, sum(nro_dias_vacacion_asvre) as nro_dias_vacacion_asvre,sum(nro_dias_pendientes_asvre) as nro_dias_pendientes_asvre  "
					+ "from asi_vacacion_resumen_empleado  "
					+ "where activo_asvre=true  "
					+ "and ide_asvac="+ide_asvac+"   "
					+ "group  by ide_asvac) a");
			
			if (tab_resumen.getValor("nro_dias_vacacion_asvre")==null || tab_resumen.getValor("nro_dias_vacacion_asvre").isEmpty() || tab_resumen.getValor("nro_dias_vacacion_asvre").equals("")) {
				dias_acumulados_temp=0.00;
				dias_acumulados=0.00;
			}else {
				dias_acumulados_temp=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"));
				dias_acumulados=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"));
			}
			
			
			
		

			} catch (NumberFormatException e1) {
			System.out.println("ERROR EN VACACIONES");
			}
			
			
			if (ide_gttem==1) {
				//VALIDACION DIAS EXTRA CODIGO DE TRABAJO
				dias_extra_temp=(dias_acumulados/15);
				valor_entero_dias_extra=dias_extra_temp.intValue();
				//Integer.parseInt(""+dias_extra_temp)
				if(valor_entero_dias_extra>=5){
					valor_total_dias_extra=(valor_entero_dias_extra-5)+1;
				//}
				if ((int)valor_total_dias_extra>0) {
					TablaGenerica tab_resumen_vacaciones=utilitario.consultar("SELECT ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
							+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre,  "
							+ "ide_periodo_asvre, "
							+ "base_imponible_asvre, dias_trabajados_liquidacion_asvre,  "
							+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac,  "
							+ "ide_geedp_asvac  "
							+ "FROM asi_vacacion_resumen_empleado "
							+ "where activo_asvre=true "  
							+ "and ide_asvac="+ide_asvac+" "
							+ "order by ide_asvre asc");
					double sumatoria=0.00;
					int valor_acumulado=0,valor_actualizar_acumulado=0,valor_obtenido=0,valor_actualizar_acumulado_anterior=0;
			
					 
					for (int i = 0; i < tab_resumen_vacaciones.getTotalFilas(); i++) {
						sumatoria=Double.parseDouble(tab_resumen_vacaciones.getValor(i,"nro_dias_vacacion_asvre"))+sumatoria;
						valor_acumulado=(int)sumatoria/15;
						valor_obtenido=(valor_acumulado-5)+1-valor_obtenido;
						if (valor_obtenido>0) {
							if (i==0) {
								valor_actualizar_acumulado=valor_obtenido;
								utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre="+valor_obtenido+"  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));
							}else{
								valor_actualizar_acumulado=valor_obtenido-valor_actualizar_acumulado;
									//Actualizo valor generado
									//double valor_actualizar=0.00;
									utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre="+valor_obtenido+"  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));
									valor_actualizar_acumulado=valor_obtenido;
							}
						}else {
							valor_actualizar_acumulado=0;
							valor_obtenido=0;
							utilitario.getConexion().ejecutarSql("update asi_vacacion_resumen_empleado set dia_extra_asvre=0.00  where ide_asvre=" + tab_resumen_vacaciones.getValor(i,"ide_asvre"));

						}
				}
					}
				
				}				
				}
		
					
			
		 	 if (((int)valor_total_dias_extra)>=1) {
				TablaGenerica tab_resumen_=utilitario.consultar("select a.ide_asvac,a.dia_extra_asvre "
						+ "from  "
						+ "(select ide_asvac, sum(dia_extra_asvre) as dia_extra_asvre  "
						+ "from asi_vacacion_resumen_empleado  "
						+ "where activo_asvre=true  "
						+ "and ide_asvac="+ide_asvac+"   "
						+ "group  by ide_asvac) a");
				
				dias_final_extra=Integer.parseInt(tab_resumen_.getValor("dia_extra_asvre"));
				banderaCodigo="true";
				ver_dias_extra="block";
				
			}else {
				banderaCodigo="false";
				ver_dias_extra="none";
				dias_final_extra=0;

			}


			dias_acumulados=0.00;
			dias_acumulados=dias_acumulados_temp+dias_final_extra;
			dias_pendientes_resumen=(Math.rint((dias_acumulados-numeroDiasTomados) * 100) / 100);
			int numeroInicioFinesSemanaResumen = (int) (((dias_acumulados*4)) / 30);						
			//numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);
			int numeroInicioFinesSemanaPendientesResumen =0;
			numeroInicioFinesSemanaSolicitados=0;
			numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);

			numeroInicioFinesSemanaPendientesResumen = numeroInicioFinesSemanaResumen - numeroInicioFinesSemanaSolicitados;
			
			Double valor=0.00;
			
			//numeroInicioFinesSemanaSolicitados = numeroInicioFinesSemanaResumen;
			numeroInicioFinesSemanaPendientes = numeroInicioFinesSemanaPendientesResumen;
			sumatotal_vacaciones=dias_acumulados;
			numeroInicioFinesSemana=numeroInicioFinesSemanaResumen;
			//double nro_fines_semana = (calculavalordiaspendientes);
			sumatotal_vacaciones = Double.parseDouble(utilitario.getFormatoNumero(dias_acumulados,2)); 

			//sumatotal_vacaciones = Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2)); 
			
			dias_pendientes=0.00;
			dias_pendientes=dias_pendientes_resumen;
			
					
			
			if (ide_gttem == 2) {
				double nro_dias_ajuste_periodo_asvac = 0;						
						if (dias_pendientes_resumen > 60 || dias_pendientes_resumen > 60.00 ) {
							Double calculoPasaDiasPendientes = 0.00;
							calculoPasaDiasPendientes=Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen,3))-60.00;
							System.out.println("dias_pendientes resumen "+calculoPasaDiasPendientes);
							if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0) {
				TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
			  //  utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
			   // +"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false,true)");
				//		utilitario.agregarMensaje("Usted a superado el limite maximo de dias acumulados", "Se ha cargado un permiso por exceso de dias");
						//return;
							}
			    		}
					}
			
			if (ide_gttem==1) {
						double nro_dias_ajuste_periodo_asvac = 0;
						if ((dias_pendientes_resumen)> 45 || (dias_pendientes_resumen) > 45.00 ) {
							Double calculoPasaDiasPendientes = 0.00;
									calculoPasaDiasPendientes =	Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen,3))-45.00;
							if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0) {
							TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
					//		utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
					//		+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false, true)");
					//		utilitario.agregarMensaje("Usted a superado el limite maximo de dias acumulados", "Se ha cargado un permiso por exceso de dias");
							//return;
							}
						}
			}
					
			
	//matriz = servicioVacaciones.getVacacionesMatrizExtra(vacacionesPeriodo, vacacionesTomadas,dias_final_extra);

	matriz = servicioVacaciones.getVacacionesMatrizExtra(ide_asvac);

			
			
			//lisVacacionesEmpleados = servicioVacaciones.getNumeroDiasTomadosEmpleados();
			//dias_pendientes = servicioVacaciones.getNumeroDiasPendientes(nde, nda, numeroDiasTomados, numeroDiasVacacionXAnio);
			// redondeo el valor
			//dias_pendientes = (sumatotal_vacaciones - numeroDiasTomados);
			//dias_pendientes = Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes,2)); 
					//Math.rint(dias_pendientes * 100) / 100;
			
			/*
			 * Tomo el parametro de empleados que contienen dias extra a vacaciones y verifica si se quiere que se despliegue el empleado
			 */
			
	
			
			
			//if (ide_gttem==1 && empleado_dia_extra_vacacion==true) {
				//dias_pendientes=dias_pendientes+dias_final_extra;
			//	ver_dias_extra="block";
			//}else {
			//	ver_dias_extra="none";
			//	dias_final_extra=0;
			//}


			//double calculavalordiaspendientes = dias_pendientes / 7;
			
			//if (ide_gttem==1) {
			//	numeroInicioFinesSemana = (int) (sumatotal_vacaciones+dias_final_extra) / 7;
			//}else {
			//numeroInicioFinesSemana = (int) sumatotal_vacaciones / 7;
		//	}
					
		
					//Math.rint(sumatotal_vacaciones * 100) / 100;

		//	 String string = ""+(dias_pendientes_resumen+ (int)valor_total_dias_extra);
		   String string = ""+dias_pendientes_resumen;
			String[] part = string.split(Pattern.quote ("."));
			String part1 = part[0]; // 123
			String part2 = part[1]; // 654321

			double valorMaxTomaVacaciones=((Double.parseDouble("0."+part2)*8));
			
			String string_min = ""+valorMaxTomaVacaciones;
			String[] partmin = string.split(Pattern.quote ("."));
			String partMin1 = partmin[0]; // 123
			String partMin2 = partmin[1]; // 654321
			double minvalorMaxTomaVacaciones=((Double.parseDouble("0."+partMin2)*60));
			str_dias_pendientes=part1+" Dias "+(int)valorMaxTomaVacaciones+" Horas "+(int)minvalorMaxTomaVacaciones+" Minutos";
			
			
			/**
			 * Actualizo la base de datos
			 * 
			 */

			
			

			try {
				TablaGenerica tab_vacacion = utilitario.consultar("select ide_asvac,dias_pendientes_asvac from asi_vacacion where ide_gtemp=" + utilitario.getVariable("IDE_GTEMP"));
				if (tab_vacacion.getValor("dias_pendientes_asvac") == null || tab_vacacion.getValor("dias_pendientes_asvac").isEmpty()) {
					anterior_dias_pendientes = 0.0;
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_pendientes_asvac=" + dias_pendientes + " where ide_gtemp=" + utilitario.getVariable("IDE_GTEMP"));
				} else {
					utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_pendientes_asvac=" + dias_pendientes + " where ide_gtemp=" + utilitario.getVariable("IDE_GTEMP"));
				}

			} catch (NumberFormatException e) {
					System.out.println("Error en obtencion de dias pendientes "+e.getMessage());
			}
		}
String ide_marcacion="";
int	validaIngreso=0,validarIngresoMarcacion=0;
		try {
			//Obtengo el id de marcacion  del empleado
			TablaGenerica tab_id_marcacion=utilitario.consultar("SELECT emp.ide_gtemp,emp.documento_identidad_gtemp, "
					+ "emp.tarjeta_marcacion_gtemp, emp.activo_gtemp  "
					+ "FROM gth_empleado  emp "
					//+ "LEFT JOIN asi_jefe_inmediato jefe on jefe.ide_gtemp=emp.ide_gtemp  "
					//+ "where jefe.tipo_asjei=4 and emp.ide_gtemp="+empleado.getIdeGtemp());
					+ "where emp.ide_gtemp="+empleado.getIdeGtemp());
			
			
			if (tab_id_marcacion.getTotalFilas()>0) {//Si el empleado tiene asignado un codigo de marcacion
				
			if(tab_id_marcacion.getValor("tarjeta_marcacion_gtemp")==null || tab_id_marcacion.getValor("tarjeta_marcacion_gtemp").equals("") || tab_id_marcacion.getValor("tarjeta_marcacion_gtemp").isEmpty()){			
					//Si contengo registro pero no tiene asignado codigo de marcacion 
					utilitario.agregarMensajeInfo("No tiene asignado un codigo de marcacion", "Favor contactarse con el Administrador");
					estadoMarcacion=true;
					return;	}
			ide_marcacion=tab_id_marcacion.getValor("tarjeta_marcacion_gtemp");
			//Validacion marcaciSon pasasdo los 15 minutos
			validaIngreso=validaIngresoMarcacion(ide_marcacion);
			validarIngresoMarcacion=0;
			if (validaIngreso==0) {//puede ingresar otro permiso y habilito el registro de marcacion
			estadoMarcacion=false;
			}else if (validaIngreso==1){//Han pasado mas de 15 minutos desde la ultima marcacion registrada
			estadoMarcacion=false;
			}else if (validaIngreso==2) {//puede ingresar otro permiso
			estadoMarcacion=true;
			utilitario.agregarMensajeInfo("El boton para realizar una nueva marcacion se habilitara en un lapso de 15 a 20 min", "Usted ya ha registrado marcacion");
			return;				
			//utilitario.getConexion().ejecutarSql("update con_biometrico_marcaciones set activo_cobim=false where ide_cobim=" + validarIngresoMarcacion);						
				}
			}else{//Si el empleado no tiene id de marcacion
			//utilitario.agregarMensajeInfo("No tiene permisos", "Favor contactarse con Talento Humano");
			estadoMarcacion=true;
			return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error en validacion de marcaciones");
		}
		

		if (objDiasVacacionesActiva == null) {
			objDiasVacacionesActiva = new Object[6];
		}
		if (lisResumenVacaciones == null) {
			lisResumenVacaciones = new ArrayList();
		}
		strPathReporte = utilitario.getURL() + "/reportes/reporte_" + utilitario.getVariable("IDE_USUA") + ".pdf";
	}



	public boolean estadoJefeSolicitud() {

		boolean estadoSolicitudJefe = solicitudVacaciones.getAprobadoAspvh().booleanValue();

		if (estadoSolicitudJefe == true) {
			return true;
		} else {
			return false;
		}
	}


	public void downloadFile(byte[] fileBytes, String nameFile) throws IOException {

		// String filePath = "C://"+nameFile+".pdf";

		// if (escribirArchivo(fileBytes, filePath)){

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		String filePath = extContext.getRealPath("/upload/permisos");
		File file = new File(filePath);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.reset();
		response.setBufferSize(1024);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Length", String.valueOf(file.length()));
		// response.setHeader("Content-Disposition", "attachment;filename=\""
		// + file.getName() + "\"");
		response.setHeader("Content-Disposition", "inline;filename=\"" + nameFile + ".pdf\"");

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			input = new BufferedInputStream(new FileInputStream(file), 1024);
			output = new BufferedOutputStream(response.getOutputStream(), 1024);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} finally {
			input.close();
			output.close();
		}
		context.responseComplete();
		// }
	}

	public void seleccionaMotivo(AjaxBehaviorEvent evt) {

		this.solicitudVacaciones.getIdeAsmot();
		//System.out.println("si ingresa");

		// Obtengo el codigo del permiso solicitado
		final Integer codigoMotivo = this.solicitudVacaciones.getIdeAsmot().getIdeAsmot();

		/**
		 * declaro una variable de tipo objeto la cual devuelve un valor de tipo
		 * boolean crea un predicado el cual busca dentro de mi lista el codigo
		 * de mi motivo si la encuentra retorna true caso contrario false
		 */
		Object obj = CollectionUtils.find(this.listaMotivos, new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				// Declaro lista de objetos
				Object[] obj = (Object[]) arg0;
				if (obj[0].equals(codigoMotivo)) {
					return true;
				}
				return false;
			}
		});

		Object[] motivoSel = (Object[]) obj;
		descripcionMotivo = (String) motivoSel[3];
		motivo = (String) motivoSel[1];
		aplicavacacion = (Boolean) motivoSel[2];
		esvacacion = (Boolean) motivoSel[5];
		numMaxRegistroPermiso=(Integer) motivoSel[7];
		
		solicitudVacaciones.setFechaDesdeAspvh(null);
		solicitudVacaciones.setFechaHastaAspvh(null);
		solicitudVacaciones.setNroDiasAspvh(null);
		solicitudVacaciones.setHoraDesdeAspvh(null);
		solicitudVacaciones.setHoraHastaAspvh(null);
		solicitudVacaciones.setNroHorasAspvh(null);
		solicitudVacaciones.setNroDiasAspvh(null);
		if (codigoMotivo==Integer.parseInt(utilitario.getVariable("p_licencia_maternidad"))) {
			enabledDias=true;
			enabledDiasHoras=false;
			enabledHasta=false;
		}else{
			enabledDias=true;
			enabledDiasHoras=true;
			enabledHasta=true;
		}
		
	}

	public boolean puedeHacerSolicitud(Integer ide_gtemp, Integer ide_geedp) {
		try {
			TablaGenerica resultadoEstado = utilitario.consultar("select ide_aspvh,ide_gtemp from asi_permisos_vacacion_hext where  "
					+ "(aprobado_tthh_aspvh=false or aprobado_tthh_aspvh is null) and (anulado_aspvh=false or anulado_aspvh is null) and ide_gtemp=" + ide_gtemp + " and ide_geedp=" + ide_geedp);

//Vallidacion de si tiene solicitudes pendientes			

		/*	if (resultadoEstado != null && resultadoEstado.getFilas() != null && resultadoEstado.getFilas().size() == 0) {
				// si puede hacer solicitud
				return true;
			} else {
				// no puede hacer solicitud
				return false;
			}*/
			
			return true;
			
		} catch (Exception e) {
			System.out.println("Error al evaluar puede hacer solicitud " + e);
		}
		return false;

	}

	public void calcularDias(DateSelectEvent evt) {
		if (!enabledDias) {
			solicitudVacaciones.setFechaHastaAspvh(solicitudVacaciones.getFechaDesdeAspvh());
			solicitudVacaciones.setNroHorasAspvh(null);
		}else if (!enabledDiasHoras) {
			solicitudVacaciones.setFechaHastaAspvh(utilitario.sumarDiasFecha(solicitudVacaciones.getFechaDesdeAspvh(), 84));
			solicitudVacaciones.setNroDiasAspvh(84);
			double valor=672.00;
			
			solicitudVacaciones.setNroHorasAspvh(new BigDecimal(valor));

		}else{
		calcularDias();}
	}

	public void calcularDias(AjaxBehaviorEvent evt) {
		if (!enabledDias) {
			solicitudVacaciones.setFechaHastaAspvh(solicitudVacaciones.getFechaDesdeAspvh());
			solicitudVacaciones.setNroHorasAspvh(null);
			solicitudVacaciones.setNroDiasAspvh(1);

		}else if (!enabledDiasHoras) {
			solicitudVacaciones.setFechaHastaAspvh(utilitario.sumarDiasFecha(solicitudVacaciones.getFechaDesdeAspvh(), 84));
			solicitudVacaciones.setNroDiasAspvh(84);
			double valor=672.00;
			solicitudVacaciones.setNroHorasAspvh(new BigDecimal(valor));

		}else{

		calcularDias();}
	}

	public void calcularHoras(AjaxBehaviorEvent evt) throws IOException {
		
		try {
			if(solicitudVacaciones.getHoraDesdeAspvh().toString()!=null && !solicitudVacaciones.getHoraDesdeAspvh().toString().isEmpty()
					&& solicitudVacaciones.getHoraHastaAspvh().toString()!=null && !solicitudVacaciones.getHoraHastaAspvh().toString().isEmpty()){
				calcularHorasPermisos(horaInical(), horaFinal());
			}
		} catch (Exception e) {
			utilitario.agregarMensajeInfo("Debe Seleccionar Hora Desde y Hora Hasta", "");
			
		}	
		
		
	}

	public void calcularHoras(DateSelectEvent evt) {
		
		try {
				calcularHorasPermisos(horaInical(),horaFinal());
		} catch (Exception e) {
			System.out.println("Error seleccion de hora "+e.getMessage());
			
		}
	}

	private void calcularDias() {
		if (solicitudVacaciones.getFechaDesdeAspvh() == null || solicitudVacaciones.getFechaHastaAspvh() == null) {
			return;
		}

		if (utilitario.isFechasValidas(utilitario.getFormatoFecha(solicitudVacaciones.getFechaDesdeAspvh()), utilitario.getFormatoFecha(solicitudVacaciones.getFechaHastaAspvh()))) {
			
			if (enabledDias) {
			solicitudVacaciones.setNroDiasAspvh(new Integer((utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), solicitudVacaciones.getFechaHastaAspvh()) + 1) + ""));
			}
			double dou_solicita = pckUtilidades.CConversion.CDbl_2(solicitudVacaciones.getNroDiasAspvh()) * 8;
			//System.out.println("tranquilo: " + dou_solicita);
			solicitudVacaciones.setNroHorasAspvh(new BigDecimal(dou_solicita));

		} else {
			solicitudVacaciones.setNroDiasAspvh(null);
			utilitario.agregarMensajeInfo("Fechas no válidas", "La Fecha Hasta debe ser mayor o igual a la Fecha Desde");
			solicitudVacaciones.setFechaHastaAspvh(null);
		}
	}

	public void calcularHorasPermisos(String str_hora_inicial , String str_hora_final) throws IOException{
		try {

			
			Date hora_inicial = utilitario.getHora(utilitario.getFormatoHora(str_hora_inicial));
			Date hora_final = utilitario.getHora(utilitario.getFormatoHora(str_hora_final));

				int total_segundos_hora_inicial = (hora_inicial.getHours() * 3600) + (hora_inicial.getMinutes() * 60) + hora_inicial.getSeconds();
				int total_segundos_hora_final = (hora_final.getHours() * 3600) + (hora_final.getMinutes() * 60) + hora_final.getSeconds();


			Date fecha_actual=utilitario.getDate();
			String fecha=utilitario.DeDateAString(fecha_actual);

			String fecha_inicial=fecha+" "+str_hora_inicial;
		    String fecha_final_1=utilitario.DeDateAString(utilitario.sumarDiasFecha(fecha_actual, 1));
			String fecha_final=fecha_final_1+" "+str_hora_final;

			String hora1=""+hora_inicial;
			String hora2=""+hora_final;

			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			
		        Date fechaInicial=dateFormat.parse(fecha_inicial);
		        Date fechaFinal=dateFormat.parse(fecha_final);
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
		        //System.out.println("Hay "+dias+" dias, "+horas+" horas, "+minutos+" minutos y "+diferencia+" segundos de diferencia");
		        
		         
		    	if ((dias==0)&&(horas==24)&&(minutos==0)&&(diferencia==0)) {
					solicitudVacaciones.setHoraHastaAspvh(null);
					solicitudVacaciones.setNroHorasAspvh(null);
				return;
		    	} 

		    	
		    	double total_diferencia_segundos = ((horas * 3600) + (minutos * 60) + diferencia);
				double total_diferencia_horas = total_diferencia_segundos / 3600;
			
				if (total_diferencia_horas > 8){
					solicitudVacaciones.setNroHorasAspvh(null);
					solicitudVacaciones.setHoraHastaAspvh(null);
					utilitario.agregarMensajeInfo("Selección de horas incorrectas", "Rango de horas entre 0.1 y 8 hora(s)");
				    }else {
			    	solicitudVacaciones.setNroHorasAspvh(new BigDecimal(total_diferencia_horas).setScale(3, BigDecimal.ROUND_HALF_UP));
			}
				
				hora_inicial_reporte = str_hora_inicial;
				hora_final_reporte = str_hora_final;

		} catch (Exception e) {
			solicitudVacaciones.setNroHorasAspvh(null);
			utilitario.agregarMensajeInfo("Horas no válidas", "No se pudo calcular el número de horas " + e.getMessage());

		}

	}

	/**
	 * 
	 * 
	 * @return
	 */
	// private boolean validarTiempoPermitidoPermiso(){
	//
	//
	// if (enabledDias){
	// //aplica_dias
	// if (solicitudVacaciones.getNroDiasAspvh() > numMaximoDiasPermiso ){
	// return false;
	// }
	// }else{
	// //aplica_horas
	// if (solicitudVacaciones.getNroHorasAspvh().intValue() >
	// numMaximoDiasPermiso ){
	// return false;
	// }
	//
	// }
	//
	// return true;
	//
	// }

	/**
	 * 
	 * Devuelve los permisos de tipo  permisos personales y de vacaciones
	 * a 3 respectivamente
	 * 
	 * @return
	 */

	public int validarTiempoPermitidoPermiso() {
		if ((aplicavacacion == true) && (esvacacion == true)) {
			// aplica_dias
			return 1;
		}
		if ((aplicavacacion == true) && (esvacacion == false)) {
			// aplica_horas
			return 2;
		}
		return 0;
	}

	public void cambiaDiasHoras(AjaxBehaviorEvent evt) {
		if (!enabledDias) {
			solicitudVacaciones.setFechaHastaAspvh(solicitudVacaciones.getFechaDesdeAspvh());
			solicitudVacaciones.setNroDiasAspvh(null);
			solicitudVacaciones.setNroHorasAspvh(null);
			enabledHasta=false;
		} else {
			solicitudVacaciones.setFechaDesdeAspvh(null);
			solicitudVacaciones.setFechaHastaAspvh(null);
			solicitudVacaciones.setNroDiasAspvh(null);
			solicitudVacaciones.setHoraDesdeAspvh(null);
			solicitudVacaciones.setHoraHastaAspvh(null);
			solicitudVacaciones.setNroHorasAspvh(null);
			 enabledHasta=true;
		}
	}

	public void guardarSolicitud(ActionEvent evt) {

		puedeGuardarSolicitud = true;

		boolean puedeHacerSolicitud = puedeHacerSolicitud(solicitudVacaciones.getIdeGtemp().getIdeGtemp(), solicitudVacaciones.getIdeGeedp().getIdeGeedp());

		if (!puedeHacerSolicitud) {
			utilitario.agregarMensajeError("Solicitud no válida", "No puede realizar otra solicitud, tiene una solicitud pendiente en curso.");
			return;
		}

		
		//Validación jefe inmediato seleccionado no puede ser igual al empleado
		if (solicitudVacaciones.getGenIdeGeedp2().toString().equals(solicitudVacaciones.getIdeGeedp().toString())) {
			utilitario.agregarMensajeError("Solicitud inválida", "Error al escoger jefe inmediato, Escoja nuevamente");
			return;
		}

		/*
		 * Validacion permisos tipo personal <= 3 dias y solicitud a vacaciones
		 * >= 3dias
		 */
		boolean validarDiasIngresoPermiso=false;
		//validarDiasIngresoPermiso=validaDiasIngresoPermiso(solicitudVacaciones.getFechaDesdeAspvh());
		if (validarDiasIngresoPermiso == false) {
			//utilitario.agregarMensajeError("No se puede ingresar el permiso fecha solicitud invï¿½lida", "Fecha desde supera 3 dias hï¿½biles a la fecha solicitud");
			//return;
		}
		
		//Cambio de permiso a personal se si pasa de la fecha autorizada
	/*	if (validarDiasIngresoPermiso == true  || solicitudVacaciones.getIdeAsmot().getIdeAsmot()==14 || solicitudVacaciones.getIdeAsmot().getIdeAsmot()==3 || ide_gttem==1) {
			banderaPermisoExcedido=false;
		}else {
			solicitudVacaciones.setIdeAsmot(new AsiMotivo(14));
			banderaPermisoExcedido=true;

		}*/

		

		if (validarTiempoPermitidoPermiso() == 1) {
			//System.out.println("solicitudVacaciones.getNroDiasAspvh()" + solicitudVacaciones.getNroDiasAspvh());
			
			if (solicitudVacaciones.getNroDiasAspvh()==null || solicitudVacaciones.getNroDiasAspvh().toString().isEmpty()) {
				utilitario.agregarMensajeError("Solicitud de Vacaciones no válida", "Debe escoger un rango mayor a 4 días");
				return;
			}
			
			//Validadción solicitud de vacaciones mayor a tres dias 
			if (solicitudVacaciones.getNroDiasAspvh().intValue() <= 3) {
				//System.out.println("Resultado2");
				utilitario.agregarMensajeError("Solicitud de Vacaciones no válida", "Debe escoger un rango mayor a 4 días");
				return;
			}

			// Validación den los dias para la planificación de solicitud de vacaciones
			//Se quito restricccion de acuerdo a ticket 585071
	//		long diferenciaEn_ms = solicitudVacaciones.getFechaSolicitudAspvh().getTime() - solicitudVacaciones.getFechaDesdeAspvh().getTime();
	//		long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
	//		if (((int) dias > -7)) {
		//		utilitario.agregarMensajeInfo("Solicitud de vacaciones invalida", "Su solictud debe ser planificada con 8 dias de anticipacion");
//				return;
//			} else {

	//		}
		}

		
		/*
		 * Validacion por tipo de solicitud Tipo Permiso 1:Horas,Tipo Permiso
		 * 2:vacaciones y Tipo Permiso 4:dias
		 */

		if (esvacacion) {
			solicitudVacaciones.setTipoAspvh(4);
		} else {
			if (enabledDias) {
				solicitudVacaciones.setTipoAspvh(4);
			} else {
				solicitudVacaciones.setTipoAspvh(1);

			}
		}


		
		int codigoMotivo=0;
		boolean band=false;
		codigoMotivo=solicitudVacaciones.getIdeAsmot().getIdeAsmot();
		if (numMaxRegistroPermiso==null) {
		}else{
		String hora=null;
		if (solicitudVacaciones.getTipoAspvh().intValue()==4) {
			band=validaDiasIngresoPermiso(solicitudVacaciones.getFechaDesdeAspvh(), numMaxRegistroPermiso,codigoMotivo,"00:00:00");

		}else {
			if(solicitudVacaciones.getTipoAspvh().intValue()==1){
				String[] parts = getFechaAsyyyyMMddHHmmss(solicitudVacaciones.getHoraDesdeAspvh()).split(" ");
				String part1 = parts[0]; // 123
				String part2 = parts[1]; // 65432
				band=validaDiasIngresoPermiso(solicitudVacaciones.getFechaDesdeAspvh(), numMaxRegistroPermiso,codigoMotivo,part2);

			}
		}
		//Consulta Medica
		}
//
		if (band==true) {
			//utilitario.agregarMensajeInfo("Permiso Registrado", "Se ha registrado correctamente");

		}else {
				utilitario.agregarMensajeInfo("Fechas de registro invalida", "Recuerde que debe cumplir con las restriccionesperdir el permiso ");	
			
			
		return;
		
		
		}
		//numMaxRegistroPermiso

		
		
		
		
		
		
		
		

		if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==14) {
			int valorFinal=0;
			valorFinal=utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), utilitario.getDate());
			if (valorFinal<Integer.parseInt(utilitario.getVariable("p_dias_ingreso_permiso_personal"))) {
			}else {
			
				utilitario.agregarMensajeError("No se puede registrar el permiso", "Pongase en contacto con TTHH");
				solicitudVacaciones.setFechaHastaAspvh(null);
				solicitudVacaciones.setFechaDesdeAspvh(null);
				solicitudVacaciones.setNroHorasAspvh(null);
				solicitudVacaciones.setNroDiasAspvh(null);
				solicitudVacaciones.setDetalleAspvh(null);
				solicitudVacaciones.setHoraDesdeAspvh(null);
				solicitudVacaciones.setHoraHastaAspvh(null);
				return;

		/*	if (utilitario.DeDateAString(solicitudVacaciones.getFechaDesdeAspvh()).compareTo(utilitario.DeDateAString(utilitario.getDate()))<0) {
				utilitario.agregarMensajeError("No se puede registrar el permiso", "Pongase en contacto con TTHH");
				solicitudVacaciones.setFechaHastaAspvh(null);
				solicitudVacaciones.setFechaDesdeAspvh(null);
				solicitudVacaciones.setNroHorasAspvh(null);
				solicitudVacaciones.setNroDiasAspvh(null);
				solicitudVacaciones.setDetalleAspvh(null);


				return;
			}else {
				if (solicitudVacaciones.getTipoAspvh().toString().equals("1")) {
					String horaDesdeTemp="",minDesdeTemp="",segDesdeTemp="";
					if (solicitudVacaciones.getHoraDesdeAspvh().getHours()<10) {
						horaDesdeTemp="0"+solicitudVacaciones.getHoraDesdeAspvh().getHours();
					}else {
						horaDesdeTemp=""+solicitudVacaciones.getHoraDesdeAspvh().getHours();
					}
    				
    				
    				if (solicitudVacaciones.getHoraDesdeAspvh().getMinutes()<10) {
						minDesdeTemp="0"+solicitudVacaciones.getHoraDesdeAspvh().getMinutes();
					}else {
						minDesdeTemp=""+solicitudVacaciones.getHoraDesdeAspvh().getMinutes();
					}
    				
    				
    				if (solicitudVacaciones.getHoraDesdeAspvh().getSeconds()<10) {
    					segDesdeTemp="0"+solicitudVacaciones.getHoraDesdeAspvh().getSeconds();
					}else {
						segDesdeTemp=""+solicitudVacaciones.getHoraDesdeAspvh().getSeconds();
					}
    				
    				
    				String fechaHoraTimbreEntradaTemp =utilitario.getFechaActual()+" "+horaDesdeTemp+":"+minDesdeTemp+":"+segDesdeTemp;   				
    				String horaTimbreEntradaTemp=sumarRestarMinutos(getFechaAsyyyyMMddHHmm(fechaHoraTimbreEntradaTemp), 5);
					if(horaTimbreEntradaTemp.compareTo(utilitario.getHoraActual())<0){
						utilitario.agregarMensajeError("No se puede registrar el permiso", "Pongase en contacto con TTHH");
						solicitudVacaciones.setFechaHastaAspvh(null);
						solicitudVacaciones.setFechaDesdeAspvh(null);
						solicitudVacaciones.setHoraDesdeAspvh(null);
						solicitudVacaciones.setHoraHastaAspvh(null);
						solicitudVacaciones.setNroHorasAspvh(null);
						solicitudVacaciones.setNroDiasAspvh(null);
						solicitudVacaciones.setDetalleAspvh(null);

						return;
					}
				}
			}*/
			}			
		}

		else if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==4) {
			int valorFinal=0;
			valorFinal=utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), utilitario.getDate());
			if (valorFinal<Integer.parseInt(utilitario.getVariable("p_dias_ingreso_permiso_personal"))) {
			}else {
			if (utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), utilitario.getDate())>1) {
				utilitario.agregarMensajeError("No se puede registrar el permiso", "Pongase en contacto con TTHH");
				solicitudVacaciones.setFechaHastaAspvh(null);
				solicitudVacaciones.setFechaDesdeAspvh(null);
				solicitudVacaciones.setHoraDesdeAspvh(null);
				solicitudVacaciones.setHoraHastaAspvh(null);
				solicitudVacaciones.setNroHorasAspvh(null);
				solicitudVacaciones.setNroDiasAspvh(null);
				solicitudVacaciones.setDetalleAspvh(null);

				return;
			}	
		}
		}
		
		   boolean estadoEntradaMenor=false,estadoEntradaMayor=false,estadoSalidaMenor=false,estadoSalidaMayor=false,estadoFinal=false;
		
		boolean estadoEntrada1=false,estadoEntrada2=false;
		
		boolean estadoSalida1=false,estadoSalida2=false;
    	
		
		Calendar calFechaSolicitudPermisoEntrada = Calendar.getInstance();
    	Calendar calFechaSolicitudPermisoSalida = Calendar.getInstance();
    	calFechaSolicitudPermisoEntrada.setTime(solicitudVacaciones.getFechaDesdeAspvh());
		calFechaSolicitudPermisoSalida.setTime(solicitudVacaciones.getFechaHastaAspvh());

			//
    	int valorRetorno=0;
    	Calendar calFecha = Calendar.getInstance();
    	Calendar calFechaInicio = Calendar.getInstance();
    	Calendar calFechaFin = Calendar.getInstance();
			TablaGenerica tab_novedad=utilitario.consultar("select ide_aspvh,tipo_aspvh,ide_asmot,fecha_desde_aspvh,fecha_hasta_aspvh,aprobado_tthh_aspvh,hora_desde_aspvh, "
	    			+ "hora_hasta_aspvh,nro_horas_aspvh,detalle_aspvh,aprobado_aspvh,fecha_solicitud_aspvh,nro_dias_aspvh  "
	    			+ "from asi_permisos_vacacion_hext "
	    			+ "where ide_gtemp="+solicitudVacaciones.getIdeGtemp().getIdeGtemp()+"  and fecha_solicitud_aspvh between '"+getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(solicitudVacaciones
	    					.getFechaSolicitudAspvh(), -30))+"' and "		    					
	    			+ "'"+	getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(solicitudVacaciones.getFechaSolicitudAspvh(), 30))+"' and anulado_aspvh =false");

			if (tab_novedad.getTotalFilas()<0) {
	    		//si no tiene permisos ingresados en ese rango de fechas

		
	    		//System.out.println("no existe permiso ingresados para esa fecha"+valorRetorno);

	    	}else {
	    		
	    	
	    	for (int i = 0; i < tab_novedad.getTotalFilas(); i++) {
	    	
	    		//Obtengo los dias de los permisos consultados hasta tres dias antes
	    		String fechaInicioAsnov=tab_novedad.getValor(i, "fecha_desde_aspvh");
	    		String fechaFinAsnov=tab_novedad.getValor(i, "fecha_hasta_aspvh");
	    		

	    	
	    			    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
	    				calFecha.setTime(solicitudVacaciones.getFechaSolicitudAspvh());
	    				calFechaInicio.setTime(getFechaAsyyyyMMdd(fechaInicioAsnov));
	    				calFechaFin.setTime(getFechaAsyyyyMMdd(fechaFinAsnov));
	    				//cALFECHAiNICIO ES MENOR QUE  FECHA 
	    				
	    				
	    				if (calFechaSolicitudPermisoEntrada.compareTo(calFechaInicio) >= 0 && calFechaSolicitudPermisoSalida.compareTo(calFechaFin)<=0)
	    				//if (calFecha.compareTo(calFechaInicio) >= 0 && calFecha.compareTo(calFechaFin)<=0)
	    				{
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
    	    					utilitario.agregarMensajeInfo("Permiso No.: "+ide_aspvh+" por dias ","El permiso ya se encuentra ingresado para el rango de fechas seleccionadas");
    	    					return;
		    	    	    	    			
	    	    			}else if (tipoPermiso.equals("1")) {
	    	    				//permiso ingresadio por dias
	    	    				
	    	    				if (solicitudVacaciones.getTipoAspvh()==4) {
	    	    					utilitario.agregarMensajeInfo("Permiso No.: "+ide_aspvh+" por horas ","El permiso ya se encuentra ingresado para el rango de fechas seleccionadas");
	    	    					return;		
								}
	    	    				
	    	    				//Obtengo los valores 
	    	    				String hora_desde_aspvh=tab_novedad.getValor(i, "hora_desde_aspvh");
	    	    				String hora_hasta_aspvh=tab_novedad.getValor(i, "hora_hasta_aspvh");
	    	    				String nro_horas_aspvh=tab_novedad.getValor(i, "nro_horas_aspvh");
	    	    				
	    	    				String horaDesde="",minDesde="",horaHasta="",minHasta="";
	    	    				
	    	    				if (solicitudVacaciones.getHoraDesdeAspvh().getHours()<10) {
									horaDesde="0"+solicitudVacaciones.getHoraDesdeAspvh().getHours();
								}else {
									horaDesde=""+solicitudVacaciones.getHoraDesdeAspvh().getHours();
								}
	    	    				
	    	    				
	    	    				if (solicitudVacaciones.getHoraDesdeAspvh().getMinutes()<10) {
									minDesde="0"+solicitudVacaciones.getHoraDesdeAspvh().getMinutes();
								}else {
									minDesde=""+solicitudVacaciones.getHoraDesdeAspvh().getMinutes();
											
								}
	    	    				
	    	    				String horaTimbreEntrada =""+horaDesde+":"+minDesde+":00";
	    	    			
	    	    				if (solicitudVacaciones.getHoraHastaAspvh().getHours()<10) {
									horaHasta="0"+solicitudVacaciones.getHoraHastaAspvh().getHours();
								}else {
									horaHasta=""+solicitudVacaciones.getHoraHastaAspvh().getHours();
								}
	    	    				
	    	    				
	    	    				if (solicitudVacaciones.getHoraHastaAspvh().getMinutes()<10) {
									minHasta="0"+solicitudVacaciones.getHoraHastaAspvh().getMinutes();
								}else {
									minHasta=""+solicitudVacaciones.getHoraHastaAspvh().getMinutes();
								}
	    	    				
	    	    				String horaTimbreSalida =""+horaHasta+":"+minHasta+":00";
	    	    			
	    	    				
	    	    				String mensaje="";
	    	    				int estadoPermiso=0;
	    	    		
	    	    			  	    		
	    	    			// Estado de la entrada
	    	    				if(hora_desde_aspvh.compareTo(horaTimbreEntrada)>=0){
	    	    					estadoEntrada1=true;
	    	    				}else {
	    	    					estadoEntrada1=false;
								}
	    	    				if (hora_hasta_aspvh.compareTo(horaTimbreSalida)<=0) {
	    	    					estadoEntrada2=true;

								}else {
	    	    					estadoEntrada2=false;

								}
	    	    				//hhjh
	    	    				
	    	    			//Validacion si la entrada es mayor o menor a 	
	    	    
	    	    		if ((estadoEntrada1==true) && (estadoEntrada2==false)) {
							estadoEntradaMenor=true;
						}
	    	    				
	    	    		if ((estadoEntrada1==false) && (estadoEntrada2==true)) {
	    	    			estadoEntradaMayor=true;
						}
	    	    		
	    	    		
	    	    		if (estadoEntradaMayor) {
							if (hora_hasta_aspvh.compareTo(horaTimbreSalida)<=0) {
								//System.out.println("registro permitido Mayor");

    	    					
							}else {
								utilitario.agregarMensajeInfo("Rango de horas inválidas","Revise el rango de horas del Permiso No.: "+ide_aspvh);
    	    					return;
							}
						}
	    	    		
	    	    		if (estadoEntradaMenor) {
							if (hora_desde_aspvh.compareTo(horaTimbreSalida)>=0) {
								//System.out.println("registro permitido Menor");
								
							}else {
								utilitario.agregarMensajeInfo("Rango de horas inválidas","Revise el rango de horas del Permiso No.: "+ide_aspvh);
    	    					return;
							}
						}
	    	    		
	    	    					
	    	    			  	    		
	    	    			/*	if (hora_desde_aspvh.compareTo(horaTimbreEntrada)>=0 && hora_hasta_aspvh.compareTo(horaTimbreSalida)<=0) {
	    	    					utilitario.agregarMensajeInfo("Rango de horas invï¿½lidas","Revise el rango de horas del Permiso No.: "+ide_aspvh);
	    	    					return;
	    	    				}	
	    	    				else {
	    	    		
	    	    			/*	}*/
	    	    				

	    	    	}
	    	    		
	    				}//Fechas		
	    	    							
	    					
		
		
		
	    	}//FOR
		
		
	    	}//ELSE
		
		
		
		
		
			if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==4 || solicitudVacaciones.getIdeAsmot().getIdeAsmot()==31){
				if (solicitudVacaciones.getTipoAspvh().intValue()==1) {
					
				}else {
					utilitario.agregarMensajeInfo("El permiso seleccionado solo puede ser ingresado por horas", "Rango de fechas inválido");
					return;
					
				}
			}
		
		
			if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==30){
				String fecha_limite=utilitario.DeDateAString(utilitario.sumarDiasFechaSinFinSemana(solicitudVacaciones.getFechaDesdeAspvh(), 2));
				String fecha_hasta=utilitario.DeDateAString(solicitudVacaciones.getFechaHastaAspvh());
   				if (fecha_limite.compareTo(fecha_hasta)<0) {
   					utilitario.agregarMensaje("No se puede realizar la accion seleccionada", "El permiso unicamente puede ser tomado por 3 dias laborables seguidos");
   					return;
   				}		
   					}

			
			
		
//			Valiacion dias pendientes en negativo a vacacion 
			
				if (solicitudVacaciones.getIdeAsmot().getIdeAsmot()==14 ||solicitudVacaciones.getIdeAsmot().getIdeAsmot()==3) {
				 double dias_pendien_=0.00,division=0.00;
				 division=solicitudVacaciones.getNroHorasAspvh().doubleValue()/8;
				 dias_pendien_=dias_pendientes-division;
					if (dias_pendien_<Integer.parseInt(utilitario.getVariable("p_dias_max_permitidos"))) {
					utilitario.agregarMensajeInfo("Estimado funcionario usted no contiene dias disponibles", "Pongase en contacto con la persona encargada de TTHH");
					return;
				}else {
					//horas
			
					//Permisos sin aprobar 
					
					double valor_temp_dias=0.00;
					TablaGenerica tab_vacacion=utilitario.consultar("SELECT  ide_gtemp, SUM(nro_horas_aspvh) as nro_horas_aspvh  "
							+ "FROM asi_permisos_vacacion_hext  "
							+ "WHERE IDE_GTEMP="+utilitario.getVariable("IDE_GTEMP")+" AND IDE_ASMOT IN(14,3) AND ANULADO_ASPVH=FALSE AND REGISTRO_NOVEDAD_ASPVH =FALSE AND FECHA_SOLICITUD_ASPVH >= '2021-01-01' "
							+ "group by ide_gtemp ");

					if (tab_vacacion.getValor("nro_horas_aspvh")==null || tab_vacacion.getValor("nro_horas_aspvh").equals("0.00") ) {
						
					}else {
						valor_temp_dias=Double.parseDouble(tab_vacacion.getValor("nro_horas_aspvh"))/8;
					}
					
					double valor1=0.0,valor2=0.0,valorMaxTomaVacaciones=0.0;
					String texto="";
					if (solicitudVacaciones.getTipoAspvh().intValue()==1) {
						int dias_sabados=getDiasSabado(utilitario.DeDateAString(solicitudVacaciones.getFechaDesdeAspvh()),1);
						int dias_domingos=getDiasDomingo(utilitario.DeDateAString(solicitudVacaciones.getFechaDesdeAspvh()),1);
						valor1=Double.parseDouble(utilitario.getFormatoNumero((solicitudVacaciones.getNroHorasAspvh().doubleValue()/8),2));
						if (dias_sabados>0 || dias_domingos>0) {
							
							numeroDiasTomadosTemp=(dias_pendientes-valor1)-(int)valor_temp_dias; 

							//dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
							//int p_etn1Temp = ((int) dias_sumados_aplicados_vacacion / 5);
							//double descuentoTemp = dias_sumados_aplicados_vacacion + (p_etn1Temp * 2);
							//numeroDiasTomadosTemp = numeroDiasTomadosTemp + descuentoTemp + nroDiasAjustePeriodo + (numeroDiasTomadosFinSemana+valor1);
							//valor2 = Double.parseDouble(utilitario.getFormatoNumero((sumatotal_vacaciones - numeroDiasTomadosTemp),2));
							double valorEvaluar=dias_pendientes;
							String string = ""+valorEvaluar;
							String[] parts = string.split(Pattern.quote ("."));
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // 654321
							
							valorMaxTomaVacaciones=((Double.parseDouble("0."+part2)*8));
							
							String string_min = ""+valorMaxTomaVacaciones;
							String[] partmin = string.split(Pattern.quote ("."));
							String partMin1 = partmin[0]; // 123
							String partMin2 = partmin[1]; // 654321
							
							double minvalorMaxTomaVacaciones=((Double.parseDouble("0."+partMin2)*100));
							texto=part1+" Dias "+(int)valorMaxTomaVacaciones+" Horas "+(int)minvalorMaxTomaVacaciones+" Minutos";
							
							/*if (numeroDiasTomadosTemp>0.00){
							
							}else {
								utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
								return;
							}*/
							if (numeroDiasTomadosTemp<=0.00){
							if(numeroDiasTomadosTemp>=Double.parseDouble(utilitario.getVariable("p_dias_pendientes_adicionales"))){
								}else {
									utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
									return;
								}
							}
			
						}else {
							
							int p_etn1Temp = ((int) valor1 / 5);
							//double descuentoTemp = dias_sumados_aplicados_vacacion + (p_etn1Temp * 2);
							double valorTemporal=valor1 + (p_etn1Temp * 2);;
							numeroDiasTomadosTemp = (dias_pendientes-valorTemporal)-(int)valor_temp_dias;
							

							//dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial+valor1;
							//int p_etn1Temp = ((int) dias_sumados_aplicados_vacacion / 5);
							//double descuentoTemp = dias_sumados_aplicados_vacacion + (p_etn1Temp * 2);
							//numeroDiasTomadosTemp = numeroDiasTomadosTemp + descuentoTemp + nroDiasAjustePeriodo + numeroDiasTomadosFinSemana;
							//valor2 = Double.parseDouble(utilitario.getFormatoNumero((sumatotal_vacaciones - numeroDiasTomadosTemp),2));	
							
							double valorEvaluar=dias_pendientes;
							String string = ""+valorEvaluar;
							String[] parts = string.split(Pattern.quote ("."));
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // 654321
							valorMaxTomaVacaciones=((Double.parseDouble("0."+part2)*8));
							
							String string_min = ""+valorMaxTomaVacaciones;
							String[] partmin = string.split(Pattern.quote ("."));
							String partMin1 = partmin[0]; // 123
							String partMin2 = partmin[1]; // 654321
							
							double minvalorMaxTomaVacaciones=((Double.parseDouble("0."+partMin2)*100));
							texto=part1+" Dias "+(int)valorMaxTomaVacaciones+" Horas "+(int)minvalorMaxTomaVacaciones+" Minutos";

								
							
							if (numeroDiasTomadosTemp<=0.00){
								if(numeroDiasTomadosTemp>=Double.parseDouble(utilitario.getVariable("p_dias_pendientes_adicionales"))){
							}else {
								utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
								return;
							}
								}
							
							/*if (numeroDiasTomadosTemp>0.00){
								
							}else {
								utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
								return;
							}*/
							
						}
						
						
					
						if (valor2>0) {
						}else if(utilitario.getVariable("p_empleado_ingresa_permisos").equals("392"))
						{	
						}else {
		
							utilitario.agregarMensajeInfo("Estimado funcionario usted ha sobrepasado el valor de tiempo disponibles", "Tiempo disponible con cargo a vacación : "+texto);
							return;
						}
					}else {
						//permisos por dias
						valor1=solicitudVacaciones.getNroDiasAspvh().doubleValue();
						int dias_sabados=getDiasSabado(utilitario.DeDateAString(solicitudVacaciones.getFechaDesdeAspvh()),solicitudVacaciones.getNroDiasAspvh());
						int dias_domingos=getDiasDomingo(utilitario.DeDateAString(solicitudVacaciones.getFechaDesdeAspvh()),solicitudVacaciones.getNroDiasAspvh());
						//valor1=solicitudVacaciones.getNroDiasAspvh().doubleValue();
//IOghh
						if (dias_sabados>0 || dias_domingos>0 ) {
											
							numeroDiasTomadosTemp=(dias_pendientes-valor1)-(int)valor_temp_dias; 

						//	dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
						//	int p_etn1Temp = ((int) dias_sumados_aplicados_vacacion / 5);
						//	double descuentoTemp = dias_sumados_aplicados_vacacion + (p_etn1Temp * 2);
						//	//numeroDiasTomadosTemp = numeroDiasTomadosTemp + descuentoTemp + nroDiasAjustePeriodo + (numeroDiasTomadosFinSemana+valor1);
							//valor2 = Double.parseDouble(utilitario.getFormatoNumero((sumatotal_vacaciones - numeroDiasTomadosTemp),2));
							double valorEvaluar=dias_pendientes;
							String string = ""+valorEvaluar;
							String[] parts = string.split(Pattern.quote ("."));
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // 654321
		
							valorMaxTomaVacaciones=((Double.parseDouble("0."+part2)*8));
							
							String string_min = ""+valorMaxTomaVacaciones;
							String[] partmin = string.split(Pattern.quote ("."));
							String partMin1 = partmin[0]; // 123
							String partMin2 = partmin[1]; // 654321
							
							double minvalorMaxTomaVacaciones=((Double.parseDouble("0."+partMin2)*100));
							texto=part1+" Dias "+(int)valorMaxTomaVacaciones+" Horas "+(int)minvalorMaxTomaVacaciones+" Minutos";
						
							/*if (numeroDiasTomadosTemp>0.00){
								
						}else {
								utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
								return;
							}*/
							
							if (numeroDiasTomadosTemp<=0.00){
								if(numeroDiasTomadosTemp>=Double.parseDouble(utilitario.getVariable("p_dias_pendientes_adicionales"))){
								}else {
									utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
									return;
								}
							}
							
						
						}else {
							//dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial+valor1;
							int p_etn1Temp = ((int) valor1 / 5);
							//double descuentoTemp = dias_sumados_aplicados_vacacion + (p_etn1Temp * 2);
							double valorTemporal=valor1 + (p_etn1Temp * 2);;
							numeroDiasTomadosTemp = (dias_pendientes-valorTemporal)-(int)valor_temp_dias;
							//valor2 = Double.parseDouble(utilitario.getFormatoNumero((sumatotal_vacaciones - numeroDiasTomadosTemp),2));	
							double valorEvaluar=dias_pendientes;
							String string = ""+valorEvaluar;
							String[] parts = string.split(Pattern.quote ("."));
							String part1 = parts[0]; // 123
							String part2 = parts[1]; // 654321
		
							valorMaxTomaVacaciones=((Double.parseDouble("0."+part2)*8));
							
							String string_min = ""+valorMaxTomaVacaciones;
							String[] partmin = string.split(Pattern.quote ("."));
							String partMin1 = partmin[0]; // 123
							String partMin2 = partmin[1]; // 654321
							
							double minvalorMaxTomaVacaciones=((Double.parseDouble("0."+partMin2)*100));
							texto=part1+" Dias "+(int)valorMaxTomaVacaciones+" Horas "+(int)minvalorMaxTomaVacaciones+" Minutos";
						
							if (numeroDiasTomadosTemp<=0.00){
							
							if(numeroDiasTomadosTemp>=Double.parseDouble(utilitario.getVariable("p_dias_pendientes_adicionales"))){
							
								
							}else {
								utilitario.agregarMensajeInfo("No se puede generar el permiso solicitado", "Usted a sobrepasado los dias disponibles");
								return;
							}
							}
						}
		
		
						
		
						if (valor2>0) {
						}else if(utilitario.getVariable("p_empleado_ingresa_permisos").equals("392")){
						}else {
							utilitario.agregarMensajeInfo("Estimado funcionario usted ha sobrepasado el valor de tiempo disponibles", "Tiempo disponible con cargo a vacación : "+texto);
							return;
						}
					}				
				}
				}
		
		


		
		//valido si el permiso es personal le deje ingresar para tipo horas 
		if (validarTiempoPermitidoPermiso() == 2) {
			if (solicitudVacaciones.getTipoAspvh()==1)
			{
				
			}else {
				
			if (solicitudVacaciones.getNroDiasAspvh().intValue() > 3  && banderaPermisoExcedido==false) {
				//System.out.println("Resultado2");

				utilitario.agregarMensajeError("Solicitud Personal no válida", "Debe escoger un rango menor o igual a 3 días");
				return;
			}
			}

			int dias = utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), solicitudVacaciones.getFechaSolicitudAspvh());

			//System.out.println("diassasas" + dias);

			//if (solicitudVacaciones.getTipoAspvh().intValue()==4) {

		//		if ((((int) dias) > -1)) {

			//		utilitario.agregarMensajeInfo("Solicitud de vacaciones inválida", "Su solictud debe ser planificada con 1 dias de anticipación");
		//			return;
		//		} else {

			//	}
			//}

		}


		/**
		 * Asignacion y caldulo de fines de semana
		 */
		
		
		double dias_solicitados = 0.0;

		if ((solicitudVacaciones.getNroDiasAspvh() == null) ||(solicitudVacaciones.getNroDiasAspvh().toString().isEmpty())) {
			
		}else {
			dias_solicitados = solicitudVacaciones.getNroDiasAspvh();

		}

			
		solicitudJustificacion.setFechaAspej(new Date());
		solicitudVacaciones.setGenIdeGeedp(solicitudVacaciones.getGenIdeGeedp2());
		
		
		
		
		
		TablaGenerica  tab_empleado_asignado=utilitario.consultar("SELECT empjefe.ide_emjei, jefe.ide_asjei, empjefe.activo_emjei,jefe.ide_gtemp_padre_asjei,jefe.ide_gtemp "
				+ "FROM asi_jefe_inmediato jefe "
				+ "left join asi_empleado_jefe_inmediato  empjefe on empjefe.ide_asjei=jefe.ide_asjei "
				+ "where jefe.tipo_asjei=3 and "
				+ "jefe.ide_gtemp_padre_asjei in(select ide_gtemp from gen_empleados_departamento_par where ide_geedp in("+solicitudVacaciones.getGenIdeGeedp2().getIdeGeedp().toString()+")) "
				+ "and empjefe.ide_gtemp in("+solicitudVacaciones.getIdeGtemp().getIdeGtemp()+")");
		
		tab_empleado_asignado.imprimirSql();
		System.out.println("validacion");
		if (tab_empleado_asignado.getTotalFilas()>0) {
			solicitudVacaciones.setIdeGtempPreaprobador(Integer.parseInt(tab_empleado_asignado.getValor("ide_gtemp")));
			empleadoPreAprobador=tab_empleado_asignado.getValor("ide_gtemp");
			solicitudVacaciones.setIdeAsjei(Integer.parseInt(tab_empleado_asignado.getValor("ide_asjei")));
		}else {
			solicitudVacaciones.setIdeGtempPreaprobador(null);
			solicitudVacaciones.setIdeAsjei(null);
			empleadoPreAprobador=null;
		}
	


		// Valido que tenga dias de vacaciones

		/*
		 * Calculo de dias de gracias en solicitudes que aplica a vacacion
		 */

		calculo_dias_max_aplica = -15.0;

		if ((validarTiempoPermitidoPermiso() == 1)) {

//			if (solicitudVacaciones.getNroDiasAspvh() >= (Integer.parseInt(utilitario.getVariable("p_dias_max_permitidos_vacacion"))+dias_pendientes)) {
//				utilitario.agregarMensajeError("Solicitud inv�lida", "Puede tomar unicamente hasta "+utilitario.getVariable("p_dias_max_permitidos_vacacion")+" d�as con cargo a vacaci�n");
//				return;
//			}
		}

		
		//Calculo de horas
			if (solicitudVacaciones.getTipoAspvh()==1) {
				
				if ((solicitudVacaciones.getNroHorasAspvh()==null)||(solicitudVacaciones.getNroHorasAspvh().toString().isEmpty())) {
					utilitario.agregarMensajeError("Rango de Horas inválida", "Nro. hora(s) vacias");
					return;
				}else
		
			if(solicitudVacaciones.getNroHorasAspvh().compareTo(BigDecimal.ZERO)<=0){
					utilitario.agregarMensajeError("Rango de Horas inválida", "Nro. hora(s) 0");
				return;
			}
		
		}
		

		
		// valido si es permiso con cargo a vacaciones
		if ((validarTiempoPermitidoPermiso() == 1) || (validarTiempoPermitidoPermiso() == 2)) {

			if (solicitudVacaciones.getTipoAspvh() == 1) {

			/*	if (dias_pendientes >= -15) {

				} else {
					double diasPendientesTomar = (solicitudVacaciones.getNroHorasAspvh().doubleValue() / 8);
					double calculoDiasPendientesTomar = dias_pendientes + Integer.parseInt(utilitario.getVariable("p_dias_max_permitidos_vacacion"));
					utilitario.agregarMensajeInfo("Rango de Vacaciones permito es de -15 dias ", "Usted contiene: " + dias_pendientes + " a tomar");
					return;
				}

			}

			if (solicitudVacaciones.getTipoAspvh() == 4) {
				double nroDiasDescontar = dias_pendientes - solicitudVacaciones.getNroDiasAspvh();
				double resultadoGraciaDiasPendientes = dias_pendientes - solicitudVacaciones.getNroDiasAspvh().doubleValue();
//Ver si cambiar
				if (resultadoGraciaDiasPendientes >= -(Integer.parseInt(utilitario.getVariable("p_dias_max_permitidos_vacacion"))+dias_pendientes)) {

				} else {
					double calculoDiasPendientesTomar = dias_pendientes + 15;
					utilitario.agregarMensajeInfo("Rango de Vacaciones permito es de -15 dias ", "Usted contiene: " + dias_pendientes + " a tomar");
					return;
				}*/

			}

			
	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}

		else {

			// aqui cambio
			try {
				double dou_solicita = Double.parseDouble(solicitudVacaciones.getNroDiasAspvh() + "");
				if (solicitudVacaciones.getTipoAspvh() == 1) {
					dou_solicita = pckUtilidades.CConversion.CDbl_2(solicitudVacaciones.getNroHorasAspvh()) / 8;
					// solicitudVacaciones.setNroHorasAspvh(BigDecimal.valueOf(dou_solicita));
				} else {
					// solicitudVacaciones.setNroHorasAspvh(null);
					solicitudVacaciones.setHoraDesdeAspvh(null);
					solicitudVacaciones.setHoraHastaAspvh(null);

					nuemro_horas_reporte = "8";
					hora_inicial_reporte = "08:00:00";
					hora_final_reporte = "17:00:00";

				}

			} catch (Exception e) {
			}

			/*
			 * }else {
			 * utilitario.agregarMensajeError("No se puede guardar la solicitud"
			 * , "El número de horas supera el tiempo permitido ("+
			 * numMaximoDiasPermiso
			 * +" días) para el tipo de permiso solicitado");
			 * this.puedeGuardarSolicitud = false;
			 * System.out.println("exeption supera tiempo limite...."); return;
			 * }
			 */
		} // else {
			// utilitario.agregarMensajeError("Solicitud inválida",
			// "No contiene días de vacaciones");
			// return;
			// }
		System.out.println("Intentando generar el permiso...." );
		if(subirAdjunto())
		{
			
			if (solicitudJustificacion.getArchivoAspej()==null) {
				
			}else if(solicitudJustificacion.getArchivoAspej().length()>0){
				utilitario.agregarMensajeInfo("Archivo Adjunto", "Se guardo correctamente...");
			}

			String str_mensaje = servicioVacaciones.guardarSolicitudVacaciones(solicitudVacaciones, solicitudJustificacion);
			Correo(solicitudVacaciones.getIdeGtemp().getIdeGtemp(),banderaPermisoExcedido);

			System.out.println("str_mensaje: " + str_mensaje);
			if (str_mensaje.isEmpty()) {
				utilitario.agregarMensaje("Se guardo Correctamente", "");
				utilitario.agregarMensaje("Correo enviado satisfactoriamente", "Su solicitud se encuentra generada correctamente");
				if (banderaPermisoExcedido==true) {
				utilitario.agregarMensaje("Su permiso a excedió el tiempo límite de 3 días hábiles", "Permiso cargado a vacaciones");
				}

				System.out.println("Se ha generado un nuevo permiso: "+utilitario.getVariable("EMP_SESSION"));
				try {
					cargarDatos();		
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("ERROR CARGAR DATOS");
				}
				
			} else {
				utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
				}
				
				
		}
		else
			utilitario.agregarMensajeError("No se puede subir el archivo","Error al cargar el adjunto");

	}

	/**
	 * Metodo emailNotificacionAprobado
	 */

	public String emailNotificacionAprobadoJefe(String str_mensaje, String ide_aspvh_correo, String fecha_solicitud_aspvh) {
		String html = "<p>Estimado/a, " + "<p>&nbsp;</p>\n" + "<p>Notificamos mediante la presente que la solicitud de permisos Nro: " + ide_aspvh_correo + "  con fecha: " + fecha_solicitud_aspvh + " ha sido ingresada satisfactoriamente.</p>\n"
				+ "<p>&nbsp;</p>\n" + "<p>Saludos cordiales,</p>\n" + "<table style=\"height: 144px;\" width=\"571\">\n" + "<tbody>\n" + "<tr>\n"
				+ "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n" + "<td width=\"476\">\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">Ing." + str_mensaje + " </p>\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n" + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
				+ "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n" + "</td>\n" + "</tr>\n" + "</tbody>\n" + "</table>";
		return html;
	}

	/**
	 * Metodo emailNotificacionAprobado
	 */

	public String emailNotificacionAprobado(String strNombreEmpleado, String detallePermiso, String fecha_solicitud_aspvh) {
		String html = "<p>Estimado/a   "+strNombreEmpleado+", " + "<p>&nbsp;</p>\n" + "<p>Notificamos mediante la presente que la solicitud de permisos/vacaciones:  " + detallePermiso + "  con fecha: " + fecha_solicitud_aspvh + " ha sido ingresada satisfactoriamente.</p>\n"
				+ "<p>&nbsp;</p>\n" + "<p>Saludos cordiales,</p>\n" + "<table style=\"height: 144px;\" width=\"571\">\n" + "<tbody>\n" + "<tr>\n"
				+ "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n" + "<td width=\"476\">\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">" + " </p>\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n" + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
				+ "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n" + "</td>\n" + "</tr>\n" + "</tbody>\n" + "</table>";
		return html;
	}

	public String emailNotificacionMarcacionEmpleado(String strNombreEmpleado, int detallePermiso, String fecha_solicitud_aspvh) {
		String html = "<p>Estimado/a   "+strNombreEmpleado+", " + "<p>&nbsp;</p>\n" + "<p>Ha realizado el registro de marcación por Teletrabajo a través del Portal del Sistema ERP con fecha y hora: " + fecha_solicitud_aspvh + " .</p>\n"
				//+"<p> El mismo que debe ser ingresado en el portal para la correcta validacion de la marcac" + "<p>&nbsp; .  Recuerde si no realiza la validacion no se registrara su marcacion</p>\n"
				+ "<p>&nbsp;</p>\n" + "<p>Saludos cordiales,</p>\n" + "<table style=\"height: 144px;\" width=\"571\">\n" + "<tbody>\n" + "<tr>\n"
				+ "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n" + "<td width=\"476\">\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">" + " </p>\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n" + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
				+ "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n" + "</td>\n" + "</tr>\n" + "</tbody>\n" + "</table>";
		return html;
	}


	public String emailNotificacionAprobado1(String strNombreEmpleado, String detallePermiso, String fecha_solicitud_aspvh) {
		String html = "<p>Estimado/a, " + "<p>&nbsp;</p>\n" + "<p>Notificamos mediante la presente que la solicitud de permisos/vacaciones:  " + detallePermiso + " se encuentra en su bandeja. Para revisarlo de <a href=\"http://erp.emgirs.gob.ec:8080/sampu/home.jsf\" > Clic Aquí </a></p>\n" + "<p>&nbsp;</p>\n" + "<p>Saludos cordiales,</p>\n"
				+ "<table style=\"height: 144px;\" width=\"571\">\n" + "<tbody>\n" + "<tr>\n" + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
				+ "<td width=\"476\">\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\">" + " </p>\n" + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
				+ "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n" + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
				+ "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n" + "</td>\n" + "</tr>\n" + "</tbody>\n" + "</table>";
		return html;
	}

	/**
	 * Metodo de Envio de Correo Electronico
	 */
	public void Correo(int ide_gtemp,boolean banderaPermisoExcedido) {
		try {
			TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, " + "clave_corr from sis_correo where ide_corr=2");
			boolean bandCorreoAviso=false;
			String smtp_correo = tab_correo_envio.getValor("smtp_corr");
			String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
			String correo_envio = tab_correo_envio.getValor("correo_corr");
			String usuario_envio = tab_correo_envio.getValor("usuario_corr");
			String clave_correo = tab_correo_envio.getValor("clave_corr");
			String detallePermiso="";
			String detalleAspvh="",strNombreEmpleadoAviso = "";

			//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();
			EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

			TablaGenerica tab_correo = utilitario.consultar("select ide_gtemp,detalle_gtcor "
					+ "from gth_correo  "
					+ "where ide_gtemp=" + solicitudVacaciones.getIdeGtemp().getIdeGtemp()+" "
					+ "and activo_gtcor=true and notificacion_gtcor=true");
			String correo = tab_correo.getValor("detalle_gtcor");
			//tab_correo.imprimirSql();

			System.out.println("solicitudVacaciones.getIdeAsmot().getIdeAsmot(): "+solicitudVacaciones.getIdeAsmot().getIdeAsmot());
			 p_encargado_revision_permisos_enfermedad=utilitario.getVariable("p_encargado_revision_permisos_enfermedad");
			 String correo_aviso_enfermedad ="",correo_aviso_trabajadora_social =""; 
			TablaGenerica tab_correo_aviso_enfermedad = utilitario.consultar("select ide_gtemp,detalle_gtcor "
						+ "from gth_correo  "
						+ "where ide_gtemp=" + p_encargado_revision_permisos_enfermedad+" "
						+ "and activo_gtcor=true and notificacion_gtcor=true");
			tab_correo_aviso_enfermedad.imprimirSql();
			if (tab_correo_aviso_enfermedad.getTotalFilas()>0) {
				correo_aviso_enfermedad = tab_correo_aviso_enfermedad.getValor("detalle_gtcor");
				strNombreEmpleadoAviso = retornaDatosCorreoEmpleado(Integer.parseInt(p_encargado_revision_permisos_enfermedad));
				
				p_permisos_doc_enfermedad=utilitario.getVariable("p_permisos_doc_enfermedad");
				if (Integer.parseInt(p_permisos_doc_enfermedad)==solicitudVacaciones.getIdeAsmot().getIdeAsmot()) {
					bandCorreoAviso=true;
					p_encargado_revision_permisos_enfermedad_trabajador_social=utilitario.getVariable("p_encargado_trabajador_social");
					TablaGenerica tab_correo_aviso_trabajador_social = utilitario.consultar("select ide_gtemp,detalle_gtcor "
							+ "from gth_correo  "
							+ "where ide_gtemp=" + p_encargado_revision_permisos_enfermedad_trabajador_social+" "
							+ "and activo_gtcor=true and notificacion_gtcor=true");
					if (tab_correo_aviso_trabajador_social.getTotalFilas()>0) {
						correo_aviso_trabajadora_social = tab_correo_aviso_trabajador_social.getValor("detalle_gtcor");
					}
					
					
					
				}else {
					bandCorreoAviso=false;
				}
			}else {
				bandCorreoAviso=false;
			}
			

			 
			 
			TablaGenerica tab_correo_jefe = utilitario.consultar("select ide_gtemp,detalle_gtcor  "
					+ "from gth_correo  "
					+ "where ide_gtemp in (select ide_gtemp from gen_empleados_departamento_par where ide_geedp="
					+ solicitudVacaciones.getGenIdeGeedp2().getIdeGeedp().toString() + ") "
					+ "and activo_gtcor=true and notificacion_gtcor=true");
			String correo_jefe = tab_correo_jefe.getValor("detalle_gtcor");
			//tab_correo_jefe.imprimirSql();
			// Estructura de mensaje
			// obtengo el empleado del cual requiero los datos
			
			TablaGenerica tab_empleado = utilitario.consultar("select * from gth_empleado where ide_gtemp=" + solicitudVacaciones.getIdeGtemp().getIdeGtemp());

			String documento = tab_empleado.getValor("documento_identidad_gtemp");
			String primer_nombre_empleado = tab_empleado.getValor("primer_nombre_gtemp").toString();
			String segundo_nombre_empleado = tab_empleado.getValor("segundo_nombre_gtemp").toString();
			String apellido_paterno_empleado = tab_empleado.getValor("apellido_paterno_gtemp").toString();
			String apellido_materno_empleado = tab_empleado.getValor("apellido_materno_gtemp").toString();

			// armar mensaje
			ide_aspvh_correo = solicitudVacaciones.getIdeAspvh().toString();
            detalleAspvh = solicitudVacaciones.getDetalleAspvh().toString().toUpperCase();
			// Nombre y fecha
			Date fecha_solicitud_aspvh1 = solicitudVacaciones.getFechaSolicitudAspvh();
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			String fecha_solicitud_aspvh = sf.format(fecha_solicitud_aspvh1);

			String strNombreEmpleado = retornaDatosCorreoEmpleado(ide_gtemp);

			
			TablaGenerica obtenerIdeGtempJefe = utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_geedp=" + solicitudVacaciones.getGenIdeGeedp2().getIdeGeedp().toString());

			if (solicitudVacaciones.getTipoAspvh().intValue() == 1) {
				nombreTipoSolicitud = "Permiso Por Horas ";
				BigDecimal obtieneNroHoras = solicitudVacaciones.getNroHorasAspvh();
				nroHoras = obtieneNroHoras.doubleValue();
				nroHoras =((double)Math.round(nroHoras * 100d) / 100d);
				if(banderaPermisoExcedido==false){
				detallePermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + nroHoras + " hora(s)  referente a: " +detalleAspvh.toLowerCase();}
				else{
					detallePermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + nroHoras + " hora(s)  referente a: " +detalleAspvh.toLowerCase()+"  . Este permiso sera cargado a vacaciones ya que sobrepaso el tiempo permitdio para el registro de 72 horas   ";
				}
			}
			if (solicitudVacaciones.getTipoAspvh().intValue() == 4) {
				nombreTipoSolicitud = "Permiso Por Dias ";
				obtieneNroDias = solicitudVacaciones.getNroDiasAspvh();
				if(banderaPermisoExcedido==false){
				detallePermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + obtieneNroDias + " dia(s)  referente a: " +detalleAspvh.toLowerCase();
				}else{
				detallePermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + obtieneNroDias + " dia(s)  referente a: " +detalleAspvh.toLowerCase()+".  Este permiso sera cargado a vacaciones ya que sobrepaso el tiempo permitdio para el registro de 72 horas   ";
				}
			}

			System.out.println(detallePermiso);

			try {
				//util.EnviaMail(envMail, correo, "GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE", emailNotificacionAprobado(strNombreEmpleado, detallePermiso, fecha_solicitud_aspvh), null);
				envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
				envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado, detallePermiso, fecha_solicitud_aspvh));
				envMail.setPara(correo);
				System.out.println(correo);
				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
				
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
				System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
			}

			try {
				//util.EnviaMail(envMail, correo_jefe, "GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE", emailNotificacionAprobado1("",  "El Funcionario : "+strNombreEmpleado+"   "+detallePermiso, fecha_solicitud_aspvh), null);
				envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
				envMail.setCuerpoHtml(emailNotificacionAprobado1("",  "El Funcionario : "+strNombreEmpleado+"   "+detallePermiso, fecha_solicitud_aspvh));
				
				if (empleadoPreAprobador==null) {
				envMail.setPara(correo_jefe);
					System.out.println(correo_jefe);

				}else {
					envMail.setPara(correo_jefe);
					System.out.println(correo_jefe);

					TablaGenerica tab_correo_pre_aprobador = utilitario.consultar("select ide_gtemp,detalle_gtcor  "
							+ "from gth_correo  "
							+ "where ide_gtemp in ("+empleadoPreAprobador+") "
							+ "and activo_gtcor=true and notificacion_gtcor=true");
					String correo_pre_aprobador = tab_correo_pre_aprobador.getValor("detalle_gtcor");
					envMail.setPara(correo_pre_aprobador);
					System.out.println("Correo enviado PreAprobador "+correo_pre_aprobador);
				}
				

			//AVISO DE CORREO ENFERMEDAD
			if (bandCorreoAviso==true) {
					envMail.setCopia(correo_aviso_enfermedad);
					envMail.setCopia(correo_aviso_trabajadora_social);	
					envMail.setCopia(utilitario.getVariable("p_notificacion_cssa"));	

					System.out.println("Se remitio correo electronico dra");
				}

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo_jefe);
			}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo_jefe);
				
			} catch (NullPointerException e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a ", "");
				System.out.println("Ha ocurrido un error al enviar el correo a jefe inmediato: " + e.getMessage()); 
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error al enviar el email()" + e.getMessage()); 
			utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email", "");
		}

	}

	
	public void CorreoMarcaciones(String ide_gtemp,int codigoValidador,String fecha) {
		try {
			TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, " + "clave_corr from sis_correo where ide_corr=2");

			String smtp_correo = tab_correo_envio.getValor("smtp_corr");
			String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
			String correo_envio = tab_correo_envio.getValor("correo_corr");
			String usuario_envio = tab_correo_envio.getValor("usuario_corr");
			String clave_correo = tab_correo_envio.getValor("clave_corr");
			String detallePermiso="";
			String detalleAspvh="";

			//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();
			EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

			TablaGenerica tab_correo = utilitario.consultar("select ide_gtemp,detalle_gtcor "
					+ "from gth_correo  "
					+ "where ide_gtemp=" + ide_gtemp+" "
					+ "and activo_gtcor=true and notificacion_gtcor=true");
			tab_correo.imprimirSql();
			String correo = tab_correo.getValor("detalle_gtcor");
		
			TablaGenerica tab_empleado = utilitario.consultar("select * from gth_empleado where ide_gtemp=" + ide_gtemp);

			String documento = tab_empleado.getValor("documento_identidad_gtemp");
			String primer_nombre_empleado = tab_empleado.getValor("primer_nombre_gtemp").toString();
			String segundo_nombre_empleado = tab_empleado.getValor("segundo_nombre_gtemp").toString();
			String apellido_paterno_empleado = tab_empleado.getValor("apellido_paterno_gtemp").toString();
			String apellido_materno_empleado = tab_empleado.getValor("apellido_materno_gtemp").toString();

			// armar mensaje
			//Date fecha_solicitud_aspvh1 = solicitudVacaciones.getFechaSolicitudAspvh();
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			String fecha_solicitud_aspvh = utilitario.getFechaActual();
			String strNombreEmpleado = retornaDatosCorreoEmpleado(Integer.parseInt(ide_gtemp));

			System.out.println("fecha_solicitud_aspvh: "+fecha_solicitud_aspvh);
			System.out.println("ide_gtemp :"+ide_gtemp);
			System.out.println("strNombreEmpleado :"+strNombreEmpleado);
			//System.out.println("codigoValidador :"+codigoValidador);
			//System.out.println("codigoValidador :"+codigoValidador);

			

			try {
				//util.EnviaMail(envMail, correo, "GESTION DE MARCACIONES POR TELETRABAJO ONLINE", emailNotificacionMarcacionEmpleado(strNombreEmpleado, codigoValidador, fecha), null);
				envMail.setAsunto("GESTION DE MARCACIONES POR TELETRABAJO ONLINE");
				envMail.setCuerpoHtml(emailNotificacionMarcacionEmpleado(strNombreEmpleado, codigoValidador, fecha));
				envMail.setPara(correo);

				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + correo);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + correo);
			} catch (Exception e) {
				utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
				System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error al enviar el email()" + e.getMessage()); 
			utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email", "");
		}

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
			
			if (solicitudJustificacion.getDetalleAspej().length()>0 || cargo)
			{
				String carpeta="/permisos/"+utilitario.getVariable("IDE_GTEMP")+"/";
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
					solicitudJustificacion.setArchivoAspej("/upload" +carpeta+ str_nombre);
				} catch (IOException e) {
					System.out.println("Error: subirAdjunto() "+e.getMessage());
				}
				subir=true;
			}
			else
			{
				System.out.println("No se ha seleccionado un archivo adjunto ...");
				subir=true;
			}
			
		} catch (Exception ex) {
			utilitario.agregarMensajeError("Error en el Archivo Adjunto", "Usted no adjunto ningun archivo...");
			System.out.println("Error subirAdjunto: " + ex.getMessage());
		}
		return subir;
	}
	
	/**
	 * Metodo para validar la subida de un archivo al repositorio en la base de
	 * datos sampu
	 * 
	 * @throws JRException
	 * @throws IOException
	 */

	/*
	public void subirFoto(FileUploadEvent event) {
		try {

			// solicitudVacaciones.setArchivoAdjuntoAspvh(event.getFile().getContents());

			String str_nombre = event.getFile().getFileName();
			String str_path = utilitario.getPropiedad("rutaUpload") + "/archivos";
			File path = new File(str_path);
			path.mkdirs();// Creo el Directorio
			File result = new File(str_path + "/" + str_nombre);
			// /Para el .war
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			str_path = extContext.getRealPath("/upload/permisos");
			// empleado.setPathFotoGtemp("/upload/fotos/" + str_nombre);
			path = new File(str_path);
			path.mkdirs();// Creo el Directorio
			File result1 = new File(str_path + "/" + str_nombre);

			int BUFFER_SIZE = 6124;
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(result);
				byte[] buffer = new byte[BUFFER_SIZE];
				int bulk;
				InputStream inputStream = event.getFile().getInputstream();
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
				System.out.println(e.getMessage());
			}
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(result1);
				byte[] buffer = new byte[BUFFER_SIZE];
				int bulk;
				InputStream inputStream = event.getFile().getInputstream();
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
				solicitudJustificacion.setArchivoAspej("/upload/permisos/" + str_nombre);
				// Guardo la foto si se subio correctamente
				// String str_msj = servicioEmpleado.guardarEmpleado(empleado);
				// if (str_msj.isEmpty()) {
				// utilitario.agregarMensaje("Se Guardo Correctamente", "");
				// } else {
				// utilitario.agregarMensajeError("No se pudo Guardar",
				// str_msj);
				// }
				// Recargar la pagina para que se cambie la foto
				// FacesContext.getCurrentInstance().getExternalContext().redirect("vacaciones.jsf");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
*/
	
	public String retornaDatosCorreoEmpleado(int IDE_GTEMP) {
		String detallePermiso = "";
		String nombreTipoSolicitud;
		String nroHoras;
		String motivo;
		String obtieneNroDias;
		// Estructura de mensaje
		String strNombreEmpleado = "";
		// obtengo el empleado del cual requiero los datos
		TablaGenerica tab_empleado = ser_empleado.getEmpleado(""+IDE_GTEMP);
		String documento = tab_empleado.getValor("documento_identidad_gtemp");
		String primer_nombre_empleado = tab_empleado.getValor("primer_nombre_gtemp").toString();
		String segundo_nombre_empleado = tab_empleado.getValor("segundo_nombre_gtemp").toString();
		String apellido_paterno_empleado = tab_empleado.getValor("apellido_paterno_gtemp").toString();
		String apellido_materno_empleado = tab_empleado.getValor("apellido_materno_gtemp").toString();
		strNombreEmpleado = ucFirst(primer_nombre_empleado) + " " + ucFirst(segundo_nombre_empleado) + " " + ucFirst(apellido_paterno_empleado) + "  " + ucFirst(apellido_materno_empleado);
		return strNombreEmpleado;

	}

	/*
	public void visualizarPermisos() throws JRException, IOException {

		try {

			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			System.out.println("path:" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(strPathReporte));

			reportBuilder();

			System.out.println("enter a imprimir veamos que pasa");

			JRExporter exporter = new JRPdfExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

			File fil_reporte = new File(ec.getRealPath("/reportes/reporte_" + utilitario.getVariable("IDE_USUA") + ".pdf"));

			// System.out.println("/reportes/reporte_"+utilitario.getNombreMes(utilitario.getMes(fecha_fin_gepro))+"_"
			// + utilitario.getVariable("IDE_USUA") + ".pdf");

			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);

			exporter.exportReport();

		} catch (Exception ex) {
			System.out.println("error" + ex.getMessage());
			ex.printStackTrace();
		}

	}

	
	public void reportBuilder() throws JRException {

		strPathReporte = utilitario.getURL() + "/reportes/reporte_" + utilitario.getVariable("IDE_USUA") + ".pdf";

		TablaGenerica miUsuario = utilitario.consultar("SELECT EPAR.IDE_GEEDP,   " + " EMP.APELLIDO_PATERNO_GTEMP || ' ' ||      " + " (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||     "
				+ " EMP.PRIMER_NOMBRE_GTEMP || ' ' ||     " + " (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS ,detalle_geare   " + " FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR     "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP    " + " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU     " + " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP     "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE where epar.ide_gtemp =   " + utilitario.getVariable("IDE_GTEMP") + " order by APELLIDO_PATERNO_GTEMP;   ");

		System.out.println("SELECT EPAR.IDE_GEEDP,   " + " EMP.APELLIDO_PATERNO_GTEMP || ' ' ||      " + " (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||     "
				+ " EMP.PRIMER_NOMBRE_GTEMP || ' ' ||     " + " (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS ,detalle_geare   " + " FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR     "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP    " + " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU     " + " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP     "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE where epar.ide_gtemp =   " + utilitario.getVariable("IDE_GTEMP") + " order by APELLIDO_PATERNO_GTEMP;   ");

		if (solicitudVacaciones.getIdeAsmot().getIdeAsmot() == 6) {
			parametros.put("personal", "");
			parametros.put("calamidad", "");
			parametros.put("enfermedad", "X");
		} else {
			if (solicitudVacaciones.getIdeAsmot().getIdeAsmot() == 2) {
				parametros.put("personal", "");
				parametros.put("calamidad", "X");
				parametros.put("enfermedad", "");
			} else {
				parametros.put("personal", "X");
				parametros.put("calamidad", "");
				parametros.put("enfermedad", "");

			}
		}

		parametros.put("fecha_solicitud", utilitario.getFormatoFecha(solicitudVacaciones.getFechaSolicitudAspvh()));
		parametros.put("nombres", miUsuario.getValor("NOMBRES_APELLIDOS"));
		parametros.put("area", miUsuario.getValor("detalle_geare"));

		TablaGenerica miJefe = utilitario.consultar("SELECT EPAR.IDE_GEEDP,   " + " EMP.APELLIDO_PATERNO_GTEMP || ' ' ||      " + " (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||     "
				+ " EMP.PRIMER_NOMBRE_GTEMP || ' ' ||     " + " (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS ,detalle_geare   " + " FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR     "
				+ " LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP    " + " LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU     " + " LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP     "
				+ " LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE where epar.IDE_GEEDP =   " + solicitudVacaciones.getIdeGeedp().getIdeGeedp() + " order by APELLIDO_PATERNO_GTEMP;   ");

		parametros.put("jefe", miJefe.getValor("NOMBRES_APELLIDOS"));
		parametros.put("fecha_inicio", utilitario.getFormatoFecha(solicitudVacaciones.getFechaDesdeAspvh()));
		parametros.put("fecha_fin", utilitario.getFormatoFecha(solicitudVacaciones.getFechaHastaAspvh()));
		parametros.put("hora_inicio", hora_inicial_reporte);
		parametros.put("hora_fin", hora_final_reporte);
		parametros.put("numero_horas", nuemro_horas_reporte);

		System.out.println("personal" + "X");
		System.out.println("calamidad" + "X");
		System.out.println("enfermedad" + "X");

		System.out.println("fecha_solicitud" + utilitario.getFormatoFecha(solicitudVacaciones.getFechaSolicitudAspvh()));
		System.out.println("nombres" + miUsuario.getValor("NOMBRES_APELLIDOS"));
		System.out.println("area" + miUsuario.getValor("detalle_geare"));
		System.out.println("jefe" + miJefe.getValor("NOMBRES_APELLIDOS"));
		System.out.println("fecha_incio" + utilitario.getFormatoFecha(solicitudVacaciones.getFechaDesdeAspvh()));
		System.out.println("fecha_fin" + utilitario.getFormatoFecha(solicitudVacaciones.getFechaHastaAspvh()));
		System.out.println("hora_inicio" + hora_inicial_reporte);
		System.out.println("hora_fin" + hora_final_reporte);
		System.out.println("numero_horas" + nuemro_horas_reporte);

		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_asistencia/permiso.jasper"));

		String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_asistencia/permiso.jasper");
		jasperPrint = JasperFillManager.fillReport(report, parametros, new net.sf.jasperreports.engine.JREmptyDataSource(1));
	}
*/
	public int getDiasEmpleadoXAnio(int nde, int nda) {
		if (nde < 360) {
			return nde;
		}
		int nuevo_calculo = nde - nda;
		if (nuevo_calculo >= 1 && nuevo_calculo <= 5) {
			nde = nda;
			return nde;
		} else {
			BigDecimal nuevo_valor_nde = new BigDecimal(nde / nda);
			if (nuevo_valor_nde.intValue() >= 1) {
				int valor_calcula = nuevo_valor_nde.intValue() * 5;
				nde = nde - valor_calcula;
				return nde;
			}

		}
		return nde;

	}

	public AsiPermisosVacacionHext getSolicitudVacaciones() {
		return solicitudVacaciones;
	}

	public void setSolicitudVacaciones(AsiPermisosVacacionHext solicitudVacaciones) {
		this.solicitudVacaciones = solicitudVacaciones;
	}

	public List getListaPermisos() {
		return listaPermisos;
	}

	public void setListaPermisos(List listaMotivos) {
		this.listaPermisos = listaMotivos;
	}

	public List getListaJefesInmediatos() {
		return listaJefesInmediatos;
	}

	public void setListaJefesInmediatos(List listaJefes) {
		this.listaJefesInmediatos = listaJefes;
	}

	public List getListaAnios() {
		return listaAnios;
	}

	public void setListaAnios(List listaAnios) {
		this.listaAnios = listaAnios;
	}

	public List getListaMeses() {
		return listaMeses;
	}

	public void setListaMeses(List listaMeses) {
		this.listaMeses = listaMeses;
	}

	public List<AsiPermisosVacacionHext> getListaSolicitudes() {
		return listaSolicitudes;
	}

	public void setListaSolicitudes(List<AsiPermisosVacacionHext> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

	public String getStrOpcion() {
		return strOpcion;
	}

	public void setStrOpcion(String strOpcion) {
		this.strOpcion = strOpcion;
	}

	public Object getObjDiasVacacionesActiva() {
		return objDiasVacacionesActiva;
	}

	public void setObjDiasVacacionesActiva(Object objDiasVacacionesActiva) {
		this.objDiasVacacionesActiva = objDiasVacacionesActiva;
	}

	public List getLisResumenVacaciones() {
		return lisResumenVacaciones;
	}

	public void setLisResumenVacaciones(List lisResumenVacaciones) {
		this.lisResumenVacaciones = lisResumenVacaciones;
	}

	public Conexion getConexion() {
		Conexion conexion = (Conexion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
		return conexion;
	}

	public String getStrPathReporte() {
		return strPathReporte;
	}

	public void setStrPathReporte(String strPathReporte) {
		this.strPathReporte = strPathReporte;
	}

	public Map getParametros() {
		return parametros;
	}

	public void setParametros(Map parametros) {
		this.parametros = parametros;
	}

	public String getURL() {
		ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
		String path = request.getRequestURL() + "";
		path = path.substring(0, path.lastIndexOf("/"));
		if (path.indexOf("portal") > 0) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}

	public boolean isAplica_dias() {
		return aplica_dias;
	}

	public void setAplica_dias(boolean aplica_dias) {
		this.aplica_dias = aplica_dias;
	}

	public Boolean getEnabledDias() {
		return enabledDias;
	}

	public void setEnabledDias(Boolean enabledDias) {
		this.enabledDias = enabledDias;
	}

	public boolean getPuedeGuardarSolicitud() {
		//System.out.println("getPuedeGuardarSolicitud...." + this.puedeGuardarSolicitud);
		return puedeGuardarSolicitud;
	}

	public void setPuedeGuardarSolicitud(boolean puedeGuardarSolicitud) {
		this.puedeGuardarSolicitud = puedeGuardarSolicitud;
	}

	public List<AsiPermisosVacacionHext> getListaSolicitudesRecibidas() {
		return listaSolicitudesRecibidas;
	}

	public void setListaSolicitudesRecibidas(List<AsiPermisosVacacionHext> listaSolicitudesRecibidas) {
		this.listaSolicitudesRecibidas = listaSolicitudesRecibidas;
	}

	public List getListaJustificacion() {
		return listaJustificacion;
	}

	public void setListaJustificacion(List listaJustificacion) {
		this.listaJustificacion = listaJustificacion;
	}

	public AsiPermisoJustificacion getSolicitudJustificacion() {
		return solicitudJustificacion;
	}

	public void setSolicitudJustificacion(AsiPermisoJustificacion solicitudJustificacion) {
		this.solicitudJustificacion = solicitudJustificacion;
	}

	public String getEstadoJefe() {
		return estadoJefe;
	}

	public void setEstadoJefe(String estadoJefe) {
		this.estadoJefe = estadoJefe;
	}

	public double getDias_pendientes() {
		return dias_pendientes;
	}

	public void setDias_pendientes(double dias_pendientes) {
		this.dias_pendientes = dias_pendientes;
	}

	public List getLisVacacionesEmpleados() {
		return lisVacacionesEmpleados;
	}

	public void setLisVacacionesEmpleados(List lisVacacionesEmpleados) {
		this.lisVacacionesEmpleados = lisVacacionesEmpleados;
	}

	public Object getObjMatriz() {
		return objMatriz;
	}

	public void setObjMatriz(Object objMatriz) {
		this.objMatriz = objMatriz;
	}

	public Double[] getObj() {
		return obj;
	}

	public void setObj(Double[] obj) {
		this.obj = obj;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Double[]> getMatriz() {
		return matriz;
	}

	public void setMatriz(List<Double[]> matriz) {
		this.matriz = matriz;
	}

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public List<String[]> getLisPeriodosEmpleado() {
		return lisPeriodosEmpleado;
	}

	public void setLisPeriodosEmpleado(List<String[]> lisPeriodosEmpleado) {
		this.lisPeriodosEmpleado = lisPeriodosEmpleado;
	}

	public double getNumeroDiasTomados() {
		return numeroDiasTomados;
	}

	public void setNumeroDiasTomados(double numeroDiasTomados) {
		this.numeroDiasTomados = numeroDiasTomados;
	}

	public double getSumatotal_vacaciones() {
		return sumatotal_vacaciones;
	}

	public void setSumatotal_vacaciones(double sumatotal_vacaciones) {
		this.sumatotal_vacaciones = sumatotal_vacaciones;
	}

	public double getResultado_detalle() {
		return resultado_detalle;
	}

	public void setResultado_detalle(double resultado_detalle) {
		this.resultado_detalle = resultado_detalle;
	}

	public String getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(String fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public GthEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(GthEmpleado empleado) {
		this.empleado = empleado;
	}

	public int getAcumulaFinesSemana() {
		return acumulaFinesSemana;
	}

	public void setAcumulaFinesSemana(int acumulaFinesSemana) {
		this.acumulaFinesSemana = acumulaFinesSemana;
	}

	public boolean isAplicavacacion() {
		return aplicavacacion;
	}

	public void setAplicavacacion(boolean aplicavacacion) {
		this.aplicavacacion = aplicavacacion;
	}

	public Boolean getEsvacacion() {
		return esvacacion;
	}

	public void setEsvacacion(Boolean esvacacion) {
		this.esvacacion = esvacacion;
	}

	public String getDescripcionMotivo() {
		return descripcionMotivo;
	}

	public void setDescripcionMotivo(String descripcionMotivo) {
		this.descripcionMotivo = descripcionMotivo;
	}

	public Integer getNumMaximoDiasPermiso() {
		return numMaximoDiasPermiso;
	}

	public void setNumMaximoDiasPermiso(Integer numMaximoDiasPermiso) {
		this.numMaximoDiasPermiso = numMaximoDiasPermiso;
	}

	public Integer getTiempo_permiso() {
		return tiempo_permiso;
	}

	public void setTiempo_permiso(Integer tiempo_permiso) {
		this.tiempo_permiso = tiempo_permiso;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public int getNumeroInicioFinesSemana() {
		return numeroInicioFinesSemana;
	}

	public void setNumeroInicioFinesSemana(int numeroInicioFinesSemana) {
		this.numeroInicioFinesSemana = numeroInicioFinesSemana;
	}

	public int getNumeroInicioFinesSemanaSolicitados() {
		return numeroInicioFinesSemanaSolicitados;
	}

	public void setNumeroInicioFinesSemanaSolicitados(int numeroInicioFinesSemanaSolicitados) {
		this.numeroInicioFinesSemanaSolicitados = numeroInicioFinesSemanaSolicitados;
	}

	public int getNumeroInicioFinesSemanaPendientes() {
		return numeroInicioFinesSemanaPendientes;
	}

	public void setNumeroInicioFinesSemanaPendientes(int numeroInicioFinesSemanaPendientes) {
		this.numeroInicioFinesSemanaPendientes = numeroInicioFinesSemanaPendientes;
	}

	public List getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(List listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public UploadedFile getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(UploadedFile adjunto) {
		//if (adjunto.getSize() != 0)
		this.adjunto = adjunto;
	}

	public TablaGenerica getEmpledoPartida(String ide_usua) {
		return utilitario.consultar("SELECT ide_geedp,E.ide_gtemp,d.ide_gegro,d.ide_gecaf,d.ide_geare FROM SIS_USUARIO U" + " INNER JOIN gth_empleado E ON E.IDE_GTEMP = U.IDE_GTEMP"
				+ " INNER JOIN gen_empleados_departamento_par D ON D.IDE_GTEMP = E.IDE_GTEMP" + " WHERE IDE_USUA= " + ide_usua + " AND activo_geedp=true");
	}

	public double getNumeroDiasTomadosTemporal() {
		return numeroDiasTomadosTemporal;
	}

	public void setNumeroDiasTomadosTemporal(double numeroDiasTomadosTemporal) {
		this.numeroDiasTomadosTemporal = numeroDiasTomadosTemporal;
	}

	public Integer getNdeInicialFechaCalculoDiasTomados() {
		return ndeInicialFechaCalculoDiasTomados;
	}

	public void setNdeInicialFechaCalculoDiasTomados(Integer ndeInicialFechaCalculoDiasTomados) {
		this.ndeInicialFechaCalculoDiasTomados = ndeInicialFechaCalculoDiasTomados;
	}

	public double getSumatotal_vacacionesCalculoInicial() {
		return sumatotal_vacacionesCalculoInicial;
	}

	public void setSumatotal_vacacionesCalculoInicial(double sumatotal_vacacionesCalculoInicial) {
		this.sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial;
	}

	public int getDiasSabado(Date FechaSolicitud, int nro_dias_aspvh) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = sdf.format(FechaSolicitud);

		int dias_asignados = nro_dias_aspvh;
		int calculo_sabados = 0;
		int calculo_domingos = 0;
		for (int i = 0; i < dias_asignados; i++) {
			StringTokenizer st = new StringTokenizer(dateInString, "-");
			String anio = st.nextToken();
			String mes = st.nextToken();
			String dia = st.nextToken();
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1, Integer.parseInt(dia) + i);
			String fin_semana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
			if (fin_semana.equalsIgnoreCase("sábado")) {
				calculo_sabados++;
			}
			if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}
		return calculo_sabados;
	}

	public int getDiasDomingo(Date FechaSolicitud, int nro_dias_aspvh) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = sdf.format(FechaSolicitud);
		int dias_asignados = nro_dias_aspvh;
		int calculo_sabados = 0;
		int calculo_domingos = 0;
		for (int i = 0; i < dias_asignados; i++) {
			StringTokenizer st = new StringTokenizer(dateInString, "-");
			String anio = st.nextToken();
			String mes = st.nextToken();
			String dia = st.nextToken();
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1, Integer.parseInt(dia) + i);
			String fin_semana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
			if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}
		return calculo_domingos;
	}

	public double getDiasPnedientesX15Dias(double dias_pendientes) {
		double valor1 = 0.0;
		valor1 = dias_pendientes + 15;
		int entero1 = (int) ((valor1 / 5));
		// Dato que se guarda en la base de datos
		int calcula_dias_vacacion1 = (((entero1 * 5) - (entero1 * 2)) + (((int) (valor1)) - (entero1 * 5)));
		int enteroDias1 = (int) ((calcula_dias_vacacion1 / 5));
		int calculoDias1 = enteroDias1 * 2;
		int enteroSuma1 = calcula_dias_vacacion1 + calculoDias1;
		// Valor guardo en campo nro_dias_ajuste_asvac
		double calculaParteDecimal1 = valor1 - enteroSuma1;
		return calcula_dias_vacacion1;
	}



public String horaFinal() throws IOException{	
	String str_hora_final="";
	try {
		Calendar fechaFinal = Calendar.getInstance();
		fechaFinal.setTime(solicitudVacaciones.getHoraHastaAspvh());
		
		if ((fechaFinal.getTime().getHours()<10)&&(fechaFinal.getTime().getMinutes()<10)) {
			str_hora_final = "0"+fechaFinal.getTime().getHours() + ":0" + fechaFinal.getTime().getMinutes() + ":00";
			return str_hora_final;
		}
		
		
		if ((fechaFinal.getTime().getHours()<10)&&(fechaFinal.getTime().getMinutes()>=10)) {
			str_hora_final ="0"+fechaFinal.getTime().getHours() + ":" + fechaFinal.getTime().getMinutes() + ":00";
			return str_hora_final;
		}
		
		
		if ((fechaFinal.getTime().getHours()>=10)&&(fechaFinal.getTime().getMinutes()>=10)) {
			str_hora_final =fechaFinal.getTime().getHours() + ":" + fechaFinal.getTime().getMinutes() + ":00";
			return str_hora_final;
		}
		
		if ((fechaFinal.getTime().getHours()>=10)&&(fechaFinal.getTime().getMinutes()<10)) {
			str_hora_final = fechaFinal.getTime().getHours() + ":0" + fechaFinal.getTime().getMinutes() + ":00";
			return str_hora_final;
		}
		return str_hora_final;
	} catch (Exception e) {
		System.out.println("Error en obtencion de la horaFinal(): "+e.getMessage());
		solicitudVacaciones.setNroHorasAspvh(null);
	}
return str_hora_final;
}
	
	
	 public String  horaInical() throws IOException{
	    	
		    String str_hora_inicial="";
			try {
				Calendar fechaInicial = Calendar.getInstance();
				fechaInicial.setTime(solicitudVacaciones.getHoraDesdeAspvh());
		
				
				if ((fechaInicial.getTime().getHours()<10)&&(fechaInicial.getTime().getMinutes()<10)) {
					str_hora_inicial = "0"+fechaInicial.getTime().getHours() + ":0" + fechaInicial.getTime().getMinutes() + ":00";
					return str_hora_inicial;
				}
				
				
				if ((fechaInicial.getTime().getHours()<10)&&(fechaInicial.getTime().getMinutes()>=10)) {
					str_hora_inicial ="0"+fechaInicial.getTime().getHours() + ":" + fechaInicial.getTime().getMinutes() + ":00";
					return str_hora_inicial;
				}
				
				
				if ((fechaInicial.getTime().getHours()>=10)&&(fechaInicial.getTime().getMinutes()>=10)) {
				str_hora_inicial = fechaInicial.getTime().getHours() + ":" + fechaInicial.getTime().getMinutes() + ":00";
					return str_hora_inicial;
				}
				
				if ((fechaInicial.getTime().getHours()>=10)&&(fechaInicial.getTime().getMinutes()<10)) {
					str_hora_inicial = fechaInicial.getTime().getHours() + ":0" + fechaInicial.getTime().getMinutes() + ":00";
					return str_hora_inicial;
				}
				return str_hora_inicial;

			} catch (Exception e) {
				System.out.println("Error en  la horaInical() : "+e.getMessage());
				solicitudVacaciones.setNroHorasAspvh(null);
			}
			return str_hora_inicial;
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
	    
	    private String getHHmmss(Date hora){
		    DateFormat df = new SimpleDateFormat("HH:mm:ss");
		    return df.format(hora);
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


	
public void inicializar(){
	
	tab_novedad=null;
	 nda=0;
	 numeroDiasVacacionXAnio = 0.0;
	ide_gttem=0;
	 nde=0;
	ndeInicialFechaCalculoDiasTomados=0;
	numeroDiasTomados=0.0;
	Object objMatriz;
	List<Double[]> matriz;
	Double[] obj = new Double[5];
	date = null;
	List<String[]> lisPeriodosEmpleado;
	 Fecha = "";
	sumatotal_vacaciones = 0.0;
	sumatotal_vacacionesCalculoInicial = 0.0;
	dias_acumulados_inicial = 0;
	resultado_detalle = 0.0;
	fecha_ingreso = "";
	anterior_dias_pendientes=0;
	dias_sumados_aplicados_vacacion=0;

	p_asi_dias_max_vacaciones_losep=0;
	p_asi_dias_max_vacaciones_codigo_trabajo=0;
	p_asi_dias_anio=0;

	// Calculo de de dias tomados por el empleado
	numeroDiasTomadosInicial=0.0;
	totalNumeroDiasTomadosInicial=0.0;
	diasPendientesInicialAjuste=0.0;
	nroDiasAjustePeriodo=0.0;

	acumulaFinesSemana=0;
    apruebaJefeInmediato="";
	// crear en parametros
	calculo_dias_max_aplica = 0.0;
	diasGraciaPuedeTomar = 0.0;
	numeroInicioFinesSemana = 0;
	numeroInicioFinesSemanaSolicitados = 0;
	numeroInicioFinesSemanaPendientes = 0;

	// Variables de Correo
	int obtieneNroDias;
	String nombreTipoSolicitud;
	double nroHoras;
	int tipo_solicitud;
	String ide_aspvh_correo;
	String motivo;

	 GthEmpleado empleado;

	String strOpcion = "1";
	Utilitario utilitario = new Utilitario();

	AsiPermisosVacacionHext solicitudVacaciones;
	AsiPermisoJustificacion solicitudJustificacion;
	AsiVacacion solicitudVacacion;
	ConBiometricoMarcaciones biometrico;
	numeroSumarFinesSemana = 0;

	/**
	 * IDE_ASMOT,DETALLE_ASMOT,APLICA_VACACIONES_ASMOT,DESCRIPCION_ASMOT,
	 * NUM_MAX_DIAS_ASMOT,esvacacion Obj
	 * [IDE_ASMOT,DETALLE_ASMOT,TIEMPO_PERMISO_ASMOT
	 * ,APLICA_HORAS_DIAS_ASMOT,DESCRIPCION_ASMOT
	 * ,NUM_MAX_DIAS_ASMOT,ARCHIVO_ADJUNTO_ASPVH]
	 */
	 List listaMotivos;
	List listaPermisos;
	 List listaAnios;
	 List listaMeses;
	 List listaJefesInmediatos;
	List listaJustificacion;

	List<AsiPermisosVacacionHext> listaSolicitudes;
	 List<AsiPermisosVacacionHext> listaSolicitudesRecibidas;
	 List<AsiPermisoJustificacion> listaJustificaciones;
	boolean aplica_dias;
	 boolean no_aplica_dias;
	 String estadoJefe = "EN CURSO";
	
	boolean aplicavacacion;
	 Boolean esvacacion;
	String descripcionMotivo;
	Integer numMaximoDiasPermiso;
	Integer tiempo_permiso;
	Integer numMaxRegistroPermiso=0,numMaxAprobacionPermiso=0;

	// variables para cambiar las horas de ingreso y salida
	// private Integer totalSegundosHoraEntrada = 28800;
	// private Integer totalSegundosHoraSalida = 61200;
	Integer totalSegundosHoraEntrada = 0;
	Integer totalSegundosHoraSalida = 612000;

	Boolean enabledDias = true;
	boolean puedeGuardarSolicitud = true;
	// private double diasTotales =0;
	int numeroFinesSemana = 0;
	int numeroFinesSemanaTotal = 0;
	int descuentoFinesSemana = 0;
	double numeroDiasTomadosTemporal;

	// Ajuste inicial de dias

	double calculaParteDecimal = 0.0;
	int calcula_dias_vacacion = 0;
	int enteroDias = 0;
	double valor = 0.0;
	int calculoDias = enteroDias = 0;
	int enteroSuma = 0;

	 Object objDiasVacacionesActiva;
	List lisResumenVacaciones;
	List lisVacacionesEmpleados;
	 Map parametros = new HashMap();
	 JasperPrint jasperPrint;
	String strPathReporte;
	String hora_inicial_reporte = "";
	String hora_final_reporte = "";
	String nuemro_horas_reporte = "";
	Connection conn;
	double totalNumeroDiasAjuste;
	double numeroDiasTomadosFinSemana;
	}
	    


public TablaGenerica getTab_novedad() {
	return tab_novedad;
}



public void setTab_novedad(TablaGenerica tab_novedad) {
	this.tab_novedad = tab_novedad;
}
	    


public static String ucFirst(String str) {
    if (str.isEmpty()) {
        return str;            
    } else {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1); 
    }
}


/*
 * Metodo recibe el empleado y retorna el periodo y los dias pendientes a vacaciones por cambio de contrato
 */
public String detalleVacaciones(String ide_gtemp){
	TablaGenerica tabVacaciones=utilitario.consultar("select IDE_ASVAC,IDE_GTEMP,ACTIVO_ASVAC from asi_vacacion where ide_gtemp="+ide_gtemp+" ORDER BY IDE_ASVAC ASC");
	String valor="";
	if (tabVacaciones.getTotalFilas()>0) {
		for (int i = 0; i <tabVacaciones.getTotalFilas() ; i++) {
			if (tabVacaciones.getValor(i,"ACTIVO_ASVAC").equals("false")){
				TablaGenerica tabDetalleVacacion=utilitario.consultar("select ide_asvre,ide_asvac,periodo_asvre, "
				+ "nro_dias_vacacion_asvre,nro_dias_tomados_asvre,nro_dias_pendientes_asvre, "
				+ "activo_asvre,ide_periodo_asvre "
				+ "from asi_vacacion_resumen_empleado where ide_asvac="+tabVacaciones.getValor("IDE_ASVAC")+" "
				+ "ORDER BY ide_periodo_asvre DESC "
				+ "limit 1");	

				if (tabDetalleVacacion.getTotalFilas()>0) {
					valor=tabDetalleVacacion.getValor("periodo_asvre")+","+tabDetalleVacacion.getValor("nro_dias_pendientes_asvre");
					return valor;	
				}else {
					return valor;
				}
		
		}else {
			return valor;
		}	
			
		}
	
	}else {
		return valor;
	}

return valor;
}






public String getPart1() {
	return part1;
}



public void setPart1(String part1) {
	this.part1 = part1;
}



public String getPart2() {
	return part2;
}



public void setPart2(String part2) {
	this.part2 = part2;
}



public int getNumCol() {
	return numCol;
}



public void setNumCol(int numCol) {
	this.numCol = numCol;
}

public boolean validaDiasIngresoPermiso(Date fechaSolicitud,int diasSolicitud,int tipoPermiso,String hora){
	boolean valor=false;
	Date  fecha_actual=null,fechaFinRango=null;
	Date fecha_comparacion=utilitario.DeStringADate(utilitario.getFechaActual());
	
	if (fechaSolicitud.toString()==null || fechaSolicitud.toString().equals("") || fechaSolicitud.toString().isEmpty()) {
		valor=false;
		return valor;
	}else {
		fecha_actual=fechaSolicitud;
		//Date valorDiasSinFindeSemana=sumarDiasFechaSinFinSemana(fechaSolicitud, Integer.parseInt(utilitario.getVariable("p_dias_limite_ingreso_permiso")));
		Date valorDiasSinFindeSemana=null;
				if (tipoPermiso==33) {
					String fecPersonal="";
					//sumarDiasFechaSinFinSemana(fechaSolicitud, -1);	
				//	valorDiasSinFindeSemana=sumarRestarHorasFecha(fechaSolicitud, -diasSolicitud);
					if (tipoPermiso==14 && !hora.equals("00:00:00")) {
					//fecha_actual=utilitario.DeStringADate(utilitario.DeDateAString(fecha_actual)+" "+hora);	
					fecha_comparacion=restarHorasFecha(getFechaAsyyyyMMddHHmmss(utilitario.DeDateAString(fecha_comparacion)+" "+utilitario.getHoraActual()), diasSolicitud);
					valorDiasSinFindeSemana=getFechaAsyyyyMMddHHmmss(utilitario.DeDateAString(fecha_actual)+" "+hora);
					}else {
					valorDiasSinFindeSemana=restarHorasFecha(fecha_actual, diasSolicitud);
					}
					if (valorDiasSinFindeSemana.compareTo(fecha_comparacion)>= 0) {
						//	if (fecha_actual.compareTo(valorDiasSinFindeSemana)<= 0) {
						System.out.println("Permisex registrado");
						
					valor=true;
				}else {
					valor=false;
				}
				
				
		
				}else {
				//	valorDiasSinFindeSemana=restarDiasFechaSinFinSemana(fechaSolicitud, (diasSolicitud/24));	//CONSULTA MEDICA
					valorDiasSinFindeSemana=sumarRestarHorasFecha(fecha_actual, diasSolicitud);		
					//if (tipoPermiso==14){
						if ( valorDiasSinFindeSemana.compareTo(fecha_comparacion)>= 0) {
							System.out.println("Permisex registrado");
		
			valor=true;
		}else {
							valor=false;                                                                                                                                                                                                                                                                                                                                							valor=false;
		}
		
					
					//}
		}
		//int valorDiferencia=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fecha_actual), valorDiasSinFindeSemana);
		

		
	}
	return valor;
	
}

int getDiasSabado(String FechaSolicitud,int nro_dias_aspvh){ 	
	String dateInString = FechaSolicitud;
    
    
int dias_asignados=nro_dias_aspvh;		
int calculo_sabados=0;
int calculo_domingos=0; 		  
for (int i = 0; i < dias_asignados; i++) {
	
		   

StringTokenizer st = new StringTokenizer(dateInString, "-");
		    String anio = st.nextToken(); 
		    String mes = st.nextToken(); 
		    String dia = st.nextToken();		
	
		    Calendar cal = GregorianCalendar.getInstance(); 
		    cal.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1 , Integer.parseInt(dia)+i); 
		    //System.out.println(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES")));
		    
		    String fin_semana=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
		    
		    if (fin_semana.equalsIgnoreCase("sábado")) {
				calculo_sabados++;
			}
		    
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
		    return calculo_sabados;
}




public int getDiasDomingo(String FechaSolicitud,int nro_dias_aspvh){ 	
	String dateInString = FechaSolicitud;
    
    
int dias_asignados=nro_dias_aspvh;		
int calculo_sabados=0;
int calculo_domingos=0; 		  
for (int i = 0; i < dias_asignados; i++) {
	
StringTokenizer st = new StringTokenizer(dateInString, "-");
		    String anio = st.nextToken(); 
		    String mes = st.nextToken(); 
		    String dia = st.nextToken();		
	
		    Calendar cal = GregorianCalendar.getInstance(); 
		    cal.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1 , Integer.parseInt(dia)+i); 
		    //System.out.println(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES")));
		    
		    String fin_semana=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ES"));
		    if (fin_semana.equalsIgnoreCase("domingo")) {
				calculo_domingos++;
			}
		}		    
		    
		    return calculo_domingos;
}



public double getNumeroDiasTomadosTemp() {
	return numeroDiasTomadosTemp;
}



public void setNumeroDiasTomadosTemp(double numeroDiasTomadosTemp) {
	this.numeroDiasTomadosTemp = numeroDiasTomadosTemp;
}



public String getStr_dias_pendientes() {
	return str_dias_pendientes;
}



public void setStr_dias_pendientes(String str_dias_pendientes) {
	this.str_dias_pendientes = str_dias_pendientes;
}

public int getDias360(String periodoNuevoEntrada, String periodoNuevoSalida){

	int mesIniLaborados,mesFinLaborados,diaInicioLaborados,diaFinLaborados,anioInicioLaborados,anioFinLaborados;
	diaFinLaborados=utilitario.getDia(periodoNuevoSalida);
	diaInicioLaborados=utilitario.getDia(periodoNuevoEntrada);
	mesFinLaborados=utilitario.getMes(periodoNuevoSalida);
	mesIniLaborados=utilitario.getMes(periodoNuevoEntrada);
	anioFinLaborados=utilitario.getAnio(periodoNuevoSalida);
	anioInicioLaborados=utilitario.getAnio(periodoNuevoEntrada);

	int valorAnio,valorMes,valorDia;
	valorAnio=anioFinLaborados-anioInicioLaborados;
	valorMes=mesFinLaborados-mesIniLaborados;
	valorDia=diaFinLaborados-diaInicioLaborados;
	int formula=((valorAnio*360)+((valorMes)*30)+(valorDia))+1;
	return formula;
}



public int getDias_final_extra() {
	return dias_final_extra;
}



public void setDias_final_extra(int dias_final_extra) {
	this.dias_final_extra = dias_final_extra;
}



public String getVer_dias_extra() {
	return ver_dias_extra;
}



public String getFechaHoraMarcacion() {
	return fechaHoraMarcacion;
}



public void setFechaHoraMarcacion(String fechaHoraMarcacion) {
	this.fechaHoraMarcacion = fechaHoraMarcacion;
}






public void setVer_dias_extra(String ver_dias_extra) {
	this.ver_dias_extra = ver_dias_extra;
}

public TablaGenerica retornaTablaAccionResumenVacacion(String fec_ini,String fec_fin,String ide_gtemp){
	TablaGenerica tab_acciones=utilitario.consultar(" select ide_geedp,rmu_geedp,fecha_geedp,fecha_liquidacion_geedp,activo_geedp FROM gen_empleados_departamento_par where "
			+ "fecha_geedp between '"+fec_ini+"' and '"+fec_fin+"' and ide_gtemp="+ide_gtemp+" order by ide_geedp asc");
	return tab_acciones;
} 


public void guardarMarcacion(ActionEvent evt) {
	String ide_marcacion="";
	try {
	TablaGenerica tab_id_marcacion=utilitario.consultar("SELECT ide_gtemp,documento_identidad_gtemp, "
			+ "tarjeta_marcacion_gtemp, activo_gtemp  "
			+ "FROM gth_empleado   "
			+ "where ide_gtemp="+empleado.getIdeGtemp());
	ide_marcacion=tab_id_marcacion.getValor("tarjeta_marcacion_gtemp");

			fechaEvento=utilitario.getFechaHoraActual();
			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			if (fechaEvento==null || fechaEvento.equals("") || fechaEvento.isEmpty()) {
				utilitario.agregarMensajeInfo("Sin fecha ni hora", "No se puede realizar marcacion");
				return;
			}else{
				biometrico.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
				biometrico.setUsuarioIngre(utilitario.getVariable("IDE_USUA"));
				Timestamp timestamp = Timestamp.valueOf(utilitario.getFechaHoraActual());
				biometrico.setHoraIngre(timestamp);
				biometrico.setTeletrabajoCobim(true);
				biometrico.setIdeCorel(new ConReloj(15));
			}

		SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fecha = new Date();
		java.util.Date utilDate = new java.util.Date(); //fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fechaDate = new Date();
		fechaDate = df.parse(utilitario.getFechaHoraActual());	

		Timestamp ts = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date temp =dateFormat.parse(utilitario.getFechaHoraActual());
			  biometrico.setFechaEventoCobim(new Timestamp(temp.getTime()));
	          biometrico.setIdePersonaCobim(""+ide_marcacion);
	          intCodigoEnviado=randomCodigo();
	          biometrico.setIdeCodigoValidadorCobim(intCodigoEnviado);
	          String str_mensaje = "";
	          if (validaIngreso==2) {
				return;
				}else{
					str_mensaje = servicioVacaciones.guardarMarcacionesBiometrico(biometrico);
					CorreoMarcaciones(utilitario.getVariable("IDE_GTEMP"),intCodigoEnviado,biometrico.getFechaEventoCobim().toString());
					System.out.println("str_mensaje: " + str_mensaje);}
				if (str_mensaje.isEmpty()) {
					utilitario.agregarMensaje("Correo Electronico enviado correctamente", "Se ha remitido la marcacion realizada a su correo");
					System.out.println("Codigo remitido al empleado "+utilitario.getVariable("EMP_SESSION"));
					try {
						cargarDatos();		
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("ERROR CARGAR DATOS");
					}
					
				} else {
					utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
					}
				//Correo(solicitudVacaciones.getIdeGtemp().getIdeGtemp());
	} catch (ParseException e) {
	// TODO Auto-generated catch block
	System.out.println("Eror en metodo guardar");
	}
}



public ConBiometricoMarcaciones getBiometrico() {
	return biometrico;
}



public void setBiometrico(ConBiometricoMarcaciones biometrico) {
	this.biometrico = biometrico;
}



public String getFechaEvento() {
	return fechaEvento;
}



public void setFechaEvento(String fechaEvento) {
	this.fechaEvento = fechaEvento;
}


public void cargarDatosMarcaciones() {
	fechaEvento="";
}



public List<ConBiometricoMarcaciones> getListaMarcaciones() {
	return listaMarcaciones;
}



public void setListaMarcaciones(List<ConBiometricoMarcaciones> listaMarcaciones) {
	this.listaMarcaciones = listaMarcaciones;
}

/*public TimeZone getTimeZone() {
	TimeZone retornaValor=TimeZone.getDefault();
	return retornaValor;
}*/



public TimeZone getRetornaValor() {
	return retornaValor;
}



public void setRetornaValor(TimeZone retornaValor) {
	this.retornaValor = retornaValor;
}

public int randomCodigo() {
    int min = 0;
    int max = 10000;
    //Generate random double value from 50 to 100 
    int random_int = (int)(Math.random() * (max - min + 1) + min);
    //System.out.println(random_int);
    return random_int;
  }



public Integer getIntCodigoEnviado() {
	return intCodigoEnviado;
}



public void setIntCodigoEnviado(Integer intCodigoEnviado) {
	this.intCodigoEnviado = intCodigoEnviado;
}


public int validaIngresoMarcacion(String ide_marcacion){
	int retornaValor=0;
	Calendar calFechaHoraNueva=Calendar.getInstance();
    Calendar calFechaEvento=Calendar.getInstance();

	
	Date fechayhora=utilitario.DeStringADate(utilitario.getFechaHoraActual());
	Date fechayhoranueva;
	TablaGenerica tabMarcaciones = utilitario.consultar("SELECT ide_cobim, ide_corel, ide_persona_cobim, evento_reloj_cobim, "
			+ "fecha_evento_cobim, estatus_cobim, activo_cobim, usuario_ingre,  "
			+ "fecha_ingre, usuario_actua, hora_ingre, fecha_actua, hora_actua,  "
			+ "teletrabajo_cobim, ide_codigo_validador_cobim  "
			+ "FROM con_biometrico_marcaciones "
			+ "where ide_persona_cobim=trim('"+ide_marcacion+"') and  ide_corel=15 "
			//+ "and marcacion_validada_cobim=false "
			+ "and TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') ='"+utilitario.getFechaActual()+"' "
			+ "and activo_cobim=true "
			+ " order by ide_cobim desc "
			+ "limit 1");
	String fechaEvento="",fechaComparacion ="";

	if (tabMarcaciones.getTotalFilas()>0) {
		if (tabMarcaciones.getValor("fecha_evento_cobim")== null || tabMarcaciones.getValor("fecha_evento_cobim").equals("") || tabMarcaciones.getValor("fecha_evento_cobim").isEmpty()) {
			retornaValor=0;//Si no tiene ingreso realizado
		}else{
		
	//fechaEvento=tabMarcaciones.getValor("fecha_evento_cobim");
		
			
	try {
		fechaEvento=utilitario.getFechaHoraActual();
		fechayhoranueva=sumarRestarMinutosFecha(getFechaAsyyyyMMddHHmmss(tabMarcaciones.getValor("fecha_evento_cobim")),Integer.parseInt(utilitario.getVariable("p_tiempo_limite_marcacion")));
		//fechayhoranueva=sumarRestarHorasFecha(parseDate(fechaEvento), 15);
		//String  fechaHoraHorarioFinAlmuerzo=getFechaAsyyyyMMdd(fechayhoranueva);
		    	calFechaHoraNueva.setTime(fechayhoranueva);
		   		    calFechaEvento.setTime(getFechaAsyyyyMMddHHmmss(fechaEvento));
		    
		    
		//fechaComparacion =utilitario.DeDateAString(fechayhoranueva);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
	//if (fechaEvento.compareTo(fechaComparacion)<=0) {
	if (calFechaHoraNueva.compareTo(calFechaEvento) <= 0){
		retornaValor=1;//si tiene y ha pasado mas de 15 min
	}else{
		retornaValor=2;	//si tiene y ha pasado menos de 15 min
				}
	}//si la fecha del evento es null o vacia
	}//si hay marcacion 
	else{
		retornaValor=0;	
	}
return retornaValor;
	 
}


public int cambiarEstadoMarcacion(String ide_marcacion){
	int retornaValor=0;
	
	
	Date fechayhora=utilitario.DeStringADate(utilitario.getFechaHoraActual());
	Date fechayhoranueva=sumarRestarHorasFecha(fechayhora, 15);
	
	TablaGenerica tabMarcaciones = utilitario.consultar("SELECT ide_cobim, ide_corel, ide_persona_cobim, evento_reloj_cobim, "
			+ "fecha_evento_cobim, estatus_cobim, activo_cobim, usuario_ingre,  "
			+ "fecha_ingre, usuario_actua, hora_ingre, fecha_actua, hora_actua,  "
			+ "teletrabajo_cobim, ide_codigo_validador_cobim  "
			+ "FROM con_biometrico_marcaciones "
			+ "where ide_persona_cobim=trim("+ide_marcacion+") and  ide_corel=15 "
			//+ "and marcacion_validada_cobim=false "
			+ "and TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') ='"+utilitario.getFechaActual()+"' "
			+ "and activo_cobim=true "
			+ " order by ide_cobim desc "
			+ "limit 1");
	String fechaEvento="",fechaComparacion ="";

	if (tabMarcaciones.getTotalFilas()>0) {
		if (tabMarcaciones.getValor("marcacion_validada_cobim")== null || tabMarcaciones.getValor("marcacion_validada_cobim").equals("") || tabMarcaciones.getValor("marcacion_validada_cobim").isEmpty()) {
			retornaValor=0;
		}else{
			if (tabMarcaciones.getValor("marcacion_validada_cobim").equals("false") || tabMarcaciones.getValor("marcacion_validada_cobim").isEmpty()) {
				retornaValor=Integer.parseInt(tabMarcaciones.getValor("ide_cobim"));
			}else{
				retornaValor=1;
			}
				

		}
		}
	return retornaValor;
}



public Date sumarRestarHorasFecha(Date fecha, int minutos){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(fecha); // Configuramos la fecha que se recibe
     calendar.add(Calendar.HOUR, minutos);  // numero de horas a a�adir, o restar en caso de horas<0
     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas a�adidas

}

public Date restarHorasFecha(Date fecha, int minutos){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fecha); // Configuramos la fecha que se recibe
    calendar.add(Calendar.HOUR, -minutos);  // numero de horas a a�adir, o restar en caso de horas<0
    return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas a�adidas

}


public Boolean getEstadoMarcacion() {
	return estadoMarcacion;
}



public void setEstadoMarcacion(Boolean estadoMarcacion) {
	this.estadoMarcacion = estadoMarcacion;
}

public static Date parseDate(String date) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return sdf.parse(date);
}

//Sumar horas en fecha
public Date sumarRestarMinutosFecha(Date fecha, int horas){
Calendar calendar = Calendar.getInstance();
calendar.setTime(fecha); // Configuramos la fecha que se recibe
calendar.add(Calendar.MINUTE, horas);  // numero de horas a añadir, o restar en caso de horas<0
return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
	  }


public int getValidaIngreso() {
	return validaIngreso;
}


public void setValidaIngreso(int validaIngreso) {
	this.validaIngreso = validaIngreso;
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





public boolean isBanderaPermisoExcedido() {
	return banderaPermisoExcedido;
}



public void setBanderaPermisoExcedido(boolean banderaPermisoExcedido) {
	this.banderaPermisoExcedido = banderaPermisoExcedido;
}







public Date restarDiasFechaSinFinSemana(Date fch, int numeroDiasSumar) {
	
	Calendar fechaInicial = Calendar.getInstance();
	Calendar fechaInicialCalculo = Calendar.getInstance();

	fechaInicial.setTime(fch);
	int contador  = 1;
	while (contador <= numeroDiasSumar)
	{
		if (fechaInicial.get(Calendar.DAY_OF_WEEK) != 7 && fechaInicial.get(Calendar.DAY_OF_WEEK) != 6) {

			 contador++;

		}
			fechaInicial.add(Calendar.DATE, -1);

	}
	return fechaInicial.getTime();

}



public boolean isEnabledDiasHoras() {
	return enabledDiasHoras;
}



public void setEnabledDiasHoras(boolean enabledDiasHoras) {
	this.enabledDiasHoras = enabledDiasHoras;
}



public boolean isEnabledHasta() {
	return enabledHasta;
}



public void setEnabledHasta(boolean enabledHasta) {
	this.enabledHasta = enabledHasta;
}





public TablaGenerica getPartida(String IDE_GTEMP){
    return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GTEMP="+IDE_GTEMP+" ORDER BY IDE_GEEDP DESC  LIMIT 1 ");}



public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC,String fecha_ingreso_asvac,String fecha_finiquito_asvac){

	//			+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
		TablaGenerica tab_partida = getPartida(IDE_GEEDP);

	       
    	
		
       String dateInString = "";
       boolean activo_asvac=false;
	      int sumaValor=0;
       if (fecha_ingreso_asvac==null || fecha_ingreso_asvac.equals("") || fecha_ingreso_asvac.isEmpty()) {
    	   return 0;
       }else {
    	   dateInString=fecha_ingreso_asvac;
       }
       
      
       String fecha_finiquito=null;
       
       	 int diaFin=0;
    		 int mesFin=0; // 0 Enero, 11 Diciembre
    		 int anioFin=0;
	        
    
        
   		Date date = null;
   		Date dateFechaCalculoFiniquito = null;
    		/**
    		 * Le asigno a una variable de tipo date la fecha de ingreso
    		 */
    			
    /*		try {
    			date = sdf.parse(dateInString);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    	*/	
    		
   			
       	 try {
			if (fecha_finiquito_asvac==null || fecha_finiquito_asvac.isEmpty()){
			   		try {
			    			date = sdf.parse(dateInString);
			   		       } catch (ParseException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
	        		
	        		Calendar calendarFin = Calendar.getInstance();
	        		calendarFin.setTime(utilitario.getDate());
	        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		 anioFin = calendarFin.get(Calendar.YEAR);
			activo_asvac=true;
			
			}else{
			if (activo_asvac==false){
				   fecha_finiquito = fecha_finiquito_asvac;
				   
				  if (utilitario.getDia(fecha_finiquito)==31) {
					   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), -1));
										
				   }
				   
				   if (utilitario.getMes(fecha_finiquito)==2 || utilitario.getMes(fecha_finiquito)==02) {
					
			
				   if (utilitario.getDia(fecha_finiquito)==29) {
					   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
										sumaValor=1;
				   }
				   
				   
				   if (utilitario.getDia(fecha_finiquito)==28) {
					//fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
										sumaValor=2;
				   }
				   
					}
    		try {
    			date = sdf.parse(dateInString);
						dateFechaCalculoFiniquito = sdf.parse(fecha_finiquito);
				   	
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			//e.printStackTrace();
    			System.out.println();
    		}
    		

					Calendar calendarFin = Calendar.getInstance();
        			calendarFin.setTime(dateFechaCalculoFiniquito);
        			 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
            		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
            		 anioFin = calendarFin.get(Calendar.YEAR);
				
				
			}else if (activo_asvac==true){
				try {
	    			date = sdf.parse(dateInString);
	   		//		dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
	    	   	
	   		       } catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	       		
	   		
			
			
    		
    		Calendar calendarFin = Calendar.getInstance();
    		calendarFin.setTime(utilitario.getDate());
    		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
    		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
    		 anioFin = calendarFin.get(Calendar.YEAR);
				
				
			}else {
				utilitario.agregarMensajeError("No contiene Vacaciones", "No se pudo encontrar un registro para el empleado");
			}
			
				
			}	
				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       		

		
    		

    		Calendar calendarInicio = Calendar.getInstance();
    		calendarInicio.setTime(date);
    		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
    		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
    		int anioInicio = calendarInicio.get(Calendar.YEAR);
    		
    		int anios = 0;
    		int mesesPorAnio = 0;
    		int diasPorMes = 0;
    		int diasTipoMes = 30;

    			if (mesInicio <= mesFin) {
    				anios = anioFin - anioInicio;
    				if (diaInicio <= diaFin) {
    					mesesPorAnio = mesFin - mesInicio;
    					diasPorMes = diaFin - diaInicio;
    				} else {
    					if (mesFin == mesInicio) {
    						anios = anios - 1;
    					}
    					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
    					diasPorMes = diasTipoMes - (diaInicio - diaFin);
    				}
		    	}else{
    				anios = anioFin - anioInicio - 1;
    				//System.out.println(anios);
    				if (diaInicio > diaFin) {
    					mesesPorAnio = mesFin - mesInicio - 1 + 12;
    					diasPorMes = diasTipoMes - (diaInicio - diaFin);
    				} else {
    					mesesPorAnio = mesFin - mesInicio + 12;
    					diasPorMes = diaFin - diaInicio;
		    	}
			}
    		
           			
           		//System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");

           		//System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		



    		int returnValue = -1;
    				returnValue = anios * 12 + mesesPorAnio;
           				//System.out.println("Total meses: " + returnValue + " Meses.");

    				if (activo_asvac==false){
    					returnValue =(((returnValue*30)+diasPorMes))+1+sumaValor;
           				}else{
           					returnValue =(((returnValue*30)+diasPorMes))+1;
           				}
           				//System.out.println("Total dias: " + returnValue);
    				 return returnValue;
        }

 //PERIODOS DE EMPLEADO DE ACUERDO A LA FECHA DE INGRESO 

public static List<Integer> getPeriodos(Integer nda, Integer nde) {
	List<Integer> peridos = new ArrayList<Integer>();
	Integer indicePeriodo = 1;
	Integer ndeAux = nde;
	do {
		ndeAux = ndeAux - nda;
		peridos.add(indicePeriodo);
		indicePeriodo++;
	}while(ndeAux > 0);
	return peridos;
}

//VACACIONES POR PERIODOS
	public static List<Double> getVacacionesXPeriodo(Integer nda, Integer nde, List<Integer> peridos, double numeroDiasVacacionXAnio) {
		List<Double> vaxacionXPeriodo = new ArrayList<Double>();
		Integer ndeAux = nde;
		for (Integer periodo : peridos) {
			if (ndeAux >= nda){
				ndeAux = nde - (periodo * nda);
				vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
			}else{
			//	System.out.println("calculo final: "+ndeAux);
				double numdiasUltimoperiodo = (ndeAux * numeroDiasVacacionXAnio) / nda;
				vaxacionXPeriodo.add(numdiasUltimoperiodo);
			}
		}
		return vaxacionXPeriodo;
	}
	
	
public double getDatosEmpleado1(String IDE_GTEMP,boolean reporte,String ide_asvac,String fecha_ingreso_continuidad,String fecha_fin_continuidad,String IDE_GEEDP,Double numeroDiasTomados, int periodo,String ide_asvac_origen){
	int bandEntrada=0;
	int  bandSalida=0;
		nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		TablaGenerica tab_partida = getPartida(IDE_GTEMP);
		nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac,fecha_ingreso_continuidad,fecha_fin_continuidad);
		// Obtener los periodos de cada empleado 1,2,3 etc
		List<Integer> peridos = getPeriodos(nda, nde);
		// Obtengo los periodos para el ajuste de dias pendientes del
		// empleado hasta el 30 de abril del 2017
		List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);
		// Obtengo el total de mis dias pendientes al restar los dias
		// acumulados-dias tomados
		//double sumatotalDiasGenerados = 0.0;
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el presente
		List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el 30 de abril de
		//List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
		 double sumatotal_vacaciones = 0.0;
		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta la fecha de hoy
		 //BigDecimal num1 = new BigDecimal(0);
		for (int i = 0; i < vacacionesPeriodo.size(); i++) {
			sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
		}
		double total_dias_tomados=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2))-Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
		double total_dias_pendientes=0,total_dias_tomados_=0.0;
       double valor_retorno=0.0;
		if (total_dias_tomados>0.0) {
			total_dias_pendientes=Double.parseDouble(utilitario.getFormatoNumero(total_dias_tomados,2));
			total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
			valor_retorno=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
		}else if(total_dias_tomados==0.0){
			total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
			total_dias_pendientes=Double.parseDouble(utilitario.getFormatoNumero(numeroDiasTomados,2));
		}else {
			total_dias_tomados_=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
			total_dias_pendientes=0.0;
			valor_retorno=Double.parseDouble(utilitario.getFormatoNumero(sumatotal_vacaciones,2));
		}

		String periodoNuevo=""+fecha_ingreso_continuidad+" - "+fecha_fin_continuidad;
		//TablaGenerica tab_codigo=utilitario.consultar("select ide_asvre,ide_asvre from asi_vacacion_resumen_empleado order by ide_asvre limit 1 ");
		TablaGenerica tab_codigo = utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("asi_vacacion_resumen_empleado", "ide_asvre"));

		
		TablaGenerica tab_usuario=utilitario.consultar("select ide_usua,nick_usua "
				+ "from sis_usuario "
				+ "where ide_gtemp="+IDE_GTEMP);
		
		/*System.out.println("insert into asi_vacacion_resumen_empleado (ide_asvre, ide_asvac,ide_periodo_asvre, periodo_asvre, nro_dias_vacacion_asvre,nro_dias_tomados_asvre, nro_dias_pendientes_asvre, "
		+ "activo_asvre, usuario_ingre, fecha_ingre, hora_ingre,ide_geedp_asvac) "
				+"values ( "+Integer.parseInt(tab_codigo.getValor("codigo"))+","+ide_asvac_origen+","+periodo+",'"+periodoNuevo+"',"+utilitario.getFormatoNumero(sumatotal_vacaciones,2)+","
				+ ""+total_dias_tomados_+" ,"+total_dias_pendientes+",true,'"+tab_usuario.getValor("nick_usua")+"','"+utilitario.getFechaActual()+"','"+utilitario.getHoraActual()+"',"+IDE_GEEDP+")");
		*/
		utilitario.getConexion().ejecutarSql("insert into asi_vacacion_resumen_empleado (ide_asvre, ide_asvac,ide_periodo_asvre, periodo_asvre, nro_dias_vacacion_asvre,nro_dias_tomados_asvre, nro_dias_pendientes_asvre, "
		+ "activo_asvre, usuario_ingre, fecha_ingre, hora_ingre,ide_geedp_asvac) "
				+"values ( "+Integer.parseInt(tab_codigo.getValor("codigo"))+","+ide_asvac_origen+","+periodo+",'"+periodoNuevo+"',"+utilitario.getFormatoNumero(sumatotal_vacaciones,2)+","
				+ ""+total_dias_tomados_+" ,"+total_dias_pendientes+",true,'"+tab_usuario.getValor("nick_usua")+"','"+utilitario.getFechaActual()+"','"+utilitario.getHoraActual()+"',"+IDE_GEEDP+")");
				//utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
		
		
		
		
		
		/*TablaGenerica tab_asi_vacacion_resumen_empleado=new TablaGenerica();
		tab_asi_vacacion_resumen_empleado.setTabla("asi_vacacion_resumen_empleado", "IDE_ASVRE", 3);
		tab_asi_vacacion_resumen_empleado.setCondicion("IDE_ASVRE=-1");
		tab_asi_vacacion_resumen_empleado.ejecutarSql();
		tab_asi_vacacion_resumen_empleado.insertar();
		tab_asi_vacacion_resumen_empleado.setValor("IDE_ASVAC",""+Integer.parseInt(ide_asvac_origen));
		tab_asi_vacacion_resumen_empleado.setValor("ide_periodo_asvre",""+(periodo));
		tab_asi_vacacion_resumen_empleado.setValor("PERIODO_ASVRE",""+periodoNuevo);
		tab_asi_vacacion_resumen_empleado.setValor("nro_dias_vacacion_asvre",""+utilitario.getFormatoNumero(sumatotal_vacaciones,2));
		tab_asi_vacacion_resumen_empleado.setValor("nro_dias_tomados_asvre",""+total_dias_tomados_);
		tab_asi_vacacion_resumen_empleado.setValor("nro_dias_pendientes_asvre",""+total_dias_pendientes);
	    tab_asi_vacacion_resumen_empleado.setValor("ide_geedp_asvac",IDE_GEEDP);
	    tab_asi_vacacion_resumen_empleado.setValor("activo_asvre","true");
		tab_asi_vacacion_resumen_empleado.guardar();
		guardarPantalla();*/
       return valor_retorno;
}



public String getBanderaCodigo() {
	return banderaCodigo;
}



public void setBanderaCodigo(String banderaCodigo) {
	this.banderaCodigo = banderaCodigo;
}

/**
 * Metodo que devuelve el ide maximo de una tabla
	* @return String SQL Codigo maximo de los ide primarios de de las tablas
	*/

	public String servicioCodigoMaximo(String tabla,String ide_primario){
	String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
	return maximo;
	}
 		 	

    private Date getFechaAsyyyyMMddHHmm(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    
    private String getFechaAsyyyyMMddHHmm(Date fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    return df.format(fecha);
	    }
	//busca si contiene marcacion en la noche


//Sumar horas en fecha
 public String sumarRestarMinutos(Date fecha, int minuto){
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(fecha); // Configuramos la fecha que se recibe
 calendar.add(Calendar.MINUTE, minuto);  // numero de horas a añadir, o restar en caso de horas<0
   // Devuelve el objeto Date con las nuevas horas añadidas
  //nuevoDia=sumarRestarMinutosFecha(getFechaAsyyyyMMdd(nuevoDiaTemp), 5);
  
	String horaDesdeTemp="",minDesdeTemp="",segDesdeTemp="";
	if (calendar.getTime().getHours()<10) {
		horaDesdeTemp="0"+calendar.getTime().getHours();
	}else {
		horaDesdeTemp=""+calendar.getTime().getHours();
	}
	
	
	if (calendar.getTime().getMinutes()<10) {
		minDesdeTemp="0"+calendar.getTime().getMinutes();
	}else {
		minDesdeTemp=""+calendar.getTime().getMinutes();
	}
	
	
	if (calendar.getTime().getSeconds()<10) {
		segDesdeTemp="0"+calendar.getTime().getSeconds();
	}else {
		segDesdeTemp=""+calendar.getTime().getSeconds();
	}
 
   String hora = horaDesdeTemp+":"+minDesdeTemp+":"+segDesdeTemp;   				

   return hora;
		
 
 }
 
 

public Integer getNumMaxRegistroPermiso() {
	return numMaxRegistroPermiso;
}



public void setNumMaxRegistroPermiso(Integer numMaxRegistroPermiso) {
	this.numMaxRegistroPermiso = numMaxRegistroPermiso;
}
 
 

}
