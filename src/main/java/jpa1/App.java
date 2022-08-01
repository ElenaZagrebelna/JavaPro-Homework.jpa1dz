package jpa1;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add flats");
                    System.out.println("2: add random flats");
                    System.out.println("3: delete flat");
                    System.out.println("4: sort flat");
                    System.out.println("5: view flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlats(sc);
                            break;
                        case "2":
                            insertRandomFlats(sc);
                            break;
                        case "3":
                            deleteFlat(sc);
                            break;
                        case "4":
                            sortFlat(sc);
                            break;
                        case "5":
                            viewFlats();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addFlats(Scanner sc) {
        System.out.print("Enter city: ");
        String city = sc.nextLine();
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter count rooms: ");
        String scountRooms = sc.nextLine();
        int countRooms = Integer.parseInt(scountRooms);
        System.out.print("Enter square flat: ");
        String ssquareFlat = sc.nextLine();
        int squareFlat = Integer.parseInt(ssquareFlat);
        System.out.print("Enter price: ");
        String sprice = sc.nextLine();
        int price = Integer.parseInt(sprice);

        em.getTransaction().begin();
        try {
            Flat f = new Flat(city, district, address, countRooms, squareFlat, price);
            em.persist(f);
            em.getTransaction().commit();

            System.out.println(f.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteFlat(Scanner sc) {
        System.out.print("Enter flat id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Flat f = em.getReference(Flat.class, id);
        if (f == null) {
            System.out.println("Flat not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(f);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void sortFlat(Scanner sc) {
        String fieldName = "";
        Map<Integer, String> map = new HashMap<Integer, String>();

        System.out.print("Enter parameter sort(" +
                "1-city, 2-district, 3-address," +
                " 4-countRooms, 5-squareFlat, 6-price): ");
        String striger = sc.nextLine();
        int triger = Integer.parseInt(striger);

        Field[] flat = Flat.class.getDeclaredFields();
        for (int i = 0; i < flat.length; i++) {
            map.put(i, flat[i].getName());
        }
        fieldName = map.get(triger);

        if (triger <= 3) {
            sortFlatByString(sc, fieldName);
        } else if (triger > 3 && triger <= 6) {
            sortFlatByInt(sc, fieldName);
        } else {
            System.out.println("Triger not found!");
        }
    }

    private static void sortFlatByString(Scanner sc, String fieldName) {
        System.out.print("Enter value: ");
        String valueTriger = sc.nextLine();
        try {
            String querys = "SELECT x FROM Flat x WHERE x." + fieldName + "= ?1";
            Query query = em.createQuery(querys, Flat.class);

            query.setParameter(1, valueTriger);
            List<Flat> list = (List<Flat>) query.getResultList();

            for (Flat f : list)
                System.out.println(f);

        } catch (NoResultException ex) {
            System.out.println("Flat not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }

    private static void sortFlatByInt(Scanner sc, String fieldName) {
        System.out.print("Enter min value: ");
        int min = sc.nextInt();
        System.out.print("Enter max value: ");
        int max = sc.nextInt();
        try {
            String querys = "SELECT x FROM Flat x WHERE x." + fieldName + " BETWEEN ?1 AND ?2";
            Query query = em.createQuery(querys, Flat.class);

            query.setParameter(1, min);
            query.setParameter(2, max);
            List<Flat> list = (List<Flat>) query.getResultList();

            for (Flat f : list)
                System.out.println(f);

        } catch (NoResultException ex) {
            System.out.println("Flat not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }

    private static void insertRandomFlats(Scanner sc) {
        System.out.print("Enter clients count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Flat f = new Flat(randomCity(), randomDistrict(), randomADRESS(), RND.nextInt(6), RND.nextInt(300), RND.nextInt(100));
                em.persist(f);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void viewFlats() {
        Query query = em.createQuery(
                "SELECT f FROM Flat f", Flat.class);
        List<Flat> list = (List<Flat>) query.getResultList();

        for (Flat f : list)
            System.out.println(f);
    }

    static final String[] CITY = {"Poltava", "Kyiv", "Lviv", "Harkiv", "Dnipro"};
    static final String[] DISTRICT = {"Poltavsky", "Kievsky", "Lvivsky", "Harkivsky", "Dniprovsky"};
    static final String[] ADRESS = {"Poltava1", "Kyiv1", "Lviv1", "Harkiv1", "Dnipro1"};
    static final Random RND = new Random();

    static String randomCity() {
        return CITY[RND.nextInt(CITY.length)];
    }

    static String randomDistrict() {
        return DISTRICT[RND.nextInt(DISTRICT.length)];
    }

    static String randomADRESS() {
        return ADRESS[RND.nextInt(ADRESS.length)];
    }

}


