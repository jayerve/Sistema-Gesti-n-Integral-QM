package paq_precontractual;

import java.util.ArrayList;
import java.util.List;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

public class precon_etapa extends Pantalla{
	private Tabla tab_etapa=new Tabla();
	private Tabla tab_etapa_requisito=new Tabla();
	
	public precon_etapa()
	{
		
		tab_etapa.setId("tab_etapa");
		tab_etapa.setHeader("ACTIVIDAD");
		tab_etapa.setTabla("precon_etapa","ide_preta",1);
		
		//tab_etapa.setTipoFormulario(true);  //formulario 
		//tab_etapa.getGrid().setColumns(2); //hacer  columnas
		//tab_etapa.getColumna("ide_preta");
		
		tab_etapa.getColumna("ide_preta").setNombreVisual("Código");
		//tab_etapa.getColumna("descripcion_preta");
		tab_etapa.getColumna("descripcion_preta").setNombreVisual("Nombre");
		//tab_etapa.getColumna("descripcion_preta").setFiltroContenido();
		//tab_etapa.getColumna("descripcion_preta").setComentario("Filtro por contenido");
		List lista = new ArrayList();
	    Object fila1[] = {
	           "SI", "SI"
	    };
	    Object fila2[] = {
	         "NO", "NO"
	    };
	    lista.add(fila1);
	    lista.add(fila2);
	    tab_etapa.getColumna("iniciosegumiento_preta").setRadio(lista, "2");
		tab_etapa.getColumna("iniciosegumiento_preta").setNombreVisual("Inicio Actividad");
		tab_etapa.getColumna("iniciosegumiento_preta").setValorDefecto("NO");
		tab_etapa.getColumna("iniciosegumiento_preta").setOrden(3);
		tab_etapa.getColumna("finseguimiento_preta").setRadio(lista, "2");
		tab_etapa.getColumna("finseguimiento_preta").setNombreVisual("Fin Actividad");
		tab_etapa.getColumna("finseguimiento_preta").setValorDefecto("NO");
		tab_etapa.getColumna("finseguimiento_preta").setOrden(4);
		tab_etapa.getColumna("requiere_aprobacion_preta").setOrden(5);
		tab_etapa.getColumna("bien_servicio_preta").setNombreVisual("Bienes y Servicios");
		tab_etapa.getColumna("bien_servicio_preta").setCombo(utilitario.getListaTipoProcesoContracion(false));
		tab_etapa.getColumna("bien_servicio_preta").setOrden(6);
		tab_etapa.getColumna("activo_preta").setNombreVisual("ACTIVO");
		tab_etapa.getColumna("activo_preta").setValorDefecto("true");
		tab_etapa.getColumna("activo_preta").setOrden(7);
		tab_etapa.getColumna("plantilla_preta").setOrden(8);
		tab_etapa.getColumna("plantilla_preta").setNombreVisual("PLANTILLA");
		tab_etapa.getColumna("plantilla_preta").setUpload("contrataciones_plantillas");
		
		tab_etapa.agregarRelacion(tab_etapa_requisito);
		//tab_etapa.setRows(10);
		//tab_etapa.setLectura(true);
		tab_etapa.dibujar();
		
        PanelTabla pat_etapa=new PanelTabla();
        pat_etapa.setPanelTabla(tab_etapa);
        
        Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
		/////asignacion empleados
		tab_etapa_requisito.setId("tab_etapa_requisito");
		tab_etapa_requisito.setIdCompleto("tab_tabulador:tab_etapa_requisito");
		tab_etapa_requisito.setTabla("precon_requisito", "ide_prreq", 2);
		tab_etapa_requisito.getColumna("ide_prreq");
		tab_etapa_requisito.getColumna("ide_prreq").setNombreVisual("Código");
		tab_etapa_requisito.getColumna("descripcion_prreq");
		tab_etapa_requisito.getColumna("descripcion_prreq").setNombreVisual("Nombre");
		tab_etapa_requisito.getColumna("descripcion_prreq").setRequerida(true);
		tab_etapa_requisito.getColumna("activo_prreq").setValorDefecto("true");
		tab_etapa_requisito.getColumna("activo_prreq").setNombreVisual("ACTIVO");
		
		tab_etapa_requisito.dibujar();
		PanelTabla pat_etapa_requisito =new PanelTabla();
		pat_etapa_requisito.setPanelTabla(tab_etapa_requisito);

		tab_Tabulador.agregarTab("REQUISITOS", pat_etapa_requisito);
        
		Division div_division = new Division();
		div_division.dividir2(pat_etapa,tab_Tabulador,"70%","h");
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_etapa.isFocus()){
			utilitario.getTablaisFocus().insertar();
		}else if(tab_etapa_requisito.isFocus()){
			tab_etapa_requisito.insertar();
		}
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_etapa.guardar()){
			tab_etapa_requisito.guardar();
		}
		guardarPantalla();
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_etapa() {
		return tab_etapa;
	}

	public void setTab_etapa(Tabla tab_etapa) {
		this.tab_etapa = tab_etapa;
	}

	public Tabla getTab_etapa_requisito() {
		return tab_etapa_requisito;
	}

	public void setTab_etapa_requisito(Tabla tab_etapa_requisito) {
		this.tab_etapa_requisito = tab_etapa_requisito;
	}
}
