package de.openknowledge.playground.api.rest.supportCode.dataBase;

import com.github.database.rider.core.util.EntityManagerProvider;
import de.openknowledge.playground.api.rest.supportCode.domain.AccountEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.FollowerEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.TweetEntity;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DBConnection {
    private final Properties dbConfig;
    private IDatabaseConnection connection;
    private static EntityManagerFactory emFactory;
    private EntityManager entityManager;

    public DBConnection() {
        emFactory = Persistence.createEntityManagerFactory("test-local");
        entityManager = emFactory.createEntityManager();

        dbConfig = new Properties();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, retrieveDbUnitDataTypeFactoryName());
        dbConfig.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, "false");

        try {
            this.connection = getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String retrieveDbUnitDataTypeFactoryName() {
        Map<String, Object> entityManagerProperties = emFactory.getProperties();
        String databaseDriverClazz = (String)entityManagerProperties.get("javax.persistence.jdbc.driver");
        return DbUnitDatatypeFactory.getDatatypeFactory(databaseDriverClazz);
    }

    private IDatabaseConnection getConnection() throws DatabaseUnitException {
        connection = new DatabaseConnection(EntityManagerProvider.instance("test-local").connection());
        connection.getConfig().setPropertiesByString(dbConfig);

        return connection;
    }

    public IDataSet getActualDataSet() {
        IDataSet actualDataSet;
        String[] tableNames = new String []{"TAB_TWEET", "TAB_ACCOUNT"};
        try {
            actualDataSet = connection.createDataSet(tableNames);
        } catch (SQLException | DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return actualDataSet;
    }


    public void insertTweets(List<TweetEntity> tweets) {
        try {
            DatabaseOperation.INSERT.execute(this.connection, CustomizedDataSetBuilder.tweetDataSet(tweets));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void insertLikes (List<LikeEntity> likes) {
        try {
            DatabaseOperation.INSERT.execute(this.connection, CustomizedDataSetBuilder.likeDataSet(likes));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void insertFollower (List<FollowerEntity> follower) {
        try {
            DatabaseOperation.INSERT.execute(this.connection, CustomizedDataSetBuilder.followerDataSet(follower));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void updateAccounts (List<AccountEntity> accounts) throws DatabaseUnitException, SQLException {
        DatabaseOperation.CLEAN_INSERT.execute(this.connection, CustomizedDataSetBuilder.accountDataSet(accounts));
    }

    public void deleteAccountIfPresent(int accountId) {
        IDataSet dataSet = getActualDataSet();
        try {
            ITable table = dataSet.getTable("TAB_ACCOUNT");

            for (int i=0; i<table.getRowCount(); i++) {
                Integer id = (Integer) table.getValue(i, "ACCOUNT_ID");
                if ( id.equals(accountId)) {
                    try {
                        DatabaseOperation.CLEAN_INSERT.execute(this.connection, CustomizedDataSetBuilder.deleteAccountFromDataSet(accountId, getActualDataSet()));
                    } catch (DatabaseUnitException | SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }
            }
        } catch (DataSetException e) {
            e.printStackTrace();
        }
    }

    public void initTables() {
        this.clearTables();
    }

    public void clearTables () {
        //todo: Löscht nicht die Daten aus TAB_ACCOUNT --> Namen ändern?
        try {
            DatabaseOperation.DELETE_ALL.execute(this.connection, CustomizedDataSetBuilder.emptyTableDataSet());
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }









    public void setUpData (IDataSet dataSet) {
        try {
            DatabaseOperation.CLEAN_INSERT.execute(this.connection, dataSet);
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDataBase (IDataSet dataSet) {
        try {
            DatabaseOperation.DELETE_ALL.execute(this.connection, dataSet);
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
