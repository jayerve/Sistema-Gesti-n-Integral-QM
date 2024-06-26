package paq_presupuesto;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_sistema.aplicacion.Pantalla;

public class pre_certificacion_poa extends Pantalla 
{
	private Tabla tab_procesos_cert=new Tabla();
	private Combo com_anio=new Combo();
	
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	public static String par_sec_certificacion_poa;
	
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	 public pre_certificacion_poa() {
		// TODO Auto-generated constructor stub
		 
		bar_botones.limpiar(); 
		 
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		 
		com_anio.setCombo("select ide_geani,detalle_geani from gen_anio order by ide_geani desc");
		com_anio.setMetodo("filtrarAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		//com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		par_sec_certificacion_poa =utilitario.getVariable("p_sec_certificacion_poa");
				 
		tab_procesos_cert.setId("tab_procesos_cert");
		
		tab_procesos_cert.setTabla("precon_precontractual","ide_prpre",1);

		tab_procesos_cert.getColumna("ide_prpre").setNombreVisual("Código");
		tab_procesos_cert.getColumna("ide_prpre").setOrden(1);
		tab_procesos_cert.getColumna("ide_prpre").setLongitud(8);
		tab_procesos_cert.getColumna("codigo_prpre").setNombreVisual("Código Proceso");
		tab_procesos_cert.getColumna("codigo_prpre").setOrden(2);
		tab_procesos_cert.getColumna("codigo_prpre").setLongitud(15);
		tab_procesos_cert.getColumna("codigo_prpre").setLectura(true);
		tab_procesos_cert.getColumna("codigo_prpre").setEstilo("width:200px");

		tab_procesos_cert.getColumna("fecha_prpre").setNombreVisual("Fecha de Registro");
		tab_procesos_cert.getColumna("fecha_prpre").setOrden(3);
		tab_procesos_cert.getColumna("fecha_prpre").setLectura(true);

		tab_procesos_cert.getColumna("hora_prpre").setVisible(false);
		tab_procesos_cert.getColumna("ide_prpac").setVisible(false);
				
		tab_procesos_cert.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_procesos_cert.getColumna("ide_pretp").setNombreVisual("Procedimiento");
		tab_procesos_cert.getColumna("ide_pretp").setLectura(true);
		tab_procesos_cert.getColumna("ide_pretp").setOrden(5);
		tab_procesos_cert.getColumna("ide_pretp").setEstilo("width:300px");
		tab_procesos_cert.getColumna("descripcion_prpre").setNombreVisual("Descripción (NOMBRE DEL PROCESO)");
		tab_procesos_cert.getColumna("descripcion_prpre").setOrden(4);

		tab_procesos_cert.getColumna("descripcion_prpre").setLectura(true);
		
	    tab_procesos_cert.getColumna("estado_proceso_prpre").setCombo(utilitario.getListaEstadoProceso());
	    tab_procesos_cert.getColumna("estado_proceso_prpre").setNombreVisual("Estado");
	    tab_procesos_cert.getColumna("estado_proceso_prpre").setOrden(6);
	    tab_procesos_cert.getColumna("estado_proceso_prpre").setLectura(true);
	    
	    tab_procesos_cert.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
	    tab_procesos_cert.getColumna("ide_geare").setLectura(true);
	    
		tab_procesos_cert.getColumna("monto_prpre").setNombreVisual("Monto");
		tab_procesos_cert.getColumna("monto_prpre").setOrden(7);
		tab_procesos_cert.getColumna("monto_prpre").setLectura(true);
		tab_procesos_cert.getColumna("ide_actual_preta").setVisible(false);
		tab_procesos_cert.getColumna("actividad_actual_prpre").setNombreVisual("Actividad Actual");
		tab_procesos_cert.getColumna("actividad_actual_prpre").setOrden(8);
		tab_procesos_cert.getColumna("actividad_actual_prpre").setLectura(true);
		tab_procesos_cert.getColumna("actividad_actual_prpre").setEstilo("width:300px");
		tab_procesos_cert.getColumna("ide_actual_geedp").setVisible(false);
		tab_procesos_cert.getColumna("responsable_actual_prpre");
		tab_procesos_cert.getColumna("responsable_actual_prpre").setNombreVisual("Responsable Actual");
		tab_procesos_cert.getColumna("responsable_actual_prpre").setOrden(9);
		tab_procesos_cert.getColumna("responsable_actual_prpre").setLectura(true);
		tab_procesos_cert.getColumna("departamento_actual_prpre").setNombreVisual("Departamento Actual");
		tab_procesos_cert.getColumna("departamento_actual_prpre").setOrden(10);
		tab_procesos_cert.getColumna("departamento_actual_prpre").setLectura(true);
		tab_procesos_cert.getColumna("departamento_actual_prpre").setEstilo("width:300px");
		tab_procesos_cert.getColumna("ide_usuario_iniciop_prpre");
		tab_procesos_cert.getColumna("ide_usuario_iniciop_prpre").setVisible(false);
		tab_procesos_cert.getColumna("ide_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "activo_preta=true");
		tab_procesos_cert.getColumna("ide_preta").setNombreVisual("Actividad a Enviar");
		tab_procesos_cert.getColumna("ide_preta").setRequerida(false);
		tab_procesos_cert.getColumna("ide_preta").setLectura(true);
		tab_procesos_cert.getColumna("ide_preta").setOrden(11);
		tab_procesos_cert.getColumna("ide_preta").setEstilo("width:300px");
		tab_procesos_cert.getColumna("observacion_prpre").setLectura(true);
		tab_procesos_cert.getColumna("observacion_prpre").setNombreVisual("Observación");
		tab_procesos_cert.getColumna("observacion_prpre").setOrden(12);
		tab_procesos_cert.getColumna("ide_tepro").setCombo(ser_generalAdm.getProveedores());
		tab_procesos_cert.getColumna("ide_tepro").setAutoCompletar();
		tab_procesos_cert.getColumna("ide_tepro").setOrden(13);
		tab_procesos_cert.getColumna("ide_tepro").setNombreVisual("Proveedor");
		tab_procesos_cert.getColumna("ide_tepro").setLectura(true);

	    tab_procesos_cert.getColumna("termino_especificacion_prpre").setCombo(utilitario.getListaTipoProcesoContracion(false));
	    tab_procesos_cert.getColumna("termino_especificacion_prpre").setNombreVisual("¿Defina si es un Bien o Servicio?");
	    tab_procesos_cert.getColumna("termino_especificacion_prpre").setOrden(5);
	    tab_procesos_cert.getColumna("termino_especificacion_prpre").setValorDefecto("NA");
	    tab_procesos_cert.getColumna("termino_especificacion_prpre").setLectura(true);

	    tab_procesos_cert.getColumna("aprueba_proyecto_prpre").setCombo(utilitario.getListaSiNo());
	    tab_procesos_cert.getColumna("aprueba_proyecto_prpre").setNombreVisual("¿Aprueba?");
	    tab_procesos_cert.getColumna("aprueba_proyecto_prpre").setOrden(14);
	    tab_procesos_cert.getColumna("aprueba_proyecto_prpre").setLectura(true);
	    tab_procesos_cert.getColumna("consta_poa_prpre").setCombo(utilitario.getListaSiNo());
	    tab_procesos_cert.getColumna("consta_poa_prpre").setNombreVisual("¿Consta en POA?");
	    tab_procesos_cert.getColumna("consta_poa_prpre").setOrden(3);
	    tab_procesos_cert.getColumna("consta_poa_prpre").setLectura(true);
	    tab_procesos_cert.getColumna("consta_pac_prpre").setCombo(utilitario.getListaSiNo());
	    tab_procesos_cert.getColumna("consta_pac_prpre").setNombreVisual("¿Consta en PAC?");
	    tab_procesos_cert.getColumna("consta_pac_prpre").setOrden(19);
	    tab_procesos_cert.getColumna("consta_pac_prpre").setLectura(true);

		tab_procesos_cert.getColumna("activo_prpre").setNombreVisual("ACTIVO");
		tab_procesos_cert.getColumna("activo_prpre").setValorDefecto("true");
		tab_procesos_cert.getColumna("activo_prpre").setLectura(true);
		tab_procesos_cert.getColumna("activo_prpre").setVisible(true);
		tab_procesos_cert.getColumna("activo_prpre").setOrden(15);

		tab_procesos_cert.getColumna("aprueba_informetm_prpre").setVisible(false);
		tab_procesos_cert.getColumna("es_favorable_informej_prpre").setVisible(false);
		tab_procesos_cert.getColumna("cumple_prpre").setVisible(false);
		tab_procesos_cert.getColumna("manifestacion_interes_prpre").setVisible(false);
		tab_procesos_cert.getColumna("es_superior_prpre").setVisible(false);
		tab_procesos_cert.getColumna("convalida_recadjudicar_prpre").setVisible(false);
		tab_procesos_cert.getColumna("se_recibe_oferta_prpre").setVisible(false);
		tab_procesos_cert.getColumna("necesario_reaperturar_prpre").setVisible(false);
		tab_procesos_cert.getColumna("recomienda_adjudicar_prpre").setVisible(false);
		tab_procesos_cert.getColumna("existe_mas_oferta_prpre").setVisible(false);
		tab_procesos_cert.getColumna("convalidacion_satisfac_prpre").setVisible(false);
		tab_procesos_cert.getColumna("error_forma_prpre").setVisible(false);
		tab_procesos_cert.getColumna("es_viable_prpre").setVisible(false);
		tab_procesos_cert.getColumna("comision_tecnica_prpre").setVisible(false);
		tab_procesos_cert.getColumna("esta_ok_prpre").setVisible(false);
		tab_procesos_cert.getColumna("solicitar_alcance_prpre").setVisible(false);
		tab_procesos_cert.getColumna("es_proceso_infima_prpre").setVisible(false);
		tab_procesos_cert.getColumna("es_favorable_informej_prpre").setLectura(true);

		tab_procesos_cert.getColumna("se_encuentra_catalogoe_prpre").setVisible(false);

		tab_procesos_cert.setCondicion("ide_prpre=-1");
		tab_procesos_cert.dibujar();
		PanelTabla pat_procesos_cert=new PanelTabla();
		pat_procesos_cert.setPanelTabla(tab_procesos_cert);
		
		agregarComponente(pat_procesos_cert);

	}
	 
	public void filtrarAnio(){

		if(com_anio.getValue()!=null){
			tab_procesos_cert.setCondicion(" ide_actual_preta=121 and extract(year from fecha_prpre)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
		}
		else{
			tab_procesos_cert.setCondicion("ide_prpre=-1");
		}
		tab_procesos_cert.ejecutarSql();		
		tab_procesos_cert.imprimirSql();
		utilitario.addUpdate("tab_procesos_cert");
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
		if(rep_reporte.getReporteSelecionado().equals("CERTIFICACION POA"))
		{			
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				
				TablaGenerica tab_anio =utilitario.consultar("SELECT ide_geani, detalle_geani FROM gen_anio where ide_geani="+com_anio.getValue());				
				String anio=tab_anio.getValor("detalle_geani");
				String secuencial_doc="CERT-"+ser_contabilidad.numeroSecuencial(par_sec_certificacion_poa)+"-"+anio;
				utilitario.agregarMensaje("Guardando secuencial ", "Nro: "+secuencial_doc);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_certificacion_poa), par_sec_certificacion_poa);
				
				p_parametros=new HashMap();		
				p_parametros.put("ide_prpac",pckUtilidades.CConversion.CInt(tab_procesos_cert.getValor("ide_prpac")));
				p_parametros.put("nro_documento",secuencial_doc);
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();				
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");	
			}		
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		else
			utilitario.agregarMensaje("No se puede insertar", "Opcion solo para emitir certificacion");

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_procesos_cert.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		//tab_procesos_cert.eliminar();
		
	}

	public Tabla getTab_procesos_cert() {
		return tab_procesos_cert;
	}

	public void setTab_procesos_cert(Tabla tab_procesos_cert) {
		this.tab_procesos_cert = tab_procesos_cert;
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


	

}
