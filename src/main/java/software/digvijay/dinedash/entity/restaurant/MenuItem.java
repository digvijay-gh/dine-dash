package software.digvijay.dinedash.entity.restaurant;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import software.digvijay.dinedash.dto.InputMenuItemDTO;
import software.digvijay.dinedash.dto.MenuItemDTO;

@Document(collection = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    private ObjectId itemId;
    private ObjectId restaurantId;
    private String name;
    private int price;
    private String desc;

    public MenuItem(InputMenuItemDTO input){
        this.name=input.getName();
        this.price=input.getPrice();
        this.desc=input.getDesc();
    }
}
