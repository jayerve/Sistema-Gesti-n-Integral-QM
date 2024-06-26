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
@Table(name = "sis_perfil", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPerfil.findAll", query = "SELECT s FROM SisPerfil s"),
    @NamedQuery(name = "SisPerfil.findByIdePerf", query = "SELECT s FROM SisPerfil s WHERE s.idePerf = :idePerf"),
    @NamedQuery(name = "SisPerfil.findByNomPerf", query = "SELECT s FROM SisPerfil s WHERE s.nomPerf = :nomPerf"),
    @NamedQuery(name = "SisPerfil.findByDescripcionPerf", query = "SELECT s FROM SisPerfil s WHERE s.descripcionPerf = :descripcionPerf"),
    @NamedQuery(name = "SisPerfil.findByActivoPerf", query = "SELECT s FROM SisPerfil s WHERE s.activoPerf = :activoPerf"),
    @NamedQuery(name = "SisPerfil.findByUsuarioIngre", query = "SELECT s FROM SisPerfil s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPerfil.findByFechaIngre", query = "SELECT s FROM SisPerfil s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPerfil.findByUsuarioActua", query = "SELECT s FROM SisPerfil s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPerfil.findByFechaActua", query = "SELECT s FROM SisPerfil s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPerfil.findByHoraIngre", query = "SELECT s FROM SisPerfil s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPerfil.findByHoraActua", query = "SELECT s FROM SisPerfil s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisPerfil.findByPermUtilPerf", query = "SELECT s FROM SisPerfil s WHERE s.permUtilPerf = :permUtilPerf")})
public class SisPerfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_perf", nullable = false)
    private Integer idePerf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_perf", nullable = false, length = 50)
    private String nomPerf;
    @Size(max = 190)
    @Column(name = "descripcion_perf", length = 190)
    private String descripcionPerf;
    @Column(name = "activo_perf")
    private Boolean activoPerf;
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
    @Column(name = "perm_util_perf")
    private Boolean permUtilPerf;
    @OneToMany(mappedBy = "idePerf")
    private List<SisPerfilReporte> sisPerfilReporteList;
    @OneToMany(mappedBy = "idePerf")
    private List<SisPerfilObjeto> sisPerfilObjetoList;
    @OneToMany(mappedBy = "idePerf")
    private List<SisPerfilCampo> sisPerfilCampoList;
    @OneToMany(mappedBy = "idePerf")
    private List<SisUsuario> sisUsuarioList;
    @OneToMany(mappedBy = "idePerf")
    private List<SisPerfilOpcion> sisPerfilOpcionList;

    public SisPerfil() {
    }

    public SisPerfil(Integer idePerf) {
        this.idePerf = idePerf;
    }

    public SisPerfil(Integer idePerf, String nomPerf) {
        this.idePerf = idePerf;
        this.nomPerf = nomPerf;
    }

    public Integer getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(Integer idePerf) {
        this.idePerf = idePerf;
    }

    public String getNomPerf() {
        return nomPerf;
    }

    public void setNomPerf(String nomPerf) {
        this.nomPerf = nomPerf;
    }

    public String getDescripcionPerf() {
        return descripcionPerf;
    }

    public void setDescripcionPerf(String descripcionPerf) {
        this.descripcionPerf = descripcionPerf;
    }

    public Boolean getActivoPerf() {
        return activoPerf;
    }

    public void setActivoPerf(Boolean activoPerf) {
        this.activoPerf = activoPerf;
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

    public Boolean getPermUtilPerf() {
        return permUtilPerf;
    }

    public void setPermUtilPerf(Boolean permUtilPerf) {
        this.permUtilPerf = permUtilPerf;
    }

    public List<SisPerfilReporte> getSisPerfilReporteList() {
        return sisPerfilReporteList;
    }

    public void setSisPerfilReporteList(List<SisPerfilReporte> sisPerfilReporteList) {
        this.sisPerfilReporteList = sisPerfilReporteList;
    }

    public List<SisPerfilObjeto> getSisPerfilObjetoList() {
        return sisPerfilObjetoList;
    }

    public void setSisPerfilObjetoList(List<SisPerfilObjeto> sisPerfilObjetoList) {
        this.sisPerfilObjetoList = sisPerfilObjetoList;
    }

    public List<SisPerfilCampo> getSisPerfilCampoList() {
        return sisPerfilCampoList;
    }

    public void setSisPerfilCampoList(List<SisPerfilCampo> sisPerfilCampoList) {
        this.sisPerfilCampoList = sisPerfilCampoList;
    }

    public List<SisUsuario> getSisUsuarioList() {
        return sisUsuarioList;
    }

    public void setSisUsuarioList(List<SisUsuario> sisUsuarioList) {
        this.sisUsuarioList = sisUsuarioList;
    }

    public List<SisPerfilOpcion> getSisPerfilOpcionList() {
        return sisPerfilOpcionList;
    }

    public void setSisPerfilOpcionList(List<SisPerfilOpcion> sisPerfilOpcionList) {
        this.sisPerfilOpcionList = sisPerfilOpcionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePerf != null ? idePerf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfil)) {
            return false;
        }
        SisPerfil other = (SisPerfil) object;
        if ((this.idePerf == null && other.idePerf != null) || (this.idePerf != null && !this.idePerf.equals(other.idePerf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfil[ idePerf=" + idePerf + " ]";
    }
    
}
