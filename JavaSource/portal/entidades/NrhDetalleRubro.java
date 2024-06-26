/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "nrh_detalle_rubro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhDetalleRubro.findAll", query = "SELECT n FROM NrhDetalleRubro n"),
    @NamedQuery(name = "NrhDetalleRubro.findByIdeNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.ideNrder = :ideNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByFormulaNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.formulaNrder = :formulaNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByOrdenNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.ordenNrder = :ordenNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByOrdenImprimeNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.ordenImprimeNrder = :ordenImprimeNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByFechaInicialNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.fechaInicialNrder = :fechaInicialNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByFechaFinalNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.fechaFinalNrder = :fechaFinalNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByFechaPagoNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.fechaPagoNrder = :fechaPagoNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByObservacionNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.observacionNrder = :observacionNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByActivoNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.activoNrder = :activoNrder"),
    @NamedQuery(name = "NrhDetalleRubro.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleRubro n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleRubro.findByFechaIngre", query = "SELECT n FROM NrhDetalleRubro n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleRubro.findByUsuarioActua", query = "SELECT n FROM NrhDetalleRubro n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleRubro.findByFechaActua", query = "SELECT n FROM NrhDetalleRubro n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleRubro.findByHoraIngre", query = "SELECT n FROM NrhDetalleRubro n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleRubro.findByHoraActua", query = "SELECT n FROM NrhDetalleRubro n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhDetalleRubro.findByImprimeNrder", query = "SELECT n FROM NrhDetalleRubro n WHERE n.imprimeNrder = :imprimeNrder")})
public class NrhDetalleRubro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrder", nullable = false)
    private Integer ideNrder;
    @Size(max = 1000)
    @Column(name = "formula_nrder", length = 1000)
    private String formulaNrder;
    @Column(name = "orden_nrder")
    private Integer ordenNrder;
    @Column(name = "orden_imprime_nrder")
    private Integer ordenImprimeNrder;
    @Size(max = 9)
    @Column(name = "fecha_inicial_nrder", length = 9)
    private String fechaInicialNrder;
    @Size(max = 9)
    @Column(name = "fecha_final_nrder", length = 9)
    private String fechaFinalNrder;
    @Size(max = 9)
    @Column(name = "fecha_pago_nrder", length = 9)
    private String fechaPagoNrder;
    @Size(max = 4000)
    @Column(name = "observacion_nrder", length = 4000)
    private String observacionNrder;
    @Column(name = "activo_nrder")
    private Boolean activoNrder;
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
    @Column(name = "imprime_nrder")
    private Boolean imprimeNrder;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrder")
    private List<NrhDetalleRol> nrhDetalleRolList;
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub", nullable = false)
    @ManyToOne(optional = false)
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_nrdtn", referencedColumnName = "ide_nrdtn", nullable = false)
    @ManyToOne(optional = false)
    private NrhDetalleTipoNomina ideNrdtn;
    @JoinColumn(name = "ide_gereg", referencedColumnName = "ide_gereg")
    @ManyToOne
    private GenRegion ideGereg;

    public NrhDetalleRubro() {
    }

    public NrhDetalleRubro(Integer ideNrder) {
        this.ideNrder = ideNrder;
    }

    public Integer getIdeNrder() {
        return ideNrder;
    }

    public void setIdeNrder(Integer ideNrder) {
        this.ideNrder = ideNrder;
    }

    public String getFormulaNrder() {
        return formulaNrder;
    }

    public void setFormulaNrder(String formulaNrder) {
        this.formulaNrder = formulaNrder;
    }

    public Integer getOrdenNrder() {
        return ordenNrder;
    }

    public void setOrdenNrder(Integer ordenNrder) {
        this.ordenNrder = ordenNrder;
    }

    public Integer getOrdenImprimeNrder() {
        return ordenImprimeNrder;
    }

    public void setOrdenImprimeNrder(Integer ordenImprimeNrder) {
        this.ordenImprimeNrder = ordenImprimeNrder;
    }

    public String getFechaInicialNrder() {
        return fechaInicialNrder;
    }

    public void setFechaInicialNrder(String fechaInicialNrder) {
        this.fechaInicialNrder = fechaInicialNrder;
    }

    public String getFechaFinalNrder() {
        return fechaFinalNrder;
    }

    public void setFechaFinalNrder(String fechaFinalNrder) {
        this.fechaFinalNrder = fechaFinalNrder;
    }

    public String getFechaPagoNrder() {
        return fechaPagoNrder;
    }

    public void setFechaPagoNrder(String fechaPagoNrder) {
        this.fechaPagoNrder = fechaPagoNrder;
    }

    public String getObservacionNrder() {
        return observacionNrder;
    }

    public void setObservacionNrder(String observacionNrder) {
        this.observacionNrder = observacionNrder;
    }

    public Boolean getActivoNrder() {
        return activoNrder;
    }

    public void setActivoNrder(Boolean activoNrder) {
        this.activoNrder = activoNrder;
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

    public Boolean getImprimeNrder() {
        return imprimeNrder;
    }

    public void setImprimeNrder(Boolean imprimeNrder) {
        this.imprimeNrder = imprimeNrder;
    }

    public List<NrhDetalleRol> getNrhDetalleRolList() {
        return nrhDetalleRolList;
    }

    public void setNrhDetalleRolList(List<NrhDetalleRol> nrhDetalleRolList) {
        this.nrhDetalleRolList = nrhDetalleRolList;
    }

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhDetalleTipoNomina getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(NrhDetalleTipoNomina ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public GenRegion getIdeGereg() {
        return ideGereg;
    }

    public void setIdeGereg(GenRegion ideGereg) {
        this.ideGereg = ideGereg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrder != null ? ideNrder.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleRubro)) {
            return false;
        }
        NrhDetalleRubro other = (NrhDetalleRubro) object;
        if ((this.ideNrder == null && other.ideNrder != null) || (this.ideNrder != null && !this.ideNrder.equals(other.ideNrder))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleRubro[ ideNrder=" + ideNrder + " ]";
    }
    
}
