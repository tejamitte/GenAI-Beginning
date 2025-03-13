package com.epam.training.gen.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobilePhones {
  private Integer id;
  private String brand;
  private Boolean available;
  private Long startingPrice;
}
