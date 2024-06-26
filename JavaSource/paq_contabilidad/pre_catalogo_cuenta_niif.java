package paq_contabilidad;

import javax.ejb.EJB;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_catalogo_cuenta_niif extends Pantalla{
	
	private Tabla tab_catalogo_cuenta_niif=new Tabla();
	private Division div_division = new Division();	
	private Arbol arb_catalogo_cuenta=new Arbol();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
		
	public pre_catalogo_cuenta_niif(){
		
					        
		tab_catalogo_cuenta_niif.setId("tab_catalogo_cuenta_niif");
		tab_catalogo_cuenta_niif.setHeader("CATALOGO CUENTAS SEGUN NIIFS");
		tab_catalogo_cuenta_niif.setTipoFormulario(true);  //formulario 
		tab_catalogo_cuenta_niif.getGrid().setColumns(4); //hacer  columnas 
		tab_catalogo_cuenta_niif.setTabla("cont_catalogo_niif", "ide_cocan", 1);		

	  	tab_catalogo_cuenta_niif.getColumna("nivel_cocan").setCombo(utilitario.getListaGruposNivelCuenta());
	  	tab_catalogo_cuenta_niif.getColumna("grupo_nivel_cocan").setVisible(false);
	  	tab_catalogo_cuenta_niif.getColumna("activo_cocan").setValorDefecto("true");

        tab_catalogo_cuenta_niif.setCampoPadre("con_ide_cocan"); //necesarios para el arbol ide recursivo
		tab_catalogo_cuenta_niif.setCampoNombre("(select coalesce(cat_codigo_cocan,'') ||' '|| coalesce(descripcion_cocan,'') as descripcion_cocan from cont_catalogo_niif b where b.ide_cocan=cont_catalogo_niif.ide_cocan)"); //necesarios para el arbol campo a mostrarse
		tab_catalogo_cuenta_niif.agregarArbol(arb_catalogo_cuenta);//necesarios para el arbol
		
		tab_catalogo_cuenta_niif.dibujar();
		PanelTabla pat_tipo_catalogo_cuenta = new PanelTabla();
		pat_tipo_catalogo_cuenta.setPanelTabla(tab_catalogo_cuenta_niif);
		
		
		//arbol				

		arb_catalogo_cuenta.setId("arb_catalogo_cuenta");
		arb_catalogo_cuenta.dibujar();
		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_catalogo_cuenta,pat_tipo_catalogo_cuenta,"25%","v");
		agregarComponente(div_division);
		
       		
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();

		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_catalogo_cuenta_niif.guardar()){
        	guardarPantalla();
            //actualiza el arbol
            arb_catalogo_cuenta.ejecutarSql();
            utilitario.addUpdate("arb_arbol");
			   
           }
    				
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
		
	}


	public Arbol getArb_catalogo_cuenta() {
		return arb_catalogo_cuenta;
	}
	public void setArb_catalogo_cuenta(Arbol arb_catalogo_cuenta) {
		this.arb_catalogo_cuenta = arb_catalogo_cuenta;
	}
	public Tabla getTab_catalogo_cuenta_niif() {
		return tab_catalogo_cuenta_niif;
	}
	public void setTab_catalogo_cuenta_niif(Tabla tab_catalogo_cuenta_niif) {
		this.tab_catalogo_cuenta_niif = tab_catalogo_cuenta_niif;
	}
	
	

}
