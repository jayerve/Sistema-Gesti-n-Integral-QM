package paq_nomina;

import javax.ejb.EJB;

import org.primefaces.component.blockui.BlockUI;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.Parametro;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 * @author 
 *
 */
public class pre_generar_rol extends Pantalla{

	private Combo com_periodo=new Combo();
	private Combo com_periodo_fondos=new Combo();

	private Tabla tab_detalle_tipo_nomina=new Tabla();
	private Confirmar con_guardar=new Confirmar();
	private SeleccionTabla set_pago_decimos=new SeleccionTabla();
	private SeleccionTabla set_pago_fondos=new SeleccionTabla();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);


	public pre_generar_rol() {

		if (!validarParametrosSistemaImportados()){
			return;
		}
		com_periodo.setId("com_periodo");
		com_periodo.setCombo(ser_nomina.getSqlComboPeriodoRol());

		com_periodo.setMetodo("seleccionaPeriodo");
		com_periodo.setStyle("width: 350px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Periodo Rol:"));
		bar_botones.agregarComponente(com_periodo);

		Boton bot_calcular_rubros=new Boton();
		bar_botones.agregarBoton(bot_calcular_rubros);
		bot_calcular_rubros.setIcon("ui-icon-calculator");
		bot_calcular_rubros.setTitle("Generar Rol");
		bot_calcular_rubros.setValue("Generar Rol");
		bot_calcular_rubros.setMetodo("calcularRubros");
		bot_calcular_rubros.setOnstart("blo_deta_rubro.show()");
		bot_calcular_rubros.setOncomplete("blo_deta_rubro.hide()");

		Boton bot_calcular_renta=new Boton();
		bar_botones.agregarBoton(bot_calcular_renta);
		bot_calcular_renta.setIcon("ui-icon-calculator");
		bot_calcular_renta.setTitle("Calcular Renta");
		bot_calcular_renta.setValue("Calcular Renta");
		bot_calcular_renta.setMetodo("calcularRenta");
		bot_calcular_renta.setOnstart("blo_deta_rubro.show()");
		bot_calcular_renta.setOncomplete("blo_deta_rubro.hide()");

		Boton bot_pagar_decimos=new Boton();
		bar_botones.agregarBoton(bot_pagar_decimos);
		bot_pagar_decimos.setIcon("ui-icon-calculator");
		bot_pagar_decimos.setTitle("Pagar Decimos");
		bot_pagar_decimos.setValue("Pagar Decimos");
		bot_pagar_decimos.setMetodo("abrirDialogoPagoDecimos");
		bot_pagar_decimos.setOnstart("blo_deta_rubro.show()");
		bot_pagar_decimos.setOncomplete("blo_deta_rubro.hide()");


		Boton bot_pagar_fondos_reserva=new Boton();
	//	bar_botones.agregarBoton(bot_pagar_fondos_reserva);
		bot_pagar_fondos_reserva.setIcon("ui-icon-calculator");
		bot_pagar_fondos_reserva.setTitle("Generar Fondos de Reserva");
		bot_pagar_fondos_reserva.setValue("Generar Fondos de Reserva");
		bot_pagar_fondos_reserva.setMetodo("abrirDialogoFondosReserva");
		bot_pagar_fondos_reserva.setOnstart("blo_deta_rubro.show()");
		bot_pagar_fondos_reserva.setOncomplete("blo_deta_rubro.hide()");


		Boton bot_reimportar=new Boton();
		bar_botones.agregarBoton(bot_reimportar);
		bot_reimportar.setIcon("ui-icon-arrowreturnthick-1-n");
		bot_reimportar.setTitle("Re-Calcular Rubros");
		bot_reimportar.setValue("Re-Calcular Rubros");
		bot_reimportar.setMetodo("recalcularRubros");
		bot_reimportar.setOnstart("blo_deta_rubro.show()");
		bot_reimportar.setOncomplete("blo_deta_rubro.hide()");


		Boton bot_elimar_rol=new Boton();
		bar_botones.agregarBoton(bot_elimar_rol);
		bot_elimar_rol.setIcon("ui-icon-trash");
		bot_elimar_rol.setTitle("Eliminar Nomina");
		bot_elimar_rol.setValue("Eliminar Nomina");
		//		bot_elimar_rol.setMetodo("confirmarEliminarNominaGenerada");
		bot_elimar_rol.setMetodo("eliminar");
		bot_elimar_rol.setOnstart("blo_deta_rubro.show()");
		bot_elimar_rol.setOncomplete("blo_deta_rubro.hide()");


		tab_detalle_tipo_nomina.setId("tab_detalle_tipo_nomina");
		tab_detalle_tipo_nomina.setSql("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN," +
				"TEM.DETALLE_GTTEM " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"LEFT JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_NRTIT=DTN.IDE_NRTIT "+
				"WHERE ACTIVO_NRDTN=true "+
				"and TIN.IDE_NRTIN = "+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" " +
				"AND PRO.IDE_GEPRO IN (-1) ");
		tab_detalle_tipo_nomina.setCampoPrimaria("ide_nrdtn");
		tab_detalle_tipo_nomina.setTipoSeleccion(true);
		tab_detalle_tipo_nomina.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setMensajeWarn("DETALLE TIPO DE NOMINA");
		pat_panel1.setPanelTabla(tab_detalle_tipo_nomina);


		Division div=new Division();
		div.dividir1(pat_panel1);

		agregarComponente(div);


		//Bloque cuando calcula el rol
		BlockUI blo_deta_rubro=new BlockUI();		
		blo_deta_rubro.setBlock(div.getDivision1().getClientId());
		blo_deta_rubro.setWidgetVar("blo_deta_rubro");
		blo_deta_rubro.getChildren().add(new Etiqueta("Calculando... </br>"));
		Imagen ima_gif=new Imagen();
		ima_gif.setValue("imagenes/cargando.gif");
		blo_deta_rubro.getChildren().add(ima_gif);
		agregarComponente(blo_deta_rubro);

		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE ELIMINAR EL ROL GENERADO PARA EL TIPO DE NOMINA SELECCIONADO");
		con_guardar.setTitle("CONFIRMACION ELIMINACION DE ROL");
		con_guardar.getBot_aceptar().setMetodo("eliminarNominaGenerada");
		agregarComponente(con_guardar);

		set_pago_decimos.setId("set_pago_decimos");
		set_pago_decimos.setSeleccionTabla("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"WHERE ACTIVO_NRDTN=TRUE " +
				"and DTN.IDE_NRTIN in ("+utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")+") ", "IDE_NRDTN");
		set_pago_decimos.setTitle("seleccion de Nomina para Pago de Decimos");
		set_pago_decimos.getBot_aceptar().setMetodo("aceptarPagoDecimos");
		agregarComponente(set_pago_decimos);
		
		
		set_pago_fondos.setId("set_pago_fondos");
		set_pago_fondos.setSeleccionTabla("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"WHERE ACTIVO_NRDTN=TRUE " +
				"and DTN.IDE_NRTIN in ("+utilitario.getVariable("p_nrh_tipo_nomina_fondos_reserva")+") ", "IDE_NRDTN");
		set_pago_fondos.setTitle("seleccion de Nomina para Pago de Fondos Reserva");
		set_pago_fondos.getBot_aceptar().setMetodo("aceptarPagoFondos");
		agregarComponente(set_pago_fondos);

		
		
	}


	public boolean validarParametrosSistemaImportados(){
		if (utilitario.getVariable("p_nrh_tipo_nomina_normal")==null || utilitario.getVariable("p_nrh_tipo_nomina_normal").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_normal");
			return false;
		}

		if (utilitario.getVariable("p_nrh_estado_nomina_cerrada")==null || utilitario.getVariable("p_nrh_estado_nomina_cerrada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_estado_nomina_cerrada");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_valor_recibir")==null || utilitario.getVariable("p_nrh_rubro_valor_recibir").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_valor_recibir");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_total_ingresos")==null || utilitario.getVariable("p_nrh_rubro_total_ingresos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_total_ingresos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_total_egresos")==null || utilitario.getVariable("p_nrh_rubro_total_egresos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_total_egresos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")==null || utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_pago_decimos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_tipo_nomina_liquidacion")==null || utilitario.getVariable("p_nrh_tipo_nomina_liquidacion").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_liquidacion");
			return false;
		}

		if (utilitario.getVariable("p_nrh_estado_pre_nomina")==null || utilitario.getVariable("p_nrh_estado_pre_nomina").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_estado_pre_nomina");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_region")==null || utilitario.getVariable("p_nrh_rubro_region").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_region");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar")==null || utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_desc_valores_liquidar");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_remuneracion_unificada");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_remuneracion_unificada_honorarios");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva")==null || utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_acumula_fondos_reserva");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_dias_trabajados")==null || utilitario.getVariable("p_nrh_rubro_dias_trabajados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_trabajados");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_dias_antiguedad")==null || utilitario.getVariable("p_nrh_rubro_dias_antiguedad").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_antiguedad");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva")==null || utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_fondos_reserva");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina")==null || utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_periodo_nomina");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante")==null || utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_rmu_cargo_subrogante");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_subrogados")==null || utilitario.getVariable("p_nrh_rubro_dias_subrogados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_subrogados");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo")==null || utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_ajuste_sueldo");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_ajuste_sueldo")==null || utilitario.getVariable("p_nrh_rubro_ajuste_sueldo").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_ajuste_sueldo");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion")==null || utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_pendientes_vacacion");
			return false;
		}
		return true;
	}

	public void seleccionaPeriodo(){

		String tipo_rol=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("TIPO_ROL");
		
		
	if(tipo_rol.equals(utilitario.getVariable("p_nrh_generar_rol_decimo_cuarto"))){
			
			tab_detalle_tipo_nomina.setSql("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN," +
					"TEM.DETALLE_GTTEM " +
					"from NRH_DETALLE_TIPO_NOMINA DTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
					"LEFT JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_NRTIT=DTN.IDE_NRTIT "+
					"WHERE PRO.IDE_GEPRO =-1 ");			
		}else if(tipo_rol.equals(utilitario.getVariable("p_nrh_generar_rol_decimo_tercer"))){
			
			tab_detalle_tipo_nomina.setSql("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN," +
					"TEM.DETALLE_GTTEM " +
					"from NRH_DETALLE_TIPO_NOMINA DTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
					"LEFT JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_NRTIT=DTN.IDE_NRTIT "+
					"WHERE PRO.IDE_GEPRO =-1 ");
			
		}else{
						//Si la nomina es de tipo manual 4
		tab_detalle_tipo_nomina.setSql("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN," +
				"TEM.DETALLE_GTTEM " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"LEFT JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_NRTIT=DTN.IDE_NRTIT "+
				"WHERE ACTIVO_NRDTN=true "+
				"and TIN.IDE_NRTIN in ( "+tipo_rol+") " +
				"AND PRO.IDE_GEPRO IN ("+com_periodo.getValue()+") ");
		}
		 
						
		tab_detalle_tipo_nomina.ejecutarSql();	
		
		
		
	}

	
	public void seleccionaPeriodoFondos(){
		tab_detalle_tipo_nomina.setSql("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN," +
				"TEM.DETALLE_GTTEM " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"LEFT JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_NRTIT=DTN.IDE_NRTIT "+
				"WHERE ACTIVO_NRDTN=true "+
				"and TIN.IDE_NRTIN in ( "+utilitario.getVariable("p_nrh_tipo_nomina_para_generar_rol")+") " +
				"AND PRO.IDE_GEPRO IN ("+com_periodo.getValue()+") ");
		tab_detalle_tipo_nomina.ejecutarSql();
	}

	boolean boo_recalcular_decimo=false;
	public void aceptarPagoDecimos(){

		if (set_pago_decimos.getSeleccionados()==null || set_pago_decimos.getSeleccionados().isEmpty()){
			utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			return;
		}
		TablaGenerica tab_per=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
		if (tab_per.getValor("FECHA_INICIAL_GEPRO")==null || tab_per.getValor("FECHA_INICIAL_GEPRO").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar los decimos", "No existe fecha inicial de periodo");
			return ;
		}
		if (tab_per.getValor("FECHA_FINAL_GEPRO")==null || tab_per.getValor("FECHA_FINAL_GEPRO").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar los decimos", "No existe fecha final de periodo");
			return ;
		}


		if (!validarPagoDecimos(com_periodo.getValue()+"")){
			utilitario.agregarMensajeInfo("No se puede generar los decimos", "No existe fecha de pago de decimos para el periodo seleccionado");
			return;
		}

		for (int k = 0; k < set_pago_decimos.getListaSeleccionados().size(); k++) {
			Fila fila=set_pago_decimos.getListaSeleccionados().get(k);
			TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol.getTotalFilas()>0){
				if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
					utilitario.agregarMensajeInfo("No se puede generar los decimos", "Existe nomina de pago de decimos cerrada "+fila.getCampos()[1]+"-"+fila.getCampos()[2]);
					return;
				}
			}		
		}

		if (!con_guardar.isVisible()){
			boo_recalcular_decimo=false;
			for (int k = 0; k < set_pago_decimos.getListaSeleccionados().size(); k++) {
				Fila fila=set_pago_decimos.getListaSeleccionados().get(k);
				TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
				if (tab_rol.getTotalFilas()>0){

					con_guardar.setMessage("Ya existe generado pago de decimos, Desea recalcular");
					con_guardar.setTitle("CONFIRMACION RECALCULAR DECIMOS");

					con_guardar.getBot_aceptar().setMetodo("aceptarPagoDecimos");
					boo_recalcular_decimo=true;
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
					return;

				}		
			}
		}else{
			con_guardar.cerrar();
		}

		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
		Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
		for (int k = 0; k < set_pago_decimos.getListaSeleccionados().size(); k++) {
			Fila fila=set_pago_decimos.getListaSeleccionados().get(k);

			TablaGenerica tab_rol_aux=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol_aux.getTotalFilas()>0){
				String ide_nrtit=ser_nomina.getDetalleTipoNomina(tab_rol_aux.getValor("ide_nrdtn")).getValor("ide_nrtit");
				//El cero del final corresponde a que no contiene un tipo de rol
				ser_nomina.reCalcularNomina(fila.getRowKey()+"", com_periodo.getValue()+"",ide_nrtit,true,false,"",false,0);
				utilitario.agregarMensaje("Se genero correctamente "+fila.getCampos()[1]+"-"+fila.getCampos()[2],"");

			}
			else{
				String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIN");
				//if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")))
				if (utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN)) //awbecerra 
				{

					String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");
                                        //ecevallos
					String sql=ser_nomina.getSqlEmpleadosTipoNomina(fila.getRowKey(),fecha_final_gepro);
					//String sql=ser_nomina.getSqlEmpleadosNomina(ser_nomina.getRol(fila.getRowKey(),com_periodo.getValue()+"").getValor("IDE_NRROL"));

					/*sql="select IDE_GEEDP,DOCUMENTO_IDENTIDAD_GTEMP,NOMBRES, IDE_SUCU, IDE_GTEMP, IDE_GTTEM, FECHA_GEEDP, RMU_GEEDP, AJUSTE_SUELDO_GEEDP, "
							+ "FECHA_AJUSTE_GEEDP, FECHA_INGRESO_GTEMP, ACUMULA_FONDOS_GEEDP, IDE_GEREG, FECHA_ENCARGO_GEEDP, SUELDO_SUBROGA_GEEDP, "
							+ "FECHA_ENCARGO_FIN_GEEDP, dias_vacacion, DISCAPACITADO_GTEMP,  ACUMULA_DECIMO_GTEMP,  PORCENTAJE_GTDIE from ("+sql+") ac where coalesce(ACUMULA_DECIMO_GTEMP,false)=true ";
					*/
					TablaGenerica tab_emp=utilitario.consultar(sql);
					if (tab_emp.getTotalFilas()>0){

						String ide_nrrol=insertarCabeceraRol(fila.getRowKey(),com_periodo.getValue()+"");
						guardarPantalla();
						TablaGenerica deta_rol=ser_nomina.calcularRol(tab_emp,ide_nrrol,ide_num_max,0,"");
						ide_num_max=ide_num_max+Long.parseLong(deta_rol.getTotalFilas()+"")+1;
						
						
					TablaGenerica	TabEmpleadosDecimo=utilitario.consultar("select ide_nrrol,ide_geedp from nrh_detalle_rol where ide_nrrol in(144) "
							+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro drub "
							+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub  "
							+ "where rub.ide_nrrub in(131) "
							+ ") and valor_nrdro=0");
						
					if (TabEmpleadosDecimo.getTotalFilas()>0) {
						StringBuilder str_empleados_sin_decimo= new StringBuilder();
						
						for (int i = 0; i < TabEmpleadosDecimo.getTotalFilas(); i++) 
						{
							
							if (i==(TabEmpleadosDecimo.getTotalFilas()-1)) {
								str_empleados_sin_decimo.append(TabEmpleadosDecimo.getValor(i,"IDE_GEEDP"));
							}else{
							str_empleados_sin_decimo.append(TabEmpleadosDecimo.getValor(i,"IDE_GEEDP"));
							str_empleados_sin_decimo.append(",");}
					}
						
						utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrrol in("+ide_nrrol+") and ide_geedp in ("+str_empleados_sin_decimo.toString()+") ");
											
					}else {
						
				}

					

					}
			}

			}


		}
		set_pago_decimos.cerrar();
	}

////////////////////////////////////////////////////////////////////////////CALCULO DE FONDOS DE RESERVA/////////////////////////////////////////////////////////

	
	boolean boo_recalcular_fondos=false;
	public void aceptarPagoFondos(){

		if (set_pago_fondos.getSeleccionados()==null || set_pago_fondos.getSeleccionados().isEmpty()){
			utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			return;
		}
		TablaGenerica tab_per=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
		if (tab_per.getValor("FECHA_INICIAL_GEPRO")==null || tab_per.getValor("FECHA_INICIAL_GEPRO").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar fondos de reserva", "No existe fecha inicial de periodo");
			return ;
		}
		if (tab_per.getValor("FECHA_FINAL_GEPRO")==null || tab_per.getValor("FECHA_FINAL_GEPRO").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar los fondos de reserva", "No existe fecha final de periodo");
			return ;
		}

		for (int k = 0; k < set_pago_fondos.getListaSeleccionados().size(); k++) {
			Fila fila=set_pago_fondos.getListaSeleccionados().get(k);
			TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol.getTotalFilas()>0){
				if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
					utilitario.agregarMensajeInfo("No se puede generar los fondos", "Existe nomina de pago de decimos cerrada "+fila.getCampos()[1]+"-"+fila.getCampos()[2]);
					return;
				}
			}		
		}

		if (!con_guardar.isVisible()){
			boo_recalcular_fondos=false;
			for (int k = 0; k < set_pago_fondos.getListaSeleccionados().size(); k++) {
				Fila fila=set_pago_fondos.getListaSeleccionados().get(k);
				TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
				if (tab_rol.getTotalFilas()>0){

					con_guardar.setMessage("Ya existe generado pago de fondos de reserva, Desea recalcular");
					con_guardar.setTitle("CONFIRMACION RECALCULAR FONDOS DE RESERVA");
					con_guardar.getBot_aceptar().setMetodo("aceptarPagoFondos");
					boo_recalcular_fondos=true;
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
					return;
				}		
			}
		}else{
			con_guardar.cerrar();
		}

		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
		Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
		for (int k = 0; k < set_pago_fondos.getListaSeleccionados().size(); k++) {
			Fila fila=set_pago_fondos.getListaSeleccionados().get(k);
			TablaGenerica tab_rol_aux=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol_aux.getTotalFilas()>0){
				String ide_nrtit=ser_nomina.getDetalleTipoNomina(tab_rol_aux.getValor("ide_nrdtn")).getValor("ide_nrtit");
				if(boo_recalcular_fondos==true){
					String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");
					TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_final_gepro)+"%'");
					String ide_gepro_anterio=ser_nomina.getPeriodoRolNomina(fecha_final_gepro);
					//El numero cero al final corresponde al tipo de rol a ejecutar
					ser_nomina.reCalcularNomina(fila.getRowKey()+"", com_periodo.getValue()+"",ide_nrtit,false,true,ide_gepro_anterio,false,0);
				}else{
					//El numero cero al final corresponde al tipo de rol a ejecutar
				ser_nomina.reCalcularNomina(fila.getRowKey()+"", com_periodo.getValue()+"",ide_nrtit,true,false,"",false,0);}
				utilitario.agregarMensaje("Se genero correctamente "+fila.getCampos()[1]+"-"+fila.getCampos()[2],"");

			}
			else{
				String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIN");
				//if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")))
				if (utilitario.getVariable("p_nrh_tipo_nomina_fondos_reserva").contains(IDE_NRTIN)) //awbecerra 
				{	String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");	
					String ide_gepro_anterio=ser_nomina.getPeriodoRolNominaFondos(fecha_final_gepro,Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra")));
					String sql=ser_nomina.getSqlEmpleadosTipoNominaFondosReserva(fila.getRowKey(),fecha_final_gepro,ide_gepro_anterio,Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra")));
					TablaGenerica tab_emp=utilitario.consultar(sql);
					if (tab_emp.getTotalFilas()>0){
						String ide_nrrol=insertarCabeceraRol(fila.getRowKey(),com_periodo.getValue()+"");
						guardarPantalla();
						TablaGenerica deta_rol=ser_nomina.calcularRol(tab_emp,ide_nrrol,ide_num_max,0,"");
						ide_num_max=ide_num_max+Long.parseLong(deta_rol.getTotalFilas()+"")+1;
					}
				}
			}

		}
		set_pago_fondos.cerrar();
	}
	
	
	
	
	String str_ide_nrder_decimos="";
	public boolean validarPagoDecimos(String ide_gepro){
		int band=0;
		str_ide_nrder_decimos="";		
		for (int i = 0; i < set_pago_decimos.getListaSeleccionados().size(); i++) {
			Fila fila=set_pago_decimos.getListaSeleccionados().get(i);
			int anio_periodo=utilitario.getAnio(ser_nomina.getPeriodoRol(ide_gepro).getValor("fecha_inicial_gepro"));
			TablaGenerica tab_decimos=utilitario.consultar("select * from NRH_DETALLE_RUBRO " +
					"where IDE_NRDTN="+fila.getRowKey()+" " +

					"and ide_nrrub in (select IDE_NRRUB from NRH_RUBRO where DECIMO_NRRUB=TRUE) ");


			for (int j = 0; j < tab_decimos.getTotalFilas(); j++) {
				String fecha_pago_nrder=tab_decimos.getValor(j,"FECHA_PAGO_NRDER");
				if (fecha_pago_nrder!=null && !fecha_pago_nrder.isEmpty()){
					try {
						fecha_pago_nrder=fecha_pago_nrder.substring(0, fecha_pago_nrder.indexOf("/"));
					} catch (Exception e) {
						// TODO: handle exception
					}

					String fecha_pago_rubro=""+anio_periodo+"-"+fecha_pago_nrder;
					String query = "select * from GEN_PERIDO_ROL where IDE_GEPRO="+ide_gepro+" " +
							"and ('"+fecha_pago_rubro+"') BETWEEN FECHA_INICIAL_GEPRO and FECHA_FINAL_GEPRO ";
					System.out.println(query);
					//ecevallos
					TablaGenerica tab_per=utilitario.consultar(query);

					if (tab_per.getTotalFilas()>0){
						str_ide_nrder_decimos+=tab_decimos.getValor(j,"IDE_NRDER")+",";
						band=1;
					}
				}
				//				System.out.println("periodos "+tab_per.getSql());
			}

		}
		if (band==1){
			str_ide_nrder_decimos=str_ide_nrder_decimos.substring(0, str_ide_nrder_decimos.length()-1);
			return true;
		}
		return false;
	}
	
	
	
	String str_ide_nrder_fondos="";
	public boolean validarPagoFondos(String ide_gepro){
		int band=0;
		str_ide_nrder_fondos="";		
		for (int i = 0; i < set_pago_fondos.getListaSeleccionados().size(); i++) {
			Fila fila=set_pago_fondos.getListaSeleccionados().get(i);
			int anio_periodo=utilitario.getAnio(ser_nomina.getPeriodoRol(ide_gepro).getValor("fecha_inicial_gepro"));
			TablaGenerica tab_decimos=utilitario.consultar("select * from NRH_DETALLE_RUBRO " +
					"where IDE_NRDTN="+fila.getRowKey()+" " +

					"and ide_nrrub in (select IDE_NRRUB from NRH_RUBRO where DECIMO_NRRUB=TRUE) ");


			for (int j = 0; j < tab_decimos.getTotalFilas(); j++) {
				String fecha_pago_nrder=tab_decimos.getValor(j,"FECHA_PAGO_NRDER");
				if (fecha_pago_nrder!=null && !fecha_pago_nrder.isEmpty()){
					try {
						fecha_pago_nrder=fecha_pago_nrder.substring(0, fecha_pago_nrder.indexOf("/"));
					} catch (Exception e) {
						// TODO: handle exception
					}

					String fecha_pago_rubro=""+anio_periodo+"-"+fecha_pago_nrder;
					String query = "select * from GEN_PERIDO_ROL where IDE_GEPRO="+ide_gepro+" " +
							"and ('"+fecha_pago_rubro+"') BETWEEN FECHA_INICIAL_GEPRO and FECHA_FINAL_GEPRO ";
					System.out.println(query);
					//ecevallos
					TablaGenerica tab_per=utilitario.consultar(query);

					if (tab_per.getTotalFilas()>0){
						str_ide_nrder_decimos+=tab_decimos.getValor(j,"IDE_NRDER")+",";
						band=1;
					}
				}
				//				System.out.println("periodos "+tab_per.getSql());
			}

		}
		if (band==1){
			str_ide_nrder_fondos=str_ide_nrder_fondos.substring(0, str_ide_nrder_fondos.length()-1);
			return true;
		}
		return false;
	}

	
	
	
	
	public void abrirDialogoPagoDecimos(){

		if (com_periodo.getValue()!=null){
			set_pago_decimos.dibujar();
		}else{
			utilitario.agregarMensajeInfo("Primero debe seleccionar el periodo del rol", "");
		}


		//			if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()>0){
		//				if (validarPagoDecimos(com_periodo.getValue()+"")){
		//					utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
		//					Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
		//					for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
		//						Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
		//						String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIN");
		//						if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		//							String sql=ser_nomina.getSqlEmpleadosTipoNomina(fila.getRowKey());
		//							TablaGenerica tab_emp=utilitario.consultar(sql);
		//							if (tab_emp.getTotalFilas()>0){
		//								String ide_nrrol=insertarCabeceraRol(fila.getRowKey(),com_periodo.getValue()+"");
		//								guardarPantalla();
		//								TablaGenerica deta_rol=ser_nomina.generarDecimos(tab_emp,ide_nrrol,ide_num_max,str_ide_nrder_decimos);
		//								ide_num_max=ide_num_max+Long.parseLong(deta_rol.getTotalFilas()+"")+1;
		//							}
		//						}
		//					}
		//				}else{
		//					utilitario.agregarMensajeInfo("No se puede generar los decimos", "No existe fecha de pago de decimos para el periodo seleccionado");
		//				}
		//			}else{
		//				utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
		//			}
	}


	
	
	
	
	
	
	
	
	public void abrirDialogoFondosReserva(){
		if (com_periodo.getValue()!=null){
			
			set_pago_fondos.dibujar();
		}else{
			utilitario.agregarMensajeInfo("Primero debe seleccionar el periodo del rol", "");
			return;
		}
		
	}
	

	public void recalcularRubros(){
		if(com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("Primero debe seleccionar el periodo del rol", "");
			return;
		}
		if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()==0){
			utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			return;
		}
		int int_band=0;
		for (int k = 0; k < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); k++) {
			Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(k);
			if (ser_nomina.isNominaGenerada(fila.getRowKey(), com_periodo.getValue()+"")){
				int_band=1;
				break;
			}
		}
		if (int_band==1){
			for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
				Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
				String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIN");
				String ide_nrtit=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIT");

				if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){

					boolean nomina_manual=false;
					String IDE_NRDTN="";
					if (fila.getRowKey().toString().equals("20")) {
							IDE_NRDTN="";
							IDE_NRDTN="4";
							nomina_manual=true;
					}else if(fila.getRowKey().toString().equals("21")){
							IDE_NRDTN="";
							IDE_NRDTN="2";					
							nomina_manual=true;
					}else{
						nomina_manual=false;
					}
					
					if (ser_nomina.getRol(fila.getRowKey()+"", com_periodo.getValue()+"").getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
						
						TablaGenerica tab_periodo=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+com_periodo.getValue());
						int tipoRol=0;
						tipoRol=ser_nomina.validarTipoRol(tab_periodo.getValor("tipo_rol"));
						String ide_gepro_anterior="";
 
						
						//Implementar Horas Extra
						if (tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra")) 
						|| tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_alimentacion"))
						|| tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_tipo_nonima_horas_suplementarias"))
					    || tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_hxa"))){
					
							//Cambiar por el nombre generico getSqlEmpleadosTipoNominaFondosReserva ya que esta consulta servira para 
							ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(tab_periodo.getValor("fecha_final_gepro"),tipoRol);
						}else{
							ide_gepro_anterior="";
					}
						
						
						boolean fondos_reserva=false;
					 if(tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_fondos_reserva"))){
						ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(tab_periodo.getValor("fecha_final_gepro"),tipoRol);
						fondos_reserva=true;
					 }else{
						 fondos_reserva=false;
					 }
						
						
						if (tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_nomina_manual"))) {
							nomina_manual=true;	
						}else {
							nomina_manual=false;
						}			
			
					ser_nomina.reCalcularNomina(fila.getRowKey()+"", com_periodo.getValue()+"",ide_nrtit,false,fondos_reserva,ide_gepro_anterior,nomina_manual,tipoRol);
		//CALCULO JUBILACION POR INVALIDES DEL EMPLEADO ARIAS CAHUEÑAS
						
						TablaGenerica tabEmpleado = utilitario.consultar("Select ide_gtemp,jubilado_invalidez_gtemp from gth_empleado where jubilado_invalidez_gtemp=true");
						TablaGenerica TabEmpDepaActivo= utilitario.consultar("Select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_gtemp="+tabEmpleado.getValor("IDE_GTEMP")+" and ACTIVO_GEEDP=TRUE");
												
						TablaGenerica TabEmpRol= utilitario.consultar("select ide_nrdro,IDE_NRROL,valor_nrdro from nrh_detalle_rol  "
								+ "where ide_nrrol IN(SELECT IDE_NRROL FROM NRH_ROL  WHERE IDE_GEPRO="+ com_periodo.getValue()+") and ide_geedp="+TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.encerrarValoresRol(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.sumarRubrosJubilacionXInvalidacion(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.actualizarRubrosIessPersonalXJubilacionXInvalidacion(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						utilitario.agregarMensaje("Se guardo correctamente", "");
					}else if (ser_nomina.getRol(fila.getRowKey()+"", com_periodo.getValue()+"").getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
						utilitario.agregarMensajeInfo("No se puede re calcular la nomina "+fila.getCampos()[2], "Ya se encuentra cerrada");
						return;
					}else{
						utilitario.agregarMensajeInfo("No se puede re calcular la nomina "+fila.getCampos()[2], "No tiene estado de prenomina");
						return;
					}
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede recalcular","No existen roles generados para el periodo seleccionado");
		}


	}


	public void confirmarEliminarNominaGenerada(){
		if(com_periodo.getValue()!=null){
			if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()>0){
				int int_band=0;
				for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
					Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
					if (ser_nomina.isNominaGenerada(fila.getRowKey(), com_periodo.getValue()+"")){
						int_band=1;
						break;
					}
				}
				if (int_band==1){
					con_guardar.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No existe rol generado para la nomina seleccionada ","");
				}
			}else{
				utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			}
		}else{
			utilitario.agregarMensajeInfo("Primero debe seleccionar el periodo del rol", "");
		}
	}


	public void eliminarNominaGenerada(){


		for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
			Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
			TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol.getTotalFilas()>0){
				//				if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
				//						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

				TablaGenerica tab_per_rol=ser_nomina.getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));
				String fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
				String fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");

				String str_update_anticipos="update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
						"where FECHA_VENCIMIENTO_NRAMO " +
						"BETWEEN to_date ('"+fecha_ini_gepro+"','yy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yy-mm-dd') " +
						"and IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE " +
						"IDE_NRANT IN (SELECT IDE_NRANT FROM NRH_ANTICIPO WHERE " +
						"CALIFICADO_NRANT=true AND APROBADO_NRANT=true AND ACTIVO_NRANT=true AND " +
						"IDE_GEEDP IN (select EDP.IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
						"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
						"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
						"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
						"WHERE DTN.IDE_NRDTN="+fila.getRowKey()+" " +
						"AND EDP.ACTIVO_GEEDP=true " +
						")))";
				utilitario.getConexion().agregarSql(str_update_anticipos);
				System.out.println("update nticipos "+str_update_anticipos);

				utilitario.getConexion().agregarSql("delete from NRH_DETALLE_ROL where ide_nrrol ="+tab_rol.getValor(0,"IDE_NRROL"));
				utilitario.getConexion().agregarSql("delete from NRH_ROL where ide_nrrol ="+tab_rol.getValor(0,"IDE_NRROL"));

				//				}else{
				//					utilitario.agregarMensajeInfo("No se puede eliminar la nomina", "La nomina seleccionada no tiene estado de prenomina");
				//				}
			}
		}
		guardarPantalla();
		con_guardar.cerrar();

	}


	public void calcularRubros(){
		if(com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("Primero debe seleccionar el periodo del rol", "");
			return;
		}

		TablaGenerica tab_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
		String fecha_final_gepro=tab_gepro.getValor("FECHA_FINAL_GEPRO");
		String fecha_inicial_gepro=tab_gepro.getValor("FECHA_INICIAL_GEPRO");
		String  tipo_rol =  tab_gepro.getValor("tipo_rol");
		int tipoRol=0;
		tipoRol=ser_nomina.validarTipoRol(tab_gepro.getValor("tipo_rol"));

		if (fecha_inicial_gepro==null || fecha_inicial_gepro.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "No ha definido la fecha inicial del Periodo de rol ");
			return;
		}

		if (fecha_final_gepro==null || fecha_final_gepro.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "No ha definido la fecha final del Periodo de rol ");
			return;
		}
		if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()==0){
			utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			return;
		}

		int int_band=0;
		for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
			Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
			if (ser_nomina.isNominaGenerada(fila.getRowKey(), com_periodo.getValue()+"")){
				utilitario.agregarNotificacionInfo("No se puede generar la nomina "+fila.getCampos()[2]," ya se encuentra generada");
				int_band=1;
				break;
			}
		}


		// validar que no existan roles generados sin cerrar
		try {
			TablaGenerica tab_roles_tipo_normal_pre_nomina=utilitario.consultar("select IDE_NRROL,ROL.IDE_GEPRO,rol.IDE_NRDTN,detalle_nrtin,detalle_gttem,DETALLE_GEMES " +
					"from NRH_ROL rol " +
					"inner join NRH_DETALLE_TIPO_NOMINA dtn on dtn.IDE_NRDTN=rol.IDE_NRDTN " +
					"inner join NRH_TIPO_NOMINA tin on TIN.ide_nrtin=dtn.ide_nrtin " +
					"inner join GTH_TIPO_EMPLEADO tem on tem.ide_gttem=dtn.ide_gttem " +
					"inner join GEN_PERIDO_ROL pro on pro.ide_gepro=rol.ide_gepro "+
					"inner join GEN_MES mes on mes.ide_gemes=pro.ide_gemes "+
					"WHERE rol.IDE_NRESR="+utilitario.getVariable("p_nrh_estado_pre_nomina")+" " +
					"and TIN.IDE_NRTIN in ("+utilitario.getVariable("p_nrh_tipo_nomina_para_generar_rol")+") "+
					" and dtn.ide_gttem!=3 "
					+ "and pro.tipo_rol=0") ;
					//"and tin.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_normal")); //arreglado awbecerra
			tab_roles_tipo_normal_pre_nomina.imprimirSql();
			
			if (tab_roles_tipo_normal_pre_nomina.getTotalFilas()>0){
				TablaGenerica tab_gepro_pre_nomina=ser_nomina.getPeriodoTipoRol(tab_roles_tipo_normal_pre_nomina.getValor("IDE_GEPRO"));
				int mes=0;
				int anio=0;
				int mes_pre_nom=0;
				int anio_pre_nom=0;
				try {
					mes=pckUtilidades.CConversion.CInt(tab_gepro.getValor("IDE_GEMES"));
					anio=pckUtilidades.CConversion.CInt(ser_nomina.getAnio(tab_gepro.getValor("IDE_GEANI")).getValor("detalle_geani"));

					mes_pre_nom=pckUtilidades.CConversion.CInt(tab_gepro_pre_nomina.getValor("IDE_GEMES"));
					anio_pre_nom=pckUtilidades.CConversion.CInt(ser_nomina.getAnio(tab_gepro_pre_nomina.getValor("IDE_GEANI")).getValor("detalle_geani"));

				} catch (Exception e) {
					// TODO: handle exception
				}
				if (mes>0 && anio>0 && mes_pre_nom>0 && anio_pre_nom>0 ){
					if (anio>=anio_pre_nom){
						if (mes>mes_pre_nom){
							utilitario.agregarNotificacionInfo("No se puede calcular, Existen roles anteriores sin cerrar", "Cerrar nomina "+tab_roles_tipo_normal_pre_nomina.getValor("DETALLE_NRTIN")+" "+tab_roles_tipo_normal_pre_nomina.getValor("DETALLE_GTTEM")+" del periodo "+anio_pre_nom+" "+tab_roles_tipo_normal_pre_nomina.getValor("DETALLE_GEMES"));
							return;
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		if (int_band==0){
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
			Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
			boolean nomina_manual=false;
			for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
				Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
				String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(fila.getRowKey()+"").getValor("IDE_NRTIN");
				if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
					
				

					if (tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_nomina_manual"))) {
						nomina_manual=true;	
					}else {
						nomina_manual=false;
					}			

					String sql="",ide_gepro_anterior="";
					if (nomina_manual) {
					sql=ser_nomina.getSqlEmpleadosTipoNominaManual(fila.getRowKey(),fecha_final_gepro);
					}else{
						//Implementar Horas Extra
						if (tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra")) 
							|| tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_tipo_nonima_horas_suplementarias"))) {
							//Cambiar por el nombre generico getSqlEmpleadosTipoNominaFondosReserva ya que esta consulta servira para 
							ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(fecha_final_gepro,tipoRol);
							sql=ser_nomina.getSqlEmpleadosTipoNominaFondosReserva(fila.getRowKey(),fecha_final_gepro,ide_gepro_anterior,tipoRol);
						}else if(tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_fondos_reserva"))){
							ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(fecha_final_gepro,tipoRol);
							sql=ser_nomina.getSqlEmpleadosTipoNominaFondosReserva(fila.getRowKey(),fecha_final_gepro,ide_gepro_anterior,tipoRol);		
						}else if(tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_alimentacion"))){
							ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(fecha_final_gepro,tipoRol);
							sql=ser_nomina.getSqlEmpleadosTipoNominaFondosReserva(fila.getRowKey(),fecha_final_gepro,ide_gepro_anterior,tipoRol);		

						}else if(tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_hxa"))){
							ide_gepro_anterior=ser_nomina.getPeriodoRolNominaFondos(fecha_final_gepro,tipoRol);
							sql=ser_nomina.getSqlEmpleadosTipoNominaFondosReserva(fila.getRowKey(),fecha_final_gepro,ide_gepro_anterior,tipoRol);		
						
						}else{		
					sql=ser_nomina.getSqlEmpleadosTipoNomina(fila.getRowKey(),fecha_final_gepro);
					}
					}
					TablaGenerica tab_emp=utilitario.consultar(sql);
					if (tab_emp.getTotalFilas()>0){
						//							if (ser_nomina.getRol(fila.getRowKey()+"", com_periodo.getValue()+"").getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
						System.out.println("ide_nrdtn "+fila.getRowKey());
						System.out.println("periodo "+com_periodo.getValue());
						String ide_nrrol=insertarCabeceraRol(fila.getRowKey(),com_periodo.getValue()+"");

						guardarPantalla();
						System.out.println("ide max "+ide_num_max);
						 
						TablaGenerica deta_rol=ser_nomina.calcularRol(tab_emp,ide_nrrol,ide_num_max,tipoRol,ide_gepro_anterior);
						ide_num_max=ide_num_max+Long.parseLong(deta_rol.getTotalFilas()+"")+1;
						
						//CALCULO JUBILACION POR INVALIDES DEL EMPLEADO ARIAS CAHUEÑAS
						TablaGenerica tabEmpleado = utilitario.consultar("Select ide_gtemp,jubilado_invalidez_gtemp from gth_empleado where jubilado_invalidez_gtemp=true");
						TablaGenerica TabEmpDepaActivo= utilitario.consultar("Select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_gtemp="+tabEmpleado.getValor("IDE_GTEMP")+" and ACTIVO_GEEDP=TRUE");
						TablaGenerica TabEmpRol= utilitario.consultar("select ide_nrdro,IDE_NRROL,valor_nrdro from nrh_detalle_rol  "
								+ "where ide_nrrol IN(SELECT IDE_NRROL FROM NRH_ROL  WHERE IDE_GEPRO="+ com_periodo.getValue()+") and ide_geedp="+TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.encerrarValoresRol(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.sumarRubrosJubilacionXInvalidacion(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						ser_nomina.actualizarRubrosIessPersonalXJubilacionXInvalidacion(TabEmpRol.getValor("IDE_NRROL"),TabEmpDepaActivo.getValor("IDE_GEEDP"));
						//Eliminar empleados que se no se encuentren generando valores 
											
						
					}
					else
					{
						utilitario.agregarNotificacionInfo("No se puede generar el rol porque no existen empleados para el tipo: "+fila.getCampos()[2], "Generar Nomina");
					}

				}
				else
				{
					utilitario.agregarNotificacionInfo("No se puede generar el rol tipo: "+fila.getCampos()[2], "Generar Nomina");
				}
			}
		}
	}


	public void calcularRenta(){
		// valida que seleccione un periodo
		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede calcular la renta","Debe seleccionar un periodo");
			return;
		}

		if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()==0){
			utilitario.agregarMensajeInfo("No existen nominas seleccionadas", "");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_aportes_personales")==null || utilitario.getVariable("p_nrh_rubro_aportes_personales").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")==null || utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_subtotal_iess")==null || utilitario.getVariable("p_nrh_rubro_subtotal_iess").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_factor_multiplicador_renta_discapacitados")==null || utilitario.getVariable("p_factor_multiplicador_renta_discapacitados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado el parametro de factor multiplizador para calculo de la renta de discapcitados y tercera edad, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_seguro_social_con_rmu")==null || utilitario.getVariable("p_nrh_rubro_seguro_social_con_rmu").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado el parametro p_nrh_rubro_seguro_social_con_rmu para calculo de la renta, favor importar los parametros del sistema");
			return;
		}


		String fecha_ini_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_INICIAL_GEPRO");

		// obtenemos el ide_srimr de la tabla sri impuesto a la renta de acuerdo a la fecha de inicio de periodo
		String ide_srimr=ser_nomina.getSriImpuestoRenta(fecha_ini_gepro).getValor("IDE_SRIMR");

		// validamos que exista configuarcion de detalles de la tabla del sri para retenciones
		if (ser_nomina.getSriDetalleImpuestoRenta(ide_srimr).getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No existe configuracion de la tabla de impuesto a la renta del sri para el año del periodo seleccionado");
			return;
		}



		for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
			Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
			TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey()+"", com_periodo.getValue()+"");
			if (tab_rol.getTotalFilas()>0){

				// valido que aun no se calcule la renta
				String estado_renta_nrrol=tab_rol.getValor(tab_rol.getFilaActual(),"ESTADO_RENTA_NRROL");
				System.out.println("estado renta "+estado_renta_nrrol);
				if (estado_renta_nrrol!=null && !estado_renta_nrrol.isEmpty() 
						&& estado_renta_nrrol.equalsIgnoreCase("1")){
					utilitario.agregarMensajeInfo("No se puede calcular la renta", "Ya se calculo la renta para la nomina "+fila.getCampos()[2]+", Recalcule la nomina, para poder calcular la renta nuevamente");
					return;
				}

				if (tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
					try {						
						TablaGenerica tab_periodo=utilitario.consultar("select * from gen_perido_rol where ide_gepro in("+tab_rol.getValor("ide_gepro")+")");
						//Se valida el tipo de rol
						int tipoRol=ser_nomina.validarTipoRol(tab_periodo.getValor("tipo_rol"));
						if (tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_nomina_normal")))
						{
							//|| tipoRol==Integer.parseInt(utilitario.getVariable("p_nrh_generar_rol_horas_extra"))
							ser_nomina.calcularRentaEmpleados(tab_rol.getValor("IDE_NRROL"),tipoRol);}
						else{
							//si no es de tipo normal se guarda el campo estado rol como calculado
							utilitario.agregarMensaje("se guardo correctamente", "");
							utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+tab_rol.getValor("ide_nrrol"));	
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("error al calcular renta en tipo de nomina (ide) "+fila.getRowKey());
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede calcular la renta", "La nomina "+fila.getCampos()[2]+" no tiene estado pre-nomina");
					break;
				}
			}
		}

	}

	public String insertarCabeceraRol(String IDE_NRDTN,String IDE_GEPRO){
		Tabla tab_rol=new Tabla();
		String ide_nrtit=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("IDE_NRTIT");
		tab_rol.setTabla("NRH_ROL", "IDE_NRROL",-1);
		tab_rol.setCondicion("IDE_NRROL=-1");
		tab_rol.ejecutarSql();
		tab_rol.insertar();
		tab_rol.setValor("IDE_NRESR", utilitario.getVariable("p_nrh_estado_pre_nomina"));
		tab_rol.setValor("IDE_NRDTN", IDE_NRDTN);
		tab_rol.setValor("IDE_GEPRO", IDE_GEPRO);
		tab_rol.setValor("FECHA_NRROL", utilitario.getFechaActual());
		tab_rol.setValor("ACTIVO_NRROL", "true");
		tab_rol.setValor("IDE_USUA", utilitario.getVariable("IDE_USUA"));
		if (tab_rol.guardar()){
			return tab_rol.getValor(0,"IDE_NRROL");
		}else{
			return null;
		}
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

		if (tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size()>1){
			utilitario.agregarMensajeInfo("No se puede eliminar", "Solo puede seleccionar una nomina");
			return;
		}

		for (int i = 0; i < tab_detalle_tipo_nomina.getListaFilasSeleccionadas().size(); i++) {
			Fila fila=tab_detalle_tipo_nomina.getListaFilasSeleccionadas().get(i);
			TablaGenerica tab_rol=ser_nomina.getRol(fila.getRowKey(), com_periodo.getValue()+"");
			if (tab_rol.getTotalFilas()>0){
				if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
					con_guardar.setMessage("ESTA SEGURO DE ELIMINAR LA NOMINA SELECCIONADA");
					con_guardar.setTitle("CONFIRMACION ELIMINACION DE NOMINA");

					con_guardar.getBot_aceptar().setMetodo("eliminarNominaGenerada");

					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
				}else if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
					utilitario.agregarMensajeInfo("No se puede eliminar la nomina", "La nomina seleccionada se encuentra cerrada");
					return;
				}else{
					utilitario.agregarMensajeInfo("No se puede eliminar la nomina", "La nomina seleccionada no tiene estado pre-nomina");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede eliminar la nomina", "No existe rol generado por elimnar");
				return;
			}
		}


	}

	public Tabla getTab_detalle_tipo_nomina() {
		return tab_detalle_tipo_nomina;
	}

	public void setTab_detalle_tipo_nomina(Tabla tab_detalle_tipo_nomina) {
		this.tab_detalle_tipo_nomina = tab_detalle_tipo_nomina;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public SeleccionTabla getSet_pago_decimos() {
		return set_pago_decimos;
	}

	public void setSet_pago_decimos(SeleccionTabla set_pago_decimos) {
		this.set_pago_decimos = set_pago_decimos;
	}


	public SeleccionTabla getSet_pago_fondos() {
		return set_pago_fondos;
	}


	public void setSet_pago_fondos(SeleccionTabla set_pago_fondos) {
		this.set_pago_fondos = set_pago_fondos;
	}


		
		
		

}
