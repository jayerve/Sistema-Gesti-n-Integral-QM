package paq_asistencia;
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

import paq_anticipos.ejb.ServicioAnticipo;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
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
	
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

	
	public pre_permisos_jefe_aprobacion() {
	//botones ocultos	
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("aprobarSolicitud");

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");


		// autocompletar empleado
		//aut_empleado.setId("aut_empleado");
		//String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		//aut_empleado.setAutoCompletar(str_sql_emp);		
		//aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Opciones Autorizacion Permisos: ");
		bar_botones.agregarComponente(eti_colaborador);
		//bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");



		//  PERMISOS (division 1)

		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
	 	tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("GEN_IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false); 
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);
		tab_permisos.getColumna("RAZON_ANULA_ASPVH").setVisible(false);
		tab_permisos.getColumna("DOCUMENTO_ANULA_ASPVH").setVisible(false);
		tab_permisos.getColumna("FECHA_ANULA_ASPVH").setVisible(false);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setVisible(false);
		tab_permisos.getColumna("ANULADO_ASPVH").setVisible(false);
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setVisible(false);
		tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
		tab_permisos.getColumna("IDE_ASMOT").setLectura(true);
		
		tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		
		tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		//		tab_permisos.getColumna("GEN_IDE_GEEDP").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		//GERENTE DE AREA GEN_IDE_GEEDP3
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);

		tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);
		//tab_permisos.getColumna("IDE_ASMOT").setRequerida(true);		
		//tab_permisos.getColumna("IDE_ASMOT").setLectura(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setLectura(true);
	    tab_permisos.getColumna("FECHA_DESDE_ASPVH").setLectura(true);
	    tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setLectura(true);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setLectura(true);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);
		//tab_permisos.getColumna("NRO_HORAS_ASPVH").setLectura(true);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
		tab_permisos.getColumna("DETALLE_ASPVH").setLectura(true);

		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
		tab_permisos.getColumna("IDE_GEEST").setLectura(true);


		tab_permisos.getColumna("NRO_FINES_SEMANA_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_FINES_SEMANA_ASPVH").alinearCentro();

		tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);

		tab_permisos.getColumna("APROBADO_ASPVH").setLectura(true);
		tab_permisos.getColumna("APROBADO_ASPVH").setLectura(true);
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
		tab_permisos.agregarRelacion(tab_permiso_justificacion);


		tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+tabEmpDep.getValor("ide_geedp")+" AND ANULADO_ASPVH=false");
		tab_permisos.setCampoOrden("IDE_ASPVH DESC");
		tab_permisos.dibujar();


		
		
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("BANDEJA DE SOLICITUDES DE PERMISOS / VACACIONES");


		//Permiso de Justificacion 

		tab_permiso_justificacion.setId("tab_permiso_justificacion");
		tab_permiso_justificacion.setTabla("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ", 2);
		tab_permiso_justificacion.getColumna("ACTIVO_ASPEJ").setCheck();
		tab_permiso_justificacion.getColumna(" ACTIVO_ASPEJ").setValorDefecto("true");
		tab_permiso_justificacion.getColumna("ARCHIVO_ASPEJ").setUpload("archivos");
		tab_permiso_justificacion.setCondicion("ide_aspvh="+tab_permisos.getValorSeleccionado());
		tab_permiso_justificacion.setLectura(true);
		tab_permiso_justificacion.dibujar();
		

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_permiso_justificacion);
		pat_panel2.setMensajeWarn("JUSTIFICACION DE PERMISOS  / VACACIONES");

		
		
		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		//div_division.dividir1(tab_permisos);
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		bar_botones.agregarBoton(bot_aprobar_solicitud);
	}

	
	
	public void aprobarSolicitud(){	
		
		
		//if (tab_permisos.getValor("NRO_DIAS_ASPVH").equals("")) {
			
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());

			//if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
				if(tab_permisos.getTotalFilas()>0){
				
				String empleado= aut_empleado.getValor();
					con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
					con_guardar.setTitle("CONFIRMACION APROBACION DE SOLICITUD DE PERMISO");
					con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
				}else{
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
				}		
			//}else{
			//	utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
			//}

	
	}
	
	
		
	public void aceptarAprobarSolicitud(){		

		try {
			utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());
			guardarPantalla();
			con_guardar.cerrar();		
			String ide_anterior=tab_permisos.getValorSeleccionado();		
			tab_permisos.ejecutarSql();
			tab_permisos.setFilaActual(ide_anterior);
			tab_permiso_justificacion.limpiar();
			//aut_empleado.limpiar();
			//utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar

		} catch (Exception e) {
			// TODO Auto-generated catch b
			e.printStackTrace();
			System.out.println("Error al aprobar solicitud "+e);
		}
		
	
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
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
	//	tab_permisos.setCondicion("TIPO_ASPVH=4 AND IDE_GTEMP="+aut_empleado.getValor());
		//tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL)");
		
		TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
		System.out.println("juan : : "+tabEmpDep.getValor("ide_geedp"));
		tab_permisos.setCondicion("(APROBADO_ASPVH = FALSE OR APROBADO_ASPVH IS NULL) AND GEN_IDE_GEEDP2="+tabEmpDep.getValor("ide_geedp")+" AND IDE_GTEMP="+aut_empleado.getValor());
		//tab_permisos.setCondicion("TIPO_ASPVH=2 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_permisos.ejecutarSql();
		actualizarTabla2();
	}


	
	
	
	
	
	
	
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_permisos.limpiar();
		tab_permiso_justificacion.limpiar();
		ide_geedp_activo="";
		//aut_empleado.limpiar();
		sel_cal.Limpiar();

		//utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_permisos.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){					
					tab_permisos.insertar();
					tab_permisos.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_permisos.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "El contrato del empleado no esta activo");
				}					
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Permiso");
			}
		}else if(tab_permiso_justificacion.isFocus()){
			tab_permiso_justificacion.insertar();
		}	
	}


	public boolean validarSolicitudPermiso(){
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
			utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return false;
		}
		if (tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha desde ");
			return false;
		}

		if (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha hasta");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP")==null || tab_permisos.getValor("GEN_IDE_GEEDP").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar Jefe Inmediato");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP2")==null || tab_permisos.getValor("GEN_IDE_GEEDP2").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Jefe de Talento Humano");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP3")==null || tab_permisos.getValor("GEN_IDE_GEEDP3").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Gerente de Area");
			return false;
		}	
		if (tab_permisos.getValor("IDE_ASMOT")==null || tab_permisos.getValor("IDE_ASMOT").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Motivo");
			return false;
		}
		if (tab_permisos.getValor("FECHA_SOLICITUD_ASPVH")==null || tab_permisos.getValor("FECHA_SOLICITUD_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar La fecha de Solicitud");
			return false;
		}
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
			utilitario.agregarMensajeError("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return false;
		}

		return true;
	}

	

	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (aut_empleado.getValor()!=null){		
			//if (validarSolicitudPermiso()){				
				if (tab_permisos.guardar()){
					if (tab_permiso_justificacion.guardar()) {						
						guardarPantalla();

				//	}					
					
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Permiso", "Debe seleccionar un Empleado");
		}
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
	}




	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
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


	
}
