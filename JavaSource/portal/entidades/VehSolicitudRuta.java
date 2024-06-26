/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
 * @author abecerra
 */
@Entity
@Table(name = "veh_solicitud_ruta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehSolicitudRuta.findAll", query = "SELECT v FROM VehSolicitudRuta v"),
    @NamedQuery(name = "VehSolicitudRuta.findByIdeVesru", query = "SELECT v FROM VehSolicitudRuta v WHERE v.ideVesru = :ideVesru"),
    @NamedQuery(name = "VehSolicitudRuta.findByDetalleVesru", query = "SELECT v FROM VehSolicitudRuta v WHERE v.detalleVesru = :detalleVesru"),
    @NamedQuery(name = "VehSolicitudRuta.findByActivoVesru", query = "SELECT v FROM VehSolicitudRuta v WHERE v.activoVesru = :activoVesru"),
    @NamedQuery(name = "VehSolicitudRuta.findByUsuarioIngre", query = "SELECT v FROM VehSolicitudRuta v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehSolicitudRuta.findByFechaIngre", query = "SELECT v FROM VehSolicitudRuta v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehSolicitudRuta.findByHoraIngre", query = "SELECT v FROM VehSolicitudRuta v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehSolicitudRuta.findByUsuarioActua", query = "SELECT v FROM VehSolicitudRuta v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehSolicitudRuta.findByFechaActua", query = "SELECT v FROM VehSolicitudRuta v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehSolicitudRuta.findByHoraActua", query = "SELECT v FROM VehSolicitudRuta v WHERE v.horaActua = :horaActua"),
	@NamedQuery(name = "VehSolicitudRuta.findByIdeSalidaVesru", query = "SELECT v FROM VehSolicitudRuta v WHERE v.ideSalidaVerut = :ideSalidaVerut"),
    @NamedQuery(name = "VehSolicitudRuta.findByDetalleSalidaVesru", query = "SELECT v FROM VehSolicitudRuta v WHERE v.detalleSalidaVesru = :detalleSalidaVesru")})


public class VehSolicitudRuta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vesru")
    private Long ideVesru;
    @Column(name = "detalle_vesru")
    private String detalleVesru;
    @Column(name = "activo_vesru")
    private Boolean activoVesru;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_vesol", referencedColumnName = "ide_vesol")
    @ManyToOne
    private VehSolicitud ideVesol;
    @JoinColumn(name = "ide_verut", referencedColumnName = "ide_verut")
    @ManyToOne
    private VehRuta ideVerut;
    @Column(name = "ide_salida_verut")
    private Long ideSalidaVerut;
    @Column(name = "detalle_salida_vesru")
    private String detalleSalidaVesru;
    
    

    public VehSolicitudRuta() {
    }

    public VehSolicitudRuta(Long ideVesru) {
        this.ideVesru = ideVesru;
    }

    public Long getIdeVesru() {
        return ideVesru;
    }

    public void setIdeVesru(Long ideVesru) {
        this.ideVesru = ideVesru;
    }

    public String getDetalleVesru() {
        return detalleVesru;
    }

    public void setDetalleVesru(String detalleVesru) {
        this.detalleVesru = detalleVesru;
    }

    public Boolean getActivoVesru() {
        return activoVesru;
    }

    public void setActivoVesru(Boolean activoVesru) {
        this.activoVesru = activoVesru;
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

    public VehSolicitud getIdeVesol() {
        return ideVesol;
    }

    public void setIdeVesol(VehSolicitud ideVesol) {
        this.ideVesol = ideVesol;
    }

    public VehRuta getIdeVerut() {
        return ideVerut;
    }

    public void setIdeVerut(VehRuta ideVerut) {
        this.ideVerut = ideVerut;
    }



	public Long getIdeSalidaVerut() {
		return ideSalidaVerut;
    }

	public void setIdeSalidaVerut(Long ideSalidaVerut) {
		this.ideSalidaVerut = ideSalidaVerut;
	}

	public String getDetalleSalidaVesru() {
		return detalleSalidaVesru;
	}

	public void setDetalleSalidaVesru(String detalleSalidaVesru) {
		this.detalleSalidaVesru = detalleSalidaVesru;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVesru != null ? ideVesru.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehSolicitudRuta)) {
            return false;
        }
        VehSolicitudRuta other = (VehSolicitudRuta) object;
        if ((this.ideVesru == null && other.ideVesru != null) || (this.ideVesru != null && !this.ideVesru.equals(other.ideVesru))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehSolicitudRuta[ ideVesru=" + ideVesru + " ]";
    }
    
}
