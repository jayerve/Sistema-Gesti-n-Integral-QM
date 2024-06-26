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
@Table(name = "sis_perfil_reporte", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPerfilReporte.findAll", query = "SELECT s FROM SisPerfilReporte s"),
    @NamedQuery(name = "SisPerfilReporte.findByIdePere", query = "SELECT s FROM SisPerfilReporte s WHERE s.idePere = :idePere"),
    @NamedQuery(name = "SisPerfilReporte.findByUsuarioIngre", query = "SELECT s FROM SisPerfilReporte s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPerfilReporte.findByFechaIngre", query = "SELECT s FROM SisPerfilReporte s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPerfilReporte.findByUsuarioActua", query = "SELECT s FROM SisPerfilReporte s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPerfilReporte.findByFechaActua", query = "SELECT s FROM SisPerfilReporte s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPerfilReporte.findByHoraIngre", query = "SELECT s FROM SisPerfilReporte s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPerfilReporte.findByHoraActua", query = "SELECT s FROM SisPerfilReporte s WHERE s.horaActua = :horaActua")})
public class SisPerfilReporte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pere", nullable = false)
    private Integer idePere;
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
    @JoinColumn(name = "ide_repo", referencedColumnName = "ide_repo")
    @ManyToOne
    private SisReporte ideRepo;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;

    public SisPerfilReporte() {
    }

    public SisPerfilReporte(Integer idePere) {
        this.idePere = idePere;
    }

    public Integer getIdePere() {
        return idePere;
    }

    public void setIdePere(Integer idePere) {
        this.idePere = idePere;
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

    public SisReporte getIdeRepo() {
        return ideRepo;
    }

    public void setIdeRepo(SisReporte ideRepo) {
        this.ideRepo = ideRepo;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePere != null ? idePere.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilReporte)) {
            return false;
        }
        SisPerfilReporte other = (SisPerfilReporte) object;
        if ((this.idePere == null && other.idePere != null) || (this.idePere != null && !this.idePere.equals(other.idePere))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilReporte[ idePere=" + idePere + " ]";
    }
    
}
