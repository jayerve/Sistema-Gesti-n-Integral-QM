package paq_activos;

import javax.ejb.EJB;
import paq_activos.ejb.ServicioActivos;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_activos_consulta_perito extends Pantalla {

	private Tabla tab_consulta_activo = new Tabla();
	private Combo com_anio=new Combo();

	@EJB
	private ServicioActivos ser_activo = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);
	@EJB
	private ServicioEmpleado serv_empleado;
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	public pre_activos_consulta_perito() {
		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		
		tab_consulta_activo.setId("tab_consulta_activo");
		tab_consulta_activo.setSql(ser_activo.getConsultaPeritoBienes("0"));
		tab_consulta_activo.setCampoOrden("ide_prcer desc");
		tab_consulta_activo.getColumna("codigo_bien").setFiltroContenido();
		tab_consulta_activo.getColumna("codigo_anterior").setFiltroContenido();
		tab_consulta_activo.getColumna("item_presupuestario").setFiltroContenido();
		tab_consulta_activo.getColumna("descripcion_caracteristicas").setFiltroContenido();
		tab_consulta_activo.getColumna("componentes").setFiltroContenido();
		tab_consulta_activo.getColumna("serie").setFiltroContenido();
		tab_consulta_activo.getColumna("marca").setFiltroContenido();
		tab_consulta_activo.getColumna("modelo").setFiltroContenido();
		tab_consulta_activo.setColumnaSuma("valor_neto,iva,valor_compra,valor_residual,valor_revaluo_comercial,valor_realizacion"); 
		tab_consulta_activo.setLectura(true);
		tab_consulta_activo.setPaginator(true);
		tab_consulta_activo.setRows(20);
		tab_consulta_activo.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta_activo);

		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
	}


	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_consulta_activo.setSql(ser_activo.getConsultaPeritoBienes(com_anio.getValue()+""));
			//tab_consulta_activo.setCondicion("ide_geani="+com_anio.getValue());
			tab_consulta_activo.ejecutarSql();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
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


	
	public Tabla getTab_consulta_activo() {
		return tab_consulta_activo;
	}


	public void setTab_consulta_activo(Tabla tab_consulta_activo) {
		this.tab_consulta_activo = tab_consulta_activo;
	}


	public Combo getCom_anio() {
		return com_anio;
	}


	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
}
