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
@Table(name = "sis_auditoria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisAuditoria.findAll", query = "SELECT s FROM SisAuditoria s"),
    @NamedQuery(name = "SisAuditoria.findByIdeAudi", query = "SELECT s FROM SisAuditoria s WHERE s.ideAudi = :ideAudi"),
    @NamedQuery(name = "SisAuditoria.findByIdeSucu", query = "SELECT s FROM SisAuditoria s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SisAuditoria.findByFechaAudi", query = "SELECT s FROM SisAuditoria s WHERE s.fechaAudi = :fechaAudi"),
    @NamedQuery(name = "SisAuditoria.findBySqlAudi", query = "SELECT s FROM SisAuditoria s WHERE s.sqlAudi = :sqlAudi"),
    @NamedQuery(name = "SisAuditoria.findByTablaAudi", query = "SELECT s FROM SisAuditoria s WHERE s.tablaAudi = :tablaAudi"),
    @NamedQuery(name = "SisAuditoria.findByIpAudi", query = "SELECT s FROM SisAuditoria s WHERE s.ipAudi = :ipAudi"),
    @NamedQuery(name = "SisAuditoria.findByAccionAudi", query = "SELECT s FROM SisAuditoria s WHERE s.accionAudi = :accionAudi"),
    @NamedQuery(name = "SisAuditoria.findByHoraAudi", query = "SELECT s FROM SisAuditoria s WHERE s.horaAudi = :horaAudi")})
public class SisAuditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_audi", nullable = false)
    private Integer ideAudi;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "fecha_audi")
    @Temporal(TemporalType.DATE)
    private Date fechaAudi;
    @Size(max = 4000)
    @Column(name = "sql_audi", length = 4000)
    private String sqlAudi;
    @Size(max = 60)
    @Column(name = "tabla_audi", length = 60)
    private String tablaAudi;
    @Size(max = 20)
    @Column(name = "ip_audi", length = 20)
    private String ipAudi;
    @Size(max = 50)
    @Column(name = "accion_audi", length = 50)
    private String accionAudi;
    @Column(name = "hora_audi")
    @Temporal(TemporalType.TIME)
    private Date horaAudi;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;

    public SisAuditoria() {
    }

    public SisAuditoria(Integer ideAudi) {
        this.ideAudi = ideAudi;
    }

    public Integer getIdeAudi() {
        return ideAudi;
    }

    public void setIdeAudi(Integer ideAudi) {
        this.ideAudi = ideAudi;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Date getFechaAudi() {
        return fechaAudi;
    }

    public void setFechaAudi(Date fechaAudi) {
        this.fechaAudi = fechaAudi;
    }

    public String getSqlAudi() {
        return sqlAudi;
    }

    public void setSqlAudi(String sqlAudi) {
        this.sqlAudi = sqlAudi;
    }

    public String getTablaAudi() {
        return tablaAudi;
    }

    public void setTablaAudi(String tablaAudi) {
        this.tablaAudi = tablaAudi;
    }

    public String getIpAudi() {
        return ipAudi;
    }

    public void setIpAudi(String ipAudi) {
        this.ipAudi = ipAudi;
    }

    public String getAccionAudi() {
        return accionAudi;
    }

    public void setAccionAudi(String accionAudi) {
        this.accionAudi = accionAudi;
    }

    public Date getHoraAudi() {
        return horaAudi;
    }

    public void setHoraAudi(Date horaAudi) {
        this.horaAudi = horaAudi;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    public SisEmpresa getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(SisEmpresa ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAudi != null ? ideAudi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisAuditoria)) {
            return false;
        }
        SisAuditoria other = (SisAuditoria) object;
        if ((this.ideAudi == null && other.ideAudi != null) || (this.ideAudi != null && !this.ideAudi.equals(other.ideAudi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisAuditoria[ ideAudi=" + ideAudi + " ]";
    }
    
}
