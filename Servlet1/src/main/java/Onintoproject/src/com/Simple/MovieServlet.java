package com.Simple;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MovieServlet extends HttpServlet {

    private Connection getConnection() throws SQLException {
        // Set up your JDBC connection here
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username = "your_username";
        String password = "your_password";
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        // Handle GET /api/v1/longest-duration-movies
        if (pathInfo.equals("/longest-duration-movies")) {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                String query = "SELECT tconst, primaryTitle, runtimeMinutes, genres " +
                        "FROM movies " +
                        "ORDER BY runtimeMinutes DESC " +
                        "FETCH FIRST 10 ROWS ONLY";
                ResultSet rs = stmt.executeQuery(query);
                // Process the ResultSet and generate JSON response
                // Write the response using `out.print()` or any JSON library
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        }

        // Handle GET /api/v1/top-rated-movies
        else if (pathInfo.equals("/top-rated-movies")) {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                String query = "SELECT tconst, primaryTitle, genres, averageRating " +
                        "FROM movies " +
                        "JOIN ratings ON movies.tconst = ratings.tconst " +
                        "WHERE averageRating > 6.0 " +
                        "ORDER BY averageRating DESC";
                ResultSet rs = stmt.executeQuery(query);
                // Process the ResultSet and generate JSON response
                // Write the response using `out.print()` or any JSON library
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        }

        // Handle GET /api/v1/genre-movies-with-subtotals
        else if (pathInfo.equals("/genre-movies-with-subtotals")) {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                String query = "SELECT genres, primaryTitle, SUM(numVotes) AS numVotes " +
                        "FROM movies " +
                        "GROUP BY ROLLUP(genres, primaryTitle) " +
                        "HAVING genres IS NOT NULL OR primaryTitle IS NOT NULL";
                ResultSet rs = stmt.executeQuery(query);
                // Process the ResultSet and generate JSON response
                // Write the response using `out.print()` or any JSON library
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        }

        // Handle other routes
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("Invalid route");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        // Handle POST /api/v1/new-movie
        if (pathInfo.equals("/new-movie")) {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                // Parse JSON payload from request body
                String jsonPayload = request.getReader().lines()
                        .reduce("", (accumulator, actual) -> accumulator + actual);
                // Extract movie data from JSON
                // Example using Gson library:
                Gson gson = new Gson();
                Movie movie = gson.fromJson(jsonPayload, Movie.class);

                // Insert the movie data into the database
                String insertQuery = "INSERT INTO movies (tconst, primaryTitle, runtimeMinutes, genres) " +
                        "VALUES ('" + movie.getTconst() + "', '" + movie.getPrimaryTitle() + "', " +
                        movie.getRuntimeMinutes() + ", '" + movie.getGenres() + "')";
                stmt.executeUpdate(insertQuery);

                // Send success response
                out.print("success");
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Failed to insert movie");
            }
        }

        // Handle POST /api/v1/update-runtime-minutes
        else if (pathInfo.equals("/update-runtime-minutes")) {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                String updateQuery = "UPDATE movies " +
                        "SET runtimeMinutes = " +
                        "CASE " +
                        "WHEN genres = 'Documentary' THEN runtimeMinutes + 15 " +
                        "WHEN genres = 'Animation' THEN runtimeMinutes + 30 " +
                        "ELSE runtimeMinutes + 45 " +
                        "END";
                stmt.executeUpdate(updateQuery);
                // Send success response
                out.print("success");
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Failed to update runtime minutes");
            }
        }

        // Handle other routes
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("Invalid route");
        }
    }
}
