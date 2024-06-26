/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "distributivo_final_noviembre", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "DistributivoFinalNoviembre.findAll", query = "SELECT d FROM DistributivoFinalNoviembre d"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByIdeDistributivo", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.ideDistributivo = :ideDistributivo"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByNumeroDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.numeroDiago = :numeroDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByCedulaDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.cedulaDiago = :cedulaDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByNombreDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.nombreDiago = :nombreDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByPartidaDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.partidaDiago = :partidaDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByFechaIngresoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.fechaIngresoDiago = :fechaIngresoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByFechaSalidaDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.fechaSalidaDiago = :fechaSalidaDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByDenomPuestoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.denomPuestoDiago = :denomPuestoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByTipoProcesDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.tipoProcesDiago = :tipoProcesDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByProcesoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.procesoDiago = :procesoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findBySubprocesoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.subprocesoDiago = :subprocesoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByMicroProcesoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.microProcesoDiago = :microProcesoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByGrupoOcupDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.grupoOcupDiago = :grupoOcupDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByRmu2014Diago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.rmu2014Diago = :rmu2014Diago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByRegimenDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.regimenDiago = :regimenDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByModalidadDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.modalidadDiago = :modalidadDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByLugarTrabDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.lugarTrabDiago = :lugarTrabDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByGeneroDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.generoDiago = :generoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByFormacionDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.formacionDiago = :formacionDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByTituloDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.tituloDiago = :tituloDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByAreaConociDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.areaConociDiago = :areaConociDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByObservacionDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.observacionDiago = :observacionDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByDiscapacidad", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.discapacidad = :discapacidad"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByTipoDiscapacidadDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.tipoDiscapacidadDiago = :tipoDiscapacidadDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByFechaNacDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.fechaNacDiago = :fechaNacDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByOtrosDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.otrosDiago = :otrosDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByDepartamentoDiago", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.departamentoDiago = :departamentoDiago"),
    @NamedQuery(name = "DistributivoFinalNoviembre.findByCargoFuncional", query = "SELECT d FROM DistributivoFinalNoviembre d WHERE d.cargoFuncional = :cargoFuncional")})
public class DistributivoFinalNoviembre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_distributivo", nullable = false)
    private Integer ideDistributivo;
    @Column(name = "numero_diago")
    private Integer numeroDiago;
    @Size(max = 30)
    @Column(name = "cedula_diago", length = 30)
    private String cedulaDiago;
    @Size(max = 100)
    @Column(name = "nombre_diago", length = 100)
    private String nombreDiago;
    @Size(max = 50)
    @Column(name = "partida_diago", length = 50)
    private String partidaDiago;
    @Column(name = "fecha_ingreso_diago")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoDiago;
    @Column(name = "fecha_salida_diago")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaDiago;
    @Size(max = 100)
    @Column(name = "denom_puesto_diago", length = 100)
    private String denomPuestoDiago;
    @Size(max = 100)
    @Column(name = "tipo_proces_diago", length = 100)
    private String tipoProcesDiago;
    @Size(max = 100)
    @Column(name = "proceso_diago", length = 100)
    private String procesoDiago;
    @Size(max = 100)
    @Column(name = "subproceso_diago", length = 100)
    private String subprocesoDiago;
    @Size(max = 100)
    @Column(name = "micro_proceso_diago", length = 100)
    private String microProcesoDiago;
    @Size(max = 100)
    @Column(name = "grupo_ocup_diago", length = 100)
    private String grupoOcupDiago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rmu_2014_diago", precision = 10, scale = 2)
    private BigDecimal rmu2014Diago;
    @Size(max = 100)
    @Column(name = "regimen_diago", length = 100)
    private String regimenDiago;
    @Size(max = 100)
    @Column(name = "modalidad_diago", length = 100)
    private String modalidadDiago;
    @Size(max = 100)
    @Column(name = "lugar_trab_diago", length = 100)
    private String lugarTrabDiago;
    @Size(max = 50)
    @Column(name = "genero_diago", length = 50)
    private String generoDiago;
    @Size(max = 100)
    @Column(name = "formacion_diago", length = 100)
    private String formacionDiago;
    @Size(max = 100)
    @Column(name = "titulo_diago", length = 100)
    private String tituloDiago;
    @Size(max = 100)
    @Column(name = "area_conoci_diago", length = 100)
    private String areaConociDiago;
    @Size(max = 100)
    @Column(name = "observacion_diago", length = 100)
    private String observacionDiago;
    @Size(max = 100)
    @Column(name = "discapacidad", length = 100)
    private String discapacidad;
    @Size(max = 100)
    @Column(name = "tipo_discapacidad_diago", length = 100)
    private String tipoDiscapacidadDiago;
    @Column(name = "fecha_nac_diago")
    @Temporal(TemporalType.DATE)
    private Date fechaNacDiago;
    @Size(max = 100)
    @Column(name = "otros_diago", length = 100)
    private String otrosDiago;
    @Size(max = 100)
    @Column(name = "departamento_diago", length = 100)
    private String departamentoDiago;
    @Size(max = 100)
    @Column(name = "cargo_funcional", length = 100)
    private String cargoFuncional;

    public DistributivoFinalNoviembre() {
    }

    public DistributivoFinalNoviembre(Integer ideDistributivo) {
        this.ideDistributivo = ideDistributivo;
    }

    public Integer getIdeDistributivo() {
        return ideDistributivo;
    }

    public void setIdeDistributivo(Integer ideDistributivo) {
        this.ideDistributivo = ideDistributivo;
    }

    public Integer getNumeroDiago() {
        return numeroDiago;
    }

    public void setNumeroDiago(Integer numeroDiago) {
        this.numeroDiago = numeroDiago;
    }

    public String getCedulaDiago() {
        return cedulaDiago;
    }

    public void setCedulaDiago(String cedulaDiago) {
        this.cedulaDiago = cedulaDiago;
    }

    public String getNombreDiago() {
        return nombreDiago;
    }

    public void setNombreDiago(String nombreDiago) {
        this.nombreDiago = nombreDiago;
    }

    public String getPartidaDiago() {
        return partidaDiago;
    }

    public void setPartidaDiago(String partidaDiago) {
        this.partidaDiago = partidaDiago;
    }

    public Date getFechaIngresoDiago() {
        return fechaIngresoDiago;
    }

    public void setFechaIngresoDiago(Date fechaIngresoDiago) {
        this.fechaIngresoDiago = fechaIngresoDiago;
    }

    public Date getFechaSalidaDiago() {
        return fechaSalidaDiago;
    }

    public void setFechaSalidaDiago(Date fechaSalidaDiago) {
        this.fechaSalidaDiago = fechaSalidaDiago;
    }

    public String getDenomPuestoDiago() {
        return denomPuestoDiago;
    }

    public void setDenomPuestoDiago(String denomPuestoDiago) {
        this.denomPuestoDiago = denomPuestoDiago;
    }

    public String getTipoProcesDiago() {
        return tipoProcesDiago;
    }

    public void setTipoProcesDiago(String tipoProcesDiago) {
        this.tipoProcesDiago = tipoProcesDiago;
    }

    public String getProcesoDiago() {
        return procesoDiago;
    }

    public void setProcesoDiago(String procesoDiago) {
        this.procesoDiago = procesoDiago;
    }

    public String getSubprocesoDiago() {
        return subprocesoDiago;
    }

    public void setSubprocesoDiago(String subprocesoDiago) {
        this.subprocesoDiago = subprocesoDiago;
    }

    public String getMicroProcesoDiago() {
        return microProcesoDiago;
    }

    public void setMicroProcesoDiago(String microProcesoDiago) {
        this.microProcesoDiago = microProcesoDiago;
    }

    public String getGrupoOcupDiago() {
        return grupoOcupDiago;
    }

    public void setGrupoOcupDiago(String grupoOcupDiago) {
        this.grupoOcupDiago = grupoOcupDiago;
    }

    public BigDecimal getRmu2014Diago() {
        return rmu2014Diago;
    }

    public void setRmu2014Diago(BigDecimal rmu2014Diago) {
        this.rmu2014Diago = rmu2014Diago;
    }

    public String getRegimenDiago() {
        return regimenDiago;
    }

    public void setRegimenDiago(String regimenDiago) {
        this.regimenDiago = regimenDiago;
    }

    public String getModalidadDiago() {
        return modalidadDiago;
    }

    public void setModalidadDiago(String modalidadDiago) {
        this.modalidadDiago = modalidadDiago;
    }

    public String getLugarTrabDiago() {
        return lugarTrabDiago;
    }

    public void setLugarTrabDiago(String lugarTrabDiago) {
        this.lugarTrabDiago = lugarTrabDiago;
    }

    public String getGeneroDiago() {
        return generoDiago;
    }

    public void setGeneroDiago(String generoDiago) {
        this.generoDiago = generoDiago;
    }

    public String getFormacionDiago() {
        return formacionDiago;
    }

    public void setFormacionDiago(String formacionDiago) {
        this.formacionDiago = formacionDiago;
    }

    public String getTituloDiago() {
        return tituloDiago;
    }

    public void setTituloDiago(String tituloDiago) {
        this.tituloDiago = tituloDiago;
    }

    public String getAreaConociDiago() {
        return areaConociDiago;
    }

    public void setAreaConociDiago(String areaConociDiago) {
        this.areaConociDiago = areaConociDiago;
    }

    public String getObservacionDiago() {
        return observacionDiago;
    }

    public void setObservacionDiago(String observacionDiago) {
        this.observacionDiago = observacionDiago;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getTipoDiscapacidadDiago() {
        return tipoDiscapacidadDiago;
    }

    public void setTipoDiscapacidadDiago(String tipoDiscapacidadDiago) {
        this.tipoDiscapacidadDiago = tipoDiscapacidadDiago;
    }

    public Date getFechaNacDiago() {
        return fechaNacDiago;
    }

    public void setFechaNacDiago(Date fechaNacDiago) {
        this.fechaNacDiago = fechaNacDiago;
    }

    public String getOtrosDiago() {
        return otrosDiago;
    }

    public void setOtrosDiago(String otrosDiago) {
        this.otrosDiago = otrosDiago;
    }

    public String getDepartamentoDiago() {
        return departamentoDiago;
    }

    public void setDepartamentoDiago(String departamentoDiago) {
        this.departamentoDiago = departamentoDiago;
    }

    public String getCargoFuncional() {
        return cargoFuncional;
    }

    public void setCargoFuncional(String cargoFuncional) {
        this.cargoFuncional = cargoFuncional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDistributivo != null ? ideDistributivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DistributivoFinalNoviembre)) {
            return false;
        }
        DistributivoFinalNoviembre other = (DistributivoFinalNoviembre) object;
        if ((this.ideDistributivo == null && other.ideDistributivo != null) || (this.ideDistributivo != null && !this.ideDistributivo.equals(other.ideDistributivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.DistributivoFinalNoviembre[ ideDistributivo=" + ideDistributivo + " ]";
    }
    
}
