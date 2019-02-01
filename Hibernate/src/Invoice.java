import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Invoice {
    public Invoice(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int InvoiceNumber;

    @ManyToMany(mappedBy = "invoices", cascade = CascadeType.PERSIST)
    public Set<Product> ProductsSold = new HashSet<>();

}
