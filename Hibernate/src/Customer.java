import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
//@DiscriminatorValue(value = "Customer")
public class Customer extends Company {
    public Customer(){super();}
    public Customer(String name,String city,String street,String zipCode,double discount){
        this.CompanyName = name;
        this.city = city;
        this.street = street;
        this.discount = discount;
        this.zipCode = zipCode;
    }
    public double discount;//between 0 and 1

    @OneToMany(mappedBy = "customer")
    List<Orders> orders = new ArrayList<>();

    public void CalculateOrders(){
        for( Orders o: this.orders){
            System.out.format("%d\n",o.calculateValue());
        }
    }
}
