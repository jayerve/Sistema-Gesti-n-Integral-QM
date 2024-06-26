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
@Table(name = "pre_nivel_funcion_programa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreNivelFuncionPrograma.findAll", query = "SELECT p FROM PreNivelFuncionPrograma p"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByIdePrnfp", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.idePrnfp = :idePrnfp"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByDetallePrnfp", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.detallePrnfp = :detallePrnfp"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByActivoPrnfp", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.activoPrnfp = :activoPrnfp"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByUsuarioIngre", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByFechaIngre", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByHoraIngre", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByUsuarioActua", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByFechaActua", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreNivelFuncionPrograma.findByHoraActua", query = "SELECT p FROM PreNivelFuncionPrograma p WHERE p.horaActua = :horaActua")})
public class PreNivelFuncionPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prnfp", nullable = false)
    private Long idePrnfp;
    @Size(max = 50)
    @Column(name = "detalle_prnfp", length = 50)
    private String detallePrnfp;
    @Column(name = "activo_prnfp")
    private Boolean activoPrnfp;
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
    @OneToMany(mappedBy = "idePrnfp")
    private List<PreFuncionPrograma> preFuncionProgramaList;

    public PreNivelFuncionPrograma() {
    }

    public PreNivelFuncionPrograma(Long idePrnfp) {
        this.idePrnfp = idePrnfp;
    }

    public Long getIdePrnfp() {
        return idePrnfp;
    }

    public void setIdePrnfp(Long idePrnfp) {
        this.idePrnfp = idePrnfp;
    }

    public String getDetallePrnfp() {
        return detallePrnfp;
    }

    public void setDetallePrnfp(String detallePrnfp) {
        this.detallePrnfp = detallePrnfp;
    }

    public Boolean getActivoPrnfp() {
        return activoPrnfp;
    }

    public void setActivoPrnfp(Boolean activoPrnfp) {
        this.activoPrnfp = activoPrnfp;
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

    public List<PreFuncionPrograma> getPreFuncionProgramaList() {
        return preFuncionProgramaList;
    }

    public void setPreFuncionProgramaList(List<PreFuncionPrograma> preFuncionProgramaList) {
        this.preFuncionProgramaList = preFuncionProgramaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrnfp != null ? idePrnfp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreNivelFuncionPrograma)) {
            return false;
        }
        PreNivelFuncionPrograma other = (PreNivelFuncionPrograma) object;
        if ((this.idePrnfp == null && other.idePrnfp != null) || (this.idePrnfp != null && !this.idePrnfp.equals(other.idePrnfp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreNivelFuncionPrograma[ idePrnfp=" + idePrnfp + " ]";
    }
    
}
