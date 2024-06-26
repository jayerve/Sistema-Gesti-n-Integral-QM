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
@Table(name = "bis_motivo_entrevista", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BisMotivoEntrevista.findAll", query = "SELECT b FROM BisMotivoEntrevista b"),
    @NamedQuery(name = "BisMotivoEntrevista.findByIdeBimoe", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.ideBimoe = :ideBimoe"),
    @NamedQuery(name = "BisMotivoEntrevista.findByDetalleBimoe", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.detalleBimoe = :detalleBimoe"),
    @NamedQuery(name = "BisMotivoEntrevista.findByActivoBimoe", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.activoBimoe = :activoBimoe"),
    @NamedQuery(name = "BisMotivoEntrevista.findByUsuarioIngre", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BisMotivoEntrevista.findByFechaIngre", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BisMotivoEntrevista.findByHoraIngre", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BisMotivoEntrevista.findByUsuarioActua", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BisMotivoEntrevista.findByFechaActua", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BisMotivoEntrevista.findByHoraActua", query = "SELECT b FROM BisMotivoEntrevista b WHERE b.horaActua = :horaActua")})
public class BisMotivoEntrevista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bimoe", nullable = false)
    private Integer ideBimoe;
    @Size(max = 100)
    @Column(name = "detalle_bimoe", length = 100)
    private String detalleBimoe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_bimoe", nullable = false)
    private boolean activoBimoe;
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
    @OneToMany(mappedBy = "ideBimoe")
    private List<BisEntrevista> bisEntrevistaList;

    public BisMotivoEntrevista() {
    }

    public BisMotivoEntrevista(Integer ideBimoe) {
        this.ideBimoe = ideBimoe;
    }

    public BisMotivoEntrevista(Integer ideBimoe, boolean activoBimoe) {
        this.ideBimoe = ideBimoe;
        this.activoBimoe = activoBimoe;
    }

    public Integer getIdeBimoe() {
        return ideBimoe;
    }

    public void setIdeBimoe(Integer ideBimoe) {
        this.ideBimoe = ideBimoe;
    }

    public String getDetalleBimoe() {
        return detalleBimoe;
    }

    public void setDetalleBimoe(String detalleBimoe) {
        this.detalleBimoe = detalleBimoe;
    }

    public boolean getActivoBimoe() {
        return activoBimoe;
    }

    public void setActivoBimoe(boolean activoBimoe) {
        this.activoBimoe = activoBimoe;
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
        hash += (ideBimoe != null ? ideBimoe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BisMotivoEntrevista)) {
            return false;
        }
        BisMotivoEntrevista other = (BisMotivoEntrevista) object;
        if ((this.ideBimoe == null && other.ideBimoe != null) || (this.ideBimoe != null && !this.ideBimoe.equals(other.ideBimoe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BisMotivoEntrevista[ ideBimoe=" + ideBimoe + " ]";
    }
    
}
