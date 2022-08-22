package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Transaction transaction = null;
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = new StringBuilder().append("create table if not exists ")
                .append("pre_proj.users")
                .append("(")
                .append("id").append(" ").append("bigint not null primary key auto_increment,")
                .append("name").append(" ").append("varchar(255) not null,")
                .append("lastName").append(" ").append("varchar(255) not null,")
                .append("age").append(" ").append("bigint not null")
                .append(");")
                .toString();
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public void dropUsersTable() {
        String sql = "drop table if exists pre_proj.users cascade;";
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()){
            User user = new User(name, lastName, age);
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            users = session.createCriteria(User.class).list();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "delete from pre_proj.users";
        {
            try (Session session = Util.getSessionFactory().openSession()){
                transaction = session.beginTransaction();
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
            }
            catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }

    }
}
