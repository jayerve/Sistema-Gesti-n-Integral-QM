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
@Table(name = "sbs_tipo_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsTipoArchivo.findAll", query = "SELECT s FROM SbsTipoArchivo s"),
    @NamedQuery(name = "SbsTipoArchivo.findByIdeSbtia", query = "SELECT s FROM SbsTipoArchivo s WHERE s.ideSbtia = :ideSbtia"),
    @NamedQuery(name = "SbsTipoArchivo.findByDetalleSbtia", query = "SELECT s FROM SbsTipoArchivo s WHERE s.detalleSbtia = :detalleSbtia"),
    @NamedQuery(name = "SbsTipoArchivo.findByActivoSbtia", query = "SELECT s FROM SbsTipoArchivo s WHERE s.activoSbtia = :activoSbtia"),
    @NamedQuery(name = "SbsTipoArchivo.findByUsuarioIngre", query = "SELECT s FROM SbsTipoArchivo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsTipoArchivo.findByFechaIngre", query = "SELECT s FROM SbsTipoArchivo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsTipoArchivo.findByHoraIngre", query = "SELECT s FROM SbsTipoArchivo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsTipoArchivo.findByUsuarioActua", query = "SELECT s FROM SbsTipoArchivo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsTipoArchivo.findByFechaActua", query = "SELECT s FROM SbsTipoArchivo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsTipoArchivo.findByHoraActua", query = "SELECT s FROM SbsTipoArchivo s WHERE s.horaActua = :horaActua")})
public class SbsTipoArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbtia", nullable = false)
    private Integer ideSbtia;
    @Size(max = 50)
    @Column(name = "detalle_sbtia", length = 50)
    private String detalleSbtia;
    @Column(name = "activo_sbtia")
    private Boolean activoSbtia;
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
    @OneToMany(mappedBy = "ideSbtia")
    private List<SbsArchivoCargoFuncional> sbsArchivoCargoFuncionalList;

    public SbsTipoArchivo() {
    }

    public SbsTipoArchivo(Integer ideSbtia) {
        this.ideSbtia = ideSbtia;
    }

    public Integer getIdeSbtia() {
        return ideSbtia;
    }

    public void setIdeSbtia(Integer ideSbtia) {
        this.ideSbtia = ideSbtia;
    }

    public String getDetalleSbtia() {
        return detalleSbtia;
    }

    public void setDetalleSbtia(String detalleSbtia) {
        this.detalleSbtia = detalleSbtia;
    }

    public Boolean getActivoSbtia() {
        return activoSbtia;
    }

    public void setActivoSbtia(Boolean activoSbtia) {
        this.activoSbtia = activoSbtia;
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

    public List<SbsArchivoCargoFuncional> getSbsArchivoCargoFuncionalList() {
        return sbsArchivoCargoFuncionalList;
    }

    public void setSbsArchivoCargoFuncionalList(List<SbsArchivoCargoFuncional> sbsArchivoCargoFuncionalList) {
        this.sbsArchivoCargoFuncionalList = sbsArchivoCargoFuncionalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbtia != null ? ideSbtia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsTipoArchivo)) {
            return false;
        }
        SbsTipoArchivo other = (SbsTipoArchivo) object;
        if ((this.ideSbtia == null && other.ideSbtia != null) || (this.ideSbtia != null && !this.ideSbtia.equals(other.ideSbtia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsTipoArchivo[ ideSbtia=" + ideSbtia + " ]";
    }
    
}
