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
@Table(name = "pre_grupo_economico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreGrupoEconomico.findAll", query = "SELECT p FROM PreGrupoEconomico p"),
    @NamedQuery(name = "PreGrupoEconomico.findByIdePrgre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.idePrgre = :idePrgre"),
    @NamedQuery(name = "PreGrupoEconomico.findByDetallePrgre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.detallePrgre = :detallePrgre"),
    @NamedQuery(name = "PreGrupoEconomico.findByActivoPrgre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.activoPrgre = :activoPrgre"),
    @NamedQuery(name = "PreGrupoEconomico.findByUsuarioIngre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreGrupoEconomico.findByFechaIngre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreGrupoEconomico.findByHoraIngre", query = "SELECT p FROM PreGrupoEconomico p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreGrupoEconomico.findByUsuarioActua", query = "SELECT p FROM PreGrupoEconomico p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreGrupoEconomico.findByFechaActua", query = "SELECT p FROM PreGrupoEconomico p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreGrupoEconomico.findByHoraActua", query = "SELECT p FROM PreGrupoEconomico p WHERE p.horaActua = :horaActua")})
public class PreGrupoEconomico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prgre", nullable = false)
    private Long idePrgre;
    @Size(max = 100)
    @Column(name = "detalle_prgre", length = 100)
    private String detallePrgre;
    @Column(name = "activo_prgre")
    private Boolean activoPrgre;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "idePrgre")
    private List<PreClasificador> preClasificadorList;

    public PreGrupoEconomico() {
    }

    public PreGrupoEconomico(Long idePrgre) {
        this.idePrgre = idePrgre;
    }

    public Long getIdePrgre() {
        return idePrgre;
    }

    public void setIdePrgre(Long idePrgre) {
        this.idePrgre = idePrgre;
    }

    public String getDetallePrgre() {
        return detallePrgre;
    }

    public void setDetallePrgre(String detallePrgre) {
        this.detallePrgre = detallePrgre;
    }

    public Boolean getActivoPrgre() {
        return activoPrgre;
    }

    public void setActivoPrgre(Boolean activoPrgre) {
        this.activoPrgre = activoPrgre;
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

    public List<PreClasificador> getPreClasificadorList() {
        return preClasificadorList;
    }

    public void setPreClasificadorList(List<PreClasificador> preClasificadorList) {
        this.preClasificadorList = preClasificadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrgre != null ? idePrgre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreGrupoEconomico)) {
            return false;
        }
        PreGrupoEconomico other = (PreGrupoEconomico) object;
        if ((this.idePrgre == null && other.idePrgre != null) || (this.idePrgre != null && !this.idePrgre.equals(other.idePrgre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreGrupoEconomico[ idePrgre=" + idePrgre + " ]";
    }

}
