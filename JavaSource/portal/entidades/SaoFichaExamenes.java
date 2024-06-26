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
@Table(name = "sao_ficha_examenes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFichaExamenes.findAll", query = "SELECT s FROM SaoFichaExamenes s"),
    @NamedQuery(name = "SaoFichaExamenes.findByIdeSafie", query = "SELECT s FROM SaoFichaExamenes s WHERE s.ideSafie = :ideSafie"),
    @NamedQuery(name = "SaoFichaExamenes.findByActivoSafie", query = "SELECT s FROM SaoFichaExamenes s WHERE s.activoSafie = :activoSafie"),
    @NamedQuery(name = "SaoFichaExamenes.findByUsuarioIngre", query = "SELECT s FROM SaoFichaExamenes s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFichaExamenes.findByFechaIngre", query = "SELECT s FROM SaoFichaExamenes s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFichaExamenes.findByUsuarioActua", query = "SELECT s FROM SaoFichaExamenes s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFichaExamenes.findByFechaActua", query = "SELECT s FROM SaoFichaExamenes s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFichaExamenes.findByHoraIngre", query = "SELECT s FROM SaoFichaExamenes s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoFichaExamenes.findByHoraActua", query = "SELECT s FROM SaoFichaExamenes s WHERE s.horaActua = :horaActua")})
public class SaoFichaExamenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safie", nullable = false)
    private Integer ideSafie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safie", nullable = false)
    private boolean activoSafie;
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
    @JoinColumn(name = "ide_safim", referencedColumnName = "ide_safim")
    @ManyToOne
    private SaoFichaMedica ideSafim;
    @JoinColumn(name = "ide_saexa", referencedColumnName = "ide_saexa")
    @ManyToOne
    private SaoExamenes ideSaexa;

    public SaoFichaExamenes() {
    }

    public SaoFichaExamenes(Integer ideSafie) {
        this.ideSafie = ideSafie;
    }

    public SaoFichaExamenes(Integer ideSafie, boolean activoSafie) {
        this.ideSafie = ideSafie;
        this.activoSafie = activoSafie;
    }

    public Integer getIdeSafie() {
        return ideSafie;
    }

    public void setIdeSafie(Integer ideSafie) {
        this.ideSafie = ideSafie;
    }

    public boolean getActivoSafie() {
        return activoSafie;
    }

    public void setActivoSafie(boolean activoSafie) {
        this.activoSafie = activoSafie;
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

    public SaoFichaMedica getIdeSafim() {
        return ideSafim;
    }

    public void setIdeSafim(SaoFichaMedica ideSafim) {
        this.ideSafim = ideSafim;
    }

    public SaoExamenes getIdeSaexa() {
        return ideSaexa;
    }

    public void setIdeSaexa(SaoExamenes ideSaexa) {
        this.ideSaexa = ideSaexa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafie != null ? ideSafie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFichaExamenes)) {
            return false;
        }
        SaoFichaExamenes other = (SaoFichaExamenes) object;
        if ((this.ideSafie == null && other.ideSafie != null) || (this.ideSafie != null && !this.ideSafie.equals(other.ideSafie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFichaExamenes[ ideSafie=" + ideSafie + " ]";
    }
    
}
