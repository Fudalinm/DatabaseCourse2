import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    //Stwórz klase produktu z polami ProductName, UnitsOnStock
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String ProductName;
    public Integer UnitsOnStock;

    @ManyToOne
    @JoinColumn(name = "Supplier_Fk")
    public Supplier isSuppliedBy;

    @ManyToOne
    @JoinColumn(name="Category_FK")
    public Category category;

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Invoice> invoices = new HashSet<>();

    int price;

    public Product(){}
    public Product(String name, Integer i,int price){
        this.ProductName = name;
        this.UnitsOnStock = i;
        this.price = price;
    }
}