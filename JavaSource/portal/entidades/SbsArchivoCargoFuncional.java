/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "sbs_archivo_cargo_funcional", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoCargoFuncional.findAll", query = "SELECT s FROM SbsArchivoCargoFuncional s"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByIdeSbacf", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.ideSbacf = :ideSbacf"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByIdeGecaf", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByActivoSbacf", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.activoSbacf = :activoSbacf"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByFechaIngre", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByHoraIngre", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByUsuarioActua", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByFechaActua", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoCargoFuncional.findByHoraActua", query = "SELECT s FROM SbsArchivoCargoFuncional s WHERE s.horaActua = :horaActua")})
public class SbsArchivoCargoFuncional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbacf", nullable = false)
    private Integer ideSbacf;
    @Column(name = "ide_gecaf")
    private Integer ideGecaf;
    @Column(name = "activo_sbacf")
    private Boolean activoSbacf;
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
    @JoinColumn(name = "ide_sbtia", referencedColumnName = "ide_sbtia")
    @ManyToOne
    private SbsTipoArchivo ideSbtia;

    public SbsArchivoCargoFuncional() {
    }

    public SbsArchivoCargoFuncional(Integer ideSbacf) {
        this.ideSbacf = ideSbacf;
    }

    public Integer getIdeSbacf() {
        return ideSbacf;
    }

    public void setIdeSbacf(Integer ideSbacf) {
        this.ideSbacf = ideSbacf;
    }

    public Integer getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Boolean getActivoSbacf() {
        return activoSbacf;
    }

    public void setActivoSbacf(Boolean activoSbacf) {
        this.activoSbacf = activoSbacf;
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

    public SbsTipoArchivo getIdeSbtia() {
        return ideSbtia;
    }

    public void setIdeSbtia(SbsTipoArchivo ideSbtia) {
        this.ideSbtia = ideSbtia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbacf != null ? ideSbacf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoCargoFuncional)) {
            return false;
        }
        SbsArchivoCargoFuncional other = (SbsArchivoCargoFuncional) object;
        if ((this.ideSbacf == null && other.ideSbacf != null) || (this.ideSbacf != null && !this.ideSbacf.equals(other.ideSbacf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoCargoFuncional[ ideSbacf=" + ideSbacf + " ]";
    }
    
}
