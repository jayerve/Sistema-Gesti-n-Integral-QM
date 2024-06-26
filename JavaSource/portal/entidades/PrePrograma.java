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
@Table(name = "pre_programa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePrograma.findAll", query = "SELECT p FROM PrePrograma p"),
    @NamedQuery(name = "PrePrograma.findByIdePrpro", query = "SELECT p FROM PrePrograma p WHERE p.idePrpro = :idePrpro"),
    @NamedQuery(name = "PrePrograma.findByCodProgramaPrpro", query = "SELECT p FROM PrePrograma p WHERE p.codProgramaPrpro = :codProgramaPrpro"),
    @NamedQuery(name = "PrePrograma.findByActivoPrpro", query = "SELECT p FROM PrePrograma p WHERE p.activoPrpro = :activoPrpro"),
    @NamedQuery(name = "PrePrograma.findByUsuarioIngre", query = "SELECT p FROM PrePrograma p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePrograma.findByFechaIngre", query = "SELECT p FROM PrePrograma p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePrograma.findByHoraIngre", query = "SELECT p FROM PrePrograma p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePrograma.findByUsuarioActua", query = "SELECT p FROM PrePrograma p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePrograma.findByFechaActua", query = "SELECT p FROM PrePrograma p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePrograma.findByHoraActua", query = "SELECT p FROM PrePrograma p WHERE p.horaActua = :horaActua")})
public class PrePrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpro", nullable = false)
    private Long idePrpro;
    @Size(max = 50)
    @Column(name = "cod_programa_prpro", length = 50)
    private String codProgramaPrpro;
    @Column(name = "activo_prpro")
    private Boolean activoPrpro;
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
    @JoinColumn(name = "ide_prfup", referencedColumnName = "ide_prfup")
    @ManyToOne
    private PreFuncionPrograma idePrfup;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @OneToMany(mappedBy = "idePrpro")
    private List<PreContratacionPartida> preContratacionPartidaList;
    @OneToMany(mappedBy = "idePrpro")
    private List<PreAnual> preAnualList;
    @OneToMany(mappedBy = "idePrpro")
    private List<ContVigente> contVigenteList;

    public PrePrograma() {
    }

    public PrePrograma(Long idePrpro) {
        this.idePrpro = idePrpro;
    }

    public Long getIdePrpro() {
        return idePrpro;
    }

    public void setIdePrpro(Long idePrpro) {
        this.idePrpro = idePrpro;
    }

    public String getCodProgramaPrpro() {
        return codProgramaPrpro;
    }

    public void setCodProgramaPrpro(String codProgramaPrpro) {
        this.codProgramaPrpro = codProgramaPrpro;
    }

    public Boolean getActivoPrpro() {
        return activoPrpro;
    }

    public void setActivoPrpro(Boolean activoPrpro) {
        this.activoPrpro = activoPrpro;
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

    public PreFuncionPrograma getIdePrfup() {
        return idePrfup;
    }

    public void setIdePrfup(PreFuncionPrograma idePrfup) {
        this.idePrfup = idePrfup;
    }

    public PreClasificador getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(PreClasificador idePrcla) {
        this.idePrcla = idePrcla;
    }

    public List<PreContratacionPartida> getPreContratacionPartidaList() {
        return preContratacionPartidaList;
    }

    public void setPreContratacionPartidaList(List<PreContratacionPartida> preContratacionPartidaList) {
        this.preContratacionPartidaList = preContratacionPartidaList;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpro != null ? idePrpro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePrograma)) {
            return false;
        }
        PrePrograma other = (PrePrograma) object;
        if ((this.idePrpro == null && other.idePrpro != null) || (this.idePrpro != null && !this.idePrpro.equals(other.idePrpro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePrograma[ idePrpro=" + idePrpro + " ]";
    }
    
}
