package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {
    private String itemId;
    private String restaurantId;
    private String name;
    private int price;
    private String desc;

    public MenuItemDTO(MenuItem menuItem){
        if (menuItem != null) {
            this.itemId = menuItem.getItemId().toString(); // Assuming MenuItem has a getItemId() method
            this.restaurantId = menuItem.getRestaurantId().toString(); // Assuming MenuItem has a getRestaurantId() method
            this.name = menuItem.getName();
            this.price = menuItem.getPrice();
            this.desc = menuItem.getDesc();
        }
    }
}
