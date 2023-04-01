package app;

import java.util.List;

public interface UserDAO {
    Integer save(User user);
    List<User> readAll();
    User readById(Integer id);
    List<User> readAllByRole(Role role);
    boolean update(User user);
    boolean delete(User user);
}
