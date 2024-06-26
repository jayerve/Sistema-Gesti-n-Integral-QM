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
import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
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


public class pre_permiso_induvidual_dias extends Pantalla {

	//private Tabla tab_recaudacion = new Tabla();
	private Tabla tab_permisos=new Tabla();
	private AutoCompletar aut_recaudador = new AutoCompletar();
	private AutoCompletar aut_sucursal= new AutoCompletar();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_filtro_activo = new Dialogo();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado_asis=new SeleccionTabla();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	private Tabla tab_permiso_justificacion=new Tabla();
	String ide_empleado="";
	String ide_empleado1="";
	String ide_sucursal="";
	int empleado_entero=0;
	private Dialogo dia_anulado=new Dialogo();
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	//Arreglar metodo retorno a empleado activo
	public pre_permiso_induvidual_dias() {
		ide_empleado=obtenerEmpleado();
		ide_empleado1=filtrarEmpleado(ide_empleado);
		try {
			empleado_entero=pckUtilidades.CConversion.CInt(ide_empleado1);
		} catch (Exception e) {
		}

	if (ide_empleado1==null){
			utilitario.agregarNotificacion("EMPLEADO INACTIVO", "El usuario no contiene Contrato vigente", "");
			return;
	}else
		{
        Etiqueta eti_recaudador = new Etiqueta("EMPLEADO :");
        eti_recaudador.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
        bar_botones.agregarComponente(eti_recaudador);
        
        //autocompletar     
       	aut_recaudador.setId("aut_recaudador");
       	aut_recaudador.setStyle("text-aling:left");
        aut_recaudador.setAutoCompletar("select ide_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp from gth_empleado");
       	aut_recaudador.setValor(ide_empleado);
       	aut_recaudador.setDisabled(true);
       	bar_botones.agregarComponente(aut_recaudador);
        bar_botones.getBot_eliminar().setRendered(false); 	
    	bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");
       	tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.getColumna("TIPO_ASPVH").setValorDefecto("1");// 1 permisos 
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
	    tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
	    tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		
		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " );
		tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
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
		tab_permisos.getColumna("IDE_ASMOT").setRequerida(true);		
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setMetodoChange("CargarFechaHasta");
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setLectura(true);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setRequerida(true);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setMetodoChange("calaculahoras");
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setRequerida(true);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setMetodoChange("calaculahoras");
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setFormatoNumero(2);

		tab_permisos.getColumna("NRO_HORAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_HORAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setRequerida(true);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setEtiqueta();
		tab_permisos.getColumna("NRO_DIAS_ASPVH").alinearCentro();
		tab_permisos.getColumna("IDE_GEEST").setCombo("gen_estados", "IDE_GEEST", "detalle_geest", "");
		tab_permisos.getColumna("IDE_GEEST").setValorDefecto(utilitario.getVariable("p_gen_estado_activo"));
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);
		tab_permisos.getColumna("documento_anula_aspvh").setVisible(false);
		tab_permisos.getColumna("razon_anula_aspvh").setVisible(false);
		tab_permisos.getColumna("fecha_anula_aspvh").setVisible(false);
		tab_permisos.getColumna("fecha_hasta_aspvh").setLectura(true);
		tab_permisos.getColumna("aprobado_tthh_aspvh").setLectura(true);
		tab_permisos.getColumna("aprobado_aspvh").setLectura(true);
		tab_permisos.getColumna("aprobado_tthh_aspvh").setLectura(true);
		tab_permisos.getColumna("anulado_aspvh").setLectura(true);
		tab_permisos.getColumna("activo_aspvh").setLectura(true);
		tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);
		tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("APROBADO_TTHH_ASPVH").setCheck();
	    tab_permisos.setCondicion("TIPO_ASPVH=1 AND IDE_GEEDP="+empleado_entero);
		tab_permisos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		pat_panel1.setMensajeWarn("SOLICITUD DE PERMISOS POR HORAS");


		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel1);
		agregarComponente(div_division);
       	
       
		}
       
	}
		
	public String obtenerEmpleado(){
		String empleado="";
		String empleado1="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_gtemp from sis_usuario where ide_usua= "+utilitario.getVariable("IDE_USUA"));	
			if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			empleado=String.valueOf(list_sql1.get(0));
		}
		return empleado;
		}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
						}
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}


	@Override
	public void actualizar() {
		super.actualizar();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
	}

	String ide_geedp_activo="";
	public String filtrarEmpleado(String ide_empleado){
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(ide_empleado);
		return ide_geedp_activo;
		}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_permisos.isFocus()){
			if (ide_empleado!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){	
					tab_permisos.insertar();
					tab_permisos.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_permisos.setValor("IDE_GTEMP", aut_recaudador.getValor());									
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un empleado valido");
				}					
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Permiso");
			     }
		     }
	}

	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new  SimpleDateFormat ("hh:mm:ss");
			String hora1 = utilitario.getFormatoHora(hora_ini);
			String hora2 = utilitario.getFormatoHora(hora_fin);
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

	public boolean validarSolicitudPermiso(){
		//		if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
		//		utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
		//	return false;
		//}

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

		if (tab_permisos.getValor("HORA_HASTA_ASPVH")==null || tab_permisos.getValor("HORA_HASTA_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la hora hasta");
			return false;
		}
		if (tab_permisos.getValor("HORA_DESDE_ASPVH")==null || tab_permisos.getValor("HORA_DESDE_ASPVH").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la hora desde");
			return false;
		}
		if (isHoraMayor(tab_permisos.getValor("HORA_DESDE_ASPVH"),tab_permisos.getValor("HORA_HASTA_ASPVH"))) {
			utilitario.agregarMensajeInfo("No se puede guardar", "La Hora Inicial no puede ser Menor a Hora Final");
			return false;
		}
		return true;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (ide_empleado!=null){
			System.out.println("ingresa al if de autocompletar: ");
			if (validarSolicitudPermiso()){
				System.out.println("ingresa al if : validarSolicitudPermiso");
				if (tab_permisos.guardar()){
					System.out.println("ingresa al if : tab_permisos");
					guardarPantalla();	
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Permiso", "Debe seleccionar un Empleado");
		}
	}
	@Override
	public void eliminar() {
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
					System.out.println("p_parametro "+ ide_geedp_activo);
					p_parametros.put("IDE_GEEDP",pckUtilidades.CConversion.CInt(ide_geedp_activo));
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


	public void CargarFechaHasta(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);		
		if(tab_permisos.getValor("FECHA_DESDE_ASPVH")!=null && !tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()){
			tab_permisos.setValor("FECHA_HASTA_ASPVH", tab_permisos.getValor("FECHA_DESDE_ASPVH"));
			tab_permisos.setValor("NRO_DIAS_ASPVH", "1");	
			utilitario.addUpdateTabla(tab_permisos, "FECHA_HASTA_ASPVH,NRO_DIAS_ASPVH", "");
		}
	}
	public void CargarFechaHasta(SelectEvent evt){
		tab_permisos.modificar(evt);		
		if(tab_permisos.getValor("FECHA_DESDE_ASPVH")!=null && !tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()){
			tab_permisos.setValor("FECHA_HASTA_ASPVH", tab_permisos.getValor("FECHA_DESDE_ASPVH"));	
			utilitario.addUpdateTabla(tab_permisos, "FECHA_HASTA_ASPVH", "");
		}
	}

	public void calcularDiasPermisos(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);
		//	if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
		//		|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
		//return;
		//}

		if(aut_empleado.getValor()!=null){
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{
				int nro_dias=0;		
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
				tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",nro_dias+"");
				utilitario.addUpdateTabla(tab_permisos, "NRO_DIAS_ASPVH", "");
			}					
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}	
	}

	public void calcularDiasPermisos(SelectEvent evt){
		tab_permisos.modificar(evt);
		if(aut_empleado.getValor()!=null){
			if((tab_permisos.getValor("FECHA_DESDE_ASPVH")==null || tab_permisos.getValor("FECHA_DESDE_ASPVH").isEmpty()) 
					|| (tab_permisos.getValor("FECHA_HASTA_ASPVH")==null || tab_permisos.getValor("FECHA_HASTA_ASPVH").isEmpty())){
				return;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")))){
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{

				double nro_dias=0;		
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_permisos.getValor("FECHA_DESDE_ASPVH")), utilitario.getFecha(tab_permisos.getValor("FECHA_HASTA_ASPVH")));
				tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_DIAS_ASPVH",nro_dias+"");
				utilitario.addUpdateTabla(tab_permisos, "NRO_DIAS_ASPVH", "");
				System.out.println("numero de dias: "+nro_dias);

			}					
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "Ingrese un Empleado");
		}	
	}



	public void calculoHoras(String str_hora_inicial , String str_hora_final){
		try {
			System.out.println("hora inicial"+str_hora_inicial);
			System.out.println("hora inicial"+str_hora_final);
			Date hora_inicial= utilitario.getHora(utilitario.getFormatoHora(str_hora_inicial));
			Date hora_final= utilitario.getHora(utilitario.getFormatoHora(str_hora_final));
			int total_segundos_hora_inicial=(hora_inicial.getHours()*3600)+(hora_inicial.getMinutes()*60) + hora_inicial.getSeconds();
			int total_segundos_hora_final=(hora_final.getHours()*3600)+(hora_final.getMinutes()*60)+hora_final.getSeconds();


			int total_diferencia_segundo=total_segundos_hora_final-total_segundos_hora_inicial;


			int total_horas=total_diferencia_segundo/3600;
			int nuevo_valor=total_diferencia_segundo-(total_horas*3600);
			int total_minutos=nuevo_valor/60;
			int total_segundos=nuevo_valor-(total_minutos*60);

			double total_diferencia_segundos=((total_horas*3600)+(total_minutos*60)+total_segundos);
			double total_diferencia_horas=total_diferencia_segundos/3600;

			tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH",total_diferencia_horas+"");
			utilitario.addUpdateTabla(tab_permisos,"NRO_HORAS_ASPVH", total_diferencia_horas+"");
		} catch (Exception e) {
			// TODO: handle exception
			tab_permisos.setValor(tab_permisos.getFilaActual(),"NRO_HORAS_ASPVH","");
			utilitario.addUpdateTabla(tab_permisos,"NRO_HORAS_ASPVH", "");
		}
	}
	public  void calaculahoras(AjaxBehaviorEvent evt){
		tab_permisos.modificar(evt);		
		if(tab_permisos.getValor("HORA_DESDE_ASPVH")!=null && !tab_permisos.getValor("HORA_DESDE_ASPVH").isEmpty()
				&& tab_permisos.getValor("HORA_HASTA_ASPVH")!=null && !tab_permisos.getValor("HORA_HASTA_ASPVH").isEmpty()){
			if (!isHoraMayor(tab_permisos.getValor("HORA_DESDE_ASPVH"),tab_permisos.getValor("HORA_HASTA_ASPVH"))) {
				calculoHoras(tab_permisos.getValor("HORA_DESDE_ASPVH"), tab_permisos.getValor("HORA_HASTA_ASPVH"));
			}else {
				utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
			}	
		}
	}

	public  void calaculahoras(SelectEvent evt){
		tab_permisos.modificar(evt);
		if (!isHoraMayor(tab_permisos.getValor("HORA_DESDE_ASPVH"),tab_permisos.getValor("HORA_HASTA_ASPVH"))) {
			calculoHoras(tab_permisos.getValor("HORA_DESDE_ASPVH"), tab_permisos.getValor("HORA_HASTA_ASPVH"));
			tab_permisos.setColumnaSuma("NRO_HORAS_ASPVH");
		}
		else {
			utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
		}
	}
	public AutoCompletar getAut_recaudador() {
		return aut_recaudador;
	}

	public void setAut_recaudador(AutoCompletar aut_recaudador) {
		this.aut_recaudador = aut_recaudador;
	}

	public AutoCompletar getAut_sucursal() {
		return aut_sucursal;
	}
	public void setAut_sucursal(AutoCompletar aut_sucursal) {
		this.aut_sucursal = aut_sucursal;
	}

	public ServicioEmpleado getSer_empleado() {
		return ser_empleado;
	}

	public void setSer_empleado(ServicioEmpleado ser_empleado) {
		this.ser_empleado = ser_empleado;
	}

	public String getIde_empleado() {
		return ide_empleado;
	}

	public void setIde_empleado(String ide_empleado) {
		this.ide_empleado = ide_empleado;
	}

	public String getIde_sucursal() {
		return ide_sucursal;
	}

	public void setIde_sucursal(String ide_sucursal) {
		this.ide_sucursal = ide_sucursal;
	}


}
