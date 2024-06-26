package paq_bodega;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_ingreso_material_solicitud extends Pantalla{

	private Tabla tab_ingreso_material= new Tabla();
	private Tabla tab_solicitud=new Tabla();
	private AutoCompletar aut_ing_material= new AutoCompletar();
	private SeleccionTabla set_solicitud = new SeleccionTabla();
	private Dialogo dia_recibir_solicitud=new Dialogo();
	public static String par_secuencial_modulo;



	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_ingreso_material_solicitud() {
		bar_botones.limpiar();
		par_secuencial_modulo=utilitario.getVariable("p_modulo_sec_bod_ingresos");

		Boton bot_material = new Boton();
		bot_material.setValue("Buscar Factura Solicitud Compra");
		bot_material.setTitle("Solicitud Compra");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarSolicitud");
		bar_botones.agregarBoton(bot_material);
		
		Boton bot_solicitud=new Boton();
		bot_solicitud.setIcon("ui-icon-person");
		bot_solicitud.setValue("Recibir Solicitud");
		bot_solicitud.setMetodo("abrirRecibirSolicitud");
		bar_botones.agregarBoton(bot_solicitud);

		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
		ItemMenu itm_todas = new ItemMenu();
		ItemMenu itm_niguna = new ItemMenu();

		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_solicitud");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_solicitud");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo( "seleccionarNinguna");
		itm_niguna.setUpdate("tab_solicitud");
		boc_seleccion_inversa.agregarBoton(itm_niguna);

		tab_solicitud.setId("tab_solicitud");
		tab_solicitud.setSql("select b.ide_addef,detalle_bomat,codigo_bomat,num_factura_adfac,valor_adsoc,nro_solicitud_adsoc,valor_total_addef,valor_unitario_addef,cantidad_addef," +
				" detalle_adsoc from adq_solicitud_compra a,adq_detalle_factura b,adq_factura c , bodt_material d" +
				" where a.ide_adsoc=c.ide_adsoc and b.ide_adfac=c.ide_adfac  and d.ide_bomat=b.ide_bomat and a.ide_adsoc=-1 order by codigo_bomat");
		tab_solicitud.setNumeroTabla(2);
		tab_solicitud.setCampoPrimaria("ide_addef");
		tab_solicitud.getColumna("detalle_adsoc").setNombreVisual("JUSTIFICACION DE LA SOLICITUD");
		tab_solicitud.getColumna("num_factura_adfac").setNombreVisual("NUMERO FACTURA");
		tab_solicitud.getColumna("valor_adsoc").setNombreVisual("VALOR SOLICITUD");
		tab_solicitud.getColumna("nro_solicitud_adsoc").setNombreVisual("NRO. SOLICITUD");
		tab_solicitud.getColumna("valor_total_addef").setNombreVisual("VALOR TOTAL COMPRA");
		tab_solicitud.getColumna("valor_unitario_addef").setNombreVisual("VALOR UNITARIO");
		tab_solicitud.getColumna("cantidad_addef").setNombreVisual("CANTIDAD");
		tab_solicitud.getColumna("codigo_bomat").setNombreVisual("CODIGO MATERIAL");
		tab_solicitud.getColumna("detalle_bomat").setNombreVisual("DETALLE MATERIAL");

		tab_solicitud.setLectura(true);
		tab_solicitud.setTipoSeleccion(true);
		tab_solicitud.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.getChildren().add(boc_seleccion_inversa);
		pat_panel.setPanelTabla(tab_solicitud);
		pat_panel.getMenuTabla().getItem_formato().setDisabled(true);

		Division div_division=new Division();
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
	
		set_solicitud.setId("set_solicitud");
		set_solicitud.setSeleccionTabla(ser_bodega.getSolicitudFactura("true,false"),"ide_adfac");
		set_solicitud.getTab_seleccion().getColumna("num_factura_adfac").setNombreVisual("Nro. FACTURA");
		set_solicitud.getTab_seleccion().getColumna("detalle_adsoc").setNombreVisual("DETALLE COMPRA");
		set_solicitud.getTab_seleccion().getColumna("nro_solicitud_adsoc").setNombreVisual("Nro. SOLICITUD COMPRA");
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setNombreVisual("PROVEEDOR");
		set_solicitud.getTab_seleccion().getColumna("ruc_tepro").setNombreVisual("RUC PROVEEDOR");
		set_solicitud.getTab_seleccion().getColumna("valor_adsoc").setNombreVisual("VALOR COMPRA");

		set_solicitud.getTab_seleccion().getColumna("num_factura_adfac").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("detalle_adsoc").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("nro_solicitud_adsoc").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("valor_adsoc").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("num_factura_adfac").setLongitud(2);

		set_solicitud.getBot_aceptar().setMetodo("aceptarSolicitud");
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.setRadio();
		agregarComponente(set_solicitud);
		
		dia_recibir_solicitud.setId("dia_recibir_solicitud");
		dia_recibir_solicitud.setTitle("RECIBIR SOLICITUD");
		dia_recibir_solicitud.setWidth("70%");
		dia_recibir_solicitud.setHeight("50%");
		Grid gri_cuerpo=new Grid();
		
		tab_ingreso_material.setId("tab_ingreso_material");
		tab_ingreso_material.setTabla("bodt_bodega", "ide_bobod",2);
		tab_ingreso_material.setTipoFormulario(true);
		tab_ingreso_material.setCondicion("ide_bobod=-1");//para que aparesca vacia
		tab_ingreso_material.getGrid().setColumns(4);
		tab_ingreso_material.getColumna("ide_bomat").setVisible(false);
		tab_ingreso_material.getColumna("ide_tepro").setVisible(false);
		tab_ingreso_material.getColumna("ide_coest").setVisible(false);
		tab_ingreso_material.getColumna("fecha_compra_bobod").setVisible(false);
		tab_ingreso_material.getColumna("cantidad_ingreso_bobod").setVisible(false);
		tab_ingreso_material.getColumna("recibido_bobod").setVisible(false);
		tab_ingreso_material.getColumna("modelo_bobod").setVisible(false);
		tab_ingreso_material.getColumna("marca_bobod").setVisible(false);
		tab_ingreso_material.getColumna("num_factura_bobod").setVisible(false);
		tab_ingreso_material.getColumna("serie_bobod").setVisible(false);
		tab_ingreso_material.getColumna("valor_unitario_bobod").setVisible(false);
		tab_ingreso_material.getColumna("color_bobod").setVisible(false);
		tab_ingreso_material.getColumna("saldo_bobod").setVisible(false);
		tab_ingreso_material.getColumna("ide_adsoc").setVisible(false);
		tab_ingreso_material.getColumna("activo_bobod").setVisible(false);
		tab_ingreso_material.getColumna("valor_total_bobod").setVisible(false);
		tab_ingreso_material.getColumna("ide_comov").setVisible(false);
		tab_ingreso_material.getColumna("ide_boinv").setVisible(false);
		tab_ingreso_material.getColumna("ide_bobod").setVisible(false);
		tab_ingreso_material.getColumna("numero_ingreso_bobod").setValorDefecto(ser_contabilidad.numeroSecuencial(par_secuencial_modulo));

		tab_ingreso_material.getColumna("ide_geani").setNombreVisual("AÑO");
		tab_ingreso_material.getColumna("num_doc_bobod").setNombreVisual("NUMERO DE DOCUMENTO");
		tab_ingreso_material.getColumna("descripcion_bobod").setNombreVisual("DESCRIPCION");
		tab_ingreso_material.getColumna("tipo_ingreso_bobod").setNombreVisual("TIPO DE INGRESO");
		tab_ingreso_material.getColumna("fecha_ingreso_bobod").setNombreVisual("FECHA DE INGRESO");
		tab_ingreso_material.getColumna("numero_ingreso_bobod").setNombreVisual("NUMERO DE INGRESO");
		tab_ingreso_material.getColumna("existencia_anterior_bobod").setVisible(false);
		tab_ingreso_material.getColumna("ide_geani").setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		tab_ingreso_material.getColumna("ide_geani").setAutoCompletar();
		List lista = new ArrayList();
	       Object fila1[] = {
	           "1", "CONSUMO EXTERNO"
	       };
	       Object fila2[] = {
	           "0", "CONSUMO INTERNO"
	       };
	       Object fila3[] = {
		       "2", "ACTIVOS FIJOS"
		   };
	       lista.add(fila1);
	       lista.add(fila2);
	       lista.add(fila3);
	       
	       tab_ingreso_material.getColumna("tipo_ingreso_bobod").setRadio(lista, "0");
	       tab_ingreso_material.getColumna("tipo_ingreso_bobod").setRadioVertical(true);
		tab_ingreso_material.dibujar();
		gri_cuerpo.getChildren().add(tab_ingreso_material);
		dia_recibir_solicitud.getBot_aceptar().setMetodo("aceptarDialogoSolicitud");
		dia_recibir_solicitud.setDialogo(gri_cuerpo);
		agregarComponente(dia_recibir_solicitud);
		
		
	}
	
	public void importarSolicitud(){
		set_solicitud.getTab_seleccion().setSql(ser_bodega.getSolicitudFactura("true"));
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.dibujar();

	}

	public  void aceptarSolicitud(){
		
		tab_solicitud.setSql("select b.ide_addef,detalle_bomat,codigo_bomat,num_factura_adfac,valor_adsoc,nro_solicitud_adsoc,valor_total_addef,valor_unitario_addef,cantidad_addef," +
				" detalle_adsoc from adq_solicitud_compra a,adq_detalle_factura b,adq_factura c , bodt_material d" +
				" where a.ide_adsoc=c.ide_adsoc and b.ide_adfac=c.ide_adfac and recibido_addef=true  and d.ide_bomat=b.ide_bomat and c.ide_adfac="+set_solicitud.getValorSeleccionado().toString()+" order by codigo_bomat");
		tab_solicitud.ejecutarSql();
		utilitario.addUpdate("tab_solicitud");
		set_solicitud.cerrar();
	}

	public void seleccionarTodas() {
		tab_solicitud.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_solicitud.getTotalFilas()];
        for (int i = 0; i < tab_solicitud.getFilas().size(); i++) {
            seleccionados[i] = tab_solicitud.getFilas().get(i);
        }
        tab_solicitud.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_solicitud.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_solicitud.getSeleccionados().length == tab_solicitud.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_solicitud.getTotalFilas() - tab_solicitud.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_solicitud.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_solicitud.getSeleccionados().length; j++) {
                    if (tab_solicitud.getSeleccionados()[j].equals(tab_solicitud.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_solicitud.getFilas().get(i);
                    cont++;
                }
            }
            tab_solicitud.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_solicitud.setSeleccionados(null);
    }
public void abrirRecibirSolicitud(){

	if(tab_solicitud.getTotalFilas()==0){
		utilitario.agregarMensaje("No existen Registros", "Envie a buscar una solicitud de compra");
		return;
	}
	if (tab_solicitud.getListaFilasSeleccionadas().size()==0){
		utilitario.agregarMensajeInfo("Seleccione un registro", "Debe seleccionar al menos un registro");
		return;
	}
	//Hace aparecer el componente
	tab_ingreso_material.limpiar();
	tab_ingreso_material.insertar();
	TablaGenerica anio_actual= utilitario.consultar(ser_contabilidad.getAnio("true","false"));
	tab_ingreso_material.setValor("numero_ingreso_bobod", ser_contabilidad.numeroSecuencial(par_secuencial_modulo));
	tab_ingreso_material.setValor("fecha_ingreso_bobod", utilitario.getFechaActual());
	tab_ingreso_material.setValor("ide_geani",anio_actual.getValor("ide_geani"));

	utilitario.addUpdateTabla(tab_ingreso_material,"numero_ingreso_bobod,ide_geani,fecha_ingreso_bobod","");

	//tab_direccion.limpiar();
	//	tab_direccion.insertar();
	dia_recibir_solicitud.dibujar();

}
long ide_inicial=0;

public void  aceptarDialogoSolicitud(){
	
	String str_seleccionados=tab_solicitud.getFilasSeleccionadas();
	System.out.println(" probando el str_Selccionado "+str_seleccionados);
	if(str_seleccionados!=null || !str_seleccionados.isEmpty()){
		
	TablaGenerica tab_consulta_solicitud= ser_bodega.getTablaGenericaSolicitudCompra(str_seleccionados);
	
	if (tab_ingreso_material.getValor("tipo_ingreso_bobod").equals("0")){
	   TablaGenerica validaInventario=ser_bodega.getTablaGenericaValidaInventario(str_seleccionados, tab_ingreso_material.getValor("ide_geani"));
	   String mensaje="";
	   if(validaInventario.getTotalFilas()>0){
		   for(int i=0;i<validaInventario.getTotalFilas();i++){
			   TablaGenerica material=ser_bodega.getTablaGenericaMaterial(validaInventario.getValor(i, "ide_bomat"));
			  mensaje+=material.getValor("codigo_bomat")+" "+material.getValor("detalle_bomat")+" "; 
		   }
		   dia_recibir_solicitud.cerrar();
		   utilitario.agregarNotificacionInfo("Materiales No Registrados en Inventarios", "El Siguiente Listado de Materiales no se encuentrados Registrados en Inventarios "+mensaje);
		   return;
	   }
	   
	}
	
	
	
	utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'bodt_bodega'");
	TablaGenerica valor=utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("bodt_bodega", "ide_bobod"));
	ide_inicial=Long.parseLong(valor.getValor("codigo"));
	
	System.out.println(" probando el inicial "+ide_inicial);

	for(int i=0;i<tab_consulta_solicitud.getTotalFilas();i++){
		
		utilitario.getConexion().ejecutarSql("update adq_detalle_factura set recibido_addef= false where ide_addef="+tab_consulta_solicitud.getValor(i,"ide_addef"));
		
		String consulta_inserta_bodega="INSERT INTO bodt_bodega(ide_bobod, ide_geani, ide_bomat, ide_tepro,marca_bobod,modelo_bobod,serie_bobod,color_bobod, fecha_ingreso_bobod, fecha_compra_bobod, recibido_bobod, " +
				" cantidad_ingreso_bobod, numero_ingreso_bobod, num_factura_bobod, num_doc_bobod, descripcion_bobod, tipo_ingreso_bobod, " +
				" valor_unitario_bobod, valor_total_bobod,activo_bobod, ide_adsoc,existencia_anterior_bobod,ide_boinv) VALUES("+ide_inicial+", "+tab_ingreso_material.getValor("ide_geani")+","+tab_consulta_solicitud.getValor(i,"ide_bomat")+"," 
				+ tab_consulta_solicitud.getValor(i,"ide_tepro")+",'"+tab_consulta_solicitud.getValor(i,"marca_addef")+"','"+tab_consulta_solicitud.getValor(i,"modelo_addef")+"','"+tab_consulta_solicitud.getValor(i,"serie_addef")+"','"+tab_consulta_solicitud.getValor(i,"color_addef")+"','"+tab_ingreso_material.getValor("fecha_ingreso_bobod")+"','"+tab_consulta_solicitud.getValor(i,"fecha_factura_adfac")+"',true,"
				+ tab_consulta_solicitud.getValor(i,"cantidad_addef")+",'"+tab_ingreso_material.getValor("numero_ingreso_bobod")+"','"+tab_consulta_solicitud.getValor(i,"num_factura_adfac")+
				"','"+tab_ingreso_material.getValor("num_doc_bobod")+"','"+tab_ingreso_material.getValor("descripcion_bobod")+"','"+tab_ingreso_material.getValor("tipo_ingreso_bobod")+
				"',"+tab_consulta_solicitud.getValor(i,"valor_unitario_addef")+","+tab_consulta_solicitud.getValor(i,"valor_total_addef")+",true,"+tab_consulta_solicitud.getValor(i,"ide_adsoc");
	
		if (tab_ingreso_material.getValor("tipo_ingreso_bobod").equals("0")){
			TablaGenerica inventario=utilitario.consultar(ser_bodega.getDatosInventario(tab_consulta_solicitud.getValor(i,"ide_bomat"), tab_ingreso_material.getValor("ide_geani")));
			consulta_inserta_bodega +=","+inventario.getValor("existencia_actual") +","+inventario.getValor("ide_boinv")+");";
			boolean resultado;
			resultado =ser_bodega.registraInventarioIngresos(tab_consulta_solicitud.getValor(i,"ide_bomat"), tab_ingreso_material.getValor("ide_geani"), tab_consulta_solicitud.getValor(i,"cantidad_addef"), tab_consulta_solicitud.getValor(i,"valor_total_addef"));
		    	
		}
		else {
			consulta_inserta_bodega +=",null,null); ";
		}
		utilitario.getConexion().ejecutarSql(consulta_inserta_bodega);

		ide_inicial++;	
	}
	utilitario.getConexion().ejecutarSql("update adq_factura set activo_adfac= false where ide_adfac in ( select ide_adfac from adq_factura where not ide_adfac in ("
			+" select ide_adfac from adq_detalle_factura where recibido_addef=true ) and ide_adfac="+tab_consulta_solicitud.getValor("ide_adfac")+"  )");
	//validar mediante tabla generica si existe alguna factura activa para no cambiar el estado de la solicitud de compra
	TablaGenerica estado_factura = utilitario.consultar(ser_bodega.getEstadoFactura(tab_consulta_solicitud.getValor("ide_adsoc"), "true"));
	if(estado_factura.getTotalFilas()>0){
		
	}
	else {
	utilitario.getConexion().ejecutarSql("update adq_solicitud_compra set activo_adsoc=false where ide_adsoc in ( select ide_adsoc from adq_factura where not ide_adfac in ("
    +" select ide_adfac from adq_detalle_factura where recibido_addef=true ) and ide_adsoc= "+tab_consulta_solicitud.getValor("ide_adsoc")+")");
	}
	
	String mensja=ser_contabilidad.guardaSecuencial(tab_ingreso_material.getValor("numero_ingreso_bobod"), par_secuencial_modulo);
	dia_recibir_solicitud.cerrar();
	utilitario.agregarMensaje("RECIBIDO", "Se recibio los productos correctamente");
	tab_solicitud.limpiar();
	}
	else {
		utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		return;
		
	}
		
	}

	@Override
	public void insertar() {
		if (aut_ing_material.getValor()!=null){
			if(tab_ingreso_material.isFocus()){
				tab_ingreso_material.getColumna("ide_bobod").setValorDefecto(aut_ing_material.getValor());
				tab_ingreso_material.insertar();
			}

		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar los datos de material","");
		}

	}

	@Override
	public void guardar() {
		if(tab_ingreso_material.guardar()){
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();

	}
	public Tabla getTab_ingreso_material() {
		return tab_ingreso_material;
	}
	public void setTab_ingreso_material(Tabla tab_ingreso_material) {
		this.tab_ingreso_material = tab_ingreso_material;
	}
	public AutoCompletar getAut_ing_material() {
		return aut_ing_material;
	}
	public void setAut_ing_material(AutoCompletar aut_ing_material) {
		this.aut_ing_material = aut_ing_material;
	}
	public SeleccionTabla getSet_solicitud() {
		return set_solicitud;
	}
	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_solicitud = set_solicitud;
	}
	public Tabla getTab_solicitud() {
		return tab_solicitud;
	}
	public void setTab_solicitud(Tabla tab_solicitud) {
		this.tab_solicitud = tab_solicitud;
	}

}
