package paq_sri;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sri.ejb.ServicioSRI;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.reportes.ReporteDataSource;
import groovy.util.IFileNameFinder;
/**
 *
 * @author DELL-USER
 */
public class pre_formulario_107 extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_seleccion = new Tabla();
	private Tabla tab_tabla3 = new Tabla();
	private String empleado="";
	private String ruta = "pre_index.clase";
    private ItemMenu itm_todas = new ItemMenu();
    private ItemMenu itm_niguna = new ItemMenu();
	String roles="";
	double sueldo=0.0;

	@EJB
	private ServicioSRI ser_sri = (ServicioSRI) utilitario.instanciarEJB(ServicioSRI.class);

	@EJB
	private ServicioEmpleado ser_empleados = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Map p_parametros = new HashMap();

	private SeleccionTabla set_empleado=new SeleccionTabla();
	private SeleccionTabla 	set_empleado_rdep=new SeleccionTabla();
	 private BotonesCombo boc_seleccion_inversa = new BotonesCombo();

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();	
	private SeleccionCalendario sec_fecha_entrega=new SeleccionCalendario();
	private AutoCompletar aut_empleado=new AutoCompletar();

	public pre_formulario_107() {
		
		Boton bot_rdep=new Boton();
		bot_rdep.setValue("Generar Anexo RDEP");
		bot_rdep.setAjax(false);
		bot_rdep.setMetodo("seleccionarEmpleado");
	//	bar_botones.agregarBoton(bot_rdep);
	
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
					


		
		Boton bot_formulario107=new Boton();
		bot_formulario107.setValue("Generar Formulario 107");
		//bot_formulario107.setAjax(false);
		bot_formulario107.setMetodoRuta("aceptarReporte");
	//	bar_botones.agregarBoton(bot_formulario107);
	
		
		Etiqueta eti_formulario=new Etiqueta("  GENERAR FORMULARIO 107");
		bar_botones.agregarComponente(eti_formulario);

		  
		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_seleccion");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_seleccion");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo( "seleccionarNinguna");
		itm_niguna.setUpdate("tab_seleccion");
		boc_seleccion_inversa.agregarBoton(itm_niguna);
		
	

  	    set_empleado.setId("set_empleado");
		//set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"), "ide_geedp");
	
  	    
  	  set_empleado.setSeleccionTabla("SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
  	  		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
  	  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) "
  	  		+ "AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
  	  		+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
  	  		+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
  	  		+ "order by EMP.APELLIDO_PATERNO_GTEMP", "IDE_GEEDP");
  	    
  	    set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");

		agregarComponente(set_empleado);


		bar_botones.agregarReporte();
		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("SRI_IMPUESTO_RENTA", "IDE_SRIMR", 1);
		tab_tabla1.getColumna("IDE_SRIMR").setNombreVisual("CÓDIGO");
		tab_tabla1.getColumna("DETALLE_SRIMR").setNombreVisual("AÑO FISCAL");
		tab_tabla1.getColumna("FECHA_INICIO_SRIMR").setNombreVisual("FEC.INICIO");
		tab_tabla1.getColumna("FECHA_INICIO_SRIMR").setNombreVisual("FEC.FINAL");
		tab_tabla1.getColumna("ACTIVO_SRIMR").setCheck();
		tab_tabla1.getColumna("ACTIVO_SRIMR").setNombreVisual("ESTADO");
		tab_tabla1.onSelect("seleccionoEmpleado");

		tab_tabla1.dibujar();
		tab_tabla1.setHeader("IMPUESTO A LA RENTA AÑO FISCAL ");
		
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);
		
		tab_seleccion.setId("tab_seleccion");
	/*	tab_seleccion.setSql("select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as  fraccion_basica_srdir   "
				+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr = 7 and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*   "
				+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     FROM public.sri_detalle_impuesto_renta  "
				+ "where  ide_srimr = 7 and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir          "
				+ "FROM public.sri_detalle_impuesto_renta   where  ide_srimr = 7 and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    "
				+ "as impuestoCausado   from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc, ide_geedp,       "
				+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,   documento_identidad_gtemp as idRet,      "
				+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,      "
				+ " COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab, '001' as estab,"
				+ "      '01'   as residenciaTrab,   '593' as pasisResidencia,      'NA' as aplicaConvenio,  "
				+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap, case when   "
				+ "round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap, 'N' as tipidDiscap,   "
				+ "'999' as idDiscap,      COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,    COALESCE(sobresueldos,0.00) as sobSuelComRemu, "
				+ "     '0' as partUtil,   '0' as intGrabGen,   '0' as impRentEmpl, "
				+ "COALESCE(DecimoTercero,0.00) as DecimoTercero,   case   "
				+ " when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>380.00 then 386.00 "
				+ "else "
				+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end  "
				+ "as decimoCuarto,  "
				//+ "  COALESCE(decimoTercero,0.00)-COALESCE(decimoTercerAjuste,0.00) as decimoTercer,  "
				//+ "    COALESCE(decimoCuarto, 0.00) as decimoCuarto,    "
				+ "COALESCE(fondo, 0.00) as  fondoReserva,      '0' as salarioDigno,   '0' as otrosIngRenGrav, "
				+ "  COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl,   '1' as sisSalNet, "
				+ "  COALESCE(aporteIess, 0.00) as apoPerIess,    '0.00' as aporPerIessConOtrosEmpls,      case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then "
				+ "round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   "
				+ "WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)   "
				+ " from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    "
				+ "where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
				+ "case when round(FLOOR(((extract(year from cast('2015-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  "
				+ "(extract(month from cast('2015-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  "
				+ "(extract (day from cast('2015-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 "
				+ "then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  "
				+ "WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "
				+ " where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,   "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       "
				+ "(COALESCE(vivienda.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(salud.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ "then fecha_geedp   else '2015-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +   "
				+ "   (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ "then fecha_geedp else '2015-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    "
				+ "else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda,  "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       "
				+ "(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015 "
				+ " then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ "then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,         "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015 "
				+ " then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "
				+ "else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,        "
				+ " COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <      "
				+ " (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then "
				+ "fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "
				+ "else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,        "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <      "
				+ " (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then  "
				+ "fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
				+ "else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,    "
				+ " case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00)  -  "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then  "
				+ "(COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +       "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))         "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then  "
				+ "(COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  "
				+ " then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "
				+ "else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))         "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
				+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then  "
				+ "fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))         "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then  "
				+ "(COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then "
				+ "fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +       "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then  "
				+ "fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))        "
				+ "  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then  "
				+ "(COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015   "
				+ "then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month',  "
				+ "case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))    "
				+ "    - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) *  "
				+ "cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) *  "
				+ "(select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN  "
				+ "GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end) "
				+ "  -(case when round(FLOOR(((extract(year from cast('2015-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  "
				+ " (extract(month from cast('2015-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('2015-12-31' as Date))- "
				+ "extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   "
				+ " WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA     "
				+ "where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then   "
				+ "round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +      "
				+ "COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)   "
				+ "   +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))* "
				+ "(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) =  "
				+ "2015  then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,  "
				+ "FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) *  "
				+ "12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))     "
				+ " else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else  "
				+ "COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))     "
				+ "  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+  "
				+ "COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)     "
				+ " +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))* "
				+ "(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +     "
				+ " (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))    "
				+ "  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',  "
				+ "case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +   "
				+ "   (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))     "
				+ " else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end  "
				+ "as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "
				+ "  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))* "
				+ "(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   "
				+ "case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT  "
				+ "(YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      < 12  "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month',  "
				+ "case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
				+ "else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))         "
				+ "  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "
				+ " (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      + "
				+ "COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))* "
				+ "(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +    "
				+ "  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month',  "
				+ "case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))   <  "
				+ "12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2015 then fecha_geedp else '2015-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2015  then fecha_geedp else '2015-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('2015-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('2015-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('2015-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= 7 GROUP BY IDE_SRIMR),0),2) else 0 end)   "
				+ "as numeric),2)  else 0.00 end as basImp, '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      "
				+ "COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   "
				+ "round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,   "
				+ "COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,   "
				+ "round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   "
				+ "then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 "
				+ "then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   "
				+ "COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    "
				+ "round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   "
				+ "then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp      "
				+ "left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =    "
				+ "(select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 7   "
				+ "and DETALLE_SRDED like 'ALIMENTACION')     left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     "
				+ "and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    "
				+ "left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 7  and DETALLE_SRDED like 'SALUD')    "
				+ "left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      "
				+ "and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    "
				+ "left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 7  and DETALLE_SRDED like 'VIVIENDA')     "
				+ "left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =    "
				+ "(select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = 7  and DETALLE_SRDED like 'EDUCACION')    "
				+ "left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    "
				+ "left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 7  and DETALLE_SRDED like 'VESTIMENTA')   "
				+ "left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      "
				+ "left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp      "
				+ "left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,   "
				+ "ide_geedp, STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from "
				+ "GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   "
				+ "left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded    "
				+ "where  activo_geded = true  "
				+ " and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )=2015)   "
				+ "group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, ide_geedp)   as depar on depar.ide_gtemp= emp.ide_gtemp    left join    "
				+ "(SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol    "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))    "
				+ "and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     "
				+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    "
				+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "
				+ "(select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte    "
				+ "FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
				+ "where  IDE_NRROL in    "
				+ "(select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "
				+ "(select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos    FROM NRH_DETALLE_ROL detarol   "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO    in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP    "
			//	+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero    "
			//	+ "FROM NRH_DETALLE_ROL detarol     "
				
		//		dcd
				
				
				
				+"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero    "
				+ "FROM NRH_DETALLE_ROL detarol     "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
				+ "where  IDE_NRROL    in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "
				+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=7))     "
				+ "and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste  "+     
				//+ ""
				//+ ""
				//+ "FROM NRH_DETALLE_ROL detarol   "+  
				//"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "+ 
				//"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+  
				//"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL  "+
				//"  in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))   "+
				//"  and detrubro.IDE_NRRUB in (334,368,367) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP   "+ 
				//"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste     "+
				"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+  
				"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
				"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
				+ "where  IDE_NRROL in "+
				"  (select IDE_NRROL from NRH_ROL where "
				+ "IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
				+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107, "
				+ "108,109,110,111)) "+    
				//+ "IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  "+
				"  and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP    "+
				//"  and detrubro.IDE_NRRUB in (369) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP    "+
				"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuarto   "
				+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
				+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
				+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106, "
				+ "107,108,109,110,111)) "
				+ " and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP, "
				+ "SUM(DETAROL.VALOR_NRDRO "
				+ "AS decimoCuartoAjuste "
				
				//+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				//+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
				//+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
				///+ "where  IDE_NRROL    in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))     "
				//+ "and detrubro.IDE_NRRUB in (334,368,367) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP     "
				//+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste       "
				//+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "
				//+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				//+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
				//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))   "
			//	+ " and detrubro.IDE_NRRUB in (369) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      "
		//		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuarto    
				+" FROM NRH_DETALLE_ROL detarol    "
				+" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol      "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
				+ " where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in    (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))   "
				+ "and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP   "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess      "
				+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  7))      "
				+ "and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "where depar.ide_gttem not like '%3%'      "
				+ "order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP, "
				+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y ");*/

		
		
		
		tab_seleccion.setSql("select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as  fraccion_basica_srdir    "
				+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr = 12 and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*    "
				+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     "
				+ "FROM public.sri_detalle_impuesto_renta     "
				+ "where  ide_srimr = 12 and basimp between  "
				+ "fraccion_basica_srdir and exceso_hasta_srdir)) +  "
				+ "(SELECT  imp_fraccion_srdir FROM public.sri_detalle_impuesto_renta   where  ide_srimr = 12 and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    as impuestoCausado    "
				+ "from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc,  "
				+ "ide_geedp,  emp.ide_gtemp,    "
				+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,   documento_identidad_gtemp as idRet,   "
				+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab, "
				+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab, '001' as estab,      '01'   as residenciaTrab,   '593' as pasisResidencia,      'NA' as aplicaConvenio,  "
				+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap, case when   round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap, "
				+ "'N' as tipidDiscap,   '999' as idDiscap,      COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,    COALESCE(sobresueldos,0.00) as sobSuelComRemu,      '0' as partUtil,   '0' as intGrabGen,  "
				+ "'0' as impRentEmpl,   COALESCE(DecimoTercero,0.00) as decimoTercer,   case   "
				+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>380.00 then 386.00 "
				+ "else "
				+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end "
				+ "as decimoCuarto,    "
				+ "COALESCE(fondo, 0.00) as  fondoReserva,    "
				+ "'0' as salarioDigno,   '0' as otrosIngRenGrav,   COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl, "
				+ "'1' as sisSalNet,   COALESCE(aporteIess, 0.00) as apoPerIess,    '0.00' as aporPerIessConOtrosEmpls,      "
				+ "case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   "
				+ "WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    "
				+ "from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    "
				+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
				+ "case when round(FLOOR(((extract(year from cast('2018-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA   where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,   "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "
				+ "(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))  "
				+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp   else '2018-01-1' end::date)) < 12 then  "
				+ "(DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12   "
				+ "+ (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))   "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda,  "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))   "
				+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ " - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12  "
				+ " + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) < 12   "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))       "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,  "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00) "
				+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
				+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +       "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
				+ "else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,         "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+ "
				+ "COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +       "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12    "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))     "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,  "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00) "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00) +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,   "
				+ "case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00)  -    "
				+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12   "
				+ "+(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))   "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+   "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
				+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
				+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12    "
				+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12   "
				+ "+ (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))    "
				+ " -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)   "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
				+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
				+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ "- DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
				+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))    "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),     "
				+ "COALESCE(ali.valor_deducible_srdee,0.00))-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)  "
				+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   "
				+ "COALESCE(vest.valor_deducible_srdee,0.00))        - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then    "
				+ "round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)   "
				+ "* (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA   "
				+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)   -(case when round(FLOOR(((extract(year from cast('2018-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372   "
				+ "+   (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65   "
				+ "then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA   "
				+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   "
				+ "COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+   "
				+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),  "
				+ " COALESCE(vivienda.valor_deducible_srdee,0.00))       -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+   "
				+ "COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date)   "
				+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "
				+ "else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))       "
				+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)   "
				+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
				+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
				+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   "
				+ "COALESCE(educa.valor_deducible_srdee,0.00))           -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)  "
				+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date))   < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
				+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  "
				+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   "
				+ "COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)"
				+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
				+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
				+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
				+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
				+ "else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40  "
				+ " then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) *  "
				+ " (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    "
				+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('2018-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372   "
				+ " +  (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65   "
				+ "then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)   "
				+ "from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, "
				+ " '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      "
				+ " COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,    "
				+ " COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,    "
				+ " round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0    "
				+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   "
				+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    "
				+ " round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0    "
				+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp      "
				+ " left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =     "
				+ " (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12   and DETALLE_SRDED like 'ALIMENTACION')  "
				+ "left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri     "
				+ "left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'SALUD')   "
				+ "left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      and vivienda.IDE_SRDED =  "
				+ "  (select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'VIVIENDA')     "
				+ "left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =     "
				+ "(select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = 12  and DETALLE_SRDED like 'EDUCACION')    "
				+ "left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =  "
				+ "(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'VESTIMENTA')   "
				+ "left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp     "
				+ "left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp,    "
				+ "STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   "
				+ "left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded    "
				+ "where  activo_geded = true and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )=2018)   "
				+ "group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp)   as depar on depar.ide_gtemp= emp.ide_gtemp     "
				+ "left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol     "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))    "
				+ "and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     "
				+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol     "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "
				+ "(select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))     "
				+ "and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte     "
				+ "FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11)) and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP)   "
				+ "solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     "
				+ " left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))  "
				+ "and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP      "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos    "
				+ "FROM NRH_DETALLE_ROL detarol   left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO     "
				+ "in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))      and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero     "
				+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
				+ "where  IDE_NRROL    in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in    "
				+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))     and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP     "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste       "
				+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
				+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
				+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111)) "
				+ "and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,   "
				+ "SUM(DETAROL.VALOR_NRDRO)  "
				+ "AS DecimoCuarto  "
				+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
				+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
				+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))    "
				+ "and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,   "
				+ "SUM(DETAROL.VALOR_NRDRO)  "
				+ "AS decimoCuartoAjuste     "
				+ "FROM NRH_DETALLE_ROL detarol    "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))  "
				+ "and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol      "
				+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
				+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in    "
				+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))   and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP    "
				+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess      FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
				+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
				+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))      "
				+ "and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      where depar.ide_gttem not like '%3%'      "
				+ "order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,      COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y");
		
		 tab_seleccion.imprimirSql();
		tab_seleccion.setCampoPrimaria("ide_geedp");
		tab_seleccion.setLectura(true);
		tab_seleccion.setTipoSeleccion(true);
		tab_seleccion.setNumeroTabla(2);
		tab_seleccion.getColumna("nuRuc").setVisible(false);
		tab_seleccion.getColumna("tipIdRet").setVisible(false);
		tab_seleccion.getColumna("estab").setVisible(false);
		tab_seleccion.getColumna("residenciaTrab").setVisible(false);
		tab_seleccion.getColumna("aplicaConvenio").setVisible(false);
		tab_seleccion.getColumna("pasisResidencia").setVisible(false);

		tab_seleccion.getColumna("tipoTrabajoDiscap").setVisible(false);
		tab_seleccion.getColumna("porcentajeDiscap").setVisible(false);
		tab_seleccion.getColumna("tipidDiscap").setVisible(false);
		tab_seleccion.getColumna("idDiscap").setVisible(false);
		tab_seleccion.getColumna("impRentEmpl").setVisible(false);
		
		tab_seleccion.getColumna("salarioDigno").setVisible(false);
		tab_seleccion.getColumna("sisSalNet").setVisible(false);
		tab_seleccion.getColumna("impRentEmpl").setVisible(false);
		
		tab_seleccion.getColumna("valor_liquidacion_geedp").setVisible(false);
		tab_seleccion.getColumna("baseSolidaria").setVisible(false);
		tab_seleccion.getColumna("solidario").setVisible(false);
		tab_seleccion.getColumna("impuestoSolidario").setVisible(false);
		
		
		tab_seleccion.getColumna("numMeses").setVisible(false);
		tab_seleccion.getColumna("promedio").setVisible(false);


		
		tab_seleccion.getColumna("ide_gtemp").setLongitud(15);
		tab_seleccion.getColumna("ide_gtemp").setFiltro(true);
		tab_seleccion.getColumna("ide_gtemp").alinearCentro();
		tab_seleccion.getColumna("ide_gtemp").setNombreVisual("COD EMPLEADO");
		
		tab_seleccion.getColumna("idRet").setLongitud(15);
		tab_seleccion.getColumna("idRet").setFiltro(true);
		tab_seleccion.getColumna("idRet").alinearCentro();
		tab_seleccion.getColumna("idRet").setNombreVisual("IDENTIFICACION");


		tab_seleccion.getColumna("apellidoTrab").setLongitud(15);
		tab_seleccion.getColumna("apellidoTrab").setFiltro(true);

		tab_seleccion.getColumna("apellidoTrab").alinearCentro();
		tab_seleccion.getColumna("apellidoTrab").setNombreVisual("APELLIDO");

		tab_seleccion.getColumna("nombreTrab").setLongitud(15);
		tab_seleccion.getColumna("nombreTrab").setFiltro(true);

		tab_seleccion.getColumna("nombreTrab").alinearCentro();
		tab_seleccion.getColumna("nombreTrab").setNombreVisual("NOMBRE");


		tab_seleccion.getColumna("suelSal").setLongitud(15);
		tab_seleccion.getColumna("suelSal").alinearCentro();
		tab_seleccion.getColumna("suelSal").setNombreVisual("301");

		tab_seleccion.getColumna("sobSuelComRemu").setLongitud(10);
		tab_seleccion.getColumna("sobSuelComRemu").alinearCentro();
		tab_seleccion.getColumna("sobSuelComRemu").setNombreVisual("SOBRESUELDOS, COMISIONES, BONOS Y OTROS INGRESOS GRAVADOS 303");

		tab_seleccion.getColumna("partUtil").setLongitud(15);
		tab_seleccion.getColumna("partUtil").alinearCentro();
		tab_seleccion.getColumna("partUtil").setNombreVisual("PARTICIPACIÓN UTILIDADES 305");


		tab_seleccion.getColumna("intGrabGen").setLongitud(19);
		tab_seleccion.getColumna("intGrabGen").alinearCentro();
		tab_seleccion.getColumna("intGrabGen").setNombreVisual("INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES 307");


		tab_seleccion.getColumna("decimoTercer").setLongitud(15);
		tab_seleccion.getColumna("decimoTercer").alinearCentro();
		tab_seleccion.getColumna("decimoTercer").setNombreVisual("DÉCIMO TERCER SUELDO 311");

		tab_seleccion.getColumna("decimoCuarto").setLongitud(15);
		tab_seleccion.getColumna("decimoCuarto").alinearCentro();
		tab_seleccion.getColumna("decimoCuarto").setNombreVisual("DÉCIMO CUARTO SUELDO 313");
		tab_seleccion.getColumna("fondoReserva").setLongitud(10);
		tab_seleccion.getColumna("fondoReserva").alinearCentro();
		tab_seleccion.getColumna("fondoReserva").setNombreVisual("FONDO DE RESERVA 315");
		
		tab_seleccion.getColumna("otrosIngRenGrav").setLongitud(15);
		tab_seleccion.getColumna("otrosIngRenGrav").alinearCentro();
		tab_seleccion.getColumna("otrosIngRenGrav").setNombreVisual("OTROS INGRESOS EN RELACIÓN DE DEPENDENCIA QUE NO CONSTITUYEN RENTA GRAVADA 317");
		
		
		tab_seleccion.getColumna("apoPerIess").setLongitud(15);
		tab_seleccion.getColumna("apoPerIess").alinearCentro();
		tab_seleccion.getColumna("apoPerIess").setNombreVisual(" (-) APORTE PERSONAL IESS CON ESTE EMPLEADOR 351");
		
		
		
		tab_seleccion.getColumna("aporPerIessConOtrosEmpls").setLongitud(15);
		tab_seleccion.getColumna("aporPerIessConOtrosEmpls").alinearCentro();
		tab_seleccion.getColumna("aporPerIessConOtrosEmpls").setNombreVisual("(-) APORTE PERSONAL IESS CON OTROS EMPLEADORES (únicamente pagado por el trabajador) 353");
		
		tab_seleccion.getColumna("vivienda").setLongitud(15);
		tab_seleccion.getColumna("vivienda").alinearCentro();
		tab_seleccion.getColumna("vivienda").setNombreVisual("VIVIENDA 361");
		
		tab_seleccion.getColumna("salud").setLongitud(15);
		tab_seleccion.getColumna("salud").alinearCentro();
		tab_seleccion.getColumna("salud").setNombreVisual("SALUD 363");
		tab_seleccion.getColumna("educacion").setLongitud(15);
		tab_seleccion.getColumna("educacion").alinearCentro();
		tab_seleccion.getColumna("educacion").setNombreVisual("EDUCACIÓN 365");
		tab_seleccion.getColumna("alimentacion").setLongitud(15);
		tab_seleccion.getColumna("alimentacion").alinearCentro();
		tab_seleccion.getColumna("alimentacion").setNombreVisual("ALIMENTACIÓN 367");
		
		
		tab_seleccion.getColumna("vestimenta").setLongitud(10);
		tab_seleccion.getColumna("vestimenta").alinearCentro();
		tab_seleccion.getColumna("vestimenta").setNombreVisual("VESTIMENTA 369");
		
		tab_seleccion.getColumna("descuentoDiscapacidad").setLongitud(15);
		tab_seleccion.getColumna("descuentoDiscapacidad").alinearCentro();
		tab_seleccion.getColumna("descuentoDiscapacidad").setNombreVisual("(-) EXONERACIÓN POR DISCAPACIDAD 371");
		
		tab_seleccion.getColumna("descuentoTercera").setLongitud(15);
		tab_seleccion.getColumna("descuentoTercera").alinearCentro();
		tab_seleccion.getColumna("descuentoTercera").setNombreVisual("(-) EXONERACIÓN POR TERCERA EDAD 373");
		
		tab_seleccion.getColumna("basimp").setLongitud(15);
		tab_seleccion.getColumna("basimp").alinearCentro();
		tab_seleccion.getColumna("basimp").setNombreVisual("BASE IMPONIBLE 399");
		
		
		
		tab_seleccion.getColumna("impuestoCausado").setLongitud(15);
		tab_seleccion.getColumna("impuestoCausado").alinearCentro();
		tab_seleccion.getColumna("impuestoCausado").setNombreVisual("IMPUESTO A LA RENTA CAUSADO  401");
		
		
		tab_seleccion.getColumna("valRetAsuOtrosEmpls").setLongitud(20);
		tab_seleccion.getColumna("valRetAsuOtrosEmpls").alinearCentro();
		tab_seleccion.getColumna("valRetAsuOtrosEmpls").setNombreVisual("VALOR DEL IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPLEADORES DURANTE EL PERÍODO DECLARADO 403");
		
		tab_seleccion.getColumna("valImpAsuEsteEmpl").setLongitud(15);
		tab_seleccion.getColumna("valImpAsuEsteEmpl").alinearCentro();
		tab_seleccion.getColumna("valImpAsuEsteEmpl").setNombreVisual(" VALOR DEL IMPUESTO ASUMIDO POR ESTE EMPLEADOR 405");
		
		
		tab_seleccion.getColumna("valRet").setLongitud(10);
		tab_seleccion.getColumna("valRet").alinearCentro();
		tab_seleccion.getColumna("valRet").setNombreVisual("VALOR DEL IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR 407");
		
		
		tab_seleccion.getColumna("ingGravConEsteEmpl").setLongitud(15);
		tab_seleccion.getColumna("ingGravConEsteEmpl").alinearCentro();
		tab_seleccion.getColumna("ingGravConEsteEmpl").setNombreVisual("INGRESOS GRAVADOS 349");
		tab_seleccion.setRows(18);
		tab_seleccion.dibujar();
		tab_seleccion.setHeader("REPORTE RESUMEN FORMULARIO 107 POR EMPLEADO ");
		

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.getChildren().add(boc_seleccion_inversa);
		pat_panel2.setPanelTabla(tab_seleccion);

		Division div_division = new Division();
		//div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "15%", "H");
		agregarComponente(div_division);


		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		//bar_botones.agregarBoton(bot_limpiar);
		
		
		
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		agregarComponente(rep_reporte);
	}

	/**
	 * Genera el Anexo RDEP del periodo seleccionado
	 */
		
	public void generarRDEP(String SRIMR, String IDE_GTEMP){
		
	/*
		File[] str_archivo;
		try {
	
			str_archivo = ser_sri.generarAnexoRDEP(SRIMR,IDE_GTEMP);
			   utilitario.addUpdate("tab_seleccion");
		if(str_archivo!=null){
			utilitario.crearArchivoZIP(str_archivo, "Carpeta.zip");

		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	*/
	}
	
	
	
	public void seleccionarEmpleado(){
		tab_seleccion.setLectura(false);
		
		if (!tab_seleccion.getFilasSeleccionadas().isEmpty() || !tab_seleccion.getFilasSeleccionadas().equals("") || !tab_seleccion.getFilasSeleccionadas().equals("")) {
		tab_seleccion.getFilasSeleccionadas().length();
		//	generarRDEP(tab_tabla1.getValor("IDE_SRIMR"), tab_seleccion.getFilasSeleccionadas().toString());
					
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un empleado", "");
		}
	}

	
	public void aceptarEmpleado(){
    if (!set_empleado_rdep.getSeleccionados().isEmpty() || !set_empleado_rdep.getSeleccionados().equals(null) || !set_empleado_rdep.getSeleccionados().equals("")) {
//        	   generarRDEP(set_empleado_rdep.getSeleccionados());
   			   set_empleado_rdep.cerrar();
		}else {
			utilitario.agregarMensajeInfo("Debe seleccionar un empleado", "");
		}
	}
	
	

	public void exportarRDEPExcel(){
		
		
	}
	

	@Override
	public void aceptarReporte() {
		
		try {
			if(!tab_tabla1.isEmpty()){			
				if (rep_reporte.getReporteSelecionado().equals("Reporte Formulario 107")){
					if(rep_reporte.isVisible()){
						String str_seleccionados= tab_seleccion.getFilasSeleccionadas().toString();
						tab_seleccion.getFilasSeleccionadas().length();
						//System.out.printf("Esto me selecciona:"+str_seleccionados);
						if(str_seleccionados==null || str_seleccionados.isEmpty()){
							rep_reporte.cerrar();
							utilitario.agregarMensajeInfo("Debe seleccionar al menos un empleado", "");
							return;
						}	
						
						
						String IDE_SRIMR=tab_tabla1.getValor("IDE_SRIMR");
						TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
						String  fecha_inicio = tab_imp_renta.getValor("fecha_inicio_srimr");
						String   fecha_fin =tab_imp_renta.getValor("fecha_fin_srimr");
						String anioHasta =utilitario.getAnio(fecha_fin)+"";
						TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anioHasta+"'");
						String str_ide_anio=tab_anio.getValor("IDE_GEANI");
						
						String fechaActual=utilitario.getFechaActual();
						int anio=utilitario.getAnio(fechaActual);
						int mes= utilitario.getMes(fechaActual);
						int dia =utilitario.getDia(fechaActual);		
						p_parametros = new HashMap();
						rep_reporte.cerrar();
						TablaGenerica tabAcciones=utilitario.consultar("select ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_geedp="+tab_seleccion.getFilasSeleccionadas());
						TablaGenerica tabAccionesPersonal=utilitario.consultar("select ide_gtemp,ide_geedp from  gen_empleados_departamento_par where ide_gtemp="+tabAcciones.getValor("IDE_GTEMP"));
						StringBuilder str = new StringBuilder();
						int contador=0;
						if (tabAccionesPersonal.getTotalFilas()==0) {
							utilitario.agregarMensajeInfo("No contiene acciones el empleado", "Contactese con el administrador");
							return;
						}
						
						for (int i = 0; i < tabAccionesPersonal.getTotalFilas(); i++) {
							str.append(tabAccionesPersonal.getValor(i,"IDE_GEEDP"));
							if (tabAccionesPersonal.getTotalFilas()==1) {
				   			}else if (contador<=tabAccionesPersonal.getTotalFilas()) {
							contador++;
								if(contador<(tabAccionesPersonal.getTotalFilas())){
								str.append(",");
				                    //System.out.println("str_ide:  "+str_ide);
				   					}
							}
						}
						
						p_parametros.put("ide_geedp",str.toString());
						p_parametros.put("IDE_ASMIR",IDE_SRIMR);
						p_parametros.put("anioHasta",anioHasta);
						p_parametros.put("str_ide_anio",Integer.parseInt(str_ide_anio));
						p_parametros.put("anio",anio);
						p_parametros.put("mes",mes);
						p_parametros.put("dia",dia);
						p_parametros.put("IDE_NRROL",roles.toString());
						p_parametros.put("sbv",sueldo);
						if ((sueldo-5)<0) {
							p_parametros.put("sbvRestado",(double)(0.0));
						}else {
							p_parametros.put("sbvRestado",(double)(sueldo-5));	
						}
						System.out.println(IDE_SRIMR);
						System.out.println(anioHasta);
						System.out.println(str_ide_anio);
						System.out.println(anio);
						System.out.println(mes);
						System.out.println(dia);
						System.out.println(roles);
						System.out.println(sueldo);
						
						sef_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
						sef_reporte.dibujar();
					}
				}
			}
			else{
				utilitario.agregarMensajeInfo("Debe configurar un periodo de impuesto a la renta", "");
			}
		} catch (NumberFormatException e) {

		}		
	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}


	@Override
	public void insertar() {
	}

	@Override
	public void guardar() {

	}

	@Override
	public void eliminar() {
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}



	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
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

	public SeleccionCalendario getSec_fecha_entrega() {
		return sec_fecha_entrega;
	}

	public void setSec_fecha_entrega(SeleccionCalendario sec_fecha_entrega) {
		this.sec_fecha_entrega = sec_fecha_entrega;
	}

	public SeleccionTabla getSet_empleado_rdep() {
		return set_empleado_rdep;
	}

	public void setSet_empleado_rdep(SeleccionTabla set_empleado_rdep) {
		this.set_empleado_rdep = set_empleado_rdep;
	}    

	public void filtrarEmpleadoRED(SelectEvent evt){

		aut_empleado.onSelect(evt);
		if(aut_empleado.getValor()!=null){		
			empleado=aut_empleado.getValor();
		}
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Tabla getTab_seleccion() {
		return tab_seleccion;
	}

	public void setTab_seleccion(Tabla tab_seleccion) {
		this.tab_seleccion = tab_seleccion;
	}
	
	

	
long ide_inicial=0;
public void seleccionarTodas() {
	tab_seleccion.setSeleccionados(null);
    Fila seleccionados[] = new Fila[tab_seleccion.getTotalFilas()];
    for (int i = 0; i < tab_seleccion.getFilas().size(); i++) {
        seleccionados[i] = tab_seleccion.getFilas().get(i);
    }
    tab_seleccion.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_seleccion.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_seleccion.getSeleccionados().length == tab_seleccion.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_seleccion.getTotalFilas() - tab_seleccion.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_seleccion.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_seleccion.getSeleccionados().length; j++) {
                    if (tab_seleccion.getSeleccionados()[j].equals(tab_seleccion.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_seleccion.getFilas().get(i);
                    cont++;
                }
            }
            tab_seleccion.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_seleccion.setSeleccionados(null);
    }
	
	
  


public void getTablaReporte(String IDE_SRIMR, String anioHasta, String str_ide_anio ){
	StringBuilder 	str_ide= new StringBuilder();
	StringBuilder 	str_ide_actual= new StringBuilder();
	StringBuilder 	str_ide_decimotercer= new StringBuilder();
//	utilitario.getConexion().ejecutarSql("delete from sri_formulario107 where generado_liquidacion_srfor=false and ide_srimr="+IDE_SRIMR);

	int valorEmp=0;
	int valorEmpActual=0;
	int anio=0,anioResultado=0;
	double valor_sbv_anterior=0.0,valor_sbv_actual=0.0;
	sueldo=0.0;
	TablaGenerica tabAnio =utilitario.consultar("Select * from gen_anio where ide_geani="+str_ide_anio);
	roles="";
	anio=Integer.parseInt(tabAnio.getValor("DETALLE_GEANI"));
	anioResultado=anio-1;

	
	TablaGenerica tabRolDecimoCuarto1=null;
	TablaGenerica tabAnioNuevo =utilitario.consultar("Select * from gen_anio where detalle_geani like '%"+anioResultado+"%'");
	if (tabAnioNuevo.getTotalFilas()>0) {
		
		tabRolDecimoCuarto1=utilitario.consultar("select * from nrh_rol  rol "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
				+ "where gprol.ide_gemes in(8,9,10,11,12) and gprol.ide_geani="+tabAnioNuevo.getValor("IDE_GEANI")+" "
				+ "and rol.ide_nrdtn in(2,4)  "
				+ "order by rol.ide_nrrol asc ");
		
if (tabRolDecimoCuarto1.getTotalFilas()>0) {
	for (int i = 0; i < tabRolDecimoCuarto1.getTotalFilas(); i++) 
 		{
 		//Voy anidando los ides de la accion
  	    str_ide.append(tabRolDecimoCuarto1.getValor(i, "IDE_NRROL"));
       // valor++;
        if (tabRolDecimoCuarto1.getTotalFilas()==1) {
		}else if (valorEmp<=tabRolDecimoCuarto1.getTotalFilas()) {
			valorEmp++;
				if(valorEmp<(tabRolDecimoCuarto1.getTotalFilas())){
            str_ide.append(",");
            //System.out.println("str_ide:  "+str_ide);
				}
		}

		 }
	valor_sbv_anterior=consultarSueloXAnio(Integer.parseInt(tabAnioNuevo.getValor("IDE_GEANI")), "12");

		}else{
			tabRolDecimoCuarto1=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in(8,9,10,11,12) and gprol.ide_geani=-1 and rol.ide_nrdtn in(2,4)  "
			+ "order by rol.ide_nrrol asc ");
	str_ide.append("-1");
	valor_sbv_anterior=0;
		}



	}else {
      // utilitario.agregarMensajeInfo("No existe anio seleccionado", "Seleccione un anio válido");
      // return;
	}


	
	

	
	
	
	
	
	TablaGenerica tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in(1,2,3,4,5,6,7) and gprol.ide_geani="+str_ide_anio+"  "
				+ "and rol.ide_nrdtn in(2,4)   "
			+ "order by rol.ide_nrrol asc");
	
	
	
	
	if (tabRolDecimoCuarto2.getTotalFilas()>0) {
		for (int i = 0; i < tabRolDecimoCuarto2.getTotalFilas(); i++) 
	 		{
	 		//Voy anidando los ides de la accion
	  	    str_ide_actual.append(tabRolDecimoCuarto2.getValor(i, "IDE_NRROL"));
	  	    //valorEmpActual++;
	        if (tabRolDecimoCuarto2.getTotalFilas()==1) {
			}else if (valorEmpActual<=tabRolDecimoCuarto2.getTotalFilas()) {
				valorEmpActual++;
					if(valorEmpActual<(tabRolDecimoCuarto2.getTotalFilas())){
	            str_ide_actual.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }

		valor_sbv_actual=consultarSueloXAnio(Integer.parseInt(str_ide_anio.toString()), "1");

		
			}else{
				tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
				+ "where gprol.ide_gemes in(1,2,3,4,5,6,7) and gprol.ide_geani=-1 and rol.ide_nrdtn in(2,4)  "
				+ "order by rol.ide_nrrol asc ");
		str_ide_actual.append("-1");
		valor_sbv_actual=0.0;
		}

	
	boolean uno=false,dos=false;
	
	
	if (str_ide.length()==0) {
		uno=false;
	}else {
		uno=true;
	}
	
	if (str_ide_actual.length()==0) {
		dos=false;
	}else {
		dos=true;
	}	
if (uno==true && dos==true) {
	roles=str_ide.toString()+","+str_ide_actual;
	sueldo=valor_sbv_actual;
}
if (uno==true && dos==false) {
	roles=str_ide.toString();
	sueldo=valor_sbv_anterior;
}
	
if (uno==false && dos==true) {
	roles=str_ide_actual.toString();
	sueldo=valor_sbv_actual;
}
		
if (uno==false && dos==false) {
	roles="-1";
	sueldo=0;
}	

TablaGenerica tabRolDecimoTercer=utilitario.consultar("select * from nrh_rol  rol "
		+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
		+ "where gprol.ide_gemes in(1,2,3,4,5,6,7,8,9,10,11,12) and gprol.ide_geani="+str_ide_anio+"  "
			+ "and rol.ide_nrdtn in(2,4)   "
		+ "order by rol.ide_nrrol asc");
	
valorEmp=0;
if (tabRolDecimoTercer.getTotalFilas()>0) {
	for (int i = 0; i < tabRolDecimoTercer.getTotalFilas(); i++) 
 		{
 		//Voy anidando los ides de la accion
		str_ide_decimotercer.append(tabRolDecimoTercer.getValor(i, "IDE_NRROL"));
       // valor++;
        if (tabRolDecimoTercer.getTotalFilas()==1) {
		}else if (valorEmp<=tabRolDecimoTercer.getTotalFilas()) {
			valorEmp++;
				if(valorEmp<(tabRolDecimoTercer.getTotalFilas())){
					str_ide_decimotercer.append(",");
            //System.out.println("str_ide:  "+str_ide);
				}
		}

		 }

}


	/*
	 String sql="select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as "
	 		+ "fraccion_basica_srdir "
	 		+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr =  " + IDE_SRIMR + "  and "
	 		+ "basimp between fraccion_basica_srdir and exceso_hasta_srdir))* "
	 		+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100 "
	 		+ "FROM public.sri_detalle_impuesto_renta "
	 		+ "where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir "
	 		+ "FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    as impuestoCausado  "
	 		+ "from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc, ide_geedp, emp.ide_gtemp, "
	 		//+ "(select nom_empr from sis_empresa limit 1) as nomEmpresa,  "
	 		+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,  "
	 		+ "documento_identidad_gtemp as idRet,  "
	 		+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,   "
	 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab,  "
	 		+ "'001' as estab,  "
	 		+ "'01'   as residenciaTrab,  "
	 		+ "'593' as pasisResidencia,  "
	 		+ "'NA' as aplicaConvenio, "
	 		+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap,  "
	 		+ "case when   round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap,  "
	 		+ "'N' as tipidDiscap,  "
	 		+ "'999' as idDiscap,  "
	 		+ "COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,  "
	 		+ "COALESCE(sobresueldos,0.00) as sobSuelComRemu,  "
	 		+ "'0' as partUtil,  "
	 		+ "'0' as intGrabGen,  "
	 		+ "'0' as impRentEmpl,  "
	 		+ "COALESCE(DecimoTercero,0.00) as decimoTercer,   case  "
	 		+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>"+(sueldo-5)+" then "+sueldo+"  "
	 		+ "else  "
	 		+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end  "
	 		+ " as decimoCuarto, "
	 		+ "COALESCE(fondo, 0.00) as  fondoReserva,  "
	 		+ "'0' as salarioDigno,  "
	 		+ "'0' as otrosIngRenGrav,  "
	 		+ "COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl,  "
	 		+ "'1' as sisSalNet,  "
	 		+ "COALESCE(aporteIess, 0.00) as apoPerIess,  "
	 		+ "'0.00' as aporPerIessConOtrosEmpls,  "
	 		+ "case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) "
	 		+ "from SRI_DETALLE_IMPUESTO_RENTA  "
	 		+ "where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
	 		+ "case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)    "
	 		+ "from SRI_DETALLE_IMPUESTO_RENTA   "
	 		+ "where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp   else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda, "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,  "
	 		+ "case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00)  - COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))          -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +  "
	 		+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))        - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)   -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  "
	 		+ "(extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +      COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))       -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))           -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))   < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,   COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,   round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp      "
	 		+ " left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =    (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "   and DETALLE_SRDED like 'ALIMENTACION')     "
	 		+ " left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'SALUD')    "
	 		+ " left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VIVIENDA')     "
	 		+ " left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =    (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'EDUCACION')    "
	 		+ " left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VESTIMENTA')   left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      "
	 		+ " left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp      left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp,   STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded   "
	 		+ "where  activo_geded = true   "
	 		
	 		//+ "and ide_geedp in ($P!{ide_geedp}) "
	 		+ "and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+")   "
	 		+ "group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, ide_geedp)   as "
	 		+ "depar on depar.ide_gtemp= emp.ide_gtemp  "
	 		+ "left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol    "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))    "
	 		+ "and detrubro.IDE_NRRUB in (347,345) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     "
	 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol     "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
	 		+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "
	 		+ "and detrubro.IDE_NRRUB in (288,344) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      "
	 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte    "
	 		+ "FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
	 		+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "
	 		+ "and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0 AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      "
	 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
	 		+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "
	 		+ "and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos  "
	 		+ "FROM NRH_DETALLE_ROL detarol   left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO    in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))  "
	 		+ "and detrubro.IDE_NRRUB in (27,17,354,331,336,313) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero  "
	 		+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+")"
	 		//+ "select IDE_NRROL from NRH_ROL where IDE_GEPRO in   "
	 		//+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))     "
	 		+ "and detrubro.IDE_NRRUB in (334,125) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "     left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste  "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))   "
	 		+ "where IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))   "
	 		+ "and detrubro.IDE_NRRUB in (333) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE)  group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
	 		+ "SUM(DETAROL.VALOR_NRDRO)  "
	 		+ "AS DecimoCuarto  "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))  "
	 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+roles+"))  "	
	 		
	 		+ "and detrubro.IDE_NRRUB in (121) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
	 		+ "SUM(DETAROL.VALOR_NRDRO)  "
	 		+ "AS decimoCuartoAjuste  "
	 		+ "FROM NRH_DETALLE_ROL detarol  "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
	 		+ "and detrubro.IDE_NRRUB in (333,359) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP   "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol   "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "
	 		+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
	 		+ "and detrubro.IDE_NRRUB in (42) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP   "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess   "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   " 
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))    "
	 		+ "and detrubro.IDE_NRRUB in (44) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro IS NULL OR DETAROL.es_liquidacion_nrdro=FALSE) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      where depar.ide_gttem not like '%3%'      order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,      "
	 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y ";
	

	 System.out.println(sql);*/
	 
	 
	 
	   String sql="select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as "
	 		+ "fraccion_basica_srdir "
	 		+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr =  " + IDE_SRIMR + "  and "
	 		+ "basimp between fraccion_basica_srdir and exceso_hasta_srdir))* "
	 		+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100 "
	 		+ "FROM public.sri_detalle_impuesto_renta "
	 		+ "where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir "
	 		+ "FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    as impuestoCausado  "
	 		+ "from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc, ide_geedp, emp.ide_gtemp, "
	 		//+ "(select nom_empr from sis_empresa limit 1) as nomEmpresa,  "
	 		+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,  "
	 		+ "documento_identidad_gtemp as idRet,  "
	 		+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,   "
	 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab,  "
	 		+ "'001' as estab,  "
	 		+ "'01'   as residenciaTrab,  "
	 		+ "'593' as pasisResidencia,  "
	 		+ "'NA' as aplicaConvenio, "
	 		+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap,  "
	 		+ "case when   round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap,  "
	 		+ "'N' as tipidDiscap,  "
	 		+ "'999' as idDiscap,  "
	 		+ "COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,  "
	 		+ "COALESCE(sobresueldos,0.00) as sobSuelComRemu,  "
	 		+ "'0' as partUtil,  "
	 		+ "'0' as intGrabGen,  "
	 		+ "'0' as impRentEmpl,  "
	 		+ "COALESCE(DecimoTercero,0.00) as decimoTercer,   case  "
	 		+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>"+(sueldo-5)+" then "+sueldo+"  "
	 		+ "else  "
	 		+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end  "
	 		+ " as decimoCuarto, "
	 		+ "COALESCE(fondo, 0.00) as  fondoReserva,  "
	 		+ "'0' as salarioDigno,  "
	 		+ "'0' as otrosIngRenGrav,  "
	 		+ "COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl,  "
	 		+ "'1' as sisSalNet,  "
	 		+ "COALESCE(aporteIess, 0.00) as apoPerIess,  "
	 		+ "'0.00' as aporPerIessConOtrosEmpls,  "
	 		+ "case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) "
	 		+ "from SRI_DETALLE_IMPUESTO_RENTA  "
	 		+ "where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
	 		+ "case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)    "
	 		+ "from SRI_DETALLE_IMPUESTO_RENTA   "
	 		+ "where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp   else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda, "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,  "
	 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,  "
	 		+ "case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00)  - COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))          -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +  "
	 		+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))        - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)   -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  "
	 		+ "(extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +      COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))       -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))           -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))   < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,   COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,   round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp      left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =    (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "   and DETALLE_SRDED like 'ALIMENTACION')     left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'SALUD')    left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VIVIENDA')     left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =    (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'EDUCACION')    left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VESTIMENTA')   left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp      left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp,   STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded   "
	 		+ "where  activo_geded = true   "
	 		//+ "and ide_geedp in ($P!{ide_geedp}) "
	 		+ "and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+")   group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, ide_geedp)   as depar on depar.ide_gtemp= emp.ide_gtemp    left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))    and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte    FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos  "
	 		+ "FROM NRH_DETALLE_ROL detarol   left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO    in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))  "
	 		+ "and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero  "
	 		+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+")"
	 		//+ "select IDE_NRROL from NRH_ROL where IDE_GEPRO in   "
	 		//+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))     "
	 		+ "and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP  "
	 		+ "     left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste  "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))   "
	 		+ "where IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))   "
	 		+ "and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
	 		+ "SUM(DETAROL.VALOR_NRDRO)  "
	 		+ "AS DecimoCuarto  "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))  "
	 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+roles+"))  "	
	 		
	 		+ "and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
	 		+ "SUM(DETAROL.VALOR_NRDRO)  "
	 		+ "AS decimoCuartoAjuste  "
	 		+ "FROM NRH_DETALLE_ROL detarol  "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
	 		+ "and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP   "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol   "
	 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
	 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
	 		+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "
	 		+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
	 		+ "and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP   "
	 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess   "
	 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   " 
	 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))    "
	 		+ "and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      where depar.ide_gttem not like '%3%'      order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,      "
	 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y ";
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	
	/*
	

	 String sql="select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as  fraccion_basica_srdir    "
			+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr = 12 and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*    "
			+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     "
			+ "FROM public.sri_detalle_impuesto_renta     "
			+ "where  ide_srimr = 12 and basimp between  "
			+ "fraccion_basica_srdir and exceso_hasta_srdir)) +  "
			+ "(SELECT  imp_fraccion_srdir FROM public.sri_detalle_impuesto_renta   where  ide_srimr = 12 and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    as impuestoCausado    "
			+ "from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc,  "
			+ "ide_geedp,      "
			+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,   documento_identidad_gtemp as idRet,   "
			+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab, "
			+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab, '001' as estab,      '01'   as residenciaTrab,   '593' as pasisResidencia,      'NA' as aplicaConvenio,  "
			+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap, case when   round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap, "
			+ "'N' as tipidDiscap,   '999' as idDiscap,      COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,    COALESCE(sobresueldos,0.00) as sobSuelComRemu,      '0' as partUtil,   '0' as intGrabGen,  "
			+ "'0' as impRentEmpl,   COALESCE(DecimoTercero,0.00) as decimoTercer,   case   "
			+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>380.00 then 386.00 "
			+ "else "
			+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end "
			+ "as decimoCuarto,    "
			+ "COALESCE(fondo, 0.00) as  fondoReserva,    "
			+ "'0' as salarioDigno,   '0' as otrosIngRenGrav,   COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl, "
			+ "'1' as sisSalNet,   COALESCE(aporteIess, 0.00) as apoPerIess,    '0.00' as aporPerIessConOtrosEmpls,      "
			+ "case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   "
			+ "WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    "
			+ "from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    "
			+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
			+ "case when round(FLOOR(((extract(year from cast('2018-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA   where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,   "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "
			+ "(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))  "
			+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp   else '2018-01-1' end::date)) < 12 then  "
			+ "(DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12   "
			+ "+ (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))   "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda,  "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))   "
			+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ " - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12  "
			+ " + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) < 12   "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 + (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))       "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,  "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00) "
			+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
			+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
			+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +       "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
			+ "else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,         "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+ "
			+ "COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))       "
			+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
			+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +       "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12    "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))     "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,  "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00) "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00) +COALESCE(ali.valor_deducible_srdee,0.00)))       "
			+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -  "
			+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,   "
			+ "case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00)  -    "
			+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00)+COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
			+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12   "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12   "
			+ "+(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))   "
			+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+   "
			+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
			+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      "
			+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12    "
			+ "then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12   "
			+ "+ (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))    "
			+ " -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)   "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   "
			+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) -   "
			+ "DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ "- DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ "- DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      "
			+ "else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))    "
			+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)  "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),     "
			+ "COALESCE(ali.valor_deducible_srdee,0.00))-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)  "
			+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   "
			+ "COALESCE(vest.valor_deducible_srdee,0.00))        - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then    "
			+ "round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)   "
			+ "* (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA   "
			+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)   -(case when round(FLOOR(((extract(year from cast('2018-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372   "
			+ "+   (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65   "
			+ "then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA   "
			+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   "
			+ "COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+   "
			+ "COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),  "
			+ " COALESCE(vivienda.valor_deducible_srdee,0.00))       -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+   "
			+ "COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date)   "
			+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "
			+ "else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))       "
			+ "-COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)   "
			+ "+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))    "
			+ "then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018    "
			+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   "
			+ "COALESCE(educa.valor_deducible_srdee,0.00))           -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)  "
			+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
			+ "then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date))   < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018   "
			+ "then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  "
			+ "then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   "
			+ "COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)"
			+ "+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       "
			+ "then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = 2018 then fecha_geedp else '2018-01-1' end::date)) * 12 +(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)   "
			+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date)  "
			+ " - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = 2018  then fecha_geedp else '2018-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12   "
			+ "else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40  "
			+ " then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) *  "
			+ " (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    "
			+ "where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('2018-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372   "
			+ " +  (extract(month from cast('2018-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('2018-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65   "
			+ "then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)   "
			+ "from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= 12 GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, "
			+ " '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      "
			+ " COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,    "
			+ " COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,    "
			+ " round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0    "
			+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   "
			+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    "
			+ " round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0    "
			+ " then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp      "
			+ " left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =     "
			+ " (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12   and DETALLE_SRDED like 'ALIMENTACION')  "
			+ "left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri     "
			+ "left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'SALUD')   "
			+ "left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      and vivienda.IDE_SRDED =  "
			+ "  (select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'VIVIENDA')     "
			+ "left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =     "
			+ "(select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = 12  and DETALLE_SRDED like 'EDUCACION')    "
			+ "left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =  "
			+ "(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = 12  and DETALLE_SRDED like 'VESTIMENTA')   "
			+ "left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp     "
			+ "left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp,    "
			+ "STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   "
			+ "left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded    "
			+ "where  activo_geded = true and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )=2018)   "
			+ "group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp)   as depar on depar.ide_gtemp= emp.ide_gtemp     "
			+ "left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol     "
			+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
			+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))    "
			+ "and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     "
			+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol     "
			+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    "
			+ "(select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))     "
			+ "and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      "
			+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte     "
			+ "FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
			+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11)) and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP)   "
			+ "solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     "
			+ " left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))  "
			+ "and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP      "
			+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos    "
			+ "FROM NRH_DETALLE_ROL detarol   left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
			+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO     "
			+ "in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))      and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  "
			+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero     "
			+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
			+ "where  IDE_NRROL    in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in    "
			+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))     and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP     "
			+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste       "
			+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
			+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
			+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111)) "
			+ "and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,   "
			+ "SUM(DETAROL.VALOR_NRDRO)  "
			+ "AS DecimoCuarto  "
			+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
			+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
			+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))    "
			+ "and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,   "
			+ "SUM(DETAROL.VALOR_NRDRO)  "
			+ "AS decimoCuartoAjuste     "
			+ "FROM NRH_DETALLE_ROL detarol    "
			+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))  "
			+ "and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol      "
			+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
			+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in    "
			+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))   and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP    "
			+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess      FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
			+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
			+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  11))      "
			+ "and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      where depar.ide_gttem not like '%3%'      "
			+ "order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,      COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y";

	*/
	
	
	
	
	
	/*
	 String sql="select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as  fraccion_basica_srdir  "+ 
		"  FROM public.sri_detalle_impuesto_renta  where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*     "+
		"  (SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     FROM public.sri_detalle_impuesto_renta   "+
		"  where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir        "+
		"  FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)  "+
		"  as impuestoCausado   from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc, ide_geedp,     "+
		"  substring(detalle_gttdi from 1 for 1) as tipIdRet,   documento_identidad_gtemp as idRet,    "+
		"  APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,     "+
		"  COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab, '001' as estab,    "+
		"  '01'   as residenciaTrab,   '593' as pasisResidencia,    "+
		"  'NA' as aplicaConvenio,  case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap, case when "+
		"  round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap, 'N' as tipidDiscap,   '999' as idDiscap,    "+
		"  COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,    COALESCE(sobresueldos,0.00) as sobSuelComRemu,    "+
		"  '0' as partUtil,   '0' as intGrabGen,   '0' as impRentEmpl,"
		
		+ "COALESCE(DecimoTercero,0.00) as decimoTercer,   case  "
		+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>380.00 then 386.00 "
		+ "else   "
		+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end "
		+ "as decimoCuarto,  "
		+ ""
		+ ""
		//+ "   COALESCE(decimoTercero,0.00)-COALESCE(decimoTercerAjuste,0.00) as decimoTercer,    "+
		//"  COALESCE(decimoCuarto, 0.00) as decimoCuarto,    "
		+ "COALESCE(fondo, 0.00) as  fondoReserva,    "+
		"  '0' as salarioDigno,   '0' as otrosIngRenGrav,   COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)  "+
		"  as ingGravConEsteEmpl,   '1' as sisSalNet,   COALESCE(aporteIess, 0.00) as apoPerIess,    '0.00' as aporPerIessConOtrosEmpls,    "+
		
		"  case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad , "+
		
		"  case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		" (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		" (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		" WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		" where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera , "+
 
		"  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when "+
		"  EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp "+
		"  else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "+
		"  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda,       "+
		"  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))  as salud,       "+
		"  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,       "+
		"  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,      "+ 			
		"  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,     "+  
		"  case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + "+
		"  COALESCE(sobresueldos,0.00),0.00)  - COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vivienda.valor_deducible_srdee,0.00))       "+  
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))       "+
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))       "+
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(ali.valor_deducible_srdee,0.00))        "+	
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vest.valor_deducible_srdee,0.00))      "+ 
		"  - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end) "+
		"  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		"  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		"  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0 "+
		"  then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +    "+
		"  COALESCE(sobresueldos,0.00),0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
	    "  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vivienda.valor_deducible_srdee,0.00))     "+
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , "+
		"  FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))      "+ 
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))         "+		
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(ali.valor_deducible_srdee,0.00))      "+
		"  -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
		"  (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when "+
		"  EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vest.valor_deducible_srdee,0.00))        "+
		"  - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)"+
		"  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		" (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		" (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,    "+
		"  COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP, "+
		"  round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria, "+
		"  COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario, "+
		"  round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 "+
		"  then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)"+
		"  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2) "+
		"  as impuestoSolidario , "+
		"  COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,  "+
		"  round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 "+
		"  then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp    "+
		"  left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =  "+
		"  (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp  "+
		"  on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "   and DETALLE_SRDED like 'ALIMENTACION')   "+  
		"  left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp   "+
		"  and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"  left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'SALUD')  "+   
		"  left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp    "+
		"  and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"  left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VIVIENDA')   "+  
		"  left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =  "+
		"  (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr  "+
		"  where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'EDUCACION')    left join sri_deducibles_empleado as vest on vest.ide_gtemp   =  "+
		"  emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"  left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like 'VESTIMENTA') "+   
		"  left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi    "+
		"  left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp    "+ 
		"  left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp,  "+
		
		"  STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar "+
		"  left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded "+
		//AQUI ES LO DE EMPLEADO    and ide_geedp in ("+IDE_GEEDP+")
		
		"  where  activo_geded = true and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  "+
		"  is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+") "+
		"  group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,ide_geedp) "+   
		"  as depar on depar.ide_gtemp= emp.ide_gtemp    left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo    "+ 
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER    "+ 
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB     "+
		"  where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  "+  
		"  and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP   "+		
		"  left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol   "+  
		"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"  and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP    "+	
		"  (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte  "+
		"  FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"  (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP    "+
		"  left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol   "+  
		"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"  (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP    "+
		"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos    FROM NRH_DETALLE_ROL detarol "+  
		"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+  
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB     "+
		"  where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO  "+
		"  in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP   "+ 
		"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero    "
		+ "FROM NRH_DETALLE_ROL detarol     "
		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
		+ "where  IDE_NRROL    in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "
		+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+ str_ide_anio +"))     "
		+ "and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP "
		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste  "+     
		//+ ""
		//+ ""
		//+ "FROM NRH_DETALLE_ROL detarol   "+  
		//"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "+ 
		//"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+  
		//"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL  "+
		//"  in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))   "+
		//"  and detrubro.IDE_NRRUB in (334,368,367) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP   "+ 
		//"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste     "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+  
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
		+ "where  IDE_NRROL in "+
		"  (select IDE_NRROL from NRH_ROL where "
		+ "IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
		+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107, "
		+ "108,109,110,111)) "+    
		//+ "IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  "+
		"  and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP    "+
		//"  and detrubro.IDE_NRRUB in (369) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP    "+
		"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuarto   "
		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ( "
		+ "72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106, "
		+ "107,108,109,110,111)) "
		+ " and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP, "
		+ "SUM(DETAROL.VALOR_NRDRO "
		+ "AS decimoCuartoAjuste "
		+ "FROM NRH_DETALLE_ROL detarol  "+   
		"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "+ 
		"  where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  and detrubro.IDE_NRRUB in (333,359) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP    "+
		"  left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol    "+ 
		"  left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB     "+
		"  where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "+
		"  (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))   and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta  "+
		"  on renta.IDE_GTEMP = emp.IDE_GTEMP   left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess    "+
		"  FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+  
		"  left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"  left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "+ 
		"  where  IDE_NRROL in (select IDE_NRROL from NRH_ROL  "+
		"  where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"  and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP    "+
		"  where depar.ide_gttem not like '%3%'    "+
		"  order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,    "+
		"  COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y ";
	 	*/
	 	tab_seleccion.setSql(sql);
	 	System.out.println(sql);

	 	tab_seleccion.ejecutarSql();
	 	utilitario.addUpdate("tab_seleccion");
	 
	 	 /* for (int i = 0; i <  tab_seleccion.getTotalFilas(); i++) {
	 		 ser_sri.insertarTablaFormulario107(Double.parseDouble(tab_seleccion.getValor(i,"suelSal")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"sobSuelComRemu")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"partUtil")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"intGrabGen")),
	 				  Double.parseDouble(tab_seleccion.getValor(i,"decimoTercer")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"decimoCuarto")),
	 				  Double.parseDouble(tab_seleccion.getValor(i,"fondoReserva")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"otrosIngRenGrav")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"apoPerIess")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"aporPerIessConOtrosEmpls")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"vivienda")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"salud")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"educacion")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"alimentacion")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"vestimenta")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"descuentoDiscapacidad")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"descuentoTercera")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"valimpasuesteempl")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"basimp")),
	 				  Double.parseDouble(tab_seleccion.getValor(i,"impuestoCausado")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"valRetAsuOtrosEmpls")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"valImpAsuEsteEmpl")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"valRet")), 
	 				  Double.parseDouble(tab_seleccion.getValor(i,"ingGravConEsteEmpl")), 
	 				  true, 
	 				  false,Integer.parseInt(tab_seleccion.getValor(i,"ide_gtemp")),
	 				  Integer.parseInt(tab_seleccion.getValor(i,"ide_geedp")),  Integer.parseInt(IDE_SRIMR),
	 				 tab_seleccion.getValor(i,"nuRuc"));
	 	  }*/
	  
			
}	 

public double consultarSueloXAnio(int anio, String mes){

TablaGenerica tabSBU=utilitario.consultar("SELECT valor_nrdro,count(*) as contador  "
		+ "FROM nrh_detalle_rol "
		+ "WHERE  ide_nrrol in( "
		+ "select ide_nrrol from nrh_rol  rol  "
		+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro  "
		+ "where gprol.ide_geani="+anio+" and gprol.ide_gemes in ("+mes+") "
		+ ")  and ide_nrder in ( "
		+ "select ide_nrder from nrh_detalle_rubro nrder "
		+ "left join nrh_rubro rub on rub.ide_nrrub = nrder.ide_nrrub  "
		+ "where nrder.ide_nrrub=276) and valor_nrdro>0.0 "
		+ "GROUP BY valor_nrdro "
		+ "HAVING valor_nrdro>1 "
		+ "ORDER BY valor_nrdro ");
		double a=0,b=0;
		boolean bandera=false;
		if (tabSBU.getTotalFilas()>0) {
			a=Double.parseDouble(tabSBU.getValor("VALOR_NRDRO"));
			return a;	
		}else {
			return 0;
		}



}


public void seleccionoEmpleado(SelectEvent evt){
	tab_tabla1.seleccionarFila(evt);
	String IDE_SRIMR=tab_tabla1.getValor("IDE_SRIMR");
	System.out.println("IDE_SRIMR: "+IDE_SRIMR);
	TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
	String  fecha_inicio = tab_imp_renta.getValor("fecha_inicio_srimr");
	System.out.println("fecha_inicio: "+fecha_inicio);
	String   fecha_fin =tab_imp_renta.getValor("fecha_fin_srimr");
	System.out.println("fecha_Fin: "+fecha_fin);
	String anioHasta =utilitario.getAnio(fecha_fin)+"";
	System.out.println("anioHasta: "+anioHasta);
	TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anioHasta+"'");
	String str_ide_anio=tab_anio.getValor("IDE_GEANI");
	System.out.println("str_ide_anio: "+str_ide_anio);
	

	
    getTablaReporte(IDE_SRIMR,anioHasta,str_ide_anio);
	


	  
	  
	  
	  
	  
	  
		//tab_seleccion.getColumna("nuRuc").setVisible(false);
		//tab_seleccion.getColumna("tipIdRet").setVisible(false);
		//tab_seleccion.getColumna("estab").setVisible(false);
		//tab_seleccion.getColumna("residenciaTrab").setVisible(false);
		//tab_seleccion.getColumna("aplicaConvenio").setVisible(false);
		//tab_seleccion.getColumna("pasisResidencia").setVisible(false);

		//tab_seleccion.getColumna("tipoTrabajoDiscap").setVisible(false);
		//tab_seleccion.getColumna("porcentajeDiscap").setVisible(false);
		//tab_seleccion.getColumna("tipidDiscap").setVisible(false);
		//tab_seleccion.getColumna("idDiscap").setVisible(false);
		//tab_seleccion.getColumna("impRentEmpl").setVisible(false);
		
		/*tab_seleccion.getColumna("salarioDigno").setVisible(false);
		tab_seleccion.getColumna("sisSalNet").setVisible(false);
		tab_seleccion.getColumna("impRentEmpl").setVisible(false);
		
		tab_seleccion.getColumna("valor_liquidacion_geedp").setVisible(false);
		tab_seleccion.getColumna("baseSolidaria").setVisible(false);
		tab_seleccion.getColumna("solidario").setVisible(false);
		tab_seleccion.getColumna("impuestoSolidario").setVisible(false);
		
		
		tab_seleccion.getColumna("numMeses").setVisible(false);
		tab_seleccion.getColumna("promedio").setVisible(false);


		tab_seleccion.getColumna("idRet").setLongitud(15);
		tab_seleccion.getColumna("idRet").setFiltro(true);
		tab_seleccion.getColumna("idRet").alinearCentro();
		tab_seleccion.getColumna("idRet").setNombreVisual("IDENTIFICACION");


		tab_seleccion.getColumna("apellidoTrab").setLongitud(15);
		tab_seleccion.getColumna("apellidoTrab").setFiltro(true);

		tab_seleccion.getColumna("apellidoTrab").alinearCentro();
		tab_seleccion.getColumna("apellidoTrab").setNombreVisual("APELLIDO");

		tab_seleccion.getColumna("nombreTrab").setLongitud(15);
		tab_seleccion.getColumna("nombreTrab").setFiltro(true);

		tab_seleccion.getColumna("nombreTrab").alinearCentro();
		tab_seleccion.getColumna("nombreTrab").setNombreVisual("NOMBRE");
*/

		
	  
	  
	  
	  
	  
	  

   
    
    
}


public TablaGenerica getImpuestoRenta(String IDE_SRIMR){			
	return utilitario.consultar("SELECT * FROM SRI_IMPUESTO_RENTA WHERE IDE_SRIMR=".concat(IDE_SRIMR));
}

public String getRoles() {
	return roles;
}

public void setRoles(String roles) {
	this.roles = roles;
}

public double getSueldo() {
	return sueldo;
}

public void setSueldo(double sueldo) {
	this.sueldo = sueldo;
}





}
