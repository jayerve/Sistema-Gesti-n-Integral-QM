package paq_bodega;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_bodega_estimacion extends Pantalla{
	
	private Tabla tab_tabla = new Tabla();

	
	// REPORTE
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	
	private Combo com_anio=new Combo();
    private Combo com_mes=new Combo();
    
    @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_bodega_estimacion(){
		System.out.println("pre_bodega_estimacion");
		
		bar_botones.limpiar();
		
		rep_reporte.setId("rep_reporte"); // id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");// ejecuta el
																	// metodo al
																	// aceptar
																	// reporte
		agregarComponente(rep_reporte);// agrega el componente a la pantalla
		bar_botones.agregarReporte();// aparece el boton de reportes en la barra
										// de botones
		self_reporte.setId("self_reporte"); // id
		agregarComponente(self_reporte);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
						

		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(ser_bodega.getEstimacion());
		
		tab_tabla.getColumna("ide_bocam").setVisible(false);
		tab_tabla.getColumna("a").setVisible(false);
		tab_tabla.getColumna("b").setVisible(false);
		tab_tabla.getColumna("n").setVisible(false);

		tab_tabla.getColumna("descripcion_bocam").setNombreVisual("CATÁLOGO");
		tab_tabla.getColumna("y_1").setNombreVisual("ESTIMACIÓN SIGUIENTE MES");
		tab_tabla.getColumna("y_12").setNombreVisual("ESTIMACIÓN SIGUIENTE AÑO");
		
		tab_tabla.getColumna("descripcion_bocam").setFiltroContenido();
		
		
		tab_tabla.setRows(25);
		tab_tabla.setLectura(true);
		tab_tabla.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
	}
	
	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_mes.getValue() != null) {
			tab_tabla.setSql(ser_bodega.getResumenMensual(com_anio.getValue().toString(), com_mes.getValue().toString()));
			tab_tabla.ejecutarSql();
		} else {
			utilitario.agregarMensajeInfo("Selecione el año y mes", "");
		}
	}

	public void limpiar(){
		tab_tabla.limpiar();
		
		tab_tabla.setSql(ser_tesoreria.comprobantesClientes("-1","1900-01-01","1900-01-01","false","false"));
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_recaudacion,aut_cliente,eti_devolucion");
	}

	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}

	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Resumen General Mensual"))
		{
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap<String, Object>();
				rep_reporte.cerrar();
				TablaGenerica tg = utilitario.consultar(ser_contabilidad.getAnioDetalle("true,false","true,false"));
				TablaGenerica tg_mes = utilitario.consultar(ser_bodega.getMes(com_mes.getValue().toString()));
								
				try {
					p_parametros.put("titulo", "RESUMEN GENERAL MES " + tg_mes.getValor("detalle_gemes").toString() + " - " + tg.getValor("detalle_geani").toString());
				} catch (NumberFormatException e) {
					System.out.println("Error: "+ e.getMessage());
				}
				
				
				p_parametros.put("ide_usua", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
				p_parametros.put("ide_empr", 0);
				p_parametros.put("ide_sucu", 1);
				p_parametros.put("autorizado", utilitario.getVariable("p_jefe_activos_fijos"));
				p_parametros.put("ide_geani", Long.valueOf(pckUtilidades.CConversion.CInt(com_anio.getValue().toString())));
				p_parametros.put("mes", Long.valueOf(pckUtilidades.CConversion.CInt(com_mes.getValue().toString())));
				
				System.out.println(p_parametros);
				System.out.println("aceptarReporte " + rep_reporte.getPath());
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
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



	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public ServicioTesoreria getSer_tesoreria() {
		return ser_tesoreria;
	}

	public void setSer_tesoreria(ServicioTesoreria ser_tesoreria) {
		this.ser_tesoreria = ser_tesoreria;
	}


	
	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
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
