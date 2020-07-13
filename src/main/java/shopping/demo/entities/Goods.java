package shopping.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Goods implements Serializable {
    private Integer id;
    private String name;
    private Integer count;
    private Integer cost;
    private Integer kill;
}
