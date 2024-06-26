package paq_presupuesto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_libera_compromiso extends Pantalla{
	private Tabla tab_libera_compromiso=new Tabla();
	private Tabla tab_detalle_libera=new Tabla();
	private SeleccionTabla set_compromiso=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Radio rad_imprimir= new Radio();
	public static String par_modulosec_compromiso;
	private Confirmar con_guardar=new Confirmar();
	
	///reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte self_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
		      
	public pre_libera_compromiso(){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		par_modulosec_compromiso=utilitario.getVariable("p_modulo_secuencialliberacionComp");
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
        //boton buscar compromiso
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Compromiso");
		bot_buscar.setMetodo("importarCompromiso");
		bot_buscar.setStyle("display: inline-block;");
		bar_botones.agregarBoton(bot_buscar);
		
		List listax = new ArrayList();
        Object fila1x[] = {
           "0", "PARCIAL"
        };
        Object fila2x[] = {
           "1", "TOTAL"
        };
       
        listax.add(fila1x);
        listax.add(fila2x);
        rad_imprimir.setId("rad_imprimir");
        rad_imprimir.setRadio(listax);
        rad_imprimir.setValue(fila2x);
        rad_imprimir.setStyle("display: inline-block; margin: 0 0 -8px 0;");
        bar_botones.agregarComponente(rad_imprimir);
		bar_botones.agregarComponente(bot_buscar);   
		
		con_guardar.setId("con_guardarcon_guardar");
		con_guardar.setMessage("¿Esta Seguro de Generar la Liberación? Recuerde que no podra ser editada posteriormente");
		con_guardar.setTitle("Confirmación de Generación");
		con_guardar.getBot_aceptar().setMetodo("generarLiqu");
		agregarComponente(con_guardar);
		
		tab_libera_compromiso.setId("tab_libera_compromiso");
		tab_libera_compromiso.setHeader("LIBERACIÓN COMPROMISO");
		tab_libera_compromiso.setTabla("pre_libera_compromiso", "ide_prlic", 1);
		tab_libera_compromiso.getColumna("IDE_PRTRA").setCombo(ser_presupuesto.getTramite("true,false"));
		tab_libera_compromiso.getColumna("IDE_PRTRA").setAutoCompletar();
		tab_libera_compromiso.getColumna("IDE_PRTRA").setLectura(true);//ide del compromiso
		tab_libera_compromiso.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true"));
		tab_libera_compromiso.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_libera_compromiso.getColumna("IDE_GEEDP").setLectura(true);
		tab_libera_compromiso.getColumna("fecha_prlic").setValorDefecto(utilitario.getFechaActual());
		tab_libera_compromiso.getColumna("activo_prlic").setValorDefecto("true");
		tab_libera_compromiso.getColumna("total_parcial_prlic").setRadio(listax, "1");
		tab_libera_compromiso.getColumna("total_parcial_prlic").setRadioVertical(true);
		tab_libera_compromiso.getColumna("total_parcial_prlic").setLectura(true);
		tab_libera_compromiso.getColumna("sec_liquidacion_prlce").setLectura(true);
		tab_libera_compromiso.getColumna("valor_total_prlic").setValorDefecto("0.00");
		tab_libera_compromiso.getColumna("valor_total_prlic").setEtiqueta();
		tab_libera_compromiso.getColumna("valor_total_prlic").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_libera_compromiso.setCondicion("ide_geani=-1");
		tab_libera_compromiso.setTipoFormulario(true);
		tab_libera_compromiso.getGrid().setColumns(6);
		tab_libera_compromiso.agregarRelacion(tab_detalle_libera);
		tab_libera_compromiso.dibujar();
		PanelTabla pat_libera=new PanelTabla();
		pat_libera.setPanelTabla(tab_libera_compromiso);
		
		//detalle libera compromiso
		tab_detalle_libera.setId("tab_detalle_libera");
		tab_detalle_libera.setHeader("DETALLE LIBERACIÓN DEL COMPROMISO");
		tab_detalle_libera.setTabla("pre_detalle_libera_compro", "ide_prdlc", 2);
		tab_detalle_libera.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_detalle_libera.getColumna("ide_prpoa").setAutoCompletar();
		tab_detalle_libera.getColumna("ide_prpoa").setLectura(true);
		tab_detalle_libera.getColumna("ide_prpoa").setLongitud(90);
		tab_detalle_libera.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_detalle_libera.getColumna("ide_prfuf").setAutoCompletar();
		tab_detalle_libera.getColumna("ide_prfuf").setLectura(true);
		tab_detalle_libera.getColumna("saldo_liquidado_prdlc").setLectura(true);
		tab_detalle_libera.getColumna("activo_prdlc").setValorDefecto("true");
		tab_detalle_libera.getColumna("activo_prdlc").setLectura(true);
		tab_detalle_libera.getColumna("valor_comprometido_prdlc").setLectura(true);
		tab_detalle_libera.setColumnaSuma("valor_comprometido_prdlc,valor_liberado_prdlc");  
		tab_detalle_libera.dibujar();
		
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle_libera);
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_libera, pat_detalle, "40%", "H");
		agregarComponente(div_divi);
		
		set_compromiso.setId("set_compromiso");
		set_compromiso.setSeleccionTabla(ser_presupuesto.getTramite("true,false"),"ide_prtra");
		set_compromiso.setTitle("Seleccione Compromiso");
		set_compromiso.getTab_seleccion().getColumna("nro_compromiso").setFiltro(true);
		set_compromiso.setRadio();
		set_compromiso.getBot_aceptar().setMetodo("aceptarCompromiso");
		agregarComponente(set_compromiso);

	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_libera_compromiso.setCondicion("ide_geani="+com_anio.getValue());
			tab_libera_compromiso.ejecutarSql();
			tab_detalle_libera.ejecutarValorForanea(tab_libera_compromiso.getValorSeleccionado());
		}
		else{
			tab_libera_compromiso.setCondicion("ide_geani=-1");
			tab_libera_compromiso.ejecutarSql();
		}
	}
	
	//importar compromiso
	public void importarCompromiso(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		set_compromiso.getTab_seleccion().ejecutarSql();
		set_compromiso.dibujar();
	}

	public  void aceptarCompromiso(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		String str_seleccionados = set_compromiso.getValorSeleccionado();

		if (str_seleccionados!=null){
			tab_libera_compromiso.insertar();
			tab_libera_compromiso.setValor("ide_prtra", str_seleccionados);
			//tab_libera_compromiso.setValor("IDE_GEEDP", set_compromiso.get);
			tab_libera_compromiso.setValor("ide_geani", com_anio.getValue()+"");
			tab_libera_compromiso.setValor("total_parcial_prlic", rad_imprimir.getValue().toString());
			tab_libera_compromiso.setValor("sec_liquidacion_prlce", ser_contabilidad.numeroSecuencial(par_modulosec_compromiso));
			// Primera validacion cuando es una liquidacion total del compromiso
			if(rad_imprimir.getValue().equals("1")){
				TablaGenerica tab_valida_presupuesto = utilitario.consultar("select * from tes_comprobante_pago where ide_prtra="+str_seleccionados);
				if(tab_valida_presupuesto.getTotalFilas()>0){
					utilitario.agregarMensajeError("No se puede realizar una Liberación Total", "El compromiso seleccionado esta devengandose");
					tab_libera_compromiso.limpiar();
					return;
				}
				else{
					
					TablaGenerica tab_consulta_poacer = utilitario.consultar("select ide_prpot,ide_prpoa,ide_prfuf,comprometido_prpot,comprometido_prpot* (-1) as total_liquidado  from pre_poa_tramite  where ide_prtra="+str_seleccionados);
					if(tab_consulta_poacer.getTotalFilas()>0){
						for(int i=0;i<tab_consulta_poacer.getTotalFilas();i++){
							tab_detalle_libera.insertar();
							tab_detalle_libera.setValor("valor_comprometido_prdlc", tab_consulta_poacer.getValor(i, "comprometido_prpot"));;
							tab_detalle_libera.setValor("valor_liberado_prdlc", tab_consulta_poacer.getValor(i, "total_liquidado"));;
							tab_detalle_libera.setValor("saldo_liquidado_prdlc", "0");
							tab_detalle_libera.setValor("activo_prdlc", "true");
							tab_detalle_libera.setValor("ide_prpoa", tab_consulta_poacer.getValor(i, "ide_prpoa"));;
							tab_detalle_libera.setValor("ide_prfuf", tab_consulta_poacer.getValor(i, "ide_prfuf"));;
						}
						tab_libera_compromiso.setValor("valor_total_prlic", utilitario.getFormatoNumero(tab_detalle_libera.getSumaColumna("valor_liberado_prdlc"),2)+"");
					}
				}
			}
			// Segunda validacion cuando es una liquidacion parcial de la certificacion
			else if(rad_imprimir.getValue().equals("0")){
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
			}

			set_compromiso.cerrar();
			utilitario.addUpdate("tab_libera_compromiso");
		
	}
	
	//reporte
	public void abrirListaReportes() {
	    // TODO Auto-generated method stub
		rep_reporte.dibujar();
		//System.out.println("abrirListaReportes "+rep_reporte.getPath());
	}

	public void aceptarReporte(){
		//System.out.println("aceptarReporte");
		if(rep_reporte.getReporteSelecionado().equals("Liquidacion Compromiso"));{
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo","REPORTE DE LIBERACION  DEL COMPROMISO");
				p_parametros.put("ide_prtra", pckUtilidades.CConversion.CInt(tab_libera_compromiso.getValor("ide_prtra")));
				p_parametros.put("ide_prlic", pckUtilidades.CConversion.CInt(tab_libera_compromiso.getValor("ide_prlic")));
				p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));
				p_parametros.put("pie_especialista_pres",  utilitario.getVariable("p_pie_especialista_pres"));
				p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt("1"));

				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();
			
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

			}
		}
			
	}

	public void generarLiqu(){
		
		double valorLiq=0;
		
		if(tab_libera_compromiso.getValor("total_parcial_prlic").equals("1")){
			double saldo=0;
			try{
				saldo=tab_detalle_libera.getSumaColumna("saldo_liquidado_prdlc");
				valorLiq=tab_detalle_libera.getSumaColumna("valor_liberado_prdlc");
			}
			catch (Exception e){
				utilitario.agregarMensaje("No existe valor", "El valor del saldo debe ser valido");
			}
			if (saldo !=0){
				utilitario.agregarMensajeError("No se puede guardar", "Para una Liquidacion Total del Compromiso, los Saldos deben ser Cero");
			}
		}
		else
			valorLiq= pckUtilidades.CConversion.CDbl_2(tab_detalle_libera.getSumaColumna("valor_liberado_prdlc"));
		
		String sql;

		// TODO Auto-generated method stub
		if(tab_libera_compromiso.isFilaInsertada()){
			System.out.println("Guardando la liquidacion y afectando al compromiso y al poa");
			tab_libera_compromiso.setValor("valor_total_prlic", valorLiq+""); 
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
		
		con_guardar.cerrar();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	
		if(tab_libera_compromiso.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe buscar un Compromiso");
		}
		else if(tab_detalle_libera.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe ingresar un Compromiso");
		}
	}

	@Override
	public void guardar() {

		if(tab_libera_compromiso.isFilaInsertada()){
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}
		else{
			utilitario.agregarMensajeInfo("Registro no editable", "");
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_libera_compromiso.isFocus()){
			utilitario.agregarMensajeInfo("No puede Eliminar una liquidación.", "");
			return;
		}
		utilitario.getTablaisFocus().eliminar();
		//ecevallos
		generarLiqu();
		guardar();
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

	public SeleccionTabla getSet_compromiso() {
		return set_compromiso;
	}

	public void setSet_compromiso(SeleccionTabla set_compromiso) {
		this.set_compromiso = set_compromiso;
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
