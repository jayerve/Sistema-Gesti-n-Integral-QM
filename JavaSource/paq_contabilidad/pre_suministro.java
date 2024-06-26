package paq_contabilidad;

import javax.ejb.EJB;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_suministro extends Pantalla{

	private Tabla tab_suministro=new Tabla();
	private Tabla tab_servicio_suministrio=new Tabla();
	private Tabla tab_servicios_basicos=new Tabla();
	
	private Combo com_servicios_basicos=new Combo();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	public pre_suministro() {  
						
		com_servicios_basicos.setCombo("select ide_coseb,detalle_coseb from cont_servicio_basico order by detalle_coseb");
		com_servicios_basicos.setMetodo("seleccionaServicioBasico");
		bar_botones.agregarComponente(new Etiqueta("Servicio Basico:"));
		bar_botones.agregarComponente(com_servicios_basicos);		
				
		
		tab_suministro.setId("tab_suministro");
		tab_suministro.setHeader("SUMINISTRO");
		//tab_suministro.setNumeroTabla(1);
		tab_suministro.setTabla("cont_suministro", "ide_cosum",1);
		tab_suministro.getColumna("ide_coseb").setVisible(false);
		tab_suministro.getColumna("ide_colus").setCombo("cont_lugar_suministro", "ide_colus", "detalle_colus,direccion_colus,telefono_colus", "");
		tab_suministro.setTipoFormulario(true);
		tab_suministro.getGrid().setColumns(4);
		tab_suministro.setCondicion("ide_coseb=-1");
		tab_suministro.agregarRelacion(tab_servicio_suministrio);
		tab_suministro.dibujar();
		PanelTabla pat_suministro=new PanelTabla();
		pat_suministro.setPanelTabla(tab_suministro);
		
		
		
		
		//tabla 2
		tab_servicio_suministrio.setId("tab_servicio_suministrio");
		tab_servicio_suministrio.setHeader("REGISTRO DE FACTURA SUMINISTRO");
		tab_servicio_suministrio.setTabla("cont_servicio_suministro","ide_coses",2);
		tab_servicio_suministrio.setCampoForanea("ide_cosum");
		tab_servicio_suministrio.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_servicio_suministrio.getColumna("factura_coses").setUpload("contabilidad");
		tab_servicio_suministrio.setTipoFormulario(true);
		tab_servicio_suministrio.getGrid().setColumns(4);
		tab_servicio_suministrio.dibujar();
		PanelTabla pat_servicio_suministrio=new PanelTabla();
		pat_servicio_suministrio.setPanelTabla(tab_servicio_suministrio);
		
		Division div_division=new Division();
		div_division.dividir2(pat_suministro,pat_servicio_suministrio, "50%", "h");
		agregarComponente(div_division);
		
		
			}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (com_servicios_basicos.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe Seleccionar un Servicio Básico");
			return;
		}
		if(tab_suministro.isFocus()){
			tab_suministro.insertar();
			tab_suministro.setValor("ide_coseb", com_servicios_basicos.getValue()+"");
			}
		    else if(tab_servicio_suministrio.isFocus()){
			tab_servicio_suministrio.insertar();
			
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_suministro.guardar()){
			tab_servicio_suministrio.guardar();
		
		}
		
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_suministro.isFocus()){
			tab_suministro.eliminar();
			
			
	}
		else if(tab_servicio_suministrio.isFocus()){
			tab_servicio_suministrio.eliminar();

			
		}
	}

	public void seleccionaServicioBasico(){
		if(com_servicios_basicos.getValue()!=null){
			tab_suministro.setCondicion("ide_coseb="+com_servicios_basicos.getValue());
			tab_suministro.ejecutarSql();
			tab_servicio_suministrio.ejecutarValorForanea(tab_suministro.getValorSeleccionado());
		}
		else {
			tab_suministro.setCondicion("ide_coseb=-1");
			tab_suministro.ejecutarSql();
			tab_servicio_suministrio.ejecutarValorForanea(tab_suministro.getValorSeleccionado());
	
		}
	}
	
	public Tabla getTab_suministro() {
		return tab_suministro;
	}

	public void setTab_suministro(Tabla tab_suministro) {
		this.tab_suministro = tab_suministro;
	}

	public Tabla getTab_servicio_suministrio() {
		return tab_servicio_suministrio;
	}

	public void setTab_servicio_suministrio(Tabla tab_servicio_suministrio) {
		this.tab_servicio_suministrio = tab_servicio_suministrio;
	}

	public Combo getCom_servicios_basicos() {
		return com_servicios_basicos;
	}

	public void setCom_servicios_basicos(Combo com_servicios_basicos) {
		this.com_servicios_basicos = com_servicios_basicos;
	}

	public Tabla getTab_servicios_basicos() {
		return tab_servicios_basicos;
	}

	public void setTab_servicios_basicos(Tabla tab_servicios_basicos) {
		this.tab_servicios_basicos = tab_servicios_basicos;
	}


}
