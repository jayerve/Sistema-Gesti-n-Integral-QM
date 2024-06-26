/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "cont_viajeros", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContViajeros.findAll", query = "SELECT c FROM ContViajeros c"),
    @NamedQuery(name = "ContViajeros.findByIdeCovia", query = "SELECT c FROM ContViajeros c WHERE c.ideCovia = :ideCovia"),
    @NamedQuery(name = "ContViajeros.findByIdeCotiv", query = "SELECT c FROM ContViajeros c WHERE c.ideCotiv = :ideCotiv"),
    @NamedQuery(name = "ContViajeros.findByNumMemoCovia", query = "SELECT c FROM ContViajeros c WHERE c.numMemoCovia = :numMemoCovia"),
    @NamedQuery(name = "ContViajeros.findByBoletoCovia", query = "SELECT c FROM ContViajeros c WHERE c.boletoCovia = :boletoCovia"),
    @NamedQuery(name = "ContViajeros.findByActivoCovia", query = "SELECT c FROM ContViajeros c WHERE c.activoCovia = :activoCovia"),
    @NamedQuery(name = "ContViajeros.findByUsuarioIngre", query = "SELECT c FROM ContViajeros c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContViajeros.findByFechaIngre", query = "SELECT c FROM ContViajeros c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContViajeros.findByHoraIngre", query = "SELECT c FROM ContViajeros c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContViajeros.findByUsuarioActua", query = "SELECT c FROM ContViajeros c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContViajeros.findByFechaActua", query = "SELECT c FROM ContViajeros c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContViajeros.findByHoraActua", query = "SELECT c FROM ContViajeros c WHERE c.horaActua = :horaActua")})
public class ContViajeros implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_covia", nullable = false)
    private Long ideCovia;
    @Column(name = "ide_cotiv")
    private BigInteger ideCotiv;
    @Size(max = 50)
    @Column(name = "num_memo_covia", length = 50)
    private String numMemoCovia;
    @Size(max = 250)
    @Column(name = "boleto_covia", length = 250)
    private String boletoCovia;
    @Column(name = "activo_covia")
    private Boolean activoCovia;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_coclv", referencedColumnName = "ide_coclv")
    @ManyToOne
    private ContClaseViaje ideCoclv;

    public ContViajeros() {
    }

    public ContViajeros(Long ideCovia) {
        this.ideCovia = ideCovia;
    }

    public Long getIdeCovia() {
        return ideCovia;
    }

    public void setIdeCovia(Long ideCovia) {
        this.ideCovia = ideCovia;
    }

    public BigInteger getIdeCotiv() {
        return ideCotiv;
    }

    public void setIdeCotiv(BigInteger ideCotiv) {
        this.ideCotiv = ideCotiv;
    }

    public String getNumMemoCovia() {
        return numMemoCovia;
    }

    public void setNumMemoCovia(String numMemoCovia) {
        this.numMemoCovia = numMemoCovia;
    }

    public String getBoletoCovia() {
        return boletoCovia;
    }

    public void setBoletoCovia(String boletoCovia) {
        this.boletoCovia = boletoCovia;
    }

    public Boolean getActivoCovia() {
        return activoCovia;
    }

    public void setActivoCovia(Boolean activoCovia) {
        this.activoCovia = activoCovia;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public ContClaseViaje getIdeCoclv() {
        return ideCoclv;
    }

    public void setIdeCoclv(ContClaseViaje ideCoclv) {
        this.ideCoclv = ideCoclv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCovia != null ? ideCovia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContViajeros)) {
            return false;
        }
        ContViajeros other = (ContViajeros) object;
        if ((this.ideCovia == null && other.ideCovia != null) || (this.ideCovia != null && !this.ideCovia.equals(other.ideCovia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContViajeros[ ideCovia=" + ideCovia + " ]";
    }
    
}
