package paq_general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_general.ejb.ServicioGeneral;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

public class pre_contratacion_partida_presupuestaria extends Pantalla{

	private Tabla tab_poa=new Tabla();
	private Tabla tab_grupo_ocupacional=new Tabla();
	private Tabla tab_mes_codificado=new Tabla();
	private Tabla tab_reforma=new Tabla();
	private Tabla tab_archivo=new Tabla();
	private Tabla tab_financiamiento=new Tabla();
	private Tabla tab_funcion_programa=new Tabla();
	private Tabla tab_financiamiento_reforma=new Tabla();
	private Tabla tab_financiamiento_ejecucion=new Tabla();
	private Tabla tab_mes=new Tabla();
	private Tabla tab_empleados = new Tabla();
	private SeleccionTabla set_reforma_fuente = new SeleccionTabla();

	private Combo com_anio=new Combo();

	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private SeleccionTabla set_clasificador_actua=new SeleccionTabla();
	private SeleccionTabla set_partida_cargo_funcional=new SeleccionTabla();

	private SeleccionTabla set_funcion=new SeleccionTabla();
	private SeleccionTabla set_actualizarfuncion=new SeleccionTabla();
	private SeleccionTabla set_sub_actividad=new SeleccionTabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_saldo_fuente=new SeleccionTabla();
	
	private Confirmar con_guardar=new Confirmar();
	private Arbol arb_arbol = new Arbol();
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private SeleccionTabla sel_poa= new SeleccionTabla();
	private SeleccionTabla sel_resolucion= new SeleccionTabla();
	private Combo com_fuente_financiamiento=new Combo();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral) utilitario.instanciarEJB(ServicioGeneral.class);


	public pre_contratacion_partida_presupuestaria(){
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		com_anio.setMetodo("filtrarAnio");

		bar_botones.agregarComponente(com_anio);
		
		
		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("mostrarSetReformaFuente");
		bar_botones.agregarBoton(bot_buscar); 
		
		

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_poa.setId("tab_poa");   
		tab_poa.setHeader("DISTRIBUTIVO");
		tab_poa.setTabla("gen_partida_grupo_cargo","ide_gepgc",2);
	//	tab_poa.getColumna("ide_geani").setVisible(false);
	//	tab_poa.setCondicion("ide_geani=-1");  
		
		
		tab_poa.getColumna("IDE_GEGRO").setUnico(true);
		tab_poa.getColumna("IDE_GECAF").setUnico(true);
		tab_poa.getColumna("IDE_SUCU").setUnico(true);
		tab_poa.getColumna("IDE_GEARE").setUnico(true);
		tab_poa.getColumna("IDE_GEDEP").setUnico(true);
		tab_poa.getColumna("IDE_GEPAP").setUnico(true);
		tab_poa.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL",	"IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_poa.getColumna("IDE_GEGRO").setMetodoChange("grupoOcupacional");
		tab_poa.getColumna("IDE_GEGRO").setAutoCompletar();
		tab_poa.getColumna("IDE_GECAF").setCombo("select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a, GEN_GRUPO_CARGO b where a.IDE_GECAF = b.IDE_GECAF");
		tab_poa.getColumna("IDE_GECAF").setAutoCompletar();
		tab_poa.setMostrarcampoSucursal(true);
		tab_poa.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU","NOM_SUCU", "");
		/*tab_poa.getColumna("IDE_SUCU").setCombo("select gds.ide_sucu,sucu.nom_sucu,gds.ide_gedep,gds.ide_geare  "
				+ "from  "
				+ "gen_departamento_sucursal  gds "
				+ "left join sis_sucursal sucu on sucu.ide_sucu=gds.ide_sucu "
				+ "where ide_geare=-1 and ide_gedep=-1");*/
		tab_poa.getColumna("IDE_SUCU").setBuscarenCombo(false);
		tab_poa.getColumna("IDE_SUCU").setNombreVisual("SUCURSAL");
		//tab_poa.getColumna("IDE_SUCU").setMetodoChange("cargarArea");
		tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE");
		tab_poa.getColumna("IDE_GEARE").setLectura(true);
	//	tab_poa.getColumna("IDE_GEARE").setMetodoChange("cargarDepartamentos");
	//	tab_poa.getColumna("IDE_GEARE").setBuscarenCombo(false);
		tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT DISTINCT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP");
		tab_poa.getColumna("IDE_GEDEP").setBuscarenCombo(false);
		tab_poa.getColumna("IDE_GEDEP").setLectura(true);
		tab_poa.getColumna("IDE_GEPAP").setCombo("GEN_PARTIDA_PRESUPUESTARIA", "IDE_GEPAP","CODIGO_PARTIDA_GEPAP,DETALLE_GEPAP", "ACTIVO_GEPAP=TRUE");
		tab_poa.getColumna("IDE_GEPAP").setAutoCompletar();
		tab_poa.getColumna("IDE_GEPAP").setLectura(true);
		tab_poa.setMostrarcampoSucursal(true);
		tab_poa.getColumna("ACTIVO_GEPGC").setCheck();
		tab_poa.getColumna("ACTIVO_GEPGC").setValorDefecto("true");
		tab_poa.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO","IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_poa.getColumna("IDE_GTTEM").setRequerida(true);
		tab_poa.getColumna("IDE_GEPAP").setRequerida(true);
		tab_poa.getColumna("VACANTE_GEPGC").setCheck();
		tab_poa.getColumna("VACANTE_GEPGC").setValorDefecto("true");
		//		tab_tabla.getColumna("VACANTE_GEPGC").setLectura(true);
		tab_poa.getColumna("FECHA_ACTIVACION_GEPGC").setLectura(true);		
		tab_poa.getColumna("FECHA_DESACTIVA_GEPGC").setLectura(true);
		tab_poa.getColumna("MOTIVO_GEPGC").setLectura(true);
		tab_poa.getColumna("ACTIVO_GEPGC").setLectura(true);
		tab_poa.getColumna("ENCARGO_GEPGC").setCheck();
		tab_poa.getColumna("SALARIO_ENCARGO_GEPGC").setLectura(true);
		tab_poa.getColumna("ENCARGO_GEPGC").setMetodoChange("salarioEncargo");
		tab_poa.getColumna("IDE_GEPGC").setNombreVisual("CODIGO");
		tab_poa.getColumna("IDE_GTTEM").setNombreVisual("REGIMEN LABORAL");
		tab_poa.getColumna("IDE_SUCU").setNombreVisual("LUGAR TRABAJO");
		tab_poa.getColumna("IDE_SUCU").setOrden(3);
		tab_poa.getColumna("IDE_GEGRO").setNombreVisual("GRUPO OCUPACIONAL");
		tab_poa.getColumna("IDE_GEGRO").setOrden(4);
		tab_poa.getColumna("IDE_GEPAP").setNombreVisual("PARTIDA PRESUPUESTARIA");
		tab_poa.getColumna("IDE_GEPAP").setOrden(7);
		tab_poa.getColumna("IDE_GEARE").setNombreVisual("PROCESO");
		tab_poa.getColumna("IDE_GEARE").setOrden(5);
		tab_poa.getColumna("IDE_GEDEP").setNombreVisual("SUB_PROCESO");
		tab_poa.getColumna("IDE_GEDEP").setOrden(6);
		tab_poa.getColumna("TITULO_CARGO_GEPGC").setNombreVisual("DENOMINACION PUESTO");
		tab_poa.getColumna("TITULO_CARGO_GEPGC").setOrden(2);
		tab_poa.getColumna("FECHA_ACTIVACION_GEPGC").setNombreVisual("FECHA ACTIVACIÓN");
		tab_poa.getColumna("FECHA_DESACTIVA_GEPGC ").setNombreVisual("FECHA DESACTIVA");
		tab_poa.getColumna("MOTIVO_GEPGC").setNombreVisual("MOTIVO ACTIVACIÓN");
		tab_poa.getColumna("ACTIVO_GEPGC").setNombreVisual("ACTIVO");
		tab_poa.getColumna("VACANTE_GEPGC").setNombreVisual("VACANTE");
		tab_poa.getColumna("IDE_GEESO").setVisible(false);

		tab_poa.setCampoOrden("ide_gepgc desc");
		tab_poa.setCondicion("IDE_GEPGC=-1");
		tab_poa.setTipoFormulario(true);
		tab_poa.getGrid().setColumns(4);
		
		//tab_poa.agregarRelacion(tab_mes);//agraga relacion para los tabuladores
		//tab_poa.agregarRelacion(tab_mes_codificado);//agraga relacion para los tabuladores
		//tab_poa.agregarRelacion(tab_financiamiento);
		//tab_poa.agregarRelacion(tab_reforma);
		//tab_poa.agregarRelacion(tab_financiamiento_reforma);
		//tab_poa.agregarRelacion(tab_financiamiento_ejecucion);

		tab_poa.dibujar();
		PanelTabla pat_poa=new PanelTabla();
		pat_poa.setPanelTabla(tab_poa);
		
		
		tab_grupo_ocupacional.setId("tab_grupo_ocupacional");
		tab_grupo_ocupacional.setHeader("GRUPO OCUPACIONAL / CARGO FUNCIONAL");
		tab_grupo_ocupacional.setTabla("gen_estructura_grupo_cargo", "ide_geegc", 3);
		tab_grupo_ocupacional.getColumna("activo_geegc").setCheck();
		tab_grupo_ocupacional.getColumna("activo_geegc").setValorDefecto("true");
		tab_grupo_ocupacional.getColumna("ide_gegro").setCombo("GEN_GRUPO_OCUPACIONAL","IDE_GEGRO" , "DETALLE_GEGRO", "");
		//tab_grupo_ocupacional.getColumna("ide_gegro").setMetodoChange("getCargoFuncional");
		
		//tab_grupo_ocupacional.getColumna("ide_gecaf").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF" , "DETALLE_GECAF", "");
		
		tab_grupo_ocupacional.getColumna("ide_gecaf").setCombo("select GGC.IDE_GECAF,GGC.DETALLE_GECAF from GEN_CARGO_FUNCIONAL GGC "
	//			+ "LEFT JOIN GEN_CARGO_FUNCIONAL GCF ON GCF.IDE_GECAF=GGC.IDE_GEGRO "
				+ "WHERE GGC.DETALLE_GECAF IS NOT NULL");
		
		//tab_grupo_ocupacional.getColumna("DETALLE_GEGRO").setRequerida(true);
		//tab_grupo_ocupacional.getColumna("SIGLAS_GEGRO").setVisible(false);
		//tab_grupo_ocupacional.onSelect("seleccionarTabla1");
		//tab_grupo_ocupacional.getColumna("IDE_GTNIV").setCombo("GTH_NIVEL_VIATICO","IDE_GTNIV" , "DETALLE_GTNIV", "");
		//tab_grupo_ocupacional.getColumna("IDE_GTNIV").setVisible(false);
		//tab_grupo_ocupacional.getColumna("subrogacion_gegro").setVisible(false);
		//tab_grupo_ocupacional.getColumna("rmu_gegro").setVisible(false);

		
		 
		tab_grupo_ocupacional.dibujar();
		PanelTabla pat_grupo_ocupacional = new PanelTabla();
		pat_grupo_ocupacional.setPanelTabla(tab_grupo_ocupacional);

		
		
		
		tab_empleados.setId("tab_empleados");
		tab_empleados.setSql("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP  "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "where epar.ide_gepgc in(-1) "
				+ "and epar.activo_geedp=true");
		tab_empleados.setCampoPrimaria("IDE_GEEDP");
		tab_empleados.setNumeroTabla(1);
		tab_empleados.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("CEDULA");
		tab_empleados.getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");
		
		tab_empleados.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		tab_empleados.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		tab_empleados.setLectura(true);
		tab_empleados.setRows(10);
		tab_empleados.dibujar();
		PanelTabla pat_panel_empleado = new PanelTabla(); 
		pat_panel_empleado.getMenuTabla().getItem_formato().setDisabled(true);
		pat_panel_empleado.setPanelTabla(tab_empleados);

		
		
		
		
		
		
		
		
		//tab_poa.getColumna("objeto_programa_prpoa").setVisible(false);
		//tab_poa.getColumna("objetivo_proyecto_prpoa").setVisible(false);
		//tab_poa.getColumna("meta_proyecto_prpoa").setVisible(false);
		//tab_poa.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		///tab_poa.getColumna("ide_coest").setVisible(false);
		//tab_poa.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		//tab_poa.getColumna("ide_prcla").setAutoCompletar();
		//tab_poa.getColumna("ide_prcla").setLectura(true);
		//tab_poa.getColumna("ide_prfup").setVisible(false);
		//tab_poa.getColumna("ide_prcla").setAncho(25);
		//tab_poa.getColumna("activo_prpoa").setLectura(true);
		//tab_poa.getColumna("activo_prpoa").setValorDefecto("false");
		//tab_poa.getColumna("ejecutado_presupuesto_prpoa").setLectura(true);
		//tab_poa.getColumna("ejecutado_presupuesto_prpoa").setValorDefecto("false");

		//tab_poa.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
		//tab_poa.getColumna("presupuesto_inicial_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		//tab_poa.getColumna("presupuesto_inicial_prpoa").setValorDefecto("0.00");
		//tab_poa.getColumna("presupuesto_inicial_prpoa").setEtiqueta();
		//tab_poa.getColumna("reforma_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		//tab_poa.getColumna("reforma_prpoa").setValorDefecto("0.00");
		//tab_poa.getColumna("reforma_prpoa").setEtiqueta();
		//tab_poa.getColumna("presupuesto_codificado_prpoa").setEtiqueta();
		//tab_poa.getColumna("presupuesto_codificado_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		//tab_poa.getColumna("presupuesto_codificado_prpoa").setValorDefecto("0.00");
		//tab_poa.getColumna("valor_certificado_prpoa").setEtiqueta();
		//tab_poa.getColumna("valor_certificado_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:green");
		//tab_poa.getColumna("valor_certificado_prpoa").setValorDefecto("0.00");
		//tab_poa.getColumna("valor_compromiso_prpoa").setEtiqueta();
		//tab_poa.getColumna("valor_compromiso_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:green");
		//tab_poa.getColumna("valor_compromiso_prpoa").setValorDefecto("0.00");
		//tab_poa.getColumna("valor_devengado_prpoa").setEtiqueta();
		//tab_poa.getColumna("valor_devengado_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:green");
		//tab_poa.getColumna("valor_devengado_prpoa").setValorDefecto("0.00");


		
		
				
		//EJECUCION MENSUAL
		/*tab_mes.setId("tab_mes");
		tab_mes.setHeader("EJECUCION MENSUAL (POA)");
		tab_mes.setIdCompleto("tab_tabulador:tab_mes");
		tab_mes.setTabla("pre_poa_mes","ide_prpom",3);
		tab_mes.setCampoForanea("ide_prpoa");
		tab_mes.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_mes.getColumna("activo_prpom").setValorDefecto("true");
		tab_mes.getColumna("activo_prpom").setLectura(true);
		tab_mes.getColumna("valor_presupuesto_prpom").setMetodoChange("actualizarEjecucionMensual");		
		tab_mes.setColumnaSuma("valor_presupuesto_prpom");
		//tab_mes.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_mes);*/

		//EJECUCION MENSUAL CODIFICADO
		tab_mes_codificado.setId("tab_mes_codificado");
		tab_mes_codificado.setHeader("EJECUCION MENSUAL CODIFICADO (POA)");
		tab_mes_codificado.setIdCompleto("tab_tabulador:tab_mes_codificado");
		tab_mes_codificado.setTabla("pre_poa_mes_codificado","ide_prpmc",10);
		tab_mes_codificado.setCampoForanea("ide_prpoa");
		tab_mes_codificado.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_mes_codificado.getColumna("activo_prpmc").setValorDefecto("true");
		tab_mes_codificado.getColumna("activo_prpmc").setLectura(true);
		tab_mes_codificado.getColumna("valor_presupuesto_prpmc").setMetodoChange("actualizarEjecucionMensualCodificado");		
		tab_mes_codificado.setColumnaSuma("valor_presupuesto_prpmc");
		//tab_mes_codificado.dibujar();
		PanelTabla pat_panel2c = new PanelTabla();
		pat_panel2c.setPanelTabla(tab_mes_codificado);
		
		
		// REFORMA MENSUAL
		tab_reforma.setId("tab_reforma");
		tab_reforma.setHeader("REFORMA MENSUAL (POA)");
		tab_reforma.setIdCompleto("tab_tabulador:tab_reforma");
		tab_reforma.setTabla("pre_poa_reforma", "ide_prpor",4);
		tab_reforma.setCampoForanea("ide_prpoa");
		tab_reforma.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_reforma.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_reforma.getColumna("valor_reformado_prpor").setMetodoChange("valorReforma");
		tab_reforma.getColumna("SALDO_ACTUAL_PRPOR").setLectura(true);
		tab_reforma.getColumna("ide_coest").setVisible(false);
		tab_reforma.getColumna("pre_ide_prpoa").setVisible(false);
		tab_reforma.getColumna("ide_gemes").setVisible(false);
		tab_reforma.getColumna("activo_prpor").setValorDefecto("true");
		tab_reforma.getColumna("activo_prpor").setLectura(true);
		tab_reforma.getColumna("fecha_prpor").setValorDefecto(utilitario.getFechaActual());
		//tab_reforma.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);

		//FINANCIAMIENTO
		tab_financiamiento.setId("tab_financiamiento");
		tab_financiamiento.setHeader("FUENTES DE FINANCIAMIENTO (POA)");
		tab_financiamiento.setIdCompleto("tab_tabulador:tab_financiamiento");
		tab_financiamiento.setTabla("pre_poa_financiamiento","ide_prpof",5);
		tab_financiamiento.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_financiamiento.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		//tab_financiamiento.getColumna("valor_financiamiento_prpof").setMetodoChange("valorFinanciamiento");
		tab_financiamiento.getColumna("activo_prpof").setValorDefecto("true");
		tab_financiamiento.getColumna("activo_prpof").setLectura(true);
		tab_financiamiento.getColumna("ide_coest").setVisible(false);
		tab_financiamiento.setColumnaSuma("valor_financiamiento_prpof");
		//tab_financiamiento.dibujar();
		PanelTabla pat_panel4= new PanelTabla();
		pat_panel4.setPanelTabla(tab_financiamiento);

		//FINANCIAMIENTO
		tab_financiamiento_reforma.setId("tab_financiamiento_reforma"); 
		tab_financiamiento_reforma.setHeader("REFORMA FUENTES DE FINANCIAMIENTO (POA)");
		tab_financiamiento_reforma.setIdCompleto("tab_tabulador:tab_financiamiento_reforma");
		tab_financiamiento_reforma.setTabla("pre_poa_reforma_fuente","ide_prprf",7);
		tab_financiamiento_reforma.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_financiamiento_reforma.setColumnaSuma("valor_reformado_prprf");
		//tab_financiamiento_reforma.dibujar();
		PanelTabla pat_panel7= new PanelTabla();
		pat_panel7.setPanelTabla(tab_financiamiento_reforma);
		
		//FINANCIAMIENTO EJECUCION
		tab_financiamiento_ejecucion.setId("tab_financiamiento_ejecucion");
		tab_financiamiento_ejecucion.setHeader("EJECUCION FUENTE DE FINANCIAMIENTO");
		tab_financiamiento_ejecucion.setIdCompleto("tab_tabulador:tab_financiamiento_ejecucion");
		tab_financiamiento_ejecucion.setTabla("pre_poa_fuente_ejecucion", "ide_prpfe", 8);
		tab_financiamiento_ejecucion.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_financiamiento_ejecucion.getColumna("valor_certificado_prpfe").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_financiamiento_ejecucion.getColumna("valor_certificado_prpfe").setValorDefecto("0.00");
		tab_financiamiento_ejecucion.getColumna("valor_certificado_prpfe").setEtiqueta();
		tab_financiamiento_ejecucion.getColumna("valor_compromiso_prpfe").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_financiamiento_ejecucion.getColumna("valor_compromiso_prpfe").setValorDefecto("0.00");
		tab_financiamiento_ejecucion.getColumna("valor_compromiso_prpfe").setEtiqueta();
		tab_financiamiento_ejecucion.getColumna("valor_devengado_prpfe").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_financiamiento_ejecucion.getColumna("valor_devengado_prpfe").setValorDefecto("0.00");
		tab_financiamiento_ejecucion.getColumna("valor_devengado_prpfe").setEtiqueta();
		tab_financiamiento_ejecucion.getColumna("ide_prfuf").setLectura(true);
		tab_financiamiento_ejecucion.getColumna("activo_prpfe").setLectura(true);
		tab_financiamiento_ejecucion.setColumnaSuma("valor_certificado_prpfe,valor_compromiso_prpfe,valor_devengado_prpfe");
		//tab_financiamiento_ejecucion.dibujar();
		PanelTabla pat_panel8=new PanelTabla();
		pat_panel8.setPanelTabla(tab_financiamiento_ejecucion);
		
		//ARCHIVO
		tab_archivo.setId("tab_archivo");
		tab_archivo.setHeader("ARCHIVOS ANEXOS (POA)");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		tab_archivo.setTipoFormulario(true);
		tab_archivo.setTabla("pre_archivo","ide_prarc",6);
		tab_archivo.getColumna("foto_prarc").setUpload("presupuesto");
		tab_archivo.setCampoForanea("ide_prpoa");
		tab_archivo.getColumna("ide_prpac").setVisible(false);
		tab_archivo.getColumna("ide_prcon").setVisible(false);
		tab_archivo.getColumna("ide_prcop").setVisible(false);
		tab_archivo.getColumna("ide_prtra").setVisible(false);
		tab_archivo.getColumna("activo_prarc").setValorDefecto("true");
		//tab_archivo.dibujar();
		PanelTabla pat_panel5= new PanelTabla();
		pat_panel5.setPanelTabla(tab_archivo);
		Imagen fondo= new Imagen();   

		fondo.setStyle("text-aling:center;position:absolute;top:100px;left:490px;");
		fondo.setValue("imagenes/logo.png");
		pat_panel5.setWidth("100%");
		pat_panel5.getChildren().add(fondo);

		tab_tabulador.agregarTab("GRUPO OCUPACIONAL / CARGO FUNCIONAL", pat_grupo_ocupacional);//intancia los tabuladores 
		tab_tabulador.agregarTab("EJECUCION MENSUAL CODIFICADO", pat_panel2c);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA MENSUAL",pat_panel3);
		tab_tabulador.agregarTab("FINANCIAMIENTO", pat_panel4);
		tab_tabulador.agregarTab("REFORMA FINANCIAMIENTO", pat_panel7);
		tab_tabulador.agregarTab("EJECUCION FINANCIAMIENTO", pat_panel8);
		tab_tabulador.agregarTab("ARCHIVOS",pat_panel5);

		// factor_competencia
		arb_arbol.setId("arb_arbol");
		arb_arbol.setArbol("gen_estructura_organizacional", "ide_geeso", "detalle_geeso", "pre_ide_geeso");
		//arb_arbol.setArbol("CMP_FACTOR_COMPETENCIA", "IDE_CMFAC", "DETALLE_CMFAC", "CMP_IDE_CMFAC");
		arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where ide_geeso != null and ide_geani=-1 )"); //Carga vacio
		arb_arbol.onSelect("seleccionar_arbol");	
		arb_arbol.dibujar();


		Division div1 = new Division(); //UNE OPCION Y DIV 2
		div1.dividir2(pat_grupo_ocupacional,pat_panel_empleado, "50%", "V");
		
		
		Division div3 = new Division(); //UNE OPCION Y DIV 2
		div3.dividir2(pat_poa, div1, "50%", "H");
		
		
		Division div_division = new Division();
		div_division.setId("div_division");
		//div_division.dividir1(tab_poa);
		div_division.dividir2(arb_arbol, div3, "30%", "V");  //arbol y div3
		agregarComponente(div_division);



		Boton bot_agregar=new Boton();
		bot_agregar.setValue("Agregar Partida Presupuestaria");
		bot_agregar.setMetodo("agregarClasificador");
		bar_botones.agregarBoton(bot_agregar);
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setValue("Actualizar Partida Presupuestaria");
		bot_actualizar.setMetodo("actualizadorClasificador");
		bar_botones.agregarBoton(bot_actualizar);
		
		Boton bot_aprobar_poa = new Boton();
		bot_aprobar_poa.setValue("Transferir (POA) Presupuesto");
		bot_aprobar_poa.setMetodo("aprobarPoa");
		//bar_botones.agregarBoton(bot_aprobar_poa);
		
		Boton bot_saldo_fuente = new Boton();
		bot_saldo_fuente.setValue("Consultar Saldos Fuente Financiamiento");
		bot_saldo_fuente.setMetodo("saldoFuente");
		bar_botones.agregarBoton(bot_saldo_fuente);
		
		
		Boton bot_asignacion = new Boton();
		bot_asignacion.setValue("Asignar Partida Grupo Cargo");
		bot_asignacion.setMetodo("asignarPartidaGrupoCargo");
		bar_botones.agregarBoton(bot_asignacion);
		
		
		set_clasificador.setId("set_clasificador");
		set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_clasificador.setRadio(); //solo selecciona una opcion
		set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla"); 
		set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido(); //pone filtro
		set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();//pone filtro
		set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
		agregarComponente(set_clasificador);


		set_clasificador_actua.setId("set_clasificador_actua");
		set_clasificador_actua.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_clasificador_actua.setRadio(); //solo selecciona una opcion
		set_clasificador_actua.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla"); 
		set_clasificador_actua.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido(); //pone filtro
		set_clasificador_actua.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();//pone filtro
		set_clasificador_actua.getBot_aceptar().setMetodo("aceptarActualizaClasificador");
		agregarComponente(set_clasificador_actua);
		
		set_partida_cargo_funcional.setId("set_partida_cargo_funcional");
		set_partida_cargo_funcional.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_partida_cargo_funcional.setRadio(); //solo selecciona una opcion
		set_partida_cargo_funcional.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla"); 
		//set_partida_cargo_funcional.setSeleccionTabla(ser_general.getGenPartidaGrupoCargo("true"),"ide_gepgc"); 
		
		//set_partida_cargo_funcional.getTab_seleccion().getColumna("CODIGO_PARTIDA_GEPAP").setFiltroContenido(); //pone filtro
		//set_partida_cargo_funcional.getTab_seleccion().getColumna("DETALLE_GEPAP").setFiltroContenido();//pone filtro
		//set_partida_cargo_funcional.getTab_seleccion().getColumna("TITULO_CARGO_GEPGC").setFiltroContenido();//pone filtro
		//set_partida_cargo_funcional.getBot_aceptar().setMetodo("aceptarActualizaEstructuraPartida");
		//agregarComponente(set_partida_cargo_funcional);
		
		inicializaSetPoaReformar();

	}
	
	
	public void inicializaSetPoaReformar(){
		set_reforma_fuente.setId("set_reforma_fuente");
		set_reforma_fuente.setTitle("Seleccione una Linea del POA para la Reforma");
		set_reforma_fuente.setSeleccionTabla(ser_presupuesto.getPoaSaldosFuenteFinanciamiento("-1","-1","0","1","-1"),"codigo");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setNombreVisual("Fuente Financiamiento");
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setNombreVisual("Asignación Inicial F.F.");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setNombreVisual("Partida Presupuestaria");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setNombreVisual("Codigo Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setNombreVisual("Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setNombreVisual("Nro. Resolución");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setNombreVisual("PROYECTO");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geare").setNombreVisual("AREA/COORDINACION");

		set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geare").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_programa").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_producto").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_actividad").setFiltroContenido();

		set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setNombreVisual("Codigo POA");
		//set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("ide_prfuf").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_reformado").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_saldo_fuente").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("fecha_inicio_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("fecha_fin_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_inicial_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_codificado_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("reforma_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geani").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setVisible(false);
		set_reforma_fuente.getTab_seleccion().setSortBy("ide_prpoa");
		
		//set_reforma_fuente.setRadio();
		set_reforma_fuente.getTab_seleccion().ejecutarSql();
		set_reforma_fuente.getBot_aceptar().setMetodo("aceptarPoa");

		agregarComponente(set_reforma_fuente);

	}
	public void iniciaFuente(){
		set_saldo_fuente.setId("set_saldo_fuente");
		set_saldo_fuente.setSeleccionTabla(ser_presupuesto.saldoFuenteFinanciamiento("-1"),"ide_prfuf");
		set_saldo_fuente.setTitle("SALDO FUENTES DE FINANCIAMIENTO");
		agregarComponente(set_saldo_fuente);
	}
	public void saldoFuente(){
		set_saldo_fuente.getTab_seleccion().setSql(ser_presupuesto.saldoFuenteFinanciamiento(com_anio.getValue().toString()));
		set_saldo_fuente.getTab_seleccion().ejecutarSql();
		set_saldo_fuente.dibujar();
	}
	public void iniciaPoa(){
		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","false"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
		set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
		set_poa.getBot_aceptar().setMetodo("aprobarPoa");
		agregarComponente(set_poa);
	}
	public void aprobarPoa(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(set_poa.isVisible()){
			
			String str_seleccionados=set_poa.getSeleccionados();
			if (str_seleccionados!=null){
				TablaGenerica tab_aprueba_poa =utilitario.consultar("select ide_prpoa,activo_prpoa from pre_poa where ide_prpoa in ("+str_seleccionados+")");
				if(tab_aprueba_poa.getTotalFilas()>0){
					
					for(int i=0;i<tab_aprueba_poa.getTotalFilas();i++){
						
						TablaGenerica tab_existe =utilitario.consultar("select 1 as ide, (case when b.ide_prfup is null and b.ide_prcla is null then 0 else 1 end) as codigo, "
								+ " (case when a.ide_prcla is null then 0 else 1 end) as codigo_pres from pre_poa a "
								+ "left join pre_programa b on a.ide_prfup = b.ide_prfup and a.ide_prcla=b.ide_prcla where ide_prpoa in ("+tab_aprueba_poa.getValor(i,"ide_prpoa")+");");
						
						if(tab_existe.getValor("codigo").equals("0"))
						{
							System.out.println("tab_existe.getValor "+tab_existe.getValor("codigo") + " ide_poa: "+tab_aprueba_poa.getValor(i,"ide_prpoa"));
							if(tab_existe.getValor("codigo_pres").equals("1"))
							{
							// Consulto codigo maximo de la cabecera de la tabla de pre_programa
							TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("pre_programa", "ide_prpro"));
							String maximo_cont_movimiento=tab_maximo.getValor("codigo");
							// Consulto codigo maximo de la cabecera de la tabla de pre_programa
							TablaGenerica tab_maximo_vigente =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("cont_vigente", "ide_covig"));
							String maximo_cont_vigente=tab_maximo_vigente.getValor("codigo");
							// Consulto codigo maximo  de la tabla de fuente financiameinto ejecucion
							TablaGenerica tab_maximo_ejecucion =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("pre_poa_fuente_ejecucion", "ide_prpfe"));
							String maximo_ejecucion=tab_maximo_ejecucion.getValor("codigo");
							
							ser_presupuesto.apruebaPoa(tab_aprueba_poa.getValor(i,"ide_prpoa"), maximo_cont_movimiento);
							if(ser_presupuesto.insertaVigente(maximo_cont_vigente, maximo_cont_movimiento, com_anio.getValue().toString()).equals(""))	
							{
								ser_presupuesto.insertaFuenteEjecucion(maximo_ejecucion, tab_aprueba_poa.getValor(i,"ide_prpoa"));
								utilitario.getConexion().ejecutarSql("update pre_poa set activo_prpoa=true where ide_prpoa in ("+tab_aprueba_poa.getValor(i,"ide_prpoa")+");");
							}
							else
								utilitario.agregarMensajeInfo("Problemas al transferir al presupuesto.", "");
						}
						else
								utilitario.agregarMensajeInfo("Problemas al transferir al presupuesto. La subActividad no cuenta con un Item PRESUPUESTADO", "");
						}
						else
							utilitario.getConexion().ejecutarSql("update pre_poa set activo_prpoa=true where ide_prpoa in ("+tab_aprueba_poa.getValor(i,"ide_prpoa")+");");
					}
					utilitario.agregarMensaje("Aprobado POA", "Se transfirió el POA para las ejecuciones presupuestarias.");

				}
				set_poa.cerrar();
		
			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			}
		}
		else{
			
			set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","false"));
			set_poa.getTab_seleccion().ejecutarSql();
			set_poa.dibujar();
		}
	}
	public void actualizarEjecucionMensual(AjaxBehaviorEvent evt) {
		tab_mes.modificar(evt); //Siempre es la primera linea
		tab_mes.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mes");
 
	}
	
	
	public void actualizarEjecucionMensualCodificado(AjaxBehaviorEvent evt) {
		tab_mes_codificado.modificar(evt); //Siempre es la primera linea
		tab_mes_codificado.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mes_codificado");
 
	}
	//valor Financiamiento
	// para subir vaslores de un tabla a otra 
	public void  calcularValor(){
		double dou_valor_finan=0;
		double dou_valor_codificado=0;
		double dou_valor_refor=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_refor=pckUtilidades.CConversion.CDbl_2(tab_reforma.getValor("valor_reformado_prpor"));
		} catch (Exception e) {
		}
		
		
		
		String valor_financiamiento=tab_financiamiento.getSumaColumna("valor_financiamiento_prpof")+"";
		dou_valor_finan=pckUtilidades.CConversion.CDbl_2(valor_financiamiento);
		dou_valor_codificado=dou_valor_finan+dou_valor_refor;

		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_poa.setValor("presupuesto_inicial_prpoa",utilitario.getFormatoNumero(valor_financiamiento,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "presupuesto_inicial_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorFinanciamiento(AjaxBehaviorEvent evt) {
		tab_financiamiento.modificar(evt); //Siempre es la primera linea
		calcularValor();

	}

	/////valor reforma
	// para subir vaslores de un tabla a otra Financiamiento 
	public void  calcularReforma(){
		double dou_valor_refor=0;
		double dou_valor_codificado=0;
		double dou_valor_finan=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_finan=pckUtilidades.CConversion.CDbl_2(tab_financiamiento.getValor("valor_financiamiento_prpof"));
		} catch (Exception e) {
		}
		
		String valor_reforma=tab_reforma.getSumaColumna("valor_reformado_prpor")+"";
		dou_valor_refor=pckUtilidades.CConversion.CDbl_2(valor_reforma);
		//calcula valor codficado
		dou_valor_codificado=dou_valor_finan+dou_valor_refor;
		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_poa.setValor("reforma_prpoa",utilitario.getFormatoNumero(valor_reforma,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "reforma_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorReforma(AjaxBehaviorEvent evt) {
		tab_reforma.modificar(evt); //Siempre es la primera linea
		calcularReforma();

	}


	public void seleccionar_arbol(NodeSelectEvent evt) {
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		StringBuilder str_ide_empleado = new StringBuilder();
		System.out.println(arb_arbol.getValorSeleccionado());
		
		arb_arbol.seleccionarNodo(evt);
		tab_poa.setCondicion("ide_geeso="+arb_arbol.getValorSeleccionado());
		tab_poa.ejecutarSql();		
		tab_poa.getColumna("ide_geeso").setValorDefecto(arb_arbol.getValorSeleccionado());
		tab_grupo_ocupacional.ejecutarValorForanea(arb_arbol.getValorSeleccionado());
		//tab_grupo_ocupacional.setCondicion("ide_geeso="+tab_poa.getValorSeleccionado());
		tab_grupo_ocupacional.ejecutarSql();		
		//tab_grupo_ocupacional.getColumna("ide_geeso").setValorDefecto(arb_arbol.getValorSeleccionado());
	 if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().equals("") || arb_arbol.getValorSeleccionado().isEmpty()) {
		 str_ide_empleado.append("-1");
		 }else {

				if (tab_poa.getValor("IDE_GEDEP")==null || tab_poa.getValor("IDE_GEDEP").equals("") || tab_poa.getValor("IDE_GEDEP").isEmpty()) {
					tab_empleados.setSql(genEmpleadoDepartamento("-1"));
				}else {
					tab_empleados.setSql(genEmpleadoDepartamento(tab_poa.getValor("IDE_GEPGC")));	
				}
				tab_empleados.ejecutarSql();
				utilitario.addUpdate("tab_empleados");				 

			 
			
		/*	TablaGenerica tabEmpleadoDepartamento=utilitario.consultar("select * from gen_partida_grupo_cargo where ide_geeso="+arb_arbol.getValorSeleccionado());
		
			if (tabEmpleadoDepartamento.getTotalFilas()>0) {
				
			
			int valor=0;
	       		for (int i = 0; i < tabEmpleadoDepartamento.getTotalFilas(); i++) 
	     		{
	     		//Voy anidando los ides de la accion
	      	    str_ide_empleado.append(tabEmpleadoDepartamento.getValor(i, "IDE_GEPGC"));
	           // valor++;
	            if (tabEmpleadoDepartamento.getTotalFilas()==1) {
				}else if (valor<=tabEmpleadoDepartamento.getTotalFilas()) {
						valor++;
						if(valor<(tabEmpleadoDepartamento.getTotalFilas())){
	                str_ide_empleado.append(",");
	               // System.out.println("str_ide:  "+str_ide_empleado);
						}
				}
	             }
			}else {
				str_ide_empleado.append("-1");
			}
	}	
	 
	 */
	 
			
		//tab_mes_codificado.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_reforma.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_archivo.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_financiamiento.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_financiamiento_reforma.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		//tab_financiamiento_ejecucion.ejecutarValorForanea(tab_poa.getValorSeleccionado());
	 }
	}

	public void actualizadorClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
				
		set_clasificador_actua.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnioTTHH("true",com_anio.getValue().toString()));
		set_clasificador_actua.getTab_seleccion().ejecutarSql();
		set_clasificador_actua.dibujar();
	}
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		/*
		//Si la tabla esta vacia
		if(tab_poa.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		*/
		//Filtrar los clasificadores del año seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnioTTHH("true",com_anio.getValue().toString()));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
	    tab_poa.setValor("ide_gepap","5");			
	    tab_poa.modificar(tab_poa.getFilaActual());
		utilitario.addUpdate("tab_pao");	
		set_clasificador.cerrar();
		
		/*if(set_clasificador.getValorSeleccionado()!=null){
			tab_poa.insertar();
			tab_poa.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			tab_poa.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_poa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}*/
	}

	public void aceptarActualizaClasificador(){
		if(set_clasificador_actua.getValorSeleccionado()!=null){			
			tab_poa.setValor("ide_prcla", set_clasificador_actua.getValorSeleccionado());
			tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
			//Actualiza 
			tab_poa.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_poa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador_actua.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}	
	
	
	public void asignarPartidaGrupoCargo(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;

		}
		

		 String tab_partida_presupuestaria="SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP,pgc.TITULO_CARGO_GEPGC  "
		 		+ "from GEN_PARTIDA_GRUPO_CARGO pgc  "
		 		+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP  "
		 		+ "where pgc.ACTIVO_GEPGC=TRUE and  IDE_GEPGC in( "
		 		+ "select par.ide_gepgc from gen_empleados_departamento_par par   "
		 		+ "left join gth_empleado emp on emp.ide_gtemp=par.ide_gtemp "
		 		+ "where emp.activo_gtemp=true and par.activo_geedp in(true) order by par.ide_geedp asc "
		 		+ ") order by IDE_GEPGC";
		 
		
		 TablaGenerica tab_gen_empleados_departamento_par= utilitario.consultar("select * "
		 + "from gen_empleados_departamento_par "
		 + "where ide_gtemp in (select ide_gtemp from gth_empleado emp where activo_gtemp=true) and activo_geedp=true order by ide_gtemp asc");
	
		

		 TablaGenerica tab_estrutura= null;
		 
		 
StringBuilder str_ide_empleado=null;
TablaGenerica gen_estructura_organizacional=null;
		 if (tab_gen_empleados_departamento_par.getTotalFilas()>0) {

		for (int i = 0; i < tab_gen_empleados_departamento_par.getTotalFilas(); i++) {
		
			gen_estructura_organizacional = utilitario.consultar("select * "
					 + "from gen_partida_grupo_cargo where ide_gepgc="+tab_gen_empleados_departamento_par.getValor(i,"IDE_GEPGC"));
			
			if (gen_estructura_organizacional.getTotalFilas()>0) {
				tab_estrutura= utilitario.consultar("select ges.ide_geeso,ges.pre_ide_geeso "
						 + "from gen_estructura_organizacional ges "
						 + "left join gen_vigente_estructura_organizacional geo on geo.ide_geeso=ges.ide_geeso "
						 + "where ges.ide_geare in ("+gen_estructura_organizacional.getValor("IDE_GEARE")+") and ges.ide_gedep in("+gen_estructura_organizacional.getValor("IDE_GEARE")+")  "
						 + "and geo.ide_geani=11 AND ges.ide_sucu in ("+gen_estructura_organizacional.getValor("IDE_SUCU")+") "
						 + "order by ide_geeso asc)");
		
				if (tab_estrutura.getTotalFilas()>0) {
					utilitario.getConexion().ejecutarSql("update gen_partida_grupo_cargo set ide_geani=11, ide_geeso=" + tab_estrutura.getValor("ide_geeso") + " "
							+ "where ide_gepgc="+ gen_estructura_organizacional.getValor("IDE_GEPGC") );
					
				}else {
					System.out.println("Sin SUCUAREADEPT() partidas no actualizadas"+tab_gen_empleados_departamento_par.getValor(i,"IDE_GEPGC"));
				}

				
			}
			
			
		   			 }
		         		

			
			
			
			
			
			
		}
		
	///	set_partida_cargo_funcional.getTab_seleccion().setSql(ser_general.getGenPartidaGrupoCargo("true"));
		set_partida_cargo_funcional.getTab_seleccion().ejecutarSql();
		set_partida_cargo_funcional.dibujar();
	
	}
	
	
	
	public void aceptarActualizaEstructuraPartida(){
		
		/*TablaGenerica tabPartidaGrupoCargo=utilitario.consultar("SELECT ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, ide_gedep, ide_geare,  "
				+ "ide_gepap, ide_gttem, titulo_cargo_gepgc, salario_encargo_gepgc,  "
				+ "fecha_activacion_gepgc, fecha_desactiva_gepgc, motivo_gepgc,  "
				+ "activo_gepgc,vacante_gepgc, encargo_gepgc "
				+ "FROM gen_partida_grupo_cargo "
				+ "WHERE ide_gepgc="+set_partida_cargo_funcional.getValorSeleccionado());
		
		tab_poa.modificar(tab_poa.getFilaActual());*/		
		//tab_poa.insertar();		
		/*tab_poa.setValor("IDE_GEGRO",tabPartidaGrupoCargo.getValor("ide_gepgc"));
		tab_poa.setValor("IDE_GEGRO",tabPartidaGrupoCargo.getValor("ide_gegro"));
		tab_poa.setValor("IDE_GECAF",tabPartidaGrupoCargo.getValor("ide_gecaf"));
		tab_poa.setValor("IDE_SUCU",tabPartidaGrupoCargo.getValor("ide_sucu"));
		tab_poa.setValor("IDE_GEARE",tabPartidaGrupoCargo.getValor("ide_geare"));
		tab_poa.setValor("IDE_GEDEP",tabPartidaGrupoCargo.getValor("ide_gedep"));
		tab_poa.setValor("IDE_GEPAP",tabPartidaGrupoCargo.getValor("ide_gepap"));
		tab_poa.setValor("ide_gttem",tabPartidaGrupoCargo.getValor("ide_gttem"));
		tab_poa.setValor("titulo_cargo_gepgc",tabPartidaGrupoCargo.getValor("titulo_cargo_gepgc"));
		tab_poa.setValor("SALARIO_ENCARGO_GEPGC",tabPartidaGrupoCargo.getValor("salario_encargo_gepgc"));
		tab_poa.setValor("FECHA_ACTIVACION_GEPGC",tabPartidaGrupoCargo.getValor("fecha_activacion_gepgc"));		
		tab_poa.setValor("FECHA_DESACTIVA_GEPGC",tabPartidaGrupoCargo.getValor("fecha_desactiva_gepgc"));
		tab_poa.setValor("MOTIVO_GEPGC",tabPartidaGrupoCargo.getValor("motivo_gepgc"));
		tab_poa.setValor("ACTIVO_GEPGC",tabPartidaGrupoCargo.getValor("activo_gepgc"));
		tab_poa.setValor("VACANTE_GEPGC",tabPartidaGrupoCargo.getValor("vacante_gepgc"));
		tab_poa.setValor("ENCARGO_GEPGC",tabPartidaGrupoCargo.getValor("encargo_gepgc"));
		*/
		tab_poa.setCondicion("ide_gepgc ="+set_partida_cargo_funcional.getValorSeleccionado());
		tab_poa.ejecutarSql();
		set_partida_cargo_funcional.cerrar();
		tab_poa.setValor("ide_geeso", arb_arbol.getValorSeleccionado());
		tab_poa.setValor("ide_geani", com_anio.getValue()+"");
		tab_poa.modificar(tab_poa.getFilaActual());
		tab_poa.guardar();
		guardarPantalla();
		utilitario.addUpdate("tab_poa,set_partida_cargo_funcional");

	}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Plan Operativo Anual (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
				aceptarSelPoa();
				
			}
			else if(sel_poa.isVisible()) {
				
				if(sel_poa.getListaSeleccionados().size()>0){	
					p_parametros.put("ide_prpoa",sel_poa.getSeleccionados());
					p_parametros.put("titulo","Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_poa.cerrar();
				   self_reporte.dibujar();
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
		
		
		/////REFORMA
		else if(rep_reporte.getReporteSelecionado().equals("Reforma Plan Operativo Anual (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
				aceptarSelResolucion();
			}
			else if(sel_resolucion.isVisible()) {
				
				if(sel_resolucion.getListaSeleccionados().size()>0){	
					p_parametros.put("pnro_resolucion",sel_resolucion.getSeleccionados());
					p_parametros.put("titulo","Reforma Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
	
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_resolucion.cerrar();
				   self_reporte.dibujar();
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
		
		//REFORMA Fuente
		else if(rep_reporte.getReporteSelecionado().equals("Reforma Fuentes (POA)")){
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
				aceptarSelResolucion();
			}
			else if(sel_resolucion.isVisible()) {
				
				if(sel_resolucion.getListaSeleccionados().size()>0){	
					p_parametros.put("pnro_resolucion",sel_resolucion.getSeleccionados());
					p_parametros.put("titulo","Reforma-Fuentes Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
					p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(com_anio.getValue().toString()));
	
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					sel_resolucion.cerrar();
				    self_reporte.dibujar();
				}		
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
	
	}

	public void inicializarSelPoa(){

		/////dialogo para reporte
		sel_poa.setId("sel_poa");
		sel_poa.setSeleccionTabla("select a.ide_prpoa,detalle_programa,programa,detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
				" detalle_subactividad,subactividad,codigo_subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa," +
				" presupuesto_codificado_prpoa,reforma_prpoa,detalle_geani,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_geare" +
				" from pre_poa a" +
				" left join  gen_anio b on a.ide_geani= b.ide_geani" +
				" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" +
				" left join (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad," +
				" detalle_producto,producto,detalle_proyecto,proyecto,detalle_programa ,programa" +
				" from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,detalle_prnfp as subactividad" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3) c, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e" +
				" where a.pre_ide_prfup = b.ide_prfup" +
				" and b.pre_ide_prfup = c.ide_prfup" +
				" and c.pre_ide_prfup = d.ide_prfup" +
				" and d.pre_ide_prfup = e.ide_prfup" +
				" ) f on a.ide_prfup = f.ide_prfup" +
				" left join gen_area g on a.ide_geare=g.ide_geare" +
				" where a.ide_geani=-1" +				
			    " order by codigo_subactividad,a.ide_prpoa", "ide_prpoa");
		sel_poa.getTab_seleccion().ejecutarSql();
		sel_poa.getTab_seleccion().getColumna("detalle_programa").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_proyecto").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_actividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_subactividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("codigo_subactividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		sel_poa.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_poa);
		
	} 

	public void aceptarSelPoa(){
		sel_poa.getTab_seleccion().setSql("select a.ide_prpoa,detalle_programa,programa,detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
			" detalle_subactividad,subactividad,codigo_subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa," +
			" presupuesto_codificado_prpoa,reforma_prpoa,detalle_geani,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_geare" +
			" from pre_poa a" +
			" left join  gen_anio b on a.ide_geani= b.ide_geani" +
			" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" +
			" left join (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad," +
			" detalle_producto,producto,detalle_proyecto,proyecto,detalle_programa ,programa" +
			" from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,detalle_prnfp as subactividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3) c, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e" +
			" where a.pre_ide_prfup = b.ide_prfup" +
			" and b.pre_ide_prfup = c.ide_prfup" +
			" and c.pre_ide_prfup = d.ide_prfup" +
			" and d.pre_ide_prfup = e.ide_prfup" +
			" ) f on a.ide_prfup = f.ide_prfup" +
			" left join gen_area g on a.ide_geare=g.ide_geare" +
			" where a.ide_geani=" +com_anio.getValue()+				
		    " order by codigo_subactividad,a.ide_prpoa");
		sel_poa.getTab_seleccion().ejecutarSql();
		sel_poa.getBot_aceptar().setMetodo("aceptarReporte");
		sel_poa.dibujar();
	
	}

	public void inicializarSelResolucion (){
		//dialogo para reporte
		sel_resolucion.setId("sel_resolucion");
		sel_resolucion.setSeleccionTabla("select resolucion_prpor as codigo,resolucion_prpor " +
				" from pre_poa_reforma group by resolucion_prpor order by codigo desc","codigo");
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_resolucion);
	
	}

	public void aceptarSelResolucion (){
        //dialogo para reporte
		sel_resolucion.getTab_seleccion().setSql("select resolucion_prpor as codigo,resolucion_prpor " +
				" from pre_poa_reforma group by resolucion_prpor");
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("aceptarReporte");
		sel_resolucion.dibujar();
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;

		}
		
			TablaGenerica tabEstructura=null;
		if (tab_poa.isFocus()) {
			
			 if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().toString().equals("") || arb_arbol.getValorSeleccionado().toString().isEmpty()) {
				 tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geeso=-1 ");
				 
					utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;	 
			 
			 }else {
				 tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
						
			}					
			if (tabEstructura.getTotalFilas()>0) {
			 if (tabEstructura.getValor("IDE_GEARE")==null || tabEstructura.getValor("IDE_GEARE").equals("") || tabEstructura.getValor("IDE_GEARE").isEmpty()) {
				 utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;			
			}
			 if (tabEstructura.getValor("IDE_GEDEP")==null || tabEstructura.getValor("IDE_GEDEP").equals("") || tabEstructura.getValor("IDE_GEDEP").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;	 
			 }
		
			}
			
			
			
			
			
			tab_poa.getColumna("IDE_GEARE").setLectura(false);
			tab_poa.getColumna("IDE_GEDEP").setLectura(false);

			
			tab_poa.insertar();
			tab_poa.setValor("ide_geeso", arb_arbol.getValorSeleccionado()+"");
			//tab_poa.setValor("ide_geare", arb_arbol.getValorSeleccionado()+"");
			//tab_poa.setValor("ide_gedep", arb_arbol.getValorSeleccionado()+"");
			tab_poa.getColumna("IDE_SUCU").setCombo("select gds.ide_sucu,sucu.nom_sucu,gds.ide_gedep,gds.ide_geare  "
				+ "from  "
				+ "gen_departamento_sucursal  gds "
				+ "left join sis_sucursal sucu on sucu.ide_sucu=gds.ide_sucu "
				+ "where ide_geare="+tabEstructura.getValor("IDE_GEARE")+" and ide_gedep="+tabEstructura.getValor("IDE_GEDEP"));
			
			/*tab_poa.getColumna("IDE_GEARE").setCombo("select gds.ide_sucu,sucu.nom_sucu,gds.ide_gedep,gds.ide_geare  "
					+ "from  "
					+ "gen_departamento_sucursal  gds "
					+ "left join sis_sucursal sucu on sucu.ide_sucu=gds.ide_sucu "
					+ "where ide_geare="+tabEstructura.getValor("IDE_GEARE")+" and ide_gedep="+tabEstructura.getValor("IDE_GEDEP"));
	
			tab_poa.getColumna("IDE_GEARE").setCombo("select gds.ide_sucu,sucu.nom_sucu,gds.ide_gedep,gds.ide_geare  "
					+ "from  "
					+ "gen_departamento_sucursal  gds "
					+ "left join sis_sucursal sucu on sucu.ide_sucu=gds.ide_sucu "
					+ "where ide_geare="+tabEstructura.getValor("IDE_GEARE")+" and ide_gedep="+tabEstructura.getValor("IDE_GEDEP"));
		*/
			
			tab_poa.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
					"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE "
					+ "where b.ide_geare="+tabEstructura.getValor("IDE_GEARE")+
					" GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
					" ORDER BY b.DETALLE_GEARE");
			tab_poa.setValor("IDE_GEARE",tabEstructura.getValor("IDE_GEARE").toString());
			//+tab_poa.getColumna("IDE_GEARE").setMetodoChange("cargarDepartamentos");
			//tab_poa.getColumna("IDE_GEARE").setBuscarenCombo(false);
			tab_poa.getColumna("IDE_GEDEP").setCombo("SELECT DISTINCT a.IDE_GEDEP,a.DETALLE_GEDEP  "
					+ "FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b  "
					+ "WHERE a.IDE_GEDEP=b.IDE_GEDEP  "
					+ "and b.ide_gedep="+tabEstructura.getValor("IDE_GEDEP"));
			tab_poa.setValor("IDE_GEDEP",tabEstructura.getValor("IDE_GEDEP").toString());
			tab_poa.getColumna("IDE_GEARE").setLectura(true);
			tab_poa.getColumna("IDE_GEDEP").setLectura(true);

			
		}
		
		/*
		else if (tab_mes.isFocus()) {
			tab_mes.insertar();

		}
		else if (tab_mes_codificado.isFocus()) {
			tab_mes_codificado.insertar();
		}
		else if (tab_reforma.isFocus()) {
			tab_reforma.insertar();
			tab_reforma.setValor("SALDO_ACTUAL_PRPOR", tab_poa.getValor("presupuesto_codificado_prpoa"));

		}

		else if (tab_financiamiento.isFocus()){
			tab_financiamiento.insertar();

		}
		else if (tab_archivo.isFocus()) {
			tab_archivo.insertar();

		}
		else if (tab_financiamiento_reforma.isFocus()){
			tab_financiamiento_reforma.insertar();
		}*/
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		//double presupuesto_inicial = pckUtilidades.CConversion.CDbl_2(tab_poa.getValor("presupuesto_inicial_prpoa"));
		//double presupuesto_codificado = pckUtilidades.CConversion.CDbl_2(tab_poa.getValor("presupuesto_codificado_prpoa"));
        //double presupuesto_ejecutado = tab_mes.getSumaColumna("valor_presupuesto_prpom");
        //double presupuesto_ejecutado_codificado = tab_mes_codificado.getSumaColumna("valor_presupuesto_prpmc");
		//double presupuesto_reformado = pckUtilidades.CConversion.CDbl_2(tab_poa.getValor("reforma_prpoa"));
	
		/*
        if(presupuesto_inicial<0){
        	utilitario.agregarMensajeInfo("Presupuesto Inicial", "EL valor del presupuesto inicial no puede ser negativo");
            return;
        }
        if(presupuesto_reformado !=0){
        	if(presupuesto_codificado<0){ 
        	utilitario.agregarMensajeInfo("Reforma Mensual", "No puede reforma con un valor superior al presupuesto inicial asignado");
            return;
        	}
        }
        if(presupuesto_ejecutado>presupuesto_codificado){
        	utilitario.agregarMensajeInfo("Ejecución Mensual", "No puede ejecutar un valor superior al presupuesto codificado");
            return;
        }
        if(tab_financiamiento.isFocus()){        	
        	
        	for(int i=0;i<tab_financiamiento.getTotalFilas();i++){
        		TablaGenerica tabla1= utilitario.consultar(ser_presupuesto.getInicialFuenteFinanciamiento(tab_financiamiento.getValor(i,"ide_prfuf"), com_anio.getValue().toString()));
        		double valor_ingresado= pckUtilidades.CConversion.CDbl_2(tab_financiamiento.getValor(i, "valor_financiamiento_prpof"));
        		BigDecimal big_valor_ingresado=new BigDecimal(valor_ingresado);
        		big_valor_ingresado=big_valor_ingresado.setScale(2, RoundingMode.HALF_UP);
        		
        		double ejcutado_inicial=0;
        		double ejecucion=0;

        		if(tabla1.getTotalFilas()>0){

        			double asi_inicial=pckUtilidades.CConversion.CDbl_2(tabla1.getValor("valor"));
            		BigDecimal big_inicial=new BigDecimal(asi_inicial);
            		big_inicial=big_inicial.setScale(2, RoundingMode.HALF_UP);


        			TablaGenerica tabla2=utilitario.consultar(ser_presupuesto.getEjecutaFuenteFinanciamiento(tab_financiamiento.getValor(i, "ide_prfuf"), com_anio.getValue().toString(),tab_financiamiento.getValor(i, "ide_prpof")));
        			if(tabla2.getTotalFilas()>0){
        				ejcutado_inicial=pckUtilidades.CConversion.CDbl_2(utilitario.getFormatoNumero(tabla2.getValor("valor"),2));
        				
        				BigDecimal big_ejecutado_inicial=new BigDecimal(ejcutado_inicial);
        				big_ejecutado_inicial=big_ejecutado_inicial.setScale(2, RoundingMode.HALF_UP);
                		
        				ejecucion=pckUtilidades.CConversion.CDbl_2(big_valor_ingresado)+pckUtilidades.CConversion.CDbl_2(big_ejecutado_inicial);
          				
        				BigDecimal big_ejecucion=new BigDecimal(ejecucion);
        				big_ejecucion=big_ejecucion.setScale(2, RoundingMode.HALF_UP);
        				
         				ejecucion=pckUtilidades.CConversion.CDbl_2(big_ejecucion);
        				asi_inicial=pckUtilidades.CConversion.CDbl_2(big_inicial);
        			}
        			else {
        				ejecucion=valor_ingresado+0;
        			}

        		 //cometando momentaneamente para revisuon
        			if(asi_inicial<ejecucion){        		
        				double saldo=asi_inicial-ejcutado_inicial; 
        				utilitario.agregarMensajeError("Asignación supera el valor total en el POA", "Ya no dispone de saldo suficiente para la fuente de financiamiento seleccionada SALDO: "+saldo +" fuente id: "+tab_financiamiento.getValor(i,"ide_prfuf"));
        				return;
        			}
        			
        		}
        		else{
        			utilitario.agregarMensajeError("No se puede asignar fuente de financiamiento", "No existe una fuente de Financiamiento Inicial asignada");
        			return;
        		}
        	
        	}
        	
        	//valida que la fuente de financiamiento sea igual ala valro asignado
        	double total_fin= pckUtilidades.CConversion.CDbl_2(tab_financiamiento.getSumaColumna("valor_financiamiento_prpof"));
        	//double total_fin=tab_financiamiento.getSumaColumna("valor_financiamiento_prpof");
        	double inicial= pckUtilidades.CConversion.CDbl_2(tab_poa.getValor("presupuesto_inicial_prpoa"));
        	
        	System.out.println("total_fin "+total_fin+ " inicial "+inicial);
        	if(total_fin!=inicial){
        		utilitario.agregarMensajeError("Fuente de Financiamiento", "La Asignación de la fuente de financiamiento no conicide con el valor del presupuesto inicial");
        		return;
        	}
        	
        	tab_financiamiento.guardar();
			guardarPantalla();

        }*/
  
		if (tab_poa.isFocus()){
			
			if (tab_poa.getTotalFilas() > 0) {			
				TablaGenerica tab_partida = utilitario.consultar("SELECT * FROM GEN_PARTIDA_GRUPO_CARGO WHERE IDE_GEPGC="+ tab_poa.getValor("IDE_GEPGC")+ " AND vacante_gepgc=false");
				System.out.println("imprimo sql"+tab_partida.getSql());
				if (tab_partida.getTotalFilas() > 0) {
					utilitario.agregarMensajeInfo("No se puede guardar","Esta partida ya se encuentra en uso");
				} else {
					tab_poa.guardar();
					guardarPantalla();

				}
			} else {
			}	
			
		
			

	}	
/*			if (tab_mes.guardar()) {				
					if(tab_reforma.guardar()){
						if(tab_financiamiento_reforma.guardar()){			
							
							tab_archivo.guardar();
							guardarPantalla();
							if(tab_financiamiento_reforma.isFocus()){
								//triger que se ejcuta cuando realiza Insert, Update, Delete en las reformas por fuentes
							ser_presupuesto.trigActualizaReformaFuente(tab_poa.getValor("ide_prpoa"));
							tab_reforma.ejecutarSql();
							//triger que se ejcuta cuando realiza Insert, Update, Delete en las reformas
							ser_presupuesto.trigActualizaReforma(tab_poa.getValor("ide_prpoa"));
						    tab_poa.ejecutarSql();
							}
						}
					}				
			}
			
			if (tab_mes_codificado.guardar()) {				
				if(tab_reforma.guardar()){
					if(tab_financiamiento_reforma.guardar()){			
						
						tab_archivo.guardar();
						guardarPantalla();
						if(tab_financiamiento_reforma.isFocus()){
							//triger que se ejcuta cuando realiza Insert, Update, Delete en las reformas por fuentes
						ser_presupuesto.trigActualizaReformaFuente(tab_poa.getValor("ide_prpoa"));
						tab_reforma.ejecutarSql();
						//triger que se ejcuta cuando realiza Insert, Update, Delete en las reformas
						ser_presupuesto.trigActualizaReforma(tab_poa.getValor("ide_prpoa"));
					    tab_poa.ejecutarSql();
						}
					}
				}				
		}
		}*/
		
	}

	public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_poa.isFocus()){
			tab_poa.eliminar();
		}
		else if (tab_mes.isFocus()){
			tab_mes.eliminar();
			tab_mes.sumarColumnas();
			utilitario.addUpdate("tab_mes");


		}
		else if (tab_mes_codificado.isFocus()){
			tab_mes_codificado.eliminar();
			tab_mes_codificado.sumarColumnas();
			utilitario.addUpdate("tab_mes_codificado");


		}
		else if (tab_reforma.isFocus()){
			tab_reforma.eliminar();
			calcularReforma();

		}
		else if (tab_financiamiento.isFocus()){
			tab_financiamiento.eliminar();
		}
		else if (tab_archivo.isFocus()){
			tab_archivo.eliminar();
		}
		else if(tab_financiamiento_reforma.isFocus()){
			tab_financiamiento_reforma.eliminar();
		}
	}
	
	public void filtrarAnio(){

		if(com_anio.getValue()!=null){
			arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where not ide_geeso is null and ide_geani="+com_anio.getValue()+")");		
			//tab_poa.getColumna("ide_geani").setValorDefecto(com_anio.getValue().toString());
		}
		else{
			arb_arbol.setCondicion("ide_geeso in (select ide_geeso from gen_vigente_estructura_organizacional where not ide_geeso is null and ide_geani=-1 )");
		}
		arb_arbol.ejecutarSql();		
		
		utilitario.addUpdate("arb_arbol");


		TablaGenerica tabPartidaPresupuestaria=utilitario.consultar("SELECT ide_gepap, detalle_gepap, codigo_partida_gepap, activo_gepap, "
				+ "ide_prasp, ide_prcla "
				+ "FROM gen_partida_presupuestaria");
		
	String[] partida_presupustaria=null;
	String resultado="";	
	int contadorPuntos=0;
	String punto=".";
	 
	 List<String> partes = new ArrayList<String>();
	for (int i = 0; i < tabPartidaPresupuestaria.getTotalFilas(); i++) {
	    String texto =  tabPartidaPresupuestaria.getValor(i, "CODIGO_PARTIDA_GEPAP");
	   
	    for (int x = 0; x < texto.length(); x += 2) {
	    	partes.add(texto.substring(x, Math.min(x + 2,texto.length())));}
	    for (int x = 0; x < partes.size(); x++) {
	    	System.out.println(partes.get(x));   }
	    
	    for (int x = 0; x < partes.size(); x++)
			{
	    	contadorPuntos++;
			
			if (contadorPuntos<=1) {
				punto=partes.get(x).toString()+".";	
			}else if(contadorPuntos>1 && contadorPuntos<(partes.size())){
			punto=punto+""+partes.get(x).toString()+".";
		}else {
			punto=punto+""+partes.get(x).toString();
		}

	}


		TablaGenerica tabClasificador=utilitario.consultar("SELECT ide_prcla, pre_ide_prcla,  "
				+ "codigo_clasificador_prcla, descripcion_clasificador_prcla,  "
				+ "tipo_prcla, nivel_prcla, grupo_prcla, sigefc_prcla, activo_prcla, "
				+ "ide_prgre "
				+ "FROM pre_clasificador "
				+ "where codigo_clasificador_prcla like '%"+punto+"%'");
	    
	    
		utilitario.getConexion().ejecutarSql("update gen_partida_presupuestaria set ide_prcla="+tabClasificador.getValor("ide_prcla")+" where ide_gepap in ("+tabPartidaPresupuestaria.getValor(i,"ide_gepap")+");");

	contadorPuntos=0;
	 partes = new ArrayList<String>();
		
	}
		
		
	
		
	}



	public Tabla getTab_poa() {
		return tab_poa;
	}

	public void setTab_poa(Tabla tab_poa) {
		this.tab_poa = tab_poa;
	}

	public Tabla getTab_mes() {
		return tab_mes;
	}

	public Tabla getTab_mes_codificado() {
		return tab_mes_codificado;
	}
	
	public void setTab_mes(Tabla tab_mes) {
		this.tab_mes = tab_mes;
	}

	public void setTab_mes_codificado(Tabla tab_mes_codificado) {
		this.tab_mes_codificado = tab_mes_codificado;
	}

	public Tabla getTab_financiamiento() {
		return tab_financiamiento;
	}

	public void setTab_financiamiento(Tabla tab_financiamiento) {
		this.tab_financiamiento = tab_financiamiento;
	}

	public Tabla getTab_archivo() {
		return tab_archivo;
	}

	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}

	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}

	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public Tabla getTab_reforma() {
		return tab_reforma;
	}

	public void setTab_reforma(Tabla tab_reforma) {
		this.tab_reforma = tab_reforma;
	}

	public SeleccionTabla getSet_funcion() {
		return set_funcion;
	}

	public void setSet_funcion(SeleccionTabla set_funcion) {
		this.set_funcion = set_funcion;
	}

	public Tabla getTab_funcion_programa() {
		return tab_funcion_programa;
	}

	public void setTab_funcion_programa(Tabla tab_funcion_programa) {
		this.tab_funcion_programa = tab_funcion_programa;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}
	public SeleccionTabla getSet_sub_actividad() {
		return set_sub_actividad;
	}
	public void setSet_sub_actividad(SeleccionTabla set_sub_actividad) {
		this.set_sub_actividad = set_sub_actividad;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}
	public Map getMap_parametros() {
		return map_parametros;
	}
	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}
	public SeleccionTabla getSel_poa() {
		return sel_poa;
	}
	public void setSel_poa(SeleccionTabla sel_poa) {
		this.sel_poa = sel_poa;
	}
	public SeleccionTabla getSel_resolucion() {
		return sel_resolucion;
	}
	public void setSel_resolucion(SeleccionTabla sel_resolucion) {
		this.sel_resolucion = sel_resolucion;
	}

	public Tabla getTab_financiamiento_reforma() {
		return tab_financiamiento_reforma;
	}

	public void setTab_financiamiento_reforma(Tabla tab_financiamiento_reforma) {
		this.tab_financiamiento_reforma = tab_financiamiento_reforma;
	}
	public Tabla getTab_financiamiento_ejecucion() {
		return tab_financiamiento_ejecucion;
	}
	public void setTab_financiamiento_ejecucion(Tabla tab_financiamiento_ejecucion) {
		this.tab_financiamiento_ejecucion = tab_financiamiento_ejecucion;
	}
	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}
	public SeleccionTabla getSet_saldo_fuente() {
		return set_saldo_fuente;
	}
	public void setSet_saldo_fuente(SeleccionTabla set_saldo_fuente) {
		this.set_saldo_fuente = set_saldo_fuente;
	}
	public SeleccionTabla getSet_clasificador_actua() {
		return set_clasificador_actua;
	}
	public void setSet_clasificador_actua(SeleccionTabla set_clasificador_actua) {
		this.set_clasificador_actua = set_clasificador_actua;
	}
	public Tabla getTab_grupo_ocupacional() {
		return tab_grupo_ocupacional;
	}
	public void setTab_grupo_ocupacional(Tabla tab_grupo_ocupacional) {
		this.tab_grupo_ocupacional = tab_grupo_ocupacional;
	}
	public SeleccionTabla getSet_partida_cargo_funcional() {
		return set_partida_cargo_funcional;
	}
	public void setSet_partida_cargo_funcional(
			SeleccionTabla set_partida_cargo_funcional) {
		this.set_partida_cargo_funcional = set_partida_cargo_funcional;
	}
	public Tabla getTab_empleados() {
		return tab_empleados;
	}
	public void setTab_empleados(Tabla tab_empleados) {
		this.tab_empleados = tab_empleados;
	}
	
	public void seleccionarPartidaPregusta(SelectEvent evt) {
		tab_poa.seleccionarFila(evt);
		tab_empleados.setSql("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP  "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "where EPAR.IDE_GEPGC="+tab_poa.getValor("IDE_GEPGC")
				+ " and epar.activo_geedp=true");	
		tab_empleados.ejecutarSql();
		utilitario.addUpdate("tab_empleados");
	}
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizar();
	}
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizar();
	}

	
	
	public void actualizar(){
		tab_empleados.setSql("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP  "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "where EPAR.IDE_GEPGC="+tab_poa.getValor("IDE_GEPGC")
				+ " and epar.activo_geedp=true");	
		tab_empleados.ejecutarSql();
		utilitario.addUpdate("tab_empleados");
		
		
	}
	
	public String genEmpleadoDepartamento(String variable){
	String sql="SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
			+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
			+ "DEPA.DETALLE_GEDEP  "
			+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP  "
			+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
			+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
			+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
			+ "where epar.ide_gepgc in("+variable+") "
			+ "and epar.activo_geedp=true";
	return sql;
	}
	
	public void mostrarSetReformaFuente(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		//else if(com_fuente_financiamiento.getValue()==null){
		//	utilitario.agregarMensajeInfo("Fuente de Financiamiento", "Seleccione una fuente de Financiamiento para realizar la reforma");
		//	return;
		//}
		else {
		//tab_poa_reforma_fuenta.setCondicion("ide_prprf=-1 and ide_geani=-1");	
		//tab_poa_reforma_fuenta.ejecutarSql();
		//eti_valor_total.setValue("0.00");
		//utilitario.addUpdate("tab_poa_reforma_fuenta,eti_valor_total");
			TablaGenerica tabEstructura=null;
			 if (arb_arbol.getValorSeleccionado()==null || arb_arbol.getValorSeleccionado().toString().equals("") || arb_arbol.getValorSeleccionado().toString().isEmpty()) {
				 tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geeso=-1 ");
				 
					utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;	 
			 
			 }else {
				 tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geeso="+arb_arbol.getValorSeleccionado());
						
			}					
			if (tabEstructura.getTotalFilas()>0) {
			 if (tabEstructura.getValor("IDE_GEARE")==null || tabEstructura.getValor("IDE_GEARE").equals("") || tabEstructura.getValor("IDE_GEARE").isEmpty()) {
				 utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;			
			}
			 if (tabEstructura.getValor("IDE_GEDEP")==null || tabEstructura.getValor("IDE_GEDEP").equals("") || tabEstructura.getValor("IDE_GEDEP").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede ingresar el registro", "Revise el nivel jerarquico debe contener los campos de area y departamento");
					 return ;	 
			 }
		
			}

			
		utilitario.getConexion().ejecutarSql("delete from pre_poa_reforma_fuente where aprobado_prprf = false and resolucion_prprf is null;");
		set_reforma_fuente.getTab_seleccion().setSql(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),"26","0","","9"));
		//com_anio.getValue().toString(),com_fuente_financiamiento.getValue().toString(),"0","-1"));
		set_reforma_fuente.getTab_seleccion().ejecutarSql();
		set_reforma_fuente.dibujar();
		}
	}


	public SeleccionTabla getSet_reforma_fuente() {
		return set_reforma_fuente;
	}


	public void setSet_reforma_fuente(SeleccionTabla set_reforma_fuente) {
		this.set_reforma_fuente = set_reforma_fuente;
	}
	
	//////////////////SE ME CARGA EL MISMO DATO AL OTRO CAMPO 
	
public void grupoOcupacional(AjaxBehaviorEvent evt){
	tab_poa.modificar(evt);//Siempre es la primera linea
	//tab_tabla.setValor("IDE_GECAF", tab_tabla.getValor("IDE_GEGRO"));
	//utilitario.addUpdateTabla(tab_tabla, "IDE_GECAF", "");
	
	String sql="select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a, "
			+ "GEN_GRUPO_CARGO b where a.IDE_GECAF = b.IDE_GECAF and b.ide_gegro="+tab_poa.getValor("ide_gegro");
	System.out.println("grupoOcupacional "+sql);
	tab_poa.getColumna("IDE_GECAF").setCombo(sql);
	
	utilitario.addUpdate("tab_poa");
}
	public void aceptarPoa(){
     
		System.out.println(set_reforma_fuente.getAttributes().get(1));		

	System.out.println(set_reforma_fuente.getChildren().get(0).toString());		
	System.out.println(set_reforma_fuente.getAttributes().get(1).toString());	
	System.out.println(set_reforma_fuente.getAttributes().get(2));	
	System.out.println(set_reforma_fuente.getAttributes().get(3));	
	}
	public void modificarPartida(){
		String str_partidaActualizado=set_reforma_fuente.getValorSeleccionado();
	  TablaGenerica tab_partida_presupuestaria=utilitario.consultar("SELECT ide_gepap, detalle_gepap, "
	  		+ "codigo_partida_gepap, activo_gepap, "
	  		+ "ide_prasp, ide_prcla "
	  		+ "FROM gen_partida_presupuestaria "
	  		+ "WHERE IDE_");
		tab_poa.setValor("ide_gepap",str_partidaActualizado);			
	    tab_poa.modificar(tab_poa.getFilaActual());
		utilitario.addUpdate("tab_poa");	
/*
		con_guardar_partida.setMessage("Esta Seguro que desea Guardar ");
		con_guardar_partida.setTitle("CONFIRMCIÓN GUARDAR PARTIDA");
		con_guardar_partida.getBot_aceptar().setMetodo("guardarPartida");
		con_guardar_partida.dibujar();*/
		utilitario.addUpdate("con_guardar_partida");

	}
	
}

