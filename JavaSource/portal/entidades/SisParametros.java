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
@Table(name = "sis_parametros", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisParametros.findAll", query = "SELECT s FROM SisParametros s"),
    @NamedQuery(name = "SisParametros.findByIdePara", query = "SELECT s FROM SisParametros s WHERE s.idePara = :idePara"),
    @NamedQuery(name = "SisParametros.findByNomPara", query = "SELECT s FROM SisParametros s WHERE s.nomPara = :nomPara"),
    @NamedQuery(name = "SisParametros.findByDescripcionPara", query = "SELECT s FROM SisParametros s WHERE s.descripcionPara = :descripcionPara"),
    @NamedQuery(name = "SisParametros.findByValorPara", query = "SELECT s FROM SisParametros s WHERE s.valorPara = :valorPara"),
    @NamedQuery(name = "SisParametros.findByTablaPara", query = "SELECT s FROM SisParametros s WHERE s.tablaPara = :tablaPara"),
    @NamedQuery(name = "SisParametros.findByCampoCodigoPara", query = "SELECT s FROM SisParametros s WHERE s.campoCodigoPara = :campoCodigoPara"),
    @NamedQuery(name = "SisParametros.findByCampoNombrePara", query = "SELECT s FROM SisParametros s WHERE s.campoNombrePara = :campoNombrePara"),
    @NamedQuery(name = "SisParametros.findByUsuarioIngre", query = "SELECT s FROM SisParametros s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisParametros.findByFechaIngre", query = "SELECT s FROM SisParametros s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisParametros.findByUsuarioActua", query = "SELECT s FROM SisParametros s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisParametros.findByFechaActua", query = "SELECT s FROM SisParametros s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisParametros.findByHoraIngre", query = "SELECT s FROM SisParametros s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisParametros.findByHoraActua", query = "SELECT s FROM SisParametros s WHERE s.horaActua = :horaActua")})
public class SisParametros implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_para", nullable = false)
    private Integer idePara;
    @Size(max = 50)
    @Column(name = "nom_para", length = 50)
    private String nomPara;
    @Size(max = 2000)
    @Column(name = "descripcion_para", length = 2000)
    private String descripcionPara;
    @Size(max = 60)
    @Column(name = "valor_para", length = 60)
    private String valorPara;
    @Size(max = 60)
    @Column(name = "tabla_para", length = 60)
    private String tablaPara;
    @Size(max = 40)
    @Column(name = "campo_codigo_para", length = 40)
    private String campoCodigoPara;
    @Size(max = 50)
    @Column(name = "campo_nombre_para", length = 50)
    private String campoNombrePara;
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
    @JoinColumn(name = "ide_modu", referencedColumnName = "ide_modu")
    @ManyToOne
    private SisModulo ideModu;
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;

    public SisParametros() {
    }

    public SisParametros(Integer idePara) {
        this.idePara = idePara;
    }

    public Integer getIdePara() {
        return idePara;
    }

    public void setIdePara(Integer idePara) {
        this.idePara = idePara;
    }

    public String getNomPara() {
        return nomPara;
    }

    public void setNomPara(String nomPara) {
        this.nomPara = nomPara;
    }

    public String getDescripcionPara() {
        return descripcionPara;
    }

    public void setDescripcionPara(String descripcionPara) {
        this.descripcionPara = descripcionPara;
    }

    public String getValorPara() {
        return valorPara;
    }

    public void setValorPara(String valorPara) {
        this.valorPara = valorPara;
    }

    public String getTablaPara() {
        return tablaPara;
    }

    public void setTablaPara(String tablaPara) {
        this.tablaPara = tablaPara;
    }

    public String getCampoCodigoPara() {
        return campoCodigoPara;
    }

    public void setCampoCodigoPara(String campoCodigoPara) {
        this.campoCodigoPara = campoCodigoPara;
    }

    public String getCampoNombrePara() {
        return campoNombrePara;
    }

    public void setCampoNombrePara(String campoNombrePara) {
        this.campoNombrePara = campoNombrePara;
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

    public SisModulo getIdeModu() {
        return ideModu;
    }

    public void setIdeModu(SisModulo ideModu) {
        this.ideModu = ideModu;
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
        hash += (idePara != null ? idePara.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisParametros)) {
            return false;
        }
        SisParametros other = (SisParametros) object;
        if ((this.idePara == null && other.idePara != null) || (this.idePara != null && !this.idePara.equals(other.idePara))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisParametros[ idePara=" + idePara + " ]";
    }
    
}
