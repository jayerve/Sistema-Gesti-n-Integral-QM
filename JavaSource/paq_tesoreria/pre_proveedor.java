package paq_tesoreria;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidad.EMGIRS_SRI_RUC;
import pckServicio.ServicioDINARDAP;



public class pre_proveedor extends Pantalla {
	private Tabla tab_proveedor= new Tabla();
	private Tabla tab_direccion= new Tabla();
	private Tabla tab_telefono= new Tabla();
	private Tabla tab_correo= new Tabla();
	private Tabla tab_cuenta_bancaria= new Tabla();

	private static String empleado_responsable;


	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_proveedor() {
		// TODO Auto-generated constructor stub
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_erp_tesoreria")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor de la IP: "+utilitario.getVariable("p_ip_servidor_erp_tesoreria"));
		}
		
		empleado_responsable=ser_contabilidad.empleadoResponsable(utilitario.getVariable("p_modulo_tesoreria_emp"),ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp"));
		System.out.println("empleado_responsable"+empleado_responsable);		
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_proveedor.setId("tab_proveedor");
		tab_proveedor.setTabla("tes_proveedor", "ide_tepro", 1);
		tab_proveedor.setTipoFormulario(true);
		tab_proveedor.getGrid().setColumns(4);
		//tab_proveedor.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_proveedor.getColumna("ide_coest").setVisible(false);
		tab_proveedor.getColumna("ide_retic").setCombo("rec_tipo_contribuyente","ide_retic","detalle_retic","");
		tab_proveedor.getColumna("ide_tetpp").setCombo("tes_tipo_proveedor","ide_tetpp","detalle_tetpp","");
		//tab_proveedor.getColumna("nombre_tepro").setLectura(true);;
		tab_proveedor.getColumna("activo_tepro").setValorDefecto("true");
		tab_proveedor.getColumna("ruc_tepro").setMetodoChange("consultaDinardap");
		tab_proveedor.agregarRelacion(tab_direccion);//agraga relacion para los tabuladores
		tab_proveedor.agregarRelacion(tab_telefono);
		tab_proveedor.agregarRelacion(tab_correo);
		tab_proveedor.agregarRelacion(tab_cuenta_bancaria);
		tab_proveedor.dibujar();
		PanelTabla pat_proveedor= new PanelTabla();
		pat_proveedor.setPanelTabla(tab_proveedor);
		
///direccion 
		tab_direccion.setId("tab_direccion");
		tab_direccion.setHeader("DIRECCIÒN");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("tes_direccion","ide_tedir",2);
		tab_direccion.getColumna("activo_tedir").setValorDefecto("true");
		tab_direccion.setCampoForanea("ide_tepro");
		tab_direccion.dibujar();
		PanelTabla pat_direccion = new PanelTabla();
		pat_direccion.setPanelTabla(tab_direccion);
// telefono
		tab_telefono.setId("tab_telefono");
		tab_telefono.setHeader("TELEFONO");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("tes_telefono","ide_tetel",3);
		tab_telefono.setCampoForanea("ide_tepro");
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora","ide_reteo","detalle_reteo","");
		tab_telefono.getColumna("activo_tetel").setValorDefecto("true");
		tab_telefono.dibujar();
		PanelTabla pat_telefono = new PanelTabla();
		pat_telefono.setPanelTabla(tab_telefono);
//correo
		tab_correo.setId("tab_correo");
		tab_correo.setHeader("CORREO");
		tab_correo.setIdCompleto("tab_tabulador:tab_correo");
		tab_correo.setTabla("tes_correo","ide_tecor",4);
		tab_correo.setCampoForanea("ide_tepro");
		tab_correo.getColumna("activo_tecor").setValorDefecto("true");
		tab_correo.dibujar();
		PanelTabla pat_correo = new PanelTabla();
		pat_correo.setPanelTabla(tab_correo);
// cuenta bancaria	
		tab_cuenta_bancaria.setId("tab_cuenta_bancaria");
		tab_cuenta_bancaria.setHeader("CUENTA BANCARIA");
		tab_cuenta_bancaria.setIdCompleto("tab_tabulador:tab_cuenta_bancaria");
		tab_cuenta_bancaria.setTabla("tes_proveedor_cuenta_bancaria","ide_tepcb",5);
		tab_cuenta_bancaria.setCampoForanea("ide_tepro");
		tab_cuenta_bancaria.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL and IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		
		tab_cuenta_bancaria.getColumna("IDE_GTTCB").setCombo("GTH_TIPO_CUENTA_BANCARIA", "IDE_GTTCB", "DETALLE_GTTCB", "");
		tab_cuenta_bancaria.getColumna("activo_tepcb").setValorDefecto("true");
		if(empleado_responsable==null ||empleado_responsable.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario responsable para el registro de cuentas bancarias");
			tab_cuenta_bancaria.setLectura(true);
		}
		tab_cuenta_bancaria.dibujar();
		PanelTabla pat_cuenta = new PanelTabla();
		pat_cuenta.setPanelTabla(tab_cuenta_bancaria);
		

		tab_tabulador.agregarTab("DIRECCION", pat_direccion);//intancia los tabuladores 
		tab_tabulador.agregarTab("TELEFONO",pat_telefono);
		tab_tabulador.agregarTab("CORREO", pat_correo);
		tab_tabulador.agregarTab("CUENTA BANCARIA", pat_cuenta);

		//division2

		Division div_division=new Division();
		div_division.dividir2(pat_proveedor,tab_tabulador,"50%","H");
		agregarComponente(div_division);

		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);

	}

	public void consultaDinardap()
	{

		String ruc=pckUtilidades.CConversion.CStr(tab_proveedor.getValor("ruc_tepro"));
		
		TablaGenerica proveedores=ser_bodega.getTablaProveedorPorRuc(ruc);
		
		if(proveedores.getTotalFilas()>0)
		{
			//utilitario.agregarMensajeInfo("RUC: "+ruc, "YA REGISTRADO...");
			utilitario.agregarNotificacionInfo("RUC: "+ruc, "ESTE PROVEEDOR YA SE ENCUENTRA REGISTRADO...");
			//return;
		}
		
		if(ruc.length()==13){		
			EMGIRS_SRI_RUC objPrv = ServicioDINARDAP.consultarRUC(ruc);	
			if(objPrv.isExisteRUC())
			{
				tab_proveedor.getColumna("nombre_tepro").setLectura(true);
				if(!objPrv.getDesEstado().toUpperCase().contains("ACTIVO"))
				{
					utilitario.agregarMensajeInfo("RUC: "+ruc, objPrv.getDesEstado());
					tab_proveedor.setValor("activo_tepro", "false");	
				}
				else
				{
					tab_proveedor.setValor("activo_tepro", "true");					
					tab_proveedor.setValor("nombre_tepro", objPrv.getRazonSocial());		
					tab_proveedor.modificar(tab_proveedor.getFilaActual());//para que haga el update	

					if(tab_proveedor.isFilaInsertada()){
						tab_direccion.insertar();
						tab_direccion.setValor("detalle_tedir", objPrv.getDireccionCorta());
						tab_direccion.setValor("notificacion_tedir", "true");
						
						if(objPrv.getEmail().length()>0)
						{
							tab_correo.insertar();
							tab_correo.setValor("detalle_tecor", objPrv.getEmail());
							tab_correo.setValor("notificacion_tecor", "true");
						}
					}
					
					utilitario.addUpdate("tab_proveedor,tab_direccion,tab_correo");
				}
			}
			else
				utilitario.agregarMensajeInfo("RUC: "+ruc, "NO EXISTE");
		}
		else
			if(ruc.length()==10){
				tab_proveedor.getColumna("nombre_tepro").setLectura(false);
				//tab_proveedor.dibujar();
			}
		
		utilitario.addUpdate("tab_proveedor");
	}

	public void exportarExcel()
	{
		String sql="SELECT pv.ide_tepro,detalle_retic as tipo_constitucion, detalle_gttdi as tipo_identificacion,ruc_tepro,  nombre_tepro,"
			        +" detalle_tetpp as tipo_proveedor, ruc_representante_tepro, nom_representante_tepro,detalle_tedir as direccion,numero_telefono_tele,detalle_tecor as correos"
			        +" , detalle_gttcb as tipo_cuenta_bancaria, detalle_geins as institucion_financiera,numero_cuenta_tepcb"
					+"   FROM tes_proveedor pv"
					+" left  join rec_tipo_contribuyente tc on tc.ide_retic=pv.ide_retic"
					+" left join tes_tipo_proveedor tp on tp.ide_tetpp=pv.ide_tetpp"
					+" left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=pv.ide_gttdi"
					+" left join tes_direccion td on td.ide_tepro=pv.ide_tepro and td.activo_tedir=true"
					+" left join (select ide_tepro, string_agg(numero_telefono_tele, ' / ') as numero_telefono_tele from tes_telefono where activo_tetel=true group by ide_tepro) tt on tt.ide_tepro=pv.ide_tepro"
					+" left join (select ide_tepro, string_agg(detalle_tecor, ', ') as detalle_tecor from tes_correo where activo_tecor=true group by ide_tepro) tcc on tcc.ide_tepro=pv.ide_tepro"
					+" left join tes_proveedor_cuenta_bancaria tpcb on tpcb.ide_tepro=pv.ide_tepro and activo_tepcb=true and acreditacion_tepcb=true"
					+" left join gth_tipo_cuenta_bancaria tcb on tcb.ide_gttcb=tpcb.ide_gttcb"
					+" left join gen_institucion gi on gi.ide_geins=tpcb.ide_geins"
					+" where activo_tepro=true"
					+" order by 4";
		
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql(sql);
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_proveedor.isFocus()) {
			tab_proveedor.insertar();
		}
		else if (tab_direccion.isFocus()) {
			tab_direccion.insertar();

		}
		else if (tab_telefono.isFocus()) {
			tab_telefono.insertar();

		}

		else if (tab_correo.isFocus()){
			tab_correo.insertar();

		}
		else if (tab_cuenta_bancaria.isFocus()){
			tab_cuenta_bancaria.insertar();
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_correo.getTotalFilas()<1){
			utilitario.agregarMensajeInfo("No se puede guardar", "EL proveedor no cuenta con un correo electrónico.");
			return;
		}
		
		if(!tab_correo.getValor("activo_tecor").equals("true")){
			utilitario.agregarMensajeInfo("No se puede guardar", "El proveedor no cuenta con un correo activo.");
			return;
		}
		
		if(!tab_correo.getValor("notificacion_tecor").equals("true")){
			utilitario.agregarMensajeInfo("No se puede guardar", "El proveedor no cuenta con un correo activo para notificación.");
			return;
		}
		
		if(!pckUtilidades.Utilitario.validarEmailFuerte(tab_correo.getValor("detalle_tecor")))
        {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe de ingresar un correo eléctronico valido.");
			return;
        }
		
		///////
		
		if(tab_cuenta_bancaria.getTotalFilas()<1){
			utilitario.agregarMensajeInfo("No se puede guardar", "EL proveedor no cuenta con una cuenta bancaria.");
			return;
		}
		
		if(!tab_cuenta_bancaria.getValor("activo_tepcb").equals("true")){
			utilitario.agregarMensajeInfo("No se puede guardar", "El proveedor no cuenta con una cuenta bancaria activa.");
			return;
		}
		
		if(!tab_cuenta_bancaria.getValor("acreditacion_tepcb").equals("true")){
			utilitario.agregarMensajeInfo("No se puede guardar", "El proveedor no cuenta con una cuenta bancaria para acreditación.");
			return;
		}
	
	
		if (tab_proveedor.guardar()) {
			if (tab_direccion.guardar()) {
				if( tab_telefono.guardar()){
					if(tab_correo.guardar()){
						if(tab_cuenta_bancaria.guardar()){
							
						}

					}
				}
			}
		}
guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_proveedor.eliminar();
	}



	public Tabla getTab_proveedor() {
		return tab_proveedor;
	}



	public void setTab_proveedor(Tabla tab_proveedor) {
		this.tab_proveedor = tab_proveedor;
	}



	public Tabla getTab_direccion() {
		return tab_direccion;
	}



	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}



	public Tabla getTab_telefono() {
		return tab_telefono;
	}



	public void setTab_telefono(Tabla tab_telefono) {
		this.tab_telefono = tab_telefono;
	}



	public Tabla getTab_correo() {
		return tab_correo;
	}



	public void setTab_correo(Tabla tab_correo) {
		this.tab_correo = tab_correo;
	}



	public Tabla getTab_cuenta_bancaria() {
		return tab_cuenta_bancaria;
	}



	public void setTab_cuenta_bancaria(Tabla tab_cuenta_bancaria) {
		this.tab_cuenta_bancaria = tab_cuenta_bancaria;
	}


}
