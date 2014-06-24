
package c.inetub.wwwroot.webservice.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTipoTessera complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTipoTessera">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoTessera" type="{c://inetub/wwwroot/webservice/Service.asmx}TipoTessera" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTipoTessera", propOrder = {
    "tipoTessera"
})
public class ArrayOfTipoTessera {

    @XmlElement(name = "TipoTessera")
    protected List<TipoTessera> tipoTessera;

    /**
     * Gets the value of the tipoTessera property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipoTessera property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipoTessera().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoTessera }
     * 
     * 
     */
    public List<TipoTessera> getTipoTessera() {
        if (tipoTessera == null) {
            tipoTessera = new ArrayList<TipoTessera>();
        }
        return this.tipoTessera;
    }

}
