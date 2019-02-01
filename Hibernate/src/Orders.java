import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {
    public Orders(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int OrderId;

    @ManyToOne
    @JoinColumn(name = "Customer_FK")
    Customer customer;

    @OneToMany(mappedBy = "order")
    List<OrderDetails> orderDetails = new ArrayList<>();

    public int calculateValue(){
        int value = 0;
        for( OrderDetails orderDetails: this.orderDetails){
            value += (orderDetails.product.price*orderDetails.quantity)*(1-customer.discount);
        }
        return value;
    }

}
