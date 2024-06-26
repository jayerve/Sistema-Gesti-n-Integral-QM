package paq_presupuesto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.poi.hssf.util.HSSFColor.TAN;

import oracle.net.aso.d;



import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_aprobar_resolucion_poa extends Pantalla{

	Tabla tab_libera_compromiso = new Tabla();
	Dialogo dis_liberar = new Dialogo();
	Calendario cal_fecha = new Calendario();
	Texto txt_num_resolucion = new Texto();
	private SeleccionTabla sel_resolucion= new SeleccionTabla();
	private Radio rad_aprobado = new Radio();
	private Etiqueta eti_mensaje = new Etiqueta();
	private Dialogo dia_confirma = new Dialogo();

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_aprobar_resolucion_poa() {
		
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		Boton bot_resolucion = new Boton();
		bot_resolucion.setId("bot_resolucion");
		bot_resolucion.setIcon("ui-icon-person");
		bot_resolucion.setValue("BUSCAR RESOLUCION");
		bot_resolucion.setMetodo("dibujaDialogo");
		bar_botones.agregarBoton(bot_resolucion);
		
		Boton bot_aprobara = new Boton();
		bot_aprobara.setId("bot_aprobara");
		bot_aprobara.setIcon("ui-icon-person");
		bot_aprobara.setValue("APROBAR REFORMA");
		bot_aprobara.setMetodo("dibujaConfirmar");
		bar_botones.agregarBoton(bot_aprobara);
		
		
		eti_mensaje.setId("eti_mensaje");
		eti_mensaje.setValue("Mensajeando");
		eti_mensaje.setStyle("font-size: 18px;color: red;font-weight: bold");

		
		dia_confirma.setId("dia_confirma");
		dia_confirma.setWidth("50%");
		dia_confirma.setHeight("30%");
		dia_confirma.getBot_aceptar().setMetodo("dibujaConfirmar");
		dia_confirma.setDialogo(eti_mensaje);
		
		agregarComponente(dia_confirma);
		
		
		List listax = new ArrayList();
	    Object filax1[] = {
	       "true", "Aprobado"
	    };
	    Object filax2[] = {
	       "false", "No Aprobado"
	    };
	   
	    listax.add(filax1);
	    listax.add(filax2);
	    rad_aprobado.setId("rad_aprobado");
	    rad_aprobado.setRadio(listax);
	    rad_aprobado.setValue(filax1);
	    rad_aprobado.setMetodoChange("consultaReformas");
	    bar_botones.agregarComponente(rad_aprobado);
		
		 	BotonesCombo boc_seleccion_inversa = new BotonesCombo();
	        ItemMenu itm_todas = new ItemMenu();
	        ItemMenu itm_niguna = new ItemMenu();


	        boc_seleccion_inversa.setValue("Selección Inversa");
	        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
	        boc_seleccion_inversa.setMetodo("seleccinarInversa");
	        boc_seleccion_inversa.setUpdate("tab_libera_compromiso");
	        itm_todas.setValue("Seleccionar Todo");
	        itm_todas.setIcon("ui-icon-check");
	        itm_todas.setMetodo("seleccionarTodas");
	        itm_todas.setUpdate("tab_libera_compromiso");
	        boc_seleccion_inversa.agregarBoton(itm_todas);
	        itm_niguna.setValue("Seleccionar Ninguna");
	        itm_niguna.setIcon("ui-icon-minus");
	        itm_niguna.setMetodo( "seleccionarNinguna");
	        itm_niguna.setUpdate("tab_libera_compromiso");
	        boc_seleccion_inversa.agregarBoton(itm_niguna);
		
		tab_libera_compromiso.setId("tab_libera_compromiso");
		tab_libera_compromiso.setSql(datosSql(" where 1= 0 "));
		tab_libera_compromiso.getColumna("resolucion_prprf").setFiltroContenido();
		tab_libera_compromiso.getColumna("detalle_subactividad").setFiltroContenido();
		//tab_libera_compromiso.getSumaColumna("valor_reformado_prprf");
		tab_libera_compromiso.setColumnaSuma("valor_reformado_prprf");
		tab_libera_compromiso.setLectura(true);
		tab_libera_compromiso.setTipoSeleccion(true);
		tab_libera_compromiso.setNumeroTabla(1);
		tab_libera_compromiso.setRows(20);
		tab_libera_compromiso.dibujar();
		
		
        PanelTabla pat_panel = new PanelTabla();
        //pat_panel.setHeader(gri_formulario);
        pat_panel.getChildren().add(boc_seleccion_inversa);
        pat_panel.setPanelTabla(tab_libera_compromiso);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);	
		inicializarSelResolucion();
		
	}

	public void consultaReformas(){
		tab_libera_compromiso.setSql(datosSql(" where aprobado_prprf ="+rad_aprobado.getValue().toString()));
		tab_libera_compromiso.ejecutarSql();
	}
	public void dibujaConfirmar(){
		
		if(tab_libera_compromiso.getSeleccionados().length>0){
			TablaGenerica tab_reformas=utilitario.consultar("select 1 as codigo,count(*) as numero, resolucion_prprf from ( select * from pre_poa_reforma_fuente where ide_prprf in ("+tab_libera_compromiso.getFilasSeleccionadas()+") ) a group by resolucion_prprf order by resolucion_prprf");

			if(dia_confirma.isVisible()){
				
				String actualiza_reformas="update pre_poa_reforma_fuente set aprobado_prprf=true where ide_prprf in ("+tab_libera_compromiso.getFilasSeleccionadas()+")";
				utilitario.getConexion().ejecutarSql(actualiza_reformas);
				
				TablaGenerica tab_poa_fuente = utilitario.consultar("select * from pre_poa_reforma_fuente where ide_prprf in ("+tab_libera_compromiso.getFilasSeleccionadas()+")");
				
				for(int j=0;j<tab_poa_fuente.getTotalFilas();j++){
					ser_presupuesto.trigActualizaReformaFuente(tab_poa_fuente.getValor(j, "ide_prpoa"));
					ser_presupuesto.trigActualizaReforma(tab_poa_fuente.getValor(j, "ide_prpoa"));

					
				}				
				
				tab_libera_compromiso.setSql(datosSql(" where 1= 0 "));
				tab_libera_compromiso.ejecutarSql();
				utilitario.agregarMensaje("Aprobado", "Se guardo las Reformas");
				dia_confirma.cerrar();
				
			}
			else {
				String mensaj="";
				for (int i=0;i< tab_reformas.getTotalFilas();i++){
					mensaj +="\n Usted va a aprobar ( "+tab_reformas.getValor(i,"numero")+" ) nro. de registros correspondientes a la Reforma:  "+tab_reformas.getValor(i,"resolucion_prprf")+"\n";
				}
				eti_mensaje.setValue(mensaj);

				utilitario.addUpdate("eti_mensaje");
				dia_confirma.dibujar();
			}

		}
		else {
			utilitario.agregarMensajeError("No existen registros seleccionados", "Seleccione un registro para proceder a la liberación del Compromiso");	
			return;
		}
	}
	public void inicializarSelResolucion (){
	/////dialogo para reporte
			sel_resolucion.setId("sel_resolucion");
			sel_resolucion.setSeleccionTabla("select resolucion_prpor as codigo,resolucion_prpor " +
					" from pre_poa_reforma group by resolucion_prpor","codigo");
			sel_resolucion.getTab_seleccion().ejecutarSql();
			sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
			sel_resolucion.setRadio();
			sel_resolucion.getBot_aceptar().setMetodo("actualizarTabla");
			agregarComponente(sel_resolucion);
		
	}
	public void dibujaDialogo (){
		sel_resolucion.getTab_seleccion().setSql("select resolucion_prpor as codigo,resolucion_prpor " +
				" from pre_poa_reforma group by resolucion_prpor");
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("actualizarTabla");
		sel_resolucion.dibujar();
	}
	public void actualizarTabla(){
		String seleccionados=sel_resolucion.getValorSeleccionado();
		//System.out.println("Seleccionados "+seleccionados);
		tab_libera_compromiso.setSql(" where resolucion_prprf in ('"+seleccionados+"')");
		tab_libera_compromiso.ejecutarSql();
		sel_resolucion.cerrar();
	}
	public String datosSql(String resolucion){
		String sql="select ide_prprf,valor_reformado_prprf,"
				+" resolucion_prprf,fecha_prprf,activo_prprf,aprobado_prprf,codigo_subactividad,detalle_subactividad,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_prfuf"
				+" from pre_poa_reforma_fuente a"
				+" left join ("
				
				+" select a.ide_prpoa,codigo_subactividad,detalle_subactividad,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_poa a"
				+" left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join "
				+" ( " +ser_presupuesto.getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup"
				+" left join gen_area g on a.ide_geare=g.ide_geare  order by codigo_subactividad,a.ide_prpoa "
				+" ) b on  a.ide_prpoa = b.ide_prpoa"
				
				+" left join pre_fuente_financiamiento c on a.ide_prfuf = c.ide_prfuf"
				+ resolucion +" order by resolucion_prprf,codigo_subactividad";
		
		System.out.println("datosSql aprobar reforma: "+sql);
		
		return sql;
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
		tab_libera_compromiso.setSeleccionados(null);
		Fila seleccionados[] = new Fila[tab_libera_compromiso.getTotalFilas()];
		for (int i = 0; i < tab_libera_compromiso.getFilas().size(); i++) {
			seleccionados[i] = tab_libera_compromiso.getFilas().get(i);
		}
		tab_libera_compromiso.setSeleccionados(seleccionados);
		
	}

	/**DFJ**/
	public void seleccinarInversa() {
		if (tab_libera_compromiso.getSeleccionados() == null) {
			seleccionarTodas();
		} else if (tab_libera_compromiso.getSeleccionados().length == tab_libera_compromiso.getTotalFilas()) {
			seleccionarNinguna();
		} else {
			Fila seleccionados[] = new Fila[tab_libera_compromiso.getTotalFilas() - tab_libera_compromiso.getSeleccionados().length];
			int cont = 0;
			for (int i = 0; i < tab_libera_compromiso.getFilas().size(); i++) {
				boolean boo_selecionado = false;
				for (int j = 0; j < tab_libera_compromiso.getSeleccionados().length; j++) {
					if (tab_libera_compromiso.getSeleccionados()[j].equals(tab_libera_compromiso.getFilas().get(i))) {
						boo_selecionado = true;
						break;
					}
				}
				if (boo_selecionado == false) {
					seleccionados[cont] = tab_libera_compromiso.getFilas().get(i);
					cont++;
				}
			}
			tab_libera_compromiso.setSeleccionados(seleccionados);
		}
	}

	/**DFJ**/
	public void seleccionarNinguna() {
		tab_libera_compromiso.setSeleccionados(null);

	}

public Tabla getTab_libera_compromiso() {
	return tab_libera_compromiso;
}
public void setTab_libera_compromiso(Tabla tab_libera_compromiso) {
	this.tab_libera_compromiso = tab_libera_compromiso;
}

public SeleccionTabla getSel_resolucion() {
	return sel_resolucion;
}

public void setSel_resolucion(SeleccionTabla sel_resolucion) {
	this.sel_resolucion = sel_resolucion;
}

public Dialogo getDia_confirma() {
	return dia_confirma;
}

public void setDia_confirma(Dialogo dia_confirma) {
	this.dia_confirma = dia_confirma;
}
	


}
