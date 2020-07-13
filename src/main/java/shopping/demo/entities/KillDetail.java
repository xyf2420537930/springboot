package shopping.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KillDetail implements Serializable {
    private String username;
    private Integer gid;
    private Integer cost;
}
