/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_mantenimiento;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_mantenimiento_equipo_hardware extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_mantenimiento_equipo_hardware() {
    	
    	tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("mto_equipo_hardware", "ide_mteqh", 1);
		tab_tabla.getColumna("ACTIVO_mteqh").setCheck();
		tab_tabla.getColumna("ACTIVO_mteqh").setValorDefecto("true");
		
		tab_tabla.getColumna("ide_equip").setCombo("SELECT ide_mtequ, est.nombre_mtest, equip.ide_afact, equip.marca_mtequ, equip.memoria_mtequ, "
				+ "equip.modelo_mtequ, equip.nombre_mtequ, equip.observacion_mtequ, equip.procesador_mtequ, "
				+ "equip.serie_mtequ, tesq.nombre_mtteq,tesq.descripcion_mtteq "
				+ "FROM mto_equipo  equip "
				+ "left join mto_estado est  on est.ide_mtest=equip.ide_estado "
				+ "left join mto_tipo_equipo tesq  on tesq.ide_mtteq=equip.ide_tipequ");
		tab_tabla.getColumna("ide_equip").setAutoCompletar();
    
		tab_tabla.getColumna("ide_hdware").setCombo("SELECT hard.ide_mthar,hard.nombre_mthar, thar.nombre_mttha, thar.descripcion_mttha "
				+ "FROM mto_hardware hard "
				+ "left join mto_tipo_hardware thar on thar.ide_mttha=hard.ide_tiphar");
		tab_tabla.getColumna("ide_hdware").setAutoCompletar();
    
		
		tab_tabla.getColumna("estado_mteqh").setCombo("	SELECT ide_mtest,nombre_mtest FROM mto_estado");
		tab_tabla.getColumna("estado_mteqh").setAutoCompletar();
	
		tab_tabla.getColumna("ide_afact").setCombo("SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa");
		tab_tabla.getColumna("ide_afact").setAutoCompletar();

	

		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	
       /* tab_tabla.setId("tab_tabla");
        tab_tabla.setNumeroTabla(1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);*/
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}
