package software.digvijay.dinedash.dto;


import lombok.*;
import software.digvijay.dinedash.entity.restaurant.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMenuItemDTO {

    private String name;
    private int price;
    private String desc;

    public InputMenuItemDTO(MenuItem menuItem){
        if (menuItem != null) {
            this.name = menuItem.getName();
            this.price = menuItem.getPrice();
            this.desc = menuItem.getDesc();
        }
    }
}