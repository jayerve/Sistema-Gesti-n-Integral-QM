/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "sri_beneficio_tributario_disc", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriBeneficioTributarioDisc.findAll", query = "SELECT s FROM SriBeneficioTributarioDisc s"),
    @NamedQuery(name = "SriBeneficioTributarioDisc.findByIdeSrbtd", query = "SELECT s FROM SriBeneficioTributarioDisc s WHERE s.ideSrbtd = :ideSrbtd"),
    @NamedQuery(name = "SriBeneficioTributarioDisc.findByGradoInicialSrbtd", query = "SELECT s FROM SriBeneficioTributarioDisc s WHERE s.gradoInicialSrbtd = :gradoInicialSrbtd"),
    @NamedQuery(name = "SriBeneficioTributarioDisc.findByGradoFinalSrbtd", query = "SELECT s FROM SriBeneficioTributarioDisc s WHERE s.gradoFinalSrbtd = :gradoFinalSrbtd"),
    @NamedQuery(name = "SriBeneficioTributarioDisc.findByPorcentajeAplicaSrbtd", query = "SELECT s FROM SriBeneficioTributarioDisc s WHERE s.porcentajeAplicaSrbtd = :porcentajeAplicaSrbtd")})
public class SriBeneficioTributarioDisc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_srbtd", nullable = false)
    private Integer ideSrbtd;
    @Column(name = "grado_inicial_srbtd")
    private Integer gradoInicialSrbtd;
    @Column(name = "grado_final_srbtd")
    private Integer gradoFinalSrbtd;
    @Column(name = "porcentaje_aplica_srbtd")
    private Integer porcentajeAplicaSrbtd;

    public SriBeneficioTributarioDisc() {
    }

    public SriBeneficioTributarioDisc(Integer ideSrbtd) {
        this.ideSrbtd = ideSrbtd;
    }

    public Integer getIdeSrbtd() {
        return ideSrbtd;
    }

    public void setIdeSrbtd(Integer ideSrbtd) {
        this.ideSrbtd = ideSrbtd;
    }

    public Integer getGradoInicialSrbtd() {
        return gradoInicialSrbtd;
    }

    public void setGradoInicialSrbtd(Integer gradoInicialSrbtd) {
        this.gradoInicialSrbtd = gradoInicialSrbtd;
    }

    public Integer getGradoFinalSrbtd() {
        return gradoFinalSrbtd;
    }

    public void setGradoFinalSrbtd(Integer gradoFinalSrbtd) {
        this.gradoFinalSrbtd = gradoFinalSrbtd;
    }

    public Integer getPorcentajeAplicaSrbtd() {
        return porcentajeAplicaSrbtd;
    }

    public void setPorcentajeAplicaSrbtd(Integer porcentajeAplicaSrbtd) {
        this.porcentajeAplicaSrbtd = porcentajeAplicaSrbtd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrbtd != null ? ideSrbtd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriBeneficioTributarioDisc)) {
            return false;
        }
        SriBeneficioTributarioDisc other = (SriBeneficioTributarioDisc) object;
        if ((this.ideSrbtd == null && other.ideSrbtd != null) || (this.ideSrbtd != null && !this.ideSrbtd.equals(other.ideSrbtd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriBeneficioTributarioDisc[ ideSrbtd=" + ideSrbtd + " ]";
    }
    
}
