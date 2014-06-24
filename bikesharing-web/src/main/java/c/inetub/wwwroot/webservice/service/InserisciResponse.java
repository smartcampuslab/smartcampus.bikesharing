
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
 *         &lt;element name="InserisciResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ReturnMSG"/>
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
    "inserisciResult",
    "ana",
    "idModalitaPagamento"
})
@XmlRootElement(name = "InserisciResponse")
public class InserisciResponse {

    @XmlElement(name = "InserisciResult", required = true)
    protected ReturnMSG inserisciResult;
    protected AnagraficaUtente ana;
    @XmlElement(name = "IDModalitaPagamento")
    protected int idModalitaPagamento;

    /**
     * Gets the value of the inserisciResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnMSG }
     *     
     */
    public ReturnMSG getInserisciResult() {
        return inserisciResult;
    }

    /**
     * Sets the value of the inserisciResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnMSG }
     *     
     */
    public void setInserisciResult(ReturnMSG value) {
        this.inserisciResult = value;
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
