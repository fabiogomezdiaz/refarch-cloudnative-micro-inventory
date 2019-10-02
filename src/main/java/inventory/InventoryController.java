package inventory;

import inventory.models.Inventory;
import inventory.models.InventoryRepo;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST Controller to manage Inventory database
 *
 */
@RestController("inventoryController")
public class InventoryController {

	Logger logger =  LoggerFactory.getLogger(InventoryController.class);

	@Autowired
	@Qualifier("inventoryRepo")
	private InventoryRepo itemsRepo;

	/**
	 * @return all items in inventory
	 */
	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	@ResponseBody Iterable<Inventory> getInventory() {
		logger.info("/inventory");
		return itemsRepo.findAll();
	}

	/**
	 * @return all items in inventory
	 */
	@RequestMapping(value = "/items", method = RequestMethod.GET)
	@ResponseBody Iterable<Inventory> getItems()  {
		logger.info("/items");
		return itemsRepo.findAll();
	}

	/**
	 * @return item by id
	 * @throws JSONException
	 */
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
	ResponseEntity<?> getById(@PathVariable long id) throws JSONException {
		logger.info("/items/" + id);
		final Inventory item = itemsRepo.findById(id);
		if (item == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(item);
	}

	/**
	 * @return item(s) containing name
	 * @throws JSONException
	 */
	@RequestMapping(value = "/items/name/{name}", method = RequestMethod.GET)
	@ResponseBody
	List<Inventory> getByName(@PathVariable String name) throws JSONException {
		logger.info("/items/name/" + name);
		return itemsRepo.findByNameContaining(name);
	}
}
