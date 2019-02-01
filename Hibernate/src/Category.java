import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Category {
    public Category(){}

    public Category(String n){
        this.name = n;
        this.productList = new LinkedList<>();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int CategoryId;

    public String name;

    @OneToMany(mappedBy = "category")
    public List<Product> productList;

}
