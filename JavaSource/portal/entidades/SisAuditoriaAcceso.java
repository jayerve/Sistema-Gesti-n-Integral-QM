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
@Table(name = "sis_auditoria_acceso", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisAuditoriaAcceso.findAll", query = "SELECT s FROM SisAuditoriaAcceso s"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByIdeAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.ideAuac = :ideAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByIdeUsua", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.ideUsua = :ideUsua"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByIdeAcau", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.ideAcau = :ideAcau"),
    @NamedQuery(name = "SisAuditoriaAcceso.findBySisIdeUsua", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.sisIdeUsua = :sisIdeUsua"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByFechaAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.fechaAuac = :fechaAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByHoraAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.horaAuac = :horaAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByIpAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.ipAuac = :ipAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByIdSessionAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.idSessionAuac = :idSessionAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByFinAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.finAuac = :finAuac"),
    @NamedQuery(name = "SisAuditoriaAcceso.findByDetalleAuac", query = "SELECT s FROM SisAuditoriaAcceso s WHERE s.detalleAuac = :detalleAuac")})
public class SisAuditoriaAcceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_auac", nullable = false)
    private Integer ideAuac;
    @Column(name = "ide_usua")
    private Integer ideUsua;
    @Column(name = "ide_acau")
    private Integer ideAcau;
    @Column(name = "sis_ide_usua")
    private Integer sisIdeUsua;
    @Column(name = "fecha_auac")
    @Temporal(TemporalType.DATE)
    private Date fechaAuac;
    @Column(name = "hora_auac")
    @Temporal(TemporalType.TIME)
    private Date horaAuac;
    @Size(max = 50)
    @Column(name = "ip_auac", length = 50)
    private String ipAuac;
    @Size(max = 50)
    @Column(name = "id_session_auac", length = 50)
    private String idSessionAuac;
    @Column(name = "fin_auac")
    private Boolean finAuac;
    @Size(max = 100)
    @Column(name = "detalle_auac", length = 100)
    private String detalleAuac;

    public SisAuditoriaAcceso() {
    }

    public SisAuditoriaAcceso(Integer ideAuac) {
        this.ideAuac = ideAuac;
    }

    public Integer getIdeAuac() {
        return ideAuac;
    }

    public void setIdeAuac(Integer ideAuac) {
        this.ideAuac = ideAuac;
    }

    public Integer getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(Integer ideUsua) {
        this.ideUsua = ideUsua;
    }

    public Integer getIdeAcau() {
        return ideAcau;
    }

    public void setIdeAcau(Integer ideAcau) {
        this.ideAcau = ideAcau;
    }

    public Integer getSisIdeUsua() {
        return sisIdeUsua;
    }

    public void setSisIdeUsua(Integer sisIdeUsua) {
        this.sisIdeUsua = sisIdeUsua;
    }

    public Date getFechaAuac() {
        return fechaAuac;
    }

    public void setFechaAuac(Date fechaAuac) {
        this.fechaAuac = fechaAuac;
    }

    public Date getHoraAuac() {
        return horaAuac;
    }

    public void setHoraAuac(Date horaAuac) {
        this.horaAuac = horaAuac;
    }

    public String getIpAuac() {
        return ipAuac;
    }

    public void setIpAuac(String ipAuac) {
        this.ipAuac = ipAuac;
    }

    public String getIdSessionAuac() {
        return idSessionAuac;
    }

    public void setIdSessionAuac(String idSessionAuac) {
        this.idSessionAuac = idSessionAuac;
    }

    public Boolean getFinAuac() {
        return finAuac;
    }

    public void setFinAuac(Boolean finAuac) {
        this.finAuac = finAuac;
    }

    public String getDetalleAuac() {
        return detalleAuac;
    }

    public void setDetalleAuac(String detalleAuac) {
        this.detalleAuac = detalleAuac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAuac != null ? ideAuac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisAuditoriaAcceso)) {
            return false;
        }
        SisAuditoriaAcceso other = (SisAuditoriaAcceso) object;
        if ((this.ideAuac == null && other.ideAuac != null) || (this.ideAuac != null && !this.ideAuac.equals(other.ideAuac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisAuditoriaAcceso[ ideAuac=" + ideAuac + " ]";
    }
    
}
