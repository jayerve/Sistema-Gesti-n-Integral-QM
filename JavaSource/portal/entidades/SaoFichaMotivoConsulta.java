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
@Table(name = "sao_ficha_motivo_consulta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFichaMotivoConsulta.findAll", query = "SELECT s FROM SaoFichaMotivoConsulta s"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByIdeSafmc", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.ideSafmc = :ideSafmc"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByActivoSafmc", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.activoSafmc = :activoSafmc"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByUsuarioIngre", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByFechaIngre", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByUsuarioActua", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByFechaActua", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByHoraIngre", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoFichaMotivoConsulta.findByHoraActua", query = "SELECT s FROM SaoFichaMotivoConsulta s WHERE s.horaActua = :horaActua")})
public class SaoFichaMotivoConsulta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safmc", nullable = false)
    private Integer ideSafmc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safmc", nullable = false)
    private boolean activoSafmc;
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
    @JoinColumn(name = "ide_samoc", referencedColumnName = "ide_samoc")
    @ManyToOne
    private SaoMotivoConsulta ideSamoc;
    @JoinColumn(name = "ide_safim", referencedColumnName = "ide_safim")
    @ManyToOne
    private SaoFichaMedica ideSafim;

    public SaoFichaMotivoConsulta() {
    }

    public SaoFichaMotivoConsulta(Integer ideSafmc) {
        this.ideSafmc = ideSafmc;
    }

    public SaoFichaMotivoConsulta(Integer ideSafmc, boolean activoSafmc) {
        this.ideSafmc = ideSafmc;
        this.activoSafmc = activoSafmc;
    }

    public Integer getIdeSafmc() {
        return ideSafmc;
    }

    public void setIdeSafmc(Integer ideSafmc) {
        this.ideSafmc = ideSafmc;
    }

    public boolean getActivoSafmc() {
        return activoSafmc;
    }

    public void setActivoSafmc(boolean activoSafmc) {
        this.activoSafmc = activoSafmc;
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

    public SaoMotivoConsulta getIdeSamoc() {
        return ideSamoc;
    }

    public void setIdeSamoc(SaoMotivoConsulta ideSamoc) {
        this.ideSamoc = ideSamoc;
    }

    public SaoFichaMedica getIdeSafim() {
        return ideSafim;
    }

    public void setIdeSafim(SaoFichaMedica ideSafim) {
        this.ideSafim = ideSafim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafmc != null ? ideSafmc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFichaMotivoConsulta)) {
            return false;
        }
        SaoFichaMotivoConsulta other = (SaoFichaMotivoConsulta) object;
        if ((this.ideSafmc == null && other.ideSafmc != null) || (this.ideSafmc != null && !this.ideSafmc.equals(other.ideSafmc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFichaMotivoConsulta[ ideSafmc=" + ideSafmc + " ]";
    }
    
}
