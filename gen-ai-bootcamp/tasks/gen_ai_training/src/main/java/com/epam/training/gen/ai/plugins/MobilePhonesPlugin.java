package com.epam.training.gen.ai.plugins;

import com.epam.training.gen.ai.model.MobilePhones;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobilePhonesPlugin {

  private final Map<Integer, MobilePhones> mobilePhones = new HashMap<>();

  public MobilePhonesPlugin() {
    mobilePhones.put(1, new MobilePhones(1, "iPhone", true, 45000L));
    mobilePhones.put(2, new MobilePhones(2, "Samsung", true, 25000L));
    mobilePhones.put(3, new MobilePhones(3, "Nokia", true, 3000L));
    mobilePhones.put(4, new MobilePhones(4, "OnePlus", false, 3000L));
  }

  @DefineKernelFunction(
      name = "get_mobile_phones",
      description = "Gets a list of mobile phones, availability and their starting price")
  public List<MobilePhones> getMobilePhones() {
    System.out.println("Getting Mobile Phones");
    return new ArrayList<>(mobilePhones.values());
  }

  @DefineKernelFunction(
      name = "change_availability",
      description = "Changes the availability of mobile phone")
  public MobilePhones changeAvailability(
      @KernelFunctionParameter(name = "id", description = "The ID of the mobile phone to change")
          int id,
      @KernelFunctionParameter(
              name = "isAvailable",
              description = "The new state of the mobile phone")
          boolean isAvailable) {
    System.out.println("Changing mobile phone " + id + " " + isAvailable);
    if (!mobilePhones.containsKey(id)) {
      throw new IllegalArgumentException("Mobile Phone not found . . !");
    }
    mobilePhones.get(id).setAvailable(isAvailable);
    return mobilePhones.get(id);
  }

  @DefineKernelFunction(name = "remove_mobile_phone", description = "Removes the new mobile phone")
  public List<MobilePhones> removeMobilePhone(
      @KernelFunctionParameter(name = "id", description = "The ID of the mobile phone to remove")
          int id) {
    System.out.println("Removing Mobile Phones with ID " + id);
    mobilePhones.remove(id);
    return new ArrayList<>(mobilePhones.values());
  }
}
