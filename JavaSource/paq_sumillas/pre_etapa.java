package paq_sumillas;

import javax.ejb.EJB;

import org.primefaces.component.panelmenu.PanelMenu;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioDestinatario;
import paq_sumillas.ejb.ServicioGerenciaCoordinacion;
import framework.componentes.Division;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_etapa extends Pantalla {

	private Tabla tab_etapa = new Tabla();
	private Tabla tab_etapa_usuario = new Tabla();
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
	
	private Division div_division = new Division();
	
	@EJB
	private ServicioGerenciaCoordinacion ser_gercoo = (ServicioGerenciaCoordinacion ) utilitario.instanciarEJB(ServicioGerenciaCoordinacion.class);
	
	public pre_etapa() {
		
			
		tab_etapa.setId("tab_etapa");
		tab_etapa.setHeader("ETAPAS DEL PROCESO");
		tab_etapa.setTabla("sum_etapa", "ide_sumet", 1);
		
		//tab_etapa.setTipoFormulario(true);
		
		// formulario
		//tab_etapa.getGrid().setColumns(2);
		// hacer columnas
		tab_etapa.getColumna("ide_sumet").setNombreVisual("Código");
		tab_etapa.getColumna("ide_sumet").setOrden(1);
		
		tab_etapa.getColumna("nombre_sumet").setNombreVisual("Nombre Etapa");
		tab_etapa.getColumna("nombre_sumet").setOrden(2);
		
		tab_etapa.getColumna("activo_sumet").setNombreVisual("Activo");
		tab_etapa.getColumna("activo_sumet").setValorDefecto("true");
		tab_etapa.getColumna("activo_sumet").setOrden(3);
		
		tab_etapa.agregarRelacion(tab_etapa_usuario);
		tab_etapa.dibujar();
		
		dibujarRespuestas();
		
		PanelTabla pat_dat_gen=new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_etapa); 
		PanelTabla pat_ing_res=new PanelTabla();  
		pat_ing_res.setPanelTabla(tab_etapa_usuario); 
		
		Division div_division = new Division();  
		div_division.dividir2(pat_dat_gen, pat_ing_res, "30%", "H"); 
		agregarComponente(div_division); 
	}
	
	
	public void dibujarRespuestas()
	{
		tab_etapa_usuario.setId("tab_etapa_usuario");
		tab_etapa_usuario.setHeader("USUARIO A NOTIFICAR");
		tab_etapa_usuario.setTabla("sum_etapa_usuario", "ide_sumeu", 2);
		tab_etapa_usuario.setTipoFormulario(true);		
		// formulario
		tab_etapa_usuario.getGrid().setColumns(4);
		
		tab_etapa_usuario.getColumna("ide_sumeu");
		tab_etapa_usuario.getColumna("ide_sumeu").setNombreVisual("Código");
		tab_etapa_usuario.getColumna("ide_sumeu").setOrden(1);
		
		tab_etapa_usuario.getColumna("ide_usua").setNombreVisual("Usuario");
		tab_etapa_usuario.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
		tab_etapa_usuario.getColumna("ide_usua").setOrden(2);
		
		tab_etapa_usuario.getColumna("ide_geareg").setNombreVisual("Gerencia");
		tab_etapa_usuario.getColumna("ide_geareg").setCombo(ser_gercoo.getGerencia());
		tab_etapa_usuario.getColumna("ide_geareg").setMetodoChange("seleccionaCoordinacion");
		tab_etapa_usuario.getColumna("ide_geareg").setOrden(7);
		
		tab_etapa_usuario.getColumna("ide_gearec").setNombreVisual("Coordinación");
		tab_etapa_usuario.getColumna("ide_gearec").setCombo(ser_gercoo.getCoordinacionesTotas());
		tab_etapa_usuario.getColumna("ide_gearec").setOrden(8);
		
		tab_etapa_usuario.getColumna("activo_sumeu").setNombreVisual("Activo");
		tab_etapa_usuario.getColumna("activo_sumeu").setOrden(3);
		
		tab_etapa_usuario.setCampoForanea("ide_sumet");
		tab_etapa_usuario.dibujar();
	}
	
	public String seleccionaCoordinacion() {
		String sqlcoo = "";
		if (tab_etapa_usuario.getValor("ide_geareg")!= null) {
			sqlcoo = ser_gercoo.getCoordinacion(Integer.parseInt(tab_etapa_usuario.getValor("ide_geareg").toString()));
			tab_etapa_usuario.getColumna("ide_gearec").setCombo(sqlcoo);
			utilitario.addUpdateTabla(tab_etapa_usuario, "ide_gearec", "");
		} else {
			sqlcoo = ser_gercoo.getVacio();
		}
		return sqlcoo;
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_etapa.guardar()){   
			guardarPantalla();  
		} 
		if(tab_etapa_usuario.guardar()){
			guardarPantalla();
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	
	public PanelMenu getPam_menu() {
		return pam_menu;
	}

	public void setPam_menu(PanelMenu pam_menu) {
		this.pam_menu = pam_menu;
	}

	public Panel getPan_opcion() {
		return pan_opcion;
	}

	public void setPan_opcion(Panel pan_opcion) {
		this.pan_opcion = pan_opcion;
	}

	public String getStr_opcion() {
		return str_opcion;
	}

	public void setStr_opcion(String str_opcion) {
		this.str_opcion = str_opcion;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public Tabla gettab_etapa_usuario() {
		return tab_etapa_usuario;
	}

	public void settab_etapa_usuario(Tabla tab_etapa_usuario) {
		this.tab_etapa_usuario = tab_etapa_usuario;
	}

	public Tabla getTab_etapa() {
		return tab_etapa;
	}

	public void setTab_etapa(Tabla tab_etapa) {
		this.tab_etapa = tab_etapa;
	}

	public Tabla getTab_etapa_usuario() {
		return tab_etapa_usuario;
	}

	public void setTab_etapa_usuario(Tabla tab_etapa_usuario) {
		this.tab_etapa_usuario = tab_etapa_usuario;
	}
}
