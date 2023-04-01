package app;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class UserDAOImpl implements UserDAO{
    @Override
    public Integer save(User user) {
        Transaction transaction = null;
        Integer userId = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userId = (Integer) session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Math.toIntExact(userId);
    }

    @Override
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            for (User user : query.list()) {
                users.add(new User(user.getId(), user.getName(), user.getLogin(), user.getPassword()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User readById(Integer id) {
        User user = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Query<User> query = session.createQuery("from User u join fetch u.roles where u.id=:id", User.class);
            query.setParameter("id", id);
            user = query.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> readAllByRole(Role role) {
        List<User> users = new ArrayList<>();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User u join fetch u.roles r where r = :role", User.class);
            query.setParameter("role", role);
            users = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean update(User user) {
        Transaction transaction = null;
        boolean success = false;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where id = :id", User.class);
            query.setParameter("id", user.getId());
            if (!query.list().isEmpty()) {
                User tempUser = query.list().get(0);
                tempUser.setName(user.getName());
                tempUser.setLogin(user.getLogin());
                tempUser.setPassword(user.getPassword());
                tempUser.setRoles(user.getRoles());
                transaction = session.beginTransaction();
                session.update(tempUser);
                transaction.commit();
                success = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean delete(User user) {
        Transaction transaction = null;
        boolean success = false;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where id = :id", User.class);
            query.setParameter("id", user.getId());
            if (!query.list().isEmpty()) {
                transaction = session.beginTransaction();
                session.delete(query.list().get(0));
                transaction.commit();
                success = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return success;
    }
}
