
package SOAP.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ListaMenu", namespace = "http://SOAP/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaMenu", namespace = "http://SOAP/")
public class ListaMenu {

    @XmlElement(name = "CodUsuario", namespace = "http://SOAP/")
    private String codUsuario;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodUsuario() {
        return this.codUsuario;
    }

    /**
     * 
     * @param codUsuario
     *     the value for the codUsuario property
     */
    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

}
