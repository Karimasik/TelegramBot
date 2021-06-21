import java.sql.*;

public class DatabaseFilms {
    //Данные для поключения к БД
    private String connectionUrl = "jdbc:mysql://localhost:3306/Movies?serverTimezone=GMT";
    private String userName = "karimas";
    private String password = "Aq321152!";

    public boolean findFilm(String msg) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             PreparedStatement ps = connection.prepareStatement("SELECT Links FROM Movie WHERE Title = '" + msg + "'")){
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return true;
        }
        return false;
    }

    public String addLink(String msg) throws ClassNotFoundException, SQLException {
        String link = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Links FROM Movie WHERE Title = '" + msg + "'");
            while(resultSet.next()) {
                link = resultSet.getString("Links");
            }
        } catch(Exception e) {
        System.out.println("Connection failed");
        e.printStackTrace();
        }
        return link;
    }
}
