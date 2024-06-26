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
@Table(name = "cont_vigente", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContVigente.findAll", query = "SELECT c FROM ContVigente c"),
    @NamedQuery(name = "ContVigente.findByIdeCovig", query = "SELECT c FROM ContVigente c WHERE c.ideCovig = :ideCovig"),
    @NamedQuery(name = "ContVigente.findByBloqueadoCovig", query = "SELECT c FROM ContVigente c WHERE c.bloqueadoCovig = :bloqueadoCovig"),
    @NamedQuery(name = "ContVigente.findByActivoPrmop", query = "SELECT c FROM ContVigente c WHERE c.activoPrmop = :activoPrmop"),
    @NamedQuery(name = "ContVigente.findByUsuarioIngre", query = "SELECT c FROM ContVigente c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContVigente.findByFechaIngre", query = "SELECT c FROM ContVigente c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContVigente.findByHoraIngre", query = "SELECT c FROM ContVigente c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContVigente.findByUsuarioActua", query = "SELECT c FROM ContVigente c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContVigente.findByFechaActua", query = "SELECT c FROM ContVigente c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContVigente.findByHoraActua", query = "SELECT c FROM ContVigente c WHERE c.horaActua = :horaActua")})
public class ContVigente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_covig", nullable = false)
    private Long ideCovig;
    @Column(name = "bloqueado_covig")
    private Boolean bloqueadoCovig;
    @Column(name = "activo_prmop")
    private Boolean activoPrmop;
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
    @OneToMany(mappedBy = "ideCovig")
    private List<ContDetalleMovimiento> contDetalleMovimientoList;
    @OneToMany(mappedBy = "conIdeCovig")
    private List<ContDetalleMovimiento> contDetalleMovimientoList1;
    @OneToMany(mappedBy = "conIdeCovig2")
    private List<ContDetalleMovimiento> contDetalleMovimientoList2;
    @JoinColumn(name = "ide_prpro", referencedColumnName = "ide_prpro")
    @ManyToOne
    private PrePrograma idePrpro;
    @JoinColumn(name = "ide_prfup", referencedColumnName = "ide_prfup")
    @ManyToOne
    private PreFuncionPrograma idePrfup;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @JoinColumn(name = "ide_prasp", referencedColumnName = "ide_prasp")
    @ManyToOne
    private PreAsociacionPresupuestaria idePrasp;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_cocac", referencedColumnName = "ide_cocac")
    @ManyToOne
    private ContCatalogoCuenta ideCocac;

    public ContVigente() {
    }

    public ContVigente(Long ideCovig) {
        this.ideCovig = ideCovig;
    }

    public Long getIdeCovig() {
        return ideCovig;
    }

    public void setIdeCovig(Long ideCovig) {
        this.ideCovig = ideCovig;
    }

    public Boolean getBloqueadoCovig() {
        return bloqueadoCovig;
    }

    public void setBloqueadoCovig(Boolean bloqueadoCovig) {
        this.bloqueadoCovig = bloqueadoCovig;
    }

    public Boolean getActivoPrmop() {
        return activoPrmop;
    }

    public void setActivoPrmop(Boolean activoPrmop) {
        this.activoPrmop = activoPrmop;
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

    public List<ContDetalleMovimiento> getContDetalleMovimientoList() {
        return contDetalleMovimientoList;
    }

    public void setContDetalleMovimientoList(List<ContDetalleMovimiento> contDetalleMovimientoList) {
        this.contDetalleMovimientoList = contDetalleMovimientoList;
    }

    public List<ContDetalleMovimiento> getContDetalleMovimientoList1() {
        return contDetalleMovimientoList1;
    }

    public void setContDetalleMovimientoList1(List<ContDetalleMovimiento> contDetalleMovimientoList1) {
        this.contDetalleMovimientoList1 = contDetalleMovimientoList1;
    }

    public List<ContDetalleMovimiento> getContDetalleMovimientoList2() {
        return contDetalleMovimientoList2;
    }

    public void setContDetalleMovimientoList2(List<ContDetalleMovimiento> contDetalleMovimientoList2) {
        this.contDetalleMovimientoList2 = contDetalleMovimientoList2;
    }

    public PrePrograma getIdePrpro() {
        return idePrpro;
    }

    public void setIdePrpro(PrePrograma idePrpro) {
        this.idePrpro = idePrpro;
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

    public PreAsociacionPresupuestaria getIdePrasp() {
        return idePrasp;
    }

    public void setIdePrasp(PreAsociacionPresupuestaria idePrasp) {
        this.idePrasp = idePrasp;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContCatalogoCuenta getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(ContCatalogoCuenta ideCocac) {
        this.ideCocac = ideCocac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCovig != null ? ideCovig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContVigente)) {
            return false;
        }
        ContVigente other = (ContVigente) object;
        if ((this.ideCovig == null && other.ideCovig != null) || (this.ideCovig != null && !this.ideCovig.equals(other.ideCovig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContVigente[ ideCovig=" + ideCovig + " ]";
    }
    
}
