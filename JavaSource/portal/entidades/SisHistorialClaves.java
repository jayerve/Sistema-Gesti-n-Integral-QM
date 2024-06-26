/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "sis_historial_claves", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisHistorialClaves.findAll", query = "SELECT s FROM SisHistorialClaves s"),
    @NamedQuery(name = "SisHistorialClaves.findByIdeHicl", query = "SELECT s FROM SisHistorialClaves s WHERE s.ideHicl = :ideHicl"),
    @NamedQuery(name = "SisHistorialClaves.findByClaveHicl", query = "SELECT s FROM SisHistorialClaves s WHERE s.claveHicl = :claveHicl"),
    @NamedQuery(name = "SisHistorialClaves.findByFechaHicl", query = "SELECT s FROM SisHistorialClaves s WHERE s.fechaHicl = :fechaHicl"),
    @NamedQuery(name = "SisHistorialClaves.findByHoraHicl", query = "SELECT s FROM SisHistorialClaves s WHERE s.horaHicl = :horaHicl")})
public class SisHistorialClaves implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_hicl", nullable = false)
    private Integer ideHicl;
    @Size(max = 50)
    @Column(name = "clave_hicl", length = 50)
    private String claveHicl;
    @Column(name = "fecha_hicl")
    @Temporal(TemporalType.DATE)
    private Date fechaHicl;
    @Column(name = "hora_hicl")
    @Temporal(TemporalType.TIME)
    private Date horaHicl;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;

    public SisHistorialClaves() {
    }

    public SisHistorialClaves(Integer ideHicl) {
        this.ideHicl = ideHicl;
    }

    public Integer getIdeHicl() {
        return ideHicl;
    }

    public void setIdeHicl(Integer ideHicl) {
        this.ideHicl = ideHicl;
    }

    public String getClaveHicl() {
        return claveHicl;
    }

    public void setClaveHicl(String claveHicl) {
        this.claveHicl = claveHicl;
    }

    public Date getFechaHicl() {
        return fechaHicl;
    }

    public void setFechaHicl(Date fechaHicl) {
        this.fechaHicl = fechaHicl;
    }

    public Date getHoraHicl() {
        return horaHicl;
    }

    public void setHoraHicl(Date horaHicl) {
        this.horaHicl = horaHicl;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideHicl != null ? ideHicl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisHistorialClaves)) {
            return false;
        }
        SisHistorialClaves other = (SisHistorialClaves) object;
        if ((this.ideHicl == null && other.ideHicl != null) || (this.ideHicl != null && !this.ideHicl.equals(other.ideHicl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisHistorialClaves[ ideHicl=" + ideHicl + " ]";
    }
    
}
