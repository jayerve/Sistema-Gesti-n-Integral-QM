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
@Table(name = "nrh_tipo_rubro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhTipoRubro.findAll", query = "SELECT n FROM NrhTipoRubro n"),
    @NamedQuery(name = "NrhTipoRubro.findByIdeNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.ideNrtir = :ideNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByDetalleNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.detalleNrtir = :detalleNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findBySignoNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.signoNrtir = :signoNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByActivoNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.activoNrtir = :activoNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByUsuarioIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByFechaIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByUsuarioActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoRubro.findByFechaActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoRubro.findByHoraIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByHoraActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.horaActua = :horaActua")})
public class NrhTipoRubro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrtir", nullable = false)
    private Integer ideNrtir;
    @Size(max = 50)
    @Column(name = "detalle_nrtir", length = 50)
    private String detalleNrtir;
    @Column(name = "signo_nrtir")
    private Integer signoNrtir;
    @Column(name = "activo_nrtir")
    private Boolean activoNrtir;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrtir")
    private List<NrhRubro> nrhRubroList;

    public NrhTipoRubro() {
    }

    public NrhTipoRubro(Integer ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public Integer getIdeNrtir() {
        return ideNrtir;
    }

    public void setIdeNrtir(Integer ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public String getDetalleNrtir() {
        return detalleNrtir;
    }

    public void setDetalleNrtir(String detalleNrtir) {
        this.detalleNrtir = detalleNrtir;
    }

    public Integer getSignoNrtir() {
        return signoNrtir;
    }

    public void setSignoNrtir(Integer signoNrtir) {
        this.signoNrtir = signoNrtir;
    }

    public Boolean getActivoNrtir() {
        return activoNrtir;
    }

    public void setActivoNrtir(Boolean activoNrtir) {
        this.activoNrtir = activoNrtir;
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
        hash += (ideNrtir != null ? ideNrtir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoRubro)) {
            return false;
        }
        NrhTipoRubro other = (NrhTipoRubro) object;
        if ((this.ideNrtir == null && other.ideNrtir != null) || (this.ideNrtir != null && !this.ideNrtir.equals(other.ideNrtir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoRubro[ ideNrtir=" + ideNrtir + " ]";
    }
    
}
