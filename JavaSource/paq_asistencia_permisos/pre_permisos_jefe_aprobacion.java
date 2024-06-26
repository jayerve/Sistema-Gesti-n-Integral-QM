package paq_asistencia_permisos;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import groovy.util.IFileNameFinder;


public class pre_permisos_jefe_aprobacion extends Pantalla {

	private Tabla tab_permisos=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_filtro_activo = new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_asis=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private Tabla tab_permiso_justificacion=new Tabla();
	
	private Dialogo dia_anulado=new Dialogo();
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();
	private int numeroSumarFinesSemana =0;
	//private double diasTotales =0;
	private int numeroFinesSemana = 0;
	private int numeroFinesSemanaTotal =0;
	private int descuentoFinesSemana =0;
		boolean inconsistenciaBiometrico=false,bandDoc=false,bandJefeInmediato=false,bandJefe=false;
		int estadoJefe=0,estadoJefeGerente=0;
	String 	empleadoJefe="",empleadoGerente="", p_permisos_doc_enfermedad="",p_encargado_revision_permisos_enfermedad="",empleadoDoc="",p_encargado_trabajador_social="",p_encargado_trabajador_social2="";
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private String fechaIni="",fechaFin="",ide_gtempxx="",tipo_perfil="",sucu="",area="",depa="",ide_geare="",jefe_padre="",cargo_padre="",ide_asjei="",ide_gtemp_aprobacion="";	
	TablaGenerica tabJefeInmediato=null,tabJefe=null;
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();


	
	public pre_permisos_jefe_aprobacion() {
		
		
		

		//Consulta empleado loggeado
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");

		
		
		
	ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	
    	
    	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp,asjei.ide_gtemp_padre_asjei, "
    			+ "asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,"
    			+ "area.detalle_geare,ide_gtemp_padre_asjei,cargo_padre_asjei  "
    			+ "FROM asi_jefe_inmediato  asjei "
    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
    			+ "where ide_gtemp="+ide_gtempxx);

    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    		//utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		//return;
    		bandJefeInmediato=false;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");

         	if(tipo_perfil.equals("3")){
        		ide_gtemp_aprobacion=tabJefeInmediato.getValor("ide_gtemp");
        		bandJefeInmediato=true;
        		}
    	}
		
		
    	
    	tabJefe=utilitario.consultar("SELECT * FROM asi_jefe_inmediato  where ide_gtemp_padre_asjei="+ide_gtempxx+" and tipo_asjei=3 and activo_asjei=true");
    	if (tabJefe.getTotalFilas()>0) {
			bandJefe=true;
		}else {
			bandJefe=false;
		}		

    	
    	
		
		
		
	//botones ocultos	
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		
		//Motivo
		 p_permisos_doc_enfermedad=utilitario.getVariable("p_permisos_doc_enfermedad");
		 p_encargado_revision_permisos_enfermedad=utilitario.getVariable("p_encargado_revision_permisos_enfermedad");
		 p_encargado_trabajador_social=utilitario.getVariable("p_encargado_trabajador_social");
		 p_encargado_trabajador_social2=utilitario.getVariable("p_encargado_trabajador_social2");

		 
		 TablaGenerica tab_usua=utilitario.consultar("select * from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("ide_usua"));
		 empleadoDoc=tab_usua.getValor("ide_gtemp");
		 
		 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
			 bandDoc=true;
				bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
				cal_fecha_inicial.setFechaActual();
				bar_botones.agregarComponente(cal_fecha_inicial);
				bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
				cal_fecha_final.setFechaActual();
				bar_botones.agregarComponente(cal_fecha_final);
		    	Boton bot_fechas= new Boton();
		    	bot_fechas.setIcon("ui-icon-calculator");
		    	bot_fechas.setMetodo("seleccionarFechasReporte");
		    	bot_fechas.setValue("CONSULTA POR FECHAS");
		    	bot_fechas.setTitle("CONSULTA POR FECHAS");
		    	bar_botones.agregarBoton(bot_fechas);
		}else {
			 bandDoc=false;
				Etiqueta eti_colaborador=new Etiqueta("OPCIONES JEFE INMEDIATO: ");
				bar_botones.agregarComponente(eti_colaborador);
		}
		 
		 
		 
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("aprobarSolicitud");

		
		Boton bot_anular_solicitud=new Boton();
		bot_anular_solicitud.setValue("ANULAR SOLICITUD");
		bot_anular_solicitud.setMetodo("anularSolicitud");

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");


		
		Boton bot_biometrico=new Boton();
		bot_biometrico.setValue("SOLICITUDES BIOMÉTRICO");
		bot_biometrico.setMetodo("buscarSolicitudesBiometrico");
		
		Boton bot_solicitudes=new Boton();
		bot_solicitudes.setValue("SOLICITUDES");
		bot_solicitudes.setMetodo("buscarSolicitudes");


		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:200px;");

		Grid gri_anular_horas_extra=new Grid();
		gri_anular_horas_extra.setColumns(1);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
	

		dia_anulado.setId("dia_anulado");
		dia_anulado.setDialogo(gri_anular_horas_extra);
		dia_anulado.setWidth("18%");
		dia_anulado.setHeight("20%");
		dia_anulado.setTitle("ANULACIÓN DE SOLICITUD DE PERMISOS");
		dia_anulado.getBot_aceptar().setMetodo("aceptarAnulacionHorasPermisos");				
		dia_anulado.setDynamic(false);
		gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anulado);
		
		
		

	//	bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");



		//  PERMISOS (division 1)

		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setOrden(19);				

	 	tab_permisos.getColumna("GEN_IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("GEN_IDE_GEEDP").setOrden(20);				

	 	tab_permisos.getColumna("GEN_IDE_GEEDP2").setVisible(false);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setOrden(21);				

		tab_permisos.getColumna("GEN_IDE_GEEDP3").setVisible(false);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setOrden(22);				

		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false); 
		tab_permisos.getColumna("TIPO_ASPVH").setOrden(23);				

		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setOrden(24);				
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setVisible(false);
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setOrden(25);				

		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setVisible(false);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setOrden(26);				

		
		tab_permisos.getColumna("IDE_ASPVH").setOrden(1);				
		tab_permisos.getColumna("IDE_ASPVH").setNombreVisual("CÓDIGO");
		tab_permisos.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				//"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				//"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_permisos.getColumna("IDE_GEEDP").setOrden(2);
		tab_permisos.getColumna("IDE_GEEDP").setNombreVisual("EMPLEADO");
		tab_permisos.getColumna("IDE_GEEDP").setLectura(true);
		tab_permisos.getColumna("IDE_GEEDP").setAutoCompletar();
		
		
		tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
		
			tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
		tab_permisos.getColumna("IDE_ASMOT").setOrden(3);
		tab_permisos.getColumna("IDE_ASMOT").setNombreVisual("MOTIVO AUSENCIA");
		tab_permisos.getColumna("IDE_ASMOT").setLectura(true);
		tab_permisos.getColumna("DETALLE_ASPVH").setOrden(5);		
		tab_permisos.getColumna("DETALLE_ASPVH").setNombreVisual("DETALLE");
		tab_permisos.getColumna("DETALLE_ASPVH").setLectura(true);		
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setOrden(4);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
				tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setNombreVisual("FECHA SOLICITUD");
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setOrden(6);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setNombreVisual("FECHA DESDE");
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setOrden(7);		
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setNombreVisual("FECHA HASTA");
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setOrden(8);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setNombreVisual("NRO DÍAS");
        tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setOrden(9);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setNombreVisual("HORA DESDE");
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setLectura(true);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setOrden(10);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setNombreVisual("HORA HASTA");
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setLectura(true);

		tab_permisos.getColumna("NRO_HORAS_ASPVH").setOrden(11);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setNombreVisual("NRO HORAS");

		tab_permisos.getColumna("IDE_GEEST").setOrden(12);
		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);
		
		tab_permisos.getColumna("IDE_GEMES").setOrden(13);
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setOrden(14);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("APROBADO_ASPVH").setOrden(15);
		tab_permisos.getColumna("APROBADO_ASPVH").setLectura(true);
		tab_permisos.getColumna("APROBADO_ASPVH").setNombreVisual("APROBADO");
		tab_permisos.getColumna("ANULADO_ASPVH").setOrden(15);
		tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("ANULADO_ASPVH").setNombreVisual("ANULADO");
		tab_permisos.getColumna("ANULADO_ASPVH").setLectura(true);

		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setOrden(16);
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setNombreVisual("FEC. ANULACION");
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setLectura(true);
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setOrden(17);
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setNombreVisual("RAZ�N ANULACION");
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setLectura(true);

		tab_permisos.getColumna("ACTIVO_ASPVH").setOrden(18);
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		tab_permisos.getColumna("ACTIVO_ASPVH").setNombreVisual("ACTIVO");
		tab_permisos.getColumna("ACTIVO_ASPVH").setLectura(true);
		tab_permisos.getColumna("ide_gtemp_aprobador_doc").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GTH_EMPLEADO EMP ");
		tab_permisos.getColumna("ide_gtemp_aprobador_doc").setAutoCompletar();
		tab_permisos.getColumna("ide_gtemp_aprobador_doc").setLectura(true);

		
		tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GTH_EMPLEADO EMP ");
		tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setAutoCompletar();

		tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setLectura(true);
		tab_permisos.getColumna("estado_preaprobado_aspvh").setLectura(true);
		tab_permisos.getColumna("estado_preaprobado_aspvh").setNombreVisual("PRE-APROBADO");

		tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setVisible(false);
		tab_permisos.getColumna("estado_preaprobado_aspvh").setVisible(false);
		tab_permisos.getColumna("ide_asjei").setVisible(false);

		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_permisos.getValor("IDE_GTEMP"));
		
		if (bandDoc==true) {
			tab_permisos.getColumna("ide_gtemp_aprobador_doc").setVisible(true);			
			tab_permisos.setCondicion("ANULADO_ASPVH=false and (aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) and "
	     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and ide_asmot in("+p_permisos_doc_enfermedad+") ");

		}else {
			tab_permisos.getColumna("ide_gtemp_aprobador_doc").setVisible(false);
			if (bandJefeInmediato==true) {
				tab_permisos.getColumna("estado_preaprobado_aspvh").setVisible(true);
					tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND ide_gtemp_preaprobador IN ("+ide_gtemp_aprobacion+") AND (estado_preaprobado_aspvh= FALSE OR estado_preaprobado_aspvh IS NULL)    AND ANULADO_ASPVH=false and "
			     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");

				
			}else{
			if (bandJefe==true) {
				tab_permisos.getColumna("IDE_GTEMP_PREAPROBADOR ").setVisible(true);
				tab_permisos.getColumna("estado_preaprobado_aspvh").setVisible(true);

				tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false  "
						+ " AND (estado_preaprobado_aspvh=true)  and "
			     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");

		}else {
		tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
		}
		}
		}
		

		

		tab_permisos.agregarRelacion(tab_permiso_justificacion);
		tab_permisos.getGrid().setColumns(4);
        tab_permisos.setStyle("width:100%;");
		tab_permisos.setTipoFormulario(true);
		
	/*	int estadoJefe=0;
		String empleado="";
		//if (tab_permisos.getValor("aprobado_aspvh")==null  || tab_permisos.getValor("aprobado_aspvh").equals("") || tab_permisos.getValor("aprobado_aspvh").isEmpty() || tab_permisos.getValor("aprobado_aspvh").equals("false")) {
			TablaGenerica empleadoDepartamento=utilitario.consultar("select ide_geedp,ide_gtemp  from   gen_empleados_departamento_par where ide_gtemp IN (612,508,634,367) and"
					+ " activo_geedp=true");
			for (int i = 0; i < empleadoDepartamento.getTotalFilas(); i++) {
				if (empleadoDepartamento.getValor(i,"IDE_GEEDP").equals(ide_geedp_activo)) {
					estadoJefe=1;
					empleado=empleadoDepartamento.getValor(i,"IDE_GEEDP");
				}			
			}
 
			
			//for (int i = 0; i < empleadoDepartamento.getTotalFilas(); i++) {
		if (empleado.equals("911")) {
				
			//if (tab_permisos.getValor("gen_ide_geedp2").equals(ide_geedp_activo)) {
				tab_permisos.setCondicion("(aprobado_tthh_aspvh = FALSE OR aprobado_tthh_aspvh IS NULL) AND GEN_IDE_GEEDP3 IN ("+empleado+")    AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4,3) ");
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
				//i=empleadoDepartamento.getTotalFilas();		
			/*}else if (tab_permisos.getValor("gen_ide_geedp3").equals(ide_geedp_activo)) {
				tab_permisos.setCondicion("APROBADO_ASPVH = TRUE AND GEN_IDE_GEEDP3="+ide_geedp_activo+" AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4,3) ");
				tab_permisos.setCampoOrden("IDE_ASPVH DESC");//			i=empleadoDepartamento.getTotalFilas();
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
				//i=empleadoDepartamento.getTotalFilas();

			} 
			
     	
		
		}
	if (empleado.equals(ide_geedp_activo)) {
			tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
	     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4,3) ");
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(false);
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
		
		}
		
		
		else  {
		 	tab_permisos.setCondicion("((APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+ide_geedp_activo+" AND ANULADO_ASPVH=false and "
	     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4) ");
			tab_permisos.setCampoOrden("IDE_ASPVH DESC");//			i=empleadoDepartamento.getTotalFilas();
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(false);
			tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
			//i=empleadoDepartamento.getTotalFilas();
			
			
		//}
	}

		/*}
		
		
		
		else if (tab_permisos.getValor("aprobado_aspvh").equals("true")) {
			TablaGenerica empleadoDepartamento=utilitario.consultar("select ide_geedp,ide_gtemp  from   gen_empleados_departamento_par where ide_gtemp IN (612,508,634,367) and"
					+ " activo_geedp=true");
				
			
			for (int i = 0; i < empleadoDepartamento.getTotalFilas(); i++) {
				if (empleadoDepartamento.getValor(i,"IDE_GEEDP3").equals(ide_geedp_activo)) {
		     	tab_permisos.setCondicion("(APROBADO_ASPVH = true AND GEN_IDE_GEEDP3="+ide_geedp_activo+" AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' ");
				tab_permisos.setCampoOrden("IDE_ASPVH DESC");//			i=empleadoDepartamento.getTotalFilas();
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
				tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
				
				
				}else {
					tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO where ver_motivo_asmot=true order by DETALLE_ASMOT");		
					i=empleadoDepartamento.getTotalFilas();
				}
			}
			
		}*/
		
		
		

		tab_permisos.setCampoOrden("IDE_ASPVH DESC");//			
		tab_permisos.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setStyle("width:100%;");
		pat_panel1.setMensajeWarn("BANDEJA DE SOLICITUDES DE PERMISOS / VACACIONES");



		//Permiso de Justificacion 

		tab_permiso_justificacion.setId("tab_permiso_justificacion");
		tab_permiso_justificacion.setTabla("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ", 2);
		tab_permiso_justificacion.getColumna("IDE_ASPEJ").setNombreVisual("CODIGO");
		tab_permiso_justificacion.getColumna("DETALLE_ASPEJ").setNombreVisual("DETALLE");	
		tab_permiso_justificacion.getColumna("FECHA_ASPEJ").setNombreVisual("FECHA");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setCheck();
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setValorDefecto("true");
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setNombreVisual("ACTIVO");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setUpload("archivos");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setNombreVisual("ARCHIVO");
		tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
	    tab_permiso_justificacion.setLectura(true);
		tab_permiso_justificacion.dibujar();
		

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_permiso_justificacion);
		pat_panel2.setMensajeWarn("JUSTIFICACIÓN DE PERMISOS  / VACACIONES");

		
		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "70%", "H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		if (bandDoc==false) {
		bar_botones.agregarBoton(bot_aprobar_solicitud);
		bar_botones.agregarBoton(bot_anular_solicitud);
		//bar_botones.agregarBoton(bot_limpiar);

		bar_botones.agregarBoton(bot_biometrico);
		bar_botones.agregarBoton(bot_solicitudes);
		}else {
			bar_botones.agregarBoton(bot_aprobar_solicitud);
			bar_botones.agregarBoton(bot_anular_solicitud);
		}

	}

	
	
	public void anularSolicitud(){
		
		
		if (tab_permisos.getValor("TIPO_ASPVH").equals("5")) {
			utilitario.agregarMensajeInfo("No se puede Anular", "Contactese con el Administrador");
			return;
		}
		
		
		
		
		
		if(tab_permisos.getTotalFilas()>0){
			if (utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 
					Integer.parseInt(utilitario.getVariable("p_dia_aprobacion_permiso"))).compareTo(utilitario.DeStringADate(utilitario.getFechaActual()))<=0) {
					utilitario.agregarMensajeError("No se puede aprobar", "Fecha de máxima permitido: "+utilitario.DeDateAString(utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 1)));
					return;
				}
			int ide_asmot=Integer.parseInt(tab_permisos.getValor("IDE_ASMOT"));
			TablaGenerica tab_motivo=utilitario.consultar("select IDE_ASMOT,APLICA_VACACIONES_ASMOT,ES_VACACION_ASMOT  from asi_motivo where ide_asmot="+ide_asmot);
			String aplica_vacaciones=tab_motivo.getValor("APLICA_VACACIONES_ASMOT");
			String es_vacacion=tab_motivo.getValor("ES_VACACION_ASMOT");
			if (aplica_vacaciones.equals("true") && es_vacacion.equals("true")) {
				utilitario.agregarMensajeInfo("No se puede realizar esta acción ", "Contáctese con Talento Humano");
			return;
			}
		if (tab_permisos.getValor("aprobado_tthh_aspvh").equals("true") || tab_permisos.getValor("aprobado_aspvh").equals("true")) {
			utilitario.agregarMensajeInfo("No se puede realizar esta accion", "Permiso ya fue aprobado");
			return;
		}	
			
		are_tex_razon_anula.setValue("");
		dia_anulado.dibujar();
		utilitario.addUpdate("dia_anulado");	
	
		}else{
			utilitario.agregarMensajeInfo("No se puede Anular", "No se ha seleccionado una Solicitud");
		}
	
	}
	
	
	public void aprobarSolicitud(){	
		
		
		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());

				if(tab_permisos.getTotalFilas()>0){
					
					if (tipo_perfil.equals("3")) {
						if (utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 
								Integer.parseInt(utilitario.getVariable("p_dia_aprobacion_permiso"))).compareTo(utilitario.DeStringADate(utilitario.getFechaActual()))<=0) {
								utilitario.agregarMensajeError("No se puede aprobar", "Fecha de máxima permitido: "+utilitario.DeDateAString(utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 1)));
								return;
							}
						
						if (tab_permisos.getValor("estado_preaprobado_aspvh")==null  || tab_permisos.getValor("estado_preaprobado_aspvh").equals("") || tab_permisos.getValor("estado_preaprobado_aspvh").isEmpty() || tab_permisos.getValor("estado_preaprobado_aspvh").equals("false")) {
					con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
							con_guardar.setTitle("CONFIRMACION APROBACI�N DE SOLICITUD DE PERMISO");
							con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
							con_guardar.dibujar();
							utilitario.addUpdate("con_guardar");
							}
					}else{
					
					if (tab_permisos.getValor("aprobado_aspvh")==null  || tab_permisos.getValor("aprobado_aspvh").equals("") || tab_permisos.getValor("aprobado_aspvh").isEmpty() || tab_permisos.getValor("aprobado_aspvh").equals("false")) {
					
					if (utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 
						Integer.parseInt(utilitario.getVariable("p_dia_aprobacion_permiso"))).compareTo(utilitario.DeStringADate(utilitario.getFechaActual()))<=0) {
						utilitario.agregarMensajeError("No se puede aprobar", "Fecha de máxima permitido: "+utilitario.DeDateAString(utilitario.sumarDiasFechaSinFinSemana(utilitario.DeStringADate(tab_permisos.getValor("fecha_solicitud_aspvh")), 1)));
						return;
					}	
						
					con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
					con_guardar.setTitle("CONFIRMACION APROBACI�N DE SOLICITUD DE PERMISO");
					con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
				}else{
						utilitario.agregarMensajeInfo("No se puede aprobar", "Permiso ya ha sido aprobado");
						return;
					}
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede Aprobar ", "No contiene Solicitudes Pendientes");
				}		
	
	}
	
	
		
	public void aceptarAprobarSolicitud(){		
		 if (tab_permisos.getValor("ide_asmot").equals(p_permisos_doc_enfermedad)) {
		 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {	 
			}else {
				 utilitario.agregarMensajeInfo("Permisos por emfermedad", "No se puede aprobar");
				 return;
			}
		 }else {
			 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
				 utilitario.agregarMensajeInfo("No se puede aprobar el permiso", "Permiso por enfermedad");
				 return;
			 }
		}
		
		
		   boolean bandCoordinadora=false;
		try {
			   TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			
			if(tab_permisos.getTotalFilas()>0){
			
	if (tab_permisos.getValor("TIPO_ASPVH").equals("4")) {
	if (tab_permisos.getValor("aprobado_aspvh")==null  || tab_permisos.getValor("aprobado_aspvh").equals("") || tab_permisos.getValor("aprobado_aspvh").isEmpty() || tab_permisos.getValor("aprobado_aspvh").equals("false")) {

		if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true,ide_gtemp_aprobador_doc="+empleadoDoc+" where ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permisos.setValor("APROBADO_ASPVH", "true");	
	    
		}else{
			if(tipo_perfil.equals("3")){
				utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set estado_preaprobado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
				tab_permisos.setValor("estado_preaprobado_aspvh", "true");	
	   
			}else{
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permisos.setValor("APROBADO_ASPVH", "true");	

			}
			}
	}
	}else if (tab_permisos.getValor("TIPO_ASPVH").equals("5")) {
		if (estadoJefe==1) {
			 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
					utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true,ide_gtemp_aprobador_doc="+empleadoDoc+" where ide_aspvh="+tab_permisos.getValorSeleccionado());
			 }else{
				 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
						utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true,ide_gtemp_aprobador_doc="+empleadoDoc+" where ide_aspvh="+tab_permisos.getValorSeleccionado());
	 				 }else{
			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
				 }
				 
				 }
			tab_permisos.setValor("APROBADO_ASPVH", "true");
		}else if (estadoJefeGerente==1){
			 if (empleadoDoc.equals(p_encargado_revision_permisos_enfermedad)  || empleadoDoc.equals(p_encargado_trabajador_social) || empleadoDoc.equals(p_encargado_trabajador_social2)) {
					utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true,ide_gtemp_aprobador_doc="+empleadoDoc+" where ide_aspvh="+tab_permisos.getValorSeleccionado());

			 }else{

			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_tthh_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());	}
		}
		
	}else if (tab_permisos.getValor("TIPO_ASPVH").equals("1")){
		
		if(tipo_perfil.equals("3")){
			inconsistenciaBiometrico=false;
			bandCoordinadora=false;
			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set estado_preaprobado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
			tab_permisos.setValor("estado_preaprobado_aspvh", "true");
		}else{		
		tab_permisos.setValor("APROBADO_ASPVH", "true");	
		inconsistenciaBiometrico=false;
		bandCoordinadora=false;
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true,NRO_DIAS_ASPVH=1 where ide_aspvh="+tab_permisos.getValorSeleccionado());
		}
		
	}

		
			}
	
			guardarPantalla();
			con_guardar.cerrar();		
			String str_ide_anterior=tab_permisos.getValorSeleccionado();
			int anio_desde=0;
			anio_desde=utilitario.getAnio(utilitario.getFechaActual())-1;
			String fecha_inicio="";
			fecha_inicio=anio_desde+"-01"+"-01";
			if (estadoJefe==1) {
				tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+empleadoJefe+" AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+fecha_inicio+"' AND '"+utilitario.getFechaActual()+"' and TIPO_ASPVH IN (5)");
				
				
				EnviarCorreo(Integer.parseInt(tab_permisos.getValorSeleccionado()),obtenerIdEmpleado(tab_permisos.getValor("GEN_IDE_GEEDP")));

					
			}else if (estadoJefeGerente==1) {
				tab_permisos.setCondicion("(aprobado_tthh_aspvh = FALSE OR aprobado_tthh_aspvh IS NULL) AND GEN_IDE_GEEDP="+empleadoGerente+" AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+fecha_inicio+"' AND '"+utilitario.getFechaActual()+"' and TIPO_ASPVH =5 and aprobado_aspvh=true");
					
			}else if (bandJefeInmediato==true) {
				tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND ide_gtemp_preaprobador IN ("+ide_gtemp_aprobacion+") AND (estado_preaprobado_aspvh= FALSE OR estado_preaprobado_aspvh IS NULL)    AND ANULADO_ASPVH=false and "
			     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
		
			}else if(bandJefe==true){
				tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false  "
						+ " AND (estado_preaprobado_aspvh=true)  and "
			     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
		}else {
				if (bandDoc==true) {
					try {
						//util.EnviaMail(envMail, correo, "GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE", emailNotificacionAprobado(strNombreEmpleado, detallePermiso, fecha_solicitud_aspvh), null);
						
						TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, " + "clave_corr from sis_correo where ide_corr=2");

						String smtp_correo = tab_correo_envio.getValor("smtp_corr");
						String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
						String correo_envio = tab_correo_envio.getValor("correo_corr");
						String usuario_envio = tab_correo_envio.getValor("usuario_corr");
						String clave_correo = tab_correo_envio.getValor("clave_corr");
						String detallePermiso="";
						String detalleAspvh="";
						
						EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

						String strNombreEmpleado="",strNombreEmpleado1="";
						strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tab_permisos.getValor("gen_ide_geedp2"));
						
						TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
								"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
								"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
								"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
								"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
								"FROM GTH_EMPLEADO EMP " +
								" WHERE EMP.IDE_GTEMP="+tab_permisos.getValor("ide_gtemp")+" limit 1");
						
						

						TablaGenerica tab_correo= utilitario.consultar("select cor.ide_gtemp,cor.detalle_gtcor  "
								+ "from gth_correo cor "
								+ "left join gen_empleados_departamento_par par on par.ide_gtemp=cor.ide_gtemp "
								+ "where cor.activo_gtcor=true and cor.notificacion_gtcor=true and par.activo_geedp=true "
								+ "and par.ide_geedp="+tab_permisos.getValor("gen_ide_geedp2")+" "
								+ "order by cor.ide_gtemp asc");
											
						
						TablaGenerica tab_permisos1=utilitario.consultar("select * from asi_permisos_vacacion_hext where ide_aspvh="+tab_permisos.getValor("ide_aspvh"));		
						TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
						String nombreTipoSolicitud="",nroHoras="",motivo="",detalleSolicitudPermiso="",obtieneNroDias="";
						
						if (tab_permisos.getValor("tipo_aspvh").equals("1")) {
							 nombreTipoSolicitud="Permiso Por Horas ";
							 nroHoras= tab_permisos.getValor("nro_horas_aspvh"); 
							 motivo=tab_motivo.getValor("detalle_asmot");
							 detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
							 detalleSolicitudPermiso = " Nro:  "+ tab_permisos.getValor("ide_aspvh") +" por " + motivo.toLowerCase() + " de " + nroHoras + " hora(s) ingresada el "+tab_permisos.getValor("fecha_solicitud_aspvh")+" "
							 		+ "del funcionario: "+tabEmpleado.getValor("NOMBRES_APELLIDOS").toUpperCase()+" ha sido Aprobado";
							 System.out.println(detalleSolicitudPermiso);
						}
							if (tab_permisos.getValor("tipo_aspvh").equals("4")) {
								nombreTipoSolicitud="Permiso Por Dia(s) ";
								obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
								motivo=tab_motivo.getValor("detalle_asmot");
								detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
								detalleSolicitudPermiso = " Nro:  "+ tab_permisos.getValor("ide_aspvh") +" por " + motivo.toLowerCase() + " de " + obtieneNroDias + " dia(s) ingresada el "+tab_permisos.getValor("fecha_solicitud_aspvh")+" "
										+ "del funcionario: "+tabEmpleado.getValor("NOMBRES_APELLIDOS").toUpperCase()+" ha sido Aprobado";
								System.out.println(detalleSolicitudPermiso);
							
			}
			
			
						envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
						envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado, detalleSolicitudPermiso, ""));
						//envMail.setPara(tab_correo.getValor("detalle_gtcor"));
						//	t
					
						if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
						{
							utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + tab_correo.getValor("detalle_gtcor"));
							//envMail.setCopia(correo_aviso_enfermedad);
							//envMail.setCopia(correo_aviso_trabajadora_social);	
							envMail.setCopia(utilitario.getVariable("p_notificacion_cssa"));	
						}
						else
							utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + tab_correo.getValor("detalle_gtcor"));
					} catch (Exception e) {
						utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
						System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
					}
					
				tab_permisos.setCondicion("ANULADO_ASPVH=false and (aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) and "
			     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and ide_asmot in("+p_permisos_doc_enfermedad+") ");
					
					
		
	
				}else{	
					if (bandJefeInmediato==true) {
						tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND ide_gtemp_preaprobador IN ("+ide_gtemp_aprobacion+") AND (estado_preaprobado_aspvh= FALSE OR estado_preaprobado_aspvh IS NULL)    AND ANULADO_ASPVH=false and "
					     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
				
					}else if(bandJefe==true){
						tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false  "
								+ " AND (estado_preaprobado_aspvh=true)  and "
					     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
				}else{	
					
			tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+tabEmpDep.getValor("ide_geedp")+" AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+fecha_inicio+"' AND '"+utilitario.getFechaActual()+"' and TIPO_ASPVH IN (1,4) and ide_asmot not in(26,27)");
				}
				}	
			}
			
			tab_permisos.ejecutarSql();
			
			tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
			tab_permiso_justificacion.ejecutarSql();
			utilitario.addUpdate("tab_permisos,tab_permiso_justificacion");
			/*}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar ", "No contiene Solicitudes Pendientes");
			}
			*/
			
		} catch (Exception e) {
			System.out.println("Error en  aprobar solicitud aceptarAprobarSolicitud:  "+e.getMessage());
		}
		
	
	}

	
	
	
	public void aceptarAnulacionHorasPermisos(){
		
		if(tab_permisos.getTotalFilas()>0){
	    	TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));

			
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
					tab_permisos.setValor("razon_anula_aspvh",are_tex_razon_anula.getValue().toString());
					tab_permisos.setValor("fecha_anula_aspvh",utilitario.getFechaActual());
					tab_permisos.setValor("anulado_aspvh","true");
					tab_permisos.setValor("activo_aspvh","false");
					tab_permisos.modificar(tab_permisos.getFilaActual());
					tab_permisos.guardar();
					guardarPantalla();
					dia_anulado.cerrar();
					if (bandDoc==true) {
						
						try {
							//util.EnviaMail(envMail, correo, "GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE", emailNotificacionAprobado(strNombreEmpleado, detallePermiso, fecha_solicitud_aspvh), null);
							
							TablaGenerica tab_correo_envio = utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, " + "clave_corr from sis_correo where ide_corr=2");

							String smtp_correo = tab_correo_envio.getValor("smtp_corr");
							String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
							String correo_envio = tab_correo_envio.getValor("correo_corr");
							String usuario_envio = tab_correo_envio.getValor("usuario_corr");
							String clave_correo = tab_correo_envio.getValor("clave_corr");
							String detallePermiso="";
							String detalleAspvh="";
							
							EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

							String strNombreEmpleado="";
							strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tab_permisos.getValor("gen_ide_geedp2"));
							
						
							TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
									"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
									"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
									"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
									"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
									"FROM GTH_EMPLEADO EMP " +
									" WHERE EMP.IDE_GTEMP="+tab_permisos.getValor("ide_gtemp")+" limit 1");
							
							
							
							
							TablaGenerica tab_correoJefe= utilitario.consultar("select cor.ide_gtemp,cor.detalle_gtcor  "
									+ "from gth_correo cor "
									+ "left join gen_empleados_departamento_par par on par.ide_gtemp=cor.ide_gtemp "
									+ "where cor.activo_gtcor=true and cor.notificacion_gtcor=true and par.activo_geedp=true "
									+ "and par.ide_geedp="+tab_permisos.getValor("gen_ide_geedp2")+" "
									+ "order by cor.ide_gtemp asc");
					
							
							TablaGenerica tab_correo= utilitario.consultar("select cor.ide_gtemp,cor.detalle_gtcor  "
									+ "from gth_correo cor "
									+ "where cor.activo_gtcor=true and cor.notificacion_gtcor=true and "
									+ "cor.ide_gtemp="+tab_permisos.getValor("ide_gtemp")+" "
									+ "order by cor.ide_gtemp asc");
												
							
							TablaGenerica tab_permisos1=utilitario.consultar("select * from asi_permisos_vacacion_hext where ide_aspvh="+tab_permisos.getValor("ide_aspvh"));		
							TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
							String nombreTipoSolicitud="",nroHoras="",motivo="",detalleSolicitudPermiso="",obtieneNroDias="";
							
							if (tab_permisos.getValor("tipo_aspvh").equals("1")) {
								 nombreTipoSolicitud="Permiso Por Horas ";
								 nroHoras= tab_permisos.getValor("nro_horas_aspvh"); 
								 motivo=tab_motivo.getValor("detalle_asmot");
								 detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
								 detalleSolicitudPermiso = " Nro:  "+ tab_permisos.getValor("ide_aspvh") +" por " + motivo.toLowerCase() + " de " + nroHoras + " hora(s) ingresada el "+tab_permisos.getValor("fecha_solicitud_aspvh")+" ha sido Anulado debido: "+detallePermiso;
								 System.out.println(detalleSolicitudPermiso);
							}
								if (tab_permisos.getValor("tipo_aspvh").equals("4")) {
									nombreTipoSolicitud="Permiso Por Dia(s) ";
									obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
									motivo=tab_motivo.getValor("detalle_asmot");
									detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
									detalleSolicitudPermiso = " Nro:  "+ tab_permisos.getValor("ide_aspvh") +" por " + motivo.toLowerCase() + " de " + obtieneNroDias + " dia(s) ingresada el "+tab_permisos.getValor("fecha_solicitud_aspvh")+" ha sido Anulado debido: "+detallePermiso;
									System.out.println(detalleSolicitudPermiso);
								
							}
								
							
							envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
							envMail.setCuerpoHtml(emailNotificacionAprobado(tabEmpleado.getValor("NOMBRES_APELLIDOS"), detalleSolicitudPermiso, ""));
							//envMail.setPara(tab_correo.getValor("detalle_gtcor"));

						
							if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
							{
								utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + tab_correo.getValor("detalle_gtcor"));
								envMail.setCopia(tab_correoJefe.getValor("detalle_gtcor"));
								//envMail.setCopia(correo_aviso_trabajadora_social);	
								envMail.setCopia(utilitario.getVariable("p_notificacion_cssa"));	
							}
							else
								utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + tab_correo.getValor("detalle_gtcor"));
						} catch (Exception e) {
							utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
							System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
						}
						
						
						tab_permisos.setCondicion("ANULADO_ASPVH=false and (aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) and "
				     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and ide_asmot in("+p_permisos_doc_enfermedad+") ");

					}else {
						
						EnviaCorreo();

						tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
				     		+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+utilitario.getVariable("p_fecha_ini")+"' AND '"+utilitario.getVariable("p_fecha_fin")+"' and tipo_aspvh in(1,4) and ide_asmot not in(26,27)");
					}
					
					
					

					
					/*tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+tabEmpDep.getValor("ide_geedp")+" AND ANULADO_ASPVH=false and "
			     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2017-01-01' AND '2020-12-01' and ide_asmot not in(26,27)");*/
					tab_permisos.ejecutarSql();

					tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
					tab_permiso_justificacion.ejecutarSql();
					
		} else {
			utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe ingresar una razón");
		}
		
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar ", "No contiene Solicitudes Pendientes");
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
		actualizarTabla2();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		actualizarTabla2();
	}

	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarTabla2();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarTabla2();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 0000000000000000
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
	
		try {
			super.siguiente();
			actualizarTabla2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error siguiente "+e);
		}
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarTabla2();
	}

public void actualizarTabla2(){
	tab_permiso_justificacion.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
	tab_permiso_justificacion.ejecutarSql();		
}

	String ide_geedp_activo="";
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_permisos.limpiar();
		tab_permiso_justificacion.limpiar();
		ide_geedp_activo="";
		//aut_empleado.limpiar();
		sel_cal.Limpiar();

	     utilitario.addUpdate("tab_permisos,tab_justificacion");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Detalle Permisos")){
			if (tab_permisos.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();		
					p_parametros.put("IDE_GEEDP",ide_geedp_activo);
					p_parametros.put("titulo", " BIESS GERENCIA ADMINISTRATIVA - FINANCIERA DEPARTAMENTO DE TALENTO HUMANO PERMISOS");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						

					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No contiene registro de permisos");
			}	

		}else if (rep_reporte.getReporteSelecionado().equals("Detalle Permisos Fecha")){

			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();	
				dia_filtro_activo.dibujar();
			}
			else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!=null && ! lis_activo.getSeleccionados().isEmpty()){
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					sel_cal.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}



			} else if (sel_cal.isVisible()) {
				if(sel_cal.isFechasValidas()){

					p_parametros.put("APROBACION",sel_cal.getFecha1String());
					p_parametros.put("VENCIMIENTO",sel_cal.getFecha2String());
					System.out.println("fecha 1:"+sel_cal.getFecha1String());
					System.out.println("fecha 2:"+sel_cal.getFecha2String());
					//				sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
					set_empleado_asis.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,  " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' ||  " +
							"PRIMER_NOMBRE_GTEMP || ' ' ||  " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES  " +
							"from GTH_EMPLEADO " +
							"WHERE ACTIVO_GTEMP IN("+lis_activo.getSeleccionados()+") " +
							"ORDER BY IDE_GTEMP ASC, " +
							"NOMBRES ASC ");
					set_empleado_asis.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado_asis.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado_asis.getTab_seleccion().ejecutarSql();

					set_empleado_asis.getBot_aceptar().setMetodo("aceptarReporte");
					sel_cal.cerrar();
					set_empleado_asis.dibujar();
				}else  {
					utilitario.agregarMensajeInfo("ERROR AL GENERAR REPORTE","Las fechas no son validas");
				}
			}
			else if(set_empleado_asis.isVisible()){
				if(set_empleado_asis.getSeleccionados()!=null && !set_empleado_asis.getSeleccionados().isEmpty()){

					System.out.println(""+set_empleado_asis.getSeleccionados());

					p_parametros.put("IDE_GTEMP",set_empleado_asis.getSeleccionados());
					p_parametros.put("titulo", " BIESS GERENCIA ADMINISTRATIVA - FINANCIERA  DEPARTAMENTO DE TALENTO HUMANO PERMISOS POR FECHA");
					System.out.println("path "+rep_reporte.getPath());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					set_empleado_asis.cerrar();
					sef_reporte.dibujar();

				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}

	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
	}

	public SeleccionTabla getSet_empleado_asis() {
		return set_empleado_asis;
	}

	public void setSet_empleado_asis(SeleccionTabla set_empleado_asis) {
		this.set_empleado_asis = set_empleado_asis;
	}

	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}

	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}
	
	
	public Dialogo getDia_anulado() {
		return dia_anulado;
	}

	public void setDia_anulado(Dialogo dia_anulado) {
		this.dia_anulado = dia_anulado;
	}

	public Tabla getTab_permiso_justificacion() {
		return tab_permiso_justificacion;
	}

	public void setTab_permiso_justificacion(Tabla tab_permiso_justificacion) {
		this.tab_permiso_justificacion = tab_permiso_justificacion;
	}




	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Tabla getTab_permisos() {
		return tab_permisos;
	}

	public void setTab_permisos(Tabla tab_permisos) {
		this.tab_permisos = tab_permisos;
	}


	public void EnviaCorreo(){

		try {
			String mensaje_anulacion="";
			TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+tab_permisos.getValor("IDE_GTEMP"));
			String correo=tab_correo.getValor("detalle_gtcor");
			TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, clave_corr from sis_correo where ide_corr=2"); 

			String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
			String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
			String correo_envio=tab_correo_envio.getValor("correo_corr");
			String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
			String clave_correo=tab_correo_envio.getValor("clave_corr");
			
				
			//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
			EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
			
			
					
					String detallePermiso="";
					String nombreTipoSolicitud;
					String nroHoras;
					String motivo;
					String obtieneNroDias;
					String detalleSolicitudPermiso="";
					//Estructura de mensaje
					String strNombreEmpleado="";
					String strNombreJefe="";

					strNombreEmpleado = retornaDatosCorreoEmpleado(tab_permisos.getValor("IDE_GTEMP"));
					
					// armar mensaje
					String ide_aspvh_correo =tab_permisos.getValor("ide_aspvh").toString();
					String anulado_aspvh_correo =tab_permisos.getValor("anulado_aspvh").toString();
					
					//Para solicitude de horas
					
					String fecha=tab_permisos.getValor("fecha_solicitud_aspvh").toString();
					Date fecha_nueva= utilitario.DeStringADate(fecha);
					Date fecha_solicitud_aspvh1 = fecha_nueva;
					 SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
					 String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
					
					
					//Para solicitude de dias
					String estado_aprobacion_tthh="";
					estado_aprobacion_tthh="Anulado";
					mensaje_anulacion=" Recuerde que tiene un tiempo límite para ingresar su justificación por inasistencia de lo contrario se le aplicará la sanción correspondiente";
					    
					    
				TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
					
					if (tab_permisos.getValor("tipo_aspvh").equals("1")) {
						 nombreTipoSolicitud="Permiso Por Horas ";
						 nroHoras= tab_permisos.getValor("nro_horas_aspvh"); 
						 motivo=tab_motivo.getValor("detalle_asmot");
						 detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
						 detalleSolicitudPermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + nroHoras + " hora(s) ingresada el "+fecha_solicitud_aspvh+" ha sido "+estado_aprobacion_tthh+" debido a: " +detallePermiso.toLowerCase();
						 System.out.println(detalleSolicitudPermiso);
					}

					if (tab_permisos.getValor("tipo_aspvh").equals("4")) {
						nombreTipoSolicitud="Permiso Por Dia(s) ";
						obtieneNroDias= tab_permisos.getValor("nro_dias_aspvh");
						 motivo=tab_motivo.getValor("detalle_asmot");
						 detallePermiso=tab_permisos.getValor("razon_anula_aspvh");
						 detalleSolicitudPermiso = " Nro:  "+ ide_aspvh_correo +" por " + motivo.toLowerCase() + " de " + obtieneNroDias + " dia(s) ingresada el "+fecha_solicitud_aspvh+" ha sido "+estado_aprobacion_tthh+" debido a: "+detallePermiso.toLowerCase();
						 System.out.println(detalleSolicitudPermiso);
					}
								
					try {
						
						envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
						envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado, detalleSolicitudPermiso, ""));
						envMail.setPara(tab_correo.getValor("detalle_gtcor"));

					
						if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
						{
			
						}
						else
							utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + tab_correo.getValor("detalle_gtcor"));
					} catch (Exception e) {
						utilitario.agregarMensajeError("Ha ocurrido un error al enviar el email a " + "", "");
						System.out.println("Ha ocurrido un error al enviar el email al empleado: " + e.getMessage()); 
					}
				
					
			/*		util.EnviaMail(envMail, correo, 
								"GESTI�N DE SOLICITUDES DE PERMISOS ONLINE",
								emailNotificacionAprobado(strNombreEmpleado,detalleSolicitudPermiso,mensaje_anulacion), 
							null);*/
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("No se ha podido enviar el correo error en la obtención de datos:"+e.getMessage());
				}
	}

	
	
				public String emailNotificacionAprobado(String strNombreEmpleado ,String detallePermiso,String mensaje_anulacion) {
			        String html = "<p>&nbsp;</p>\n"
			                + "<p>Estimado "+strNombreEmpleado.toUpperCase()+", "
			                + "</p>\n"
			                + "<p>&nbsp;</p>\n"
			                + "<p>Notificamos mediante la presente que la solicitud de permisos/vacaciones:  "+detallePermiso+".</p>\n"
			                + "<p>&nbsp;</p>\n"
			                + "<p>&nbsp;</p>\n" 
			                + "<p>"+mensaje_anulacion+"</p>\n"
			                + "<p>&nbsp;</p>\n"
			                + "<p>Saludos cordiales,</p>\n"
			                + "<table style=\"height: 144px;\" width=\"571\">\n"
			                + "<tbody>\n"
			                + "<tr>\n"
			                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
			                + "<td width=\"476\">\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>TALENTO HUMANO</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
			                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
			                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
			                + "</td>\n"
			                + "</tr>\n"
			                + "</tbody>\n"
			                + "</table>";
			        return html;
			    }

			    
	
				public String retornaDatosCorreoEmpleado(String IDE_GTEMP){

					
					String detallePermiso="";
					String nombreTipoSolicitud;
					String nroHoras;
					String motivo;
					String obtieneNroDias;
				
					//Estructura de mensaje
					String strNombreEmpleado="";
					
					//obtengo el empleado del cual requiero los datos
					TablaGenerica tab_empleado = ser_empleado.getEmpleado(IDE_GTEMP);
					String documento = tab_empleado.getValor("documento_identidad_gtemp");
					String primer_nombre_empleado= tab_empleado.getValor("primer_nombre_gtemp").toString(); 
					String segundo_nombre_empleado= tab_empleado.getValor("segundo_nombre_gtemp").toString();
					String apellido_paterno_empleado= tab_empleado.getValor("apellido_paterno_gtemp").toString(); 
					String apellido_materno_empleado= tab_empleado.getValor("apellido_materno_gtemp").toString();
					strNombreEmpleado=primer_nombre_empleado+" "+segundo_nombre_empleado+" "+apellido_paterno_empleado+"  "+apellido_materno_empleado;
					return strNombreEmpleado;


				
				}
	
				

			public void	buscarSolicitudesBiometrico(){
				
				
				estadoJefe=0;
				estadoJefeGerente=0;
				String empleado="";

				
				TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
				ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
				
				
					TablaGenerica empleadoDepartamento=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
					"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
					"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
					"DEPA.DETALLE_GEDEP " +
					"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
					"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
					"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
					"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
					"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
					+ "where IDE_GEEDP="+ide_geedp_activo+" AND EPAR.IDE_GECAF=11 AND EPAR.IDE_GEGRO=4 "
							+ "and EPAR.IDE_GEARE=9");
					
					
					
					
					if (empleadoDepartamento.getTotalFilas()>0) {
						estadoJefe=1;
						empleadoJefe=ide_geedp_activo;
					}
					
					
				
					/*TablaGenerica empleadoDepartamentoGerente=utilitario.consultar("select ide_geedp,ide_gtemp  from   gen_empleados_departamento_par where ide_gtemp IN (612,508,634,367) and"
							+ " activo_geedp=true");
					*/
					
					
					
					TablaGenerica empleadoDepartamentoGerente=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
							"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
							"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
							"DEPA.DETALLE_GEDEP " +
							"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
							"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
							"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
							"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
							"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
							+ "where IDE_GEEDP="+ide_geedp_activo+" AND EPAR.IDE_GECAF=14 AND EPAR.IDE_GEGRO=7 "
									+ "and EPAR.IDE_GEARE=12");
							
					
					if (empleadoDepartamentoGerente.getTotalFilas()>0) {
						estadoJefeGerente=1;
						empleadoGerente=ide_geedp_activo;
					}
					
				if (estadoJefe==0 && estadoJefeGerente==0) {
					tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
			     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4) and ide_asmot not in(26,27) ");
					tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
					tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
					tab_permisos.ejecutarSql();
					utilitario.agregarMensajeInfo("No se han registrado", "Bandeja vacía para este tipo de permiso");
					inconsistenciaBiometrico=false;
				}else if(estadoJefeGerente==1) {
						inconsistenciaBiometrico=true;
					//if (tab_permisos.getValor("gen_ide_geedp2").equals(ide_geedp_activo)) {
						tab_permisos.setCondicion("(aprobado_tthh_aspvh = FALSE OR aprobado_tthh_aspvh IS NULL) AND GEN_IDE_GEEDP IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
				     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(5) and aprobado_aspvh=true ");
						tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
						tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
						tab_permisos.ejecutarSql();
					
				}else if(estadoJefe==1){
				tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
			     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(5) ");
					tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(true);
					tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setOrden(27);				
					tab_permisos.ejecutarSql();
			
				}
				

				
				utilitario.addUpdate("tab_permisos");

				}



			
			
			
			public void EnviarCorreo(int ide_permiso,String empleado){
				
				try {
				TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+empleado);
				String correo=tab_correo.getValor("detalle_gtcor");
		        
				TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
						+ "clave_corr from sis_correo where ide_corr=2"); 
				String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
				String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
				String correo_envio=tab_correo_envio.getValor("correo_corr");
				String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
				String clave_correo=tab_correo_envio.getValor("clave_corr");
				
					
					
				//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
				EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
						String detallePermiso="";
						String nombreTipoSolicitud;
						String nroHoras;
						String motivo;
						String obtieneNroDias;
					
						//Estructura de mensaje
						String strNombreEmpleado="";
						strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(tab_permisos.getValor("gen_ide_geedp2"));
						
					    // armar mensajeDD
						
				
						TablaGenerica tab_permisos1=utilitario.consultar("select * from asi_permisos_vacacion_hext where ide_aspvh="+ide_permiso);

						
						String ide_aspvh_correo =tab_permisos1.getValor("ide_aspvh").toString();
						String anulado_aspvh_correo =tab_permisos1.getValor("anulado_aspvh").toString();
						//String aprobado_tthh_aspvh_correo =tab_permisos1.getValor("aprobado_tthh_aspvh").toString();
						//String aprobado_aspvh_correo =tab_permisos1.getValor("aprobado_aspvh").toString();
						String registro_novedad_aspvh =tab_permisos1.getValor("registro_novedad_aspvh").toString();
						String detalle_aspvh =tab_permisos1.getValor("detalle_aspvh").toString();
						String detalle_aspej ="";
						
						if (tab_permiso_justificacion.getValor("detalle_aspej")==null || tab_permiso_justificacion.getValor("detalle_aspej").equals("") || tab_permiso_justificacion.getValor("detalle_aspej").isEmpty() ) {
							
						}else {
							detalle_aspej=tab_permiso_justificacion.getValor("detalle_aspej");
						}

						
						String detalleAnulacion="";
						//Para solicitude de horas
						
						String fecha=tab_permisos1.getValor("fecha_solicitud_aspvh").toString();
						Date fecha_nueva= utilitario.DeStringADate(fecha);
						Date fecha_solicitud_aspvh1 = fecha_nueva;
						 SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
						 String fecha_solicitud_aspvh=sf.format(fecha_solicitud_aspvh1);
						
							String nro_dias_aspvh="";
						//Para solicitude de dias
						 if (tab_permisos1.getValor("TIPO_ASPVH").equals("5")) {
						nro_dias_aspvh =tab_permisos1.getValor("nro_dias_aspvh").toString();
						
						}else {
							nro_dias_aspvh="1";
						}
						String estado_aprobacion_tthh="";
						
						
					
						    
						
						if ((anulado_aspvh_correo.equals("true"))) {
							detalleAnulacion=tab_permisos1.getValor("razon_anula_aspvh");
						}
						 
							
						
							
					TablaGenerica tab_motivo=utilitario.consultar("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot="+tab_permisos.getValor("ide_asmot"));	
						
						if (tab_permisos1.getValor("tipo_aspvh").equals("1")) {
							 nombreTipoSolicitud="Permiso Por Horas ";
							 nroHoras= tab_permisos1.getValor("nro_horas_aspvh"); 
							 motivo=tab_motivo.getValor("detalle_asmot");
							 detallePermiso="El permiso Nro:  "+ide_aspvh_correo+" "+motivo.toLowerCase()+" ingresado el "+fecha_solicitud_aspvh+". "
							 		+ "Desde: "+tab_permisos1.getValor("fecha_desde_aspvh")+"  y "
			  				 		+ "Hasta: "+tab_permisos1.getValor("fecha_hasta_aspvh")+" por un lapso de "+nroHoras+" hora(s)  "
			  				 		+ " se encuentra "+estado_aprobacion_tthh+".";
						     System.out.println(detallePermiso);
						}
						if (tab_permisos1.getValor("tipo_aspvh").equals("5")) {
							 motivo=tab_motivo.getValor("detalle_asmot");
							 nroHoras= tab_permisos1.getValor("nro_dias_aspvh"); 
							
							detallePermiso="El permiso Nro:  "+ide_aspvh_correo+" "+motivo.toLowerCase()+" ingresado el "+fecha_solicitud_aspvh+" "
								 		+ "Desde: "+tab_permisos1.getValor("fecha_desde_aspvh")+"  "
				  				 		+ "Hasta: "+tab_permisos1.getValor("fecha_hasta_aspvh")+" por un lapso de "+nroHoras+" dia(s) para los siguientes eempleados : "
				  				 				+ " "+detalle_aspvh+" debido "+detalle_aspej;
				  				 		//+ " se encuentra "+estado_aprobacion_tthh+". ";
		 					 System.out.println("DE :"+strNombreEmpleado+" PARA:"+correo+" "+detallePermiso);
						} 
									

				System.out.println("Enviando Correo.........");
							
				try {
					/*util.EnviaMail(envMail, correo, 
									"GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE",
									emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh), 
									null);
					*/
						String str_mail=correo;
						envMail.setAsunto("GESTIÓN DE SOLICITUDES DE PERMISOS ONLINE");
						envMail.setCuerpoHtml(emailNotificacionAprobado(strNombreEmpleado,detallePermiso,fecha_solicitud_aspvh));
						envMail.setPara(str_mail);
						pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
						
						if(obj.getRespuesta())
						{
							utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
						}
						else
							utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
					} catch (Exception e) {
					System.out.println("Error en el envío de correo"+e.getMessage());
						}

					} catch (Exception e) {
						e.printStackTrace();
						utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
					}

						} 		
			
			
			
 
			
			public String obtenerNombresApellidosEmpleadoCorreo(String empleado){
				String retornoValor="";
			
				TablaGenerica tabEmpleado=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
						"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
						"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
						"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
						"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
						"DEPA.DETALLE_GEDEP " +
						"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
						"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
						"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
						"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
						+ " WHERE ACTIVO_GEEDP=TRUE AND EPAR.IDE_GEEDP="+empleado+" limit 1");
				
				return retornoValor=tabEmpleado.getValor("NOMBRES_APELLIDOS");

			}
			
			
			
			public String obtenerIdEmpleado(String ide_geedp){
				String ide_gtemp="";
				
				TablaGenerica tabObtenerIdEmpleado=utilitario.consultar("SELECT ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_geedp="+ide_geedp+" and activo_geedp=true");
				ide_gtemp=tabObtenerIdEmpleado.getValor("IDE_GTEMP");
				return ide_gtemp;
			}



			public int getEstadoJefe() {
				return estadoJefe;
			}



			public void setEstadoJefe(int estadoJefe) {
				this.estadoJefe = estadoJefe;
			}



			public int getEstadoJefeGerente() {
				return estadoJefeGerente;
			}



			public void setEstadoJefeGerente(int estadoJefeGerente) {
				this.estadoJefeGerente = estadoJefeGerente;
			}



			public String getEmpleadoJefe() {
				return empleadoJefe;
			}



			public void setEmpleadoJefe(String empleadoJefe) {
				this.empleadoJefe = empleadoJefe;
			}



			public String getEmpleadoGerente() {
				return empleadoGerente;
			}



			public void setEmpleadoGerente(String empleadoGerente) {
				this.empleadoGerente = empleadoGerente;
			}
			
			
			
			
			public void buscarSolicitudes(){
				tab_permisos.setCondicion("(aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) AND GEN_IDE_GEEDP2 IN ("+ide_geedp_activo+")    AND ANULADO_ASPVH=false and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '2018-01-01' AND '2020-12-01' and tipo_aspvh in(1,4) ");
				tab_permisos.ejecutarSql();
				
				tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
				tab_permiso_justificacion.ejecutarSql();
				utilitario.addUpdate("tab_permisos,tab_permiso_justificacion");
				
			}
			
			
			
	public void seleccionarFechasReporte(){
			 	if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
				if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha())) {
					fechaIni=(cal_fecha_inicial.getFecha());	
					fechaFin=(cal_fecha_final.getFecha());
		  	
				/*tab_permisos.setCondicion("fecha_desde_aspvh between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' AND ANULADO_ASPVH=false and ide_asmot in("+p_permisos_doc_enfermedad+")"
						+ "and (aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) and activo_aspvh=true ");*/
				
				tab_permisos.setCondicion("ANULADO_ASPVH=false and (aprobado_aspvh = FALSE OR aprobado_aspvh IS NULL) and "
		     			+ "FECHA_SOLICITUD_ASPVH  BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and ide_asmot in("+p_permisos_doc_enfermedad+") ");

				
				
				
				tab_permisos.ejecutarSql();		
				tab_permiso_justificacion.setCondicion("IDE_ASPVH="+tab_permisos.getValorSeleccionado());
				tab_permiso_justificacion.ejecutarSql();
				
				
				}	 else {
					utilitario.agregarMensajeInfo("Rango de fechas invalidos",	"Fecha inicial debe ser menor a fecha final");
					return;
				
			} 	
			 	
				}else {
						utilitario.agregarMensajeInfo("Rango de fechas invalidos",	"Debe seleccionar Fecha Inicial y Fecha Final");
						return;
					
				}
		 
		 
		 }	
			
}
