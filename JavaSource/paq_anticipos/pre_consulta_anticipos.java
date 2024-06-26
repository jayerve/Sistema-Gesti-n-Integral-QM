package paq_anticipos;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
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
import framework.componentes.Upload;


public class pre_consulta_anticipos extends Pantalla {

	private Tabla tab_anticipo=new Tabla();
	private Tabla tab_amortizacion=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Consulta con_abonos_anticipo=new Consulta();
	private Dialogo dia_datos_precancelacion=new Dialogo();
	private Tabla tab_datos_precancelacion=new Tabla();

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_consulta_anticipos() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");

		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");


		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);

		//  ANTICIPOS (division 1)

		tab_anticipo.setId("tab_anticipo");
		tab_anticipo.setTabla("NRH_ANTICIPO", "IDE_NRANT", 1);

		tab_anticipo.getColumna("IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP3").setVisible(false);
		tab_anticipo.getColumna("IDE_GTEMP").setVisible(false);
		tab_anticipo.getColumna("ANTICIPO_NRANT").setVisible(false);
		tab_anticipo.getColumna("IDE_NRMOA").setVisible(false);
		
		
		tab_anticipo.getColumna("CALIFICADO_NRANT").setCheck();
		tab_anticipo.getColumna("ANULADO_NRANT").setCheck();
		tab_anticipo.getColumna("APROBADO_NRANT").setCheck();
		tab_anticipo.getColumna("ACTIVO_NRANT").setCheck();

		tab_anticipo.setCondicion("ANTICIPO_NRANT=1 AND IDE_GTEMP=-1");
		tab_anticipo.setCampoOrden("IDE_NRANT DESC");
		tab_anticipo.setLectura(true);
		tab_anticipo.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anticipo);



		tab_amortizacion.setId("tab_amortizacion");
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", 3);
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setCheck();
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setCheck();
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("IDE_NRRUB").setLectura(true);
		tab_amortizacion.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","");
		tab_amortizacion.getColumna("CAPITAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("INTERES_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("NRO_CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("FECHA_CANCELADO_NRAMO").setLectura(true);
		tab_amortizacion.setLectura(true);
		tab_amortizacion.setColumnaSuma("CUOTA_NRAMO,PRINCIPAL_NRAMO");
		tab_amortizacion.setCampoOrden("FECHA_VENCIMIENTO_NRAMO ASC");
		tab_amortizacion.setCondicion("IDE_NRAMO=-1");

		tab_amortizacion.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_amortizacion);

		ItemMenu itm_ver_cancelacion=new ItemMenu();
		itm_ver_cancelacion.setIcon("ui-contact");
		itm_ver_cancelacion.setMetodo("verDatosPreCancelacion");
		itm_ver_cancelacion.setValue("Ver Datos Pre-Can");
		pat_panel3.getMenuTabla().getChildren().add(itm_ver_cancelacion);

		ItemMenu itm_ver_abonos=new ItemMenu();
		itm_ver_abonos.setIcon("ui-contact");
		itm_ver_abonos.setMetodo("verDatosAbonos");
		itm_ver_abonos.setValue("Ver Abonos");
		pat_panel3.getMenuTabla().getChildren().add(itm_ver_abonos);



	

		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, pat_panel3, "50%", "H");
		agregarComponente(div_division);


		//   PARA VER DATOS PRE CANCELACION

		dia_datos_precancelacion.setId("dia_datos_precancelacion");
		dia_datos_precancelacion.setWidth("60%");
		dia_datos_precancelacion.setHeight("60%");


		tab_datos_precancelacion.setId("tab_datos_precancelacion");
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=1 " +
				"and amo.ide_nrani=-1 ");
		tab_datos_precancelacion.setCampoPrimaria("IDE_NRPRE");
		tab_datos_precancelacion.setNumeroTabla(13);
		tab_datos_precancelacion.getColumna("PATH_FOTO_NRPRE").setVisible(false);
		tab_datos_precancelacion.getColumna("IDE_NRPRE").setVisible(false);
		tab_datos_precancelacion.setTipoFormulario(false);
		tab_datos_precancelacion.setLectura(true);
		tab_datos_precancelacion.setMostrarNumeroRegistros(false);
		tab_datos_precancelacion.dibujar();

		Grid gri_datos_pre=new Grid();
		gri_datos_pre.getChildren().add(tab_datos_precancelacion);
		gri_datos_pre.setStyle("width:" + (dia_datos_precancelacion.getAnchoPanel() - 5) + "px;height:" + dia_datos_precancelacion.getAltoPanel() + "px;overflow: auto;display: block;");

		dia_datos_precancelacion.setDialogo(gri_datos_pre);
		dia_datos_precancelacion.getBot_aceptar().setRendered(false);
		agregarComponente(dia_datos_precancelacion);


		// PARA VER DATOS DE ABONOS DE ANTICIPOS

		con_abonos_anticipo.setId("con_abonos_anticipo");
		con_abonos_anticipo.setConsulta("select IDE_NRANA, " +
				"SALDO_ANTERIOR_NRANA,VALOR_ABONO_NRANA,MONTO_PENDIENTE_NRANA,PLAZO_NRANA,FECHA_PAGO_NRANA,DETALLE_GEINS,DOC_DEPOSITO_NRANA,FECHA_DEPOSITO_NRANA "+
				"from NRH_ANTICIPO_ABONO ana " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=ana.IDE_GEINS " +
				"where IDE_NRANT=-1", "IDE_NRANA");
		con_abonos_anticipo.getTab_consulta_dialogo().setColumnaSuma("VALOR_ABONO_NRANA");
		con_abonos_anticipo.getBot_aceptar().setMetodo("aceptarVerAbonos"); 
		agregarComponente(con_abonos_anticipo);

		///////////////////////////////////////////////////////////////




		//UIViewRoot newView = FacesContext.getCurrentInstance().getApplication().getViewHandler().createView(FacesContext.getCurrentInstance(), "/index.jsf"); FacesContext.getCurrentInstance().setViewRoot(newView);
		//utilitario.addUpdate("@all");
	}


	public void aceptarVerAbonos(){
		con_abonos_anticipo.cerrar();
	}

	public void verDatosAbonos(){
		con_abonos_anticipo.getTab_consulta_dialogo().setSql("select IDE_NRANA, " +
				"SALDO_ANTERIOR_NRANA,VALOR_ABONO_NRANA,MONTO_PENDIENTE_NRANA,PLAZO_NRANA,FECHA_PAGO_NRANA,DETALLE_GEINS,DOC_DEPOSITO_NRANA,FECHA_DEPOSITO_NRANA " +
				"from NRH_ANTICIPO_ABONO ana " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=ana.IDE_GEINS " +
				"where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
		con_abonos_anticipo.getTab_consulta_dialogo().ejecutarSql();
		if (con_abonos_anticipo.getTab_consulta_dialogo().getTotalFilas()>0){
			con_abonos_anticipo.dibujar();
		}else{
			utilitario.agregarMensajeInfo("El anticipo seleccionado", "no tiene abonos realizados");
		}
	}
	public void verDatosPreCancelacion(){
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=1 " +
				"and amo.ide_nrani="+tab_amortizacion.getValor("IDE_NRANI"));
		tab_datos_precancelacion.ejecutarSql();
		if (tab_datos_precancelacion.getTotalFilas()>0){
			dia_datos_precancelacion.setTitle("DATOS PRECANCELACION ANTICIPO");
			dia_datos_precancelacion.dibujar();
		}else{
			utilitario.agregarMensajeInfo("El anticipo seleccionado", "no tiene cuota pre canceladas");
		}
	}


	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);

		tab_anticipo.setCondicion("ANTICIPO_NRANT=1 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();

		tab_amortizacion.setCondicion("IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+")");
		tab_amortizacion.ejecutarSql();
		
		
		
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_anticipo.limpiar();
		tab_amortizacion.limpiar();
		aut_empleado.limpiar();
		tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(0);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(0);

	}



	public Tabla getTab_anticipo() {
		return tab_anticipo;
	}

	public void setTab_anticipo(Tabla tab_anticipo) {
		this.tab_anticipo = tab_anticipo;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
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


	public Tabla getTab_amortizacion() {
		return tab_amortizacion;
	}


	public void setTab_amortizacion(Tabla tab_amortizacion) {
		this.tab_amortizacion = tab_amortizacion;
	}


	public Consulta getCon_abonos_anticipo() {
		return con_abonos_anticipo;
	}


	public void setCon_abonos_anticipo(Consulta con_abonos_anticipo) {
		this.con_abonos_anticipo = con_abonos_anticipo;
	}


	public Dialogo getDia_datos_precancelacion() {
		return dia_datos_precancelacion;
	}


	public void setDia_datos_precancelacion(Dialogo dia_datos_precancelacion) {
		this.dia_datos_precancelacion = dia_datos_precancelacion;
	}


	public Tabla getTab_datos_precancelacion() {
		return tab_datos_precancelacion;
	}


	public void setTab_datos_precancelacion(Tabla tab_datos_precancelacion) {
		this.tab_datos_precancelacion = tab_datos_precancelacion;
	}


}

