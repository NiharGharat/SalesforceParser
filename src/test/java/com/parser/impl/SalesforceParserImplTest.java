package com.parser.impl;

import com.parser.bean.ApiTypes;
import com.parser.bean.ReaderOutput;
import com.parser.bean.SalesforceInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SalesforceParserImplTest {

    private static String clientSecret;
    private static String clientId;
    private static String userName;
    private static String pwd;
    private static String baseUrl = "https://login.salesforce.com";

    @BeforeAll
    public static void setup() {
        try (FileReader reader = new FileReader("dump/privateConfs.config");
                BufferedReader fileReader = new BufferedReader(reader)) {
            String eachLine = "";
            while ((eachLine = fileReader.readLine()) != null) {
                if (eachLine.toUpperCase(Locale.ROOT).startsWith("SF_CLIENT_SECRET")) {
                    clientSecret = eachLine.replace("SF_CLIENT_SECRET=", "");
                } else if (eachLine.toUpperCase(Locale.ROOT).startsWith("SF_CLIENT_ID")) {
                    clientId = eachLine.replace("SF_CLIENT_ID=", "");
                } else if (eachLine.toUpperCase(Locale.ROOT).startsWith("SF_USERNAME")) {
                    userName = eachLine.replace("SF_USERNAME=", "");
                } else if (eachLine.toUpperCase(Locale.ROOT).startsWith("SF_PWD")) {
                    pwd = eachLine.replace("SF_PWD=", "");
                } else if (eachLine.toUpperCase(Locale.ROOT).startsWith("SF_BASE_URL")) {
                    baseUrl = eachLine.replace("SF_BASE_URL=", "");
                } else {
                    throw new RuntimeException("New mapping needs to exp map to credentials in test case");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unitTest_read_shouldSucceed() throws Exception {
        final String tableName = "Test50Col1000Row";
        SalesforceParser parser = new SalesforceParser();
        SalesforceInput input = SalesforceInput.
                builder()
                .clientSecret(clientSecret)
                .apiTypeRequested(ApiTypes.USER_PWD_API)
                .baseUrl(baseUrl)
                .pwdSecurityToken(pwd)
                .tableName(tableName)
                .clientId(clientId)
                .userName(userName)
                .build();

        final ReaderOutput readerOutput = parser.readDao(input);
        assertEquals(1000 + 1, readerOutput.getRowsResult().size());

        // hellower
        final List<String> headers = readerOutput.getRowsResult().get(0);
        ArrayList<Integer> indexOfHellower = new ArrayList<>();
        // This will be used in value assertion
        Map<String, Integer> indexValueAssertionMap = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            indexValueAssertionMap.put(headers.get(i), i);
            if (headers.get(i).startsWith("hellower")) {
                indexOfHellower.add(i);
            }
        }
        // hellower column only had uniq entires, check if thats the case, for each hellower col
        for (Integer eachI : indexOfHellower) {
            final List<String> collect = readerOutput.getRowsResult().stream().map(e -> e.get(eachI)).filter(Objects::nonNull).collect(Collectors.toList());
            assertNotEquals(0, collect.size());
            assertEquals(collect.size(), collect.stream().distinct().count());
        }

        // TODO
        // This assertions are left todo
        // Now to assert on values
        String hellowerUniqValue = "1";
        Map<String, String> keyValueExpected = new HashMap<>();
        keyValueExpected.put("intCol1__c", "100");
        keyValueExpected.put("strCol1__c", "hadhasdh ahd ha dhah dhasd hahd ahd dkandsnd qn qdnlq n");
        keyValueExpected.put("longCol1__c", "89823423");
        keyValueExpected.put("doubleCol1__c", "4533.34122");
        keyValueExpected.put("spclChar1__c", "dcas AS&d sad1!!!");
        keyValueExpected.put("hello1__c", " dasd");
        keyValueExpected.put("empty1__c", "");
        keyValueExpected.put("driedMix__c", "dad");
        keyValueExpected.put("intCol__c", "23918");

        boolean isFirst = true;
        for (List<String> eachRow : readerOutput.getRowsResult()) {
            if (isFirst) {
                isFirst = false;
                System.out.println("In");
            }
            final OptionalInt min = indexOfHellower.stream().mapToInt(Integer::intValue).min();
            if (eachRow.get(min.getAsInt()).equals(hellowerUniqValue)) {
                System.out.println("Min is " + min.getAsInt());
                final Set<Map.Entry<String, Integer>> entries = indexValueAssertionMap.entrySet();
                for (Map.Entry<String, Integer> eachEntry : entries) {
                    final String expected = eachRow.get(eachEntry.getValue().intValue());
                    //System.out.println("Asserting " + expected);
                    final String key = eachEntry.getKey();
                    assertEquals(expected, keyValueExpected.get(key));
                }
            }
        }
        System.out.println("Done");
    }

    @Test
    public void unitTest_read_moreThan2000_shouldSucceed() throws Exception {
        final String tableName = "Test50Col20000Row";
        final String tableApiName = "Test50Col20000Row__c";
        SalesforceParser parser = new SalesforceParser();
        SalesforceInput input = SalesforceInput.
                builder()
                .clientSecret(clientSecret)
                .apiTypeRequested(ApiTypes.USER_PWD_API)
                .baseUrl(baseUrl)
                .pwdSecurityToken(pwd)
                .tableName(tableName)
                .clientId(clientId)
                .userName(userName)
                .tableApiName(tableApiName)
                .build();

        final ReaderOutput readerOutput = parser.readDao(input);
        assertEquals(20000 + 1, readerOutput.getRowsResult().size());
    }
}
