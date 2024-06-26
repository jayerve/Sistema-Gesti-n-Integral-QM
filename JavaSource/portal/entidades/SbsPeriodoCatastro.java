/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "sbs_periodo_catastro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsPeriodoCatastro.findAll", query = "SELECT s FROM SbsPeriodoCatastro s"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByIdeSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.ideSbpec = :ideSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByFechaPeriodoSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.fechaPeriodoSbpec = :fechaPeriodoSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByCodigoIdentidadSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.codigoIdentidadSbpec = :codigoIdentidadSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByFechaFinSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.fechaFinSbpec = :fechaFinSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByFechaInicioSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.fechaInicioSbpec = :fechaInicioSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByActivoSbpec", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.activoSbpec = :activoSbpec"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByUsuarioIngre", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByFechaIngre", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByHoraIngre", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByUsuarioActua", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByFechaActua", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsPeriodoCatastro.findByHoraActua", query = "SELECT s FROM SbsPeriodoCatastro s WHERE s.horaActua = :horaActua")})
public class SbsPeriodoCatastro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbpec", nullable = false)
    private Integer ideSbpec;
    @Column(name = "fecha_periodo_sbpec")
    @Temporal(TemporalType.DATE)
    private Date fechaPeriodoSbpec;
    @Size(max = 50)
    @Column(name = "codigo_identidad_sbpec", length = 50)
    private String codigoIdentidadSbpec;
    @Column(name = "fecha_fin_sbpec")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSbpec;
    @Column(name = "fecha_inicio_sbpec")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSbpec;
    @Column(name = "activo_sbpec")
    private Boolean activoSbpec;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSbpec")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideSbpec")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideSbpec")
    private List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList;
    @OneToMany(mappedBy = "ideSbpec")
    private List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList;

    public SbsPeriodoCatastro() {
    }

    public SbsPeriodoCatastro(Integer ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public Integer getIdeSbpec() {
        return ideSbpec;
    }

    public void setIdeSbpec(Integer ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public Date getFechaPeriodoSbpec() {
        return fechaPeriodoSbpec;
    }

    public void setFechaPeriodoSbpec(Date fechaPeriodoSbpec) {
        this.fechaPeriodoSbpec = fechaPeriodoSbpec;
    }

    public String getCodigoIdentidadSbpec() {
        return codigoIdentidadSbpec;
    }

    public void setCodigoIdentidadSbpec(String codigoIdentidadSbpec) {
        this.codigoIdentidadSbpec = codigoIdentidadSbpec;
    }

    public Date getFechaFinSbpec() {
        return fechaFinSbpec;
    }

    public void setFechaFinSbpec(Date fechaFinSbpec) {
        this.fechaFinSbpec = fechaFinSbpec;
    }

    public Date getFechaInicioSbpec() {
        return fechaInicioSbpec;
    }

    public void setFechaInicioSbpec(Date fechaInicioSbpec) {
        this.fechaInicioSbpec = fechaInicioSbpec;
    }

    public Boolean getActivoSbpec() {
        return activoSbpec;
    }

    public void setActivoSbpec(Boolean activoSbpec) {
        this.activoSbpec = activoSbpec;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<SbsArchivoCuarentaUno> getSbsArchivoCuarentaUnoList() {
        return sbsArchivoCuarentaUnoList;
    }

    public void setSbsArchivoCuarentaUnoList(List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList) {
        this.sbsArchivoCuarentaUnoList = sbsArchivoCuarentaUnoList;
    }

    public List<SbsArchivoVeinteTres> getSbsArchivoVeinteTresList() {
        return sbsArchivoVeinteTresList;
    }

    public void setSbsArchivoVeinteTresList(List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList) {
        this.sbsArchivoVeinteTresList = sbsArchivoVeinteTresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbpec != null ? ideSbpec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsPeriodoCatastro)) {
            return false;
        }
        SbsPeriodoCatastro other = (SbsPeriodoCatastro) object;
        if ((this.ideSbpec == null && other.ideSbpec != null) || (this.ideSbpec != null && !this.ideSbpec.equals(other.ideSbpec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsPeriodoCatastro[ ideSbpec=" + ideSbpec + " ]";
    }
    
}
