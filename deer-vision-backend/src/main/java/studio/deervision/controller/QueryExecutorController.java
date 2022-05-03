package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.deervision.service.QueryExecutorService;

import javax.persistence.PersistenceException;
import java.io.PrintWriter;
import java.io.StringWriter;

@RestController
@RequestMapping("/api")
public class QueryExecutorController {

    private final QueryExecutorService queryExecutorService;

    @Autowired
    public QueryExecutorController(QueryExecutorService queryExecutorService) {
        this.queryExecutorService = queryExecutorService;
    }

    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" -H "Content-Type: text/plain" --data "select * from issue" "http://localhost:5000/api/admin/query-select"
    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" -H "Content-Type: text/plain" --data "select * from issue" "https://backend.deervision.studio/api/admin/query-select"
    @PostMapping(value = "/admin/query-select")
    public ResponseEntity<String> querySelect(@RequestBody String sqlQuery) {
        try {
            String result = queryExecutorService.executeSelect(sqlQuery);
            return ResponseEntity.ok(result);
        } catch (PersistenceException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return ResponseEntity.badRequest().body("Error to execute select query '" + sqlQuery + "': " + sw);
        }
    }

    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" -H "Content-Type: text/plain" --data "create index index_levelId on level_completion_time (level_id)" "https://backend.deervision.studio/api/admin/query-update"
    @PostMapping(value = "/admin/query-update")
    public ResponseEntity<String> queryUpdate(@RequestBody String sqlQuery) {
        try {
            int updateCount = queryExecutorService.executeUpdate(sqlQuery);
            return ResponseEntity.ok(String.valueOf(updateCount));
        } catch (PersistenceException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return ResponseEntity.badRequest().body("Error to execute update query '" + sqlQuery + "': " + sw);
        }
    }

}
