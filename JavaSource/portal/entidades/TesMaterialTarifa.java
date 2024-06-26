/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "tes_material_tarifa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesMaterialTarifa.findAll", query = "SELECT t FROM TesMaterialTarifa t"),
    @NamedQuery(name = "TesMaterialTarifa.findByIdeTemat", query = "SELECT t FROM TesMaterialTarifa t WHERE t.ideTemat = :ideTemat"),
    @NamedQuery(name = "TesMaterialTarifa.findByValorTemat", query = "SELECT t FROM TesMaterialTarifa t WHERE t.valorTemat = :valorTemat"),
    @NamedQuery(name = "TesMaterialTarifa.findByActivoTemat", query = "SELECT t FROM TesMaterialTarifa t WHERE t.activoTemat = :activoTemat"),
    @NamedQuery(name = "TesMaterialTarifa.findByUsuarioIngre", query = "SELECT t FROM TesMaterialTarifa t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesMaterialTarifa.findByFechaIngre", query = "SELECT t FROM TesMaterialTarifa t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesMaterialTarifa.findByHoraIngre", query = "SELECT t FROM TesMaterialTarifa t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesMaterialTarifa.findByUsuarioActua", query = "SELECT t FROM TesMaterialTarifa t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesMaterialTarifa.findByFechaActua", query = "SELECT t FROM TesMaterialTarifa t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesMaterialTarifa.findByHoraActua", query = "SELECT t FROM TesMaterialTarifa t WHERE t.horaActua = :horaActua")})
public class TesMaterialTarifa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_temat", nullable = false)
    private Long ideTemat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_temat", precision = 10, scale = 2)
    private BigDecimal valorTemat;
    @Column(name = "activo_temat")
    private Boolean activoTemat;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTemat")
    private List<TesClienteTarifa> tesClienteTarifaList;
    @JoinColumn(name = "ide_tetar", referencedColumnName = "ide_tetar")
    @ManyToOne
    private TesTarifas ideTetar;
    @JoinColumn(name = "ide_bomat", referencedColumnName = "ide_bomat")
    @ManyToOne
    private BodtMaterial ideBomat;

    public TesMaterialTarifa() {
    }

    public TesMaterialTarifa(Long ideTemat) {
        this.ideTemat = ideTemat;
    }

    public Long getIdeTemat() {
        return ideTemat;
    }

    public void setIdeTemat(Long ideTemat) {
        this.ideTemat = ideTemat;
    }

    public BigDecimal getValorTemat() {
        return valorTemat;
    }

    public void setValorTemat(BigDecimal valorTemat) {
        this.valorTemat = valorTemat;
    }

    public Boolean getActivoTemat() {
        return activoTemat;
    }

    public void setActivoTemat(Boolean activoTemat) {
        this.activoTemat = activoTemat;
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

    public List<TesClienteTarifa> getTesClienteTarifaList() {
        return tesClienteTarifaList;
    }

    public void setTesClienteTarifaList(List<TesClienteTarifa> tesClienteTarifaList) {
        this.tesClienteTarifaList = tesClienteTarifaList;
    }

    public TesTarifas getIdeTetar() {
        return ideTetar;
    }

    public void setIdeTetar(TesTarifas ideTetar) {
        this.ideTetar = ideTetar;
    }

    public BodtMaterial getIdeBomat() {
        return ideBomat;
    }

    public void setIdeBomat(BodtMaterial ideBomat) {
        this.ideBomat = ideBomat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTemat != null ? ideTemat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesMaterialTarifa)) {
            return false;
        }
        TesMaterialTarifa other = (TesMaterialTarifa) object;
        if ((this.ideTemat == null && other.ideTemat != null) || (this.ideTemat != null && !this.ideTemat.equals(other.ideTemat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesMaterialTarifa[ ideTemat=" + ideTemat + " ]";
    }
    
}
