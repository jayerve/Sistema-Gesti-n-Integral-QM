package paq_tesoreria;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_anticipos.ejb.ServicioAnticipo;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;



public class pre_convenio_pago extends Pantalla{
	
	private Tabla tab_convenio = new Tabla();
	private Tabla tab_detalle_convenio=new Tabla();
	private Tabla tab_convenio_amortizacion=new Tabla();
	private SeleccionTabla sel_clientes = new SeleccionTabla();
	private SeleccionTabla sel_comp_vencidos = new SeleccionTabla();
	private SeleccionTabla sel_titulos = new SeleccionTabla();

	private Combo com_anio=new Combo();
    public static String par_modulosec_convenios;
    private String ide_empleado="";
	private String str_ruc_cliente_seleccionado="";
	private String str_comprobantes_seleccionado="";
	private String str_titulo_seleccionado="";
	
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina ) utilitario.instanciarEJB(ServicioNomina.class);	
	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);
	

	public pre_convenio_pago(){
		
		par_modulosec_convenios=utilitario.getVariable("p_modulo_secuencial_convenios");
		ide_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		if(ide_empleado==null ||ide_empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro");
			return;
		}

		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_agregar = new Boton();
		bot_agregar.setValue("Agregar Cliente");
		bot_agregar.setMetodo("agregar_cliente");
		bar_botones.agregarBoton(bot_agregar);
		
		Boton bot_agregar_titulo = new Boton();
		bot_agregar_titulo.setValue("Agregar Titulo");
		bot_agregar_titulo.setMetodo("agregar_titulo");
		bar_botones.agregarBoton(bot_agregar_titulo);
		
		Boton bot_generar_amort = new Boton();
		bot_generar_amort.setValue("Generar Tabla Amortización");
		bot_generar_amort.setMetodo("generar_amort");
		bar_botones.agregarBoton(bot_generar_amort);
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_convenio.setId("tab_convenio");
		tab_convenio.setHeader("CONVENIO DE PAGO");
		tab_convenio.setTabla("fac_convenio", "ide_facon", 1);
		tab_convenio.setCondicion("titulo_credito_facon=false and ide_geani=-1"); 
		tab_convenio.getColumna("titulo_credito_facon").setValorDefecto("false");
		tab_convenio.getColumna("activo_facon").setValorDefecto("true");
		tab_convenio.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_convenio.getColumna("fecha_facon").setValorDefecto(utilitario.getFechaActual());
		tab_convenio.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_convenio.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_convenio.getColumna("ide_gtemp").setLectura(true);
		tab_convenio.getColumna("ide_gtemp").setAutoCompletar();
		tab_convenio.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_convenio.getColumna("ide_recli").setAutoCompletar();
		tab_convenio.getColumna("ide_recli").setLectura(true);
		tab_convenio.getColumna("titulo_credito_facon").setVisible(false);
		tab_convenio.getColumna("fecha_titulo_facon").setVisible(false);
		tab_convenio.getColumna("monto_convenio_facon").setValorDefecto("0.00");
		tab_convenio.getColumna("monto_convenio_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_convenio.getColumna("monto_convenio_facon").setEtiqueta();
		tab_convenio.getColumna("nro_titulo_facon").setVisible(false);
		tab_convenio.getColumna("nro_convenio_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_convenio.getColumna("nro_convenio_facon").setEtiqueta();
		tab_convenio.getColumna("numero_meses_facon").setValorDefecto("0.00");
		tab_convenio.getColumna("valor_cuotas_facon").setValorDefecto("0.00");
		tab_convenio.getColumna("valor_cuotas_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_convenio.getColumna("valor_cuotas_facon").setEtiqueta();
		tab_convenio.getColumna("monto_entrada_facon").setValorDefecto("0.00");
		tab_convenio.getColumna("interes_facon").setValorDefecto(utilitario.getVariable("p_valor_interes_mora_nd"));
		tab_convenio.getColumna("interes_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_convenio.getColumna("interes_facon").setEtiqueta();
		tab_convenio.getColumna("saldo_facon").setValorDefecto("0.00");
		tab_convenio.getColumna("saldo_facon").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_convenio.getColumna("saldo_facon").setEtiqueta();
		tab_convenio.setTipoFormulario(true);
		tab_convenio.getGrid().setColumns(4);
		tab_convenio.agregarRelacion(tab_detalle_convenio);
		tab_convenio.agregarRelacion(tab_convenio_amortizacion);
		tab_convenio.dibujar();
		PanelTabla pat_convenio=new PanelTabla();
		pat_convenio.setPanelTabla(tab_convenio);
		
		
		tab_detalle_convenio.setId("tab_detalle_convenio");
		tab_detalle_convenio.setIdCompleto("tab_tabulador:tab_detalle_convenio");
		tab_detalle_convenio.setHeader("DETALLE CONVENIO");
		tab_detalle_convenio.setTabla("fac_detalle_convenio", "ide_facof", 2);
		tab_detalle_convenio.setCampoForanea("ide_facon");
		tab_detalle_convenio.getColumna("activo_facof").setValorDefecto("true");
		tab_detalle_convenio.getColumna("ide_fafac").setCombo("fac_factura","ide_fafac","secuencial_fafac","ide_coest in (2,24)");
		tab_detalle_convenio.getColumna("ide_fafac").setLectura(true);
		tab_detalle_convenio.getColumna("ide_fafac").setAutoCompletar();
		tab_detalle_convenio.getColumna("ide_fanod").setCombo("fac_nota_debito","ide_fanod","'Nota Debito '||coalesce(nro_nota_debito_elect_fanod,'')","ide_coest in (2)");
		tab_detalle_convenio.getColumna("ide_fanod").setLectura(true);
		tab_detalle_convenio.getColumna("ide_fanod").setAutoCompletar();
		tab_detalle_convenio.setColumnaSuma("valor_facof"); 
		tab_detalle_convenio.dibujar();
		PanelTabla pat_det_convenio=new PanelTabla();
		pat_det_convenio.setPanelTabla(tab_detalle_convenio);
		
		tab_convenio_amortizacion.setId("tab_convenio_amortizacion");
		tab_convenio_amortizacion.setIdCompleto("tab_tabulador:tab_convenio_amortizacion");
		tab_convenio_amortizacion.setHeader("TABLA AMORTIZACION");
		tab_convenio_amortizacion.setTabla("fac_convenio_amortizacion", "ide_facoa", 3);
		tab_convenio_amortizacion.setCampoForanea("ide_facon");
		tab_convenio_amortizacion.getColumna("activo_facoa").setValorDefecto("true");
		tab_convenio_amortizacion.getColumna("valor_mora_facoa").setValorDefecto("0.00");
		tab_convenio_amortizacion.getColumna("interes_mora_facoa").setValorDefecto("0.00");
		tab_convenio_amortizacion.getColumna("valor_cuota_facoa").setValorDefecto("0.00");
		tab_convenio_amortizacion.getColumna("interes_facoa").setValorDefecto("0.00");
		tab_convenio_amortizacion.getColumna("saldo_facoa").setValorDefecto("0.00");
		tab_convenio_amortizacion.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_convenio_amortizacion.getColumna("ide_tecaj").setVisible(false);
		tab_convenio_amortizacion.getColumna("ide_sucu").setVisible(false);
		tab_convenio_amortizacion.getColumna("ide_retip").setVisible(false);
		tab_convenio_amortizacion.getColumna("ide_gtemp").setVisible(false);
		tab_convenio_amortizacion.setColumnaSuma("valor_cuota_facoa,valor_mora_facoa,interes_mora_facoa,interes_facoa,saldo_facoa"); 
		tab_convenio_amortizacion.dibujar();
		PanelTabla pat_convenio_amort=new PanelTabla();
		pat_convenio_amort.setPanelTabla(tab_convenio_amortizacion);
		
		tab_tabulador.agregarTab("DETALLE CONVENIO PAGO", pat_det_convenio);
		tab_tabulador.agregarTab("DETALLE TABLA AMORTIZACION", pat_convenio_amort);
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_convenio, tab_tabulador, "50%", "H");
		agregarComponente(div_divi);
		
		inicializaClientes();
		inicializaDeudas();
		inicializaTitulos();
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_convenio.setCondicion("titulo_credito_facon=false and ide_geani="+com_anio.getValue());
			tab_convenio.ejecutarSql();
			tab_detalle_convenio.ejecutarValorForanea(tab_convenio.getValorSeleccionado());
			tab_convenio_amortizacion.ejecutarValorForanea(tab_convenio.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void inicializaClientes(){
		sel_clientes.setId("sel_clientes");
		sel_clientes.setTitle("LISTA DE CLIENTES");
		sel_clientes.setSeleccionTabla(ser_facturacion.getDatosBasicosClientesLite("0,1"), "ide_recli");
		sel_clientes.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_clientes.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(30);
		sel_clientes.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		sel_clientes.getTab_seleccion().getColumna("razon_social_recli").setLongitud(100);
		//.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		//sel_clientes.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(100);
		sel_clientes.setRadio();
		sel_clientes.getTab_seleccion().ejecutarSql();
		sel_clientes.getBot_aceptar().setMetodo("agregar_cliente");
		agregarComponente(sel_clientes);
	}
	
	public void inicializaDeudas(){
		sel_comp_vencidos.setId("sel_comp_vencidos");
		sel_comp_vencidos.setTitle("COMPROBANTES IMPAGOS");
		sel_comp_vencidos.setSeleccionTabla(ser_tesoreria.getSqlDeudaClientesAbonos("-1"), "codigo");
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_fafac").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_recli").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ide_fanod").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("ruc_comercial_recli").setVisible(false);
		sel_comp_vencidos.getTab_seleccion().getColumna("detalle_bogrm").setLongitud(50);
		sel_comp_vencidos.getTab_seleccion().getColumna("secuencial_fafac").setLongitud(40);
		//sel_comp_vencidos.seleccionarTodas();
		sel_comp_vencidos.getTab_seleccion().ejecutarSql();
		sel_comp_vencidos.getBot_aceptar().setMetodo("agregar_cliente");
		agregarComponente(sel_comp_vencidos);
	}
	
	public void inicializaTitulos(){
		sel_titulos.setId("sel_titulos");
		sel_titulos.setTitle("TITULOS DE CREDITO");
		sel_titulos.setSeleccionTabla(ser_tesoreria.getSqlTitulosCredito(com_anio.getValue()+""), "ide_facon");
		sel_titulos.getTab_seleccion().getColumna("nro_titulo_facon").setFiltro(true);
		sel_titulos.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		sel_titulos.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(30);
		sel_titulos.getTab_seleccion().getColumna("razon_social_recli").setFiltro(true);
		sel_titulos.getTab_seleccion().getColumna("razon_social_recli").setLongitud(100);
		sel_titulos.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		sel_titulos.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(100);
		sel_titulos.setRadio();
		sel_titulos.getTab_seleccion().ejecutarSql();
		sel_titulos.getBot_aceptar().setMetodo("agregar_titulo");
		agregarComponente(sel_titulos);
	}
	
	public void agregar_cliente(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(sel_clientes.isVisible()){
			if(sel_clientes.getValorSeleccionado()!=null){
				str_ruc_cliente_seleccionado=sel_clientes.getValorSeleccionado();	
				sel_clientes.cerrar();	

				sel_comp_vencidos.setSql(ser_tesoreria.getSqlDeudaClientesAbonos(str_ruc_cliente_seleccionado));
				sel_comp_vencidos.getTab_seleccion().ejecutarSql();
				sel_comp_vencidos.dibujar();
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else if(sel_comp_vencidos.isVisible()){
			//Pregunto si existen registros seleccionados para contabilizar.
			if(sel_comp_vencidos.getSeleccionados() !=null){
				str_comprobantes_seleccionado=sel_comp_vencidos.getSeleccionados();
				sel_comp_vencidos.cerrar();
				//todo aqui
				
				TablaGenerica tab_cliente_seleccionado= utilitario.consultar("select ide_recli as codigo, ide_recli "
						+ " from rec_clientes where ruc_comercial_recli like '"+str_ruc_cliente_seleccionado+"' and coalesce(nro_establecimiento_recli,0)=1 and factura_datos_recli=1 and activo_recli=true ");
				
				if(tab_cliente_seleccionado.getTotalFilas()<=0)
				{
					utilitario.agregarMensajeInfo("El cliente seleccionado no posee una matriz activa", "");
					return;
				}
				
				tab_convenio.insertar();
				tab_convenio.setValor("nro_convenio_facon", ser_contabilidad.numeroSecuencial(par_modulosec_convenios));
				tab_convenio.setValor("ide_geani", com_anio.getValue()+"");
				tab_convenio.setValor("ide_recli", tab_cliente_seleccionado.getValor("ide_recli"));
				tab_convenio.setValor("ide_gtemp", ide_empleado);
				tab_convenio.setValor("ide_coest", "2");
				
				String[] str_comp=str_comprobantes_seleccionado.split(",");
				
				for(int i=0;i<sel_comp_vencidos.getTab_seleccion().getTotalFilas();i++){
					
					for(int j=0;j<str_comp.length;j++)
					{
						if(str_comp[j].equals(sel_comp_vencidos.getTab_seleccion().getValor(i,"codigo")))
						{
							tab_detalle_convenio.insertar();
							tab_detalle_convenio.setValor("ide_fafac", sel_comp_vencidos.getTab_seleccion().getValor(i, "ide_fafac")+"");
							tab_detalle_convenio.setValor("valor_facof", sel_comp_vencidos.getTab_seleccion().getValor(i, "valor")+"");
							
							if(pckUtilidades.CConversion.CInt(sel_comp_vencidos.getTab_seleccion().getValor(i,"ide_fanod")) > 0){
								tab_detalle_convenio.insertar();
								tab_detalle_convenio.setValor("ide_fanod", sel_comp_vencidos.getTab_seleccion().getValor(i, "ide_fanod")+"");
								tab_detalle_convenio.setValor("valor_facof", sel_comp_vencidos.getTab_seleccion().getValor(i, "interes")+"");
							 }
						}
					}
				}
				tab_convenio.setValor("monto_convenio_facon",tab_detalle_convenio.getSumaColumna("valor_facof")+"");
			}
			else{
				utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
			}
			
		}
		else
		{
			sel_clientes.dibujar();
		}
	}
	
	public void agregar_titulo(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if(sel_titulos.isVisible()){
			if(sel_titulos.getValorSeleccionado()!=null){
				str_titulo_seleccionado=sel_titulos.getValorSeleccionado();	
				sel_titulos.cerrar();	
				
				String sql="update fac_convenio set nro_convenio_facon ="+ ser_contabilidad.numeroSecuencial(par_modulosec_convenios);
				sql+=" ,titulo_credito_facon=false, fecha_facon=now() where ide_facon= "+str_titulo_seleccionado;
				
				System.out.println(sql);
				utilitario.getConexion().ejecutarSql(sql);

				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_convenios), par_modulosec_convenios);

				tab_convenio.ejecutarSql();
				tab_detalle_convenio.ejecutarValorForanea(tab_convenio.getValorSeleccionado());
				tab_convenio_amortizacion.ejecutarValorForanea(tab_convenio.getValorSeleccionado());
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else
		{
			sel_titulos.setSql(ser_tesoreria.getSqlTitulosCredito(com_anio.getValue()+""));
			sel_titulos.getTab_seleccion().ejecutarSql();
			sel_titulos.dibujar();
		}
		
	}
	
	public void generar_amort(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(tab_convenio_amortizacion.getTotalFilas() > 0)
		{
			utilitario.agregarMensajeInfo("El convenio ya cuenta con una tabla de amortizacion", "");
			return;
		}
		
		int numeroMeses=pckUtilidades.CConversion.CInt(tab_convenio.getValor("numero_meses_facon"));
		
		if(numeroMeses == 0)
		{
			utilitario.agregarMensajeInfo("Debe ingresar el numero de meses", "");
			return;
		}
		
		double valorEntrada=pckUtilidades.CConversion.CDbl_2(tab_convenio.getValor("monto_entrada_facon"));
		
		if( valorEntrada == 0)
		{
			utilitario.agregarMensajeInfo("Debe ingresar el monto de entrada", "");
			return;
		}


		double valorDetalle=pckUtilidades.CConversion.CDbl_2(tab_detalle_convenio.getSumaColumna("valor_facof"));
		
		if(((valorDetalle*30)/100) >= valorEntrada)
		{
			utilitario.agregarMensajeInfo("El monto de entrada debe ser al menos el 30% del valor del convenio", "");
			return;
		}
		
		Date fecha=new Date();
        double capital = valorDetalle;
        double cuota=0; 
        double interesAplicado=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_valor_interes_mora_nd"));
        double interesMensual=0;
        double saldo_capital=capital;
        
        int amortCada=30; //dias
        int mesesGracia=0;

        cuota=pckUtilidades.CConversion.CDbl_2(ser_anticipo.getCuota(capital, interesAplicado, numeroMeses, amortCada, mesesGracia));
        
        tab_convenio.modificar(tab_convenio.getFilaActual());
        
        tab_convenio.setValor("interes_facon", interesAplicado+"");
        tab_convenio.setValor("valor_cuotas_facon", cuota+"");
        tab_convenio.setValor("saldo_facon", saldo_capital+"");
        tab_convenio.setValor("fecha_aprobacion_facon", utilitario.getFechaActual());
        
		for(int i=1;i<=numeroMeses;i++){
			tab_convenio_amortizacion.insertar();
			tab_convenio_amortizacion.setValor("nro_cuota_facoa", i+"");
			tab_convenio_amortizacion.setValor("ide_coest", "2");
			
			fecha=utilitario.sumarDiasFecha(fecha,amortCada);
			interesMensual=pckUtilidades.CConversion.CDbl_2((saldo_capital*(interesAplicado/(double)100)) / ((double)360/amortCada));
            

			tab_convenio_amortizacion.setValor("fecha_pago_facoa", utilitario.DeDateAString(fecha));
			tab_convenio_amortizacion.setValor("interes_facoa", interesMensual+"");
			tab_convenio_amortizacion.setValor("valor_cuota_facoa", cuota+"");
			tab_convenio_amortizacion.setValor("saldo_facoa", saldo_capital+"");
		
			System.out.println("Numero: "+i+" capital: "+capital+" saldo_capital:"+saldo_capital+" interes: "+interesMensual+" cuota: "+cuota);
			
			capital=pckUtilidades.CConversion.CDbl_2(cuota-interesMensual);
			saldo_capital=pckUtilidades.CConversion.CDbl_2(saldo_capital-capital);
            
		}
		
		if (tab_convenio.guardar())
			if (tab_convenio_amortizacion.guardar())
				guardarPantalla();			 
			  
		utilitario.addUpdate("tab_tabulador:tab_detalle_convenio,tab_tabulador:tab_convenio_amortizacion,tab_convenio");
	}
	
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Certificación Presupuestaria"));{
			//TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
				p_parametros.put("ide_prcer", pckUtilidades.CConversion.CInt(tab_convenio.getValor("ide_facon")));
				p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));
				p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt("1"));
	
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
		
		if(tab_convenio.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_convenios), par_modulosec_convenios);
		}
		
		if (tab_convenio.guardar())
		  if( tab_detalle_convenio.guardar())
		  {
			 guardarPantalla();			 
		  }
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_convenio() {
		return tab_convenio;
	}

	public void setTab_convenio(Tabla tab_convenio) {
		this.tab_convenio = tab_convenio;
	}

	public Tabla getTab_detalle_convenio() {
		return tab_detalle_convenio;
	}

	public void setTab_detalle_convenio(Tabla tab_detalle_convenio) {
		this.tab_detalle_convenio = tab_detalle_convenio;
	}

	public Tabla getTab_convenio_amortizacion() {
		return tab_convenio_amortizacion;
	}

	public void setTab_convenio_amortizacion(Tabla tab_convenio_amortizacion) {
		this.tab_convenio_amortizacion = tab_convenio_amortizacion;
	}
	

	public SeleccionTabla getSel_clientes() {
		return sel_clientes;
	}

	public void setSel_clientes(SeleccionTabla sel_clientes) {
		this.sel_clientes = sel_clientes;
	}

	public SeleccionTabla getSel_comp_vencidos() {
		return sel_comp_vencidos;
	}

	public void setSel_comp_vencidos(SeleccionTabla sel_comp_vencidos) {
		this.sel_comp_vencidos = sel_comp_vencidos;
	}


	public SeleccionTabla getSel_titulos() {
		return sel_titulos;
	}

	public void setSel_titulos(SeleccionTabla sel_titulos) {
		this.sel_titulos = sel_titulos;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
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

	public Map getMap_parametros() {
		return map_parametros;
	}

	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}



}
