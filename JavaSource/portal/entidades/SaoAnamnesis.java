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
@Table(name = "sao_anamnesis", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoAnamnesis.findAll", query = "SELECT s FROM SaoAnamnesis s"),
    @NamedQuery(name = "SaoAnamnesis.findByIdeSaana", query = "SELECT s FROM SaoAnamnesis s WHERE s.ideSaana = :ideSaana"),
    @NamedQuery(name = "SaoAnamnesis.findByDetalleSaana", query = "SELECT s FROM SaoAnamnesis s WHERE s.detalleSaana = :detalleSaana"),
    @NamedQuery(name = "SaoAnamnesis.findByActivoSaana", query = "SELECT s FROM SaoAnamnesis s WHERE s.activoSaana = :activoSaana"),
    @NamedQuery(name = "SaoAnamnesis.findByUsuarioIngre", query = "SELECT s FROM SaoAnamnesis s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoAnamnesis.findByFechaIngre", query = "SELECT s FROM SaoAnamnesis s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoAnamnesis.findByUsuarioActua", query = "SELECT s FROM SaoAnamnesis s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoAnamnesis.findByFechaActua", query = "SELECT s FROM SaoAnamnesis s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoAnamnesis.findByHoraActua", query = "SELECT s FROM SaoAnamnesis s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SaoAnamnesis.findByHoraIngre", query = "SELECT s FROM SaoAnamnesis s WHERE s.horaIngre = :horaIngre")})
public class SaoAnamnesis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saana", nullable = false)
    private Integer ideSaana;
    @Size(max = 100)
    @Column(name = "detalle_saana", length = 100)
    private String detalleSaana;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saana", nullable = false)
    private boolean activoSaana;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @OneToMany(mappedBy = "ideSaana")
    private List<SaoFichaAnamnesis> saoFichaAnamnesisList;

    public SaoAnamnesis() {
    }

    public SaoAnamnesis(Integer ideSaana) {
        this.ideSaana = ideSaana;
    }

    public SaoAnamnesis(Integer ideSaana, boolean activoSaana) {
        this.ideSaana = ideSaana;
        this.activoSaana = activoSaana;
    }

    public Integer getIdeSaana() {
        return ideSaana;
    }

    public void setIdeSaana(Integer ideSaana) {
        this.ideSaana = ideSaana;
    }

    public String getDetalleSaana() {
        return detalleSaana;
    }

    public void setDetalleSaana(String detalleSaana) {
        this.detalleSaana = detalleSaana;
    }

    public boolean getActivoSaana() {
        return activoSaana;
    }

    public void setActivoSaana(boolean activoSaana) {
        this.activoSaana = activoSaana;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public List<SaoFichaAnamnesis> getSaoFichaAnamnesisList() {
        return saoFichaAnamnesisList;
    }

    public void setSaoFichaAnamnesisList(List<SaoFichaAnamnesis> saoFichaAnamnesisList) {
        this.saoFichaAnamnesisList = saoFichaAnamnesisList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaana != null ? ideSaana.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoAnamnesis)) {
            return false;
        }
        SaoAnamnesis other = (SaoAnamnesis) object;
        if ((this.ideSaana == null && other.ideSaana != null) || (this.ideSaana != null && !this.ideSaana.equals(other.ideSaana))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoAnamnesis[ ideSaana=" + ideSaana + " ]";
    }
    
}
