
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
 *         &lt;element name="RicercaUtenteResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ReturnMSG"/>
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
    "ricercaUtenteResult"
})
@XmlRootElement(name = "RicercaUtenteResponse")
public class RicercaUtenteResponse {

    @XmlElement(name = "RicercaUtenteResult", required = true)
    protected ReturnMSG ricercaUtenteResult;

    /**
     * Gets the value of the ricercaUtenteResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnMSG }
     *     
     */
    public ReturnMSG getRicercaUtenteResult() {
        return ricercaUtenteResult;
    }

    /**
     * Sets the value of the ricercaUtenteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnMSG }
     *     
     */
    public void setRicercaUtenteResult(ReturnMSG value) {
        this.ricercaUtenteResult = value;
    }

}
