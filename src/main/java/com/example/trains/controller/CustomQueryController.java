package com.example.trains.controller;

import com.example.trains.DTO.CustomQueryRequestDTO;
import com.example.trains.DTO.CustomQueryResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/custom-query")
public class CustomQueryController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping
    public ResponseEntity<?> tryCustomQuery(@RequestBody CustomQueryRequestDTO request) {
        try {
            Connection connection = DriverManager
                    .getConnection(url, user, password);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            if (request.getQuery() != null && !request.getQuery().isEmpty() && request.getQuery().split(" ")[0].equalsIgnoreCase("select")) {
                ResultSet resultSet = statement.executeQuery( request.getQuery() );
                List<String> headers = new ArrayList<>();
                List<Object> rows = new ArrayList<>();
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    String name = resultSet.getMetaData().getColumnLabel(i+1);
                    headers.add(name);
                }


                while ( resultSet.next() ) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                        row.add(resultSet.getObject(i+1));
                    }
                    rows.add(row);
                }
                resultSet.close();
                statement.close();
                connection.commit();
                return ResponseEntity.ok(new CustomQueryResponseDTO(headers, rows));
            }
            statement.execute(request.getQuery());
            statement.close();
            connection.commit();
            return ResponseEntity.ok("Выполнено");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
