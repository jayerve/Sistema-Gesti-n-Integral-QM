package paq_mantenimiento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.text.TabExpander;

import org.apache.poi.hssf.record.formula.TblPtg;
import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_comercializacion.ejb.ServicioClientes;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;


public class pre_mantenimiento_ficha extends Pantalla {

	
	//private Tabla tab_direccion=new Tabla();
//	private Tabla tab_telefono=new Tabla();
//	private Tabla tab_email=new Tabla();
	private Tabla tab_documento=new Tabla();
//	private Tabla tab_tarifa=new Tabla();
//	private Tabla tab_clientes=new Tabla();
//	private Tabla tab_contratos = new Tabla();
//	private Tabla tab_servicio = new Tabla();
//	private Tabla tab_establecimiento = new Tabla();
	
	private Tabla tab_equipo_hardware=new Tabla();
	private Tabla tab_equipo_software = new Tabla();
	private Tabla tab_rutina_mantenimiento_equipo = new Tabla();
	private Tabla tab_empleado = new Tabla();
	private Tabla tab_mantenimiento = new Tabla();
	private Tabla tab_equipo = new Tabla();
	private Tabla tab_equipo_otro_componente = new Tabla();
	private SeleccionTabla set_pantalla_sucursal= new SeleccionTabla();
	private SeleccionTabla set_actualizar=new SeleccionTabla();
	private Confirmar con_guardar= new Confirmar();
	private SeleccionTabla set_buscar_cliente = new SeleccionTabla();
	private Combo com_anio=new Combo();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private String ide_mtequ ="";
	private Dialogo dia_equipo = new Dialogo();

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	public pre_mantenimiento_ficha (){
		Etiqueta eti_colaborador=new Etiqueta("Escoga equipo:");
		// autocompletar equipo
		aut_empleado.setId("aut_empleado");
		String str_sql_emp="";
		str_sql_emp="SELECT ide_mtequ,ide_mtequ, est.nombre_mtest, equip.ide_afact, equip.marca_mtequ, equip.memoria_mtequ, "
						+ "equip.modelo_mtequ, equip.nombre_mtequ, equip.observacion_mtequ, equip.procesador_mtequ, "
						+ "equip.serie_mtequ, tesq.nombre_mtteq,tesq.descripcion_mtteq "
						+ "FROM mto_equipo  equip "
						+ "left join mto_estado est  on est.ide_mtest=equip.ide_estado "
						+ "left join mto_tipo_equipo tesq  on tesq.ide_mtteq=equip.ide_tipequ ";
		aut_empleado.setAutoCompletar(str_sql_emp);		
		aut_empleado.setMetodoChange("filtrarEquipo");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		// boton limpiar
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();

		//Combo anio
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Anio:"));
		bar_botones.agregarComponente(com_anio);
		//Tabulador Mantenimiento, hardware, software, documento, otro componentes
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		//Tabla equipo
		tab_equipo.setId("tab_equipo");
		tab_equipo.setTabla("mto_equipo", "ide_mtequ", 1);
		tab_equipo.getColumna("ide_afact").setCombo("SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa");
		tab_equipo.getColumna("ide_afact").setAutoCompletar();
		tab_equipo.getColumna("ide_afact").setMetodoChange("getDatosBien");
		tab_equipo.getColumna("ide_estado").setCombo("SELECT ide_mtest, nombre_mtest FROM mto_estado");
	    tab_equipo.getColumna("ide_tipequ").setCombo("SELECT ide_mtteq, nombre_mtteq  FROM mto_tipo_equipo");
		tab_equipo.getColumna("ACTIVO_mtequ").setCheck();
		tab_equipo.getColumna("ACTIVO_mtequ").setValorDefecto("true");
		tab_equipo.getColumna("NOMBRE_MTEQU").setUnico(true);
		tab_equipo.getColumna("NOMBRE_MTEQU").setMayusculas(true);
		tab_equipo.getColumna("NOMBRE_MTEQU").setMetodoChange("verificaBien");
		tab_equipo.getColumna("puertos_mtequ").setCheck();
		tab_equipo.getColumna("puertos_mtequ").setValorDefecto("true");
		tab_equipo.getColumna("ide_afubi").setCombo("SELECT ide_afubi,detalle_afubi FROM AFI_UBICACION order by detalle_afubi asc");
		tab_equipo.getColumna("ide_afubi").setRequerida(true);
		tab_equipo.getColumna("foto_mtequ").setUpload("fotos_equipo_mantenimiento");
		tab_equipo.getColumna("foto_mtequ").setValorDefecto("/imagenes/im_empleado.png");
		tab_equipo.getColumna("foto_mtequ").setImagen("128", "128");
		tab_equipo.getColumna("ide_gtemp").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
				+ "FROM GTH_EMPLEADO EMP "
				+ "WHERE EMP.ACTIVO_GTEMP =TRUE");
		tab_equipo.getColumna("ide_gtemp").setRequerida(true);
		tab_equipo.setCondicion("ide_mtequ=-1");
		tab_equipo.setTipoFormulario(true);
		tab_equipo.getGrid().setColumns(6);
		tab_equipo.dibujar();
		PanelTabla pat_clientes=new PanelTabla ();
		pat_clientes.setPanelTabla(tab_equipo);
	
		
		//Tabla mantenimiento
		tab_mantenimiento.setId("tab_mantenimiento");
		tab_mantenimiento.setIdCompleto("tab_tabulador:tab_mantenimiento");
		tab_mantenimiento.getGrid().setColumns(4); 
		tab_mantenimiento.setTabla("mto_mantenimiento", "ide_mtman",2);
		tab_mantenimiento.getColumna("ide_mtequ").setVisible(false);
		tab_mantenimiento.getColumna("ide_estman").setCombo("SELECT ide_mtesm,  nombre_mtesm  "
				+ "FROM mto_estado_mantenimiento");
		tab_mantenimiento.getColumna("ide_geedp_custodio").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP, " +
				"CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_mantenimiento.getColumna("ide_geedp_custodio").setAutoCompletar();
		tab_mantenimiento.getColumna("ide_geedp_custodio").setRequerida(true);
		tab_mantenimiento.getColumna("ide_geedp_custodio").setMetodoChange("getDatosBienCustodio");
		tab_mantenimiento.getColumna("ide_geedp_responsable").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP, " +
				"CASE WHEN EPAR.ACTIVO_GEEDP=true THEN 'ACTIVO' ELSE 'INACTIVO' END " +

				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_mantenimiento.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_mantenimiento.getColumna("ide_geedp_responsable").setAutoCompletar();
		tab_mantenimiento.getColumna("ide_geedp_responsable").setRequerida(true);
		tab_mantenimiento.getColumna("ide_gtemp_respomsable").setVisible(false);
		tab_mantenimiento.getColumna("ide_gtemp_custodio_mantenimiento").setVisible(false);
		tab_mantenimiento.getColumna("fecha_mtman").setValorDefecto(utilitario.getFechaActual());
		tab_mantenimiento.getColumna("fecha_mtman").setNombreVisual("FECHA MANTENIMIENTO");
		tab_mantenimiento.getColumna("fecha_mtman").setRequerida(true);
		tab_mantenimiento.getColumna("fecha_mtman").setLectura(true);
		tab_mantenimiento.getColumna("activo_mtman").setCheck();
		tab_mantenimiento.getColumna("activo_mtman").setValorDefecto("true");
		tab_mantenimiento.getColumna("ide_geani").setCombo("select ide_geani, detalle_geani from gen_anio");
		tab_mantenimiento.getColumna("ide_geani").setAutoCompletar();
		tab_mantenimiento.getColumna("ide_geani").setLectura(true);
		tab_mantenimiento.setCondicion("ide_mtman=-1");
		tab_mantenimiento.setTipoFormulario(true);  //formulario 
		tab_mantenimiento.agregarRelacion(tab_equipo_hardware);//agraga relacion para los tabuladores
		tab_mantenimiento.agregarRelacion(tab_equipo_software);
		tab_mantenimiento.agregarRelacion(tab_rutina_mantenimiento_equipo);
		tab_mantenimiento.agregarRelacion(tab_equipo_otro_componente);
		tab_mantenimiento.dibujar();
		PanelTabla pat_panel6 = new PanelTabla();
		pat_panel6.setPanelTabla(tab_mantenimiento);
		
		
		//Tabla equipo hardware 
		tab_equipo_hardware.setId("tab_equipo_hardware");
		tab_equipo_hardware.setIdCompleto("tab_tabulador:tab_equipo_hardware");
		tab_equipo_hardware.setTabla("mto_equipo_hardware", "ide_mteqh", 3);
		tab_equipo_hardware.getColumna("ACTIVO_mteqh").setCheck();
		tab_equipo_hardware.getColumna("ACTIVO_mteqh").setValorDefecto("true");
		tab_equipo_hardware.getColumna("ide_mtman").setVisible(false);
		tab_equipo_hardware.getColumna("ide_hdware").setCombo("SELECT hard.ide_mthar,hard.nombre_mthar, thar.nombre_mttha "
				+ "FROM mto_hardware hard "
				+ "left join mto_tipo_hardware thar on thar.ide_mttha=hard.ide_tiphar");
		tab_equipo_hardware.getColumna("ide_hdware").setAutoCompletar();
    	tab_equipo_hardware.getColumna("estado_mteqh").setCombo("	SELECT ide_mtest,nombre_mtest FROM mto_estado");
		tab_equipo_hardware.getColumna("estado_mteqh").setAutoCompletar();
		tab_equipo_hardware.getColumna("ide_afact").setCombo("SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa");
		tab_equipo_hardware.getColumna("ide_afact").setAutoCompletar();
		tab_equipo_hardware.getColumna("ide_afact").setMetodoChange("getDatosBienHardware");
		tab_equipo_hardware.getColumna("direccion_ip_mteqh").setVisible(false);
		tab_equipo_hardware.getColumna("direccion_mac_mteqh").setVisible(false);
		tab_equipo_hardware.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_equipo_hardware);		
		
		
		//Tabla equipo software 
		tab_equipo_software.setId("tab_equipo_software");
		tab_equipo_software.setIdCompleto("tab_tabulador:tab_equipo_software");
		tab_equipo_software.setTabla("mto_equipo_software", "ide_mteqs", 4);
		tab_equipo_software.getColumna("IDE_SOFTWARE").setCombo("SELECT ide_mtsof,  nombre_mtsof "
		+ "FROM mto_software");
		tab_equipo_software.getColumna("IDE_SOFTWARE").setAutoCompletar();
		tab_equipo_software.getColumna("ide_afact").setCombo("SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa");
		tab_equipo_software.getColumna("ide_afact").setAutoCompletar();
		tab_equipo_software.getColumna("ACTIVO_mteqs").setCheck();
		tab_equipo_software.getColumna("ACTIVO_mteqs").setValorDefecto("true");
		tab_equipo_software.getColumna("ide_mtman").setVisible(false);
		tab_equipo_software.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_equipo_software);


		//Tabla tab_rutina_mantenimiento_equipo 
    	tab_rutina_mantenimiento_equipo.setId("tab_rutina_mantenimiento_equipo");
    	tab_rutina_mantenimiento_equipo.setIdCompleto("tab_tabulador:tab_rutina_mantenimiento_equipo");
    	tab_rutina_mantenimiento_equipo.setTabla("mto_rutina_mantenimiento_equipo", "ide_mtrum", 5);
    	tab_rutina_mantenimiento_equipo.getColumna("realizado_mtrum").setCheck();
    	tab_rutina_mantenimiento_equipo.getColumna("realizado_mtrum").setValorDefecto("false");
    	tab_rutina_mantenimiento_equipo.getColumna("ide_mtrut").setCombo("SELECT ide_mtrut,nombre_mtrut "
				+ "FROM mto_rutina_mantenimiento");
    	tab_rutina_mantenimiento_equipo.getColumna("ide_mtrut").setAutoCompletar();
    	tab_rutina_mantenimiento_equipo.getColumna("ACTIVO_mtrum").setCheck();
    	tab_rutina_mantenimiento_equipo.getColumna("ACTIVO_mtrum").setValorDefecto("true");
    	tab_rutina_mantenimiento_equipo.getColumna("IDE_MTMAN").setVisible(false);
    	tab_rutina_mantenimiento_equipo.dibujar();
		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_rutina_mantenimiento_equipo);

		
		//Tabla tab_rutina_mantenimiento_equipo 
		tab_documento.setId("tab_documento");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTipoFormulario(true);
		tab_documento.getGrid().setColumns(4);
		tab_documento.setTabla("mto_archivo","ide_mtarc",6);
		tab_documento.getColumna("archi_uri").setUpload("mantenimiento/archivos");
		tab_documento.getColumna("activo_mtarc").setCheck();
		tab_documento.getColumna("activo_mtarc").setValorDefecto("true");
		tab_documento.getColumna("ide_mtequ").setVisible(false);
		tab_documento.getColumna("ide_mtman").setVisible(false);
		tab_documento.dibujar();
		PanelTabla pat_panel5 = new PanelTabla();
		pat_panel5.setPanelTabla(tab_documento);
	
		
		//Tabla tab_equipo_otro_componente 
		tab_equipo_otro_componente.setId("tab_equipo_otro_componente");
		tab_equipo_otro_componente.setIdCompleto("tab_tabulador:tab_equipo_otro_componente");
		tab_equipo_otro_componente.setTabla("mto_equipo_componente_adicional", "ide_mtcoa", 7);
		tab_equipo_otro_componente.getColumna("ide_mtman").setVisible(false);
		tab_equipo_otro_componente.getColumna("ide_mttco").setCombo("select "
				+ "ide_mttco,descripcion_mttco "
				+ "from mto_tipo_componente  "
				+ "where activo_mttco=true "
				+ "order by descripcion_mttco asc");
		
		tab_equipo_otro_componente.getColumna("ide_mtsof").setCombo("SELECT ide_mtsof,  nombre_mtsof "
				+ "FROM mto_software");
		tab_equipo_otro_componente.getColumna("ide_mthar").setCombo("SELECT hard.ide_mthar,hard.nombre_mthar, thar.nombre_mttha "
				+ "FROM mto_hardware hard "
				+ "left join mto_tipo_hardware thar on thar.ide_mttha=hard.ide_tiphar");
		tab_equipo_otro_componente.getColumna("activo_mtcoa").setCheck();
		tab_equipo_otro_componente.getColumna("activo_mtcoa").setValorDefecto("true");
		tab_equipo_otro_componente.getColumna("ide_mtman").setVisible(false);
		tab_equipo_otro_componente.setTipoFormulario(true);
		tab_equipo_otro_componente.dibujar();
		PanelTabla pat_panel7 = new PanelTabla();
		pat_panel7.setPanelTabla(tab_equipo_otro_componente);
	
		//Tabuladores de tablas
		tab_tabulador.agregarTab("MANTENIMIENTO", pat_panel6);
		tab_tabulador.agregarTab("HARDWARE", pat_panel2);
		tab_tabulador.agregarTab("SOFTWARE", pat_panel3);//intancia los tabuladores
		tab_tabulador.agregarTab("RUTINA", pat_panel4);
		tab_tabulador.agregarTab("ARHIVO", pat_panel5);
		tab_tabulador.agregarTab("OTRO COMPONENTE", pat_panel7);

		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes,tab_tabulador,"60%","H");
		agregarComponente(div_division);

		//BOTON AGREGAR CLIENTE
		//Boton bot_agregarSucursal=new Boton();
	//	bot_agregarSucursal.setValue("AGREGAR MATRIZ");
		//bot_agregarSucursal.setIcon("ui-icon-person");
		//bot_agregarSucursal.setMetodo("agregarMatriz");
		//bar_botones.agregarBoton(bot_agregarSucursal);

		//PANTALLA SELECIONAR CLIENTE
		//set_pantalla_sucursal.setId("set_pantalla_sucursal");
		//set_pantalla_sucursal.setTitle("SELECCIONE MATRIZ");
		//set_pantalla_sucursal.getBot_aceptar().setMetodo("aceptarMatriz");
		
		

	}
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
			}
			
		if (tab_equipo.isFocus()) {
			tab_equipo.insertar();
	
		}
		if (tab_mantenimiento.isFocus()) {
			
			if (!tab_mantenimiento.isFilaInsertada()){
				if (tab_equipo.getValor("IDE_MTEQU")==null || tab_equipo.getValor("IDE_MTEQU").equals("") || tab_equipo.getValor("IDE_MTEQU").isEmpty())
	    		{
				utilitario.agregarMensaje("No se puede insertar", "Debe guardar un equipo valido");
				 return;
			}else{
			}
				tab_mantenimiento.insertar();
				tab_mantenimiento.setValor("ide_geani",""+com_anio.getValue());
				tab_mantenimiento.setValor("IDE_MTEQU", tab_equipo.getValor("IDE_MTEQU"));

			}else{
				if (tab_equipo.getValor("IDE_MTEQU")==null || tab_equipo.getValor("IDE_MTEQU").equals("") || tab_equipo.getValor("IDE_MTEQU").isEmpty())
	    		{
				utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
				return;
			}
				tab_mantenimiento.setValor("IDE_MTEQU", tab_equipo.getValor("IDE_MTEQU"));
			}
				
			
			TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			String ide_geedp_activo_responsable="",ide_geedp_activo_custodio="";
			ide_geedp_activo_responsable=ser_gestion.getIdeContratoActivo(tabEmpDep.getValor("IDE_GTEMP"));
			//ide_geedp_activo_responsable=ser_gestion.getIdeContratoActivo(tabEmpDep.getValor("IDE_GTEMP"));
			int empleado_cusodio=0;
			tab_mantenimiento.setValor("ide_gtemp_respomsable", tabEmpDep.getValor("IDE_GTEMP"));
			tab_mantenimiento.setValor("ide_geedp_responsable", ide_geedp_activo_responsable);
			
			try {
				if (tab_equipo.getValor("IDE_AFACT")==null) {
					if (tab_equipo.getValor("IDE_GTEMP")!=null) {
						tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento",""+tab_equipo.getValor("IDE_GTEMP"));
						ide_geedp_activo_custodio="";
						ide_geedp_activo_custodio=ser_gestion.getIdeContratoActivo(tab_equipo.getValor("IDE_GTEMP"));
						tab_mantenimiento.setValor("ide_geedp_custodio", ide_geedp_activo_custodio);
					}
					
				}else
				if (tab_equipo.getValor("IDE_AFACT")!=null || tab_equipo.getValor("IDE_AFACT").equals("") ||  !tab_equipo.getValor("IDE_AFACT").isEmpty()) {
					empleado_cusodio=obtenerEmpleado(tab_equipo.getValor("IDE_AFACT"));
					tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento",""+empleado_cusodio);
					ide_geedp_activo_custodio=ser_gestion.getIdeContratoActivo(""+empleado_cusodio);
					tab_mantenimiento.setValor("ide_geedp_custodio", ide_geedp_activo_custodio);
				}
			} catch (Exception e) {
				System.out.println("ERROR EN ACTIVO");
			}
	
		
		}

		if (tab_equipo_hardware.isFocus()) {
			if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
    		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
			return;}
			tab_equipo_hardware.insertar();
			tab_equipo_hardware.setValor("ide_mtman", tab_mantenimiento.getValor("ide_mtman"));
		}

		 if (tab_equipo_software.isFocus()) {
			if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
    		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
			return;}
			tab_equipo_software.insertar();
			tab_equipo_software.setValor("ide_mtman", ""+tab_mantenimiento.getValor("ide_mtman"));

		}
		
		if (tab_rutina_mantenimiento_equipo.isFocus()) {
			if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
    		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
			return;}
			
			int ide_mtrut=0;
			TablaGenerica tab_rutinas =null;
		       TablaGenerica tab_consultar_datos= utilitario.consultar("select * from mto_rutina_mantenimiento_equipo where  "
		       		+ "ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
		
	    	    tab_rutinas = utilitario.consultar("SELECT ide_mtrut, descripcion_mtrut, nombre_mtrut, orden_mtrut, activo_mtrut "
						+ "FROM mto_rutina_mantenimiento "
						+ "order by  ide_mtrut asc");
	    	    
		       if (tab_consultar_datos.getTotalFilas()>0) {
		    	   
		    		for (int i = 0; i < tab_rutinas.getTotalFilas(); i++) {
	    				for (int j = 0; j < tab_consultar_datos.getTotalFilas(); j++) {
			    		if (tab_rutinas.getValor(i,"ide_mtrut").equals(tab_consultar_datos.getValor(j,"ide_mtrut"))) {
			    			ide_mtrut=Integer.parseInt(tab_rutinas.getValor(i,"ide_mtrut"));
			    		}
			    		}
						if(ide_mtrut==0){
		    			tab_rutina_mantenimiento_equipo.insertar();
		    			tab_rutina_mantenimiento_equipo.setValor("ide_mtman", tab_mantenimiento.getValor("ide_mtman"));
		    			tab_rutina_mantenimiento_equipo.setValor("realizado_mtrum", ""+false);
		    			tab_rutina_mantenimiento_equipo.setValor("ide_mtrut", tab_rutinas.getValor(i,"ide_mtrut"));
		    			ide_mtrut=0;
						}else{
							ide_mtrut=0;
						}
			    		//}
		    		}
		    	   
			}else{
				
		
		for (int i = 0; i < tab_rutinas.getTotalFilas(); i++) {
			tab_rutina_mantenimiento_equipo.insertar();
			tab_rutina_mantenimiento_equipo.setValor("ide_mtman", tab_mantenimiento.getValor("ide_mtman"));
			tab_rutina_mantenimiento_equipo.setValor("realizado_mtrum", ""+false);
			tab_rutina_mantenimiento_equipo.setValor("ide_mtrut", tab_rutinas.getValor(i,"ide_mtrut"));
		}
		
			}
			
		}
		
		
		if (tab_documento.isFocus()) {
			if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
    		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
			return;}
			tab_documento.insertar();
			tab_documento.setValor("ide_mtman", tab_mantenimiento.getValor("ide_mtman"));

		}
		
		if (tab_equipo_otro_componente.isFocus()) {
			if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
    		{
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un mantenimiento valido");
			return;}
			tab_equipo_otro_componente.insertar();
			tab_equipo_otro_componente.setValor("ide_mtman", tab_mantenimiento.getValor("ide_mtman"));

		}
		
		
		

	}

	@Override
	public void guardar() {
		
		validarIngresoEquipo();
	      if (tab_equipo.guardar()) {
	    	  if(tab_equipo.isFilaInsertada() || tab_equipo.isFilaModificada()){
					tab_equipo.setValor("NOMBRE_MTEQU",tab_equipo.getValor("NOMBRE_MTEQU").toUpperCase());
	            	guardarPantalla();
	            	ide_mtequ=tab_equipo.getValor("IDE_MTEQU");

	    	  }else{
	            	guardarPantalla();
	            	ide_mtequ=tab_equipo.getValor("IDE_MTEQU");

	    	  }

			//	tab_equipo.setValor("ide_geani",""+com_anio.getValue());


	      	      }
	      
	      if (tab_mantenimiento.guardar()) {
	    	  if(tab_mantenimiento.isFilaInsertada() || tab_mantenimiento.isFilaModificada()){
	  			TablaGenerica tabEmpDep = utilitario.consultar("select ide_geedp,ide_gtemp "
	  					+ "from gen_empleados_departamento_par where ide_geedp in("+tab_mantenimiento.getValor("ide_geedp_custodio")+") "
	  							+ "limit 1");
	    			String ide_geedp_activo_responsable="",ide_geedp_activo_custodio="";
	    			int empleado_cusodio=0;
	    		  guardarPantalla(); 
	    	  }else{
	    		  TablaGenerica tabEmpDep = utilitario.consultar("select ide_geedp,ide_gtemp "
		  					+ "from gen_empleados_departamento_par where ide_geedp in("+tab_mantenimiento.getValor("ide_geedp_custodio")+") "
		  							+ "limit 1");
		    			String ide_geedp_activo_responsable="",ide_geedp_activo_custodio="";
		    			int empleado_cusodio=0;
	    		  guardarPantalla(); 

	           
	    	  }
	            }if (tab_equipo_hardware.guardar()) {
	            	  if(tab_equipo_hardware.isFilaInsertada() || tab_equipo_hardware.isFilaModificada()){
	            		 	if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
	                		{
	            			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un mantenimiento valido");
	            			return;
	            		}
	        				tab_equipo_hardware.setValor("nombre_mteqh",tab_equipo_hardware.getValor("nombre_mteqh").toUpperCase());

	    	    		  guardarPantalla(); 
	    	    	  }else{
       				//tab_equipo_hardware.setValor("nombre_mteqh",tab_equipo_hardware.getValor("nombre_mteqh").toUpperCase());
	                guardarPantalla();}
	                
	            }if (tab_equipo_software.guardar()) {
            	  if(tab_equipo_software.isFilaInsertada() || tab_equipo_software.isFilaModificada()){
	            	if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
            		{
        			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un mantenimiento valido");
        			return;
            		}
	                guardarPantalla();
            	  }else{
  	                guardarPantalla();
            	  }   
	            }if (tab_rutina_mantenimiento_equipo.guardar()) {
	            	if(tab_rutina_mantenimiento_equipo.isFilaInsertada() || tab_rutina_mantenimiento_equipo.isFilaModificada()){
	               	if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
            		{
        			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
        			return;
        		}
	                guardarPantalla();}
	            	else{
	            		   guardarPantalla();	            		
	            	}
	                
	            }if (tab_documento.guardar()) {
	            	if(tab_documento.isFilaInsertada() || tab_documento.isFilaModificada()){

	               	if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
            		{
        			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
        			return;
        		}
	                guardarPantalla();
	            	}else{
	            		 guardarPantalla();	
	            	}
	            
	        }
		
	            if (tab_equipo_otro_componente.guardar()) {
	            	if(tab_equipo_otro_componente.isFilaInsertada() || tab_equipo_otro_componente.isFilaModificada()){

	               	if (tab_mantenimiento.getValor("ide_mtman")==null || tab_mantenimiento.getValor("ide_mtman").equals("") || tab_mantenimiento.getValor("ide_mtman").isEmpty())
            		{
        			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un equipo valido");
        			return;
        		}
	                guardarPantalla();
	            	}else{
	            		 guardarPantalla();	
	            	}
	            
	        } 
	            
		
		
		
	}



	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 * metodo booleano para validar el tipo de documento de identidad cedula y ruc
	 */
	public boolean validarDocumentoIdentidad(String ide_gttdi,String documento_identidad){
		if (ide_gttdi!=null && !ide_gttdi.isEmpty()){
			if (documento_identidad!=null && !documento_identidad.isEmpty()){
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
					if (!utilitario.validarCedula(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de cedula ingresado no es valido");
						return false;
					}
				}else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
					if (!utilitario.validarRUC(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de RUC ingresado no es valido");
						return false;
					}
				}
			}
		}
		return true;
	}




	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}



	

	public Tabla getTab_documento() {
		return tab_documento;
	}



	public void setTab_documento(Tabla tab_documento) {
		this.tab_documento = tab_documento;
	}

	public SeleccionTabla getSet_pantalla_sucursal() {
		return set_pantalla_sucursal;
	}

	public void setSet_pantalla_sucursal(SeleccionTabla set_pantalla_sucursal) {
		this.set_pantalla_sucursal = set_pantalla_sucursal;
	}

	public SeleccionTabla getSet_actualizar() {
		return set_actualizar;
	}

	public void setSet_actualizar(SeleccionTabla set_actualizar) {
		this.set_actualizar = set_actualizar;
	}

	
	public SeleccionTabla getSet_buscar_cliente() {
		return set_buscar_cliente;
	}

	public void setSet_buscar_cliente(SeleccionTabla set_buscar_cliente) {
		this.set_buscar_cliente = set_buscar_cliente;
	}

	public Tabla getTab_rutina_mantenimiento_equipo() {
		return tab_rutina_mantenimiento_equipo;
	}

	public Tabla getTab_equipo_hardware() {
		return tab_equipo_hardware;
	}




	public void setTab_equipo_hardware(Tabla tab_equipo_hardware) {
		this.tab_equipo_hardware = tab_equipo_hardware;
	}




	public Tabla getTab_equipo_software() {
		return tab_equipo_software;
	}




	public void setTab_equipo_software(Tabla tab_equipo_software) {
		this.tab_equipo_software = tab_equipo_software;
	}




	public void setTab_rutina_mantenimiento_equipo(
			Tabla tab_rutina_mantenimiento_equipo) {
		this.tab_rutina_mantenimiento_equipo = tab_rutina_mantenimiento_equipo;
	}

	public Tabla getTab_empleado() {
		return tab_empleado;
	}

	public void setTab_empleado(Tabla tab_empleado) {
		this.tab_empleado = tab_empleado;
	}

	public Tabla getTab_mantenimiento() {
		return tab_mantenimiento;
	}

	public void setTab_mantenimiento(Tabla tab_mantenimiento) {
		this.tab_mantenimiento = tab_mantenimiento;
	}


	

public Tabla getTab_equipo() {
		return tab_equipo;
	}




	public void setTab_equipo(Tabla tab_equipo) {
		this.tab_equipo = tab_equipo;
	}




public Tabla getTab_equipo_otro_componente() {
	return tab_equipo_otro_componente;
}

public void setTab_equipo_otro_componente(Tabla tab_equipo_otro_componente) {
	this.tab_equipo_otro_componente = tab_equipo_otro_componente;
}

	


	public void aceptarEquipo(){
 		    tab_equipo.guardar();
			guardarPantalla();
			guardar();
			TablaGenerica tab_equipoGenerica= utilitario.consultar("SELECT ide_mtequ, ide_estado "
					+ "FROM mto_equipo order by ide_mtequ desc limit 1");
			tab_equipo.setCondicion("ide_mtequ="+tab_equipoGenerica.getValor("ide_mtequ"));
			tab_equipo.ejecutarSql();
			tab_mantenimiento.setValor("ide_mtequ", tab_equipoGenerica.getValor("ide_mtequ"));
			utilitario.addUpdate("tab_mantenimiento");
			dia_equipo.cerrar();
			utilitario.addUpdate("dia_equipo");
		
	}

	public void empleadoCustodio(){
		TablaGenerica tab_1=utilitario.consultar("SELECT ide_mtequ, ide_estado, ide_afact, marca_mtequ, memoria_mtequ  "
				+ "FROM mto_equipo "
				+ "where ide_mtequ="+tab_mantenimiento.getValor("ide_mtequ"));

		
		TablaGenerica tab_=utilitario.consultar("SELECT act.ide_afact as codigo,cod_anterior_afact as codigo_anterior,bcm.descripcion_bocam as nombre_catalogo,bcm.cat_codigo_bocam as item_presupuestario, "
				+ "cantidad_afact as cantidad,detalle_afact as descripcion_caracteristicas,observaciones_afact as componentes,   serie_afact as serie, marca_afact as marca, "
				+ "modelo_afact as modelo,  color_afact as color, "
				+ "chasis_afact as chasis, motor_afact as motor, placa_afact as placa,detalle_afest as estado,detalle_afubi as ubicacion,doc.nro_secuencial_afdoc as numero_ultima_acta,"
				+ "act.afi_ultima_acta as ultima_acta_erp,tdoc.detalle_aftidoc as tipo_ultima_acta,doccus.ide_gtemp  "
				+ " FROM 	afi_docu AS doc  "
				+ "LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc  "
				+ "LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc ,  afi_activo AS act  "
				+ "LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact  "
				+ "LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam   "
				+ "LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa "
				+ "LEFT JOIN afi_estado AS est ON act.ide_afest = est.ide_afest  "
				+ "LEFT JOIN afi_ubicacion AS ubi ON act.ide_afubi = ubi.ide_afubi  "
				+ "WHERE   act.ide_afact in ("+tab_1.getValor("ide_afact")+")  AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true)");


		if(tab_.getTotalFilas()==0){
			tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento", "-1");
			tab_mantenimiento.setValor("ide_geedp_custodio", "-1");

		}else {
			tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento", tab_.getValor("IDE_GTEMP"));
			String ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_.getValor("IDE_GTEMP"));
			tab_mantenimiento.setValor("ide_geedp_custodio", ide_geedp_activo);
			utilitario.addUpdateTabla(tab_mantenimiento,"ide_geedp_custodio", "");
			tab_equipo.setCondicion("ide_mtequ="+tab_mantenimiento.getValor("ide_mtequ"));
			tab_equipo.ejecutarSql();
			utilitario.addUpdate("tab_equipo");
			
		}

		
	}

	public ServicioGestion getSer_gestion() {
		return ser_gestion;
	}

	public void setSer_gestion(ServicioGestion ser_gestion) {
		this.ser_gestion = ser_gestion;
	}

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public ServicioClientes getSer_cliente() {
		return ser_cliente;
	}

	public void setSer_cliente(ServicioClientes ser_cliente) {
		this.ser_cliente = ser_cliente;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}
	
	/**
	 * Metodo retorna el anio seleccionado 
	 */
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null && aut_empleado.getValor()==null){
			tab_mantenimiento.setCondicion("IDE_GEANI="+com_anio.getValue());
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga equipo o anio ");
			return;	
		}else if(aut_empleado.getValor()!=null && com_anio.getValue()!=null)
		{
			tab_equipo.setCondicion("IDE_MTEQU="+aut_empleado.getValor());
			tab_mantenimiento.setCondicion("IDE_GEANI="+com_anio.getValue()+" AND IDE_MTEQU="+aut_empleado.getValor());
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga equipo o anio ");
			return;			
		}
			tab_mantenimiento.ejecutarSql();
			tab_equipo.ejecutarSql();
			tab_rutina_mantenimiento_equipo.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
			tab_rutina_mantenimiento_equipo.ejecutarSql();
			tab_equipo_hardware.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
			tab_equipo_hardware.ejecutarSql();
			tab_equipo_software.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
			tab_equipo_software.ejecutarSql();
			tab_documento.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
			tab_documento.ejecutarSql();
			tab_equipo_otro_componente.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
			tab_equipo_otro_componente.ejecutarSql();
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}
	
	public int  actualizarTablasMantenimiento(){
		TablaGenerica tabMantenimiento=utilitario.consultar("SELECT ide_mtman, ide_mtequ "
				+ "FROM mto_mantenimiento "
				+ "order by ide_mtman desc "
				+ "limit 1");
		int ide=0;
		if (tabMantenimiento.getTotalFilas()>0){
		ide=Integer.parseInt(tabMantenimiento.getValor("ide_mtman"))+1;
		}else {
			ide=1;
		}
		return ide;
			
	}
	

	@Override
	public void inicio() {
		try {
			super.inicio();
			actualizarTablas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);
		}
	}
	
	
	public void actualizarTablas(){
		tab_mantenimiento.setCondicion("ide_mtequ="+tab_equipo.getValor("ide_mtequ"));
		tab_equipo_hardware.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
		tab_equipo_software.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
		tab_rutina_mantenimiento_equipo.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
		tab_documento.setCondicion("ide_mtman="+tab_mantenimiento.getValor("ide_mtman"));
		tab_mantenimiento.ejecutarSql();
		tab_equipo_hardware.ejecutarSql();
		tab_equipo_software.ejecutarSql();
		tab_rutina_mantenimiento_equipo.ejecutarSql();
		tab_documento.ejecutarSql();
		utilitario.addUpdate("tab_equipo,tab_equipo_hardware,tab_equipo_software,tab_rutina_mantenimiento_equipo,tab_documento");
	}


	

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		try {
			super.fin();
			actualizarTablas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);		}
	}

	@Override
	public void siguiente() {
		try {
			// TODO Auto-generated method stub
			super.siguiente();
			actualizarTablas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);
		}
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		try {
			super.atras();
			actualizarTablas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);		}
	}
	
	
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
	}




	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		aut_empleado.limpiar();
		tab_equipo.limpiar();
		tab_mantenimiento.limpiar();
		tab_equipo_hardware.limpiar();
		tab_equipo_software.limpiar();
		tab_rutina_mantenimiento_equipo.limpiar();
		tab_documento.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado,tab_equipo,tab_mantenimiento,tab_equipo_hardware,tab_equipo_software,tab_rutina_mantenimiento_equipo,tab_documento");// limpia y refresca el autocompletar
	}

	
/**
 * Metodo retorna los equipos ingresados a la base de datos	
 * @param evt
 */
public void filtrarEquipo(SelectEvent evt){
	aut_empleado.onSelect(evt);
	//Anio no seleccionado y empleado seleccionado 
	if (com_anio.getValue()==null && aut_empleado.getValor()!= null) {
		tab_equipo.setCondicion("IDE_MTEQU=-1");
		tab_mantenimiento.setCondicion("IDE_MTEQU=-1");
		utilitario.agregarMensajeError("Debe seleccionar equipo o anio", "Seleccione parametro de busqueda");
		return;
	}
	
	//Anio seleccionado y empleado no seleccionado 
	if (com_anio.getValue()!=null && aut_empleado.getValor()== null) {
		tab_equipo.setCondicion("IDE_MTEQU=-1");
		tab_mantenimiento.setCondicion("IDE_MTEQU=-1");
		utilitario.agregarMensajeError("Debe seleccionar equipo", "Seleccione parametro de busqueda");
		return;
	}
	
	//Anio no seleccionado y empleado no seleccionado 
	if(aut_empleado.getValor()== null && com_anio.getValue()==null){
		utilitario.agregarMensajeError("Debe seleccionar equipo o año", "Seleccione parametro de busqueda");
		return;
	}
	
	tab_equipo.setCondicion("IDE_MTEQU="+aut_empleado.getValor());
	tab_mantenimiento.setCondicion("IDE_MTEQU="+aut_empleado.getValor()+" and IDE_GEANI="+com_anio.getValue());
	tab_equipo.ejecutarSql();
	tab_mantenimiento.ejecutarSql();
	tab_equipo_hardware.ejecutarValorForanea(tab_mantenimiento.getValorSeleccionado());
	tab_equipo_software.ejecutarValorForanea(tab_mantenimiento.getValorSeleccionado());
	tab_rutina_mantenimiento_equipo.ejecutarValorForanea(tab_mantenimiento.getValorSeleccionado());
	tab_documento.ejecutarValorForanea(tab_mantenimiento.getValorSeleccionado());
	tab_equipo_otro_componente.ejecutarValorForanea(tab_mantenimiento.getValorSeleccionado());
	tab_mantenimiento.ejecutarSql();
	tab_rutina_mantenimiento_equipo.ejecutarSql();
	tab_equipo_hardware.ejecutarSql();
	tab_equipo_software.ejecutarSql();
	tab_equipo_otro_componente.ejecutarSql();
}




public AutoCompletar getAut_empleado() {
	return aut_empleado;
}




public void setAut_empleado(AutoCompletar aut_empleado) {
	this.aut_empleado = aut_empleado;
}


public void getDatosBien(AjaxBehaviorEvent evt) {
	tab_equipo.modificar(evt);
	if (tab_equipo.getValor("ide_afact") == null) {
		return;
	}

	
	//TablaGenerica tab_datos_equipo=utilitario.consultar("select ide_mtequ,ide_afact from mto_equipo where ide_afact="+tab_equipo.getValor("IDE_AFACT"));
	if (verificaActivo(tab_equipo.getValor("ide_afact"))>0) {
		tab_equipo.setValor("ide_afact","");
		utilitario.addUpdateTabla(tab_equipo, "ide_afact,ide_gtemp", "");
		utilitario.agregarMensajeInfo("El equipo ya se encuentra ingresado", "Por favor realice la busqueda");
		return;
	}else{

	TablaGenerica tab_datos=utilitario.consultar("select marca_afact,modelo_afact,serie_afact from afi_activo where ide_afact="+tab_equipo.getValor("IDE_AFACT"));
	if (tab_datos.getTotalFilas()>0) {
		tab_equipo.setValor("marca_mtequ", tab_datos.getValor("marca_afact"));
		tab_equipo.setValor("modelo_mtequ", tab_datos.getValor("modelo_afact"));
		tab_equipo.setValor("serie_mtequ", tab_datos.getValor("serie_afact"));
		int empleado_cusodio=obtenerEmpleado(tab_equipo.getValor("IDE_AFACT"));
		tab_equipo.setValor("ide_gtemp", ""+empleado_cusodio);

		utilitario.addUpdateTabla(tab_equipo, "marca_mtequ,modelo_mtequ,serie_mtequ,ide_gtemp", "");
	}else{
		
	}
	}
} 



public void getDatosBienCustodio(AjaxBehaviorEvent evt) {
	tab_mantenimiento.modificar(evt);

	TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
	int empleado_cusodio=0;
	
	
	if (tab_equipo.getValor("IDE_AFACT")==null ){
		
		TablaGenerica emp= utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp="+tab_mantenimiento.getValor("ide_geedp_custodio"));
		tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento", tabEmpDep.getValor("IDE_GTEMP"));
	}
 else	if (tab_equipo.getValor("IDE_AFACT")!=null || tab_equipo.getValor("IDE_AFACT").equals("") ||  !tab_equipo.getValor("IDE_AFACT").isEmpty()) {
	empleado_cusodio=obtenerEmpleado(tab_equipo.getValor("IDE_AFACT"));
		tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento",""+empleado_cusodio);
		String ide_geedp_activo_custodio=ser_gestion.getIdeContratoActivo(""+empleado_cusodio);
		tab_mantenimiento.setValor("ide_geedp_custodio", ide_geedp_activo_custodio);

	}
} 

public void getDatosBienEmpleado(AjaxBehaviorEvent evt) {
	tab_mantenimiento.modificar(evt);
	TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
	String ide_geedp_activo_responsable="",ide_geedp_activo_custodio="";
	ide_geedp_activo_responsable=ser_gestion.getIdeContratoActivo(tabEmpDep.getValor("IDE_GTEMP"));
	//ide_geedp_activo_responsable=ser_gestion.getIdeContratoActivo(tabEmpDep.getValor("IDE_GTEMP"));
	int empleado_cusodio=0;
	tab_mantenimiento.setValor("ide_gtemp_custodio_mantenimiento", tabEmpDep.getValor("IDE_GTEMP"));
	tab_mantenimiento.setValor("ide_geedp_responsable", ide_geedp_activo_responsable);


} 

public void getDatosBienHardware(AjaxBehaviorEvent evt) {
	tab_equipo_hardware.modificar(evt);
	TablaGenerica tab_datos=utilitario.consultar("select marca_afact,modelo_afact,serie_afact from afi_activo where ide_afact="+tab_equipo_hardware.getValor("IDE_AFACT"));
	if (tab_datos.getTotalFilas()>0) {
		tab_equipo_hardware.setValor("marca_mteqh", tab_datos.getValor("marca_afact"));
		tab_equipo_hardware.setValor("modelo_mteqh", tab_datos.getValor("modelo_afact"));
		tab_equipo_hardware.setValor("serie_mteqh", tab_datos.getValor("serie_afact"));
		//utilitario.addUpdate("tab_equipo_hardware");
		utilitario.addUpdateTabla(tab_equipo_hardware, "marca_mteqh,modelo_mteqh,serie_mteqh,serie_mteqh", "");
		utilitario.addUpdate("tab_tabulador:tab_equipo_hardware");	

	}else{
	}
	// tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
} 


public void getDatosBienSoftware(AjaxBehaviorEvent evt) {
	tab_equipo_software.modificar(evt);
	TablaGenerica tab_datos=utilitario.consultar("select marca_afact,modelo_afact,serie_afact from afi_activo where ide_afact="+tab_equipo_software.getValor("IDE_AFACT"));
	if (tab_datos.getTotalFilas()>0) {
		tab_equipo_software.setValor("marca_mtequ", tab_datos.getValor("marca_afact"));
		tab_equipo_software.setValor("modelo_mtequ", tab_datos.getValor("modelo_afact"));
		tab_equipo_software.setValor("serie_mtequ", tab_datos.getValor("serie_afact"));
		utilitario.addUpdate("tab_equipo_software");
	}else{
	}
} 


public int verificaActivo(String IDE_AFACT){
	
	TablaGenerica tabBien = utilitario.consultar("SELECT IDE_MTEQU,IDE_AFACT,NOMBRE_MTEQU FROM MTO_EQUIPO WHERE IDE_AFACT="+IDE_AFACT);
	if (tabBien.getTotalFilas()>0) {
		return 1;
	}
	else{
		return 0;
	}
}


/**
 * Metodo valida que no se repita el equipo
 * @param evt
 */
public void  verificaBien(AjaxBehaviorEvent evt){
	tab_equipo.modificar(evt);
	TablaGenerica tabBien = utilitario.consultar("SELECT IDE_AFACT,NOMBRE_MTEQU,IDE_MTEQU FROM MTO_EQUIPO WHERE NOMBRE_MTEQU LIKE '%"+tab_equipo.getValor("NOMBRE_MTEQU").toUpperCase()+"%' ");
	if  (tabBien.getTotalFilasVisibles()>0){
		tab_equipo.setValor("NOMBRE_MTEQU", "");
		utilitario.addUpdateTabla(tab_equipo, "NOMBRE_MTEQU", "");
		utilitario.agregarMensajeError("Ya se ha registrado este equipo", "No se puede insertar un equipo repetido");
		return;
	}
}


public void validarIngresoEquipo(){
	if (tab_equipo.getValor("NOMBRE_MTEQU")==null || tab_equipo.getValor("NOMBRE_MTEQU").equals("") || tab_equipo.getValor("NOMBRE_MTEQU").isEmpty() ) {
		utilitario.agregarMensajeInfo("Campo Nonmbre vacio", "Debe ingresar datos en este campo");
		return;
	}
}




@Override
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}




@Override
public void aceptarReporte() {
	// TODO Auto-generated method stub
	Locale locale=new Locale("es","ES");
	if (rep_reporte.getReporteSelecionado().equals("Ficha Mantenimiento Equipo")){
		if (tab_equipo.getTotalFilas()>0) {
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();				
				p_parametros.put("IDE_MTEQU",Long.parseLong(tab_equipo.getValor("ide_mtequ")));
				p_parametros.put("IDE_MTMAN",Long.parseLong(tab_mantenimiento.getValor("IDE_MTMAN")));
				p_parametros.put("logo_empr","/upload/logos/logo.png");
				p_parametros.put("direccion_empr"," OE3G - N51-84 y Av. R�o");
				p_parametros.put("telefono_empr"," (02) 3930-600");
				
				
				TablaGenerica tab_=utilitario.consultar("SELECT ide_afubi, ide_aftid, afi_ide_afubi, detalle_afubi "
						+ "FROM afi_ubicacion  "
						+ "where ide_afubi="+tab_equipo.getValor("ide_afubi"));


				p_parametros.put("ubicacion",tab_.getValor("detalle_afubi"));
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				sef_reporte.dibujar();
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
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

public int obtenerEmpleado(String IDE_AFACT){
	int valorRetorno=0;
	TablaGenerica tab_empleado=utilitario.consultar(" SELECT act.ide_afact as codigo,cod_anterior_afact as codigo_anterior,bcm.descripcion_bocam as nombre_catalogo,bcm.cat_codigo_bocam as item_presupuestario, "
			+ "cantidad_afact as cantidad,detalle_afact as descripcion_caracteristicas,observaciones_afact as componentes,   "
			+ "serie_afact as serie, marca_afact as marca, modelo_afact as modelo,  color_afact as color, "
			+ "chasis_afact as chasis, motor_afact as motor, placa_afact as placa,detalle_afest as estado,detalle_afubi as ubicacion,doc.nro_secuencial_afdoc as numero_ultima_acta,act.afi_ultima_acta as ultima_acta_erp,  "
			+ "tdoc.detalle_aftidoc as tipo_ultima_acta,doccus.ide_gtemp    "
			+ "FROM afi_docu AS doc  "
			+ "LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc  "
			+ "LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc ,  afi_activo AS act   "
			+ "LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact   "
			+ "LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam   "
			+ "LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa  "
			+ "LEFT JOIN afi_estado AS est ON act.ide_afest = est.ide_afest   "
			+ "LEFT JOIN afi_ubicacion AS ubi ON act.ide_afubi = ubi.ide_afubi   "
			+ "WHERE   act.ide_afact in ("+IDE_AFACT+")  AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true)");

	if (tab_empleado.getTotalFilas()>0) {
		valorRetorno=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
	}else{
		valorRetorno=0;
	}

	return valorRetorno;
}




public String getIde_mtequ() {
	return ide_mtequ;
}




public void setIde_mtequ(String ide_mtequ) {
	this.ide_mtequ = ide_mtequ;
}








}

