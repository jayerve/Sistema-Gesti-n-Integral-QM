package paq_juridico;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
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
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_avance_procesal extends Pantalla {

	private Tabla tab_coactivaap = new Tabla();
	private Tabla tab_coactivaap_archivo = new Tabla();
	
	private Confirmar con_saltar_etapa=new Confirmar();
	private Confirmar con_aplicaConvenio=new Confirmar();
	private Combo com_anio=new Combo();
	
	////////////////////
	private Dialogo d_saltarEtapa = new Dialogo();
	private Combo comboEtapasAnt = new Combo();
	private Combo comboEtapasNew = new Combo();
	private AreaTexto are_txt_observacion = new AreaTexto();
	private Grid grid_saltarEtapaCab = new Grid();
	
	///////////////////
	private Dialogo d_convenioPago = new Dialogo();
	private Texto txt_tipoAcuerdo = new Texto();
	private Calendario cld_fecha = new Calendario();
	private Texto txt_plazos = new Texto();
	private Texto txt_montoPago = new Texto();
	private Grid grid_convenioPago = new Grid();
	
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_avance_procesal() {
		
		//bar_botones.limpiar();
		bar_botones.quitarBotonInsertar();
		bar_botones.quitarBotonGuardar();
		bar_botones.quitarBotonEliminar();
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		//Boton Saltar Etapa procesal
		Boton bot_saltarEtapa=new Boton();
		bot_saltarEtapa.setIcon("ui-icon-suitcase");
		bot_saltarEtapa.setValue("Saltar Etapa Judicial");
		bot_saltarEtapa.setMetodo("saltarEtapa");
		bar_botones.agregarBoton(bot_saltarEtapa);
		
		
		//Inicio Dialogo Saltar etapa judicial
		d_saltarEtapa.setId("d_saltarEtapa");
		d_saltarEtapa.setTitle("SALTAR ETAPA JUDICIAL");
		d_saltarEtapa.setHeight("40%");
		d_saltarEtapa.setWidth("45%");
		
		Grid grid_saltarEtapa = new Grid();
		grid_saltarEtapaCab.setColumns(1);
		grid_saltarEtapa.setColumns(2);

		comboEtapasAnt.setId("comboEtapasAnt");
		comboEtapasAnt.setCombo("SELECT ide_juepr,detalle_juepr FROM jur_etapa_procesal;");
		comboEtapasAnt.setDisabled(true);
		grid_saltarEtapa.getChildren().add(new Etiqueta("Etapa Actual:"));
		grid_saltarEtapa.getChildren().add(comboEtapasAnt);
		
		comboEtapasNew.setId("comboEtapasNew");
		comboEtapasNew.setCombo("SELECT ide_juepr,detalle_juepr FROM jur_etapa_procesal;");
		comboEtapasNew.setDisabled(true);
		grid_saltarEtapa.getChildren().add(new Etiqueta("Nueva Etapa:"));
		grid_saltarEtapa.getChildren().add(comboEtapasNew);
		
		are_txt_observacion.setId("are_txt_observacion");
		are_txt_observacion.setAutoResize(true);
		are_txt_observacion.setStyle("width:350px; height:70px");
		grid_saltarEtapa.getChildren().add(new Etiqueta("Info del proceso:"));
		grid_saltarEtapa.getChildren().add(are_txt_observacion);
		
		
		
		con_saltar_etapa.setId("con_saltar_etapa");
		con_saltar_etapa.setMessage("Esta Seguro de Saltar la Etapa Judicial");
		con_saltar_etapa.setTitle("Confirmación...");
		con_saltar_etapa.getBot_aceptar().setMetodo("saltarEtapa");
		agregarComponente(con_saltar_etapa);
		
		//Boton convenio pago
		Boton bot_convenioPago=new Boton();
		bot_convenioPago.setIcon("ui-icon-check");
		bot_convenioPago.setValue("Aplica Convenio Pago");
		bot_convenioPago.setMetodo("AplicaConvenio");
		bar_botones.agregarBoton(bot_convenioPago);
		
		//Inicio Dialogo d_convenioPago
		d_convenioPago.setId("d_convenioPago");
		d_convenioPago.setTitle("CONVENIO DE PAGO");
		d_convenioPago.setHeight("45%");
		d_convenioPago.setWidth("40%");
		
		
		grid_convenioPago.setColumns(2);
		
		txt_tipoAcuerdo.setId("txt_tipoAcuerdo");
		txt_tipoAcuerdo.setStyle("width:350px;");
		grid_convenioPago.getChildren().add(new Etiqueta("Tipo de Acuerdo:"));
		grid_convenioPago.getChildren().add(txt_tipoAcuerdo);
		
		cld_fecha.setId("cld_fecha");
		cld_fecha.setFechaActual();
		//cld_fecha.setStyle("width:350px; height:70px");
		grid_convenioPago.getChildren().add(new Etiqueta("Fecha:"));
		grid_convenioPago.getChildren().add(cld_fecha);
		
		txt_plazos.setId("txt_plazos");
		txt_plazos.setStyle("width:100px;");
		txt_plazos.setSoloEnteros();
		txt_plazos.setMetodoChange("calcularMonto");
		grid_convenioPago.getChildren().add(new Etiqueta("Plazos:"));
		grid_convenioPago.getChildren().add(txt_plazos);
		
		txt_montoPago.setId("txt_montoPago");
		txt_montoPago.setStyle("width:100px;");
		txt_montoPago.setSoloNumeros();
		grid_convenioPago.getChildren().add(new Etiqueta("Monto a Pagar:"));
		grid_convenioPago.getChildren().add(txt_montoPago);
		
		d_convenioPago.getBot_aceptar().setMetodo("AplicaConvenio");
		d_convenioPago.setDialogo(grid_convenioPago);
		agregarComponente(d_convenioPago);
		
		con_aplicaConvenio.setId("con_aplicaConvenio");
		con_aplicaConvenio.setMessage("Esta Seguro de registrar la informacion del convenio de pago");
		con_aplicaConvenio.setTitle("Confirmación...");
		con_aplicaConvenio.getBot_aceptar().setMetodo("AplicaConvenio");
		agregarComponente(con_aplicaConvenio);
		////////////////////////////////
		
		// TODO Auto-generated constructor stub
		tab_coactivaap.setId("tab_coactivaap");
		tab_coactivaap.setTabla("jur_coactiva", "ide_jucoa", 1);
		tab_coactivaap.getColumna("fecha_jucoa").setValorDefecto(utilitario.getFechaActual());
		tab_coactivaap.getColumna("ide_juepr").setCombo("jur_etapa_procesal", "ide_juepr", "detalle_juepr", "");
		tab_coactivaap.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_coactivaap.getColumna("ide_recli").setAutoCompletar();
		tab_coactivaap.getColumna("activo_jucoa").setValorDefecto("true");
		tab_coactivaap.setCondicion("ide_geani=-1"); 
		tab_coactivaap.setTipoFormulario(true);
		tab_coactivaap.setLectura(true);
		tab_coactivaap.getGrid().setColumns(4);
		tab_coactivaap.agregarRelacion(tab_coactivaap_archivo);
		tab_coactivaap.dibujar();

		PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_coactivaap);
        
        tab_coactivaap_archivo.setId("tab_coactivaap_archivo");
        tab_coactivaap_archivo.setTabla("jur_coactiva_archivo", "ide_jucar", 2);
        tab_coactivaap_archivo.setTipoFormulario(true);
        tab_coactivaap_archivo.getGrid().setColumns(4);
        tab_coactivaap_archivo.getColumna("fecha_jucar").setValorDefecto(utilitario.getFechaActual());
        tab_coactivaap_archivo.getColumna("descripcion_jucar").setLongitud(50);
        tab_coactivaap_archivo.getColumna("activo_jucar").setValorDefecto("true");
        tab_coactivaap_archivo.getColumna("adjunto_jucar").setUpload("coactivas");
        tab_coactivaap_archivo.getColumna("activo_jucar").setLectura(true);
        tab_coactivaap_archivo.getColumna("ide_jucar").setNombreVisual("CODIGO");
        tab_coactivaap_archivo.getColumna("ide_jucar").setOrden(1);
        tab_coactivaap_archivo.getColumna("fecha_jucar").setNombreVisual("FECHA");
        tab_coactivaap_archivo.getColumna("fecha_jucar").setOrden(2);
        tab_coactivaap_archivo.getColumna("descripcion_jucar").setNombreVisual("DESCRIPCION");
        tab_coactivaap_archivo.getColumna("descripcion_jucar").setOrden(3);
        tab_coactivaap_archivo.getColumna("activo_jucar").setNombreVisual("ACTIVO");
        tab_coactivaap_archivo.getColumna("activo_jucar").setOrden(4);
        tab_coactivaap_archivo.getColumna("adjunto_jucar").setNombreVisual("ANEXO");
        tab_coactivaap_archivo.getColumna("adjunto_jucar").setOrden(5);
        tab_coactivaap_archivo.setRecuperarLectura(true);
        tab_coactivaap_archivo.setMostrarNumeroRegistros(false);
        tab_coactivaap_archivo.dibujar();
        

		grid_saltarEtapaCab.getChildren().add(grid_saltarEtapa);
		grid_saltarEtapaCab.getChildren().add(tab_coactivaap_archivo);
		
		
		d_saltarEtapa.getBot_aceptar().setMetodo("saltarEtapa");
		d_saltarEtapa.setDialogo(grid_saltarEtapaCab);
		agregarComponente(d_saltarEtapa);
        
        Division div_division = new Division();

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);


	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_coactivaap.setCondicion("ide_geani="+com_anio.getValue());
			tab_coactivaap.ejecutarSql();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void saltarEtapa(){

		if(d_saltarEtapa.isVisible())
		{
			if(pckUtilidades.CConversion.CStr(are_txt_observacion.getValue()).length()>0)
			{
				d_saltarEtapa.cerrar();
				con_saltar_etapa.dibujar();	
				utilitario.addUpdate("con_guardar_titulo");
			}
			else
			{
				utilitario.agregarMensajeInfo("Aceptar", "Favor ingrese alguna informacion del proceso.");
				return;
			}
			
		}
		else if (con_saltar_etapa.isVisible()){
			
			tab_coactivaap.modificar(tab_coactivaap.getFilaActual());
			tab_coactivaap.setValor("observacion_jucoa", are_txt_observacion.getValue()+"");
			tab_coactivaap.setValor("ide_juepr", comboEtapasNew.getValue()+"");			
			String detArc=tab_coactivaap_archivo.getValor("descripcion_jucar");
			tab_coactivaap_archivo.setValor("descripcion_jucar", detArc+" - "+are_txt_observacion.getValue());
			
			utilitario.addUpdate("tab_coactivaap");
			
			tab_coactivaap.guardar();
			tab_coactivaap_archivo.guardar();
			guardarPantalla();
			
			con_saltar_etapa.cerrar();

		}
		else
		{
			if(pckUtilidades.CConversion.CInt(tab_coactivaap.getValor("ide_juepr"))<6)
			{
				comboEtapasAnt.setValue(tab_coactivaap.getValor("ide_juepr"));
				comboEtapasNew.setValue(pckUtilidades.CConversion.CInt(tab_coactivaap.getValor("ide_juepr"))+1);
				
				utilitario.addUpdate("comboEtapasAnt");
				utilitario.addUpdate("comboEtapasNew");
				
				tab_coactivaap_archivo.insertar();
				d_saltarEtapa.dibujar();
			}
			else
			{
				utilitario.agregarMensajeInfo("Saltar Etapa Judicial", "No se puede saltar ya que esta se encuentra en la ultima etapa judicial.");
				return;
			}
		}
	}
	
	public void calcularMonto(AjaxBehaviorEvent evt){
		
		//tab_detalle_movimiento.modificar(evt);
		
		double cuantia = pckUtilidades.CConversion.CDbl_2(tab_coactivaap.getValor("cuantia_jucoa"));
		double plazo= pckUtilidades.CConversion.CDbl_2(txt_plazos.getValue());
		double monto=cuantia/plazo;
		txt_montoPago.setValue(monto);
		utilitario.addUpdate("txt_montoPago");
	}

	public void AplicaConvenio(){

		if(d_convenioPago.isVisible())
		{

			if(pckUtilidades.CConversion.CStr(txt_tipoAcuerdo.getValue()).length()<1)
			{
				utilitario.agregarMensajeInfo("Aceptar", "Favor ingrese el tipo de acuerdo.");
				return;
			}
			
			if(pckUtilidades.CConversion.CInt(txt_plazos.getValue())==0)
			{
				utilitario.agregarMensajeInfo("Aceptar", "Favor ingrese el plazo.");
				return;
			}
			
			if(pckUtilidades.CConversion.CDbl_2(txt_montoPago.getValue())==0)
			{
				utilitario.agregarMensajeInfo("Aceptar", "Favor ingrese el monto.");
				return;
			}
			
			d_convenioPago.cerrar();
			con_aplicaConvenio.dibujar();
			utilitario.addUpdate("con_aplicaConvenio");
			
		}
		else if (con_aplicaConvenio.isVisible()){
			
			tab_coactivaap.modificar(tab_coactivaap.getFilaActual());
			tab_coactivaap.setValor("aplica_convenio_pago_jucoa", "true");
			tab_coactivaap.setValor("tipo_convenio_jucoa", txt_tipoAcuerdo.getValue()+"");
			tab_coactivaap.setValor("fecha_convenio_jucoa", cld_fecha.getFecha()+"");
			tab_coactivaap.setValor("plazos_convenio_jucoa", txt_plazos.getValue()+"");
			tab_coactivaap.setValor("monto_convenio_jucoa", txt_montoPago.getValue()+"");
			utilitario.addUpdate("tab_coactivaap");
			
			tab_coactivaap.guardar();
			guardarPantalla();
			
			con_aplicaConvenio.cerrar();

		}
		else
		{
			d_convenioPago.dibujar();
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
	

	
	public Tabla getTab_coactivaap() {
		return tab_coactivaap;
	}

	public void setTab_coactivaap(Tabla tab_coactivaap) {
		this.tab_coactivaap = tab_coactivaap;
	}

	public Combo getComboEtapasAnt() {
		return comboEtapasAnt;
	}

	public void setComboEtapasAnt(Combo comboEtapasAnt) {
		this.comboEtapasAnt = comboEtapasAnt;
	}

	public Combo getComboEtapasNew() {
		return comboEtapasNew;
	}

	public void setComboEtapasNew(Combo comboEtapasNew) {
		this.comboEtapasNew = comboEtapasNew;
	}

	public Tabla getTab_coactivaap_archivo() {
		return tab_coactivaap_archivo;
	}

	public void setTab_coactivaap_archivo(Tabla tab_coactivaap_archivo) {
		this.tab_coactivaap_archivo = tab_coactivaap_archivo;
	}

	
	

}
