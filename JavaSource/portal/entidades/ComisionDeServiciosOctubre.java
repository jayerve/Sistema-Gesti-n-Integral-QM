/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "comision_de_servicios_octubre", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ComisionDeServiciosOctubre.findAll", query = "SELECT c FROM ComisionDeServiciosOctubre c"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIdeCosoc", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ideCosoc = :ideCosoc"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEmplcdid", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.emplcdid = :emplcdid"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByGrpocdgo", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.grpocdgo = :grpocdgo"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEmplnmbr", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.emplnmbr = :emplnmbr"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEmplapll", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.emplapll = :emplapll"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEmplctgr", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.emplctgr = :emplctgr"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEmplcrgo", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.emplcrgo = :emplcrgo"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByInga", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.inga = :inga"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngb", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingb = :ingb"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngc", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingc = :ingc"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngd", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingd = :ingd"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByInge", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.inge = :inge"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngf", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingf = :ingf"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngg", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingg = :ingg"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngh", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingh = :ingh"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngi", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingi = :ingi"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngj", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingj = :ingj"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngk", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingk = :ingk"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngl", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingl = :ingl"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngm", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingm = :ingm"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngn", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingn = :ingn"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngo", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingo = :ingo"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngp", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingp = :ingp"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngq", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingq = :ingq"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngr", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingr = :ingr"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngs", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ings = :ings"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngt", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingt = :ingt"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngu", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingu = :ingu"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngv", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingv = :ingv"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngw", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingw = :ingw"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngx", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingx = :ingx"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngy", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingy = :ingy"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngz", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingz = :ingz"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByOIngres", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.oIngres = :oIngres"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIngreso", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.ingreso = :ingreso"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMega", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.mega = :mega"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegb", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megb = :megb"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegc", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megc = :megc"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegd", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megd = :megd"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMege", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.mege = :mege"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegf", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megf = :megf"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegg", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megg = :megg"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegh", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megh = :megh"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegi", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megi = :megi"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegj", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megj = :megj"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegk", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megk = :megk"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegl", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megl = :megl"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegm", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megm = :megm"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegn", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megn = :megn"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMego", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.mego = :mego"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegp", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megp = :megp"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegq", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megq = :megq"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegr", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megr = :megr"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegs", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megs = :megs"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegt", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megt = :megt"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegu", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megu = :megu"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegv", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megv = :megv"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegw", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megw = :megw"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegx", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megx = :megx"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegy", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megy = :megy"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByMegz", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.megz = :megz"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByOEgreso", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.oEgreso = :oEgreso"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByEgreso", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.egreso = :egreso"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByTotal", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.total = :total"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByPatronal", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.patronal = :patronal"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByBaseiess", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.baseiess = :baseiess"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findByIece", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.iece = :iece"),
    @NamedQuery(name = "ComisionDeServiciosOctubre.findBySecap", query = "SELECT c FROM ComisionDeServiciosOctubre c WHERE c.secap = :secap")})
public class ComisionDeServiciosOctubre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_cosoc", nullable = false)
    private Integer ideCosoc;
    @Column(name = "emplcdid")
    private BigInteger emplcdid;
    @Size(max = 10)
    @Column(name = "grpocdgo", length = 10)
    private String grpocdgo;
    @Size(max = 30)
    @Column(name = "emplnmbr", length = 30)
    private String emplnmbr;
    @Size(max = 30)
    @Column(name = "emplapll", length = 30)
    private String emplapll;
    @Column(name = "emplctgr")
    private Integer emplctgr;
    @Size(max = 15)
    @Column(name = "emplcrgo", length = 15)
    private String emplcrgo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "inga", precision = 10, scale = 2)
    private BigDecimal inga;
    @Column(name = "ingb")
    private Integer ingb;
    @Column(name = "ingc")
    private Integer ingc;
    @Column(name = "ingd")
    private Integer ingd;
    @Column(name = "inge")
    private Integer inge;
    @Column(name = "ingf")
    private Integer ingf;
    @Column(name = "ingg")
    private Integer ingg;
    @Column(name = "ingh")
    private Integer ingh;
    @Column(name = "ingi")
    private Integer ingi;
    @Column(name = "ingj")
    private Integer ingj;
    @Column(name = "ingk")
    private Integer ingk;
    @Column(name = "ingl")
    private Integer ingl;
    @Column(name = "ingm")
    private Integer ingm;
    @Column(name = "ingn")
    private Integer ingn;
    @Column(name = "ingo")
    private Integer ingo;
    @Column(name = "ingp")
    private Integer ingp;
    @Column(name = "ingq")
    private Integer ingq;
    @Column(name = "ingr")
    private Integer ingr;
    @Column(name = "ings")
    private Integer ings;
    @Column(name = "ingt")
    private Integer ingt;
    @Column(name = "ingu")
    private Integer ingu;
    @Column(name = "ingv")
    private Integer ingv;
    @Column(name = "ingw")
    private Integer ingw;
    @Column(name = "ingx")
    private Integer ingx;
    @Column(name = "ingy")
    private Integer ingy;
    @Column(name = "ingz")
    private Integer ingz;
    @Column(name = "o_ingres", precision = 10, scale = 2)
    private BigDecimal oIngres;
    @Column(name = "ingreso_", precision = 10, scale = 2)
    private BigDecimal ingreso;
    @Column(name = "mega", precision = 10, scale = 2)
    private BigDecimal mega;
    @Column(name = "megb", precision = 10, scale = 2)
    private BigDecimal megb;
    @Column(name = "megc", precision = 10, scale = 2)
    private BigDecimal megc;
    @Column(name = "megd")
    private Integer megd;
    @Column(name = "mege")
    private Integer mege;
    @Column(name = "megf", precision = 10, scale = 2)
    private BigDecimal megf;
    @Column(name = "megg", precision = 10, scale = 2)
    private BigDecimal megg;
    @Column(name = "megh", precision = 10, scale = 2)
    private BigDecimal megh;
    @Column(name = "megi")
    private Integer megi;
    @Column(name = "megj")
    private Integer megj;
    @Column(name = "megk")
    private Integer megk;
    @Column(name = "megl")
    private Integer megl;
    @Column(name = "megm")
    private Integer megm;
    @Column(name = "megn")
    private Integer megn;
    @Column(name = "mego")
    private Integer mego;
    @Column(name = "megp")
    private Integer megp;
    @Column(name = "megq")
    private Integer megq;
    @Column(name = "megr")
    private Integer megr;
    @Column(name = "megs")
    private Integer megs;
    @Column(name = "megt")
    private Integer megt;
    @Column(name = "megu")
    private Integer megu;
    @Column(name = "megv")
    private Integer megv;
    @Column(name = "megw")
    private Integer megw;
    @Column(name = "megx")
    private Integer megx;
    @Column(name = "megy")
    private Integer megy;
    @Column(name = "megz")
    private Integer megz;
    @Column(name = "o_egreso_", precision = 10, scale = 2)
    private BigDecimal oEgreso;
    @Column(name = "egreso_", precision = 10, scale = 2)
    private BigDecimal egreso;
    @Column(name = "total_", precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "patronal", precision = 10, scale = 2)
    private BigDecimal patronal;
    @Column(name = "baseiess", precision = 10, scale = 2)
    private BigDecimal baseiess;
    @Column(name = "iece", precision = 10, scale = 2)
    private BigDecimal iece;
    @Column(name = "secap")
    private Integer secap;

    public ComisionDeServiciosOctubre() {
    }

    public ComisionDeServiciosOctubre(Integer ideCosoc) {
        this.ideCosoc = ideCosoc;
    }

    public Integer getIdeCosoc() {
        return ideCosoc;
    }

    public void setIdeCosoc(Integer ideCosoc) {
        this.ideCosoc = ideCosoc;
    }

    public BigInteger getEmplcdid() {
        return emplcdid;
    }

    public void setEmplcdid(BigInteger emplcdid) {
        this.emplcdid = emplcdid;
    }

    public String getGrpocdgo() {
        return grpocdgo;
    }

    public void setGrpocdgo(String grpocdgo) {
        this.grpocdgo = grpocdgo;
    }

    public String getEmplnmbr() {
        return emplnmbr;
    }

    public void setEmplnmbr(String emplnmbr) {
        this.emplnmbr = emplnmbr;
    }

    public String getEmplapll() {
        return emplapll;
    }

    public void setEmplapll(String emplapll) {
        this.emplapll = emplapll;
    }

    public Integer getEmplctgr() {
        return emplctgr;
    }

    public void setEmplctgr(Integer emplctgr) {
        this.emplctgr = emplctgr;
    }

    public String getEmplcrgo() {
        return emplcrgo;
    }

    public void setEmplcrgo(String emplcrgo) {
        this.emplcrgo = emplcrgo;
    }

    public BigDecimal getInga() {
        return inga;
    }

    public void setInga(BigDecimal inga) {
        this.inga = inga;
    }

    public Integer getIngb() {
        return ingb;
    }

    public void setIngb(Integer ingb) {
        this.ingb = ingb;
    }

    public Integer getIngc() {
        return ingc;
    }

    public void setIngc(Integer ingc) {
        this.ingc = ingc;
    }

    public Integer getIngd() {
        return ingd;
    }

    public void setIngd(Integer ingd) {
        this.ingd = ingd;
    }

    public Integer getInge() {
        return inge;
    }

    public void setInge(Integer inge) {
        this.inge = inge;
    }

    public Integer getIngf() {
        return ingf;
    }

    public void setIngf(Integer ingf) {
        this.ingf = ingf;
    }

    public Integer getIngg() {
        return ingg;
    }

    public void setIngg(Integer ingg) {
        this.ingg = ingg;
    }

    public Integer getIngh() {
        return ingh;
    }

    public void setIngh(Integer ingh) {
        this.ingh = ingh;
    }

    public Integer getIngi() {
        return ingi;
    }

    public void setIngi(Integer ingi) {
        this.ingi = ingi;
    }

    public Integer getIngj() {
        return ingj;
    }

    public void setIngj(Integer ingj) {
        this.ingj = ingj;
    }

    public Integer getIngk() {
        return ingk;
    }

    public void setIngk(Integer ingk) {
        this.ingk = ingk;
    }

    public Integer getIngl() {
        return ingl;
    }

    public void setIngl(Integer ingl) {
        this.ingl = ingl;
    }

    public Integer getIngm() {
        return ingm;
    }

    public void setIngm(Integer ingm) {
        this.ingm = ingm;
    }

    public Integer getIngn() {
        return ingn;
    }

    public void setIngn(Integer ingn) {
        this.ingn = ingn;
    }

    public Integer getIngo() {
        return ingo;
    }

    public void setIngo(Integer ingo) {
        this.ingo = ingo;
    }

    public Integer getIngp() {
        return ingp;
    }

    public void setIngp(Integer ingp) {
        this.ingp = ingp;
    }

    public Integer getIngq() {
        return ingq;
    }

    public void setIngq(Integer ingq) {
        this.ingq = ingq;
    }

    public Integer getIngr() {
        return ingr;
    }

    public void setIngr(Integer ingr) {
        this.ingr = ingr;
    }

    public Integer getIngs() {
        return ings;
    }

    public void setIngs(Integer ings) {
        this.ings = ings;
    }

    public Integer getIngt() {
        return ingt;
    }

    public void setIngt(Integer ingt) {
        this.ingt = ingt;
    }

    public Integer getIngu() {
        return ingu;
    }

    public void setIngu(Integer ingu) {
        this.ingu = ingu;
    }

    public Integer getIngv() {
        return ingv;
    }

    public void setIngv(Integer ingv) {
        this.ingv = ingv;
    }

    public Integer getIngw() {
        return ingw;
    }

    public void setIngw(Integer ingw) {
        this.ingw = ingw;
    }

    public Integer getIngx() {
        return ingx;
    }

    public void setIngx(Integer ingx) {
        this.ingx = ingx;
    }

    public Integer getIngy() {
        return ingy;
    }

    public void setIngy(Integer ingy) {
        this.ingy = ingy;
    }

    public Integer getIngz() {
        return ingz;
    }

    public void setIngz(Integer ingz) {
        this.ingz = ingz;
    }

    public BigDecimal getOIngres() {
        return oIngres;
    }

    public void setOIngres(BigDecimal oIngres) {
        this.oIngres = oIngres;
    }

    public BigDecimal getIngreso() {
        return ingreso;
    }

    public void setIngreso(BigDecimal ingreso) {
        this.ingreso = ingreso;
    }

    public BigDecimal getMega() {
        return mega;
    }

    public void setMega(BigDecimal mega) {
        this.mega = mega;
    }

    public BigDecimal getMegb() {
        return megb;
    }

    public void setMegb(BigDecimal megb) {
        this.megb = megb;
    }

    public BigDecimal getMegc() {
        return megc;
    }

    public void setMegc(BigDecimal megc) {
        this.megc = megc;
    }

    public Integer getMegd() {
        return megd;
    }

    public void setMegd(Integer megd) {
        this.megd = megd;
    }

    public Integer getMege() {
        return mege;
    }

    public void setMege(Integer mege) {
        this.mege = mege;
    }

    public BigDecimal getMegf() {
        return megf;
    }

    public void setMegf(BigDecimal megf) {
        this.megf = megf;
    }

    public BigDecimal getMegg() {
        return megg;
    }

    public void setMegg(BigDecimal megg) {
        this.megg = megg;
    }

    public BigDecimal getMegh() {
        return megh;
    }

    public void setMegh(BigDecimal megh) {
        this.megh = megh;
    }

    public Integer getMegi() {
        return megi;
    }

    public void setMegi(Integer megi) {
        this.megi = megi;
    }

    public Integer getMegj() {
        return megj;
    }

    public void setMegj(Integer megj) {
        this.megj = megj;
    }

    public Integer getMegk() {
        return megk;
    }

    public void setMegk(Integer megk) {
        this.megk = megk;
    }

    public Integer getMegl() {
        return megl;
    }

    public void setMegl(Integer megl) {
        this.megl = megl;
    }

    public Integer getMegm() {
        return megm;
    }

    public void setMegm(Integer megm) {
        this.megm = megm;
    }

    public Integer getMegn() {
        return megn;
    }

    public void setMegn(Integer megn) {
        this.megn = megn;
    }

    public Integer getMego() {
        return mego;
    }

    public void setMego(Integer mego) {
        this.mego = mego;
    }

    public Integer getMegp() {
        return megp;
    }

    public void setMegp(Integer megp) {
        this.megp = megp;
    }

    public Integer getMegq() {
        return megq;
    }

    public void setMegq(Integer megq) {
        this.megq = megq;
    }

    public Integer getMegr() {
        return megr;
    }

    public void setMegr(Integer megr) {
        this.megr = megr;
    }

    public Integer getMegs() {
        return megs;
    }

    public void setMegs(Integer megs) {
        this.megs = megs;
    }

    public Integer getMegt() {
        return megt;
    }

    public void setMegt(Integer megt) {
        this.megt = megt;
    }

    public Integer getMegu() {
        return megu;
    }

    public void setMegu(Integer megu) {
        this.megu = megu;
    }

    public Integer getMegv() {
        return megv;
    }

    public void setMegv(Integer megv) {
        this.megv = megv;
    }

    public Integer getMegw() {
        return megw;
    }

    public void setMegw(Integer megw) {
        this.megw = megw;
    }

    public Integer getMegx() {
        return megx;
    }

    public void setMegx(Integer megx) {
        this.megx = megx;
    }

    public Integer getMegy() {
        return megy;
    }

    public void setMegy(Integer megy) {
        this.megy = megy;
    }

    public Integer getMegz() {
        return megz;
    }

    public void setMegz(Integer megz) {
        this.megz = megz;
    }

    public BigDecimal getOEgreso() {
        return oEgreso;
    }

    public void setOEgreso(BigDecimal oEgreso) {
        this.oEgreso = oEgreso;
    }

    public BigDecimal getEgreso() {
        return egreso;
    }

    public void setEgreso(BigDecimal egreso) {
        this.egreso = egreso;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPatronal() {
        return patronal;
    }

    public void setPatronal(BigDecimal patronal) {
        this.patronal = patronal;
    }

    public BigDecimal getBaseiess() {
        return baseiess;
    }

    public void setBaseiess(BigDecimal baseiess) {
        this.baseiess = baseiess;
    }

    public BigDecimal getIece() {
        return iece;
    }

    public void setIece(BigDecimal iece) {
        this.iece = iece;
    }

    public Integer getSecap() {
        return secap;
    }

    public void setSecap(Integer secap) {
        this.secap = secap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCosoc != null ? ideCosoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionDeServiciosOctubre)) {
            return false;
        }
        ComisionDeServiciosOctubre other = (ComisionDeServiciosOctubre) object;
        if ((this.ideCosoc == null && other.ideCosoc != null) || (this.ideCosoc != null && !this.ideCosoc.equals(other.ideCosoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ComisionDeServiciosOctubre[ ideCosoc=" + ideCosoc + " ]";
    }
    
}
