package com.mike.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Repository
public class StudentDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Student> selectAllStudents(){
        String sql = "" +
                "SELECT " +
                "   student_id, " +
                "   first_name, " +
                "   last_name, " +
                "   email, " +
                "   gender " +
                "FROM student";

        return jdbcTemplate.query(sql, mapStudentFromDb());

    }

    int insertStudent(UUID studentId, Student student) {
        String sql = "" +
                "INSERT INTO student (" +
                "student_id, " +
                "first_name, " +
                "last_name, " +
                "email, " +
                "gender) " +
                "VALUES(?, ?, ?, ?, ?::gender)";

        return jdbcTemplate.update(
                sql,
                studentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );
    }

    @SuppressWarnings("ConstantConditions")
    boolean isEmailTaken(String email){
        String sql = "" +
                "SELECT EXISTS(" +
                    "SELECT 1 FROM student " +
                    "WHERE email=?" +
                ")";

        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {email},
                (rs, i) -> rs.getBoolean(1)
        );
    }

    private RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);

            String firstName = resultSet.getString("first_name");

            String lastName = resultSet.getString("last_name");

            String email = resultSet.getString("email");

            String genderStr = resultSet.getString("gender").toUpperCase();
            Student.Gender gender = Student.Gender.valueOf(genderStr);
            return new Student(
                    studentId,
                    firstName,
                    lastName,
                    email,
                    gender);
        };
    }


    public List<StudentCourse> selectAllStudentCourses(UUID studentId) {
        String sql = "" +
                "SELECT " +
                "   course.course_id, " +
                "   student.student_id, " +
                "   course.name," +
                "   course.description, " +
                "   course.department, " +
                "   course.teacher_name," +
                "   student_course.start_date," +
                "   student_course.end_date," +
                "   student_course.grade " +
                "FROM Student " +
                "JOIN student_course USING (student_id) " +
                "JOIN course USING (course_id) " +
                "WHERE student.student_id=?;";

        return jdbcTemplate.query(sql, new Object[]{studentId},
                mapStudentCourseFromDb());
    }

    private RowMapper<StudentCourse> mapStudentCourseFromDb() {
        return (rs, i) -> {
            String courseId = rs.getString("course_id");
            String studentId = rs.getString("student_id");
            LocalDate startDate = rs.getDate("start_date").toLocalDate();
            LocalDate endDate = rs.getDate("end_date").toLocalDate();
            Integer grade = rs.getInt("grade");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String department = rs.getString("department");
            String teacherName = rs.getString("teacher_name");

            return new StudentCourse(UUID.fromString(studentId),
                    UUID.fromString(courseId),
                    name,
                    description,
                    department,
                    teacherName,
                    startDate,
                    endDate,
                    grade);
        };
    }
}
