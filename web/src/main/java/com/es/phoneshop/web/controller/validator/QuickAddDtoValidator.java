package com.es.phoneshop.web.controller.validator;

import com.es.core.cart.QuickAddDto;
import com.es.core.cart.QuickAddItemDto;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.stock.StockDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.*;

@Component
public class QuickAddDtoValidator implements Validator {

    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return QuickAddDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuickAddDto quickAddDto = (QuickAddDto) o;
        List<String> rejectedModels = new ArrayList<>();
        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        Iterator<QuickAddItemDto> iterator = quickAddDto.getItems().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            QuickAddItemDto item = iterator.next();
            String model = item.getModel();
            String requestedQuantity = item.getQuantity();
            checkModel(model, requestedQuantity, errors, i, rejectedModels);
            if (!model.isEmpty()) {
                int duplicateCount = 0;
                for (int j = 0; j < i; j++) {
                    if (quickAddDto.getItems().get(j).getModel().equals(model)) {
                        duplicateCount++;
                        break;
                    }
                }

                if (duplicateCount > 0) {
                    errors.rejectValue("items['" + i + "'].model", "invalid.model", "Duplicate model");
                    indexesToRemove.add(i);
                }
            }
            i++;
        }
        Collections.reverse(indexesToRemove);
        for (int index : indexesToRemove) {
            quickAddDto.getItems().remove(index);
        }
        rejectedModels.forEach(model -> quickAddDto.getItems().removeIf(item -> item.getModel().equals(model)));
    }

    private void checkModel(String model, String requestedQuantity, Errors errors, int i, List<String> rejectedModels) {

        if (model == null || model.equals("")) {
            rejectedModels.add(model);
            return;
        }
        Optional<Phone> phone = phoneDao.get(model);
        if (phone.isPresent()) {
            checkQuantity(model, requestedQuantity, errors, i, rejectedModels);
        } else {
            errors.rejectValue("items['" + i + "'].model", "invalid.model",
                    "Product not found");
            rejectedModels.add(model);
        }
    }

    private void checkQuantity(String model, String requestedQuantity, Errors errors, int i, List<String> rejectedModels) {
        try {
            long quantity = Long.parseLong(requestedQuantity);

            if (quantity < 1) {
                errors.rejectValue("items['" + i + "'].quantity", "invalid.quantity",
                        "Must be positive amount");
                rejectedModels.add(model);
            }

            int availableStock = stockDao.availableStock(phoneDao.get(model).get().getId());

            if (quantity > availableStock) {
                errors.rejectValue("items['" + i + "'].quantity", "out.of.stock.quantity",
                        "Out of stock");
                rejectedModels.add(model);
            }

        } catch (NumberFormatException ex) {
            errors.rejectValue("items['" + i + "'].quantity", "invalid.quantity",
                    "Invalid quantity");
            rejectedModels.add(model);
        }
    }


}
