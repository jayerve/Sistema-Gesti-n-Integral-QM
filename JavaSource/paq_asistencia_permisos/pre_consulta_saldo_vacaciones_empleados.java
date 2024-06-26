/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
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
import portal.servicios.*;
import portal.controladores.*;

import javax.ejb.EJB;



public class pre_consulta_saldo_vacaciones_empleados extends Pantalla {

	
	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	
	
	
	private Tabla tab_tabla_vacacion = new Tabla();
	private Tabla tab_tabla_detalle_vacaccion = new Tabla();
	private Tabla tab_tabla3 = new Tabla();	
	private Tabla tab_tabla_resumen_vacacion_empleado = new Tabla();	
	private Tabla 	tab_asi_vacacion_resumen_empleado = new Tabla();	

	boolean banderaExcedeDias=false;

	
	
	
	
	private Integer anioVacacion=0,mesVacacion=0,diaVacacion=0;
	private Integer anioVacacionSalida=0,mesVacacionSalida=0,diaVacacionSalida=0;
	
	private AutoCompletar aut_empleado=new AutoCompletar();
	private Consulta con_ver_vacaciones = new Consulta();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Consulta con_ver_ficha_vacacion = new Consulta();
	private Dialogo dia_aplica_vacion = new Dialogo();

	double division;
	
	// variables para calculo de vacaciones
	private Integer nda;
	private Double numeroDiasVacacionXAnio = 0.0;
	private Integer ide_gttem;
	private Integer nde;
	private Integer ndeInicialFechaCalculoDiasTomados;
	private double numeroDiasTomados;
	private int p_asi_dias_max_vacaciones_losep;
	private int p_asi_dias_max_vacaciones_codigo_trabajo;
	private int p_asi_dias_anio;
	
	private double dias_pendientes;
	// Calculo de de dias tomados por el empleado
	double numeroDiasTomadosInicial;
	double totalNumeroDiasTomadosInicial;
	double diasPendientesInicialAjuste;
	double nroDiasAjustePeriodo;
	double dias_sumados_aplicados_vacacion;
	private double numeroDiasTomadosTemporal;
	double numeroDiasTomadosFinSemana;
	// Ajuste inicial de dias

	double calculaParteDecimal = 0.0;
	int calcula_dias_vacacion = 0;
	int enteroDias = 0;
	double valor = 0.0;
	int calculoDias = enteroDias = 0;
	int enteroSuma = 0;
	// crear en parametros
		double calculo_dias_max_aplica = 0.0;
		double diasGraciaPuedeTomar = 0.0;
		int numeroInicioFinesSemana = 0;
		int numeroInicioFinesSemanaSolicitados = 0;
		int numeroInicioFinesSemanaPendientes = 0;

		private Object objMatriz;
		private List<Double[]> matriz;
		private Double[] obj = new Double[4];
		
		private Etiqueta etiDiasAcumulados= new Etiqueta("");
		private Etiqueta etiDiasDescontados= new Etiqueta("");
		private Etiqueta etiDiasPendientes= new Etiqueta("");
		
		private Etiqueta etiNroFinesSemana=new Etiqueta("");
		private Etiqueta etiDescuentoFinesSemana=new Etiqueta("");
		private Etiqueta etiNroFinesSemanaPendientes=new Etiqueta("");
		private Etiqueta resumenVacaciones1=new Etiqueta();
		private Etiqueta resumenVacaciones2=new Etiqueta();
		private Etiqueta resumenVacaciones3=new Etiqueta();
		private Etiqueta etiDiasAdicionales = new Etiqueta();
		private Etiqueta periodo = new Etiqueta();
		private Etiqueta dias_vacacion = new Etiqueta();
		private Etiqueta dias_vac_tomados = new Etiqueta();
		private Etiqueta dias_vac_pendientes = new Etiqueta();


		private Etiqueta resumenVacaciones=new Etiqueta();
		PanelGrid gri_resumen_vacaciones_periodo =new PanelGrid();
		PanelGrid gri_anular_dias_solicitados = new PanelGrid();
		PanelGrid gri_resumen_vacaciones_cabecera = new PanelGrid();
		Grid gri_resumen_vacaciones_espaciador=new Grid();
		Grid gri_titulo=new Grid();

	    int bandEntrada=0,bandSalida=0;
		private double totalNumeroDiasAjuste;
			
	
	public pre_consulta_saldo_vacaciones_empleados() {        
		
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
		
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		//rep_reporte.setId("rep_reporte"); 
		//rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		//agregarComponente(rep_reporte);
		//bar_botones.agregarReporte();

		//sef_reporte.setId("sef_reporte");
		//agregarComponente(sef_reporte);
		bar_botones.agregarComponente(new Etiqueta("OPCIONES DE REPORTE "));
		
		Boton bot_ver_vacaciones=new Boton();
			
		bot_ver_vacaciones.setValue("VER RESUMEN VACACIONES");
		bot_ver_vacaciones.setMetodo("verVacaciones");
		bar_botones.agregarComponente(bot_ver_vacaciones);
		
				

		
		
		tab_tabla_resumen_vacacion_empleado.setId("tab_tabla_resumen_vacacion_empleado");
		tab_tabla_resumen_vacacion_empleado.setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
				+ "'' as DIASACUMULADOS,'' as DIASDESCONTADOS, '' as TOTALDIASPENDIENTES "
				+ "from gth_empleado EMP  "
				+ "where activo_gtemp=true  "
				+ "and ide_gtemp =-1 "
				+ "ORDER BY EMP.IDE_GTEMP");
		tab_tabla_resumen_vacacion_empleado.setCampoPrimaria("IDE_GTEMP");
		tab_tabla_resumen_vacacion_empleado.getColumna("IDE_GTEMP").setLongitud(20);
		tab_tabla_resumen_vacacion_empleado.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(35);
		tab_tabla_resumen_vacacion_empleado.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("NOMBRES_APELLIDOS").setLongitud(70);
		tab_tabla_resumen_vacacion_empleado.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");
		tab_tabla_resumen_vacacion_empleado.getColumna("NOMBRES_APELLIDOS").alinearCentro();
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASACUMULADOS").setNombreVisual("DIAS ACUMULADOS");
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASACUMULADOS").setLongitud(35);
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASACUMULADOS").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASDESCONTADOS").setNombreVisual("DIAS DESCONTADOS");
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASDESCONTADOS").setLongitud(35);
		tab_tabla_resumen_vacacion_empleado.getColumna("DIASDESCONTADOS").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.getColumna("TOTALDIASPENDIENTES").setNombreVisual("DIAS PENDIENTES");
		tab_tabla_resumen_vacacion_empleado.getColumna("TOTALDIASPENDIENTES").setLongitud(35);
		tab_tabla_resumen_vacacion_empleado.getColumna("TOTALDIASPENDIENTES").alinearDerecha();
		tab_tabla_resumen_vacacion_empleado.setLectura(true);
		tab_tabla_resumen_vacacion_empleado.dibujar();
		
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setMensajeWarn("RESUMEN DE VACACIÓN POR EMPLEADO");
		pat_panel3.setPanelTabla(tab_tabla_resumen_vacacion_empleado);
		
	
		Division div_division = new Division();		
		div_division.dividir1(pat_panel3);
		agregarComponente(div_division);
		
	
	}
	

	public void limpiar() {
		tab_tabla_resumen_vacacion_empleado.limpiar();
		utilitario.addUpdate("tab_tabla_resumen_vacacion_empleado");// limpia y refresca el autocompletar
	} 
	
	public void verVacaciones(){
		tab_tabla_resumen_vacacion_empleado.setLectura(false);
		tab_tabla_resumen_vacacion_empleado.setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
						+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
						+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
						+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
						+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
						+ "'' as DIASACUMULADOS,'' as DIASDESCONTADOS, '' as TOTALDIASPENDIENTES "
						+ "from gth_empleado EMP  "
						+ "where activo_gtemp=true  "
						+ "and ide_gtemp  in(select ide_gtemp from asi_vacacion where activo_asvac=true)  "
						+ "ORDER BY EMP.IDE_GTEMP");
		tab_tabla_resumen_vacacion_empleado.ejecutarSql();
		
		utilitario.addUpdate("tab_tabla_resumen_vacacion_empleado");
				
				for (int i = 0; i < tab_tabla_resumen_vacacion_empleado.getTotalFilas(); i++) {
					//System.out.println("empleado: "+tabVacacionEmpleado.getValor(i,"IDE_GTEMP")+tabVacacionEmpleado.getValor(i,"NOMBRES_APELLIDOS"));
					TablaGenerica tabVacacion = utilitario.consultar("select * from asi_vacacion where ide_gtemp in("+tab_tabla_resumen_vacacion_empleado.getValor(i,"IDE_GTEMP")+") "
							+ "AND ACTIVO_ASVAC=TRUE");
					//System.out.println("vacacion:  "+tabVacacion.getValor("IDE_ASVAC"));
					
					try {
						getDatosEmpleado(tab_tabla_resumen_vacacion_empleado.getValor(i,"IDE_GTEMP"),false,tabVacacion.getValor("IDE_ASVAC"),i);
						
						
						//getDatosEmpleado("48",false,"45",i);

						} catch (Exception e) {
							//System.out.println("error verVacaciones()");		
							}
				}
				
				if (banderaExcedeDias==true) {
					tab_tabla_resumen_vacacion_empleado.setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
							+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || "
							+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
							+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
							+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
							+ "'' as DIASACUMULADOS,'' as DIASDESCONTADOS, '' as TOTALDIASPENDIENTES "
							+ "from gth_empleado EMP  "
							+ "where EMP.IDE_GTEMP=-1  "
							+ "and ide_gtemp  in(select ide_gtemp from asi_vacacion where activo_asvac=true)  "
							+ "ORDER BY EMP.IDE_GTEMP");
			tab_tabla_resumen_vacacion_empleado.ejecutarSql();
			utilitario.agregarMensaje("Vacaciones de empleados actualizadas", "Por favor vuelva a generar el reporte");
			utilitario.addUpdate("tab_tabla_resumen_vacacion_empleado");
			banderaExcedeDias=false;
				}
				tab_tabla_resumen_vacacion_empleado.setLectura(true);
	}
	
	

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla_vacacion.guardar();
		tab_tabla_detalle_vacaccion.guardar();
		utilitario.getConexion().guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
 if (rep_reporte.getReporteSelecionado().equals("Detalle Vacaciones Por Empleado")){

			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
								rep_reporte.cerrar();
			try {
				getDatosEmpleado(aut_empleado.getValor(),true,tab_tabla_vacacion.getValorSeleccionado(),0);

			} catch (Exception e) {
				// TODO: handle exception
				//System.out.println("error en generacion de reporte aceptarReporte()");
			}

			}
		}
	}	



	public Tabla gettab_tabla_detalle_vacaccion() {
		return tab_tabla_detalle_vacaccion;
	}

	public void settab_tabla_detalle_vacaccion(Tabla tab_tabla_detalle_vacaccion) {
		this.tab_tabla_detalle_vacaccion = tab_tabla_detalle_vacaccion;
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

	
	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
	
	
	

	public void getDatosEmpleado(String IDE_GTEMP,boolean reporte,String ide_asvac,int indice){
		
	    bandEntrada=0;
	    bandSalida=0;
		
/*		nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		TablaGenerica tab_partida = getPartida(IDE_GTEMP);
		
		
		
		TablaGenerica tab_codigo_vacacion_ = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" "
				+ "AND ACTIVO_ASVAC=true");

		String fecha_ingreso_continuidad="",ide_asvac_continuidad="";
		ide_asvac_continuidad=tab_codigo_vacacion_.getValor("ide_asvac");

		TablaGenerica tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
				+ "from asi_vacacion where ide_gtemp=" + IDE_GTEMP+" and activo_asvac=false "
				+ "order by ide_asvac desc");
		if (tab_vacacion_continuidad.getTotalFilas()>0) {
			for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
				if ( tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
				//SI TIENE CONTINUIDAD
					fecha_ingreso_continuidad=tab_codigo_vacacion_.getValor("fecha_ingreso_asvac");
					//if ((tab_vacacion_continuidad.getTotalFilas()-1)==i) {
					//	ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac");
					//}else {
					//	ide_asvac_continuidad+=tab_codigo_vacacion.getValor("ide_asvac")+",";	
					//}
					
					break;
				}else{
				//SI ES LIQUIDACION
					fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
					if ((tab_vacacion_continuidad.getTotalFilas()-1)==i) {
						ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");
					}else {
						ide_asvac_continuidad+=","+tab_vacacion_continuidad.getValor(i,"ide_asvac");	
					}

				}
			}
		}else{
			fecha_ingreso_continuidad=tab_codigo_vacacion_.getValor("fecha_ingreso_asvac");				
		}
		
		
		
		//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
		nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac_continuidad);
	//System.out.println("en metodo "+IDE_GTEMP+" : vacacion"+ide_asvac);
		
		//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
		ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

		// Recalculo los valores para q no se pasen de 360 dias al año
		//nde = getDiasEmpleadoXAnio(nde, nda);
		//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

		// Obtener los periodos de cada empleado 1,2,3 etc
		List<Integer> peridos = getPeriodos(nda, nde);
		
		
		// Obtengo los periodos para el ajuste de dias pendientes del
		// empleado hasta el 30 de abril del 2017
		List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);

	
	
		
		ide_gttem = getGrupoTipoEmpleado(IDE_GTEMP,ide_asvac);
		if (ide_gttem==0) {
			utilitario.agregarMensajeInfo("El empleado seleccionado no contiene acción de personal", "Debe realizar la acción correspondiente para el cálculo de vacaciones del empleado");
			return;
		}
		if (ide_gttem==10) {
			utilitario.agregarMensajeInfo("Periodo de vacaciones inactivo", "Debe activar el periodo de vacaciones del empleado seleccionado");
			return;
		}

		
		//System.out.println("Tipo empleado: "+ide_gttem);
		
		if (ide_gttem == 1) {
			// Asigno el numero de dias max de vacaciones al año 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
			
		//Aumentamos dia por trabajar mas de 5 anios
			if(peridos.size()>5){
				double anio_periodo=peridos.size();
				 division=anio_periodo/5;
				nde=nde+(int)division;
			}
			
		
		}

		if (ide_gttem == 2) {
			// Asigno el numero de dias max de vacaciones al año 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));

		}
		
		
		
		
		
		
		
		// Obtengo el total de mis dias pendientes al restar los dias
		// acumulados-dias tomados
		double sumatotalDiasGenerados = 0.0;
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el presente
		List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el 30 de abril de
		
		List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
		 double sumatotal_vacaciones = 0.0;
		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta la fecha de hoy
		 
		 BigDecimal num1 = new BigDecimal(0);

		for (int i = 0; i < vacacionesPeriodo.size(); i++) {
			sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
	             	
		}

	//	System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
		 double sumatotal_vacacionesCalculoInicial = 0.0;

		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta el 30 de Abril de 2017
		for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
			sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
		}
		//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

		// Devuelve el numero de dias pendientes obtenidos de excel hasta el
		// 30 de abril
	
		
		            numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac_continuidad);
		            totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac_continuidad);
		            diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac_continuidad);
					nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac_continuidad);
					
					
					
					
					  if(numeroDiasTomadosInicial!=0)
					  {
						
						if (numeroDiasTomadosInicial < 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
				            //valor=valor1-sumatotal_vacaciones;
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
							utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
                totalNumeroDiasAjuste=valor;
								

						}
						if (numeroDiasTomadosInicial > 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
				            //valor=valor1-sumatotal_vacaciones;
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
														utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
			                totalNumeroDiasAjuste=valor;

							}

					  
					  }
						


					totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac_continuidad);
     				numeroDiasTomados=totalNumeroDiasAjuste;
					numeroDiasTomadosTemporal = numeroDiasTomados;

					// suma el valor de los dias descontados de la tabla
					// asi_detalle_vacacion
					// Aqui se encuentra el cuadre
					dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
					int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
					double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);

					
				
					
				   numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
					
					
					double resultado_descuento = descuento + numeroDiasTomados;
			
					
					if (ide_gttem == 2) {

						double nro_dias_ajuste_periodo_asvac = 0;
								TablaGenerica tab_codigo_vacacion = utilitario.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
												+ ide_asvac);

								if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 60) {
									//System.out.println("Ingreso a descuento mayor a 60");
									
									BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
									BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
									BigDecimal descuento1= new BigDecimal(descuento);
									BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
									BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);

									BigDecimal  dias= new BigDecimal(60);
									BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));

//System.out.println("Valor exceso losep: "+calculoPasaDiasPendientes);
									if (calculoPasaDiasPendientes.doubleValue()>=0.01) {
								
									
							TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
					         utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
					        +"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
								
									}
					          nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;

					          
								}
							}

			if (ide_gttem == 1) {

								double nro_dias_ajuste_periodo_asvac = 0;
								TablaGenerica tab_codigo_vacacion = utilitario
										.consultar("select ide_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac from asi_vacacion where ide_asvac="
												+ ide_asvac);
							

								if ((sumatotal_vacaciones - (numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana)) > 45) {
									
								//	System.out.println("Ingreso a descuento mayor a 45");

									
									BigDecimal sumatotal_vacaciones1= new BigDecimal(sumatotal_vacaciones);
									BigDecimal numeroDiasTomados1= new BigDecimal(numeroDiasTomados);
									BigDecimal descuento1= new BigDecimal(descuento);
									BigDecimal nroDiasAjustePeriodo1= new BigDecimal(nroDiasAjustePeriodo);
									BigDecimal numeroDiasTomadosFinSemana1= new BigDecimal(numeroDiasTomadosFinSemana);

									BigDecimal  dias= new BigDecimal(45);
									

									BigDecimal calculoPasaDiasPendientes = ((sumatotal_vacaciones1.subtract(numeroDiasTomados1.add(descuento1).add(nroDiasAjustePeriodo1).add(numeroDiasTomadosFinSemana1))).subtract(dias));
									//System.out.println("Valor exceso codigo trabajo: "+calculoPasaDiasPendientes);

									if (calculoPasaDiasPendientes.doubleValue()>=0.01) {

									TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
									utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_acumulado_asdev,observacion_asdev,activo_asdev,anulado_asdev)"
									+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false)");
									}
									nroDiasAjustePeriodo=calculoPasaDiasPendientes.doubleValue()+nroDiasAjustePeriodo;
																	
								}
							}
				 
			       numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
				   numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

					/*
					 * Tomo los parametros creados para realizar los calculos de los
					 * dias pendientes calculo de vacaciones dependiendo si el empleado
					 * pertenece a la losep o al codigo de trabajo
					 */
/*int cont =0;

					List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);
		
					String periodoNuevo="";
					String periodoNuevoEntrada="",periodoNuevoSalida="";
					int valorDiasXAnio=0;
					List<Double[]> matriz = new ArrayList<Double[]>();
						
						Integer dimension = vacacionesPeriodo.size();
						for (Integer i = 0; i < dimension; i++) {
							Double[] obj = new Double[5];
							obj[0] = i.doubleValue() + 1; // periodo
							obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
						//	obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
							obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
						 //   obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
							obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
						//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
							obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
				
					/**
					 * Obtencion de dias pendientes del empleado
					 */
					// System.out.println("numero dias pendientes vacacion: "+
					// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
						//}
				/*	dias_pendientes = getNumeroDiasPendientes(nde,nda, numeroDiasTomados, numeroDiasVacacionXAnio);
					// redondeo el valor
					//dias_pendientes = Math.rint(dias_pendientes * 100) / 100;


					// //////////////////////////////////////////////////7vsvnkkskvdvskvnvdsknsd/////////////////////////////////////////////////7
					dias_pendientes = (sumatotal_vacaciones - numeroDiasTomados);
					//dias_pendientes = Math.rint(dias_pendientes * 100) / 100;


					numeroInicioFinesSemana = (int) ((sumatotal_vacaciones*4) / 30);
					numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);
					numeroInicioFinesSemanaPendientes = numeroInicioFinesSemana
							- numeroInicioFinesSemanaSolicitados;
					sumatotal_vacaciones = Math.rint(sumatotal_vacaciones * 100) / 100;

					tab_tabla_resumen_vacacion_empleado.setValor(indice, "DIASACUMULADOS",""+sumatotal_vacaciones);
					tab_tabla_resumen_vacacion_empleado.setValor(indice, "DIASDESCONTADOS",""+numeroDiasTomados);
					tab_tabla_resumen_vacacion_empleado.setValor(indice, "TOTALDIASPENDIENTES",""+dias_pendientes);					
								

					

					if (ide_gttem==1) {
									
					//if ((int)division>0) {
					//		gri_anular_dias_solicitados.getChildren().add(new Etiqueta("Nro dias Adicionales"));
			//gri_anular_dias_solicitados.getChildren().add(new Etiqueta(""+(int)division));
			
						
					
				//	}
					

					
					
					}*/
	    
	    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    
	    
	    bandEntrada=0;
	    bandSalida=0;
		
		nda = Integer.parseInt(utilitario.getVariable("p_asi_dias_anio"));
		TablaGenerica tab_partida = getPartida(IDE_GTEMP);
		
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
				if (tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
				//SI TIENE CONTINUIDAD
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
		
		
 

		
		//nde = getNumeroDiasEmpleado(tab_partida.getValor("IDE_GEEDP"));
		nde = getNumeroDiasEmpleado(IDE_GTEMP,ide_asvac_continuidad,fecha_ingreso_continuidad,fecha_fin_continuidad);
		//ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(tab_partida.getValor("IDE_GEEDP"));
		ndeInicialFechaCalculoDiasTomados = getNumeroDiasEmpleadoCalculoInicial(IDE_GTEMP,ide_asvac);

		// Recalculo los valores para q no se pasen de 360 dias al año
		//nde = getDiasEmpleadoXAnio(nde, nda);
		//ndeInicialFechaCalculoDiasTomados = getDiasEmpleadoXAnio(ndeInicialFechaCalculoDiasTomados, nda);

		// Obtener los periodos de cada empleado 1,2,3 etc
		List<Integer> peridos = getPeriodos(nda, nde);
		
		
		// Obtengo los periodos para el ajuste de dias pendientes del
		// empleado hasta el 30 de abril del 2017
		List<Integer> peridosCalculoIncial = getPeriodos(nda, ndeInicialFechaCalculoDiasTomados);

	
	
		
		ide_gttem = getGrupoTipoEmpleado(IDE_GTEMP,ide_asvac);
		if (ide_gttem==0) {
			utilitario.agregarMensajeInfo("El empleado seleccionado no contiene acción de personal", "Debe realizar la acción correspondiente para el cálculo de vacaciones del empleado");
			return;
		}
		if (ide_gttem==10) {
			utilitario.agregarMensajeInfo("Periodo de vacaciones inactivo", "Debe activar el periodo de vacaciones del empleado seleccionado");
			return;
		}

		
		//System.out.println("Tipo empleado: "+ide_gttem);
		String banderaCodigo="";
		
		//Dias adicionelae para Codigo de Trabajo
		
		
		if (ide_gttem == 1) {
			// Asigno el numero de dias max de vacaciones al año 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_codigo"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_codigo_trabajo = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_codigo_trabajo"));
			banderaCodigo="true";	
			
		//Aumentamos dia por trabajar mas de 5 anios
		/*	if(peridos.size()>5){
				double anio_periodo=peridos.size();
				 division=anio_periodo/5;
				nde=nde+(int)division;
			}*/
			
		
		}

		if (ide_gttem == 2) {
			// Asigno el numero de dias max de vacaciones al año 15 dias
			numeroDiasVacacionXAnio = Double.parseDouble(utilitario.getVariable("p_asi_dias_losep"));
			// Los dias maximo q puede acumular 45 dias por tres periodos
			p_asi_dias_max_vacaciones_losep = Integer.parseInt(utilitario.getVariable("p_asi_dias_max_vacaciones_losep"));
			banderaCodigo=null;	

		}
		
			
		
		
		// Obtengo el total de mis dias pendientes al restar los dias
		// acumulados-dias tomados
		double sumatotalDiasGenerados = 0.0;
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el presente
		List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos,numeroDiasVacacionXAnio);
		// Obtengo el numero de periodos y le asigno a cada uno los 30 dias
		// que le corresponde y los dias generados hasta el 30 de abril de
		 
		List<Double> vacacionesPeriodoCalculoInicial = getVacacionesXPeriodo(nda,ndeInicialFechaCalculoDiasTomados,peridosCalculoIncial, numeroDiasVacacionXAnio);
		 double sumatotal_vacaciones = 0.0;
		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta la fecha de hoy
		 
	//	 BigDecimal num1 = new BigDecimal(0);

	//	for (int i = 0; i < vacacionesPeriodo.size(); i++) {
	//		sumatotal_vacaciones = (sumatotal_vacaciones	+ vacacionesPeriodo.get(i));
	///             	
	//	}

	//	System.out.println("Suma vacaciones hasta Fecha Actual: "+ sumatotal_vacaciones);
	
		double sumatotal_vacacionesCalculoInicial = 0.0;

		// Asigno la sumatoria de total de mis dias acumulados a vacacion
		// desde la fecha de ingreso hasta el 30 de Abril de 2017
		for (int i = 0; i < vacacionesPeriodoCalculoInicial.size(); i++) {
			sumatotal_vacacionesCalculoInicial = sumatotal_vacacionesCalculoInicial+ vacacionesPeriodoCalculoInicial.get(i);
		}
		//System.out.println("Suma vacaciones hasta 31 de Mayo: "+ sumatotal_vacacionesCalculoInicial);

		// Devuelve el numero de dias pendientes obtenidos de excel hasta el
		// 30 de abril
	
					//numeroDiasTomadosInicial  = tabla asi_vacacion (dias_tomados_asvac)
		            numeroDiasTomadosInicial = getNumeroDiasPendientesInicial(IDE_GTEMP,ide_asvac_continuidad);
					//totalNumeroDiasAjuste  = tabla asi_vacacion (nro_dias_ajuste_asvac)
					totalNumeroDiasAjuste = getNumeroDiasAjusteEmpleado(IDE_GTEMP,ide_asvac_continuidad);
					//nroDiasAjustePeriodo  = tabla ASI_DETALLE_VACACION (dia_acumulado_asdev)
					nroDiasAjustePeriodo = nroDiasAjustePeriodo(IDE_GTEMP,ide_asvac_continuidad);
					//diasPendientesInicialAjuste  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
					diasPendientesInicialAjuste = getNumeroDiasPendientesInicialAjuste(IDE_GTEMP,ide_asvac_continuidad);
					
				    //Numero de dias 
					if(numeroDiasTomadosInicial!=0.00)
					  {
						if (numeroDiasTomadosInicial < 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
							utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
							totalNumeroDiasAjuste=valor;
						}
						if (numeroDiasTomadosInicial > 0) {
							double valor = 0.0;
							double valor1 = 0.0;
							valor = sumatotal_vacacionesCalculoInicial - (numeroDiasTomadosInicial);
							utilitario.getConexion().ejecutarSql("update asi_vacacion set dias_tomados_asvac=0.0, nro_dias_ajuste_asvac="+ valor + " where ide_GTEMP="+IDE_GTEMP );	
														utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");
			                totalNumeroDiasAjuste=valor;
							}
				     }
					//totalNumeroDiasTomadosInicial  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
				   //Dias con SIN de semana 
					totalNumeroDiasTomadosInicial =getNumeroDiasTomados(IDE_GTEMP,ide_asvac_continuidad);
					//nro_dias_ajuste_asvac DE LA TABALA ASI VACACION
     				numeroDiasTomados=totalNumeroDiasAjuste;
					numeroDiasTomadosTemporal = numeroDiasTomados;

					// suma el valor de los dias descontados de la tabla
					// asi_detalle_vacacion
					// Aqui se encuentra el cuadre
					dias_sumados_aplicados_vacacion = totalNumeroDiasTomadosInicial;
					int p_etn1 = ((int) dias_sumados_aplicados_vacacion / 5);
					//EL NUMERO DE DIAS POR FIN DE SEMANA A DESCONTAR
					double descuento = dias_sumados_aplicados_vacacion + (p_etn1 * 2);
					//totalNumeroDiasTomadosInicial  = tabla ASI_DETALLE_VACACION (DIA_SOLICITADO_ASDEV)
				    numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
				    double resultado_descuento = descuento + numeroDiasTomados;
							 
			        numeroDiasTomadosFinSemana=getNumeroDiasTomadosFinSemana(IDE_GTEMP,ide_asvac_continuidad);
				    numeroDiasTomados = numeroDiasTomados + descuento+ nroDiasAjustePeriodo+numeroDiasTomadosFinSemana;

					/*
					 * Tomo los parametros creados para realizar los calculos de los
					 * dias pendientes calculo de vacaciones dependiendo si el empleado
					 * pertenece a la losep o al codigo de trabajo
					 */


					//List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos,numeroDiasVacacionXAnio);

					String periodoNuevo="";
					String periodoNuevoEntrada="",periodoNuevoSalida="";
					int valorDiasXAnio=0;
					List<Double[]> matriz = new ArrayList<Double[]>();

				
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
//					Integer dimension = vacacionesPeriodo.size();
					//Variables para el calculo de dias extra
					Double total_dias_extra=0.00,dias_extra_temp=0.00;
					int valor_entero_dias_extra=0,valor_total_dias_extra=0;
					Double dias_pendientes_resumen=0.0,dias_acumulados=0.0;
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
						guardarPantalla();
						if (reporte==false) {
						 utilitario.addUpdate("dia_aplica_vacion");
						}
					
					/**
					 * Obtencion de dias pendientes del empleado
					 */
					// System.out.println("numero dias pendientes vacacion: "+
					// servicioVacaciones.getNumeroDiasPendientes(nde,nda,numeroDiasTomados,numeroDiasVacacionXAnio));
					
						
						double dias=0.0,dias_extra=0.0,vacaciones_extra=0.0;
						int dias_final_extra=0;
					    if (ide_gttem==1) {
					    	double dias_extra_=(sumatotal_vacaciones/5);
							dias_extra=dias_extra_;
							int diasE=pckUtilidades.CConversion.CInt(dias_extra);
						}
						
										
					TablaGenerica tab_resumen=utilitario.consultar("select a.nro_dias_vacacion_asvre,a.nro_dias_pendientes_asvre  "
							+ "from  "
							+ "(select ide_asvac, sum(nro_dias_vacacion_asvre) as nro_dias_vacacion_asvre,sum(nro_dias_pendientes_asvre) as nro_dias_pendientes_asvre  "
							+ "from asi_vacacion_resumen_empleado  "
							+ "where activo_asvre=true  "
							+ "and ide_asvac="+ide_asvac+"   "
							+ "group  by ide_asvac) a");
					
					
					dias_acumulados=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"));
			
					Double valor=0.00;
					
					
					
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
				
							
					
					if (((int)valor_total_dias_extra)>1) {
						banderaCodigo="true";
					}else {
						banderaCodigo="false";
					}
					
					
					
					 dias_acumulados=Double.parseDouble(tab_resumen.getValor("nro_dias_vacacion_asvre"))+(int)valor_total_dias_extra;
					dias_pendientes_resumen=dias_acumulados-numeroDiasTomados;
					
					/*if (dias_pendientes_resumen<0.00 || dias_pendientes_resumen<0) {
						TablaGenerica tab_resumen_vacaciones2=utilitario.consultar("SELECT ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre,  "
								+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre,  "
								+ "ide_periodo_asvre, "
								+ "base_imponible_asvre, dias_trabajados_liquidacion_asvre,  "
								+ "activo_liquidacion, valor_pagar_asvre, valor_dia_asvre, ide_gtemp_asvac,  "
								+ "ide_geedp_asvac  "
								+ "FROM asi_vacacion_resumen_empleado "
								+ "where activo_asvre=true "  
								+ "and ide_asvac="+ide_asvac+" "
								+ "order by ide_asvre asc");
						
						int indice=0;
						indice=tab_resumen_vacaciones2.getTotalFilas()-1;
						utilitario.getConexion().ejecutarSql("UPDATE asi_vacacion_resumen_empleado SET "
								+ "nro_dias_pendientes_asvre="+dias_pendientes_resumen+"  "
								+ "where "
								+ "ide_asvre="+tab_resumen_vacaciones2.getValor(indice,"ide_asvre"));
					}*/
					
					int numeroInicioFinesSemanaResumen = (int) (((dias_acumulados*4)+dias_final_extra) / 30);						
					//numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);
					int numeroInicioFinesSemanaPendientesResumen =0;
					numeroInicioFinesSemanaSolicitados=0;
					numeroInicioFinesSemanaSolicitados = (int) ((numeroDiasTomados*4) / 30);

					numeroInicioFinesSemanaPendientesResumen = numeroInicioFinesSemanaResumen - numeroInicioFinesSemanaSolicitados;
					
					if (ide_gttem == 2) {
						double nro_dias_ajuste_periodo_asvac = 0;						
								if (dias_pendientes_resumen > 60 || dias_pendientes_resumen > 60.00 ) {
									Double calculoPasaDiasPendientes =  Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen,3))-60.00;
									if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0) {
						TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
					    utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
					    +"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false,true)");
							//	utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
							//	return;
					    banderaExcedeDias=true;
									}
					    		}
							}

			if (ide_gttem == 1) {
								double nro_dias_ajuste_periodo_asvac = 0;
								if ((dias_pendientes_resumen+ ((int)valor_total_dias_extra))> 45 || (dias_pendientes_resumen+ ((int)valor_total_dias_extra)) > 45.00 ) {
									Double calculoPasaDiasPendientes = Double.parseDouble(utilitario.getFormatoNumero(dias_pendientes_resumen,3))-45.00;
									if (calculoPasaDiasPendientes.doubleValue()>0.00 || calculoPasaDiasPendientes.doubleValue()>0.0) {
									TablaGenerica tab_codigo =utilitario.consultar(servicioCodigoMaximo("asi_detalle_vacacion", "ide_asdev"));
									utilitario.getConexion().ejecutarSql("insert into asi_detalle_vacacion (ide_asdev,ide_asvac,fecha_novedad_asdev,dia_descontado_asdev,observacion_asdev,activo_asdev,anulado_asdev,fin_semana_asdev)"
									+"values ( "+tab_codigo.getValor("codigo")+","+ide_asvac+",'"+utilitario.getFechaActual()+"',"+(calculoPasaDiasPendientes)+",'APLICACIÓN DESCUENTO POR EXCESO DE DIAS' ,true, false, true)");
								//	utilitario.agregarMensaje("Las vacaciones del empleado seleccionado ha sido actualizada", "Presione nuevamente el boton");
								//	return;
								    banderaExcedeDias=true;
									}
								}
							}


			/*p_parametros.put("IDE_GTEMP",Long.parseLong(aut_empleado.getValor()));
			p_parametros.put("titulo", "DETALLE DE VACACIONES POR EMPLEADO");
			//p_parametros.put("diasacumulados",""+utilitario.getFormatoNumero(sumatotal_vacaciones,2));
			p_parametros.put("diasacumulados",""+utilitario.getFormatoNumero(dias_acumulados,2));
			p_parametros.put("total",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));

			p_parametros.put("nrofinesdesemana",""+numeroInicioFinesSemanaResumen);
			p_parametros.put("diasdescontados",""+(Math.rint(numeroDiasTomados * 100) / 100));
			p_parametros.put("descuentofinesdesemana",""+numeroInicioFinesSemanaSolicitados);
			//p_parametros.put("totaldiaspendientes",""+(Math.rint((dias_pendientes) * 100) / 100));
			p_parametros.put("totaldiaspendientes",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));
			p_parametros.put("nrofinessemanapendientes",""+numeroInicioFinesSemanaPendientesResumen);
			p_parametros.put("ide_asvac",Integer.parseInt(ide_asvac));
			p_parametros.put("p_coordinador_tthh",utilitario.getVariable("p_gth_coordinador_tthh"));
			p_parametros.put("p_analista_tthh",utilitario.getVariable("p_gth_analista_tthh_vacaciones"));
			p_parametros.put("p_especialiista_tthh",utilitario.getVariable("p_especialiista_tthh"));
			p_parametros.put("dia_extra",(int)valor_total_dias_extra);
			p_parametros.put("fecha_ingreso_asvac",fecha_ingreso_continuidad);
			p_parametros.put("banderaCodigo",banderaCodigo);*/
			
			
			
			tab_tabla_resumen_vacacion_empleado.setValor(indice, "DIASACUMULADOS",""+utilitario.getFormatoNumero(dias_acumulados,2));
			tab_tabla_resumen_vacacion_empleado.setValor(indice, "DIASDESCONTADOS",""+(Math.rint(numeroDiasTomados * 100) / 100));
			tab_tabla_resumen_vacacion_empleado.setValor(indice, "TOTALDIASPENDIENTES",""+(Math.rint((dias_pendientes_resumen) * 100) / 100));		
	    
	    
			}
			
				
				
			
	   public Tabla getTab_tabla_resumen_vacacion_empleado() {
		return tab_tabla_resumen_vacacion_empleado;
	}
	
	public void setTab_tabla_resumen_vacacion_empleado(
			Tabla tab_tabla_resumen_vacacion_empleado) {
		this.tab_tabla_resumen_vacacion_empleado = tab_tabla_resumen_vacacion_empleado;
	}
	
	   public TablaGenerica getPartida(String IDE_GTEMP){
	        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GTEMP="+IDE_GTEMP+" ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
	        
	        
///METODO PARA SABER LA ACCION DEL EMPLEADO 	        
	        
	        /*String cambioContratoPorEmpleado="";
			//tabla gen_accion_motivo_empleado  
			TablaGenerica tab_accion_motivo_empleado=null;
			//for (int i = tab_empleado_departamento_par.getTotalFilas()-1; i < tab_empleado_departamento_par.getTotalFilas(); i++) {
				cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor((Integer)(tab_empleado_departamento_par.getTotalFilas()-1),"IDE_GEDED");
				//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
				tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
						+ "FROM gen_detalle_empleado_departame "
						+ "where ide_geded  in("+cambioContratoPorEmpleado+") ");

				//Validacion de que tenga terminacion de contrato
				String motivoContratoPorEmpleado="";
				boolean estadoTerminacionContrato=false;
			//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
					motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
					if (motivoContratoPorEmpleado.equals("19")) {
						estadoTerminacionContrato=true;
					}*/

	        
	    }
	
	 //CALCULO DE NUMERO DE DIAS 
	    
		/**
		 * calcular numero de dias calendario que el empleado lleva en la empresa
		 * @param fechaIngresoEmpleado
		 * @return
		 */
	 /*   public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC){
	    	
	    	
	    	
			// TablaGenerica tab_periodo =
			// utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=TRUE AND IDE_GEEDP="
			// + IDE_GEEDP + ")");
			// String dateInString = "2016-02-02";

			TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac from asi_vacacion where  "
					+ "ide_gtemp=" + IDE_GEEDP+" and activo_asvac=true");

			
			String fecha_ingreso_continuidad="";
			TablaGenerica tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
					+ "from asi_vacacion where ide_gtemp=" + IDE_GEEDP+" and activo_asvac=false "
					+ "order by ide_asvac desc");
			if (tab_vacacion_continuidad.getTotalFilas()>0) {
				for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
					if ( tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
					//SI TIENE CONTINUIDAD
						fecha_ingreso_continuidad=tab_periodo.getValor("fecha_ingreso_asvac");
						break;
					}else{
					//SI ES LIQUIDACION
						fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
					}
					
				}
			}else {
				fecha_ingreso_continuidad=tab_periodo.getValor("fecha_ingreso_asvac");
			}
			
			
			
		
	    	//TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
	    	//		+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TablaGenerica tab_partida = getPartida(IDE_GEEDP);

		       
	    	
			
	       String dateInString = "";
	       if ((fecha_ingreso_continuidad==null) || (fecha_ingreso_continuidad.equals("")) || (fecha_ingreso_continuidad.isEmpty()) ) {
	    	   return 0;
	       }else {
	    	   dateInString=fecha_ingreso_continuidad;
	       }
	       
	       boolean activo_asvac=false;
	       String fecha_finiquito=null;
	       
	       	 int diaFin=0;
	    		 int mesFin=0; // 0 Enero, 11 Diciembre
	    		 int anioFin=0;
		        
	    
	        
	   		Date date = null;
	   		Date dateFechaCalculoFiniquito = null;
	    		/**
	    		 * Le asigno a una variable de tipo date la fecha de ingreso
	    		 */
	    			
	    /*try {
	    			date = sdf.parse(dateInString);
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		
	    		
	    		
	   			int sumaValor=0;
	       	 try {
				if (tab_periodo.getValor("fecha_finiquito_asvac")==null || tab_periodo.getValor("fecha_finiquito_asvac").isEmpty()){
						Calendar calendarFin = Calendar.getInstance();
		        		calendarFin.setTime(utilitario.getDate());
		        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		        		 anioFin = calendarFin.get(Calendar.YEAR);
				fecha_finiquito=utilitario.getFechaActual();
				
				}else{
			   fecha_finiquito = tab_periodo.getValor("fecha_finiquito_asvac");
				

						Calendar calendarFin = Calendar.getInstance();
	        			calendarFin.setTime(dateFechaCalculoFiniquito);
	        			 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	            		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	            		 anioFin = calendarFin.get(Calendar.YEAR);
				}
				
								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error en calculo dias emppleado");			}
	       		
	
			
	       	 
			   if (utilitario.getDia(fecha_finiquito)==31) {
				   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), -1));
								sumaValor=-1;	
			   }
			   
			   if (utilitario.getMes(fecha_finiquito)==2 || utilitario.getMes(fecha_finiquito)==02) {
				
				  
			   if (utilitario.getDia(fecha_finiquito)==29) {
				   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
				   sumaValor=+1;		
			   }
			   
			   
			   if (utilitario.getDia(fecha_finiquito)==28) {
				   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 2));
							sumaValor=2;		
			   }
			   
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

               				returnValue =(((returnValue*30)+diasPorMes))+1+sumaValor;
               				//System.out.println("Total dias: " + returnValue);
        				 return returnValue;
	    		
	    		


	    }*/
	    
	   
	   
	   
	   
	   public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC,String fecha_ingreso_asvac,String fecha_finiquito_asvac){
			
		    //	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
		    //			+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
	   
	   
	   
	   
	    //CALCULO DE NUMERO DE DIAS 
	      
	    	/**
	    	 * calcula el numero de dias calendario que el empleado lleva en la empresa desde la fecha ingreso hasta el 30 de abril del 2017
	    	 * @param fechaIngresoEmpleado
	    	 * @return
	    	 */
	        public Integer getNumeroDiasEmpleadoCalculoInicial(String IDE_GEEDP,String IDE_ASVAC){
	           //TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where  IDE_GEEDP=" + IDE_GEEDP + ")");        
		    	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac "
		    			+ "from asi_vacacion where  IDE_ASVAC="+IDE_ASVAC);        

	           
	           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		       String dateInString = tab_periodo.getValor("fecha_ingreso_asvac");
	      		String dateInStringFechaCalculoInicial="2017-05-30";

	      		Date date = null;
	        	//Fecha calculo de vacaciones hasta el 30 de abril
	       		Date dateFechaCalculoInicial=null;	
	        		/**
	        		 * Le asigno a una variable de tipo date la fecha de ingreso
	        		 */
	        			
	        		try {
	        			date = sdf.parse(dateInString);
	        			dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
	        		} catch (ParseException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}

		    		
	        		Calendar calendarInicio = Calendar.getInstance();
	        		calendarInicio.setTime(date);
	        		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
	        		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		int anioInicio = calendarInicio.get(Calendar.YEAR);
	        		
	        		
	        		// Fecha fin
	        		Calendar calendarFin = Calendar.getInstance();
	        		calendarFin.setTime(dateFechaCalculoInicial);
	        		int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	        		int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		int anioFin = calendarFin.get(Calendar.YEAR);
	        		
	        		
				    
	        		int anios = 0;
	        		int mesesPorAnio = 0;
	        		int diasPorMes = 0;
	        		int diasTipoMes = 0;
	        		diasTipoMes=30;
	        		// Calculo de días del mes

		if (mesInicio <= mesFin) {
	        				anios = anioFin - anioInicio;
	        				if (diaInicio <= diaFin) {
	        					mesesPorAnio = mesFin - mesInicio;
	        					diasPorMes = diaFin - diaInicio;
				    	}else{
	        					if (mesFin == mesInicio) {
	        						anios = anios - 1;
	        					}
	        					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				}
	        			} else {
	        				anios = anioFin - anioInicio - 1;
	        				if (diaInicio > diaFin) {
	        					mesesPorAnio = mesFin - mesInicio - 1 + 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				} else {
	        					mesesPorAnio = mesFin - mesInicio + 12;
	        					diasPorMes = diaFin - diaInicio;
				    	}
					}
	        //		System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");		
	       // 		System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
	        	 
	        		int returnValue = -1;
	        				returnValue = anios * 12 + mesesPorAnio;
	       				//System.out.println("Total meses anteriores: " + returnValue + " Meses.");
	        				returnValue =(((returnValue*30)+diasPorMes)+1);
	        				 //System.out.println("Total dias anteriore: " + returnValue);

	        				 return returnValue;
		    		
	        }
	        
	        
	        

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
	    			
	    			
	    		    /**
	    		     * Metodo para obtener al grupo que pertenece el empleado 1: Codigo de trabajo, 2: Losep, 3:Pasante
	    		     * @param IDE_GEEDP
	    		     * @return
	    		     */

	    		   public Integer getGrupoTipoEmpleado(String IDE_GTEMP,String ide_asvac){
	    			   Integer ide_gttem=0;
	    			   TablaGenerica tab_tipo_empleado=null;
	    			   boolean estadoTerminacion=false;
	    			   boolean fechaEntradaEmpleado=false,fechaSalidaEmpleado=false;
		 			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,IDE_GTEMP,FECHA_INGRESO_ASVAC,FECHA_FINIQUITO_ASVAC, "
		 			    		+ "ACTIVO_ASVAC"
		 			    		+ " FROM ASI_VACACION WHERE IDE_ASVAC=" + ide_asvac+ " ");  
		 			    
		 			    if (tab_periodo.getValor("ACTIVO_ASVAC").equals("false")) {
							String fecha_ingreso_asvac="",fecha_finiquito_asvac="";
		 			    	if ((tab_periodo.getValor("FECHA_INGRESO_ASVAC")==null) || (tab_periodo.getValor("FECHA_INGRESO_ASVAC").equals("") || (tab_periodo.getValor("FECHA_INGRESO_ASVAC").isEmpty()))) {
		 			    		fechaEntradaEmpleado=false;
							}else {
								fechaEntradaEmpleado=true;
							}
		 			    	if ((tab_periodo.getValor("FECHA_FINIQUITO_ASVAC")==null) || (tab_periodo.getValor("FECHA_FINIQUITO_ASVAC").equals("") || (tab_periodo.getValor("FECHA_FINIQUITO_ASVAC").isEmpty()))) {
		 			    		fechaSalidaEmpleado=false;
							}else {
								fechaSalidaEmpleado=true;
							}
		 			    	
		 			    	
		 			    	if (fechaEntradaEmpleado==true && fechaSalidaEmpleado==true) {
		 			    		fecha_ingreso_asvac=tab_periodo.getValor("FECHA_INGRESO_ASVAC");
								fecha_finiquito_asvac=tab_periodo.getValor("FECHA_FINIQUITO_ASVAC");

								TablaGenerica tab_empleado_departamento_par=null;
								tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,ide_geded  "
										+ "from gen_empleados_departamento_par  "
										+ "where ide_gtemp="+IDE_GTEMP+" "
												+ "order by ide_geedp DESC ");
												//+ "limit 1");
								if (tab_empleado_departamento_par.getTotalFilas()==0) {
									return 0;
								}
								String cambioContratoPorEmpleado="";
								//tabla gen_accion_motivo_empleado  
								TablaGenerica tab_accion_motivo_empleado=null;
								//for (int i = tab_empleado_departamento_par.getTotal
								for (int i = 0; i < tab_empleado_departamento_par.getTotalFilas(); i++) {
							
								
								cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(i,"IDE_GEDED");
								//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
								tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
										+ "FROM gen_detalle_empleado_departame "
										+ "where ide_geded  in("+cambioContratoPorEmpleado+") ORDER BY IDE_GEDED DESC ");
								String motivoContratoPorEmpleado="";
								boolean estadoTerminacionContrato=false;
							//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
									motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
									
									
								TablaGenerica tabTerminacionContrato=null;
								tabTerminacionContrato=utilitario.consultar("SELECT geame.ide_geame, geame.ide_geaed, geame.ide_gemed,  "
										+ "geame.activo_geame,geame.anterior_geame,geaed.detalle_geaed "
										+ "FROM  "
										+ "gen_accion_motivo_empleado geame "
										+ "left join gen_accion_empleado_depa  geaed on geaed.ide_geaed=geame.ide_geaed "
										+ "where geaed.detalle_geaed like '%TERMINACION LABORAL%'");
								
								
								for (int j = 0; j < tabTerminacionContrato.getTotalFilas(); j++) {
									
									if (tabTerminacionContrato.getValor(j, "IDE_GEAME").equals(motivoContratoPorEmpleado)) {
										estadoTerminacion=true;
										j=tabTerminacionContrato.getTotalFilas();
									}else {
										estadoTerminacion=false;
									}
								}
								
								
								
							//		if (motivoContratoPorEmpleado.equals("19") || motivoContratoPorEmpleado.equals("6")) {
								if (estadoTerminacion==true) {
										tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +" "
						    			   		+ " and IDE_GEDED ="+tab_empleado_departamento_par.getValor(i,"IDE_GEDED")+" "
						    			   		+ " ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
										i=tab_empleado_departamento_par.getTotalFilas();
					    		        ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      

										}else {
										//tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +" "
						    			//   		+ " and fecha_geedp='"+fecha_ingreso_asvac+"' "
						    			//   		+ " ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
									}
								
								
					    
							}
								
								
							}else {
								return 10;
								
								
							}
						}else {
			    			   tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GTEMP  +"  "
			    			   		+ "AND ACTIVO_GEEDP=TRUE "
			    			   		+ "ORDER BY IDE_GEEDP DESC  LIMIT 1 ");   
			    			   //tab_tipo_empleado.imprimirSql();

						}
		 			    

	    			   
	    		        ide_gttem= Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));      
	    		       return ide_gttem;
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
	    		 		
	    		 		
	    		 		 /**
	    		 	    * Metodo extrae el numero de dias pendientes para el calculo de dias tomados hasta el 30 de abril obtenidos del excel
	    		 	    * @param IDE_GEEDP
	    		 	    * @return
	    		 	    */
	    		 	   
	    		 	            double valorIncialDiasTomados;
	    		 		  public double getNumeroDiasPendientesInicial(String IDE_GEEDP,String ide_asvac) {
	    		 			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
	    		 		        if (!tab_periodo.isEmpty()) {
		    		 				TablaGenerica tab_valor_InicialDiasTomados = utilitario.consultar("select ide_gtemp,sum(dias_tomados_asvac) as dias_tomados_asvac  " + " "
		    		 						+ "from asi_vacacion  "
		    		 						+ "where IDE_ASVAC in("+ide_asvac+") "
		    		 						+ "group by ide_gtemp");
		    		 						//+ "" + tab_periodo.getValor("IDE_ASVAC"));
	    		 		       	if (tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac")==null || tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac").isEmpty())
	    		 		       	{
	    		 		       	valorIncialDiasTomados=0.0;
	    		 	    		}else {
	    		 	       		valorIncialDiasTomados=Double.parseDouble(tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac"));
	    		 	    		}
	    		 		       //	System.out.println(valorIncialDiasTomados);
	    		 		        return valorIncialDiasTomados;	
	    		 		        }

	    		 		        return 0;
	    		 	    		
	    		 		  }
	    		 	    		 		  
	    		 		   /*
	    		 		    * Metodo devuelve los dias pendientes calculados a partir del excel de cada empleado
	    		 		    */
	    		 		   
	    		 		   public double getNumeroDiasPendientesInicialAjuste(String IDE_GEEDP,String ide_asvac) {     
	    		 			   double numeroDiasTomadoss=0.0;
	    		 			   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 		        if (!tab_periodo.isEmpty()) {
	    		 		        	TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_SOLICITADO_ASDEV) as DIA_SOLICITADO "
	    	    		 					+ "from asi_vacacion vac "
	    	    		 					+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    	    		 					+ "where vac.ide_asvac in("+ide_asvac+") "
	    	    		 					+ "group by vac.ide_gtemp");


	    		 			    	   if (tab_numero_tomados.getValor("DIA_SOLICITADO")==null || tab_numero_tomados.getValor("DIA_SOLICITADO").isEmpty())
	    		 			    	   {
	    		 			    		   numeroDiasTomadoss=0.0;
	    		 	      		       }else {
	    		 	      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_SOLICITADO"));	
	    		 	      		       
	    		 	      		       }
	    		 	         		   return  numeroDiasTomadoss;

	    		 		               }
	    		 		      return  0;
	    		 		        
	    		 		    }
	    		 		    
	    		 		   
	    		 		 		   
	    		 		   
	    		 		   

	    		 		  /*
	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	    		 		   */
	    		 		  	   public double getNumeroDiasTomados(String IDE_GEEDP, String ide_asvac) {     
	    		 		  		   double numeroDiasTomadoss=0.0;
	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 		  	        if (!tab_periodo.isEmpty()) {
	    		 		  	 		TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
	    		 							+ "from asi_vacacion vac "
	    		 							+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    		 							+ "where vac.ide_asvac in("+ide_asvac+") and dev.fin_semana_asdev=false "
	    		 							+ "group by vac.ide_gtemp");

	    		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
	    		 		  		    	   {
	    		 		  		    		   numeroDiasTomadoss=0.0;
	    		 		        		       }else {
	    		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
	    		 		        		       
	    		 		        		       }
	    		 		           		   return  numeroDiasTomadoss;

	    		 		  	               }
	    		 		  	      return  0;
	    		 		  	        
	    		 		  	    }
	    		 		  	          

	    		 		  	   
	 	    		 		  /*
	 	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	 	    		 		   */
	 	    		 		  	   public double getNumeroDiasTomadosFinSemana(String IDE_GEEDP,String ide_asvac) {     
	 	    		 		  		   double numeroDiasTomadoss=0.0;
	 	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	 	    		 		  	        if (!tab_periodo.isEmpty()) {
	 	    		 		  			TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
	 	    		 							+ "from asi_vacacion vac "
	 	    		 							+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	 	    		 							+ "where vac.ide_asvac in("+ide_asvac+") and  dev.fin_semana_asdev=true "
	 	    		 							+ "group by vac.ide_gtemp");

	 	    		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
	 	    		 		  		    	   {
	 	    		 		  		    		   numeroDiasTomadoss=0.0;
	 	    		 		        		       }else {
	 	    		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
	 	    		 		        		       
	 	    		 		        		       }
	 	    		 		           		   return  numeroDiasTomadoss;

	 	    		 		  	               }
	 	    		 		  	      return  0;
	 	    		 		  	        
	 	    		 		  	    }
	    		 		  	   
	 	    		 		  	   

	 //Metodo Descuento Fines de Semana
	 	    		 		  	   
	 	 	    		 		  /*
	 	 	    		 		   * Metodo devuelve los dias solicitados con cargo a vacacion de cada empleado	        
	 	 	    		 		   */
	 	 	    		 		  	   public double getNumeroFinSemanaTomado(String IDE_GEEDP) {     
	 	 	    		 		  		   double numeroDiasTomadoss=0.0;
	 	 	    		 		  		   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	 	 	    		 		  	        if (!tab_periodo.isEmpty()) {
	 	 	    		 		  		    	   TablaGenerica  tab_numero_tomados=utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO "
	 	 	    		 		  	        		+ "FROM ( "
	 	 	    		 		  	        		+ "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
	 	 	    		 		  	        		+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO "
	 	 	    		 		  	        		+ "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=true and es_fin_semana_asdev=true GROUP BY IDE_ASVAC "
	    		 		  	        	    + " ) a where IDE_ASVAC="+ tab_periodo.getValor("IDE_ASVAC"));

	    		 		  		    	   if (tab_numero_tomados.getValor("DIA_DESCONTADO")==null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty())
	    		 		  		    	   {
	    		 		  		    		   numeroDiasTomadoss=0.0;
	    		 		        		       }else {
	    		 		        		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));	
	    		 		        		       
	    		 		        		       }
	    		 		           		   return  numeroDiasTomadoss;

	    		 		  	               }
	    		 		  	      return  0;
	    		 		  	        
	    		 		  	    }
	    		 		  	          

	    		 		  	  /**
	    		 		  	   * Metodo devuelve el numero de dias para el nuevo calculo de dias tomados a vacacion 
	    		 		  	   * @param IDE_GEEDP
	    		 		  	   * @return
	    		 		  	   */
	    		 		  		  public double getNumeroDiasAjusteEmpleado(String IDE_GEEDP,String ide_asvac) {
	    		 		  	          double valorNumeroDiasAjusteEmpleado;
	    		 		  			    TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " ");        
	    		 		  		        if (!tab_periodo.isEmpty()) {
	    		 		  		        	TablaGenerica tabvalorNumeroDiasAjusteEmpleado = utilitario.consultar("select ide_gtemp,sum(nro_dias_ajuste_asvac) as nro_dias_ajuste_asvac " + " "
	    	    		 							+ "from asi_vacacion "
	    	    		 							+ "where IDE_ASVAC in("+ide_asvac+") "
	    	    		 							+ "group by ide_gtemp");
	    	    		 							//+ "" + tab_periodo.getValor("IDE_ASVAC"));
	    		 		  		       	if (tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac")==null || tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac").isEmpty())
	    		 		  		       	{
	    		 		  		       		valorNumeroDiasAjusteEmpleado=0.0;
	    		 		  	  		}else {
	    		 		  	  			valorNumeroDiasAjusteEmpleado=Double.parseDouble(tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac"));
	    		 		  	  		}
	    		 		  		        return valorNumeroDiasAjusteEmpleado;	
	    		 		  		        }

	    		 		  		        return 0;
	    		 		  	  		
	    		 		  		  }
	    		 		  	
	    		 		  		  
	    		 		  		  
	    		 		  		  
	    		 		  		  
	    		 		  		  /**
	    		 		  		   * Metodo devuelve el numero de dias de ajuste para cuando se pasa de 60
	    		 		  		   * @param IDE_GEEDP
	    		 		  		   * @return
	    		 		  		   */
	    		 		  			  public double nroDiasAjustePeriodo(String IDE_GEEDP, String ide_asvac) {
	    		 		  		       
	    		 		  			   double numeroDiasTomadoss=0.0;
	    		 					   TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " ");        
	    		 				        if (!tab_periodo.isEmpty()) {
	    		 				   		TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.dia_acumulado_asdev) as DIA_ACUMULADO "
	    	    		 		  				+ "from asi_vacacion vac "
	    	    		 		  				+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
	    	    		 		  				+ "where vac.ide_asvac in("+ide_asvac+") "
	    	    		 		  				+ "group by vac.ide_gtemp");


	    		 					    	   if (tab_numero_tomados.getValor("DIA_ACUMULADO")==null || tab_numero_tomados.getValor("DIA_ACUMULADO").isEmpty())
	    		 					    	   {
	    		 					    		   numeroDiasTomadoss=0.0;
	    		 			      		       }else {
	    		 			      		    	 numeroDiasTomadoss=Double.parseDouble(tab_numero_tomados.getValor("DIA_ACUMULADO"));	
	    		 						  //     System.out.println("cambio de valores"+numeroDiasTomadoss);
	    		 			      		       
	    		 			      		       }
	    		 			         		   return  numeroDiasTomadoss;

	    		 				               }
	    		 				      return  0;
	    		 		  				  
	    		 		  			  }
  

	    		 		  			
	    		 		  			//DIAS TOMADOS POR VACACION
	    		 		  					
	    		 		  					public static List<Double> getVacacionesTomadasXPeriodo(Double numeroDiasTomados, List<Integer> peridos, Double numeroDiasVacacionXAnio) {
	    		 		  						List<Double> vaxacionXPeriodo = new ArrayList<Double>();
	    		 		  						double ndeAux = numeroDiasTomados;

	    		 		  						for (Integer periodo : peridos) {
	    		 		  							if (ndeAux >= numeroDiasVacacionXAnio){
	    		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
	    		 		  								vaxacionXPeriodo.add(numeroDiasVacacionXAnio);	
	    		 		  							}else{
	    		 		  								if (ndeAux >= 0){
	    		 		  								vaxacionXPeriodo.add(ndeAux);
	    		 		  								ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
	    		 		  								}else{
	    		 		  									vaxacionXPeriodo.add(0.0);
	    		 		  								}
	    		 		  							}
	    		 		  						}
	    		 		  						return vaxacionXPeriodo;
	    		 		  					}
	    		 		  					
	    		 		  					
	    		 		  					
	    		 		  					
	    		 		  					/**
	    		 		  					 * Metodo que crea la 		
	    		 		  					 * @param vacacionesPeriodo
	    		 		  					 * @param vacacionesTomadas
	    		 		  					 * @return
	    		 		  					 */
	    		 		  							
	    		 		  							public static List<Double[]> getVacacionesMatriz(List<Double> vacacionesPeriodo, List<Double> vacacionesTomadas) {
	    		 		  								List<Double[]> matriz = new ArrayList<Double[]>();
	    		 		  								
	    		 		  								Integer dimension = vacacionesPeriodo.size();
	    		 		  								for (Integer i = 0; i < dimension; i++) {
	    		 		  									Double[] obj = new Double[5];
	    		 		  									obj[0] = i.doubleValue() + 1; // periodo
	    		 		  									obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
	    		 		  									//obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
	    		 		  									obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x periodo
	    		 		  								//	obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
	    		 		  									obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias vacacion penientes x periodo
	    		 		  								//	obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
	    		 		  									obj[4] = vacacionesPeriodo.get(i)+ vacacionesPeriodo.get(i);
	    		 		  								//	obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
	    		 		  									matriz.add(obj);
	    		 		  								}
	    		 		  								return matriz;
	    		 		  							}
	    		 		  							
	    		 		  							/**
	    		 		  							 * 		
	    		 		  							 * @param fechaIngresoEmpleado
	    		 		  							 * @return
	    		 		  							 */

	    		 		  									public  Double getNumeroDiasPendientes(int nde, int nda, double numeroDiasTomados,double numeroDiasVacacionXAnio){

	    		 		  										List<Integer> peridos = getPeriodos(nda, nde);
	    		 		  										List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
	    		 		  										List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos, numeroDiasVacacionXAnio);

	    		 		  										List<Double[]> matriz = getVacacionesMatriz(vacacionesPeriodo, vacacionesTomadas);
	    		 		  										Double diasPendientes = 0.0;
	    		 		  										for (Double[] vacaciones : matriz) {
	    		 		  											diasPendientes = diasPendientes + vacaciones[3];	
	    		 		  										
	    		 		  										}
	    		 		  										return diasPendientes;
}

													public Dialogo getDia_aplica_vacion() {
														return dia_aplica_vacion;
													}

													public void setDia_aplica_vacion(Dialogo dia_aplica_vacion) {
														this.dia_aplica_vacion = dia_aplica_vacion;
													}

													public List<Double[]> getMatriz() {
														return matriz;
													}

													public void setMatriz(List<Double[]> matriz) {
														this.matriz = matriz;
													}
	    		 		  									
		/**
	    * Metodo que devuelve el ide maximo de una tabla
		* @return String SQL Codigo maximo de los ide primarios de de las tablas
		*/

		public String servicioCodigoMaximo(String tabla,String ide_primario){
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
		}
	    		 		  									
	
	/*
	 * Limpio los coponentes para la tabla de de resumen de vacaciones	
	 */
		public void setear(){
		  gri_resumen_vacaciones_periodo.getChildren().clear();
		  gri_anular_dias_solicitados.getChildren().clear();
		  utilitario.addUpdate("dia_aplica_vacion");
		  utilitario.addUpdate("tab_tabla_vacacion,tab_tabla_detalle_vacaccion");

		  dia_aplica_vacion.cerrar();
	  }
	  
	  

	    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
	        double parteEntera, resultado;
	        resultado = valorInicial;
	        parteEntera = Math.floor(resultado);
	        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
	        resultado=Math.round(resultado);
	        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
	        return resultado;
	    }
	    
		public Tabla getTab_tabla_vacacion() {
			return tab_tabla_vacacion;
		}

		public void setTab_tabla_vacacion(Tabla tab_tabla_vacacion) {
			this.tab_tabla_vacacion = tab_tabla_vacacion;
		}

		public Tabla getTab_tabla_detalle_vacaccion() {
			return tab_tabla_detalle_vacaccion;
		}

		public void setTab_tabla_detalle_vacaccion(Tabla tab_tabla_detalle_vacaccion) {
			this.tab_tabla_detalle_vacaccion = tab_tabla_detalle_vacaccion;
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

		public Tabla getTab_asi_vacacion_resumen_empleado() {
			return tab_asi_vacacion_resumen_empleado;
		}

		public void setTab_asi_vacacion_resumen_empleado(
				Tabla tab_asi_vacacion_resumen_empleado) {
			this.tab_asi_vacacion_resumen_empleado = tab_asi_vacacion_resumen_empleado;
		}


	    
		public double getDatosEmpleado1(String IDE_GTEMP,boolean reporte,String ide_asvac,String fecha_ingreso_continuidad,String fecha_fin_continuidad,String IDE_GEEDP,Double numeroDiasTomados, int periodo,String ide_asvac_origen){
			 bandEntrada=0;
			    bandSalida=0;
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
				TablaGenerica tab_asi_vacacion_resumen_empleado=new TablaGenerica();
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
				guardarPantalla();
                return valor_retorno;
		}

	    public TablaGenerica retornaTablaAccionResumenVacacion(String fec_ini,String fec_fin,String ide_gtemp){
	    	TablaGenerica tab_acciones=utilitario.consultar(" select ide_geedp,rmu_geedp,fecha_geedp,fecha_liquidacion_geedp,activo_geedp FROM gen_empleados_departamento_par where "
	    			+ "fecha_geedp between '"+fec_ini+"' and '"+fec_fin+"' and ide_gtemp="+ide_gtemp+" order by ide_geedp asc");
	    	return tab_acciones;
	    }


		public boolean isBanderaExcedeDias() {
			return banderaExcedeDias;
		}


		public void setBanderaExcedeDias(boolean banderaExcedeDias) {
			this.banderaExcedeDias = banderaExcedeDias;
		} 
	    		 		  									
}		
