package com.example.places.directory.converter;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;

public interface Converter<I, O> {

  /**
   * Converts the given input to the output.
   *
   * @param input the input to convert.
   * @return the converted output.
   */
  O convert(I input);

  /**
   * Converts the given list of inputs to a list of outputs.
   *
   * @param input the list of inputs to convert.
   * @return the set of converted outputs.
   */
  default List<O> convert(List<I> input) {
    if (isEmpty(input)) {
      return emptyList();
    }
    return input.stream().map(this::convert).collect(toList());
  }

}
