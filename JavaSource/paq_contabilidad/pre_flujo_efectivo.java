package paq_contabilidad;
 
import java.util.HashMap;
import java.util.Locale;
import java.util.Map; 

import javax.ejb.EJB;



import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_flujo_efectivo extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public static String par_tipo_asiento_inicial;
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();

	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_flujo_efectivo() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));	
		com_anio.setValue(utilitario.getVariable("p_anio_vigente"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		
		bar_botones.agregarReporte();
		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);		
				
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("cont_flujo_efectivo", "ide_flujo_efectivo", 1);	
		tab_balance_inicial.setHeader("FLUJO EFECTIVO");
		tab_balance_inicial.getColumna("con_ide_flujo_efectivo").setCombo("cont_flujo_efectivo", "ide_flujo_efectivo", "des_cuenta,tipo_grupo", "");
		tab_balance_inicial.getColumna("ide_cuenta").setCombo(ser_contabilidad.getCuentaContable("true,false"));
		tab_balance_inicial.getColumna("ide_cuenta").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cuenta").setLongitud(200);
		tab_balance_inicial.getColumna("signo").setCombo(utilitario.getListaSumaResta());	
		tab_balance_inicial.getColumna("tipo_grupo").setCombo(utilitario.getListaTipoGrupoFlujo());	
		tab_balance_inicial.getColumna("flujo").setCombo(utilitario.getListaFlujo());	

		//tab_balance_inicial.setLectura(true);
		tab_balance_inicial.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_balance_inicial);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Calcular Flujo Efectivo");
		bot_actualizar.setMetodo("generarCedula");
		bar_botones.agregarBoton(bot_actualizar);	
		
		inicializaCalendario();
		
		utilitario.agregarNotificacionInfo("IMPORTANTE","Antes de Recalcular el Flujo del Efectivo, Recalcule el Balance General...");
		
	}
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		//sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha1(utilitario.obtenerFechaInicioAnio());
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generarCedula");
		agregarComponente(sel_calendario);
	}
	
	public void generarCedula(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		TablaGenerica tab_valida_anio=utilitario.consultar("select 1 as codigo, 2 as resultado from tes_caja where not extract(year from cast('"+sel_calendario.getFecha1String()+"' as date)) =extract(year from cast('"+sel_calendario.getFecha2String()+"' as date))");
		if(tab_valida_anio.getTotalFilas()>0){
			utilitario.agregarMensajeError("Fechas no Validas", "Favor ingrese fechas que se encuentren dentro de un mismo año");
			return;
		}
		if(sel_calendario.isVisible()){
			sel_calendario.cerrar();

			ser_contabilidad.generarBalComprobacion(com_anio.getValue().toString(), sel_calendario.getFecha1().toString(), sel_calendario.getFecha2().toString(), par_tipo_asiento_inicial);
			TablaGenerica tab_meses = utilitario.consultar("select 1 as codigo,extract(month from cast('"+sel_calendario.getFecha1String()+"' as date)) as mes_inicial, extract(month from cast('"+sel_calendario.getFecha2String()+"' as date)) as mes_final");
			
			String actualiza_tabla="update cont_flujo_efectivo"
					+" set valor = 0,"
					+" periodo_inicial="+tab_meses.getValor("mes_inicial") +","
					+" periodo_final="+tab_meses.getValor("mes_final")+","
					+" fecha_inicial='"+sel_calendario.getFecha1String()+"',"
					+" fecha_final='"+sel_calendario.getFecha2String()+"'";
			utilitario.getConexion().ejecutarSql(actualiza_tabla);
			
			
			String traspasa_valores="update cont_flujo_efectivo"
					+" set valor = (case when flujo =1 then debe3 -haber3  when flujo = 2 then haber3 -debe3 when flujo = 3 then debe3 when flujo = 4 then haber3 end)*signo "
					+" from ("
					+" select ide_cocac,debe_acumulado_cobac+ debe_periodo_cobac - debe_inicial_cobac as debe3,haber_acumulado_cobac+haber_periodo_cobac - haber_inicial_cobac as haber3,(case when debe_saldo_cobac = 0 then haber_saldo_cobac else debe_saldo_cobac end) as nuevovalor from cont_balance_comprobacion ) a where ide_cocac=ide_cuenta";
			utilitario.getConexion().ejecutarSql(traspasa_valores);
			
			String calcula_tabla="update cont_flujo_efectivo"
					+" set valor = val"
					+" from ("
					+" select sum(valor) as val,con_ide_flujo_efectivo"
					+" from cont_flujo_efectivo"
					+" group by con_ide_flujo_efectivo"
					+" ) a where a.con_ide_flujo_efectivo = cont_flujo_efectivo.ide_flujo_efectivo";
			
			int i=0;
			while (i<=3){
			utilitario.getConexion().ejecutarSql(calcula_tabla);
			 i++;
            }
			
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
		}
		else{
			sel_calendario.dibujar();
		}
	}
		public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_nivel_cuenta_inicial.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Inicial", "");
			return;			

		}
		if(com_nivel_cuenta_final.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Final", "");
			return;			

		}
		
			
			
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
		
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_balance_inicial.limpiar();	
		utilitario.addUpdate("tab_balance_inicial");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.eliminar();
	}
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		Locale locale=new Locale("es","ES");
		
		if(rep_reporte.getReporteSelecionado().equals("Flujo del Efectivo")){
			
			String cuentas= inicial("p_cuenta_saldo_inicial");
			String tipo_inicial= inicial("p_tipo_asiento_inicial");
			
			String val_inicial = saldo(cuentas, tipo_inicial)	;	
			Double dou_inicial = pckUtilidades.CConversion.CDbl_2(val_inicial);
			
			String cobro_fuentes=valor("FUENTES");
			Double dou_fuentes=pckUtilidades.CConversion.CDbl_2(cobro_fuentes);
			
			String pago_usos=valor("USOS");		
			Double dou_usos=pckUtilidades.CConversion.CDbl_2(pago_usos);
			
			Double saldo_final = dou_inicial+dou_fuentes + dou_usos;
			//saldo_final= pckUtilidades.CConversion.CDbl_n(saldo_final,3);
			saldo_final= pckUtilidades.CConversion.CDbl_2(saldo_final);
			
			//System.out.println("dou_inicial: "+dou_inicial);
			//System.out.println("dou_fuentes: "+dou_fuentes);
			//System.out.println("dou_usos: "+dou_usos);
			
			TablaGenerica tab_valorBancos = utilitario.consultar("SELECT 1 as codigo, coalesce(sum(valor),0) as bancos FROM cont_balance_general where ide_cuenta in ("+utilitario.getVariable("p_cuenta_bancos")+");");
			String strCuadre="";
			
			if(tab_valorBancos.getTotalFilas()>0)
			{
				tab_valorBancos.imprimirSql();
				Double saldo_bancos=pckUtilidades.CConversion.CDbl_2(tab_valorBancos.getValor("bancos"));
				saldo_bancos= pckUtilidades.CConversion.CDbl_2(saldo_bancos);
				
				Double valorCuadre=saldo_bancos-saldo_final;
				valorCuadre= pckUtilidades.CConversion.CDbl_2(valorCuadre);
				
				System.out.println("saldo_bancos: "+saldo_bancos);
				System.out.println("saldo_final: "+saldo_final);
				System.out.println("valorCuadre: "+valorCuadre);
				
				if(valorCuadre!=0)
					strCuadre="BALANCE DESCUADRADO -> (" + valorCuadre +")";
				System.out.println("strCuadre: "+strCuadre);
			}
			
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				map_parametros.put("titulo","ESTADO DE FLUJO DEL EFECTIVO");
				
				map_parametros.put("pval_inicial", utilitario.getFormatoNumero(dou_inicial, 2) );				
				map_parametros.put("pval_cobro",   utilitario.getFormatoNumero(dou_fuentes, 2));				
				map_parametros.put("pval_pago",   utilitario.getFormatoNumero(dou_usos, 2));				
				map_parametros.put("pval_final",   utilitario.getFormatoNumero(saldo_final, 2));				
				map_parametros.put("p_cuadre",   strCuadre);
				map_parametros.put("gerente",  utilitario.getVariable("p_nombre_gerente"));				
				map_parametros.put("cargo_gerente",  utilitario.getVariable("p_cargo_gerente"));
				
				map_parametros.put("contador_general",  utilitario.getVariable("p_nombre_contador"));				
				map_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				map_parametros.put("REPORT_LOCALE", locale);
				
				sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
				sel_rep.dibujar();
				}
		} 
	}
	public String inicial(String parametro){
		String valor="";
		TablaGenerica tab_parametros = utilitario.consultar("select 1 as codigo,valor_para from sis_parametros where nom_para like '"+parametro+"' ");
		
		valor= tab_parametros.getValor("valor_para");
		//System.out.println(" parametros "+valor);

		return valor;
	}
	public String valor(String tipo){
		String valor ="";
		TablaGenerica tab_valor = utilitario.consultar("select tipo_grupo,sum(valor) as valor from ("
				+" select * from cont_flujo_efectivo where tipo_grupo='"+tipo+"' and con_ide_flujo_efectivo is null order by orden"
				+" ) a group by tipo_grupo");
		valor= tab_valor.getValor("valor");	
		//System.out.println(" valor por fuente sy susos "+valor);

		return valor;
	}
	
	public String saldo(String cuenta,String inicial){
		String valor="";
		String sql="select 1 as codigo,sum(debe_codem) as valor from cont_detalle_movimiento where ide_comov in ("
				+" select ide_comov from cont_movimiento where ide_cotia in ( "+inicial+" ) and ide_geani="+com_anio.getValue()+" ) and ide_cocac in ("+cuenta+")";
		TablaGenerica tab_valor=utilitario.consultar(sql);
		valor =tab_valor.getValor("valor");
		System.out.println("flujo efectivo sql saldo "+sql);

		return valor;
	}
	
	public Tabla getTab_balance_inicial() {
		return tab_balance_inicial;
	}

	public void setTab_balance_inicial(Tabla tab_balance_inicial) {
		this.tab_balance_inicial = tab_balance_inicial;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Combo getCom_nivel_cuenta_inicial() {
		return com_nivel_cuenta_inicial;
	}
	public void setCom_nivel_cuenta_inicial(Combo com_nivel_cuenta_inicial) {
		this.com_nivel_cuenta_inicial = com_nivel_cuenta_inicial;
	}
	public Combo getCom_nivel_cuenta_final() {
		return com_nivel_cuenta_final;
	}
	public void setCom_nivel_cuenta_final(Combo com_nivel_cuenta_final) {
		this.com_nivel_cuenta_final = com_nivel_cuenta_final;
	}
	public SeleccionTabla getSet_asiento_contable() {
		return set_asiento_contable;
	}
	public void setSet_asiento_contable(SeleccionTabla set_asiento_contable) {
		this.set_asiento_contable = set_asiento_contable;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}
	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSel_rep() {
		return sel_rep;
	}
	public void setSel_rep(SeleccionFormatoReporte sel_rep) {
		this.sel_rep = sel_rep;
	}

	


}
