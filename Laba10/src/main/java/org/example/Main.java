package org.example;

import java.sql.*;

public class Main {
    public static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "ViTaLiK0101");
        Statement statement = connection.createStatement();
        System.out.println("Cписок студентов, сдавших физику, на оценку выше 3:");
        Query1(statement, "Физика");
        System.out.println("\nСредний балл по предмету Физика");
        Query2(statement, "Физика");
        System.out.println("\nСредний балл Барсова Марка Яковлевича:");
        Query3(statement, "Барсов", "Марк", "Яковлевич");
        System.out.println("\nТри премета, которые сдали наибольшее количество студентов:");
        Query4(statement);
        System.out.println("\nСтуденты ,получающие стипендию:");
        Query5(statement);
    }
    public static void Query1(Statement statement, String subject) throws SQLException {
        ResultSet rs = statement.executeQuery("select s.last_name, s.first_name, s.patronimic, p.mark \n" +
                "\tfrom public.Student s join public.Performance p on (s.student_id = p.student_id) \n" +
                "\t\tjoin public.Subject sub on (p.subject_id = sub.subject_id) \n" +
                "\twhere p.mark > 3 and sub.name = '" + subject + "';");
        RSprint(rs);
    }
    public static void Query2(Statement statement, String subject) throws SQLException {
        ResultSet rs = statement.executeQuery("select avg(mark) " +
                "from public.Performance p join public.Subject sub on (p.subject_id = sub.subject_id)" +
                "where sub.name = '" + subject + "';");
        RSprint(rs);
    }
    public static void Query3(Statement statement, String person_F, String person_I, String person_O) throws SQLException {
        ResultSet rs = statement.executeQuery("select avg(mark)" +
                "from public.Performance p join public.student sub on (p.student_id = sub.student_id)" +
                "where sub.first_name = '" + person_I + "' and sub.last_name = '" + person_F + "' and sub.patronimic = '" + person_O + "'");
        RSprint(rs);
    }
    public static void Query4(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("select sub.name, count(*)\n" +
                "from public.Performance p join public.Subject sub on (p.subject_id = sub.subject_id)\n" +
                "where mark > 2\n" +
                "group by sub.name\n" +
                "order by count(*) desc\n" +
                "limit 3;\n");
        RSprint(rs);
    }
    public static void Query5(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("select s.last_name, s.first_name, s.patronimic, p.mark,sub.name \n" +
                "\tfrom public.Student s join public.Performance p on (s.student_id = p.student_id) \n" +
                "\t join public.Subject sub on (sub.subject_id = p.subject_id)\n"+
                "\twhere not exists(select p.student_id from public.Performance p where p.mark <4 and s.student_id = p.student_id) LIMIT 4 OFFSET 2;\n");
        RSprint(rs);
    }

    public static void RSprint(ResultSet rs) throws SQLException {
        int columns = rs.getMetaData().getColumnCount();
        while (rs.next()){
            for(int i = 1; i <= columns; i++){
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }
}