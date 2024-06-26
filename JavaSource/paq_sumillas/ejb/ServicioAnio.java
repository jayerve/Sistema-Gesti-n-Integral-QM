package paq_sumillas.ejb;



import javax.ejb.Stateless;

/**
 * @author 
 *
 */
@Stateless
public class ServicioAnio {

	
	public String getAnioDetalle (String activo, String bloqueado ){
		String tab_anio= "select ide_geani,detalle_geani, " +
				" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani," +
				" CASE WHEN bloqueado_geani = true THEN 'Bloqueado' ELSE 'Habilitado' END AS bloqueado_geani" +
				" from gen_anio where activo_geani in("+activo+")" +
				" and bloqueado_geani in ("+bloqueado+")" +
				" order by detalle_geani desc" ;
		return tab_anio;
	}
	
	public String getAnioPoId (Integer anio ){
		String tab_anio= "select detalle_geani " +
				" from gen_anio where ide_geani = "+anio ;
		return tab_anio;
	}

}
