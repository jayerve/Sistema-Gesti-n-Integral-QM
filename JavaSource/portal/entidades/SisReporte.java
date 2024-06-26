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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sis_reporte", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisReporte.findAll", query = "SELECT s FROM SisReporte s"),
    @NamedQuery(name = "SisReporte.findByIdeRepo", query = "SELECT s FROM SisReporte s WHERE s.ideRepo = :ideRepo"),
    @NamedQuery(name = "SisReporte.findByNomRepo", query = "SELECT s FROM SisReporte s WHERE s.nomRepo = :nomRepo"),
    @NamedQuery(name = "SisReporte.findByPathRepo", query = "SELECT s FROM SisReporte s WHERE s.pathRepo = :pathRepo"),
    @NamedQuery(name = "SisReporte.findByUsuarioIngre", query = "SELECT s FROM SisReporte s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisReporte.findByFechaIngre", query = "SELECT s FROM SisReporte s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisReporte.findByUsuarioActua", query = "SELECT s FROM SisReporte s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisReporte.findByFechaActua", query = "SELECT s FROM SisReporte s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisReporte.findByHoraIngre", query = "SELECT s FROM SisReporte s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisReporte.findByHoraActua", query = "SELECT s FROM SisReporte s WHERE s.horaActua = :horaActua")})
public class SisReporte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_repo", nullable = false)
    private Integer ideRepo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nom_repo", nullable = false, length = 80)
    private String nomRepo;
    @Size(max = 150)
    @Column(name = "path_repo", length = 150)
    private String pathRepo;
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
    @OneToMany(mappedBy = "ideRepo")
    private List<SisPerfilReporte> sisPerfilReporteList;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;
    @OneToMany(mappedBy = "ideRepo")
    private List<CrtActividad> crtActividadList;

    public SisReporte() {
    }

    public SisReporte(Integer ideRepo) {
        this.ideRepo = ideRepo;
    }

    public SisReporte(Integer ideRepo, String nomRepo) {
        this.ideRepo = ideRepo;
        this.nomRepo = nomRepo;
    }

    public Integer getIdeRepo() {
        return ideRepo;
    }

    public void setIdeRepo(Integer ideRepo) {
        this.ideRepo = ideRepo;
    }

    public String getNomRepo() {
        return nomRepo;
    }

    public void setNomRepo(String nomRepo) {
        this.nomRepo = nomRepo;
    }

    public String getPathRepo() {
        return pathRepo;
    }

    public void setPathRepo(String pathRepo) {
        this.pathRepo = pathRepo;
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

    public List<SisPerfilReporte> getSisPerfilReporteList() {
        return sisPerfilReporteList;
    }

    public void setSisPerfilReporteList(List<SisPerfilReporte> sisPerfilReporteList) {
        this.sisPerfilReporteList = sisPerfilReporteList;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    public List<CrtActividad> getCrtActividadList() {
        return crtActividadList;
    }

    public void setCrtActividadList(List<CrtActividad> crtActividadList) {
        this.crtActividadList = crtActividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRepo != null ? ideRepo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisReporte)) {
            return false;
        }
        SisReporte other = (SisReporte) object;
        if ((this.ideRepo == null && other.ideRepo != null) || (this.ideRepo != null && !this.ideRepo.equals(other.ideRepo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisReporte[ ideRepo=" + ideRepo + " ]";
    }
    
}
