
package paq_tesoreria;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_consulta_asientos extends Pantalla{
	
	private AutoCompletar aut_asiento_tipo=new AutoCompletar(); 
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Tabla tab_asiento_consulta = new Tabla();
	private Tabla tab_factura_consulta = new Tabla();
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_consulta_asientos(){
		
		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_asiento_tipo.setId("aut_asiento_tipo");     
		aut_asiento_tipo.setAutoCompletar(ser_contabilidad.getNombreAsientoContable_corto("1,33,34,53","true")); 
		aut_asiento_tipo.setFiltroContenido(true);

		Etiqueta eti_colaborador=new Etiqueta("ASIENTO TIPO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_asiento_tipo);
		bar_botones.agregarBoton(bot_limpiar);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		cal_fecha_inicial.setStyle("width: 100px;");
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
    	
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_asiento_consulta.setId("tab_asiento_consulta");
		tab_asiento_consulta.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaberFecha("1900-01-01","1900-01-01",aut_asiento_tipo.getValor()));
		tab_asiento_consulta.getColumna("ide_comov").setFiltro(true);
		tab_asiento_consulta.getColumna("detalle_conac").setFiltroContenido();
		tab_asiento_consulta.getColumna("detalle_comov").setFiltroContenido();
		tab_asiento_consulta.setColumnaSuma("debe,haber,DIFERENCIA_ASIENTO,CONTABILIZADO,DIFERENCIA");
		tab_asiento_consulta.setRows(10);
		tab_asiento_consulta.setLectura(true);
		tab_asiento_consulta.onSelect("VerDetalle");
		tab_asiento_consulta.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_asiento_consulta);
		
		tab_factura_consulta.setId("tab_factura_consulta");
		tab_factura_consulta.setHeader("DETALLE DE COMPROBANTES");
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados_detallado("0","0","0","1900-01-01","1900-01-01","0","false"));
		tab_factura_consulta.getColumna("ide_comov").setNombreVisual("CODIGO MOVIMIENTO");
		tab_factura_consulta.getColumna("detalle_conac").setNombreVisual("ASIENTO TIPO");
		tab_factura_consulta.setColumnaSuma("neto,iva,neto_contabilizado,valor_contabilizado");
		tab_factura_consulta.setRows(12);
		tab_factura_consulta.setLectura(true);
		tab_factura_consulta.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_factura_consulta);
		
		Division div_tabla = new Division();
		div_tabla.dividir2(pat_panel,pat_panel2,"35%","H");
		agregarComponente(div_tabla);
		
		
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
	
	public void seleccionaOpcion (){
		tab_asiento_consulta.limpiar();
		//utilitario.addUpdate("aut_asiento_tipo");
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		tab_asiento_consulta.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaberFecha(fecha_inicial,fecha_final,aut_asiento_tipo.getValor()));
		tab_asiento_consulta.ejecutarSql();
		//utilitario.addUpdate("tab_asiento_consulta");
		
	}
	
	public void VerDetalle(SelectEvent evt)
	{
		tab_asiento_consulta.seleccionarFila(evt);
		tab_factura_consulta.setSql(ser_facturacion.getComprobantesContabilizados_detallado("1","0",tab_asiento_consulta.getValor("ide_comov"),"1900-01-01","1900-01-01","0","false"));
		tab_factura_consulta.ejecutarSql();
		utilitario.addUpdate("tab_asiento_consulta,tab_factura_consulta");
		//utilitario.agregarMensajeInfo("Busqueda", "Los detalles solo seran de facturas");
	}
	
	public void limpiar(){
		aut_asiento_tipo.limpiar();
		tab_asiento_consulta.limpiar();
		utilitario.addUpdate("tab_asiento_consulta,aut_asiento_tipo");
	}

	public AutoCompletar getAut_asiento_tipo() {
		return aut_asiento_tipo;
	}

	public void setAut_asiento_tipo(AutoCompletar aut_asiento_tipo) {
		this.aut_asiento_tipo = aut_asiento_tipo;
	}

	public Tabla getTab_asiento_consulta() {
		return tab_asiento_consulta;
	}

	public void setTab_asiento_consulta(Tabla tab_asiento_consulta) {
		this.tab_asiento_consulta = tab_asiento_consulta;
	}

	public Tabla getTab_factura_consulta() {
		return tab_factura_consulta;
	}

	public void setTab_factura_consulta(Tabla tab_factura_consulta) {
		this.tab_factura_consulta = tab_factura_consulta;
	}
	
	
	
}