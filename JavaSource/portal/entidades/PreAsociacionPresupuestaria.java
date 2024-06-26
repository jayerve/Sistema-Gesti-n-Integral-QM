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
@Table(name = "pre_asociacion_presupuestaria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreAsociacionPresupuestaria.findAll", query = "SELECT p FROM PreAsociacionPresupuestaria p"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByIdePrasp", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.idePrasp = :idePrasp"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByActivoPrasp", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.activoPrasp = :activoPrasp"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByUsuarioIngre", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByFechaIngre", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByHoraIngre", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByUsuarioActua", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByFechaActua", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreAsociacionPresupuestaria.findByHoraActua", query = "SELECT p FROM PreAsociacionPresupuestaria p WHERE p.horaActua = :horaActua")})
public class PreAsociacionPresupuestaria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prasp", nullable = false)
    private Long idePrasp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_prasp", nullable = false)
    private boolean activoPrasp;
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
    @OneToMany(mappedBy = "idePrasp")
    private List<GenPartidaPresupuestaria> genPartidaPresupuestariaList;
    @JoinColumn(name = "ide_prmop", referencedColumnName = "ide_prmop")
    @ManyToOne
    private PreMovimientoPresupuestario idePrmop;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @JoinColumn(name = "ide_gelua", referencedColumnName = "ide_gelua")
    @ManyToOne
    private GenLugarAplica ideGelua;
    @JoinColumn(name = "ide_cocac", referencedColumnName = "ide_cocac")
    @ManyToOne
    private ContCatalogoCuenta ideCocac;
    @OneToMany(mappedBy = "idePrasp")
    private List<ContVigente> contVigenteList;

    public PreAsociacionPresupuestaria() {
    }

    public PreAsociacionPresupuestaria(Long idePrasp) {
        this.idePrasp = idePrasp;
    }

    public PreAsociacionPresupuestaria(Long idePrasp, boolean activoPrasp) {
        this.idePrasp = idePrasp;
        this.activoPrasp = activoPrasp;
    }

    public Long getIdePrasp() {
        return idePrasp;
    }

    public void setIdePrasp(Long idePrasp) {
        this.idePrasp = idePrasp;
    }

    public boolean getActivoPrasp() {
        return activoPrasp;
    }

    public void setActivoPrasp(boolean activoPrasp) {
        this.activoPrasp = activoPrasp;
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

    public List<GenPartidaPresupuestaria> getGenPartidaPresupuestariaList() {
        return genPartidaPresupuestariaList;
    }

    public void setGenPartidaPresupuestariaList(List<GenPartidaPresupuestaria> genPartidaPresupuestariaList) {
        this.genPartidaPresupuestariaList = genPartidaPresupuestariaList;
    }

    public PreMovimientoPresupuestario getIdePrmop() {
        return idePrmop;
    }

    public void setIdePrmop(PreMovimientoPresupuestario idePrmop) {
        this.idePrmop = idePrmop;
    }

    public PreClasificador getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(PreClasificador idePrcla) {
        this.idePrcla = idePrcla;
    }

    public GenLugarAplica getIdeGelua() {
        return ideGelua;
    }

    public void setIdeGelua(GenLugarAplica ideGelua) {
        this.ideGelua = ideGelua;
    }

    public ContCatalogoCuenta getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(ContCatalogoCuenta ideCocac) {
        this.ideCocac = ideCocac;
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
        hash += (idePrasp != null ? idePrasp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreAsociacionPresupuestaria)) {
            return false;
        }
        PreAsociacionPresupuestaria other = (PreAsociacionPresupuestaria) object;
        if ((this.idePrasp == null && other.idePrasp != null) || (this.idePrasp != null && !this.idePrasp.equals(other.idePrasp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreAsociacionPresupuestaria[ idePrasp=" + idePrasp + " ]";
    }
    
}
