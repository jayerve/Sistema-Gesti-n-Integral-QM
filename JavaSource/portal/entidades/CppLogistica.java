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
@Table(name = "cpp_logistica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppLogistica.findAll", query = "SELECT c FROM CppLogistica c"),
    @NamedQuery(name = "CppLogistica.findByIdeCplog", query = "SELECT c FROM CppLogistica c WHERE c.ideCplog = :ideCplog"),
    @NamedQuery(name = "CppLogistica.findByMemoAutorizaCplog", query = "SELECT c FROM CppLogistica c WHERE c.memoAutorizaCplog = :memoAutorizaCplog"),
    @NamedQuery(name = "CppLogistica.findByMemoDiponePresuCplog", query = "SELECT c FROM CppLogistica c WHERE c.memoDiponePresuCplog = :memoDiponePresuCplog"),
    @NamedQuery(name = "CppLogistica.findByNroCertificaPresuCplog", query = "SELECT c FROM CppLogistica c WHERE c.nroCertificaPresuCplog = :nroCertificaPresuCplog"),
    @NamedQuery(name = "CppLogistica.findByValorEmitidoCplog", query = "SELECT c FROM CppLogistica c WHERE c.valorEmitidoCplog = :valorEmitidoCplog"),
    @NamedQuery(name = "CppLogistica.findByValorConsumidoCplog", query = "SELECT c FROM CppLogistica c WHERE c.valorConsumidoCplog = :valorConsumidoCplog"),
    @NamedQuery(name = "CppLogistica.findByMemoPagoCplog", query = "SELECT c FROM CppLogistica c WHERE c.memoPagoCplog = :memoPagoCplog"),
    @NamedQuery(name = "CppLogistica.findByActivoCplog", query = "SELECT c FROM CppLogistica c WHERE c.activoCplog = :activoCplog"),
    @NamedQuery(name = "CppLogistica.findByUsuarioIngre", query = "SELECT c FROM CppLogistica c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppLogistica.findByFechaIngre", query = "SELECT c FROM CppLogistica c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppLogistica.findByHoraIngre", query = "SELECT c FROM CppLogistica c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppLogistica.findByUsuarioActua", query = "SELECT c FROM CppLogistica c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppLogistica.findByFechaActua", query = "SELECT c FROM CppLogistica c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppLogistica.findByHoraActua", query = "SELECT c FROM CppLogistica c WHERE c.horaActua = :horaActua")})
public class CppLogistica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cplog", nullable = false)
    private Integer ideCplog;
    @Size(max = 50)
    @Column(name = "memo_autoriza_cplog", length = 50)
    private String memoAutorizaCplog;
    @Size(max = 50)
    @Column(name = "memo_dipone_presu_cplog", length = 50)
    private String memoDiponePresuCplog;
    @Size(max = 50)
    @Column(name = "nro_certifica_presu_cplog", length = 50)
    private String nroCertificaPresuCplog;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_emitido_cplog", precision = 12, scale = 2)
    private BigDecimal valorEmitidoCplog;
    @Column(name = "valor_consumido_cplog", precision = 12, scale = 2)
    private BigDecimal valorConsumidoCplog;
    @Size(max = 50)
    @Column(name = "memo_pago_cplog", length = 50)
    private String memoPagoCplog;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cplog", nullable = false)
    private boolean activoCplog;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_cptil", referencedColumnName = "ide_cptil")
    @ManyToOne
    private CppTipoLogistica ideCptil;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;

    public CppLogistica() {
    }

    public CppLogistica(Integer ideCplog) {
        this.ideCplog = ideCplog;
    }

    public CppLogistica(Integer ideCplog, boolean activoCplog) {
        this.ideCplog = ideCplog;
        this.activoCplog = activoCplog;
    }

    public Integer getIdeCplog() {
        return ideCplog;
    }

    public void setIdeCplog(Integer ideCplog) {
        this.ideCplog = ideCplog;
    }

    public String getMemoAutorizaCplog() {
        return memoAutorizaCplog;
    }

    public void setMemoAutorizaCplog(String memoAutorizaCplog) {
        this.memoAutorizaCplog = memoAutorizaCplog;
    }

    public String getMemoDiponePresuCplog() {
        return memoDiponePresuCplog;
    }

    public void setMemoDiponePresuCplog(String memoDiponePresuCplog) {
        this.memoDiponePresuCplog = memoDiponePresuCplog;
    }

    public String getNroCertificaPresuCplog() {
        return nroCertificaPresuCplog;
    }

    public void setNroCertificaPresuCplog(String nroCertificaPresuCplog) {
        this.nroCertificaPresuCplog = nroCertificaPresuCplog;
    }

    public BigDecimal getValorEmitidoCplog() {
        return valorEmitidoCplog;
    }

    public void setValorEmitidoCplog(BigDecimal valorEmitidoCplog) {
        this.valorEmitidoCplog = valorEmitidoCplog;
    }

    public BigDecimal getValorConsumidoCplog() {
        return valorConsumidoCplog;
    }

    public void setValorConsumidoCplog(BigDecimal valorConsumidoCplog) {
        this.valorConsumidoCplog = valorConsumidoCplog;
    }

    public String getMemoPagoCplog() {
        return memoPagoCplog;
    }

    public void setMemoPagoCplog(String memoPagoCplog) {
        this.memoPagoCplog = memoPagoCplog;
    }

    public boolean getActivoCplog() {
        return activoCplog;
    }

    public void setActivoCplog(boolean activoCplog) {
        this.activoCplog = activoCplog;
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

    public CppTipoLogistica getIdeCptil() {
        return ideCptil;
    }

    public void setIdeCptil(CppTipoLogistica ideCptil) {
        this.ideCptil = ideCptil;
    }

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCplog != null ? ideCplog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppLogistica)) {
            return false;
        }
        CppLogistica other = (CppLogistica) object;
        if ((this.ideCplog == null && other.ideCplog != null) || (this.ideCplog != null && !this.ideCplog.equals(other.ideCplog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppLogistica[ ideCplog=" + ideCplog + " ]";
    }
    
}
