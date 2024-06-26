package paq_activos;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_activos.ejb.ServicioActivos;
import paq_sistema.aplicacion.Pantalla;

public class pre_depreciacion_activo extends Pantalla {
	private Tabla tab_depreciacion = new Tabla();
	private Dialogo dia_fecha = new Dialogo();
	private Tabla tab_fecha = new Tabla();
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

	public pre_depreciacion_activo() {
		bar_botones.getBot_insertar().setRendered(false);

		tab_depreciacion.setId("tab_depreciacion");
		tab_depreciacion.setSql(ser_activos.getActivosDepreciacion(" ide_afact=-1 "));
		tab_depreciacion.setNumeroTabla(1);

		tab_depreciacion.setPaginator(true);
		tab_depreciacion.setRows(20);
		tab_depreciacion.setCampoPrimaria("ide_afact");
		tab_depreciacion.setLectura(true);
		tab_depreciacion.dibujar();
		PanelTabla pat_panel = new PanelTabla();

		pat_panel.setPanelTabla(tab_depreciacion);
		
		Division div_division = new Division();
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);

		// /Boton depreciación activo
		Boton bot_depre = new Boton();
		bot_depre.setIcon("ui-calendario");
		bot_depre.setValue("DEPRECIACION ACTIVO");
		bot_depre.setMetodo("abrirDialogo");
		bar_botones.agregarBoton(bot_depre);
		dia_fecha.setId("dia_fecha");
		dia_fecha.setTitle("FECHA DEPRECIACION ACTIVO");
		dia_fecha.setWidth("45%");
		dia_fecha.setHeight("45%");
		Grid gri_cuerpo = new Grid();

		tab_fecha.setId("tab_fecha");
		tab_fecha.setTabla("afi_custodio", "ide_afcus", 10);
		tab_fecha.setCondicion("ide_afact=-1");// para que aparesca vacia
		tab_fecha.setTipoFormulario(true);
		tab_fecha.getGrid().setColumns(2);
		// oculto todos los campos
		tab_fecha.getColumna("fecha_entrega_afcus").setNombreVisual("FECHA CALCULO");
		tab_fecha.getColumna("fecha_entrega_afcus").setValorDefecto(utilitario.getFechaActual());
		tab_fecha.getColumna("ide_afcus").setVisible(false);
		tab_fecha.getColumna("ide_afact").setVisible(false);
		tab_fecha.getColumna("gen_ide_geedp").setVisible(false);
		tab_fecha.getColumna("detalle_afcus").setVisible(false);
		tab_fecha.getColumna("cod_barra_afcus").setVisible(false);
		tab_fecha.getColumna("nro_secuencial_afcus").setVisible(false);
		tab_fecha.getColumna("activo_afcus").setVisible(false);
		tab_fecha.getColumna("ide_geedp").setVisible(false);
		tab_fecha.getColumna("fecha_entrega_afcus").setVisible(true);
		tab_fecha.getColumna("fecha_descargo_afcus").setVisible(false);
		tab_fecha.getColumna("numero_acta_afcus").setVisible(false);
		tab_fecha.getColumna("razon_descargo_afcus").setVisible(false);
		tab_fecha.dibujar();
		gri_cuerpo.getChildren().add(tab_fecha);

		dia_fecha.getBot_aceptar().setMetodo("aceptarDialogo");

		dia_fecha.setDialogo(gri_cuerpo);
		agregarComponente(dia_fecha);

	}

	public void abrirDialogo() {
		// Hace aparecer el componente

		tab_fecha.limpiar();
		tab_fecha.insertar();
		dia_fecha.dibujar();
	}

	public void aceptarDialogo() {
		String fecha = tab_fecha.getValor("fecha_entrega_afcus");
		String sql = "";
		sql += "UPDATE bodt_catalogo_material SET vida_util_bocam = 0  WHERE cat_codigo_bocam  NOT LIKE ALL ('{84.01.03,84.01.04,84.01.05,84.01.06,84.01.07,84.01.11,84.01.12,84.01.13,84.01.15,84.02.02,84.02.03}');";
		sql += "UPDATE bodt_catalogo_material SET vida_util_bocam = 10 WHERE cat_codigo_bocam  LIKE ANY ('{84.01.03,84.01.04,84.01.06,84.01.11,84.01.12,84.01.13,84.01.15,84.02.03}');";
		sql += "UPDATE bodt_catalogo_material SET vida_util_bocam = 5  WHERE cat_codigo_bocam  LIKE ANY ('{84.01.05}');";
		sql += "UPDATE bodt_catalogo_material SET vida_util_bocam = 3  WHERE cat_codigo_bocam  LIKE ANY ('{84.01.07}');";
		sql += "UPDATE bodt_catalogo_material SET vida_util_bocam = 50 WHERE cat_codigo_bocam  LIKE ANY ('{84.02.02}');";
		sql += "UPDATE afi_activo a SET vida_util_afact = (SELECT b.vida_util_bocam FROM bodt_catalogo_material b WHERE b.ide_bocam=a.ide_bocam);";
		sql += "UPDATE afi_activo SET valor_depre_mes_afact = 0,valor_depre_dia_afact = 0, val_depreciacion_periodo_afact = 0, valor_depreciacion_afact = 0, valor_residual_afact = valor_compra_afact;";
		sql += "UPDATE afi_activo SET fecha_calculo_afact = '" + fecha + "';";
		
		sql += "UPDATE afi_activo a SET valor_compra_afact_temp = COALESCE((SELECT b.valor_realizacion_afper FROM afi_perito b WHERE (a.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = a.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1), a.valor_compra_afact);";
		sql += "UPDATE afi_activo a SET vida_util_afact_temp = COALESCE((SELECT b.vida_util_restante_afper FROM afi_perito b WHERE (a.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = a.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1), a.vida_util_afact);";
		sql += "UPDATE afi_activo a SET fecha_alta_afact_temp = COALESCE((SELECT b.fecha_reavaluo_afper FROM afi_perito b WHERE (a.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = a.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1), a.fecha_alta_afact);";
		sql += "UPDATE afi_activo a SET acta_codigo_temp = COALESCE((SELECT b.ide_afdoc FROM afi_doc_detalle_activo afdd LEFT JOIN afi_docu b ON afdd.ide_afdoc = b.ide_afdoc LEFT JOIN afi_tipo_docu c ON b.ide_aftidoc = c.ide_aftidoc WHERE(a.fecha_calculo_afact::date - b.fecha_afdoc::date) >= 0 AND afdd.ide_afact = a.ide_afact ORDER BY b.fecha_afdoc DESC,b.ide_afdoc DESC LIMIT 1) ,a.afi_ultima_acta,a.ide_bobod);";
		sql += "UPDATE afi_activo afa SET acta_tipo_temp = COALESCE((SELECT c.detalle_aftidoc FROM afi_doc_detalle_activo a LEFT JOIN afi_docu b ON a.ide_afdoc = b.ide_afdoc LEFT JOIN afi_tipo_docu c ON b.ide_aftidoc = c.ide_aftidoc WHERE(afa.fecha_calculo_afact::date - b.fecha_afdoc::date) >= 0 AND a.ide_afact = afa.ide_afact ORDER BY b.fecha_afdoc DESC,b.ide_afdoc DESC LIMIT 1) ,'Ingreso a bodega');";
		
		sql += "UPDATE afi_activo afa SET fecha_calculo_afact = (CASE WHEN COALESCE((SELECT c.saca_del_inventario_empresarial FROM afi_doc_detalle_activo a LEFT JOIN afi_docu b ON a.ide_afdoc = b.ide_afdoc LEFT JOIN afi_tipo_docu c ON b.ide_aftidoc = c.ide_aftidoc WHERE(afa.fecha_calculo_afact::date - b.fecha_afdoc::date) >= 0 AND a.ide_afact = afa.ide_afact ORDER BY b.fecha_afdoc DESC,b.ide_afdoc DESC LIMIT 1) ,false) THEN (SELECT fecha_afdoc FROM afi_docu adocu WHERE afa.acta_codigo_temp=adocu.ide_afdoc) ELSE afa.fecha_calculo_afact  END);";
		
		sql += "UPDATE afi_activo a SET valor_residual_afact  = COALESCE((SELECT b.valor_residual_afper FROM afi_perito b WHERE (a.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = a.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1), valor_compra_afact * 0.1)  WHERE vida_util_afact_temp > 0;";
		sql += "UPDATE afi_activo a SET valor_depre_mes_afact = (valor_compra_afact_temp-valor_residual_afact) /(vida_util_afact_temp * 12 ) WHERE vida_util_afact_temp > 0;";
		sql += "UPDATE afi_activo a SET valor_depre_dia_afact = (valor_compra_afact_temp-valor_residual_afact) /(vida_util_afact_temp * 365) WHERE vida_util_afact_temp > 0;";
		sql += "UPDATE afi_activo a SET valor_depreciacion_afact = ROUND((((valor_compra_afact_temp-valor_residual_afact)/(vida_util_afact_temp*365)) * (fecha_calculo_afact::date - fecha_alta_afact_temp::date)),3) WHERE (fecha_calculo_afact::date - fecha_alta_afact_temp::date)>=0 AND vida_util_afact_temp > 0;";
		sql += "UPDATE afi_activo a SET valor_en_libros = valor_compra_afact_temp - valor_depreciacion_afact  WHERE vida_util_afact_temp > 0;";
		System.out.println("DEPRECIACIÓN: " + sql);
		utilitario.getConexion().ejecutarSql(sql);

		utilitario.agregarMensaje("Valoración", "Se ejecuto la valoracion con éxito");
		dia_fecha.cerrar();
		tab_depreciacion.setSql(ser_activos.getActivosDepreciacion(" fecha_alta_afact<=fecha_calculo_afact "));
		tab_depreciacion.setRows(20);
		tab_depreciacion.ejecutarSql();
		utilitario.addUpdate("tab_depreciacion");
		utilitario.agregarMensaje("Guardado", "Proceso de Valoración realizado con exito");
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

	public Tabla getTab_depreciacion() {
		return tab_depreciacion;
	}

	public void setTab_depreciacion(Tabla tab_depreciacion) {
		this.tab_depreciacion = tab_depreciacion;
	}

	public Dialogo getDia_fecha() {
		return dia_fecha;
	}

	public void setDia_fecha(Dialogo dia_fecha) {
		this.dia_fecha = dia_fecha;
	}

	public Tabla getTab_fecha() {
		return tab_fecha;
	}

	public void setTab_fecha(Tabla tab_fecha) {
		this.tab_fecha = tab_fecha;
	}
	

}
