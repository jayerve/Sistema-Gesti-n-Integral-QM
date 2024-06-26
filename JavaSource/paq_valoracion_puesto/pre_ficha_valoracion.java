package paq_valoracion_puesto;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_ficha_valoracion extends Pantalla{
	
	private Tabla tab_ficha=new Tabla();
	private Tabla tab_detalle=new Tabla();
	private Tabla tab_descripcion=new Tabla();
	private String empleado;
	private SeleccionTabla sel_factor=new SeleccionTabla();
	private SeleccionTabla sel_valora=new SeleccionTabla();

	
	 @EJB
	 private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	 
	 @EJB
	 private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	
	
	public pre_ficha_valoracion (){
		
		///TABULADOR
				Tabulador tab_Tabulador=new Tabulador();
				tab_Tabulador.setId("tab_tabulador");
				
		///seguridad
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
		utilitario.agregarNotificacionInfo("Mensaje", "No exixte usuario registrado para el registro de compras");
		return;
	}
		tab_ficha.setId("tab_ficha");
		tab_ficha.setHeader("VALORACIÓN PUESTOS");
		tab_ficha.setTabla("gth_ficha_valoracion", "ide_gtfiv", 1);
		tab_ficha.getColumna("activo_gtfiv").setValorDefecto("true");
		tab_ficha.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_ficha.getColumna("ide_gtemp").setLectura(true);
		tab_ficha.getColumna("ide_gtemp").setAutoCompletar();
		tab_ficha.getColumna("fecha_valoracion_gtfiv").setValorDefecto(utilitario.getFechaActual());
		tab_ficha.agregarRelacion(tab_descripcion);
		tab_ficha.agregarRelacion(tab_detalle);
		//tab_ficha.agregarRelacion(tab_descripcion);
		tab_ficha.dibujar();
		PanelTabla pat_ficha=new PanelTabla();
		pat_ficha.setPanelTabla(tab_ficha);
		
		
		
		////detalle valoración
		
		tab_detalle.setId("tab_detalle");
		//tab_detalle.setHeader("DETALLE VALORACIÓN");
		tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
		tab_detalle.setTabla("gth_detalle_valoracion", "ide_gtdev", 2);
		tab_detalle.getColumna("activo_gtdev").setValorDefecto("true");
		tab_detalle.getColumna("ide_gtvag").setCombo("select ide_gtvag, detalle_gtfav, detalle_gtvag from gth_valora_grupo a, gth_factor_valoracion b" +
										" where a.ide_gtfav= b.ide_gtfav order by detalle_gtfav,detalle_gtvag");
		//tab_detalle.getColumna("ide_gtvag").setCombo("gth_valora_grupo", "ide_gtvag", "detalle_gtvag", "");
		tab_detalle.getColumna("ide_gtvag").setAutoCompletar();
		tab_detalle.getColumna("ide_gtvag").setLectura(true);
		tab_detalle.getColumna("PUNTOS_GTDEV").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_detalle.getColumna("PUNTOS_GTDEV").setEtiqueta();
		tab_detalle.getColumna("PUNTOS_GTDEV").setValorDefecto("00.00");
		tab_detalle.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle);
		
		///DESCRIPCIÓN VALORACION
		

		tab_descripcion.setId("tab_descripcion");
		//tab_descripcion.setHeader("DESCRIPCIÓN VALORACIÓN");
		tab_descripcion.setIdCompleto("tab_tabulador:tab_descripcion");
		tab_descripcion.setTabla("gth_descripcion_valoracion", "ide_gtdva",3);
		tab_descripcion.getColumna("ide_geare").setCombo("gen_area", "ide_geare", "detalle_geare", "");
		tab_descripcion.getColumna("activo_gtdva").setValorDefecto("true");
		tab_descripcion.getColumna("ide_gegro").setCombo("gen_grupo_ocupacional", "ide_gegro", "detalle_gegro", "");
		tab_descripcion.getColumna("ide_gegro").setMetodoChange("rmu");
		tab_descripcion.getColumna("rmu_gtdva").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_descripcion.getColumna("rmu_gtdva").setEtiqueta();
		tab_descripcion.getColumna("rmu_gtdva").setValorDefecto("00.00");
		tab_descripcion.getColumna("TOTAL_PUNTOS_GTDVA").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_descripcion.getColumna("TOTAL_PUNTOS_GTDVA").setEtiqueta();
		tab_descripcion.getColumna("TOTAL_PUNTOS_GTDVA").setValorDefecto("00.00");
	
		tab_descripcion.dibujar();
		PanelTabla pat_des=new PanelTabla();
		pat_des.setPanelTabla(tab_descripcion);
		
		tab_Tabulador.agregarTab("DETALLE VALORACIÓN", pat_detalle);
		tab_Tabulador.agregarTab("DESCRIPCIÓN VALORACIÓN", pat_des);

		Division div_divi=new Division();
		div_divi.dividir2(pat_ficha, tab_Tabulador, "50%", "h");
		agregarComponente(div_divi);
	
		/////factor valoraciòn
		Boton bot_puesto = new Boton();
		bot_puesto.setValue("Valoraciòn Puesto");
		//bot_puesto.setTitle("Valoraciòn Puesto");
		bot_puesto.setIcon("ui-icon-person");
		bot_puesto.setMetodo("importarPuesto");
		bar_botones.agregarBoton(bot_puesto);
		
		sel_factor.setId("sel_factor");
		sel_factor.setSeleccionTabla("gth_factor_valoracion","ide_gtfav","detalle_gtfav");
		sel_factor.setTitle("FACTOR VALORACIÒN");
		sel_factor.getBot_aceptar().setMetodo("aceptarPuesto");
		sel_factor.setRadio();
		agregarComponente(sel_factor);
		
		sel_valora.setId("sel_valora");
		sel_valora.setSeleccionTabla("gth_valora_grupo","ide_gtvag","detalle_gtvag,puntos_gtvag");
		sel_valora.setTitle("VALORACIÒN GRUPO");
		sel_valora.getBot_aceptar().setMetodo("aceptarPuesto");
		sel_valora.setRadio();
		agregarComponente(sel_valora);
		
		
	}
	
	///pas q me suba el valor de rmu
	public void rmu(AjaxBehaviorEvent evt){
		tab_descripcion.modificar(evt);
		TablaGenerica tab_rmu=utilitario.consultar("select ide_gegro, rmu_gegro from gen_grupo_ocupacional where ide_gegro="+tab_descripcion.getValor("ide_gegro"));
		System.out.println("imprimir consulta nrmu "+tab_rmu.getSql());
		tab_descripcion.setValor("rmu_gtdva", tab_rmu.getValor("rmu_gegro"));
		utilitario.addUpdateTabla(tab_descripcion, "rmu_gtdva", "");
	}
	
	/// ventana factor valoraciòn
	public void importarPuesto(){
		sel_factor.getTab_seleccion().setSql("select ide_gtfav,detalle_gtfav from gth_factor_valoracion order by detalle_gtfav");
		sel_factor.getTab_seleccion().ejecutarSql();
		sel_factor.dibujar();
	}
	String str_seleccionado="";
	public void aceptarPuesto(){
		if(sel_factor.isVisible()){
			if(sel_factor.getValorSeleccionado()!=null){
				//tab_detalle.insertar();
				str_seleccionado=sel_factor.getValorSeleccionado();
				sel_valora.getTab_seleccion().setSql("select ide_gtvag,detalle_gtvag,puntos_gtvag from gth_valora_grupo order by detalle_gtvag");
				sel_valora.getTab_seleccion().ejecutarSql();
				sel_valora.dibujar();
				sel_factor.cerrar();
			}
			 else {
                 utilitario.agregarMensajeInfo("Seleccione un registro","");
             }	
		}
		else if (sel_valora.isVisible()){
			 str_seleccionado=sel_valora.getValorSeleccionado();
			TablaGenerica tab_puesto=utilitario.consultar("select ide_gtvag,detalle_gtvag,puntos_gtvag from gth_valora_grupo where ide_gtvag="+str_seleccionado);
			System.out.println("imprimir consulta puesti"+ tab_puesto.getSql());
			if(sel_valora.getValorSeleccionado()!=null){
				tab_detalle.insertar();
				tab_detalle.setValor("ide_gtvag", str_seleccionado);
				tab_detalle.setValor("puntos_gtdev", tab_puesto.getValor("puntos_gtvag"));
				
			}
			sel_valora.cerrar();
			utilitario.addUpdate("tab_detalle");
		}
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		
		if(tab_ficha.isFocus()){
			tab_ficha.insertar();
			tab_ficha.setValor("ide_gtemp",ide_gtempxx );
			utilitario.addUpdate("tab_ficha");
			
		}
		else if(tab_detalle.isFocus()){
			utilitario.agregarMensajeInfo("No se puede insertar", "Buscar valoraciòn puestos");
			//tab_detalle.insertar();
			
		}
		else if (tab_descripcion.isFocus()){
			tab_descripcion.insertar();
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_ficha.guardar()){
			if(tab_detalle.guardar()){
				if(tab_descripcion.guardar()){
					guardarPantalla();
					
				}
			}
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
	}


	public Tabla getTab_ficha() {
		return tab_ficha;
	}


	public void setTab_ficha(Tabla tab_ficha) {
		this.tab_ficha = tab_ficha;
	}


	public Tabla getTab_detalle() {
		return tab_detalle;
	}


	public void setTab_detalle(Tabla tab_detalle) {
		this.tab_detalle = tab_detalle;
	}


	public Tabla getTab_descripcion() {
		return tab_descripcion;
	}


	public void setTab_descripcion(Tabla tab_descripcion) {
		this.tab_descripcion = tab_descripcion;
	}

	public SeleccionTabla getSel_factor() {
		return sel_factor;
	}

	public void setSel_factor(SeleccionTabla sel_factor) {
		this.sel_factor = sel_factor;
	}

	public SeleccionTabla getSel_valora() {
		return sel_valora;
	}

	public void setSel_valora(SeleccionTabla sel_valora) {
		this.sel_valora = sel_valora;
	}
	
	

}
