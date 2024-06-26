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
@Table(name = "cont_suministro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContSuministro.findAll", query = "SELECT c FROM ContSuministro c"),
    @NamedQuery(name = "ContSuministro.findByIdeCosum", query = "SELECT c FROM ContSuministro c WHERE c.ideCosum = :ideCosum"),
    @NamedQuery(name = "ContSuministro.findByIdeSucu", query = "SELECT c FROM ContSuministro c WHERE c.ideSucu = :ideSucu"),
    @NamedQuery(name = "ContSuministro.findByNumSuministroCosum", query = "SELECT c FROM ContSuministro c WHERE c.numSuministroCosum = :numSuministroCosum"),
    @NamedQuery(name = "ContSuministro.findByDireccionCosum", query = "SELECT c FROM ContSuministro c WHERE c.direccionCosum = :direccionCosum"),
    @NamedQuery(name = "ContSuministro.findByObservacionCosum", query = "SELECT c FROM ContSuministro c WHERE c.observacionCosum = :observacionCosum"),
    @NamedQuery(name = "ContSuministro.findByActivoCosum", query = "SELECT c FROM ContSuministro c WHERE c.activoCosum = :activoCosum"),
    @NamedQuery(name = "ContSuministro.findByUsuarioIngre", query = "SELECT c FROM ContSuministro c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContSuministro.findByFechaIngre", query = "SELECT c FROM ContSuministro c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContSuministro.findByHoraIngre", query = "SELECT c FROM ContSuministro c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContSuministro.findByUsuarioActua", query = "SELECT c FROM ContSuministro c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContSuministro.findByFechaActua", query = "SELECT c FROM ContSuministro c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContSuministro.findByHoraActua", query = "SELECT c FROM ContSuministro c WHERE c.horaActua = :horaActua")})
public class ContSuministro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cosum", nullable = false)
    private Long ideCosum;
    @Column(name = "ide_sucu")
    private BigInteger ideSucu;
    @Size(max = 50)
    @Column(name = "num_suministro_cosum", length = 50)
    private String numSuministroCosum;
    @Size(max = 250)
    @Column(name = "direccion_cosum", length = 250)
    private String direccionCosum;
    @Size(max = 2147483647)
    @Column(name = "observacion_cosum", length = 2147483647)
    private String observacionCosum;
    @Column(name = "activo_cosum")
    private Boolean activoCosum;
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
    @OneToMany(mappedBy = "ideCosum")
    private List<ContServicioSuministro> contServicioSuministroList;
    @JoinColumn(name = "ide_coseb", referencedColumnName = "ide_coseb")
    @ManyToOne
    private ContServicioBasico ideCoseb;

    public ContSuministro() {
    }

    public ContSuministro(Long ideCosum) {
        this.ideCosum = ideCosum;
    }

    public Long getIdeCosum() {
        return ideCosum;
    }

    public void setIdeCosum(Long ideCosum) {
        this.ideCosum = ideCosum;
    }

    public BigInteger getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(BigInteger ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getNumSuministroCosum() {
        return numSuministroCosum;
    }

    public void setNumSuministroCosum(String numSuministroCosum) {
        this.numSuministroCosum = numSuministroCosum;
    }

    public String getDireccionCosum() {
        return direccionCosum;
    }

    public void setDireccionCosum(String direccionCosum) {
        this.direccionCosum = direccionCosum;
    }

    public String getObservacionCosum() {
        return observacionCosum;
    }

    public void setObservacionCosum(String observacionCosum) {
        this.observacionCosum = observacionCosum;
    }

    public Boolean getActivoCosum() {
        return activoCosum;
    }

    public void setActivoCosum(Boolean activoCosum) {
        this.activoCosum = activoCosum;
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

    public List<ContServicioSuministro> getContServicioSuministroList() {
        return contServicioSuministroList;
    }

    public void setContServicioSuministroList(List<ContServicioSuministro> contServicioSuministroList) {
        this.contServicioSuministroList = contServicioSuministroList;
    }

    public ContServicioBasico getIdeCoseb() {
        return ideCoseb;
    }

    public void setIdeCoseb(ContServicioBasico ideCoseb) {
        this.ideCoseb = ideCoseb;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCosum != null ? ideCosum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContSuministro)) {
            return false;
        }
        ContSuministro other = (ContSuministro) object;
        if ((this.ideCosum == null && other.ideCosum != null) || (this.ideCosum != null && !this.ideCosum.equals(other.ideCosum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContSuministro[ ideCosum=" + ideCosum + " ]";
    }
    
}
