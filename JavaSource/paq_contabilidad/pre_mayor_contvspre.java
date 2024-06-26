package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;



import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_mayor_contvspre extends Pantalla{

	private AutoCompletar aut_catalogo_cuentas = new AutoCompletar();
	private AutoCompletar aut_catalogo_presupuestario = new AutoCompletar();
	private Combo com_tipo_cuenta = new Combo();
	private SeleccionCalendario sel_calendario = new SeleccionCalendario();
	private Tabla tab_mayor_cont_pre=new Tabla();
	private Tabla tab_mayor_pre_cont= new Tabla();
	private SeleccionTabla seltab_cuenta = new SeleccionTabla();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	
	public pre_mayor_contvspre() {
		/*
			aut_catalogo_cuentas.setId("aut_catalogo_cuentas");
			aut_catalogo_cuentas.setAutoCompletar(ser_contabilidad.getCuentaContable("true")); 
			Etiqueta eti_catalogo=new Etiqueta("Cuenta Contable:");
		*/	
			aut_catalogo_presupuestario.setId("aut_catalogo_presupuestario");
			aut_catalogo_presupuestario.setAutoCompletar(ser_presupuesto.getCatalogoPresupuestario("true")); 
			Etiqueta eti_presupuesto=new Etiqueta("Partida Presupuestaria:");
			Boton bot_limpiar = new Boton();
			bot_limpiar.setIcon("ui-icon-cancel");
			bot_limpiar.setMetodo("limpiar");
			//bar_botones.agregarComponente(eti_catalogo);
			//bar_botones.agregarComponente(aut_catalogo_cuentas);
			bar_botones.agregarComponente(eti_presupuesto);
			bar_botones.agregarComponente(aut_catalogo_presupuestario);
			
			  List lista = new ArrayList();
		       Object fila1[] = {
		           "1", "INGRESOS"
		       };
		       Object fila2[] = {
		           "0", "GASTOS"
		       };
		       
		       lista.add(fila1);
		       lista.add(fila2);
			com_tipo_cuenta.setId("com_tipo_cuenta");
			com_tipo_cuenta.setCombo(lista);
			bar_botones.agregarComponente(com_tipo_cuenta);
			
		       
			Boton bot_generar = new Boton();
			bot_generar.setId("bot_generar");
			bot_generar.setValue("Generar Movimientos Financieros");
			bot_generar.setMetodo("generar");
			bar_botones.agregarBoton(bot_generar);
			
			sel_calendario.setId("sel_calendario");
			agregarComponente(sel_calendario);
			
			
			seltab_cuenta.setId("seltab_cuenta");
			seltab_cuenta.setSeleccionTabla(ser_contabilidad.getCuentaContable("null"),"ide_cocac");
			seltab_cuenta.getTab_seleccion().getColumna("cue_codigo_cocac").setFiltro(true);;
			seltab_cuenta.setTitle("Seleccione la Cuenta Contable");
			seltab_cuenta.getBot_aceptar().setMetodo("generar");
			agregarComponente(seltab_cuenta);
			
			
			tab_mayor_cont_pre.setId("tab_mayor_cont_pre");
			tab_mayor_cont_pre.setHeader("MOVIMIENTOS CONTABLES VS MOVIMIENTOS PRESUPUESTARIOS");
			tab_mayor_cont_pre.setSql("select 1 as ide_codem, 2 as mov_fecha_comov,3 as cue_codigo_cocac,4 as cue_descripcion_cocac,5 as ide_comov,6 as debe_codem,7 as haber_codem,8 as devengado_prmen,8 as cobrado_prmen,9 as codigo_clasificador_prcla,10 as descripcion_clasificador_prcla, 11 as detalle_prfuf where 1=0");
			tab_mayor_cont_pre.setCampoPrimaria("ide_codem");
			tab_mayor_cont_pre.setColumnaSuma("debe_codem,haber_codem,devengado_prmen,cobrado_prmen");
			tab_mayor_cont_pre.setLectura(true);
			tab_mayor_cont_pre.dibujar();
			tab_mayor_cont_pre.setRows(5);
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_mayor_cont_pre);
			
			
			tab_mayor_pre_cont.setId("tab_mayor_pre_cont");
			tab_mayor_pre_cont.setHeader("MOVIMIENTOS PRESUPUESTARIOS VS MOVIMIENTOS CONTABLES");
			tab_mayor_pre_cont.setSql("select 1 as ide_codem, 2 as mov_fecha_comov,3 as cue_codigo_cocac,4 as cue_descripcion_cocac,5 as ide_comov,6 as debe_codem,7 as haber_codem,8 as devengado_prmen,8 as cobrado_prmen,9 as codigo_clasificador_prcla,10 as descripcion_clasificador_prcla, 11 as detalle_prfuf where 1=0");
			tab_mayor_pre_cont.setCampoPrimaria("ide_codem");
			tab_mayor_pre_cont.setColumnaSuma("debe_codem,haber_codem,devengado_prmen,cobrado_prmen");

			tab_mayor_pre_cont.setLectura(true);
			tab_mayor_pre_cont.dibujar();
			tab_mayor_pre_cont.setRows(5);
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setPanelTabla(tab_mayor_pre_cont);
			
			Division div_dividir = new Division();
			div_dividir.dividir2(pat_panel1, pat_panel2, "50%", "H");
			agregarComponente(div_dividir);
			
			
			
						
	}
	public void limpiar(){
		aut_catalogo_presupuestario.limpiar();
		tab_mayor_cont_pre.limpiar();
		tab_mayor_pre_cont.limpiar();
		utilitario.addUpdate("aut_catalogo_presupuestario");
		
	}
	public void generar(){
		
			if(aut_catalogo_presupuestario.getValor()!=null){
				
				if(com_tipo_cuenta.getValue()!=null){
					if(sel_calendario.isVisible()){
						
						
						if (seltab_cuenta.isVisible()){
							
							String seleccionado = seltab_cuenta.getSeleccionados();
							sel_calendario.cerrar();
							seltab_cuenta.cerrar();
							tab_mayor_cont_pre.setSql(sqlGeneraMayor("1", com_tipo_cuenta.getValue().toString(), sel_calendario.getFecha1String(),sel_calendario.getFecha2String(),seleccionado, aut_catalogo_presupuestario.getValor()));
							tab_mayor_pre_cont.setSql(sqlGeneraMayor("2", com_tipo_cuenta.getValue().toString(), sel_calendario.getFecha1String(),sel_calendario.getFecha2String(),seleccionado, aut_catalogo_presupuestario.getValor()));
						
							tab_mayor_cont_pre.ejecutarSql();
							tab_mayor_pre_cont.ejecutarSql();
						
							tab_mayor_cont_pre.dibujar();
							tab_mayor_pre_cont.dibujar();
						}
						else {
							seltab_cuenta.getTab_seleccion().setSql(ser_contabilidad.getCuentaContable("true"));
							seltab_cuenta.getTab_seleccion().ejecutarSql();
							seltab_cuenta.dibujar();	
						}
					}
					else{
						calendarioDibujar();

					}
					
									}
				else {
					utilitario.agregarMensajeError("Seleccione un Cuenta", "Seleccione un Tipo de Cuenta");
					return;					
				}
				
			}
			else {
				utilitario.agregarMensajeError("Seleccione un Cuenta", "Seleccione una Partida Presupuestaria");
				return;
			}

		
		
		System.out.println("paso por aqui22");

	}
	public void calendarioDibujar(){
		sel_calendario.setTitle("SELECCIONAR RANGO DE FECHAS");
		sel_calendario.setFooter("Para actualizar el reporte seleccione un rango de fechas");
		sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generar");
		sel_calendario.dibujar();
	}
	/*
	 * El presnte metedod permitira generar de forma dinamica los mayores analiticos Tbla 1= mayor contable vs presupuesto
	 * Tabla 2= mayor prsupuestario vs contabilidad
	 */
	public String sqlGeneraMayor(String numero_tabla,String tipo_cuenta,String fechaini,String fechafin,String cuenta,String partida){
		String sql="";
		
		//TablaGenerica tab_cuenta = utilitario.consultar("select ide_cocac,cue_codigo_cocac from cont_catalogo_cuenta where ide_cocac in ("+cuenta+")");
		
		TablaGenerica tab_partida = utilitario.consultar("select ide_prcla,codigo_clasificador_prcla from pre_clasificador where ide_prcla="+partida);
		
		String cabecera_sql="select a.ide_codem,mov_fecha_comov,cue_codigo_cocac,cue_descripcion_cocac,ide_comov,debe_codem,haber_codem,devengado_prmen,cobrado_prmen,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_prfuf from (";
		
		String sql_contabilidad="select b.ide_codem,mov_fecha_comov,cue_codigo_cocac,cue_descripcion_cocac,a.ide_comov,debe_codem,haber_codem from  cont_movimiento a"
				+" left join cont_detalle_movimiento b on a.ide_comov = b.ide_comov"
				+" left join cont_catalogo_cuenta c on b.ide_cocac = c.ide_cocac where not ide_codem is null ";
		
		String sql_presupuesto_gasto="select a.ide_prpoa,c.ide_prcla,devengado_prmen,cobrado_prmen,codigo_clasificador_prcla,descripcion_clasificador_prcla,ide_codem,detalle_prfuf"
				+" from pre_anual a "
				 +" join pre_mensual b on b.ide_pranu = a.ide_pranu"
				 +" join pre_poa c on c.ide_prpoa=a.ide_prpoa"
				 +" join pre_clasificador d on d.ide_prcla=c.ide_prcla"
				 +" left join pre_fuente_financiamiento ff on ff.ide_prfuf=b.ide_prfuf";

		String sql_presupuesto_ingreso="select a.ide_prcla,devengado_prmen,cobrado_prmen,codigo_clasificador_prcla,descripcion_clasificador_prcla,ide_codem,detalle_prfuf"
				 +" from pre_anual a"
				 +" join pre_mensual b on b.ide_pranu = a.ide_pranu "
				 +" join pre_clasificador d on d.ide_prcla = a.ide_prcla"
				 +" left join pre_fuente_financiamiento ff on ff.ide_prfuf=b.ide_prfuf";
		
		String sql_orden_pie=" order by cue_codigo_cocac,mov_fecha_comov,ide_comov";
		
		sql+=cabecera_sql;  // carga la cabecera tanto para tabla 1 y tabla 2
		
		if(numero_tabla.equals("1")){ // CONTAVILIDAD VS PRESUPUESTO
			
			sql += sql_contabilidad +" and mov_fecha_comov between '"+fechaini+"' and '"+fechafin+"'  and b.ide_cocac in( "+cuenta+") ) a " ;
			
			if(tipo_cuenta.equals("1")){ // cuando tipo cuenta es iguala 1 es INGRESOS caso contrario Gastos
			
				sql +="left join ("+sql_presupuesto_ingreso+") b on a.ide_codem = b.ide_codem ";
			}
			else if(tipo_cuenta.equals("0")){// cuando tipo cuenta es igula a 0 GASTOS
				sql +=" left join ("+sql_presupuesto_gasto+") b on a.ide_codem = b.ide_codem";
			}
						
		}
		
		if(numero_tabla.equals("2")){ // PRESUPUESTO VS CONTABILIDAD
			
			if(tipo_cuenta.equals("1")){ // cuando tipo cuenta es iguala 1 es INGRESOS caso contrario Gastos
				
				sql +=sql_presupuesto_ingreso+" and fecha_ejecucion_prmen between '"+fechaini+"' and '"+fechafin+"' and codigo_clasificador_prcla like '"+tab_partida.getValor("codigo_clasificador_prcla")+"%' ) a";
			}
			else if(tipo_cuenta.equals("0")){// cuando tipo cuenta es igula a 0 GASTOS
				sql +=sql_presupuesto_gasto+" and fecha_ejecucion_prmen between '"+fechaini+"' and '"+fechafin+"' and codigo_clasificador_prcla like '"+tab_partida.getValor("codigo_clasificador_prcla")+"%' ) a";
			}
			
			sql += " left join ("+sql_contabilidad +") b on a.ide_codem = b.ide_codem " ;
			
		}
		
		sql +=sql_orden_pie;
		System.out.println(" retorno sql "+sql);
		return sql;
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
	public AutoCompletar getAut_catalogo_cuentas() {
		return aut_catalogo_cuentas;
	}
	public void setAut_catalogo_cuentas(AutoCompletar aut_catalogo_cuentas) {
		this.aut_catalogo_cuentas = aut_catalogo_cuentas;
	}
	public AutoCompletar getAut_catalogo_presupuestario() {
		return aut_catalogo_presupuestario;
	}
	public void setAut_catalogo_presupuestario(
			AutoCompletar aut_catalogo_presupuestario) {
		this.aut_catalogo_presupuestario = aut_catalogo_presupuestario;
	}
	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}
	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}
	public Tabla getTab_mayor_cont_pre() {
		return tab_mayor_cont_pre;
	}
	public void setTab_mayor_cont_pre(Tabla tab_mayor_cont_pre) {
		this.tab_mayor_cont_pre = tab_mayor_cont_pre;
	}
	public Tabla getTab_mayor_pre_cont() {
		return tab_mayor_pre_cont;
	}
	public void setTab_mayor_pre_cont(Tabla tab_mayor_pre_cont) {
		this.tab_mayor_pre_cont = tab_mayor_pre_cont;
	}
	public SeleccionTabla getSeltab_cuenta() {
		return seltab_cuenta;
	}
	public void setSeltab_cuenta(SeleccionTabla seltab_cuenta) {
		this.seltab_cuenta = seltab_cuenta;
	}




}
