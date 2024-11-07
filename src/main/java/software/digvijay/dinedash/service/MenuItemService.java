package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.repository.MenuItemRepository;

import java.util.Optional;

@Service
@Slf4j
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public void saveItem(MenuItem item) {

        try {
            menuItemRepository.save(item);
        } catch (Exception e) {
            log.error("Error while saving {}", item.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void saveNewItem(ObjectId restaurantId, MenuItem item) {
        try {
            item.setRestaurantId(restaurantId);
            menuItemRepository.save(item);
        } catch (Exception e) {
            log.error("Error while saving {}", item.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void updateItem(ObjectId oldId, MenuItem newItem) {
        try {
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(oldId);
            if (optionalMenuItem.isPresent()) {
                MenuItem menuItem = optionalMenuItem.get();
                if (newItem.getName() != null && newItem.getName().isEmpty()) {
                    menuItem.setName(newItem.getName());
                }
                if (newItem.getPrice() != 0) {
                    menuItem.setPrice(newItem.getPrice());
                }
                if (newItem.getDesc() != null && newItem.getDesc().isEmpty()) {
                    menuItem.setDesc(newItem.getDesc());
                }
                saveItem(menuItem);
            }
        } catch (Exception e) {
            log.error("Error while updating {} to {}", oldId, newItem.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void deleteItemById(ObjectId id) {
        try {
            menuItemRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while deleting {}", id, e);
            throw new RuntimeException(e);
        }
    }


    public Optional<MenuItem> getMenuItemById(ObjectId id) {
        return menuItemRepository.findById(id);
    }


}
