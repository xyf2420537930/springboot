package shopping.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private String username;
    private String password;
    private Integer money;
    private String role;
}
