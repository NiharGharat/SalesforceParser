package com.parser.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReaderOutput {
    List<List<String>> rowsResult;
    List<String> dataTypes;
}
