import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main2 {


    public static void main(String[] args) {


//        b. Stworz kilka produktow i kilka kategorii
//        c. Dodaj kilka produktów do wybranej kategorii
//        Category category = Main.createCategory();
//        Product[] products = new Product[3]; //= new Product[]();
//        for(int i = 0; i<3;i++){
//            products[i] = Main.createproduct();
//            products[i].category = category;
//            em.persist(products[i]);
//        }
//        em.persist(category);

//        wypisywanie produktow dla kategori i na odwrot
//        Product p = em.find(Product.class,120);
//        System.out.format("Produkt: %s jego kategoria %s\n",p.ProductName,p.category.name);
//
//        Category c = em.find(Category.class,121);
//        System.out.format("Lista produktow kategorii %s\n",c.name);
//        for(Product p1 : c.productList){
//            System.out.format("%s\n",p1.ProductName);
//        }

//tworzenie faktury i produktow dla niej oraz jej zapis
//        Invoice i = new Invoice();
//        Product p = new Product("fakturoprodukt12",100);
//        Product p2 = new Product("fakturoprodukt22",200);
//        p.invoices.add(i);
//        p2.invoices.add(i);
//        i.ProductsSold.add(p);
//        i.ProductsSold.add(p2);
//        em.persist(i);




        EntityManagerFactory emf = Persistence.
                createEntityManagerFactory("MFudalinskiJPA");
        EntityManager em = emf.createEntityManager();
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        Customer c = em.find(Customer.class,3);
        c.CalculateOrders();


//        //dodajemy 2/3 produkty
//        Product p1 = new Product("cola",20,300);
//        Product p2 = new Product("fanta",40,100);
//        //dpdajemy klienta
//        Customer c = new Customer("zbigniewix","zbigniejowice","zbignowiejska","33-883",0.1);
//        //dodajemy zamowienie na te produkty
//        Orders orders = new Orders();
//        orders.customer = c;
//        c.orders.add(orders);
//        //dodajemy szczegoly do kazdego produktu
//        OrderDetails od1 = new OrderDetails(); od1.product = p1; od1.quantity = 20; od1.order = orders;
//        OrderDetails od2 = new OrderDetails(); od2.product = p2; od2.quantity = 10; od2.order = orders;
//        //utrwalamy
//        em.persist(p1);
//        em.persist(p2);
//        em.persist(c);
//        em.persist(orders);
//        em.persist(od1);
//        em.persist(od2);


        etx.commit();
        em.close();



//
//        etx = em.getTransaction();
//        etx.begin();
//        Custome
//        etx.commit();
//        em.close();


    }
}
