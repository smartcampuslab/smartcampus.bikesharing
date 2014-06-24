
package c.inetub.wwwroot.webservice.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTipoAbbonamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTipoAbbonamento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoAbbonamento" type="{c://inetub/wwwroot/webservice/Service.asmx}TipoAbbonamento" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTipoAbbonamento", propOrder = {
    "tipoAbbonamento"
})
public class ArrayOfTipoAbbonamento {

    @XmlElement(name = "TipoAbbonamento")
    protected List<TipoAbbonamento> tipoAbbonamento;

    /**
     * Gets the value of the tipoAbbonamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipoAbbonamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipoAbbonamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoAbbonamento }
     * 
     * 
     */
    public List<TipoAbbonamento> getTipoAbbonamento() {
        if (tipoAbbonamento == null) {
            tipoAbbonamento = new ArrayList<TipoAbbonamento>();
        }
        return this.tipoAbbonamento;
    }

}
