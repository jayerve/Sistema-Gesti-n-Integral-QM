/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author awbecerra
 */
@Embeddable
public class GthRegistroMilitarPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtrem", nullable = false)
    private int ideGtrem;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtemp", nullable = false)
    private int ideGtemp;

    public GthRegistroMilitarPK() {
    }

    public GthRegistroMilitarPK(int ideGtrem, int ideGtemp) {
        this.ideGtrem = ideGtrem;
        this.ideGtemp = ideGtemp;
    }

    public int getIdeGtrem() {
        return ideGtrem;
    }

    public void setIdeGtrem(int ideGtrem) {
        this.ideGtrem = ideGtrem;
    }

    public int getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(int ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGtrem;
        hash += (int) ideGtemp;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthRegistroMilitarPK)) {
            return false;
        }
        GthRegistroMilitarPK other = (GthRegistroMilitarPK) object;
        if (this.ideGtrem != other.ideGtrem) {
            return false;
        }
        if (this.ideGtemp != other.ideGtemp) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthRegistroMilitarPK[ ideGtrem=" + ideGtrem + ", ideGtemp=" + ideGtemp + " ]";
    }
    
}
