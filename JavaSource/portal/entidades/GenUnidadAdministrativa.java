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
@Table(name = "gen_unidad_administrativa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenUnidadAdministrativa.findAll", query = "SELECT g FROM GenUnidadAdministrativa g"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByIdeGeuna", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.ideGeuna = :ideGeuna"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByDetalleGeuna", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.detalleGeuna = :detalleGeuna"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByActivoGeuna", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.activoGeuna = :activoGeuna"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByUsuarioIngre", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByFechaIngre", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByUsuarioActua", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByFechaActua", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByHoraIngre", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenUnidadAdministrativa.findByHoraActua", query = "SELECT g FROM GenUnidadAdministrativa g WHERE g.horaActua = :horaActua")})
public class GenUnidadAdministrativa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geuna", nullable = false)
    private Integer ideGeuna;
    @Size(max = 50)
    @Column(name = "detalle_geuna", length = 50)
    private String detalleGeuna;
    @Column(name = "activo_geuna")
    private Boolean activoGeuna;
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
    @OneToMany(mappedBy = "ideGeuna")
    private List<CppPerfil> cppPerfilList;
    @OneToMany(mappedBy = "ideGeuna")
    private List<PrePoa> prePoaList;

    public GenUnidadAdministrativa() {
    }

    public GenUnidadAdministrativa(Integer ideGeuna) {
        this.ideGeuna = ideGeuna;
    }

    public Integer getIdeGeuna() {
        return ideGeuna;
    }

    public void setIdeGeuna(Integer ideGeuna) {
        this.ideGeuna = ideGeuna;
    }

    public String getDetalleGeuna() {
        return detalleGeuna;
    }

    public void setDetalleGeuna(String detalleGeuna) {
        this.detalleGeuna = detalleGeuna;
    }

    public Boolean getActivoGeuna() {
        return activoGeuna;
    }

    public void setActivoGeuna(Boolean activoGeuna) {
        this.activoGeuna = activoGeuna;
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

    public List<CppPerfil> getCppPerfilList() {
        return cppPerfilList;
    }

    public void setCppPerfilList(List<CppPerfil> cppPerfilList) {
        this.cppPerfilList = cppPerfilList;
    }

    public List<PrePoa> getPrePoaList() {
        return prePoaList;
    }

    public void setPrePoaList(List<PrePoa> prePoaList) {
        this.prePoaList = prePoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeuna != null ? ideGeuna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenUnidadAdministrativa)) {
            return false;
        }
        GenUnidadAdministrativa other = (GenUnidadAdministrativa) object;
        if ((this.ideGeuna == null && other.ideGeuna != null) || (this.ideGeuna != null && !this.ideGeuna.equals(other.ideGeuna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenUnidadAdministrativa[ ideGeuna=" + ideGeuna + " ]";
    }
    
}
