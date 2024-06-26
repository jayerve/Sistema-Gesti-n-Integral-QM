/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "nrh_forma_calculo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhFormaCalculo.findAll", query = "SELECT n FROM NrhFormaCalculo n"),
    @NamedQuery(name = "NrhFormaCalculo.findByIdeNrfoc", query = "SELECT n FROM NrhFormaCalculo n WHERE n.ideNrfoc = :ideNrfoc"),
    @NamedQuery(name = "NrhFormaCalculo.findByDetalleNrfoc", query = "SELECT n FROM NrhFormaCalculo n WHERE n.detalleNrfoc = :detalleNrfoc"),
    @NamedQuery(name = "NrhFormaCalculo.findByActivoNrfoc", query = "SELECT n FROM NrhFormaCalculo n WHERE n.activoNrfoc = :activoNrfoc"),
    @NamedQuery(name = "NrhFormaCalculo.findByUsuarioIngre", query = "SELECT n FROM NrhFormaCalculo n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhFormaCalculo.findByFechaIngre", query = "SELECT n FROM NrhFormaCalculo n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhFormaCalculo.findByUsuarioActua", query = "SELECT n FROM NrhFormaCalculo n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhFormaCalculo.findByFechaActua", query = "SELECT n FROM NrhFormaCalculo n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhFormaCalculo.findByHoraIngre", query = "SELECT n FROM NrhFormaCalculo n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhFormaCalculo.findByHoraActua", query = "SELECT n FROM NrhFormaCalculo n WHERE n.horaActua = :horaActua")})
public class NrhFormaCalculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrfoc", nullable = false)
    private Integer ideNrfoc;
    @Size(max = 50)
    @Column(name = "detalle_nrfoc", length = 50)
    private String detalleNrfoc;
    @Column(name = "activo_nrfoc")
    private Boolean activoNrfoc;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrfoc")
    private List<NrhRubro> nrhRubroList;

    public NrhFormaCalculo() {
    }

    public NrhFormaCalculo(Integer ideNrfoc) {
        this.ideNrfoc = ideNrfoc;
    }

    public Integer getIdeNrfoc() {
        return ideNrfoc;
    }

    public void setIdeNrfoc(Integer ideNrfoc) {
        this.ideNrfoc = ideNrfoc;
    }

    public String getDetalleNrfoc() {
        return detalleNrfoc;
    }

    public void setDetalleNrfoc(String detalleNrfoc) {
        this.detalleNrfoc = detalleNrfoc;
    }

    public Boolean getActivoNrfoc() {
        return activoNrfoc;
    }

    public void setActivoNrfoc(Boolean activoNrfoc) {
        this.activoNrfoc = activoNrfoc;
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

    public List<NrhRubro> getNrhRubroList() {
        return nrhRubroList;
    }

    public void setNrhRubroList(List<NrhRubro> nrhRubroList) {
        this.nrhRubroList = nrhRubroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrfoc != null ? ideNrfoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhFormaCalculo)) {
            return false;
        }
        NrhFormaCalculo other = (NrhFormaCalculo) object;
        if ((this.ideNrfoc == null && other.ideNrfoc != null) || (this.ideNrfoc != null && !this.ideNrfoc.equals(other.ideNrfoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhFormaCalculo[ ideNrfoc=" + ideNrfoc + " ]";
    }
    
}
