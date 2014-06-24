
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
 *         &lt;element name="ana" type="{c://inetub/wwwroot/webservice/Service.asmx}AnagraficaUtente" minOccurs="0"/>
 *         &lt;element name="IDModalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "ana",
    "idModalitaPagamento"
})
@XmlRootElement(name = "Inserisci")
public class Inserisci {

    @XmlElement(name = "UsernameRivenditore")
    protected String usernameRivenditore;
    @XmlElement(name = "PasswordRivenditore")
    protected String passwordRivenditore;
    protected AnagraficaUtente ana;
    @XmlElement(name = "IDModalitaPagamento")
    protected int idModalitaPagamento;

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
     * Gets the value of the ana property.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaUtente }
     *     
     */
    public AnagraficaUtente getAna() {
        return ana;
    }

    /**
     * Sets the value of the ana property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaUtente }
     *     
     */
    public void setAna(AnagraficaUtente value) {
        this.ana = value;
    }

    /**
     * Gets the value of the idModalitaPagamento property.
     * 
     */
    public int getIDModalitaPagamento() {
        return idModalitaPagamento;
    }

    /**
     * Sets the value of the idModalitaPagamento property.
     * 
     */
    public void setIDModalitaPagamento(int value) {
        this.idModalitaPagamento = value;
    }

}
