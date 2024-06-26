package paq_reportes;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.poi.hssf.record.formula.TblPtg;







import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;

public class pre_resultado extends Pantalla {
	private Tabla tab_resultado =new Tabla();
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private Combo com_anio=new Combo();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	
	public pre_resultado (){
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		tab_resultado.setId("tab_resultado");
		tab_resultado.setTabla("rep_estado_resultado", "ide_rpesr", 1);
		tab_resultado.setCondicion("ide_rpesr=-1");
		tab_resultado.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_resultado);
		agregarComponente(pat_panel1);
	}
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		
		String sql="update rep_estado_resultado"
+" set valor_rpesr=0,"
+" total_rpesr=0,"
+" total_general_rpesr=0;"
+" update rep_estado_resultado"
+" set valor_rpesr = haber "
+" from ("
+" select ide_cobac, ide_geani, a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac, (debe1_cobac+debe2_cobac+debe3_cobac+debe4_cobac+debe5_cobac+debe6_cobac+debe7_cobac+"
+" debe8_cobac+debe9_cobac+debe10_cobac+debe11_cobac+debe12_cobac ) - ("
+" haber1_cobac+haber2_cobac+haber3_cobac+debe4_cobac+haber4_cobac+haber5_cobac+haber6_cobac+haber7_cobac+haber8_cobac+haber9_cobac+"
+" haber10_cobac+haber11_cobac+haber12_cobac ) as haber"
+" from cont_balance_comprobacion a, cont_catalogo_cuenta b"
+" where a.ide_cocac = b.ide_cocac and a.ide_geani="+com_anio.getValue().toString()
+" order by cue_codigo_cocac"
+" ) a where codigo_rpesr = cue_codigo_cocac;"
+" update rep_estado_resultado"
+" set total_rpesr=total"
+" from ("
+" select sum(valor_rpesr) as total,codigo_grupo_rpesr"
+" from rep_estado_resultado group by codigo_grupo_rpesr"
+" ) a where a.codigo_grupo_rpesr = rep_estado_resultado.codigo_grupo_rpesr;"
+" update rep_estado_resultado"
+" set total_general_rpesr=total"
+" from ("
+" select sum(valor_rpesr) as total"
+" from rep_estado_resultado "
+" ) a ;";
		
		utilitario.getConexion().ejecutarSql(sql);
		tab_resultado.setCondicion("1=1");

		tab_resultado.ejecutarSql();
			utilitario.addUpdate("tab_resultado");
		
	}
	 public void abrirListaReportes() {

		   	// TODO Auto-generated method stub
		   	rep_reporte.dibujar();
		   }
		   public void aceptarReporte(){
			   TablaGenerica anio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue().toString());

		   	if(rep_reporte.getReporteSelecionado().equals("Estado de Resultado"));{
		   		if (rep_reporte.isVisible()){
		   			p_parametros=new HashMap();		
		   			rep_reporte.cerrar();	
		   			p_parametros.put("anio",anio.getValor("detalle_geani"));
		   			p_parametros.put("titulo","Estado de Resultado");
		   			p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt("7"));
		   			p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt("0"));
		   			p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt("1"));
		   		//p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
		   		self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
		   		self_reporte.dibujar();
		   		
		   	}
		   }
		   }

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_resultado.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_resultado.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_resultado.eliminar();
	}

	public Tabla getTab_resultado() {
		return tab_resultado;
	}

	public void setTab_resultado(Tabla tab_resultado) {
		this.tab_resultado = tab_resultado;
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
	public Map getMap_parametros() {
		return map_parametros;
	}
	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}
	public Combo getCom_anio() {
		return com_anio;
	}
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

}
