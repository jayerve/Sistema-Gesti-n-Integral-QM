/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "sao_custodio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoCustodio.findAll", query = "SELECT s FROM SaoCustodio s"),
    @NamedQuery(name = "SaoCustodio.findByIdeSacus", query = "SELECT s FROM SaoCustodio s WHERE s.ideSacus = :ideSacus"),
    @NamedQuery(name = "SaoCustodio.findByFechaEntregaSacus", query = "SELECT s FROM SaoCustodio s WHERE s.fechaEntregaSacus = :fechaEntregaSacus"),
    @NamedQuery(name = "SaoCustodio.findByNroActaSacus", query = "SELECT s FROM SaoCustodio s WHERE s.nroActaSacus = :nroActaSacus"),
    @NamedQuery(name = "SaoCustodio.findByFotoSacus", query = "SELECT s FROM SaoCustodio s WHERE s.fotoSacus = :fotoSacus"),
    @NamedQuery(name = "SaoCustodio.findByCantidadSacus", query = "SELECT s FROM SaoCustodio s WHERE s.cantidadSacus = :cantidadSacus"),
    @NamedQuery(name = "SaoCustodio.findByFechaDescargaSacus", query = "SELECT s FROM SaoCustodio s WHERE s.fechaDescargaSacus = :fechaDescargaSacus"),
    @NamedQuery(name = "SaoCustodio.findByUsoSacus", query = "SELECT s FROM SaoCustodio s WHERE s.usoSacus = :usoSacus"),
    @NamedQuery(name = "SaoCustodio.findByCodBarrasSacus", query = "SELECT s FROM SaoCustodio s WHERE s.codBarrasSacus = :codBarrasSacus"),
    @NamedQuery(name = "SaoCustodio.findByCustodioSacus", query = "SELECT s FROM SaoCustodio s WHERE s.custodioSacus = :custodioSacus"),
    @NamedQuery(name = "SaoCustodio.findByTipoSacus", query = "SELECT s FROM SaoCustodio s WHERE s.tipoSacus = :tipoSacus"),
    @NamedQuery(name = "SaoCustodio.findByActivoSacus", query = "SELECT s FROM SaoCustodio s WHERE s.activoSacus = :activoSacus"),
    @NamedQuery(name = "SaoCustodio.findByUsuarioIngre", query = "SELECT s FROM SaoCustodio s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoCustodio.findByFechaIngre", query = "SELECT s FROM SaoCustodio s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoCustodio.findByHoraIngre", query = "SELECT s FROM SaoCustodio s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoCustodio.findByUsuarioActua", query = "SELECT s FROM SaoCustodio s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoCustodio.findByFechaActua", query = "SELECT s FROM SaoCustodio s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoCustodio.findByHoraActua", query = "SELECT s FROM SaoCustodio s WHERE s.horaActua = :horaActua")})
public class SaoCustodio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sacus", nullable = false)
    private Integer ideSacus;
    @Column(name = "fecha_entrega_sacus")
    @Temporal(TemporalType.DATE)
    private Date fechaEntregaSacus;
    @Size(max = 50)
    @Column(name = "nro_acta_sacus", length = 50)
    private String nroActaSacus;
    @Size(max = 100)
    @Column(name = "foto_sacus", length = 100)
    private String fotoSacus;
    @Column(name = "cantidad_sacus")
    private Integer cantidadSacus;
    @Column(name = "fecha_descarga_sacus")
    @Temporal(TemporalType.DATE)
    private Date fechaDescargaSacus;
    @Size(max = 1000)
    @Column(name = "uso_sacus", length = 1000)
    private String usoSacus;
    @Size(max = 50)
    @Column(name = "cod_barras_sacus", length = 50)
    private String codBarrasSacus;
    @Size(max = 100)
    @Column(name = "custodio_sacus", length = 100)
    private String custodioSacus;
    @Column(name = "tipo_sacus")
    private Integer tipoSacus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sacus", nullable = false)
    private boolean activoSacus;
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
    @JoinColumn(name = "ide_saubs", referencedColumnName = "ide_saubs")
    @ManyToOne
    private SaoUbicacionSucursal ideSaubs;
    @OneToMany(mappedBy = "saoIdeSacus")
    private List<SaoCustodio> saoCustodioList;
    @JoinColumn(name = "sao_ide_sacus", referencedColumnName = "ide_sacus")
    @ManyToOne
    private SaoCustodio saoIdeSacus;
    @JoinColumn(name = "ide_sabie", referencedColumnName = "ide_sabie")
    @ManyToOne
    private SaoBienes ideSabie;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideSacus")
    private List<SaoDotacionUniforme> saoDotacionUniformeList;

    public SaoCustodio() {
    }

    public SaoCustodio(Integer ideSacus) {
        this.ideSacus = ideSacus;
    }

    public SaoCustodio(Integer ideSacus, boolean activoSacus) {
        this.ideSacus = ideSacus;
        this.activoSacus = activoSacus;
    }

    public Integer getIdeSacus() {
        return ideSacus;
    }

    public void setIdeSacus(Integer ideSacus) {
        this.ideSacus = ideSacus;
    }

    public Date getFechaEntregaSacus() {
        return fechaEntregaSacus;
    }

    public void setFechaEntregaSacus(Date fechaEntregaSacus) {
        this.fechaEntregaSacus = fechaEntregaSacus;
    }

    public String getNroActaSacus() {
        return nroActaSacus;
    }

    public void setNroActaSacus(String nroActaSacus) {
        this.nroActaSacus = nroActaSacus;
    }

    public String getFotoSacus() {
        return fotoSacus;
    }

    public void setFotoSacus(String fotoSacus) {
        this.fotoSacus = fotoSacus;
    }

    public Integer getCantidadSacus() {
        return cantidadSacus;
    }

    public void setCantidadSacus(Integer cantidadSacus) {
        this.cantidadSacus = cantidadSacus;
    }

    public Date getFechaDescargaSacus() {
        return fechaDescargaSacus;
    }

    public void setFechaDescargaSacus(Date fechaDescargaSacus) {
        this.fechaDescargaSacus = fechaDescargaSacus;
    }

    public String getUsoSacus() {
        return usoSacus;
    }

    public void setUsoSacus(String usoSacus) {
        this.usoSacus = usoSacus;
    }

    public String getCodBarrasSacus() {
        return codBarrasSacus;
    }

    public void setCodBarrasSacus(String codBarrasSacus) {
        this.codBarrasSacus = codBarrasSacus;
    }

    public String getCustodioSacus() {
        return custodioSacus;
    }

    public void setCustodioSacus(String custodioSacus) {
        this.custodioSacus = custodioSacus;
    }

    public Integer getTipoSacus() {
        return tipoSacus;
    }

    public void setTipoSacus(Integer tipoSacus) {
        this.tipoSacus = tipoSacus;
    }

    public boolean getActivoSacus() {
        return activoSacus;
    }

    public void setActivoSacus(boolean activoSacus) {
        this.activoSacus = activoSacus;
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

    public SaoUbicacionSucursal getIdeSaubs() {
        return ideSaubs;
    }

    public void setIdeSaubs(SaoUbicacionSucursal ideSaubs) {
        this.ideSaubs = ideSaubs;
    }

    public List<SaoCustodio> getSaoCustodioList() {
        return saoCustodioList;
    }

    public void setSaoCustodioList(List<SaoCustodio> saoCustodioList) {
        this.saoCustodioList = saoCustodioList;
    }

    public SaoCustodio getSaoIdeSacus() {
        return saoIdeSacus;
    }

    public void setSaoIdeSacus(SaoCustodio saoIdeSacus) {
        this.saoIdeSacus = saoIdeSacus;
    }

    public SaoBienes getIdeSabie() {
        return ideSabie;
    }

    public void setIdeSabie(SaoBienes ideSabie) {
        this.ideSabie = ideSabie;
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

    public List<SaoDotacionUniforme> getSaoDotacionUniformeList() {
        return saoDotacionUniformeList;
    }

    public void setSaoDotacionUniformeList(List<SaoDotacionUniforme> saoDotacionUniformeList) {
        this.saoDotacionUniformeList = saoDotacionUniformeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSacus != null ? ideSacus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoCustodio)) {
            return false;
        }
        SaoCustodio other = (SaoCustodio) object;
        if ((this.ideSacus == null && other.ideSacus != null) || (this.ideSacus != null && !this.ideSacus.equals(other.ideSacus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoCustodio[ ideSacus=" + ideSacus + " ]";
    }
    
}
