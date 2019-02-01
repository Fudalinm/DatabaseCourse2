
//no longer used
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    public Address(){}
    public Address(String city,String street,String zipCode){
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
    public String city;
    public String street;
    public String zipCode;


}
