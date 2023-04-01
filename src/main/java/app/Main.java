package app;

import java.util.List;

public class Main {
    private static final UserDAO USER_DAO = new UserDAOImpl();

    public static void main(String[] args) {
        USER_DAO.save(new User(1, "Сережа", "Sergey30", "1234", List.of(Role.ADMIN)));
        USER_DAO.save(new User(2,"Жук", "Zhyk", "123", List.of(Role.GUEST)));
        USER_DAO.save(new User(3,"Макарон", "MAKAROSHKA", "12", List.of(Role.USER)));

        List<User> users = USER_DAO.readAll();
        if (!users.isEmpty()) {
            users.forEach(System.out::println);
        }

        User user = USER_DAO.readById(1);
        if (user != null) {
            System.out.println(user);
        }

        List<User> allUsers = USER_DAO.readAllByRole(Role.GUEST);
        if (!allUsers.isEmpty()) {
            allUsers.forEach(System.out::println);
        }

        User jake = USER_DAO.readById(2);
        jake.setRoles(List.of(Role.ADMIN));
        if (USER_DAO.update(jake)) {
            System.out.println("Успешно");
        } else {
            System.out.println("Не получилось");
        }

        if (USER_DAO.delete(USER_DAO.readById(2))) {
            System.out.println("Успешно");
        } else {
            System.out.println("Не получилось");
        }
    }
}
