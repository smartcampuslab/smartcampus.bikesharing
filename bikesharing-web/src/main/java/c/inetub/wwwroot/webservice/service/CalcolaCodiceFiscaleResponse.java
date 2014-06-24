
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
 *         &lt;element name="CalcolaCodiceFiscaleResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "calcolaCodiceFiscaleResult"
})
@XmlRootElement(name = "CalcolaCodiceFiscaleResponse")
public class CalcolaCodiceFiscaleResponse {

    @XmlElement(name = "CalcolaCodiceFiscaleResult")
    protected String calcolaCodiceFiscaleResult;

    /**
     * Gets the value of the calcolaCodiceFiscaleResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalcolaCodiceFiscaleResult() {
        return calcolaCodiceFiscaleResult;
    }

    /**
     * Sets the value of the calcolaCodiceFiscaleResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalcolaCodiceFiscaleResult(String value) {
        this.calcolaCodiceFiscaleResult = value;
    }

}
