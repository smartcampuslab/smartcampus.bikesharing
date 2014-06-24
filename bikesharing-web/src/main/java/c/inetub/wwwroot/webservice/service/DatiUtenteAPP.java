
package c.inetub.wwwroot.webservice.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lingua" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strCipherText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "username",
    "lingua",
    "strCipherText"
})
@XmlRootElement(name = "DatiUtenteAPP")
public class DatiUtenteAPP {

    protected String username;
    protected String lingua;
    protected String strCipherText;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the lingua property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLingua() {
        return lingua;
    }

    /**
     * Sets the value of the lingua property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLingua(String value) {
        this.lingua = value;
    }

    /**
     * Gets the value of the strCipherText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCipherText() {
        return strCipherText;
    }

    /**
     * Sets the value of the strCipherText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCipherText(String value) {
        this.strCipherText = value;
    }

}
