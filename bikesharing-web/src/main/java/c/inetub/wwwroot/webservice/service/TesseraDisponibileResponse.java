
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
 *         &lt;element name="TesseraDisponibileResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "tesseraDisponibileResult"
})
@XmlRootElement(name = "TesseraDisponibileResponse")
public class TesseraDisponibileResponse {

    @XmlElement(name = "TesseraDisponibileResult")
    protected boolean tesseraDisponibileResult;

    /**
     * Gets the value of the tesseraDisponibileResult property.
     * 
     */
    public boolean isTesseraDisponibileResult() {
        return tesseraDisponibileResult;
    }

    /**
     * Sets the value of the tesseraDisponibileResult property.
     * 
     */
    public void setTesseraDisponibileResult(boolean value) {
        this.tesseraDisponibileResult = value;
    }

}
