package paq_asistencia.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.component.tabview.Tab;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;


import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioControl {
	private Utilitario utilitario=new Utilitario();

	private TablaGenerica tab_valida_asistencia;






	/**
	 * Consulta los empleados a los cuales se les va a realizar el control del biometrico,
	 *  excluye solo empleados que no realizan control de asistencia y aquellos que se encuentren en período de vacaciones
	 * @param fecha  fecha de conulta YYYY-MM-DD
	 * @return
	 */
	public TablaGenerica getEmpleadosControlAsistencia(String fecha){
		TablaGenerica tab_emple=utilitario.consultar("select  ide_geedp,TARJETA_MARCACION_GTEMP,fecha_evento_cobim,TO_CHAR(fecha_evento_cobim ,'YYYY-MM-DD') AS FECHA_MARCACION, TO_CHAR(fecha_evento_cobim ,'HH24:MM:SS') AS HORA_MARCACION, EVENTO_RELOJ_COBIM,IDE_SUCU,IDE_GTGRE " +
				"	from ( select ide_geedp,documento_identidad_gtemp,TARJETA_MARCACION_GTEMP,IDE_SUCU,IDE_GTGRE " +
				"	from  gth_empleado a, gen_empleados_departamento_par b where a.ide_gtemp = b.ide_gtemp and activo_geedp=true " +
				"	and not ide_geedp in ( 	select ide_geedp from asi_permisos_vacacion_hext where to_date('"+utilitario.getFormatoFecha(fecha)+"','yyyy-mm-dd') between fecha_desde_aspvh and fecha_hasta_aspvh and ide_geest=1 ) " +
				"and control_asiStencia_geedp=true ) a 	left join con_biometrico_marcaciones b on a.TARJETA_MARCACION_GTEMP =RTRIM( b.ide_persona_cobim) " +
				"		and to_char(b.fecha_evento_cobim,'yyyy-mm-dd') = '"+utilitario.getFormatoFecha(fecha)+"' 	order by ide_geedp,EVENTO_RELOJ_COBIM,fecha_evento_cobim");		
		System.out.println("imprimo tabla generia getEmpleadosControlAsistencia ");
		tab_emple.imprimirSql();
		
		return tab_emple;
	}


	/**Busca todos los eventos configurados para dispositivos de control de asistencia
	 * @return
	 */
	public TablaGenerica getEventosReloj(){
		return utilitario.consultar("select * from CON_RELOJ_EVENTO order by IDE_SUCU,ALTERNO_COREE");
	}

	private String[] getEvento(String alternoEvento, TablaGenerica tab_eventos,String ide_sucu){
		for (int i=0;i<tab_eventos.getTotalFilas();i++){
			if(tab_eventos.getValor(i, "IDE_SUCU").equals(ide_sucu)){				
				if(tab_eventos.getValor(i, "ALTERNO_COREE").equalsIgnoreCase(alternoEvento)){
					//Encontro configuración de Evento
					try {
						if(tab_eventos.getValor(i, "ALMUERZO_COREE").equals("true")|| tab_eventos.getValor(i, "ALMUERZO_COREE").equals("1")){
							return new String[]{tab_eventos.getValor(i, "DETALLE_COREE")+" - "+alternoEvento,"2"};
						}	
						else if(tab_eventos.getValor(i, "ENTRADA_COREE").equals("true")|| tab_eventos.getValor(i, "ENTRADA_COREE").equals("1")){
							return new String[]{tab_eventos.getValor(i, "DETALLE_COREE")+" - "+alternoEvento,"1"};
						}
					} catch (Exception e) {
						// TODO: handle exception
					}


				}
			}
		}		
		return new String[]{"EVENTO NO CONFIGURADO "+alternoEvento,null};
	}


	/**
	 * Busca todos los tipos de horarios, incluye grupo de empleado, sirve para obtener la tolerancia del horario
	 * @return
	 */
	public TablaGenerica getHorariosGrupoEmpleado(){
		String p_asi_hora_normal="3";

		TablaGenerica tab_horario =utilitario.consultar("SELECT DH.IDE_ASHOR,DH.IDE_GEDIA,HO.IDE_ASGRI,HO.IDE_SUCU,TO_CHAR(HO.HORA_INICIAL_ASHOR ,'HH24:MM:SS') AS HORA_INICIO,TO_char(HO.HORA_FINAL_ASHOR,'HH24:MM:SS') AS HORA_FIN,TUR.IDE_GTGRE,TUR.MINUTO_TOLERANCIA_ASTUR,minuto_almuerzo_gtgre " +
				"from ASI_DIA_HORARIO dh " +
				"INNER JOIN ASI_HORARIO ho on DH.IDE_ASHOR=HO.IDE_ASHOR " +
				"INNER JOIN ASI_TURNOS TUR on DH.IDE_ASHOR=TUR.IDE_ASHOR " +
				"inner join GTH_GRUPO_EMPLEADO GRU on GRU.IDE_GTGRE= TUR.IDE_GTGRE " +
				"WHERE IDE_ASGRI="+p_asi_hora_normal+" " +
				"ORDER BY IDE_SUCU,IDE_GTGRE,HO.IDE_ASHOR,IDE_GEDIA "); 
		return tab_horario;	
	}


	private void buscaHorarioMarcacion(TablaGenerica tab_horariosGrupo,List<String[]> lis_marcaciones,String fecha){
		int int_num_dia=utilitario.getNumeroDiasSemana(utilitario.getFecha(fecha));		
		//1) Busco en la tabla de horariosGrupo+

		System.out.println("horarios "+tab_horariosGrupo.getTotalFilas()+" lis_marcaciones.size()  "+ lis_marcaciones.size() );
		for(int indice_marcacion=0;indice_marcacion<lis_marcaciones.size();indice_marcacion++){			
			String[] str_fila=lis_marcaciones.get(indice_marcacion);
			System.out.println("entre a horarios marcaciones ");
			System.out.println("str_fila[1] " +str_fila[1]); 
			System.out.println("str_fila[2] " +str_fila[2]); 
			
			if(str_fila[1]!=null){
				if(tab_horariosGrupo.getTotalFilas()>0){
					//Existio marcacion en horario normal					
					for(int i=0;i<tab_horariosGrupo.getTotalFilas();i++){
						//		try {
						//Busco el horario al que corresponde la marcación por grupo de empleado, sucursal y dia
						if((tab_horariosGrupo.getValor(i, "IDE_GTGRE").equals(str_fila[2]))&&(tab_horariosGrupo.getValor(i, "IDE_SUCU").equals(str_fila[3]))&&(String.valueOf(int_num_dia).equals(tab_horariosGrupo.getValor(i,"IDE_GEDIA")))){
							String str_hora_inicio=tab_horariosGrupo.getValor(i, "HORA_INICIO");
							String str_hora_fin=tab_horariosGrupo.getValor(i, "HORA_FIN");

							System.out.println("HORA INICIO "+str_hora_inicio);
							System.out.println("HORA FIN "+str_hora_fin);
							System.out.println("str_fila[8] "+str_fila[8]);
						
							//Verifico que exista marcación					
							String str_hora_marcoi=	utilitario.getFormatoHora(str_fila[5]);

							if(str_fila[6]!=null){
								String str_hora_marcof=	utilitario.getFormatoHora(str_fila[6]);
								//Exitio inicio y fin de la marcacion								
								System.out.println("ENCONTRO HORARIO MARCACION       "+str_hora_inicio+"     "+str_hora_fin+"  ... vs  ...  "+str_hora_marcoi+"    "+str_hora_marcof+"   ===  "+str_fila[1] +"  ..///// timpo almuerzo ===   "+tab_horariosGrupo.getValor(i,"minuto_almuerzo_gtgre") +"     /////  "+str_fila[8]);								
								//Valida con la entrada (1)
								if(str_fila[8]!=null &&str_fila[8].equals("1")){
									System.out.println("entre al str ");
									// saco la diferencia de las horas
									
									
									double dou_diferencia= utilitario.getDiferenciaHoras(utilitario.getHora(str_hora_inicio), utilitario.getHora(str_hora_marcoi));
									//double dou_diferencia= utilitario.getDiferenciaHoras(utilitario.DeStringAHora(str_hora_inicio), utilitario.DeStringAHora(str_hora_fin));

									System.out.println("difenercia de horario "+dou_diferencia);
									
									tab_valida_asistencia.insertar();
									tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
									tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
									tab_valida_asistencia.setValor("HORA_MARCACION_ASVAA", str_hora_marcoi);
									tab_valida_asistencia.setValor("DIFERENCIA_ASVAA",dou_diferencia +"");
									tab_valida_asistencia.setValor("EVENTO_ASVAA", str_fila[7]);
									tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
									tab_valida_asistencia.setValor("IMPORTO_ASVAA", "1");

									//Aplica la toleracia
									double dou_tolerancia=0;										
									try {
										dou_tolerancia=Double.parseDouble(tab_horariosGrupo.getValor(i,"MINUTO_TOLERANCIA_ASTUR"));
										dou_tolerancia=dou_tolerancia/60;///para sacar en horas
									} catch (Exception e) {
										// TODO: handle exception
									}
									if(dou_diferencia>dou_tolerancia){
										tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
									}
									else{
										tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "false");
									}
									//Salida
									double dou_salida= 0; //utilitario.getDiferenciaHoras(utilitario.getHora(str_hora_fin), utilitario.getHora(str_hora_marcof));
									System.out.println("salida del diferencia "+dou_salida);
									tab_valida_asistencia.setValor("HORA_MARCA_SALIDA_ASVAA", str_hora_marcof);
									tab_valida_asistencia.setValor("DIFERENCIA_SALIDA_ASVAA",dou_salida +"");
									//solo si no tiene novedad en la entrado verifico q no tenga novedad en la salida
									if(tab_valida_asistencia.getValor("NOVEDAD_ASVAA").equals("false")){
										if(dou_salida<0){
											tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
										}
										else{
											if(dou_salida>=1){
												//si tiene  retraso mas 60 min en la salida es novedad
												tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");	
											}
											else{
												tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "false");
											}
										}	
									}
								}												
								else if(str_fila[8]!=null &&str_fila[8].equals("2")){
									//Valida con la salida (2)
									double dou_total_almuerzo=0; //utilitario.getDiferenciaHoras(utilitario.getHora(str_hora_marcoi), utilitario.getHora(str_hora_marcof));
									double dou_tiempo_almuerzo=0;
									try {										
										dou_tiempo_almuerzo=Double.parseDouble(tab_horariosGrupo.getValor(i,"minuto_almuerzo_gtgre"));										
										dou_tiempo_almuerzo=dou_tiempo_almuerzo/60; //Para sacar en horas
										dou_tiempo_almuerzo=dou_total_almuerzo-dou_tiempo_almuerzo;										
									} catch (Exception e) {
										// TODO: handle exception
									}

									tab_valida_asistencia.insertar();
									tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
									tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
									tab_valida_asistencia.setValor("HORA_MARCACION_ASVAA", str_hora_marcoi);
									tab_valida_asistencia.setValor("DIFERENCIA_ASVAA", dou_tiempo_almuerzo+"");
									tab_valida_asistencia.setValor("EVENTO_ASVAA", str_fila[7]);
									tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
									tab_valida_asistencia.setValor("HORA_MARCA_SALIDA_ASVAA", str_hora_marcof);
									tab_valida_asistencia.setValor("IMPORTO_ASVAA", "0");

									if(dou_tiempo_almuerzo>0){
										//se paso del tiempo de almuerzo 
										tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
									}
									else{
										tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "false");
									}
									//System.out.println("DIFERENCIA ALMUERZO =======  "+dou_tiempo_almuerzo);									
								}
								else {
									//Resta los dos tiempos de las marcaciones
									double dou_total_tiempo=0; //utilitario.getDiferenciaHoras(utilitario.getHora(str_hora_marcoi), utilitario.getHora(str_hora_marcof));									
									tab_valida_asistencia.insertar();
									tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
									tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
									tab_valida_asistencia.setValor("HORA_MARCACION_ASVAA", str_hora_marcoi);
									tab_valida_asistencia.setValor("DIFERENCIA_ASVAA", dou_total_tiempo+"");
									tab_valida_asistencia.setValor("EVENTO_ASVAA", str_fila[7]);
									tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
									tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
									tab_valida_asistencia.setValor("HORA_MARCA_SALIDA_ASVAA", str_hora_marcof);
									tab_valida_asistencia.setValor("IMPORTO_ASVAA", "0");
									//	System.out.println("DIFERENCIA MARCACIÓN =======  "+dou_total_tiempo);									
								}
							}
							else{
								//Solo existio una marcación
								//	System.out.println("ENCONTRO UNA SOLA HORARIO MARCACION "+str_hora_inicio+"     "+str_hora_fin+"  ... vs  mmmm...  "+str_hora_marcoi+"   ===  "+str_fila[1]);
								tab_valida_asistencia.insertar();
								tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
								tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
								tab_valida_asistencia.setValor("HORA_MARCACION_ASVAA", str_hora_marcoi);								
								tab_valida_asistencia.setValor("EVENTO_ASVAA", "UNA SOLA MARCACIÓN "+str_fila[7]);
								tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
								tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
								tab_valida_asistencia.setValor("IMPORTO_ASVAA", "0");

							}
							break;
						}

						//								} catch (Exception e) {
						//									// TODO: handle exception
						//									System.out.println("ERROR AL buscaHorarioMarcacion "+e.getMessage());
						//								
						//								}
					}
				}
				else{
					//No encontro configuracion de horarios		
					System.out.println("entre a poner que no ENCONTRO HORARIO ");
					tab_valida_asistencia.insertar();
					tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
					tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
					tab_valida_asistencia.setValor("EVENTO_ASVAA", "NO ENCONTRO HORARIO "+str_fila[7]);
					tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
					tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
					tab_valida_asistencia.setValor("IMPORTO_ASVAA", "0");
				}

			}
			else{
				//System.out.println("======= NO ENCONTRO  MARCACION "+fecha );
				//Accion si no realizo marcación ese dia se registra como novedad , verifico que deba marcar la fecha indicada
				boolean boo_debe_marcar=false;
				for(int i=0;i<tab_horariosGrupo.getTotalFilas();i++){
					//Busco el horario al que corresponde la marcación por grupo de empleado, sucursal y dia
					if((tab_horariosGrupo.getValor(i, "IDE_GTGRE").equals(str_fila[2]))&&(tab_horariosGrupo.getValor(i, "IDE_SUCU").equals(str_fila[3]))&&(String.valueOf(int_num_dia).equals(tab_horariosGrupo.getValor(i,"IDE_GEDIA")))){
						boo_debe_marcar=true;
						break;
					}				
				}
				//Excluir si la fecha es feriado no debe marcar
				////////////				
				if(boo_debe_marcar){
					tab_valida_asistencia.insertar();
					tab_valida_asistencia.setValor("IDE_GEEDP", str_fila[0]);
					tab_valida_asistencia.setValor("FECHA_MARCACION_ASVAA", fecha);
					tab_valida_asistencia.setValor("EVENTO_ASVAA", "NO MARCO");												
					tab_valida_asistencia.setValor("ACTIVO_ASVAA", "true");
					tab_valida_asistencia.setValor("NOVEDAD_ASVAA", "true");
					tab_valida_asistencia.setValor("IMPORTO_ASVAA", "0");
				}								
			}
		}
	}

	public int resumirAsistencia(String fechaInicio, String fechaFin){
		//1) elimino si ya existe resumen de ese dia.. para no repetir ingresados
		eliminarResumen(fechaInicio, fechaFin);
		String fecha=fechaInicio;

		tab_valida_asistencia=new TablaGenerica();
		tab_valida_asistencia.setTabla("ASI_VALIDA_ASISTENCIA", "IDE_ASVAA", -1);
		tab_valida_asistencia.setCondicion("IDE_ASVAA=-1");
		tab_valida_asistencia.getColumna("VERIFICADO_ASVAA").setValorDefecto("true");
		tab_valida_asistencia.ejecutarSql();	
		//Recoore mientras la fecha inicial sea menor que la fecha fin		
		while (fechaFin.equals(fecha) || utilitario.isFechaMayor(utilitario.getFecha(fechaFin), utilitario.getFecha(fecha))){
			TablaGenerica tab_marcaciones=getEmpleadosControlAsistencia(fecha);
			TablaGenerica tab_horariosGrupo=getHorariosGrupoEmpleado();			
			TablaGenerica tab_eventos=getEventosReloj();

			List<String[]> lis_marca_empleados=new ArrayList<String[]>();
			String str_ide_geedp_actual="";

			for (int i = 0; i < tab_marcaciones.getTotalFilas(); i++) {
				if(i==0){
					str_ide_geedp_actual=tab_marcaciones.getValor(i, "IDE_GEEDP");
					System.out.println("pregunto str_ide_geedp_actual "+i+"  espacio "+str_ide_geedp_actual);

				}
				System.out.println("pregunto lis_marca_empleados "+i+" espacio 2 "+lis_marca_empleados);

				if(!str_ide_geedp_actual.equals(tab_marcaciones.getValor(i, "IDE_GEEDP"))){					
					//Es otro empleado Analizar Asitencia
					System.out.println("primer ingreso str_ide_geedp_actual "+str_ide_geedp_actual+" IDE_GEEDP " +tab_marcaciones.getValor(i, "IDE_GEEDP")+" lis_marca_empleados "+lis_marca_empleados);
					buscaHorarioMarcacion(tab_horariosGrupo, lis_marca_empleados, fecha);
					lis_marca_empleados.clear();
					str_ide_geedp_actual=tab_marcaciones.getValor(i, "IDE_GEEDP");
				}

				if(str_ide_geedp_actual.equals(tab_marcaciones.getValor(i, "IDE_GEEDP"))){
					String str_evento=tab_marcaciones.getValor(i, "EVENTO_RELOJ_COBIM");
					if(str_evento!=null){
						//Existio marcación	
						int int_band=getNumeroFilaEmpleado(lis_marca_empleados, str_evento);
						if(int_band==-1){ 
							//Creo una fila correspondiete a la marcación				
							String[] str_fila=new String[9];
							str_fila[0]=tab_marcaciones.getValor(i, "IDE_GEEDP");
							str_fila[1]=str_evento;
							str_fila[2]=tab_marcaciones.getValor(i, "IDE_GTGRE");
							str_fila[3]=tab_marcaciones.getValor(i, "IDE_SUCU");
							str_fila[4]=tab_marcaciones.getValor(i, "fecha_evento_cobim");
							str_fila[5]=tab_marcaciones.getValor(i, "HORA_MARCACION");
							str_fila[6]=null;// HORA FIN
							String[] str_tipo_evento=getEvento(str_evento, tab_eventos, tab_marcaciones.getValor(i, "IDE_SUCU"));							
							str_fila[7]=str_tipo_evento[0];
							str_fila[8]=str_tipo_evento[1];// GUARDA EL TIPO DE EVENTO  1=ASISTENCIA, 2=ALMUERZO null si no encontro evento asociado a la marcación 

							lis_marca_empleados.add(str_fila);					
						}
						else{
							String[] str_fila=lis_marca_empleados.get(int_band);
							str_fila[6]=tab_marcaciones.getValor(i, "HORA_MARCACION");
							lis_marca_empleados.set(int_band, str_fila);
						}
					}
					else{
						//EL EMPLEADO NO MARCO
						//Creo una fila correspondiete a la marcación				
						String[] str_fila=new String[7];
						str_fila[0]=tab_marcaciones.getValor(i, "IDE_GEEDP");
						str_fila[1]=null;
						str_fila[2]=tab_marcaciones.getValor(i, "IDE_GTGRE");
						str_fila[3]=tab_marcaciones.getValor(i, "IDE_SUCU");
						str_fila[4]=fecha;
						lis_marca_empleados.add(str_fila);					
					}

				}
			}
			///precesa el ultimo 
			System.out.println("segundo ingreso tab_horariosGrupo "+tab_horariosGrupo+" lis_marca_empleados " +lis_marca_empleados);

			//Es otro empleado Analizar Asitencia
			buscaHorarioMarcacion(tab_horariosGrupo, lis_marca_empleados, fecha);
			lis_marca_empleados.clear();

			String str_evento=tab_marcaciones.getValor( tab_marcaciones.getTotalFilas()-1, "EVENTO_RELOJ_COBIM");
			if(str_evento!=null){
				//Existio marcación	
				int int_band=getNumeroFilaEmpleado(lis_marca_empleados, str_evento);
				if(int_band==-1){
					//Creo una fila correspondiete a la marcación				
					String[] str_fila=new String[9];
					str_fila[0]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "IDE_GEEDP");
					str_fila[1]=str_evento;
					str_fila[2]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "IDE_GTGRE");
					str_fila[3]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "IDE_SUCU");
					str_fila[4]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "fecha_evento_cobim");
					str_fila[5]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "HORA_MARCACION");
					str_fila[6]=null;// HORA FIN
					String[] str_tipo_evento=getEvento(str_evento, tab_eventos, tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "IDE_SUCU"));							
					str_fila[7]=str_tipo_evento[0];
					str_fila[8]=str_tipo_evento[1];// GUARDA EL TIPO DE EVENTO  1=ASISTENCIA, 2=ALMUERZO null si no encontro evento asociado a la marcación 
					lis_marca_empleados.add(str_fila);					
				}
				else{
					String[] str_fila=lis_marca_empleados.get(int_band);
					str_fila[6]=tab_marcaciones.getValor(tab_marcaciones.getTotalFilas()-1, "HORA_MARCACION");
					lis_marca_empleados.set(int_band, str_fila);
				}				
			}


			fecha=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha), 1));			
		}		

		tab_valida_asistencia.guardar();		
		//utilitario.getConexion().agregarSql("UPDATE CON_BIOMETRICO_MARCACIONES SET ACTIVO_COBIM=0  WHERE FECHA_EVENTO_COBIM BETWEEN TO_DATE('"+utilitario.getFormatoFecha(fechaInicio)+"','YYYY-MM-DD') AND TO_DATE('"+utilitario.getFormatoFecha(fechaFin)+"','YYYY-MM-DD')");
		utilitario.getConexion().ejecutarListaSql();

		int int_total=tab_valida_asistencia.getTotalFilas();
		tab_valida_asistencia=null;
		return int_total;		
	}

	public String eliminarResumen(String fechaInicio,String fechaFin){		

		// return utilitario.getConexion().ejecutarSql("DELETE FROM ASI_VALIDA_ASISTENCIA WHERE FECHA_MARCACION_ASVAA BETWEEN TO_DATE('"+utilitario.getFormatoFecha(fechaInicio)+"','YYYY-MM-DD') AND TO_DATE('"+utilitario.getFormatoFecha(fechaFin)+"','YYYY-MM-DD')");
		//Actualice para que no elimine las q ya estan importadas a detalle horas extra
		return utilitario.getConexion().ejecutarSql("DELETE FROM ASI_VALIDA_ASISTENCIA WHERE IMPORTO_ASVAA!=1 AND FECHA_MARCACION_ASVAA BETWEEN TO_DATE('"+utilitario.getFormatoFecha(fechaInicio)+"','YYYY-MM-DD') AND TO_DATE('"+utilitario.getFormatoFecha(fechaFin)+"','YYYY-MM-DD')");

	}

	private int getNumeroFilaEmpleado(List<String[]> lis_marca_empleados,String str_evento){		
		for(int i=0;i<lis_marca_empleados.size();i++){
			String[] actual=lis_marca_empleados.get(i);
			if(actual[1].equals(str_evento)){
				return i;
			}
		}
		return -1;
	}


	public TablaGenerica getNovedades(String fechaInicio,String fechaFin,TablaGenerica tab_marcaciones){
		TablaGenerica tab_novedades=utilitario.consultar("SELECT  * from ASI_NOVEDAD where to_date('"+fechaInicio+"','yyyy-mm-dd') BETWEEN FECHA_INICIO_ASNOV and FECHA_FIN_ASNOV " +
				"and to_date('"+fechaFin+"','yyyy-mm-dd') BETWEEN FECHA_INICIO_ASNOV and FECHA_FIN_ASNOV " +
				"or IDE_ASNOV in ( " +
				"select IDE_ASNOV from ASI_NOVEDAD_DETALLE where IDE_ASVAA in ( " +
				"select a.ide_asvaa from ( " +
				""+tab_marcaciones.getSql()+" "+
				")a " +
				")GROUP BY ide_asnov) ");
		return tab_novedades;
	}


	public TablaGenerica insertarNovedades(TablaGenerica tab_marc,String fechaInicio,String fechaFin){
		if(tab_marc.isEmpty()==false && tab_marc.getTotalFilas()>0){

			TablaGenerica tab_nove=utilitario.consultar("select * from ASI_NOVEDAD " +
					"where FECHA_FIN_ASNOV <= to_date('"+fechaInicio+"','yyyy-mm-dd') " +
					"order by FECHA_FIN_ASNOV DESC");

			//1) Creo la cabecera de la novedad
			TablaGenerica tab_cab_novedad=new TablaGenerica();
			tab_cab_novedad.setTabla("ASI_NOVEDAD", "IDE_ASNOV", -1);
			tab_cab_novedad.setCondicion("IDE_ASNOV=-1");
			tab_cab_novedad.ejecutarSql();
			tab_cab_novedad.insertar();
			if (tab_nove.getTotalFilas()>0){
				fechaInicio=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_nove.getValor(0,"FECHA_FIN_ASNOV")),1));
				tab_cab_novedad.setValor("FECHA_INICIO_ASNOV", utilitario.getFormatoFecha(fechaInicio));
			}else{
				tab_cab_novedad.setValor("FECHA_INICIO_ASNOV", utilitario.getFormatoFecha(fechaInicio));
			}

			tab_cab_novedad.setValor("ACTIVO_ASNOV", "true");
			tab_cab_novedad.setValor("FECHA_FIN_ASNOV", utilitario.getFormatoFecha(fechaFin));
			tab_cab_novedad.setValor("OBSERVACION_ASNOV", "NOVEDADES IMPORTADAS DE RESUMEN DE ASISTENCIA DEL "+utilitario.getFormatoFecha(fechaInicio)+" AL "+utilitario.getFormatoFecha(fechaFin));
			tab_cab_novedad.setValor("IDE_USUA", utilitario.getVariable("IDE_USUA"));
			tab_cab_novedad.guardar();

			//2) Guardo los detalles de la novedad
			TablaGenerica tab_deta_novedad=new TablaGenerica();
			tab_deta_novedad.setTabla("ASI_NOVEDAD_DETALLE", "IDE_ASNOD", '1');
			tab_deta_novedad.setCondicion("IDE_ASNOD=-1");
			tab_deta_novedad.ejecutarSql();


			StringBuilder str_acumula_ide=new StringBuilder();//Sirve para acumular los ide q ya se han pasado a novedades
			List<StringBuilder> lis_actualiza=new ArrayList<StringBuilder>();


			for(int i=0;i<tab_marc.getTotalFilas();i++){

				if(i==500){
					lis_actualiza.add(new StringBuilder(str_acumula_ide));
					str_acumula_ide=new StringBuilder();
				}				

				if(str_acumula_ide.toString().isEmpty()==false){
					str_acumula_ide.append(",");
				}
				str_acumula_ide.append(tab_marc.getValor(i, "IDE_ASVAA"));

				//1) si es null diferencia1 = no marco o solo marco una ves o marco sin configuracion de evento
				if(tab_marc.getValor(i, "DIFERENCIA_ASVAA")==null){
					tab_deta_novedad.insertar();

					tab_deta_novedad.setValor("IDE_ASVAA", tab_marc.getValor(i, "IDE_ASVAA"));
					tab_deta_novedad.setValor("FECHA_ASNOD", tab_marc.getValor(i, "FECHA_MARCACION_ASVAA"));

					tab_deta_novedad.setValor("IDE_GTEMP", tab_marc.getValor(i, "IDE_GTEMP"));				
					tab_deta_novedad.setValor("NRO_HORAS_ASNOD", "0"); 
					tab_deta_novedad.setValor("NRO_HORAS_APROBADO_ASNOD", "0");				
					tab_deta_novedad.setValor("APROBADO_ASNOD", "0");
					tab_deta_novedad.setValor("NOMINA_ASNOD", "0");
					tab_deta_novedad.setValor("VACACIONES_ASNOD", "0");
					tab_deta_novedad.setValor("OBSERVACION_ASNOD", tab_marc.getValor(i, "EVENTO_ASVAA"));
					tab_deta_novedad.setValor("ACTIVO_ASNOD", "true");
					if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
						tab_deta_novedad.setValor("HORA_INICIO_ASNOD", tab_marc.getValor(i, "HORA_MARCACION_ASVAA"));	
					}
					if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
						tab_deta_novedad.setValor("HORA_FIN_ASNOD", tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA"));	
					}									
					tab_deta_novedad.setValor("IDE_ASNOV", tab_cab_novedad.getValor("IDE_ASNOV"));
				}
				else{  //existe marcacion con hora inicio y hora fin

					double dou_diferencia1=0;
					double dou_diferencia2=0;				
					try {
						dou_diferencia1=Double.parseDouble(tab_marc.getValor(i, "DIFERENCIA_ASVAA"));
					} catch (Exception e) {
						dou_diferencia1=0;					
					}
					try {
						dou_diferencia2=Double.parseDouble(tab_marc.getValor(i, "DIFERENCIA_SALIDA_ASVAA"));
					} catch (Exception e) {
						dou_diferencia2=0;					
					}

					//Si la diferencia1 es menor o igual a 0... entonces NO hay novedad 
					if(dou_diferencia1>0){
						//novedad  asistencia,almuerzo o configuracion de otro evento marcada correctamente						
						tab_deta_novedad.insertar();

						tab_deta_novedad.setValor("IDE_ASVAA", tab_marc.getValor(i, "IDE_ASVAA"));
						tab_deta_novedad.setValor("FECHA_ASNOD", tab_marc.getValor(i, "FECHA_MARCACION_ASVAA"));

						tab_deta_novedad.setValor("IDE_GTEMP", tab_marc.getValor(i, "IDE_GTEMP"));				
						tab_deta_novedad.setValor("NRO_HORAS_ASNOD", utilitario.getFormatoNumero(dou_diferencia1)); 
						tab_deta_novedad.setValor("NRO_HORAS_APROBADO_ASNOD", utilitario.getFormatoNumero(dou_diferencia1));				
						tab_deta_novedad.setValor("APROBADO_ASNOD", "0");
						tab_deta_novedad.setValor("NOMINA_ASNOD", "0");
						tab_deta_novedad.setValor("VACACIONES_ASNOD", "0");
						tab_deta_novedad.setValor("OBSERVACION_ASNOD", tab_marc.getValor(i, "EVENTO_ASVAA"));
						tab_deta_novedad.setValor("ACTIVO_ASNOD", "true");
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_INICIO_ASNOD", tab_marc.getValor(i, "HORA_MARCACION_ASVAA"));	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_FIN_ASNOD", tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA"));	
						}									
						tab_deta_novedad.setValor("IDE_ASNOV", tab_cab_novedad.getValor("IDE_ASNOV"));						
					}					
					if(dou_diferencia2<0){
						//Novedad ya q salio a antes de su horario
						tab_deta_novedad.insertar();

						tab_deta_novedad.setValor("IDE_ASVAA", tab_marc.getValor(i, "IDE_ASVAA"));
						tab_deta_novedad.setValor("FECHA_ASNOD", tab_marc.getValor(i, "FECHA_MARCACION_ASVAA"));

						tab_deta_novedad.setValor("IDE_GTEMP", tab_marc.getValor(i, "IDE_GTEMP"));				
						tab_deta_novedad.setValor("NRO_HORAS_ASNOD", utilitario.getFormatoNumero(dou_diferencia2)); 
						tab_deta_novedad.setValor("NRO_HORAS_APROBADO_ASNOD", utilitario.getFormatoNumero(dou_diferencia2));				
						tab_deta_novedad.setValor("APROBADO_ASNOD", "0");
						tab_deta_novedad.setValor("NOMINA_ASNOD", "0");
						tab_deta_novedad.setValor("VACACIONES_ASNOD", "0");
						tab_deta_novedad.setValor("OBSERVACION_ASNOD", tab_marc.getValor(i, "EVENTO_ASVAA"));
						tab_deta_novedad.setValor("ACTIVO_ASNOD", "true");
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_INICIO_ASNOD", tab_marc.getValor(i, "HORA_MARCACION_ASVAA"));	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_FIN_ASNOD", tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA"));	
						}									
						tab_deta_novedad.setValor("IDE_ASNOV", tab_cab_novedad.getValor("IDE_ASNOV"));

					}
					else if(dou_diferencia2>=1){
						//novedad por q salio mas  de 1 hora tarde q su horario normal de trabajo						
						tab_deta_novedad.insertar();

						tab_deta_novedad.setValor("IDE_ASVAA", tab_marc.getValor(i, "IDE_ASVAA"));
						tab_deta_novedad.setValor("FECHA_ASNOD", tab_marc.getValor(i, "FECHA_MARCACION_ASVAA"));

						tab_deta_novedad.setValor("IDE_GTEMP", tab_marc.getValor(i, "IDE_GTEMP"));				
						tab_deta_novedad.setValor("NRO_HORAS_ASNOD", utilitario.getFormatoNumero(dou_diferencia2)); 
						tab_deta_novedad.setValor("NRO_HORAS_APROBADO_ASNOD", utilitario.getFormatoNumero(dou_diferencia2));				
						tab_deta_novedad.setValor("APROBADO_ASNOD", "0");
						tab_deta_novedad.setValor("NOMINA_ASNOD", "0");
						tab_deta_novedad.setValor("VACACIONES_ASNOD", "0");
						tab_deta_novedad.setValor("OBSERVACION_ASNOD", tab_marc.getValor(i, "EVENTO_ASVAA"));
						tab_deta_novedad.setValor("ACTIVO_ASNOD", "true");
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_INICIO_ASNOD", tab_marc.getValor(i, "HORA_MARCACION_ASVAA"));	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							tab_deta_novedad.setValor("HORA_FIN_ASNOD", tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA"));	
						}									
						tab_deta_novedad.setValor("IDE_ASNOV", tab_cab_novedad.getValor("IDE_ASNOV"));

					}

				}

			}

			if (tab_deta_novedad.guardar()){
				return tab_deta_novedad;
			}


			//				//x si hay mas de 1000 registros en un IN
			//				for(StringBuilder str_actual:lis_actualiza){
			//					utilitario.getConexion().agregarSql("UPDATE ASI_VALIDA_ASISTENCIA SET IDE_ASNOV="+tab_cab_novedad.getValor("IDE_ASNOV")+" WHERE IDE_ASVAA IN("+str_actual.toString()+")");	
			//				}
			//
			//				String str_msj=utilitario.getConexion().ejecutarListaSql();
			//				if(str_msj.isEmpty()){
			//
			//					return "Se importaron "+tab_deta_novedad.getTotalFilas()+" registros";
			//				}
			//				else{
			//					return str_msj;
			//				}		
		}					


		return null;

	}

	/**
	 * Importa las novedades de la tabla de asistencia q esten en estado novedad = true 
	 */
	public String importarNovedadesMarcacion(String fechaInicio,String fechaFin){

		TablaGenerica tab_marc=utilitario.consultar("SELECT IDE_ASVAA,PAR.IDE_GTEMP,FECHA_MARCACION_ASVAA,HORA_MARCACION_ASVAA,EVENTO_ASVAA,DIFERENCIA_ASVAA,DIFERENCIA_SALIDA_ASVAA,HORA_MARCA_SALIDA_ASVAA FROM ASI_VALIDA_ASISTENCIA ava " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR par on AVA.IDE_GEEDP = PAR.IDE_GEEDP " +
				"WHERE FECHA_MARCACION_ASVAA BETWEEN TO_DATE('"+fechaInicio+"','YYYY-MM-DD') AND TO_DATE('"+fechaFin+"','YYYY-MM-DD') " +
				"AND NOVEDAD_ASVAA=true " +
				"and IDE_ASNOV IS NULL " +
				"order by FECHA_MARCACION_ASVAA,HORA_MARCACION_ASVAA");

		TablaGenerica tab_nove=getNovedades(fechaInicio, fechaFin, tab_marc);
		System.out.println("tab nove "+tab_nove.getSql());
		if (tab_nove.getTotalFilas()==0){


			System.out.println("crear novedades "+tab_marc.getSql());
			TablaGenerica tab_deta_novedad=insertarNovedades(tab_marc, fechaInicio, fechaFin);
			if (tab_deta_novedad!=null){
				// actualizo las novedades importadas
				utilitario.getConexion().agregarSql("update ASI_VALIDA_ASISTENCIA set IDE_ASNOV="+tab_deta_novedad.getValor(0,"ide_asnov")+" where IDE_ASVAA in ( select ide_asvaa from ("+ tab_marc.getSql()+" )a)");

				String str_msj=utilitario.getConexion().ejecutarListaSql();
				if(str_msj.isEmpty()){
					return "Se importaron "+tab_deta_novedad.getTotalFilas()+" registros";
				}
				else{
					return str_msj;
				}		
			}else{
				return "";
			}

		}else{
			// si ya existen importadas las novedades actualizamos siempre y cuando no se encuentre aprobado
			tab_marc=utilitario.consultar("select * from ( "+
					""+tab_marc.getSql()+" "+
					")a "+ 
					"where a.ide_asvaa in (select IDE_ASVAA from " +
					"ASI_NOVEDAD_DETALLE where IDE_ASNOV="+tab_nove.getValor("IDE_ASNOV")+" " +
					"and APROBADO_ASNOD!=1) ");

			//System.out.println("actualizar novedades "+tab_marc.getSql());

			//1) obtengo la cabecera ya creada de la novedad
			String ide_asnov=tab_nove.getValor("IDE_ASNOV");

			//2) actualizo los detalles de la novedad


			StringBuilder str_acumula_ide=new StringBuilder();//Sirve para acumular los ide q ya se han pasado a novedades
			List<StringBuilder> lis_actualiza=new ArrayList<StringBuilder>();


			for(int i=0;i<tab_marc.getTotalFilas();i++){

				if(i==500){
					lis_actualiza.add(new StringBuilder(str_acumula_ide));
					str_acumula_ide=new StringBuilder();
				}				

				if(str_acumula_ide.toString().isEmpty()==false){
					str_acumula_ide.append(",");
				}
				str_acumula_ide.append(tab_marc.getValor(i, "IDE_ASVAA"));

				//1) si es null diferencia1 = no marco o solo marco una ves o marco sin configuracion de evento
				if(tab_marc.getValor(i, "DIFERENCIA_ASVAA")==null){
					String str_update="update ASI_NOVEDAD_DETALLE " +
							"set ide_gtemp="+tab_marc.getValor(i, "IDE_GTEMP")+", " +
							"FECHA_ASNOD=TO_DATE('"+tab_marc.getValor(i, "FECHA_MARCACION_ASVAA")+"','yyyy-mm-dd'), "+
							"observacion_asnod='"+tab_marc.getValor(i, "EVENTO_ASVAA")+"' ";
					if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
						str_update+=",HORA_INICIO_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCACION_ASVAA")+"','hh24-MM-ss') ";	
					}
					if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
						str_update+=",HORA_FIN_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")+"','hh24-MM-ss') ";	
					}									
					str_update+=" where IDE_ASVAA="+tab_marc.getValor(i, "IDE_ASVAA")+""+ 
							" and ide_asnov="+ide_asnov+"";
					utilitario.getConexion().agregarSqlPantalla(str_update);
				}
				else{  //existe marcacion con hora inicio y hora fin

					double dou_diferencia1=0;
					double dou_diferencia2=0;				
					try {
						dou_diferencia1=Double.parseDouble(tab_marc.getValor(i, "DIFERENCIA_ASVAA"));
					} catch (Exception e) {
						dou_diferencia1=0;					
					}
					try {
						dou_diferencia2=Double.parseDouble(tab_marc.getValor(i, "DIFERENCIA_SALIDA_ASVAA"));
					} catch (Exception e) {
						dou_diferencia2=0;					
					}

					//Si la diferencia1 es menor o igual a 0... entonces NO hay novedad 
					if(dou_diferencia1>0){
						//novedad  asistencia,almuerzo o configuracion de otro evento marcada correctamente						

						String str_update="update ASI_NOVEDAD_DETALLE " +
								"set ide_gtemp="+tab_marc.getValor(i, "IDE_GTEMP")+" " +
								",FECHA_ASNOD=TO_DATE('"+tab_marc.getValor(i, "FECHA_MARCACION_ASVAA")+"','yyyy-mm-dd') "+
								",NRO_HORAS_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",NRO_HORAS_APROBADO_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",observacion_asnod='"+tab_marc.getValor(i, "EVENTO_ASVAA")+"' ";
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							str_update+=",HORA_INICIO_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCACION_ASVAA")+"','hh24-MM-ss') ";	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							str_update+=",HORA_FIN_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")+"','hh24-MM-ss') ";	
						}									
						str_update+=" where IDE_ASVAA="+tab_marc.getValor(i, "IDE_ASVAA")+""+ 
								" and ide_asnov="+ide_asnov+"";
						utilitario.getConexion().agregarSqlPantalla(str_update);

					}					
					if(dou_diferencia2<0){
						//Novedad ya q salio a antes de su horario

						String str_update="update ASI_NOVEDAD_DETALLE " +
								"set ide_gtemp="+tab_marc.getValor(i, "IDE_GTEMP")+" " +
								",FECHA_ASNOD=TO_DATE('"+tab_marc.getValor(i, "FECHA_MARCACION_ASVAA")+"','yyyy-mm-dd')"+
								",NRO_HORAS_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",NRO_HORAS_APROBADO_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",observacion_asnod='"+tab_marc.getValor(i, "EVENTO_ASVAA")+"' ";
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							str_update+=",HORA_INICIO_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCACION_ASVAA")+"','hh24-MM-ss') ";	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							str_update+=",HORA_FIN_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")+"','hh24-MM-ss') ";	
						}									
						str_update+=" where IDE_ASVAA="+tab_marc.getValor(i, "IDE_ASVAA")+""+ 
								" and ide_asnov="+ide_asnov+"";
						utilitario.getConexion().agregarSqlPantalla(str_update);

					}
					else if(dou_diferencia2>=1){
						//novedad por q salio mas  de 1 hora tarde q su horario normal de trabajo						

						String str_update="update ASI_NOVEDAD_DETALLE " +
								"set ide_gtemp="+tab_marc.getValor(i, "IDE_GTEMP")+" " +
								",FECHA_ASNOD=TO_DATE('"+tab_marc.getValor(i, "FECHA_MARCACION_ASVAA")+"','yyyy-mm-dd') "+
								",NRO_HORAS_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",NRO_HORAS_APROBADO_ASNOD="+utilitario.getFormatoNumero(dou_diferencia1)+" "+
								",observacion_asnod='"+tab_marc.getValor(i, "EVENTO_ASVAA")+"' ";
						if(tab_marc.getValor(i, "HORA_MARCACION_ASVAA")!=null){
							str_update+=",HORA_INICIO_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCACION_ASVAA")+"','hh24-MM-ss') ";	
						}
						if(tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")!=null){
							str_update+=",HORA_FIN_ASNOD=to_timestamp('"+ tab_marc.getValor(i, "HORA_MARCA_SALIDA_ASVAA")+"','hh24-MM-ss') ";	
						}									
						str_update+=" where IDE_ASVAA="+tab_marc.getValor(i, "IDE_ASVAA")+""+ 
								" and ide_asnov="+ide_asnov+"";
						utilitario.getConexion().agregarSqlPantalla(str_update);
					}
				}

			}


			int num_registros_actualizados=utilitario.getConexion().getSqlPantalla().size();
			utilitario.getConexion().agregarSql("update ASI_VALIDA_ASISTENCIA set IDE_ASNOV="+ide_asnov+" where IDE_ASVAA in ( select ide_asvaa from ("+ tab_marc.getSql()+" )a)");

			int num_registros_insertados=0;
			if (utilitario.isFechaMayor(utilitario.getFecha(fechaFin),utilitario.getFecha(tab_nove.getValor("FECHA_FIN_ASNOV")))){

				fechaInicio=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_nove.getValor("FECHA_FIN_ASNOV")),1));

				tab_marc=utilitario.consultar("SELECT IDE_ASVAA,PAR.IDE_GTEMP,FECHA_MARCACION_ASVAA,HORA_MARCACION_ASVAA,EVENTO_ASVAA,DIFERENCIA_ASVAA,DIFERENCIA_SALIDA_ASVAA,HORA_MARCA_SALIDA_ASVAA FROM ASI_VALIDA_ASISTENCIA ava " +
						"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR par on AVA.IDE_GEEDP = PAR.IDE_GEEDP " +
						"WHERE FECHA_MARCACION_ASVAA BETWEEN TO_DATE('"+fechaInicio+"','YYYY-MM-DD') AND TO_DATE('"+fechaFin+"','YYYY-MM-DD') " +
						"AND NOVEDAD_ASVAA=true and IDE_ASNOV IS NULL order by FECHA_MARCACION_ASVAA,HORA_MARCACION_ASVAA");
				//System.out.println("insertar novedades extras "+tab_marc.getSql());
				TablaGenerica tab_deta_novedad=insertarNovedades(tab_marc, fechaInicio, fechaFin);
				if (tab_deta_novedad!=null){
					// actualizo las novedades importadas
					utilitario.getConexion().agregarSql("update ASI_VALIDA_ASISTENCIA set IDE_ASNOV="+tab_deta_novedad.getValor(0,"ide_asnov")+" where IDE_ASVAA in ( select ide_asvaa from ("+ tab_marc.getSql()+" )a)");
				}

			}


			String str_msj=utilitario.getConexion().ejecutarListaSql();
			if(str_msj.isEmpty()){

				return "Se importaron "+tab_marc.getTotalFilas()+" registros";
			}
			else{
				return str_msj;
			}		

		}
	}

}
