import javax.persistence.*;

@Entity
public class OrderDetails {
    public OrderDetails(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int OrderDetailsId;

    @ManyToOne
    @JoinColumn(name = "Order_FK")
    Orders order;

    @ManyToOne
    @JoinColumn(name = "Product_FK")
    Product product;

    int quantity;

}
