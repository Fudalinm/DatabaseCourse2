import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue(value = "newSupplier")
public class newSupplier extends Company {
    public newSupplier(){super();}
    public String bankAccountNumber;
}
