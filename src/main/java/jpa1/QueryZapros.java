package jpa1;

import javax.persistence.*;
import java.util.List;

public class QueryZapros {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void metod(String[] args) {
        emf = Persistence.createEntityManagerFactory("JPATest");
        em = emf.createEntityManager();
        try {
            String[] mySelect = new String[5];
            mySelect[0] = "SELECT x FROM Flat x WHERE x.city = ?1";
            mySelect[1] = "SELECT x FROM Flat x WHERE x.district = ?1";
            mySelect[2] = "SELECT x FROM Flat x WHERE x.address = ?1";
            mySelect[3] = "SELECT x FROM Flat x WHERE x.countRooms BETWEEN ?2 AND ?3";
            mySelect[4] = "SELECT x FROM Flat x WHERE x.countRooms BETWEEN ?2 AND ?3";
            mySelect[5] = "SELECT x FROM Flat x WHERE x.countRooms BETWEEN ?2 AND ?3";

            for (String select : mySelect) {
                for (int i = 0; i < mySelect.length - 1; i++) {
                    Query query = em.createQuery(mySelect[i], Flat.class);
                }
            }
//            Query query1 = em.createQuery("SELECT x FROM Flat x WHERE EXISTS{SELECT x FROM Flat x WHERE x.district = ?1}x.city = ?1 EXISTS{" +
//                    "(SELECT x FROM Flat x WHERE x.district = ?1)", Flat.class);
//            Query query2 = em.createQuery("SELECT x FROM Flat x WHERE x.district = ?1", Flat.class);


            Query query = em.createQuery("SELECT x FROM Flat x WHERE x.city = ?1 or EXISTS" +
                    "(SELECT x FROM Flat x WHERE x.district = ?1 or exists " +
                    "(SELECT x FROM Flat x WHERE x.address = ?1))", Flat.class);


//            Query query = em.createQuery(
//                    "SELECT x FROM Flat x WHERE builder.equel() " +
//                            "x.city = ?1" +
//                            "and x.district = ?2" +
//                            "and x.address = ?3" +
//                            "and (x.countRooms BETWEEN ?4 AND ?5)" +
//                            "and (x.square BETWEEN ?6 AND ?7)" +
//                            "and (x.price  BETWEEN ?8 AND ?9)", Flat.class);
//
//            query.setParameter(1, valueTriger);
//            query.setParameter(2, min);
//            query.setParameter(3, max);
//            query.sel
            List<Flat> list = (List<Flat>) query.getResultList();

            for (Flat f : list)
                System.out.println(f);

        } catch (
                NoResultException ex) {
            System.out.println("Flat not found!");
            return;
        } catch (
                NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }
}
