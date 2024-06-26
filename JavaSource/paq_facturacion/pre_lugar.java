package paq_facturacion;

import org.primefaces.component.tabview.Tab;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

public class pre_lugar extends Pantalla{
	private Tabla tab_lugar=new Tabla();
	private Tabla tab_usuario_lugar=new Tabla();
	private Tabla tab_venta_lugar=new Tabla();
	
	public pre_lugar (){
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_lugar.setId("tab_lugar");
		tab_lugar.setHeader("Lugar");
		tab_lugar.setTabla("fac_lugar", "ide_falug", 1);
		tab_lugar.agregarRelacion(tab_usuario_lugar);
		tab_lugar.agregarRelacion(tab_venta_lugar);
		tab_lugar.dibujar();
		PanelTabla pat_lugar=new PanelTabla();
		pat_lugar.setPanelTabla(tab_lugar);
		
		
		
		tab_usuario_lugar.setId("tab_usuario_lugar");
		tab_usuario_lugar.setHeader("Usuario Lugar");
		tab_usuario_lugar.setIdCompleto("tab_tabulador:tab_usuario_lugar");
		tab_usuario_lugar.setTabla("fac_usuario_lugar", "ide_fausl", 2);
		tab_usuario_lugar.getColumna("ide_usua").setCombo("select ide_usua,nom_usua,nick_usua from sis_usuario order by nom_usua");
		tab_usuario_lugar.getColumna("ide_tecaj").setCombo("tes_caja","ide_tecaj","detalle_tecaj","");
		tab_usuario_lugar.getColumna("ide_usua").setUnico(true);
		tab_usuario_lugar.getColumna("ide_falug").setUnico(true);
		tab_usuario_lugar.dibujar();
		PanelTabla pat_usuario_lugar=new PanelTabla();
		pat_usuario_lugar.setPanelTabla(tab_usuario_lugar);

		// venta lugar
		tab_venta_lugar.setId("tab_venta_lugar");
		tab_venta_lugar.setHeader("Venta Lugar");
		tab_venta_lugar.setIdCompleto("tab_tabulador:tab_venta_lugar");
		tab_venta_lugar.setTabla("fac_venta_lugar", "ide_favel", 3);
		tab_venta_lugar.getColumna("ide_bogrm").setCombo("select ide_bogrm,detalle_bogrm,autorizacion_sri_bogrm from bodt_grupo_material order by detalle_bogrm");
		//tab_venta_lugar.setCampoForanea("ide_falug");
		tab_venta_lugar.dibujar();
		PanelTabla pat_venta=new PanelTabla();
		pat_venta.setPanelTabla(tab_venta_lugar);
		
		tab_tabulador.agregarTab("USUARIO LUGAR", pat_usuario_lugar);//intancia los tabuladores 
		tab_tabulador.agregarTab("VENTA LUGAR",pat_venta);
		
	//division2
		
		Division div_lugar=new Division();
        div_lugar.setId("div_lugar");
		div_lugar.dividir2(pat_lugar,tab_tabulador,"50%","H");
		agregarComponente(div_lugar);
		
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_lugar.guardar()) {
			if (tab_usuario_lugar.guardar()){
				tab_venta_lugar.guardar();
			}
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}


	public Tabla getTab_lugar() {
		return tab_lugar;
	}


	public void setTab_lugar(Tabla tab_lugar) {
		this.tab_lugar = tab_lugar;
	}


	public Tabla getTab_usuario_lugar() {
		return tab_usuario_lugar;
	}


	public void setTab_usuario_lugar(Tabla tab_usuario_lugar) {
		this.tab_usuario_lugar = tab_usuario_lugar;
	}


	public Tabla getTab_venta_lugar() {
		return tab_venta_lugar;
	}


	public void setTab_venta_lugar(Tabla tab_venta_lugar) {
		this.tab_venta_lugar = tab_venta_lugar;
	}


	}
