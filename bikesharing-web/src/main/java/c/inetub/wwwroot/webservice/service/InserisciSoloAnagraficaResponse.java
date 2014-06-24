
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
 *         &lt;element name="InserisciSoloAnagraficaResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ReturnMSG"/>
 *         &lt;element name="ana" type="{c://inetub/wwwroot/webservice/Service.asmx}AnagraficaUtente" minOccurs="0"/>
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
    "inserisciSoloAnagraficaResult",
    "ana"
})
@XmlRootElement(name = "InserisciSoloAnagraficaResponse")
public class InserisciSoloAnagraficaResponse {

    @XmlElement(name = "InserisciSoloAnagraficaResult", required = true)
    protected ReturnMSG inserisciSoloAnagraficaResult;
    protected AnagraficaUtente ana;

    /**
     * Gets the value of the inserisciSoloAnagraficaResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnMSG }
     *     
     */
    public ReturnMSG getInserisciSoloAnagraficaResult() {
        return inserisciSoloAnagraficaResult;
    }

    /**
     * Sets the value of the inserisciSoloAnagraficaResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnMSG }
     *     
     */
    public void setInserisciSoloAnagraficaResult(ReturnMSG value) {
        this.inserisciSoloAnagraficaResult = value;
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

}
