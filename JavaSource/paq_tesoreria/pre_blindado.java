package paq_tesoreria;

import java.io.File;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_anticipos.ejb.ServicioAnticipo;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;


public class pre_blindado extends Pantalla{
	
	private Tabla tab_blindado = new Tabla();
	private Tabla tab_detalle_blindado=new Tabla();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();

	private Combo com_anio=new Combo();
    public static String par_modulosec;
    private String ide_empleado="";

    /*double valor_tefec=0;
	double valor_tchq=0;
	double dou_total_billetes=0;
	double dou_total_monedas=0;
	double dou_total_cheques=0;*/
	double duo_valor_iva=0.12;
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina ) utilitario.instanciarEJB(ServicioNomina.class);	
	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);
	

	public pre_blindado(){
		
		par_modulosec=utilitario.getVariable("p_modulo_secuencial_cierre_caja");
		ide_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		if(ide_empleado==null ||ide_empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro");
			return;
		}
		
		duo_valor_iva=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_iva")); //0.14 de IVA
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_blindado.setId("tab_blindado");
		tab_blindado.setHeader("DATOS DEL BLINDADO");
		tab_blindado.setTabla("tes_blindado", "ide_tebli", 1);
		tab_blindado.getColumna("fecha_tebli").setValorDefecto(utilitario.getFechaActual());
		tab_blindado.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_blindado.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_blindado.getColumna("ide_gtemp").setLectura(true);
		tab_blindado.getColumna("ide_gtemp").setAutoCompletar();
		tab_blindado.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");

		tab_blindado.getColumna("total_efectivo_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("total_efectivo_tebli").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_blindado.getColumna("total_efectivo_tebli").setEtiqueta();
		tab_blindado.getColumna("total_cheque_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("total_cheque_tebli").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_blindado.getColumna("total_cheque_tebli").setEtiqueta();
		tab_blindado.getColumna("fundas_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("fundas_tebli").setMetodoChange("calcular");
		tab_blindado.getColumna("total_transporte_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("total_transporte_tebli").setMetodoChange("calcular");
		tab_blindado.getColumna("subtotal_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("subtotal_tebli").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_blindado.getColumna("subtotal_tebli").setEtiqueta();
		tab_blindado.getColumna("iva_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("iva_tebli").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_blindado.getColumna("iva_tebli").setEtiqueta();
		tab_blindado.getColumna("total_pagar_tebli").setValorDefecto("0.00");
		tab_blindado.getColumna("total_pagar_tebli").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_blindado.getColumna("total_pagar_tebli").setEtiqueta();
		tab_blindado.getColumna("activo_tebli").setValorDefecto("true");
		tab_blindado.setCondicion("ide_geani=-1"); 
		tab_blindado.setTipoFormulario(true);
		tab_blindado.getGrid().setColumns(4);
		tab_blindado.agregarRelacion(tab_detalle_blindado);
		tab_blindado.dibujar();
		PanelTabla pat_convenio=new PanelTabla();
		pat_convenio.setPanelTabla(tab_blindado);
		
		tab_detalle_blindado.setId("tab_detalle_blindado");
		tab_detalle_blindado.setIdCompleto("tab_tabulador:tab_detalle_blindado");
		tab_detalle_blindado.setHeader("CIERRES DE CAJA");
		tab_detalle_blindado.setTabla("tes_detalle_blindado", "ide_tedbl", 2);
		tab_detalle_blindado.getColumna("fecha_tedbl").setValorDefecto(utilitario.getFechaActual());
		tab_detalle_blindado.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_detalle_blindado.getColumna("ide_gtemp").setAutoCompletar();
		tab_detalle_blindado.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_detalle_blindado.getColumna("ide_tecaj").setCombo("SELECT ide_tecaj, (detalle_tecaj || ' ' || coalesce(nom_sucu,'')) as detalle FROM tes_caja c "
																+" left join sis_sucursal s on s.ide_sucu=c.ide_sucu "
																+" order by nom_sucu");
		tab_detalle_blindado.getColumna("ide_tecaj").setAutoCompletar();
		tab_detalle_blindado.getColumna("activo_tedbl").setValorDefecto("true");
		tab_detalle_blindado.getColumna("valor_cheque_tedbl").setMetodoChange("re_calcular"); 
		tab_detalle_blindado.getColumna("valor_efectivo_tedbl").setMetodoChange("re_calcular");
		tab_detalle_blindado.setColumnaSuma("valor_cheque_tedbl,valor_efectivo_tedbl"); 
		tab_detalle_blindado.setRows(10);
		tab_detalle_blindado.dibujar();
		PanelTabla pat_det_convenio=new PanelTabla();
		pat_det_convenio.setPanelTabla(tab_detalle_blindado);
		
		tab_tabulador.agregarTab("CIERRES DE CAJA", pat_det_convenio);
		

		Division div_divi=new Division();
		div_divi.dividir2(pat_convenio, tab_tabulador, "50%", "H");
		
		agregarComponente(div_divi);

  		
		inicializaCalendario();
		
	}
	
	public void seleccionaElAnio(){
		if(com_anio.getValue()!=null){
			tab_blindado.setCondicion("ide_geani="+com_anio.getValue());
			tab_blindado.ejecutarSql();
			tab_detalle_blindado.ejecutarValorForanea(tab_blindado.getValorSeleccionado());
			//actualizaDetalles();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
			tab_blindado.setCondicion("ide_geani=-1");
			tab_blindado.ejecutarSql();
			tab_detalle_blindado.ejecutarValorForanea(tab_blindado.getValorSeleccionado());
			//actualizaDetalles();
		}
	}
	
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		//sel_calendario.getBot_aceptar().setMetodo("generarCierre");
		agregarComponente(sel_calendario);
	}
	
	public void calcular(AjaxBehaviorEvent evt) {
		tab_blindado.modificar(evt); // Siempre es la primera linea
		
		double fundas=pckUtilidades.CConversion.CDbl_2(tab_blindado.getValor("fundas_tebli"));
		double total_transporte=pckUtilidades.CConversion.CDbl_2(tab_blindado.getValor("total_transporte_tebli"));
		double subTotal=(fundas+total_transporte);
		double iva=pckUtilidades.CConversion.CDbl_2(subTotal*duo_valor_iva);
		
		tab_blindado.setValor("subtotal_tebli",subTotal+"");
		tab_blindado.setValor("iva_tebli",iva+"");
		tab_blindado.setValor("total_pagar_tebli",(subTotal+iva)+"");
		tab_blindado.guardar();
		guardarPantalla();
	
	}
	
	public void re_calcular(AjaxBehaviorEvent evt) {
		tab_detalle_blindado.modificar(evt); // Siempre es la primera linea
		tab_detalle_blindado.guardar();
		guardarPantalla();
		actualizaDetalles();
	}
	
	public void actualizaDetalles()
	{
		/*String ide_tecca="-1";
		
		if(tab_cierre.getValor("ide_tecca")!=null)
		{
			ide_tecca=tab_cierre.getValor("ide_tecca");
		}
		else
			return;
		*/
		
		
		double valorEfectivo=tab_detalle_blindado.getSumaColumna("valor_efectivo_tedbl");
		double valorCheque=tab_detalle_blindado.getSumaColumna("valor_cheque_tedbl");
		
		tab_blindado.setValor("total_efectivo_tebli",valorEfectivo+"");
		tab_blindado.setValor("total_cheque_tebli",valorCheque+"");
		tab_blindado.modificar(tab_blindado.getFilaActual());//para que haga el update
		tab_blindado.guardar();
		
		utilitario.addUpdateTabla(tab_blindado, "total_efectivo_tebli,total_cheque_tebli", "");
		utilitario.addUpdate("tab_detalle_blindado");
		
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
		}
		
		if(tab_blindado.isFocus())
		{
			tab_blindado.insertar();
			tab_blindado.setValor("secuencial_tebli", ser_contabilidad.numeroSecuencial(par_modulosec));
			tab_blindado.setValor("ide_coest", "2");
			tab_blindado.setValor("ide_geani", com_anio.getValue()+"");
			tab_blindado.setValor("ide_gtemp", ide_empleado);
			//actualizaDetalles();
			
		}
	
		if(tab_detalle_blindado.isFocus())
		{
			tab_detalle_blindado.insertar();
			tab_detalle_blindado.setValor("ide_coest", "2");
			tab_detalle_blindado.setValor("ide_gtemp", ide_empleado);
			//actualizaDetalles();
			
		}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_blindado.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec), par_modulosec);
		}
		
		if (tab_blindado.guardar())
		{
			if (tab_detalle_blindado.guardar())
				guardarPantalla();			 
		  
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
    public void inicio() {
        super.inicio();
        // TODO Auto-generated method stub
        if (tab_blindado.isFocus()){
			//actualizaDetalles();
		}
    }
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
    	super.siguiente();
    	if (tab_blindado.isFocus()){
			//actualizaDetalles();
		}
    }
    @Override
    public void atras() {
        super.atras();
        // TODO Auto-generated method stub
        if (tab_blindado.isFocus()){
			//actualizaDetalles();
		}
    }
    
	@Override
	public void fin()
	{
		super.fin();
		if (tab_blindado.isFocus()){
			//actualizaDetalles();
		}
	}

	public Tabla getTab_blindado() {
		return tab_blindado;
	}

	public void setTab_blindado(Tabla tab_blindado) {
		this.tab_blindado = tab_blindado;
	}

	public Tabla getTab_detalle_blindado() {
		return tab_detalle_blindado;
	}

	public void setTab_detalle_blindado(Tabla tab_detalle_blindado) {
		this.tab_detalle_blindado = tab_detalle_blindado;
	}

	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}

	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}

	
	

}
