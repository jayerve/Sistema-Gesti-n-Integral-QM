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
@Table(name = "sao_ficha_anamnesis", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFichaAnamnesis.findAll", query = "SELECT s FROM SaoFichaAnamnesis s"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByIdeSafia", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.ideSafia = :ideSafia"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByDetalleSafia", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.detalleSafia = :detalleSafia"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByActivoSafia", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.activoSafia = :activoSafia"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByUsuarioIngre", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByFechaIngre", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByUsuarioActua", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByFechaActua", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByHoraIngre", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoFichaAnamnesis.findByHoraActua", query = "SELECT s FROM SaoFichaAnamnesis s WHERE s.horaActua = :horaActua")})
public class SaoFichaAnamnesis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safia", nullable = false)
    private Integer ideSafia;
    @Size(max = 1000)
    @Column(name = "detalle_safia", length = 1000)
    private String detalleSafia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safia", nullable = false)
    private boolean activoSafia;
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
    @JoinColumn(name = "ide_saana", referencedColumnName = "ide_saana")
    @ManyToOne
    private SaoAnamnesis ideSaana;

    public SaoFichaAnamnesis() {
    }

    public SaoFichaAnamnesis(Integer ideSafia) {
        this.ideSafia = ideSafia;
    }

    public SaoFichaAnamnesis(Integer ideSafia, boolean activoSafia) {
        this.ideSafia = ideSafia;
        this.activoSafia = activoSafia;
    }

    public Integer getIdeSafia() {
        return ideSafia;
    }

    public void setIdeSafia(Integer ideSafia) {
        this.ideSafia = ideSafia;
    }

    public String getDetalleSafia() {
        return detalleSafia;
    }

    public void setDetalleSafia(String detalleSafia) {
        this.detalleSafia = detalleSafia;
    }

    public boolean getActivoSafia() {
        return activoSafia;
    }

    public void setActivoSafia(boolean activoSafia) {
        this.activoSafia = activoSafia;
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

    public SaoAnamnesis getIdeSaana() {
        return ideSaana;
    }

    public void setIdeSaana(SaoAnamnesis ideSaana) {
        this.ideSaana = ideSaana;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafia != null ? ideSafia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFichaAnamnesis)) {
            return false;
        }
        SaoFichaAnamnesis other = (SaoFichaAnamnesis) object;
        if ((this.ideSafia == null && other.ideSafia != null) || (this.ideSafia != null && !this.ideSafia.equals(other.ideSafia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFichaAnamnesis[ ideSafia=" + ideSafia + " ]";
    }
    
}
