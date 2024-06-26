package paq_facturacion;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;

public class pre_nota_debito_multiple extends Pantalla{
	private Tabla tab_debito=new Tabla ();
	private Tabla tab_detalle_debito=new Tabla ();
	private AutoCompletar aut_cliente=new AutoCompletar();
	private SeleccionTabla set_factura = new SeleccionTabla();
	public static String p_valor_interes_mora_nd;


	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);


	public pre_nota_debito_multiple(){
		tab_debito.setId("tab_debito");
		tab_debito.setHeader("NOTA DE DEBITO");
		tab_debito.setTabla("fac_nota_debito", "ide_fanod", 1);
		tab_debito.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_debito.getColumna("base_imponible_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("base_imponible_fanod").setEtiqueta();
		tab_debito.getColumna("interes_generado_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("interes_generado_fanod").setEtiqueta();
		tab_debito.getColumna("valor_iva_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("valor_iva_fanod").setEtiqueta();
		tab_debito.getColumna("total_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("total_fanod").setEtiqueta();
		tab_debito.getColumna("interes_aplicado_fanod").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_debito.getColumna("interes_aplicado_fanod").setEtiqueta();
		tab_debito.getColumna("fecha_emision_fanod").setValorDefecto(utilitario.getFechaActual());
		tab_debito.getColumna("ide_recli").setCombo(ser_facturacion.getClientesDatosBasicos("0,1"));
		tab_debito.getColumna("ide_recli").setAutoCompletar();
		tab_debito.getColumna("ide_recli").setLectura(true);
		//tab_debito.getColumna("ide_recli").setVisible(false);
		//tab_debito.setTipoFormulario(true);
		//tab_debito.getGrid().setColumns(4);
		tab_debito.agregarRelacion(tab_detalle_debito);
		tab_debito.dibujar();
		PanelTabla pat_debito=new PanelTabla();
		pat_debito.setPanelTabla(tab_debito);

		///// DETALLE DEBITO  
		tab_detalle_debito.setId("tab_detalle_debito");
		tab_detalle_debito.setHeader("NOTA DETALLE DEBITO");
		tab_detalle_debito.setTabla("fac_detalle_debito", "ide_faded", 2);
		//tab_detalle_debito.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_detalle_debito.getColumna("ide_fafac").setCombo(ser_facturacion.getCabeceraFactura("1",""));
		tab_detalle_debito.getColumna("ide_fafac").setAutoCompletar();
		tab_detalle_debito.getColumna("ide_fafac").setLectura(true);
		tab_detalle_debito.getColumna("base_imponible_faded").setEtiqueta();
		tab_detalle_debito.getColumna("base_imponible_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("interes_generado_faded").setEtiqueta();
		tab_detalle_debito.getColumna("interes_generado_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("valor_iva_faded").setEtiqueta();
		tab_detalle_debito.getColumna("valor_iva_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("total_faded").setEtiqueta();
		tab_detalle_debito.getColumna("total_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_detalle_debito.getColumna("interes_aplicado_faded").setEtiqueta();
		tab_detalle_debito.getColumna("interes_aplicado_faded").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo

		tab_detalle_debito.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle_debito);

		Division div_division=new Division();
		div_division.dividir2(pat_debito, pat_detalle, "50%", "H");
		agregarComponente(div_division);

		Boton bot_factura = new Boton();
		bot_factura.setValue("Buscar Clientes");
		bot_factura.setTitle("Factura");
		bot_factura.setIcon("ui-icon-person");
		bot_factura.setMetodo("importarFactura");
		bar_botones.agregarBoton(bot_factura);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_facturacion.getClientes("-1"),"ide_recli");
		set_factura.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("establecimiento_operativo_recli").setFiltro(true);
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.getTab_seleccion().ejecutarSql();
		agregarComponente(set_factura);

	}

	public void importarFactura(){

		set_factura.getTab_seleccion().setSql(ser_facturacion.getClientes("1,0"));
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.dibujar();
	}


	public  void aceptarFactura(){
		double dou_interes_generado=0;
		double dou_base_imponible=0;
		double dou_valor_iva=0;
		double dou_total=0;
		double dou_interes_aplicado=0;
		double dias_retrasado=0;
		
		String str_seleccion_cliente = set_factura.getSeleccionados();
		if (str_seleccion_cliente!=null){ 
			TablaGenerica tab_clientes = utilitario.consultar("select ide_recli,ide_retia from rec_clientes where ide_recli in ("+str_seleccion_cliente+")");
			
			for(int j=0;j<tab_clientes.getTotalFilas();j++){

			TablaGenerica tab_cliente_factura = ser_facturacion.getTablaGenericaFacturasVencidas(tab_clientes.getValor(j,"ide_recli"));
			 tab_debito.insertar();
	         tab_debito.setValor("ide_recli", tab_clientes.getValor(j,"ide_recli"));

			for(int k=0;k<tab_cliente_factura.getTotalFilas();k++){

				String str_seleccionado = tab_cliente_factura.getValor(k,"ide_fafac");
				//System.out.println("entra al str  "+str_seleccionado);
		         
		
				if (str_seleccionado!=null){
					TablaGenerica tab_aceptarfactura = ser_facturacion.getTablaGenericaFacturaCabecera(str_seleccionado);		
					System.out.println(" tabla generica"+tab_aceptarfactura.getSql());
					for(int i=0;i<tab_aceptarfactura.getTotalFilas();i++){

						tab_detalle_debito.insertar();
						tab_detalle_debito.setValor("ide_fafac", tab_aceptarfactura.getValor("ide_fafac"));
						tab_detalle_debito.setValor("base_imponible_faded",tab_aceptarfactura.getValor(i,"base_aprobada_fafac"));
						tab_detalle_debito.setValor("fecha_emision_factura_faded",tab_aceptarfactura.getValor(i,"fecha_transaccion_fafac"));			
						dou_base_imponible= pckUtilidades.CConversion.CDbl_2(tab_aceptarfactura.getValor(i,"base_aprobada_fafac"));
						Date fecha_fin= utilitario.getFecha(tab_debito.getValor("fecha_emision_fanod"));
						Date fecha_inicio= utilitario.getFecha(tab_aceptarfactura.getValor(i,"fecha_transaccion_fafac"));
						double dias_plazo=0;
						dias_plazo=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_dias_calculo_interes_mora_nd"));
						dias_retrasado= utilitario.getDiferenciasDeFechas(fecha_inicio, fecha_fin);
						System.out.println("dias retrasados"+dias_retrasado);
						double resultado_dias_retraso=0;
						resultado_dias_retraso=dias_retrasado-dias_plazo;
						String dias_retra=dias_retrasado+"";


						tab_detalle_debito.setValor("dias_retraso_faded",dias_retra );
						dou_interes_aplicado=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_interes_mora_nd"));
						String inte_apli=dou_interes_aplicado+"";
						tab_detalle_debito.setValor("interes_aplicado_faded",utilitario.getFormatoNumero(inte_apli,3));

						dou_interes_generado=((((dou_interes_aplicado/12)/30)*dou_base_imponible)*dias_retrasado)/100;
						String inte_generado=dou_interes_generado+"";
						tab_detalle_debito.setValor("interes_generado_faded",utilitario.getFormatoNumero(inte_generado,3));
						//dou_valor_iva=(dou_interes_generado+dou_base_imponible)*0.12;
						String val_iva=dou_valor_iva+"";
						tab_detalle_debito.setValor("valor_iva_faded",utilitario.getFormatoNumero(val_iva,3));
						dou_total=dou_base_imponible+dou_valor_iva+dou_interes_generado;
						String val_total=dou_total+"";
						tab_detalle_debito.setValor("total_faded",utilitario.getFormatoNumero( val_total,3));
						String valo_total=tab_detalle_debito.getSumaColumna("total_faded")+"";
						tab_debito.setValor("total_fanod",valo_total );
						String valo_iva=dou_valor_iva+"";
						tab_detalle_debito.setValor("valor_iva_faded",utilitario.getFormatoNumero(valo_iva,3));
						String iva_valo=tab_detalle_debito.getSumaColumna("valor_iva_faded")+"";
						tab_debito.setValor("valor_iva_fanod",iva_valo );

						String inte_gene=dou_interes_generado+"";
						tab_detalle_debito.setValor("interes_generado_faded",utilitario.getFormatoNumero(inte_gene,3));
						String inte_gener=tab_detalle_debito.getSumaColumna("interes_generado_faded")+"";
						tab_debito.setValor("interes_generado_fanod",inte_gener );

						String base_impo=dou_base_imponible+"";
						tab_detalle_debito.setValor("base_imponible_faded",utilitario.getFormatoNumero(base_impo,3));
						String base_imponi=tab_detalle_debito.getSumaColumna("base_imponible_faded")+"";
						tab_debito.setValor("base_imponible_fanod",base_imponi );
						//dou_interes_aplicado
						String inte_aplica=dou_interes_aplicado+"";
						tab_detalle_debito.setValor("interes_aplicado_faded",utilitario.getFormatoNumero(inte_aplica,3));
						String inte_aplicad=tab_detalle_debito.getSumaColumna("interes_aplicado_faded")+"";
						tab_debito.setValor("interes_aplicado_fanod",inte_aplicad );

					}
				}  // cierra for cliente factura
			}  // cierra for cliente
		} // cierra if clientes
			set_factura.cerrar();
			utilitario.addUpdate("tab_debito");
			utilitario.addUpdate("tab_detalle_debito");
		}
	}


	public void limpiar(){
		aut_cliente.limpiar();
		tab_debito.limpiar();
		tab_detalle_debito.limpiar();
		utilitario.addUpdate("aut_cliente");
	}

	//METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_cliente.onSelect(evt);
		tab_debito.setCondicion("ide_faded="+aut_cliente.getValor());
		tab_detalle_debito.ejecutarSql();
		tab_detalle_debito.ejecutarValorForanea(tab_debito.getValorSeleccionado());
	}


	///CALCULAR 

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(aut_cliente.getValor()!=null){
			if(tab_debito.isFocus()){
				tab_debito.getColumna("ide_recli").setValorDefecto(aut_cliente.getValor());
				tab_debito.insertar();

			}
			else if(tab_detalle_debito.isFocus()){
				tab_detalle_debito.insertar();
			}
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_debito.guardar()){
			tab_detalle_debito.guardar();
		}
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();


	}

	public Tabla getTab_debito() {
		return tab_debito;
	}

	public void setTab_debito(Tabla tab_debito) {
		this.tab_debito = tab_debito;

	}

	public Tabla getTab_detalle_debito() {
		return tab_detalle_debito;
	}

	public void setTab_detalle_debito(Tabla tab_detalle_debito) {
		this.tab_detalle_debito = tab_detalle_debito;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	public SeleccionTabla getSet_factura() {
		return set_factura;
	}

	public void setSet_factura(SeleccionTabla set_factura) {
		this.set_factura = set_factura;
	}


}
