
package c.inetub.wwwroot.webservice.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UsernameRivenditore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PasswordRivenditore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodiceComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodiceStazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodiceColonnina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodicePortale" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "usernameRivenditore",
    "passwordRivenditore",
    "codiceComune",
    "codiceStazione",
    "codiceColonnina",
    "codicePortale"
})
@XmlRootElement(name = "GetColumnConfiguration")
public class GetColumnConfiguration {

    @XmlElement(name = "UsernameRivenditore")
    protected String usernameRivenditore;
    @XmlElement(name = "PasswordRivenditore")
    protected String passwordRivenditore;
    @XmlElement(name = "CodiceComune")
    protected String codiceComune;
    @XmlElement(name = "CodiceStazione")
    protected String codiceStazione;
    @XmlElement(name = "CodiceColonnina")
    protected String codiceColonnina;
    @XmlElement(name = "CodicePortale")
    protected int codicePortale;

    /**
     * Gets the value of the usernameRivenditore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsernameRivenditore() {
        return usernameRivenditore;
    }

    /**
     * Sets the value of the usernameRivenditore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsernameRivenditore(String value) {
        this.usernameRivenditore = value;
    }

    /**
     * Gets the value of the passwordRivenditore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordRivenditore() {
        return passwordRivenditore;
    }

    /**
     * Sets the value of the passwordRivenditore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordRivenditore(String value) {
        this.passwordRivenditore = value;
    }

    /**
     * Gets the value of the codiceComune property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceComune() {
        return codiceComune;
    }

    /**
     * Sets the value of the codiceComune property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceComune(String value) {
        this.codiceComune = value;
    }

    /**
     * Gets the value of the codiceStazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceStazione() {
        return codiceStazione;
    }

    /**
     * Sets the value of the codiceStazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceStazione(String value) {
        this.codiceStazione = value;
    }

    /**
     * Gets the value of the codiceColonnina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceColonnina() {
        return codiceColonnina;
    }

    /**
     * Sets the value of the codiceColonnina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceColonnina(String value) {
        this.codiceColonnina = value;
    }

    /**
     * Gets the value of the codicePortale property.
     * 
     */
    public int getCodicePortale() {
        return codicePortale;
    }

    /**
     * Sets the value of the codicePortale property.
     * 
     */
    public void setCodicePortale(int value) {
        this.codicePortale = value;
    }

}
