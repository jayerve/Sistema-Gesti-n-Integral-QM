/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cont_servicio_suministro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContServicioSuministro.findAll", query = "SELECT c FROM ContServicioSuministro c"),
    @NamedQuery(name = "ContServicioSuministro.findByIdeCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.ideCoses = :ideCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByValorCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.valorCoses = :valorCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByFechaEmisionCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.fechaEmisionCoses = :fechaEmisionCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByFechaVencimientoCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.fechaVencimientoCoses = :fechaVencimientoCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByCanceladoCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.canceladoCoses = :canceladoCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByFechaCanceladoCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.fechaCanceladoCoses = :fechaCanceladoCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByFacturaCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.facturaCoses = :facturaCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByActivoCoses", query = "SELECT c FROM ContServicioSuministro c WHERE c.activoCoses = :activoCoses"),
    @NamedQuery(name = "ContServicioSuministro.findByUsuarioIngre", query = "SELECT c FROM ContServicioSuministro c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContServicioSuministro.findByFechaIngre", query = "SELECT c FROM ContServicioSuministro c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContServicioSuministro.findByHoraIngre", query = "SELECT c FROM ContServicioSuministro c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContServicioSuministro.findByUsuarioActua", query = "SELECT c FROM ContServicioSuministro c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContServicioSuministro.findByFechaActua", query = "SELECT c FROM ContServicioSuministro c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContServicioSuministro.findByHoraActua", query = "SELECT c FROM ContServicioSuministro c WHERE c.horaActua = :horaActua")})
public class ContServicioSuministro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coses", nullable = false)
    private Long ideCoses;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_coses", precision = 10, scale = 2)
    private BigDecimal valorCoses;
    @Column(name = "fecha_emision_coses")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionCoses;
    @Column(name = "fecha_vencimiento_coses")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoCoses;
    @Column(name = "cancelado_coses")
    private Boolean canceladoCoses;
    @Column(name = "fecha_cancelado_coses")
    @Temporal(TemporalType.DATE)
    private Date fechaCanceladoCoses;
    @Size(max = 250)
    @Column(name = "factura_coses", length = 250)
    private String facturaCoses;
    @Column(name = "activo_coses")
    private Boolean activoCoses;
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
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_cosum", referencedColumnName = "ide_cosum")
    @ManyToOne
    private ContSuministro ideCosum;

    public ContServicioSuministro() {
    }

    public ContServicioSuministro(Long ideCoses) {
        this.ideCoses = ideCoses;
    }

    public Long getIdeCoses() {
        return ideCoses;
    }

    public void setIdeCoses(Long ideCoses) {
        this.ideCoses = ideCoses;
    }

    public BigDecimal getValorCoses() {
        return valorCoses;
    }

    public void setValorCoses(BigDecimal valorCoses) {
        this.valorCoses = valorCoses;
    }

    public Date getFechaEmisionCoses() {
        return fechaEmisionCoses;
    }

    public void setFechaEmisionCoses(Date fechaEmisionCoses) {
        this.fechaEmisionCoses = fechaEmisionCoses;
    }

    public Date getFechaVencimientoCoses() {
        return fechaVencimientoCoses;
    }

    public void setFechaVencimientoCoses(Date fechaVencimientoCoses) {
        this.fechaVencimientoCoses = fechaVencimientoCoses;
    }

    public Boolean getCanceladoCoses() {
        return canceladoCoses;
    }

    public void setCanceladoCoses(Boolean canceladoCoses) {
        this.canceladoCoses = canceladoCoses;
    }

    public Date getFechaCanceladoCoses() {
        return fechaCanceladoCoses;
    }

    public void setFechaCanceladoCoses(Date fechaCanceladoCoses) {
        this.fechaCanceladoCoses = fechaCanceladoCoses;
    }

    public String getFacturaCoses() {
        return facturaCoses;
    }

    public void setFacturaCoses(String facturaCoses) {
        this.facturaCoses = facturaCoses;
    }

    public Boolean getActivoCoses() {
        return activoCoses;
    }

    public void setActivoCoses(Boolean activoCoses) {
        this.activoCoses = activoCoses;
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

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public ContSuministro getIdeCosum() {
        return ideCosum;
    }

    public void setIdeCosum(ContSuministro ideCosum) {
        this.ideCosum = ideCosum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoses != null ? ideCoses.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContServicioSuministro)) {
            return false;
        }
        ContServicioSuministro other = (ContServicioSuministro) object;
        if ((this.ideCoses == null && other.ideCoses != null) || (this.ideCoses != null && !this.ideCoses.equals(other.ideCoses))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContServicioSuministro[ ideCoses=" + ideCoses + " ]";
    }
    
}
