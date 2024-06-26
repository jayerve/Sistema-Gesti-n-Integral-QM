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
@Table(name = "tes_cliente_tarifa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesClienteTarifa.findAll", query = "SELECT t FROM TesClienteTarifa t"),
    @NamedQuery(name = "TesClienteTarifa.findByIdeTeclt", query = "SELECT t FROM TesClienteTarifa t WHERE t.ideTeclt = :ideTeclt"),
    @NamedQuery(name = "TesClienteTarifa.findByActivoTeclt", query = "SELECT t FROM TesClienteTarifa t WHERE t.activoTeclt = :activoTeclt"),
    @NamedQuery(name = "TesClienteTarifa.findByUsuarioIngre", query = "SELECT t FROM TesClienteTarifa t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesClienteTarifa.findByFechaIngre", query = "SELECT t FROM TesClienteTarifa t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesClienteTarifa.findByHoraIngre", query = "SELECT t FROM TesClienteTarifa t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesClienteTarifa.findByUsuarioActua", query = "SELECT t FROM TesClienteTarifa t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesClienteTarifa.findByFechaActua", query = "SELECT t FROM TesClienteTarifa t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesClienteTarifa.findByHoraActua", query = "SELECT t FROM TesClienteTarifa t WHERE t.horaActua = :horaActua")})
public class TesClienteTarifa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_teclt", nullable = false)
    private Long ideTeclt;
    @Column(name = "activo_teclt")
    private Boolean activoTeclt;
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
    @JoinColumn(name = "ide_temat", referencedColumnName = "ide_temat", nullable = false)
    @ManyToOne(optional = false)
    private TesMaterialTarifa ideTemat;
    @JoinColumn(name = "ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes ideRecli;

    public TesClienteTarifa() {
    }

    public TesClienteTarifa(Long ideTeclt) {
        this.ideTeclt = ideTeclt;
    }

    public Long getIdeTeclt() {
        return ideTeclt;
    }

    public void setIdeTeclt(Long ideTeclt) {
        this.ideTeclt = ideTeclt;
    }

    public Boolean getActivoTeclt() {
        return activoTeclt;
    }

    public void setActivoTeclt(Boolean activoTeclt) {
        this.activoTeclt = activoTeclt;
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

    public TesMaterialTarifa getIdeTemat() {
        return ideTemat;
    }

    public void setIdeTemat(TesMaterialTarifa ideTemat) {
        this.ideTemat = ideTemat;
    }

    public RecClientes getIdeRecli() {
        return ideRecli;
    }

    public void setIdeRecli(RecClientes ideRecli) {
        this.ideRecli = ideRecli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTeclt != null ? ideTeclt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesClienteTarifa)) {
            return false;
        }
        TesClienteTarifa other = (TesClienteTarifa) object;
        if ((this.ideTeclt == null && other.ideTeclt != null) || (this.ideTeclt != null && !this.ideTeclt.equals(other.ideTeclt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesClienteTarifa[ ideTeclt=" + ideTeclt + " ]";
    }
    
}
