package hawk.service;

import hawk.entity.Item;
import hawk.entity.User;
import hawk.form.Search;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    EntityManager entityManager;

    public List<Item> search(Search search) {
        final Session session = entityManager.unwrap(Session.class);
        List users = session.doReturningWork(connection -> {
            List<User> users1 = new ArrayList<>();
            // The wrong way
            String query = "select id, name, description from USER where name like '%" +
                    search.getSearchText() + "%'";

            LOGGER.log(Level.INFO, "SQL Query " + query);
            ResultSet rs = connection
                    .createStatement()
                    .executeQuery(query);

            /* The righter way, should probably use built in Data Model for this, but this is safe
            String query = "select id, name, description from ITEM where description like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + search.getSearchText() + "%");
            LOGGER.log(Level.INFO, "SQL Query " + statement);
            ResultSet rs = statement.executeQuery();
            */

            while (rs.next()) {
                users1.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
            }
            return users1;
        });
        return users;
    }


}
