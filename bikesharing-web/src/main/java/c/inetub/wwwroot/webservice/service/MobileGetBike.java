
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
 *         &lt;element name="strCipherText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strIdstazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strColonnina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strCipherText",
    "strUserName",
    "strIdstazione",
    "strColonnina"
})
@XmlRootElement(name = "MobileGetBike")
public class MobileGetBike {

    protected String strCipherText;
    protected String strUserName;
    protected String strIdstazione;
    protected String strColonnina;

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

    /**
     * Gets the value of the strUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrUserName() {
        return strUserName;
    }

    /**
     * Sets the value of the strUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrUserName(String value) {
        this.strUserName = value;
    }

    /**
     * Gets the value of the strIdstazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrIdstazione() {
        return strIdstazione;
    }

    /**
     * Sets the value of the strIdstazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrIdstazione(String value) {
        this.strIdstazione = value;
    }

    /**
     * Gets the value of the strColonnina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrColonnina() {
        return strColonnina;
    }

    /**
     * Sets the value of the strColonnina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrColonnina(String value) {
        this.strColonnina = value;
    }

}
