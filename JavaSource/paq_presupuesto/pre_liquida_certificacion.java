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
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_liquida_certificacion extends Pantalla {
	
	private Tabla tab_liquida_certificacion=new Tabla();
	private  Tabla tab_detalle=new Tabla();
	private SeleccionTabla set_certificacion=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Radio rad_imprimir= new Radio();
	public static String par_modulosec_certificacion;
	private Confirmar con_guardar=new Confirmar();

	///reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte self_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_liquida_certificacion (){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		par_modulosec_certificacion=utilitario.getVariable("p_modulo_secuencialliquidacion");
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);

		/////boton buscar poa
		Boton bot_certificacion=new Boton();
		bot_certificacion.setIcon("ui-icon-person");
		bot_certificacion.setValue("Buscar Certificación");
		bot_certificacion.setMetodo("importarCertificacion");
		bot_certificacion.setStyle("display: inline-block;");
	
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
		bar_botones.agregarComponente(bot_certificacion);
		
		con_guardar.setId("con_guardarcon_guardar");
		con_guardar.setMessage("¿Esta Seguro de Generar la Liquidación? Recuerde que no podra ser editada posteriormente");
		con_guardar.setTitle("Confirmación de Generación");
		con_guardar.getBot_aceptar().setMetodo("generarLiqu");
		agregarComponente(con_guardar);
		
		tab_liquida_certificacion.setId("tab_liquida_certificacion");
		tab_liquida_certificacion.setHeader("LIQUIDACION CERTIFICACION");
		tab_liquida_certificacion.setTabla("pre_liquida_certificacion", "ide_prlce", 1);
		tab_liquida_certificacion.getColumna("activo_prlce").setValorDefecto("true");
		tab_liquida_certificacion.getColumna("ide_prcer").setCombo(ser_presupuesto.getCertificacion("true,false"));
		//tab_liquida_certificacion.getColumna("ide_prcer").setCombo("pre_certificacion", "ide_prcer", "cast(nro_certificacion_prcer as character varying(10))", "");
		tab_liquida_certificacion.getColumna("ide_prcer").setAutoCompletar();
		tab_liquida_certificacion.getColumna("ide_prcer").setLectura(true);//ide de la certificacion
        tab_liquida_certificacion.getColumna("total_parcial_prlce").setRadio(listax, "1");
        tab_liquida_certificacion.getColumna("total_parcial_prlce").setRadioVertical(true);
        tab_liquida_certificacion.getColumna("total_parcial_prlce").setLectura(true);
        tab_liquida_certificacion.getColumna("fecha_prlce").setValorDefecto(utilitario.getFechaActual());
        tab_liquida_certificacion.getColumna("sec_liquidacion_prlce").setLectura(true);
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setValorDefecto("0.00");
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setEtiqueta();
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
	    tab_liquida_certificacion.setCondicion("ide_geani=-1"); 
	    tab_liquida_certificacion.setTipoFormulario(true);
	    tab_liquida_certificacion.getGrid().setColumns(6);
		tab_liquida_certificacion.agregarRelacion(tab_detalle);
		tab_liquida_certificacion.dibujar();
		PanelTabla pat_liquida =new PanelTabla();
		pat_liquida.setPanelTabla(tab_liquida_certificacion);
		
		///// detalle liquida certificacion
		tab_detalle.setId("tab_detalle");
		tab_detalle.setHeader("DETALLE LIQUIDACION CERTIFICACION");
		tab_detalle.setTabla("pre_detalle_liquida_certif", "ide_prdcl", 2);
		tab_detalle.getColumna("activo_prdcl").setValorDefecto("true");
		tab_detalle.getColumna("activo_prdcl").setLectura(true);
		tab_detalle.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_detalle.getColumna("ide_prpoa").setAutoCompletar();
		tab_detalle.getColumna("ide_prpoa").setLectura(true);
		tab_detalle.getColumna("ide_prpoa").setLongitud(90);
		tab_detalle.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_detalle.getColumna("ide_prfuf").setAutoCompletar();
		tab_detalle.getColumna("saldo_liquidado_prdcl").setLectura(true);
		tab_detalle.getColumna("ide_prfuf").setLectura(true);
		tab_detalle.getColumna("valor_certificado_prdcl").setLectura(true);
		tab_detalle.setColumnaSuma("valor_certificado_prdcl,valor_liquidado_prdcl");  
		tab_detalle.dibujar();
		
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle);

		Division div_divi=new Division();
		div_divi.dividir2(pat_liquida, pat_detalle, "40%", "H");
		
		agregarComponente(div_divi);

		set_certificacion.setId("set_certificacion");
		set_certificacion.setSeleccionTabla(ser_presupuesto.getCertificacion("true,false"),"ide_prcer");
		set_certificacion.setTitle("Seleccione Certificación");
		set_certificacion.getTab_seleccion().getColumna("nro_certificacion_prcer").setFiltro(true);

		set_certificacion.setRadio();
		set_certificacion.getBot_aceptar().setMetodo("aceptarCertificacion");
		agregarComponente(set_certificacion);
		
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_liquida_certificacion.setCondicion("ide_geani="+com_anio.getValue());
			tab_liquida_certificacion.ejecutarSql();
			tab_detalle.ejecutarValorForanea(tab_liquida_certificacion.getValorSeleccionado());
		}
		else{
			tab_liquida_certificacion.setCondicion("ide_geani=-1");
			tab_liquida_certificacion.ejecutarSql();
		}
	}
	
	
	/////certificacion
	public void importarCertificacion(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		//set_certificacion.getTab_seleccion().setSql(ser_presupuesto.getCertificacion("true,false"));
		set_certificacion.getTab_seleccion().ejecutarSql();
		set_certificacion.dibujar();

	}
	
	public  void aceptarCertificacion(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		String str_seleccionado = set_certificacion.getValorSeleccionado();
		
		if (str_seleccionado!=null){

			tab_liquida_certificacion.insertar();
			tab_liquida_certificacion.setValor("ide_prcer", str_seleccionado);
			tab_liquida_certificacion.setValor("ide_geani", com_anio.getValue()+"");
			tab_liquida_certificacion.setValor("total_parcial_prlce", rad_imprimir.getValue().toString());
			tab_liquida_certificacion.setValor("sec_liquidacion_prlce", ser_contabilidad.numeroSecuencial(par_modulosec_certificacion));
			// Primera validacion cuando es una liquidacion total de la certificacion
			if(rad_imprimir.getValue().equals("1")){
			    TablaGenerica tab_valida_presupuesto = utilitario.consultar("select * from pre_tramite where ide_prcer="+str_seleccionado);
				if(tab_valida_presupuesto.getTotalFilas()>0){
					utilitario.agregarMensajeError("No se puede realizar una Liquidación Total", "Existen ya ejecutados compromisos con la certificacion seleccionada");
					tab_liquida_certificacion.limpiar();
					return;

				}
				else{
					
					TablaGenerica tab_consulta_poacer = utilitario.consultar("select ide_prpoc,ide_prpoa,ide_prfuf,valor_certificado_prpoc,valor_certificado_prpoc* (-1) as total_liquidado from pre_poa_certificacion  where ide_prcer="+str_seleccionado);
					if(tab_consulta_poacer.getTotalFilas()>0){
						for(int i=0;i<tab_consulta_poacer.getTotalFilas();i++){
							tab_detalle.insertar();
							tab_detalle.setValor("valor_certificado_prdcl", tab_consulta_poacer.getValor(i, "valor_certificado_prpoc"));;
							tab_detalle.setValor("valor_liquidado_prdcl", tab_consulta_poacer.getValor(i, "total_liquidado"));;
							tab_detalle.setValor("saldo_liquidado_prdcl", "0");
							tab_detalle.setValor("activo_prdcl", "true");
							tab_detalle.setValor("ide_prpoa", tab_consulta_poacer.getValor(i, "ide_prpoa"));;
							tab_detalle.setValor("ide_prfuf", tab_consulta_poacer.getValor(i, "ide_prfuf"));;
						}
						tab_liquida_certificacion.setValor("valor_total_prlce", utilitario.getFormatoNumero(tab_detalle.getSumaColumna("valor_liquidado_prdcl"),2)+"");
					}
				}
			}
			// Segunda validacion cuando es una liquidacion parcial de la certificacion
			else if(rad_imprimir.getValue().equals("0")){
				
				TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaCert(str_seleccionado);		
				for(int i=0;i<tab_poa.getTotalFilas();i++){
					tab_detalle.insertar();
					tab_detalle.setValor("ide_prpoa", tab_poa.getValor(i, "ide_prpoa"));
					tab_detalle.setValor("ide_prfuf", tab_poa.getValor(i, "ide_prfuf"));
					tab_detalle.setValor("valor_certificado_prdcl", tab_poa.getValor(i, "valor_certificado_prpoc"));
					TablaGenerica tab_consulta_negativo=utilitario.consultar("select 1 as codigo,(-1)*"+tab_poa.getValor(i, "saldo_comprometer")+" as valor_liquidar");
					
					tab_detalle.setValor("valor_liquidado_prdcl",tab_consulta_negativo.getValor("valor_liquidar"));
					tab_detalle.setValor("saldo_liquidado_prdcl", "0");
					tab_detalle.setValor("activo_prdcl", "true");

				}
				tab_liquida_certificacion.setValor("valor_total_prlce", tab_detalle.getSumaColumna("valor_liquidado_prdcl")+"");

			  }
			
			}
			set_certificacion.cerrar();
			utilitario.addUpdate("tab_liquida_certificacion");
		}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		//System.out.println("aceptarReporte");
		if(rep_reporte.getReporteSelecionado().equals("Liquidacion Certificación"));{
			TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo","LIQUIDACIÓN DE LA CERTIFICACIÓN");
				//p_parametros.put("ide_prcer", pckUtilidades.CConversion.CInt(tab_certificacion.getValor("ide_prcer")));
				p_parametros.put("ide_prlce", pckUtilidades.CConversion.CInt(tab_liquida_certificacion.getValor("ide_prlce")));
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
		
		if(tab_liquida_certificacion.getValor("total_parcial_prlce").equals("1")){
			double saldo=0;
			try{
				saldo = tab_detalle.getSumaColumna("saldo_liquidado_prdcl");
				valorLiq = tab_detalle.getSumaColumna("valor_liquidado_prdcl");
			}
			catch (Exception e){
				utilitario.agregarMensaje("No existe valor", "El valor del saldo deb ser valido");
			}
			if (saldo !=0){
				utilitario.agregarMensajeError("No se puede guardar", "Para una Liquidacion Total de la Certificación los Saldos deben ser Cero");
			}
		}
		else
			valorLiq= pckUtilidades.CConversion.CDbl_2(tab_detalle.getSumaColumna("valor_liquidado_prdcl"));
		
		String sql;
		
		if(tab_liquida_certificacion.isFilaInsertada())
		{
			System.out.println("Guardando la liquidacion y afectando a la certificacion y poa");
			tab_liquida_certificacion.setValor("valor_total_prlce", valorLiq+"");
			if(tab_liquida_certificacion.guardar()){
	
				if(tab_detalle.guardar()){
					guardarPantalla();
					System.out.println("guardarPantalla");
					//AWBECERRA	
					for(int i=0;i<tab_detalle.getTotalFilas();i++){
						//Actualiza detalles de la certificacion
						sql="update pre_poa_certificacion set valor_certificado_prpoc=coalesce(valor_certificado_prpoc,0)+"+tab_detalle.getValor(i, "valor_liquidado_prdcl")+" ,saldo_certificacion_prpoc=0 where ide_prcer="+tab_liquida_certificacion.getValor("ide_prcer") + " and ide_prpoa="+ tab_detalle.getValor(i, "ide_prpoa")+ " and ide_prfuf="+ tab_detalle.getValor(i, "ide_prfuf");
						utilitario.getConexion().ejecutarSql(sql);
	
						//Actualiza valore de certificacion en el POA-SubActividad
						ser_presupuesto.trigEjecutaCertificacion(tab_detalle.getValor(i, "ide_prpoa"),tab_detalle.getValor(i, "ide_prfuf"));
						ser_presupuesto.trigActualizaCertificadoPoa(tab_detalle.getValor(i, "ide_prpoa"));
					}
				}
				//Actualiza total en la certificacion
				sql="update pre_certificacion set valor_certificacion_prcer=coalesce(valor_certificacion_prcer,0)+"
						+tab_liquida_certificacion.getValor("valor_total_prlce")+", valor_liberado_prcer=(-1)*"+tab_liquida_certificacion.getValor("valor_total_prlce")
						+", valor_disponible_prcer=0 where ide_prcer="+tab_liquida_certificacion.getValor("ide_prcer");
				utilitario.getConexion().ejecutarSql(sql);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_certificacion), par_modulosec_certificacion);
				
			}
		}
		con_guardar.cerrar();
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_liquida_certificacion.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe buscar una Certificación");
		}
		else if(tab_detalle.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe ingresar una Certificación");
		}
	}

	@Override
	public void guardar() {
		
		// TODO Auto-generated method stub
		if(tab_liquida_certificacion.isFilaInsertada()){
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
		if(tab_liquida_certificacion.isFocus()){
			utilitario.agregarMensajeInfo("No puede Eliminar una liquidación.", "");
			return;
		}
		
		if(tab_detalle.isFocus()){
			utilitario.agregarMensajeInfo("No se puede Eliminar los detalles de la liquidación.", "");
			return;
		}
		
		utilitario.getTablaisFocus().eliminar();
		//ecevallos
		generarLiqu();
		guardar();
	}

	public Tabla getTab_liquida_certificacion() {
		return tab_liquida_certificacion;
	}


	public void setTab_liquida_certificacion(Tabla tab_liquida_certificacion) {
		this.tab_liquida_certificacion = tab_liquida_certificacion;
	}


	public Tabla getTab_detalle() {
		return tab_detalle;
	}


	public void setTab_detalle(Tabla tab_detalle) {
		this.tab_detalle = tab_detalle;
	}

	public SeleccionTabla getSet_certificacion() {
		return set_certificacion;
	}

	public void setSet_certificacion(SeleccionTabla set_certificacion) {
		this.set_certificacion = set_certificacion;
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
