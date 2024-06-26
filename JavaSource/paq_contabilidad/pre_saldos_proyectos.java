package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;



import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_saldos_proyectos extends Pantalla{

	private Tabla tab_movimientos_contables=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Radio rad_mayoriza_desmayoriza= new Radio();
	private Combo com_mes = new Combo();

	public static String par_tipo_asiento_inicial;

	
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_saldos_proyectos() {
		// Este parametro contiene el el tipo siento inicial de apertura
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial"); 
		
		bar_botones.limpiar();
		
		
		
		tab_movimientos_contables.setId("tab_movimientos_contables");  
		tab_movimientos_contables.setHeader("SALDOS POR PORYECTOS");
		tab_movimientos_contables.setSql("select a.ide_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_programa,detalle_subactividad,descripcion_clasificador_prcla,programa,"
+" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad,"
+" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa,"
+" presupuesto_codificado_prpoa,reforma_prpoa,saldo_poa,detalle_geani,detalle_geare"
+" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join" 
+" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto,"
+" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,"
+" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , "
+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad"
 +" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, "
+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, "
 +" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, "
+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup"
+" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup"
+" left join gen_area g on a.ide_geare=g.ide_geare "
+" left join ("
+" select a.ide_prpoa,presupuesto_codificado_prpoa - (case when valor_certificado_prpoc is null then 0 else valor_certificado_prpoc end) as saldo_poa"
+" from pre_poa a"
+" left join (select sum(valor_certificado_prpoc) as valor_certificado_prpoc,ide_prpoa from pre_poa_certificacion group by ide_prpoa) b on a.ide_prpoa = b.ide_prpoa"
+" ) h on a.ide_prpoa = h.ide_prpoa"
+" where a.ide_geani= 7 order by codigo_subactividad,a.ide_prpoa");	
		tab_movimientos_contables.setNumeroTabla(1);
		tab_movimientos_contables.setCampoPrimaria("ide_prpoa");
		tab_movimientos_contables.getColumna("ide_prpoa").setNombreVisual("CODIGO");

		tab_movimientos_contables.getColumna("codigo_clasificador_prcla").setFiltro(true);
		tab_movimientos_contables.getColumna("descripcion_clasificador_prcla").setFiltro(true);
		tab_movimientos_contables.getColumna("detalle_geare").setFiltro(true);
		tab_movimientos_contables.getColumna("codigo_subactividad").setFiltro(true);
		tab_movimientos_contables.getColumna("detalle_programa").setFiltro(true);
		tab_movimientos_contables.getColumna("detalle_subactividad").setFiltro(true);
		tab_movimientos_contables.getColumna("detalle_actividad").setFiltro(true);
		
		tab_movimientos_contables.setRows(20);
		tab_movimientos_contables.setLectura(true);

		tab_movimientos_contables.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_movimientos_contables);
		pat_balance_inicial.getMenuTabla().getItem_formato().setDisabled(true);

		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		
	}

	public void desmayoriza(){
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
			return;	
		}
			else if(com_mes.getValue() != null){
				
				ser_contabilidad.desmayorizaAsientos(com_anio.getValue().toString(), com_mes.getValue().toString());
				utilitario.agregarMensajeInfo("Desmayorizado", "Asientos desmayorizados");

			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
				return;		
			}
	}
	public void generarBalanceComprobacion(){
	
	if (com_anio.getValue()==null){
		utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
		return;	
	}
		else if(com_mes.getValue() != null){
			
			if (!con_guardar.isVisible()){
				con_guardar.setMessage("ESTA SEGURO DE MAYORIZAR Y GENERAR EL BALANCE DE COMPROBACION");
				con_guardar.setTitle("CONFIRMACION DE CALCULO");
				con_guardar.getBot_aceptar().setMetodo("generarBalanceComprobacion");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();
				ser_contabilidad.generarBalanceComprobacion(com_anio.getValue().toString(), com_mes.getValue().toString(), par_tipo_asiento_inicial);
			    utilitario.agregarMensajeInfo("Se Mayorizo", "Se mayorizo y genero el balance de comprobaciòn");	
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
			return;		
		}
	}
	public void seleccionaOpcion (){
		String meses_todos="1,2,3,4,5,6,7,8,9,10,11,12";
		String estado="true,false";
		if(com_anio.getValue()==null){
			
			tab_movimientos_contables.limpiar();
			utilitario.addUpdate("tab_movimientos_contables");
			return;

		}
		if(com_mes.getValue()!=null){
			meses_todos=com_mes.getValue().toString();
		}
		System.out.println("valor radio "+rad_mayoriza_desmayoriza.getValue());
		if(rad_mayoriza_desmayoriza.getValue().equals("true")){
			estado="true";
		}
		if(rad_mayoriza_desmayoriza.getValue().equals("false")){
			estado="false";
		}
		
			tab_movimientos_contables.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaber(com_anio.getValue().toString(),meses_todos,estado,par_tipo_asiento_inicial));	
			tab_movimientos_contables.ejecutarSql();
		
		utilitario.addUpdate("tab_movimientos_contables");
	}
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_movimientos_contables.limpiar();	
		utilitario.addUpdate("tab_movimientos_contables");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.eliminar();
	}

	public void seleccionarTodas() {
		tab_movimientos_contables.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_movimientos_contables.getTotalFilas()];
        for (int i = 0; i < tab_movimientos_contables.getFilas().size(); i++) {
            seleccionados[i] = tab_movimientos_contables.getFilas().get(i);
        }
        tab_movimientos_contables.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_movimientos_contables.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_movimientos_contables.getSeleccionados().length == tab_movimientos_contables.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_movimientos_contables.getTotalFilas() - tab_movimientos_contables.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_movimientos_contables.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_movimientos_contables.getSeleccionados().length; j++) {
                    if (tab_movimientos_contables.getSeleccionados()[j].equals(tab_movimientos_contables.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_movimientos_contables.getFilas().get(i);
                    cont++;
                }
            }
            tab_movimientos_contables.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_movimientos_contables.setSeleccionados(null);
    }

	public Tabla getTab_movimientos_contables() {
		return tab_movimientos_contables;
	}
	public void setTab_movimientos_contables(Tabla tab_movimientos_contables) {
		this.tab_movimientos_contables = tab_movimientos_contables;
	}
	public SeleccionTabla getSet_asiento_contable() {
		return set_asiento_contable;
	}
	public void setSet_asiento_contable(SeleccionTabla set_asiento_contable) {
		this.set_asiento_contable = set_asiento_contable;
	}
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Combo getCom_mes() {
		return com_mes;
	}

	public void setCom_mes(Combo com_mes) {
		this.com_mes = com_mes;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	


}
