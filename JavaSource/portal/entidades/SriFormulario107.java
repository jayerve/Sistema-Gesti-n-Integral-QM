/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jayerve
 */
@Entity
@Table(name = "sri_formulario_107",catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriFormulario107.findAll", query = "SELECT s FROM SriFormulario107 s"),
    @NamedQuery(name = "SriFormulario107.findByIdeSrfor", query = "SELECT s FROM SriFormulario107 s WHERE s.ideSrfor = :ideSrfor"),
    @NamedQuery(name = "SriFormulario107.findByR301Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r301Srfor = :r301Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR303Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r303Srfor = :r303Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR305Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r305Srfor = :r305Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR307Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r307Srfor = :r307Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR311Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r311Srfor = :r311Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR313Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r313Srfor = :r313Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR315Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r315Srfor = :r315Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR317Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r317Srfor = :r317Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR351Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r351Srfor = :r351Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR353Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r353Srfor = :r353Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR361Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r361Srfor = :r361Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR363Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r363Srfor = :r363Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR365Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r365Srfor = :r365Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR367Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r367Srfor = :r367Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR369Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r369Srfor = :r369Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR371Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r371Srfor = :r371Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR373Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r373Srfor = :r373Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR381Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r381Srfor = :r381Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR399Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r399Srfor = :r399Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR401Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r401Srfor = :r401Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR403Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r403Srfor = :r403Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR405Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r405Srfor = :r405Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR407Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r407Srfor = :r407Srfor"),
    @NamedQuery(name = "SriFormulario107.findByR349Srfor", query = "SELECT s FROM SriFormulario107 s WHERE s.r349Srfor = :r349Srfor"),
    @NamedQuery(name = "SriFormulario107.findByActivoSrfor", query = "SELECT s FROM SriFormulario107 s WHERE s.activoSrfor = :activoSrfor"),
    @NamedQuery(name = "SriFormulario107.findByTipoFormularioSrfor", query = "SELECT s FROM SriFormulario107 s WHERE s.tipoFormularioSrfor = :tipoFormularioSrfor"),
    @NamedQuery(name = "SriFormulario107.findByUsuarioIngre", query = "SELECT s FROM SriFormulario107 s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriFormulario107.findByFechaIngre", query = "SELECT s FROM SriFormulario107 s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriFormulario107.findByUsuarioActua", query = "SELECT s FROM SriFormulario107 s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriFormulario107.findByFechaActua", query = "SELECT s FROM SriFormulario107 s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriFormulario107.findByHoraIngre", query = "SELECT s FROM SriFormulario107 s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriFormulario107.findByHoraActua", query = "SELECT s FROM SriFormulario107 s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SriFormulario107.findByIdeGtemp", query = "SELECT s FROM SriFormulario107 s WHERE s.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "SriFormulario107.findByFechaRegistroSrfor", query = "SELECT s FROM SriFormulario107 s WHERE s.fechaRegistroSrfor = :fechaRegistroSrfor")})
public class SriFormulario107 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srfor")
    private Integer ideSrfor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "r301_srfor")
    private BigDecimal r301Srfor;
    @Column(name = "r303_srfor")
    private BigDecimal r303Srfor;
    @Column(name = "r305_srfor")
    private BigDecimal r305Srfor;
    @Column(name = "r307_srfor")
    private BigDecimal r307Srfor;
    @Column(name = "r311_srfor")
    private BigDecimal r311Srfor;
    @Column(name = "r313_srfor")
    private BigDecimal r313Srfor;
    @Column(name = "r315_srfor")
    private BigDecimal r315Srfor;
    @Column(name = "r317_srfor")
    private BigDecimal r317Srfor;
    @Column(name = "r351_srfor")
    private BigDecimal r351Srfor;
    @Column(name = "r353_srfor")
    private BigDecimal r353Srfor;
    @Column(name = "r361_srfor")
    private BigDecimal r361Srfor;
    @Column(name = "r363_srfor")
    private BigDecimal r363Srfor;
    @Column(name = "r365_srfor")
    private BigDecimal r365Srfor;
    @Column(name = "r367_srfor")
    private BigDecimal r367Srfor;
    @Column(name = "r369_srfor")
    private BigDecimal r369Srfor;
    @Column(name = "r371_srfor")
    private BigDecimal r371Srfor;
    @Column(name = "r373_srfor")
    private BigDecimal r373Srfor;
    @Column(name = "r381_srfor")
    private BigDecimal r381Srfor;
    @Column(name = "r399_srfor")
    private BigDecimal r399Srfor;
    @Column(name = "r401_srfor")
    private BigDecimal r401Srfor;
    @Column(name = "r403_srfor")
    private BigDecimal r403Srfor;
    @Column(name = "r405_srfor")
    private BigDecimal r405Srfor;
    @Column(name = "r407_srfor")
    private BigDecimal r407Srfor;
    @Column(name = "r349_srfor")
    private BigDecimal r349Srfor;
    @Column(name = "activo_srfor")
    private Boolean activoSrfor;
    @Column(name = "tipo_formulario_srfor")
    private Integer tipoFormularioSrfor;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
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
    @Column(name = "fecha_registro_srfor")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroSrfor;
    @JoinColumn(name = "ide_srimr", referencedColumnName = "ide_srimr")
    @ManyToOne
    private SriImpuestoRenta ideSrimr;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public SriFormulario107() {
    }

    public SriFormulario107(Integer ideSrfor) {
        this.ideSrfor = ideSrfor;
    }

    public Integer getIdeSrfor() {
        return ideSrfor;
    }

    public void setIdeSrfor(Integer ideSrfor) {
        this.ideSrfor = ideSrfor;
    }

    public BigDecimal getR301Srfor() {
        return r301Srfor;
    }

    public void setR301Srfor(BigDecimal r301Srfor) {
        this.r301Srfor = r301Srfor;
    }

    public BigDecimal getR303Srfor() {
        return r303Srfor;
    }

    public void setR303Srfor(BigDecimal r303Srfor) {
        this.r303Srfor = r303Srfor;
    }

    public BigDecimal getR305Srfor() {
        return r305Srfor;
    }

    public void setR305Srfor(BigDecimal r305Srfor) {
        this.r305Srfor = r305Srfor;
    }

    public BigDecimal getR307Srfor() {
        return r307Srfor;
    }

    public void setR307Srfor(BigDecimal r307Srfor) {
        this.r307Srfor = r307Srfor;
    }

    public BigDecimal getR311Srfor() {
        return r311Srfor;
    }

    public void setR311Srfor(BigDecimal r311Srfor) {
        this.r311Srfor = r311Srfor;
    }

    public BigDecimal getR313Srfor() {
        return r313Srfor;
    }

    public void setR313Srfor(BigDecimal r313Srfor) {
        this.r313Srfor = r313Srfor;
    }

    public BigDecimal getR315Srfor() {
        return r315Srfor;
    }

    public void setR315Srfor(BigDecimal r315Srfor) {
        this.r315Srfor = r315Srfor;
    }

    public BigDecimal getR317Srfor() {
        return r317Srfor;
    }

    public void setR317Srfor(BigDecimal r317Srfor) {
        this.r317Srfor = r317Srfor;
    }

    public BigDecimal getR351Srfor() {
        return r351Srfor;
    }

    public void setR351Srfor(BigDecimal r351Srfor) {
        this.r351Srfor = r351Srfor;
    }

    public BigDecimal getR353Srfor() {
        return r353Srfor;
    }

    public void setR353Srfor(BigDecimal r353Srfor) {
        this.r353Srfor = r353Srfor;
    }

    public BigDecimal getR361Srfor() {
        return r361Srfor;
    }

    public void setR361Srfor(BigDecimal r361Srfor) {
        this.r361Srfor = r361Srfor;
    }

    public BigDecimal getR363Srfor() {
        return r363Srfor;
    }

    public void setR363Srfor(BigDecimal r363Srfor) {
        this.r363Srfor = r363Srfor;
    }

    public BigDecimal getR365Srfor() {
        return r365Srfor;
    }

    public void setR365Srfor(BigDecimal r365Srfor) {
        this.r365Srfor = r365Srfor;
    }

    public BigDecimal getR367Srfor() {
        return r367Srfor;
    }

    public void setR367Srfor(BigDecimal r367Srfor) {
        this.r367Srfor = r367Srfor;
    }

    public BigDecimal getR369Srfor() {
        return r369Srfor;
    }

    public void setR369Srfor(BigDecimal r369Srfor) {
        this.r369Srfor = r369Srfor;
    }

    public BigDecimal getR371Srfor() {
        return r371Srfor;
    }

    public void setR371Srfor(BigDecimal r371Srfor) {
        this.r371Srfor = r371Srfor;
    }

    public BigDecimal getR373Srfor() {
        return r373Srfor;
    }

    public void setR373Srfor(BigDecimal r373Srfor) {
        this.r373Srfor = r373Srfor;
    }

    public BigDecimal getR381Srfor() {
        return r381Srfor;
    }

    public void setR381Srfor(BigDecimal r381Srfor) {
        this.r381Srfor = r381Srfor;
    }

    public BigDecimal getR399Srfor() {
        return r399Srfor;
    }

    public void setR399Srfor(BigDecimal r399Srfor) {
        this.r399Srfor = r399Srfor;
    }

    public BigDecimal getR401Srfor() {
        return r401Srfor;
    }

    public void setR401Srfor(BigDecimal r401Srfor) {
        this.r401Srfor = r401Srfor;
    }

    public BigDecimal getR403Srfor() {
        return r403Srfor;
    }

    public void setR403Srfor(BigDecimal r403Srfor) {
        this.r403Srfor = r403Srfor;
    }

    public BigDecimal getR405Srfor() {
        return r405Srfor;
    }

    public void setR405Srfor(BigDecimal r405Srfor) {
        this.r405Srfor = r405Srfor;
    }

    public BigDecimal getR407Srfor() {
        return r407Srfor;
    }

    public void setR407Srfor(BigDecimal r407Srfor) {
        this.r407Srfor = r407Srfor;
    }

    public BigDecimal getR349Srfor() {
        return r349Srfor;
    }

    public void setR349Srfor(BigDecimal r349Srfor) {
        this.r349Srfor = r349Srfor;
    }

    public Boolean getActivoSrfor() {
        return activoSrfor;
    }

    public void setActivoSrfor(Boolean activoSrfor) {
        this.activoSrfor = activoSrfor;
    }

    public Integer getTipoFormularioSrfor() {
        return tipoFormularioSrfor;
    }

    public void setTipoFormularioSrfor(Integer tipoFormularioSrfor) {
        this.tipoFormularioSrfor = tipoFormularioSrfor;
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

    public Date getFechaRegistroSrfor() {
        return fechaRegistroSrfor;
    }

    public void setFechaRegistroSrfor(Date fechaRegistroSrfor) {
        this.fechaRegistroSrfor = fechaRegistroSrfor;
    }

    public SriImpuestoRenta getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(SriImpuestoRenta ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrfor != null ? ideSrfor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriFormulario107)) {
            return false;
        }
        SriFormulario107 other = (SriFormulario107) object;
        if ((this.ideSrfor == null && other.ideSrfor != null) || (this.ideSrfor != null && !this.ideSrfor.equals(other.ideSrfor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sampudb.SriFormulario107[ ideSrfor=" + ideSrfor + " ]";
    }
    
}
