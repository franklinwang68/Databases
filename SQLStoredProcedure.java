//Java code for Franklin's 2 use cases
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class SQLStoredProcedure {

// Connect to your database.
static String connectionUrl;
static Scanner myObj = new Scanner(System.in);

    public static void main(String[] args) {
        // Replace server name, username, and password with your credentials
        connectionUrl =
            "jdbc:sqlserver://cxp-sql-02\\fmw13;"
            + "database=salesOrders;"
            + "user=dbuser;"
            + "password=csds341143sdsc;"
            // + "password=scsd341143dscs;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=15;";

        int inputnum = -1;    
            
        System.out.println("Welcome to the Sales Order Database");    
            
        while (inputnum != 0) {
            System.out.println("Enter the number corresponding to the program you want to run");      
            System.out.println("0:  Quit the program");     
            System.out.println("1:  Insert New Residence");      
            System.out.println("2:  Insert New Business");      
            System.out.println("3:  Find Local Supplier");
            System.out.println("4:  Update Customer Residence");
            System.out.println("5:  Make an Order");
            System.out.println("6:  Cancel an Order");
            System.out.println("7:  Find the Total Sales Made to a Customer by a Business");
            System.out.println();      
            
            inputnum = myObj.nextInt();      
                
            switch (inputnum) {
                case 0: myObj.close();
                        break;        
                case 1: newRes();                
                        break;        
                case 2: newBus();
                        break;              
                case 3: findLocalSupplier();                
                        break;   
                case 4: updateCustomerResidence();
                        break;
                case 5: insertOrder();
                        break;   
                case 6: cancelOrder();
                        break;   
                case 7: findTotalSales();
                        break;   
            
                default: System.out.println("Illegal value entered");
            }//switch    
        }//while
    

    }//main

    public static void newRes(){
        //new residence
        int streetNum; // input parameter
        String street; // input parameter, max length 20
        String city; // input parameter, max length 20
        String state; // input parameter, max length 20
        String zip; // input parameter, max length 5
        String phone; // input parameter, max length 20

        //inputs
        System.out.println("Enter street number");
        streetNum = myObj.nextInt();
        System.out.println("Enter street");
        street = myObj.nextLine(); //Something goes wrong here, comment this line 
        street = myObj.nextLine();
        System.out.println("Enter city");
        city = myObj.nextLine();
        System.out.println("Enter state");
        state = myObj.nextLine();
        System.out.println("Enter zip");
        zip = myObj.nextLine();
        System.out.println("Enter phone");
        phone = myObj.nextLine();

        System.out.println(
            "Street Number: " + streetNum +
            ", Street: " + street +
            ", City: " + city +
            ", State: " + state +
            ", ZIP: " + zip +
            ", Phone: " + phone
        );

        String callNewRes = "{call dbuser.insertNewResidence(?,?,?,?,?,?,?)}";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
            CallableStatement prepsStoredProc = connection.prepareCall(callNewRes);)
        {
            connection.setAutoCommit(false);
            // Index for paramters start at 1.
            prepsStoredProc.setInt(1, streetNum);
            prepsStoredProc.setString(2, street);
            prepsStoredProc.setString(3, city);
            prepsStoredProc.setString(4, state);
            prepsStoredProc.setString(5, zip);
            prepsStoredProc.setString(6, phone);
            //the 7th parameter is an output parameter
            prepsStoredProc.registerOutParameter(7, java.sql.Types.INTEGER) ;
            prepsStoredProc.execute();
            connection.commit(); 
            System.out.println("Generated Identity: " + prepsStoredProc.getInt(7));
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void newBus(){
        //new business
        String name;
        int resID;

        //inputs
        System.out.println("Enter business name");
        name = myObj.nextLine(); //Something goes wrong here, comment this line
        name = myObj.nextLine();
        System.out.println("Enter resID");
        resID = myObj.nextInt();

        System.out.println(
            "Business Name: " + name +
            ", resID: " + resID
        );

        String callNewBus = "{call dbo.insertNewBusiness(?,?,?)}";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
            CallableStatement prepsStoredProc = connection.prepareCall(callNewBus);)
        {
            connection.setAutoCommit(false);
            // Index for paramters start at 1.
            prepsStoredProc.setString(1, name);
            prepsStoredProc.setInt(2, resID);
            //the 3rd parameter is an output parameter
            prepsStoredProc.registerOutParameter(3, java.sql.Types.INTEGER);
            prepsStoredProc.execute();
            connection.commit(); 
            System.out.println("Generated Identity: " + prepsStoredProc.getInt(3));
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void findLocalSupplier(){
        //supplier same zip as custID
        int custID;

        //inputs
        System.out.println("Enter the custID of the customer you want to find a local supplier for");
        custID = myObj.nextInt();

        System.out.println(
            "custID: " + custID
        );

        String findLocalSupplier = "{call dbo.findLocalSupplier(?)}";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
            CallableStatement prepsStoredProc = connection.prepareCall(findLocalSupplier);) {
                prepsStoredProc.setInt(1, custID);
            
                resultSet = prepsStoredProc.executeQuery();

                System.out.println("The supID of suppliers who have the same zip code as your customer are:");
                boolean b;
                while (b = resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
                if(!b){System.out.println("End");}
                
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();

    }

    public static void updateCustomerResidence() {
        int custID;
        String streetNum, street, city, state, zip, phone;

        // Get user input
        System.out.println("Enter your customer ID: ");
        custID = myObj.nextInt();
        System.out.println("Enter your street number: ");
        streetNum = myObj.nextLine();
        streetNum = myObj.nextLine();

        System.out.println("Enter your street name: ");
        street = myObj.nextLine();
        System.out.println("Enter your city: ");
        city = myObj.nextLine();
        System.out.println("Enter your state: ");
        state = myObj.nextLine();
        System.out.println("Enter your zipcode: ");
        zip = myObj.nextLine();
        System.out.println("Enter your phone number: ");
        phone = myObj.nextLine();



        // 7 ? because 7 parameters to stored proc needed. Index for paramters start at 1.
        String callStoredProc = "{call dbo.updateCustomerResidence(?,?,?,?,?,?,?)}";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        CallableStatement prepUpdateCustomerResidence = connection.prepareCall(callStoredProc);) {

            connection.setAutoCommit(false);
            // 3 parameters to stored proc start with a parameter index of 1
            prepUpdateCustomerResidence.setInt(1, custID);
            prepUpdateCustomerResidence.setString(2, streetNum);
            prepUpdateCustomerResidence.setString(3, street);
            prepUpdateCustomerResidence.setString(4, city);
            prepUpdateCustomerResidence.setString(5, state);
            prepUpdateCustomerResidence.setString(6, zip);
            prepUpdateCustomerResidence.setString(7, phone);

            prepUpdateCustomerResidence.execute();
            
            connection.commit();
            System.out.println("Address succesfully changed");
    

        
        }

        // Handle any errors that may have occurred.clear
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOrder(){
        int custID, busID, supID, orderID, prodID, salePrice, quantity;
        String orderDate, shipDate;

        // Get user input
        System.out.println("Enter customer ID: ");
        custID = myObj.nextInt();
        System.out.println("Enter business ID: ");
        busID = myObj.nextInt();
        System.out.println("Enter order date: ");
        orderDate = myObj.nextLine();
        orderDate = myObj.nextLine();
        System.out.println("Enter ship date: ");
        shipDate = myObj.nextLine();
        System.out.println("Enter product ID: ");
        prodID = myObj.nextInt();
        System.out.println("Enter supplier ID: ");
        supID = myObj.nextInt();
        System.out.println("Enter sale price ");
        salePrice = myObj.nextInt();
        System.out.println("Enter quantity: ");
        quantity = myObj.nextInt();

    


        // 7 ? because 7 parameters to stored proc needed. Index for paramters start at 1.
        String callStoredProc1 = "{call dbo.insertCustOrder(?,?,?,?,?)}";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        CallableStatement prepinsertCustOrder =
        connection.prepareCall(callStoredProc1);) {

            prepinsertCustOrder.setInt(1, custID);
            prepinsertCustOrder.setInt(2, busID);
            prepinsertCustOrder.setString(3, orderDate);
            prepinsertCustOrder.setString(4, shipDate);
            prepinsertCustOrder.registerOutParameter(5, java.sql.Types.INTEGER);

            prepinsertCustOrder.execute();
            orderID = prepinsertCustOrder.getInt(5);

            String callStoredProc2 = "{call dbo.insertOrderDetails(?,?,?,?,?)}";
            try (Connection connection2 = DriverManager.getConnection(connectionUrl);
                CallableStatement prepinsertOrderDetails =
                connection.prepareCall(callStoredProc2);) {
                // 3 parameters to stored proc start with a parameter index of 1
                prepinsertOrderDetails.setInt(1, orderID);
                prepinsertOrderDetails.setInt(2, prodID);
                prepinsertOrderDetails.setInt(3, supID);
                prepinsertOrderDetails.setInt(4, salePrice);
                prepinsertOrderDetails.setInt(5, quantity);

                prepinsertOrderDetails.execute();

                System.out.println("Order Insert Succesfully: " + orderID);
            }
            // Handle any errors that may have occurred.clear
            catch (SQLException e) {
            e.printStackTrace();
            }
        
        }
        
        

        // Handle any errors that may have occurred.clear
        catch (SQLException e) {
            e.printStackTrace();
        }

        
    }

    public static void cancelOrder(){
        //supplier same zip as custID
        int orderID;

        //inputs
        System.out.println("Enter the orderID of the order you want to cancel");
        orderID = myObj.nextInt();

        System.out.println(
            "orderID: " + orderID
        );

        String cancelOrder = "{call dbo.cancelOrder(?)}";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
            CallableStatement prepsStoredProc = connection.prepareCall(cancelOrder);) {
                prepsStoredProc.setInt(1, orderID);
            
                prepsStoredProc.execute();
                System.out.println("Order Canceled");
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();

    }

    public static void findTotalSales(){
        
        int custID;
        int busID;

        //inputs
        System.out.println("Enter the custID of the customer");
        custID = myObj.nextInt();
        System.out.println("Enter the busID of the business");
        busID = myObj.nextInt();

        System.out.println(
            "custID: " + custID + "\n" +
            "busID: " + busID
        );

        String findTotalSales = "{call dbo.findTotalSales(?,?)}";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
            CallableStatement prepsStoredProc = connection.prepareCall(findTotalSales);) {
                prepsStoredProc.setInt(1, custID);
                prepsStoredProc.setInt(2, busID);
            
                resultSet = prepsStoredProc.executeQuery();

                System.out.println("CustID      TotalSales");
                boolean b;
                while (b = resultSet.next()) {
                    System.out.println(resultSet.getString(1) + "        "  +
                    resultSet.getString(2));
                }
                if(!b){System.out.println("End");}
                
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();

    }




}//class