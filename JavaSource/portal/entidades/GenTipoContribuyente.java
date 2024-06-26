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
@Table(name = "gen_tipo_contribuyente", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoContribuyente.findAll", query = "SELECT g FROM GenTipoContribuyente g"),
    @NamedQuery(name = "GenTipoContribuyente.findByIdeGetic", query = "SELECT g FROM GenTipoContribuyente g WHERE g.ideGetic = :ideGetic"),
    @NamedQuery(name = "GenTipoContribuyente.findByDetalleGetic", query = "SELECT g FROM GenTipoContribuyente g WHERE g.detalleGetic = :detalleGetic"),
    @NamedQuery(name = "GenTipoContribuyente.findByActivoGetic", query = "SELECT g FROM GenTipoContribuyente g WHERE g.activoGetic = :activoGetic"),
    @NamedQuery(name = "GenTipoContribuyente.findByUsuarioIngre", query = "SELECT g FROM GenTipoContribuyente g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoContribuyente.findByFechaIngre", query = "SELECT g FROM GenTipoContribuyente g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoContribuyente.findByUsuarioActua", query = "SELECT g FROM GenTipoContribuyente g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoContribuyente.findByFechaActua", query = "SELECT g FROM GenTipoContribuyente g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoContribuyente.findByHoraIngre", query = "SELECT g FROM GenTipoContribuyente g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoContribuyente.findByHoraActua", query = "SELECT g FROM GenTipoContribuyente g WHERE g.horaActua = :horaActua")})
public class GenTipoContribuyente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getic", nullable = false)
    private Integer ideGetic;
    @Size(max = 50)
    @Column(name = "detalle_getic", length = 50)
    private String detalleGetic;
    @Column(name = "activo_getic")
    private Boolean activoGetic;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGetic")
    private List<GenBeneficiario> genBeneficiarioList;

    public GenTipoContribuyente() {
    }

    public GenTipoContribuyente(Integer ideGetic) {
        this.ideGetic = ideGetic;
    }

    public Integer getIdeGetic() {
        return ideGetic;
    }

    public void setIdeGetic(Integer ideGetic) {
        this.ideGetic = ideGetic;
    }

    public String getDetalleGetic() {
        return detalleGetic;
    }

    public void setDetalleGetic(String detalleGetic) {
        this.detalleGetic = detalleGetic;
    }

    public Boolean getActivoGetic() {
        return activoGetic;
    }

    public void setActivoGetic(Boolean activoGetic) {
        this.activoGetic = activoGetic;
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

    public List<GenBeneficiario> getGenBeneficiarioList() {
        return genBeneficiarioList;
    }

    public void setGenBeneficiarioList(List<GenBeneficiario> genBeneficiarioList) {
        this.genBeneficiarioList = genBeneficiarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetic != null ? ideGetic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoContribuyente)) {
            return false;
        }
        GenTipoContribuyente other = (GenTipoContribuyente) object;
        if ((this.ideGetic == null && other.ideGetic != null) || (this.ideGetic != null && !this.ideGetic.equals(other.ideGetic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoContribuyente[ ideGetic=" + ideGetic + " ]";
    }
    
}
