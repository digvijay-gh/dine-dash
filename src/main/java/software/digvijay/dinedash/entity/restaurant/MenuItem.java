package software.digvijay.dinedash.entity.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
