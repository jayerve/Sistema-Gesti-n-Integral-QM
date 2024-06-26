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
@Table(name = "spi_transferencias", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SpiTransferencias.findAll", query = "SELECT s FROM SpiTransferencias s"),
    @NamedQuery(name = "SpiTransferencias.findByIdeSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.ideSptra = :ideSptra"),
    @NamedQuery(name = "SpiTransferencias.findBySecuecialSpiSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.secuecialSpiSptra = :secuecialSpiSptra"),
    @NamedQuery(name = "SpiTransferencias.findByNroCuentaBancoSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.nroCuentaBancoSptra = :nroCuentaBancoSptra"),
    @NamedQuery(name = "SpiTransferencias.findByCodigoBancoSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.codigoBancoSptra = :codigoBancoSptra"),
    @NamedQuery(name = "SpiTransferencias.findByInstitucionSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.institucionSptra = :institucionSptra"),
    @NamedQuery(name = "SpiTransferencias.findByLocalidadSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.localidadSptra = :localidadSptra"),
    @NamedQuery(name = "SpiTransferencias.findByTipoCuentaSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.tipoCuentaSptra = :tipoCuentaSptra"),
    @NamedQuery(name = "SpiTransferencias.findByDetalleTranferenciaSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.detalleTranferenciaSptra = :detalleTranferenciaSptra"),
    @NamedQuery(name = "SpiTransferencias.findByFechaSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.fechaSptra = :fechaSptra"),
    @NamedQuery(name = "SpiTransferencias.findByActivoSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.activoSptra = :activoSptra"),
    @NamedQuery(name = "SpiTransferencias.findByUsuarioIngre", query = "SELECT s FROM SpiTransferencias s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SpiTransferencias.findByFechaIngre", query = "SELECT s FROM SpiTransferencias s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SpiTransferencias.findByUsuarioActua", query = "SELECT s FROM SpiTransferencias s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SpiTransferencias.findByFechaActua", query = "SELECT s FROM SpiTransferencias s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SpiTransferencias.findByHoraIngre", query = "SELECT s FROM SpiTransferencias s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SpiTransferencias.findByHoraActua", query = "SELECT s FROM SpiTransferencias s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SpiTransferencias.findByHoraSptra", query = "SELECT s FROM SpiTransferencias s WHERE s.horaSptra = :horaSptra")})
public class SpiTransferencias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sptra", nullable = false)
    private Integer ideSptra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuecial_spi_sptra", nullable = false)
    private int secuecialSpiSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nro_cuenta_banco_sptra", nullable = false, length = 50)
    private String nroCuentaBancoSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo_banco_sptra", nullable = false, length = 50)
    private String codigoBancoSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "institucion_sptra", nullable = false, length = 50)
    private String institucionSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "localidad_sptra", nullable = false, length = 50)
    private String localidadSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipo_cuenta_sptra", nullable = false, length = 50)
    private String tipoCuentaSptra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_tranferencia_sptra", nullable = false, length = 50)
    private String detalleTranferenciaSptra;
    @Column(name = "fecha_sptra")
    @Temporal(TemporalType.DATE)
    private Date fechaSptra;
    @Column(name = "activo_sptra")
    private Boolean activoSptra;
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
    @Column(name = "hora_sptra")
    @Temporal(TemporalType.TIME)
    private Date horaSptra;
    @OneToMany(mappedBy = "ideSptra")
    private List<SpiTransferenciasDetalle> spiTransferenciasDetalleList;

    public SpiTransferencias() {
    }

    public SpiTransferencias(Integer ideSptra) {
        this.ideSptra = ideSptra;
    }

    public SpiTransferencias(Integer ideSptra, int secuecialSpiSptra, String nroCuentaBancoSptra, String codigoBancoSptra, String institucionSptra, String localidadSptra, String tipoCuentaSptra, String detalleTranferenciaSptra) {
        this.ideSptra = ideSptra;
        this.secuecialSpiSptra = secuecialSpiSptra;
        this.nroCuentaBancoSptra = nroCuentaBancoSptra;
        this.codigoBancoSptra = codigoBancoSptra;
        this.institucionSptra = institucionSptra;
        this.localidadSptra = localidadSptra;
        this.tipoCuentaSptra = tipoCuentaSptra;
        this.detalleTranferenciaSptra = detalleTranferenciaSptra;
    }

    public Integer getIdeSptra() {
        return ideSptra;
    }

    public void setIdeSptra(Integer ideSptra) {
        this.ideSptra = ideSptra;
    }

    public int getSecuecialSpiSptra() {
        return secuecialSpiSptra;
    }

    public void setSecuecialSpiSptra(int secuecialSpiSptra) {
        this.secuecialSpiSptra = secuecialSpiSptra;
    }

    public String getNroCuentaBancoSptra() {
        return nroCuentaBancoSptra;
    }

    public void setNroCuentaBancoSptra(String nroCuentaBancoSptra) {
        this.nroCuentaBancoSptra = nroCuentaBancoSptra;
    }

    public String getCodigoBancoSptra() {
        return codigoBancoSptra;
    }

    public void setCodigoBancoSptra(String codigoBancoSptra) {
        this.codigoBancoSptra = codigoBancoSptra;
    }

    public String getInstitucionSptra() {
        return institucionSptra;
    }

    public void setInstitucionSptra(String institucionSptra) {
        this.institucionSptra = institucionSptra;
    }

    public String getLocalidadSptra() {
        return localidadSptra;
    }

    public void setLocalidadSptra(String localidadSptra) {
        this.localidadSptra = localidadSptra;
    }

    public String getTipoCuentaSptra() {
        return tipoCuentaSptra;
    }

    public void setTipoCuentaSptra(String tipoCuentaSptra) {
        this.tipoCuentaSptra = tipoCuentaSptra;
    }

    public String getDetalleTranferenciaSptra() {
        return detalleTranferenciaSptra;
    }

    public void setDetalleTranferenciaSptra(String detalleTranferenciaSptra) {
        this.detalleTranferenciaSptra = detalleTranferenciaSptra;
    }

    public Date getFechaSptra() {
        return fechaSptra;
    }

    public void setFechaSptra(Date fechaSptra) {
        this.fechaSptra = fechaSptra;
    }

    public Boolean getActivoSptra() {
        return activoSptra;
    }

    public void setActivoSptra(Boolean activoSptra) {
        this.activoSptra = activoSptra;
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

    public Date getHoraSptra() {
        return horaSptra;
    }

    public void setHoraSptra(Date horaSptra) {
        this.horaSptra = horaSptra;
    }

    public List<SpiTransferenciasDetalle> getSpiTransferenciasDetalleList() {
        return spiTransferenciasDetalleList;
    }

    public void setSpiTransferenciasDetalleList(List<SpiTransferenciasDetalle> spiTransferenciasDetalleList) {
        this.spiTransferenciasDetalleList = spiTransferenciasDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSptra != null ? ideSptra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SpiTransferencias)) {
            return false;
        }
        SpiTransferencias other = (SpiTransferencias) object;
        if ((this.ideSptra == null && other.ideSptra != null) || (this.ideSptra != null && !this.ideSptra.equals(other.ideSptra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SpiTransferencias[ ideSptra=" + ideSptra + " ]";
    }
    
}
