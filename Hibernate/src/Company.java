import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "Company_type")
public class Company {
    public Company(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int CompanyId;
    public String CompanyName;
    public String city;
    public String street;
    public String zipCode;

}
