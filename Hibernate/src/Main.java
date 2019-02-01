import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import javax.persistence.metamodel.EntityType;
import java.util.Scanner;


public class Main {

    ///
    public static Supplier createSup(){
        Scanner input = new Scanner(System.in);
        String companyName = input.nextLine();
        String Street = input.nextLine();
        String city = input.nextLine();
        return new Supplier(companyName,Street,city);
    }
    public static Product createproduct(){
        Scanner input = new Scanner(System.in);
        String prodName = input.nextLine();
        String units = input.nextLine();
        Integer iunits = Integer.valueOf(units);
        return new Product(prodName,iunits,20);
    }
    public static Category createCategory(){
        Scanner input = new Scanner(System.in);
        String categoryName = input.nextLine();
        return new Category(categoryName);
    }
///
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);

        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }

        Session s = getSession();
        Transaction t = s.beginTransaction();

        t.commit();
        s.close();

//        Category[] categories = new Category[3];
//        for(int i = 0; i<3;i++){
//            //dodajemy produkty do dostawcy
//            categories[i] = createCategory();
//            s.save(categories[i]);
//        }
//        //tworzenie kilku produktow
//        //c. Dodaj kilka produktów do wybranej kategorii
//
//        Product[] products = new Product[3]; //= new Product[]();
//        for(int i = 0; i<3;i++){
//            //dodajemy produkty do dostawcy
//            products[i] = createproduct();
//            products[i].category = categories[1];
//            s.save(products[i]);
//        }

//        Category c = s.get(Category.class,10);
//        System.out.format("Produkty z kategorii %s\n",c.name);
//
//        for(Product p : c.productList){
//            System.out.format("%s\n",p.ProductName);
//        }
//        Product p = s.get(Product.class,12);
//        System.out.format("\nProdukty %s nalezy do kategorii %s\n",p.ProductName,p.category.name);


//
//        Invoice invoice = new Invoice();
//        //s.save(invoice);
//        Product[] products = new Product[3];
//        for(int i = 0; i<3;i++){
//            //dodajemy produkty do dostawcy
//            products[i] = createproduct();
//            invoice.ProductsSold.add(products[i]);
//            products[i].invoices.add(invoice);
//            //dodanie do faktury
//            s.save(products[i]);
//        }
//        s.save(invoice);

//        Invoice invoice = s.get(Invoice.class,30);
//        System.out.format("wypisuje produkty z faktury %d:\n",invoice.InvoiceNumber);
//        for(Product x: invoice.ProductsSold){
//            System.out.format("%s\n",x.ProductName);
//        }
//
//        Product product = s.get(Product.class,28);
//        System.out.format("Wypisuje faktury powiazane z produktem %s:\n",product.ProductName);
//        for(Invoice x: product.invoices){
//            System.out.format("%s\n",x.InvoiceNumber);
//        }
//
//        product = s.get(Product.class,29);
//        System.out.format("Wypisuje faktury powiazane z produktem %s:\n",product.ProductName);
//        for(Invoice x: product.invoices){
//            System.out.format("%s\n",x.InvoiceNumber);
//        }


    }
}