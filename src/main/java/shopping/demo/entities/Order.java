package shopping.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order implements Serializable {
    private Integer id;
    private String time;
    private String username;
    private Integer gid;
    private Integer price;
    private String status;
}
