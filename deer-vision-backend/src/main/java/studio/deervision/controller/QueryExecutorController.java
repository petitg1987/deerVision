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

    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" -H "Content-Type: text/plain" --data "select * from issue where encode(lo_get(data), 'escape') like '%Err%'" "http://localhost:5000/api/admin/query-select"
    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" -H "Content-Type: text/plain" --data "select * from issue where encode(lo_get(data), 'escape') like '%Err%'" "https://backend.deervision.studio/api/admin/query-select"
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

    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" -H "Content-Type: text/plain" --data "delete from issue where encode(lo_get(data), 'escape') like '%GeForce RTX 2080 SUPER%'" "https://backend.deervision.studio/api/admin/query-update"
    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" -H "Content-Type: text/plain" --data "update action_completion_time set action_name='OpenCageDoor' where action_name='Open Cage Door'" "https://backend.deervision.studio/api/admin/query-update"
    //curl -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" -H "Content-Type: text/plain" --data "delete from action_completion_time where app_version='1.0.0'" "https://backend.deervision.studio/api/admin/query-update"
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
