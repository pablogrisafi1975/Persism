package net.sf.persism;

import net.sf.persism.categories.ExternalDB;
import net.sf.persism.dao.Customer;
import net.sf.persism.dao.DAOFactory;
import net.sf.persism.dao.Order;
import org.junit.experimental.categories.Category;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Category(ExternalDB.class)
public class TestFirebird extends BaseTest {

    private static final Log log = Log.getLogger(TestFirebird.class);

    @Override
    public void setUp() throws Exception {
        connectionType = ConnectionTypes.Firebird;
        super.setUp();

        if (getClass().equals(TestFirebird.class)) {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/firebird.properties"));
            String driver = props.getProperty("database.driver");
            String url = props.getProperty("database.url");
            String username = props.getProperty("database.username");
            String password = props.getProperty("database.password");
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);

            log.info(con.getMetaData().getDatabaseProductName());

            createTables();

            session = new Session(con);
        }
    }


    @Override
    public void testContactTable() throws SQLException {
        super.testContactTable();
        assertTrue(true);
    }

    @Override
    protected void createTables() throws SQLException {
        List<String> commands = new ArrayList<>(12);
        String sql;


        if (UtilsForTests.isTableInDatabase("ORDERS", con)) {
            commands.add("DROP TABLE ORDERS;");
        }


        sql = "CREATE TABLE ORDERS ( " +
                "id integer generated by default as identity primary key, " +
                " NAME VARCHAR(30), " +
                " PAID BOOLEAN, " +
                " Prepaid BOOLEAN," +
                " IsCollect BOOLEAN," +
                " IsCancelled BOOLEAN," +
                " CUSTOMER_ID VARCHAR(10), " +
                " CREATED TIMESTAMP DEFAULT 'NOW', " +
                " DATE_PAID TIMESTAMP, " +
                " DATE_SOMETHING TIMESTAMP " +
                "); ";

        commands.add(sql);

        if (UtilsForTests.isTableInDatabase("CUSTOMERS", con)) {
            commands.add("DROP TABLE CUSTOMERS;");
            commands.add("DROP GENERATOR GEN_CUSTOMER_ID;");
        }

        sql = "CREATE GENERATOR GEN_CUSTOMER_ID; ";
        commands.add(sql);
//        executeCommand(sql, con);


        sql = "CREATE TABLE CUSTOMERS ( " +
                "  CUSTOMER_ID VARCHAR(10) NOT NULL, " +
                "  COMPANY_NAME VARCHAR(30) NOT NULL, " +
                "  CONTACT_NAME VARCHAR(30), " +
                "  CONTACT_TITLE VARCHAR(10), " +
                "  ADDRESS VARCHAR(40), " +
                "  CITY VARCHAR(30), " +
                "  REGION VARCHAR(10), " +
                "  POSTAL_CODE VARCHAR(20), " +
                "  COUNTRY VARCHAR(2) DEFAUlT 'US', " +
                "  PHONE VARCHAR(30), " +
                "  FAX VARCHAR(30), " +
                "  STATUS CHAR(1), " +
                "  DATE_REGISTERED TIMESTAMP, " +
                "  DATE_OF_LAST_ORDER DATE, " +
                " TestLocalDate DATE, " +
                " TestLocalDateTime TIMESTAMP, " +
                "  CONSTRAINT PK_CUSTOMER PRIMARY KEY (CUSTOMER_ID) " +
                "); ";
        commands.add(sql);
        executeCommands(commands, con);

        if (UtilsForTests.isTableInDatabase("Invoices", con)) {
            executeCommand("DROP TABLE Invoices", con);
        }

        executeCommand("CREATE TABLE Invoices ( " +
                " Invoice_ID integer generated by default as identity primary key, " +
                " Customer_ID varchar(10) NOT NULL, " +
                " Paid BOOLEAN NOT NULL, " +
                " Price NUMERIC(7,3) NOT NULL, " +
                " ACTUALPRICE NUMERIC(7,3) NOT NULL, " +
                " Status INT DEFAULT 1, " +
                " Created TIMESTAMP default 'NOW', " + // make read-only in Invoice Object
                " Quantity NUMERIC(10) NOT NULL, " +
                //" Total NUMERIC(10,3) NOT NULL, " +
                " Discount NUMERIC(10,3) NOT NULL " +
                ") ", con);



        if (UtilsForTests.isTableInDatabase("Contacts", con)) {
            executeCommand("DROP TABLE Contacts", con);
        }
        // FIREBIRD and Derby don't like NULL
        sql = "CREATE TABLE Contacts( " +
                "   identity varchar(40) NOT NULL PRIMARY KEY, \n" +  // test binary(16)
                "   PartnerID varchar(36) NOT NULL, \n" + // test varchar(36)
                "   Type char(2) NOT NULL, \n" +
                "   Firstname varchar(50) NOT NULL, \n" +
                "   Lastname varchar(50) NOT NULL, \n" +
                "   ContactName varchar(50) NOT NULL, \n" +
                "   Company varchar(50) NOT NULL, \n" +
                "   Division varchar(50), \n" +
                "   Email varchar(50), \n" +
                "   Address1 varchar(50), \n" +
                "   Address2 varchar(50), \n" +
                "   City varchar(50), \n" +
                "   StateProvince varchar(50), \n" +
                "   ZipPostalCode varchar(10), \n" +
                "   Country varchar(50), \n" +
                "   Status SMALLINT, \n" +
                "   DateAdded Date, \n" +
                "   LastModified TIMESTAMP, \n" +
                "   Notes BLOB SUB_TYPE TEXT, \n" +
                "   AmountOwed REAL, \n" +
                "   \"BigInt\" DECIMAL(18), \n" +
                "   SomeDate TIMESTAMP, \n" +
                "   TestInstant TIMESTAMP, \n" +
                "   TestInstant2 TIMESTAMP, \n" +
                "   WhatMiteIsIt TIME, \n  " +
                "   WhatTimeIsIt TIME ) ";

        executeCommand(sql, con);


        // TIMESTAMP for DATETIME in Firebird

        if (UtilsForTests.isTableInDatabase("DateTestLocalTypes", con)) {
            executeCommand("DROP TABLE DateTestLocalTypes", con);
        }

        sql = "CREATE TABLE DateTestLocalTypes ( " +
                " ID INT, " +
                " Description VARCHAR(100), " +
                " DateOnly DATE, " +
                " TimeOnly TIME," +
                " DateAndTime TIMESTAMP) ";

        executeCommand(sql, con);

        if (UtilsForTests.isTableInDatabase("DateTestSQLTypes", con)) {
            executeCommand("DROP TABLE DateTestSQLTypes", con);
        }

        sql = "CREATE TABLE DateTestSQLTypes ( " +
                " ID INT, " +
                " Description VARCHAR(100), " +
                " DateOnly DATE, " +
                " TimeOnly TIME," +
                " UtilDateAndTime TIMESTAMP," +
                " DateAndTime TIMESTAMP) ";

        executeCommand(sql, con);
    }

    public void testSomething() {
        // what else?
        Collection<PropertyInfo> x = MetaData.getPropertyInfo(Customer.class);
        for (PropertyInfo pi : x) {
            log.warn(pi); // exercise toString for no good reason! Well, for code coverage...
        }
    }

    public void testOrders() throws SQLException {
        Order order = DAOFactory.newOrder(con);

        order.setPaid(true);
        order.setDatePaid(LocalDateTime.now());
        order.setCreated(LocalDate.now());
        order.setCustomerId("SOMEONE");

        session.insert(order);

    }

    @Override
    public void testAllDates() {
        super.testAllDates();
    }
}
