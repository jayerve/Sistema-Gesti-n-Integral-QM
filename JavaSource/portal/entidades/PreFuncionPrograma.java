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
@Table(name = "pre_funcion_programa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreFuncionPrograma.findAll", query = "SELECT p FROM PreFuncionPrograma p"),
    @NamedQuery(name = "PreFuncionPrograma.findByIdePrfup", query = "SELECT p FROM PreFuncionPrograma p WHERE p.idePrfup = :idePrfup"),
    @NamedQuery(name = "PreFuncionPrograma.findByDetallePrfup", query = "SELECT p FROM PreFuncionPrograma p WHERE p.detallePrfup = :detallePrfup"),
    @NamedQuery(name = "PreFuncionPrograma.findByCodigoPrfup", query = "SELECT p FROM PreFuncionPrograma p WHERE p.codigoPrfup = :codigoPrfup"),
    @NamedQuery(name = "PreFuncionPrograma.findByActivoPrfup", query = "SELECT p FROM PreFuncionPrograma p WHERE p.activoPrfup = :activoPrfup"),
    @NamedQuery(name = "PreFuncionPrograma.findByUsuarioIngre", query = "SELECT p FROM PreFuncionPrograma p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreFuncionPrograma.findByFechaIngre", query = "SELECT p FROM PreFuncionPrograma p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreFuncionPrograma.findByHoraIngre", query = "SELECT p FROM PreFuncionPrograma p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreFuncionPrograma.findByUsuarioActua", query = "SELECT p FROM PreFuncionPrograma p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreFuncionPrograma.findByFechaActua", query = "SELECT p FROM PreFuncionPrograma p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreFuncionPrograma.findByHoraActua", query = "SELECT p FROM PreFuncionPrograma p WHERE p.horaActua = :horaActua")})
public class PreFuncionPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prfup", nullable = false)
    private Long idePrfup;
    @Size(max = 50)
    @Column(name = "detalle_prfup", length = 50)
    private String detallePrfup;
    @Size(max = 50)
    @Column(name = "codigo_prfup", length = 50)
    private String codigoPrfup;
    @Column(name = "activo_prfup")
    private Boolean activoPrfup;
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
    @OneToMany(mappedBy = "idePrfup")
    private List<PrePrograma> preProgramaList;
    @JoinColumn(name = "ide_prnfp", referencedColumnName = "ide_prnfp")
    @ManyToOne
    private PreNivelFuncionPrograma idePrnfp;
    @OneToMany(mappedBy = "preIdePrfup")
    private List<PreFuncionPrograma> preFuncionProgramaList;
    @JoinColumn(name = "pre_ide_prfup", referencedColumnName = "ide_prfup")
    @ManyToOne
    private PreFuncionPrograma preIdePrfup;
    @OneToMany(mappedBy = "idePrfup")
    private List<PreAnual> preAnualList;
    @OneToMany(mappedBy = "idePrfup")
    private List<ContVigente> contVigenteList;
    @OneToMany(mappedBy = "idePrfup")
    private List<PrePoa> prePoaList;

    public PreFuncionPrograma() {
    }

    public PreFuncionPrograma(Long idePrfup) {
        this.idePrfup = idePrfup;
    }

    public Long getIdePrfup() {
        return idePrfup;
    }

    public void setIdePrfup(Long idePrfup) {
        this.idePrfup = idePrfup;
    }

    public String getDetallePrfup() {
        return detallePrfup;
    }

    public void setDetallePrfup(String detallePrfup) {
        this.detallePrfup = detallePrfup;
    }

    public String getCodigoPrfup() {
        return codigoPrfup;
    }

    public void setCodigoPrfup(String codigoPrfup) {
        this.codigoPrfup = codigoPrfup;
    }

    public Boolean getActivoPrfup() {
        return activoPrfup;
    }

    public void setActivoPrfup(Boolean activoPrfup) {
        this.activoPrfup = activoPrfup;
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

    public List<PrePrograma> getPreProgramaList() {
        return preProgramaList;
    }

    public void setPreProgramaList(List<PrePrograma> preProgramaList) {
        this.preProgramaList = preProgramaList;
    }

    public PreNivelFuncionPrograma getIdePrnfp() {
        return idePrnfp;
    }

    public void setIdePrnfp(PreNivelFuncionPrograma idePrnfp) {
        this.idePrnfp = idePrnfp;
    }

    public List<PreFuncionPrograma> getPreFuncionProgramaList() {
        return preFuncionProgramaList;
    }

    public void setPreFuncionProgramaList(List<PreFuncionPrograma> preFuncionProgramaList) {
        this.preFuncionProgramaList = preFuncionProgramaList;
    }

    public PreFuncionPrograma getPreIdePrfup() {
        return preIdePrfup;
    }

    public void setPreIdePrfup(PreFuncionPrograma preIdePrfup) {
        this.preIdePrfup = preIdePrfup;
    }

    public List<PreAnual> getPreAnualList() {
        return preAnualList;
    }

    public void setPreAnualList(List<PreAnual> preAnualList) {
        this.preAnualList = preAnualList;
    }

    public List<ContVigente> getContVigenteList() {
        return contVigenteList;
    }

    public void setContVigenteList(List<ContVigente> contVigenteList) {
        this.contVigenteList = contVigenteList;
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
        hash += (idePrfup != null ? idePrfup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreFuncionPrograma)) {
            return false;
        }
        PreFuncionPrograma other = (PreFuncionPrograma) object;
        if ((this.idePrfup == null && other.idePrfup != null) || (this.idePrfup != null && !this.idePrfup.equals(other.idePrfup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreFuncionPrograma[ idePrfup=" + idePrfup + " ]";
    }
    
}
