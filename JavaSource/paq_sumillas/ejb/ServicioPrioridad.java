package paq_sumillas.ejb;


import javax.ejb.Stateless;


/**
 * @author 
 *
 */
@Stateless
public class ServicioPrioridad {
	
	public String getPrioridad(Integer idPrioridad){
		String sql_prioridad="SELECT dias_sumpr " +
				"FROM sum_prioridad pr " +
				"WHERE  pr.ide_sumpr = " + idPrioridad;
		return sql_prioridad;
	}
	
}
