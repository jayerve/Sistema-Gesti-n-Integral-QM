package paq_presupuesto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_libera_compromisos_totales extends Pantalla {

	Tabla tab_libera_compromiso_matriz = new Tabla();
	private Tabla tab_libera_compromiso = new Tabla();
	private Tabla tab_detalle_libera = new Tabla();

	Dialogo dis_liberar = new Dialogo();
	Calendario cal_fecha = new Calendario();
	Texto txt_num_resolucion = new Texto();
	public static String par_modulosec_compromiso;
	
	Dialogo dis_reporte = new Dialogo();
	Texto txt_sec_inicial = new Texto();
	Texto txt_sec_final = new Texto();

	///reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte self_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
		
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_libera_compromisos_totales() {
		
		par_modulosec_compromiso=utilitario.getVariable("p_modulo_secuencialliberacionComp");
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);

		Boton bot_liberar = new Boton();
		bot_liberar.setIcon("ui-icon-person");
		bot_liberar.setValue("LIBERAR COMPROMISOS");
		bot_liberar.setMetodo("dibujaDialogo");
		bar_botones.agregarBoton(bot_liberar);

		dis_liberar.setId("dis_liberar");
		dis_liberar.setTitle("Liberar Compromisos");
		dis_liberar.setWidth("25%");
		dis_liberar.setHeight("20%");

		Grid gri_cuerpo = new Grid();
		gri_cuerpo.setColumns(2);
		gri_cuerpo.getChildren().add(new Etiqueta("Fecha Liberación Compromiso"));
		gri_cuerpo.getChildren().add(cal_fecha);
		gri_cuerpo.getChildren().add(new Etiqueta("Nro. Resolución"));
		gri_cuerpo.getChildren().add(txt_num_resolucion);

		dis_liberar.getBot_aceptar().setMetodo("libera");
		dis_liberar.setDialogo(gri_cuerpo);
		agregarComponente(dis_liberar);
		
		dis_reporte.setId("dis_reporte");
		dis_reporte.setTitle("Reporte Liberación Masiva");
		dis_reporte.setWidth("25%");
		dis_reporte.setHeight("20%");

		Grid gri_reporte = new Grid();
		gri_reporte.setColumns(2);
		gri_reporte.getChildren().add(new Etiqueta("Secuencial Inicio"));
		gri_reporte.getChildren().add(txt_sec_inicial);
		gri_reporte.getChildren().add(new Etiqueta("Secuencial Fin"));
		gri_reporte.getChildren().add(txt_sec_final);

		dis_reporte.getBot_aceptar().setMetodo("reporte");
		dis_reporte.setDialogo(gri_reporte);
		agregarComponente(dis_reporte);

		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
		ItemMenu itm_todas = new ItemMenu();
		ItemMenu itm_niguna = new ItemMenu();

		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_libera_compromiso_matriz");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_libera_compromiso_matriz");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo("seleccionarNinguna");
		itm_niguna.setUpdate("tab_libera_compromiso_matriz");
		boc_seleccion_inversa.agregarBoton(itm_niguna);

		tab_libera_compromiso_matriz.setId("tab_libera_compromiso_matriz");
		tab_libera_compromiso_matriz.setSql(sqlSaldosCompromisos("1"," and (comprometido_prpot  - (case when devengado is null then 0.0 else devengado end)) !=0 "));
		// tab_libera_compromiso_matriz.setCampoPrimaria("codigo");
		tab_libera_compromiso_matriz.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_libera_compromiso_matriz.getColumna("ide_prpoa").setAutoCompletar();
		tab_libera_compromiso_matriz.getColumna("ide_prpoa").setLectura(true);
		tab_libera_compromiso_matriz.getColumna("ide_prpoa").setLongitud(90);
		tab_libera_compromiso_matriz.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_libera_compromiso_matriz.getColumna("ide_prfuf").setAutoCompletar();
		tab_libera_compromiso_matriz.getColumna("ide_prfuf").setLectura(true);
		tab_libera_compromiso_matriz.getColumna("ide_prtra").setNombreVisual("NRO. COMRPOMISO");
		tab_libera_compromiso_matriz.getColumna("nro_certificacion_prcer").setNombreVisual("NRO. CERTIFICACION");
		tab_libera_compromiso_matriz.getColumna("ide_prfuf").setNombreVisual("FUENTE DE FINANCIAMIENTO");
		tab_libera_compromiso_matriz.getColumna("ide_prtra").setFiltro(true);
		tab_libera_compromiso_matriz.getColumna("nro_certificacion_prcer").setFiltro(true);
		tab_libera_compromiso_matriz.setLectura(true);
		tab_libera_compromiso_matriz.setTipoSeleccion(true);
		// tab_libera_compromiso_matriz.setValueExpression("rowStyleClass",
		// "fila.campos[7] eq '1' ? 'text-red' : fila.campos[6] eq '0'  ? 'text-green' : null");
		tab_libera_compromiso_matriz.getSumaColumna("comprometido_prpot,devengado");

		tab_libera_compromiso_matriz.dibujar();

		// ////////////////////
		tab_libera_compromiso.setId("tab_libera_compromiso");
		tab_libera_compromiso.setHeader("LIBERACIÓN COMPROMISO");
		tab_libera_compromiso.setTabla("pre_libera_compromiso", "ide_prlic", 1);
		tab_libera_compromiso.getColumna("valor_total_prlic").setValorDefecto("0.00");
		tab_libera_compromiso.getColumna("activo_prlic").setValorDefecto("true");
		tab_libera_compromiso.agregarRelacion(tab_detalle_libera);

		tab_detalle_libera.setId("tab_detalle_libera");
		tab_detalle_libera.setHeader("DETALLE LIBERACIÓN DEL COMPROMISO");
		tab_detalle_libera.setTabla("pre_detalle_libera_compro", "ide_prdlc", 2);

		PanelTabla pat_panel = new PanelTabla();
		// pat_panel.setHeader(gri_formulario);
		pat_panel.getChildren().add(boc_seleccion_inversa);
		pat_panel.setPanelTabla(tab_libera_compromiso_matriz);

		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);

		agregarComponente(div_recaudacion);

	}

	public void libera() {
		
		String sqlgrupo = "select ide_prtra, sum(saldo_devengar) as saldo_devengar  from ( select * from ("
				+ sqlSaldosCompromisos("1","and (comprometido_prpot  - (case when devengado is null then 0.0 else devengado end)) !=0")
				+ ") a where codigo in ("+ tab_libera_compromiso_matriz.getFilasSeleccionadas()+ ")) a group by ide_prtra order by ide_prtra";
	    
		System.out.println("sql "+sqlgrupo);
		TablaGenerica tab_cabecera_compromiso = utilitario.consultar(sqlgrupo);

		for (int i = 0; i < tab_cabecera_compromiso.getTotalFilas(); i++) {
			
			aceptarCompromiso(tab_cabecera_compromiso.getValor(i,"ide_prtra"));
			generarLiqu();

		}
		dis_liberar.cerrar();
		tab_libera_compromiso_matriz.ejecutarSql();
		utilitario.agregarMensaje("Se guardo Correctamente", "");
	}

	public void aceptarCompromiso(String str_seleccionados) {
		System.out.println("aceptarCompromiso ide_prtra: "+str_seleccionados);
		System.out.println("aceptarCompromiso fecha: "+cal_fecha.getValue());
		
		tab_libera_compromiso.insertar();
		tab_libera_compromiso.setValor("ide_prtra", str_seleccionados);
		tab_libera_compromiso.setValor("sec_liquidacion_prlce", ser_contabilidad.numeroSecuencial(par_modulosec_compromiso));
		tab_libera_compromiso.setValor("fecha_prlic", utilitario.DeDateAString((Date)cal_fecha.getValue()));
		tab_libera_compromiso.setValor("num_doc_prlic", txt_num_resolucion.getValue().toString());
	
		
		TablaGenerica tab_valida_presupuesto = utilitario.consultar("select * from tes_comprobante_pago where ide_prtra="+str_seleccionados);
		if(tab_valida_presupuesto.getTotalFilas()>0){
			tab_libera_compromiso.setValor("total_parcial_prlic", "0");//PARCIAL
			TablaGenerica tab_tramite = ser_presupuesto.getTablaGenericaComp(str_seleccionados);		
			for(int i=0;i<tab_tramite.getTotalFilas();i++){
				tab_detalle_libera.insertar();
				tab_detalle_libera.setValor("ide_prpoa", tab_tramite.getValor(i, "ide_prpoa"));
				tab_detalle_libera.setValor("ide_prfuf", tab_tramite.getValor(i, "ide_prfuf"));
				tab_detalle_libera.setValor("valor_comprometido_prdlc", tab_tramite.getValor(i, "comprometido_prpot"));
				TablaGenerica tab_consulta_negativo=utilitario.consultar("select 1 as codigo,(-1)*"+tab_tramite.getValor(i, "saldo_devengar")+" as valor_liquidar");
				tab_detalle_libera.setValor("valor_liberado_prdlc",tab_consulta_negativo.getValor("valor_liquidar"));
				tab_detalle_libera.setValor("saldo_liquidado_prdlc", "0");
				tab_detalle_libera.setValor("activo_prdlc", "true");
			}
			tab_libera_compromiso.setValor("valor_total_prlic", tab_detalle_libera.getSumaColumna("valor_liberado_prdlc")+"");
		}

		else{
			tab_libera_compromiso.setValor("total_parcial_prlic", "1");//TOTAL
			TablaGenerica tab_consulta_poacer = utilitario.consultar("select ide_prpot,ide_prpoa,ide_prfuf,comprometido_prpot,comprometido_prpot* (-1) as total_liquidado  from pre_poa_tramite  where ide_prtra="+str_seleccionados);
			if(tab_consulta_poacer.getTotalFilas()>0){
				for(int i=0;i<tab_consulta_poacer.getTotalFilas();i++){
					tab_detalle_libera.insertar();
					tab_detalle_libera.setValor("valor_comprometido_prdlc", tab_consulta_poacer.getValor(i, "comprometido_prpot"));
					tab_detalle_libera.setValor("valor_liberado_prdlc", tab_consulta_poacer.getValor(i, "total_liquidado"));
					tab_detalle_libera.setValor("saldo_liquidado_prdlc", "0");
					tab_detalle_libera.setValor("activo_prdlc", "true");
					tab_detalle_libera.setValor("ide_prpoa", tab_consulta_poacer.getValor(i, "ide_prpoa"));
					tab_detalle_libera.setValor("ide_prfuf", tab_consulta_poacer.getValor(i, "ide_prfuf"));
				}
				tab_libera_compromiso.setValor("valor_total_prlic", utilitario.getFormatoNumero(tab_detalle_libera.getSumaColumna("valor_liberado_prdlc"),2)+"");
			}
		}
		utilitario.addUpdate("tab_libera_compromiso");
	}
	
	public void generarLiqu(){
	System.out.println("generarLiqu: ");
		if(tab_libera_compromiso.getValor("total_parcial_prlic").equals("1")){
			double saldo=0;
			try{
				saldo=tab_detalle_libera.getSumaColumna("saldo_liquidado_prdlc");
			}
			catch (Exception e){
				utilitario.agregarMensaje("No existe valor", "El valor del saldo deeb ser valido");
			}
			if (saldo !=0){
				utilitario.agregarMensajeError("No se puede guardar", "Para una Liquidacion Total del Compromiso, los Saldos deben ser Cero");
			}
		}
		String sql;

		// TODO Auto-generated method stub
		if(tab_libera_compromiso.isFilaInsertada()){
			System.out.println("Guardando la liquidacion y afectando al compromiso y al poa");
			if(tab_libera_compromiso.guardar()){
				if(tab_detalle_libera.guardar()){
					guardarPantalla();
					
					for(int i=0;i<tab_detalle_libera.getTotalFilas();i++){
						//Actualiza detalles del compromiso
						sql="update pre_poa_tramite set comprometido_prpot=coalesce(comprometido_prpot,0)+"+tab_detalle_libera.getValor(i, "valor_liberado_prdlc") +" ,saldo_comprometido_prpot=0 "
								+ " where ide_prtra="+tab_libera_compromiso.getValor("ide_prtra") + " and ide_prpoa="+ tab_detalle_libera.getValor(i, "ide_prpoa")+ " and ide_prfuf="+ tab_detalle_libera.getValor(i, "ide_prfuf");
						
						System.out.println("Actualiza detalles del compromiso: "+sql);
						utilitario.getConexion().ejecutarSql(sql);
	
						//Actualiza valore del compromiso en el POA-SubActividad
						ser_presupuesto.trigEjecutaCompromiso(tab_detalle_libera.getValor(i, "ide_prpoa"),tab_detalle_libera.getValor(i, "ide_prfuf"));
						ser_presupuesto.trigActualizaCompromisoPoa(tab_detalle_libera.getValor(i, "ide_prpoa"));
					}
				}
				//Actualiza total en el compromiso
				sql="update pre_tramite set total_compromiso_prtra=coalesce(total_compromiso_prtra,0)+"
						+tab_libera_compromiso.getValor("valor_total_prlic")+", valor_liberado_prtra=(-1)*"+tab_libera_compromiso.getValor("valor_total_prlic")
						+" where ide_prtra="+tab_libera_compromiso.getValor("ide_prtra");
				utilitario.getConexion().ejecutarSql(sql);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_compromiso), par_modulosec_compromiso);
			}
		}

	}

	public void dibujaDialogo() {
		if (tab_libera_compromiso_matriz.getSeleccionados().length > 0) {
			dis_liberar.dibujar();
		} else {
			utilitario
					.agregarMensajeError("No existen registros seleccionados",
							"Seleccione un registro para proceder a la liberación del Compromiso");
			return;
		}
	}

	public void reporte()
	{
		try
		{
			int secIni=pckUtilidades.CConversion.CInt(txt_sec_inicial.getValue().toString());
			int secFin=pckUtilidades.CConversion.CInt(txt_sec_final.getValue().toString());
			if(secIni>secFin)
			{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Ingresado un rango adecuado");
			}
			else
			{
				dis_reporte.cerrar();
				rep_reporte.dibujar();
			}
		}
		catch(Exception ex)
		{
			utilitario.agregarMensajeInfo("No se puede continuar", "Ingrese correctamente un rango.");
		}
	}
	
	//reporte
	public void abrirListaReportes() {
	    // TODO Auto-generated method stub
		dis_reporte.dibujar();
	}
		
	public void aceptarReporte(){
		//System.out.println("aceptarReporte");
		if(rep_reporte.getReporteSelecionado().equals("Liquidacion Compromiso FULL"));{
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo","REPORTE DE LIBERACION  DE COMPROMISOS MASIVOS");
				p_parametros.put("sec_inicial", pckUtilidades.CConversion.CInt(txt_sec_inicial.getValue().toString()));
				p_parametros.put("sec_final", pckUtilidades.CConversion.CInt(txt_sec_final.getValue().toString()));
				p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_especialista_pres",  utilitario.getVariable("p_pie_especialista_pres"));
				p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt("8"));

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
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
	}

	public void seleccionarTodas() {
		tab_libera_compromiso_matriz.setSeleccionados(null);
		Fila seleccionados[] = new Fila[tab_libera_compromiso_matriz
				.getTotalFilas()];
		for (int i = 0; i < tab_libera_compromiso_matriz.getFilas().size(); i++) {
			seleccionados[i] = tab_libera_compromiso_matriz.getFilas().get(i);
		}
		tab_libera_compromiso_matriz.setSeleccionados(seleccionados);

	}

	/** DFJ **/
	public void seleccinarInversa() {
		if (tab_libera_compromiso_matriz.getSeleccionados() == null) {
			seleccionarTodas();
		} else if (tab_libera_compromiso_matriz.getSeleccionados().length == tab_libera_compromiso_matriz
				.getTotalFilas()) {
			seleccionarNinguna();
		} else {
			Fila seleccionados[] = new Fila[tab_libera_compromiso_matriz
					.getTotalFilas()
					- tab_libera_compromiso_matriz.getSeleccionados().length];
			int cont = 0;
			for (int i = 0; i < tab_libera_compromiso_matriz.getFilas().size(); i++) {
				boolean boo_selecionado = false;
				for (int j = 0; j < tab_libera_compromiso_matriz
						.getSeleccionados().length; j++) {
					if (tab_libera_compromiso_matriz.getSeleccionados()[j]
							.equals(tab_libera_compromiso_matriz.getFilas()
									.get(i))) {
						boo_selecionado = true;
						break;
					}
				}
				if (boo_selecionado == false) {
					seleccionados[cont] = tab_libera_compromiso_matriz
							.getFilas().get(i);
					cont++;
				}
			}
			tab_libera_compromiso_matriz.setSeleccionados(seleccionados);
		}
	}

	/** DFJ **/
	public void seleccionarNinguna() {
		tab_libera_compromiso_matriz.setSeleccionados(null);

	}

	public String sqlSaldosCompromisos(String poa_compromisos, String condicion) {
		String sql = "";
		sql += "select row_number() over(order by f.ide_prtra,a.ide_prpoa,a.ide_prfuf ) as codigo"
				+ "	,f.ide_prtra,nro_certificacion_prcer,f.ide_prpoa,f.ide_prfuf,comprometido_prpot"
				+ "	,(case when valor_liberado is null then 0.0 else valor_liberado end) as valor_liberado"
				+ "	,(case when devengado is null then 0.0 else devengado end) as devengado"
				+ "	,comprometido_prpot -(coalesce(devengado,0)-coalesce(valor_liberado,0)) as saldo_devengar "
				+ "	,(case when (comprometido_prpot - (case when devengado is null then 0.0 else devengado end) ) < 0 then 1 else 0 end) as estado"
				+ "	from ("
				+ "			select a.ide_prpoa,ide_prfuf,valor_financiamiento_prpof"
				+ "			from pre_poa a, pre_poa_financiamiento b"
				+ "			where a.ide_prpoa = b.ide_prpoa "
				+ "			) a"
				+ "			left join (select ide_prpoa,ide_prfuf,sum(valor_reformado_prprf) as valor_reformado_prprf from  pre_poa_reforma_fuente group by ide_prpoa,ide_prfuf )b on a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
				+ "			left join (select a.ide_prcer,ide_prpoa,ide_prfuf,nro_certificacion_prcer,valor_certificado_prpoc from pre_certificacion a, pre_poa_certificacion b where a.ide_prcer=b.ide_prcer) c on a.ide_prpoa = c.ide_prpoa  and a.ide_prfuf = c.ide_prfuf"
				+ "			left join (select ide_prcer,ide_prpoa,ide_prfuf,sum(valor_certificado_prpoc) as total_certificado from pre_poa_certificacion group by ide_prpoa,ide_prfuf,ide_prcer ) d on c.ide_prpoa= d.ide_prpoa and c.ide_prfuf = d.ide_prfuf and c.ide_prcer = d.ide_prcer"
				+ "			left join ( select ide_prcer,sum( valor_liquidado_prdcl) as valor_liquidado_prdcl,ide_prpoa,ide_prfuf"
				+ "					from pre_liquida_certificacion a,pre_detalle_liquida_certif b where a.ide_prlce = b.ide_prlce group by ide_prcer,ide_prpoa,ide_prfuf ) e on c.ide_prcer = e.ide_prcer"
				+ "					and c.ide_prpoa = e.ide_prpoa  and c.ide_prfuf = e.ide_prfuf"
				+ "					left join ( select a.ide_prcer,a.ide_prtra,ide_prpoa,ide_prfuf,comprometido_prpot from pre_tramite a,pre_poa_tramite b where a.ide_prtra = b.ide_prtra  ) f on c.ide_prcer = f.ide_prcer"
				+ "					and c.ide_prpoa = f.ide_prpoa  and c.ide_prfuf = f.ide_prfuf"
				+ "					left join (select ide_prtra,ide_prpoa,ide_prfuf, sum(valor_liberado_prdlc) as valor_liberado from pre_libera_compromiso a, pre_detalle_libera_compro b"
				+ "							where a.ide_prlic = b.ide_prlic group by ide_prtra,ide_prpoa,ide_prfuf ) g on f.ide_prtra = g.ide_prtra and f.ide_prpoa = g.ide_prpoa and f.ide_prfuf = g.ide_prfuf"
				+ "							left join ( select a.ide_prpoa,ide_prtra,b.ide_prfuf, sum(devengado_prmen) as devengado from pre_anual a,pre_mensual b where a.ide_pranu=b.ide_pranu and not ide_prtra is null"
				+ "							group by a.ide_prpoa,ide_prtra,b.ide_prfuf ) h on f.ide_prtra = h.ide_prtra and f.ide_prpoa = h.ide_prpoa and f.ide_prfuf = h.ide_prfuf"
				+ "	where not f.ide_prtra is null and (comprometido_prpot -(coalesce(devengado,0)-coalesce(valor_liberado,0)))>0 ";
		//sql += condicion;
		sql += " order by f.ide_prtra,f.ide_prpoa,f.ide_prfuf";
		System.out.println(""+sql);
		return sql;
	}

	public Tabla getTab_libera_compromiso_matriz() {
		return tab_libera_compromiso_matriz;
	}

	public void setTab_libera_compromiso_matriz(
			Tabla tab_libera_compromiso_matriz) {
		this.tab_libera_compromiso_matriz = tab_libera_compromiso_matriz;
	}

	public Tabla getTab_libera_compromiso() {
		return tab_libera_compromiso;
	}

	public void setTab_libera_compromiso(Tabla tab_libera_compromiso) {
		this.tab_libera_compromiso = tab_libera_compromiso;
	}

	public Tabla getTab_detalle_libera() {
		return tab_detalle_libera;
	}

	public void setTab_detalle_libera(Tabla tab_detalle_libera) {
		this.tab_detalle_libera = tab_detalle_libera;
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
