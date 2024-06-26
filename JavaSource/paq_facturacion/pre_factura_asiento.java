package paq_facturacion;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

import org.apache.poi.hssf.record.CalcCountRecord;
import org.primefaces.event.SelectEvent;
import framework.componentes.AutoCompletar;
import framework.componentes.Etiqueta;


public class pre_factura_asiento extends Pantalla

{

	private Tabla tab_factura = new Tabla();
	private Tabla tab_factura_asiento=new Tabla();
	private AutoCompletar aut_factura=new AutoCompletar();
	private Dialogo crear_cliente_dialogo=new Dialogo();
	private Tabla tab_cliente=new Tabla();
	private Tabla tab_direccion= new Tabla();

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}
	//SELCCION TABLA
	private SeleccionTabla set_pantalla_dias= new SeleccionTabla();
	private SeleccionTabla set_insertarbodega = new SeleccionTabla();
	private SeleccionTabla set_pantallacliente= new SeleccionTabla();
	private SeleccionTabla set_actualizar_cliente = new SeleccionTabla();
	private Confirmar con_guardar_cliente=new Confirmar();

	private Etiqueta eti_total= new Etiqueta();


	//CALENDARIO
	private SeleccionCalendario sec_rango_fechas = new SeleccionCalendario();
	private String srt_fecha_inicio;
	private String srt_fecha_fin;
	//private double dou_por_iva=0.12;
	double dou_base_no_iva=0;
	double dou_base_cero=0;
	double dou_base_aprobada=0;
	double dou_valor_iva=0;
	double dou_total=0;
	private String valor;
	private String mensaje;
	private List<Fila> lis_fechas_seleccionadas;

	private Dialogo dia_valor=new Dialogo();
	private Texto tex_valor=new Texto();
	String str_smaterial_seleccionado;
	//REPORTE
		private Map p_parametros = new HashMap();
		private Reporte rep_reporte = new Reporte();
		private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
		private Map map_parametros = new HashMap();

	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	
	
	
	public pre_factura_asiento() {
		
		bar_botones.quitarBotonEliminar();
		bar_botones.quitarBotonGuardar();
		bar_botones.quitarBotonInsertar();
		tab_factura.setHeader("FACTURA");
		tab_factura.setId("tab_factura");
		tab_factura.setTabla("fac_factura", "ide_fafac", 1);
		tab_factura.setGenerarPrimaria(false);
		tab_factura.getColumna("ide_fafac").setLectura(true);
		//para q no se dibuje antes q seleccione el autocompletar
		tab_factura.getColumna("ide_fadaf").setVisible(false);
		tab_factura.getColumna("ide_comov").setVisible(false);
		tab_factura.getColumna("ide_sucu").setCombo("sis_sucursal", "ide_sucu", "nom_sucu", "");
		tab_factura.getColumna("ide_tetid").setVisible(false);
		tab_factura.getColumna("ide_tedar").setVisible(false);
		tab_factura.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_factura.getColumna("ide_geins").setCombo("gen_institucion", "ide_geins", "detalle_geins", "");
		tab_factura.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_factura.getColumna("ide_recli").setAutoCompletar();
		tab_factura.getColumna("ide_recli").setLongitud(100);
		tab_factura.getColumna("ide_recli").setLectura(true);
		tab_factura.getColumna("ide_falug").setCombo("fac_lugar", "ide_falug", "detalle_lugar_falug", "");
		tab_factura.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		tab_factura.getColumna("ide_retip").setValorDefecto(utilitario.getVariable("p_tipo_cobro_factura"));
		tab_factura.getColumna("ide_retip").setValorDefecto("4");
		tab_factura.getColumna("ide_gtemp").setCombo(ser_empleado.servicioEmpleadosActivos("true,false"));
		//TOTALES DE COLOR ROJO--ESTILO DE COLOR ROJO Y NEGRILLA
		tab_factura.getColumna("base_no_iva_fafac").setEtiqueta();
		tab_factura.getColumna("base_no_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.setCampoOrden("ide_fafac desc");
		tab_factura.getColumna("base_cero_fafac").setEtiqueta();
		tab_factura.getColumna("base_cero_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("base_aprobada_fafac").setEtiqueta();
		tab_factura.getColumna("base_aprobada_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("valor_iva_fafac").setEtiqueta();
		tab_factura.getColumna("valor_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("total_fafac").setEtiqueta();
		tab_factura.getColumna("total_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.getColumna("secuencial_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_factura.getColumna("secuencial_fafac").setEtiqueta();
		tab_factura.getColumna("fecha_vencimiento_fafac").setVisible(false);
		tab_factura.getColumna("fecha_emision_fafac").setVisible(false);
		tab_factura.getColumna("direccion_fafac").setVisible(false);
		tab_factura.getColumna("responsable_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("codigo_faclinea_fafac").setLectura(true);
		tab_factura.getColumna("activo_fafac").setValorDefecto("true");
		tab_factura.getColumna("activo_fafac").setLectura(true);
		tab_factura.getColumna("conciliado_fafac").setLectura(true);
		tab_factura.getColumna("num_comprobante_fafac").setVisible(false);
		tab_factura.getColumna("fecha_transaccion_fafac").setValorDefecto(utilitario.getFechaActual());
		tab_factura.getColumna("base_no_iva_fafac").setVisible(false);
		tab_factura.setCondicion("extract(year from fecha_transaccion_fafac)=2017 and extract(month from fecha_transaccion_fafac)=12");
		tab_factura.agregarRelacion(tab_factura_asiento);
		tab_factura.setLectura(true);
		tab_factura.setRows(10);
		tab_factura.dibujar();

		PanelTabla pat_factura=new PanelTabla();
		pat_factura.setPanelTabla(tab_factura);

		// TABLA 2
		tab_factura_asiento.setId("tab_factura_asiento");
		tab_factura_asiento.setTabla("cont_factura_asiento", "ide_cofaa", 2);
		tab_factura_asiento.getColumna("ide_conac").setCombo("cont_nombre_asiento_contable", "ide_conac", "detalle_conac", "");
		tab_factura_asiento.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_factura_asiento.setLectura(true);
		tab_factura_asiento.dibujar();


		PanelTabla pat_factura_asiento= new PanelTabla();
		pat_factura_asiento.setMensajeWarn("Factura Asiento");
		pat_factura_asiento.setPanelTabla(tab_factura_asiento);

	
		Division div_division=new Division();
		div_division.dividir2(pat_factura, pat_factura_asiento, "50%", "h");
		agregarComponente(div_division);
	}
	

	
	@Override
	protected String guardarPantalla() {
		// TODO Auto-generated method stub
		return super.guardarPantalla();
	}
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}
	public Tabla getTab_factura() {
		return tab_factura;
	}
	public void setTab_factura(Tabla tab_factura) {
		this.tab_factura = tab_factura;
	}
	public Tabla getTab_factura_asiento() {
		return tab_factura_asiento;
	}
	public void setTab_factura_asiento(Tabla tab_factura_asiento) {
		this.tab_factura_asiento = tab_factura_asiento;
	}
	


}
