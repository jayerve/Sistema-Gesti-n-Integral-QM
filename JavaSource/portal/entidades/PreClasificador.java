/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "pre_clasificador", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreClasificador.findAll", query = "SELECT p FROM PreClasificador p"),
    @NamedQuery(name = "PreClasificador.findByIdePrcla", query = "SELECT p FROM PreClasificador p WHERE p.idePrcla = :idePrcla"),
    @NamedQuery(name = "PreClasificador.findByCodigoClasificadorPrcla", query = "SELECT p FROM PreClasificador p WHERE p.codigoClasificadorPrcla = :codigoClasificadorPrcla"),
    @NamedQuery(name = "PreClasificador.findByDescripcionClasificadorPrcla", query = "SELECT p FROM PreClasificador p WHERE p.descripcionClasificadorPrcla = :descripcionClasificadorPrcla"),
    @NamedQuery(name = "PreClasificador.findByTipoPrcla", query = "SELECT p FROM PreClasificador p WHERE p.tipoPrcla = :tipoPrcla"),
    @NamedQuery(name = "PreClasificador.findByNivelPrcla", query = "SELECT p FROM PreClasificador p WHERE p.nivelPrcla = :nivelPrcla"),
    @NamedQuery(name = "PreClasificador.findByGrupoPrcla", query = "SELECT p FROM PreClasificador p WHERE p.grupoPrcla = :grupoPrcla"),
    @NamedQuery(name = "PreClasificador.findBySigefcPrcla", query = "SELECT p FROM PreClasificador p WHERE p.sigefcPrcla = :sigefcPrcla"),
    @NamedQuery(name = "PreClasificador.findByActivoPrcla", query = "SELECT p FROM PreClasificador p WHERE p.activoPrcla = :activoPrcla"),
    @NamedQuery(name = "PreClasificador.findByUsuarioIngre", query = "SELECT p FROM PreClasificador p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreClasificador.findByFechaIngre", query = "SELECT p FROM PreClasificador p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreClasificador.findByHoraIngre", query = "SELECT p FROM PreClasificador p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreClasificador.findByUsuarioActua", query = "SELECT p FROM PreClasificador p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreClasificador.findByFechaActua", query = "SELECT p FROM PreClasificador p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreClasificador.findByHoraActua", query = "SELECT p FROM PreClasificador p WHERE p.horaActua = :horaActua")})
public class PreClasificador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prcla", nullable = false)
    private Long idePrcla;
    @Size(max = 50)
    @Column(name = "codigo_clasificador_prcla", length = 50)
    private String codigoClasificadorPrcla;
    @Size(max = 500)
    @Column(name = "descripcion_clasificador_prcla", length = 500)
    private String descripcionClasificadorPrcla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_prcla", nullable = false)
    private long tipoPrcla;
    @Column(name = "nivel_prcla")
    private BigInteger nivelPrcla;
    @Size(max = 50)
    @Column(name = "grupo_prcla", length = 50)
    private String grupoPrcla;
    @Column(name = "sigefc_prcla")
    private Boolean sigefcPrcla;
    @Column(name = "activo_prcla")
    private Boolean activoPrcla;
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
    @OneToMany(mappedBy = "idePrcla")
    private List<PrePrograma> preProgramaList;
    @OneToMany(mappedBy = "idePrcla")
    private List<PrePac> prePacList;
    @OneToMany(mappedBy = "idePrcla")
    private List<PrePartidaPac> prePartidaPacList;
    @JoinColumn(name = "ide_prgre", referencedColumnName = "ide_prgre")
    @ManyToOne
    private PreGrupoEconomico idePrgre;
    @OneToMany(mappedBy = "preIdePrcla")
    private List<PreClasificador> preClasificadorList;
    @JoinColumn(name = "pre_ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador preIdePrcla;
    @OneToMany(mappedBy = "idePrcla")
    private List<PreAnual> preAnualList;
    @OneToMany(mappedBy = "idePrcla")
    private List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList;
    @OneToMany(mappedBy = "idePrcla")
    private List<ContVigente> contVigenteList;
    @OneToMany(mappedBy = "idePrcla")
    private List<PrePoa> prePoaList;

    public PreClasificador() {
    }

    public PreClasificador(Long idePrcla) {
        this.idePrcla = idePrcla;
    }

    public PreClasificador(Long idePrcla, long tipoPrcla) {
        this.idePrcla = idePrcla;
        this.tipoPrcla = tipoPrcla;
    }

    public Long getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(Long idePrcla) {
        this.idePrcla = idePrcla;
    }

    public String getCodigoClasificadorPrcla() {
        return codigoClasificadorPrcla;
    }

    public void setCodigoClasificadorPrcla(String codigoClasificadorPrcla) {
        this.codigoClasificadorPrcla = codigoClasificadorPrcla;
    }

    public String getDescripcionClasificadorPrcla() {
        return descripcionClasificadorPrcla;
    }

    public void setDescripcionClasificadorPrcla(String descripcionClasificadorPrcla) {
        this.descripcionClasificadorPrcla = descripcionClasificadorPrcla;
    }

    public long getTipoPrcla() {
        return tipoPrcla;
    }

    public void setTipoPrcla(long tipoPrcla) {
        this.tipoPrcla = tipoPrcla;
    }

    public BigInteger getNivelPrcla() {
        return nivelPrcla;
    }

    public void setNivelPrcla(BigInteger nivelPrcla) {
        this.nivelPrcla = nivelPrcla;
    }

    public String getGrupoPrcla() {
        return grupoPrcla;
    }

    public void setGrupoPrcla(String grupoPrcla) {
        this.grupoPrcla = grupoPrcla;
    }

    public Boolean getSigefcPrcla() {
        return sigefcPrcla;
    }

    public void setSigefcPrcla(Boolean sigefcPrcla) {
        this.sigefcPrcla = sigefcPrcla;
    }

    public Boolean getActivoPrcla() {
        return activoPrcla;
    }

    public void setActivoPrcla(Boolean activoPrcla) {
        this.activoPrcla = activoPrcla;
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

    public List<PrePac> getPrePacList() {
        return prePacList;
    }

    public void setPrePacList(List<PrePac> prePacList) {
        this.prePacList = prePacList;
    }

    public List<PrePartidaPac> getPrePartidaPacList() {
        return prePartidaPacList;
    }

    public void setPrePartidaPacList(List<PrePartidaPac> prePartidaPacList) {
        this.prePartidaPacList = prePartidaPacList;
    }

    public PreGrupoEconomico getIdePrgre() {
        return idePrgre;
    }

    public void setIdePrgre(PreGrupoEconomico idePrgre) {
        this.idePrgre = idePrgre;
    }

    public List<PreClasificador> getPreClasificadorList() {
        return preClasificadorList;
    }

    public void setPreClasificadorList(List<PreClasificador> preClasificadorList) {
        this.preClasificadorList = preClasificadorList;
    }

    public PreClasificador getPreIdePrcla() {
        return preIdePrcla;
    }

    public void setPreIdePrcla(PreClasificador preIdePrcla) {
        this.preIdePrcla = preIdePrcla;
    }

    public List<PreAnual> getPreAnualList() {
        return preAnualList;
    }

    public void setPreAnualList(List<PreAnual> preAnualList) {
        this.preAnualList = preAnualList;
    }

    public List<PreAsociacionPresupuestaria> getPreAsociacionPresupuestariaList() {
        return preAsociacionPresupuestariaList;
    }

    public void setPreAsociacionPresupuestariaList(List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList) {
        this.preAsociacionPresupuestariaList = preAsociacionPresupuestariaList;
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
        hash += (idePrcla != null ? idePrcla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreClasificador)) {
            return false;
        }
        PreClasificador other = (PreClasificador) object;
        if ((this.idePrcla == null && other.idePrcla != null) || (this.idePrcla != null && !this.idePrcla.equals(other.idePrcla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreClasificador[ idePrcla=" + idePrcla + " ]";
    }
    
}
