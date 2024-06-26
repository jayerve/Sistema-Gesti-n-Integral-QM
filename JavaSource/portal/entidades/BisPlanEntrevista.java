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
@Table(name = "bis_plan_entrevista", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BisPlanEntrevista.findAll", query = "SELECT b FROM BisPlanEntrevista b"),
    @NamedQuery(name = "BisPlanEntrevista.findByIdeBiple", query = "SELECT b FROM BisPlanEntrevista b WHERE b.ideBiple = :ideBiple"),
    @NamedQuery(name = "BisPlanEntrevista.findByDetalleBiple", query = "SELECT b FROM BisPlanEntrevista b WHERE b.detalleBiple = :detalleBiple"),
    @NamedQuery(name = "BisPlanEntrevista.findByActivoBiple", query = "SELECT b FROM BisPlanEntrevista b WHERE b.activoBiple = :activoBiple"),
    @NamedQuery(name = "BisPlanEntrevista.findByUsuarioIngre", query = "SELECT b FROM BisPlanEntrevista b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BisPlanEntrevista.findByFechaIngre", query = "SELECT b FROM BisPlanEntrevista b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BisPlanEntrevista.findByHoraIngre", query = "SELECT b FROM BisPlanEntrevista b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BisPlanEntrevista.findByUsuarioActua", query = "SELECT b FROM BisPlanEntrevista b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BisPlanEntrevista.findByFechaActua", query = "SELECT b FROM BisPlanEntrevista b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BisPlanEntrevista.findByHoraActua", query = "SELECT b FROM BisPlanEntrevista b WHERE b.horaActua = :horaActua")})
public class BisPlanEntrevista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_biple", nullable = false)
    private Integer ideBiple;
    @Size(max = 100)
    @Column(name = "detalle_biple", length = 100)
    private String detalleBiple;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_biple", nullable = false)
    private boolean activoBiple;
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
    @OneToMany(mappedBy = "ideBiple")
    private List<BisEntrevista> bisEntrevistaList;

    public BisPlanEntrevista() {
    }

    public BisPlanEntrevista(Integer ideBiple) {
        this.ideBiple = ideBiple;
    }

    public BisPlanEntrevista(Integer ideBiple, boolean activoBiple) {
        this.ideBiple = ideBiple;
        this.activoBiple = activoBiple;
    }

    public Integer getIdeBiple() {
        return ideBiple;
    }

    public void setIdeBiple(Integer ideBiple) {
        this.ideBiple = ideBiple;
    }

    public String getDetalleBiple() {
        return detalleBiple;
    }

    public void setDetalleBiple(String detalleBiple) {
        this.detalleBiple = detalleBiple;
    }

    public boolean getActivoBiple() {
        return activoBiple;
    }

    public void setActivoBiple(boolean activoBiple) {
        this.activoBiple = activoBiple;
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

    public List<BisEntrevista> getBisEntrevistaList() {
        return bisEntrevistaList;
    }

    public void setBisEntrevistaList(List<BisEntrevista> bisEntrevistaList) {
        this.bisEntrevistaList = bisEntrevistaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBiple != null ? ideBiple.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BisPlanEntrevista)) {
            return false;
        }
        BisPlanEntrevista other = (BisPlanEntrevista) object;
        if ((this.ideBiple == null && other.ideBiple != null) || (this.ideBiple != null && !this.ideBiple.equals(other.ideBiple))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BisPlanEntrevista[ ideBiple=" + ideBiple + " ]";
    }
    
}
