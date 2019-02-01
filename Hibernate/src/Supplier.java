import javax.persistence.*;
import java.util.Set;

@Entity
//@SecondaryTable(name = "Address_Table")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String CompanyName;

    //@Column(table = "Address_Table")
    public String city;
    //@Column(table = "Address_Table")
    public String street;
    //@Column(table = "Address_Table")
    public String zipCode;

    @OneToMany(mappedBy = "isSuppliedBy")
    public Set<Product> products;

    public Supplier(){}
    public Supplier(String companyName,String street,String city){
        this.CompanyName = companyName;
        this.city = city;
        this.street = street;
        this.zipCode = "00-000";
    }
    public Supplier(String companyName,String street,String city,String zipCode){
        this.CompanyName = companyName;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}