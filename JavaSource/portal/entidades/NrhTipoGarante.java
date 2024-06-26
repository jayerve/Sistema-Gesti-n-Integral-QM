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
@Table(name = "nrh_tipo_garante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhTipoGarante.findAll", query = "SELECT n FROM NrhTipoGarante n"),
    @NamedQuery(name = "NrhTipoGarante.findByIdeNrtig", query = "SELECT n FROM NrhTipoGarante n WHERE n.ideNrtig = :ideNrtig"),
    @NamedQuery(name = "NrhTipoGarante.findByDetalleNrtig", query = "SELECT n FROM NrhTipoGarante n WHERE n.detalleNrtig = :detalleNrtig"),
    @NamedQuery(name = "NrhTipoGarante.findByActivoNrtig", query = "SELECT n FROM NrhTipoGarante n WHERE n.activoNrtig = :activoNrtig"),
    @NamedQuery(name = "NrhTipoGarante.findByUsuarioIngre", query = "SELECT n FROM NrhTipoGarante n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoGarante.findByFechaIngre", query = "SELECT n FROM NrhTipoGarante n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoGarante.findByUsuarioActua", query = "SELECT n FROM NrhTipoGarante n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoGarante.findByFechaActua", query = "SELECT n FROM NrhTipoGarante n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoGarante.findByHoraIngre", query = "SELECT n FROM NrhTipoGarante n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoGarante.findByHoraActua", query = "SELECT n FROM NrhTipoGarante n WHERE n.horaActua = :horaActua")})
public class NrhTipoGarante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrtig", nullable = false)
    private Integer ideNrtig;
    @Size(max = 100)
    @Column(name = "detalle_nrtig", length = 100)
    private String detalleNrtig;
    @Column(name = "activo_nrtig")
    private Boolean activoNrtig;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideNrtig")
    private List<NrhGarante> nrhGaranteList;

    public NrhTipoGarante() {
    }

    public NrhTipoGarante(Integer ideNrtig) {
        this.ideNrtig = ideNrtig;
    }

    public Integer getIdeNrtig() {
        return ideNrtig;
    }

    public void setIdeNrtig(Integer ideNrtig) {
        this.ideNrtig = ideNrtig;
    }

    public String getDetalleNrtig() {
        return detalleNrtig;
    }

    public void setDetalleNrtig(String detalleNrtig) {
        this.detalleNrtig = detalleNrtig;
    }

    public Boolean getActivoNrtig() {
        return activoNrtig;
    }

    public void setActivoNrtig(Boolean activoNrtig) {
        this.activoNrtig = activoNrtig;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<NrhGarante> getNrhGaranteList() {
        return nrhGaranteList;
    }

    public void setNrhGaranteList(List<NrhGarante> nrhGaranteList) {
        this.nrhGaranteList = nrhGaranteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtig != null ? ideNrtig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoGarante)) {
            return false;
        }
        NrhTipoGarante other = (NrhTipoGarante) object;
        if ((this.ideNrtig == null && other.ideNrtig != null) || (this.ideNrtig != null && !this.ideNrtig.equals(other.ideNrtig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoGarante[ ideNrtig=" + ideNrtig + " ]";
    }
    
}
